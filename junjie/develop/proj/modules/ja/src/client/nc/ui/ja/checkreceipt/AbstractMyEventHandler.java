package nc.ui.ja.checkreceipt;

import java.util.ArrayList;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ArrayListProcessor;
import nc.jdbc.framework.processor.ArrayProcessor;
import nc.ui.ja.buttons.ICustomizeButton;
import nc.ui.pr.pray.SetBillVOforReviseUI;
import nc.ui.pub.ClientEnvironment;
import nc.ui.trade.base.IBillOperate;
import nc.ui.trade.bill.ICardController;
import nc.ui.trade.card.BillCardUI;
import nc.ui.trade.card.CardEventHandler;
import nc.ui.uap.sf.SFClientUtil;
import nc.vo.ja.entityreceiptck.JaEntityReceiptCkVO;
import nc.vo.ja.pub.itf.IEntityReceipt;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDouble;

/**
  *
  *该类是一个抽象类，主要目的是生成按钮事件处理的框架
  *@author author
  *@version tempProject version
  */
  
    
public abstract class AbstractMyEventHandler 
                                          extends CardEventHandler{

 public AbstractMyEventHandler(BillCardUI billUI,ICardController control){
		super(billUI,control);		
	}
	
	protected void onBoElse(int intBtn) throws Exception {
		switch(intBtn){
		
		   case  ICustomizeButton.DefAllot :
	      onDefAllot(); 
	      break;
		   case  ICustomizeButton.DefCheck :
	      onDefCheck(); 
	      break;
		   case  ICustomizeButton.DefQuery :
	      onDefQuery(); 
	      break;
		   case  ICustomizeButton.DefReturn :
	      onDefReturn(); 
	      break;
			
	}
	     	}

	private void onDefReturn() {
		// TODO Auto-generated method stub
		SFClientUtil.closeFuncWindow("JAH102");
		
	}

	private void onDefQuery() {
		// TODO Auto-generated method stub
		
	}
	//核销
	protected void onDefCheck() throws BusinessException {
		// TODO Auto-generated method stub
		int rows=getBillCardPanelWrapper().getBillCardPanel().getBillTable().getRowCount();
		Object money=getBillCardPanelWrapper().getBillCardPanel().getHeadItem("money").getValue();//(表体本次计算)核算总金额
		Boolean ischoice=false;
		int sum=0;
		Double checkmoney=0d;//实际核算总金额
		ArrayList pklist=new ArrayList();//所选核销记录的主键list
		ArrayList moneylist=new ArrayList();//所选核销的金额list
		
		for(int i=0;i<rows;i++){
			Object obj= getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i, "ischoice");
			ischoice=obj==null?false:true;
			
			//选择标识是否勾选
			if(ischoice){		
				sum++;				
				Object cj=getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i, "checkamount");//核算金额
				Object pk=getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i, "def10");  //实体发票pk
				
				pklist.add(pk);
				moneylist.add(cj);
				checkmoney=checkmoney+ new Double(cj.toString());								
			}else{
				continue;
			}			
		}
		
		if(checkmoney>new Double(money.toString())){
			this.getBillUI().showWarningMessage("核算总金额大于本次结算金额，请检查");
			checkmoney=0d;
			return;
		}
		if(sum==0){
			this.getBillUI().showWarningMessage("请先选择要核销的记录");
			return;
		}
		//-----
		Object pk=getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(0, "def9");
		IUAPQueryBS iquery = NCLocator.getInstance().lookup(IUAPQueryBS.class);
		for(int i=0;i<pklist.size();i++){
			
			String sql="select * from v_ja_check where def10='"+pklist.get(i)+"' ";
			
			Object[] objs=(Object[]) iquery.executeQuery(sql, new ArrayProcessor());
			
			JaEntityReceiptCkVO CkVO=new JaEntityReceiptCkVO();
			CkVO.setAttributeValue("pk_invdoc", objs[4]);
			CkVO.setAttributeValue("pk_invcl", objs[6]);
			CkVO.setAttributeValue("count", new UFDouble((objs[13]==null?"0":objs[13]).toString()));
			CkVO.setAttributeValue("price", new UFDouble(objs[14].toString()));
			CkVO.setAttributeValue("amount", new UFDouble(objs[15].toString()));
			CkVO.setAttributeValue("taxamount", new UFDouble(objs[18].toString()));
			CkVO.setAttributeValue("def1", objs[20]);
			CkVO.setAttributeValue("def2", objs[21]);
			CkVO.setAttributeValue("pk_entity_receipt", pk);
			//核销金额
			CkVO.setAttributeValue("tax", moneylist.get(i));
			
			CkVO.setPk_corp(ClientEnvironment.getInstance().getCorporation().getPk_corp());//公司
			
			//回写关联
			IEntityReceipt ier=NCLocator.getInstance().lookup(IEntityReceipt.class);
			try {
				ier.onCheck(CkVO);
				getBillUI().setBillOperate(IBillOperate.OP_NOTEDIT);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	//分配
	protected void onDefAllot() {
		// TODO Auto-generated method stub
		Object money=getBillCardPanelWrapper().getBillCardPanel().getHeadItem("money").getValue();
		getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(money, 0, "checkamount");
		getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt("Y", 0, "ischoice");
		
		
	}
	
		   	
	
}