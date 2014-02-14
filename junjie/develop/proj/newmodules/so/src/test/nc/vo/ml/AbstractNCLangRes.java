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
	 * �������� 
	 */
	protected Language[] allLanguages = null;

	private java.util.HashMap hm_AllLanguages = null;//����map,key������code��value������VO

	private ResWrite4Upload resWrite = new ResWrite4Upload();	
	
/**���󷽷�,����������Ҫʵ��,������������VO
 * @return ��������VO
 */
public abstract Language[] getAllLanguages();

/** ��֯����map�����ҷ�������map
 * @return ����map��key������code��value������VO
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
 * �������Ա��뷵������VO,ͨ������map��ȡ
 * �������ڣ�(2004-9-13 13:39:37)
 * @return nc.vo.ml.Language �������Ա��뷵������VO
 * @param langCode java.lang.String ���Ա���
 */
public nc.vo.ml.Language getLanguage(java.lang.String langCode) {
	return (Language)getAllLanguagesAsHM().get(langCode);
}


/**
���ݲ�Ʒ����,��������,��ԴID���ط�����ַ���
* @return String �������ַ���
* @param productCode java.lang.String ��Ʒ����
* @param simpleChinese java.lang.String ��Ҫ����ļ��������ַ���
* @param resId java.lang.String ��Ҫ����ļ��������ַ�������ԴID
*/
public String getString(String  productCode,  String  simpleChinese,  String  resId){
	String str = getStringByPath(productCode, simpleChinese,resId, null);
	return str;
}
/**
���ݲ�Ʒ����,��ԴID���ط�����ַ���
* @return String �������ַ���
* @param productCode java.lang.String ��Ʒ����
* @param resId java.lang.String ��Ҫ����ļ��������ַ�������ԴID
*/
public String getStrByID(String  productCode, String  resId){
	String str = getStringByPath(productCode, null,resId, null);
	return str;
}

/**
���ݲ�Ʒ����,��������,��ԴID,����·�����ط�����ַ���
* @return String �������ַ���
* @param productCode java.lang.String ��Ʒ����
* @param simpleChinese java.lang.String ��Ҫ����ļ��������ַ���
* @param resId java.lang.String ��Ҫ����ļ��������ַ�������ԴID
* @param path java.lang.String	�������Դ·��
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
	���ݲ�Ʒ����,�����������,�����ԴID
	��������󷵻�ƴ�Ӻõ��ַ���
 * @return String �����ƴ�Ӻõ��ַ���
 * @param productCode java.lang.String ��Ʒ����
 * @param simpleChineses java.lang.String ��Ҫ����Ķ�����������ַ���
 * @param resIDs java.lang.String ��Ҫ����Ķ�����������ַ�������ԴID	
*/
public String getString(String  productCode,  String[]  simpleChineses, String[]  resIDs){
	return getStringByPath(productCode, simpleChineses, resIDs, null);
}
/**
���ݲ�Ʒ����,�����ԴID
��������󷵻�ƴ�Ӻõ��ַ���
* @return String �����ƴ�Ӻõ��ַ���
* @param productCode java.lang.String ��Ʒ����
* @param resIDs java.lang.String ��Ҫ����Ķ�����������ַ�������ԴID	
*/
public String getStrByID(String  productCode, String[]  resIDs){
return getStringByPath(productCode, null, resIDs, null);
}

/**
���ݲ�Ʒ����,�����������,�����ԴID
��������󷵻��ַ�������
* @return String[] �������ַ�������
* @param productCode java.lang.String ��Ʒ����
* @param simpleChineses java.lang.String ��Ҫ����Ķ�����������ַ���
* @param resIDs java.lang.String ��Ҫ����Ķ�����������ַ�������ԴID
*/
public String[] getStrings(String  productCode,  String[]  simpleChineses, String[]  resIDs){
	return getStringsByPath(productCode, simpleChineses, resIDs, null);
}

