package nc.ui.ja.entityreceiptck;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ArrayProcessor;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.trade.bill.ICardController;
import nc.ui.trade.card.BillCardUI;
import nc.ui.trade.card.CardEventHandler;
import nc.vo.pub.BusinessException;


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

public class ClientUI extends AbstractClientUI{
	
	
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

	}

	public void setDefaultData() throws Exception {
	}
	
	/**
	 * �޸Ĵ˷������Ӻ�̨У��
	 */
	public Object getUserObject() {
		return null;
	}

	@Override
	public void afterEdit(BillEditEvent e) {
		// TODO Auto-generated method stub
		String code=e.getKey().trim();
		int row=getBillCardPanel().getBillTable().getSelectedRow();
		IUAPQueryBS iQBS=(IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
		//����������--start--
		if(code.equals("invcode")){
			Object obj=getBillCardPanel().getBodyValueAt(row, "pk_invdoc");
			
			String sql="select bd_invcl.pk_invcl,bd_invcl.invclassname from bd_invcl " +
					"inner join bd_invbasdoc on bd_invcl.invclassname=bd_invbasdoc.graphid " +
					"where bd_invbasdoc.pk_invbasdoc='"+obj+"'";
			try {
				Object[] olist=(Object[]) iQBS.executeQuery(sql, new ArrayProcessor());
				if(olist==null){
					getBillCardPanel().setBodyValueAt(null, row, "pk_invcl");					
					getBillCardPanel().setBodyValueAt(null, row, "invclassname");
					return;
				}
				//�������pk
				getBillCardPanel().setBodyValueAt(olist[0], row, "pk_invcl");
				//�����������
				getBillCardPanel().setBodyValueAt(olist[1], row, "invclassname");
				
			} catch (BusinessException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}//----end
		
		//�Զ�����˰���˰�ϼ�
		
		String formula1="tax->mul(zeroifnull(count) ,mul(zeroifnull(price),zeroifnull(taxrate)))";
		String formula2="amount->mul(zeroifnull(count) ,zeroifnull(price))";
		String formula3="taxamount->add(tax ,amount)";
		if("price".equals(code)||"count".equals(code)){
			String[] formulas={formula2,formula1,formula3};
			getBillCardPanel().execBodyFormulas(row, formulas);
		}
		if("taxrate".equals(code)){
			String[] formulas={formula1,formula3};
			getBillCardPanel().execBodyFormulas(row, formulas);
		}
	}
	
	
}
