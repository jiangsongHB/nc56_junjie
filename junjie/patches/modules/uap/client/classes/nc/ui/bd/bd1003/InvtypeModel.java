package nc.ui.bd.bd1003;

import java.util.ArrayList;

import nc.jdbc.framework.processor.ArrayListProcessor;
import nc.ui.bd.ref.AbstractRefTreeModel;
import nc.ui.dbcache.DBCacheFacade;
import nc.ui.mo.mo1034.BillCardPanel;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.bill.BillEditEvent;

public class InvtypeModel extends nc.ui.bd.ref.AbstractRefModel{
	

		private String pk_invcl ;

	/**
	 * AccsubjTypeRefModel ������ע�⡣
	 */
	public InvtypeModel() {
		super();
	}

	

	public String getPk_invcl() {
		return pk_invcl;
	}
	/**
	 * getDefaultFieldCount ����ע�⡣
	 */
	public int getDefaultFieldCount() {
		return 4;
	}
	/**
	 * Order�Ӿ䡣
	 * 
	 * @return java.lang.String
	 */
//	public String getOrderPart() {
//		
//		return "pk_corp";
//	}
	/**
	 * �������ݿ��ֶ�������
	 * @return java.lang.String 
	 */
//	public java.lang.String[] getFieldCode() {
//		return new String[]{"invcode","invtype","invname","pk_invbasdoc"};
//	}
//	public java.lang.String[] getFieldCode() {
//		return new String[]{"invtype","pk_invcl"};
//	}
	public java.lang.String[] getFieldCode() {
		return new String[]{"invtype"};
	}

	/**
	 * �����ݿ��ֶ��������Ӧ��������������
	 * @return java.lang.String
	 */
//	public java.lang.String[] getFieldName() {
//		return new String[] {"�������","����","�������","����"};
//	}
//	public java.lang.String[] getFieldName() {
//		return new String[] {"����","����"};
//	}
	public java.lang.String[] getFieldName() {
		return new String[] {"����"};
	}
	/**
	 * ����ʾ�ֶ��б�
	 * @return java.lang.String
	 */
//	public java.lang.String[] getHiddenFieldCode() {
//		return new String[]{"invcode","invname","pk_invbasdoc"};
//	}
//	public java.lang.String[] getHiddenFieldCode() {
//	return new String[]{"pk_invcl"};
//}
	/**
	 * �˴����뷽��˵����
	 * @return java.lang.String
	 */

	/**
	 * Ҫ���ص������ֶ���i.e. pk_deptdoc
	 * @return java.lang.String
	 */
	public String getPkFieldCode() {
		return "pk_invcl";
	}
	/**
	 * ���ձ���
	 * @return java.lang.String
	 */
	public String getRefTitle() {
		return "���ʲ���";
	}
	/**
	 * �������ݿ�������ͼ��
	 * @return java.lang.String
	 */
	public String getTableName() {		
		return "bd_invbasdoc" ;		
	}

	/**
	 * �˴���ӹ�˾����
	 * @return java.lang.String
	 */
	//private ClientUI uipanel;
//	public String getWherePart() {
//		whereSql.append("  ");
		
//		String strWherePart=super.getWherePart();	
//		if((null!=strWherePart)&&(!"".equals(strWherePart.trim()))){
//			strWherePart +="  ";
//		}
//		else 
//			strWherePart="pk_invbasdoc ='"+pk_invbasdoc+"' and invtype != null";		
//
//		return strWherePart;
		//wsy-end
		
//	if(whereSql==null&&pk_invcl!=null){
//			whereSql.append(" pk_invcl ='"+getPk_invcl()+"' and invtype != null");
//		}
//				return whereSql.toString();



//	}
	/**
	 * �˴����뷽��˵����
	 * @param newPk_corp java.lang.String
	 */

	
}


