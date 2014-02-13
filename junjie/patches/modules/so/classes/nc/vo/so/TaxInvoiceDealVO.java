package nc.vo.so;

import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFDouble;

public class TaxInvoiceDealVO extends SuperVO {

	private java.lang.String csaleid;
	private java.lang.String vreceiptcode;
	private nc.vo.pub.lang.UFDate dbilldate;
	private java.lang.String pk_corp;
	private String cinvoice_bid;
	private UFDouble nnumber;
	private UFDouble nmny;
	private String cinvbasdocid;
	private UFDouble ntotaldealmny;
	private UFDouble ntotaldealnum;
	private String crowno;
	private UFDouble ndealnum;
	private UFDouble ndealmny;
	private UFDouble nrewritenum;
	private UFDouble nrewritemny;
	private String ctaxinvoiceid;
	private String ctaxinvoice_bid;
	

	public TaxInvoiceDealVO() {
		// TODO Auto-generated constructor stub
		super();
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

	public java.lang.String getCsaleid() {
		return csaleid;
	}

	public void setCsaleid(java.lang.String csaleid) {
		this.csaleid = csaleid;
	}

	public nc.vo.pub.lang.UFDate getDbilldate() {
		return dbilldate;
	}

	public void setDbilldate(nc.vo.pub.lang.UFDate dbilldate) {
		this.dbilldate = dbilldate;
	}

	public java.lang.String getPk_corp() {
		return pk_corp;
	}

	public void setPk_corp(java.lang.String pk_corp) {
		this.pk_corp = pk_corp;
	}

	public java.lang.String getVreceiptcode() {
		return vreceiptcode;
	}

	public void setVreceiptcode(java.lang.String vreceiptcode) {
		this.vreceiptcode = vreceiptcode;
	}

	public String getCinvoice_bid() {
		return cinvoice_bid;
	}

	public void setCinvoice_bid(String cinvoice_bid) {
		this.cinvoice_bid = cinvoice_bid;
	}

	public String getCinvbasdocid() {
		return cinvbasdocid;
	}

	public void setCinvbasdocid(String cinvbasdocid) {
		this.cinvbasdocid = cinvbasdocid;
	}

	public UFDouble getNmny() {
		return nmny;
	}

	public void setNmny(UFDouble nmny) {
		this.nmny = nmny;
	}

	public UFDouble getNnumber() {
		return nnumber;
	}

	public void setNnumber(UFDouble nnumber) {
		this.nnumber = nnumber;
	}

	public UFDouble getNtotaldealmny() {
		return ntotaldealmny;
	}

	public void setNtotaldealmny(UFDouble ntotaldealmny) {
		this.ntotaldealmny = ntotaldealmny;
	}

	public UFDouble getNtotaldealnum() {
		return ntotaldealnum;
	}

	public void setNtotaldealnum(UFDouble ntotaldealnum) {
		this.ntotaldealnum = ntotaldealnum;
	}

	public String getCrowno() {
		return crowno;
	}

	public void setCrowno(String crowno) {
		this.crowno = crowno;
	}

	public UFDouble getNdealmny() {
		return ndealmny;
	}

	public void setNdealmny(UFDouble ndealmny) {
		this.ndealmny = ndealmny;
	}

	public UFDouble getNdealnum() {
		return ndealnum;
	}

	public void setNdealnum(UFDouble ndealnum) {
		this.ndealnum = ndealnum;
	}

	public UFDouble getNrewritemny() {
		return nrewritemny;
	}

	public void setNrewritemny(UFDouble nrewritemny) {
		this.nrewritemny = nrewritemny;
	}

	public UFDouble getNrewritenum() {
		return nrewritenum;
	}

	public void setNrewritenum(UFDouble nrewritenum) {
		this.nrewritenum = nrewritenum;
	}

	public String getCtaxinvoice_bid() {
		return ctaxinvoice_bid;
	}

	public void setCtaxinvoice_bid(String ctaxinvoice_bid) {
		this.ctaxinvoice_bid = ctaxinvoice_bid;
	}

	public String getCtaxinvoiceid() {
		return ctaxinvoiceid;
	}

	public void setCtaxinvoiceid(String ctaxinvoiceid) {
		this.ctaxinvoiceid = ctaxinvoiceid;
	}

}
