package nc.bs.pub.action;

import java.util.Hashtable;

import nc.bs.pub.compiler.AbstractCompiler2;
import nc.vo.pub.BusinessException;
import nc.vo.pub.compiler.PfParameterVO;
import nc.vo.uap.pf.PFBusinessException;
/**
 * 备注：采购发票的取消传应付
单据动作执行中的动态执行类的动态执行类。
 *
 * 创建日期：(2008-10-17)
 * @author 平台脚本生成
 */
public class N_25_DELAPBILL extends AbstractCompiler2 {
private java.util.Hashtable m_methodReturnHas=new java.util.Hashtable();
private Hashtable m_keyHas=null;
/**
 * N_25_DELAPBILL 构造子注解。
 */
public N_25_DELAPBILL() {
	super();
}
/*
* 备注：平台编写规则类
* 接口执行类
*/
public Object runComClass(PfParameterVO vo) throws BusinessException {
try{
	super.m_tmpVo=vo;
	Object[] inObject  = getVos ();
	if (inObject  == null){
		throw new nc.vo.pub.BusinessException (nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("4004pub","UPP4004pub-000156")/*@res "错误：您希望弃审的发票没有数据"*/);
	}
	nc.vo.pi.InvoiceVO[] inVO  = (nc.vo.pi.InvoiceVO[])inObject;
	setParameter ( "INVO",inVO);
	//
	Object oUser = getUserObj();
	boolean bLockCheckTs = "MANUAL".equalsIgnoreCase(nc.vo.scm.pu.PuPubVO.getString_TrimZeroLenAsNull(oUser));
	Object oLockRet = null;
	try {
		//手工触发时才做并发处理
		if(bLockCheckTs){         
			//对采购发票申请业务锁
			oLockRet=runClass( "nc.bs.pu.pub.PubDMO", "lockPkForVos", "&INVO:nc.vo.pub.AggregatedValueObject[]",vo,m_keyHas,m_methodReturnHas);
			//并发控制
			runClass( "nc.bs.pu.pub.PubDMO", "checkVosNoChanged", "&INVO:nc.vo.pub.AggregatedValueObject[]",vo,m_keyHas,m_methodReturnHas);
		}
		//设置发票已经传应付标志
		setParameter("BAPFLAG",nc.vo.pub.lang.UFBoolean.FALSE);
		runClass("nc.bs.pi.InvoiceImpl","updateApFlag","&INVO:nc.vo.pi.InvoiceVO[],&BAPFLAG:nc.vo.pub.lang.UFBoolean",vo,m_keyHas,m_methodReturnHas);
		//删除蓝红应付单据
		String key = null;
		for (int i  = 0; i < inVO.length; i  ++) {
			if(!inVO[i].isVirtual()) {
				key  = ( (nc.vo.pi.InvoiceHeaderVO)inVO[i].getParentVO ()).getPrimaryKey ();
				setParameter ( "BILLPK",key);
				//删除蓝(正式)应付单据
				nc.itf.arap.pub.IArapBillPublic iArap = (nc.itf.arap.pub.IArapBillPublic) nc.bs.framework.common.NCLocator.getInstance().lookup(nc.itf.arap.pub.IArapBillPublic.class.getName());
				iArap.deleteOutArapBillByPk(key);       
 			}
		}
		//删除红(红冲)暂估应付单据
		runClass("nc.bs.pi.InvoiceImpl","unAdjustForZGYF","&INVO:nc.vo.pi.InvoiceVO[]",vo,m_keyHas,m_methodReturnHas);
		//add by ouyangzhb 2011-05-09删除红（）红费用暂估应付单
		runClass("nc.bs.pi.InvoiceImpl","unAdjustForFEEZGYF","&INVO:nc.vo.pi.InvoiceVO[]",vo,m_keyHas,m_methodReturnHas);
//
	}catch (Exception e) {
		nc.bs.pu.pub.PubDMO.throwBusinessException(e);
	}finally {
		//解业务锁
		if(bLockCheckTs && oLockRet!= null && ((nc.vo.pub.lang.UFBoolean)oLockRet).booleanValue()){
			runClass( "nc.bs.pu.pub.PubDMO", "freePkForVos", "&INVO:nc.vo.pub.AggregatedValueObject[]",vo,m_keyHas,m_methodReturnHas);
		}
	}
	//
	return null;
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
	return "	Object[] inObject  = getVos ();\n	if (inObject  == null){\n		throw new nc.vo.pub.BusinessException (nc.bs.ml.NCLangResOnserver.getInstance().getStrByID(\"4004pub\",\"UPP4004pub-000156\")/*@res \"错误：您希望弃审的发票没有数据\"*/);\n	}\n	nc.vo.pi.InvoiceVO[] inVO  = (nc.vo.pi.InvoiceVO[])inObject;\n	setParameter ( \"INVO\",inVO);\n	//\n	Object oUser = getUserObj();\n	boolean bLockCheckTs = \"MANUAL\".equalsIgnoreCase(nc.vo.scm.pu.PuPubVO.getString_TrimZeroLenAsNull(oUser));\n	Object oLockRet = null;\n	try {\n		//手工触发时才做并发处理\n		if(bLockCheckTs){         \n			//对采购发票申请业务锁\n			oLockRet=runClass( \"nc.bs.pu.pub.PubDMO\", \"lockPkForVos\", \"&INVO:nc.vo.pub.AggregatedValueObject[]\",vo,m_keyHas,m_methodReturnHas);\n			//并发控制\n			runClass( \"nc.bs.pu.pub.PubDMO\", \"checkVosNoChanged\", \"&INVO:nc.vo.pub.AggregatedValueObject[]\",vo,m_keyHas,m_methodReturnHas);\n		}\n		//设置发票已经传应付标志\n		setParameter(\"BAPFLAG\",nc.vo.pub.lang.UFBoolean.FALSE);\n		runClass(\"nc.bs.pi.InvoiceImpl\",\"updateApFlag\",\"&INVO:nc.vo.pi.InvoiceVO[],&BAPFLAG:nc.vo.pub.lang.UFBoolean\",vo,m_keyHas,m_methodReturnHas);\n		//删除蓝红应付单据\n		String key = null;\n		for (int i  = 0; i < inVO.length; i  ++) {\n			if(!inVO[i].isVirtual()) {\n				key  = ( (nc.vo.pi.InvoiceHeaderVO)inVO[i].getParentVO ()).getPrimaryKey ();\n				setParameter ( \"BILLPK\",key);\n				//删除蓝(正式)应付单据\n				nc.itf.arap.pub.IArapBillPublic iArap = (nc.itf.arap.pub.IArapBillPublic) nc.bs.framework.common.NCLocator.getInstance().lookup(nc.itf.arap.pub.IArapBillPublic.class.getName());\n				iArap.deleteOutArapBillByPk(key);       \n 			}\n		}\n		//删除红(红冲)暂估应付单据\n		runClass(\"nc.bs.pi.InvoiceImpl\",\"unAdjustForZGYF\",\"&INVO:nc.vo.pi.InvoiceVO[]\",vo,m_keyHas,m_methodReturnHas);\n		//\n	}catch (Exception e) {\n		nc.bs.pu.pub.PubDMO.throwBusinessException(e);\n	}finally {\n		//解业务锁\n		if(bLockCheckTs && oLockRet!= null && ((nc.vo.pub.lang.UFBoolean)oLockRet).booleanValue()){\n			runClass( \"nc.bs.pu.pub.PubDMO\", \"freePkForVos\", \"&INVO:nc.vo.pub.AggregatedValueObject[]\",vo,m_keyHas,m_methodReturnHas);\n		}\n	}\n	//\n	return null;\n";}
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
