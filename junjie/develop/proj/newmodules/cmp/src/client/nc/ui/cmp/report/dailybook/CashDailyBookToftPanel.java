/**
 *
 */
package nc.ui.cmp.report.dailybook;


import java.awt.event.ItemEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import nc.bs.framework.common.NCLocator;
import nc.bs.pf.pub.PfDataCache;
import nc.cmp.utils.ArrayUtil;
import nc.cmp.utils.StringUtil;
import nc.itf.cm.prv.CmpConst;
import nc.itf.cmp.bankacc.CMPaccStatus;
import nc.itf.cmp.prv.ICMPAccountQuery;
import nc.ui.cmp.report.reportmanager.ColumnManager;
import nc.ui.cmp.report.reportmanager.ConditionManager;
import nc.ui.cmp.report.reportmanager.DSProcessInfoManager;
import nc.ui.cmp.report.reportuicommon.CommonReportQueryPanel;
import nc.ui.cmp.report.reportuicommon.LinkQueryBillData;
import nc.ui.cmp.report.reportuicommon.PagedDataContainer;
import nc.ui.cmp.report.reportuicommon.ReportFrameForReportTemplet;
import nc.ui.glpub.IParent;
import nc.ui.glpub.IUiPanel;
import nc.ui.pf.change.PfUtilUITools;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.beans.UIComboBox;
import nc.ui.pub.beans.UILabel;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.beans.ValueChangedEvent;
import nc.ui.pub.beans.constenum.DefaultConstEnum;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.IBillItem;
import nc.ui.querytemplate.operator.IOperatorConstants;
import nc.ui.uap.sf.SFClientUtil;
import nc.vo.cmp.report.BalanceKey;
import nc.vo.cmp.report.BalanceQueryVO;
import nc.vo.cmp.report.BusiSystemGetter;
import nc.vo.cmp.report.QueryProcessHelper;
import nc.vo.cmp.report.column.ColumnBasicInfo;
import nc.vo.cmp.report.column.ColumnDataGetterInfo;
import nc.vo.cmp.report.column.ReportColumn;
import nc.vo.cmp.report.condition.SimpleConditionUnit;
import nc.vo.cmp.report.datasetprocess.DataSetProcessType;
import nc.vo.cmp.report.datasetprocess.info.AddBalanceRowProcessInfo;
import nc.vo.cmp.report.datasetprocess.info.BalanceRowsComputeProcessInfo;
import nc.vo.cmp.report.datasetprocess.info.FixAmountPrecisionInfo;
import nc.vo.cmp.report.datasetprocess.info.SubstituteProcessInfo;
import nc.vo.cmp.report.datasetprocess.info.SubtotalProcessInfo;
import nc.vo.cmp.report.inteltool.AppendVO;
import nc.vo.cmp.report.logicdb.FieldUnit;
import nc.vo.cmp.report.logicdb.ReportDataSourceTableInfo;
import nc.vo.cmp.report.selfdefdsprocessor.AddVoucherInfoProcessor;
import nc.vo.glpub.IVoAccess;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.ValueObject;
import nc.vo.pub.lang.UFDate;
import nc.vo.uapbd.bankaccount.BankaccbasVO;

/**
 * �ֽ��ռ���
 * @author jianghao
 * @version V5.5
 * @since V5.5
 */
public class CashDailyBookToftPanel extends ReportFrameForReportTemplet implements IUiPanel {

	private static final long serialVersionUID = 5584354310629606748L;

	private IParent m_parent = null;  //�����uimanager����
	private ButtonObject[] subQuerybuttons = null;  //���鰴ť

	private String[] groupByBalKey = new String[]{BalanceKey.PK_CURRTYPE};
	private PagedDataContainer dataCon = null;
	private boolean hasQueryed = false;  //�Ƿ�ͨ��query()��ѯ��

	private ICMPAccountQuery accountSrv = NCLocator.getInstance().lookup(ICMPAccountQuery.class);
	private BankaccbasVO[] cashAccs = null;  //��Ӧĳ��˾���˻�

	private boolean isFirstTimeLinkedQuery = true;  //�Ƿ��״α�����
	
	/** �ϲ�ͬ���ݷ�¼����� */
//	private static final String[] combineGroupbyCol = new String[]{"date", "corp",
//		"system", "billtype", "billno", "explanation", "currtype", "pk_bill","direction"};
	private static final String[] combineGroupbyCol = new String[]{"date", "corp",
		"system", "billtype", "billno",  "currtype", "pk_bill","direction"};  //wanglei 2014-11-11 �����ݺϲ�ȡ��ժҪά��
	private static final String[] combineSumCol = new String[]{"inamount", "inlocalamount",
		"outamount", "outlocalamount"};



	public CashDailyBookToftPanel() {
		super();
		dataCon = new PagedDataContainer();

	}

	@Override
	public ButtonObject[] createMainFrameButtons() {

		ButtonObject subQueryBtn = new ButtonObject(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("report","UPPreport-000016")/*@res "���鵥��"*/, nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("report","UPPreport-000016")/*@res "���鵥��"*/, 2, "���鵥��");	/*-=notranslate=-*/
		subQueryBtn.setTag("���鵥��");
		subQueryBtn.setEnabled(false);
		ButtonObject viewBtn = new ButtonObject(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("report","UC001-0000021")/*@res "���"*/, nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("report","UC001-0000021")/*@res "���"*/, 2, "���");	/*-=notranslate=-*/
		ButtonObject firstBtn = new ButtonObject(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("report","UCH031")/*@res "��ҳ"*/, nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("report","UCH031")/*@res "��ҳ"*/, 2, "��ҳ");	/*-=notranslate=-*/
		firstBtn.setTag("��ҳ");
		ButtonObject preBtn = new ButtonObject(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("report","UCH033")/*@res "��һҳ"*/, nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("report","UCH033")/*@res "��һҳ"*/, 2, "��һҳ");	/*-=notranslate=-*/
		preBtn.setTag("��һҳ");
		ButtonObject nextBtn = new ButtonObject(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("report","UCH034")/*@res "��һҳ"*/, nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("report","UCH034")/*@res "��һҳ"*/, 2, "��һҳ");	/*-=notranslate=-*/
		nextBtn.setTag("��һҳ");
		ButtonObject endBtn = new ButtonObject(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("report","UCH032")/*@res "ĩҳ"*/, nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("report","UCH032")/*@res "ĩҳ"*/, 2, "ĩҳ");	/*-=notranslate=-*/
		endBtn.setTag("ĩҳ");
		viewBtn.addChildButton(firstBtn);
		viewBtn.addChildButton(preBtn);
		viewBtn.addChildButton(nextBtn);
		viewBtn.addChildButton(endBtn);
		ButtonObject[] buttons = new ButtonObject[5];
		buttons[0] = getQueryBtn();
		buttons[1] = getRefreshBtn();
		buttons[2] = getPrintBtn();
		buttons[3] = subQueryBtn;
		buttons[4] = viewBtn;
		return buttons;
	}




