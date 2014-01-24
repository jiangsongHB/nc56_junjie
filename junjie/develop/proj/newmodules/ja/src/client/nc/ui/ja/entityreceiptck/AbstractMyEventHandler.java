package nc.ui.ja.entityreceiptck;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.ui.arap.pub.LinkQuery;
import nc.ui.ja.buttons.ICustomizeButton;
import nc.ui.pub.linkoperate.ILinkAdd;
import nc.ui.pub.linkoperate.ILinkQueryData;
import nc.ui.sm.login.ShowDialog;
import nc.ui.trade.bill.ICardController;
import nc.ui.trade.card.BillCardUI;
import nc.ui.trade.card.CardEventHandler;
import nc.ui.uap.sf.SFClientUtil;
import nc.vo.pub.BusinessException;

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
	//返回
	protected void onDefReturn() {	
		//getBillCardPanelWrapper().getBillCardPanel().
		int i=getBillCardPanelWrapper().getBillCardPanel().getBillData().getBillstatus();
		if(i!=0){
			int is=getBillUI().showYesNoMessage("未保存数据，是否保存？");
			if(is==4){
				//保存
			}else{
				//取消
			}
		}
		//SFClientUtil.openLinkedADDDialog("JAH101", this.getBillUI(), null);
	}
	//联查
	protected void onDefQuery() throws BusinessException {	
		int row=getBillCardPanelWrapper().getBillCardPanel().getBillTable().getSelectedRow();
		if(row<0){
			getBillUI().showWarningMessage("请先选择数据");
			return;
		}
		Object invcl=getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(row, "invclassname");//存货分类
		Object invcode=getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(row, "invcode");//存货编码
		String sql="";
		IUAPQueryBS iquery = NCLocator.getInstance().lookup(IUAPQueryBS.class);
		sql="select pk_entity_receipt from ja_entity_receipt_b where invclassname='"+invcl+"' and invcode='"+invcode+"' ";
		Object obj=iquery.executeQuery(sql, new ColumnProcessor());
		if(obj!=null){
			String[] s={(String) obj};
			LinkQuery data =new LinkQuery(s);
			
			SFClientUtil.openLinkedQueryDialog("JAH101", this.getBillUI(), data);
			
		}
	}
	//核销
	protected void onDefCheck() throws BusinessException {	
		int row=getBillCardPanelWrapper().getBillCardPanel().getBillTable().getSelectedRow();
		if(row<0){
			getBillUI().showWarningMessage("请先选择数据");
			return;
		}
		//获取选中行，核销所需相关信息
		Object invcl=getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(row, "invclassname");//存货分类
		Object invcode=getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(row, "invcode");//存货编码
		getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(row, "price");//单价
		getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(row, "taxamount");//价税合计
		
		String sql="";
		IUAPQueryBS iquery = NCLocator.getInstance().lookup(IUAPQueryBS.class);
		sql="select pk_entity_receipt from ja_entity_receipt_b where invclassname='"+invcl+"' and invcode='"+invcode+"' ";
		Object obj=iquery.executeQuery(sql, new ColumnProcessor());
		if(obj!=null){
			sql="select vbillstatus from ja_entity_receipt where pk_entity_receipt='"+obj+"' ";
			Object vbillstatus=iquery.executeQuery(sql, new ColumnProcessor());
			//发票是否审核
			if(Integer.parseInt(vbillstatus.toString())==1){
				//核销 
			}else{
				getBillUI().showWarningMessage("相关发票未审核通过，不能核销");
				return;
			}
		}else{
			getBillUI().showWarningMessage("没有相关发票，请检查数据录入");
			return;
		}
	}
	//分配
	protected void onDefAllot() {}
	
		   	
	
}