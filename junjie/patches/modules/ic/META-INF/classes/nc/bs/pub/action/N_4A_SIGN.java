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
 * 备注：库存其它入库单的签字
单据动作执行中的动态执行类的动态执行类。
 *
 * 创建日期：(2008-7-14)
 * @author 平台脚本生成
 */
public class N_4A_SIGN extends AbstractCompiler2 {
private java.util.Hashtable m_methodReturnHas=new java.util.Hashtable();
private Hashtable m_keyHas=null;
/**
 * N_4A_SIGN 构造子注解。
 */
public N_4A_SIGN() {
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
nc.vo.pub.AggregatedValueObject[] inCurObjects=getVos();
StringBuffer sErr=new StringBuffer();
Object retObj=null;
if(inCurObjects== null||inCurObjects.length==0)  throw new nc.vo.pub.BusinessException("Remote Call",new nc.vo.pub.BusinessException("错误：单据没有数据"));
//1,首先检查传入参数类型是否合法，是否为空。
if(!(inCurObjects[0] instanceof  nc.vo.ic.pub.bill.GeneralBillVO)) throw new nc.vo.pub.BusinessException("Remote Call",new nc.vo.pub.BusinessException("错误：单据类型不匹配"));
//2,数据合法，把数据转换为库存单据。
nc.vo.ic.pub.bill.GeneralBillVO[] inCurVOs=null;
inCurVOs=(nc.vo.ic.pub.bill.GeneralBillVO[])inCurObjects;
inCurObjects=null;
//获取平台传入的参数
setParameter("INCURVOS",inCurVOs);
ArrayList alLockedPK=null;
setParameter("ILOCKS",(nc.vo.ic.pub.ILockIC[])inCurVOs);
try{
//####重要说明：生成的业务组件方法尽量不要进行修改####
//方法说明:库存出入库单据加业务锁
alLockedPK=(ArrayList)runClass("nc.bs.ic.pub.bill.ICLockBO","lockICBills","&INCURVOS:nc.vo.pub.AggregatedValueObject[]",vo,m_keyHas,m_methodReturnHas);
//####重要说明：生成的业务组件方法尽量不要进行修改####
//方法说明:检查是否关帐。<可配置>
//目前是根据单据表体业务日期检查。如果根据登录日期检查，请将checkAccountStatus改为checkAccountStatus1
runClass("nc.bs.ic.ic2a3.AccountctrlDMO","checkAccountStatus","&INCURVOS:nc.vo.ic.pub.bill.GeneralBillVO[]",vo,m_keyHas,m_methodReturnHas);
//#############################################################
//方法说明:库存出入库单据加业务锁
//alLockedPK=runClass("nc.bs.ic.pub.bill.GeneralBillBO","lockBills","&INCURVOS:nc.vo.pub.AggregatedValueObject[]",vo,m_keyHas,m_methodReturnHas);
//##################################################
//该方法<不可配置>
//方法说明:检查库存单据时间戳
runClass("nc.bs.ic.pub.check.CheckDMO","checkTimeStamps","&INCURVOS:nc.vo.ic.pub.bill.GeneralBillVO[]",vo,m_keyHas,m_methodReturnHas);
//该方法<不可配置>
//方法说明:库存签字前处理
runClass("nc.bs.ic.pub.BillActionBase","beforeSign","&INCURVOS:nc.vo.ic.pub.bill.GeneralBillVO[]",vo,m_keyHas,m_methodReturnHas);
//############################
//校验条码数量和存货数量 该方法可以配置
String sBarcodeCheckErr=(String)runClass("nc.bs.ic.pub.check.CheckBarcodeDMO","checkBarcodeAbsent","&INCURVOS:nc.vo.ic.pub.bill.GeneralBillVO[]",vo,m_keyHas,m_methodReturnHas);
if (sBarcodeCheckErr!=null )
sErr.append(sBarcodeCheckErr);
//#################
//方法说明:单据签字
runClass("nc.bs.ic.ic207.GeneralHBO","signBills","&INCURVOS:nc.vo.ic.pub.bill.GeneralBillVO[]",vo,m_keyHas,m_methodReturnHas);
//############################
//该方法<不可配置>
//方法说明:库存签字后处理
runClass("nc.bs.ic.pub.BillActionBase","afterSign","&INCURVOS:nc.vo.ic.pub.bill.GeneralBillVO[]",vo,m_keyHas,m_methodReturnHas);
//插入业务日志，该方法可以配置
setParameter("ERR",sErr.toString());
setParameter("FUN","签字");
runClass("nc.bs.ic.pub.check.CheckDMO","insertBusinesslog","&INCURVOS:nc.vo.ic.pub.bill.GeneralBillVO[],&ERR:String,&FUN:String",vo,m_keyHas,m_methodReturnHas);
//############################
//结果返回前必须检查类型是否匹配
if(retObj != null && !(retObj instanceof  ArrayList))  throw new nc.vo.pub.BusinessException("Remote Call",new nc.vo.pub.BusinessException("错误：签字动作的返回值类型错误。"));
//####重要说明：生成的业务组件方法尽量不要进行修改####
//方法说明:生成存货单据接口
//nc.bs.ic.pub.ictoia.Ic2IaDMO.saveIABills(inCurVOs,"4A","I4",vo);
setParameter("icbilltype","4A");
setParameter("iabilltype","I4");
setParameter("PFPARAVO",vo);
runClass("nc.bs.ic.pub.ictoia.Ic2IaDMO","saveIABills","&INCURVOS:nc.vo.ic.pub.bill.GeneralBillVO[],&icbilltype:String,&iabilltype:String,&PFPARAVO:nc.vo.pub.compiler.PfParameterVO",vo,m_keyHas,m_methodReturnHas);
//##################################################

//########### add by ouyangzhb 2012-12-14 其他入库单签字推出费用暂估应付以及调整单###########
ClientLink cl = clientLinkCreate(vo);
setParameter("ClientLink", cl);
runClass("nc.bs.ic.ic211.GeneralHBO","push4AToAPAndIA","&INCURVOS:nc.vo.ic.pub.bill.GeneralBillVO[],&ClientLink:nc.vo.scm.pub.session.ClientLink",vo,m_keyHas,m_methodReturnHas);
//########### add by ouyangzhb 2012-12-14 其他入库单签字推出费用暂估应付以及调整单###########

}catch(Exception e){
//############################
//插入业务日志，该方法可以配置
setParameter("ERR",e.getMessage());
setParameter("FUN","签字 ");
runClass("nc.bs.ic.pub.check.CheckBO","insertBusinessExceptionlog","&INCURVOS:nc.vo.ic.pub.bill.GeneralBillVO[],&ERR:String,&FUN:String",vo,m_keyHas,m_methodReturnHas);
//###########################
//如果是新增状态，退回单据号
for(int i=0;i<inCurVOs.length;i++){
setParameter("IBC",(nc.vo.scm.pub.IBillCode)inCurVOs[i]);
if(inCurVOs[i].getHeaderVO().getStatus()==nc.vo.pub.VOStatus.NEW)
         runClass("nc.bs.ic.pub.check.CheckDMO","returnBillCode","&IBC:nc.vo.scm.pub.IBillCode",vo,m_keyHas,m_methodReturnHas);
}
          if (e instanceof nc.vo.pub.BusinessException)
      throw (nc.vo.pub.BusinessException) e;
    else
      throw new nc.vo.pub.BusinessException("Remote Call", e);
}finally{
//####重要说明：生成的业务组件方法尽量不要进行修改####
//方法说明:库存出入库单据解业务锁
if(alLockedPK!=null){
setParameter("ALPK",alLockedPK);
 runClass("nc.bs.ic.pub.bill.ICLockBO","unlockBills","&INCURVOS:nc.vo.pub.AggregatedValueObject[],&ALPK:ArrayList",vo,m_keyHas,m_methodReturnHas);
}//##################################################
}
ArrayList alRet=new ArrayList();
if(sErr.toString().trim().length()==0)
  alRet.add(null);
else
  alRet.add(sErr.toString());
alRet.add(retObj);
//添加小型单据VO向前台传递 
nc.vo.ic.pub.smallbill.SMGeneralBillVO smbillvo = inCurVOs[0].getSmallBillVO();
alRet.add(smbillvo);
inCurVOs=null;
return new Object[]{alRet};
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
  return "  //####本脚本必须含有返回值,返回DLG和PNL的组件不允许有返回值####\n//*************从平台取得由该动作传入的入口参数。本方法取得需要保存的VO。***********\nnc.vo.pub.AggregatedValueObject[] inCurObjects=getVos();\nStringBuffer sErr=new StringBuffer();\nObject retObj=null;\nif(inCurObjects== null||inCurObjects.length==0)  throw new nc.vo.pub.BusinessException(\"Remote Call\",new nc.vo.pub.BusinessException(\"错误：单据没有数据\"));\n//1,首先检查传入参数类型是否合法，是否为空。\nif(!(inCurObjects[0] instanceof  nc.vo.ic.pub.bill.GeneralBillVO)) throw new nc.vo.pub.BusinessException(\"Remote Call\",new nc.vo.pub.BusinessException(\"错误：单据类型不匹配\"));\n//2,数据合法，把数据转换为库存单据。\nnc.vo.ic.pub.bill.GeneralBillVO[] inCurVOs=null;\ninCurVOs=(nc.vo.ic.pub.bill.GeneralBillVO[])inCurObjects;\ninCurObjects=null;\n//获取平台传入的参数\nsetParameter(\"INCURVOS\",inCurVOs);\nArrayList alLockedPK=null;\nsetParameter(\"ILOCKS\",(nc.vo.ic.pub.ILockIC[])inCurVOs);\ntry{\n//####重要说明：生成的业务组件方法尽量不要进行修改####\n//方法说明:库存出入库单据加业务锁\nalLockedPK=(ArrayList)runClassCom@\"nc.bs.ic.pub.bill.ICLockBO\",\"lockICBills\",\"&INCURVOS:nc.vo.pub.AggregatedValueObject[]\"@;\n//####重要说明：生成的业务组件方法尽量不要进行修改####\n//方法说明:检查是否关帐。<可配置>\n//目前是根据单据表体业务日期检查。如果根据登录日期检查，请将checkAccountStatus改为checkAccountStatus1\nrunClassCom@\"nc.bs.ic.ic2a3.AccountctrlDMO\",\"checkAccountStatus\",\"&INCURVOS:nc.vo.ic.pub.bill.GeneralBillVO[]\"@;\n//#############################################################\n//方法说明:库存出入库单据加业务锁\n//alLockedPK=runClassCom@\"nc.bs.ic.pub.bill.GeneralBillBO\",\"lockBills\",\"&INCURVOS:nc.vo.pub.AggregatedValueObject[]\"@;\n//##################################################\n//该方法<不可配置>\n//方法说明:检查库存单据时间戳\nrunClassCom@\"nc.bs.ic.pub.check.CheckDMO\",\"checkTimeStamps\",\"&INCURVOS:nc.vo.ic.pub.bill.GeneralBillVO[]\"@;\n//该方法<不可配置>\n//方法说明:库存签字前处理\nrunClassCom@\"nc.bs.ic.pub.BillActionBase\",\"beforeSign\",\"&INCURVOS:nc.vo.ic.pub.bill.GeneralBillVO[]\"@;\n//############################\n//校验条码数量和存货数量 该方法可以配置\nString sBarcodeCheckErr=(String)runClassCom@\"nc.bs.ic.pub.check.CheckBarcodeDMO\",\"checkBarcodeAbsent\",\"&INCURVOS:nc.vo.ic.pub.bill.GeneralBillVO[]\"@;\nif (sBarcodeCheckErr!=null )\nsErr.append(sBarcodeCheckErr);\n//#################\n//方法说明:单据签字\nrunClassCom@\"nc.bs.ic.ic207.GeneralHBO\",\"signBills\",\"&INCURVOS:nc.vo.ic.pub.bill.GeneralBillVO[]\"@;\n//############################\n//该方法<不可配置>\n//方法说明:库存签字后处理\nrunClassCom@\"nc.bs.ic.pub.BillActionBase\",\"afterSign\",\"&INCURVOS:nc.vo.ic.pub.bill.GeneralBillVO[]\"@;\n//插入业务日志，该方法可以配置\nsetParameter(\"ERR\",sErr.toString());\nsetParameter(\"FUN\",\"签字\");\nrunClassCom@\"nc.bs.ic.pub.check.CheckDMO\",\"insertBusinesslog\",\"&INCURVOS:nc.vo.ic.pub.bill.GeneralBillVO[],&ERR:String,&FUN:String\"@;\n" +
      "//############################\n//结果返回前必须检查类型是否匹配\nif(retObj != null && !(retObj instanceof  ArrayList))  throw new nc.vo.pub.BusinessException(\"Remote Call\",new nc.vo.pub.BusinessException(\"错误：签字动作的返回值类型错误。\"));\n//####重要说明：生成的业务组件方法尽量不要进行修改####\n//方法说明:生成存货单据接口\n//nc.bs.ic.pub.ictoia.Ic2IaDMO.saveIABills(inCurVOs,\"4A\",\"I4\",vo);\nsetParameter(\"icbilltype\",\"4A\");\nsetParameter(\"iabilltype\",\"I4\");\nsetParameter(\"PFPARAVO\",vo);\nrunClassCom@\"nc.bs.ic.pub.ictoia.Ic2IaDMO\",\"saveIABills\",\"&INCURVOS:nc.vo.ic.pub.bill.GeneralBillVO[],&icbilltype:String,&iabilltype:String,&PFPARAVO:nc.vo.pub.compiler.PfParameterVO\"@;\n//##################################################\n}catch(Exception e){\n//############################\n//插入业务日志，该方法可以配置\nsetParameter(\"ERR\",e.getMessage());\nsetParameter(\"FUN\",\"签字 \");\nrunClassCom@\"nc.bs.ic.pub.check.CheckBO\",\"insertBusinessExceptionlog\",\"&INCURVOS:nc.vo.ic.pub.bill.GeneralBillVO[],&ERR:String,&FUN:String\"@;\n//###########################\n//如果是新增状态，退回单据号\nfor(int i=0;i<inCurVOs.length;i++){\nsetParameter(\"IBC\",(nc.vo.scm.pub.IBillCode)inCurVOs[i]);\nif(inCurVOs[i].getHeaderVO().getStatus()==nc.vo.pub.VOStatus.NEW)\n         runClassCom@\"nc.bs.ic.pub.check.CheckDMO\",\"returnBillCode\",\"&IBC:nc.vo.scm.pub.IBillCode\"@;\n}\n          if (e instanceof nc.vo.pub.BusinessException)\n     throw (nc.vo.pub.BusinessException) e;\n    else\n      throw new nc.vo.pub.BusinessException(\"Remote Call\", e);\n}finally{\n//####重要说明：生成的业务组件方法尽量不要进行修改####\n//方法说明:库存出入库单据解业务锁\nif(alLockedPK!=null){\nsetParameter(\"ALPK\",alLockedPK);\n runClassCom@\"nc.bs.ic.pub.bill.ICLockBO\",\"unlockBills\",\"&INCURVOS:nc.vo.pub.AggregatedValueObject[],&ALPK:ArrayList\"@;\n}//##################################################\n}\nArrayList alRet=new ArrayList();\nif(sErr.toString().trim().length()==0)\n alRet.add(null);\nelse\n  alRet.add(sErr.toString());\nalRet.add(retObj);\n//添加小型单据VO向前台传递 \nnc.vo.ic.pub.smallbill.SMGeneralBillVO smbillvo = inCurVOs[0].getSmallBillVO();\nalRet.add(smbillvo);\ninCurVOs=null;\nreturn new Object[]{alRet};\n//************************************************************************\n";}
/*
* 备注：设置脚本变量的HAS
*/
private void setParameter(String key,Object val)  {
  if (m_keyHas==null){
    m_keyHas=new Hashtable();
  }
  if (val!=null)  {
    m_keyHas.put(key,val);
  }
}

/**
 * add by ouyangzhb 2012-09-22 获取 当前客户端连接信息
 */
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