	public void addListener(Object objListener, Object objUserdata) {

	}



	public ButtonObject[] getButtons() {
		return super.getButtons();
	}



	public String getTitle() {
		return nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("report","UPPreport-000076")/*@res "�ֽ��ռ���"*/;
	}





	public void initTempletHeader() throws Exception {
		super.initTempletHeader();
		getAccountBoxInHeader().addItemListener(this);
	}



	private UIComboBox getAccountBoxInHeader()
	{
		return (UIComboBox)getReportTemplet().getHeadItem("account").getComponent();
	}



	public void itemStateChanged(ItemEvent e) {
		super.itemStateChanged(e);
		try {
			if(hasQueryed && e.getSource().equals(getAccountBoxInHeader())
					&& e.getStateChange() == ItemEvent.SELECTED)
			{
				ValueObject[] results = null;
				int selIndex = getAccountBoxInHeader().getSelectedIndex();
				String[] accPk = new String[]{getAccountBoxInHeader().getSelectdItemValue().toString()};
				if(dataCon.getData(selIndex) == null)
				{
					results = queryByPkAccount(accPk);
					dataCon.loadData(results, selIndex);
				}
				//�����л�bug�޸� zhaozh
				for(BankaccbasVO vo : cashAccs){
					if(null != vo && null != accPk[0] 
                       && vo.getPk_bankaccbas().equals(accPk[0]))
					setCurrentDigit(vo.getPk_currtype());
				}
				showData(dataCon.getData(selIndex));
			}
		} catch (Exception e1) {
			handleException(e1);
		}
	}

	/**
	 * @param accPk
	 * @return
	 */
	private ValueObject[] queryByPkAccount(String[] accPk) {
		ValueObject[] results;
		SimpleConditionUnit scu = (SimpleConditionUnit)getConditionManager().getSimpleConditionUnit(pk_accountField, IOperatorConstants.IN, false);
		scu.setValueList(accPk);
		AddBalanceRowProcessInfo addBrPi = (AddBalanceRowProcessInfo)getDSProcessInfoManager().getProcessInfo(DataSetProcessType.ADD_BALANCE_ROW);
		BalanceQueryVO bqvo = addBrPi.getBqvo();
		bqvo.setPks_account(accPk);
		results = queryNoResetByDlg();
		results = processResultVOs(results);
		return results;
	}



	public Object invoke(Object objData, Object objUserData) {
		Object[] condValues = null;
		if(objUserData != null && objUserData.equals("queryFromDailyReport"))
		{
			//Ϊ����������谴ť
			if(subQuerybuttons == null)
			{
				ButtonObject refreshBtn = getRefreshBtn();
				ButtonObject printBtn = getPrintBtn();
				ButtonObject subQueryBtn = new ButtonObject(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("report","UPPreport-000016")/*@res "���鵥��"*/, nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("report","UPPreport-000016")/*@res "���鵥��"*/, 2, "���鵥��");	/*-=notranslate=-*/
				subQueryBtn.setTag("���鵥��");
				subQueryBtn.setPowerContrl(false);
//				subQueryBtn.setEnabled(false);
				//���鰴ť��ݼ���Ӧ�ù������ݿ���ע��Ľڵ㰴ť��ݼ�
				subQueryBtn.setHotKey("K");
				subQueryBtn.setDisplayHotkey("(Ctrl+K)");
				subQueryBtn.setModifiers(2);
				ButtonObject returnBtn = new ButtonObject(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("report","UC001-0000038")/*@res "����"*/, nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("report","UC001-0000038")/*@res "����"*/, 2, "����");	/*-=notranslate=-*/
				returnBtn.setPowerContrl(false);
				returnBtn.setTag("����");
				subQuerybuttons = new ButtonObject[4];
				subQuerybuttons[0] = refreshBtn;
				subQuerybuttons[1] = printBtn;
				subQuerybuttons[2] = subQueryBtn;
				subQuerybuttons[3] = returnBtn;
				setButtons(subQuerybuttons);
			}
			condValues = (Object[])objData;

		}
		subQuery(condValues, objUserData.toString());

		return null;
	}



	public void nextClosed() {

	}



