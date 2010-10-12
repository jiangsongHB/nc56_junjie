package nc.ui.bd.bd1003;

import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.businessaction.IBusinessActionType;
import nc.ui.trade.button.IBillButton;
import nc.vo.bd.bd1003.AdditionalValueVO;

import nc.vo.trade.pub.HYBillVO;

public class ClientCtrl extends AbstractManageController {

	/**
	 * @function ʵ�ָ���Ĺ��췽��
	 */
	public ClientCtrl() {
		super();
	}

	/**
	 * @function ʵ�ָ���ӿڣ��ݲ����� 
	 */
	public String[] getCardBodyHideCol() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @function ���ÿ�Ƭ��ť
	 */
	public int[] getCardButtonAry() {
		// TODO Auto-generated method stub
		return new int[] { 
				IBillButton.Query,// ��ѯ
				IBillButton.Add, // ����
				IBillButton.Edit, // �޸�
				IBillButton.Save, // ����
				IBillButton.Delete,// ɾ��
				IBillButton.Cancel,// ȡ��
				IBillButton.Return, // ����
				IBillButton.Refresh,// ˢ��
				IBillButton.Print // ��ӡ

		};
	}

	/**
	 * @function ��ʾ��Ƭ�к�
	 */
	public boolean isShowCardRowNo() {
		// TODO Auto-generated method stub
		return true;
	}

	/**
	 * @function ����ʾ��Ƭ�ϼ��� 
	 */
	public boolean isShowCardTotal() {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * @function �õ���������
	 */
	public String getBillType() {
		// TODO Auto-generated method stub
		return "HB1003";
	}

	/**
	 * @function �õ�����VO
	 */
	public String[] getBillVoName() {
		// TODO Auto-generated method stub
		return new String[] { HYBillVO.class.getName(),
				AdditionalValueVO.class.getName(),
				AdditionalValueVO.class.getName() };
	}

	/**
	 * @function ʵ�ָ���ӿڣ��ݲ����� 
	 */
	public String getBodyCondition() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @function ʵ�ָ���ӿڣ��ݲ�����
	 */
	public String getBodyZYXKey() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @function �õ�ҵ������ 
	 */
	public int getBusinessActionType() {
		// TODO Auto-generated method stub
		return IBusinessActionType.BD;
	}

	/**
	 * @function �õ��ӱ�����
	 */
	public String getChildPkField() {
		// TODO Auto-generated method stub
		return "pk_additionalvalue";
	}

	/**
	 * @function ʵ�ָ���ӿڣ��ݲ�����
	 */
	public String getHeadZYXKey() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @function �õ�����
	 */
	public String getPkField() {
		// TODO Auto-generated method stub
		return "pk_additionalvalue";
	}

	/**
	 * @function ʵ�ָ���ӿڣ��ݲ����� 
	 */
	public Boolean isEditInGoing() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @function ʵ�ָ���ӿڣ��ݲ�����
	 */
	public boolean isExistBillStatus() {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * @function ִ�п�Ƭ���ع�ʽ 
	 */
	public boolean isLoadCardFormula() {
		// TODO Auto-generated method stub
		return true;
	}

	/**
	 * @function ʵ�ָ���ӿڣ��ݲ�����
	 */
	public String[] getListBodyHideCol() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @function �����б�ť 
	 */
	public int[] getListButtonAry() {
		// TODO Auto-generated method stub
		return new int[] { 
				IBillButton.Query, // ��ѯ
				IBillButton.Add, // ����
				IBillButton.Edit, // �޸�
				IBillButton.Save, // ����
				IBillButton.Delete, // ɾ��
				IBillButton.Cancel, // ȡ��
				IBillButton.Card, // ��Ƭ��ʾ
				IBillButton.Refresh,// ˢ��
				IBillButton.Print // ��ӡ
		};
	}

	/**
	 * @function ʵ�ָ���ӿڣ��ݲ�����
	 */
	public String[] getListHeadHideCol() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @function ��ʾ�б��к�
	 *  
	 */
	public boolean isShowListRowNo() {
		// TODO Auto-generated method stub
		return true;
	}

	/**
	 * @function ����ʾ�б�ϼ���
	 * 
	 */
	public boolean isShowListTotal() {
		// TODO Auto-generated method stub
		return false;
	}

}
