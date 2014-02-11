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
			  	// TODO ��ʵ�ִ˰�ť�¼����߼�
	  	throw new BusinessRuntimeException("δʵ�ִ˰�ť���¼������߼�,�뵽"+this.getClass().getName()+"�в���ҵ�����߼�����!");
	}
	
	 public ButtonVO getButtonVO(){
	 
	  if(btnVo == null) {
	  	
	  	btnVo = new ButtonVO();
		btnVo.setBtnNo(BUTTON_NO);
		btnVo.setBtnCode("btDealGp");
		btnVo.setBtnName("����");
		btnVo.setBtnChinaName("����");
	 
		btnVo.setChildAry(new int[]{
		           btDealBoCommand.BUTTON_NO,
		           btRevDealBoCommand.BUTTON_NO
		                });
		   
	 	 }  
	  return btnVo;
 	}
 	
}
 