package nc.vo.ml;

import nc.vo.jcom.lang.StringUtil;
import nc.vo.ml.translator.ITranslator;

/**
 * @author zsb
 * Created on 2004-10-12
 *
 */
public abstract class AbstractNCLangRes{
	/**
	 * 所有语言 
	 */
	protected Language[] allLanguages = null;

	private java.util.HashMap hm_AllLanguages = null;//语言map,key是语言code，value是语言VO

	private ResWrite4Upload resWrite = new ResWrite4Upload();	
	
/**抽象方法,具体子类需要实现,返回所有语言VO
 * @return 所有语言VO
 */
public abstract Language[] getAllLanguages();

/** 组织语言map，并且返回语言map
 * @return 语言map，key是语言code，value是语言VO
 */
private  java.util.HashMap getAllLanguagesAsHM() {
	synchronized(AbstractNCLangRes.class){
		if (hm_AllLanguages == null) {
			hm_AllLanguages = new java.util.HashMap();
			Language[] alllang = getAllLanguages();
			int count = alllang == null ? 0 : alllang.length;
			for (int i = 0; i < count; i++){
				hm_AllLanguages.put(alllang[i].getCode(), alllang[i]);
			}
		}
	}
	return hm_AllLanguages;
}

/**
 * 根据语言编码返回语言VO,通过语言map获取
 * 创建日期：(2004-9-13 13:39:37)
 * @return nc.vo.ml.Language 根据语言编码返回语言VO
 * @param langCode java.lang.String 语言编码
 */
public nc.vo.ml.Language getLanguage(java.lang.String langCode) {
	return (Language)getAllLanguagesAsHM().get(langCode);
}


/**
根据产品编码,简体中文,资源ID返回翻译的字符串
* @return String 翻译后的字符串
* @param productCode java.lang.String 产品编码
* @param simpleChinese java.lang.String 将要翻译的简体中文字符串
* @param resId java.lang.String 将要翻译的简体中文字符串的资源ID
*/
public String getString(String  productCode,  String  simpleChinese,  String  resId){
	String str = getStringByPath(productCode, simpleChinese,resId, null);
	return str;
}
/**
根据产品编码,资源ID返回翻译的字符串
* @return String 翻译后的字符串
* @param productCode java.lang.String 产品编码
* @param resId java.lang.String 将要翻译的简体中文字符串的资源ID
*/
public String getStrByID(String  productCode, String  resId){
	String str = getStringByPath(productCode, null,resId, null);
	return str;
}

/**
根据产品编码,简体中文,资源ID,搜索路径返回翻译的字符串
* @return String 翻译后的字符串
* @param productCode java.lang.String 产品编码
* @param simpleChinese java.lang.String 将要翻译的简体中文字符串
* @param resId java.lang.String 将要翻译的简体中文字符串的资源ID
* @param path java.lang.String	翻译的资源路径
*/
public String getStringByPath(String  productCode,  String  simpleChinese,  String  resId, String path){
	String str = null;
	try {
		ITranslator translator = LanguageTranslatorFactor.getInstance().getTranslator(getCurrLanguage());
		str = getString(translator,productCode, simpleChinese, resId, path);
	} catch (Exception e) {
		e.printStackTrace();
		if(simpleChinese != null)
			return simpleChinese;
		else
			return resId;
	}
	return str;
}

/**
	根据产品编码,多个简体中文,多个资源ID
	经过翻译后返回拼接好的字符串
 * @return String 翻译后拼接好的字符串
 * @param productCode java.lang.String 产品编码
 * @param simpleChineses java.lang.String 将要翻译的多个简体中文字符串
 * @param resIDs java.lang.String 将要翻译的多个简体中文字符串的资源ID	
*/
public String getString(String  productCode,  String[]  simpleChineses, String[]  resIDs){
	return getStringByPath(productCode, simpleChineses, resIDs, null);
}
/**
根据产品编码,多个资源ID
经过翻译后返回拼接好的字符串
* @return String 翻译后拼接好的字符串
* @param productCode java.lang.String 产品编码
* @param resIDs java.lang.String 将要翻译的多个简体中文字符串的资源ID	
*/
public String getStrByID(String  productCode, String[]  resIDs){
return getStringByPath(productCode, null, resIDs, null);
}

/**
根据产品编码,多个简体中文,多个资源ID
经过翻译后返回字符串数组
* @return String[] 翻译后的字符串数组
* @param productCode java.lang.String 产品编码
* @param simpleChineses java.lang.String 将要翻译的多个简体中文字符串
* @param resIDs java.lang.String 将要翻译的多个简体中文字符串的资源ID
*/
public String[] getStrings(String  productCode,  String[]  simpleChineses, String[]  resIDs){
	return getStringsByPath(productCode, simpleChineses, resIDs, null);
}

/**
	根据产品编码,多个简体中文,多个资源ID,搜索路径
	经过翻译后返回拼接好的字符串
 * @return String 翻译后拼接好的字符串
 * @param productCode java.lang.String 产品编码
 * @param simpleChineses java.lang.String 将要翻译的多个简体中文字符串
 * @param resIDs java.lang.String 将要翻译的多个简体中文字符串的资源ID
 * @param path java.lang.String	翻译的资源路径
*/
public String getStringByPath(String  productCode,  String[]  simpleChineses, String[]  resIDs, String path){
	int count = resIDs == null ? 0 : resIDs.length;
	StringBuffer sb = new StringBuffer();
	Language lang = getCurrLanguage();
	String wordSpace = "";
	if(lang.getCode().equals(Language.ENGLISH_CODE))
		wordSpace = " ";
	for (int i = 0; i < count; i++){
		String str = getStringByPath(productCode, simpleChineses == null?null:simpleChineses[i], resIDs[i], path);
		sb.append(str).append(wordSpace);
	}
	return sb.toString();
}

/**
根据产品编码,多个简体中文,多个资源ID,搜索路径
经过翻译后返回字符串数组
* @return String[] 翻译后的字符串数组
* @param productCode java.lang.String 产品编码
* @param simpleChineses java.lang.String 将要翻译的多个简体中文字符串
* @param resIDs java.lang.String 将要翻译的多个简体中文字符串的资源ID
* @param path java.lang.String	翻译的资源路径
*/
public String[] getStringsByPath(String  productCode,  String[]  simpleChineses, String[]  resIDs, String path){
	int count = simpleChineses == null ? 0 : simpleChineses.length;
	String []retrs = new String[count];
	for (int i = 0; i < count; i++){
		retrs[i] = getStringByPath(productCode , simpleChineses[i], resIDs == null?null:resIDs[i], path);
	}
	return retrs;
}


/**
	根据简体中文,资源ID,搜索路径返回翻译的字符串,
	并用参数值来替换翻印后的字符串中的参数
	字符串中含有的参数的写法为：{index}表示该处应该用参数值数组的第index个元素来替换
	例如 {0}表示应该用参数值数组中的第一个元素来替换
 * @return String 翻译后的字符串
 * @param productCode java.lang.String 产品编码
 * @param simpleChinese java.lang.String 将要翻译的简体中文字符串
 * @param resId java.lang.String 将要翻译的简体中文字符串的资源ID
 * @param path java.lang.String	翻译的资源路径
 * @param argValues java.lang.String[]	翻译后的字符串中参数的值
*/
public String getString(String  productCode,  String  simpleChinese,  String  resId, String path, String[]argValues){
	String str = getStringByPath(productCode, simpleChinese, resId, path);
	int count = argValues == null ? 0 : argValues.length;
	for (int i = 0; i < count; i++){
		String arg = "{"+i+"}";
		str = StringUtil.replaceAllString(str, arg, argValues[i]==null? "" : argValues[i]);
//		str = str.replaceAll(arg, argValues[i]==null?"":argValues[i]);//nc.bs.mw.util.StringUtil.replaceAllString(str, arg, argValues[i]==null?"":argValues[i]);//argValues[i]在replaceAllString方法中不支持null
	}
	return str;
}
/**
根据资源ID,搜索路径返回翻译的字符串,
并用参数值来替换翻印后的字符串中的参数
字符串中含有的参数的写法为：{index}表示该处应该用参数值数组的第index个元素来替换
例如 {0}表示应该用参数值数组中的第一个元素来替换
* @return String 翻译后的字符串
* @param productCode java.lang.String 产品编码
* @param resId java.lang.String 将要翻译的简体中文字符串的资源ID
* @param path java.lang.String	翻译的资源路径
* @param argValues java.lang.String[]	翻译后的字符串中参数的值
*/
public String getStrByID(String  productCode,  String  resId, String path, String[]argValues){
String str = getStringByPath(productCode, null, resId, path);
int count = argValues == null ? 0 : argValues.length;
for (int i = 0; i < count; i++){
	String arg = "{"+i+"}";
	str = StringUtil.replaceAllString(str, arg, argValues[i]==null? "" : argValues[i]);
//	str = str.replaceAll(arg, argValues[i]==null?"":argValues[i]);//nc.bs.mw.util.StringUtil.replaceAllString(str, arg, argValues[i]==null?"":argValues[i]);//argValues[i]在replaceAllString方法中不支持null
}
return str;
}



/**
根据具体的翻译器,产品编码,简体中文,资源ID,资源路径返回翻译的字符串
* @return String 翻译后的字符串
* @param translator ITranslator 具体的翻译器
* @param productCode java.lang.String 产品编码
* @param simpleChinese java.lang.String 将要翻译的简体中文字符串
* @param resId java.lang.String 将要翻译的简体中文字符串的资源ID
* @param path java.lang.String	翻译的资源路径
*/
protected String getString(ITranslator translator,String  productCode,  String  simpleChinese,  String  resId, String path){
	String str = null;
	try {
		if (resId == null){
			new Exception("resid can't be null").printStackTrace();
			return null;
//			productCode = IProductCode.PRODUCTCODE_COMMON;//对于id为null，目前规定都要把资源放在COMMON中
//			//对于resID为null的，自动插入到本地文件中为了资源上传到数据库中（这个只是在测试环境中才能用）
//			resWrite.writeToFile4Test(StringUtil.filterString(simpleChinese));
		}
//		//增加一个分类类型来定义用户自定义的资源,分类类型为usercustom
//		try{
//			str = translator.getString(IProductCode.PRODUCTCODE_USERCUSTOM, simpleChinese, resId);
//		}catch(java.util.MissingResourceException e){		
////			System.out.println("查找资源失败："+simpleChinese+"/"+resId+"/"+productCode);
//		}
		
		//
		if (productCode != null)
			try{
				str = translator.getString(productCode, simpleChinese, resId);
			}catch(java.util.MissingResourceException e){		
//				System.out.println("查找资源失败："+simpleChinese+"/"+resId+"/"+productCode);
			}
		if (str == null&&path != null){
			String[] searchPath = splitSearchPath(path);
			for (int i = 0; i < searchPath.length; i++){
				try{
					str = translator.getString(searchPath[i], simpleChinese, resId);
				}catch(java.util.MissingResourceException e){		
//					System.out.println("查找资源失败："+simpleChinese+"/"+resId+"/"+searchPath[i]);
				}
				if (str != null)
					break;
			}
		}
		if (str == null && ( productCode !=null  && !productCode.equals(IProductCode.PRODUCTCODE_COMMON))) {
			//productCode为COMMON时，就不用再去找了
			try{
				str = translator.getString(IProductCode.PRODUCTCODE_COMMON, simpleChinese, resId);
			}catch(java.util.MissingResourceException e){		
//				System.out.println("查找资源失败："+simpleChinese+"/"+resId+"/"+IProductCode.PRODUCTCODE_COMMON);
			}
		}
		if (str == null){
//			System.out.println("查找资源失败："+productCode+"/"+simpleChinese+"/"+resId+"/"+path);
			str = simpleChinese;
		}
		if (str == null && resId != null) {
			str = resId;
		}
	} catch (Exception e) {
		e.printStackTrace();
	}
	return str;
}

/** 根据传入的参数以-符号分隔拆分路径
 * @param path 路径,以-符号分隔
 * @return String[] 拆分后的字符串数组
 */
private String[] splitSearchPath(String path) {
	String []productCodes = null;
	if (path != null){
		path = path.trim();
		java.util.StringTokenizer st = new java.util.StringTokenizer(path, "-");
		int count = st.countTokens();
		productCodes = new String[count];
		for (int i = 0; i < count && st.hasMoreTokens(); i++){
			productCodes[i] = st.nextToken();
		}
	}else{
		productCodes = new String[0];
	}
	return productCodes;
}

/**
 * 获得当前使用的语言。
 * 创建日期：(2004-9-13 13:39:37)
 * @return nc.vo.ml.Language 当前使用的语言
 */
public abstract nc.vo.ml.Language getCurrLanguage() ;


public static  Language getDefaultLanguage() {
    Language lang = new Language();
    lang.setCode("simpchn");
    lang.setDisplayName("简体中文");
    lang.setTranslatorClassName("nc.vo.ml.translator.SimpleChineseTranslator");
    lang.setLocalLang("zh");
    lang.setLocalCountry("CN");
    return lang;
}

public void setWriteToFileTest(boolean bWrite){
	resWrite.setWriteToFileTest(bWrite);
}

}