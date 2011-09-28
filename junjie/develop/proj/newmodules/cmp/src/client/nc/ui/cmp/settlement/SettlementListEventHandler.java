package nc.ui.cmp.settlement;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import org.apache.commons.lang.StringUtils;

import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.bs.pf.pub.PfDataCache;
import nc.cmp.utils.CMPFactory;
import nc.cmp.utils.CheckException;
import nc.cmp.utils.CmpUtils;
import nc.cmp.utils.GenerateSettleNo;
import nc.cmp.utils.InterfaceLocator;
import nc.cmp.utils.Lists;
import nc.cmp.utils.NetValidate;
import nc.cmp.utils.SettleValidate;
import nc.itf.cm.prv.CmpConst;
import nc.itf.cmp.settlement.ISettlementService;
import nc.itf.fi.pub.Currency;
import nc.itf.fi.pub.SysInit;
import nc.ui.cmp.netpayment.PaymentProc;
import nc.ui.cmp.netpayment.SearchNetPay;
import nc.ui.cmp.netpayment.SearchPay;
import nc.ui.cmp.pub.AccountExceptionHandler;
import nc.ui.cmp.pub.CAAuthor;
import nc.ui.cmp.pub.DefaultListEventHandler;
import nc.ui.cmp.pub.DefaultListUI;
import nc.ui.cmp.pub.ICMPButton;
import nc.ui.ep.dj.ShenheLog;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.pfflow.PfCall;
import nc.ui.trade.button.IBillButton;
import nc.vo.arap.exception.ExceptionHandler;
import nc.vo.bd.notetype.INotetypeConst;
import nc.vo.cmp.BusiStatus;
import nc.vo.cmp.SettleStatus;
import nc.vo.cmp.exception.CmpAuthorizationException;
import nc.vo.cmp.exception.ErmException;
import nc.vo.cmp.settlement.DataSourceVO;
import nc.vo.cmp.settlement.FtsProcess;
import nc.vo.cmp.settlement.SettleConditionVO;
import nc.vo.cmp.settlement.SettlementAggVO;
import nc.vo.cmp.settlement.SettlementBodyVO;
import nc.vo.cmp.settlement.SettlementHeadVO;
import nc.vo.cmpbill.outer.BugetAlarmBusinessException;
import nc.vo.cmpbill.outer.CmpFpBusinessException;
import nc.vo.fts.fts102.BillConst;
import nc.vo.ml.NCLangRes4VoTransl;
import nc.vo.pub.BusinessException;
import nc.vo.pub.billtype.BilltypeVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.pfflow01.BillbusinessVO;
import nc.vo.querytemplate.CompositeMetaVO;
import nc.vo.querytemplate.TemplateInfo;


public class SettlementListEventHandler extends DefaultListEventHandler {

	public static final int FIRST_PAGE = 1;

//	private int pageSize;

	private SettlementCardUI cardPanel;

	private ISettlementService service;

	private boolean isDoubleClieck = false;

	private int selectRow = 0;

	private TemplateInfo tempinfo;

	private SettlementBodyVO body;

	private SettlementAggVO aggVO;

	private SettleQueryDlg dlg;

	private String defaultSQL;

	private SettleNormalPanel normalPanel;

	private SettleConditionVO conditionVO;

	private SettleValidate validate;

	private UIRuntimeVO runtime;
	
	private boolean isDlg;

	//ƾ֤����
	private String voucher;

//	static {
//		try {
//			PAGE_SIZE =
//		} catch (BusinessException e) {
//			Logger.error(e.getMessage());
//		}
//	}

	private ISettlementService getService() {
		if (service == null) {
			return  NCLocator.getInstance().lookup(ISettlementService.class);
		}
		return service;
	}

	public SettlementListEventHandler(DefaultListUI listUI) {
		getRuntime().setListUI((SettlementListUI)listUI);
		getRuntime().getListUI().setRuntime(getRuntime());
		try {
			setPageSize(Integer.valueOf(SysInit.getParaString(ClientEnvironment.getInstance().getCorporation().getPk_corp(), "FICOMMON02")));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Logger.error(e.getMessage(), e);
		}
	}

	public void onButton(ButtonObject bo) {
		int intBtn = Integer.parseInt(bo.getTag());
		if (intBtn > 100) {
			// ��Ŵ���100��Ϊ���Զ��尴ť
			onBoElse(intBtn);
		} else {
			// ������Ϊ��Ԥ�ð�ť
			onButton(intBtn);
		}
	}

	private void onButton(int intBtn) {
		switch (intBtn) {
		case IBillButton.Card:
			onBoCard();
			break;
		case IBillButton.Query:
			onBoQuery();
			break;
		case IBillButton.Edit:
			onBoEdit();
			break;
		case IBillButton.SelAll:
			onBoSelAll();
			break;
		case IBillButton.SelNone:
			onBoSelNone();
			break;
		case IBillButton.First:
			onBoFirst();
			break;
		case IBillButton.Last:
			onBoLast();
			break;
		case IBillButton.Prev:
			onBoPrev();
			break;
		case IBillButton.Next:
			onBoNext();
			break;
		case IBillButton.Refresh:
			onBoRefresh();
			break;
		case IBillButton.Print:
			onBoPrint();
			break;
		}
	}

	/**
	 * ��ӡ
	 *
	 */
	private void onBoPrint() {

	}

	/**
	 * ��ӡ�б�
	 *
	 */
	private void onBoPrintList() {
		//ClientEnvironment env = ClientEnvironment.getInstance();
//		String pk_corp = getList().getPkCorp();
//		FiPrintEntry entry = FiPrintEntryFactory.createBillListPanelPrintEntry(getList().getBillListPanel(), true);
//		entry.printView(pk_corp, "20040401", "list");

		new PrintHandler().printList(getList().getBillListPanel(), getList().getPkCorp(), "20040401", "list", true);
	}

	/**
	 * ��ӡ�ص�
	 *
	 */
	private void onBoPrintReceipt() {
		// TODO Auto-generated method stub

	}

	private void onBoRefresh() {
		try {
			DataSourceVO<SettlementHeadVO> source = new DataSourceVO<SettlementHeadVO>();
//			source.setGoToPage(getRuntime().getListUI().getRuntime().getTransfer().getPageNo() == 0?  FIRST_PAGE : getRuntime().getTransfer().getPageNo());
			source.setGoToPage(FIRST_PAGE);
			String whereSQL = null;
			if(!isDlg) {
				getDlg().initUIData();
				whereSQL = getDlg().getWhereSQL();
				String sql = getSql(whereSQL, getConditionVO());
				setDefaultSQL(sql);
			}
			source.setPageSize(getRuntime().getListUI().getRuntime().getTransfer().getPageSize() == 0 ? getPageSize() : getRuntime().getTransfer().getPageSize());
			source.setCondition(getRuntime().getListUI().getRuntime().getTransfer().getCondition() == null ? getDefaultSQL() : getRuntime().getTransfer().getCondition());
			source.setVoucher(getVoucher());
			getRuntime().getTransfer().setTempMap(null);
			getRuntime().getTransfer().setMap(null);
			getRuntime().getTransfer().setRefresh(true);
			getQuery().queryAggVO(getList(), source);
			getList().showHintMessage(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("payment","UPPpayment-000002")/*@res "ˢ�����"*/);
			getList().updateButtonStatus(BillStatus.QUERY);
		} catch (BusinessException e) {
			Logger.error(e.getMessage(), e);
			getList().showErrorMessage(e.getMessage());

		}
	}

