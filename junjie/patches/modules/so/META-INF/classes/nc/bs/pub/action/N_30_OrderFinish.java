package nc.bs.pub.action;

import nc.vo.pub.pf.PfUtilActionVO;
import nc.bs.pub.compiler.*;
import nc.vo.pub.compiler.PfParameterVO;
import java.math.*;
import java.util.*;
import nc.vo.pub.lang.*;
import nc.bs.pub.pf.PfUtilTools;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.uap.pf.PFBusinessException;
/**
 * ��ע�����۶����Ľ���
���ݶ���ִ���еĶ�ִ̬����Ķ�ִ̬���ࡣ
 *
 * �������ڣ�(2008-12-31)
 * @author ƽ̨�ű�����
 */
public class N_30_OrderFinish extends AbstractCompiler2 {
private java.util.Hashtable m_methodReturnHas=new java.util.Hashtable();
private Hashtable m_keyHas=null;
/**
 * N_30_OrderFinish ������ע�⡣
 */
public N_30_OrderFinish() {
	super();
}
/*
* ��ע��ƽ̨��д������
* �ӿ�ִ����
*/
public Object runComClass(PfParameterVO vo) throws BusinessException {
	
	long s = System.currentTimeMillis();
	
try{
	super.m_tmpVo=vo;
	// ####���ű����뺬�з���ֵ,����DLG��PNL������������з���ֵ####
Object retObj = null;
// *************��ƽ̨ȡ���ɸö����������ڲ�����***********
Object inObject = getVo();
// 1,���ȼ�鴫����������Ƿ�Ϸ����Ƿ�Ϊ�ա�
if (!(inObject instanceof nc.vo.so.so001.SaleOrderVO))
	throw new nc.vo.pub.BusinessException(
			nc.bs.ml.NCLangResOnserver.getInstance().getStrByID(
					"sopub", "UPPsopub-000258")/*
												 * @res
												 * "������ϣ����������۶������Ͳ�ƥ��"
												 */);
if (inObject == null)
	throw new nc.vo.pub.BusinessException(
			nc.bs.ml.NCLangResOnserver.getInstance().getStrByID(
					"sopub", "UPPsopub-000259")/*
												 * @res
												 * "������ϣ����������۶���û������"
												 */);
// 2,���ݺϷ���������ת����
nc.vo.so.so001.SaleOrderVO inVO = (nc.vo.so.so001.SaleOrderVO) inObject;
String pk_bill = ((nc.vo.so.so001.SaleorderHVO) inVO.getParentVO())
		.getCsaleid();
String billtype = ((nc.vo.so.so001.SaleorderHVO) inVO.getParentVO())
		.getCreceipttype();
nc.vo.so.so001.SaleorderBVO[] body = inVO.getBodyVOs();

//���˿��Ӳ����ID
String[] freezebids = null;
List<String> list_bids = new ArrayList<String>();
for (int i = 0, len = body.length; i < len; i++) {
	if (body[i].getCfreezeid()!=null)
		list_bids.add(body[i].getCorder_bid());
}
if (list_bids.size()>0)
	freezebids = list_bids.toArray(new String[list_bids.size()]);

String userid = inVO.getClientLink().getUser();
nc.vo.pub.lang.UFDate logindate = inVO.getClientLink()
		.getLogonDate();
// 3,��ȡ�����������۹�˾�����е����۶���vo��
// nc.vo.so.so001.SaleOrderVO selfVO = inVO.getOrdVOOfSaleCorp();
inObject = null;
Integer iAct = new Integer(nc.vo.so.so001.ISaleOrderAction.A_CLOSE);
setParameter("iAction", iAct);
// **************************************************************************************************
Integer status = new Integer(2);
setParameter("STATUS", status);
setParameter("INVO", inVO);
setParameter("PKBILL", pk_bill);
setParameter("BILLTYPE", billtype);
// setParameter ( "SELFVO",selfVO);
String sActionMsg = "";
String sButtonName = nc.bs.ml.NCLangResOnserver.getInstance()
		.getStrByID("SCMCOMMON", "UPPSCMCommon-000128");/* @res "����" */
// **************************************************************************************************
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
	runClass("nc.impl.scm.so.pub.CheckStatusDMO", "isFinishStatus", "&INVO:nc.vo.pub.AggregatedValueObject",vo,m_keyHas,m_methodReturnHas);
	if (retObj != null) {
		m_methodReturnHas.put("isFinishStatus", retObj);
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
				nc.vo.scm.pub.bill.CreditConst.ICREDIT_ACT_OUTCLOSE, new String[] { inVO.getBizTypeid() }, inVO.getPk_corp(),
				creditManager);
		creditObject = creditManager;
		creditPara = para;
		creditManager.renovateARByHidsBegin(para);
	}
	// ##################################################
	// ####��Ҫ˵�������ɵ�ҵ���������������Ҫ�����޸�####
	// ����˵��:�������ӿ�
	/**************(V5.5���÷�������--modifyATPBefore)*****************/
	nc.vo.so.so001.SaleOrderVO atpvo = inVO;
	ArrayList listatpbefore = null;
	if (atpvo != null && atpvo.getBodyVOs() != null
			&& atpvo.getBodyVOs().length > 0) {
		setParameter("ATPVO", atpvo);
		listatpbefore  = (ArrayList)runClass("nc.impl.so.sointerface.SOATP", "modifyATPBefore", "&ATPVO:nc.vo.pub.AggregatedValueObject",vo,m_keyHas,m_methodReturnHas);
	}
	/**************(V5.5���÷�������--modifyATPBefore)******************/
	if (retObj != null) {
		m_methodReturnHas.put("updateAR", retObj);
	}
	// ##################################################
	// ####��Ҫ˵�������ɵ�ҵ���������������Ҫ�����޸�####
	// ����˵��:��������ʱ���¶���������ϵ
	
	runClass("nc.impl.scm.so.so016.BalanceDMO", "updateSoBalance", "&INVO:nc.vo.so.so001.SaleOrderVO,&iAction:Integer",vo,m_keyHas,m_methodReturnHas);
	if (retObj != null) {
		m_methodReturnHas.put("updateSoBalance", retObj);
	}
	// ##################################################
	// ####��Ҫ˵�������ɵ�ҵ���������������Ҫ�����޸�####
	// ����˵��:��������ʱ���º�ִͬ������
	runClass("nc.impl.scm.so.so001.SaleOrderDMO", "setSaleCTWhenClose", "&INVO:nc.vo.pub.AggregatedValueObject",vo,m_keyHas,m_methodReturnHas);
	if (retObj != null) {
		m_methodReturnHas.put("setSaleCTWhenClose", retObj);
	}
	// ##################################################
	// ####��Ҫ˵�������ɵ�ҵ���������������Ҫ�����޸�####
	// ����˵��:����
	runClass("nc.impl.scm.so.pub.BusinessControlDMO", "setBillFinish", "&PKBILL:String,&BILLTYPE:String",vo,m_keyHas,m_methodReturnHas);
	if (retObj != null) {
		m_methodReturnHas.put("setBillFinish", retObj);
	}
	// ##################################################
	// ####��Ҫ˵�������ɵ�ҵ���������������Ҫ�����޸�####
	// ����˵��:�繫˾ֱ�����۶����г���ر�/�򿪣��������������г���ر�/��
	setParameter("INVOS", new nc.vo.so.so001.SaleOrderVO[]{inVO});
	runClass("nc.impl.scm.so.so001.SaleOrderDMO", "set5XOutEndFlagForAllClose", "&INVOS:nc.vo.so.so001.SaleOrderVO[]",vo,m_keyHas,m_methodReturnHas);
	if (retObj != null) {
		m_methodReturnHas.put("set5XOutEndFlag", retObj);
	}
	// ##################################################
	// ####��Ҫ˵�������ɵ�ҵ���������������Ҫ�����޸�####
	// ����˵��:v5.5���ζ�������ر�--�Զ��ر������������ķ�������ɾ������״̬�ķ�����
	setParameter("INVOS", new nc.vo.so.so001.SaleOrderVO[]{inVO});
	runClass("nc.impl.scm.so.so001.SaleOrderDMO", "processOrderOutEndForAll", "&INVOS:nc.vo.so.so001.SaleOrderVO[]",vo,m_keyHas,m_methodReturnHas);
	if (retObj != null) {
		m_methodReturnHas.put("set5XOutEndFlag", retObj);
	}
	
	
	/**############################# add by chenjianhua 2013-04-14 �ر�ʱɾ���뵥���� ########################**/
	runClass( "nc.impl.scm.so.so001.JunJieSoDMO", "freeMdsd", "&INVOS:nc.vo.so.so001.SaleOrderVO[]",vo,m_keyHas,m_methodReturnHas);
	/**############################# add by chenjianhua 2013-04-14 �ر�ʱɾ���뵥���� ########################**/
	
	
	
	// ##################################################
	//�Զ�������Ӳ����
	if (freezebids!=null){
		nc.itf.ic.service.IICToSO iictoso = (nc.itf.ic.service.IICToSO)nc.bs.framework.common.NCLocator.getInstance().lookup(nc.itf.ic.service.IICToSO.class.getName());
		iictoso.unLockInv(billtype,freezebids,userid,logindate);
	}
	// ##################################################
	/**************(V5.5���÷�������--modifyATPAfter)*****************/
	if (atpvo != null && atpvo.getBodyVOs() != null
			&& atpvo.getBodyVOs().length > 0) {
		setParameter("LISTATPBEFORE", listatpbefore);
		runClass("nc.impl.so.sointerface.SOATP", "modifyATPAfter", "&ATPVO:nc.vo.pub.AggregatedValueObject,&LISTATPBEFORE:java.util.ArrayList",vo,m_keyHas,m_methodReturnHas);
	}
	/**************(V5.5���÷�������--modifyATPAfter)*****************/
	
	// ##################################################
	// ��������____����____(���������������____��ʼ____���ɶԳ���)
	if (creditEnabled) {
		nc.itf.so.so120.IBillInvokeCreditManager creditManager = (nc.itf.so.so120.IBillInvokeCreditManager)creditObject;
		creditManager.renovateARByHidsEnd((nc.vo.so.credit.SOCreditPara)creditPara);
	}
} catch (Exception e) {
	// ##################################################
	// ��¼��־
	nc.impl.scm.so.pub.DataControlDMO datactldmo = new nc.impl.scm.so.pub.DataControlDMO();
	sActionMsg = e.getMessage();
	datactldmo.insertBusinessExceptionlog(inVO, sActionMsg,
			sButtonName, getOperator(),
			getUserDate() != null ? getUserDate().getDate()
					.toString() : null);
	// ##################################################
	if(e instanceof BusinessException)
		throw (BusinessException)e;
	throw new BusinessException(e.getMessage());
} 
// *********���ؽ��******************************************************
inVO = null;
pk_bill = null;
billtype = null;

nc.vo.scm.pub.SCMEnv.out("<====== ���۶��������رգ�N_30_OrderFinish ## ����ʱ["
		+ (System.currentTimeMillis() - s)/1000+"."
		+ (System.currentTimeMillis() - s)%1000+"��]==============>");

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
	return "	// ####���ű����뺬�з���ֵ,����DLG��PNL������������з���ֵ####\nObject retObj = null;\n// *************��ƽ̨ȡ���ɸö����������ڲ�����***********\nObject inObject = getVo();\n// 1,���ȼ�鴫����������Ƿ�Ϸ����Ƿ�Ϊ�ա�\nif (!(inObject instanceof nc.vo.so.so001.SaleOrderVO))\n	throw new nc.vo.pub.BusinessException(\n			nc.bs.ml.NCLangResOnserver.getInstance().getStrByID(\n					\"sopub\", \"UPPsopub-000258\")/*\n												 * @res\n												 * \"������ϣ����������۶������Ͳ�ƥ��\"\n												 */);\nif (inObject == null)\n	throw new nc.vo.pub.BusinessException(\n			nc.bs.ml.NCLangResOnserver.getInstance().getStrByID(\n					\"sopub\", \"UPPsopub-000259\")/*\n												 * @res\n												 * \"������ϣ����������۶���û������\"\n												 */);\n// 2,���ݺϷ���������ת����\nnc.vo.so.so001.SaleOrderVO inVO = (nc.vo.so.so001.SaleOrderVO) inObject;\nString pk_bill = ((nc.vo.so.so001.SaleorderHVO) inVO.getParentVO())\n		.getCsaleid();\nString billtype = ((nc.vo.so.so001.SaleorderHVO) inVO.getParentVO())\n		.getCreceipttype();\nnc.vo.so.so001.SaleorderBVO[] body = inVO.getBodyVOs();\nString[] bids = new String[body.length];\nfor (int i = 0, len = body.length; i < len; i++) {\n	bids[i] = body[i].getCorder_bid();\n}\nString userid = inVO.getClientLink().getUser();\nnc.vo.pub.lang.UFDate logindate = inVO.getClientLink()\n		.getLogonDate();\n// 3,��ȡ�����������۹�˾�����е����۶���vo��\n// nc.vo.so.so001.SaleOrderVO selfVO = inVO.getOrdVOOfSaleCorp();\ninObject = null;\nInteger iAct = new Integer(nc.vo.so.so001.ISaleOrderAction.A_CLOSE);\nsetParameter(\"iAction\", iAct);\n// **************************************************************************************************\nInteger status = new Integer(2);\nsetParameter(\"STATUS\", status);\nsetParameter(\"INVO\", inVO);\nsetParameter(\"PKBILL\", pk_bill);\nsetParameter(\"BILLTYPE\", billtype);\n// setParameter ( \"SELFVO\",selfVO);\nString sActionMsg = \"\";\nString sButtonName = nc.bs.ml.NCLangResOnserver.getInstance()\n		.getStrByID(\"SCMCOMMON\", \"UPPSCMCommon-000128\");/* @res \"����\" */\n// **************************************************************************************************\n// ����˵��:����\nrunClassCom@\"nc.impl.scm.so.pub.DataControlDMO\", \"lockPkForVo\", \"&INVO:nc.vo.pub.AggregatedValueObject\"@;\nif (retObj != null) {\n	m_methodReturnHas.put(\"lockPkForVo\", retObj);\n}\n// ##################################################\ntry {\n	// ####��Ҫ˵�������ɵ�ҵ���������������Ҫ�����޸�####\n	// ����˵��:�������\n	runClassCom@\"nc.impl.scm.so.pub.ParallelCheckDMO\", \"checkVoNoChanged\", \"&INVO:nc.vo.pub.AggregatedValueObject\"@;\n	if (retObj != null) {\n		m_methodReturnHas.put(\"checkVoNoChanged\", retObj);\n	}\n	// ##################################################\n	// ####��Ҫ˵�������ɵ�ҵ���������������Ҫ�����޸�####\n	// ����˵��:����������\n	runClassCom@\"nc.impl.scm.so.pub.CheckStatusDMO\", \"isFinishStatus\", \"&INVO:nc.vo.pub.AggregatedValueObject\"@;\n	if (retObj != null) {\n		m_methodReturnHas.put(\"isFinishStatus\", retObj);\n	}\n	// ##################################################\n	// ####��Ҫ˵�������ɵ�ҵ���������������Ҫ�����޸�####\n	// ��������____��ʼ____(���������������____����____���ɶԳ���)\n	nc.itf.uap.sf.ICreateCorpQueryService corpService = (nc.itf.uap.sf.ICreateCorpQueryService)nc.bs.framework.common.NCLocator.getInstance().lookup(nc.itf.uap.sf.ICreateCorpQueryService.class.getName());\n	boolean creditEnabled = corpService.isEnabled(inVO.getPk_corp(), \"SO6\");\n	Object creditObject = null;\n	Object creditPara = null;\n	if (creditEnabled) {\n		// ע�⣺�˴����ܲ���runClassCom�ķ�ʽ���е���\n		nc.itf.so.so120.IBillInvokeCreditManager creditManager = (nc.itf.so.so120.IBillInvokeCreditManager) nc.bs.framework.common.NCLocator\n			.getInstance().lookup(nc.itf.so.so120.IBillInvokeCreditManager.class.getName());\n		nc.vo.so.credit.SOCreditPara para = new nc.vo.so.credit.SOCreditPara(new String[] { inVO.getPrimaryKey() }, null,\n				nc.vo.scm.pub.bill.CreditConst.ICREDIT_ACT_OUTCLOSE, new String[] { inVO.getBizTypeid() }, inVO.getPk_corp(),\n				creditManager);\n		creditObject = creditManager;\n		creditPara = para;\n		creditManager.renovateARByHidsBegin(para);\n	}\n	// ##################################################\n	// ####��Ҫ˵�������ɵ�ҵ���������������Ҫ�����޸�####\n	// ����˵��:�������ӿ�\n	/**************(V5.5���÷�������--modifyATPBefore)*****************/\n	nc.vo.so.so001.SaleOrderVO atpvo = inVO;\n	ArrayList listatpbefore = null;\n	if (atpvo != null && atpvo.getBodyVOs() != null\n			&& atpvo.getBodyVOs().length > 0) {\n" +
			"		setParameter(\"ATPVO\", atpvo);\n		listatpbefore  = (ArrayList)runClassCom@\"nc.impl.so.sointerface.SOATP\", \"modifyATPBefore\", \"&ATPVO:nc.vo.pub.AggregatedValueObject\"@;\n	}\n	/**************(V5.5���÷�������--modifyATPBefore)******************/\n	if (retObj != null) {\n		m_methodReturnHas.put(\"updateAR\", retObj);\n	}\n	// ##################################################\n	// ####��Ҫ˵�������ɵ�ҵ���������������Ҫ�����޸�####\n	// ����˵��:��������ʱ���¶���������ϵ\n	runClassCom@\"nc.impl.scm.so.so016.BalanceDMO\", \"updateSoBalance\", \"&INVO:nc.vo.so.so001.SaleOrderVO,&iAction:Integer\"@;\n	if (retObj != null) {\n		m_methodReturnHas.put(\"updateSoBalance\", retObj);\n	}\n	// ##################################################\n	// ####��Ҫ˵�������ɵ�ҵ���������������Ҫ�����޸�####\n	// ����˵��:��������ʱ���º�ִͬ������\n	runClassCom@\"nc.impl.scm.so.so001.SaleOrderDMO\", \"setSaleCTWhenClose\", \"&INVO:nc.vo.pub.AggregatedValueObject\"@;\n	if (retObj != null) {\n		m_methodReturnHas.put(\"setSaleCTWhenClose\", retObj);\n	}\n	// ##################################################\n	// ####��Ҫ˵�������ɵ�ҵ���������������Ҫ�����޸�####\n	// ����˵��:����\n	runClassCom@\"nc.impl.scm.so.pub.BusinessControlDMO\", \"setBillFinish\", \"&PKBILL:String,&BILLTYPE:String\"@;\n	if (retObj != null) {\n		m_methodReturnHas.put(\"setBillFinish\", retObj);\n	}\n	// ##################################################\n	// ####��Ҫ˵�������ɵ�ҵ���������������Ҫ�����޸�####\n	// ����˵��:�繫˾ֱ�����۶����г���ر�/�򿪣��������������г���ر�/��\n	setParameter(\"INVOS\", new nc.vo.so.so001.SaleOrderVO[]{inVO});\n	runClassCom@\"nc.impl.scm.so.so001.SaleOrderDMO\", \"set5XOutEndFlagForAllClose\", \"&INVOS:nc.vo.so.so001.SaleOrderVO[]\"@;\n	if (retObj != null) {\n		m_methodReturnHas.put(\"set5XOutEndFlag\", retObj);\n	}\n	// ##################################################\n	// ####��Ҫ˵�������ɵ�ҵ���������������Ҫ�����޸�####\n	// ����˵��:v5.5���ζ�������ر�--�Զ��ر������������ķ�������ɾ������״̬�ķ�����\n	setParameter(\"INVOS\", new nc.vo.so.so001.SaleOrderVO[]{inVO});\n	runClassCom@\"nc.impl.scm.so.so001.SaleOrderDMO\", \"processOrderOutEndForAll\", \"&INVOS:nc.vo.so.so001.SaleOrderVO[]\"@;\n	if (retObj != null) {\n		m_methodReturnHas.put(\"set5XOutEndFlag\", retObj);\n	}\n	// ##################################################\n	//�Զ�������Ӳ����\n	nc.itf.ic.service.IICToSO iictoso = (nc.itf.ic.service.IICToSO)nc.bs.framework.common.NCLocator.getInstance().lookup(nc.itf.ic.service.IICToSO.class.getName());\n	iictoso.unLockInv(billtype,bids,userid,logindate);\n	// ##################################################\n	/**************(V5.5���÷�������--modifyATPAfter)*****************/\n	if (atpvo != null && atpvo.getBodyVOs() != null\n			&& atpvo.getBodyVOs().length > 0) {\n		setParameter(\"LISTATPBEFORE\", listatpbefore);\n		runClassCom@\"nc.impl.so.sointerface.SOATP\", \"modifyATPAfter\", \"&ATPVO:nc.vo.pub.AggregatedValueObject,&LISTATPBEFORE:java.util.ArrayList\"@;\n	}\n	/**************(V5.5���÷�������--modifyATPAfter)*****************/\n	\n	// ##################################################\n	// ��������____����____(���������������____��ʼ____���ɶԳ���)\n	if (creditEnabled) {\n		nc.itf.so.so120.IBillInvokeCreditManager creditManager = (nc.itf.so.so120.IBillInvokeCreditManager)creditObject;\n		creditManager.renovateARByHidsEnd((nc.vo.so.credit.SOCreditPara)creditPara);\n	}\n} catch (Exception e) {\n	// ##################################################\n	// ��¼��־\n	nc.impl.scm.so.pub.DataControlDMO datactldmo = new nc.impl.scm.so.pub.DataControlDMO();\n	sActionMsg = e.getMessage();\n	datactldmo.insertBusinessExceptionlog(inVO, sActionMsg,\n			sButtonName, getOperator(),\n			getUserDate() != null ? getUserDate().getDate()\n					.toString() : null);\n	// ##################################################\n	if(e instanceof BusinessException)\n		throw (BusinessException)e;\n	throw new BusinessException(e.getMessage());\n} \n// *********���ؽ��******************************************************\ninVO = null;\npk_bill = null;\nbilltype = null;\nreturn retObj;\n// ************************************************************************\n";}
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
}
