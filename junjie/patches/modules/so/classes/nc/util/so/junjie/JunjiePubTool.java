package nc.util.so.junjie;

public class JunjiePubTool {
	
	 /**
	  * @function ����tablename���name
	  * @param tablename
	  * @param name
	  * @param colNm
	  * @param id
	  * @return
	  */
	 public static String getNameByID(String tablename, String name, String colNm,
	   String id) {
	 
	  nc.ui.pub.formulaparse.FormulaParse parse = new nc.ui.pub.formulaparse.FormulaParse();
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
	  java.util.List<String> list = new java.util.ArrayList<String>();
	  list.add(id);
	  parse.addVariable("value", list);
	  // ���
	  String[] values = parse.getValueS();
	  return values == null ? null : values[0];
	 }

}
