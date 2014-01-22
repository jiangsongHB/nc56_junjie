package nc.bs.pub.action;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import nc.bs.pub.compiler.AbstractCompiler2;
import nc.vo.pub.BusinessException;
import nc.vo.pub.compiler.PfParameterVO;
import nc.vo.uap.pf.PFBusinessException;

/**
 * ��ע������������
 * 
 * �������ڣ�
 * 
 * @author 
 */
public class N_JA01_SAVE extends AbstractCompiler2 {
	private java.util.Hashtable m_methodReturnHas = new java.util.Hashtable();
	private Hashtable m_keyHas = null;

	/**
	 * N_36GL_UNAPPROVE ������ע�⡣
	 */
	public N_JA01_SAVE() {
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
		Object retObj  =runClass( "nc.bs.trade.comstatus.BillCommit", "commitBill", "nc.vo.pub.AggregatedValueObject:01",vo,m_keyHas,m_methodReturnHas);
		return retObj;
	} catch (Exception ex) {
		if (ex instanceof BusinessException)
			throw (BusinessException) ex;
		else 
	    throw new PFBusinessException(ex.getMessage(), ex);
	}
	}
	 /**
	 * @function ����tablename���name
	 * @param tablename
	 * @param name
	 * @param colNm
	 * @param id
	 * @return
	 */
	public String getNameByID(String tablename, String name, String colNm,
			String id) {

		nc.bs.pub.formulaparse.FormulaParse parse = new nc.bs.pub.formulaparse.FormulaParse();
		String express = "name->getColValue(\""
				+ tablename
				+ "\", \""
				+ name
				+ "\", \""
				+ colNm
				+ "\", value)";
		// ���ù�ʽ
		parse.setExpress(express);
		// ��Ӳ���
		List<String> list = new ArrayList<String>();
		list.add(id);
		parse.addVariable("value", list);
		// ���
		String[] values = parse.getValueS();
		return values == null ? null : values[0];
	}
	 
	/*
	 * ��ע��ƽ̨��дԭʼ�ű�
	 */
	public String getCodeRemark() {
		return nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("3620add", "UPP3620ADD-000173")/*
																									 * @res
																									 * "	//####���ű����뺬�з���ֵ,����DLG��PNL������������з���ֵ####\n	Object retObj=null;\n	return retObj;\n"
																									 */;
	}

	/*
	 * ��ע�����ýű�������HAS
	 */
	private void setParameter(String key, Object val) {
		if (m_keyHas == null) {
			m_keyHas = new Hashtable();
		}
		if (val != null) {
			m_keyHas.put(key, val);
		}
	}
}