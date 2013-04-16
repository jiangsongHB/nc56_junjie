package nc.bs.pub.action;

import java.util.ArrayList;
import java.util.Hashtable;

import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.pub.compiler.AbstractCompiler2;
import nc.impl.so.sointerface.SaleToCRM;
import nc.vo.pub.BusinessException;
import nc.vo.pub.compiler.PfParameterVO;
import nc.vo.scm.crm.CrmSynchroVO;
import nc.vo.uap.pf.PFBusinessException;
/**
 * ��ע�����۶���������
���ݶ���ִ���еĶ�ִ̬����Ķ�ִ̬���ࡣ
 *
 * �������ڣ�(2008-12-31)
 * @author ƽ̨�ű�����
 */
public class N_30_SoBlankout extends AbstractCompiler2 {
private java.util.Hashtable m_methodReturnHas=new java.util.Hashtable();
private Hashtable m_keyHas=null;
/**
 * N_30_SoBlankout ������ע�⡣
 */
public N_30_SoBlankout() {
	super();
}
/*
* ��ע��ƽ̨��д������
* �ӿ�ִ����
*/
public Object runComClass(PfParameterVO vo) throws BusinessException {
try{
	super.m_tmpVo=vo;
	// ####���ű����뺬�з���ֵ,����DLG��PNL������������з���ֵ####
// *************��ƽ̨ȡ���ɸö����������ڲ�����***********
Object inObject = getVo();
// 1,���ȼ�鴫����������Ƿ�Ϸ����Ƿ�Ϊ�ա�
if (!(inObject instanceof nc.vo.so.so001.SaleOrderVO))
	throw new nc.vo.pub.BusinessException(
					nc.bs.ml.NCLangResOnserver.getInstance()
							.getStrByID("sopub", "UPPsopub-000258")/*
																	 * @res
																	 * "������ϣ����������۶������Ͳ�ƥ��"
																	 */);
if (inObject == null)
	throw new nc.vo.pub.BusinessException(
					nc.bs.ml.NCLangResOnserver.getInstance()
							.getStrByID("sopub", "UPPsopub-000259")/*
																	 * @res
																	 * "������ϣ����������۶���û������"
																	 */);
// 2,���ݺϷ���������ת����
nc.vo.so.so001.SaleOrderVO inVO = (nc.vo.so.so001.SaleOrderVO) inObject;
String pk_bill = ((nc.vo.so.so001.SaleorderHVO) inVO.getParentVO())
		.getCsaleid();
String billtype = ((nc.vo.so.so001.SaleorderHVO) inVO.getParentVO())
		.getCreceipttype();
// 3,��ȡ�����������۹�˾�����е����۶���vo��
// nc.vo.so.so001.SaleOrderVO selfVO = inVO.getOrdVOOfSaleCorp();
inObject = null;

Integer iAct = new Integer(
		nc.vo.so.so001.ISaleOrderAction.A_BLANKOUT);
setParameter("iAction", iAct);
String sActionMsg = "";
setParameter("ACTIONMSG", sActionMsg);
// **************************************************************************************************
setParameter("INVO", inVO);
setParameter("PKBILL", pk_bill);
setParameter("BILLTYPE", billtype);
// **************************************************************************************************
Object retObj = null;
// ����˵��:����
runClass("nc.impl.scm.so.pub.DataControlDMO", "lockPkForVo", "&INVO:nc.vo.pub.AggregatedValueObject",vo,m_keyHas,m_methodReturnHas);
if (retObj != null) {
	m_methodReturnHas.put("lockPkForVo", retObj);
}
// ##################################################
try {
	// ####��Ҫ˵�������ɵ�ҵ���������������Ҫ�����޸�####
	// ����˵��:�������
	runClass("nc.impl.scm.so.pub.ParallelCheckDMO", "checkVoNoChanged", "&INVO:nc.vo.pub.AggregatedValueObject",vo,m_keyHas,m_methodReturnHas);
	if (retObj != null) {
		m_methodReturnHas.put("checkVoNoChanged", retObj);
	}
	// ##################################################
	// ####��Ҫ˵�������ɵ�ҵ���������������Ҫ�����޸�####
	// ����˵��:����������
	retObj = runClass("nc.impl.scm.so.pub.CheckStatusDMO", "isDelStatus", "&INVO:nc.vo.pub.AggregatedValueObject",vo,m_keyHas,m_methodReturnHas);
	if (retObj != null) {
		m_methodReturnHas.put("isEditStatus", retObj);
	}
	
	// ##################################################
	// ####��Ҫ˵�������ɵ�ҵ���������������Ҫ�����޸�####
	// ��������____��ʼ____(���������������____����____���ɶԳ���)
	nc.itf.uap.sf.ICreateCorpQueryService corpService = (nc.itf.uap.sf.ICreateCorpQueryService)nc.bs.framework.common.NCLocator.getInstance().lookup(nc.itf.uap.sf.ICreateCorpQueryService.class.getName());
	boolean creditEnabled = corpService.isEnabled(inVO.getPk_corp(), "SO6");
	Object creditObject = null;
	Object creditPara = null;
	if (creditEnabled) {
		// ע�⣺�˴����ܲ���runClassCom�ķ�ʽ���е���
		nc.itf.so.so120.IBillInvokeCreditManager creditManager = (nc.itf.so.so120.IBillInvokeCreditManager) nc.bs.framework.common.NCLocator
				.getInstance().lookup(nc.itf.so.so120.IBillInvokeCreditManager.class.getName());
		nc.vo.so.credit.SOCreditPara para = new nc.vo.so.credit.SOCreditPara(new String[] { inVO.getPrimaryKey() }, null,
				nc.vo.scm.pub.bill.CreditConst.ICREDIT_ACT_DEL, new String[] { inVO.getBizTypeid() }, inVO.getPk_corp(),
				creditManager);
		creditObject = creditManager;
		creditPara = para;
		creditManager.renovateARByHidsBegin(para);
	}
	
	// �������
	nc.bs.scm.plugin.InvokeEventProxy eventproxy = new nc.bs.scm.plugin.InvokeEventProxy("SO","30");
	eventproxy.beforeAction(nc.vo.scm.plugin.Action.DELETE, new  nc.vo.pub.AggregatedValueObject[]{inVO}, null);
	
	// ##################################################
	// ####��Ҫ˵�������ɵ�ҵ���������������Ҫ�����޸�####
	// ����˵��:�������ӿ�
	/**************(V5.5���÷�������--modifyATPBefore)*****************/
	Integer istatus = (Integer) inVO.getParentVO()
			.getAttributeValue("fstatus");
	nc.vo.so.so001.SaleOrderVO atpvo = inVO;
	ArrayList listatpbefore = null;
	// ����Ƿ�������δͨ���ĵ���
	if (istatus != null
			&& istatus.intValue() != nc.ui.pub.bill.BillStatus.NOPASS) {
		if (atpvo != null && atpvo.getBodyVOs() != null
				&& atpvo.getBodyVOs().length > 0) {
			setParameter("ATPVO", atpvo);
			listatpbefore  = (ArrayList)runClass("nc.impl.so.sointerface.SOATP", "modifyATPBefore", "&ATPVO:nc.vo.pub.AggregatedValueObject",vo,m_keyHas,m_methodReturnHas);
		}
	}
	/**************(V5.5���÷�������--modifyATPBefore)*****************/
	
	
	/**############################# add by chenjianhua 2013-04-14 �ر�ʱɾ���뵥���� ########################**/
	runClass( "nc.impl.scm.so.so001.JunJieSoDMO", "freeMdsd", "&INVOS:nc.vo.so.so001.SaleOrderVO[]",vo,m_keyHas,m_methodReturnHas);
	/**############################# add by chenjianhua 2013-04-14 �ر�ʱɾ���뵥���� ########################**/
	
	
	
	// ##################################################
	// ####��Ҫ˵�������ɵ�ҵ���������������Ҫ�����޸�####
	// ����˵��:��д��ͬ������
	runClass("nc.impl.scm.so.pub.OtherInterfaceDMO", "setSaleCT", "&INVO:nc.vo.pub.AggregatedValueObject",vo,m_keyHas,m_methodReturnHas);
	if (retObj != null) {
		m_methodReturnHas.put("setSaleCT", retObj);
	}
	// ##################################################
    // ��дԤ����״̬
	runClass("nc.impl.scm.so.so001.SaleOrderDMO", "setPreOrdStatus", "&INVO:nc.vo.pub.AggregatedValueObject",vo,m_keyHas,m_methodReturnHas);
	// *********���ؽ��******************************************************
    // ##################################################
	// ####��Ҫ˵�������ɵ�ҵ���������������Ҫ�����޸�####
	// ����˵��:���ϱ�����
	retObj = runClass("nc.impl.scm.so.pub.BusinessControlDMO", "setBillBlankOut", "&PKBILL:String,&BILLTYPE:String",vo,m_keyHas,m_methodReturnHas);
	if (retObj != null) {
		m_methodReturnHas.put("setBillBlankOut", retObj);
	}
	// ##################################################
	// ####��Ҫ˵�������ɵ�ҵ���������������Ҫ�����޸�####
	// ����˵��:�ͷŵ��ݺ�
	retObj = runClass("nc.impl.scm.so.pub.CheckValueValidityImpl", "returnBillCodeNoNewTrans", "&INVO:nc.vo.pub.AggregatedValueObject",vo,m_keyHas,m_methodReturnHas);
	if (retObj != null) {
		m_methodReturnHas.put("returnBillNo", retObj);
	}
	// ##################################################
	// ************************�����۶������������**************************************************
	// **************************************************************************************************
	// ####��Ҫ˵�������ɵ�ҵ���������������Ҫ�����޸�####
	// ����˵��:����ʱ���¶���������ϵ
	retObj = runClass("nc.impl.scm.so.so016.BalanceDMO", "updateSoBalanceWhenOrdBlankout", "&INVO:nc.vo.so.so001.SaleOrderVO",vo,m_keyHas,m_methodReturnHas);
	if (retObj != null) {
		m_methodReturnHas.put("updateSoBalanceWhenOrdBlankout", retObj);
	}
	// ##################################################
	// *********���ؽ��******************************************************
	// ##################################################
	// ��д�˻�����
	runClass("nc.impl.scm.so.so001.SaleOrderDMO", "setRetNum", "&INVO:nc.vo.so.so001.SaleOrderVO",vo,m_keyHas,m_methodReturnHas);
	if (retObj != null) {
		m_methodReturnHas.put("setRetNum", retObj);
	}
	// ##################################################
	// ##################################################
	// ȱ���Ǽ�
	runClass("nc.impl.scm.so.so008.OosinfoDMO", "blankFromOrder", "&INVO:nc.vo.so.so001.SaleOrderVO",vo,m_keyHas,m_methodReturnHas);
	if (retObj != null) {
		m_methodReturnHas.put("blankFromOrder", retObj);
	}
	
	// ɾ��ʱ����ǲ��ղɹ����������۶�������Ҫ��дԴ�ɹ�����
	if (inVO.getHeadVO().isCoopped()) {

		String sourceId = (String) inVO.getBodyVOs()[0].getCsourcebillid();
		reWritePO(new String[] { sourceId }, false);

	}
	
	// ####��Ҫ˵�������ɵ�ҵ���������������Ҫ�����޸�####
	// ##################################################
	// ####��Ҫ˵�������ɵ�ҵ���������������Ҫ�����޸�####
	// ����˵��:ATP
	/**************(V5.5���÷�������--modifyATPAfter)*****************/
	if (istatus != null
			&& istatus.intValue() != nc.ui.pub.bill.BillStatus.NOPASS) {
		if (atpvo != null && atpvo.getBodyVOs() != null
				&& atpvo.getBodyVOs().length > 0) {
			setParameter("LISTATPBEFORE", listatpbefore);
			runClass("nc.impl.so.sointerface.SOATP", "modifyATPAfter", "&ATPVO:nc.vo.pub.AggregatedValueObject,&LISTATPBEFORE:java.util.ArrayList",vo,m_keyHas,m_methodReturnHas);
		}
	}
	/**************(V5.5���÷�������--modifyATPAfter)*****************/
	// ##################################################
	// ��¼��־
  String sButtonName = nc.bs.ml.NCLangResOnserver.getInstance()
  .getStrByID("common", "UC001-0000039");/* @res "ɾ��" */
  inVO.insertOperLog(inVO, sActionMsg, getOperator(), sButtonName);
//	setParameter("ACTIONMSG", sActionMsg);
//	runClass("nc.impl.scm.so.pub.DataControlDMO", "insertBusinesslog", "&INVO:nc.vo.pub.AggregatedValueObject,&ACTIONMSG:String,&BUTTONNAME:String,&OPERID:String,&USERDATE:String",vo,m_keyHas,m_methodReturnHas);
	if (retObj != null) {
		m_methodReturnHas.put("insertBusinesslog", retObj);
	}
	// ##################################################
	// ��������____����____(���������������____��ʼ____���ɶԳ���)
	if (creditEnabled) {
		nc.itf.so.so120.IBillInvokeCreditManager creditManager = (nc.itf.so.so120.IBillInvokeCreditManager)creditObject;
		creditManager.renovateARByHidsEnd((nc.vo.so.credit.SOCreditPara)creditPara);
	}
	
	/**********   CRM����   begin **********/  
	nc.bs.logging.Logger.error(">>>>>>>���۶�����"+inVO.getHeadVO().getVreceiptcode()+"��ɾ��ͬ����CRM��ʼ"); 
	new SaleToCRM().synchronizeSO(inVO,CrmSynchroVO.ICRM_DELETE);
	nc.bs.logging.Logger.error(">>>>>>>���۶�����"+inVO.getHeadVO().getVreceiptcode()+"��ɾ��ͬ����CRM����"); 
	/**********   CRM����   end **********/  
	
} catch (Exception e) {
	if (e instanceof BusinessException)
		throw (BusinessException) e;
	throw new BusinessException(e.getMessage());
} 
// *********���ؽ��******************************************************
inVO = null;
pk_bill = null;
billtype = null;
return retObj;
// ************************************************************************
} catch (Exception ex) {
	if (ex instanceof BusinessException)
		throw (BusinessException) ex;
	else 
    throw new PFBusinessException(ex.getMessage(), ex);
}
}
/*
* ��ע��ƽ̨��дԭʼ�ű�
*/
public String getCodeRemark(){
	return "	// ####���ű����뺬�з���ֵ,����DLG��PNL������������з���ֵ####\n// *************��ƽ̨ȡ���ɸö����������ڲ�����***********\nObject inObject = getVo();\n// 1,���ȼ�鴫����������Ƿ�Ϸ����Ƿ�Ϊ�ա�\nif (!(inObject instanceof nc.vo.so.so001.SaleOrderVO))\n	throw new nc.vo.pub.BusinessException(\n					nc.bs.ml.NCLangResOnserver.getInstance()\n							.getStrByID(\"sopub\", \"UPPsopub-000258\")/*\n																	 * @res\n																	 * \"������ϣ����������۶������Ͳ�ƥ��\"\n																	 */);\nif (inObject == null)\n	throw new nc.vo.pub.BusinessException(\n					nc.bs.ml.NCLangResOnserver.getInstance()\n							.getStrByID(\"sopub\", \"UPPsopub-000259\")/*\n																	 * @res\n																	 * \"������ϣ����������۶���û������\"\n																	 */);\n// 2,���ݺϷ���������ת����\nnc.vo.so.so001.SaleOrderVO inVO = (nc.vo.so.so001.SaleOrderVO) inObject;\nString pk_bill = ((nc.vo.so.so001.SaleorderHVO) inVO.getParentVO())\n		.getCsaleid();\nString billtype = ((nc.vo.so.so001.SaleorderHVO) inVO.getParentVO())\n		.getCreceipttype();\n// 3,��ȡ�����������۹�˾�����е����۶���vo��\n// nc.vo.so.so001.SaleOrderVO selfVO = inVO.getOrdVOOfSaleCorp();\ninObject = null;\nInteger iAct = new Integer(\n		nc.vo.so.so001.ISaleOrderAction.A_BLANKOUT);\nsetParameter(\"iAction\", iAct);\n// **************************************************************************************************\nsetParameter(\"INVO\", inVO);\nsetParameter(\"PKBILL\", pk_bill);\nsetParameter(\"BILLTYPE\", billtype);\n// **************************************************************************************************\nObject retObj = null;\n// ����˵��:����\nrunClassCom@\"nc.impl.scm.so.pub.DataControlDMO\", \"lockPkForVo\", \"&INVO:nc.vo.pub.AggregatedValueObject\"@;\nif (retObj != null) {\n	m_methodReturnHas.put(\"lockPkForVo\", retObj);\n}\n// ##################################################\ntry {\n	// ####��Ҫ˵�������ɵ�ҵ���������������Ҫ�����޸�####\n	// ����˵��:�������\n	runClassCom@\"nc.impl.scm.so.pub.ParallelCheckDMO\", \"checkVoNoChanged\", \"&INVO:nc.vo.pub.AggregatedValueObject\"@;\n	if (retObj != null) {\n		m_methodReturnHas.put(\"checkVoNoChanged\", retObj);\n	}\n	// ##################################################\n	// ####��Ҫ˵�������ɵ�ҵ���������������Ҫ�����޸�####\n	// ����˵��:����������\n	retObj = runClassCom@\"nc.impl.scm.so.pub.CheckStatusDMO\", \"isDelStatus\", \"&INVO:nc.vo.pub.AggregatedValueObject\"@;\n	if (retObj != null) {\n		m_methodReturnHas.put(\"isEditStatus\", retObj);\n	}\n	\n	// ##################################################\n	// ####��Ҫ˵�������ɵ�ҵ���������������Ҫ�����޸�####\n	// ��������____��ʼ____(���������������____����____���ɶԳ���)\n	nc.itf.uap.sf.ICreateCorpQueryService corpService = (nc.itf.uap.sf.ICreateCorpQueryService)nc.bs.framework.common.NCLocator.getInstance().lookup(nc.itf.uap.sf.ICreateCorpQueryService.class.getName());\n	boolean creditEnabled = corpService.isEnabled(inVO.getPk_corp(), \"SO6\");\n	Object creditObject = null;\n	Object creditPara = null;\n	if (creditEnabled) {\n		// ע�⣺�˴����ܲ���runClassCom�ķ�ʽ���е���\n		nc.itf.so.so120.IBillInvokeCreditManager creditManager = (nc.itf.so.so120.IBillInvokeCreditManager) nc.bs.framework.common.NCLocator\n				.getInstance().lookup(nc.itf.so.so120.IBillInvokeCreditManager.class.getName());\n		nc.vo.so.credit.SOCreditPara para = new nc.vo.so.credit.SOCreditPara(new String[] { inVO.getPrimaryKey() }, null,\n				nc.vo.scm.pub.bill.CreditConst.ICREDIT_ACT_DEL, new String[] { inVO.getBizTypeid() }, inVO.getPk_corp(),\n				creditManager);\n		creditObject = creditManager;\n		creditPara = para;\n		creditManager.renovateARByHidsBegin(para);\n	}\n	// ##################################################\n	// ####��Ҫ˵�������ɵ�ҵ���������������Ҫ�����޸�####\n	// ����˵��:�������ӿ�\n	/**************(V5.5���÷�������--modifyATPBefore)*****************/\n	Integer istatus = (Integer) inVO.getParentVO()\n			.getAttributeValue(\"fstatus\");\n	nc.vo.so.so001.SaleOrderVO atpvo = inVO;\n	ArrayList listatpbefore = null;\n	// ����Ƿ�������δͨ���ĵ���\n	if (istatus != null\n			&& istatus.intValue() != nc.ui.pub.bill.BillStatus.NOPASS) {\n		if (atpvo != null && atpvo.getBodyVOs() != null\n				&& atpvo.getBodyVOs().length > 0) {\n			setParameter(\"ATPVO\", atpvo);\n			listatpbefore  = (ArrayList)runClassCom@\"nc.impl.so.sointerface.SOATP\", \"modifyATPBefore\", \"&ATPVO:nc.vo.pub.AggregatedValueObject\"@;\n		}\n	}\n	/**************(V5.5���÷�������--modifyATPBefore)*****************/\n	// ##################################################\n	// ####��Ҫ˵�������ɵ�ҵ���������������Ҫ�����޸�####\n	// ����˵��:��д��ͬ������\n	" +
			"runClassCom@\"nc.impl.scm.so.pub.OtherInterfaceDMO\", \"setSaleCT\", \"&INVO:nc.vo.pub.AggregatedValueObject\"@;\n	if (retObj != null) {\n		m_methodReturnHas.put(\"setSaleCT\", retObj);\n	}\n	// ##################################################\n    // ��дԤ����״̬\n	runClassCom@\"nc.impl.scm.so.so001.SaleOrderDMO\", \"setPreOrdStatus\", \"&INVO:nc.vo.pub.AggregatedValueObject\"@;\n	// *********���ؽ��******************************************************\n    // ##################################################\n	// ####��Ҫ˵�������ɵ�ҵ���������������Ҫ�����޸�####\n	// ����˵��:���ϱ�����\n	retObj = runClassCom@\"nc.impl.scm.so.pub.BusinessControlDMO\", \"setBillBlankOut\", \"&PKBILL:String,&BILLTYPE:String\"@;\n	if (retObj != null) {\n		m_methodReturnHas.put(\"setBillBlankOut\", retObj);\n	}\n	// ##################################################\n	// ####��Ҫ˵�������ɵ�ҵ���������������Ҫ�����޸�####\n	// ����˵��:�ͷŵ��ݺ�\n	retObj = runClassCom@\"nc.impl.scm.so.pub.CheckValueValidityImpl\", \"returnBillCodeNoNewTrans\", \"&INVO:nc.vo.pub.AggregatedValueObject\"@;\n	if (retObj != null) {\n		m_methodReturnHas.put(\"returnBillNo\", retObj);\n	}\n	// ##################################################\n	// ************************�����۶������������**************************************************\n	// **************************************************************************************************\n	// ####��Ҫ˵�������ɵ�ҵ���������������Ҫ�����޸�####\n	// ����˵��:����ʱ���¶���������ϵ\n	retObj = runClassCom@\"nc.impl.scm.so.so016.BalanceDMO\", \"updateSoBalanceWhenOrdBlankout\", \"&INVO:nc.vo.so.so001.SaleOrderVO\"@;\n	if (retObj != null) {\n		m_methodReturnHas.put(\"updateSoBalanceWhenOrdBlankout\", retObj);\n	}\n	// ##################################################\n	// *********���ؽ��******************************************************\n	// ##################################################\n	// ��д�˻�����\n	runClassCom@\"nc.impl.scm.so.so001.SaleOrderDMO\", \"setRetNum\", \"&INVO:nc.vo.so.so001.SaleOrderVO\"@;\n	if (retObj != null) {\n		m_methodReturnHas.put(\"setRetNum\", retObj);\n	}\n	// ##################################################\n	// ##################################################\n	// ȱ���Ǽ�\n	runClassCom@\"nc.impl.scm.so.so008.OosinfoDMO\", \"blankFromOrder\", \"&INVO:nc.vo.so.so001.SaleOrderVO\"@;\n	if (retObj != null) {\n		m_methodReturnHas.put(\"blankFromOrder\", retObj);\n	}\n	// ####��Ҫ˵�������ɵ�ҵ���������������Ҫ�����޸�####\n	// ##################################################\n	// ####��Ҫ˵�������ɵ�ҵ���������������Ҫ�����޸�####\n	// ����˵��:ATP\n	/**************(V5.5���÷�������--modifyATPAfter)*****************/\n	if (istatus != null\n			&& istatus.intValue() != nc.ui.pub.bill.BillStatus.NOPASS) {\n		if (atpvo != null && atpvo.getBodyVOs() != null\n				&& atpvo.getBodyVOs().length > 0) {\n			setParameter(\"LISTATPBEFORE\", listatpbefore);\n			runClassCom@\"nc.impl.so.sointerface.SOATP\", \"modifyATPAfter\", \"&ATPVO:nc.vo.pub.AggregatedValueObject,&LISTATPBEFORE:java.util.ArrayList\"@;\n		}\n	}\n	/**************(V5.5���÷�������--modifyATPAfter)*****************/\n	// ##################################################\n	// ��¼��־\n	String sActionMsg = \"���ݺ�Ϊ: \"+inVO.getHeadVO().getVreceiptcode()+\" �����۶���ɾ���ɹ�!\";\n	setParameter(\"ACTIONMSG\", sActionMsg);\n	runClassCom@\"nc.impl.scm.so.pub.DataControlDMO\", \"insertBusinesslog\", \"&INVO:nc.vo.pub.AggregatedValueObject,&ACTIONMSG:String,&BUTTONNAME:String,&OPERID:String,&USERDATE:String\"@;\n	if (retObj != null) {\n		m_methodReturnHas.put(\"insertBusinesslog\", retObj);\n	}\n	// ##################################################\n	// ��������____����____(���������������____��ʼ____���ɶԳ���)\n	if (creditEnabled) {\n		nc.itf.so.so120.IBillInvokeCreditManager creditManager = (nc.itf.so.so120.IBillInvokeCreditManager)creditObject;\n		creditManager.renovateARByHidsEnd((nc.vo.so.credit.SOCreditPara)creditPara);\n	}\n} catch (Exception e) {\n	if (e instanceof BusinessException)\n		throw (BusinessException) e;\n	throw new BusinessException(e.getMessage());\n} \n// *********���ؽ��******************************************************\ninVO = null;\npk_bill = null;\nbilltype = null;\nreturn retObj;\n// ************************************************************************\n";}
/*
* ��ע�����ýű�������HAS
*/
private void setParameter(String key,Object val)	{
	if (m_keyHas==null){
		m_keyHas=new Hashtable();
	}
	if (val!=null)	{
		m_keyHas.put(key,val);
	}
}

/**
 * ɾ��ʱ��д�ɹ�������
 * 
 * @param poID
 * @param isReferred
 * @throws Exception
 */

private void reWritePO(String[] poID, boolean isReferred) throws Exception {
	String opid = InvocationInfoProxy.getInstance().getUserCode();
	nc.itf.po.IOrder bo = (nc.itf.po.IOrder) nc.bs.framework.common.NCLocator.getInstance()
			.lookup(nc.itf.po.IOrder.class.getName());
	bo.updateCoopFlag(isReferred, poID, null, null, opid);
	// bo.updateCoopFlag(isReferred, poID);
}

}
