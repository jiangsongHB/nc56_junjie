package nc.ui.so.taxinvoice;

import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.bsdelegate.BusinessDelegator;
import nc.ui.pub.linkoperate.*;
import nc.vo.pub.AggregatedValueObject;
import nc.ui.trade.base.IBillOperate;
import nc.ui.trade.bill.BillTemplateWrapper;



import nc.vo.trade.field.BillFieldOfMeta;
import nc.vo.trade.field.IBillField;

import java.util.List;
import nc.ui.trade.bocommand.IUserDefButtonCommand;
import nc.ui.trade.button.IBillButton;
import nc.ui.pub.ButtonObject;

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
  public abstract class AbstractClientUI extends nc.ui.trade.manage.BillManageUI implements  ILinkQuery{

	private List<IUserDefButtonCommand> bos = null;
	 
	public AbstractClientUI()
	{
		super();
		getBillCardPanel().setAutoExecHeadEditFormula(true);
	}
	 
	protected AbstractManageController createController() {
		return new ClientUICtrl();
	}
	
	/**
	 * ������ݲ���ƽ̨ʱ��UI����Ҫ���ش˷��������ز���ƽ̨��ҵ������� 
	 * @return BusinessDelegator ����ƽ̨��ҵ�������
	 */
	protected BusinessDelegator createBusinessDelegator() {
		return new nc.ui.so.taxinvoice.MyDelegator();
	}

	/**
	  * ע���Զ��尴ť
	  */
	 protected void initPrivateButton() {
	 
	  if (getUserButtons() != null) {
		   for(IUserDefButtonCommand cmd : getUserButtons())
			   addPrivateButton(cmd.getButtonVO());
		  	}
	 }
	 
	 public List<IUserDefButtonCommand> getUserButtons() {
		  if(bos == null)
			  bos = creatUserButtons();
		  return bos;
	 }
	 
	 protected abstract List<IUserDefButtonCommand> creatUserButtons();
	
		
		
		 protected IBillField createBillField() {
		  String billType = getUIControl().getBillType();
		  return new BillFieldOfMeta(billType);
	 }
	
	/**
	 * ע��ǰ̨У����
	 */
	public Object getUserObject() {
		//return new ClientUICheckRule();  //����Ҫ��������һ�¡� 2014-02-14 ����ʱ�ᱨ�Ҳ��������
		return new ClientUIGetter();
	}
	
	public void doQueryAction(ILinkQueryData querydata) {
	        String billId = querydata.getBillID();
	        if (billId != null) {
	            try {
	            	setCurrentPanel(BillTemplateWrapper.CARDPANEL);
	            	AggregatedValueObject vo = loadHeadData(billId);
	                getBufferData().addVOToBuffer(vo);
	                setListHeadData(new CircularlyAccessibleValueObject[]{vo.getParentVO()});
	                getBufferData().setCurrentRow(getBufferData().getCurrentRow());
	                setBillOperate(IBillOperate.OP_NO_ADDANDEDIT);
	            } catch (Exception ex) {
	                ex.printStackTrace();
	            }
	        }
    	}
    	
	protected void saveOnClosing() throws Exception {

		ButtonObject bo = getButtonManager().getButton(IBillButton.Save);
		getManageEventHandler().onButton(bo);
		
	}
}
