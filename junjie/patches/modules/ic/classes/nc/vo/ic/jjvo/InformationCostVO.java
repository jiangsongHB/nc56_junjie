
package nc.vo.ic.jjvo;
	
import nc.vo.pub.lang.*;
import nc.vo.scm.bd.SmartVODataUtils;
import nc.vo.scm.pub.smart.SmartVO;
	

/**
 * @function ������ϢVO
 * 
 * @author QuSida
 * 
 * @date 2010-8-10 ����11:19:14
 *
 */
@SuppressWarnings("serial")
public  class InformationCostVO extends SmartVO {
	
	/**
	 * �ֶ���ϸ��Ϣ��ο�nc.vo.scm.jjvo.InformationCostVOMeta.java
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
	 * ����vtaxtype��Getter����.
	 * ��������:2010-08-10 09:58:28
	 * @return String
	 */

	public String getVtaxtype () {		
		 return (String) getAttributeValue("vtaxtype");
	}   
	/**
	 * ����vtaxtype��Setter����.
	 * ��������:2010-08-10 09:58:28
	 * @param newVtaxtype String
	 */
	public void setVtaxtype (String newVtaxtype ) {
		setAttributeValue("vtaxtype", newVtaxtype);
	} 	  
	/**
	 * ����ninstoreoriginalcurtaxmny��Getter����.
	 * ��������:2010-08-10 09:58:28
	 * @return UFDouble
	 */
	public UFDouble getNinstoreoriginalcurtaxmny () {		
		 return (UFDouble) getAttributeValue("ninstoreoriginalcurtaxmny");
	}   
	/**
	 * ����ninstoreoriginalcurtaxmny��Setter����.
	 * ��������:2010-08-10 09:58:28
	 * @param newNinstoreoriginalcurtaxmny UFDouble
	 */
	public void setNinstoreoriginalcurtaxmny (UFDouble newNinstoreoriginalcurtaxmny ) {
	 	setAttributeValue("ninstoreoriginalcurtaxmny", newNinstoreoriginalcurtaxmny);
	} 	  
	/**
	 * ����pk_taxitems��Getter����.
	 * ��������:2010-08-10 09:58:28
	 * @return String
	 */
	public String getPk_taxitems () {
	
		return (String) getAttributeValue("pk_taxitems");
	}   
	/**
	 * ����pk_taxitems��Setter����.
	 * ��������:2010-08-10 09:58:28
	 * @param newPk_taxitems String
	 */
	public void setPk_taxitems (String newPk_taxitems ) {
	 	setAttributeValue("pk_taxitems", newPk_taxitems);
	} 	  
	/**
	 * ����vdef4��Getter����.
	 * ��������:2010-08-10 09:58:28
	 * @return String
	 */
	public String getVdef4 () {
		return (String) getAttributeValue("vdef4");
	}   
	/**
	 * ����vdef4��Setter����.
	 * ��������:2010-08-10 09:58:28
	 * @param newVdef4 String
	 */
	public void setVdef4 (String newVdef4 ) {
	 	setAttributeValue("vdef4", newVdef4);
	} 	  
	/**
	 * ����ninvoriginalcurtaxmny��Getter����.
	 * ��������:2010-08-10 09:58:28
	 * @return UFDouble
	 */
	public UFDouble getNinvoriginalcurtaxmny () {

		return (UFDouble) getAttributeValue("ninvoriginalcurtaxmny");
	}   
	/**
	 * ����ninvoriginalcurtaxmny��Setter����.
	 * ��������:2010-08-10 09:58:28
	 * @param newNinvoriginalcurtaxmny UFDouble
	 */
	public void setNinvoriginalcurtaxmny (UFDouble newNinvoriginalcurtaxmny ) {
	 	setAttributeValue("ninvoriginalcurtaxmny", newNinvoriginalcurtaxmny);
	} 	  
	/**
	 * ����noriginalcurmny��Getter����.
	 * ��������:2010-08-10 09:58:28
	 * @return UFDouble
	 */
	public UFDouble getNoriginalcurmny () {
		return (UFDouble) getAttributeValue("noriginalcurmny");
	}   
	/**
	 * ����noriginalcurmny��Setter����.
	 * ��������:2010-08-10 09:58:28
	 * @param newNoriginalcurmny UFDouble
	 */
	public void setNoriginalcurmny (UFDouble newNoriginalcurmny ) {
	 	setAttributeValue("noriginalcurmny", newNoriginalcurmny);

	} 	  
	/**
	 * ����vdef7��Getter����.
	 * ��������:2010-08-10 09:58:28
	 * @return String
	 */
	public String getVdef7 () {
		return (String) getAttributeValue("vdef7");
	}   
	/**
	 * ����vdef7��Setter����.
	 * ��������:2010-08-10 09:58:28
	 * @param newVdef7 String
	 */
	public void setVdef7 (String newVdef7 ) {
	 	setAttributeValue("vdef7", newVdef7);
	} 	  
	/**
	 * ����ntaxprice��Getter����.
	 * ��������:2010-08-10 09:58:28
	 * @return UFDouble
	 */
	public UFDouble getNtaxprice () {
		return (UFDouble)getAttributeValue("ntaxprice");
	}   
	/**
	 * ����ntaxprice��Setter����.
	 * ��������:2010-08-10 09:58:28
	 * @param newNtaxprice UFDouble
	 */
	public void setNtaxprice (UFDouble newNtaxprice ) {
	 	setAttributeValue("ntaxprice", newNtaxprice);
	} 	 
	/**
	 * ����dr��Getter����.
	 * ��������:2010-08-10 09:58:28
	 * @return UFDouble
	 */
	public Integer getDr () {
		return (Integer)getAttributeValue("dr");
	}   
	/**
	 * ����dr��Setter����.
	 * ��������:2010-08-10 09:58:28
	 * @param newDr UFDouble
	 */
	public void setDr (Integer newDr ) {
	 	setAttributeValue("dr", newDr);
	} 	  
	/**
	 * ����nmny��Getter����.
	 * ��������:2010-08-10 09:58:28
	 * @return UFDouble
	 */
	public UFDouble getNmny () {
		return (UFDouble)getAttributeValue("nmny");
	}   
	/**
	 * ����nmny��Setter����.
	 * ��������:2010-08-10 09:58:28
	 * @param newNmny UFDouble
	 */
	public void setNmny (UFDouble newNmny ) {
	 	setAttributeValue("nmny", newNmny);
	} 	  
	/**
	 * ����currtypeid��Getter����.
	 * ��������:2010-08-10 09:58:28
	 * @return String
	 */
	public String getCurrtypeid () {
		return (String)getAttributeValue("currtypeid");
	}   
	/**
	 * ����currtypeid��Setter����.
	 * ��������:2010-08-10 09:58:28
	 * @param newCurrtypeid String
	 */
	public void setCurrtypeid (String newCurrtypeid ) {
	 	setAttributeValue("currtypeid", newCurrtypeid);
	} 	  
	/**
	 * ����vdef2��Getter����.
	 * ��������:2010-08-10 09:58:28
	 * @return String
	 */
	public String getVdef2 () {
		return (String)getAttributeValue("vdef2");
	}   
	/**
	 * ����vdef2��Setter����.
	 * ��������:2010-08-10 09:58:28
	 * @param newVdef2 String
	 */
	public void setVdef2 (String newVdef2 ) {
	 	setAttributeValue("vdef2", newVdef2);
	} 	  
	/**
	 * ����pk_informantioncost��Getter����.
	 * ��������:2010-08-10 09:58:28
	 * @return String
	 */
	public String getPk_informantioncost () {
		return (String)getAttributeValue("pk_informantioncost");
	}   
	/**
	 * ����pk_informantioncost��Setter����.
	 * ��������:2010-08-10 09:58:28
	 * @param newPk_informantioncost String
	 */
	public void setPk_informantioncost (String newPk_informantioncost ) {
	 	setAttributeValue("pk_informantioncost", newPk_informantioncost);
	} 	  
	/**
	 * ����ninstoremny��Getter����.
	 * ��������:2010-08-10 09:58:28
	 * @return UFDouble
	 */
	public UFDouble getNinstoremny () {
		return (UFDouble)getAttributeValue("ninstoremny");
	}   
	/**
	 * ����ninstoremny��Setter����.
	 * ��������:2010-08-10 09:58:28
	 * @param newNinstoremny UFDouble
	 */
	public void setNinstoremny (UFDouble newNinstoremny ) {
	 	setAttributeValue("ninstoremny", newNinstoremny);
	} 	  
	/**
	 * ����ninvtaxmny��Getter����.
	 * ��������:2010-08-10 09:58:28
	 * @return UFDouble
	 */
	public UFDouble getNinvtaxmny () {
		return (UFDouble)getAttributeValue("ninvtaxmny");
	}   
	/**
	 * ����ninvtaxmny��Setter����.
	 * ��������:2010-08-10 09:58:28
	 * @param newNinvtaxmny UFDouble
	 */
	public void setNinvtaxmny (UFDouble newNinvtaxmny ) {
	 	setAttributeValue("ninvtaxmny", newNinvtaxmny);
	} 	  
	/**
	 * ����vmemo��Getter����.
	 * ��������:2010-08-10 09:58:28
	 * @return String
	 */
	public String getVmemo () {
		return (String)getAttributeValue("vmemo");
	}   
	/**
	 * ����vmemo��Setter����.
	 * ��������:2010-08-10 09:58:28
	 * @param newVmemo String
	 */
	public void setVmemo (String newVmemo ) {
	 	setAttributeValue("vmemo", newVmemo);
	} 	  
	/**
	 * ����vcosttype��Getter����.
	 * ��������:2010-08-10 09:58:28
	 * @return String
	 */
	public String getVcosttype () {
		return (String)getAttributeValue("vcosttype");
	}   
	/**
	 * ����vcosttype��Setter����.
	 * ��������:2010-08-10 09:58:28
	 * @param newVcosttype String
	 */
	public void setVcosttype (String newVcosttype ) {
	 	setAttributeValue("vcosttype", newVcosttype);
	} 	  
	/**
	 * ����cbillid��Getter����.
	 * ��������:2010-08-10 09:58:28
	 * @return String
	 */
	public String getCbillid () {
		return (String)getAttributeValue("cbillid");
	}   
	/**
	 * ����cbillid��Setter����.
	 * ��������:2010-08-10 09:58:28
	 * @param newCbillid String
	 */
	public void setCbillid (String newCbillid ) {
	 	setAttributeValue("cbillid", newCbillid);
	} 	  
	/**
	 * ����ts��Getter����.
	 * ��������:2010-08-10 09:58:28
	 * @return UFDateTime
	 */
	public UFDateTime getTs () {
		Object oValue = getAttributeValue("ts");
    return SmartVODataUtils.getUFDateTime(oValue);}   
	/**
	 * ����ts��Setter����.
	 * ��������:2010-08-10 09:58:28
	 * @param newTs UFDateTime
	 */
	public void setTs (UFDateTime newTs ) {
	 	setAttributeValue("ts", newTs);
	} 	  
	/**
	 * ����costname��Getter����.
	 * ��������:2010-08-10 09:58:28
	 * @return String
	 */
	public String getCostname () {
		return (String)getAttributeValue("costname");
	}   
	/**
	 * ����costname��Setter����.
	 * ��������:2010-08-10 09:58:28
	 * @param newCostname String
	 */
	public void setCostname (String newCostname ) {
	 	setAttributeValue("costname", newCostname);
	} 	  
	/**
	 * ����vdef1��Getter����.
	 * ��������:2010-08-10 09:58:28
	 * @return String
	 */
	public String getVdef1 () {
		return (String)getAttributeValue("vdef1");
	}   
	/**
	 * ����vdef1��Setter����.
	 * ��������:2010-08-10 09:58:28
	 * @param newVdef1 String
	 */
	public void setVdef1 (String newVdef1 ) {
	 	setAttributeValue("vdef1", newVdef1);
	} 	  
	/**
	 * ����ccostunitid��Getter����.
	 * ��������:2010-08-10 09:58:28
	 * @return String
	 */
	public String getCcostunitid (){
		return (String)getAttributeValue("ccostunitid");
	}   
	/**
	 * ����ccostunitid��Setter����.
	 * ��������:2010-08-10 09:58:28
	 * @param newCcostunitid String
	 */
	public void setCcostunitid (String newCcostunitid ) {
	 	setAttributeValue("ccostunitid", newCcostunitid);
	} 	  
	/**
	 * ����cmeasdocid��Getter����.
	 * ��������:2010-08-10 09:58:28
	 * @return String
	 */
	public String getCmeasdocid () {
		return (String)getAttributeValue("cmeasdocid");
	}   
	/**
	 * ����cmeasdocid��Setter����.
	 * ��������:2010-08-10 09:58:28
	 * @param newCmeasdocid String
	 */
	public void setCmeasdocid (String newCmeasdocid ) {
	 	setAttributeValue("cmeasdocid", newCmeasdocid);
	} 	  
	/**
	 * ����ninstoreoriginalcurmny��Getter����.
	 * ��������:2010-08-10 09:58:28
	 * @return UFDouble
	 */
	public UFDouble getNinstoreoriginalcurmny () {
		return (UFDouble)getAttributeValue("ninstoreoriginalcurmny");
	}   
	/**
	 * ����ninstoreoriginalcurmny��Setter����.
	 * ��������:2010-08-10 09:58:28
	 * @param newNinstoreoriginalcurmny UFDouble
	 */
	public void setNinstoreoriginalcurmny (UFDouble newNinstoreoriginalcurmny ) {
	 	setAttributeValue("ninstoreoriginalcurmny", newNinstoreoriginalcurmny);
	} 	  
	/**
	 * ����noriginalcurtaxprice��Getter����.
	 * ��������:2010-08-10 09:58:28
	 * @return UFDouble
	 */
	public UFDouble getNoriginalcurtaxprice () {
		return (UFDouble)getAttributeValue("noriginalcurtaxprice");
	}   
	/**
	 * ����noriginalcurtaxprice��Setter����.
	 * ��������:2010-08-10 09:58:28
	 * @param newNoriginalcurtaxprice UFDouble
	 */
	public void setNoriginalcurtaxprice (UFDouble newNoriginalcurtaxprice ) {
	 	setAttributeValue("noriginalcurtaxprice", newNoriginalcurtaxprice);
	} 	  
	/**
	 * ����ninvoriginalcurmny��Getter����.
	 * ��������:2010-08-10 09:58:28
	 * @return UFDouble
	 */
	public UFDouble getNinvoriginalcurmny () {
		return (UFDouble)getAttributeValue("ninvoriginalcurmny");
	}   
	/**
	 * ����ninvoriginalcurmny��Setter����.
	 * ��������:2010-08-10 09:58:28
	 * @param newNinvoriginalcurmny UFDouble
	 */
	public void setNinvoriginalcurmny (UFDouble newNinvoriginalcurmny ) {
	 	setAttributeValue("ninvoriginalcurmny", newNinvoriginalcurmny);
	} 	  
	/**
	 * ����costcode��Getter����.
	 * ��������:2010-08-10 09:58:28
	 * @return String
	 */
	public String getCostcode () {
		return (String)getAttributeValue("costcode");
	}   
	/**
	 * ����costcode��Setter����.
	 * ��������:2010-08-10 09:58:28
	 * @param newCostcode String
	 */
	public void setCostcode (String newCostcode ) {
	 	setAttributeValue("costcode", newCostcode);
	} 	  
	/**
	 * ����vdef3��Getter����.
	 * ��������:2010-08-10 09:58:28
	 * @return String
	 */
	public String getVdef3 () {
		return (String)getAttributeValue("vdef3");
	}   
	/**
	 * ����vdef3��Setter����.
	 * ��������:2010-08-10 09:58:28
	 * @param newVdef3 String
	 */
	public void setVdef3 (String newVdef3 ) {
	 	setAttributeValue("vdef3", newVdef3);
	} 	  
	/**
	 * ����vdef9��Getter����.
	 * ��������:2010-08-10 09:58:28
	 * @return String
	 */
	public String getVdef9 () {
		return (String)getAttributeValue("vdef9");
	}   
	/**
	 * ����vdef9��Setter����.
	 * ��������:2010-08-10 09:58:28
	 * @param newVdef9 String
	 */
	public void setVdef9 (String newVdef9 ) {
	 	setAttributeValue("vdef9", newVdef9);
	} 	  
	/**
	 * ����nsummny��Getter����.
	 * ��������:2010-08-10 09:58:28
	 * @return UFDouble
	 */
	public UFDouble getNsummny () {
		return (UFDouble)getAttributeValue("nsummny");
	}   
	/**
	 * ����nsummny��Setter����.
	 * ��������:2010-08-10 09:58:28
	 * @param newNsummny UFDouble
	 */
	public void setNsummny (UFDouble newNsummny ) {
	 	setAttributeValue("nsummny", newNsummny);
	} 	  
	/**
	 * ����ninvmny��Getter����.
	 * ��������:2010-08-10 09:58:28
	 * @return UFDouble
	 */
	public UFDouble getNinvmny () {
		return (UFDouble)getAttributeValue("ninvmny");
	}   
	/**
	 * ����ninvmny��Setter����.
	 * ��������:2010-08-10 09:58:28
	 * @param newNinvmny UFDouble
	 */
	public void setNinvmny (UFDouble newNinvmny ) {
	 	setAttributeValue("ninvmny", newNinvmny);
	} 	  
	/**
	 * ����vdef8��Getter����.
	 * ��������:2010-08-10 09:58:28
	 * @return String
	 */
	public String getVdef8 () {
		return (String)getAttributeValue("vdef8");
	}   
	/**
	 * ����vdef8��Setter����.
	 * ��������:2010-08-10 09:58:28
	 * @param newVdef8 String
	 */
	public void setVdef8 (String newVdef8 ) {
	 	setAttributeValue("vdef8", newVdef8);
	} 	  
	/**
	 * ����nprice��Getter����.
	 * ��������:2010-08-10 09:58:28
	 * @return UFDouble
	 */
	public UFDouble getNprice () {
		return (UFDouble)getAttributeValue("nprice");
	}   
	/**
	 * ����nprice��Setter����.
	 * ��������:2010-08-10 09:58:28
	 * @param newNprice UFDouble
	 */
	public void setNprice (UFDouble newNprice ) {
	 	setAttributeValue("nprice", newNprice);
	} 	  
	/**
	 * ����ninstoretaxmny��Getter����.
	 * ��������:2010-08-10 09:58:28
	 * @return UFDouble
	 */
	public UFDouble getNinstoretaxmny () {
		return (UFDouble)getAttributeValue("ninstoretaxmny");
	}   
	/**
	 * ����ninstoretaxmny��Setter����.
	 * ��������:2010-08-10 09:58:28
	 * @param newNinstoretaxmny UFDouble
	 */
	public void setNinstoretaxmny (UFDouble newNinstoretaxmny ) {
	 	setAttributeValue("ninstoretaxmny", newNinstoretaxmny);
	} 	  
	/**
	 * ����vdef6��Getter����.
	 * ��������:2010-08-10 09:58:28
	 * @return String
	 */
	public String getVdef6 () {
		return (String)getAttributeValue("vdef6");
	}   
	/**
	 * ����vdef6��Setter����.
	 * ��������:2010-08-10 09:58:28
	 * @param newVdef6 String
	 */
	public void setVdef6 (String newVdef6 ) {
	 	setAttributeValue("vdef6", newVdef6);
	} 	  
	/**
	 * ����vdef10��Getter����.
	 * ��������:2010-08-10 09:58:28
	 * @return String
	 */
	public String getVdef10 () {
		return (String)getAttributeValue("vdef10");
	}   
	/**
	 * ����vdef10��Setter����.
	 * ��������:2010-08-10 09:58:28
	 * @param newVdef10 String
	 */
	public void setVdef10 (String newVdef10 ) {
		setAttributeValue("vdef10", newVdef10);
	} 	  
	/**
	 * ����nnumber��Getter����.
	 * ��������:2010-08-10 09:58:28
	 * @return UFDouble
	 */
	public UFDouble getNnumber () {
		return (UFDouble)getAttributeValue("nnumber");
	}   
	/**
	 * ����nnumber��Setter����.
	 * ��������:2010-08-10 09:58:28
	 * @param newNnumber UFDouble
	 */
	public void setNnumber (UFDouble newNnumber ) {
	 	setAttributeValue("nnumber", newNnumber);
	} 	  
	/**
	 * ����vdef5��Getter����.
	 * ��������:2010-08-10 09:58:28
	 * @return String
	 */
	public String getVdef5 () {
		return (String)getAttributeValue("vdef5");
	}   
	/**
	 * ����vdef5��Setter����.
	 * ��������:2010-08-10 09:58:28
	 * @param newVdef5 String
	 */
	public void setVdef5 (String newVdef5 ) {
	 	setAttributeValue("vdef5", newVdef5);
	} 	  
 
	/**
	  * <p>ȡ�ø�VO�����ֶ�.
	  * <p>
	  * ��������:2010-08-10 09:58:28
	  * @return java.lang.String
	  */
	public java.lang.String getParentPKFieldName() {
	    return null;
	}   
    
	/**
	  * <p>ȡ�ñ�����.
	  * <p>
	  * ��������:2010-08-10 09:58:28
	  * @return java.lang.String
	  */
	public java.lang.String getPKFieldName() {
	  return "pk_informantioncost";
	}
    
	/**
	 * <p>���ر�����.
	 * <p>
	 * ��������:2010-08-10 09:58:28
	 * @return java.lang.String
	 */
	public java.lang.String getTableName() {
		return "jj_scm_informationcost";
	}    
    
    /**
	  * ����Ĭ�Ϸ�ʽ����������.
	  *
	  * ��������:2010-08-10 09:58:28
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
