package nc.vo.ic.xcl;

import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;

/**
 * 码单现存量表头VO
 * 
 * @author heyq
 * 
 */

public class MdxclVO extends SuperVO {

	private String pk_mdxcl;// 主键

	private String pk_corp;// 公司主建

	private String ccalbodyidb;// 库存组织

	private String cwarehouseidb;// 仓库PK

	private String cinvbasid;// 存货基本档案

	private String cinventoryidb;// 存货管理档案

	private UFDouble sum_zhishu; // 结存支数

	private UFDouble sum_zhongliang;// 结存重量

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

	public String getCcalbodyidb() {
		return ccalbodyidb;
	}

	public void setCcalbodyidb(String ccalbodyidb) {
		this.ccalbodyidb = ccalbodyidb;
	}

	public String getCinvbasid() {
		return cinvbasid;
	}

	public void setCinvbasid(String cinvbasid) {
		this.cinvbasid = cinvbasid;
	}

	public String getCinventoryidb() {
		return cinventoryidb;
	}

	public void setCinventoryidb(String cinventoryidb) {
		this.cinventoryidb = cinventoryidb;
	}

	public String getCwarehouseidb() {
		return cwarehouseidb;
	}

	public void setCwarehouseidb(String cwarehouseidb) {
		this.cwarehouseidb = cwarehouseidb;
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

	public String getPk_mdxcl() {
		return pk_mdxcl;
	}

	public void setPk_mdxcl(String pk_mdxcl) {
		this.pk_mdxcl = pk_mdxcl;
	}

	public UFDouble getSum_zhishu() {
		return sum_zhishu;
	}

	public void setSum_zhishu(UFDouble sum_zhishu) {
		this.sum_zhishu = sum_zhishu;
	}

	public UFDouble getSum_zhongliang() {
		return sum_zhongliang;
	}

	public void setSum_zhongliang(UFDouble sum_zhongliang) {
		this.sum_zhongliang = sum_zhongliang;
	}

	public String getVoperatorid() {
		return voperatorid;
	}

	public void setVoperatorid(String voperatorid) {
		this.voperatorid = voperatorid;
	}

	/**
	 * 取得表主键.
	 * 
	 * @return String
	 */
	public String getPKFieldName() {

		return "pk_mdxcl";

	}

	/**
	 * 返回表名称.
	 * 
	 * @return java.lang.String tableName
	 */
	public String getTableName() {
		return "nc_mdxcl";
	}

	/**
	 * 返回数值对象的显示名称.
	 */
	public String getEntityName() {
		return "nc_mdxcl";
	}

	public String getPrimaryKey() {
		return pk_mdxcl;
	}

	public void setPrimaryKey(String pk_mdxcl) {
		this.pk_mdxcl = pk_mdxcl;
	}

	@Override
	public String getParentPKFieldName() {
		// TODO Auto-generated method stub
		return null;
	}

}
