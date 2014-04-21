package nc.bs.pub.action;

import java.util.Hashtable;

import nc.bs.framework.common.NCLocator;
import nc.bs.pub.compiler.AbstractCompiler2;
import nc.bs.pub.compiler.IWorkFlowRet;
import nc.impl.so.sointerface.SaleToCRM;
import nc.itf.so.so120.IBillInvokeCreditManager;
import nc.itf.uap.sf.ICreateCorpQueryService;
import nc.ui.scm.so.SaleBillType;
import nc.vo.pub.BusinessException;
import nc.vo.pub.compiler.PfParameterVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.scm.crm.CrmSynchroVO;
import nc.vo.scm.pub.SCMEnv;
import nc.vo.scm.pub.bill.CreditConst;
import nc.vo.so.credit.SOCreditPara;
import nc.vo.so.pub.BusiUtil;
import nc.vo.so.pub.SOGenMethod;
import nc.vo.so.so002.SaleVO;
import nc.vo.so.so002.SaleinvoiceBVO;
import nc.vo.so.so002.SaleinvoiceVO;
import nc.vo.so.so012.SquareVO;

public class N_32_APPROVE extends AbstractCompiler2 {

	private Hashtable<String, Object> m_methodReturnHas;
	private Hashtable m_keyHas;

	public N_32_APPROVE() {
		m_methodReturnHas = new Hashtable<String, Object>();
		m_keyHas = null;
	}

