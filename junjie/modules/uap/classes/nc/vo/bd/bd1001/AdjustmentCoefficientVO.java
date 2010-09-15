
package nc.vo.bd.bd1001;
	
import nc.vo.pub.*;
import nc.vo.pub.lang.*;
	
/**
 * @description ����ϵ��VO
 * 
 * @author QuSida
 * 
 * @date 2010-08-05 10:25:45
 */
@SuppressWarnings("serial")
public class AdjustmentCoefficientVO extends SuperVO {
	private String pk_adjustmentcoefficient; //����ϵ������	
	private String vmemo;//��ע
	private String vinvspec;//���
	private String pk_invbasdoc;//�����������ID
	private UFDouble nadjustmentcoefficient;//����ϵ��
	private String ts;
	private Integer dr;
	private String vdef1;
	private String vdef2;
	private String vdef3;
	private String vdef4;
	private String vdef5;
	private String vdef6;
	private String vdef7;
	private String vdef8;
	private String vdef9; 
	private String vdef10;
	

	public static final String PK_ADJUSTMENTCOEFFICIENT = "pk_adjustmentcoefficient";
	public static final String VDEF9 = "vdef9";
	public static final String VDEF1 = "vdef1";
	public static final String VDEF8 = "vdef8";
	public static final String VDEF10 = "vdef10";
	public static final String VDEF2 = "vdef2";
	public static final String VDEF5 = "vdef5";
	public static final String VMEMO = "vmemo";
	public static final String VINVSPEC = "vinvspec";
	public static final String PK_INVBASDOC = "pk_invbasdoc";
	public static final String VDEF3 = "vdef3";
	public static final String VDEF6 = "vdef6";
	public static final String NADJUSTMENTCOEFFICIENT = "nadjustmentcoefficient";
	public static final String VDEF4 = "vdef4";
	public static final String VDEF7 = "vdef7";
			
