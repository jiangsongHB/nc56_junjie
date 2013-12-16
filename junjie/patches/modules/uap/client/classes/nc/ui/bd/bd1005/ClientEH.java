package nc.ui.bd.bd1005;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.ui.pub.bill.BillItem;
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
		    //add by ouyangzhb 2011-08-20 解决毛边系数删除后在新增后修改时不能保存的问题
		    String billpk = this.getBillCardPanelWrapper().getBillCardPanel().getHeadItem("pk_burrcoefficient").getValue();
		    if(billpk==null || "".equals(billpk)){
		    	String sql = "select count(pk_invbasdoc) from jj_bd_burrcoefficient where pk_invbasdoc = '"+pk_invbasdoc+"' and dr=0 " ;
			    int o = Integer.parseInt(query.executeQuery(sql, new ColumnProcessor()).toString());
			    if(o > 0){
			    	getBillUI().showErrorMessage("该存货已设置系数！");
			    	return;
			    }
		    	
		    }
		    //add by ouyangzhb 2011-08-20 end
		    
		    super.onBoSave();
		    
		    }
}
