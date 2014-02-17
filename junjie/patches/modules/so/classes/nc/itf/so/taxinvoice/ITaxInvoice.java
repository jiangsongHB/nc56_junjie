/**
 * 
 */
package nc.itf.so.taxinvoice;

import java.sql.SQLException;
import java.util.ArrayList;

import nc.vo.pub.BusinessException;
import nc.vo.so.TaxInvoiceBbVO;
import nc.vo.so.TaxInvoiceDealVO;

/**
 * @author ausu
 *
 */
public interface ITaxInvoice {
	public  void saveDeal(TaxInvoiceBbVO[] dealvo) throws BusinessException,SQLException;
	public  void deleteDeal(TaxInvoiceBbVO[] dealvo) throws BusinessException, SQLException;
}
