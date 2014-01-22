package nc.ui.ja.entityrecrpit;

import nc.ui.ja.buttons.ICustomizeButton;
import nc.ui.pub.linkoperate.ILinkAddData;
import nc.ui.pub.msg.PfLinkData;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.ui.trade.manage.ManageEventHandler;
import nc.ui.uap.sf.SFClientUtil;
import nc.vo.cmp.applaybill.AggApplyBillVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.CircularlyAccessibleValueObject;

/**
  *
  *该类是一个抽象类，主要目的是生成按钮事件处理的框架
  *@author author
  *@version tempProject version
  */
  
  public class AbstractMyEventHandler 
                                          extends ManageEventHandler{

        public AbstractMyEventHandler(BillManageUI billUI, IControllerBase control){
		super(billUI,control);		
	}
	
	protected void onBoElse(int intBtn) throws Exception {
		switch(intBtn){
		
		  
		   case  ICustomizeButton.DefCheck :
	      onDefCheck(); 
	      break;
		  
		 
			
	}
		     	}

	protected void onDefCheck() throws Exception {
		// TODO Auto-generated method stub
		AggregatedValueObject aggvo= getBillCardPanelWrapper().getBillVOFromUI();
		CircularlyAccessibleValueObject[] vos=getBillCardPanelWrapper().getSelectedBodyVOs();
		if(vos!=null){
			
			aggvo.setChildrenVO(vos);
		}
		
		PfLinkData data = new PfLinkData();
 		 data.setUserObject(aggvo);
		SFClientUtil.openLinkedADDDialog("JAH102", this.getBillUI(), data);
	}
	
		   	
	
}