	/**
	 * ����pk_adjustmentcoefficient��Getter����.
	 * ��������:2010-08-05 10:25:45
	 * @return String
	 */
	public String getPk_adjustmentcoefficient () {
		return pk_adjustmentcoefficient;
	}   
	/**
	 * ����pk_adjustmentcoefficient��Setter����.
	 * ��������:2010-08-05 10:25:45
	 * @param newPk_adjustmentcoefficient String
	 */
	public void setPk_adjustmentcoefficient (String newPk_adjustmentcoefficient ) {
	 	this.pk_adjustmentcoefficient = newPk_adjustmentcoefficient;
	} 	  
	/**
	 * ����ts��Getter����.
	 * ��������:2010-08-05 10:25:45
	 * @return UFDateTime
	 */
	public String getTs () {
		return ts;
	}   
	/**
	 * ����ts��Setter����.
	 * ��������:2010-08-05 10:25:45
	 * @param newTs UFDateTime
	 */
	public void setTs (String newTs ) {
	 	this.ts = newTs;
	} 	  
	/**
	 * ����vdef9��Getter����.
	 * ��������:2010-08-05 10:25:45
	 * @return String
	 */
	public String getVdef9 () {
		return vdef9;
	}   
	/**
	 * ����vdef9��Setter����.
	 * ��������:2010-08-05 10:25:45
	 * @param newVdef9 String
	 */
	public void setVdef9 (String newVdef9 ) {
	 	this.vdef9 = newVdef9;
	} 	  
	/**
	 * ����vdef1��Getter����.
	 * ��������:2010-08-05 10:25:45
	 * @return String
	 */
	public String getVdef1 () {
		return vdef1;
	}   
	/**
	 * ����vdef1��Setter����.
	 * ��������:2010-08-05 10:25:45
	 * @param newVdef1 String
	 */
	public void setVdef1 (String newVdef1 ) {
	 	this.vdef1 = newVdef1;
	} 	  
	/**
	 * ����vdef8��Getter����.
	 * ��������:2010-08-05 10:25:45
	 * @return String
	 */
	public String getVdef8 () {
		return vdef8;
	}   
	/**
	 * ����vdef8��Setter����.
	 * ��������:2010-08-05 10:25:45
	 * @param newVdef8 String
	 */
	public void setVdef8 (String newVdef8 ) {
	 	this.vdef8 = newVdef8;
	} 	  
	/**
	 * ����vdef10��Getter����.
	 * ��������:2010-08-05 10:25:45
	 * @return String
	 */
	public String getVdef10 () {
		return vdef10;
	}   
	/**
	 * ����vdef10��Setter����.
	 * ��������:2010-08-05 10:25:45
	 * @param newVdef10 String
	 */
	public void setVdef10 (String newVdef10 ) {
	 	this.vdef10 = newVdef10;
	} 	  
	/**
	 * ����vdef2��Getter����.
	 * ��������:2010-08-05 10:25:45
	 * @return String
	 */
	public String getVdef2 () {
		return vdef2;
	}   
	/**
	 * ����vdef2��Setter����.
	 * ��������:2010-08-05 10:25:45
	 * @param newVdef2 String
	 */
	public void setVdef2 (String newVdef2 ) {
	 	this.vdef2 = newVdef2;
	} 	  
	/**
	 * ����vdef5��Getter����.
	 * ��������:2010-08-05 10:25:45
	 * @return String
	 */
	public String getVdef5 () {
		return vdef5;
	}   
	/**
	 * ����vdef5��Setter����.
	 * ��������:2010-08-05 10:25:45
	 * @param newVdef5 String
	 */
	public void setVdef5 (String newVdef5 ) {
	 	this.vdef5 = newVdef5;
	} 	  
	/**
	 * ����vmemo��Getter����.
	 * ��������:2010-08-05 10:25:45
	 * @return String
	 */
	public String getVmemo () {
		return vmemo;
	}   
	/**
	 * ����vmemo��Setter����.
	 * ��������:2010-08-05 10:25:45
	 * @param newVmemo String
	 */
	public void setVmemo (String newVmemo ) {
	 	this.vmemo = newVmemo;
	} 	  
	/**
	 * ����vinvspec��Getter����.
	 * ��������:2010-08-05 10:25:45
	 * @return String
	 */
	public String getVinvspec () {
		return vinvspec;
	}   
	/**
	 * ����vinvspec��Setter����.
	 * ��������:2010-08-05 10:25:45
	 * @param newVinvspec String
	 */
	public void setVinvspec (String newVinvspec ) {
	 	this.vinvspec = newVinvspec;
	} 	  
	/**
	 * ����pk_invbasdoc��Getter����.
	 * ��������:2010-08-05 10:25:45
	 * @return String
	 */
	public String getPk_invbasdoc () {
		return pk_invbasdoc;
	}   
	/**
	 * ����pk_invbasdoc��Setter����.
	 * ��������:2010-08-05 10:25:45
	 * @param newPk_invbasdoc String
	 */
	public void setPk_invbasdoc (String newPk_invbasdoc ) {
	 	this.pk_invbasdoc = newPk_invbasdoc;
	} 	  
	/**
	 * ����vdef3��Getter����.
	 * ��������:2010-08-05 10:25:45
	 * @return String
	 */
	public String getVdef3 () {
		return vdef3;
	}   
	/**
	 * ����vdef3��Setter����.
	 * ��������:2010-08-05 10:25:45
	 * @param newVdef3 String
	 */
	public void setVdef3 (String newVdef3 ) {
	 	this.vdef3 = newVdef3;
	} 	  
	/**
	 * ����vdef6��Getter����.
	 * ��������:2010-08-05 10:25:45
	 * @return String
	 */
	public String getVdef6 () {
		return vdef6;
	}   
	/**
	 * ����vdef6��Setter����.
	 * ��������:2010-08-05 10:25:45
	 * @param newVdef6 String
	 */
	public void setVdef6 (String newVdef6 ) {
	 	this.vdef6 = newVdef6;
	} 	  
	/**
	 * ����dr��Getter����.
	 * ��������:2010-08-05 10:25:45
	 * @return UFDouble
	 */
	public Integer getDr () {
		return dr;
	}   
	/**
	 * ����dr��Setter����.
	 * ��������:2010-08-05 10:25:45
	 * @param newDr UFDouble
	 */
	public void setDr (Integer newDr ) {
	 	this.dr = newDr;
	} 	  
	/**
	 * ����nadjustmentcoefficient��Getter����.
	 * ��������:2010-08-05 10:25:45
	 * @return UFDouble
	 */
	public UFDouble getNadjustmentcoefficient () {
		return nadjustmentcoefficient;
	}   
	/**
	 * ����nadjustmentcoefficient��Setter����.
	 * ��������:2010-08-05 10:25:45
	 * @param newNadjustmentcoefficient UFDouble
	 */
	public void setNadjustmentcoefficient (UFDouble newNadjustmentcoefficient ) {
	 	this.nadjustmentcoefficient = newNadjustmentcoefficient;
	} 	  
	/**
	 * ����vdef4��Getter����.
	 * ��������:2010-08-05 10:25:45
	 * @return String
	 */
	public String getVdef4 () {
		return vdef4;
	}   
	/**
	 * ����vdef4��Setter����.
	 * ��������:2010-08-05 10:25:45
	 * @param newVdef4 String
	 */
	public void setVdef4 (String newVdef4 ) {
	 	this.vdef4 = newVdef4;
	} 	  
	/**
	 * ����vdef7��Getter����.
	 * ��������:2010-08-05 10:25:45
	 * @return String
	 */
	public String getVdef7 () {
		return vdef7;
	}   
	/**
	 * ����vdef7��Setter����.
	 * ��������:2010-08-05 10:25:45
	 * @param newVdef7 String
	 */
	public void setVdef7 (String newVdef7 ) {
	 	this.vdef7 = newVdef7;
	} 	  
 
	/**
	  * <p>ȡ�ø�VO�����ֶ�.
	  * <p>
	  * ��������:2010-08-05 10:25:45
	  * @return java.lang.String
	  */
	public java.lang.String getParentPKFieldName() {
	    return null;
	}   
    
	/**
	  * <p>ȡ�ñ�����.
	  * <p>
	  * ��������:2010-08-05 10:25:45
	  * @return java.lang.String
	  */
	public java.lang.String getPKFieldName() {
	  return "pk_adjustmentcoefficient";
	}
    
	/**
	 * <p>���ر�����.
	 * <p>
	 * ��������:2010-08-05 10:25:45
	 * @return java.lang.String
	 */
	public java.lang.String getTableName() {
		return "JJ_BD_ADJUSTMENTCOEFFICIENT";
	}    
    
    /**
	  * ����Ĭ�Ϸ�ʽ����������.
	  *
	  * ��������:2010-08-05 10:25:45
	  */
     public AdjustmentCoefficientVO() {
		super();	
	}    
} 
