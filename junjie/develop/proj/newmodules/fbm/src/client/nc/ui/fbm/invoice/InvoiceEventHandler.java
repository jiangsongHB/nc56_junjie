/**
 *
 */
package nc.ui.fbm.invoice;

import nc.itf.cdm.util.CommonUtil;
import nc.ui.fac.account.pub.RefTakenQueryConditionClient;
import nc.ui.fac.account.pub.RefTakenQueryFilterEditorFactory;
import nc.ui.fbm.pub.DefaultLinkQueryParam;
import nc.ui.fbm.pub.FBMManageUI;
import nc.ui.fbm.pub.FBManageEventHandler;
import nc.ui.fbm.pub.IFBMButton;
import nc.ui.fbm.pub.YFBEditListerner;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillItem;
import nc.ui.trade.base.AbstractBillUI;
import nc.ui.trade.base.IBillOperate;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.trade.button.IBillButton;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.ui.uap.sf.SFClientUtil;
import nc.vo.cc.control.ICcControl;
import nc.vo.cc.pub.billenum.CCBillEnum;
import nc.vo.fbm.ccinterface.CCDataAdapter;
import nc.vo.fbm.pub.BaseinfoVO;
import nc.vo.fbm.pub.constant.FbmBusConstant;
import nc.vo.fbm.register.RegisterVO;
import nc.vo.fp.pub.IOBudgetQueryVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.trade.pub.IExAggVO;

/**
 * <p>
 * 开票登记前台事件处理
 * <p>
 * 创建人：lpf <b>日期：2007-9-4
 * 
 */
public class InvoiceEventHandler extends FBManageEventHandler {

	/**
	 * @param billUI
	 * @param control
	 */
	public InvoiceEventHandler(BillManageUI billUI, IControllerBase control) {
		super(billUI, control);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected UIDialog createQueryUI() {
		RefTakenQueryConditionClient dialog = new RefTakenQueryConditionClient(
				getBillUI(), createTemplateInof(getBillUI()));
		// dialog.registerFieldValueEelementEditorFactory(new
		// ReleaseFieldValueElementEditorFactory(dialog.getQueryContext()));
		dialog.registerFilterEditorFactory(new RefTakenQueryFilterEditorFactory(
				(FBMManageUI) getBillUI(),
				((FBMManageUI) getBillUI()).getRefTakenProccessor(), dialog));
		return dialog;
	}

	@Override
	protected String getHeadCondition() {
		String strwhere = super.getHeadCondition();
		if (CommonUtil.isNull(strwhere)) {
			return " (fbm_register.pk_billtypecode = '"
					+ FbmBusConstant.BILLTYPE_INVOICE
					+ "') ";
		} else {
			return strwhere
					+ " and (fbm_register.pk_billtypecode = '"
					+ FbmBusConstant.BILLTYPE_INVOICE
					+ "') ";
		}
	}

	@Override
	protected void onBoElse(int intBtn) throws Exception {
		switch (intBtn) {
		case IFBMButton.QUERYRATION:
			onLinkQueryRation();
			break;
		case IFBMButton.Invoice_Return:
			checkSfFlag();
			JumpToOtherUI(FbmBusConstant.BILLTYPE_RETURN);
			break;
		case IFBMButton.Invoice_BillPay:
			checkSfFlag();
			JumpToOtherUI(FbmBusConstant.BILLTYPE_BILLPAY);
			break;
		case IFBMButton.Gather_LQuerySFBill:
			String pk_busibill = getBufferData().getCurrentVO().getParentVO().getPrimaryKey();
			jumpToArapQuery(pk_busibill);
			break;
		case IFBMButton.BTN_QUERY_CHARGE_PLAN:
			onQueryPlan(createQueryPlanVO(RegisterVO.CHARGEPLANITEM));
			break;
		case IFBMButton.BTN_QUERY_INVOICE_PAY_PLAN:
			onQueryPlan(createQueryPlanVO(RegisterVO.INVOICEPLANITEM));
			break;
		case IFBMButton.PRINT4NOTE:
			onPrint4Note();
			break;
		}
		super.onBoElse(intBtn);
	}

	/**
	 * 检查付票登记单 已付票标志
	 * @throws BusinessException
	 */
	private void checkSfFlag() throws BusinessException{
		if(getBufferData().getCurrentVO()!=null && getBufferData().getCurrentVO().getParentVO() !=null){
			RegisterVO vo = (RegisterVO) getBufferData().getCurrentVO().getParentVO();
			if(!vo.getSfflag().booleanValue()){
				throw new BusinessException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("36201505", "UPP36201505-000007")/*
						 * @res
						 * "付票登记单未付票,无法执行后续业务操作"
						 */);
			}
		}
	}
	@Override
	protected void onBoSave() throws Exception {
		getBillCardPanelWrapper().getBillCardPanel().execHeadEditFormulas();
		super.onBoSave();

	}

