package nc.ui.so.taxinvoice.command;

import nc.ui.trade.base.AbstractBillUI;
import nc.ui.trade.controller.IControllerBase;
import nc.vo.pub.BusinessRuntimeException;
import nc.ui.trade.bocommand.AbstractUserDefBoCommand;
import nc.vo.trade.button.ButtonVO; 
import nc.ui.pub.ButtonObject;

public class btDealGpBoCommand extends AbstractUserDefBoCommand {

	public final static int BUTTON_NO = 101;
	private ButtonVO btnVo;

	public btDealGpBoCommand(AbstractBillUI billUI, IControllerBase control) {
		super(billUI, control);
	}

	public void execute(ButtonObject bo) throws Exception {
			  	// TODO 请实现此按钮事件的逻辑
	  	throw new BusinessRuntimeException("未实现此按钮的事件处理逻辑,请到"+this.getClass().getName()+"中补充业务处理逻辑代码!");
	}
	
	 public ButtonVO getButtonVO(){
	 
	  if(btnVo == null) {
	  	
	  	btnVo = new ButtonVO();
		btnVo.setBtnNo(BUTTON_NO);
		btnVo.setBtnCode("btDealGp");
		btnVo.setBtnName("核销");
		btnVo.setBtnChinaName("核销");
	 
		btnVo.setChildAry(new int[]{
		           btDealBoCommand.BUTTON_NO,
		           btRevDealBoCommand.BUTTON_NO
		                });
		   
	 	 }  
	  return btnVo;
 	}
 	
}
 