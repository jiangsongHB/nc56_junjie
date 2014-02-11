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
			  	// TODO ��ʵ�ִ˰�ť�¼����߼�
	  	//throw new BusinessRuntimeException("δʵ�ִ˰�ť���¼������߼�,�뵽"+this.getClass().getName()+"�в���ҵ�����߼�����!");
		((ClientUI)this.getBillUI()).showVerifyPanel();  //��������

	}
	
	 public ButtonVO getButtonVO(){
	 
	  if(btnVo == null) {
	  	
	  	btnVo = new ButtonVO();
		btnVo.setBtnNo(BUTTON_NO);
		btnVo.setBtnCode("btDeal");
		btnVo.setBtnName("��������");
		btnVo.setBtnChinaName("��������");
	 
		btnVo.setChildAry(new int[]{
		                });
		   
	 	 }  
	  return btnVo;
 	}
		
}
 