	/**
	 * 
	 * <p>
	 * 联查授信额度
	 * <p>
	 * 作者：lpf 日期：2007-9-19
	 */
	private void onLinkQueryRation() {
		AggregatedValueObject vo = getBufferData().getCurrentVO();
		// fillInvoiceBufferVo(vo);

		if (vo == null || vo.getParentVO() == null)
			return;
		RegisterVO invoiceVo = (RegisterVO) vo.getParentVO();
		DefaultLinkQueryParam queryParam = new DefaultLinkQueryParam();
		queryParam.setBillPK(invoiceVo.getPrimaryKey());

		ICcControl cccontrol = CCDataAdapter.getInstance().invoiceToCControlForApprove(invoiceVo);
		queryParam.setUserObject(cccontrol);

		SFClientUtil.openLinkedQueryDialog(CCBillEnum.BankRationNokeKey, getUI(), queryParam);
	}

	@Override
	protected void onBoCopy() throws Exception {
		// 获得数据
		AggregatedValueObject copyVo = getBufferData().getCurrentVOClone();
		// 进行主键清空处理
		copyVo.getParentVO().setPrimaryKey(null);
		if (copyVo instanceof IExAggVO) {
			clearChildPk(((IExAggVO) copyVo).getAllChildrenVO());
		} else {
			clearChildPk(copyVo.getChildrenVO());
		}
		// 设置为新增处理
		getBillUI().setBillOperate(IBillOperate.OP_ADD);
		getUI().getBillCardPanel().getHeadItem(RegisterVO.SECURITYACCOUNT).setEnabled(true);
		// 设置单据号
		String noField = getBillUI().getBillField().getField_BillNo();
		BillItem noitem = getBillCardPanelWrapper().getBillCardPanel().getHeadItem(noField);
		if (noitem != null)
			copyVo.getParentVO().setAttributeValue(noField, noitem.getValueObject());
		// 设置界面数据
		getBillUI().setCardUIData(copyVo);
		getUI().getBillCardPanel().getHeadItem(RegisterVO.PK_BASEINFO).setValue(null);
		getBillCardPanelWrapper().getBillCardPanel().setHeadItem(RegisterVO.SFFLAG, new UFBoolean(false));
		getBillCardPanelWrapper().getBillCardPanel().setHeadItem(RegisterVO.INVOICEOUTPLANITEM, null);
	}

	/**
	 * 清空子表主键。 创建日期：(2004-2-25 19:59:34)
	 * 
	 * @param vos
	 *            nc.vo.pub.CircularlyAccessibleValueObject[]
	 */
	private void clearChildPk(CircularlyAccessibleValueObject[] vos)
			throws Exception {
		if (vos == null || vos.length == 0)
			return;
		for (int i = 0; i < vos.length; i++) {
			vos[i].setPrimaryKey(null);
		}
	}

	@Override
	protected void buttonActionAfter(AbstractBillUI billUI, int intBtn)
			throws Exception {
		super.buttonActionAfter(billUI, intBtn);
		switch (intBtn) {
		case IBillButton.Copy:
			String sfflag = (String) getUI().getBillCardPanel().getHeadItem(RegisterVO.SFFLAG).getValueObject();
			if ("true".equals(sfflag)) {
				getBillCardPanelWrapper().getBillCardPanel().getHeadItem(RegisterVO.INVOICEOUTPLANITEM).setEnabled(true);
			}
			getBillCardPanelWrapper().getBillCardPanel().getHeadItem(RegisterVO.REGISTERNO).setValue(null);
			getBillCardPanelWrapper().getBillCardPanel().getHeadItem(RegisterVO.FBMBILLNO).setValue(null);
			getBillCardPanelWrapper().getBillCardPanel().getHeadItem(RegisterVO.ISVERIFY).setValue(false);
			getBillCardPanelWrapper().getBillCardPanel().getHeadItem(RegisterVO.VOUCHER).setValue(false);
			getUI().fireCardAfterEdit(RegisterVO.IMPAWNMODE);
			BillItem bi = getUI().getBillCardPanel().getHeadItem(RegisterVO.PK_CURR);
			getBillCardPanelWrapper().getBillCardPanel().getHeadTailItem(RegisterVO.VERIFYDATE).setValue(null);
			getBillCardPanelWrapper().getBillCardPanel().getHeadTailItem(RegisterVO.VERIFYMAN).setValue(null);
			createCurrencyEditEvent(bi);
			break;
		case IBillButton.Edit:
			AggregatedValueObject vo = getUI().getBufferData().getCurrentVO();
			if (vo != null) {
				String assureType = (String) getUI().getBillCardPanel().getHeadItem(RegisterVO.IMPAWNMODE).getValueObject();
				if (!CommonUtil.isNull(assureType)
						&& assureType.equals(FbmBusConstant.ASSURETYPE_BAIL)) {
					getUI().getBillCardPanel().getHeadItem(RegisterVO.SECURITYACCOUNT).setEnabled(true);
					getUI().getBillCardPanel().getHeadItem(RegisterVO.SECURITYMONEY).setEnabled(true);
					getUI().getBillCardPanel().getHeadItem(RegisterVO.SECURITYRATE).setEnabled(true);
				} else {
					getUI().getBillCardPanel().getHeadItem(RegisterVO.SECURITYACCOUNT).setEnabled(false);
					getUI().getBillCardPanel().getHeadItem(RegisterVO.SECURITYMONEY).setEnabled(false);
					getUI().getBillCardPanel().getHeadItem(RegisterVO.SECURITYRATE).setEnabled(false);
				}
				String sfflag_b = (String) getUI().getBillCardPanel().getHeadItem(RegisterVO.SFFLAG).getValueObject();
				if ("true".equals(sfflag_b)) {
					getBillCardPanelWrapper().getBillCardPanel().getHeadItem(RegisterVO.INVOICEOUTPLANITEM).setEnabled(true);
				} else {
					getBillCardPanelWrapper().getBillCardPanel().getHeadItem(RegisterVO.INVOICEOUTPLANITEM).setEnabled(false);
				}
			}
			bi = getUI().getBillCardPanel().getHeadItem(RegisterVO.PK_CURR);
			createCurrencyEditEvent(bi);
		}
	}

