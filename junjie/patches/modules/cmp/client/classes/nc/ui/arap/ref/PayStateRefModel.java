package nc.ui.arap.ref;

import java.util.Vector;
public class PayStateRefModel extends nc.ui.bd.ref.AbstractRefModel {
	/**���յ��б���*/
	private String[] m_strFieldCode = null;
	/**���յ�������*/
	private String[] m_strFieldName = null;
	/**���ձ���*/
	private String m_strTitle = "";
/**
 * WldxrefModel ������ע�⡣
 */
public PayStateRefModel() {
	super();
	initModel();
}
/**
 * ��ȡ��������ݿ���еĲ������ݣ�����άVector��
 * �������ڣ�(2001-8-23 18:39:24)
 * @return java.util.Vector
 */
public java.util.Vector getData() {
	Vector<Object> vetData = new Vector<Object>();
	Vector<Object> vetLine = new Vector<Object>();
	vetLine = new Vector<Object>();  //0Ĭ��,1ת�ʳɹ�,-1ת��ʧ�� 2ת���� 11����ת�ʳɹ� 12����ɹ�
	vetLine.addElement("0");
	vetLine.addElement("δ֧��");
	vetData.addElement(vetLine);
	vetLine = new Vector<Object>();
	vetLine.addElement("1");
	vetLine.addElement("ת�˳ɹ�");
	vetData.addElement(vetLine);
	vetLine = new Vector<Object>();
	vetLine.addElement("-11");
	vetLine.addElement("ת��ʧ��");
	vetData.addElement(vetLine);
	vetLine = new Vector<Object>();
	vetLine.addElement("2");
	vetLine.addElement("ת����");
	vetData.addElement(vetLine);
	vetLine = new Vector<Object>();
	vetLine.addElement("11");
	vetLine.addElement("����ת�ʳɹ�");
	vetData.addElement(vetLine);
	vetLine = new Vector<Object>();
	vetLine.addElement("12");
	vetLine.addElement("����ɹ�");
	vetData.addElement(vetLine);
	return vetData;
}
/**
 * ��ʾ�ֶ��б�
 * �������ڣ�(01-4-4 0:57:23)
 * @return java.lang.String
 */
public java.lang.String[] getFieldCode() {
	return m_strFieldCode;
}
/**
 * ��ʾ�ֶ�������
 * �������ڣ�(01-4-4 0:57:23)
 * @return java.lang.String
 */
public java.lang.String[] getFieldName() {
	return m_strFieldName;
}
/**
 * �����ֶ���
 * @return java.lang.String
 */
public String getPkFieldCode() {
	return getFieldCode()[0];
}
/**
 * ���ձ���
 * �������ڣ�(01-4-4 0:57:23)
 * @return java.lang.String
 */
public String getRefTitle() {
	return m_strTitle;
}
///**
// * �������ݿ�������ͼ��
// * �������ڣ�(01-4-4 0:57:23)
// * @return java.lang.String
// */
//public String getTableName() {
//	return "";
//}
/**
 * ��ȡ��������ݿ���еĲ������ݣ�����άVector��
 * �������ڣ�(2001-8-23 18:39:24)
 * @return java.util.Vector
 */
public java.util.Vector getVecData() {

	return getData();
}
/**
 * ���ܣ���ʼ��ģ��
 * ���ߣ�����
 * ����ʱ�䣺(2001-12-20 13:42:42)
 * ������<|>
 * ����ֵ��
 * �㷨��
 */
private void initModel() {
	String[] sNames = new String[]{nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UC000-0003279")/*@res "����"*/,nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UC000-0001155")/*@res "����"*/};
	String[] sCodes = new String[]{"code","name"};
	setFieldCode(sCodes);
	setFieldName(sNames);
	setRefTitle("֧��״̬");
}
/**
 * ��ȡ��������ݿ���еĲ������ݣ�����άVector��
 * �������ڣ�(2001-8-23 18:39:24)
 * @return java.util.Vector
 */
public java.util.Vector reloadData() {

	return getData();
}
/**
 * ��ʾ�ֶ��б�
 * �������ڣ�(01-4-4 0:57:23)
 * @return java.lang.String
 */
public void setFieldCode(String[] newFieldCode) {
	m_strFieldCode = newFieldCode;
}
/**
 * ��ʾ�ֶ�������
 * �������ڣ�(01-4-4 0:57:23)
 * @return java.lang.String
 */
public void setFieldName(String[] newFieldName) {
	m_strFieldName = newFieldName;
}
/**
 * ���ձ���
 * �������ڣ�(01-4-4 0:57:23)
 * @return java.lang.String
 */
public void setRefTitle(String newTitle) {
	m_strTitle = newTitle;
}
}