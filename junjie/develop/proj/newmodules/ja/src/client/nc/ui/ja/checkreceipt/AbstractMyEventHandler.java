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

	private void onDefReturn() {
		// TODO Auto-generated method stub
		SFClientUtil.closeFuncWindow("JAH102");
		
	}

	private void onDefQuery() {
		// TODO Auto-generated method stub
		
	}
	//����
	protected void onDefCheck() throws BusinessException {
		// TODO Auto-generated method stub
		int rows=getBillCardPanelWrapper().getBillCardPanel().getBillTable().getRowCount();
		Object money=getBillCardPanelWrapper().getBillCardPanel().getHeadItem("money").getValue();//(���屾�μ���)�����ܽ��
		Boolean ischoice=false;
		int sum=0;
		Double checkmoney=0d;//ʵ�ʺ����ܽ��
		ArrayList pklist=new ArrayList();//��ѡ������¼������list
		ArrayList moneylist=new ArrayList();//��ѡ�����Ľ��list
		
		for(int i=0;i<rows;i++){
			Object obj= getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i, "ischoice");
			ischoice=obj==null?false:true;
			
			//ѡ���ʶ�Ƿ�ѡ
			if(ischoice){		
				sum++;				
				Object cj=getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i, "checkamount");//������
				Object pk=getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i, "def10");  //ʵ�巢Ʊpk
				
				pklist.add(pk);
				moneylist.add(cj);
				checkmoney=checkmoney+ new Double(cj.toString());								
			}else{
				continue;
			}			
		}
		
		if(checkmoney>new Double(money.toString())){
			this.getBillUI().showWarningMessage("�����ܽ����ڱ��ν��������");
			checkmoney=0d;
			return;
		}
		if(sum==0){
			this.getBillUI().showWarningMessage("����ѡ��Ҫ�����ļ�¼");
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
			//�������
			CkVO.setAttributeValue("tax", moneylist.get(i));
			
			CkVO.setPk_corp(ClientEnvironment.getInstance().getCorporation().getPk_corp());//��˾
			
			//��д����
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
	//����
	protected void onDefAllot() {
		// TODO Auto-generated method stub
		Object money=getBillCardPanelWrapper().getBillCardPanel().getHeadItem("money").getValue();
		getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(money, 0, "checkamount");
		getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt("Y", 0, "ischoice");
		
		
	}
	
		   	
	
}