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
	 * 如果单据不走平台时，UI类需要重载此方法，返回不走平台的业务代理类 
	 * @return BusinessDelegator 不走平台的业务代理类
	 */
	protected BusinessDelegator createBusinessDelegator() {
		return new nc.ui.so.taxinvoice.MyDelegator();
	}

	/**
	  * 注册自定义按钮
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
	 * 注册前台校验类
	 */
	public Object getUserObject() {
		//return new ClientUICheckRule();  //这里要这样调整一下。 2014-02-14 保存时会报找不到类错误
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
