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
  *������һ�������࣬��ҪĿ�������ɰ�ť�¼�����Ŀ��
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
	//����
	protected void onDefReturn() {	
		//getBillCardPanelWrapper().getBillCardPanel().
		int i=getBillCardPanelWrapper().getBillCardPanel().getBillData().getBillstatus();
		if(i!=0){
			int is=getBillUI().showYesNoMessage("δ�������ݣ��Ƿ񱣴棿");
			if(is==4){
				//����
			}else{
				//ȡ��
			}
		}
		//SFClientUtil.openLinkedADDDialog("JAH101", this.getBillUI(), null);
	}
	//����
	protected void onDefQuery() throws BusinessException {	
		int row=getBillCardPanelWrapper().getBillCardPanel().getBillTable().getSelectedRow();
		if(row<0){
			getBillUI().showWarningMessage("����ѡ������");
			return;
		}
		Object invcl=getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(row, "invclassname");//�������
		Object invcode=getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(row, "invcode");//�������
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
	//����
	protected void onDefCheck() throws BusinessException {	
		int row=getBillCardPanelWrapper().getBillCardPanel().getBillTable().getSelectedRow();
		if(row<0){
			getBillUI().showWarningMessage("����ѡ������");
			return;
		}
		//��ȡѡ���У��������������Ϣ
		Object invcl=getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(row, "invclassname");//�������
		Object invcode=getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(row, "invcode");//�������
		getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(row, "price");//����
		getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(row, "taxamount");//��˰�ϼ�
		
		String sql="";
		IUAPQueryBS iquery = NCLocator.getInstance().lookup(IUAPQueryBS.class);
		sql="select pk_entity_receipt from ja_entity_receipt_b where invclassname='"+invcl+"' and invcode='"+invcode+"' ";
		Object obj=iquery.executeQuery(sql, new ColumnProcessor());
		if(obj!=null){
			sql="select vbillstatus from ja_entity_receipt where pk_entity_receipt='"+obj+"' ";
			Object vbillstatus=iquery.executeQuery(sql, new ColumnProcessor());
			//��Ʊ�Ƿ����
			if(Integer.parseInt(vbillstatus.toString())==1){
				//���� 
			}else{
				getBillUI().showWarningMessage("��ط�Ʊδ���ͨ�������ܺ���");
				return;
			}
		}else{
			getBillUI().showWarningMessage("û����ط�Ʊ����������¼��");
			return;
		}
	}
	//����
	protected void onDefAllot() {}
	
		   	
	
}