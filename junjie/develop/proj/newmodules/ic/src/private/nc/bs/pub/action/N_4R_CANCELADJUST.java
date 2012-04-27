package nc.bs.pub.action;

import nc.vo.ic.pub.bill.GeneralBillItemVO;
import nc.vo.pub.pf.PfUtilActionVO;
import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.pub.compiler.*;
import nc.vo.pub.compiler.PfParameterVO;
import java.math.*;
import java.util.*;

import nc.vo.pub.lang.*;
import nc.bs.pub.pf.PfUtilTools;
import nc.jdbc.framework.processor.ArrayListProcessor;
import nc.jdbc.framework.processor.ArrayProcessor;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.uap.pf.PFBusinessException;
/**
 * 备注：库存盘点单的取消调整
单据动作执行中的动态执行类的动态执行类。
 *
 * 创建日期：(2008-9-27)
 * @author 平台脚本生成
 */
public class N_4R_CANCELADJUST extends AbstractCompiler2 {
private java.util.Hashtable m_methodReturnHas=new java.util.Hashtable();
private Hashtable m_keyHas=null;
/**
 * N_4R_CANCELADJUST 构造子注解。
 */
public N_4R_CANCELADJUST() {
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
if(!(inCurObject instanceof  nc.vo.ic.pub.bill.SpecialBillVO)) throw new nc.vo.pub.BusinessException("Remote Call",new nc.vo.pub.BusinessException("错误：单据类型不匹配"));
if(inCurObject == null)  throw new nc.vo.pub.BusinessException("Remote Call",new nc.vo.pub.BusinessException("错误：单据没有数据"));
//2,数据合法，把数据转换为库存单据。
nc.vo.ic.pub.bill.SpecialBillVO inCurVO=null;
if(inCurObject !=null)
 inCurVO=(nc.vo.ic.pub.bill.SpecialBillVO)inCurObject;
inCurObject=null;
//获取平台传入的参数
setParameter("INCURVO",inCurVO);
//方法说明:单据加业务锁
runClass("nc.bs.ic.pub.bill.ICLockBO","lockBill","&INCURVO:nc.vo.pub.AggregatedValueObject",vo,m_keyHas,m_methodReturnHas);
//方法说明:检查库存单据时间戳
runClass("nc.bs.ic.pub.check.CheckDMO","checkTimeStamp","&INCURVO:nc.vo.pub.AggregatedValueObject",vo,m_keyHas,m_methodReturnHas);

/**add by ouyangzhb 2012-04-26 在取消调整的时候，需要同时删除调整时所生成的码单出入库信息并调整码单现存量*/
String billpk = inCurVO.getPrimaryKey();
setParameter("STR",billpk);
runClass("nc.bs.ic.md.MDToolsImpl","cancelMD","&STR:String",vo,m_keyHas,m_methodReturnHas);
/**add end */



//方法说明:单据取消调整
//####重要说明：生成的业务组件方法尽量不要进行修改####
runClass("nc.bs.ic.ic261.SpecialHBO","deleteGeneralbills","&INCURVO:nc.vo.ic.pub.bill.SpecialBillVO",vo,m_keyHas,m_methodReturnHas);
//##################################################




inCurVO=null;
return null;
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
	return "	//####本脚本必须含有返回值,返回DLG和PNL的组件不允许有返回值####\n//*************从平台取得由该动作传入的入口参数。本方法取得需要保存的VO。***********\nObject inCurObject=getVo();\nObject retObj=null;\n//1,首先检查传入参数类型是否合法，是否为空。\nif(!(inCurObject instanceof  nc.vo.ic.pub.bill.SpecialBillVO)) throw new nc.vo.pub.BusinessException(\"Remote Call\",new nc.vo.pub.BusinessException(\"错误：单据类型不匹配\"));\nif(inCurObject == null)  throw new nc.vo.pub.BusinessException(\"Remote Call\",new nc.vo.pub.BusinessException(\"错误：单据没有数据\"));\n//2,数据合法，把数据转换为库存单据。\nnc.vo.ic.pub.bill.SpecialBillVO inCurVO=null;\nif(inCurObject !=null)\n inCurVO=(nc.vo.ic.pub.bill.SpecialBillVO)inCurObject;\ninCurObject=null;\n//获取平台传入的参数\nsetParameter(\"INCURVO\",inCurVO);\n//方法说明:单据加业务锁\nrunClassCom@\"nc.bs.ic.pub.bill.ICLockBO\",\"lockBill\",\"&INCURVO:nc.vo.pub.AggregatedValueObject\"@;\n//方法说明:检查库存单据时间戳\nrunClassCom@\"nc.bs.ic.pub.check.CheckDMO\",\"checkTimeStamp\",\"&INCURVO:nc.vo.pub.AggregatedValueObject\"@;\n//方法说明:单据取消调整\n//####重要说明：生成的业务组件方法尽量不要进行修改####\nrunClassCom@\"nc.bs.ic.ic261.SpecialHBO\",\"deleteGeneralbills\",\"&INCURVO:nc.vo.ic.pub.bill.SpecialBillVO\"@;\n//##################################################\ninCurVO=null;\nreturn null;\n//************************************************************************\n";}
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
