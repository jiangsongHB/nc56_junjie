package nc.ui.ic.mdck;

import nc.vo.bd.CorpVO;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.sm.UserVO;

/**
 * �����ϢVO
 * 
 * @author heyq
 * 
 */

public class ChInfoVO extends SuperVO {

	private CorpVO corpVo; // ��ǰ��˾

	private UserVO userVo; // ��ǰ�û�

	private UFDate ufdate; // ��ǰ����

	private String pk_invmandoc;// ���������

	private String pk_invbasdoc;// �����������

	private String ccalbodyidb;// �����֯��

	private String cwarehouseidb;// �ֿ�PK

	private String cbodybilltypecode;// ��������

	private String cgeneralbid;// ����ⵥ����PK

	private UFDouble noutassistnum;// Ӧ��������

	private UFDouble noutnum;// Ӧ������

	private String lydjh;// ��Դ����PK

	private Integer fbillflag;// ����״̬

	private UFBoolean sfbj;// �Ƿ����
	

	public UFBoolean getSfbj() {
		return sfbj;
	}

	public void setSfbj(UFBoolean sfbj) {
		this.sfbj = sfbj;
	}

	public Integer getFbillflag() {
		return fbillflag;
	}

	public void setFbillflag(Integer fbillflag) {
		this.fbillflag = fbillflag;
	}

	public String getLydjh() {
		return lydjh;
	}

	public void setLydjh(String lydjh) {
		this.lydjh = lydjh;
	}

	public UFDouble getNoutassistnum() {
		return noutassistnum;
	}

	public void setNoutassistnum(UFDouble noutassistnum) {
		this.noutassistnum = noutassistnum;
	}

	public UFDouble getNoutnum() {
		return noutnum;
	}

	public void setNoutnum(UFDouble noutnum) {
		this.noutnum = noutnum;
	}

	public String getCgeneralbid() {
		return cgeneralbid;
	}

	public void setCgeneralbid(String cgeneralbid) {
		this.cgeneralbid = cgeneralbid;
	}

	public String getCbodybilltypecode() {
		return cbodybilltypecode;
	}

	public void setCbodybilltypecode(String cbodybilltypecode) {
		this.cbodybilltypecode = cbodybilltypecode;
	}

	public String getCcalbodyidb() {
		return ccalbodyidb;
	}

	public void setCcalbodyidb(String ccalbodyidb) {
		this.ccalbodyidb = ccalbodyidb;
	}

	public CorpVO getCorpVo() {
		return corpVo;
	}

	public void setCorpVo(CorpVO corpVo) {
		this.corpVo = corpVo;
	}

	public String getCwarehouseidb() {
		return cwarehouseidb;
	}

	public void setCwarehouseidb(String cwarehouseidb) {
		this.cwarehouseidb = cwarehouseidb;
	}

	public String getPk_invbasdoc() {
		return pk_invbasdoc;
	}

	public void setPk_invbasdoc(String pk_invbasdoc) {
		this.pk_invbasdoc = pk_invbasdoc;
	}

	public String getPk_invmandoc() {
		return pk_invmandoc;
	}

	public void setPk_invmandoc(String pk_invmandoc) {
		this.pk_invmandoc = pk_invmandoc;
	}

	public UFDate getUfdate() {
		return ufdate;
	}

	public void setUfdate(UFDate ufdate) {
		this.ufdate = ufdate;
	}

	public UserVO getUserVo() {
		return userVo;
	}

	public void setUserVo(UserVO userVo) {
		this.userVo = userVo;
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
