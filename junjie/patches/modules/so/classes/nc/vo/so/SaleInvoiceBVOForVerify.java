package nc.vo.so;

import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFDouble;

public class SaleInvoiceBVOForVerify extends SuperVO {
	
	private UFDouble ntotaldealmny;
	private UFDouble ntotaldealnum;
	private String cinvoice_bid;
	private String csaleid;
	

	@Override
	public String getPKFieldName() {
		// TODO Auto-generated method stub
		return "cinvoice_bid";
	}

	@Override
	public String getParentPKFieldName() {
		// TODO Auto-generated method stub
		return "csaleid";
	}

	@Override
	public String getTableName() {
		// TODO Auto-generated method stub
		return "so_saleinvoice_b";
	}

	public SaleInvoiceBVOForVerify(){
		super();
	}

	public String getCinvoice_bid() {
		return cinvoice_bid;
	}

	public void setCinvoice_bid(String cinvoice_bid) {
		this.cinvoice_bid = cinvoice_bid;
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

	public String getCsaleid() {
		return csaleid;
	}

	public void setCsaleid(String csaleid) {
		this.csaleid = csaleid;
	}
}