	public void onButtonClicked(ButtonObject bo) {
		if(bo == getQueryBtn() || bo.getTag().equals("ˢ��"))
		{
			//���漸��˳�򡣡���
			hasQueryed = false;
			dataCon.clear();
			if(bo == getQueryBtn())
				onQuery();
			else
				refresh();
			if(getCommonReportQueryPanel().getChkBoxNotShowNoHappenAndBal().isSelected() && dataCon.getPageCount() > 0)
				showData(dataCon.first());  //��ʱonQuery��һ������ǵ�һ���з������Ĺ�˾+�˻�
			dataCon.loadData(getReportData());  //��һҳ����
			hasQueryed = true;
		}
		else if(bo.getTag().equals("ֱ�Ӵ�ӡ"))
			directPrint();
		else if(bo.getTag().equals("ģ���ӡ"))
			templetPrint();
		else if(bo.getTag().equals("���鵥��"))
		{
			//��������IUiPanel�ӿ����飨��Դ���ݱ���ʵ�֣�
			queryBill();
		}
		else if(bo.getTag().equals("����"))
			m_parent.closeMe();
		else
		{
			UIComboBox accountBox = null;
			try {
				accountBox = getAccountBoxInHeader();
			} catch (Exception e) {
				handleException(e);
			}
			if(bo.getTag().equals("��ҳ"))
				accountBox.setSelectedIndex(0);
			else if(bo.getTag().equals("��һҳ"))
				accountBox.setSelectedIndex(accountBox.getSelectedIndex()==0?
						0:accountBox.getSelectedIndex()-1);
			else if(bo.getTag().equals("��һҳ"))
				accountBox.setSelectedIndex(
						accountBox.getSelectedIndex()==accountBox.getItemCount()-1?
								accountBox.getItemCount()-1:accountBox.getSelectedIndex()+1);
			else if(bo.getTag().equals("ĩҳ"))
				accountBox.setSelectedIndex(accountBox.getItemCount()-1);
		}
	}


	private void queryBill()
	{
		String pk_bill = getSelectedRowValue("pk_bill");
		String pk_billType = getSelectedRowValue("pk_billtype").trim();
		String pk_corp = getSelectedRowValue("corp");
		LinkQueryBillData lqbd = new LinkQueryBillData();
		lqbd.setBillID(pk_bill);
		lqbd.setBillType(pk_billType);
		lqbd.setPkOrg(pk_corp);
		String nodeCode = PfUtilUITools.findNodecodeOfBilltype(pk_billType, lqbd);
		SFClientUtil.openLinkedQueryDialog(nodeCode, this, lqbd);
	}


	/**
	 * ���屨����Ҫʵ��
	 */
	public void bodyRowChange(BillEditEvent e) {
		int selected = e.getRow();
		String explanation = (String)getReportData()[selected].getAttributeValue("explanation");
		ButtonObject subQryBtn = null;
		for (int i = 0; i < getButtons().length; i++) {
			if(getButtons()[i].getTag().equals("���鵥��"))
			{
				subQryBtn = getButtons()[i];
				break;
			}
		}
		if(explanation != null && (explanation.indexOf(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("report","UPPreport-000065")/*@res "�ڳ�"*/) > -1
				|| explanation.indexOf(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("report","UPPreport-000006")/*@res "С��"*/) > -1 || explanation.indexOf(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("report","UPPreport-000007")/*@res "�ܼ�"*/) > -1))
			subQryBtn.setEnabled(false);
		else
			subQryBtn.setEnabled(true);
		updateButtons();
	}



	public void showHeaderData() {
		CommonReportQueryPanel panel = getCommonReportQueryPanel();
		UIRefPane corpPanel = (UIRefPane)getReportTemplet().getHeadItem("corpname").getComponent();
		corpPanel.setText(panel.getRefPanelUnit().getText());
		UIRefPane datePanel = (UIRefPane)getReportTemplet().getHeadItem("date").getComponent();
		String dateRange = "";
		if(panel.getRefPanelBeginDate().getText() != null && !panel.getRefPanelBeginDate().getText().trim().equals(""))
			dateRange += panel.getRefPanelBeginDate().getText() + " ---- ";
		if(panel.getRefPanelEndDate().getText() != null && !panel.getRefPanelEndDate().getText().trim().equals(""))
			dateRange += panel.getRefPanelEndDate().getText();
		datePanel.setText(dateRange);

		UIComboBox accountBox = getAccountBoxInHeader();
		accountBox.removeAllItems();
		String[] accPks = panel.getRefPanelCashAcc().getRefPKs();
		DefaultConstEnum[] accounts = null;
		if(accPks != null && accPks.length > 0)
		{
			accPks = filterNoOccurAccount(accPks);
			accounts = new DefaultConstEnum[accPks.length];
			if(accPks != null && accPks.length > 0)
			{
				HashMap<String, String> accPk_AccName_Map = getAccPk_AccName_Map(accPks);
				for (int i = 0; i < accPks.length; i++) {
					accounts[i] = new DefaultConstEnum(accPks[i], accPk_AccName_Map.get(accPks[i]));
				}
			}
		}
		else if(cashAccs != null && cashAccs.length > 0)
		{
			cashAccs = filterNoOccurAccount(cashAccs);
			accounts = new DefaultConstEnum[cashAccs.length];
			for (int i = 0; i < cashAccs.length; i++) {
				accounts[i] = new DefaultConstEnum(cashAccs[i].getPk_bankaccbas(), cashAccs[i].getAccount());//�ĳ���ʾ�˺�
			}
		}
		accountBox.addItems(accounts);
	}

	
	private HashMap<String, String> getAccPk_AccName_Map(String[] accPks) {
		HashMap<String, String> pkNameMap = new HashMap<String, String>();
		BankaccbasVO[] vos = null;
		try {
			vos = accountSrv.findBankBasVOByAccountPks(accPks);
		} catch (BusinessException e) {
			e.printStackTrace();
		}
		if(vos != null)
			for (int i = 0; i < vos.length; i++) {
				pkNameMap.put(vos[i].getPk_bankaccbas(), vos[i].getAccount());//�ĳ���ʾ�˺�
			}
		return pkNameMap;
	}
	

	/**
	 * �����˻�
	 * @param accPks �˻�pk
	 * @return ����޷�������ʾ�������з������ݵ��˻�������ֱ�ӷ��ز���ָ�����˻�
	 */
	private String[] filterNoOccurAccount(String[] accPks) {
		boolean notShowNoHappen = getCommonReportQueryPanel().getChkBoxNotShowNoHappenAndBal().isSelected();
		if(notShowNoHappen)
		{
			ValueObject[] results = null;
			ArrayList<String> accPkList = new ArrayList<String>();
			String[] pks = new String[1];
			int c = 0;
			for (int i = 0; i < accPks.length; i++) {
				pks[0] = accPks[i];
				results = queryByPkAccount(pks);
				if(results != null && results.length > 0)
				{
					dataCon.loadData(results, c++);
					accPkList.add(accPks[i]);
				}
			}
			return accPkList.toArray(new String[0]);
		}
		else
			return accPks;
	}

