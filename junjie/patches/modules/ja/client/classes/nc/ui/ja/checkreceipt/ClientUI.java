package nc.ui.ja.checkreceipt;

import java.util.ArrayList;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ArrayListProcessor;
import nc.jdbc.framework.processor.ArrayProcessor;
import nc.jdbc.framework.processor.BeanProcessor;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.linkoperate.ILinkAdd;
import nc.ui.pub.linkoperate.ILinkAddData;
import nc.ui.trade.base.IBillOperate;
import nc.ui.trade.bill.ICardController;
import nc.ui.trade.card.BillCardUI;
import nc.ui.trade.card.CardEventHandler;
import nc.vo.ja.checkreceipt.MyBillVO;
import nc.vo.ja.checkreceipt.VJaCheckVO;
import nc.vo.ja.entityrecrpit.JaEntityReceiptBVO;
import nc.vo.ja.entityrecrpit.JaEntityReceiptVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.bill.BillRendererVO;
import nc.vo.pub.lang.UFDouble;


/**
 * <b> �ڴ˴���Ҫ��������Ĺ��� </b>
 *
 * <p>
 *     �ڴ˴���Ӵ����������Ϣ
 * </p>
 *
 *
 * @author author
 * @version tempProject version
 */

public class ClientUI extends AbstractClientUI implements ILinkAdd{
	
	
	protected CardEventHandler createEventHandler() {
	    return new MyEventHandler(this, getUIControl());
          }

	public String getRefBillType() {
		return null;
	}
	
	/**
	 * �޸Ĵ˷�����ʼ��ģ��ؼ�����
	 */
	protected void initSelfData() {
		getBillCardPanel().setAutoExecHeadEditFormula(true);
		getBillCardPanel().execHeadEditFormulas();
		//��������Ϊ��ʱ����ʾ��
		BillRendererVO voCell = new BillRendererVO();
		voCell.setShowZeroLikeNull(false);
		getBillCardPanel().getBodyPanel().setShowFlags(voCell);
	}

	public void setDefaultData() throws Exception {
	}
	
	/**
	 * �޸Ĵ˷������Ӻ�̨У��
	 */
	public Object getUserObject() {
		return null;
	}

	public void doAddAction(ILinkAddData data) {
		// TODO Auto-generated method stub
		MyBillVO  billVO =new MyBillVO();
		IUAPQueryBS iquery =NCLocator.getInstance().lookup(IUAPQueryBS.class);
		String sql="";
		AggregatedValueObject aggvo=(AggregatedValueObject) data.getUserObject();
		JaEntityReceiptVO vo= (JaEntityReceiptVO) aggvo.getParentVO();
		CircularlyAccessibleValueObject[] vobs= aggvo.getChildrenVO();
		JaEntityReceiptBVO vob= (JaEntityReceiptBVO) vobs[0];
		//VJaCheckVO[] cvos;
		try {
			sql="select busstype,pk_custreceipt from ja_entity_receipt where pk_entity_receipt='"+vo.getPrimaryKey()+"' ";
			Object[] obj=(Object[]) iquery.executeQuery(sql, new ArrayProcessor());
			sql="select * from v_ja_check where checktype='"+obj[0]+"' and def1='"+obj[1]+"' and pk_invdoc='"+vob.getPk_invdoc()+"' ";
			//System.out.println(sql);
			ArrayList volist=(ArrayList) iquery.executeQuery(sql, new ArrayListProcessor());
			
			VJaCheckVO[] cvo=new VJaCheckVO[volist.size()];
			for(int i=0;i<volist.size();i++){
				Object[] objs= (Object[]) volist.get(i);
				VJaCheckVO v=new VJaCheckVO();
				
				v.setAttributeValue("checkid", objs[0]);
				
				//v.setAttributeValue("checkamount",new UFDouble(objs[18].toString()));
				v.setAttributeValue("checktype", objs[2]);
				v.setAttributeValue("pk_invdoc", objs[4]);
				v.setAttributeValue("pk_invcl", objs[6]);
				v.setAttributeValue("count", new UFDouble((objs[13]==null?"0":objs[13]).toString()));
				v.setAttributeValue("price", new UFDouble(objs[14].toString()));
				v.setAttributeValue("amount", new UFDouble(objs[15].toString()));
				v.setAttributeValue("taxamount", new UFDouble(objs[18].toString()));
				v.setAttributeValue("def1", objs[20]);
				v.setAttributeValue("def2", objs[21]);
				v.setAttributeValue("def9", vo.getPrimaryKey());//ʵ�巢Ʊ����
				v.setAttributeValue("def10", objs[29]);//
				v.setAttributeValue("def8", vob.getPrimaryKey());//ʵ�巢Ʊ��ϸ����
				cvo[i]=v;
			}
			
			billVO.setChildrenVO(cvo);
			setBillOperate(IBillOperate.OP_ADD); 
			getBillCardPanel().setBillValueVO(billVO);
			getBillCardPanel().getBillModel().execLoadFormula();
			getBillCardPanel().updateUI();
			getBillCardPanel().updateValue();
			UFDouble d=vob.totalamount==null?new UFDouble(0):vob.totalamount;
			getBillCardPanel().setHeadItem("money", vob.taxamount.sub(d));
			System.out.println(d);
			getBillCardPanel().setHeadItem("yemoney", vob.taxamount.sub(d));
			//getbillc
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void afterEdit(BillEditEvent e) {
		// TODO Auto-generated method stub
		String code=e.getKey().trim();
		int row=getBillCardPanel().getBillTable().getSelectedRow();
		if("ischoice".equals(code)){
			Object obj=getBillCardPanel().getBodyValueAt(row, "ischoice");
			if((Boolean) obj?true:false){
				getBillCardPanel().getBodyItem("checkamount").setEdit(true);
				getBillCardPanel().setBodyValueAt(getBillCardPanel().getBodyValueAt(row, "taxamount"), row, "checkamount");
			}else{
				getBillCardPanel().setBodyValueAt(null, row, "checkamount");
			}
		}
		
		if("money".equals(code)){
			Double money= new Double(getBillCardPanel().getHeadItem("money").getValue());
			Double yemoney=new Double(getBillCardPanel().getHeadItem("yemoney").getValue());
			if(money>yemoney){
				getBillCardPanel().setHeadItem("money",yemoney);
			}
		}
	}
	
	
}
