package nc.ui.ic.md;

import java.awt.event.ActionEvent;

import nc.ui.ic.md.dialog.MDUtils;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillItemEvent;
import nc.ui.pub.bill.BillMouseEnent;
import nc.ui.scm.plugin.IScmUIPlugin;
import nc.ui.scm.plugin.SCMUIContext;
import nc.vo.ic.pub.bill.GeneralBillItemVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.scm.plugin.Action;

/**
 * 
 * @author ThinkPad
 * @since 2010-09-20 11:24:15
 */
public abstract class SCMBasePlugin implements IScmUIPlugin {

	public void afterAction(Action arg0, AggregatedValueObject[] arg1,
			SCMUIContext arg2) throws BusinessException {
	}

	public void afterButtonClicked(ButtonObject arg0, SCMUIContext arg1)
			throws BusinessException {

	}

	public void afterEdit(BillEditEvent arg0, SCMUIContext arg1) {

	}

	public void afterSetBillVOToCard(AggregatedValueObject arg0,
			SCMUIContext arg1) {

	}

	public void afterSetBillVOsToListBody(
			CircularlyAccessibleValueObject[] arg0, SCMUIContext arg1) {

	}

	public void afterSetBillVOsToListHead(
			CircularlyAccessibleValueObject[] arg0, SCMUIContext arg1) {

	}

	public void beforeAction(Action arg0, AggregatedValueObject[] arg1,
			SCMUIContext arg2) throws BusinessException {
		if(MDUtils.getBillNameByBilltype(arg2.getCbilltype())!=null){
			if(arg0==Action.DELETE){
				beforDelte(arg1);
			}else if(arg0==Action.SAVE){
				beforSave(arg1);
			}else if(arg0==Action.AUDIT){
				beforAudit(arg1);
			}
		}
	}

	/**
	 * 签字前调用方法
	 * @param arg1
	 * @throws BusinessException
	 */
	public abstract void beforAudit(AggregatedValueObject[] arg1) throws BusinessException;
	
	/**
	 * 删除前调用方法
	 * @param arg1
	 * @param arg2
	 * @throws BusinessException
	 */
	public abstract void beforDelte(AggregatedValueObject[] arg1 )  throws BusinessException;
	
	/**
	 * 保存前调用方法
	 * @param arg1
	 * @param arg2
	 * @throws BusinessException
	 */
	public abstract void beforSave(AggregatedValueObject[] arg1 )  throws BusinessException;

	public void beforeButtonClicked(ButtonObject arg0, SCMUIContext arg1)
			throws BusinessException {
//		if(arg0.getName().equals(ICButtonConst.BTN_LINE_DELETE)){
//			int[] i = arg1.getBillCardPanel().getBillTable().getSelectedRows();
//			ArrayList<GeneralBillItemVO> list = new ArrayList<GeneralBillItemVO>();
//			for (int j : i) {
//				GeneralBillItemVO itemvo = (GeneralBillItemVO)arg1.getBillCardPanel().getBillModel().getBodyValueRowVO(j, GeneralBillItemVO.class.getName());
//				list.add(itemvo);
//			}
//			beforDeleteLine(list.toArray(new GeneralBillItemVO[list.size()]));
//		}
	}

	public abstract void beforDeleteLine(GeneralBillItemVO[] items) throws BusinessException;

	public boolean beforeEdit(BillEditEvent arg0, SCMUIContext arg1) {
		return true;
	}

	public boolean beforeEdit(BillItemEvent arg0, SCMUIContext arg1) {
		return true;
	}

	public AggregatedValueObject[] beforePrint(AggregatedValueObject[] arg0,
			SCMUIContext arg1) {
		return null;
	}

	public void beforeSetBillVOToCard(AggregatedValueObject arg0,
			SCMUIContext arg1) {

	}

	public void beforeSetBillVOsToListBody(
			CircularlyAccessibleValueObject[] arg0, SCMUIContext arg1) {

	}

	public void beforeSetBillVOsToListHead(
			CircularlyAccessibleValueObject[] arg0, SCMUIContext arg1) {

	}

	public void bodyRowChange(BillEditEvent arg0, SCMUIContext arg1) {

	}

	public boolean init(SCMUIContext arg0) {
		return true;
	}

	public void mouse_doubleclick(BillMouseEnent arg0, SCMUIContext arg1) {
	}

	public void onAddLine(SCMUIContext arg0) throws BusinessException {

	}

	public void onMenuItemClick(ActionEvent arg0, SCMUIContext arg1) {

	}

	public void onPastLine(SCMUIContext arg0) throws BusinessException {

	}

	public String onQuery(String arg0, SCMUIContext arg1)
			throws BusinessException {
		return null;
	}

	public Object[] retBillToBillRefVOs(CircularlyAccessibleValueObject[] arg0,
			CircularlyAccessibleValueObject[] arg1) throws BusinessException {
		return null;
	}

	public AggregatedValueObject[] retChangeBusiVOs(
			AggregatedValueObject[] arg0, AggregatedValueObject[] arg1)
			throws BusinessException {
		return null;
	}

	public void setButtonStatus(SCMUIContext arg0) {

	}

}
