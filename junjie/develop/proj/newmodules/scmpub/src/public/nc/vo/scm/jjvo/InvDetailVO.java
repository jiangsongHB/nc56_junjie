/***************************************************************\
 *     The skeleton of this class is generated by an automatic *
 * code generator for NC product. It is based on Velocity.     *
\***************************************************************/
package nc.vo.scm.jjvo;
	
import nc.vo.pub.*;
import nc.vo.pub.lang.*;
	
/**
 * <b> 在此处简要描述此类的功能 </b>
 * <p>
 *     在此处添加此类的描述信息
 * </p>
 * 创建日期:2010-12-13 14:30:25
 * @author MeiChao
 * @version NCPrj 1.0
 */
@SuppressWarnings("serial")
public class InvDetailVO extends SuperVO {
	private String pk_corp;
	private UFDateTime ts;
	private String vdef9;
	private String contractlength;
	private String vdef10;
	private String cgeneralbid;
	private UFDouble arrivenumber;
	private String vdef15;
	private UFDateTime modifytime;
	private String vdef14;
	private String vdef7;
	private String modifyoperator;
	private String arrivethick;
	private UFDouble contractweight;
	private String vdef16;
	private String vdef2;
	private String vdef5;
	private UFDouble amount;
	private String vdef19;
	private String pk_invbasdoc;
	private String contractwidth;
	private UFDateTime createtime;
	private UFBoolean booleandef2;
	private String vdef18;
	private String vdef4;
	private String arrivemeter;
	private UFDouble warehousenumber;
	private UFBoolean booleandef3;
	private String vdef17;
	private String pk_invdetail;
	private String vdef20;
	private String vdef1;
	private String vdef8;
	private UFDate modifydate;
	private String carriveorder_bid;
	private UFBoolean booleandef1;
	private String arrivewidth;
	private UFBoolean booleandef5;
	private String corder_bid;
	private String note;
	private UFDouble price;
	private UFDouble arriveweight;
	private String arrivelength;
	private String vdef13;
	private UFDouble ordernumber;
	private UFDouble conversionrates;
	private UFBoolean booleandef4;
	private String contractthick;
	private UFDouble sellweight;
	private String vdef11;
	private String contractmeter;
	private String vdef12;
	private String vdef3;
	private String vdef6;
	private UFDate createdate;
	private Integer dr;
	private String createoperator;
	private String pk_invmandoc;

	public static final String PK_CORP = "pk_corp";
	public static final String VDEF9 = "vdef9";
	public static final String CONTRACTLENGTH = "contractlength";
	public static final String VDEF10 = "vdef10";
	public static final String CGENERALBID = "cgeneralbid";
	public static final String ARRIVENUMBER = "arrivenumber";
	public static final String VDEF15 = "vdef15";
	public static final String MODIFYTIME = "modifytime";
	public static final String VDEF14 = "vdef14";
	public static final String VDEF7 = "vdef7";
	public static final String MODIFYOPERATOR = "modifyoperator";
	public static final String ARRIVETHICK = "arrivethick";
	public static final String CONTRACTWEIGHT = "contractweight";
	public static final String VDEF16 = "vdef16";
	public static final String VDEF2 = "vdef2";
	public static final String VDEF5 = "vdef5";
	public static final String AMOUNT = "amount";
	public static final String VDEF19 = "vdef19";
	public static final String PK_INVBASDOC = "pk_invbasdoc";
	public static final String CONTRACTWIDTH = "contractwidth";
	public static final String CREATETIME = "createtime";
	public static final String BOOLEANDEF2 = "booleandef2";
	public static final String VDEF18 = "vdef18";
	public static final String VDEF4 = "vdef4";
	public static final String ARRIVEMETER = "arrivemeter";
	public static final String WAREHOUSENUMBER = "warehousenumber";
	public static final String BOOLEANDEF3 = "booleandef3";
	public static final String VDEF17 = "vdef17";
	public static final String PK_INVDETAIL = "pk_invdetail";
	public static final String VDEF20 = "vdef20";
	public static final String VDEF1 = "vdef1";
	public static final String VDEF8 = "vdef8";
	public static final String MODIFYDATE = "modifydate";
	public static final String CARRIVEORDER_BID = "carriveorder_bid";
	public static final String BOOLEANDEF1 = "booleandef1";
	public static final String ARRIVEWIDTH = "arrivewidth";
	public static final String BOOLEANDEF5 = "booleandef5";
	public static final String CORDER_BID = "corder_bid";
	public static final String NOTE = "note";
	public static final String PRICE = "price";
	public static final String ARRIVEWEIGHT = "arriveweight";
	public static final String ARRIVELENGTH = "arrivelength";
	public static final String VDEF13 = "vdef13";
	public static final String ORDERNUMBER = "ordernumber";
	public static final String CONVERSIONRATES = "conversionrates";
	public static final String BOOLEANDEF4 = "booleandef4";
	public static final String CONTRACTTHICK = "contractthick";
	public static final String SELLWEIGHT = "sellweight";
	public static final String VDEF11 = "vdef11";
	public static final String CONTRACTMETER = "contractmeter";
	public static final String VDEF12 = "vdef12";
	public static final String VDEF3 = "vdef3";
	public static final String VDEF6 = "vdef6";
	public static final String CREATEDATE = "createdate";
	public static final String CREATEOPERATOR = "createoperator";
	public static final String PK_INVMANDOC = "pk_invmandoc";
			
