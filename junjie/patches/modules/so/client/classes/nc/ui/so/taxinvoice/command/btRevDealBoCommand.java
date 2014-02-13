package nc.ui.so.taxinvoice.command;

import nc.ui.so.taxinvoice.ClientUI;
import nc.ui.trade.base.AbstractBillUI;
import nc.ui.trade.controller.IControllerBase;
import nc.vo.pub.BusinessRuntimeException;
import nc.ui.trade.bocommand.AbstractUserDefBoCommand;
import nc.vo.trade.button.ButtonVO; 
import nc.ui.pub.ButtonObject;

public class btRevDealBoCommand extends AbstractUserDefBoCommand {

	public final static int BUTTON_NO = 103;
	private ButtonVO btnVo;

	public btRevDealBoCommand(AbstractBillUI billUI, IControllerBase control) {
		super(billUI, control);
		
	}

	public void execute(ButtonObject bo) throws Exception {
			  	// TODO 请实现此按钮事件的逻辑
	  	//throw new BusinessRuntimeException("未实现此按钮的事件处理逻辑,请到"+this.getClass().getName()+"中补充业务处理逻辑代码!");
		((ClientUI)this.getBillUI()).showVerifyQryPanel();  //核销查询
		this.getBillUI().getBufferData().refresh(); 
	}
	
	 public ButtonVO getButtonVO(){
	 
	  if(btnVo == null) {
	  	
	  	btnVo = new ButtonVO();
		btnVo.setBtnNo(BUTTON_NO);
		btnVo.setBtnCode("btRevDeal");
		btnVo.setBtnName("核销查询");
		btnVo.setBtnChinaName("核销查询");
	 
		btnVo.setChildAry(new int[]{
		                });
		   
	 	 }  
	  return btnVo;
 	}
 	
}
 