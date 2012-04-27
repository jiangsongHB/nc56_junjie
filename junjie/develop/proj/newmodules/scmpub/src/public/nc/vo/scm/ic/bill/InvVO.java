package nc.vo.scm.ic.bill;

import java.util.ArrayList;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.NullFieldException;
import nc.vo.pub.ValidationException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;

public class InvVO extends CircularlyAccessibleValueObject
  implements FreeItemDeal
{
  public static int quality_day = 2;
  public static int quality_month = 1;
  public static int quality_year = 0;

  public String m_pk = null;

  public String m_cinventoryid = null;
  public String m_cinventorycode = null;
  public String m_invname = null;
  public String m_invspec = null;
  public String m_invtype = null;
  public String m_pk_measdoc = null;
  public String m_measdocname = null;
  public String m_castunitid = null;
  public String m_castunitname = null;
  public UFDouble m_hsl = null;
  public Integer m_isLotMgt = null;
  public Integer m_isSerialMgt = null;
  public Integer m_isValidateMgt = null;
  public Integer m_isAstUOMmgt = null;
  public Integer m_isFreeItemMgt = null;
  public Integer m_isSet = null;
  public UFDouble m_standStoreUOM = null;
  public String m_defaultAstUOM = null;
  public Integer m_isSellProxy = null;

  public Integer m_qualityDay = null;

  public Integer m_qualityperiodunit = null;

  public Integer m_isSolidConvRate = null;

  public UFBoolean m_autobalancemeas = null;

  public String m_vbatchcode = null;
  public UFDate m_dvalidate = null;
  public UFDouble m_ninnum = null;
  public UFDouble m_ninassistnum = null;
  public UFDouble m_noutnum = null;
  public UFDouble m_noutassistnum = null;
  public UFDouble m_nplannedprice = null;
  public Integer m_flargess = null;
  public Integer m_faccflag = null;
  public Integer m_fchecked = null;
  public UFDouble m_bkxcl = null;
  public UFDouble m_xczl = null;
  public UFDouble m_nmaxstocknum = null;
  public UFDouble m_nminstocknum = null;
  public UFDouble m_norderpointnum = null;
  public UFDouble m_nsafestocknum = null;
  public static final int FIFO = 0;
  final Integer IntTrue = new Integer(1);
  public static final int LIFO = 1;
  public String m_ccorrespondbid = null;
  public String m_ccorrespondcode = null;
  public String m_ccorrespondhid = null;
  public String m_ccorrespondtype = null;
  public String m_ccorrespondtypename = null;

  public String m_cfreezeid = null;

  public String m_cgeneralbid = null;
  public String m_cspecialbid = null;

  public String m_cgeneralhid = null;
  public UFDouble m_childsnum = null;
  public UFDouble m_chzl = null;

  public String m_cinvmanid = null;
  public String m_cprojectcode = null;
  public String m_cprojectid = null;
  public String m_cprojectname = null;
  public String m_cprojectphasecode = null;
  public String m_cprojectphaseid = null;
  public String m_cprojectphasename = null;
  public String m_cspacecode = null;
  public String m_cspaceid = null;
  public String m_cspacename = null;
  public UFBoolean m_discountflag = null;

  public UFDate m_dproducedate = null;
  public Integer m_fbillrowflag = null;
  public FreeVO m_freevo = null;
  public Integer m_icheckcycle = null;
  public String m_invclasscode = null;
  public String m_invclasslev = null;
  public String m_invclassname = null;
  public String m_invsetparttype = null;

  public UFBoolean m_iscancalculatedinvcost = new UFBoolean("Y");

  public UFBoolean m_Isprimarybarcode = new UFBoolean("N");

  public UFBoolean m_Issecondarybarcode = new UFBoolean("N");
  public UFBoolean m_isused;
  public UFDouble m_keepwasterate = null;
  public UFBoolean m_laborflag = null;

  public UFBoolean m_negallowed = new UFBoolean("N");
  public UFDouble m_nonhandassistnum = null;

  public UFDouble m_nonhandnum = null;
  public UFDouble m_nonhandgrossnum = null;

  public UFDouble m_npacknum = null;
  public UFDouble m_nunitvolume = null;
  public UFDouble m_nunitweight = null;

  public Integer m_outpriority = new Integer(0);

  public UFBoolean m_outtrackin = new UFBoolean("N");
  public UFDouble m_partpercent = null;

  public String m_pk_corp = null;

  public String m_pk_invcl = null;
  public String m_pk_packsort = null;

  public UFBoolean m_sellproxyflag = null;
  public String m_vpacktypename = null;
  public static final int VALIDATE = 2;
  public Integer m_invReservedPty = null;

  private String m_cselastunitid = null;

  private String m_Cckjlc = null;
  private String m_Crkjlc = null;
  public Integer m_issupplierstock = null;
  public Integer m_ismngstockbygrswt = null;
  public UFDouble m_ningrossnum = null;
  public UFDouble m_noutgrossnum = null;
  public Integer m_isstorebyconvert = null;

  public String m_cvendorid = null;

  public Integer m_isAsset = null;
  
  /**add by ouyangzhb 增加自定义项的取值*/
	public String  m_vdef1= null;
	public String  m_vdef2= null;
	public String  m_vdef3= null;
	public String  m_vdef4= null;
	public String  m_vdef5= null;
	public String  m_vdef6= null;
	public String  m_vdef7= null;
	public String  m_vdef8= null;
	public String  m_vdef9= null;
	public String  m_vdef10= null;
	public String  m_vdef11= null;
	 /**add by ouyangzhb 增加自定义项的取值end*/
  
  

  public InvVO()
  {
    this.m_freevo = new FreeVO();
  }

  public InvVO(String newPk)
  {
    this.m_freevo = new FreeVO();

    this.m_pk = newPk;
  }

  public Object clone()
  {
    Object o = null;
    try {
      o = super.clone(); } catch (Exception e) {
    }
    InvVO inv = (InvVO)o;

    if (this.m_freevo != null) {
      inv.setFreeItemVO((FreeVO)this.m_freevo.clone());
    }
    return inv;
  }

  public String getEntityName()
  {
    return "Inv";
  }

  public String getPrimaryKey()
  {
    return this.m_pk;
  }

  public void setPrimaryKey(String newPk)
  {
    this.m_pk = newPk;
  }

  public String getPk()
  {
    return this.m_pk;
  }

  public String getCinventoryid()
  {
    return this.m_cinventoryid;
  }

  public String getCinventorycode()
  {
    return this.m_cinventorycode;
  }

  public String getInvname()
  {
    return this.m_invname;
  }

  public String getInvspec()
  {
    return this.m_invspec;
  }

  public String getInvtype()
  {
    return this.m_invtype;
  }

  public String getPk_measdoc()
  {
    return this.m_pk_measdoc;
  }

  public String getMeasdocname()
  {
    return this.m_measdocname;
  }

  public String getCastunitid()
  {
    return this.m_castunitid;
  }

  public String getCastunitname()
  {
    return this.m_castunitname;
  }

  public UFDouble getHsl()
  {
    return this.m_hsl;
  }

  public Integer getIsLotMgt()
  {
    return this.m_isLotMgt;
  }

  public Integer getIsSerialMgt()
  {
    return this.m_isSerialMgt;
  }

  public Integer getIsValidateMgt()
  {
    return this.m_isValidateMgt;
  }

  public Integer getIsAstUOMmgt()
  {
    return this.m_isAstUOMmgt;
  }

  public Integer getIsFreeItemMgt()
  {
    return this.m_isFreeItemMgt;
  }

  public Integer getIsSet()
  {
    return this.m_isSet;
  }

  public String getDefaultAstUOM()
  {
    return this.m_defaultAstUOM;
  }

  public Integer getIsSellProxy()
  {
    return this.m_isSellProxy;
  }

  public Integer getIssupplierstock() {
    return this.m_issupplierstock;
  }

  public Integer getIsmngstockbygrswt() {
    return this.m_ismngstockbygrswt;
  }

  public Integer getQualityDay()
  {
    return this.m_qualityDay;
  }

  public Integer getQualityperiodunit()
  {
    return this.m_qualityperiodunit;
  }

  public void setQualityperiodunit(Integer iqualityperiodunit)
  {
    this.m_qualityperiodunit = iqualityperiodunit;
  }

  public Integer getIsSolidConvRate()
  {
    return this.m_isSolidConvRate;
  }

  public Integer getIsStoreByConvert() {
    return this.m_isstorebyconvert;
  }

  public String getVbatchcode()
  {
    return this.m_vbatchcode;
  }

  public UFDate getDvalidate()
  {
    return this.m_dvalidate;
  }

  public UFDouble getNinnum()
  {
    return this.m_ninnum;
  }

  public UFDouble getNingrossnum() {
    return this.m_ningrossnum;
  }

  public UFDouble getNoutgrossnum() {
    return this.m_noutgrossnum;
  }

  public UFDouble getNinassistnum()
  {
    return this.m_ninassistnum;
  }

  public UFDouble getNoutnum()
  {
    return this.m_noutnum;
  }

  public UFDouble getNoutassistnum()
  {
    return this.m_noutassistnum;
  }

  public UFDouble getNplannedprice()
  {
    return this.m_nplannedprice;
  }

  public Integer getFlargess()
  {
    return this.m_flargess;
  }

  public Integer getFaccflag()
  {
    return this.m_faccflag;
  }

  public Integer getFchecked()
  {
    return this.m_fchecked;
  }

  public UFDouble getBkxcl()
  {
    return this.m_bkxcl;
  }

  public UFDouble getXczl()
  {
    return this.m_xczl;
  }

  public UFDouble getNmaxstocknum()
  {
    return this.m_nmaxstocknum;
  }

  public UFDouble getNminstocknum()
  {
    return this.m_nminstocknum;
  }

  public UFDouble getNorderpointnum()
  {
    return this.m_norderpointnum;
  }

  public UFDouble getNsafestocknum()
  {
    return this.m_nsafestocknum;
  }

  public Integer getInvReservedPty()
  {
    return this.m_invReservedPty;
  }
  
  
  /**add by ouyangzhb 2012-04-24 自定义项的定义*/
  public String getVdef1() {
  	return m_vdef1;
  }

  public String getVdef2() {
  	return m_vdef2;
  }

  public String getVdef3() {
  	return m_vdef3;
  }

  public String getVdef4() {
  	return m_vdef4;
  }

  public String getVdef5() {
  	return m_vdef5;
  }

  public String getVdef6() {
  	return m_vdef6;
  }

  public String getVdef7() {
  	return m_vdef7;
  }

  public String getVdef8() {
  	return m_vdef8;
  }

  public String getVdef9() {
  	return m_vdef9;
  }

  public String getVdef10() {
  	return m_vdef10;
  }

  public String getVdef11() {
  	return m_vdef11;
  }


  public void setVdef1(String newVdef1) {

  	m_vdef1 = newVdef1;
  }

  public void setVdef2(String newVdef2) {

  	m_vdef2 = newVdef2;
  }
  public void setVdef3(String newVdef3) {

  	m_vdef3 = newVdef3;
  }
  public void setVdef4(String newVdef4) {

  	m_vdef4 = newVdef4;
  }
  public void setVdef5(String newVdef5) {

  	m_vdef5 = newVdef5;
  }
  public void setVdef6(String newVdef6) {

  	m_vdef6 = newVdef6;
  }
  public void setVdef7(String newVdef7) {

  	m_vdef7 = newVdef7;
  }
  public void setVdef8(String newVdef8) {

  	m_vdef8 = newVdef8;
  }
  public void setVdef9(String newVdef9) {

  	m_vdef9 = newVdef9;
  }
  public void setVdef10(String newVdef10) {

  	m_vdef10 = newVdef10;
  }
  public void setVdef11(String newVdef11) {

  	m_vdef11 = newVdef11;
  }
  /**add by ouyangzhb 2012-04-24 自定义项的定义 end */
  

  public void setPk(String newPk)
  {
    this.m_pk = newPk;
  }

  public void setCinventoryid(String newCinventoryid)
  {
    this.m_cinventoryid = newCinventoryid;
  }

  public void setCinventorycode(String newCinventorycode)
  {
    this.m_cinventorycode = newCinventorycode;
  }

  public void setInvname(String newInvname)
  {
    this.m_invname = newInvname;
  }

  public void setInvspec(String newInvspec)
  {
    this.m_invspec = newInvspec;
  }

  public void setInvtype(String newInvtype)
  {
    this.m_invtype = newInvtype;
  }

  public void setPk_measdoc(String newPk_measdoc)
  {
    this.m_pk_measdoc = newPk_measdoc;
  }

  public void setMeasdocname(String newMeasdocname)
  {
    this.m_measdocname = newMeasdocname;
  }

  public void setCastunitid(String newCastunitid)
  {
    this.m_castunitid = newCastunitid;
  }

  public void setCastunitname(String newCastunitname)
  {
    this.m_castunitname = newCastunitname;
  }

  public void setHsl(UFDouble newHsl)
  {
    this.m_hsl = newHsl;
  }

  public void setIsLotMgt(Integer newIsLotMgt)
  {
    this.m_isLotMgt = newIsLotMgt;
  }

  public void setIsSerialMgt(Integer newIsSerialMgt)
  {
    this.m_isSerialMgt = newIsSerialMgt;
  }

  public void setIsValidateMgt(Integer newIsValidateMgt)
  {
    this.m_isValidateMgt = newIsValidateMgt;
  }

  public void setIsAstUOMmgt(Integer newIsAstUOMmgt)
  {
    this.m_isAstUOMmgt = newIsAstUOMmgt;
  }

  public void setIsFreeItemMgt(Integer newIsFreeItemMgt)
  {
    this.m_isFreeItemMgt = newIsFreeItemMgt;
  }

  public void setIsSet(Integer newIsSet)
  {
    this.m_isSet = newIsSet;
  }

  public void setDefaultAstUOM(String newDefaultAstUOM)
  {
    this.m_defaultAstUOM = newDefaultAstUOM;
  }

  public void setIsSellProxy(Integer newIsSellProxy)
  {
    this.m_isSellProxy = newIsSellProxy;
  }

  public void setQualityDay(Integer newQualityDay)
  {
    this.m_qualityDay = newQualityDay;
  }

  public void setIsSolidConvRate(Integer newIsSolidConvRate)
  {
    this.m_isSolidConvRate = newIsSolidConvRate;
  }

  public void setIsStoreByConvert(Integer newIsStoreByConvert) {
    this.m_isstorebyconvert = newIsStoreByConvert;
  }
  public void setIssupplierstock(Integer newIssupplierstock) {
    this.m_issupplierstock = newIssupplierstock;
  }

  public void setIsmngstockbygrswt(Integer newIsmngstockbygrswt) {
    this.m_ismngstockbygrswt = newIsmngstockbygrswt;
  }

  public void setVbatchcode(String newVbatchcode)
  {
    this.m_vbatchcode = newVbatchcode;
  }

  public void setDvalidate(UFDate newDvalidate)
  {
    this.m_dvalidate = newDvalidate;
  }

  public void setNinnum(UFDouble newNinnum)
  {
    this.m_ninnum = newNinnum;
  }

  public void setNingrossnum(UFDouble newNingrossnum)
  {
    this.m_ningrossnum = newNingrossnum;
  }

  public void setNoutgrossnum(UFDouble newNoutgrossnum)
  {
    this.m_noutgrossnum = newNoutgrossnum;
  }

  public void setNinassistnum(UFDouble newNinassistnum)
  {
    this.m_ninassistnum = newNinassistnum;
  }

  public void setNoutnum(UFDouble newNoutnum)
  {
    this.m_noutnum = newNoutnum;
  }

  public void setNoutassistnum(UFDouble newNoutassistnum)
  {
    this.m_noutassistnum = newNoutassistnum;
  }

  public void setNplannedprice(UFDouble newNplannedprice)
  {
    this.m_nplannedprice = newNplannedprice;
  }

  public void setFlargess(Integer newFlargess)
  {
    this.m_flargess = newFlargess;
  }

  public void setFaccflag(Integer newFaccflag)
  {
    this.m_faccflag = newFaccflag;
  }

  public void setFchecked(Integer newFchecked)
  {
    this.m_fchecked = newFchecked;
  }

  public void setBkxcl(UFDouble newBkxcl)
  {
    this.m_bkxcl = newBkxcl;
  }

  public void setXczl(UFDouble newXczl)
  {
    this.m_xczl = newXczl;
  }

  public void setNmaxstocknum(UFDouble newNmaxstocknum)
  {
    this.m_nmaxstocknum = newNmaxstocknum;
  }

  public void setNminstocknum(UFDouble newNminstocknum)
  {
    this.m_nminstocknum = newNminstocknum;
  }

  public void setNorderpointnum(UFDouble newNorderpointnum)
  {
    this.m_norderpointnum = newNorderpointnum;
  }

  public void setNsafestocknum(UFDouble newNsafestocknum)
  {
    this.m_nsafestocknum = newNsafestocknum;
  }

  public void setInvReservedPty(Integer newInvReservedPty)
  {
    this.m_invReservedPty = newInvReservedPty;
  }

  public void validate()
    throws ValidationException
  {
    ArrayList errFields = new ArrayList();

    if (this.m_pk == null) {
      errFields.add(new String("m_pk"));
    }

    StringBuffer message = new StringBuffer();
    message.append("下列字段不能为空：");
    if (errFields.size() > 0) {
      String[] temp = (String[])(String[])errFields.toArray(new String[0]);
      message.append(temp[0]);
      for (int i = 1; i < temp.length; ++i) {
        message.append("、");
        message.append(temp[i]);
      }

      throw new NullFieldException(message.toString());
    }
  }

  public String[] getAttributeNames()
  {
    return new String[] { "cinventoryid", "cinvmanid", "cinventorycode", "invname", "invspec", "invtype", "pk_measdoc", "measdocname", "castunitid", "castunitname", "hsl", "isLotMgt", "isSerialMgt", "isValidateMgt", "isAstUOMmgt", "isFreeItemMgt", "isSet", "standStoreUOM", "defaultAstUOM", "isSellProxy", "qualityDay", "isSolidConvRate", "vbatchcode", "dvalidate", "ninnum", "ninassistnum", "noutnum", "noutassistnum", "nplannedprice", "flargess", "faccflag", "fchecked", "bkxcl", "xczl", "nmaxstocknum", "nminstocknum", "norderpointnum", "nsafestocknum", "vfree0", "invReservedPty", "freeItem", "fbillrowflag", "invsetparttype", "desl", "childsnum", "partpercent", "locatorid", "locatorname", "vfree0", "vfree1", "vfree2", "vfree3", "vfree4", "vfree5", "vfree6", "vfree7", "vfree8", "vfree9", "vfree10", "cspaceid", "cspacecode", "cspacename", "discountflag", "laborflag", "sellproxyflag", "childsnum", "iscancalculatedinvcost", "isprimarybarcode", "issecondarybarcode", "pk_packsort", "npacknum", "vpacktypename", "unitweight", "unitvolume", "cckjlc", "crkjlc", "issupplierstock", "ismngstockbygrswt", "ningrossnum", "noutgrossnum", "isstorebyconvert", "qualityperiodunit", "isAsset" 
    		//add by ouyangzhb 2012-04-24 增加自定义项
    		, "vdef1",
    		    "vdef2",
    		    "vdef3",
    		    "vdef4",
    		    "vdef5",
    		    "vdef6",
    		    "vdef7",
    		    "vdef8",
    		    "vdef9",
    		    "vdef10",
    		    "vdef11"
    
    
    };
  }

  public Object getAttributeValue(String attributeName)
  {
    if (attributeName.equals("pk"))
      return this.m_pk;
    if (attributeName.equals("cinventoryid"))
      return this.m_cinventoryid;
    if (attributeName.equals("cinvmanid"))
      return this.m_cinvmanid;
    if (attributeName.equals("cinventorycode"))
      return this.m_cinventorycode;
    if (attributeName.equals("invname"))
      return this.m_invname;
    if (attributeName.equals("invspec"))
      return this.m_invspec;
    if (attributeName.equals("invtype"))
      return this.m_invtype;
    if (attributeName.equals("pk_measdoc"))
      return this.m_pk_measdoc;
    if (attributeName.equals("measdocname"))
      return this.m_measdocname;
    if (attributeName.equals("castunitid"))
      return this.m_castunitid;
    if (attributeName.equals("castunitname"))
      return this.m_castunitname;
    if (attributeName.equals("hsl"))
      return this.m_hsl;
    if (attributeName.equals("isLotMgt"))
      return this.m_isLotMgt;
    if (attributeName.equals("isSerialMgt"))
      return this.m_isSerialMgt;
    if (attributeName.equals("isValidateMgt"))
      return this.m_isValidateMgt;
    if (attributeName.equals("isAstUOMmgt"))
      return this.m_isAstUOMmgt;
    if (attributeName.equals("isFreeItemMgt"))
      return this.m_isFreeItemMgt;
    if (attributeName.equals("isSet"))
      return this.m_isSet;
    if (attributeName.equals("standStoreUOM"))
      return this.m_standStoreUOM;
    if (attributeName.equals("storeunitnum"))
      return this.m_standStoreUOM;
    if (attributeName.equals("defaultAstUOM"))
      return this.m_defaultAstUOM;
    if (attributeName.equals("isSellProxy"))
      return this.m_isSellProxy;
    if (attributeName.equals("qualityDay"))
      return this.m_qualityDay;
    if (attributeName.equals("isSolidConvRate")) {
      return this.m_isSolidConvRate;
    }
    if (attributeName.equals("islotmgt"))
      return this.m_isLotMgt;
    if (attributeName.equals("isserialmgt"))
      return this.m_isSerialMgt;
    if (attributeName.equals("isvalidatemgt"))
      return this.m_isValidateMgt;
    if (attributeName.equals("isastuommgt"))
      return this.m_isAstUOMmgt;
    if (attributeName.equals("isfreeitemmgt"))
      return this.m_isFreeItemMgt;
    if (attributeName.equals("isset"))
      return this.m_isSet;
    if (attributeName.equals("standstoreuom"))
      return this.m_standStoreUOM;
    if (attributeName.equals("defaultastuom"))
      return this.m_defaultAstUOM;
    if (attributeName.equals("issellproxy"))
      return this.m_isSellProxy;
    if (attributeName.equals("qualityday"))
      return this.m_qualityDay;
    if (attributeName.equals("issolidconvrate"))
      return this.m_isSolidConvRate;
    if (attributeName.equals("vbatchcode"))
      return this.m_vbatchcode;
    if (attributeName.equals("dvalidate"))
      return this.m_dvalidate;
    if ((attributeName.equals("scrq")) || (attributeName.equals("producedate")))
      return this.m_dproducedate;
    if (attributeName.equals("ninnum"))
      return this.m_ninnum;
    if (attributeName.equals("ninassistnum"))
      return this.m_ninassistnum;
    if (attributeName.equals("noutnum"))
      return this.m_noutnum;
    if (attributeName.equals("noutassistnum"))
      return this.m_noutassistnum;
    if (attributeName.equals("nonhandnum"))
      return this.m_nonhandnum;
    if (attributeName.equals("nonhandassistnum"))
      return this.m_nonhandassistnum;
    if (attributeName.equals("nonhandgrossnum")) {
      return this.m_nonhandgrossnum;
    }
    if (attributeName.equals("nplannedprice"))
      return this.m_nplannedprice;
    if (attributeName.equals("flargess"))
      return this.m_flargess;
    if (attributeName.equals("faccflag"))
      return this.m_faccflag;
    if (attributeName.equals("fchecked"))
      return this.m_fchecked;
    if (attributeName.equals("bkxcl"))
      return this.m_bkxcl;
    if (attributeName.equals("xczl"))
      return this.m_xczl;
    if (attributeName.equals("chzl"))
      return this.m_chzl;
    if (attributeName.equals("nmaxstocknum"))
      return this.m_nmaxstocknum;
    if (attributeName.equals("nminstocknum"))
      return this.m_nminstocknum;
    if (attributeName.equals("norderpointnum"))
      return this.m_norderpointnum;
    if (attributeName.equals("nsafestocknum"))
      return this.m_nsafestocknum;
    if (attributeName.equals("invReservedPty"))
      return this.m_invReservedPty;
    if (attributeName.equals("fbillrowflag"))
      return this.m_fbillrowflag;
    if (attributeName.equals("autobalancemeas")) {
      return this.m_autobalancemeas;
    }

    if (attributeName.equals("cspecialbid")) {
      return this.m_cspecialbid;
    }

    if (attributeName.equals("invsetparttype"))
      return getInvsetparttype();
    if ((attributeName.equals("childsnum")) || (attributeName.equals("desl")))
      return this.m_childsnum;
    if (attributeName.equals("partpercent"))
      return this.m_partpercent;
    if (attributeName.equals("locatorid"))
      return this.m_cspaceid;
    if (attributeName.equals("locatorname"))
      return this.m_cspacename;
    if (attributeName.equals("pk_invcl"))
      return this.m_pk_invcl;
    if (attributeName.equals("invclasscode"))
      return this.m_invclasscode;
    if (attributeName.equals("invclassname"))
      return this.m_invclassname;
    if (attributeName.equals("invclasslev"))
      return this.m_invclasslev;
    if (attributeName.equals("ccorrespondcode"))
      return this.m_ccorrespondcode;
    if (attributeName.equals("ccorrespondbid"))
      return this.m_ccorrespondbid;
    if (attributeName.equals("ccorrespondhid"))
      return this.m_ccorrespondhid;
    if (attributeName.equals("ccorrespondtype"))
      return this.m_ccorrespondtype;
    if (attributeName.equals("ccorrespondtypename"))
      return this.m_ccorrespondtypename;
    if (attributeName.equals("cgeneralbid"))
      return this.m_cgeneralbid;
    if (attributeName.equals("cgeneralhid"))
      return this.m_cgeneralhid;
    if (attributeName.equals("pk_corp"))
      return this.m_pk_corp;
    if (attributeName.equals("cprojectid"))
      return this.m_cprojectid;
    if (attributeName.equals("cprojectname"))
      return this.m_cprojectname;
    if (attributeName.equals("cprojectphaseid"))
      return this.m_cprojectphaseid;
    if (attributeName.equals("cprojectcode"))
      return this.m_cprojectcode;
    if (attributeName.equals("cprojectphasecode"))
      return this.m_cprojectphasecode;
    if (attributeName.equals("cprojectphasename"))
      return this.m_cprojectphasename;
    if (attributeName.equals("keepwasterate"))
      return this.m_keepwasterate;
    if (attributeName.equals("icheckcycle"))
      return this.m_icheckcycle;
    if (attributeName.equals("discountflag"))
      return this.m_discountflag;
    if (attributeName.equals("isused"))
      return this.m_isused;
    if (attributeName.equals("laborflag"))
      return this.m_laborflag;
    if (attributeName.equals("outpriority"))
      return this.m_outpriority;
    if (attributeName.equals("outtrackin"))
      return this.m_outtrackin;
    if (attributeName.equals("negallowed"))
      return this.m_negallowed;
    if (attributeName.equals("iscancalculatedinvcost")) {
      return this.m_iscancalculatedinvcost;
    }
    if (attributeName.equals("cfreezeid"))
      return this.m_cfreezeid;
    if (attributeName.equals("cspaceid"))
      return this.m_cspaceid;
    if (attributeName.equals("cspacecode"))
      return this.m_cspacecode;
    if (attributeName.equals("cspacename"))
      return this.m_cspacename;
    if (attributeName.equals("sellproxyflag")) {
      return this.m_sellproxyflag;
    }
    if (attributeName.equals("isprimarybarcode")) {
      return this.m_Isprimarybarcode;
    }
    if (attributeName.equals("issecondarybarcode"))
      return this.m_Issecondarybarcode;
    if (attributeName.equals("npacknum"))
      return this.m_npacknum;
    if (attributeName.equals("pk_packsort"))
      return this.m_pk_packsort;
    if (attributeName.equals("vpacktypename"))
      return this.m_vpacktypename;
    if (attributeName.equals("unitweight"))
      return this.m_nunitweight;
    if (attributeName.equals("unitvolume"))
      return this.m_nunitvolume;
    if (attributeName.equals("cckjlc"))
      return this.m_Cckjlc;
    if (attributeName.equals("crkjlc"))
      return this.m_Crkjlc;
    if (attributeName.equals("issupplierstock"))
      return this.m_issupplierstock;
    if (attributeName.equals("ismngstockbygrswt"))
      return this.m_ismngstockbygrswt;
    if (attributeName.equals("ningrossnum"))
      return this.m_ningrossnum;
    if (attributeName.equals("noutgrossnum"))
      return this.m_noutgrossnum;
    if (attributeName.equals("isstorebyconvert"))
      return this.m_isstorebyconvert;
    if (attributeName.equals("cvendorid"))
      return this.m_cvendorid;
    if (attributeName.equals("qualityperiodunit")) {
      return this.m_qualityperiodunit;
    }
    if (attributeName.equals("isAsset")) {
      return this.m_isAsset;
    }
    /**add by ouyangzhb 2012-04-24 支持自定义项的取值*/
    else if(attributeName.equals("vdef1"))
  	    return m_vdef1;
    else if(attributeName.equals("vdef2"))
  	    return m_vdef2;
    else if(attributeName.equals("vdef3"))
  	    return m_vdef3;
    else if(attributeName.equals("vdef4"))
  	    return m_vdef4;
    else if(attributeName.equals("vdef5"))
  	    return m_vdef5;
    else if(attributeName.equals("vdef6"))
  	    return m_vdef6;
    else if(attributeName.equals("vdef7"))
  	    return m_vdef7;
    else if(attributeName.equals("vdef8"))
  	    return m_vdef8;
    else if(attributeName.equals("vdef9"))
  	    return m_vdef9;
    else if(attributeName.equals("vdef10"))
  	    return m_vdef10;
    else if(attributeName.equals("vdef11"))
  	    return m_vdef11;

    return getFreeItemValue(attributeName);
  }

  public void setAttributeValue(String name, Object value)
  {
    String sTrimedValue = null;

    if ((value != null) && 
      (value instanceof String)) {
      sTrimedValue = (String)value;
      if (sTrimedValue.trim().length() == 0)
        sTrimedValue = null;
    }
    try
    {
      if (name.equals("pk"))
        this.m_pk = sTrimedValue;
      else if (name.equals("cspecialbid"))
        this.m_cspecialbid = sTrimedValue;
      else if (name.equals("cinventoryid"))
        this.m_cinventoryid = sTrimedValue;
      else if (name.equals("cinvmanid"))
        this.m_cinvmanid = sTrimedValue;
      else if (name.equals("cinventorycode"))
        this.m_cinventorycode = sTrimedValue;
      else if (name.equals("invname"))
        this.m_invname = sTrimedValue;
      else if (name.equals("invspec"))
        this.m_invspec = sTrimedValue;
      else if (name.equals("invtype"))
        this.m_invtype = sTrimedValue;
      else if (name.equals("pk_measdoc"))
        this.m_pk_measdoc = sTrimedValue;
      else if (name.equals("measdocname"))
        this.m_measdocname = sTrimedValue;
      else if (name.equals("castunitid"))
        this.m_castunitid = sTrimedValue;
      else if (name.equals("castunitname"))
        this.m_castunitname = sTrimedValue;
      else if (name.equals("hsl"))
        this.m_hsl = SwitchObject.switchObjToUFDouble(value);
      else if (name.equals("isLotMgt"))
        this.m_isLotMgt = SwitchObject.switchYNToInteger(value);
      else if (name.equals("isSerialMgt"))
        this.m_isSerialMgt = SwitchObject.switchYNToInteger(value);
      else if (name.equals("isValidateMgt"))
        this.m_isValidateMgt = SwitchObject.switchYNToInteger(value);
      else if (name.equals("isAstUOMmgt"))
        this.m_isAstUOMmgt = SwitchObject.switchYNToInteger(value);
      else if (name.equals("isFreeItemMgt"))
        this.m_isFreeItemMgt = SwitchObject.switchYNToInteger(value);
      else if (name.equals("isSet"))
        this.m_isSet = SwitchObject.switchYNToInteger(value);
      else if (name.equals("standStoreUOM"))
        this.m_standStoreUOM = SwitchObject.switchObjToUFDouble(value);
      else if (name.equals("defaultAstUOM")) {
        this.m_defaultAstUOM = sTrimedValue;
      }
      else if (name.equals("iscancalculatedinvcost")) {
        this.m_iscancalculatedinvcost = SwitchObject.switchObjToUFBoolean(value);
      }
      else if (name.equals("isSellProxy"))
        this.m_isSellProxy = SwitchObject.switchYNToInteger(value);
      else if (name.equals("qualityDay")) {
        this.m_qualityDay = SwitchObject.switchObjToInteger(value);
      }
      else if (name.equals("isSolidConvRate"))
        this.m_isSolidConvRate = SwitchObject.switchYNToInteger(value);
      else if (name.equals("islotmgt"))
        this.m_isLotMgt = SwitchObject.switchYNToInteger(value);
      else if (name.equals("isserialmgt"))
        this.m_isSerialMgt = SwitchObject.switchYNToInteger(value);
      else if (name.equals("isvalidatemgt"))
        this.m_isValidateMgt = SwitchObject.switchYNToInteger(value);
      else if (name.equals("isastuommgt"))
        this.m_isAstUOMmgt = SwitchObject.switchYNToInteger(value);
      else if (name.equals("isfreeitemmgt"))
        this.m_isFreeItemMgt = SwitchObject.switchYNToInteger(value);
      else if (name.equals("isset"))
        this.m_isSet = SwitchObject.switchYNToInteger(value);
      else if (name.equals("standstoreuom")) {
        this.m_standStoreUOM = SwitchObject.switchObjToUFDouble(value);
      }
      else if (name.equals("storeunitnum")) {
        this.m_standStoreUOM = SwitchObject.switchObjToUFDouble(value);
      }
      else if (name.equals("defaultastuom"))
        this.m_defaultAstUOM = sTrimedValue;
      else if (name.equals("issellproxy"))
        this.m_isSellProxy = SwitchObject.switchYNToInteger(value);
      else if (name.equals("qualityday"))
        this.m_qualityDay = SwitchObject.switchObjToInteger(value);
      else if (name.equals("issolidconvrate"))
        this.m_isSolidConvRate = SwitchObject.switchYNToInteger(value);
      else if (name.equals("vbatchcode"))
        this.m_vbatchcode = sTrimedValue;
      else if (name.equals("dvalidate"))
        this.m_dvalidate = SwitchObject.switchObjToUFDate(value);
      else if ((name.equals("producedate")) || (name.equals("scrq")))
        this.m_dproducedate = SwitchObject.switchObjToUFDate(value);
      else if (name.equals("ninnum"))
        this.m_ninnum = SwitchObject.switchObjToUFDouble(value);
      else if (name.equals("ninassistnum"))
        this.m_ninassistnum = SwitchObject.switchObjToUFDouble(value);
      else if (name.equals("noutnum"))
        this.m_noutnum = SwitchObject.switchObjToUFDouble(value);
      else if (name.equals("noutassistnum"))
        this.m_noutassistnum = SwitchObject.switchObjToUFDouble(value);
      else if (name.equals("nonhandnum"))
        this.m_nonhandnum = SwitchObject.switchObjToUFDouble(value);
      else if (name.equals("nonhandassistnum"))
        this.m_nonhandassistnum = SwitchObject.switchObjToUFDouble(value);
      else if (name.equals("nonhandgrossnum")) {
        this.m_nonhandgrossnum = SwitchObject.switchObjToUFDouble(value);
      }
      else if (name.equals("nplannedprice"))
        this.m_nplannedprice = SwitchObject.switchObjToUFDouble(value);
      else if (name.equals("flargess"))
        this.m_flargess = SwitchObject.switchYNToInteger(value);
      else if (name.equals("faccflag"))
        this.m_faccflag = SwitchObject.switchYNToInteger(value);
      else if (name.equals("fchecked"))
        this.m_fchecked = SwitchObject.switchYNToInteger(value);
      else if (name.equals("bkxcl"))
        this.m_bkxcl = SwitchObject.switchObjToUFDouble(value);
      else if (name.equals("xczl"))
        this.m_xczl = SwitchObject.switchObjToUFDouble(value);
      else if (name.equals("chzl"))
        this.m_chzl = SwitchObject.switchObjToUFDouble(value);
      else if (name.equals("nmaxstocknum"))
        this.m_nmaxstocknum = SwitchObject.switchObjToUFDouble(value);
      else if (name.equals("nminstocknum"))
        this.m_nminstocknum = SwitchObject.switchObjToUFDouble(value);
      else if (name.equals("norderpointnum"))
        this.m_norderpointnum = SwitchObject.switchObjToUFDouble(value);
      else if (name.equals("nsafestocknum"))
        this.m_nsafestocknum = SwitchObject.switchObjToUFDouble(value);
      else if (name.equals("invReservedPty"))
        this.m_invReservedPty = SwitchObject.switchObjToInteger(value);
      else if (name.equals("invreservedpty"))
        this.m_invReservedPty = SwitchObject.switchObjToInteger(value);
      else if (name.equals("fbillrowflag"))
        this.m_fbillrowflag = SwitchObject.switchObjToInteger(value);
      else if (name.equals("invsetparttype"))
        this.m_invsetparttype = sTrimedValue;
      else if ((name.equals("childsnum")) || (name.equals("desl")))
        this.m_childsnum = SwitchObject.switchObjToUFDouble(value);
      else if (name.equals("partpercent"))
        this.m_partpercent = SwitchObject.switchObjToUFDouble(value);
      else if (name.equals("locatorid"))
        this.m_cspaceid = sTrimedValue;
      else if (name.equals("locatorname"))
        this.m_cspacename = sTrimedValue;
      else if (name.equals("pk_invcl"))
        this.m_pk_invcl = sTrimedValue;
      else if (name.equals("invclasscode"))
        this.m_invclasscode = sTrimedValue;
      else if (name.equals("invclassname"))
        this.m_invclassname = sTrimedValue;
      else if (name.equals("invclasslev"))
        this.m_invclasslev = sTrimedValue;
      else if (name.equals("ccorrespondbid"))
        this.m_ccorrespondbid = sTrimedValue;
      else if (name.equals("ccorrespondcode"))
        this.m_ccorrespondcode = sTrimedValue;
      else if (name.equals("ccorrespondhid"))
        this.m_ccorrespondhid = sTrimedValue;
      else if (name.equals("ccorrespondtype"))
        this.m_ccorrespondtype = sTrimedValue;
      else if (name.equals("ccorrespondtypename"))
        this.m_ccorrespondtypename = sTrimedValue;
      else if (name.equals("cgeneralbid"))
        this.m_cgeneralbid = sTrimedValue;
      else if (name.equals("cgeneralhid"))
        this.m_cgeneralhid = sTrimedValue;
      else if (name.equals("pk_corp"))
        this.m_pk_corp = sTrimedValue;
      else if (name.equals("cprojectid"))
        this.m_cprojectid = sTrimedValue;
      else if (name.equals("cprojectname"))
        this.m_cprojectname = sTrimedValue;
      else if (name.equals("cprojectphaseid"))
        this.m_cprojectphaseid = sTrimedValue;
      else if (name.equals("cprojectcode"))
        this.m_cprojectcode = sTrimedValue;
      else if (name.equals("cprojectphasecode"))
        this.m_cprojectphasecode = sTrimedValue;
      else if (name.equals("cprojectphasename"))
        this.m_cprojectphasename = sTrimedValue;
      else if (name.equals("cspaceid"))
        this.m_cspaceid = sTrimedValue;
      else if (name.equals("cspacecode"))
        this.m_cspacecode = sTrimedValue;
      else if (name.equals("cspacename"))
        this.m_cspacename = sTrimedValue;
      else if (name.equals("keepwasterate"))
        this.m_keepwasterate = SwitchObject.switchObjToUFDouble(value);
      else if (name.equals("icheckcycle"))
        this.m_icheckcycle = SwitchObject.switchObjToInteger(value);
      else if (name.equals("discountflag"))
        this.m_discountflag = SwitchObject.switchObjToUFBoolean(value);
      else if (name.equals("autobalancemeas")) {
        this.m_autobalancemeas = SwitchObject.switchObjToUFBoolean(value);
      }
      else if (name.equals("laborflag"))
        this.m_laborflag = SwitchObject.switchObjToUFBoolean(value);
      else if (name.equals("isused"))
        this.m_isused = SwitchObject.switchObjToUFBoolean(value);
      else if (name.equals("outpriority"))
        this.m_outpriority = SwitchObject.switchObjToInteger(value);
      else if (name.equals("outtrackin"))
        this.m_outtrackin = SwitchObject.switchObjToUFBoolean(value);
      else if (name.equals("negallowed"))
        this.m_negallowed = SwitchObject.switchObjToUFBoolean(value);
      else if (name.equals("cfreezeid"))
        this.m_cfreezeid = sTrimedValue;
      else if (name.equals("sellproxyflag"))
        this.m_sellproxyflag = SwitchObject.switchObjToUFBoolean(value);
      else if (name.equals("pk_packsort"))
        this.m_pk_packsort = sTrimedValue;
      else if (name.equals("vpacktypename"))
        this.m_vpacktypename = sTrimedValue;
      else if (name.equals("npacknum"))
        this.m_npacknum = SwitchObject.switchObjToUFDouble(value);
      else if (name.equals("unitweight"))
        this.m_nunitweight = SwitchObject.switchObjToUFDouble(value);
      else if (name.equals("unitvolume")) {
        this.m_nunitvolume = SwitchObject.switchObjToUFDouble(value);
      }
      else if (name.equals("isprimarybarcode")) {
        this.m_Isprimarybarcode = SwitchObject.switchObjToUFBoolean(value);
      }
      else if (name.equals("issecondarybarcode"))
        this.m_Issecondarybarcode = SwitchObject.switchObjToUFBoolean(value);
      else if (name.equals("cckjlc"))
        this.m_Cckjlc = sTrimedValue;
      else if (name.equals("crkjlc"))
        this.m_Crkjlc = sTrimedValue;
      else if (name.equals("issupplierstock"))
        this.m_issupplierstock = SwitchObject.switchYNToInteger(value);
      else if (name.equals("ismngstockbygrswt"))
        this.m_ismngstockbygrswt = SwitchObject.switchYNToInteger(value);
      else if (name.equals("ningrossnum"))
        this.m_ningrossnum = SwitchObject.switchObjToUFDouble(value);
      else if (name.equals("noutgrossnum"))
        this.m_noutgrossnum = SwitchObject.switchObjToUFDouble(value);
      else if (name.equals("isstorebyconvert"))
        this.m_isstorebyconvert = SwitchObject.switchYNToInteger(value);
      else if (name.equals("cvendorid"))
        this.m_cvendorid = sTrimedValue;
      else if (name.equals("qualityperiodunit")) {
        this.m_qualityperiodunit = SwitchObject.switchObjToInteger(value);
      }
      else if (name.equals("isAsset")) {
        this.m_isAsset = SwitchObject.switchYNToInteger(value);
      }
      
      /**add by ouyangzhb 2012-04-24 自定义项的支持  */
      else if (name.equals("vdef1")) {
		m_vdef1 = sTrimedValue;
	} else if (name.equals("vdef2")) {
		m_vdef2 = sTrimedValue;
	} else if (name.equals("vdef3")) {
		m_vdef3 = sTrimedValue;
	} else if (name.equals("vdef4")) {
		m_vdef4 = sTrimedValue;
	} else if (name.equals("vdef5")) {
		m_vdef5 = sTrimedValue;
	} else if (name.equals("vdef6")) {
		m_vdef6 = sTrimedValue;
	} else if (name.equals("vdef7")) {
		m_vdef7 = sTrimedValue;
	} else if (name.equals("vdef8")) {
		m_vdef8 = sTrimedValue;
	} else if (name.equals("vdef9")) {
		m_vdef9 = sTrimedValue;
	} else if (name.equals("vdef10")) {
		m_vdef10 = sTrimedValue;
	} else if (name.equals("vdef11")) {
		m_vdef11 = sTrimedValue;
	/**add by ouyangzhb 2012-04-24 自定义项的支持 end */
      
	}else if (value != null)
        setFreeItemValue(name, value.toString());
      else
        setFreeItemValue(name, null);
    } catch (ClassCastException e) {
      throw new ClassCastException("setAttributeValue方法中为 " + name + " 赋值时类型转换错误！");
    }
  }

  public void calPrdDate()
  {
    if (((this.m_dproducedate != null) && (this.m_dproducedate.toString().trim().length() != 0)) || (this.m_dvalidate == null) || (this.m_qualityDay == null)) {
      return;
    }
    this.m_dproducedate = this.m_dvalidate.getDateBefore(this.m_qualityDay.intValue());
  }

  public String getCcorrespondbid()
  {
    return this.m_ccorrespondbid;
  }

  public String getCcorrespondcode()
  {
    return this.m_ccorrespondcode;
  }

  public String getCcorrespondhid()
  {
    return this.m_ccorrespondhid;
  }

  public String getCcorrespondtype()
  {
    return this.m_ccorrespondtype;
  }

  public String getCcorrespondtypename()
  {
    return this.m_ccorrespondtypename;
  }

  public String getCfreezeid()
  {
    return this.m_cfreezeid;
  }

  public String getCgeneralbid()
  {
    return this.m_cgeneralbid;
  }

  public String getCspecialbid() {
    return this.m_cspecialbid;
  }

  public String getCgeneralhid()
  {
    return this.m_cgeneralhid;
  }

  public UFDouble getChildsnum()
  {
    return this.m_childsnum;
  }

  public UFDouble getChzl()
  {
    return this.m_chzl;
  }

  public String getCinvbaseid()
  {
    return this.m_cinvmanid;
  }

  public String getCinvmanid()
  {
    return this.m_cinvmanid;
  }

  public String getCprojectcode()
  {
    return this.m_cprojectcode;
  }

  public String getCprojectid()
  {
    return this.m_cprojectid;
  }

  public String getCprojectname()
  {
    return this.m_cprojectname;
  }

  public String getCprojectphasecode()
  {
    return this.m_cprojectphasecode;
  }

  public String getCprojectphaseid()
  {
    return this.m_cprojectphaseid;
  }

  public String getCprojectphasename()
  {
    return this.m_cprojectphasename;
  }

  public String getCspacecode()
  {
    return this.m_cspacecode;
  }

  public String getCckjlc()
  {
    return this.m_Cckjlc;
  }

  public String getCrkjlc()
  {
    return this.m_Crkjlc;
  }

  public void setCckjlc(String pk)
  {
    this.m_Cckjlc = pk;
  }

  public void setCrkjlc(String pk)
  {
    this.m_Crkjlc = pk;
  }

  public String getCspaceid()
  {
    return this.m_cspaceid;
  }

  public String getCvendorid() {
    return this.m_cvendorid;
  }

  public String getCspacename()
  {
    return this.m_cspacename;
  }

  public UFBoolean getDiscountflag()
  {
    if (this.m_discountflag == null)
      this.m_discountflag = new UFBoolean(false);
    return this.m_discountflag;
  }

  public UFDate getDproducedate()
  {
    return this.m_dproducedate;
  }

  public Integer getFbillrowflag()
  {
    return this.m_fbillrowflag;
  }

  public String getFreeItemValue(String sItemKey)
  {
    if ((this.m_freevo != null) && (this.m_freevo.getAttributeValue(sItemKey) != null)) {
      return this.m_freevo.getAttributeValue(sItemKey).toString();
    }
    return null;
  }

  public FreeVO getFreeItemVO()
  {
    if (this.m_freevo != null) {
      return (FreeVO)this.m_freevo.clone();
    }
    return null;
  }

  public FreeVO getRealFreeItemVO()
  {
    return this.m_freevo;
  }

  public Integer getIcheckcycle()
  {
    return this.m_icheckcycle;
  }

  public int getInOutFlag()
  {
    int iInOut = 0;

    if (((this.m_ninnum != null) && (this.m_ninnum.doubleValue() > 0.0D)) || ((this.m_noutnum != null) && (this.m_noutnum.doubleValue() < 0.0D)))
    {
      iInOut = 1;
    }
    else if (((this.m_ninnum != null) && (this.m_ninnum.doubleValue() < 0.0D)) || ((this.m_noutnum != null) && (this.m_noutnum.doubleValue() > 0.0D)))
    {
      iInOut = -1;
    }return iInOut;
  }

  public String getInvclasscode()
  {
    return this.m_invclasscode;
  }

  public String getInvclasslev()
  {
    return this.m_invclasslev;
  }

  public String getInvclassname()
  {
    return this.m_invclassname;
  }

  public String getInvsetparttype()
  {
    if (this.m_fbillrowflag != null) {
      if (this.m_fbillrowflag.intValue() == 0)
        this.m_invsetparttype = "套件";
      else if (this.m_fbillrowflag.intValue() == 1)
        this.m_invsetparttype = "配件";
      else if (this.m_fbillrowflag.intValue() == 2)
        this.m_invsetparttype = "转换前";
      else if (this.m_fbillrowflag.intValue() == 3)
        this.m_invsetparttype = "转换后";
    }
    else {
      this.m_invsetparttype = null;
    }
    return this.m_invsetparttype;
  }

  public UFBoolean getIscancalculatedinvcost()
  {
    if (this.m_iscancalculatedinvcost == null)
      this.m_iscancalculatedinvcost = new UFBoolean(false);
    return this.m_iscancalculatedinvcost;
  }

  public UFBoolean getIsprimarybarcode()
  {
    if (this.m_Isprimarybarcode == null)
      this.m_Isprimarybarcode = new UFBoolean(false);
    return this.m_Isprimarybarcode;
  }

  public UFBoolean getIssecondarybarcode()
  {
    if (this.m_Issecondarybarcode == null) {
      this.m_Issecondarybarcode = new UFBoolean(false);
    }
    return this.m_Issecondarybarcode;
  }

  public UFBoolean getIsused()
  {
    if (this.m_isused == null)
      this.m_isused = new UFBoolean(false);
    return this.m_isused;
  }

  public UFDouble getKeepwasterate()
  {
    return this.m_keepwasterate;
  }

  public UFBoolean getLaborflag()
  {
    if (this.m_laborflag == null)
      this.m_laborflag = new UFBoolean(false);
    return this.m_laborflag;
  }

  public String getLocatorid()
  {
    return this.m_cspaceid;
  }

  public String getLocatorname()
  {
    return this.m_cspacename;
  }

  public UFBoolean getNegallowed()
  {
    if (this.m_negallowed == null)
      this.m_negallowed = new UFBoolean(false);
    return this.m_negallowed;
  }

  public UFDouble getNonhandassistnum()
  {
    return this.m_nonhandassistnum;
  }

  public UFDouble getNonhandnum()
  {
    return this.m_nonhandnum;
  }

  public UFDouble getNonhandgrossnum() {
    return this.m_nonhandgrossnum;
  }

  public UFDouble getNPacknum()
  {
    return this.m_npacknum;
  }

  public UFDouble getNunitvolume()
  {
    return this.m_nunitvolume;
  }

  public UFDouble getNunitweight()
  {
    return this.m_nunitweight;
  }

  public Integer getOutpriority()
  {
    return this.m_outpriority;
  }

  public UFBoolean getOuttrackin()
  {
    return this.m_outtrackin;
  }

  public UFDouble getPartpercent()
  {
    return this.m_partpercent;
  }

  public String getPk_corp()
  {
    return this.m_pk_corp;
  }

  public String getPk_invcl()
  {
    return this.m_pk_invcl;
  }

  public String getPk_packsort()
  {
    return this.m_pk_packsort;
  }

  public UFBoolean getSellproxyflag()
  {
    if (this.m_sellproxyflag == null)
      this.m_sellproxyflag = new UFBoolean(false);
    return this.m_sellproxyflag;
  }

  public UFDouble getStandStoreUOM()
  {
    return this.m_standStoreUOM;
  }

  public String getCselastunitid()
  {
    return this.m_cselastunitid;
  }

  public void setCselastunitid(String newM_cselastunitid)
  {
    this.m_cselastunitid = newM_cselastunitid;
  }

  public String getUK()
  {
    StringBuffer sbKey = new StringBuffer();
    String sFreeItem0 = null;
    if (this.m_freevo == null)
      this.m_freevo = new FreeVO();
    sFreeItem0 = this.m_freevo.getWholeFreeItemValue();

    sbKey.append(this.m_cinventoryid).append(sFreeItem0).append(this.m_vbatchcode).append(this.m_castunitid).append(this.m_cspaceid);

    return sbKey.toString();
  }

  public String getVpacktypename()
  {
    return this.m_vpacktypename;
  }

  public void seOutpriority(Integer newM_outpriority)
  {
    this.m_outpriority = newM_outpriority;
  }

  public void setCcorrespondbid(String newCcorrespondbid)
  {
    this.m_ccorrespondbid = newCcorrespondbid;
  }

  public void setCcorrespondcode(String newCcorrespondcode)
  {
    this.m_ccorrespondcode = newCcorrespondcode;
  }

  public void setCcorrespondhid(String newCcorrespondhid)
  {
    this.m_ccorrespondhid = newCcorrespondhid;
  }

  public void setCcorrespondtype(String newCcorrespondtype)
  {
    this.m_ccorrespondtype = newCcorrespondtype;
  }

  public void setCcorrespondtypename(String newCcorrespondtypename)
  {
    this.m_ccorrespondtypename = newCcorrespondtypename;
  }

  public void setCfreezeid(String newM_cfreezeid)
  {
    this.m_cfreezeid = newM_cfreezeid;
  }

  public void setCgeneralbid(String newCgeneralbid)
  {
    this.m_cgeneralbid = newCgeneralbid;
  }

  public void setCgeneralhid(String newCgeneralhid)
  {
    this.m_cgeneralhid = newCgeneralhid;
  }

  public void setChildsnum(UFDouble newChildsnum)
  {
    this.m_childsnum = newChildsnum;
  }

  public void setChzl(UFDouble newXczl)
  {
    this.m_chzl = newXczl;
  }

  public void setCinvmanid(String newM_cinvmanid)
  {
    this.m_cinvmanid = newM_cinvmanid;
  }

  public void setCprojectcode(String newCprojectcode)
  {
    this.m_cprojectcode = newCprojectcode;
  }

  public void setCprojectid(String newCprojectid)
  {
    this.m_cprojectid = newCprojectid;
  }

  public void setCprojectname(String newCprojectname)
  {
    this.m_cprojectname = newCprojectname;
  }

  public void setCprojectphasecode(String newCprojectphasecode)
  {
    this.m_cprojectphasecode = newCprojectphasecode;
  }

  public void setCprojectphaseid(String newCprojectphaseid)
  {
    this.m_cprojectphaseid = newCprojectphaseid;
  }

  public void setCprojectphasename(String newCprojectphasename)
  {
    this.m_cprojectphasename = newCprojectphasename;
  }

  public void setCspacecode(String newCspacecode)
  {
    this.m_cspacecode = newCspacecode;
  }

  public void setCspaceid(String newCspaceid)
  {
    this.m_cspaceid = newCspaceid;
  }

  public void setcVendorid(String newCvendorid) {
    this.m_cvendorid = newCvendorid;
  }

  public void setCspacename(String newCspacename)
  {
    this.m_cspacename = newCspacename;
  }

  public void setDiscountflag(UFBoolean newPk)
  {
    this.m_discountflag = newPk;
  }

  public void setDproducedate(UFDate newDvalidate)
  {
    this.m_dproducedate = newDvalidate;
  }

  public void setFbillrowflag(Integer newFbillrowflag)
  {
    this.m_fbillrowflag = newFbillrowflag;
  }

  public void setFreeItemValue(String sItemKey, String sValue)
  {
    if (this.m_freevo == null)
    {
      this.m_freevo = new FreeVO();
    }

    if ((((this.m_isFreeItemMgt == null) || (!this.m_isFreeItemMgt.equals(this.IntTrue)))) && (sItemKey.startsWith("vfreeid")) && (sValue != null) && (sValue.trim().length() > 0))
    {
      this.m_isFreeItemMgt = this.IntTrue;
    }this.m_freevo.setAttributeValue(sItemKey, sValue);
  }

  public void setFreeItemVO(FreeVO newM_freevo)
  {
    if (newM_freevo != null)
      this.m_freevo = ((FreeVO)newM_freevo.clone());
    else
      this.m_freevo = null;
  }

  public void setFreeItemVOValue(FreeVO newM_freevo)
  {
    if (newM_freevo != null)
      for (int i = 1; i < 10; ++i)
        this.m_freevo.setAttributeValue("vfree" + i, newM_freevo.getAttributeValue("vfree" + i));
  }

  public void setFreexNull()
  {
    if (this.m_freevo == null) {
      this.m_freevo = new FreeVO();
    }
    else
    {
      for (int i = 1; i < 11; ++i)
        this.m_freevo.setAttributeValue("vfree" + i, null);
    }
  }

  public void setIcheckcycle(Integer newM_icheckcycle)
  {
    this.m_icheckcycle = newM_icheckcycle;
  }

  public void setInvclasscode(String newM_invclasscode)
  {
    this.m_invclasscode = newM_invclasscode;
  }

  public void setInvclasslev(String newM_invclasslev)
  {
    this.m_invclasslev = newM_invclasslev;
  }

  public void setInvclassname(String newM_invclassname)
  {
    this.m_invclassname = newM_invclassname;
  }

  public void setInvsetparttype(String newInvsetparttype)
  {
    this.m_invsetparttype = newInvsetparttype;
  }

  public void setIscancalculatedinvcost(UFBoolean newM_iscancalculatedinvcost)
  {
    this.m_iscancalculatedinvcost = newM_iscancalculatedinvcost;
  }

  public void setIsprimarybarcode(UFBoolean newIsprimarybarcode)
  {
    this.m_Isprimarybarcode = newIsprimarybarcode;
  }

  public void setIssecondarybarcode(UFBoolean newIssecondarybarcode)
  {
    this.m_Issecondarybarcode = newIssecondarybarcode;
  }

  public void setIsused(UFBoolean newM_isused)
  {
    this.m_isused = newM_isused;
  }

  public void setKeepwasterate(UFDouble newM_keepwasterate)
  {
    this.m_keepwasterate = newM_keepwasterate;
  }

  public void setLaborflag(UFBoolean newPk)
  {
    this.m_laborflag = newPk;
  }

  public void setNegallowed(UFBoolean newM_negallowed)
  {
    this.m_negallowed = newM_negallowed;
  }

  public void setNonhandassistnum(UFDouble newNinnum)
  {
    this.m_nonhandassistnum = newNinnum;
  }

  public void setNonhandnum(UFDouble newNinnum)
  {
    this.m_nonhandnum = newNinnum;
  }

  public void setNonhandgrossnum(UFDouble newNonhandgrossnum) {
    this.m_nonhandgrossnum = newNonhandgrossnum;
  }

  public void setNPacknum(UFDouble newM_ncountnum)
  {
    this.m_npacknum = newM_ncountnum;
  }

  public void setNunitvolume(UFDouble newM_nunitvolume)
  {
    this.m_nunitvolume = newM_nunitvolume;
  }

  public void setNunitweight(UFDouble newM_nunitweight)
  {
    this.m_nunitweight = newM_nunitweight;
  }

  public void setOutpriority(Integer newM_outpriority)
  {
    this.m_outpriority = newM_outpriority;
  }

  public void setOuttrackin(UFBoolean newM_outtrackin)
  {
    this.m_outtrackin = newM_outtrackin;
  }

  public void setPartpercent(UFDouble newPartpercent)
  {
    this.m_partpercent = newPartpercent;
  }

  public void setPk_corp(String newPk_corp)
  {
    this.m_pk_corp = newPk_corp;
  }

  public void setPk_invcl(String newM_pk_invcl)
  {
    this.m_pk_invcl = newM_pk_invcl;
  }

  public void setPk_packsort(String newM_pk_packsort)
  {
    this.m_pk_packsort = newM_pk_packsort;
  }

  public void setSellproxyflag(UFBoolean newSellproxyflag)
  {
    this.m_sellproxyflag = newSellproxyflag;
  }

  public void setStandStoreUOM(UFDouble newStandStoreUOM)
  {
    this.m_standStoreUOM = newStandStoreUOM;
  }

  public void setVpacktypename(String newM_vpacktypename)
  {
    this.m_vpacktypename = newM_vpacktypename;
  }

  public static int[] getAfterMonth(int year, int month, int interval)
  {
    if (interval <= 0) return null;
    int m = interval / 12;
    int n = interval % 12;

    int yearX = year + m;
    int monthX = month + n;
    if (monthX > 12) {
      yearX += 1;
      monthX -= 12;
    }

    int[] iyearmonth = new int[2];
    iyearmonth[0] = yearX;
    iyearmonth[1] = monthX;

    return iyearmonth;
  }

  public static UFDate getUFDate(int year, int month, int day)
  {
    if ((year <= 0) || (month <= 0) || (day <= 0))
      return null;
    String syear = year + "";
    String smonth = month + "";
    if (month < 10)
      smonth = "0" + month;
    int days = UFDate.getDaysMonth(year, month);
    if (day > days)
      day = days;
    String sday = day + "";
    if (day < 10)
      sday = "0" + day;
    return new UFDate(syear + "-" + smonth + "-" + sday);
  }

  public static UFDate calcQualityDate(UFDate d, Integer qualityperiodunit, Integer qualitydaynum)
  {
    if ((d == null) || (qualityperiodunit == null) || (qualitydaynum == null))
      return null;
    if (qualitydaynum.intValue() == 0)
      return d;
    if (quality_day == qualityperiodunit.intValue())
      return d.getDateAfter(qualitydaynum.intValue());
    if (quality_month == qualityperiodunit.intValue()) {
      int[] yearday = getAfterMonth(d.getYear(), d.getMonth(), qualitydaynum.intValue());
      if (yearday == null)
        return null;
      return getUFDate(yearday[0], yearday[1], d.getDay());
    }if (quality_year == qualityperiodunit.intValue()) {
      return getUFDate(d.getYear() + qualitydaynum.intValue(), d.getMonth(), d.getDay());
    }
    return null;
  }

  public static UFDate calcQualityScrqDate(UFDate d, Integer qualityperiodunit, Integer qualitydaynum)
  {
    if ((d == null) || (qualityperiodunit == null) || (qualitydaynum == null))
      return null;
    if (qualitydaynum.intValue() == 0)
      return d;
    if (quality_day == qualityperiodunit.intValue())
      return d.getDateBefore(qualitydaynum.intValue());
    if (quality_month == qualityperiodunit.intValue()) {
      int[] yearday = getBeforeMonth(d.getYear(), d.getMonth(), qualitydaynum.intValue());
      if (yearday == null)
        return null;
      return getUFDate(yearday[0], yearday[1], d.getDay());
    }if (quality_year == qualityperiodunit.intValue()) {
      return getUFDate(d.getYear() - qualitydaynum.intValue(), d.getMonth(), d.getDay());
    }
    return null;
  }

  public static int[] getBeforeMonth(int year, int month, int interval) {
    if (interval <= 0) return null;
    int m = interval / 12;
    int n = interval % 12;

    int yearX = year - m;
    int monthX = month - n;
    if (monthX > 12) {
      yearX += 1;
      monthX -= 12;
    }

    int[] iyearmonth = new int[2];
    iyearmonth[0] = yearX;
    iyearmonth[1] = monthX;

    return iyearmonth;
  }
  public Integer getM_isAsset() {
    return this.m_isAsset;
  }
  public void setM_isAsset(Integer asset) {
    this.m_isAsset = asset;
  }
}