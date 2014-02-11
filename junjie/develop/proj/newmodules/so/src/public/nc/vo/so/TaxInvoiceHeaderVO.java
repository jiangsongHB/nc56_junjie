/***************************************************************\
 *     The skeleton of this class is generated by an automatic *
 * code generator for NC product. It is based on Velocity.     *
\***************************************************************/
package nc.vo.so;
	
import nc.vo.pub.*;
import nc.vo.pub.lang.*;
	
/**
 * <b> 在此处简要描述此类的功能 </b>
 * <p>
 *     在此处添加此类的描述信息
 * </p>
 * 创建日期:2014-01-21 12:14:31
 * @author 
 * @version NCPrj ??
 */
@SuppressWarnings("serial")
public class TaxInvoiceHeaderVO extends SuperVO {
	private java.lang.String ctaxinvoiceid;
	private java.lang.String vinvoiceno;
	private java.lang.String vbillno;
	private nc.vo.pub.lang.UFDate dinvoicedate;
	private java.lang.String pk_corp;
	private java.lang.String cbilltype;
	private java.lang.Integer ibillstatus;
	private java.lang.String ccreator;
	private nc.vo.pub.lang.UFDate dcreatedate;
	private java.lang.String cmodifier;
	private nc.vo.pub.lang.UFDate dmodifydate;
	private java.lang.String capprover;
	private nc.vo.pub.lang.UFDate dapprovedate;
	private java.lang.String cinvoicemamid;
	private nc.vo.pub.lang.UFDate dreceivedate;
	private java.lang.String cordermanid;
	private java.lang.String cservicemanid;
	private java.lang.String cinvoicetype;
	private nc.vo.pub.lang.UFBoolean ispray;
	private java.lang.String csaletype;
	private java.lang.String cpersonid;
	private java.lang.String cdeptid;
	private java.lang.String ccurrencyid;
	private nc.vo.pub.lang.UFDouble nroe;
	private java.lang.String vmemo;
	private java.lang.String cbusitype;
	private java.lang.String vdef1;
	private java.lang.String vdef2;
	private java.lang.String vdef3;
	private java.lang.String vdef4;
	private java.lang.String vdef5;
	private java.lang.String vdef6;
	private java.lang.String vdef7;
	private java.lang.String vdef8;
	private java.lang.String vdef9;
	private java.lang.String vdef10;
	private java.lang.String vapprovnote;
	private java.lang.Integer dr;
	private nc.vo.pub.lang.UFDateTime ts;

	public static final String CTAXINVOICEID = "ctaxinvoiceid";
	public static final String VINVOICENO = "vinvoiceno";
	public static final String VBILLNO = "vbillno";
	public static final String DINVOICEDATE = "dinvoicedate";
	public static final String PK_CORP = "pk_corp";
	public static final String CBILLTYPE = "cbilltype";
	public static final String IBILLSTATUS = "ibillstatus";
	public static final String CCREATOR = "ccreator";
	public static final String DCREATEDATE = "dcreatedate";
	public static final String CMODIFIER = "cmodifier";
	public static final String DMODIFYDATE = "dmodifydate";
	public static final String CAPPROVER = "capprover";
	public static final String DAPPROVEDATE = "dapprovedate";
	public static final String CINVOICEMAMID = "cinvoicemamid";
	public static final String DRECEIVEDATE = "dreceivedate";
	public static final String CORDERMANID = "cordermanid";
	public static final String CSERVICEMANID = "cservicemanid";
	public static final String CINVOICETYPE = "cinvoicetype";
	public static final String ISPRAY = "ispray";
	public static final String CSALETYPE = "csaletype";
	public static final String CPERSONID = "cpersonid";
	public static final String CDEPTID = "cdeptid";
	public static final String CCURRENCYID = "ccurrencyid";
	public static final String NROE = "nroe";
	public static final String VMEMO = "vmemo";
	public static final String CBUSITYPE = "cbusitype";
	public static final String VDEF1 = "vdef1";
	public static final String VDEF2 = "vdef2";
	public static final String VDEF3 = "vdef3";
	public static final String VDEF4 = "vdef4";
	public static final String VDEF5 = "vdef5";
	public static final String VDEF6 = "vdef6";
	public static final String VDEF7 = "vdef7";
	public static final String VDEF8 = "vdef8";
	public static final String VDEF9 = "vdef9";
	public static final String VDEF10 = "vdef10";
	public static final String VAPPROVNOTE = "vapprovnote";
			