	//�����˻�vo
	private BankaccbasVO[] filterNoOccurAccount(BankaccbasVO[] accbasVO) {
		boolean notShowNoHappen = getCommonReportQueryPanel().getChkBoxNotShowNoHappenAndBal().isSelected();
		if(notShowNoHappen)
		{
			ValueObject[] results = null;
			ArrayList<BankaccbasVO> accvoList = new ArrayList<BankaccbasVO>();
			String[] pks = new String[1];
			int c = 0;
			for (int i = 0; i < accbasVO.length; i++) {
				pks[0] = accbasVO[i].getPk_bankaccbas();
				results = queryByPkAccount(pks);
				if(results != null && results.length > 0)
				{
					dataCon.loadData(results, c++);
					accvoList.add(accbasVO[i]);
				}
			}
			return accvoList.toArray(new BankaccbasVO[0]);
		}
		else
			return accbasVO;
	}




	public void removeListener(Object objListener, Object objUserdata) {

	}



	public void showMe(IParent parent) {
		parent.getUiManager().removeAll();
		parent.getUiManager().add(this,this.getName());
		m_parent = parent;
	}


	@Override
	public ValueObject[] processResultVOs(ValueObject[] resultVos) {
		if(resultVos == null)
			return null;
		resultVos = super.processResultVOs(resultVos);
		CircularlyAccessibleValueObject[] vos = (CircularlyAccessibleValueObject[])resultVos;
		//CIntelVO�ӹ���������û�취��ֻ�ý�������Ĵ���
		Object expla = null;
		String sys = null;
		Object billType = null;
		String pk_corp = null;
		for (int i = 0; i < vos.length; i++) {
			expla = vos[i].getAttributeValue("explanation");
			if(expla != null && (expla.toString().trim().indexOf(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("report","UPPreport-000073")/*@res "����С��"*/) > -1
					|| expla.toString().trim().indexOf(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("report","UPPreport-000007")/*@res "�ܼ�"*/) > -1))
			{
				vos[i].setAttributeValue("billno", "");
				vos[i].setAttributeValue("currtype", null);
				vos[i].setAttributeValue("system", null);
				vos[i].setAttributeValue("billtype", null);
				vos[i].setAttributeValue("voucherno", null);
			}


			else if(!((AppendVO)((IVoAccess)vos[i]).getUserData()).m_blnIsInit)
			{
				sys = vos[i].getAttributeValue("system").toString();
				billType = vos[i].getAttributeValue("billtype");
				pk_corp = vos[i].getAttributeValue("corp").toString();
				vos[i].setAttributeValue("system",
						BusiSystemGetter.getSysNameByCode(sys));
				if(billType != null)
					vos[i].setAttributeValue("billtype",
								PfDataCache.getBillType(billType.toString().trim()).getBilltypename());
			}
		}
		return resultVos;
	}



	@Override
	public String getFunNode() {
		return "20040709";
	}

	@Override
	public String[] getHideCurrFormatColCodes() {
		return new String[]{"inamount","outamount","balance"};
	}

	@Override
	public void setDataGetterInfo(ColumnDataGetterInfo getterInfo, String key) {
		if("date".equals(key))  //���ڣ�Ĭ������Ϊ��������
			getterInfo.setFieldUnit(billDateField);
		else if("corp".equals(key))  //��˾
			getterInfo.setFieldUnit(pk_corpField);
		else if("system".equals(key))  //ҵ��ϵͳ
			getterInfo.setFieldUnit(systemCodeField);
		else if("billtype".equals(key))  //��������
			getterInfo.setFieldUnit(billTypeField);
		else if("billno".equals(key))  //���ݺ�
			getterInfo.setFieldUnit(billcodeField);
		else if("explanation".equals(key))  //ժҪ
			getterInfo.setFieldUnit(new FieldUnit("cmp_detail", "memo"));
		else if("currtype".equals(key))  //����
			getterInfo.setFieldUnit(pk_currtypeField);
		else if("inamount".equals(key))  //����ԭ��
			getterInfo.setFieldUnit(receiveField);
		else if("inlocalamount".equals(key))  //���뱾��
			getterInfo.setFieldUnit(receiveLField);
		else if("outamount".equals(key))  //֧��ԭ��
			getterInfo.setFieldUnit(payField);
		else if("outlocalamount".equals(key))  //֧������
			getterInfo.setFieldUnit(payLField);
		else if("pkdetail".equals(key)){//��ϸ���������ڱ���ģ����ȡ�����ֶ��Խ�����չ��
			getterInfo.setFieldUnit(pk_detailField);
		}
	}


	protected ReportColumn[] getExtraReportColumns()
	{
		ReportColumn[] rcs = new ReportColumn[3];
		rcs[0] = new ReportColumn();
		rcs[0].setColBasicInfo(new ColumnBasicInfo("pk_bill", true));
		rcs[0].setGetterInfo(new ColumnDataGetterInfo(pk_billField, false, false));
		rcs[1] = new ReportColumn();
		rcs[1].setColBasicInfo(new ColumnBasicInfo("pk_billtype", true));  //Ϊ�����鵥���ٴ�һ��
		rcs[1].setGetterInfo(getColumnManager().getGetterInfoByColKey("billtype"));
		rcs[2] = new ReportColumn();
		rcs[2].setColBasicInfo(new ColumnBasicInfo("direction", true));  //������ϲ�
		rcs[2].setGetterInfo(new ColumnDataGetterInfo(directionField, false, false));
		return rcs;
	}

