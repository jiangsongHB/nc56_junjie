package nc.ui.bd.bd1001;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.ui.am.inventory.command.ShowEqualCommand;
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
    protected void onBoSave() throws Exception {
    String pk_invbasdoc = getBillCardPanelWrapper().getBillCardPanel().getHeadItem("pk_invbasdoc").getValue();
    IUAPQueryBS query = NCLocator.getInstance().lookup(IUAPQueryBS.class);
    String sql = "select count(pk_invbasdoc) from jj_bd_adjustmentcoefficient where pk_invbasdoc = '"+pk_invbasdoc+"'" ;
    int o = Integer.parseInt(query.executeQuery(sql, new ColumnProcessor()).toString());
    if(o > 0){
    	getBillUI().showErrorMessage("该存货已设置系数！");
    	return;
    }
    super.onBoSave();
    
    }
}
