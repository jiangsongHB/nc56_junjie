package nc.ui.bd.bd1001;

import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.ui.trade.manage.ManageEventHandler;

/**
 * @function ����ϵ���ڵ�EventHandler��
 * 
 * @author QuSida
 * 
 * @date 2010-08-05 11:39:22
 * 
 */
public class ClientEH extends ManageEventHandler {

	/**
	 * @function ʵ�ָ���Ĺ��췽��
	 * 
	 * @author QuSida
	 * 
	 * @date 2010-08-05 11:40:02
	 * 
	 */
	public ClientEH(BillManageUI billUI, IControllerBase control) {
		super(billUI, control);

	}
	protected void onBoQuery() throws Exception {
		// TODO Auto-generated method stub
		super.onBoQuery();
		}

}