	@SuppressWarnings("serial")
	public CommonReportQueryPanel createCommonReportQueryPanel() {
		return new CommonReportQueryPanel()
		{
			@Override
			public void addControlsToUI() {
				addToRegularControlPanel(getLabelUnit(), getRefPanelUnit());
				addToRegularControlPanel(getLabelCashAcc(), getRefPanelCashAcc());
				addToRegularControlPanel(getLabelCurr(), getRefPanelCurrency());
				addToRegularControlPanel(getLabelBillStatus(),getCbBoxSignStatus());
				addToRegularControlPanel(getLabelDateType(),getCbBoxDateType());
				addToRegularControlPanel(new UILabel(""), new UILabel("")); //just for layout
				addToRegularControlPanel(getLabelDate(),getRefPanelBeginDate());
				addToRegularControlPanel(getLabelDateTo(),getRefPanelEndDate());
				addToRegularControlPanel(getLabelSystem(), getRefPanelSystem());
				addToRegularControlPanel(getLabelBillType(), getRefPanelBillType());
				addToChkBoxPanel(getChkBoxShowVoucherNo(), getChkBoxNotShowNoHappenAndBal(), getChkBoxCombineSameBill());
				//��Ĭ��ֵ
				/**@author zhaozh 2009-9-17 ����10:34:39 �����ռ�����ʼ����Ϊ���� */
				getRefPanelBeginDate().setText(ce.getDate().toString());
				getRefPanelUnit().setPK(ce.getCorporation().getPrimaryKey());
				getRefPanelUnit().setMultiSelectedEnabled(false);
				UFDate date = ce.getDate();
//				getRefPanelBeginDate().setText(date.getDateBefore(date.getDay()-1).toString());
				getRefPanelEndDate().setText(date.toString());
				//wanglei 2014-11-11 Ĭ�ϰ����ݺϲ���ѯ
				getChkBoxCombineSameBill().setSelected(true);  
			}

			@Override
			public String check() {
				String startDate = getRefPanelBeginDate().getText();
				String endDate = getRefPanelEndDate().getText();
				if(startDate == null || startDate.trim().equals(""))
					return nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("report","UPPreport-000012")/*@res "��ʼ���ڲ���Ϊ�գ�"*/;
				if(StringUtil.isEmptyWithTrim(endDate)){
					return nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("report","UPPreport-000013")/*@res "�������ڲ���Ϊ�գ�"*/;
				}
				if(!(endDate == null || endDate.trim().equals("")) && UFDate.getDate(endDate).before(UFDate.getDate(startDate)))
					return nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("report","UPPreport-000014")/*@res "��ʼ���ڲ����ڽ�������֮��"*/;
				return super.check();
			}

			public void valueChanged(ValueChangedEvent event) {
				Object target = event.getSource();
				if(target.equals(getRefPanelCashAcc())){
					String[] pks = getRefPanelCashAcc().getRefPKs();
					if(pks != null && pks.length > 0)
						getRefPanelCurrency().setEnabled(false);
					else
						getRefPanelCurrency().setEnabled(true);
				}
				else if(target.equals(getRefPanelUnit())){
					getRefPanelCashAcc().setPk_corp(getBaseCorpPk());
				}
				else if(target.equals(getRefPanelCurrency())){
					String[] pks = getRefPanelCurrency().getRefPKs();
					String wherePart = " bd_bankaccbas.ownercorp = '" + getBaseCorpPk() + "' and accclass = 3 ";
					if(pks != null && pks.length > 0)
					{
						String pkInStr = QueryProcessHelper.getInSqlStr(pks, false);
						wherePart = "("+wherePart+")";
						getRefPanelCashAcc().setText("");
						getRefPanelCashAcc().getRefModel().setWherePart(
								wherePart + " and pk_currtype in (" + pkInStr + ")", true);
					}
					else
						getRefPanelCashAcc().getRefModel().setWherePart(wherePart, true);
				}
				else if(target.equals(getRefPanelBeginDate()))
				{
					changeToCmpCreateDate();
				}
			}
		};
	}

	@Override
	public void initConditionManager(ConditionManager manager) {
		//��������ʽ���̬���������ֽ��ռ���
		SimpleConditionUnit condUnit = new SimpleConditionUnit(fundFormField, IOperatorConstants.EQ, true);
		condUnit.setValueList(new Integer[]{new Integer(CmpConst.HAND_CASH)});
		manager.addCondition(condUnit);
		manager.addCondition(isBillRecordCU);
		manager.addCondition(drConditionUnit);
		manager.addCondition(new SimpleConditionUnit(pk_corpField, IOperatorConstants.EQ, false));
		manager.addCondition(new SimpleConditionUnit(pk_currtypeField, IOperatorConstants.IN, false));
		manager.addCondition(new SimpleConditionUnit(pk_accountField, IOperatorConstants.IN, false));
		manager.addCondition(new SimpleConditionUnit(billDateField, IOperatorConstants.GE, false));
		manager.addCondition(new SimpleConditionUnit(billDateField, IOperatorConstants.LE, false));
		manager.addCondition(new SimpleConditionUnit(signDateField,
				IOperatorConstants.GE, false));
		manager.addCondition(new SimpleConditionUnit(signDateField,
				IOperatorConstants.LE, false));
		manager.addCondition(new SimpleConditionUnit(payDateField, IOperatorConstants.GE, false));
		manager.addCondition(new SimpleConditionUnit(payDateField, IOperatorConstants.LE, false));
		manager.addCondition(new SimpleConditionUnit(systemCodeField, IOperatorConstants.IN, false));
		manager.addCondition(new SimpleConditionUnit(billTypeField, IOperatorConstants.IN, false));

		//��ʼ��������
//		manager.addOrderField(billcodeField, 0);
		manager.addOrderField(directionField, 0);
		manager.addOrderField(billcodeField, 1);

	}

	@Override
	public boolean isComplexConditionDlgUsed() {
		return false;
	}

