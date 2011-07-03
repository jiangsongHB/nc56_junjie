package nc.bs.pub.action;

import nc.ui.pub.change.PfChangeBO_Client;
import nc.vo.pub.pf.PfUtilActionVO;
import nc.bs.pub.compiler.*;
import nc.vo.pub.compiler.PfParameterVO;
import java.math.*;
import java.util.*;
import nc.vo.pub.lang.*;
import nc.bs.pub.pf.PfUtilTools;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.so.so001.SaleOrderVO;
import nc.vo.so.so002.SaleinvoiceVO;
import nc.vo.uap.pf.PFBusinessException;
/**
 * 备注：库存销售出库单的关闭上游订单
单据动作执行中的动态执行类的动态执行类。
 *
 * 创建日期：(2008-9-1)
 * @author 平台脚本生成
 */
public class N_4C_ORDERINVOICE extends AbstractCompiler2 {
private java.util.Hashtable m_methodReturnHas=new java.util.Hashtable();
private Hashtable m_keyHas=null;
/**
 * N_4C_CLOSEORDER 构造子注解。
 */
public N_4C_ORDERINVOICE() {
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
	
	nc.vo.so.so001.SaleOrderVO inObject = null ;
	nc.vo.pub.AggregatedValueObject invo1=(nc.vo.pub.AggregatedValueObject)getVo();
	nc.vo.ic.pub.bill.GeneralBillVO invo=(nc.vo.ic.pub.bill.GeneralBillVO)invo1;
	setParameter("GeneralBillVO",invo);
	inObject =  (SaleOrderVO) runClass(
			"nc.bs.ic.ic211.GeneralHBO",
			"findOrderVO",
			"&GeneralBillVO:nc.vo.ic.pub.bill.GeneralBillVO",
			vo, m_keyHas,m_methodReturnHas);
	
	if(inObject!=null){
		
	}
	// 2,数据合法，把数据转换为发票。
	nc.vo.so.so001.SaleOrderVO inVO = (nc.vo.so.so001.SaleOrderVO) inObject;
	nc.vo.so.so002.SaleinvoiceVO outVO = (nc.vo.so.so002.SaleinvoiceVO) changeData(
			inVO, "30", "32");
//	nc.vo.so.so002.SaleinvoiceVO outVO =(SaleinvoiceVO) PfUtilTools.runChangeData( "30", "32",inVO,null);
	
	
	inObject = null;
	inVO = null;
	Object retObj = null;
	// ####################################################################################################
	// ##该组件为单动作处理开始,必须修改参数,如果不置入参数值，系统默认为null##
	// ###动作名称-->PFACTION###
	setParameter("PFACTION", "PUSHWRITE");
	// ###单据类型-->PFBILLTYPE###
	setParameter("PFBILLTYPE", "32");
	// ###当前日期-->PFDATE###
	setParameter("PFDATE", getUserDate().toString());
	// ###输入VO-->PFVO###
	setParameter("PFVO", outVO);
	// ###输入对象-->PFUSEROBJ###
	runClass(
			"nc.bs.pub.pf.PfUtilBO",
			"processAction",
			"&PFACTION:String,&PFBILLTYPE:String,&PFDATE:String,&PFFLOW:nc.vo.pub.pf.PfUtilWorkFlowVO,&PFVO:nc.vo.pub.AggregatedValueObject, &PFUSEROBJ:Object",
			vo, m_keyHas, m_methodReturnHas);
	if (retObj != null) {
		m_methodReturnHas.put("processAction", retObj);
	}
	// ####........该组件为单动作处理结束............####
	// ####################################################################################################
	// *******************执行订单开票后的业务处理**********************
	// ************************************************************************
	// *********返回结果******************************************************
	outVO = null;
	if (retObj != null && !(retObj instanceof String))
		throw new nc.vo.pub.BusinessException(
						nc.bs.ml.NCLangResOnserver.getInstance()
								.getStrByID("sopub", "UPPsopub-000261")/*
																		 * @res
																		 * "错误：销售订单保存动作的返回值类型错误。"
																		 */);
	return retObj;
	// ************************************************************************
} catch (Exception ex) {
	if(ex instanceof BusinessException)
		throw (BusinessException)ex;
	throw new BusinessException(ex.getMessage());
}
}
/*
* 备注：平台编写原始脚本
*/
public String getCodeRemark(){
	return "	//####本脚本必须含有返回值,返回DLG和PNL的组件不允许有返回值####\n//*************从平台取得由该动作传入的入口参数。本方法取得需要保存的VO。***********\nObject retObj=null;\nnc.vo.pub.AggregatedValueObject invo1=(nc.vo.pub.AggregatedValueObject)getVo();\nnc.vo.ic.pub.bill.GeneralBillVO invo=(nc.vo.ic.pub.bill.GeneralBillVO)invo1;\nsetParameter(\"INCURVO\",invo);\nrunClassCom@\"nc.bs.ic.ic211.GeneralHBO\",\"closeSOOrder\",\"&INCURVO:nc.vo.ic.pub.bill.GeneralBillVO\"@;\n return invo;  \n //************************************************************************\n";}
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
