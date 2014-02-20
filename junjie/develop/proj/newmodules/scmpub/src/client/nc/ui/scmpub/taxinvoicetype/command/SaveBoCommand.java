package nc.ui.scmpub.taxinvoicetype.command;

import nc.ui.trade.bocommand.IButtonCommand;
import nc.ui.pub.bill.BillData;
import nc.ui.pub.bill.BillStatus;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.ClientEnvironment;
import nc.vo.pub.VOStatus;
import nc.vo.trade.pub.IBillStatus;
/**
 * <b> 带单据模板校验功能的保存操作 </b><br>
 *
 * <p>
 *     在此处添加此类的描述信息
 * </p>
 *
 *
 * @author author
 * @version tempProject version
 */
public class SaveBoCommand extends nc.ui.trade.manage.ManageEventHandler implements IButtonCommand {
	 

	public SaveBoCommand(nc.ui.trade.manage.BillManageUI billUI, nc.ui.trade.controller.IControllerBase control) {
		super(billUI, control);
	}

	public void execute(ButtonObject bo) throws Exception {
	
		BillData data = getBillCardPanelWrapper().getBillCardPanel().getBillData();
		if(data != null)
			data.dataNotNullValidate(); 
		
		if(data.getBillstatus() == VOStatus.NEW) {
			data.getBodyItem("creationtime").setValue(ClientEnvironment.getInstance().getServerTime().getDate());
		}
		if(data.getBillstatus() == VOStatus.UPDATED)  {
			data.getBodyItem("modifiedtime").setValue(ClientEnvironment.getInstance().getServerTime().getDate());
			data.getBodyItem("modifier").setValue(ClientEnvironment.getInstance().getUser().getPrimaryKey());
		}
		super.onBoSave();
		 
	}
}
 