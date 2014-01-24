package nc.ui.ja.entityreceiptck;

import nc.ui.pub.ButtonObject;
import nc.ui.pub.ClientEnvironment;
import nc.ui.trade.bill.ICardController;
import nc.ui.trade.card.BillCardUI;
import nc.vo.pub.BusinessRuntimeException;

/**
  *
  *������AbstractMyEventHandler�������ʵ���࣬
  *��Ҫ�������˰�ť��ִ�ж������û����Զ���Щ����������Ҫ�����޸�
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
	

	//��ѯ
	protected void onFirstQuery() throws Exception {
		String strWhere = " isnull(dr,0) = 0 and " + getWherePart();
		doBodyQuery(strWhere);
	}   	
	@Override
	protected void onBoBodyQuery() throws Exception {
		// TODO Auto-generated method stub
		StringBuffer strWhere = new StringBuffer();

		if (askForBodyQueryCondition(strWhere) == false)
			return;// �û������˲�ѯ
		strWhere .append( " and " + getWherePart());
		doBodyQuery(strWhere.toString());
		System.out.println(strWhere);
	}
	/**
	* <p>
	*  ȡ���Զ���Ĳ�ѯ����
	* <p/>
	* 
	*/
	public String getWherePart() {
		
		String swhere="";
		swhere= "   ( pk_corp = '"+ ClientEnvironment.getInstance().getCorporation().getPrimaryKey()	+ "')";	
		return swhere;
	}
		
}