package nc.ui.bd.bd1001;

import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.businessaction.IBusinessActionType;
import nc.ui.trade.button.IBillButton;
import nc.vo.bd.bd1001.AdjustmentCoefficientVO;

import nc.vo.trade.pub.HYBillVO;

/**
 * @function ����ϵ���ڵ�UICtrl��
 * 
 * @author QuSida
 * 
 * @date 2010-08-05 11:55:25
 * 
 */
public class ClientCtrl extends AbstractManageController {

	/**
	 * @function ʵ�ָ���Ĺ��췽��
	 * 
	 * @author QuSida
	 * 
	 * @date 2010-8-5 ����04:03:44
	 * 
	 */
	public ClientCtrl() {
		super();
	}

	/**
	 * @function ʵ�ָ���ӿڣ��ݲ�����
	 * 
	 * @author QuSida
	 * 
	 * @date 2010-8-5 ����04:04:15
	 * 
	 */
	public String[] getCardBodyHideCol() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @function ���ÿ�Ƭ��ť
	 * 
	 * @author QuSida
	 * 
	 * @date 2010-8-5 ����04:07:34
	 * 
	 */
	public int[] getCardButtonAry() {
		// TODO Auto-generated method stub
		return new int[] { IBillButton.Query,// ��ѯ
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
	 * 
	 * @author QuSida
	 * 
	 * @date 2010-8-5 ����04:13:10
	 * 
	 */
	public boolean isShowCardRowNo() {
		// TODO Auto-generated method stub
		return true;
	}

	/**
	 * @function ����ʾ��Ƭ�ϼ���
	 * 
	 * @author QuSida
	 * 
	 * @date 2010-8-5 ����04:13:33
	 * 
	 */
	public boolean isShowCardTotal() {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * @function �õ���������
	 * 
	 * @author QuSida
	 * 
	 * @date 2010-8-5 ����04:14:06
	 * 
	 */
	public String getBillType() {
		// TODO Auto-generated method stub
		return "HB1001";
	}

	/**
	 * @function �õ�����VO
	 * 
	 * @author QuSida
	 * 
	 * @date 2010-8-5 ����04:14:16
	 * 
	 */
	public String[] getBillVoName() {
		// TODO Auto-generated method stub
		return new String[] { HYBillVO.class.getName(),
				AdjustmentCoefficientVO.class.getName(),
				AdjustmentCoefficientVO.class.getName() };
	}

	/**
	 * @function ʵ�ָ���ӿڣ��ݲ�����
	 * 
	 * @author QuSida
	 * 
	 * @date 2010-8-5 ����04:10:07
	 * 
	 */
	public String getBodyCondition() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @function ʵ�ָ���ӿڣ��ݲ�����
	 * 
	 * @author QuSida
	 * 
	 * @date 2010-8-5 ����04:10:03
	 * 
	 */
	public String getBodyZYXKey() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @function �õ�ҵ������
	 * 
	 * @author QuSida
	 * 
	 * @date 2010-8-5 ����04:14:41
	 * 
	 */
	public int getBusinessActionType() {
		// TODO Auto-generated method stub
		return IBusinessActionType.BD;
	}

	/**
	 * @function �õ��ӱ�����
	 * 
	 * @author QuSida
	 * 
	 * @date 2010-8-5 ����04:19:20
	 * 
	 */
	public String getChildPkField() {
		// TODO Auto-generated method stub
		return "pk_adjustmentcoefficient";
	}

	/**
	 * @function ʵ�ָ���ӿڣ��ݲ�����
	 * 
	 * @author QuSida
	 * 
	 * @date 2010-8-5 ����04:10:14
	 * 
	 */
	public String getHeadZYXKey() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @function �õ�����
	 * 
	 * @author QuSida
	 * 
	 * @date 2010-8-5 ����04:19:38
	 * 
	 */
	public String getPkField() {
		// TODO Auto-generated method stub
		return "pk_adjustmentcoefficient";
	}

	/**
	 * @function ʵ�ָ���ӿڣ��ݲ�����
	 * 
	 * @author QuSida
	 * 
	 * @date 2010-8-5 ����04:10:21
	 * 
	 */
	public Boolean isEditInGoing() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @function ʵ�ָ���ӿڣ��ݲ�����
	 * 
	 * @author QuSida
	 * 
	 * @date 2010-8-5 ����04:10:26
	 * 
	 */
	public boolean isExistBillStatus() {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * @function ִ�п�Ƭ���ع�ʽ
	 * 
	 * @author QuSida
	 * 
	 * @date 2010-8-5 ����04:19:59
	 * 
	 */
	public boolean isLoadCardFormula() {
		// TODO Auto-generated method stub
		return true;
	}

	/**
	 * @function ʵ�ָ���ӿڣ��ݲ�����
	 * 
	 * @author QuSida
	 * 
	 * @date 2010-8-5 ����04:20:39
	 * 
	 */
	public String[] getListBodyHideCol() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @function �����б�ť
	 * 
	 * @author QuSida
	 * 
	 * @date 2010-8-5 ����04:20:45
	 * 
	 */
	public int[] getListButtonAry() {
		// TODO Auto-generated method stub
		return new int[] { IBillButton.Query, // ��ѯ
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
	 * 
	 * @author QuSida
	 * 
	 * @date 2010-8-5 ����04:20:58
	 * 
	 */
	public String[] getListHeadHideCol() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @function ��ʾ�б��к�
	 * 
	 * @author QuSida
	 * 
	 * @date 2010-8-5 ����04:21:33
	 * 
	 */
	public boolean isShowListRowNo() {
		// TODO Auto-generated method stub
		return true;
	}

	/**
	 * @function ����ʾ�б�ϼ���
	 * 
	 * @author QuSida
	 * 
	 * @date 2010-8-5 ����04:21:31
	 * 
	 */
	public boolean isShowListTotal() {
		// TODO Auto-generated method stub
		return false;
	}

}