	@Override
	public void resetManagerByDlg(ColumnManager colManager, ConditionManager condManager, DSProcessInfoManager piManager) {
		CommonReportQueryPanel panel = getCommonReportQueryPanel();
		resetColumnManager(colManager, panel);
		resetConditionManager(condManager, panel);
		resetDSProcessInfoManager(piManager, panel);
	}


	/**
	 * @param condManager
	 * @param panel
	 */
	private void resetConditionManager(ConditionManager condManager, CommonReportQueryPanel panel) {
		String[] values = null;
		values = new String[]{panel.getRefPanelUnit().getRefPK()};
		SimpleConditionUnit scu = (SimpleConditionUnit)condManager.getSimpleConditionUnit(pk_corpField, IOperatorConstants.EQ, false);
		scu.setValueList(values);
		scu = (SimpleConditionUnit)condManager.getSimpleConditionUnit(pk_accountField, IOperatorConstants.IN, false);
		values = panel.getRefPanelCashAcc().getRefPKs();
		if(values == null)
		{
			BankaccbasVO[] cashAccs = getCashAccoutsByCorp(panel.getRefPanelUnit().getRefPK(),panel.getRefPanelCurrency().getRefPKs());
			if(cashAccs != null && cashAccs.length > 0)
				scu.setValueList(new String[]{cashAccs[0].getPk_bankaccbas()});  //��ѯ���˻�����Ϊ��һ��
			else
				scu.setValueList(new String[]{"nodata"});
		}
		else
			scu.setValueList(new String[]{values[0]});  //��ֻ���һ���˻�
		scu = (SimpleConditionUnit)condManager.getSimpleConditionUnit(pk_currtypeField, IOperatorConstants.IN, false);
		values = panel.getRefPanelCurrency().getRefPKs();
		scu.setValueList(values);
		resetBillStatusCondition(condManager, ((Integer)panel.getCbBoxSignStatus().getSelectdItemValue()).intValue());

		Object dateType = panel.getCbBoxDateType().getSelectedItem();
		clearDateCondition(condManager);

		if(dateType.equals(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("report","UC000-0000799")/*@res "��������"*/))
			scu = (SimpleConditionUnit)condManager.getSimpleConditionUnit(billDateField,
					IOperatorConstants.GE, false);
		else if(dateType.equals(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("report","UC000-0003103")/*@res "ǩ������"*/))
			scu = (SimpleConditionUnit)condManager.getSimpleConditionUnit(signDateField,
					IOperatorConstants.GE, false);
		else
			scu = (SimpleConditionUnit)condManager.getSimpleConditionUnit(payDateField,
					IOperatorConstants.GE, false);
		if(panel.getRefPanelBeginDate().getText() != null &&
				!panel.getRefPanelBeginDate().getText().trim().equals(""))
			scu.setValueList(new String[]{panel.getRefPanelBeginDate().getText()});
		else
			scu.setValueList(null);
		if(dateType.equals(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("report","UC000-0000799")/*@res "��������"*/))
			scu = (SimpleConditionUnit)condManager.getSimpleConditionUnit(billDateField,
					IOperatorConstants.LE, false);
		else if(dateType.equals(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("report","UC000-0003103")/*@res "ǩ������"*/))
			scu = (SimpleConditionUnit)condManager.getSimpleConditionUnit(signDateField,
					IOperatorConstants.LE, false);
		else
			scu = (SimpleConditionUnit)condManager.getSimpleConditionUnit(payDateField,
					IOperatorConstants.LE, false);
		if(panel.getRefPanelEndDate().getText() != null &&
				!panel.getRefPanelEndDate().getText().trim().equals(""))
			scu.setValueList(new String[]{panel.getRefPanelEndDate().getText()});
		else
			scu.setValueList(null);

		scu = (SimpleConditionUnit)condManager.getSimpleConditionUnit(systemCodeField, IOperatorConstants.IN, false);
		scu.setValueList(panel.getRefPanelSystem().getRefCodes());
		scu = (SimpleConditionUnit)condManager.getSimpleConditionUnit(billTypeField, IOperatorConstants.IN, false);
		scu.setValueList(panel.getRefPanelBillType().getRefPKs());
	}


	/**
	 * @param corp
	 * @param strings
	 * @return
	 */
	private BankaccbasVO[] getCashAccoutsByCorp(String corp, String[] pk_currtypes) {
		try {
			cashAccs = accountSrv.findCashBasVObyCorpId4DataPower(corp);
			List<BankaccbasVO> filterBanks = new ArrayList<BankaccbasVO>();
			if(null == pk_currtypes || pk_currtypes.length < 0)
				return cashAccs;
			List<String> pkcurrtypes = ArrayUtil.changeArray2List(pk_currtypes);
			if(null != pkcurrtypes || pkcurrtypes.size()>0){
				for(BankaccbasVO vo:cashAccs){
					if(pkcurrtypes.contains(vo.getPk_currtype()))
						filterBanks.add(vo);
				}
				cashAccs = ArrayUtil.changeListtovos(filterBanks, BankaccbasVO.class);
			}
		} catch (BusinessException e) {
			handleException(e);
		}
		return cashAccs;
	}

