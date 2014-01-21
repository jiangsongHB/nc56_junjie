package nc.ui.ja.entityreceiptck;

import nc.ui.ja.buttons.ICustomizeButton;
import nc.ui.trade.bill.ICardController;
import nc.ui.trade.businessaction.IBusinessActionType;

import nc.vo.ja.entityreceiptck.MyBillVO;
import nc.vo.ja.entityreceiptck.JaEntityReceiptCkVO;
import nc.ui.trade.button.IBillButton;
import nc.ui.trade.bill.ISingleController;

/**
 * <b> UI��������</b><br>
 *
 * <p>
 *     ���ý��水ť�����ݣ��Ƿ�ƽ̨��ص���Ϣ
 * </p>
 * <br>
 *
 *
 * @author authorName
 * @version tempProject version
 */

public class ClientUICtrl implements ICardController,ISingleController {

	public String[] getCardBodyHideCol() {
		return null;
	}

	/**
	 * ���ý��水ť
	 */
	public int[] getCardButtonAry() {
	        return new int[]{
			IBillButton.Query,
			IBillButton.Add,
			IBillButton.Edit,
			IBillButton.Line,
			IBillButton.Save,
			IBillButton.Cancel,
			//IBillButton.Delete,
			IBillButton.SelAll,
			IBillButton.SelNone,
			ICustomizeButton.DefAllot,
			ICustomizeButton.DefCheck,
			ICustomizeButton.DefQuery,
			ICustomizeButton.DefReturn
	        };
	}

	public boolean isShowCardRowNo() {
		return false;
	}

	public boolean isShowCardTotal() {
		return false;
	}

	public String getBillType() {
		return "JAH103";
	}

	public String[] getBillVoName() {
		return new String[]{
			MyBillVO.class.getName(),
			JaEntityReceiptCkVO.class.getName(),
			JaEntityReceiptCkVO.class.getName()
		};
	}

	public String getBodyCondition() {
		return null;
	}

	public String getBodyZYXKey() {
		return null;
	}
	
	/**
	 * �Ƿ�ƽ̨�޹�
	 */
	public int getBusinessActionType() {
		return IBusinessActionType.BD;
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
		return false;
	}

	public boolean isLoadCardFormula() {
		return false;
	}

	/**
	 * �Ƿ񵥱�
	 * @return boolean true:�����壬false:����ͷ
	 */ 
	public boolean isSingleDetail() {
		return true; //����ͷ
	}
	
}
