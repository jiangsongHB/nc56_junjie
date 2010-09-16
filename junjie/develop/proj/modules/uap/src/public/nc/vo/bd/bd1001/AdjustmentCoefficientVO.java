
package nc.vo.bd.bd1001;
	
import nc.vo.pub.*;
import nc.vo.pub.lang.*;
	
/**
 * @description 理算系数VO
 * 
 * @author QuSida
 * 
 * @date 2010-08-05 10:25:45
 */
@SuppressWarnings("serial")
public class AdjustmentCoefficientVO extends SuperVO {
	private String pk_adjustmentcoefficient; //理算系数主键	
	private String vmemo;//备注
	private String vinvspec;//规格
	private String pk_invbasdoc;//存货基本档案ID
	private UFDouble nadjustmentcoefficient;//理算系数
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
	 * 属性pk_adjustmentcoefficient的Getter方法.
	 * 创建日期:2010-08-05 10:25:45
	 * @return String
	 */
	public String getPk_adjustmentcoefficient () {
		return pk_adjustmentcoefficient;
	}   
	/**
	 * 属性pk_adjustmentcoefficient的Setter方法.
	 * 创建日期:2010-08-05 10:25:45
	 * @param newPk_adjustmentcoefficient String
	 */
	public void setPk_adjustmentcoefficient (String newPk_adjustmentcoefficient ) {
	 	this.pk_adjustmentcoefficient = newPk_adjustmentcoefficient;
	} 	  
	/**
	 * 属性ts的Getter方法.
	 * 创建日期:2010-08-05 10:25:45
	 * @return UFDateTime
	 */
	public String getTs () {
		return ts;
	}   
	/**
	 * 属性ts的Setter方法.
	 * 创建日期:2010-08-05 10:25:45
	 * @param newTs UFDateTime
	 */
	public void setTs (String newTs ) {
	 	this.ts = newTs;
	} 	  
	/**
	 * 属性vdef9的Getter方法.
	 * 创建日期:2010-08-05 10:25:45
	 * @return String
	 */
	public String getVdef9 () {
		return vdef9;
	}   
	/**
	 * 属性vdef9的Setter方法.
	 * 创建日期:2010-08-05 10:25:45
	 * @param newVdef9 String
	 */
	public void setVdef9 (String newVdef9 ) {
	 	this.vdef9 = newVdef9;
	} 	  
	/**
	 * 属性vdef1的Getter方法.
	 * 创建日期:2010-08-05 10:25:45
	 * @return String
	 */
	public String getVdef1 () {
		return vdef1;
	}   
	/**
	 * 属性vdef1的Setter方法.
	 * 创建日期:2010-08-05 10:25:45
	 * @param newVdef1 String
	 */
	public void setVdef1 (String newVdef1 ) {
	 	this.vdef1 = newVdef1;
	} 	  
	/**
	 * 属性vdef8的Getter方法.
	 * 创建日期:2010-08-05 10:25:45
	 * @return String
	 */
	public String getVdef8 () {
		return vdef8;
	}   
	/**
	 * 属性vdef8的Setter方法.
	 * 创建日期:2010-08-05 10:25:45
	 * @param newVdef8 String
	 */
	public void setVdef8 (String newVdef8 ) {
	 	this.vdef8 = newVdef8;
	} 	  
	/**
	 * 属性vdef10的Getter方法.
	 * 创建日期:2010-08-05 10:25:45
	 * @return String
	 */
	public String getVdef10 () {
		return vdef10;
	}   
	/**
	 * 属性vdef10的Setter方法.
	 * 创建日期:2010-08-05 10:25:45
	 * @param newVdef10 String
	 */
	public void setVdef10 (String newVdef10 ) {
	 	this.vdef10 = newVdef10;
	} 	  
	/**
	 * 属性vdef2的Getter方法.
	 * 创建日期:2010-08-05 10:25:45
	 * @return String
	 */
	public String getVdef2 () {
		return vdef2;
	}   
	/**
	 * 属性vdef2的Setter方法.
	 * 创建日期:2010-08-05 10:25:45
	 * @param newVdef2 String
	 */
	public void setVdef2 (String newVdef2 ) {
	 	this.vdef2 = newVdef2;
	} 	  
	/**
	 * 属性vdef5的Getter方法.
	 * 创建日期:2010-08-05 10:25:45
	 * @return String
	 */
	public String getVdef5 () {
		return vdef5;
	}   
	/**
	 * 属性vdef5的Setter方法.
	 * 创建日期:2010-08-05 10:25:45
	 * @param newVdef5 String
	 */
	public void setVdef5 (String newVdef5 ) {
	 	this.vdef5 = newVdef5;
	} 	  
	/**
	 * 属性vmemo的Getter方法.
	 * 创建日期:2010-08-05 10:25:45
	 * @return String
	 */
	public String getVmemo () {
		return vmemo;
	}   
	/**
	 * 属性vmemo的Setter方法.
	 * 创建日期:2010-08-05 10:25:45
	 * @param newVmemo String
	 */
	public void setVmemo (String newVmemo ) {
	 	this.vmemo = newVmemo;
	} 	  
	/**
	 * 属性vinvspec的Getter方法.
	 * 创建日期:2010-08-05 10:25:45
	 * @return String
	 */
	public String getVinvspec () {
		return vinvspec;
	}   
	/**
	 * 属性vinvspec的Setter方法.
	 * 创建日期:2010-08-05 10:25:45
	 * @param newVinvspec String
	 */
	public void setVinvspec (String newVinvspec ) {
	 	this.vinvspec = newVinvspec;
	} 	  
	/**
	 * 属性pk_invbasdoc的Getter方法.
	 * 创建日期:2010-08-05 10:25:45
	 * @return String
	 */
	public String getPk_invbasdoc () {
		return pk_invbasdoc;
	}   
	/**
	 * 属性pk_invbasdoc的Setter方法.
	 * 创建日期:2010-08-05 10:25:45
	 * @param newPk_invbasdoc String
	 */
	public void setPk_invbasdoc (String newPk_invbasdoc ) {
	 	this.pk_invbasdoc = newPk_invbasdoc;
	} 	  
	/**
	 * 属性vdef3的Getter方法.
	 * 创建日期:2010-08-05 10:25:45
	 * @return String
	 */
	public String getVdef3 () {
		return vdef3;
	}   
	/**
	 * 属性vdef3的Setter方法.
	 * 创建日期:2010-08-05 10:25:45
	 * @param newVdef3 String
	 */
	public void setVdef3 (String newVdef3 ) {
	 	this.vdef3 = newVdef3;
	} 	  
	/**
	 * 属性vdef6的Getter方法.
	 * 创建日期:2010-08-05 10:25:45
	 * @return String
	 */
	public String getVdef6 () {
		return vdef6;
	}   
	/**
	 * 属性vdef6的Setter方法.
	 * 创建日期:2010-08-05 10:25:45
	 * @param newVdef6 String
	 */
	public void setVdef6 (String newVdef6 ) {
	 	this.vdef6 = newVdef6;
	} 	  
	/**
	 * 属性dr的Getter方法.
	 * 创建日期:2010-08-05 10:25:45
	 * @return UFDouble
	 */
	public Integer getDr () {
		return dr;
	}   
	/**
	 * 属性dr的Setter方法.
	 * 创建日期:2010-08-05 10:25:45
	 * @param newDr UFDouble
	 */
	public void setDr (Integer newDr ) {
	 	this.dr = newDr;
	} 	  
	/**
	 * 属性nadjustmentcoefficient的Getter方法.
	 * 创建日期:2010-08-05 10:25:45
	 * @return UFDouble
	 */
	public UFDouble getNadjustmentcoefficient () {
		return nadjustmentcoefficient;
	}   
	/**
	 * 属性nadjustmentcoefficient的Setter方法.
	 * 创建日期:2010-08-05 10:25:45
	 * @param newNadjustmentcoefficient UFDouble
	 */
	public void setNadjustmentcoefficient (UFDouble newNadjustmentcoefficient ) {
	 	this.nadjustmentcoefficient = newNadjustmentcoefficient;
	} 	  
	/**
	 * 属性vdef4的Getter方法.
	 * 创建日期:2010-08-05 10:25:45
	 * @return String
	 */
	public String getVdef4 () {
		return vdef4;
	}   
	/**
	 * 属性vdef4的Setter方法.
	 * 创建日期:2010-08-05 10:25:45
	 * @param newVdef4 String
	 */
	public void setVdef4 (String newVdef4 ) {
	 	this.vdef4 = newVdef4;
	} 	  
	/**
	 * 属性vdef7的Getter方法.
	 * 创建日期:2010-08-05 10:25:45
	 * @return String
	 */
	public String getVdef7 () {
		return vdef7;
	}   
	/**
	 * 属性vdef7的Setter方法.
	 * 创建日期:2010-08-05 10:25:45
	 * @param newVdef7 String
	 */
	public void setVdef7 (String newVdef7 ) {
	 	this.vdef7 = newVdef7;
	} 	  
 
	/**
	  * <p>取得父VO主键字段.
	  * <p>
	  * 创建日期:2010-08-05 10:25:45
	  * @return java.lang.String
	  */
	public java.lang.String getParentPKFieldName() {
	    return null;
	}   
    
	/**
	  * <p>取得表主键.
	  * <p>
	  * 创建日期:2010-08-05 10:25:45
	  * @return java.lang.String
	  */
	public java.lang.String getPKFieldName() {
	  return "pk_adjustmentcoefficient";
	}
    
	/**
	 * <p>返回表名称.
	 * <p>
	 * 创建日期:2010-08-05 10:25:45
	 * @return java.lang.String
	 */
	public java.lang.String getTableName() {
		return "JJ_BD_ADJUSTMENTCOEFFICIENT";
	}    
    
    /**
	  * 按照默认方式创建构造子.
	  *
	  * 创建日期:2010-08-05 10:25:45
	  */
     public AdjustmentCoefficientVO() {
		super();	
	}    
} 
