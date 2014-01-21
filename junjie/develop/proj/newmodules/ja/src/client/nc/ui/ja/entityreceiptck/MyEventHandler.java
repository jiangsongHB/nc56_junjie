package nc.ui.ja.entityreceiptck;

import nc.ui.pub.ButtonObject;
import nc.ui.pub.ClientEnvironment;
import nc.ui.trade.bill.ICardController;
import nc.ui.trade.card.BillCardUI;
import nc.vo.pub.BusinessRuntimeException;

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
	protected void onBoLineAdd() throws Exception {
		// TODO Auto-generated method stub
		super.onBoLineAdd();
		int row = getBillCardPanelWrapper().getBillCardPanel().getBillTable().getSelectedRow();
		getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(ClientEnvironment.getInstance().getCorporation().getPrimaryKey(), row, "pk_corp");
	}

	@Override
	protected void onBoSave() throws Exception {
		// TODO Auto-generated method stub
		super.onBoSave();
		onFirstQuery();
	}
	

	//查询
	protected void onFirstQuery() throws Exception {
		String strWhere = " isnull(dr,0) = 0 and " + getWherePart();
		doBodyQuery(strWhere);
	}   	
	@Override
	protected void onBoBodyQuery() throws Exception {
		// TODO Auto-generated method stub
		StringBuffer strWhere = new StringBuffer();

		if (askForBodyQueryCondition(strWhere) == false)
			return;// 用户放弃了查询
		strWhere .append( " and " + getWherePart());
		doBodyQuery(strWhere.toString());
		System.out.println(strWhere);
	}
	/**
	* <p>
	*  取得自定义的查询条件
	* <p/>
	* 
	*/
	public String getWherePart() {
		
		String swhere="";
		swhere= "   ( pk_corp = '"+ ClientEnvironment.getInstance().getCorporation().getPrimaryKey()	+ "')";	
		return swhere;
	}
		
}