	/**
	 * <p>
	 * 
	 * <p>
	 * 作者：lpf 日期：2007-12-29
	 * 
	 * @param bi
	 */
	private void createCurrencyEditEvent(BillItem bi) {
		BillEditEvent editevent = new BillEditEvent(bi.getComponent(),
				bi.getValueObject(), RegisterVO.PK_CURR, -1, BillItem.HEAD);
		new YFBEditListerner(getUI(), RegisterVO.PK_CURR, RegisterVO.PK_CURR,
				RegisterVO.MONEYY, RegisterVO.MONEYF, RegisterVO.MONEYB,
				RegisterVO.FRATE, RegisterVO.BRATE).responseEditEvent(editevent);
		new YFBEditListerner(getUI(), RegisterVO.PK_CURR, RegisterVO.PK_CURR,
				RegisterVO.SECURITYMONEY, RegisterVO.SECURITYMONEYF,
				RegisterVO.SECURITYMONEYB, RegisterVO.FRATE, RegisterVO.BRATE).responseEditEvent(editevent);
		new YFBEditListerner(getUI(), RegisterVO.PK_CURR, RegisterVO.PK_CURR,
				RegisterVO.POUNDAGEMONEY, RegisterVO.POUNDAGEMONEYF,
				RegisterVO.POUNDAGEMONEYB, RegisterVO.FRATE, RegisterVO.BRATE).responseEditEvent(editevent);
	}

	// @Override
	// protected UIDialog createQueryUI() {
	// return new RefTakenQueryConditionClient(
	// getBillUI(),
	// null,
	// _getCorp().getPrimaryKey(),
	// getBillUI()._getModuleCode(),
	// _getOperator(),
	// getBillUI().getBusinessType(),
	// getBillUI().getNodeKey(),
	// ((FBMManageUI)getBillUI()).getRefTakenProccessor());
	// }

	@Override
	protected IOBudgetQueryVO createQueryPlanVO(String planitemKey)
			throws Exception {
		// TODO Auto-generated method stub
		IOBudgetQueryVO vo = super.createQueryPlanVO(planitemKey);
		RegisterVO regVO = (RegisterVO) getBufferData().getCurrentVO().getParentVO();
		vo.setPk_planitem((String) regVO.getAttributeValue(planitemKey));
		vo.setPk_currtype(regVO.getPk_curr());
		HYPubBO_Client client = new HYPubBO_Client();
		BaseinfoVO baseVO = (BaseinfoVO) client.queryByPrimaryKey(BaseinfoVO.class, regVO.getPk_baseinfo());
		if (baseVO != null) {
			vo.setCheckplandate(baseVO.getInvoicedate());
		}
		return vo;
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see nc.ui.trade.bill.BillEventHandler#onBoQuery()
	 */
	@Override
	protected void onBoQuery() throws Exception {
		// TODO 自动生成方法存根
		super.onBoQuery();
	}

	@Override
	public void onBoActionElse(ButtonObject bo) throws Exception {
		if (bo.getCode().equals(IFBMButton.Invoice_CancelBill_STR)) {
			if (getUI().showOkCancelMessage(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("fbmcode112", "UPPFBMCODE112-000017")/*
																																		 * @res
																																		 * "核销操作不可逆，是否继续?"
																																		 */) != UIDialog.ID_OK) {
				return;
			}
		}
		super.onBoActionElse(bo);

	}

}