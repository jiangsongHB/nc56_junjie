package nc.ui.so.taxinvoice.command;

import nc.ui.so.taxinvoice.ClientUICheckRule;
import nc.ui.trade.bocommand.IButtonCommand;
import nc.ui.pub.bill.BillData;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.ClientEnvironment;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.VOStatus;
import nc.vo.pub.lang.UFDouble;
import nc.vo.so.TaxInvoiceBbVO;
import nc.vo.so.TaxInvoiceHeaderVO;
import nc.vo.so.TaxInvoiceItemVO;
import nc.vo.so.TaxInvoiceVO;
import nc.vo.trade.checkrule.VOChecker;
/**
 * <b> 带单据模板校验功能的保存操作 </b><br>
 *
 * <p>
 *     在此处添加此类的描述信息
 * </p>
 *
 *
 * @author author
 * @version tempProject version
 */
public class SaveBoCommand extends nc.ui.trade.manage.ManageEventHandler implements IButtonCommand {
	 

	public SaveBoCommand(nc.ui.trade.manage.BillManageUI billUI, nc.ui.trade.controller.IControllerBase control) {
		super(billUI, control);
	}

	public void execute(ButtonObject bo) throws Exception {
	
		BillData data = getBillCardPanelWrapper().getBillCardPanel().getBillData();
		if(data != null)
			data.dataNotNullValidate();
		ClientUICheckRule check = new ClientUICheckRule();
		
		AggregatedValueObject vo = getBillCardPanelWrapper().getBillCardPanel().getBillValueVO(TaxInvoiceVO.class.getName(), TaxInvoiceHeaderVO.class.getName(), TaxInvoiceItemVO.class.getName());
		
		if(!VOChecker.check(vo,check ))
			throw new nc.vo.pub.BusinessException(VOChecker.getErrorMessage());
		
		VOChecker.checkBack(vo,check ); //2014-03-07 wanglei 后台校验，检查表头重复
		
		TaxInvoiceItemVO[] vos = (TaxInvoiceItemVO[])vo.getChildrenVO();
		for (int i=0; i<vos.length; i ++) {
			if (vos[i].getNnumber() == null || vos[i].getNnumber().compareTo(UFDouble.ZERO_DBL) == 0 ||
					vos[i].getNsummny() == null || vos[i].getNsummny().compareTo(UFDouble.ZERO_DBL)	== 0) {
				throw new nc.vo.pub.BusinessException("发票行的数量，价税合计不能为空或零！");
			}
		}
		
		if(data.getBillstatus() == VOStatus.NEW) {
			data.getTailItem("dcreatedate").setValue(ClientEnvironment.getInstance().getServerTime().getDate());
			checkInvoiceNO((TaxInvoiceVO)vo);
		}
		if(data.getBillstatus() == VOStatus.UPDATED)  {
			data.getTailItem("dmodifydate").setValue(ClientEnvironment.getInstance().getServerTime().getDate());
			data.getTailItem("cmodifier").setValue(ClientEnvironment.getInstance().getUser().getPrimaryKey());
		}
		super.onBoSave();
	}

	private void checkInvoiceNO(TaxInvoiceVO vo) {
		// TODO Auto-generated method stub
		
	}
}
 