	public Object runComClass(PfParameterVO vo) throws BusinessException {
		try {
			m_tmpVo = vo;
			Object inObject = getVo();
			if (!(inObject instanceof SaleinvoiceVO))
				throw  new BusinessException(nc.bs.ml.NCLangResOnserver.getInstance()
						.getStrByID("sopub", "UPPsopub-000262")/*@res "错误：您希望保存的销售发票类型不匹配"*/);
			if (inObject == null)
				throw new BusinessException(nc.bs.ml.NCLangResOnserver.getInstance()
						.getStrByID("sopub", "UPPsopub-000263")/*@res "错误：您希望保存的销售发票没有数据"*/);
			
			SaleinvoiceVO inVO = (SaleinvoiceVO) inObject;
			SaleinvoiceBVO[] salebody = (SaleinvoiceBVO[]) inVO.getChildrenVO();
			int ilength = salebody.length;

			inObject = null;
			setParameter("INVO", inVO);
			Object retObj = null;
            //并发控制：加锁
			runClass("nc.impl.scm.so.pub.DataControlDMO", "lockPkForVo", "&INVO:nc.vo.pub.AggregatedValueObject", vo,
					m_keyHas, m_methodReturnHas);
			if (retObj != null)
				m_methodReturnHas.put("lockPkForVo", retObj);

            //并发控制：校验时间戳
		    runClass("nc.impl.scm.so.pub.ParallelCheckDMO", "checkVoNoChanged",
						"&INVO:nc.vo.pub.AggregatedValueObject", vo, m_keyHas, m_methodReturnHas);
			if (retObj != null)
				m_methodReturnHas.put("checkVoNoChanged", retObj);
			//单据状态校验
             runClass("nc.impl.scm.so.pub.CheckStatusDMO", "isApproveStatus",
						"&INVO:nc.vo.pub.AggregatedValueObject", vo, m_keyHas, m_methodReturnHas);
			if (retObj != null)
				m_methodReturnHas.put("isApproveStatus", retObj);
             //销售发票审核影响三个应收
             //判断信用管理模块是否启用
			ICreateCorpQueryService icorp = (ICreateCorpQueryService) NCLocator.getInstance().lookup(
						ICreateCorpQueryService.class.getName());
			Hashtable ht = icorp.queryProductEnabled(inVO.getPk_corp(), new String[] { "SO6" });
			boolean bEnableSO6 = ((UFBoolean) ht.get("SO6")).booleanValue();

			/** 更新三个应收 begin* */
			IBillInvokeCreditManager creditManager = null;
			SOCreditPara para = null;
			if (bEnableSO6) {
				String[] sourcehid = new String[ilength];
				String[] biztype = new String[ilength];
				String curbiztype = ((SaleVO) inVO.getParentVO()).getCbiztype();
				String[] sourcebids = new String[ilength];
				for (int i = 0; i < ilength; i++) {
					sourcehid[i] = salebody[i].getCupsourcebillid();
					biztype[i] = curbiztype;
					sourcebids[i] = salebody[i].getCupsourcebillbodyid();
				}
                //获得来源单据类型
				String sourcebilltype = null;
                for(SaleinvoiceBVO invoicebvo:inVO.getItemVOs()){
                    if(null != invoicebvo.getCupreceipttype()
                        && invoicebvo.getCupreceipttype().length() > 0){
                        sourcebilltype = invoicebvo.getCupreceipttype();
                        break;
                     }
                  }
			    int iSourceBillType = -1;
				if (SaleBillType.SaleOrder.equals(sourcebilltype)) {
					iSourceBillType = CreditConst.ICREDIT_BILL_ORDER;
				} else if (SaleBillType.SaleOutStore.equalsIgnoreCase(sourcebilltype)) {
					iSourceBillType = CreditConst.ICREDIT_BILL_OUTGENERAL;
				}
				creditManager = (IBillInvokeCreditManager) nc.bs.framework.common.NCLocator.getInstance().lookup(
							IBillInvokeCreditManager.class.getName());
				para = new SOCreditPara(null, sourcebids, new String[] { inVO.getPrimaryKey() }, null,
							CreditConst.ICREDIT_ACT_ARSUBCHGBYINVOICE, new String[] { inVO.getHeadVO().getCbiztype() },
							inVO.getPk_corp(), CreditConst.ICREDIT_BILL_INVOICE, iSourceBillType, creditManager);
				creditManager.renovateARByHidsBegin(para);
			}
			/** 更新三个应收 begin* */
   
             if(null == inVO.getHeadVO().getDapprovedate()){
				 inVO.getHeadVO().setDapprovedate(SOGenMethod.getBSDate());
              }
             if(null == inVO.getHeadVO().getCapproveid()){
            	 inVO.getHeadVO().setCapproveid(SOGenMethod.getBSUser());
             }
             //调用平台审批流处理机制
			 Object m_sysflowObj = procActionFlow(vo);
				
			 /** 更新三个应收 end* */
			 if (bEnableSO6) {
			   creditManager.renovateARByHidsEnd(para);
			  }
			 /** 更新三个应收 end* */
			 //如果审批未通过，则直接返回；若审批通过会返回null
			 if (m_sysflowObj != null) {
               //需要返回新的时间戳等数据刷新前台数据
				Object obj = runClass("nc.impl.scm.so.so002.SaleinvoiceDMO", "getReturnNewVOFromDB",
							"&INVO:nc.vo.so.so002.SaleinvoiceVO", vo, m_keyHas, m_methodReturnHas);
					((IWorkFlowRet) m_sysflowObj).m_inVo = obj;
				return m_sysflowObj;
			  }

			//劳务折扣类存货在出库单确认应收的模式下，发票审批自动结算传应收
			SaleVO header = (SaleVO) inVO.getParentVO();
			if (new BusiUtil().isICToArap(header.getPk_corp(), header.getCbiztype())) {
					Object oret = runClass("nc.impl.scm.so.so002.SaleinvoiceDMO", "splitDisPart",
							"&INVO:nc.vo.so.so002.SaleinvoiceVO", vo, m_keyHas, m_methodReturnHas);
				if (oret != null) {
					SaleinvoiceVO partVO = (SaleinvoiceVO) oret;
					SquareVO sqVO = null;
					sqVO = (SquareVO) changeData(partVO, "32", "33");
					boolean autocost = new BusiUtil().is32AutoCost(partVO.getPk_corp(), partVO.getHeadVO()
								.getCbiztype());
					//##该组件为单动作处理开始,必须修改参数,如果不置入参数值，系统默认为null##
					//###动作名称-->PFACTION###
					if(autocost)
						setParameter("PFACTION", "AutoBalance");
					else
						setParameter("PFACTION", "AutoIncomeBal");
						//###单据类型-->PFBILLTYPE###
						setParameter("PFBILLTYPE", "33");
						//###当前日期-->PFDATE###
						setParameter("PFDATE", getUserDate().toString());
						//###输入VO-->PFVO###
						setParameter("PFVO", sqVO);
						//###输入对象-->PFUSEROBJ###
						retObj = runClass(
								"nc.bs.pub.pf.PfUtilBO",
								"processAction",
								"&PFACTION:String,&PFBILLTYPE:String,&PFDATE:String,&PFFLOW:nc.vo.pub.pf.PfUtilWorkFlowVO,&PFVO:nc.vo.pub.AggregatedValueObject, &PFUSEROBJ:Object",
								vo, m_keyHas, m_methodReturnHas);
				}
			}
            //判断采购是否启用
			ICreateCorpQueryService myService = (ICreateCorpQueryService) nc.bs.framework.common.NCLocator
						.getInstance().lookup(ICreateCorpQueryService.class.getName());
			boolean bPOStartUp = myService.isEnabled(header.getPk_corp(), "PO");
			if (bPOStartUp){
				runClass("nc.impl.scm.so.so002.SaleinvoiceDMO", "insertSaleData",
							"&INVO:nc.vo.so.so002.SaleinvoiceVO", vo, m_keyHas, m_methodReturnHas);

				if (retObj != null)
					m_methodReturnHas.put("insertSaleData", retObj);
				
				runClass("nc.impl.scm.so.so002.SaleinvoiceDMO", "setInvoiceCost", "&INVO:nc.vo.so.so002.SaleinvoiceVO",
						vo, m_keyHas, m_methodReturnHas);
				if (retObj != null)
					m_methodReturnHas.put("setInvoiceCost", retObj);
				}
			/** 更新流程平台数据 为结算传应收作准备* */
			setVo(inVO);
			//记录日志
			String sActionMsg = "";
			String sButtonName = nc.bs.ml.NCLangResOnserver.getInstance()
				.getStrByID("common", "UC001-0000027");/* @res "审批" */
			inVO.insertOperLog(inVO, sActionMsg, sButtonName);
				 
			/**********   CRM适配   begin **********/  
//			nc.bs.logging.Logger.error(">>>>>>>销售发票【"+inVO.getHeadVO().getVreceiptcode()+"】审核同步到CRM开始"); 
//	        new SaleToCRM().synchronizeSO(inVO, CrmSynchroVO.ICRM_UPDATE);
//	        nc.bs.logging.Logger.error(">>>>>>>销售发票【"+inVO.getHeadVO().getVreceiptcode()+"】审核同步到CRM结束"); 
	        /**********   CRM适配   end **********/ 
			//查询获得当前数据库中最新的时间戳等记录	
			retObj = runClass("nc.impl.scm.so.so002.SaleinvoiceDMO", "getReturnNewVOFromDB",
					 "&INVO:nc.vo.so.so002.SaleinvoiceVO", vo, m_keyHas, m_methodReturnHas);
		   return retObj;
		} catch (Exception ex) {
			SCMEnv.out(">>>>>>>销售发票审核脚本异常："+ex);
			if (ex instanceof BusinessException)
				throw (BusinessException) ex;
			else
				throw new BusinessException(ex);
		}
	}