	private void onBoEdit() {
		try {
			SettlementAggVO aggVO = null;
			aggVO = getSelectedVO();
//			new CAAuthor.INSTANCE().author();
			if (aggVO.getParentVO() == null && aggVO.getChildrenVO() == null) {
				throw new BusinessException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("payment","UPPpayment-000003")/*@res "��ѡ�����ĵ���"*/);
			}
			SettlementHeadVO head = (SettlementHeadVO) aggVO.getParentVO();
			if (head.getBusistatus() == BusiStatus.Sign.getBillStatusKind()) {
				throw new BusinessException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("payment","UPPpayment-000050")/*@res "�Ѿ�ǩ�ֵĵ��ݲ����޸�"*/);
			}
			onBoCard();
			((SettlementCardUI)(getRuntime().getCardUI())).invoke("edit", "edit");
		} catch (BusinessException e) {
			Logger.error(e.getMessage(), e);
			getList().showErrorMessage(e.getMessage());
		}
	}

	private void onBoQuery() {
		try {
			String whereSql = getSqlCondition();
			SettleConditionVO conditionVO2 = getConditionVO();
			if (whereSql != null && whereSql.equals("null")) {
				return;
			}
			String condition = getSql(whereSql, conditionVO2);
			DataSourceVO<SettlementHeadVO> source = new DataSourceVO<SettlementHeadVO>();
			source.setGoToPage(FIRST_PAGE);
			source.setPageSize(getPageSize());

			source.setCondition(condition);
			source.setVoucher(getVoucher());
			getRuntime().getTransfer().setTempMap(null);
			getRuntime().getTransfer().setMap(null);
			getRuntime().getTransfer().setRefresh(true);
			getRuntime().getTransfer().setVourch(getVoucher());
			getQuery().queryAggVO(getList(), source);
//			getList().updateButtonStatus(BillStatus.QUERY);
			getList().updateButtonStatusByHead(getList().getHeadList().get(0));
		}catch(BusinessException e) {
			Logger.error(e.getMessage(), e);
			getList().showErrorMessage(e.getMessage());
		}
	}

	private String getSqlCondition() throws BusinessException {
		boolean isCan = true;
		String sql = null;
		while(isCan) {
			SettleQueryDlg dlg = getDlg();
			dlg.transferFocus();
			if (dlg.showModal() != UIDialog.ID_OK) {
				return "null";
			}
			sql = dlg.getWhereSQL();
			int countMatches = StringUtils.countMatches(sql, "cmp_settlement.busi_billdate");
			if(countMatches != 2) {
				MessageDialog.showErrorDlg(getList(), "����", "�������ڱ���Ϊ������");
			}else {
				isCan = false;
			}
		}
		return sql;
//		if(!sql.contains("AND")) {
//			
//		}else {
//			String[] split = sql.split("AND");
//		}
//		StringBuilder sb = new StringBuilder(sql);
//		sb.

	}

	protected void onBoCard() {
		try {
			SettlementAggVO aggVO = null;
			aggVO = getSelectedVO();
			CheckException.checkArgument(aggVO.getParentVO() == null && aggVO.getChildrenVO() == null, nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("payment","UPPpayment-000003")/*@res "��ѡ�����ĵ���"*/);
			List<String> sortColumnKeys = getList().getBillListPanel().getHeadBillModel().getSortColumnKeys();
			//������������
			if(!CheckException.checkContionsIsNull(sortColumnKeys)) {
				processSort();
			}else {
//				List<SettlementHeadVO> heas = Lists.newArrayList();
				getRuntime().setSortList(Lists.newLinkedList(getRuntime().getShowMap().values()));
			}
			List<String> idList = CmpUtils.makeList();

			idList.add(aggVO.getParentVO().getPrimaryKey());
			getRuntime().setDirection(((SettlementHeadVO) aggVO.getParentVO()).getDirection());
			getRuntime().setSelectedId(idList);
			getRuntime().setBufferMap(getRuntime().getBufferMap());
			getRuntime().setListUI(getList());
			getRuntime().setTransfer(getRuntime().getTransfer());
			getRuntime().setShowMap(getRuntime().getShowMap());
			getRuntime().setCardUI(new SettlementCardUI());
			getRuntime().getCardUI().setRuntime(getRuntime());
			((SettlementCardUI)getRuntime().getCardUI()).invoke("initbusi", aggVO);
			getList().listparent.showNext((SettlementCardUI)getRuntime().getCardUI());

			((SettlementCardUI)getRuntime().getCardUI()).setBillStatus(BillStatus.BROW);

			((SettlementCardUI)getRuntime().getCardUI()).invoke("selfAggVO", aggVO);

		} catch (BusinessException e) {
			Logger.error(e.getMessage(), e);
			getList().showErrorMessage(e.getMessage());
		}

	}

	private void processSort() throws BusinessException {
		int rowCount = getList().getBillListPanel().getHeadTable().getRowCount();
//		Map<String, SettlementHeadVO> cloneMap = CmpUtils.CloneObj(getRuntime().getShowMap());
//		getRuntime().getShowMap().clear();
		List<SettlementHeadVO> lists = Lists.newLinkedList();
		for(int i = 0; i<rowCount; i++) {
			String id = (String)getList().getBillListPanel().getHeadBillModel().getValueAt(i, "pk_settlement");
			lists.add(getRuntime().getShowMap().get(id));
			
		}
		getRuntime().setSortList(lists);
	}

	private void onBoElse(int intBtn) {
		switch (intBtn) {
		case ICMPButton.SIGNATURE:
			onBoSign();
			break;
		case ICMPButton.CANCEL_SIGMATURE:
			onBoCancelSign();
			break;
		case ICMPButton.SETTLE:
			onBoSettle();
			break;
		case ICMPButton.CANCEL_SETTLE:
			onBoCancelSettle();
			break;
		case ICMPButton.COMBIN_SETTLE:
			onBoCombinSettle();
			break;
//		case ICMPButton.COMMISSION_PAYING:
//			onBoCommissionPay();
//			break;
//		case ICMPButton.COMMISSION_GATHERING:
//			onBoCommissionGather();
//			break;
		case ICMPButton.PRINT_LIST:
			onBoPrintList();
			break;
		case ICMPButton.PRINT_RECEIPT:
			onBoPrintReceipt();
			break;
		case ICMPButton.MAKE_BILL:
			onBoMakeBill();
			break;
		case ICMPButton.DOCUMENT_MANAGE:
			onBoDocmng();
			break;
		case ICMPButton.REGISTER_BILL:
			onBoRegBill();
			break;
		case ICMPButton.SEARCH_AUDIT_CONDITION:
			onBoSearchAuditCondition();
			break;
		case ICMPButton.SEARCH_NOTE:
			onBoSearchNote();
			break;
		case ICMPButton.SEARCH_VOUCHER:
			onBoSearchVoucher();
			break;
		case ICMPButton.SEARCH_BUGDET_PERFORM:
			onBoSearchBugdet();
			break;
		case ICMPButton.SEARCH_CAPITAL_PLAN:
			onBoSearchCapitalPlan();
			break;
		case ICMPButton.SEARCH_CONSIGN_PAY:
			onBoSearchConsignPay();
			break;
		case ICMPButton.SEARCH_CONSIGN_RECEIVE:
			onBoSearchConsignReceive();
			break;
		case ICMPButton.SEARCH_BALANCE:
			onBoSearchBalance();
			break;
		case ICMPButton.SEARCH_AUDIT_BILL:
			onBoSearchAuditBill();
			break;
		case ICMPButton.SEARCH_NETBANK:
			onBoSearchEbank();
			break;
		case ICMPButton.SEARCH_PAY_AFFIRM:
			onBoSearchPayAffirm();
			break;
		case ICMPButton.FILL_NETBANK:
			onBoFillNetBank();
			break;
		case ICMPButton.NET_TRANSFER:
			onBoNetTransfer();
			break;
		case ICMPButton.COMBIN_PAYING:
			onBoCombinPay();
			break;
		case ICMPButton.SUBMIT:
			onBoSubmit();
			break;
		}

	}

	private void onBoSubmit() {
		try {
			SettlementAggVO[] aggVO = null;
			aggVO = getSelectedAggVO();
			int len = aggVO == null ? 0 : aggVO.length;
			CheckException.checkArgument(len != 1, nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("payment","UPPpayment-000075")/*@res "��ѡ��һ�ŵ���"*/);
			SettlementAggVO agg = aggVO[0];
//			getFtsHandler(((SettlementHeadVO)agg.getParentVO()).getPk_busitype()).doFtsWork(agg, getList());
			getFtsHandler(agg);
			if(getRuntime().getShowMap() != null) {
				SettlementHeadVO settlementHeadVO = getRuntime().getShowMap().get(agg.getParentVO().getPrimaryKey());
				if(settlementHeadVO != null) {
					if(!settlementHeadVO.getSettlestatus().equals(SettleStatus.NONESETTLE.getStatus())) {
						getList().showHintMessage("�ύ�ɹ�");
					}
				}
			}
		} catch (BusinessException e) {
			Logger.error(e.getMessage(), e);
			getList().showErrorMessage(e.getMessage());
		}
	}
	
	private void getFtsHandler(SettlementAggVO agg) throws BusinessException {
		SettlementHeadVO head = (SettlementHeadVO) agg.getParentVO();
		String pk_busitype = head.getPk_busitype();
		DefaultFtsHandler ftsHandler = null;
		//���ʹ�õ���Ĭ�J����������Ҫ��ȡ���ݿ�
		if(CmpConst.COMMISION_PAY_SET.contains(pk_busitype)) {
			CAAuthor.INSTANCE.author4Net();
			ftsHandler =  new CommisionPayHandler();
			ftsHandler.doFtsWork(agg, getList(), FtsProcess.COMMISION_PAY);
			return;
		}else if(CmpConst.COMMISION_REC_SET.contains(pk_busitype)) {
			ftsHandler =  new CommisionReceiveHandler();
			ftsHandler.doFtsWork(agg, getList(), FtsProcess.COMMISION_REC);
			return;
		}else if(CmpConst.INNER_REC_TYPE.equals(pk_busitype)) {
			CAAuthor.INSTANCE.author4Net();
			ftsHandler =  new FtsInnerRecHandler();
			ftsHandler.doFtsWork(agg, getList(), FtsProcess.INNER_REC);
			return;
		}
		//����ʹ�õ�Ĭ����������Ҫ��������ƽ̨��ѯ
		BilltypeVO billTypeInfo = PfDataCache.getBillTypeInfo(head.getPk_tradetype());
		String parentBilltype = billTypeInfo.getParentbilltype();
		BillbusinessVO[] findAllDrive = null;
		
		//���ж��Ƿ�Ϊ���Ŷ��������
		findAllDrive = findDrives(head, parentBilltype, true);
		if(findAllDrive == null) {
			findAllDrive = findDrives(head, parentBilltype, false);
		}
		CheckException.checkArgument(CheckException.checkArraysIsNull(findAllDrive), "��������ѡ�����,���ܽ����ύ����");
		Set<String> billtypes = new HashSet<String>();
		for(BillbusinessVO vo : findAllDrive) {
			billtypes.add(vo.getPk_billtype());
		}

		if(head.getDirection() == CmpConst.Direction_Receive && billtypes.contains("LZ")) {
			ftsHandler =  new CommisionReceiveHandler();
			ftsHandler.doFtsWork(agg, getList(), FtsProcess.COMMISION_REC);
			
		}else if(head.getDirection() == CmpConst.Direction_Pay) {
			CAAuthor.INSTANCE.author4Net();
			if(billtypes.contains("36LD")) {
				ftsHandler =  new CommisionPayHandler();
				ftsHandler.doFtsWork(agg, getList(), FtsProcess.COMMISION_PAY);
			}else if(billtypes.contains("36LG")) {
				ftsHandler =  new FtsInnerRecHandler();
				ftsHandler.doFtsWork(agg, getList(), FtsProcess.INNER_REC);
			}
		}
		CheckException.checkArgument(ftsHandler == null, "���̴��󣬲����ύ");
		
	}
	
	private BillbusinessVO[] findDrives(SettlementHeadVO head, String pk_billtype, boolean isGroup) throws BusinessException {
		//���жϵ����������жϽ�������
		BillbusinessVO[] findAllDrive = null;
		BillbusinessVO billbusiVO = new BillbusinessVO();
		billbusiVO.setPk_businesstype(head.getPk_busitype());
		billbusiVO.setPk_corp(isGroup? "@@@@" : head.getPk_corp());
		billbusiVO.setPk_billtype(pk_billtype);
		findAllDrive = PfCall.findAllDrive(billbusiVO);
		if(findAllDrive == null) {
			billbusiVO.setPk_billtype(null);
			billbusiVO.setTranstype(head.getPk_tradetype());
			findAllDrive = PfCall.findAllDrive(billbusiVO);
		}
		return findAllDrive;
	}

	private void onBoCombinPay() {
		try {
			SettlementAggVO[] selects = getSelectedAggVO();
			CAAuthor.INSTANCE.author4Net();
			int i = MessageDialog.showYesNoDlg(getList(), nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("20040401","UPP20040401-000011")/*@res "��ʾ"*/, nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("20040401","UPP20040401-000012")/*@res "ȷ����������֧����"*/);
			if(i != UIDialog.ID_YES) {
				return;
			}
//			new SettleValidate().checkSettleStatus(selects);
			new PaymentProc().doZhifu(getList(), selects);
			refesh();
			getList().showHintMessage(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("20040401","UPP20040401-000013")/*@res "֧�����"*/);
		}catch(BusinessException e) {
			Logger.error(e.getMessage(), e);
			getList().showErrorMessage(e.getMessage());

		}
	}

	private void onBoNetTransfer() {
		try {
			PaymentProc pay = null;
			SettlementAggVO[] selects = getSelectedAggVO();
			CAAuthor.INSTANCE.author4Net();
			int i = MessageDialog.showYesNoDlg(getList(), nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("20040401","UPP20040401-000011")/*@res "��ʾ"*/, nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("20040401","UPP20040401-000012")/*@res "ȷ����������֧����"*/);
			if(i != UIDialog.ID_YES) {
				return;
			}
//			new SettleValidate().checkSettleStatus(selects);
			new NetValidate().validateCombin(selects, true);
			for(SettlementAggVO aggVO : selects) {
				if(pay == null) {
					pay = new PaymentProc();
				}
				pay.doZhuanzhang(getList(), aggVO);
			}
			refesh();
			getList().showHintMessage(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("20040401","UPP20040401-000013")/*@res "֧�����"*/);
		}catch(BusinessException e) {
			Logger.error(e.getMessage(), e);
			getList().showErrorMessage(e.getMessage());

		}
	}

	private void onBoFillNetBank() {
		try {
			SettlementAggVO[] selects = getSelectedAggVO();
			CheckException.checkArgument(selects.length > 1, nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("payment","UPPpayment-000004")/*@res "һ��ֻ�ܲ�¼һ�ŵ��ݵ�������Ϣ"*/);
//			new SettleValidate().checkSettleStatus(selects);
			for(SettlementAggVO aggVO : selects) {
				NetValidate.isForPerson(aggVO);
//				CheckException.checkArgument(CmpUtils.isForPerson((SettlementBodyVO[])aggVO.getChildrenVO()), "��˽֧���ĵ��ݲ���¼������Ϣ");
			}
			new PaymentProc().doBulu(getList(), selects);
		}catch(BusinessException e) {
			Logger.error(e.getMessage(), e);
			getList().showErrorMessage(e.getMessage());

		}
	}

	private void onBoSearchPayAffirm() {
		try {
			SettlementAggVO[] selects = getSelectedAggVO();
			new SearchPay().search(getList(), selects);
		}catch(BusinessException e) {
			Logger.error(e.getMessage(), e);
			getList().showErrorMessage(e.getMessage());

		}
	}

	private void onBoSearchEbank() {
		try {
			SettlementAggVO[] selects = getSelectedAggVO();
			new SearchNetPay().search(selects, getList());
		}catch(BusinessException e) {
			Logger.error(e.getMessage(), e);
			getList().showErrorMessage(e.getMessage());
		}
	}

	private void onBoSearchAuditBill() {

	}

	private void onBoSearchBalance() {
		try {

			SettlementBodyVO body = getSelectedBody();
			CheckException.checkArgument(body == null, nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("payment","UPPpayment-000053")/*@res "��ѡ����Ҫ��ѯ������"*/);
			CheckException.checkArgument(body.getPk_account() == null, nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("payment","UPPpayment-000054")/*@res "�ñ�����û�б����˻�����������"*/);
//			if(body == null) {
//				throw new BusinessException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("payment","UPPpayment-000053")/*@res "��ѡ����Ҫ��ѯ������"*/);
//			}
//			if(body.getPk_account() == null) {
//				throw new BusinessException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("payment","UPPpayment-000054")/*@res "�ñ�����û�б����˻�����������"*/);
//			}
			SettlementAggVO aggVO = new SettlementAggVO();
			aggVO.setChildrenVO(new SettlementBodyVO[]{body});
			new LinkSearchHandler(aggVO, getList()).searchBalance();
		}catch(BusinessException e) {
			Logger.error(e.getMessage(), e);
			getList().showErrorMessage(e.getMessage());
		}
	}

	private void onBoSearchConsignReceive() {
		try {
			SettlementAggVO[] stateSelectedVOs = getStateSelectedVOs();
			int len = stateSelectedVOs == null ? 0 : stateSelectedVOs.length;
			if (len != 1) {
				throw new BusinessException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("payment","UPPpayment-000075")/*@res "��ѡ��һ�ŵ���"*/);
			}
			SettlementHeadVO head = (SettlementHeadVO) stateSelectedVOs[0].getParentVO();
			if(head.getPk_ftsbill() == null) {
				if(!head.getIsapplybill().booleanValue() || (head.getIsapplybill().booleanValue() && head.getDirection() != CmpConst.Direction_Receive)) {
					throw new BusinessException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("payment","UPPpayment-000055")/*@res "û��ί���տ����"*/);
				}
			}else {
				if(head.getDirection() == CmpConst.Direction_Receive || (head.getFts_billtype() != null && (head.getFts_billtype() == BillConst.PaymentCenterBillType ))) {
					throw new BusinessException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("payment","UPPpayment-000055")/*@res "û��ί���տ����"*/);
				}
			}
			new LinkSearchHandler(stateSelectedVOs[0], getList()).searchConsignReceive();
		}catch(BusinessException e) {
			Logger.error(e.getMessage(), e);
			getList().showErrorMessage(e.getMessage());
		}
	}

	private void onBoSearchConsignPay() {
		try {
			SettlementAggVO[] stateSelectedVOs = getStateSelectedVOs();
			int len = stateSelectedVOs == null ? 0 : stateSelectedVOs.length;
//			if (len != 1) {
//				throw new BusinessException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("payment","UPPpayment-000075")/*@res "��ѡ��һ�ŵ���"*/);
//			}
			CheckException.checkArgument(len != 1, nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("payment","UPPpayment-000075")/*@res "��ѡ��һ�ŵ���"*/);
			SettlementHeadVO head = (SettlementHeadVO) stateSelectedVOs[0].getParentVO();
			if(head.getPk_ftsbill() == null) {
				CheckException.checkArgument(!head.getIsapplybill().booleanValue() || (head.getIsapplybill().booleanValue() && head.getDirection() != CmpConst.Direction_Pay), nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("payment","UPPpayment-000056")/*@res "û��ί�и������"*/);
//				if(!head.getIsapplybill().booleanValue() || (head.getIsapplybill().booleanValue() && head.getDirection() != CmpConst.Direction_Pay)) {
//					throw new BusinessException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("payment","UPPpayment-000056")/*@res "û��ί�и������"*/);
//				}
			}else {
				CheckException.checkArgument(head.getDirection() == CmpConst.Direction_Pay || (head.getFts_billtype() != null && (head.getFts_billtype() == BillConst.GatheringBillType)), nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("payment","UPPpayment-000056")/*@res "û��ί�и������"*/);
//				if(head.getDirection() == CmpConst.Direction_Pay || (head.getFts_billtype() != null && (head.getFts_billtype() == BillConst.GatheringBillType))) {
//					throw new BusinessException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("payment","UPPpayment-000056")/*@res "û��ί�и������"*/);
//				}
			}
			new LinkSearchHandler(stateSelectedVOs[0], getList()).searchConsignPay();
		}catch(BusinessException e) {
			Logger.error(e.getMessage(), e);
			getList().showErrorMessage(e.getMessage());
		}
	}

	private void onBoSearchCapitalPlan() {
		// TODO Auto-generated method stub

	}

	private void onBoSearchBugdet() {
		// TODO Auto-generated method stub

	}

	private void onBoSearchVoucher() {
		try {
			SettlementAggVO[] aggVO = null;
			aggVO = getSelectedAggVO();
			int len = aggVO == null ? 0 : aggVO.length;
			CheckException.checkArgument(len != 1, nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("payment","UPPpayment-000075")/*@res "��ѡ��һ�ŵ���"*/);
			SettlementAggVO selectedAggVO = aggVO[0];
			new LinkSearchHandler(selectedAggVO, getList()).searchVoucher();
		}catch(BusinessException e) {
			Logger.error(e.getMessage(), e);
			getList().showErrorMessage(e.getMessage());
		}


	}

	private void onBoSearchNote() {

		try {

			SettlementBodyVO body = getSelectedBody();
			if(body == null) {
				throw new BusinessException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("payment","UPPpayment-000053")/*@res "��ѡ����Ҫ��ѯ������"*/);
			}
			String pk_notetype = body.getPk_notetype();
			if (pk_notetype != null) {
				String nc = getList().getColValue("bd_notetype", "noteclass", "pk_notetype", pk_notetype);
				if (nc != null && nc.trim().length() != 0) {
					int noteclass = Integer.valueOf(nc);
					if (noteclass != INotetypeConst.NOTECLASS_COMMERCIALDRAFT) {
						throw new BusinessException(NCLangRes4VoTransl.getNCLangRes().getStrByID("payment","UPPpayment-000057")/*@res "������ҵ��Ʊ����������Ʊ��"*/);
					}
					new FtsPJHandler().doPjLinkQuery(getList(), body.getPk_settlement(), body.getPk_detail());
				}
			}else {
				throw new BusinessException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("payment","UPPpayment-000058")/*@res "û��Ʊ�����ͣ����ܲ�ѯ"*/);
			}
		}catch(BusinessException e) {
			Logger.error(e.getMessage(), e);
			getList().showErrorMessage(e.getMessage());
		}
	}

	private void onBoSearchAuditCondition() {
		try {
			SettlementAggVO[] aggVO = null;
			aggVO = getSelectedAggVO();
			int len = aggVO == null ? 0 : aggVO.length;
			CheckException.checkArgument(len != 1, nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("payment","UPPpayment-000075")/*@res "��ѡ��һ�ŵ���"*/);
			SettlementAggVO selectedAggVO = aggVO[0];
			new LinkSearchHandler(selectedAggVO, getList()).searchAuditCondition();
		}catch(BusinessException e) {
			Logger.error(e.getMessage(), e);
			getList().showErrorMessage(e.getMessage());
		}

	}

	private void onBoRegBill() {

		try {
//			SettlementAggVO[] aggVO = null;
//			aggVO = getSelectedAggVO();
//			int len = aggVO == null ? 0 : aggVO.length;
//			if (len != 1) {
//				throw new BusinessException("��ѡ��һ�ŵ���");
//			}
//			SettlementHeadVO head = (SettlementHeadVO) aggVO[0].getParentVO();
			new FtsPJHandler().doPjRegister(getList(), getList().getPkCorp());
		}catch(BusinessException e) {
			Logger.error(e.getMessage(), e);
			getList().showErrorMessage(e.getMessage());
		}
	}

	private void onBoDocmng() {

		try {
			SettlementAggVO[] aggVO = null;
			aggVO = getSelectedAggVO();
			int len = aggVO == null ? 0 : aggVO.length;
			CheckException.checkArgument(len != 1, nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("payment","UPPpayment-000075")/*@res "��ѡ��һ�ŵ���"*/);
			SettlementHeadVO head = (SettlementHeadVO) aggVO[0].getParentVO();
			new DocumentManager().handle(getList(), head, false);
		} catch (BusinessException e) {
			Logger.error(e.getMessage(), e);
			getList().showErrorMessage(e.getMessage());
		}

	}

	private void onBoMakeBill() {

		try {
			SettlementAggVO[] aggVOs = getStateSelectedVOs();
			if(aggVOs.length > 1) {
				List<SettlementBodyVO> bodyList = new ArrayList<SettlementBodyVO>();
				for(SettlementAggVO aggVO : aggVOs) {
					SettlementBodyVO[] bodys = (SettlementBodyVO[]) aggVO.getChildrenVO();
					for(SettlementBodyVO body : bodys) {
						bodyList.add(body);
					}
				}

				InterfaceLocator.getInterfaceLocator().getValidate().validateBodys(CmpUtils.covertListToArrays(bodyList, SettlementBodyVO.class));
			}
			new MakeBillHandler(getList().getPkCorp()).makeBill4ListHandler(aggVOs, getList());
		} catch (BusinessException e) {
			Logger.error(e.getMessage(), e);
			getList().showErrorMessage(e.getMessage());
		}

	}

	private void onBoCombinSettle() {

		List<SettlementAggVO> aggList = new ArrayList<SettlementAggVO>();
		List<SettlementHeadVO> headList = new ArrayList<SettlementHeadVO>();
		List<SettlementBodyVO> bodyList= new ArrayList<SettlementBodyVO>();
		SettlementAggVO aggVO = new SettlementAggVO();
		try{
			SettlementAggVO[] aggs = getSelectedAggVO();
			CAAuthor.INSTANCE.author();
			if(aggs == null || (aggs != null && aggs.length == 1)) {
				throw new BusinessException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("payment","UPPpayment-000076")/*@res "��ѡ����ŵ��ݺϲ�����"*/);
			}
			processList(aggs, headList, bodyList, aggList, aggVO);
			checkStatus(aggs);
			getValidate().validateSettle(CmpUtils.covertArraysToList(aggs));
		}catch(BusinessException e) {
			getList().showErrorMessage(e.getMessage());
			Logger.error(e.getMessage(), e);
			return;
		}
		try {
			GenerateSettleNo gen = new GenerateSettleNo(((SettlementHeadVO)aggList.get(0).getParentVO()).getPk_corp());
			if(!gen.validateSettleNo(CmpUtils.covertListToArrays(headList, SettlementHeadVO.class))) {
				throw new BusinessException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("payment","UPPpayment-000077")/*@res "����ű���ȫ����ͬ������ȫ��Ϊ��"*/);
			}
			}catch(BusinessException e) {
				Logger.error(e.getMessage(), e);
				getList().showErrorMessage(e.getMessage());
				return;
			}
		while (true) {
			try {
				for(SettlementHeadVO head : headList) {
					checkHead(head);
					CheckException.checkArgument(head.getBusistatus() != BusiStatus.Sign.getBillStatusKind(), nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("payment","UPPpayment-000033")/*@res "û��ǩ�ֵĵ��ݲ��ܽ���"*/);
					CheckException.checkArgument(head.getSettlestatus() == SettleStatus.SUCCESSSETTLE.getStatus(), nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("payment","UPPpayment-000036")/*@res "������Ĳ����ٴν���"*/);
//					if (head.getBusistatus() != BusiStatus.Sign.getBillStatusKind()) {
//						throw new BusinessException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("payment","UPPpayment-000033")/*@res "û��ǩ�ֵĵ��ݲ��ܽ���"*/);
//					}
//					if (head.getSettlestatus() != CMPaccStatus.NOACCOUNT.getStatus()) {
//						throw new BusinessException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("payment","UPPpayment-000036")/*@res "������Ĳ����ٴν���"*/);
//					}
					head.setPk_executor(getList().getPerson());
					head.setSetlledate(getList().getDate());
					head.setLastupdatedate(getList().getDate());
					head.setLastupdater(getList().getPerson());
				}
				if(headList.size() > 1) {
					getValidate().validateBodys(bodyList.toArray(new SettlementBodyVO[0]));
				}
				getService().handleCombinSettle(aggList, true, getList().getDate(), getList().getPerson());
				refesh();
				break;
			} catch (CmpAuthorizationException exp) {
				AccountExceptionHandler handler = new AccountExceptionHandler(getList());
				handler.setAggVO(aggVO);
				Map<String, String> map = handler.handleException1(exp);
				if (map.size() != 0) {
					break;
				}
			} catch (BusinessException e) {
				Logger.error(e.getMessage(), e);
				getList().showErrorMessage(e.getMessage());
				break;
			}
		}

	}

	private void checkStatus(SettlementAggVO[] aggs) throws BusinessException {
		Set<Integer> set = new HashSet<Integer>();
		for(SettlementAggVO agg : aggs) {
			SettlementHeadVO head = (SettlementHeadVO) agg.getParentVO();
			if(head.getSettlestatus() != SettleStatus.HANDPAYAG.getStatus()) {
				set.add(head.getSettlestatus());
			}
		}
		if(set.size() == 1) {
			Integer status = set.iterator().next();
			CheckException.checkArgument(!(status == SettleStatus.NONESETTLE.getStatus() || status == SettleStatus.PAYFAIL.getStatus()), "����δ����״̬�ĵ��ݲ��ܺϲ�����");
		}else if(set.size() == 2) {
			CheckException.checkArgument(!(set.contains(SettleStatus.NONESETTLE.getStatus()) && set.contains(SettleStatus.PAYFAIL.getStatus())), "����δ����״̬�ĵ��ݲ��ܺϲ�����");
		}else {
			throw new BusinessException("����δ����״̬�ĵ��ݲ��ܺϲ�����");
		}
//		CheckException.checkArgument(!(set.size() == 1 || set.size() == 2), "����δ����״̬�ĵ��ݲ��ܺϲ�����");
//		CheckException.checkArgument(!(set.contains(SettleStatus.NONESETTLE.getStatus()) && set.contains(SettleStatus.PAYFAIL.getStatus())), "����δ����״̬�ĵ��ݲ��ܺϲ�����");
//		CheckException.checkArgument(set.size() != 1, "����δ����״̬�ĵ��ݲ��ܺϲ�����");
//		CheckException.checkArgument(set.iterator().next() != SettleStatus.NONESETTLE.getStatus(), "����δ����״̬�ĵ��ݲ��ܺϲ�����");
	}

