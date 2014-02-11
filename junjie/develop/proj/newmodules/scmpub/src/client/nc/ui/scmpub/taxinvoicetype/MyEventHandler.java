package nc.ui.scmpub.taxinvoicetype;

import java.sql.SQLException;
import java.util.Map;
import java.util.HashMap;
import java.util.List;

import nc.bs.logging.Logger;
import nc.ui.pub.ButtonObject;
import nc.ui.trade.bocommand.IButtonCommand;
import nc.ui.trade.bocommand.MetaDataPrintBoCommand;
import nc.ui.trade.bocommand.IUserDefButtonCommand; 
import nc.ui.scmpub.taxinvoicetype.command.SaveBoCommand;
import nc.ui.trade.button.IBillButton;
import nc.ui.trade.manage.ManageEventHandler;
import nc.vo.pub.BusinessException;
import nc.ui.trade.base.IBillOperate;

import nc.ui.trade.controller.IControllerBase;
import nc.vo.trade.button.ButtonVO;

/** 
  *
  *该类用来进行按钮事件处理, 扩展按钮的事件响应应该实现IUIButtonCommand并进行注册
  *@author author
  *@version tempProject version
  */
  
public class MyEventHandler 
                                          extends ManageEventHandler{
	
	private Map<Integer, IButtonCommand> commands = new HashMap<Integer, IButtonCommand>(); 

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
	 }
	 
	 private void onBoCommand(IButtonCommand command, ButtonObject bo){
	 
		if (getBillUI().getBillOperate() == IBillOperate.OP_ADD
				|| getBillUI().getBillOperate() == IBillOperate.OP_EDIT) {
			if (getBillCardPanelWrapper() != null)
				getBillCardPanelWrapper().getBillCardPanel().stopEditing();
		}
		try {
			command.execute(bo);
		} catch (BusinessException ex) {
			onBusinessException(ex);
		} catch (SQLException ex) {
			getBillUI().showErrorMessage(ex.getMessage());
		} catch (Exception e) {
			getBillUI().showErrorMessage(e.getMessage());
			Logger.error(e.getMessage());
		}
	}


}