	public String getCodeRemark() {
		return "\t//####本脚本必须含有返回值,返回DLG和PNL的组件不允许有返回值#### \n\t//*************从平台取得由该动作传入的入口参数。*********** \n\tObject inObject  =getVo (); \n\t//1,首先检查传入参数类型是否合法，是否为空。 \n\tif (! (inObject instanceof nc.vo.so.so002.SaleinvoiceVO)) throw new java.rmi.RemoteException ( \"Remote Call\",new nc.vo.pub.BusinessException ( \"错误：您希望保存的销售发票类型不匹配\")); \n\tif (inObject  == null) throw new java.rmi.RemoteException ( \"Remote Call\",new nc.vo.pub.BusinessException ( \"错误：您希望保存的销售发票没有数据\")); \n\t//2,数据合法，把数据转换。 \n\tnc.vo.so.so002.SaleinvoiceVO inVO  = (nc.vo.so.so002.SaleinvoiceVO)inObject; \n\tinObject  =null; \n\t//************************************************************************************************** \n\tsetParameter ( \"INVO\",inVO); \n\t//************************************************************************************************** \n\tObject retObj  =null; \n              \t//方法说明:加锁\n\tObject bFlag=null;\n\tbFlag=runClassCom@\"nc.bs.so.pub.DataControlDMO\",\"lockPkForVo\",\"&INVO:nc.vo.pub.AggregatedValueObject\"@;\n\t//##################################################\n\ttry{\n\t               //####重要说明：生成的业务组件方法尽量不要进行修改####\n\t               //方法说明:并发检查\n\t               runClassCom@ \"nc.bs.so.pub.ParallelCheckDMO\", \"checkVoNoChanged\", \"&INVO:nc.vo.pub.AggregatedValueObject\"@;\n\t               //##################################################\n\t               //####重要说明：生成的业务组件方法尽量不要进行修改#### \n\t               //方法说明:互斥检查 \n\t               runClassCom@ \"nc.bs.so.pub.CheckStatusDMO\", \"isApproveStatus\", \"&INVO:nc.vo.pub.AggregatedValueObject\"@; \n\t               //################################################## \n\t               //####重要说明：生成的业务组件方法尽量不要进行修改####\n\t               //方法说明:获得采购发票成本\n\t               runClassCom@\"nc.bs.so.so002.SaleinvoiceDMO\",\"setInvoiceCost\",\"&INVO:nc.vo.so.so002.SaleinvoiceVO\"@;\n\t               //##################################################\n\t               //####该组件为单动作工作流处理开始...不能进行修改####\n\t               procActionFlow@@;\n\t               //####该组件为单动作工作流处理结束...不能进行修改####\n\t               //####重要说明：生成的业务组件方法尽量不要进行修改####\n\t               //方法说明:销售发票审核时调用传采购\n\t               runClassCom@\"nc.bs.so.so002.SaleinvoiceDMO\",\"insertSaleData\",\"&INVO:nc.vo.so.so002.SaleinvoiceVO\"@;\n\t               //##################################################\n\t               //####重要说明：生成的业务组件方法尽量不要进行修改####\n\t               //方法说明:由采购发票获得销售成本金额\n\t               runClassCom@\"nc.bs.so.so002.SaleinvoiceDMO\",\"setInvoiceCost\",\"&INVO:nc.vo.so.so002.SaleinvoiceVO\"@;\n\t               //##################################################\n\t}\n\tcatch (Exception e) {\n\t\tif (e instanceof\tRemoteException) throw (RemoteException)e;\n\t\telse throw new RemoteException (e.getMessage());\n\t}\n\tfinally {\n\t\t//解业务锁\n\t\t//####重要说明：生成的业务组件方法尽量不要进行修改####\n\t\t//方法说明:解锁\n\t\tif(bFlag!=null && ((nc.vo.pub.lang.UFBoolean)bFlag).booleanValue()){\n\t\t\trunClassCom@ \"nc.bs.so.pub.DataControlDMO\", \"freePkForVo\", \"&INVO:nc.vo.pub.AggregatedValueObject\"@;\n\t\t\t//##################################################\n\t\t}\n\t}\n              //*********返回结果****************************************************** \n\tinVO  =null; \n\treturn retObj; \n\t//************************************************************************\n";
	}

	private void setParameter(String key, Object val) {
		if (m_keyHas == null)
			m_keyHas = new Hashtable();
		if (val != null)
			m_keyHas.put(key, val);
	}
}