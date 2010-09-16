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
	 * AccsubjTypeRefModel 构造子注解。
	 */
	public InvtypeModel() {
		super();
	}

	

	public String getPk_invcl() {
		return pk_invcl;
	}
	/**
	 * getDefaultFieldCount 方法注解。
	 */
	public int getDefaultFieldCount() {
		return 4;
	}
	/**
	 * Order子句。
	 * 
	 * @return java.lang.String
	 */
//	public String getOrderPart() {
//		
//		return "pk_corp";
//	}
	/**
	 * 参照数据库字段名数组
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
	 * 和数据库字段名数组对应的中文名称数组
	 * @return java.lang.String
	 */
//	public java.lang.String[] getFieldName() {
//		return new String[] {"分类编码","材质","类别名称","主键"};
//	}
//	public java.lang.String[] getFieldName() {
//		return new String[] {"材质","主键"};
//	}
	public java.lang.String[] getFieldName() {
		return new String[] {"材质"};
	}
	/**
	 * 不显示字段列表
	 * @return java.lang.String
	 */
//	public java.lang.String[] getHiddenFieldCode() {
//		return new String[]{"invcode","invname","pk_invbasdoc"};
//	}
//	public java.lang.String[] getHiddenFieldCode() {
//	return new String[]{"pk_invcl"};
//}
	/**
	 * 此处插入方法说明。
	 * @return java.lang.String
	 */

	/**
	 * 要返回的主键字段名i.e. pk_deptdoc
	 * @return java.lang.String
	 */
	public String getPkFieldCode() {
		return "pk_invcl";
	}
	/**
	 * 参照标题
	 * @return java.lang.String
	 */
	public String getRefTitle() {
		return "材质参照";
	}
	/**
	 * 参照数据库表或者视图名
	 * @return java.lang.String
	 */
	public String getTableName() {		
		return "bd_invbasdoc" ;		
	}

	/**
	 * 此处添加公司编码
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
	 * 此处插入方法说明。
	 * @param newPk_corp java.lang.String
	 */

	
}


