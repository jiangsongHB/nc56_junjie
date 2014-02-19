/***************************************************************\
 *     The skeleton of this class is generated by an automatic *
 * code generator for NC product. It is based on Velocity.     *
\***************************************************************/
package nc.vo.scmpub;
	
import nc.vo.pub.*;
import nc.vo.pub.lang.*;
	
/**
 * <b> 在此处简要描述此类的功能 </b>
 * <p>
 *     在此处添加此类的描述信息
 * </p>
 * 创建日期:2014-01-25 00:46:04
 * @author 
 * @version NCPrj ??
 */
@SuppressWarnings("serial")
public class TaxInvoiceTypeVO extends SuperVO {
	private java.lang.String pk_taxinvoicetype;
	private java.lang.String pk_corp;
	private java.lang.String code;
	private java.lang.String name;
	private java.lang.String creator;
	private nc.vo.pub.lang.UFDate creationtime;
	private java.lang.String modifier;
	private nc.vo.pub.lang.UFDate modifiedtime;
	private java.lang.String approver;
	private nc.vo.pub.lang.UFDate approvedtime;
	private nc.vo.pub.lang.UFBoolean iffee;
	private nc.vo.pub.lang.UFBoolean isdefault;
	private nc.vo.pub.lang.UFBoolean isseal;
	private java.lang.Integer iapprovetype = 1;
	private java.lang.Integer idealtype;
	private nc.vo.pub.lang.UFDouble ntaxrate;
	private java.lang.Integer dr;
	private nc.vo.pub.lang.UFDateTime ts;

	public static final String PK_TAXINVOICETYPE = "pk_taxinvoicetype";
	public static final String PK_CORP = "pk_corp";
	public static final String CODE = "code";
	public static final String NAME = "name";
	public static final String CREATOR = "creator";
	public static final String CREATIONTIME = "creationtime";
	public static final String MODIFIER = "modifier";
	public static final String MODIFIEDTIME = "modifiedtime";
	public static final String APPROVER = "approver";
	public static final String APPROVEDTIME = "approvedtime";
	public static final String IFFEE = "iffee";
	public static final String ISDEFAULT = "isdefault";
	public static final String ISSEAL = "isseal";
	public static final String IAPPROVETYPE = "iapprovetype";
	public static final String IDEALTYPE = "idealtype";
	public static final String NTAXRATE = "ntaxrate";
			
