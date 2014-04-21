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
						.getStrByID("sopub", "UPPsopub-000262")/*@res "������ϣ����������۷�Ʊ���Ͳ�ƥ��"*/);
			if (inObject == null)
				throw new BusinessException(nc.bs.ml.NCLangResOnserver.getInstance()
						.getStrByID("sopub", "UPPsopub-000263")/*@res "������ϣ����������۷�Ʊû������"*/);
			
			SaleinvoiceVO inVO = (SaleinvoiceVO) inObject;
			SaleinvoiceBVO[] salebody = (SaleinvoiceBVO[]) inVO.getChildrenVO();
			int ilength = salebody.length;

			inObject = null;
			setParameter("INVO", inVO);
			Object retObj = null;
            //�������ƣ�����
			runClass("nc.impl.scm.so.pub.DataControlDMO", "lockPkForVo", "&INVO:nc.vo.pub.AggregatedValueObject", vo,
					m_keyHas, m_methodReturnHas);
			if (retObj != null)
				m_methodReturnHas.put("lockPkForVo", retObj);

            //�������ƣ�У��ʱ���
		    runClass("nc.impl.scm.so.pub.ParallelCheckDMO", "checkVoNoChanged",
						"&INVO:nc.vo.pub.AggregatedValueObject", vo, m_keyHas, m_methodReturnHas);
			if (retObj != null)
				m_methodReturnHas.put("checkVoNoChanged", retObj);
			//����״̬У��
             runClass("nc.impl.scm.so.pub.CheckStatusDMO", "isApproveStatus",
						"&INVO:nc.vo.pub.AggregatedValueObject", vo, m_keyHas, m_methodReturnHas);
			if (retObj != null)
				m_methodReturnHas.put("isApproveStatus", retObj);
             //���۷�Ʊ���Ӱ������Ӧ��
             //�ж����ù���ģ���Ƿ�����
			ICreateCorpQueryService icorp = (ICreateCorpQueryService) NCLocator.getInstance().lookup(
						ICreateCorpQueryService.class.getName());
			Hashtable ht = icorp.queryProductEnabled(inVO.getPk_corp(), new String[] { "SO6" });
			boolean bEnableSO6 = ((UFBoolean) ht.get("SO6")).booleanValue();

			/** ��������Ӧ�� begin* */
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
                //�����Դ��������
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
			/** ��������Ӧ�� begin* */
   
             if(null == inVO.getHeadVO().getDapprovedate()){
				 inVO.getHeadVO().setDapprovedate(SOGenMethod.getBSDate());
              }
             if(null == inVO.getHeadVO().getCapproveid()){
            	 inVO.getHeadVO().setCapproveid(SOGenMethod.getBSUser());
             }
             //����ƽ̨�������������
			 Object m_sysflowObj = procActionFlow(vo);
				
			 /** ��������Ӧ�� end* */
			 if (bEnableSO6) {
			   creditManager.renovateARByHidsEnd(para);
			  }
			 /** ��������Ӧ�� end* */
			 //�������δͨ������ֱ�ӷ��أ�������ͨ���᷵��null
			 if (m_sysflowObj != null) {
               //��Ҫ�����µ�ʱ���������ˢ��ǰ̨����
				Object obj = runClass("nc.impl.scm.so.so002.SaleinvoiceDMO", "getReturnNewVOFromDB",
							"&INVO:nc.vo.so.so002.SaleinvoiceVO", vo, m_keyHas, m_methodReturnHas);
					((IWorkFlowRet) m_sysflowObj).m_inVo = obj;
				return m_sysflowObj;
			  }

			//�����ۿ������ڳ��ⵥȷ��Ӧ�յ�ģʽ�£���Ʊ�����Զ����㴫Ӧ��
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
					//##�����Ϊ����������ʼ,�����޸Ĳ���,������������ֵ��ϵͳĬ��Ϊnull##
					//###��������-->PFACTION###
					if(autocost)
						setParameter("PFACTION", "AutoBalance");
					else
						setParameter("PFACTION", "AutoIncomeBal");
						//###��������-->PFBILLTYPE###
						setParameter("PFBILLTYPE", "33");
						//###��ǰ����-->PFDATE###
						setParameter("PFDATE", getUserDate().toString());
						//###����VO-->PFVO###
						setParameter("PFVO", sqVO);
						//###�������-->PFUSEROBJ###
						retObj = runClass(
								"nc.bs.pub.pf.PfUtilBO",
								"processAction",
								"&PFACTION:String,&PFBILLTYPE:String,&PFDATE:String,&PFFLOW:nc.vo.pub.pf.PfUtilWorkFlowVO,&PFVO:nc.vo.pub.AggregatedValueObject, &PFUSEROBJ:Object",
								vo, m_keyHas, m_methodReturnHas);
				}
			}
            //�жϲɹ��Ƿ�����
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
			/** ��������ƽ̨���� Ϊ���㴫Ӧ����׼��* */
			setVo(inVO);
			//��¼��־
			String sActionMsg = "";
			String sButtonName = nc.bs.ml.NCLangResOnserver.getInstance()
				.getStrByID("common", "UC001-0000027");/* @res "����" */
			inVO.insertOperLog(inVO, sActionMsg, sButtonName);
				 
			/**********   CRM����   begin **********/  