	/**
	 * 属性pk_corp的Getter方法.
	 * 创建日期:2010-12-13 14:30:25
	 * @return String
	 */
	public String getPk_corp () {
		return pk_corp;
	}   
	/**
	 * 属性pk_corp的Setter方法.
	 * 创建日期:2010-12-13 14:30:25
	 * @param newPk_corp String
	 */
	public void setPk_corp (String newPk_corp ) {
	 	this.pk_corp = newPk_corp;
	} 	  
	/**
	 * 属性ts的Getter方法.
	 * 创建日期:2010-12-13 14:30:25
	 * @return UFDateTime
	 */
	public UFDateTime getTs () {
		return ts;
	}   
	/**
	 * 属性ts的Setter方法.
	 * 创建日期:2010-12-13 14:30:25
	 * @param newTs UFDateTime
	 */
	public void setTs (UFDateTime newTs ) {
	 	this.ts = newTs;
	} 	  
	/**
	 * 属性vdef9的Getter方法.
	 * 创建日期:2010-12-13 14:30:25
	 * @return String
	 */
	public String getVdef9 () {
		return vdef9;
	}   
	/**
	 * 属性vdef9的Setter方法.
	 * 创建日期:2010-12-13 14:30:25
	 * @param newVdef9 String
	 */
	public void setVdef9 (String newVdef9 ) {
	 	this.vdef9 = newVdef9;
	} 	  
	/**
	 * 属性contractlength的Getter方法.
	 * 创建日期:2010-12-13 14:30:25
	 * @return String
	 */
	public String getContractlength () {
		return contractlength;
	}   
	/**
	 * 属性contractlength的Setter方法.
	 * 创建日期:2010-12-13 14:30:25
	 * @param newContractlength String
	 */
	public void setContractlength (String newContractlength ) {
	 	this.contractlength = newContractlength;
	} 	  
	/**
	 * 属性vdef10的Getter方法.
	 * 创建日期:2010-12-13 14:30:25
	 * @return String
	 */
	public String getVdef10 () {
		return vdef10;
	}   
	/**
	 * 属性vdef10的Setter方法.
	 * 创建日期:2010-12-13 14:30:25
	 * @param newVdef10 String
	 */
	public void setVdef10 (String newVdef10 ) {
	 	this.vdef10 = newVdef10;
	} 	  
	/**
	 * 属性cgeneralbid的Getter方法.
	 * 创建日期:2010-12-13 14:30:25
	 * @return String
	 */
	public String getCgeneralbid () {
		return cgeneralbid;
	}   
	/**
	 * 属性cgeneralbid的Setter方法.
	 * 创建日期:2010-12-13 14:30:25
	 * @param newCgeneralbid String
	 */
	public void setCgeneralbid (String newCgeneralbid ) {
	 	this.cgeneralbid = newCgeneralbid;
	} 	  
	/**
	 * 属性arrivenumber的Getter方法.
	 * 创建日期:2010-12-13 14:30:25
	 * @return UFDouble
	 */
	public UFDouble getArrivenumber () {
		return arrivenumber;
	}   
	/**
	 * 属性arrivenumber的Setter方法.
	 * 创建日期:2010-12-13 14:30:25
	 * @param newArrivenumber UFDouble
	 */
	public void setArrivenumber (UFDouble newArrivenumber ) {
	 	this.arrivenumber = newArrivenumber;
	} 	  
	/**
	 * 属性vdef15的Getter方法.
	 * 创建日期:2010-12-13 14:30:25
	 * @return String
	 */
	public String getVdef15 () {
		return vdef15;
	}   
	/**
	 * 属性vdef15的Setter方法.
	 * 创建日期:2010-12-13 14:30:25
	 * @param newVdef15 String
	 */
	public void setVdef15 (String newVdef15 ) {
	 	this.vdef15 = newVdef15;
	} 	  
	/**
	 * 属性modifytime的Getter方法.
	 * 创建日期:2010-12-13 14:30:25
	 * @return UFDateTime
	 */
	public UFDateTime getModifytime () {
		return modifytime;
	}   
	/**
	 * 属性modifytime的Setter方法.
	 * 创建日期:2010-12-13 14:30:25
	 * @param newModifytime UFDateTime
	 */
	public void setModifytime (UFDateTime newModifytime ) {
	 	this.modifytime = newModifytime;
	} 	  
	/**
	 * 属性vdef14的Getter方法.
	 * 创建日期:2010-12-13 14:30:25
	 * @return String
	 */
	public String getVdef14 () {
		return vdef14;
	}   
	/**
	 * 属性vdef14的Setter方法.
	 * 创建日期:2010-12-13 14:30:25
	 * @param newVdef14 String
	 */
	public void setVdef14 (String newVdef14 ) {
	 	this.vdef14 = newVdef14;
	} 	  
	/**
	 * 属性vdef7的Getter方法.
	 * 创建日期:2010-12-13 14:30:25
	 * @return String
	 */
	public String getVdef7 () {
		return vdef7;
	}   
	/**
	 * 属性vdef7的Setter方法.
	 * 创建日期:2010-12-13 14:30:25
	 * @param newVdef7 String
	 */
	public void setVdef7 (String newVdef7 ) {
	 	this.vdef7 = newVdef7;
	} 	  
	/**
	 * 属性modifyoperator的Getter方法.
	 * 创建日期:2010-12-13 14:30:25
	 * @return String
	 */
	public String getModifyoperator () {
		return modifyoperator;
	}   
	/**
	 * 属性modifyoperator的Setter方法.
	 * 创建日期:2010-12-13 14:30:25
	 * @param newModifyoperator String
	 */
	public void setModifyoperator (String newModifyoperator ) {
	 	this.modifyoperator = newModifyoperator;
	} 	  
	/**
	 * 属性arrivethick的Getter方法.
	 * 创建日期:2010-12-13 14:30:25
	 * @return String
	 */
	public String getArrivethick () {
		return arrivethick;
	}   
	/**
	 * 属性arrivethick的Setter方法.
	 * 创建日期:2010-12-13 14:30:25
	 * @param newArrivethick String
	 */
	public void setArrivethick (String newArrivethick ) {
	 	this.arrivethick = newArrivethick;
	} 	  
	/**
	 * 属性contractweight的Getter方法.
	 * 创建日期:2010-12-13 14:30:25
	 * @return UFDouble
	 */
	public UFDouble getContractweight () {
		return contractweight;
	}   
	/**
	 * 属性contractweight的Setter方法.
	 * 创建日期:2010-12-13 14:30:25
	 * @param newContractweight UFDouble
	 */
	public void setContractweight (UFDouble newContractweight ) {
	 	this.contractweight = newContractweight;
	} 	  
	/**
	 * 属性vdef16的Getter方法.
	 * 创建日期:2010-12-13 14:30:25
	 * @return String
	 */
	public String getVdef16 () {
		return vdef16;
	}   
	/**
	 * 属性vdef16的Setter方法.
	 * 创建日期:2010-12-13 14:30:25
	 * @param newVdef16 String
	 */
	public void setVdef16 (String newVdef16 ) {
	 	this.vdef16 = newVdef16;
	} 	  
	/**
	 * 属性vdef2的Getter方法.
	 * 创建日期:2010-12-13 14:30:25
	 * @return String
	 */
	public String getVdef2 () {
		return vdef2;
	}   
	/**
	 * 属性vdef2的Setter方法.
	 * 创建日期:2010-12-13 14:30:25
	 * @param newVdef2 String
	 */
	public void setVdef2 (String newVdef2 ) {
	 	this.vdef2 = newVdef2;
	} 	  
	/**
	 * 属性vdef5的Getter方法.
	 * 创建日期:2010-12-13 14:30:25
	 * @return String
	 */
	public String getVdef5 () {
		return vdef5;
	}   
	/**
	 * 属性vdef5的Setter方法.
	 * 创建日期:2010-12-13 14:30:25
	 * @param newVdef5 String
	 */
	public void setVdef5 (String newVdef5 ) {
	 	this.vdef5 = newVdef5;
	} 	  
	/**
	 * 属性amount的Getter方法.
	 * 创建日期:2010-12-13 14:30:25
	 * @return UFDouble
	 */
	public UFDouble getAmount () {
		return amount;
	}   
	/**
	 * 属性amount的Setter方法.
	 * 创建日期:2010-12-13 14:30:25
	 * @param newAmount UFDouble
	 */
	public void setAmount (UFDouble newAmount ) {
	 	this.amount = newAmount;
	} 	  
	/**
	 * 属性vdef19的Getter方法.
	 * 创建日期:2010-12-13 14:30:25
	 * @return String
	 */
	public String getVdef19 () {
		return vdef19;
	}   
	/**
	 * 属性vdef19的Setter方法.
	 * 创建日期:2010-12-13 14:30:25
	 * @param newVdef19 String
	 */
	public void setVdef19 (String newVdef19 ) {
	 	this.vdef19 = newVdef19;
	} 	  
	/**
	 * 属性pk_invbasdoc的Getter方法.
	 * 创建日期:2010-12-13 14:30:25
	 * @return String
	 */
	public String getPk_invbasdoc () {
		return pk_invbasdoc;
	}   
	/**
	 * 属性pk_invbasdoc的Setter方法.
	 * 创建日期:2010-12-13 14:30:25
	 * @param newPk_invbasdoc String
	 */
	public void setPk_invbasdoc (String newPk_invbasdoc ) {
	 	this.pk_invbasdoc = newPk_invbasdoc;
	} 	  
	/**
	 * 属性contractwidth的Getter方法.
	 * 创建日期:2010-12-13 14:30:25
	 * @return String
	 */
	public String getContractwidth () {
		return contractwidth;
	}   
	/**
	 * 属性contractwidth的Setter方法.
	 * 创建日期:2010-12-13 14:30:25
	 * @param newContractwidth String
	 */
	public void setContractwidth (String newContractwidth ) {
	 	this.contractwidth = newContractwidth;
	} 	  
	/**
	 * 属性createtime的Getter方法.
	 * 创建日期:2010-12-13 14:30:25
	 * @return UFDateTime
	 */
	public UFDateTime getCreatetime () {
		return createtime;
	}   
	/**
	 * 属性createtime的Setter方法.
	 * 创建日期:2010-12-13 14:30:25
	 * @param newCreatetime UFDateTime
	 */
	public void setCreatetime (UFDateTime newCreatetime ) {
	 	this.createtime = newCreatetime;
	} 	  
	/**
	 * 属性booleandef2的Getter方法.
	 * 创建日期:2010-12-13 14:30:25
	 * @return UFBoolean
	 */
	public UFBoolean getBooleandef2 () {
		return booleandef2;
	}   
	/**
	 * 属性booleandef2的Setter方法.
	 * 创建日期:2010-12-13 14:30:25
	 * @param newBooleandef2 UFBoolean
	 */
	public void setBooleandef2 (UFBoolean newBooleandef2 ) {
	 	this.booleandef2 = newBooleandef2;
	} 	  
	/**
	 * 属性vdef18的Getter方法.
	 * 创建日期:2010-12-13 14:30:25
	 * @return String
	 */
	public String getVdef18 () {
		return vdef18;
	}   
	/**
	 * 属性vdef18的Setter方法.
	 * 创建日期:2010-12-13 14:30:25
	 * @param newVdef18 String
	 */
	public void setVdef18 (String newVdef18 ) {
	 	this.vdef18 = newVdef18;
	} 	  
	/**
	 * 属性vdef4的Getter方法.
	 * 创建日期:2010-12-13 14:30:25
	 * @return String
	 */
	public String getVdef4 () {
		return vdef4;
	}   
	/**
	 * 属性vdef4的Setter方法.
	 * 创建日期:2010-12-13 14:30:25
	 * @param newVdef4 String
	 */
	public void setVdef4 (String newVdef4 ) {
	 	this.vdef4 = newVdef4;
	} 	  
	/**
	 * 属性arrivemeter的Getter方法.
	 * 创建日期:2010-12-13 14:30:25
	 * @return String
	 */
	public String getArrivemeter () {
		return arrivemeter;
	}   
	/**
	 * 属性arrivemeter的Setter方法.
	 * 创建日期:2010-12-13 14:30:25
	 * @param newArrivemeter String
	 */
	public void setArrivemeter (String newArrivemeter ) {
	 	this.arrivemeter = newArrivemeter;
	} 	  
	/**
	 * 属性warehousenumber的Getter方法.
	 * 创建日期:2010-12-13 14:30:25
	 * @return UFDouble
	 */
	public UFDouble getWarehousenumber () {
		return warehousenumber;
	}   
	/**
	 * 属性warehousenumber的Setter方法.
	 * 创建日期:2010-12-13 14:30:25
	 * @param newWarehousenumber UFDouble
	 */
	public void setWarehousenumber (UFDouble newWarehousenumber ) {
	 	this.warehousenumber = newWarehousenumber;
	} 	  
	/**
	 * 属性booleandef3的Getter方法.
	 * 创建日期:2010-12-13 14:30:25
	 * @return UFBoolean
	 */
	public UFBoolean getBooleandef3 () {
		return booleandef3;
	}   
	/**
	 * 属性booleandef3的Setter方法.
	 * 创建日期:2010-12-13 14:30:25
	 * @param newBooleandef3 UFBoolean
	 */
	public void setBooleandef3 (UFBoolean newBooleandef3 ) {
	 	this.booleandef3 = newBooleandef3;
	} 	  
	/**
	 * 属性vdef17的Getter方法.
	 * 创建日期:2010-12-13 14:30:25
	 * @return String
	 */
	public String getVdef17 () {
		return vdef17;
	}   
	/**
	 * 属性vdef17的Setter方法.
	 * 创建日期:2010-12-13 14:30:25
	 * @param newVdef17 String
	 */
	public void setVdef17 (String newVdef17 ) {
	 	this.vdef17 = newVdef17;
	} 	  
	/**
	 * 属性pk_invdetail的Getter方法.
	 * 创建日期:2010-12-13 14:30:25
	 * @return String
	 */
	public String getPk_invdetail () {
		return pk_invdetail;
	}   
	/**
	 * 属性pk_invdetail的Setter方法.
	 * 创建日期:2010-12-13 14:30:25
	 * @param newPk_invdetail String
	 */
	public void setPk_invdetail (String newPk_invdetail ) {
	 	this.pk_invdetail = newPk_invdetail;
	} 	  
	/**
	 * 属性vdef20的Getter方法.
	 * 创建日期:2010-12-13 14:30:25
	 * @return String
	 */
	public String getVdef20 () {
		return vdef20;
	}   
	/**
	 * 属性vdef20的Setter方法.
	 * 创建日期:2010-12-13 14:30:25
	 * @param newVdef20 String
	 */
	public void setVdef20 (String newVdef20 ) {
	 	this.vdef20 = newVdef20;
	} 	  
	/**
	 * 属性vdef1的Getter方法.
	 * 创建日期:2010-12-13 14:30:25
	 * @return String
	 */
	public String getVdef1 () {
		return vdef1;
	}   
	/**
	 * 属性vdef1的Setter方法.
	 * 创建日期:2010-12-13 14:30:25
	 * @param newVdef1 String
	 */
	public void setVdef1 (String newVdef1 ) {
	 	this.vdef1 = newVdef1;
	} 	  
	/**
	 * 属性vdef8的Getter方法.
	 * 创建日期:2010-12-13 14:30:25
	 * @return String
	 */
	public String getVdef8 () {
		return vdef8;
	}   
	/**
	 * 属性vdef8的Setter方法.
	 * 创建日期:2010-12-13 14:30:25
	 * @param newVdef8 String
	 */
	public void setVdef8 (String newVdef8 ) {
	 	this.vdef8 = newVdef8;
	} 	  
	/**
	 * 属性modifydate的Getter方法.
	 * 创建日期:2010-12-13 14:30:25
	 * @return UFDate
	 */
	public UFDate getModifydate () {
		return modifydate;
	}   
	/**
	 * 属性modifydate的Setter方法.
	 * 创建日期:2010-12-13 14:30:25
	 * @param newModifydate UFDate
	 */
	public void setModifydate (UFDate newModifydate ) {
	 	this.modifydate = newModifydate;
	} 	  
	/**
	 * 属性carriveorder_bid的Getter方法.
	 * 创建日期:2010-12-13 14:30:25
	 * @return String
	 */
	public String getCarriveorder_bid () {
		return carriveorder_bid;
	}   
	/**
	 * 属性carriveorder_bid的Setter方法.
	 * 创建日期:2010-12-13 14:30:25
	 * @param newCarriveorder_bid String
	 */
	public void setCarriveorder_bid (String newCarriveorder_bid ) {
	 	this.carriveorder_bid = newCarriveorder_bid;
	} 	  
	/**
	 * 属性booleandef1的Getter方法.
	 * 创建日期:2010-12-13 14:30:25
	 * @return UFBoolean
	 */
	public UFBoolean getBooleandef1 () {
		return booleandef1;
	}   
	/**
	 * 属性booleandef1的Setter方法.
	 * 创建日期:2010-12-13 14:30:25
	 * @param newBooleandef1 UFBoolean
	 */
	public void setBooleandef1 (UFBoolean newBooleandef1 ) {
	 	this.booleandef1 = newBooleandef1;
	} 	  
	/**
	 * 属性arrivewidth的Getter方法.
	 * 创建日期:2010-12-13 14:30:25
	 * @return String
	 */
	public String getArrivewidth () {
		return arrivewidth;
	}   
	/**
	 * 属性arrivewidth的Setter方法.
	 * 创建日期:2010-12-13 14:30:25
	 * @param newArrivewidth String
	 */
	public void setArrivewidth (String newArrivewidth ) {
	 	this.arrivewidth = newArrivewidth;
	} 	  
	/**
	 * 属性booleandef5的Getter方法.
	 * 创建日期:2010-12-13 14:30:25
	 * @return UFBoolean
	 */
	public UFBoolean getBooleandef5 () {
		return booleandef5;
	}   
	/**
	 * 属性booleandef5的Setter方法.
	 * 创建日期:2010-12-13 14:30:25
	 * @param newBooleandef5 UFBoolean
	 */
	public void setBooleandef5 (UFBoolean newBooleandef5 ) {
	 	this.booleandef5 = newBooleandef5;
	} 	  
	/**
	 * 属性corder_bid的Getter方法.
	 * 创建日期:2010-12-13 14:30:25
	 * @return String
	 */
	public String getCorder_bid () {
		return corder_bid;
	}   
	/**
	 * 属性corder_bid的Setter方法.
	 * 创建日期:2010-12-13 14:30:25
	 * @param newCorder_bid String
	 */
	public void setCorder_bid (String newCorder_bid ) {
	 	this.corder_bid = newCorder_bid;
	} 	  
	/**
	 * 属性note的Getter方法.
	 * 创建日期:2010-12-13 14:30:25
	 * @return String
	 */
	public String getNote () {
		return note;
	}   
	/**
	 * 属性note的Setter方法.
	 * 创建日期:2010-12-13 14:30:25
	 * @param newNote String
	 */
	public void setNote (String newNote ) {
	 	this.note = newNote;
	} 	  
	/**
	 * 属性price的Getter方法.
	 * 创建日期:2010-12-13 14:30:25
	 * @return UFDouble
	 */
	public UFDouble getPrice () {
		return price;
	}   
	/**
	 * 属性price的Setter方法.
	 * 创建日期:2010-12-13 14:30:25
	 * @param newPrice UFDouble
	 */
	public void setPrice (UFDouble newPrice ) {
	 	this.price = newPrice;
	} 	  
	/**
	 * 属性arriveweight的Getter方法.
	 * 创建日期:2010-12-13 14:30:25
	 * @return UFDouble
	 */
	public UFDouble getArriveweight () {
		return arriveweight;
	}   
	/**
	 * 属性arriveweight的Setter方法.
	 * 创建日期:2010-12-13 14:30:25
	 * @param newArriveweight UFDouble
	 */
	public void setArriveweight (UFDouble newArriveweight ) {
	 	this.arriveweight = newArriveweight;
	} 	  
	/**
	 * 属性arrivelength的Getter方法.
	 * 创建日期:2010-12-13 14:30:25
	 * @return String
	 */
	public String getArrivelength () {
		return arrivelength;
	}   
	/**
	 * 属性arrivelength的Setter方法.
	 * 创建日期:2010-12-13 14:30:25
	 * @param newArrivelength String
	 */
	public void setArrivelength (String newArrivelength ) {
	 	this.arrivelength = newArrivelength;
	} 	  
	/**
	 * 属性vdef13的Getter方法.
	 * 创建日期:2010-12-13 14:30:25
	 * @return String
	 */
	public String getVdef13 () {
		return vdef13;
	}   
	/**
	 * 属性vdef13的Setter方法.
	 * 创建日期:2010-12-13 14:30:25
	 * @param newVdef13 String
	 */
	public void setVdef13 (String newVdef13 ) {
	 	this.vdef13 = newVdef13;
	} 	  
	/**
	 * 属性ordernumber的Getter方法.
	 * 创建日期:2010-12-13 14:30:25
	 * @return UFDouble
	 */
	public UFDouble getOrdernumber () {
		return ordernumber;
	}   
	/**
	 * 属性ordernumber的Setter方法.
	 * 创建日期:2010-12-13 14:30:25
	 * @param newOrdernumber UFDouble
	 */
	public void setOrdernumber (UFDouble newOrdernumber ) {
	 	this.ordernumber = newOrdernumber;
	} 	  
	/**
	 * 属性conversionrates的Getter方法.
	 * 创建日期:2010-12-13 14:30:25
	 * @return UFDouble
	 */
	public UFDouble getConversionrates () {
		return conversionrates;
	}   
	/**
	 * 属性conversionrates的Setter方法.
	 * 创建日期:2010-12-13 14:30:25
	 * @param newConversionrates UFDouble
	 */
	public void setConversionrates (UFDouble newConversionrates ) {
	 	this.conversionrates = newConversionrates;
	} 	  
	/**
	 * 属性booleandef4的Getter方法.
	 * 创建日期:2010-12-13 14:30:25
	 * @return UFBoolean
	 */
	public UFBoolean getBooleandef4 () {
		return booleandef4;
	}   
	/**
	 * 属性booleandef4的Setter方法.
	 * 创建日期:2010-12-13 14:30:25
	 * @param newBooleandef4 UFBoolean
	 */
	public void setBooleandef4 (UFBoolean newBooleandef4 ) {
	 	this.booleandef4 = newBooleandef4;
	} 	  
	/**
	 * 属性contractthick的Getter方法.
	 * 创建日期:2010-12-13 14:30:25
	 * @return String
	 */
	public String getContractthick () {
		return contractthick;
	}   
	/**
	 * 属性contractthick的Setter方法.
	 * 创建日期:2010-12-13 14:30:25
	 * @param newContractthick String
	 */
	public void setContractthick (String newContractthick ) {
	 	this.contractthick = newContractthick;
	} 	  
	/**
	 * 属性sellweight的Getter方法.
	 * 创建日期:2010-12-13 14:30:25
	 * @return UFDouble
	 */
	public UFDouble getSellweight () {
		return sellweight;
	}   
	/**
	 * 属性sellweight的Setter方法.
	 * 创建日期:2010-12-13 14:30:25
	 * @param newSellweight UFDouble
	 */
	public void setSellweight (UFDouble newSellweight ) {
	 	this.sellweight = newSellweight;
	} 	  
	/**
	 * 属性vdef11的Getter方法.
	 * 创建日期:2010-12-13 14:30:25
	 * @return String
	 */
	public String getVdef11 () {
		return vdef11;
	}   
	/**
	 * 属性vdef11的Setter方法.
	 * 创建日期:2010-12-13 14:30:25
	 * @param newVdef11 String
	 */
	public void setVdef11 (String newVdef11 ) {
	 	this.vdef11 = newVdef11;
	} 	  
	/**
	 * 属性contractmeter的Getter方法.
	 * 创建日期:2010-12-13 14:30:25
	 * @return String
	 */
	public String getContractmeter () {
		return contractmeter;
	}   
	/**
	 * 属性contractmeter的Setter方法.
	 * 创建日期:2010-12-13 14:30:25
	 * @param newContractmeter String
	 */
	public void setContractmeter (String newContractmeter ) {
	 	this.contractmeter = newContractmeter;
	} 	  
	/**
	 * 属性vdef12的Getter方法.
	 * 创建日期:2010-12-13 14:30:25
	 * @return String
	 */
	public String getVdef12 () {
		return vdef12;
	}   
	/**
	 * 属性vdef12的Setter方法.
	 * 创建日期:2010-12-13 14:30:25
	 * @param newVdef12 String
	 */
	public void setVdef12 (String newVdef12 ) {
	 	this.vdef12 = newVdef12;
	} 	  
	/**
	 * 属性vdef3的Getter方法.
	 * 创建日期:2010-12-13 14:30:25
	 * @return String
	 */
	public String getVdef3 () {
		return vdef3;
	}   
	/**
	 * 属性vdef3的Setter方法.
	 * 创建日期:2010-12-13 14:30:25
	 * @param newVdef3 String
	 */
	public void setVdef3 (String newVdef3 ) {
	 	this.vdef3 = newVdef3;
	} 	  
	/**
	 * 属性vdef6的Getter方法.
	 * 创建日期:2010-12-13 14:30:25
	 * @return String
	 */
	public String getVdef6 () {
		return vdef6;
	}   
	/**
	 * 属性vdef6的Setter方法.
	 * 创建日期:2010-12-13 14:30:25
	 * @param newVdef6 String
	 */
	public void setVdef6 (String newVdef6 ) {
	 	this.vdef6 = newVdef6;
	} 	  
	/**
	 * 属性createdate的Getter方法.
	 * 创建日期:2010-12-13 14:30:25
	 * @return UFDate
	 */
	public UFDate getCreatedate () {
		return createdate;
	}   
	/**
	 * 属性createdate的Setter方法.
	 * 创建日期:2010-12-13 14:30:25
	 * @param newCreatedate UFDate
	 */
	public void setCreatedate (UFDate newCreatedate ) {
	 	this.createdate = newCreatedate;
	} 	  
	/**
	 * 属性dr的Getter方法.
	 * 创建日期:2010-12-13 14:30:25
	 * @return UFDouble
	 */
	public Integer getDr () {
		return dr;
	}   
	/**
	 * 属性dr的Setter方法.
	 * 创建日期:2010-12-13 14:30:25
	 * @param newDr UFDouble
	 */
	public void setDr (Integer newDr ) {
	 	this.dr = newDr;
	} 	  
	/**
	 * 属性createoperator的Getter方法.
	 * 创建日期:2010-12-13 14:30:25
	 * @return String
	 */
	public String getCreateoperator () {
		return createoperator;
	}   
	/**
	 * 属性createoperator的Setter方法.
	 * 创建日期:2010-12-13 14:30:25
	 * @param newCreateoperator String
	 */
	public void setCreateoperator (String newCreateoperator ) {
	 	this.createoperator = newCreateoperator;
	} 	  
	/**
	 * 属性pk_invmandoc的Getter方法.
	 * 创建日期:2010-12-13 14:30:25
	 * @return String
	 */
	public String getPk_invmandoc () {
		return pk_invmandoc;
	}   
	/**
	 * 属性pk_invmandoc的Setter方法.
	 * 创建日期:2010-12-13 14:30:25
	 * @param newPk_invmandoc String
	 */
	public void setPk_invmandoc (String newPk_invmandoc ) {
	 	this.pk_invmandoc = newPk_invmandoc;
	} 	  
 
	/**
	  * <p>取得父VO主键字段.
	  * <p>
	  * 创建日期:2010-12-13 14:30:25
	  * @return java.lang.String
	  */
	public java.lang.String getParentPKFieldName() {
	    return null;
	}   
    
	/**
	  * <p>取得表主键.
	  * <p>
	  * 创建日期:2010-12-13 14:30:25
	  * @return java.lang.String
	  */
	public java.lang.String getPKFieldName() {
	  return "pk_invdetail";
	}
    
	/**
	 * <p>返回表名称.
	 * <p>
	 * 创建日期:2010-12-13 14:30:25
	 * @return java.lang.String
	 */
	public java.lang.String getTableName() {
		return "scm_invdetail";
	}    
    
    /**
	  * 按照默认方式创建构造子.
	  *
	  * 创建日期:2010-12-13 14:30:25
	  */
     public InvDetailVO() {
		super();	
	}    
} 
