package nc.vo.ic.md;

import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;

public class CargInfVO extends SuperVO 
{
	private static final long serialVersionUID = 1L;
	/** ����ⵥ����PK */
	private String cgeneralbid;
	/** ����ⵥ��λ���PK */
	private String cgeneralbb1;
	
	private String cspaceid;
	
	private Integer dr;
	
	private UFDouble ningrossnum;
	
	private UFDouble ninspaceassistnum;
	
	private UFDouble ninspacenum;
	
	private UFDouble noutgrossnum;
	
	private UFDouble noutspaceassistnum;
	
	private UFDouble noutspacenum;
	
	private String pk_corp;
	
	private UFDateTime ts;
	

	public CargInfVO() {
		super();
	}
	
	@Override
	public String getPKFieldName() {
		// TODO Auto-generated method stub
		return "cgeneralbb1";
	}

	@Override
	public String getParentPKFieldName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getTableName() {
		// TODO Auto-generated method stub
		return "ic_general_bb1";
	}

	

	public String getCgeneralbb1() {
		return cgeneralbb1;
	}

	public void setCgeneralbb1(String cgeneralbb1) {
		this.cgeneralbb1 = cgeneralbb1;
	}

	public String getCgeneralbid() {
		return cgeneralbid;
	}

	public void setCgeneralbid(String cgeneralbid) {
		this.cgeneralbid = cgeneralbid;
	}

	public String getCspaceid() {
		return cspaceid;
	}

	public void setCspaceid(String cspaceid) {
		this.cspaceid = cspaceid;
	}

	public Integer getDr() {
		return dr;
	}

	public void setDr(Integer dr) {
		this.dr = dr;
	}

	public UFDouble getNingrossnum() {
		return ningrossnum;
	}

	public void setNingrossnum(UFDouble ningrossnum) {
		this.ningrossnum = ningrossnum;
	}

	public UFDouble getNinspaceassistnum() {
		return ninspaceassistnum;
	}

	public void setNinspaceassistnum(UFDouble ninspaceassistnum) {
		this.ninspaceassistnum = ninspaceassistnum;
	}

	public UFDouble getNinspacenum() {
		return ninspacenum;
	}

	public void setNinspacenum(UFDouble ninspacenum) {
		this.ninspacenum = ninspacenum;
	}

	public UFDouble getNoutgrossnum() {
		return noutgrossnum;
	}

	public void setNoutgrossnum(UFDouble noutgrossnum) {
		this.noutgrossnum = noutgrossnum;
	}

	public UFDouble getNoutspaceassistnum() {
		return noutspaceassistnum;
	}

	public void setNoutspaceassistnum(UFDouble noutspaceassistnum) {
		this.noutspaceassistnum = noutspaceassistnum;
	}

	public UFDouble getNoutspacenum() {
		return noutspacenum;
	}

	public void setNoutspacenum(UFDouble noutspacenum) {
		this.noutspacenum = noutspacenum;
	}

	public String getPk_corp() {
		return pk_corp;
	}

	public void setPk_corp(String pk_corp) {
		this.pk_corp = pk_corp;
	}

	public UFDateTime getTs() {
		return ts;
	}

	public void setTs(UFDateTime ts) {
		this.ts = ts;
	}

}
