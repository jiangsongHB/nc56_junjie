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

	private void onDefReturn() throws Exception {
		// TODO Auto-generated method stub
		
		getBillUI().setBillOperate(IBillOperate.OP_NOTEDIT);
		SFClientUtil.closeFuncWindow("JAH102");
					
	}

	protected void onDefQuery() {
		// TODO Auto-generated method stub
		System.out.println(this.getBillUI().getBillOperate());
	}
	//核销
	protected void onDefCheck() throws BusinessException {
		// TODO Auto-generated method stub
		if(this.getBillUI().getBillOperate()==2){
			this.getBillUI().showWarningMessage("本次核销已完成了");
			return;
		}
		int rows=getBillCardPanelWrapper().getBillCardPanel().getBillTable().getRowCount();
		Object money=getBillCardPanelWrapper().getBillCardPanel().getHeadItem("money").getValue();//(表体本次计算)核销总金额
		
		int sum=0;
		Double checkmoney=0d;//实际核销总金额
		ArrayList pklist=new ArrayList();//所选核销记录的主键list
		ArrayList moneylist=new ArrayList();//所选核销的金额list
		
		for(int i=0;i<rows;i++){
			Boolean ischoice=false;
			Object obj= getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i, "ischoice");
			if(obj!=null){
				
				
				ischoice=(Boolean) obj?true:false;
			}
			
			//选择标识是否勾选
			if(ischoice){		
				sum++;				
				Object cj=getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i, "checkamount");//核销金额
				Object pk=getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i, "def10");  //系统发票pk
				
				pklist.add(pk);
				moneylist.add(cj);
				checkmoney=checkmoney+ new Double(cj.toString());								
			}else{
				continue;
			}			
		}
		
		if(sum==0){
			this.getBillUI().showWarningMessage("请先选择要核销的记录");
			return;
		}
		if(checkmoney>new Double(money.toString()==null?"0":money.toString())){
			this.getBillUI().showWarningMessage("核销总金额大于本次结算金额，请检查");			
			return;
		}
		if(checkmoney==0d){
			this.getBillUI().showWarningMessage("本次结算金额为0，无法核销，请检查");			
			return;
		}
		
		//-----
		Object pk=getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(0, "def9");//实体发票主键
		Object pkb=getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(0, "def8");//实体发票明细主键
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
			CkVO.setAttributeValue("def3", pkb);
		
			CkVO.setAttributeValue("def4", pklist.get(i));//系统发票pk
			
			//核销金额
			CkVO.setAttributeValue("tax", moneylist.get(i));
			
			CkVO.setPk_corp(ClientEnvironment.getInstance().getCorporation().getPk_corp());//公司
			CkVO.setDef29(ClientEnvironment.getInstance().getUser().getPrimaryKey());//核销人
			CkVO.setDef30(ClientEnvironment.getInstance().getDate().toString());//核销日期
			
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
		this.getBillUI().showHintMessage("核销完成!");
	}
	//分配
	protected void onDefAllot() {
		// TODO Auto-generated method stub
		int rows=getBillCardPanelWrapper().getBillCardPanel().getBillTable().getRowCount();
		//--全消
		for (int i = 0; i < rows; i++) {
			getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt("N", i, "ischoice");
			getBillCardPanelWrapper().getBillCardPanel().setCellEditable(i, "checkamount", false);
			getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(null, i, "checkamount");
		}
		//---
		Object money=getBillCardPanelWrapper().getBillCardPanel().getHeadItem("money").getValue();
		
		Double dmoney=new Double(money.toString()==null?"0":money.toString());
		
		for(int i=0;i<rows;i++){
			Object tempmoney=getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt( i, "tempmoney");
			Double dtempmoney=new Double(tempmoney.toString());
			if(dtempmoney<=0d){
				continue;
			}
			if(dmoney>dtempmoney){
				getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(dtempmoney, i, "checkamount");				
			}else{
				getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(dmoney, i, "checkamount");				
			}
			getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt("Y", i, "ischoice");
			dmoney=dmoney-dtempmoney;
			if(dmoney<=0d){
				break;
			}			
		}
		
	}
	
}