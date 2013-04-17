package nc.util.so.junjie;

public class JunjiePubTool {
	
	 /**
	  * @function 根据tablename获得name
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
	  // 设置公式
	  parse.setExpress(express);
	  // 添加参数
	  java.util.List<String> list = new java.util.ArrayList<String>();
	  list.add(id);
	  parse.addVariable("value", list);
	  // 结果
	  String[] values = parse.getValueS();
	  return values == null ? null : values[0];
	 }

}
