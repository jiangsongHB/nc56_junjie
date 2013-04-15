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
 * 备注：销售订单的结束
单据动作执行中的动态执行类的动态执行类。
 *
 * 创建日期：(2008-12-31)
 * @author 平台脚本生成
 */
public class N_30_OrderFinish extends AbstractCompiler2 {
private java.util.Hashtable m_methodReturnHas=new java.util.Hashtable();
private Hashtable m_keyHas=null;
/**
 * N_30_OrderFinish 构造子注解。
 */
public N_30_OrderFinish() {
	super();
}
/*
* 备注：平台编写规则类
* 接口执行类
*/
public Object runComClass(PfParameterVO vo) throws BusinessException {
	
	long s = System.currentTimeMillis();
	
try{
	super.m_tmpVo=vo;
	// ####本脚本必须含有返回值,返回DLG和PNL的组件不允许有返回值####
Object retObj = null;
// *************从平台取得由该动作传入的入口参数。***********
Object inObject = getVo();
// 1,首先检查传入参数类型是否合法，是否为空。
if (!(inObject instanceof nc.vo.so.so001.SaleOrderVO))
	throw new nc.vo.pub.BusinessException(
			nc.bs.ml.NCLangResOnserver.getInstance().getStrByID(
					"sopub", "UPPsopub-000258")/*
												 * @res
												 * "错误：您希望保存的销售订单类型不匹配"
												 */);
if (inObject == null)
	throw new nc.vo.pub.BusinessException(
			nc.bs.ml.NCLangResOnserver.getInstance().getStrByID(
					"sopub", "UPPsopub-000259")/*
												 * @res
												 * "错误：您希望保存的销售订单没有数据"
												 */);
// 2,数据合法，把数据转换。
nc.vo.so.so001.SaleOrderVO inVO = (nc.vo.so.so001.SaleOrderVO) inObject;
String pk_bill = ((nc.vo.so.so001.SaleorderHVO) inVO.getParentVO())
		.getCsaleid();
String billtype = ((nc.vo.so.so001.SaleorderHVO) inVO.getParentVO())
		.getCreceipttype();
nc.vo.so.so001.SaleorderBVO[] body = inVO.getBodyVOs();

//过滤库存硬锁定ID
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
// 3,获取仅包含本销售公司发货行的销售订单vo。
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
		.getStrByID("SCMCOMMON", "UPPSCMCommon-000128");/* @res "结束" */
// **************************************************************************************************
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
	runClass("nc.impl.scm.so.pub.CheckStatusDMO", "isFinishStatus", "&INVO:nc.vo.pub.AggregatedValueObject",vo,m_keyHas,m_methodReturnHas);
	if (retObj != null) {
		m_methodReturnHas.put("isFinishStatus", retObj);
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
				nc.vo.scm.pub.bill.CreditConst.ICREDIT_ACT_OUTCLOSE, new String[] { inVO.getBizTypeid() }, inVO.getPk_corp(),
				creditManager);
		creditObject = creditManager;
		creditPara = para;
		creditManager.renovateARByHidsBegin(para);
	}
	// ##################################################
	// ####重要说明：生成的业务组件方法尽量不要进行修改####
	// 方法说明:可用量接口
	/**************(V5.5调用方法更新--modifyATPBefore)*****************/
	nc.vo.so.so001.SaleOrderVO atpvo = inVO;
	ArrayList listatpbefore = null;
	if (atpvo != null && atpvo.getBodyVOs() != null
			&& atpvo.getBodyVOs().length > 0) {
		setParameter("ATPVO", atpvo);
		listatpbefore  = (ArrayList)runClass("nc.impl.so.sointerface.SOATP", "modifyATPBefore", "&ATPVO:nc.vo.pub.AggregatedValueObject",vo,m_keyHas,m_methodReturnHas);
	}
	/**************(V5.5调用方法更新--modifyATPBefore)******************/
	if (retObj != null) {
		m_methodReturnHas.put("updateAR", retObj);
	}
	// ##################################################
	// ####重要说明：生成的业务组件方法尽量不要进行修改####
	// 方法说明:订单结束时更新订单核销关系
	
	runClass("nc.impl.scm.so.so016.BalanceDMO", "updateSoBalance", "&INVO:nc.vo.so.so001.SaleOrderVO,&iAction:Integer",vo,m_keyHas,m_methodReturnHas);
	if (retObj != null) {
		m_methodReturnHas.put("updateSoBalance", retObj);
	}
	// ##################################################
	// ####重要说明：生成的业务组件方法尽量不要进行修改####
	// 方法说明:订单结束时更新合同执行数量
	runClass("nc.impl.scm.so.so001.SaleOrderDMO", "setSaleCTWhenClose", "&INVO:nc.vo.pub.AggregatedValueObject",vo,m_keyHas,m_methodReturnHas);
	if (retObj != null) {
		m_methodReturnHas.put("setSaleCTWhenClose", retObj);
	}
	// ##################################################
	// ####重要说明：生成的业务组件方法尽量不要进行修改####
	// 方法说明:结束
	runClass("nc.impl.scm.so.pub.BusinessControlDMO", "setBillFinish", "&PKBILL:String,&BILLTYPE:String",vo,m_keyHas,m_methodReturnHas);
	if (retObj != null) {
		m_methodReturnHas.put("setBillFinish", retObj);
	}
	// ##################################################
	// ####重要说明：生成的业务组件方法尽量不要进行修改####
	// 方法说明:跨公司直运销售订单行出库关闭/打开，联带调拨订单行出库关闭/打开
	setParameter("INVOS", new nc.vo.so.so001.SaleOrderVO[]{inVO});
	runClass("nc.impl.scm.so.so001.SaleOrderDMO", "set5XOutEndFlagForAllClose", "&INVOS:nc.vo.so.so001.SaleOrderVO[]",vo,m_keyHas,m_methodReturnHas);
	if (retObj != null) {
		m_methodReturnHas.put("set5XOutEndFlag", retObj);
	}
	// ##################################################
	// ####重要说明：生成的业务组件方法尽量不要进行修改####
	// 方法说明:v5.5上游订单出库关闭--自动关闭下游已审批的发货单、删除自由状态的发货单
	setParameter("INVOS", new nc.vo.so.so001.SaleOrderVO[]{inVO});
	runClass("nc.impl.scm.so.so001.SaleOrderDMO", "processOrderOutEndForAll", "&INVOS:nc.vo.so.so001.SaleOrderVO[]",vo,m_keyHas,m_methodReturnHas);
	if (retObj != null) {
		m_methodReturnHas.put("set5XOutEndFlag", retObj);
	}
	
	
	/**############################# add by chenjianhua 2013-04-14 关闭时删除码单锁定 ########################**/
	runClass( "nc.impl.scm.so.so001.JunJieSoDMO", "freeMdsd", "&INVOS:nc.vo.so.so001.SaleOrderVO[]",vo,m_keyHas,m_methodReturnHas);
	/**############################# add by chenjianhua 2013-04-14 关闭时删除码单锁定 ########################**/
	
	
	
	// ##################################################
	//自动解除库存硬锁定
	if (freezebids!=null){
		nc.itf.ic.service.IICToSO iictoso = (nc.itf.ic.service.IICToSO)nc.bs.framework.common.NCLocator.getInstance().lookup(nc.itf.ic.service.IICToSO.class.getName());
		iictoso.unLockInv(billtype,freezebids,userid,logindate);
	}
	// ##################################################
	/**************(V5.5调用方法更新--modifyATPAfter)*****************/
	if (atpvo != null && atpvo.getBodyVOs() != null
			&& atpvo.getBodyVOs().length > 0) {
		setParameter("LISTATPBEFORE", listatpbefore);
		runClass("nc.impl.so.sointerface.SOATP", "modifyATPAfter", "&ATPVO:nc.vo.pub.AggregatedValueObject,&LISTATPBEFORE:java.util.ArrayList",vo,m_keyHas,m_methodReturnHas);
	}
	/**************(V5.5调用方法更新--modifyATPAfter)*****************/
	
	// ##################################################
	// 调用信用____结束____(必须跟“调用信用____开始____”成对出现)
	if (creditEnabled) {
		nc.itf.so.so120.IBillInvokeCreditManager creditManager = (nc.itf.so.so120.IBillInvokeCreditManager)creditObject;
		creditManager.renovateARByHidsEnd((nc.vo.so.credit.SOCreditPara)creditPara);
	}
} catch (Exception e) {
	// ##################################################
	// 记录日志
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
// *********返回结果******************************************************
inVO = null;
pk_bill = null;
billtype = null;

nc.vo.scm.pub.SCMEnv.out("<====== 销售订单整单关闭：N_30_OrderFinish ## 共耗时["
		+ (System.currentTimeMillis() - s)/1000+"."
		+ (System.currentTimeMillis() - s)%1000+"秒]==============>");

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
	return "	// ####本脚本必须含有返回值,返回DLG和PNL的组件不允许有返回值####\nObject retObj = null;\n// *************从平台取得由该动作传入的入口参数。***********\nObject inObject = getVo();\n// 1,首先检查传入参数类型是否合法，是否为空。\nif (!(inObject instanceof nc.vo.so.so001.SaleOrderVO))\n	throw new nc.vo.pub.BusinessException(\n			nc.bs.ml.NCLangResOnserver.getInstance().getStrByID(\n					\"sopub\", \"UPPsopub-000258\")/*\n												 * @res\n												 * \"错误：您希望保存的销售订单类型不匹配\"\n												 */);\nif (inObject == null)\n	throw new nc.vo.pub.BusinessException(\n			nc.bs.ml.NCLangResOnserver.getInstance().getStrByID(\n					\"sopub\", \"UPPsopub-000259\")/*\n												 * @res\n												 * \"错误：您希望保存的销售订单没有数据\"\n												 */);\n// 2,数据合法，把数据转换。\nnc.vo.so.so001.SaleOrderVO inVO = (nc.vo.so.so001.SaleOrderVO) inObject;\nString pk_bill = ((nc.vo.so.so001.SaleorderHVO) inVO.getParentVO())\n		.getCsaleid();\nString billtype = ((nc.vo.so.so001.SaleorderHVO) inVO.getParentVO())\n		.getCreceipttype();\nnc.vo.so.so001.SaleorderBVO[] body = inVO.getBodyVOs();\nString[] bids = new String[body.length];\nfor (int i = 0, len = body.length; i < len; i++) {\n	bids[i] = body[i].getCorder_bid();\n}\nString userid = inVO.getClientLink().getUser();\nnc.vo.pub.lang.UFDate logindate = inVO.getClientLink()\n		.getLogonDate();\n// 3,获取仅包含本销售公司发货行的销售订单vo。\n// nc.vo.so.so001.SaleOrderVO selfVO = inVO.getOrdVOOfSaleCorp();\ninObject = null;\nInteger iAct = new Integer(nc.vo.so.so001.ISaleOrderAction.A_CLOSE);\nsetParameter(\"iAction\", iAct);\n// **************************************************************************************************\nInteger status = new Integer(2);\nsetParameter(\"STATUS\", status);\nsetParameter(\"INVO\", inVO);\nsetParameter(\"PKBILL\", pk_bill);\nsetParameter(\"BILLTYPE\", billtype);\n// setParameter ( \"SELFVO\",selfVO);\nString sActionMsg = \"\";\nString sButtonName = nc.bs.ml.NCLangResOnserver.getInstance()\n		.getStrByID(\"SCMCOMMON\", \"UPPSCMCommon-000128\");/* @res \"结束\" */\n// **************************************************************************************************\n// 方法说明:加锁\nrunClassCom@\"nc.impl.scm.so.pub.DataControlDMO\", \"lockPkForVo\", \"&INVO:nc.vo.pub.AggregatedValueObject\"@;\nif (retObj != null) {\n	m_methodReturnHas.put(\"lockPkForVo\", retObj);\n}\n// ##################################################\ntry {\n	// ####重要说明：生成的业务组件方法尽量不要进行修改####\n	// 方法说明:并发检查\n	runClassCom@\"nc.impl.scm.so.pub.ParallelCheckDMO\", \"checkVoNoChanged\", \"&INVO:nc.vo.pub.AggregatedValueObject\"@;\n	if (retObj != null) {\n		m_methodReturnHas.put(\"checkVoNoChanged\", retObj);\n	}\n	// ##################################################\n	// ####重要说明：生成的业务组件方法尽量不要进行修改####\n	// 方法说明:并发互斥检查\n	runClassCom@\"nc.impl.scm.so.pub.CheckStatusDMO\", \"isFinishStatus\", \"&INVO:nc.vo.pub.AggregatedValueObject\"@;\n	if (retObj != null) {\n		m_methodReturnHas.put(\"isFinishStatus\", retObj);\n	}\n	// ##################################################\n	// ####重要说明：生成的业务组件方法尽量不要进行修改####\n	// 调用信用____开始____(必须跟“调用信用____结束____”成对出现)\n	nc.itf.uap.sf.ICreateCorpQueryService corpService = (nc.itf.uap.sf.ICreateCorpQueryService)nc.bs.framework.common.NCLocator.getInstance().lookup(nc.itf.uap.sf.ICreateCorpQueryService.class.getName());\n	boolean creditEnabled = corpService.isEnabled(inVO.getPk_corp(), \"SO6\");\n	Object creditObject = null;\n	Object creditPara = null;\n	if (creditEnabled) {\n		// 注意：此处不能采用runClassCom的方式进行调用\n		nc.itf.so.so120.IBillInvokeCreditManager creditManager = (nc.itf.so.so120.IBillInvokeCreditManager) nc.bs.framework.common.NCLocator\n			.getInstance().lookup(nc.itf.so.so120.IBillInvokeCreditManager.class.getName());\n		nc.vo.so.credit.SOCreditPara para = new nc.vo.so.credit.SOCreditPara(new String[] { inVO.getPrimaryKey() }, null,\n				nc.vo.scm.pub.bill.CreditConst.ICREDIT_ACT_OUTCLOSE, new String[] { inVO.getBizTypeid() }, inVO.getPk_corp(),\n				creditManager);\n		creditObject = creditManager;\n		creditPara = para;\n		creditManager.renovateARByHidsBegin(para);\n	}\n	// ##################################################\n	// ####重要说明：生成的业务组件方法尽量不要进行修改####\n	// 方法说明:可用量接口\n	/**************(V5.5调用方法更新--modifyATPBefore)*****************/\n	nc.vo.so.so001.SaleOrderVO atpvo = inVO;\n	ArrayList listatpbefore = null;\n	if (atpvo != null && atpvo.getBodyVOs() != null\n			&& atpvo.getBodyVOs().length > 0) {\n" +
			"		setParameter(\"ATPVO\", atpvo);\n		listatpbefore  = (ArrayList)runClassCom@\"nc.impl.so.sointerface.SOATP\", \"modifyATPBefore\", \"&ATPVO:nc.vo.pub.AggregatedValueObject\"@;\n	}\n	/**************(V5.5调用方法更新--modifyATPBefore)******************/\n	if (retObj != null) {\n		m_methodReturnHas.put(\"updateAR\", retObj);\n	}\n	// ##################################################\n	// ####重要说明：生成的业务组件方法尽量不要进行修改####\n	// 方法说明:订单结束时更新订单核销关系\n	runClassCom@\"nc.impl.scm.so.so016.BalanceDMO\", \"updateSoBalance\", \"&INVO:nc.vo.so.so001.SaleOrderVO,&iAction:Integer\"@;\n	if (retObj != null) {\n		m_methodReturnHas.put(\"updateSoBalance\", retObj);\n	}\n	// ##################################################\n	// ####重要说明：生成的业务组件方法尽量不要进行修改####\n	// 方法说明:订单结束时更新合同执行数量\n	runClassCom@\"nc.impl.scm.so.so001.SaleOrderDMO\", \"setSaleCTWhenClose\", \"&INVO:nc.vo.pub.AggregatedValueObject\"@;\n	if (retObj != null) {\n		m_methodReturnHas.put(\"setSaleCTWhenClose\", retObj);\n	}\n	// ##################################################\n	// ####重要说明：生成的业务组件方法尽量不要进行修改####\n	// 方法说明:结束\n	runClassCom@\"nc.impl.scm.so.pub.BusinessControlDMO\", \"setBillFinish\", \"&PKBILL:String,&BILLTYPE:String\"@;\n	if (retObj != null) {\n		m_methodReturnHas.put(\"setBillFinish\", retObj);\n	}\n	// ##################################################\n	// ####重要说明：生成的业务组件方法尽量不要进行修改####\n	// 方法说明:跨公司直运销售订单行出库关闭/打开，联带调拨订单行出库关闭/打开\n	setParameter(\"INVOS\", new nc.vo.so.so001.SaleOrderVO[]{inVO});\n	runClassCom@\"nc.impl.scm.so.so001.SaleOrderDMO\", \"set5XOutEndFlagForAllClose\", \"&INVOS:nc.vo.so.so001.SaleOrderVO[]\"@;\n	if (retObj != null) {\n		m_methodReturnHas.put(\"set5XOutEndFlag\", retObj);\n	}\n	// ##################################################\n	// ####重要说明：生成的业务组件方法尽量不要进行修改####\n	// 方法说明:v5.5上游订单出库关闭--自动关闭下游已审批的发货单、删除自由状态的发货单\n	setParameter(\"INVOS\", new nc.vo.so.so001.SaleOrderVO[]{inVO});\n	runClassCom@\"nc.impl.scm.so.so001.SaleOrderDMO\", \"processOrderOutEndForAll\", \"&INVOS:nc.vo.so.so001.SaleOrderVO[]\"@;\n	if (retObj != null) {\n		m_methodReturnHas.put(\"set5XOutEndFlag\", retObj);\n	}\n	// ##################################################\n	//自动解除库存硬锁定\n	nc.itf.ic.service.IICToSO iictoso = (nc.itf.ic.service.IICToSO)nc.bs.framework.common.NCLocator.getInstance().lookup(nc.itf.ic.service.IICToSO.class.getName());\n	iictoso.unLockInv(billtype,bids,userid,logindate);\n	// ##################################################\n	/**************(V5.5调用方法更新--modifyATPAfter)*****************/\n	if (atpvo != null && atpvo.getBodyVOs() != null\n			&& atpvo.getBodyVOs().length > 0) {\n		setParameter(\"LISTATPBEFORE\", listatpbefore);\n		runClassCom@\"nc.impl.so.sointerface.SOATP\", \"modifyATPAfter\", \"&ATPVO:nc.vo.pub.AggregatedValueObject,&LISTATPBEFORE:java.util.ArrayList\"@;\n	}\n	/**************(V5.5调用方法更新--modifyATPAfter)*****************/\n	\n	// ##################################################\n	// 调用信用____结束____(必须跟“调用信用____开始____”成对出现)\n	if (creditEnabled) {\n		nc.itf.so.so120.IBillInvokeCreditManager creditManager = (nc.itf.so.so120.IBillInvokeCreditManager)creditObject;\n		creditManager.renovateARByHidsEnd((nc.vo.so.credit.SOCreditPara)creditPara);\n	}\n} catch (Exception e) {\n	// ##################################################\n	// 记录日志\n	nc.impl.scm.so.pub.DataControlDMO datactldmo = new nc.impl.scm.so.pub.DataControlDMO();\n	sActionMsg = e.getMessage();\n	datactldmo.insertBusinessExceptionlog(inVO, sActionMsg,\n			sButtonName, getOperator(),\n			getUserDate() != null ? getUserDate().getDate()\n					.toString() : null);\n	// ##################################################\n	if(e instanceof BusinessException)\n		throw (BusinessException)e;\n	throw new BusinessException(e.getMessage());\n} \n// *********返回结果******************************************************\ninVO = null;\npk_bill = null;\nbilltype = null;\nreturn retObj;\n// ************************************************************************\n";}
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