	/**
	 * 属性ctaxinvoiceid的Getter方法.
	 * 创建日期:2014-01-21 12:14:31
	 * @return java.lang.String
	 */
	public java.lang.String getCtaxinvoiceid () {
		return ctaxinvoiceid;
	}   
	/**
	 * 属性ctaxinvoiceid的Setter方法.
	 * 创建日期:2014-01-21 12:14:31
	 * @param newCtaxinvoiceid java.lang.String
	 */
	public void setCtaxinvoiceid (java.lang.String newCtaxinvoiceid ) {
	 	this.ctaxinvoiceid = newCtaxinvoiceid;
	} 	  
	/**
	 * 属性vinvoiceno的Getter方法.
	 * 创建日期:2014-01-21 12:14:31
	 * @return java.lang.String
	 */
	public java.lang.String getVinvoiceno () {
		return vinvoiceno;
	}   
	/**
	 * 属性vinvoiceno的Setter方法.
	 * 创建日期:2014-01-21 12:14:31
	 * @param newVinvoiceno java.lang.String
	 */
	public void setVinvoiceno (java.lang.String newVinvoiceno ) {
	 	this.vinvoiceno = newVinvoiceno;
	} 	  
	/**
	 * 属性vbillno的Getter方法.
	 * 创建日期:2014-01-21 12:14:31
	 * @return java.lang.String
	 */
	public java.lang.String getVbillno () {
		return vbillno;
	}   
	/**
	 * 属性vbillno的Setter方法.
	 * 创建日期:2014-01-21 12:14:31
	 * @param newVbillno java.lang.String
	 */
	public void setVbillno (java.lang.String newVbillno ) {
	 	this.vbillno = newVbillno;
	} 	  
	/**
	 * 属性dinvoicedate的Getter方法.
	 * 创建日期:2014-01-21 12:14:31
	 * @return nc.vo.pub.lang.UFDate
	 */
	public nc.vo.pub.lang.UFDate getDinvoicedate () {
		return dinvoicedate;
	}   
	/**
	 * 属性dinvoicedate的Setter方法.
	 * 创建日期:2014-01-21 12:14:31
	 * @param newDinvoicedate nc.vo.pub.lang.UFDate
	 */
	public void setDinvoicedate (nc.vo.pub.lang.UFDate newDinvoicedate ) {
	 	this.dinvoicedate = newDinvoicedate;
	} 	  
	/**
	 * 属性pk_corp的Getter方法.
	 * 创建日期:2014-01-21 12:14:31
	 * @return java.lang.String
	 */
	public java.lang.String getPk_corp () {
		return pk_corp;
	}   
	/**
	 * 属性pk_corp的Setter方法.
	 * 创建日期:2014-01-21 12:14:31
	 * @param newPk_corp java.lang.String
	 */
	public void setPk_corp (java.lang.String newPk_corp ) {
	 	this.pk_corp = newPk_corp;
	} 	  
	/**
	 * 属性cbilltype的Getter方法.
	 * 创建日期:2014-01-21 12:14:31
	 * @return java.lang.String
	 */
	public java.lang.String getCbilltype () {
		return cbilltype;
	}   
	/**
	 * 属性cbilltype的Setter方法.
	 * 创建日期:2014-01-21 12:14:31
	 * @param newCbilltype java.lang.String
	 */
	public void setCbilltype (java.lang.String newCbilltype ) {
	 	this.cbilltype = newCbilltype;
	} 	  
	/**
	 * 属性ibillstatus的Getter方法.
	 * 创建日期:2014-01-21 12:14:31
	 * @return java.lang.Integer
	 */
	public java.lang.Integer getIbillstatus () {
		return ibillstatus;
	}   
	/**
	 * 属性ibillstatus的Setter方法.
	 * 创建日期:2014-01-21 12:14:31
	 * @param newIbillstatus java.lang.Integer
	 */
	public void setIbillstatus (java.lang.Integer newIbillstatus ) {
	 	this.ibillstatus = newIbillstatus;
	} 	  
	/**
	 * 属性ccreator的Getter方法.
	 * 创建日期:2014-01-21 12:14:31
	 * @return java.lang.String
	 */
	public java.lang.String getCcreator () {
		return ccreator;
	}   
	/**
	 * 属性ccreator的Setter方法.
	 * 创建日期:2014-01-21 12:14:31
	 * @param newCcreator java.lang.String
	 */
	public void setCcreator (java.lang.String newCcreator ) {
	 	this.ccreator = newCcreator;
	} 	  
	/**
	 * 属性dcreatedate的Getter方法.
	 * 创建日期:2014-01-21 12:14:31
	 * @return nc.vo.pub.lang.UFDate
	 */
	public nc.vo.pub.lang.UFDate getDcreatedate () {
		return dcreatedate;
	}   
	/**
	 * 属性dcreatedate的Setter方法.
	 * 创建日期:2014-01-21 12:14:31
	 * @param newDcreatedate nc.vo.pub.lang.UFDate
	 */
	public void setDcreatedate (nc.vo.pub.lang.UFDate newDcreatedate ) {
	 	this.dcreatedate = newDcreatedate;
	} 	  
	/**
	 * 属性cmodifier的Getter方法.
	 * 创建日期:2014-01-21 12:14:31
	 * @return java.lang.String
	 */
	public java.lang.String getCmodifier () {
		return cmodifier;
	}   
	/**
	 * 属性cmodifier的Setter方法.
	 * 创建日期:2014-01-21 12:14:31
	 * @param newCmodifier java.lang.String
	 */
	public void setCmodifier (java.lang.String newCmodifier ) {
	 	this.cmodifier = newCmodifier;
	} 	  
	/**
	 * 属性dmodifydate的Getter方法.
	 * 创建日期:2014-01-21 12:14:31
	 * @return nc.vo.pub.lang.UFDate
	 */
	public nc.vo.pub.lang.UFDate getDmodifydate () {
		return dmodifydate;
	}   
	/**
	 * 属性dmodifydate的Setter方法.
	 * 创建日期:2014-01-21 12:14:31
	 * @param newDmodifydate nc.vo.pub.lang.UFDate
	 */
	public void setDmodifydate (nc.vo.pub.lang.UFDate newDmodifydate ) {
	 	this.dmodifydate = newDmodifydate;
	} 	  
	/**
	 * 属性capprover的Getter方法.
	 * 创建日期:2014-01-21 12:14:31
	 * @return java.lang.String
	 */
	public java.lang.String getCapprover () {
		return capprover;
	}   
	/**
	 * 属性capprover的Setter方法.
	 * 创建日期:2014-01-21 12:14:31
	 * @param newCapprover java.lang.String
	 */
	public void setCapprover (java.lang.String newCapprover ) {
	 	this.capprover = newCapprover;
	} 	  
	/**
	 * 属性dapprovedate的Getter方法.
	 * 创建日期:2014-01-21 12:14:31
	 * @return nc.vo.pub.lang.UFDate
	 */
	public nc.vo.pub.lang.UFDate getDapprovedate () {
		return dapprovedate;
	}   
	/**
	 * 属性dapprovedate的Setter方法.
	 * 创建日期:2014-01-21 12:14:31
	 * @param newDapprovedate nc.vo.pub.lang.UFDate
	 */
	public void setDapprovedate (nc.vo.pub.lang.UFDate newDapprovedate ) {
	 	this.dapprovedate = newDapprovedate;
	} 	  
	/**
	 * 属性cinvoicemamid的Getter方法.
	 * 创建日期:2014-01-21 12:14:31
	 * @return java.lang.String
	 */
	public java.lang.String getCinvoicemamid () {
		return cinvoicemamid;
	}   
	/**
	 * 属性cinvoicemamid的Setter方法.
	 * 创建日期:2014-01-21 12:14:31
	 * @param newCinvoicemamid java.lang.String
	 */
	public void setCinvoicemamid (java.lang.String newCinvoicemamid ) {
	 	this.cinvoicemamid = newCinvoicemamid;
	} 	  
	/**
	 * 属性dreceivedate的Getter方法.
	 * 创建日期:2014-01-21 12:14:31
	 * @return nc.vo.pub.lang.UFDate
	 */
	public nc.vo.pub.lang.UFDate getDreceivedate () {
		return dreceivedate;
	}   
	/**
	 * 属性dreceivedate的Setter方法.
	 * 创建日期:2014-01-21 12:14:31
	 * @param newDreceivedate nc.vo.pub.lang.UFDate
	 */
	public void setDreceivedate (nc.vo.pub.lang.UFDate newDreceivedate ) {
	 	this.dreceivedate = newDreceivedate;
	} 	  
	/**
	 * 属性cordermanid的Getter方法.
	 * 创建日期:2014-01-21 12:14:31
	 * @return java.lang.String
	 */
	public java.lang.String getCordermanid () {
		return cordermanid;
	}   
	/**
	 * 属性cordermanid的Setter方法.
	 * 创建日期:2014-01-21 12:14:31
	 * @param newCordermanid java.lang.String
	 */
	public void setCordermanid (java.lang.String newCordermanid ) {
	 	this.cordermanid = newCordermanid;
	} 	  
	/**
	 * 属性cservicemanid的Getter方法.
	 * 创建日期:2014-01-21 12:14:31
	 * @return java.lang.String
	 */
	public java.lang.String getCservicemanid () {
		return cservicemanid;
	}   
	/**
	 * 属性cservicemanid的Setter方法.
	 * 创建日期:2014-01-21 12:14:31
	 * @param newCservicemanid java.lang.String
	 */
	public void setCservicemanid (java.lang.String newCservicemanid ) {
	 	this.cservicemanid = newCservicemanid;
	} 	  
	/**
	 * 属性cinvoicetype的Getter方法.
	 * 创建日期:2014-01-21 12:14:31
	 * @return java.lang.String
	 */
	public java.lang.String getCinvoicetype () {
		return cinvoicetype;
	}   
	/**
	 * 属性cinvoicetype的Setter方法.
	 * 创建日期:2014-01-21 12:14:31
	 * @param newCinvoicetype java.lang.String
	 */
	public void setCinvoicetype (java.lang.String newCinvoicetype ) {
	 	this.cinvoicetype = newCinvoicetype;
	} 	  
	/**
	 * 属性ispray的Getter方法.
	 * 创建日期:2014-01-21 12:14:31
	 * @return nc.vo.pub.lang.UFBoolean
	 */
	public nc.vo.pub.lang.UFBoolean getIspray () {
		return ispray;
	}   
	/**
	 * 属性ispray的Setter方法.
	 * 创建日期:2014-01-21 12:14:31
	 * @param newIspray nc.vo.pub.lang.UFBoolean
	 */
	public void setIspray (nc.vo.pub.lang.UFBoolean newIspray ) {
	 	this.ispray = newIspray;
	} 	  
	/**
	 * 属性csaletype的Getter方法.
	 * 创建日期:2014-01-21 12:14:31
	 * @return java.lang.String
	 */
	public java.lang.String getCsaletype () {
		return csaletype;
	}   
	/**
	 * 属性csaletype的Setter方法.
	 * 创建日期:2014-01-21 12:14:31
	 * @param newCsaletype java.lang.String
	 */
	public void setCsaletype (java.lang.String newCsaletype ) {
	 	this.csaletype = newCsaletype;
	} 	  
	/**
	 * 属性cpersonid的Getter方法.
	 * 创建日期:2014-01-21 12:14:31
	 * @return java.lang.String
	 */
	public java.lang.String getCpersonid () {
		return cpersonid;
	}   
	/**
	 * 属性cpersonid的Setter方法.
	 * 创建日期:2014-01-21 12:14:31
	 * @param newCpersonid java.lang.String
	 */
	public void setCpersonid (java.lang.String newCpersonid ) {
	 	this.cpersonid = newCpersonid;
	} 	  
	/**
	 * 属性cdeptid的Getter方法.
	 * 创建日期:2014-01-21 12:14:31
	 * @return java.lang.String
	 */
	public java.lang.String getCdeptid () {
		return cdeptid;
	}   
	/**
	 * 属性cdeptid的Setter方法.
	 * 创建日期:2014-01-21 12:14:31
	 * @param newCdeptid java.lang.String
	 */
	public void setCdeptid (java.lang.String newCdeptid ) {
	 	this.cdeptid = newCdeptid;
	} 	  
	/**
	 * 属性ccurrencyid的Getter方法.
	 * 创建日期:2014-01-21 12:14:31
	 * @return java.lang.String
	 */
	public java.lang.String getCcurrencyid () {
		return ccurrencyid;
	}   
	/**
	 * 属性ccurrencyid的Setter方法.
	 * 创建日期:2014-01-21 12:14:31
	 * @param newCcurrencyid java.lang.String
	 */
	public void setCcurrencyid (java.lang.String newCcurrencyid ) {
	 	this.ccurrencyid = newCcurrencyid;
	} 	  
	/**
	 * 属性nroe的Getter方法.
	 * 创建日期:2014-01-21 12:14:31
	 * @return nc.vo.pub.lang.UFDouble
	 */
	public nc.vo.pub.lang.UFDouble getNroe () {
		return nroe;
	}   
	/**
	 * 属性nroe的Setter方法.
	 * 创建日期:2014-01-21 12:14:31
	 * @param newNroe nc.vo.pub.lang.UFDouble
	 */
	public void setNroe (nc.vo.pub.lang.UFDouble newNroe ) {
	 	this.nroe = newNroe;
	} 	  
	/**
	 * 属性vmemo的Getter方法.
	 * 创建日期:2014-01-21 12:14:31
	 * @return java.lang.String
	 */
	public java.lang.String getVmemo () {
		return vmemo;
	}   
	/**
	 * 属性vmemo的Setter方法.
	 * 创建日期:2014-01-21 12:14:31
	 * @param newVmemo java.lang.String
	 */
	public void setVmemo (java.lang.String newVmemo ) {
	 	this.vmemo = newVmemo;
	} 	  
	/**
	 * 属性cbusitype的Getter方法.
	 * 创建日期:2014-01-21 12:14:31
	 * @return java.lang.String
	 */
	public java.lang.String getCbusitype () {
		return cbusitype;
	}   
	/**
	 * 属性cbusitype的Setter方法.
	 * 创建日期:2014-01-21 12:14:31
	 * @param newCbusitype java.lang.String
	 */
	public void setCbusitype (java.lang.String newCbusitype ) {
	 	this.cbusitype = newCbusitype;
	} 	  
	/**
	 * 属性vdef1的Getter方法.
	 * 创建日期:2014-01-21 12:14:31
	 * @return java.lang.String
	 */
	public java.lang.String getVdef1 () {
		return vdef1;
	}   
	/**
	 * 属性vdef1的Setter方法.
	 * 创建日期:2014-01-21 12:14:31
	 * @param newVdef1 java.lang.String
	 */
	public void setVdef1 (java.lang.String newVdef1 ) {
	 	this.vdef1 = newVdef1;
	} 	  
	/**
	 * 属性vdef2的Getter方法.
	 * 创建日期:2014-01-21 12:14:31
	 * @return java.lang.String
	 */
	public java.lang.String getVdef2 () {
		return vdef2;
	}   
	/**
	 * 属性vdef2的Setter方法.
	 * 创建日期:2014-01-21 12:14:31
	 * @param newVdef2 java.lang.String
	 */
	public void setVdef2 (java.lang.String newVdef2 ) {
	 	this.vdef2 = newVdef2;
	} 	  
	/**
	 * 属性vdef3的Getter方法.
	 * 创建日期:2014-01-21 12:14:31
	 * @return java.lang.String
	 */
	public java.lang.String getVdef3 () {
		return vdef3;
	}   
	/**
	 * 属性vdef3的Setter方法.
	 * 创建日期:2014-01-21 12:14:31
	 * @param newVdef3 java.lang.String
	 */
	public void setVdef3 (java.lang.String newVdef3 ) {
	 	this.vdef3 = newVdef3;
	} 	  
	/**
	 * 属性vdef4的Getter方法.
	 * 创建日期:2014-01-21 12:14:31
	 * @return java.lang.String
	 */
	public java.lang.String getVdef4 () {
		return vdef4;
	}   
	/**
	 * 属性vdef4的Setter方法.
	 * 创建日期:2014-01-21 12:14:31
	 * @param newVdef4 java.lang.String
	 */
	public void setVdef4 (java.lang.String newVdef4 ) {
	 	this.vdef4 = newVdef4;
	} 	  
	/**
	 * 属性vdef5的Getter方法.
	 * 创建日期:2014-01-21 12:14:31
	 * @return java.lang.String
	 */
	public java.lang.String getVdef5 () {
		return vdef5;
	}   
	/**
	 * 属性vdef5的Setter方法.
	 * 创建日期:2014-01-21 12:14:31
	 * @param newVdef5 java.lang.String
	 */
	public void setVdef5 (java.lang.String newVdef5 ) {
	 	this.vdef5 = newVdef5;
	} 	  
	/**
	 * 属性vdef6的Getter方法.
	 * 创建日期:2014-01-21 12:14:31
	 * @return java.lang.String
	 */
	public java.lang.String getVdef6 () {
		return vdef6;
	}   
	/**
	 * 属性vdef6的Setter方法.
	 * 创建日期:2014-01-21 12:14:31
	 * @param newVdef6 java.lang.String
	 */
	public void setVdef6 (java.lang.String newVdef6 ) {
	 	this.vdef6 = newVdef6;
	} 	  
	/**
	 * 属性vdef7的Getter方法.
	 * 创建日期:2014-01-21 12:14:31
	 * @return java.lang.String
	 */
	public java.lang.String getVdef7 () {
		return vdef7;
	}   
	/**
	 * 属性vdef7的Setter方法.
	 * 创建日期:2014-01-21 12:14:31
	 * @param newVdef7 java.lang.String
	 */
	public void setVdef7 (java.lang.String newVdef7 ) {
	 	this.vdef7 = newVdef7;
	} 	  
	/**
	 * 属性vdef8的Getter方法.
	 * 创建日期:2014-01-21 12:14:31
	 * @return java.lang.String
	 */
	public java.lang.String getVdef8 () {
		return vdef8;
	}   
	/**
	 * 属性vdef8的Setter方法.
	 * 创建日期:2014-01-21 12:14:31
	 * @param newVdef8 java.lang.String
	 */
	public void setVdef8 (java.lang.String newVdef8 ) {
	 	this.vdef8 = newVdef8;
	} 	  
	/**
	 * 属性vdef9的Getter方法.
	 * 创建日期:2014-01-21 12:14:31
	 * @return java.lang.String
	 */
	public java.lang.String getVdef9 () {
		return vdef9;
	}   
	/**
	 * 属性vdef9的Setter方法.
	 * 创建日期:2014-01-21 12:14:31
	 * @param newVdef9 java.lang.String
	 */
	public void setVdef9 (java.lang.String newVdef9 ) {
	 	this.vdef9 = newVdef9;
	} 	  
	/**
	 * 属性vdef10的Getter方法.
	 * 创建日期:2014-01-21 12:14:31
	 * @return java.lang.String
	 */
	public java.lang.String getVdef10 () {
		return vdef10;
	}   
	/**
	 * 属性vdef10的Setter方法.
	 * 创建日期:2014-01-21 12:14:31
	 * @param newVdef10 java.lang.String
	 */
	public void setVdef10 (java.lang.String newVdef10 ) {
	 	this.vdef10 = newVdef10;
	} 	  
	/**
	 * 属性vapprovnote的Getter方法.
	 * 创建日期:2014-01-21 12:14:31
	 * @return java.lang.String
	 */
	public java.lang.String getVapprovnote () {
		return vapprovnote;
	}   
	/**
	 * 属性vapprovnote的Setter方法.
	 * 创建日期:2014-01-21 12:14:31
	 * @param newVapprovnote java.lang.String
	 */
	public void setVapprovnote (java.lang.String newVapprovnote ) {
	 	this.vapprovnote = newVapprovnote;
	} 	  
	/**
	 * 属性dr的Getter方法.
	 * 创建日期:2014-01-21 12:14:31
	 * @return java.lang.Integer
	 */
	public java.lang.Integer getDr () {
		return dr;
	}   
	/**
	 * 属性dr的Setter方法.
	 * 创建日期:2014-01-21 12:14:31
	 * @param newDr java.lang.Integer
	 */
	public void setDr (java.lang.Integer newDr ) {
	 	this.dr = newDr;
	} 	  
	/**
	 * 属性ts的Getter方法.
	 * 创建日期:2014-01-21 12:14:31
	 * @return nc.vo.pub.lang.UFDateTime
	 */
	public nc.vo.pub.lang.UFDateTime getTs () {
		return ts;
	}   
	/**
	 * 属性ts的Setter方法.
	 * 创建日期:2014-01-21 12:14:31
	 * @param newTs nc.vo.pub.lang.UFDateTime
	 */
	public void setTs (nc.vo.pub.lang.UFDateTime newTs ) {
	 	this.ts = newTs;
	} 	  
 
	/**
	  * <p>取得父VO主键字段.
	  * <p>
	  * 创建日期:2014-01-21 12:14:31
	  * @return java.lang.String
	  */
	public java.lang.String getParentPKFieldName() {
	    return null;
	}   
    
	/**
	  * <p>取得表主键.
	  * <p>
	  * 创建日期:2014-01-21 12:14:31
	  * @return java.lang.String
	  */
	public java.lang.String getPKFieldName() {
	  return "ctaxinvoiceid";
	}
    
	/**
	 * <p>返回表名称.
	 * <p>
	 * 创建日期:2014-01-21 12:14:31
	 * @return java.lang.String
	 */
	public java.lang.String getTableName() {
		return "so_taxinvoice";
	}    
    
    /**
	  * 按照默认方式创建构造子.
	  *
	  * 创建日期:2014-01-21 12:14:31
	  */
     public TaxInvoiceHeaderVO() {
		super();	
	}    
} 
