package nc.ui.scmpub.taxinvoicetype;

import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.trade.manage.ManageEventHandler;
 
  
import nc.ui.trade.bocommand.IUserDefButtonCommand;
import java.util.ArrayList;
import java.util.List;  


/**
 * <b> �ڴ˴���Ҫ��������Ĺ��� </b>
 *
 * <p>
 *     �ڴ˴���Ӵ����������Ϣ
 * </p>
 *
 *
 * @author author
 * @version tempProject version
 */
 public class ClientUI extends AbstractClientUI{
       
	protected ManageEventHandler createEventHandler() {
		return new MyEventHandler(this, getUIControl());
	}
       
	public void setBodySpecialData(CircularlyAccessibleValueObject[] vos)
			throws Exception {}

	protected void setHeadSpecialData(CircularlyAccessibleValueObject vo,
			int intRow) throws Exception {}

	protected void setTotalHeadSpecialData(CircularlyAccessibleValueObject[] vos)
			throws Exception {	}

	protected void initSelfData() {
		setNoEditItems();
	}
	
		
	 protected List<IUserDefButtonCommand> creatUserButtons(){
		 
		  List<IUserDefButtonCommand> bos = new ArrayList<IUserDefButtonCommand>();
		  return bos;
		}

	public void setDefaultData() throws Exception {
		getBillCardPanel().getBodyItem("pk_corp").setDefaultValue(ClientEnvironment.getInstance().getCorporation().getPk_corp());
		getBillCardPanel().getBodyItem("creator").setDefaultValue(ClientEnvironment.getInstance().getUser().getPrimaryKey());
	}
	
	private void setNoEditItems() {
		// TODO Auto-generated method stub
		String[] snoedititems = new String[] {"pk_taxinvoicetype","pk_corp","creator","creationtime",
				"modifier","modifiedtime","approver","approvedtime"};
		
		for (int i = 0 ; i < snoedititems.length; i++ ){
			getBillCardPanel().getBodyItem(snoedititems[i]).setEdit(false);
		}
	}

}
