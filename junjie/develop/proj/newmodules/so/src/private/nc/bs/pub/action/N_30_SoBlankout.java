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
 * 备注：销售订单的作废
单据动作执行中的动态执行类的动态执行类。
 *
 * 创建日期：(2008-12-31)
 * @author 平台脚本生成
 */
public class N_30_SoBlankout extends AbstractCompiler2 {
private java.util.Hashtable m_methodReturnHas=new java.util.Hashtable();
private Hashtable m_keyHas=null;
/**
 * N_30_SoBlankout 构造子注解。
 */
public N_30_SoBlankout() {
	super();
}
/*
* 备注：平台编写规则类
* 接口执行类
*/
public Object runComClass(PfParameterVO vo) throws BusinessException {
try{
	super.m_tmpVo=vo;
	// ####本脚本必须含有返回值,返回DLG和PNL的组件不允许有返回值####
// *************从平台取得由该动作传入的入口参数。***********
Object inObject = getVo();
// 1,首先检查传入参数类型是否合法，是否为空。
if (!(inObject instanceof nc.vo.so.so001.SaleOrderVO))
	throw new nc.vo.pub.BusinessException(
					nc.bs.ml.NCLangResOnserver.getInstance()
							.getStrByID("sopub", "UPPsopub-000258")/*
																	 * @res
																	 * "错误：您希望保存的销售订单类型不匹配"
																	 */);
if (inObject == null)
	throw new nc.vo.pub.BusinessException(
					nc.bs.ml.NCLangResOnserver.getInstance()
							.getStrByID("sopub", "UPPsopub-000259")/*
																	 * @res
																	 * "错误：您希望保存的销售订单没有数据"
																	 */);
// 2,数据合法，把数据转换。
nc.vo.so.so001.SaleOrderVO inVO = (nc.vo.so.so001.SaleOrderVO) inObject;
String pk_bill = ((nc.vo.so.so001.SaleorderHVO) inVO.getParentVO())
		.getCsaleid();
String billtype = ((nc.vo.so.so001.SaleorderHVO) inVO.getParentVO())
		.getCreceipttype();
// 3,获取仅包含本销售公司发货行的销售订单vo。
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
// 方法说明:加锁
runClass("nc.impl.scm.so.pub.DataControlDMO", "lockPkForVo", "&INVO:nc.vo.pub.AggregatedValueObject",vo,m_keyHas,m_methodReturnHas);
if (retObj != null) {
	m_methodReturnHas.put("lockPkForVo", retObj);
}
// ##################################################
try {
	// ####重要说明：生成的业务组件方法尽量不要进行修改####
	// 方法说明:并发检查
	runClass("nc.impl.scm.so.pub.ParallelCheckDMO", "checkVoNoChanged", "&INVO:nc.vo.pub.AggregatedValueObject",vo,m_keyHas,m_methodReturnHas);
	if (retObj != null) {
		m_methodReturnHas.put("checkVoNoChanged", retObj);
	}
	// ##################################################
	// ####重要说明：生成的业务组件方法尽量不要进行修改####
	// 方法说明:并发互斥检查
	retObj = runClass("nc.impl.scm.so.pub.CheckStatusDMO", "isDelStatus", "&INVO:nc.vo.pub.AggregatedValueObject",vo,m_keyHas,m_methodReturnHas);
	if (retObj != null) {
		m_methodReturnHas.put("isEditStatus", retObj);
	}
	
	// ##################################################
	// ####重要说明：生成的业务组件方法尽量不要进行修改####
	// 调用信用____开始____(必须跟“调用信用____结束____”成对出现)
	nc.itf.uap.sf.ICreateCorpQueryService corpService = (nc.itf.uap.sf.ICreateCorpQueryService)nc.bs.framework.common.NCLocator.getInstance().lookup(nc.itf.uap.sf.ICreateCorpQueryService.class.getName());
	boolean creditEnabled = corpService.isEnabled(inVO.getPk_corp(), "SO6");
	Object creditObject = null;
	Object creditPara = null;
	if (creditEnabled) {
		// 注意：此处不能采用runClassCom的方式进行调用
		nc.itf.so.so120.IBillInvokeCreditManager creditManager = (nc.itf.so.so120.IBillInvokeCreditManager) nc.bs.framework.common.NCLocator
				.getInstance().lookup(nc.itf.so.so120.IBillInvokeCreditManager.class.getName());
		nc.vo.so.credit.SOCreditPara para = new nc.vo.so.credit.SOCreditPara(new String[] { inVO.getPrimaryKey() }, null,
				nc.vo.scm.pub.bill.CreditConst.ICREDIT_ACT_DEL, new String[] { inVO.getBizTypeid() }, inVO.getPk_corp(),
				creditManager);
		creditObject = creditManager;
		creditPara = para;
		creditManager.renovateARByHidsBegin(para);
	}
	
	// 插件处理
	nc.bs.scm.plugin.InvokeEventProxy eventproxy = new nc.bs.scm.plugin.InvokeEventProxy("SO","30");
	eventproxy.beforeAction(nc.vo.scm.plugin.Action.DELETE, new  nc.vo.pub.AggregatedValueObject[]{inVO}, null);
	
	// ##################################################
	// ####重要说明：生成的业务组件方法尽量不要进行修改####
	// 方法说明:可用量接口
	/**************(V5.5调用方法更新--modifyATPBefore)*****************/
	Integer istatus = (Integer) inVO.getParentVO()
			.getAttributeValue("fstatus");
	nc.vo.so.so001.SaleOrderVO atpvo = inVO;
	ArrayList listatpbefore = null;
	// 检查是否是审批未通过的单据
	if (istatus != null
			&& istatus.intValue() != nc.ui.pub.bill.BillStatus.NOPASS) {
		if (atpvo != null && atpvo.getBodyVOs() != null
				&& atpvo.getBodyVOs().length > 0) {
			setParameter("ATPVO", atpvo);
			listatpbefore  = (ArrayList)runClass("nc.impl.so.sointerface.SOATP", "modifyATPBefore", "&ATPVO:nc.vo.pub.AggregatedValueObject",vo,m_keyHas,m_methodReturnHas);
		}
	}
	/**************(V5.5调用方法更新--modifyATPBefore)*****************/
	
	
	/**############################# add by chenjianhua 2013-04-14 关闭时删除码单锁定 ########################**/
	runClass( "nc.impl.scm.so.so001.JunJieSoDMO", "freeMdsd", "&INVOS:nc.vo.so.so001.SaleOrderVO[]",vo,m_keyHas,m_methodReturnHas);
	/**############################# add by chenjianhua 2013-04-14 关闭时删除码单锁定 ########################**/
	
	
	
	// ##################################################
	// ####重要说明：生成的业务组件方法尽量不要进行修改####
	// 方法说明:回写合同订货量
	runClass("nc.impl.scm.so.pub.OtherInterfaceDMO", "setSaleCT", "&INVO:nc.vo.pub.AggregatedValueObject",vo,m_keyHas,m_methodReturnHas);
	if (retObj != null) {
		m_methodReturnHas.put("setSaleCT", retObj);
	}
	// ##################################################
    // 回写预订单状态
	runClass("nc.impl.scm.so.so001.SaleOrderDMO", "setPreOrdStatus", "&INVO:nc.vo.pub.AggregatedValueObject",vo,m_keyHas,m_methodReturnHas);
	// *********返回结果******************************************************
    // ##################################################
	// ####重要说明：生成的业务组件方法尽量不要进行修改####
	// 方法说明:作废本单据
	retObj = runClass("nc.impl.scm.so.pub.BusinessControlDMO", "setBillBlankOut", "&PKBILL:String,&BILLTYPE:String",vo,m_keyHas,m_methodReturnHas);
	if (retObj != null) {
		m_methodReturnHas.put("setBillBlankOut", retObj);
	}
	// ##################################################
	// ####重要说明：生成的业务组件方法尽量不要进行修改####
	// 方法说明:释放单据号
	retObj = runClass("nc.impl.scm.so.pub.CheckValueValidityImpl", "returnBillCodeNoNewTrans", "&INVO:nc.vo.pub.AggregatedValueObject",vo,m_keyHas,m_methodReturnHas);
	if (retObj != null) {
		m_methodReturnHas.put("returnBillNo", retObj);
	}
	// ##################################################
	// ************************把销售订单置入参数表。**************************************************
	// **************************************************************************************************
	// ####重要说明：生成的业务组件方法尽量不要进行修改####
	// 方法说明:作废时更新订单核销关系
	retObj = runClass("nc.impl.scm.so.so016.BalanceDMO", "updateSoBalanceWhenOrdBlankout", "&INVO:nc.vo.so.so001.SaleOrderVO",vo,m_keyHas,m_methodReturnHas);
	if (retObj != null) {
		m_methodReturnHas.put("updateSoBalanceWhenOrdBlankout", retObj);
	}
	// ##################################################
	// *********返回结果******************************************************
	// ##################################################
	// 回写退货数量
	runClass("nc.impl.scm.so.so001.SaleOrderDMO", "setRetNum", "&INVO:nc.vo.so.so001.SaleOrderVO",vo,m_keyHas,m_methodReturnHas);
	if (retObj != null) {
		m_methodReturnHas.put("setRetNum", retObj);
	}
	// ##################################################
	// ##################################################
	// 缺货登记
	runClass("nc.impl.scm.so.so008.OosinfoDMO", "blankFromOrder", "&INVO:nc.vo.so.so001.SaleOrderVO",vo,m_keyHas,m_methodReturnHas);
	if (retObj != null) {
		m_methodReturnHas.put("blankFromOrder", retObj);
	}
	
	// 删除时如果是参照采购定单的销售订单，需要回写源采购定单
	if (inVO.getHeadVO().isCoopped()) {

		String sourceId = (String) inVO.getBodyVOs()[0].getCsourcebillid();
		reWritePO(new String[] { sourceId }, false);

	}
	
	// ####重要说明：生成的业务组件方法尽量不要进行修改####
	// ##################################################
	// ####重要说明：生成的业务组件方法尽量不要进行修改####
	// 方法说明:ATP
	/**************(V5.5调用方法更新--modifyATPAfter)*****************/
	if (istatus != null
			&& istatus.intValue() != nc.ui.pub.bill.BillStatus.NOPASS) {
		if (atpvo != null && atpvo.getBodyVOs() != null
				&& atpvo.getBodyVOs().length > 0) {
			setParameter("LISTATPBEFORE", listatpbefore);
			runClass("nc.impl.so.sointerface.SOATP", "modifyATPAfter", "&ATPVO:nc.vo.pub.AggregatedValueObject,&LISTATPBEFORE:java.util.ArrayList",vo,m_keyHas,m_methodReturnHas);
		}
	}
	/**************(V5.5调用方法更新--modifyATPAfter)*****************/
	// ##################################################
	// 记录日志
  String sButtonName = nc.bs.ml.NCLangResOnserver.getInstance()
  .getStrByID("common", "UC001-0000039");/* @res "删除" */
  inVO.insertOperLog(inVO, sActionMsg, getOperator(), sButtonName);
//	setParameter("ACTIONMSG", sActionMsg);
//	runClass("nc.impl.scm.so.pub.DataControlDMO", "insertBusinesslog", "&INVO:nc.vo.pub.AggregatedValueObject,&ACTIONMSG:String,&BUTTONNAME:String,&OPERID:String,&USERDATE:String",vo,m_keyHas,m_methodReturnHas);
	if (retObj != null) {
		m_methodReturnHas.put("insertBusinesslog", retObj);
	}
	// ##################################################
	// 调用信用____结束____(必须跟“调用信用____开始____”成对出现)
	if (creditEnabled) {
		nc.itf.so.so120.IBillInvokeCreditManager creditManager = (nc.itf.so.so120.IBillInvokeCreditManager)creditObject;
		creditManager.renovateARByHidsEnd((nc.vo.so.credit.SOCreditPara)creditPara);
	}
	
	/**********   CRM适配   begin **********/  
	nc.bs.logging.Logger.error(">>>>>>>销售订单【"+inVO.getHeadVO().getVreceiptcode()+"】删除同步到CRM开始"); 
	new SaleToCRM().synchronizeSO(inVO,CrmSynchroVO.ICRM_DELETE);
	nc.bs.logging.Logger.error(">>>>>>>销售订单【"+inVO.getHeadVO().getVreceiptcode()+"】删除同步到CRM结束"); 
	/**********   CRM适配   end **********/  
	
} catch (Exception e) {
	if (e instanceof BusinessException)
		throw (BusinessException) e;
	throw new BusinessException(e.getMessage());
} 
// *********返回结果******************************************************
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
* 备注：平台编写原始脚本
*/
public String getCodeRemark(){
	return "	// ####本脚本必须含有返回值,返回DLG和PNL的组件不允许有返回值####\n// *************从平台取得由该动作传入的入口参数。***********\nObject inObject = getVo();\n// 1,首先检查传入参数类型是否合法，是否为空。\nif (!(inObject instanceof nc.vo.so.so001.SaleOrderVO))\n	throw new nc.vo.pub.BusinessException(\n					nc.bs.ml.NCLangResOnserver.getInstance()\n							.getStrByID(\"sopub\", \"UPPsopub-000258\")/*\n																	 * @res\n																	 * \"错误：您希望保存的销售订单类型不匹配\"\n																	 */);\nif (inObject == null)\n	throw new nc.vo.pub.BusinessException(\n					nc.bs.ml.NCLangResOnserver.getInstance()\n							.getStrByID(\"sopub\", \"UPPsopub-000259\")/*\n																	 * @res\n																	 * \"错误：您希望保存的销售订单没有数据\"\n																	 */);\n// 2,数据合法，把数据转换。\nnc.vo.so.so001.SaleOrderVO inVO = (nc.vo.so.so001.SaleOrderVO) inObject;\nString pk_bill = ((nc.vo.so.so001.SaleorderHVO) inVO.getParentVO())\n		.getCsaleid();\nString billtype = ((nc.vo.so.so001.SaleorderHVO) inVO.getParentVO())\n		.getCreceipttype();\n// 3,获取仅包含本销售公司发货行的销售订单vo。\n// nc.vo.so.so001.SaleOrderVO selfVO = inVO.getOrdVOOfSaleCorp();\ninObject = null;\nInteger iAct = new Integer(\n		nc.vo.so.so001.ISaleOrderAction.A_BLANKOUT);\nsetParameter(\"iAction\", iAct);\n// **************************************************************************************************\nsetParameter(\"INVO\", inVO);\nsetParameter(\"PKBILL\", pk_bill);\nsetParameter(\"BILLTYPE\", billtype);\n// **************************************************************************************************\nObject retObj = null;\n// 方法说明:加锁\nrunClassCom@\"nc.impl.scm.so.pub.DataControlDMO\", \"lockPkForVo\", \"&INVO:nc.vo.pub.AggregatedValueObject\"@;\nif (retObj != null) {\n	m_methodReturnHas.put(\"lockPkForVo\", retObj);\n}\n// ##################################################\ntry {\n	// ####重要说明：生成的业务组件方法尽量不要进行修改####\n	// 方法说明:并发检查\n	runClassCom@\"nc.impl.scm.so.pub.ParallelCheckDMO\", \"checkVoNoChanged\", \"&INVO:nc.vo.pub.AggregatedValueObject\"@;\n	if (retObj != null) {\n		m_methodReturnHas.put(\"checkVoNoChanged\", retObj);\n	}\n	// ##################################################\n	// ####重要说明：生成的业务组件方法尽量不要进行修改####\n	// 方法说明:并发互斥检查\n	retObj = runClassCom@\"nc.impl.scm.so.pub.CheckStatusDMO\", \"isDelStatus\", \"&INVO:nc.vo.pub.AggregatedValueObject\"@;\n	if (retObj != null) {\n		m_methodReturnHas.put(\"isEditStatus\", retObj);\n	}\n	\n	// ##################################################\n	// ####重要说明：生成的业务组件方法尽量不要进行修改####\n	// 调用信用____开始____(必须跟“调用信用____结束____”成对出现)\n	nc.itf.uap.sf.ICreateCorpQueryService corpService = (nc.itf.uap.sf.ICreateCorpQueryService)nc.bs.framework.common.NCLocator.getInstance().lookup(nc.itf.uap.sf.ICreateCorpQueryService.class.getName());\n	boolean creditEnabled = corpService.isEnabled(inVO.getPk_corp(), \"SO6\");\n	Object creditObject = null;\n	Object creditPara = null;\n	if (creditEnabled) {\n		// 注意：此处不能采用runClassCom的方式进行调用\n		nc.itf.so.so120.IBillInvokeCreditManager creditManager = (nc.itf.so.so120.IBillInvokeCreditManager) nc.bs.framework.common.NCLocator\n				.getInstance().lookup(nc.itf.so.so120.IBillInvokeCreditManager.class.getName());\n		nc.vo.so.credit.SOCreditPara para = new nc.vo.so.credit.SOCreditPara(new String[] { inVO.getPrimaryKey() }, null,\n				nc.vo.scm.pub.bill.CreditConst.ICREDIT_ACT_DEL, new String[] { inVO.getBizTypeid() }, inVO.getPk_corp(),\n				creditManager);\n		creditObject = creditManager;\n		creditPara = para;\n		creditManager.renovateARByHidsBegin(para);\n	}\n	// ##################################################\n	// ####重要说明：生成的业务组件方法尽量不要进行修改####\n	// 方法说明:可用量接口\n	/**************(V5.5调用方法更新--modifyATPBefore)*****************/\n	Integer istatus = (Integer) inVO.getParentVO()\n			.getAttributeValue(\"fstatus\");\n	nc.vo.so.so001.SaleOrderVO atpvo = inVO;\n	ArrayList listatpbefore = null;\n	// 检查是否是审批未通过的单据\n	if (istatus != null\n			&& istatus.intValue() != nc.ui.pub.bill.BillStatus.NOPASS) {\n		if (atpvo != null && atpvo.getBodyVOs() != null\n				&& atpvo.getBodyVOs().length > 0) {\n			setParameter(\"ATPVO\", atpvo);\n			listatpbefore  = (ArrayList)runClassCom@\"nc.impl.so.sointerface.SOATP\", \"modifyATPBefore\", \"&ATPVO:nc.vo.pub.AggregatedValueObject\"@;\n		}\n	}\n	/**************(V5.5调用方法更新--modifyATPBefore)*****************/\n	// ##################################################\n	// ####重要说明：生成的业务组件方法尽量不要进行修改####\n	// 方法说明:回写合同订货量\n	" +
			"runClassCom@\"nc.impl.scm.so.pub.OtherInterfaceDMO\", \"setSaleCT\", \"&INVO:nc.vo.pub.AggregatedValueObject\"@;\n	if (retObj != null) {\n		m_methodReturnHas.put(\"setSaleCT\", retObj);\n	}\n	// ##################################################\n    // 回写预订单状态\n	runClassCom@\"nc.impl.scm.so.so001.SaleOrderDMO\", \"setPreOrdStatus\", \"&INVO:nc.vo.pub.AggregatedValueObject\"@;\n	// *********返回结果******************************************************\n    // ##################################################\n	// ####重要说明：生成的业务组件方法尽量不要进行修改####\n	// 方法说明:作废本单据\n	retObj = runClassCom@\"nc.impl.scm.so.pub.BusinessControlDMO\", \"setBillBlankOut\", \"&PKBILL:String,&BILLTYPE:String\"@;\n	if (retObj != null) {\n		m_methodReturnHas.put(\"setBillBlankOut\", retObj);\n	}\n	// ##################################################\n	// ####重要说明：生成的业务组件方法尽量不要进行修改####\n	// 方法说明:释放单据号\n	retObj = runClassCom@\"nc.impl.scm.so.pub.CheckValueValidityImpl\", \"returnBillCodeNoNewTrans\", \"&INVO:nc.vo.pub.AggregatedValueObject\"@;\n	if (retObj != null) {\n		m_methodReturnHas.put(\"returnBillNo\", retObj);\n	}\n	// ##################################################\n	// ************************把销售订单置入参数表。**************************************************\n	// **************************************************************************************************\n	// ####重要说明：生成的业务组件方法尽量不要进行修改####\n	// 方法说明:作废时更新订单核销关系\n	retObj = runClassCom@\"nc.impl.scm.so.so016.BalanceDMO\", \"updateSoBalanceWhenOrdBlankout\", \"&INVO:nc.vo.so.so001.SaleOrderVO\"@;\n	if (retObj != null) {\n		m_methodReturnHas.put(\"updateSoBalanceWhenOrdBlankout\", retObj);\n	}\n	// ##################################################\n	// *********返回结果******************************************************\n	// ##################################################\n	// 回写退货数量\n	runClassCom@\"nc.impl.scm.so.so001.SaleOrderDMO\", \"setRetNum\", \"&INVO:nc.vo.so.so001.SaleOrderVO\"@;\n	if (retObj != null) {\n		m_methodReturnHas.put(\"setRetNum\", retObj);\n	}\n	// ##################################################\n	// ##################################################\n	// 缺货登记\n	runClassCom@\"nc.impl.scm.so.so008.OosinfoDMO\", \"blankFromOrder\", \"&INVO:nc.vo.so.so001.SaleOrderVO\"@;\n	if (retObj != null) {\n		m_methodReturnHas.put(\"blankFromOrder\", retObj);\n	}\n	// ####重要说明：生成的业务组件方法尽量不要进行修改####\n	// ##################################################\n	// ####重要说明：生成的业务组件方法尽量不要进行修改####\n	// 方法说明:ATP\n	/**************(V5.5调用方法更新--modifyATPAfter)*****************/\n	if (istatus != null\n			&& istatus.intValue() != nc.ui.pub.bill.BillStatus.NOPASS) {\n		if (atpvo != null && atpvo.getBodyVOs() != null\n				&& atpvo.getBodyVOs().length > 0) {\n			setParameter(\"LISTATPBEFORE\", listatpbefore);\n			runClassCom@\"nc.impl.so.sointerface.SOATP\", \"modifyATPAfter\", \"&ATPVO:nc.vo.pub.AggregatedValueObject,&LISTATPBEFORE:java.util.ArrayList\"@;\n		}\n	}\n	/**************(V5.5调用方法更新--modifyATPAfter)*****************/\n	// ##################################################\n	// 记录日志\n	String sActionMsg = \"单据号为: \"+inVO.getHeadVO().getVreceiptcode()+\" 的销售订单删除成功!\";\n	setParameter(\"ACTIONMSG\", sActionMsg);\n	runClassCom@\"nc.impl.scm.so.pub.DataControlDMO\", \"insertBusinesslog\", \"&INVO:nc.vo.pub.AggregatedValueObject,&ACTIONMSG:String,&BUTTONNAME:String,&OPERID:String,&USERDATE:String\"@;\n	if (retObj != null) {\n		m_methodReturnHas.put(\"insertBusinesslog\", retObj);\n	}\n	// ##################################################\n	// 调用信用____结束____(必须跟“调用信用____开始____”成对出现)\n	if (creditEnabled) {\n		nc.itf.so.so120.IBillInvokeCreditManager creditManager = (nc.itf.so.so120.IBillInvokeCreditManager)creditObject;\n		creditManager.renovateARByHidsEnd((nc.vo.so.credit.SOCreditPara)creditPara);\n	}\n} catch (Exception e) {\n	if (e instanceof BusinessException)\n		throw (BusinessException) e;\n	throw new BusinessException(e.getMessage());\n} \n// *********返回结果******************************************************\ninVO = null;\npk_bill = null;\nbilltype = null;\nreturn retObj;\n// ************************************************************************\n";}
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

/**
 * 删除时回写采购订单用
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
