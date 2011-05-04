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
 * ʵ���㷨:ͳһ������������仯��ϵ��
 * <p>
 * <strong>�ṩ�ߣ�</strong>��Ӧ��-�ɹ�����
 * <p>
 * <strong>ʹ���ߣ�</strong>NC��Ӧ����NC����
 * <p>
 * <strong>���״̬��</strong>����
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
   * ����������� ������[����BD501]��[������BD502]��[������BD503]��[����BD505]; �洢�ṹ:{pk_corp=int[]{}}
   */
  private  HashMap m_mapDigitsPara = new HashMap();

  // ����[���]����--ҵ�񾫶�,�洢�ṹ:{pk_currtype=Integer}
  private  HashMap m_mapDigitsMnyBusi = new HashMap();

  // ����[���]����--���񾫶�,�洢�ṹ:{pk_currtype=Integer}
  private  HashMap m_mapDigitsMnyFina = new HashMap();

  // ���ݿ�Ĭ�Ͼ���
  final static int DEFAULT_POWER_DATABASE = 8;

  // ����ģ��Ĭ�Ͼ���
  final static int DEFAULT_POWER_TEMPLET = 2;
  
  // �ۿ�Ĭ�Ͼ���
  final static int DEFAULT_POWER_DISCOUNTRATE = 6;

  // �㳣��
  final static UFDouble ZERO = new UFDouble(0.0);

  //��ӡ���Կ���
  private boolean debug = false;
  
  public void setDebug(boolean debug) {
    this.debug = debug;
  }

  // ������ñ�־(Ĭ��Ϊ false)
  // ������ñ�־(Ĭ��Ϊ SCM)
  private final static int ForScm = 0;

  private final static int ForFinance = 1;

  private int m_iFinaFlag = ForScm;
  
  private UFDouble ratemny = null;
  
  private int[] m_forbidEditField = null;

  // private boolean m_bFinaFlag = false;

  private BusinessCurrencyRateUtil bcurr;
  
  // ��ǰ�ı��KEY
  private String m_strChangedKey;

  // �ֶ�KEY����
  public static final int DISCOUNT_TAX_TYPE_NAME = 0; // ��˰�����(Ӧ˰�ں���Ӧ˰��ӣ�����˰)

  public static final int DISCOUNT_TAX_TYPE_KEY = 1; // ��˰���

  public static final int NUM = 2; // ������

  public static final int NUM_QUALIFIED = 3; // �ϸ�����

  public static final int NUM_UNQUALIFIED = 4; // ���ϸ�����

  public static final int NUM_ASSIST = 5; // ������

  public static final int NET_PRICE_ORIGINAL = 7; // ��˰���� --ԭ��

  public static final int NET_TAXPRICE_ORIGINAL = 8; // ��˰���� --ԭ��

  public static final int PRICE_ORIGINAL = 9; // ��˰���� --ԭ��

  public static final int TAXRATE = 11; // ˰��

  public static final int TAX_ORIGINAL = 12; // ˰�� --ԭ��

  public static final int MONEY_ORIGINAL = 13; // ��� --ԭ��

  public static final int SUMMNY_ORIGINAL = 14; // ��˰�ϼ� --ԭ��

  public static final int DISCOUNT_RATE = 15; // ��Ʒ�ۿ�(�ɹ��еĿ���)

  public static final int CONVERT_RATE = 16; // ������

  public static final int IS_FIXED_CONVERT = 17; // �Ƿ�̶�������

  public static final int TAXPRICE_ORIGINAL = 18; //��˰����
  
  public static final int ALLDISCOUNT_RATE = 19; // �����ۿ�(��ʱֻ��������)
  
  public static final int HEAD_SUMMNY_ORIGINAL = 101; //������˰�ϼ�
  
  public static final int DISCOUNTMNY_ORIGINA = 102;   //ԭ���ۿ۶�
  
  public static final int UNITID = 103;   //��������λID
  
  public static final int QUOTEUNITID = 104;   //���ۼ�����λID
  
  public static final int QT_CONVERT_RATE = 105; // ���ۻ�����

  public static final int QT_IS_FIXED_CONVERT = 106; // �����Ƿ�̶�������
  
  public static final int QT_NUM = 20;                    //ԭ�ұ��۵�λ����                
  public static final int QT_TAXPRICE_ORIGINAL = 21;      //ԭ�ұ��۵�λ��˰����        
  public static final int QT_PRICE_ORIGINAL = 22;         //ԭ�ұ��۵�λ��˰����        
  public static final int QT_NET_TAXPRICE_ORIGINAL = 23;  //ԭ�ұ��۵�λ��˰����        
  public static final int QT_NET_PRICE_ORIGINAL = 24;     //ԭ�ұ��۵�λ��˰����     

  public static final int PRICE_LOCAL = 25;              //������˰����                                          
  public static final int TAXPRICE_LOCAL = 26;           //���Һ�˰����                                                   
  public static final int NET_PRICE_LOCAL = 27;          //������˰����                                                     
  public static final int NET_TAXPRICE_LOCAL = 28;       //���Һ�˰����                                                     
  public static final int TAX_LOCAL = 29;                //����˰��                                                        
  public static final int MONEY_LOCAL = 30;              //������˰���                                                
  public static final int SUMMNY_LOCAL = 31;             //���Ҽ�˰�ϼ�                                                 
  public static final int DISCOUNTMNY_LOCAL = 32;        //�����ۿ۶�

  public static final int QT_PRICE_LOCAL = 33;           //���۵�λ������˰����                                          
  public static final int QT_TAXPRICE_LOCAL = 34;        //���۵�λ���Һ�˰����                                                   
  public static final int QT_NET_PRICE_LOCAL = 35;       //���۵�λ������˰����                                                     
  public static final int QT_NET_TAXPRICE_LOCAL = 36;    //���۵�λ���Һ�˰����                                                     

  public static final int PRICE_FRACTIONAL = 37;               //������˰����                                          
  public static final int TAXPRICE_FRACTIONAL = 38;            //���Һ�˰����                                                   
  public static final int NET_PRICE_FRACTIONAL = 39;           //������˰����                                                     
  public static final int NET_TAXPRICE_FRACTIONAL = 40;        //���Һ�˰����                                                     
  public static final int TAX_FRACTIONAL = 41;                 //����˰��                                                        
  public static final int MONEY_FRACTIONAL = 42;               //������˰���                                                
  public static final int SUMMNY_FRACTIONAL = 43;              //���Ҽ�˰�ϼ�                                                 
  public static final int DISCOUNTMNY_FRACTIONAL = 44;         //�����ۿ۶�

  public static final int ASSIST_PRICE_ORIGINAL = 45;  		  //��������˰����		--ԭ��
  public static final int ASSIST_TAXPRICE_ORIGINAL = 46;      //��������˰����		--ԭ��

  public static final int ASK_TAXPRICE = 47;           //ѯ��ԭ�Һ�˰����
  public static final int ASK_PRICE = 48;              //ѯ��ԭ����˰����

  public static final int CURRTYPEPk = 49;              //ԭ��PK
  public static final int PK_CORP = 50;                 //PK_CORP
  public static final int EXCHANGE_O_TO_BRATE = 51;     //�۱�����
  public static final int EXCHANGE_O_TO_ARATE = 52;     //�۸�����
  public static final int BILLDATE = 53;                //��������

  // �ֶ�KEY������Ӧ���ֶ�KEY��������ʾ����(��̨����ʱ�����ֶ����ƣ���ע����﷭��)
  private String m_strDiscountTaxTypeKey = null; // ��˰���0

  private String m_strDiscountTaxTypeName = null; // ��˰��������(Ӧ˰�ں���Ӧ˰��ӣ�����˰)1

  private String m_strNumKey = ""; // ������2

  private String m_strQualifiedNumKey = ""; // �ϸ�����3

  private String m_strUnQualifiedNumKey = ""; // ���ϸ�����4

  private String m_strAssistNumKey = ""; // ������5

  private String m_strNetPriceKey = ""; // ��˰���� --ԭ��6

  private String m_strNetTaxPriceKey = ""; // ��˰���� (����˰����) --ԭ��7

  private String m_strPriceKey = ""; // ��˰���� --ԭ��8

  private String m_strTaxRateKey = ""; // ˰��9

  private String m_strTaxKey = ""; // ˰�� --ԭ��10

  private String m_strMoneyKey = ""; // ��� --ԭ��113

  private String m_strSummnyKey = ""; // ��˰�ϼ� --ԭ��12
  
  private String m_strDiscountRateKey = ""; // ��Ʒ�ۿ�(���ɹ��еĿ���)13

  private String m_strConvertRateKey = ""; // ������14

  private String m_strTaxPriceKey = ""; // ��˰���� --ԭ��15

  private boolean m_bFixedConvertRateKey = false; // �Ƿ�̶�������16
  
  private String m_strAllDiscountRateKey = ""; // �����ۿ�(��ʱֻ��������)17
  
  private String m_strheadstrSummnyKey = ""; // ������˰�ϼ� --ԭ��18
  
  private String m_strDiscountMnyKey = ""; // ԭ���ۿ۶� --ԭ��19
  
  private String m_strQt_ConvertRateKey = ""; // ���ۻ�����14

  private boolean m_bQt_FixedConvertRateKey = false; // �����Ƿ�̶�������16

  private String m_strQt_NumKey = "";           //ԭ�ұ��۵�λ����                
  private String m_strQt_TaxPriceKey = "";      //ԭ�ұ��۵�λ��˰����        
  private String m_strQt_PriceKey = "";         //ԭ�ұ��۵�λ��˰����        
  private String m_strQt_NetTaxPriceKey = "";  //ԭ�ұ��۵�λ��˰����        
  private String m_strQt_NetPriceKey = "";     //ԭ�ұ��۵�λ��˰����     

  private String m_strPrice_LocalKey = "";              //������˰����                                          
  private String m_strTaxPrice_LocalKey = "";           //���Һ�˰����                                                   
  private String m_strNet_Price_LocalKey = "";          //������˰����                                                     
  private String m_strNet_TaxPrice_LocalKey = "";       //���Һ�˰����                                                     
  private String m_strTax_LocalKey = "";                //����˰��                                                        
  private String m_strMoney_LocalKey = "";              //������˰���                                                
  private String m_strSummny_LocalKey = "";             //���Ҽ�˰�ϼ�                                                 
  private String m_strDiscountMny_LocalKey = "";        //�����ۿ۶�

  private String m_strQt_Price_LocalKey = "";           //���۵�λ������˰����                                          
  private String m_strQt_TaxPrice_LocalKey = "";        //���۵�λ���Һ�˰����                                                   
  private String m_strQt_Net_Price_LocalKey = "";       //���۵�λ������˰����                                                     
  private String m_strQt_Net_TaxPrice_LocalKey = "";    //���۵�λ���Һ�˰����                                                     

  private String m_strPrice_FractionalKey = "";               //������˰����                                          
  private String m_strTaxPrice_FractionalKey = "";            //���Һ�˰����                                                   
  private String m_strNet_Price_FractionalKey = "";           //������˰����                                                     
  private String m_strNet_TaxPrice_FractionalKey = "";        //���Һ�˰����                                                     
  private String m_strTax_FractionalKey = "";                 //����˰��                                                        
  private String m_strMoney_FractionalKey = "";               //������˰���                                                
  private String m_strSummny_FractionalKey = "";              //���Ҽ�˰�ϼ�                                                 
  private String m_strDiscountMny_FractionalKey = "";         //�����ۿ۶�

  private String m_strAssist_Price = "";  		  //��������˰����		--ԭ��
  private String m_strAssist_TaxPrice = "";      //��������˰����		--ԭ��

  private String m_strAsk_TaxPriceKey = "";           //ѯ��ԭ�Һ�˰����
  private String m_strAsk_PriceKey = "";              //ѯ��ԭ����˰����

  private String m_strCurrTypePkKey = "";              //ԭ��PK
  private String m_strPk_CorpKey = "";                 //PK_CORP
  private String m_strExchange_O_TO_BrateKey = "";     //�۱�����
  private String m_strExchange_O_TO_ArateKey = "";     //�۸�����
  private String m_strBillDateKey = "";                //��������
  private String m_strUnitID = "";                     //��������λ
  private String m_strQuoteUnitID = "";                //���ۼ�����λ
  
  
  // �����ֶ�������ʾ��Ϣ
  // private static String m_strNetPriceName = "������";
  // private static String m_strNetTaxPriceName = "����˰����";
  // private static String m_strPriceName = "����";
  // private static String m_strTaxRateName = "˰��";
  // private static String m_strDiscountRateName = "����";
  // private static String m_strConvertRateName = "������";
  // private static String m_strTaxPriceName = "��˰����";
  //	
  // static{
  private final static String m_strNetPriceName = NCLangRes4VoTransl
      .getNCLangRes().getStrByID("common", "UC000-0000450")/* @res:������ */;

  private final static String m_strNetTaxPriceName = NCLangRes4VoTransl
      .getNCLangRes().getStrByID("common", "UC000-0000452")/* @res:����˰���� */;

  private final static String m_strPriceName = NCLangRes4VoTransl
      .getNCLangRes().getStrByID("common", "UC000-0000741")/* @res:���� */;

  private final static String m_strTaxRateName = NCLangRes4VoTransl
      .getNCLangRes().getStrByID("common", "UC000-0003078")/* @res:˰�� */;

  private final static String m_strDiscountRateName = NCLangRes4VoTransl
      .getNCLangRes().getStrByID("common", "UC000-0001998")/* @res:���� */;

  private final static String m_strConvertRateName = NCLangRes4VoTransl
      .getNCLangRes().getStrByID("common", "UC000-0002161")/* @res:������ */;

  private final static String m_strTaxPriceName = NCLangRes4VoTransl
      .getNCLangRes().getStrByID("common", "UC000-0001160")/* @res:��˰���� */;

  // }

  // ����CIRCLE
  private static final int CIRCLE_CONVERT_RATE = 2; // ��������������������

  private static final int CIRCLE_IMPOSSIBLE = 0; // û�п�ִ�е�CIRCLE

  private static final int CIRCLE_NETTAXPRICE_NETPRICE = 9; // ˰�ʡ���˰���ۡ���˰����

  private static final int CIRCLE_NUM_NETPRICE_MONEY = 3; // �����������ۡ����

  private static final int CIRCLE_NUM_NETTAXPRICE_SUMMNY = 4; // ��������˰���ۡ���˰�ϼ�

  private static final int CIRCLE_PRICE_NETPRICE = 6; // ���ʡ���˰���ۡ���˰����

  private static final int CIRCLE_QUALIFIED_NUM = 1; // �������ϸ����������ϸ�����

  private static final int CIRCLE_TAXPRICE_NETTAXPRICE = 7; // ���ʡ���˰���ۡ���˰����

  private static final int CIRCLE_TAXPRICE_PRICE = 8; // ˰�ʡ���˰���ۡ���˰����

  private static final int CIRCLE_TAXRATE = 5; // ˰�ʡ���˰���˰�ϼ�
  
  private static final int CIRCLE_DISCOUNTMNY = 10; // ��˰���ۡ���������˰�ϼơ��ۿ۶�
  
  private static final int CIRCLE_VIAPRICE= 11; // ��������˰���ۡ���������˰���ۡ�����������˰�ϼơ���˰��˰�ʡ��ۿ۶�

  private static final int CIRCLE_QT_TAXPRICE_PRICE = 12;//˰�ʡ�����ԭ�Һ�˰���ۡ�����ԭ����˰����
  
  private static final int CIRCLE_QT_NETTAXPRICE_NETPRICE = 13;//˰�ʡ�����ԭ�Һ�˰���ۡ�����ԭ����˰����
  
  private static final int CIRCLE_QT_TAXPRICE_NETTAXPRICE = 14; // ���ʡ�����ԭ�Һ�˰���ۡ�����ԭ�Һ�˰����
  
  private static final int CIRCLE_QT_PRICE_NETPRICE = 15; // ���ʡ�����ԭ����˰���ۡ�����ԭ����˰����
  
  private static final int CIRCLE_QT_NUM_NETTAXPRICE_SUMMNY = 16;// ��������������ԭ�Һ�˰���ۡ���˰�ϼ�
  
  private static final int CIRCLE_QT_NUM_NETPRICE_MONEY = 17;// ��������������ԭ����˰���ۡ���˰���
  
  private static final int CIRCLE_QT_CONVERT_RATE = 18; // ���������������������ۻ�����
  
  private static final int CIRCLE_LOCAL_SUMMNY_MNY_TAX = 19; //���Ҽ�˰�ϼơ�������˰������˰��
  
  // ===============��ʼ�仯ITEMȷ������Ӧ����Щ�Ӧ�޸ģ��Ա���Ӳ�ͬ�����ʱ�ĸ����޸�
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

  // ===============��־ĳ��CIRCLE�Ƿ��ѱ�ִ��
  private boolean m_bCircleExecuted[] = null;

  // ����ģ�幫ʽ��zero
  private boolean m_bNullAsZero = false;

  // /==============�¼���ģ�����
  // private BillEditEvent m_evtEdit = null;
  // ��ǰҪ����� CircularlyAccessibleValueObject[] ��λ��
  // private BillCardPanel m_pnlBill = null;
  // ��ǰҪ����ĵ�������VO
  private CircularlyAccessibleValueObject m_voCurr = null;
  
  //��ǰҪ����ı�ͷVO
  private CircularlyAccessibleValueObject m_hvoCurr = null;
  //��ǰҪ����ı���VO����
  private CircularlyAccessibleValueObject[] m_bvoCurr = null;
  
  
  //================Դ�ң����ң�����
  private String pk_CurrType;
  private String locaCurrType;
  private String fracCurrType;

  // ===============������Ȼ��Ƶı���
  // Ĭ�ϣ���˰����(�������ں�˰����)
  private int m_iPrior_Price_TaxPrice = RelationsCalVO.PRICE_PRIOR_TO_TAXPRICE;
  
  // Ĭ�ϣ����ۿۡ��༭��˰,��˰����,��˰�ϼ�,��˰���ʱ,�������ۻ��ߵ�Ʒ�ۿ�
  private int m_iPrior_ItemDiscountRate_Price = RelationsCalVO.ITEMDISCOUNTRATE_PRIOR_TO_PRICE;
  
  // Ĭ�ϣ����㷨�����б��ҡ����ҵļ���
  private int m_iPrior_LOCAL_FRAC = RelationsCalVO.NO_LOCAL_FRAC;
  
  // ===============��ֵ
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

  // V5:������ִ���ʱ����ʾ��Ϣ
  private String m_strShowHintMsg = null;

  // һ��IE�Ựһ��ʵ��
  private FormulaParse m_fp = null;

  /**
   * ��̨���õ�ͳһ��ڣ����ݱ仯��KEY����һ��VO�ı�Ӱ���ֵ
   * 
   * @param voaCurr
   *          ��ǰ������VO����
   * @param iaPrior
   *          Ŀǰ��ʱ����һ������int[],int[0]=��˰���Ȼ��ǲ���˰����{��˰�������ڵ���:RelationsCalVO.TAXPRICE_PRIOR_TO_PRICE;�������ں�˰����:RelationsCalVO.PRICE_PRIOR_TO_TAXPRICE}
   * @param strChangedKey
   *          ��ǰ�����仯���ֶ�(����VO���,���綩������Ϊ��nordernum��)
   * @param nDescriptions
   *          ��ǰҪ����ĵ��ֶ�����(�㷨�涨��,�μ�SCMRelationsCal��"�ֶ�KEY����"��ض���)
   * @param strKeys
   *          ��ǰҪ������ֶ�(����VO���,���綩������Ϊ��nordernum��)
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
   * ����ר�ã���̨���õ�ͳһ��ڣ����ݱ仯��KEY����һ��VO�ı�Ӱ���ֵ
   * 
   * @param voaCurr
   *          ��ǰ������VO����
   * @param iaPrior
   *          Ŀǰ��ʱ����һ������int[],int[0]=��˰���Ȼ��ǲ���˰����{��˰�������ڵ���:RelationsCalVO.TAXPRICE_PRIOR_TO_PRICE;�������ں�˰����:RelationsCalVO.PRICE_PRIOR_TO_TAXPRICE}
   * @param strChangedKey
   *          ��ǰ�����仯���ֶ�(����VO���,���綩������Ϊ��nordernum��)
   * @param nDescriptions
   *          ��ǰҪ����ĵ��ֶ�����(�㷨�涨��,�μ�SCMRelationsCal��"�ֶ�KEY����"��ض���)
   * @param strKeys
   *          ��ǰҪ������ֶ�(����VO���,���綩������Ϊ��nordernum��)
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
   * ��������VO���㣬�����������ҵļ���
   * 
   * ǰ����̨���õ�ͳһ��ڣ����ݱ仯��KEY����һ��VO�ı�Ӱ���ֵ
   * 
   * @param voCurr
   *          ��ǰ������VO
   * @param iaPrior
   *          Ŀǰ��ʱ����һ������int[],int[0]=��˰���Ȼ��ǲ���˰����{��˰�������ڵ���:RelationsCalVO.TAXPRICE_PRIOR_TO_PRICE;�������ں�˰����:RelationsCalVO.PRICE_PRIOR_TO_TAXPRICE}
   * @param strChangedKey
   *          ��ǰ�����仯���ֶ�(����VO���,���綩������Ϊ��nordernum��)
   * @param nDescriptions
   *          ��ǰҪ����ĵ��ֶ�����(�㷨�涨��,�μ�SCMRelationsCal��"�ֶ�KEY����"��ض���)
   * @param strKeys
   *          ��ǰҪ������ֶ�(����VO���,���綩������Ϊ��nordernum��)
   * @return
   * @exception BusinessException
   * @author <a href="mailto:czp@ufida.com.cn">czp</a>
   * @darte 2006-05-16
   */
  public static Object calculate(CircularlyAccessibleValueObject voCurr,
      int[] iaPrior, String strChangedKey, int[] nDescriptions, String[] strKeys, int[] forbidEditField) {

    // ��ʼ��
    SCMRelationsCal cal = new SCMRelationsCal(ForScm, voCurr, iaPrior,
        strChangedKey, nDescriptions, strKeys,forbidEditField);

    // ����
    cal.calculate();
    if (cal.m_strShowHintMsg != null) {
      return cal.m_strShowHintMsg;
    }
	else if (cal.ratemny!=null){
	  return new Object[]{"���ұ䶯���ܳ������������" + cal.ratemny+",�Ƿ������",voCurr};
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
   * ������ͷVO,��������VO���㣬���������ҵļ���
   * calculate��������:Ϊ���۲�Ʒ�����Ľӿ�,���ӵ������ڲ����Ա���������Ҽ���
   * 2008-03-20  zhangcheng
   */
  public static Object calculate(CircularlyAccessibleValueObject voCurrB,
			CircularlyAccessibleValueObject voCurrH, int[] iaPrior,
			String strChangedKey, int[] nDescriptions, String[] strKeys, int[] forbidEditField ) {
	  
	  // ��ʼ��
	  SCMRelationsCal cal = new SCMRelationsCal(ForScm, voCurrB,voCurrH,iaPrior,
	      strChangedKey, nDescriptions, strKeys,forbidEditField);

	  // ����
	  cal.calculate();
	  if (cal.m_strShowHintMsg != null) {
	    return cal.m_strShowHintMsg;
	  }
	  else if (cal.ratemny!=null){
		  return new Object[]{"���ұ䶯���ܳ������������" + cal.ratemny+",�Ƿ������",voCurrB};
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
   * ������ͷVO���������VO���㣬���������ҵļ���
   * calculate��������:Ϊ���۲�Ʒ�����Ľӿ�,���ӵ������ڲ����Ա���������Ҽ���
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
   * ������ͷVO���������VO���㣬���������ҵļ���
   * calculate��������:Ϊ���۲�Ʒ�����Ľӿ�,���ӵ������ڲ����Ա���������Ҽ���
   * 2008-03-20  zhangcheng
   */
  public static void calculate(CircularlyAccessibleValueObject[] voaCurr,
		  CircularlyAccessibleValueObject voCurrH,
	      int[] iaPrior, String strChangedKey, int[] nDescriptions, String[] strKeys)
	      throws BusinessException {
	  calculate(voaCurr, voCurrH, iaPrior, strChangedKey, nDescriptions, strKeys, null);
  }
  
  /**
   * ǰ����̨���õ�ͳһ��ڣ����ݱ�ͷ�仯��KEY����һ��VO�ı�Ӱ���ֵ
   * 
   * @param voCurr
   *          ��ǰ������VO
   * @param oldValue
   *          ��ǰ�����ֶεľ�ֵ         
   * @param iaPrior
   *          Ŀǰ��ʱ����һ������int[],int[0]=��˰���Ȼ��ǲ���˰����{��˰�������ڵ���:RelationsCalVO.TAXPRICE_PRIOR_TO_PRICE;�������ں�˰����:RelationsCalVO.PRICE_PRIOR_TO_TAXPRICE}
   * @param strChangedKey
   *          ��ǰ�����仯���ֶ�(����VO���,���綩������Ϊ��nordernum��)
   * @param nDescriptions
   *          ��ǰҪ����ĵ��ֶ�����(�㷨�涨��,�μ�SCMRelationsCal��"�ֶ�KEY����"��ض���)
   * @param strKeys
   *          ��ǰҪ������ֶ�(����VO���,���綩������Ϊ��nordernum��)
   * @return
   * @exception BusinessException
   * @darte 2008-03-13
   */
  /*public static Object calculate(CircularlyAccessibleValueObject H_voCurr,
			CircularlyAccessibleValueObject[] B_voCurr, Object HeadoldValue,
			int[] iaPrior, String strChangedKey, int[] nDescriptions,
			String[] strKeys) throws BusinessException {

    // ��ʼ��
    SCMRelationsCal cal = new SCMRelationsCal(ForScm, H_voCurr, B_voCurr,HeadoldValue, iaPrior,
        strChangedKey, nDescriptions, strKeys);

    // ���ݱ�ͷ�仯�������仯
    cal.calculate(HeadoldValue);
    
    if (cal.m_strShowHintMsg != null) {
      return cal.m_strShowHintMsg;
    }
    else {
      //����仯��������������ֵ
      calculate(B_voCurr, iaPrior, cal.m_strSummnyKey, nDescriptions, strKeys);   	
      return B_voCurr;
    }
  }*/

  /**
   * ����ר�ã�ǰ����̨���õ�ͳһ��ڣ����ݱ仯��KEY����һ��VO�ı�Ӱ���ֵ
   * 
   * @param voCurr
   *          ��ǰ������VO
   * @param iaPrior
   *          Ŀǰ��ʱ����һ������int[],int[0]=��˰���Ȼ��ǲ���˰����{��˰�������ڵ���:RelationsCalVO.TAXPRICE_PRIOR_TO_PRICE;�������ں�˰����:RelationsCalVO.PRICE_PRIOR_TO_TAXPRICE}
   * @param strChangedKey
   *          ��ǰ�����仯���ֶ�(����VO���,���綩������Ϊ��nordernum��)
   * @param nDescriptions
   *          ��ǰҪ����ĵ��ֶ�����(�㷨�涨��,�μ�SCMRelationsCal��"�ֶ�KEY����"��ض���)
   * @param strKeys
   *          ��ǰҪ������ֶ�(����VO���,���綩������Ϊ��nordernum��)
   * @return
   * @exception BusinessException
   * @author <a href="mailto:czp@ufida.com.cn">czp</a>
   * @darte 2006-05-16
   */
  public static Object calculateFina(CircularlyAccessibleValueObject voCurr,
      int[] iaPrior, String strChangedKey, int[] nDescriptions, String[] strKeys) {

    // ��ʼ��
    SCMRelationsCal cal = new SCMRelationsCal(ForFinance, voCurr, iaPrior,
        strChangedKey, nDescriptions, strKeys,null);

    // ����
    cal.calculate();
    if (cal.m_strShowHintMsg != null) {
      return cal.m_strShowHintMsg;
    }
	else if (cal.ratemny!=null){
	   return new Object[]{"���ұ䶯���ܳ������������" + cal.ratemny+",�Ƿ������",voCurr};
	}
    else {
      return voCurr;
    }
  }

  /**
   * ��ʼ����ʽ������
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
   * ��VOִ�й�ʽ
   * 
   * @author czp
   * @param formulas
   * @date 2006-05-16
   */
  private void execFormulas(String[] formulas) {
    int iCnt = formulas.length;
    for (int k = 0; k < iCnt; k++) {
      // ���ù�ʽ
      m_fp.setExpress(formulas[k]);
      // ���ñ���
      int iLen = m_fp.getVarry().getVarry().length;
      Object oVal = null;
      String strKey = null;
      for (int i = 0; i < iLen; i++) {
        strKey = m_fp.getVarry().getVarry()[i];
        oVal = m_voCurr.getAttributeValue(m_fp.getVarry().getVarry()[i]);
        // ������ȡVOֵ,��UI����һ��
        oVal = getResultPower(strKey, oVal);
        m_fp.addVariable(m_fp.getVarry().getVarry()[i], m_voCurr
            .getAttributeValue(m_fp.getVarry().getVarry()[i]));
      }
      // �����ȴ���ʽִ�н��
      strKey = m_fp.getVarry().getFormulaName();

      oVal = m_fp.getValueAsObject();
    
      oVal = getResultPower(strKey, oVal);
      // ���ý����VO
      m_voCurr.setAttributeValue(m_fp.getVarry().getFormulaName(), oVal);

    }
    
    //���Դ�ӡ
    if (debug)
    	debugPrint(formulas);
  }

  /**
   * �����ȴ���
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
    // ��������������������
    if (strKey.equalsIgnoreCase(m_strNumKey)
        || strKey.equalsIgnoreCase(m_strQualifiedNumKey)
        || strKey.equalsIgnoreCase(m_strUnQualifiedNumKey)
        || strKey.equalsIgnoreCase(m_strQt_NumKey)) {
      iPower = getPowerNum();
    }
    // ��������������������
    else if (strKey.equalsIgnoreCase(m_strAssistNumKey)) {
      iPower = getPowerAssNum();
    }
    // �����ʡ����ۻ����ʾ���
    else if (strKey.equalsIgnoreCase(m_strConvertRateKey) 
    		|| strKey.equalsIgnoreCase(m_strQt_ConvertRateKey)) {
      iPower = getPowerRate();
    }
    // ���۾���
    else if (strKey.equalsIgnoreCase(m_strNetPriceKey)
        || strKey.equalsIgnoreCase(m_strNetTaxPriceKey)
        || strKey.equalsIgnoreCase(m_strPriceKey)
        || strKey.equalsIgnoreCase(m_strTaxPriceKey)
        //����
        || strKey.equalsIgnoreCase(m_strPrice_LocalKey)
        || strKey.equalsIgnoreCase(m_strTaxPrice_LocalKey)
        || strKey.equalsIgnoreCase(m_strNet_Price_LocalKey)
        || strKey.equalsIgnoreCase(m_strNet_TaxPrice_LocalKey)
        //����
        || strKey.equalsIgnoreCase(m_strNet_Price_FractionalKey)
        || strKey.equalsIgnoreCase(m_strNet_TaxPrice_FractionalKey)
        || strKey.equalsIgnoreCase(m_strPrice_FractionalKey)
        || strKey.equalsIgnoreCase(m_strTaxPrice_FractionalKey)
        //����ԭ��
        || strKey.equalsIgnoreCase(m_strQt_NetPriceKey)
        || strKey.equalsIgnoreCase(m_strQt_NetTaxPriceKey)
        || strKey.equalsIgnoreCase(m_strQt_PriceKey)
        || strKey.equalsIgnoreCase(m_strQt_TaxPriceKey)
        //���۱��� 
        || strKey.equalsIgnoreCase(m_strQt_Net_Price_LocalKey)
        || strKey.equalsIgnoreCase(m_strQt_Net_TaxPrice_LocalKey)
        || strKey.equalsIgnoreCase(m_strQt_Price_LocalKey)
        || strKey.equalsIgnoreCase(m_strQt_TaxPrice_LocalKey)
        //������ԭ��
        || strKey.equalsIgnoreCase(m_strAssist_Price)
        || strKey.equalsIgnoreCase(m_strAssist_TaxPrice)) {
      iPower = getPowerPrice();
    }
    // ���ȣ�ԭ�ң�
    else if (strKey.equalsIgnoreCase(m_strTaxKey)
        || strKey.equalsIgnoreCase(m_strMoneyKey)
        || strKey.equalsIgnoreCase(m_strSummnyKey)) {
      if (m_iFinaFlag == ForFinance) {//����
        iPower = getPowerMnyFina();
      }
      else {//��Ӧ��
    	 String strCurrTypePk = getPkcorpOrCurrType("currtype");
    	 if (strCurrTypePk==null)
    		 iPower = DEFAULT_POWER_DATABASE;
    	 else
    		 iPower = getPowerMnyBusi(strCurrTypePk); 
      }
    }
    // ����(����)
    else if (strKey.equalsIgnoreCase(m_strTax_LocalKey)
    	     || strKey.equalsIgnoreCase(m_strMoney_LocalKey)
    	     || strKey.equalsIgnoreCase(m_strSummny_LocalKey)){
    	 if (m_iFinaFlag == ForFinance) {//����
    	        iPower = getPowerMnyFina(locaCurrType);
    	      }
    	      else {//��Ӧ��
    	    	  iPower = getPowerMnyBusi(locaCurrType);
    	      }
    }
    // ����(����)
    else if (strKey.equalsIgnoreCase(m_strTax_FractionalKey)
    	    || strKey.equalsIgnoreCase(m_strMoney_FractionalKey)
    	    || strKey.equalsIgnoreCase(m_strSummny_FractionalKey)){
    	 if (m_iFinaFlag == ForFinance) {//����
    	        iPower = getPowerMnyFina(fracCurrType);
    	      }
    	      else {//��Ӧ��
    	          iPower = getPowerMnyBusi(fracCurrType);
    	      }
    }                    
    // ˰�ʼ�����ȡ����ģ�徫��
    else if (strKey.equalsIgnoreCase(m_strTaxRateKey)) {
    	iPower = DEFAULT_POWER_TEMPLET;
    }
    //����ȡ6λ��v5.5�Ժ�ͳһҪ��
    else if (strKey.equalsIgnoreCase(m_strDiscountRateKey)
    		||strKey.equalsIgnoreCase(m_strAllDiscountRateKey)){
    	iPower = DEFAULT_POWER_DISCOUNTRATE;
    }
    
    // �����ȴ�������
    return ufdRet.add(ZERO, iPower, UFDouble.ROUND_HALF_UP);
  }

  /**
   * ��������VO����
   * NCRelationsCal ������ע�⡣
   */
  private SCMRelationsCal(
      int forWhichSystem, CircularlyAccessibleValueObject voCurr,
      int[] iaPrior, String strChangedKey, int[] nDescriptions, String[] strKeys, int[] forbidEditField) {

    super();

    initFormulaParse();

    m_iFinaFlag = forWhichSystem;//���� or ��Ӧ�����ñ�־

    m_voCurr = voCurr;//������vo

    initKeys(nDescriptions, strKeys);//��ʼ���ֶζ�Ӧ��ϵ
    
    setFirstChangedKey(strChangedKey,iaPrior);//���ñ仯KEY
    
    initCircle();//��ʼ�� KEY CIRCLE OBJ ��ֵ��ȫ��Ϊfalse��
    
    initObjects();//�ݴ��ֵ�������ع�
    
    initFormulaParseModel();//��ʼ����ʽ���������Ĺ�ϵ
    
    setForbidEditField(forbidEditField);//���ò��ɱ༭�ֶ�ӳ��
    
    setPrior(iaPrior);//�������ȼ�
  }
  
  /**
   * ��������VO����
   * NCRelationsCal ������ע�⡣
   */
  private SCMRelationsCal(int forWhichSystem,
			CircularlyAccessibleValueObject voCurrB,
			CircularlyAccessibleValueObject voCurrH, int[] iaPrior,
			String strChangedKey, int[] nDescriptions, String[] strKeys, int[] forbidEditField) {

    super();

    initFormulaParse();

    m_iFinaFlag = forWhichSystem;//���� or ��Ӧ�����ñ�־

    m_voCurr = voCurrB;//�����㵥������vo
    
    m_hvoCurr = voCurrH;//�������ͷvo

    initKeys(nDescriptions, strKeys);//��ʼ���ֶζ�Ӧ��ϵ
    
    setFirstChangedKey(strChangedKey,iaPrior);//���ñ仯KEY
    
    initCircle();//��ʼ�� KEY CIRCLE OBJ ��ֵ��ȫ��Ϊfalse��
    
    initObjects();//�ݴ��ֵ�������ع�
    
    initFormulaParseModel();//��ʼ����ʽ���������Ĺ�ϵ
    
    setForbidEditField(forbidEditField);//���ò��ɱ༭�ֶ�ӳ��
    
    setPrior(iaPrior);//�������ȼ�
  }
  
  /**
   * ���ڱ༭��ͷ�ֶ�ʱ����ʼ���㷨��ĿǰӦ��Ϊ���༭��ͷ�����ۿۣ�
   * ���ҽ��������ҵļ���
   */
  /*private SCMRelationsCal(int forWhichSystem,
			CircularlyAccessibleValueObject H_voCurr,
			CircularlyAccessibleValueObject[] B_voCurr, Object oldValue,
			int[] iaPrior, String strChangedKey, int[] nDescriptions,
			String[] strKeys, int[] forbidEditField) {

	    super();

	    initFormulaParse();

	    m_iFinaFlag = forWhichSystem;//���� or ��Ӧ�����ñ�־

	    m_bvoCurr = B_voCurr;//���������vo����
	    
	    m_hvoCurr = H_voCurr;//�������ͷvo

	    initKeys(nDescriptions, strKeys);//��ʼ���ֶζ�Ӧ��ϵ
	    
	    setFirstChangedKey(strChangedKey);//���ñ仯KEY
	    
	    initCircle();//��ʼ�� KEY CIRCLE OBJ ��ֵ��ȫ��Ϊfalse��
	    
	    initObjects();//�ݴ��ֵ�������ع�
	    
	    initFormulaParseModel();//��ʼ����ʽ���������Ĺ�ϵ
	    
	    setForbidEditField(forbidEditField);//���ò��ɱ༭�ֶ�ӳ��
	    
	    setPrior(iaPrior);//�������ȼ�
  }*/

  /**
   * �༭��ͷ�ֶ�ʱ����
   * @param HeadoldValue
   */
  private String calculate(Object HeadoldValue) {
	  //�༭�����ۿ�
	  if (getFirstChangedKey().equals(m_strAllDiscountRateKey)){
		  //ԭ������˰�ϼ�
		  UFDouble old_headsummny = getUFDoubleH(m_strheadstrSummnyKey);
		  //ԭ�����ۿ�
		  UFDouble old_alldiscount = PuPubVO.getUFDouble_ValueAsValue(HeadoldValue);
		  //�������ۿ�
		  UFDouble new_alldiscount = getUFDoubleH(m_strAllDiscountRateKey);
		  //��������˰�ϼ� = ԭ�ܼ�˰�ϼ� / ԭ�����ۿ� * �������ۿ�
		  UFDouble new_headsummny = (old_headsummny.div(old_alldiscount)).multiply(new_alldiscount);
		  
		  //���¼����������µļ�˰�ϼ�
		  recalculateBodysummny(new_headsummny,old_headsummny);
	  }	  
	  return null;
  }
  
  /**
   * ���¼����������µļ�˰�ϼ�:���ո��м�˰�ϼƱ��ʼ�����м�˰�ϼƣ�β������һ��
   * @param new_headsummny
   * @param old_headsummny
   */
  private void recalculateBodysummny(UFDouble new_headsummny,UFDouble old_headsummny){
	  
  }
  
  /**
   * ���ߣ���ӡ�� ���ܣ����� �������� ���أ��� ���⣺�� ���ڣ�(2003-6-17 11:39:21) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
   */
  private String calculate() {

    // �õ��ı��KEY����ЧӰ��CIRCLE,����ִ��
    int iAvailableCircle = getAvailableCircleByKey(getFirstChangedKey());
    
    //v5.5֧�ֽ��н��ı��Ҽ��� zhangcheng jhp
    /*if (iAvailableCircle == CIRCLE_IMPOSSIBLE && 
    		!getFirstChangedKey().equals(m_strExchange_O_TO_BrateKey)) {
      return null;
    }*/

    // ��ʼִ��--ԭ��:���ʱ䶯������ԭ�ҵļ���
    if (!getFirstChangedKey().equals(m_strExchange_O_TO_BrateKey))
    	execCircle(iAvailableCircle);
    
    //�����Ҽ���
    exec_LocalFracCurr();

    // ���Ч�ʣ��ڲ���������£���߹�ʽ������Ч�� begin
    // m_pnlBill.getBillModel().getFormulaParse().setNeedDoPostConvert(bOldPostCovt)
    // ;

    //���ʼ��
    checkLocalRate();

    // ����Ƿ���ڲ�������ֵ
    isResultsRight();

    // ���������������ֵ�ظ�ԭ����ֵ
    if (m_strShowHintMsg != null) {
      setOldValuesToPanel();
    }
    // ����У�鴦��
    processAfterCal();

    debugPrint(new String[] {
      ""
    });

    return m_strShowHintMsg;
  }

	/**
	 * v5.5
	 * ���Լ����У��ҵ�񵥾��ϵ�ԭ���������ϵ��������ҽ��<>ԭ��*����/������ʱ����ǰ̨������
	 *         ��ȡ��һ��ʵ����ġ��������������������������Χ�ڣ�ֱ�Ӽ�������������
	 *         ���������������û���ʾ�����û�ѡ�������������ȡ����
	 *         ��̨����ʱ�����������������У����ʾ
	 *         
	 * true ---- ���ҽ��=ԭ��*����/������ 
	 * false --- ���ҽ��<>ԭ��*����/������ʱ,���ҳ��������ݲΧ
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
		
		//����VO�е�ԭ�ҽ����ʣ���������Ӧ�ı��ҽ��
		try {
			
			//�������ڡ�����
			String billDate = null;
			if (m_voCurr.getAttributeValue(m_strBillDateKey)== null )
				billDate = m_hvoCurr.getAttributeValue(m_strBillDateKey).toString();
			else
				billDate = m_voCurr.getAttributeValue(m_strBillDateKey).toString();
			
			if (pk_CurrType==null)
				// ԭ��PK
				if (m_voCurr.getAttributeValue(m_strCurrTypePkKey)==null && m_hvoCurr != null)
					pk_CurrType = m_hvoCurr.getAttributeValue(m_strCurrTypePkKey).toString();
				else
					pk_CurrType = m_voCurr.getAttributeValue(m_strCurrTypePkKey).toString();
				
			if (locaCurrType==null)
				locaCurrType = CurrParamQuery.getInstance().getLocalCurrPK(getCorpId());
			
			UFDouble brate = SmartVODataUtils
					.getUFDouble(getBusinessCurrencyRateUtil().getRate(
							pk_CurrType, locaCurrType, billDate));
			//����Ϊ����У��
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
			
			// ԭ�ҽ��
			UFDouble[] amounts = new UFDouble[] { SmartVODataUtils
					.getUFDouble(m_voCurr.getAttributeValue(key_org)) };
			// VO�еı��ҽ��
			UFDouble moneyLocal = SmartVODataUtils.getUFDouble(m_voCurr
					.getAttributeValue(key_loc));
			moneyLocal = moneyLocal == null ? UFDouble.ZERO_DBL : moneyLocal;
			
			//�����ı��ҽ��
			UFDouble[] result = null;
			
			//���Ҳ��񾫶�
/*			ICurrtype currQrySrv = (ICurrtype) NCLocator.getInstance().lookup(ICurrtype.class.getName());
			CurrtypeVO voRet = currQrySrv.findCurrtypeVOByPK(locaCurrType);*/
			CurrtypeVO voRet = CurrtypeQuery.getInstance().getCurrtypeVO(locaCurrType);
			
			// ����
			int mnyRet = m_iFinaFlag == this.ForFinance ? voRet
					.getCurrdigit().intValue() : voRet.getCurrbusidigit().intValue();
				
			//���Ҽ���
			result = getBusinessCurrencyRateUtil().getAmountsByOpp(
					pk_CurrType, locaCurrType, amounts, brate, billDate,mnyRet);
			
			//���ҽ��<>ԭ��*����/������
			UFDouble submny = moneyLocal.sub(result[0] == null ? UFDouble.ZERO_DBL : result[0]).abs();
			//����������
			UFDouble maxconverr = getBusinessCurrencyRateUtil().getCurrinfoVO(pk_CurrType, locaCurrType).getMaxconverr();
			maxconverr = (maxconverr==null?UFDouble.ZERO_DBL:maxconverr);
			if (submny.doubleValue()!=0){
				if (submny.compareTo(maxconverr)>0)
					this.ratemny = maxconverr;
			}	
		} catch (BusinessException e) {
			nc.vo.scm.pub.SCMEnv.out("��ȡ���˹�����ʧ��\n" + e.getMessage());
		}
  }
  
  
  /**
   * ����ԭ�ҶԱ���\���ҵļ��� 2008-03-17
   */
  private void exec_LocalFracCurr(){
	  // �Ƿ���б���\���ҵļ���
		if (!isCalLocalFrac_CurrEnable())
			return;
		try {
			
			// ԭ��PK
			if (m_voCurr.getAttributeValue(m_strCurrTypePkKey) == null && m_hvoCurr != null) {
				if (m_hvoCurr.getAttributeValue(m_strCurrTypePkKey) != null)
					pk_CurrType = m_hvoCurr.getAttributeValue(m_strCurrTypePkKey).toString();
			}
			else
				pk_CurrType = m_voCurr.getAttributeValue(m_strCurrTypePkKey).toString();
			
			if (pk_CurrType == null)
				return;
			
			// ����PK
			locaCurrType = CurrParamQuery.getInstance().getLocalCurrPK(getCorpId());
			// ����PK
			fracCurrType = CurrParamQuery.getInstance().getFracCurrPK(getCorpId());
			
			// ��������
			String billDate = null;
			if (m_voCurr.getAttributeValue(m_strBillDateKey)== null )
				billDate = m_hvoCurr.getAttributeValue(m_strBillDateKey).toString();
			else
				billDate = m_voCurr.getAttributeValue(m_strBillDateKey).toString();
			
			// ���۾���
			int priceRet = getPowerPrice();
			
			// ����
			int mnyRet = m_iFinaFlag==ForScm ? getPowerMnyBusi(locaCurrType) : getPowerMnyFina(locaCurrType);
			
			// �۱�����
			UFDouble nRate = null;
			if (m_voCurr.getAttributeValue(m_strExchange_O_TO_BrateKey)== null )
				nRate = (UFDouble) m_hvoCurr.getAttributeValue(m_strExchange_O_TO_BrateKey);
			else
				nRate = (UFDouble) m_voCurr.getAttributeValue(m_strExchange_O_TO_BrateKey);
			
			//���е�ԭ�Ҽ۸�
			UFDouble[] prices = new UFDouble[] { 
					getUFDouble(m_strPriceKey),getUFDouble(m_strTaxPriceKey),
					getUFDouble(m_strNetPriceKey),getUFDouble(m_strNetTaxPriceKey),
					getUFDouble(m_strQt_PriceKey),getUFDouble(m_strQt_TaxPriceKey),
					getUFDouble(m_strQt_NetPriceKey),getUFDouble(m_strQt_NetTaxPriceKey)};
			//���еı��Ҽ۸�
			UFDouble[] priceresult = null;
			
			//���ԭ�Ҽ�˰�ϼƺ���˰����Ϊ0,ֱ�ӽ����ҽ����Ϊ0,�����û�������
			if ((getUFDouble(m_strSummnyKey)==null&&getUFDouble(m_strMoneyKey)==null)
					||((getUFDouble(m_strSummnyKey)!=null && getUFDouble(m_strSummnyKey).doubleValue()==0 
					&& getUFDouble(m_strMoneyKey)!=null && getUFDouble(m_strMoneyKey).doubleValue()==0))){
				m_voCurr.setAttributeValue(m_strSummny_LocalKey, ZERO);//���Ҽ�˰�ϼ�
				m_voCurr.setAttributeValue(m_strMoney_LocalKey, ZERO);//������˰���
				m_voCurr.setAttributeValue(m_strTax_LocalKey, ZERO);//������˰���
				m_voCurr.setAttributeValue(m_strDiscountMny_LocalKey, ZERO);//�����ۿ۶�
				
				//���Ҽ���																 
				priceresult = getBusinessCurrencyRateUtil().getAmountsByOpp(
						pk_CurrType, locaCurrType, prices, nRate, billDate,priceRet);
				
				m_voCurr.setAttributeValue(m_strPrice_LocalKey, priceresult[0]);//������˰����
				m_voCurr.setAttributeValue(m_strTaxPrice_LocalKey, priceresult[1]);//���Һ�˰����
				m_voCurr.setAttributeValue(m_strNet_Price_LocalKey, priceresult[2]);//������˰����
				m_voCurr.setAttributeValue(m_strNet_TaxPrice_LocalKey, priceresult[3]);//���Һ�˰����
				m_voCurr.setAttributeValue(m_strQt_Price_LocalKey, priceresult[4]);//���ұ��ۼ�����λ��˰����
				m_voCurr.setAttributeValue(m_strQt_TaxPrice_LocalKey, priceresult[5]);//���ұ��ۼ�����λ��˰����
				m_voCurr.setAttributeValue(m_strQt_Net_Price_LocalKey, priceresult[6]);//���ұ��ۼ�����λ��˰����
				m_voCurr.setAttributeValue(m_strQt_Net_TaxPrice_LocalKey, priceresult[7]);//���ұ��ۼ�����λ��˰����
				
				//�Ƿ������Һ���
				if (CurrParamQuery.getInstance().isBlnLocalFrac(getCorpId())){
					m_voCurr.setAttributeValue(m_strSummny_FractionalKey, ZERO);//���Ҽ�˰�ϼ�
					m_voCurr.setAttributeValue(m_strMoney_FractionalKey, ZERO);//������˰���
					m_voCurr.setAttributeValue(m_strDiscountMny_FractionalKey, ZERO);//�����ۿ۶�
					m_voCurr.setAttributeValue(m_strPrice_FractionalKey, ZERO);//������˰����
					m_voCurr.setAttributeValue(m_strTaxPrice_FractionalKey, ZERO);//���Һ�˰����
					m_voCurr.setAttributeValue(m_strNet_Price_FractionalKey, ZERO);//������˰����
					m_voCurr.setAttributeValue(m_strNet_TaxPrice_FractionalKey, ZERO);//���Һ�˰����
				}
				
				//�����б��ۼ�����λ���ҵ��ۼ���
				exec_QtLocalPrice();
				
				return;
			}
			//�������Һ��㲢�ұ���=Դ��,���б���ֱ��ȡԴ��
			else if (!CurrParamQuery.getInstance().isBlnLocalFrac(getCorpId()) && pk_CurrType.equals(locaCurrType)){
				m_voCurr.setAttributeValue(m_strSummny_LocalKey, m_voCurr.getAttributeValue(m_strSummnyKey));//���Ҽ�˰�ϼ�
				m_voCurr.setAttributeValue(m_strMoney_LocalKey, m_voCurr.getAttributeValue(m_strMoneyKey));//������˰���
				m_voCurr.setAttributeValue(m_strTax_LocalKey, m_voCurr.getAttributeValue(m_strTaxKey));//������˰���
				m_voCurr.setAttributeValue(m_strDiscountMny_LocalKey, m_voCurr.getAttributeValue(m_strDiscountMnyKey));//�����ۿ۶�
				
				m_voCurr.setAttributeValue(m_strPrice_LocalKey, m_voCurr.getAttributeValue(m_strPriceKey));//������˰����
				m_voCurr.setAttributeValue(m_strTaxPrice_LocalKey, m_voCurr.getAttributeValue(m_strTaxPriceKey));//���Һ�˰����
				m_voCurr.setAttributeValue(m_strNet_Price_LocalKey, m_voCurr.getAttributeValue(m_strNetPriceKey));//������˰����
				m_voCurr.setAttributeValue(m_strNet_TaxPrice_LocalKey, m_voCurr.getAttributeValue(m_strNetTaxPriceKey));//���Һ�˰����
				
				m_voCurr.setAttributeValue(m_strQt_Price_LocalKey, m_voCurr.getAttributeValue(m_strQt_PriceKey));//���ұ��ۼ�����λ��˰����
				m_voCurr.setAttributeValue(m_strQt_TaxPrice_LocalKey, m_voCurr.getAttributeValue(m_strQt_TaxPriceKey));//���ұ��ۼ�����λ��˰����
				m_voCurr.setAttributeValue(m_strQt_Net_Price_LocalKey, m_voCurr.getAttributeValue(m_strQt_NetPriceKey));//���ұ��ۼ�����λ��˰����
				m_voCurr.setAttributeValue(m_strQt_Net_TaxPrice_LocalKey, m_voCurr.getAttributeValue(m_strQt_NetTaxPriceKey));//���ұ��ۼ�����λ��˰����
				
				return;
			}
			
			//ԭ�Ҽ�˰�ϼơ���˰���ۿ۶�
			UFDouble[] amounts = new UFDouble[] { getUFDouble(m_strSummnyKey),
					getUFDouble(m_strMoneyKey),
					getUFDouble(m_strDiscountMnyKey) };
			//�����ļ�˰�ϼơ���˰���ۿ۶�
			UFDouble[] result = null;
			
			// �Ƿ���������Һ���
			if (CurrParamQuery.getInstance().isBlnLocalFrac(getCorpId())
					&& (isCalFieldExist(new String[] {
							m_strSummny_FractionalKey,m_strTax_LocalKey,
							m_strMoney_FractionalKey, m_strSummny_LocalKey,
							m_strMoney_LocalKey, m_strNumKey,m_strTax_FractionalKey }))) {// �����Һ���
				
				// �۸�����
				UFDouble aRate = null;
				if (m_voCurr.getAttributeValue(m_strExchange_O_TO_ArateKey)== null )
					aRate = (UFDouble) m_hvoCurr.getAttributeValue(m_strExchange_O_TO_ArateKey);
				else
					aRate = (UFDouble) m_voCurr.getAttributeValue(m_strExchange_O_TO_ArateKey);
				
				//���Ҽ���
				result = getBusinessCurrencyRateUtil().getAmountsByOpp(
						pk_CurrType, fracCurrType, amounts, aRate, billDate,
						mnyRet);
				m_voCurr.setAttributeValue(m_strSummny_FractionalKey, result[0]);//���Ҽ�˰�ϼ�
				m_voCurr.setAttributeValue(m_strMoney_FractionalKey, result[1]);//������˰���
				if (!m_strDiscountMny_FractionalKey.equals(""))
					m_voCurr.setAttributeValue(m_strDiscountMny_FractionalKey, result[2]);//�����ۿ۶�
				//Դ��=���ң���۸�ֱ�Ӹ�ֵ
				if (pk_CurrType.equals(fracCurrType)){
					m_voCurr.setAttributeValue(m_strTax_FractionalKey, m_voCurr.getAttributeValue(m_strTaxKey));//����˰��
					m_voCurr.setAttributeValue(m_strPrice_FractionalKey, m_voCurr.getAttributeValue(m_strPriceKey));//������˰����
					m_voCurr.setAttributeValue(m_strTaxPrice_FractionalKey, m_voCurr.getAttributeValue(m_strTaxPriceKey));//���Һ�˰����
					m_voCurr.setAttributeValue(m_strNet_Price_FractionalKey, m_voCurr.getAttributeValue(m_strNetPriceKey));//������˰����
					m_voCurr.setAttributeValue(m_strNet_TaxPrice_FractionalKey, m_voCurr.getAttributeValue(m_strNetTaxPriceKey));//���Һ�˰����
				}
				else
					exec_FracPrice();//���㸨�Ҽ۸񣬲����û�m_voCurr
				
				//���Ҽ���
				//1.ȡ���ҵ����ҵĻ���
				nRate = getBusinessCurrencyRateUtil().getRate(fracCurrType, locaCurrType, billDate);
				//2.���㱾��
				result = getBusinessCurrencyRateUtil().getAmountsByOpp(
						fracCurrType, locaCurrType, result, nRate, billDate,
						mnyRet);
				m_voCurr.setAttributeValue(m_strSummny_LocalKey, result[0]);//���Ҽ�˰�ϼ�
				m_voCurr.setAttributeValue(m_strMoney_LocalKey, result[1]);//������˰���
				if (!m_strDiscountMny_LocalKey.equals(""))
				    m_voCurr.setAttributeValue(m_strDiscountMny_LocalKey, result[2]);//�����ۿ۶�
				//����=���ң���۸�ֱ�Ӹ�ֵ
				if (fracCurrType.equals(locaCurrType)){
					m_voCurr.setAttributeValue(m_strTax_LocalKey, m_voCurr.getAttributeValue(m_strTax_FractionalKey));//����˰��
					m_voCurr.setAttributeValue(m_strPrice_LocalKey, m_voCurr.getAttributeValue(m_strPrice_FractionalKey));//������˰����
					m_voCurr.setAttributeValue(m_strTaxPrice_LocalKey, m_voCurr.getAttributeValue(m_strTaxPrice_FractionalKey));//���Һ�˰����
					m_voCurr.setAttributeValue(m_strNet_Price_LocalKey, m_voCurr.getAttributeValue(m_strNet_Price_FractionalKey));//������˰����
					m_voCurr.setAttributeValue(m_strNet_TaxPrice_LocalKey, m_voCurr.getAttributeValue(m_strNet_TaxPrice_FractionalKey));//���Һ�˰����
				}
				else
					exec_LocalPrice();//���㱾�Ҽ۸񣬲����û�m_voCurr
				
			} else {// �����Һ���,ֻ���㱾��
				//���Ҽ���																 
				result = getBusinessCurrencyRateUtil().getAmountsByOpp(
						pk_CurrType, locaCurrType, amounts, nRate, billDate,
						mnyRet);
				m_voCurr.setAttributeValue(m_strSummny_LocalKey, result[0]);//���Ҽ�˰�ϼ�
				m_voCurr.setAttributeValue(m_strMoney_LocalKey, result[1]);//������˰���
				if (!m_strDiscountMny_LocalKey.equals(""))
				    m_voCurr.setAttributeValue(m_strDiscountMny_LocalKey, result[2]);//�����ۿ۶�
				exec_LocalPrice();//�������Ҽ۸񣬲����û�m_voCurr
			}
			
			//�����б��ۼ�����λ���ҵ��ۼ���
			exec_QtLocalPrice();
			
		} catch (Exception e) {
			nc.vo.scm.pub.SCMEnv.error("���㱾�ҵ��۽��ʧ��!", e);
		}
		return;
  }
  
  /**
   * ���㸨�Ҽ۸񣬲����û�m_voCurr
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
   * ���㱾�Ҽ۸񣬲����û�m_voCurr
   */
  private void exec_LocalPrice(){
	  if (!m_strTax_LocalKey.equals(""))
		  execFormulas(new String[] {m_strTax_LocalKey + "->" + m_strSummny_LocalKey + "-" + m_strMoney_LocalKey});
	  
	  if ("".equals(m_strSummny_LocalKey)&&"".equals(m_strMoney_LocalKey))
		  return;
	  
	  //����
	  if (isCalFieldExist(new String[] { m_strNet_TaxPrice_LocalKey,
				m_strNet_Price_LocalKey, m_strNumKey })){
		  String[] formulas = new String[] {
				  m_strNet_TaxPrice_LocalKey + "->" + m_strSummny_LocalKey + "/" + m_strNumKey ,
			      m_strNet_Price_LocalKey + "->" + m_strMoney_LocalKey + "/" + m_strNumKey 
			  };
		  execFormulas(formulas);
	  }
	  
	  //����
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
   * ���㱨�ۼ�����λ���Ҽ۸񣬲����û�m_voCurr
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
	 * @return �������˹����ࣺ���ڼ��㱾/����
	 */
  private BusinessCurrencyRateUtil getBusinessCurrencyRateUtil(){
	  if (bcurr == null){
		  try {
			if (m_voCurr.getAttributeValue(m_strPk_CorpKey)!=null)
				bcurr = new BusinessCurrencyRateUtil(m_voCurr.getAttributeValue(m_strPk_CorpKey).toString());
			else
				bcurr = new BusinessCurrencyRateUtil(m_hvoCurr.getAttributeValue(m_strPk_CorpKey).toString());
		} catch (Exception e) {
			nc.vo.scm.pub.SCMEnv.out("��ȡ���˹�����ʧ��\n" + e.getMessage());
			return null;
		}		  
	  }
	  return bcurr;
  }
/**
* @return ��˾���������ڼ�����
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
   * ���ߣ���ӡ�� ���ܣ����ڸ��ٴ�ӡ��ʽ �������� ���أ��� ���⣺�� ���ڣ�(2003-06-09 11:22:51)
   * �޸�����2008-03-14���޸��� �ųɣ��޸�ԭ�� �������Ĵ�ӡ���ܣ�ע�ͱ�־��
   */
  private void debugPrint(String[] saFormula) {
    if (saFormula == null) {
      return;
    }
    if (true) {
      int iLen = saFormula.length;
      if (debug)
        SCMEnv.out("\n~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n");
      String split = "->*/+-()";//�ָ��־�ַ���Ϊ����ʾ���ģ�
      StringTokenizer token = null;//��ŷָ�����ĵ���
      String cur_token = null;//��ǰ����
      String chineseFormula = null;//���Ĺ�ʽ
      String keyValueFormula = null;//�ֶ�ʵ��ֵ
      String curChineseName = null;//��ǰ�ֶ�������
      for (int i = 0; i < iLen; i++) {
    	  chineseFormula = saFormula[i];
    	  keyValueFormula = saFormula[i];
    	  token = new StringTokenizer(saFormula[i], split);//�ָ��
    	  while (token.hasMoreTokens()) {//ƴ�����Ĺ�ʽ
    		cur_token = token.nextToken();
    		curChineseName = getChineseName(cur_token);
    		if (!cur_token.equals(curChineseName)){
    			//��ǰ�ֶβ�������
    			chineseFormula = chineseFormula.replaceAll(cur_token, curChineseName);
    			keyValueFormula = m_voCurr.getAttributeValue(cur_token)==null ? "" :
    				keyValueFormula.replaceAll(cur_token, m_voCurr.getAttributeValue(cur_token).toString());
    		}
  		  }
    	  if (debug){
    		  nc.vo.scm.pub.SCMEnv.out(chineseFormula + " ### " + saFormula[i]);//��ӡ��ʽ(���� + ԭʼ)
    		  nc.vo.scm.pub.SCMEnv.out(keyValueFormula);//��ӡ��ʽ(��ֵ) 
    	  }
    	  
      }// end for
      
    }// end if
    
  }

  /**
   * @param cur_token
   * @return ���ش����ַ����������ƣ���������֣�ֱ�ӷ���
   */
  private String getChineseName(String cur_token){
	  if (m_strNumKey.equals(cur_token))  return "������";
	  else if (m_strTaxRateKey.equals(cur_token))  return "˰��";
	  else if (m_strDiscountRateKey.equals(cur_token))  return "��Ʒ�ۿ�";
	  else if (m_strAllDiscountRateKey.equals(cur_token))  return "�����ۿ�";
	  else if (m_strConvertRateKey.equals(cur_token))  return "������";
	  else if (m_strQt_ConvertRateKey.equals(cur_token))  return "���۵�λ������";
	  else if (m_strAssistNumKey.equals(cur_token))  return "������";
	  else if (m_strUnitID.equals(cur_token))  return "����λ";
	  else if (m_strQuoteUnitID.equals(cur_token))  return "���ۼ�����λ";
	  else if (m_strCurrTypePkKey.equals(cur_token))  return "ԭ��PK";
	  else if (m_strPk_CorpKey.equals(cur_token))  return "��˾PK";
	  else if (m_strExchange_O_TO_BrateKey.equals(cur_token))  return "�۱�����";
	  else if (m_strBillDateKey.equals(cur_token))  return "��������";
	  
	  //ԭ��
	  else if (m_strNetPriceKey.equals(cur_token))  return "��˰����";
	  else if (m_strNetTaxPriceKey.equals(cur_token))  return "��˰����";
	  else if (m_strPriceKey.equals(cur_token))  return "��˰����";
	  else if (m_strTaxPriceKey.equals(cur_token))  return "��˰����";
	  else if (m_strSummnyKey.equals(cur_token))  return "��˰�ϼ�";
	  else if (m_strMoneyKey.equals(cur_token))  return "��˰���";
	  else if (m_strTaxKey.equals(cur_token))  return "˰��";
	  else if (m_strDiscountMnyKey.equals(cur_token))  return "�ۿ۶�";
	  else if (m_strheadstrSummnyKey.equals(cur_token))  return "������˰�ϼ�";
	  
	  else if (m_strQt_NumKey.equals(cur_token))  return "���۵�λ����";
	  else if (m_strQt_TaxPriceKey.equals(cur_token))  return "���۵�λ��˰����";
	  else if (m_strQt_PriceKey.equals(cur_token))  return "���۵�λ��˰����";
	  else if (m_strQt_NetTaxPriceKey.equals(cur_token))  return "���۵�λ��˰����";
	  else if (m_strQt_NetPriceKey.equals(cur_token))  return "���۵�λ��˰����";
	  
	  else if (m_strAssist_Price.equals(cur_token))  return "��������˰����";
	  else if (m_strAssist_TaxPrice.equals(cur_token))  return "��������˰����";
	  else if (m_strAsk_TaxPriceKey.equals(cur_token))  return "ѯ�ۺ�˰����";
	  else if (m_strAsk_PriceKey.equals(cur_token))  return "ѯ����˰����";

	  //����
	  else if (m_strPrice_LocalKey.equals(cur_token))  return "������˰����";
	  else if (m_strTaxPrice_LocalKey.equals(cur_token))  return "���Һ�˰����";
	  else if (m_strNet_Price_LocalKey.equals(cur_token))  return "������˰����";
	  else if (m_strNet_TaxPrice_LocalKey.equals(cur_token))  return "���Һ�˰����";
	  else if (m_strTax_LocalKey.equals(cur_token))  return "����˰��";
	  else if (m_strMoney_LocalKey.equals(cur_token))  return "������˰���";
	  else if (m_strSummny_LocalKey.equals(cur_token))  return "���Ҽ�˰�ϼ�";
	  else if (m_strDiscountMny_LocalKey.equals(cur_token))  return "�����ۿ۶�";
	  
	  else if (m_strQt_Price_LocalKey.equals(cur_token))  return "���۵�λ������˰����";
	  else if (m_strQt_TaxPrice_LocalKey.equals(cur_token))  return "���۵�λ���Һ�˰����";
	  else if (m_strQt_Net_Price_LocalKey.equals(cur_token))  return "���۵�λ������˰����";
	  else if (m_strQt_Net_TaxPrice_LocalKey.equals(cur_token))  return "���۵�λ���Һ�˰����";
	  
	  //����
	  else if (m_strPrice_FractionalKey.equals(cur_token))  return "������˰����";
	  else if (m_strTaxPrice_FractionalKey.equals(cur_token))  return "���Һ�˰����";
	  else if (m_strNet_Price_FractionalKey.equals(cur_token))  return "������˰����";
	  else if (m_strNet_TaxPrice_FractionalKey.equals(cur_token))  return "���Һ�˰����";
	  else if (m_strTax_FractionalKey.equals(cur_token))  return "����˰��";
	  else if (m_strMoney_FractionalKey.equals(cur_token))  return "������˰���";
	  else if (m_strSummny_FractionalKey.equals(cur_token))  return "���Ҽ�˰�ϼ�";
	  else if (m_strDiscountMny_FractionalKey.equals(cur_token))  return "�����ۿ۶�";

	  else if (m_strQualifiedNumKey.equals(cur_token))  return "�ϸ�����";
	  else if (m_strUnQualifiedNumKey.equals(cur_token))  return "���ϸ�����";
	  
	  return cur_token;//������ֵ
  }
  
  /**
   * ������ȱ༭��������������������λ������������Ӧ�뵱ǰ�༭ֵ����һ��
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
   * ���ߣ���ӡ�� ���ܣ��ж�����ĸ�CIRCLE����ִ�� ������int circle ����CIRCLE ���أ��� ���⣺�� ���ڣ�(2002-5-15
   * 11:39:21) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
   */
  private void execCircle(int circle) {

    if (circle == CIRCLE_IMPOSSIBLE) {
      return;
    }
    
    // ������ȱ༭��������������������λ������������Ӧ�뵱ǰ�༭ֵ����һ��
    if (getFirstChangedKey().equals(m_strNumKey)
    		|| getFirstChangedKey().equals(m_strAssistNumKey)
    		|| getFirstChangedKey().equals(m_strQt_NumKey))
    	execCircle_Num();
    
    // ����������ȵ�ִ��
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
    /**v5.3 �ı����̣�ʹ����˰����ʱ����֤����֮���˰�ʹ�ϵ,�����Ǻ�˰�۵Ŀ��ʹ�ϵ���ʽ�����ע�͵� */	
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
     /**v5.3 �ı����̣�ʹ�ú�˰����ʱ����֤����֮���˰�ʹ�ϵ,��������˰�۵Ŀ��ʹ�ϵ���ʽ�����ע�͵� */	
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
    else if (circle == CIRCLE_QT_TAXPRICE_PRICE) {//˰�ʡ�����ԭ�Һ�˰���ۡ�����ԭ����˰����
    	execCircle_Qt_TaxPricePrice(getFirstChangedKey(), true);
    }
    else if (circle == CIRCLE_QT_NETTAXPRICE_NETPRICE) {//˰�ʡ�����ԭ�Һ�˰���ۡ�����ԭ����˰����
    	execCircle_Qt_NetTaxPriceNetPrice(getFirstChangedKey(), true);
    }
    else if (circle == CIRCLE_QT_TAXPRICE_NETTAXPRICE) {// ���ʡ�����ԭ�Һ�˰���ۡ�����ԭ�Һ�˰����
    	execCircle_Qt_TaxPriceNetTaxPrice(getFirstChangedKey(), true);
    }
    else if (circle == CIRCLE_QT_PRICE_NETPRICE) {// ���ʡ�����ԭ����˰���ۡ�����ԭ����˰����
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
    
    /**���г�FirstChangedKey����������λ֮�����һ�ּ�����λԭ�ҵļ���(����������/���ۼ���)*/
    execCircle_AnotherUnit(getFirstChangedKey());
    
    /**�����и�������λ���۵ļ���*/
    execCircle_ViaPrice(getFirstChangedKey());
    
    /**���۱༭ʲô�ֶ�(��˰��),��󶼻�����ۿ۶�ļ��� */
    execCircle_DiscountMny(getFirstChangedKey(),false);
    
    return;
  }
  
  /**
   * ���г�FirstChangedKey����������λ֮�����һ�ּ�����λԭ�ҵļ���
   * ���FirstChangedKey---��������λ,����б��ۼ�����λ�ļ���
   * ���FirstChangedKey---���ۼ�����λ,�������������λ�ļ���
   * @param sChangedKey
   */
  private void execCircle_AnotherUnit(String sChangedKey){
	  if ((m_strUnitID == null || m_strQuoteUnitID == null
				|| m_strUnitID.equals("") || m_strQuoteUnitID.equals(""))
				&& (getUFDouble(m_strNumKey) == null || getUFDouble(m_strQt_NumKey) == null))
			return;
	  
	  //���ȱ䶯���Ǳ��ۼ�����λ
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
		  //��\��������λ��ͬ,�������ȱ༭ֵ�Ǳ������������ۻ�����:�Լ�˰�ϼƻ���˰���Ϊ�����㣬������������λ�ļ���
		  else if ( !sChangedKey.equals(m_strQt_NumKey) && !sChangedKey.equals(m_strQt_ConvertRateKey)){
			  reInitData(true);//���³�ʼ������
			  if (sChangedKey.equals(m_strQt_TaxPriceKey) || sChangedKey.equals(m_strQt_NetTaxPriceKey))
				  execCircle_NumNetTaxPriceSummny(m_strSummnyKey);
			  else
				  execCircle_NumNetPriceMoney(m_strMoneyKey);
		  }
	  }
	  //���ȱ䶯������������λ
	  else{
		  //���û�б��ۼ�����λ�����������б��ۼ�����λ��ؼ���
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
          //���ȱ䶯��Ϊ˰�ʣ�ֻ���㱨��ԭ����˰��\����
          else {
        	  if (sChangedKey.equals(m_strTaxRateKey)){
        		  execCircle_Qt_TaxPricePrice(m_strTaxRateKey,false);
        		  execCircle_Qt_NetTaxPriceNetPrice(m_strTaxRateKey,false);
        	  }
        	  //��\��������λ��ͬ,����(�䶯�����ʻ��߱��ۼ۸�=null)�����ȱ䶯�ķ�˰��:�Լ�˰�ϼƻ���˰���Ϊ�����㣬���б��ۼ�����λ�ļ���
    		  else if (!isQt_FixedConvertRate()
						|| (getUFDouble(m_strQt_NetTaxPriceKey) == null
								|| getUFDouble(m_strQt_NetPriceKey) == null
								|| getUFDouble(m_strQt_TaxPriceKey) == null || getUFDouble(m_strQt_PriceKey) == null)) {
    			  reInitData(false);//���³�ʼ������
    			  //��˰����
    			  if (sChangedKey.equals(m_strTaxPriceKey)||sChangedKey.equals(m_strNetTaxPriceKey)
    					  ||sChangedKey.equals(m_strSummnyKey))
    				  execCircle_Qt_NumNetTaxPriceSummny(m_strSummnyKey);
    			  //��˰����
    			  else if (sChangedKey.equals(m_strPriceKey)||sChangedKey.equals(m_strNetPriceKey)
    					  ||sChangedKey.equals(m_strMoneyKey))
    				  execCircle_Qt_NumNetPriceMoney(m_strMoneyKey);
    			  //��˰����
    			  else if (getPrior_Price_TaxPrice() == RelationsCalVO.PRICE_PRIOR_TO_TAXPRICE)
    				  execCircle_Qt_NumNetPriceMoney(m_strMoneyKey);
    			  //��˰����
    			  else if (getPrior_Price_TaxPrice() == RelationsCalVO.TAXPRICE_PRIOR_TO_PRICE)
    				  execCircle_Qt_NumNetTaxPriceSummny(m_strSummnyKey);
    		  }
          }
	  }
  }
  
  /**
   * ���ڵڶ��μ�����һ�ּ�����λ�۸�ʱ�����³�ʼ������
   * qt--true:  ���ȱ䶯���Ǳ��ۼ�����λ
   * qt--false: ���ȱ䶯������������λ
   */
  private void reInitData(boolean qt){
	  initCircle();//�������ø���Circleû��ִ�й�
	  
	  //���ȱ䶯���Ǳ��ۼ�����λ:��������4���۸���Ϊ�����Ĺ�
	  if (qt){
		  setKeyExecuted(m_strTaxPriceKey, false);
		  setKeyExecuted(m_strNetTaxPriceKey, false);
		  setKeyExecuted(m_strPriceKey, false);
		  setKeyExecuted(m_strNetPriceKey, false);
	  }
	  //���ȱ䶯������������λ�������ۼ���4���۸���Ϊ�����Ĺ�
	  else{
		  setKeyExecuted(m_strQt_TaxPriceKey, false);
		  setKeyExecuted(m_strQt_NetTaxPriceKey, false);
		  setKeyExecuted(m_strQt_PriceKey, false);
		  setKeyExecuted(m_strQt_NetPriceKey, false);
	  }
  }
  
  /**
   * ���и�������λ�ļ���--�༭���������������ʡ�˰��ʱ�����м���
   * @param sChangedKey
   */
  private void execCircle_ViaPrice(String sChangedKey){
	  // ��ִ�й��򲻿�ִ��,����ִ��
	  if (isCircleExecuted(CIRCLE_VIAPRICE)
		    || !isCircleExecutable(CIRCLE_VIAPRICE, sChangedKey))
		  return;
		    
		String formulas[] = {m_strAssist_TaxPrice + "->(" + m_strSummnyKey + "+" + m_strDiscountMnyKey + ")/" + m_strAssistNumKey ,
				m_strAssist_Price + "->(" + m_strSummnyKey + "+" + m_strDiscountMnyKey + ")/[(1.0+" 
			         + m_strTaxRateKey + "/100.0)*" + m_strAssistNumKey + "]" };
		execFormulas(formulas);
		// ���ø�KEY�Ѹı�
		setKeysExecuted(CIRCLE_VIAPRICE, true);
		// ����ִ�б��
		setCircleExecuted(CIRCLE_VIAPRICE, true);
		return;
	  
  }
  
  /**
   * ���ߣ��ų� ���ܣ���˰���ۡ���������˰�ϼơ��ۿ۶�CIRCLE��,����һ������ı������������� 
   * ���༭˰��ʱ����Ȧ�����м���  ������String sChangedKey
   * Ӱ���CIRCLE���� ���أ��� ���⣺�� ���ڣ�(2008-3-14 11:39:21) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
   */
  private void execCircle_DiscountMny(String sChangedKey,boolean bExecFollowing) {
	// ��ִ�й��򲻿�ִ��,����ִ��
	if (isCircleExecuted(CIRCLE_DISCOUNTMNY)
	    || !isCircleExecutable(CIRCLE_DISCOUNTMNY, sChangedKey))
	  return;
	    
	//������ۡ�������ֵ��ͬ�����ۿ۶�ǿ����0,����Ҫ�����ۿ۶�ļ���
    if (!processAfterCalDiscountMny())
      return;	
	
	String formulas[] = new String[] { m_strDiscountMnyKey + "->"
				+ m_strNumKey + "*" + m_strTaxPriceKey + "-" + m_strSummnyKey };
	execFormulas(formulas);
	// ���ø�KEY�Ѹı�
	setKeysExecuted(CIRCLE_DISCOUNTMNY, true);
	// ����ִ�б��
	setCircleExecuted(CIRCLE_DISCOUNTMNY, true);
	return;
  }

  /**
	 * ���ߣ���ӡ�� ���ܣ�����������������������CIRCLE��,����һ������ı������������� ������String sChangedKey
	 * Ӱ���CIRCLE���� ���أ��� ���⣺�� ���ڣ�(2002-5-15 11:39:21) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 */
  private void execCircle_ConvertRate(String sChangedKey) {

    // ��ִ�й��򲻿�ִ��,����ִ��
    if (isCircleExecuted(CIRCLE_CONVERT_RATE)
        || !isCircleExecutable(CIRCLE_CONVERT_RATE, sChangedKey))
      return;

    boolean isNullAsZero = m_fp.isNullAsZero();
    //�޸��ˣ������� �޸�ʱ�䣺2008-12-22 ����11:25:19 �޸�ԭ���������������ʵĻ����ϵ�޸ĳ�����һ�¡�0��0�����ǿա�
    m_fp.setNullAsZero(false);
    /*
     * ��������������*������
     * ����ǹ̶������ʣ������ʲ��ܸı䣬���������ı��Ӱ�츨�������������ı�Ҳ��Ӱ�������� 
     * ����Ƿǹ̶����㣬�ı丨���������ʣ���������֮�仯���ı�����������ʾ�Ļ�������֮�仯��
     */
    // ////////////////�������ı�
    if (sChangedKey.equals(m_strNumKey)) {
      if (isFixedConvertRate()) {// �̶�������
 
    	//������\����λ
        if (!m_strConvertRateKey.equals("")
              && !isKeyExecuted(m_strAssistNumKey)) {
            // ִ�й�ʽ
            String formulas[] = new String[] {
               m_strAssistNumKey + "->iif(null == "+ m_strNumKey + " || null == "+m_strConvertRateKey+", null, " + m_strNumKey + "/" + m_strConvertRateKey+ ")"
            };
            execFormulas(formulas);        
        // ���ø�KEY�Ѹı�
        setKeysExecuted(CIRCLE_CONVERT_RATE, true);
        // ����ִ�б��
        setCircleExecuted(CIRCLE_CONVERT_RATE, true);
        
        // ִ����һ��CIRCLE
        execCircle_Qt_ConvertRate(m_strNumKey);//���۵�λ������ϵȦ
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
      else {// �ǹ̶�������
    	  
        //������\����λ---�����ʸı�
        if (getUFDouble(m_strAssistNumKey) != null
            && !m_strConvertRateKey.equals("")
            && !isKeyExecuted(m_strConvertRateKey)) {
          // ִ�й�ʽ
          String formulas[] = new String[] {
            m_strConvertRateKey + "->" + m_strNumKey + "/" + m_strAssistNumKey
          };
          execFormulas(formulas);
          
        // ���ø�KEY�Ѹı�
        setKeysExecuted(CIRCLE_CONVERT_RATE, true);
        // ����ִ�б��
        setCircleExecuted(CIRCLE_CONVERT_RATE, true);
        
        // ִ����һ��CIRCLE
        execCircle_Qt_ConvertRate(m_strNumKey);//���۵�λ������ϵȦ
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
        // �������ı�
        if (getUFDouble(m_strConvertRateKey) != null
            && !m_strAssistNumKey.equals("")
            && !isKeyExecuted(m_strAssistNumKey)) {
          // ִ�й�ʽ
          String formulas[] = new String[] {
            m_strAssistNumKey + "->iif(null == "+ m_strNumKey + " || null == "+m_strConvertRateKey+", null, " + m_strNumKey + "/" + m_strConvertRateKey + ")"
          };
          execFormulas(formulas);
          // ���ø�KEY�Ѹı�
          setKeysExecuted(CIRCLE_CONVERT_RATE, true);
          // ����ִ�б��
          setCircleExecuted(CIRCLE_CONVERT_RATE, true);
          // ִ����һ��CIRCLE
          execCircle_Qt_ConvertRate(m_strNumKey);//���۵�λ������ϵȦ
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

    // //////////////�������ı�
    if (sChangedKey.equals(m_strAssistNumKey)) {
      if (isFixedConvertRate()) {// �̶�������
    	//1.������\����λ
        if (getUFDouble(m_strConvertRateKey) != null && !m_strNumKey.equals("")
            && !isKeyExecuted(m_strNumKey)) {
          // ִ�й�ʽ
          String formulas[] = new String[] {
            m_strNumKey + "->iif(null == "+ m_strAssistNumKey + " || null == "+m_strConvertRateKey+", null, "+ m_strAssistNumKey + "*" + m_strConvertRateKey+")"
          };
          execFormulas(formulas);
                
        // ���ø�KEY�Ѹı�
        setKeysExecuted(CIRCLE_CONVERT_RATE, true);
        // ����ִ�б��
        setCircleExecuted(CIRCLE_CONVERT_RATE, true);
        
        // ִ����һ��CIRCLE
        execCircle_Qt_ConvertRate(m_strNumKey);//���۵�λ������ϵȦ
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
      //�䶯������
      else {
        //1.������\����λ---�����ʲ���
        if (getUFDouble(m_strConvertRateKey) != null && !m_strNumKey.equals("")
            && !isKeyExecuted(m_strNumKey)) {
          // ִ�й�ʽ
          String formulas[] = new String[] {
            m_strNumKey + "->iif(null == "+ m_strAssistNumKey + " || null == "+m_strConvertRateKey+", null, "+ m_strAssistNumKey + "*" + m_strConvertRateKey + ")"
          };
          execFormulas(formulas); 
        
        // ���ø�KEY�Ѹı�
        setKeysExecuted(CIRCLE_CONVERT_RATE, true);
        // ����ִ�б��
        setCircleExecuted(CIRCLE_CONVERT_RATE, true);
        // ִ����һ��CIRCLE
        execCircle_Qt_ConvertRate(m_strNumKey);//���۵�λ������ϵȦ
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
        /*// ����������
        if (getUFDouble(m_strNumKey) != null && !m_strConvertRateKey.equals("")
            && !isKeyExecuted(m_strConvertRateKey)) {
          // ִ�й�ʽ
          String formulas[] = new String[] {
            m_strConvertRateKey + "->" + m_strNumKey + "/" + m_strAssistNumKey
          };
          execFormulas(formulas);
          // ���ø�KEY�Ѹı�
          setKeysExecuted(CIRCLE_CONVERT_RATE, true);
          // ����ִ�б��
          setCircleExecuted(CIRCLE_CONVERT_RATE, true);
          return;
        }*/
      }
    }

    // //////////////�����ʸı�
    if (sChangedKey.equals(m_strConvertRateKey)) {

      // V5�޸ģ��������޸ĸ���λʱ�����������䣬���㸨����

      // //���Ƿ�̶��������޹�
      // //����������������
      // //����������
      // if( getUFDouble(m_strAssistNum)!=null && !m_strNum.equals("") &&
      // !isKeyExecuted(m_strNum) ){
      // //ִ�й�ʽ
      // String formulas[] = new String[]{m_strNum + "->" + m_strAssistNum + "*"
      // + m_strConvertRate } ;
      // execFormula(formulas) ;
      // //���Դ�ӡ
      // debugPrint(formulas) ;
      // //���ø�KEY�Ѹı�
      // setKeysExecuted(CIRCLE_CONVERT_RATE,true) ;
      // //����ִ�б��
      // setCircleExecuted(CIRCLE_CONVERT_RATE,true) ;
      // //ִ����һ��CIRCLE
      // if(getPrior_Price_TaxPrice()==RelationsCalVO.PRICE_PRIOR_TO_TAXPRICE){
      // execCircle_NumNetPriceMoney(m_strNum) ;
      // execCircle_NumNetTaxPriceSummny(m_strNum) ;
      // }else{
      // execCircle_NumNetTaxPriceSummny(m_strNum) ;
      // execCircle_NumNetPriceMoney(m_strNum) ;
      // }
      // return ;
      // }
      //������\����λ---����������
      if (getUFDouble(m_strNumKey) != null && !m_strAssistNumKey.equals("")
          && !isKeyExecuted(m_strAssistNumKey)) {
        // ִ�й�ʽ
        String formulas[] = new String[] {
          m_strAssistNumKey + "->" + m_strNumKey + "/" + m_strConvertRateKey
        };
        execFormulas(formulas);
      
      // ���ø�KEY�Ѹı�
      setKeysExecuted(CIRCLE_CONVERT_RATE, true);
      // ����ִ�б��
      setCircleExecuted(CIRCLE_CONVERT_RATE, true);
      m_fp.setNullAsZero(isNullAsZero);
      return;
     }
    }
  }
  
  /**
   * 18.���������������������ۻ�����
   */
   private void execCircle_Qt_ConvertRate(String sChangedKey){
	    // ��ִ�й��򲻿�ִ��,����ִ��
	    if (isCircleExecuted(CIRCLE_QT_CONVERT_RATE)
	        || !isCircleExecutable(CIRCLE_QT_CONVERT_RATE, sChangedKey))
	      return;
	    
        //////////////�������ı�
		if (sChangedKey.equals(m_strNumKey)) {
			// �̶�������
			if (isQt_FixedConvertRate()) {
				if (getUFDouble(m_strQt_ConvertRateKey) != null
						&& !isKeyExecuted(m_strQt_NumKey)) {
					// ִ�й�ʽ
					String formulas[] = new String[] { m_strQt_NumKey + "->"
							+ m_strNumKey + "/" + m_strQt_ConvertRateKey };
					execFormulas(formulas);
					// ���ø�KEY�Ѹı�
					setKeysExecuted(CIRCLE_QT_CONVERT_RATE, true);
					// ����ִ�б��
					setCircleExecuted(CIRCLE_QT_CONVERT_RATE, true);
				}
			}
			// �䶯������
			else{
				if (getUFDouble(m_strQt_NumKey) != null
						&& !isKeyExecuted(m_strQt_ConvertRateKey)) {
					// ִ�й�ʽ
					String formulas[] = new String[] { m_strQt_ConvertRateKey + "->"
							+ m_strNumKey + "/" + m_strQt_NumKey };
					execFormulas(formulas);
					// ���ø�KEY�Ѹı�
					setKeysExecuted(CIRCLE_QT_CONVERT_RATE, true);
					// ����ִ�б��
					setCircleExecuted(CIRCLE_QT_CONVERT_RATE, true);
				}
			}
		}
		
		//////////////���ۻ����ʸı�
	    if (sChangedKey.equals(m_strQt_ConvertRateKey)) {
	    	if (getUFDouble(m_strQt_ConvertRateKey) != null 
	    	          && !isKeyExecuted(m_strQt_NumKey)) {
	    		// ִ�й�ʽ
    	        String formulas[] = new String[] {
    	        	m_strQt_NumKey + "->" + m_strNumKey + "/" + m_strQt_ConvertRateKey
    	        };
    	        execFormulas(formulas);
    	      
    	        // ���ø�KEY�Ѹı�
    	        setKeysExecuted(CIRCLE_QT_CONVERT_RATE, true);
    	        // ����ִ�б��
    	        setCircleExecuted(CIRCLE_QT_CONVERT_RATE, true);
    	        //ִ����һ��CIRCLE
    	        if (getPrior_Price_TaxPrice() == RelationsCalVO.PRICE_PRIOR_TO_TAXPRICE) {
    	            execCircle_Qt_NumNetPriceMoney(m_strQt_NumKey);
    	        }
    	        else {
    	            execCircle_Qt_NumNetTaxPriceSummny(m_strQt_NumKey);
    	        }
    	        return;
	    	}
	    }
	   
	    //////////////���������ı�
		if (sChangedKey.equals(m_strQt_NumKey)) {
			// �̶�������
			if (isQt_FixedConvertRate()) {
				// ������\����λ
				if (getUFDouble(m_strQt_ConvertRateKey) != null
						&& !isKeyExecuted(m_strNumKey)) {
					// ִ�й�ʽ
					String formulas[] = new String[] { m_strNumKey + "->"
							+ m_strQt_NumKey + "*" + m_strQt_ConvertRateKey };
					execFormulas(formulas);

					// ���ø�KEY�Ѹı�
					setKeysExecuted(CIRCLE_QT_CONVERT_RATE, true);
					// ����ִ�б��
					setCircleExecuted(CIRCLE_QT_CONVERT_RATE, true);
					// ִ����һ��CIRCLE
					execCircle_ConvertRate(m_strNumKey);//����������������������
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
			// �ǹ̶�������
			else {
				// ������\����λ---���ۻ����ʸı�
				if (getUFDouble(m_strQt_ConvertRateKey) != null
						&& !isKeyExecuted(m_strNumKey)) {
					// ִ�й�ʽ
					String formulas[] = new String[] { m_strNumKey + "->"
							+ m_strQt_NumKey + "*" + m_strQt_ConvertRateKey };
					execFormulas(formulas);

					// ���ø�KEY�Ѹı�
					setKeysExecuted(CIRCLE_QT_CONVERT_RATE, true);
					// ����ִ�б��
					setCircleExecuted(CIRCLE_QT_CONVERT_RATE, true);
					// ִ����һ��CIRCLE
					execCircle_ConvertRate(m_strNumKey);//����������������������
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
	 * ��14����Ʒ�ۿۡ�����ԭ�Һ�˰���ۡ�����ԭ�Һ�˰���� 2008-03-20 zhangcheng v5.3
	 */
  private void execCircle_Qt_TaxPriceNetTaxPrice(String sChangedKey,
	      boolean bExecFollowing) {

	    // ��ִ�й��򲻿�ִ��,����ִ��
	    if (isCircleExecuted(CIRCLE_QT_TAXPRICE_NETTAXPRICE)
	        || !isCircleExecutable(CIRCLE_QT_TAXPRICE_NETTAXPRICE, sChangedKey)) {
	      return;
	    }
	    // /////////////////��˰����
	    if (sChangedKey.equals(m_strQt_TaxPriceKey)){
	    	// ��˰���� = ��˰����* ����
	        if (getUFDouble(m_strDiscountRateKey) != null
	            && !m_strQt_NetTaxPriceKey.equals("")
	            && !isKeyExecuted(m_strQt_NetTaxPriceKey)) {
	          // ִ�й�ʽ
	          String formulas[] = getTaxPrice_NetTaxPrice_Discount( m_strQt_NetTaxPriceKey );
	          execFormulas(formulas);
	          // ���ø�KEY�Ѹı�
	          setKeysExecuted(CIRCLE_QT_TAXPRICE_NETTAXPRICE, true);
	          // ����ִ�б��
	          setCircleExecuted(CIRCLE_QT_TAXPRICE_NETTAXPRICE, true);
	          // ִ����һ��CIRCLE
	          if (bExecFollowing) {
	        	execCircle_Qt_NetTaxPriceNetPrice(m_strQt_NetTaxPriceKey, false);
	            execCircle_Qt_NumNetTaxPriceSummny(m_strQt_NetTaxPriceKey);
	            execCircle_Qt_TaxPricePrice(m_strQt_TaxPriceKey, false);
	          }
	          return;
	        }
	    }
	    ///////////////////��˰����
	    else if (sChangedKey.equals(m_strQt_NetTaxPriceKey)){
	    	//���ۿ�(Ĭ��):����һ�θı���Ǳ��ۼ�����λ�ļ۸��Ҳ����ǵ��ۿ�ʱ
	    	if ((getFirstChangedKey().equals(m_strQt_NetTaxPriceKey)
					|| getFirstChangedKey().equals(m_strQt_NetPriceKey)
					|| getFirstChangedKey().equals(m_strQt_TaxPriceKey) || getFirstChangedKey()
					.equals(m_strQt_PriceKey))
					&& m_iPrior_ItemDiscountRate_Price == RelationsCalVO.ITEMDISCOUNTRATE_PRIOR_TO_PRICE) {
	    		if (!isKeyExecuted(m_strDiscountRateKey)){
		    		// ִ�й�ʽ����Ʒ�ۿ� = ��˰���� / ����˰���� * �����ۿۣ�
		            String formulas[] = getTaxPrice_NetTaxPrice_Discount(m_strDiscountRateKey);
		            execFormulas(formulas);
		            // ���ø�KEY�Ѹı�
		            setKeysExecuted(CIRCLE_QT_TAXPRICE_NETTAXPRICE, true);
		            // ����ִ�б��
		            setCircleExecuted(CIRCLE_QT_TAXPRICE_NETTAXPRICE, true);
		            // ִ����һ��CIRCLE
			        if (bExecFollowing) {
			        	execCircle_Qt_NetTaxPriceNetPrice(m_strQt_NetTaxPriceKey, false);
			            execCircle_Qt_NumNetTaxPriceSummny(m_strQt_NetTaxPriceKey);
			        }
		            return;
		    	}
	    	}
	    	//�����ۣ�����һ�θı������������λ�ļ۸���߲����ǵ�����ʱ
	    	else{
	    		if (!isKeyExecuted(m_strQt_TaxPriceKey)){
		    		// ִ�й�ʽ����˰���� = ��˰���� / ����Ʒ�ۿ� * �����ۿۣ�
		            String formulas[] = getTaxPrice_NetTaxPrice_Discount(m_strQt_TaxPriceKey);
		            execFormulas(formulas);
		            // ���ø�KEY�Ѹı�
		            setKeysExecuted(CIRCLE_QT_TAXPRICE_NETTAXPRICE, true);
		            // ����ִ�б��
		            setCircleExecuted(CIRCLE_QT_TAXPRICE_NETTAXPRICE, true);
		            // ִ����һ��CIRCLE
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
   * ��16����������������ԭ�Һ�˰���ۡ���˰�ϼ�
   *  2008-03-20  zhangcheng v5.3 
   */
  private void execCircle_Qt_NumNetTaxPriceSummny(String sChangedKey) {

	    // ��ִ�й��򲻿�ִ��,����ִ��
	    if (isCircleExecuted(CIRCLE_QT_NUM_NETTAXPRICE_SUMMNY)
	        || !isCircleExecutable(CIRCLE_QT_NUM_NETTAXPRICE_SUMMNY, sChangedKey)) {
	      return;
	    }
        ////////////////��˰�ϼƸı�
	    if (sChangedKey.equals(m_strSummnyKey)){
	    	if (!isKeyExecuted(m_strQt_NetTaxPriceKey)){
	    		// ִ�й�ʽ
	            String formulas[] = new String[] {
	            	m_strQt_NetTaxPriceKey + "->" + m_strSummnyKey + "/" + m_strQt_NumKey
	            };
	            execFormulas(formulas);
	            // ���ø�KEY�Ѹı�
	            setKeysExecuted(CIRCLE_QT_NUM_NETTAXPRICE_SUMMNY, true);
	            // ����ִ�б��
	            setCircleExecuted(CIRCLE_QT_NUM_NETTAXPRICE_SUMMNY, true);
	            //���㱨��ԭ�Һ�˰����
	            execCircle_Qt_TaxPriceNetTaxPrice(m_strQt_NetTaxPriceKey, false);
	            //���㱨��ԭ����˰����
	            execCircle_Qt_NetTaxPriceNetPrice(m_strQt_NetTaxPriceKey, false);
	            //���㱨��ԭ����˰����
	            execCircle_Qt_TaxPricePrice(m_strQt_TaxPriceKey, false);
	            return;
	    	}
	    }
        ////////////////���������ı�
	    if (sChangedKey.equals(m_strQt_NumKey)){
	    	// ��˰�ϼ� = ���ۺ�˰���� * ��������
	    	if (getUFDouble(m_strQt_NetTaxPriceKey) != null &&!isKeyExecuted(m_strSummnyKey)){
	    		// ִ�й�ʽ
	            String formulas[] = new String[] {
	            		m_strSummnyKey + "->" + m_strQt_NetTaxPriceKey + "*" + m_strQt_NumKey
	            };
	            execFormulas(formulas);
	            // ���ø�KEY�Ѹı�
	            setKeysExecuted(CIRCLE_QT_NUM_NETTAXPRICE_SUMMNY, true);
	            // ����ִ�б��
	            setCircleExecuted(CIRCLE_QT_NUM_NETTAXPRICE_SUMMNY, true);
	            execCircle_Qt_ConvertRate(m_strQt_NumKey);
	            execCircle_Taxrate(m_strSummnyKey);
	            return;
	    	}
	    	// ���ۺ�˰���� = ��˰�ϼ� / ��������
	    	if (getUFDouble(m_strSummnyKey) != null &&!isKeyExecuted(m_strQt_NetTaxPriceKey)){
	    		// ִ�й�ʽ
	            String formulas[] = new String[] {
	            	m_strQt_NetTaxPriceKey + "->" + m_strSummnyKey + "/" + m_strQt_NumKey
	            };
	            execFormulas(formulas);
	            // ���ø�KEY�Ѹı�
	            setKeysExecuted(CIRCLE_QT_NUM_NETTAXPRICE_SUMMNY, true);
	            // ����ִ�б��
	            setCircleExecuted(CIRCLE_QT_NUM_NETTAXPRICE_SUMMNY, true);
	            //���۵�λ������ϵȦ
	            execCircle_Qt_ConvertRate(m_strQt_NumKey);
	            //���㱨��ԭ�Һ�˰����
	            execCircle_Qt_TaxPriceNetTaxPrice(m_strQt_NetTaxPriceKey, false);
	            //���㱨��ԭ����˰����
	            execCircle_Qt_NetTaxPriceNetPrice(m_strQt_NetTaxPriceKey, false);
	            //���㱨��ԭ����˰����
	            execCircle_Qt_TaxPricePrice(m_strQt_TaxPriceKey, false);
	            return;
	    	}
	    }
	    ////////////////��˰���۸ı�
	    else if (sChangedKey.equals(m_strQt_NetTaxPriceKey)) {
	      // �����˰�ϼ�
	      if (getUFDouble(m_strQt_NumKey) != null && !m_strSummnyKey.equals("")
	          && !isKeyExecuted(m_strSummnyKey)) {
	        // ִ�й�ʽ
	        String formulas[] = new String[] {
	          m_strSummnyKey + "->" + m_strQt_NumKey + "*" + m_strQt_NetTaxPriceKey
	        };
	        execFormulas(formulas);
	        // ���ø�KEY�Ѹı�
	        setKeysExecuted(CIRCLE_QT_NUM_NETTAXPRICE_SUMMNY, true);
	        // ����ִ�б��
	        setCircleExecuted(CIRCLE_QT_NUM_NETTAXPRICE_SUMMNY, true);
	        // ִ����һ��CIRCLE
	        execCircle_Taxrate(m_strSummnyKey);
	        execCircle_Qt_TaxPriceNetTaxPrice(m_strQt_NetTaxPriceKey, false);
	        return;
	      }
	    }
  }
  
  /**
   * ��17����������������ԭ����˰���ۡ���˰���
   *  2008-03-20  zhangcheng v5.3 
   */
  private void execCircle_Qt_NumNetPriceMoney(String sChangedKey) {

	    // ��ִ�й��򲻿�ִ��,����ִ��
	    if (isCircleExecuted(CIRCLE_QT_NUM_NETPRICE_MONEY)
	        || !isCircleExecutable(CIRCLE_QT_NUM_NETPRICE_MONEY, sChangedKey)) {
	      return;
	    }
	    ////////////��˰���ı�
	    if (sChangedKey.equals(m_strMoneyKey)){
	    	if (!isKeyExecuted(m_strQt_NetPriceKey)){
	    		// ִ�й�ʽ
	            String formulas[] = new String[] {
	            	m_strQt_NetPriceKey + "->" + m_strMoneyKey + "/" + m_strQt_NumKey
	            };
	            execFormulas(formulas);
	            // ���ø�KEY�Ѹı�
	            setKeysExecuted(CIRCLE_QT_NUM_NETPRICE_MONEY, true);
	            // ����ִ�б��
	            setCircleExecuted(CIRCLE_QT_NUM_NETPRICE_MONEY, true);
	            //���㱨��ԭ����˰����
	            execCircle_Qt_PriceNetPrice(m_strQt_NetPriceKey, false);
	            //���㱨��ԭ�Һ�˰����
	            execCircle_Qt_NetTaxPriceNetPrice(m_strQt_NetPriceKey, false);
	            //���㱨��ԭ�Һ�˰����
	            execCircle_Qt_TaxPricePrice(m_strQt_PriceKey, false);
	            return;
	    	}
	    }
        ////////////////���������ı�
	    if (sChangedKey.equals(m_strQt_NumKey)){
	    	// ��˰�ϼ� = ������˰���� * ��������
	    	if (getUFDouble(m_strQt_NetPriceKey) != null &&!isKeyExecuted(m_strMoneyKey)){
	    		// ִ�й�ʽ
	            String formulas[] = new String[] {
	            		m_strMoneyKey + "->" + m_strQt_NetPriceKey + "*" + m_strQt_NumKey
	            };
	            execFormulas(formulas);
	            // ���ø�KEY�Ѹı�
	            setKeysExecuted(CIRCLE_QT_NUM_NETPRICE_MONEY, true);
	            // ����ִ�б��
	            setCircleExecuted(CIRCLE_QT_NUM_NETPRICE_MONEY, true);
	            execCircle_Qt_ConvertRate(m_strQt_NumKey);
	            execCircle_Taxrate(m_strMoneyKey);
	            return;
	    	}
	    	// ���ۺ�˰���� = ��˰�ϼ� / ��������
	    	if (getUFDouble(m_strMoneyKey) != null &&!isKeyExecuted(m_strQt_NetPriceKey)){
	    		// ִ�й�ʽ
	            String formulas[] = new String[] {
	            	m_strQt_NetPriceKey + "->" + m_strMoneyKey + "/" + m_strQt_NumKey
	            };
	            execFormulas(formulas);
	            // ���ø�KEY�Ѹı�
	            setKeysExecuted(CIRCLE_QT_NUM_NETPRICE_MONEY, true);
	            // ����ִ�б��
	            setCircleExecuted(CIRCLE_QT_NUM_NETPRICE_MONEY, true);
	            //���㱨��ԭ����˰����
	            execCircle_Qt_PriceNetPrice(m_strQt_NetPriceKey, false);
	            //���㱨��ԭ�Һ�˰����
	            execCircle_Qt_NetTaxPriceNetPrice(m_strQt_NetPriceKey, false);
	            //���㱨��ԭ�Һ�˰����
	            execCircle_Qt_TaxPricePrice(m_strQt_PriceKey, false);
	            return;
	    	}
	    }
	    ////////////��˰���۸ı�
	    else if (sChangedKey.equals(m_strQt_NetPriceKey)){
	    	// �����˰�ϼ�
		    if (getUFDouble(m_strQt_NumKey) != null && !m_strMoneyKey.equals("")
		          && !isKeyExecuted(m_strMoneyKey)) {
		        // ִ�й�ʽ
		        String formulas[] = new String[] {
		        		m_strMoneyKey + "->" + m_strQt_NumKey + "*" + m_strQt_NetPriceKey
		        };
		        execFormulas(formulas);
		        // ���ø�KEY�Ѹı�
		        setKeysExecuted(CIRCLE_QT_NUM_NETPRICE_MONEY, true);
		        // ����ִ�б��
		        setCircleExecuted(CIRCLE_QT_NUM_NETPRICE_MONEY, true);
		        // ִ����һ��CIRCLE
		        execCircle_Taxrate(m_strMoneyKey);
		        execCircle_Qt_TaxPriceNetTaxPrice(m_strQt_NetPriceKey, false);
		        return;
		    }
	    }
  }
  
  /**
   * ��15����Ʒ�ۿۡ�����ԭ����˰���ۡ�����ԭ����˰����
   *  2008-03-20  zhangcheng v5.3 
   */
  private void execCircle_Qt_PriceNetPrice(String sChangedKey,
	      boolean bExecFollowing) {

	    // ��ִ�й��򲻿�ִ��,����ִ��
	    if (isCircleExecuted(CIRCLE_QT_PRICE_NETPRICE)
	        || !isCircleExecutable(CIRCLE_QT_PRICE_NETPRICE, sChangedKey)) {
	      return;
	    }
	    
	    ///////////////��˰���۸ı�
	    if (sChangedKey.equals(m_strQt_NetPriceKey)){
	    	//���ۿ�(Ĭ��):����һ�θı���Ǳ��ۼ�����λ�ļ۸��Ҳ����ǵ��ۿ�ʱ
	    	if ((getFirstChangedKey().equals(m_strQt_NetTaxPriceKey)
					|| getFirstChangedKey().equals(m_strQt_NetPriceKey)
					|| getFirstChangedKey().equals(m_strQt_TaxPriceKey) || getFirstChangedKey()
					.equals(m_strQt_PriceKey))
					&& m_iPrior_ItemDiscountRate_Price == RelationsCalVO.ITEMDISCOUNTRATE_PRIOR_TO_PRICE) {
	    		if (!isKeyExecuted(m_strQt_PriceKey)){
		    		// ִ�й�ʽ
		            String formulas[] = getPrice_NetPrice_Discount(m_strDiscountRateKey);
		            execFormulas(formulas);
		            // ���ø�KEY�Ѹı�
		            setKeysExecuted(CIRCLE_QT_PRICE_NETPRICE, true);
		            // ����ִ�б��
		            setCircleExecuted(CIRCLE_QT_PRICE_NETPRICE, true);
		            // ִ����һ��CIRCLE
			        if (bExecFollowing) {
			        	execCircle_Qt_NetTaxPriceNetPrice(m_strQt_NetPriceKey, false);//��˰���� = ��˰���� * (1+˰��)
			        	execCircle_Qt_NumNetPriceMoney(m_strQt_NetPriceKey);
			        }
		            return;
		    	}	
	    	}
	    	//������
	    	else {
	    		if (!isKeyExecuted(m_strQt_PriceKey)){
		    		// ִ�й�ʽ
		            String formulas[] = getPrice_NetPrice_Discount(m_strQt_PriceKey);
		            execFormulas(formulas);
		            // ���ø�KEY�Ѹı�
		            setKeysExecuted(CIRCLE_QT_PRICE_NETPRICE, true);
		            // ����ִ�б��
		            setCircleExecuted(CIRCLE_QT_PRICE_NETPRICE, true);
		            // ִ����һ��CIRCLE
			        if (bExecFollowing) {
			        	execCircle_Qt_NetTaxPriceNetPrice(m_strQt_NetPriceKey, false);//��˰���� = ��˰���� * (1+˰��)
			        	execCircle_Qt_NumNetPriceMoney(m_strQt_NetPriceKey);
			            execCircle_Qt_TaxPricePrice(m_strQt_PriceKey, false);//��˰���� = ��˰���� * (1+˰��)
			        }
		            return;
		    	}
	    	}	    	
	    }
        ///////////////��˰���۸ı�
	    else if(sChangedKey.equals(m_strQt_PriceKey)){
	    	// ��˰���� = ��˰����* ����
	        if (getUFDouble(m_strDiscountRateKey) != null
	            && !m_strQt_NetPriceKey.equals("")
	            && !isKeyExecuted(m_strQt_NetPriceKey)) {
	          // ִ�й�ʽ
	          String formulas[] = getPrice_NetPrice_Discount( m_strQt_NetPriceKey );
	          execFormulas(formulas);
	          // ���ø�KEY�Ѹı�
	          setKeysExecuted(CIRCLE_QT_PRICE_NETPRICE, true);
	          // ����ִ�б��
	          setCircleExecuted(CIRCLE_QT_PRICE_NETPRICE, true);
	          // ִ����һ��CIRCLE
	          if (bExecFollowing) {
	        	execCircle_Qt_NetTaxPriceNetPrice(m_strQt_NetPriceKey, false);//��˰���� = ��˰���� * (1+˰��)
	        	execCircle_Qt_NumNetPriceMoney(m_strQt_NetPriceKey);
	            execCircle_Qt_TaxPricePrice(m_strQt_PriceKey, false);//��˰���� = ��˰���� * (1+˰��)
	          }
	          return;
	        }
	    }
  }
  
  /**
   * ��13��˰�ʡ�����ԭ�Һ�˰���ۡ�����ԭ����˰����
   * 2008-03-20  zhangcheng v5.3 
   */
  private void execCircle_Qt_NetTaxPriceNetPrice(String sChangedKey,
      boolean bExecFollowing) {

    // ��ִ�й��򲻿�ִ��,����ִ��
    if (isCircleExecuted(CIRCLE_QT_NETTAXPRICE_NETPRICE)
        || !isCircleExecutable(CIRCLE_QT_NETTAXPRICE_NETPRICE, sChangedKey))
      return;

    // ////////////////��˰���ۡ�˰�ʸı�
    if (sChangedKey.equals(m_strTaxRateKey) || sChangedKey.equals(m_strQt_NetTaxPriceKey)
    		|| sChangedKey.equals(m_strQt_NetPriceKey)) {
    	//��˰����
    	if (getPrior_Price_TaxPrice() == RelationsCalVO.TAXPRICE_PRIOR_TO_PRICE)
    	{
    		// ��˰���� = ��˰����/(1+˰��)
    	    if (!isKeyExecuted(m_strQt_NetPriceKey)) {
    	        // ִ�й�ʽ
    	        String formulas[] = new String[] {
    	          m_strQt_NetPriceKey+"->"+m_strQt_NetTaxPriceKey+"/(1.0+"+m_strTaxRateKey+"/100.0)"
    	        };
    	        execFormulas(formulas);
    	        // ���ø�KEY�Ѹı�
    	        setKeysExecuted(CIRCLE_QT_NETTAXPRICE_NETPRICE, true);
    	        // ����ִ�б��
    	        setCircleExecuted(CIRCLE_QT_NETTAXPRICE_NETPRICE, true);
    	        return;
    	    }
    	}
    	//��˰����
    	else{
    		// ��˰���� = ��˰����*(1+˰��)
    	    if (!isKeyExecuted(m_strQt_NetTaxPriceKey)) {
    	        // ִ�й�ʽ
    	        String formulas[] = new String[] {
    	          m_strQt_NetTaxPriceKey+"->"+m_strQt_NetPriceKey+"*(1.0+"+m_strTaxRateKey+"/100.0)"
    	        };
    	        execFormulas(formulas);
    	        // ���ø�KEY�Ѹı�
    	        setKeysExecuted(CIRCLE_QT_NETTAXPRICE_NETPRICE, true);
    	        // ����ִ�б��
    	        setCircleExecuted(CIRCLE_QT_NETTAXPRICE_NETPRICE, true);
    	        return;
    	    }
    	}
    }
  }

  /**
   * ��12��˰�ʡ�����ԭ�Һ�˰���ۡ�����ԭ����˰����
   * 2008-03-20  zhangcheng v5.3 
   */
  private void execCircle_Qt_TaxPricePrice(String sChangedKey,
      boolean bExecFollowing) {

    // ��ִ�й��򲻿�ִ��,����ִ��
    if (isCircleExecuted(CIRCLE_QT_TAXPRICE_PRICE)
        || !isCircleExecutable(CIRCLE_QT_TAXPRICE_PRICE, sChangedKey))
      return;

    // ////////////////˰�ʸġ���˰\��˰���۱�
    if (sChangedKey.equals(m_strTaxRateKey)|| sChangedKey.equals(m_strQt_TaxPriceKey)
    		|| sChangedKey.equals(m_strQt_PriceKey)) {
    	//��˰����
    	if (getPrior_Price_TaxPrice() == RelationsCalVO.TAXPRICE_PRIOR_TO_PRICE)
    	{
    		// ��˰���� = ��˰����/(1+˰��)
    	    if (!isKeyExecuted(m_strQt_PriceKey)) {
    	        // ִ�й�ʽ
    	        String formulas[] = new String[] {
    	          m_strQt_PriceKey+"->"+m_strQt_TaxPriceKey+"/(1.0+"+m_strTaxRateKey+"/100.0)"
    	        };
    	        execFormulas(formulas);
    	        // ���ø�KEY�Ѹı�
    	        setKeysExecuted(CIRCLE_QT_TAXPRICE_PRICE, true);
    	        // ����ִ�б��
    	        setCircleExecuted(CIRCLE_QT_TAXPRICE_PRICE, true);
    	        return;
    	    }
    	}
    	//��˰����
    	else{
    		// ��˰���� = ��˰����*(1+˰��)
    	    if (!isKeyExecuted(m_strQt_TaxPriceKey)) {
    	        // ִ�й�ʽ
    	        String formulas[] = new String[] {
    	          m_strQt_TaxPriceKey+"->"+m_strQt_PriceKey+"*(1.0+"+m_strTaxRateKey+"/100.0)"
    	        };
    	        execFormulas(formulas);
    	        // ���ø�KEY�Ѹı�
    	        setKeysExecuted(CIRCLE_QT_TAXPRICE_PRICE, true);
    	        // ����ִ�б��
    	        setCircleExecuted(CIRCLE_QT_TAXPRICE_PRICE, true);
    	        return;
    	    }
    	}
    }
  }  
  
  /**
   * ��9����˰���ۣ���˰���ۣ�˰��
   * ���ߣ���ӡ�� ���ܣ�˰�ʡ�����˰���ۡ�������CIRCLE��,����һ������ı������������� ������String sChangedKey
   * Ӱ���CIRCLE���� ���أ��� ���⣺�� ���ڣ�(2003-10-10 11:39:21) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
   * 2008-03-07  zhangcheng v5.3 ������
   */
  private void execCircle_NetTaxPriceNetPrice(String sChangedKey,
      boolean bExecFollowing) {

    // ��ִ�й��򲻿�ִ��,����ִ��
    if (isCircleExecuted(CIRCLE_NETTAXPRICE_NETPRICE)
        || !isCircleExecutable(CIRCLE_NETTAXPRICE_NETPRICE, sChangedKey))
      return;

    // ////////////////�����۸ı�
    if (sChangedKey.equals(m_strNetPriceKey)) {
      // ����˰���� = FUNC(�����ۡ�˰��)
      if (!m_strTaxRateKey.equals("") && !isKeyExecuted(m_strNetTaxPriceKey)) {
        // ִ�й�ʽ
        String formulas[] = new String[] {
          getNetTaxPrice_TaxrateNetPrice()
        };
        execFormulas(formulas);
        // ���ø�KEY�Ѹı�
        setKeysExecuted(CIRCLE_NETTAXPRICE_NETPRICE, true);
        // ����ִ�б��
        setCircleExecuted(CIRCLE_NETTAXPRICE_NETPRICE, true);
        // ִ����һ��CIRCLE
        if (bExecFollowing) {
          execCircle_TaxPriceNetTaxPrice(m_strNetTaxPriceKey, false);
          execCircle_NumNetPriceMoney(m_strNetPriceKey);
          execCircle_PriceNetPrice(m_strNetPriceKey, false);
        }
        return;
      }
    }

    // ////////////��˰���� ������˰���۸ı䣩
    if (sChangedKey.equals(m_strNetTaxPriceKey)) {
      // ������ = FUNC(����˰���ۡ�˰��)
      if (!m_strNetTaxPriceKey.equals("") && !isKeyExecuted(m_strNetPriceKey)) {
        // ִ�й�ʽ
        String formulas[] = new String[] {
          getNetPrice_TaxrateNetTaxPrice()//���ݿ�˰���˰�ʡ�����˰���� �õ� �����۵ļ��㹫ʽ
        };
        execFormulas(formulas);
        // ���ø�KEY�Ѹı�
        setKeysExecuted(CIRCLE_NETTAXPRICE_NETPRICE, true);
        // ����ִ�б��
        setCircleExecuted(CIRCLE_NETTAXPRICE_NETPRICE, true);
        // ִ����һ��CIRCLE
        if (bExecFollowing) {
          execCircle_PriceNetPrice(m_strNetPriceKey, false);
          execCircle_NumNetTaxPriceSummny(m_strNetTaxPriceKey);
          execCircle_TaxPriceNetTaxPrice(m_strNetTaxPriceKey, false);
        }
        return;
      }
    }

    // ////////////˰�ʸı�
    if (sChangedKey.equals(m_strTaxRateKey)) {
      // ��˰����
      if (getPrior_Price_TaxPrice() == RelationsCalVO.PRICE_PRIOR_TO_TAXPRICE) {
        // ����˰���� = FUNC(�����ۡ�˰��)
        if (!m_strNetTaxPriceKey.equals("") && !m_strNetPriceKey.equals("")
            && getUFDouble(m_strNetPriceKey) != null
            && !isKeyExecuted(m_strNetTaxPriceKey)) {
          // ִ�й�ʽ
          String formulas[] = new String[] {
            getNetTaxPrice_TaxrateNetPrice()
          };
          execFormulas(formulas);
          // ���ø�KEY�Ѹı�
          setKeysExecuted(CIRCLE_NETTAXPRICE_NETPRICE, true);
          // ����ִ�б��
          setCircleExecuted(CIRCLE_NETTAXPRICE_NETPRICE, true);
          // ִ����һ��CIRCLE
          // if (bExecFollowing) {
          // }
          return;
        }
        else {
          // ������ = FUNC(����˰���ۡ�˰��)
          if (!m_strNetTaxPriceKey.equals("") && !m_strNetPriceKey.equals("")
              && !isKeyExecuted(m_strNetPriceKey)) {
            // ִ�й�ʽ
            String formulas[] = new String[] {
              getNetPrice_TaxrateNetTaxPrice()
            };
            execFormulas(formulas);
            // ���ø�KEY�Ѹı�
            setKeysExecuted(CIRCLE_NETTAXPRICE_NETPRICE, true);
            // ����ִ�б��
            setCircleExecuted(CIRCLE_NETTAXPRICE_NETPRICE, true);
            // ִ����һ��CIRCLE
            // if (bExecFollowing) {
            // }
            return;
          }
        }
      }
      // ��˰����
      else {
        // ������ = FUNC(����˰���ۡ�˰��)
        if (!m_strNetTaxPriceKey.equals("") && !m_strNetPriceKey.equals("")
            && getUFDouble(m_strNetTaxPriceKey) != null
            && !isKeyExecuted(m_strNetPriceKey)) {
          // ִ�й�ʽ
          String formulas[] = new String[] {
            getNetPrice_TaxrateNetTaxPrice()
          };
          execFormulas(formulas);
          // ���ø�KEY�Ѹı�
          setKeysExecuted(CIRCLE_NETTAXPRICE_NETPRICE, true);
          // ����ִ�б��
          setCircleExecuted(CIRCLE_NETTAXPRICE_NETPRICE, true);
          // ִ����һ��CIRCLE
          // if (bExecFollowing) {
          // }
          return;
        }
        else {
          // ����˰���� = FUNC(�����ۡ�˰��)
          if (!m_strNetTaxPriceKey.equals("") && !m_strNetPriceKey.equals("")
              && !isKeyExecuted(m_strNetTaxPriceKey)) {
            // ִ�й�ʽ
            String formulas[] = new String[] {
              getNetPrice_TaxrateNetTaxPrice()
            };
            execFormulas(formulas);
            // ���ø�KEY�Ѹı�
            setKeysExecuted(CIRCLE_NETTAXPRICE_NETPRICE, true);
            // ����ִ�б��
            setCircleExecuted(CIRCLE_NETTAXPRICE_NETPRICE, true);
            // ִ����һ��CIRCLE
            // if (bExecFollowing) {
            // }
            return;
          }
        }
      }
    }
  }

  /**
   * ���ߣ���ӡ�� ���ܣ������������ۡ����CIRCLE��,����һ������ı������������� ������String sChangedKey
   * Ӱ���CIRCLE���� ���أ��� ���⣺�� ���ڣ�(2002-5-15 11:39:21) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
   * 2002-05-16 wyf �������ȿ���
   */
  private void execCircle_NumNetPriceMoney(String sChangedKey) {

    // ��ִ�й��򲻿�ִ��,����ִ��
    if (isCircleExecuted(CIRCLE_NUM_NETPRICE_MONEY)
        || !isCircleExecutable(CIRCLE_NUM_NETPRICE_MONEY, sChangedKey))
      return;

    // ////////////////�����ı�
    if (sChangedKey.equals(m_strNumKey)) {
      // �۸����ڽ��
      if (getUFDouble(m_strNetPriceKey) != null && !m_strMoneyKey.equals("")
          && !isKeyExecuted(m_strMoneyKey)) {
        // ִ�й�ʽ
        String formulas[] = new String[] {
          m_strMoneyKey + "->" + m_strNetPriceKey + "*" + m_strNumKey
        };
        execFormulas(formulas);
        // ���ø�KEY�Ѹı�
        setKeysExecuted(CIRCLE_NUM_NETPRICE_MONEY, true);
        // ����ִ�б��
        setCircleExecuted(CIRCLE_NUM_NETPRICE_MONEY, true);
        // ִ����һ��CIRCLE
        execCircle_Taxrate(m_strMoneyKey);
        execCircle_QualifiedNum(m_strNumKey);
        execCircle_ConvertRate(m_strNumKey);
        execCircle_Qt_ConvertRate(m_strNumKey);//���۵�λ������ϵȦ
        return;
      }

      if (getUFDouble(m_strMoneyKey) != null && !m_strNetPriceKey.equals("")
          && !isKeyExecuted(m_strNetPriceKey)) {
        // ִ�й�ʽ
        String formulas[] = new String[] {
          m_strNetPriceKey + "->" + m_strMoneyKey + "/" + m_strNumKey
        };
        execFormulas(formulas);
        // ���ø�KEY�Ѹı�
        setKeysExecuted(CIRCLE_NUM_NETPRICE_MONEY, true);
        // ����ִ�б��
        setCircleExecuted(CIRCLE_NUM_NETPRICE_MONEY, true);
        // ִ����һ��CIRCLE
        execCircle_PriceNetPrice(m_strNetPriceKey, false);
        execCircle_NetTaxPriceNetPrice(m_strNetPriceKey, false);
        execCircle_QualifiedNum(m_strNumKey);
        execCircle_ConvertRate(m_strNumKey);
        execCircle_NumNetTaxPriceSummny(m_strNumKey);
        return;
      }
    }

    // //////////////�����۸ı�
    if (sChangedKey.equals(m_strNetPriceKey)) {
      // �������ڽ��
      if (getUFDouble(m_strNumKey) != null && !m_strMoneyKey.equals("")
          && !isKeyExecuted(m_strMoneyKey)) {
        // ִ�й�ʽ
        String formulas[] = new String[] {
          m_strMoneyKey + "->" + m_strNumKey + "*" + m_strNetPriceKey
        };
        execFormulas(formulas);
        // ���ø�KEY�Ѹı�
        setKeysExecuted(CIRCLE_NUM_NETPRICE_MONEY, true);
        // ����ִ�б��
        setCircleExecuted(CIRCLE_NUM_NETPRICE_MONEY, true);
        // ִ����һ��CIRCLE
        execCircle_Taxrate(m_strMoneyKey);
        execCircle_PriceNetPrice(m_strNetPriceKey, false);
        execCircle_NetTaxPriceNetPrice(m_strNetPriceKey, false);
        return;
      }

      if (getUFDouble(m_strMoneyKey) != null && !m_strNumKey.equals("")
          && !isKeyExecuted(m_strNumKey)) {
        // ִ�й�ʽ
        String formulas[] = new String[] {
          m_strNumKey + "->" + m_strMoneyKey + "/" + m_strNetPriceKey
        };
        execFormulas(formulas);
        // ���ø�KEY�Ѹı�
        setKeysExecuted(CIRCLE_NUM_NETPRICE_MONEY, true);
        // ����ִ�б��
        setCircleExecuted(CIRCLE_NUM_NETPRICE_MONEY, true);
        // ִ����һ��CIRCLE
        execCircle_QualifiedNum(m_strNumKey);
        execCircle_ConvertRate(m_strNumKey);
        execCircle_NumNetTaxPriceSummny(m_strNumKey);
        execCircle_PriceNetPrice(m_strNetPriceKey, false);
        return;
      }
    }

    // //////////////���ı�
    if (sChangedKey.equals(m_strMoneyKey)) {
      // ֻ�е���������ǽ��ʱ�������ɴ�����������ɴ˹�ʽ���
      if (getFirstChangedKey().equals(m_strMoneyKey)) {
        // �������ھ�����
        if (getUFDouble(m_strNumKey) != null && !m_strNetPriceKey.equals("")
            && !isKeyExecuted(m_strNetPriceKey)) {
          // ִ�й�ʽ
          String formulas[] = new String[] {
            m_strNetPriceKey + "->" + m_strMoneyKey + "/" + m_strNumKey
          };
          execFormulas(formulas);
          // ���ø�KEY�Ѹı�
          setKeysExecuted(CIRCLE_NUM_NETPRICE_MONEY, true);
          // ����ִ�б��
          setCircleExecuted(CIRCLE_NUM_NETPRICE_MONEY, true);
          // ִ����һ��CIRCLE
          execCircle_NetTaxPriceNetPrice(m_strNetPriceKey, false);
          execCircle_PriceNetPrice(m_strNetPriceKey, false);
          execCircle_TaxPricePrice(m_strPriceKey, false);
          execCircle_Taxrate(m_strMoneyKey);
          return;
        }
      }

      if (getUFDouble(m_strNetPriceKey) != null && !m_strNumKey.equals("")
          && !isKeyExecuted(m_strNumKey)) {
        // ִ�й�ʽ
        String formulas[] = new String[] {
          m_strNumKey + "->" + m_strMoneyKey + "/" + m_strNetPriceKey
        };
        execFormulas(formulas);
        // ���ø�KEY�Ѹı�
        setKeysExecuted(CIRCLE_NUM_NETPRICE_MONEY, true);
        // ����ִ�б��
        setCircleExecuted(CIRCLE_NUM_NETPRICE_MONEY, true);
        // ִ����һ��CIRCLE
        execCircle_QualifiedNum(m_strNumKey);
        execCircle_ConvertRate(m_strNumKey);
        execCircle_Taxrate(m_strMoneyKey);
        return;
      }
    }
  }

  /**
   * ��4����������˰���ۡ���˰�ϼ�
   * ���ߣ���ӡ�� ���ܣ�ִ������������˰���ۡ���˰�ϼ�CIECLE ������String sChangedKey ���KEY ���أ��� ���⣺��
   * ���ڣ�(2001-06-09 11:22:51) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
   */
  private void execCircle_NumNetTaxPriceSummny(String sChangedKey) {

    // ��ִ�й��򲻿�ִ��,����ִ��
    if (isCircleExecuted(CIRCLE_NUM_NETTAXPRICE_SUMMNY)
        || !isCircleExecutable(CIRCLE_NUM_NETTAXPRICE_SUMMNY, sChangedKey))
      return;

    // ///////////////�����ı�
    if (sChangedKey.equals(m_strNumKey)) {
      // //////����˰�������ڼ�˰�ϼ�

      // ��˰�ϼ� = ���� * ����˰����
      if (getUFDouble(m_strNetTaxPriceKey) != null
          && !m_strSummnyKey.equals("") && !isKeyExecuted(m_strSummnyKey)) {
        // ִ�й�ʽ
        String formulas[] = new String[] {
          m_strSummnyKey + "->" + m_strNumKey + "*" + m_strNetTaxPriceKey
        };
        execFormulas(formulas);
        // ���ø�KEY�Ѹı�
        setKeysExecuted(CIRCLE_NUM_NETTAXPRICE_SUMMNY, true);
        // ����ִ�б��
        setCircleExecuted(CIRCLE_NUM_NETTAXPRICE_SUMMNY, true);
        // ִ����һ��CIRCLE
        execCircle_Taxrate(m_strSummnyKey);
        execCircle_NumNetTaxPriceSummny(m_strNetTaxPriceKey);
        execCircle_ConvertRate(m_strNumKey);
        execCircle_Qt_ConvertRate(m_strNumKey);//���۵�λ������ϵȦ
        execCircle_NumNetPriceMoney(m_strNumKey);
        return;
      }

      // ����˰���� = ��˰�ϼ� / ����
      if (getUFDouble(m_strSummnyKey) != null
          && !m_strNetTaxPriceKey.equals("")
          && !isKeyExecuted(m_strNetTaxPriceKey)) {
        // ִ�й�ʽ
        String formulas[] = new String[] {
          m_strNetTaxPriceKey + "->" + m_strSummnyKey + "/" + m_strNumKey
        };
        execFormulas(formulas);
        // ���ø�KEY�Ѹı�
        setKeysExecuted(CIRCLE_NUM_NETTAXPRICE_SUMMNY, true);
        // ����ִ�б��
        setCircleExecuted(CIRCLE_NUM_NETTAXPRICE_SUMMNY, true);
        // ִ����һ��CIRCLE
        execCircle_TaxPriceNetTaxPrice(m_strNetTaxPriceKey, false);
        execCircle_ConvertRate(m_strNumKey);
        execCircle_Qt_ConvertRate(m_strNumKey);//���۵�λ������ϵȦ
        execCircle_NumNetPriceMoney(m_strNumKey);
        return;
      }

    }
    // ///////////////��˰�ϼƸı�
    if (sChangedKey.equals(m_strSummnyKey)) {
      // //////�������ھ���˰����

      /**�����ж��Ƕ����,���Ҵ���bug**/
      // ֻ�е����ȱ仯������(��˰�ϼƻ�˰��)ʱ����˰�����ɴ�����������ɴ˹�ʽ���
      /*if (getFirstChangedKey().equals(m_strSummnyKey)
          || getFirstChangedKey().equals(m_strTaxKey)) {*/
      /************************************************/
    	
      // ��˰���� = ��˰�ϼ� / ����
      if (getUFDouble(m_strNumKey) != null && !m_strNetTaxPriceKey.equals("")
            && !isKeyExecuted(m_strNetTaxPriceKey)) {
          // ִ�й�ʽ
          String formulas[] = new String[] {
        //add by ouyangzhb 2011-05-04 ���س�Ӧ����ʱȡ���۵ľ���ֵ
            m_strNetTaxPriceKey + "->abs(" + m_strSummnyKey + "/" + m_strNumKey+")"
          };
          execFormulas(formulas);
          // ���ø�KEY�Ѹı�
          setKeysExecuted(CIRCLE_NUM_NETTAXPRICE_SUMMNY, true);
          // ����ִ�б��
          setCircleExecuted(CIRCLE_NUM_NETTAXPRICE_SUMMNY, true);
          // ִ����һ��CIRCLE
          execCircle_NetTaxPriceNetPrice(m_strNetTaxPriceKey, false);
          execCircle_TaxPriceNetTaxPrice(m_strNetTaxPriceKey, false);
          execCircle_TaxPricePrice(m_strTaxPriceKey, false);
          execCircle_Taxrate(m_strSummnyKey);
          return;
      }

      // ���� = ��˰�ϼ� / ����˰����
      if (getUFDouble(m_strNetTaxPriceKey) != null && !m_strNumKey.equals("")
          && !isKeyExecuted(m_strNumKey)) {
        // ִ�й�ʽ
        String formulas[] = new String[] {
          m_strNumKey + "->" + m_strSummnyKey + "/" + m_strNetTaxPriceKey
        };
        execFormulas(formulas);
        // ���ø�KEY�Ѹı�
        setKeysExecuted(CIRCLE_NUM_NETTAXPRICE_SUMMNY, true);
        // ����ִ�б��
        setCircleExecuted(CIRCLE_NUM_NETTAXPRICE_SUMMNY, true);
        // ִ����һ��CIRCLE
        execCircle_NumNetPriceMoney(m_strNumKey);
        execCircle_QualifiedNum(m_strNumKey);
        execCircle_Taxrate(m_strSummnyKey);
        execCircle_ConvertRate(m_strNumKey);
        return;
      }
    }

    // ////////////////��˰���۸ı�
    if (sChangedKey.equals(m_strNetTaxPriceKey)) {
      // �������ڼ�˰�ϼ�
      if (getUFDouble(m_strNumKey) != null && !m_strSummnyKey.equals("")
          && !isKeyExecuted(m_strSummnyKey)) {
        // ִ�й�ʽ
        String formulas[] = new String[] {
          m_strSummnyKey + "->" + m_strNumKey + "*" + m_strNetTaxPriceKey
        };
        execFormulas(formulas);
        // ���ø�KEY�Ѹı�
        setKeysExecuted(CIRCLE_NUM_NETTAXPRICE_SUMMNY, true);
        // ����ִ�б��
        setCircleExecuted(CIRCLE_NUM_NETTAXPRICE_SUMMNY, true);
        // ִ����һ��CIRCLE
        execCircle_Taxrate(m_strSummnyKey);
        execCircle_TaxPriceNetTaxPrice(m_strNetTaxPriceKey, false);
        return;
      }

      if (getUFDouble(m_strSummnyKey) != null && !m_strNumKey.equals("")
          && !isKeyExecuted(m_strNumKey)) {
        // ִ�й�ʽ
        String formulas[] = new String[] {
          m_strNumKey + "->" + m_strSummnyKey + "/" + m_strNetTaxPriceKey
        };
        execFormulas(formulas);
        // ���ø�KEY�Ѹı�
        setKeysExecuted(CIRCLE_NUM_NETTAXPRICE_SUMMNY, true);
        // ����ִ�б��
        setCircleExecuted(CIRCLE_NUM_NETTAXPRICE_SUMMNY, true);
        // ִ����һ��CIRCLE
        execCircle_NumNetPriceMoney(m_strNumKey);
        execCircle_QualifiedNum(m_strNumKey);
        execCircle_TaxPriceNetTaxPrice(m_strNetTaxPriceKey, false);
        execCircle_ConvertRate(m_strNumKey);
        return;
      }
    }

  }

  /**
   * ��6����˰���ۡ���˰���ۡ���Ʒ�ۿ�
   * ���ߣ���ӡ�� ���ܣ�ִ�� ���ۡ����ʡ�������CIRCLE ������String sChangedKey Ӱ���CIRCLE���� boolean
   * bExecFollowing �Ƿ�ִ������CIRCLE ���أ��� ���⣺�� ���ڣ�(2002-5-15 11:39:21)
   * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־�� 2002-05-16 wyf �������ȿ���
   * 2008-03-07  zhangcheng v5.3 ������
   */
  private void execCircle_PriceNetPrice(String sChangedKey,
      boolean bExecFollowing) {

    // ��ִ�й��򲻿�ִ��,����ִ��
    if (isCircleExecuted(CIRCLE_PRICE_NETPRICE)
        || !isCircleExecutable(CIRCLE_PRICE_NETPRICE, sChangedKey))
      return;

    // ////////////////�����۸ı�
    if (sChangedKey.equals(m_strNetPriceKey)) {
      // ���ۿ�(Ĭ��):����һ�θı������������λ�ļ۸��Ҳ����ǵ��ۿ�ʱ
      if (getUFDouble(m_strPriceKey) != null
          && !m_strDiscountRateKey.equals("")
          && !isKeyExecuted(m_strDiscountRateKey)
          && (getFirstChangedKey().equals(m_strTaxPriceKey)
          		  ||getFirstChangedKey().equals(m_strNetTaxPriceKey)
          		  ||getFirstChangedKey().equals(m_strNetPriceKey)
          		  ||getFirstChangedKey().equals(m_strPriceKey)
          		  ||getFirstChangedKey().equals(m_strMoneyKey))
          		  ) {
        // ���۸����ȹ���������
        if (getPrior_Price_TaxPrice() == RelationsCalVO.TAXPRICE_PRIOR_TO_PRICE) {
          // ��˰�������� ����=����˰����/��˰����
          execCircle_NetTaxPriceNetPrice(m_strNetPriceKey, false);
          execCircle_TaxPriceNetTaxPrice(m_strNetTaxPriceKey, false);
        }
        else {
          // ִ�й�ʽ
          /** v5.3 �������������ۿ�ѡ��ʽ */	
          /*String formulas[] = new String[] {
            m_strDiscountRateKey + "->100.0*" + m_strNetPriceKey + "/"
                + m_strPriceKey
          };*/
          String formulas[] = getPrice_NetPrice_Discount( m_strDiscountRateKey );
          /*******************************************/
          
          execFormulas(formulas);
        }
        // ���ø�KEY�Ѹı�
        setKeysExecuted(CIRCLE_PRICE_NETPRICE, true);
        // ����ִ�б��
        setCircleExecuted(CIRCLE_PRICE_NETPRICE, true);
        // ִ����һ��CIRCLE
        if (bExecFollowing) {
          // execCircle_TaxPriceNetTaxPrice(m_strDiscountRate,false) ;
          execCircle_NetTaxPriceNetPrice(m_strNetPriceKey, false);
          execCircle_NumNetPriceMoney(m_strNetPriceKey);
        }
        return;
      }

      //������:����һ�θı�ķ���������λ�ļ۸���߲����ǵ�����ʱ 
      if (getUFDouble(m_strDiscountRateKey) != null
          && !m_strDiscountRateKey.equals("") && !isKeyExecuted(m_strPriceKey)) {
    	//����= ������ / ����
        String formulas[] = getPrice_NetPrice_Discount( m_strPriceKey );      
        execFormulas(formulas);
        // ���ø�KEY�Ѹı�
        setKeysExecuted(CIRCLE_PRICE_NETPRICE, true);
        // ����ִ�б��
        setCircleExecuted(CIRCLE_PRICE_NETPRICE, true);
        // ִ����һ��CIRCLE
        if (bExecFollowing) {
          execCircle_TaxPricePrice(m_strPriceKey, false);
          execCircle_NetTaxPriceNetPrice(m_strNetPriceKey, false);
          execCircle_NumNetPriceMoney(m_strNetPriceKey);
        }
        return;
      }
    }

    // ////////////���۸ı�
    if (sChangedKey.equals(m_strPriceKey)) {
      // ////////���� ���� ������

      // ������ = ����* ����
      if (getUFDouble(m_strDiscountRateKey) != null
          && !m_strNetPriceKey.equals("") && !isKeyExecuted(m_strNetPriceKey)) {
    	
    	/** v5.3 �������������ۿ�ѡ��ʽ */  
        // ִ�й�ʽ
       /* String formulas[] = new String[] {
          m_strNetPriceKey + "->" + m_strPriceKey + "*(" + m_strDiscountRateKey
              + "/100.0)"
        };*/
        String formulas[] = getPrice_NetPrice_Discount( m_strNetPriceKey );
        /*******************************************/
        
        execFormulas(formulas);
        // ���ø�KEY�Ѹı�
        setKeysExecuted(CIRCLE_PRICE_NETPRICE, true);
        // ����ִ�б��
        setCircleExecuted(CIRCLE_PRICE_NETPRICE, true);
        // ִ����һ��CIRCLE
        if (bExecFollowing) {
          execCircle_NetTaxPriceNetPrice(m_strNetPriceKey, false);
          execCircle_NumNetPriceMoney(m_strNetPriceKey);
          execCircle_TaxPricePrice(m_strPriceKey, false);
        }
        return;
      }

      // ���� = ������ / ����
      if (getUFDouble(m_strNetPriceKey) != null
          && !m_strDiscountRateKey.equals("")
          && !isKeyExecuted(m_strDiscountRateKey)) {
    	  
    	/** v5.3 �������������ۿ�ѡ��ʽ */  
        // ִ�й�ʽ
        /*String formulas[] = new String[] {
          m_strDiscountRateKey + "->100.0*" + m_strNetPriceKey + "/"
              + m_strPriceKey
        };*/
        String formulas[] = getPrice_NetPrice_Discount( m_strDiscountRateKey );
        /*******************************************/
        
        execFormulas(formulas);
        // ���ø�KEY�Ѹı�
        setKeysExecuted(CIRCLE_PRICE_NETPRICE, true);
        // ����ִ�б��
        setCircleExecuted(CIRCLE_PRICE_NETPRICE, true);
        // ִ����һ��CIRCLE
        if (bExecFollowing) {
          execCircle_TaxPriceNetTaxPrice(m_strDiscountRateKey, false);
          execCircle_TaxPricePrice(m_strPriceKey, false);
        }
        return;
      }
    }

    // ////////////���ʸı�
    if (sChangedKey.equals(m_strDiscountRateKey)||sChangedKey.equals(m_strAllDiscountRateKey)) {
      // //////�������� ������
      // ������ = ����* ����
      if (getUFDouble(m_strPriceKey) != null && !m_strNetPriceKey.equals("")
          && !isKeyExecuted(m_strNetPriceKey)) {
    	  
    	/** v5.3 �������������ۿ�ѡ��ʽ */  
        // ִ�й�ʽ
    	String formulas[] = getPrice_NetPrice_Discount( m_strNetPriceKey );
        /*******************************************/
    	
        execFormulas(formulas);
        // ���ø�KEY�Ѹı�
        setKeysExecuted(CIRCLE_PRICE_NETPRICE, true);
        // ����ִ�б��
        setCircleExecuted(CIRCLE_PRICE_NETPRICE, true);
        // ִ����һ��CIRCLE
        if (bExecFollowing) {
          execCircle_NetTaxPriceNetPrice(m_strNetPriceKey, false);
          execCircle_NumNetPriceMoney(m_strNetPriceKey);
          //���ȱ�֤��˰�����ߵĿ��ʹ�ϵ�����Բ��ٽ��루7����˰���ۡ���˰���ۡ���Ʒ�ۿ�
          //execCircle_TaxPriceNetTaxPrice(m_strDiscountRateKey, false);
        }
        return;
      }

      // ���� = ������ / ����
      if (getUFDouble(m_strDiscountRateKey) != null
          && !m_strDiscountRateKey.equals("") && !isKeyExecuted(m_strPriceKey)) {
    	  
    	/** v5.3 �������������ۿ�ѡ��ʽ */  
        // ִ�й�ʽ
       /* String formulas[] = new String[] {
          m_strPriceKey + "->100.0*" + m_strNetPriceKey + "/"
              + m_strDiscountRateKey
        };*/
        String formulas[] = getPrice_NetPrice_Discount( m_strPriceKey );
        /*******************************************/
        
        execFormulas(formulas);
        // ���ø�KEY�Ѹı�
        setKeysExecuted(CIRCLE_PRICE_NETPRICE, true);
        // ����ִ�б��
        setCircleExecuted(CIRCLE_PRICE_NETPRICE, true);
        // ִ����һ��CIRCLE
        if (bExecFollowing) {
        }
        return;
      }
    }

  }

  /**
   * ���ߣ���ӡ�� ���ܣ��ϸ�+���ϸ�����=����CIRCLE��,����һ������ı������������� ������String sChangedKey
   * Ӱ���CIRCLE���� ���أ��� ���⣺�� ���ڣ�(2002-5-15 11:39:21) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
   */
  private void execCircle_QualifiedNum(String sChangedKey) {

    // ��ִ�й��򲻿�ִ��,����ִ��
    if (isCircleExecuted(CIRCLE_QUALIFIED_NUM)
        || !isCircleExecutable(CIRCLE_QUALIFIED_NUM, sChangedKey))
      return;

    // ////////////////�����ı�
    if (sChangedKey.equals(m_strNumKey)) {
      // �ϸ����ڲ��ϸ�
      if (getUFDouble(m_strQualifiedNumKey) != null
          && !m_strUnQualifiedNumKey.equals("")
          && !isKeyExecuted(m_strQualifiedNumKey)
          && !isKeyExecuted(m_strUnQualifiedNumKey)) {
        // �ϸ��������ڵ�������,��ϸ�����=��������
        if (getUFDouble(m_strQualifiedNumKey).compareTo(
            getUFDouble(m_strNumKey)) >= 0) {
          // m_pnlBill.setBodyValueAt(getUFDouble(m_strNumKey),m_iPos,m_strQualifiedNumKey)
          // ;
          m_voCurr.setAttributeValue(m_strQualifiedNumKey,
              getUFDouble(m_strNumKey));
        }
        // ִ�й�ʽ
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
        // ���ø�KEY�Ѹı�
        setKeysExecuted(CIRCLE_QUALIFIED_NUM, true);
        // ����ִ�б��
        setCircleExecuted(CIRCLE_QUALIFIED_NUM, true);
        // ִ����һ��CIECLE
        execCircle_ConvertRate(m_strNumKey);
        return;
      }

      if (getUFDouble(m_strUnQualifiedNumKey) != null
          && !m_strQualifiedNumKey.equals("")
          && !isKeyExecuted(m_strQualifiedNumKey)) {
        // ִ�й�ʽ
        String formulas[] = new String[] {
          m_strQualifiedNumKey + "->" + m_strNumKey + "-"
              + m_strUnQualifiedNumKey
        };
        execFormulas(formulas);
        // ���ø�KEY�Ѹı�
        setKeysExecuted(CIRCLE_QUALIFIED_NUM, true);
        // ����ִ�б��
        setCircleExecuted(CIRCLE_QUALIFIED_NUM, true);
        // ִ����һ��CIECLE
        execCircle_ConvertRate(m_strNumKey);
        return;
      }
    }

    // //////////////�ϸ������ı�
    if (sChangedKey.equals(m_strQualifiedNumKey)) {
      // �������ڲ��ϸ�����
      if (getUFDouble(m_strNumKey) != null
          && !m_strUnQualifiedNumKey.equals("")
          && !isKeyExecuted(m_strUnQualifiedNumKey)) {
        // ִ�й�ʽ
        String formulas[] = new String[] {
          m_strUnQualifiedNumKey + "->" + m_strNumKey + "-"
              + m_strQualifiedNumKey
        };
        execFormulas(formulas);
        // ���ø�KEY�Ѹı�
        setKeysExecuted(CIRCLE_QUALIFIED_NUM, true);
        // ����ִ�б��
        setCircleExecuted(CIRCLE_QUALIFIED_NUM, true);
        return;
      }

      if (getUFDouble(m_strUnQualifiedNumKey) != null
          && !m_strNumKey.equals("") && !isKeyExecuted(m_strNumKey)) {
        // ִ�й�ʽ
        String formulas[] = new String[] {
          m_strNumKey + "->" + m_strQualifiedNumKey + "+"
              + m_strUnQualifiedNumKey
        };
        execFormulas(formulas);
        // ���ø�KEY�Ѹı�
        setKeysExecuted(CIRCLE_QUALIFIED_NUM, true);
        // ����ִ�б��
        setCircleExecuted(CIRCLE_QUALIFIED_NUM, true);
        // ִ����һ��CIRCLE
        execCircle_NumNetPriceMoney(m_strNumKey);
        execCircle_ConvertRate(m_strNumKey);
        return;
      }
    }

    // //////////////���ϸ������ı�
    if (sChangedKey.equals(m_strUnQualifiedNumKey)) {
      // �������ںϸ�����
      if (getUFDouble(m_strNumKey) != null && !m_strQualifiedNumKey.equals("")
          && !isKeyExecuted(m_strQualifiedNumKey)) {
        // ִ�й�ʽ
        String formulas[] = new String[] {
          m_strQualifiedNumKey + "->" + m_strNumKey + "-"
              + m_strUnQualifiedNumKey
        };
        execFormulas(formulas);
        // ���ø�KEY�Ѹı�
        setKeysExecuted(CIRCLE_QUALIFIED_NUM, true);
        // ����ִ�б��
        setCircleExecuted(CIRCLE_QUALIFIED_NUM, true);
        return;
      }

      if (getUFDouble(m_strQualifiedNumKey) != null && !m_strNumKey.equals("")
          && !isKeyExecuted(m_strNumKey)) {
        // ִ�й�ʽ
        String formulas[] = new String[] {
          m_strNumKey + "->" + m_strQualifiedNumKey + "+"
              + m_strUnQualifiedNumKey
        };
        execFormulas(formulas);
        // ���ø�KEY�Ѹı�
        setKeysExecuted(CIRCLE_QUALIFIED_NUM, true);
        // ����ִ�б��
        setCircleExecuted(CIRCLE_QUALIFIED_NUM, true);
        // ִ����һ��CIRCLE
        execCircle_NumNetPriceMoney(m_strNumKey);
        execCircle_ConvertRate(m_strNumKey);
        return;
      }
    }
  }

  /**
   * ��7��= ��˰���ۣ���˰���ۣ���Ʒ�ۿ�
   * ���ߣ���ӡ�� ���ܣ�ִ�к�˰���ۡ����ʡ�����˰����CIRCLE ������String sChangedKey ���KEY boolean
   * bExecFollowing �Ƿ�ִ������CIRCLE ���أ��� ���⣺�� ���ڣ�(2003-06-09 11:22:51)
   * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��2008-03-07  zhangcheng v5.3 ������
   */
  private void execCircle_TaxPriceNetTaxPrice(String sChangedKey,
      boolean bExecFollowing) {

    // ��ִ�й��򲻿�ִ��,����ִ��
    if (isCircleExecuted(CIRCLE_TAXPRICE_NETTAXPRICE)
        || !isCircleExecutable(CIRCLE_TAXPRICE_NETTAXPRICE, sChangedKey)) {
      return;
    }

    // ////////////////(��˰����)����˰���۸ı�
    if (sChangedKey.equals(m_strNetTaxPriceKey)) {

      // ���ۿ�(Ĭ��):����һ�θı������������λ�ļ۸�����Ҳ����ǵ��ۿ�ʱ
      if (getUFDouble(m_strTaxPriceKey) != null
          && !m_strDiscountRateKey.equals("")
          && !isKeyExecuted(m_strDiscountRateKey)
          && (getFirstChangedKey().equals(m_strTaxPriceKey)
        		  ||getFirstChangedKey().equals(m_strNetTaxPriceKey)
        		  ||getFirstChangedKey().equals(m_strNetPriceKey)
        		  ||getFirstChangedKey().equals(m_strPriceKey)
        		  ||getFirstChangedKey().equals(m_strSummnyKey))
          ) {
        // ִ�й�ʽ
        // ���۸����ȹ���������
        if (getPrior_Price_TaxPrice() == RelationsCalVO.PRICE_PRIOR_TO_TAXPRICE) {
          // ��˰�������� ����=������/����
          execCircle_NetTaxPriceNetPrice(m_strNetTaxPriceKey, false);
          execCircle_PriceNetPrice(m_strNetPriceKey, false);
        }
        else {
          /** v5.3 �������������ۿ�ѡ��ʽ */	
          /*String formulas[] = new String[] {
            m_strDiscountRateKey + "->100.0*" + m_strNetTaxPriceKey + "/"
                + m_strTaxPriceKey
          };*/	
          String formulas[] = getTaxPrice_NetTaxPrice_Discount( m_strDiscountRateKey );
          /*****************************/
          
          execFormulas(formulas);
        }
        // ���ø�KEY�Ѹı�
        setKeysExecuted(CIRCLE_TAXPRICE_NETTAXPRICE, true);
        // ����ִ�б��
        setCircleExecuted(CIRCLE_TAXPRICE_NETTAXPRICE, true);
        // ִ����һ��CIRCLE
        if (bExecFollowing) {
          // execCircle_PriceNetPrice(m_strDiscountRate,false) ;
          execCircle_NetTaxPriceNetPrice(m_strNetTaxPriceKey, false);
          execCircle_NumNetTaxPriceSummny(m_strNetTaxPriceKey);
        }
        return;
      }

      //������:����һ�θı�ķ���������λ�ļ۸���߲����ǵ�����ʱ
      if (getUFDouble(m_strDiscountRateKey) != null
          && !m_strTaxPriceKey.equals("") && !isKeyExecuted(m_strTaxPriceKey)) {
    	// ִ�й�ʽ����˰����= ����˰���� / ����
    	String formulas[] = getTaxPrice_NetTaxPrice_Discount( m_strTaxPriceKey );
        execFormulas(formulas);
        // ���ø�KEY�Ѹı�
        setKeysExecuted(CIRCLE_TAXPRICE_NETTAXPRICE, true);
        // ����ִ�б��
        setCircleExecuted(CIRCLE_TAXPRICE_NETTAXPRICE, true);
        // ִ����һ��CIRCLE
        if (bExecFollowing) {
          execCircle_TaxPricePrice(m_strTaxPriceKey, false);
          execCircle_NetTaxPriceNetPrice(m_strNetTaxPriceKey, false);
          execCircle_NumNetTaxPriceSummny(m_strNetTaxPriceKey);
        }
        return;
      }
    }

    // ////////////��˰���۸ı�
    if (sChangedKey.equals(m_strTaxPriceKey)) {
      // ////////���� ���� ����˰����

      // ����˰���� = ��˰����* ����
      if (getUFDouble(m_strDiscountRateKey) != null
          && !m_strNetTaxPriceKey.equals("")
          && !isKeyExecuted(m_strNetTaxPriceKey)) {
        // ִ�й�ʽ
    	/** v5.3 �������������ۿ�ѡ��ʽ */
        /*String formulas[] = new String[] {
          m_strNetTaxPriceKey + "->" + m_strTaxPriceKey + "*("
              + m_strDiscountRateKey + "/100.0)"
        };*/
        String formulas[] = getTaxPrice_NetTaxPrice_Discount( m_strNetTaxPriceKey );
        /*****************************/
        
        execFormulas(formulas);
        // ���ø�KEY�Ѹı�
        setKeysExecuted(CIRCLE_TAXPRICE_NETTAXPRICE, true);
        // ����ִ�б��
        setCircleExecuted(CIRCLE_TAXPRICE_NETTAXPRICE, true);
        // ִ����һ��CIRCLE
        if (bExecFollowing) {
          execCircle_NetTaxPriceNetPrice(m_strNetTaxPriceKey, false);
          execCircle_NumNetTaxPriceSummny(m_strNetTaxPriceKey);
          execCircle_TaxPricePrice(m_strTaxPriceKey, false);
        }
        return;
      }

      // ���� = ����˰���� / ��˰����
      if (getUFDouble(m_strNetTaxPriceKey) != null
          && !m_strDiscountRateKey.equals("")
          && !isKeyExecuted(m_strDiscountRateKey)) {
        // ִ�й�ʽ
    	/** v5.3 �������������ۿ�ѡ��ʽ */
        /*String formulas[] = new String[] {
          m_strDiscountRateKey + "->100.0*" + m_strNetTaxPriceKey + "/"
              + m_strTaxPriceKey
        };*/
        String formulas[] = getTaxPrice_NetTaxPrice_Discount( m_strDiscountRateKey );
        /*****************************/
        
        execFormulas(formulas);
        // ���ø�KEY�Ѹı�
        setKeysExecuted(CIRCLE_TAXPRICE_NETTAXPRICE, true);
        // ����ִ�б��
        setCircleExecuted(CIRCLE_TAXPRICE_NETTAXPRICE, true);
        // ִ����һ��CIRCLE
        if (bExecFollowing) {
          // execCircle_PriceNetPrice(m_strDiscountRate,false) ;
        }
        return;
      }
    }

    // ////////////���ʸı�
    if (sChangedKey.equals(m_strDiscountRateKey)||sChangedKey.equals(m_strAllDiscountRateKey)) {
      // Ĭ��Ϊ��˰��������
      // ��˰���� = ��˰����* ��Ʒ�ۿ� * �����ۿ� (����˰���� = ��˰����* ����) 
      if (getUFDouble(m_strTaxPriceKey) != null
          && !m_strNetTaxPriceKey.equals("")
          && !isKeyExecuted(m_strNetTaxPriceKey)) {
        // ִ�й�ʽ:
    	/** v5.3 �������������ۿ�ѡ��ʽ */
    	String formulas[] = getTaxPrice_NetTaxPrice_Discount( m_strNetTaxPriceKey );
        /*****************************/
    	
        execFormulas(formulas);
        // ���ø�KEY�Ѹı�
        setKeysExecuted(CIRCLE_TAXPRICE_NETTAXPRICE, true);
        // ����ִ�б��
        setCircleExecuted(CIRCLE_TAXPRICE_NETTAXPRICE, true);
        // ִ����һ��CIRCLE
        if (bExecFollowing) {
          execCircle_NetTaxPriceNetPrice(m_strNetTaxPriceKey, false);
          execCircle_NumNetTaxPriceSummny(m_strNetTaxPriceKey);
          //���ȱ�֤��˰�����ߵĿ��ʹ�ϵ�����Բ��ٽ��루6����˰���ۡ���˰���ۡ���Ʒ�ۿ�
          //execCircle_PriceNetPrice(m_strDiscountRateKey,false) ;
        }
        return;
      }

      // ��˰����= ����˰���� / ����
      if (getUFDouble(m_strNetTaxPriceKey) != null
          && !m_strTaxPriceKey.equals("") && !isKeyExecuted(m_strTaxPriceKey)) {
        // ִ�й�ʽ
    	/** v5.3 �������������ۿ�ѡ��ʽ */  
        /*String formulas[] = new String[] {
          m_strTaxPriceKey + "->" + m_strNetTaxPriceKey + "/("
              + m_strDiscountRateKey + "/100.0)"
        };*/
    	String formulas[] = getTaxPrice_NetTaxPrice_Discount( m_strTaxPriceKey );
        /*****************************/
        
        execFormulas(formulas);
        // ���ø�KEY�Ѹı�
        setKeysExecuted(CIRCLE_TAXPRICE_NETTAXPRICE, true);
        // ����ִ�б��
        setCircleExecuted(CIRCLE_TAXPRICE_NETTAXPRICE, true);
        // ִ����һ��CIRCLE
        if (bExecFollowing) {
          execCircle_TaxPriceNetTaxPrice(m_strTaxPriceKey, false);
        }
        return;
      }
    }
  }

  /**
   * (8)��˰����,��˰����,˰��
   * ���ߣ���ӡ�� ���ܣ�˰�ʡ���˰���ۡ�����CIRCLE��,����һ������ı������������� ������String sChangedKey
   * Ӱ���CIRCLE���� ���أ��� ���⣺�� ���ڣ�(2003-10-10 11:39:21) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
   */
  private void execCircle_TaxPricePrice(String sChangedKey,
      boolean bExecFollowing) {

    // ��ִ�й��򲻿�ִ��,����ִ��
    if (isCircleExecuted(CIRCLE_TAXPRICE_PRICE)
        || !isCircleExecutable(CIRCLE_TAXPRICE_PRICE, sChangedKey))
      return;

    // ////////////////���۸ı�
    if (sChangedKey.equals(m_strPriceKey)) {
      // ��˰���� = FUNC(���ۡ�˰��)
      if (!m_strTaxRateKey.equals("") && !isKeyExecuted(m_strTaxPriceKey)) {
        // ִ�й�ʽ
        String formulas[] = new String[] {
          getTaxPrice_TaxratePrice()
        };
        execFormulas(formulas);
        // ���ø�KEY�Ѹı�
        setKeysExecuted(CIRCLE_TAXPRICE_PRICE, true);
        // ����ִ�б��
        setCircleExecuted(CIRCLE_TAXPRICE_PRICE, true);
        // ִ����һ��CIRCLE
        if (bExecFollowing) {
          execCircle_NumNetPriceMoney(m_strPriceKey);
          execCircle_PriceNetPrice(m_strPriceKey, false);
        }
        return;
      }
    }

    // ////////////��˰���۸ı�
    if (sChangedKey.equals(m_strTaxPriceKey)) {
      // ���� = FUNC(��˰���ۡ�˰��)
      if (!m_strTaxPriceKey.equals("") && !isKeyExecuted(m_strPriceKey)) {
        // ִ�й�ʽ
        String formulas[] = new String[] {
          getPrice_TaxrateTaxPrice()
        };
        execFormulas(formulas);
        // ���ø�KEY�Ѹı�
        setKeysExecuted(CIRCLE_TAXPRICE_PRICE, true);
        // ����ִ�б��
        setCircleExecuted(CIRCLE_TAXPRICE_PRICE, true);
        // ִ����һ��CIRCLE
        if (bExecFollowing) {
          execCircle_NumNetTaxPriceSummny(m_strTaxPriceKey);
          execCircle_TaxPriceNetTaxPrice(m_strTaxPriceKey, false);
        }
        return;
      }
    }

    // ////////////˰�ʸı�
    if (sChangedKey.equals(m_strTaxRateKey)) {
      // ��˰����
      if (getPrior_Price_TaxPrice() == RelationsCalVO.PRICE_PRIOR_TO_TAXPRICE) {
        // ��˰���� = FUNC(���ۡ�˰��)
        if (!m_strTaxPriceKey.equals("") && !m_strPriceKey.equals("")
            && getUFDouble(m_strPriceKey) != null
            && !isKeyExecuted(m_strTaxPriceKey)) {
          // ִ�й�ʽ
          String formulas[] = new String[] {
            getTaxPrice_TaxratePrice()
          };
          execFormulas(formulas);
          // ���ø�KEY�Ѹı�
          setKeysExecuted(CIRCLE_TAXPRICE_PRICE, true);
          // ����ִ�б��
          setCircleExecuted(CIRCLE_TAXPRICE_PRICE, true);
          // ִ����һ��CIRCLE
          if (bExecFollowing) {
            execCircle_NetTaxPriceNetPrice(m_strNetPriceKey, true);
          }
          return;
        }
        else {
          // ���� = FUNC(��˰���ۡ�˰��)
          if (!m_strTaxPriceKey.equals("") && !m_strPriceKey.equals("")
              && !isKeyExecuted(m_strPriceKey)) {
            // ִ�й�ʽ
            String formulas[] = new String[] {
              getPrice_TaxrateTaxPrice()
            };
            execFormulas(formulas);
            // ���ø�KEY�Ѹı�
            setKeysExecuted(CIRCLE_TAXPRICE_PRICE, true);
            // ����ִ�б��
            setCircleExecuted(CIRCLE_TAXPRICE_PRICE, true);
            // ִ����һ��CIRCLE
            if (bExecFollowing) {
              execCircle_NetTaxPriceNetPrice(m_strNetTaxPriceKey, true);
            }
            return;
          }
        }
      }
      // ��˰����
      else {
        // ���� = FUNC(��˰���ۡ�˰��)
        if (!m_strTaxPriceKey.equals("") && !m_strPriceKey.equals("")
            && getUFDouble(m_strTaxPriceKey) != null
            && !isKeyExecuted(m_strPriceKey)) {
          // ִ�й�ʽ
          String formulas[] = new String[] {
            getPrice_TaxrateTaxPrice()
          };
          execFormulas(formulas);
          // ���ø�KEY�Ѹı�
          setKeysExecuted(CIRCLE_TAXPRICE_PRICE, true);
          // ����ִ�б��
          setCircleExecuted(CIRCLE_TAXPRICE_PRICE, true);
          // ִ����һ��CIRCLE
          if (bExecFollowing) {
            execCircle_NetTaxPriceNetPrice(m_strNetTaxPriceKey, true);
          }
          return;
        }
        else {
          // ��˰���� = FUNC(���ۡ�˰��)
          if (!m_strTaxPriceKey.equals("") && !m_strPriceKey.equals("")
              && !isKeyExecuted(m_strTaxPriceKey)) {
            // ִ�й�ʽ
            String formulas[] = new String[] {
              getTaxPrice_TaxratePrice()
            };
            execFormulas(formulas);
            // ���ø�KEY�Ѹı�
            setKeysExecuted(CIRCLE_TAXPRICE_PRICE, true);
            // ����ִ�б��
            setCircleExecuted(CIRCLE_TAXPRICE_PRICE, true);
            // ִ����һ��CIRCLE
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
   * (19) ���Ҽ�˰�ϼơ�������˰������˰��
   */
  private void execCircle_Local_Summny_Mny_Tax(String sChangedKey) {
	  
	    // ��ִ�й��򲻿�ִ��,����ִ��
	    if (isCircleExecuted(CIRCLE_LOCAL_SUMMNY_MNY_TAX)
	        || !isCircleExecutable(CIRCLE_LOCAL_SUMMNY_MNY_TAX, sChangedKey))
	      return;
	    
	    //�༭���Ҽ�˰�ϼ�
	    if (sChangedKey.equals(m_strSummny_LocalKey)) {
	    	//˰��Ϊ0��Ϊ��
	    	if (getUFDouble(m_strTaxRateKey) == null
					|| getUFDouble(m_strTaxRateKey).doubleValue() == ZERO
							.doubleValue()) {
	    		m_voCurr.setAttributeValue(m_strMoney_LocalKey, m_voCurr
						.getAttributeValue(m_strSummny_LocalKey));
	    	}
	    	//˰��!=0: ����˰�� = ���Ҽ�˰�ϼ� - ������˰���
	    	else{
	    		if (!isKeyExecuted(m_strTax_LocalKey)){
	    			String[] formulas = new String[] {
	    					m_strTax_LocalKey + "->" + m_strSummny_LocalKey + "-" + m_strMoney_LocalKey
	    		            };
	    			execFormulas(formulas);
	    		}
	    	}	
	    }
	    //�༭������˰���
	    else if (sChangedKey.equals(m_strMoney_LocalKey)){
            //˰��Ϊ0��Ϊ��
	    	if (getUFDouble(m_strTaxRateKey) == null
					|| getUFDouble(m_strTaxRateKey).doubleValue() == ZERO
							.doubleValue()) {
	    		m_voCurr.setAttributeValue(m_strSummny_LocalKey, m_voCurr
						.getAttributeValue(m_strMoney_LocalKey));
	    	}
	    	//˰��!=0: ����˰�� = ���Ҽ�˰�ϼ� - ������˰���
	    	else{
	    		if (!isKeyExecuted(m_strTax_LocalKey)){
	    			String[] formulas = new String[] {
	    					m_strTax_LocalKey + "->" + m_strSummny_LocalKey + "-" + m_strMoney_LocalKey
	    		            };
	    			execFormulas(formulas);
	    		}
	    	}	
	    }
	    
        //���ø�KEY�Ѹı�
        setKeysExecuted(CIRCLE_LOCAL_SUMMNY_MNY_TAX, true);
        // ����ִ�б��
        setCircleExecuted(CIRCLE_LOCAL_SUMMNY_MNY_TAX, true);
  }
  
  
  
  /**
   * ��5��˰�ʡ���˰�ϼơ���˰��˰��
   * ���ߣ���ӡ�� ���ܣ�˰��CIRCLE��,����һ������ı������������� �ú������������������б仯��ϡ� ������String sChangedKey
   * Ӱ���CIRCLE���� ���أ��� ���⣺�� ���ڣ�(2003-06-09 11:22:51) 
   * �޸�����  2008-03-07
   * �޸���    zhangcheng
   * �޸�ԭ��  v5.3������
   * ע�ͱ�־��
   */
  private void execCircle_Taxrate(String sChangedKey) {

    // ��ִ�й��򲻿�ִ��,����ִ��
    if (isCircleExecuted(CIRCLE_TAXRATE)
        || !isCircleExecutable(CIRCLE_TAXRATE, sChangedKey))
      return;

    // ////////////////˰�ʸı�
    if (sChangedKey.equals(m_strTaxRateKey)) {
      // ��� ���� ˰��,��˰�ϼ�

      // -----------��������
      if (getPrior_Price_TaxPrice() == RelationsCalVO.PRICE_PRIOR_TO_TAXPRICE) {
        // ���,˰�� ---> ˰��,��˰�ϼ�
        if (getUFDouble(m_strMoneyKey) != null && !m_strTaxKey.equals("")
            && !m_strSummnyKey.equals("") && !isKeyExecuted(m_strTaxKey)
            && !isKeyExecuted(m_strSummnyKey)) {
          // ִ�й�ʽ
          String formulas[] = new String[] {
            getTax_TaxrateMoney()
          };
          execFormulas(formulas);
          // ִ�й�ʽ�� ˰��Ϊ��ʱ�����ӽ���ʽ�������ƻ� ���+˰��=��˰�ϼ� �Ĺ�ϵ
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
          // ���ø�KEY�Ѹı�
          setKeysExecuted(CIRCLE_TAXRATE, true);
          // ����ִ�б��
          setCircleExecuted(CIRCLE_TAXRATE, true);
          // ִ����һ��CIRCLE
          {
            //execCircle_NumNetTaxPriceSummny(m_strSummnyKey);
            if (getPrior_Price_TaxPrice() == RelationsCalVO.TAXPRICE_PRIOR_TO_PRICE) {
              // ��˰����
              execCircle_NetTaxPriceNetPrice(m_strNetTaxPriceKey, false);
              execCircle_TaxPricePrice(m_strTaxPriceKey, false);
            }
            else {
              // ��˰����
              execCircle_NetTaxPriceNetPrice(m_strNetPriceKey, false);
              execCircle_TaxPricePrice(m_strPriceKey, false);
            }
          }
          return;
        }
      }
      // -----------��˰��������
      else if (getPrior_Price_TaxPrice() == RelationsCalVO.TAXPRICE_PRIOR_TO_PRICE) {
        // ��˰�ϼ�,˰�� ---> ˰��,���
        if (getUFDouble(m_strSummnyKey) != null && !m_strTaxKey.equals("")
            && !m_strMoneyKey.equals("") && !isKeyExecuted(m_strTaxKey)
            && !isKeyExecuted(m_strMoneyKey)) {
          // ִ�й�ʽ
          String formulas[] = new String[] {
            getTax_TaxrateSummny()
          };
          execFormulas(formulas);
          // ִ�й�ʽ�� ˰��Ϊ��ʱ�����ӽ���ʽ�������ƻ� ���+˰��=��˰�ϼ� �Ĺ�ϵ
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
          // ���ø�KEY�Ѹı�
          setKeysExecuted(CIRCLE_TAXRATE, true);
          // ����ִ�б��
          setCircleExecuted(CIRCLE_TAXRATE, true);
          // ִ����һ��CIRCLE
          {
            //execCircle_NumNetPriceMoney(m_strMoneyKey);
            if (getPrior_Price_TaxPrice() == RelationsCalVO.TAXPRICE_PRIOR_TO_PRICE) {
              // ��˰����
              execCircle_NetTaxPriceNetPrice(m_strNetTaxPriceKey, false);
              execCircle_TaxPricePrice(m_strTaxPriceKey, false);
            }
            else {
              // ��˰����
              execCircle_NetTaxPriceNetPrice(m_strNetPriceKey, false);
              execCircle_TaxPricePrice(m_strPriceKey, false);
            }
          }
          return;
        }
      }
    }

    // ////////////////���ı�
    if (sChangedKey.equals(m_strMoneyKey)) {
      // ˰�� ���� ˰��,��˰�ϼ�

      // ˰�ʲ�Ϊ��ʱ:���,˰�� ---> ˰��,��˰�ϼ�
      if (!m_strTaxRateKey.equals("") && !m_strTaxKey.equals("")
          && !m_strSummnyKey.equals("") && !isKeyExecuted(m_strTaxKey)
          && !isKeyExecuted(m_strSummnyKey)) {
        // ��ȡ��˰��
        String formulas[] = new String[] {
          getTax_TaxrateMoney()
        };
        execFormulas(formulas);
        // ִ�й�ʽ�� ˰��Ϊ��ʱ�����ӽ���ʽ�������ƻ� ���+˰��=��˰�ϼ� �Ĺ�ϵ
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
      // ���ø�KEY�Ѹı�
      setKeysExecuted(CIRCLE_TAXRATE, true);
      // ����ִ�б��
      setCircleExecuted(CIRCLE_TAXRATE, true);
      // ִ����һ��CIRCLE
      execCircle_NumNetPriceMoney(m_strMoneyKey);
      execCircle_NumNetTaxPriceSummny(m_strSummnyKey);
      return; 
    }

    // ////////////////˰��ı�
    if (sChangedKey.equals(m_strTaxKey)) {
      // ˰�� ���� ��˰���,��˰�ϼ�

      // û����˰����˰�ϼơ�˰���ֶ�ʱ �����м���;
      // ���Ҽ�˰�ϼơ�˰��Ϊ��ʱ����˰�ϼ� = ˰��
      if (!m_strTaxRateKey.equals("") && !m_strMoneyKey.equals("")
          && !m_strSummnyKey.equals("") && getUFDouble(m_strMoneyKey) == null
          && getUFDouble(m_strSummnyKey) == null
          && getUFDouble(m_strTaxRateKey) == null) {
        String formulas[] = new String[] {
          m_strSummnyKey + "->" + m_strTaxKey
        };
        execFormulas(formulas);
        // ���ø�KEY�Ѹı�
        setKeysExecuted(CIRCLE_TAXRATE, true);
        // ����ִ�б��
        setCircleExecuted(CIRCLE_TAXRATE, true);
        // ִ����һ��CIRCLE
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

      // ��˰���Ϊ�ա�˰�ʲ�Ϊ��ʱ��ͨ��˰�����ϼ�
      if (!m_strTaxRateKey.equals("") && !m_strMoneyKey.equals("")
          && !m_strSummnyKey.equals("") && m_strDiscountTaxTypeName != null
          && getUFDouble(m_strMoneyKey) == null
          && getUFDouble(m_strTaxRateKey) != null) {

        if (m_strDiscountTaxTypeName.equals("Ӧ˰���")) {
          String formulas[] = new String[] {
              m_strMoneyKey + "->" + m_strTaxKey + "/(" + m_strTaxRateKey
                  + "/100.0)",
              m_strSummnyKey + "->" + m_strMoneyKey + "+" + m_strTaxKey
          };
          execFormulas(formulas);
          // ���ø�KEY�Ѹı�
          setKeysExecuted(CIRCLE_TAXRATE, true);
          // ����ִ�б��
          setCircleExecuted(CIRCLE_TAXRATE, true);
          // ִ����һ��CIRCLE
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
        else if (m_strDiscountTaxTypeName.equals("Ӧ˰�ں�")) {
          String formulas[] = new String[] {
              m_strSummnyKey + "->" + m_strTaxKey + "/(" + m_strTaxRateKey
                  + "/100.0)",
              m_strMoneyKey + "->" + m_strSummnyKey + "-" + m_strTaxKey
          };
          execFormulas(formulas);
          // ���ø�KEY�Ѹı�
          setKeysExecuted(CIRCLE_TAXRATE, true);
          // ����ִ�б��
          setCircleExecuted(CIRCLE_TAXRATE, true);
          // ִ����һ��CIRCLE
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

      // ����˰�ϼơ���˰��˰�ʾ�����ʱ ---> ���ݲ�������Ӱ����˰���Ǽ�˰�ϼ�
      if (!m_strTaxRateKey.equals("") && !m_strMoneyKey.equals("")
          && !m_strSummnyKey.equals("") ) {
        // ִ�й�ʽ
        if (m_strDiscountTaxTypeName == null) {
          return;
        }
        String formulas[] = null;
        /*// ˰��Ϊ��ʱ���˰���Ϊ����˰ʱ�����ӽ���ʽ�������ƻ� ���+˰��=��˰�ϼ� �Ĺ�ϵ
        if (m_strDiscountTaxTypeName.equals("����˰")
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
        // ���ø�KEY�Ѹı�
        setKeysExecuted(CIRCLE_TAXRATE, true);
        // ����ִ�б��
        setCircleExecuted(CIRCLE_TAXRATE, true);
        // ִ����һ��CIRCLE
        execCircle_NumNetTaxPriceSummny(m_strSummnyKey);*/
        	
       /** 
        * �����߼�v5.3�ı䣺����˰�ϼơ���˰��˰�ʾ���Ϊ�գ��ı�˰��ʱ��
        * ��˰���ȣ���˰��� =  ��˰�ϼ� �C ˰��
        * ��˰���ȣ���˰�ϼ� = ��˰��� + ˰��
        * ������������
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
        // ���ø�KEY�Ѹı�
        setKeysExecuted(CIRCLE_TAXRATE, true);
        // ����ִ�б��
        setCircleExecuted(CIRCLE_TAXRATE, true);	
        return;
      //}
    }
   }   

    // ////////////////��˰�ϼƸı�
    if (sChangedKey.equals(m_strSummnyKey)) {
      // ˰�� ���� ���,˰��

      // ��˰�ϼ�,˰�� ---> ˰��,���
      if (!m_strTaxRateKey.equals("") && !m_strTaxKey.equals("")
          && !m_strSummnyKey.equals("") && !isKeyExecuted(m_strTaxKey)
          && !isKeyExecuted(m_strMoneyKey)) {
        // ��ȡ��˰��
        String formulas[] = new String[] {
          getTax_TaxrateSummny()
        };
        execFormulas(formulas);
        // ִ�й�ʽ��˰��Ϊ��ʱ�����ӽ���ʽ�������ƻ� ��˰���+˰��=��˰�ϼ� �Ĺ�ϵ
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
        // ���ø�KEY�Ѹı�
        setKeysExecuted(CIRCLE_TAXRATE, true);
        // ����ִ�б��
        setCircleExecuted(CIRCLE_TAXRATE, true);
        // ִ����һ��CIRCLE
        execCircle_NumNetPriceMoney(m_strMoneyKey);
        execCircle_NumNetTaxPriceSummny(m_strSummnyKey);
        return;
      }

      // ��˰�ϼ�,˰�� ---> ˰��,���
      if (!m_strTaxRateKey.equals("") && !m_strTaxKey.equals("")
          && !m_strSummnyKey.equals("") && isKeyExecuted(m_strMoneyKey)) {
        // ��ȡ��˰��
        String formulas[] = new String[] {
          getTax_TaxrateSummny()
        };
        execFormulas(formulas);
        // ���ø�KEY�Ѹı�
        setKeysExecuted(CIRCLE_TAXRATE, true);
        // ����ִ�б��
        setCircleExecuted(CIRCLE_TAXRATE, true);
        // ִ����һ��CIRCLE
        execCircle_NumNetTaxPriceSummny(m_strSummnyKey);
        return;
      }
    }
  }

  /**
   * ���ߣ���ӡ�� ���ܣ����ݸı����,�õ�����Ӱ�����ЧCIRCLE �������� ���أ���ִ�еĵ�һ����ЧCIRCLE ���⣺�� ���ڣ�(2002-5-15
   * 11:39:21) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
   */
  private int getAvailableCircleByKey(String sFirstChangedKey) {

    int iRetCircle = CIRCLE_IMPOSSIBLE;
    // ����
    if (sFirstChangedKey.equals(m_strNumKey)) {
      // ��˰��������
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
      // ��������
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
    // ��˰����
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
    // ��������
    else if (sFirstChangedKey.equals(m_strQt_NumKey)) {
    	// ��˰����
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
        // ��˰����
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
    // ���ۻ�����
    else if (sFirstChangedKey.equals(m_strQt_ConvertRateKey)) {
      if (isCircleExecutable(CIRCLE_QT_CONVERT_RATE, m_strQt_ConvertRateKey)) {
        iRetCircle = CIRCLE_QT_CONVERT_RATE;
      }
    }
    // ���
    else if (sFirstChangedKey.equals(m_strMoneyKey)) {
      if (isCircleExecutable(CIRCLE_TAXRATE, m_strMoneyKey)) {
        iRetCircle = CIRCLE_TAXRATE;
      }
      else if (isCircleExecutable(CIRCLE_NUM_NETPRICE_MONEY, m_strMoneyKey)) {
        iRetCircle = CIRCLE_NUM_NETPRICE_MONEY;
      }
    }
    // ˰��
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
    // ˰��
    else if (sFirstChangedKey.equals(m_strTaxKey)) {
      if (isCircleExecutable(CIRCLE_TAXRATE, m_strTaxKey)) {
        iRetCircle = CIRCLE_TAXRATE;
      }
    }
    // ��˰�ϼ�
    else if (sFirstChangedKey.equals(m_strSummnyKey)) {
      if (isCircleExecutable(CIRCLE_TAXRATE, m_strSummnyKey)) {
            iRetCircle = CIRCLE_TAXRATE;
          }
      else if (isCircleExecutable(CIRCLE_NUM_NETTAXPRICE_SUMMNY, m_strSummnyKey)) {
        iRetCircle = CIRCLE_NUM_NETTAXPRICE_SUMMNY;
      }
    }
    // ��˰����
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
    // ����(������Ʒ�ۿۣ������ۿ�)
    else if (sFirstChangedKey.equals(m_strDiscountRateKey)
    		|| sFirstChangedKey.equals(m_strAllDiscountRateKey)) {
      // ��˰����(��˰��������)
      if (getPrior_Price_TaxPrice() == RelationsCalVO.TAXPRICE_PRIOR_TO_PRICE) {
        if (isCircleExecutable(CIRCLE_TAXPRICE_NETTAXPRICE,
        		sFirstChangedKey)) {
          iRetCircle = CIRCLE_TAXPRICE_NETTAXPRICE;
        }
        else if (isCircleExecutable(CIRCLE_PRICE_NETPRICE, sFirstChangedKey)) {
          iRetCircle = CIRCLE_PRICE_NETPRICE;
        }
      }
      // ��˰����(��������)
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
    // ��˰����
    else if (sFirstChangedKey.equals(m_strTaxPriceKey)) {
      if (isCircleExecutable(CIRCLE_TAXPRICE_NETTAXPRICE, m_strTaxPriceKey)) {//��˰������,�ۿ�
        iRetCircle = CIRCLE_TAXPRICE_NETTAXPRICE;
      }
      else if (isCircleExecutable(CIRCLE_TAXPRICE_PRICE, m_strTaxPriceKey)) {
        iRetCircle = CIRCLE_TAXPRICE_PRICE;
      }
    }
    // ��˰����
    else if (sFirstChangedKey.equals(m_strPriceKey)) {
      if (isCircleExecutable(CIRCLE_PRICE_NETPRICE, m_strPriceKey)) {
        iRetCircle = CIRCLE_PRICE_NETPRICE;
      }
      else if (isCircleExecutable(CIRCLE_TAXPRICE_PRICE, m_strPriceKey)) {
        iRetCircle = CIRCLE_TAXPRICE_PRICE;
      }
    }
    // �ϸ�����
    else if (sFirstChangedKey.equals(m_strQualifiedNumKey)) {
      if (isCircleExecutable(CIRCLE_QUALIFIED_NUM, m_strQualifiedNumKey)) {
        iRetCircle = CIRCLE_QUALIFIED_NUM;
      }
    }
    // ���ϸ�����
    else if (sFirstChangedKey.equals(m_strUnQualifiedNumKey)) {
      if (isCircleExecutable(CIRCLE_QUALIFIED_NUM, m_strUnQualifiedNumKey)) {
        iRetCircle = CIRCLE_QUALIFIED_NUM;
      }
    }
    // ������
    else if (sFirstChangedKey.equals(m_strAssistNumKey)) {
      if (isCircleExecutable(CIRCLE_CONVERT_RATE, m_strAssistNumKey)) {
        iRetCircle = CIRCLE_CONVERT_RATE;
      }
    }
    // ������
    else if (sFirstChangedKey.equals(m_strConvertRateKey)) {
      if (isCircleExecutable(CIRCLE_CONVERT_RATE, m_strConvertRateKey)) {
        iRetCircle = CIRCLE_CONVERT_RATE;
      }
    }
    // ����ԭ�Һ�˰����
    else if (sFirstChangedKey.equals(m_strQt_TaxPriceKey)) {
      if (isCircleExecutable(CIRCLE_QT_TAXPRICE_NETTAXPRICE, m_strQt_TaxPriceKey)) {
        iRetCircle = CIRCLE_QT_TAXPRICE_NETTAXPRICE;
      }
      else if (isCircleExecutable(CIRCLE_QT_TAXPRICE_PRICE, m_strQt_TaxPriceKey)) {
        iRetCircle = CIRCLE_QT_TAXPRICE_PRICE;
      }
    }
    // ����ԭ����˰����
    else if (sFirstChangedKey.equals(m_strQt_PriceKey)) {
      if (isCircleExecutable(CIRCLE_QT_PRICE_NETPRICE, m_strQt_PriceKey)) {
        iRetCircle = CIRCLE_QT_PRICE_NETPRICE;
      }
      else if (isCircleExecutable(CIRCLE_QT_TAXPRICE_PRICE, m_strQt_PriceKey)) {
        iRetCircle = CIRCLE_QT_TAXPRICE_PRICE;
      }
    }
    // ����ԭ�Һ�˰����
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
    // ����ԭ����˰����
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
    // ���Ҽ�˰�ϼ�
    else if (sFirstChangedKey.equals(m_strSummny_LocalKey)) {
    	iRetCircle = CIRCLE_LOCAL_SUMMNY_MNY_TAX;
    }
    // ������˰���
    else if (sFirstChangedKey.equals(m_strMoney_LocalKey)) {
    	iRetCircle = CIRCLE_LOCAL_SUMMNY_MNY_TAX;
    }

    // ���ø�KEY�ı�󣬲��ı����
    setKeyExecutedForFirstChangedKey(sFirstChangedKey, iRetCircle);
    // ���ø�KEY�Ѹı�
    setKeyExecuted(sFirstChangedKey, true);

    return iRetCircle;
  }

  /**
   * ���ߣ���ӡ�� ���ܣ��õ�����ı��KEY �������� ���أ�����ı��KEY ���⣺�� ���ڣ�(2002-5-15 11:39:21)
   * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
   */
  private String getFirstChangedKey() {
    return m_strChangedKey;
  }

  /**
   * ���ݿ�˰��𡢽�˰�ʵõ���˰�ϼƵļ��㹫ʽ
   * 
   * @param ��
   * @return �õ��ļ�˰�ϼƵļ��㹫ʽ
   * @exception �쳣����
   * @see ��Ҫ�μ�����������
   * @since �������һ���汾���˷�������ӽ���������ѡ��
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
    //���ۼ�����λ
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
    //��/���ۼ�����λID
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
   * ���ߣ��ų� (7)
   * ���ܣ����� ��˰���Ȼ����Լ��Ƿ��������ۿ� 
   * �õ� ��˰���ۡ���˰���ۡ��ۿۼ��㹫ʽ �������� ���أ����ۻ��ۿ۵ļ��㹫ʽ 
   * ���⣺�� ���ڣ�(2008-03-09 11:22:51) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
   */
  private String[] getTaxPrice_NetTaxPrice_Discount( String changekey ) {
	  //�������ۿ�
	  if (m_strAllDiscountRateKey.equals("")){
		  //���㺬˰����
		  if ( m_strTaxPriceKey.equals(changekey) ){
			  return new String[]{ m_strTaxPriceKey + "->" + m_strNetTaxPriceKey + "/("
				+ m_strDiscountRateKey + "/100.0)"};
		  }
		  //���㺬˰����
		  else if (m_strNetTaxPriceKey.equals(changekey)){
			  return new String[]{ m_strNetTaxPriceKey + "->" + m_strTaxPriceKey + "*("
				+ m_strDiscountRateKey + "/100.0)"};
		  }
		  //�����ۿ�
	      else if (m_strDiscountRateKey.equals(changekey)){
	    	  if (getFirstChangedKey().equals(m_strQt_NetTaxPriceKey))
                  return new String[] { m_strDiscountRateKey + "->100.0*"
                            + m_strQt_NetTaxPriceKey + "/" + m_strQt_TaxPriceKey };
              else
                  return new String[] { m_strDiscountRateKey + "->100.0*"
                            + m_strNetTaxPriceKey + "/" + m_strTaxPriceKey };
		  }
		  //���㱨��ԭ�Һ�˰����
	      else if ( m_strQt_TaxPriceKey.equals(changekey) ){
			  return new String[]{ m_strQt_TaxPriceKey + "->" + m_strQt_NetTaxPriceKey + "/("
				+ m_strDiscountRateKey + "/100.0)"};
		  }
		  //���㱨��ԭ�Һ�˰����
	      else if ( m_strQt_NetTaxPriceKey.equals(changekey) ){
	    	  return new String[]{ m_strQt_NetTaxPriceKey + "->" + m_strQt_TaxPriceKey + "*("
	  				+ m_strDiscountRateKey + "/100.0)"};
		  }
	  }
	  //�������ۿ�
	  else {
		  //���㺬˰����
          if ( m_strTaxPriceKey.equals(changekey) ){
        	  return new String[]{ m_strTaxPriceKey + "->" + m_strNetTaxPriceKey
				+ "/((" + m_strDiscountRateKey + "/100.0) * ("
				+ m_strAllDiscountRateKey + "/100.0))"};
		  }
          //���㺬˰����
		  else if (m_strNetTaxPriceKey.equals(changekey)){
			  return new String[]{ m_strNetTaxPriceKey + "->" + m_strTaxPriceKey + "*("
				+ m_strDiscountRateKey + "/100.0)*("
				+ m_strAllDiscountRateKey + "/100.0)"};
		  }
          //�����ۿ�
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
          //���㱨��ԭ�Һ�˰����
	      else if ( m_strQt_TaxPriceKey.equals(changekey) ){
			  return new String[]{ m_strQt_TaxPriceKey + "->" + m_strQt_NetTaxPriceKey
						+ "/((" + m_strDiscountRateKey + "/100.0) * ("
						+ m_strAllDiscountRateKey + "/100.0))"};
		  }
          //���㱨��ԭ�Һ�˰����
	      else if ( m_strQt_NetTaxPriceKey.equals(changekey) ){
	    	  return new String[]{ m_strQt_NetTaxPriceKey + "->" + m_strQt_TaxPriceKey + "*("
	  				+ m_strDiscountRateKey + "/100.0)*("
	  				+ m_strAllDiscountRateKey + "/100.0)"};
		  }
	  }
	  return null;
  }
  
  /**
   * ���ߣ��ų� (6)
   * ���ܣ����� ��˰���Ȼ����Լ��Ƿ��������ۿ� 
   * �õ� ��˰���ۡ���˰���ۡ��ۿۼ��㹫ʽ �������� ���أ����ۻ��ۿ۵ļ��㹫ʽ 
   * ���⣺�� ���ڣ�(2008-03-09 11:22:51) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
   */
  private String[] getPrice_NetPrice_Discount( String changekey ) {
	  	  
	  //�������ۿ�
	  if (m_strAllDiscountRateKey.equals("")){
		  //������˰����
		  if ( m_strPriceKey.equals(changekey) ){
			  return new String[]{ m_strPriceKey + "->" + m_strNetPriceKey + "/("
				+ m_strDiscountRateKey + "/100.0)"};
		  }
		  //������˰����
		  else if (m_strNetPriceKey.equals(changekey)){
			  return new String[]{ m_strNetPriceKey + "->" + m_strPriceKey + "*("
				+ m_strDiscountRateKey + "/100.0)"};
		  }
		  //�����ۿ�
	      else if (m_strDiscountRateKey.equals(changekey)){
	    	  return new String[]{ m_strDiscountRateKey + "->100.0*"
				+ m_strNetPriceKey + "/" + m_strPriceKey};
		  }
		  //���㱨��ԭ����˰����
	      else if ( m_strQt_PriceKey.equals(changekey) ){
	    	  if (getFirstChangedKey().equals(m_strQt_NetPriceKey))
                  return new String[]{ m_strDiscountRateKey + "->100.0*"
                      + m_strQt_NetPriceKey + "/" + m_strQt_PriceKey};
              else
                  return new String[]{ m_strDiscountRateKey + "->100.0*"
                    + m_strNetPriceKey + "/" + m_strPriceKey};
		  }
		  //���㱨��ԭ����˰����
	      else if ( m_strQt_NetPriceKey.equals(changekey) ){
	    	  return new String[]{ m_strQt_NetPriceKey + "->" + m_strQt_PriceKey + "*("
	  				+ m_strDiscountRateKey + "/100.0)"};
		  }
	  }
	  //�������ۿ�
	  else {
		  //������˰����
          if ( m_strPriceKey.equals(changekey) ){
        	  return new String[]{ m_strPriceKey + "->" + m_strNetPriceKey
				+ "/((" + m_strDiscountRateKey + "/100.0) * ("
				+ m_strAllDiscountRateKey + "/100.0))"};
		  }
          //������˰����
		  else if (m_strNetPriceKey.equals(changekey)){
			  return new String[]{ m_strNetPriceKey + "->" + m_strPriceKey + "*("
				+ m_strDiscountRateKey + "/100.0)*("
				+ m_strAllDiscountRateKey + "/100.0)"};
		  }
          //�����ۿ�
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
          //���㱨��ԭ����˰����
	      else if ( m_strQt_PriceKey.equals(changekey) ){
			  return new String[]{ m_strQt_PriceKey + "->" + m_strQt_NetPriceKey
						+ "/((" + m_strDiscountRateKey + "/100.0) * ("
						+ m_strAllDiscountRateKey + "/100.0))"};
		  }
          //���㱨��ԭ����˰����
	      else if ( m_strQt_NetPriceKey.equals(changekey) ){
	    	  return new String[]{ m_strQt_NetPriceKey + "->" + m_strQt_PriceKey + "*("
	  				+ m_strDiscountRateKey + "/100.0)*("
	  				+ m_strAllDiscountRateKey + "/100.0)"};
		  }
	  }
	  
	  /*//��˰����
	  if ( m_iPrior_Price_TaxPrice ==  RelationsCalVO.PRICE_PRIOR_TO_TAXPRICE){
		  //�������ۿ�
		  if (m_strAllDiscountRateKey.equals("")){
			  //��ǰ�仯ֵΪ��˰���ۻ��ۿ�
			  if ( m_strPriceKey.equals(changekey) || m_strDiscountRateKey.equals(changekey) ){
				  return m_strNetPriceKey + "->" + m_strPriceKey + "*("
							+ m_strDiscountRateKey + "/100.0)";
			  }
			  //��ǰ�仯ֵΪ��˰����
			  else if (m_strNetPriceKey.equals(changekey)){
				  //������
				  if (m_iPrior_ItemDiscountRate_Price == RelationsCalVO.MODIFY_PRICE)
					  return m_strPriceKey + "->" + m_strNetPriceKey + "/("
							+ m_strDiscountRateKey + "/100.0)";
				  //���ۿ�
				  else if (m_iPrior_ItemDiscountRate_Price == RelationsCalVO.MODIFY_ITEMDISCOUNTRATE)
					  return m_strDiscountRateKey + "->"
								+ m_strNetPriceKey + "/" + m_strPriceKey;
			  }
		  }
		  //�������ۿ�
		  else{
			  //��ǰ�仯ֵΪ��˰���ۻ��ۿ�
			  if ( m_strPriceKey.equals(changekey) || m_strDiscountRateKey.equals(changekey) ){
				  return m_strNetPriceKey + "->" + m_strPriceKey + "*("
							+ m_strDiscountRateKey + "/100.0)*("
							+ m_strAllDiscountRateKey + "/100.0)";
			  }
			  //��ǰ�仯ֵΪ��˰����
			  else if (m_strNetPriceKey.equals(changekey)){
				  //������
				  if (m_iPrior_ItemDiscountRate_Price == RelationsCalVO.MODIFY_PRICE)
					  return m_strPriceKey + "->" + m_strNetPriceKey
								+ "/(" + m_strDiscountRateKey + "/100.0) * ("
								+ m_strAllDiscountRateKey + "/100.0)";
				  //���ۿ�
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
   * ���ߣ���ӡ�� ���ܣ����ݿ�˰���˰�ʡ�����˰���� �õ� �����۵ļ��㹫ʽ �������� ���أ�˰��ļ��㹫ʽ ���⣺�� ���ڣ�(2003-10-10
   * 11:22:51) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
   */
  private String getNetPrice_TaxrateNetTaxPrice() {

    if (m_strDiscountTaxTypeName == null)
      return "";

    String realTaxrate = m_strTaxRateKey + "/100.0";
    if (m_strDiscountTaxTypeName.equals("Ӧ˰�ں�")) {
      return m_strNetPriceKey + "->" + m_strNetTaxPriceKey + "*(1.0-"
          + realTaxrate + ")";
    }
    else if (m_strDiscountTaxTypeName.equals("Ӧ˰���")) {
      return m_strNetPriceKey + "->" + m_strNetTaxPriceKey + "/(1.0+"
          + realTaxrate + ")";
    }
    else if (m_strDiscountTaxTypeName.equals("����˰")) {
      return m_strNetPriceKey + "->" + m_strNetTaxPriceKey;
    }

    return null;
  }

  /**
   * ���ߣ���ӡ�� ���ܣ����ݿ�˰���˰�ʡ������� �õ� ����˰���۵ļ��㹫ʽ �������� ���أ�˰��ļ��㹫ʽ ���⣺�� ���ڣ�(2003-10-10
   * 11:22:51) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
   */
  private String getNetTaxPrice_TaxrateNetPrice() {

    if (m_strDiscountTaxTypeName == null)
      return "";

    String realTaxrate = m_strTaxRateKey + "/100.0";
    if (m_strDiscountTaxTypeName.equals("Ӧ˰�ں�")) {
      return m_strNetTaxPriceKey + "->" + m_strNetPriceKey + "/(1.0-"
          + realTaxrate + ")";
    }
    else if (m_strDiscountTaxTypeName.equals("Ӧ˰���")) {
      return m_strNetTaxPriceKey + "->" + m_strNetPriceKey + "*(1.0+"
          + realTaxrate + ")";
    }
    else if (m_strDiscountTaxTypeName.equals("����˰")) {
      return m_strNetTaxPriceKey + "->" + m_strNetPriceKey;
    }

    return null;
  }

  /**
   * ���ߣ���ӡ�� ���ܣ����ݿ�˰���˰�ʡ���˰���� �õ� ���۵ļ��㹫ʽ �������� ���أ�˰��ļ��㹫ʽ ���⣺�� ���ڣ�(2003-10-10
   * 11:22:51) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
   */
  private String getPrice_TaxrateTaxPrice() {

    if (m_strDiscountTaxTypeName == null)
      return "";

    String realTaxrate = m_strTaxRateKey + "/100.0";
    if (m_strDiscountTaxTypeName.equals("Ӧ˰�ں�")) {
      return m_strPriceKey + "->" + m_strTaxPriceKey + "*(1.0-" + realTaxrate
          + ")";
    }
    else if (m_strDiscountTaxTypeName.equals("Ӧ˰���")) {
      return m_strPriceKey + "->" + m_strTaxPriceKey + "/(1.0+" + realTaxrate
          + ")";
    }
    else if (m_strDiscountTaxTypeName.equals("����˰")) {
      return m_strPriceKey + "->" + m_strTaxPriceKey;
    }

    return null;
  }

  /**
   * ���ߣ���ӡ�� ���ܣ��õ����ۡ���˰���������� �������� ���أ����ۡ���˰�����ĸ����� ���⣺�� ���ڣ�(2003-06-09 11:22:51)
   * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
   */
  private int getPrior_Price_TaxPrice() {
    return m_iPrior_Price_TaxPrice;
  }

  /**
   * ���ߣ���ӡ�� ���ܣ����ݿ�˰���˰�ʡ����õ�˰��ļ��㹫ʽ �������� ���أ�˰��ļ��㹫ʽ ���⣺�� ���ڣ�(2001-06-09
   * 11:22:51) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
   */
  private String getTax_TaxrateMoney() {

    if (m_strDiscountTaxTypeName == null)
      return "";

    String realTaxrate = m_strTaxRateKey + "/100.0";
    if (m_strDiscountTaxTypeName.equals("Ӧ˰�ں�")) {
      return m_strTaxKey + "->" + m_strMoneyKey + "*" + realTaxrate + "/(1.0-"
          + realTaxrate + ")";
    }
    else if (m_strDiscountTaxTypeName.equals("Ӧ˰���")) {
      return m_strTaxKey + "->" + m_strMoneyKey + "*" + realTaxrate;
    }
    else if (m_strDiscountTaxTypeName.equals("����˰")) {
      return m_strTaxKey + "->" + m_strTaxKey + "*0.0";
    }

    return null;
  }

  /**
   * ���ߣ���ӡ�� ���ܣ����ݿ�˰���˰�ʡ���˰�ϼƵõ�˰��ļ��㹫ʽ �������� ���أ�˰��ļ��㹫ʽ ���⣺�� ���ڣ�(2001-06-09
   * 11:22:51) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
   */
  private java.lang.String getTax_TaxrateSummny() {
    if (m_strDiscountTaxTypeName == null)
      return "";

    // String realTaxrate = "(" + m_strTaxRate + "/100.0)";
    String realTaxrate = m_strTaxRateKey + "/100.0";
    if (m_strDiscountTaxTypeName.equals("Ӧ˰�ں�")) {
      return m_strTaxKey + "->" + m_strSummnyKey + "*" + realTaxrate;
    }
    else if (m_strDiscountTaxTypeName.equals("Ӧ˰���")) {
      return m_strTaxKey + "->" + m_strSummnyKey + "*" + realTaxrate + "/(1.0+"
          + realTaxrate + ")";
    }
    else if (m_strDiscountTaxTypeName.equals("����˰")) {
      return m_strTaxKey + "->" + m_strTaxKey + "*0.0";
    }

    return null;
  }

  /**
   * ���ߣ���ӡ�� ���ܣ����ݿ�˰���˰�ʡ����� �õ� ��˰���۵ļ��㹫ʽ �������� ���أ�˰��ļ��㹫ʽ ���⣺�� ���ڣ�(2003-10-10
   * 11:22:51) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
   */
  private String getTaxPrice_TaxratePrice() {

    if (m_strDiscountTaxTypeName == null)
      return "";

    String realTaxrate = m_strTaxRateKey + "/100.0";
    if (m_strDiscountTaxTypeName.equals("Ӧ˰�ں�")) {
      return m_strTaxPriceKey + "->" + m_strPriceKey + "/(1.0-" + realTaxrate
          + ")";
    }
    else if (m_strDiscountTaxTypeName.equals("Ӧ˰���")) {
      return m_strTaxPriceKey + "->" + m_strPriceKey + "*(1.0+" + realTaxrate
          + ")";
    }
    else if (m_strDiscountTaxTypeName.equals("����˰")) {
      return m_strTaxPriceKey + "->" + m_strPriceKey;
    }

    return null;
  }

  /**
   * ���ߣ���ӡ�� ���ܣ�����ITEMKEY�õ��õ���ǰ�е�ֵ ֻҪ��ֵ�������Ƿ�Ϊ�㣬��ȡ���ֵ Ϊ�ջ�մ���ʱ������NULL ��������
   * ���أ�UFDouble ����ITEMKEY��Ӧ��ֵ ���⣺�� ���ڣ�(2002-5-15 11:39:21) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
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
	 * ȡ��ͷVO�е�ֵ:����ITEMKEY�õ��õ���ǰ�е�ֵ ֻҪ��ֵ�������Ƿ�Ϊ�㣬��ȡ���ֵ Ϊ�ջ�մ���ʱ������NULL
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
   * ������㣬�򷵻�NULL
   */
  private UFDouble getUFDoubleZeroAsNull(String strKey) {

    if (strKey == null || strKey.equals("")) {
      return null;
    }

    return PuPubVO.getUFDouble_ZeroAsNull(m_voCurr.getAttributeValue(strKey));

  }

  /**
   * ���ߣ���ӡ�� ���ܣ���ʼ��CIRCLE�Ƿ�ִ�еĳ�ʼֵ �������� ���أ��� ���⣺�� ���ڣ�(2002-5-15 11:39:21)
   * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
   */
  private void initCircle() {
    // 19��CIRCLE
    m_bCircleExecuted = new boolean[] {
        false, false, false, false, false, false, false, false, false, false, false,
        false, false, false, false, false, false, false, false
    };
  }

  /**
   * ���ߣ���ӡ�� ���ܣ���ʼ����ʽ���������Ĺ�ϵ �������� ���أ��� ���⣺�� ���ڣ�(2002-5-15 11:39:21)
   * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
   */
  private void initFormulaParseModel() {

    // m_bNullAsZero = m_pnlBill.getBillModel().getFormulaParse().isNullAsZero()
    // ;
    m_bNullAsZero = m_fp.isNullAsZero();

    // FROM V3.0����
    // if (getUFDouble(getFirstChangedKey())==null) {
    // m_pnlBill.getBillModel().getFormulaParse().setNullAsZero(false) ;
    // } else {
    m_fp.setNullAsZero(true);
    // }

  }

  /**
   * ��ʼ������KEY��Ӧ��OBJECT
   * 
   * @param ��
   * @return ��
   * @exception �쳣����
   * @see ��Ҫ�μ�����������
   * @since �������һ���汾���˷�������ӽ���������ѡ��
   */
  private void initKeys(int[] nDescriptions, String[] strKeys) {

    for (int i = 0; i < nDescriptions.length; i++) {
      if (nDescriptions[i] == DISCOUNT_TAX_TYPE_NAME) {// ��˰�����
    	 
    	// Ӧ˰���
    	if (NCLangRes4VoTransl.getNCLangRes().getStrByID("scmpub", "UPPscmpub-000504").equals(strKeys[i]))
    		m_strDiscountTaxTypeName = "Ӧ˰���";
    	else if (NCLangRes4VoTransl.getNCLangRes().getStrByID("scmpub", "UPPscmpub-000505").equals(strKeys[i]))
    		m_strDiscountTaxTypeName = "Ӧ˰�ں�";
    	else if (NCLangRes4VoTransl.getNCLangRes().getStrByID("scmpub", "UPPscmpub-000506").equals(strKeys[i]))
    		m_strDiscountTaxTypeName = "����˰";
    	else
    		m_strDiscountTaxTypeName = "Ӧ˰���";
      }
      else if (nDescriptions[i] == DISCOUNT_TAX_TYPE_KEY) {// ��˰���
        m_strDiscountTaxTypeKey = strKeys[i];
      }
      else if (nDescriptions[i] == NUM) {// ����
        m_strNumKey = strKeys[i];
      }
      else if (nDescriptions[i] == NUM_QUALIFIED) {// �ϸ�����
        m_strQualifiedNumKey = strKeys[i];
      }
      else if (nDescriptions[i] == NUM_UNQUALIFIED) {// ���ϸ�����
        m_strUnQualifiedNumKey = strKeys[i];
      }
      else if (nDescriptions[i] == NUM_ASSIST) {// ������
        m_strAssistNumKey = strKeys[i];
      }
      else if (nDescriptions[i] == NET_PRICE_ORIGINAL) {// ԭ����˰����
        m_strNetPriceKey = strKeys[i];
      }
      else if (nDescriptions[i] == NET_TAXPRICE_ORIGINAL) {// ԭ�Һ�˰����
        m_strNetTaxPriceKey = strKeys[i];
      }
      else if (nDescriptions[i] == PRICE_ORIGINAL) {// ԭ�ҿ��ʵ���
        m_strPriceKey = strKeys[i];
      }
      else if (nDescriptions[i] == TAXRATE) {// ˰��
        m_strTaxRateKey = strKeys[i];
      }
      else if (nDescriptions[i] == TAX_ORIGINAL) {// ԭ��˰��
        m_strTaxKey = strKeys[i];
      }
      else if (nDescriptions[i] == MONEY_ORIGINAL) {// ��˰���
        m_strMoneyKey = strKeys[i];
      }
      else if (nDescriptions[i] == SUMMNY_ORIGINAL) {// ԭ�Ҽ�˰�ϼ�
        m_strSummnyKey = strKeys[i];
      }
      else if (nDescriptions[i] == HEAD_SUMMNY_ORIGINAL) {// ����ԭ�Ҽ�˰�ϼ�
    	  m_strheadstrSummnyKey = strKeys[i];
        }
      else if (nDescriptions[i] == DISCOUNT_RATE) {// ����
        m_strDiscountRateKey = strKeys[i];
      }
      else if (nDescriptions[i] == ALLDISCOUNT_RATE) {// �����ۿ�
    	  m_strAllDiscountRateKey = strKeys[i];
        }
      else if (nDescriptions[i] == CONVERT_RATE) {// ������
        m_strConvertRateKey = strKeys[i];
      }
      else if (nDescriptions[i] == IS_FIXED_CONVERT) {// �Ƿ�̶�������
    	Object fixedflag = m_voCurr.getAttributeValue(strKeys[i]);
        if (strKeys[i].equals("Y") || new UFBoolean(fixedflag == null ? "N" : fixedflag.toString()).booleanValue() )
          m_bFixedConvertRateKey = true;
        else if (strKeys[i].equals("N") || new UFBoolean(fixedflag == null ? "N" : fixedflag.toString()).booleanValue())
          m_bFixedConvertRateKey = false;
      }
      else if (nDescriptions[i] == QT_CONVERT_RATE) {// ���ۻ�����
          m_strQt_ConvertRateKey = strKeys[i];
      }
      else if (nDescriptions[i] == QT_IS_FIXED_CONVERT) {// �����Ƿ�̶�������
    	  Object fixedflag = m_voCurr.getAttributeValue(strKeys[i]);
          if (strKeys[i].equals("Y") || new UFBoolean(fixedflag == null ? "N" : fixedflag.toString()).booleanValue())
            m_bQt_FixedConvertRateKey = true;
          else if (strKeys[i].equals("N") || new UFBoolean(fixedflag == null ? "N" : fixedflag.toString()).booleanValue())
            m_bQt_FixedConvertRateKey = false;
      }
      else if (nDescriptions[i] == TAXPRICE_ORIGINAL) {// ��˰����
        m_strTaxPriceKey = strKeys[i];
      }
      else if (nDescriptions[i] == DISCOUNTMNY_ORIGINA) {// ԭ���ۿ۶�
    	  m_strDiscountMnyKey = strKeys[i];
      }
      else if (nDescriptions[i] == QT_NUM) {// ԭ�ұ��۵�λ����
    	  m_strQt_NumKey = strKeys[i];
      }
      else if (nDescriptions[i] == QT_TAXPRICE_ORIGINAL) {// ԭ�ұ��۵�λ��˰����
    	  m_strQt_TaxPriceKey = strKeys[i];
      }
      else if (nDescriptions[i] == QT_PRICE_ORIGINAL) {// ԭ�ұ��۵�λ��˰����
    	  m_strQt_PriceKey = strKeys[i];
      }
      else if (nDescriptions[i] == QT_NET_TAXPRICE_ORIGINAL) {// ԭ�ұ��۵�λ��˰����
    	  m_strQt_NetTaxPriceKey = strKeys[i];
      }
      else if (nDescriptions[i] == QT_NET_PRICE_ORIGINAL) {// ԭ�ұ��۵�λ��˰����
    	  m_strQt_NetPriceKey = strKeys[i];
      }
      else if (nDescriptions[i] == PRICE_LOCAL) {// ������˰����
    	  m_strPrice_LocalKey = strKeys[i];
      }
      else if (nDescriptions[i] == TAXPRICE_LOCAL) {// ���Һ�˰���� 
    	  m_strTaxPrice_LocalKey = strKeys[i];
      }
      else if (nDescriptions[i] == NET_PRICE_LOCAL) {// ������˰����
    	  m_strNet_Price_LocalKey = strKeys[i];
      }
      else if (nDescriptions[i] == NET_TAXPRICE_LOCAL) {// ���Һ�˰����
    	  m_strNet_TaxPrice_LocalKey = strKeys[i];
      }
      else if (nDescriptions[i] == TAX_LOCAL) {// ����˰��
    	  m_strTax_LocalKey = strKeys[i];
      }
      else if (nDescriptions[i] == MONEY_LOCAL) {// ������˰���
    	  m_strMoney_LocalKey = strKeys[i];
      }
      else if (nDescriptions[i] == SUMMNY_LOCAL) {// ���Ҽ�˰�ϼ�
    	  m_strSummny_LocalKey = strKeys[i];
      }
      else if (nDescriptions[i] == DISCOUNTMNY_LOCAL) {//�����ۿ۶�
    	  m_strDiscountMny_LocalKey = strKeys[i];
      }
      else if (nDescriptions[i] == QT_PRICE_LOCAL) {// ���۵�λ������˰����
    	  m_strQt_Price_LocalKey = strKeys[i];
      }
      else if (nDescriptions[i] == QT_TAXPRICE_LOCAL) {// ���۵�λ���Һ�˰����
    	  m_strQt_TaxPrice_LocalKey = strKeys[i];
      }
      else if (nDescriptions[i] == QT_NET_PRICE_LOCAL) {// ���۵�λ������˰����
    	  m_strQt_Net_Price_LocalKey = strKeys[i];
      }
      else if (nDescriptions[i] == QT_NET_TAXPRICE_LOCAL) {//���۵�λ���Һ�˰����
    	  m_strQt_Net_TaxPrice_LocalKey = strKeys[i];
      }
      else if (nDescriptions[i] == PRICE_FRACTIONAL) {// ������˰����
    	  m_strPrice_FractionalKey = strKeys[i];
      }
      else if (nDescriptions[i] == TAXPRICE_FRACTIONAL) {// ���Һ�˰���� 
    	  m_strTaxPrice_FractionalKey = strKeys[i];
      }
      else if (nDescriptions[i] == NET_PRICE_FRACTIONAL) {// ������˰����
    	  m_strNet_Price_FractionalKey = strKeys[i];
      }
      else if (nDescriptions[i] == NET_TAXPRICE_FRACTIONAL) {// ���Һ�˰����
    	  m_strNet_TaxPrice_FractionalKey = strKeys[i];
      }
      else if (nDescriptions[i] == TAX_FRACTIONAL) {// ����˰��
    	  m_strTax_FractionalKey = strKeys[i];
      }
      else if (nDescriptions[i] == MONEY_FRACTIONAL) {// ������˰���
    	  m_strMoney_FractionalKey = strKeys[i];
      }
      else if (nDescriptions[i] == SUMMNY_FRACTIONAL) {// ���Ҽ�˰�ϼ�
    	  m_strSummny_FractionalKey = strKeys[i];
      }
      else if (nDescriptions[i] == DISCOUNTMNY_FRACTIONAL) {//�����ۿ۶�
    	  m_strDiscountMny_FractionalKey = strKeys[i];
      }
      else if (nDescriptions[i] == ASSIST_PRICE_ORIGINAL) {// ��������˰����
    	  m_strAssist_Price = strKeys[i];
      }
      else if (nDescriptions[i] == ASSIST_TAXPRICE_ORIGINAL) {//��������˰����
    	  m_strAssist_TaxPrice = strKeys[i];
      }
      else if (nDescriptions[i] == ASK_TAXPRICE) {// ѯ��ԭ�Һ�˰����
    	  m_strAsk_TaxPriceKey = strKeys[i];
      }
      else if (nDescriptions[i] == ASK_PRICE) {//ѯ��ԭ����˰����
    	  m_strAsk_PriceKey = strKeys[i];
      }
      else if (nDescriptions[i] == CURRTYPEPk) {// ԭ��PK
    	  m_strCurrTypePkKey = strKeys[i];
      }
      else if (nDescriptions[i] == PK_CORP) {// PK_CORP
    	  m_strPk_CorpKey = strKeys[i];
      }
      else if (nDescriptions[i] == EXCHANGE_O_TO_BRATE) {// �۱�����
    	  m_strExchange_O_TO_BrateKey = strKeys[i];
      }
      else if (nDescriptions[i] == EXCHANGE_O_TO_ARATE) {//�۸�����
    	  m_strExchange_O_TO_ArateKey = strKeys[i];
      }
      else if (nDescriptions[i] == BILLDATE) {//��������
    	  m_strBillDateKey = strKeys[i];
      }
      else if (nDescriptions[i] == UNITID) {//��������λID
    	  m_strUnitID = strKeys[i];
      }
      else if (nDescriptions[i] == QUOTEUNITID) {//���ۼ�����λID
    	  m_strQuoteUnitID = strKeys[i];
      }
      
      if (debug)
        //�����ڲ���ֵ�����ڵ���
        SCMEnv.out(getChineseName(strKeys[i])+"["+strKeys[i]+"]��"+getObject(strKeys[i]));
      
    }
  }

  /**
   * ���ߣ���ӡ�� ���ܣ�������ֵ�������ع� �������� ���أ����ۡ���˰�����ĸ����� ���⣺�� ���ڣ�(2002-06-09 11:22:51)
   * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
   */
  private void initObjects() {

    // ����
    // if(m_pnlBill.getBodyItem(m_strNumKey) != null ){
    m_objOldNum = m_voCurr.getAttributeValue(m_strNumKey);
    if (getFirstChangedKey().equals(m_strNumKey)) {
      m_objOldNum = m_voCurr.getAttributeValue(getFirstChangedKey());
    }
    // }
    // ������
    // if(m_pnlBill.getBodyItem(m_strNetPriceKey) != null ){
    m_objOldNetPrice = m_voCurr.getAttributeValue(m_strNetPriceKey);
    if (getFirstChangedKey().equals(m_strNetPriceKey)) {
      m_objOldNetPrice = m_voCurr.getAttributeValue(getFirstChangedKey());
    }
    // }
    // ���
    // if(m_pnlBill.getBodyItem(m_strMoneyKey) != null ){
    m_objOldMoney = m_voCurr.getAttributeValue(m_strMoneyKey);
    if (getFirstChangedKey().equals(m_strMoneyKey)) {
      m_objOldMoney = m_voCurr.getAttributeValue(getFirstChangedKey());
    }
    // }
    // ��˰������
    // if(m_pnlBill.getBodyItem(m_strNetTaxPriceKey) != null ){
    m_objOldNetTaxPrice = m_voCurr.getAttributeValue(m_strNetTaxPriceKey);
    if (getFirstChangedKey().equals(m_strNetTaxPriceKey)) {
      m_objOldNetTaxPrice = m_voCurr.getAttributeValue(getFirstChangedKey());
    }
    // }
    // ˰��
    // if(m_pnlBill.getBodyItem(m_strTaxRateKey) != null ){
    m_objOldTaxrate = m_voCurr.getAttributeValue(m_strTaxRateKey);
    if (getFirstChangedKey().equals(m_strTaxRateKey)) {
      m_objOldTaxrate = m_voCurr.getAttributeValue(getFirstChangedKey());
    }
    // }
    // ˰��
    // if(m_pnlBill.getBodyItem(m_strTaxKey) != null ){
    m_objOldTax = m_voCurr.getAttributeValue(m_strTaxKey);
    if (getFirstChangedKey().equals(m_strTaxKey)) {
      m_objOldTax = m_voCurr.getAttributeValue(getFirstChangedKey());
    }
    // }
    // ��˰�ϼ�
    // if(m_pnlBill.getBodyItem(m_strSummnyKey) != null ){
    m_objOldSummny = m_voCurr.getAttributeValue(m_strSummnyKey);
    if (getFirstChangedKey().equals(m_strSummnyKey)) {
      m_objOldSummny = m_voCurr.getAttributeValue(getFirstChangedKey());
    }
    // }
    // ����
    // if(m_pnlBill.getBodyItem(m_strDiscountRateKey) != null ){
    m_objOldDiscountRate = m_voCurr.getAttributeValue(m_strDiscountRateKey);
    if (getFirstChangedKey().equals(m_strDiscountRateKey)) {
      m_objOldDiscountRate = m_voCurr.getAttributeValue(getFirstChangedKey());
    }
    // }
    // ����
    // if(m_pnlBill.getBodyItem(m_strPriceKey) != null ){
    m_objOldPrice = m_voCurr.getAttributeValue(m_strPriceKey);
    if (getFirstChangedKey().equals(m_strPriceKey)) {
      m_objOldPrice = m_voCurr.getAttributeValue(getFirstChangedKey());
    }
    // }
    // �ϸ�����
    // if(m_pnlBill.getBodyItem(m_strQualifiedNumKey) != null ){
    m_objOldQualifiedNum = m_voCurr.getAttributeValue(m_strQualifiedNumKey);
    if (getFirstChangedKey().equals(m_strQualifiedNumKey)) {
      m_objOldQualifiedNum = m_voCurr.getAttributeValue(getFirstChangedKey());
    }
    // }
    // ���ϸ�����
    // if(m_pnlBill.getBodyItem(m_strUnQualifiedNumKey) != null ){
    m_objOldUnQualifiedNum = m_voCurr.getAttributeValue(m_strUnQualifiedNumKey);
    if (getFirstChangedKey().equals(m_strUnQualifiedNumKey)) {
      m_objOldUnQualifiedNum = m_voCurr.getAttributeValue(getFirstChangedKey());
    }
    // }
    // ������
    // if(m_pnlBill.getBodyItem(m_strAssistNumKey) != null ){
    m_objOldAssistNum = m_voCurr.getAttributeValue(m_strAssistNumKey);
    if (getFirstChangedKey().equals(m_strAssistNumKey)) {
      m_objOldAssistNum = m_voCurr.getAttributeValue(getFirstChangedKey());
    }
    // }
    // ������
    // if(m_pnlBill.getBodyItem(m_strConvertRateKey) != null ){
    m_objOldConvertRate = m_voCurr.getAttributeValue(m_strConvertRateKey);
    if (getFirstChangedKey().equals(m_strConvertRateKey)) {
      m_objOldConvertRate = m_voCurr.getAttributeValue(getFirstChangedKey());
    }
    // }
    // ��˰����
    // if(m_pnlBill.getBodyItem(m_strTaxPriceKey) != null ){
    m_objOldTaxPrice = m_voCurr.getAttributeValue(m_strTaxPriceKey);
    if (getFirstChangedKey().equals(m_strTaxPriceKey)) {
      m_objOldTaxPrice = m_voCurr.getAttributeValue(getFirstChangedKey());
    }
    // }
  }
  
  /**
   * 1.Ĭ�ϲ����б���\���ҵļ���;
   * 2.���û��ԭ��PK��PK_Corp���۱�/�۸����ʡ��������ڣ������б���\���ҵļ���;
   * 3.���ԭ�Ҽ�˰�ϼơ���˰����Ϊ0��NULLʱ�������б���\���ҵļ���;
   * @return �Ƿ���Խ��б���\���ҵļ���
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
    * @return �ж�����ֶ��Ƿ����
    */
    private boolean isCalFieldExist(String[] keys){
 	  for (String key : keys ){
 		 if (key.equals(""))
  			return false;  
 	  }	  
 	  return true;
   } 
   
  /**
   * ���ߣ���ӡ�� ���ܣ����ݸı����ж�һ��CIRCLE�Ƿ��ִ�� ������int circle CIRCLEֵ String changedKey
   * ��CIRCLE�Ľ����� ���أ�boolean true��ִ��;false����ִ�� ���⣺�� ���ڣ�(2001-05-27 11:39:21)
   * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
   */
  private boolean isCircleExecutable(int circle, String sChangedKey) {

    if (circle == CIRCLE_NUM_NETPRICE_MONEY) {// ����,������,���
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
    else if (circle == CIRCLE_TAXRATE) {// ˰��,���,˰��,��˰�ϼ�
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
    else if (circle == CIRCLE_NUM_NETTAXPRICE_SUMMNY) {// ����,����˰����,��˰�ϼ�
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
    else if (circle == CIRCLE_PRICE_NETPRICE) {// ����,��˰����,��˰����
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
    else if (circle == CIRCLE_QUALIFIED_NUM) {// ����,�ϸ�����,���ϸ�����
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
    else if (circle == CIRCLE_CONVERT_RATE) {// ������,������,������
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
    else if (circle == CIRCLE_QT_CONVERT_RATE) {// ������,��������,���ۻ�����
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
    else if (circle == CIRCLE_TAXPRICE_NETTAXPRICE) {// ����,��˰����,��˰����
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
    else if (circle == CIRCLE_TAXPRICE_PRICE) {// ˰��,��˰����,����
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
    else if (circle == CIRCLE_NETTAXPRICE_NETPRICE) {// ˰��,����˰����,������
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
    else if (circle == CIRCLE_DISCOUNTMNY) {// ��˰���ۡ���������˰�ϼơ��ۿ۶�
        if (m_strTaxPriceKey.equals("") || m_strNumKey.equals("")
            || m_strSummnyKey.equals("") || m_strDiscountMnyKey.equals("") ) {
          return false;
        }
        if (sChangedKey.equals(m_strTaxKey)) {//��ǰ�༭˰��ʱ���������ۿ۶�
          return false;
        }
        else{
        	if ( getUFDouble(m_strTaxPriceKey) == null || getUFDouble(m_strNumKey) == null
        			|| getUFDouble(m_strSummnyKey) == null) 
        	   return false;
        }
        return true;	
    }
    else if (circle == CIRCLE_VIAPRICE) {// ��������˰���ۡ���������˰���ۡ�����������˰�ϼơ���˰��˰�ʡ��ۿ۶�
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
    else if (circle == CIRCLE_QT_NETTAXPRICE_NETPRICE) {//˰�ʡ�����ԭ�Һ�˰���ۡ�����ԭ����˰����
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
    else if (circle == CIRCLE_QT_TAXPRICE_PRICE) {//˰�ʡ�����ԭ�Һ�˰���ۡ�����ԭ����˰����
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
    else if (circle == CIRCLE_QT_TAXPRICE_NETTAXPRICE) {// ���ʡ�����ԭ�Һ�˰���ۡ�����ԭ�Һ�˰����
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
    else if (circle == CIRCLE_QT_PRICE_NETPRICE) {// ���ʡ�����ԭ����˰���ۡ�����ԭ����˰����
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
    else if (circle == CIRCLE_QT_NUM_NETTAXPRICE_SUMMNY) {// ��������������ԭ�Һ�˰���ۡ���˰�ϼ�
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
    else if (circle == CIRCLE_QT_NUM_NETPRICE_MONEY) {// ��������������ԭ����˰���ۡ���˰���
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
    else if (circle == CIRCLE_LOCAL_SUMMNY_MNY_TAX) {// ���Ҽ�˰�ϼơ�������˰��˰��
    	if (m_strSummny_LocalKey.equals("") || m_strMoney_LocalKey.equals("")
                || m_strTax_LocalKey.equals("") ) {
              return false;
    	}else
    		return true;
    }

    return false;
  }

  /**
   * ���ߣ���ӡ�� ���ܣ��ж�ĳCIRCLE�Ƿ���ִ�� �������� ���أ�boolean true��ִ��;falseδִ�� ���⣺��
   * ���ڣ�(2001-05-27 11:39:21) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
   */
  private boolean isCircleExecuted(int circle) {

    return m_bCircleExecuted[circle - 1];
  }

  /**
   * ���ߣ���ӡ�� ���ܣ��Ƿ�̶������� ������panel ���أ� ���⣺�� ���ڣ�(2001-05-27 11:39:21)
   * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
   */
  private boolean isFixedConvertRate() {

    return m_bFixedConvertRateKey;
  }
  
  private boolean isQt_FixedConvertRate() {

	    return m_bQt_FixedConvertRateKey;
  }

  /**
   * ���ߣ���ӡ�� ���ܣ��ж�һ��KEY�Ƿ����ı����������ı䣬�򲻿��ٸı� �������� ���أ�boolean true���ɸı䣻false�����ɸı�
   * ���⣺�� ���ڣ�(2003-6-10 11:39:21) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
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
   * ���ߣ���ӡ�� ���ܣ�������Ϻ��ж��Ƿ����е������ ������ ���أ� ���⣺�� ���ڣ�(2003-05-27 11:39:21)
   * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
   */
  private void isResultsRight() {
    m_strShowHintMsg = null;
    // �����۲���Ϊ��
    // if(m_pnlBill.getBodyItem(m_strNetPriceKey) != null ){
    if (getUFDouble(m_strNetPriceKey) != null
        && getUFDouble(m_strNetPriceKey).doubleValue() < 0.0) {
      // MessageDialog.showHintDlg(m_pnlBill,nc.ui.ml.NCLangRes.getInstance().getStrByID("scmcommon","UPPSCMCommon-000270")/*@res
      // "��ʾ"*/,m_pnlBill.getBodyItem(m_strNetPriceKey).getName()+nc.ui.ml.NCLangRes.getInstance().getStrByID("scmpub","UPPscmpub-000507")/*@res
      // "����Ϊ�������������롣"*/) ;
      m_strShowHintMsg = m_strNetPriceName
          + NCLangRes4VoTransl.getNCLangRes().getStrByID("scmpub",
              "UPPscmpub-000507")/* @res "����Ϊ�������������롣" */;
      // return false ;
    }
    // }
    // ��˰�����۲�Ϊ��
    // if(m_pnlBill.getBodyItem(m_strNetTaxPriceKey) != null ){
    if (getUFDouble(m_strNetTaxPriceKey) != null
        && getUFDouble(m_strNetTaxPriceKey).doubleValue() < 0.0) {
      // MessageDialog.showHintDlg(m_pnlBill,nc.ui.ml.NCLangRes.getInstance().getStrByID("scmcommon","UPPSCMCommon-000270")/*@res
      // "��ʾ"*/,m_pnlBill.getBodyItem(m_strNetTaxPriceKey).getName()+nc.ui.ml.NCLangRes.getInstance().getStrByID("scmpub","UPPscmpub-000507")/*@res
      // "����Ϊ�������������롣"*/) ;
      // return false ;
      m_strShowHintMsg = m_strNetTaxPriceName
          + NCLangRes4VoTransl.getNCLangRes().getStrByID("scmpub",
              "UPPscmpub-000507")/* @res "����Ϊ�������������롣" */;
    }
    // }
    // ���۲�Ϊ��
    // if(m_pnlBill.getBodyItem(m_strPriceKey) != null ){
    if (getUFDouble(m_strPriceKey) != null
        && getUFDouble(m_strPriceKey).doubleValue() < 0.0) {
      // MessageDialog.showHintDlg(m_pnlBill,nc.ui.ml.NCLangRes.getInstance().getStrByID("scmcommon","UPPSCMCommon-000270")/*@res
      // "��ʾ"*/,m_pnlBill.getBodyItem(m_strPriceKey).getName()+nc.ui.ml.NCLangRes.getInstance().getStrByID("scmpub","UPPscmpub-000507")/*@res
      // "����Ϊ�������������롣"*/) ;
      // return false ;
      m_strShowHintMsg = m_strPriceName
          + NCLangRes4VoTransl.getNCLangRes().getStrByID("scmpub",
              "UPPscmpub-000507")/* @res "����Ϊ�������������롣" */;
    }
    // }
    // ˰�ʲ�Ϊ��
    // if(m_pnlBill.getBodyItem(m_strTaxRateKey) != null ){
    if (getUFDouble(m_strTaxRateKey) != null
        && getUFDouble(m_strTaxRateKey).doubleValue() < 0.0) {
      // MessageDialog.showHintDlg(m_pnlBill,nc.ui.ml.NCLangRes.getInstance().getStrByID("scmcommon","UPPSCMCommon-000270")/*@res
      // "��ʾ"*/,m_pnlBill.getBodyItem(m_strTaxRateKey).getName()+nc.ui.ml.NCLangRes.getInstance().getStrByID("scmpub","UPPscmpub-000507")/*@res
      // "����Ϊ�������������롣"*/) ;
      // return false ;
      m_strShowHintMsg = m_strTaxRateName
          + NCLangRes4VoTransl.getNCLangRes().getStrByID("scmpub",
              "UPPscmpub-000507")/* @res "����Ϊ�������������롣" */;
    }
    // }
    // ���ʲ�Ϊ��,(0,100)
    // if(m_pnlBill.getBodyItem(m_strDiscountRateKey) != null ){
    if (getUFDouble(m_strDiscountRateKey) != null) {
      if (getUFDouble(m_strDiscountRateKey).doubleValue() < 0.0) {
        // MessageDialog.showHintDlg(m_pnlBill,nc.ui.ml.NCLangRes.getInstance().getStrByID("scmcommon","UPPSCMCommon-000270")/*@res
        // "��ʾ"*/,nc.ui.ml.NCLangRes.getInstance().getStrByID("scmpub","UPPscmpub-000508")/*@res
        // "���ʲ���Ϊ�������������롣"*/) ;
        // return false ;
        m_strShowHintMsg = m_strDiscountRateName
            + NCLangRes4VoTransl.getNCLangRes().getStrByID("scmpub",
                "UPPscmpub-000507")/* @res "����Ϊ�������������롣" */;
      }
      // else if( getUFDouble(m_strDiscountRate).doubleValue()>100.0 ){
      // MessageDialog.showHintDlg(m_pnlBill,"��ʾ","���ʲ��ܴ���100.0�����������롣") ;
      // return false ;
      // }
    }
    // }
    // �����ʲ�Ϊ��
    // if(m_pnlBill.getBodyItem(m_strConvertRateKey) != null ){
    if (getUFDouble(m_strConvertRateKey) != null
        && getUFDouble(m_strConvertRateKey).doubleValue() < 0.0) {
      // MessageDialog.showHintDlg(m_pnlBill,nc.ui.ml.NCLangRes.getInstance().getStrByID("scmcommon","UPPSCMCommon-000270")/*@res
      // "��ʾ"*/,m_pnlBill.getBodyItem(m_strConvertRateKey).getName()+nc.ui.ml.NCLangRes.getInstance().getStrByID("scmpub","UPPscmpub-000507")/*@res
      // "����Ϊ�������������롣"*/) ;
      // return false ;
      m_strShowHintMsg = m_strConvertRateName
          + NCLangRes4VoTransl.getNCLangRes().getStrByID("scmpub",
              "UPPscmpub-000507")/* @res "����Ϊ�������������롣" */;
    }
    // }
    // ��˰���۲�Ϊ��
    // if(m_pnlBill.getBodyItem(m_strTaxPriceKey) != null ){
    if (getUFDouble(m_strTaxPriceKey) != null
        && getUFDouble(m_strTaxPriceKey).doubleValue() < 0.0) {
      // MessageDialog.showHintDlg(m_pnlBill,nc.ui.ml.NCLangRes.getInstance().getStrByID("scmcommon","UPPSCMCommon-000270")/*@res
      // "��ʾ"*/,m_pnlBill.getBodyItem(m_strTaxPriceKey).getName()+nc.ui.ml.NCLangRes.getInstance().getStrByID("scmpub","UPPscmpub-000507")/*@res
      // "����Ϊ�������������롣"*/) ;
      // return false ;
      m_strShowHintMsg = m_strTaxPriceName
          + NCLangRes4VoTransl.getNCLangRes().getStrByID("scmpub",
              "UPPscmpub-000507")/* @res "����Ϊ�������������롣" */;
    }
    // }

    // return true ;
  }

  /**
   * ���ߣ���ӡ�� ���ܣ�������ɺ��������� ������ ���أ� ���⣺�� ���ڣ�(2001-05-27 11:39:21)
   * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
   */

  private void processAferCalReSetZeroAndValue() {
    UFDouble dValue = null;
    // ����
    // if(m_pnlBill.getBodyItem(m_strNumKey) != null ){
    dValue = getUFDoubleZeroAsNull(m_strNumKey);
    // if( dValue!=null && dValue.compareTo(VariableConst.ZERO)==0 ){
    // m_pnlBill.setBodyValueAt(null,m_iPos,m_strNum) ;
    // }else{
    m_voCurr.setAttributeValue(m_strNumKey, dValue);
    // }
    // }
    // ����
    // if(m_pnlBill.getBodyItem(m_strNetPriceKey) != null ){
    dValue = getUFDoubleZeroAsNull(m_strNetPriceKey);

    // SEG����Ҫ���0.00����������������
    // if( dValue!=null && dValue.compareTo(VariableConst.ZERO)==0 ){
    // m_pnlBill.setBodyValueAt(null,m_iPos,m_strNetPrice) ;
    // }
    // else{
    m_voCurr.setAttributeValue(m_strNetPriceKey, dValue);
    // }
    // }

    // ���
    // if(m_pnlBill.getBodyItem(m_strMoneyKey) != null ){
    dValue = getUFDoubleZeroAsNull(m_strMoneyKey);
    // if( dValue!=null) && dValue.compareTo(VariableConst.ZERO)==0 ){
    // m_pnlBill.setBodyValueAt(null,m_iPos,m_strMoney) ;
    // }
    // else{
    m_voCurr.setAttributeValue(m_strMoneyKey, dValue);
    // }
    // }

    // ��˰����
    // if(m_pnlBill.getBodyItem(m_strNetTaxPriceKey) != null ){
    dValue = getUFDoubleZeroAsNull(m_strNetTaxPriceKey);
    // if( dValue!=null && dValue.compareTo(VariableConst.ZERO)==0 ){
    // m_pnlBill.setBodyValueAt(null,m_iPos,m_strNetTaxPrice) ;
    // }
    // else{
    m_voCurr.setAttributeValue(m_strNetTaxPriceKey, dValue);
    // }
    // }

    // ˰��
    // if(m_pnlBill.getBodyItem(m_strTaxRateKey) != null ){
    dValue = getUFDoubleZeroAsNull(m_strTaxRateKey);
    // if( dValue!=null && dValue.compareTo(VariableConst.ZERO)==0 ){
    // m_pnlBill.setBodyValueAt(null,m_iPos,m_strTaxRate) ;
    // }else{
    m_voCurr.setAttributeValue(m_strTaxRateKey, dValue);
    // }
    // }

    // ˰��
    // if(m_pnlBill.getBodyItem(m_strTaxKey) != null ){
    dValue = getUFDoubleZeroAsNull(m_strTaxKey);
    // if( dValue!=null && dValue.compareTo(VariableConst.ZERO)==0 ){
    // m_pnlBill.setBodyValueAt(null,m_iPos,m_strTax) ;
    // }else{
    m_voCurr.setAttributeValue(m_strTaxKey, dValue);
    // }
    // }

    // ��˰�ϼ�
    // if(m_pnlBill.getBodyItem(m_strSummnyKey) != null ){
    dValue = getUFDoubleZeroAsNull(m_strSummnyKey);
    // if( dValue!=null && dValue.compareTo(VariableConst.ZERO)==0 ){
    // m_pnlBill.setBodyValueAt(null,m_iPos,m_strSummny) ;
    // }
    // else{
    m_voCurr.setAttributeValue(m_strSummnyKey, dValue);
    // }
    // }

    // ����
    // if(m_pnlBill.getBodyItem(m_strDiscountRateKey) != null ){
    dValue = getUFDoubleZeroAsNull(m_strDiscountRateKey);
    // if( dValue!=null && dValue.compareTo(VariableConst.ZERO)==0 ){
    // m_pnlBill.setBodyValueAt(null,m_iPos,m_strDiscountRate) ;
    // }else{
    m_voCurr.setAttributeValue(m_strDiscountRateKey, dValue);
    // }
    // }

    // ���ʵ���
    // if(m_pnlBill.getBodyItem(m_strPriceKey) != null ){
    dValue = getUFDoubleZeroAsNull(m_strPriceKey);
    // if( dValue!=null && dValue.compareTo(VariableConst.ZERO)==0 ){
    // m_pnlBill.setBodyValueAt(null,m_iPos,m_strPrice) ;
    // }else{
    m_voCurr.setAttributeValue(m_strPriceKey, dValue);
    // }
    // }
    // �ϸ�����
    // if(m_pnlBill.getBodyItem(m_strQualifiedNumKey) != null ){
    dValue = getUFDoubleZeroAsNull(m_strQualifiedNumKey);
    // if( dValue!=null && dValue.compareTo(VariableConst.ZERO)==0 ){
    // m_pnlBill.setBodyValueAt(null,m_iPos,m_strQualifiedNum) ;
    // }else{
    m_voCurr.setAttributeValue(m_strQualifiedNumKey, dValue);
    // }
    // }
    // ���ϸ�����
    // if(m_pnlBill.getBodyItem(m_strUnQualifiedNumKey) != null ){
    dValue = getUFDoubleZeroAsNull(m_strUnQualifiedNumKey);
    // if( dValue!=null && dValue.compareTo(VariableConst.ZERO)==0 ){
    // m_voCurr.setAttributeValue(m_strUnQualifiedNumKey,null) ;
    // }else{
    m_voCurr.setAttributeValue(m_strUnQualifiedNumKey, dValue);
    // }
    // }
    // ������
    // if(m_pnlBill.getBodyItem(m_strAssistNumKey) != null ){
    dValue = getUFDoubleZeroAsNull(m_strAssistNumKey);
    // if( dValue!=null && dValue.compareTo(VariableConst.ZERO)==0 ){
    // m_voCurr.setAttributeValue(m_strAssistNumKey,null) ;
    // }else{
    m_voCurr.setAttributeValue(m_strAssistNumKey, dValue);
    // }
    // }
    // ������
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
   * ���ߣ���ӡ�� ���ܣ������������ ������ ���أ� ���⣺�� ���ڣ�(2001-05-27 11:39:21) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
   */
  private void processAfterCal() {

    // �ϸ��벻�ϸ�����
    processAfterCalQualifiedNum();
    
    // ���Ϊ�㣬����ʾΪ��,ͬʱ����С��λ����ʱȥ����
    //processAferCalReSetZeroAndValue();
    
    // ���ûع�ʽ����Ĳ���
    processAfterCalFormulaParseModel();

  }

  /**
   * �����������
   * 
   * @param int
   *          circle CIRCLEֵ boolean executedFlag ��CIRCLE��ִ�б�־
   * @return ��
   * @exception �쳣����
   * @see ��Ҫ�μ�����������
   * @since �������һ���汾���˷�������ӽ���������ѡ��
   */
  private void processAfterCalFormulaParseModel() {
    // m_pnlBill.getBillModel().getFormulaParse().setNullAsZero(m_bNullAsZero) ;
    m_fp.setNullAsZero(m_bNullAsZero);

  }
  
  
  /**
   * ������ۡ�������ֵ��ͬ�����ۿ۶�ǿ����0
   * false: �ۿ۶���0������Ҫ���м��㣺�ۿ۶� = ���� * ��˰���� - ��˰�ϼ�
   * true:  ��Ҫ���м��㣺�ۿ۶� = ���� * ��˰���� - ��˰�ϼ�
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
   * �����������
   * 
   * @param int
   *          circle CIRCLEֵ boolean executedFlag ��CIRCLE��ִ�б�־
   * @return ��
   * @exception �쳣����
   * @see ��Ҫ�μ�����������
   * @since �������һ���汾���˷�������ӽ���������ѡ��
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
   * ����һ��CIRCLE��ִ�б�־
   * 
   * @param int
   *          circle CIRCLEֵ boolean executedFlag ��CIRCLE��ִ�б�־
   * @return ��
   * @exception �쳣����
   * @see ��Ҫ�μ�����������
   * @since �������һ���汾���˷�������ӽ���������ѡ��
   */
  private void setCircleExecuted(int circle, boolean executedFlag) {
    m_bCircleExecuted[circle - 1] = executedFlag;
  }

  /**
   * @param iaPrior - Ϊ�������ȱ䶯key�������ú�˰���Ȼ��� 
   * ���ߣ���ӡ�� ���ܣ���������ı��KEY �������� ���أ��� ���⣺�� ���ڣ�(2002-05-27 11:39:21)
   * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
   * @throws BusinessException 
   */
  private void setFirstChangedKey(String sNewKey,int[] iaPrior) {
	  try {
	  
	//���ú�˰���Ȼ���
	setPriorForSetFirstChangeKey(iaPrior);
	  
    // ����ı��KEYΪ��˰�������Ϊ��˰�ʸı䣬�����仯
    if (sNewKey.equals(getKeyByDescription(DISCOUNT_TAX_TYPE_KEY))) {
      // ��˰��Ϊ�仯KEY
      m_strChangedKey = getKeyByDescription(TAXRATE);
    }
    //�۱����ʡ�ԭ�ұ��ֱ仯������Ϊ���仯
    else if (sNewKey.equals(getKeyByDescription(EXCHANGE_O_TO_BRATE))
    		||sNewKey.equals(getKeyByDescription(CURRTYPEPk))){
    	
    	//���ֱ仯ȡĬ�ϻ��ʣ���������VO��
    	if (sNewKey.equals(getKeyByDescription(CURRTYPEPk))){
    		
			// ԭ��PK
			if (m_voCurr.getAttributeValue(m_strCurrTypePkKey)==null && m_hvoCurr != null)
				pk_CurrType = m_hvoCurr.getAttributeValue(m_strCurrTypePkKey).toString();
			else
				pk_CurrType = m_voCurr.getAttributeValue(m_strCurrTypePkKey).toString();
			// ����PK
			locaCurrType = CurrParamQuery.getInstance().getLocalCurrPK(getCorpId());
			
			//��������
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
    	nc.vo.scm.pub.SCMEnv.out("��ȡ���˹�����ʧ��\n" + e.getMessage());
	}	
  }

  /**
   * ���ߣ���ӡ�� ���ܣ�����һ��KEY�Ƿ����ı����������ı䣬�򲻿��ٸı� ������String sKey KEY boolean bNewStatus
   * ��״̬ ���أ��� ���⣺�� ���ڣ�(2003-6-10 11:39:21) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
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
   * ���ߣ���ӡ�� ���ܣ����ݵ�һ���ı����,������Щ��ɸı� �������� ���أ���ִ�еĵ�һ����ЧCIRCLE ���⣺�� ���ڣ�(2002-5-15
   * 11:39:21) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
   * 2008-03-07 zhangcheng  ������:���༭��˰�ϼ�ʱ,���ݲ����жϵ��ۿۻ򵥼�;Ĭ���ǵ��ۿ�,�򵥼۲���
   */
  private void setKeyExecutedForFirstChangedKey(String sFirstChangedKey,
      int iFirstCircle) {

    if (iFirstCircle == CIRCLE_IMPOSSIBLE) {
      return;
    }

    // ����
    if (sFirstChangedKey.equals(m_strNumKey)) {
      // �̶�������--����λ2���۸񲻱�
      if (m_bFixedConvertRateKey){
    	  if (getUFDouble(m_strAssist_Price) != null) {
              setKeyExecuted(m_strAssist_Price, true);
          }
          if (getUFDouble(m_strAssist_TaxPrice) != null) {
              setKeyExecuted(m_strAssist_TaxPrice, true);
          }
      }
      // ���۹̶�������--���۵�λ4���۸񲻱�
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
      // ����λ4���۸��ۿۣ�˰�ʾ�����
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
    
    //������
    else if (sFirstChangedKey.equals(m_strAssistNumKey)){
      if (getUFDouble(m_strAssist_Price) != null) {
            setKeyExecuted(m_strAssist_Price, true);
      }
      if (getUFDouble(m_strAssist_TaxPrice) != null) {
            setKeyExecuted(m_strAssist_TaxPrice, true);
      }	
      //����λ4���۸��ۿۣ�˰�ʾ�����
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
    //������
    else if (sFirstChangedKey.equals(m_strConvertRateKey)){
      //����λ�����۵�λ8���۸��ۿۣ�˰�ʾ�����,��˰�ϼƣ���˰����
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
    // ��������
    if (sFirstChangedKey.equals(m_strQt_NumKey)) {
    // ����λ4���۸�
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
      
      //���۵�λ4���۸��ۿۣ�˰�ʾ�����
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
    //���ۻ�����
    else if (sFirstChangedKey.equals(m_strQt_ConvertRateKey)){
      //����λ������λ6���۸��ۿۣ�˰�ʾ�����,��˰�ϼƣ���˰����
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
    
    // ��˰����
	/** v5.3��� zhangcheng ���ݲ����������ۿۻ��ǵ���;Ĭ�ϵ��ۿ��򵥼۲��� */
	else if (sFirstChangedKey.equals(m_strNetPriceKey)) {	
	// ���۵��ۿۻ��ǵ���,������˰�ʾ�����
	  if (getUFDouble(m_strNumKey) != null) {
		setKeyExecuted(m_strNumKey, true);
	  }
	  if (getUFDouble(m_strTaxRateKey) != null) {
		setKeyExecuted(m_strTaxRateKey, true);
	  }
	  // ���ۿ�ʱ(Ĭ��):��˰���ۡ���˰���۲���,�ۿ۱�
	  if (m_iPrior_ItemDiscountRate_Price == RelationsCalVO.ITEMDISCOUNTRATE_PRIOR_TO_PRICE) {
		if (getUFDouble(m_strTaxPriceKey) != null
			&& getUFDouble(m_strTaxPriceKey).intValue() > 0) {
				setKeyExecuted(m_strTaxPriceKey, true);
		}
		if (getUFDouble(m_strPriceKey) != null) {
				setKeyExecuted(m_strPriceKey, true);
		}
	  } else {// ������ʱ(Ĭ��):�ۿ۲���,��˰���ۡ���˰���۱�
			if (getUFDouble(m_strDiscountRateKey) != null) {
				setKeyExecuted(m_strDiscountRateKey, true);
			}
		}
	}			
		// ��˰���
		/** v5.3��� zhangcheng ���ݲ����������ۿۻ��ǵ���;Ĭ�ϵ��ۿ��򵥼۲��� */
		else if (sFirstChangedKey.equals(m_strMoneyKey)) {
			// ���۵��ۿۻ��ǵ���,������˰�ʾ�����
			if (getUFDouble(m_strNumKey) != null) {
				setKeyExecuted(m_strNumKey, true);
			}
			if (getUFDouble(m_strTaxRateKey) != null) {
				setKeyExecuted(m_strTaxRateKey, true);
			}
			// ���ۿ�ʱ(Ĭ��):��˰���ۡ���˰���۲���,�ۿ۱�
			if (m_iPrior_ItemDiscountRate_Price == RelationsCalVO.ITEMDISCOUNTRATE_PRIOR_TO_PRICE) {
				if (getUFDouble(m_strTaxPriceKey) != null
						&& getUFDouble(m_strTaxPriceKey).intValue() > 0) {
					setKeyExecuted(m_strTaxPriceKey, true);
				}
				if (getUFDouble(m_strPriceKey) != null) {
					setKeyExecuted(m_strPriceKey, true);
				}
			} else {// ������ʱ(Ĭ��):�ۿ۲���,��˰���ۡ���˰���۱�
				if (getUFDouble(m_strDiscountRateKey) != null) {
					setKeyExecuted(m_strDiscountRateKey, true);
				}
			}
		}
    // ˰��
    else if (sFirstChangedKey.equals(m_strTaxRateKey)) {
      // ���������ʲ���
      if (getUFDouble(m_strNumKey) != null) {
        setKeyExecuted(m_strNumKey, true);
      }
      if (getUFDouble(m_strDiscountRateKey) != null) {
        setKeyExecuted(m_strDiscountRateKey, true);
      }
      if (getPrior_Price_TaxPrice() == RelationsCalVO.TAXPRICE_PRIOR_TO_PRICE) {
        // ��˰�������� ��˰���ۡ�����˰���ۡ���˰�ϼƲ���
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
        // �������� ���ۡ������ۡ�����
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
    // ˰��
    else if (sFirstChangedKey.equals(m_strTaxKey)) {
      // ��˰���ۡ����ʡ���˰���ۡ�˰�ʡ���������˰����
      /** v5.3 ˰�ʡ����������ʡ�4���۸��ۿ۶��˰�ϼ�(��˰����ʱ)����˰���(��˰����ʱ)�������� */	
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
    // ��˰�ϼ�
    /** v5.3���  zhangcheng ���ݲ����������ۿۻ��ǵ���;Ĭ�ϵ��ۿ��򵥼۲��� */
    else if (sFirstChangedKey.equals(m_strSummnyKey)) {
    	// ���۵��ۿۻ��ǵ���,������˰�ʾ�����
		if (getUFDouble(m_strNumKey) != null) {
			setKeyExecuted(m_strNumKey, true);
		}
		if (getUFDouble(m_strTaxRateKey) != null) {
			setKeyExecuted(m_strTaxRateKey, true);
		}
		// ���ۿ�ʱ(Ĭ��):��˰���ۡ���˰���۲���,�ۿ۱�
		if (m_iPrior_ItemDiscountRate_Price == RelationsCalVO.ITEMDISCOUNTRATE_PRIOR_TO_PRICE) {
			if (getUFDouble(m_strTaxPriceKey) != null
					&& getUFDouble(m_strTaxPriceKey).intValue() > 0) {
				setKeyExecuted(m_strTaxPriceKey, true);
			}
			if (getUFDouble(m_strPriceKey) != null) {
				setKeyExecuted(m_strPriceKey, true);
			}
		}
		else{//������ʱ:�ۿ۲���,��˰���ۡ���˰���۱�
			if (getUFDouble(m_strDiscountRateKey) != null) {
				setKeyExecuted(m_strDiscountRateKey, true);
			}
		}
    }
    // ��˰����
    /** v5.3���  zhangcheng ���ݲ����������ۿۻ��ǵ���;Ĭ�ϵ��ۿ��򵥼۲��� */
    else if (sFirstChangedKey.equals(m_strNetTaxPriceKey)) {
      // ���۵��ۿۻ��ǵ���,������˰�ʾ�����
      if (getUFDouble(m_strNumKey) != null) {
        setKeyExecuted(m_strNumKey, true);
      }
      if (getUFDouble(m_strTaxRateKey) != null) {
        setKeyExecuted(m_strTaxRateKey, true);
      }
      // ���ۿ�ʱ(Ĭ��):��˰���ۡ���˰���۲���,�ۿ۱�
	  if (m_iPrior_ItemDiscountRate_Price == RelationsCalVO.ITEMDISCOUNTRATE_PRIOR_TO_PRICE) {
	      if (getUFDouble(m_strTaxPriceKey) != null
			 && getUFDouble(m_strTaxPriceKey).intValue() > 0) {
				 setKeyExecuted(m_strTaxPriceKey, true);
		  }
		  if (getUFDouble(m_strPriceKey) != null) {
				 setKeyExecuted(m_strPriceKey, true);
		  }
	  } else {// ������ʱ:�ۿ۲���,��˰���ۡ���˰���۱�
		  if (getUFDouble(m_strDiscountRateKey) != null) {
			 	setKeyExecuted(m_strDiscountRateKey, true);
			 }
	  }
    }
    
    
    
    // ����
    else if (sFirstChangedKey.equals(m_strDiscountRateKey)||sFirstChangedKey.equals(m_strAllDiscountRateKey)) {
      // ���������ۡ���˰���ۡ�˰�ʲ���
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
    // ��˰����
    else if (sFirstChangedKey.equals(m_strTaxPriceKey)) {
      // ���ʡ�������˰�ʲ���
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
    // ��˰����
    else if (sFirstChangedKey.equals(m_strPriceKey)) {
      // ���ʡ�������˰�ʲ���
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
    // �ϸ����� ���ϸ����� ������ ������
    else if (sFirstChangedKey.equals(m_strQualifiedNumKey)
        || sFirstChangedKey.equals(m_strUnQualifiedNumKey))
         {
      // ͬ�����仯ʱ�Ĳ��ı���
      setKeyExecutedForFirstChangedKey(m_strNumKey, iFirstCircle);
    }
    // ����ԭ�Һ�˰����
    else if (sFirstChangedKey.equals(m_strQt_TaxPriceKey)) {
      // ���ʡ�������˰�ʲ���
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
    // ����ԭ����˰����
    else if (sFirstChangedKey.equals(m_strQt_PriceKey)) {
      // ���ʡ�������˰�ʲ���
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
    // ����ԭ�Һ�˰����
    else if (sFirstChangedKey.equals(m_strQt_NetTaxPriceKey)) {
      // ���۵��ۿۻ��ǵ���,������˰�ʾ�����
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
      
      // ���ۿ�ʱ(Ĭ��):����ԭ�Һ�˰���ۡ���˰���۲���,�ۿ۱�
	  if (m_iPrior_ItemDiscountRate_Price == RelationsCalVO.ITEMDISCOUNTRATE_PRIOR_TO_PRICE) {
	      if (getUFDouble(m_strQt_TaxPriceKey) != null
			 && getUFDouble(m_strQt_TaxPriceKey).intValue() > 0) {
				 setKeyExecuted(m_strQt_TaxPriceKey, true);
		  }
		  if (getUFDouble(m_strQt_PriceKey) != null) {
				 setKeyExecuted(m_strQt_PriceKey, true);
		  }
	  } else {// ������ʱ:�ۿ۲���,����ԭ�Һ�˰���ۡ���˰���۱�
		  if (getUFDouble(m_strDiscountRateKey) != null) {
			 	setKeyExecuted(m_strDiscountRateKey, true);
			 }
	  }
    }
    // ����ԭ����˰����
	else if (sFirstChangedKey.equals(m_strQt_NetPriceKey)) {	
	// ���۵��ۿۻ��ǵ���,������˰�ʾ�����
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
	  // ���ۿ�ʱ(Ĭ��):����ԭ�Һ�˰���ۡ���˰���۲���,�ۿ۱�
	  if (m_iPrior_ItemDiscountRate_Price == RelationsCalVO.ITEMDISCOUNTRATE_PRIOR_TO_PRICE) {
		if (getUFDouble(m_strQt_TaxPriceKey) != null
			&& getUFDouble(m_strQt_TaxPriceKey).intValue() > 0) {
				setKeyExecuted(m_strQt_TaxPriceKey, true);
		}
		if (getUFDouble(m_strQt_PriceKey) != null) {
				setKeyExecuted(m_strQt_PriceKey, true);
		}
	  } else {// ������ʱ:�ۿ۲���,����ԭ�Һ�˰���ۡ���˰���۱�
			if (getUFDouble(m_strDiscountRateKey) != null) {
				setKeyExecuted(m_strDiscountRateKey, true);
			}
		}
	}
    // ���Ҽ�˰�ϼơ�������˰���
	else if (sFirstChangedKey.equals(m_strSummny_LocalKey)
				|| sFirstChangedKey.equals(m_strMoney_LocalKey)) {	
	  // ���ʡ�ԭ�ҽ���
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
   * ���ߣ���ӡ�� ���ܣ�����һ��iCircle�е�����KEY�Ƿ����ı����������ı䣬�򲻿��ٸı� ������String sKey KEY boolean
   * bNewStatus ��״̬ ���أ��� ���⣺�� ���ڣ�(2003-6-10 11:39:21) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
   */
  private void setKeysExecuted(int iCircle, boolean bNewStatus) {

    if (iCircle == CIRCLE_NUM_NETPRICE_MONEY) { // ����,������,���
      setKeyExecuted(m_strNumKey, bNewStatus);
      setKeyExecuted(m_strMoneyKey, bNewStatus);
      setKeyExecuted(m_strNetPriceKey, bNewStatus);
    }
    else if (iCircle == CIRCLE_TAXRATE) { // ˰��,���,˰��,��˰�ϼ�
      setKeyExecuted(m_strTaxRateKey, bNewStatus);
      setKeyExecuted(m_strMoneyKey, bNewStatus);
      setKeyExecuted(m_strSummnyKey, bNewStatus);
      setKeyExecuted(m_strTaxKey, bNewStatus);
    }
    else if (iCircle == CIRCLE_NUM_NETTAXPRICE_SUMMNY) { // ����,����˰����,��˰�ϼ�
      setKeyExecuted(m_strNumKey, bNewStatus);
      setKeyExecuted(m_strNetTaxPriceKey, bNewStatus);
      setKeyExecuted(m_strSummnyKey, bNewStatus);
    }
    else if (iCircle == CIRCLE_DISCOUNTMNY) { // ��˰���ۡ���������˰�ϼơ��ۿ۶�
        setKeyExecuted(m_strNumKey, bNewStatus);
        setKeyExecuted(m_strTaxPriceKey, bNewStatus);
        setKeyExecuted(m_strSummnyKey, bNewStatus);
        setKeyExecuted(m_strDiscountMnyKey, bNewStatus);
    }
    else if (iCircle == CIRCLE_PRICE_NETPRICE) { // ����,������,����
      setKeyExecuted(m_strNetPriceKey, bNewStatus);
      setKeyExecuted(m_strPriceKey, bNewStatus);
      setKeyExecuted(m_strDiscountRateKey, bNewStatus);
    }
    else if (iCircle == CIRCLE_QUALIFIED_NUM) { // ����,�ϸ�����,���ϸ�����
      setKeyExecuted(m_strNumKey, bNewStatus);
      setKeyExecuted(m_strQualifiedNumKey, bNewStatus);
      setKeyExecuted(m_strUnQualifiedNumKey, bNewStatus);
    }
    else if (iCircle == CIRCLE_CONVERT_RATE) { // ������,������,������,��������,���ۻ�����
      setKeyExecuted(m_strNumKey, bNewStatus);
      setKeyExecuted(m_strAssistNumKey, bNewStatus);
      setKeyExecuted(m_strConvertRateKey, bNewStatus);
      setKeyExecuted(m_strNetPriceKey, bNewStatus);
    }
    else if (iCircle == CIRCLE_QT_CONVERT_RATE) { // ������,��������,���ۻ�����
        setKeyExecuted(m_strNumKey, bNewStatus);
        setKeyExecuted(m_strQt_NumKey, bNewStatus);
        setKeyExecuted(m_strQt_ConvertRateKey, bNewStatus);
    }
    else if (iCircle == CIRCLE_TAXPRICE_NETTAXPRICE) { // ����,����˰����,��˰����
      setKeyExecuted(m_strNetTaxPriceKey, bNewStatus);
      setKeyExecuted(m_strTaxPriceKey, bNewStatus);
      setKeyExecuted(m_strDiscountRateKey, bNewStatus);
    }
    else if (iCircle == CIRCLE_TAXPRICE_PRICE) { // ˰��,��˰����,����
      setKeyExecuted(m_strTaxRateKey, bNewStatus);
      setKeyExecuted(m_strTaxPriceKey, bNewStatus);
      setKeyExecuted(m_strPriceKey, bNewStatus);
    }
    else if (iCircle == CIRCLE_NETTAXPRICE_NETPRICE) { // ˰��,����˰����,����
      setKeyExecuted(m_strTaxRateKey, bNewStatus);
      setKeyExecuted(m_strNetTaxPriceKey, bNewStatus);
      setKeyExecuted(m_strNetPriceKey, bNewStatus);
    }
    
    else if (iCircle == CIRCLE_QT_TAXPRICE_PRICE) { // ˰�ʡ�����ԭ�Һ�˰���ۡ�����ԭ����˰����
        setKeyExecuted(m_strTaxRateKey, bNewStatus);
        setKeyExecuted(m_strQt_TaxPriceKey, bNewStatus);
        setKeyExecuted(m_strQt_PriceKey, bNewStatus);
      }
    else if (iCircle == CIRCLE_QT_NETTAXPRICE_NETPRICE) { // ˰�ʡ�����ԭ�Һ�˰���ۡ�����ԭ����˰����
        setKeyExecuted(m_strTaxRateKey, bNewStatus);
        setKeyExecuted(m_strQt_NetTaxPriceKey, bNewStatus);
        setKeyExecuted(m_strQt_NetPriceKey, bNewStatus);
      }
    else if (iCircle == CIRCLE_QT_TAXPRICE_NETTAXPRICE) { // ���ʡ�����ԭ�Һ�˰���ۡ�����ԭ�Һ�˰����
        setKeyExecuted(m_strDiscountRateKey, bNewStatus);
        setKeyExecuted(m_strQt_TaxPriceKey, bNewStatus);
        setKeyExecuted(m_strQt_NetTaxPriceKey, bNewStatus);
      }
    else if (iCircle == CIRCLE_QT_PRICE_NETPRICE) { // ���ʡ�����ԭ����˰���ۡ�����ԭ����˰����
        setKeyExecuted(m_strDiscountRateKey, bNewStatus);
        setKeyExecuted(m_strQt_PriceKey, bNewStatus);
        setKeyExecuted(m_strQt_NetPriceKey, bNewStatus);
      }
    
    else if (iCircle == CIRCLE_QT_NUM_NETTAXPRICE_SUMMNY) { // ��������������ԭ�Һ�˰���ۡ���˰�ϼ�
        setKeyExecuted(m_strSummnyKey, bNewStatus);
        setKeyExecuted(m_strQt_NumKey, bNewStatus);
        setKeyExecuted(m_strQt_NetTaxPriceKey, bNewStatus);
      }
    else if (iCircle == CIRCLE_QT_NUM_NETPRICE_MONEY) { // ��������������ԭ����˰���ۡ���˰���
        setKeyExecuted(m_strMoneyKey, bNewStatus);
        setKeyExecuted(m_strQt_NumKey, bNewStatus);
        setKeyExecuted(m_strQt_NetPriceKey, bNewStatus);
      }
    else if (iCircle == CIRCLE_LOCAL_SUMMNY_MNY_TAX) { // ���Ҽ�˰�ϼơ�������˰������˰��
        setKeyExecuted(m_strSummny_LocalKey, bNewStatus);
        setKeyExecuted(m_strMoney_LocalKey, bNewStatus);
        setKeyExecuted(m_strTax_LocalKey, bNewStatus);
      }
    

  }

  /**
   * ���ߣ���ӡ�� ���ܣ���ģ�������þ�ֵ���˴�Ϊ�ع� ������ ���أ� ���⣺�� ���ڣ�(2001-05-27 11:39:21)
   * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
   */

  private void setOldValuesToPanel() {
    // ����
    // if(m_pnlBill.getBodyItem(m_strNumKey) != null ){
    // m_pnlBill.getBillModel().setValueAt(m_objOldNum,m_iPos,m_strNumKey) ;
    m_voCurr.setAttributeValue(m_strNumKey, m_objOldNum);
    // }
    // ������
    // if(m_pnlBill.getBodyItem(m_strNetPriceKey) != null ){
    // m_pnlBill.getBillModel().setValueAt(m_objOldNetPrice,m_iPos,m_strNetPriceKey)
    // ;
    m_voCurr.setAttributeValue(m_strNetPriceKey, m_objOldNetPrice);
    // }
    // ���
    // if(m_pnlBill.getBodyItem(m_strMoneyKey) != null ){
    // m_pnlBill.getBillModel().setValueAt(m_objOldMoney,m_iPos,m_strMoneyKey) ;
    m_voCurr.setAttributeValue(m_strMoneyKey, m_objOldMoney);
    // }
    // ��˰������
    // if(m_pnlBill.getBodyItem(m_strNetTaxPriceKey) != null ){
    // m_pnlBill.getBillModel().setValueAt(m_objOldNetTaxPrice,m_iPos,m_strNetTaxPriceKey)
    // ;
    m_voCurr.setAttributeValue(m_strNetTaxPriceKey, m_objOldNetTaxPrice);
    // }
    // ˰��
    // if(m_pnlBill.getBodyItem(m_strTaxRateKey) != null ){
    // m_pnlBill.getBillModel().setValueAt(m_objOldTaxrate,m_iPos,m_strTaxRateKey)
    // ;
    m_voCurr.setAttributeValue(m_strTaxRateKey, m_objOldTaxrate);
    // }
    // ˰��
    // if(m_pnlBill.getBodyItem(m_strTaxKey) != null ){
    // m_pnlBill.getBillModel().setValueAt(m_objOldTax,m_iPos,m_strTaxKey) ;
    m_voCurr.setAttributeValue(m_strTaxKey, m_objOldTax);
    // }
    // ��˰�ϼ�
    // if(m_pnlBill.getBodyItem(m_strSummnyKey) != null ){
    // m_pnlBill.getBillModel().setValueAt(m_objOldSummny,m_iPos,m_strSummnyKey)
    // ;
    m_voCurr.setAttributeValue(m_strSummnyKey, m_objOldSummny);
    // }
    // ����
    // if(m_pnlBill.getBodyItem(m_strDiscountRateKey) != null ){
    // m_pnlBill.getBillModel().setValueAt(m_objOldDiscountRate,m_iPos,m_strDiscountRateKey)
    // ;
    m_voCurr.setAttributeValue(m_strDiscountRateKey, m_objOldDiscountRate);
    // }
    // ����
    // if(m_pnlBill.getBodyItem(m_strPriceKey) != null ){
    // m_pnlBill.getBillModel().setValueAt(m_objOldPrice,m_iPos,m_strPriceKey) ;
    m_voCurr.setAttributeValue(m_strPriceKey, m_objOldPrice);
    // }
    // �ϸ�����
    // if(m_pnlBill.getBodyItem(m_strQualifiedNumKey) != null ){
    // m_pnlBill.getBillModel().setValueAt(m_objOldQualifiedNum,m_iPos,m_strQualifiedNumKey)
    // ;
    m_voCurr.setAttributeValue(m_strQualifiedNumKey, m_objOldQualifiedNum);
    // }
    // ���ϸ�����
    // if(m_pnlBill.getBodyItem(m_strUnQualifiedNumKey) != null ){
    // m_pnlBill.getBillModel().setValueAt(m_objOldUnQualifiedNum,m_iPos,m_strUnQualifiedNumKey)
    // ;
    m_voCurr.setAttributeValue(m_strUnQualifiedNumKey, m_objOldUnQualifiedNum);
    // }
    // ������
    // if(m_pnlBill.getBodyItem(m_strAssistNumKey) != null ){
    // m_pnlBill.getBillModel().setValueAt(m_objOldAssistNum,m_iPos,m_strAssistNumKey)
    // ;
    m_voCurr.setAttributeValue(m_strAssistNumKey, m_objOldAssistNum);
    // }
    // ������
    // if(m_pnlBill.getBodyItem(m_strConvertRateKey) != null ){
    // m_pnlBill.getBillModel().setValueAt(m_objOldConvertRate,m_iPos,m_strConvertRateKey)
    // ;
    m_voCurr.setAttributeValue(m_strConvertRateKey, m_objOldConvertRate);
    // }
    // ��˰����
    // if(m_pnlBill.getBodyItem(m_strTaxPriceKey) != null ){
    // m_pnlBill.getBillModel().setValueAt(m_objOldTaxPrice,m_iPos,m_strTaxPriceKey)
    // ;
    m_voCurr.setAttributeValue(m_strTaxPriceKey, m_objOldTaxPrice);
    // }
  }

  /**
   * ����ǿ�в��ɱ༭�ֶ�ӳ��
   * @param forbidEditField
   */
  private void setForbidEditField(int[] forbidEditField){
	  
	  if (forbidEditField==null || forbidEditField.length<=0)
		  return;
	  
	  for (int field : forbidEditField){
		     if (field == NUM) {// ����
		         setKeyExecuted(m_strNumKey, true);
		      }
		      else if (field == NUM_QUALIFIED) {// �ϸ�����
		         setKeyExecuted(m_strQualifiedNumKey, true);
		      }
		      else if (field == NUM_UNQUALIFIED) {// ���ϸ�����
		        
		        setKeyExecuted(m_strUnQualifiedNumKey, true);
		      }
		      else if (field == NUM_ASSIST) {// ������
		       
		        setKeyExecuted(m_strAssistNumKey, true);
		      }
		      else if (field == NET_PRICE_ORIGINAL) {// ԭ����˰����
		        
		        setKeyExecuted(m_strNetPriceKey, true);
		      }
		      else if (field == NET_TAXPRICE_ORIGINAL) {// ԭ�Һ�˰����
		        
		        setKeyExecuted(m_strNetTaxPriceKey, true);
		      }
		      else if (field == PRICE_ORIGINAL) {// ԭ����˰����
		       
		        setKeyExecuted(m_strPriceKey, true);
		      }
		      else if (field == TAXPRICE_ORIGINAL) {// ԭ�Һ�˰����
		    	  setKeyExecuted(m_strTaxPriceKey, true);
		        }
		      else if (field == TAXRATE) {// ˰��
		        
		        setKeyExecuted(m_strTaxRateKey, true);
		      }
		      else if (field == TAX_ORIGINAL) {// ԭ��˰��
		        
		        setKeyExecuted(m_strTaxKey, true);
		      }
		      else if (field == MONEY_ORIGINAL) {// ��˰���
		      
		        setKeyExecuted(m_strMoneyKey, true);
		      }
		      else if (field == SUMMNY_ORIGINAL) {// ԭ�Ҽ�˰�ϼ�
		     
		        setKeyExecuted(m_strSummnyKey, true);
		      }
		      else if (field == DISCOUNT_RATE) {// ����
		       
		        setKeyExecuted(m_strDiscountRateKey, true);
		      }
		      else if (field == ALLDISCOUNT_RATE) {// �����ۿ�
		    	
		    	  setKeyExecuted(m_strAllDiscountRateKey, true);
		        }
		      else if (field == CONVERT_RATE) {// ������
		  
		        setKeyExecuted(m_strConvertRateKey, true);
		      }
		      else if (field == QT_CONVERT_RATE) {// ���ۻ�����
		      
		          setKeyExecuted(m_strQt_ConvertRateKey, true);
		      }
		      else if (field == TAXPRICE_ORIGINAL) {// ��˰����
		     
		        setKeyExecuted(m_strTaxPriceKey, true);
		      }
		      else if (field == DISCOUNTMNY_ORIGINA) {// ԭ���ۿ۶�
		    	
		    	  setKeyExecuted(m_strDiscountMnyKey, true);
		      }
		      else if (field == QT_NUM) {// ԭ�ұ��۵�λ����
		    	
		    	  setKeyExecuted(m_strQt_NumKey, true);
		      }
		      else if (field == QT_TAXPRICE_ORIGINAL) {// ԭ�ұ��۵�λ��˰����
		    	
		    	  setKeyExecuted(m_strQt_TaxPriceKey, true);
		      }
		      else if (field == QT_PRICE_ORIGINAL) {// ԭ�ұ��۵�λ��˰����
		    	
		    	  setKeyExecuted(m_strQt_PriceKey, true);
		      }
		      else if (field == QT_NET_TAXPRICE_ORIGINAL) {// ԭ�ұ��۵�λ��˰����
		    	
		    	  setKeyExecuted(m_strQt_NetTaxPriceKey, true);
		      }
		      else if (field == QT_NET_PRICE_ORIGINAL) {// ԭ�ұ��۵�λ��˰����
		    
		    	  setKeyExecuted(m_strQt_NetPriceKey, true);
		      }
		      /*else if (field == PRICE_LOCAL) {// ������˰����
		    	
		    	  setKeyExecuted(m_strPrice_LocalKey, true);
		      }
		      else if (field == TAXPRICE_LOCAL) {// ���Һ�˰���� 
		    
		    	  setKeyExecuted(m_strTaxPrice_LocalKey, true);
		      }
		      else if (field == NET_PRICE_LOCAL) {// ������˰����
		    	 
		    	  setKeyExecuted(m_strNet_Price_LocalKey, true);
		      }
		      else if (field == NET_TAXPRICE_LOCAL) {// ���Һ�˰����
		    	
		    	  setKeyExecuted(m_strNet_TaxPrice_LocalKey, true);
		      }
		      else if (field == TAX_LOCAL) {// ����˰��
		    	 
		    	  setKeyExecuted(m_strTax_LocalKey, true);
		      }
		      else if (field == MONEY_LOCAL) {// ������˰���
		    	
		    	  setKeyExecuted(m_strMoney_LocalKey, true);
		      }
		      else if (field == SUMMNY_LOCAL) {// ���Ҽ�˰�ϼ�
		    
		    	  setKeyExecuted(m_strSummny_LocalKey, true);
		      }
		      else if (field == DISCOUNTMNY_LOCAL) {//�����ۿ۶�
		    	
		    	  setKeyExecuted(m_strDiscountMny_LocalKey, true);
		      }
		      else if (field == QT_PRICE_LOCAL) {// ���۵�λ������˰����
		    	
		    	  setKeyExecuted(m_strQt_Price_LocalKey, true);
		      }
		      else if (field == QT_TAXPRICE_LOCAL) {// ���۵�λ���Һ�˰����
		    	
		    	  setKeyExecuted(m_strQt_TaxPrice_LocalKey, true);
		      }
		      else if (field == QT_NET_PRICE_LOCAL) {// ���۵�λ������˰����
		    	
		    	  setKeyExecuted(m_strQt_Net_Price_LocalKey, true);
		      }
		      else if (field == QT_NET_TAXPRICE_LOCAL) {//���۵�λ���Һ�˰����
		    
		    	  setKeyExecuted(m_strQt_Net_TaxPrice_LocalKey, true);
		      }
		      else if (field == PRICE_FRACTIONAL) {// ������˰����
		    	 
		    	  setKeyExecuted(m_strPrice_FractionalKey, true);
		      }
		      else if (field == TAXPRICE_FRACTIONAL) {// ���Һ�˰���� 
		    	 
		    	  setKeyExecuted(m_strTaxPrice_FractionalKey, true);
		      }
		      else if (field == NET_PRICE_FRACTIONAL) {// ������˰����
		    	 
		    	  setKeyExecuted(m_strNet_Price_FractionalKey, true);
		      }
		      else if (field == NET_TAXPRICE_FRACTIONAL) {// ���Һ�˰����
		    	  
		    	  setKeyExecuted(m_strNet_TaxPrice_FractionalKey, true);
		      }
		      else if (field == TAX_FRACTIONAL) {// ����˰��
		    	
		    	  setKeyExecuted(m_strTax_FractionalKey, true);
		      }
		      else if (field == MONEY_FRACTIONAL) {// ������˰���
		    	
		    	  setKeyExecuted(m_strMoney_FractionalKey, true);
		      }
		      else if (field == SUMMNY_FRACTIONAL) {// ���Ҽ�˰�ϼ�
		    	
		    	  setKeyExecuted(m_strSummny_FractionalKey, true);
		      }
		      else if (field == DISCOUNTMNY_FRACTIONAL) {//�����ۿ۶�
		    	 
		    	  setKeyExecuted(m_strDiscountMny_FractionalKey, true);
		      }
		      else if (field == ASSIST_PRICE_ORIGINAL) {// ��������˰����
		    	
		    	  setKeyExecuted(m_strAssist_Price, true);
		      }
		      else if (field == ASSIST_TAXPRICE_ORIGINAL) {//��������˰����
		    	  setKeyExecuted(m_strAssist_TaxPrice, true);
		      }
		      else if (field == ASK_TAXPRICE) {// ѯ��ԭ�Һ�˰����
		    	  setKeyExecuted(m_strAsk_TaxPriceKey, true);
		      }
		      else if (field == ASK_PRICE) {//ѯ��ԭ����˰����
		    	  setKeyExecuted(m_strAsk_PriceKey, true);
		      }  		*/
  	  }
  }
  
    /**
	 * Ϊ�������ȱ䶯key�������ú�˰���Ȼ���
	 */
	private void setPriorForSetFirstChangeKey(int[] iaPrior) {
		if (iaPrior == null) {
			return;
		}
		int iLen = iaPrior.length;
		for (int i = 0; i < iLen; i++) {
			// ��˰����˰����
			if (iaPrior[i] == RelationsCalVO.PRICE_PRIOR_TO_TAXPRICE) {
				m_iPrior_Price_TaxPrice = RelationsCalVO.PRICE_PRIOR_TO_TAXPRICE;
			}
			if (iaPrior[i] == RelationsCalVO.TAXPRICE_PRIOR_TO_PRICE) {
				m_iPrior_Price_TaxPrice = RelationsCalVO.TAXPRICE_PRIOR_TO_PRICE;
			}
		}
	}
	    
  /**
	 * ���ߣ���ӡ�� ���ܣ��������Ȼ��� ������int[] iaPrior ���޻������� �����޳������ƣ�����ͬ�����ʵ����Ȼ��ƣ��Ժ������Ϊ׼
	 * �磺[0]=NUM_PRIOR_TO_PRICE [3]=PRICE_PRIOR_TO_NUM ��ȡPRICE_PRIOR_TO_NUM ���أ���
	 * ���⣺�� ���ڣ�(2002-5-15 11:39:21) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 */
  private void setPrior(int[] iaPrior) {
    if (iaPrior == null) {
      return;
    }
    int iLen = iaPrior.length;
    for (int i = 0; i < iLen; i++) {
      // ��˰����˰����
      if (iaPrior[i] == RelationsCalVO.PRICE_PRIOR_TO_TAXPRICE) {
        m_iPrior_Price_TaxPrice = RelationsCalVO.PRICE_PRIOR_TO_TAXPRICE;
      }
      if (iaPrior[i] == RelationsCalVO.TAXPRICE_PRIOR_TO_PRICE) {
        m_iPrior_Price_TaxPrice = RelationsCalVO.TAXPRICE_PRIOR_TO_PRICE;
      }
      //�༭���ۣ���˰�ϼ�ʱ�������ۻ����ۿ�
      if (iaPrior[i] == RelationsCalVO.ITEMDISCOUNTRATE_PRIOR_TO_PRICE) {
    	  m_iPrior_ItemDiscountRate_Price = RelationsCalVO.ITEMDISCOUNTRATE_PRIOR_TO_PRICE;
      }
      if (iaPrior[i] == RelationsCalVO.PRICE_PRIOR_TO_ITEMDISCOUNTRATE) {
    	  m_iPrior_ItemDiscountRate_Price = RelationsCalVO.PRICE_PRIOR_TO_ITEMDISCOUNTRATE;
      }
      //�����Һ���
      if (iaPrior[i] == RelationsCalVO.YES_LOCAL_FRAC) {
    	  m_iPrior_LOCAL_FRAC = RelationsCalVO.YES_LOCAL_FRAC;
      }
      if (iaPrior[i] == RelationsCalVO.NO_LOCAL_FRAC) {
    	  m_iPrior_LOCAL_FRAC = RelationsCalVO.NO_LOCAL_FRAC;
      }
    }
    
    //(�������Ϊ0��null��ǿ�Ƶ�������)
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
    
    //���ǿ�����ú�˰��˰���Ȼ���
    //��˰����
	if (getFirstChangedKey().equals(m_strSummnyKey)
				|| getFirstChangedKey().equals(m_strTaxPriceKey)
				|| getFirstChangedKey().equals(m_strNetTaxPriceKey)
				|| getFirstChangedKey().equals(m_strQt_TaxPriceKey)
				|| getFirstChangedKey().equals(m_strQt_NetTaxPriceKey))
		m_iPrior_Price_TaxPrice = RelationsCalVO.TAXPRICE_PRIOR_TO_PRICE;
	// ��˰����
	else if (getFirstChangedKey().equals(m_strMoneyKey)
				|| getFirstChangedKey().equals(m_strPriceKey)
				|| getFirstChangedKey().equals(m_strNetPriceKey)
				|| getFirstChangedKey().equals(m_strQt_PriceKey)
				|| getFirstChangedKey().equals(m_strQt_NetPriceKey))
		m_iPrior_Price_TaxPrice = RelationsCalVO.PRICE_PRIOR_TO_TAXPRICE;
  }

  /**
   * ���ع�˾����[����BD501]��[������BD502]��[������BD503]��[����BD505]
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
      SCMEnv.out("ȡ��˾���ȣ�[����BD501]��[������BD502]��[������BD503]��[����BD505]ʱ�����쳣��");/*-=notranslate=-*/
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
   * ����ѯ���ݾ���(�ǽ����ֵ��)
   * 
   * @return int[]
   * @param corpId
   *          ��˾����
   * @param paraCodes
   *          ������������
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
       * �̶���ǰ̨������ȡ��������֧�ֶ�̬�޸Ĳ��������¶�ȡ���ݿ�
       * ��Ϊ���з�ʽ����У��ʱ������б䶯�����ݿ⣬�����»���
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
   * �ӱ�ͷ�����ȡ��˾��ԭ�ұ���
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
   * ��ȡ���㾫��--������
   * <p>
   * <strong>ע�⣺</strong>
   * <p>
   * 1�������ǰ����VOδʵ�ֽӿ�[IRelaCalInfos]����Ĭ�Ͼ��ȴ���[����]
   * <p>
   * 2�������ǰ����VO���ӿ�[IRelaCalInfos]��ʵ��δ��ȷ����[��˾����]����Ĭ�Ͼ��ȴ���[����]
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
   * ��ȡ���㾫��--������
   * <p>
   * <strong>ע�⣺</strong>
   * <p>
   * 1�������ǰ����VOδʵ�ֽӿ�[IRelaCalInfos]����Ĭ�Ͼ��ȴ���[������]
   * <p>
   * 2�������ǰ����VO���ӿ�[IRelaCalInfos]��ʵ��δ��ȷ����[��˾����]����Ĭ�Ͼ��ȴ���[������]
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
   * ��ȡ���㾫��--������
   * <p>
   * <strong>ע�⣺</strong>
   * <p>
   * 1�������ǰ����VOδʵ�ֽӿ�[IRelaCalInfos]����Ĭ�Ͼ��ȴ���[������]
   * <p>
   * 2�������ǰ����VO���ӿ�[IRelaCalInfos]��ʵ��δ��ȷ����[��˾����]����Ĭ�Ͼ��ȴ���[������]
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
   * ��ȡ���㾫��--����
   * <p>
   * <strong>ע�⣺</strong>
   * <p>
   * 1�������ǰ����VOδʵ�ֽӿ�[IRelaCalInfos]����Ĭ�Ͼ��ȴ���[����]
   * <p>
   * 2�������ǰ����VO���ӿ�[IRelaCalInfos]��ʵ��δ��ȷ����[��˾����]����Ĭ�Ͼ��ȴ���[����]
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
   * @return ���ݹ�˾��õ��۾���
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
	 * ��ȡ���ּ��㾫��--ҵ��
	 * <p>
	 * <strong>ע�⣺</strong>
	 * <p>
	 * 1�������ǰ����VOδʵ�ֽӿ�[IRelaCalInfos]����Ĭ�Ͼ��ȴ���[���]
	 * <p>
	 * 2�������ǰ����VO���ӿ�[IRelaCalInfos]��ʵ��δ��ȷ����[��������]����Ĭ�Ͼ��ȴ���[���]
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
        SCMEnv.out("ȡ���֣�[��" + strCurrTypePk + "��]����ʱ�����쳣��");-=notranslate=-
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
   * @return ���ݱ���ID��ȡ����ҵ�񾫶�(���ڿ��ƽ���)
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
	        SCMEnv.out("ȡ���֣�[��" + strCurrTypePk + "��]����ʱ�����쳣��");-=notranslate=-
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
   * ��ȡ���ּ��㾫��--����
   * <p>
   * <strong>ע�⣺</strong>
   * <p>
   * 1�������ǰ����VOδʵ�ֽӿ�[IRelaCalInfos]����Ĭ�Ͼ��ȴ���[���]
   * <p>
   * 2�������ǰ����VO���ӿ�[IRelaCalInfos]��ʵ��δ��ȷ����[��������]����Ĭ�Ͼ��ȴ���[���]
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
        SCMEnv.out("ȡ���֣�[��" + strCurrTypePk + "��]����ʱ�����쳣��");-=notranslate=-
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
   * ��ȡ���ּ��㾫��--����
   * <p>
   * <strong>ע�⣺</strong>
   * <p>
   * 1�������ǰ����VOδʵ�ֽӿ�[IRelaCalInfos]����Ĭ�Ͼ��ȴ���[���]
   * <p>
   * 2�������ǰ����VO���ӿ�[IRelaCalInfos]��ʵ��δ��ȷ����[��������]����Ĭ�Ͼ��ȴ���[���]
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
        SCMEnv.out("ȡ���֣�[��" + strCurrTypePk + "��]����ʱ�����쳣��");-=notranslate=-
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