	private void resetDSProcessInfoManager(DSProcessInfoManager piManager, CommonReportQueryPanel panel) {
		AddBalanceRowProcessInfo addBrPi = (AddBalanceRowProcessInfo)piManager.getProcessInfo(DataSetProcessType.ADD_BALANCE_ROW);
		//��̬ƾ֤���У�������
		addBrPi.setStrKey_ColumnIndex_Map(getColumnManager().getBalanceKey_ColIndex_Map());
		addBrPi.setAttributeNames(getColumnManager().getAttributeNames());
		BalanceQueryVO bqvo = addBrPi.getBqvo();
		if(bqvo == null)
		{
			bqvo = new BalanceQueryVO();
			bqvo.setFundForm(CmpConst.HAND_CASH);
			bqvo.setGroupByKey(groupByBalKey);  //��ֹû���κ�group by���ɿ���
			addBrPi.setBqvo(bqvo);
		}

		bqvo.setPks_corp(new String[]{panel.getRefPanelUnit().getRefPK()});
		if(panel.getRefPanelCashAcc().getRefPKs() == null)
			if(cashAccs != null && cashAccs.length > 0)  //��resetConditionManager�в������
				bqvo.setPks_account(new String[]{cashAccs[0].getPk_bankaccbas()});  //��ѯ���˻�����Ϊ��һ��
			else
				bqvo.setPks_account(null);
		else
			bqvo.setPks_account(new String[]{panel.getRefPanelCashAcc().getRefPKs()[0]});  //��ֻ���һ���˻�
		bqvo.setPks_currtype(panel.getRefPanelCurrency().getRefPKs());
		bqvo.setBillStatus(((Integer)panel.getCbBoxSignStatus().getSelectdItemValue())
				.intValue());
		bqvo.setDateType(panel.getCbBoxDateType().getSelectedItem().toString());
		bqvo.setDate(new UFDate(panel.getRefPanelBeginDate().getText().trim()));
		String[] sysPks = panel.getRefPanelSystem().getRefPKs();
		if(sysPks != null)
			bqvo.setSystemCodes(sysPks);
		else
			bqvo.setSystemCodes(null);

		if(panel.getChkBoxShowVoucherNo().isSelected())
		{
			piManager.unRemovedProcessInfo(DataSetProcessType.SELF_DEFINE);
			piManager.setCriticalIndex(2); //����ƾ֤���ں�̨����
		}
		else
		{
			piManager.removeProcessInfo(DataSetProcessType.SELF_DEFINE);
			piManager.setCriticalIndex(1);
		}
	}


	private void clearDateCondition(ConditionManager condManager) {
		((SimpleConditionUnit)condManager.getSimpleConditionUnit(billDateField,
				IOperatorConstants.GE, false)).setValueList(null);
		((SimpleConditionUnit)condManager.getSimpleConditionUnit(signDateField,
				IOperatorConstants.GE, false)).setValueList(null);
		((SimpleConditionUnit)condManager.getSimpleConditionUnit(payDateField,
				IOperatorConstants.GE, false)).setValueList(null);
		((SimpleConditionUnit)condManager.getSimpleConditionUnit(billDateField,
				IOperatorConstants.LE, false)).setValueList(null);
		((SimpleConditionUnit)condManager.getSimpleConditionUnit(signDateField,
				IOperatorConstants.LE, false)).setValueList(null);
		((SimpleConditionUnit)condManager.getSimpleConditionUnit(payDateField,
				IOperatorConstants.LE, false)).setValueList(null);
	}

	private void resetColumnManager(ColumnManager colManager, CommonReportQueryPanel panel) {
		Object dateType = panel.getCbBoxDateType().getSelectedItem();
		ColumnDataGetterInfo getterInfo = colManager.getGetterInfoByColKey("date");
		if(dateType.equals(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("report","UC000-0000799")/*@res "��������"*/))
			getterInfo.setFieldUnit(billDateField);
		else if(dateType.equals(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("report","UC000-0003103")/*@res "ǩ������"*/))
			getterInfo.setFieldUnit(signDateField);
		else
			getterInfo.setFieldUnit(payDateField);

		colManager.removeAllDynamicColumn();
		if(panel.getChkBoxShowVoucherNo().isSelected())
			colManager.addDynamicColumn("voucherno", nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("report","UC000-0000475")/*@res "ƾ֤��"*/, IBillItem.STRING, 5);
		
		boolean isCombineSameBill = panel.getChkBoxCombineSameBill().isSelected();
		ColumnDataGetterInfo[] getterInfos = colManager.getGetterInfoByColKeys(combineGroupbyCol);
		for (int i = 0; i < getterInfos.length; i++) {
			getterInfos[i].setGroupByField(isCombineSameBill);
		}
		ColumnDataGetterInfo[] getterInfos1 = colManager.getGetterInfoByColKeys(combineSumCol);
		for (int i = 0; i < getterInfos1.length; i++) {
			getterInfos1[i].setSumField(isCombineSameBill);
		}
	}


	@Override
	public void initDataSourceTableInfo(ReportDataSourceTableInfo dataSourceTableInfo) {
//		dataSourceTableInfo.setMainTable("cmp_settlebill_b");//������Ϊ������㵥��¼��
//		JoinUnit ju = new JoinUnit(settlebill_bPk_busisettle_refField, pk_busisettle_refField, JoinUnit.INNER);
//		dataSourceTableInfo.addJoinUnit(ju);
		dataSourceTableInfo.setMainTable("cmp_detail");
	}