/**
	���ݲ�Ʒ����,�����������,�����ԴID,����·��
	��������󷵻�ƴ�Ӻõ��ַ���
 * @return String �����ƴ�Ӻõ��ַ���
 * @param productCode java.lang.String ��Ʒ����
 * @param simpleChineses java.lang.String ��Ҫ����Ķ�����������ַ���
 * @param resIDs java.lang.String ��Ҫ����Ķ�����������ַ�������ԴID
 * @param path java.lang.String	�������Դ·��
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
���ݲ�Ʒ����,�����������,�����ԴID,����·��
��������󷵻��ַ�������
* @return String[] �������ַ�������
* @param productCode java.lang.String ��Ʒ����
* @param simpleChineses java.lang.String ��Ҫ����Ķ�����������ַ���
* @param resIDs java.lang.String ��Ҫ����Ķ�����������ַ�������ԴID
* @param path java.lang.String	�������Դ·��
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
	���ݼ�������,��ԴID,����·�����ط�����ַ���,
	���ò���ֵ���滻��ӡ����ַ����еĲ���
	�ַ����к��еĲ�����д��Ϊ��{index}��ʾ�ô�Ӧ���ò���ֵ����ĵ�index��Ԫ�����滻
	���� {0}��ʾӦ���ò���ֵ�����еĵ�һ��Ԫ�����滻
 * @return String �������ַ���
 * @param productCode java.lang.String ��Ʒ����
 * @param simpleChinese java.lang.String ��Ҫ����ļ��������ַ���
 * @param resId java.lang.String ��Ҫ����ļ��������ַ�������ԴID
 * @param path java.lang.String	�������Դ·��
 * @param argValues java.lang.String[]	�������ַ����в�����ֵ
*/
public String getString(String  productCode,  String  simpleChinese,  String  resId, String path, String[]argValues){
	String str = getStringByPath(productCode, simpleChinese, resId, path);
	int count = argValues == null ? 0 : argValues.length;
	for (int i = 0; i < count; i++){
		String arg = "{"+i+"}";
		str = StringUtil.replaceAllString(str, arg, argValues[i]==null? "" : argValues[i]);
//		str = str.replaceAll(arg, argValues[i]==null?"":argValues[i]);//nc.bs.mw.util.StringUtil.replaceAllString(str, arg, argValues[i]==null?"":argValues[i]);//argValues[i]��replaceAllString�����в�֧��null
	}
	return str;
}
/**
������ԴID,����·�����ط�����ַ���,
���ò���ֵ���滻��ӡ����ַ����еĲ���
�ַ����к��еĲ�����д��Ϊ��{index}��ʾ�ô�Ӧ���ò���ֵ����ĵ�index��Ԫ�����滻
���� {0}��ʾӦ���ò���ֵ�����еĵ�һ��Ԫ�����滻
* @return String �������ַ���
* @param productCode java.lang.String ��Ʒ����
* @param resId java.lang.String ��Ҫ����ļ��������ַ�������ԴID
* @param path java.lang.String	�������Դ·��
* @param argValues java.lang.String[]	�������ַ����в�����ֵ
*/
public String getStrByID(String  productCode,  String  resId, String path, String[]argValues){
String str = getStringByPath(productCode, null, resId, path);
int count = argValues == null ? 0 : argValues.length;
for (int i = 0; i < count; i++){
	String arg = "{"+i+"}";
	str = StringUtil.replaceAllString(str, arg, argValues[i]==null? "" : argValues[i]);
//	str = str.replaceAll(arg, argValues[i]==null?"":argValues[i]);//nc.bs.mw.util.StringUtil.replaceAllString(str, arg, argValues[i]==null?"":argValues[i]);//argValues[i]��replaceAllString�����в�֧��null
}
return str;
}



/**
���ݾ���ķ�����,��Ʒ����,��������,��ԴID,��Դ·�����ط�����ַ���
* @return String �������ַ���
* @param translator ITranslator ����ķ�����
* @param productCode java.lang.String ��Ʒ����
* @param simpleChinese java.lang.String ��Ҫ����ļ��������ַ���
* @param resId java.lang.String ��Ҫ����ļ��������ַ�������ԴID
* @param path java.lang.String	�������Դ·��
*/
protected String getString(ITranslator translator,String  productCode,  String  simpleChinese,  String  resId, String path){
	String str = null;
	try {
		if (resId == null){
			new Exception("resid can't be null").printStackTrace();
			return null;
//			productCode = IProductCode.PRODUCTCODE_COMMON;//����idΪnull��Ŀǰ�涨��Ҫ����Դ����COMMON��
//			//����resIDΪnull�ģ��Զ����뵽�����ļ���Ϊ����Դ�ϴ������ݿ��У����ֻ���ڲ��Ի����в����ã�
//			resWrite.writeToFile4Test(StringUtil.filterString(simpleChinese));
		}
//		//����һ�����������������û��Զ������Դ,��������Ϊusercustom
//		try{
//			str = translator.getString(IProductCode.PRODUCTCODE_USERCUSTOM, simpleChinese, resId);
//		}catch(java.util.MissingResourceException e){		
////			System.out.println("������Դʧ�ܣ�"+simpleChinese+"/"+resId+"/"+productCode);
//		}
		
		//
		if (productCode != null)
			try{
				str = translator.getString(productCode, simpleChinese, resId);
			}catch(java.util.MissingResourceException e){		
//				System.out.println("������Դʧ�ܣ�"+simpleChinese+"/"+resId+"/"+productCode);
			}
		if (str == null&&path != null){
			String[] searchPath = splitSearchPath(path);
			for (int i = 0; i < searchPath.length; i++){
				try{
					str = translator.getString(searchPath[i], simpleChinese, resId);
				}catch(java.util.MissingResourceException e){		
//					System.out.println("������Դʧ�ܣ�"+simpleChinese+"/"+resId+"/"+searchPath[i]);
				}
				if (str != null)
					break;
			}
		}
		if (str == null && ( productCode !=null  && !productCode.equals(IProductCode.PRODUCTCODE_COMMON))) {
			//productCodeΪCOMMONʱ���Ͳ�����ȥ����
			try{
				str = translator.getString(IProductCode.PRODUCTCODE_COMMON, simpleChinese, resId);
			}catch(java.util.MissingResourceException e){		
//				System.out.println("������Դʧ�ܣ�"+simpleChinese+"/"+resId+"/"+IProductCode.PRODUCTCODE_COMMON);
			}
		}
		if (str == null){
//			System.out.println("������Դʧ�ܣ�"+productCode+"/"+simpleChinese+"/"+resId+"/"+path);
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

/** ���ݴ���Ĳ�����-���ŷָ����·��
 * @param path ·��,��-���ŷָ�
 * @return String[] ��ֺ���ַ�������
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
 * ��õ�ǰʹ�õ����ԡ�
 * �������ڣ�(2004-9-13 13:39:37)
 * @return nc.vo.ml.Language ��ǰʹ�õ�����
 */
public abstract nc.vo.ml.Language getCurrLanguage() ;


public static  Language getDefaultLanguage() {
    Language lang = new Language();
    lang.setCode("simpchn");
    lang.setDisplayName("��������");
    lang.setTranslatorClassName("nc.vo.ml.translator.SimpleChineseTranslator");
    lang.setLocalLang("zh");
    lang.setLocalCountry("CN");
    return lang;
}

public void setWriteToFileTest(boolean bWrite){
	resWrite.setWriteToFileTest(bWrite);
}

}