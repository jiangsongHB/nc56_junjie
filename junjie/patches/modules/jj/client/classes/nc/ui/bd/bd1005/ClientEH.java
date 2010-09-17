package nc.ui.bd.bd1005;

import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.ui.trade.manage.ManageEventHandler;

  public class ClientEH extends ManageEventHandler {
	  
    public ClientEH(BillManageUI billUI, IControllerBase control) {
	   super(billUI, control);

 }
	protected void onBoQuery() throws Exception {
		// TODO Auto-generated method stub
		super.onBoQuery();
		}
}
