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
import nc.vo.scm.pub.session.ClientLink;
import nc.vo.uap.pf.PFBusinessException;
/**
 * 备注：库存其它入库单的取消签字
单据动作执行中的动态执行类的动态执行类。
 *
 * 创建日期：(2007-4-30)
 * @author 平台脚本生成
 */
public class N_4A_CANCELSIGN extends AbstractCompiler2 {
private java.util.Hashtable m_methodReturnHas=new java.util.Hashtable();
private Hashtable m_keyHas=null;
/**
 * N_4A_CANCELSIGN 构造子注解。
 */
public N_4A_CANCELSIGN() {
	super();
}
/*
* 备注：平台编写规则类
* 接口执行类
*/
public Object runComClass(PfParameterVO vo) throws BusinessException {
try{
	super.m_tmpVo=vo;
	//####本脚本必须含有返回值,返回DLG和PNL的组件不允许有返回值####
//*************从平台取得由该动作传入的入口参数。本方法取得需要保存的VO。***********
Object inCurObject=getVo();
Object retObj=null;
//1,首先检查传入参数类型是否合法，是否为空。
if(!(inCurObject instanceof  nc.vo.ic.pub.bill.GeneralBillVO)) throw new BusinessException("Remote Call",new nc.vo.pub.BusinessException("错误：单据类型不匹配"));
if(inCurObject == null)  throw new BusinessException("Remote Call",new nc.vo.pub.BusinessException("错误：单据没有数据"));
//2,数据合法，把数据转换为库存单据。
nc.vo.ic.pub.bill.GeneralBillVO inCurVO=null;
if(inCurObject !=null)
 inCurVO=(nc.vo.ic.pub.bill.GeneralBillVO)inCurObject;
inCurObject=null;
//获取平台传入的参数
setParameter("INCURVO",inCurVO);
Object alLockedPK=null;
try{
//####重要说明：生成的业务组件方法尽量不要进行修改####
//方法说明:检查是否关帐。<可配置>
//目前是根据单据表体业务日期检查。如果根据登录日期检查，请将checkAccountStatus改为checkAccountStatus1
runClass("nc.bs.ic.ic2a3.AccountctrlDMO","checkAccountStatus","&INCURVO:nc.vo.ic.pub.bill.GeneralBillVO",vo,m_keyHas,m_methodReturnHas);
//#############################################################
//方法说明:库存出入库单据加业务锁
alLockedPK=runClass("nc.bs.ic.pub.bill.ICLockBO","lockICBill","&INCURVO:nc.vo.pub.AggregatedValueObject",vo,m_keyHas,m_methodReturnHas);
//##################################################
//该方法<不可配置>
//方法说明:检查库存单据时间戳
runClass("nc.bs.ic.pub.check.CheckDMO","checkTimeStamp","&INCURVO:nc.vo.pub.AggregatedValueObject",vo,m_keyHas,m_methodReturnHas);
//该方法<不可配置>
//方法说明:检查是否本人签字的单据
runClass("nc.bs.ic.pub.check.CheckBusiDMO","checkCancelSigningUser","&INCURVO:nc.vo.pub.AggregatedValueObject",vo,m_keyHas,m_methodReturnHas);
//####重要说明：生成的业务组件方法尽量不要进行修改####
//方法说明:判断是否进行了消耗汇总
runClass("nc.bs.ic.pub.check.CheckDMO","checkBillSumed","&INCURVO:nc.vo.pub.AggregatedValueObject",vo,m_keyHas,m_methodReturnHas);
//方法说明:单据取消签字
retObj=runClass("nc.bs.ic.ic207.GeneralHBO","cancelSignBill","&INCURVO:nc.vo.ic.pub.bill.GeneralBillVO",vo,m_keyHas,m_methodReturnHas);
//结果返回前必须检查类型是否匹配
if(retObj != null && !(retObj instanceof  ArrayList))  throw new BusinessException("Remote Call",new nc.vo.pub.BusinessException("错误：取消签字动作的返回值类型错误。"));
//####重要说明：生成的业务组件方法尽量不要进行修改####
//方法说明:删除存货接口单据
//nc.bs.ic.pub.ictoia.Ic2IaDMO.deleteIABills(inCurVO);
runClass("nc.bs.ic.pub.ictoia.Ic2IaDMO","deleteIABills","&INCURVO:nc.vo.ic.pub.bill.GeneralBillVO",vo,m_keyHas,m_methodReturnHas);
//wanglei 2014-06-19
//方法说明:删除暂估应付单据及入库调整单
//nc.bs.ic.pub.ictoia.Ic2IaDMO.deleteIABills(inCurVO);
runClass("nc.bs.ic.ic211.GeneralHBO","delete4A2AP","&INCURVO:nc.vo.ic.pub.bill.GeneralBillVO",vo,m_keyHas,m_methodReturnHas);

//############################
//插入业务日志，该方法可以配置
setParameter("ERR","");
setParameter("FUN","取消签字 ");
runClass("nc.bs.ic.pub.check.CheckDMO","insertBusinesslog","&INCURVO:nc.vo.pub.AggregatedValueObject,&ERR:String,&FUN:String",vo,m_keyHas,m_methodReturnHas);
//############################
//##################################################
}catch(Exception e){
//############################
//插入业务日志，该方法可以配置
setParameter("ERR",e.getMessage());
setParameter("FUN","取消签字");
runClass("nc.bs.ic.pub.check.CheckBO","insertBusinessExceptionlog","&INCURVO:nc.vo.pub.AggregatedValueObject,&ERR:String,&FUN:String",vo,m_keyHas,m_methodReturnHas);
//###########################
        if (e instanceof BusinessException)
			throw (BusinessException) e;
		else
			throw new BusinessException("Remote Call", e);
}
finally{
//####重要说明：生成的业务组件方法尽量不要进行修改####
//方法说明:库存出入库单据解业务锁
setParameter("ALLPK",(ArrayList)alLockedPK);
if(alLockedPK!=null)
runClass("nc.bs.ic.pub.bill.ICLockBO","unlockBill","&INCURVO:nc.vo.pub.AggregatedValueObject,&ALLPK:ArrayList",vo,m_keyHas,m_methodReturnHas);
//##################################################
}
inCurVO=null;
return retObj; 
//************************************************************************
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
	return "	//####本脚本必须含有返回值,返回DLG和PNL的组件不允许有返回值####\n//*************从平台取得由该动作传入的入口参数。本方法取得需要保存的VO。***********\nObject inCurObject=getVo();\nObject retObj=null;\n//1,首先检查传入参数类型是否合法，是否为空。\nif(!(inCurObject instanceof  nc.vo.ic.pub.bill.GeneralBillVO)) throw new BusinessException(\"Remote Call\",new nc.vo.pub.BusinessException(\"错误：单据类型不匹配\"));\nif(inCurObject == null)  throw new BusinessException(\"Remote Call\",new nc.vo.pub.BusinessException(\"错误：单据没有数据\"));\n//2,数据合法，把数据转换为库存单据。\nnc.vo.ic.pub.bill.GeneralBillVO inCurVO=null;\nif(inCurObject !=null)\n inCurVO=(nc.vo.ic.pub.bill.GeneralBillVO)inCurObject;\ninCurObject=null;\n//获取平台传入的参数\nsetParameter(\"INCURVO\",inCurVO);\nObject alLockedPK=null;\ntry{\n//####重要说明：生成的业务组件方法尽量不要进行修改####\n//方法说明:检查是否关帐。<可配置>\n//目前是根据单据表体业务日期检查。如果根据登录日期检查，请将checkAccountStatus改为checkAccountStatus1\nrunClassCom@\"nc.bs.ic.ic2a3.AccountctrlDMO\",\"checkAccountStatus\",\"&INCURVO:nc.vo.ic.pub.bill.GeneralBillVO\"@;\n//#############################################################\n//方法说明:库存出入库单据加业务锁\nalLockedPK=runClassCom@\"nc.bs.ic.pub.bill.ICLockBO\",\"lockICBill\",\"&INCURVO:nc.vo.pub.AggregatedValueObject\"@;\n//##################################################\n//该方法<不可配置>\n//方法说明:检查库存单据时间戳\nrunClassCom@\"nc.bs.ic.pub.check.CheckDMO\",\"checkTimeStamp\",\"&INCURVO:nc.vo.pub.AggregatedValueObject\"@;\n//该方法<不可配置>\n//方法说明:检查是否本人签字的单据\nrunClassCom@\"nc.bs.ic.pub.check.CheckBusiDMO\",\"checkCancelSigningUser\",\"&INCURVO:nc.vo.pub.AggregatedValueObject\"@;\n//####重要说明：生成的业务组件方法尽量不要进行修改####\n//方法说明:判断是否进行了消耗汇总\nrunClassCom@\"nc.bs.ic.pub.check.CheckDMO\",\"checkBillSumed\",\"&INCURVO:nc.vo.pub.AggregatedValueObject\"@;\n//方法说明:单据取消签字\nretObj=runClassCom@\"nc.bs.ic.ic207.GeneralHBO\",\"cancelSignBill\",\"&INCURVO:nc.vo.ic.pub.bill.GeneralBillVO\"@;\n//结果返回前必须检查类型是否匹配\nif(retObj != null && !(retObj instanceof  ArrayList))  throw new BusinessException(\"Remote Call\",new nc.vo.pub.BusinessException(\"错误：取消签字动作的返回值类型错误。\"));\n//####重要说明：生成的业务组件方法尽量不要进行修改####\n//方法说明:删除存货接口单据\n//nc.bs.ic.pub.ictoia.Ic2IaDMO.deleteIABills(inCurVO);\nrunClassCom@\"nc.bs.ic.pub.ictoia.Ic2IaDMO\",\"deleteIABills\",\"&INCURVO:nc.vo.ic.pub.bill.GeneralBillVO\"@;\n//############################\n//插入业务日志，该方法可以配置\nsetParameter(\"ERR\",\"\");\nsetParameter(\"FUN\",\"取消签字 \");\nrunClassCom@\"nc.bs.ic.pub.check.CheckDMO\",\"insertBusinesslog\",\"&INCURVO:nc.vo.pub.AggregatedValueObject,&ERR:String,&FUN:String\"@;\n//############################\n//##################################################\n}catch(Exception e){\n//############################\n//插入业务日志，该方法可以配置\nsetParameter(\"ERR\",e.getMessage());\nsetParameter(\"FUN\",\"取消签字\");\nrunClassCom@\"nc.bs.ic.pub.check.CheckBO\",\"insertBusinessExceptionlog\",\"&INCURVO:nc.vo.pub.AggregatedValueObject,&ERR:String,&FUN:String\"@;\n//###########################\n        if (e instanceof BusinessException)\n			throw (BusinessException) e;\n		else\n			throw new BusinessException(\"Remote Call\", e);\n}\nfinally{\n//####重要说明：生成的业务组件方法尽量不要进行修改####\n//方法说明:库存出入库单据解业务锁\nsetParameter(\"ALLPK\",(ArrayList)alLockedPK);\nif(alLockedPK!=null)\nrunClassCom@\"nc.bs.ic.pub.bill.ICLockBO\",\"unlockBill\",\"&INCURVO:nc.vo.pub.AggregatedValueObject,&ALLPK:ArrayList\"@;\n//##################################################\n}\ninCurVO=null;\nreturn retObj; \n//************************************************************************\n";}
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
public ClientLink clientLinkCreate(PfParameterVO pfVo)
{
    String pk_corp = pfVo.m_coId;
    String cuserid = pfVo.m_operator;
    UFDate date = new UFDate(pfVo.m_currentDate);
    String accountYear = pfVo.m_currentDate.substring(0, 4);
    String accountMonth = pfVo.m_currentDate.substring(5, 7);;
    String yearMonth = null;
    UFDate monthStart = null;
    UFDate monthEnd = null;
    String language = null;
    boolean isDebug = false;
    String moduleName = null;
    String moduleCode = null;
    String moduleID = null;
    ClientLink clientLink = new ClientLink(pk_corp, cuserid, date, accountYear, accountMonth, yearMonth, monthStart, monthEnd, language, isDebug, moduleName, moduleCode, moduleID);
    return clientLink;
}
}
