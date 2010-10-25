package nc.ui.ic.ic251;

import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDouble;

/**
 * 码单现存量表体VO
 * 
 * @author heyq
 * 
 */
public class HwtzVO extends SuperVO {

	private String pk_mdxcl_b; // 主键

	private String pk_mdxcl;// 主表主建
	
	private UFBoolean isselect;// 是否选择

	private String invcode;// 存货编码

	private String invname;// 存货名称

	private String pk_cspaceid;//货位PK
	
	/** 货位PK */
	private String cspaceid;

	/** 件编号 */
	private String jbh;

	/** 宽度 */
	private UFDouble md_width;

	/** 长度 */
	private UFDouble md_length;

	/** 米数 */
	private UFDouble md_meter;

	/** 实测厚*宽*长 */
	private String md_note;

	/** 炉批号 */
	private String md_lph;

	/** 资源号 */
	private String md_zyh;

	/** 质保证书号 */
	private String md_zlzsh;

	/** 备注 */
	private String remark;

	private UFDouble zhishu;// 支数

	private UFDouble zhongliang;// 重量

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

	public String getCspaceid() {
		return cspaceid;
	}

	public void setCspaceid(String cspaceid) {
		this.cspaceid = cspaceid;
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

	public Integer getDr() {
		return dr;
	}

	public void setDr(Integer dr) {
		this.dr = dr;
	}

	public String getJbh() {
		return jbh;
	}

	public void setJbh(String jbh) {
		this.jbh = jbh;
	}

	public UFDouble getMd_length() {
		return md_length;
	}

	public void setMd_length(UFDouble md_length) {
		this.md_length = md_length;
	}

	public String getMd_lph() {
		return md_lph;
	}

	public void setMd_lph(String md_lph) {
		this.md_lph = md_lph;
	}

	public UFDouble getMd_meter() {
		return md_meter;
	}

	public void setMd_meter(UFDouble md_meter) {
		this.md_meter = md_meter;
	}

	public String getMd_note() {
		return md_note;
	}

	public void setMd_note(String md_note) {
		this.md_note = md_note;
	}

	public UFDouble getMd_width() {
		return md_width;
	}

	public void setMd_width(UFDouble md_width) {
		this.md_width = md_width;
	}

	public String getMd_zlzsh() {
		return md_zlzsh;
	}

	public void setMd_zlzsh(String md_zlzsh) {
		this.md_zlzsh = md_zlzsh;
	}

	public String getMd_zyh() {
		return md_zyh;
	}

	public void setMd_zyh(String md_zyh) {
		this.md_zyh = md_zyh;
	}

	public String getPk_mdxcl() {
		return pk_mdxcl;
	}

	public void setPk_mdxcl(String pk_mdxcl) {
		this.pk_mdxcl = pk_mdxcl;
	}

	public String getPk_mdxcl_b() {
		return pk_mdxcl_b;
	}

	public void setPk_mdxcl_b(String pk_mdxcl_b) {
		this.pk_mdxcl_b = pk_mdxcl_b;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public UFDouble getZhishu() {
		return zhishu;
	}

	public void setZhishu(UFDouble zhishu) {
		this.zhishu = zhishu;
	}

	public UFDouble getZhongliang() {
		return zhongliang;
	}

	public void setZhongliang(UFDouble zhongliang) {
		this.zhongliang = zhongliang;
	}

	/**
	 * 取得表主键.
	 * 
	 * @return String
	 */
	public String getPKFieldName() {

		return "pk_mdxcl_b";

	}

	/**
	 * 返回表名称.
	 * 
	 * @return java.lang.String tableName
	 */
	public String getTableName() {
		return "nc_mdxcl_b";
	}

	/**
	 * 返回数值对象的显示名称.
	 */
	public String getEntityName() {
		return "nc_mdxcl_b";
	}

	public String getPrimaryKey() {
		return pk_mdxcl_b;
	}

	public void setPrimaryKey(String pk_mdxcl_b) {
		this.pk_mdxcl_b = pk_mdxcl_b;
	}

	@Override
	public String getParentPKFieldName() {
		// TODO Auto-generated method stub
		return null;
	}
	

	public String getInvcode() {
		return invcode;
	}

	public void setInvcode(String invcode) {
		this.invcode = invcode;
	}

	public String getInvname() {
		return invname;
	}

	public void setInvname(String invname) {
		this.invname = invname;
	}

	public UFBoolean getIsselect() {
		return isselect;
	}

	public void setIsselect(UFBoolean isselect) {
		this.isselect = isselect;
	}

	public String getPk_cspaceid() {
		return pk_cspaceid;
	}

	public void setPk_cspaceid(String pk_cspaceid) {
		this.pk_cspaceid = pk_cspaceid;
	}
	
	
}
