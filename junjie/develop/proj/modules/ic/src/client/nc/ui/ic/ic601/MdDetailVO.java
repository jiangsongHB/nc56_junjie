package nc.ui.ic.ic601;

import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFDouble;

public class MdDetailVO extends SuperVO {

	private String invcode;// 存货编码

	private String invname;// 存货名称

	private String invspec;// 规格

	private String cspaceid;// 货位号

	private String jbh;// 件编号

	private UFDouble md_width;// 宽度

	private UFDouble md_length;// 长度

	private UFDouble md_meter;// 米数

	private String md_note;// 实测厚*宽*长

	private UFDouble zhishu;// 支数

	private UFDouble zhongliang;// 重量

	private UFDouble yxsdzs;// 有效锁定支数

	private UFDouble kyzs;// 可用支数

	private String md_lph;// 炉编号

	private String md_zyh;// 资源号

	private String md_zlzsh;// 质量保证书号

	private String remark;// 备注；

	public UFDouble getKyzs() {
		return kyzs;
	}

	public void setKyzs(UFDouble kyzs) {
		this.kyzs = kyzs;
	}

	public UFDouble getYxsdzs() {
		return yxsdzs;
	}

	public void setYxsdzs(UFDouble yxsdzs) {
		this.yxsdzs = yxsdzs;
	}

	public String getCspaceid() {
		return cspaceid;
	}

	public void setCspaceid(String cspaceid) {
		this.cspaceid = cspaceid;
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

	public String getInvspec() {
		return invspec;
	}

	public void setInvspec(String invspec) {
		this.invspec = invspec;
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

	@Override
	public String getPKFieldName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getParentPKFieldName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getTableName() {
		// TODO Auto-generated method stub
		return null;
	}

}
