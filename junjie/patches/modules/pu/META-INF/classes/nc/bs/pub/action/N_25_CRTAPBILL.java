package nc.bs.pub.action;

import java.util.Hashtable;

import nc.bs.pub.compiler.AbstractCompiler2;
import nc.vo.pub.BusinessException;
import nc.vo.pub.compiler.PfParameterVO;
import nc.vo.uap.pf.PFBusinessException;
/**
 * 备注：采购发票的传应付
单据动作执行中的动态执行类的动态执行类。
 *
 * 创建日期：(2008-12-1)
 * @author 平台脚本生成
 */
public class N_25_CRTAPBILL extends AbstractCompiler2 {
private java.util.Hashtable m_methodReturnHas=new java.util.Hashtable();
private Hashtable m_keyHas=null;
/**
 * N_25_CRTAPBILL 构造子注解。
 */
public N_25_CRTAPBILL() {
	super();
}
/*
* 备注：平台编写规则类
* 接口执行类
*/
public Object runComClass(PfParameterVO vo) throws BusinessException {
try{
	super.m_tmpVo=vo;
	//业务动作自定义参数
	Object oUser = getUserObj();	
	//手工触发标志
	boolean bManualFlag = "MANUAL".equalsIgnoreCase(nc.vo.scm.pu.PuPubVO.getString_TrimZeroLenAsNull(oUser));
	//
	nc.vo.pi.InvoiceVO[] inVO  = null;
	Object inObjectOne = null;
	Object[] inObject = null;
	if(bManualFlag){
		inObject  = getVos ();
		if (inObject  == null){
			throw new nc.vo.pub.BusinessException (nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("4004pub","UPP4004pub-000156")/*@res "错误：您希望弃审的发票没有数据"*/);
		}
		inVO  = (nc.vo.pi.InvoiceVO[])inObject;
	}else{
		inObjectOne  = getVo ();
		if (inObjectOne  == null){
			throw new nc.vo.pub.BusinessException (nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("4004pub","UPP4004pub-000156")/*@res "错误：您希望弃审的发票没有数据"*/);
		}
		inVO  = new nc.vo.pi.InvoiceVO[]{(nc.vo.pi.InvoiceVO)inObjectOne};
	}
	//
	setParameter ( "INVO",inVO);
	Object oLockRet = null;
	try {
		//手工触发时才做并发处理
		if(bManualFlag){         
			//对采购发票申请业务锁
			oLockRet=runClass( "nc.bs.pu.pub.PubDMO", "lockPkForVos", "&INVO:nc.vo.pub.AggregatedValueObject[]",vo,m_keyHas,m_methodReturnHas);
			//并发控制
			runClass( "nc.bs.pu.pub.PubDMO", "checkVosNoChanged", "&INVO:nc.vo.pub.AggregatedValueObject[]",vo,m_keyHas,m_methodReturnHas);
		}
		//检查传应付必须配置组件
		runClass("nc.bs.pi.InvoiceImpl","checkDriveCRTAPBILL","&INVO:nc.vo.pi.InvoiceVO[]",vo,m_keyHas,m_methodReturnHas);
		//设置发票的已经传应付标志
		setParameter("BAPFLAG",nc.vo.pub.lang.UFBoolean.TRUE);
		runClass("nc.bs.pi.InvoiceImpl","updateApFlag","&INVO:nc.vo.pi.InvoiceVO[],&BAPFLAG:nc.vo.pub.lang.UFBoolean",vo,m_keyHas,m_methodReturnHas);
		//冲暂估应付
		runClass("nc.bs.pi.InvoiceImpl","adjustForZGYF","&INVO:nc.vo.pi.InvoiceVO[]",vo,m_keyHas,m_methodReturnHas);
		//add by ouyangzhb 2011-05-09 冲暂估费用应付
		runClass("nc.bs.pi.InvoiceImpl","adjustForFeeZGYF","&INVO:nc.vo.pi.InvoiceVO[]",vo,m_keyHas,m_methodReturnHas);
	}catch (Exception e) {
		nc.bs.pu.pub.PubDMO.throwBusinessException(e);
	}finally {
		//解业务锁
		if(bManualFlag && oLockRet!= null && ((nc.vo.pub.lang.UFBoolean)oLockRet).booleanValue()){
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
	return "	//业务动作自定义参数\n	Object oUser = getUserObj();	\n	//手工触发标志\n	boolean bManualFlag = \"MANUAL\".equalsIgnoreCase(nc.vo.scm.pu.PuPubVO.getString_TrimZeroLenAsNull(oUser));\n	//\n	nc.vo.pi.InvoiceVO[] inVO  = null;\n	Object inObjectOne = null;\n	Object[] inObject = null;\n	if(bManualFlag){\n		inObject  = getVos ();\n		if (inObject  == null){\n			throw new nc.vo.pub.BusinessException (nc.bs.ml.NCLangResOnserver.getInstance().getStrByID(\"4004pub\",\"UPP4004pub-000156\")/*@res \"错误：您希望弃审的发票没有数据\"*/);\n		}\n		inVO  = (nc.vo.pi.InvoiceVO[])inObject;\n	}else{\n		inObjectOne  = getVo ();\n		if (inObjectOne  == null){\n			throw new nc.vo.pub.BusinessException (nc.bs.ml.NCLangResOnserver.getInstance().getStrByID(\"4004pub\",\"UPP4004pub-000156\")/*@res \"错误：您希望弃审的发票没有数据\"*/);\n		}\n		inVO  = new nc.vo.pi.InvoiceVO[]{(nc.vo.pi.InvoiceVO)inObjectOne};\n	}\n	//\n	setParameter ( \"INVO\",inVO);\n	Object oLockRet = null;\n	try {\n		//手工触发时才做并发处理\n		if(bManualFlag){         \n			//对采购发票申请业务锁\n			oLockRet=runClass( \"nc.bs.pu.pub.PubDMO\", \"lockPkForVos\", \"&INVO:nc.vo.pub.AggregatedValueObject[]\",vo,m_keyHas,m_methodReturnHas);\n			//并发控制\n			runClass( \"nc.bs.pu.pub.PubDMO\", \"checkVosNoChanged\", \"&INVO:nc.vo.pub.AggregatedValueObject[]\",vo,m_keyHas,m_methodReturnHas);\n		}\n		//检查传应付必须配置组件\n		runClass(\"nc.bs.pi.InvoiceImpl\",\"checkDriveCRTAPBILL\",\"&INVO:nc.vo.pi.InvoiceVO[]\",vo,m_keyHas,m_methodReturnHas);\n		//设置发票的已经传应付标志\n		setParameter(\"BAPFLAG\",nc.vo.pub.lang.UFBoolean.TRUE);\n		runClass(\"nc.bs.pi.InvoiceImpl\",\"updateApFlag\",\"&INVO:nc.vo.pi.InvoiceVO[],&BAPFLAG:nc.vo.pub.lang.UFBoolean\",vo,m_keyHas,m_methodReturnHas);\n		//冲暂估应付\n		runClass(\"nc.bs.pi.InvoiceImpl\",\"adjustForZGYF\",\"&INVO:nc.vo.pi.InvoiceVO[]\",vo,m_keyHas,m_methodReturnHas);\n	}catch (Exception e) {\n		nc.bs.pu.pub.PubDMO.throwBusinessException(e);\n	}finally {\n		//解业务锁\n		if(bManualFlag && oLockRet!= null && ((nc.vo.pub.lang.UFBoolean)oLockRet).booleanValue()){\n			runClass( \"nc.bs.pu.pub.PubDMO\", \"freePkForVos\", \"&INVO:nc.vo.pub.AggregatedValueObject[]\",vo,m_keyHas,m_methodReturnHas);\n		}\n	}\n	//\n	return null;\n";}
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
