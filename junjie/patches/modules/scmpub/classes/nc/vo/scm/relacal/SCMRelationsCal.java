package nc.vo.scm.relacal;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.StringTokenizer;

import nc.bs.bd.b21.BusinessCurrencyRateUtil;
import nc.bs.framework.common.NCLocator;
import nc.ui.bd.b21.CurrParamQuery;
import nc.ui.bd.b21.CurrtypeQuery;
import nc.ui.pub.formulaparse.FormulaParse;
import nc.ui.pub.para.SysInitBO_Client;
import nc.vo.bd.b20.CurrtypeVO;
import nc.vo.ml.NCLangRes4VoTransl;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.bd.SmartVODataUtils;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.scm.pu.RelationsCalVO;
import nc.vo.scm.pub.SCMEnv;

/**
 * 实现算法:统一的数量金额计算变化关系。
 * <p>
 * <strong>提供者：</strong>供应链-采购管理
 * <p>
 * <strong>使用者：</strong>NC供应链、NC财务
 * <p>
 * <strong>设计状态：</strong>编码
 * 
 * @version 1.0
 * @author <a href="mailto:czp@ufida.com.cn">czp</a>
 * @date 2006-05-16
 * 
 * @version 1.1 (v5.3)
 * @author <a href="mailto:zhangcheng@ufida.com.cn">zhangcheng</a>
 * @date 2008-03-07
 */
public class SCMRelationsCal {

  /*
   * 缓存参数精度 包括：[数量BD501]、[辅数量BD502]、[换算率BD503]、[单价BD505]; 存储结构:{pk_corp=int[]{}}
   */
  private  HashMap m_mapDigitsPara = new HashMap();

  // 缓存[金额]精度--业务精度,存储结构:{pk_currtype=Integer}
  private  HashMap m_mapDigitsMnyBusi = new HashMap();

  // 缓存[金额]精度--财务精度,存储结构:{pk_currtype=Integer}
  private  HashMap m_mapDigitsMnyFina = new HashMap();

  // 数据库默认精度
  final static int DEFAULT_POWER_DATABASE = 8;

  // 单据模板默认精度
  final static int DEFAULT_POWER_TEMPLET = 2;
  
  // 折扣默认精度
  final static int DEFAULT_POWER_DISCOUNTRATE = 6;

  // 零常量
  final static UFDouble ZERO = new UFDouble(0.0);

  //打印调试开关
  private boolean debug = false;
  
  public void setDebug(boolean debug) {
    this.debug = debug;
  }

  // 财务调用标志(默认为 false)
  // 财务调用标志(默认为 SCM)
  private final static int ForScm = 0;

  private final static int ForFinance = 1;

  private int m_iFinaFlag = ForScm;
  
  private UFDouble ratemny = null;
  
  private int[] m_forbidEditField = null;

  // private boolean m_bFinaFlag = false;

  private BusinessCurrencyRateUtil bcurr;
  
  // 当前改变的KEY
  private String m_strChangedKey;

  // 字段KEY索引
  public static final int DISCOUNT_TAX_TYPE_NAME = 0; // 扣税类别名(应税内含，应税外加，不计税)

  public static final int DISCOUNT_TAX_TYPE_KEY = 1; // 扣税类别

  public static final int NUM = 2; // 主数量

  public static final int NUM_QUALIFIED = 3; // 合格数量

  public static final int NUM_UNQUALIFIED = 4; // 不合格数量

  public static final int NUM_ASSIST = 5; // 辅数量

  public static final int NET_PRICE_ORIGINAL = 7; // 无税净价 --原币

  public static final int NET_TAXPRICE_ORIGINAL = 8; // 含税净价 --原币

  public static final int PRICE_ORIGINAL = 9; // 无税单价 --原币

  public static final int TAXRATE = 11; // 税率

  public static final int TAX_ORIGINAL = 12; // 税额 --原币

  public static final int MONEY_ORIGINAL = 13; // 金额 --原币

  public static final int SUMMNY_ORIGINAL = 14; // 价税合计 --原币

  public static final int DISCOUNT_RATE = 15; // 单品折扣(采购中的扣率)

  public static final int CONVERT_RATE = 16; // 换算率

  public static final int IS_FIXED_CONVERT = 17; // 是否固定换算率

  public static final int TAXPRICE_ORIGINAL = 18; //含税单价
  
  public static final int ALLDISCOUNT_RATE = 19; // 整单折扣(暂时只有销售有)
  
  public static final int HEAD_SUMMNY_ORIGINAL = 101; //整单价税合计
  
  public static final int DISCOUNTMNY_ORIGINA = 102;   //原币折扣额
  
  public static final int UNITID = 103;   //主计量单位ID
  
  public static final int QUOTEUNITID = 104;   //报价计量单位ID
  
  public static final int QT_CONVERT_RATE = 105; // 报价换算率

  public static final int QT_IS_FIXED_CONVERT = 106; // 报价是否固定换算率
  
  public static final int QT_NUM = 20;                    //原币报价单位数量                
  public static final int QT_TAXPRICE_ORIGINAL = 21;      //原币报价单位含税单价        
  public static final int QT_PRICE_ORIGINAL = 22;         //原币报价单位无税单价        
  public static final int QT_NET_TAXPRICE_ORIGINAL = 23;  //原币报价单位含税净价        
  public static final int QT_NET_PRICE_ORIGINAL = 24;     //原币报价单位无税净价     

  public static final int PRICE_LOCAL = 25;              //本币无税单价                                          
  public static final int TAXPRICE_LOCAL = 26;           //本币含税单价                                                   
  public static final int NET_PRICE_LOCAL = 27;          //本币无税净价                                                     
  public static final int NET_TAXPRICE_LOCAL = 28;       //本币含税净价                                                     
  public static final int TAX_LOCAL = 29;                //本币税额                                                        
  public static final int MONEY_LOCAL = 30;              //本币无税金额                                                
  public static final int SUMMNY_LOCAL = 31;             //本币价税合计                                                 
  public static final int DISCOUNTMNY_LOCAL = 32;        //本币折扣额

  public static final int QT_PRICE_LOCAL = 33;           //报价单位本币无税单价                                          
  public static final int QT_TAXPRICE_LOCAL = 34;        //报价单位本币含税单价                                                   
  public static final int QT_NET_PRICE_LOCAL = 35;       //报价单位本币无税净价                                                     
  public static final int QT_NET_TAXPRICE_LOCAL = 36;    //报价单位本币含税净价                                                     

  public static final int PRICE_FRACTIONAL = 37;               //辅币无税单价                                          
  public static final int TAXPRICE_FRACTIONAL = 38;            //辅币含税单价                                                   
  public static final int NET_PRICE_FRACTIONAL = 39;           //辅币无税净价                                                     
  public static final int NET_TAXPRICE_FRACTIONAL = 40;        //辅币含税净价                                                     
  public static final int TAX_FRACTIONAL = 41;                 //辅币税额                                                        
  public static final int MONEY_FRACTIONAL = 42;               //辅币无税金额                                                
  public static final int SUMMNY_FRACTIONAL = 43;              //辅币价税合计                                                 
  public static final int DISCOUNTMNY_FRACTIONAL = 44;         //辅币折扣额

  public static final int ASSIST_PRICE_ORIGINAL = 45;  		  //辅计量无税单价		--原币
  public static final int ASSIST_TAXPRICE_ORIGINAL = 46;      //辅计量含税单价		--原币

  public static final int ASK_TAXPRICE = 47;           //询价原币含税单价
  public static final int ASK_PRICE = 48;              //询价原币无税单价

  public static final int CURRTYPEPk = 49;              //原币PK
  public static final int PK_CORP = 50;                 //PK_CORP
  public static final int EXCHANGE_O_TO_BRATE = 51;     //折本汇率
  public static final int EXCHANGE_O_TO_ARATE = 52;     //折辅汇率
  public static final int BILLDATE = 53;                //单据日期

  // 字段KEY索引对应的字段KEY及界面显示名称(后台调用时传入字段名称，但注意多语翻译)
  private String m_strDiscountTaxTypeKey = null; // 扣税类别0

  private String m_strDiscountTaxTypeName = null; // 扣税类别的名称(应税内含，应税外加，不计税)1

  private String m_strNumKey = ""; // 主数量2

  private String m_strQualifiedNumKey = ""; // 合格数量3

  private String m_strUnQualifiedNumKey = ""; // 不合格数量4

  private String m_strAssistNumKey = ""; // 辅数量5

  private String m_strNetPriceKey = ""; // 无税净价 --原币6

  private String m_strNetTaxPriceKey = ""; // 含税净价 (净含税单价) --原币7

  private String m_strPriceKey = ""; // 无税单价 --原币8

  private String m_strTaxRateKey = ""; // 税率9

  private String m_strTaxKey = ""; // 税额 --原币10

  private String m_strMoneyKey = ""; // 金额 --原币113

  private String m_strSummnyKey = ""; // 价税合计 --原币12
  
  private String m_strDiscountRateKey = ""; // 单品折扣(即采购中的扣率)13

  private String m_strConvertRateKey = ""; // 换算率14

  private String m_strTaxPriceKey = ""; // 含税单价 --原币15

  private boolean m_bFixedConvertRateKey = false; // 是否固定换算率16
  
  private String m_strAllDiscountRateKey = ""; // 整单折扣(暂时只有销售有)17
  
  private String m_strheadstrSummnyKey = ""; // 整单价税合计 --原币18
  
  private String m_strDiscountMnyKey = ""; // 原币折扣额 --原币19
  
  private String m_strQt_ConvertRateKey = ""; // 报价换算率14

  private boolean m_bQt_FixedConvertRateKey = false; // 报价是否固定换算率16

  private String m_strQt_NumKey = "";           //原币报价单位数量                
  private String m_strQt_TaxPriceKey = "";      //原币报价单位含税单价        
  private String m_strQt_PriceKey = "";         //原币报价单位无税单价        
  private String m_strQt_NetTaxPriceKey = "";  //原币报价单位含税净价        
  private String m_strQt_NetPriceKey = "";     //原币报价单位无税净价     

  private String m_strPrice_LocalKey = "";              //本币无税单价                                          
  private String m_strTaxPrice_LocalKey = "";           //本币含税单价                                                   
  private String m_strNet_Price_LocalKey = "";          //本币无税净价                                                     
  private String m_strNet_TaxPrice_LocalKey = "";       //本币含税净价                                                     
  private String m_strTax_LocalKey = "";                //本币税额                                                        
  private String m_strMoney_LocalKey = "";              //本币无税金额                                                
  private String m_strSummny_LocalKey = "";             //本币价税合计                                                 
  private String m_strDiscountMny_LocalKey = "";        //本币折扣额

  private String m_strQt_Price_LocalKey = "";           //报价单位本币无税单价                                          
  private String m_strQt_TaxPrice_LocalKey = "";        //报价单位本币含税单价                                                   
  private String m_strQt_Net_Price_LocalKey = "";       //报价单位本币无税净价                                                     
  private String m_strQt_Net_TaxPrice_LocalKey = "";    //报价单位本币含税净价                                                     

  private String m_strPrice_FractionalKey = "";               //辅币无税单价                                          
  private String m_strTaxPrice_FractionalKey = "";            //辅币含税单价                                                   
  private String m_strNet_Price_FractionalKey = "";           //辅币无税净价                                                     
  private String m_strNet_TaxPrice_FractionalKey = "";        //辅币含税净价                                                     
  private String m_strTax_FractionalKey = "";                 //辅币税额                                                        
  private String m_strMoney_FractionalKey = "";               //辅币无税金额                                                
  private String m_strSummny_FractionalKey = "";              //辅币价税合计                                                 
  private String m_strDiscountMny_FractionalKey = "";         //辅币折扣额

  private String m_strAssist_Price = "";  		  //辅计量无税单价		--原币
  private String m_strAssist_TaxPrice = "";      //辅计量含税单价		--原币

  private String m_strAsk_TaxPriceKey = "";           //询价原币含税单价
  private String m_strAsk_PriceKey = "";              //询价原币无税单价

  private String m_strCurrTypePkKey = "";              //原币PK
  private String m_strPk_CorpKey = "";                 //PK_CORP
  private String m_strExchange_O_TO_BrateKey = "";     //折本汇率
  private String m_strExchange_O_TO_ArateKey = "";     //折辅汇率
  private String m_strBillDateKey = "";                //单据日期
  private String m_strUnitID = "";                     //主计量单位
  private String m_strQuoteUnitID = "";                //报价计量单位
  
  
  // 以下字段用于提示信息
  // private static String m_strNetPriceName = "净单价";
  // private static String m_strNetTaxPriceName = "净含税单价";
  // private static String m_strPriceName = "单价";
  // private static String m_strTaxRateName = "税率";
  // private static String m_strDiscountRateName = "扣率";
  // private static String m_strConvertRateName = "换算率";
  // private static String m_strTaxPriceName = "含税单价";
  //	
  // static{
  private final static String m_strNetPriceName = NCLangRes4VoTransl
      .getNCLangRes().getStrByID("common", "UC000-0000450")/* @res:净单价 */;

  private final static String m_strNetTaxPriceName = NCLangRes4VoTransl
      .getNCLangRes().getStrByID("common", "UC000-0000452")/* @res:净含税单价 */;

  private final static String m_strPriceName = NCLangRes4VoTransl
      .getNCLangRes().getStrByID("common", "UC000-0000741")/* @res:单价 */;

  private final static String m_strTaxRateName = NCLangRes4VoTransl
      .getNCLangRes().getStrByID("common", "UC000-0003078")/* @res:税率 */;

  private final static String m_strDiscountRateName = NCLangRes4VoTransl
      .getNCLangRes().getStrByID("common", "UC000-0001998")/* @res:扣率 */;

  private final static String m_strConvertRateName = NCLangRes4VoTransl
      .getNCLangRes().getStrByID("common", "UC000-0002161")/* @res:换算率 */;

  private final static String m_strTaxPriceName = NCLangRes4VoTransl
      .getNCLangRes().getStrByID("common", "UC000-0001160")/* @res:含税单价 */;

  // }

  // 所有CIRCLE
  private static final int CIRCLE_CONVERT_RATE = 2; // 数量、辅数量、换算率

  private static final int CIRCLE_IMPOSSIBLE = 0; // 没有可执行的CIRCLE

  private static final int CIRCLE_NETTAXPRICE_NETPRICE = 9; // 税率、含税净价、无税净价

  private static final int CIRCLE_NUM_NETPRICE_MONEY = 3; // 数量、净单价、金额

  private static final int CIRCLE_NUM_NETTAXPRICE_SUMMNY = 4; // 数量、含税净价、价税合计

  private static final int CIRCLE_PRICE_NETPRICE = 6; // 扣率、无税单价、无税净价

  private static final int CIRCLE_QUALIFIED_NUM = 1; // 数量、合格数量、不合格数量

  private static final int CIRCLE_TAXPRICE_NETTAXPRICE = 7; // 扣率、含税单价、含税净价

  private static final int CIRCLE_TAXPRICE_PRICE = 8; // 税率、含税单价、无税单价

  private static final int CIRCLE_TAXRATE = 5; // 税率、金额、税额、价税合计
  
  private static final int CIRCLE_DISCOUNTMNY = 10; // 含税单价、数量、价税合计、折扣额
  
  private static final int CIRCLE_VIAPRICE= 11; // 辅计量含税单价、辅计量无税单价、辅数量、价税合计、无税金额、税率、折扣额

  private static final int CIRCLE_QT_TAXPRICE_PRICE = 12;//税率、报价原币含税单价、报价原币无税单价
  
  private static final int CIRCLE_QT_NETTAXPRICE_NETPRICE = 13;//税率、报价原币含税净价、报价原币无税净价
  
  private static final int CIRCLE_QT_TAXPRICE_NETTAXPRICE = 14; // 扣率、报价原币含税单价、报价原币含税净价
  
  private static final int CIRCLE_QT_PRICE_NETPRICE = 15; // 扣率、报价原币无税单价、报价原币无税净价
  
  private static final int CIRCLE_QT_NUM_NETTAXPRICE_SUMMNY = 16;// 报价数量、报价原币含税净价、价税合计
  
  private static final int CIRCLE_QT_NUM_NETPRICE_MONEY = 17;// 报价数量、报价原币无税净价、无税金额
  
  private static final int CIRCLE_QT_CONVERT_RATE = 18; // 主数量、报价数量、报价换算率
  
  private static final int CIRCLE_LOCAL_SUMMNY_MNY_TAX = 19; //本币价税合计、本币无税金额、本币税额
  
  // ===============初始变化ITEM确定后，相应的哪些项不应修改，以避免从不同点进入时的覆盖修改
  private boolean m_baEverChangedAssistNum = false;

  private boolean m_baEverChangedConvertRate = false;

  private boolean m_baEverChangedDiscountRate = false;

  private boolean m_baEverChangedMoney = false;

  private boolean m_baEverChangedNetPrice = false;

  private boolean m_baEverChangedNetTaxPrice = false;

  private boolean m_baEverChangedNum = false;

  private boolean m_baEverChangedPrice = false;

  private boolean m_baEverChangedQualifiedNum = false;

  private boolean m_baEverChangedSummny = false;

  private boolean m_baEverChangedTax = false;

  private boolean m_baEverChangedTaxPrice = false;

  private boolean m_baEverChangedTaxRate = false;

  private boolean m_baEverChangedUnQualifiedNum = false;
  
  private boolean m_baEverChangedQt_Num = false;
  
  private boolean m_baEverChangedQt_ConvertRate = false;
  
  private boolean m_baEverChangedQt_TaxPrice = false;
  
  private boolean m_baEverChangedQt_Price = false;
  
  private boolean m_baEverChangedQt_Net_TaxPrice = false;
  
  private boolean m_baEverChangedQt_Net_Price = false;
  
  private boolean m_baEverChangedLocal_Summny = false;
  
  private boolean m_baEverChangedLocal_Mny = false;
  
  private boolean m_baEverChangedLocal_Tax = false;
  
  private boolean m_baEverChangedLocal_ExchangeBrate = false;

  // ===============标志某个CIRCLE是否已被执行
  private boolean m_bCircleExecuted[] = null;

  // 单据模板公式的zero
  private boolean m_bNullAsZero = false;

  // /==============事件及模板对象
  // private BillEditEvent m_evtEdit = null;
  // 当前要计算的 CircularlyAccessibleValueObject[] 的位置
  // private BillCardPanel m_pnlBill = null;
  // 当前要计算的单个表体VO
  private CircularlyAccessibleValueObject m_voCurr = null;
  
  //当前要计算的表头VO
  private CircularlyAccessibleValueObject m_hvoCurr = null;
  //当前要计算的表体VO数组
  private CircularlyAccessibleValueObject[] m_bvoCurr = null;
  
  
  //================源币，本币，辅币
  private String pk_CurrType;
  private String locaCurrType;
  private String fracCurrType;

  // ===============存放优先机制的变量
  // 默认：无税优先(单价优于含税单价)
  private int m_iPrior_Price_TaxPrice = RelationsCalVO.PRICE_PRIOR_TO_TAXPRICE;
  
  // 默认：调折扣。编辑含税,无税净价,价税合计,无税金额时,调整单价或者单品折扣
  private int m_iPrior_ItemDiscountRate_Price = RelationsCalVO.ITEMDISCOUNTRATE_PRIOR_TO_PRICE;
  
  // 默认：本算法不进行本币、辅币的计算
  private int m_iPrior_LOCAL_FRAC = RelationsCalVO.NO_LOCAL_FRAC;
  
  // ===============旧值
  private Object m_objOldAssistNum = null;

  private Object m_objOldConvertRate = null;

  private Object m_objOldDiscountRate = null;

  private Object m_objOldMoney = null;

  private Object m_objOldNetPrice = null;

  private Object m_objOldNetTaxPrice = null;

  private Object m_objOldNum = null;

  private Object m_objOldPrice = null;

  private Object m_objOldQualifiedNum = null;

  private Object m_objOldSummny = null;

  private Object m_objOldTax = null;

  private Object m_objOldTaxPrice = null;

  private Object m_objOldTaxrate = null;

  private Object m_objOldUnQualifiedNum = null;

  // V5:计算出现错误时的提示信息
  private String m_strShowHintMsg = null;

  // 一次IE会话一个实例
  private FormulaParse m_fp = null;

  /**
   * 后台调用的统一入口，根据变化的KEY运算一组VO的被影响的值
   * 
   * @param voaCurr
   *          当前操作的VO数组
   * @param iaPrior
   *          目前暂时传递一个长的int[],int[0]=含税优先还是不含税优先{含税单价优于单价:RelationsCalVO.TAXPRICE_PRIOR_TO_PRICE;单价优于含税单价:RelationsCalVO.PRICE_PRIOR_TO_TAXPRICE}
   * @param strChangedKey
   *          当前触发变化的字段(调用VO相关,比如订单数量为“nordernum”)
   * @param nDescriptions
   *          当前要运算的的字段索引(算法规定的,参见SCMRelationsCal的"字段KEY索引"相关定义)
   * @param strKeys
   *          当前要运算的字段(调用VO相关,比如订单数量为“nordernum”)
   * @return
   * @exception BusinessException
   * @author <a href="mailto:czp@ufida.com.cn">czp</a>
   * @darte 2006-05-16
   */
  public static void calculate(CircularlyAccessibleValueObject[] voaCurr,
      int[] iaPrior, String strChangedKey, int[] nDescriptions, String[] strKeys, int[] forbidEditField)
      throws BusinessException {

    if (voaCurr == null || voaCurr.length == 0) {
      return;
    }
    int iLen = voaCurr.length;
    Object  oRet = null ;
    for (int i = 0; i < iLen; i++) {
      oRet = calculate(voaCurr[i], iaPrior, strChangedKey, nDescriptions, strKeys, forbidEditField);
      if (oRet != null && oRet instanceof String) {
        throw new BusinessException((String)oRet);
      }
    }
  }
  
  public static void calculate(CircularlyAccessibleValueObject[] voaCurr,
	      int[] iaPrior, String strChangedKey, int[] nDescriptions, String[] strKeys)
	      throws BusinessException {
	  calculate(voaCurr, iaPrior, strChangedKey, nDescriptions, strKeys, null);
  }

  /**
   * 财务专用：后台调用的统一入口，根据变化的KEY运算一组VO的被影响的值
   * 
   * @param voaCurr
   *          当前操作的VO数组
   * @param iaPrior
   *          目前暂时传递一个长的int[],int[0]=含税优先还是不含税优先{含税单价优于单价:RelationsCalVO.TAXPRICE_PRIOR_TO_PRICE;单价优于含税单价:RelationsCalVO.PRICE_PRIOR_TO_TAXPRICE}
   * @param strChangedKey
   *          当前触发变化的字段(调用VO相关,比如订单数量为“nordernum”)
   * @param nDescriptions
   *          当前要运算的的字段索引(算法规定的,参见SCMRelationsCal的"字段KEY索引"相关定义)
   * @param strKeys
   *          当前要运算的字段(调用VO相关,比如订单数量为“nordernum”)
   * @return
   * @exception BusinessException
   * @author <a href="mailto:czp@ufida.com.cn">czp</a>
   * @darte 2006-05-16
   */
  public static void calculateFina(CircularlyAccessibleValueObject[] voaCurr,
      int[] iaPrior, String strChangedKey, int[] nDescriptions, String[] strKeys)
      throws BusinessException {

    if (voaCurr == null || voaCurr.length == 0) {
      return;
    }
    int iLen = voaCurr.length;
    Object  oRet = null ;
    for (int i = 0; i < iLen; i++) {
      oRet = calculateFina(voaCurr[i], iaPrior, strChangedKey, nDescriptions, strKeys);
      if (oRet != null && oRet instanceof String) {
        throw new BusinessException((String)oRet);
      }
    }
  }

  /**
   * 单个表体VO计算，不进行主辅币的计算
   * 
   * 前、后台调用的统一入口，根据变化的KEY运算一个VO的被影响的值
   * 
   * @param voCurr
   *          当前操作的VO
   * @param iaPrior
   *          目前暂时传递一个长的int[],int[0]=含税优先还是不含税优先{含税单价优于单价:RelationsCalVO.TAXPRICE_PRIOR_TO_PRICE;单价优于含税单价:RelationsCalVO.PRICE_PRIOR_TO_TAXPRICE}
   * @param strChangedKey
   *          当前触发变化的字段(调用VO相关,比如订单数量为“nordernum”)
   * @param nDescriptions
   *          当前要运算的的字段索引(算法规定的,参见SCMRelationsCal的"字段KEY索引"相关定义)
   * @param strKeys
   *          当前要运算的字段(调用VO相关,比如订单数量为“nordernum”)
   * @return
   * @exception BusinessException
   * @author <a href="mailto:czp@ufida.com.cn">czp</a>
   * @darte 2006-05-16
   */
  public static Object calculate(CircularlyAccessibleValueObject voCurr,
      int[] iaPrior, String strChangedKey, int[] nDescriptions, String[] strKeys, int[] forbidEditField) {

    // 初始化
    SCMRelationsCal cal = new SCMRelationsCal(ForScm, voCurr, iaPrior,
        strChangedKey, nDescriptions, strKeys,forbidEditField);

    // 计算
    cal.calculate();
    if (cal.m_strShowHintMsg != null) {
      return cal.m_strShowHintMsg;
    }
	else if (cal.ratemny!=null){
	  return new Object[]{"本币变动不能超过最大折算误差：" + cal.ratemny+",是否继续？",voCurr};
	}
    else {
      return voCurr;
    }
  }
  
  public static Object calculate(CircularlyAccessibleValueObject voCurr,
	      int[] iaPrior, String strChangedKey, int[] nDescriptions, String[] strKeys) {
	    return calculate(voCurr, iaPrior, strChangedKey, nDescriptions, strKeys, null);
	  }
  
  /**
   * 单个表头VO,单个表体VO计算，进行主辅币的计算
   * calculate方法重载:为销售产品创建的接口,增加单据日期参数以便进行主辅币计算
   * 2008-03-20  zhangcheng
   */
  public static Object calculate(CircularlyAccessibleValueObject voCurrB,
			CircularlyAccessibleValueObject voCurrH, int[] iaPrior,
			String strChangedKey, int[] nDescriptions, String[] strKeys, int[] forbidEditField ) {
	  
	  // 初始化
	  SCMRelationsCal cal = new SCMRelationsCal(ForScm, voCurrB,voCurrH,iaPrior,
	      strChangedKey, nDescriptions, strKeys,forbidEditField);

	  // 计算
	  cal.calculate();
	  if (cal.m_strShowHintMsg != null) {
	    return cal.m_strShowHintMsg;
	  }
	  else if (cal.ratemny!=null){
		  return new Object[]{"本币变动不能超过最大折算误差：" + cal.ratemny+",是否继续？",voCurrB};
	  }
	  else {
	    return voCurrB;
	  }
  }
  
  public static Object calculate(CircularlyAccessibleValueObject voCurrB,
			CircularlyAccessibleValueObject voCurrH, int[] iaPrior,
			String strChangedKey, int[] nDescriptions, String[] strKeys) {
	  return calculate(voCurrB, voCurrH, iaPrior, strChangedKey, nDescriptions, strKeys, null);
}
  
  /**
   * 单个表头VO、多个表体VO计算，进行主辅币的计算
   * calculate方法重载:为销售产品创建的接口,增加单据日期参数以便进行主辅币计算
   * 2008-03-20  zhangcheng
   */
  public static void calculate(CircularlyAccessibleValueObject[] voaCurr,
		  CircularlyAccessibleValueObject voCurrH,
	      int[] iaPrior, String strChangedKey, int[] nDescriptions, String[] strKeys, int[] forbidEditField)
	      throws BusinessException {
	  
	    if (voaCurr == null || voaCurr.length == 0) {
	      return;
	    }
	    int iLen = voaCurr.length;
	    Object  oRet = null ;
	    for (int i = 0; i < iLen; i++) {
	      oRet = calculate(voaCurr[i],voCurrH,iaPrior, strChangedKey, nDescriptions, strKeys, forbidEditField);
	      if (oRet != null && oRet instanceof String) {
	        throw new BusinessException((String)oRet);
	      }
	    }
  }
  
  /**
   * 单个表头VO、多个表体VO计算，进行主辅币的计算
   * calculate方法重载:为销售产品创建的接口,增加单据日期参数以便进行主辅币计算
   * 2008-03-20  zhangcheng
   */
  public static void calculate(CircularlyAccessibleValueObject[] voaCurr,
		  CircularlyAccessibleValueObject voCurrH,
	      int[] iaPrior, String strChangedKey, int[] nDescriptions, String[] strKeys)
	      throws BusinessException {
	  calculate(voaCurr, voCurrH, iaPrior, strChangedKey, nDescriptions, strKeys, null);
  }
  
  /**
   * 前、后台调用的统一入口，根据表头变化的KEY运算一个VO的被影响的值
   * 
   * @param voCurr
   *          当前操作的VO
   * @param oldValue
   *          当前操作字段的旧值         
   * @param iaPrior
   *          目前暂时传递一个长的int[],int[0]=含税优先还是不含税优先{含税单价优于单价:RelationsCalVO.TAXPRICE_PRIOR_TO_PRICE;单价优于含税单价:RelationsCalVO.PRICE_PRIOR_TO_TAXPRICE}
   * @param strChangedKey
   *          当前触发变化的字段(调用VO相关,比如订单数量为“nordernum”)
   * @param nDescriptions
   *          当前要运算的的字段索引(算法规定的,参见SCMRelationsCal的"字段KEY索引"相关定义)
   * @param strKeys
   *          当前要运算的字段(调用VO相关,比如订单数量为“nordernum”)
   * @return
   * @exception BusinessException
   * @darte 2008-03-13
   */
  /*public static Object calculate(CircularlyAccessibleValueObject H_voCurr,
			CircularlyAccessibleValueObject[] B_voCurr, Object HeadoldValue,
			int[] iaPrior, String strChangedKey, int[] nDescriptions,
			String[] strKeys) throws BusinessException {

    // 初始化
    SCMRelationsCal cal = new SCMRelationsCal(ForScm, H_voCurr, B_voCurr,HeadoldValue, iaPrior,
        strChangedKey, nDescriptions, strKeys);

    // 根据表头变化计算表体变化
    cal.calculate(HeadoldValue);
    
    if (cal.m_strShowHintMsg != null) {
      return cal.m_strShowHintMsg;
    }
    else {
      //表体变化后联动计算其他值
      calculate(B_voCurr, iaPrior, cal.m_strSummnyKey, nDescriptions, strKeys);   	
      return B_voCurr;
    }
  }*/

  /**
   * 财务专用：前、后台调用的统一入口，根据变化的KEY运算一个VO的被影响的值
   * 
   * @param voCurr
   *          当前操作的VO
   * @param iaPrior
   *          目前暂时传递一个长的int[],int[0]=含税优先还是不含税优先{含税单价优于单价:RelationsCalVO.TAXPRICE_PRIOR_TO_PRICE;单价优于含税单价:RelationsCalVO.PRICE_PRIOR_TO_TAXPRICE}
   * @param strChangedKey
   *          当前触发变化的字段(调用VO相关,比如订单数量为“nordernum”)
   * @param nDescriptions
   *          当前要运算的的字段索引(算法规定的,参见SCMRelationsCal的"字段KEY索引"相关定义)
   * @param strKeys
   *          当前要运算的字段(调用VO相关,比如订单数量为“nordernum”)
   * @return
   * @exception BusinessException
   * @author <a href="mailto:czp@ufida.com.cn">czp</a>
   * @darte 2006-05-16
   */
  public static Object calculateFina(CircularlyAccessibleValueObject voCurr,
      int[] iaPrior, String strChangedKey, int[] nDescriptions, String[] strKeys) {

    // 初始化
    SCMRelationsCal cal = new SCMRelationsCal(ForFinance, voCurr, iaPrior,
        strChangedKey, nDescriptions, strKeys,null);

    // 计算
    cal.calculate();
    if (cal.m_strShowHintMsg != null) {
      return cal.m_strShowHintMsg;
    }
	else if (cal.ratemny!=null){
	   return new Object[]{"本币变动不能超过最大折算误差：" + cal.ratemny+",是否继续？",voCurr};
	}
    else {
      return voCurr;
    }
  }

  /**
   * 初始化公式解析器
   * 
   * @author czp
   * @date 2006-05-16
   */
  private void initFormulaParse() {
    if (m_fp == null) {
      m_fp = new FormulaParse();
    }
  }

  /**
   * 按VO执行公式
   * 
   * @author czp
   * @param formulas
   * @date 2006-05-16
   */
  private void execFormulas(String[] formulas) {
    int iCnt = formulas.length;
    for (int k = 0; k < iCnt; k++) {
      // 设置公式
      m_fp.setExpress(formulas[k]);
      // 设置变量
      int iLen = m_fp.getVarry().getVarry().length;
      Object oVal = null;
      String strKey = null;
      for (int i = 0; i < iLen; i++) {
        strKey = m_fp.getVarry().getVarry()[i];
        oVal = m_voCurr.getAttributeValue(m_fp.getVarry().getVarry()[i]);
        // 按精度取VO值,与UI保持一致
        oVal = getResultPower(strKey, oVal);
        m_fp.addVariable(m_fp.getVarry().getVarry()[i], m_voCurr
            .getAttributeValue(m_fp.getVarry().getVarry()[i]));
      }
      // 按精度处理公式执行结果
      strKey = m_fp.getVarry().getFormulaName();

      oVal = m_fp.getValueAsObject();
    
      oVal = getResultPower(strKey, oVal);
      // 设置结果到VO
      m_voCurr.setAttributeValue(m_fp.getVarry().getFormulaName(), oVal);

    }
    
    //调试打印
    if (debug)
    	debugPrint(formulas);
  }

  /**
   * 按精度处理
   * 
   * @param strKey
   * @param oResult
   * @author czp
   * @date 2006-09-15
   */
  private Object getResultPower(String strKey, Object oResult) {
    if (strKey == null || oResult == null || !(oResult instanceof UFDouble)) {
      return oResult;
    }
    UFDouble ufdRet = (UFDouble) oResult;

    int iPower = DEFAULT_POWER_DATABASE;
    // 主数量、报价数量精度
    if (strKey.equalsIgnoreCase(m_strNumKey)
        || strKey.equalsIgnoreCase(m_strQualifiedNumKey)
        || strKey.equalsIgnoreCase(m_strUnQualifiedNumKey)
        || strKey.equalsIgnoreCase(m_strQt_NumKey)) {
      iPower = getPowerNum();
    }
    // 辅数量、报价数量精度
    else if (strKey.equalsIgnoreCase(m_strAssistNumKey)) {
      iPower = getPowerAssNum();
    }
    // 换算率、报价换算率精度
    else if (strKey.equalsIgnoreCase(m_strConvertRateKey) 
    		|| strKey.equalsIgnoreCase(m_strQt_ConvertRateKey)) {
      iPower = getPowerRate();
    }
    // 单价精度
    else if (strKey.equalsIgnoreCase(m_strNetPriceKey)
        || strKey.equalsIgnoreCase(m_strNetTaxPriceKey)
        || strKey.equalsIgnoreCase(m_strPriceKey)
        || strKey.equalsIgnoreCase(m_strTaxPriceKey)
        //本币
        || strKey.equalsIgnoreCase(m_strPrice_LocalKey)
        || strKey.equalsIgnoreCase(m_strTaxPrice_LocalKey)
        || strKey.equalsIgnoreCase(m_strNet_Price_LocalKey)
        || strKey.equalsIgnoreCase(m_strNet_TaxPrice_LocalKey)
        //辅币
        || strKey.equalsIgnoreCase(m_strNet_Price_FractionalKey)
        || strKey.equalsIgnoreCase(m_strNet_TaxPrice_FractionalKey)
        || strKey.equalsIgnoreCase(m_strPrice_FractionalKey)
        || strKey.equalsIgnoreCase(m_strTaxPrice_FractionalKey)
        //报价原币
        || strKey.equalsIgnoreCase(m_strQt_NetPriceKey)
        || strKey.equalsIgnoreCase(m_strQt_NetTaxPriceKey)
        || strKey.equalsIgnoreCase(m_strQt_PriceKey)
        || strKey.equalsIgnoreCase(m_strQt_TaxPriceKey)
        //报价本币 
        || strKey.equalsIgnoreCase(m_strQt_Net_Price_LocalKey)
        || strKey.equalsIgnoreCase(m_strQt_Net_TaxPrice_LocalKey)
        || strKey.equalsIgnoreCase(m_strQt_Price_LocalKey)
        || strKey.equalsIgnoreCase(m_strQt_TaxPrice_LocalKey)
        //辅计量原币
        || strKey.equalsIgnoreCase(m_strAssist_Price)
        || strKey.equalsIgnoreCase(m_strAssist_TaxPrice)) {
      iPower = getPowerPrice();
    }
    // 金额精度（原币）
    else if (strKey.equalsIgnoreCase(m_strTaxKey)
        || strKey.equalsIgnoreCase(m_strMoneyKey)
        || strKey.equalsIgnoreCase(m_strSummnyKey)) {
      if (m_iFinaFlag == ForFinance) {//财务
        iPower = getPowerMnyFina();
      }
      else {//供应链
    	 String strCurrTypePk = getPkcorpOrCurrType("currtype");
    	 if (strCurrTypePk==null)
    		 iPower = DEFAULT_POWER_DATABASE;
    	 else
    		 iPower = getPowerMnyBusi(strCurrTypePk); 
      }
    }
    // 金额精度(本币)
    else if (strKey.equalsIgnoreCase(m_strTax_LocalKey)
    	     || strKey.equalsIgnoreCase(m_strMoney_LocalKey)
    	     || strKey.equalsIgnoreCase(m_strSummny_LocalKey)){
    	 if (m_iFinaFlag == ForFinance) {//财务
    	        iPower = getPowerMnyFina(locaCurrType);
    	      }
    	      else {//供应链
    	    	  iPower = getPowerMnyBusi(locaCurrType);
    	      }
    }
    // 金额精度(辅币)
    else if (strKey.equalsIgnoreCase(m_strTax_FractionalKey)
    	    || strKey.equalsIgnoreCase(m_strMoney_FractionalKey)
    	    || strKey.equalsIgnoreCase(m_strSummny_FractionalKey)){
    	 if (m_iFinaFlag == ForFinance) {//财务
    	        iPower = getPowerMnyFina(fracCurrType);
    	      }
    	      else {//供应链
    	          iPower = getPowerMnyBusi(fracCurrType);
    	      }
    }                    
    // 税率及扣率取单据模板精度
    else if (strKey.equalsIgnoreCase(m_strTaxRateKey)) {
    	iPower = DEFAULT_POWER_TEMPLET;
    }
    //扣率取6位：v5.5以后统一要求
    else if (strKey.equalsIgnoreCase(m_strDiscountRateKey)
    		||strKey.equalsIgnoreCase(m_strAllDiscountRateKey)){
    	iPower = DEFAULT_POWER_DISCOUNTRATE;
    }
    
    // 按精度处理并返回
    return ufdRet.add(ZERO, iPower, UFDouble.ROUND_HALF_UP);
  }

  /**
   * 单个表体VO计算
   * NCRelationsCal 构造子注解。
   */
  private SCMRelationsCal(
      int forWhichSystem, CircularlyAccessibleValueObject voCurr,
      int[] iaPrior, String strChangedKey, int[] nDescriptions, String[] strKeys, int[] forbidEditField) {

    super();

    initFormulaParse();

    m_iFinaFlag = forWhichSystem;//财务 or 供应链调用标志

    m_voCurr = voCurr;//待计算vo

    initKeys(nDescriptions, strKeys);//初始化字段对应关系
    
    setFirstChangedKey(strChangedKey,iaPrior);//设置变化KEY
    
    initCircle();//初始化 KEY CIRCLE OBJ 旧值（全部为false）
    
    initObjects();//暂存旧值，以利回滚
    
    initFormulaParseModel();//初始化公式计算空与零的关系
    
    setForbidEditField(forbidEditField);//设置不可编辑字段映射
    
    setPrior(iaPrior);//设置优先级
  }
  
  /**
   * 单个表体VO计算
   * NCRelationsCal 构造子注解。
   */
  private SCMRelationsCal(int forWhichSystem,
			CircularlyAccessibleValueObject voCurrB,
			CircularlyAccessibleValueObject voCurrH, int[] iaPrior,
			String strChangedKey, int[] nDescriptions, String[] strKeys, int[] forbidEditField) {

    super();

    initFormulaParse();

    m_iFinaFlag = forWhichSystem;//财务 or 供应链调用标志

    m_voCurr = voCurrB;//待计算单个表体vo
    
    m_hvoCurr = voCurrH;//待计算表头vo

    initKeys(nDescriptions, strKeys);//初始化字段对应关系
    
    setFirstChangedKey(strChangedKey,iaPrior);//设置变化KEY
    
    initCircle();//初始化 KEY CIRCLE OBJ 旧值（全部为false）
    
    initObjects();//暂存旧值，以利回滚
    
    initFormulaParseModel();//初始化公式计算空与零的关系
    
    setForbidEditField(forbidEditField);//设置不可编辑字段映射
    
    setPrior(iaPrior);//设置优先级
  }
  
  /**
   * 用于编辑表头字段时，初始化算法（目前应用为：编辑表头整单折扣）
   * 并且进行主辅币的计算
   */
  /*private SCMRelationsCal(int forWhichSystem,
			CircularlyAccessibleValueObject H_voCurr,
			CircularlyAccessibleValueObject[] B_voCurr, Object oldValue,
			int[] iaPrior, String strChangedKey, int[] nDescriptions,
			String[] strKeys, int[] forbidEditField) {

	    super();

	    initFormulaParse();

	    m_iFinaFlag = forWhichSystem;//财务 or 供应链调用标志

	    m_bvoCurr = B_voCurr;//待计算表体vo数组
	    
	    m_hvoCurr = H_voCurr;//待计算表头vo

	    initKeys(nDescriptions, strKeys);//初始化字段对应关系
	    
	    setFirstChangedKey(strChangedKey);//设置变化KEY
	    
	    initCircle();//初始化 KEY CIRCLE OBJ 旧值（全部为false）
	    
	    initObjects();//暂存旧值，以利回滚
	    
	    initFormulaParseModel();//初始化公式计算空与零的关系
	    
	    setForbidEditField(forbidEditField);//设置不可编辑字段映射
	    
	    setPrior(iaPrior);//设置优先级
  }*/

  /**
   * 编辑表头字段时计算
   * @param HeadoldValue
   */
  private String calculate(Object HeadoldValue) {
	  //编辑整单折扣
	  if (getFirstChangedKey().equals(m_strAllDiscountRateKey)){
		  //原整单价税合计
		  UFDouble old_headsummny = getUFDoubleH(m_strheadstrSummnyKey);
		  //原整单折扣
		  UFDouble old_alldiscount = PuPubVO.getUFDouble_ValueAsValue(HeadoldValue);
		  //新整单折扣
		  UFDouble new_alldiscount = getUFDoubleH(m_strAllDiscountRateKey);
		  //新整单价税合计 = 原总价税合计 / 原整单折扣 * 新整单折扣
		  UFDouble new_headsummny = (old_headsummny.div(old_alldiscount)).multiply(new_alldiscount);
		  
		  //重新计算表体各行新的价税合计
		  recalculateBodysummny(new_headsummny,old_headsummny);
	  }	  
	  return null;
  }
  
  /**
   * 重新计算表体各行新的价税合计:按照各行价税合计比率计算各行价税合计，尾差挤入最后一行
   * @param new_headsummny
   * @param old_headsummny
   */
  private void recalculateBodysummny(UFDouble new_headsummny,UFDouble old_headsummny){
	  
  }
  
  /**
   * 作者：王印芬 功能：计算 参数：无 返回：无 例外：无 日期：(2003-6-17 11:39:21) 修改日期，修改人，修改原因，注释标志：
   */
  private String calculate() {

    // 得到改变的KEY的有效影响CIRCLE,而后执行
    int iAvailableCircle = getAvailableCircleByKey(getFirstChangedKey());
    
    //v5.5支持仅有金额的本币计算 zhangcheng jhp
    /*if (iAvailableCircle == CIRCLE_IMPOSSIBLE && 
    		!getFirstChangedKey().equals(m_strExchange_O_TO_BrateKey)) {
      return null;
    }*/

    // 开始执行--原币:汇率变动不进行原币的计算
    if (!getFirstChangedKey().equals(m_strExchange_O_TO_BrateKey))
    	execCircle(iAvailableCircle);
    
    //主辅币计算
    exec_LocalFracCurr();

    // 提高效率，在不查库的情况下，提高公式解析的效率 begin
    // m_pnlBill.getBillModel().getFormulaParse().setNeedDoPostConvert(bOldPostCovt)
    // ;

    //汇率检查
    checkLocalRate();

    // 检查是否存在不合理数值
    isResultsRight();

    // 如果不合理，所有数值回复原有数值
    if (m_strShowHintMsg != null) {
      setOldValuesToPanel();
    }
    // 后续校验处理
    processAfterCal();

    debugPrint(new String[] {
      ""
    });

    return m_strShowHintMsg;
  }

	/**
	 * v5.5
	 * 汇差约束：校验业务单据上的原本币折算关系，如果本币金额<>原币*（或/）汇率时且是前台操作，
	 *         读取外币汇率档案的“最大折算误差”，差额在最大折算误差范围内，直接继续后续操作，
	 *         差额超过最大误差，则给予用户提示，由用户选择操作继续还是取消。
	 *         后台处理时，不做差额和最大误差的校验提示
	 *         
	 * true ---- 本币金额=原币*（或/）汇率 
	 * false --- 本币金额<>原币*（或/）汇率时,并且超过汇率容差范围
	 */
  private void checkLocalRate(){ 
	  
	  if (m_voCurr==null||m_iPrior_LOCAL_FRAC == RelationsCalVO.NO_LOCAL_FRAC
			  ||((pk_CurrType!=null&&locaCurrType!=null)&&pk_CurrType.equals(locaCurrType))
				||m_strExchange_O_TO_BrateKey.equals("")
				||m_strCurrTypePkKey.equals("")
				||m_strPk_CorpKey.equals("")
				||m_strBillDateKey.equals("")
				||(!m_strMoneyKey.equals(m_strChangedKey)&&!m_strSummnyKey.equals(m_strChangedKey)
				   && !m_strMoney_LocalKey.equals(m_strChangedKey)&& !m_strSummny_LocalKey.equals(m_strChangedKey)
				   && !m_strExchange_O_TO_BrateKey.equals(m_strChangedKey))
				||(m_strMoney_LocalKey.equals(m_strChangedKey)&&(m_strMoneyKey.equals("")
						||m_strMoney_LocalKey.equals("")||m_voCurr.getAttributeValue(m_strMoneyKey)==null
						||m_voCurr.getAttributeValue(m_strMoney_LocalKey)==null)
				   )
				||(m_strSummny_LocalKey.equals(m_strChangedKey)&&(m_strSummnyKey.equals("")
						||m_strSummny_LocalKey.equals("")||m_voCurr.getAttributeValue(m_strSummnyKey)==null
						||m_voCurr.getAttributeValue(m_strSummny_LocalKey)==null)
				   )
				||m_voCurr.getAttributeValue(m_strExchange_O_TO_BrateKey)==null)
			return ;
		
		//按照VO中的原币金额、汇率，计算所对应的本币金额
		try {
			
			//单据日期、汇率
			String billDate = null;
			if (m_voCurr.getAttributeValue(m_strBillDateKey)== null )
				billDate = m_hvoCurr.getAttributeValue(m_strBillDateKey).toString();
			else
				billDate = m_voCurr.getAttributeValue(m_strBillDateKey).toString();
			
			if (pk_CurrType==null)
				// 原币PK
				if (m_voCurr.getAttributeValue(m_strCurrTypePkKey)==null && m_hvoCurr != null)
					pk_CurrType = m_hvoCurr.getAttributeValue(m_strCurrTypePkKey).toString();
				else
					pk_CurrType = m_voCurr.getAttributeValue(m_strCurrTypePkKey).toString();
				
			if (locaCurrType==null)
				locaCurrType = CurrParamQuery.getInstance().getLocalCurrPK(getCorpId());
			
			UFDouble brate = SmartVODataUtils
					.getUFDouble(getBusinessCurrencyRateUtil().getRate(
							pk_CurrType, locaCurrType, billDate));
			//汇率为０不校验
			if (brate.compareTo(this.ZERO)==0)
				return;
			
			String key_org = null;
			String key_loc = null;
			if (m_strChangedKey.equals(m_strSummny_LocalKey)||m_strChangedKey.equals(m_strSummnyKey)){
				key_org = m_strSummnyKey;
				key_loc = m_strSummny_LocalKey;
			}else if(m_strChangedKey.equals(m_strExchange_O_TO_BrateKey)
					&&m_strSummny_LocalKey!=null&&!"".equals(m_strSummny_LocalKey)){ 
				key_org = m_strSummnyKey;
				key_loc = m_strSummny_LocalKey;
			}else {
				key_org = m_strMoneyKey;
				key_loc = m_strMoney_LocalKey;
			}
			
			// 原币金额
			UFDouble[] amounts = new UFDouble[] { SmartVODataUtils
					.getUFDouble(m_voCurr.getAttributeValue(key_org)) };
			// VO中的本币金额
			UFDouble moneyLocal = SmartVODataUtils.getUFDouble(m_voCurr
					.getAttributeValue(key_loc));
			moneyLocal = moneyLocal == null ? UFDouble.ZERO_DBL : moneyLocal;
			
			//折算后的本币金额
			UFDouble[] result = null;
			
			//本币财务精度
/*			ICurrtype currQrySrv = (ICurrtype) NCLocator.getInstance().lookup(ICurrtype.class.getName());
			CurrtypeVO voRet = currQrySrv.findCurrtypeVOByPK(locaCurrType);*/
			CurrtypeVO voRet = CurrtypeQuery.getInstance().getCurrtypeVO(locaCurrType);
			
			// 金额精度
			int mnyRet = m_iFinaFlag == this.ForFinance ? voRet
					.getCurrdigit().intValue() : voRet.getCurrbusidigit().intValue();
				
			//本币计算
			result = getBusinessCurrencyRateUtil().getAmountsByOpp(
					pk_CurrType, locaCurrType, amounts, brate, billDate,mnyRet);
			
			//本币金额<>原币*（或/）汇率
			UFDouble submny = moneyLocal.sub(result[0] == null ? UFDouble.ZERO_DBL : result[0]).abs();
			//最大折算误差
			UFDouble maxconverr = getBusinessCurrencyRateUtil().getCurrinfoVO(pk_CurrType, locaCurrType).getMaxconverr();
			maxconverr = (maxconverr==null?UFDouble.ZERO_DBL:maxconverr);
			if (submny.doubleValue()!=0){
				if (submny.compareTo(maxconverr)>0)
					this.ratemny = maxconverr;
			}	
		} catch (BusinessException e) {
			nc.vo.scm.pub.SCMEnv.out("获取总账工具类失败\n" + e.getMessage());
		}
  }
  
  
  /**
   * 进行原币对本币\辅币的计算 2008-03-17
   */
  private void exec_LocalFracCurr(){
	  // 是否进行本币\辅币的计算
		if (!isCalLocalFrac_CurrEnable())
			return;
		try {
			
			// 原币PK
			if (m_voCurr.getAttributeValue(m_strCurrTypePkKey) == null && m_hvoCurr != null) {
				if (m_hvoCurr.getAttributeValue(m_strCurrTypePkKey) != null)
					pk_CurrType = m_hvoCurr.getAttributeValue(m_strCurrTypePkKey).toString();
			}
			else
				pk_CurrType = m_voCurr.getAttributeValue(m_strCurrTypePkKey).toString();
			
			if (pk_CurrType == null)
				return;
			
			// 本币PK
			locaCurrType = CurrParamQuery.getInstance().getLocalCurrPK(getCorpId());
			// 辅币PK
			fracCurrType = CurrParamQuery.getInstance().getFracCurrPK(getCorpId());
			
			// 单据日期
			String billDate = null;
			if (m_voCurr.getAttributeValue(m_strBillDateKey)== null )
				billDate = m_hvoCurr.getAttributeValue(m_strBillDateKey).toString();
			else
				billDate = m_voCurr.getAttributeValue(m_strBillDateKey).toString();
			
			// 单价精度
			int priceRet = getPowerPrice();
			
			// 金额精度
			int mnyRet = m_iFinaFlag==ForScm ? getPowerMnyBusi(locaCurrType) : getPowerMnyFina(locaCurrType);
			
			// 折本汇率
			UFDouble nRate = null;
			if (m_voCurr.getAttributeValue(m_strExchange_O_TO_BrateKey)== null )
				nRate = (UFDouble) m_hvoCurr.getAttributeValue(m_strExchange_O_TO_BrateKey);
			else
				nRate = (UFDouble) m_voCurr.getAttributeValue(m_strExchange_O_TO_BrateKey);
			
			//所有的原币价格
			UFDouble[] prices = new UFDouble[] { 
					getUFDouble(m_strPriceKey),getUFDouble(m_strTaxPriceKey),
					getUFDouble(m_strNetPriceKey),getUFDouble(m_strNetTaxPriceKey),
					getUFDouble(m_strQt_PriceKey),getUFDouble(m_strQt_TaxPriceKey),
					getUFDouble(m_strQt_NetPriceKey),getUFDouble(m_strQt_NetTaxPriceKey)};
			//所有的本币价格
			UFDouble[] priceresult = null;
			
			//如果原币价税合计和无税金额均为0,直接将本币金额置为0,单价用汇率折算
			if ((getUFDouble(m_strSummnyKey)==null&&getUFDouble(m_strMoneyKey)==null)
					||((getUFDouble(m_strSummnyKey)!=null && getUFDouble(m_strSummnyKey).doubleValue()==0 
					&& getUFDouble(m_strMoneyKey)!=null && getUFDouble(m_strMoneyKey).doubleValue()==0))){
				m_voCurr.setAttributeValue(m_strSummny_LocalKey, ZERO);//本币价税合计
				m_voCurr.setAttributeValue(m_strMoney_LocalKey, ZERO);//本币无税金额
				m_voCurr.setAttributeValue(m_strTax_LocalKey, ZERO);//本币无税金额
				m_voCurr.setAttributeValue(m_strDiscountMny_LocalKey, ZERO);//本币折扣额
				
				//主币计算																 
				priceresult = getBusinessCurrencyRateUtil().getAmountsByOpp(
						pk_CurrType, locaCurrType, prices, nRate, billDate,priceRet);
				
				m_voCurr.setAttributeValue(m_strPrice_LocalKey, priceresult[0]);//本币无税单价
				m_voCurr.setAttributeValue(m_strTaxPrice_LocalKey, priceresult[1]);//本币含税单价
				m_voCurr.setAttributeValue(m_strNet_Price_LocalKey, priceresult[2]);//本币无税净价
				m_voCurr.setAttributeValue(m_strNet_TaxPrice_LocalKey, priceresult[3]);//本币含税净价
				m_voCurr.setAttributeValue(m_strQt_Price_LocalKey, priceresult[4]);//本币报价计量单位无税单价
				m_voCurr.setAttributeValue(m_strQt_TaxPrice_LocalKey, priceresult[5]);//本币报价计量单位含税单价
				m_voCurr.setAttributeValue(m_strQt_Net_Price_LocalKey, priceresult[6]);//本币报价计量单位无税净价
				m_voCurr.setAttributeValue(m_strQt_Net_TaxPrice_LocalKey, priceresult[7]);//本币报价计量单位含税净价
				
				//是否主辅币核算
				if (CurrParamQuery.getInstance().isBlnLocalFrac(getCorpId())){
					m_voCurr.setAttributeValue(m_strSummny_FractionalKey, ZERO);//辅币价税合计
					m_voCurr.setAttributeValue(m_strMoney_FractionalKey, ZERO);//辅币无税金额
					m_voCurr.setAttributeValue(m_strDiscountMny_FractionalKey, ZERO);//辅币折扣额
					m_voCurr.setAttributeValue(m_strPrice_FractionalKey, ZERO);//辅币无税单价
					m_voCurr.setAttributeValue(m_strTaxPrice_FractionalKey, ZERO);//辅币含税单价
					m_voCurr.setAttributeValue(m_strNet_Price_FractionalKey, ZERO);//辅币无税净价
					m_voCurr.setAttributeValue(m_strNet_TaxPrice_FractionalKey, ZERO);//辅币含税净价
				}
				
				//最后进行报价计量单位本币单价计算
				exec_QtLocalPrice();
				
				return;
			}
			//非主辅币核算并且本币=源币,所有本币直接取源币
			else if (!CurrParamQuery.getInstance().isBlnLocalFrac(getCorpId()) && pk_CurrType.equals(locaCurrType)){
				m_voCurr.setAttributeValue(m_strSummny_LocalKey, m_voCurr.getAttributeValue(m_strSummnyKey));//本币价税合计
				m_voCurr.setAttributeValue(m_strMoney_LocalKey, m_voCurr.getAttributeValue(m_strMoneyKey));//本币无税金额
				m_voCurr.setAttributeValue(m_strTax_LocalKey, m_voCurr.getAttributeValue(m_strTaxKey));//本币无税金额
				m_voCurr.setAttributeValue(m_strDiscountMny_LocalKey, m_voCurr.getAttributeValue(m_strDiscountMnyKey));//本币折扣额
				
				m_voCurr.setAttributeValue(m_strPrice_LocalKey, m_voCurr.getAttributeValue(m_strPriceKey));//本币无税单价
				m_voCurr.setAttributeValue(m_strTaxPrice_LocalKey, m_voCurr.getAttributeValue(m_strTaxPriceKey));//本币含税单价
				m_voCurr.setAttributeValue(m_strNet_Price_LocalKey, m_voCurr.getAttributeValue(m_strNetPriceKey));//本币无税净价
				m_voCurr.setAttributeValue(m_strNet_TaxPrice_LocalKey, m_voCurr.getAttributeValue(m_strNetTaxPriceKey));//本币含税净价
				
				m_voCurr.setAttributeValue(m_strQt_Price_LocalKey, m_voCurr.getAttributeValue(m_strQt_PriceKey));//本币报价计量单位无税单价
				m_voCurr.setAttributeValue(m_strQt_TaxPrice_LocalKey, m_voCurr.getAttributeValue(m_strQt_TaxPriceKey));//本币报价计量单位含税单价
				m_voCurr.setAttributeValue(m_strQt_Net_Price_LocalKey, m_voCurr.getAttributeValue(m_strQt_NetPriceKey));//本币报价计量单位无税净价
				m_voCurr.setAttributeValue(m_strQt_Net_TaxPrice_LocalKey, m_voCurr.getAttributeValue(m_strQt_NetTaxPriceKey));//本币报价计量单位含税净价
				
				return;
			}
			
			//原币价税合计、无税金额、折扣额
			UFDouble[] amounts = new UFDouble[] { getUFDouble(m_strSummnyKey),
					getUFDouble(m_strMoneyKey),
					getUFDouble(m_strDiscountMnyKey) };
			//折算后的价税合计、无税金额、折扣额
			UFDouble[] result = null;
			
			// 是否进行主辅币核算
			if (CurrParamQuery.getInstance().isBlnLocalFrac(getCorpId())
					&& (isCalFieldExist(new String[] {
							m_strSummny_FractionalKey,m_strTax_LocalKey,
							m_strMoney_FractionalKey, m_strSummny_LocalKey,
							m_strMoney_LocalKey, m_strNumKey,m_strTax_FractionalKey }))) {// 主辅币核算
				
				// 折辅汇率
				UFDouble aRate = null;
				if (m_voCurr.getAttributeValue(m_strExchange_O_TO_ArateKey)== null )
					aRate = (UFDouble) m_hvoCurr.getAttributeValue(m_strExchange_O_TO_ArateKey);
				else
					aRate = (UFDouble) m_voCurr.getAttributeValue(m_strExchange_O_TO_ArateKey);
				
				//辅币计算
				result = getBusinessCurrencyRateUtil().getAmountsByOpp(
						pk_CurrType, fracCurrType, amounts, aRate, billDate,
						mnyRet);
				m_voCurr.setAttributeValue(m_strSummny_FractionalKey, result[0]);//辅币价税合计
				m_voCurr.setAttributeValue(m_strMoney_FractionalKey, result[1]);//辅币无税金额
				if (!m_strDiscountMny_FractionalKey.equals(""))
					m_voCurr.setAttributeValue(m_strDiscountMny_FractionalKey, result[2]);//辅币折扣额
				//源币=辅币，则价格直接赋值
				if (pk_CurrType.equals(fracCurrType)){
					m_voCurr.setAttributeValue(m_strTax_FractionalKey, m_voCurr.getAttributeValue(m_strTaxKey));//辅币税额
					m_voCurr.setAttributeValue(m_strPrice_FractionalKey, m_voCurr.getAttributeValue(m_strPriceKey));//辅币无税单价
					m_voCurr.setAttributeValue(m_strTaxPrice_FractionalKey, m_voCurr.getAttributeValue(m_strTaxPriceKey));//辅币含税单价
					m_voCurr.setAttributeValue(m_strNet_Price_FractionalKey, m_voCurr.getAttributeValue(m_strNetPriceKey));//辅币无税净价
					m_voCurr.setAttributeValue(m_strNet_TaxPrice_FractionalKey, m_voCurr.getAttributeValue(m_strNetTaxPriceKey));//辅币含税净价
				}
				else
					exec_FracPrice();//计算辅币价格，并设置回m_voCurr
				
				//本币计算
				//1.取辅币到本币的汇率
				nRate = getBusinessCurrencyRateUtil().getRate(fracCurrType, locaCurrType, billDate);
				//2.计算本币
				result = getBusinessCurrencyRateUtil().getAmountsByOpp(
						fracCurrType, locaCurrType, result, nRate, billDate,
						mnyRet);
				m_voCurr.setAttributeValue(m_strSummny_LocalKey, result[0]);//本币价税合计
				m_voCurr.setAttributeValue(m_strMoney_LocalKey, result[1]);//本币无税金额
				if (!m_strDiscountMny_LocalKey.equals(""))
				    m_voCurr.setAttributeValue(m_strDiscountMny_LocalKey, result[2]);//本币折扣额
				//本币=辅币，则价格直接赋值
				if (fracCurrType.equals(locaCurrType)){
					m_voCurr.setAttributeValue(m_strTax_LocalKey, m_voCurr.getAttributeValue(m_strTax_FractionalKey));//本币税额
					m_voCurr.setAttributeValue(m_strPrice_LocalKey, m_voCurr.getAttributeValue(m_strPrice_FractionalKey));//本币无税单价
					m_voCurr.setAttributeValue(m_strTaxPrice_LocalKey, m_voCurr.getAttributeValue(m_strTaxPrice_FractionalKey));//本币含税单价
					m_voCurr.setAttributeValue(m_strNet_Price_LocalKey, m_voCurr.getAttributeValue(m_strNet_Price_FractionalKey));//本币无税净价
					m_voCurr.setAttributeValue(m_strNet_TaxPrice_LocalKey, m_voCurr.getAttributeValue(m_strNet_TaxPrice_FractionalKey));//本币含税净价
				}
				else
					exec_LocalPrice();//计算本币价格，并设置回m_voCurr
				
			} else {// 单主币核算,只计算本币
				//主币计算																 
				result = getBusinessCurrencyRateUtil().getAmountsByOpp(
						pk_CurrType, locaCurrType, amounts, nRate, billDate,
						mnyRet);
				m_voCurr.setAttributeValue(m_strSummny_LocalKey, result[0]);//本币价税合计
				m_voCurr.setAttributeValue(m_strMoney_LocalKey, result[1]);//本币无税金额
				if (!m_strDiscountMny_LocalKey.equals(""))
				    m_voCurr.setAttributeValue(m_strDiscountMny_LocalKey, result[2]);//本币折扣额
				exec_LocalPrice();//计算主币价格，并设置回m_voCurr
			}
			
			//最后进行报价计量单位本币单价计算
			exec_QtLocalPrice();
			
		} catch (Exception e) {
			nc.vo.scm.pub.SCMEnv.error("计算本币单价金额失败!", e);
		}
		return;
  }
  
  /**
   * 计算辅币价格，并设置回m_voCurr
   */
  private void exec_FracPrice(){
	  if (!m_strTax_FractionalKey.equals(""))
		  execFormulas(new String[] {m_strTax_FractionalKey + "->" + m_strSummny_FractionalKey + "-" + m_strMoney_FractionalKey});
	  
	  if (!isCalFieldExist(new String[] {m_strTax_FractionalKey,m_strNet_TaxPrice_FractionalKey,m_strNet_Price_FractionalKey,
			  m_strTaxPrice_FractionalKey,m_strPrice_FractionalKey,m_strTaxRateKey}))
		  return;
	  String formulas[] = new String[] {
	      m_strNet_TaxPrice_FractionalKey + "->" + m_strSummny_FractionalKey + "/" + m_strNumKey ,
	      m_strNet_Price_FractionalKey + "->" + m_strMoney_FractionalKey + "/" + m_strNumKey ,
	      m_strTaxPrice_FractionalKey + "->(" + m_strSummny_FractionalKey + "+" + m_strDiscountMny_FractionalKey + ")/" + m_strNumKey ,
	      m_strPrice_FractionalKey + "->(" + m_strSummny_FractionalKey + "+" + m_strDiscountMny_FractionalKey + ")/[(1.0+" 
	         + m_strTaxRateKey + "/100.0)*" + m_strNumKey + "]" 
	  };
	  execFormulas(formulas);
  }
  
  /**
   * 计算本币价格，并设置回m_voCurr
   */
  private void exec_LocalPrice(){
	  if (!m_strTax_LocalKey.equals(""))
		  execFormulas(new String[] {m_strTax_LocalKey + "->" + m_strSummny_LocalKey + "-" + m_strMoney_LocalKey});
	  
	  if ("".equals(m_strSummny_LocalKey)&&"".equals(m_strMoney_LocalKey))
		  return;
	  
	  //净价
	  if (isCalFieldExist(new String[] { m_strNet_TaxPrice_LocalKey,
				m_strNet_Price_LocalKey, m_strNumKey })){
		  String[] formulas = new String[] {
				  m_strNet_TaxPrice_LocalKey + "->" + m_strSummny_LocalKey + "/" + m_strNumKey ,
			      m_strNet_Price_LocalKey + "->" + m_strMoney_LocalKey + "/" + m_strNumKey 
			  };
		  execFormulas(formulas);
	  }
	  
	  //单价
	  if (isCalFieldExist(new String[] { m_strTaxPrice_LocalKey,
				m_strPrice_LocalKey, m_strTaxRateKey,m_strNumKey })){
		  String[] formulas = new String[] {
			      m_strTaxPrice_LocalKey + "->(" + m_strSummny_LocalKey + "+" + m_strDiscountMny_LocalKey + ")/" + m_strNumKey ,
			      m_strPrice_LocalKey + "->(" + m_strSummny_LocalKey + "+" + m_strDiscountMny_LocalKey + ")/[(1.0+" 
			         + m_strTaxRateKey + "/100.0)*" + m_strNumKey + "]" 
			  }; 
		  execFormulas(formulas);
	  }


  }
  
  /**
   * 计算报价计量单位本币价格，并设置回m_voCurr
   */
  private void exec_QtLocalPrice(){
	  if (!isCalFieldExist(new String[] {m_strQt_Net_TaxPrice_LocalKey,m_strQt_Net_Price_LocalKey,
			  m_strQt_TaxPrice_LocalKey,m_strQt_Price_LocalKey,m_strTaxRateKey,m_strQt_NumKey}))
		  return;
	  String formulas[] = new String[] {
			  m_strQt_Net_TaxPrice_LocalKey + "->" + m_strSummny_LocalKey + "/" + m_strQt_NumKey ,
			  m_strQt_Net_Price_LocalKey + "->" + m_strMoney_LocalKey + "/" + m_strQt_NumKey ,
			  m_strQt_TaxPrice_LocalKey + "->(" + m_strSummny_LocalKey + "+" + m_strDiscountMny_LocalKey + ")/" + m_strQt_NumKey ,
			  m_strQt_Price_LocalKey + "->(" + m_strSummny_LocalKey + "+" + m_strDiscountMny_LocalKey + ")/[(1.0+" 
		      + m_strTaxRateKey + "/100.0)*" + m_strQt_NumKey + "]" 
		  };
		  execFormulas(formulas);
  }
  
  /**
	 * @return 返回总账工具类：用于计算本/辅币
	 */
  private BusinessCurrencyRateUtil getBusinessCurrencyRateUtil(){
	  if (bcurr == null){
		  try {
			if (m_voCurr.getAttributeValue(m_strPk_CorpKey)!=null)
				bcurr = new BusinessCurrencyRateUtil(m_voCurr.getAttributeValue(m_strPk_CorpKey).toString());
			else
				bcurr = new BusinessCurrencyRateUtil(m_hvoCurr.getAttributeValue(m_strPk_CorpKey).toString());
		} catch (Exception e) {
			nc.vo.scm.pub.SCMEnv.out("获取总账工具类失败\n" + e.getMessage());
			return null;
		}		  
	  }
	  return bcurr;
  }
/**
* @return 公司主键：用于计算金额
*/
private String getCorpId(){
	String strCorpId = null;
	//
	if (m_voCurr.getAttributeValue(m_strPk_CorpKey)!=null)
		strCorpId = m_voCurr.getAttributeValue(m_strPk_CorpKey).toString();
	else
		strCorpId = m_hvoCurr.getAttributeValue(m_strPk_CorpKey).toString();
	//
	return strCorpId;
} 
  /**
   * 作者：王印芬 功能：用于跟踪打印公式 参数：无 返回：无 例外：无 日期：(2003-06-09 11:22:51)
   * 修改日期2008-03-14，修改人 张成，修改原因 增加中文打印功能，注释标志：
   */
  private void debugPrint(String[] saFormula) {
    if (saFormula == null) {
      return;
    }
    if (true) {
      int iLen = saFormula.length;
      if (debug)
        SCMEnv.out("\n~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n");
      String split = "->*/+-()";//分割标志字符（为了显示中文）
      StringTokenizer token = null;//存放分割出来的单词
      String cur_token = null;//当前单词
      String chineseFormula = null;//中文公式
      String keyValueFormula = null;//字段实际值
      String curChineseName = null;//当前字段中文名
      for (int i = 0; i < iLen; i++) {
    	  chineseFormula = saFormula[i];
    	  keyValueFormula = saFormula[i];
    	  token = new StringTokenizer(saFormula[i], split);//分割单词
    	  while (token.hasMoreTokens()) {//拼接中文公式
    		cur_token = token.nextToken();
    		curChineseName = getChineseName(cur_token);
    		if (!cur_token.equals(curChineseName)){
    			//当前字段不是数字
    			chineseFormula = chineseFormula.replaceAll(cur_token, curChineseName);
    			keyValueFormula = m_voCurr.getAttributeValue(cur_token)==null ? "" :
    				keyValueFormula.replaceAll(cur_token, m_voCurr.getAttributeValue(cur_token).toString());
    		}
  		  }
    	  if (debug){
    		  nc.vo.scm.pub.SCMEnv.out(chineseFormula + " ### " + saFormula[i]);//打印公式(中文 + 原始)
    		  nc.vo.scm.pub.SCMEnv.out(keyValueFormula);//打印公式(数值) 
    	  }
    	  
      }// end for
      
    }// end if
    
  }

  /**
   * @param cur_token
   * @return 返回传入字符串中文名称，如果是数字，直接返回
   */
  private String getChineseName(String cur_token){
	  if (m_strNumKey.equals(cur_token))  return "主数量";
	  else if (m_strTaxRateKey.equals(cur_token))  return "税率";
	  else if (m_strDiscountRateKey.equals(cur_token))  return "单品折扣";
	  else if (m_strAllDiscountRateKey.equals(cur_token))  return "整单折扣";
	  else if (m_strConvertRateKey.equals(cur_token))  return "换算率";
	  else if (m_strQt_ConvertRateKey.equals(cur_token))  return "报价单位换算率";
	  else if (m_strAssistNumKey.equals(cur_token))  return "辅数量";
	  else if (m_strUnitID.equals(cur_token))  return "主单位";
	  else if (m_strQuoteUnitID.equals(cur_token))  return "报价计量单位";
	  else if (m_strCurrTypePkKey.equals(cur_token))  return "原币PK";
	  else if (m_strPk_CorpKey.equals(cur_token))  return "公司PK";
	  else if (m_strExchange_O_TO_BrateKey.equals(cur_token))  return "折本汇率";
	  else if (m_strBillDateKey.equals(cur_token))  return "单据日期";
	  
	  //原币
	  else if (m_strNetPriceKey.equals(cur_token))  return "无税净价";
	  else if (m_strNetTaxPriceKey.equals(cur_token))  return "含税净价";
	  else if (m_strPriceKey.equals(cur_token))  return "无税单价";
	  else if (m_strTaxPriceKey.equals(cur_token))  return "含税单价";
	  else if (m_strSummnyKey.equals(cur_token))  return "价税合计";
	  else if (m_strMoneyKey.equals(cur_token))  return "无税金额";
	  else if (m_strTaxKey.equals(cur_token))  return "税额";
	  else if (m_strDiscountMnyKey.equals(cur_token))  return "折扣额";
	  else if (m_strheadstrSummnyKey.equals(cur_token))  return "整单价税合计";
	  
	  else if (m_strQt_NumKey.equals(cur_token))  return "报价单位数量";
	  else if (m_strQt_TaxPriceKey.equals(cur_token))  return "报价单位含税单价";
	  else if (m_strQt_PriceKey.equals(cur_token))  return "报价单位无税单价";
	  else if (m_strQt_NetTaxPriceKey.equals(cur_token))  return "报价单位含税净价";
	  else if (m_strQt_NetPriceKey.equals(cur_token))  return "报价单位无税净价";
	  
	  else if (m_strAssist_Price.equals(cur_token))  return "辅计量无税单价";
	  else if (m_strAssist_TaxPrice.equals(cur_token))  return "辅计量含税单价";
	  else if (m_strAsk_TaxPriceKey.equals(cur_token))  return "询价含税单价";
	  else if (m_strAsk_PriceKey.equals(cur_token))  return "询价无税单价";

	  //本币
	  else if (m_strPrice_LocalKey.equals(cur_token))  return "本币无税单价";
	  else if (m_strTaxPrice_LocalKey.equals(cur_token))  return "本币含税单价";
	  else if (m_strNet_Price_LocalKey.equals(cur_token))  return "本币无税净价";
	  else if (m_strNet_TaxPrice_LocalKey.equals(cur_token))  return "本币含税净价";
	  else if (m_strTax_LocalKey.equals(cur_token))  return "本币税额";
	  else if (m_strMoney_LocalKey.equals(cur_token))  return "本币无税金额";
	  else if (m_strSummny_LocalKey.equals(cur_token))  return "本币价税合计";
	  else if (m_strDiscountMny_LocalKey.equals(cur_token))  return "本币折扣额";
	  
	  else if (m_strQt_Price_LocalKey.equals(cur_token))  return "报价单位本币无税单价";
	  else if (m_strQt_TaxPrice_LocalKey.equals(cur_token))  return "报价单位本币含税单价";
	  else if (m_strQt_Net_Price_LocalKey.equals(cur_token))  return "报价单位本币无税净价";
	  else if (m_strQt_Net_TaxPrice_LocalKey.equals(cur_token))  return "报价单位本币含税净价";
	  
	  //辅币
	  else if (m_strPrice_FractionalKey.equals(cur_token))  return "辅币无税单价";
	  else if (m_strTaxPrice_FractionalKey.equals(cur_token))  return "辅币含税单价";
	  else if (m_strNet_Price_FractionalKey.equals(cur_token))  return "辅币无税净价";
	  else if (m_strNet_TaxPrice_FractionalKey.equals(cur_token))  return "辅币含税净价";
	  else if (m_strTax_FractionalKey.equals(cur_token))  return "辅币税额";
	  else if (m_strMoney_FractionalKey.equals(cur_token))  return "辅币无税金额";
	  else if (m_strSummny_FractionalKey.equals(cur_token))  return "辅币价税合计";
	  else if (m_strDiscountMny_FractionalKey.equals(cur_token))  return "辅币折扣额";

	  else if (m_strQualifiedNumKey.equals(cur_token))  return "合格数量";
	  else if (m_strUnQualifiedNumKey.equals(cur_token))  return "不合格数量";
	  
	  return cur_token;//返回数值
  }
  
  /**
   * 如果最先编辑的是数量则其他计量单位的数量正负号应与当前编辑值保持一致
   */
  private void execCircle_Num(){
	  Object numf = m_voCurr.getAttributeValue(getFirstChangedKey());
	  if (numf==null) 
		  return;
	  
	  Object num = m_voCurr.getAttributeValue(m_strNumKey);
	  Object num_qt = m_voCurr.getAttributeValue(m_strQt_NumKey);
	  Object num_ass = m_voCurr.getAttributeValue(m_strAssistNumKey);
	  UFDouble zero = new UFDouble(0);
	  
	  if ( ((UFDouble)numf).compareTo(zero) < 0 ){
		  if (num!=null)
			  m_voCurr.setAttributeValue(m_strNumKey,zero.sub(((UFDouble)num).abs()));
		  if (num_qt!=null)
			  m_voCurr.setAttributeValue(m_strQt_NumKey,zero.sub(((UFDouble)num_qt).abs()));
		  if (num_ass!=null)
			  m_voCurr.setAttributeValue(m_strAssistNumKey,zero.sub(((UFDouble)num_ass).abs()));
	  }
	  else if ( ((UFDouble)numf).compareTo(zero) > 0 ){
		  if (num!=null)
			  m_voCurr.setAttributeValue(m_strNumKey,((UFDouble)num).abs());
		  if (num_qt!=null)
			  m_voCurr.setAttributeValue(m_strQt_NumKey,((UFDouble)num_qt).abs());
		  if (num_ass!=null)
			  m_voCurr.setAttributeValue(m_strAssistNumKey,((UFDouble)num_ass).abs());
	  } 
  }
  
  /**
   * 作者：王印芬 功能：判断入口哪个CIRCLE而后执行 参数：int circle 具体CIRCLE 返回：无 例外：无 日期：(2002-5-15
   * 11:39:21) 修改日期，修改人，修改原因，注释标志：
   */
  private void execCircle(int circle) {

    if (circle == CIRCLE_IMPOSSIBLE) {
      return;
    }
    
    // 如果最先编辑的是数量则其他计量单位的数量正负号应与当前编辑值保持一致
    if (getFirstChangedKey().equals(m_strNumKey)
    		|| getFirstChangedKey().equals(m_strAssistNumKey)
    		|| getFirstChangedKey().equals(m_strQt_NumKey))
    	execCircle_Num();
    
    // 进行深度优先的执行
    if (circle == CIRCLE_NUM_NETPRICE_MONEY) {
      execCircle_NumNetPriceMoney(getFirstChangedKey());
    }
    else if (circle == CIRCLE_TAXRATE) {
      execCircle_Taxrate(getFirstChangedKey());
    }
    else if (circle == CIRCLE_NUM_NETTAXPRICE_SUMMNY) {
      execCircle_NumNetTaxPriceSummny(getFirstChangedKey());
    }
    else if (circle == CIRCLE_PRICE_NETPRICE) {
    /**v5.3 改变流程，使得无税优先时，保证净价之间的税率关系,而不是含税价的扣率关系。故将以下注释掉 */	
     /* if (getFirstChangedKey().equals(m_strDiscountRateKey)) {
        execCircle_TaxPriceNetTaxPrice(getFirstChangedKey(), false);
      }*/
      execCircle_PriceNetPrice(getFirstChangedKey(), true);
    }
    else if (circle == CIRCLE_QUALIFIED_NUM) {
      execCircle_QualifiedNum(getFirstChangedKey());
    }
    else if (circle == CIRCLE_CONVERT_RATE) {
      execCircle_ConvertRate(getFirstChangedKey());
    }
    else if (circle == CIRCLE_TAXPRICE_NETTAXPRICE) {
     /**v5.3 改变流程，使得含税优先时，保证净价之间的税率关系,而不是无税价的扣率关系。故将以下注释掉 */	
     /* if (getFirstChangedKey().equals(m_strDiscountRateKey)) {
        execCircle_PriceNetPrice(getFirstChangedKey(), false);
      }*/
      execCircle_TaxPriceNetTaxPrice(getFirstChangedKey(), true);
    }
    else if (circle == CIRCLE_TAXPRICE_PRICE) {
      execCircle_TaxPricePrice(getFirstChangedKey(), true);
    }
    else if (circle == CIRCLE_NETTAXPRICE_NETPRICE) {
      execCircle_NetTaxPriceNetPrice(getFirstChangedKey(), true);
    }
    else if (circle == CIRCLE_QT_TAXPRICE_PRICE) {//税率、报价原币含税单价、报价原币无税单价
    	execCircle_Qt_TaxPricePrice(getFirstChangedKey(), true);
    }
    else if (circle == CIRCLE_QT_NETTAXPRICE_NETPRICE) {//税率、报价原币含税净价、报价原币无税净价
    	execCircle_Qt_NetTaxPriceNetPrice(getFirstChangedKey(), true);
    }
    else if (circle == CIRCLE_QT_TAXPRICE_NETTAXPRICE) {// 扣率、报价原币含税单价、报价原币含税净价
    	execCircle_Qt_TaxPriceNetTaxPrice(getFirstChangedKey(), true);
    }
    else if (circle == CIRCLE_QT_PRICE_NETPRICE) {// 扣率、报价原币无税单价、报价原币无税净价
        execCircle_Qt_PriceNetPrice(getFirstChangedKey(), true);
    }
    else if (circle == CIRCLE_QT_CONVERT_RATE) {
    	execCircle_Qt_ConvertRate(getFirstChangedKey());
    }
    else if (circle == CIRCLE_QT_NUM_NETTAXPRICE_SUMMNY) {
    	execCircle_Qt_NumNetTaxPriceSummny(getFirstChangedKey());
    }
    else if (circle == CIRCLE_QT_NUM_NETPRICE_MONEY) {
    	execCircle_Qt_NumNetPriceMoney(getFirstChangedKey());
    }
    else if (circle == CIRCLE_LOCAL_SUMMNY_MNY_TAX) {
    	execCircle_Local_Summny_Mny_Tax(getFirstChangedKey());
    }
    
    /**进行除FirstChangedKey所属计量单位之外的另一种计量单位原币的计算(仅限主计量/报价计量)*/
    execCircle_AnotherUnit(getFirstChangedKey());
    
    /**最后进行辅计量单位单价的计算*/
    execCircle_ViaPrice(getFirstChangedKey());
    
    /**无论编辑什么字段(除税额),最后都会进行折扣额的计算 */
    execCircle_DiscountMny(getFirstChangedKey(),false);
    
    return;
  }
  
  /**
   * 进行除FirstChangedKey所属计量单位之外的另一种计量单位原币的计算
   * 如果FirstChangedKey---主计量单位,则进行报价计量单位的计算
   * 如果FirstChangedKey---报价计量单位,则进行主计量单位的计算
   * @param sChangedKey
   */
  private void execCircle_AnotherUnit(String sChangedKey){
	  if ((m_strUnitID == null || m_strQuoteUnitID == null
				|| m_strUnitID.equals("") || m_strQuoteUnitID.equals(""))
				&& (getUFDouble(m_strNumKey) == null || getUFDouble(m_strQt_NumKey) == null))
			return;
	  
	  //最先变动的是报价计量单位
	  if (sChangedKey.equals(m_strQt_TaxPriceKey) || sChangedKey.equals(m_strQt_PriceKey)
			  || sChangedKey.equals(m_strQt_NetTaxPriceKey) || sChangedKey.equals(m_strQt_NumKey)
			  || sChangedKey.equals(m_strQt_NetPriceKey) || sChangedKey.equals(m_strQt_ConvertRateKey)){
		  if (getUFDouble(m_strNumKey).doubleValue() == getUFDouble(
					m_strQt_NumKey).doubleValue()) {
			  m_voCurr.setAttributeValue(m_strNetTaxPriceKey, m_voCurr.getAttributeValue(m_strQt_NetTaxPriceKey));
			  m_voCurr.setAttributeValue(m_strNetPriceKey, m_voCurr.getAttributeValue(m_strQt_NetPriceKey));
			  m_voCurr.setAttributeValue(m_strTaxPriceKey, m_voCurr.getAttributeValue(m_strQt_TaxPriceKey));
			  m_voCurr.setAttributeValue(m_strPriceKey, m_voCurr.getAttributeValue(m_strQt_PriceKey));
		  }
		  //主\报计量单位不同,并且最先编辑值非报价数量、报价换算率:以价税合计或无税金额为触发点，进行主计量单位的计算
		  else if ( !sChangedKey.equals(m_strQt_NumKey) && !sChangedKey.equals(m_strQt_ConvertRateKey)){
			  reInitData(true);//重新初始化数据
			  if (sChangedKey.equals(m_strQt_TaxPriceKey) || sChangedKey.equals(m_strQt_NetTaxPriceKey))
				  execCircle_NumNetTaxPriceSummny(m_strSummnyKey);
			  else
				  execCircle_NumNetPriceMoney(m_strMoneyKey);
		  }
	  }
	  //最先变动的是主计量单位
	  else{
		  //如果没有报价计量单位数量，不进行报价计量单位相关计算
		  if(getUFDouble(m_strQt_NumKey)==null)
			  return;
		  
		  if ((getUFDouble(m_strNumKey).doubleValue() == getUFDouble(
					m_strQt_NumKey).doubleValue())||
		(
			(m_voCurr.getAttributeValue(m_strUnitID)!=null&&m_voCurr.getAttributeValue(m_strQuoteUnitID)!=null)	&& m_voCurr.getAttributeValue(m_strUnitID).toString().equals(m_voCurr.getAttributeValue(m_strQuoteUnitID).toString()) )
		
		  ) {
        	  m_voCurr.setAttributeValue(m_strQt_NetTaxPriceKey, m_voCurr.getAttributeValue(m_strNetTaxPriceKey));
        	  m_voCurr.setAttributeValue(m_strQt_NetPriceKey, m_voCurr.getAttributeValue(m_strNetPriceKey));
        	  m_voCurr.setAttributeValue(m_strQt_TaxPriceKey, m_voCurr.getAttributeValue(m_strTaxPriceKey));
        	  m_voCurr.setAttributeValue(m_strQt_PriceKey, m_voCurr.getAttributeValue(m_strPriceKey));
		  }
          //最先变动的为税率：只计算报价原币无税单\净价
          else {
        	  if (sChangedKey.equals(m_strTaxRateKey)){
        		  execCircle_Qt_TaxPricePrice(m_strTaxRateKey,false);
        		  execCircle_Qt_NetTaxPriceNetPrice(m_strTaxRateKey,false);
        	  }
        	  //主\报计量单位不同,并且(变动换算率或者报价价格=null)，最先变动的非税率:以价税合计或无税金额为触发点，进行报价计量单位的计算
    		  else if (!isQt_FixedConvertRate()
						|| (getUFDouble(m_strQt_NetTaxPriceKey) == null
								|| getUFDouble(m_strQt_NetPriceKey) == null
								|| getUFDouble(m_strQt_TaxPriceKey) == null || getUFDouble(m_strQt_PriceKey) == null)) {
    			  reInitData(false);//重新初始化数据
    			  //含税优先
    			  if (sChangedKey.equals(m_strTaxPriceKey)||sChangedKey.equals(m_strNetTaxPriceKey)
    					  ||sChangedKey.equals(m_strSummnyKey))
    				  execCircle_Qt_NumNetTaxPriceSummny(m_strSummnyKey);
    			  //无税优先
    			  else if (sChangedKey.equals(m_strPriceKey)||sChangedKey.equals(m_strNetPriceKey)
    					  ||sChangedKey.equals(m_strMoneyKey))
    				  execCircle_Qt_NumNetPriceMoney(m_strMoneyKey);
    			  //无税优先
    			  else if (getPrior_Price_TaxPrice() == RelationsCalVO.PRICE_PRIOR_TO_TAXPRICE)
    				  execCircle_Qt_NumNetPriceMoney(m_strMoneyKey);
    			  //含税优先
    			  else if (getPrior_Price_TaxPrice() == RelationsCalVO.TAXPRICE_PRIOR_TO_PRICE)
    				  execCircle_Qt_NumNetTaxPriceSummny(m_strSummnyKey);
    		  }
          }
	  }
  }
  
  /**
   * 用于第二次计算另一种计量单位价格时，重新初始化数据
   * qt--true:  最先变动的是报价计量单位
   * qt--false: 最先变动的是主计量单位
   */
  private void reInitData(boolean qt){
	  initCircle();//重新设置各个Circle没有执行过
	  
	  //最先变动的是报价计量单位:将主计量4个价格置为不曾改过
	  if (qt){
		  setKeyExecuted(m_strTaxPriceKey, false);
		  setKeyExecuted(m_strNetTaxPriceKey, false);
		  setKeyExecuted(m_strPriceKey, false);
		  setKeyExecuted(m_strNetPriceKey, false);
	  }
	  //最先变动的是主计量单位：将报价计量4个价格置为不曾改过
	  else{
		  setKeyExecuted(m_strQt_TaxPriceKey, false);
		  setKeyExecuted(m_strQt_NetTaxPriceKey, false);
		  setKeyExecuted(m_strQt_PriceKey, false);
		  setKeyExecuted(m_strQt_NetPriceKey, false);
	  }
  }
  
  /**
   * 进行辅计量单位的计算--编辑各种数量、换算率、税额时不进行计算
   * @param sChangedKey
   */
  private void execCircle_ViaPrice(String sChangedKey){
	  // 已执行过或不可执行,则不再执行
	  if (isCircleExecuted(CIRCLE_VIAPRICE)
		    || !isCircleExecutable(CIRCLE_VIAPRICE, sChangedKey))
		  return;
		    
		String formulas[] = {m_strAssist_TaxPrice + "->(" + m_strSummnyKey + "+" + m_strDiscountMnyKey + ")/" + m_strAssistNumKey ,
				m_strAssist_Price + "->(" + m_strSummnyKey + "+" + m_strDiscountMnyKey + ")/[(1.0+" 
			         + m_strTaxRateKey + "/100.0)*" + m_strAssistNumKey + "]" };
		execFormulas(formulas);
		// 设置该KEY已改变
		setKeysExecuted(CIRCLE_VIAPRICE, true);
		// 设置执行标记
		setCircleExecuted(CIRCLE_VIAPRICE, true);
		return;
	  
  }
  
  /**
   * 作者：张成 功能：含税单价、数量、价税合计、折扣额CIRCLE中,根据一个具体改变的项计算其他项 
   * 当编辑税额时，此圈不进行计算  参数：String sChangedKey
   * 影响该CIRCLE的项 返回：无 例外：无 日期：(2008-3-14 11:39:21) 修改日期，修改人，修改原因，注释标志：
   */
  private void execCircle_DiscountMny(String sChangedKey,boolean bExecFollowing) {
	// 已执行过或不可执行,则不再执行
	if (isCircleExecuted(CIRCLE_DISCOUNTMNY)
	    || !isCircleExecutable(CIRCLE_DISCOUNTMNY, sChangedKey))
	  return;
	    
	//如果单价、净价数值相同，则折扣额强制清0,不需要进行折扣额的计算
    if (!processAfterCalDiscountMny())
      return;	
	
	String formulas[] = new String[] { m_strDiscountMnyKey + "->"
				+ m_strNumKey + "*" + m_strTaxPriceKey + "-" + m_strSummnyKey };
	execFormulas(formulas);
	// 设置该KEY已改变
	setKeysExecuted(CIRCLE_DISCOUNTMNY, true);
	// 设置执行标记
	setCircleExecuted(CIRCLE_DISCOUNTMNY, true);
	return;
  }

  /**
	 * 作者：王印芬 功能：主数量、辅数量、换算率CIRCLE中,根据一个具体改变的项计算其他项 参数：String sChangedKey
	 * 影响该CIRCLE的项 返回：无 例外：无 日期：(2002-5-15 11:39:21) 修改日期，修改人，修改原因，注释标志：
	 * 
	 */
  private void execCircle_ConvertRate(String sChangedKey) {

    // 已执行过或不可执行,则不再执行
    if (isCircleExecuted(CIRCLE_CONVERT_RATE)
        || !isCircleExecutable(CIRCLE_CONVERT_RATE, sChangedKey))
      return;

    boolean isNullAsZero = m_fp.isNullAsZero();
    //修改人：刘家清 修改时间：2008-12-22 上午11:25:19 修改原因：主、辅、换算率的换算关系修改成与库存一致。0是0，空是空。
    m_fp.setNullAsZero(false);
    /*
     * 主数量＝辅数量*换算率
     * 如果是固定换算率，换算率不能改变，则主数量改变会影响辅数量，辅数量改变也会影响主数量 
     * 如果是非固定换算，改变辅数量或换算率，主数量随之变化。改变主数量，显示的换算率随之变化。
     */
    // ////////////////主数量改变
    if (sChangedKey.equals(m_strNumKey)) {
      if (isFixedConvertRate()) {// 固定换算率
 
    	//计算主\辅单位
        if (!m_strConvertRateKey.equals("")
              && !isKeyExecuted(m_strAssistNumKey)) {
            // 执行公式
            String formulas[] = new String[] {
               m_strAssistNumKey + "->iif(null == "+ m_strNumKey + " || null == "+m_strConvertRateKey+", null, " + m_strNumKey + "/" + m_strConvertRateKey+ ")"
            };
            execFormulas(formulas);        
        // 设置该KEY已改变
        setKeysExecuted(CIRCLE_CONVERT_RATE, true);
        // 设置执行标记
        setCircleExecuted(CIRCLE_CONVERT_RATE, true);
        
        // 执行下一个CIRCLE
        execCircle_Qt_ConvertRate(m_strNumKey);//报价单位数量关系圈
        if (getPrior_Price_TaxPrice() == RelationsCalVO.PRICE_PRIOR_TO_TAXPRICE) {
          execCircle_NumNetPriceMoney(m_strNumKey);
          //execCircle_NumNetTaxPriceSummny(m_strNumKey);
        }
        else {
          execCircle_NumNetTaxPriceSummny(m_strNumKey);
          //execCircle_NumNetPriceMoney(m_strNumKey);
        }
        m_fp.setNullAsZero(isNullAsZero);
        return;
       }
      }
      else {// 非固定换算率
    	  
        //计算主\辅单位---换算率改变
        if (getUFDouble(m_strAssistNumKey) != null
            && !m_strConvertRateKey.equals("")
            && !isKeyExecuted(m_strConvertRateKey)) {
          // 执行公式
          String formulas[] = new String[] {
            m_strConvertRateKey + "->" + m_strNumKey + "/" + m_strAssistNumKey
          };
          execFormulas(formulas);
          
        // 设置该KEY已改变
        setKeysExecuted(CIRCLE_CONVERT_RATE, true);
        // 设置执行标记
        setCircleExecuted(CIRCLE_CONVERT_RATE, true);
        
        // 执行下一个CIRCLE
        execCircle_Qt_ConvertRate(m_strNumKey);//报价单位数量关系圈
        if (getPrior_Price_TaxPrice() == RelationsCalVO.PRICE_PRIOR_TO_TAXPRICE) {
          execCircle_NumNetPriceMoney(m_strNumKey);
          execCircle_NumNetTaxPriceSummny(m_strNumKey);
        }
        else {
          execCircle_NumNetTaxPriceSummny(m_strNumKey);
          execCircle_NumNetPriceMoney(m_strNumKey);
        }
        m_fp.setNullAsZero(isNullAsZero);
        return;
       }
        // 辅数量改变
        if (getUFDouble(m_strConvertRateKey) != null
            && !m_strAssistNumKey.equals("")
            && !isKeyExecuted(m_strAssistNumKey)) {
          // 执行公式
          String formulas[] = new String[] {
            m_strAssistNumKey + "->iif(null == "+ m_strNumKey + " || null == "+m_strConvertRateKey+", null, " + m_strNumKey + "/" + m_strConvertRateKey + ")"
          };
          execFormulas(formulas);
          // 设置该KEY已改变
          setKeysExecuted(CIRCLE_CONVERT_RATE, true);
          // 设置执行标记
          setCircleExecuted(CIRCLE_CONVERT_RATE, true);
          // 执行下一个CIRCLE
          execCircle_Qt_ConvertRate(m_strNumKey);//报价单位数量关系圈
          if (getPrior_Price_TaxPrice() == RelationsCalVO.PRICE_PRIOR_TO_TAXPRICE) {
            execCircle_NumNetPriceMoney(m_strNumKey);
            execCircle_NumNetTaxPriceSummny(m_strNumKey);
          }
          else {
            execCircle_NumNetTaxPriceSummny(m_strNumKey);
            execCircle_NumNetPriceMoney(m_strNumKey);
          }
          m_fp.setNullAsZero(isNullAsZero);
          return;
        }
      }
    }

    // //////////////辅数量改变
    if (sChangedKey.equals(m_strAssistNumKey)) {
      if (isFixedConvertRate()) {// 固定换算率
    	//1.计算主\辅单位
        if (getUFDouble(m_strConvertRateKey) != null && !m_strNumKey.equals("")
            && !isKeyExecuted(m_strNumKey)) {
          // 执行公式
          String formulas[] = new String[] {
            m_strNumKey + "->iif(null == "+ m_strAssistNumKey + " || null == "+m_strConvertRateKey+", null, "+ m_strAssistNumKey + "*" + m_strConvertRateKey+")"
          };
          execFormulas(formulas);
                
        // 设置该KEY已改变
        setKeysExecuted(CIRCLE_CONVERT_RATE, true);
        // 设置执行标记
        setCircleExecuted(CIRCLE_CONVERT_RATE, true);
        
        // 执行下一个CIRCLE
        execCircle_Qt_ConvertRate(m_strNumKey);//报价单位数量关系圈
        if (getPrior_Price_TaxPrice() == RelationsCalVO.PRICE_PRIOR_TO_TAXPRICE) {
          execCircle_NumNetPriceMoney(m_strNumKey);
          execCircle_NumNetTaxPriceSummny(m_strNumKey);
        }
        else {
          execCircle_NumNetTaxPriceSummny(m_strNumKey);
          execCircle_NumNetPriceMoney(m_strNumKey);
        }
        m_fp.setNullAsZero(isNullAsZero);
        return;
       }
      }
      //变动换算率
      else {
        //1.计算主\辅单位---换算率不变
        if (getUFDouble(m_strConvertRateKey) != null && !m_strNumKey.equals("")
            && !isKeyExecuted(m_strNumKey)) {
          // 执行公式
          String formulas[] = new String[] {
            m_strNumKey + "->iif(null == "+ m_strAssistNumKey + " || null == "+m_strConvertRateKey+", null, "+ m_strAssistNumKey + "*" + m_strConvertRateKey + ")"
          };
          execFormulas(formulas); 
        
        // 设置该KEY已改变
        setKeysExecuted(CIRCLE_CONVERT_RATE, true);
        // 设置执行标记
        setCircleExecuted(CIRCLE_CONVERT_RATE, true);
        // 执行下一个CIRCLE
        execCircle_Qt_ConvertRate(m_strNumKey);//报价单位数量关系圈
        if (getPrior_Price_TaxPrice() == RelationsCalVO.PRICE_PRIOR_TO_TAXPRICE) {
          execCircle_NumNetPriceMoney(m_strNumKey);
          execCircle_NumNetTaxPriceSummny(m_strNumKey);
        }
        else {
          execCircle_NumNetTaxPriceSummny(m_strNumKey);
          execCircle_NumNetPriceMoney(m_strNumKey);
        }
        m_fp.setNullAsZero(isNullAsZero);
        return;
       }
        /*// 主数量不变
        if (getUFDouble(m_strNumKey) != null && !m_strConvertRateKey.equals("")
            && !isKeyExecuted(m_strConvertRateKey)) {
          // 执行公式
          String formulas[] = new String[] {
            m_strConvertRateKey + "->" + m_strNumKey + "/" + m_strAssistNumKey
          };
          execFormulas(formulas);
          // 设置该KEY已改变
          setKeysExecuted(CIRCLE_CONVERT_RATE, true);
          // 设置执行标记
          setCircleExecuted(CIRCLE_CONVERT_RATE, true);
          return;
        }*/
      }
    }

    // //////////////换算率改变
    if (sChangedKey.equals(m_strConvertRateKey)) {

      // V5修改：德美，修改辅单位时，主数量不变，换算辅数量

      // //与是否固定换算率无关
      // //辅数量优于主数量
      // //辅数量不变
      // if( getUFDouble(m_strAssistNum)!=null && !m_strNum.equals("") &&
      // !isKeyExecuted(m_strNum) ){
      // //执行公式
      // String formulas[] = new String[]{m_strNum + "->" + m_strAssistNum + "*"
      // + m_strConvertRate } ;
      // execFormula(formulas) ;
      // //调试打印
      // debugPrint(formulas) ;
      // //设置该KEY已改变
      // setKeysExecuted(CIRCLE_CONVERT_RATE,true) ;
      // //设置执行标记
      // setCircleExecuted(CIRCLE_CONVERT_RATE,true) ;
      // //执行下一个CIRCLE
      // if(getPrior_Price_TaxPrice()==RelationsCalVO.PRICE_PRIOR_TO_TAXPRICE){
      // execCircle_NumNetPriceMoney(m_strNum) ;
      // execCircle_NumNetTaxPriceSummny(m_strNum) ;
      // }else{
      // execCircle_NumNetTaxPriceSummny(m_strNum) ;
      // execCircle_NumNetPriceMoney(m_strNum) ;
      // }
      // return ;
      // }
      //计算主\辅单位---主数量不变
      if (getUFDouble(m_strNumKey) != null && !m_strAssistNumKey.equals("")
          && !isKeyExecuted(m_strAssistNumKey)) {
        // 执行公式
        String formulas[] = new String[] {
          m_strAssistNumKey + "->" + m_strNumKey + "/" + m_strConvertRateKey
        };
        execFormulas(formulas);
      
      // 设置该KEY已改变
      setKeysExecuted(CIRCLE_CONVERT_RATE, true);
      // 设置执行标记
      setCircleExecuted(CIRCLE_CONVERT_RATE, true);
      m_fp.setNullAsZero(isNullAsZero);
      return;
     }
    }
  }
  
  /**
   * 18.主数量、报价数量、报价换算率
   */
   private void execCircle_Qt_ConvertRate(String sChangedKey){
	    // 已执行过或不可执行,则不再执行
	    if (isCircleExecuted(CIRCLE_QT_CONVERT_RATE)
	        || !isCircleExecutable(CIRCLE_QT_CONVERT_RATE, sChangedKey))
	      return;
	    
        //////////////主数量改变
		if (sChangedKey.equals(m_strNumKey)) {
			// 固定换算率
			if (isQt_FixedConvertRate()) {
				if (getUFDouble(m_strQt_ConvertRateKey) != null
						&& !isKeyExecuted(m_strQt_NumKey)) {
					// 执行公式
					String formulas[] = new String[] { m_strQt_NumKey + "->"
							+ m_strNumKey + "/" + m_strQt_ConvertRateKey };
					execFormulas(formulas);
					// 设置该KEY已改变
					setKeysExecuted(CIRCLE_QT_CONVERT_RATE, true);
					// 设置执行标记
					setCircleExecuted(CIRCLE_QT_CONVERT_RATE, true);
				}
			}
			// 变动换算率
			else{
				if (getUFDouble(m_strQt_NumKey) != null
						&& !isKeyExecuted(m_strQt_ConvertRateKey)) {
					// 执行公式
					String formulas[] = new String[] { m_strQt_ConvertRateKey + "->"
							+ m_strNumKey + "/" + m_strQt_NumKey };
					execFormulas(formulas);
					// 设置该KEY已改变
					setKeysExecuted(CIRCLE_QT_CONVERT_RATE, true);
					// 设置执行标记
					setCircleExecuted(CIRCLE_QT_CONVERT_RATE, true);
				}
			}
		}
		
		//////////////报价换算率改变
	    if (sChangedKey.equals(m_strQt_ConvertRateKey)) {
	    	if (getUFDouble(m_strQt_ConvertRateKey) != null 
	    	          && !isKeyExecuted(m_strQt_NumKey)) {
	    		// 执行公式
    	        String formulas[] = new String[] {
    	        	m_strQt_NumKey + "->" + m_strNumKey + "/" + m_strQt_ConvertRateKey
    	        };
    	        execFormulas(formulas);
    	      
    	        // 设置该KEY已改变
    	        setKeysExecuted(CIRCLE_QT_CONVERT_RATE, true);
    	        // 设置执行标记
    	        setCircleExecuted(CIRCLE_QT_CONVERT_RATE, true);
    	        //执行下一个CIRCLE
    	        if (getPrior_Price_TaxPrice() == RelationsCalVO.PRICE_PRIOR_TO_TAXPRICE) {
    	            execCircle_Qt_NumNetPriceMoney(m_strQt_NumKey);
    	        }
    	        else {
    	            execCircle_Qt_NumNetTaxPriceSummny(m_strQt_NumKey);
    	        }
    	        return;
	    	}
	    }
	   
	    //////////////报价数量改变
		if (sChangedKey.equals(m_strQt_NumKey)) {
			// 固定换算率
			if (isQt_FixedConvertRate()) {
				// 计算主\报单位
				if (getUFDouble(m_strQt_ConvertRateKey) != null
						&& !isKeyExecuted(m_strNumKey)) {
					// 执行公式
					String formulas[] = new String[] { m_strNumKey + "->"
							+ m_strQt_NumKey + "*" + m_strQt_ConvertRateKey };
					execFormulas(formulas);

					// 设置该KEY已改变
					setKeysExecuted(CIRCLE_QT_CONVERT_RATE, true);
					// 设置执行标记
					setCircleExecuted(CIRCLE_QT_CONVERT_RATE, true);
					// 执行下一个CIRCLE
					execCircle_ConvertRate(m_strNumKey);//主数量、辅数量、换算率
					if (getPrior_Price_TaxPrice() == RelationsCalVO.PRICE_PRIOR_TO_TAXPRICE) {
	    	            execCircle_Qt_NumNetPriceMoney(m_strQt_NumKey);
	    	            //execCircle_Qt_NumNetTaxPriceSummny(m_strQt_NumKey);
	    	        }
	    	        else {
	    	            execCircle_Qt_NumNetTaxPriceSummny(m_strQt_NumKey);
	    	            //execCircle_Qt_NumNetPriceMoney(m_strQt_NumKey);
	    	        }
					return;
				}
			}
			// 非固定换算率
			else {
				// 计算主\报单位---报价换算率改变
				if (getUFDouble(m_strQt_ConvertRateKey) != null
						&& !isKeyExecuted(m_strNumKey)) {
					// 执行公式
					String formulas[] = new String[] { m_strNumKey + "->"
							+ m_strQt_NumKey + "*" + m_strQt_ConvertRateKey };
					execFormulas(formulas);

					// 设置该KEY已改变
					setKeysExecuted(CIRCLE_QT_CONVERT_RATE, true);
					// 设置执行标记
					setCircleExecuted(CIRCLE_QT_CONVERT_RATE, true);
					// 执行下一个CIRCLE
					execCircle_ConvertRate(m_strNumKey);//主数量、辅数量、换算率
					if (getPrior_Price_TaxPrice() == RelationsCalVO.PRICE_PRIOR_TO_TAXPRICE) {
	    	            execCircle_Qt_NumNetPriceMoney(m_strQt_NumKey);
	    	            //execCircle_Qt_NumNetTaxPriceSummny(m_strQt_NumKey);
	    	        }
	    	        else {
	    	            execCircle_Qt_NumNetTaxPriceSummny(m_strQt_NumKey);
	    	            //execCircle_Qt_NumNetPriceMoney(m_strQt_NumKey);
	    	        }
					return;
				}
			}
		}
  }
  
  /**
	 * （14）单品折扣、报价原币含税净价、报价原币含税单价 2008-03-20 zhangcheng v5.3
	 */
  private void execCircle_Qt_TaxPriceNetTaxPrice(String sChangedKey,
	      boolean bExecFollowing) {

	    // 已执行过或不可执行,则不再执行
	    if (isCircleExecuted(CIRCLE_QT_TAXPRICE_NETTAXPRICE)
	        || !isCircleExecutable(CIRCLE_QT_TAXPRICE_NETTAXPRICE, sChangedKey)) {
	      return;
	    }
	    // /////////////////含税单价
	    if (sChangedKey.equals(m_strQt_TaxPriceKey)){
	    	// 含税净价 = 含税单价* 扣率
	        if (getUFDouble(m_strDiscountRateKey) != null
	            && !m_strQt_NetTaxPriceKey.equals("")
	            && !isKeyExecuted(m_strQt_NetTaxPriceKey)) {
	          // 执行公式
	          String formulas[] = getTaxPrice_NetTaxPrice_Discount( m_strQt_NetTaxPriceKey );
	          execFormulas(formulas);
	          // 设置该KEY已改变
	          setKeysExecuted(CIRCLE_QT_TAXPRICE_NETTAXPRICE, true);
	          // 设置执行标记
	          setCircleExecuted(CIRCLE_QT_TAXPRICE_NETTAXPRICE, true);
	          // 执行下一个CIRCLE
	          if (bExecFollowing) {
	        	execCircle_Qt_NetTaxPriceNetPrice(m_strQt_NetTaxPriceKey, false);
	            execCircle_Qt_NumNetTaxPriceSummny(m_strQt_NetTaxPriceKey);
	            execCircle_Qt_TaxPricePrice(m_strQt_TaxPriceKey, false);
	          }
	          return;
	        }
	    }
	    ///////////////////含税净价
	    else if (sChangedKey.equals(m_strQt_NetTaxPriceKey)){
	    	//调折扣(默认):当第一次改变的是报价计量单位的价格并且参数是调折扣时
	    	if ((getFirstChangedKey().equals(m_strQt_NetTaxPriceKey)
					|| getFirstChangedKey().equals(m_strQt_NetPriceKey)
					|| getFirstChangedKey().equals(m_strQt_TaxPriceKey) || getFirstChangedKey()
					.equals(m_strQt_PriceKey))
					&& m_iPrior_ItemDiscountRate_Price == RelationsCalVO.ITEMDISCOUNTRATE_PRIOR_TO_PRICE) {
	    		if (!isKeyExecuted(m_strDiscountRateKey)){
		    		// 执行公式：单品折扣 = 含税净价 / （含税单价 * 整单折扣）
		            String formulas[] = getTaxPrice_NetTaxPrice_Discount(m_strDiscountRateKey);
		            execFormulas(formulas);
		            // 设置该KEY已改变
		            setKeysExecuted(CIRCLE_QT_TAXPRICE_NETTAXPRICE, true);
		            // 设置执行标记
		            setCircleExecuted(CIRCLE_QT_TAXPRICE_NETTAXPRICE, true);
		            // 执行下一个CIRCLE
			        if (bExecFollowing) {
			        	execCircle_Qt_NetTaxPriceNetPrice(m_strQt_NetTaxPriceKey, false);
			            execCircle_Qt_NumNetTaxPriceSummny(m_strQt_NetTaxPriceKey);
			        }
		            return;
		    	}
	    	}
	    	//调单价：当第一次改变的是主计量单位的价格或者参数是调单价时
	    	else{
	    		if (!isKeyExecuted(m_strQt_TaxPriceKey)){
		    		// 执行公式：含税单价 = 含税净价 / （单品折扣 * 整单折扣）
		            String formulas[] = getTaxPrice_NetTaxPrice_Discount(m_strQt_TaxPriceKey);
		            execFormulas(formulas);
		            // 设置该KEY已改变
		            setKeysExecuted(CIRCLE_QT_TAXPRICE_NETTAXPRICE, true);
		            // 设置执行标记
		            setCircleExecuted(CIRCLE_QT_TAXPRICE_NETTAXPRICE, true);
		            // 执行下一个CIRCLE
			        if (bExecFollowing) {
			        	execCircle_Qt_NetTaxPriceNetPrice(m_strQt_NetTaxPriceKey, false);
			            execCircle_Qt_NumNetTaxPriceSummny(m_strQt_NetTaxPriceKey);
			            execCircle_Qt_TaxPricePrice(m_strQt_TaxPriceKey, false);
			        }
		            return;
		    	}
	    	}	
	    }
  }
  
  /**
   * （16）报价数量、报价原币含税净价、价税合计
   *  2008-03-20  zhangcheng v5.3 
   */
  private void execCircle_Qt_NumNetTaxPriceSummny(String sChangedKey) {

	    // 已执行过或不可执行,则不再执行
	    if (isCircleExecuted(CIRCLE_QT_NUM_NETTAXPRICE_SUMMNY)
	        || !isCircleExecutable(CIRCLE_QT_NUM_NETTAXPRICE_SUMMNY, sChangedKey)) {
	      return;
	    }
        ////////////////价税合计改变
	    if (sChangedKey.equals(m_strSummnyKey)){
	    	if (!isKeyExecuted(m_strQt_NetTaxPriceKey)){
	    		// 执行公式
	            String formulas[] = new String[] {
	            	m_strQt_NetTaxPriceKey + "->" + m_strSummnyKey + "/" + m_strQt_NumKey
	            };
	            execFormulas(formulas);
	            // 设置该KEY已改变
	            setKeysExecuted(CIRCLE_QT_NUM_NETTAXPRICE_SUMMNY, true);
	            // 设置执行标记
	            setCircleExecuted(CIRCLE_QT_NUM_NETTAXPRICE_SUMMNY, true);
	            //计算报价原币含税单价
	            execCircle_Qt_TaxPriceNetTaxPrice(m_strQt_NetTaxPriceKey, false);
	            //计算报价原币无税净价
	            execCircle_Qt_NetTaxPriceNetPrice(m_strQt_NetTaxPriceKey, false);
	            //计算报价原币无税单价
	            execCircle_Qt_TaxPricePrice(m_strQt_TaxPriceKey, false);
	            return;
	    	}
	    }
        ////////////////报价数量改变
	    if (sChangedKey.equals(m_strQt_NumKey)){
	    	// 价税合计 = 报价含税净价 * 报价数量
	    	if (getUFDouble(m_strQt_NetTaxPriceKey) != null &&!isKeyExecuted(m_strSummnyKey)){
	    		// 执行公式
	            String formulas[] = new String[] {
	            		m_strSummnyKey + "->" + m_strQt_NetTaxPriceKey + "*" + m_strQt_NumKey
	            };
	            execFormulas(formulas);
	            // 设置该KEY已改变
	            setKeysExecuted(CIRCLE_QT_NUM_NETTAXPRICE_SUMMNY, true);
	            // 设置执行标记
	            setCircleExecuted(CIRCLE_QT_NUM_NETTAXPRICE_SUMMNY, true);
	            execCircle_Qt_ConvertRate(m_strQt_NumKey);
	            execCircle_Taxrate(m_strSummnyKey);
	            return;
	    	}
	    	// 报价含税净价 = 价税合计 / 报价数量
	    	if (getUFDouble(m_strSummnyKey) != null &&!isKeyExecuted(m_strQt_NetTaxPriceKey)){
	    		// 执行公式
	            String formulas[] = new String[] {
	            	m_strQt_NetTaxPriceKey + "->" + m_strSummnyKey + "/" + m_strQt_NumKey
	            };
	            execFormulas(formulas);
	            // 设置该KEY已改变
	            setKeysExecuted(CIRCLE_QT_NUM_NETTAXPRICE_SUMMNY, true);
	            // 设置执行标记
	            setCircleExecuted(CIRCLE_QT_NUM_NETTAXPRICE_SUMMNY, true);
	            //报价单位数量关系圈
	            execCircle_Qt_ConvertRate(m_strQt_NumKey);
	            //计算报价原币含税单价
	            execCircle_Qt_TaxPriceNetTaxPrice(m_strQt_NetTaxPriceKey, false);
	            //计算报价原币无税净价
	            execCircle_Qt_NetTaxPriceNetPrice(m_strQt_NetTaxPriceKey, false);
	            //计算报价原币无税单价
	            execCircle_Qt_TaxPricePrice(m_strQt_TaxPriceKey, false);
	            return;
	    	}
	    }
	    ////////////////含税净价改变
	    else if (sChangedKey.equals(m_strQt_NetTaxPriceKey)) {
	      // 计算价税合计
	      if (getUFDouble(m_strQt_NumKey) != null && !m_strSummnyKey.equals("")
	          && !isKeyExecuted(m_strSummnyKey)) {
	        // 执行公式
	        String formulas[] = new String[] {
	          m_strSummnyKey + "->" + m_strQt_NumKey + "*" + m_strQt_NetTaxPriceKey
	        };
	        execFormulas(formulas);
	        // 设置该KEY已改变
	        setKeysExecuted(CIRCLE_QT_NUM_NETTAXPRICE_SUMMNY, true);
	        // 设置执行标记
	        setCircleExecuted(CIRCLE_QT_NUM_NETTAXPRICE_SUMMNY, true);
	        // 执行下一个CIRCLE
	        execCircle_Taxrate(m_strSummnyKey);
	        execCircle_Qt_TaxPriceNetTaxPrice(m_strQt_NetTaxPriceKey, false);
	        return;
	      }
	    }
  }
  
  /**
   * （17）报价数量、报价原币无税净价、无税金额
   *  2008-03-20  zhangcheng v5.3 
   */
  private void execCircle_Qt_NumNetPriceMoney(String sChangedKey) {

	    // 已执行过或不可执行,则不再执行
	    if (isCircleExecuted(CIRCLE_QT_NUM_NETPRICE_MONEY)
	        || !isCircleExecutable(CIRCLE_QT_NUM_NETPRICE_MONEY, sChangedKey)) {
	      return;
	    }
	    ////////////无税金额改变
	    if (sChangedKey.equals(m_strMoneyKey)){
	    	if (!isKeyExecuted(m_strQt_NetPriceKey)){
	    		// 执行公式
	            String formulas[] = new String[] {
	            	m_strQt_NetPriceKey + "->" + m_strMoneyKey + "/" + m_strQt_NumKey
	            };
	            execFormulas(formulas);
	            // 设置该KEY已改变
	            setKeysExecuted(CIRCLE_QT_NUM_NETPRICE_MONEY, true);
	            // 设置执行标记
	            setCircleExecuted(CIRCLE_QT_NUM_NETPRICE_MONEY, true);
	            //计算报价原币无税单价
	            execCircle_Qt_PriceNetPrice(m_strQt_NetPriceKey, false);
	            //计算报价原币含税净价
	            execCircle_Qt_NetTaxPriceNetPrice(m_strQt_NetPriceKey, false);
	            //计算报价原币含税单价
	            execCircle_Qt_TaxPricePrice(m_strQt_PriceKey, false);
	            return;
	    	}
	    }
        ////////////////报价数量改变
	    if (sChangedKey.equals(m_strQt_NumKey)){
	    	// 价税合计 = 报价无税净价 * 报价数量
	    	if (getUFDouble(m_strQt_NetPriceKey) != null &&!isKeyExecuted(m_strMoneyKey)){
	    		// 执行公式
	            String formulas[] = new String[] {
	            		m_strMoneyKey + "->" + m_strQt_NetPriceKey + "*" + m_strQt_NumKey
	            };
	            execFormulas(formulas);
	            // 设置该KEY已改变
	            setKeysExecuted(CIRCLE_QT_NUM_NETPRICE_MONEY, true);
	            // 设置执行标记
	            setCircleExecuted(CIRCLE_QT_NUM_NETPRICE_MONEY, true);
	            execCircle_Qt_ConvertRate(m_strQt_NumKey);
	            execCircle_Taxrate(m_strMoneyKey);
	            return;
	    	}
	    	// 报价含税净价 = 价税合计 / 报价数量
	    	if (getUFDouble(m_strMoneyKey) != null &&!isKeyExecuted(m_strQt_NetPriceKey)){
	    		// 执行公式
	            String formulas[] = new String[] {
	            	m_strQt_NetPriceKey + "->" + m_strMoneyKey + "/" + m_strQt_NumKey
	            };
	            execFormulas(formulas);
	            // 设置该KEY已改变
	            setKeysExecuted(CIRCLE_QT_NUM_NETPRICE_MONEY, true);
	            // 设置执行标记
	            setCircleExecuted(CIRCLE_QT_NUM_NETPRICE_MONEY, true);
	            //计算报价原币无税单价
	            execCircle_Qt_PriceNetPrice(m_strQt_NetPriceKey, false);
	            //计算报价原币含税净价
	            execCircle_Qt_NetTaxPriceNetPrice(m_strQt_NetPriceKey, false);
	            //计算报价原币含税单价
	            execCircle_Qt_TaxPricePrice(m_strQt_PriceKey, false);
	            return;
	    	}
	    }
	    ////////////无税净价改变
	    else if (sChangedKey.equals(m_strQt_NetPriceKey)){
	    	// 计算价税合计
		    if (getUFDouble(m_strQt_NumKey) != null && !m_strMoneyKey.equals("")
		          && !isKeyExecuted(m_strMoneyKey)) {
		        // 执行公式
		        String formulas[] = new String[] {
		        		m_strMoneyKey + "->" + m_strQt_NumKey + "*" + m_strQt_NetPriceKey
		        };
		        execFormulas(formulas);
		        // 设置该KEY已改变
		        setKeysExecuted(CIRCLE_QT_NUM_NETPRICE_MONEY, true);
		        // 设置执行标记
		        setCircleExecuted(CIRCLE_QT_NUM_NETPRICE_MONEY, true);
		        // 执行下一个CIRCLE
		        execCircle_Taxrate(m_strMoneyKey);
		        execCircle_Qt_TaxPriceNetTaxPrice(m_strQt_NetPriceKey, false);
		        return;
		    }
	    }
  }
  
  /**
   * （15）单品折扣、报价原币无税净价、报价原币无税单价
   *  2008-03-20  zhangcheng v5.3 
   */
  private void execCircle_Qt_PriceNetPrice(String sChangedKey,
	      boolean bExecFollowing) {

	    // 已执行过或不可执行,则不再执行
	    if (isCircleExecuted(CIRCLE_QT_PRICE_NETPRICE)
	        || !isCircleExecutable(CIRCLE_QT_PRICE_NETPRICE, sChangedKey)) {
	      return;
	    }
	    
	    ///////////////无税净价改变
	    if (sChangedKey.equals(m_strQt_NetPriceKey)){
	    	//调折扣(默认):当第一次改变的是报价计量单位的价格并且参数是调折扣时
	    	if ((getFirstChangedKey().equals(m_strQt_NetTaxPriceKey)
					|| getFirstChangedKey().equals(m_strQt_NetPriceKey)
					|| getFirstChangedKey().equals(m_strQt_TaxPriceKey) || getFirstChangedKey()
					.equals(m_strQt_PriceKey))
					&& m_iPrior_ItemDiscountRate_Price == RelationsCalVO.ITEMDISCOUNTRATE_PRIOR_TO_PRICE) {
	    		if (!isKeyExecuted(m_strQt_PriceKey)){
		    		// 执行公式
		            String formulas[] = getPrice_NetPrice_Discount(m_strDiscountRateKey);
		            execFormulas(formulas);
		            // 设置该KEY已改变
		            setKeysExecuted(CIRCLE_QT_PRICE_NETPRICE, true);
		            // 设置执行标记
		            setCircleExecuted(CIRCLE_QT_PRICE_NETPRICE, true);
		            // 执行下一个CIRCLE
			        if (bExecFollowing) {
			        	execCircle_Qt_NetTaxPriceNetPrice(m_strQt_NetPriceKey, false);//含税净价 = 无税净价 * (1+税率)
			        	execCircle_Qt_NumNetPriceMoney(m_strQt_NetPriceKey);
			        }
		            return;
		    	}	
	    	}
	    	//调单价
	    	else {
	    		if (!isKeyExecuted(m_strQt_PriceKey)){
		    		// 执行公式
		            String formulas[] = getPrice_NetPrice_Discount(m_strQt_PriceKey);
		            execFormulas(formulas);
		            // 设置该KEY已改变
		            setKeysExecuted(CIRCLE_QT_PRICE_NETPRICE, true);
		            // 设置执行标记
		            setCircleExecuted(CIRCLE_QT_PRICE_NETPRICE, true);
		            // 执行下一个CIRCLE
			        if (bExecFollowing) {
			        	execCircle_Qt_NetTaxPriceNetPrice(m_strQt_NetPriceKey, false);//含税净价 = 无税净价 * (1+税率)
			        	execCircle_Qt_NumNetPriceMoney(m_strQt_NetPriceKey);
			            execCircle_Qt_TaxPricePrice(m_strQt_PriceKey, false);//含税单价 = 无税单价 * (1+税率)
			        }
		            return;
		    	}
	    	}	    	
	    }
        ///////////////无税单价改变
	    else if(sChangedKey.equals(m_strQt_PriceKey)){
	    	// 无税净价 = 无税单价* 扣率
	        if (getUFDouble(m_strDiscountRateKey) != null
	            && !m_strQt_NetPriceKey.equals("")
	            && !isKeyExecuted(m_strQt_NetPriceKey)) {
	          // 执行公式
	          String formulas[] = getPrice_NetPrice_Discount( m_strQt_NetPriceKey );
	          execFormulas(formulas);
	          // 设置该KEY已改变
	          setKeysExecuted(CIRCLE_QT_PRICE_NETPRICE, true);
	          // 设置执行标记
	          setCircleExecuted(CIRCLE_QT_PRICE_NETPRICE, true);
	          // 执行下一个CIRCLE
	          if (bExecFollowing) {
	        	execCircle_Qt_NetTaxPriceNetPrice(m_strQt_NetPriceKey, false);//含税净价 = 无税净价 * (1+税率)
	        	execCircle_Qt_NumNetPriceMoney(m_strQt_NetPriceKey);
	            execCircle_Qt_TaxPricePrice(m_strQt_PriceKey, false);//含税单价 = 无税单价 * (1+税率)
	          }
	          return;
	        }
	    }
  }
  
  /**
   * （13）税率、报价原币含税净价、报价原币无税净价
   * 2008-03-20  zhangcheng v5.3 
   */
  private void execCircle_Qt_NetTaxPriceNetPrice(String sChangedKey,
      boolean bExecFollowing) {

    // 已执行过或不可执行,则不再执行
    if (isCircleExecuted(CIRCLE_QT_NETTAXPRICE_NETPRICE)
        || !isCircleExecutable(CIRCLE_QT_NETTAXPRICE_NETPRICE, sChangedKey))
      return;

    // ////////////////含税净价、税率改变
    if (sChangedKey.equals(m_strTaxRateKey) || sChangedKey.equals(m_strQt_NetTaxPriceKey)
    		|| sChangedKey.equals(m_strQt_NetPriceKey)) {
    	//含税优先
    	if (getPrior_Price_TaxPrice() == RelationsCalVO.TAXPRICE_PRIOR_TO_PRICE)
    	{
    		// 无税净价 = 含税净价/(1+税率)
    	    if (!isKeyExecuted(m_strQt_NetPriceKey)) {
    	        // 执行公式
    	        String formulas[] = new String[] {
    	          m_strQt_NetPriceKey+"->"+m_strQt_NetTaxPriceKey+"/(1.0+"+m_strTaxRateKey+"/100.0)"
    	        };
    	        execFormulas(formulas);
    	        // 设置该KEY已改变
    	        setKeysExecuted(CIRCLE_QT_NETTAXPRICE_NETPRICE, true);
    	        // 设置执行标记
    	        setCircleExecuted(CIRCLE_QT_NETTAXPRICE_NETPRICE, true);
    	        return;
    	    }
    	}
    	//无税优先
    	else{
    		// 含税净价 = 无税净价*(1+税率)
    	    if (!isKeyExecuted(m_strQt_NetTaxPriceKey)) {
    	        // 执行公式
    	        String formulas[] = new String[] {
    	          m_strQt_NetTaxPriceKey+"->"+m_strQt_NetPriceKey+"*(1.0+"+m_strTaxRateKey+"/100.0)"
    	        };
    	        execFormulas(formulas);
    	        // 设置该KEY已改变
    	        setKeysExecuted(CIRCLE_QT_NETTAXPRICE_NETPRICE, true);
    	        // 设置执行标记
    	        setCircleExecuted(CIRCLE_QT_NETTAXPRICE_NETPRICE, true);
    	        return;
    	    }
    	}
    }
  }

  /**
   * （12）税率、报价原币含税单价、报价原币无税单价
   * 2008-03-20  zhangcheng v5.3 
   */
  private void execCircle_Qt_TaxPricePrice(String sChangedKey,
      boolean bExecFollowing) {

    // 已执行过或不可执行,则不再执行
    if (isCircleExecuted(CIRCLE_QT_TAXPRICE_PRICE)
        || !isCircleExecutable(CIRCLE_QT_TAXPRICE_PRICE, sChangedKey))
      return;

    // ////////////////税率改、含税\无税单价变
    if (sChangedKey.equals(m_strTaxRateKey)|| sChangedKey.equals(m_strQt_TaxPriceKey)
    		|| sChangedKey.equals(m_strQt_PriceKey)) {
    	//含税优先
    	if (getPrior_Price_TaxPrice() == RelationsCalVO.TAXPRICE_PRIOR_TO_PRICE)
    	{
    		// 无税单价 = 含税单价/(1+税率)
    	    if (!isKeyExecuted(m_strQt_PriceKey)) {
    	        // 执行公式
    	        String formulas[] = new String[] {
    	          m_strQt_PriceKey+"->"+m_strQt_TaxPriceKey+"/(1.0+"+m_strTaxRateKey+"/100.0)"
    	        };
    	        execFormulas(formulas);
    	        // 设置该KEY已改变
    	        setKeysExecuted(CIRCLE_QT_TAXPRICE_PRICE, true);
    	        // 设置执行标记
    	        setCircleExecuted(CIRCLE_QT_TAXPRICE_PRICE, true);
    	        return;
    	    }
    	}
    	//无税优先
    	else{
    		// 含税单价 = 无税单价*(1+税率)
    	    if (!isKeyExecuted(m_strQt_TaxPriceKey)) {
    	        // 执行公式
    	        String formulas[] = new String[] {
    	          m_strQt_TaxPriceKey+"->"+m_strQt_PriceKey+"*(1.0+"+m_strTaxRateKey+"/100.0)"
    	        };
    	        execFormulas(formulas);
    	        // 设置该KEY已改变
    	        setKeysExecuted(CIRCLE_QT_TAXPRICE_PRICE, true);
    	        // 设置执行标记
    	        setCircleExecuted(CIRCLE_QT_TAXPRICE_PRICE, true);
    	        return;
    	    }
    	}
    }
  }  
  
  /**
   * （9）含税净价，无税净价，税率
   * 作者：王印芬 功能：税率、净含税单价、净单价CIRCLE中,根据一个具体改变的项计算其他项 参数：String sChangedKey
   * 影响该CIRCLE的项 返回：无 例外：无 日期：(2003-10-10 11:39:21) 修改日期，修改人，修改原因，注释标志：
   * 2008-03-07  zhangcheng v5.3 需求变更
   */
  private void execCircle_NetTaxPriceNetPrice(String sChangedKey,
      boolean bExecFollowing) {

    // 已执行过或不可执行,则不再执行
    if (isCircleExecuted(CIRCLE_NETTAXPRICE_NETPRICE)
        || !isCircleExecutable(CIRCLE_NETTAXPRICE_NETPRICE, sChangedKey))
      return;

    // ////////////////净单价改变
    if (sChangedKey.equals(m_strNetPriceKey)) {
      // 净含税单价 = FUNC(净单价、税率)
      if (!m_strTaxRateKey.equals("") && !isKeyExecuted(m_strNetTaxPriceKey)) {
        // 执行公式
        String formulas[] = new String[] {
          getNetTaxPrice_TaxrateNetPrice()
        };
        execFormulas(formulas);
        // 设置该KEY已改变
        setKeysExecuted(CIRCLE_NETTAXPRICE_NETPRICE, true);
        // 设置执行标记
        setCircleExecuted(CIRCLE_NETTAXPRICE_NETPRICE, true);
        // 执行下一个CIRCLE
        if (bExecFollowing) {
          execCircle_TaxPriceNetTaxPrice(m_strNetTaxPriceKey, false);
          execCircle_NumNetPriceMoney(m_strNetPriceKey);
          execCircle_PriceNetPrice(m_strNetPriceKey, false);
        }
        return;
      }
    }

    // ////////////含税净价 （净含税单价改变）
    if (sChangedKey.equals(m_strNetTaxPriceKey)) {
      // 净单价 = FUNC(净含税单价、税率)
      if (!m_strNetTaxPriceKey.equals("") && !isKeyExecuted(m_strNetPriceKey)) {
        // 执行公式
        String formulas[] = new String[] {
          getNetPrice_TaxrateNetTaxPrice()//根据扣税类别、税率、净含税单价 得到 净单价的计算公式
        };
        execFormulas(formulas);
        // 设置该KEY已改变
        setKeysExecuted(CIRCLE_NETTAXPRICE_NETPRICE, true);
        // 设置执行标记
        setCircleExecuted(CIRCLE_NETTAXPRICE_NETPRICE, true);
        // 执行下一个CIRCLE
        if (bExecFollowing) {
          execCircle_PriceNetPrice(m_strNetPriceKey, false);
          execCircle_NumNetTaxPriceSummny(m_strNetTaxPriceKey);
          execCircle_TaxPriceNetTaxPrice(m_strNetTaxPriceKey, false);
        }
        return;
      }
    }

    // ////////////税率改变
    if (sChangedKey.equals(m_strTaxRateKey)) {
      // 无税优先
      if (getPrior_Price_TaxPrice() == RelationsCalVO.PRICE_PRIOR_TO_TAXPRICE) {
        // 净含税单价 = FUNC(净单价、税率)
        if (!m_strNetTaxPriceKey.equals("") && !m_strNetPriceKey.equals("")
            && getUFDouble(m_strNetPriceKey) != null
            && !isKeyExecuted(m_strNetTaxPriceKey)) {
          // 执行公式
          String formulas[] = new String[] {
            getNetTaxPrice_TaxrateNetPrice()
          };
          execFormulas(formulas);
          // 设置该KEY已改变
          setKeysExecuted(CIRCLE_NETTAXPRICE_NETPRICE, true);
          // 设置执行标记
          setCircleExecuted(CIRCLE_NETTAXPRICE_NETPRICE, true);
          // 执行下一个CIRCLE
          // if (bExecFollowing) {
          // }
          return;
        }
        else {
          // 净单价 = FUNC(净含税单价、税率)
          if (!m_strNetTaxPriceKey.equals("") && !m_strNetPriceKey.equals("")
              && !isKeyExecuted(m_strNetPriceKey)) {
            // 执行公式
            String formulas[] = new String[] {
              getNetPrice_TaxrateNetTaxPrice()
            };
            execFormulas(formulas);
            // 设置该KEY已改变
            setKeysExecuted(CIRCLE_NETTAXPRICE_NETPRICE, true);
            // 设置执行标记
            setCircleExecuted(CIRCLE_NETTAXPRICE_NETPRICE, true);
            // 执行下一个CIRCLE
            // if (bExecFollowing) {
            // }
            return;
          }
        }
      }
      // 含税优先
      else {
        // 净单价 = FUNC(净含税单价、税率)
        if (!m_strNetTaxPriceKey.equals("") && !m_strNetPriceKey.equals("")
            && getUFDouble(m_strNetTaxPriceKey) != null
            && !isKeyExecuted(m_strNetPriceKey)) {
          // 执行公式
          String formulas[] = new String[] {
            getNetPrice_TaxrateNetTaxPrice()
          };
          execFormulas(formulas);
          // 设置该KEY已改变
          setKeysExecuted(CIRCLE_NETTAXPRICE_NETPRICE, true);
          // 设置执行标记
          setCircleExecuted(CIRCLE_NETTAXPRICE_NETPRICE, true);
          // 执行下一个CIRCLE
          // if (bExecFollowing) {
          // }
          return;
        }
        else {
          // 净含税单价 = FUNC(净单价、税率)
          if (!m_strNetTaxPriceKey.equals("") && !m_strNetPriceKey.equals("")
              && !isKeyExecuted(m_strNetTaxPriceKey)) {
            // 执行公式
            String formulas[] = new String[] {
              getNetPrice_TaxrateNetTaxPrice()
            };
            execFormulas(formulas);
            // 设置该KEY已改变
            setKeysExecuted(CIRCLE_NETTAXPRICE_NETPRICE, true);
            // 设置执行标记
            setCircleExecuted(CIRCLE_NETTAXPRICE_NETPRICE, true);
            // 执行下一个CIRCLE
            // if (bExecFollowing) {
            // }
            return;
          }
        }
      }
    }
  }

  /**
   * 作者：王印芬 功能：数量、净单价、金额CIRCLE中,根据一个具体改变的项计算其他项 参数：String sChangedKey
   * 影响该CIRCLE的项 返回：无 例外：无 日期：(2002-5-15 11:39:21) 修改日期，修改人，修改原因，注释标志：
   * 2002-05-16 wyf 加入优先控制
   */
  private void execCircle_NumNetPriceMoney(String sChangedKey) {

    // 已执行过或不可执行,则不再执行
    if (isCircleExecuted(CIRCLE_NUM_NETPRICE_MONEY)
        || !isCircleExecutable(CIRCLE_NUM_NETPRICE_MONEY, sChangedKey))
      return;

    // ////////////////数量改变
    if (sChangedKey.equals(m_strNumKey)) {
      // 价格优于金额
      if (getUFDouble(m_strNetPriceKey) != null && !m_strMoneyKey.equals("")
          && !isKeyExecuted(m_strMoneyKey)) {
        // 执行公式
        String formulas[] = new String[] {
          m_strMoneyKey + "->" + m_strNetPriceKey + "*" + m_strNumKey
        };
        execFormulas(formulas);
        // 设置该KEY已改变
        setKeysExecuted(CIRCLE_NUM_NETPRICE_MONEY, true);
        // 设置执行标记
        setCircleExecuted(CIRCLE_NUM_NETPRICE_MONEY, true);
        // 执行下一个CIRCLE
        execCircle_Taxrate(m_strMoneyKey);
        execCircle_QualifiedNum(m_strNumKey);
        execCircle_ConvertRate(m_strNumKey);
        execCircle_Qt_ConvertRate(m_strNumKey);//报价单位数量关系圈
        return;
      }

      if (getUFDouble(m_strMoneyKey) != null && !m_strNetPriceKey.equals("")
          && !isKeyExecuted(m_strNetPriceKey)) {
        // 执行公式
        String formulas[] = new String[] {
          m_strNetPriceKey + "->" + m_strMoneyKey + "/" + m_strNumKey
        };
        execFormulas(formulas);
        // 设置该KEY已改变
        setKeysExecuted(CIRCLE_NUM_NETPRICE_MONEY, true);
        // 设置执行标记
        setCircleExecuted(CIRCLE_NUM_NETPRICE_MONEY, true);
        // 执行下一个CIRCLE
        execCircle_PriceNetPrice(m_strNetPriceKey, false);
        execCircle_NetTaxPriceNetPrice(m_strNetPriceKey, false);
        execCircle_QualifiedNum(m_strNumKey);
        execCircle_ConvertRate(m_strNumKey);
        execCircle_NumNetTaxPriceSummny(m_strNumKey);
        return;
      }
    }

    // //////////////净单价改变
    if (sChangedKey.equals(m_strNetPriceKey)) {
      // 数量优于金额
      if (getUFDouble(m_strNumKey) != null && !m_strMoneyKey.equals("")
          && !isKeyExecuted(m_strMoneyKey)) {
        // 执行公式
        String formulas[] = new String[] {
          m_strMoneyKey + "->" + m_strNumKey + "*" + m_strNetPriceKey
        };
        execFormulas(formulas);
        // 设置该KEY已改变
        setKeysExecuted(CIRCLE_NUM_NETPRICE_MONEY, true);
        // 设置执行标记
        setCircleExecuted(CIRCLE_NUM_NETPRICE_MONEY, true);
        // 执行下一个CIRCLE
        execCircle_Taxrate(m_strMoneyKey);
        execCircle_PriceNetPrice(m_strNetPriceKey, false);
        execCircle_NetTaxPriceNetPrice(m_strNetPriceKey, false);
        return;
      }

      if (getUFDouble(m_strMoneyKey) != null && !m_strNumKey.equals("")
          && !isKeyExecuted(m_strNumKey)) {
        // 执行公式
        String formulas[] = new String[] {
          m_strNumKey + "->" + m_strMoneyKey + "/" + m_strNetPriceKey
        };
        execFormulas(formulas);
        // 设置该KEY已改变
        setKeysExecuted(CIRCLE_NUM_NETPRICE_MONEY, true);
        // 设置执行标记
        setCircleExecuted(CIRCLE_NUM_NETPRICE_MONEY, true);
        // 执行下一个CIRCLE
        execCircle_QualifiedNum(m_strNumKey);
        execCircle_ConvertRate(m_strNumKey);
        execCircle_NumNetTaxPriceSummny(m_strNumKey);
        execCircle_PriceNetPrice(m_strNetPriceKey, false);
        return;
      }
    }

    // //////////////金额改变
    if (sChangedKey.equals(m_strMoneyKey)) {
      // 只有当输入的项是金额时，单价由此算出，否则不由此公式算出
      if (getFirstChangedKey().equals(m_strMoneyKey)) {
        // 数量优于净单价
        if (getUFDouble(m_strNumKey) != null && !m_strNetPriceKey.equals("")
            && !isKeyExecuted(m_strNetPriceKey)) {
          // 执行公式
          String formulas[] = new String[] {
            m_strNetPriceKey + "->" + m_strMoneyKey + "/" + m_strNumKey
          };
          execFormulas(formulas);
          // 设置该KEY已改变
          setKeysExecuted(CIRCLE_NUM_NETPRICE_MONEY, true);
          // 设置执行标记
          setCircleExecuted(CIRCLE_NUM_NETPRICE_MONEY, true);
          // 执行下一个CIRCLE
          execCircle_NetTaxPriceNetPrice(m_strNetPriceKey, false);
          execCircle_PriceNetPrice(m_strNetPriceKey, false);
          execCircle_TaxPricePrice(m_strPriceKey, false);
          execCircle_Taxrate(m_strMoneyKey);
          return;
        }
      }

      if (getUFDouble(m_strNetPriceKey) != null && !m_strNumKey.equals("")
          && !isKeyExecuted(m_strNumKey)) {
        // 执行公式
        String formulas[] = new String[] {
          m_strNumKey + "->" + m_strMoneyKey + "/" + m_strNetPriceKey
        };
        execFormulas(formulas);
        // 设置该KEY已改变
        setKeysExecuted(CIRCLE_NUM_NETPRICE_MONEY, true);
        // 设置执行标记
        setCircleExecuted(CIRCLE_NUM_NETPRICE_MONEY, true);
        // 执行下一个CIRCLE
        execCircle_QualifiedNum(m_strNumKey);
        execCircle_ConvertRate(m_strNumKey);
        execCircle_Taxrate(m_strMoneyKey);
        return;
      }
    }
  }

  /**
   * （4）数量、含税净价、价税合计
   * 作者：王印芬 功能：执行数量、净含税单价、价税合计CIECLE 参数：String sChangedKey 入口KEY 返回：无 例外：无
   * 日期：(2001-06-09 11:22:51) 修改日期，修改人，修改原因，注释标志：
   */
  private void execCircle_NumNetTaxPriceSummny(String sChangedKey) {

    // 已执行过或不可执行,则不再执行
    if (isCircleExecuted(CIRCLE_NUM_NETTAXPRICE_SUMMNY)
        || !isCircleExecutable(CIRCLE_NUM_NETTAXPRICE_SUMMNY, sChangedKey))
      return;

    // ///////////////数量改变
    if (sChangedKey.equals(m_strNumKey)) {
      // //////净含税单价优于价税合计

      // 价税合计 = 数量 * 净含税单价
      if (getUFDouble(m_strNetTaxPriceKey) != null
          && !m_strSummnyKey.equals("") && !isKeyExecuted(m_strSummnyKey)) {
        // 执行公式
        String formulas[] = new String[] {
          m_strSummnyKey + "->" + m_strNumKey + "*" + m_strNetTaxPriceKey
        };
        execFormulas(formulas);
        // 设置该KEY已改变
        setKeysExecuted(CIRCLE_NUM_NETTAXPRICE_SUMMNY, true);
        // 设置执行标记
        setCircleExecuted(CIRCLE_NUM_NETTAXPRICE_SUMMNY, true);
        // 执行下一个CIRCLE
        execCircle_Taxrate(m_strSummnyKey);
        execCircle_NumNetTaxPriceSummny(m_strNetTaxPriceKey);
        execCircle_ConvertRate(m_strNumKey);
        execCircle_Qt_ConvertRate(m_strNumKey);//报价单位数量关系圈
        execCircle_NumNetPriceMoney(m_strNumKey);
        return;
      }

      // 净含税单价 = 价税合计 / 数量
      if (getUFDouble(m_strSummnyKey) != null
          && !m_strNetTaxPriceKey.equals("")
          && !isKeyExecuted(m_strNetTaxPriceKey)) {
        // 执行公式
        String formulas[] = new String[] {
          m_strNetTaxPriceKey + "->" + m_strSummnyKey + "/" + m_strNumKey
        };
        execFormulas(formulas);
        // 设置该KEY已改变
        setKeysExecuted(CIRCLE_NUM_NETTAXPRICE_SUMMNY, true);
        // 设置执行标记
        setCircleExecuted(CIRCLE_NUM_NETTAXPRICE_SUMMNY, true);
        // 执行下一个CIRCLE
        execCircle_TaxPriceNetTaxPrice(m_strNetTaxPriceKey, false);
        execCircle_ConvertRate(m_strNumKey);
        execCircle_Qt_ConvertRate(m_strNumKey);//报价单位数量关系圈
        execCircle_NumNetPriceMoney(m_strNumKey);
        return;
      }

    }
    // ///////////////价税合计改变
    if (sChangedKey.equals(m_strSummnyKey)) {
      // //////数量优于净含税单价

      /**此项判断是多余的,并且存在bug**/
      // 只有当首先变化的项是(价税合计或税额)时，含税净价由此算出，否则不由此公式算出
      /*if (getFirstChangedKey().equals(m_strSummnyKey)
          || getFirstChangedKey().equals(m_strTaxKey)) {*/
      /************************************************/
    	
      // 含税净价 = 价税合计 / 数量
      if (getUFDouble(m_strNumKey) != null && !m_strNetTaxPriceKey.equals("")
            && !isKeyExecuted(m_strNetTaxPriceKey)) {
          // 执行公式
          String formulas[] = new String[] {
        //add by ouyangzhb 2011-05-04 当回冲应付单时取单价的绝对值
            m_strNetTaxPriceKey + "->abs(" + m_strSummnyKey + "/" + m_strNumKey+")"
          };
          execFormulas(formulas);
          // 设置该KEY已改变
          setKeysExecuted(CIRCLE_NUM_NETTAXPRICE_SUMMNY, true);
          // 设置执行标记
          setCircleExecuted(CIRCLE_NUM_NETTAXPRICE_SUMMNY, true);
          // 执行下一个CIRCLE
          execCircle_NetTaxPriceNetPrice(m_strNetTaxPriceKey, false);
          execCircle_TaxPriceNetTaxPrice(m_strNetTaxPriceKey, false);
          execCircle_TaxPricePrice(m_strTaxPriceKey, false);
          execCircle_Taxrate(m_strSummnyKey);
          return;
      }

      // 数量 = 价税合计 / 净含税单价
      if (getUFDouble(m_strNetTaxPriceKey) != null && !m_strNumKey.equals("")
          && !isKeyExecuted(m_strNumKey)) {
        // 执行公式
        String formulas[] = new String[] {
          m_strNumKey + "->" + m_strSummnyKey + "/" + m_strNetTaxPriceKey
        };
        execFormulas(formulas);
        // 设置该KEY已改变
        setKeysExecuted(CIRCLE_NUM_NETTAXPRICE_SUMMNY, true);
        // 设置执行标记
        setCircleExecuted(CIRCLE_NUM_NETTAXPRICE_SUMMNY, true);
        // 执行下一个CIRCLE
        execCircle_NumNetPriceMoney(m_strNumKey);
        execCircle_QualifiedNum(m_strNumKey);
        execCircle_Taxrate(m_strSummnyKey);
        execCircle_ConvertRate(m_strNumKey);
        return;
      }
    }

    // ////////////////含税净价改变
    if (sChangedKey.equals(m_strNetTaxPriceKey)) {
      // 数量优于价税合计
      if (getUFDouble(m_strNumKey) != null && !m_strSummnyKey.equals("")
          && !isKeyExecuted(m_strSummnyKey)) {
        // 执行公式
        String formulas[] = new String[] {
          m_strSummnyKey + "->" + m_strNumKey + "*" + m_strNetTaxPriceKey
        };
        execFormulas(formulas);
        // 设置该KEY已改变
        setKeysExecuted(CIRCLE_NUM_NETTAXPRICE_SUMMNY, true);
        // 设置执行标记
        setCircleExecuted(CIRCLE_NUM_NETTAXPRICE_SUMMNY, true);
        // 执行下一个CIRCLE
        execCircle_Taxrate(m_strSummnyKey);
        execCircle_TaxPriceNetTaxPrice(m_strNetTaxPriceKey, false);
        return;
      }

      if (getUFDouble(m_strSummnyKey) != null && !m_strNumKey.equals("")
          && !isKeyExecuted(m_strNumKey)) {
        // 执行公式
        String formulas[] = new String[] {
          m_strNumKey + "->" + m_strSummnyKey + "/" + m_strNetTaxPriceKey
        };
        execFormulas(formulas);
        // 设置该KEY已改变
        setKeysExecuted(CIRCLE_NUM_NETTAXPRICE_SUMMNY, true);
        // 设置执行标记
        setCircleExecuted(CIRCLE_NUM_NETTAXPRICE_SUMMNY, true);
        // 执行下一个CIRCLE
        execCircle_NumNetPriceMoney(m_strNumKey);
        execCircle_QualifiedNum(m_strNumKey);
        execCircle_TaxPriceNetTaxPrice(m_strNetTaxPriceKey, false);
        execCircle_ConvertRate(m_strNumKey);
        return;
      }
    }

  }

  /**
   * （6）无税单价、无税净价、单品折扣
   * 作者：王印芬 功能：执行 单价、扣率、净单价CIRCLE 参数：String sChangedKey 影响该CIRCLE的项 boolean
   * bExecFollowing 是否执行其后的CIRCLE 返回：无 例外：无 日期：(2002-5-15 11:39:21)
   * 修改日期，修改人，修改原因，注释标志： 2002-05-16 wyf 加入优先控制
   * 2008-03-07  zhangcheng v5.3 需求变更
   */
  private void execCircle_PriceNetPrice(String sChangedKey,
      boolean bExecFollowing) {

    // 已执行过或不可执行,则不再执行
    if (isCircleExecuted(CIRCLE_PRICE_NETPRICE)
        || !isCircleExecutable(CIRCLE_PRICE_NETPRICE, sChangedKey))
      return;

    // ////////////////净单价改变
    if (sChangedKey.equals(m_strNetPriceKey)) {
      // 调折扣(默认):当第一次改变的是主计量单位的价格并且参数是调折扣时
      if (getUFDouble(m_strPriceKey) != null
          && !m_strDiscountRateKey.equals("")
          && !isKeyExecuted(m_strDiscountRateKey)
          && (getFirstChangedKey().equals(m_strTaxPriceKey)
          		  ||getFirstChangedKey().equals(m_strNetTaxPriceKey)
          		  ||getFirstChangedKey().equals(m_strNetPriceKey)
          		  ||getFirstChangedKey().equals(m_strPriceKey)
          		  ||getFirstChangedKey().equals(m_strMoneyKey))
          		  ) {
        // 按价格优先规则计算扣率
        if (getPrior_Price_TaxPrice() == RelationsCalVO.TAXPRICE_PRIOR_TO_PRICE) {
          // 含税单价优先 扣率=净含税单价/含税单价
          execCircle_NetTaxPriceNetPrice(m_strNetPriceKey, false);
          execCircle_TaxPriceNetTaxPrice(m_strNetTaxPriceKey, false);
        }
        else {
          // 执行公式
          /** v5.3 根据有无整单折扣选择公式 */	
          /*String formulas[] = new String[] {
            m_strDiscountRateKey + "->100.0*" + m_strNetPriceKey + "/"
                + m_strPriceKey
          };*/
          String formulas[] = getPrice_NetPrice_Discount( m_strDiscountRateKey );
          /*******************************************/
          
          execFormulas(formulas);
        }
        // 设置该KEY已改变
        setKeysExecuted(CIRCLE_PRICE_NETPRICE, true);
        // 设置执行标记
        setCircleExecuted(CIRCLE_PRICE_NETPRICE, true);
        // 执行下一个CIRCLE
        if (bExecFollowing) {
          // execCircle_TaxPriceNetTaxPrice(m_strDiscountRate,false) ;
          execCircle_NetTaxPriceNetPrice(m_strNetPriceKey, false);
          execCircle_NumNetPriceMoney(m_strNetPriceKey);
        }
        return;
      }

      //调单价:当第一次改变的非主计量单位的价格或者参数是调单价时 
      if (getUFDouble(m_strDiscountRateKey) != null
          && !m_strDiscountRateKey.equals("") && !isKeyExecuted(m_strPriceKey)) {
    	//单价= 净单价 / 扣率
        String formulas[] = getPrice_NetPrice_Discount( m_strPriceKey );      
        execFormulas(formulas);
        // 设置该KEY已改变
        setKeysExecuted(CIRCLE_PRICE_NETPRICE, true);
        // 设置执行标记
        setCircleExecuted(CIRCLE_PRICE_NETPRICE, true);
        // 执行下一个CIRCLE
        if (bExecFollowing) {
          execCircle_TaxPricePrice(m_strPriceKey, false);
          execCircle_NetTaxPriceNetPrice(m_strNetPriceKey, false);
          execCircle_NumNetPriceMoney(m_strNetPriceKey);
        }
        return;
      }
    }

    // ////////////单价改变
    if (sChangedKey.equals(m_strPriceKey)) {
      // ////////扣率 优于 净单价

      // 净单价 = 单价* 扣率
      if (getUFDouble(m_strDiscountRateKey) != null
          && !m_strNetPriceKey.equals("") && !isKeyExecuted(m_strNetPriceKey)) {
    	
    	/** v5.3 根据有无整单折扣选择公式 */  
        // 执行公式
       /* String formulas[] = new String[] {
          m_strNetPriceKey + "->" + m_strPriceKey + "*(" + m_strDiscountRateKey
              + "/100.0)"
        };*/
        String formulas[] = getPrice_NetPrice_Discount( m_strNetPriceKey );
        /*******************************************/
        
        execFormulas(formulas);
        // 设置该KEY已改变
        setKeysExecuted(CIRCLE_PRICE_NETPRICE, true);
        // 设置执行标记
        setCircleExecuted(CIRCLE_PRICE_NETPRICE, true);
        // 执行下一个CIRCLE
        if (bExecFollowing) {
          execCircle_NetTaxPriceNetPrice(m_strNetPriceKey, false);
          execCircle_NumNetPriceMoney(m_strNetPriceKey);
          execCircle_TaxPricePrice(m_strPriceKey, false);
        }
        return;
      }

      // 扣率 = 净单价 / 单价
      if (getUFDouble(m_strNetPriceKey) != null
          && !m_strDiscountRateKey.equals("")
          && !isKeyExecuted(m_strDiscountRateKey)) {
    	  
    	/** v5.3 根据有无整单折扣选择公式 */  
        // 执行公式
        /*String formulas[] = new String[] {
          m_strDiscountRateKey + "->100.0*" + m_strNetPriceKey + "/"
              + m_strPriceKey
        };*/
        String formulas[] = getPrice_NetPrice_Discount( m_strDiscountRateKey );
        /*******************************************/
        
        execFormulas(formulas);
        // 设置该KEY已改变
        setKeysExecuted(CIRCLE_PRICE_NETPRICE, true);
        // 设置执行标记
        setCircleExecuted(CIRCLE_PRICE_NETPRICE, true);
        // 执行下一个CIRCLE
        if (bExecFollowing) {
          execCircle_TaxPriceNetTaxPrice(m_strDiscountRateKey, false);
          execCircle_TaxPricePrice(m_strPriceKey, false);
        }
        return;
      }
    }

    // ////////////扣率改变
    if (sChangedKey.equals(m_strDiscountRateKey)||sChangedKey.equals(m_strAllDiscountRateKey)) {
      // //////单价优于 净单价
      // 净单价 = 单价* 扣率
      if (getUFDouble(m_strPriceKey) != null && !m_strNetPriceKey.equals("")
          && !isKeyExecuted(m_strNetPriceKey)) {
    	  
    	/** v5.3 根据有无整单折扣选择公式 */  
        // 执行公式
    	String formulas[] = getPrice_NetPrice_Discount( m_strNetPriceKey );
        /*******************************************/
    	
        execFormulas(formulas);
        // 设置该KEY已改变
        setKeysExecuted(CIRCLE_PRICE_NETPRICE, true);
        // 设置执行标记
        setCircleExecuted(CIRCLE_PRICE_NETPRICE, true);
        // 执行下一个CIRCLE
        if (bExecFollowing) {
          execCircle_NetTaxPriceNetPrice(m_strNetPriceKey, false);
          execCircle_NumNetPriceMoney(m_strNetPriceKey);
          //优先保证无税优先线的扣率关系，所以不再进入（7）含税单价、含税净价、单品折扣
          //execCircle_TaxPriceNetTaxPrice(m_strDiscountRateKey, false);
        }
        return;
      }

      // 单价 = 净单价 / 扣率
      if (getUFDouble(m_strDiscountRateKey) != null
          && !m_strDiscountRateKey.equals("") && !isKeyExecuted(m_strPriceKey)) {
    	  
    	/** v5.3 根据有无整单折扣选择公式 */  
        // 执行公式
       /* String formulas[] = new String[] {
          m_strPriceKey + "->100.0*" + m_strNetPriceKey + "/"
              + m_strDiscountRateKey
        };*/
        String formulas[] = getPrice_NetPrice_Discount( m_strPriceKey );
        /*******************************************/
        
        execFormulas(formulas);
        // 设置该KEY已改变
        setKeysExecuted(CIRCLE_PRICE_NETPRICE, true);
        // 设置执行标记
        setCircleExecuted(CIRCLE_PRICE_NETPRICE, true);
        // 执行下一个CIRCLE
        if (bExecFollowing) {
        }
        return;
      }
    }

  }

  /**
   * 作者：王印芬 功能：合格+不合格数量=数量CIRCLE中,根据一个具体改变的项计算其他项 参数：String sChangedKey
   * 影响该CIRCLE的项 返回：无 例外：无 日期：(2002-5-15 11:39:21) 修改日期，修改人，修改原因，注释标志：
   */
  private void execCircle_QualifiedNum(String sChangedKey) {

    // 已执行过或不可执行,则不再执行
    if (isCircleExecuted(CIRCLE_QUALIFIED_NUM)
        || !isCircleExecutable(CIRCLE_QUALIFIED_NUM, sChangedKey))
      return;

    // ////////////////数量改变
    if (sChangedKey.equals(m_strNumKey)) {
      // 合格优于不合格
      if (getUFDouble(m_strQualifiedNumKey) != null
          && !m_strUnQualifiedNumKey.equals("")
          && !isKeyExecuted(m_strQualifiedNumKey)
          && !isKeyExecuted(m_strUnQualifiedNumKey)) {
        // 合格数量大于到货数量,则合格数量=到货数量
        if (getUFDouble(m_strQualifiedNumKey).compareTo(
            getUFDouble(m_strNumKey)) >= 0) {
          // m_pnlBill.setBodyValueAt(getUFDouble(m_strNumKey),m_iPos,m_strQualifiedNumKey)
          // ;
          m_voCurr.setAttributeValue(m_strQualifiedNumKey,
              getUFDouble(m_strNumKey));
        }
        // 执行公式
        String formulas[] = null;
        if (getUFDouble(m_strNumKey).equals("")) {
          formulas = new String[] {
              m_strQualifiedNumKey + "->" + m_strNumKey + "*0.0",
              m_strUnQualifiedNumKey + "->" + m_strNumKey + "*0.0"
          };
        }
        else {
          formulas = new String[] {
            m_strUnQualifiedNumKey + "->" + m_strNumKey + "-"
                + m_strQualifiedNumKey
          };
        }
        execFormulas(formulas);
        // 设置该KEY已改变
        setKeysExecuted(CIRCLE_QUALIFIED_NUM, true);
        // 设置执行标记
        setCircleExecuted(CIRCLE_QUALIFIED_NUM, true);
        // 执行下一个CIECLE
        execCircle_ConvertRate(m_strNumKey);
        return;
      }

      if (getUFDouble(m_strUnQualifiedNumKey) != null
          && !m_strQualifiedNumKey.equals("")
          && !isKeyExecuted(m_strQualifiedNumKey)) {
        // 执行公式
        String formulas[] = new String[] {
          m_strQualifiedNumKey + "->" + m_strNumKey + "-"
              + m_strUnQualifiedNumKey
        };
        execFormulas(formulas);
        // 设置该KEY已改变
        setKeysExecuted(CIRCLE_QUALIFIED_NUM, true);
        // 设置执行标记
        setCircleExecuted(CIRCLE_QUALIFIED_NUM, true);
        // 执行下一个CIECLE
        execCircle_ConvertRate(m_strNumKey);
        return;
      }
    }

    // //////////////合格数量改变
    if (sChangedKey.equals(m_strQualifiedNumKey)) {
      // 数量优于不合格数量
      if (getUFDouble(m_strNumKey) != null
          && !m_strUnQualifiedNumKey.equals("")
          && !isKeyExecuted(m_strUnQualifiedNumKey)) {
        // 执行公式
        String formulas[] = new String[] {
          m_strUnQualifiedNumKey + "->" + m_strNumKey + "-"
              + m_strQualifiedNumKey
        };
        execFormulas(formulas);
        // 设置该KEY已改变
        setKeysExecuted(CIRCLE_QUALIFIED_NUM, true);
        // 设置执行标记
        setCircleExecuted(CIRCLE_QUALIFIED_NUM, true);
        return;
      }

      if (getUFDouble(m_strUnQualifiedNumKey) != null
          && !m_strNumKey.equals("") && !isKeyExecuted(m_strNumKey)) {
        // 执行公式
        String formulas[] = new String[] {
          m_strNumKey + "->" + m_strQualifiedNumKey + "+"
              + m_strUnQualifiedNumKey
        };
        execFormulas(formulas);
        // 设置该KEY已改变
        setKeysExecuted(CIRCLE_QUALIFIED_NUM, true);
        // 设置执行标记
        setCircleExecuted(CIRCLE_QUALIFIED_NUM, true);
        // 执行下一个CIRCLE
        execCircle_NumNetPriceMoney(m_strNumKey);
        execCircle_ConvertRate(m_strNumKey);
        return;
      }
    }

    // //////////////不合格数量改变
    if (sChangedKey.equals(m_strUnQualifiedNumKey)) {
      // 数量优于合格数量
      if (getUFDouble(m_strNumKey) != null && !m_strQualifiedNumKey.equals("")
          && !isKeyExecuted(m_strQualifiedNumKey)) {
        // 执行公式
        String formulas[] = new String[] {
          m_strQualifiedNumKey + "->" + m_strNumKey + "-"
              + m_strUnQualifiedNumKey
        };
        execFormulas(formulas);
        // 设置该KEY已改变
        setKeysExecuted(CIRCLE_QUALIFIED_NUM, true);
        // 设置执行标记
        setCircleExecuted(CIRCLE_QUALIFIED_NUM, true);
        return;
      }

      if (getUFDouble(m_strQualifiedNumKey) != null && !m_strNumKey.equals("")
          && !isKeyExecuted(m_strNumKey)) {
        // 执行公式
        String formulas[] = new String[] {
          m_strNumKey + "->" + m_strQualifiedNumKey + "+"
              + m_strUnQualifiedNumKey
        };
        execFormulas(formulas);
        // 设置该KEY已改变
        setKeysExecuted(CIRCLE_QUALIFIED_NUM, true);
        // 设置执行标记
        setCircleExecuted(CIRCLE_QUALIFIED_NUM, true);
        // 执行下一个CIRCLE
        execCircle_NumNetPriceMoney(m_strNumKey);
        execCircle_ConvertRate(m_strNumKey);
        return;
      }
    }
  }

  /**
   * （7）= 含税净价，含税单价，单品折扣
   * 作者：王印芬 功能：执行含税单价、扣率、净含税单价CIRCLE 参数：String sChangedKey 入口KEY boolean
   * bExecFollowing 是否执行其后的CIRCLE 返回：无 例外：无 日期：(2003-06-09 11:22:51)
   * 修改日期，修改人，修改原因，注释标志：2008-03-07  zhangcheng v5.3 需求变更
   */
  private void execCircle_TaxPriceNetTaxPrice(String sChangedKey,
      boolean bExecFollowing) {

    // 已执行过或不可执行,则不再执行
    if (isCircleExecuted(CIRCLE_TAXPRICE_NETTAXPRICE)
        || !isCircleExecutable(CIRCLE_TAXPRICE_NETTAXPRICE, sChangedKey)) {
      return;
    }

    // ////////////////(含税净价)净含税单价改变
    if (sChangedKey.equals(m_strNetTaxPriceKey)) {

      // 调折扣(默认):当第一次改变的是主计量单位的价格金额，并且参数是调折扣时
      if (getUFDouble(m_strTaxPriceKey) != null
          && !m_strDiscountRateKey.equals("")
          && !isKeyExecuted(m_strDiscountRateKey)
          && (getFirstChangedKey().equals(m_strTaxPriceKey)
        		  ||getFirstChangedKey().equals(m_strNetTaxPriceKey)
        		  ||getFirstChangedKey().equals(m_strNetPriceKey)
        		  ||getFirstChangedKey().equals(m_strPriceKey)
        		  ||getFirstChangedKey().equals(m_strSummnyKey))
          ) {
        // 执行公式
        // 按价格优先规则计算扣率
        if (getPrior_Price_TaxPrice() == RelationsCalVO.PRICE_PRIOR_TO_TAXPRICE) {
          // 无税单价优先 扣率=净单价/单价
          execCircle_NetTaxPriceNetPrice(m_strNetTaxPriceKey, false);
          execCircle_PriceNetPrice(m_strNetPriceKey, false);
        }
        else {
          /** v5.3 根据有无整单折扣选择公式 */	
          /*String formulas[] = new String[] {
            m_strDiscountRateKey + "->100.0*" + m_strNetTaxPriceKey + "/"
                + m_strTaxPriceKey
          };*/	
          String formulas[] = getTaxPrice_NetTaxPrice_Discount( m_strDiscountRateKey );
          /*****************************/
          
          execFormulas(formulas);
        }
        // 设置该KEY已改变
        setKeysExecuted(CIRCLE_TAXPRICE_NETTAXPRICE, true);
        // 设置执行标记
        setCircleExecuted(CIRCLE_TAXPRICE_NETTAXPRICE, true);
        // 执行下一个CIRCLE
        if (bExecFollowing) {
          // execCircle_PriceNetPrice(m_strDiscountRate,false) ;
          execCircle_NetTaxPriceNetPrice(m_strNetTaxPriceKey, false);
          execCircle_NumNetTaxPriceSummny(m_strNetTaxPriceKey);
        }
        return;
      }

      //调单价:当第一次改变的非主计量单位的价格或者参数是调单价时
      if (getUFDouble(m_strDiscountRateKey) != null
          && !m_strTaxPriceKey.equals("") && !isKeyExecuted(m_strTaxPriceKey)) {
    	// 执行公式：含税单价= 净含税单价 / 扣率
    	String formulas[] = getTaxPrice_NetTaxPrice_Discount( m_strTaxPriceKey );
        execFormulas(formulas);
        // 设置该KEY已改变
        setKeysExecuted(CIRCLE_TAXPRICE_NETTAXPRICE, true);
        // 设置执行标记
        setCircleExecuted(CIRCLE_TAXPRICE_NETTAXPRICE, true);
        // 执行下一个CIRCLE
        if (bExecFollowing) {
          execCircle_TaxPricePrice(m_strTaxPriceKey, false);
          execCircle_NetTaxPriceNetPrice(m_strNetTaxPriceKey, false);
          execCircle_NumNetTaxPriceSummny(m_strNetTaxPriceKey);
        }
        return;
      }
    }

    // ////////////含税单价改变
    if (sChangedKey.equals(m_strTaxPriceKey)) {
      // ////////扣率 优于 净含税单价

      // 净含税单价 = 含税单价* 扣率
      if (getUFDouble(m_strDiscountRateKey) != null
          && !m_strNetTaxPriceKey.equals("")
          && !isKeyExecuted(m_strNetTaxPriceKey)) {
        // 执行公式
    	/** v5.3 根据有无整单折扣选择公式 */
        /*String formulas[] = new String[] {
          m_strNetTaxPriceKey + "->" + m_strTaxPriceKey + "*("
              + m_strDiscountRateKey + "/100.0)"
        };*/
        String formulas[] = getTaxPrice_NetTaxPrice_Discount( m_strNetTaxPriceKey );
        /*****************************/
        
        execFormulas(formulas);
        // 设置该KEY已改变
        setKeysExecuted(CIRCLE_TAXPRICE_NETTAXPRICE, true);
        // 设置执行标记
        setCircleExecuted(CIRCLE_TAXPRICE_NETTAXPRICE, true);
        // 执行下一个CIRCLE
        if (bExecFollowing) {
          execCircle_NetTaxPriceNetPrice(m_strNetTaxPriceKey, false);
          execCircle_NumNetTaxPriceSummny(m_strNetTaxPriceKey);
          execCircle_TaxPricePrice(m_strTaxPriceKey, false);
        }
        return;
      }

      // 扣率 = 净含税单价 / 含税单价
      if (getUFDouble(m_strNetTaxPriceKey) != null
          && !m_strDiscountRateKey.equals("")
          && !isKeyExecuted(m_strDiscountRateKey)) {
        // 执行公式
    	/** v5.3 根据有无整单折扣选择公式 */
        /*String formulas[] = new String[] {
          m_strDiscountRateKey + "->100.0*" + m_strNetTaxPriceKey + "/"
              + m_strTaxPriceKey
        };*/
        String formulas[] = getTaxPrice_NetTaxPrice_Discount( m_strDiscountRateKey );
        /*****************************/
        
        execFormulas(formulas);
        // 设置该KEY已改变
        setKeysExecuted(CIRCLE_TAXPRICE_NETTAXPRICE, true);
        // 设置执行标记
        setCircleExecuted(CIRCLE_TAXPRICE_NETTAXPRICE, true);
        // 执行下一个CIRCLE
        if (bExecFollowing) {
          // execCircle_PriceNetPrice(m_strDiscountRate,false) ;
        }
        return;
      }
    }

    // ////////////扣率改变
    if (sChangedKey.equals(m_strDiscountRateKey)||sChangedKey.equals(m_strAllDiscountRateKey)) {
      // 默认为含税单价优先
      // 含税净价 = 含税单价* 单品折扣 * 整单折扣 (净含税单价 = 含税单价* 扣率) 
      if (getUFDouble(m_strTaxPriceKey) != null
          && !m_strNetTaxPriceKey.equals("")
          && !isKeyExecuted(m_strNetTaxPriceKey)) {
        // 执行公式:
    	/** v5.3 根据有无整单折扣选择公式 */
    	String formulas[] = getTaxPrice_NetTaxPrice_Discount( m_strNetTaxPriceKey );
        /*****************************/
    	
        execFormulas(formulas);
        // 设置该KEY已改变
        setKeysExecuted(CIRCLE_TAXPRICE_NETTAXPRICE, true);
        // 设置执行标记
        setCircleExecuted(CIRCLE_TAXPRICE_NETTAXPRICE, true);
        // 执行下一个CIRCLE
        if (bExecFollowing) {
          execCircle_NetTaxPriceNetPrice(m_strNetTaxPriceKey, false);
          execCircle_NumNetTaxPriceSummny(m_strNetTaxPriceKey);
          //优先保证含税优先线的扣率关系，所以不再进入（6）无税单价、无税净价、单品折扣
          //execCircle_PriceNetPrice(m_strDiscountRateKey,false) ;
        }
        return;
      }

      // 含税单价= 净含税单价 / 扣率
      if (getUFDouble(m_strNetTaxPriceKey) != null
          && !m_strTaxPriceKey.equals("") && !isKeyExecuted(m_strTaxPriceKey)) {
        // 执行公式
    	/** v5.3 根据有无整单折扣选择公式 */  
        /*String formulas[] = new String[] {
          m_strTaxPriceKey + "->" + m_strNetTaxPriceKey + "/("
              + m_strDiscountRateKey + "/100.0)"
        };*/
    	String formulas[] = getTaxPrice_NetTaxPrice_Discount( m_strTaxPriceKey );
        /*****************************/
        
        execFormulas(formulas);
        // 设置该KEY已改变
        setKeysExecuted(CIRCLE_TAXPRICE_NETTAXPRICE, true);
        // 设置执行标记
        setCircleExecuted(CIRCLE_TAXPRICE_NETTAXPRICE, true);
        // 执行下一个CIRCLE
        if (bExecFollowing) {
          execCircle_TaxPriceNetTaxPrice(m_strTaxPriceKey, false);
        }
        return;
      }
    }
  }

  /**
   * (8)含税单价,无税单价,税率
   * 作者：王印芬 功能：税率、含税单价、单价CIRCLE中,根据一个具体改变的项计算其他项 参数：String sChangedKey
   * 影响该CIRCLE的项 返回：无 例外：无 日期：(2003-10-10 11:39:21) 修改日期，修改人，修改原因，注释标志：
   */
  private void execCircle_TaxPricePrice(String sChangedKey,
      boolean bExecFollowing) {

    // 已执行过或不可执行,则不再执行
    if (isCircleExecuted(CIRCLE_TAXPRICE_PRICE)
        || !isCircleExecutable(CIRCLE_TAXPRICE_PRICE, sChangedKey))
      return;

    // ////////////////单价改变
    if (sChangedKey.equals(m_strPriceKey)) {
      // 含税单价 = FUNC(单价、税率)
      if (!m_strTaxRateKey.equals("") && !isKeyExecuted(m_strTaxPriceKey)) {
        // 执行公式
        String formulas[] = new String[] {
          getTaxPrice_TaxratePrice()
        };
        execFormulas(formulas);
        // 设置该KEY已改变
        setKeysExecuted(CIRCLE_TAXPRICE_PRICE, true);
        // 设置执行标记
        setCircleExecuted(CIRCLE_TAXPRICE_PRICE, true);
        // 执行下一个CIRCLE
        if (bExecFollowing) {
          execCircle_NumNetPriceMoney(m_strPriceKey);
          execCircle_PriceNetPrice(m_strPriceKey, false);
        }
        return;
      }
    }

    // ////////////含税单价改变
    if (sChangedKey.equals(m_strTaxPriceKey)) {
      // 单价 = FUNC(含税单价、税率)
      if (!m_strTaxPriceKey.equals("") && !isKeyExecuted(m_strPriceKey)) {
        // 执行公式
        String formulas[] = new String[] {
          getPrice_TaxrateTaxPrice()
        };
        execFormulas(formulas);
        // 设置该KEY已改变
        setKeysExecuted(CIRCLE_TAXPRICE_PRICE, true);
        // 设置执行标记
        setCircleExecuted(CIRCLE_TAXPRICE_PRICE, true);
        // 执行下一个CIRCLE
        if (bExecFollowing) {
          execCircle_NumNetTaxPriceSummny(m_strTaxPriceKey);
          execCircle_TaxPriceNetTaxPrice(m_strTaxPriceKey, false);
        }
        return;
      }
    }

    // ////////////税率改变
    if (sChangedKey.equals(m_strTaxRateKey)) {
      // 无税优先
      if (getPrior_Price_TaxPrice() == RelationsCalVO.PRICE_PRIOR_TO_TAXPRICE) {
        // 含税单价 = FUNC(单价、税率)
        if (!m_strTaxPriceKey.equals("") && !m_strPriceKey.equals("")
            && getUFDouble(m_strPriceKey) != null
            && !isKeyExecuted(m_strTaxPriceKey)) {
          // 执行公式
          String formulas[] = new String[] {
            getTaxPrice_TaxratePrice()
          };
          execFormulas(formulas);
          // 设置该KEY已改变
          setKeysExecuted(CIRCLE_TAXPRICE_PRICE, true);
          // 设置执行标记
          setCircleExecuted(CIRCLE_TAXPRICE_PRICE, true);
          // 执行下一个CIRCLE
          if (bExecFollowing) {
            execCircle_NetTaxPriceNetPrice(m_strNetPriceKey, true);
          }
          return;
        }
        else {
          // 单价 = FUNC(含税单价、税率)
          if (!m_strTaxPriceKey.equals("") && !m_strPriceKey.equals("")
              && !isKeyExecuted(m_strPriceKey)) {
            // 执行公式
            String formulas[] = new String[] {
              getPrice_TaxrateTaxPrice()
            };
            execFormulas(formulas);
            // 设置该KEY已改变
            setKeysExecuted(CIRCLE_TAXPRICE_PRICE, true);
            // 设置执行标记
            setCircleExecuted(CIRCLE_TAXPRICE_PRICE, true);
            // 执行下一个CIRCLE
            if (bExecFollowing) {
              execCircle_NetTaxPriceNetPrice(m_strNetTaxPriceKey, true);
            }
            return;
          }
        }
      }
      // 含税优先
      else {
        // 单价 = FUNC(含税单价、税率)
        if (!m_strTaxPriceKey.equals("") && !m_strPriceKey.equals("")
            && getUFDouble(m_strTaxPriceKey) != null
            && !isKeyExecuted(m_strPriceKey)) {
          // 执行公式
          String formulas[] = new String[] {
            getPrice_TaxrateTaxPrice()
          };
          execFormulas(formulas);
          // 设置该KEY已改变
          setKeysExecuted(CIRCLE_TAXPRICE_PRICE, true);
          // 设置执行标记
          setCircleExecuted(CIRCLE_TAXPRICE_PRICE, true);
          // 执行下一个CIRCLE
          if (bExecFollowing) {
            execCircle_NetTaxPriceNetPrice(m_strNetTaxPriceKey, true);
          }
          return;
        }
        else {
          // 含税单价 = FUNC(单价、税率)
          if (!m_strTaxPriceKey.equals("") && !m_strPriceKey.equals("")
              && !isKeyExecuted(m_strTaxPriceKey)) {
            // 执行公式
            String formulas[] = new String[] {
              getTaxPrice_TaxratePrice()
            };
            execFormulas(formulas);
            // 设置该KEY已改变
            setKeysExecuted(CIRCLE_TAXPRICE_PRICE, true);
            // 设置执行标记
            setCircleExecuted(CIRCLE_TAXPRICE_PRICE, true);
            // 执行下一个CIRCLE
            if (bExecFollowing) {
              execCircle_NetTaxPriceNetPrice(m_strNetPriceKey, true);
            }
            return;
          }
        }
      }
    }
  }

  /**
   * (19) 本币价税合计、本币无税金额、本币税额
   */
  private void execCircle_Local_Summny_Mny_Tax(String sChangedKey) {
	  
	    // 已执行过或不可执行,则不再执行
	    if (isCircleExecuted(CIRCLE_LOCAL_SUMMNY_MNY_TAX)
	        || !isCircleExecutable(CIRCLE_LOCAL_SUMMNY_MNY_TAX, sChangedKey))
	      return;
	    
	    //编辑本币价税合计
	    if (sChangedKey.equals(m_strSummny_LocalKey)) {
	    	//税率为0或为空
	    	if (getUFDouble(m_strTaxRateKey) == null
					|| getUFDouble(m_strTaxRateKey).doubleValue() == ZERO
							.doubleValue()) {
	    		m_voCurr.setAttributeValue(m_strMoney_LocalKey, m_voCurr
						.getAttributeValue(m_strSummny_LocalKey));
	    	}
	    	//税率!=0: 本币税额 = 本币价税合计 - 本币无税金额
	    	else{
	    		if (!isKeyExecuted(m_strTax_LocalKey)){
	    			String[] formulas = new String[] {
	    					m_strTax_LocalKey + "->" + m_strSummny_LocalKey + "-" + m_strMoney_LocalKey
	    		            };
	    			execFormulas(formulas);
	    		}
	    	}	
	    }
	    //编辑本币无税金额
	    else if (sChangedKey.equals(m_strMoney_LocalKey)){
            //税率为0或为空
	    	if (getUFDouble(m_strTaxRateKey) == null
					|| getUFDouble(m_strTaxRateKey).doubleValue() == ZERO
							.doubleValue()) {
	    		m_voCurr.setAttributeValue(m_strSummny_LocalKey, m_voCurr
						.getAttributeValue(m_strMoney_LocalKey));
	    	}
	    	//税率!=0: 本币税额 = 本币价税合计 - 本币无税金额
	    	else{
	    		if (!isKeyExecuted(m_strTax_LocalKey)){
	    			String[] formulas = new String[] {
	    					m_strTax_LocalKey + "->" + m_strSummny_LocalKey + "-" + m_strMoney_LocalKey
	    		            };
	    			execFormulas(formulas);
	    		}
	    	}	
	    }
	    
        //设置该KEY已改变
        setKeysExecuted(CIRCLE_LOCAL_SUMMNY_MNY_TAX, true);
        // 设置执行标记
        setCircleExecuted(CIRCLE_LOCAL_SUMMNY_MNY_TAX, true);
  }
  
  
  
  /**
   * （5）税率、价税合计、无税金额、税额
   * 作者：王印芬 功能：税率CIRCLE中,根据一个具体改变的项计算其他项 该函数中穷举了四项的所有变化组合。 参数：String sChangedKey
   * 影响该CIRCLE的项 返回：无 例外：无 日期：(2003-06-09 11:22:51) 
   * 修改日期  2008-03-07
   * 修改人    zhangcheng
   * 修改原因  v5.3需求变更
   * 注释标志：
   */
  private void execCircle_Taxrate(String sChangedKey) {

    // 已执行过或不可执行,则不再执行
    if (isCircleExecuted(CIRCLE_TAXRATE)
        || !isCircleExecutable(CIRCLE_TAXRATE, sChangedKey))
      return;

    // ////////////////税率改变
    if (sChangedKey.equals(m_strTaxRateKey)) {
      // 金额 优于 税额,价税合计

      // -----------单价优先
      if (getPrior_Price_TaxPrice() == RelationsCalVO.PRICE_PRIOR_TO_TAXPRICE) {
        // 金额,税率 ---> 税额,价税合计
        if (getUFDouble(m_strMoneyKey) != null && !m_strTaxKey.equals("")
            && !m_strSummnyKey.equals("") && !isKeyExecuted(m_strTaxKey)
            && !isKeyExecuted(m_strSummnyKey)) {
          // 执行公式
          String formulas[] = new String[] {
            getTax_TaxrateMoney()
          };
          execFormulas(formulas);
          // 执行公式后 税额为空时，不加进公式，以免破坏 金额+税额=价税合计 的关系
          // String formulas[] = null ;
          if (getUFDouble(m_strTaxKey) == null) {
            formulas = new String[] {
              m_strSummnyKey + "->" + m_strMoneyKey
            };
          }
          else {
            formulas = new String[] {
              m_strSummnyKey + "->" + m_strMoneyKey + "+" + m_strTaxKey
            };
          }
          execFormulas(formulas);
          // 设置该KEY已改变
          setKeysExecuted(CIRCLE_TAXRATE, true);
          // 设置执行标记
          setCircleExecuted(CIRCLE_TAXRATE, true);
          // 执行下一个CIRCLE
          {
            //execCircle_NumNetTaxPriceSummny(m_strSummnyKey);
            if (getPrior_Price_TaxPrice() == RelationsCalVO.TAXPRICE_PRIOR_TO_PRICE) {
              // 含税优先
              execCircle_NetTaxPriceNetPrice(m_strNetTaxPriceKey, false);
              execCircle_TaxPricePrice(m_strTaxPriceKey, false);
            }
            else {
              // 无税优先
              execCircle_NetTaxPriceNetPrice(m_strNetPriceKey, false);
              execCircle_TaxPricePrice(m_strPriceKey, false);
            }
          }
          return;
        }
      }
      // -----------含税单价优先
      else if (getPrior_Price_TaxPrice() == RelationsCalVO.TAXPRICE_PRIOR_TO_PRICE) {
        // 价税合计,税率 ---> 税额,金额
        if (getUFDouble(m_strSummnyKey) != null && !m_strTaxKey.equals("")
            && !m_strMoneyKey.equals("") && !isKeyExecuted(m_strTaxKey)
            && !isKeyExecuted(m_strMoneyKey)) {
          // 执行公式
          String formulas[] = new String[] {
            getTax_TaxrateSummny()
          };
          execFormulas(formulas);
          // 执行公式后 税额为空时，不加进公式，以免破坏 金额+税额=价税合计 的关系
          // String formulas[] = null ;
          if (getUFDouble(m_strTaxKey) == null) {
            formulas = new String[] {
              m_strMoneyKey + "->" + m_strSummnyKey
            };
          }
          else {
            formulas = new String[] {
              m_strMoneyKey + "->" + m_strSummnyKey + "-" + m_strTaxKey
            };
          }
          execFormulas(formulas);
          // 设置该KEY已改变
          setKeysExecuted(CIRCLE_TAXRATE, true);
          // 设置执行标记
          setCircleExecuted(CIRCLE_TAXRATE, true);
          // 执行下一个CIRCLE
          {
            //execCircle_NumNetPriceMoney(m_strMoneyKey);
            if (getPrior_Price_TaxPrice() == RelationsCalVO.TAXPRICE_PRIOR_TO_PRICE) {
              // 含税优先
              execCircle_NetTaxPriceNetPrice(m_strNetTaxPriceKey, false);
              execCircle_TaxPricePrice(m_strTaxPriceKey, false);
            }
            else {
              // 无税优先
              execCircle_NetTaxPriceNetPrice(m_strNetPriceKey, false);
              execCircle_TaxPricePrice(m_strPriceKey, false);
            }
          }
          return;
        }
      }
    }

    // ////////////////金额改变
    if (sChangedKey.equals(m_strMoneyKey)) {
      // 税率 优于 税额,价税合计

      // 税率不为零时:金额,税率 ---> 税额,价税合计
      if (!m_strTaxRateKey.equals("") && !m_strTaxKey.equals("")
          && !m_strSummnyKey.equals("") && !isKeyExecuted(m_strTaxKey)
          && !isKeyExecuted(m_strSummnyKey)) {
        // 先取得税额
        String formulas[] = new String[] {
          getTax_TaxrateMoney()
        };
        execFormulas(formulas);
        // 执行公式后 税额为空时，不加进公式，以免破坏 金额+税额=价税合计 的关系
        // String formulas[] = null ;
        if (getUFDouble(m_strTaxKey) == null) {
          formulas = new String[] {
            m_strSummnyKey + "->" + m_strMoneyKey
          };
        }
        else {
          formulas = new String[] {
            m_strSummnyKey + "->" + m_strMoneyKey + "+" + m_strTaxKey
          };
        }
        execFormulas(formulas);
       }
      // 设置该KEY已改变
      setKeysExecuted(CIRCLE_TAXRATE, true);
      // 设置执行标记
      setCircleExecuted(CIRCLE_TAXRATE, true);
      // 执行下一个CIRCLE
      execCircle_NumNetPriceMoney(m_strMoneyKey);
      execCircle_NumNetTaxPriceSummny(m_strSummnyKey);
      return; 
    }

    // ////////////////税额改变
    if (sChangedKey.equals(m_strTaxKey)) {
      // 税率 优于 无税金额,价税合计

      // 没有无税金额、价税合计、税率字段时 不进行计算;
      // 并且价税合计、税率为空时，价税合计 = 税额
      if (!m_strTaxRateKey.equals("") && !m_strMoneyKey.equals("")
          && !m_strSummnyKey.equals("") && getUFDouble(m_strMoneyKey) == null
          && getUFDouble(m_strSummnyKey) == null
          && getUFDouble(m_strTaxRateKey) == null) {
        String formulas[] = new String[] {
          m_strSummnyKey + "->" + m_strTaxKey
        };
        execFormulas(formulas);
        // 设置该KEY已改变
        setKeysExecuted(CIRCLE_TAXRATE, true);
        // 设置执行标记
        setCircleExecuted(CIRCLE_TAXRATE, true);
        // 执行下一个CIRCLE
        if (getPrior_Price_TaxPrice() == RelationsCalVO.TAXPRICE_PRIOR_TO_PRICE) {
          execCircle_NumNetTaxPriceSummny(m_strSummnyKey);
          execCircle_NumNetPriceMoney(m_strMoneyKey);
        }
        else if (getPrior_Price_TaxPrice() == RelationsCalVO.PRICE_PRIOR_TO_TAXPRICE) {
          execCircle_NumNetPriceMoney(m_strMoneyKey);
          execCircle_NumNetTaxPriceSummny(m_strSummnyKey);
        }
        return;
      }

      // 无税金额为空、税率不为空时，通过税额反算金额及合计
      if (!m_strTaxRateKey.equals("") && !m_strMoneyKey.equals("")
          && !m_strSummnyKey.equals("") && m_strDiscountTaxTypeName != null
          && getUFDouble(m_strMoneyKey) == null
          && getUFDouble(m_strTaxRateKey) != null) {

        if (m_strDiscountTaxTypeName.equals("应税外加")) {
          String formulas[] = new String[] {
              m_strMoneyKey + "->" + m_strTaxKey + "/(" + m_strTaxRateKey
                  + "/100.0)",
              m_strSummnyKey + "->" + m_strMoneyKey + "+" + m_strTaxKey
          };
          execFormulas(formulas);
          // 设置该KEY已改变
          setKeysExecuted(CIRCLE_TAXRATE, true);
          // 设置执行标记
          setCircleExecuted(CIRCLE_TAXRATE, true);
          // 执行下一个CIRCLE
          // if
          // (getPrior_Price_TaxPrice()==RelationsCalVO.TAXPRICE_PRIOR_TO_PRICE)
          // {
          // execCircle_NumNetTaxPriceSummny(m_strSummny) ;
          // execCircle_NumNetPriceMoney(m_strMoney) ;
          // }else if
          // (getPrior_Price_TaxPrice()==RelationsCalVO.PRICE_PRIOR_TO_TAXPRICE)
          // {
          execCircle_NumNetPriceMoney(m_strMoneyKey);
          execCircle_NumNetTaxPriceSummny(m_strSummnyKey);
          // }
          return;
        }
        else if (m_strDiscountTaxTypeName.equals("应税内含")) {
          String formulas[] = new String[] {
              m_strSummnyKey + "->" + m_strTaxKey + "/(" + m_strTaxRateKey
                  + "/100.0)",
              m_strMoneyKey + "->" + m_strSummnyKey + "-" + m_strTaxKey
          };
          execFormulas(formulas);
          // 设置该KEY已改变
          setKeysExecuted(CIRCLE_TAXRATE, true);
          // 设置执行标记
          setCircleExecuted(CIRCLE_TAXRATE, true);
          // 执行下一个CIRCLE
          // if
          // (getPrior_Price_TaxPrice()==RelationsCalVO.TAXPRICE_PRIOR_TO_PRICE)
          // {
          execCircle_NumNetTaxPriceSummny(m_strSummnyKey);
          execCircle_NumNetPriceMoney(m_strMoneyKey);
          // }else if
          // (getPrior_Price_TaxPrice()==RelationsCalVO.PRICE_PRIOR_TO_TAXPRICE)
          // {
          // execCircle_NumNetPriceMoney(m_strMoney) ;
          // execCircle_NumNetTaxPriceSummny(m_strSummny) ;
          // }
          return;
        }
      }

      // 当价税合计、无税金额、税率均存在时 ---> 根据参数决定影响无税金额还是价税合计
      if (!m_strTaxRateKey.equals("") && !m_strMoneyKey.equals("")
          && !m_strSummnyKey.equals("") ) {
        // 执行公式
        if (m_strDiscountTaxTypeName == null) {
          return;
        }
        String formulas[] = null;
        /*// 税率为空时或扣税类别为不计税时，不加进公式，以免破坏 金额+税额=价税合计 的关系
        if (m_strDiscountTaxTypeName.equals("不计税")
            || getUFDouble(m_strTaxKey) == null) {
          formulas = new String[] {
            m_strSummnyKey + "->" + m_strMoneyKey
          };*/
        /*}
        else {*/
        	/*  formulas = new String[] {
            m_strSummnyKey + "->" + m_strMoneyKey + "+" + m_strTaxKey
          };
        }
        execFormulas(formulas);
        // 设置该KEY已改变
        setKeysExecuted(CIRCLE_TAXRATE, true);
        // 设置执行标记
        setCircleExecuted(CIRCLE_TAXRATE, true);
        // 执行下一个CIRCLE
        execCircle_NumNetTaxPriceSummny(m_strSummnyKey);*/
        	
       /** 
        * 以上逻辑v5.3改变：若价税合计、无税金额、税率均不为空，改变税额时：
        * 含税优先：无税金额 =  价税合计 – 税额
        * 无税优先：价税合计 = 无税金额 + 税额
        * 其余各项均不变
        * */
        if (getPrior_Price_TaxPrice() == RelationsCalVO.TAXPRICE_PRIOR_TO_PRICE) {
           formulas = new String[] {
              m_strMoneyKey + "->" + m_strSummnyKey + "-" + m_strTaxKey
        	};
        }
        else if (getPrior_Price_TaxPrice() == RelationsCalVO.PRICE_PRIOR_TO_TAXPRICE) {
           formulas = new String[] {
              m_strSummnyKey + "->" + m_strMoneyKey + "+" + m_strTaxKey
           };
        }
        execFormulas(formulas);
        // 设置该KEY已改变
        setKeysExecuted(CIRCLE_TAXRATE, true);
        // 设置执行标记
        setCircleExecuted(CIRCLE_TAXRATE, true);	
        return;
      //}
    }
   }   

    // ////////////////价税合计改变
    if (sChangedKey.equals(m_strSummnyKey)) {
      // 税率 优于 金额,税额

      // 价税合计,税率 ---> 税额,金额
      if (!m_strTaxRateKey.equals("") && !m_strTaxKey.equals("")
          && !m_strSummnyKey.equals("") && !isKeyExecuted(m_strTaxKey)
          && !isKeyExecuted(m_strMoneyKey)) {
        // 先取得税额
        String formulas[] = new String[] {
          getTax_TaxrateSummny()
        };
        execFormulas(formulas);
        // 执行公式后，税额为空时，不加进公式，以免破坏 无税金额+税额=价税合计 的关系
        if (getUFDouble(m_strTaxKey) == null) {
          formulas = new String[] {
            m_strMoneyKey + "->" + m_strSummnyKey
          };
        }
        else {
          formulas = new String[] {
            m_strMoneyKey + "->" + m_strSummnyKey + "-" + m_strTaxKey
          };
        }
        execFormulas(formulas);
        // 设置该KEY已改变
        setKeysExecuted(CIRCLE_TAXRATE, true);
        // 设置执行标记
        setCircleExecuted(CIRCLE_TAXRATE, true);
        // 执行下一个CIRCLE
        execCircle_NumNetPriceMoney(m_strMoneyKey);
        execCircle_NumNetTaxPriceSummny(m_strSummnyKey);
        return;
      }

      // 价税合计,税率 ---> 税额,金额
      if (!m_strTaxRateKey.equals("") && !m_strTaxKey.equals("")
          && !m_strSummnyKey.equals("") && isKeyExecuted(m_strMoneyKey)) {
        // 先取得税额
        String formulas[] = new String[] {
          getTax_TaxrateSummny()
        };
        execFormulas(formulas);
        // 设置该KEY已改变
        setKeysExecuted(CIRCLE_TAXRATE, true);
        // 设置执行标记
        setCircleExecuted(CIRCLE_TAXRATE, true);
        // 执行下一个CIRCLE
        execCircle_NumNetTaxPriceSummny(m_strSummnyKey);
        return;
      }
    }
  }

  /**
   * 作者：王印芬 功能：根据改变的项,得到该项影响的有效CIRCLE 参数：无 返回：可执行的第一个有效CIRCLE 例外：无 日期：(2002-5-15
   * 11:39:21) 修改日期，修改人，修改原因，注释标志：
   */
  private int getAvailableCircleByKey(String sFirstChangedKey) {

    int iRetCircle = CIRCLE_IMPOSSIBLE;
    // 数量
    if (sFirstChangedKey.equals(m_strNumKey)) {
      // 含税单价优先
      if (getPrior_Price_TaxPrice() == RelationsCalVO.TAXPRICE_PRIOR_TO_PRICE) {
        if (isCircleExecutable(CIRCLE_NUM_NETTAXPRICE_SUMMNY, m_strNumKey)) {
          iRetCircle = CIRCLE_NUM_NETTAXPRICE_SUMMNY;
        }
        else if (isCircleExecutable(CIRCLE_NUM_NETPRICE_MONEY, m_strNumKey)) {
          iRetCircle = CIRCLE_NUM_NETPRICE_MONEY;
        }
        else if (isCircleExecutable(CIRCLE_QUALIFIED_NUM, m_strNumKey)) {
          iRetCircle = CIRCLE_QUALIFIED_NUM;
        }
        else if (isCircleExecutable(CIRCLE_CONVERT_RATE, m_strNumKey)) {
          iRetCircle = CIRCLE_CONVERT_RATE;
        }
        else if (isCircleExecutable(CIRCLE_QT_CONVERT_RATE, m_strNumKey)) {
            iRetCircle = CIRCLE_QT_CONVERT_RATE;
        }
      }
      // 单价优先
      else if (getPrior_Price_TaxPrice() == RelationsCalVO.PRICE_PRIOR_TO_TAXPRICE) {
        if (isCircleExecutable(CIRCLE_NUM_NETPRICE_MONEY, m_strNumKey)) {
          iRetCircle = CIRCLE_NUM_NETPRICE_MONEY;
        }
        else if (isCircleExecutable(CIRCLE_NUM_NETTAXPRICE_SUMMNY, m_strNumKey)) {
          iRetCircle = CIRCLE_NUM_NETTAXPRICE_SUMMNY;
        }
        else if (isCircleExecutable(CIRCLE_QUALIFIED_NUM, m_strNumKey)) {
          iRetCircle = CIRCLE_QUALIFIED_NUM;
        }
        else if (isCircleExecutable(CIRCLE_CONVERT_RATE, m_strNumKey)) {
          iRetCircle = CIRCLE_CONVERT_RATE;
        }
        else if (isCircleExecutable(CIRCLE_QT_CONVERT_RATE, m_strNumKey)) {
            iRetCircle = CIRCLE_QT_CONVERT_RATE;
        }
      }
    }
    // 无税净价
    else if (sFirstChangedKey.equals(m_strNetPriceKey)) {
      if (isCircleExecutable(CIRCLE_PRICE_NETPRICE, m_strNetPriceKey)) {
        iRetCircle = CIRCLE_PRICE_NETPRICE;
      }
      else if (isCircleExecutable(CIRCLE_NETTAXPRICE_NETPRICE, m_strNetPriceKey)) {
        iRetCircle = CIRCLE_NETTAXPRICE_NETPRICE;
      }
      else if (isCircleExecutable(CIRCLE_NUM_NETPRICE_MONEY, m_strNetPriceKey)) {
        iRetCircle = CIRCLE_NUM_NETPRICE_MONEY;
      }
    }
    // 报价数量
    else if (sFirstChangedKey.equals(m_strQt_NumKey)) {
    	// 含税优先
        if (getPrior_Price_TaxPrice() == RelationsCalVO.TAXPRICE_PRIOR_TO_PRICE) {
          if (isCircleExecutable(CIRCLE_QT_NUM_NETTAXPRICE_SUMMNY, m_strQt_NumKey)) {
            iRetCircle = CIRCLE_QT_NUM_NETTAXPRICE_SUMMNY;
          }
          else if (isCircleExecutable(CIRCLE_QT_NUM_NETPRICE_MONEY, m_strQt_NumKey)) {
            iRetCircle = CIRCLE_QT_NUM_NETPRICE_MONEY;
          }
          else if (isCircleExecutable(CIRCLE_QT_CONVERT_RATE, m_strQt_NumKey)) {
              iRetCircle = CIRCLE_QT_CONVERT_RATE;
          }
        }
        // 无税优先
        else if (getPrior_Price_TaxPrice() == RelationsCalVO.PRICE_PRIOR_TO_TAXPRICE) {
          if (isCircleExecutable(CIRCLE_QT_NUM_NETPRICE_MONEY, m_strQt_NumKey)) {
            iRetCircle = CIRCLE_QT_NUM_NETPRICE_MONEY;
          }
          else if (isCircleExecutable(CIRCLE_QT_NUM_NETTAXPRICE_SUMMNY, m_strQt_NumKey)) {
            iRetCircle = CIRCLE_QT_NUM_NETTAXPRICE_SUMMNY;
          }
          else if (isCircleExecutable(CIRCLE_QT_CONVERT_RATE, m_strQt_NumKey)) {
              iRetCircle = CIRCLE_QT_CONVERT_RATE;
          }
        }    
    }
    // 报价换算率
    else if (sFirstChangedKey.equals(m_strQt_ConvertRateKey)) {
      if (isCircleExecutable(CIRCLE_QT_CONVERT_RATE, m_strQt_ConvertRateKey)) {
        iRetCircle = CIRCLE_QT_CONVERT_RATE;
      }
    }
    // 金额
    else if (sFirstChangedKey.equals(m_strMoneyKey)) {
      if (isCircleExecutable(CIRCLE_TAXRATE, m_strMoneyKey)) {
        iRetCircle = CIRCLE_TAXRATE;
      }
      else if (isCircleExecutable(CIRCLE_NUM_NETPRICE_MONEY, m_strMoneyKey)) {
        iRetCircle = CIRCLE_NUM_NETPRICE_MONEY;
      }
    }
    // 税率
    else if (sFirstChangedKey.equals(m_strTaxRateKey)) {
      if (isCircleExecutable(CIRCLE_TAXRATE, m_strTaxRateKey)) {
        iRetCircle = CIRCLE_TAXRATE;
      }
      else if (isCircleExecutable(CIRCLE_TAXPRICE_PRICE, m_strTaxRateKey)) {
        iRetCircle = CIRCLE_TAXPRICE_PRICE;
      }
      else if (isCircleExecutable(CIRCLE_NETTAXPRICE_NETPRICE, m_strTaxRateKey)) {
        iRetCircle = CIRCLE_NETTAXPRICE_NETPRICE;
      }
    }
    // 税额
    else if (sFirstChangedKey.equals(m_strTaxKey)) {
      if (isCircleExecutable(CIRCLE_TAXRATE, m_strTaxKey)) {
        iRetCircle = CIRCLE_TAXRATE;
      }
    }
    // 价税合计
    else if (sFirstChangedKey.equals(m_strSummnyKey)) {
      if (isCircleExecutable(CIRCLE_TAXRATE, m_strSummnyKey)) {
            iRetCircle = CIRCLE_TAXRATE;
          }
      else if (isCircleExecutable(CIRCLE_NUM_NETTAXPRICE_SUMMNY, m_strSummnyKey)) {
        iRetCircle = CIRCLE_NUM_NETTAXPRICE_SUMMNY;
      }
    }
    // 含税净价
    else if (sFirstChangedKey.equals(m_strNetTaxPriceKey)) {
      if (isCircleExecutable(CIRCLE_TAXPRICE_NETTAXPRICE, m_strNetTaxPriceKey)) {
        iRetCircle = CIRCLE_TAXPRICE_NETTAXPRICE;
      }
      else if (isCircleExecutable(CIRCLE_NETTAXPRICE_NETPRICE,
          m_strNetTaxPriceKey)) {
        iRetCircle = CIRCLE_NETTAXPRICE_NETPRICE;
      }
      else if (isCircleExecutable(CIRCLE_NUM_NETTAXPRICE_SUMMNY,
          m_strNetTaxPriceKey)) {
        iRetCircle = CIRCLE_NUM_NETTAXPRICE_SUMMNY;
      }
    }
    // 扣率(包括单品折扣，整单折扣)
    else if (sFirstChangedKey.equals(m_strDiscountRateKey)
    		|| sFirstChangedKey.equals(m_strAllDiscountRateKey)) {
      // 含税优先(含税单价优先)
      if (getPrior_Price_TaxPrice() == RelationsCalVO.TAXPRICE_PRIOR_TO_PRICE) {
        if (isCircleExecutable(CIRCLE_TAXPRICE_NETTAXPRICE,
        		sFirstChangedKey)) {
          iRetCircle = CIRCLE_TAXPRICE_NETTAXPRICE;
        }
        else if (isCircleExecutable(CIRCLE_PRICE_NETPRICE, sFirstChangedKey)) {
          iRetCircle = CIRCLE_PRICE_NETPRICE;
        }
      }
      // 无税优先(单价优先)
      else if (getPrior_Price_TaxPrice() == RelationsCalVO.PRICE_PRIOR_TO_TAXPRICE) {
        if (isCircleExecutable(CIRCLE_PRICE_NETPRICE, sFirstChangedKey)) {
          iRetCircle = CIRCLE_PRICE_NETPRICE;
        }
        else if (isCircleExecutable(CIRCLE_TAXPRICE_NETTAXPRICE,
        		sFirstChangedKey)) {
          iRetCircle = CIRCLE_TAXPRICE_NETTAXPRICE;
        }
      }
    }
    // 含税单价
    else if (sFirstChangedKey.equals(m_strTaxPriceKey)) {
      if (isCircleExecutable(CIRCLE_TAXPRICE_NETTAXPRICE, m_strTaxPriceKey)) {//含税单净价,折扣
        iRetCircle = CIRCLE_TAXPRICE_NETTAXPRICE;
      }
      else if (isCircleExecutable(CIRCLE_TAXPRICE_PRICE, m_strTaxPriceKey)) {
        iRetCircle = CIRCLE_TAXPRICE_PRICE;
      }
    }
    // 无税单价
    else if (sFirstChangedKey.equals(m_strPriceKey)) {
      if (isCircleExecutable(CIRCLE_PRICE_NETPRICE, m_strPriceKey)) {
        iRetCircle = CIRCLE_PRICE_NETPRICE;
      }
      else if (isCircleExecutable(CIRCLE_TAXPRICE_PRICE, m_strPriceKey)) {
        iRetCircle = CIRCLE_TAXPRICE_PRICE;
      }
    }
    // 合格数量
    else if (sFirstChangedKey.equals(m_strQualifiedNumKey)) {
      if (isCircleExecutable(CIRCLE_QUALIFIED_NUM, m_strQualifiedNumKey)) {
        iRetCircle = CIRCLE_QUALIFIED_NUM;
      }
    }
    // 不合格数量
    else if (sFirstChangedKey.equals(m_strUnQualifiedNumKey)) {
      if (isCircleExecutable(CIRCLE_QUALIFIED_NUM, m_strUnQualifiedNumKey)) {
        iRetCircle = CIRCLE_QUALIFIED_NUM;
      }
    }
    // 辅数量
    else if (sFirstChangedKey.equals(m_strAssistNumKey)) {
      if (isCircleExecutable(CIRCLE_CONVERT_RATE, m_strAssistNumKey)) {
        iRetCircle = CIRCLE_CONVERT_RATE;
      }
    }
    // 换算率
    else if (sFirstChangedKey.equals(m_strConvertRateKey)) {
      if (isCircleExecutable(CIRCLE_CONVERT_RATE, m_strConvertRateKey)) {
        iRetCircle = CIRCLE_CONVERT_RATE;
      }
    }
    // 报价原币含税单价
    else if (sFirstChangedKey.equals(m_strQt_TaxPriceKey)) {
      if (isCircleExecutable(CIRCLE_QT_TAXPRICE_NETTAXPRICE, m_strQt_TaxPriceKey)) {
        iRetCircle = CIRCLE_QT_TAXPRICE_NETTAXPRICE;
      }
      else if (isCircleExecutable(CIRCLE_QT_TAXPRICE_PRICE, m_strQt_TaxPriceKey)) {
        iRetCircle = CIRCLE_QT_TAXPRICE_PRICE;
      }
    }
    // 报价原币无税单价
    else if (sFirstChangedKey.equals(m_strQt_PriceKey)) {
      if (isCircleExecutable(CIRCLE_QT_PRICE_NETPRICE, m_strQt_PriceKey)) {
        iRetCircle = CIRCLE_QT_PRICE_NETPRICE;
      }
      else if (isCircleExecutable(CIRCLE_QT_TAXPRICE_PRICE, m_strQt_PriceKey)) {
        iRetCircle = CIRCLE_QT_TAXPRICE_PRICE;
      }
    }
    // 报价原币含税净价
    else if (sFirstChangedKey.equals(m_strQt_NetTaxPriceKey)) {
      if (isCircleExecutable(CIRCLE_QT_TAXPRICE_NETTAXPRICE, m_strQt_NetTaxPriceKey)) {
        iRetCircle = CIRCLE_QT_TAXPRICE_NETTAXPRICE;
      }
      else if (isCircleExecutable(CIRCLE_QT_NETTAXPRICE_NETPRICE,
    		  m_strQt_NetTaxPriceKey)) {
        iRetCircle = CIRCLE_QT_NETTAXPRICE_NETPRICE;
      }
      else if (isCircleExecutable(CIRCLE_QT_NUM_NETTAXPRICE_SUMMNY,
    		  m_strQt_NetTaxPriceKey)) {
        iRetCircle = CIRCLE_QT_NUM_NETTAXPRICE_SUMMNY;
      }
    }
    // 报价原币无税净价
    else if (sFirstChangedKey.equals(m_strQt_NetPriceKey)) {
      if (isCircleExecutable(CIRCLE_QT_PRICE_NETPRICE, m_strQt_NetPriceKey)) {
        iRetCircle = CIRCLE_QT_PRICE_NETPRICE;
      }
      else if (isCircleExecutable(CIRCLE_QT_NETTAXPRICE_NETPRICE, m_strQt_NetPriceKey)) {
        iRetCircle = CIRCLE_QT_NETTAXPRICE_NETPRICE;
      }
      else if (isCircleExecutable(CIRCLE_QT_NUM_NETPRICE_MONEY, m_strQt_NetPriceKey)) {
        iRetCircle = CIRCLE_QT_NUM_NETPRICE_MONEY;
      }
    }
    // 本币价税合计
    else if (sFirstChangedKey.equals(m_strSummny_LocalKey)) {
    	iRetCircle = CIRCLE_LOCAL_SUMMNY_MNY_TAX;
    }
    // 本币无税金额
    else if (sFirstChangedKey.equals(m_strMoney_LocalKey)) {
    	iRetCircle = CIRCLE_LOCAL_SUMMNY_MNY_TAX;
    }

    // 设置该KEY改变后，不改变的项
    setKeyExecutedForFirstChangedKey(sFirstChangedKey, iRetCircle);
    // 设置该KEY已改变
    setKeyExecuted(sFirstChangedKey, true);

    return iRetCircle;
  }

  /**
   * 作者：王印芬 功能：得到最初改变的KEY 参数：无 返回：最初改变的KEY 例外：无 日期：(2002-5-15 11:39:21)
   * 修改日期，修改人，修改原因，注释标志：
   */
  private String getFirstChangedKey() {
    return m_strChangedKey;
  }

  /**
   * 根据扣税类别、金额、税率得到价税合计的计算公式
   * 
   * @param 无
   * @return 得到的价税合计的计算公式
   * @exception 异常描述
   * @see 需要参见的其它内容
   * @since 从类的那一个版本，此方法被添加进来。（可选）
   */
  private String getKeyByDescription(int nDescription) {

    if (nDescription == DISCOUNT_TAX_TYPE_KEY) {
      return m_strDiscountTaxTypeKey;
    }
    else if (nDescription == NUM) {
      return m_strNumKey;
    }
    else if (nDescription == NUM_QUALIFIED) {
      return m_strQualifiedNumKey;
    }
    else if (nDescription == NUM_UNQUALIFIED) {
      return m_strUnQualifiedNumKey;
    }
    else if (nDescription == NUM_ASSIST) {
      return m_strAssistNumKey;
    }
    else if (nDescription == NET_PRICE_ORIGINAL) {
      return m_strNetPriceKey;
    }
    else if (nDescription == NET_TAXPRICE_ORIGINAL) {
      return m_strNetTaxPriceKey;
    }
    else if (nDescription == PRICE_ORIGINAL) {
      return m_strPriceKey;
    }
    else if (nDescription == TAXRATE) {
      return m_strTaxRateKey;
    }
    else if (nDescription == TAX_ORIGINAL) {
      return m_strTaxKey;
    }
    else if (nDescription == MONEY_ORIGINAL) {
      return m_strMoneyKey;
    }
    else if (nDescription == SUMMNY_ORIGINAL) {
      return m_strSummnyKey;
    }
    else if (nDescription == DISCOUNT_RATE) {
      return m_strDiscountRateKey;
    }
    else if (nDescription == CONVERT_RATE) {
      return m_strConvertRateKey;
    }
    else if (nDescription == TAXPRICE_ORIGINAL) {
      return m_strTaxPriceKey;
    }
    //报价计量单位
    else if (nDescription == QT_TAXPRICE_ORIGINAL) {
        return m_strQt_TaxPriceKey;
    }
    else if (nDescription == QT_PRICE_ORIGINAL) {
        return m_strQt_PriceKey;
    }
    else if (nDescription == QT_NET_TAXPRICE_ORIGINAL) {
        return m_strQt_NetTaxPriceKey;
    }
    else if (nDescription == QT_NET_PRICE_ORIGINAL) {
        return m_strQt_NetPriceKey;
    }
    else if (nDescription == QT_NUM) {
        return m_strQt_NumKey;
    }
    //主/报价计量单位ID
    else if (nDescription == UNITID) {
        return m_strUnitID;
    }
    else if (nDescription == QUOTEUNITID) {
        return m_strQuoteUnitID;
    }
    else if (nDescription == EXCHANGE_O_TO_BRATE){
    	return m_strExchange_O_TO_BrateKey;
    }
    else if (nDescription == CURRTYPEPk){
    	return m_strCurrTypePkKey;
    }
    
    return null;
  }
  
  /**
   * 作者：张成 (7)
   * 功能：根据 含税优先机制以及是否有整单折扣 
   * 得到 含税单价、含税净价、折扣计算公式 参数：无 返回：单价或折扣的计算公式 
   * 例外：无 日期：(2008-03-09 11:22:51) 修改日期，修改人，修改原因，注释标志：
   */
  private String[] getTaxPrice_NetTaxPrice_Discount( String changekey ) {
	  //无整单折扣
	  if (m_strAllDiscountRateKey.equals("")){
		  //计算含税单价
		  if ( m_strTaxPriceKey.equals(changekey) ){
			  return new String[]{ m_strTaxPriceKey + "->" + m_strNetTaxPriceKey + "/("
				+ m_strDiscountRateKey + "/100.0)"};
		  }
		  //计算含税净价
		  else if (m_strNetTaxPriceKey.equals(changekey)){
			  return new String[]{ m_strNetTaxPriceKey + "->" + m_strTaxPriceKey + "*("
				+ m_strDiscountRateKey + "/100.0)"};
		  }
		  //计算折扣
	      else if (m_strDiscountRateKey.equals(changekey)){
	    	  if (getFirstChangedKey().equals(m_strQt_NetTaxPriceKey))
                  return new String[] { m_strDiscountRateKey + "->100.0*"
                            + m_strQt_NetTaxPriceKey + "/" + m_strQt_TaxPriceKey };
              else
                  return new String[] { m_strDiscountRateKey + "->100.0*"
                            + m_strNetTaxPriceKey + "/" + m_strTaxPriceKey };
		  }
		  //计算报价原币含税单价
	      else if ( m_strQt_TaxPriceKey.equals(changekey) ){
			  return new String[]{ m_strQt_TaxPriceKey + "->" + m_strQt_NetTaxPriceKey + "/("
				+ m_strDiscountRateKey + "/100.0)"};
		  }
		  //计算报价原币含税净价
	      else if ( m_strQt_NetTaxPriceKey.equals(changekey) ){
	    	  return new String[]{ m_strQt_NetTaxPriceKey + "->" + m_strQt_TaxPriceKey + "*("
	  				+ m_strDiscountRateKey + "/100.0)"};
		  }
	  }
	  //有整单折扣
	  else {
		  //计算含税单价
          if ( m_strTaxPriceKey.equals(changekey) ){
        	  return new String[]{ m_strTaxPriceKey + "->" + m_strNetTaxPriceKey
				+ "/((" + m_strDiscountRateKey + "/100.0) * ("
				+ m_strAllDiscountRateKey + "/100.0))"};
		  }
          //计算含税净价
		  else if (m_strNetTaxPriceKey.equals(changekey)){
			  return new String[]{ m_strNetTaxPriceKey + "->" + m_strTaxPriceKey + "*("
				+ m_strDiscountRateKey + "/100.0)*("
				+ m_strAllDiscountRateKey + "/100.0)"};
		  }
          //计算折扣
	      else if (m_strDiscountRateKey.equals(changekey)){
	    	  if (getFirstChangedKey().equals(m_strQt_NetTaxPriceKey))
                  return new String[]{ m_strDiscountRateKey + "->100.0*"
                       + m_strQt_NetTaxPriceKey + "/(" + m_strQt_TaxPriceKey
                       + "*" + m_strAllDiscountRateKey + "/100.0)"};
              else
                  return new String[]{ m_strDiscountRateKey + "->100.0*"
                       + m_strNetTaxPriceKey + "/(" + m_strTaxPriceKey
                       + "*" + m_strAllDiscountRateKey + "/100.0)"};
		  }
          //计算报价原币含税单价
	      else if ( m_strQt_TaxPriceKey.equals(changekey) ){
			  return new String[]{ m_strQt_TaxPriceKey + "->" + m_strQt_NetTaxPriceKey
						+ "/((" + m_strDiscountRateKey + "/100.0) * ("
						+ m_strAllDiscountRateKey + "/100.0))"};
		  }
          //计算报价原币含税净价
	      else if ( m_strQt_NetTaxPriceKey.equals(changekey) ){
	    	  return new String[]{ m_strQt_NetTaxPriceKey + "->" + m_strQt_TaxPriceKey + "*("
	  				+ m_strDiscountRateKey + "/100.0)*("
	  				+ m_strAllDiscountRateKey + "/100.0)"};
		  }
	  }
	  return null;
  }
  
  /**
   * 作者：张成 (6)
   * 功能：根据 含税优先机制以及是否有整单折扣 
   * 得到 无税单价、无税净价、折扣计算公式 参数：无 返回：单价或折扣的计算公式 
   * 例外：无 日期：(2008-03-09 11:22:51) 修改日期，修改人，修改原因，注释标志：
   */
  private String[] getPrice_NetPrice_Discount( String changekey ) {
	  	  
	  //无整单折扣
	  if (m_strAllDiscountRateKey.equals("")){
		  //计算无税单价
		  if ( m_strPriceKey.equals(changekey) ){
			  return new String[]{ m_strPriceKey + "->" + m_strNetPriceKey + "/("
				+ m_strDiscountRateKey + "/100.0)"};
		  }
		  //计算无税净价
		  else if (m_strNetPriceKey.equals(changekey)){
			  return new String[]{ m_strNetPriceKey + "->" + m_strPriceKey + "*("
				+ m_strDiscountRateKey + "/100.0)"};
		  }
		  //计算折扣
	      else if (m_strDiscountRateKey.equals(changekey)){
	    	  return new String[]{ m_strDiscountRateKey + "->100.0*"
				+ m_strNetPriceKey + "/" + m_strPriceKey};
		  }
		  //计算报价原币无税单价
	      else if ( m_strQt_PriceKey.equals(changekey) ){
	    	  if (getFirstChangedKey().equals(m_strQt_NetPriceKey))
                  return new String[]{ m_strDiscountRateKey + "->100.0*"
                      + m_strQt_NetPriceKey + "/" + m_strQt_PriceKey};
              else
                  return new String[]{ m_strDiscountRateKey + "->100.0*"
                    + m_strNetPriceKey + "/" + m_strPriceKey};
		  }
		  //计算报价原币无税净价
	      else if ( m_strQt_NetPriceKey.equals(changekey) ){
	    	  return new String[]{ m_strQt_NetPriceKey + "->" + m_strQt_PriceKey + "*("
	  				+ m_strDiscountRateKey + "/100.0)"};
		  }
	  }
	  //有整单折扣
	  else {
		  //计算无税单价
          if ( m_strPriceKey.equals(changekey) ){
        	  return new String[]{ m_strPriceKey + "->" + m_strNetPriceKey
				+ "/((" + m_strDiscountRateKey + "/100.0) * ("
				+ m_strAllDiscountRateKey + "/100.0))"};
		  }
          //计算无税净价
		  else if (m_strNetPriceKey.equals(changekey)){
			  return new String[]{ m_strNetPriceKey + "->" + m_strPriceKey + "*("
				+ m_strDiscountRateKey + "/100.0)*("
				+ m_strAllDiscountRateKey + "/100.0)"};
		  }
          //计算折扣
	      else if (m_strDiscountRateKey.equals(changekey)){
	    	  if (getFirstChangedKey().equals(m_strQt_NetPriceKey))
                  return new String[]{ m_strDiscountRateKey + "->100.0*"
                      + m_strQt_NetPriceKey + "/(" + m_strQt_PriceKey
                      + "*" + m_strAllDiscountRateKey + "/100.0)"};
              else
                  return new String[]{ m_strDiscountRateKey + "->100.0*"
                    + m_strNetPriceKey + "/(" + m_strPriceKey
                    + "*" + m_strAllDiscountRateKey + "/100.0)"};
		  }
          //计算报价原币无税单价
	      else if ( m_strQt_PriceKey.equals(changekey) ){
			  return new String[]{ m_strQt_PriceKey + "->" + m_strQt_NetPriceKey
						+ "/((" + m_strDiscountRateKey + "/100.0) * ("
						+ m_strAllDiscountRateKey + "/100.0))"};
		  }
          //计算报价原币无税净价
	      else if ( m_strQt_NetPriceKey.equals(changekey) ){
	    	  return new String[]{ m_strQt_NetPriceKey + "->" + m_strQt_PriceKey + "*("
	  				+ m_strDiscountRateKey + "/100.0)*("
	  				+ m_strAllDiscountRateKey + "/100.0)"};
		  }
	  }
	  
	  /*//无税优先
	  if ( m_iPrior_Price_TaxPrice ==  RelationsCalVO.PRICE_PRIOR_TO_TAXPRICE){
		  //无整单折扣
		  if (m_strAllDiscountRateKey.equals("")){
			  //当前变化值为无税单价或折扣
			  if ( m_strPriceKey.equals(changekey) || m_strDiscountRateKey.equals(changekey) ){
				  return m_strNetPriceKey + "->" + m_strPriceKey + "*("
							+ m_strDiscountRateKey + "/100.0)";
			  }
			  //当前变化值为无税净价
			  else if (m_strNetPriceKey.equals(changekey)){
				  //调单价
				  if (m_iPrior_ItemDiscountRate_Price == RelationsCalVO.MODIFY_PRICE)
					  return m_strPriceKey + "->" + m_strNetPriceKey + "/("
							+ m_strDiscountRateKey + "/100.0)";
				  //调折扣
				  else if (m_iPrior_ItemDiscountRate_Price == RelationsCalVO.MODIFY_ITEMDISCOUNTRATE)
					  return m_strDiscountRateKey + "->"
								+ m_strNetPriceKey + "/" + m_strPriceKey;
			  }
		  }
		  //有整单折扣
		  else{
			  //当前变化值为无税单价或折扣
			  if ( m_strPriceKey.equals(changekey) || m_strDiscountRateKey.equals(changekey) ){
				  return m_strNetPriceKey + "->" + m_strPriceKey + "*("
							+ m_strDiscountRateKey + "/100.0)*("
							+ m_strAllDiscountRateKey + "/100.0)";
			  }
			  //当前变化值为无税净价
			  else if (m_strNetPriceKey.equals(changekey)){
				  //调单价
				  if (m_iPrior_ItemDiscountRate_Price == RelationsCalVO.MODIFY_PRICE)
					  return m_strPriceKey + "->" + m_strNetPriceKey
								+ "/(" + m_strDiscountRateKey + "/100.0) * ("
								+ m_strAllDiscountRateKey + "/100.0)";
				  //调折扣
				  else if (m_iPrior_ItemDiscountRate_Price == RelationsCalVO.MODIFY_ITEMDISCOUNTRATE)
					  return m_strDiscountRateKey + "->"
								+ m_strNetPriceKey + "/(" + m_strPriceKey
								+ "*" + m_strAllDiscountRateKey + "/100.0)";
			  }
		  }
	  }*/
	  return null;
  }

  /**
   * 作者：王印芬 功能：根据扣税类别、税率、净含税单价 得到 净单价的计算公式 参数：无 返回：税额的计算公式 例外：无 日期：(2003-10-10
   * 11:22:51) 修改日期，修改人，修改原因，注释标志：
   */
  private String getNetPrice_TaxrateNetTaxPrice() {

    if (m_strDiscountTaxTypeName == null)
      return "";

    String realTaxrate = m_strTaxRateKey + "/100.0";
    if (m_strDiscountTaxTypeName.equals("应税内含")) {
      return m_strNetPriceKey + "->" + m_strNetTaxPriceKey + "*(1.0-"
          + realTaxrate + ")";
    }
    else if (m_strDiscountTaxTypeName.equals("应税外加")) {
      return m_strNetPriceKey + "->" + m_strNetTaxPriceKey + "/(1.0+"
          + realTaxrate + ")";
    }
    else if (m_strDiscountTaxTypeName.equals("不计税")) {
      return m_strNetPriceKey + "->" + m_strNetTaxPriceKey;
    }

    return null;
  }

  /**
   * 作者：王印芬 功能：根据扣税类别、税率、净单价 得到 净含税单价的计算公式 参数：无 返回：税额的计算公式 例外：无 日期：(2003-10-10
   * 11:22:51) 修改日期，修改人，修改原因，注释标志：
   */
  private String getNetTaxPrice_TaxrateNetPrice() {

    if (m_strDiscountTaxTypeName == null)
      return "";

    String realTaxrate = m_strTaxRateKey + "/100.0";
    if (m_strDiscountTaxTypeName.equals("应税内含")) {
      return m_strNetTaxPriceKey + "->" + m_strNetPriceKey + "/(1.0-"
          + realTaxrate + ")";
    }
    else if (m_strDiscountTaxTypeName.equals("应税外加")) {
      return m_strNetTaxPriceKey + "->" + m_strNetPriceKey + "*(1.0+"
          + realTaxrate + ")";
    }
    else if (m_strDiscountTaxTypeName.equals("不计税")) {
      return m_strNetTaxPriceKey + "->" + m_strNetPriceKey;
    }

    return null;
  }

  /**
   * 作者：王印芬 功能：根据扣税类别、税率、含税单价 得到 单价的计算公式 参数：无 返回：税额的计算公式 例外：无 日期：(2003-10-10
   * 11:22:51) 修改日期，修改人，修改原因，注释标志：
   */
  private String getPrice_TaxrateTaxPrice() {

    if (m_strDiscountTaxTypeName == null)
      return "";

    String realTaxrate = m_strTaxRateKey + "/100.0";
    if (m_strDiscountTaxTypeName.equals("应税内含")) {
      return m_strPriceKey + "->" + m_strTaxPriceKey + "*(1.0-" + realTaxrate
          + ")";
    }
    else if (m_strDiscountTaxTypeName.equals("应税外加")) {
      return m_strPriceKey + "->" + m_strTaxPriceKey + "/(1.0+" + realTaxrate
          + ")";
    }
    else if (m_strDiscountTaxTypeName.equals("不计税")) {
      return m_strPriceKey + "->" + m_strTaxPriceKey;
    }

    return null;
  }

  /**
   * 作者：王印芬 功能：得到单价、含税单价优先数 参数：无 返回：单价、含税单价哪个优先 例外：无 日期：(2003-06-09 11:22:51)
   * 修改日期，修改人，修改原因，注释标志：
   */
  private int getPrior_Price_TaxPrice() {
    return m_iPrior_Price_TaxPrice;
  }

  /**
   * 作者：王印芬 功能：根据扣税类别、税率、金额得到税额的计算公式 参数：无 返回：税额的计算公式 例外：无 日期：(2001-06-09
   * 11:22:51) 修改日期，修改人，修改原因，注释标志：
   */
  private String getTax_TaxrateMoney() {

    if (m_strDiscountTaxTypeName == null)
      return "";

    String realTaxrate = m_strTaxRateKey + "/100.0";
    if (m_strDiscountTaxTypeName.equals("应税内含")) {
      return m_strTaxKey + "->" + m_strMoneyKey + "*" + realTaxrate + "/(1.0-"
          + realTaxrate + ")";
    }
    else if (m_strDiscountTaxTypeName.equals("应税外加")) {
      return m_strTaxKey + "->" + m_strMoneyKey + "*" + realTaxrate;
    }
    else if (m_strDiscountTaxTypeName.equals("不计税")) {
      return m_strTaxKey + "->" + m_strTaxKey + "*0.0";
    }

    return null;
  }

  /**
   * 作者：王印芬 功能：根据扣税类别、税率、价税合计得到税额的计算公式 参数：无 返回：税额的计算公式 例外：无 日期：(2001-06-09
   * 11:22:51) 修改日期，修改人，修改原因，注释标志：
   */
  private java.lang.String getTax_TaxrateSummny() {
    if (m_strDiscountTaxTypeName == null)
      return "";

    // String realTaxrate = "(" + m_strTaxRate + "/100.0)";
    String realTaxrate = m_strTaxRateKey + "/100.0";
    if (m_strDiscountTaxTypeName.equals("应税内含")) {
      return m_strTaxKey + "->" + m_strSummnyKey + "*" + realTaxrate;
    }
    else if (m_strDiscountTaxTypeName.equals("应税外加")) {
      return m_strTaxKey + "->" + m_strSummnyKey + "*" + realTaxrate + "/(1.0+"
          + realTaxrate + ")";
    }
    else if (m_strDiscountTaxTypeName.equals("不计税")) {
      return m_strTaxKey + "->" + m_strTaxKey + "*0.0";
    }

    return null;
  }

  /**
   * 作者：王印芬 功能：根据扣税类别、税率、单价 得到 含税单价的计算公式 参数：无 返回：税额的计算公式 例外：无 日期：(2003-10-10
   * 11:22:51) 修改日期，修改人，修改原因，注释标志：
   */
  private String getTaxPrice_TaxratePrice() {

    if (m_strDiscountTaxTypeName == null)
      return "";

    String realTaxrate = m_strTaxRateKey + "/100.0";
    if (m_strDiscountTaxTypeName.equals("应税内含")) {
      return m_strTaxPriceKey + "->" + m_strPriceKey + "/(1.0-" + realTaxrate
          + ")";
    }
    else if (m_strDiscountTaxTypeName.equals("应税外加")) {
      return m_strTaxPriceKey + "->" + m_strPriceKey + "*(1.0+" + realTaxrate
          + ")";
    }
    else if (m_strDiscountTaxTypeName.equals("不计税")) {
      return m_strTaxPriceKey + "->" + m_strPriceKey;
    }

    return null;
  }

  /**
   * 作者：王印芬 功能：根据ITEMKEY得到得到当前行的值 只要有值，不管是否为零，都取这个值 为空或空串等时，返回NULL 参数：无
   * 返回：UFDouble 该行ITEMKEY对应的值 例外：无 日期：(2002-5-15 11:39:21) 修改日期，修改人，修改原因，注释标志：
   */
  private UFDouble getUFDouble(String strKey) {

    if (strKey == null || strKey.equals("")) {
      return null;
    }

    return PuPubVO.getUFDouble_ValueAsValue(m_voCurr.getAttributeValue(strKey));

  }
  
  private Object getObject(String strKey) {
		if (strKey == null || strKey.equals("")) {
			return null;
		}

		Object value = m_voCurr.getAttributeValue(strKey);
		if (value == null && m_hvoCurr != null)
			value = m_hvoCurr.getAttributeValue(strKey);

		return value;
	}
  
  /**
	 * 取表头VO中的值:根据ITEMKEY得到得到当前行的值 只要有值，不管是否为零，都取这个值 为空或空串等时，返回NULL
	 * 
	 * @param strKey
	 * @return
	 */
  private UFDouble getUFDoubleH(String strKey) {

	    if (strKey == null || strKey.equals("")) {
	      return null;
	    }

	    return PuPubVO.getUFDouble_ValueAsValue(m_hvoCurr.getAttributeValue(strKey));

  }

  /**
   * 如果是零，则返回NULL
   */
  private UFDouble getUFDoubleZeroAsNull(String strKey) {

    if (strKey == null || strKey.equals("")) {
      return null;
    }

    return PuPubVO.getUFDouble_ZeroAsNull(m_voCurr.getAttributeValue(strKey));

  }

  /**
   * 作者：王印芬 功能：初始化CIRCLE是否执行的初始值 参数：无 返回：无 例外：无 日期：(2002-5-15 11:39:21)
   * 修改日期，修改人，修改原因，注释标志：
   */
  private void initCircle() {
    // 19个CIRCLE
    m_bCircleExecuted = new boolean[] {
        false, false, false, false, false, false, false, false, false, false, false,
        false, false, false, false, false, false, false, false
    };
  }

  /**
   * 作者：王印芬 功能：初始化公式计算空与零的关系 参数：无 返回：无 例外：无 日期：(2002-5-15 11:39:21)
   * 修改日期，修改人，修改原因，注释标志：
   */
  private void initFormulaParseModel() {

    // m_bNullAsZero = m_pnlBill.getBillModel().getFormulaParse().isNullAsZero()
    // ;
    m_bNullAsZero = m_fp.isNullAsZero();

    // FROM V3.0，改
    // if (getUFDouble(getFirstChangedKey())==null) {
    // m_pnlBill.getBillModel().getFormulaParse().setNullAsZero(false) ;
    // } else {
    m_fp.setNullAsZero(true);
    // }

  }

  /**
   * 初始化各个KEY对应的OBJECT
   * 
   * @param 无
   * @return 无
   * @exception 异常描述
   * @see 需要参见的其它内容
   * @since 从类的那一个版本，此方法被添加进来。（可选）
   */
  private void initKeys(int[] nDescriptions, String[] strKeys) {

    for (int i = 0; i < nDescriptions.length; i++) {
      if (nDescriptions[i] == DISCOUNT_TAX_TYPE_NAME) {// 扣税类别名
    	 
    	// 应税外加
    	if (NCLangRes4VoTransl.getNCLangRes().getStrByID("scmpub", "UPPscmpub-000504").equals(strKeys[i]))
    		m_strDiscountTaxTypeName = "应税外加";
    	else if (NCLangRes4VoTransl.getNCLangRes().getStrByID("scmpub", "UPPscmpub-000505").equals(strKeys[i]))
    		m_strDiscountTaxTypeName = "应税内含";
    	else if (NCLangRes4VoTransl.getNCLangRes().getStrByID("scmpub", "UPPscmpub-000506").equals(strKeys[i]))
    		m_strDiscountTaxTypeName = "不计税";
    	else
    		m_strDiscountTaxTypeName = "应税外加";
      }
      else if (nDescriptions[i] == DISCOUNT_TAX_TYPE_KEY) {// 扣税类别
        m_strDiscountTaxTypeKey = strKeys[i];
      }
      else if (nDescriptions[i] == NUM) {// 数量
        m_strNumKey = strKeys[i];
      }
      else if (nDescriptions[i] == NUM_QUALIFIED) {// 合格数量
        m_strQualifiedNumKey = strKeys[i];
      }
      else if (nDescriptions[i] == NUM_UNQUALIFIED) {// 不合格数量
        m_strUnQualifiedNumKey = strKeys[i];
      }
      else if (nDescriptions[i] == NUM_ASSIST) {// 辅数量
        m_strAssistNumKey = strKeys[i];
      }
      else if (nDescriptions[i] == NET_PRICE_ORIGINAL) {// 原币无税单价
        m_strNetPriceKey = strKeys[i];
      }
      else if (nDescriptions[i] == NET_TAXPRICE_ORIGINAL) {// 原币含税单价
        m_strNetTaxPriceKey = strKeys[i];
      }
      else if (nDescriptions[i] == PRICE_ORIGINAL) {// 原币扣率单价
        m_strPriceKey = strKeys[i];
      }
      else if (nDescriptions[i] == TAXRATE) {// 税率
        m_strTaxRateKey = strKeys[i];
      }
      else if (nDescriptions[i] == TAX_ORIGINAL) {// 原币税额
        m_strTaxKey = strKeys[i];
      }
      else if (nDescriptions[i] == MONEY_ORIGINAL) {// 无税金额
        m_strMoneyKey = strKeys[i];
      }
      else if (nDescriptions[i] == SUMMNY_ORIGINAL) {// 原币价税合计
        m_strSummnyKey = strKeys[i];
      }
      else if (nDescriptions[i] == HEAD_SUMMNY_ORIGINAL) {// 整单原币价税合计
    	  m_strheadstrSummnyKey = strKeys[i];
        }
      else if (nDescriptions[i] == DISCOUNT_RATE) {// 扣率
        m_strDiscountRateKey = strKeys[i];
      }
      else if (nDescriptions[i] == ALLDISCOUNT_RATE) {// 整单折扣
    	  m_strAllDiscountRateKey = strKeys[i];
        }
      else if (nDescriptions[i] == CONVERT_RATE) {// 换算率
        m_strConvertRateKey = strKeys[i];
      }
      else if (nDescriptions[i] == IS_FIXED_CONVERT) {// 是否固定换算率
    	Object fixedflag = m_voCurr.getAttributeValue(strKeys[i]);
        if (strKeys[i].equals("Y") || new UFBoolean(fixedflag == null ? "N" : fixedflag.toString()).booleanValue() )
          m_bFixedConvertRateKey = true;
        else if (strKeys[i].equals("N") || new UFBoolean(fixedflag == null ? "N" : fixedflag.toString()).booleanValue())
          m_bFixedConvertRateKey = false;
      }
      else if (nDescriptions[i] == QT_CONVERT_RATE) {// 报价换算率
          m_strQt_ConvertRateKey = strKeys[i];
      }
      else if (nDescriptions[i] == QT_IS_FIXED_CONVERT) {// 报价是否固定换算率
    	  Object fixedflag = m_voCurr.getAttributeValue(strKeys[i]);
          if (strKeys[i].equals("Y") || new UFBoolean(fixedflag == null ? "N" : fixedflag.toString()).booleanValue())
            m_bQt_FixedConvertRateKey = true;
          else if (strKeys[i].equals("N") || new UFBoolean(fixedflag == null ? "N" : fixedflag.toString()).booleanValue())
            m_bQt_FixedConvertRateKey = false;
      }
      else if (nDescriptions[i] == TAXPRICE_ORIGINAL) {// 含税单价
        m_strTaxPriceKey = strKeys[i];
      }
      else if (nDescriptions[i] == DISCOUNTMNY_ORIGINA) {// 原币折扣额
    	  m_strDiscountMnyKey = strKeys[i];
      }
      else if (nDescriptions[i] == QT_NUM) {// 原币报价单位数量
    	  m_strQt_NumKey = strKeys[i];
      }
      else if (nDescriptions[i] == QT_TAXPRICE_ORIGINAL) {// 原币报价单位含税单价
    	  m_strQt_TaxPriceKey = strKeys[i];
      }
      else if (nDescriptions[i] == QT_PRICE_ORIGINAL) {// 原币报价单位无税单价
    	  m_strQt_PriceKey = strKeys[i];
      }
      else if (nDescriptions[i] == QT_NET_TAXPRICE_ORIGINAL) {// 原币报价单位含税净价
    	  m_strQt_NetTaxPriceKey = strKeys[i];
      }
      else if (nDescriptions[i] == QT_NET_PRICE_ORIGINAL) {// 原币报价单位无税净价
    	  m_strQt_NetPriceKey = strKeys[i];
      }
      else if (nDescriptions[i] == PRICE_LOCAL) {// 本币无税单价
    	  m_strPrice_LocalKey = strKeys[i];
      }
      else if (nDescriptions[i] == TAXPRICE_LOCAL) {// 本币含税单价 
    	  m_strTaxPrice_LocalKey = strKeys[i];
      }
      else if (nDescriptions[i] == NET_PRICE_LOCAL) {// 本币无税净价
    	  m_strNet_Price_LocalKey = strKeys[i];
      }
      else if (nDescriptions[i] == NET_TAXPRICE_LOCAL) {// 本币含税净价
    	  m_strNet_TaxPrice_LocalKey = strKeys[i];
      }
      else if (nDescriptions[i] == TAX_LOCAL) {// 本币税额
    	  m_strTax_LocalKey = strKeys[i];
      }
      else if (nDescriptions[i] == MONEY_LOCAL) {// 本币无税金额
    	  m_strMoney_LocalKey = strKeys[i];
      }
      else if (nDescriptions[i] == SUMMNY_LOCAL) {// 本币价税合计
    	  m_strSummny_LocalKey = strKeys[i];
      }
      else if (nDescriptions[i] == DISCOUNTMNY_LOCAL) {//本币折扣额
    	  m_strDiscountMny_LocalKey = strKeys[i];
      }
      else if (nDescriptions[i] == QT_PRICE_LOCAL) {// 报价单位本币无税单价
    	  m_strQt_Price_LocalKey = strKeys[i];
      }
      else if (nDescriptions[i] == QT_TAXPRICE_LOCAL) {// 报价单位本币含税单价
    	  m_strQt_TaxPrice_LocalKey = strKeys[i];
      }
      else if (nDescriptions[i] == QT_NET_PRICE_LOCAL) {// 报价单位本币无税净价
    	  m_strQt_Net_Price_LocalKey = strKeys[i];
      }
      else if (nDescriptions[i] == QT_NET_TAXPRICE_LOCAL) {//报价单位本币含税净价
    	  m_strQt_Net_TaxPrice_LocalKey = strKeys[i];
      }
      else if (nDescriptions[i] == PRICE_FRACTIONAL) {// 辅币无税单价
    	  m_strPrice_FractionalKey = strKeys[i];
      }
      else if (nDescriptions[i] == TAXPRICE_FRACTIONAL) {// 辅币含税单价 
    	  m_strTaxPrice_FractionalKey = strKeys[i];
      }
      else if (nDescriptions[i] == NET_PRICE_FRACTIONAL) {// 辅币无税净价
    	  m_strNet_Price_FractionalKey = strKeys[i];
      }
      else if (nDescriptions[i] == NET_TAXPRICE_FRACTIONAL) {// 辅币含税净价
    	  m_strNet_TaxPrice_FractionalKey = strKeys[i];
      }
      else if (nDescriptions[i] == TAX_FRACTIONAL) {// 辅币税额
    	  m_strTax_FractionalKey = strKeys[i];
      }
      else if (nDescriptions[i] == MONEY_FRACTIONAL) {// 辅币无税金额
    	  m_strMoney_FractionalKey = strKeys[i];
      }
      else if (nDescriptions[i] == SUMMNY_FRACTIONAL) {// 辅币价税合计
    	  m_strSummny_FractionalKey = strKeys[i];
      }
      else if (nDescriptions[i] == DISCOUNTMNY_FRACTIONAL) {//辅币折扣额
    	  m_strDiscountMny_FractionalKey = strKeys[i];
      }
      else if (nDescriptions[i] == ASSIST_PRICE_ORIGINAL) {// 辅计量无税单价
    	  m_strAssist_Price = strKeys[i];
      }
      else if (nDescriptions[i] == ASSIST_TAXPRICE_ORIGINAL) {//辅计量含税单价
    	  m_strAssist_TaxPrice = strKeys[i];
      }
      else if (nDescriptions[i] == ASK_TAXPRICE) {// 询价原币含税单价
    	  m_strAsk_TaxPriceKey = strKeys[i];
      }
      else if (nDescriptions[i] == ASK_PRICE) {//询价原币无税单价
    	  m_strAsk_PriceKey = strKeys[i];
      }
      else if (nDescriptions[i] == CURRTYPEPk) {// 原币PK
    	  m_strCurrTypePkKey = strKeys[i];
      }
      else if (nDescriptions[i] == PK_CORP) {// PK_CORP
    	  m_strPk_CorpKey = strKeys[i];
      }
      else if (nDescriptions[i] == EXCHANGE_O_TO_BRATE) {// 折本汇率
    	  m_strExchange_O_TO_BrateKey = strKeys[i];
      }
      else if (nDescriptions[i] == EXCHANGE_O_TO_ARATE) {//折辅汇率
    	  m_strExchange_O_TO_ArateKey = strKeys[i];
      }
      else if (nDescriptions[i] == BILLDATE) {//单据日期
    	  m_strBillDateKey = strKeys[i];
      }
      else if (nDescriptions[i] == UNITID) {//主计量单位ID
    	  m_strUnitID = strKeys[i];
      }
      else if (nDescriptions[i] == QUOTEUNITID) {//报价计量单位ID
    	  m_strQuoteUnitID = strKeys[i];
      }
      
      if (debug)
        //输出入口参数值，便于调试
        SCMEnv.out(getChineseName(strKeys[i])+"["+strKeys[i]+"]："+getObject(strKeys[i]));
      
    }
  }

  /**
   * 作者：王印芬 功能：保留旧值，以利回滚 参数：无 返回：单价、含税单价哪个优先 例外：无 日期：(2002-06-09 11:22:51)
   * 修改日期，修改人，修改原因，注释标志：
   */
  private void initObjects() {

    // 数量
    // if(m_pnlBill.getBodyItem(m_strNumKey) != null ){
    m_objOldNum = m_voCurr.getAttributeValue(m_strNumKey);
    if (getFirstChangedKey().equals(m_strNumKey)) {
      m_objOldNum = m_voCurr.getAttributeValue(getFirstChangedKey());
    }
    // }
    // 净单价
    // if(m_pnlBill.getBodyItem(m_strNetPriceKey) != null ){
    m_objOldNetPrice = m_voCurr.getAttributeValue(m_strNetPriceKey);
    if (getFirstChangedKey().equals(m_strNetPriceKey)) {
      m_objOldNetPrice = m_voCurr.getAttributeValue(getFirstChangedKey());
    }
    // }
    // 金额
    // if(m_pnlBill.getBodyItem(m_strMoneyKey) != null ){
    m_objOldMoney = m_voCurr.getAttributeValue(m_strMoneyKey);
    if (getFirstChangedKey().equals(m_strMoneyKey)) {
      m_objOldMoney = m_voCurr.getAttributeValue(getFirstChangedKey());
    }
    // }
    // 含税净单价
    // if(m_pnlBill.getBodyItem(m_strNetTaxPriceKey) != null ){
    m_objOldNetTaxPrice = m_voCurr.getAttributeValue(m_strNetTaxPriceKey);
    if (getFirstChangedKey().equals(m_strNetTaxPriceKey)) {
      m_objOldNetTaxPrice = m_voCurr.getAttributeValue(getFirstChangedKey());
    }
    // }
    // 税率
    // if(m_pnlBill.getBodyItem(m_strTaxRateKey) != null ){
    m_objOldTaxrate = m_voCurr.getAttributeValue(m_strTaxRateKey);
    if (getFirstChangedKey().equals(m_strTaxRateKey)) {
      m_objOldTaxrate = m_voCurr.getAttributeValue(getFirstChangedKey());
    }
    // }
    // 税额
    // if(m_pnlBill.getBodyItem(m_strTaxKey) != null ){
    m_objOldTax = m_voCurr.getAttributeValue(m_strTaxKey);
    if (getFirstChangedKey().equals(m_strTaxKey)) {
      m_objOldTax = m_voCurr.getAttributeValue(getFirstChangedKey());
    }
    // }
    // 价税合计
    // if(m_pnlBill.getBodyItem(m_strSummnyKey) != null ){
    m_objOldSummny = m_voCurr.getAttributeValue(m_strSummnyKey);
    if (getFirstChangedKey().equals(m_strSummnyKey)) {
      m_objOldSummny = m_voCurr.getAttributeValue(getFirstChangedKey());
    }
    // }
    // 扣率
    // if(m_pnlBill.getBodyItem(m_strDiscountRateKey) != null ){
    m_objOldDiscountRate = m_voCurr.getAttributeValue(m_strDiscountRateKey);
    if (getFirstChangedKey().equals(m_strDiscountRateKey)) {
      m_objOldDiscountRate = m_voCurr.getAttributeValue(getFirstChangedKey());
    }
    // }
    // 单价
    // if(m_pnlBill.getBodyItem(m_strPriceKey) != null ){
    m_objOldPrice = m_voCurr.getAttributeValue(m_strPriceKey);
    if (getFirstChangedKey().equals(m_strPriceKey)) {
      m_objOldPrice = m_voCurr.getAttributeValue(getFirstChangedKey());
    }
    // }
    // 合格数量
    // if(m_pnlBill.getBodyItem(m_strQualifiedNumKey) != null ){
    m_objOldQualifiedNum = m_voCurr.getAttributeValue(m_strQualifiedNumKey);
    if (getFirstChangedKey().equals(m_strQualifiedNumKey)) {
      m_objOldQualifiedNum = m_voCurr.getAttributeValue(getFirstChangedKey());
    }
    // }
    // 不合格数量
    // if(m_pnlBill.getBodyItem(m_strUnQualifiedNumKey) != null ){
    m_objOldUnQualifiedNum = m_voCurr.getAttributeValue(m_strUnQualifiedNumKey);
    if (getFirstChangedKey().equals(m_strUnQualifiedNumKey)) {
      m_objOldUnQualifiedNum = m_voCurr.getAttributeValue(getFirstChangedKey());
    }
    // }
    // 辅数量
    // if(m_pnlBill.getBodyItem(m_strAssistNumKey) != null ){
    m_objOldAssistNum = m_voCurr.getAttributeValue(m_strAssistNumKey);
    if (getFirstChangedKey().equals(m_strAssistNumKey)) {
      m_objOldAssistNum = m_voCurr.getAttributeValue(getFirstChangedKey());
    }
    // }
    // 换算率
    // if(m_pnlBill.getBodyItem(m_strConvertRateKey) != null ){
    m_objOldConvertRate = m_voCurr.getAttributeValue(m_strConvertRateKey);
    if (getFirstChangedKey().equals(m_strConvertRateKey)) {
      m_objOldConvertRate = m_voCurr.getAttributeValue(getFirstChangedKey());
    }
    // }
    // 含税单价
    // if(m_pnlBill.getBodyItem(m_strTaxPriceKey) != null ){
    m_objOldTaxPrice = m_voCurr.getAttributeValue(m_strTaxPriceKey);
    if (getFirstChangedKey().equals(m_strTaxPriceKey)) {
      m_objOldTaxPrice = m_voCurr.getAttributeValue(getFirstChangedKey());
    }
    // }
  }
  
  /**
   * 1.默认不进行本币\辅币的计算;
   * 2.如果没有原币PK、PK_Corp、折本/折辅汇率、单据日期，不进行本币\辅币的计算;
   * 3.如果原币价税合计、无税金额均为0或NULL时，不进行本币\辅币的计算;
   * @return 是否可以进行本币\辅币的计算
   */
   private boolean isCalLocalFrac_CurrEnable(){
	  if (m_iPrior_LOCAL_FRAC == RelationsCalVO.NO_LOCAL_FRAC
			    || getFirstChangedKey().equals(m_strSummny_LocalKey)
			    || getFirstChangedKey().equals(m_strMoney_LocalKey)
				|| m_strPk_CorpKey.equals("")
				|| m_strCurrTypePkKey.equals("")
				|| m_strBillDateKey.equals("")
				|| m_strExchange_O_TO_BrateKey.equals(""))
				//|| m_strExchange_O_TO_ArateKey.equals("")
				//|| m_strSummnyKey.equals("")
				//|| m_strMoneyKey.equals("")
				//|| (getUFDouble(m_strSummnyKey) == null && getUFDouble(m_strMoneyKey) == null))
			return false;
		return true;
  }

   /**
    * @return 判断相关字段是否存在
    */
    private boolean isCalFieldExist(String[] keys){
 	  for (String key : keys ){
 		 if (key.equals(""))
  			return false;  
 	  }	  
 	  return true;
   } 
   
  /**
   * 作者：王印芬 功能：根据改变的项，判断一个CIRCLE是否可执行 参数：int circle CIRCLE值 String changedKey
   * 该CIRCLE的进入项 返回：boolean true可执行;false不可执行 例外：无 日期：(2001-05-27 11:39:21)
   * 修改日期，修改人，修改原因，注释标志：
   */
  private boolean isCircleExecutable(int circle, String sChangedKey) {

    if (circle == CIRCLE_NUM_NETPRICE_MONEY) {// 数量,净单价,金额
      if (m_strNumKey.equals("") || m_strMoneyKey.equals("")
          || m_strNetPriceKey.equals("")) {
        return false;
      }

      if (sChangedKey.equals(m_strNumKey)) {
        if (getUFDouble(m_strNetPriceKey) == null
            && getUFDouble(m_strMoneyKey) == null) {
          return false;
        }
        else {
          return true;
        }
      }
      else if (sChangedKey.equals(m_strNetPriceKey)) {
        if (getUFDouble(m_strNumKey) == null
            && getUFDouble(m_strMoneyKey) == null) {
          return false;
        }
        else {
          return true;
        }
      }
      else if (sChangedKey.equals(m_strMoneyKey)) {
        if (getUFDouble(m_strNetPriceKey) == null
            && getUFDouble(m_strNumKey) == null) {
          return false;
        }
        else {
          return true;
        }
      }
    }
    else if (circle == CIRCLE_TAXRATE) {// 税率,金额,税额,价税合计
      // if( m_strTax.equals("") || m_strMoney.equals("") ||
      // m_strNetPrice.equals("") ){
      // return false ;
      // }

      if (sChangedKey.equals(m_strTaxRateKey)) {
        if (getUFDouble(m_strMoneyKey) == null
            && getUFDouble(m_strTaxKey) == null
            && getUFDouble(m_strSummnyKey) == null) {
          return false;
        }
        else {
          return true;
        }
      }
      else if(sChangedKey.equals(m_strMoneyKey)){
    	    
    	  /* if (getUFDouble(m_strTaxRateKey) == null)
    	    return false;*/
    	   
    	   return true ;
    	  }

      else if (sChangedKey.equals(m_strTaxKey)) {
        // if( (m_objTaxrate==null || m_objTaxrate.toString().equals("")) &&
        // (m_objMoney==null || m_objMoney.toString().equals("")) &&
        // (m_objSummny==null || m_objSummny.toString().equals("")) ){
        // return false ;
        // }else{
        // return true ;
        // }
        return true;
      }
      else if (sChangedKey.equals(m_strSummnyKey)) {
        // if( (m_objTaxrate==null || m_objTaxrate.toString().equals("")) &&
        // (m_objTax==null || m_objTax.toString().equals("")) &&
        // (m_objMoney==null || m_objMoney.toString().equals("")) ){
        // return false ;
        // }else{
        // return true ;
        // }
        return true;
      }
    }
    else if (circle == CIRCLE_NUM_NETTAXPRICE_SUMMNY) {// 数量,净含税单价,价税合计
      if (m_strNumKey.equals("") || m_strNetTaxPriceKey.equals("")
          || m_strSummnyKey.equals("")) {
        return false;
      }

      if (sChangedKey.equals(m_strNumKey)) {
        if (getUFDouble(m_strNetTaxPriceKey) == null
            && getUFDouble(m_strSummnyKey) == null) {
          return false;
        }
        else {
          return true;
        }
      }
      else if (sChangedKey.equals(m_strNetTaxPriceKey)) {
        if (getUFDouble(m_strNumKey) == null
            && getUFDouble(m_strSummnyKey) == null) {
          return false;
        }
        else {
          return true;
        }
      }
      else if (sChangedKey.equals(m_strSummnyKey)) {
        if (getUFDouble(m_strNumKey) == null
            && getUFDouble(m_strNetTaxPriceKey) == null) {
          return false;
        }
        else {
          return true;
        }
      }
    }
    else if (circle == CIRCLE_PRICE_NETPRICE) {// 扣率,无税净价,无税单价
      if (m_strNetPriceKey.equals("") || m_strPriceKey.equals("")
          || m_strDiscountRateKey.equals("")) {
        return false;
      }

      if (sChangedKey.equals(m_strNetPriceKey)) {
        if (getUFDouble(m_strDiscountRateKey) == null
            && getUFDouble(m_strPriceKey) == null) {
          return false;
        }
        else {
          return true;
        }
      }
      else if (sChangedKey.equals(m_strDiscountRateKey)||sChangedKey.equals(m_strAllDiscountRateKey)) {
        if (getUFDouble(m_strNetPriceKey) == null
            && getUFDouble(m_strPriceKey) == null) {
          return false;
        }
        else {
          return true;
        }
      }
      else if (sChangedKey.equals(m_strPriceKey)) {
        if (getUFDouble(m_strDiscountRateKey) == null
            && getUFDouble(m_strNetPriceKey) == null) {
          return false;
        }
        else {
          return true;
        }
      }
    }
    else if (circle == CIRCLE_QUALIFIED_NUM) {// 数量,合格数量,不合格数量
      if (m_strNumKey.equals("") || m_strQualifiedNumKey.equals("")
          || m_strUnQualifiedNumKey.equals("")) {
        return false;
      }

      if (sChangedKey.equals(m_strNumKey)) {
        if (getUFDouble(m_strQualifiedNumKey) == null
            && getUFDouble(m_strUnQualifiedNumKey) == null) {
          return false;
        }
        else {
          return true;
        }
      }
      else if (sChangedKey.equals(m_strQualifiedNumKey)) {
        if (getUFDouble(m_strNumKey) == null
            && getUFDouble(m_strUnQualifiedNumKey) == null) {
          return false;
        }
        else {
          return true;
        }
      }
      else if (sChangedKey.equals(m_strUnQualifiedNumKey)) {
        if (getUFDouble(m_strNumKey) == null
            && getUFDouble(m_strQualifiedNumKey) == null) {
          return false;
        }
        else {
          return true;
        }
      }
    }
    else if (circle == CIRCLE_CONVERT_RATE) {// 主数量,辅数量,换算率
      if (m_strNumKey.equals("") || m_strAssistNumKey.equals("")
          || m_strConvertRateKey.equals("")) {
        return false;
      }

      if (sChangedKey.equals(m_strNumKey)) {
        if (getUFDouble(m_strAssistNumKey) == null
            && getUFDouble(m_strConvertRateKey) == null) {
          return false;
        }
        else {
          return true;
        }
      }
      else if (sChangedKey.equals(m_strAssistNumKey)) {
        if (getUFDouble(m_strNumKey) == null
            && getUFDouble(m_strConvertRateKey) == null) {
          return false;
        }
        else {
          return true;
        }
      }
      else if (sChangedKey.equals(m_strConvertRateKey)) {
        if (getUFDouble(m_strNumKey) == null
            && getUFDouble(m_strAssistNumKey) == null) {
          return false;
        }
        else {
          return true;
        }
      }
    }
    else if (circle == CIRCLE_QT_CONVERT_RATE) {// 主数量,报价数量,报价换算率
        if (m_strNumKey.equals("") || m_strQt_NumKey.equals("")
            || m_strQt_ConvertRateKey.equals("")) {
          return false;
        }

        if (sChangedKey.equals(m_strNumKey)) {
          if (getUFDouble(m_strQt_NumKey) == null
              && getUFDouble(m_strQt_ConvertRateKey) == null) {
            return false;
          }
          else {
            return true;
          }
        }
        else if (sChangedKey.equals(m_strQt_NumKey)) {
          if (getUFDouble(m_strNumKey) == null
              && getUFDouble(m_strQt_ConvertRateKey) == null) {
            return false;
          }
          else {
            return true;
          }
        }
        else if (sChangedKey.equals(m_strQt_ConvertRateKey)) {
          if (getUFDouble(m_strNumKey) == null
              && getUFDouble(m_strQt_NumKey) == null) {
            return false;
          }
          else {
            return true;
          }
        }
    }
    else if (circle == CIRCLE_TAXPRICE_NETTAXPRICE) {// 扣率,含税净价,含税单价
      if (m_strNetTaxPriceKey.equals("") || m_strTaxPriceKey.equals("")
          || m_strDiscountRateKey.equals("")) {
        return false;
      }

      if (sChangedKey.equals(m_strNetTaxPriceKey)) {
        if (getUFDouble(m_strDiscountRateKey) == null
            && getUFDouble(m_strTaxPriceKey) == null) {
          return false;
        }
        else {
          return true;
        }
      }
      else if (sChangedKey.equals(m_strDiscountRateKey)||sChangedKey.equals(m_strAllDiscountRateKey)) {
        if (getUFDouble(m_strNetTaxPriceKey) == null
            && getUFDouble(m_strTaxPriceKey) == null) {
          return false;
        }
        else {
          return true;
        }
      }
      else if (sChangedKey.equals(m_strTaxPriceKey)) {
        if (getUFDouble(m_strDiscountRateKey) == null
            && getUFDouble(m_strNetTaxPriceKey) == null) {
          return false;
        }
        else {
          return true;
        }
      }
    }
    else if (circle == CIRCLE_TAXPRICE_PRICE) {// 税率,含税单价,单价
      if (m_strTaxRateKey.equals("") || m_strTaxPriceKey.equals("")
          || m_strPriceKey.equals("")) {
        return false;
      }

      if (sChangedKey.equals(m_strTaxRateKey)) {
        if (getUFDouble(m_strTaxPriceKey) == null
            && getUFDouble(m_strPriceKey) == null) {
          return false;
        }
        else {
          return true;
        }
      }
      else if (sChangedKey.equals(m_strTaxPriceKey)) {
    	  if (getUFDouble(m_strTaxPriceKey) == null
    	            && getUFDouble(m_strPriceKey) == null) {
    	          return false;
    	  }
    	  else {
    	          return true;
    	  }
      }
      else if (sChangedKey.equals(m_strPriceKey)) {
    	  if (getUFDouble(m_strTaxPriceKey) == null
    	            && getUFDouble(m_strPriceKey) == null) {
    	          return false;
    	  }
    	  else {
    	          return true;
    	  }
      }
    }
    else if (circle == CIRCLE_NETTAXPRICE_NETPRICE) {// 税率,净含税单价,净单价
      if (m_strTaxRateKey.equals("") || m_strNetTaxPriceKey.equals("")
          || m_strNetPriceKey.equals("")) {
        return false;
      }

      if (sChangedKey.equals(m_strTaxRateKey)) {
        if (getUFDouble(m_strNetTaxPriceKey) == null
            && getUFDouble(m_strNetPriceKey) == null) {
          return false;
        }
        else {
          return true;
        }
      }
      else if (sChangedKey.equals(m_strNetTaxPriceKey)) {
    	  if (getUFDouble(m_strNetTaxPriceKey) == null
    	            && getUFDouble(m_strNetPriceKey) == null) {
    	          return false;
    	  }
    	  else {
    	          return true;
    	  }
      }
      else if (sChangedKey.equals(m_strNetPriceKey)) {
    	  if (getUFDouble(m_strNetTaxPriceKey) == null
  	            && getUFDouble(m_strNetPriceKey) == null) {
  	          return false;
  	      }
  	      else {
  	          return true;
  	      }
      }
    }
    else if (circle == CIRCLE_DISCOUNTMNY) {// 含税单价、数量、价税合计、折扣额
        if (m_strTaxPriceKey.equals("") || m_strNumKey.equals("")
            || m_strSummnyKey.equals("") || m_strDiscountMnyKey.equals("") ) {
          return false;
        }
        if (sChangedKey.equals(m_strTaxKey)) {//当前编辑税额时，不计算折扣额
          return false;
        }
        else{
        	if ( getUFDouble(m_strTaxPriceKey) == null || getUFDouble(m_strNumKey) == null
        			|| getUFDouble(m_strSummnyKey) == null) 
        	   return false;
        }
        return true;	
    }
    else if (circle == CIRCLE_VIAPRICE) {// 辅计量含税单价、辅计量无税单价、辅数量、价税合计、无税金额、税率、折扣额
    	if ( sChangedKey.equals(m_strNumKey) || sChangedKey.equals(m_strAssistNumKey)
  			  || sChangedKey.equals(m_strTaxKey) || sChangedKey.equals(m_strQt_NumKey))
  		  return false;
    	if ( m_strAssistNumKey.equals("") || m_strMoneyKey.equals("") ||
  			  m_strSummnyKey.equals("") || m_strTaxRateKey.equals("") ||
  			  m_strAssist_Price.equals("") || m_strAssist_TaxPrice.equals("") )
    	  return false;
    	if ( getUFDouble(m_strTaxRateKey) == null || getUFDouble(m_strAssistNumKey) == null
    			|| getUFDouble(m_strSummnyKey) == null || getUFDouble(m_strMoneyKey) == null)
    		return false;
    	else
    		return true;
    }
    else if (circle == CIRCLE_QT_NETTAXPRICE_NETPRICE) {//税率、报价原币含税净价、报价原币无税净价
        if (m_strQt_NetTaxPriceKey.equals("") || m_strQt_NetPriceKey.equals("")
            || m_strTaxRateKey.equals("") ) {
          return false;
        }
        if (sChangedKey.equals(m_strTaxRateKey)) {
            if (getUFDouble(m_strQt_NetTaxPriceKey) == null
                && getUFDouble(m_strQt_NetPriceKey) == null) {
              return false;
            }
            else {
              return true;
            }
        }
        else if (sChangedKey.equals(m_strQt_NetTaxPriceKey)) {
            return true;
        }
        else if (sChangedKey.equals(m_strQt_NetPriceKey)) {
            return true;
        }
    }
    else if (circle == CIRCLE_QT_TAXPRICE_PRICE) {//税率、报价原币含税单价、报价原币无税单价
        if (m_strQt_TaxPriceKey.equals("") || m_strQt_PriceKey.equals("")
            || m_strTaxRateKey.equals("") ) {
          return false;
        }
        if (sChangedKey.equals(m_strTaxRateKey)) {
            if (getUFDouble(m_strQt_TaxPriceKey) == null
                && getUFDouble(m_strQt_PriceKey) == null) {
              return false;
            }
            else {
              return true;
            }
        }
        else if (sChangedKey.equals(m_strQt_TaxPriceKey)) {
            return true;
        }
        else if (sChangedKey.equals(m_strQt_PriceKey)) {
            return true;
        }
    }
    else if (circle == CIRCLE_QT_TAXPRICE_NETTAXPRICE) {// 扣率、报价原币含税单价、报价原币含税净价
        if (m_strQt_NetTaxPriceKey.equals("") || m_strQt_TaxPriceKey.equals("")
            || m_strDiscountRateKey.equals("")) {
          return false;
        }

        if (sChangedKey.equals(m_strQt_NetTaxPriceKey)) {
          if (getUFDouble(m_strDiscountRateKey) == null
              && getUFDouble(m_strQt_TaxPriceKey) == null) {
            return false;
          }
          else {
            return true;
          }
        }
        else if (sChangedKey.equals(m_strDiscountRateKey)) {
          if (getUFDouble(m_strQt_NetTaxPriceKey) == null
              && getUFDouble(m_strQt_TaxPriceKey) == null) {
            return false;
          }
          else {
            return true;
          }
        }
        else if (sChangedKey.equals(m_strQt_TaxPriceKey)) {
          if (getUFDouble(m_strDiscountRateKey) == null
              && getUFDouble(m_strQt_NetTaxPriceKey) == null) {
            return false;
          }
          else {
            return true;
          }
        }
      }
    else if (circle == CIRCLE_QT_PRICE_NETPRICE) {// 扣率、报价原币无税单价、报价原币无税净价
        if (m_strQt_NetPriceKey.equals("") || m_strQt_PriceKey.equals("")
            || m_strDiscountRateKey.equals("")) {
          return false;
        }

        if (sChangedKey.equals(m_strQt_NetPriceKey)) {
          if (getUFDouble(m_strDiscountRateKey) == null
              && getUFDouble(m_strQt_PriceKey) == null) {
            return false;
          }
          else {
            return true;
          }
        }
        else if (sChangedKey.equals(m_strDiscountRateKey)) {
          if (getUFDouble(m_strQt_NetPriceKey) == null
              && getUFDouble(m_strQt_PriceKey) == null) {
            return false;
          }
          else {
            return true;
          }
        }
        else if (sChangedKey.equals(m_strQt_PriceKey)) {
          if (getUFDouble(m_strDiscountRateKey) == null
              && getUFDouble(m_strQt_NetPriceKey) == null) {
            return false;
          }
          else {
            return true;
          }
        }
      }
    else if (circle == CIRCLE_QT_NUM_NETTAXPRICE_SUMMNY) {// 报价数量、报价原币含税净价、价税合计
        if (m_strQt_NumKey.equals("") || m_strQt_NetTaxPriceKey.equals("")
            || m_strSummnyKey.equals("")) {
          return false;
        }

        if (sChangedKey.equals(m_strQt_NumKey)) {
          if (getUFDouble(m_strQt_NetTaxPriceKey) == null
              && getUFDouble(m_strSummnyKey) == null) {
            return false;
          }
          else {
            return true;
          }
        }
        else if (sChangedKey.equals(m_strQt_NetTaxPriceKey)) {
          if (getUFDouble(m_strQt_NumKey) == null
              && getUFDouble(m_strSummnyKey) == null) {
            return false;
          }
          else {
            return true;
          }
        }
        else if (sChangedKey.equals(m_strSummnyKey)) {
          if (getUFDouble(m_strQt_NumKey) == null
              && getUFDouble(m_strQt_NetTaxPriceKey) == null) {
            return false;
          }
          else {
            return true;
          }
        }
      }
    else if (circle == CIRCLE_QT_NUM_NETPRICE_MONEY) {// 报价数量、报价原币无税净价、无税金额
        if (m_strQt_NumKey.equals("") || m_strQt_NetPriceKey.equals("")
            || m_strMoneyKey.equals("")) {
          return false;
        }

        if (sChangedKey.equals(m_strQt_NumKey)) {
          if (getUFDouble(m_strQt_NetPriceKey) == null
              && getUFDouble(m_strMoneyKey) == null) {
            return false;
          }
          else {
            return true;
          }
        }
        else if (sChangedKey.equals(m_strQt_NetPriceKey)) {
          if (getUFDouble(m_strQt_NumKey) == null
              && getUFDouble(m_strMoneyKey) == null) {
            return false;
          }
          else {
            return true;
          }
        }
        else if (sChangedKey.equals(m_strMoneyKey)) {
          if (getUFDouble(m_strQt_NumKey) == null
              && getUFDouble(m_strQt_NetPriceKey) == null) {
            return false;
          }
          else {
            return true;
          }
        }
    }
    else if (circle == CIRCLE_LOCAL_SUMMNY_MNY_TAX) {// 本币价税合计、本币无税金额、税额
    	if (m_strSummny_LocalKey.equals("") || m_strMoney_LocalKey.equals("")
                || m_strTax_LocalKey.equals("") ) {
              return false;
    	}else
    		return true;
    }

    return false;
  }

  /**
   * 作者：王印芬 功能：判断某CIRCLE是否已执行 参数：无 返回：boolean true已执行;false未执行 例外：无
   * 日期：(2001-05-27 11:39:21) 修改日期，修改人，修改原因，注释标志：
   */
  private boolean isCircleExecuted(int circle) {

    return m_bCircleExecuted[circle - 1];
  }

  /**
   * 作者：王印芬 功能：是否固定换算率 参数：panel 返回： 例外：无 日期：(2001-05-27 11:39:21)
   * 修改日期，修改人，修改原因，注释标志：
   */
  private boolean isFixedConvertRate() {

    return m_bFixedConvertRateKey;
  }
  
  private boolean isQt_FixedConvertRate() {

	    return m_bQt_FixedConvertRateKey;
  }

  /**
   * 作者：王印芬 功能：判断一个KEY是否曾改变过。如果曾改变，则不可再改变 参数：无 返回：boolean true，可改变；false，不可改变
   * 例外：无 日期：(2003-6-10 11:39:21) 修改日期，修改人，修改原因，注释标志：
   */
  private boolean isKeyExecuted(String sKey) {

    if (sKey.equals(m_strAssistNumKey)) {
      return m_baEverChangedAssistNum;
    }
    else if (sKey.equals(m_strConvertRateKey)) {
      return m_baEverChangedConvertRate;
    }
    else if (sKey.equals(m_strDiscountRateKey)) {
      return m_baEverChangedDiscountRate;
    }
    else if (sKey.equals(m_strMoneyKey)) {
      return m_baEverChangedMoney;
    }
    else if (sKey.equals(m_strNumKey)) {
      return m_baEverChangedNum;
    }
    else if (sKey.equals(m_strNetPriceKey)) {
      return m_baEverChangedNetPrice;
    }
    else if (sKey.equals(m_strPriceKey)) {
      return m_baEverChangedPrice;
    }
    else if (sKey.equals(m_strNetTaxPriceKey)) {
      return m_baEverChangedNetTaxPrice;
    }
    else if (sKey.equals(m_strQualifiedNumKey)) {
      return m_baEverChangedQualifiedNum;
    }
    else if (sKey.equals(m_strSummnyKey)) {
      return m_baEverChangedSummny;
    }
    else if (sKey.equals(m_strTaxKey)) {
      return m_baEverChangedTax;
    }
    else if (sKey.equals(m_strTaxRateKey)) {
      return m_baEverChangedTaxRate;
    }
    else if (sKey.equals(m_strUnQualifiedNumKey)) {
      return m_baEverChangedUnQualifiedNum;
    }
    else if (sKey.equals(m_strTaxPriceKey)) {
      return m_baEverChangedTaxPrice;
    }
    else if (sKey.equals(m_strQt_NumKey)) {
        return m_baEverChangedQt_Num;
    }
    else if (sKey.equals(m_strQt_ConvertRateKey)) {
        return m_baEverChangedQt_ConvertRate;
    }
    else if (sKey.equals(m_strQt_TaxPriceKey)) {
        return m_baEverChangedQt_TaxPrice;
    }
    else if (sKey.equals(m_strQt_PriceKey)) {
        return m_baEverChangedQt_Price;
    }
    else if (sKey.equals(m_strQt_NetTaxPriceKey)) {
        return m_baEverChangedQt_Net_TaxPrice;
    }
    else if (sKey.equals(m_strQt_NetPriceKey)) {
        return m_baEverChangedQt_Net_Price;
    }
    else if (sKey.equals(m_strSummny_LocalKey)) {
        return m_baEverChangedLocal_Summny;
    }
    else if (sKey.equals(m_strMoney_LocalKey)) {
        return m_baEverChangedLocal_Mny;
    }
    else if (sKey.equals(m_strExchange_O_TO_BrateKey)) {
        return m_baEverChangedLocal_ExchangeBrate;
    }
    
    return false;
  }

  /**
   * 作者：王印芬 功能：计算完毕后，判断是否所有的项都合理 参数： 返回： 例外：无 日期：(2003-05-27 11:39:21)
   * 修改日期，修改人，修改原因，注释标志：
   */
  private void isResultsRight() {
    m_strShowHintMsg = null;
    // 净单价不能为负
    // if(m_pnlBill.getBodyItem(m_strNetPriceKey) != null ){
    if (getUFDouble(m_strNetPriceKey) != null
        && getUFDouble(m_strNetPriceKey).doubleValue() < 0.0) {
      // MessageDialog.showHintDlg(m_pnlBill,nc.ui.ml.NCLangRes.getInstance().getStrByID("scmcommon","UPPSCMCommon-000270")/*@res
      // "提示"*/,m_pnlBill.getBodyItem(m_strNetPriceKey).getName()+nc.ui.ml.NCLangRes.getInstance().getStrByID("scmpub","UPPscmpub-000507")/*@res
      // "不能为负，请重新输入。"*/) ;
      m_strShowHintMsg = m_strNetPriceName
          + NCLangRes4VoTransl.getNCLangRes().getStrByID("scmpub",
              "UPPscmpub-000507")/* @res "不能为负，请重新输入。" */;
      // return false ;
    }
    // }
    // 含税净单价不为负
    // if(m_pnlBill.getBodyItem(m_strNetTaxPriceKey) != null ){
    if (getUFDouble(m_strNetTaxPriceKey) != null
        && getUFDouble(m_strNetTaxPriceKey).doubleValue() < 0.0) {
      // MessageDialog.showHintDlg(m_pnlBill,nc.ui.ml.NCLangRes.getInstance().getStrByID("scmcommon","UPPSCMCommon-000270")/*@res
      // "提示"*/,m_pnlBill.getBodyItem(m_strNetTaxPriceKey).getName()+nc.ui.ml.NCLangRes.getInstance().getStrByID("scmpub","UPPscmpub-000507")/*@res
      // "不能为负，请重新输入。"*/) ;
      // return false ;
      m_strShowHintMsg = m_strNetTaxPriceName
          + NCLangRes4VoTransl.getNCLangRes().getStrByID("scmpub",
              "UPPscmpub-000507")/* @res "不能为负，请重新输入。" */;
    }
    // }
    // 单价不为负
    // if(m_pnlBill.getBodyItem(m_strPriceKey) != null ){
    if (getUFDouble(m_strPriceKey) != null
        && getUFDouble(m_strPriceKey).doubleValue() < 0.0) {
      // MessageDialog.showHintDlg(m_pnlBill,nc.ui.ml.NCLangRes.getInstance().getStrByID("scmcommon","UPPSCMCommon-000270")/*@res
      // "提示"*/,m_pnlBill.getBodyItem(m_strPriceKey).getName()+nc.ui.ml.NCLangRes.getInstance().getStrByID("scmpub","UPPscmpub-000507")/*@res
      // "不能为负，请重新输入。"*/) ;
      // return false ;
      m_strShowHintMsg = m_strPriceName
          + NCLangRes4VoTransl.getNCLangRes().getStrByID("scmpub",
              "UPPscmpub-000507")/* @res "不能为负，请重新输入。" */;
    }
    // }
    // 税率不为负
    // if(m_pnlBill.getBodyItem(m_strTaxRateKey) != null ){
    if (getUFDouble(m_strTaxRateKey) != null
        && getUFDouble(m_strTaxRateKey).doubleValue() < 0.0) {
      // MessageDialog.showHintDlg(m_pnlBill,nc.ui.ml.NCLangRes.getInstance().getStrByID("scmcommon","UPPSCMCommon-000270")/*@res
      // "提示"*/,m_pnlBill.getBodyItem(m_strTaxRateKey).getName()+nc.ui.ml.NCLangRes.getInstance().getStrByID("scmpub","UPPscmpub-000507")/*@res
      // "不能为负，请重新输入。"*/) ;
      // return false ;
      m_strShowHintMsg = m_strTaxRateName
          + NCLangRes4VoTransl.getNCLangRes().getStrByID("scmpub",
              "UPPscmpub-000507")/* @res "不能为负，请重新输入。" */;
    }
    // }
    // 扣率不为负,(0,100)
    // if(m_pnlBill.getBodyItem(m_strDiscountRateKey) != null ){
    if (getUFDouble(m_strDiscountRateKey) != null) {
      if (getUFDouble(m_strDiscountRateKey).doubleValue() < 0.0) {
        // MessageDialog.showHintDlg(m_pnlBill,nc.ui.ml.NCLangRes.getInstance().getStrByID("scmcommon","UPPSCMCommon-000270")/*@res
        // "提示"*/,nc.ui.ml.NCLangRes.getInstance().getStrByID("scmpub","UPPscmpub-000508")/*@res
        // "扣率不能为负，请重新输入。"*/) ;
        // return false ;
        m_strShowHintMsg = m_strDiscountRateName
            + NCLangRes4VoTransl.getNCLangRes().getStrByID("scmpub",
                "UPPscmpub-000507")/* @res "不能为负，请重新输入。" */;
      }
      // else if( getUFDouble(m_strDiscountRate).doubleValue()>100.0 ){
      // MessageDialog.showHintDlg(m_pnlBill,"提示","扣率不能大于100.0，请重新输入。") ;
      // return false ;
      // }
    }
    // }
    // 换算率不为负
    // if(m_pnlBill.getBodyItem(m_strConvertRateKey) != null ){
    if (getUFDouble(m_strConvertRateKey) != null
        && getUFDouble(m_strConvertRateKey).doubleValue() < 0.0) {
      // MessageDialog.showHintDlg(m_pnlBill,nc.ui.ml.NCLangRes.getInstance().getStrByID("scmcommon","UPPSCMCommon-000270")/*@res
      // "提示"*/,m_pnlBill.getBodyItem(m_strConvertRateKey).getName()+nc.ui.ml.NCLangRes.getInstance().getStrByID("scmpub","UPPscmpub-000507")/*@res
      // "不能为负，请重新输入。"*/) ;
      // return false ;
      m_strShowHintMsg = m_strConvertRateName
          + NCLangRes4VoTransl.getNCLangRes().getStrByID("scmpub",
              "UPPscmpub-000507")/* @res "不能为负，请重新输入。" */;
    }
    // }
    // 含税单价不为负
    // if(m_pnlBill.getBodyItem(m_strTaxPriceKey) != null ){
    if (getUFDouble(m_strTaxPriceKey) != null
        && getUFDouble(m_strTaxPriceKey).doubleValue() < 0.0) {
      // MessageDialog.showHintDlg(m_pnlBill,nc.ui.ml.NCLangRes.getInstance().getStrByID("scmcommon","UPPSCMCommon-000270")/*@res
      // "提示"*/,m_pnlBill.getBodyItem(m_strTaxPriceKey).getName()+nc.ui.ml.NCLangRes.getInstance().getStrByID("scmpub","UPPscmpub-000507")/*@res
      // "不能为负，请重新输入。"*/) ;
      // return false ;
      m_strShowHintMsg = m_strTaxPriceName
          + NCLangRes4VoTransl.getNCLangRes().getStrByID("scmpub",
              "UPPscmpub-000507")/* @res "不能为负，请重新输入。" */;
    }
    // }

    // return true ;
  }

  /**
   * 作者：王印芬 功能：计算完成后，重置数据 参数： 返回： 例外：无 日期：(2001-05-27 11:39:21)
   * 修改日期，修改人，修改原因，注释标志：
   */

  private void processAferCalReSetZeroAndValue() {
    UFDouble dValue = null;
    // 数量
    // if(m_pnlBill.getBodyItem(m_strNumKey) != null ){
    dValue = getUFDoubleZeroAsNull(m_strNumKey);
    // if( dValue!=null && dValue.compareTo(VariableConst.ZERO)==0 ){
    // m_pnlBill.setBodyValueAt(null,m_iPos,m_strNum) ;
    // }else{
    m_voCurr.setAttributeValue(m_strNumKey, dValue);
    // }
    // }
    // 单价
    // if(m_pnlBill.getBodyItem(m_strNetPriceKey) != null ){
    dValue = getUFDoubleZeroAsNull(m_strNetPriceKey);

    // SEG需求，要求对0.00不做处理以下类似
    // if( dValue!=null && dValue.compareTo(VariableConst.ZERO)==0 ){
    // m_pnlBill.setBodyValueAt(null,m_iPos,m_strNetPrice) ;
    // }
    // else{
    m_voCurr.setAttributeValue(m_strNetPriceKey, dValue);
    // }
    // }

    // 金额
    // if(m_pnlBill.getBodyItem(m_strMoneyKey) != null ){
    dValue = getUFDoubleZeroAsNull(m_strMoneyKey);
    // if( dValue!=null) && dValue.compareTo(VariableConst.ZERO)==0 ){
    // m_pnlBill.setBodyValueAt(null,m_iPos,m_strMoney) ;
    // }
    // else{
    m_voCurr.setAttributeValue(m_strMoneyKey, dValue);
    // }
    // }

    // 含税单价
    // if(m_pnlBill.getBodyItem(m_strNetTaxPriceKey) != null ){
    dValue = getUFDoubleZeroAsNull(m_strNetTaxPriceKey);
    // if( dValue!=null && dValue.compareTo(VariableConst.ZERO)==0 ){
    // m_pnlBill.setBodyValueAt(null,m_iPos,m_strNetTaxPrice) ;
    // }
    // else{
    m_voCurr.setAttributeValue(m_strNetTaxPriceKey, dValue);
    // }
    // }

    // 税率
    // if(m_pnlBill.getBodyItem(m_strTaxRateKey) != null ){
    dValue = getUFDoubleZeroAsNull(m_strTaxRateKey);
    // if( dValue!=null && dValue.compareTo(VariableConst.ZERO)==0 ){
    // m_pnlBill.setBodyValueAt(null,m_iPos,m_strTaxRate) ;
    // }else{
    m_voCurr.setAttributeValue(m_strTaxRateKey, dValue);
    // }
    // }

    // 税额
    // if(m_pnlBill.getBodyItem(m_strTaxKey) != null ){
    dValue = getUFDoubleZeroAsNull(m_strTaxKey);
    // if( dValue!=null && dValue.compareTo(VariableConst.ZERO)==0 ){
    // m_pnlBill.setBodyValueAt(null,m_iPos,m_strTax) ;
    // }else{
    m_voCurr.setAttributeValue(m_strTaxKey, dValue);
    // }
    // }

    // 价税合计
    // if(m_pnlBill.getBodyItem(m_strSummnyKey) != null ){
    dValue = getUFDoubleZeroAsNull(m_strSummnyKey);
    // if( dValue!=null && dValue.compareTo(VariableConst.ZERO)==0 ){
    // m_pnlBill.setBodyValueAt(null,m_iPos,m_strSummny) ;
    // }
    // else{
    m_voCurr.setAttributeValue(m_strSummnyKey, dValue);
    // }
    // }

    // 扣率
    // if(m_pnlBill.getBodyItem(m_strDiscountRateKey) != null ){
    dValue = getUFDoubleZeroAsNull(m_strDiscountRateKey);
    // if( dValue!=null && dValue.compareTo(VariableConst.ZERO)==0 ){
    // m_pnlBill.setBodyValueAt(null,m_iPos,m_strDiscountRate) ;
    // }else{
    m_voCurr.setAttributeValue(m_strDiscountRateKey, dValue);
    // }
    // }

    // 扣率单价
    // if(m_pnlBill.getBodyItem(m_strPriceKey) != null ){
    dValue = getUFDoubleZeroAsNull(m_strPriceKey);
    // if( dValue!=null && dValue.compareTo(VariableConst.ZERO)==0 ){
    // m_pnlBill.setBodyValueAt(null,m_iPos,m_strPrice) ;
    // }else{
    m_voCurr.setAttributeValue(m_strPriceKey, dValue);
    // }
    // }
    // 合格数量
    // if(m_pnlBill.getBodyItem(m_strQualifiedNumKey) != null ){
    dValue = getUFDoubleZeroAsNull(m_strQualifiedNumKey);
    // if( dValue!=null && dValue.compareTo(VariableConst.ZERO)==0 ){
    // m_pnlBill.setBodyValueAt(null,m_iPos,m_strQualifiedNum) ;
    // }else{
    m_voCurr.setAttributeValue(m_strQualifiedNumKey, dValue);
    // }
    // }
    // 不合格数量
    // if(m_pnlBill.getBodyItem(m_strUnQualifiedNumKey) != null ){
    dValue = getUFDoubleZeroAsNull(m_strUnQualifiedNumKey);
    // if( dValue!=null && dValue.compareTo(VariableConst.ZERO)==0 ){
    // m_voCurr.setAttributeValue(m_strUnQualifiedNumKey,null) ;
    // }else{
    m_voCurr.setAttributeValue(m_strUnQualifiedNumKey, dValue);
    // }
    // }
    // 辅数量
    // if(m_pnlBill.getBodyItem(m_strAssistNumKey) != null ){
    dValue = getUFDoubleZeroAsNull(m_strAssistNumKey);
    // if( dValue!=null && dValue.compareTo(VariableConst.ZERO)==0 ){
    // m_voCurr.setAttributeValue(m_strAssistNumKey,null) ;
    // }else{
    m_voCurr.setAttributeValue(m_strAssistNumKey, dValue);
    // }
    // }
    // 换算率
    // if(m_pnlBill.getBodyItem(m_strConvertRateKey) != null ){
    dValue = getUFDoubleZeroAsNull(m_strConvertRateKey);
    // if( dValue!=null && dValue.compareTo(VariableConst.ZERO)==0 ){
    // m_voCurr.setAttributeValue(m_strConvertRateKey,dValue) ;
    // }else{
    m_voCurr.setAttributeValue(m_strConvertRateKey, dValue);
    // }
    // }
  }

  /**
   * 作者：王印芬 功能：处理后续事项 参数： 返回： 例外：无 日期：(2001-05-27 11:39:21) 修改日期，修改人，修改原因，注释标志：
   */
  private void processAfterCal() {

    // 合格与不合格数量
    processAfterCalQualifiedNum();
    
    // 如果为零，则显示为空,同时处理小数位（暂时去掉）
    //processAferCalReSetZeroAndValue();
    
    // 设置回公式计算的参数
    processAfterCalFormulaParseModel();

  }

  /**
   * 处理后续事项
   * 
   * @param int
   *          circle CIRCLE值 boolean executedFlag 该CIRCLE的执行标志
   * @return 无
   * @exception 异常描述
   * @see 需要参见的其它内容
   * @since 从类的那一个版本，此方法被添加进来。（可选）
   */
  private void processAfterCalFormulaParseModel() {
    // m_pnlBill.getBillModel().getFormulaParse().setNullAsZero(m_bNullAsZero) ;
    m_fp.setNullAsZero(m_bNullAsZero);

  }
  
  
  /**
   * 如果单价、净价数值相同，则折扣额强制清0
   * false: 折扣额清0，不需要进行计算：折扣额 = 数量 * 含税单价 - 价税合计
   * true:  需要进行计算：折扣额 = 数量 * 含税单价 - 价税合计
   */
  private boolean processAfterCalDiscountMny() {
	  if (m_strDiscountMnyKey == null || m_strDiscountMnyKey.equals(""))
			return true;
	  
	  if ( (m_iPrior_Price_TaxPrice == RelationsCalVO.TAXPRICE_PRIOR_TO_PRICE
			&& (getUFDouble(m_strTaxPriceKey)!=null && getUFDouble(m_strNetTaxPriceKey)!=null && getUFDouble(m_strTaxPriceKey).doubleValue() == getUFDouble(
					m_strNetTaxPriceKey).doubleValue())) || 
			(m_iPrior_Price_TaxPrice == RelationsCalVO.PRICE_PRIOR_TO_TAXPRICE
					&& (getUFDouble(m_strPriceKey)!=null && getUFDouble(m_strNetPriceKey)!=null && getUFDouble(m_strPriceKey).doubleValue() == getUFDouble(
							m_strNetPriceKey).doubleValue())) ){
		  m_voCurr.setAttributeValue(m_strDiscountMnyKey, new UFDouble(0.0));
	      m_voCurr.setAttributeValue(m_strDiscountMny_LocalKey, new UFDouble(0.0));
//	      if (getBusinessCurrencyRateUtil().isBlnLocalFrac())
	      if (CurrParamQuery.getInstance().isBlnLocalFrac(getCorpId()))
	    	  m_voCurr.setAttributeValue(m_strDiscountMny_FractionalKey, new UFDouble(0.0));
	      return false;
	  }
	  
	  return true;
  }

  /**
   * 处理后续事项
   * 
   * @param int
   *          circle CIRCLE值 boolean executedFlag 该CIRCLE的执行标志
   * @return 无
   * @exception 异常描述
   * @see 需要参见的其它内容
   * @since 从类的那一个版本，此方法被添加进来。（可选）
   */
  private void processAfterCalQualifiedNum() {

    if (m_strNumKey != null && !m_strNumKey.trim().equals("")) {
      UFDouble dValue = getUFDouble(m_strNumKey);
      if (dValue != null && dValue.doubleValue() >= 0.0) {
        if (m_strQualifiedNumKey != null
            && !m_strQualifiedNumKey.trim().equals("")) {
          m_voCurr.setAttributeValue(m_strQualifiedNumKey, new UFDouble(0.0));
        }
        if (m_strUnQualifiedNumKey != null
            && !m_strUnQualifiedNumKey.trim().equals("")) {
          m_voCurr.setAttributeValue(m_strUnQualifiedNumKey, new UFDouble(0.0));
        }
      }
    }
  }

  /**
   * 设置一个CIRCLE的执行标志
   * 
   * @param int
   *          circle CIRCLE值 boolean executedFlag 该CIRCLE的执行标志
   * @return 无
   * @exception 异常描述
   * @see 需要参见的其它内容
   * @since 从类的那一个版本，此方法被添加进来。（可选）
   */
  private void setCircleExecuted(int circle, boolean executedFlag) {
    m_bCircleExecuted[circle - 1] = executedFlag;
  }

  /**
   * @param iaPrior - 为设置最先变动key，而设置含税优先机制 
   * 作者：王印芬 功能：设置最初改变的KEY 参数：无 返回：无 例外：无 日期：(2002-05-27 11:39:21)
   * 修改日期，修改人，修改原因，注释标志：
   * @throws BusinessException 
   */
  private void setFirstChangedKey(String sNewKey,int[] iaPrior) {
	  try {
	  
	//设置含税优先机制
	setPriorForSetFirstChangeKey(iaPrior);
	  
    // 如果改变的KEY为扣税类别，则认为是税率改变，触发变化
    if (sNewKey.equals(getKeyByDescription(DISCOUNT_TAX_TYPE_KEY))) {
      // 设税率为变化KEY
      m_strChangedKey = getKeyByDescription(TAXRATE);
    }
    //折本汇率、原币币种变化，则认为金额变化
    else if (sNewKey.equals(getKeyByDescription(EXCHANGE_O_TO_BRATE))
    		||sNewKey.equals(getKeyByDescription(CURRTYPEPk))){
    	
    	//币种变化取默认汇率，并设置在VO中
    	if (sNewKey.equals(getKeyByDescription(CURRTYPEPk))){
    		
			// 原币PK
			if (m_voCurr.getAttributeValue(m_strCurrTypePkKey)==null && m_hvoCurr != null)
				pk_CurrType = m_hvoCurr.getAttributeValue(m_strCurrTypePkKey).toString();
			else
				pk_CurrType = m_voCurr.getAttributeValue(m_strCurrTypePkKey).toString();
			// 本币PK
			locaCurrType = CurrParamQuery.getInstance().getLocalCurrPK(getCorpId());
			
			//单据日期
			String billDate = null;
			if (m_voCurr.getAttributeValue(m_strBillDateKey)== null )
				billDate = m_hvoCurr.getAttributeValue(m_strBillDateKey).toString();
			else
				billDate = m_voCurr.getAttributeValue(m_strBillDateKey).toString();
    		UFDouble nRate = getBusinessCurrencyRateUtil().getRate(pk_CurrType, locaCurrType, billDate);
    		
    		m_voCurr.setAttributeValue(m_strExchange_O_TO_BrateKey, nRate);
    	}
    	
    	m_strChangedKey = getKeyByDescription(EXCHANGE_O_TO_BRATE);
    }
    else {
      m_strChangedKey = sNewKey;
    }
    	
    } catch (BusinessException e) {
    	nc.vo.scm.pub.SCMEnv.out("获取总账工具类失败\n" + e.getMessage());
	}	
  }

  /**
   * 作者：王印芬 功能：设置一个KEY是否曾改变过。如果曾改变，则不可再改变 参数：String sKey KEY boolean bNewStatus
   * 新状态 返回：无 例外：无 日期：(2003-6-10 11:39:21) 修改日期，修改人，修改原因，注释标志：
   */
  private void setKeyExecuted(String sKey, boolean bNewStatus) {

    if (sKey.equals(m_strAssistNumKey)) {
      m_baEverChangedAssistNum = bNewStatus;
    }
    else if (sKey.equals(m_strConvertRateKey)) {
      m_baEverChangedConvertRate = bNewStatus;
    }
    else if (sKey.equals(m_strDiscountRateKey)) {
      m_baEverChangedDiscountRate = bNewStatus;
    }
    else if (sKey.equals(m_strMoneyKey)) {
      m_baEverChangedMoney = bNewStatus;
    }
    else if (sKey.equals(m_strNumKey)) {
      m_baEverChangedNum = bNewStatus;
    }
    else if (sKey.equals(m_strNetPriceKey)) {
      m_baEverChangedNetPrice = bNewStatus;
    }
    else if (sKey.equals(m_strPriceKey)) {
      m_baEverChangedPrice = bNewStatus;
    }
    else if (sKey.equals(m_strNetTaxPriceKey)) {
      m_baEverChangedNetTaxPrice = bNewStatus;
    }
    else if (sKey.equals(m_strQualifiedNumKey)) {
      m_baEverChangedQualifiedNum = bNewStatus;
    }
    else if (sKey.equals(m_strSummnyKey)) {
      m_baEverChangedSummny = bNewStatus;
    }
    else if (sKey.equals(m_strTaxKey)) {
      m_baEverChangedTax = bNewStatus;
    }
    else if (sKey.equals(m_strTaxRateKey)) {
      m_baEverChangedTaxRate = bNewStatus;
    }
    else if (sKey.equals(m_strUnQualifiedNumKey)) {
      m_baEverChangedUnQualifiedNum = bNewStatus;
    }
    else if (sKey.equals(m_strTaxPriceKey)) {
      m_baEverChangedTaxPrice = bNewStatus;
    }
    else if (sKey.equals(m_strQt_NumKey)) {
        m_baEverChangedQt_Num = bNewStatus;
    }
    else if (sKey.equals(m_strQt_ConvertRateKey)) {
        m_baEverChangedQt_ConvertRate = bNewStatus;
    }
    else if (sKey.equals(m_strQt_TaxPriceKey)) {
        m_baEverChangedQt_TaxPrice = bNewStatus;
    }
    else if (sKey.equals(m_strQt_PriceKey)) {
        m_baEverChangedQt_Price = bNewStatus;
    }
    else if (sKey.equals(m_strQt_NetTaxPriceKey)) {
        m_baEverChangedQt_Net_TaxPrice = bNewStatus;
    }
    else if (sKey.equals(m_strQt_NetPriceKey)) {
        m_baEverChangedQt_Net_Price = bNewStatus;
    }
    else if (sKey.equals(m_strSummny_LocalKey)) {
    	m_baEverChangedLocal_Summny = bNewStatus;
    }
    else if (sKey.equals(m_strMoney_LocalKey)) {
    	m_baEverChangedLocal_Mny = bNewStatus;
    }
    else if (sKey.equals(m_strTax_LocalKey)) {
    	m_baEverChangedLocal_Tax = bNewStatus;
    }
    else if (sKey.equals(m_strExchange_O_TO_BrateKey)) {
    	m_baEverChangedLocal_ExchangeBrate = bNewStatus;
    }
    
  }

  /**
   * 作者：王印芬 功能：根据第一个改变的项,设置哪些项不可改变 参数：无 返回：可执行的第一个有效CIRCLE 例外：无 日期：(2002-5-15
   * 11:39:21) 修改日期，修改人，修改原因，注释标志：
   * 2008-03-07 zhangcheng  需求变更:当编辑价税合计时,根据参数判断调折扣或单价;默认是调折扣,则单价不变
   */
  private void setKeyExecutedForFirstChangedKey(String sFirstChangedKey,
      int iFirstCircle) {

    if (iFirstCircle == CIRCLE_IMPOSSIBLE) {
      return;
    }

    // 数量
    if (sFirstChangedKey.equals(m_strNumKey)) {
      // 固定换算率--辅单位2个价格不变
      if (m_bFixedConvertRateKey){
    	  if (getUFDouble(m_strAssist_Price) != null) {
              setKeyExecuted(m_strAssist_Price, true);
          }
          if (getUFDouble(m_strAssist_TaxPrice) != null) {
              setKeyExecuted(m_strAssist_TaxPrice, true);
          }
      }
      // 报价固定换算率--报价单位4个价格不变
      if (m_bQt_FixedConvertRateKey) {
				if (getUFDouble(m_strQt_TaxPriceKey) != null) {
					setKeyExecuted(m_strQt_TaxPriceKey, true);
				}
				if (getUFDouble(m_strQt_PriceKey) != null) {
					setKeyExecuted(m_strQt_PriceKey, true);
				}
				if (getUFDouble(m_strQt_NetTaxPriceKey) != null) {
					setKeyExecuted(m_strQt_NetTaxPriceKey, true);
				}
				if (getUFDouble(m_strQt_NetPriceKey) != null) {
					setKeyExecuted(m_strQt_NetPriceKey, true);
				}
	  }
      // 主单位4个价格，折扣，税率均不变
      if (getUFDouble(m_strTaxPriceKey) != null) {
 	     setKeyExecuted(m_strTaxPriceKey, true);
 	  }
 	  if (getUFDouble(m_strPriceKey) != null) {
 	     setKeyExecuted(m_strPriceKey, true);
 	  }
 	  if (getUFDouble(m_strNetTaxPriceKey) != null) {
 	      setKeyExecuted(m_strNetTaxPriceKey, true);
 	  }
 	  if (getUFDouble(m_strNetPriceKey) != null) {
 	      setKeyExecuted(m_strNetPriceKey, true);
 	  }
      if (getUFDouble(m_strDiscountRateKey) != null) {
        setKeyExecuted(m_strDiscountRateKey, true);
      }
      if (getUFDouble(m_strTaxRateKey) != null) {
        setKeyExecuted(m_strTaxRateKey, true);
      }
    }
    
    //辅数量
    else if (sFirstChangedKey.equals(m_strAssistNumKey)){
      if (getUFDouble(m_strAssist_Price) != null) {
            setKeyExecuted(m_strAssist_Price, true);
      }
      if (getUFDouble(m_strAssist_TaxPrice) != null) {
            setKeyExecuted(m_strAssist_TaxPrice, true);
      }	
      //主单位4个价格，折扣，税率均不变
      if (getUFDouble(m_strTaxPriceKey) != null) {
   	     setKeyExecuted(m_strTaxPriceKey, true);
   	  }
   	  if (getUFDouble(m_strPriceKey) != null) {
   	     setKeyExecuted(m_strPriceKey, true);
   	  }
   	  if (getUFDouble(m_strNetTaxPriceKey) != null) {
   	      setKeyExecuted(m_strNetTaxPriceKey, true);
   	  }
   	  if (getUFDouble(m_strNetPriceKey) != null) {
   	      setKeyExecuted(m_strNetPriceKey, true);
   	  }
        if (getUFDouble(m_strDiscountRateKey) != null) {
          setKeyExecuted(m_strDiscountRateKey, true);
        }
        if (getUFDouble(m_strTaxRateKey) != null) {
          setKeyExecuted(m_strTaxRateKey, true);
        }
    }
    //换算率
    else if (sFirstChangedKey.equals(m_strConvertRateKey)){
      //主单位，报价单位8个价格，折扣，税率均不变,价税合计，无税金额不变
      if (getUFDouble(m_strTaxPriceKey) != null) {
   	     setKeyExecuted(m_strTaxPriceKey, true);
   	  }
   	  if (getUFDouble(m_strPriceKey) != null) {
   	     setKeyExecuted(m_strPriceKey, true);
   	  }
   	  if (getUFDouble(m_strNetTaxPriceKey) != null) {
   	      setKeyExecuted(m_strNetTaxPriceKey, true);
   	  }
   	  if (getUFDouble(m_strNetPriceKey) != null) {
   	      setKeyExecuted(m_strNetPriceKey, true);
   	  }
      if (getUFDouble(m_strDiscountRateKey) != null) {
          setKeyExecuted(m_strDiscountRateKey, true);
      }
      if (getUFDouble(m_strTaxRateKey) != null) {
          setKeyExecuted(m_strTaxRateKey, true);
      }
      if (getUFDouble(m_strSummnyKey) != null) {
          setKeyExecuted(m_strSummnyKey, true);
      }
      if (getUFDouble(m_strMoneyKey) != null) {
          setKeyExecuted(m_strMoneyKey, true);
      }
      if (getUFDouble(m_strQt_TaxPriceKey) != null) {
	 	     setKeyExecuted(m_strQt_TaxPriceKey, true);
	  }
	  if (getUFDouble(m_strQt_PriceKey) != null) {
	 	     setKeyExecuted(m_strQt_PriceKey, true);
	  }
	  if (getUFDouble(m_strQt_NetTaxPriceKey) != null) {
	 	      setKeyExecuted(m_strQt_NetTaxPriceKey, true);
	  }
	  if (getUFDouble(m_strQt_NetPriceKey) != null) {
	 	      setKeyExecuted(m_strQt_NetPriceKey, true);
	  }
    }
    // 报价数量
    if (sFirstChangedKey.equals(m_strQt_NumKey)) {
    // 主单位4个价格
    	if (getUFDouble(m_strTaxPriceKey) != null) {
    	 	     setKeyExecuted(m_strTaxPriceKey, true);
    	  }
    	  if (getUFDouble(m_strPriceKey) != null) {
    	 	     setKeyExecuted(m_strPriceKey, true);
    	  }
    	  if (getUFDouble(m_strNetTaxPriceKey) != null) {
    	 	      setKeyExecuted(m_strNetTaxPriceKey, true);
    	  }
    	  if (getUFDouble(m_strNetPriceKey) != null) {
    	 	      setKeyExecuted(m_strNetPriceKey, true);
    	  }
      
      //报价单位4个价格，折扣，税率均不变
      if (getUFDouble(m_strQt_TaxPriceKey) != null) {
	 	     setKeyExecuted(m_strQt_TaxPriceKey, true);
	  }
	  if (getUFDouble(m_strQt_PriceKey) != null) {
	 	     setKeyExecuted(m_strQt_PriceKey, true);
	  }
	  if (getUFDouble(m_strQt_NetTaxPriceKey) != null) {
	 	      setKeyExecuted(m_strQt_NetTaxPriceKey, true);
	  }
	  if (getUFDouble(m_strQt_NetPriceKey) != null) {
	 	      setKeyExecuted(m_strQt_NetPriceKey, true);
	  }
      if (getUFDouble(m_strDiscountRateKey) != null) {
        setKeyExecuted(m_strDiscountRateKey, true);
      }
      if (getUFDouble(m_strTaxRateKey) != null) {
        setKeyExecuted(m_strTaxRateKey, true);
      }
    }
    //报价换算率
    else if (sFirstChangedKey.equals(m_strQt_ConvertRateKey)){
      //辅单位，主单位6个价格，折扣，税率均不变,价税合计，无税金额不变
      if (getUFDouble(m_strAssist_Price) != null) {
            setKeyExecuted(m_strAssist_Price, true);
      }
      if (getUFDouble(m_strAssist_TaxPrice) != null) {
            setKeyExecuted(m_strAssist_TaxPrice, true);
      }	
      if (getUFDouble(m_strTaxPriceKey) != null) {
   	     setKeyExecuted(m_strTaxPriceKey, true);
   	  }
   	  if (getUFDouble(m_strPriceKey) != null) {
   	     setKeyExecuted(m_strPriceKey, true);
   	  }
   	  if (getUFDouble(m_strNetTaxPriceKey) != null) {
   	      setKeyExecuted(m_strNetTaxPriceKey, true);
   	  }
   	  if (getUFDouble(m_strNetPriceKey) != null) {
   	      setKeyExecuted(m_strNetPriceKey, true);
   	  }
      if (getUFDouble(m_strDiscountRateKey) != null) {
          setKeyExecuted(m_strDiscountRateKey, true);
      }
      if (getUFDouble(m_strTaxRateKey) != null) {
          setKeyExecuted(m_strTaxRateKey, true);
      }
      if (getUFDouble(m_strSummnyKey) != null) {
          setKeyExecuted(m_strSummnyKey, true);
      }
      if (getUFDouble(m_strMoneyKey) != null) {
          setKeyExecuted(m_strMoneyKey, true);
      }
    }
    
    // 无税净价
	/** v5.3变更 zhangcheng 根据参数决定调折扣还是单价;默认调折扣则单价不变 */
	else if (sFirstChangedKey.equals(m_strNetPriceKey)) {	
	// 无论调折扣还是单价,数量和税率均不变
	  if (getUFDouble(m_strNumKey) != null) {
		setKeyExecuted(m_strNumKey, true);
	  }
	  if (getUFDouble(m_strTaxRateKey) != null) {
		setKeyExecuted(m_strTaxRateKey, true);
	  }
	  // 调折扣时(默认):含税单价、无税单价不变,折扣变
	  if (m_iPrior_ItemDiscountRate_Price == RelationsCalVO.ITEMDISCOUNTRATE_PRIOR_TO_PRICE) {
		if (getUFDouble(m_strTaxPriceKey) != null
			&& getUFDouble(m_strTaxPriceKey).intValue() > 0) {
				setKeyExecuted(m_strTaxPriceKey, true);
		}
		if (getUFDouble(m_strPriceKey) != null) {
				setKeyExecuted(m_strPriceKey, true);
		}
	  } else {// 调单价时(默认):折扣不变,含税单价、无税单价变
			if (getUFDouble(m_strDiscountRateKey) != null) {
				setKeyExecuted(m_strDiscountRateKey, true);
			}
		}
	}			
		// 无税金额
		/** v5.3变更 zhangcheng 根据参数决定调折扣还是单价;默认调折扣则单价不变 */
		else if (sFirstChangedKey.equals(m_strMoneyKey)) {
			// 无论调折扣还是单价,数量和税率均不变
			if (getUFDouble(m_strNumKey) != null) {
				setKeyExecuted(m_strNumKey, true);
			}
			if (getUFDouble(m_strTaxRateKey) != null) {
				setKeyExecuted(m_strTaxRateKey, true);
			}
			// 调折扣时(默认):含税单价、无税单价不变,折扣变
			if (m_iPrior_ItemDiscountRate_Price == RelationsCalVO.ITEMDISCOUNTRATE_PRIOR_TO_PRICE) {
				if (getUFDouble(m_strTaxPriceKey) != null
						&& getUFDouble(m_strTaxPriceKey).intValue() > 0) {
					setKeyExecuted(m_strTaxPriceKey, true);
				}
				if (getUFDouble(m_strPriceKey) != null) {
					setKeyExecuted(m_strPriceKey, true);
				}
			} else {// 调单价时(默认):折扣不变,含税单价、无税单价变
				if (getUFDouble(m_strDiscountRateKey) != null) {
					setKeyExecuted(m_strDiscountRateKey, true);
				}
			}
		}
    // 税率
    else if (sFirstChangedKey.equals(m_strTaxRateKey)) {
      // 数量、扣率不变
      if (getUFDouble(m_strNumKey) != null) {
        setKeyExecuted(m_strNumKey, true);
      }
      if (getUFDouble(m_strDiscountRateKey) != null) {
        setKeyExecuted(m_strDiscountRateKey, true);
      }
      if (getPrior_Price_TaxPrice() == RelationsCalVO.TAXPRICE_PRIOR_TO_PRICE) {
        // 含税单价优先 含税单价、净含税单价、价税合计不变
        if (getUFDouble(m_strTaxPriceKey) != null) {
          setKeyExecuted(m_strTaxPriceKey, true);
        }
        if (getUFDouble(m_strNetTaxPriceKey) != null) {
          setKeyExecuted(m_strNetTaxPriceKey, true);
        }
        if (getUFDouble(m_strSummnyKey) != null) {
          setKeyExecuted(m_strSummnyKey, true);
        }
      }
      else if (getPrior_Price_TaxPrice() == RelationsCalVO.PRICE_PRIOR_TO_TAXPRICE) {
        // 单价优先 单价、净单价、金额不变
        if (getUFDouble(m_strPriceKey) != null) {
          setKeyExecuted(m_strPriceKey, true);
        }
        if (getUFDouble(m_strNetPriceKey) != null) {
          setKeyExecuted(m_strNetPriceKey, true);
        }
        if (getUFDouble(m_strMoneyKey) != null) {
          setKeyExecuted(m_strMoneyKey, true);
        }
      }
    }
    // 税额
    else if (sFirstChangedKey.equals(m_strTaxKey)) {
      // 无税单价、扣率、无税净价、税率、数量、无税金额不变
      /** v5.3 税率、数量、扣率、4个价格、折扣额、价税合计(含税优先时)、无税金额(无税优先时)，均不变 */	
      if (getUFDouble(m_strTaxPriceKey) != null) {
		setKeyExecuted(m_strTaxPriceKey, true);
      }
      if (getUFDouble(m_strPriceKey) != null) {
        setKeyExecuted(m_strPriceKey, true);
      }
      if (getUFDouble(m_strNetTaxPriceKey) != null) {
          setKeyExecuted(m_strNetTaxPriceKey, true);
      }
      if (getUFDouble(m_strNetPriceKey) != null) {
          setKeyExecuted(m_strNetPriceKey, true);
      }
      if (getUFDouble(m_strDiscountRateKey) != null) {
        setKeyExecuted(m_strDiscountRateKey, true);
      }
      if (getUFDouble(m_strTaxRateKey) != null) {
        setKeyExecuted(m_strTaxRateKey, true);
      }
      if (getUFDouble(m_strNumKey) != null) {
        setKeyExecuted(m_strNumKey, true);
      }
      if (getUFDouble(m_strMoneyKey) != null && getPrior_Price_TaxPrice() == RelationsCalVO.PRICE_PRIOR_TO_TAXPRICE) {
        setKeyExecuted(m_strMoneyKey, true);
      }
      if (getUFDouble(m_strSummnyKey) != null && getPrior_Price_TaxPrice() == RelationsCalVO.TAXPRICE_PRIOR_TO_PRICE) {
          setKeyExecuted(m_strSummnyKey, true);
      }
    }
    // 价税合计
    /** v5.3变更  zhangcheng 根据参数决定调折扣还是单价;默认调折扣则单价不变 */
    else if (sFirstChangedKey.equals(m_strSummnyKey)) {
    	// 无论调折扣还是单价,数量和税率均不变
		if (getUFDouble(m_strNumKey) != null) {
			setKeyExecuted(m_strNumKey, true);
		}
		if (getUFDouble(m_strTaxRateKey) != null) {
			setKeyExecuted(m_strTaxRateKey, true);
		}
		// 调折扣时(默认):含税单价、无税单价不变,折扣变
		if (m_iPrior_ItemDiscountRate_Price == RelationsCalVO.ITEMDISCOUNTRATE_PRIOR_TO_PRICE) {
			if (getUFDouble(m_strTaxPriceKey) != null
					&& getUFDouble(m_strTaxPriceKey).intValue() > 0) {
				setKeyExecuted(m_strTaxPriceKey, true);
			}
			if (getUFDouble(m_strPriceKey) != null) {
				setKeyExecuted(m_strPriceKey, true);
			}
		}
		else{//调单价时:折扣不变,含税单价、无税单价变
			if (getUFDouble(m_strDiscountRateKey) != null) {
				setKeyExecuted(m_strDiscountRateKey, true);
			}
		}
    }
    // 含税净价
    /** v5.3变更  zhangcheng 根据参数决定调折扣还是单价;默认调折扣则单价不变 */
    else if (sFirstChangedKey.equals(m_strNetTaxPriceKey)) {
      // 无论调折扣还是单价,数量和税率均不变
      if (getUFDouble(m_strNumKey) != null) {
        setKeyExecuted(m_strNumKey, true);
      }
      if (getUFDouble(m_strTaxRateKey) != null) {
        setKeyExecuted(m_strTaxRateKey, true);
      }
      // 调折扣时(默认):含税单价、无税单价不变,折扣变
	  if (m_iPrior_ItemDiscountRate_Price == RelationsCalVO.ITEMDISCOUNTRATE_PRIOR_TO_PRICE) {
	      if (getUFDouble(m_strTaxPriceKey) != null
			 && getUFDouble(m_strTaxPriceKey).intValue() > 0) {
				 setKeyExecuted(m_strTaxPriceKey, true);
		  }
		  if (getUFDouble(m_strPriceKey) != null) {
				 setKeyExecuted(m_strPriceKey, true);
		  }
	  } else {// 调单价时:折扣不变,含税单价、无税单价变
		  if (getUFDouble(m_strDiscountRateKey) != null) {
			 	setKeyExecuted(m_strDiscountRateKey, true);
			 }
	  }
    }
    
    
    
    // 扣率
    else if (sFirstChangedKey.equals(m_strDiscountRateKey)||sFirstChangedKey.equals(m_strAllDiscountRateKey)) {
      // 数量、单价、含税单价、税率不变
      if (getUFDouble(m_strNumKey) != null) {
        setKeyExecuted(m_strNumKey, true);
      }
      if (getUFDouble(m_strPriceKey) != null) {
        setKeyExecuted(m_strPriceKey, true);
      }
      if (getUFDouble(m_strTaxPriceKey) != null) {
        setKeyExecuted(m_strTaxPriceKey, true);
      }
      if (getUFDouble(m_strTaxRateKey) != null) {
        setKeyExecuted(m_strTaxRateKey, true);
      }
    }
    // 含税单价
    else if (sFirstChangedKey.equals(m_strTaxPriceKey)) {
      // 扣率、数量、税率不变
      if (getUFDouble(m_strDiscountRateKey) != null) {
        setKeyExecuted(m_strDiscountRateKey, true);
      }
      if (getUFDouble(m_strNumKey) != null) {
        setKeyExecuted(m_strNumKey, true);
      }
      if (getUFDouble(m_strTaxRateKey) != null) {
        setKeyExecuted(m_strTaxRateKey, true);
      }
    }
    // 无税单价
    else if (sFirstChangedKey.equals(m_strPriceKey)) {
      // 扣率、数量、税率不变
      if (getUFDouble(m_strDiscountRateKey) != null) {
        setKeyExecuted(m_strDiscountRateKey, true);
      }
      if (getUFDouble(m_strNumKey) != null) {
        setKeyExecuted(m_strNumKey, true);
      }
      if (getUFDouble(m_strTaxRateKey) != null) {
        setKeyExecuted(m_strTaxRateKey, true);
      }
    }
    // 合格数量 不合格数量 辅数量 换算率
    else if (sFirstChangedKey.equals(m_strQualifiedNumKey)
        || sFirstChangedKey.equals(m_strUnQualifiedNumKey))
         {
      // 同数量变化时的不改变项
      setKeyExecutedForFirstChangedKey(m_strNumKey, iFirstCircle);
    }
    // 报价原币含税单价
    else if (sFirstChangedKey.equals(m_strQt_TaxPriceKey)) {
      // 扣率、数量、税率不变
      if (getUFDouble(m_strDiscountRateKey) != null) {
        setKeyExecuted(m_strDiscountRateKey, true);
      }
      if (getUFDouble(m_strQt_NumKey) != null) {
        setKeyExecuted(m_strQt_NumKey, true);
      }
      if (getUFDouble(m_strTaxRateKey) != null) {
        setKeyExecuted(m_strTaxRateKey, true);
      }
      if (getUFDouble(m_strNumKey) != null) {
          setKeyExecuted(m_strNumKey, true);
      }
      if (getUFDouble(m_strConvertRateKey) != null) {
          setKeyExecuted(m_strConvertRateKey, true);
      }
      setKeyExecuted(m_strTaxPriceKey, true);
      setKeyExecuted(m_strNetTaxPriceKey, true);
      setKeyExecuted(m_strPriceKey, true);
      setKeyExecuted(m_strNetPriceKey, true);
    }
    // 报价原币无税单价
    else if (sFirstChangedKey.equals(m_strQt_PriceKey)) {
      // 扣率、数量、税率不变
      if (getUFDouble(m_strDiscountRateKey) != null) {
        setKeyExecuted(m_strDiscountRateKey, true);
      }
      if (getUFDouble(m_strQt_NumKey) != null) {
        setKeyExecuted(m_strQt_NumKey, true);
      }
      if (getUFDouble(m_strTaxRateKey) != null) {
        setKeyExecuted(m_strTaxRateKey, true);
      }
      if (getUFDouble(m_strNumKey) != null) {
          setKeyExecuted(m_strNumKey, true);
      }
      if (getUFDouble(m_strConvertRateKey) != null) {
          setKeyExecuted(m_strConvertRateKey, true);
      }
      setKeyExecuted(m_strTaxPriceKey, true);
      setKeyExecuted(m_strNetTaxPriceKey, true);
      setKeyExecuted(m_strPriceKey, true);
      setKeyExecuted(m_strNetPriceKey, true);
    }
    // 报价原币含税净价
    else if (sFirstChangedKey.equals(m_strQt_NetTaxPriceKey)) {
      // 无论调折扣还是单价,数量和税率均不变
      if (getUFDouble(m_strQt_NumKey) != null) {
        setKeyExecuted(m_strQt_NumKey, true);
      }
      if (getUFDouble(m_strTaxRateKey) != null) {
        setKeyExecuted(m_strTaxRateKey, true);
      }
      if (getUFDouble(m_strNumKey) != null) {
            setKeyExecuted(m_strNumKey, true);
      }
      if (getUFDouble(m_strConvertRateKey) != null) {
            setKeyExecuted(m_strConvertRateKey, true);
      }
      setKeyExecuted(m_strTaxPriceKey, true);
      setKeyExecuted(m_strNetTaxPriceKey, true);
      setKeyExecuted(m_strPriceKey, true);
      setKeyExecuted(m_strNetPriceKey, true);
      
      // 调折扣时(默认):报价原币含税单价、无税单价不变,折扣变
	  if (m_iPrior_ItemDiscountRate_Price == RelationsCalVO.ITEMDISCOUNTRATE_PRIOR_TO_PRICE) {
	      if (getUFDouble(m_strQt_TaxPriceKey) != null
			 && getUFDouble(m_strQt_TaxPriceKey).intValue() > 0) {
				 setKeyExecuted(m_strQt_TaxPriceKey, true);
		  }
		  if (getUFDouble(m_strQt_PriceKey) != null) {
				 setKeyExecuted(m_strQt_PriceKey, true);
		  }
	  } else {// 调单价时:折扣不变,报价原币含税单价、无税单价变
		  if (getUFDouble(m_strDiscountRateKey) != null) {
			 	setKeyExecuted(m_strDiscountRateKey, true);
			 }
	  }
    }
    // 报价原币无税净价
	else if (sFirstChangedKey.equals(m_strQt_NetPriceKey)) {	
	// 无论调折扣还是单价,数量和税率均不变
	  if (getUFDouble(m_strNumKey) != null) {
		setKeyExecuted(m_strNumKey, true);
	  }
	  if (getUFDouble(m_strTaxRateKey) != null) {
		setKeyExecuted(m_strTaxRateKey, true);
	  }
	  if (getUFDouble(m_strQt_NumKey) != null) {
          setKeyExecuted(m_strQt_NumKey, true);
      }
      if (getUFDouble(m_strConvertRateKey) != null) {
          setKeyExecuted(m_strConvertRateKey, true);
      }
      setKeyExecuted(m_strTaxPriceKey, true);
      setKeyExecuted(m_strNetTaxPriceKey, true);
      setKeyExecuted(m_strPriceKey, true);
      setKeyExecuted(m_strNetPriceKey, true);
	  // 调折扣时(默认):报价原币含税单价、无税单价不变,折扣变
	  if (m_iPrior_ItemDiscountRate_Price == RelationsCalVO.ITEMDISCOUNTRATE_PRIOR_TO_PRICE) {
		if (getUFDouble(m_strQt_TaxPriceKey) != null
			&& getUFDouble(m_strQt_TaxPriceKey).intValue() > 0) {
				setKeyExecuted(m_strQt_TaxPriceKey, true);
		}
		if (getUFDouble(m_strQt_PriceKey) != null) {
				setKeyExecuted(m_strQt_PriceKey, true);
		}
	  } else {// 调单价时:折扣不变,报价原币含税单价、无税单价变
			if (getUFDouble(m_strDiscountRateKey) != null) {
				setKeyExecuted(m_strDiscountRateKey, true);
			}
		}
	}
    // 本币价税合计、本币无税金额
	else if (sFirstChangedKey.equals(m_strSummny_LocalKey)
				|| sFirstChangedKey.equals(m_strMoney_LocalKey)) {	
	  // 汇率、原币金额不变
	  if (getUFDouble(m_strExchange_O_TO_BrateKey) != null) {
		setKeyExecuted(m_strExchange_O_TO_BrateKey, true);
	  }
	  if (getUFDouble(m_strSummnyKey) != null) {
		setKeyExecuted(m_strSummnyKey, true);
	  }
	  if (getUFDouble(m_strMoneyKey) != null) {
          setKeyExecuted(m_strMoneyKey, true);
      }
      if (getUFDouble(m_strTaxKey) != null) {
          setKeyExecuted(m_strTaxKey, true);
      }
	}
    
    return;
  }

  /**
   * 作者：王印芬 功能：设置一个iCircle中的所有KEY是否曾改变过。如果曾改变，则不可再改变 参数：String sKey KEY boolean
   * bNewStatus 新状态 返回：无 例外：无 日期：(2003-6-10 11:39:21) 修改日期，修改人，修改原因，注释标志：
   */
  private void setKeysExecuted(int iCircle, boolean bNewStatus) {

    if (iCircle == CIRCLE_NUM_NETPRICE_MONEY) { // 数量,净单价,金额
      setKeyExecuted(m_strNumKey, bNewStatus);
      setKeyExecuted(m_strMoneyKey, bNewStatus);
      setKeyExecuted(m_strNetPriceKey, bNewStatus);
    }
    else if (iCircle == CIRCLE_TAXRATE) { // 税率,金额,税额,价税合计
      setKeyExecuted(m_strTaxRateKey, bNewStatus);
      setKeyExecuted(m_strMoneyKey, bNewStatus);
      setKeyExecuted(m_strSummnyKey, bNewStatus);
      setKeyExecuted(m_strTaxKey, bNewStatus);
    }
    else if (iCircle == CIRCLE_NUM_NETTAXPRICE_SUMMNY) { // 数量,净含税单价,价税合计
      setKeyExecuted(m_strNumKey, bNewStatus);
      setKeyExecuted(m_strNetTaxPriceKey, bNewStatus);
      setKeyExecuted(m_strSummnyKey, bNewStatus);
    }
    else if (iCircle == CIRCLE_DISCOUNTMNY) { // 含税单价、数量、价税合计、折扣额
        setKeyExecuted(m_strNumKey, bNewStatus);
        setKeyExecuted(m_strTaxPriceKey, bNewStatus);
        setKeyExecuted(m_strSummnyKey, bNewStatus);
        setKeyExecuted(m_strDiscountMnyKey, bNewStatus);
    }
    else if (iCircle == CIRCLE_PRICE_NETPRICE) { // 扣率,净单价,单价
      setKeyExecuted(m_strNetPriceKey, bNewStatus);
      setKeyExecuted(m_strPriceKey, bNewStatus);
      setKeyExecuted(m_strDiscountRateKey, bNewStatus);
    }
    else if (iCircle == CIRCLE_QUALIFIED_NUM) { // 数量,合格数量,不合格数量
      setKeyExecuted(m_strNumKey, bNewStatus);
      setKeyExecuted(m_strQualifiedNumKey, bNewStatus);
      setKeyExecuted(m_strUnQualifiedNumKey, bNewStatus);
    }
    else if (iCircle == CIRCLE_CONVERT_RATE) { // 主数量,辅数量,换算率,报价数量,报价换算率
      setKeyExecuted(m_strNumKey, bNewStatus);
      setKeyExecuted(m_strAssistNumKey, bNewStatus);
      setKeyExecuted(m_strConvertRateKey, bNewStatus);
      setKeyExecuted(m_strNetPriceKey, bNewStatus);
    }
    else if (iCircle == CIRCLE_QT_CONVERT_RATE) { // 主数量,报价数量,报价换算率
        setKeyExecuted(m_strNumKey, bNewStatus);
        setKeyExecuted(m_strQt_NumKey, bNewStatus);
        setKeyExecuted(m_strQt_ConvertRateKey, bNewStatus);
    }
    else if (iCircle == CIRCLE_TAXPRICE_NETTAXPRICE) { // 扣率,净含税单价,含税单价
      setKeyExecuted(m_strNetTaxPriceKey, bNewStatus);
      setKeyExecuted(m_strTaxPriceKey, bNewStatus);
      setKeyExecuted(m_strDiscountRateKey, bNewStatus);
    }
    else if (iCircle == CIRCLE_TAXPRICE_PRICE) { // 税率,含税单价,单价
      setKeyExecuted(m_strTaxRateKey, bNewStatus);
      setKeyExecuted(m_strTaxPriceKey, bNewStatus);
      setKeyExecuted(m_strPriceKey, bNewStatus);
    }
    else if (iCircle == CIRCLE_NETTAXPRICE_NETPRICE) { // 税率,净含税单价,单价
      setKeyExecuted(m_strTaxRateKey, bNewStatus);
      setKeyExecuted(m_strNetTaxPriceKey, bNewStatus);
      setKeyExecuted(m_strNetPriceKey, bNewStatus);
    }
    
    else if (iCircle == CIRCLE_QT_TAXPRICE_PRICE) { // 税率、报价原币含税单价、报价原币无税单价
        setKeyExecuted(m_strTaxRateKey, bNewStatus);
        setKeyExecuted(m_strQt_TaxPriceKey, bNewStatus);
        setKeyExecuted(m_strQt_PriceKey, bNewStatus);
      }
    else if (iCircle == CIRCLE_QT_NETTAXPRICE_NETPRICE) { // 税率、报价原币含税净价、报价原币无税净价
        setKeyExecuted(m_strTaxRateKey, bNewStatus);
        setKeyExecuted(m_strQt_NetTaxPriceKey, bNewStatus);
        setKeyExecuted(m_strQt_NetPriceKey, bNewStatus);
      }
    else if (iCircle == CIRCLE_QT_TAXPRICE_NETTAXPRICE) { // 扣率、报价原币含税单价、报价原币含税净价
        setKeyExecuted(m_strDiscountRateKey, bNewStatus);
        setKeyExecuted(m_strQt_TaxPriceKey, bNewStatus);
        setKeyExecuted(m_strQt_NetTaxPriceKey, bNewStatus);
      }
    else if (iCircle == CIRCLE_QT_PRICE_NETPRICE) { // 扣率、报价原币无税单价、报价原币无税净价
        setKeyExecuted(m_strDiscountRateKey, bNewStatus);
        setKeyExecuted(m_strQt_PriceKey, bNewStatus);
        setKeyExecuted(m_strQt_NetPriceKey, bNewStatus);
      }
    
    else if (iCircle == CIRCLE_QT_NUM_NETTAXPRICE_SUMMNY) { // 报价数量、报价原币含税净价、价税合计
        setKeyExecuted(m_strSummnyKey, bNewStatus);
        setKeyExecuted(m_strQt_NumKey, bNewStatus);
        setKeyExecuted(m_strQt_NetTaxPriceKey, bNewStatus);
      }
    else if (iCircle == CIRCLE_QT_NUM_NETPRICE_MONEY) { // 报价数量、报价原币无税净价、无税金额
        setKeyExecuted(m_strMoneyKey, bNewStatus);
        setKeyExecuted(m_strQt_NumKey, bNewStatus);
        setKeyExecuted(m_strQt_NetPriceKey, bNewStatus);
      }
    else if (iCircle == CIRCLE_LOCAL_SUMMNY_MNY_TAX) { // 本币价税合计、本币无税金额、本币税额
        setKeyExecuted(m_strSummny_LocalKey, bNewStatus);
        setKeyExecuted(m_strMoney_LocalKey, bNewStatus);
        setKeyExecuted(m_strTax_LocalKey, bNewStatus);
      }
    

  }

  /**
   * 作者：王印芬 功能：在模板上设置旧值，此处为回滚 参数： 返回： 例外：无 日期：(2001-05-27 11:39:21)
   * 修改日期，修改人，修改原因，注释标志：
   */

  private void setOldValuesToPanel() {
    // 数量
    // if(m_pnlBill.getBodyItem(m_strNumKey) != null ){
    // m_pnlBill.getBillModel().setValueAt(m_objOldNum,m_iPos,m_strNumKey) ;
    m_voCurr.setAttributeValue(m_strNumKey, m_objOldNum);
    // }
    // 净单价
    // if(m_pnlBill.getBodyItem(m_strNetPriceKey) != null ){
    // m_pnlBill.getBillModel().setValueAt(m_objOldNetPrice,m_iPos,m_strNetPriceKey)
    // ;
    m_voCurr.setAttributeValue(m_strNetPriceKey, m_objOldNetPrice);
    // }
    // 金额
    // if(m_pnlBill.getBodyItem(m_strMoneyKey) != null ){
    // m_pnlBill.getBillModel().setValueAt(m_objOldMoney,m_iPos,m_strMoneyKey) ;
    m_voCurr.setAttributeValue(m_strMoneyKey, m_objOldMoney);
    // }
    // 含税净单价
    // if(m_pnlBill.getBodyItem(m_strNetTaxPriceKey) != null ){
    // m_pnlBill.getBillModel().setValueAt(m_objOldNetTaxPrice,m_iPos,m_strNetTaxPriceKey)
    // ;
    m_voCurr.setAttributeValue(m_strNetTaxPriceKey, m_objOldNetTaxPrice);
    // }
    // 税率
    // if(m_pnlBill.getBodyItem(m_strTaxRateKey) != null ){
    // m_pnlBill.getBillModel().setValueAt(m_objOldTaxrate,m_iPos,m_strTaxRateKey)
    // ;
    m_voCurr.setAttributeValue(m_strTaxRateKey, m_objOldTaxrate);
    // }
    // 税额
    // if(m_pnlBill.getBodyItem(m_strTaxKey) != null ){
    // m_pnlBill.getBillModel().setValueAt(m_objOldTax,m_iPos,m_strTaxKey) ;
    m_voCurr.setAttributeValue(m_strTaxKey, m_objOldTax);
    // }
    // 价税合计
    // if(m_pnlBill.getBodyItem(m_strSummnyKey) != null ){
    // m_pnlBill.getBillModel().setValueAt(m_objOldSummny,m_iPos,m_strSummnyKey)
    // ;
    m_voCurr.setAttributeValue(m_strSummnyKey, m_objOldSummny);
    // }
    // 扣率
    // if(m_pnlBill.getBodyItem(m_strDiscountRateKey) != null ){
    // m_pnlBill.getBillModel().setValueAt(m_objOldDiscountRate,m_iPos,m_strDiscountRateKey)
    // ;
    m_voCurr.setAttributeValue(m_strDiscountRateKey, m_objOldDiscountRate);
    // }
    // 单价
    // if(m_pnlBill.getBodyItem(m_strPriceKey) != null ){
    // m_pnlBill.getBillModel().setValueAt(m_objOldPrice,m_iPos,m_strPriceKey) ;
    m_voCurr.setAttributeValue(m_strPriceKey, m_objOldPrice);
    // }
    // 合格数量
    // if(m_pnlBill.getBodyItem(m_strQualifiedNumKey) != null ){
    // m_pnlBill.getBillModel().setValueAt(m_objOldQualifiedNum,m_iPos,m_strQualifiedNumKey)
    // ;
    m_voCurr.setAttributeValue(m_strQualifiedNumKey, m_objOldQualifiedNum);
    // }
    // 不合格数量
    // if(m_pnlBill.getBodyItem(m_strUnQualifiedNumKey) != null ){
    // m_pnlBill.getBillModel().setValueAt(m_objOldUnQualifiedNum,m_iPos,m_strUnQualifiedNumKey)
    // ;
    m_voCurr.setAttributeValue(m_strUnQualifiedNumKey, m_objOldUnQualifiedNum);
    // }
    // 辅数量
    // if(m_pnlBill.getBodyItem(m_strAssistNumKey) != null ){
    // m_pnlBill.getBillModel().setValueAt(m_objOldAssistNum,m_iPos,m_strAssistNumKey)
    // ;
    m_voCurr.setAttributeValue(m_strAssistNumKey, m_objOldAssistNum);
    // }
    // 换算率
    // if(m_pnlBill.getBodyItem(m_strConvertRateKey) != null ){
    // m_pnlBill.getBillModel().setValueAt(m_objOldConvertRate,m_iPos,m_strConvertRateKey)
    // ;
    m_voCurr.setAttributeValue(m_strConvertRateKey, m_objOldConvertRate);
    // }
    // 含税单价
    // if(m_pnlBill.getBodyItem(m_strTaxPriceKey) != null ){
    // m_pnlBill.getBillModel().setValueAt(m_objOldTaxPrice,m_iPos,m_strTaxPriceKey)
    // ;
    m_voCurr.setAttributeValue(m_strTaxPriceKey, m_objOldTaxPrice);
    // }
  }

  /**
   * 设置强行不可编辑字段映射
   * @param forbidEditField
   */
  private void setForbidEditField(int[] forbidEditField){
	  
	  if (forbidEditField==null || forbidEditField.length<=0)
		  return;
	  
	  for (int field : forbidEditField){
		     if (field == NUM) {// 数量
		         setKeyExecuted(m_strNumKey, true);
		      }
		      else if (field == NUM_QUALIFIED) {// 合格数量
		         setKeyExecuted(m_strQualifiedNumKey, true);
		      }
		      else if (field == NUM_UNQUALIFIED) {// 不合格数量
		        
		        setKeyExecuted(m_strUnQualifiedNumKey, true);
		      }
		      else if (field == NUM_ASSIST) {// 辅数量
		       
		        setKeyExecuted(m_strAssistNumKey, true);
		      }
		      else if (field == NET_PRICE_ORIGINAL) {// 原币无税净价
		        
		        setKeyExecuted(m_strNetPriceKey, true);
		      }
		      else if (field == NET_TAXPRICE_ORIGINAL) {// 原币含税净价
		        
		        setKeyExecuted(m_strNetTaxPriceKey, true);
		      }
		      else if (field == PRICE_ORIGINAL) {// 原币无税单价
		       
		        setKeyExecuted(m_strPriceKey, true);
		      }
		      else if (field == TAXPRICE_ORIGINAL) {// 原币含税单价
		    	  setKeyExecuted(m_strTaxPriceKey, true);
		        }
		      else if (field == TAXRATE) {// 税率
		        
		        setKeyExecuted(m_strTaxRateKey, true);
		      }
		      else if (field == TAX_ORIGINAL) {// 原币税额
		        
		        setKeyExecuted(m_strTaxKey, true);
		      }
		      else if (field == MONEY_ORIGINAL) {// 无税金额
		      
		        setKeyExecuted(m_strMoneyKey, true);
		      }
		      else if (field == SUMMNY_ORIGINAL) {// 原币价税合计
		     
		        setKeyExecuted(m_strSummnyKey, true);
		      }
		      else if (field == DISCOUNT_RATE) {// 扣率
		       
		        setKeyExecuted(m_strDiscountRateKey, true);
		      }
		      else if (field == ALLDISCOUNT_RATE) {// 整单折扣
		    	
		    	  setKeyExecuted(m_strAllDiscountRateKey, true);
		        }
		      else if (field == CONVERT_RATE) {// 换算率
		  
		        setKeyExecuted(m_strConvertRateKey, true);
		      }
		      else if (field == QT_CONVERT_RATE) {// 报价换算率
		      
		          setKeyExecuted(m_strQt_ConvertRateKey, true);
		      }
		      else if (field == TAXPRICE_ORIGINAL) {// 含税单价
		     
		        setKeyExecuted(m_strTaxPriceKey, true);
		      }
		      else if (field == DISCOUNTMNY_ORIGINA) {// 原币折扣额
		    	
		    	  setKeyExecuted(m_strDiscountMnyKey, true);
		      }
		      else if (field == QT_NUM) {// 原币报价单位数量
		    	
		    	  setKeyExecuted(m_strQt_NumKey, true);
		      }
		      else if (field == QT_TAXPRICE_ORIGINAL) {// 原币报价单位含税单价
		    	
		    	  setKeyExecuted(m_strQt_TaxPriceKey, true);
		      }
		      else if (field == QT_PRICE_ORIGINAL) {// 原币报价单位无税单价
		    	
		    	  setKeyExecuted(m_strQt_PriceKey, true);
		      }
		      else if (field == QT_NET_TAXPRICE_ORIGINAL) {// 原币报价单位含税净价
		    	
		    	  setKeyExecuted(m_strQt_NetTaxPriceKey, true);
		      }
		      else if (field == QT_NET_PRICE_ORIGINAL) {// 原币报价单位无税净价
		    
		    	  setKeyExecuted(m_strQt_NetPriceKey, true);
		      }
		      /*else if (field == PRICE_LOCAL) {// 本币无税单价
		    	
		    	  setKeyExecuted(m_strPrice_LocalKey, true);
		      }
		      else if (field == TAXPRICE_LOCAL) {// 本币含税单价 
		    
		    	  setKeyExecuted(m_strTaxPrice_LocalKey, true);
		      }
		      else if (field == NET_PRICE_LOCAL) {// 本币无税净价
		    	 
		    	  setKeyExecuted(m_strNet_Price_LocalKey, true);
		      }
		      else if (field == NET_TAXPRICE_LOCAL) {// 本币含税净价
		    	
		    	  setKeyExecuted(m_strNet_TaxPrice_LocalKey, true);
		      }
		      else if (field == TAX_LOCAL) {// 本币税额
		    	 
		    	  setKeyExecuted(m_strTax_LocalKey, true);
		      }
		      else if (field == MONEY_LOCAL) {// 本币无税金额
		    	
		    	  setKeyExecuted(m_strMoney_LocalKey, true);
		      }
		      else if (field == SUMMNY_LOCAL) {// 本币价税合计
		    
		    	  setKeyExecuted(m_strSummny_LocalKey, true);
		      }
		      else if (field == DISCOUNTMNY_LOCAL) {//本币折扣额
		    	
		    	  setKeyExecuted(m_strDiscountMny_LocalKey, true);
		      }
		      else if (field == QT_PRICE_LOCAL) {// 报价单位本币无税单价
		    	
		    	  setKeyExecuted(m_strQt_Price_LocalKey, true);
		      }
		      else if (field == QT_TAXPRICE_LOCAL) {// 报价单位本币含税单价
		    	
		    	  setKeyExecuted(m_strQt_TaxPrice_LocalKey, true);
		      }
		      else if (field == QT_NET_PRICE_LOCAL) {// 报价单位本币无税净价
		    	
		    	  setKeyExecuted(m_strQt_Net_Price_LocalKey, true);
		      }
		      else if (field == QT_NET_TAXPRICE_LOCAL) {//报价单位本币含税净价
		    
		    	  setKeyExecuted(m_strQt_Net_TaxPrice_LocalKey, true);
		      }
		      else if (field == PRICE_FRACTIONAL) {// 辅币无税单价
		    	 
		    	  setKeyExecuted(m_strPrice_FractionalKey, true);
		      }
		      else if (field == TAXPRICE_FRACTIONAL) {// 辅币含税单价 
		    	 
		    	  setKeyExecuted(m_strTaxPrice_FractionalKey, true);
		      }
		      else if (field == NET_PRICE_FRACTIONAL) {// 辅币无税净价
		    	 
		    	  setKeyExecuted(m_strNet_Price_FractionalKey, true);
		      }
		      else if (field == NET_TAXPRICE_FRACTIONAL) {// 辅币含税净价
		    	  
		    	  setKeyExecuted(m_strNet_TaxPrice_FractionalKey, true);
		      }
		      else if (field == TAX_FRACTIONAL) {// 辅币税额
		    	
		    	  setKeyExecuted(m_strTax_FractionalKey, true);
		      }
		      else if (field == MONEY_FRACTIONAL) {// 辅币无税金额
		    	
		    	  setKeyExecuted(m_strMoney_FractionalKey, true);
		      }
		      else if (field == SUMMNY_FRACTIONAL) {// 辅币价税合计
		    	
		    	  setKeyExecuted(m_strSummny_FractionalKey, true);
		      }
		      else if (field == DISCOUNTMNY_FRACTIONAL) {//辅币折扣额
		    	 
		    	  setKeyExecuted(m_strDiscountMny_FractionalKey, true);
		      }
		      else if (field == ASSIST_PRICE_ORIGINAL) {// 辅计量无税单价
		    	
		    	  setKeyExecuted(m_strAssist_Price, true);
		      }
		      else if (field == ASSIST_TAXPRICE_ORIGINAL) {//辅计量含税单价
		    	  setKeyExecuted(m_strAssist_TaxPrice, true);
		      }
		      else if (field == ASK_TAXPRICE) {// 询价原币含税单价
		    	  setKeyExecuted(m_strAsk_TaxPriceKey, true);
		      }
		      else if (field == ASK_PRICE) {//询价原币无税单价
		    	  setKeyExecuted(m_strAsk_PriceKey, true);
		      }  		*/
  	  }
  }
  
    /**
	 * 为设置最先变动key，而设置含税优先机制
	 */
	private void setPriorForSetFirstChangeKey(int[] iaPrior) {
		if (iaPrior == null) {
			return;
		}
		int iLen = iaPrior.length;
		for (int i = 0; i < iLen; i++) {
			// 含税、无税优先
			if (iaPrior[i] == RelationsCalVO.PRICE_PRIOR_TO_TAXPRICE) {
				m_iPrior_Price_TaxPrice = RelationsCalVO.PRICE_PRIOR_TO_TAXPRICE;
			}
			if (iaPrior[i] == RelationsCalVO.TAXPRICE_PRIOR_TO_PRICE) {
				m_iPrior_Price_TaxPrice = RelationsCalVO.TAXPRICE_PRIOR_TO_PRICE;
			}
		}
	}
	    
  /**
	 * 作者：王印芬 功能：设置优先机制 参数：int[] iaPrior 优无机制数组 数组无长度限制，但属同种性质的优先机制，以后输入的为准
	 * 如：[0]=NUM_PRIOR_TO_PRICE [3]=PRICE_PRIOR_TO_NUM 则取PRICE_PRIOR_TO_NUM 返回：无
	 * 例外：无 日期：(2002-5-15 11:39:21) 修改日期，修改人，修改原因，注释标志：
	 */
  private void setPrior(int[] iaPrior) {
    if (iaPrior == null) {
      return;
    }
    int iLen = iaPrior.length;
    for (int i = 0; i < iLen; i++) {
      // 含税、无税优先
      if (iaPrior[i] == RelationsCalVO.PRICE_PRIOR_TO_TAXPRICE) {
        m_iPrior_Price_TaxPrice = RelationsCalVO.PRICE_PRIOR_TO_TAXPRICE;
      }
      if (iaPrior[i] == RelationsCalVO.TAXPRICE_PRIOR_TO_PRICE) {
        m_iPrior_Price_TaxPrice = RelationsCalVO.TAXPRICE_PRIOR_TO_PRICE;
      }
      //编辑净价，价税合计时，调单价还是折扣
      if (iaPrior[i] == RelationsCalVO.ITEMDISCOUNTRATE_PRIOR_TO_PRICE) {
    	  m_iPrior_ItemDiscountRate_Price = RelationsCalVO.ITEMDISCOUNTRATE_PRIOR_TO_PRICE;
      }
      if (iaPrior[i] == RelationsCalVO.PRICE_PRIOR_TO_ITEMDISCOUNTRATE) {
    	  m_iPrior_ItemDiscountRate_Price = RelationsCalVO.PRICE_PRIOR_TO_ITEMDISCOUNTRATE;
      }
      //主辅币核算
      if (iaPrior[i] == RelationsCalVO.YES_LOCAL_FRAC) {
    	  m_iPrior_LOCAL_FRAC = RelationsCalVO.YES_LOCAL_FRAC;
      }
      if (iaPrior[i] == RelationsCalVO.NO_LOCAL_FRAC) {
    	  m_iPrior_LOCAL_FRAC = RelationsCalVO.NO_LOCAL_FRAC;
      }
    }
    
    //(如果单价为0或null，强制调整单价)
    if ( 
    	  ( m_iPrior_ItemDiscountRate_Price == RelationsCalVO.ITEMDISCOUNTRATE_PRIOR_TO_PRICE) &&
    	  (	
    	  ((getFirstChangedKey().equals(m_strSummnyKey)||getFirstChangedKey().equals(m_strNetTaxPriceKey))&&(getUFDouble(m_strTaxPriceKey) == null 
			   || getUFDouble(m_strTaxPriceKey).doubleValue() == 0))
		   ||((getFirstChangedKey().equals(m_strMoneyKey)||getFirstChangedKey().equals(m_strNetPriceKey))&&(getUFDouble(m_strPriceKey) == null 
				   || getUFDouble(m_strPriceKey).doubleValue() == 0))
		   ||(getFirstChangedKey().equals(m_strQt_NetTaxPriceKey)&&(getUFDouble(m_strQt_TaxPriceKey) == null 
				   || getUFDouble(m_strQt_TaxPriceKey).doubleValue() == 0))
		   ||(getFirstChangedKey().equals(m_strQt_NetPriceKey)&&(getUFDouble(m_strQt_PriceKey) == null 
				   || getUFDouble(m_strQt_PriceKey).doubleValue() == 0))
		  )
	   )
		  m_iPrior_ItemDiscountRate_Price = RelationsCalVO.PRICE_PRIOR_TO_ITEMDISCOUNTRATE;
    
    //最后强制设置含税无税优先机制
    //含税优先
	if (getFirstChangedKey().equals(m_strSummnyKey)
				|| getFirstChangedKey().equals(m_strTaxPriceKey)
				|| getFirstChangedKey().equals(m_strNetTaxPriceKey)
				|| getFirstChangedKey().equals(m_strQt_TaxPriceKey)
				|| getFirstChangedKey().equals(m_strQt_NetTaxPriceKey))
		m_iPrior_Price_TaxPrice = RelationsCalVO.TAXPRICE_PRIOR_TO_PRICE;
	// 无税优先
	else if (getFirstChangedKey().equals(m_strMoneyKey)
				|| getFirstChangedKey().equals(m_strPriceKey)
				|| getFirstChangedKey().equals(m_strNetPriceKey)
				|| getFirstChangedKey().equals(m_strQt_PriceKey)
				|| getFirstChangedKey().equals(m_strQt_NetPriceKey))
		m_iPrior_Price_TaxPrice = RelationsCalVO.PRICE_PRIOR_TO_TAXPRICE;
  }

  /**
   * 加载公司精度[数量BD501]、[辅数量BD502]、[换算率BD503]、[单价BD505]
   * 
   * @param pk_corp
   */
  private void loadPowerByCorpPk(String pk_corp) {
    int[] iaRet = null;
    try {
      iaRet = getDigitBatch(pk_corp, new String[] {
          "BD501", "BD502", "BD503", "BD505"
      });
    }
    catch (BusinessException e) {
      SCMEnv.out("取公司精度：[数量BD501]、[辅数量BD502]、[换算率BD503]、[单价BD505]时出现异常：");/*-=notranslate=-*/
      SCMEnv.out(e);
    }
    if (iaRet == null || iaRet.length < 4) {
      iaRet = new int[] {
          DEFAULT_POWER_DATABASE, DEFAULT_POWER_DATABASE,
          DEFAULT_POWER_DATABASE, DEFAULT_POWER_DATABASE
      };
    }
    m_mapDigitsPara.put(pk_corp, iaRet);
  }

  /**
   * 批查询数据精度(非金额数值型)
   * 
   * @return int[]
   * @param corpId
   *          公司主键
   * @param paraCodes
   *          参数编码数组
   * @since v50
   * @author czp
   * @date 2006-10-09
   */
  public int[] getDigitBatch(String corpID, String[] paraCodes)
      throws BusinessException {
    int[] iRets = null;
    if (paraCodes == null || paraCodes.length <= 0)
      return null;
    Hashtable htt = null;
    try {
      
      /**
       * 固定在前台缓存中取参数，不支持动态修改参数后重新读取数据库
       * 改为下列方式，回校验时间戳，有变动读数据库，并更新缓存
       * v5.3 jiangzhe zhangcheng
       */
      /*ISysInitQry bo = (ISysInitQry) NCLocator.getInstance().lookup(
          ISysInitQry.class.getName());
      htt = bo.queryBatchParaValues(corpID, paraCodes);*/
      htt =SysInitBO_Client.queryBatchParaValues(corpID, paraCodes);
      
      iRets = new int[paraCodes.length];
      if (htt == null || htt.size() <= 0) {
        for (int i = 0; i < iRets.length; i++) {
          iRets[i] = 2;
        }
      }
      else {
        for (int i = 0; i < iRets.length; i++) {
          if (htt.containsKey(paraCodes[i])) {
            if (htt.get(paraCodes[i]) != null) {
              iRets[i] = Integer.parseInt(htt.get(paraCodes[i]).toString());
            }
            else {
              iRets[i] = 2;
            }
          }
        }
      }
    }
    catch (Exception e) {
      SCMEnv.out(e);
      throw new BusinessException(e);
    }
    return iRets;
  }
  
  /**
   * 从表头或表体取公司或原币币种
   */
  private String getPkcorpOrCurrType(String key){
	  String value = null;
	 
	  if (m_voCurr != null && m_voCurr instanceof IRelaCalInfos){
		  if (key.equals("pk_corp"))
			  value = PuPubVO.getString_TrimZeroLenAsNull(((IRelaCalInfos) m_voCurr).getCorpPk());
		  else if (key.equals("currtype"))
			  value = PuPubVO.getString_TrimZeroLenAsNull(((IRelaCalInfos) m_voCurr).getCurrTypePk());
	  }
	  else if (m_hvoCurr != null && m_hvoCurr instanceof IRelaCalInfos){
		  if (key.equals("pk_corp"))
			  value = PuPubVO.getString_TrimZeroLenAsNull(((IRelaCalInfos) m_hvoCurr).getCorpPk());
		  else if (key.equals("currtype"))
			  value = PuPubVO.getString_TrimZeroLenAsNull(((IRelaCalInfos) m_hvoCurr).getCurrTypePk());
	  }
	  else if (m_voCurr != null){
		  if (key.equals("pk_corp"))
			  value = PuPubVO.getString_TrimZeroLenAsNull( m_voCurr.getAttributeValue(m_strPk_CorpKey));
		  else if (key.equals("currtype"))
			  value = PuPubVO.getString_TrimZeroLenAsNull(m_voCurr.getAttributeValue(m_strCurrTypePkKey));
	  }
	  
	  if (value==null){
		  if (m_hvoCurr != null ){
			  if (key.equals("pk_corp"))
				  value = PuPubVO.getString_TrimZeroLenAsNull(m_hvoCurr.getAttributeValue(m_strPk_CorpKey));
			  else if (key.equals("currtype"))
				  value = PuPubVO.getString_TrimZeroLenAsNull(m_hvoCurr.getAttributeValue(m_strCurrTypePkKey));
		  }
	  }
	  
	  return value;
  }

  /**
   * 获取计算精度--主数量
   * <p>
   * <strong>注意：</strong>
   * <p>
   * 1、如果当前计算VO未实现接口[IRelaCalInfos]，则按默认精度处理[数量]
   * <p>
   * 2、如果当前计算VO，接口[IRelaCalInfos]的实例未正确返加[公司主键]，则按默认精度处理[数量]
   * 
   * @return int
   * @since v50
   * @author czp
   */
  private int getPowerNum() {
    int iPower = DEFAULT_POWER_DATABASE;
    
    String pk_corp = getPkcorpOrCurrType("pk_corp");
    
    /*if (m_voCurr == null || !(m_voCurr instanceof IRelaCalInfos)) {
      return iPower;
    }
    String pk_corp = PuPubVO
        .getString_TrimZeroLenAsNull(((IRelaCalInfos) m_voCurr).getCorpPk());*/
    
    
    
    if (pk_corp == null) {
      return iPower;
    }
    if (m_mapDigitsPara.containsKey(pk_corp)) {
      int[] iaDigit = (int[]) m_mapDigitsPara.get(pk_corp);
      iPower = iaDigit[0];
    }
    else {
      loadPowerByCorpPk(pk_corp);
    }
    return iPower;
  }

  /**
   * 获取计算精度--辅数量
   * <p>
   * <strong>注意：</strong>
   * <p>
   * 1、如果当前计算VO未实现接口[IRelaCalInfos]，则按默认精度处理[辅数量]
   * <p>
   * 2、如果当前计算VO，接口[IRelaCalInfos]的实例未正确返加[公司主键]，则按默认精度处理[辅数量]
   * 
   * @return int
   * @since v50
   * @author czp
   */
  private int getPowerAssNum() {
    int iPower = DEFAULT_POWER_DATABASE;
    
    String pk_corp = getPkcorpOrCurrType("pk_corp");
    
    /*if (m_voCurr == null || !(m_voCurr instanceof IRelaCalInfos)) {
      return iPower;
    }
    String pk_corp = PuPubVO
        .getString_TrimZeroLenAsNull(((IRelaCalInfos) m_voCurr).getCorpPk());*/
    
    if (pk_corp == null) {
      return iPower;
    }
    if (m_mapDigitsPara.containsKey(pk_corp)) {
      int[] iaDigit = (int[]) m_mapDigitsPara.get(pk_corp);
      iPower = iaDigit[1];
    }
    else {
      loadPowerByCorpPk(pk_corp);
    }
    return iPower;
  }

  /**
   * 获取计算精度--换算率
   * <p>
   * <strong>注意：</strong>
   * <p>
   * 1、如果当前计算VO未实现接口[IRelaCalInfos]，则按默认精度处理[换算率]
   * <p>
   * 2、如果当前计算VO，接口[IRelaCalInfos]的实例未正确返加[公司主键]，则按默认精度处理[换算率]
   * 
   * @return int
   * @since v50
   * @author czp
   */
  private int getPowerRate() {
    int iPower = DEFAULT_POWER_DATABASE;
    
    String pk_corp = getPkcorpOrCurrType("pk_corp");
    
    /*if (m_voCurr == null || !(m_voCurr instanceof IRelaCalInfos)) {
      return iPower;
    }
    String pk_corp = PuPubVO
        .getString_TrimZeroLenAsNull(((IRelaCalInfos) m_voCurr).getCorpPk());*/
    
    
    if (pk_corp == null) {
      return iPower;
    }
    if (m_mapDigitsPara.containsKey(pk_corp)) {
      int[] iaDigit = (int[]) m_mapDigitsPara.get(pk_corp);
      iPower = iaDigit[2];
    }
    else {
      loadPowerByCorpPk(pk_corp);
    }
    return iPower;
  }
  
  /**
   * 获取计算精度--单价
   * <p>
   * <strong>注意：</strong>
   * <p>
   * 1、如果当前计算VO未实现接口[IRelaCalInfos]，则按默认精度处理[单价]
   * <p>
   * 2、如果当前计算VO，接口[IRelaCalInfos]的实例未正确返加[公司主键]，则按默认精度处理[单价]
   * 
   * @return int
   * @since v50
   * @author czp
   */
  private int getPowerPrice() {
    int iPower = DEFAULT_POWER_DATABASE;
    
    String pk_corp = getPkcorpOrCurrType("pk_corp");
    
    /*if (m_voCurr == null || !(m_voCurr instanceof IRelaCalInfos)) {
      return iPower;
    }
    String pk_corp = PuPubVO
        .getString_TrimZeroLenAsNull(((IRelaCalInfos) m_voCurr).getCorpPk());*/
    
    
    if (pk_corp == null) {
      return iPower;
    }
    // m_mapDigitsPara = new HashMap();
    if (m_mapDigitsPara.containsKey(pk_corp)) {
      int[] iaDigit = (int[]) m_mapDigitsPara.get(pk_corp);
      iPower = iaDigit[3];
    }
    else {
      loadPowerByCorpPk(pk_corp);
    }
    return iPower;
  }
  
  /**
   * @param pk_corp
   * @return 根据公司获得单价精度
   */
  private int getPowerPrice(String pk_corp) {
	  int iPower = DEFAULT_POWER_DATABASE;
	  if (!m_mapDigitsPara.containsKey(pk_corp)) {
			loadPowerByCorpPk(pk_corp);
	  }
	  int[] iaDigit = (int[]) m_mapDigitsPara.get(pk_corp);
	  return iaDigit[3];
  }

  /**
	 * 获取币种计算精度--业务
	 * <p>
	 * <strong>注意：</strong>
	 * <p>
	 * 1、如果当前计算VO未实现接口[IRelaCalInfos]，则按默认精度处理[金额]
	 * <p>
	 * 2、如果当前计算VO，接口[IRelaCalInfos]的实例未正确返加[币种主键]，则按默认精度处理[金额]
	 * 
	 * @return int
	 * @since v50
	 * @author czp
	 */
  private int getPowerMnyBusi() {
    int iPower = DEFAULT_POWER_DATABASE;
    
    String strCurrTypePk = getPkcorpOrCurrType("currtype");
    
    /*if (m_voCurr == null || !(m_voCurr instanceof IRelaCalInfos)) {
      return iPower;
    }
    String strCurrTypePk = PuPubVO
        .getString_TrimZeroLenAsNull(((IRelaCalInfos) m_voCurr).getCurrTypePk());*/

    
    if (strCurrTypePk == null) {
      return iPower;
    }
    // m_mapDigitsMnyBusi = new HashMap();
    if (m_mapDigitsMnyBusi.containsKey(strCurrTypePk)) {
      Integer iDigit = (Integer) m_mapDigitsMnyBusi.get(strCurrTypePk);
      iPower = iDigit.intValue();
    }
    else {
     /* ICurrtype currQrySrv = (ICurrtype) NCLocator.getInstance().lookup(
          ICurrtype.class.getName());*/
      CurrtypeVO voRet = null;
      //try {
      voRet = CurrtypeQuery.getInstance().getCurrtypeVO(strCurrTypePk);
      /*}
      catch (BusinessException e) {
        SCMEnv.out("取币种：[“" + strCurrTypePk + "”]精度时出现异常：");-=notranslate=-
        SCMEnv.out(e);
      }*/
      if (voRet == null || voRet.getCurrbusidigit() == null) {
        m_mapDigitsMnyBusi.put(strCurrTypePk, new Integer(
            DEFAULT_POWER_DATABASE));
        iPower = DEFAULT_POWER_DATABASE;
      }
      else {
        m_mapDigitsMnyBusi.put(strCurrTypePk, voRet.getCurrbusidigit());
        iPower = voRet.getCurrbusidigit().intValue();
      }
    }
    return iPower;
  }

  /**
   * @param strCurrTypePk
   * @return 根据币种ID获取币种业务精度(用于控制金额精度)
   */
  private int getPowerMnyBusi(String strCurrTypePk) {
	  int iPower = DEFAULT_POWER_DATABASE;
	    if (strCurrTypePk == null || strCurrTypePk.equals("")) {
	      return iPower;
	    }
	    if (m_mapDigitsMnyBusi.containsKey(strCurrTypePk)) {
	      Integer iDigit = (Integer) m_mapDigitsMnyBusi.get(strCurrTypePk);
	      iPower = iDigit.intValue();
	    }
	    else {
/*	      ICurrtype currQrySrv = (ICurrtype) NCLocator.getInstance().lookup(
	          ICurrtype.class.getName());*/
	      CurrtypeVO voRet = null;
	      //try {
	      //voRet = currQrySrv.findCurrtypeVOByPK(strCurrTypePk);
	      voRet = CurrtypeQuery.getInstance().getCurrtypeVO(strCurrTypePk);
	      //}
/*	      catch (BusinessException e) {
	        SCMEnv.out("取币种：[“" + strCurrTypePk + "”]精度时出现异常：");-=notranslate=-
	        SCMEnv.out(e);
	      }*/
	      if (voRet == null || voRet.getCurrbusidigit() == null) {
	        m_mapDigitsMnyBusi.put(strCurrTypePk, new Integer(
	            DEFAULT_POWER_DATABASE));
	        iPower = DEFAULT_POWER_DATABASE;
	      }
	      else {
	        m_mapDigitsMnyBusi.put(strCurrTypePk, voRet.getCurrbusidigit());
	        iPower = voRet.getCurrbusidigit().intValue();
	      }
	    }
	    return iPower;
  }
  
  /**
   * 获取币种计算精度--财务
   * <p>
   * <strong>注意：</strong>
   * <p>
   * 1、如果当前计算VO未实现接口[IRelaCalInfos]，则按默认精度处理[金额]
   * <p>
   * 2、如果当前计算VO，接口[IRelaCalInfos]的实例未正确返加[币种主键]，则按默认精度处理[金额]
   * 
   * @return int
   * @since v50
   * @author czp
   */
  private int getPowerMnyFina(String strCurrTypePk) {
    int iPower = DEFAULT_POWER_DATABASE;   
    
    if (strCurrTypePk == null || strCurrTypePk.equals("")) {
	      return iPower;
	    }
    if (m_mapDigitsMnyFina.containsKey(strCurrTypePk)) {
      Integer iDigit = (Integer) m_mapDigitsMnyFina.get(strCurrTypePk);
      iPower = iDigit.intValue();
    }
    else {
     /* ICurrtype currQrySrv = (ICurrtype) NCLocator.getInstance().lookup(
          ICurrtype.class.getName());*/
      CurrtypeVO voRet = null;
      //try {
      voRet = CurrtypeQuery.getInstance().getCurrtypeVO(strCurrTypePk);
     /* }
      catch (BusinessException e) {
        SCMEnv.out("取币种：[“" + strCurrTypePk + "”]精度时出现异常：");-=notranslate=-
        SCMEnv.out(e);
      }*/
      if (voRet == null || voRet.getCurrdigit() == null) {
        m_mapDigitsMnyFina.put(strCurrTypePk, new Integer(
            DEFAULT_POWER_DATABASE));
        iPower = DEFAULT_POWER_DATABASE;
      }
      else {
        m_mapDigitsMnyFina.put(strCurrTypePk, voRet.getCurrdigit());
        iPower = voRet.getCurrdigit().intValue();
      }
    }
    return iPower;
}
  
  /**
   * 获取币种计算精度--财务
   * <p>
   * <strong>注意：</strong>
   * <p>
   * 1、如果当前计算VO未实现接口[IRelaCalInfos]，则按默认精度处理[金额]
   * <p>
   * 2、如果当前计算VO，接口[IRelaCalInfos]的实例未正确返加[币种主键]，则按默认精度处理[金额]
   * 
   * @return int
   * @since v50
   * @author czp
   */
  private int getPowerMnyFina() {
    int iPower = DEFAULT_POWER_DATABASE;
    
    String strCurrTypePk = getPkcorpOrCurrType("currtype");
    
    /*if (m_voCurr == null || !(m_voCurr instanceof IRelaCalInfos)) {
      return iPower;
    }
    String strCurrTypePk = PuPubVO
        .getString_TrimZeroLenAsNull(((IRelaCalInfos) m_voCurr).getCurrTypePk());*/
    
    
    
    if (strCurrTypePk == null) {
      return iPower;
    }
    if (m_mapDigitsMnyFina.containsKey(strCurrTypePk)) {
      Integer iDigit = (Integer) m_mapDigitsMnyFina.get(strCurrTypePk);
      iPower = iDigit.intValue();
    }
    else {
/*      ICurrtype currQrySrv = (ICurrtype) NCLocator.getInstance().lookup(
          ICurrtype.class.getName());*/
      CurrtypeVO voRet = null;
      /*try {
        voRet = currQrySrv.findCurrtypeVOByPK(strCurrTypePk);*/
        voRet = CurrtypeQuery.getInstance().getCurrtypeVO(strCurrTypePk);
      /*}
      catch (BusinessException e) {
        SCMEnv.out("取币种：[“" + strCurrTypePk + "”]精度时出现异常：");-=notranslate=-
        SCMEnv.out(e);
      }*/
      if (voRet == null || voRet.getCurrdigit() == null) {
        m_mapDigitsMnyFina.put(strCurrTypePk, new Integer(
            DEFAULT_POWER_DATABASE));
        iPower = DEFAULT_POWER_DATABASE;
      }
      else {
        m_mapDigitsMnyFina.put(strCurrTypePk, voRet.getCurrdigit());
        iPower = voRet.getCurrdigit().intValue();
      }
    }
    return iPower;
  }
}