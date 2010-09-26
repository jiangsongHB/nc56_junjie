package nc.ui.bd.bd1003;


import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ColumnProcessor;
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
	  protected void onBoSave() throws Exception {
		    String pk_invbasdoc = getBillCardPanelWrapper().getBillCardPanel().getHeadItem("pk_invbasdoc").getValue();
		    IUAPQueryBS query = NCLocator.getInstance().lookup(IUAPQueryBS.class);
		    String sql = "select count(pk_invbasdoc) from jj_bd_additionalvalue where pk_invbasdoc = '"+pk_invbasdoc+"'" ;
		    int o = Integer.parseInt(query.executeQuery(sql, new ColumnProcessor()).toString());
		    if(o > 0){
		    	getBillUI().showErrorMessage("该存货已设置系数！");
		    	return;
		    }
		    super.onBoSave();
		    
		    }

	
}