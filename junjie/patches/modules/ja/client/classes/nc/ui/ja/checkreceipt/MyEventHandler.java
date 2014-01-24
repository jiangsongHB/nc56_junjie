package nc.ui.ja.checkreceipt;

import nc.ui.trade.bill.ICardController;
import nc.ui.trade.card.BillCardUI;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessRuntimeException;
import nc.vo.pub.SuperVO;

/**
  *
  *该类是AbstractMyEventHandler抽象类的实现类，
  *主要是重载了按钮的执行动作，用户可以对这些动作根据需要进行修改
  *@author author
  *@version tempProject version
  */
  
  public class MyEventHandler 
                                          extends AbstractMyEventHandler{

	public MyEventHandler(BillCardUI billUI,ICardController control){
		super(billUI,control);		
	}

	

	@Override
	protected void onBoSelAll() throws Exception {
		// TODO Auto-generated method stub
		int rows=getBillCardPanelWrapper().getBillCardPanel().getRowCount();
		for (int i = 0; i < rows; i++) {
			getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt("Y", i, "ischoice");
			getBillCardPanelWrapper().getBillCardPanel().setCellEditable(i, "checkamount", true);				
			getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i, "taxamount"), i, "checkamount");
		}
	}

	@Override
	protected void onBoSelNone() throws Exception {
		// TODO Auto-generated method stub
		int rows=getBillCardPanelWrapper().getBillCardPanel().getRowCount();
		for (int i = 0; i < rows; i++) {
			getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt("N", i, "ischoice");
			getBillCardPanelWrapper().getBillCardPanel().setCellEditable(i, "checkamount", false);
			getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(null, i, "checkamount");
		}
	}
	
		
}