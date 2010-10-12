package nc.ui.bd.bd1003;


import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.ui.trade.manage.ManageEventHandler;

/**
 * <p>
 * 该类是一个抽象类，主要目的是生成按钮事件处理的框架
 * <p/>
 * <b>修改内容 修改时间 修改人:</b>
 * <ul>
 * <li></li>
 * </ul>
 * @author libaoshan
 * @param 
 * @return 
 * @exception 
 */
    
 public class ClientEH extends ManageEventHandler {


        /**
	 * <p>
	 * 构造方法
	 * <p/>
	 * <b>修改内容 修改时间 修改人:</b>
	 * <ol>
	 * <li></li>
	 * </ol>
	 * @author libaoshan
	 * @param 
	 * @return 
	 * @exception 
	 */
	public ClientEH(BillManageUI billUI, IControllerBase control) {
		   super(billUI, control);
	}
        /**
	 * <p>
	 * 自定义按钮的处理
	 * <p/>
	 * <b>修改内容 修改时间 修改人:</b>
	 * <ol>
	 * <li></li>
	 * </ol>
	 * @author libaoshan
	 * @param intBtn 按钮的编辑 100以下为系统按钮,100以上为自定义按钮
	 * @return 
	 * @exception Exception 抛出异常
	 */
/*	protected void onBoElse(int intBtn) throws Exception {
		
	     	}*/
	@Override
	protected void onBoQuery() throws Exception {
	// TODO Auto-generated method stub
	super.onBoQuery();
	}

	
}