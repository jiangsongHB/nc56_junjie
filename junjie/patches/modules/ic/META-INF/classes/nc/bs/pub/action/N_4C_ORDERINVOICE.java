package nc.bs.pub.action;

import nc.ui.pub.change.PfChangeBO_Client;
import nc.vo.pub.pf.PfUtilActionVO;
import nc.bs.pub.compiler.*;
import nc.vo.pub.compiler.PfParameterVO;
import nc.vo.pub.formulaset.FormulaParseFather;

import java.math.*;
import java.util.*;

import nc.vo.pub.lang.*;
import nc.bs.pub.pf.PfUtilTools;
import nc.bs.scm.pub.BillRowNoDMO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.so.credit.CreditUtil;
import nc.vo.so.so001.SaleOrderVO;
import nc.vo.so.so001.SaleorderBVO;
import nc.vo.so.so001.SaleorderHVO;
import nc.vo.so.so002.SaleVO;
import nc.vo.so.so002.SaleinvoiceBVO;
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
	
//	nc.vo.so.so001.SaleOrderVO inObject = null ;
//	nc.vo.pub.AggregatedValueObject invo1=(nc.vo.pub.AggregatedValueObject)getVo();
//	nc.vo.ic.pub.bill.GeneralBillVO invo=(nc.vo.ic.pub.bill.GeneralBillVO)invo1;
//	setParameter("GeneralBillVO",invo);
//	inObject =  (SaleOrderVO) runClass(
//			"nc.bs.ic.ic211.GeneralHBO",
//			"findOrderVO",
//			"&GeneralBillVO:nc.vo.ic.pub.bill.GeneralBillVO",
//			vo, m_keyHas,m_methodReturnHas);
//	
//	if(inObject!=null){
//		
//	}
//	// 2,数据合法，把数据转换为发票。
//	nc.vo.so.so001.SaleOrderVO inVO = (nc.vo.so.so001.SaleOrderVO) inObject;
//	nc.vo.so.so002.SaleinvoiceVO outVO = (nc.vo.so.so002.SaleinvoiceVO) changeData(
//			inVO, "30", "32");
////	nc.vo.so.so002.SaleinvoiceVO outVO =(SaleinvoiceVO) PfUtilTools.runChangeData( "30", "32",inVO,null);
	
	//wanglei 2014-04-13 考虑信用处理，调整为从出库交换
	
	nc.vo.so.so001.SaleOrderVO ordObject = null ;
	nc.vo.pub.AggregatedValueObject invo=(nc.vo.pub.AggregatedValueObject)getVo();
	nc.vo.ic.pub.bill.GeneralBillVO invVO=(nc.vo.ic.pub.bill.GeneralBillVO)invo;
	
	//处理出库单交换到发票
	nc.vo.so.so002.SaleinvoiceVO outVO1 = (nc.vo.so.so002.SaleinvoiceVO) changeData(
			invVO, "4C", "32");
	
	//获得上游订单VO
	setParameter("GeneralBillVO",invo);
	ordObject =  (SaleOrderVO) runClass(
			"nc.bs.ic.ic211.GeneralHBO",
			"findOrderVO",
			"&GeneralBillVO:nc.vo.ic.pub.bill.GeneralBillVO",
			vo, m_keyHas,m_methodReturnHas);
	
	nc.vo.so.so002.SaleinvoiceVO outVO2 = null;
	if(ordObject!=null){
		nc.vo.so.so001.SaleOrderVO inOrdVO = (nc.vo.so.so001.SaleOrderVO) ordObject;
		nc.vo.so.so001.SaleorderBVO[] inOrdBdVO = inOrdVO.getBodyVOs();
		//根据上游订单设置一下表头（这里只支持一对一的订单处理）
		outVO1.getHeadVO().setCreceiptcorpid(inOrdVO.getHeadVO().getCreceiptcorpid());
		outVO1.getHeadVO().setCcustbaseid(inOrdVO.getHeadVO().getCcustbasid());
		outVO1.getHeadVO().setCreceiptcustomerid(inOrdVO.getHeadVO().getCreceiptcustomerid());
		outVO1.getHeadVO().setCsalecorpid(inOrdVO.getHeadVO().getCsalecorpid());
		outVO1.getHeadVO().setVprintcustname(inOrdVO.getHeadVO().getCccustomername());
		outVO1.getHeadVO().setCdeptid(inOrdVO.getHeadVO().getCdeptid());
		outVO1.getHeadVO().setCemployeeid(inOrdVO.getHeadVO().getCemployeeid());
		outVO1.getHeadVO().setCcurrencyid(inOrdVO.getHeadVO().getCcurrencytypeid());
		outVO1.getHeadVO().setCreceiptcorpid(inOrdVO.getHeadVO().getCreceiptcorpid());
		outVO1.getHeadVO().setCtermprotocolid(inOrdVO.getHeadVO().getCtermprotocolid());
		
		FormulaParseFather f = new nc.bs.pub.formulaparse.FormulaParse();
		String[] formulas = new String[]{
				"custname->getColValue(\"bd_cubasdoc\", \"custname\", \"pk_cubasdoc\", getColValue(\"bd_cumandoc\",\"pk_cubasdoc\",\"pk_cumandoc\",ccustomerid))",
            };
		f.addVariable("ccustomerid", inOrdVO.getHeadVO().getCreceiptcorpid());
		f.setExpressArray(formulas);
		String[][] vOs = f.getValueSArray();
		
		outVO1.getHeadVO().setVprintcustname(vOs[0][0]); 
		//end;
		
		ArrayList<nc.vo.so.so001.SaleorderBVO> al = new ArrayList();
		for (int i = 0;i<inOrdBdVO.length; i++ ){
			//只处理订单中的费用和折扣类型
			if ((inOrdBdVO[i].getLaborflag() != null ||inOrdBdVO[i].getDiscountflag() != null) &&   
					(inOrdBdVO[i].getLaborflag().booleanValue() || inOrdBdVO[i].getDiscountflag().booleanValue())){
				al.add(inOrdBdVO[i]);
			}
		}
		if (al.size() >0) {
			//转单
			inOrdVO.setChildrenVO(al.toArray(new SaleorderBVO[al.size()]));
			outVO2 = (nc.vo.so.so002.SaleinvoiceVO) changeData(
				inOrdVO, "30", "32");
		}
	}
	
	//合并来源订单的VO和出库的VO，暂时不考虑分单问题了，简化处理
	
	nc.vo.so.so002.SaleinvoiceVO outVO = outVO1;
	if (outVO2 != null){
		ArrayList<SaleinvoiceBVO> al = new ArrayList();
		SaleinvoiceBVO[] bvo1 = outVO.getBodyVO();
		for (int i = 0 ; i< bvo1.length; i++){
			al.add(bvo1[i]);
		}
		SaleinvoiceBVO[] bvo2 = outVO2.getBodyVO();
		for (int i = 0 ; i< bvo2.length; i++){
			al.add(bvo2[i]);
		}
		outVO.setChildrenVO(al.toArray(new SaleinvoiceBVO[al.size()]));
	}
	
	BillRowNoDMO.setVORowNoByRule(outVO.getChildrenVO(), "32", "crowno");
	//end 2014-04-13
	
	// 2,数据合法，把数据转换为发票。
	//nc.vo.so.so001.SaleOrderVO inOrdVO = (nc.vo.so.so001.SaleOrderVO) ordObject;
	//nc.vo.so.so002.SaleinvoiceVO outVO2 = (nc.vo.so.so002.SaleinvoiceVO) changeData(
	//		inOrdVO, "30", "32");
