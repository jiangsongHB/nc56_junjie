package nc.impl.so.taxinvoice;

import java.sql.SQLException;
import java.util.ArrayList;

import javax.naming.NamingException;

import nc.itf.so.taxinvoice.ITaxInvoice;
import nc.vo.pub.BusinessException;
import nc.vo.so.TaxInvoiceBbVO;
import nc.vo.so.TaxInvoiceDealVO;

public class TaxInvoiceImp implements ITaxInvoice {
	
	public void saveDeal(TaxInvoiceBbVO[] dealvo) throws  BusinessException, SQLException {
		// TODO Auto-generated method stub
		try {
			TaxInvoiceDMO dmo = new TaxInvoiceDMO();
			dmo.saveDeal(dealvo);
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}

	}

	public void deleteDeal(TaxInvoiceBbVO[] dealvo) throws  BusinessException, SQLException {
		// TODO Auto-generated method stub
		try {
			TaxInvoiceDMO dmo = new TaxInvoiceDMO();
			dmo.deleteDeal(dealvo);
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
