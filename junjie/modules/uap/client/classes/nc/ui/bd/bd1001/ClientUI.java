package nc.ui.bd.bd1001;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.ui.pub.bill.BillItem;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.manage.BillManageUI;
import nc.ui.trade.manage.ManageEventHandler;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;

/**
 * @function 理算系数节点UI
 * 
 * @author QuSida
 * 
 * @date 2010-08-05 10:40:35
 * 
 */

public class ClientUI extends BillManageUI {

	/**
	 * @function 实现父类的无参构造方法
	 * 
	 * @author QuSida
	 * 
	 * @date 2010-08-05 11:40:35
	 * 
	 */
	public ClientUI() {
		super();
	}

	/**
	 * @function 实现父类的构造方法
	 * 
	 * @author QuSida
	 * 
	 * @date 2010-08-05 11:41:22
	 * 
	 */
	public ClientUI(String pk_corp, String pk_billType, String pk_busitype,
			String operater, String billId) {
		super(pk_corp, pk_billType, pk_busitype, operater, billId);
	}


	/**
	 * @function 创建EventHandler类
	 * 
	 * @author QuSida
	 * 
	 * @date 2010-08-05 11:41:32
	 */
	protected ManageEventHandler createEventHandler() {
		return new ClientEH(this, getUIControl());
	}

	/**
	 * @function 创建UICtrl类
	 * 
	 * @author QuSida
	 * 
	 * @date 2010-08-05 11:41:55
	 * 
	 */
	protected AbstractManageController createController() {
		// TODO Auto-generated method stub
		return new ClientCtrl();
	}

	/**
	 * @function 实现父类的抽象方法，暂不处理
	 * 
	 * @author QuSida
	 * 
	 * @date 2010-08-05 11:42:03
	 * 
	 */
	public void setBodySpecialData(CircularlyAccessibleValueObject[] vos)
			throws Exception {
		// TODO Auto-generated method stub

	}

	/**
	 * @function 实现父类的抽象方法，暂不处理
	 * 
	 * @author QuSida
	 * 
	 * @date 2010-08-05 11:42:15
	 * 
	 */
	protected void setHeadSpecialData(CircularlyAccessibleValueObject vo,
			int intRow) throws Exception {
		// TODO Auto-generated method stub

	}

	/**
	 * @function 实现父类的抽象方法，暂不处理
	 * 
	 * @author QuSida
	 * 
	 * @date 2010-08-05 11:42:21
	 * 
	 */
	protected void setTotalHeadSpecialData(CircularlyAccessibleValueObject[] vos)
			throws Exception {
		// TODO Auto-generated method stub

	}

	/**
	 * @function 实现父类的抽象方法，暂不处理
	 * 
	 * @author QuSida
	 * 
	 * @date 2010-08-05 11:42:44
	 * 
	 */
	protected void initSelfData() {
		// TODO Auto-generated method stub

	}

	/**
	 * @function 实现父类的抽象方法，暂不处理
	 * 
	 * @author QuSida
	 * 
	 * @date 2010-08-05 11:42:55
	 * 
	 */
	public void setDefaultData() throws Exception {
		// TODO Auto-generated method stub

	}
	/**
	 * @function 重写父类编辑后事件处理，由于公式带不出规格，因此监听参照的编辑后事件，根据参照强行为规格赋值
	 *
	 * @author QuSida
	 *
	 * @param e 
	 *
	 * @date 2010-8-12 下午01:52:15
	 */
	public void afterEdit(nc.ui.pub.bill.BillEditEvent e) {
		if(e.getKey().equals("pk_invbasdoc")){
			String pk_invbasdoc = getBillCardPanel().getHeadItem("pk_invbasdoc").getValue();			
			getBillCardPanel().setHeadItem("vinvspec", execQuery(pk_invbasdoc));
		}
	}
	/**
	 * @function 根据存货基本档案ID查出规格
	 *
	 * @author QuSida
	 *
	 * @param pk_invbasdoc 
	 *
	 * @return Object
	 *
	 * @date 2010-8-12 下午01:51:32
	 */
	private Object execQuery(String pk_invbasdoc) {
		IUAPQueryBS qryBS = (IUAPQueryBS) NCLocator.getInstance().lookup(
				IUAPQueryBS.class.getName());
		Object o = null;
		String sql = "select invspec from bd_invbasdoc where pk_invbasdoc = '"+pk_invbasdoc+"'";
		try {
			o = qryBS.executeQuery(sql, new ColumnProcessor());
		} catch (BusinessException e) {
			e.printStackTrace();
		}
		return o;
	}

}
