package nc.ui.so.taxinvoice;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.SQLException;
import java.util.Map;
import java.util.HashMap;
import java.util.List;

import nc.bs.logging.Logger;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.bill.BillStatus;
import nc.ui.trade.bill.BillTemplateWrapper;
import nc.ui.trade.bocommand.IButtonCommand;
import nc.ui.trade.bocommand.MetaDataPrintBoCommand;
import nc.ui.trade.bocommand.IUserDefButtonCommand; 
import nc.ui.scm.pub.report.BillRowNo;
import nc.ui.so.taxinvoice.command.SaveBoCommand;
import nc.ui.trade.button.IBillButton;
import nc.ui.trade.manage.ManageEventHandler;
import nc.vo.pf.pub.IBillState;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pub.lang.UFDouble;
import nc.ui.trade.base.IBillConst;
import nc.ui.trade.base.IBillOperate;

import nc.ui.trade.controller.IControllerBase;
import nc.vo.scmpub.ITaxInvoiceApproveType;
import nc.vo.so.TaxInvoiceBbVO;
import nc.vo.so.TaxInvoiceHeaderVO;
import nc.vo.so.TaxInvoiceItemVO;
import nc.vo.so.TaxInvoiceVO;
import nc.vo.trade.button.ButtonVO;
import nc.vo.trade.pub.IBillStatus;
import nc.vo.wa.wa_001_03.TaxgroupHeaderVO;

/** 
  *
  *该类用来进行按钮事件处理, 扩展按钮的事件响应应该实现IUIButtonCommand并进行注册
  *@author author
  *@version tempProject version
  */
  
