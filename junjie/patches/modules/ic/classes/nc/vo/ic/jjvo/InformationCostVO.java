
package nc.vo.ic.jjvo;
	
import nc.vo.pub.lang.*;
import nc.vo.scm.bd.SmartVODataUtils;
import nc.vo.scm.pub.smart.SmartVO;
	

/**
 * @function 费用信息VO
 * 
 * @author QuSida
 * 
 * @date 2010-8-10 上午11:19:14
 *
 */
@SuppressWarnings("serial")
public  class InformationCostVO extends SmartVO {
	
	/**
	 * 字段详细信息请参考nc.vo.scm.jjvo.InformationCostVOMeta.java
	 */
	
	

	public  UFBoolean getIsmny(){
		return  (UFBoolean)getAttributeValue("ismny");
	}
	public void setIsmny(UFBoolean ismny){
		setAttributeValue("ismny", ismny);
	}
	
	public UFDouble getNoriginalcurprice(){
		return (UFDouble)getAttributeValue("noriginalcurprice");
	}
	public void setNoriginalcurprice(UFDouble noriginalcurprice){
		setAttributeValue("noriginalcurprice", noriginalcurprice);
	}
	public UFDouble getNoriginalcursummny(){
		return (UFDouble)getAttributeValue("noriginalcursummny");
	}
	public void setNoriginalcursummny(UFDouble noriginalcursummny){
		setAttributeValue("noriginalcursummny", noriginalcursummny);
	}
	/**
	 * 属性vtaxtype的Getter方法.
	 * 创建日期:2010-08-10 09:58:28
	 * @return String
	 */

