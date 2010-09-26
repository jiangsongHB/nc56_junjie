package nc.ui.bd.bd1005;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ColumnProcessor;
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
	  protected void onBoSave() throws Exception {
		    String pk_invbasdoc = getBillCardPanelWrapper().getBillCardPanel().getHeadItem("pk_invbasdoc").getValue();
		    IUAPQueryBS query = NCLocator.getInstance().lookup(IUAPQueryBS.class);
		    String sql = "select count(pk_invbasdoc) from jj_bd_burrcoefficient where pk_invbasdoc = '"+pk_invbasdoc+"'" ;
		    int o = Integer.parseInt(query.executeQuery(sql, new ColumnProcessor()).toString());
		    if(o > 0){
		    	getBillUI().showErrorMessage("该存货已设置系数！");
		    	return;
		    }
		    super.onBoSave();
		    
		    }
}
