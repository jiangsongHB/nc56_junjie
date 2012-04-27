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
 * ��ע������̵㵥��ȡ������
���ݶ���ִ���еĶ�ִ̬����Ķ�ִ̬���ࡣ
 *
 * �������ڣ�(2008-9-27)
 * @author ƽ̨�ű�����
 */
public class N_4R_CANCELADJUST extends AbstractCompiler2 {
private java.util.Hashtable m_methodReturnHas=new java.util.Hashtable();
private Hashtable m_keyHas=null;
/**
 * N_4R_CANCELADJUST ������ע�⡣
 */
public N_4R_CANCELADJUST() {
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
Object inCurObject=getVo();
Object retObj=null;
//1,���ȼ�鴫����������Ƿ�Ϸ����Ƿ�Ϊ�ա�
if(!(inCurObject instanceof  nc.vo.ic.pub.bill.SpecialBillVO)) throw new nc.vo.pub.BusinessException("Remote Call",new nc.vo.pub.BusinessException("���󣺵������Ͳ�ƥ��"));
if(inCurObject == null)  throw new nc.vo.pub.BusinessException("Remote Call",new nc.vo.pub.BusinessException("���󣺵���û������"));
//2,���ݺϷ���������ת��Ϊ��浥�ݡ�
nc.vo.ic.pub.bill.SpecialBillVO inCurVO=null;
if(inCurObject !=null)
 inCurVO=(nc.vo.ic.pub.bill.SpecialBillVO)inCurObject;
inCurObject=null;
//��ȡƽ̨����Ĳ���
setParameter("INCURVO",inCurVO);
//����˵��:���ݼ�ҵ����
runClass("nc.bs.ic.pub.bill.ICLockBO","lockBill","&INCURVO:nc.vo.pub.AggregatedValueObject",vo,m_keyHas,m_methodReturnHas);
//����˵��:����浥��ʱ���
runClass("nc.bs.ic.pub.check.CheckDMO","checkTimeStamp","&INCURVO:nc.vo.pub.AggregatedValueObject",vo,m_keyHas,m_methodReturnHas);

/**add by ouyangzhb 2012-04-26 ��ȡ��������ʱ����Ҫͬʱɾ������ʱ�����ɵ��뵥�������Ϣ�������뵥�ִ���*/
String billpk = inCurVO.getPrimaryKey();
setParameter("STR",billpk);
runClass("nc.bs.ic.md.MDToolsImpl","cancelMD","&STR:String",vo,m_keyHas,m_methodReturnHas);
/**add end */



//����˵��:����ȡ������
//####��Ҫ˵�������ɵ�ҵ���������������Ҫ�����޸�####
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
* ��ע��ƽ̨��дԭʼ�ű�
*/
public String getCodeRemark(){
	return "	//####���ű����뺬�з���ֵ,����DLG��PNL������������з���ֵ####\n//*************��ƽ̨ȡ���ɸö����������ڲ�����������ȡ����Ҫ�����VO��***********\nObject inCurObject=getVo();\nObject retObj=null;\n//1,���ȼ�鴫����������Ƿ�Ϸ����Ƿ�Ϊ�ա�\nif(!(inCurObject instanceof  nc.vo.ic.pub.bill.SpecialBillVO)) throw new nc.vo.pub.BusinessException(\"Remote Call\",new nc.vo.pub.BusinessException(\"���󣺵������Ͳ�ƥ��\"));\nif(inCurObject == null)  throw new nc.vo.pub.BusinessException(\"Remote Call\",new nc.vo.pub.BusinessException(\"���󣺵���û������\"));\n//2,���ݺϷ���������ת��Ϊ��浥�ݡ�\nnc.vo.ic.pub.bill.SpecialBillVO inCurVO=null;\nif(inCurObject !=null)\n inCurVO=(nc.vo.ic.pub.bill.SpecialBillVO)inCurObject;\ninCurObject=null;\n//��ȡƽ̨����Ĳ���\nsetParameter(\"INCURVO\",inCurVO);\n//����˵��:���ݼ�ҵ����\nrunClassCom@\"nc.bs.ic.pub.bill.ICLockBO\",\"lockBill\",\"&INCURVO:nc.vo.pub.AggregatedValueObject\"@;\n//����˵��:����浥��ʱ���\nrunClassCom@\"nc.bs.ic.pub.check.CheckDMO\",\"checkTimeStamp\",\"&INCURVO:nc.vo.pub.AggregatedValueObject\"@;\n//����˵��:����ȡ������\n//####��Ҫ˵�������ɵ�ҵ���������������Ҫ�����޸�####\nrunClassCom@\"nc.bs.ic.ic261.SpecialHBO\",\"deleteGeneralbills\",\"&INCURVO:nc.vo.ic.pub.bill.SpecialBillVO\"@;\n//##################################################\ninCurVO=null;\nreturn null;\n//************************************************************************\n";}
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