//			nc.bs.logging.Logger.error(">>>>>>>���۷�Ʊ��"+inVO.getHeadVO().getVreceiptcode()+"�����ͬ����CRM��ʼ"); 
//	        new SaleToCRM().synchronizeSO(inVO, CrmSynchroVO.ICRM_UPDATE);
//	        nc.bs.logging.Logger.error(">>>>>>>���۷�Ʊ��"+inVO.getHeadVO().getVreceiptcode()+"�����ͬ����CRM����"); 
	        /**********   CRM����   end **********/ 
			//��ѯ��õ�ǰ���ݿ������µ�ʱ����ȼ�¼	
			retObj = runClass("nc.impl.scm.so.so002.SaleinvoiceDMO", "getReturnNewVOFromDB",
					 "&INVO:nc.vo.so.so002.SaleinvoiceVO", vo, m_keyHas, m_methodReturnHas);
		   return retObj;
		} catch (Exception ex) {
			SCMEnv.out(">>>>>>>���۷�Ʊ��˽ű��쳣��"+ex);
			if (ex instanceof BusinessException)
				throw (BusinessException) ex;
			else
				throw new BusinessException(ex);
		}
	}

	public String getCodeRemark() {
		return "\t//####���ű����뺬�з���ֵ,����DLG��PNL������������з���ֵ#### \n\t//*************��ƽ̨ȡ���ɸö����������ڲ�����*********** \n\tObject inObject  =getVo (); \n\t//1,���ȼ�鴫����������Ƿ�Ϸ����Ƿ�Ϊ�ա� \n\tif (! (inObject instanceof nc.vo.so.so002.SaleinvoiceVO)) throw new java.rmi.RemoteException ( \"Remote Call\",new nc.vo.pub.BusinessException ( \"������ϣ����������۷�Ʊ���Ͳ�ƥ��\")); \n\tif (inObject  == null) throw new java.rmi.RemoteException ( \"Remote Call\",new nc.vo.pub.BusinessException ( \"������ϣ����������۷�Ʊû������\")); \n\t//2,���ݺϷ���������ת���� \n\tnc.vo.so.so002.SaleinvoiceVO inVO  = (nc.vo.so.so002.SaleinvoiceVO)inObject; \n\tinObject  =null; \n\t//************************************************************************************************** \n\tsetParameter ( \"INVO\",inVO); \n\t//************************************************************************************************** \n\tObject retObj  =null; \n              \t//����˵��:����\n\tObject bFlag=null;\n\tbFlag=runClassCom@\"nc.bs.so.pub.DataControlDMO\",\"lockPkForVo\",\"&INVO:nc.vo.pub.AggregatedValueObject\"@;\n\t//##################################################\n\ttry{\n\t               //####��Ҫ˵�������ɵ�ҵ���������������Ҫ�����޸�####\n\t               //����˵��:�������\n\t               runClassCom@ \"nc.bs.so.pub.ParallelCheckDMO\", \"checkVoNoChanged\", \"&INVO:nc.vo.pub.AggregatedValueObject\"@;\n\t               //##################################################\n\t               //####��Ҫ˵�������ɵ�ҵ���������������Ҫ�����޸�#### \n\t               //����˵��:������ \n\t               runClassCom@ \"nc.bs.so.pub.CheckStatusDMO\", \"isApproveStatus\", \"&INVO:nc.vo.pub.AggregatedValueObject\"@; \n\t               //################################################## \n\t               //####��Ҫ˵�������ɵ�ҵ���������������Ҫ�����޸�####\n\t               //����˵��:��òɹ���Ʊ�ɱ�\n\t               runClassCom@\"nc.bs.so.so002.SaleinvoiceDMO\",\"setInvoiceCost\",\"&INVO:nc.vo.so.so002.SaleinvoiceVO\"@;\n\t               //##################################################\n\t               //####�����Ϊ����������������ʼ...���ܽ����޸�####\n\t               procActionFlow@@;\n\t               //####�����Ϊ�������������������...���ܽ����޸�####\n\t               //####��Ҫ˵�������ɵ�ҵ���������������Ҫ�����޸�####\n\t               //����˵��:���۷�Ʊ���ʱ���ô��ɹ�\n\t               runClassCom@\"nc.bs.so.so002.SaleinvoiceDMO\",\"insertSaleData\",\"&INVO:nc.vo.so.so002.SaleinvoiceVO\"@;\n\t               //##################################################\n\t               //####��Ҫ˵�������ɵ�ҵ���������������Ҫ�����޸�####\n\t               //����˵��:�ɲɹ���Ʊ������۳ɱ����\n\t               runClassCom@\"nc.bs.so.so002.SaleinvoiceDMO\",\"setInvoiceCost\",\"&INVO:nc.vo.so.so002.SaleinvoiceVO\"@;\n\t               //##################################################\n\t}\n\tcatch (Exception e) {\n\t\tif (e instanceof\tRemoteException) throw (RemoteException)e;\n\t\telse throw new RemoteException (e.getMessage());\n\t}\n\tfinally {\n\t\t//��ҵ����\n\t\t//####��Ҫ˵�������ɵ�ҵ���������������Ҫ�����޸�####\n\t\t//����˵��:����\n\t\tif(bFlag!=null && ((nc.vo.pub.lang.UFBoolean)bFlag).booleanValue()){\n\t\t\trunClassCom@ \"nc.bs.so.pub.DataControlDMO\", \"freePkForVo\", \"&INVO:nc.vo.pub.AggregatedValueObject\"@;\n\t\t\t//##################################################\n\t\t}\n\t}\n              //*********���ؽ��****************************************************** \n\tinVO  =null; \n\treturn retObj; \n\t//************************************************************************\n";
	}

	private void setParameter(String key, Object val) {
		if (m_keyHas == null)
			m_keyHas = new Hashtable();
		if (val != null)
			m_keyHas.put(key, val);
	}
}