//	private void onBoCommissionGather() throws BusinessException {
//		SettlementAggVO[] aggVO = null;
//		aggVO = getSelectedAggVO();
//		int len = aggVO == null ? 0 : aggVO.length;
//		CheckException.checkArgument(len != 1, nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("payment","UPPpayment-000075")/*@res "��ѡ��һ�ŵ���"*/);
//		Container parent = getList();
//		new CommisionReceiveHandler().handleCommisionReceive(aggVO[0], parent);
//	}

//	private void onBoCommissionPay() throws BusinessException {
//		SettlementAggVO[] aggVO = null;
//		aggVO = getSelectedAggVO();
//		int len = aggVO == null ? 0 : aggVO.length;
//		CheckException.checkArgument(len != 1, nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("payment","UPPpayment-000075")/*@res "��ѡ��һ�ŵ���"*/);
//		CAAuthor.INSTANCE.author4Net();
//		new CommisionPayHandler().handleCommisionPay(aggVO[0],  getList());
//	}

//	private void onBoCancelSettle() {
//		List<SettlementAggVO> aggList = new ArrayList<SettlementAggVO>();
//		List<SettlementHeadVO> headList = new ArrayList<SettlementHeadVO>();
//		List<SettlementBodyVO> bodyList= new ArrayList<SettlementBodyVO>();
//		SettlementAggVO aggVO = new SettlementAggVO();
//		try{
//
//			SettlementAggVO[] aggs = getSelectedAggVO();
//			processList(aggs, headList, bodyList, aggList, aggVO);
//		}catch(BusinessException e) {
//			Logger.error(e.getMessage(), e);
//		}
//
//		while (true) {
//			try {
//				for(SettlementHeadVO head : headList) {
//					head.setLastupdatedate(getList().getDate());
//					head.setLastupdater(getList().getPerson());
//					if (head.getBusistatus() == BusiStatus.HangUp.getBillStatusKind()) {
//						throw new BusinessException("�����Ѿ����𣬽��㵥��������صĲ���");
//					}
//					if (head.getSettlestatus() != SettleStatus.SUCCESSSETTLE.getStatus()) {
//						throw new BusinessException("û�н�����ĵ��ݲ��ܷ�����");
//					}
//					if (head.getPk_ftsbill() != null) {
//						throw new BusinessException("ί���ո���ĵ��ݲ���ȡ������");
//					}
//					SettlementBodyVO[] bodys = (SettlementBodyVO[]) aggVO.getChildrenVO();
//					for (SettlementBodyVO body : bodys) {
//						if (body.getCheckcount() != 0) {
//							throw new BusinessException("�Ѿ��Թ��˵Ľ�����Ϣ������ȡ������");
//						}
//					}
//					head.setPk_executor(null);
//					head.setSetlledate(null);
//					head.setLastupdatedate(getList().getDate());
//					head.setLastupdater(getList().getPerson());
//				}
//				getService().handleCancelSettle(aggList);
//				refesh();
//				break;
//			} catch (CmpAuthorizationException exp) {
//				AccountExceptionHandler handler = new AccountExceptionHandler(getList());
//				handler.setAggVO(aggVO);
//				boolean isPass = handler.handleException(exp);
//				if (!isPass) {
//					break;
//				}
//			} catch (BusinessException e) {
//				Logger.error(e.getClass(), e);
//				getList().showErrorMessage(e.getMessage());
//				break;
//			}
//		}
//	}

	private void onBoCancelSettle() {
		Map<String, String> errMap = CmpUtils.makeMap();

		Data data = new Data();
		int count = 0;
		if(count == 0) {
			try{
				data.setAggs(getSelectedAggVO());
				data.processList();
				data.processMap();
			}catch(BusinessException e) {
				Logger.error(e.getMessage(), e);
				getList().showErrorMessage(e.getMessage());
				return;
			}

			try {
				GenerateSettleNo gen = new GenerateSettleNo(((SettlementHeadVO)data.getAggList().get(0).getParentVO()).getPk_corp());

//				if(!gen.isCanGenerate(CmpUtils.covertListToArrays(data.getHeadList(), SettlementHeadVO.class))) {
//					throw new BusinessException("��Ҫͬһ�����㣬����ͬһ���Ƶ���һ��ȡ������");
//				}
				CheckException.checkArgument(!gen.isCanGenerate(CmpUtils.covertListToArrays(data.getHeadList(), SettlementHeadVO.class)), nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("payment","UPPpayment-000078")/*@res "��Ҫͬһ�����㣬����ͬһ���Ƶ���һ��ȡ������"*/);
			}catch(BusinessException e) {
				Logger.error(e.getMessage(), e);
				getList().showErrorMessage(e.getMessage());
				return;
			}

			for(SettlementHeadVO head: data.getHeadList()) {
				try {
					if (head.getSettlestatus() != SettleStatus.SUCCESSSETTLE.getStatus()) {
						throw new BusinessException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("payment","UPPpayment-000062")/*@res "û�н�����ĵ��ݲ��ܷ�����"*/);
					}
					if (head.getPk_ftsbill() != null) {
						throw new BusinessException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("payment","UPPpayment-000079")/*@res "ί���ո���ĵ��ݲ���ȡ������"*/);
					}
					SettlementAggVO aggVO = data.getMap().get(head.getPrimaryKey());
					List<SettlementAggVO> list = CmpUtils.makeList();
					list.add(aggVO);
					InterfaceLocator.getInterfaceLocator().getValidate().validateCancelSettle(list);
					SettlementBodyVO[] bodys = (SettlementBodyVO[]) data.getAggVO().getChildrenVO();
					for (SettlementBodyVO body : bodys) {
						if (body.getCheckcount() != 0) {
							throw new BusinessException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("payment","UPPpayment-000064")/*@res "�Ѿ��Թ��˵Ľ�����Ϣ������ȡ������"*/);
						}
					}
				}catch(BusinessException e) {
					ExceptionHandler.consume(e);
					errMap.put(head.getPk_settlement(), e.getMessage());
					data.getMap().remove(head.getPk_settlement());
				}
			}
			count ++;
		}

		while(true) {
			try {
				List<SettlementAggVO> aggList = null;
				if(data.getMap().values() != null || data.getMap().values().size() != 0) {
					aggList = new ArrayList<SettlementAggVO>(data.getMap().values());
					if(aggList == null || aggList.size() == 0) {
						break;
					}
				}
				errMap.putAll(getService().handleCancelSettle(aggList, getList().getDate(), getList().getPerson()));
				refesh();
				break;
			}catch(CmpAuthorizationException exp) {
				AccountExceptionHandler handler = new AccountExceptionHandler(getList());
				handler.setAggVO(data.getAggVO());
				Map<String, String> map = handler.handleException1(exp);
				errMap.putAll(map);
				if(errMap.size() != 0) {
					for(String pk : errMap.keySet()) {
						if(data.getMap().containsKey(pk)) {
							data.getMap().remove(pk);
						}
					}
				}
			}catch(BusinessException e) {
				ExceptionHandler.consume(e);
			}
		}

		try {
		if(errMap.size() != 0) {
			Vector<String> v = new Vector<String>();
			for(String pk : errMap.keySet()) {
				data.processMap();
				String pk_billcode = ((SettlementHeadVO)data.getMap().get(pk).getParentVO()).getBillcode();
				v.addElement(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("payment","UPPpayment-000080")/*@res "���ݱ��Ϊ"*/ + pk_billcode + nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("payment","UPPpayment-000081")/*@res "ȡ������ʧ��"*/ + errMap.get(pk));
			}
			ShenheLog f = new ShenheLog(getList());
			Double w = new Double((getList().getToolkit().getScreenSize().getWidth() - f.getWidth()) / 2);
			Double h = new Double((getList().getToolkit().getScreenSize().getHeight() - f.getHeight()) / 2);
			f.setLocation(w.intValue(), h.intValue());
			f.f_setText(v);
			f.showModal();
		}
		}catch(BusinessException e) {
			ExceptionHandler.consume(e);
		}
	}