//	nc.vo.so.so002.SaleinvoiceVO outVO =(SaleinvoiceVO) PfUtilTools.runChangeData( "30", "32",inVO,null);
	
	
	
	//add by ouyangzhb 2013-01-24 增加表头合计
	setInvoiceHeadMny(outVO);
	
//	inObject = null;
//	inVO = null;
	Object retObj = null;
	// ####################################################################################################
	// ##该组件为单动作处理开始,必须修改参数,如果不置入参数值，系统默认为null##
	// ###动作名称-->PFACTION###
	setParameter("PFACTION", "PreKeep");
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

/**
 * add by ouyangzhb 2013-01-24 计算发票表头合计
 * @param vo
 */
private void setInvoiceHeadMny(SaleinvoiceVO vo) {
	if (vo == null || vo.getParentVO() == null || vo.getChildrenVO() == null || vo.getChildrenVO().length < 1)
		return;
	SaleVO headVO = (SaleVO) vo.getParentVO();
	UFDouble ntotalsummny = CreditUtil.ZERO;
	SaleinvoiceBVO[] items = (SaleinvoiceBVO[]) vo.getChildrenVO();
	for (int i = 0; i < items.length; i++) {
		if (items[i].getBlargessflag() == null || !items[i].getBlargessflag().booleanValue()) {
			ntotalsummny = ntotalsummny
					.add(new CreditUtil().convertObjToUFDouble(items[i].getNoriginalcursummny()));
		}
	}
	headVO.setNtotalsummny(ntotalsummny);
	headVO.setNnetmny(ntotalsummny);
	headVO.setNstrikemny(CreditUtil.ZERO);
}

}
