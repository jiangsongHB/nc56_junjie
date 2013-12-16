package nc.ui.bd.bd1003;


import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.ui.bd.ref.busi.InvbasdocDefaultRefModel;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.manage.BillManageUI;
import nc.ui.trade.manage.ManageEventHandler;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;

public class ClientUI extends BillManageUI {

	/**
	 * @function 实现父类的无参构造方法 
	 */
	public ClientUI() {
		super();

	}

	/**
	 * @function 实现父类的构造方法
	 */
	public ClientUI(String pk_corp, String pk_billType, String pk_busitype,
			String operater, String billId) {
		super(pk_corp, pk_billType, pk_busitype, operater, billId);
	}


	/**
	 * @function 创建EventHandler类
	 */
	protected ManageEventHandler createEventHandler() {
		return new ClientEH(this, getUIControl());
	}

	/**
	 * @function 创建UICtrl类
	 */
	protected AbstractManageController createController() {
		// TODO Auto-generated method stub
		return new ClientCtrl();
	}

	/**
	 * @function 实现父类的抽象方法，暂不处理 
	 */
	public void setBodySpecialData(CircularlyAccessibleValueObject[] vos)
			throws Exception {
		// TODO Auto-generated method stub

	}

	/**
	 * @function 实现父类的抽象方法，暂不处理
	 */
	protected void setHeadSpecialData(CircularlyAccessibleValueObject vo,
			int intRow) throws Exception {
		// TODO Auto-generated method stub

	}

	/**
	 * @function 实现父类的抽象方法，暂不处理 
	 */
	protected void setTotalHeadSpecialData(CircularlyAccessibleValueObject[] vos)
			throws Exception {
		// TODO Auto-generated method stub

	}

	/**
	 * @function 实现父类的抽象方法，暂不处理
 
	 */
	protected void initSelfData() {
		// TODO Auto-generated method stub

	}

	/**
	 * @function 实现父类的抽象方法，暂不处理
	 * 
	 */
	public void setDefaultData() throws Exception {
		// TODO Auto-generated method stub

	}
	public void afterEdit(nc.ui.pub.bill.BillEditEvent e) {
		if(e.getKey().equals("pk_invcl")){
			String pk_invcl = getBillCardPanel().getHeadItem("pk_invcl").getValue();
			InvbasdocDefaultRefModel invMod = 	(InvbasdocDefaultRefModel)((UIRefPane)getBillCardPanel().getHeadItem("pk_invbasdoc").getComponent()).getRefModel();		
			IUAPQueryBS query = NCLocator.getInstance().lookup(IUAPQueryBS.class);
			String invclasscode = null;
		try {
			 invclasscode = 	(String)query.executeQuery("select invclasscode from bd_invcl where pk_invcl = '"+pk_invcl+"'", new ColumnProcessor());
		} catch (BusinessException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
			invMod.setWherePart(" bd_invbasdoc.pk_invcl in (select pk_invcl from bd_invcl where invclasscode like '"+invclasscode+"%')");
			
			//invMod.setGroupPart("invtype");			
		}
		else if(e.getKey().equals("pk_invbasdoc")){
			String pk_invbasdoc = getBillCardPanel().getHeadItem("pk_invbasdoc").getValue();			
			getBillCardPanel().setHeadItem("invtype", execQuery(pk_invbasdoc));
		}
		
	}
	private Object execQuery(String pk_invbasdoc) {
		IUAPQueryBS qryBS = (IUAPQueryBS) NCLocator.getInstance().lookup(
				IUAPQueryBS.class.getName());
		Object o = null;
		String sql = "select invtype from bd_invbasdoc where pk_invbasdoc = '"+pk_invbasdoc+"'";
		try {
			o = qryBS.executeQuery(sql, new ColumnProcessor());
		} catch (BusinessException e) {
			e.printStackTrace();
		}
		return o;
	}
	protected void setListBodyData() throws Exception {
	}
}
