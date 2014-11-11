/**
 *
 */
package nc.ui.cmp.report.dailybook;


import java.awt.event.ItemEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import nc.bs.framework.common.NCLocator;
import nc.bs.pf.pub.PfDataCache;
import nc.cmp.utils.StringUtil;
import nc.itf.cm.prv.CmpConst;
import nc.itf.cmp.bankacc.CMPaccStatus;
import nc.itf.cmp.prv.ICMPAccountQuery;
import nc.itf.uap.bd.corp.ICorpQry;
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
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UICheckBox;
import nc.ui.pub.beans.UIComboBox;
import nc.ui.pub.beans.UILabel;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.beans.ValueChangedEvent;
import nc.ui.pub.beans.constenum.DefaultConstEnum;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.formulaparse.FormulaParse;
import nc.ui.pub.report.ReportItem;
import nc.ui.querytemplate.operator.IOperatorConstants;
import nc.ui.uap.sf.SFClientUtil;
import nc.ui.uif2.bankaccount.IBankAccConstant;
import nc.vo.arap.exception.ExceptionHandler;
import nc.vo.bd.CorpVO;
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
 * 银行存款日记账
 * @author jianghao
 * @version V5.5
 * @since V5.5
 * 2008-8-12
 * modifier by zhaozh on 2009-9-1 分页实现
 */
public class BankDailyBookToftPanel extends ReportFrameForReportTemplet implements IUiPanel {

	private static final long serialVersionUID = 5584354310629606748L;

	private IParent m_parent = null;  //报表的uimanager对象
	private ButtonObject[] subQuerybuttons = null;  //联查按钮

	private String[] groupByBalKey = new String[]{BalanceKey.PK_CURRTYPE};
	private PagedDataContainer dataCon = null;
	private boolean hasQueryed = false;  //是否通过query()查询过

	private ICMPAccountQuery bankSrv = NCLocator.getInstance().lookup(ICMPAccountQuery.class);

	//账户相关，为了处理包含授权需求！！！！
	private BankaccbasVO[] bankAccs = null;  //选择的所有公司有权限的账户
//	private HashMap<String, BankaccbasVO[]> corp_BankAccs_Map = new HashMap<String, BankaccbasVO[]>();  //公司 --> 有权限账户
	private ArrayList<String> corpPkList = new ArrayList<String>();  //公司
	private ArrayList<BankaccbasVO[]> bankAccsList = new ArrayList<BankaccbasVO[]>();//公司有权限账户，与corpPkList一一对应
	private Map<String, BankaccbasVO> bankaccbasVOMap=new HashMap<String, BankaccbasVO>();
	
	private Map<String, CorpVO> corpVOMap=new HashMap<String, CorpVO>();
	
	private boolean isFirstTimeLinkedQuery = true;  //是否首次被联查
	
	/** 合并同单据分录相关列 */
	private static final String[] combineGroupbyCol = new String[]{"date", "corp",
		"system", "billtype", "billno", "explanation", "currtype", "pk_bill"};
	private static final String[] combineSumCol = new String[]{"inamount", "inlocalamount",
		"outamount", "outlocalamount"};
	private ICorpQry iCorpQry=NCLocator.getInstance().lookup(ICorpQry.class);

	public BankDailyBookToftPanel() {
		super();
		dataCon = new PagedDataContainer();
	}

