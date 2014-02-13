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
 * ��ע��null�Ķ�ִ̬���ࡣ
 *
 * �������ڣ�(2014-1-25)
 * @author ƽ̨�ű�����
 */
public class N_32H_APPROVE extends AbstractCompiler2 {
private java.util.Hashtable m_methodReturnHas=new java.util.Hashtable();
private Hashtable m_keyHas=null;
/**
 * N_32H_APPROVE ������ע�⡣
 */
public N_32H_APPROVE() {
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
	Object retObj=null;
 //####�����Ϊ����������������ʼ...���ܽ����޸�####
Object m_sysflowObj= procActionFlow(vo);
	if (m_sysflowObj!=null){
		return m_sysflowObj;
	}
 //####�����Ϊ�������������������...���ܽ����޸�####
 
 //####��Ҫ˵�������ɵ�ҵ���������������Ҫ�����޸�####
 //����˵��:���鵥������VO��ts����
	retObj=runClass("nc.bs.trade.business.HYPubBO", "setBillTs", "nc.vo.pub.AggregatedValueObject:01",vo,m_keyHas,m_methodReturnHas);
 //##################################################
	return retObj;
} catch (Exception ex) {
	if (ex instanceof BusinessException)
		throw (BusinessException) ex;
	else 
    throw new PFBusinessException(ex.getMessage(), ex);
}
}
/*
* ��ע��ƽ̨��дԭʼ�ű�
*/
public String getCodeRemark(){
	return " //####���ű����뺬�з���ֵ,����DLG��PNL������������з���ֵ####\n	Object retObj=null;\n //####�����Ϊ����������������ʼ...���ܽ����޸�####\n procActionFlow@@;\n //####�����Ϊ�������������������...���ܽ����޸�####\n \n //####��Ҫ˵�������ɵ�ҵ���������������Ҫ�����޸�####\n //����˵��:���鵥������VO��ts����\n	retObj=runClassCom@\"nc.bs.trade.business.HYPubBO\", \"setBillTs\", \"nc.vo.pub.AggregatedValueObject:01\"@;\n //##################################################\n	return retObj;\n";}
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
