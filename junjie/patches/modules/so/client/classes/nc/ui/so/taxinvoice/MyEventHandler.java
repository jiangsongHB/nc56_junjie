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

	private  final static String strPanelCard = "卡片";
	private final static String strPanelList = "列表";
	
	private String strShowState = strPanelList; 

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
		
		if (((ClientUI)getBillUI()).isSaveAndCommitTogether()) {
			getButtonManager().getButton(nc.ui.trade.button.IBillButton.Commit).setVisible(false);  //2014-02-16 自动提交的话，这里就不显示这个按钮了 
		}
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

		  setUserButtonStatus();
	 }
	 
	 private void setUserButtonStatus() {
		// TODO Auto-generated method stub
		try { 
			if (((ClientUI)getBillUI()).isListPanelSelected()) {
				setStrShowState(strPanelList);
			}else
			{
				setStrShowState(strPanelCard);
			}
			if (getBillUI().getBillOperate() == IBillOperate.OP_ADD ||
					getBillUI().getBillOperate() == IBillOperate.OP_INIT ||
					getBillUI().getBillOperate() == IBillOperate.OP_REFADD ||
					(getBillUI().getBillOperate() == IBillOperate.OP_NOTEDIT  &&   getStrShowState() == strPanelList )) {
				getButtonManager().getButton(nc.ui.so.taxinvoice.command.btDealGpBoCommand.BUTTON_NO).setEnabled(false);
			}
			
			int iapprovetype = Integer.parseInt(getBillCardPanelWrapper().getBillCardPanel().getHeadItem("iapprovetype").getValue());
			TaxInvoiceVO taxinvvo = (TaxInvoiceVO) getBillCardPanelWrapper().getBillVOFromUI();
			int ibillstate = ((TaxInvoiceHeaderVO)taxinvvo.getParentVO()).getIbillstatus();
			
			if (getBillUI().getBillOperate() == IBillOperate.OP_NOTEDIT && getStrShowState() == strPanelCard) {
				getButtonManager().getButton(nc.ui.so.taxinvoice.command.btDealGpBoCommand.BUTTON_NO).setEnabled(true);
				//getButtonManager().getButton(nc.ui.so.taxinvoice.command.btRevDealBoCommand.BUTTON_NO).setEnabled(true);

				if (iapprovetype == ITaxInvoiceApproveType.DEAL_BEFORE_AUDIT){
					if ( ibillstate == IBillStatus.FREEZE || ibillstate == IBillStatus.FREE || ibillstate == IBillStatus.COMMIT){
						getButtonManager().getButton(nc.ui.so.taxinvoice.command.btDealBoCommand.BUTTON_NO).setEnabled(true);
						getButtonManager().getButton(nc.ui.so.taxinvoice.command.btRevDealBoCommand.BUTTON_NO).setEnabled(true);
					}else{
						getButtonManager().getButton(nc.ui.so.taxinvoice.command.btDealBoCommand.BUTTON_NO).setEnabled(false);
						getButtonManager().getButton(nc.ui.so.taxinvoice.command.btRevDealBoCommand.BUTTON_NO).setEnabled(true);
					}
				}else{
					if (ibillstate == IBillStatus.CHECKPASS || ibillstate == IBillStatus.CHECKGOING ){
						getButtonManager().getButton(nc.ui.so.taxinvoice.command.btDealBoCommand.BUTTON_NO).setEnabled(true);
						getButtonManager().getButton(nc.ui.so.taxinvoice.command.btRevDealBoCommand.BUTTON_NO).setEnabled(true);
					}else{
						getButtonManager().getButton(nc.ui.so.taxinvoice.command.btDealBoCommand.BUTTON_NO).setEnabled(false);
						getButtonManager().getButton(nc.ui.so.taxinvoice.command.btRevDealBoCommand.BUTTON_NO).setEnabled(true);
					}
					getButtonManager().getButton(nc.ui.so.taxinvoice.command.btDealBoCommand.BUTTON_NO).setEnabled(true);
					getButtonManager().getButton(nc.ui.so.taxinvoice.command.btRevDealBoCommand.BUTTON_NO).setEnabled(true);
				}
			}
			
			if(((ClientUI)getBillUI()).isSaveAndCommitTogether()) {
				if ( ibillstate == IBillStatus.COMMIT ){
					getButtonManager().getButton(nc.ui.trade.button.IBillButton.Edit).setEnabled(true);
					getButtonManager().getButton(nc.ui.trade.button.IBillButton.Del).setEnabled(true);
				}
			}
			if (checkDeal()) {
				getButtonManager().getButton(nc.ui.trade.button.IBillButton.Del).setEnabled(false); //如果已经核销，不能删除，修改
				getButtonManager().getButton(nc.ui.trade.button.IBillButton.Edit).setEnabled(false);
			}

			
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		getBillUI().updateButtons();

	}

	private boolean checkDeal() {
		// TODO Auto-generated method stub
		boolean ibhasdeal = false;

			try {
				TaxInvoiceVO taxinvvo = (TaxInvoiceVO) getBillCardPanelWrapper()
						.getBillVOFromUI();
				if (null == taxinvvo)
					return false;
				TaxInvoiceItemVO[] taxinvitemvo = (TaxInvoiceItemVO[]) taxinvvo
						.getChildrenVO();
				for (int i = 0; i < taxinvitemvo.length; i++) {
					UFDouble totaldealmny = taxinvitemvo[i].getNtotaldealmny()==null? UFDouble.ZERO_DBL: taxinvitemvo[i].getNtotaldealmny();
					UFDouble totaldealnmb = taxinvitemvo[i].getNtotaldealnum()==null? UFDouble.ZERO_DBL: taxinvitemvo[i].getNtotaldealnum();
					if (!totaldealmny.equals(UFDouble.ZERO_DBL) || !totaldealnmb.equals(UFDouble.ZERO_DBL)) {
						ibhasdeal = true;
						break;
					}
				}
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		return 	ibhasdeal;

	}

	private void onBoCommand(IButtonCommand command, ButtonObject bo){
	 
		if (getBillUI().getBillOperate() == IBillOperate.OP_ADD
				|| getBillUI().getBillOperate() == IBillOperate.OP_EDIT) {
			if (getBillCardPanelWrapper() != null)
				getBillCardPanelWrapper().getBillCardPanel().stopEditing();
		}
		try {
			command.execute(bo);
			setUserButtonStatus();  //更新按钮状态；
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

			if (checkAudit()) {
				super.onBoAudit();
			}
			else
				MessageDialog.showErrorDlg(getBillUI(), "错误", "发票审核策略设置与发票核销记录不符，请检查！");
		}

		private boolean checkAudit() {
		// TODO Auto-generated method stub

			boolean ibhasdeal = checkDeal();
			//从模板上获得核销策略（最好是从VO）
			int iapprovetype = Integer.parseInt(getBillCardPanelWrapper().getBillCardPanel().getHeadItem("iapprovetype").getValue());
			
			if (iapprovetype == ITaxInvoiceApproveType.DEAL_BEFORE_AUDIT) 
			{
				if (!ibhasdeal)
					return false;
				else
					return true;
					
			}else //审核后核销
			{
				if (!ibhasdeal)
					return true;
				else
					return false;
			}
	}

		public String getStrShowState() {
			return strShowState;
		}

		public void setStrShowState(String strShowState) {
			this.strShowState = strShowState;
		}

		@Override
		protected void onBoLineAdd() throws Exception {
			// TODO Auto-generated method stub
			super.onBoLineAdd();
			getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(new UFDouble(17.0), getBillCardPanelWrapper().getBillCardPanel()
					.getBillTable().getSelectedRow(), "ntaxrate") ; 

		}

		@Override
		protected void onBoCard() throws Exception {
			// TODO Auto-generated method stub
			super.onBoCard();
			  //user code 
			
//			setStrShowState(strPanelCard);
			  
			setUserButtonStatus();
		}

		@Override
		protected void onBoReturn() throws Exception {
			// TODO Auto-generated method stub
			super.onBoReturn();
			
//			setStrShowState(strPanelList);
			
			setUserButtonStatus();
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
			if (checkDeal() ) 
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