package nc.bs.pub.action;

import java.util.Hashtable;

import nc.bs.pub.compiler.AbstractCompiler2;
import nc.vo.pub.BusinessException;
import nc.vo.pub.compiler.PfParameterVO;
import nc.vo.uap.pf.PFBusinessException;
/**
 * ��ע���ɹ���Ʊ��ȡ����Ӧ��
���ݶ���ִ���еĶ�ִ̬����Ķ�ִ̬���ࡣ
 *
 * �������ڣ�(2008-10-17)
 * @author ƽ̨�ű�����
 */
public class N_25_DELAPBILL extends AbstractCompiler2 {
private java.util.Hashtable m_methodReturnHas=new java.util.Hashtable();
private Hashtable m_keyHas=null;
/**
 * N_25_DELAPBILL ������ע�⡣
 */
public N_25_DELAPBILL() {
	super();
}
/*
* ��ע��ƽ̨��д������
* �ӿ�ִ����
*/
public Object runComClass(PfParameterVO vo) throws BusinessException {
try{
	super.m_tmpVo=vo;
	Object[] inObject  = getVos ();
	if (inObject  == null){
		throw new nc.vo.pub.BusinessException (nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("4004pub","UPP4004pub-000156")/*@res "������ϣ������ķ�Ʊû������"*/);
	}
	nc.vo.pi.InvoiceVO[] inVO  = (nc.vo.pi.InvoiceVO[])inObject;
	setParameter ( "INVO",inVO);
	//
	Object oUser = getUserObj();
	boolean bLockCheckTs = "MANUAL".equalsIgnoreCase(nc.vo.scm.pu.PuPubVO.getString_TrimZeroLenAsNull(oUser));
	Object oLockRet = null;
	try {
		//�ֹ�����ʱ������������
		if(bLockCheckTs){         
			//�Բɹ���Ʊ����ҵ����
			oLockRet=runClass( "nc.bs.pu.pub.PubDMO", "lockPkForVos", "&INVO:nc.vo.pub.AggregatedValueObject[]",vo,m_keyHas,m_methodReturnHas);
			//��������
			runClass( "nc.bs.pu.pub.PubDMO", "checkVosNoChanged", "&INVO:nc.vo.pub.AggregatedValueObject[]",vo,m_keyHas,m_methodReturnHas);
		}
		//���÷�Ʊ�Ѿ���Ӧ����־
		setParameter("BAPFLAG",nc.vo.pub.lang.UFBoolean.FALSE);
		runClass("nc.bs.pi.InvoiceImpl","updateApFlag","&INVO:nc.vo.pi.InvoiceVO[],&BAPFLAG:nc.vo.pub.lang.UFBoolean",vo,m_keyHas,m_methodReturnHas);
		//ɾ������Ӧ������
		String key = null;
		for (int i  = 0; i < inVO.length; i  ++) {
			if(!inVO[i].isVirtual()) {
				key  = ( (nc.vo.pi.InvoiceHeaderVO)inVO[i].getParentVO ()).getPrimaryKey ();
				setParameter ( "BILLPK",key);
				//ɾ����(��ʽ)Ӧ������
				nc.itf.arap.pub.IArapBillPublic iArap = (nc.itf.arap.pub.IArapBillPublic) nc.bs.framework.common.NCLocator.getInstance().lookup(nc.itf.arap.pub.IArapBillPublic.class.getName());
				iArap.deleteOutArapBillByPk(key);       
 			}
		}
		//ɾ����(���)�ݹ�Ӧ������
		runClass("nc.bs.pi.InvoiceImpl","unAdjustForZGYF","&INVO:nc.vo.pi.InvoiceVO[]",vo,m_keyHas,m_methodReturnHas);
		//add by ouyangzhb 2011-05-09ɾ���죨��������ݹ�Ӧ����
		runClass("nc.bs.pi.InvoiceImpl","unAdjustForFEEZGYF","&INVO:nc.vo.pi.InvoiceVO[]",vo,m_keyHas,m_methodReturnHas);
//
	}catch (Exception e) {
		nc.bs.pu.pub.PubDMO.throwBusinessException(e);
	}finally {
		//��ҵ����
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
* ��ע��ƽ̨��дԭʼ�ű�
*/
public String getCodeRemark(){
	return "	Object[] inObject  = getVos ();\n	if (inObject  == null){\n		throw new nc.vo.pub.BusinessException (nc.bs.ml.NCLangResOnserver.getInstance().getStrByID(\"4004pub\",\"UPP4004pub-000156\")/*@res \"������ϣ������ķ�Ʊû������\"*/);\n	}\n	nc.vo.pi.InvoiceVO[] inVO  = (nc.vo.pi.InvoiceVO[])inObject;\n	setParameter ( \"INVO\",inVO);\n	//\n	Object oUser = getUserObj();\n	boolean bLockCheckTs = \"MANUAL\".equalsIgnoreCase(nc.vo.scm.pu.PuPubVO.getString_TrimZeroLenAsNull(oUser));\n	Object oLockRet = null;\n	try {\n		//�ֹ�����ʱ������������\n		if(bLockCheckTs){         \n			//�Բɹ���Ʊ����ҵ����\n			oLockRet=runClass( \"nc.bs.pu.pub.PubDMO\", \"lockPkForVos\", \"&INVO:nc.vo.pub.AggregatedValueObject[]\",vo,m_keyHas,m_methodReturnHas);\n			//��������\n			runClass( \"nc.bs.pu.pub.PubDMO\", \"checkVosNoChanged\", \"&INVO:nc.vo.pub.AggregatedValueObject[]\",vo,m_keyHas,m_methodReturnHas);\n		}\n		//���÷�Ʊ�Ѿ���Ӧ����־\n		setParameter(\"BAPFLAG\",nc.vo.pub.lang.UFBoolean.FALSE);\n		runClass(\"nc.bs.pi.InvoiceImpl\",\"updateApFlag\",\"&INVO:nc.vo.pi.InvoiceVO[],&BAPFLAG:nc.vo.pub.lang.UFBoolean\",vo,m_keyHas,m_methodReturnHas);\n		//ɾ������Ӧ������\n		String key = null;\n		for (int i  = 0; i < inVO.length; i  ++) {\n			if(!inVO[i].isVirtual()) {\n				key  = ( (nc.vo.pi.InvoiceHeaderVO)inVO[i].getParentVO ()).getPrimaryKey ();\n				setParameter ( \"BILLPK\",key);\n				//ɾ����(��ʽ)Ӧ������\n				nc.itf.arap.pub.IArapBillPublic iArap = (nc.itf.arap.pub.IArapBillPublic) nc.bs.framework.common.NCLocator.getInstance().lookup(nc.itf.arap.pub.IArapBillPublic.class.getName());\n				iArap.deleteOutArapBillByPk(key);       \n 			}\n		}\n		//ɾ����(���)�ݹ�Ӧ������\n		runClass(\"nc.bs.pi.InvoiceImpl\",\"unAdjustForZGYF\",\"&INVO:nc.vo.pi.InvoiceVO[]\",vo,m_keyHas,m_methodReturnHas);\n		//\n	}catch (Exception e) {\n		nc.bs.pu.pub.PubDMO.throwBusinessException(e);\n	}finally {\n		//��ҵ����\n		if(bLockCheckTs && oLockRet!= null && ((nc.vo.pub.lang.UFBoolean)oLockRet).booleanValue()){\n			runClass( \"nc.bs.pu.pub.PubDMO\", \"freePkForVos\", \"&INVO:nc.vo.pub.AggregatedValueObject[]\",vo,m_keyHas,m_methodReturnHas);\n		}\n	}\n	//\n	return null;\n";}
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