//	private void onBoSettle() {
//		Map<String, String> errMap = CmpUtils.makeMap();
//		List<SettlementAggVO> aggList = new ArrayList<SettlementAggVO>();
//		List<SettlementHeadVO> headList = new ArrayList<SettlementHeadVO>();
//		List<SettlementBodyVO> bodyList= new ArrayList<SettlementBodyVO>();
//		SettlementAggVO aggVO = new SettlementAggVO();
//		try{
//
//			SettlementAggVO[] aggs = getSelectedAggVO();
//			processList(aggs, headList, bodyList, aggList, aggVO);
//		}catch(BusinessException e) {
//			Logger.error(e.getMessage(), e);
//			getList().showErrorMessage(e.getMessage());
//		}
//
//		while (true) {
//			try {
//				for(SettlementHeadVO head : headList) {
//					head.setLastupdatedate(getList().getDate());
//					head.setLastupdater(getList().getPerson());
//					checkHead(head);
//					if (head.getBusistatus() != BusiStatus.Sign.getBillStatusKind()) {
//						throw new BusinessException("û��ǩ�ֵĵ��ݲ��ܽ���");
//					}
//					if (head.getSettlestatus() == SettleStatus.SUCCESSSETTLE.getStatus()) {
//						throw new BusinessException("������Ĳ����ٴν���");
//					}
//					head.setPk_executor(getList().getPerson());
//					head.setSetlledate(getList().getDate());
//					head.setLastupdatedate(getList().getDate());
//					head.setLastupdater(getList().getPerson());
//				}
//				getService().handleSettle(aggList, false);
//				refesh();
//				break;
//			} catch (CmpAuthorizationException exp) {
//				AccountExceptionHandler handler = new AccountExceptionHandler(getList());
//				handler.setAggVO(aggVO);
//				boolean isPass = handler.handleException(exp);
//				if (!isPass) {
//					break;
//				}
//			} catch (BusinessException e) {
//				Logger.error(e.getMessage(), e);
//				getList().showErrorMessage(e.getMessage());
//				break;
//			}
//		}
//	}


	private void onBoSettle() {
		Map<String, String> errMap = CmpUtils.makeMap();

		Data data = new Data();
		int count = 0;
		if(count == 0) {
			try{

				data.setAggs(getSelectedAggVO());
				CAAuthor.INSTANCE.author();
				data.processList();
				data.processMap();
			}catch(BusinessException e) {
				Logger.error(e.getMessage(), e);
				getList().showErrorMessage(e.getMessage());
				return;
			}

			for(SettlementHeadVO head: data.getHeadList()) {
				try {

					checkHead(head);
					CheckException.checkArgument(head.getBusistatus() != BusiStatus.Sign.getBillStatusKind(), nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("payment","UPPpayment-000033")/*@res "û��ǩ�ֵĵ��ݲ��ܽ���"*/);
					CheckException.checkArgument(head.getSettlestatus() == SettleStatus.SUCCESSSETTLE.getStatus(), nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("payment","UPPpayment-000036")/*@res "������Ĳ����ٴν���"*/);
					SettlementAggVO aggVO = data.getMap().get(head.getPrimaryKey());
					List<SettlementAggVO> list = Lists.newArrayList();
					list.add(aggVO);
					InterfaceLocator.getInterfaceLocator().getValidate().validateSettle(list);
				}catch(BusinessException e) {
					ExceptionHandler.consume(e);
					errMap.put(head.getPk_settlement(), e.getMessage());
					data.getMap().remove(head.getPk_settlement());
				}
			}
			try {

				GenerateSettleNo gen = new GenerateSettleNo(((SettlementHeadVO)data.getAggList().get(0).getParentVO()).getPk_corp());
				CheckException.checkArgument(!gen.isCanGenerate(CmpUtils.covertListToArrays(data.getHeadList(), SettlementHeadVO.class)), nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("payment","UPPpayment-000078")/*@res "��Ҫͬһ�����㣬����ͬһ���Ƶ���һ��ȡ������"*/);
//				if(!CheckException.checkMapIsNull(map)) {
//					SettlementHeadVO[] heads = CmpUtils.covertListToArrays(map.values(), SettlementHeadVO.class);
//					CheckException.checkArgument(!gen.isCanGenerate(heads),
//							"��Ҫͬһ���㣬����ͬһ���Ƶ���һ�����");
//				}
			}catch(BusinessException e) {
				Logger.error(e.getMessage(), e);
				getList().showErrorMessage(e.getMessage());
				return;
			}

			count ++;
		}


		while(true) {
			try {
				List<SettlementAggVO> aggList = null;
				if(data.getMap().values() != null || data.getMap().values().size() != 0) {
					aggList = new ArrayList<SettlementAggVO>(data.getMap().values());
					if(aggList == null || aggList.size() == 0) {
						break;
					}
				}
				errMap.putAll(getService().handleSettle(aggList, false, getList().getDate(), getList().getPerson()));
				refesh();
				break;
			}catch(CmpAuthorizationException exp) {
				AccountExceptionHandler handler = new AccountExceptionHandler(getList());
				handler.setAggVO(data.getAggVO());
				Map<String, String> map = handler.handleException1(exp);
				errMap.putAll(map);
				if(errMap.size() != 0) {
					for(String pk : errMap.keySet()) {
						if(data.getMap().containsKey(pk)) {
							data.getMap().remove(pk);
						}
					}
				}
			}catch(BusinessException e) {
				ExceptionHandler.consume(e);
			}
		}

		try {
		if(errMap.size() != 0) {
			Vector<String> v = new Vector<String>();
			for(String pk : errMap.keySet()) {
				data.processMap();
				String pk_billcode = ((SettlementHeadVO)data.getMap().get(pk).getParentVO()).getBillcode();
				v.addElement(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("payment","UPPpayment-000080")/*@res "���ݱ��Ϊ"*/ + pk_billcode + nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("payment","UPPpayment-000082")/*@res "����ʧ��"*/ + errMap.get(pk));
			}
			ShenheLog f = new ShenheLog(getList());
			Double w = new Double((getList().getToolkit().getScreenSize().getWidth() - f.getWidth()) / 2);
			Double h = new Double((getList().getToolkit().getScreenSize().getHeight() - f.getHeight()) / 2);
			f.setLocation(w.intValue(), h.intValue());
			f.f_setText(v);
			f.showModal();
		}
		}catch(BusinessException e) {
			ExceptionHandler.consume(e);
		}
	}



	private void onBoCancelSign() {
	Map<String, String> errMap = CmpUtils.makeMap();

		Data data = new Data();
		int count = 0;
		if(count == 0) {
			try{

				data.setAggs(getSelectedAggVO());
				data.processList();
				data.processMap();
			}catch(BusinessException e) {
				Logger.error(e.getMessage(), e);
				getList().showErrorMessage(e.getMessage());
				return;
			}

			SettlementHeadVO clone = null;
			for(SettlementHeadVO head : data.getHeadList()) {
			try {
					clone = CmpUtils.CloneObj(head);
					head.setLastupdatedate(getList().getDate());
					head.setLastupdater(getList().getPerson());

					//checkHead(head);
					if(head.getPk_ftsbill() == null && head.getIsapplybill().equals(UFBoolean.FALSE)) {
						getValidate().validateCancelSign(head);
					}

			}catch(BusinessException e) {
				ExceptionHandler.consume(e);
				errMap.put(clone.getPk_settlement(), e.getMessage());
				data.getMap().remove(clone.getPk_settlement());
			}
		}
			count ++;
		}
		while(true) {
			try {
				List<SettlementAggVO> aggList = null;
				if(data.getMap().values() != null || data.getMap().values().size() != 0) {
					aggList = new ArrayList<SettlementAggVO>(data.getMap().values());
					if(aggList == null || aggList.size() == 0) {
						break;
					}
				}
				errMap.putAll(getService().handleCancelSign(aggList, null, null, getList().getDate(), getList().getPerson()));
				refesh();
				break;
			}catch(CmpAuthorizationException exp) {
				AccountExceptionHandler handler = new AccountExceptionHandler(getList());
				handler.setAggVO(data.getAggVO());
				Map<String, String> map = handler.handleException1(exp);
				errMap.putAll(map);
				if(errMap.size() != 0) {
					for(String pk : errMap.keySet()) {
						if(data.getMap().containsKey(pk)) {
							data.getMap().remove(pk);
						}
					}
				}
			}catch(BusinessException e) {
				ExceptionHandler.consume(e);
			}
		}

		try {
			if(errMap.size() != 0) {
				Vector<String> v = new Vector<String>();
				for(String pk : errMap.keySet()) {
					data.processMap();
					String pk_billcode = ((SettlementHeadVO)data.getMap().get(pk).getParentVO()).getBillcode();
					v.addElement(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("payment","UPPpayment-000080")/*@res "���ݱ��Ϊ"*/ + pk_billcode + nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("payment","UPPpayment-000083")/*@res "ȡ��ǩ��ʧ��"*/ + errMap.get(pk));
				}
				ShenheLog f = new ShenheLog(getList());
				Double w = new Double((getList().getToolkit().getScreenSize().getWidth() - f.getWidth()) / 2);
				Double h = new Double((getList().getToolkit().getScreenSize().getHeight() - f.getHeight()) / 2);
				f.setLocation(w.intValue(), h.intValue());
				f.f_setText(v);
				f.showModal();
			}
		}catch(BusinessException e) {
			ExceptionHandler.consume(e);
		}
	}