	public String getVtaxtype () {		
		 return (String) getAttributeValue("vtaxtype");
	}   
	/**
	 * 属性vtaxtype的Setter方法.
	 * 创建日期:2010-08-10 09:58:28
	 * @param newVtaxtype String
	 */
	public void setVtaxtype (String newVtaxtype ) {
		setAttributeValue("vtaxtype", newVtaxtype);
	} 	  
	/**
	 * 属性ninstoreoriginalcurtaxmny的Getter方法.
	 * 创建日期:2010-08-10 09:58:28
	 * @return UFDouble
	 */
	public UFDouble getNinstoreoriginalcurtaxmny () {		
		 return (UFDouble) getAttributeValue("ninstoreoriginalcurtaxmny");
	}   
	/**
	 * 属性ninstoreoriginalcurtaxmny的Setter方法.
	 * 创建日期:2010-08-10 09:58:28
	 * @param newNinstoreoriginalcurtaxmny UFDouble
	 */
	public void setNinstoreoriginalcurtaxmny (UFDouble newNinstoreoriginalcurtaxmny ) {
	 	setAttributeValue("ninstoreoriginalcurtaxmny", newNinstoreoriginalcurtaxmny);
	} 	  
	/**
	 * 属性pk_taxitems的Getter方法.
	 * 创建日期:2010-08-10 09:58:28
	 * @return String
	 */
	public String getPk_taxitems () {
	
		return (String) getAttributeValue("pk_taxitems");
	}   
	/**
	 * 属性pk_taxitems的Setter方法.
	 * 创建日期:2010-08-10 09:58:28
	 * @param newPk_taxitems String
	 */
	public void setPk_taxitems (String newPk_taxitems ) {
	 	setAttributeValue("pk_taxitems", newPk_taxitems);
	} 	  
	/**
	 * 属性vdef4的Getter方法.
	 * 创建日期:2010-08-10 09:58:28
	 * @return String
	 */
	public String getVdef4 () {
		return (String) getAttributeValue("vdef4");
	}   
	/**
	 * 属性vdef4的Setter方法.
	 * 创建日期:2010-08-10 09:58:28
	 * @param newVdef4 String
	 */
	public void setVdef4 (String newVdef4 ) {
	 	setAttributeValue("vdef4", newVdef4);
	} 	  
	/**
	 * 属性ninvoriginalcurtaxmny的Getter方法.
	 * 创建日期:2010-08-10 09:58:28
	 * @return UFDouble
	 */
	public UFDouble getNinvoriginalcurtaxmny () {

		return (UFDouble) getAttributeValue("ninvoriginalcurtaxmny");
	}   
	/**
	 * 属性ninvoriginalcurtaxmny的Setter方法.
	 * 创建日期:2010-08-10 09:58:28
	 * @param newNinvoriginalcurtaxmny UFDouble
	 */
	public void setNinvoriginalcurtaxmny (UFDouble newNinvoriginalcurtaxmny ) {
	 	setAttributeValue("ninvoriginalcurtaxmny", newNinvoriginalcurtaxmny);
	} 	  
	/**
	 * 属性noriginalcurmny的Getter方法.
	 * 创建日期:2010-08-10 09:58:28
	 * @return UFDouble
	 */
	public UFDouble getNoriginalcurmny () {
		return (UFDouble) getAttributeValue("noriginalcurmny");
	}   
	/**
	 * 属性noriginalcurmny的Setter方法.
	 * 创建日期:2010-08-10 09:58:28
	 * @param newNoriginalcurmny UFDouble
	 */
	public void setNoriginalcurmny (UFDouble newNoriginalcurmny ) {
	 	setAttributeValue("noriginalcurmny", newNoriginalcurmny);

	} 	  
	/**
	 * 属性vdef7的Getter方法.
	 * 创建日期:2010-08-10 09:58:28
	 * @return String
	 */
	public String getVdef7 () {
		return (String) getAttributeValue("vdef7");
	}   
	/**
	 * 属性vdef7的Setter方法.
	 * 创建日期:2010-08-10 09:58:28
	 * @param newVdef7 String
	 */
	public void setVdef7 (String newVdef7 ) {
	 	setAttributeValue("vdef7", newVdef7);
	} 	  
	/**
	 * 属性ntaxprice的Getter方法.
	 * 创建日期:2010-08-10 09:58:28
	 * @return UFDouble
	 */
	public UFDouble getNtaxprice () {
		return (UFDouble)getAttributeValue("ntaxprice");
	}   
	/**
	 * 属性ntaxprice的Setter方法.
	 * 创建日期:2010-08-10 09:58:28
	 * @param newNtaxprice UFDouble
	 */
	public void setNtaxprice (UFDouble newNtaxprice ) {
	 	setAttributeValue("ntaxprice", newNtaxprice);
	} 	 
	/**
	 * 属性dr的Getter方法.
	 * 创建日期:2010-08-10 09:58:28
	 * @return UFDouble
	 */
	public Integer getDr () {
		return (Integer)getAttributeValue("dr");
	}   
	/**
	 * 属性dr的Setter方法.
	 * 创建日期:2010-08-10 09:58:28
	 * @param newDr UFDouble
	 */
	public void setDr (Integer newDr ) {
	 	setAttributeValue("dr", newDr);
	} 	  
	/**
	 * 属性nmny的Getter方法.
	 * 创建日期:2010-08-10 09:58:28
	 * @return UFDouble
	 */
	public UFDouble getNmny () {
		return (UFDouble)getAttributeValue("nmny");
	}   
	/**
	 * 属性nmny的Setter方法.
	 * 创建日期:2010-08-10 09:58:28
	 * @param newNmny UFDouble
	 */
	public void setNmny (UFDouble newNmny ) {
	 	setAttributeValue("nmny", newNmny);
	} 	  
	/**
	 * 属性currtypeid的Getter方法.
	 * 创建日期:2010-08-10 09:58:28
	 * @return String
	 */
	public String getCurrtypeid () {
		return (String)getAttributeValue("currtypeid");
	}   
	/**
	 * 属性currtypeid的Setter方法.
	 * 创建日期:2010-08-10 09:58:28
	 * @param newCurrtypeid String
	 */
	public void setCurrtypeid (String newCurrtypeid ) {
	 	setAttributeValue("currtypeid", newCurrtypeid);
	} 	  
	/**
	 * 属性vdef2的Getter方法.
	 * 创建日期:2010-08-10 09:58:28
	 * @return String
	 */
	public String getVdef2 () {
		return (String)getAttributeValue("vdef2");
	}   
	/**
	 * 属性vdef2的Setter方法.
	 * 创建日期:2010-08-10 09:58:28
	 * @param newVdef2 String
	 */
	public void setVdef2 (String newVdef2 ) {
	 	setAttributeValue("vdef2", newVdef2);
	} 	  
	/**
	 * 属性pk_informantioncost的Getter方法.
	 * 创建日期:2010-08-10 09:58:28
	 * @return String
	 */
	public String getPk_informantioncost () {
		return (String)getAttributeValue("pk_informantioncost");
	}   
	/**
	 * 属性pk_informantioncost的Setter方法.
	 * 创建日期:2010-08-10 09:58:28
	 * @param newPk_informantioncost String
	 */
	public void setPk_informantioncost (String newPk_informantioncost ) {
	 	setAttributeValue("pk_informantioncost", newPk_informantioncost);
	} 	  
	/**
	 * 属性ninstoremny的Getter方法.
	 * 创建日期:2010-08-10 09:58:28
	 * @return UFDouble
	 */
	public UFDouble getNinstoremny () {
		return (UFDouble)getAttributeValue("ninstoremny");
	}   
	/**
	 * 属性ninstoremny的Setter方法.
	 * 创建日期:2010-08-10 09:58:28
	 * @param newNinstoremny UFDouble
	 */
	public void setNinstoremny (UFDouble newNinstoremny ) {
	 	setAttributeValue("ninstoremny", newNinstoremny);
	} 	  
	/**
	 * 属性ninvtaxmny的Getter方法.
	 * 创建日期:2010-08-10 09:58:28
	 * @return UFDouble
	 */
	public UFDouble getNinvtaxmny () {
		return (UFDouble)getAttributeValue("ninvtaxmny");
	}   
	/**
	 * 属性ninvtaxmny的Setter方法.
	 * 创建日期:2010-08-10 09:58:28
	 * @param newNinvtaxmny UFDouble
	 */
	public void setNinvtaxmny (UFDouble newNinvtaxmny ) {
	 	setAttributeValue("ninvtaxmny", newNinvtaxmny);
	} 	  
	/**
	 * 属性vmemo的Getter方法.
	 * 创建日期:2010-08-10 09:58:28
	 * @return String
	 */
	public String getVmemo () {
		return (String)getAttributeValue("vmemo");
	}   
	/**
	 * 属性vmemo的Setter方法.
	 * 创建日期:2010-08-10 09:58:28
	 * @param newVmemo String
	 */
	public void setVmemo (String newVmemo ) {
	 	setAttributeValue("vmemo", newVmemo);
	} 	  
	/**
	 * 属性vcosttype的Getter方法.
	 * 创建日期:2010-08-10 09:58:28
	 * @return String
	 */
	public String getVcosttype () {
		return (String)getAttributeValue("vcosttype");
	}   
	/**
	 * 属性vcosttype的Setter方法.
	 * 创建日期:2010-08-10 09:58:28
	 * @param newVcosttype String
	 */
	public void setVcosttype (String newVcosttype ) {
	 	setAttributeValue("vcosttype", newVcosttype);
	} 	  
	/**
	 * 属性cbillid的Getter方法.
	 * 创建日期:2010-08-10 09:58:28
	 * @return String
	 */
	public String getCbillid () {
		return (String)getAttributeValue("cbillid");
	}   
	/**
	 * 属性cbillid的Setter方法.
	 * 创建日期:2010-08-10 09:58:28
	 * @param newCbillid String
	 */
	public void setCbillid (String newCbillid ) {
	 	setAttributeValue("cbillid", newCbillid);
	} 	  
	/**
	 * 属性ts的Getter方法.
	 * 创建日期:2010-08-10 09:58:28
	 * @return UFDateTime
	 */
	public UFDateTime getTs () {
		Object oValue = getAttributeValue("ts");
    return SmartVODataUtils.getUFDateTime(oValue);}   
	/**
	 * 属性ts的Setter方法.
	 * 创建日期:2010-08-10 09:58:28
	 * @param newTs UFDateTime
	 */
	public void setTs (UFDateTime newTs ) {
	 	setAttributeValue("ts", newTs);
	} 	  
	/**
	 * 属性costname的Getter方法.
	 * 创建日期:2010-08-10 09:58:28
	 * @return String
	 */
	public String getCostname () {
		return (String)getAttributeValue("costname");
	}   
	/**
	 * 属性costname的Setter方法.
	 * 创建日期:2010-08-10 09:58:28
	 * @param newCostname String
	 */
	public void setCostname (String newCostname ) {
	 	setAttributeValue("costname", newCostname);
	} 	  
	/**
	 * 属性vdef1的Getter方法.
	 * 创建日期:2010-08-10 09:58:28
	 * @return String
	 */
	public String getVdef1 () {
		return (String)getAttributeValue("vdef1");
	}   
	/**
	 * 属性vdef1的Setter方法.
	 * 创建日期:2010-08-10 09:58:28
	 * @param newVdef1 String
	 */
	public void setVdef1 (String newVdef1 ) {
	 	setAttributeValue("vdef1", newVdef1);
	} 	  
	/**
	 * 属性ccostunitid的Getter方法.
	 * 创建日期:2010-08-10 09:58:28
	 * @return String
	 */
	public String getCcostunitid (){
		return (String)getAttributeValue("ccostunitid");
	}   
	/**
	 * 属性ccostunitid的Setter方法.
	 * 创建日期:2010-08-10 09:58:28
	 * @param newCcostunitid String
	 */
	public void setCcostunitid (String newCcostunitid ) {
	 	setAttributeValue("ccostunitid", newCcostunitid);
	} 	  
	/**
	 * 属性cmeasdocid的Getter方法.
	 * 创建日期:2010-08-10 09:58:28
	 * @return String
	 */
	public String getCmeasdocid () {
		return (String)getAttributeValue("cmeasdocid");
	}   
	/**
	 * 属性cmeasdocid的Setter方法.
	 * 创建日期:2010-08-10 09:58:28
	 * @param newCmeasdocid String
	 */
	public void setCmeasdocid (String newCmeasdocid ) {
	 	setAttributeValue("cmeasdocid", newCmeasdocid);
	} 	  
	/**
	 * 属性ninstoreoriginalcurmny的Getter方法.
	 * 创建日期:2010-08-10 09:58:28
	 * @return UFDouble
	 */
	public UFDouble getNinstoreoriginalcurmny () {
		return (UFDouble)getAttributeValue("ninstoreoriginalcurmny");
	}   
	/**
	 * 属性ninstoreoriginalcurmny的Setter方法.
	 * 创建日期:2010-08-10 09:58:28
	 * @param newNinstoreoriginalcurmny UFDouble
	 */
	public void setNinstoreoriginalcurmny (UFDouble newNinstoreoriginalcurmny ) {
	 	setAttributeValue("ninstoreoriginalcurmny", newNinstoreoriginalcurmny);
	} 	  
	/**
	 * 属性noriginalcurtaxprice的Getter方法.
	 * 创建日期:2010-08-10 09:58:28
	 * @return UFDouble
	 */
	public UFDouble getNoriginalcurtaxprice () {
		return (UFDouble)getAttributeValue("noriginalcurtaxprice");
	}   
	/**
	 * 属性noriginalcurtaxprice的Setter方法.
	 * 创建日期:2010-08-10 09:58:28
	 * @param newNoriginalcurtaxprice UFDouble
	 */
	public void setNoriginalcurtaxprice (UFDouble newNoriginalcurtaxprice ) {
	 	setAttributeValue("noriginalcurtaxprice", newNoriginalcurtaxprice);
	} 	  
	/**
	 * 属性ninvoriginalcurmny的Getter方法.
	 * 创建日期:2010-08-10 09:58:28
	 * @return UFDouble
	 */
	public UFDouble getNinvoriginalcurmny () {
		return (UFDouble)getAttributeValue("ninvoriginalcurmny");
	}   
	/**
	 * 属性ninvoriginalcurmny的Setter方法.
	 * 创建日期:2010-08-10 09:58:28
	 * @param newNinvoriginalcurmny UFDouble
	 */
	public void setNinvoriginalcurmny (UFDouble newNinvoriginalcurmny ) {
	 	setAttributeValue("ninvoriginalcurmny", newNinvoriginalcurmny);
	} 	  
	/**
	 * 属性costcode的Getter方法.
	 * 创建日期:2010-08-10 09:58:28
	 * @return String
	 */
	public String getCostcode () {
		return (String)getAttributeValue("costcode");
	}   
	/**
	 * 属性costcode的Setter方法.
	 * 创建日期:2010-08-10 09:58:28
	 * @param newCostcode String
	 */
	public void setCostcode (String newCostcode ) {
	 	setAttributeValue("costcode", newCostcode);
	} 	  
	/**
	 * 属性vdef3的Getter方法.
	 * 创建日期:2010-08-10 09:58:28
	 * @return String
	 */
	public String getVdef3 () {
		return (String)getAttributeValue("vdef3");
	}   
	/**
	 * 属性vdef3的Setter方法.
	 * 创建日期:2010-08-10 09:58:28
	 * @param newVdef3 String
	 */
	public void setVdef3 (String newVdef3 ) {
	 	setAttributeValue("vdef3", newVdef3);
	} 	  
	/**
	 * 属性vdef9的Getter方法.
	 * 创建日期:2010-08-10 09:58:28
	 * @return String
	 */
	public String getVdef9 () {
		return (String)getAttributeValue("vdef9");
	}   
	/**
	 * 属性vdef9的Setter方法.
	 * 创建日期:2010-08-10 09:58:28
	 * @param newVdef9 String
	 */
	public void setVdef9 (String newVdef9 ) {
	 	setAttributeValue("vdef9", newVdef9);
	} 	  
	/**
	 * 属性nsummny的Getter方法.
	 * 创建日期:2010-08-10 09:58:28
	 * @return UFDouble
	 */
	public UFDouble getNsummny () {
		return (UFDouble)getAttributeValue("nsummny");
	}   
	/**
	 * 属性nsummny的Setter方法.
	 * 创建日期:2010-08-10 09:58:28
	 * @param newNsummny UFDouble
	 */
	public void setNsummny (UFDouble newNsummny ) {
	 	setAttributeValue("nsummny", newNsummny);
	} 	  
	/**
	 * 属性ninvmny的Getter方法.
	 * 创建日期:2010-08-10 09:58:28
	 * @return UFDouble
	 */
	public UFDouble getNinvmny () {
		return (UFDouble)getAttributeValue("ninvmny");
	}   
	/**
	 * 属性ninvmny的Setter方法.
	 * 创建日期:2010-08-10 09:58:28
	 * @param newNinvmny UFDouble
	 */
	public void setNinvmny (UFDouble newNinvmny ) {
	 	setAttributeValue("ninvmny", newNinvmny);
	} 	  
	/**
	 * 属性vdef8的Getter方法.
	 * 创建日期:2010-08-10 09:58:28
	 * @return String
	 */
	public String getVdef8 () {
		return (String)getAttributeValue("vdef8");
	}   
	/**
	 * 属性vdef8的Setter方法.
	 * 创建日期:2010-08-10 09:58:28
	 * @param newVdef8 String
	 */
	public void setVdef8 (String newVdef8 ) {
	 	setAttributeValue("vdef8", newVdef8);
	} 	  
	/**
	 * 属性nprice的Getter方法.
	 * 创建日期:2010-08-10 09:58:28
	 * @return UFDouble
	 */
	public UFDouble getNprice () {
		return (UFDouble)getAttributeValue("nprice");
	}   
	/**
	 * 属性nprice的Setter方法.
	 * 创建日期:2010-08-10 09:58:28
	 * @param newNprice UFDouble
	 */
	public void setNprice (UFDouble newNprice ) {
	 	setAttributeValue("nprice", newNprice);
	} 	  
	/**
	 * 属性ninstoretaxmny的Getter方法.
	 * 创建日期:2010-08-10 09:58:28
	 * @return UFDouble
	 */
	public UFDouble getNinstoretaxmny () {
		return (UFDouble)getAttributeValue("ninstoretaxmny");
	}   
	/**
	 * 属性ninstoretaxmny的Setter方法.
	 * 创建日期:2010-08-10 09:58:28
	 * @param newNinstoretaxmny UFDouble
	 */
	public void setNinstoretaxmny (UFDouble newNinstoretaxmny ) {
	 	setAttributeValue("ninstoretaxmny", newNinstoretaxmny);
	} 	  
	/**
	 * 属性vdef6的Getter方法.
	 * 创建日期:2010-08-10 09:58:28
	 * @return String
	 */
	public String getVdef6 () {
		return (String)getAttributeValue("vdef6");
	}   
	/**
	 * 属性vdef6的Setter方法.
	 * 创建日期:2010-08-10 09:58:28
	 * @param newVdef6 String
	 */
	public void setVdef6 (String newVdef6 ) {
	 	setAttributeValue("vdef6", newVdef6);
	} 	  
	/**
	 * 属性vdef10的Getter方法.
	 * 创建日期:2010-08-10 09:58:28
	 * @return String
	 */
	public String getVdef10 () {
		return (String)getAttributeValue("vdef10");
	}   
	/**
	 * 属性vdef10的Setter方法.
	 * 创建日期:2010-08-10 09:58:28
	 * @param newVdef10 String
	 */
	public void setVdef10 (String newVdef10 ) {
		setAttributeValue("vdef10", newVdef10);
	} 	  
	/**
	 * 属性nnumber的Getter方法.
	 * 创建日期:2010-08-10 09:58:28
	 * @return UFDouble
	 */
	public UFDouble getNnumber () {
		return (UFDouble)getAttributeValue("nnumber");
	}   
	/**
	 * 属性nnumber的Setter方法.
	 * 创建日期:2010-08-10 09:58:28
	 * @param newNnumber UFDouble
	 */
	public void setNnumber (UFDouble newNnumber ) {
	 	setAttributeValue("nnumber", newNnumber);
	} 	  
	/**
	 * 属性vdef5的Getter方法.
	 * 创建日期:2010-08-10 09:58:28
	 * @return String
	 */
	public String getVdef5 () {
		return (String)getAttributeValue("vdef5");
	}   
	/**
	 * 属性vdef5的Setter方法.
	 * 创建日期:2010-08-10 09:58:28
	 * @param newVdef5 String
	 */
	public void setVdef5 (String newVdef5 ) {
	 	setAttributeValue("vdef5", newVdef5);
	} 	  
 
	/**
	  * <p>取得父VO主键字段.
	  * <p>
	  * 创建日期:2010-08-10 09:58:28
	  * @return java.lang.String
	  */
	public java.lang.String getParentPKFieldName() {
	    return null;
	}   
    
	/**
	  * <p>取得表主键.
	  * <p>
	  * 创建日期:2010-08-10 09:58:28
	  * @return java.lang.String
	  */
	public java.lang.String getPKFieldName() {
	  return "pk_informantioncost";
	}
    
	/**
	 * <p>返回表名称.
	 * <p>
	 * 创建日期:2010-08-10 09:58:28
	 * @return java.lang.String
	 */
	public java.lang.String getTableName() {
		return "jj_scm_informationcost";
	}    
    
    /**
	  * 按照默认方式创建构造子.
	  *
	  * 创建日期:2010-08-10 09:58:28
	  */
     public InformationCostVO() {
		super();	
	}
	@Override
	public Class getVOMetaClass() {
		// TODO Auto-generated method stub
		return nc.vo.scm.jjvo.InformationCostVOMeta.class;
	}


} 
