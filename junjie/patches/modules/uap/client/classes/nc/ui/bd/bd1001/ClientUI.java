package nc.ui.bd.bd1001;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.ui.pub.bill.BillItem;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.manage.BillManageUI;
import nc.ui.trade.manage.ManageEventHandler;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;

/**
 * @function ����ϵ���ڵ�UI
 * 
 * @author QuSida
 * 
 * @date 2010-08-05 10:40:35
 * 
 */

public class ClientUI extends BillManageUI {

	/**
	 * @function ʵ�ָ�����޲ι��췽��
	 * 
	 * @author QuSida
	 * 
	 * @date 2010-08-05 11:40:35
	 * 
	 */
	public ClientUI() {
		super();
	}

	/**
	 * @function ʵ�ָ���Ĺ��췽��
	 * 
	 * @author QuSida
	 * 
	 * @date 2010-08-05 11:41:22
	 * 
	 */
	public ClientUI(String pk_corp, String pk_billType, String pk_busitype,
			String operater, String billId) {
		super(pk_corp, pk_billType, pk_busitype, operater, billId);
	}


	/**
	 * @function ����EventHandler��
	 * 
	 * @author QuSida
	 * 
	 * @date 2010-08-05 11:41:32
	 */
	protected ManageEventHandler createEventHandler() {
		return new ClientEH(this, getUIControl());
	}

	/**
	 * @function ����UICtrl��
	 * 
	 * @author QuSida
	 * 
	 * @date 2010-08-05 11:41:55
	 * 
	 */
	protected AbstractManageController createController() {
		// TODO Auto-generated method stub
		return new ClientCtrl();
	}

	/**
	 * @function ʵ�ָ���ĳ��󷽷����ݲ�����
	 * 
	 * @author QuSida
	 * 
	 * @date 2010-08-05 11:42:03
	 * 
	 */
	public void setBodySpecialData(CircularlyAccessibleValueObject[] vos)
			throws Exception {
		// TODO Auto-generated method stub

	}

	/**
	 * @function ʵ�ָ���ĳ��󷽷����ݲ�����
	 * 
	 * @author QuSida
	 * 
	 * @date 2010-08-05 11:42:15
	 * 
	 */
	protected void setHeadSpecialData(CircularlyAccessibleValueObject vo,
			int intRow) throws Exception {
		// TODO Auto-generated method stub

	}

	/**
	 * @function ʵ�ָ���ĳ��󷽷����ݲ�����
	 * 
	 * @author QuSida
	 * 
	 * @date 2010-08-05 11:42:21
	 * 
	 */
	protected void setTotalHeadSpecialData(CircularlyAccessibleValueObject[] vos)
			throws Exception {
		// TODO Auto-generated method stub

	}

	/**
	 * @function ʵ�ָ���ĳ��󷽷����ݲ�����
	 * 
	 * @author QuSida
	 * 
	 * @date 2010-08-05 11:42:44
	 * 
	 */
	protected void initSelfData() {
		// TODO Auto-generated method stub

	}

	/**
	 * @function ʵ�ָ���ĳ��󷽷����ݲ�����
	 * 
	 * @author QuSida
	 * 
	 * @date 2010-08-05 11:42:55
	 * 
	 */
	public void setDefaultData() throws Exception {
		// TODO Auto-generated method stub

	}
	/**
	 * @function ��д����༭���¼��������ڹ�ʽ�����������˼������յı༭���¼������ݲ���ǿ��Ϊ���ֵ
	 *
	 * @author QuSida
	 *
	 * @param e 
	 *
	 * @date 2010-8-12 ����01:52:15
	 */
	public void afterEdit(nc.ui.pub.bill.BillEditEvent e) {
		if(e.getKey().equals("pk_invbasdoc")){
			String pk_invbasdoc = getBillCardPanel().getHeadItem("pk_invbasdoc").getValue();			
			getBillCardPanel().setHeadItem("vinvspec", execQuery(pk_invbasdoc));
		}
	}
	/**
	 * @function ���ݴ����������ID������
	 *
	 * @author QuSida
	 *
	 * @param pk_invbasdoc 
	 *
	 * @return Object
	 *
	 * @date 2010-8-12 ����01:51:32
	 */
	private Object execQuery(String pk_invbasdoc) {
		IUAPQueryBS qryBS = (IUAPQueryBS) NCLocator.getInstance().lookup(
				IUAPQueryBS.class.getName());
		Object o = null;
		String sql = "select invspec from bd_invbasdoc where pk_invbasdoc = '"+pk_invbasdoc+"'";
		try {
			o = qryBS.executeQuery(sql, new ColumnProcessor());
		} catch (BusinessException e) {
			e.printStackTrace();
		}
		return o;
	}

}