//		List<SettlementAggVO> aggList = new ArrayList<SettlementAggVO>();
//		List<SettlementHeadVO> headList = new ArrayList<SettlementHeadVO>();
//		List<SettlementBodyVO> bodyList= new ArrayList<SettlementBodyVO>();
//		SettlementAggVO aggVO = new SettlementAggVO();
//		try{
//
//			SettlementAggVO[] aggs = getSelectedAggVO();
//			processList(aggs, headList, bodyList, aggList, aggVO);
//		}catch(BusinessException e) {
//			Logger.error(e.getMessage(), e);
//		}
//
//
//		while (true) {
//			try {
//				for(SettlementHeadVO head : headList) {
//					head.setLastupdatedate(getList().getDate());
//					head.setLastupdater(getList().getPerson());
//					checkHead(head);
//					getValidate().validateCancelSign(head);
//					head.setPk_signer(getList().getPerson());
//					head.setSigndate(getList().getDate());
//					head.setLastupdatedate(getList().getDate());
//					head.setLastupdater(getList().getPerson());
//				}
//				getService().handleCancelSign(aggList, null, null);
//				refesh();
//				break;
//			} catch (CmpAuthorizationException exp) {
//				AccountExceptionHandler handler = new AccountExceptionHandler(getList());
//				handler.setAggVO(aggVO);
//				boolean isPass = handler.handleException(exp);
//				if (!isPass) {
//					break;
//				}
//			} catch (BusinessException e) {
//				Logger.error(e.getMessage(), e);
//				getList().showErrorMessage(e.getMessage());
//				break;
//			}
//		}


	private void onBoSign() {
		Map<String, String> errMap = CmpUtils.makeMap();

		Data data = new Data();
		int count = 0;
		if(count == 0) {
			try{

				data.setAggs(getSelectedAggVO());
				data.processList();
				data.processMap();
			}catch(BusinessException e) {
				Logger.error(e.getMessage(), e);
				getList().showErrorMessage(e.getMessage());
				return;
			}

			SettlementHeadVO clone = null;
			for(SettlementAggVO agVO : data.getAggList()) {
				try {
					SettlementHeadVO head = (SettlementHeadVO)agVO.getParentVO();
					clone = CmpUtils.CloneObj(head);
//					if(head.getIsapplybill().booleanValue() && head.getIspay().booleanValue()) {
//						String pk_busitype = head.getPk_busitype();
//						if(pk_busitype != null) {
//							if(CmpConst.COMMISION_PAY_SET.contains(head.getPk_busitype())) {
//								getInterfaceLocator().getValidate().validaCustomer(agVO);
//							}else if(pk_busitype.equals(CmpConst.INNER_REC_TYPE)) {
//								getInterfaceLocator().getValidate().validateInnerRec(agVO);
//							}
//						}
//					}
				}catch(BusinessException e) {
					ExceptionHandler.consume(e);
					errMap.put(clone.getPk_settlement(), e.getMessage());
					data.getMap().remove(clone.getPk_settlement());
				}
			}
			for(SettlementHeadVO head : data.getHeadList()) {
			try {
				   
				    clone = CmpUtils.CloneObj(head);
					if (head.getBusistatus() == BusiStatus.Sign.getBillStatusKind()) {
						throw new BusinessException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("payment","UPPpayment-000084")/*@res "ǩ�ֹ��ĵ��ݲ�����ǩ��"*/);
					} else if (head.getBusistatus() != BusiStatus.Audit.getBillStatusKind()) {
						throw new BusinessException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("payment","UPPpayment-000085")/*@res "û����˹��ĵ��ݲ���ǩ��"*/);
					}
//					head.setPk_signer(getList().getPerson());
//					head.setSigndate(getList().getDate());
//					head.setLastupdatedate(getList().getDate());
//					head.setLastupdater(getList().getPerson());
				
					//ncm heyl �ô������߼�������ܻᵼ��ƾ֤�ظ����ɡ�
					//�׳��쳣���������ִ�����´�����˻ᵼ���Ƴ�����ȷ�ĵ���VOż���ĳ���ƾ֤�ظ���
//					clone = CmpUtils.CloneObj(head);

			}catch(BusinessException e) {  
				ExceptionHandler.consume(e);
				errMap.put(clone.getPk_settlement(), e.getMessage());
				data.getMap().remove(clone.getPk_settlement());
			}
			}
			count ++;
		}
		while(true) {
			try {
				List<SettlementAggVO> aggList = null;
				if(data.getMap().values() != null || data.getMap().values().size() != 0) {
					aggList = new ArrayList<SettlementAggVO>(data.getMap().values());
					if(aggList == null || aggList.size() == 0) {
						break;
					}
				}
				errMap.putAll(getService().handleSign(aggList, null, null, getList().getDate(), getList().getPerson()));
				refesh();
				break;
			}catch(CmpFpBusinessException ce) {
				int i = MessageDialog.showYesNoDlg(getList(), nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("20040401","UPP20040401-000011")/*@res "��ʾ"*/, ce.getMessage() + nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("20040401","UPP20040401-000024")/*@res "�Ƿ����?"*/);
				if(i == UIDialog.ID_YES) {
					data.getMap().get(ce.getPk_settlemnt()).setHasZjjhCheck(true);
				}else {
					MessageDialog.showErrorDlg(getList(), nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("payment","UPPpayment-000001")/*@res "����"*/, ce.getMessage());
					errMap.put(ce.getPk_settlemnt(), ce.getMessage());
					if(errMap.size() != 0) {
						for(String pk : errMap.keySet()) {
							if(data.getMap().containsKey(pk)) {
								data.getMap().remove(pk);
							}
						}
					}
				}
			}catch(BugetAlarmBusinessException be) {
				int i = MessageDialog.showYesNoDlg(getList(), nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("20040401","UPP20040401-000011")/*@res "��ʾ"*/, be.getMessage() + nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("20040401","UPP20040401-000024")/*@res "�Ƿ����?"*/);
				if(i == UIDialog.ID_YES) {
					data.getMap().get(be.getPk_settlemnt()).setBudgetCheck(true);
				}else {
					MessageDialog.showErrorDlg(getList(), nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("payment","UPPpayment-000001")/*@res "����"*/, be.getMessage());
					errMap.put(be.getPk_settlemnt(), be.getMessage());
					if(errMap.size() != 0) {
						for(String pk : errMap.keySet()) {
							if(data.getMap().containsKey(pk)) {
								data.getMap().remove(pk);
							}
						}
					}
				}
			}catch(CmpAuthorizationException exp) {
				AccountExceptionHandler handler = new AccountExceptionHandler(getList());
				handler.setAggVO(data.getAggVO());
				Map<String, String> map = handler.handleException1(exp);
				errMap.putAll(map);
				if(errMap.size() != 0) {
					for(String pk : errMap.keySet()) {
						if(data.getMap().containsKey(pk)) {
							data.getMap().remove(pk);
						}
					}
				}
			}catch(ErmException ermE) {
				int i = MessageDialog.showYesNoDlg(getList(), nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("20040401","UPP20040401-000011")/*@res "��ʾ"*/, ermE.getMessage() + nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("20040401","UPP20040401-000024")/*@res "�Ƿ����?"*/);
				if(i == UIDialog.ID_YES) {
					data.getMap().get(ermE.getPk_settlement()).setErmCheck(true);
				}else {
					MessageDialog.showErrorDlg(getList(), nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("payment","UPPpayment-000001")/*@res "����"*/, ermE.getMessage());
					errMap.put(ermE.getPk_settlement(), ermE.getMessage());
					if(errMap.size() != 0) {
						for(String pk : errMap.keySet()) {
							if(data.getMap().containsKey(pk)) {
								data.getMap().remove(pk);
							}
						}
					}
				}
			}catch(BusinessException e) {
				ExceptionHandler.consume(e);
			}
		}

		try {
			if(errMap.size() != 0) {
				Vector<String> v = new Vector<String>();
				for(String pk : errMap.keySet()) {
					data.processMap();
					String pk_billcode = ((SettlementHeadVO)data.getMap().get(pk).getParentVO()).getBillcode();
					v.addElement(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("payment","UPPpayment-000080")/*@res "���ݱ��Ϊ"*/ + pk_billcode + nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("payment","UPPpayment-000086")/*@res "ǩ��ʧ��"*/ + errMap.get(pk));
				}
				ShenheLog f = new ShenheLog(getList());
				Double w = new Double((getList().getToolkit().getScreenSize().getWidth() - f.getWidth()) / 2);
				Double h = new Double((getList().getToolkit().getScreenSize().getHeight() - f.getHeight()) / 2);
				f.setLocation(w.intValue(), h.intValue());
				f.f_setText(v);
				f.showModal();
			}
		}catch(BusinessException e) {
			ExceptionHandler.consume(e);
		}
	}

	private void processList(SettlementAggVO[] aggs, List<SettlementHeadVO> headList, List<SettlementBodyVO> bodyList, List<SettlementAggVO> aggList, SettlementAggVO aggVO) throws BusinessException {
		for (SettlementAggVO agg : aggs) {

			if (agg.getParentVO() == null && agg.getChildrenVO() == null) {
				throw new BusinessException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("payment","UPPpayment-000003")/*@res "��ѡ�����ĵ���"*/);
			}
			if (agg.getChildrenVO() == null) {
				agg.setChildrenVO(CmpUtils.covertListToArrays(getRuntime().getShowMap().get(agg.getParentVO().getPrimaryKey()).getBodys(), SettlementBodyVO.class));
			}
			SettlementHeadVO head = (SettlementHeadVO)agg.getParentVO();
			SettlementBodyVO[] bodys = (SettlementBodyVO[])agg.getChildrenVO();

			headList.add(head);
			for(SettlementBodyVO body : bodys) {
				bodyList.add(body);
			}
			aggList.add(agg);
		}
		aggVO.setParentVO(headList.get(0));
		aggVO.setChildrenVO(bodyList.toArray(new SettlementBodyVO[0]));
	}

	private SettlementAggVO getSelectedVO() throws BusinessException {
		int row = getList().getBillListPanel().getHeadTable().getSelectedRow();
		SettlementAggVO aggVO = null;
		if (!isDoubleClieck()) {
			aggVO = (SettlementAggVO) getList().getBillListPanel().getBillListData()
					.getBillValueVO(row, SettlementAggVO.class.getName(), SettlementHeadVO.class.getName(),
							SettlementBodyVO.class.getName());
			if(aggVO.getChildrenVO() == null) {
				aggVO.setChildrenVO(CmpUtils.covertListToArrays(getRuntime().getShowMap().get(aggVO.getParentVO().getPrimaryKey()).getBodys(), SettlementBodyVO.class));
			}
		} else {
			setDoubleClieck(false);
			aggVO = (SettlementAggVO) getList().getBillListPanel().getBillListData()
					.getBillValueVO(getSelectRow(), SettlementAggVO.class.getName(), SettlementHeadVO.class.getName(),
							SettlementBodyVO.class.getName());
			if(aggVO.getChildrenVO() == null) {
				aggVO.setChildrenVO(CmpUtils.covertListToArrays(getRuntime().getShowMap().get(aggVO.getParentVO().getPrimaryKey()).getBodys(), SettlementBodyVO.class));
			}
		}
		SettlementBodyVO[] bodys = (SettlementBodyVO[]) aggVO.getChildrenVO();
		for(SettlementBodyVO body : bodys) {
			if(body.getPk_oppaccount() != null) {
				body.setOppaccount(null);
				body.setOppbank(null);
			}
		}
		aggVO.setChildrenVO(bodys);
		return aggVO;
	}


	public boolean isDoubleClieck() {
		return isDoubleClieck;
	}

	public void setDoubleClieck(boolean isDoubleClieck) {
		this.isDoubleClieck = isDoubleClieck;
	}

	public int getSelectRow() {
		return selectRow;
	}

	public void setSelectRow(int selectRow) {
		this.selectRow = selectRow;
	}

	public void refesh() throws BusinessException {
//		���������������沢��ˢ�½���

		List<String> idList = new ArrayList<String>();
		List<SettlementHeadVO> headList = Lists.newArrayList(getRuntime().getBufferMap().values());
		for(SettlementHeadVO head : headList) {
			idList.add(head.getPrimaryKey());
		}
		headList.clear();
		getRuntime().getBufferMap().clear();
		getRuntime().getShowMap().clear();
//		getRuntime().getTransfer().setTempMap(null);
//		getRuntime().getTransfer().setMap(null);
		headList = getService().findAggVOsByHeadIds(idList);

//		for(SettlementHeadVO head : headList) {
//			getRuntime().getBufferMap().put(head.getPrimaryKey(), head);
//		}

		CMPFactory.createAlgorithm(CmpConst.DEFAULT).dealBufferMap(getRuntime().getShowMap(), headList,getRuntime().getBufferMap());
		sortHeads(headList);
		getList().processHeadList(headList);
		getList().getBillListPanel().setHeaderValueVO(null);
		getList().getBillListPanel().setHeaderValueVO(CmpUtils.covertListToArrays(headList, SettlementHeadVO.class));
		execLoadFormula();
	}

	private void checkHead(SettlementHeadVO head) throws BusinessException {
		if (head.getBusistatus() == BusiStatus.HangUp.getBillStatusKind()) {
			throw new BusinessException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("payment","UPPpayment-000034")/*@res "�����Ѿ����𣬽��㵥��������صĲ���"*/);
		}
		if (head.getSettlestatus() == SettleStatus.SUCCESSSETTLE.getStatus()) {
			throw new BusinessException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("payment","UPPpayment-000035")/*@res "�Ѿ�������ɵĲ�������������"*/);
		}


	}

	private TemplateInfo getTempinfo() {
		if (tempinfo == null) {
			tempinfo = new TemplateInfo();
			tempinfo.setPk_Org(ClientEnvironment.getInstance().getCorporation().getPrimaryKey());
			tempinfo.setCurrentCorpPk(ClientEnvironment.getInstance().getCorporation().getPrimaryKey());
			tempinfo.setFunNode("20040401");
			tempinfo.setUserid(ClientEnvironment.getInstance().getUser().getPrimaryKey());
			CompositeMetaVO composevo = new CompositeMetaVO();

			composevo.setActiveFieldCode("cmp_detail.tradertype");
			composevo.setPassiveFieldCode("cmp_detail.pk_trader");
			tempinfo.addComposeMetaVO(composevo);
		}
		return tempinfo;
	}

	private SettlementListUI getList() {
		return (SettlementListUI)getRuntime().getListUI();
	}

	public SettlementBodyVO getBody() {
		return body;
	}

	public void setBody(SettlementBodyVO body) {
		this.body = body;
	}

	private SettlementBodyVO getSelectedBody() throws BusinessException {
		int selectedRow = getList().getBillListPanel().getBodyTable("cmp_detail").getSelectedRow();
		if(selectedRow == -1) {
			throw new BusinessException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("payment","UPPpayment-000071")/*@res "��ѡ�����ı�����"*/);
		}
		SettlementBodyVO body = (SettlementBodyVO)getList().getBillListPanel().getBodyBillModel("cmp_detail").getBodyValueRowVO(selectedRow, SettlementBodyVO.class.getName());
		if(body.getPk_oppaccount() != null) {
			body.setOppaccount(null);
			body.setOppbank(null);
		}
		return body;
	}

	public SettlementAggVO getAggVO() {
		return aggVO;
	}

	public void setAggVO(SettlementAggVO aggVO) {
		this.aggVO = aggVO;
	}

	public SettleQueryDlg getDlg() {
		if(dlg == null) {
			dlg = new SettleQueryDlg(getList(),getNormalPanel(),getTempinfo(),true,false);
			dlg.setNormalPanel(getNormalPanel());
			dlg.setVisibleNormalPanel(true);
			dlg.setVisibleUserDefPanel(true);
			isDlg = true;
		}
		return dlg;
	}

	public SettleNormalPanel getNormalPanel() {
		if(normalPanel == null) {
			normalPanel = new SettleNormalPanel();


		}
		return normalPanel;
	}

	public SettleConditionVO getConditionVO() {
		conditionVO = new SettleConditionVO();
		conditionVO.setPk_corp(getNormalPanel().getCorpRef().getPk_corp());
		conditionVO.setSyscode((Integer)getNormalPanel().getSystemCombox().getSelectdItemValue());

		conditionVO.setBusistatus((Integer)getNormalPanel().getBillStatusCombox().getSelectdItemValue());
		conditionVO.setVoucherStatus((Integer)getNormalPanel().getVoucherStatus().getSelectdItemValue());
		return conditionVO;
	}

	private String getSql(String wheresql, SettleConditionVO conditionVO) {
		String pk_corp = conditionVO.getPk_corp();
		int sys = conditionVO.getSyscode();
		int status = conditionVO.getBusistatus();
		int voucherStatus = conditionVO.getVoucherStatus();
		setVoucher(null);
		StringBuffer sb = new StringBuffer();
//		String issettleeffect = voucherStatus == 1 ? "Y" : "N";

		sb.append(" cmp_detail.pk_corp = '").append(pk_corp).append("' and cmp_settlement.pk_corp = '").append(pk_corp).append("'").append(" and cmp_settlement.pk_tradetype <> 'DR'")
		.append(" and cmp_settlement.dr = 0 and cmp_detail.dr = 0");
		if(sys != 0) {
			String syscode = null;
			if(sys == 1) {
				syscode = "arap";
			}else if(sys == 2) {
				syscode = "cmp";
			}else if(sys == 3) {
				syscode = "erm";
			}
			sb.append(" and cmp_settlement.systemcode = '").append(syscode).append("'");
		}
		if(status != 0) {
			if(status == 1) {//�ݴ�
				sb.append(" and cmp_settlement.busistatus = ").append(status);
			}else if(status == 2) {//δ���
				sb.append(" and cmp_settlement.busistatus in (1,2)");
			}else if(status == 3) {//�����
				sb.append(" and cmp_settlement.aduitstatus = 1");
			}else if(status == 4) {//�����
				sb.append(" and cmp_settlement.busistatus in (3,4)");
			}else if(status == 5) {//δǩ��
				sb.append(" and cmp_settlement.busistatus in (1,2,3)");
			}else if(status == 6){//��ǩ��
				sb.append(" and cmp_settlement.busistatus = 3");
			}else if(status == 7) {//��ǩ��
				sb.append(" and cmp_settlement.busistatus = 4");
			}else if(status == 8) {
				sb.append(" and cmp_settlement.isbusieffect = 'N'");
			}else if(status == 9) {
				sb.append(" and cmp_settlement.isbusieffect = 'Y'");
			}else if(status == 10) {//δ����
				sb.append(" and cmp_settlement.settlestatus = 0");
			}else if(status == 11) {//�տ���
				sb.append(" and cmp_settlement.settlestatus = 3");
			}else if(status == 12) {//������
				sb.append(" and cmp_settlement.settlestatus = 1");
			}else if(status == 13) {//�տ�ʧ��
				sb.append(" and cmp_settlement.settlestatus = 4");
			}else if(status == 14) {//����ʧ��
				sb.append(" and cmp_settlement.settlestatus = 2");
			}else if(status == 15) {//������
				sb.append(" and cmp_settlement.busistatus = 4 and cmp_settlement.settlestatus = 0");
			}else if(status == 16) {//
				sb.append(" and cmp_settlement.settlestatus = 5");
			}
		}
		if(voucherStatus != 0) {
//			sb.append(" and cmp_settlement.issettleeffect = '").append(issettleeffect).append("'");
			setVoucher(voucherStatus == 1 ? "Y" : "N");
		}
		if(wheresql == null) {
			return sb.toString();
		}else {
			sb.append(" and ").append(wheresql);
		}

		return sb.toString();
	}

	public String getDefaultSQL() throws BusinessException {
		if(defaultSQL == null) {
			StringBuffer sb = new StringBuffer();
			sb.append(" cmp_detail.pk_corp = '").append(getList().getPkCorp()).append("' and cmp_settlement.pk_corp = '").append(getList().getPkCorp())
			  .append("' and cmp_settlement.busi_billdate >= '").append(getList().getDate().toString()).append("' and cmp_detail.pk_currtype = '")
			  .append(Currency.getLocalCurrPK(getList().getPkCorp())).append("'").append(" and cmp_settlement.busistatus = 3").append(" and cmp_settlement.pk_tradetype <> 'DR'")
			  .append(" and cmp_settlement.dr = 0 and cmp_detail.dr = 0");
			return sb.toString();
		}
		return defaultSQL;
	}

	public void setDefaultSQL(String defaultSQL) {
		this.defaultSQL = defaultSQL;
	}

	public SettleValidate getValidate() {
		if(validate == null) {
			validate = new SettleValidate();
		}
		return validate;
	}

	public SettlementCardUI getCardPanel() {
		if(cardPanel == null) {
			return new SettlementCardUI();
		}
		return cardPanel;
	}

	public void setCardPanel(SettlementCardUI cardPanel) {
		this.cardPanel = cardPanel;
	}

	public UIRuntimeVO getRuntime() {
		if(runtime == null) {
			runtime = new UIRuntimeVO();
		}
		return runtime;
	}

	public void setRuntime(UIRuntimeVO runtime) {
		this.runtime = runtime;
	}


	/**
	 * ����ǩ�֣���ǩ�֣����㣬�������ʱ��������
	 * @author liuzz
	 *
	 */
	class Data {
		private List<SettlementAggVO> aggList;

		private List<SettlementHeadVO> headList;

		private List<SettlementBodyVO> bodyList;

		//ȫ������ļ���
		private SettlementAggVO aggVO;

		//ȫ��Ϊ���Բ�����aggVO
		private Map<String, SettlementAggVO> map;

		private SettlementAggVO[] aggs;


		public void processList() throws BusinessException {
			for (SettlementAggVO agg : getAggs()) {
				agg.setSettle(true);
				if (agg.getParentVO() == null && agg.getChildrenVO() == null) {
					throw new BusinessException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("payment","UPPpayment-000003")/*@res "��ѡ�����ĵ���"*/);
				}
				if (agg.getChildrenVO() == null) {
					agg.setChildrenVO(CmpUtils.covertListToArrays(getRuntime().getShowMap().get(agg.getParentVO().getPrimaryKey()).getBodys(), SettlementBodyVO.class));
				}
				SettlementHeadVO head = (SettlementHeadVO)agg.getParentVO();
				SettlementBodyVO[] bodys = (SettlementBodyVO[])agg.getChildrenVO();

				getHeadList().add(head);
				for(SettlementBodyVO body : bodys) {
					getBodyList().add(body);
				}
				getAggList().add(agg);
			}
			getAggVO().setParentVO(getHeadList().get(0));
			getAggVO().setChildrenVO(getBodyList().toArray(new SettlementBodyVO[0]));
		}

		public void processMap() throws BusinessException {
			for(SettlementAggVO aggVO : getAggs()) {
				getMap().put(aggVO.getParentVO().getPrimaryKey(), aggVO);
			}
		}

		public List<SettlementAggVO> getAggList() {
			if(aggList == null) {
				aggList = CmpUtils.makeList();
			}
			return aggList;
		}

		public void setAggList(List<SettlementAggVO> aggList) {
			this.aggList = aggList;
		}

		public SettlementAggVO getAggVO() {
			if(aggVO == null) {
				aggVO = new SettlementAggVO();
			}
			return aggVO;
		}

		public void setAggVO(SettlementAggVO aggVO) {
			this.aggVO = aggVO;
		}

		public List<SettlementBodyVO> getBodyList() {
			if(bodyList == null) {
				bodyList = CmpUtils.makeList();
			}
			return bodyList;
		}

		public void setBodyList(List<SettlementBodyVO> bodyList) {
			this.bodyList = bodyList;
		}

		public List<SettlementHeadVO> getHeadList() {
			if(headList == null) {
				headList = CmpUtils.makeList();
			}
			return headList;
		}

		public void setHeadList(List<SettlementHeadVO> headList) {
			this.headList = headList;
		}

		public Map<String, SettlementAggVO> getMap() {
			if(map == null) {
				map = CmpUtils.makeMap();
			}
			return map;
		}

		public void setMap(Map<String, SettlementAggVO> map) {
			this.map = map;
		}

		public SettlementAggVO[] getAggs() {
			return aggs;
		}

		public void setAggs(SettlementAggVO[] aggs) {
			this.aggs = aggs;
		}
	}


	public String getVoucher() {
		return voucher;
	}

	public void setVoucher(String voucher) {
		this.voucher = voucher;
	}
	

//	public int getPageSize() {
//		return pageSize;
//	}
//
//	public void setPageSize(int pageSize) {
//		this.pageSize = pageSize;
//	}
}