	@Override
	public void initDSProcessInfoManager(DSProcessInfoManager processInfoManager) {
		//��һ��ȡ�ڳ�������ѯ������resetManagerByDlg����������
		AddBalanceRowProcessInfo addBrPi = new AddBalanceRowProcessInfo();
		processInfoManager.addProcessInfo(DataSetProcessType.ADD_BALANCE_ROW, addBrPi);
		//�ڶ������Զ��崦�����ݵ���pk����ƾ֤��
		getDSProcessorFactory().registerSelfDefProcessor(new AddVoucherInfoProcessor());//ע���Զ��崦����
		SubstituteProcessInfo sttPi0 = new SubstituteProcessInfo();
		sttPi0.setCorpCol("corp");
		ArrayList<String[]> substituteCol = new ArrayList<String[]>();
		substituteCol.add(new String[]{"voucherno"});
		sttPi0.setSubstituteCol(substituteCol);
		ArrayList<String[]> joinCol = new ArrayList<String[]>();
		joinCol.add(new String[]{"pk_bill"});
		sttPi0.setJoinCol(joinCol);
		processInfoManager.addProcessInfo(DataSetProcessType.SELF_DEFINE, sttPi0);
		
		//�������ǰ���������Ҿ���
		FixAmountPrecisionInfo fapPi = new FixAmountPrecisionInfo();
		fapPi.setLocalAmountCols(new String[]{"inlocalamount", "outlocalamount"});
		processInfoManager.addProcessInfo(DataSetProcessType.FIX_AMOUNT_PRECISION, fapPi);
		
		//������������
		BalanceRowsComputeProcessInfo brcPi = new BalanceRowsComputeProcessInfo();
		brcPi.setSortColumns(new String[]{"date"});
		brcPi.setBalanceColumns(new String[]{"balance", "localbalance"});
		String[][] express = new String[][]{
				new String[]{"balance","+","inamount","-","outamount"},
				new String[]{"localbalance","+","inlocalamount","-","outlocalamount"}};
		brcPi.setComputeExpression(express);
		brcPi.setRowInnerCompute(false);
		processInfoManager.addProcessInfo(DataSetProcessType.BALANCE_ROWS_COMPUTE, brcPi);

		//���Ĳ�С��
		SubtotalProcessInfo stPi = new SubtotalProcessInfo();
		stPi.setSubtotalByCol(new String[]{"date"});
		stPi.setLimitSumGen(0);
		stPi.setSubtotalObject(new String[]{"inamount", "inlocalamount", "outamount", "outlocalamount"});
		stPi.setSubtotalShowCol(new String[]{"explanation"});
		stPi.setShowText(new String[]{nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("report","UPPreport-000073")/*@res "����С��"*/});
		stPi.setRequireOutputDetail(true);
		stPi.setTotalSum(true);
		processInfoManager.addProcessInfo(DataSetProcessType.SUBTOTAL, stPi);

//		InteComputeProcessInfo icPi = new InteComputeProcessInfo();
//		icPi.setBalanceColumns(new String[]{"balance", "localbalance"});
//		String[][] express = new String[][]{
//		new String[]{"balance","+","inamount","-","outamount"},
//		new String[]{"localbalance","+","inlocalamount","-","outlocalamount"}};
//		icPi.setComputeExpression(express);
//		icPi.setRowInnerCompute(false);
//		SubtotalProcessInfo stPi = new SubtotalProcessInfo();
//		stPi.setSubtotalByCol(new String[]{"date"});
//		stPi.setLimitSumGen(0);
//		stPi.setSubtotalObject(new String[]{"inamount", "inlocalamount", "outamount", "outlocalamount"});
//		stPi.setSubtotalShowCol(new String[]{"explanation"});
//		stPi.setShowText(new String[]{"��С��"});
//		stPi.setRequireOutputDetail(true);
//		icPi.setStPi(stPi);
//		processInfoManager.addProcessInfo(DataSetProcessType.INTEGRATED_COMPUTE, icPi);

	}

	@Override
	protected String[][] getBalanceColCodeAndKey() {
		return new String[][]{new String[]{"corp", BalanceKey.PK_CORP}, new String[]{"currtype", BalanceKey.PK_CURRTYPE},
				new String[]{"balance", BalanceKey.PRIMAL_AMOUNT}, new String[]{"localbalance", BalanceKey.LOCAL_AMOUNT}};
	}

	@Override
	public String[] getQryObjectCodeColCodes() {
		return null;
	}

	@Override
	public String[] getQryObjectKeyColCodes() {
		return null;
	}

	@Override
	public String[] getQryObjectNameColCodes() {
		return null;
	}


	/**
	 * @author zhaozh 2009-5-25 ����10:56:58
	 * ������Դϵͳ�����ֽ��ռ���
	 */
	public void resetManagerForSubQuery(ColumnManager colManager, ConditionManager condManager,
			DSProcessInfoManager piManager, Object[] condValues, String queryName) {
		if(isFirstTimeLinkedQuery )
		{
			isFirstTimeLinkedQuery = false;
			SimpleConditionUnit condUnit = new SimpleConditionUnit(fundFormField, IOperatorConstants.EQ, true);
			condUnit.setValueList(new Integer[]{new Integer(CmpConst.HAND_CASH)});
			condManager.addCondition(condUnit);
			condManager.addCondition(drConditionUnit);
			condManager.addCondition(isBillRecordCU);
		}
		CommonReportQueryPanel panel = getCommonReportQueryPanel();
		panel.getRefPanelUnit().setPK(condValues[0]);  //��˾pk
		panel.getRefPanelCashAcc().setPK(condValues[1]); //�ֽ��˻�pk
		panel.getRefPanelCurrency().setPK(condValues[2]); //����pk
		int selectedIndex = 0;
		int status = ((Integer)condValues[3]).intValue(); //����״̬
		if(status == CMPaccStatus.SAVEACCOUNT.getStatus())
			selectedIndex = 0;
		else if(status == CMPaccStatus.SIGNACCOUNT.getStatus())
			selectedIndex = 1;
		else if(status == CMPaccStatus.SUCCESSACCOUNT.getStatus())
			selectedIndex = 2;
		panel.getCbBoxSignStatus().setSelectedIndex(selectedIndex);
		panel.getCbBoxDateType().setSelectedItem(condValues[4]);  //��������
		panel.getRefPanelBeginDate().setText((String)condValues[5]); //��ʼ����
		panel.getRefPanelEndDate().setText((String)condValues[6]); //��ֹ����
		if (condValues[8] != null) {
			panel.getRefPanelBillType().setPK(condValues[8]);
		}
		if(condValues[9] != null)
			panel.getRefPanelSystem().setPKs((String[]) condValues[9]);
		else
			panel.getRefPanelSystem().setPK(null);
		resetManagerByDlg(colManager, condManager, piManager);
	}

	@Override
	public void showHeaderDataForSubQuery(Object[] condValues, String queryName) {
		showHeaderData();
		int len = condValues.length;
		if(condValues[len-1] != null && condValues[len-1].equals(CURR_FORMAT))
			getFormatComboInHeader().setSelectedItem(condValues[len-1]);
	}

}