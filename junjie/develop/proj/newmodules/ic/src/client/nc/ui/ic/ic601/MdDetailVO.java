package nc.ui.ic.ic601;

import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFDouble;

public class MdDetailVO extends SuperVO {

	private String invcode;// �������

	private String invname;// �������

	private String invspec;// ���

	private String cspaceid;// ��λ��

	private String jbh;// �����

	private UFDouble md_width;// ���

	private UFDouble md_length;// ����

	private UFDouble md_meter;// ����

	private String md_note;// ʵ���*��*��

	private UFDouble zhishu;// ֧��

	private UFDouble zhongliang;// ����

	private UFDouble yxsdzs;// ��Ч����֧��

	private UFDouble kyzs;// ����֧��

	private String md_lph;// ¯���

	private String md_zyh;// ��Դ��

	private String md_zlzsh;// ������֤���

	private String remark;// ��ע��

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
