package nc.ui.so.taxinvoice.command;

import com.cn.bup.CilShow;

import nc.bs.logging.Logger;
import nc.ui.so.taxinvoice.ClientUI;
import nc.ui.so.taxinvoice.VerifyPanel;
import nc.ui.trade.base.AbstractBillUI;
import nc.ui.trade.controller.IControllerBase;
import nc.vo.pub.BusinessException;
import nc.vo.pub.BusinessRuntimeException;
import nc.ui.trade.bocommand.AbstractUserDefBoCommand;
import nc.vo.trade.button.ButtonVO; 
import nc.ui.arap.actions.CounterActAction;
import nc.ui.arap.verify.VerifyInTimePanel;
import nc.ui.ep.dj.DjPanel;
import nc.ui.pub.ButtonObject;

public class btDealBoCommand extends AbstractUserDefBoCommand {

	public final static int BUTTON_NO = 102;
	private ButtonVO btnVo;

	public btDealBoCommand(AbstractBillUI billUI, IControllerBase control) {
		super(billUI, control);
	}

	public void execute(ButtonObject bo) throws Exception {
			  	// TODO 请实现此按钮事件的逻辑
	  	//throw new BusinessRuntimeException("未实现此按钮的事件处理逻辑,请到"+this.getClass().getName()+"中补充业务处理逻辑代码!");
		((ClientUI)this.getBillUI()).showVerifyPanel();  //核销处理

	}
	
	 public ButtonVO getButtonVO(){
	 
	  if(btnVo == null) {
	  	
	  	btnVo = new ButtonVO();
		btnVo.setBtnNo(BUTTON_NO);
		btnVo.setBtnCode("btDeal");
		btnVo.setBtnName("核销处理");
		btnVo.setBtnChinaName("核销处理");
	 
		btnVo.setChildAry(new int[]{
		                });
		   
	 	 }  
	  return btnVo;
 	}
		
}
 