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
			  	// TODO ��ʵ�ִ˰�ť�¼����߼�
	  	//throw new BusinessRuntimeException("δʵ�ִ˰�ť���¼������߼�,�뵽"+this.getClass().getName()+"�в���ҵ�����߼�����!");
		((ClientUI)this.getBillUI()).showVerifyQryPanel();  //������ѯ
		this.getBillUI().getBufferData().refresh(); 
	}
	
	 public ButtonVO getButtonVO(){
	 
	  if(btnVo == null) {
	  	
	  	btnVo = new ButtonVO();
		btnVo.setBtnNo(BUTTON_NO);
		btnVo.setBtnCode("btRevDeal");
		btnVo.setBtnName("������ѯ");
		btnVo.setBtnChinaName("������ѯ");
	 
		btnVo.setChildAry(new int[]{
		                });
		   
	 	 }  
	  return btnVo;
 	}
 	
}
 