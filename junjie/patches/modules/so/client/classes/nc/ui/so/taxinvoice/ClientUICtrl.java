package nc.ui.so.taxinvoice;

import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.businessaction.IBusinessActionType;
import nc.ui.trade.bill.ISingleController;

import nc.vo.so.TaxInvoiceVO;
import nc.vo.so.TaxInvoiceHeaderVO;
import nc.vo.so.TaxInvoiceItemVO;
import nc.ui.trade.button.IBillButton;


/**
 * <b> 在此处简要描述此类的功能 </b>
 *
 * <p>
 *     在此处添加此类的描述信息
 * </p>
 *
 * Create on 2006-4-6 16:00:51
 *
 * @author author
 * @version tempProject version
 */

public class ClientUICtrl extends AbstractManageController {

	public String[] getCardBodyHideCol() {
		return null;
	}

	public int[] getCardButtonAry() {
	
        return new int[]{
			nc.ui.trade.button.IBillButton.Busitype,
			nc.ui.trade.button.IBillButton.Add,
			nc.ui.trade.button.IBillButton.Edit,
			nc.ui.trade.button.IBillButton.Del,
			nc.ui.trade.button.IBillButton.Line,
			nc.ui.trade.button.IBillButton.Save,
			nc.ui.trade.button.IBillButton.Cancel,
			nc.ui.trade.button.IBillButton.Commit,
			nc.ui.trade.button.IBillButton.Audit,
			nc.ui.trade.button.IBillButton.CancelAudit,
			nc.ui.trade.button.IBillButton.Refresh,
			nc.ui.trade.button.IBillButton.Print,
			nc.ui.trade.button.IBillButton.Return,
			nc.ui.trade.button.IBillButton.ApproveInfo,
			nc.ui.trade.button.IBillButton.Brow,
			nc.ui.trade.button.IBillButton.File,
			nc.ui.so.taxinvoice.command.btDealGpBoCommand.BUTTON_NO
             };
  
	}
	
	public int[] getListButtonAry() {		
	
        return new int[]{
			nc.ui.trade.button.IBillButton.Busitype,
			nc.ui.trade.button.IBillButton.Add,
			nc.ui.trade.button.IBillButton.Edit,
			nc.ui.trade.button.IBillButton.Del,
			nc.ui.trade.button.IBillButton.Query,
			nc.ui.trade.button.IBillButton.Audit,
			nc.ui.trade.button.IBillButton.Commit,
			nc.ui.trade.button.IBillButton.CancelAudit,
			nc.ui.trade.button.IBillButton.Refresh,
			nc.ui.trade.button.IBillButton.Print,
			nc.ui.trade.button.IBillButton.Card,
			nc.ui.trade.button.IBillButton.ApproveInfo,
			nc.ui.trade.button.IBillButton.File,
			nc.ui.so.taxinvoice.command.btDealGpBoCommand.BUTTON_NO
        
        };
	
	}

	public boolean isShowCardRowNo() {
		return true;
	}

	public boolean isShowCardTotal() {
		return false;
	}

	public String getBillType() {
		return "32H";
	}

	public String[] getBillVoName() {
		return new String[]{
			TaxInvoiceVO.class.getName(),
			TaxInvoiceHeaderVO.class.getName(),
			TaxInvoiceItemVO.class.getName()
		};
	}

	public String getBodyCondition() {
		return null;
	}

	public String getBodyZYXKey() {
		return null;
	}

	public int getBusinessActionType() {
		return IBusinessActionType.PLATFORM;
	}

	public String getChildPkField() {
		return null;
	}

	public String getHeadZYXKey() {
		return null;
	}

	public String getPkField() {
		return null;
	}

	public Boolean isEditInGoing() throws Exception {
		return null;
	}

	public boolean isExistBillStatus() {
		return true;
	}

	public boolean isLoadCardFormula() {		
		return false;
	}

	public String[] getListBodyHideCol() {	
		return null;
	}

	public String[] getListHeadHideCol() {		
		return null;
	}

	public boolean isShowListRowNo() {		
		return true;
	}

	public boolean isShowListTotal() {
		return true;
	}
	
}
