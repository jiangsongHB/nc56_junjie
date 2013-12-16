package nc.bs.pub.action;

import java.util.Hashtable;

import nc.bs.pub.compiler.AbstractCompiler2;
import nc.vo.pub.BusinessException;
import nc.vo.pub.compiler.PfParameterVO;
import nc.vo.scm.pu.BillTypeConst;
import nc.vo.uap.pf.PFBusinessException;
/**
 * ��ע���ɹ���Ʊ������
���ݶ���ִ���еĶ�ִ̬����Ķ�ִ̬���ࡣ
 *
 * �������ڣ�(2006-10-9)
 * @author ƽ̨�ű�����
 */
public class N_25_DISCARD extends AbstractCompiler2 {
private java.util.Hashtable m_methodReturnHas=new java.util.Hashtable();
private Hashtable m_keyHas=null;
/**
 * N_25_DISCARD ������ע�⡣
 */
public N_25_DISCARD() {
	super();
}
/*
* ��ע��ƽ̨��д������
* �ӿ�ִ����
*/
public Object runComClass(PfParameterVO vo) throws BusinessException {
try{
	super.m_tmpVo=vo;
	Object[] inObject =getVos ();
		Object[] inObject1  = getUserObjAry ();
		if (inObject  == null) throw new nc.vo.pub.BusinessException ("������ϣ����������Ϸ�Ʊû������");
		nc.vo.pi.InvoiceVO[] inVO = (nc.vo.pi.InvoiceVO[])inObject;
		Object retObj = null;
		setParameter ( "INVO",inVO);
		setParameter ( "INVO1",inObject1);
		Object oLockRet = null;
		try {
	     //���ο������֧�� by zhaoyha at 2009.1.19
      nc.bs.scm.plugin.InvokeEventProxy iep = new nc.bs.scm.plugin.InvokeEventProxy("pu",BillTypeConst.PO_INVOICE);
      iep.beforeAction(nc.vo.scm.plugin.Action.DELETE, inVO, null);

			//�Բɹ���Ʊ����ҵ����
			oLockRet=runClass( "nc.bs.pu.pub.PubDMO", "lockPkForVos", "&INVO:nc.vo.pub.AggregatedValueObject[]",vo,m_keyHas,m_methodReturnHas);
			//��������
			runClass("nc.bs.pu.pub.PubDMO", "checkVosNoChanged", "&INVO:nc.vo.pub.AggregatedValueObject[]",vo,m_keyHas,m_methodReturnHas);
			 
			/**add by ouyangzhb 2013-10-14 �������Ϊ�ݹ�Ӧ�����ģ���Ҫ��д�ۼƿ�Ʊ����**/
		      runClass("nc.bs.pi.InvoiceImpl", "reWriteInvoicenumByDel","&INVO:nc.vo.pi.InvoiceVO[]",vo,m_keyHas,m_methodReturnHas);
			
			//����˵��:���Ϸ�Ʊ
			retObj =runClass( "nc.bs.pi.InvoiceImpl", "discardInvoiceArray", "&INVO:nc.vo.pi.InvoiceVO[],&INVO1:OBJECT[]",vo,m_keyHas,m_methodReturnHas);
			//####���ݺ��˻ش���####
			for(int i = 0; i < inVO.length; i ++){
				setParameter ( "RETVO",inVO[i]);
				runClass("nc.bs.pu.pub.GetSysBillCode","returnBillNo","&RETVO:nc.vo.pub.AggregatedValueObject",vo,m_keyHas,m_methodReturnHas);
			}
      //���ο������֧�� by zhaoyha at 2009.1.19
      iep.afterAction(nc.vo.scm.plugin.Action.DELETE, inVO, null);

		}catch (Exception e) {
			nc.bs.pu.pub.PubDMO.throwBusinessException("nc.bs.pub.action.N_25_DISCARD",e);
		}finally {
			//��ҵ����
			if(oLockRet!= null && ((nc.vo.pub.lang.UFBoolean)oLockRet).booleanValue())
				runClass("nc.bs.pu.pub.PubDMO", "freePkForVos", "&INVO:nc.vo.pub.AggregatedValueObject[]",vo,m_keyHas,m_methodReturnHas);
		}

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
	return "	Object[] inObject =getVos ();\n		Object[] inObject1  = getUserObjAry ();\n		if (inObject  == null) throw new nc.vo.pub.BusinessException (\"������ϣ����������Ϸ�Ʊû������\");\n		nc.vo.pi.InvoiceVO[] inVO = (nc.vo.pi.InvoiceVO[])inObject;\n		Object retObj = null;\n		setParameter ( \"INVO\",inVO);\n		setParameter ( \"INVO1\",inObject1);\n		Object oLockRet = null;\n		try {\n			//�Բɹ���Ʊ����ҵ����\n			oLockRet=runClassCom@ \"nc.bs.pu.pub.PubDMO\", \"lockPkForVos\", \"&INVO:nc.vo.pub.AggregatedValueObject[]\"@;\n			//��������\n			runClassCom@\"nc.bs.pu.pub.PubDMO\", \"checkVosNoChanged\", \"&INVO:nc.vo.pub.AggregatedValueObject[]\"@;\n			//����˵��:���Ϸ�Ʊ\n			retObj =runClassCom@ \"nc.bs.pi.InvoiceImpl\", \"discardInvoiceArray\", \"&INVO:nc.vo.pi.InvoiceVO[],&INVO1:OBJECT[]\"@;\n			//####���ݺ��˻ش���####\n			for(int i = 0; i < inVO.length; i ++){\n				setParameter ( \"RETVO\",inVO[i]);\n				runClassCom@\"nc.bs.pu.pub.GetSysBillCode\",\"returnBillNo\",\"&RETVO:nc.vo.pub.AggregatedValueObject\"@;\n			}\n		}catch (Exception e) {\n			nc.bs.pu.pub.PubDMO.throwBusinessException(\"nc.bs.pub.action.N_25_DISCARD\",e);\n		}finally {\n			//��ҵ����\n			if(oLockRet!= null && ((nc.vo.pub.lang.UFBoolean)oLockRet).booleanValue())\n				runClassCom@\"nc.bs.pu.pub.PubDMO\", \"freePkForVos\", \"&INVO:nc.vo.pub.AggregatedValueObject[]\"@;\n		}\n		return retObj;\n";}
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
