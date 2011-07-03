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
 * ��ע��������۳��ⵥ�Ĺر����ζ���
���ݶ���ִ���еĶ�ִ̬����Ķ�ִ̬���ࡣ
 *
 * �������ڣ�(2008-9-1)
 * @author ƽ̨�ű�����
 */
public class N_4C_ORDERINVOICE extends AbstractCompiler2 {
private java.util.Hashtable m_methodReturnHas=new java.util.Hashtable();
private Hashtable m_keyHas=null;
/**
 * N_4C_CLOSEORDER ������ע�⡣
 */
public N_4C_ORDERINVOICE() {
	super();
}
/*
* ��ע��ƽ̨��д������
* �ӿ�ִ����
*/
public Object runComClass(PfParameterVO vo) throws BusinessException {
try{
	super.m_tmpVo=vo;
	//####���ű����뺬�з���ֵ,����DLG��PNL������������з���ֵ####
//*************��ƽ̨ȡ���ɸö����������ڲ�����������ȡ����Ҫ�����VO��***********
	
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
	// 2,���ݺϷ���������ת��Ϊ��Ʊ��
	nc.vo.so.so001.SaleOrderVO inVO = (nc.vo.so.so001.SaleOrderVO) inObject;
	nc.vo.so.so002.SaleinvoiceVO outVO = (nc.vo.so.so002.SaleinvoiceVO) changeData(
			inVO, "30", "32");
//	nc.vo.so.so002.SaleinvoiceVO outVO =(SaleinvoiceVO) PfUtilTools.runChangeData( "30", "32",inVO,null);
	
	
	inObject = null;
	inVO = null;
	Object retObj = null;
	// ####################################################################################################
	// ##�����Ϊ����������ʼ,�����޸Ĳ���,������������ֵ��ϵͳĬ��Ϊnull##
	// ###��������-->PFACTION###
	setParameter("PFACTION", "PUSHWRITE");
	// ###��������-->PFBILLTYPE###
	setParameter("PFBILLTYPE", "32");
	// ###��ǰ����-->PFDATE###
	setParameter("PFDATE", getUserDate().toString());
	// ###����VO-->PFVO###
	setParameter("PFVO", outVO);
	// ###�������-->PFUSEROBJ###
	runClass(
			"nc.bs.pub.pf.PfUtilBO",
			"processAction",
			"&PFACTION:String,&PFBILLTYPE:String,&PFDATE:String,&PFFLOW:nc.vo.pub.pf.PfUtilWorkFlowVO,&PFVO:nc.vo.pub.AggregatedValueObject, &PFUSEROBJ:Object",
			vo, m_keyHas, m_methodReturnHas);
	if (retObj != null) {
		m_methodReturnHas.put("processAction", retObj);
	}
	// ####........�����Ϊ�������������............####
	// ####################################################################################################
	// *******************ִ�ж�����Ʊ���ҵ����**********************
	// ************************************************************************
	// *********���ؽ��******************************************************
	outVO = null;
	if (retObj != null && !(retObj instanceof String))
		throw new nc.vo.pub.BusinessException(
						nc.bs.ml.NCLangResOnserver.getInstance()
								.getStrByID("sopub", "UPPsopub-000261")/*
																		 * @res
																		 * "�������۶������涯���ķ���ֵ���ʹ���"
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
* ��ע��ƽ̨��дԭʼ�ű�
*/
public String getCodeRemark(){
	return "	//####���ű����뺬�з���ֵ,����DLG��PNL������������з���ֵ####\n//*************��ƽ̨ȡ���ɸö����������ڲ�����������ȡ����Ҫ�����VO��***********\nObject retObj=null;\nnc.vo.pub.AggregatedValueObject invo1=(nc.vo.pub.AggregatedValueObject)getVo();\nnc.vo.ic.pub.bill.GeneralBillVO invo=(nc.vo.ic.pub.bill.GeneralBillVO)invo1;\nsetParameter(\"INCURVO\",invo);\nrunClassCom@\"nc.bs.ic.ic211.GeneralHBO\",\"closeSOOrder\",\"&INCURVO:nc.vo.ic.pub.bill.GeneralBillVO\"@;\n return invo;  \n //************************************************************************\n";}
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
