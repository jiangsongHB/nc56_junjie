package nc.vo.ic.sd;

import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;

/**
 * 码单锁定VO
 * 
 * @author heyq
 * 
 */

public class MdsdVO extends SuperVO {

	private String pk_mdsd;// 主键

	private String pk_corp;// 公司

	private String pk_mdxcl_b;// 码单现存量表体PK

	private String xsddbt_pk;// 销售订单表体PK

	private UFDate sdrq;// 锁定日期

	private UFDate sxrq; // 失效日期

	private UFDouble sdzs; // 锁定支数

	private String sfsx;// 是否生效

	private UFDate dmakedate;// 制单日期

	private String voperatorid;// 制单人

	private Integer dr;// dr

	private UFDouble def1;

	private UFDouble def2;

	private UFDouble def3;

	private UFBoolean def4;

	private UFBoolean def5;

	private String def6;

	private String def7;

	private String def8;

	private String def9;

	private String def10;

	private String def11;

	private String def12;

	private String def13;

	private String def14;

	private String def15;

	public UFDouble getDef1() {
		return def1;
	}

	public void setDef1(UFDouble def1) {
		this.def1 = def1;
	}

	public UFDouble getDef2() {
		return def2;
	}

	public void setDef2(UFDouble def2) {
		this.def2 = def2;
	}

	public UFDouble getDef3() {
		return def3;
	}

	public void setDef3(UFDouble def3) {
		this.def3 = def3;
	}

	public String getDef10() {
		return def10;
	}

	public void setDef10(String def10) {
		this.def10 = def10;
	}

	public String getDef11() {
		return def11;
	}

	public void setDef11(String def11) {
		this.def11 = def11;
	}

	public String getDef12() {
		return def12;
	}

	public void setDef12(String def12) {
		this.def12 = def12;
	}

	public String getDef13() {
		return def13;
	}

	public void setDef13(String def13) {
		this.def13 = def13;
	}

	public String getDef14() {
		return def14;
	}

	public void setDef14(String def14) {
		this.def14 = def14;
	}

	public String getDef15() {
		return def15;
	}

	public void setDef15(String def15) {
		this.def15 = def15;
	}

	public UFBoolean getDef4() {
		return def4;
	}

	public void setDef4(UFBoolean def4) {
		this.def4 = def4;
	}

	public UFBoolean getDef5() {
		return def5;
	}

	public void setDef5(UFBoolean def5) {
		this.def5 = def5;
	}

	public String getDef6() {
		return def6;
	}

	public void setDef6(String def6) {
		this.def6 = def6;
	}

	public String getDef7() {
		return def7;
	}

	public void setDef7(String def7) {
		this.def7 = def7;
	}

	public String getDef8() {
		return def8;
	}

	public void setDef8(String def8) {
		this.def8 = def8;
	}

	public String getDef9() {
		return def9;
	}

	public void setDef9(String def9) {
		this.def9 = def9;
	}

	public UFDate getDmakedate() {
		return dmakedate;
	}

	public void setDmakedate(UFDate dmakedate) {
		this.dmakedate = dmakedate;
	}

	public Integer getDr() {
		return dr;
	}

	public void setDr(Integer dr) {
		this.dr = dr;
	}

	public String getPk_corp() {
		return pk_corp;
	}

	public void setPk_corp(String pk_corp) {
		this.pk_corp = pk_corp;
	}

	public String getPk_mdsd() {
		return pk_mdsd;
	}

	public void setPk_mdsd(String pk_mdsd) {
		this.pk_mdsd = pk_mdsd;
	}

	public String getPk_mdxcl_b() {
		return pk_mdxcl_b;
	}

	public void setPk_mdxcl_b(String pk_mdxcl_b) {
		this.pk_mdxcl_b = pk_mdxcl_b;
	}

	public UFDate getSdrq() {
		return sdrq;
	}

	public void setSdrq(UFDate sdrq) {
		this.sdrq = sdrq;
	}

	public UFDouble getSdzs() {
		return sdzs;
	}

	public void setSdzs(UFDouble sdzs) {
		this.sdzs = sdzs;
	}

	public String getSfsx() {
		return sfsx;
	}

	public void setSfsx(String sfsx) {
		this.sfsx = sfsx;
	}

	public UFDate getSxrq() {
		return sxrq;
	}

	public void setSxrq(UFDate sxrq) {
		this.sxrq = sxrq;
	}

	public String getVoperatorid() {
		return voperatorid;
	}

	public void setVoperatorid(String voperatorid) {
		this.voperatorid = voperatorid;
	}

	public String getXsddbt_pk() {
		return xsddbt_pk;
	}

	public void setXsddbt_pk(String xsddbt_pk) {
		this.xsddbt_pk = xsddbt_pk;
	}

	/**
	 * 取得表主键.
	 * 
	 * @return String
	 */
	public String getPKFieldName() {

		return "pk_mdsd";

	}

	/**
	 * 返回表名称.
	 * 
	 * @return java.lang.String tableName
	 */
	public String getTableName() {
		return "nc_mdsd";
	}

	/**
	 * 返回数值对象的显示名称.
	 */
	public String getEntityName() {
		return "nc_mdsd";
	}

	public String getPrimaryKey() {
		return pk_mdsd;
	}

	public void setPrimaryKey(String pk_mdsd) {
		this.pk_mdsd = pk_mdsd;
	}

	@Override
	public String getParentPKFieldName() {
		// TODO Auto-generated method stub
		return null;
	}

}
