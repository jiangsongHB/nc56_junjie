package nc.ui.bd.bd1001;

import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.ui.trade.manage.ManageEventHandler;

/**
 * @function 理算系数节点EventHandler类
 * 
 * @author QuSida
 * 
 * @date 2010-08-05 11:39:22
 * 
 */
public class ClientEH extends ManageEventHandler {

	/**
	 * @function 实现父类的构造方法
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
