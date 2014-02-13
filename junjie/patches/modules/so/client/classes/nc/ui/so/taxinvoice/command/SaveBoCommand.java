package nc.ui.so.taxinvoice.command;

import nc.ui.trade.bocommand.IButtonCommand;
import nc.ui.pub.bill.BillData;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.ClientEnvironment;
import nc.vo.pub.VOStatus;
/**
 * <b> ������ģ��У�鹦�ܵı������ </b><br>
 *
 * <p>
 *     �ڴ˴���Ӵ����������Ϣ
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
			data.getTailItem("dcreatedate").setValue(ClientEnvironment.getInstance().getServerTime().getDate());
		}
		if(data.getBillstatus() == VOStatus.UPDATED)  {
			data.getTailItem("dmodifydate").setValue(ClientEnvironment.getInstance().getServerTime().getDate());
			data.getTailItem("cmodifier").setValue(ClientEnvironment.getInstance().getUser().getPrimaryKey());
		}
		super.onBoSave();
		
		 
	}
}
 