package nc.ui.ja.entityrecrpit;

import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ArrayProcessor;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.bsdelegate.BDBusinessDelegator;
import nc.ui.trade.manage.ManageEventHandler;
import nc.ui.pub.ClientEnvironment;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.trade.field.BillField;
import nc.vo.trade.pub.IBillStatus;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.linkoperate.ILinkQuery;
import nc.ui.pub.linkoperate.ILinkQueryData;


/**
 * <b> 在此处简要描述此类的功能 </b>
 *
 * <p>
 *     在此处添加此类的描述信息
 * </p>
 *
 *
 * @author author
 * @version tempProject version
 */
 public class ClientUI extends AbstractClientUI implements ILinkQuery{
       
       protected ManageEventHandler createEventHandler() {
		return new MyEventHandler(this, getUIControl());
	}
       
	public void setBodySpecialData(CircularlyAccessibleValueObject[] vos)
			throws Exception {}

	protected void setHeadSpecialData(CircularlyAccessibleValueObject vo,
			int intRow) throws Exception {}

	protected void setTotalHeadSpecialData(CircularlyAccessibleValueObject[] vos)
			throws Exception {	}

	protected void initSelfData() {	}

	public void setDefaultData() throws Exception {
		BillField fileDef = BillField.getInstance();
		String billtype = getUIControl().getBillType();
		String pkCorp = ClientEnvironment.getInstance().getCorporation().getPrimaryKey();
		
		String[] itemkeys = new String[]{
				fileDef.getField_Corp(),
				fileDef.getField_Operator(),
				fileDef.getField_Billtype(),
				fileDef.getField_BillStatus()
				};
		Object[] values = new Object[]{
				pkCorp,
				ClientEnvironment.getInstance().getUser().getPrimaryKey(),
				billtype,
				new Integer(IBillStatus.FREE).toString()
				};
		
		for(int i = 0; i < itemkeys.length; i++){
			BillItem item = null;
			item = getBillCardPanel().getHeadItem(itemkeys[i]);
			if(item == null)
				item = getBillCardPanel().getTailItem(itemkeys[i]);
			if(item != null)
				item.setValue(values[i]);
		}
	}

	@Override
	public void afterEdit(BillEditEvent e) {
		// TODO Auto-generated method stub
		String code=e.getKey().trim();
		int row=getBillCardPanel().getBillTable().getSelectedRow();
		IUAPQueryBS iQBS=(IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
		//存货编码带出--start--
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
				//存货分类pk
				getBillCardPanel().setBodyValueAt(olist[0], row, "pk_invcl");
				//存货分类名称
				getBillCardPanel().setBodyValueAt(olist[1], row, "invclassname");
				
			} catch (BusinessException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}//----end
		
		//自动计算税额，价税合计
		
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

	 public void doQueryAction(ILinkQueryData querydata)
	  {
	   System.out.println(querydata);
	   ILinkQuery app = (ILinkQuery)getBillCardWrapper();
	      if (app == null)
	        return;
	      app.doQueryAction(querydata);
	  }
	


}