	@Override
	public ButtonObject[] createMainFrameButtons() {
		ButtonObject subQueryBtn = new ButtonObject(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("report","UPPreport-000016")/*@res "联查单据"*/, nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("report","UPPreport-000016")/*@res "联查单据"*/, 2, "联查单据");	/*-=notranslate=-*/
		subQueryBtn.setTag("联查单据");
		subQueryBtn.setEnabled(false);
		ButtonObject viewBtn = new ButtonObject(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("report","UC001-0000021")/*@res "浏览"*/, nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("report","UC001-0000021")/*@res "浏览"*/, 2, "浏览");	/*-=notranslate=-*/
		ButtonObject firstBtn = new ButtonObject(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("report","UCH031")/*@res "首页"*/, nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("report","UCH031")/*@res "首页"*/, 2, "首页");	/*-=notranslate=-*/
		firstBtn.setTag("首页");
		ButtonObject preBtn = new ButtonObject(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("report","UCH033")/*@res "上一页"*/, nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("report","UCH033")/*@res "上一页"*/, 2, "上一页");	/*-=notranslate=-*/
		preBtn.setTag("上一页");
		ButtonObject nextBtn = new ButtonObject(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("report","UCH034")/*@res "下一页"*/, nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("report","UCH034")/*@res "下一页"*/, 2, "下一页");	/*-=notranslate=-*/
		nextBtn.setTag("下一页");
		ButtonObject endBtn = new ButtonObject(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("report","UCH032")/*@res "末页"*/, nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("report","UCH032")/*@res "末页"*/, 2, "末页");	/*-=notranslate=-*/
		endBtn.setTag("末页");
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


	/* (non-Javadoc)
	 * @see nc.ui.glpub.IUiPanel#addListener(java.lang.Object, java.lang.Object)
	 */
	public void addListener(Object objListener, Object objUserdata) {

	}

	/* (non-Javadoc)
	 * @see nc.ui.glpub.IUiPanel#getButtons()
	 */
	public ButtonObject[] getButtons() {
		return super.getButtons();
	}

	/* (non-Javadoc)
	 * @see nc.ui.glpub.IUiPanel#getTitle()
	 */
	public String getTitle() {
		return nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("report","UPPreport-000064")/*@res "银行存款日记账"*/;
	}

	/* (non-Javadoc)
	 * @see nc.ui.glpub.IUiPanel#invoke(java.lang.Object, java.lang.Object)
	 */
	public Object invoke(Object objData, Object objUserData) {
		Object[] condValues = null;
		if(objUserData != null && objUserData.equals("queryFromDailyReport"))
		{
			//为联查界面重设按钮
			if(subQuerybuttons == null)
			{
				ButtonObject refreshBtn = getRefreshBtn();
				ButtonObject printBtn = getPrintBtn();
				ButtonObject subQueryBtn = new ButtonObject(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("report","UPPreport-000016")/*@res "联查单据"*/, nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("report","UPPreport-000016")/*@res "联查单据"*/, 2, "联查单据");	/*-=notranslate=-*/
				subQueryBtn.setTag("联查单据");
				subQueryBtn.setPowerContrl(false);
//				联查按钮快捷键本应该共用数据库中注册的节点按钮快捷键，先这样
				subQueryBtn.setHotKey("K");
				subQueryBtn.setDisplayHotkey("(Ctrl+K)");
				subQueryBtn.setModifiers(2);
				ButtonObject returnBtn = new ButtonObject(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("report","UC001-0000038")/*@res "返回"*/, nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("report","UC001-0000038")/*@res "返回"*/, 2, "返回");	/*-=notranslate=-*/
				returnBtn.setTag("返回");
				returnBtn.setPowerContrl(false);
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

	/* (non-Javadoc)
	 * @see nc.ui.glpub.IUiPanel#nextClosed()
	 */
	public void nextClosed() {

	}

	/* (non-Javadoc)
	 * @see nc.ui.glpub.IUiPanel#onButtonClicked(nc.ui.pub.ButtonObject)
	 */
	public void onButtonClicked(ButtonObject bo) {
		if(bo == getQueryBtn() || bo.getTag().equals(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("report","UC001-0000009")/*@res "刷新"*/))
		{
			//下面几句顺序。。。
			hasQueryed = false;
			dataCon.clear();
			if(bo == getQueryBtn())
				onQuery();
			else
				refresh();
			if(notShowNoHappen() && dataCon.getPageCount() > 0)
				showData(dataCon.first());  //此时onQuery不一定查的是第一个有发生数的公司+账户
			else
				dataCon.loadData(getReportData());  //第一页数据
			hasQueryed = true;
		}
		else if(bo.getTag().equals("直接打印"))
			directPrint();
		else if(bo.getTag().equals("模板打印"))
			templetPrint();
		else if(bo.getTag().equals("联查单据"))
		{
			//本意是用IUiPanel接口联查（来源单据必须实现）
//			IUiPanel panel = (IUiPanel) m_parent.showNext("nc.ui.cmp.report.dailybook.BillPanelTest");
//			panel.invoke(pk_bill, "subquery");
			queryBill();
		}
		else if(bo.getTag().equals("返回"))
			m_parent.closeMe();
		else
		{
			UIComboBox accountBox = null;
			try {
				accountBox = getAccountBoxInHeader();
			} catch (Exception e) {
				handleException(e);
			}
			if(bo.getTag().equals("首页"))
				accountBox.setSelectedIndex(0);
			else if(bo.getTag().equals("上一页"))
				accountBox.setSelectedIndex(accountBox.getSelectedIndex()==0?
						0:accountBox.getSelectedIndex()-1);
			else if(bo.getTag().equals("下一页"))
				accountBox.setSelectedIndex(
						accountBox.getSelectedIndex()==accountBox.getItemCount()-1?
								accountBox.getItemCount()-1:accountBox.getSelectedIndex()+1);
			else if(bo.getTag().equals("末页"))
				accountBox.setSelectedIndex(accountBox.getItemCount()-1);
		}
	}

	private UIComboBox getAccountBoxInHeader() throws BusinessException
	{
		if(null == getReportTemplet().getHeadItem("bankaccount")){
			throw new BusinessException("账表表头格式设置不正确，必须要有银行账户");
		}
		return (UIComboBox)getReportTemplet().getHeadItem("bankaccount").getComponent();
	}


	public void initTempletHeader() throws Exception {
		super.initTempletHeader();
		getAccountBoxInHeader().addItemListener(this);
	}


	@Override
	public void itemStateChanged(ItemEvent e) {
		super.itemStateChanged(e);
		try {
			if(e.getSource().equals(getAccountBoxInHeader())
					&& e.getStateChange() == ItemEvent.SELECTED)
			{
				setBankInfoInHead();
				if(hasQueryed)  //都已经查询过了
				{
					ValueObject[] results = null;
					int selIndex = getAccountBoxInHeader().getSelectedIndex();
					Object selectedValue = ((UIComboBox)getHeadComponent("bankaccount")).getSelectdItemValue();
					if(dataCon.getData(selIndex) == null)
					{
						results = queryBySelectedValue(selectedValue);
						dataCon.loadData(results, selIndex);
					}
					if(null != ((String[])selectedValue)[1]){
						Collection<BankaccbasVO> banks = bankaccbasVOMap.values();
						String pktype = "";
						for(BankaccbasVO vo : banks){
							if(null != vo &&
								vo.getPk_bankaccbas().equals(((String[])selectedValue)[1])){
								pktype = vo.getPk_currtype();
								break;
							}
						}
						setCurrentDigit(pktype);
					}
					showData(dataCon.getData(selIndex));
				}
			}
		} catch (Exception e1) {
			handleException(e1);
		}
	}

	private void queryBill()
	{
		String pk_bill = getSelectedRowValue("pk_bill");
		String pk_billType = getSelectedRowValue("pk_billtype");
		String pk_corp = getSelectedRowValue("corp");
		if(pk_bill == null || pk_bill.trim().equals("")
				|| pk_billType == null || pk_billType.trim().equals(""))
		{
			MessageDialog.showWarningDlg(this, nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("report","UPPreport-000018")/*@res "警告"*/, nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("report","UPPreport-000019")/*@res "该记录没有单据主键或单据类型信息，无法联查！"*/);
			return;
		}
		LinkQueryBillData lqbd = new LinkQueryBillData();
		lqbd.setBillID(pk_bill);
		lqbd.setBillType(pk_billType.trim());
		lqbd.setPkOrg(pk_corp);
		String nodeCode = PfUtilUITools.findNodecodeOfBilltype(pk_billType.trim(), lqbd);
		SFClientUtil.openLinkedQueryDialog(nodeCode, this, lqbd);
	}


	/**
	 * 具体报表需要实现
	 */
	public void bodyRowChange(BillEditEvent e) {
		int selected = e.getRow();
		String explanation = (String)getReportData()[selected].getAttributeValue("explanation");
		ButtonObject subQryBtn = null;
		for (int i = 0; i < getButtons().length; i++) {
			if(getButtons()[i].getTag().equals("联查单据"))
			{
				subQryBtn = getButtons()[i];
				break;
			}
		}
		if(explanation != null && (explanation.indexOf(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("report","UPPreport-000065")/*@res "期初"*/) > -1
				|| explanation.indexOf(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("report","UPPreport-000006")/*@res "小计"*/) > -1 || explanation.indexOf(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("report","UPPreport-000007")/*@res "总计"*/) > -1))
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

		try {
			setAccComboBoxDataInHeader(panel);
		} catch (BusinessException e) {
			ExceptionHandler.consume(e);
		}
		setBankInfoInHead();
	}


	private boolean notShowNoHappen()
	{
		return getCommonReportQueryPanel().getChkBoxNotShowNoHappenAndBal().isSelected();
	}

	/**
	 * @param panel
	 * @throws BusinessException 
	 */
	private void setAccComboBoxDataInHeader(CommonReportQueryPanel panel) throws BusinessException {
		UIComboBox accountBox = getAccountBoxInHeader();
		accountBox.removeAllItems();
		String[] accPks = panel.getRefPanelBankAcc().getRefPKs();
		String[] corpPks = panel.getRefPanelUnit().getRefPKs();
		DefaultConstEnum[] accounts = null;
		if(accPks != null && accPks.length > 0)
		{
			if(needCorpCombineAsIncludeAuth())
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
			else
			{
				String[][] corpAndAccs = filterNoOccurCorpAndAccount(corpPks, accPks);
				accounts = getAccountEnums(corpAndAccs);
			}
		}
		else
		{
			if(needCorpCombineAsIncludeAuth())
			{
				if(bankAccs != null && bankAccs.length > 0)
					bankAccs = filterNoOccurAccount(bankAccs);
				if(bankAccs != null && bankAccs.length > 0)
				{
					accounts = new DefaultConstEnum[bankAccs.length];
					for (int i = 0; i < bankAccs.length; i++) {
						accounts[i] = new DefaultConstEnum(bankAccs[i].getPk_bankaccbas(), bankAccs[i].getAccount());//改成显示账号
					}
				}
			}
			else
			{
				String[][] corpAndAccs = filterNoOccurCorpAndAccount(corpPkList, bankAccsList);
				accounts = getAccountEnums(corpAndAccs);
			}
		}
		accountBox.addItems(accounts);
	}

	/**
	 * @param accounts
	 * @param corpAndAccs
	 * @return
	 * @throws BusinessException 
	 */
	private DefaultConstEnum[] getAccountEnums(String[][] corpAndAccs) throws BusinessException {
		DefaultConstEnum[] accounts = null;
		if(corpAndAccs != null && corpAndAccs.length > 0)
		{
			String[] accpks = getAccpksNoConcernCorp(corpAndAccs);
			
			HashMap<String, String> accPk_AccName_Map = getAccPk_AccName_Map(accpks);
			accounts = new DefaultConstEnum[corpAndAccs.length];
			//maji add 循环访问数据库
			List<String> corpList=new ArrayList<String>();
			for (int i = 0; i < corpAndAccs.length; i++) {
				if (!corpVOMap.containsKey(corpAndAccs[i][0])&& !corpList.contains(corpAndAccs[i][0])) {
					corpList.add(corpAndAccs[i][0]);
				}
			}
			CorpVO tempCorpVO=null;
			if (corpList.size()>0) {
				String[] corps=new String[corpList.size()];
				CorpVO[] corpVOs=iCorpQry.findCorpVOByPK(corpList.toArray(corps), null, null);
				for (CorpVO corpVO : corpVOs) {
					if (!corpVOMap.containsKey(corpVO.getPk_corp())) {
						corpVOMap.put(corpVO.getPk_corp(), corpVO);
					}
				}
			}
			for (int i = 0; i < corpAndAccs.length; i++) {
				tempCorpVO=corpVOMap.get(corpAndAccs[i][0]);
				accounts[i] = new DefaultConstEnum(new String[]{corpAndAccs[i][0], corpAndAccs[i][1]},
						accPk_AccName_Map.get(corpAndAccs[i][1])+"("+tempCorpVO.getUnitshortname()+")");
			}
		}
		return accounts;
	}


	private String[] getAccpksNoConcernCorp(String[][] corpAndAccs) {
		HashSet<String> accpkSet = new HashSet<String>();
		for (int i = 0; i < corpAndAccs.length; i++) {
			accpkSet.add(corpAndAccs[i][1]);
		}
		return accpkSet.toArray(new String[0]);
	}

	private HashMap<String, String> getAccPk_AccName_Map(String[] accPks) {
		HashMap<String, String> pkNameMap = new HashMap<String, String>();
		BankaccbasVO[] vos = null;
		try {
			List<String> accNeedQry=new ArrayList<String>();
			for (int i = 0; i < accPks.length; i++) {
				if (bankaccbasVOMap.containsKey(accPks[i])) {
					pkNameMap.put(bankaccbasVOMap.get(accPks[i]).getPk_bankaccbas(), bankaccbasVOMap.get(accPks[i]).getAccount());//改成显示账号
				}else {
					accNeedQry.add(accPks[i]);
				}
			}
			if (accNeedQry.size()>0) {
				vos = bankSrv.findBankBasVOByAccountPks(accNeedQry.toArray(new String[0]));
			}
		} catch (BusinessException e) {
			e.printStackTrace();
		}
		if(vos != null)
			for (int i = 0; i < vos.length; i++) {
				pkNameMap.put(vos[i].getPk_bankaccbas(), vos[i].getAccount());//改成显示账号
			}
		return pkNameMap;
	}

	/**
	 * 过滤账户
	 * @param accPks 账户pk
	 * @return 如果无发生不显示，返回有发生数据的账户，否则直接返回参数指定的账户
	 */
	private String[] filterNoOccurAccount(String[] accPks) {
		if(notShowNoHappen())
		{
			ValueObject[] results = null;
			ArrayList<String> accPkList = new ArrayList<String>();
			int c = 0;
			for (int i = 0; i < accPks.length; i++) {
				results = queryBySelectedValue(accPks[i]);
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


	private String[][] filterNoOccurCorpAndAccount(String[] corpPks, String[] accPks) {
		ArrayList<String[]> retList = new ArrayList<String[]>();
		String[] corpAndAcc = null;
		if(notShowNoHappen())
		{
			ValueObject[] results = null;
			int c = 0;
			for (int i = 0; i < corpPks.length; i++) {
				for (int j = 0; j < accPks.length; j++) {
					corpAndAcc = new String[]{corpPks[i], accPks[j]};
					results = queryBySelectedValue(corpAndAcc);
					if(results != null && results.length > 0)
					{
						dataCon.loadData(results, c++);
						retList.add(corpAndAcc);
					}
				}
			}
		}
		else
			for (int i = 0; i < corpPks.length; i++) {
				for (int j = 0; j < accPks.length; j++) {
					corpAndAcc = new String[]{corpPks[i], accPks[j]};
					retList.add(corpAndAcc);
				}
			}
		return retList.toArray(new String[0][0]);
	}


//	过滤账户vo
	private BankaccbasVO[] filterNoOccurAccount(BankaccbasVO[] accbasVO) {
		if(notShowNoHappen())
		{
			ValueObject[] results = null;
			ArrayList<BankaccbasVO> accvoList = new ArrayList<BankaccbasVO>();
			int c = 0;
			for (int i = 0; i < accbasVO.length; i++) {
				results = queryBySelectedValue(accbasVO[i].getPk_bankaccbas());
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


	private String[][] filterNoOccurCorpAndAccount(ArrayList<String> corpPkList, ArrayList<BankaccbasVO[]> bankAccsList) {
		ArrayList<String[]> retList = new ArrayList<String[]>();
		String[] corpAndAcc = null;
		if(notShowNoHappen())
		{
			ValueObject[] results = null;
			int c = 0;

			for (int i = 0; i < corpPkList.size(); i++) {
				for (int j = 0; j < bankAccsList.get(i).length; j++) {
					corpAndAcc = new String[]{corpPkList.get(i), bankAccsList.get(i)[j].getPk_bankaccbas()};
					results = queryBySelectedValue(corpAndAcc);
					if(results != null && results.length > 0)
					{
						dataCon.loadData(results, c++);
						retList.add(corpAndAcc);
					}
				}
			}
		}
		else
			for (int i = 0; i < corpPkList.size(); i++) {
				for (int j = 0; j < bankAccsList.get(i).length; j++) {
					corpAndAcc = new String[]{corpPkList.get(i), bankAccsList.get(i)[j].getPk_bankaccbas()};
					retList.add(corpAndAcc);
				}
			}
		return retList.toArray(new String[0][0]);
	}

	/**
	 * @param accPk
	 * @return
	 */
	private ValueObject[] queryBySelectedValue(Object selectedValue) {
		ValueObject[] results;
		SimpleConditionUnit scu = (SimpleConditionUnit)getConditionManager().getSimpleConditionUnit(pk_accountField, IOperatorConstants.IN, false);
		SimpleConditionUnit scuCorp = (SimpleConditionUnit)getConditionManager().getSimpleConditionUnit(pk_corpField, IOperatorConstants.IN, false);
		String[] pk_corps = getCommonReportQueryPanel().getRefPanelUnit().getRefPKs();

		AddBalanceRowProcessInfo addBrPi = (AddBalanceRowProcessInfo)getDSProcessInfoManager().getProcessInfo(DataSetProcessType.ADD_BALANCE_ROW);
		BalanceQueryVO bqvo = addBrPi.getBqvo();
		if(needCorpCombineAsIncludeAuth())
		{
			scu.setValueList(new String[]{selectedValue.toString()});
			scuCorp.setValueList(pk_corps);

			bqvo.setPks_corp(pk_corps);
			bqvo.setPks_account(new String[]{selectedValue.toString()});
		}
		else
		{

			scu.setValueList(new String[]{((String[])selectedValue)[1]});
			scuCorp.setValueList(new String[]{((String[])selectedValue)[0]});

			bqvo.setPks_corp(new String[]{((String[])selectedValue)[0]});
			bqvo.setPks_account(new String[]{((String[])selectedValue)[1]});
		}
		results = queryNoResetByDlg();
		results = processResultVOs(results);
		return results;
	}


	private void setBankInfoInHead()
	{
		BankaccbasVO acc = null;
		Object selectedValue = ((UIComboBox)getHeadComponent("bankaccount")).getSelectdItemValue();
		if(selectedValue != null)
		{
			try {  
				if(needCorpCombineAsIncludeAuth()){
					if (bankaccbasVOMap.get(selectedValue.toString())!=null) {
						acc=bankaccbasVOMap.get(selectedValue.toString());
					}else {
						acc = bankSrv.findBankBasVOByAccountPks(new String[]{selectedValue.toString()})[0];
						bankaccbasVOMap.put(selectedValue.toString(), acc);
					}
				}
				else{
					if (bankaccbasVOMap.get(((String[])selectedValue)[1])!=null) {
						acc=bankaccbasVOMap.get(((String[])selectedValue)[1]);
					}else {
						acc = bankSrv.findBankBasVOByAccountPks(new String[]{((String[])selectedValue)[1]})[0];
						bankaccbasVOMap.put(selectedValue.toString(), acc);
					}
				}
			} catch (BusinessException e) {
				handleException(e);
			}
		}

		if(acc != null)
		{
			FormulaParse parser = new FormulaParse();
			parser.setExpress("getColValue(bd_banktype,banktypename,pk_banktype,pk)");
			parser.addVariable("pk", acc.getPk_banktype());
			if(null != getHeadComponent("banktype"))
				((UIRefPane)getHeadComponent("banktype")).setText(parser.getValue());
			parser.setExpress("getColValue(bd_bankdoc,bankdocname,pk_bankdoc,pk)");
			parser.addVariable("pk", acc.getPk_bankdoc());
			if(null != getHeadComponent("bankdoc"))
				((UIRefPane)getHeadComponent("bankdoc")).setText(parser.getValue());
			String accAttr = "";
			if(acc != null && acc.getAccattribute() != null)
				switch (acc.getAccattribute().intValue()) {
				case IBankAccConstant.ACCATTRIBUTE_BASE:
					accAttr = nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("report","UPPreport-000066")/*@res "基本"*/;
					break;
				case IBankAccConstant.ACCATTRIBUTE_COMMON:
					accAttr = nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("report","UPPreport-000067")/*@res "一般"*/;
					break;
				case IBankAccConstant.ACCATTRIBUTE_SPECIAL:
					accAttr = nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("report","UPPreport-000068")/*@res "专用"*/;
					break;
				case IBankAccConstant.ACCATTRIBUTE_TEMP:
					accAttr = nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("report","UPPreport-000069")/*@res "临时"*/;
					break;
				default:
					break;
				}
			if(null != getHeadComponent("accountattr"))
				((UIRefPane)getHeadComponent("accountattr")).setText(accAttr);

			String arapProp = "";
			if(acc.getArapprop() != null)
				switch (acc.getArapprop().intValue()) {
				case IBankAccConstant.ARAPPROP_RECEIEVE:
					arapProp = nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("report","UPPreport-000070")/*@res "收入"*/;
					break;
				case IBankAccConstant.ARAPPROP_PAY:
					arapProp = nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("report","UPPreport-000071")/*@res "支出"*/;
					break;
				case IBankAccConstant.ARAPPROP_RECIEVEPAY:
					arapProp = nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("report","UPPreport-000072")/*@res "收支"*/;
					break;
				default:
					break;
				}
			if(null != getHeadComponent("receivepayattr"))
				((UIRefPane)getHeadComponent("receivepayattr")).setText(arapProp);
		}
		else
		{
			((UIRefPane)getHeadComponent("banktype")).setText("");
			((UIRefPane)getHeadComponent("bankdoc")).setText("");
			((UIRefPane)getHeadComponent("accountattr")).setText("");
			((UIRefPane)getHeadComponent("receivepayattr")).setText("");
		}
	}



	public void removeListener(Object objListener, Object objUserdata) {

	}



	
	public void showMe(IParent parent) {
		parent.getUiManager().removeAll();
		parent.getUiManager().add(this,this.getName());
		m_parent = parent;
	}




	public ValueObject[] processResultVOs(ValueObject[] resultVos) {
		resultVos = super.processResultVOs(resultVos);
		if(resultVos == null)
			return null;
		CircularlyAccessibleValueObject[] vos = (CircularlyAccessibleValueObject[])resultVos;
		Object expla = null;
		String sys = null;
		Object billType = null;
		String pk_corp = null;
		for (int i = 0; i < vos.length; i++) {
			expla = vos[i].getAttributeValue("explanation");
			if(expla != null && (expla.toString().trim().indexOf(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("report","UPPreport-000073")/*@res "本日小计"*/) > -1
					|| expla.toString().trim().indexOf(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("report","UPPreport-000007")/*@res "总计"*/) > -1))
			{
				vos[i].setAttributeValue("billno", "");
				vos[i].setAttributeValue("currtype", null);
				vos[i].setAttributeValue("system", null);
				vos[i].setAttributeValue("billtype", null);
				vos[i].setAttributeValue("voucherno", null);
				vos[i].setAttributeValue("corp", null);
				//加额外报表列，设总计合计行为空值
				ReportItem itemone = getReportTemplet().getBody_Item("localbalance");
				for(ReportItem item :getReportTemplet().getBody_Items()){
					if(null != item && null != itemone 
						&& item.getShowOrder() > itemone.getShowOrder())
						vos[i].setAttributeValue(item.getKey(), null);
				}
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





	public String getFunNode() {
		return "20040710";
	}



	public String[] getHideCurrFormatColCodes() {
		return new String[]{"inamount","outamount","balance"};
	}



	public void setDataGetterInfo(ColumnDataGetterInfo getterInfo, String key) {
		if("date".equals(key))  //日期，默认类型为单据日期
			getterInfo.setFieldUnit(billDateField);
		else if("corp".equals(key))  //公司
			getterInfo.setFieldUnit(pk_corpField);
		else if("system".equals(key))  //业务系统
			getterInfo.setFieldUnit(systemCodeField);
		else if("billtype".equals(key))  //单据类型
			getterInfo.setFieldUnit(billTypeField);
		else if("billno".equals(key))  //单据号
			getterInfo.setFieldUnit(billcodeField);
		else if("explanation".equals(key))  //摘要
			getterInfo.setFieldUnit(new FieldUnit("cmp_detail", "memo"));
		else if("currtype".equals(key))  //币种
			getterInfo.setFieldUnit(pk_currtypeField);
		else if("inamount".equals(key))  //收入原币
			getterInfo.setFieldUnit(receiveField);
		else if("inlocalamount".equals(key))  //收入本币
			getterInfo.setFieldUnit(receiveLField);
		else if("outamount".equals(key))  //支出原币
			getterInfo.setFieldUnit(payField);
		else if("outlocalamount".equals(key))  //支出本币
			getterInfo.setFieldUnit(payLField);
		else if("pkdetail".equals(key)){//明细主键（便于报表模板上取其他字段以进行扩展）
			getterInfo.setFieldUnit(pk_detailField);
		}
	}


	/**
	 * modifier by zhaozh on 2009-7-8
	 * 银行存款日记账查询勾选‘合并同单据分录’报错，且无法查询出任何数据
	 */
	protected ReportColumn[] getExtraReportColumns()
	{
		ReportColumn[] rcs = new ReportColumn[3];
		rcs[0] = new ReportColumn();
		rcs[0].setColBasicInfo(new ColumnBasicInfo("pk_bill", true));
		rcs[0].setGetterInfo(new ColumnDataGetterInfo(pk_billField, false, false));
		rcs[1] = new ReportColumn();
		rcs[1].setColBasicInfo(new ColumnBasicInfo("pk_billtype", true));  //为了联查单据再存一份
		rcs[1].setGetterInfo(getColumnManager().getGetterInfoByColKey("billtype"));
		rcs[2] = new ReportColumn();
		rcs[2].setColBasicInfo(new ColumnBasicInfo("direction", true));  //为了联查单据再存一份
		rcs[2].setGetterInfo(new ColumnDataGetterInfo(directionField, false, false));
		return rcs;
	}

	@Override
	public CommonReportQueryPanel createCommonReportQueryPanel() {
		return new InnerCommonQueryPanel();
	}

	private UICheckBox chkBoxIncludeAccredit = null;
	@SuppressWarnings("serial")
	class InnerCommonQueryPanel extends CommonReportQueryPanel
	{
		public void addControlsToUI() {
			addToRegularControlPanel(getLabelUnit(), getRefPanelUnit());
			addToRegularControlPanel(getLabelCurr(), getRefPanelCurrency());
			addToRegularControlPanel(getLabelBankAcc(), getRefPanelBankAcc());
			addToRegularControlPanel(getLabelDateType(),getCbBoxDateType());
			addToRegularControlPanel(getLabelBillStatus(),getCbBoxSignStatus());
			addToRegularControlPanel(new UILabel(""), new UILabel("")); //just for layout
			addToRegularControlPanel(getLabelDate(),getRefPanelBeginDate());
			addToRegularControlPanel(getLabelDateTo(),getRefPanelEndDate());
			addToRegularControlPanel(getLabelSystem(), getRefPanelSystem());
			addToRegularControlPanel(getLabelBillType(), getRefPanelBillType());
			addToChkBoxPanel(getChkBoxIncludeAccredit(), getChkBoxShowVoucherNo(), getChkBoxNotShowNoHappenAndBal());
			addToChkBoxPanel(getChkBoxCombineSameBill(), null);

			//赋默认值
			/**@author zhaozh 2009-9-17 上午10:34:39 三个日记账起始日期为当天 */
			getRefPanelBeginDate().setText(ce.getDate().toString());
			getRefPanelUnit().setPK(ce.getCorporation().getPrimaryKey());
			//去掉“and mainaccount is null”，需要参照出管理账户
			getRefPanelBankAcc().getRefModel().setWherePart(" pk_bankaccbas in (select distinct pk_bankaccbas from bd_bankaccauth where pk_corp = '" + ce.getCorporation().getPrimaryKey() + "' and isauthedusepower='Y') or (mainaccount is not null and bd_bankaccbas.pk_corp = '" + ce.getCorporation().getPrimaryKey() + "') ");
			UFDate date = ce.getDate();
//			getRefPanelBeginDate().setText(date.getDateBefore(date.getDay()-1).toString());
			getRefPanelEndDate().setText(date.toString());
			getChkBoxIncludeAccredit().setEnabled(false);
			
			getChkBoxCombineSameBill().setSelected(true);
			getChkBoxCombineSameBill().setEnabled(true);
		}

		public UICheckBox getChkBoxIncludeAccredit() {
			if(chkBoxIncludeAccredit == null)
				chkBoxIncludeAccredit = new UICheckBox(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("report","UPPreport-000074")/*@res "包含授权"*/);
			return chkBoxIncludeAccredit;
		}

		@Override
		public String check() {
			String startDate = getRefPanelBeginDate().getText();
			String endDate = getRefPanelEndDate().getText();
			if(startDate == null || startDate.trim().equals(""))
				return nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("report","UPPreport-000012")/*@res "开始日期不能为空！"*/;
			if(StringUtil.isEmptyWithTrim(endDate)){
				return nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("report","UPPreport-000013")/*@res "结束日期不能为空！"*/;
			}
			if(!(endDate == null || endDate.trim().equals("")) && UFDate.getDate(endDate).before(UFDate.getDate(startDate)))
				return nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("report","UPPreport-000014")/*@res "开始日期不能在结束日期之后！"*/;
			String checkInfo = super.check();
			if(checkInfo != null)
				return checkInfo;
//			if(!CommonQueryUtils.validateDateByInitBalCreateDate(
//					getCbBoxDateType().getSelectedItem().toString(),
//					UFDate.getDate(startDate), getRefPanelUnit().getRefPKs(),
//					getRefPanelBankAcc().getRefPKs(), null, CmpConst.BANK_DEPOSIT))
//				return nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("report","UPPreport-000094")/*@res "按单据日期查询时，起始日期必须在期初余额开户日期之后！请选择符合日期条件的账户"*/;
			return null;
		}

		public void valueChanged(ValueChangedEvent event) {
			Object target = event.getSource();
			if(target.equals(getRefPanelUnit()))
			{
				String[] pks = getRefPanelUnit().getRefPKs();
				if(pks != null && pks.length == 1)
				{
					getChkBoxIncludeAccredit().setSelected(false);
					getChkBoxIncludeAccredit().setEnabled(false);
				}
				else
					getChkBoxIncludeAccredit().setEnabled(true);
				getRefPanelBankAcc().getRefModel().setWherePart(
						" pk_bankaccbas in (select distinct pk_bankaccbas from bd_bankaccauth where pk_corp = '" + getBaseCorpPk() + "' and isauthedusepower='Y') or (mainaccount is not null and bd_bankaccbas.pk_corp = '" + getBaseCorpPk() + "') ", true);
			}
			else if(target.equals(getRefPanelCurrency()))
			{
				String[] pks = getRefPanelCurrency().getRefPKs();
				String wherePart = " pk_bankaccbas in (select distinct pk_bankaccbas from bd_bankaccauth where pk_corp = '" + getBaseCorpPk() + "' and isauthedusepower='Y') or (mainaccount is not null and bd_bankaccbas.pk_corp = '" + getBaseCorpPk() + "') ";
				if(pks != null && pks.length > 0)
				{
					String pkInStr = QueryProcessHelper.getInSqlStr(pks, false);
					wherePart = "("+wherePart+")";
					getRefPanelBankAcc().setText("");
					getRefPanelBankAcc().getRefModel().setWherePart(
							wherePart + " and pk_currtype in (" + pkInStr + ")", true);
				}
				else
					getRefPanelBankAcc().getRefModel().setWherePart(wherePart, true);
			}
			else if(target.equals(getRefPanelBeginDate()))
			{
				changeToCmpCreateDate();
			}
		}
	}

	@Override
	public void initConditionManager(ConditionManager manager) {
		//添加资金形态条件
		SimpleConditionUnit condUnit = new SimpleConditionUnit(fundFormField, IOperatorConstants.EQ, true);
		condUnit.setValueList(new Integer[]{new Integer(CmpConst.BANK_DEPOSIT)});
		manager.addCondition(condUnit);
		manager.addCondition(isBillRecordCU);
		manager.addCondition(drConditionUnit);
		manager.addCondition(new SimpleConditionUnit(pk_corpField, IOperatorConstants.IN, false));
		manager.addCondition(new SimpleConditionUnit(pk_currtypeField, IOperatorConstants.IN, false));
//		manager.addCondition(new SimpleConditionUnit(billStatusField, IOperatorConstants.GE, true));
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

		//初始排序条件
//		manager.addOrderField(billcodeField, 0);
		manager.addOrderField(directionField, 0);
		manager.addOrderField(billTypeField, 1);
		manager.addOrderField(billcodeField, 2);

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
		resetCorpAndAccCondition(condManager, panel);
		SimpleConditionUnit scu = (SimpleConditionUnit)condManager.getSimpleConditionUnit(pk_currtypeField, IOperatorConstants.IN, false);
		String[] values = panel.getRefPanelCurrency().getRefPKs();
		scu.setValueList(values);
		resetBillStatusCondition(condManager, ((Integer)panel.getCbBoxSignStatus().getSelectdItemValue()).intValue());
		Object dateType = panel.getCbBoxDateType().getSelectedItem();
		clearDateCondition(condManager);
		if(dateType.equals(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("report","UC000-0000799")/*@res "单据日期"*/))
			scu = (SimpleConditionUnit)condManager.getSimpleConditionUnit(billDateField,
					IOperatorConstants.GE, false);
		else if(dateType.equals(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("report","UC000-0003103")/*@res "签字日期"*/))
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

		if(dateType.equals(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("report","UC000-0000799")/*@res "单据日期"*/))
			scu = (SimpleConditionUnit)condManager.getSimpleConditionUnit(billDateField,
					IOperatorConstants.LE, false);
		else if(dateType.equals(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("report","UC000-0003103")/*@res "签字日期"*/))
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
	 * @param condManager
	 * @param panel
	 */
	@SuppressWarnings("unchecked")
	private void resetCorpAndAccCondition(ConditionManager condManager, CommonReportQueryPanel panel) {
		String[] values = null;
		SimpleConditionUnit	scu = (SimpleConditionUnit)condManager.getSimpleConditionUnit(pk_accountField, IOperatorConstants.IN, false);
		values = panel.getRefPanelBankAcc().getRefPKs();
		String[] pk_corps = panel.getRefPanelUnit().getRefPKs();
		SimpleConditionUnit scuCorp = (SimpleConditionUnit)condManager.getSimpleConditionUnit(pk_corpField, IOperatorConstants.IN, false);
//		HashMap<String, BankaccbasVO[]> corp_BankAccs_Mapt = null;
		ArrayList[] corpAndAccList = null;
		BankaccbasVO[] bankAccst = null;
		if(values == null)
		{
//			corp_BankAccs_Mapt = getBankAccoutsByCorp(pk_corps);
			corpAndAccList = getCorpAndAccsHavingAccs(pk_corps, panel.getRefPanelCurrency().getRefPKs());
			if(needCorpCombineAsIncludeAuth()) //按账户维度分页
			{
				bankAccst = getBankAccsNoConcernCorp((ArrayList<BankaccbasVO[]>)corpAndAccList[1]);
				if(bankAccst == null || bankAccst.length == 0)
					scu.setValueList(new String[]{"nodata"});
				else
					scu.setValueList(new String[]{bankAccst[0].getPk_bankaccbas()});
				scuCorp.setValueList(pk_corps);
			}
			else   //按公司+账户维度分页
			{
				if(corpAndAccList[1].size() == 0)
					scu.setValueList(new String[]{"nodata"});
				else
				{
					scu.setValueList(new String[]{((BankaccbasVO[])corpAndAccList[1].get(0))[0].getPk_bankaccbas()});
					scuCorp.setValueList(new String[]{(String)corpAndAccList[0].get(0)});
				}
			}
		}
		else
		{
			scu.setValueList(new String[]{values[0]});
			if(needCorpCombineAsIncludeAuth()) //按账户维度分页
				scuCorp.setValueList(pk_corps);
			else   //按公司+账户维度分页
				scuCorp.setValueList(new String[]{pk_corps[0]});
		}
	}



	private BankaccbasVO[] getBankAccsNoConcernCorp(ArrayList<BankaccbasVO[]> list) {
		HashSet<String> accPkSet = new HashSet<String>();
		ArrayList<BankaccbasVO> accList = new ArrayList<BankaccbasVO>();
		if(list.size() > 0)
			for (BankaccbasVO[] accs : list) {
				if(accs != null && accs.length > 0)
					for (int i = 0; i < accs.length; i++) {
						if(!accPkSet.contains(accs[i].getPk_bankaccbas()))
						{
							accPkSet.add(accs[i].getPk_bankaccbas());
							accList.add(accs[i]);
						}
					}
			}
		bankAccs = accList.toArray(new BankaccbasVO[0]);
		return bankAccs;
	}

	/* (non-Javadoc)
	 * @see nc.ui.cmp.report.reportuicommon.ReportFrameForReportTemplet#showData(nc.vo.pub.ValueObject[])
	 */
	@Override
	public void showData(ValueObject[] resultVos) {
		super.showData(resultVos);
		if(getCommonReportQueryPanel().getChkBoxShowVoucherNo().isSelected())
			getReportTemplet().showHiddenColumn("voucherno");
		else
			getReportTemplet().hideColumn("voucherno");
	}

//	private HashMap<String, BankaccbasVO[]> getCorpAndAccsHavingAccs(String[] corps) {
//	corp_BankAccs_Map.clear();
//	try {
//	for (int i = 0; i < corps.length; i++) {
////	必须包含取消授权，否则那些账户查不出
//	corp_BankAccs_Map.put(corps[i], bankSrv.findBankBasVOByCorpId(corps[i], true));
//	}
//	} catch (BusinessException e) {
//	handleException(e);
//	}
//	return corp_BankAccs_Map;
//	}


	private ArrayList[] getCorpAndAccsHavingAccs(String[] corps, String[] pk_currtypes) {
		corpPkList.clear();
		bankAccsList.clear();
		BankaccbasVO[] basvos = null;
		ArrayList[] retLists = new ArrayList[2];
		try {
			for (int i = 0; i < corps.length; i++) {
//				必须包含取消授权，否则那些账户查不出
				if(pk_currtypes == null || pk_currtypes.length == 0)
					basvos = bankSrv.findBankBasVOByCorpId4DataPower(corps[i], true);
				else
					basvos = bankSrv.getBankBasVOByCorpAndCurrtypes(corps[i], true, pk_currtypes);
				if(basvos != null && basvos.length > 0)
				{
					corpPkList.add(corps[i]);
					bankAccsList.add(basvos);
				}
			}
		} catch (BusinessException e) {
			handleException(e);
		}
		retLists[0] = corpPkList;
		retLists[1] = bankAccsList;
		return retLists;
	}


	/**
	 * @return
	 */
	private boolean needCorpCombineAsIncludeAuth() {
		String[] pk_corps = getCommonReportQueryPanel().getRefPanelUnit().getRefPKs();
		return chkBoxIncludeAccredit.isSelected() && pk_corps != null && pk_corps.length > 1;
	}


	private void resetDSProcessInfoManager(DSProcessInfoManager piManager, CommonReportQueryPanel panel) {
		AddBalanceRowProcessInfo addBrPi = (AddBalanceRowProcessInfo)piManager.getProcessInfo(DataSetProcessType.ADD_BALANCE_ROW);
		//动态凭证号列，得重设
		addBrPi.setStrKey_ColumnIndex_Map(getColumnManager().getBalanceKey_ColIndex_Map());
		addBrPi.setAttributeNames(getColumnManager().getAttributeNames());
		BalanceQueryVO bqvo = addBrPi.getBqvo();
		if(bqvo == null)
		{
			bqvo = new BalanceQueryVO();
			bqvo.setFundForm(CmpConst.BANK_DEPOSIT);
			bqvo.setGroupByKey(groupByBalKey);  //防止没有任何group by生成空行
			addBrPi.setBqvo(bqvo);
		}
		String[] pk_corps = panel.getRefPanelUnit().getRefPKs();
		if(panel.getRefPanelBankAcc().getRefPKs() == null)
			if(needCorpCombineAsIncludeAuth())
			{
				bqvo.setPks_corp(pk_corps);
				if(bankAccs != null && bankAccs.length > 0)  //用resetConditionManager中查出来的
					bqvo.setPks_account(new String[]{bankAccs[0].getPk_bankaccbas()});  //查询出账户，设为第一个
				else
					bqvo.setPks_account(null);
			}
			else
			{
				bqvo.setPks_corp(new String[]{corpPkList.size()==0?"nodata":corpPkList.get(0)});
				if(bankAccsList.size() > 0)  //用resetConditionManager中查出来的
					bqvo.setPks_account(new String[]{bankAccsList.get(0)[0].getPk_bankaccbas()});  //查询出账户，设为第一个
				else
					bqvo.setPks_account(null);
			}
		else
		{
			bqvo.setPks_account(new String[]{panel.getRefPanelBankAcc().getRefPKs()[0]});  //先只查第一个账户
			if(needCorpCombineAsIncludeAuth())
				bqvo.setPks_corp(pk_corps);
			else
				bqvo.setPks_corp(new String[]{pk_corps[0]});
		}
		bqvo.setPks_currtype(panel.getRefPanelCurrency().getRefPKs());
		bqvo.setBillStatus(((Integer)panel.getCbBoxSignStatus().getSelectdItemValue())
				.intValue());
		bqvo.setDate(new UFDate(panel.getRefPanelBeginDate().getText().trim()));
		bqvo.setDateType(panel.getCbBoxDateType().getSelectedItem().toString());

		if(panel.getChkBoxShowVoucherNo().isSelected())
		{
			piManager.unRemovedProcessInfo(DataSetProcessType.SELF_DEFINE);
			piManager.setCriticalIndex(2); //生成凭证号在后台处理
		}
		else
		{
			piManager.removeProcessInfo(DataSetProcessType.SELF_DEFINE);
			piManager.setCriticalIndex(1);
		}

//		if(needCorpCombineAsIncludeAuth())
//		piManager.unRemovedProcessInfo(DataSetProcessType.SORT);
//		else
//		piManager.removeProcessInfo(DataSetProcessType.SORT);
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

	/**
	 * modifier by zhaozh on 2009-7-8
	 * 银行存款日记账查询勾选‘合并同单据分录’报错，且无法查询出任何数据
	 * @param colManager
	 * @param panel
	 */
	private void resetColumnManager(ColumnManager colManager, CommonReportQueryPanel panel) {
		Object dateType = panel.getCbBoxDateType().getSelectedItem();
		ColumnDataGetterInfo getterInfo = colManager.getGetterInfoByColKey("date");
		if(dateType.equals(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("report","UC000-0000799")/*@res "单据日期"*/))
			getterInfo.setFieldUnit(billDateField);
		else if(dateType.equals(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("report","UC000-0003103")/*@res "签字日期"*/))
			getterInfo.setFieldUnit(signDateField);
		else
			getterInfo.setFieldUnit(payDateField);
		//是否合并同单据分录
		boolean isCombineSameBill = panel.getChkBoxCombineSameBill().isSelected();
		ColumnDataGetterInfo[] getterInfos = colManager.getGetterInfoByColKeys(combineGroupbyCol);
		for (int i = 0; i < getterInfos.length; i++) {
			getterInfos[i].setGroupByField(isCombineSameBill);
		}
		ColumnDataGetterInfo[] getterInfos1 = colManager.getGetterInfoByColKeys(combineSumCol);
		for (int i = 0; i < getterInfos1.length; i++) {
			getterInfos1[i].setSumField(isCombineSameBill);
		}
		colManager.getGetterInfoByColKey("direction").setGroupByField(isCombineSameBill);
	}


	@Override
	public void initDataSourceTableInfo(ReportDataSourceTableInfo dataSourceTableInfo) {
		dataSourceTableInfo.setMainTable("cmp_detail");
	}

	@Override
	public void initDSProcessInfoManager(DSProcessInfoManager processInfoManager) {
		//第一步取期初余额，余额查询条件在resetManagerByDlg方法中设置
		AddBalanceRowProcessInfo addBrPi = new AddBalanceRowProcessInfo();
		processInfoManager.addProcessInfo(DataSetProcessType.ADD_BALANCE_ROW, addBrPi);
		//第二步，自定义处理，根据单据pk生成凭证号
		getDSProcessorFactory().registerSelfDefProcessor(new AddVoucherInfoProcessor());//注册自定义处理器
		SubstituteProcessInfo sttPi0 = new SubstituteProcessInfo();
		sttPi0.setCorpCol("corp");
		ArrayList<String[]> substituteCol = new ArrayList<String[]>();
		substituteCol.add(new String[]{"voucherno"});
		sttPi0.setSubstituteCol(substituteCol);
		ArrayList<String[]> joinCol = new ArrayList<String[]>();
		joinCol.add(new String[]{"pk_bill"});
		sttPi0.setJoinCol(joinCol);
		processInfoManager.addProcessInfo(DataSetProcessType.SELF_DEFINE, sttPi0);

//		排序，为余额行计算做准备。对这一步排序要求很高。期初总是合并显示
//		SortProcessInfo sortPi = new SortProcessInfo();
//		processInfoManager.addProcessInfo(DataSetProcessType.SORT, sortPi);
//		sortPi.setOrderColumn(new String[]{"corp", "date"});
		
		//计算余额前先修正本币精度
		FixAmountPrecisionInfo fapPi = new FixAmountPrecisionInfo();
		fapPi.setLocalAmountCols(new String[]{"inlocalamount", "outlocalamount"});
		processInfoManager.addProcessInfo(DataSetProcessType.FIX_AMOUNT_PRECISION, fapPi);

		//第三步余额计算
		BalanceRowsComputeProcessInfo brcPi = new BalanceRowsComputeProcessInfo();
		brcPi.setSortColumns(new String[]{"date"});
		brcPi.setBalanceColumns(new String[]{"balance", "localbalance"});
		String[][] express = new String[][]{
				new String[]{"balance","+","inamount","-","outamount"},
				new String[]{"localbalance","+","inlocalamount","-","outlocalamount"}};
		brcPi.setComputeExpression(express);
		brcPi.setRowInnerCompute(false);
		processInfoManager.addProcessInfo(DataSetProcessType.BALANCE_ROWS_COMPUTE, brcPi);

		//第四步小计
		SubtotalProcessInfo stPi = new SubtotalProcessInfo();
		stPi.setSubtotalByCol(new String[]{"date"});
		stPi.setLimitSumGen(0);
		stPi.setSubtotalObject(new String[]{"inamount", "inlocalamount", "outamount", "outlocalamount"});
		stPi.setSubtotalShowCol(new String[]{"explanation"});
		stPi.setShowText(new String[]{nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("report","UPPreport-000073")/*@res "本日小计"*/});
		stPi.setRequireOutputDetail(true);
		stPi.setTotalSum(true);
		processInfoManager.addProcessInfo(DataSetProcessType.SUBTOTAL, stPi);
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


	public void resetManagerForSubQuery(ColumnManager colManager, ConditionManager condManager,
			DSProcessInfoManager piManager, Object[] condValues, String queryName) {
		//zhaozh 时联查加入排序（收款单在前）
		condManager.addOrderField(directionField, 0);
		condManager.addOrderField(billTypeField, 1);
		condManager.addOrderField(billcodeField, 2);
		if(isFirstTimeLinkedQuery )
		{
			isFirstTimeLinkedQuery = false;
			SimpleConditionUnit condUnit = new SimpleConditionUnit(fundFormField, IOperatorConstants.EQ, true);
			condUnit.setValueList(new Integer[]{new Integer(CmpConst.BANK_DEPOSIT)});
			condManager.addCondition(condUnit);
			condManager.addCondition(isBillRecordCU);
			condManager.addCondition(drConditionUnit);
		}
		CommonReportQueryPanel panel = getCommonReportQueryPanel();
		if(condValues[0] instanceof String)
			panel.getRefPanelUnit().setPK(condValues[0]);  //公司pk
		else if(condValues[0] instanceof String[])
			panel.getRefPanelUnit().setPKs((String[])condValues[0]);  //公司pk
		panel.getRefPanelBankAcc().getRefModel().setPk_corp(getBaseCorpPk());//防止跨公司
		panel.getRefPanelBankAcc().setPK(condValues[1]); //银行账户pk
//		bankAccs = new BankaccbasVO[]{bankSrv};//等待uap提供
		panel.getRefPanelCurrency().setPK(condValues[2]); //币种pk
		int selectedIndex = 0;
		int status = ((Integer)condValues[3]).intValue(); //单据状态
		if(status == CMPaccStatus.SAVEACCOUNT.getStatus())
			selectedIndex = 0;
		else if(status == CMPaccStatus.SIGNACCOUNT.getStatus())
			selectedIndex = 1;
		else if(status == CMPaccStatus.SUCCESSACCOUNT.getStatus())
			selectedIndex = 2;
		panel.getCbBoxSignStatus().setSelectedIndex(selectedIndex);
		panel.getCbBoxDateType().setSelectedItem(condValues[4]);  //日期类型
		panel.getRefPanelBeginDate().setText((String)condValues[5]); //起始日期
		panel.getRefPanelEndDate().setText((String)condValues[6]); //终止日期
		panel.getRefPanelSystem().setPKs((String[])condValues[7]); //业务系统pk
		panel.getRefPanelBillType().setPKs((String[])condValues[8]); //单据类型（交易类型）
		chkBoxIncludeAccredit.setSelected((Boolean)condValues[9]); //终止日期
		resetManagerByDlg(colManager, condManager, piManager);
	}

	@Override
	public void showHeaderDataForSubQuery(Object[] condValues, String queryName) {
		showHeaderData();
		int len = condValues.length;
		if(condValues[len-1] != null && condValues[len-1].equals(CURR_FORMAT))
			getFormatComboInHeader().setSelectedItem(condValues[len-1]);
	}

//	@Override
//	/**
//	 * 设置分页查询类型
//	 */
//	public int getQueryType() {
//		return IQueryType.PAGED_ONLY;
//	}
//
//	@Override
//	protected ISubject[] getPagedSubjects() {
//		return new Subject[]{
//				new Subject("date","日期")
//		};
//	}
}