public class MyEventHandler 
                                          extends ManageEventHandler {
	

	private Map<Integer, IButtonCommand> commands = new HashMap<Integer, IButtonCommand>();

//	private  final static String strPanelCard = "卡片";
//	private final static String strPanelList = "列表";
	
//	private String strShowState = strPanelList; 

	public MyEventHandler(nc.ui.trade.manage.BillManageUI billUI, IControllerBase control){
		super(billUI,control);
		initBoCommand();
	}

	protected void addBoCommand(int intBtn,IButtonCommand command){
		commands.put(intBtn, command);
	}
	
	private IButtonCommand getButtonCommand(int intBtn){
		return commands.get(intBtn);
	}
	
	private void preBoCommand(){
		addBoCommand(IBillButton.Print, new MetaDataPrintBoCommand(getBillUI(),getUIController()));
		addBoCommand(IBillButton.Save, new SaveBoCommand((nc.ui.trade.manage.BillManageUI)getBillUI(),getUIController()));
	}
	
	protected void initBoCommand(){ 
	
		preBoCommand();
		
		List<IUserDefButtonCommand> bos = ((ClientUI)getBillUI()).getUserButtons();
		if(bos != null) {
		   for(IUserDefButtonCommand cmd : bos)
			   addBoCommand(cmd.getButtonVO().getBtnNo(), cmd);
		}
//		
//		if (((ClientUI)getBillUI()).isSaveAndCommitTogether()) {
//			getButtonManager().getButton(nc.ui.trade.button.IBillButton.Commit).setVisible(false);  //2014-02-16 自动提交的话，这里就不显示这个按钮了 
//		}
	} 

	
	 @Override
	 public void onButton(ButtonObject bo) {
	 
	  	  Object boData = null;
		  if (bo.getData() != null) {
		  	  boData = bo.getData();
		  }else if(bo.getParent() != null && bo.getParent().getData() != null) {
		   	  boData = bo.getParent().getData();
		  }
		  
		  int intBtn = -1;
		  if(boData != null && boData instanceof ButtonVO)
		   	  intBtn = ((ButtonVO) boData).getBtnNo();
		  
		  IButtonCommand command = getButtonCommand(intBtn);
		  if (command != null)
		  	 onBoCommand(command,bo);
		  else
		   	 super.onButton(bo);

//		  setUserButtonStatus();
	 }
	 




	private void onBoCommand(IButtonCommand command, ButtonObject bo){
	 
		if (getBillUI().getBillOperate() == IBillOperate.OP_ADD
				|| getBillUI().getBillOperate() == IBillOperate.OP_EDIT) {
			if (getBillCardPanelWrapper() != null)
				getBillCardPanelWrapper().getBillCardPanel().stopEditing();
		}
		try {
			command.execute(bo);
//			setUserButtonStatus();  //更新按钮状态；
		} catch (BusinessException ex) {
			onBusinessException(ex);
		} catch (SQLException ex) {
			getBillUI().showErrorMessage(ex.getMessage());
		} catch (Exception e) {
			getBillUI().showErrorMessage(e.getMessage());
			Logger.error(e.getMessage());
		}
	}
		@Override
		public void onBoAudit() throws Exception {
			// TODO Auto-generated method stub

			if (((ClientUI)getBillUI()).checkAudit()) {
				super.onBoAudit();
			}
			else
				MessageDialog.showErrorDlg(getBillUI(), "错误", "发票审核策略设置与发票核销记录不符，请检查！");
		}


//		public String getStrShowState() {
//			return strShowState;
//		}
//
//		public void setStrShowState(String strShowState) {
//			this.strShowState = strShowState;
//		}

		@Override
		protected void onBoLineAdd() throws Exception {
			// TODO Auto-generated method stub
			super.onBoLineAdd();
			//取表头的税率项目
			UFDouble ntaxrate = getBillCardPanelWrapper().getBillCardPanel().getHeadItem("ntaxrate") == null? UFDouble.ZERO_DBL: new UFDouble(getBillCardPanelWrapper().getBillCardPanel().getHeadItem("ntaxrate").getValue().toString());
			getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(ntaxrate, getBillCardPanelWrapper().getBillCardPanel()
					.getBillTable().getSelectedRow(), "ntaxrate") ; 
			
			BillRowNo.addLineRowNo(getBillCardPanelWrapper().getBillCardPanel(), "",  //自动行号处理；
					"crowno");

		}

		@Override
		protected void onBoCard() throws Exception {
			// TODO Auto-generated method stub
			super.onBoCard();
			  //user code 
			
//			setStrShowState(strPanelCard);
			  
//			setUserButtonStatus();
		}

		@Override
		protected void onBoReturn() throws Exception {
			// TODO Auto-generated method stub
			super.onBoReturn();
			
//			setStrShowState(strPanelList);
			
//			setUserButtonStatus();
		}

		/* (non-Javadoc)
		 * @see nc.ui.trade.manage.ManageEventHandler#onBoAdd(nc.ui.pub.ButtonObject)
		 */
		@Override
		public void onBoAdd(ButtonObject bo) throws Exception {
			// TODO Auto-generated method stub
			super.onBoAdd(bo);
//			setStrShowState(strPanelCard);
		}

		/* (non-Javadoc)
		 * @see nc.ui.trade.manage.ManageEventHandler#onBoDel()
		 */
		@Override
		protected void onBoDel() throws Exception {
			// TODO Auto-generated method stub
			if (((ClientUI)getBillUI()).checkDeal() ) 
				MessageDialog.showErrorDlg(getBillUI(), "错误", "当前发票已经存在核销记录，不能删除，请检查！");
			super.onBoDel();
			
			getBillUI().getBufferData().removeCurrentRow();    //2014-02-16 删除后自动刷新界面。
			getBillUI().getBufferData().refresh();
		}

		/* (non-Javadoc)
		 * @see nc.ui.trade.manage.ManageEventHandler#onBoCancelAudit()
		 */
		@Override
		protected void onBoCancelAudit() throws Exception {
			// TODO Auto-generated method stub
			super.onBoCancelAudit();
			
		}

		/* (non-Javadoc)
		 * @see nc.ui.trade.manage.ManageEventHandler#onBoRefresh()
		 */
		@Override
		protected void onBoRefresh() throws Exception {
			// TODO Auto-generated method stub
			super.onBoRefresh();
		}

}