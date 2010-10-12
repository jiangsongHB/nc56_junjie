package nc.bs.pub.action;

import java.util.Hashtable;

import nc.bs.pub.compiler.AbstractCompiler2;
import nc.vo.pub.BusinessException;
import nc.vo.pub.compiler.PfParameterVO;
import nc.vo.uap.pf.PFBusinessException;
/**
 * 备注：采购发票的审批
单据动作执行中的动态执行类的动态执行类。
 *
 * 创建日期：(2008-10-7)
 * @author 平台脚本生成
 */
public class N_25_FEEAPPROVE extends AbstractCompiler2 {
private java.util.Hashtable m_methodReturnHas=new java.util.Hashtable();
private Hashtable m_keyHas=null;
/**
 * N_25_APPROVE 构造子注解。
 */
public N_25_FEEAPPROVE() {
	super();
}
/*
* 备注：平台编写规则类
* 接口执行类
*/
public Object runComClass(PfParameterVO vo) throws BusinessException {
try{
	super.m_tmpVo=vo;
	Object[] inObject  =getVos ();
	Object inObject1  = getUserObj ();
	Hashtable m_sysHasNoPassAndGonging = null;
	if (inObject == null) throw new nc.vo.pub.BusinessException ( "错误：您希望审批的采购发票没有数据");
	nc.vo.pi.InvoiceVO[] inVO  = (nc.vo.pi.InvoiceVO[])inObject;
	setParameter ( "INVO",inVO);
	setParameter ( "INVO1",inObject1);
  
	Object oLockRet = null;
	try {
		//对采购发票申请业务锁
		oLockRet=runClass( "nc.bs.pu.pub.PubDMO", "setDynamicLockForPksOfVos", "&INVO:nc.vo.pub.AggregatedValueObject[]",vo,m_keyHas,m_methodReturnHas);
  
		//并发控制
		runClass( "nc.bs.pu.pub.PubDMO", "checkVosNoChanged", "&INVO:nc.vo.pub.AggregatedValueObject[]",vo,m_keyHas,m_methodReturnHas);
		runClass( "nc.bs.pu.pub.PubDMO", "isBillStateChanged", "&INVO:nc.vo.pub.AggregatedValueObject[]",vo,m_keyHas,m_methodReturnHas);
  
		m_sysHasNoPassAndGonging = procFlowBacth(vo);
      
		String key[] = new String[inVO.length];
		for(int i = 0; i < key.length; i++) key[i] = inVO[i].getHeadVO().getCinvoiceid();
			setParameter("KEYS",key);
			Hashtable t = (Hashtable)runClass( "nc.bs.pi.InvoiceDMO", "queryForSaveAuditBatch", "&KEYS:String[]",vo,m_keyHas,m_methodReturnHas);
			if(t != null && t.size() > 0){
				for(int i = 0; i < inVO.length; i++){
					java.util.ArrayList list = (java.util.ArrayList)t.get(inVO[i].getHeadVO().getCinvoiceid());
					inVO[i].getHeadVO().setDauditdate((nc.vo.pub.lang.UFDate)list.get(0));
					inVO[i].getHeadVO().setCauditpsn((String)list.get(1));
 					inVO[i].getHeadVO().setIbillstatus((Integer)list.get(2));
					inVO[i].getHeadVO().setTs((String)list.get(3));
				}
			}
	}catch (Exception e) {
		nc.bs.pu.pub.PubDMO.throwBusinessException("nc.bs.pub.action.N_25_APPROVE",e);
	}
    
	setParameter ( "PFDATE",getUserDate().toString());
	//审批时自动结算
	Object retJS = runClass("nc.bs.pi.InvoiceImpl","doSettleArray","nc.vo.pi.InvoiceVO[]:25,&PFDATE:String",vo,m_keyHas,m_methodReturnHas);
    
	//V55 1、  注释掉过滤掉虚拟发票功能;2、 注释掉过滤掉冲减暂估应付功能
	//方法说明:滤掉虚拟发票,返回非虚拟发票数组
	Object filteredObj = runClass("nc.bs.pi.InvoiceImpl","filterVirtualInvoice","nc.vo.pi.InvoiceVO[]:25",vo,m_keyHas,m_methodReturnHas);
  
	//滤掉虚拟发票后，组织暂估应付冲减VO, 调用应付的调差接口, 冲减暂估应付
	if (filteredObj != null){
		nc.vo.pub.lang.UFBoolean bSucceed = new nc.vo.pub.lang.UFBoolean(false);
		nc.vo.pi.InvoiceVO[] filteredVOs = (nc.vo.pi.InvoiceVO[]) filteredObj;
		if(retJS != null){
			java.util.ArrayList list = (java.util.ArrayList)retJS;
			if(list != null && list.size() > 1){
				bSucceed = (nc.vo.pub.lang.UFBoolean)list.get(1);
				if(bSucceed.booleanValue()){
					//结算成功后,设置累计结算数量和累计结算金额,方便后续的暂估应付冲减
					for(int i = 0; i < filteredVOs.length; i++){
						nc.vo.pi.InvoiceItemVO bodyVO[] = filteredVOs[i].getBodyVO();
						for(int j = 0; j < bodyVO.length; j++){
							bodyVO[j].setNaccumsettnum(bodyVO[j].getNinvoicenum());
							bodyVO[j].setNaccumsettmny(bodyVO[j].getNmoney());
						}
					}
				}
			}
		}
		//生成暂估应付红冲单
		//add by QuSida (佛山骏杰) 2010-9-25
		nc.bs.pi.InvoiceImpl zgyf = new nc.bs.pi.InvoiceImpl();
		zgyf.adjustForFeeZGYF(filteredVOs);
    	}

	//写操作日志 
  setParameter("BILLTYPE", "25");
  setParameter("ACTIONNAME", "审批");
  runClass("nc.bs.pu.pub.PubImpl","insertOperateLog","&INVO:nc.vo.pub.AggregatedValueObject[],&ACTIONNAME:java.lang.String,&BILLTYPE:java.lang.String",vo,m_keyHas,m_methodReturnHas);
  
	nc.bs.pub.compiler.BatchWorkflowRet bwr = new nc.bs.pub.compiler.BatchWorkflowRet();
	bwr.setNoPassAndGoing(m_sysHasNoPassAndGonging);
	bwr.setUserObj(getVos()); 
      	//
	return new Object[]{bwr};
} catch (Exception ex) {
	if (ex instanceof BusinessException)
		throw (BusinessException) ex;
	else 
    throw new PFBusinessException(ex.getMessage(), ex);
}
}
/*
* 备注：平台编写原始脚本
*/
public String getCodeRemark(){
	return "	super.m_tmpVo=vo;\n Object[] inObject  =getVos ();\n  Object inObject1  = getUserObj ();\n  Hashtable m_sysHasNoPassAndGonging = null;\n  if (inObject == null) throw new nc.vo.pub.BusinessException ( \"错误：您希望审批的采购发票没有数据\");\n nc.vo.pi.InvoiceVO[] inVO  = (nc.vo.pi.InvoiceVO[])inObject;\n  setParameter ( \"INVO\",inVO);\n  setParameter ( \"INVO1\",inObject1);\n  \n  Object oLockRet = null;\n try {\n   //对采购发票申请业务锁\n    oLockRet=runClass( \"nc.bs.pu.pub.PubDMO\", \"setDynamicLockForPksOfVos\", \"&INVO:nc.vo.pub.AggregatedValueObject[]\",vo,m_keyHas,m_methodReturnHas);\n  \n   //并发控制\n    runClass( \"nc.bs.pu.pub.PubDMO\", \"checkVosNoChanged\", \"&INVO:nc.vo.pub.AggregatedValueObject[]\",vo,m_keyHas,m_methodReturnHas);\n   runClass( \"nc.bs.pu.pub.PubDMO\", \"isBillStateChanged\", \"&INVO:nc.vo.pub.AggregatedValueObject[]\",vo,m_keyHas,m_methodReturnHas);\n  \n    m_sysHasNoPassAndGonging = procFlowBacth(vo);\n      \n   String key[] = new String[inVO.length];\n   for(int i = 0; i < key.length; i++) key[i] = inVO[i].getHeadVO().getCinvoiceid();\n     setParameter(\"KEYS\",key);\n     Hashtable t = (Hashtable)runClass( \"nc.bs.pi.InvoiceDMO\", \"queryForSaveAuditBatch\", \"&KEYS:String[]\",vo,m_keyHas,m_methodReturnHas);\n      if(t != null && t.size() > 0){\n        for(int i = 0; i < inVO.length; i++){\n         java.util.ArrayList list = (java.util.ArrayList)t.get(inVO[i].getHeadVO().getCinvoiceid());\n         inVO[i].getHeadVO().setDauditdate((nc.vo.pub.lang.UFDate)list.get(0));\n          inVO[i].getHeadVO().setCauditpsn((String)list.get(1));\n          inVO[i].getHeadVO().setIbillstatus((Integer)list.get(2));\n         inVO[i].getHeadVO().setTs((String)list.get(3));\n       }\n     }\n }catch (Exception e) {\n    nc.bs.pu.pub.PubDMO.throwBusinessException(\"nc.bs.pub.action.N_25_APPROVE\",e);\n  }\n    \n setParameter ( \"PFDATE\",getUserDate().toString());\n  //审批时自动结算\n Object retJS = runClass(\"nc.bs.pi.InvoiceImpl\",\"doSettleArray\",\"nc.vo.pi.InvoiceVO[]:25,&PFDATE:String\",vo,m_keyHas,m_methodReturnHas);\n    \n //V55 1、  注释掉过滤掉虚拟发票功能;2、 注释掉过滤掉冲减暂估应付功能\n  //方法说明:滤掉虚拟发票,返回非虚拟发票数组\n Object filteredObj = runClass(\"nc.bs.pi.InvoiceImpl\",\"filterVirtualInvoice\",\"nc.vo.pi.InvoiceVO[]:25\",vo,m_keyHas,m_methodReturnHas);\n  \n //滤掉虚拟发票后，组织暂估应付冲减VO, 调用应付的调差接口, 冲减暂估应付\n if (filteredObj != null){\n   nc.vo.pub.lang.UFBoolean bSucceed = new nc.vo.pub.lang.UFBoolean(false);\n    nc.vo.pi.InvoiceVO[] filteredVOs = (nc.vo.pi.InvoiceVO[]) filteredObj;\n    if(retJS != null){\n      java.util.ArrayList list = (java.util.ArrayList)retJS;\n      if(list != null && list.size() > 1){\n        bSucceed = (nc.vo.pub.lang.UFBoolean)list.get(1);\n       if(bSucceed.booleanValue()){\n          //结算成功后,设置累计结算数量和累计结算金额,方便后续的暂估应付冲减\n         for(int i = 0; i < filteredVOs.length; i++){\n            nc.vo.pi.InvoiceItemVO bodyVO[] = filteredVOs[i].getBodyVO();\n           for(int j = 0; j < bodyVO.length; j++){\n             bodyVO[j].setNaccumsettnum(bodyVO[j].getNinvoicenum());\n             bodyVO[j].setNaccumsettmny(bodyVO[j].getNmoney());\n            }\n         }\n       }\n     }\n   }\n     }\n //写操作日志 \n  setParameter(\"BILLTYPE\", \"25\");\n  setParameter(\"ACTIONNAME\", \"审批\");\n  runClass(\"nc.bs.pu.pub.PubImpl\",\"insertOperateLog\",\"&INVO:nc.vo.pub.AggregatedValueObject[],&ACTIONNAME:java.lang.String,&BILLTYPE:java.lang.String\",vo,m_keyHas,m_methodReturnHas);\n  \n  nc.bs.pub.compiler.BatchWorkflowRet bwr = new nc.bs.pub.compiler.BatchWorkflowRet();\n  bwr.setNoPassAndGoing(m_sysHasNoPassAndGonging);\n  bwr.setUserObj(getVos()); \n        //\n  return new Object[]{bwr};\n";}
/*
* 备注：设置脚本变量的HAS
*/
private void setParameter(String key,Object val)	{
	if (m_keyHas==null){
		m_keyHas=new Hashtable();
	}
	if (val!=null)	{
		m_keyHas.put(key,val);
	}
}
}

