package nc.ui.ic.pub.pf;

import java.awt.Container;

import nc.ui.pub.ClientEnvironment;

public class IC4ATo4804UI  extends ICSourceBillToBillBaseUI{
	 public IC4ATo4804UI(Container ct) {
		    super(ct,null, "4A", "4804", ClientEnvironment.getInstance().getCorporation().getPrimaryKey(), 
		        ClientEnvironment.getInstance().getUser().getPrimaryKey());
		    createUI(createBillRefListPanel(), createSimpBillRefListPanel());
//		    switchShow(getQueryDlg().isShowDoubleTableRef()?
//		        BillToBillRefPanel.ShowState.DoubleTable:BillToBillRefPanel.ShowState.OneTable);
		    //switchDisplayMode(nc.ui.scm.pub.redunmulti.DisplayMode.Extend);
		    // TODO 自动生成构造函数存根
		  }
	
}
