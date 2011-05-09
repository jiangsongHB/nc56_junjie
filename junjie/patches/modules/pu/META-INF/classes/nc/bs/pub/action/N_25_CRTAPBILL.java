package nc.bs.pub.action;

import java.util.Hashtable;

import nc.bs.pub.compiler.AbstractCompiler2;
import nc.vo.pub.BusinessException;
import nc.vo.pub.compiler.PfParameterVO;
import nc.vo.uap.pf.PFBusinessException;
/**
 * ��ע���ɹ���Ʊ�Ĵ�Ӧ��
���ݶ���ִ���еĶ�ִ̬����Ķ�ִ̬���ࡣ
 *
 * �������ڣ�(2008-12-1)
 * @author ƽ̨�ű�����
 */
public class N_25_CRTAPBILL extends AbstractCompiler2 {
private java.util.Hashtable m_methodReturnHas=new java.util.Hashtable();
private Hashtable m_keyHas=null;
/**
 * N_25_CRTAPBILL ������ע�⡣
 */
public N_25_CRTAPBILL() {
	super();
}
/*
* ��ע��ƽ̨��д������
* �ӿ�ִ����
*/
public Object runComClass(PfParameterVO vo) throws BusinessException {
try{
	super.m_tmpVo=vo;
	//ҵ�����Զ������
	Object oUser = getUserObj();	
	//�ֹ�������־
	boolean bManualFlag = "MANUAL".equalsIgnoreCase(nc.vo.scm.pu.PuPubVO.getString_TrimZeroLenAsNull(oUser));
	//
	nc.vo.pi.InvoiceVO[] inVO  = null;
	Object inObjectOne = null;
	Object[] inObject = null;
	if(bManualFlag){
		inObject  = getVos ();
		if (inObject  == null){
			throw new nc.vo.pub.BusinessException (nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("4004pub","UPP4004pub-000156")/*@res "������ϣ������ķ�Ʊû������"*/);
		}
		inVO  = (nc.vo.pi.InvoiceVO[])inObject;
	}else{
		inObjectOne  = getVo ();
		if (inObjectOne  == null){
			throw new nc.vo.pub.BusinessException (nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("4004pub","UPP4004pub-000156")/*@res "������ϣ������ķ�Ʊû������"*/);
		}
		inVO  = new nc.vo.pi.InvoiceVO[]{(nc.vo.pi.InvoiceVO)inObjectOne};
	}
	//
	setParameter ( "INVO",inVO);
	Object oLockRet = null;
	try {
		//�ֹ�����ʱ������������
		if(bManualFlag){         
			//�Բɹ���Ʊ����ҵ����
			oLockRet=runClass( "nc.bs.pu.pub.PubDMO", "lockPkForVos", "&INVO:nc.vo.pub.AggregatedValueObject[]",vo,m_keyHas,m_methodReturnHas);
			//��������
			runClass( "nc.bs.pu.pub.PubDMO", "checkVosNoChanged", "&INVO:nc.vo.pub.AggregatedValueObject[]",vo,m_keyHas,m_methodReturnHas);
		}
		//��鴫Ӧ�������������
		runClass("nc.bs.pi.InvoiceImpl","checkDriveCRTAPBILL","&INVO:nc.vo.pi.InvoiceVO[]",vo,m_keyHas,m_methodReturnHas);
		//���÷�Ʊ���Ѿ���Ӧ����־
		setParameter("BAPFLAG",nc.vo.pub.lang.UFBoolean.TRUE);
		runClass("nc.bs.pi.InvoiceImpl","updateApFlag","&INVO:nc.vo.pi.InvoiceVO[],&BAPFLAG:nc.vo.pub.lang.UFBoolean",vo,m_keyHas,m_methodReturnHas);
		//���ݹ�Ӧ��
		runClass("nc.bs.pi.InvoiceImpl","adjustForZGYF","&INVO:nc.vo.pi.InvoiceVO[]",vo,m_keyHas,m_methodReturnHas);
		//add by ouyangzhb 2011-05-09 ���ݹ�����Ӧ��
		runClass("nc.bs.pi.InvoiceImpl","adjustForFeeZGYF","&INVO:nc.vo.pi.InvoiceVO[]",vo,m_keyHas,m_methodReturnHas);
	}catch (Exception e) {
		nc.bs.pu.pub.PubDMO.throwBusinessException(e);
	}finally {
		//��ҵ����
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
* ��ע��ƽ̨��дԭʼ�ű�
*/
public String getCodeRemark(){
	return "	//ҵ�����Զ������\n	Object oUser = getUserObj();	\n	//�ֹ�������־\n	boolean bManualFlag = \"MANUAL\".equalsIgnoreCase(nc.vo.scm.pu.PuPubVO.getString_TrimZeroLenAsNull(oUser));\n	//\n	nc.vo.pi.InvoiceVO[] inVO  = null;\n	Object inObjectOne = null;\n	Object[] inObject = null;\n	if(bManualFlag){\n		inObject  = getVos ();\n		if (inObject  == null){\n			throw new nc.vo.pub.BusinessException (nc.bs.ml.NCLangResOnserver.getInstance().getStrByID(\"4004pub\",\"UPP4004pub-000156\")/*@res \"������ϣ������ķ�Ʊû������\"*/);\n		}\n		inVO  = (nc.vo.pi.InvoiceVO[])inObject;\n	}else{\n		inObjectOne  = getVo ();\n		if (inObjectOne  == null){\n			throw new nc.vo.pub.BusinessException (nc.bs.ml.NCLangResOnserver.getInstance().getStrByID(\"4004pub\",\"UPP4004pub-000156\")/*@res \"������ϣ������ķ�Ʊû������\"*/);\n		}\n		inVO  = new nc.vo.pi.InvoiceVO[]{(nc.vo.pi.InvoiceVO)inObjectOne};\n	}\n	//\n	setParameter ( \"INVO\",inVO);\n	Object oLockRet = null;\n	try {\n		//�ֹ�����ʱ������������\n		if(bManualFlag){         \n			//�Բɹ���Ʊ����ҵ����\n			oLockRet=runClass( \"nc.bs.pu.pub.PubDMO\", \"lockPkForVos\", \"&INVO:nc.vo.pub.AggregatedValueObject[]\",vo,m_keyHas,m_methodReturnHas);\n			//��������\n			runClass( \"nc.bs.pu.pub.PubDMO\", \"checkVosNoChanged\", \"&INVO:nc.vo.pub.AggregatedValueObject[]\",vo,m_keyHas,m_methodReturnHas);\n		}\n		//��鴫Ӧ�������������\n		runClass(\"nc.bs.pi.InvoiceImpl\",\"checkDriveCRTAPBILL\",\"&INVO:nc.vo.pi.InvoiceVO[]\",vo,m_keyHas,m_methodReturnHas);\n		//���÷�Ʊ���Ѿ���Ӧ����־\n		setParameter(\"BAPFLAG\",nc.vo.pub.lang.UFBoolean.TRUE);\n		runClass(\"nc.bs.pi.InvoiceImpl\",\"updateApFlag\",\"&INVO:nc.vo.pi.InvoiceVO[],&BAPFLAG:nc.vo.pub.lang.UFBoolean\",vo,m_keyHas,m_methodReturnHas);\n		//���ݹ�Ӧ��\n		runClass(\"nc.bs.pi.InvoiceImpl\",\"adjustForZGYF\",\"&INVO:nc.vo.pi.InvoiceVO[]\",vo,m_keyHas,m_methodReturnHas);\n	}catch (Exception e) {\n		nc.bs.pu.pub.PubDMO.throwBusinessException(e);\n	}finally {\n		//��ҵ����\n		if(bManualFlag && oLockRet!= null && ((nc.vo.pub.lang.UFBoolean)oLockRet).booleanValue()){\n			runClass( \"nc.bs.pu.pub.PubDMO\", \"freePkForVos\", \"&INVO:nc.vo.pub.AggregatedValueObject[]\",vo,m_keyHas,m_methodReturnHas);\n		}\n	}\n	//\n	return null;\n";}
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