	/**
	 * 属性pk_taxinvoicetype的Getter方法.
	 * 创建日期:2014-01-25 00:46:04
	 * @return java.lang.String
	 */
	public java.lang.String getPk_taxinvoicetype () {
		return pk_taxinvoicetype;
	}   
	/**
	 * 属性pk_taxinvoicetype的Setter方法.
	 * 创建日期:2014-01-25 00:46:04
	 * @param newPk_taxinvoicetype java.lang.String
	 */
	public void setPk_taxinvoicetype (java.lang.String newPk_taxinvoicetype ) {
	 	this.pk_taxinvoicetype = newPk_taxinvoicetype;
	} 	  
	/**
	 * 属性pk_corp的Getter方法.
	 * 创建日期:2014-01-25 00:46:04
	 * @return java.lang.String
	 */
	public java.lang.String getPk_corp () {
		return pk_corp;
	}   
	/**
	 * 属性pk_corp的Setter方法.
	 * 创建日期:2014-01-25 00:46:04
	 * @param newPk_corp java.lang.String
	 */
	public void setPk_corp (java.lang.String newPk_corp ) {
	 	this.pk_corp = newPk_corp;
	} 	  
	/**
	 * 属性code的Getter方法.
	 * 创建日期:2014-01-25 00:46:04
	 * @return java.lang.String
	 */
	public java.lang.String getCode () {
		return code;
	}   
	/**
	 * 属性code的Setter方法.
	 * 创建日期:2014-01-25 00:46:04
	 * @param newCode java.lang.String
	 */
	public void setCode (java.lang.String newCode ) {
	 	this.code = newCode;
	} 	  
	/**
	 * 属性name的Getter方法.
	 * 创建日期:2014-01-25 00:46:04
	 * @return java.lang.String
	 */
	public java.lang.String getName () {
		return name;
	}   
	/**
	 * 属性name的Setter方法.
	 * 创建日期:2014-01-25 00:46:04
	 * @param newName java.lang.String
	 */
	public void setName (java.lang.String newName ) {
	 	this.name = newName;
	} 	  
	/**
	 * 属性creator的Getter方法.
	 * 创建日期:2014-01-25 00:46:04
	 * @return java.lang.String
	 */
	public java.lang.String getCreator () {
		return creator;
	}   
	/**
	 * 属性creator的Setter方法.
	 * 创建日期:2014-01-25 00:46:04
	 * @param newCreator java.lang.String
	 */
	public void setCreator (java.lang.String newCreator ) {
	 	this.creator = newCreator;
	} 	  
	/**
	 * 属性creationtime的Getter方法.
	 * 创建日期:2014-01-25 00:46:04
	 * @return nc.vo.pub.lang.UFDate
	 */
	public nc.vo.pub.lang.UFDate getCreationtime () {
		return creationtime;
	}   
	/**
	 * 属性creationtime的Setter方法.
	 * 创建日期:2014-01-25 00:46:04
	 * @param newCreationtime nc.vo.pub.lang.UFDate
	 */
	public void setCreationtime (nc.vo.pub.lang.UFDate newCreationtime ) {
	 	this.creationtime = newCreationtime;
	} 	  
	/**
	 * 属性modifier的Getter方法.
	 * 创建日期:2014-01-25 00:46:04
	 * @return java.lang.String
	 */
	public java.lang.String getModifier () {
		return modifier;
	}   
	/**
	 * 属性modifier的Setter方法.
	 * 创建日期:2014-01-25 00:46:04
	 * @param newModifier java.lang.String
	 */
	public void setModifier (java.lang.String newModifier ) {
	 	this.modifier = newModifier;
	} 	  
	/**
	 * 属性modifiedtime的Getter方法.
	 * 创建日期:2014-01-25 00:46:04
	 * @return nc.vo.pub.lang.UFDate
	 */
	public nc.vo.pub.lang.UFDate getModifiedtime () {
		return modifiedtime;
	}   
	/**
	 * 属性modifiedtime的Setter方法.
	 * 创建日期:2014-01-25 00:46:04
	 * @param newModifiedtime nc.vo.pub.lang.UFDate
	 */
	public void setModifiedtime (nc.vo.pub.lang.UFDate newModifiedtime ) {
	 	this.modifiedtime = newModifiedtime;
	} 	  
	/**
	 * 属性approver的Getter方法.
	 * 创建日期:2014-01-25 00:46:04
	 * @return java.lang.String
	 */
	public java.lang.String getApprover () {
		return approver;
	}   
	/**
	 * 属性approver的Setter方法.
	 * 创建日期:2014-01-25 00:46:04
	 * @param newApprover java.lang.String
	 */
	public void setApprover (java.lang.String newApprover ) {
	 	this.approver = newApprover;
	} 	  
	/**
	 * 属性approvedtime的Getter方法.
	 * 创建日期:2014-01-25 00:46:04
	 * @return nc.vo.pub.lang.UFDate
	 */
	public nc.vo.pub.lang.UFDate getApprovedtime () {
		return approvedtime;
	}   
	/**
	 * 属性approvedtime的Setter方法.
	 * 创建日期:2014-01-25 00:46:04
	 * @param newApprovedtime nc.vo.pub.lang.UFDate
	 */
	public void setApprovedtime (nc.vo.pub.lang.UFDate newApprovedtime ) {
	 	this.approvedtime = newApprovedtime;
	} 	  
	/**
	 * 属性iffee的Getter方法.
	 * 创建日期:2014-01-25 00:46:04
	 * @return nc.vo.pub.lang.UFBoolean
	 */
	public nc.vo.pub.lang.UFBoolean getIffee () {
		return iffee;
	}   
	/**
	 * 属性iffee的Setter方法.
	 * 创建日期:2014-01-25 00:46:04
	 * @param newIffee nc.vo.pub.lang.UFBoolean
	 */
	public void setIffee (nc.vo.pub.lang.UFBoolean newIffee ) {
	 	this.iffee = newIffee;
	} 	  
	/**
	 * 属性isdefault的Getter方法.
	 * 创建日期:2014-01-25 00:46:04
	 * @return nc.vo.pub.lang.UFBoolean
	 */
	public nc.vo.pub.lang.UFBoolean getIsdefault () {
		return isdefault;
	}   
	/**
	 * 属性isdefault的Setter方法.
	 * 创建日期:2014-01-25 00:46:04
	 * @param newIsdefault nc.vo.pub.lang.UFBoolean
	 */
	public void setIsdefault (nc.vo.pub.lang.UFBoolean newIsdefault ) {
	 	this.isdefault = newIsdefault;
	} 	  
	/**
	 * 属性isseal的Getter方法.
	 * 创建日期:2014-01-25 00:46:04
	 * @return nc.vo.pub.lang.UFBoolean
	 */
	public nc.vo.pub.lang.UFBoolean getIsseal () {
		return isseal;
	}   
	/**
	 * 属性isseal的Setter方法.
	 * 创建日期:2014-01-25 00:46:04
	 * @param newIsseal nc.vo.pub.lang.UFBoolean
	 */
	public void setIsseal (nc.vo.pub.lang.UFBoolean newIsseal ) {
	 	this.isseal = newIsseal;
	} 	  
	/**
	 * 属性iapprovetype的Getter方法.
	 * 创建日期:2014-01-25 00:46:04
	 * @return java.lang.Integer
	 */
	public java.lang.Integer getIapprovetype () {
		return iapprovetype;
	}   
	/**
	 * 属性iapprovetype的Setter方法.
	 * 创建日期:2014-01-25 00:46:04
	 * @param newIapprovetype java.lang.Integer
	 */
	public void setIapprovetype (java.lang.Integer newIapprovetype ) {
	 	this.iapprovetype = newIapprovetype;
	} 	  
	/**
	 * 属性idealtype的Getter方法.
	 * 创建日期:2014-01-25 00:46:04
	 * @return java.lang.Integer
	 */
	public java.lang.Integer getIdealtype () {
		return idealtype;
	}   
	/**
	 * 属性idealtype的Setter方法.
	 * 创建日期:2014-01-25 00:46:04
	 * @param newIdealtype java.lang.Integer
	 */
	public void setIdealtype (java.lang.Integer newIdealtype ) {
	 	this.idealtype = newIdealtype;
	} 	  
	/**
	 * 属性ntaxrate的Getter方法.
	 * 创建日期:2014-01-25 00:46:04
	 * @return nc.vo.pub.lang.UFDouble
	 */
	public nc.vo.pub.lang.UFDouble getNtaxrate () {
		return ntaxrate;
	}   
	/**
	 * 属性ntaxrate的Setter方法.
	 * 创建日期:2014-01-25 00:46:04
	 * @param newNtaxrate nc.vo.pub.lang.UFDouble
	 */
	public void setNtaxrate (nc.vo.pub.lang.UFDouble newNtaxrate ) {
	 	this.ntaxrate = newNtaxrate;
	} 	  
	/**
	 * 属性dr的Getter方法.
	 * 创建日期:2014-01-25 00:46:04
	 * @return java.lang.Integer
	 */
	public java.lang.Integer getDr () {
		return dr;
	}   
	/**
	 * 属性dr的Setter方法.
	 * 创建日期:2014-01-25 00:46:04
	 * @param newDr java.lang.Integer
	 */
	public void setDr (java.lang.Integer newDr ) {
	 	this.dr = newDr;
	} 	  
	/**
	 * 属性ts的Getter方法.
	 * 创建日期:2014-01-25 00:46:04
	 * @return nc.vo.pub.lang.UFDateTime
	 */
	public nc.vo.pub.lang.UFDateTime getTs () {
		return ts;
	}   
	/**
	 * 属性ts的Setter方法.
	 * 创建日期:2014-01-25 00:46:04
	 * @param newTs nc.vo.pub.lang.UFDateTime
	 */
	public void setTs (nc.vo.pub.lang.UFDateTime newTs ) {
	 	this.ts = newTs;
	} 	  
 
	/**
	  * <p>取得父VO主键字段.
	  * <p>
	  * 创建日期:2014-01-25 00:46:04
	  * @return java.lang.String
	  */
	public java.lang.String getParentPKFieldName() {
	    return null;
	}   
    
	/**
	  * <p>取得表主键.
	  * <p>
	  * 创建日期:2014-01-25 00:46:04
	  * @return java.lang.String
	  */
	public java.lang.String getPKFieldName() {
	  return "pk_taxinvoicetype";
	}
    
	/**
	 * <p>返回表名称.
	 * <p>
	 * 创建日期:2014-01-25 00:46:04
	 * @return java.lang.String
	 */
	public java.lang.String getTableName() {
		return "bd_taxinvoicetype";
	}    
    
    /**
	  * 按照默认方式创建构造子.
	  *
	  * 创建日期:2014-01-25 00:46:04
	  */
     public TaxInvoiceTypeVO() {
		super();	
	}    
} 
