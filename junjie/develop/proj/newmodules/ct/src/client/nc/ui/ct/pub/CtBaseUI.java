package nc.ui.ct.pub;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemListener;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import javax.swing.ListSelectionModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableColumnModelListener;

import nc.bs.bd.b21.BusinessCurrencyRateUtil;
import nc.bs.framework.common.NCLocator;
import nc.itf.arap.pub.IArapBillMapQureyPublic;
import nc.itf.uap.rbac.IUserManageQuery;
import nc.itf.uap.sf.ICreateCorpQueryService;
import nc.ui.bd.b21.CurrParamQuery;
import nc.ui.bd.manage.UIRefCellEditor;
import nc.ui.ct.pub.print.SCMMutiTabPrintDataInterface;
import nc.ui.ct.pub.tools.GenMethod;
import nc.ui.ct.ref.CtTypeRefModel;
import nc.ui.ct.ref.ExpRefModel;
import nc.ui.ct.ref.TermRefModel;
import nc.ui.ic.pub.query.QueryDlgUtil;
import nc.ui.ml.NCLangRes;
import nc.ui.pf.pub.PfUIDataCache;
import nc.ui.pf.query.ICheckRetVO;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.FramePanel;
import nc.ui.pub.ToftPanel;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIComboBox;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UIMenuItem;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.beans.UIScrollPane;
import nc.ui.pub.beans.UITabbedPane;
import nc.ui.pub.beans.UITextField;
import nc.ui.pub.bill.BillActionListener;
import nc.ui.pub.bill.BillBodyMenuListener;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.bill.BillData;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillEditListener;
import nc.ui.pub.bill.BillEditListener2;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillListData;
import nc.ui.pub.bill.BillModel;
import nc.ui.pub.bill.BillModelCellEditableController;
import nc.ui.pub.bill.BillSortListener;
import nc.ui.pub.bill.BillUIUtil;
import nc.ui.pub.bill.IBillItem;
import nc.ui.pub.bill.IBillModelSortPrepareListener;
import nc.ui.pub.bill.IBillRelaSortListener;
import nc.ui.pub.bill.action.BillTableLineAction;
import nc.ui.pub.linkoperate.ILinkAdd;
import nc.ui.pub.linkoperate.ILinkAddData;
import nc.ui.pub.linkoperate.ILinkApprove;
import nc.ui.pub.linkoperate.ILinkApproveData;
import nc.ui.pub.linkoperate.ILinkMaintain;
import nc.ui.pub.linkoperate.ILinkMaintainData;
import nc.ui.pub.linkoperate.ILinkQuery;
import nc.ui.pub.linkoperate.ILinkQueryData;
import nc.ui.pub.msg.PfLinkData;
import nc.ui.pub.query.QueryConditionClient;
import nc.ui.pub.workflownote.FlowStateDlg;
import nc.ui.scm.file.DocumentManager;
import nc.ui.scm.ic.exp.GeneralMethod;
import nc.ui.scm.ic.freeitem.FreeItemRefPane;
import nc.ui.scm.inv.InvTool;
import nc.ui.scm.print.IFreshTsListener;
import nc.ui.scm.pub.CacheTool;
import nc.ui.scm.pub.bill.ButtonTree;
import nc.ui.scm.pub.bill.ScmButtonConst;
import nc.ui.scm.pub.data.CellControl;
import nc.ui.scm.pub.def.DefSetTool;
import nc.ui.scm.pub.invmeas.InvMeasInfo;
import nc.ui.scm.pub.panel.AlreadyAfterEditListener;
import nc.ui.scm.pub.panel.RelationsCal;
import nc.ui.scm.pub.panel.ScmBillCardPanel;
import nc.ui.scm.pub.panel.ScmBillListPanel;
import nc.ui.scm.pub.query.SCMQueryConditionDlg;
import nc.ui.scm.pub.sourceref.ScmCurrLocRateBizDecimalListener;
import nc.ui.scm.pub.sourceref.ScmCurrMnyBizDecimalListener;
import nc.ui.uap.sf.SFClientUtil;
import nc.vo.arap.billtypemap.BillTypeMapVO;
import nc.vo.bd.b06.PsndocVO;
import nc.vo.bd.b21.CurrinfoVO;
import nc.vo.bd.def.DefVO;
import nc.vo.bill.pub.BillUtil;
import nc.vo.ct.pub.ChangeBb1VO;
import nc.vo.ct.pub.ExpBb3VO;
import nc.vo.ct.pub.ExtendManageVO;
import nc.vo.ct.pub.ManageExecVO;
import nc.vo.ct.pub.ManageHeaderVO;
import nc.vo.ct.pub.ManageItemVO;
import nc.vo.ct.pub.ManageVO;
import nc.vo.ct.pub.MemoraBb2VO;
import nc.vo.ct.pub.OverMaxPriceException;
import nc.vo.ct.pub.TermBb4VO;
import nc.vo.dm.pub.DMDataVO;
import nc.vo.dm.pub.DMVO;
import nc.vo.ep.dj.DJZBHeaderVO;
import nc.vo.ep.dj.DJZBVO;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.pp.ask.PriceauditHeaderVO;
import nc.vo.pp.ask.PriceauditMergeVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.CommonConstant;
import nc.vo.pub.ExtendedAggregatedValueObject;
import nc.vo.pub.NullFieldException;
import nc.vo.pub.ProductCode;
import nc.vo.pub.VOStatus;
import nc.vo.pub.ValidationException;
import nc.vo.pub.bill.BillTempletVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.query.ConditionVO;
import nc.vo.pub.query.QueryConditionVO;
import nc.vo.querytemplate.TemplateInfo;
import nc.vo.scm.constant.ScmConst;
import nc.vo.scm.constant.ct.BillState;
import nc.vo.scm.constant.ct.BillType;
import nc.vo.scm.constant.ct.CTExecFlow;
import nc.vo.scm.constant.ct.CTTableCode;
import nc.vo.scm.constant.ct.CtType;
import nc.vo.scm.constant.ct.DBDataLeng;
import nc.vo.scm.constant.ct.OperationState;
import nc.vo.scm.constant.ct.TabState;
import nc.vo.scm.constant.ct.TypeDataCtrl;
import nc.vo.scm.datapower.BtnPowerVO;
import nc.vo.scm.datatype.DataTypeConst;
import nc.vo.scm.ic.bill.FreeVO;
import nc.vo.scm.ic.bill.InvVO;
import nc.vo.scm.ic.exp.ICDateException;
import nc.vo.scm.ic.exp.ICHeaderNullFieldException;
import nc.vo.scm.ic.exp.ICLocatorException;
import nc.vo.scm.ic.exp.ICNullFieldException;
import nc.vo.scm.ic.exp.ICNumException;
import nc.vo.scm.ic.exp.ICPriceException;
import nc.vo.scm.ic.exp.ICRepeatException;
import nc.vo.scm.ic.exp.ICSNException;
import nc.vo.scm.print.PrintConst;
import nc.vo.scm.print.ScmPrintlogVO;
import nc.vo.scm.pu.PUMessageVO;
import nc.vo.scm.pu.RelationsCalVO;
import nc.vo.scm.pub.SCMEnv;
import nc.vo.scm.pub.SaveHintException;
import nc.vo.scm.pub.smart.ObjectUtils;
import nc.vo.scm.relacal.SCMRelationsCal;

/**
 * 功能说明： 作者：王亮
 * 
 * @version 最后修改日期(2002-8-28 11:33:34) 修改人：邵兵 修改时间：2004-12-08 15:22
 *          修改说明：在打印中增加对打印次数的控制实现。
 */
public abstract class CtBaseUI extends ToftPanel implements
		TableColumnModelListener, ItemListener, ChangeListener,
		ListSelectionListener, ICheckRetVO, BillEditListener,
		BillEditListener2, BillSortListener, AlreadyAfterEditListener,
		BillBodyMenuListener, java.awt.event.MouseListener,
		BillModelCellEditableController, nc.ui.scm.pub.bill.IBillExtendFun,
		IBillRelaSortListener, IBillModelSortPrepareListener, ILinkApprove, // 关联审核
		// （审批流）
		ILinkQuery, // 关联查询 （查询使用）
		ILinkMaintain, // 关联查询 （驳回至制单人时使用）
		ILinkAdd,// 价格审批单调用新增界面使用
		BillActionListener

// IBillModelRowStateChangeEventListener

{
	// 计算合同价格优先规则参数
	protected String sContractPriceRule = "";

	protected ButtonTree btTreeBase = null;// 合同基本页签buttonTree

	protected ButtonObject[] m_aryButtonGroup = null;

	// 单据列表panel
	protected ScmBillListPanel m_BillListPanel = null;

	// 单据卡片panel
	protected ScmBillCardPanel m_BillCardPanel = null;

	// 查询对话框
	protected CTBillQueryConditionDlgNew m_QueryConditionDlg = null;

	// 定义费用参照
	protected UIRefPane m_RefExpPane = null;

	protected UIRefCellEditor m_RefExpCellE = null;

	// 定义条款参照
	protected UIRefPane m_RefTermPane = null;

	protected UIRefCellEditor m_RefTermCellE = null;

	// 定义人员参照
	protected UIRefPane m_RefPersonsPane = null;

	protected UIRefCellEditor m_RefPersonsCellE = null;

	// 定义单据的合同类型参照
	protected UIRefPane m_RefCtTypeBillPane = null;

	protected UIRefCellEditor m_RefCtTypeBillCellE = null;

	// 定义查询时的合同类型参照
	protected UIRefPane m_RefCtTypeQryPane = null;

	protected UIRefCellEditor m_RefCtTypeQryCellE = null;

	// 合同操作状态标志
	protected int m_iBillState = 0;

	// 其它页签操作状态标志
	protected int m_iTbState = 0;

	// 合同执行过程flag
	protected String m_sExecFlag = null;

	// 临时存储各个Table中的VO或行号
	protected Vector m_vTableVO = null;

	// 公司主键
	protected String m_sPkCorp = null;

	// 用户名
	protected String m_sUserName = null;

	// 用户主键
	protected String m_sPkUser = null;

	// 系统日期
	protected UFDate m_UFToday = null;

	// 结点编码
	protected String m_sNodeCode = null;

	// 单据类型
	protected String m_sBillType = null;

	// 合同分类参数
	protected int m_iCtType = 0;

	// 临时存储单据
	protected Vector<ExtendManageVO> m_vBillVO = null;

	// m_vListVO中当前单据下标
	protected int m_iId = 0;

	// 当前m_vListVO中单据的数量
	protected int m_iElementsNum = 0;

	// 标志当前操作的页签
	protected int m_iTabbedFlag = 0;

	// 保存上一个点击页签的Index
	protected int m_iTabbedOldFlag = 0;

	// 标志页签Table是否需要重新导入数据
	protected boolean[] m_bIsNeedReInit = null;

	// 标志单据是Card形式,还是List形式
	protected boolean m_bIsCard = true;

	// 标志是否是查询后第一次点击“卡片”按钮
	protected boolean m_bIsFirstClick = false;

	// 标志合同是否可变更
	protected boolean m_bChangeFlag = false;

	// 标志是否出错
	protected boolean m_bErrFlag = false;

	// 标志是否需要触发stateChanged()方法
	protected boolean m_bNeedChange = true;

	// 保存初始的人员Where子句
	protected String m_sPerWhereSql = null;

	// 汇率折算模式
	protected boolean m_bCurrArithMode = true;

	// 存放公司币种的精度[业务精度]
	protected Hashtable m_hCurrDigit = null;

	// 本币小数精度[业务精度]
	protected int m_iMainCurrDigit = 2;

	// 单价小数位
	protected int m_iPriceDigit = 2;

	// 数量小数位
	protected int m_iAmountDigit = 2;

	// protected int[] m_iRateDigit = null;
	protected int m_iRateDigit;

	// //汇率小数精度
	// protected int m_iRateDigit= 4;
	// 默认汇率
	protected UFDouble m_UFCurrRate = null;

	// 本币币种ID
	protected String m_sLocalCurrID = null;

	// 币种对应的汇率
	protected Hashtable m_hRateDigit = null;

	// 换算率
	protected UFDouble m_UFTransRate = null;

	// 打印实体
	protected nc.ui.pub.print.PrintEntry m_print = null;

	// 是否期初
	protected UFBoolean m_UFbIfEarly = null;

	// 单据主键名称及单据代码名称
	protected java.lang.String sCtBillCodeKeyName;

	protected java.lang.String sCtPrimaryKeyName;

	protected boolean bCalType = false; // 真为 主辅币核算 ,假为 单主币核算

	// 辅币对本币的折算模式
	boolean bFracmode = true;

	// business currency rate
	protected BusinessCurrencyRateUtil currArith = null;

	// added by lirr 2008-12-24
	protected BusinessCurrencyRateUtil currRateUtil = null;

	protected boolean m_bRateEnable = false;

	// 折本折辅汇率
	protected UFDouble m_dRate = null;

	// 辅数量小数位`
	protected int m_iFracAmountDigit = 2;

	// 辅币小数精度
	protected int m_iFracCurrDigit = 2;

	// 辅币币种ID
	protected String m_sFracCurrID = null;

	// 辅数量及换算率
	// protected InvMeasRate m_voInvMeas = new InvMeasRate();

	// 显示合同变更历史的对话框
	private CtHistoryDlg m_dlgHistory = null;

	// 存放公司币种的精度[财务精度]
	protected Hashtable m_hCurrDigitcw = null;

	// 记录已经查询过的合同变更历史，在onHistory中初始化。
	private Hashtable m_htChanged = null;

	// 换算率精度
	protected int m_iHslDigit = 2;

	// 本币小数精度[财务精度]
	protected int m_iMainCurrDigitcw = 2;

	// v3.0是否自动送审参数,默认是自动.
	private boolean m_isAutoSendApprove = true;

	// 纪录查询后的查询条件，以便刷新。
	private String m_sQryCondition = null;

	// 时间类
	nc.vo.ct.pub.Timer m_timer = new nc.vo.ct.pub.Timer();

	// 复制页签改动前的VO，以便放弃时恢复[在页签编辑时复制]
	ManageVO m_voBeforeChange = null;

	SCMMutiTabPrintDataInterface m_dataSource = null;

	// 是否保留最初制单人
	protected UFBoolean m_isSaveInitOper = new UFBoolean(true); // 默认为是

	// 列表下的所有数据VO
	protected ArrayList m_alAllVOs = new ArrayList();

	// 目前合同单据模版中只有这四个部分有loadformula.
	// Jun 17, 2005 by Shawbing.
	protected String[] m_headTailFormulas = null; // 单据模版中表头/表尾公式

	protected String[] m_tableFormulas = null; // 单据模版中表体的合同基本公式

	protected String[] m_termFormulas = null; // 单据模版中表体的合同条款公式

	protected String[] m_costFormulas = null; // 单据模版中表体的合同费用公式

	// 采购合同通过复制新增合同,把原来的合同条款的内容也要复制到新的合同中
	// v31sp1需求
	// 实现：新增m_copyedTermVOs记录复制时合同条款的内容
	// 合同基本保存后，如果m_copyedTermVOs不为空，则自动跳转合同条款页签
	// 邵兵 2005-08-26
	private TermBb4VO[] m_copyedTermVOs = null;

	// 是否为批处理 add by liuzy 2007-04-17
	protected boolean isGlobelBatch = false;

	// createExec方法中执行是否通过
	protected boolean isExecPass = true;

	// added by lirr 2008-08-12 合同基本页签 表体“卡片编辑”按钮
	protected UIMenuItem miBoCardEdit = new UIMenuItem(nc.ui.ml.NCLangRes
			.getInstance().getStrByID("common", "SCMCOMMON000000267"));

	// added by lirr 2008-08-12 合同基本页签 表体“重排行号”按钮
	protected UIMenuItem miaddNewRowNo = new UIMenuItem(nc.ui.ml.NCLangRes
			.getInstance().getStrByID("SCMCOMMON", "UPP4008bill-000551"));

	// added by lirr 2008-07-23 是否参照其他单据(请购单，价格审批单)生成合同 ，true为参照
	protected boolean m_bAddFromBillFlag = false;

	// 临时存储单据
	protected Vector<ExtendManageVO> m_vBillVOForRef = null;

	// 临时存储转单前m_vBillVO中的单据
	protected Vector<ExtendManageVO> m_vBillVOBeforTran = null;

	// 辅计量单位
	protected static HashMap m_hmapCTDefaultAssUnit = null;

	// 销售辅计量单位
	protected static HashMap m_hmapCTSoDefaultAssUnit = null;

	private ScmCurrLocRateBizDecimalListener scmCurrRateDigit;

	protected ScmCurrMnyBizDecimalListener scmMnyRateDigit;

	// added by lirr 2009-8-17上午11:25:23 模板vo缓存 减少连接数
	private BillTempletVO billTempletVO = null;

	/**
	 * CtBaseUI 构造子注解。
	 */
	public CtBaseUI() {
		super();
		initialize();
	}

	public CtBaseUI(FramePanel fp) {
		super();
		setFrame(fp);
		initialize();
	}

	@Override
	public void setFrame(FramePanel fp) {
		super.setFrame(fp);
	}

	/**
	 * 编辑后事件。 创建日期：(2001-3-23 2:02:27)
	 * 
	 * @param e
	 *            ufbill.BillEditEvent
	 */
	public void afterEdit(nc.ui.pub.bill.BillEditEvent e) {

		try {
			String sPk = null;
			String strColName = e.getKey().trim();
			int pos = e.getPos();
			int currentRow = 0;
			currentRow = e.getRow();

			if (strColName.equals("astmeasname")) { // 辅单位
				afterAstunitEdit(e);
			} else if (strColName.equals("vfree0")) { // 自由项
				afterFreeItemEdit(getCtBillCardPanel().getBillModel(),
						currentRow);
			} else if (e.getKey().equals("projectname")) {
				sPk = ((UIRefPane) getCtBillCardPanel().getHeadItem(
						"projectname").getComponent()).getRefPK();
				getCtBillCardPanel().getHeadItem("projectid").setValue(sPk);
			} else if (e.getKey().equals("pername")) {
				afterPersonEdit(e, m_bAddFromBillFlag);
			} else if (e.getKey().equals("transpmodename")) {
				sPk = ((UIRefPane) getCtBillCardPanel().getHeadItem(
						"transpmodename").getComponent()).getRefPK();
				getCtBillCardPanel().getHeadItem("transpmode").setValue(sPk);
				afterTraspEdit();
			} else if (e.getKey().equals("deliaddrname")) {
				sPk = ((UIRefPane) getCtBillCardPanel().getHeadItem(
						"deliaddrname").getComponent()).getRefPK();
				getCtBillCardPanel().getHeadItem("deliaddr").setValue(sPk);
			} else if (e.getKey().equals("paytermname")) {
				sPk = ((UIRefPane) getCtBillCardPanel().getHeadItem(
						"paytermname").getComponent()).getRefPK();
				getCtBillCardPanel().getHeadItem("payterm").setValue(sPk);
			} else if (strColName.equals("ct_type"))
				afterCtTypeEdit();

			else if (strColName.equals("custname"))
				afterCustNameEdit(m_bAddFromBillFlag);
			else if (strColName.equals("currname"))
				afterCurrNameEdit(m_bAddFromBillFlag);
			else if (strColName.equals("currrate"))
				afterCurrrateEdit(e);
			/*
			 * else if (e.getKey().equals("taxration")) afterTaxrationEdit(e);
			 */
			else if (e.getKey().equals("depname"))
				afterDepNameEdit();
			else if (e.getKey().equals("subscribedate"))
				afterSubscribeDateEdit(e);
			else if (e.getKey().equals("valdate"))
				afterValDateEdit(e);
			else if (e.getKey().equals("invallidate"))
				afterInvalliDateEdit(e);
			else if (e.getKey().equals("delivdate"))
				afterDelivDateEdit(e);
			else if (e.getKey().equals("sopricecode")) {
				afterPricePolicyEdit(e);
			} else if (strColName.startsWith("def")) {// 自定义项处理zhy
				if (pos == 0) {// 表头
					String sVdefPkKey = "pk_defdoc"
							+ strColName.substring("def".length());
					DefSetTool.afterEditHead(
							getCtBillCardPanel().getBillData(), strColName,
							sVdefPkKey);
				} else if (pos == 1) {// 表体
					String sVdefPkKey = "pk_defdoc"
							+ strColName.substring("def".length());

					// 单据表体使用：afterEditBody(BillModel billModel, int iRow,String
					// sVdefValueKey, String sVdefPkKey)
					DefSetTool
							.afterEditBody(getCtBillCardPanel().getBillModel(),
									currentRow, strColName, sVdefPkKey);
				}
			}
			/*
			 * else if (e.getKey().equals("amount") ||
			 * e.getKey().equals("taxration")){ UFDouble oritaxprice =
			 * (UFDouble)getCtBillCardPanel().getBodyValueAt(currentRow,"oritaxprice");
			 * if (null != oritaxprice) { BillEditEvent evOritaxprice = new
			 * BillEditEvent(
			 * getCtBillCardPanel().getBodyItem("oritaxprice"),oritaxprice,"oritaxprice");
			 * 
			 * calcCtLinePriceMny(evOritaxprice); } }
			 */
			else if (e.getKey().equals("oritaxprice")
					|| e.getKey().equals("oriprice")
					|| e.getKey().equals("orisum")
					|| e.getKey().equals("oritaxsummny")
					|| e.getKey().equals("amount")
					|| e.getKey().equals("taxration")
					|| e.getKey().equals("oritaxmny")
					|| e.getKey().equals("astnum")
					|| e.getKey().equals("transrate")) {
				calcCtLinePriceMny(e);
			} else if (e.getKey().equals("invcode")
					|| e.getKey().equals("invid")) {
				String[] sInvids = ((UIRefPane) getCtBillCardPanel()
						.getBodyItem(e.getKey()).getComponent()).getRefPKs();

				if (sInvids == null || sInvids.length == 0) {
					return;
				}
				CTTool tool = new CTTool();
				Object[] objAssunit = tool.getInvids(getCtBillCardPanel(),
						m_sBillType);
				InvTool.loadBatchInvConvRateInfo((String[]) objAssunit[0],
						(String[]) objAssunit[1]);

				for (int i = 0; i < sInvids.length; i++) {
					if (null != getCtBillCardPanel().getBodyItem("transrate")
							.getComponent()) {
						UFDouble ufdTransrate = (UFDouble) getCtBillCardPanel()
								.getBodyValueAt(i + e.getRow(), "transrate");

						BillEditEvent evTransrate = new BillEditEvent(
								getCtBillCardPanel().getBodyItem("transrate"),
								ufdTransrate, "transrate", i + e.getRow());
						calcCtLinePriceMny(evTransrate);
					}
				}
			}
		} catch (Exception ex) {
			this.showErrorMessage(ex.toString());
			handleException(ex);
		}
	}

	/**
	 * 功能说明：根据合同类型的存货控制方式确定表体的存货编码和存货分类编码字段是否可编辑 创建日期：(2001-3-23 2:02:27)
	 * 
	 * @param e
	 *            ufbill.BillEditEvent
	 */
	public boolean beforeEdit(nc.ui.pub.bill.BillEditEvent e) {

		if (m_iBillState == OperationState.EDIT
				|| m_iBillState == OperationState.ADD
				|| m_iBillState == OperationState.CHANGE) {
			getCtBillCardPanel().superStopEditing();

			Object oRefValue = ((UIRefPane) getCtBillCardPanel().getHeadItem(
					"ct_type").getComponent()).getRefValue("ninvctlstyle");
			int iRow = e.getRow();

			if (e.getKey().equals("invcode")) {
				if (oRefValue == null) {
					MessageDialog.showHintDlg(this, null, nc.ui.ml.NCLangRes
							.getInstance().getStrByID("4020pub",
									"UPP4020pub-000026")/* @res "请先选择合同类型" */);
					return false;
				}

				// 根据合同类型的存货控制方式设置存货编码和存货分类编码是否可编辑
				int iInvctlstyle = Integer.parseInt(oRefValue.toString());

				// 如果合同类型的存货控制方式为存货
				if (iInvctlstyle == 0) {
					getCtBillCardPanel().getBodyItem("invclasscode")
							.setEnabled(false);
					getCtBillCardPanel().getBodyItem("invcode")
							.setEnabled(true);

					// 设置存货分类编码，存货分类名称为空
					getCtBillCardPanel().setBodyValueAt(null, iRow,
							"invclasscode");
					getCtBillCardPanel().setBodyValueAt(null, iRow,
							"invclassname");
					getCtBillCardPanel().setBodyValueAt(null, iRow, "invclid");

					// 将上次选中的存货清除
					// UIRefPane refInv = (UIRefPane) getCtBillCardPanel()
					// .getBodyItem("invcode").getComponent();
					// refInv.setPK(null);
					// refInv.getRefModel().setSelectedData(null);
				}

				// 如果合同类型的存货控制方式为存货分类
				else if (iInvctlstyle == 1) {
					getCtBillCardPanel().getBodyItem("invclasscode")
							.setEnabled(true);
					getCtBillCardPanel().getBodyItem("invcode").setEnabled(
							false);
					// 设置存货编码，存货名称,型号，规格，数量,计量单位为空
					getCtBillCardPanel().setBodyValueAt(null, iRow, "invcode");
					getCtBillCardPanel().setBodyValueAt(null, iRow, "invname");
					getCtBillCardPanel().setBodyValueAt(null, iRow, "invid");
					getCtBillCardPanel().setBodyValueAt(null, iRow, "spec");
					getCtBillCardPanel().setBodyValueAt(null, iRow, "mod");

					getCtBillCardPanel().setBodyValueAt(null, iRow, "measname");

				}
				// 如果合同类型的存货控制方式为空。将存货分类和存货都设置为空，不可编辑。
				else if (iInvctlstyle == 2) {
					getCtBillCardPanel().getBodyItem("invclasscode")
							.setEnabled(false);
					getCtBillCardPanel().getBodyItem("invcode").setEnabled(
							false);
					// 设置存货分类编码，存货分类名称为空
					getCtBillCardPanel().setBodyValueAt(null, iRow,
							"invclasscode");
					getCtBillCardPanel().setBodyValueAt(null, iRow,
							"invclassname");
					getCtBillCardPanel().setBodyValueAt(null, iRow, "invclid");
					// 设置存货编码，存货名称,型号，规格，数量,计量单位为空
					getCtBillCardPanel().setBodyValueAt(null, iRow, "invcode");
					getCtBillCardPanel().setBodyValueAt(null, iRow, "invname");
					getCtBillCardPanel().setBodyValueAt(null, iRow, "invid");
					getCtBillCardPanel().setBodyValueAt(null, iRow, "spec");
					getCtBillCardPanel().setBodyValueAt(null, iRow, "mod");

					getCtBillCardPanel().setBodyValueAt(null, iRow, "measname");
				}
			}

			else if (e.getKey().equals("invclasscode")) {
				if (oRefValue == null) {
					MessageDialog.showHintDlg(this, null, nc.ui.ml.NCLangRes
							.getInstance().getStrByID("4020pub",
									"UPP4020pub-000026")/* @res "请先选择合同类型" */);
					return false;
				}

				// 根据合同类型的存货控制方式设置存货编码和存货分类编码是否可编辑
				int iInvctlstyle = Integer.parseInt(oRefValue.toString());

				// 合同类型的存货控制方式为合同存货
				if (iInvctlstyle == 0) {
					getCtBillCardPanel().getBodyItem("invclasscode")
							.setEnabled(false);
					getCtBillCardPanel().getBodyItem("invcode")
							.setEnabled(true);
					// 设置存货分类编码，存货分类名称为空
					getCtBillCardPanel().setBodyValueAt(null, iRow,
							"invclasscode");
					getCtBillCardPanel().setBodyValueAt(null, iRow,
							"invclassname");
					getCtBillCardPanel().setBodyValueAt(null, iRow, "invclid");

				}
				// 合同类型的存货控制方式为存货分类
				else if (iInvctlstyle == 1) {
					getCtBillCardPanel().getBodyItem("invclasscode")
							.setEnabled(true);
					getCtBillCardPanel().getBodyItem("invcode").setEnabled(
							false);
					// 设置存货编码，存货名称,型号，规格，数量,计量单位为空
					getCtBillCardPanel().setBodyValueAt(null, iRow, "invcode");
					getCtBillCardPanel().setBodyValueAt(null, iRow, "invname");
					getCtBillCardPanel().setBodyValueAt(null, iRow, "invid");
					getCtBillCardPanel().setBodyValueAt(null, iRow, "spec");
					getCtBillCardPanel().setBodyValueAt(null, iRow, "mod");
					// getCtBillCardPanel().setBodyValueAt(null, iRow,
					// "amount");
					getCtBillCardPanel().setBodyValueAt(null, iRow, "measname");

				}
				// 如果合同类型的存货控制方式为空。将存货分类和存货都设置为空，不可编辑。
				else if (iInvctlstyle == 2) {
					getCtBillCardPanel().getBodyItem("invclasscode")
							.setEnabled(false);
					getCtBillCardPanel().getBodyItem("invcode").setEnabled(
							false);
					// 设置存货分类编码，存货分类名称为空
					getCtBillCardPanel().setBodyValueAt(null, iRow,
							"invclasscode");
					getCtBillCardPanel().setBodyValueAt(null, iRow,
							"invclassname");
					getCtBillCardPanel().setBodyValueAt(null, iRow, "invclid");
					// 设置存货编码，存货名称,型号，规格，数量,计量单位为空
					getCtBillCardPanel().setBodyValueAt(null, iRow, "invcode");
					getCtBillCardPanel().setBodyValueAt(null, iRow, "invname");
					getCtBillCardPanel().setBodyValueAt(null, iRow, "invid");
					getCtBillCardPanel().setBodyValueAt(null, iRow, "spec");
					getCtBillCardPanel().setBodyValueAt(null, iRow, "mod");

					getCtBillCardPanel().setBodyValueAt(null, iRow, "measname");
				}

			} else if (e.getKey().equals("vfree0")) {
				// 存货管理主键
				String sMangId = (String) getCtBillCardPanel().getBodyValueAt(
						iRow, "invid");
				if (sMangId == null)
					return false;
				return beforeEditBodyInvFreeItem(e, sMangId);
			}

			else if (e.getKey().equals("vbatchcode")) {
				// 存货管理主键
				String sMangId = (String) getCtBillCardPanel().getBodyValueAt(
						iRow, "invid");
				if (sMangId == null)
					return false;

				if (InvTool.isBatchManaged(sMangId))
					return true;
				else
					return false;
			}

			else if (e.getKey().startsWith("def")) {
				BillItem item = getCtBillCardPanel().getBodyItem(e.getKey());
				if (item != null && item.getComponent() instanceof UIRefPane) {
					// added by lirr 2009-05-25 自定义项参照无法删除
					((UIRefPane) getCtBillCardPanel().getBodyItem(
							CTTableCode.BASE, e.getKey()).getComponent())
							.setEditable(true);
					String index = e.getKey().substring(3);
					String pkdef = (String) getCtBillCardPanel()
							.getBodyValueAt(e.getRow(), "pk_defdoc" + index);
					Object defvalues = getCtBillCardPanel().getBodyValueAt(
							e.getRow(), e.getKey());
					if (pkdef != null && pkdef.trim().length() > 0
							&& defvalues != null
							&& defvalues.toString().trim().length() > 0) {
						try {
							((UIRefPane) item.getComponent()).setPK(pkdef);
						} catch (Exception ee) {
							SCMEnv.error(ee.getMessage());
						}
					}
				}

			}

			return getCtBillCardPanel().getBodyItem(CTTableCode.BASE,
					e.getKey()).isEnabled();
		}

		return false;
	}

	/*
	 * 功能： 编辑前处理。 该函数主要用于处理点开自由项参照时的一些编辑动作. 邵兵 2005-08-17 @parameter: e
	 * BillEditEvent 捕捉到的BillEditEvent事件 sMangId String 存货管理主键
	 */
	private boolean beforeEditBodyInvFreeItem(BillEditEvent e, String sMangId) {

		int iRow = e.getRow();
		if (iRow < 0) {
			return false;
		}

		if (InvTool.isFreeMngt(sMangId)) {

			// 自由项所需信息VO
			// 存货至少包括 存货ID，存货编码, 存货名称, 存货规格, 存货型号,是否自由项管理
			InvVO invVO = new InvVO();

			// 为自由项管理
			invVO.setIsFreeItemMgt(new Integer(1));
			// 存货管理ID
			invVO.setCinventoryid(sMangId);

			// CODE 存货编码
			Object strTemp = getCtBillCardPanel().getBodyValueAt(iRow,
					"invcode");
			invVO
					.setCinventorycode(strTemp == null ? null : strTemp
							.toString());

			// NAME 存货名称
			strTemp = getCtBillCardPanel().getBodyValueAt(iRow, "invname");
			invVO.setInvname(strTemp == null ? null : strTemp.toString());

			// SPEC 规格
			strTemp = getCtBillCardPanel().getBodyValueAt(iRow, "spec");
			invVO.setInvspec(strTemp == null ? null : strTemp.toString());

			// TYPE 型号
			strTemp = getCtBillCardPanel().getBodyValueAt(iRow, "mod");
			invVO.setInvtype(strTemp == null ? null : strTemp.toString());

			// FreeVO
			FreeVO voFree = InvTool.getInvFreeVO(sMangId);
			if (voFree != null) {
				voFree.setVfree1((String) getCtBillCardPanel().getBodyValueAt(
						iRow, "vfree1"));
				voFree.setVfree2((String) getCtBillCardPanel().getBodyValueAt(
						iRow, "vfree2"));
				voFree.setVfree3((String) getCtBillCardPanel().getBodyValueAt(
						iRow, "vfree3"));
				voFree.setVfree4((String) getCtBillCardPanel().getBodyValueAt(
						iRow, "vfree4"));
				voFree.setVfree5((String) getCtBillCardPanel().getBodyValueAt(
						iRow, "vfree5"));
			}
			// 设置FreeVO
			invVO.setFreeItemVO(voFree);
			((FreeItemRefPane) getCtBillCardPanel().getBodyItem("vfree0")
					.getComponent()).setFreeItemParam(invVO);
			((FreeItemRefPane) getCtBillCardPanel().getBodyItem("vfree0")
					.getComponent()).setButtonVisible(true);

			return true;

		} else {
			((FreeItemRefPane) getCtBillCardPanel().getBodyItem("vfree0")
					.getComponent()).setButtonVisible(false);
			return false;
		}
	}

	/**
	 * 行改变事件。 创建日期：(2001-3-23 2:02:27)
	 * 
	 * @param e
	 *            ufbill.BillEditEvent
	 */
	public void bodyRowChange(nc.ui.pub.bill.BillEditEvent e) {

	}

	/**
	 * 功能说明：保存前检查 创建日期：(2002-4-28 10:57:38)
	 */
	private boolean checkSave(ManageVO manageVO) {

		try {
			String sMessage = null;
			String sCurrrate = getCtBillCardPanel().getHeadItem("currrate")
					.getValue();
			if (sCurrrate == null
					|| (sCurrrate.equals(DataTypeConst.UFDOUBLE_0))) {

				sMessage = nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"4020pub", "UPP4020pub-000214")/*
														 * @res
														 * "当前合同控制数量，数量不能为空或零！"
														 */;
			}

			// added by lirr 2009-01-08 委外模块没有启动时 不能勾选“是否委外”
			String sBsc = null;
			if (null != getCtBillCardPanel().getHeadItem("bsc")) {
				sBsc = getCtBillCardPanel().getHeadItem("bsc").getValue();
			}
			if (sBsc != null && StringUtil.matchIgnoreCase(sBsc, "true")) {
				ICreateCorpQueryService srv = (ICreateCorpQueryService) NCLocator
						.getInstance().lookup(
								ICreateCorpQueryService.class.getName());
				if (!srv.isEnabled(m_sPkCorp, ProductCode.PROD_SC)) {
					sMessage = nc.ui.ml.NCLangRes.getInstance().getStrByID(
							"4020pub", "UPP4020pub-000276")/*
															 * @res
															 * "委外模块没有启动，采购合同不能做委外操作！"
															 */;
					MessageDialog.showErrorDlg(this, null, sMessage);
					return false;
				}
			}

			// 根据合同类型判断
			int iInvctlstyle = Integer
					.parseInt(((UIRefPane) getCtBillCardPanel().getHeadItem(
							"ct_type").getComponent()).getRefValue(
							"ninvctlstyle").toString());

			int iDatactlstyle = Integer
					.parseInt(((UIRefPane) getCtBillCardPanel().getHeadItem(
							"ct_type").getComponent()).getRefValue(
							"ndatactlstyle").toString());

			// modify by liuzy 2007-04-27
			// 修改原因:合同保存后再修改,同时添加两个存货,再删除原存货,保存报错
			// 错误原因:原代码没有校验VO状态
			// ManageItemVO[] bodyVO = (ManageItemVO[])
			// manageVO.getChildrenVO();
			// 过滤状态后的VO数组
			ManageItemVO[] bodyVO = null;
			// 未过滤状态的VO数组
			ManageItemVO[] allBodyVO = (ManageItemVO[]) manageVO
					.getChildrenVO();

			// 过滤掉状态为Delete的VO
			if (allBodyVO != null && allBodyVO.length > 0) {
				// 确定过滤后的数组长度
				int len = 0;
				for (int i = 0, j = allBodyVO.length; i < j; i++) {
					if (allBodyVO[i].getStatus() != nc.vo.pub.VOStatus.DELETED)
						len++;
				}

				bodyVO = new ManageItemVO[len];
				// 设置新数组
				int k = 0;// 新数组下标
				for (int i = 0; i < len; i++) {
					if (allBodyVO[i].getStatus() != nc.vo.pub.VOStatus.DELETED) {
						bodyVO[k] = allBodyVO[i];
						k++;
					}
				}
			}

			// 判断合同是否有存货协议
			if (bodyVO.length == 0 || bodyVO == null) {
				sMessage = nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"4020pub", "UPP4020pub-000027")/* @res "签订的合同不能没有存货协议！" */;
				MessageDialog.showErrorDlg(this, null, sMessage);
				return false;
			}

			// 判断主辅计量存货，辅单位是否输入 added by lirr 2009-01-06
			// added by lirr 2009-9-22上午10:25:24 一次加载缓存
			HashSet<String> hsInvbasidsAll = new HashSet<String>();
			for (ManageItemVO vo : bodyVO) {
				hsInvbasidsAll.add((String) vo.getAttributeValue("invbasid"));
			}
			if (null != hsInvbasidsAll && hsInvbasidsAll.size() > 0) {
				String[] sInvbasidsAll = new String[hsInvbasidsAll.size()];
				hsInvbasidsAll.toArray(sInvbasidsAll);
				InvTool.loadBatchAssistManaged(sInvbasidsAll);
			}
			for (int i = 0; i < bodyVO.length; i++) {
				if (InvTool.isAssUnitManaged((String) bodyVO[i]
						.getAttributeValue("invbasid"))
						&& null == (String) bodyVO[i]
								.getAttributeValue("astmeasname")) {

					sMessage = NCLangRes.getInstance().getStrByID(
							"4020pub",
							"UPP4020pub-000275",
							null,
							new String[] { (String) bodyVO[i]
									.getAttributeValue("crowno") })/* 第{0}行存货为主辅计量存货，辅单位名称不能为空 */;
					MessageDialog.showErrorDlg(this, null, sMessage);
					return false;
				}
			}

			Vector vID = new Vector();
			switch (iInvctlstyle) { // 控制方式为存货
			case 0:
				for (int i = 0; i < bodyVO.length; i++) {
					if (bodyVO[i].getInvid() == null
							|| bodyVO[i].getInvid().toString().length() == 0) {
						sMessage = nc.ui.ml.NCLangRes.getInstance().getStrByID(
								"4020pub", "UPP4020pub-000028")/*
																 * @res
																 * "存货不能为空！"
																 */;
						MessageDialog.showErrorDlg(this, null, sMessage);
						return false;
					}
					if (bodyVO[i].getTaxration() == null
							|| bodyVO[i].getTaxration().toString().length() <= 0) {
						sMessage = nc.ui.ml.NCLangRes.getInstance().getStrByID(
								"4020pub", "UPP4020pub-000029")/*
																 * @res
																 * "税率不能为空！"
																 */;
						MessageDialog.showErrorDlg(this, null, sMessage);
						return false;
					}
					vID.addElement(bodyVO[i].getInvid());
				}

				// 暂时去掉存货是否重复的判断，改在后台检查
				// 判断存货ID是否重复
				// for (int i= 0; i < bodyVO.length; i++)
				// {
				// if (isRepeatCode(vID, bodyVO[i].getInvid(), "Manage"))
				// {
				// MessageDialog.showWarningDlg(this, "输入错误", "不能输入重复的存货");
				// showHintMessage("输入错误！");
				// return false;
				// }
				// }

				break;
			// 控制方式为存货分类
			case 1:

				for (int i = 0; i < bodyVO.length; i++) {

					if (bodyVO[i].getInvclid() == null
							|| bodyVO[i].getInvclid().toString().length() == 0) {
						sMessage = nc.ui.ml.NCLangRes.getInstance().getStrByID(
								"4020pub", "UPP4020pub-000030")/*
																 * @res
																 * "存货分类不能为空！"
																 */;
						MessageDialog.showErrorDlg(this, null, sMessage);
						return false;
					}

					vID.addElement(bodyVO[i].getInvclid());
				}

				// //判断存货分类ID是否重复
				// for (int i= 0; i < bodyVO.length; i++)
				// {
				// if (isRepeatCode(vID, bodyVO[i].getInvclid(), "Manage"))
				// {
				// MessageDialog.showWarningDlg(this, "输入错误", "不能输入重复的存货分类");
				// showHintMessage("输入错误！");
				// return false;
				// }
				// }

				break;
			}

			// 数据控制类型
			switch (iDatactlstyle) {

			case TypeDataCtrl.PRICE: // 单价
				for (int i = 0; i < bodyVO.length; i++) {
					if (bodyVO[i].getOriprice() == null
							|| bodyVO[i].getOriprice().equals(
									DataTypeConst.UFDOUBLE_0)) {

						sMessage = nc.ui.ml.NCLangRes.getInstance().getStrByID(
								"4020pub", "UPP4020pub-000213")/*
																 * @res
																 * "当前合同控制单价，单价不能为空或零！"
																 */;
						MessageDialog.showErrorDlg(this, null, sMessage);
						return false;
					}
				}
				break;

			case TypeDataCtrl.NUM: // 数量
				for (int i = 0; i < bodyVO.length; i++) {
					if (bodyVO[i].getAmount() == null
							|| bodyVO[i].getAmount().equals(
									DataTypeConst.UFDOUBLE_0)) {

						sMessage = nc.ui.ml.NCLangRes.getInstance().getStrByID(
								"4020pub", "UPP4020pub-000214")/*
																 * @res
																 * "当前合同控制数量，数量不能为空或零！"
																 */;
						MessageDialog.showErrorDlg(this, null, sMessage);
						return false;
					}
				}
				break;

			case TypeDataCtrl.MONEY: // 金额
				for (int i = 0; i < bodyVO.length; i++) {
					if (bodyVO[i].getOrisum() == null
							|| bodyVO[i].getOrisum().equals(
									DataTypeConst.UFDOUBLE_0)) {
						sMessage = nc.ui.ml.NCLangRes.getInstance().getStrByID(
								"4020pub", "UPP4020pub-000215")/*
																 * @res
																 * "当前合同控制金额，金额不能为空或零！"
																 */;
						MessageDialog.showErrorDlg(this, null, sMessage);
						return false;
					}
				}
				break;

			case TypeDataCtrl.PRICE_NUM: // 单价+数量
				for (int i = 0; i < bodyVO.length; i++) {
					if (bodyVO[i].getOriprice() == null
							|| bodyVO[i].getAmount() == null
							|| bodyVO[i].getOriprice().equals(
									DataTypeConst.UFDOUBLE_0)
							|| bodyVO[i].getAmount().equals(
									DataTypeConst.UFDOUBLE_0)) {

						sMessage = nc.ui.ml.NCLangRes.getInstance().getStrByID(
								"4020pub", "UPP4020pub-000216")/*
																 * @res
																 * "当前合同控制单价及数量，单价或数量不能为空或零！"
																 */;
						MessageDialog.showWarningDlg(this, null, sMessage);
						return false;
					}
				}
				break;

			case TypeDataCtrl.PRICE_MONEY: // 单价+金额
				for (int i = 0; i < bodyVO.length; i++) {
					if (bodyVO[i].getOriprice() == null
							|| bodyVO[i].getOrisum() == null
							|| bodyVO[i].getOriprice().equals(
									DataTypeConst.UFDOUBLE_0)
							|| bodyVO[i].getOrisum().equals(
									DataTypeConst.UFDOUBLE_0)) {

						sMessage = nc.ui.ml.NCLangRes.getInstance().getStrByID(
								"4020pub", "UPP4020pub-000217")/*
																 * @res
																 * "当前合同控制单价及金额，单价或金额不能为空或零！"
																 */;
						MessageDialog.showWarningDlg(this, null, sMessage);
						return false;
					}
				}
				break;

			case TypeDataCtrl.NUM_MONEY: // 数量+金额
				for (int i = 0; i < bodyVO.length; i++) {
					if (bodyVO[i].getAmount() == null
							|| bodyVO[i].getOrisum() == null
							|| bodyVO[i].getAmount().equals(
									DataTypeConst.UFDOUBLE_0)
							|| bodyVO[i].getOrisum().equals(
									DataTypeConst.UFDOUBLE_0)) {

						sMessage = nc.ui.ml.NCLangRes.getInstance().getStrByID(
								"4020pub", "UPP4020pub-000218")/*
																 * @res
																 * "当前合同控制数量及金额，数量或金额不能为空或零！"
																 */;
						MessageDialog.showWarningDlg(this, null, sMessage);
						return false;
					}
				}
				break;

			case TypeDataCtrl.PRICE_NUM_MONEY: // 单价+数量+金额
				for (int i = 0; i < bodyVO.length; i++) {
					if (bodyVO[i].getAmount() == null
							|| bodyVO[i].getOriprice() == null
							|| bodyVO[i].getOrisum() == null
							|| bodyVO[i].getAmount().equals(
									DataTypeConst.UFDOUBLE_0)
							|| bodyVO[i].getOriprice().equals(
									DataTypeConst.UFDOUBLE_0)
							|| bodyVO[i].getOrisum().equals(
									DataTypeConst.UFDOUBLE_0)) {

						sMessage = nc.ui.ml.NCLangRes.getInstance().getStrByID(
								"4020pub", "UPP4020pub-000219")/*
																 * @res
																 * "当前合同控制单价、数量及金额，单价，数量或金额不能为空或零！"
																 */;
						MessageDialog.showWarningDlg(this, null, sMessage);
						return false;
					}
				}
			}

		} catch (Exception e) {
			nc.vo.scm.pub.SCMEnv.out(e);
		}

		return checkVO(manageVO, getCtBillCardPanel());
	}

	/**
	 * 功能说明：创建执行过程记录 创建日期：(2002-4-8 14:12:59)
	 * 
	 * @return java.lang.String 作者：王亮
	 */
	// protected void createExec(ManageVO mVO) {
	//
	// if (mVO == null)
	// return;
	//
	// try {
	// String sPk_ct = mVO.getParentVO().getPrimaryKey();
	// // 计划合同生效时间
	// UFDate UFValDate = new UFDate(mVO.getParentVO().getAttributeValue(
	// "valdate").toString());
	//
	// // 计划合同终止时间
	// UFDate UFInvalDate = new UFDate(mVO.getParentVO()
	// .getAttributeValue("invallidate").toString());
	//
	// String sReason = null;
	// String sTitle = nc.ui.ml.NCLangRes.getInstance().getStrByID(
	// "common", "UC000-0000900")/* @res "原因" */;
	// String sMessage = null;
	// // 执行流程名称
	// String sExecName = null;
	//
	// if (CTExecFlow.VALIDATE.equals(m_sExecFlag)) { // "实际生效"
	// if (UFValDate.compareTo(ClientEnvironment.getInstance()
	// .getDate()) != 0) {
	// if (isGlobelBatch) {
	// // 如果是批处理，直接返回给调用方，不提示给用户对话框
	// isExecPass = false;
	// return;
	// }
	// sMessage = nc.ui.ml.NCLangRes.getInstance().getStrByID(
	// "4020pub", "UPP4020pub-000038")/*
	// * @res
	// * "实际生效日期与计划生效日期不符，请输入原因"
	// */;
	// sReason = (String) MessageDialog.showInputDlg(this, sTitle,
	// sMessage, null, 120);
	// }
	// sExecName = CTExecFlow.getValidateName()/* @res "实际生效" */;
	//
	// } else if (CTExecFlow.TERMINATE.equals(m_sExecFlag)) { // "实际终止"
	// if (UFInvalDate.compareTo(ClientEnvironment.getInstance()
	// .getDate()) != 0) {
	// sMessage = nc.ui.ml.NCLangRes.getInstance().getStrByID(
	// "4020pub", "UPP4020pub-000039")/*
	// * @res
	// * "实际终止日期与计划终止日期不符，请输入原因"
	// */;
	// sReason = (String) MessageDialog.showInputDlg(this, sTitle,
	// sMessage, null, 120);
	// }
	// sExecName = CTExecFlow.geTerminateName()/* @res "实际终止" */;
	//
	// } else if (CTExecFlow.FREEZE.equals(m_sExecFlag)) { // "冻结"
	// sMessage = nc.ui.ml.NCLangRes.getInstance().getStrByID(
	// "4020pub", "UPP4020pub-000040")/* @res "请输入冻结合同的原因" */;
	// sReason = (String) MessageDialog.showInputDlg(this, sTitle,
	// sMessage, null, 120);
	// sExecName = CTExecFlow.getFreezeName()/* @res "冻结" */;
	//
	// } else if (CTExecFlow.UNFREEZE.equals(m_sExecFlag)) { // "解冻"
	// sMessage = nc.ui.ml.NCLangRes.getInstance().getStrByID(
	// "4020pub", "UPP4020pub-000041")/* @res "请输入解冻合同的原因" */;
	// sReason = (String) MessageDialog.showInputDlg(this, sTitle,
	// sMessage, null, 120);
	// sExecName = CTExecFlow.getUnfreezeName()/* @res "解冻" */;
	// }
	//
	// ManageExecVO[] execVO = new ManageExecVO[1];
	// execVO[0] = new ManageExecVO();
	// execVO[0].setAttributeValue("pk_ct_manage", sPk_ct);
	// execVO[0].setAttributeValue("execflow", sExecName);
	// execVO[0].setAttributeValue("execdate", ClientEnvironment
	// .getInstance().getDate());
	//
	// if (sReason != null)
	// execVO[0].setAttributeValue("execreason", sReason);
	// execVO[0].setPk_corp(m_sPkCorp);
	// // 保存.
	// execVO = ContractWriterHelper.insertManageExecs(execVO);
	// // 将新建的执行VO追加到已有的执行VO中.
	// ManageExecVO[] oldExec = mVO.getManageExecs();
	// if (oldExec != null && oldExec.length > 0) {
	// ManageExecVO[] voexec = new ManageExecVO[oldExec.length + 1];
	// for (int i = 0; i < oldExec.length; i++)
	// voexec[i] = oldExec[i];
	// voexec[oldExec.length] = execVO[0];
	// mVO.setManageExecs(voexec);
	// } else {
	// mVO.setManageExecs(execVO);
	// }
	//
	// } catch (Exception e) {
	// nc.vo.scm.pub.SCMEnv.out(e);
	// }
	//
	// }
	/**
	 * 此处插入方法说明:查询合同的表头时间戳 创建日期：(2003-11-3 17:04:19)
	 * 
	 * @param vo
	 *            nc.vo.ct.pub.ManageVO 修改日期 2009-8-21下午04:36:49 修改人，lirr
	 *            修改原因，注释标志： 不再从后台查询ts而是在调用此方法前的动作中返回ts
	 * @param vo
	 */
	protected void freshStatusTs(ManageVO vo, String ts) {
		// String pk = (String)
		// vo.getParentVO().getAttributeValue("pk_ct_manage");
		// if (pk != null) {
		try {
			// String ts = ContractQueryHelper.qryStatusTs(pk);

			getCtBillCardPanel().setHeadItem("ts", ts);
			vo.getParentVO().setAttributeValue("ts", ts);
		} catch (Exception e) {
			nc.vo.scm.pub.SCMEnv.out(e);
		}
		// }
	}

	/**
	 * 设置签字时间 taudittime
	 */
	protected void setAuditTime(ManageVO mVO, String sTS) {
		getCtBillCardPanel().setTailItem("taudittime", sTS);
		mVO.getParentVO().setAttributeValue("taudittime", sTS);
	}

	/**
	 * 功能说明：得到本公司所有币种对应的汇率 创建日期：(2001-12-26 17:20:55)
	 */
	protected void getAllRateDigit() {
	}

	/**
	 * 功能说明：得到币种对应的精度 创建日期：(2001-11-27 9:55:51)
	 */
	protected void getCurrDigit() {
		try {
			m_hCurrDigit = new Hashtable();
			m_hCurrDigitcw = new Hashtable();
			// 获得该币种的小数位数
			/*
			 * nc.vo.bd.b20.CurrtypeVO[] vos = nc.ui.bd.b20.CurrtypeBO_Client
			 * .queryAll(m_sPkCorp);
			 */
			// modified by lirr 2009-8-20下午06:39:16 减少连接数
			nc.vo.bd.b20.CurrtypeVO[] vos = nc.ui.bd.b21.CurrtypeQuery
					.getInstance().getAllCurrtypeVOs();

			if (vos != null || vos.length > 0) {
				for (int i = 0; i < vos.length; i++) {
					// 业务精度
					m_hCurrDigit.put(vos[i].getPk_currtype(), vos[i]
							.getCurrbusidigit());
					// 财务精度
					m_hCurrDigitcw.put(vos[i].getPk_currtype(), vos[i]
							.getCurrdigit());
				}

			} else {
				String sMessage = nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"SCMCOMMON", "UPPSCMCommon-000015")/*
															 * @res
															 * "币种未定义，请先定义币种！"
															 */;
				MessageDialog.showErrorDlg(this, null, sMessage);
			}

		} catch (Exception e) {
			String sMessage = nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"4020pub", "UPP4020pub-000020")/* @res "访问数据库失败！" */;
			MessageDialog.showErrorDlg(this, null, sMessage + "\n"
					+ e.getMessage());
		}
	}

	/**
	 * 功能说明：获得客商对应币种 创建日期：(2001-12-11 13:18:01)
	 * 
	 * @return java.lang.String
	 * @param manPk
	 *            java.lang.String
	 */
	protected String getCurrid(String manPk) {
		String currId = null;
		try {
			currId = ContractQueryHelper.findCurrByCustid(manPk);
		} catch (Exception e) {
			nc.vo.scm.pub.SCMEnv.out(e);

		}
		return currId;
	}

	/**
	 * 功能说明：由币种，日期得到汇率精度，汇率和折算模式 创建日期：(2001-11-27 9:55:51)
	 */
	protected void getCurrInfo(String pkCurrid, String subDate) {
	}

	/**
	 * 此处插入方法说明。 功能： 参数： 返回： 例外： 日期：(2002-8-28 11:21:41) 修改日期，修改人，修改原因，注释标志：
	 */
	protected void getEnvironment() {
		ClientEnvironment env = getClientEnvironment();
		// 公司主键
		m_sPkCorp = env.getCorporation().getPk_corp().toString();
		// 得到用户名和用户主键
		m_sUserName = env.getUser().getUserName();
		m_sPkUser = env.getUser().getPrimaryKey();
		// 当前日期
		m_UFToday = env.getDate();

		// 获得参数
		// 计算合同价格优先规则参数
		// CT001:计算合同价格优先规则,系统默认参数值为含税价格优先，可在含税单价或无税单价中选取。
		// CT002:单据保存时是否触发审批流
		// CT010:是否保留最初制单人
		try {
			/*
			 * String[] sParam = ContractQueryHelper.getSYSParaString(m_sPkCorp,
			 * new String[] { "CT001", "CT002", "CT010" });
			 */

			Hashtable htPara = nc.ui.pub.para.SysInitBO_Client
					.queryBatchParaValues(m_sPkCorp, new String[] { "CT001",
							"CT002", "CT010" });
			String[] sParam = new String[3];
			sParam[0] = (String) htPara.get("CT001");
			sParam[1] = (String) htPara.get("CT002");
			sParam[2] = (String) htPara.get("CT010");
			sContractPriceRule = sParam[0];

			// 是否自动送审参数,默认是自动.
			String sAutoSend = sParam[1];
			// if (sAutoSend != null && sAutoSend.equals("否"))
			// /*-=notranslate=-*/
			// modified by lirr 2009-8-25下午03:14:07 queryBatchParaValues取出为N
			if (sAutoSend != null && sAutoSend.equals("N")) /*-=notranslate=-*/
				m_isAutoSendApprove = false;

			// 是否保留最初制单人
			String sIsSaveInitOper = sParam[2];
			if (sIsSaveInitOper != null && sIsSaveInitOper.equals("N"))
				m_isSaveInitOper = new UFBoolean("false");
		} catch (Exception e) {
			sContractPriceRule = "含税单价"; /*-=notranslate=-*/
		}
	}

	/**
	 * 获得查询的条件 创建日期：(2001-9-10 13:13:55)
	 * 
	 * @return
	 */
	public CTBillQueryConditionDlgNew getQueryConditionDlg() {
		if (m_QueryConditionDlg == null) {
			TemplateInfo info = null;
			if (null == getFrame())
				info = QueryDlgUtil.getTemplateInfo(m_sPkCorp, m_sNodeCode,
						m_sPkUser);

			else
				info = QueryDlgUtil.getTemplateInfo(m_sPkCorp, (getFrame()
						.getModuleCode() != null && !getFrame().getModuleCode()
						.equals(getNodeCode())) ? getFrame().getModuleCode()
						: m_sNodeCode, m_sPkUser);

			m_QueryConditionDlg = new CTBillQueryConditionDlgNew(this, info,
					this.m_sBillType);

			m_QueryConditionDlg.setLogFields(new String[] {
					"ct_manage.operdate.from", "ct_manage.operdate.end",
					"ct_manage.custid", "ct_manage.depid",
					"ct_manage.personnelid", "cus.custname",
					"ct_manage.ctflag", "ct_manage.pk_corp",
					"ct_manage_b.invclid" });

			// 初始化查询模板
			// m_QueryConditionDlg = new CTBillQueryConditionDlg(this);
			// m_QueryConditionDlg.setTempletID(m_sPkCorp, m_sNodeCode,
			// m_sPkUser,
			// null);
			m_QueryConditionDlg
					.setDefaultCloseOperation(SCMQueryConditionDlg.HIDE_ON_CLOSE);
			m_QueryConditionDlg.setICtType(m_iCtType);
			m_QueryConditionDlg.setRefCtTypeQryEditor("ct_manage.pk_ct_type");

			// 5.1增加单据数据权限，所以在查询模版上增加了公司条件，并对其初始化
			// modify by dgq
			ArrayList alCorpIDs = new ArrayList();
			alCorpIDs.add(m_sPkCorp);
			// 默认当前登录公司，并只能选择当前登录公司
			m_QueryConditionDlg.initCorpRef("ct_manage.pk_corp", m_sPkCorp,
					alCorpIDs);

			// 日常合同维护中增加查询条件：合同状态、最新版本号
			// shaobing on May 20, 2005
			m_QueryConditionDlg
					.setCombox(
							"ct_manage.ctflag",
							new String[][] {

									{
											"2",
											nc.ui.ml.NCLangRes
													.getInstance()
													.getStrByID(
															"4020const",
															BillState.STATERESID_VALIDATE) },
									{
											"0",
											nc.ui.ml.NCLangRes
													.getInstance()
													.getStrByID(
															"4020const",
															BillState.STATERESID_FREE) },
									{
											"1",
											nc.ui.ml.NCLangRes
													.getInstance()
													.getStrByID(
															"4020const",
															BillState.STATERESID_AUDIT) },
									{
											"4",
											nc.ui.ml.NCLangRes
													.getInstance()
													.getStrByID(
															"4020const",
															BillState.STATERESID_FREEZE) },
									{
											"5",
											nc.ui.ml.NCLangRes
													.getInstance()
													.getStrByID(
															"4020const",
															BillState.STATERESID_TERMINATE) },
									{
											"6",
											nc.ui.ml.NCLangRes
													.getInstance()
													.getStrByID(
															"4020const",
															BillState.STATERESID_CHECKGOING) },
									{
											"%",
											nc.ui.ml.NCLangRes
													.getInstance()
													.getStrByID("4020nodes",
															"UPP4020nodes-000107")
									/*
									 * @res "全部状态"
									 */} });

			// 修改自定义项目：为自定义项加参照。 ShawBing 2005-06-09
			m_QueryConditionDlg.updateQueryConditionClientUserDef(m_sPkCorp,
					m_sBillType, "ct_manage.def", "ct_manage_b.def");

			// modified by liuzy 2008-01-02 给合同签订日期一个默认值
			// m_QueryConditionDlg.setInitDate("ct_manage.subscribedate",
			// m_UFToday.toString());
			// modified by liuzy 2008-03-26 碧桂园合同查询增加单据日期默认条件
			m_QueryConditionDlg.setInitDate("ct_manage.operdate.from",
					getClientEnvironment().getDate().toString());
			m_QueryConditionDlg.setInitDate("ct_manage.operdate.end",
					getClientEnvironment().getDate().toString());

			m_QueryConditionDlg.setInitDate("ct_manage.ctflag", "2");
			// m_QueryConditionDlg.setInitDate("ct_manage.pk_corp",m_sPkCorp);
			// 查询对话框显示打印次数页签。
			m_QueryConditionDlg.setShowPrintStatusPanel(true);
			m_QueryConditionDlg.hideNormal();
			// 进行数据权限的处理
			m_QueryConditionDlg.setCorpRefs("ct_manage.pk_corp", new String[] {
					"ct_manage_b.invclid", "ct_manage_b.invid",
					"ct_manage.depid"/* 部门 */,
					"ct_manage.personnelid"/* 人员 */,
					"ct_manage.custid"/* 供应商 */, "ct_manage.projectid"/* 项目 */
			});

		}

		return m_QueryConditionDlg;
	}

	/**
	 * 此处插入方法说明:获得表格参照单元格编辑器 创建日期：(2001-9-22 12:56:04)
	 * 
	 * @return
	 */
	private UIRefCellEditor getRefCtTypeQryCellEditor() {
		if (m_RefCtTypeQryCellE == null) {
			m_RefCtTypeQryCellE = new UIRefCellEditor(getRefCtTypeQryPane());
			// 设置为双击后弹出参照按钮
			// m_RefCtTypeCellE.setClickCountToStart(1);
		}
		return m_RefCtTypeQryCellE;
	}

	/**
	 * 此处插入方法说明:获得参照组件 创建日期：(2001-9-21 19:36:00)
	 */
	private UIRefPane getRefCtTypeQryPane() {

		if (m_RefCtTypeQryPane == null) {
			m_RefCtTypeQryPane = new UIRefPane();
			m_RefCtTypeQryPane.setIsCustomDefined(true); // 设置参照为自定义
			// 设置参照Model
			m_RefCtTypeQryPane.setRefModel(new CtTypeRefModel());
			if (true == m_bAddFromBillFlag
					&& (BillType.PURDAILY).equals(m_sBillType)) {
				m_RefCtTypeQryPane.setWhereString("pk_corp = '" + m_sPkCorp
						+ "' and nbusitype=" + m_iCtType + " and ninvctlstyle="
						+ 0);
			} else {
				m_RefCtTypeQryPane.setWhereString("pk_corp = '" + m_sPkCorp
						+ "' and nbusitype=" + m_iCtType);
			}

			m_RefCtTypeQryPane.setRefInputType(0);
			// 设置为返回代码
			m_RefCtTypeQryPane.setReturnCode(true);
		}
		return m_RefCtTypeQryPane;
	}

	/**
	 * 功能说明：得到数量、单价、原币金额、本币金额精度 创建日期：(2001-12-4 15:53:46)
	 */
	protected void getSysInit() {

		// 为提高效率，采用批处理的方式：
		try {
			/*
			 * String[] saParaCode = new String[] { "BD301", "BD303", "BD505",
			 * "BD501", "BD502", "BD503" };
			 */
			// modified by lirr 2009-8-17下午01:24:16 删除"BD303"
			String[] saParaCode = new String[] { "BD301", "BD505", "BD501",
					"BD502", "BD503" };
			Hashtable htPara = nc.ui.pub.para.SysInitBO_Client
					.queryBatchParaValues(m_sPkCorp, saParaCode);
			if (htPara != null) {
				// 本币币种小数位数
				m_sLocalCurrID = (String) htPara.get("BD301");
				if (m_sLocalCurrID != null) {
					// 业务精度
					m_iMainCurrDigit = ((Integer) m_hCurrDigit
							.get(m_sLocalCurrID)).intValue();
					// 财务精度
					m_iMainCurrDigitcw = ((Integer) m_hCurrDigitcw
							.get(m_sLocalCurrID)).intValue();
				}
				// 辅币币种小数位数 modify by liuzy 2007-04-25
				// deleted by lirr 2008-8-20 根据5.5需求删除辅币信息

				/*
				 * if ( !(htPara.get("BD303").toString().trim().equals("null")) &&
				 * htPara.get("BD303") != null &&
				 * !htPara.get("BD303").toString().trim().equals(""))
				 * m_sFracCurrID = (String) htPara.get("BD303");
				 * if(m_sFracCurrID != null && m_sFracCurrID.trim().length() >
				 * 0) m_iFracCurrDigit = ((Integer) m_hCurrDigit
				 * .get(m_sFracCurrID)).intValue();
				 */

				// 单价小数位
				String priceDigit = (String) htPara.get("BD505");
				if (priceDigit != null) {
					m_iPriceDigit = new Integer(priceDigit).intValue();
				}
				// 数量小数位
				String amountDigit = (String) htPara.get("BD501");
				if (amountDigit != null) {
					m_iAmountDigit = new Integer(amountDigit).intValue();
				}
				// 辅计量数量小数位
				String fracAmountDigit = (String) htPara.get("BD502");
				if (fracAmountDigit != null) {
					m_iFracAmountDigit = new Integer(fracAmountDigit)
							.intValue();
				}
				// 换算率精度
				String hslDigit = (String) htPara.get("BD503");
				if (hslDigit != null) {
					m_iHslDigit = new Integer(hslDigit).intValue();
				}
			}
		} catch (Exception e) {
			nc.vo.scm.pub.SCMEnv.out(e);
		}
	}

	/**
	 * 子类实现该方法，返回业务界面的标题。
	 * 
	 * @version (00-6-6 13:33:25)
	 * @return java.lang.String
	 */
	public String getTitle() {
		return getCtBillCardPanel().getTitle();
	}

	/**
	 * 此处插入方法说明:得到界面聚合VO 创建日期：(2001-12-18 16:59:11)
	 * 
	 * @return nc.vo.pub.AggregatedValueObject
	 */
	public nc.vo.pub.AggregatedValueObject getVo() throws java.lang.Exception {
		/*
		 * ManageVO vo = new ManageVO(); vo = getCurVO();
		 */
		ManageVO vo = null;

		// 得到界面VO
		ExtendManageVO exvo = (ExtendManageVO) getCtBillCardPanel()
				.getBillValueVOExtended(
						ExtendManageVO.class.getName(),
						ManageHeaderVO.class.getName(),
						new String[] { ManageItemVO.class.getName(),
								TermBb4VO.class.getName(),
								ExpBb3VO.class.getName(),
								MemoraBb2VO.class.getName(),
								ChangeBb1VO.class.getName(),
								ManageExecVO.class.getName() });
		vo = transVO(exvo);

		int ctState = Integer.parseInt(vo.getParentVO().getAttributeValue(
				"ctflag").toString());
		if (ctState == BillState.FREE || ctState == BillState.CHECKGOING) {
			if (m_sPkUser == null) {
				try {

					m_sPkUser = nc.ui.pub.ClientEnvironment.getInstance()
							.getUser().getPrimaryKey();
				} catch (Exception e) {
					SCMEnv.out("test user id is 2011000001");

				}
			}
			vo.getParentVO().setAttributeValue("audiid", m_sPkUser);
			vo.getParentVO().setAttributeValue("auditdate",
					getClientEnvironment().getDate());
			vo.setOldBillStatus(ctState);
			// 如果是期初合同，那么审批即生效。
			if (m_UFbIfEarly.booleanValue())
				vo.setBillStatus(BillState.VALIDATE);
			else
				vo.setBillStatus(BillState.AUDIT);

			return vo;
		} else {
			showWarningMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"4020pub", "UPP4020pub-000042")/* @res "单据状态不是自由或者审核中！" */);
			return null;
		}
	}

	/**
	 * 此处插入方法说明:检查一下是否存在影响下一步执行的问题 创建日期：(2001-10-28 19:27:46)
	 */
	private boolean haveQuestion() {
		/*
		 * if (!(m_iBillState == OperationState.FREE) || !(m_iTbState ==
		 * OperationState.FREE)) {
		 */
		// modified by lirr 2008-07-23 修改原因：如果是参照添加时 修改转单过来的合同取消时 不检查
		if ((!(m_iBillState == OperationState.FREE) || !(m_iTbState == OperationState.FREE))
				&& !m_bAddFromBillFlag) {
			/* @res "{0}可能已被更改，是否需要保存？" */
			String sMessage = nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"4020pub",
					"UPP4020pub-000193",
					null,
					new String[] { getCtBillCardPanel().getBodyTabbedPane()
							.getTitleAt(m_iTabbedFlag) });

			int ync = MessageDialog.showYesNoCancelDlg(this, null, sMessage);
			if (ync == 4) { // 需要保存
				onSave(); // 保存所做的修改
				if (!m_bErrFlag) {
					;
				} else {
					return false;
				}

			} else if (ync == 8) {
				;
			} else {
				return false;
			}

		}
		return true; // 表示可以执行一下代码
	}

	/**
	 * 此处插入方法说明。 创建日期：(2001-8-22 13:01:54)
	 */
	protected void initCard() {

		setName("ClientUI");
		setLayout(new java.awt.CardLayout());

		miBoCardEdit = nc.ui.scm.pub.BillTools.addCardEditToBodyMenus(
				getCtBillCardPanel(), CTTableCode.BASE);
		UIMenuItem[] bodyMenuItems = getCtBillCardPanel().getBodyMenuItems();
		bodyMenuItems[bodyMenuItems.length - 1]
				.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						onLineCardEdit();

					}
				});
		// 表体 合同基本页签 右键菜单添加“批改”按钮 added by lirr 2008-07-10
		UIMenuItem[] oldUIMenuItem = getCtBillCardPanel().getBodyMenuItems();
		UIMenuItem[] newUIMenuItem = new UIMenuItem[oldUIMenuItem.length + 1];
		for (int i = 0; i < oldUIMenuItem.length; i++) {
			newUIMenuItem[i] = oldUIMenuItem[i];
		}

		miaddNewRowNo.removeActionListener(this);
		miaddNewRowNo.addActionListener(this);

		newUIMenuItem[oldUIMenuItem.length] = miaddNewRowNo; // 表体重排行号
		getCtBillCardPanel().setBodyMenu("table", newUIMenuItem);

		/*
		 * for (int i = 0 ; i < getCtBillCardPanel().getBodyMenuItems().length ;
		 * i++) {
		 * if(!getButtonTree().getButton(getCtBillCardPanel().getBodyMenuItems()[i].getText().toString()).isPower()){
		 * 
		 * newUIMenuItem[i].setEnabled(false); } }
		 */

		// 自由项变色龙
		new InvAttrCellRenderer().setFreeItemRenderer(getCtBillCardPanel());

		add(getCtBillCardPanel(), "Center");
		add(getCtBillListPanel(), "North");
		getCtBillCardPanel().setVisible(true);
		getCtBillListPanel().setVisible(false);

		// 设置单据卡片不可编辑
		getCtBillCardPanel().setEnabled(false);
		CTTool tool = new CTTool();
		tool.setColorCard(getCtBillCardPanel(), null, null, null);
		// added by lirr 2008-08-14 背景色
		// modified by lirr 2008-11-19 所有页签都要符合背景色要求
		String[] tableCodes = CTTableCode.CT_TABCODE;
		for (int i = 0; i < tableCodes.length; i++) {
			if (null != getCtBillCardPanel().getBillTable(tableCodes[i])) {
				getCtBillCardPanel().getBillTable(tableCodes[i])
						.setRowSelectionAllowed(true);
				getCtBillCardPanel().getBillTable(tableCodes[i])
						.setColumnSelectionAllowed(false);
				getCtBillCardPanel()
						.getBillTable(tableCodes[i])
						.setSelectionMode(
								javax.swing.ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
				// 列表 表体加背景色
				getCtBillListPanel().getBodyTable(tableCodes[i])
						.setRowSelectionAllowed(true);
				getCtBillListPanel().getBodyTable(tableCodes[i])
						.setColumnSelectionAllowed(false);
				getCtBillListPanel()
						.getBodyTable(tableCodes[i])
						.setSelectionMode(
								javax.swing.ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
			}
		}
		/*
		 * getCtBillCardPanel().getBillTable().setRowSelectionAllowed(true);
		 * getCtBillCardPanel().getBillTable().setColumnSelectionAllowed(false);
		 * getCtBillCardPanel().getBillTable().setSelectionMode(
		 * javax.swing.ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		 */
		// 表头加背景色
		getCtBillListPanel().getHeadTable().setRowSelectionAllowed(true);
		getCtBillListPanel().getHeadTable().setColumnSelectionAllowed(false);
		getCtBillListPanel().getHeadTable().setSelectionMode(
				javax.swing.ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		/*
		 * // 表体加背景色 getCtBillListPanel().getBodyTable(CTTableCode.BASE)
		 * .setRowSelectionAllowed(true);
		 * getCtBillListPanel().getBodyTable(CTTableCode.BASE)
		 * .setColumnSelectionAllowed(false);
		 * getCtBillListPanel().getBodyTable(CTTableCode.BASE).setSelectionMode(
		 * javax.swing.ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		 */

		getCtBillCardPanel().addActionListener(this);

		setBillCardPaneFillEnable(getCtBillCardPanel());

		// added by lirr 2008-11-11 修改原因：列表下不同的币种的折本汇率精度不同
		scmCurrRateDigit = new ScmCurrLocRateBizDecimalListener(
				getCtBillListPanel().getHeadBillModel(), "currid",
				new String[] { "currrate" }, m_sPkCorp);
		// added by lirr 2009-11-16下午04:31:14 支持编辑公式
		getCtBillCardPanel().setAutoExecHeadEditFormula(true);
	}

	/**
	 * 此处插入方法说明。 创建日期：(2008-8-12 13:01:54) added by lirr 卡片编辑
	 */
	protected void onLineCardEdit() {
		String pk = ((UIRefPane) getCtBillCardPanel().getHeadItem("ct_type")
				.getComponent()).getRefPK();

		if (pk == null || pk.length() <= 0) {
			showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"4020pub", "UPP4020pub-000092")/* @res "合同类型不能为空" */);
			return;
		}
		// getCtBillCardPanel().startRowCardEdit();
		((CtBillCardPanel) getCtBillCardPanel()).setLineCardEdit(true);
		boolean ise = getCtBillCardPanel().getBillModel().getRowEditState();
		try {
			boolean isenableaddrow = false;
			/*
			 * if(m_boAddRow!=null && m_boAddRow.isEnabled()) isenableaddrow =
			 * true; if(m_sBillTypeCode!=null &&
			 * (m_sBillTypeCode.equals(BillTypeConst.m_assembly) ||
			 * m_sBillTypeCode.equals(BillTypeConst.m_disassembly) ||
			 * m_sBillTypeCode.equals(BillTypeConst.m_transform) ||
			 * m_sBillTypeCode.equals(BillTypeConst.m_check) ))
			 */
			isenableaddrow = true;
			getCtBillCardPanel().getBillModel()
					.setRowEditState(!isenableaddrow);
			UIRefPane refInv = (UIRefPane) getCtBillCardPanel().getBodyItem(
					"invcode").getComponent();
			refInv.setAutoCheck(false);// getRefModel().setSelectedData(null);
			getCtBillCardPanel().startRowCardEdit();
		} finally {
			getCtBillCardPanel().getBillModel().setRowEditState(ise);
			((CtBillCardPanel) getCtBillCardPanel()).setLineCardEdit(false);
		}

	}

	/**
	 * 此处插入方法说明。 创建日期：(2008-8-12 13:01:54) added by lirr 重排行号
	 */
	protected void onAddNewRowNo() {
		nc.ui.scm.pub.report.BillRowNo.addNewRowNo(getCtBillCardPanel(),
				m_sBillType, getCtBillCardPanel().getRowNumKey());
	}

	/**
	 * 方法功能描述：切换列表/卡片按钮
	 * <p>
	 * <b>examples:</b>
	 * <p>
	 * 使用示例
	 * <p>
	 * <b>参数说明</b>
	 * <p>
	 * 
	 * @author liuzy
	 * @time 2007-5-17 下午04:52:09
	 */
	public void showBtnSwitch() {
		if (m_bIsCard) {
			getButtonTree().getButton(CTButtonConst.BTN_SWITCH).setName(
					nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
							"UCH022")/*
										 * @res "列表显示"
										 */);
			getButtonTree().getButton(CTButtonConst.BTN_SWITCH).setHint(
					nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
							"UCH022")/*
										 * @res "列表显示"
										 */);
		} else {
			getButtonTree().getButton(CTButtonConst.BTN_SWITCH).setName(
					nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
							"UCH021")/*
										 * @res "卡片显示"
										 */);
			getButtonTree().getButton(CTButtonConst.BTN_SWITCH).setHint(
					nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
							"UCH021")/*
										 * @res "卡片显示"
										 */);
		}
		updateButton(getButtonTree().getButton(CTButtonConst.BTN_SWITCH));

	}

	/**
	 * 此处插入方法说明。 创建日期：(2001-9-7 12:44:23)
	 */
	protected void initListener() {
		getCtBillCardPanel().addEditListener(this);
		getCtBillListPanel().addEditListener(this);

		// 增加单据列表双击表头排序监听
		getCtBillListPanel().getHeadBillModel().addSortListener(this);
		getCtBillListPanel().getHeadBillModel().addSortRelaObjectListener(this);

		// 增加单据列表的表头的选择侦听
		getCtBillListPanel().getHeadTable().getSelectionModel()
				.addListSelectionListener(this);

		getCtBillCardPanel().getBodyTabbedPane().addChangeListener(this);
		getCtBillListPanel().getBodyTabbedPane().addChangeListener(this);
		getCtBillCardPanel().addBodyEditListener2(this);
		getCtBillCardPanel().addAfterEditListener(this);
		getCtBillCardPanel().addTableColumnModelListener(this);
		getCtBillCardPanel().addListSelectionListener(this);

		getCtBillListPanel().getHeadTable().addMouseListener(this);
		getCtBillCardPanel().addBodyMenuListener(this);

		// 初始化编辑前控制器
		getCtBillCardPanel().getBillModel().setCellEditableController(this);

		// 排序前监听
		getCtBillCardPanel().getBillModel(CTTableCode.BASE)
				.setSortPrepareListener(this);
		getCtBillListPanel().getBodyBillModel(CTTableCode.BASE)
				.setSortPrepareListener(this);

		// 增加表体排序监听
		// added by qinchao 2009-04-21 深圳南玻出现数据串行问题 排序后缓存未更新
		getCtBillListPanel().getBodyBillModel(CTTableCode.BASE)
				.addSortListener(this);
		getCtBillCardPanel().getBillModel(CTTableCode.BASE).addSortListener(
				this);
	}

	/**
	 * 初始化所有的Table 创建日期：(2001-9-3 9:46:05)
	 */
	protected void initTables() {
	}

	/**
	 * 此处插入方法说明。 创建日期：(2001-10-24 8:33:41)
	 */
	protected void initVariable() {

		m_bIsNeedReInit = new boolean[6];
		// 标志是否需要重新初始化Table数据
		for (int i = 1; i < 6; i++)
			m_bIsNeedReInit[i] = true; // "ture"为需要

		m_vBillVO = new Vector<ExtendManageVO>(); // 用于单据的临时保存
		// added by lirr 2008-7-23
		m_vBillVOForRef = new Vector<ExtendManageVO>(); // 用于转单单据的临时保存
		m_vTableVO = new Vector(); // 用于Table的临时保存

	}

	/**
	 * Invoked when an item has been selected or deselected. The code written
	 * for this method performs the operations that need to occur when an item
	 * is selected (or deselected).
	 */
	public void itemStateChanged(java.awt.event.ItemEvent e) {
	}

	/**
	 * 此处插入方法说明。 功能： 参数： 返回： 例外： 日期：(2002-9-16 16:39:54) 修改日期，修改人，修改原因，注释标志：
	 */
	protected final void loadData() {

		// added by lirr 2009-02-17
		switch (m_iTabbedFlag) {

		case TabState.BILL: // 合同基本页签
			getCtBillCardPanel().getBillModel("table").setBodyDataVO(
					getCurVO().getChildrenVO());
			break;

		case TabState.TERM: // 条款页签
			getCtBillCardPanel().getBillModel("term").setBodyDataVO(
					getCurVO().getTermBb4s());
			break;

		case TabState.EXP: // 费用页签
			getCtBillCardPanel().getBillModel("cost").setBodyDataVO(
					getCurVO().getExpBb3s());
			break;

		case TabState.NOTE: // 合同大事记页签
			getCtBillCardPanel().getBillModel("note").setBodyDataVO(
					getCurVO().getMemoraBb2s());
			break;
		case TabState.CHANGE: // 
			getCtBillCardPanel().getBillModel("history").setBodyDataVO(
					getCurVO().getChangeBb1s());
			break;

		default:
			getCtBillCardPanel().getBillModel("exec").setBodyDataVO(
					getCurVO().getManageExecs());
			break;
		}

	}

	/**
	 * 此处插入方法说明。 创建日期：(2001-8-24 13:43:20)
	 */
	protected void onAdd() {
		try {

			if (!m_bIsCard) { // 如果是在单据列表模式下，则切换到卡片模式下
				onList();
				m_bIsCard = true;
			}
			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"4020pub", "UPP4020pub-000044")/*
													 * @res
													 * "添加一张新的合同，正确录入合同后点击“保存”按钮进行保存。"
													 */);
			// added by lirr 2008-09-23
			// 设置合同的参照类型，原来在onedit()时调用setCtRef()，加上参照其他单据后进行了控制，
			// 转单完毕后无法重新加载，故放在此处

			((UIRefPane) getCtBillCardPanel().getHeadItem("ct_type")
					.getComponent()).setWhereString("pk_corp = '" + m_sPkCorp
					+ "' and nbusitype=" + m_iCtType);

			m_iBillState = OperationState.ADD;
			// 设置为可编辑
			getCtBillCardPanel().setEnabled(true);
			getCtBillCardPanel().addNew();

			// 合计行标记
			getCtBillCardPanel().getTotalTableModel().setValueAt(
					nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
							"UC000-0001146")/* @res "合计" */, 0, 0);
			// 单据复制

			// 改变单据，Table需要重新载入数据
			for (int i = 1; i < 6; i++)
				m_bIsNeedReInit[i] = true;

			getCtBillCardPanel().setHeadItem("ctflag",
					new Integer(BillState.FREE));
			// 置当前公司
			getCtBillCardPanel().setHeadItem("pk_corp", m_sPkCorp);
			getCtBillCardPanel().setHeadItem("unitname", m_sPkCorp);

			// 获取当前用户ID对应的业务员
			PsndocVO psnvo = getPsndocByUserid();
			if (psnvo != null) {// 如果对应有业务员
				if (getCtBillCardPanel().getHeadItem("personnelid").getValue()
						.toString().trim().equals("")) {
					// 置当前用户ID对应的业务员
					getCtBillCardPanel().setHeadItem("personnelid",
							psnvo.getPk_psndoc());
					getCtBillCardPanel().setHeadItem("pername",
							psnvo.getPk_psndoc());
				}
				if (getCtBillCardPanel().getHeadItem("depid").getValue()
						.toString().trim().equals("")) {
					// 置当前用户ID对应的业务员对应的部门
					getCtBillCardPanel().setHeadItem("depid",
							psnvo.getPk_deptdoc());
					getCtBillCardPanel().setHeadItem("depname",
							psnvo.getPk_deptdoc());
				}

			}

			getCtBillCardPanel().setTailItem("opername", m_sPkUser);
			getCtBillCardPanel().setTailItem("operid", m_sPkUser);
			getCtBillCardPanel().setTailItem("operdate", m_UFToday);
			getCtBillCardPanel().getTailItem("audiname").clearViewData();
			getCtBillCardPanel().getTailItem("auditdate").clearViewData();

			// set打印次数为０。
			getCtBillCardPanel().setTailItem("iprintcount", new Integer(0));

			// 合同签订日期置入默认值
			getCtBillCardPanel().setHeadItem("subscribedate", m_UFToday);
			// 币种默认为本位币
			((UIRefPane) getCtBillCardPanel().getHeadItem("currname")
					.getComponent()).setPK(m_sLocalCurrID);
			afterCurrNameEdit(m_bAddFromBillFlag);
			getCtBillCardPanel().getHeadItem("currrate").setEdit(false);

			getCtBillCardPanel().setEnabled(true);
			// 执行公式
			getCtBillCardPanel().getBillModel().execLoadFormula();

			rightButtonRightControl();
			// 新增时表体有一行：
			onAddLine();
			setButtonState();
		} catch (Exception e) {
			nc.vo.scm.pub.SCMEnv.out(e);
		}
	}

	/**
	 * 此处插入方法说明。 创建日期：(2001-8-28 16:28:12)
	 */
	protected void onAddLine() {

		if (m_iTabbedFlag != TabState.BILL) {
			getCtBillCardPanel().setEnabled(true);
			BillItem[] headitem = getCtBillCardPanel().getHeadItems();
			for (int i = 0; i < headitem.length; i++)
				headitem[i].setEnabled(false);
			m_iTbState = OperationState.ADD;
		}
		getCtBillCardPanel().addLine();
		int iRow = getCtBillCardPanel().getBodyPanel().getTable()
				.getSelectedRow();
		String pk = ((UIRefPane) getCtBillCardPanel().getHeadItem("transpmode")
				.getComponent()).getRefPK();
		getCtBillCardPanel().getBillModel(CTTableCode.BASE).setValueAt(pk,
				iRow, "ctranspmodeid");
		String[] strTableEditFormulas = { "transpnametable->getColValue(bd_sendtype,sendname,pk_sendtype,ctranspmodeid)" };

		getCtBillCardPanel().getBillModel().execFormula(iRow,
				strTableEditFormulas);
		// setButtonState();
		if (!m_bAddFromBillFlag) {
			setButtonState();
		} else {
			setButtonStateForCof();
		}
	}

	/**
	 * 此处插入方法说明。 创建日期：(2001-9-1 17:13:11)
	 */
	protected void onCancel() {
		int iSelectResult = MessageDialog
				.showYesNoDlg(this, null, nc.ui.ml.NCLangRes.getInstance()
						.getStrByID("common", "UCH067")/* @res "是否确定要取消？" */);
		if (iSelectResult != MessageDialog.ID_YES)
			return;

		getCtBillCardPanel().setEnabled(false);
		// added by lirr 2008-07-23 判断是否是对转单过来要生成的合同 取消
		if (m_bAddFromBillFlag) {
			m_vBillVOForRef.remove(m_iId - 1);
			// onList();

			if (m_bIsCard) { // 卡片
				// 切换到列表模式下
				onList();

			} else { // 列表
				setListDataForRef();
				getCtBillListPanel().setVisible(true);
				getCtBillCardPanel().setVisible(false);
			}
			// setButtonStateForCof();
			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"common", "UCH008")/* @res "取消成功" */);
			return;
		}

		else {
			if (m_vBillVO != null && m_vBillVO.size() > 0) {
				// added by lirr 2009-7-3上午09:24:54 取消时卡片界面需要重新设置折本汇率精度

				getCtBillCardPanel().getHeadItem("currrate").setDecimalDigits(
						getCurrateDigit(((ManageHeaderVO) getCurVO()
								.getParentVO()).getCurrid()));

				getCtBillCardPanel().superresumeValue();// 从备份数据中恢复数据到界面
			} else {
				ManageVO vo = new ManageVO();
				ManageHeaderVO voHead = new ManageHeaderVO();
				ManageItemVO[] voaItem = new ManageItemVO[1];
				voaItem[0] = new ManageItemVO();
				vo.setParentVO(voHead);
				vo.setChildrenVO(voaItem);
				getCtBillCardPanel().setBillValueVO(vo);
				getCtBillCardPanel().getBillModel().clearBodyData();
			}

			switch (m_iTabbedFlag) {

			case TabState.BILL: // 合同基本页签
				m_iBillState = OperationState.FREE;
				getCtBillCardPanel().stopEditing();
				getCtBillCardPanel().setEnabled(false);
				getCtBillCardPanel().superupdateValue();// 根据界面的值更新备份的数据
				break;

			case TabState.TERM: // 条款页签
				m_iTbState = OperationState.FREE;
				getCtBillCardPanel().getBillModel("term").resumeValue();
				getCtBillCardPanel().getBillModel("term").setEnabled(false);
				getCtBillCardPanel().getBillModel("term").updateValue();
				break;

			case TabState.EXP: // 费用页签
				m_iTbState = OperationState.FREE;
				getCtBillCardPanel().getBillModel("cost").resumeValue();
				getCtBillCardPanel().getBillModel("cost").setEnabled(false);
				getCtBillCardPanel().getBillModel("cost").updateValue();
				break;

			case TabState.NOTE: // 合同大事记页签
				m_iTbState = OperationState.FREE;
				getCtBillCardPanel().getBillModel("note").resumeValue();
				getCtBillCardPanel().getBillModel("note").setEnabled(false);
				getCtBillCardPanel().getBillModel("note").updateValue();
				break;

			default:
				m_iTbState = OperationState.FREE;
				getCtBillCardPanel().getBillModel("history").resumeValue();
				getCtBillCardPanel().getBillModel("history").setEnabled(false);
				getCtBillCardPanel().getBillModel("history").updateValue();
			}
		}

		// 取消后调用自动排序 ，qinchao 2009-04-08
		if (getCtBillCardPanel().getBillModel().getSortColumn() > 0) {
			getCtBillCardPanel().getBillModel().sortByColumn(
					getCtBillCardPanel().getBillModel().getSortColumn(),
					getCtBillCardPanel().getBillModel().isSortAscending());
		}

		// setButtonState();
		if (!m_bAddFromBillFlag) {
			setButtonState();
		} else {
			setButtonStateForCof();
		}
		// added by lirr 2009-12-2上午09:56:12 取消动作后清除getDmdo().setCellControls 否则先做少行数据再做多行数据 数组越界
		getCtBillCardPanel().getDmdo().setCellControls(null);
		
		showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
				"UCH008")/* @res "取消成功" */);
	}

	/**
	 * 功能说明：变更合同。 1：合同审批后可以进行变更操作。 2：变更时纪录变更人和变更日期。 3：合同新增两个字段，版本号和是否激活。
	 * 变更后新生成的合同版本号自动加1 作者：程起伍 创建日期：(2003-09-02 09:22:55)
	 */
	protected void onChange() {
		// 如果是在单据列表模式下，则切换到卡片模式下
		if (!m_bIsCard)
			onList();
		// 设置为变更状态
		m_iBillState = OperationState.CHANGE;

		showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4020pub",
				"UPP4020pub-000045")/* @res "变更当前合同，然后可以点击“保存”按钮进行保存" */);

		// 设置为可编辑
		getCtBillCardPanel().setEnabled(true);
		// 合同编码，类型，项目号，合同签订日日期，客商，币种，汇率，表体存货分类，存货不可编辑。
		getCtBillCardPanel().getHeadItem("ct_code").setEnabled(false);
		getCtBillCardPanel().getHeadItem("ct_type").setEnabled(false);
		getCtBillCardPanel().getHeadItem("projectname").setEnabled(false);
		getCtBillCardPanel().getHeadItem("custname").setEnabled(false);
		getCtBillCardPanel().getHeadItem("currname").setEnabled(false);
		getCtBillCardPanel().getHeadItem("currrate").setEnabled(false);
		getCtBillCardPanel().getHeadItem("subscribedate").setEnabled(false);
		// getCtBillCardPanel().getHeadItem("astcurrate").setEnabled(false);

		/**
		 * 如果以下字段的值 订单累计执行数量，订单累计执行金额，期初出入库数量，期初出入库金额 期初开发票数量，期初开发票金额，期初回付款
		 * 符合不为空或不为零条件，那么，就不能编辑存货和存货分类。
		 */
		Object oRefValue = ((UIRefPane) getCtBillCardPanel().getHeadItem(
				"ct_type").getComponent()).getRefValue("ninvctlstyle");
		// 根据合同类型的存货控制方式设置存货编码和存货分类编码是否可编辑
		int iInvctlstyle = Integer.parseInt(oRefValue.toString());

		UFDouble ufd = null; // 订单累计执行数量
		Object obj = null;
		UFDouble ufd0 = new UFDouble(0);
		int icount = getCtBillCardPanel().getRowCount();
		String[] saCheckName = new String[] { "ordsum", "ordnum" };
		String[] saEarlyCheck = new String[] { "inoutnum", "inoutsum",
				"invoicnum", "invoicesum", "paysum" };
		int iCheckNameLen = saCheckName.length;
		int iEarlyCheckLen = saEarlyCheck.length;

		for (int i = 0; i < icount; i++) {
			boolean iscanupdate = true;
			// 日常和期初都要检查
			for (int j = 0; j < iCheckNameLen; j++) {
				obj = getCtBillCardPanel().getBodyValueAt(i, saCheckName[j]);
				if (obj != null) {
					ufd = new UFDouble(obj.toString());
					if (ufd.compareTo(ufd0) > 0) {
						iscanupdate = false;
						break;
					}
				}
			}
			// 检查期初的
			if (iscanupdate && m_UFbIfEarly.booleanValue()) {
				for (int j = 0; j < iEarlyCheckLen; j++) {
					obj = getCtBillCardPanel().getBodyValueAt(i,
							saEarlyCheck[j]);
					if (obj != null) {
						ufd = new UFDouble(obj.toString());
						if (ufd.compareTo(ufd0) > 0) {
							iscanupdate = false;
							break;
						}
					}
				}
			}

			if (!iscanupdate) {
				getCtBillCardPanel().setCellEditable(i, "invclasscode", false);
				getCtBillCardPanel().setCellEditable(i, "invcode", false);
			} else { // 如果合同类型的存货控制方式为存货
				if (iInvctlstyle == 0) {
					getCtBillCardPanel().setCellEditable(i, "invclasscode",
							false);
					getCtBillCardPanel().setCellEditable(i, "invcode", true);
				} else if (iInvctlstyle == 1) {
					// 如果合同类型的存货分类控制方式的合同
					getCtBillCardPanel().setCellEditable(i, "invclasscode",
							true);
					getCtBillCardPanel().setCellEditable(i, "invcode", false);
				} else if (iInvctlstyle == 2) {
					// 如果合同类型为存货和存货类型为空
					getCtBillCardPanel().setCellEditable(i, "invclasscode",
							false);
					getCtBillCardPanel().setCellEditable(i, "invcode", false);
				}

			}
			/*
			 * 订单累计执行数量，订单执行累计价税合计不可变更 期初出入库数量，出入库金额，开发票数量，开发票金额，回付款金额不可以编辑。
			 */
			for (int k = 0; k < iCheckNameLen + iEarlyCheckLen; k++) {
				if (k < iCheckNameLen) {
					getCtBillCardPanel().setCellEditable(i, saCheckName[k],
							false);
				} else {
					if (m_UFbIfEarly.booleanValue())
						getCtBillCardPanel().setCellEditable(i,
								saEarlyCheck[k - iCheckNameLen], false);
					else
						break;
				}
			}
		} // 置入主辅币计算公式
		String pk = getCtBillCardPanel().getHeadItem("currid").getValue();
		// modified by lirr 2008-11-22 修改原因：表体计算采用新方式
		/*
		 * if (pk != null) setFormularByCur(pk);
		 */
		// setButtonState();
		if (!m_bAddFromBillFlag) {
			setButtonState();
		} else {
			setButtonStateForCof();
		}
	}

	/**
	 * 此处插入方法说明。 功能： 参数： 返回： 例外： 日期：(2002-5-11 16:44:36) 修改日期，修改人，修改原因，注释标志：
	 */
	protected void onCopy() {

		try {
			if (!m_bIsCard) { // 如果是在单据列表模式下，则切换到卡片模式下
				onList();
				m_bIsCard = true;
			}
			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"4020pub", "UPP4020pub-000046")/*
													 * @res
													 * "复制一张合同，修改后点击“保存”按钮进行保存。"
													 */);

			m_iBillState = OperationState.ADD;
			m_timer.start("执行复制程序"); /*-=notranslate=-*/

			getCtBillCardPanel().addNew();
			// 表体自动增行
			getCtBillCardPanel().setAutoAddEditLine(true);
			// 表体鼠标右键增删行
			getCtBillCardPanel().setBodyMenuShow(true);

			m_timer.showExecuteTime("getCtBillCardPanel().addNew()");
			// 单据复制

			if (m_iId != 0) {
				ManageVO mVO = getCurVO();
				ManageVO voTemp = mVO.clone(mVO);

				voTemp.getParentVO().setPrimaryKey(null);
				voTemp.getParentVO().setAttributeValue("actualvalidate", null);
				voTemp.getParentVO()
						.setAttributeValue("actualinvalidate", null);
				voTemp.getParentVO().setAttributeValue("ts", null);
				voTemp.getParentVO().setAttributeValue("nprepaymny", null);
				voTemp.setChangeBb1s(null); // 合同变更
				voTemp.setExpBb3s(null);
				voTemp.setManageExecs(null);
				voTemp.setMemoraBb2s(null);

				// 将合同条款放到一个缓存中，以便合同条款的同时复制。
				if (voTemp.getTermBb4s() != null
						&& voTemp.getTermBb4s().length > 0){
					    m_copyedTermVOs = voTemp.getTermBb4s();
					 // added by lirr 2009-12-2上午10:13:44 复制有条款的合同时合同条款的TS应清除
              clearTermsTs();
              //end
				}
				// 清表体的回写数据
				for (int i = 0; i < voTemp.getChildrenVO().length; i++) {
					voTemp.getChildrenVO()[i].setAttributeValue(
							"pk_ct_manage_b", null);
					voTemp.getChildrenVO()[i].setAttributeValue("pk_ct_manage",
							null);
					voTemp.getChildrenVO()[i].setAttributeValue("ordprice",
							null);
					voTemp.getChildrenVO()[i].setAttributeValue("ordnum", null);
					voTemp.getChildrenVO()[i].setAttributeValue("ordsum", null);
					voTemp.getChildrenVO()[i].setAttributeValue("ts", null);
					voTemp.getChildrenVO()[i].setAttributeValue("crowno", null);
					// modified by lirr 2008-09-22 清累计付款金额 累计应付款金额
					if (null != voTemp.getChildrenVO()[i]
							.getAttributeValue("ntotalgpmny")) {
						voTemp.getChildrenVO()[i].setAttributeValue(
								"ntotalgpmny", null);
					}
					if (null != voTemp.getChildrenVO()[i]
							.getAttributeValue("ntotalshgpmny")) {
						voTemp.getChildrenVO()[i].setAttributeValue(
								"ntotalshgpmny", null);
					}

					// added by lirr 2009-7-27 下午03:53:23
					if (null != voTemp.getChildrenVO()[i]
							.getAttributeValue("noritotalgpmny")) {
						voTemp.getChildrenVO()[i].setAttributeValue(
								"noritotalgpmny", null);
					}
					if (null != voTemp.getChildrenVO()[i]
							.getAttributeValue("noritotalshgpmny")) {
						voTemp.getChildrenVO()[i].setAttributeValue(
								"noritotalshgpmny", null);
					}
// added by lirr 2009-11-30下午01:16:49谢阳要求 复制整单时来源信息要清掉 复制行时56开始就清掉了
					clearSouceInfo((ManageItemVO)voTemp.getChildrenVO()[i]);
				}
				m_timer.showExecuteTime("清表体的回写数据"); /*-=notranslate=-*/

				getCtBillCardPanel().setBillValueVO(voTemp);
				m_timer
						.showExecuteTime("getCtBillCardPanel().setBillValueVO(mVO)");
				getCtBillCardPanel().resetAllRowNo();
				m_timer.showExecuteTime("getCtBillCardPanel().resetAllRowNo()");
				// 把Id置入界面
				setIdtoName();
				m_timer.showExecuteTime("setIdtoName()");
				// 改变单据，Table需要重新载入数据
				for (int i = 1; i < 6; i++)
					m_bIsNeedReInit[i] = true;

				getCtBillCardPanel().setHeadItem("ctflag",
						new Integer(BillState.FREE));
				getCtBillCardPanel().getHeadItem("ct_code").clearViewData();
				getCtBillCardPanel().getHeadItem("ct_name").clearViewData();
				// 合同版本号初始化为1.0
				getCtBillCardPanel().setHeadItem("version", "1.0");
				// 清空预付款
				if (getCtBillCardPanel().getHeadItem("nprepaymny") != null)
					getCtBillCardPanel().getHeadItem("nprepaymny")
							.clearViewData();
				// modified by lirr 2008-09-22 清累计付款总额 应收付金额
				if (null != getCtBillCardPanel().getHeadItem("ntotalgpamount")) {
					getCtBillCardPanel().getHeadItem("ntotalgpamount")
							.clearViewData();
				}
				if (null != getCtBillCardPanel()
						.getHeadItem("ntotalgpshamount")) {
					getCtBillCardPanel().getHeadItem("ntotalgpshamount")
							.clearViewData();
				}
				// added by lirr 2009-7-27 下午03:52:40 清累计付款总额 应收付金额 原币
				if (null != getCtBillCardPanel().getHeadItem(
						"noritotalgpamount")) {
					getCtBillCardPanel().getHeadItem("noritotalgpamount")
							.clearViewData();
				}
				if (null != getCtBillCardPanel().getHeadItem("norigpshamount")) {
					getCtBillCardPanel().getHeadItem("norigpshamount")
							.clearViewData();
				}
				// added by lirr 2009-8-11下午03:09:15 清原币预付款

				if (null != getCtBillCardPanel().getHeadItem("noriprepaymny")) {
					getCtBillCardPanel().getHeadItem("noriprepaymny")
							.clearViewData();
				}

				getCtBillCardPanel().setTailItem("opername", m_sPkUser);
				getCtBillCardPanel().setTailItem("operdate", m_UFToday);
				getCtBillCardPanel().getTailItem("tmaketime").clearViewData();

				getCtBillCardPanel().getTailItem("audiname").clearViewData();
				getCtBillCardPanel().getTailItem("audiid").clearViewData();
				getCtBillCardPanel().getTailItem("auditdate").clearViewData();
				getCtBillCardPanel().getTailItem("taudittime").clearViewData();

				// 置打印次数为0。
				getCtBillCardPanel().setTailItem("iprintcount", new Integer(0));

				// 最后修改人
				getCtBillCardPanel().getTailItem("clastoperatorid")
						.clearViewData();
				getCtBillCardPanel().getTailItem("vlastoperatorname")
						.clearViewData();
				// 最后修改时间
				getCtBillCardPanel().getTailItem("tlastmaketime")
						.clearViewData();

				getCtBillCardPanel().setEnabled(true);
				m_timer.showExecuteTime("清空表头审核人等数据"); /*-=notranslate=-*/

				// 置入主辅币计算公式
				// modified by lirr 2008-11-22 修改原因：表体计算采用新方式
				/*
				 * String pk = getCtBillCardPanel().getHeadItem("currid")
				 * .getValue(); if (pk != null) setFormularByCur(pk);
				 */

				m_timer.showExecuteTime("afterCurrNameEdit()");
				// 执行公式
				getCtBillCardPanel().getBillModel().execLoadFormula();

				m_timer.showExecuteTime("执行公式"); /*-=notranslate=-*/

				// added by lirr 2008-11-11 增加原因：复制生成外币其它合同折本汇率不允许修改
				if (null != getCtBillCardPanel().getHeadItem("currname")
						.getComponent()) {
					String currID = ((UIRefPane) getCtBillCardPanel()
							.getHeadItem("currname").getComponent()).getRefPK();
					if (!StringUtil.isEmptyWithTrim(currID)) {
						/*
						 * m_bRateEnable = PubSetUI.getBothExchRateEditable(
						 * m_sPkCorp, );
						 */
						setM_bRateEnable(currID);
						getCtBillCardPanel().getHeadItem("currrate")
								.setEnabled(isM_bRateEnable());
					}
				}

			}
			// 合计行标记
			getCtBillCardPanel().getTotalTableModel().setValueAt(
					nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
							"UC000-0001146")/* @res "合计" */, 0, 0);

		} catch (Exception e) {
			nc.vo.scm.pub.SCMEnv.out(e);
		}
		// setButtonState();
		if (!m_bAddFromBillFlag) {
			setButtonState();
		} else {
			setButtonStateForCof();
		}
	}

	/**
	 * 此处插入方法说明。 创建日期：(2001-8-29 10:02:05)
	 */
	protected void onDel() {
		// 操作员日志
		nc.vo.sm.log.OperatelogVO log = getNormalOperateLog();

		ManageVO tempMVO = getCurVO();
		int ctState = Integer.parseInt(tempMVO.getParentVO().getAttributeValue(
				"ctflag").toString());
		String sMessage = null;

		// 对处于不同状态的合同进行不同的处理
		if (ctState == BillState.AUDIT) { // 审核状态
			sMessage = nc.ui.ml.NCLangRes.getInstance().getStrByID("4020pub",
					"UPP4020pub-000051")/* @res "不能删除，该合同还在审核中!" */;
			MessageDialog.showWarningDlg(this, null, sMessage);

			return;
		}

		if (ctState == BillState.FREEZE) { // 冻结状态
			sMessage = nc.ui.ml.NCLangRes.getInstance().getStrByID("4020pub",
					"UPP4020pub-000052")/* @res "不能删除，该合同已被冻结！" */;
			MessageDialog.showWarningDlg(this, null, sMessage);

			return;
		}

		if (ctState == BillState.VALIDATE) { // 生效状态
			sMessage = nc.ui.ml.NCLangRes.getInstance().getStrByID("4020pub",
					"UPP4020pub-000053")/* @res "不能删除，该合同已生效！" */;
			MessageDialog.showWarningDlg(this, null, sMessage);

			return;
		}

		if (ctState == BillState.TERMINATE) { // 终止状态
			sMessage = nc.ui.ml.NCLangRes.getInstance().getStrByID("4020pub",
					"UPP4020pub-000054")/* @res "不能删除，该合同处于终止状态！" */;
			MessageDialog.showWarningDlg(this, null, sMessage);

			return;
		}

		// 处在自由状态、废止状态的合同可以删除

		sMessage = nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
				"UCH002")/*
							 * @res "是否确认要删除？"
							 */;

		if (MessageDialog.showYesNoDlg(this, null, sMessage,
				MessageDialog.ID_NO) != UIDialog.ID_YES)
			return;

		try {

			sMessage = nc.ui.ml.NCLangRes.getInstance().getStrByID("4020pub",
					"UPP4020pub-000056")/* @res "开始删除当前合同……" */;
			showHintMessage(sMessage);

			String sBillOwnerID = null; // 要删除单据的拥有者（针对参数：是否允许删除他人制单的单据）
			if (m_bIsCard) // 卡片下取表尾的operid
				sBillOwnerID = getCtBillCardPanel().getTailItem("operid")
						.getValue();
			else
				// 列表下取相应行的operid
				sBillOwnerID = (String) getCtBillListPanel().getHeadBillModel()
						.getValueAt(m_iId - 1, "operid");

			tempMVO.getParentVO().setAttributeValue("operid", sBillOwnerID);
			tempMVO.getParentVO()
					.setAttributeValue("coperatoridnow", m_sPkUser);
			// added by lirr 2009-04-21业务日志
			tempMVO.setOperatelogVO(log);

			nc.ui.pub.pf.PfUtilClient.processAction("DELETE", m_sBillType,
					getClientEnvironment().getDate().toString(), tempMVO,
					tempMVO);

			getCtBillListPanel().getHeadBillModel().removeRow(m_iId - 1);

			m_vBillVO.remove(m_iId - 1);
			// 改变单据，Table需要重新载入数据
			for (int i = 1; i < 6; i++)
				m_bIsNeedReInit[i] = true;
			m_iElementsNum--;

			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"4020pub", "UPP4020pub-000057")/* @res "合同删除成功！" */);

			if (m_iElementsNum != 0) {
				if (m_iId > m_iElementsNum)
					m_iId--;

				if (m_bIsCard) {
					// ManageVO mVO = getCurVO();
					// if(null == mVO.getChildrenVO() ||
					// mVO.getChildrenVO().length == 0){
					loadBodyData();
					// }
					ManageVO mVO = getCurVO();
					getCtBillCardPanel().setBillValueVO(mVO);
					// 执行公式
					getCtBillCardPanel().getBillModel().execLoadFormula();
					// 把id号给单据卡片的表头元素
					setIdtoName();
					getCtBillCardPanel().setEnabled(false);
					getCtBillCardPanel().updateValue();

				} else {
					setListRateDigit();
					setHeaderListData();
					getCtBillListPanel().getHeadTable()
							.setCellSelectionEnabled(false);
					getCtBillListPanel().getHeadTable().setRowSelectionAllowed(
							true);

					getCtBillListPanel().getHeadTable()
							.setRowSelectionInterval(m_iId - 1, m_iId - 1);
				}
			} else {
				m_iId--;

				if (m_bIsCard) {
					getCtBillCardPanel().getBillData().clearViewData();
				} else {
					getCtBillListPanel().setHeaderValueVO(null);
					getCtBillListPanel().setBodyValueVO(null);
				}
			}

		} catch (Exception e) {
			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"4020pub", "UPP4020pub-000058")/* @res "合同删除失败！" */);
			MessageDialog.showHintDlg(this, null, nc.ui.ml.NCLangRes
					.getInstance().getStrByID("4020pub", "UPP4020pub-000201")/*
																				 * @res
																				 * "合同删除失败！请刷新后再做尝试！"
																				 */);
		}
		// setButtonState();
		if (!m_bAddFromBillFlag) {
			setButtonState();
		} else {
			setButtonStateForCof();
		}
	}

	/**
	 * 此处插入方法说明。 创建日期：(2001-8-28 16:29:04)
	 */
	protected void onDelLine() {
		/**
		 * 如果以下字段的值 订单累计执行数量，订单累计执行金额，期初出入库数量，期初出入库金额 期初开发票数量，期初开发票金额，期初回付款
		 * 符合不为空或不为零条件，那么，就不能删行。
		 */
		if (m_iBillState == OperationState.CHANGE) {
			int row[] = getCtBillCardPanel().getBodyPanel().getTable()
					.getSelectedRows();
			UFDouble ufd = null;
			Object obj = null;
			StringBuffer err = new StringBuffer();
			UFDouble ufd0 = new UFDouble(0);
			boolean iserr = false;
			String crowno = null;
			Hashtable htErr = new Hashtable();
			ArrayList alErr = new ArrayList();
			// modified by lirr 2008-12-15 如果合同行已经付款则不允许删除该行
			String[] saCheckName = new String[] { "ordsum", "ordnum",
					"ntotalgpmny" };
			String[] saEarlyCheck = new String[] { "inoutnum", "inoutsum",
					"invoicnum", "invoicesum", "paysum" };
			int iCheckNameLen = saCheckName.length;
			int iEarlyCheckLen = saEarlyCheck.length;
			int iRowLen = row.length;

			if (iRowLen > 0) {
				for (int i = 0; i < iRowLen; i++) {
					crowno = (String) getCtBillCardPanel().getBodyValueAt(
							row[i], "crowno");
					// 日常和期初都要检查
					for (int j = 0; j < iCheckNameLen; j++) {
						obj = getCtBillCardPanel().getBodyValueAt(row[i],
								saCheckName[j]);
						// added by lirr 2008-12-15 如果合同行已经付款则不允许删除该行
						if ("ntotalgpmny".equals(saCheckName[j])) {
							if (obj == null) {
								obj = ufd0;
							}
						}

						if (obj != null) {
							ufd = new UFDouble(obj.toString());
							if (ufd.compareTo(ufd0) > 0) {
								iserr = true;
								if (!htErr.containsKey(crowno)) {
									htErr.put(crowno, "");
									alErr.add(crowno);
								}
								break;
							}
						}
					}
					// 检查期初的
					if (!iserr && m_UFbIfEarly.booleanValue()) {
						for (int j = 0; j < iEarlyCheckLen; j++) {
							obj = getCtBillCardPanel().getBodyValueAt(row[i],
									saEarlyCheck[j]);
							if (obj != null) {
								ufd = new UFDouble(obj.toString());
								if (ufd.compareTo(ufd0) > 0) {
									iserr = true;
									if (!htErr.containsKey(crowno)) {
										htErr.put(crowno, "");
										alErr.add(crowno);
									}
									break;
								}
							}
						}
					}
				}
			}
			if (iserr) {
				if (m_UFbIfEarly.booleanValue()) {
					err.append(
							nc.ui.ml.NCLangRes.getInstance().getStrByID(
									"4020pub", "UPP4020pub-000060")/*
																	 * @res
																	 * "下列行可能被单据引用或者有期初数据:"
																	 */).append("\n");
				} else {
					err.append(
							nc.ui.ml.NCLangRes.getInstance().getStrByID(
									"4020pub", "UPP4020pub-000061")/*
																	 * @res
																	 * "下列行可能被单据引用:"
																	 */).append("\n");
				}
				for (int i = 0; i < alErr.size(); i++) {
					err.append((String) alErr.get(i));
					if (i != alErr.size() - 1)
						err.append(",");
				}

				err.append("\n").append(
						nc.ui.ml.NCLangRes.getInstance().getStrByID(
								"SCMCOMMON", "UPPSCMCommon-000018")/*
																	 * @res
																	 * "不能被删除!"
																	 */);

				MessageDialog.showErrorDlg(this, null, err.toString());
				return;
			}
		}
		if (!getCtBillCardPanel().delLine()) {
			showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"SCMCOMMON", "UPPSCMCommon-000172")/* @res "没有选中行!" */);
		}
		// setButtonState();
		if (!m_bAddFromBillFlag) {
			setButtonState();
		} else {
			setButtonStateForCof();
		}
	}

	/**
	 * 此处插入方法说明。 创建日期：(2001-10-27 16:56:00)
	 */
	protected void onDelTbLine() {

		String sMessage = nc.ui.ml.NCLangRes.getInstance().getStrByID(
				"SCMCOMMON", "UPPSCMCommon-000172")/* @res "没有选中行!" */;
		// vtTableVO.clear();
		// selectedRows=null;
		switch (m_iTabbedFlag) {
		case TabState.TERM:
			if (!getCtBillCardPanel().getBodyPanel("term").delLine())
				showErrorMessage(sMessage);
			break;
		case TabState.EXP:
			if (!getCtBillCardPanel().getBodyPanel("cost").delLine())
				showErrorMessage(sMessage);
			break;
		case TabState.NOTE:
			if (!getCtBillCardPanel().getBodyPanel("note").delLine())
				showErrorMessage(sMessage);
			break;
		default: // 变更页签
			if (!getCtBillCardPanel().getBodyPanel("history").delLine())
				showErrorMessage(sMessage);

		}

		m_iTbState = OperationState.DEL;
		// setButtonState();
		if (!m_bAddFromBillFlag) {
			setButtonState();
		} else {
			setButtonStateForCof();
		}
	}

	/**
	 * 此处插入方法说明。 创建日期：(2001-8-29 10:28:29) modified by lirr 2008-07-24
	 * 增加m_bAddFromBillFlag的判断
	 */
	protected void onEdit() {

		if (!m_bIsCard) // 如果是在单据列表模式下，则切换到卡片模式下
			onList();
		// added by lirr 2009-9-25上午11:05:11 是否需要合计
		boolean isneedcal = getCtBillCardPanel().getBillModel(CTTableCode.BASE)
				.isNeedCalculate();
		getCtBillCardPanel().getBillModel(CTTableCode.BASE).setNeedCalculate(
				false);

		showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4020pub",
				"UPP4020pub-000062")/* @res "修改当前合同，然后可以点击“保存”按钮进行保存" */);
		CTTool tool = new CTTool();
		Object[] objAssunit = tool.getInvids(getCurVO());
		// 修改时间: 2009-09-10 修改人: wuwp 修改人: 报空指针异常，从而影响了按钮的状态
		if (null != objAssunit) {
			if (null != objAssunit[0] && null != objAssunit[1])
				InvTool.loadBatchInvConvRateInfo((String[]) objAssunit[0],
						(String[]) objAssunit[1]);
		}

		// modifed by lirr 2008-08-07
		// m_iBillState = OperationState.EDIT;
		if (!m_bAddFromBillFlag) {
			m_iBillState = OperationState.EDIT;
		} else {
			m_iBillState = OperationState.ADD;

			int iRowcount = getCtBillCardPanel().getBillModel(CTTableCode.BASE)
					.getRowCount();
			for (int i = 0; i < iRowcount; i++) {

				if (InvTool.isAssUnitManaged((String) getCtBillCardPanel()
						.getBodyValueAt(i, "invbasid"))) {
					// getCtBillCardPanel().calculateFirChangeAboutBodyHSLS(i,"invcode");
					/*
					 * String invCodePk =
					 * (String)getCtBillCardPanel().getBodyValueAt(i,"invcode");
					 * if (null != invCodePk && false ==
					 * "".equals(invCodePk.trim())) {
					 * getCtBillCardPanel().calculateFirChangeAboutBodyHSLS(i,
					 * "invcode"); }
					 */
					if (null != getCtBillCardPanel().getBodyItem("astmeasname")
							.getComponent()) {
						String astmeasPk = (String) getCtBillCardPanel()
								.getBodyValueAt(i, "astmeasid");
						if (null != astmeasPk
								&& false == "".equals(astmeasPk.trim())) {
							/*
							 * BillEditEvent evAstunit = new BillEditEvent(
							 * getCtBillCardPanel().getBodyItem("astmeasname"),astmeasPk,
							 * "astmeasname",i); afterAstunitEditNew(evAstunit);
							 */
							BillEditEvent evAstunit = new BillEditEvent(
									getCtBillCardPanel().getBodyItem(
											"astmeasname"), astmeasPk,
									"astmeasname", i);
							// CTTool tool = new CTTool();
							// Object[] objAssunit = tool.getInvids(getCurVO());
							// InvTool.loadBatchInvConvRateInfo((String[])objAssunit[0],(String[])objAssunit[1]);
							afterAstunitEdit(evAstunit);
						}
					}

				}

			}
			String manPk = getCtBillCardPanel().getHeadItem("custname")
					.getValue();
			/*
			 * ((UIRefPane) getCtBillCardPanel().getHeadItem(
			 * "custname").getComponent()).getRefPK();
			 */
			if (null != manPk && false == "".equals(manPk.trim())) {
				BillEditEvent ev = new BillEditEvent(getCtBillCardPanel()
						.getHeadItem("custname"), manPk, "custname");
				afterCustNameEdit(m_bAddFromBillFlag);
			}

			String currname = getCtBillCardPanel().getHeadItem("currname")
					.getValue();
			/*
			 * ((UIRefPane) getCtBillCardPanel().getHeadItem(
			 * "currname").getComponent()).getRefPK();
			 */
			if (null != currname && false == "".equals(currname.trim())) {
				BillEditEvent evCurrname = new BillEditEvent(
						getCtBillCardPanel().getHeadItem("currname"), currname,
						"currname");
				afterCurrNameEdit(m_bAddFromBillFlag);
			}

			String currrate = getCtBillCardPanel().getHeadItem("currrate")
					.getValue();
			if (null != currrate && false == "".equals(currrate.trim())) {
				BillEditEvent evCurrrate = new BillEditEvent(
						getCtBillCardPanel().getHeadItem("currrate"), currrate,
						"currrate");

				afterCurrrateEdit(evCurrrate);

			}
			if (null != getCtBillCardPanel().getHeadItem("pername")
					.getComponent()) {
				String pername = getCtBillCardPanel().getHeadItem("pername")
						.getValue();
				if (null != pername && false == "".equals(pername.trim())) {
					BillEditEvent evPername = new BillEditEvent(
							getCtBillCardPanel().getHeadItem("pername"),
							pername, "pername");
					try {
						afterPersonEdit(evPername, m_bAddFromBillFlag);
					} catch (BusinessException e) {
						// TODO 自动生成 catch 块
						GenMethod.handleException(this, e.getMessage(), e);
					}
				}
			}
		}

		ExtendManageVO curVO = null;
		ManageItemVO[] curItemvo = null;
		// added by lirr 2008-08-07
		if (!m_bAddFromBillFlag) {
			curVO = (ExtendManageVO) m_vBillVO.elementAt(m_iId - 1);
			curItemvo = (ManageItemVO[]) curVO.getTableVO("table");
			try {
				// 回写界面表头VO主键
				getCtBillCardPanel().setHeadItem("pk_ct_manage",
						curVO.getParentVO().getPrimaryKey().toString());
				// 回写界面表体VO主键
				for (int i = 0; i < curItemvo.length; i++) {
					getCtBillCardPanel().setBodyValueAt(
							curItemvo[i].getPrimaryKey().toString(), i,
							"pk_ct_manage_b");
					getCtBillCardPanel().setBodyValueAt(
							curItemvo[i].getPk_ct_manage().toString(), i,
							"pk_ct_manage");
				}

				// 判断是否保留最初制单人
				if (m_isSaveInitOper.booleanValue() == false) {
					getCtBillCardPanel().setTailItem("opername", m_sPkUser);
					getCtBillCardPanel().setTailItem("operid", m_sPkUser);
				}
			} catch (Exception e) {
				SCMEnv.out("回写界面VO时抛出异常！"); /*-=notranslate=-*/
				nc.vo.scm.pub.SCMEnv.out(e);
			}

		} else {
			curVO = (ExtendManageVO) m_vBillVOForRef.elementAt(m_iId - 1);
			curItemvo = (ManageItemVO[]) curVO.getTableVO("table");
		}

		try {
			getCtBillCardPanel().setEnabled(true);
			// modified by lirr 2008-10-08 转单过来 合同编码应该能修改

			if (!m_bAddFromBillFlag) {
				// 合同编码不可修改
				getCtBillCardPanel().getHeadItem("ct_code").setEnabled(false);
			} else {
				getCtBillCardPanel().getHeadItem("ct_code").setEnabled(true);
			}
			/*
			 * // 合同编码不可修改
			 * getCtBillCardPanel().getHeadItem("ct_code").setEnabled(false);
			 */

			if (getCtBillCardPanel().getHeadItem("currid") != null) {
				String s_currid = getCtBillCardPanel().getHeadItem("currid")
						.getValue();

				if (m_sLocalCurrID != null) {
					if (m_sLocalCurrID.equals(s_currid))
						getCtBillCardPanel().getHeadItem("currrate")
								.setEnabled(false);
				}
				// modified by lirr 2008-11-22 修改原因：表体计算采用新方式
				// setFormularByCur(s_currid);
			}
		} catch (Exception e) {
			nc.vo.scm.pub.SCMEnv.out(e);
		}
		if (!m_bAddFromBillFlag) {
			setButtonState();
		} else {
			setButtonStateForCof();
		}
		rightButtonRightControl();
		getCtBillCardPanel().getBillModel(CTTableCode.BASE).setNeedCalculate(
				isneedcal);

	}
	/**
	 * 此处插入方法说明。 创建日期：(2001-10-24 9:55:57)
	 */
	protected void onEditTbLine() {
		getCtBillCardPanel().setEnabled(true);
		BillItem[] headitem = getCtBillCardPanel().getHeadItems();
		for (int i = 0; i < headitem.length; i++)
			headitem[i].setEnabled(false);
		// int selectedRow= -1;
		switch (m_iTabbedFlag) {
		// modified by lirr 2009-7-9下午03:46:35 在修改页签前updateValue()

		case TabState.TERM:
			getCtBillCardPanel().getBillModel("term").updateValue();
			getCtBillCardPanel().getBillModel("term").setEnabled(true);
			break;
		case TabState.EXP:
			getCtBillCardPanel().getBillModel("cost").updateValue();
			getCtBillCardPanel().getBillModel("cost").setEnabled(true);
			break;
		case TabState.NOTE:
			getCtBillCardPanel().getBillModel("note").updateValue();
			getCtBillCardPanel().getBillModel("note").setEnabled(true);
			break;
		default: // 变更页签

			int rowCount = getCtBillCardPanel().getBillModel("history")
					.getRowCount();
			if (rowCount <= 1) {
				showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"4020pub", "UPP4020pub-000064")/* @res "变更历史目前不能修改." */);
				return;
			}
			for (int i = 0; i < rowCount; i++) {
				if (i == 0) {
					getCtBillCardPanel().getBillModel("history")
							.setCellEditable(i, "memo", false);
					getCtBillCardPanel().getBillModel("history")
							.setCellEditable(i, "chgreason", false);
				} else {
					getCtBillCardPanel().getBillModel("history")
							.setCellEditable(i, "memo", true);
					getCtBillCardPanel().getBillModel("history")
							.setCellEditable(i, "chgreason", true);
				}
			}
		}
		m_iTbState = OperationState.EDIT;
		// setButtonState();
		if (!m_bAddFromBillFlag) {
			setButtonState();
		} else {
			setButtonStateForCof();
		}
		// added by lirr 2009-7-13上午10:59:20

		getCtBillCardPanel()
				.getBodyPanel(CTTableCode.CT_TABCODE[m_iTabbedFlag])
				.setBBodyMenuShow(false);

	}

	/**
	 * 此处插入方法说明。 创建日期：(2001-9-19 15:56:57)
	 */
	protected void onFreeze() {

		String sMessage = null;

		try {

			ManageVO tempMVO = new ManageVO();
			ManageVO mVO = getCurVO();
			tempMVO = mVO.clone(mVO);
			int ctState = Integer.parseInt(tempMVO.getParentVO()
					.getAttributeValue("ctflag").toString());

			if (ctState == BillState.VALIDATE) { // 生效状态
				sMessage = nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"4020pub", "UPP4020pub-000065")/* @res "是否确定要冻结该合同？" */;

				if (MessageDialog.showYesNoDlg(this, null, sMessage) == 4) {
					tempMVO.getParentVO().setAttributeValue("ctflag",
							new Integer(BillState.FREEZE));

					// 当前操作员
					tempMVO.getParentVO().setAttributeValue("coperatoridnow",
							getOperator());

					tempMVO.setOldBillStatus(BillState.VALIDATE);
					tempMVO.setBillStatus(BillState.FREEZE);

					m_sExecFlag = CTExecFlow.FREEZE; // "冻结";
					// added by lirr 2009-9-14下午02:45:29 设置执行过程的动作及原因
					CTTool.setExecReason(tempMVO, this);
					ArrayList alRet = null;

					// ManageBO_Client.update(tempMVO);
					alRet = (ArrayList) nc.ui.pub.pf.PfUtilClient
							.processAction(
									"FREEZE",
									m_sBillType,
									getClientEnvironment().getDate().toString(),
									tempMVO, tempMVO);

					// m_sExecFlag = CTExecFlow.FREEZE; // "冻结";
					// createExec(mVO); // 创建执行过程记录

					m_bIsNeedReInit[TabState.EXEC] = true; // 执行过程更新，修重新载入数据

					mVO.getParentVO().setAttributeValue("ctflag",
							new Integer(BillState.FREEZE));
					getCtBillCardPanel().setHeadItem("ctflag",
							new Integer(BillState.FREEZE));
					getCtBillListPanel().getHeadBillModel().setValueAt(
							nc.ui.ml.NCLangRes.getInstance().getStrByID(
									"4020const", BillState.STATERESID_FREEZE),
							m_iId - 1, "ctflag");
					// modified by lirr 2009-8-24上午10:42:00 刷新ts不再走后台，在动作中返回
					if (alRet != null && alRet.size() > 0)
						freshStatusTs(mVO, (String) alRet.get(1));
					ExtendManageVO voCur = (ExtendManageVO) m_vBillVO
							.get(m_iId - 1);
					voCur.setParentVO(mVO.getParentVO());
					// modified by lirr 2009-9-14下午03:37:37
					// voCur.setTableVO("exec", mVO.getManageExecs());
					CTTool.setExecVO(voCur);
					m_vBillVO.remove(m_iId - 1);
					m_vBillVO.insertElementAt(voCur, m_iId - 1);
					getCtBillCardPanel().setBillValueVO(voCur);
					setIdtoName();
					getCtBillCardPanel().updateValue();
					if (!m_bIsCard) {
						getCtBillListPanel().setBodyValueVO("exec",
								mVO.getManageExecs());
					}
				}
			} else { // 状态不一致
				MessageDialog.showWarningDlg(this, null, nc.ui.ml.NCLangRes
						.getInstance().getStrByID("SCMCOMMON",
								"UPPSCMCommon-000329")/*
														 * @res
														 * "状态有误，请重新查询后再操作！"
														 */);
			}

		} catch (java.sql.SQLException e) {
			MessageDialog.showErrorDlg(this,
					nc.ui.ml.NCLangRes.getInstance().getStrByID("4020pub",
							"UPP4020pub-000066")/* @res "数据库操作错误" */, e
							.getMessage());

		} catch (java.rmi.RemoteException e) {
			MessageDialog.showErrorDlg(this,
					nc.ui.ml.NCLangRes.getInstance().getStrByID("4020pub",
							"UPP4020pub-000067")/* @res "远程调用失败" */, e
							.getMessage());

		} catch (BusinessException e) {
			MessageDialog.showErrorDlg(this, null, e.getMessage());
			reportException(e);

		} catch (Exception e) {
			MessageDialog.showErrorDlg(this, null, e.getMessage());
			reportException(e);

		}
		setButtonState();
	}

	/**
	 * 此处插入方法说明。 创建日期：(2001-9-6 17:22:38) modified by lirr 2008-07-23
	 * 参照请购单、价格审批单生成合同时，首先列表得到VO交换的数据，选中一条卡片显示，与原来的列别——>卡片不同
	 */
	protected void onList() {
		// 是否保存处理
		try {
			if (haveQuestion()) {

				getCtBillCardPanel().setEnabled(false);
				m_iBillState = OperationState.FREE;

				// 将页签转移到"合同基本"上.
				getCtBillCardPanel().getBodyTabbedPane().setSelectedIndex(
						TabState.BILL);
				getCtBillListPanel().getBodyTabbedPane().setSelectedIndex(
						TabState.BILL);
				m_iTabbedFlag = TabState.BILL;

				if (m_bIsCard) { // 卡片 --〉列表
					m_bIsCard = false;
					getButtonTree().getButton(CTButtonConst.BTN_SWITCH)
							.setName(
									nc.ui.ml.NCLangRes.getInstance()
											.getStrByID("common", "UCH021")/*
																			 * @res
																			 * "卡片显示"
																			 */);
					getButtonTree().getButton(CTButtonConst.BTN_SWITCH)
							.setHint(
									nc.ui.ml.NCLangRes.getInstance()
											.getStrByID("common", "UCH021")/*
																			 * @res
																			 * "卡片显示"
																			 */);

					setListRateDigit();
					if (m_bAddFromBillFlag) {// added by lirr 2008-07-24
						setListDataForRef();
					} else {

						setHeaderListData();

						getCtBillListPanel().getHeadTable()
								.setCellSelectionEnabled(false);
						getCtBillListPanel().getHeadTable()
								.setRowSelectionAllowed(true);

						getCtBillListPanel().getHeadTable()
								.setRowSelectionInterval(m_iId - 1, m_iId - 1);
						getCtBillListPanel().getBodyTable()
								.setRowSelectionInterval(0, 0);
					}
					getCtBillListPanel().setVisible(true);
					getCtBillCardPanel().setVisible(false);

				} else { // 列表 --〉卡片

					m_bIsCard = true;
					getButtonTree().getButton(CTButtonConst.BTN_SWITCH)
							.setName(
									nc.ui.ml.NCLangRes.getInstance()
											.getStrByID("common", "UCH022")/*
																			 * @res
																			 * "列表显示"
																			 */);
					getButtonTree().getButton(CTButtonConst.BTN_SWITCH)
							.setHint(
									nc.ui.ml.NCLangRes.getInstance()
											.getStrByID("common", "UCH022")/*
																			 * @res
																			 * "列表显示"
																			 */);

					if (m_iId > 0) {

						if (m_bAddFromBillFlag) {// 是否参照其他单据(请购单，价格审批单)生成合同
							ExtendManageVO mVOForRef = (ExtendManageVO) m_vBillVOForRef
									.elementAt(m_iId - 1);
							m_iBillState = OperationState.ADD;

							getCtBillCardPanel().addNew();
							// 表体自动增行
							getCtBillCardPanel().setAutoAddEditLine(true);
							// 表体鼠标右键增删行
							getCtBillCardPanel().setBodyMenuShow(true);

							// 

							ManageVO mVOForRefBill = new ManageVO();
							ManageHeaderVO voHead = (ManageHeaderVO) mVOForRef
									.getParentVO();
							mVOForRefBill.setParentVO(voHead);
							mVOForRefBill.setChildrenVO(mVOForRef
									.getTableVO("table"));

							ManageVO voTemp = mVOForRefBill
									.clone(mVOForRefBill);

							voTemp.getParentVO().setPrimaryKey(null);
							voTemp.getParentVO().setAttributeValue(
									"actualvalidate", null);
							voTemp.getParentVO().setAttributeValue(
									"actualinvalidate", null);
							voTemp.getParentVO().setAttributeValue("valdate",
									null);
							voTemp.getParentVO().setAttributeValue(
									"invallidate", null);
							voTemp.getParentVO().setAttributeValue("ts", null);
							voTemp.getParentVO().setAttributeValue(
									"nprepaymny", null);
							voTemp.setChangeBb1s(null); // 合同变更
							voTemp.setExpBb3s(null);
							voTemp.setManageExecs(null);
							voTemp.setMemoraBb2s(null);

							// 清表体的回写数据
							for (int i = 0; i < voTemp.getChildrenVO().length; i++) {
								voTemp.getChildrenVO()[i].setAttributeValue(
										"pk_ct_manage_b", null);
								voTemp.getChildrenVO()[i].setAttributeValue(
										"pk_ct_manage", null);
								/*
								 * voTemp.getChildrenVO()[i].setAttributeValue(
								 * "ordprice", null);
								 * voTemp.getChildrenVO()[i].setAttributeValue(
								 * "ordnum", null);
								 * voTemp.getChildrenVO()[i].setAttributeValue(
								 * "ordsum", null);
								 */
								voTemp.getChildrenVO()[i].setAttributeValue(
										"ts", null);
								voTemp.getChildrenVO()[i].setAttributeValue(
										"crowno", null);
							}
							// added by lirr 2009-9-8下午04:31:28 减少连接数处理参照类型的item
							CTTool.processNameItems(getCtBillCardPanel(),
									voTemp);
							getCtBillCardPanel().setBillValueVO(voTemp);

							getCtBillCardPanel().resetAllRowNo();

							// 改变单据，Table需要重新载入数据
							for (int i = 1; i < 6; i++)
								m_bIsNeedReInit[i] = true;
							getCtBillCardPanel().setEnabled(true);

						} else {
							ExtendManageVO mVO = (ExtendManageVO) m_vBillVO
									.elementAt(m_iId - 1);
							String currid = ((ManageHeaderVO) mVO.getParentVO())
									.getCurrid();

							// 获得此币种的小数位数
							int currDigit = 2;
							try {
								Integer integer = (Integer) m_hCurrDigit
										.get(currid);
								if (integer != null)
									currDigit = integer.intValue();

							} catch (Exception e) {
								currDigit = 2;
							}

							// 定义原币金额小数位数
							getCtBillCardPanel().getBodyItem(CTTableCode.BASE,
									"orisum").setDecimalDigits(currDigit);
							getCtBillCardPanel().getBodyItem(CTTableCode.BASE,
									"oritaxmny").setDecimalDigits(currDigit);
							getCtBillCardPanel().getBodyItem(CTTableCode.BASE,
									"oritaxsummny").setDecimalDigits(currDigit);

							// added by lirr 2009-7-23 下午03:30:09 累计收付金额 总额精度
							getCtBillCardPanel().getBodyItem(CTTableCode.BASE,
									"noritotalgpmny").setDecimalDigits(
									currDigit);
							if (null != getCtBillCardPanel().getHeadItem(
									"nprepaylimitmny"))
								getCtBillCardPanel().getHeadItem(
										"nprepaylimitmny").setDecimalDigits(
										currDigit);
							if (null != getCtBillCardPanel().getHeadItem(
									"noriprepaymny"))
								getCtBillCardPanel().getHeadItem(
										"noriprepaymny").setDecimalDigits(
										currDigit);
							if (null != getCtBillCardPanel().getHeadItem(
									"noritotalgpamount"))
								getCtBillCardPanel().getHeadItem(
										"noritotalgpamount").setDecimalDigits(
										currDigit);
							if (null != getCtBillCardPanel().getHeadItem(
									"norigpshamount"))
								getCtBillCardPanel().getHeadItem(
										"norigpshamount").setDecimalDigits(
										currDigit);
							if (null != getCtBillCardPanel().getBodyItem(
									CTTableCode.BASE, "norigpshamount"))
								getCtBillCardPanel().getBodyItem(
										CTTableCode.BASE, "norigpshamount")
										.setDecimalDigits(currDigit);
							if (null != getCtBillCardPanel().getBodyItem(
									CTTableCode.BASE, "noritotalshgpmny"))
								getCtBillCardPanel().getBodyItem(
										CTTableCode.BASE, "noritotalshgpmny")
										.setDecimalDigits(currDigit);

							// getCtBillCardPanel().getBodyItem(CTTableCode.BASE,
							// "ordsum").setDecimalDigits(currDigit);
							// //订单执行累计本币价税合计

							// 合同费用页签中金额的精度
							getCtBillCardPanel().getBodyItem(CTTableCode.COST,
									"expsum").setDecimalDigits(currDigit);
							/*
							 * m_iRateDigit = PubSetUI.getBothExchRateDigit(
							 * m_sPkCorp, currid);
							 */
							/*
							 * getCtBillCardPanel().getHeadItem("currrate")
							 * .setDecimalDigits(m_iRateDigit[0]);
							 */
							// modified by lirr 2008-12-24 折本汇率精度新方法 since v55
							getCtBillCardPanel().getHeadItem("currrate")
									.setDecimalDigits(getCurrateDigit(currid));
							/*
							 * getCtBillCardPanel().getHeadItem("astcurrate")
							 * .setDecimalDigits(m_iRateDigit[1]);
							 */

							getCtBillCardPanel()
									.removeBillEditListenerHeadTail();
							getCtBillCardPanel().setBillValueVO(mVO);

							for (int i = 1; i <= 20; i++) {
								String key = "def" + i;
								BillItem item = getCtBillCardPanel()
										.getHeadItem(key);
								if (item != null && item.getDataType() == 7) {
									String pk = (String) ((ManageHeaderVO) mVO
											.getParentVO())
											.getAttributeValue("pk_defdoc" + i);
									if (pk != null && pk.length() > 0)
										((UIRefPane) item.getComponent())
												.setPK(pk);
								}
							}

						}
						// 执行公式
						getCtBillCardPanel().getBillModel().execLoadFormula();
						// 把id号给单据卡片的表头元素
						setIdtoName();
						getCtBillCardPanel().setAutoFiltNullValueRow(false);
						getCtBillCardPanel().updateValue();
						// 合计行标记
						getCtBillCardPanel().getTotalTableModel().setValueAt(
								nc.ui.ml.NCLangRes.getInstance().getStrByID(
										"common", "UC000-0001146")/*
																	 * @res "合计"
																	 * //added
																	 * by lirr
																	 * 2009-02-19
																	 */, 0, 0);
						getCtBillCardPanel().getBodyTabbedPane()
								.setSelectedIndex(0);

						if (0 < getCtBillCardPanel().getBillTable(
								CTTableCode.BASE).getRowCount())
							getCtBillCardPanel().getBillTable(CTTableCode.BASE)
									.setRowSelectionInterval(0, 0);
						getCtBillListPanel().setVisible(false);
						getCtBillCardPanel().setVisible(true);
					} else {
						getCtBillCardPanel().setEnabled(true);
						getCtBillCardPanel().addNew();
						getCtBillListPanel().setVisible(false);
						getCtBillCardPanel().setVisible(true);
					}
				}

			}
		} catch (Exception e) {
			nc.vo.scm.pub.SCMEnv.out(e);
		} finally {
			getCtBillCardPanel().addBillEditListenerHeadTail(this);
		}
		if (m_bAddFromBillFlag) {
			setButtonStateForCof();
		} else
			setButtonState();
	}

	/**
	 * 此处插入方法说明。 创建日期：(2001-8-29 14:15:36)
	 */
	protected void onNext() {
		// 是否保存处理
		if (haveQuestion()) {
			// 将页签转移到"合同基本"上.
			getCtBillCardPanel().getBodyTabbedPane().setSelectedIndex(
					TabState.BILL);
			getCtBillListPanel().getBodyTabbedPane().setSelectedIndex(
					TabState.BILL);
			m_iTabbedFlag = TabState.BILL;

			if (m_iId < m_iElementsNum) {
				m_iId++;
				loadBodyData();
				ExtendManageVO voExtend = (ExtendManageVO) m_vBillVO
						.elementAt(m_iId - 1);
				ManageVO mVO = getCurVO();
				String currid = ((ManageHeaderVO) mVO.getParentVO())
						.getCurrid();

				// 获得此币种的小数位数
				// 获得此币种的小数位数
				int currDigit;
				try {
					Integer integer = (Integer) m_hCurrDigit.get(currid);
					if (integer != null)
						currDigit = integer.intValue();
					else
						currDigit = 2;
				} catch (Exception e) {
					currDigit = 2;
				}
				// 定义原币金额小数位数
				getCtBillCardPanel().getBodyItem(CTTableCode.BASE, "orisum")
						.setDecimalDigits(currDigit);
				getCtBillCardPanel().getBodyItem(CTTableCode.BASE, "oritaxmny")
						.setDecimalDigits(currDigit);
				getCtBillCardPanel().getBodyItem(CTTableCode.BASE,
						"oritaxsummny").setDecimalDigits(currDigit);
				// getCtBillCardPanel().getBodyItem(CTTableCode.BASE,
				// "ordsum").setDecimalDigits(currDigit);

				// m_iRateDigit = PubSetUI.getBothExchRateDigit(m_sPkCorp,
				// currid);
				/*
				 * getCtBillCardPanel().getHeadItem("currrate").setDecimalDigits(
				 * m_iRateDigit[0]);
				 */
				// modified by lirr 2008-12-24 折本汇率精度新方法 since v55
				getCtBillCardPanel().getHeadItem("currrate").setDecimalDigits(
						getCurrateDigit(currid));

				/*
				 * getCtBillCardPanel().getHeadItem("astcurrate")
				 * .setDecimalDigits(m_iRateDigit[1]);
				 */

				getCtBillCardPanel().setBillValueVO(voExtend);

				// 把Id加入界面
				setIdtoName();
				// 执行公式
				getCtBillCardPanel().getBillModel().execLoadFormula();

				getCtBillCardPanel().updateValue();
				// 合计行标记
				getCtBillCardPanel().getTotalTableModel().setValueAt(
						nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
								"UC000-0001146")/* @res "合计" */, 0, 0);

				m_bChangeFlag = false; // 标志合同不可变更
				// 改变单据，Table需要重新载入数据
				for (int i = 1; i < 6; i++)
					m_bIsNeedReInit[i] = true;
				if (m_iTabbedFlag != TabState.BILL) { // 不是主表页签
					loadData();
					m_bIsNeedReInit[m_iTabbedFlag] = false;
				}

				// tableOperaState = "自由";
				if (!getButtonTree().getButton(
						CTButtonConst.BTN_BROWSE_PREVIOUS).isEnabled()) {
					getButtonTree()
							.getButton(CTButtonConst.BTN_BROWSE_PREVIOUS)
							.setEnabled(true);
					updateButton(getButtonTree().getButton(
							CTButtonConst.BTN_BROWSE_PREVIOUS));
				}
				if (m_iId == m_iElementsNum) {
					getButtonTree().getButton(CTButtonConst.BTN_BROWSE_NEXT)
							.setEnabled(false);
					updateButton(getButtonTree().getButton(
							CTButtonConst.BTN_BROWSE_NEXT));
				}
			}

			setButtonState();

		}
	}

	/**
	 * 此处插入方法说明。 创建日期：(2001-8-29 14:16:37)
	 */
	protected void onPre() {
		// 是否保存处理
		if (haveQuestion()) {
			// 将页签转移到"合同基本"上.
			getCtBillCardPanel().getBodyTabbedPane().setSelectedIndex(
					TabState.BILL);
			getCtBillListPanel().getBodyTabbedPane().setSelectedIndex(
					TabState.BILL);
			m_iTabbedFlag = TabState.BILL;

			if (m_iId > 1) {
				m_iId--;
				loadBodyData();
				ExtendManageVO voExtend = (ExtendManageVO) m_vBillVO
						.elementAt(m_iId - 1);
				ManageVO mVO = getCurVO();
				String currid = ((ManageHeaderVO) mVO.getParentVO())
						.getCurrid();

				// 获得此币种的小数位数
				int currDigit;
				try {
					Integer integer = (Integer) m_hCurrDigit.get(currid);
					if (integer != null)
						currDigit = integer.intValue();
					else
						currDigit = 2;
				} catch (Exception e) {
					currDigit = 2;
				}

				// 定义原币金额小数位数
				getCtBillCardPanel().getBodyItem(CTTableCode.BASE, "orisum")
						.setDecimalDigits(currDigit);
				getCtBillCardPanel().getBodyItem(CTTableCode.BASE, "oritaxmny")
						.setDecimalDigits(currDigit);
				getCtBillCardPanel().getBodyItem(CTTableCode.BASE,
						"oritaxsummny").setDecimalDigits(currDigit);
				// getCtBillCardPanel().getBodyItem(CTTableCode.BASE,
				// "ordsum").setDecimalDigits(currDigit);

				// m_iRateDigit = PubSetUI.getBothExchRateDigit(m_sPkCorp,
				// currid);
				/*
				 * getCtBillCardPanel().getHeadItem("currrate").setDecimalDigits(
				 * m_iRateDigit[0]);
				 */
				// modified by lirr 2008-12-24 折本汇率精度新方法 since v55
				getCtBillCardPanel().getHeadItem("currrate").setDecimalDigits(
						getCurrateDigit(currid));
				/*
				 * getCtBillCardPanel().getHeadItem("astcurrate")
				 * .setDecimalDigits(m_iRateDigit[1]);
				 */

				getCtBillCardPanel().setBillValueVO(voExtend);

				// getCtBillCardPanel().setBillValueVO(mVO);

				// 把Id加入界面
				setIdtoName();
				// 执行公式
				getCtBillCardPanel().getBillModel().execLoadFormula();

				getCtBillCardPanel().updateValue();
				// 合计行标记
				getCtBillCardPanel().getTotalTableModel().setValueAt(
						nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
								"UC000-0001146")/* @res "合计" */, 0, 0);

				m_bChangeFlag = false; // 标志合同不可变更
				// 改变单据，所有Table都需要重新载入数据
				for (int i = 1; i < 6; i++)
					m_bIsNeedReInit[i] = true;
				if (m_iTabbedFlag != TabState.BILL) { // 不是主表页签
					loadData();
					m_bIsNeedReInit[m_iTabbedFlag] = false;

				}
				// tableOperaState = "自由";
				if (!getButtonTree().getButton(CTButtonConst.BTN_BROWSE_NEXT)
						.isEnabled()) {
					getButtonTree().getButton(CTButtonConst.BTN_BROWSE_NEXT)
							.setEnabled(true);
					updateButton(getButtonTree().getButton(
							CTButtonConst.BTN_BROWSE_NEXT));
				}
				if (m_iId == 1) {
					getButtonTree()
							.getButton(CTButtonConst.BTN_BROWSE_PREVIOUS)
							.setEnabled(false);
					updateButton(getButtonTree().getButton(
							CTButtonConst.BTN_BROWSE_PREVIOUS));
				}
			}

			// getButtonTree().getButton(CTButtonConst.BTN_BROWSE_PREVIOUS).setEnabled(false);
			setButtonState();

		}

	}

	/*
	 * 得到单据模版中表头/表尾公式. 邵兵 on Jun 17, 2005
	 */
	protected String[] getHeadTailFormulas() {
		if (m_headTailFormulas == null) {
			// 合同表头&表体
			BillItem[] headTailItems = getCtBillCardPanel().getBillData()
					.getHeadTailItems();
			m_headTailFormulas = BillUtil.getFormulas(headTailItems,
					IBillItem.LOAD);
		}

		return m_headTailFormulas;
	}

	/*
	 * 得到单据模版中表体的合同基本公式. 邵兵 on Jun 17, 2005
	 */
	protected String[] getTableFormulas() {
		if (m_tableFormulas == null) {
			// 合同基本
			BillItem[] tableItems = getCtBillCardPanel().getBillData()
					.getBodyItemsForTable(CTTableCode.BASE);
			m_tableFormulas = BillUtil.getFormulas(tableItems, IBillItem.LOAD);
		}

		return m_tableFormulas;
	}

	/*
	 * 得到单据模版中表体的合同条款公式. 邵兵 on Jun 17, 2005
	 */
	protected String[] getTermFormulas() {
		if (m_termFormulas == null) {
			// 合同条款
			BillItem[] termItems = getCtBillCardPanel().getBillData()
					.getBodyItemsForTable(CTTableCode.TERM);
			m_termFormulas = BillUtil.getFormulas(termItems, IBillItem.LOAD);
		}

		return m_termFormulas;
	}

	/*
	 * 得到单据模版中表体的合同费用公式. 邵兵 on Jun 17, 2005
	 */
	protected String[] getCostFormulas() {
		if (m_costFormulas == null) {
			// 合同费用
			BillItem[] costItems = getCtBillCardPanel().getBillData()
					.getBodyItemsForTable(CTTableCode.COST);
			m_costFormulas = BillUtil.getFormulas(costItems, IBillItem.LOAD);
		}

		return m_costFormulas;
	}

	/*
	 * 两个功能: 1 对查询出来的结果执行公式.(main function) 2 将查询结果置入m_vBillVO. 邵兵 on Jun 17,
	 * 2005
	 */
	protected void execFormularAfterQuery(ManageVO[] arrMangevos) {
		// 存放所有表头vo
		CircularlyAccessibleValueObject[] headVosAll = new CircularlyAccessibleValueObject[arrMangevos.length];
		CircularlyAccessibleValueObject[] tableVOsAll = null;
		CircularlyAccessibleValueObject[] termVOsAll = null;

		ArrayList alTableVOs = new ArrayList(); // 所有表体vo
		ArrayList alTermVOs = new ArrayList(); // 所有条款vo

		int index = 0;

		ExtendManageVO[] arrExtendVO = new ExtendManageVO[arrMangevos.length];
		for (int k = 0; k < arrMangevos.length; k++) {
			arrExtendVO[k] = new ExtendManageVO();

			arrExtendVO[k].setParentVO(arrMangevos[k].getParentVO());
			headVosAll[k] = arrMangevos[k].getParentVO();

			// modified by liuzy 2008-04-17 V5.03查询改造，只返回第一条合同的表体与其他页签
			if (null == arrMangevos[k].getChildrenVO()) {
				m_vBillVO.add(arrExtendVO[k]);
				continue;
			}
			tableVOsAll = arrMangevos[k].getChildrenVO();
			arrExtendVO[k].setTableVO("table", tableVOsAll);
			for (index = 0; index < tableVOsAll.length; index++)
				alTableVOs.add(tableVOsAll[index]);

			termVOsAll = arrMangevos[k].getTermBb4s();
			arrExtendVO[k].setTableVO("term", termVOsAll);
			if (termVOsAll != null && termVOsAll.length > 0) {
				for (index = 0; index < termVOsAll.length; index++)
					alTermVOs.add(termVOsAll[index]);
			}

			arrExtendVO[k].setTableVO("cost", arrMangevos[k].getExpBb3s());
			arrExtendVO[k].setTableVO("note", arrMangevos[k].getMemoraBb2s());
			arrExtendVO[k]
					.setTableVO("history", arrMangevos[k].getChangeBb1s());
			arrExtendVO[k].setTableVO("exec", arrMangevos[k].getManageExecs());

			m_vBillVO.add(arrExtendVO[k]);
		}
		// 执行公式
		getCtBillListPanel().getBodyBillModel().execFormulasWithVO(headVosAll,
				getHeadTailFormulas());
		
		// 合同基本
		tableVOsAll = new CircularlyAccessibleValueObject[alTableVOs.size()];
		alTableVOs.toArray(tableVOsAll);

		// 自由项组合
		/*
		 * for (int k = 0; k < tableVOsAll.length; k++) { FreeVO voFree =
		 * InvTool.getInvFreeVO((String) tableVOsAll[k]
		 * .getAttributeValue("invid")); // 存货管理主键 if (voFree != null) {
		 * voFree.setVfree1((String) tableVOsAll[k]
		 * .getAttributeValue("vfree1")); voFree.setVfree2((String)
		 * tableVOsAll[k] .getAttributeValue("vfree2"));
		 * voFree.setVfree3((String) tableVOsAll[k]
		 * .getAttributeValue("vfree3")); voFree.setVfree4((String)
		 * tableVOsAll[k] .getAttributeValue("vfree4"));
		 * voFree.setVfree5((String) tableVOsAll[k]
		 * .getAttributeValue("vfree5"));
		 * 
		 * tableVOsAll[k].setAttributeValue("vfree0", voFree
		 * .getWholeFreeItem()); } }
		 */
		// modified by lirr 2009-8-20下午07:24:22 减少连接数
		nc.ui.scm.pub.FreeVOParse freeVOParse = new nc.ui.scm.pub.FreeVOParse();
		freeVOParse.setFreeVO(tableVOsAll, null, "invid", false);
		// 合同基本执行公式
		getCtBillListPanel().getBodyBillModel().execFormulasWithVO(tableVOsAll,
				getTableFormulas());

		if (alTermVOs.size() > 0) {
			termVOsAll = new CircularlyAccessibleValueObject[alTermVOs.size()];
			alTermVOs.toArray(termVOsAll);
			getCtBillListPanel().getBodyBillModel().execFormulasWithVO(
					termVOsAll, getTermFormulas());
		}

		// 合同费用子表不再执行公式
		// 原因：合同费用子表中的pk_ct_exp非合同费用定义中的pk_ct_expset，二者之间的联系loadformula不能根据这两个键建立联系。
		// shaobing on Jul 4, 2005
		/** ***查询后公式执行结束**** */
	}

	/*
	 * 获取逻辑面板的查询条件 和 单据的一些固有查询条件
	 */
	public String getLogConditonSQLForQryExecImprest(
			nc.vo.pub.query.ConditionVO[] conVO) {
		if (conVO == null || conVO.length <= 0)
			return null;
		// modified by lirr 2008-11-27 修改原因：如果like % 在DB2库报错
		ArrayList<ConditionVO> alist = new ArrayList<ConditionVO>(conVO.length);

		// 处理合同状态
		for (int i = 0; i < conVO.length; i++) {

			if (conVO[i].getFieldCode().equals("ct_manage.ctflag")
					&& "%".equals(conVO[i].getValue()))
				continue;

			/*
			 * if (conVO[i].getFieldCode().equals("ct_manage.ctflag")) { String
			 * sValue = conVO[i].getValue(); // modified by lirr 2008-11-27
			 * 修改原因：如果like % 在DB2库报错
			 * 
			 * 
			 * if ("%".equals(sValue)) { conVO[i].setOperaCode("like"); } }
			 */
			// modified by liuzy 2008-03-26 碧桂园增加默认查询条件
			if (conVO[i].getFieldCode().equals("ct_manage.operdate.from")
					|| conVO[i].getFieldCode().equals("ct_manage.operdate.end")) {
				conVO[i].setFieldCode("ct_manage.operdate");
			}
			alist.add(conVO[i]);

		}
		if (alist != null && alist.size() > 0) {
			conVO = alist.toArray(new ConditionVO[alist.size()]);
		}
		return conVO[0].getWhereSQL(conVO);
	}

	/*
	 * 获取合同的所有查询条件 包括 逻辑面板及单据固有的条件
	 */
	public String getConditonSQLForQryExecImprest() {
		return QueryDlgUtil.andTowWhere(getQueryConditionDlg().getWhereSQL(),
				getLogConditonSQLForQryExecImprest(getQueryConditionDlg()
						.getLogicalConditionVOs()));
	}

	/*
	 * 获取逻辑面板的查询条件 和 单据的一些固有查询条件
	 */
	protected String getLogConditonSQL(nc.vo.pub.query.ConditionVO[] conVO) {
		if (conVO == null || conVO.length <= 0)
			return null;

		ArrayList<ConditionVO> alist = new ArrayList<ConditionVO>(conVO.length);
		String invclsql = null;
		String dbilldatefrom = null;
		String dbilldateend = null;
		if (conVO != null) {
			for (int i = 0; i < conVO.length; i++) {
				if (conVO[i].getFieldCode().equals("ct_manage.pk_corp"))
					continue;

				if (conVO[i].getFieldCode().equals("ct_manage.ctflag")
						&& "%".equals(conVO[i].getValue()))
					continue;

				// 不用处理这个，新的查询模板不用处理这个

				if (conVO[i].getFieldCode().indexOf("invclid") > 0
						&& conVO[i].getOperaCode() != null
						&& !conVO[i].getOperaCode().equals("is")
						&& !conVO[i].getOperaCode().equals("in")
						&& conVO[i].getValue() != null
						&& conVO[i].getValue().trim().length() == 20) {
					invclsql = conVO[i].getSQLStr();
					invclsql = invclsql.replaceFirst("and", "and (");
					invclsql += " or invid in (SELECT pk_invmandoc FROM bd_invmandoc ";
					invclsql += "inner JOIN bd_invbasdoc ON bd_invmandoc.pk_invbasdoc = bd_invbasdoc.pk_invbasdoc ";
					invclsql += "inner JOIN bd_invcl ON bd_invbasdoc.pk_invcl = bd_invcl.pk_invcl WHERE ";
					invclsql += "bd_invcl.pk_invcl='"
							+ conVO[i].getValue().trim() + "' ) )";
					continue;
				}
				// modified by liuzy 2008-03-26 碧桂园增加默认查询条件
				if (conVO[i].getFieldCode().equals("ct_manage.operdate.from")
						|| conVO[i].getFieldCode().equals(
								"ct_manage.operdate.end")) {
					if (conVO[i].getFieldCode().equals(
							"ct_manage.operdate.from"))
						dbilldatefrom = conVO[i].getValue();

					if (conVO[i].getFieldCode()
							.equals("ct_manage.operdate.end"))
						dbilldateend = conVO[i].getValue();

					conVO[i].setFieldCode("ct_manage.operdate");
				}
				alist.add(conVO[i]);
			}

			if (null == dbilldatefrom || "".equals(dbilldatefrom.trim())
					|| null == dbilldateend || "".equals(dbilldateend.trim())) {
				showErrorMessage(NCLangRes.getInstance().getStrByID("4020pub",
						"UPP4020pub-000268")/* 【制单日期从】与【制单日期到】同为必输查询条件 */);
				return null;
			}

		}

		String sWhereClause = null;
		if (alist != null && alist.size() > 0) {
			sWhereClause = conVO[0].getWhereSQL(alist
					.toArray(new ConditionVO[alist.size()]));
			if (invclsql != null)
				sWhereClause += invclsql;
		} else {
			sWhereClause = conVO[0].getWhereSQL(conVO);
		}
		StringBuffer sbWhere = new StringBuffer();

		// 增加合同类型及是否期初的条件
		if (sWhereClause == null || sWhereClause.trim().length() == 0) {
			sbWhere.append("ct_type.nbusitype=");
			sbWhere.append(m_iCtType);
			if (m_UFbIfEarly.booleanValue())
				sbWhere.append(" and ct.ifearly='Y'");
			else
				sbWhere.append(" and ct.ifearly='N'");
		} else {
			sbWhere.append(sWhereClause);
			sbWhere.append(" and ct_type.nbusitype=");
			sbWhere.append(m_iCtType);
			if (m_UFbIfEarly.booleanValue())
				sbWhere.append(" and ct.ifearly='Y'");
			else
				sbWhere.append(" and ct.ifearly='N'");
		}

		// 加公司条件
		sbWhere.append(" and ct.pk_corp = '" + m_sPkCorp + "'");
		// modified by liuzy 2008-04-11 增加表体、合同类型公司查询条件
		// sbWhere.append(" and ct_b.pk_corp = '" + m_sPkCorp + "'");
		sbWhere.append(" and ct_type.pk_corp = '" + m_sPkCorp + "'");

		// 纪录查询条件，以便刷新使用
		return sbWhere.toString();
	}

	/*
	 * 获取合同的所有查询条件 包括 逻辑面板及单据固有的条件
	 */
	protected String getConditonSQL() {

		String sWhereSQL = QueryDlgUtil.andTowWhere(getQueryConditionDlg()
				.getWhereSQL(), getLogConditonSQL(getQueryConditionDlg()
				.getLogicalConditionVOs()));

		// 纪录查询条件，以便刷新使用
		m_sQryCondition = sWhereSQL;

		return sWhereSQL;
	}

	/**
	 * 此处插入方法说明。 创建日期：(2001-9-10 13:36:31)
	 */
	protected void onQuery() {

		if (haveQuestion()) {

			// 将页签转移到"合同基本"上.
			getCtBillCardPanel().getBodyTabbedPane().setSelectedIndex(
					TabState.BILL);
			getCtBillListPanel().getBodyTabbedPane().setSelectedIndex(
					TabState.BILL);
			m_iTabbedFlag = TabState.BILL;

			getCtBillCardPanel().setEnabled(false); // 单据卡片不可编辑
			m_iBillState = OperationState.FREE;

			// 显示查询对话框
			getQueryConditionDlg().showModal();

			if (getQueryConditionDlg().getResult() != nc.ui.pub.beans.UIDialog.ID_OK)
				return;

			try {
				m_timer.start("合同查询开始"); /*-=notranslate=-*/
				showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"4020pub", "UPP4020pub-000069")/* @res "请等待，正在查询合同……" */);

				// nc.vo.pub.query.ConditionVO[] conVO = getQueryConditionDlg()
				// .getConditionVO();

				// 得到查询条件
				String sWhereSQL = getConditonSQL();

				// modified by liuzy 2008-04-09 合同查询日期跨度过大导致服务器down机
				if (null == sWhereSQL) {
					showHintMessage("");
					return;
				}

				ManageVO[] arrMangevos = loadHeadData(m_sPkCorp, sWhereSQL);

				// 查询
				// ManageVO[] arrMangevos = ContractQueryHelper.queryBill(
				// m_sPkCorp, sWhereSQL);

				// 清空缓存vo。
				m_vBillVO.clear();

				if (arrMangevos == null || arrMangevos.length == 0) { // 查询结果为空
					m_iId = 0;
					m_iElementsNum = 0;
					getCtBillCardPanel().getBillData().clearViewData();
					getCtBillListPanel().setHeaderValueVO(null);
					getCtBillListPanel().getBodyBillModel().clearBodyData();

					// setButtonState();
					if (!m_bAddFromBillFlag) {
						setButtonState();
					} else {
						setButtonStateForCof();
					}
					this.showHintMessage(nc.ui.ml.NCLangRes.getInstance()
							.getStrByID("4020pub", "UPP4020pub-000070")/*
																		 * @res
																		 * "没有找到符合条件的数据"
																		 */);

					return;
				}
				// 加载公式
				execFormularAfterQuery(arrMangevos);

				if (m_vBillVO != null && m_vBillVO.size() > 0) {

					// 判断是在卡片模式下，还是在列表模式下
					m_bIsFirstClick = true; // 标志是第一次点击
					m_iId = 1;
					m_iElementsNum = m_vBillVO.size();

					if (m_bIsCard) { // 卡片
						// 切换到列表模式下
						onList();

					} else { // 列表
						// 不切换当前的列表模式，显示查询结果
						//setListRateDigit();
						// 显示单据列表
						setHeaderListData();
						getCtBillListPanel().getHeadTable()
								.setRowSelectionInterval(0, 0);

					}

					m_bChangeFlag = false; // 标志合同不可变更
					// 所有Table都需要重新载入数据
					for (int i = 1; i < 6; i++)
						m_bIsNeedReInit[i] = true;

					// setCardVOData(); //重新初始卡片数据

					/* @res "查询结束，共查到{0}条合同！" */
					String sMessage = nc.ui.ml.NCLangRes.getInstance()
							.getStrByID(
									"4020pub",
									"UPP4020pub-000071",
									null,
									new String[] { CommonConstant.BEGIN_MARK
											+ m_vBillVO.size()
											+ CommonConstant.END_MARK });
					showHintMessage(sMessage);

				} else {
					String sMessage = nc.ui.ml.NCLangRes.getInstance()
							.getStrByID("4020pub", "UPP4020pub-000072")/*
																		 * @res
																		 * "未查到符合条件的合同！"
																		 */;
					showHintMessage(sMessage);
					MessageDialog.showHintDlg(this, null, sMessage);
				}
				m_timer.showExecuteTime("合同查询结束："); /*-=notranslate=-*/

			} catch (Exception e) {
				nc.vo.scm.pub.SCMEnv.out(e);
				MessageDialog.showHintDlg(this, null, nc.ui.ml.NCLangRes
						.getInstance().getStrByID("4020pub",
								"UPP4020pub-000073")/* @res "合同查询出错！" */
						+ "\n" + e.getMessage());
			}
		}
		// setButtonState();
		if (!m_bAddFromBillFlag) {
			setButtonState();
		} else {
			setButtonStateForCof();
		}
	}

	/**
	 * 此处插入方法说明。 创建日期：(2001-8-24 14:24:22)
	 */
	// 修改说明：修改：将sava以及嵌套的保存方法返回类型由void改为boolean 修改人：张学坤 修改日期：2007-10-31
	protected boolean onSave() {
		Boolean result = false;
		switch (m_iTabbedFlag) {
		case TabState.BILL: // 合同主表页签
			if (m_iBillState == OperationState.CHANGE)
				result = saveChangedCT(); // 变更保存
			else
				result = saveManage(); // 修改保存
			break;

		case TabState.TERM: // 条款页签
			result = saveTerm();
			// modified by liuzy 2007-12-17 用快捷键保存后最后编辑的Item仍然保留焦点，手工重置焦点
			getCtBillCardPanel().transferFocusTo(BillCardPanel.BODY);
			break;

		case TabState.EXP: // 费用页签
			result = saveExp();
			// modified by liuzy 2007-12-17 用快捷键保存后最后编辑的Item仍然保留焦点，手工重置焦点
			getCtBillCardPanel().transferFocusTo(BillCardPanel.BODY);
			break;

		case TabState.NOTE: // 大事记页签
			result = saveNotes();
			// modified by liuzy 2007-12-17 用快捷键保存后最后编辑的Item仍然保留焦点，手工重置焦点
			getCtBillCardPanel().transferFocusTo(BillCardPanel.BODY);
			break;

		case TabState.CHANGE: // 变更页签
			result = saveChange();
		}
		// setButtonState();
		// added by lirr 2008-08-07 如果是参照生成合同（包括新增按钮和价格审批单生成合同）按钮状态不一样
		if (m_bAddFromBillFlag) {
			setButtonStateForCof();
		} else {
			setButtonState();
		}
	// added by lirr 2009-12-2上午09:56:12保存动作后清除getDmdo().setCellControls 否则先做少行数据再做多行数据 数组越界
    getCtBillCardPanel().getDmdo().setCellControls(null);
    
		return result;
	}

	/**
	 * 此处插入方法说明。 创建日期：(2001-9-19 15:57:11)
	 */
	protected void onTerminate() {

		String sMessage = null;

		try {

			ManageVO tempMVO = new ManageVO();
			ManageVO mVO = getCurVO();
			tempMVO = mVO.clone(mVO);
			int ctState = Integer.parseInt(tempMVO.getParentVO()
					.getAttributeValue("ctflag").toString());

			// 对处于不同状态的合同进行不同的处理
			if (ctState == BillState.VALIDATE) { // 生效状态
				sMessage = nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"4020pub", "UPP4020pub-000074")/* @res "是否确定要终止该合同？" */;

				if (MessageDialog.showYesNoDlg(this, null, sMessage) == 4) {
					tempMVO.getParentVO().setAttributeValue("ctflag",
							new Integer(BillState.TERMINATE));

					// 当前操作员
					tempMVO.getParentVO().setAttributeValue("coperatoridnow",
							getOperator());

					tempMVO.getParentVO().setAttributeValue("actualinvalidate",
							m_UFToday);

					tempMVO.setOldBillStatus(BillState.VALIDATE);
					tempMVO.setBillStatus(BillState.TERMINATE);
					m_sExecFlag = CTExecFlow.TERMINATE; // "实际终止";
					// added by lirr 2009-9-14下午02:45:29 设置执行过程的动作及原因
					CTTool.setExecReason(tempMVO, this);
					ArrayList alRet = null;
					// ManageBO_Client.update(tempMVO);
					alRet = (ArrayList) nc.ui.pub.pf.PfUtilClient
							.processAction("TERMINATE", m_sBillType, m_UFToday
									.toString(), tempMVO, tempMVO);

					// m_sExecFlag = CTExecFlow.TERMINATE; // "实际终止";
					// createExec(mVO); // 创建执行过程记录

					m_bIsNeedReInit[TabState.EXEC] = true; // 执行过程更新，修重新载入数据

					mVO.getParentVO().setAttributeValue("ctflag",
							new Integer(BillState.TERMINATE));

					mVO.getParentVO().setAttributeValue("actualinvalidate",
							getClientEnvironment().getDate());

					getCtBillListPanel().getHeadBillModel().setValueAt(
							getClientEnvironment().getDate(), m_iId - 1,
							"actualinvalidate");
					getCtBillCardPanel().getHeadItem("actualinvalidate")
							.setValue(getClientEnvironment().getDate());

					getCtBillCardPanel().setHeadItem("ctflag",
							new Integer(BillState.TERMINATE));
					getCtBillListPanel().getHeadBillModel().setValueAt(
							nc.ui.ml.NCLangRes.getInstance()
									.getStrByID("4020const",
											BillState.STATERESID_TERMINATE),
							m_iId - 1, "ctflag");
					// modified by lirr 2009-8-24上午10:42:00 刷新ts不再走后台，在动作中返回
					if (alRet != null && alRet.size() > 0)
						freshStatusTs(mVO, (String) alRet.get(1));

					ExtendManageVO voCur = (ExtendManageVO) m_vBillVO
							.get(m_iId - 1);
					voCur.setParentVO(mVO.getParentVO());
					// voCur.setTableVO("exec", mVO.getManageExecs());
					// modified by lirr 2009-9-14下午03:38:19
					CTTool.setExecVO(voCur);
					m_vBillVO.remove(m_iId - 1);
					m_vBillVO.insertElementAt(voCur, m_iId - 1);
					getCtBillCardPanel().setBillValueVO(voCur);
					setIdtoName();
					getCtBillCardPanel().updateValue();
					if (!m_bIsCard) {
						getCtBillListPanel().setBodyValueVO("exec",
								mVO.getManageExecs());
					}
				}
			} else { // 状态不一致
				MessageDialog.showWarningDlg(this, null, nc.ui.ml.NCLangRes
						.getInstance().getStrByID("SCMCOMMON",
								"UPPSCMCommon-000329")/*
														 * @res
														 * "状态有误，请重新查询后再操作！"
														 */);
			}

		} catch (java.sql.SQLException e) {
			MessageDialog.showErrorDlg(this,
					nc.ui.ml.NCLangRes.getInstance().getStrByID("4020pub",
							"UPP4020pub-000066")/* @res "数据库操作错误" */, e
							.getMessage());

		} catch (BusinessException e) {
			MessageDialog.showErrorDlg(this, null, e.getMessage());
			reportException(e);

		} catch (java.rmi.RemoteException e) {
			MessageDialog.showErrorDlg(this,
					nc.ui.ml.NCLangRes.getInstance().getStrByID("4020pub",
							"UPP4020pub-000067")/* @res "远程调用失败" */, e
							.getMessage());

		} catch (Exception e) {
			MessageDialog.showErrorDlg(this, null, e.getMessage());
			reportException(e);

		}
		setButtonState();
	}

	/**
	 * 此处插入方法说明。 创建日期：(2001-10-24 14:40:45)
	 */
	protected void onUnFreeze() {
		String sMessage = null;
		try {
			ManageVO tempMVO = new ManageVO();
			ManageVO mVO = getCurVO();
			tempMVO = mVO.clone(mVO);
			int ctState = Integer.parseInt(tempMVO.getParentVO()
					.getAttributeValue("ctflag").toString());

			// 对处于不同状态的合同进行不同的处理
			if (ctState == BillState.FREEZE) { // 解冻状态

				sMessage = nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"4020pub", "UPP4020pub-000075")/* @res "是否确定要解冻该冻结合同？" */;

				if (MessageDialog.showYesNoDlg(this, null, sMessage) == 4) {

					tempMVO.getParentVO().setAttributeValue("ctflag",
							new Integer(BillState.VALIDATE));
					// 当前操作员
					tempMVO.getParentVO().setAttributeValue("coperatoridnow",
							getOperator());

					tempMVO.setOldBillStatus(BillState.FREEZE);
					tempMVO.setBillStatus(BillState.VALIDATE);
					m_sExecFlag = CTExecFlow.UNFREEZE; // "解冻";
					// added by lirr 2009-9-14下午02:45:29 设置执行过程的动作及原因
					CTTool.setExecReason(tempMVO, this);

					ArrayList alRet = null;
					// ManageBO_Client.update(tempMVO);
					alRet = (ArrayList) nc.ui.pub.pf.PfUtilClient
							.processAction(
									"UNFREEZE",
									m_sBillType,
									getClientEnvironment().getDate().toString(),
									tempMVO, tempMVO);

					// m_sExecFlag = CTExecFlow.UNFREEZE; // "解冻";
					// createExec(mVO); // 创建执行过程记录

					m_bIsNeedReInit[TabState.EXEC] = true; // 执行过程更新，修重新载入数据

					mVO.getParentVO().setAttributeValue("ctflag",
							new Integer(BillState.VALIDATE));
					getCtBillCardPanel().setHeadItem("ctflag",
							new Integer(BillState.VALIDATE));
					getCtBillListPanel()
							.getHeadBillModel()
							.setValueAt(
									nc.ui.ml.NCLangRes
											.getInstance()
											.getStrByID(
													"4020const",
													BillState.STATERESID_VALIDATE),
									m_iId - 1, "ctflag");
					// modified by lirr 2009-8-24上午10:42:00 刷新ts不再走后台，在动作中返回
					if (alRet != null && alRet.size() > 0)
						freshStatusTs(mVO, (String) alRet.get(1));
					ExtendManageVO voCur = (ExtendManageVO) m_vBillVO
							.get(m_iId - 1);
					voCur.setParentVO(mVO.getParentVO());
					// voCur.setTableVO("exec", mVO.getManageExecs());
					// modified by lirr 2009-9-14下午03:38:19
					CTTool.setExecVO(voCur);
					m_vBillVO.remove(m_iId - 1);
					m_vBillVO.insertElementAt(voCur, m_iId - 1);
					getCtBillCardPanel().setBillValueVO(voCur);
					setIdtoName();
					getCtBillCardPanel().updateValue();
					if (!m_bIsCard) {
						getCtBillListPanel().setBodyValueVO("exec",
								mVO.getManageExecs());
					}
				}
			} else { // 状态不一致
				MessageDialog.showWarningDlg(this, null, nc.ui.ml.NCLangRes
						.getInstance().getStrByID("SCMCOMMON",
								"UPPSCMCommon-000329")/*
														 * @res
														 * "状态有误，请重新查询后再操作！"
														 */);
			}

		} catch (java.sql.SQLException e) {
			MessageDialog.showErrorDlg(this,
					nc.ui.ml.NCLangRes.getInstance().getStrByID("4020pub",
							"UPP4020pub-000066")/* @res "数据库操作错误" */, e
							.getMessage());

		} catch (java.rmi.RemoteException e) {
			MessageDialog.showErrorDlg(this,
					nc.ui.ml.NCLangRes.getInstance().getStrByID("4020pub",
							"UPP4020pub-000067")/* @res "远程调用失败" */, e
							.getMessage());

		} catch (Exception e) {
			MessageDialog.showErrorDlg(this, null, e.getMessage());
			reportException(e);

		}
		setButtonState();
	}

	/**
	 * 此处插入方法说明。 变更历史只能是修改. 不存在新增和删除记录. 创建日期：(2001-9-19 14:47:18)
	 */
	private boolean saveChange() {
		try {

			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"SCMCOMMON", "UPPSCMCommon-000134")/* @res "开始保存……" */);

			ChangeBb1VO[] aryNew = (ChangeBb1VO[]) (getCtBillCardPanel()
					.getBillModel("history").getBodyValueVOs(ChangeBb1VO.class
					.getName()));
			ChangeBb1VO[] arySave = aryNew;

			if (arySave != null && arySave.length > 0) {
				// 插入数据库
				ContractWriterHelper.updateChangeBb1s(arySave, m_sPkUser);
				((ExtendManageVO) m_vBillVO.get(m_iId - 1)).setTableVO(
						"history", arySave);
				getCtBillCardPanel().getBillModel("history").updateValue();
			}

			m_iTbState = OperationState.FREE;
			getCtBillCardPanel().setEnabled(false);

			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"SCMCOMMON", "UPPSCMCommon-000006")/* @res "保存成功" */);

		} catch (ValidationException e) {
			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"4020pub", "UPP4020pub-000076")/* @res "输入错误！" */);
			MessageDialog.showErrorDlg(this, null, e.getMessage());
			m_bErrFlag = true;
			return false;
		} catch (java.sql.SQLException e) {
			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"4020pub", "UPP4020pub-000077")/* @res "保存失败！" */);

			if (e.getErrorCode() == 1401) {
				MessageDialog.showErrorDlg(this, null, nc.ui.ml.NCLangRes
						.getInstance().getStrByID("4020pub",
								"UPP4020pub-000078")/*
													 * @res "输入非法的字符或输入了太长的字符串！"
													 */);
			} else if (e.getErrorCode() == 8152) {
				MessageDialog.showErrorDlg(this, null, nc.ui.ml.NCLangRes
						.getInstance().getStrByID("4020pub",
								"UPP4020pub-000078")/*
													 * @res "输入非法的字符或输入了太长的字符串！"
													 */);
			} else
				MessageDialog.showErrorDlg(this, null, e.getMessage());

			m_bErrFlag = true;

			return false;

		} catch (java.rmi.RemoteException e) {
			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"4020pub", "UPP4020pub-000077")/* @res "保存失败！" */);
			MessageDialog.showErrorDlg(this,
					nc.ui.ml.NCLangRes.getInstance().getStrByID("4020pub",
							"UPP4020pub-000067")/* @res "远程调用失败" */, e
							.getMessage());

			m_bErrFlag = true;
			return false;
		} catch (Exception e) {
			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"4020pub", "UPP4020pub-000077")/* @res "保存失败！" */);
			MessageDialog.showErrorDlg(this, null, e.getMessage());
			reportException(e);
			m_bErrFlag = true;
			return false;
		}
		return true;
	}

	/**
	 * 此处插入方法说明。 合同费用的新增,修改,删除保存. 创建日期：(2001-9-19 14:46:21) 修改日期: (2004-04-02)
	 * 此方法修改后,合同费用的保存不再根据行状态来分别调用后台的方法. 而是在后台根据行状态来选择批量处理的方法,如:批新增,批删除,批修改.
	 * 修改人:cqw
	 */
	private boolean saveExp() {
		String sMessage = null;
		try {
			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"SCMCOMMON", "UPPSCMCommon-000134")/* @res "开始保存……" */);

			// 从界面获得修改后的数据
			ExpBb3VO[] aryChange = (ExpBb3VO[]) (getCtBillCardPanel()
					.getBillModel("cost").getBodyValueChangeVOs(ExpBb3VO.class
					.getName()));
			// 从界面获得目前界面上的数据
			ExpBb3VO[] aryNew = (ExpBb3VO[]) (getCtBillCardPanel()
					.getBillModel("cost").getBodyValueVOs(ExpBb3VO.class
					.getName()));
			// 需要传送到后台的VO数组:
			ExpBb3VO[] arySave = null;
			// 将删除的行追加
			Vector vDel = new Vector();
			if (aryChange != null && aryChange.length > 0) {
				for (int i = 0; i < aryChange.length; i++) {
					if (aryChange[i].getStatus() == VOStatus.DELETED)
						vDel.add(aryChange[i]);
				}
			}
			if (vDel.size() > 0) {
				arySave = new ExpBb3VO[vDel.size() + aryNew.length];
				for (int i = 0; i < aryNew.length; i++)
					arySave[i] = aryNew[i];
				for (int j = 0; j < vDel.size(); j++)
					arySave[j + aryNew.length] = (ExpBb3VO) vDel.get(j);
			} else
				arySave = aryNew;
			// 是否包含新增行,如果包含,那么将公司和表头PK置入.
			if (arySave.length > 0) {
				int iLen = arySave.length;
				for (int i = 0; i < iLen; i++) {
					arySave[i].validate();
					if (arySave[i].getStatus() == VOStatus.NEW) {
						arySave[i].setPk_corp(m_sPkCorp);
						arySave[i].setPk_ct_manage(((ExtendManageVO) m_vBillVO
								.elementAt(m_iId - 1)).getParentVO()
								.getPrimaryKey().toString());
					}
					// 记录原始位置
					arySave[i].setPosition(new Integer(i));
				}
			}
			// 插入数据库
			if (arySave == null || arySave.length <= 0 || arySave[0] == null) {
				sMessage = nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"4020pub", "UPP4020pub-000079")/* @res "没有数据需要保存." */;
				throw new ValidationException(sMessage);
			}
			ManageVO manageVO = new ManageVO();
			manageVO.setExpBb3s(arySave);
			manageVO.setParentVO(m_vBillVO.elementAt(m_iId - 1).getParentVO());
			manageVO.getParentVO().setAttributeValue("coperatoridnow",m_sPkUser);
			ManageVO alRet = null;
			alRet = (ManageVO) nc.ui.pub.pf.PfUtilClient
			.processAction("SAVECOST", m_sBillType,
					getClientEnvironment().getDate()
							.toString(), manageVO, null);
			//aryChange = ContractWriterHelper.saveExpBb3s(arySave, m_sPkUser);
			// 回写Model.
			getCtBillCardPanel().setHeadItem("ts", alRet.getParentVO().getAttributeValue("ts"));
			((ExtendManageVO) m_vBillVO.get(m_iId - 1)).getParentVO().setAttributeValue("ts", alRet.getParentVO().getAttributeValue("ts"));
			// 回写m_vBillVO.
			((ExtendManageVO) m_vBillVO.get(m_iId - 1)).setTableVO("cost",alRet.getExpBb3s());
			getCtBillCardPanel().getBillModel("cost").setBodyDataVO(alRet.getExpBb3s());
			// 更新表体状态
			getCtBillCardPanel().getBillModel("cost").updateValue();
			// 将页签状态置为自由.
			m_iTbState = OperationState.FREE;
			// 卡片模板不可编辑.
			getCtBillCardPanel().setEnabled(false);

			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"SCMCOMMON", "UPPSCMCommon-000006")/* @res "保存成功" */);

		} catch (ValidationException e) {
			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"4020pub", "UPP4020pub-000076")/* @res "输入错误！" */);
			MessageDialog.showErrorDlg(this, null, e.getMessage());
			m_bErrFlag = true;
			return false;
		} catch (java.sql.SQLException e) {
			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"4020pub", "UPP4020pub-000077")/* @res "保存失败！" */);

			if (e.getErrorCode() == 1401) {
				MessageDialog.showErrorDlg(this, null, nc.ui.ml.NCLangRes
						.getInstance().getStrByID("4020pub",
								"UPP4020pub-000078")/*
													 * @res "输入非法的字符或输入了太长的字符串！"
													 */);
			} else if (e.getErrorCode() == 8152) {
				MessageDialog.showErrorDlg(this, null, nc.ui.ml.NCLangRes
						.getInstance().getStrByID("4020pub",
								"UPP4020pub-000078")/*
													 * @res "输入非法的字符或输入了太长的字符串！"
													 */);
			} else
				MessageDialog.showErrorDlg(this, null, e.getMessage());

			m_bErrFlag = true;
			return false;

		} catch (java.rmi.RemoteException e) {
			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"4020pub", "UPP4020pub-000077")/* @res "保存失败！" */);
			MessageDialog.showErrorDlg(this,
					nc.ui.ml.NCLangRes.getInstance().getStrByID("4020pub",
							"UPP4020pub-000067")/* @res "远程调用失败" */, e
							.getMessage());

			m_bErrFlag = true;
			return false;
		} catch (Exception e) {
			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"4020pub", "UPP4020pub-000077")/* @res "保存失败！" */);
			MessageDialog.showErrorDlg(this, null, e.getMessage());
			reportException(e);
			m_bErrFlag = true;
			return false;
		}
		return true;
	}

	/**
	 * 此处插入方法说明。 创建日期：(2001-9-19 14:45:37)
	 */
	private boolean saveManage() {

		// 操作员日志
		nc.vo.sm.log.OperatelogVO log = getNormalOperateLog();

		// 把id号给单据卡片的表头元素
		m_timer.start("合同保存开始："); /*-=notranslate=-*/
		// added by lirr 2009-11-16下午04:30:19 支持验证公式
		if(!getCtBillCardPanel().getBillData().execValidateFormulas())
		    return false;
		setNameToID();
		ManageVO newManageVO = null;
		ManageVO oldManageVO = null;
		ExtendManageVO oldExtendManageVO = null;
		// 过滤空行
		getCtBillCardPanel().stopEditing();
		// 得到界面VO
		ExtendManageVO vo = (ExtendManageVO) getCtBillCardPanel()
				.getBillValueVOExtended(
						ExtendManageVO.class.getName(),
						ManageHeaderVO.class.getName(),
						new String[] { ManageItemVO.class.getName(),
								TermBb4VO.class.getName(),
								ExpBb3VO.class.getName(),
								MemoraBb2VO.class.getName(),
								ChangeBb1VO.class.getName(),
								ManageExecVO.class.getName() });
		newManageVO = transVO(vo);

		// 给表头VO赋上pk_corp和ifearly的值
		// 修改时间：2009/10/09 修改人:wuweiping 修改原因: 合同审批流中，修改合同把PK_corp更新了
		if (m_iBillState == OperationState.ADD) {
			((ManageHeaderVO) newManageVO.getParentVO()).setPk_corp(m_sPkCorp);
		}
		// ((ManageHeaderVO) newManageVO.getParentVO()).setPk_corp(m_sPkCorp);
		((ManageHeaderVO) newManageVO.getParentVO()).setIfearly(m_UFbIfEarly);

		// 设置最后修改人
		newManageVO.getParentVO().setAttributeValue("clastoperatorid",
				m_sPkUser);
		newManageVO.getParentVO().setAttributeValue("vlastoperatorname",
				m_sPkUser);

		newManageVO.setBillType(m_sBillType);
		// 给表体VO赋上pk_ct_manage和pk_corp
		ManageItemVO[] bodyVO = (ManageItemVO[]) newManageVO.getChildrenVO();
		for (int i = 0; i < newManageVO.getChildrenVO().length; i++) {
			if (m_iBillState == OperationState.ADD) {
				bodyVO[i].setPk_corp(m_sPkCorp);
			}
		}

		// 设置是非变更保存
		newManageVO.setIsChange(new UFBoolean(false));
		// 设置存货控制类型:
		Object oRefValue = ((UIRefPane) getCtBillCardPanel().getHeadItem(
				"ct_type").getComponent()).getRefValue("ninvctlstyle");
		if (oRefValue != null)
			((ManageHeaderVO) newManageVO.getParentVO())
					.setInvControlType(new Integer(oRefValue.toString()));

		// 如果单据修改，则得到已删除的表体项,并加入到MewManageVO中
		if (m_iBillState == OperationState.EDIT) { // 得到删除的表体项
			ExtendManageVO voExtendChanged = (ExtendManageVO) getCtBillCardPanel()
					.getBillValueChangeVOExtended(
							ExtendManageVO.class.getName(),
							ManageHeaderVO.class.getName(),
							new String[] { ManageItemVO.class.getName(),
									TermBb4VO.class.getName(),
									ExpBb3VO.class.getName(),
									MemoraBb2VO.class.getName(),
									ChangeBb1VO.class.getName(),
									ManageExecVO.class.getName() });
			ManageVO changedVO = transVO(voExtendChanged);

			ManageItemVO[] ChangedItemVO = (ManageItemVO[]) changedVO
					.getChildrenVO();
			Vector vDelItem = new Vector();
			for (int i = 0; i < ChangedItemVO.length; i++) {
				if (ChangedItemVO[i].getStatus() == 3)
					vDelItem.addElement(ChangedItemVO[i]);
			}
			// 如果有删除的表体项,则加入到NewManageVO中
			if (vDelItem.size() > 0) {
				ManageItemVO[] itemVO = (ManageItemVO[]) newManageVO
						.getChildrenVO();
				ManageItemVO[] newItemVO = new ManageItemVO[itemVO.length
						+ vDelItem.size()];
				for (int i = 0; i < itemVO.length; i++) {
					newItemVO[i] = itemVO[i];
				}
				for (int i = 0; i < vDelItem.size(); i++) {
					newItemVO[i + itemVO.length] = (ManageItemVO) vDelItem
							.elementAt(i);
				}
				newManageVO.setChildrenVO(newItemVO);
			}
			if (m_bAddFromBillFlag) {
				oldExtendManageVO = (ExtendManageVO) m_vBillVOForRef
						.elementAt(m_iId - 1);
			} else {
				oldExtendManageVO = (ExtendManageVO) m_vBillVO
						.elementAt(m_iId - 1);
			}

			oldManageVO = new ManageVO();
			oldManageVO.setParentVO(oldExtendManageVO.getParentVO());
			oldManageVO.setChildrenVO(oldExtendManageVO.getTableVO("table"));
			// 给表头VO赋上pk_corp和ifearly的值
			// 修改时间：2009/10/09 修改人:wuweiping 修改原因: 合同审批流中，修改合同把PK_corp更新了
			if (m_iBillState == OperationState.ADD) {
				((ManageHeaderVO) newManageVO.getParentVO())
						.setPk_corp(m_sPkCorp);
			}
			// ((ManageHeaderVO)
			// oldManageVO.getParentVO()).setPk_corp(m_sPkCorp);
			((ManageHeaderVO) oldManageVO.getParentVO()).setAttributeValue(
					"coperatoridnow", m_sPkUser);
			oldManageVO.setBillType(m_sBillType);
		}

		try {

			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"4020pub", "UPP4020pub-000081")/* @res "开始保存当前合同……" */);
			// 检查输入的合法性
			newManageVO.validate();
			// 如果检查没有通过则返回
			if (checkSave(newManageVO) == false) {
				throw new ValidationException();
			}
			// added by lirr 2009-04-21 业务日志
			newManageVO.setOperatelogVO(log);
			if (m_iBillState == OperationState.ADD) {
				newManageVO.setEditStatus(nc.vo.pub.VOStatus.NEW);

				newManageVO.getParentVO().setAttributeValue("version", "1.0");
				newManageVO.getParentVO().setAttributeValue("activeflag",
						new Integer(0));
				newManageVO.getParentVO().setAttributeValue("audiid", null);
				newManageVO.getParentVO().setAttributeValue("auditdate", null);
				((ManageHeaderVO) newManageVO.getParentVO()).setAttributeValue(
						"coperatoridnow", m_sPkUser);
				// 为检查来源ts准备 added by lirr 2008-11-04
				//
				if (m_bAddFromBillFlag) {
					ExtendManageVO curVO = null;
					ManageItemVO[] curItemvo = null;
					// modified by lirr 2008-11-26 修改原因 界面有可能发生变化 比如删行
					// curVO = (ExtendManageVO) m_vBillVOForRef.elementAt(m_iId
					// - 1);
					curVO = vo;
					// modified by lirr 2008-12-12 修改原因 从界面取curItemvo
					// 来源的ts无法获得导致并发无法控制
					// curItemvo = (ManageItemVO[]) curVO.getTableVO("table");
					/*
					 * curItemvo = (ManageItemVO[])((ExtendManageVO)
					 * m_vBillVOForRef.elementAt(m_iId -
					 * 1)).getTableVO("table"); for(int index = 0; index <
					 * curItemvo.length; index ++){ ((ManageItemVO)
					 * newManageVO.getChildrenVO()[index]).setAttributeValue(
					 * "cupsourcehts",
					 * curItemvo[index].getAttributeValue("cupsourcehts"));
					 * ((ManageItemVO)
					 * newManageVO.getChildrenVO()[index]).setAttributeValue(
					 * "cupsourcebts",
					 * curItemvo[index].getAttributeValue("cupsourcebts")); }
					 */

					curItemvo = (ManageItemVO[]) curVO.getTableVO("table");
					for (int index = 0; index < curItemvo.length; index++) {
						String[] sourceTs = getSouceBillTs((String) curItemvo[index]
								.getAttributeValue("csourcebillbid"));
						if (null != sourceTs && sourceTs.length > 0) {
							((ManageItemVO) newManageVO.getChildrenVO()[index])
									.setAttributeValue("cupsourcehts",
											sourceTs[0]);
							((ManageItemVO) newManageVO.getChildrenVO()[index])
									.setAttributeValue("cupsourcebts",
											sourceTs[1]);
						}
					}

				}
				ArrayList alRet = null;

				newManageVO.setIsSaveCheck(true);
				// modified by lirr 2009-7-17 下午02:24:01 表头财 务本币累计收/付款总额
				// 改为表头财务原币累计收/付款总额
				setAraptotalgpamount(newManageVO);
				// add by lirr 2009-06-18 压缩数据
				ManageItemVO[] bakbvos = newManageVO.getItemVOs();
				newManageVO.compressBodyWhenSave();
				while (true) {
					try {
						// modified by lirr 2009-05-31 修改原因 流量问题 新增保存 voOld 应该=
						// voNew，因此不再从前台传

						/*
						 * alRet = (ArrayList) nc.ui.pub.pf.PfUtilClient
						 * .processAction("SAVEBASE", m_sBillType,
						 * getClientEnvironment().getDate() .toString(),
						 * newManageVO, newManageVO);
						 */

						alRet = (ArrayList) nc.ui.pub.pf.PfUtilClient
								.processAction("SAVEBASE", m_sBillType,
										getClientEnvironment().getDate()
												.toString(), newManageVO, null);
						// added by lirr 判断保存完毕后转向什么样的界面

						if (null != alRet && alRet.size() > 0) {
							/*
							 * // added by lirr 2008-07-23 if
							 * (m_bAddFromBillFlag) {
							 * m_vBillVOForRef.remove(m_iId - 1);
							 * m_iElementsNum--; onList();
							 * setButtonStateForCof(); }
							 */// else {
							newManageVO.setIsSaveCheck(true);

							if (alRet != null && alRet.size() >= 2)
								newManageVO = (ManageVO) alRet.get(1);
							// add by lirr 2009-06-18 压缩数据
							// newManageVO.setChildrenVO(bakbvos);

							m_iBillState = OperationState.FREE;

							// 把当前NewManageVO保存于m_vBillVO中
							ExtendManageVO newExtendManageVO = new ExtendManageVO();
							newExtendManageVO.setParentVO(newManageVO
									.getParentVO());
							newExtendManageVO.setTableVO("table", newManageVO
									.getChildrenVO());
							newExtendManageVO.setTableVO("history", newManageVO
									.getChangeBb1s());
							m_vBillVO.addElement(newExtendManageVO);

							m_iElementsNum++;
							// added by lirr 2008-07-23
							if (m_bAddFromBillFlag) {
								m_vBillVOForRef.remove(m_iId - 1);
								// m_iElementsNum--;
								onList();
								setButtonStateForCof();
							} else {
								m_iId = m_iElementsNum;

								getCtBillCardPanel().setBillValueVO(
										newExtendManageVO);
								setHeaderListData();
							}
							/*
							 * // modified by lirr 增加判断 if (!m_bAddFromBillFlag) {
							 * m_iId = m_iElementsNum;
							 * 
							 * getCtBillCardPanel().setBillValueVO(
							 * newExtendManageVO); setHeaderListData(); // 执行公式 }
							 * else { m_iId = m_vBillVOForRef.size() - 1; }
							 */

							if (m_iId > 1) { //
								getButtonTree().getButton(
										CTButtonConst.BTN_BROWSE_PREVIOUS)
										.setEnabled(true); // 临时
								updateButton(getButtonTree().getButton(
										CTButtonConst.BTN_BROWSE_PREVIOUS));
							}

						}
						break;
					}

					catch (OverMaxPriceException e) {
						if (showYesNoMessage(e.getMessage()) == MessageDialog.ID_YES) {
							newManageVO.setIsSaveCheck(false);
							continue;
						} else {
							m_bErrFlag = true;
							showHintMessage(nc.ui.ml.NCLangRes.getInstance()
									.getStrByID("4020pub", "UPP4020pub-000077")/*
																				 * @res
																				 * "保存失败！"
																				 */);
							return false;
						}
					} finally {
						newManageVO.setChildrenVO(bakbvos);

					}
				}

			} else if (m_iBillState == OperationState.EDIT) {
				newManageVO.setEditStatus(nc.vo.pub.VOStatus.UPDATED);
				newManageVO.setChangeBb1s(oldManageVO.getChangeBb1s());
				newManageVO.setExpBb3s(oldManageVO.getExpBb3s());
				newManageVO.setManageExecs(oldManageVO.getManageExecs());
				newManageVO.setMemoraBb2s(oldManageVO.getMemoraBb2s());
				newManageVO.setTermBb4s(oldManageVO.getTermBb4s());

				int changeCount = newManageVO.getChildrenVO().length;
				for (int i = 0; i < changeCount; i++) {
					// 记录的状态为2时，表示是增加的记录,那么就必须给pk_ct_manage赋值
					if (newManageVO.getChildrenVO()[i].getStatus() == 2) {
						((ManageItemVO) newManageVO.getChildrenVO()[i])
								.setPk_ct_manage(newManageVO.getParentVO()
										.getPrimaryKey().toString());
						// added by lirr 2009-11-2下午12:17:42 没有公司信息 后台无法获得oid
						((ManageItemVO) newManageVO.getChildrenVO()[i]).setPk_corp(m_sPkCorp);
						
					} else
						continue;
				}

				newManageVO.getParentVO().setAttributeValue("audiid", null);
				newManageVO.getParentVO().setAttributeValue("auditdate", null);
				newManageVO.getParentVO().setAttributeValue("coperatoridnow",
						m_sPkUser);
				newManageVO.setIsSaveCheck(true);

				// modified by lirr 2009-7-17 下午02:24:01 表头财 务本币累计收/付款总额
				// 改为表头财务原币累计收/付款总额
				setAraptotalgpamount(newManageVO);
				// added by lirr 2009-05-31 修改原因：oldVo从数据库中查询减少传输
				// oldManageVO = null;

				// added by lirr 20092009-6-18下午04:49:09
				ManageItemVO[] bakcurbodyvos = newManageVO.getItemVOs();
				ManageItemVO[] bakoldbodyvos = oldManageVO.getItemVOs();
				newManageVO.compressBodyWhenSave();
				oldManageVO.setTransNotFullVo();

				while (true) {
					try {
						newManageVO = (ManageVO) nc.ui.pub.pf.PfUtilClient
								.processAction("EDITBASE", m_sBillType,
										getClientEnvironment().getDate()
												.toString(), newManageVO,
										oldManageVO);

						break;

						// }

					}

					catch (OverMaxPriceException e) {
						if (showYesNoMessage(e.getMessage()) == MessageDialog.ID_YES) {
							newManageVO.setIsSaveCheck(false);
							continue;
						} else {
							m_bErrFlag = true;
							showHintMessage(nc.ui.ml.NCLangRes.getInstance()
									.getStrByID("4020pub", "UPP4020pub-000077")/*
																				 * @res
																				 * "保存失败！"
																				 */);
							return false;
						}
					} finally {
						// newManageVO.setChildrenVO(bakcurbodyvos);
						oldManageVO.setChildrenVO(bakoldbodyvos);
					}
				}

				newManageVO.setIsSaveCheck(true);

				// //回写表头时间戳
				// getCtBillCardPanel().setHeadItem("ts",
				// NewManageVO.getParentVO().getAttributeValue("ts"));
				// ManageItemVO[] itemVO = (ManageItemVO[])
				// NewManageVO.getChildrenVO();
				// for (int i = 0; i < itemVO.length; i++) {
				// //回写表体时间戳
				// getCtBillCardPanel().setBodyValueAt(itemVO[i].getTs(), i,
				// "ts");
				// }

				m_iBillState = OperationState.FREE;

				m_vBillVO.remove(m_iId - 1);

				// modified by liuzy 2007-12-04 碧桂园合同修改保存报已被其他人修改的错
				// 临时方案
				// nc.vo.pub.SuperVOUtil.execFormulaWithVOs(new
				// ManageHeaderVO[]{newManageVO.getParentVO()},
				// new
				// String[]{"ts->getColValue(ct_manage,ts,pk_ct_manage,pk_ct_manage)"},
				// null, getCtBillCardPanel().getFormulaParse());
				// nc.vo.pub.SuperVOUtil.execFormulaWithVOs(newManageVO.getChildrenVO(),
				// new
				// String[]{"ts->getColValue(ct_manage_b,ts,pk_ct_manage_b,pk_ct_manage_b)"},
				// null, getCtBillCardPanel().getFormulaParse());

				oldExtendManageVO.setParentVO(newManageVO.getParentVO());
				oldExtendManageVO.setTableVO("table", newManageVO
						.getChildrenVO());

				// 合同排序后修改，会出现串行问题 ，增加串行监听后 ，需要在此修改防止出现getCurVO()调用越界
				// 调换以下2句的顺序 qinchao lirr 2009-04-21 qinchao 越界
				m_vBillVO.insertElementAt(oldExtendManageVO, (m_iId - 1));
				getCtBillCardPanel().setBillValueVO(oldExtendManageVO);

				// getCtBillCardPanel().setBillValueVO(oldExtendManageVO);
				// m_vBillVO.insertElementAt(oldExtendManageVO, (m_iId - 1));

			}

			// 所有Table都需要重新载入数据
			for (int i = 1; i < 6; i++)
				m_bIsNeedReInit[i] = true;
			// 执行公式
			getCtBillCardPanel().getBillModel().execLoadFormula();
			getCtBillCardPanel().superupdateValue(); // 更新模板状态

			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"common", "UCH005")/* @res "保存成功" */);

			getCtBillCardPanel().setEnabled(false);
			m_bErrFlag = false;
			// getCtBillCardPanel().setHeadItem("ct_code",
			// NewManageVO.getParentVO().getAttributeValue("ct_code"));
			//			
			// // 最后修改人
			// getCtBillCardPanel().setTailItem("clastoperatorid", m_sPkUser);
			// getCtBillCardPanel().setTailItem("vlastoperatorname", m_sPkUser);
			// // 最后修改时间
			// getCtBillCardPanel().setTailItem("tlastmaketime",
			// NewManageVO.getParentVO().getAttributeValue("tlastmaketime"));
			//			

			m_timer.showExecuteTime("保存结束"); /*-=notranslate=-*/
			// added by lirr 2008-10-28 若CT002为真要自动触发审批流

			if (m_isAutoSendApprove) {
				int ctflag = getCurBillVO().getParentVO().getCtflag()
						.intValue();
				if (ctflag != BillState.CHECKGOING)
					onSavetoAudit(false);
			}

			// 合同基本保存后，如果m_copyedTermVOs不为空，则自动跳转合同条款页签
			// 合同条款同时复制到新合同中
			// 将此步放到这儿，是为了最大程度保证合同基本保存的独立性。
			copyTermtoNewCnt();
			/*
			 * if (m_isAutoSendApprove) { int ctflag =
			 * getCurBillVO().getParentVO().getCtflag() .intValue(); if (ctflag !=
			 * BillState.CHECKGOING) onSavetoAudit(); }
			 */

		} catch (ValidationException e) {
			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"SCMCOMMON", "UPPSCMCommon-000250")/* @res "输入错误" */);
			// 修改:e.getMessage()为Null的时候会抛空指针，因此加了一个限制条件e.getMessage()!=null
			// 修改人：张学坤
			// 修改日期：2007-10-31
			nc.vo.scm.pub.SCMEnv.error(e);
			if (e.getMessage() != null && e.getMessage().length() > 0)
				MessageDialog.showErrorDlg(this, null, e.getMessage());

			m_bErrFlag = true;
			return false;

		} catch (java.sql.SQLException e) {
			nc.vo.scm.pub.SCMEnv.error(e);
			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"4020pub", "UPP4020pub-000077")/* @res "保存失败！" */);

			if (e.getErrorCode() == 1) {
				MessageDialog.showErrorDlg(this, null, nc.ui.ml.NCLangRes
						.getInstance().getStrByID("4020pub",
								"UPP4020pub-000083")/*
													 * @res "不能保存！不能输入重复的合同编码！"
													 */);
			} else if (e.getErrorCode() == 1401) {
				MessageDialog.showErrorDlg(this, null, nc.ui.ml.NCLangRes
						.getInstance().getStrByID("4020pub",
								"UPP4020pub-000084")/*
													 * @res "输入非法的字符或太长的字符串！"
													 */);
			} else if (e.getErrorCode() == 1438) {
				MessageDialog.showErrorDlg(this, null, nc.ui.ml.NCLangRes
						.getInstance().getStrByID("SCMCOMMON",
								"UPPSCMCommon-000251")/* @res "输入的数字超出范围！" */
						+ "\n"
						+ nc.ui.ml.NCLangRes.getInstance().getStrByID(
								"SCMCOMMON", "UPPSCMCommon-000263")/*
																	 * @res
																	 * "税率：3位整数＋4位小数"
																	 */
						+ "\n"
						+ nc.ui.ml.NCLangRes.getInstance().getStrByID(
								"SCMCOMMON", "UPPSCMCommon-000064")/*
																	 * @res
																	 * "单价或金额：16位数"
																	 */
				);
			} else if (e.getErrorCode() == 2601 || e.getErrorCode() == 2627) {
				MessageDialog.showErrorDlg(this, null, nc.ui.ml.NCLangRes
						.getInstance().getStrByID("4020pub",
								"UPP4020pub-000083")/*
													 * @res "不能保存！不能输入重复的合同编码！"
													 */);
			} else if (e.getErrorCode() == 8152) {
				MessageDialog.showErrorDlg(this, null, nc.ui.ml.NCLangRes
						.getInstance().getStrByID("4020pub",
								"UPP4020pub-000084")/*
													 * @res "输入非法的字符或太长的字符串！"
													 */);
			} else if (e.getErrorCode() == 8115) {
				MessageDialog.showErrorDlg(this, null, nc.ui.ml.NCLangRes
						.getInstance().getStrByID("SCMCOMMON",
								"UPPSCMCommon-000251")/* @res "输入的数字超出范围！" */
						+ "\n"
						+ nc.ui.ml.NCLangRes.getInstance().getStrByID(
								"SCMCOMMON", "UPPSCMCommon-000263")/*
																	 * @res
																	 * "税率：3位整数＋4位小数"
																	 */
						+ "\n"
						+ nc.ui.ml.NCLangRes.getInstance().getStrByID(
								"SCMCOMMON", "UPPSCMCommon-000064")/*
																	 * @res
																	 * "单价或金额：16位数"
																	 */
				);
			} else
				MessageDialog.showErrorDlg(this, null, e.getMessage());

			m_bErrFlag = true;
			return false;

		} catch (java.rmi.RemoteException e) {
			nc.vo.scm.pub.SCMEnv.error(e);
			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"4020pub", "UPP4020pub-000077")/* @res "保存失败！" */);
			MessageDialog.showErrorDlg(this,
					nc.ui.ml.NCLangRes.getInstance().getStrByID("4020pub",
							"UPP4020pub-000067")/* @res "远程调用失败" */, e
							.getMessage());
			m_bErrFlag = true;
			return false;
		} catch (Exception e) {
			nc.vo.scm.pub.SCMEnv.error(e);
			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"4020pub", "UPP4020pub-000077")/* @res "保存失败！" */);
			m_bErrFlag = true;
			MessageDialog.showErrorDlg(this, null, e.getMessage());
			return false;
		} catch (Throwable e) {
			nc.vo.scm.pub.SCMEnv.error(e);
			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"4020pub", "UPP4020pub-000077")/* @res "保存失败！" */);
			MessageDialog.showErrorDlg(this, null, e.getMessage());
			m_bErrFlag = true;
			return false;
		}
		return true;
	}

	/**
	 * @author shaobing 合同基本保存后，如果m_copyedTermVOs不为空，则自动跳转合同条款页签 合同条款同时复制到新合同中
	 */
	private void copyTermtoNewCnt() {
		if (m_copyedTermVOs != null) {

			// 将页签转移到"合同条款"上.
			getCtBillCardPanel().getBodyTabbedPane().setSelectedIndex(
					TabState.TERM);
			m_iTabbedFlag = TabState.TERM;

			getCtBillCardPanel().getBillModel("term").setBodyDataVO(
					m_copyedTermVOs);

			// 将页签状态置为自由.
			m_iTbState = OperationState.EDIT;
			getCtBillCardPanel().getBillModel("term").setEnabled(true);

			m_copyedTermVOs = null;

		}

	}

	/**
	 * 此处插入方法说明。 合同大事记新增,修改,删除保存. 创建日期：(2001-9-19 14:45:51) 修改日期: (2004-04-02)
	 * 此方法修改后,合同大事记的保存不再根据行状态来分别调用后台的方法. 而是在后台根据行状态来选择批量处理的方法,如:批新增,批删除,批修改.
	 * 修改人:cqw
	 */
	private boolean saveNotes() {
		try {
			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"SCMCOMMON", "UPPSCMCommon-000134")/* @res "开始保存……" */);
			// 从界面获得修改后的数据
			MemoraBb2VO[] aryChange = (MemoraBb2VO[]) (getCtBillCardPanel()
					.getBillModel("note")
					.getBodyValueChangeVOs(MemoraBb2VO.class.getName()));
			// 从界面获得目前界面上的数据
			MemoraBb2VO[] aryNew = (MemoraBb2VO[]) (getCtBillCardPanel()
					.getBillModel("note").getBodyValueVOs(MemoraBb2VO.class
					.getName()));
			// 需要传送到后台的VO数组:
			MemoraBb2VO[] arySave = null;
			// 将删除的行追加
			Vector vDel = new Vector();
			if (aryChange != null && aryChange.length > 0) {
				for (int i = 0; i < aryChange.length; i++) {
					if (aryChange[i].getStatus() == VOStatus.DELETED)
						vDel.add(aryChange[i]);
				}
			}
			if (vDel.size() > 0) {
				arySave = new MemoraBb2VO[vDel.size() + aryNew.length];
				for (int i = 0; i < aryNew.length; i++)
					arySave[i] = aryNew[i];
				for (int j = 0; j < vDel.size(); j++)
					arySave[j + aryNew.length] = (MemoraBb2VO) vDel.get(j);
			} else
				arySave = aryNew;

			// 是否包含新增行,如果包含,那么将公司和表头PK置入.
			if (arySave.length > 0) {
				int iLen = arySave.length;
				for (int i = 0; i < iLen; i++) {
					arySave[i].validate();
					if (arySave[i].getStatus() == VOStatus.NEW) {
						arySave[i].setPk_corp(m_sPkCorp);
						arySave[i].setPk_ct_manage(((ExtendManageVO) m_vBillVO
								.elementAt(m_iId - 1)).getParentVO()
								.getPrimaryKey().toString());
					}
					// 记录原始位置
					arySave[i].setPosition(new Integer(i));
				}
			}
			// 插入数据库.
			if (arySave == null || arySave.length <= 0 || arySave[0] == null) {
				throw new ValidationException(nc.ui.ml.NCLangRes.getInstance()
						.getStrByID("4020pub", "UPP4020pub-000079")/*
																	 * @res
																	 * "没有数据需要保存."
																	 */);
			}
			ManageVO manageVO = new ManageVO();
			manageVO.setMemoraBb2s(arySave);
			manageVO.setParentVO(m_vBillVO.elementAt(m_iId - 1).getParentVO());
			manageVO.getParentVO().setAttributeValue("coperatoridnow",m_sPkUser);
			ManageVO alRet = null;
			alRet = (ManageVO) nc.ui.pub.pf.PfUtilClient
			.processAction("SAVENOTE", m_sBillType,
					getClientEnvironment().getDate()
							.toString(), manageVO, null);
			//aryChange = ContractWriterHelper.saveMemoras(arySave, m_sPkUser);
			getCtBillCardPanel().setHeadItem("ts", alRet.getParentVO().getAttributeValue("ts"));
			((ExtendManageVO) m_vBillVO.get(m_iId - 1)).getParentVO().setAttributeValue("ts", alRet.getParentVO().getAttributeValue("ts"));
			// 回写Model.
			getCtBillCardPanel().getBillModel("note").setBodyDataVO(alRet.getMemoraBb2s());
			// 回写m_vBillVO.
			((ExtendManageVO) m_vBillVO.get(m_iId - 1)).setTableVO("note",alRet.getMemoraBb2s());
			// 更新表体状态.
			getCtBillCardPanel().getBillModel("note").updateValue();
			// 将页签状态置为自由.
			m_iTbState = OperationState.FREE;
			// 卡片模板不可编辑.
			getCtBillCardPanel().setEnabled(false);

			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"SCMCOMMON", "UPPSCMCommon-000006")/* @res "保存成功" */);

		} catch (ValidationException e) {
			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"4020pub", "UPP4020pub-000076")/* @res "输入错误！" */);
			MessageDialog.showErrorDlg(this, null, e.getMessage());
			m_bErrFlag = true;
			return false;
		} catch (java.sql.SQLException e) {
			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"4020pub", "UPP4020pub-000077")/* @res "保存失败！" */);
			if (e.getErrorCode() == 1401) {
				MessageDialog.showErrorDlg(this, null, nc.ui.ml.NCLangRes
						.getInstance().getStrByID("4020pub",
								"UPP4020pub-000078")/*
													 * @res "输入非法的字符或输入了太长的字符串！"
													 */);
			} else if (e.getErrorCode() == 8152) {
				MessageDialog.showErrorDlg(this, null, nc.ui.ml.NCLangRes
						.getInstance().getStrByID("4020pub",
								"UPP4020pub-000078")/*
													 * @res "输入非法的字符或输入了太长的字符串！"
													 */);
			} else
				MessageDialog.showErrorDlg(this, null, e.getMessage());

			m_bErrFlag = true;
			return false;
		} catch (java.rmi.RemoteException e) {
			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"4020pub", "UPP4020pub-000077")/* @res "保存失败！" */);
			MessageDialog.showErrorDlg(this,
					nc.ui.ml.NCLangRes.getInstance().getStrByID("4020pub",
							"UPP4020pub-000067")/* @res "远程调用失败" */, e
							.getMessage());
			m_bErrFlag = true;
			return false;
		} catch (Exception e) {
			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"4020pub", "UPP4020pub-000077")/* @res "保存失败！" */);
			MessageDialog.showErrorDlg(this, null, e.getMessage());
			reportException(e);
			m_bErrFlag = true;
			return false;
		}
		return true;
	}

	/**
	 * 此处插入方法说明。 合同条款新增,修改,删除保存. 创建日期：(2001-9-19 14:45:51) 修改日期: (2004-04-02)
	 * 此方法修改后,合同条款的保存不再根据行状态来分别调用后台的方法. 而是在后台根据行状态来选择批量处理的方法,如:批新增,批删除,批修改.
	 * 修改人:cqw
	 */
	private boolean saveTerm() {
		try {
			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"SCMCOMMON", "UPPSCMCommon-000134")/* @res "开始保存……" */);

			// 从界面获得修改后的数据
			TermBb4VO[] aryChange = (TermBb4VO[]) (getCtBillCardPanel()
					.getBillModel("term").getBodyValueChangeVOs(TermBb4VO.class
					.getName()));
			// 从界面获得目前界面上的数据
			TermBb4VO[] aryNew = (TermBb4VO[]) (getCtBillCardPanel()
					.getBillModel("term").getBodyValueVOs(TermBb4VO.class
					.getName()));
			// 需要传送到后台的VO数组:
			TermBb4VO[] arySave = null;
			// 将删除的行追加
			Vector vDel = new Vector();
			if (aryChange != null && aryChange.length > 0) {
				for (int i = 0; i < aryChange.length; i++) {
					if (aryChange[i].getStatus() == VOStatus.DELETED)
						vDel.add(aryChange[i]);
				}
			}
			if (vDel.size() > 0) {
				arySave = new TermBb4VO[vDel.size() + aryNew.length];
				for (int i = 0; i < aryNew.length; i++)
					arySave[i] = aryNew[i];
				for (int j = 0; j < vDel.size(); j++)
					arySave[j + aryNew.length] = (TermBb4VO) vDel.get(j);
			} else
				arySave = aryNew;

			// 是否包含新增行,如果包含,那么将公司和表头PK置入.
			if (arySave.length > 0) {
				int iLen = arySave.length;
				for (int i = 0; i < iLen; i++) {
					arySave[i].validate();
					if (arySave[i].getStatus() == VOStatus.NEW) {
						arySave[i].setPk_corp(m_sPkCorp);
						arySave[i].setPk_ct_manage(((ExtendManageVO) m_vBillVO
								.elementAt(m_iId - 1)).getParentVO()
								.getPrimaryKey().toString());
					}
					// 记录原始位置
					arySave[i].setPosition(new Integer(i));
				}
			}
			// 插入数据库.
			if (arySave == null || arySave.length <= 0 || arySave[0] == null) {
				throw new ValidationException(nc.ui.ml.NCLangRes.getInstance()
						.getStrByID("4020pub", "UPP4020pub-000079")/*
																	 * @res
																	 * "没有数据需要保存."
																	 */);
			}
			ManageVO manageVO = new ManageVO();
			manageVO.setTermBb4s(arySave);
			manageVO.setParentVO(m_vBillVO.elementAt(m_iId - 1).getParentVO());
			manageVO.getParentVO().setAttributeValue("coperatoridnow",m_sPkUser);
			ManageVO alRet = null;
			alRet = (ManageVO) nc.ui.pub.pf.PfUtilClient
			.processAction("SAVETERM", m_sBillType,
					getClientEnvironment().getDate()
							.toString(), manageVO, null);
			
			//aryChange = ContractWriterHelper.saveTermBb4s(arySave, m_sPkUser);
			// 回写Model.
			getCtBillCardPanel().setHeadItem("ts", alRet.getParentVO().getAttributeValue("ts"));
			((ExtendManageVO) m_vBillVO.get(m_iId - 1)).getParentVO().setAttributeValue("ts", alRet.getParentVO().getAttributeValue("ts"));
			// 回写m_vBillVO.
			((ExtendManageVO) m_vBillVO.get(m_iId - 1)).setTableVO("term",
					alRet.getTermBb4s());
			
			getCtBillCardPanel().getBillModel("term").setBodyDataVO(alRet.getTermBb4s());
			// 回写m_vBillVO.
			((ExtendManageVO) m_vBillVO.get(m_iId - 1)).setTableVO("term",
					alRet.getTermBb4s());
			// 更新表体状态.
			getCtBillCardPanel().getBillModel("term").updateValue();
			// 将页签状态置为自由.
			m_iTbState = OperationState.FREE;
			// 卡片模板不可编辑.
			getCtBillCardPanel().setEnabled(false);

			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"SCMCOMMON", "UPPSCMCommon-000006")/* @res "保存成功" */);
		} catch (ValidationException e) {
			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"4020pub", "UPP4020pub-000076")/* @res "输入错误！" */);
			MessageDialog.showErrorDlg(this, null, e.getMessage());
			m_bErrFlag = true;
			return false;
		} catch (java.sql.SQLException e) {
			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"4020pub", "UPP4020pub-000077")/* @res "保存失败！" */);

			if (e.getErrorCode() == 1401) {
				MessageDialog.showErrorDlg(this, null, nc.ui.ml.NCLangRes
						.getInstance().getStrByID("4020pub",
								"UPP4020pub-000078")/*
													 * @res "输入非法的字符或输入了太长的字符串！"
													 */);
			} else if (e.getErrorCode() == 8152) {
				MessageDialog.showErrorDlg(this, null, nc.ui.ml.NCLangRes
						.getInstance().getStrByID("4020pub",
								"UPP4020pub-000078")/*
													 * @res "输入非法的字符或输入了太长的字符串！"
													 */);
			} else
				MessageDialog.showErrorDlg(this, null, e.getMessage());

			m_bErrFlag = true;
			return false;
		} catch (java.rmi.RemoteException e) {
			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"4020pub", "UPP4020pub-000077")/* @res "保存失败！" */);
			MessageDialog.showErrorDlg(this,
					nc.ui.ml.NCLangRes.getInstance().getStrByID("4020pub",
							"UPP4020pub-000067")/* @res "远程调用失败" */, e
							.getMessage());
			m_bErrFlag = true;
			return false;
		} catch (Exception e) {
			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"4020pub", "UPP4020pub-000077")/* @res "保存失败！" */);
			MessageDialog.showErrorDlg(this, null, e.getMessage());
			reportException(e);
			m_bErrFlag = true;
			return false;
		}
		return true;
	}

	/**
	 * 此处插入方法说明。 创建日期：(2001-12-10 19:19:14)
	 */
	private void setBackWhere() {
		((UIRefPane) getCtBillCardPanel().getHeadItem("pername").getComponent())
				.setWhereString(m_sPerWhereSql);
		((UIRefPane) getCtBillCardPanel().getHeadItem("deliaddrname")
				.getComponent()).setWhereString(null);
	}

	/**
	 * 功能：单据类型赋值 参数： 返回： 例外： 日期：(2002-8-29 10:43:43) 修改日期，修改人，修改原因，注释标志：
	 */
	protected void setBillType(String sBillType) {
		m_sBillType = sBillType;
	}

	/**
	 * 此处插入方法说明。 创建日期：(2001-9-7 16:52:51)
	 */
	private void setBodyListData(int row) {// modified by lirr 2008-07-24
		// 增加m_bAddFromBillFlag的判断

		// ExtendManageVO mVO = (ExtendManageVO) m_vBillVO.elementAt(row);
		ExtendManageVO mVO = null;
		if (!m_bAddFromBillFlag) {
			mVO = (ExtendManageVO) m_vBillVO.elementAt(row);
		} else {
			mVO = (ExtendManageVO) m_vBillVOForRef.elementAt(row);

		}
		String currid = ((ManageHeaderVO) mVO.getParentVO()).getCurrid();
		// int childNum= mVO.getChildrenVO().length;

		// 获得此币种的小数位数
		// 获得此币种的小数位数
		int currDigit;
		try {
			Integer integer = (Integer) m_hCurrDigit.get(currid);
			if (integer != null)
				currDigit = integer.intValue();
			else
				currDigit = 2;
		} catch (Exception e) {
			currDigit = 2;
		}
		// 定义原币金额小数位数
		getCtBillListPanel().getBodyItem(CTTableCode.BASE, "orisum")
				.setDecimalDigits(currDigit);
		getCtBillListPanel().getBodyItem(CTTableCode.BASE, "oritaxmny")
				.setDecimalDigits(currDigit);
		getCtBillListPanel().getBodyItem(CTTableCode.BASE, "oritaxsummny")
				.setDecimalDigits(currDigit);
		// added by lirr 2009-7-28 下午03:27:40 累计原币（应）收付款金额精度
		if (null != getCtBillListPanel().getBodyItem(CTTableCode.BASE,
				"noritotalgpmny"))
			getCtBillListPanel()
					.getBodyItem(CTTableCode.BASE, "noritotalgpmny")
					.setDecimalDigits(currDigit);
		if (null != getCtBillListPanel().getBodyItem(CTTableCode.BASE,
				"noritotalshgpmny"))
			getCtBillListPanel().getBodyItem(CTTableCode.BASE,
					"noritotalshgpmny").setDecimalDigits(currDigit);// end

		getCtBillListPanel().setBodyValueVO("table", mVO.getTableVO("table"));
		getCtBillListPanel().setBodyValueVO("term", mVO.getTableVO("term"));
		getCtBillListPanel().setBodyValueVO("cost", mVO.getTableVO("cost"));
		getCtBillListPanel().setBodyValueVO("note", mVO.getTableVO("note"));
		getCtBillListPanel().setBodyValueVO("history",
				mVO.getTableVO("history"));
		getCtBillListPanel().setBodyValueVO("exec", mVO.getTableVO("exec"));
		getCtBillListPanel().getBodyBillModel().execLoadFormula();

	}

	/**
	 * 此处插入方法说明。 创建日期：(2001-9-1 19:01:18)
	 */
	public void setButtonState() {

		// 合同状态
		int iState = -1;

		switch (m_iTabbedFlag) {
		case 0:

			if (m_iBillState == OperationState.FREE) {
				iState = OperationState.FREE;
				if (m_iId == 0) {
					// 当前没有一条单据时，设置其他的页签不可编辑，否则都可编辑
					getButtonTree().getButton(CTButtonConst.BTN_CTOTHER_TERM)
							.setEnabled(false);
					getButtonTree().getButton(CTButtonConst.BTN_CTOTHER_NOTE)
							.setEnabled(false);
					getButtonTree().getButton(CTButtonConst.BTN_CTOTHER_EXP)
							.setEnabled(false);
					getButtonTree().getButton(
							CTButtonConst.BTN_CTOTHER_CHANGEHISTORY)
							.setEnabled(false);
					getButtonTree().getButton(
							CTButtonConst.BTN_CTOTHER_CHANGEHISTORY)
							.setEnabled(false);

					getButtonTree().getButton(CTButtonConst.BTN_BILL_EDIT)
							.setEnabled(false);
					getButtonTree().getButton(CTButtonConst.BTN_BILL_DELETE)
							.setEnabled(false);
					getButtonTree().getButton(CTButtonConst.BTN_BILL_COPY)
							.setEnabled(false);
					getButtonTree()
							.getButton(CTButtonConst.BTN_BROWSE_PREVIOUS)
							.setEnabled(false);
					getButtonTree().getButton(CTButtonConst.BTN_BROWSE_NEXT)
							.setEnabled(false);
					getButtonTree().getButton(CTButtonConst.BTN_EXECUTE)
							.setEnabled(false);
					getButtonTree().getButton(CTButtonConst.BTN_PRINT_PRINT)
							.setEnabled(false);
					getButtonTree().getButton(CTButtonConst.BTN_PRINT_PREVIEW)
							.setEnabled(false);
					getButtonTree().getButton(
							CTButtonConst.BTN_EXECUTE_SENDAPPRPVE).setEnabled(
							false);
					getButtonTree().getButton(CTButtonConst.BTN_SWITCH)
							.setEnabled(false);
					getButtonTree().getButton(
							CTButtonConst.BTN_ASSIST_FUNC_DOCUMENT).setEnabled(
							false);

					getButtonTree().getButton(CTButtonConst.BTN_CTOTHER_EXEC)
							.setEnabled(false);

					getButtonTree().getButton(CTButtonConst.BTN_AUDIT)
							.setEnabled(false);
					getButtonTree().getButton(CTButtonConst.BTN_PAY)
							.setEnabled(false);
					getButtonTree().getButton(CTButtonConst.BTN_REC)
							.setEnabled(false);
					getButtonTree().getButton(CTButtonConst.BTN_PAYORREC)
							.setEnabled(false);
				} else {
					getButtonTree().getButton(CTButtonConst.BTN_CTOTHER_TERM)
							.setEnabled(true);
					getButtonTree().getButton(CTButtonConst.BTN_CTOTHER_NOTE)
							.setEnabled(true);
					getButtonTree().getButton(CTButtonConst.BTN_CTOTHER_EXP)
							.setEnabled(true);
					getButtonTree().getButton(
							CTButtonConst.BTN_CTOTHER_CHANGEHISTORY)
							.setEnabled(true);
					getButtonTree().getButton(
							CTButtonConst.BTN_CTOTHER_CHANGEHISTORY)
							.setEnabled(true);

					getButtonTree().getButton(CTButtonConst.BTN_BILL_EDIT)
							.setEnabled(true);
					getButtonTree().getButton(CTButtonConst.BTN_BILL_DELETE)
							.setEnabled(true);
					getButtonTree().getButton(CTButtonConst.BTN_BILL_COPY)
							.setEnabled(true);
					getButtonTree().getButton(CTButtonConst.BTN_EXECUTE)
							.setEnabled(true);

					getButtonTree().getButton(CTButtonConst.BTN_CTOTHER_EXEC)
							.setEnabled(true);

					getButtonTree().getButton(CTButtonConst.BTN_AUDIT)
							.setEnabled(true);

					getButtonTree().getButton(CTButtonConst.BTN_SAVE)
							.setEnabled(false);

					if (m_bIsCard) { // 在卡片模式下
						if (m_iId == 1)
							getButtonTree().getButton(
									CTButtonConst.BTN_BROWSE_PREVIOUS)
									.setEnabled(false);
						/*
						 * else if (m_iId != 0) getButtonTree().getButton(
						 * CTButtonConst.BTN_BROWSE_PREVIOUS) .setEnabled(true);
						 */
						else if (m_iId != 0) {
							getButtonTree().getButton(
									CTButtonConst.BTN_BROWSE_PREVIOUS)
									.setEnabled(true);
							/*
							 * if (ctflag == BillState.VALIDATE && ctflag !=
							 * BillState.TERMINATE && m_iBillState ==
							 * OperationState.FREE) {
							 * getButtonTree().getButton(CTButtonConst.BTN_PAY)
							 * .setEnabled(true);
							 * getButtonTree().getButton(CTButtonConst.BTN_REC)
							 * .setEnabled(true); getButtonTree().getButton(
							 * CTButtonConst.BTN_PAYORREC) .setEnabled(true); }
							 * else {
							 * getButtonTree().getButton(CTButtonConst.BTN_PAY)
							 * .setEnabled(false);
							 * getButtonTree().getButton(CTButtonConst.BTN_REC)
							 * .setEnabled(false);
							 * getButtonTree().getButton(CTButtonConst.BTN_PAYORREC)
							 * .setEnabled(false); }
							 */

						}

						if (m_iId == m_iElementsNum)
							getButtonTree().getButton(
									CTButtonConst.BTN_BROWSE_NEXT).setEnabled(
									false);
						else
							getButtonTree().getButton(
									CTButtonConst.BTN_BROWSE_NEXT).setEnabled(
									true);
						getButtonTree()
								.getButton(CTButtonConst.BTN_BROWSE_NEXT)
								.setEnabled(true);
						/*
						 * getButtonTree().getButton(CTButtonConst.BTN_PAY)
						 * .setEnabled(true);
						 * getButtonTree().getButton(CTButtonConst.BTN_REC)
						 * .setEnabled(true);
						 * getButtonTree().getButton(CTButtonConst.BTN_PAYORREC)
						 * .setEnabled(true);
						 */
					} else { // 在列表模式下不需要上下按钮
						getButtonTree().getButton(
								CTButtonConst.BTN_BROWSE_PREVIOUS).setEnabled(
								false);
						getButtonTree()
								.getButton(CTButtonConst.BTN_BROWSE_NEXT)
								.setEnabled(false);
						getButtonTree().getButton(CTButtonConst.BTN_PAY)
								.setEnabled(false);
						getButtonTree().getButton(CTButtonConst.BTN_REC)
								.setEnabled(false);
						getButtonTree().getButton(CTButtonConst.BTN_PAYORREC)
								.setEnabled(false);
					}

					getButtonTree().getButton(CTButtonConst.BTN_PRINT_PRINT)
							.setEnabled(true);
					getButtonTree().getButton(CTButtonConst.BTN_PRINT_PREVIEW)
							.setEnabled(true);
					getButtonTree().getButton(
							CTButtonConst.BTN_EXECUTE_SENDAPPRPVE).setEnabled(
							true);
					getButtonTree().getButton(
							CTButtonConst.BTN_ASSIST_FUNC_DOCUMENT).setEnabled(
							true);
					getButtonTree().getButton(CTButtonConst.BTN_SWITCH)
							.setEnabled(true);
				}

				getButtonTree().getButton(CTButtonConst.BTN_BILL).setEnabled(
						true);
				getButtonTree().getButton(CTButtonConst.BTN_ADD).setEnabled(
						true);
				getButtonTree().getButton(CTButtonConst.BTN_BILL_CANCEL)
						.setEnabled(false);
				getButtonTree().getButton(CTButtonConst.BTN_QUERY).setEnabled(
						true);

				getButtonTree().getButton(CTButtonConst.BTN_SAVE).setEnabled(
						false);
				getButtonTree().getButton(CTButtonConst.BTN_LINE).setEnabled(
						false);

				// 设置刷新按钮状态[V2.3]
				if (m_sQryCondition == null || m_sQryCondition.length() <= 0)
					getButtonTree().getButton(CTButtonConst.BTN_BROWSE_REFRESH)
							.setEnabled(false);
				else
					getButtonTree().getButton(CTButtonConst.BTN_BROWSE_REFRESH)
							.setEnabled(true);
				// getButtonTree().getButton(CTButtonConst.BTN_AUDIT).setEnabled(
				// true);
				// added by lirr 2008-10-27
				getButtonTree().getButton(CTButtonConst.BTN_ASSIST_QUERY)
						.setEnabled(true);
				getButtonTree().getButton(CTButtonConst.BTN_ASSIST_FUNC)
						.setEnabled(true);
			} else if (m_iBillState == OperationState.ADD) {
				iState = OperationState.ADD;
				getButtonTree().getButton(CTButtonConst.BTN_AUDIT).setEnabled(
						false);
				getButtonTree().getButton(CTButtonConst.BTN_CTOTHER_TERM)
						.setEnabled(false);
				getButtonTree().getButton(CTButtonConst.BTN_CTOTHER_NOTE)
						.setEnabled(false);
				getButtonTree().getButton(CTButtonConst.BTN_CTOTHER_EXP)
						.setEnabled(false);
				getButtonTree().getButton(
						CTButtonConst.BTN_CTOTHER_CHANGEHISTORY).setEnabled(
						false);
				getButtonTree().getButton(
						CTButtonConst.BTN_CTOTHER_CHANGEHISTORY).setEnabled(
						false);

				// getButtonTree().getButton(CTButtonConst.BTN_BILL).setEnabled(false);
				getButtonTree().getButton(CTButtonConst.BTN_BILL).setEnabled(
						true);
				getButtonTree().getButton(CTButtonConst.BTN_ADD).setEnabled(
						false);
				getButtonTree().getButton(CTButtonConst.BTN_BILL_EDIT)
						.setEnabled(false);
				getButtonTree().getButton(CTButtonConst.BTN_BILL_COPY)
						.setEnabled(false);
				getButtonTree().getButton(CTButtonConst.BTN_BILL_DELETE)
						.setEnabled(false);
				getButtonTree().getButton(CTButtonConst.BTN_SAVE).setEnabled(
						true);
				getButtonTree().getButton(CTButtonConst.BTN_BILL_CANCEL)
						.setEnabled(true);
				getButtonTree().getButton(CTButtonConst.BTN_BILL_DELETE)
						.setEnabled(false);
				if (m_bIsCard) // 是卡片模式
					getButtonTree().getButton(CTButtonConst.BTN_LINE)
							.setEnabled(true);
				else
					getButtonTree().getButton(CTButtonConst.BTN_LINE)
							.setEnabled(false);

				getButtonTree().getButton(CTButtonConst.BTN_PRINT_PRINT)
						.setEnabled(false);
				getButtonTree().getButton(CTButtonConst.BTN_PRINT_PREVIEW)
						.setEnabled(false);
				getButtonTree()
						.getButton(CTButtonConst.BTN_EXECUTE_SENDAPPRPVE)
						.setEnabled(true);

				getButtonTree().getButton(CTButtonConst.BTN_BROWSE_PREVIOUS)
						.setEnabled(false);
				getButtonTree().getButton(CTButtonConst.BTN_BROWSE_NEXT)
						.setEnabled(false);
				getButtonTree().getButton(CTButtonConst.BTN_EXECUTE)
						.setEnabled(false);

				getButtonTree().getButton(CTButtonConst.BTN_SWITCH).setEnabled(
						false);
				getButtonTree().getButton(CTButtonConst.BTN_QUERY).setEnabled(
						false);
				getButtonTree().getButton(
						CTButtonConst.BTN_ASSIST_FUNC_DOCUMENT).setEnabled(
						false);
				getButtonTree().getButton(CTButtonConst.BTN_BROWSE_REFRESH)
						.setEnabled(false);

				// added by lirr 2008-09-10
				getButtonTree().getButton(CTButtonConst.BTN_PAY).setEnabled(
						false);
				getButtonTree().getButton(CTButtonConst.BTN_REC).setEnabled(
						false);
				getButtonTree().getButton(CTButtonConst.BTN_PAYORREC)
						.setEnabled(false);
				// added by lirr 2008-10-27
				getButtonTree().getButton(CTButtonConst.BTN_ASSIST_QUERY)
						.setEnabled(true);
				getButtonTree().getButton(CTButtonConst.BTN_ASSIST_FUNC)
						.setEnabled(true);
			} else if (m_iBillState == OperationState.EDIT) {
				iState = OperationState.EDIT;
				getButtonTree().getButton(CTButtonConst.BTN_AUDIT).setEnabled(
						false);
				getButtonTree().getButton(CTButtonConst.BTN_BILL).setEnabled(
						true);
				getButtonTree().getButton(CTButtonConst.BTN_BILL_EDIT)
						.setEnabled(false);
				getButtonTree().getButton(CTButtonConst.BTN_ADD).setEnabled(
						false);
				getButtonTree().getButton(CTButtonConst.BTN_BILL_DELETE)
						.setEnabled(false);
				getButtonTree().getButton(CTButtonConst.BTN_BILL_COPY)
						.setEnabled(false);
				getButtonTree().getButton(CTButtonConst.BTN_BILL_CANCEL)
						.setEnabled(true);
				if (m_bIsCard)
					getButtonTree().getButton(CTButtonConst.BTN_LINE)
							.setEnabled(true);
				else
					getButtonTree().getButton(CTButtonConst.BTN_LINE)
							.setEnabled(false);

				getButtonTree().getButton(CTButtonConst.BTN_SAVE).setEnabled(
						true);
				getButtonTree().getButton(CTButtonConst.BTN_BROWSE_PREVIOUS)
						.setEnabled(false);
				getButtonTree().getButton(CTButtonConst.BTN_BROWSE_NEXT)
						.setEnabled(false);
				getButtonTree().getButton(CTButtonConst.BTN_EXECUTE)
						.setEnabled(false);

				getButtonTree().getButton(CTButtonConst.BTN_PRINT_PRINT)
						.setEnabled(false);
				getButtonTree().getButton(CTButtonConst.BTN_PRINT_PREVIEW)
						.setEnabled(false);
				getButtonTree()
						.getButton(CTButtonConst.BTN_EXECUTE_SENDAPPRPVE)
						.setEnabled(true);

				getButtonTree().getButton(CTButtonConst.BTN_SWITCH).setEnabled(
						false);
				getButtonTree().getButton(CTButtonConst.BTN_QUERY).setEnabled(
						false);
				getButtonTree().getButton(
						CTButtonConst.BTN_ASSIST_FUNC_DOCUMENT).setEnabled(
						false);
				getButtonTree().getButton(CTButtonConst.BTN_BROWSE_REFRESH)
						.setEnabled(false);
				// added by lirr 2008-09-10
				getButtonTree().getButton(CTButtonConst.BTN_PAY).setEnabled(
						false);
				getButtonTree().getButton(CTButtonConst.BTN_REC).setEnabled(
						false);
				getButtonTree().getButton(CTButtonConst.BTN_PAYORREC)
						.setEnabled(false);
				// added by lirr 2008-10-27
				getButtonTree().getButton(CTButtonConst.BTN_ASSIST_QUERY)
						.setEnabled(true);
				getButtonTree().getButton(CTButtonConst.BTN_ASSIST_FUNC)
						.setEnabled(true);
			} else if (m_iBillState == OperationState.CHANGE) {
				iState = OperationState.CHANGE;

				getButtonTree().getButton(CTButtonConst.BTN_BILL).setEnabled(
						true);
				getButtonTree().getButton(CTButtonConst.BTN_ADD).setEnabled(
						false);
				getButtonTree().getButton(CTButtonConst.BTN_BILL_EDIT)
						.setEnabled(false);
				getButtonTree().getButton(CTButtonConst.BTN_BILL_COPY)
						.setEnabled(false);
				getButtonTree().getButton(CTButtonConst.BTN_BILL_DELETE)
						.setEnabled(false);
				getButtonTree().getButton(CTButtonConst.BTN_BILL_CANCEL)
						.setEnabled(true);
				getButtonTree().getButton(CTButtonConst.BTN_LINE).setEnabled(
						true);
				getButtonTree().getButton(CTButtonConst.BTN_SAVE).setEnabled(
						true);
				getButtonTree().getButton(CTButtonConst.BTN_BROWSE_PREVIOUS)
						.setEnabled(false);
				getButtonTree().getButton(CTButtonConst.BTN_BROWSE_NEXT)
						.setEnabled(false);
				getButtonTree().getButton(CTButtonConst.BTN_EXECUTE)
						.setEnabled(false);

				getButtonTree().getButton(CTButtonConst.BTN_PRINT_PRINT)
						.setEnabled(false);
				getButtonTree().getButton(CTButtonConst.BTN_PRINT_PREVIEW)
						.setEnabled(false);
				getButtonTree()
						.getButton(CTButtonConst.BTN_EXECUTE_SENDAPPRPVE)
						.setEnabled(false);
				// added by lirr 2008-09-10
				getButtonTree().getButton(CTButtonConst.BTN_PAY).setEnabled(
						false);
				getButtonTree().getButton(CTButtonConst.BTN_REC).setEnabled(
						false);
				getButtonTree().getButton(CTButtonConst.BTN_PAYORREC)
						.setEnabled(false);
				// added by lirr 2008-10-27
				getButtonTree().getButton(CTButtonConst.BTN_ASSIST_QUERY)
						.setEnabled(true);
				getButtonTree().getButton(CTButtonConst.BTN_ASSIST_FUNC)
						.setEnabled(true);

				// addied by liuzy 2008-10-29 上午10:16:27
				getButtonTree().getButton(CTButtonConst.BTN_QUERY).setEnabled(
						false);
			}

			// 根据合同状态来确定合同修改，删除按钮的可用性
			if (m_iId > 0) { // 若有合同时
				int ctflag = ((ManageHeaderVO) ((ExtendManageVO) m_vBillVO
						.elementAt(m_iId - 1)).getParentVO()).getCtflag()
						.intValue();
				if (ctflag == BillState.TERMINATE || ctflag == BillState.FREEZE
						|| ctflag == BillState.VALIDATE
						|| ctflag == BillState.AUDIT
						|| ctflag == BillState.ABOLISH
				/* || (ctflag == BillState.CHECKGOING) */) {
					// 合同审核、生效、冻结、终止后都不能修改和删除
					getButtonTree().getButton(CTButtonConst.BTN_BILL_EDIT)
							.setEnabled(false);
					getButtonTree().getButton(CTButtonConst.BTN_BILL_DELETE)
							.setEnabled(false);
					// getTabbedPane().setEnabledAt(1,false); //审核后，条款不能更改
				}
				// modified by lirr 2008-11-27 对于审核中的单据如果有了审批人则不能再修改
				if (ctflag == BillState.CHECKGOING) {
					getButtonTree().getButton(CTButtonConst.BTN_BILL_DELETE)
							.setEnabled(false);
					// 修改人:wuweiping 修改时间:2009-10-19 下午03:38:30 修改原因: 对于审核中的单据如果有了审批人则不能再修改
					String key = getCtBillCardPanel().getTailItem("audiid").getValue();
					if(key!=null && !key.equals("")){
						getButtonTree().getButton(CTButtonConst.BTN_BILL_EDIT)
						.setEnabled(false);
					}
					/*String key =  getCtBillCardPanel().getTailItem("audiid")
					.getValue();
					if (null != getCtBillCardPanel().getTailItem("audiid")
							.getValue()) {
						getButtonTree().getButton(CTButtonConst.BTN_BILL_EDIT)
								.setEnabled(false);}*/
                      else {
						if (m_iBillState != OperationState.EDIT) {
							getButtonTree().getButton(
									CTButtonConst.BTN_BILL_EDIT).setEnabled(
									true);
						}
					}
					/*
					 * //added by lirr 2008-12-17 合同处于审核中 “弃审”按钮可用
					 * getButtonTree().getButton(CTButtonConst.BTN_EXECUTE_CANCELAPPRPVE)
					 * .setEnabled(true);
					 */
					// 逐级弃审 如果没有审核人则弃审按钮setEnabled false added by lirr
					// 2009-02-11
					if (!StringUtil
							.isEmptyWithTrim(((ManageHeaderVO) ((ExtendManageVO) m_vBillVO
									.elementAt(m_iId - 1)).getParentVO())
									.getAudiid())) {
						// added by lirr 2008-12-17 合同处于审核中 “弃审”按钮可用
						getButtonTree().getButton(
								CTButtonConst.BTN_EXECUTE_CANCELAPPRPVE)
								.setEnabled(true);
					}
				}

				if (ctflag != BillState.FREE)
					getButtonTree().getButton(
							CTButtonConst.BTN_EXECUTE_SENDAPPRPVE).setEnabled(
							false);
				// 当前登录人非制单人则不能送审
				if (!((ManageHeaderVO) ((ExtendManageVO) m_vBillVO
						.elementAt(m_iId - 1)).getParentVO()).getOperid()
						.equals(m_sPkUser)) {
					getButtonTree().getButton(
							CTButtonConst.BTN_EXECUTE_SENDAPPRPVE).setEnabled(
							false);
				}
				if (m_bIsCard) {
					if (ctflag == BillState.VALIDATE
							&& ctflag != BillState.TERMINATE
							&& m_iBillState == OperationState.FREE) {
						getButtonTree().getButton(CTButtonConst.BTN_PAY)
								.setEnabled(true);
						getButtonTree().getButton(CTButtonConst.BTN_REC)
								.setEnabled(true);
						getButtonTree().getButton(CTButtonConst.BTN_PAYORREC)
								.setEnabled(true);
					} else {
						getButtonTree().getButton(CTButtonConst.BTN_PAY)
								.setEnabled(false);
						getButtonTree().getButton(CTButtonConst.BTN_REC)
								.setEnabled(false);
						getButtonTree().getButton(CTButtonConst.BTN_PAYORREC)
								.setEnabled(false);
					}
				} else {
					getButtonTree().getButton(CTButtonConst.BTN_PAY)
							.setEnabled(false);
					getButtonTree().getButton(CTButtonConst.BTN_REC)
							.setEnabled(false);
					getButtonTree().getButton(CTButtonConst.BTN_PAYORREC)
							.setEnabled(false);
				}

			}

			// 设置合同状态控制的按钮状态
			// modify by liujq 2007-05-14
			// 只有当单据操作状态为FREE时，才根据合同控制状态去更新按钮
			if (m_iBillState == OperationState.FREE)
				setFlagControlStates();

			// 根据按钮的权限设置页签的状态[V2.3]
			setTabbedState(getButtonTree().getButton(
					CTButtonConst.BTN_CTOTHER_TERM).isEnabled(),
					getButtonTree().getButton(CTButtonConst.BTN_CTOTHER_NOTE)
							.isEnabled(), getButtonTree().getButton(
							CTButtonConst.BTN_CTOTHER_EXP).isEnabled(),
					getButtonTree().getButton(
							CTButtonConst.BTN_CTOTHER_CHANGEHISTORY)
							.isEnabled(), getButtonTree().getButton(
							CTButtonConst.BTN_CTOTHER_CHANGEHISTORY)
							.isEnabled());
			break;

		default: // 设置1，2，3，4页签的状态
			setTbButtonState();

		}
		// 扩展按钮默认加在合同基本页签之上,不用考虑其他页签的处理情况.
		// iState: 合同的状态.
		setExtendBtnsStat(iState);
		updateButtons();
	}

	/**
	 * 此方法用于查询后返回卡片模式下对单据卡片数据的初始化 创建日期：(2001-9-14 10:26:26)
	 */
	private void setCardVOData() {
		// id=1;
		m_iElementsNum = m_vBillVO.size();
		// 设置单据卡片VO
		getCtBillCardPanel().setBillValueVO(
				(ExtendManageVO) m_vBillVO.elementAt(m_iId - 1));
		getCtBillCardPanel().updateValue();
		// 合计行标记
		getCtBillCardPanel().getTotalTableModel().setValueAt(
				nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
						"UC000-0001146")/* @res "合计" */, 0, 0);
		// isNeedReInit = true;//改变单据，所有Table都需要重新载入数据
	}

	/**
	 * 此处插入方法说明。 创建日期：(2001-9-21 19:45:13)
	 */
	protected void setCtRef() {

		// 设置合同类型参照参数
		/*
		 * ((UIRefPane)
		 * getCtBillCardPanel().getHeadItem("ct_type").getComponent())
		 * .setWhereString("pk_corp = '" + m_sPkCorp + "' and nbusitype=" +
		 * m_iCtType);
		 * 
		 */
		if (true == m_bAddFromBillFlag
				&& (BillType.PURDAILY).equals(m_sBillType)) {

			((UIRefPane) getCtBillCardPanel().getHeadItem("ct_type")
					.getComponent()).setWhereString("pk_corp = '" + m_sPkCorp
					+ "' and nbusitype=" + m_iCtType + " and ninvctlstyle= "
					+ 0);
		} else {
			((UIRefPane) getCtBillCardPanel().getHeadItem("ct_type")
					.getComponent()).setWhereString("pk_corp = '" + m_sPkCorp
					+ "' and nbusitype=" + m_iCtType);
		}

		// 设置交货地点参照失去焦点时显示交货地点
		((UIRefPane) getCtBillCardPanel().getHeadItem("deliaddrname")
				.getComponent()).setReturnCode(true);

		((UIRefPane) getCtBillCardPanel().getHeadItem("projectname")
				.getComponent()).setWhereString("bd_jobmngfil.pk_corp = '"
				+ m_sPkCorp + "'");

		// 费用金额输入的控制
		UITextField cell = new UITextField();
		cell.setTextType("TextDbl");
		cell.setMaxLength(16);
		cell.setNumPoint(m_iMainCurrDigit);

	}

	/**
	 * 此处插入方法说明。 创建日期：(2001-10-23 17:24:19)
	 */
	public void setDifferButtons() {
		if (m_iTabbedFlag == TabState.BILL) {
			// setButtons(m_aryButtonGroup);
			changeToBlButn();
		} else {
			if (m_iTabbedOldFlag == TabState.BILL)
				// 如果前一个页签是单据页签，则按钮改动
				changeToTbButn();
			// setButtons(m_aryTbButtonGroup);

		}
		m_iTabbedOldFlag = m_iTabbedFlag;
	}

	/**
	 * 此处插入方法说明。 创建日期：(2001-12-5 22:09:16)
	 */
	public void setFlagControlStates() {

	}

	/**
	 * 此处插入方法说明。 创建日期：(2001-9-7 10:21:41)
	 */
	private void setHeaderListData() {

		ManageHeaderVO[] vListVO = new ManageHeaderVO[m_vBillVO.size()];
		if (m_vBillVO.size() != 0)
			for (int i = 0; i < m_vBillVO.size(); i++) {
				vListVO[i] = (ManageHeaderVO) ((ExtendManageVO) m_vBillVO
						.elementAt(i)).getParentVO();
			}
		
		getCtBillListPanel().setHeaderValueVO(vListVO);
		// 执行公式
		getCtBillListPanel().getHeadBillModel().execLoadFormula();

	}

	/**
	 * 此处插入方法说明。 创建日期：(2001-11-21 10:15:36)
	 */
	private void setIdtoName() {
		// 置回参照原来的where条件
		m_timer.start("开始执行setIdToname()"); /*-=notranslate=-*/
		setBackWhere();
		m_timer.showExecuteTime("setBackWhere()");

		// 表头公司
		getCtBillCardPanel().getHeadItem("unitname").setValue(
				getCtBillCardPanel().getHeadItem("pk_corp").getValueObject());

		getCtBillCardPanel().getHeadItem("projectname").setValue(
				getCtBillCardPanel().getHeadItem("projectid").getValueObject());

		getCtBillCardPanel().getHeadItem("ct_type")
				.setValue(
						getCtBillCardPanel().getHeadItem("pk_ct_type")
								.getValueObject());

		getCtBillCardPanel().getHeadItem("custname").setValue(
				getCtBillCardPanel().getHeadItem("custid").getValueObject());

		getCtBillCardPanel().getHeadItem("currname").setValue(
				getCtBillCardPanel().getHeadItem("currid").getValueObject());

		getCtBillCardPanel().getHeadItem("depname").setValue(
				getCtBillCardPanel().getHeadItem("depid").getValueObject());

		getCtBillCardPanel().getHeadItem("pername").setValue(
				getCtBillCardPanel().getHeadItem("personnelid")
						.getValueObject());

		getCtBillCardPanel().getHeadItem("transpmodename")
				.setValue(
						getCtBillCardPanel().getHeadItem("transpmode")
								.getValueObject());

		getCtBillCardPanel().getHeadItem("deliaddrname").setValue(
				getCtBillCardPanel().getHeadItem("deliaddr").getValueObject());

		getCtBillCardPanel().getHeadItem("paytermname").setValue(
				getCtBillCardPanel().getHeadItem("payterm").getValueObject());

		getCtBillCardPanel().getTailItem("opername").setValue(
				getCtBillCardPanel().getTailItem("operid").getValueObject());

		getCtBillCardPanel().getTailItem("audiname").setValue(
				getCtBillCardPanel().getTailItem("audiid").getValueObject());

		// 最后修改人
		getCtBillCardPanel().getTailItem("vlastoperatorname").setValue(
				getCtBillCardPanel().getTailItem("clastoperatorid")
						.getValueObject());
		// added by lirr 2009-05-25 复制后自定义项清空
		for (int i = 1; i <= 20; i++) {
			String strColName = "def" + i;
			String sVdefPkKey = "pk_defdoc"
					+ strColName.substring("def".length());
			if (null != getCtBillCardPanel().getHeadItem(sVdefPkKey)
					.getValueObject()
					&& !"".equals(getCtBillCardPanel().getHeadItem(sVdefPkKey)
							.getValueObject())) {
				((UIRefPane) getCtBillCardPanel().getHeadItem(strColName)
						.getComponent()).setPK(getCtBillCardPanel()
						.getHeadItem(sVdefPkKey).getValueObject().toString());

			}

		}
		m_timer.showExecuteTime("getCtBillCardPanel().getTailItem");
	}

	/**
	 * 功能说明：为单据列表设置汇率精度 参数： 返回： 例外： 日期：(2002-7-23 20:20:05) 作者：王亮 modified by
	 * lirr 修改原因：从请购单，价格审批单转单过来的合同 放在另一个缓存中 也需要进行精度设置
	 */
	private void setListRateDigit() {

		/*
		 * if (m_vBillVO.size() != 0) { String sCurrid[] = new
		 * String[m_vBillVO.size()]; for (int i = 0; i < m_vBillVO.size(); i++) {
		 * sCurrid[i] = ((ManageHeaderVO) ((ExtendManageVO) m_vBillVO
		 * .elementAt(i)).getParentVO()).getCurrid(); }
		 */
		if (m_vBillVO.size() != 0 || m_vBillVOForRef.size() != 0) {
			String sCurrid[] = null;
			if (m_vBillVO.size() != 0) {
				sCurrid = new String[m_vBillVO.size()];
				for (int i = 0; i < m_vBillVO.size(); i++) {

					sCurrid[i] = ((ManageHeaderVO) ((ExtendManageVO) m_vBillVO
							.elementAt(i)).getParentVO()).getCurrid();
				}
			} else {
				sCurrid = new String[m_vBillVOForRef.size()];
				for (int i = 0; i < m_vBillVOForRef.size(); i++) {
					sCurrid[i] = ((ManageHeaderVO) ((ExtendManageVO) m_vBillVOForRef
							.elementAt(i)).getParentVO()).getCurrid();
				}
			}

			int iRateDecimals[] = new int[sCurrid.length];
			int iAstRateDecimals[] = new int[sCurrid.length];
			for (int i = 0; i < sCurrid.length; i++) {
				/*
				 * int[] iTemp = nc.ui.scm.pub.PubSetUI.getBothExchRateDigit(
				 * m_sPkCorp, sCurrid[i]);
				 */
				// iRateDecimals[i] = iTemp[0];
				// modifeid by lirr 2008-12-24 精度
				iRateDecimals[i] = getCurrateDigit(sCurrid[i]);
				// iAstRateDecimals[i] = iTemp[1];
			}

			nc.ui.pub.bill.BillListData bd = getCtBillListPanel()
					.getBillListData();
			nc.ui.pub.bill.BillItem rateitem = bd.getHeadItem("currrate");
			if (null != rateitem) {// &&
				MyTableCellRenderer rateCellRenderer = new MyTableCellRenderer(
						rateitem/* , true, false */);
				rateCellRenderer.setPrecision(iRateDecimals);// 设置精度
				if (rateitem.isShow()) {
					javax.swing.table.TableColumn rateColumn = getCtBillListPanel()
							.getHeadTable().getColumn(rateitem.getName());
					rateColumn.setCellRenderer(rateCellRenderer);
				}
			}

			/*
			 * nc.ui.pub.bill.BillItem astRateitem =
			 * bd.getHeadItem("astcurrate"); if (null != astRateitem) {// &&
			 * MyTableCellRenderer AstRateCellRenderer = new
			 * MyTableCellRenderer( astRateitem , true, false );
			 * AstRateCellRenderer.setPrecision(iAstRateDecimals);// 设置精度 if
			 * (astRateitem.isShow()) { javax.swing.table.TableColumn
			 * astRateColumn = getCtBillListPanel()
			 * .getHeadTable().getColumn(astRateitem.getName());
			 * astRateColumn.setCellRenderer(AstRateCellRenderer); } }
			 */
		}

	}

	/**
	 * 此处插入方法说明。 创建日期：(2001-11-29 10:58:05) 修改说明：对已经审核或生效的合同进行合同条款的变更. 修改者：邵兵
	 * 修改时间：公元2005年4月8日
	 */
	private void setTbButonByflag() {
		if (m_iId > 0 && !m_bAddFromBillFlag) { // 若有合同时 modified by lirr
			// 2008-07-24 增加不是参照其他单据的
			int ctflag = Integer.parseInt(((ExtendManageVO) m_vBillVO
					.get(m_iId - 1)).getParentVO().getAttributeValue("ctflag")
					.toString());
			// 设置合同条款的增删改状态
			//if (m_iTabbedFlag == TabState.TERM
			if ((m_iTabbedFlag == TabState.TERM || m_iTabbedFlag == TabState.EXP)
					&& m_iTbState == OperationState.FREE) {
				if (ctflag == BillState.FREE /*|| ctflag == BillState.AUDIT
						|| ctflag == BillState.VALIDATE*/)
					setTbGroupState(false, false, true, false, false);
				//begin ncm  wangminp NC2014122600148_瓮福集团_2014-12-29_专
				/*
				 * 原始业务为自由状态，审核状态、
				 * 生效状态【合同条款】标签页【修改按钮】都是可用状态
				 * 现在根据瓮福需求改为审核与生效后不可修改【合同条款】
				 */
				else if(ctflag == BillState.AUDIT
						|| ctflag == BillState.VALIDATE){
					setTbGroupState(false, false, false, false, false);
				}
				//end ncm  wangminp NC2014122600148_瓮福集团_2014-12-29_专
				else
					setTbGroupState(false, false, false, false, false);
			}
			// add by liuzy 合同终止后大记事不可编辑
			else if (m_iTabbedFlag == TabState.NOTE) {
				if (ctflag == BillState.TERMINATE)
					setTbGroupState(false, false, false, false, false);
			}
		}

	}

	/**
	 * 此处插入方法说明。 创建日期：(2001-10-24 13:56:49)
	 */
	protected void setTbButtonState() {

		if (m_iTbState == OperationState.FREE) {
			if (m_iTabbedFlag == TabState.EXEC) {
				setTbGroupState(false, false, false, false, false);
			} else if (m_iTabbedFlag == TabState.CHANGE
					&& m_bChangeFlag == false) {

				int rowCount = getCtBillCardPanel().getBillModel("history")
						.getRowCount();
				// 如果变更历史只有一行,说明没有执行过合同变更,也就无从修改变更历史.
				if (rowCount <= 1)
					setTbGroupState(false, false, false, false, false);
				else
					setTbGroupState(false, false, true, false, false);

			} else {
				setTbGroupState(false, false, true, false, false);
			}

		} else if (m_iTbState == OperationState.ADD) {

			if (m_iTabbedFlag == TabState.EXEC) {
				setTbGroupState(false, false, false, false, false);
			} else
				setTbGroupState(true, true, false, true, true);

		} else if (m_iTbState == OperationState.EDIT) {

			if (m_iTabbedFlag == TabState.EXEC) {
				setTbGroupState(false, false, false, false, false);
			} else if (m_iTabbedFlag == TabState.CHANGE) {
				setTbGroupState(false, false, false, true, true);
			} else
				setTbGroupState(true, true, false, true, true);

		} else { // tbOperaState.equals("删除")

			if (m_iTabbedFlag == TabState.EXEC) {
				setTbGroupState(false, false, false, false, false);
			} else
				setTbGroupState(true, true, false, true, true);

		}

		setTbButonByflag();
	}

	/**
	 * 设置Table按钮组的状态 创建日期：(2001-10-28 13:42:57)
	 * 
	 * @param tbAdd
	 *            boolean
	 * @param tbDel
	 *            boolean
	 * @param tbEdit
	 *            boolean
	 * @param tbCancel
	 *            boolean
	 * @param tbSave
	 *            boolean
	 */
	private void setTbGroupState(boolean tbAdd, boolean tbDel, boolean tbEdit,
			boolean tbCancel, boolean tbSave) {

		getButtonTree().getButton(CTButtonConst.BTN_TAB_ADD).setEnabled(tbAdd);
		getButtonTree().getButton(CTButtonConst.BTN_TAB_DEL).setEnabled(tbDel);
		getButtonTree().getButton(CTButtonConst.BTN_TAB_EDIT)
				.setEnabled(tbEdit);
		getButtonTree().getButton(CTButtonConst.BTN_TAB_CANCEL).setEnabled(
				tbCancel);
		getButtonTree().getButton(CTButtonConst.BTN_TAB_SAVE)
				.setEnabled(tbSave);
	}

	/**
	 * 此处插入方法说明。 创建日期：(2001-10-28 14:45:10)
	 * 
	 * @param e
	 *            javax.swing.event.ChangeEvent
	 */
	public void stateChanged(ChangeEvent e) {
		if (m_bNeedChange) {
			if (e.getSource() == getCtBillCardPanel().getBodyTabbedPane()) {
				UITabbedPane tabbedPane = (UITabbedPane) e.getSource();
				m_iTabbedFlag = tabbedPane.getSelectedIndex();

				if (!(m_iBillState == OperationState.FREE)
						|| !(m_iTbState == OperationState.FREE)) {
					/* @res "{0}可能已被更改，是否需要保存？" */
					String sMessage = nc.ui.ml.NCLangRes.getInstance()
							.getStrByID(
									"4020pub",
									"UPP4020pub-000193",
									null,
									new String[] { getCtBillCardPanel()
											.getBodyTabbedPane().getTitleAt(
													m_iTabbedOldFlag) });
					int ync = MessageDialog.showYesNoCancelDlg(this, null,
							sMessage);

					if (ync == UIDialog.ID_YES) { // 需要保存
						int temp = m_iTabbedFlag;
						m_iTabbedFlag = m_iTabbedOldFlag;
						getCtBillCardPanel().getBodyPanel().setTableCode(
								CTTableCode.CT_TABCODE[m_iTabbedFlag]);
						onSave(); // 保存所做的修改
						if (!m_bErrFlag) {
							m_iTabbedFlag = temp; // 得到现在的页签号
							getCtBillCardPanel().getBodyPanel().setTableCode(
									CTTableCode.CT_TABCODE[m_iTabbedFlag]);
						} else {
							m_bNeedChange = false; // 不能再触发stateChanged()方法
							getCtBillCardPanel().getBodyTabbedPane()
									.setSelectedIndex(m_iTabbedOldFlag);
							// 页签回赋
							m_iTabbedFlag = m_iTabbedOldFlag;

							return;
						}

					} else if (ync == UIDialog.ID_NO) { // 不需要保存

						m_bIsNeedReInit[m_iTabbedOldFlag] = true;
						int temp = m_iTabbedFlag;
						m_iTabbedFlag = m_iTabbedOldFlag;
						// getCtBillCardPanel().getBodyPanel().setTableCode(
						// CTTableCode.CT_TABCODE[m_iTabbedFlag]);
						// 取消
						onCancel();
						// added by lirr 2009-02-17
						m_iBillState = OperationState.FREE;

						if (!m_bErrFlag) {
							m_iTabbedFlag = temp; // 得到现在的页签号
							getCtBillCardPanel().getBodyPanel().setTableCode(
									CTTableCode.CT_TABCODE[m_iTabbedFlag]);

						} else {
							m_bNeedChange = false; // 不能再触发stateChanged()方法
							getCtBillCardPanel().getBodyTabbedPane()
									.setSelectedIndex(m_iTabbedOldFlag);
							// 页签回赋
							m_iTabbedFlag = m_iTabbedOldFlag;
							return;
						}

					} else { // 取消操作
						m_bNeedChange = false; // 不能再触发stateChanged()方法
						getCtBillCardPanel().getBodyTabbedPane()
								.setSelectedIndex(m_iTabbedOldFlag);
						// 页签回赋
						m_iTabbedFlag = m_iTabbedOldFlag;

						return;
					}

				}
			}
			// 改变按钮组
			setDifferButtons();

			// 如果需要重新载入数据
			if (m_iTabbedFlag != TabState.BILL
					&& m_bIsNeedReInit[m_iTabbedFlag] == true) {
				// 获得相应的Table记录
				loadData();
				m_bIsNeedReInit[m_iTabbedFlag] = false;
			}
			showHintMessage("");
			// setButtonState();
			if (!m_bAddFromBillFlag) {
				setButtonState();
			} else {
				setButtonStateForCof();
			}
		} else {
			m_bNeedChange = true; // 可以触发
		}
	}

	/**
	 * 此处插入方法说明。 创建日期：(2001-9-7 12:53:43)
	 * 
	 * @param param
	 *            javax.swing.event.ListSelectionEvent
	 */
	public void valueChanged(ListSelectionEvent e) {
		showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4020pub",
				"UPP4020pub-000085")/* @res "点击查看对应合同的存货情况" */);

		int row = -1;

		try {
			if (e.getSource() == getCtBillListPanel().getHeadTable()
					.getSelectionModel()) {

				row = getCtBillListPanel().getHeadTable().getSelectedRow();
				// 如果未选中行，返回
				if (row < 0)
					return;
				else if (row > 0)
					m_bIsFirstClick = false;

				m_iId = row + 1;

				// loadBodyData();
				if (!m_bAddFromBillFlag) {
					loadBodyData();
				}

				BillItem item = getCtBillListPanel().getHeadItem("ctflag");
				Object aValue = getCtBillListPanel().getHeadBillModel()
						.getValueAt(row, "ctflag");
				aValue = item.converType(aValue);

				int ctFlag = Integer.parseInt(aValue.toString());
				// comContractStatusItem.getSelectedIndex();
				if (!m_bAddFromBillFlag) {
					setButtonState();
				} else {
					setButtonStateForCof();
				}

				// 设置单据主按钮的状态
				// if (ctFlag == BillState.FREE) {
				// modified by lirr 正在审核 也要可以修改
				if (ctFlag == BillState.FREE ) {
					getButtonTree().getButton(CTButtonConst.BTN_BILL_EDIT)
							.setEnabled(true);
					if (!m_bAddFromBillFlag) {
						getButtonTree()
								.getButton(CTButtonConst.BTN_BILL_DELETE)
								.setEnabled(true);
					} else {
						getButtonTree()
								.getButton(CTButtonConst.BTN_BILL_DELETE)
								.setEnabled(false);
					}
                // 修改人:wuweiping 修改时间:2009-10-21 下午03:45:34 修改原因:对于审核中的单据如果有了审批人则不能再修改
				}
				if(ctFlag == BillState.CHECKGOING){
					String audiname = (String) ((ManageHeaderVO) ((ExtendManageVO) m_vBillVO
							.elementAt(m_iId - 1)).getParentVO()).getAttributeValue("audiname");
					if(audiname!=null && !audiname.equals(" ")){
						getButtonTree().getButton(CTButtonConst.BTN_BILL_EDIT)
						.setEnabled(false);
						getButtonTree().getButton(CTButtonConst.BTN_BILL_DELETE)
						.setEnabled(false);
					}
				} 
				if(ctFlag != BillState.CHECKGOING && ctFlag != BillState.FREE ){
					getButtonTree().getButton(CTButtonConst.BTN_BILL_EDIT)
					.setEnabled(false);
			        getButtonTree().getButton(CTButtonConst.BTN_BILL_DELETE)
					.setEnabled(false);
				}
				updateButtons();

				m_bChangeFlag = false; // 标志合同不可变更
				// 改变单据，所有Table都需要重新载入数据
				for (int i = 1; i < 6; i++)
					m_bIsNeedReInit[i] = true;
				// 获得对应合同的表体记录
				setBodyListData(row);

				// tableOperaState = "自由";

				// }
				// }
				if (e.getSource() == getCtBillListPanel().getHeadTable()
						.getSelectionModel()) {
					// 设置表头选中
					int nSelected[] = getCtBillListPanel().getHeadTable()
							.getSelectedRows();
					if (nSelected != null && nSelected.length > 0) {
						for (int i = 0; i < nSelected.length; i++) {
							getCtBillListPanel().getHeadBillModel()
									.setRowState(nSelected[i],
											BillModel.SELECTED);
						}
					}
				}
				// modified by lirr 2009-02-18 修改原因：在非基本页签上选择行时Row index out of
				// range
				/*
				 * getCtBillListPanel().getBodyTable().setRowSelectionInterval(0,
				 * 0);
				 */
				getCtBillListPanel().getBodyTable(
						CTTableCode.CT_TABCODE[m_iTabbedFlag])
						.setRowSelectionInterval(0, 0);

			} else if (e.getSource() == getCtBillCardPanel().getBillTable()
					.getSelectionModel()) {
				if (getCtBillCardPanel().getBillData().getEnabled()) {
					int colnow = getCtBillCardPanel().getBillTable()
							.getSelectedColumn();
					int rownow = getCtBillCardPanel().getBillTable()
							.getSelectedRow();
					if (colnow >= 0 && rownow >= 0) {
						initRef(rownow, colnow, getCtBillCardPanel());
						String key = getCtBillCardPanel().getBodyShowItems()[colnow]
								.getKey();
						// Object value=
						// getCtBillCardPanel().getBodyValueAt(rownow, key);
						// 向辅计量传递参数
						if (key.equals("astmeascode")) {
							String sInvID = (String) getCtBillCardPanel()
									.getBodyValueAt(rownow, "invid");
							if (null != sInvID) {
								filterMeas(sInvID, key, m_sPkCorp);
							}
						}
					}
				}
			}
		} catch (NumberFormatException ex) {
			// 日志异常
			nc.vo.scm.pub.SCMEnv.out(ex);
		}
	}

	// added by lirr 2008-08-14 复选框选中
	// public void valueChanged(RowStateChangeEvent event) {
	/*
	 * // getCtBillListPanel().getChildListPanel().getTable().mouseSelect; if
	 * (event.getRow() != getCtBillListPanel().getHeadTable() .getSelectedRow()) {
	 * headRowChange(event.getRow());
	 * 
	 * BillModel model = getCtBillListPanel().getBodyBillModel();
	 * IBillModelRowStateChangeEventListener l = model
	 * .getRowStateChangeEventListener();
	 * model.removeRowStateChangeEventListener();
	 * 
	 * if (event.isSelectState()) {
	 * getCtBillListPanel().getChildListPanel().selectAllTableRow(); } else {
	 * getCtBillListPanel().getChildListPanel() .cancelSelectAllTableRow(); }
	 * model.addRowStateChangeEventListener(l);
	 * 
	 * getCtBillListPanel().updateUI(); }
	 */
	// }
	/**
	 * 只对表头进行处理
	 * <li>行切换 事件
	 * <li>双击 事件
	 * <li>WARN::行切换事件发生在双击事件之前
	 * 
	 * @param iNewRow
	 */
	private synchronized void headRowChange(int iNewRow) {
		if (getCtBillListPanel().getHeadBillModel().getValueAt(iNewRow, 0) != null) {
			if (!getCtBillListPanel().setBodyModelData(iNewRow)) {
				// 1.初次载入表体数据
				loadBodyData();
				// 2.备份到模型中
				getCtBillListPanel().setBodyModelDataCopy(iNewRow);
			}
		}
		getCtBillListPanel().repaint();
	}

	/**
	 * 方法功能描述：缓加载表体数据
	 * <p>
	 * <b>examples:</b>
	 * <p>
	 * 使用示例
	 * <p>
	 * <b>参数说明</b>
	 * <p>
	 * 
	 * @author liuzy
	 * @time 2008-4-17 下午01:38:37
	 */
	protected void loadBodyData() {
		ManageVO manageVO = getCurVO();
		// if (null == manageVO.getChildrenVO()
		// || manageVO.getChildrenVO().length < 1) {
		try {
			// manageVO = ContractQueryHelper.queryAllBodyData(manageVO);
			// if (null == manageVO) {
			// showHintMessage(NCLangRes.getInstance().getStrByID(
			// "4020pub", "UPP4020pub-000269")/* 查询表体数据时出错 */);
			// return;
			// }
			ExtendManageVO voExtendCur = new ExtendManageVO();
			CircularlyAccessibleValueObject[] tableVOs = manageVO
					.getChildrenVO();
			CircularlyAccessibleValueObject[] termVOs = manageVO.getTermBb4s();
			voExtendCur.setParentVO(manageVO.getParentVO());
			voExtendCur.setTableVO("table", tableVOs);
			voExtendCur.setTableVO("term", termVOs);
			voExtendCur.setTableVO("cost", manageVO.getExpBb3s());
			voExtendCur.setTableVO("history", manageVO.getChangeBb1s());
			voExtendCur.setTableVO("exec", manageVO.getManageExecs());
			voExtendCur.setTableVO("note", manageVO.getMemoraBb2s());
			/*
			 * // 自由项组合 for (int k = 0; k < tableVOs.length; k++) { FreeVO
			 * voFree = InvTool.getInvFreeVO((String) tableVOs[k]
			 * .getAttributeValue("invid")); // 存货管理主键 if (voFree != null) {
			 * voFree.setVfree1((String) tableVOs[k]
			 * .getAttributeValue("vfree1")); voFree.setVfree2((String)
			 * tableVOs[k] .getAttributeValue("vfree2"));
			 * voFree.setVfree3((String) tableVOs[k]
			 * .getAttributeValue("vfree3")); voFree.setVfree4((String)
			 * tableVOs[k] .getAttributeValue("vfree4"));
			 * voFree.setVfree5((String) tableVOs[k]
			 * .getAttributeValue("vfree5"));
			 * 
			 * tableVOs[k].setAttributeValue("vfree0", voFree
			 * .getWholeFreeItem()); } }
			 */
			// modified by lirr 2009-8-20下午07:25:39 减少连接数
			nc.ui.scm.pub.FreeVOParse freeVOParse = new nc.ui.scm.pub.FreeVOParse();
			freeVOParse.setFreeVO(tableVOs, null, "invid", false);

			getCtBillListPanel().getBodyBillModel().execFormulasWithVO(
					tableVOs, getTableFormulas());
			getCtBillListPanel().getBodyBillModel().execFormulasWithVO(termVOs,
					getTableFormulas());
			m_vBillVO.setElementAt(voExtendCur, m_iId - 1);

		} catch (Exception e1) {
			// 日志异常
			nc.vo.scm.pub.SCMEnv.out(e1);
			// 按规范抛出异常
			showErrorMessage(NCLangRes.getInstance()
					.getStrByID("4020pub", "UPP4020pub-000270", null,
							new String[] { e1.getMessage() })/* 查询表体数据出错：\n{0} */);
			// }
		}
	}

	/**
	 * 此处插入方法说明。 创建日期：(2001-8-22 12:36:36)
	 * 
	 * @return nc.ui.pub.bill.BillCardPanel
	 */
	protected ScmBillCardPanel getCtBillCardPanel() {
		if (m_BillCardPanel == null) {
			try {
				m_BillCardPanel = new CtBillCardPanel();
				m_BillCardPanel.setTatolRowShow(true);
				m_BillCardPanel.setRowNumKey("crowno");

				// 初始化单据卡片模板
				if (m_sBillType == null) {
					MessageDialog.showErrorDlg(this, null, nc.ui.ml.NCLangRes
							.getInstance().getStrByID("4020pub",
									"UPP4020pub-000086")/* @res "单据类型未赋值" */);
				} else {
					BillData bd = null;
					if (getFrame() == null)
						/*
						 * bd = new BillData(m_BillCardPanel.getTempletData(
						 * m_sBillType, null, m_sPkUser, m_sPkCorp));
						 */
						// modified by lirr 2009-8-17下午01:11:58 减少连接数
						// 列表与卡片一次用getBillTempletVO
						bd = new BillData(getBillTempletVO(m_sBillType));
					else
						/*
						 * bd = new BillData( m_BillCardPanel .getTempletData(
						 * (getFrame().getModuleCode() != null && !getFrame()
						 * .getModuleCode() .equals(getNodeCode())) ? getFrame()
						 * .getModuleCode() : getBillType(), null, m_sPkUser,
						 * m_sPkCorp));
						 */
						bd = new BillData(
								getBillTempletVO((getFrame().getModuleCode() != null && !getFrame()
										.getModuleCode().equals(getNodeCode())) ? getFrame()
										.getModuleCode()
										: getBillType()));

					// 自由项
					FreeItemRefPane freeItemRefPanel = new FreeItemRefPane();
					BillItem freeItem = bd.getBodyItem(CTTableCode.BASE,
							"vfree0");
					freeItemRefPanel.setMaxLength(freeItem.getLength());
					freeItem.setComponent(freeItemRefPanel);

					setCardPanel(bd);
					m_BillCardPanel.setBillData(bd);
					m_BillCardPanel.setBillType(m_sBillType);
					m_BillCardPanel.setCorp(m_sPkCorp);
					m_BillCardPanel.setOperator(m_sPkUser);
					m_BillCardPanel.setNodeCode(m_sNodeCode);
					m_BillCardPanel.setTatolRowShow(true);
					m_BillCardPanel.getTotalTableModel().setValueAt(
							nc.ui.ml.NCLangRes.getInstance().getStrByID(
									"common", "UC000-0001146")/* @res "合计" */,
							0, 0);

					// m_BillCardPanel.getBillModel().

					// setCardPanelFormular(); // 置入所有公式
					// setCardPanelActionTable(); // 置入所有动作顺序
					m_BillCardPanel.setHslUsedKeys("invcode", "invid", null,
							"astmeascode", "astmeasname", "astmeasid",
							"transrate", "amount", "astnum");

					m_BillCardPanel.setPriceMoneyKey("oriprice", "orisum");
					ArrayList alNotCheck = new ArrayList();
					alNotCheck.add("bsafeprice");
					alNotCheck.add("breturnprofit");
					m_BillCardPanel.setNullRowNotCheckKeys(alNotCheck);
				}
				nc.ui.ct.pub.print.ContractPrintDataInterface cpdi = new nc.ui.ct.pub.print.ContractPrintDataInterface();
				m_BillCardPanel.setDataSource(cpdi);

				// 非固定换算率不能输入负数
				UIRefPane refTransrate = (UIRefPane) m_BillCardPanel
						.getBodyItem(CTTableCode.BASE, "transrate")
						.getComponent();
				refTransrate.setDelStr("-");
				// 合同类型参照不要缓存
				((UIRefPane) getCtBillCardPanel().getHeadItem("ct_type")
						.getComponent()).setCacheEnabled(false);
				getCtBillCardPanel().setBodyMenuShow(true);

			} catch (Throwable e) {
				nc.vo.scm.pub.SCMEnv.out(e);
			}
		}
		return m_BillCardPanel;
	}

	/**
	 * 此处插入方法说明。 创建日期：(2001-8-22 19:00:00)
	 * 
	 * @return nc.ui.pub.bill.BillListPanel
	 */
	protected ScmBillListPanel getCtBillListPanel() {
		if (m_BillListPanel == null) {
			try {
				m_BillListPanel = new ScmBillListPanel(true);

				// 初始化单据列表模板
				/*
				 * BillListData bd = new BillListData(m_BillListPanel
				 * .getDefaultTemplet(m_sBillType, null, m_sPkUser, m_sPkCorp));
				 */
				// modified by lirr 2009-8-17下午01:17:44 减少连接数
				// 卡片与列表同一用getBillTempletVO(m_sBillType)
				BillListData bd = new BillListData(
						getBillTempletVO(m_sBillType));
				// 改变界面
				setListPanelByPara(bd);
               // 修改人:wuweiping 修改时间:2009-10-15 下午06:55:23 修改原因:编写公共类减少代码量
				bd = CTTool.changeBillListDataByUserDef(m_sPkCorp, m_sBillType,bd);
				//changeBillListDataByUserDef(bd);
				// try {
				// // 修改自定义项
				// bd = changeBillDataByUserDef(bd);
				// }
				// catch (Exception e) {
				// }

				// 设置界面，置入数据源
				m_BillListPanel.setListData(bd);
				// 显示合计列
				m_BillListPanel.getChildListPanel().setTotalRowShow(true);
				// 设置表头选择模式：表头多选
				m_BillListPanel.getHeadTable().setSelectionMode(
						ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

			} catch (Throwable e) {
				nc.vo.scm.pub.SCMEnv.out(e);
			}
		}
		return m_BillListPanel;
	}

	/**
	 * 创建日期：(2003-3-18 10:16:08) 作者：王亮 修改日期： 修改人： 修改原因： 方法说明： modified by lirr
	 * 2008-08-05 增加 参数 sCustType，原因：客商可以同时为供应商或者客商，作为客商和供应商的币种不同 sCustType "and
	 * bd_cumandoc.custflag in ('0','2')" /"and bd_cumandoc.custflag in
	 * ('1','3')"
	 */
	public String[] getCustInfo(String sPk_Custbasdoc, String sCustType) {
		String[] s = null;
		try {
			s = ContractQueryHelper.queryCustInfo(sPk_Custbasdoc, sCustType);
		} catch (Exception e) {
			nc.vo.scm.pub.SCMEnv.out(e);
		}
		return s;
	}

	/**
	 * 创建日期：(2008-08-05 10:16:08) 作者：李冉冉 修改日期： 修改人： 修改原因： 方法说明： sCustType "and
	 * bd_cumandoc.custflag in ('0','2')" /"and bd_cumandoc.custflag in
	 * ('1','3')"
	 */
	public String getCustaddr(String sPk_Custbasdoc) {
		String s = null;
		try {
			s = ContractQueryHelper.queryCustAddr(sPk_Custbasdoc);
		} catch (Exception e) {
			nc.vo.scm.pub.SCMEnv.out(e);
		}
		return s;
	}

	/**
	 * 此处插入方法说明。 创建日期：(2001-8-22 16:25:45)
	 */
	protected void initButtons() {

		// 增加扩展按钮
		ButtonObject[] btnExtendButtons = getExtendBtns();
		if (btnExtendButtons != null && btnExtendButtons.length != 0) {
			ArrayList alAllButtons = new ArrayList();

			for (int i = 0; i < m_aryButtonGroup.length; i++)
				alAllButtons.add(m_aryButtonGroup[i]);

			for (int i = 0; i < btnExtendButtons.length; i++)
				alAllButtons.add(btnExtendButtons[i]);

			m_aryButtonGroup = new ButtonObject[alAllButtons.size()];
			alAllButtons.toArray(m_aryButtonGroup);
		}

		// 加上按钮组
		setButtons(m_aryButtonGroup);
		changeToBlButn();

	}

	/**
	 * 此处插入方法说明。 创建日期：(2001-9-19 15:56:35)
	 */
	protected void onAbolish() {
		String sMessage = null;
		try {

			ManageVO tempMVO = new ManageVO();
			ManageVO mVO = getCurVO();
			tempMVO = mVO.clone(mVO);
			int ctState = Integer.parseInt(tempMVO.getParentVO()
					.getAttributeValue("ctflag").toString());

			// 对处于不同状态的合同进行不同的处理
			if (ctState == BillState.FREE) { // 自由状态
				sMessage = nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"4020pub", "UPP4020pub-000087")/* @res "是否确定要废止该合同？" */;

				if (MessageDialog.showYesNoDlg(this, null, sMessage) == 4) {
					tempMVO.getParentVO().setAttributeValue("ctflag",
							new Integer(BillState.ABOLISH));

					tempMVO.setOldBillStatus(BillState.FREE);
					tempMVO.setBillStatus(BillState.ABOLISH);

					nc.ui.pub.pf.PfUtilClient.processAction("ABOLISH",
							m_sBillType, getClientEnvironment().getDate()
									.toString(), tempMVO, tempMVO);

					mVO.getParentVO().setAttributeValue("ctflag",
							new Integer(BillState.ABOLISH));
					getCtBillCardPanel().getHeadItem("ctflag").setValue(
							new Integer(BillState.ABOLISH));
					getCtBillListPanel().getHeadBillModel().setValueAt(
							nc.ui.ml.NCLangRes.getInstance().getStrByID(
									"4020const", BillState.STATERESID_ABOLISH),
							m_iId - 1, "ctflag");
					ExtendManageVO voCur = (ExtendManageVO) m_vBillVO
							.get(m_iId - 1);
					voCur.setParentVO(mVO.getParentVO());
					voCur.setTableVO("exec", mVO.getManageExecs());
					m_vBillVO.remove(m_iId - 1);
					m_vBillVO.insertElementAt(voCur, m_iId - 1);
					getCtBillCardPanel().setBillValueVO(voCur);

				}
			} else { // 状态不一致
				sMessage = nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"4020pub", "UPP4020pub-000088")/*
														 * @res
														 * "状态有误，请重新查询后再进行操作！"
														 */;
				MessageDialog.showWarningDlg(this, null, sMessage);
			}

		} catch (java.sql.SQLException e) {
			sMessage = nc.ui.ml.NCLangRes.getInstance().getStrByID("4020pub",
					"UPP4020pub-000066")/* @res "数据库操作错误" */;
			MessageDialog.showErrorDlg(this, sMessage, e.getMessage());

		} catch (java.rmi.RemoteException e) {
			sMessage = nc.ui.ml.NCLangRes.getInstance().getStrByID("4020pub",
					"UPP4020pub-000067")/* @res "远程调用失败" */;
			MessageDialog.showErrorDlg(this, sMessage, e.getMessage());

		} catch (Exception e) {
			MessageDialog.showErrorDlg(this, null, e.getMessage());
			reportException(e);

		}
		setButtonState();
	}

	/**
	 * 此处插入方法说明。 功能： 参数： 返回： 例外： 日期：(2002-9-11 9:46:03) 修改日期，修改人，修改原因，注释标志：
	 * 
	 * @param ButtongObject
	 *            nc.ui.pub.ButtonObject
	 */
	// 修改说明：将setButtonState();方法由onButtonClicked方法的最后移动到了每个事件处理的后面。修改原因：影响焦点 修改人
	// ：张学坤 修改日期:2007-11-1
	public void onButtonClicked(ButtonObject bo) {
		showHintMessage("");

		if (bo == getButtonTree().getButton(CTButtonConst.BTN_ADD)) {
			/*
			 * onAdd();
			 * getCtBillCardPanel().transferFocusTo(BillCardPanel.HEAD);
			 */

			// modified by lirr 2009-03-11 销售合同增加功能删掉 sincev56 改为：自制、参照等
			if (!BillType.PURDAILY.equals(getBillType())
					&& !BillType.SALEDAILY.equals(getBillType())) {
				onAdd();
				getCtBillCardPanel().transferFocusTo(BillCardPanel.HEAD);
			}

		} else if (bo == getButtonTree().getButton(CTButtonConst.BTN_SAVE))
			onSave();
		else if (bo == getButtonTree().getButton(CTButtonConst.BTN_BILL_EDIT))
			onEdit();
		else if (bo == getButtonTree().getButton(CTButtonConst.BTN_BILL_COPY))
			onCopy();

		else if (bo == getButtonTree().getButton(CTButtonConst.BTN_BILL_DELETE))
			onDel();

		else if (bo == getButtonTree().getButton(CTButtonConst.BTN_BILL_CANCEL))
			onCancel();

		else if (bo == getButtonTree().getButton(CTButtonConst.BTN_LINE_ADD))
			onAddLine();

		else if (bo == getButtonTree().getButton(CTButtonConst.BTN_LINE_DELETE))
			onDelLine();

		else if (bo == getButtonTree().getButton(CTButtonConst.BTN_LINE_INSERT)) {
			if (getCtBillCardPanel().getBodyPanel().getTable().getSelectedRow() == -1) {
				MessageDialog.showHintDlg(this, null, nc.ui.ml.NCLangRes
						.getInstance().getStrByID("4020pub",
								"UPT4020pub-000231")/*
													 * @res "请先选择表体行"
													 */
				);
				return;
			}

			getCtBillCardPanel().insertLine();
			// setButtonState();
			if (!m_bAddFromBillFlag) {
				setButtonState();
			} else {
				setButtonStateForCof();
			}

		} else if (bo == getButtonTree().getButton(CTButtonConst.BTN_LINE_COPY)) {
			getCtBillCardPanel().copyLine();
			// setButtonState();
			if (!m_bAddFromBillFlag) {
				setButtonState();
			} else {
				setButtonStateForCof();
			}
		} else if (bo == getButtonTree()
				.getButton(CTButtonConst.BTN_LINE_PASTE)) {
			CellControl[][] ccs = null;
			if (null != getCtBillCardPanel().getBodyItems()) {
				ccs = new CellControl[getCtBillCardPanel().getRowCount() + 1][getCtBillCardPanel()
						.getBodyItems().length];
				for (int i = 0; i < getCtBillCardPanel().getRowCount(); i++) {
					if (null != getCtBillCardPanel().getDmdo()
							.getCellControls())
						ccs[i] = getCtBillCardPanel().getDmdo()
								.getCellControls()[i];
					else
						ccs[i] = getCtBillCardPanel()
								.getDefaultRowCellControl();
				}
				ccs[getCtBillCardPanel().getRowCount()] = getCtBillCardPanel()
						.getDefaultRowCellControl();
				getCtBillCardPanel().getDmdo().setCellControls(ccs);
			}
			getCtBillCardPanel().pasteLine();

			// add by qinchao 20090224 功能：把粘贴的行的"累计付款金额"置为0
			int row = getCtBillCardPanel().getBodyPanel().getTable()
					.getSelectedRow();
			int rowlength = getCtBillCardPanel().getBillModel()
					.getPasteLineNumer();
			clearExecInfo(row,rowlength);
			// updateUI();
			// end 功能：把粘贴的行的"累计付款金额"置为0

			if (!m_bAddFromBillFlag) {
				setButtonState();
			} else {
				setButtonStateForCof();
			}
		}

		else if (bo == getButtonTree().getButton(
				CTButtonConst.BTN_LINE_LineToTail)) {
			getCtBillCardPanel().pasteLineToTail();

			// add by qinchao 20090224 功能：把粘贴的行的"累计付款金额"置为0
			int row = getCtBillCardPanel().getBodyPanel().getTable()
					.getRowCount();
			int rowlength = getCtBillCardPanel().getBillModel()
					.getPasteLineNumer();
			clearExecInfo(row,rowlength);
			
			updateUI();
			// end 功能：把粘贴的行的"累计付款金额"置为0

		}

		else if (bo == getButtonTree().getButton(CTButtonConst.BTN_BROWSE_NEXT))
			onNext();

		else if (bo == getButtonTree().getButton(
				CTButtonConst.BTN_BROWSE_PREVIOUS))
			onPre();

		else if (bo == getButtonTree().getButton(CTButtonConst.BTN_SWITCH))
			onList();

		else if (bo == getButtonTree().getButton(CTButtonConst.BTN_QUERY))
			onQuery();

		else if (bo == getButtonTree().getButton(CTButtonConst.BTN_AUDIT)) {
			// if(m_iBillState == OperationState.EDIT)
			// MessageDialog.showErrorDlg(this, null, "编辑状态下不可以进行审核操作");
			// else{
			// if (!m_bIsCard
			// && getCtBillListPanel().getHeadTable()
			// .getSelectedRowCount() > 1
			// && (MessageDialog.showYesNoDlg(this, null,
			// nc.ui.ml.NCLangRes.getInstance().getStrByID(
			// "4020nodes", "UPP4020nodes-000051")/*
			// * @res
			// * "您是否确定要审核该合同？"
			// */) == 4))
			// onBatchAction("审批");
			// else
			if (m_iBillState == OperationState.EDIT) {
				showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"4020pub", "UPT4020pub-000232")/*
														 * @res
														 * "编辑状态下不可以进行审核操作，请先取消编辑或保存"
														 */);
			} else
				onCheck(false);
			// }
			// setButtonState();
			if (!m_bAddFromBillFlag) {
				setButtonState();
			} else {
				setButtonStateForCof();
			}
		} else if (bo == getButtonTree().getButton(
				CTButtonConst.BTN_EXECUTE_CTFREEZE))
			onFreeze();

		else if (bo == getButtonTree().getButton(
				CTButtonConst.BTN_EXECUTE_CTFREEZE_CANCEL))
			onUnFreeze();

		else if (bo == getButtonTree().getButton(
				CTButtonConst.BTN_EXECUTE_TERMINATE))
			onTerminate();

		else if (bo == getButtonTree().getButton(
				CTButtonConst.BTN_EXECUTE_VALIDATE)) {
			// if (!m_bIsCard
			// && getCtBillListPanel().getHeadTable()
			// .getSelectedRowCount() > 1
			// && (MessageDialog.showYesNoDlg(this, null,
			// nc.ui.ml.NCLangRes.getInstance().getStrByID(
			// "4020pub", "UPP4020pub-000090")/*
			// * @res
			// * "是否确定要使该合同生效？"
			// */) == 4)) {
			// onBatchAction("生效");
			// } else {
			onValidate(false);
			// }
			// setButtonState();
			if (!m_bAddFromBillFlag) {
				setButtonState();
			} else {
				setButtonStateForCof();
			}
		} else if (bo == getButtonTree().getButton(
				CTButtonConst.BTN_EXECUTE_TERMINATE))
			onAbolish();

		else if (bo == getButtonTree().getButton(
				CTButtonConst.BTN_EXECUTE_CTCHANGE))
			onChange();
		else if (bo == getButtonTree().getButton(
				CTButtonConst.BTN_EXECUTE_CANCELAPPRPVE)) {
			// if (!m_bIsCard
			// && getCtBillListPanel().getHeadTable()
			// .getSelectedRowCount() > 1
			// && (MessageDialog.showYesNoDlg(this, null,
			// nc.ui.ml.NCLangRes.getInstance().getStrByID(
			// "4020pub", "UPP4020pub-000158")/*
			// * @res
			// * "是否确定要弃审该合同？"
			// */) == 4))
			// onBatchAction("弃审");
			// else
			onCancelApprove(false);
			// setButtonState();
			if (!m_bAddFromBillFlag) {
				setButtonState();
			} else {
				setButtonStateForCof();
			}
		} else if (bo == getButtonTree().getButton(CTButtonConst.BTN_TAB_ADD))
			onAddLine();

		else if (bo == getButtonTree().getButton(CTButtonConst.BTN_TAB_DEL))
			onDelTbLine();

		else if (bo == getButtonTree().getButton(CTButtonConst.BTN_TAB_EDIT))
			onEditTbLine();

		else if (bo == getButtonTree().getButton(CTButtonConst.BTN_TAB_SAVE))
			onSave();

		else if (bo == getButtonTree().getButton(
				CTButtonConst.BTN_PRINT_PREVIEW))
			onPrintPreview();

		else if (bo == getButtonTree().getButton(CTButtonConst.BTN_PRINT_PRINT))
			onPrint();

		else if (bo == getButtonTree().getButton(CTButtonConst.BTN_TAB_CANCEL))
			onCancel();

		else if (bo == getButtonTree().getButton(
				CTButtonConst.BTN_ASSIST_FUNC_DOCUMENT)) // 文档管理
			onDocument();
		else if (bo == getButtonTree().getButton(CTButtonConst.BTN_TAB_PRINT))// 页签打印
			onTabPrint();
		else if (bo == getButtonTree().getButton(
				CTButtonConst.BTN_BROWSE_REFRESH))// 刷新
			onFresh();
		else if (bo == getButtonTree().getButton(
				CTButtonConst.BTN_ASSIST_QUERY_RELATED))
			onJoin();
		else if (bo == getButtonTree()
				.getButton(CTButtonConst.BTN_CTOTHER_TERM))
			onTerm();
		else if (bo == getButtonTree()
				.getButton(CTButtonConst.BTN_CTOTHER_NOTE))
			onExp();
		else if (bo == getButtonTree().getButton(CTButtonConst.BTN_CTOTHER_EXP))
			onNotes();
		else if (bo == getButtonTree().getButton(
				CTButtonConst.BTN_CTOTHER_CHANGEHISTORY))
			onHisitory();
		else if (bo == getButtonTree()
				.getButton(CTButtonConst.BTN_CTOTHER_EXEC))
			onExec();
		else if (bo == getButtonTree().getButton(
				CTButtonConst.BTN_EXECUTE_SENDAPPRPVE))
			onSavetoAudit(true);
		// added by lirr 2008-08-12 添加卡片编辑
		else if (bo == getButtonTree().getButton(ScmButtonConst.BTN_CARD_EDIT)) {
			onLineCardEdit();
		} else if (bo == getButtonTree().getButton(
				CTButtonConst.BTN_ADD_NEWROWNO)) {
			onAddNewRowNo();
		} else if (bo == getButtonTree().getButton(
				ScmButtonConst.BTN_LINE_PASTE_TAIL)) {
			getCtBillCardPanel().pasteLineToTail();
		}
		// added by lirr 2009-9-21上午09:54:44 查看审批流状态 从各个子类中抽出
		else if (bo == getButtonTree().getButton(
				CTButtonConst.BTN_ASSIST_QUERY_FLOWSTATUE))
			onFlowStatus();

		// 扩展按钮响应
		else
			onExtendBtnsClick(bo);

		// else if(bo == boAllotImprest)
		// onAllotImprest();
	}

	/**
	 * 对单据进行审核，改变单据的状态 创建日期：(2001-9-14 14:50:53)
	 */
	public String onCheck(boolean isBatch) {
		String strRtn = null;
		try {
			if (!m_bIsCard) {
				int[] selrows = getCtBillListPanel().getHeadTable()
						.getSelectedRows();
				if (selrows.length > 1) {
					onBatchAction(selrows, BatchActionHelp.sAudit);
					return null;
				}
			}
			m_iBillState = OperationState.CHECKGOING;

			ManageVO mVO = getCurVO();// 拿到当前选中的单据VO
			ManageVO tempMVO = getAuditVO(true, mVO);
			if (tempMVO == null){
			    // added by lirr 2009-11-28下午01:46:58 若要审核的单据为空 例如 是否审核时选择否m_iBillState = OperationState.FREE
			    m_iBillState = OperationState.FREE;
				return null;
			}
			ArrayList alRet = null;

			alRet = (ArrayList) nc.ui.pub.pf.PfUtilClient.processActionFlow(
					this, "APPROVE", m_sBillType, getClientEnvironment()
							.getDate().toString(), tempMVO, tempMVO);// 调用流程平台客户端工具类执行审批
			// 如果审核顺利
			if (!nc.ui.pub.pf.PfUtilClient.isSuccess()) {

				showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"SCMCOMMON", "UPPSCMCommon-000235")/*
															 * @res "审核失败"
															 */);
				return null;
			}
			// 合同PK[刷新合同状态]
			String sPK = (String) mVO.getParentVO().getAttributeValue(
					"pk_ct_manage");
			int ctStateNew = qryCtStatus(sPK).intValue();// 获得审核后合同状态

			if (BillState.AUDIT == ctStateNew
					|| BillState.CHECKGOING == ctStateNew) {
				/*
				 * mVO.getParentVO().setAttributeValue("ctflag", new
				 * Integer(BillState.AUDIT));
				 */// 设置合同为审核状态(非上面用的临时VO)
				/*
				 * mVO.getParentVO().setAttributeValue("ctflag",
				 * qryCtStatus(sPK).intValue());
				 */
				mVO.getParentVO().setAttributeValue("ctflag", ctStateNew);
				mVO.getParentVO().setAttributeValue("audiid", m_sPkUser);
				mVO.getParentVO().setAttributeValue("auditdate",
						getClientEnvironment().getDate());

				// modified by lirr 2009-8-26下午04:27:17 连接数
				getCtBillCardPanel().setHeadItem("ctflag", ctStateNew);

				getCtBillCardPanel().setTailItem("audiname", m_sPkUser);// 设置表体审批人项(卡片界面)
				getCtBillCardPanel().setTailItem("audiid", m_sPkUser);
				getCtBillCardPanel().setTailItem("auditdate",
						getClientEnvironment().getDate());// 审批时间
				getCtBillListPanel().getHeadBillModel().setValueAt(ctStateNew,
						m_iId - 1, "ctflag");// 设置合同状态(列表界面)

				getCtBillListPanel().getHeadBillModel().setValueAt(m_sUserName,
						m_iId - 1, "audiname");
				getCtBillListPanel().getHeadBillModel().setValueAt(m_sPkUser,
						m_iId - 1, "audiid");
				getCtBillListPanel().getHeadBillModel().setValueAt(
						getClientEnvironment().getDate(), m_iId - 1,
						"auditdate");

				if (alRet != null && alRet.size() > 0)
					freshStatusTs(mVO, (String) alRet.get(1));
				// modify reason : 列表界面审核后不显示审批时间 modified by lirr
				getCtBillListPanel().getHeadBillModel().setValueAt(
						(String) mVO.getParentVO().getAttributeValue("ts"),
						m_iId - 1, "taudittime");

				// 签字时间 取近似值ts
				setAuditTime(mVO, (String) mVO.getParentVO().getAttributeValue(
						"ts"));
			}

			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"SCMCOMMON", "UPPSCMCommon-000234")/*
														 * @res "审核成功"
														 */);

			// added by lirr 2008-12-27
			/**
			 * 如果状态重设写在这里 ， 审核时抛错 ，则就不会走这段代码 ，所以把 状态重设写在catch()之后 qinchao
			 * 2009-05-07
			 */
			// m_iBillState = OperationState.FREE;
		} catch (java.sql.SQLException e) {
			MessageDialog.showErrorDlg(this,
					nc.ui.ml.NCLangRes.getInstance().getStrByID("4020nodes",
							"UPP4020nodes-000043")/* @res "数据库操作错误" */, e
							.getMessage());
			strRtn = e.getMessage();
			
		} catch (java.rmi.RemoteException e) {
			MessageDialog.showErrorDlg(this,
					nc.ui.ml.NCLangRes.getInstance().getStrByID("4020nodes",
							"UPP4020nodes-000044")/* @res "远程调用失败" */, e
							.getMessage());
			strRtn = e.getMessage();
		} catch (Exception e) {
			MessageDialog.showErrorDlg(this, null, e.getMessage());
			reportException(e);
			strRtn = e.getMessage();
		}

		// added by lirr 2008-12-27 如果状态重设写在这里 ， 审核时抛错 ，则就不会走这段代码 ，所以把
		// 状态重设写在catch()之后 qinchao 2009-05-07
		m_iBillState = OperationState.FREE;

		// added by lirr 2008-12-25
		if (!m_bAddFromBillFlag) {
			setButtonState();
		} else {
			setButtonStateForCof();
		}
		return strRtn;
	}

	/**
	 * 篭n创建日期：(2003-6-16 11:52) 作者：赵勋平 修改日期： 修改人： 修改原因： 算法说明：
	 */
	public void onDocument() {
		String[] sIDs = null;
		String[] sCodes = null;
		// addied by liuzy 2008-04-11
		// V5.03需求：采购合同、销售合同的文档管理中，控制当单据处于正在审批、审批、生效、关闭状态时，“文档管理”中的删除按钮不可用；
		int[] iCTFlag = null;
		String[] sCTFlag = null;
		HashMap<String, BtnPowerVO> hmBtn = new HashMap<String, BtnPowerVO>();
		if (m_bIsCard == true) { // 在 "卡片" 的状态下
			if (getCtBillCardPanel().getBillData().getEnabled()) {
				showWarningMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"SCMCOMMON", "UPPSCMCommon-000016")/*
															 * @res
															 * "编辑状态不可执行此操作！"
															 */);
				return;
			}
			sIDs = new String[1];
			sCodes = new String[1];
			iCTFlag = new int[1];
			sIDs[0] = getCtBillCardPanel().getHeadItem(getCtPrimaryKeyName())
					.getValue();
			sCodes[0] = getCtBillCardPanel()
					.getHeadItem(getCtBillCodeKeyName()).getValue();
			// iCTFlag[0] = (Integer) getCtBillCardPanel().getHeadItem("ctflag")
			// .getValueObject();
			// modified by liuzy 从后台查询合同状态
			iCTFlag[0] = qryCtStatus(sIDs[0]);
			if (null == sIDs[0] || sIDs[0].trim().length() == 0
					|| null == sCodes[0] || sCodes[0].trim().length() == 0) {
				showWarningMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"4020pub", "UPP4020pub-000089")/* @res "没有单据ID或单据号！" */);
				return;
			}

			BtnPowerVO btnPowerVo = new BtnPowerVO(sCodes[0]);
			if (BillState.FREE == iCTFlag[0])
				btnPowerVo.setFileDelEnable("true");
			else
				btnPowerVo.setFileDelEnable("false");
			// hmBtn.put(sCodes[0], btnPowerVo);
			// modified by lirr 2008-12-29
			hmBtn.put(sIDs[0], btnPowerVo);

		} else { // 在 "列表" 的状态下
			int[] iRows = getCtBillListPanel().getHeadTable().getSelectedRows();
			if (iRows.length == 0) {
				showWarningMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"SCMCOMMON", "UPPSCMCommon-000275")/* @res "未选择行！" */);
				return;
			}
			sIDs = new String[iRows.length];
			sCodes = new String[iRows.length];
			sCTFlag = new String[iRows.length];
			for (int i = 0; i < iRows.length; i++) {
				sIDs[i] = getCtBillListPanel().getHeadBillModel().getValueAt(
						iRows[i], getCtPrimaryKeyName()).toString();
				sCodes[i] = getCtBillListPanel().getHeadBillModel().getValueAt(
						iRows[i], getCtBillCodeKeyName()).toString();
				// sCTFlag[i] = (String) getCtBillListPanel().getHeadBillModel()
				// .getValueAt(iRows[i], "ctflag");
				if (i == 0)
					sCTFlag[i] = String.valueOf(qryCtStatus(sIDs[i]));
				BtnPowerVO btnPowerVo = new BtnPowerVO(sCodes[i]);
				// modified by lirr 判断永远是 fasle 导致“删除”按钮不可见
				// if (BillState.STATERESID_FREE.equals(sCTFlag[i]))
				if (String.valueOf(BillState.FREE).equals(sCTFlag[i]))
					btnPowerVo.setFileDelEnable("true");
				else
					btnPowerVo.setFileDelEnable("false");
				// hmBtn.put(sCodes[i], btnPowerVo);
				// modified by lirr 2008-12-25
				hmBtn.put(sIDs[i], btnPowerVo);
			}
		}

		// TODO liuzy 待适配
		DocumentManager.showDM(this, m_sBillType, sIDs, /* sCodes, */hmBtn);
		// setButtonState();
		if (!m_bAddFromBillFlag) {
			setButtonState();
		} else {
			setButtonStateForCof();
		}
	}

	public void onBatchValidate() {
		onValidate(true);
	}

	/**
	 * 此处插入方法说明。 创建日期：(2001-9-19 15:56:12)
	 */
	protected void onValidate(boolean isBatch) {
		String sMessage = null;
		try {
			if (!m_bIsCard) {
				int[] selrows = getCtBillListPanel().getHeadTable()
						.getSelectedRows();
				if (selrows.length > 1) {
					onBatchAction(selrows, BatchActionHelp.sValidate);
					return;
				}
			}
			ManageVO mVO = getCurVO();
			ManageVO tempMVO = getValidateVO(true, mVO);
			// 修改时间: 2009/10/09 修改人:wuweiping 修改原因: 合同生效，点否，报空框
			if (tempMVO == null) {
				return;
			}
			// modified by lirr 2009-8-24上午10:58:19 增加返回值
			// 返回ts减少刷新ts时的连接数
			ArrayList alRet = null;
			try {

				alRet = (ArrayList) nc.ui.pub.pf.PfUtilClient.processAction(
						"VALIDATE", m_sBillType, getClientEnvironment()
								.getDate().toString(), tempMVO, tempMVO);
			} catch (Exception e) {
				if (e instanceof nc.vo.scm.pub.SaveHintException
						|| (e.getCause() != null && e.getCause() instanceof nc.vo.scm.pub.SaveHintException)) {
					int iRet = showYesNoMessage(e.getMessage());
					if (iRet != MessageDialog.ID_YES) {

						showHintMessage(nc.ui.ml.NCLangRes.getInstance()
								.getStrByID("SCMCOMMON", "UPPSCMCommon-000297")/*
																				 * @res
																				 * "业务错误"
																				 */);
						return;
					} else {
						try {
							SaveHintException saveHintEx = null;
							if (e instanceof nc.vo.scm.pub.SaveHintException) {
								saveHintEx = (SaveHintException) e;
							} else if (e.getCause() != null
									&& e.getCause() instanceof nc.vo.scm.pub.SaveHintException) {
								saveHintEx = (SaveHintException) (e.getCause());
							}
							String[] arCtLinePks = saveHintEx
									.getErrorCtLinePks();
							if (null != arCtLinePks && arCtLinePks.length > 0) {
								ArrayList<String> alCtLinePk = new ArrayList<String>(
										arCtLinePks.length);
								for (int i = 0; i < arCtLinePks.length; i++) {
									alCtLinePk.add(arCtLinePks[i]);
								}
								for (int i = 0; i < tempMVO.getChildrenVO().length; i++) {
									if (alCtLinePk
											.contains(tempMVO.getChildrenVO()[i]
													.getAttributeValue("pk_ct_manage_b"))) {
										tempMVO.getChildrenVO()[i]
												.setAttributeValue(
														"isSaveForced",
														UFBoolean.TRUE);
									}
								}
							}
							alRet = (ArrayList) nc.ui.pub.pf.PfUtilClient
									.processAction("VALIDATE", m_sBillType,
											getClientEnvironment().getDate()
													.toString(), tempMVO,
											tempMVO);
						} catch (Exception ex) {
							if (ex instanceof BusinessException)
								throw (BusinessException) ex;
							else
								throw new BusinessException(ex);
						}
					}
				} else {
					throw e;
				}
			}
			m_bIsNeedReInit[TabState.EXEC] = true; // 执行过程更新，需重新载入数据

			mVO.getParentVO().setAttributeValue("ctflag",
					new Integer(BillState.VALIDATE));
			mVO.getParentVO().setAttributeValue("actualvalidate",
					getClientEnvironment().getDate());

			getCtBillListPanel().getHeadBillModel().setValueAt(
					getClientEnvironment().getDate(), m_iId - 1,
					"actualvalidate");
			getCtBillCardPanel().getHeadItem("actualvalidate").setValue(
					getClientEnvironment().getDate());

			getCtBillCardPanel().setHeadItem("ctflag",
					new Integer(BillState.VALIDATE));
			getCtBillListPanel()
					.getHeadBillModel()
					.setValueAt(
							nc.ui.ml.NCLangRes.getInstance().getStrByID(
									"4020const", BillState.STATERESID_VALIDATE),
							m_iId - 1, "ctflag");
			// modified by lirr 2009-8-24上午10:42:00 刷新ts不再走后台，在动作中返回
			if (alRet != null && alRet.size() > 0)
				freshStatusTs(mVO, (String) alRet.get(1));
			ExtendManageVO voCur = (ExtendManageVO) m_vBillVO.get(m_iId - 1);
			voCur.setParentVO(mVO.getParentVO());
			// voCur.setTableVO("exec", mVO.getManageExecs());
			// modified by lirr 2009-9-14下午03:38:19
			CTTool.setExecVO(voCur);
			// m_vBillVO.remove(m_iId - 1);
			// m_vBillVO.insertElementAt(voCur, m_iId - 1);
			getCtBillCardPanel().setBillValueVO(voCur);
			setIdtoName();
			getCtBillCardPanel().updateValue();
			if (!m_bIsCard) {
				getCtBillListPanel().setBodyValueVO("exec",
						mVO.getManageExecs());
			}

			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"4020pub", "UPP4020pub-000194")/*
													 * @res "实际生效"
													 */);

		} catch (java.sql.SQLException e) {
			MessageDialog.showErrorDlg(this,
					nc.ui.ml.NCLangRes.getInstance().getStrByID("4020pub",
							"UPP4020pub-000066")/* @res "数据库操作错误" */, e
							.getMessage());

		} catch (java.rmi.RemoteException e) {
			MessageDialog.showErrorDlg(this,
					nc.ui.ml.NCLangRes.getInstance().getStrByID("4020pub",
							"UPP4020pub-000067")/* @res "远程调用失败" */, e
							.getMessage());

		} catch (BusinessException e) {
			MessageDialog.showHintDlg(this,
					nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON",
							"UPPSCMCommon-000297")/* @res "业务错误" */, e
							.getMessage());

		} catch (Exception e) {
			MessageDialog.showErrorDlg(this, null, e.getMessage());
			reportException(e);

		}

	}

	/**
	 * 此处插入方法说明。 创建日期：(2001-9-10 13:36:31)
	 */
	protected void qryCt(String sCtid) {
		try {
			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"4020pub", "UPP4020pub-000069")/* @res "请等待，正在查询合同……" */);

			ArrayList<ExtendManageVO> listVO = new ArrayList<ExtendManageVO>();
			StringBuffer sWhere = new StringBuffer(" ct_manage.pk_ct_manage='"
					+ sCtid + "' ");
			sWhere.append(" AND ct_type.nbusitype=");
			sWhere.append(m_iCtType);
			if (m_UFbIfEarly.booleanValue())
				sWhere.append(" and ct_manage.ifearly='Y'");
			else
				sWhere.append(" and ct_manage.ifearly='N'");

			// modified by liuzy 2008-04-28 碧桂园查询改造
			// ManageVO[] arrMangevos = ContractQueryHelper.queryBill(m_sPkCorp,
			// sWhere.toString());
			ManageVO[] arrMangevos = ContractQueryHelper.queryAllHeadData(
					m_sPkCorp, sWhere.toString());
			if (arrMangevos == null || arrMangevos.length == 0) {
				this.showHintMessage(nc.ui.ml.NCLangRes.getInstance()
						.getStrByID("4020pub", "UPP4020pub-000070")/*
																	 * @res
																	 * "没有找到符合条件的数据"
																	 */);
				return;
			}

			/** *****************追加权限操作V51********************** */
			SCMQueryConditionDlg qrydlg = new SCMQueryConditionDlg(this);
			if (qrydlg.getAllTempletDatas() == null
					|| qrydlg.getAllTempletDatas().length <= 0)
				qrydlg.setTempletID(m_sPkCorp, m_sNodeCode, m_sPkUser, null);
			String sPkCorp = null;
			if (arrMangevos[0].getPk_corp() == null) {
				this.showHintMessage(nc.ui.ml.NCLangRes.getInstance()
						.getStrByID("4020pub", "UPP4020pub-000070")/*
																	 * @res
																	 * "没有找到符合条件的数据"
																	 */);
				return;
			}
			sPkCorp = arrMangevos[0].getPk_corp();
			String[] refcodes = getDataPowerFieldFromDlgNotByProp(qrydlg,
					false, null);
			qrydlg.setCorpRefs("ct_manage.pk_corp", refcodes);// 设置数据权限字段
			ConditionVO[] cons = qrydlg.getDataPowerConVOs(sPkCorp, refcodes);
			String swhere = null;
			if (cons != null && cons.length > 0)
				swhere = qrydlg.getWhereSQL(cons);
			if (swhere != null && swhere.trim().length() > 0)
				sWhere.append(" and " + swhere);

			// modified by liuzy 2008-04-28 碧桂园查询改造
			// arrMangevos = ContractQueryHelper.queryBill(m_sPkCorp, sWhere
			// .toString());
			if (arrMangevos == null || arrMangevos.length == 0) {
				this.showHintMessage(nc.ui.ml.NCLangRes.getInstance()
						.getStrByID("4020pub", "UPT4020pub-000230")/*
																	 * @res
																	 * "没有察看单据的权限"
																	 */);
				return;
			}
			/** *************************************** */
			execFormularAfterQuery(arrMangevos);
			for (int k = 0; k < arrMangevos.length; k++) {
				ExtendManageVO voExtend = new ExtendManageVO();
				voExtend.setParentVO(arrMangevos[k].getParentVO());
				voExtend.setTableVO("table", arrMangevos[k].getChildrenVO());
				voExtend.setTableVO("term", arrMangevos[k].getTermBb4s());
				voExtend.setTableVO("cost", arrMangevos[k].getExpBb3s());
				voExtend.setTableVO("note", arrMangevos[k].getMemoraBb2s());
				voExtend.setTableVO("history", arrMangevos[k].getChangeBb1s());
				voExtend.setTableVO("exec", arrMangevos[k].getManageExecs());

				listVO.add(voExtend);
			}
			if (listVO != null && listVO.size() > 0) {
				m_vBillVO.clear();
				for (int i = 0; i < listVO.size(); i++) {
					m_vBillVO.add(listVO.get(i));
				}
				// 判断是在卡片模式下，还是在列表模式下
				m_bIsFirstClick = true; // 标志是第一次点击
				// 切换到列表模式下
				onList();
				m_iId = 1;
				m_bChangeFlag = false; // 标志合同不可变更
				// 所有Table都需要重新载入数据
				for (int i = 1; i < 6; i++)
					m_bIsNeedReInit[i] = true;

				setCardVOData(); // 重新初始卡片数据
				// 切换回卡片
				onList();

				/* @res "查询结束，共查到{0}条合同！" */
				String sMessage = nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"4020pub",
						"UPP4020pub-000071",
						null,
						new String[] { CommonConstant.BEGIN_MARK
								+ listVO.size() + CommonConstant.END_MARK });
				showHintMessage(sMessage);

			} else {
				String sMessage = nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"4020pub", "UPP4020pub-000091")/*
														 * @res
														 * "对不起，未查到符合条件的合同！"
														 */;
				showHintMessage(sMessage);
				MessageDialog.showHintDlg(this, null, sMessage);
			}
		} catch (Exception e) {
			nc.vo.scm.pub.SCMEnv.out(e);
			MessageDialog.showHintDlg(this, null, nc.ui.ml.NCLangRes
					.getInstance().getStrByID("4020pub", "UPP4020pub-000073")/*
																				 * @res
																				 * "合同查询出错！"
																				 */
					+ "\n" + e.getMessage());
		}

	}

	/**
	 * 此处插入方法说明。 功能： 参数： 返回： 例外： 日期：(2002-9-5 14:58:46) 修改日期，修改人，修改原因，注释标志：
	 * 
	 * @param iBusyType
	 *            int
	 */
	protected void setCtType(int iCtType) {
		m_iCtType = iCtType;
	}

	/**
	 * 此处插入方法说明。 功能： 参数： 返回： 例外： 日期：(2002-9-13 13:15:44) 修改日期，修改人，修改原因，注释标志：
	 * 
	 * @param bIfEarly
	 *            boolean
	 */
	protected void setIfEarly(UFBoolean UFbIfEarly) {
		m_UFbIfEarly = UFbIfEarly;
	}

	/**
	 * 此处插入方法说明。 功能： 参数： 返回： 例外： 日期：(2002-9-6 10:36:30) 修改日期，修改人，修改原因，注释标志：
	 * 
	 * @param sNodeCode
	 *            java.lang.String
	 */
	protected void setNodeCode(String sNodeCode) {
		m_sNodeCode = sNodeCode;
	}

	/**
	 * 创建日期：(2003-3-6 10:53:02) 作者：赵勋平 修改日期： 修改人： 修改原因： 算法说明：
	 * 
	 * @param newPrimaryKeyName
	 *            java.lang.String
	 */
	protected void setCtPrimaryKeyName(java.lang.String newCtPrimaryKeyName) {
		sCtPrimaryKeyName = newCtPrimaryKeyName;
	}

	/**
	 * 创建日期：(2003-3-6 10:53:35) 作者：赵勋平 修改日期： 修改人： 修改原因： 算法说明：
	 * 
	 * @param newBillCodeKeyName
	 *            java.lang.String
	 */
	protected void setCtBillCodeKeyName(java.lang.String newCtBillCodeKeyName) {
		sCtBillCodeKeyName = newCtBillCodeKeyName;
	}

	/**
	 * 创建日期：(2003-3-6 10:53:35) 作者：赵勋平 修改日期： 修改人： 修改原因： 算法说明：
	 * 
	 * @return java.lang.String
	 */
	protected java.lang.String getCtBillCodeKeyName() {
		if (null == sCtBillCodeKeyName)
			sCtBillCodeKeyName = "";
		return sCtBillCodeKeyName;
	}

	/**
	 * 创建日期：(2003-3-6 10:53:02) 作者：赵勋平 修改日期： 修改人： 修改原因： 算法说明：
	 * 
	 * @return java.lang.String
	 */
	protected java.lang.String getCtPrimaryKeyName() {
		if (null == sCtPrimaryKeyName)
			sCtPrimaryKeyName = "";
		return sCtPrimaryKeyName;
	}

	/**
	 * 创建者：仲瑞庆 功能： 参数： 返回： 例外： 日期：(2001-11-8 19:47:29) 修改日期，修改人，修改原因，注释标志：
	 * 
	 * @return nc.ui.pub.bill.BillData
	 * @param bdData
	 *            nc.ui.pub.bill.BillData
	 */
	protected BillData changeBillDataByUserDef(BillData bdData) {
		// 进行自定义项定义用
		DefVO[] defs = null;

		/*
		 * 邵兵 修改自定义项加载方式 2005-06-23 自定义表头、表体DefVO[]作静态缓存 这样在用户登陆期间，自定义数据只做一次后台查询
		 * 并且只被初始化一次，其他的供应链节点都可使用
		 */
		defs = DefSetTool.getDefHead(m_sPkCorp, m_sBillType);
		if ((defs != null)) {
			bdData.updateItemByDef(defs, "def", true);
		}

		// 表体
		// 查得对应于公司的该单据的自定义项设置
		defs = DefSetTool.getDefBody(m_sPkCorp, m_sBillType);
		if ((defs == null)) {
			return bdData;
		}

		bdData.updateItemByDef(defs, "def", false);

		return bdData;
	}

	/**
	 * 创建者：仲瑞庆 功能： 参数： 返回： 例外： 日期：(2001-11-8 19:47:29) 修改日期，修改人，修改原因，注释标志：
	 * 
	 * @return nc.ui.pub.bill.BillData
	 * @param oldBillData
	 *            nc.ui.pub.bill.BillData
	 */
	protected BillListData changeBillListDataByUserDef(BillListData oldBillData) {
		// 进行自定义项定义用
		/*
		 * 董广群 修改自定义项加载方式 2006-03-16 自定义表头、表体DefVO[]作静态缓存
		 * 这样在用户登陆期间，自定义数据只做一次后台查询 并且只被初始化一次，其他的供应链节点都可使用
		 */
		DefVO[] defs = null;
		// 表头
		// 查得对应于公司的该单据的自定义项设置  
		defs = DefSetTool.getDefHead(m_sPkCorp, m_sBillType);//nc.ui.bd.service.BDDef.queryDefVO("供应链/ARAP单据头",m_sPkCorp);
		// 
		// /*-=notranslate=-*/
		if ((defs != null)) {
			oldBillData.updateItemByDef(defs, "def", true);
		}

		// 表体
		// 查得对应于公司的该单据的自定义项设置
		defs = DefSetTool.getDefBody(m_sPkCorp, m_sBillType);// nc.ui.bd.service.BDDef.queryDefVO("供应链/ARAP单据体",
		// m_sPkCorp);
		// /*-=notranslate=-*/
		if ((defs == null)) {
			return oldBillData;
		}

		oldBillData.updateItemByDef(defs, "def", false);

		return oldBillData;
	}
	

	/**
	 * 创建日期：(2003-6-16 15:01:57) 作者：王亮 修改日期： 修改人： 修改原因： 算法说明：
	 */
	public void initialize() {
		setCtPrimaryKeyName("pk_ct_manage");
		setCtBillCodeKeyName("ct_code");

	}

	/**
	 * 初始化前改变界面。 创建日期：(2001-11-15 9:20:13)
	 * 
	 * @param bdData
	 *            nc.ui.pub.bill.BillData
	 */
	public void setCardPanel(BillData bdData) {
		setCardPanelByPara(bdData);
		setCardPanelByOther(bdData);
	}

	/**
	 * 此处插入方法说明。 功能描述: 输入参数: 返回值: 异常处理: 日期:
	 */
	protected void setCardPanelActionTable() {
		// 定义动作顺序表
		ArrayList alActionTable = null;

		// 折辅汇率变
		alActionTable = new ArrayList();
		// 折本汇率
		// alActionTable.add("currrate");
		// getCtBillCardPanel().setActionTable("astcurrate", alActionTable);

		// 含税单价变
		alActionTable = new ArrayList();
		// 折本折辅汇率
		// alActionTable.add("astcurrate");
		// getCtBillCardPanel().setActionTable("oritaxprice", alActionTable);
		alActionTable.add("currrate");
		getCtBillCardPanel().setActionTable("oritaxprice", alActionTable);

		// 无税单价变
		alActionTable = new ArrayList();
		// 折本折辅汇率
		// alActionTable.add("astcurrate");
		// getCtBillCardPanel().setActionTable("oriprice", alActionTable);
		alActionTable.add("currrate");
		getCtBillCardPanel().setActionTable("oriprice", alActionTable);

		// 金额变
		alActionTable = new ArrayList();
		// 折本折辅汇率
		// alActionTable.add("astcurrate");
		// getCtBillCardPanel().setActionTable("orisum", alActionTable);
		alActionTable.add("currrate");
		getCtBillCardPanel().setActionTable("orisum", alActionTable);

		// 税额变
		alActionTable = new ArrayList();
		// 折本折辅汇率
		// alActionTable.add("astcurrate");
		// getCtBillCardPanel().setActionTable("oritaxmny", alActionTable);
		alActionTable.add("currrate");
		getCtBillCardPanel().setActionTable("oritaxmny", alActionTable);

		// 价税合计变
		alActionTable = new ArrayList();
		// 折本折辅汇率
		// alActionTable.add("astcurrate");
		// getCtBillCardPanel().setActionTable("oritaxsummny", alActionTable);
		alActionTable.add("currrate");
		getCtBillCardPanel().setActionTable("oritaxsummny", alActionTable);

		// 税率变
		alActionTable = new ArrayList();
		// 折本折辅汇率
		// alActionTable.add("astcurrate");
		// getCtBillCardPanel().setActionTable("taxration", alActionTable);
		alActionTable.add("currrate");
		getCtBillCardPanel().setActionTable("taxration", alActionTable);

		// 数量变
		alActionTable = new ArrayList();
		// 折本折辅汇率
		// alActionTable.add("astcurrate");
		// getCtBillCardPanel().setActionTable("amount", alActionTable);
		alActionTable.add("currrate");
		getCtBillCardPanel().setActionTable("amount", alActionTable);

		// 辅数量变
		alActionTable = new ArrayList();
		// 数量
		alActionTable.add("amount");
		getCtBillCardPanel().setActionTable("astnum", alActionTable);

		// 换算率变
		alActionTable = new ArrayList();
		// 数量
		alActionTable.add("amount");
		getCtBillCardPanel().setActionTable("transrate", alActionTable);

		// 辅计量单位编码变
		alActionTable = new ArrayList();
		// 换算率
		alActionTable.add("transrate");
		getCtBillCardPanel().setActionTable("astmeascode", alActionTable);

		// 存货编码变
		alActionTable = new ArrayList();
		// 辅计量单位编码
		alActionTable.add("astmeascode");
		getCtBillCardPanel().setActionTable("invcode", alActionTable);
	}

	/**
	 * 由其它原因改变界面。 创建日期：(2001-11-15 9:18:13)
	 * 
	 * @param bdData
	 *            nc.ui.pub.bill.BillData
	 */
	protected void setCardPanelByOther(BillData bdData) {

		// 获得单据元素对应的控件
		UIComboBox comContractStatusItem = (UIComboBox) bdData.getHeadItem(
				"ctflag").getComponent();
		comContractStatusItem.setTranslate(true);

		comContractStatusItem.addItem(nc.ui.ml.NCLangRes.getInstance()
				.getStrByID("4020const", BillState.STATERESID_FREE));
		comContractStatusItem.addItem(nc.ui.ml.NCLangRes.getInstance()
				.getStrByID("4020const", BillState.STATERESID_AUDIT));
		comContractStatusItem.addItem(nc.ui.ml.NCLangRes.getInstance()
				.getStrByID("4020const", BillState.STATERESID_VALIDATE));
		comContractStatusItem.addItem(nc.ui.ml.NCLangRes.getInstance()
				.getStrByID("4020const", BillState.STATERESID_ABOLISH));
		comContractStatusItem.addItem(nc.ui.ml.NCLangRes.getInstance()
				.getStrByID("4020const", BillState.STATERESID_FREEZE));
		comContractStatusItem.addItem(nc.ui.ml.NCLangRes.getInstance()
				.getStrByID("4020const", BillState.STATERESID_TERMINATE));
		comContractStatusItem.addItem(nc.ui.ml.NCLangRes.getInstance()
				.getStrByID("4020const", BillState.STATERESID_CHECKGOING));

		comContractStatusItem.setSelectedIndex(0);
		bdData.getHeadItem("ctflag").setWithIndex(true);

		// 设置激活状态
		UIComboBox comActiveItem = (UIComboBox) bdData
				.getHeadItem("activeflag").getComponent();
		comActiveItem.setTranslate(true);
		comActiveItem.addItem(nc.ui.ml.NCLangRes.getInstance().getStrByID(
				"SCMCOMMON", "UPPSCMCommon-000244")/* @res "是" */);
		comActiveItem.addItem(nc.ui.ml.NCLangRes.getInstance().getStrByID(
				"SCMCOMMON", "UPPSCMCommon-000108")/* @res "否" */);
		comActiveItem.setSelectedIndex(0);
		bdData.getHeadItem("activeflag").setWithIndex(true);

	}

	/**
	 * 根据参数改变界面。 创建日期：(2001-9-27 16:13:57)
	 */
	protected void setCardPanelByPara(BillData bdData) {
		try {
			// 修改自定义项
			bdData = changeBillDataByUserDef(bdData);
		} catch (Exception e) {
		}
	}

	/**
	 * 此处插入方法说明。 功能描述: 输入参数: 返回值: 异常处理: 日期:
	 */
	protected void setCardPanelFormular() {
		// 定义计算公式
		ArrayList alFormula = null;

		// 原币含税单价变
		alFormula = new ArrayList();
		// 原币价税合计=数量*原币含税单价
		alFormula.add(new String[] { "oritaxsummny", "amount*oritaxprice" });
		// 原币税额=税率*原币价税合计/(1+税率)
		alFormula.add(new String[] { "oritaxmny",
				"taxration/100*oritaxsummny/(1+taxration/100)" });
		// 原币金额=原币价税合计-原币税额
		alFormula.add(new String[] { "orisum", "oritaxsummny-oritaxmny" });
		// 原币无税单价=原币含税单价/（1+税率）
		alFormula.add(new String[] { "oriprice",
				"oritaxprice/(1+taxration/100)" });
		getCtBillCardPanel().setFormula("oritaxprice", alFormula);

		// 原币无税单价变
		alFormula = new ArrayList();
		// 原币金额=数量*原币无税单价
		alFormula.add(new String[] { "orisum", "amount*oriprice" });
		// 原币税额=税率*原币金额
		alFormula.add(new String[] { "oritaxmny", "taxration/100*orisum" });
		// 原币价税合计=原币金额+税额
		alFormula.add(new String[] { "oritaxsummny", "orisum+oritaxmny" });
		// 原币含税单价=原币无税单价*（1+税率）
		alFormula.add(new String[] { "oritaxprice",
				"oriprice*(1+taxration/100)" });
		getCtBillCardPanel().setFormula("oriprice", alFormula);

		// 原币金额变
		alFormula = new ArrayList();
		// 原币无税单价=原币金额/数量
		alFormula.add(new String[] { "oriprice", "orisum/amount" });
		// 原币税额=税率*原币金额
		alFormula.add(new String[] { "oritaxmny", "taxration/100*orisum" });
		// 原币价税合计=原币金额+原币税额
		alFormula.add(new String[] { "oritaxsummny", "orisum+oritaxmny" });
		// 原币含税单价=原币价税合计/数量
		alFormula.add(new String[] { "oritaxprice",
				"oriprice*(1+taxration/100)" });
		getCtBillCardPanel().setFormula("orisum", alFormula);

		// 原币税额变
		alFormula = new ArrayList();
		// 原币价税合计=原币金额+原币税额
		alFormula.add(new String[] { "oritaxsummny", "orisum+oritaxmny" });
		// 原币含税单价=原币价税合计/数量
		alFormula.add(new String[] { "oritaxprice", "oritaxsummny/amount" });
		getCtBillCardPanel().setFormula("oritaxmny", alFormula);

		// 原币价税合计变
		alFormula = new ArrayList();
		// 原币含税单价=原币价税合计/数量
		alFormula.add(new String[] { "oritaxprice", "oritaxsummny/amount" });
		// 原币税额=税率*原币价税合计/(1+税率)
		alFormula.add(new String[] { "oritaxmny",
				"taxration/100*oritaxsummny/(1+taxration/100)" });
		// 金额=价税合计-税额
		alFormula.add(new String[] { "orisum", "oritaxsummny-oritaxmny" });
		// 无税单价=含税单价/（1+税率）
		alFormula.add(new String[] { "oriprice",
				"oritaxprice/(1+taxration/100)" });
		getCtBillCardPanel().setFormula("oritaxsummny", alFormula);

		// 取得计算合同价格优先规则参数
		// 依参数值不同,定义不同的计算公式
		if (sContractPriceRule.equals("含税单价")) { /*-=notranslate=-*/

			// 数量变
			alFormula = new ArrayList();
			// 价税合计=数量*含税单价
			alFormula
					.add(new String[] { "oritaxsummny", "amount*oritaxprice" });
			// 税额=税率*价税合计/(1+税率)
			alFormula.add(new String[] { "oritaxmny",
					"taxration/100*oritaxsummny/(1+taxration/100)" });
			// 金额=价税合计-税额
			alFormula.add(new String[] { "orisum", "oritaxsummny-oritaxmny" });
			// 无税单价=含税单价/（1+税率）
			alFormula.add(new String[] { "oriprice",
					"oritaxprice/(1+taxration/100)" });
			getCtBillCardPanel().setFormula("amount", alFormula);

			// 税率变
			alFormula = new ArrayList();
			// 税额=税率*价税合计/(1+税率)
			alFormula.add(new String[] { "oritaxmny",
					"taxration/100*oritaxsummny/(1+taxration/100)" });
			// 金额=价税合计-税额
			alFormula.add(new String[] { "orisum", "oritaxsummny-oritaxmny" });
			// 无税单价=含税单价/（1+税率）
			alFormula.add(new String[] { "oriprice",
					"oritaxprice/(1+taxration/100)" });
			getCtBillCardPanel().setFormula("taxration", alFormula);

		} else if (sContractPriceRule.equals("无税单价")) { /*-=notranslate=-*/

			// 数量变
			alFormula = new ArrayList();
			// 金额=数量*无税单价
			alFormula.add(new String[] { "orisum", "amount*oriprice" });
			// 税额=税率*金额
			alFormula.add(new String[] { "oritaxmny", "taxration/100*orisum" });
			// 价税合计=金额+税额
			alFormula.add(new String[] { "oritaxsummny", "orisum+oritaxmny" });
			// 含税单价=无税单价*（1+税率）
			alFormula.add(new String[] { "oritaxprice",
					"oriprice*(1+taxration/100)" });
			getCtBillCardPanel().setFormula("amount", alFormula);

			// 税率变
			alFormula = new ArrayList();
			// 税额=税率*金额
			alFormula.add(new String[] { "oritaxmny", "taxration/100*orisum" });
			// 价税合计=金额+税额
			alFormula.add(new String[] { "oritaxsummny", "orisum+oritaxmny" });
			// 含税单价=无税单价*（1+税率）
			alFormula.add(new String[] { "oritaxprice",
					"oriprice*(1+taxration/100)" });
			getCtBillCardPanel().setFormula("taxration", alFormula);
		}

		try {
			// 折算模式
			// true : 乘法模式
			// false: 除法模式
			currArith = nc.ui.scm.pub.PubSetUI.getCurrArith(m_sPkCorp);
			// 核算方式
			// bCalType = currArith.isBlnLocalFrac(); // 真为 主辅币核算 ,假为 单主币核算

			// String sFracCurrPk = currArith.getFracCurrPK();
			// CurrinfoVO fracVO = currArith.getCurrinfoVO(sFracCurrPk,
			// currArith
			// .getLocalCurrPK());
			// modified by lirr 修改原因：uap删除了原方法
			// deleted by lirr 2008-8-20 根据5.5需求删除辅币信息

			CurrParamQuery cpq = CurrParamQuery.getInstance(); // 核算方式
			bCalType = cpq.isBlnLocalFrac(m_sPkCorp); // 真为 主辅币核算 ,假为 单主币核算

			String sFracCurrPk = cpq.getFracCurrPK(m_sPkCorp);
			CurrinfoVO fracVO = currArith.getCurrinfoVO(sFracCurrPk, cpq
					.getLocalCurrPK(m_sPkCorp)); // 辅币对本币的折算模式
			bFracmode = fracVO.getConvmode().booleanValue();

		} catch (Exception e) {
			showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"SCMCOMMON", "UPPSCMCommon-000116")/* @res "公式置入错误！" */);
		}
	}

	/**
	 * 此处插入方法说明。 功能描述: 输入参数: 返回值: 异常处理: 日期:
	 * 
	 * @param bdData
	 *            nc.ui.pub.bill.BillListData
	 */
	protected void setListPanelByPara(BillListData bdData) {

		// 获得单据元素对应的控件
		UIComboBox comContractStatusItem = (UIComboBox) bdData.getHeadItem(
				"ctflag").getComponent();
		comContractStatusItem.setTranslate(true);

		comContractStatusItem.addItem(nc.ui.ml.NCLangRes.getInstance()
				.getStrByID("4020const", BillState.STATERESID_FREE));
		comContractStatusItem.addItem(nc.ui.ml.NCLangRes.getInstance()
				.getStrByID("4020const", BillState.STATERESID_AUDIT));
		comContractStatusItem.addItem(nc.ui.ml.NCLangRes.getInstance()
				.getStrByID("4020const", BillState.STATERESID_VALIDATE));
		comContractStatusItem.addItem(nc.ui.ml.NCLangRes.getInstance()
				.getStrByID("4020const", BillState.STATERESID_ABOLISH));
		comContractStatusItem.addItem(nc.ui.ml.NCLangRes.getInstance()
				.getStrByID("4020const", BillState.STATERESID_FREEZE));
		comContractStatusItem.addItem(nc.ui.ml.NCLangRes.getInstance()
				.getStrByID("4020const", BillState.STATERESID_TERMINATE));
		comContractStatusItem.addItem(nc.ui.ml.NCLangRes.getInstance()
				.getStrByID("4020const", BillState.STATERESID_CHECKGOING));

		comContractStatusItem.setSelectedIndex(0);
		bdData.getHeadItem("ctflag").setComponent(comContractStatusItem);
		bdData.getHeadItem("ctflag").setWithIndex(true);

		// 设置激活状态
		UIComboBox comActiveItem = (UIComboBox) bdData
				.getHeadItem("activeflag").getComponent();
		comActiveItem.setTranslate(true);
		comActiveItem.addItem(nc.ui.ml.NCLangRes.getInstance().getStrByID(
				"SCMCOMMON", "UPPSCMCommon-000244")/* @res "是" */);
		comActiveItem.addItem(nc.ui.ml.NCLangRes.getInstance().getStrByID(
				"SCMCOMMON", "UPPSCMCommon-000108")/* @res "否" */);
		comActiveItem.setSelectedIndex(0);
		bdData.getHeadItem("activeflag").setComponent(comActiveItem);
		bdData.getHeadItem("activeflag").setWithIndex(true);
	}

	/**
	 * 由于合同有采购、销售、其他等不同的种类， 所以即使是同一个存货ID,对应的默认辅单位也会不一样， 从而对应的换算率也不相同。 作者: 赵勋平
	 * 功能描述: 返回该存货的换算率 输入参数: strInvid java.lang.String 存货id 返回值: UFDouble 异常处理:
	 * 日期:2003-6-25 16:20
	 */
	public UFDouble getTransRate(String strInvid) {
		if (m_UFTransRate == null) {
		}
		return m_UFTransRate;
	}

	/**
	 * 函数的功能、用途、对属性的更改，以及函数执行前后对象的状态。 表体自由项修改
	 * 
	 * @param 参数说明
	 * @return 返回值
	 * @exception 异常描述
	 * @see 需要参见的其它内容
	 * @since 从类的那一个版本，此方法被添加进来。（可选）
	 * @author zhongyue
	 * @param row
	 *            int
	 * @param col
	 *            int
	 */
	protected void afterFreeItemEdit(BillModel bm, int iRow) {

		try {
			// 得到自由项vo
			FreeItemRefPane freeItemRef = (FreeItemRefPane) bm.getItemByKey(
					"vfree0").getComponent();
			FreeVO voFree = freeItemRef.getFreeVO();

			if (voFree != null) {
				Object oFreexName = null;
				Object oFreexValue = null;

				// 将自由项的内容拆分为自由项1-5
				for (int i = 0; i < 5; i++) {
					String str1 = "vfreename" + new Integer(i + 1).toString();
					String str2 = "vfree" + new Integer(i + 1).toString();

					oFreexName = voFree.getAttributeValue(str1);
					oFreexValue = voFree.getAttributeValue(str2);

					if (isNull(oFreexValue)) {
						bm.setValueAt(null, iRow, str2);
					} else {
						if (oFreexName == null) {
							oFreexName = "";
						} else {
							oFreexName = oFreexName.toString().trim();
						}
						oFreexValue = oFreexValue.toString().trim();
						// vfreexvalue = "[" + vfreexname +":"+ vfreexvalue +
						// "]";
						bm.setValueAt(oFreexValue, iRow, str2);
					}
				}
			}

		} catch (Exception e) {
			nc.vo.scm.pub.SCMEnv.out(e);
		}
	}

	/**
	 * 此处插入方法说明。 功能描述:合同类型 作者：王亮 输入参数: 返回值: 异常处理: 日期:(2003-7-3 9:34:29)
	 */
	public void afterCtTypeEdit() {

		String pk = ((UIRefPane) getCtBillCardPanel().getHeadItem("ct_type")
				.getComponent()).getRefPK();

		if (pk == null || pk.length() <= 0) {
			showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"4020pub", "UPP4020pub-000092")/* @res "合同类型不能为空" */);
			return;
		}
		Object oRefValue = ((UIRefPane) getCtBillCardPanel().getHeadItem(
				"ct_type").getComponent()).getRefValue("ninvctlstyle");

		if (oRefValue != null) {

			int iInvctlstyle = Integer.parseInt(oRefValue.toString());

			// 如果选择了存货合同类型，则判断模板设置的存货编码是否可见，存货分类编码、名称是否必输
			if (iInvctlstyle == 0) { // 如果invcode不可见
				if (getCtBillCardPanel().getBodyItem(CTTableCode.BASE,
						"invcode").isShow() == false) {
					showWarningMessage(nc.ui.ml.NCLangRes.getInstance()
							.getStrByID("4020pub", "UPP4020pub-000093")/*
																		 * @res
																		 * "若选择存货控制类型为存货的合同类型，表体存货编码应该可见，请重新设置单据模版！"
																		 */);
				}
				// 如果invclasscode必输
				if (getCtBillCardPanel().getBodyItem(CTTableCode.BASE,
						"invclasscode").isNull() == true
						|| getCtBillCardPanel().getBodyItem(CTTableCode.BASE,
								"invclassname").isNull() == true) {
					showWarningMessage(nc.ui.ml.NCLangRes.getInstance()
							.getStrByID("4020pub", "UPP4020pub-000094")/*
																		 * @res
																		 * "若选择存货控制类型为存货的合同类型，表体存货分类编码或存货分类名称不应为必输项，请重新设置单据模版！"
																		 */);
				}

			}

			// 如果选择了存货分类合同类型，则判断模板设置的存货分类编码是否可见，存货编码、名称是否必输
			else if (iInvctlstyle == 1) { // 如果invclasscode不可见
				if (getCtBillCardPanel().getBodyItem(CTTableCode.BASE,
						"invclasscode").isShow() == false) {
					showWarningMessage(nc.ui.ml.NCLangRes.getInstance()
							.getStrByID("4020pub", "UPP4020pub-000095")/*
																		 * @res
																		 * "若选择存货控制类型为存货大类的合同类型，表体存货分类编码应该可见，请重新设置单据模版！"
																		 */);
				}
				// 如果invcode必输
				if (getCtBillCardPanel().getBodyItem(CTTableCode.BASE,
						"invcode").isNull() == true
						|| getCtBillCardPanel().getBodyItem(CTTableCode.BASE,
								"invname").isNull() == true
						|| getCtBillCardPanel().getBodyItem(CTTableCode.BASE,
								"spec").isNull() == true
						|| getCtBillCardPanel().getBodyItem(CTTableCode.BASE,
								"mod").isNull() == true
						|| getCtBillCardPanel().getBodyItem(CTTableCode.BASE,
								"measname").isNull() == true) {
					showWarningMessage(nc.ui.ml.NCLangRes.getInstance()
							.getStrByID("4020pub", "UPP4020pub-000096")/*
																		 * @res
																		 * "若选择存货控制类型为存货大类的合同类型，表体存货编码或存货名称或型号或规格或计量单位不应为必输项，请重新设置单据模版！"
																		 */);
				}

			}
			// 如果合同类型的存货控制方式为空。将存货分类和存货都设置为空，不可编辑。
			else if (iInvctlstyle == 2) {
				// 设置存货分类编码，存货分类名称为空
				int iRowLen = getCtBillCardPanel().getRowCount();
				for (int i = 0; i < iRowLen; i++) {
					getCtBillCardPanel()
							.setBodyValueAt(null, i, "invclasscode");
					getCtBillCardPanel()
							.setBodyValueAt(null, i, "invclassname");
					getCtBillCardPanel().setBodyValueAt(null, i, "invclid");
					// 设置存货编码，存货名称,型号，规格，数量,计量单位为空
					getCtBillCardPanel().setBodyValueAt(null, i, "invcode");
					getCtBillCardPanel().setBodyValueAt(null, i, "invname");
					getCtBillCardPanel().setBodyValueAt(null, i, "invid");
					getCtBillCardPanel().setBodyValueAt(null, i, "spec");
					getCtBillCardPanel().setBodyValueAt(null, i, "mod");
					getCtBillCardPanel().setBodyValueAt(null, i, "measname");
					// getCtBillCardPanel().setBodyValueAt(null, i,
					// "astmeascode");
					getCtBillCardPanel().setBodyValueAt(null, i, "astmeasname");
					getCtBillCardPanel().setBodyValueAt(null, i, "astnum");
					getCtBillCardPanel().setBodyValueAt(null, i, "transrate");
					getCtBillCardPanel().setBodyValueAt(null, i, "astmeasid");
					getCtBillCardPanel().setBodyValueAt(null, i, "measid");
					// added by lirr 20092009-6-29上午09:56:48 清空存货基本id

					getCtBillCardPanel().setBodyValueAt(null, i, "invbasid");

				}
			}

		}
		if (m_iCtType == CtType.OTHER) {
			String sCustType = "";
			String sRecorPay = "02";

			try {
				sRecorPay = ContractQueryHelper.queryRecorPayByCtType(pk);

				if ("01".equals(sRecorPay)) {
					sCustType = "   bd_cumandoc.custflag in ('0','2')";
				} else if ("02".equals(sRecorPay)) {
					sCustType = "   bd_cumandoc.custflag in ('1','3')";
				}
				((UIRefPane) getCtBillCardPanel().getHeadItem("custname")
						.getComponent())
						.setWhereString(" bd_cumandoc.pk_corp = '" + m_sPkCorp
								+ "' and " + sCustType);

			} catch (Exception e) {
				GenMethod.handleException(this, e.getMessage(), e);
				nc.vo.scm.pub.SCMEnv.out(e);
			}
		}

	}

	/**
	 * 功能描述:原币币种 编辑后处理 修改人：邵 兵 修改日期:(2005-06-21 9:34:29) 修改日期：2008-10-23
	 * 修改原因：参照生成合同 触发该事件时currID为空 李冉冉
	 */
	public void afterCurrNameEdit(boolean isFromBill) {

		ManageItemVO[] itemVOs = (ManageItemVO[]) getCtBillCardPanel()
				.getBillModel().getBodyValueVOs(ManageItemVO.class.getName());

		for (int i = 0; i < itemVOs.length; i++) { // 非新增行，则置为编辑状态
			if (itemVOs[i].getStatus() != 2)
				getCtBillCardPanel().getBillModel().setRowState(i, 2);
		}
		// added by lirr 2008-11-10 解决效率问题，只有汇率发生变化才重新计算表体
		String sOldCurrate = null;
		if (null != getCtBillCardPanel().getHeadItem("currrate")
				&& null != getCtBillCardPanel().getHeadItem("currrate")
						.getValue()) {
			sOldCurrate = getCtBillCardPanel().getHeadItem("currrate")
					.getValue();
		}
		// 清除折本汇率
		getCtBillCardPanel().getHeadItem("currrate").clearViewData();
		// getCtBillCardPanel().getHeadItem("astcurrate").clearViewData(); //
		// 折辅汇率

		/*
		 * // 修改后的原币币种ID String currID = ((UIRefPane)
		 * getCtBillCardPanel().getHeadItem(
		 * "currname").getComponent()).getRefPK();
		 */
		String currID = null;
		if (!isFromBill) {
			currID = ((UIRefPane) getCtBillCardPanel().getHeadItem("currname")
					.getComponent()).getRefPK();
		} else {
			currID = getCtBillCardPanel().getHeadItem("currname").getValue();
		}
		if (currID == null) {
			MessageDialog.showHintDlg(this, null, nc.ui.ml.NCLangRes
					.getInstance().getStrByID("scmcommon",
							"UPPSCMCommon-000015")/* @res "币种未定义，请先定义币种！" */);
			return;
		}
		getCtBillCardPanel().getHeadItem("currid").setValue(currID);

		// 获得此币种的小数位数
		int currDigit = 2;
		if (m_hCurrDigit.containsKey(currID)) {
			currDigit = ((Integer) m_hCurrDigit.get(currID)).intValue();
		}
		// 定义原币金额小数位数
		getCtBillCardPanel().getBodyItem(CTTableCode.BASE, "orisum")
				.setDecimalDigits(currDigit);
		getCtBillCardPanel().getBodyItem(CTTableCode.BASE, "oritaxmny")
				.setDecimalDigits(currDigit);
		getCtBillCardPanel().getBodyItem(CTTableCode.BASE, "oritaxsummny")
				.setDecimalDigits(currDigit);

		getCtBillListPanel().getBodyItem(CTTableCode.BASE, "orisum")
				.setDecimalDigits(currDigit);
		getCtBillListPanel().getBodyItem(CTTableCode.BASE, "oritaxmny")
				.setDecimalDigits(currDigit);
		getCtBillListPanel().getBodyItem(CTTableCode.BASE, "oritaxsummny")
				.setDecimalDigits(currDigit);

		// 合同费用页签中金额的精度
		getCtBillCardPanel().getBodyItem(CTTableCode.COST, "expsum")
				.setDecimalDigits(currDigit);
		getCtBillListPanel().getBodyItem(CTTableCode.COST, "expsum")
				.setDecimalDigits(currDigit);

		// 根据签定日期取得汇率
		String subDate = getCtBillCardPanel().getHeadItem("subscribedate")
				.getValue();
		if (subDate == null || subDate.trim().length() == 0) {
			MessageDialog.showHintDlg(this, null, nc.ui.ml.NCLangRes
					.getInstance().getStrByID("4020pub", "UPP4020pub-000097")/*
																				 * @res
																				 * "如果需要得到默认汇率，请先输入合同签订日期！"
																				 */);
			return;
		}
		try {
			BusinessCurrencyRateUtil currRateUtil = new BusinessCurrencyRateUtil(
					m_sPkCorp);
			// modified by lirr 2009-8-27下午08:26:09 修改方法

			/*
			 * m_dRate = PubSetUI.getBothExchRateValue(m_sPkCorp, currID, new
			 * UFDate( subDate));
			 */
			// m_iRateDigit = PubSetUI.getBothExchRateDigit(m_sPkCorp, currID);
			// m_bRateEnable = PubSetUI.getBothExchRateEditable(m_sPkCorp,
			// currID);
			setM_bRateEnable(currID);
			getCtBillCardPanel().getHeadItem("currrate").setEnabled(
					isM_bRateEnable());
			m_dRate = currRateUtil.getRate(currID, m_sLocalCurrID, new UFDate(
					subDate).toString());
		} catch (Exception e) {
			nc.vo.scm.pub.SCMEnv.out(e);
		}

		/*
		 * getCtBillCardPanel().getHeadItem("currrate").setDecimalDigits(
		 * m_iRateDigit[0]);
		 */
		// modified by lirr 2008-12-24 折本汇率精度新方法 since v55
		getCtBillCardPanel().getHeadItem("currrate").setDecimalDigits(
				getCurrateDigit(currID));
		// deleted by lirr 2008-8-20 根据5.5需求删除辅币信息
		// getCtBillCardPanel().getHeadItem("astcurrate").setDecimalDigits(
		// m_iRateDigit[1]);

		getCtBillCardPanel().getHeadItem("currrate").setValue(m_dRate);
		// getCtBillCardPanel().getHeadItem("astcurrate").setValue(m_dRate[1]);

		/*
		 * getCtBillCardPanel().getHeadItem("currrate").setEnabled(
		 * m_bRateEnable[0]);
		 */
		// getCtBillCardPanel().getHeadItem("astcurrate").setEnabled(
		// m_bRateEnable[1]);
		// modified by lirr 2008-11-22 修改原因：表体计算采用新方式
		// 置入主辅币计算公式
		// setFormularByCur(currID);
		// getCtBillCardPanel().calculateWhenHeadChanged("currrate");
		String sNewCurrate = getCtBillCardPanel().getHeadItem("currrate")
				.getValue();
		// modified by lirr 2008-11-22 折本汇率变化后不用原来的公式计算
		// 调用新calcCtLinePriceMny方式即可

		/*
		 * if(!sOldCurrate.equals(sNewCurrate)){
		 * //getCtBillCardPanel().getBillModel(CTTableCode.BASE).execFormulas(getFormularForNat(),0,getCtBillCardPanel().getRowCount());
		 * getCtBillCardPanel().getBillModel().execFormulas(getFormularForNat(sNewCurrate),
		 * 0,getCtBillCardPanel().getRowCount()); }
		 */

		// getCtBillCardPanel().calculateWhenHeadChanged("astcurrate");
		for (int i = 0; i < getCtBillCardPanel().getRowCount(); i++) {
			/*
			 * calcCtLinePriceMny(new BillEditEvent(this, null, null,
			 * "oritaxprice", i, BillItem.BODY));
			 */
			calcCtLinePriceMny(new BillEditEvent(this, null, null, "currrate",
					i, BillItem.BODY));

		}

	}

	/**
	 * 此处插入方法说明。 功能描述:汇率 作者：王亮 输入参数: 返回值: 异常处理: 日期:(2003-7-3 13:23:58)
	 */
	public void afterCurrrateEdit(nc.ui.pub.bill.BillEditEvent e) {
		if (e.getValue() == null
				|| new UFDouble(e.getValue().toString()).doubleValue() <= 0) {
			MessageDialog.showErrorDlg(this, null, nc.ui.ml.NCLangRes
					.getInstance().getStrByID("4020pub", "UPP4020pub-000098")/*
																				 * @res
																				 * "汇率不能为空，也不能小于等于0！"
																				 */);
			getCtBillCardPanel().getHeadItem("currrate").clearViewData();
		}

		ManageItemVO[] itemVOs = (ManageItemVO[]) getCtBillCardPanel()
				.getBillModel().getBodyValueVOs(ManageItemVO.class.getName());

		// 修改汇率后，将所有的非新增表体行置为修改状态（表体本币信息有修改）
		for (int i = 0; i < itemVOs.length; i++) { // 非新增行，则置为编辑状态
			if (itemVOs[i].getStatus() != 2)
				getCtBillCardPanel().getBillModel().setRowState(i, 2);
		}
		// added by lirr 2008-10-30 汇率变化后 标题行没有重算
		UFBoolean bOrimode = null;
		setFormularForNat(bOrimode);
		// getCtBillCardPanel().calculateWhenHeadChanged("currrate");
		String sNewCurrate = getCtBillCardPanel().getHeadItem("currrate")
				.getValue();
		// modified by lirr 2008-11-22 折本汇率变化后不用原来的公式计算
		// 调用新calcCtLinePriceMny方式即可
		// getCtBillCardPanel().getBillModel(CTTableCode.BASE).execFormulas(getFormularForNat(sNewCurrate),0,getCtBillCardPanel().getRowCount());

		for (int i = 0; i < getCtBillCardPanel().getRowCount(); i++) {
			/*
			 * calcCtLinePriceMny(new BillEditEvent(this, null, null,
			 * "oritaxprice", i, BillItem.BODY));
			 */
			calcCtLinePriceMny(new BillEditEvent(this, null, null, "currrate",
					i, BillItem.BODY));
		}
	}

	/**
	 * 此处插入方法说明。 功能描述:客商 作者：王亮 输入参数: 返回值: 异常处理: 日期:(2003-7-3 9:34:29) 修改：cqw
	 * 修改日期:2003-10-08 改变客商/供应商如果带出的信息不为空则替换相关的信息，如果带出的信息为空，则相关信息不处理。
	 * 修改日期：2008-10-23 修改原因：参照生成合同 触发该事件时manPk为空 李冉冉
	 */
	public void afterCustNameEdit(Boolean isFromBill) {

		/*
		 * // 自动带入对应客商默认的币种 String manPk = ((UIRefPane) getCtBillCardPanel()
		 * .getHeadItem("custname").getComponent()).getRefPK();
		 */
		// added by lirr 2008-11-10 解决效率问题，只有汇率发生变化才重新计算表体
		String sOldCurrate = null;
		if (null != getCtBillCardPanel().getHeadItem("currrate")
				&& null != getCtBillCardPanel().getHeadItem("currrate")
						.getValue()) {
			sOldCurrate = getCtBillCardPanel().getHeadItem("currrate")
					.getValue();
		}
		String manPk = null;
		if (!m_bAddFromBillFlag) {
			manPk = ((UIRefPane) getCtBillCardPanel().getHeadItem("custname")
					.getComponent()).getRefPK();
			Object oRefValue = ((UIRefPane) getCtBillCardPanel().getHeadItem(
					"ct_type").getComponent()).getRefValue("ninvctlstyle");
			// modified by liuzy 2008-10-29 上午09:58:46 只有其它合同在录入供应商时需要检测合同类型
			/*
			 * if (m_sBillType.equals(nc.vo.scm.constant.ScmConst.CT_OTHER) &&
			 * oRefValue == null) { MessageDialog.showHintDlg(this, null,
			 * nc.ui.ml.NCLangRes .getInstance().getStrByID("4020pub",
			 * "UPP4020pub-000026") @res "请先选择合同类型" ); }
			 */
		} else {
			manPk = getCtBillCardPanel().getHeadItem("custname").getValue();
		}

		if (manPk == null || manPk.length() <= 0)
			return;

		ManageItemVO[] itemVOs = (ManageItemVO[]) getCtBillCardPanel()
				.getBillModel().getBodyValueVOs(ManageItemVO.class.getName());

		// 修改币种后，将所有的非新增表体行置为修改状态（表体本币信息有修改）
		for (int i = 0; i < itemVOs.length; i++) { // 非新增行，则置为编辑状态
			if (itemVOs[i].getStatus() != 2)
				getCtBillCardPanel().getBillModel().setRowState(i, 2);
		}

		getCtBillCardPanel().getBillData().execHeadFormulas(
				getCtBillCardPanel().getHeadItem("custname").getEditFormulas());

		String basPk = getCtBillCardPanel().getHeadItem("custbasid").getValue();

		((UIRefPane) getCtBillCardPanel().getHeadItem("deliaddrname")
				.getComponent()).setWhereString(" pk_cubasdoc = '" + basPk
				+ "'");

		/*
		 * if (manPk != null) { String[] sCustInfo = getCustInfo(manPk);
		 */
		String sCustType = "";
		String sRecorPay = "02";
		/*
		 * String sCtTypepk = ((UIRefPane)
		 * getCtBillCardPanel().getHeadItem("ct_type")
		 * .getComponent()).getRefPK();
		 */
		try {
			// sRecorPay =
			// ContractQueryHelper.queryRecorPayByCtType(sCtTypepk);
			if (nc.vo.scm.constant.ScmConst.CT_PU.equals(getBillType())) {
				sRecorPay = "02";
			}
			if (nc.vo.scm.constant.ScmConst.CT_SO.equals(getBillType())) {
				sRecorPay = "01";
			}
			if ("01".equals(sRecorPay)) {
				sCustType = "and bd_cumandoc.custflag in ('0','2') and bd_cumandoc.pk_cumandoc = '"
						+ manPk + "'";
			} else if ("02".equals(sRecorPay)) {
				sCustType = "and bd_cumandoc.custflag in ('1','3') and bd_cumandoc.pk_cumandoc = '"
						+ manPk + "'";
			}

			String sCurrid = null;
			if (basPk != null) {
				String[] sCustInfo = getCustInfo(basPk, sCustType);

				sCurrid = sCustInfo[0];
				String sDepid = sCustInfo[1];
				String sPerid = sCustInfo[2];
				String sTransid = sCustInfo[3];
				String sPayid = sCustInfo[4];
				// String sDeladdid = getCustaddr(basPk);
				// modified by lirr 2009-9-3下午03:57:10 减连接数
				String sDeladdid = sCustInfo[5];
				if (sDepid != null) {
					getCtBillCardPanel().getHeadItem("depname")
							.setValue(sDepid);
					getCtBillCardPanel().getHeadItem("depid").setValue(sDepid);
				}
				if (sPerid != null) {
					getCtBillCardPanel().getHeadItem("pername")
							.setValue(sPerid);
					getCtBillCardPanel().getHeadItem("personnelid").setValue(
							sPerid);
				}
				if (sTransid != null) {
					getCtBillCardPanel().getHeadItem("transpmodename")
							.setValue(sTransid);
					getCtBillCardPanel().getHeadItem("transpmode").setValue(
							sTransid);
				}
				if (sPayid != null) {
					getCtBillCardPanel().getHeadItem("paytermname").setValue(
							sPayid);
					getCtBillCardPanel().getHeadItem("payterm")
							.setValue(sPayid);
				}
				if (sDeladdid != null) {
					getCtBillCardPanel().getHeadItem("deliaddrname").setValue(
							sDeladdid);
					getCtBillCardPanel().getHeadItem("deliaddr").setValue(
							sDeladdid);
				}
				if (sCurrid != null) {
					if (!m_bAddFromBillFlag) {
						getCtBillCardPanel().getHeadItem("currname").setValue(
								sCurrid);
						getCtBillCardPanel().getHeadItem("currid").setValue(
								sCurrid);
					} else {
						if (((UIRefPane) getCtBillCardPanel().getHeadItem(
								"currid").getComponent()).getRefPK() == null) {
							getCtBillCardPanel().getHeadItem("currname")
									.setValue(sCurrid);
							getCtBillCardPanel().getHeadItem("currid")
									.setValue(sCurrid);
						}

					}

				}
				// 获得此币种的小数位数
				String pk = ((UIRefPane) getCtBillCardPanel().getHeadItem(
						"currname").getComponent()).getRefPK();
				int currDigit = 2;
				if (pk != null && m_hCurrDigit.containsKey(pk)) {
					currDigit = ((Integer) m_hCurrDigit.get(pk)).intValue();
				}
				// 定义原币金额小数位数
				getCtBillCardPanel().getBodyItem(CTTableCode.BASE, "orisum")
						.setDecimalDigits(currDigit);
				getCtBillCardPanel().getBodyItem(CTTableCode.BASE, "oritaxmny")
						.setDecimalDigits(currDigit);
				getCtBillCardPanel().getBodyItem(CTTableCode.BASE,
						"oritaxsummny").setDecimalDigits(currDigit);

				getCtBillListPanel().getBodyItem(CTTableCode.BASE, "orisum")
						.setDecimalDigits(currDigit);
				getCtBillListPanel().getBodyItem(CTTableCode.BASE, "oritaxmny")
						.setDecimalDigits(currDigit);
				getCtBillListPanel().getBodyItem(CTTableCode.BASE,
						"oritaxsummny").setDecimalDigits(currDigit);

				// 设置汇率、汇率精度、是否可编辑
				String subDate = getCtBillCardPanel().getHeadItem(
						"subscribedate").getValue();

				if (subDate == null || subDate.trim().length() == 0) {
					MessageDialog.showHintDlg(this, null, nc.ui.ml.NCLangRes
							.getInstance().getStrByID("4020pub",
									"UPP4020pub-000097")/*
														 * @res
														 * "如果需要得到默认汇率，请先输入合同签订日期！"
														 */);
					return;
				}

				// m_dRate = PubSetUI.getBothExchRateValue(m_sPkCorp, pk, new
				// UFDate(
				// subDate));
				// m_iRateDigit = PubSetUI.getBothExchRateDigit(m_sPkCorp, pk);

				// m_bRateEnable = PubSetUI.getBothExchRateEditable(m_sPkCorp,
				// pk);
				setM_bRateEnable(pk);
				getCtBillCardPanel().getHeadItem("currrate").setEnabled(
						isM_bRateEnable());
				/*
				 * getCtBillCardPanel().getHeadItem("currrate").setDecimalDigits(
				 * m_iRateDigit[0]);
				 */
				// modified by lirr 2008-12-24 折本汇率精度新方法 since v55
				getCtBillCardPanel().getHeadItem("currrate").setDecimalDigits(
						getCurrateDigit(pk));
				// getCtBillCardPanel().getHeadItem("astcurrate").setDecimalDigits(
				// m_iRateDigit[1]);
				BusinessCurrencyRateUtil currRateUtil = new BusinessCurrencyRateUtil(
						m_sPkCorp);
				m_dRate = currRateUtil.getRate(pk, m_sLocalCurrID, new UFDate(
						subDate).toString());
				getCtBillCardPanel().getHeadItem("currrate").setValue(m_dRate);

				// getCtBillCardPanel().getHeadItem("astcurrate").setValue(m_dRate[1]);

				/*
				 * getCtBillCardPanel().getHeadItem("currrate").setEnabled(
				 * m_bRateEnable[0]);
				 */
				// getCtBillCardPanel().getHeadItem("astcurrate").setEnabled(
				// m_bRateEnable[1]);
				// modified by lirr 2008-11-22 修改原因：表体计算采用新方式
				// 置入主辅币计算公式
				// setFormularByCur(pk);
				// getCtBillCardPanel().calculateWhenHeadChanged("currrate");
				String sNewCurrate = getCtBillCardPanel().getHeadItem(
						"currrate").getValue();
				// modified by lirr 2008-11-22 折本汇率变化后不用原来的公式计算
				// 调用新calcCtLinePriceMny方式即可

				/*
				 * if(!sOldCurrate.equals(sNewCurrate)){
				 * getCtBillCardPanel().getBillModel(CTTableCode.BASE).execFormulas(getFormularForNat(sNewCurrate),0,getCtBillCardPanel().getRowCount()); }
				 */
//				for (int i = 0; i < getCtBillCardPanel().getRowCount(); i++) {  //wanglei 2013-12-19 骏杰注释
//					/*
//					 * calcCtLinePriceMny(new BillEditEvent(this, null, null,
//					 * "oritaxprice", i, BillItem.BODY));
//					 */
//					calcCtLinePriceMny(new BillEditEvent(this, null, null,
//							"currrate", i, BillItem.BODY));
//
//				}

			}
		} catch (Exception e) {
			GenMethod.handleException(this, e.getMessage(), e);
			nc.vo.scm.pub.SCMEnv.out(e);
		}

	}

	/**
	 * 此处插入方法说明。 功能描述:计划收发货日期 作者：王亮 输入参数: 返回值: 异常处理: 日期:(2003-7-3 13:23:58)
	 */
	public void afterDelivDateEdit(nc.ui.pub.bill.BillEditEvent e) {
		Object oDelivdate = e.getValue();
		if (oDelivdate != null && oDelivdate.toString().trim().length() > 0) {

			Object oSubDate = getCtBillCardPanel().getHeadItem("subscribedate")
					.getValue();
			Object oInvlliDate = getCtBillCardPanel()
					.getHeadItem("invallidate").getValue();

			// 如果合同签订日期为空
			if (oSubDate == null || oSubDate.toString().trim().length() <= 0) {
				MessageDialog.showWarningDlg(this, null, nc.ui.ml.NCLangRes
						.getInstance().getStrByID("4020pub",
								"UPP4020pub-000099")/*
													 * @res "合同签订日期不能为空。"
													 */);

			} else if (oInvlliDate == null
					|| oInvlliDate.toString().trim().length() <= 0) {
				MessageDialog.showWarningDlg(this, null, nc.ui.ml.NCLangRes
						.getInstance().getStrByID("4020pub",
								"UPP4020pub-000100")/*
													 * @res "计划终止日期不能为空。"
													 */);

			} else {
				UFDate dDelivdate = new UFDate(oDelivdate.toString());
				UFDate dSubDate = new UFDate(oSubDate.toString());
				UFDate dInvlliDate = new UFDate(oInvlliDate.toString());

				if (dDelivdate.before(dSubDate)
						|| dDelivdate.after(dInvlliDate)) {
					MessageDialog.showWarningDlg(this, null, nc.ui.ml.NCLangRes
							.getInstance().getStrByID("4020pub",
									"UPP4020pub-000101")/*
														 * @res
														 * "计划收发货日期应大于合同签订日期并且小于合同计划终止日期。"
														 */);

					getCtBillCardPanel().getBodyItem(CTTableCode.BASE,
							"delivdate").clearViewData();
					getCtBillCardPanel().setBodyValueAt(null, e.getRow(),
							"delivdate");
				}

			}

		}
	}

	/**
	 * 此处插入方法说明。 功能描述:部门 作者：王亮 输入参数: 返回值: 异常处理: 日期:(2003-7-3 9:34:29)
	 */
	public void afterDepNameEdit() {
		// deleted by lirr 2009-11-6上午10:30:22 清除部门不再清除人员
		//getCtBillCardPanel().getHeadItem("pername").clearViewData();

		String pk = ((UIRefPane) getCtBillCardPanel().getHeadItem("depname")
				.getComponent()).getRefPK();

		if (pk != null) {
			getCtBillCardPanel().getHeadItem("depid").setValue(pk);
			// deleted by lirr 2009-11-6上午10:34:10 不再跟据部门变换人员
			/*((UIRefPane) getCtBillCardPanel().getHeadItem("pername")
					.getComponent()).getRefModel().setWherePart(
					m_sPerWhereSql + " and bd_psndoc.pk_deptdoc = '" + pk
							+ "' ");*/
		/*} else {
			((UIRefPane) getCtBillCardPanel().getHeadItem("pername")
					.getComponent()).getRefModel().setWherePart(m_sPerWhereSql);
		 */
		}
		else {
		    getCtBillCardPanel().getHeadItem("depname").clearViewData();
	      getCtBillCardPanel().getHeadItem("depid").setValue(null);
		}

	}

	/**
	 * 此处插入方法说明。 功能描述:计划终止日期 作者：王亮 输入参数: 返回值: 异常处理: 日期:(2003-7-3 13:23:58)
	 */
	public void afterInvalliDateEdit(nc.ui.pub.bill.BillEditEvent e) {
		// 校验签订日期、生效日期、失效日期的大小关系
		Object obj_Value = e.getValue();
		if (obj_Value != null && !obj_Value.toString().trim().equals("")) {
			String valStr = getCtBillCardPanel().getHeadItem("valdate")
					.getValue();
			if (valStr != null && valStr.trim().length() != 0) {
				UFDate invalDate = new UFDate(obj_Value.toString());
				UFDate valDate = new UFDate(valStr);
				if (invalDate.before(valDate) || invalDate.equals(valDate)) {
					MessageDialog.showErrorDlg(this, null, nc.ui.ml.NCLangRes
							.getInstance().getStrByID("4020pub",
									"UPP4020pub-000102")/*
														 * @res
														 * "计划终止日期不能在计划生效日期之前，也不能与计划生效日期同天！"
														 */);

					getCtBillCardPanel().getHeadItem("invallidate")
							.clearViewData();
				}
			}
		}
	}

	/**
	 * 车型修改 创建日期：(2002-5-31 9:36:55)
	 * 
	 * @param row
	 *            int
	 */
	public void afterRefEdit(Object value, int row, String CodeField,
			String PKField, String NameField) {
		getCtBillCardPanel().afterRefEdit(value, row, CodeField, PKField,
				NameField);
	}

	/**
	 * 此处插入方法说明。 功能描述:合同签订日期 作者：王亮 输入参数: 返回值: 异常处理: 日期:(2003-7-3 13:23:58)
	 */
	public void afterSubscribeDateEdit(nc.ui.pub.bill.BillEditEvent e) {
		// 校验签订日期、生效日期、失效日期的大小关系
		Object obj_Value = e.getValue();
		if (obj_Value != null && !obj_Value.toString().trim().equals("")) {
			String valStr = getCtBillCardPanel().getHeadItem("valdate")
					.getValue();
			if (valStr != null && valStr.trim().length() != 0) {
				UFDate subDate = new UFDate(obj_Value.toString());
				UFDate valDate = new UFDate(valStr);
				if (subDate.after(valDate)) {
					MessageDialog.showErrorDlg(this, null, nc.ui.ml.NCLangRes
							.getInstance().getStrByID("4020pub",
									"UPP4020pub-000103")/*
														 * @res
														 * "签订日期不能在计划生效日期之后！"
														 */);
					getCtBillCardPanel().getHeadItem("subscribedate")
							.clearViewData();
				}
			}
			// 调用供应商改变事件，获得默认汇率等
			//afterCustNameEdit(m_bAddFromBillFlag);  //wanglei 2013-12-19 骏杰修改
		}
	}

	/**
	 * 此处插入方法说明。 功能描述:税率 作者：王亮 输入参数: 返回值: 异常处理: 日期:(2003-7-3 13:23:58) 修改：cqw
	 * 税率编辑后，如果有值则不能小于零。[因为存货大类的合同的税率可以为空] 税率的非空检查统一到保存时检查。
	 */
	public void afterTaxrationEdit(nc.ui.pub.bill.BillEditEvent e) {

		if (e.getValue() != null
				&& new UFDouble(e.getValue().toString()).doubleValue() < 0) {
			MessageDialog.showErrorDlg(this, null, nc.ui.ml.NCLangRes
					.getInstance().getStrByID("SCMCOMMON",
							"UPPSCMCommon-000264")/* @res "税率不能小于0！" */);
			return;
		}
	}

	/**
	 * 此处插入方法说明。 功能描述:计划终止日期 作者：王亮 输入参数: 返回值: 异常处理: 日期:(2003-7-3 13:23:58)
	 */
	public void afterValDateEdit(nc.ui.pub.bill.BillEditEvent e) {
		// 校验签订日期、生效日期、失效日期的大小关系
		Object obj_Value = e.getValue();
		if (obj_Value != null && !obj_Value.toString().trim().equals("")) {
			String subStr = getCtBillCardPanel().getHeadItem("subscribedate")
					.getValue();
			String invalStr = getCtBillCardPanel().getHeadItem("invallidate")
					.getValue();
			if (subStr != null && subStr.trim().length() != 0) {
				UFDate subDate = new UFDate(subStr);
				UFDate valDate = new UFDate(obj_Value.toString());
				if (valDate.before(subDate)) {
					MessageDialog.showWarningDlg(this, null, nc.ui.ml.NCLangRes
							.getInstance().getStrByID("4020pub",
									"UPP4020pub-000104")/*
														 * @res
														 * "计划生效日期不能在签订日期之前！"
														 */);
					getCtBillCardPanel().getHeadItem("valdate").clearViewData();
				}
			}
			if (invalStr != null && invalStr.trim().length() != 0) {
				UFDate valDate = new UFDate(obj_Value.toString());
				UFDate invalDate = new UFDate(invalStr);
				if (valDate.after(invalDate) || valDate.equals(invalDate)) {
					MessageDialog.showWarningDlg(this, null, nc.ui.ml.NCLangRes
							.getInstance().getStrByID("4020pub",
									"UPP4020pub-000105")/*
														 * @res
														 * "计划生效日期不能在计划终止日期之后，也不能与计划终止日期同天！"
														 */);
					getCtBillCardPanel().getHeadItem("valdate").clearViewData();
				}
			}
		}
	}

	/**
	 * 此处插入方法说明。 创建日期：(2002-6-27 16:16:11)
	 */
	protected boolean checkVO(AggregatedValueObject vo, ScmBillCardPanel bcp) {
		try {
			if (!bcp.checkVO((DMVO) vo, true, true))
				return false;
			return checkOther(vo, bcp);
		} catch (ICDateException e) {
			// 显示提示
			String sErrorMessage = GeneralMethod.getBodyErrorMessage(bcp, e
					.getErrorRowNums(), e.getHint());
			showErrorMessage(sErrorMessage);
			return false;
		} catch (ICNullFieldException e) {
			// 显示提示
			String sErrorMessage = GeneralMethod.getBodyErrorMessage(bcp, e
					.getErrorRowNums(), e.getHint());
			showErrorMessage(sErrorMessage);
			return false;
		} catch (ICNumException e) {
			// 显示提示
			String sErrorMessage = GeneralMethod.getBodyErrorMessage(bcp, e
					.getErrorRowNums(), e.getHint());
			showErrorMessage(sErrorMessage);
			return false;
		} catch (ICPriceException e) {
			// 显示提示
			String sErrorMessage = GeneralMethod.getBodyErrorMessage(bcp, e
					.getErrorRowNums(), e.getHint());
			showErrorMessage(sErrorMessage);
			return false;
		} catch (ICSNException e) {
			// 显示提示
			String sErrorMessage = GeneralMethod.getBodyErrorMessage(bcp, e
					.getErrorRowNums(), e.getHint());
			showErrorMessage(sErrorMessage);
			return false;
		} catch (ICLocatorException e) {
			// 显示提示
			String sErrorMessage = GeneralMethod.getBodyErrorMessage(bcp, e
					.getErrorRowNums(), e.getHint());
			showErrorMessage(sErrorMessage);
			return false;
		} catch (ICRepeatException e) {
			// 显示提示
			String sErrorMessage = GeneralMethod.getBodyErrorMessage(bcp, e
					.getErrorRowNums(), e.getHint());
			showErrorMessage(sErrorMessage);
			return false;
		} catch (ICHeaderNullFieldException e) {
			// 显示提示
			String sErrorMessage = GeneralMethod.getHeaderErrorMessage(bcp, e
					.getErrorRowNums(), e.getHint());
			showErrorMessage(sErrorMessage);
			return false;
		} catch (NullFieldException e) {
			showErrorMessage(e.getHint());
			return false;
		} catch (ValidationException e) {
			String sMessage = nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"4020pub", "UPP4020pub-000106")/* @res "校验异常！其他未知故障..." */;
			showErrorMessage(sMessage);

			handleException(e);
			return false;
		} catch (Exception e) {
			return false;
		}
	}

	/** Tells listeners that a column was added to the model. */
	public void columnAdded(javax.swing.event.TableColumnModelEvent e) {
	}

	/** Tells listeners that a column was moved due to a margin change. */
	public void columnMarginChanged(javax.swing.event.ChangeEvent e) {
	}

	/** Tells listeners that a column was repositioned. */
	public void columnMoved(javax.swing.event.TableColumnModelEvent e) {
	}

	/** Tells listeners that a column was removed from the model. */
	public void columnRemoved(javax.swing.event.TableColumnModelEvent e) {
	}

	/**
	 * Tells listeners that the selection model of the TableColumnModel changed.
	 */
	public void columnSelectionChanged(javax.swing.event.ListSelectionEvent e) {
		if (getCtBillCardPanel().getBillData().getEnabled()) {
			int colnow = getCtBillCardPanel().getBillTable()
					.getSelectedColumn();
			int rownow = getCtBillCardPanel().getBillTable().getSelectedRow();
			if (colnow >= 0 && rownow >= 0) {
				initRef(rownow, colnow, getCtBillCardPanel());
				String key = getCtBillCardPanel().getBodyShowItems()[colnow]
						.getKey();
				Object value = getCtBillCardPanel().getBodyValueAt(rownow, key);
				whenEntered(rownow, colnow, key, value, getCtBillCardPanel());
			}
		}

	}

	/**
	 * 创建者：仲瑞庆 功能：产生辅计量 参数： 返回： 例外： 日期：(2001-5-16 下午 6:32) 修改日期，修改人，修改原因，注释标志：
	 */
	protected void filterMeas(String sInvID, String sCastUnitNameField,
			String pkcorp) {

		nc.ui.pub.beans.UIRefPane refCast = (nc.ui.pub.beans.UIRefPane) getCtBillCardPanel()
				.getBodyItem(CTTableCode.BASE, sCastUnitNameField)
				.getComponent();

		refCast.setReturnCode(true);
		// m_voInvMeas.filterMeas(pkcorp, sInvID, refCast);
	}

	/**
	 * 函数的功能、用途、对属性的更改，以及函数执行前后对象的状态。 表体自由项初始化
	 * 
	 * @param 参数说明
	 * @return 返回值
	 * @exception 异常描述
	 * @see 需要参见的其它内容
	 * @since 从类的那一个版本，此方法被添加进来。（可选）
	 * @author zhongyue
	 * @param row
	 *            int
	 * @param col
	 *            int
	 */
	protected InvVO getBodyInvVOFromPanel(int row, int col,
			ScmBillCardPanel dmbcp) {
		try {
			getInvInfo("", "");
			return null;
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 功能：查存货信息，带辅计量单位。 参数： 返回： 例外： 日期：(2002-8-1 20:24:56) 修改日期，修改人，修改原因，注释标志：
	 */
	protected InvVO[] getInvInfo(String[] saInv, String[] saAstID) {
		if (null == saInv || saInv.length == 0) {
			return new InvVO[0];
		}
		InvVO[] invvos = new InvVO[saInv.length];
		for (int i = 0; i < saInv.length; i++) {
			invvos[i] = InvMeasInfo.getInvInfo(saInv[i], saAstID[i]);
		}
		return invvos;
	}

	/**
	 * 功能：查存货信息，带辅计量单位。 参数： 返回： 例外： 日期：(2002-8-1 20:24:56) 修改日期，修改人，修改原因，注释标志：
	 */
	protected InvVO getInvInfo(String sInv, String sAstID) {
		return null;
	}

	/**
	 * 每当部件抛出异常时被调用
	 * 
	 * @param exception
	 *            java.lang.Throwable
	 */
	protected void handleException(Exception exception) {
		/* 除去下列各行的注释，以将未捕捉到的异常打印至 stdout。 */
		SCMEnv.out("--------- 未捕捉到的异常 ---------"); /*-=notranslate=-*/
		SCMEnv.out(exception);
	}

	/**
	 * 函数的功能、用途、对属性的更改，以及函数执行前后对象的状态。 表体自由项初始化
	 * 
	 * @param 参数说明
	 * @return 返回值
	 * @exception 异常描述
	 * @see 需要参见的其它内容
	 * @since 从类的那一个版本，此方法被添加进来。（可选）
	 * @author zhongyue
	 * @param row
	 *            int
	 * @param col
	 *            int
	 */
	protected void initBodyFreeItem(int row, int col, ScmBillCardPanel dmbcp) {
		try {
			// 取值
			InvVO invvo = getBodyInvVOFromPanel(row, col, dmbcp);

			// 向自由项参照传入数据
			// 初始化
			((FreeItemRefPane) getCtBillCardPanel().getBodyItem("vfree0")
					.getComponent()).setFreeItemParam(invvo);
			((FreeItemRefPane) getCtBillCardPanel().getBodyItem("vfree0")
					.getComponent()).setEditable(false);

		} catch (Exception e) {
		}
	}

	/**
	 * 此处插入方法说明。 功能：初始化每一格的参照 参数： 返回： 例外： 日期：(2002-5-8 19:04:08)
	 * 修改日期，修改人，修改原因，注释标志： 2002-06-19 韩卫 修改 （1）添加判断和显示发运组织选择提示框，
	 * （2）添加m_OrgNoShowFlag，变量和getOrgnoshowFlag、setOrgnoshowFlag方法
	 */
	public void initRef(int rownow, int colnow, ScmBillCardPanel bcp) {
		String sItemKey = bcp.getBillModel().getBodyKeyByCol(colnow).trim();
		if (!bcp.getBillData().getBodyItem(CTTableCode.BASE, sItemKey)
				.isEnabled()) {
			return;
		}
	}

	/**
	 * 此处插入方法说明。 功能描述:打印 输入参数: 返回值: 异常处理: 日期: 修改说明：增加打印次数控制 修改者：邵兵 2004-12-08
	 */
	private void onCardPrint(BillCardPanel bcp) {
		/* 增加打印次数控制后的打印方案 */
		showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4020pub",
				"UPP4020pub-000107")/* @res "正在打印，请稍候..." */);
		// 准备数据：获得要打印的vo.
		/*---合同基本信息与合同条款的合并打印---*/
		ExtendManageVO vo = (ExtendManageVO) getValueForCardPrint(bcp);
		if (null == vo) {
			vo = new ExtendManageVO();
		}
		if (null == vo.getParentVO()) {
			vo.setParentVO(new ManageHeaderVO());
		}
		if (null == vo.getTableVO("table"))
			vo.setTableVO("table", new ManageItemVO[] { new ManageItemVO() });
		if (null == vo.getTableVO("term"))
			vo.setTableVO("term", new TermBb4VO[] { new TermBb4VO() });

		if (getPrintEntry().selectTemplate() < 0)
			return;
		/*---准备数据结束---*/

		// 合同单据主表
		ManageHeaderVO headerVO = (ManageHeaderVO) vo.getParentVO();
		String sBillID = headerVO.getPrimaryKey();

		// 构造PrintLogClient及设置PrintInfo.
		ScmPrintlogVO voSpl = new ScmPrintlogVO();
		voSpl.setCbillid(sBillID); // 单据主表的ID
		voSpl.setVbillcode(headerVO.getCt_code());// 传入合同号，用于显示。
		voSpl.setCbilltypecode(getBillType());
		voSpl.setCoperatorid(getOperator());
		voSpl.setIoperatetype(new Integer(PrintConst.PAT_OK));// 固定
		voSpl.setPk_corp(getCorpPrimaryKey());
		voSpl.setTs(headerVO.getTs());// 单据主表的TS

		nc.ui.scm.print.PrintLogClient plc = new nc.ui.scm.print.PrintLogClient();
		// 设置单据信息
		plc.setPrintInfo(voSpl);
		// 设置TS刷新监听.
		plc.addFreshTsListener(new FreshTsListener());
		// 设置打印监听
		getPrintEntry().setPrintListener(plc);

		plc.setPrintEntry(getPrintEntry());// 用于单打时
		// 设置单据信息
		plc.setPrintInfo(voSpl);

		// 向打印置入数据源，进行打印
		getDataSource().setBillVO(vo);
		getPrintEntry().setDataSource(getDataSource());
		getPrintEntry().print();

		MessageDialog.showHintDlg(this, null, plc.getPrintResultMsg(false));

		showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
				"SCMCOMMON", "UPPSCMCommon-000133")/* @res "就绪" */);

	}

	/**
	 * 此处插入方法说明。 功能描述:打印功能 输入参数: 返回值: 异常处理: 日期:2003-7-7 10:27
	 */
	public void onPrint() {
		// m_bIsCard 标志单据是Card形式,还是List形式
		// bill card
		// addied by liuzy 2008-01-10 项目问题合并 问题号：200712141125493518
		// cy 为了每次预览都可以选择打印模版，需要重新初始化
		// UAP
		// 薄奇：上星期的打印模板选择问题，是因为你们的界面一直使用同一个PrintEntry对象实例，而我们对PrintEntry单个实例多次调用打印支持不全面，我以前看过你们的相关调用代码，应该有一个getPrintEntry()方法，
		// if(this.printEntry != null) return this.printEntry; else return new
		// PrintEntry()，所以，简单的解决方式, 每次都new一个PrintEntry返回。

		newPrintEntry();

		if (m_bIsCard) {
			onCardPrint(getCtBillCardPanel());

		} else { // 列表打印
			onListPrint();
		}
		// setButtonState();
		if (!m_bAddFromBillFlag) {
			setButtonState();
		} else {
			setButtonStateForCof();
		}
	}

	/**
	 * 此处插入方法说明。 功能描述: 输入参数: 返回值: 异常处理: 日期:
	 */
	public void onPrintPreview() {
		// m_bIsCard 标志单据是Card形式,还是List形式
		// card
		// addied by liuzy 2008-01-10 项目问题合并 问题号：200712141125493518
		// cy 为了每次预览都可以选择打印模版，需要重新初始化
		// UAP
		// 薄奇：上星期的打印模板选择问题，是因为你们的界面一直使用同一个PrintEntry对象实例，而我们对PrintEntry单个实例多次调用打印支持不全面，我以前看过你们的相关调用代码，应该有一个getPrintEntry()方法，
		// if(this.printEntry != null) return this.printEntry; else return new
		// PrintEntry()，所以，简单的解决方式, 每次都new一个PrintEntry返回。

		newPrintEntry();

		if (m_bIsCard) {
			onCardPrintPreview(getCtBillCardPanel());
		} else { // 列表下预览
			onListPrintPreview();
		}
		// setButtonState();
		if (!m_bAddFromBillFlag) {
			setButtonState();
		} else {
			setButtonStateForCof();
		}
	}

	protected void newPrintEntry() {
		m_print = new nc.ui.pub.print.PrintEntry(null, null);
		if (null == getFrame())
			m_print.setTemplateID(getCorpPrimaryKey(), getNodeCode(),
					getOperator(), null);
		else
			m_print.setTemplateID(getCorpPrimaryKey(),
					getFrame().getModuleCode() != null
							&& !getFrame().getModuleCode()
									.equals(getNodeCode()) ? getFrame()
							.getModuleCode() : getNodeCode(), getOperator(),
					null);
	}

	/**
	 * 此处插入方法说明。 功能描述: 输入参数: 返回值: 异常处理: 日期:
	 * 
	 * @param sCurrentID
	 *            java.lang.String
	 */
	/*
	 * protected void setFormularByCur(String sCurrentID) { // 原币对辅币的折算模式
	 * UFBoolean bOrimode = null;
	 * 
	 * try { // sCurrentID 币种ID // CurrinfoVO currVO =
	 * currArith.getCurrinfoVO(sCurrentID, currArith // .getFracCurrPK()); //
	 * modidied by lirr 2008-07-10 修改原因：UAP去掉了原方法 CurrParamQuery cpq =
	 * CurrParamQuery.getInstance(); CurrinfoVO currVO =
	 * currArith.getCurrinfoVO(sCurrentID, cpq .getLocalCurrPK(m_sPkCorp)); //
	 * 原币对辅币的折算模式 bOrimode = currVO.getConvmode(); } catch (BusinessException e) { //
	 * eat exception SCMEnv.out("未能获得此币种对应的折算模式!"); } // 如果原币是本位币，那么只折辅不折本 if
	 * (m_sLocalCurrID != null && m_sLocalCurrID.equals(sCurrentID)) { //
	 * deleted by lirr 2008-8-20 根据5.5需求删除辅币信息 // 清掉辅币信息
	 * 
	 * int irowcount = getCtBillCardPanel().getRowCount(); String[] sAstField =
	 * new String[] { "astprice", "astsum", "asttaxprice", "asttaxmny",
	 * "asttaxsummny" }; for (int i = 0; i < irowcount; i++)
	 * getCtBillCardPanel().getBillModel().clearRowData(i, sAstField); // 公式
	 * setFormularForNat(bOrimode); return; } // deleted by lirr 2008-8-20
	 * 根据5.5需求删除辅币信息 // if (bCalType) { // 主辅币核算方式 //
	 * setFormularForNatAst(bOrimode); // } else { // 单主币核算方式
	 * setFormularForNat(bOrimode); // } }
	 */

	/**
	 * 作者：毕晖 查存货属性 创建日期：(2002-6-11 9:47:30)
	 * 
	 * @param sInv
	 *            java.lang.String[]
	 */
	protected void setInvItemEditable(ScmBillCardPanel bcp) {
		// 获得界面数据
		DMVO dvo = (DMVO) bcp.getBillValueVO(DMVO.class.getName(),
				DMDataVO.class.getName(), DMDataVO.class.getName());
		DMDataVO[] dmdvos = dvo.getBodyVOs();
		// 获得存货主键、辅计量主键
		String[] invkeys = new String[dmdvos.length];
		String[] astkeys = new String[dmdvos.length];
		for (int i = 0; i < dmdvos.length; i++) {
			invkeys[i] = (String) dmdvos[i].getAttributeValue("invid");
			astkeys[i] = (String) dmdvos[i].getAttributeValue("astmeasid");
		}
		// 获得存货信息
		InvVO[] invvos = getInvInfo(invkeys, astkeys);
		// 置存货属性是否可编辑
		for (int i = 0; i < dmdvos.length; i++) {
			// 是否辅计量
			// 辅数量
			Integer isassistunit = (Integer) invvos[i]
					.getAttributeValue("isAstUOMmgt");
			/*
			 * bcp.setCellEditable(i, "astmeascode", (isassistunit.intValue() ==
			 * 1));
			 */
			bcp.setCellEditable(i, "astnum", (isassistunit.intValue() == 1));
		}
	}

	/**
	 * 此处插入方法说明。 功能：鼠标点击或键盘移动时，刚刚进入表体中格时触发 参数： 返回： 例外： 日期：(2002-8-6 13:50:21)
	 * 修改日期，修改人，修改原因，注释标志：
	 * 
	 * @param row
	 *            int
	 * @param col
	 *            int
	 * @param key
	 *            java.lang.String
	 * @param value
	 *            java.lang.Object
	 */
	public void whenEntered(int row, int col, String key, Object value,
			ScmBillCardPanel bcp) {
		// 向参照等对象内置入值
		if (null != bcp.getBodyItem(CTTableCode.BASE, key)
				&& bcp.getBodyItem(CTTableCode.BASE, key).getComponent() instanceof UIRefPane) {
			((UIRefPane) bcp.getBodyItem(CTTableCode.BASE, key).getComponent())
					.setValue(value == null ? null : value.toString());
		}
		// 向自由项传递参数
		if (key.equals("vfree0"))
			initBodyFreeItem(row, col, bcp);

		// 向辅计量传递参数
		if (key.equals("astmeascode")) {
			String sInvID = (String) bcp.getBodyValueAt(row, "invid");
			if (null != sInvID) {
				filterMeas(sInvID, key, m_sPkCorp);
			}
		}
	}

	/**
	 * 此处插入方法说明。 功能描述:返回需要打印的列表内容的VO 输入参数: 返回值:java.util.ArrayList 异常处理: 日期:
	 */
	public ArrayList getAllVOs() {
		return m_alAllVOs;
	}

	/**
	 * 此处插入方法说明。 功能描述:设置需要打印的列表内容的VO 输入参数:java.util.ArrayList 返回值: 异常处理: 日期:
	 * 
	 * @param newAllVOs
	 *            java.util.ArrayList
	 */
	public void setAllVOs(java.util.ArrayList newAllVOs) {
		m_alAllVOs = newAllVOs;
	}

	/**
	 * 此处插入方法说明。 功能描述:打印 输入参数: 返回值: 异常处理: 日期: 修改说明：增加打印次数控制 修改者：邵兵 2004-12-08
	 * 
	 * @param bcp
	 *            nc.ui.pub.bill.BillCardPanel
	 */
	public void onCardPrintPreview(BillCardPanel bcp) {
		/*---- 修改打印次数后的打印预览。----*/
		// 准备数据
		/*---合同基本信息与合同条款的合并打印---*/
		ExtendManageVO vo = (ExtendManageVO) getValueForCardPrint(bcp);
		if (null == vo) {
			vo = new ExtendManageVO();
		}
		if (null == vo.getParentVO()) {
			vo.setParentVO(new DMDataVO());
		}
		if (null == vo.getTableVO("table"))
			vo.setTableVO("table", new ManageItemVO[] { new ManageItemVO() });
		if (null == vo.getTableVO("term"))
			vo.setTableVO("term", new TermBb4VO[] { new TermBb4VO() });

		if (getPrintEntry().selectTemplate() < 0)
			return;
		/*---准备数据结束---*/

		// 合同单据主表
		ManageHeaderVO headerVO = (ManageHeaderVO) vo.getParentVO();
		String sBillID = headerVO.getPrimaryKey();

		// 构造PringLogClient以及设置PrintInfo
		ScmPrintlogVO voSpl = new ScmPrintlogVO();
		voSpl = new ScmPrintlogVO();
		voSpl.setCbillid(sBillID); // 单据主表的ID
		voSpl.setVbillcode(headerVO.getCt_code());// 传入合同号，用于显示。
		voSpl.setCbilltypecode(getBillType());
		voSpl.setCoperatorid(getOperator());
		voSpl.setIoperatetype(new Integer(PrintConst.PAT_OK));// 固定
		voSpl.setPk_corp(getCorpPrimaryKey());
		voSpl.setTs(headerVO.getTs());// 单据主表的TS

		nc.ui.scm.print.PrintLogClient plc = new nc.ui.scm.print.PrintLogClient();
		plc.setPrintEntry(getPrintEntry());
		// 设置单据信息
		plc.setPrintInfo(voSpl);
		// 设置ts和printcount刷新监听.
		plc.addFreshTsListener(new FreshTsListener());
		// 设置打印监听
		getPrintEntry().setPrintListener(plc);

		// 打印预览
		getDataSource().setBillVO(vo);
		getPrintEntry().setDataSource(getDataSource());
		getPrintEntry().preview();

		showHintMessage(plc.getPrintResultMsg(true));
	}

	/*
	 * 得到列表下选择的单据 邵兵 2005-08-09
	 */
	protected ArrayList getSelectedBills() {
		// 准备数据
		int iSelListHeadRowCount = getCtBillListPanel().getHeadTable()
				.getSelectedRowCount();
		if (iSelListHeadRowCount <= 0) {
			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"SCMCOMMON", "UPPSCMCommon-000167")/* @res "没有选择单据!" */);
			return null;
		}

		// 得到选择单据的索引：
		int[] arySelListHeadRows = new int[iSelListHeadRowCount];
		arySelListHeadRows = getCtBillListPanel().getHeadTable()
				.getSelectedRows();

		ArrayList alvos = new ArrayList();
		ExtendManageVO voExtend = null;

		// modified by lirr 2008-07-25 修改原因：修改前只能打印第一条合同的表头和表体，其他的合同只打印表头

		/*
		 * for (int i = 0; i < iSelListHeadRowCount; i++) { voExtend =
		 * (ExtendManageVO) m_vBillVO .elementAt(arySelListHeadRows[i]);
		 * alvos.add(voExtend); }
		 */

		int index = 0;
		ManageVO[] allDataManagevos = new ManageVO[iSelListHeadRowCount];// 含有所有表体的合同数组
		ManageVO[] allVoOnlyHaveHead = new ManageVO[iSelListHeadRowCount];// 只有第一条记录有表体的合同数组
		// added by lirr 2008-12-30
		String pkHasItem = "";
		for (int i = 0; i < iSelListHeadRowCount; i++) {
			voExtend = (ExtendManageVO) m_vBillVO
					.elementAt(arySelListHeadRows[i]);
			// added by lirr 2008-12-30
			if (null != voExtend.getTableVO("table")) {
				ManageHeaderVO head = (ManageHeaderVO) voExtend.getParentVO();
				pkHasItem = head.getPk_ct_manage();
			} // end
			ManageHeaderVO voHead = (ManageHeaderVO) voExtend.getParentVO();
			allVoOnlyHaveHead[i] = new ManageVO();
			allVoOnlyHaveHead[i].setParentVO(voHead);
		}
		try {
			// 查询数据库 获得含有所有表体的合同数组
			allDataManagevos = ContractQueryHelper
					.queryAllArrBodyData(allVoOnlyHaveHead);
		} catch (Exception e) {
			// TODO 自动生成 catch 块
			// e.printStackTrace();
			SCMEnv.out(e.getMessage());

		}
		// modified by liuzy 2008-09-08 刷公式
		execFormularAfterQueryWithoutCache(allDataManagevos);
		// added by lirr 排序打印问题 南玻提出
		for (int i = 0; i < allDataManagevos.length; i++) {
			if (allDataManagevos[i].getParentVO().getPrimaryKey().equals(
					pkHasItem)) {
				allDataManagevos[i].setTableVO("table", getCtBillListPanel()
						.getBodyBillModel("table").getBodyValueVOs(
								ManageItemVO.class.getName()));
			}

		}

		// 将ManageVO[] allVoOnlyHaveHead，转换为ExtendManageVO 类型并放入alvos
		if (null != allDataManagevos && allDataManagevos.length > 0) {
			// added by lirr 2008-11-28 打印模板设置精度
			try {
				nc.vo.ct.pub.GenMethod.setVOScale(allDataManagevos);
				for (int i = 0; i < allDataManagevos.length; i++) {

					ExtendManageVO voExtendForAl = new ExtendManageVO();
					voExtendForAl
							.setParentVO(allDataManagevos[i].getParentVO());
					voExtendForAl.setTableVO("table", allDataManagevos[i]
							.getChildrenVO());
					voExtendForAl.setTableVO("term", allDataManagevos[i]
							.getTermBb4s());
					voExtendForAl.setTableVO("cost", allDataManagevos[i]
							.getExpBb3s());
					voExtendForAl.setTableVO("note", allDataManagevos[i]
							.getMemoraBb2s());
					voExtendForAl.setTableVO("history", allDataManagevos[i]
							.getChangeBb1s());
					voExtendForAl.setTableVO("exec", allDataManagevos[i]
							.getManageExecs());
					alvos.add(voExtendForAl);
				}
			} catch (Exception e) {
				// TODO 自动生成 catch 块
				GenMethod.handleException(this, e.getMessage(), e);
			}

		}

		return getValueForListPrint(alvos, arySelListHeadRows);

	}

	/*
	 * 为了支持批打印 需要得到所有的选中行的表体数据 added by lirr 2008-07-24
	 */
	protected ArrayList getSelectedBillsForListPrint() {
		// 准备数据
		int iSelListHeadRowCount = getCtBillListPanel().getHeadTable()
				.getSelectedRowCount();
		if (iSelListHeadRowCount <= 0) {
			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"SCMCOMMON", "UPPSCMCommon-000167")/* @res "没有选择单据!" */);
			return null;
		}

		// 得到选择单据的索引：
		int[] arySelListHeadRows = new int[iSelListHeadRowCount];
		arySelListHeadRows = getCtBillListPanel().getHeadTable()
				.getSelectedRows();

		ArrayList alvos = new ArrayList();
		ExtendManageVO voExtend = null;

		int index = 0;
		ManageVO[] allDataManagevos = new ManageVO[iSelListHeadRowCount];
		ManageVO[] allVoOnlyHaveHead = new ManageVO[iSelListHeadRowCount];
		for (int i = 0; i < iSelListHeadRowCount; i++) {
			voExtend = (ExtendManageVO) m_vBillVO
					.elementAt(arySelListHeadRows[i]);

			// modified by lirr 修改前只获得合同头以及第一条合同的表体，为了支持批打印须获得所有合同行

			ManageHeaderVO voHead = (ManageHeaderVO) voExtend.getParentVO();
			allVoOnlyHaveHead[i] = new ManageVO();
			allVoOnlyHaveHead[i].setParentVO(voHead);
		}

		try {
			allDataManagevos = ContractQueryHelper
					.queryAllArrBodyData(allVoOnlyHaveHead);
		} catch (Exception e) {
			// TODO 自动生成 catch 块
			// e.printStackTrace();
			SCMEnv.out(e.getMessage());

		}

		// execFormularAfterQuery(allDataManagevos);
		for (int i = 0; i < allDataManagevos.length; i++) {

			ExtendManageVO voExtendForAl = new ExtendManageVO();
			voExtendForAl.setParentVO(allDataManagevos[i].getParentVO());

			voExtendForAl.setTableVO("table", allDataManagevos[i]
					.getChildrenVO());
			voExtendForAl.setTableVO("term", allDataManagevos[i].getTermBb4s());
			voExtendForAl.setTableVO("cost", allDataManagevos[i].getExpBb3s());
			voExtendForAl.setTableVO("note", allDataManagevos[i]
					.getMemoraBb2s());
			voExtendForAl.setTableVO("history", allDataManagevos[i]
					.getChangeBb1s());
			voExtendForAl.setTableVO("exec", allDataManagevos[i]
					.getManageExecs());
			alvos.add(voExtendForAl);
		}

		return getValueForListPrint(alvos, arySelListHeadRows);
	}

	/*
	 * 得到列表下选择的第一张单据(允许多选) 邵兵 2005-08-09
	 */
	protected ArrayList getFirstSelectedBill() {
		// 准备数据
		int iSelListHeadRowCount = getCtBillListPanel().getHeadTable()
				.getSelectedRowCount();
		if (iSelListHeadRowCount <= 0) {
			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"SCMCOMMON", "UPPSCMCommon-000167")/* @res "没有选择单据!" */);
			return null;
		}

		// 得到选择单据的索引：
		int[] arySelListHeadRows = new int[iSelListHeadRowCount];
		arySelListHeadRows = getCtBillListPanel().getHeadTable()
				.getSelectedRows();

		ArrayList alvos = new ArrayList();
		// first selected bill:
		ExtendManageVO voExtend = (ExtendManageVO) m_vBillVO
				.elementAt(arySelListHeadRows[0]);
		alvos.add(voExtend);

		return getValueForListPrint(alvos, arySelListHeadRows);
	}

	/**
	 * 列表打印 修改说明：增加打印次数控制 修改者：邵兵 2004-12-08
	 */
	public void onListPrint() {
		// 得到要打印的列表vo,ArrayList.
		ArrayList alPrintVOs = getSelectedBills();
		if (alPrintVOs == null)
			return;

		/* 增加打印次数控制后的打印方案 */
		showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4020pub",
				"UPP4020pub-000107")/* @res "正在打印，请稍候..." */);

		SCMMutiTabPrintDataInterface ds = null;
		ExtendManageVO mvo = null;
		// 合同单据主表
		ManageHeaderVO headerVO = null;

		nc.ui.scm.print.PrintLogClient plc = new nc.ui.scm.print.PrintLogClient();
		plc.setBatchPrint(true);// 设置是批打

		getPrintEntry().beginBatchPrint();
		// 设置打印监听
		getPrintEntry().setPrintListener(plc);
		plc.setPrintEntry(getPrintEntry());
		// 打印操作
		try {

			// ArrayList alUnPrintCt = new ArrayList();
			for (int i = 0; i < alPrintVOs.size(); i++) {
				mvo = (ExtendManageVO) alPrintVOs.get(i);
				for (int j = 0; j < mvo.getTableVO("cost").length; j++) {
					(mvo.getTableVO("cost"))[j]
							.setAttributeValue("costmemo", mvo
									.getTableVO("cost")[j]
									.getAttributeValue("memo"));
				}
				for (int j = 0; j < mvo.getTableVO("note").length; j++) {
					(mvo.getTableVO("note"))[j]
							.setAttributeValue("notememo", mvo
									.getTableVO("note")[j]
									.getAttributeValue("memo"));
				}
				headerVO = (ManageHeaderVO) mvo.getParentVO();

				ScmPrintlogVO voSpl = new ScmPrintlogVO();
				voSpl = new ScmPrintlogVO();
				voSpl.setCbilltypecode(getBillType());
				voSpl.setCoperatorid(getOperator());
				voSpl.setIoperatetype(new Integer(PrintConst.PAT_OK));// 固定
				voSpl.setPk_corp(getCorpPrimaryKey());
				voSpl.setCbillid(headerVO.getPrimaryKey()); // 单据主表的ID
				voSpl.setVbillcode(headerVO.getCt_code());// 传入合同号，用于显示。
				voSpl.setTs(headerVO.getTs());// 单据主表的TS

				// 设置单据信息
				plc.setPrintInfo(voSpl);
				// 设置TS刷新监听.
				plc.addFreshTsListener(new FreshTsListener());

				if (plc.check()) {// 检查通过才执行打印，有错误的话自动插入打印日志，这里不用处理。

					ds = getNewDataSource();
					ds.setBillVO(mvo);
					getPrintEntry().setDataSource(ds);

					// 常量定义在Setup（很小）中，在现场很容易定制它。
					/*
					 * while (getPrintEntry().dsCountInPool() >
					 * PrintConst.PL_MAX_TAST_NUM) {
					 * Thread.sleep(PrintConst.PL_SLEEP_TIME); //
					 * 如果有PL_MAX_TAST_NUM个以上任务，就等待PL_SLEEP_TIME秒。 }
					 */
				}
			}
			getPrintEntry().endBatchPrint();
			MessageDialog.showHintDlg(this, null, plc.getPrintResultMsg(false));

		} catch (Exception e) {
			nc.vo.scm.pub.SCMEnv.out(e);
			showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"4020pub", "UPP4020pub-000108")/* @res "打印出错！" */
					+ "\n" + e.getMessage());
		}

		showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
				"SCMCOMMON", "UPPSCMCommon-000133")/* @res "就绪" */);
	}

	/**
	 * 列表下预览 修改说明：增加打印次数控制 修改者：邵兵 2004-12-08
	 */
	public void onListPrintPreview() {
		// 准备数据：获得要打印的vo.
		// 得到要打印的列表vo,ArrayList.
		ArrayList alPrintVOs = getSelectedBills();
		if (alPrintVOs == null)
			return;

		// 增加打印次数控制后的打印方案
		showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4020pub",
				"UPP4020pub-000109") /* @res "正在生成第一张单据的打印预览数据，请稍候..." */);

		ExtendManageVO dmvo = (ExtendManageVO) alPrintVOs.get(0);
		// 合同单据主表
		ManageHeaderVO headerVO = (ManageHeaderVO) dmvo.getParentVO();
		String sBillID = headerVO.getPrimaryKey();

		// 构造PringLogClient以及设置PrintInfo
		ScmPrintlogVO voSpl = new ScmPrintlogVO();
		voSpl = new ScmPrintlogVO();
		voSpl.setCbillid(sBillID); // 单据主表的ID
		voSpl.setVbillcode(headerVO.getCt_code());// 传入合同号，用于显示。
		voSpl.setCbilltypecode(getBillType());
		voSpl.setCoperatorid(getOperator());
		voSpl.setIoperatetype(new Integer(PrintConst.PAT_OK));// 固定
		voSpl.setPk_corp(getCorpPrimaryKey());
		voSpl.setTs(headerVO.getTs());// 单据主表的TS

		nc.ui.scm.print.PrintLogClient plc = new nc.ui.scm.print.PrintLogClient();
		plc.setPrintEntry(getPrintEntry());
		// 设置单据信息
		plc.setPrintInfo(voSpl);
		// 设置ts和printcount刷新监听.
		plc.addFreshTsListener(new FreshTsListener());
		// 设置打印监听
		getPrintEntry().setPrintListener(plc);

		// 打印预览
		getDataSource().setBillVO(dmvo);

		getPrintEntry().setDataSource(getDataSource());

		// modified by liuzy 2008-01-11 选择打印模板
		if (getPrintEntry().selectTemplate() < 0)
			return;

		getPrintEntry().preview();

		showHintMessage(plc.getPrintResultMsg(true));

	}

	/**
	 * getPrintEntry()。 创建日期：(2004-12-09)
	 * 
	 * @author：邵兵
	 * @return nc.ui.pub.print.PrintEntry
	 * @param null
	 */
	protected nc.ui.pub.print.PrintEntry getPrintEntry() {
		if (null == m_print) {
			m_print = new nc.ui.pub.print.PrintEntry(null, null);
			if (null == getFrame())
				m_print.setTemplateID(getCorpPrimaryKey(), getNodeCode(),
						getOperator(), null);
			else
				m_print.setTemplateID(getCorpPrimaryKey(),
						getFrame().getModuleCode() != null
								&& !getFrame().getModuleCode().equals(
										getNodeCode()) ? getFrame()
								.getModuleCode() : getNodeCode(),
						getOperator(), null);
		}
		return m_print;
	}

	/**
	 * 得到一个PrintDataInterface类型实例，用作card下的打印。 2004-12-17 邵兵
	 * 
	 * @return
	 */
	protected nc.ui.ct.pub.print.SCMMutiTabPrintDataInterface getDataSource() {
		if (null == m_dataSource) {
			m_dataSource = new nc.ui.ct.pub.print.SCMMutiTabPrintDataInterface();
			BillData bd = getCtBillCardPanel().getBillData();
			m_dataSource.setBillData(bd);
			m_dataSource.setModuleName(getModuleCode());
			m_dataSource.setTotalLinesInOnePage(getPrintEntry().getBreakPos());
		}
		return m_dataSource;
	}

	/**
	 * getDataSourceNew()。 创建日期：(2004-12-09)
	 * 每调用一次就创建一个新的PrintDataInterface类型实例，用作list下的打印。
	 * 
	 * @author：邵兵
	 * @return PrintDataInterface
	 * @param null
	 */
	protected SCMMutiTabPrintDataInterface getNewDataSource() {
		SCMMutiTabPrintDataInterface ds = new SCMMutiTabPrintDataInterface();
		BillData bd = getCtBillCardPanel().getBillData();
		ds.setBillData(bd);
		ds.setModuleName(getNodeCode());
		ds.setTotalLinesInOnePage(getPrintEntry().getBreakPos());

		return ds;
	}

	/**
	 * 此处插入方法说明。 功能描述:获取节点编码 输入参数: 返回值:java.lang.String 异常处理: 日期:
	 * 创建日期：(2002-6-27 20:14:10)
	 * 
	 * @return java.lang.String
	 */
	public String getNodeCode() {
		return m_sNodeCode;
	}

	/**
	 * 返回操作员。
	 * 
	 * @version (00-6-6 13:33:25)
	 * @return java.lang.String
	 */
	public String getOperator() {
		if (null != getClientEnvironment().getUser())
			return getClientEnvironment().getUser().getPrimaryKey();
		else
			return null;
	}

	/**
	 * 获得单据类型。 创建日期：(2001-11-15 8:52:43)
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getBillType() {
		return m_sBillType;
	}

	/**
	 * 此处插入方法说明。 功能描述:根据参照的pk值，检索其对应的名称 异常处理: 日期:
	 * 
	 * @return java.lang.String
	 * @param strItemKey
	 *            java.lang.String
	 * @param objPK
	 *            java.lang.Object
	 */
	public String getCardHeadRefNameByPK(String strItemKey, Object objPK) {
		if (objPK == null)
			return "";

		BillItem bi = getCtBillCardPanel().getHeadItem(strItemKey);
		if (bi == null)
			return "";

		UIRefPane uiRefPane = (UIRefPane) bi.getComponent();

		String oldPK = uiRefPane.getRefPK(); // store the old pk

		uiRefPane.setPK((String) objPK);
		String strName = uiRefPane.getRefName();
		if (strItemKey.equals("deliaddrname"))
			strName = uiRefPane.getRefCode();
		uiRefPane.setPK(oldPK); // restore the old pk

		if (null == strName)
			strName = "";
		return strName;
	}

	/**
	 * 此处插入方法说明。 功能描述: 加工数据，得到卡片列表的打印或者打印预览的数据 输入参数: 返回值: 异常处理: 日期:
	 * 
	 * @return nc.vo.dm.pub.DMVO
	 */
	public ExtendedAggregatedValueObject getValueForCardPrint(BillCardPanel bcp) {
		ExtendManageVO voRet = new ExtendManageVO();
		ManageVO dmvo = (ManageVO) ((ScmBillCardPanel) bcp).getBillValueVO(
				ManageVO.class.getName(), ManageHeaderVO.class.getName(),
				ManageItemVO.class.getName());

		String[] strItemKeys = { "ct_type", "projectname", "custname",
				"currname", "depname", "pername", "transpmodename",
				"deliaddrname", "paytermname" };

		Object objPK;
		String strName;
		ManageHeaderVO dmdvHead = (ManageHeaderVO) dmvo.getParentVO();

		for (int i = strItemKeys.length - 1; i >= 0; i--) {
			objPK = dmdvHead.getAttributeValue(strItemKeys[i]);
			strName = getCardHeadRefNameByPK(strItemKeys[i], objPK);
			dmdvHead.setAttributeValue(strItemKeys[i], strName);
		}

		// 设置合同状态为其正确的显示值，而非整型值。
		UIComboBox comboCtFlag = (UIComboBox) (getCtBillCardPanel()
				.getHeadItem("ctflag").getComponent());
		dmdvHead.setAttributeValue("ctflag", comboCtFlag.getSelectedItemName());

		BillItem bi = getCtBillCardPanel().getTailItem("opername");
		if (bi != null) {
			UIRefPane uiRefPane = (UIRefPane) bi.getComponent();
			strName = uiRefPane.getRefName();
			dmdvHead.setAttributeValue("opername", strName);
		}
		bi = getCtBillCardPanel().getTailItem("audiname");
		if (bi != null) {
			UIRefPane uiRefPane = (UIRefPane) bi.getComponent();
			strName = uiRefPane.getRefName();
			dmdvHead.setAttributeValue("audiname", strName);
		}
		if (dmvo == null || dmvo.getParentVO() == null)
			return null;
		// added by lirr 2008-11-28 打印模板设置精度
		try {
			nc.vo.ct.pub.GenMethod.setVOScale(new ManageVO[] { dmvo });
		} catch (Exception e) {
			// TODO 自动生成 catch 块
			GenMethod.handleException(this, e.getMessage(), e);
		}

		// return dmvo;
		voRet.setParentVO(dmvo.getParentVO());
		// modified by lirr 2008-11-28 打印模板设置精度
		/*
		 * ManageItemVO[] itemVOs = (ManageItemVO[]) ((ExtendManageVO)
		 * (m_vBillVO .get(m_iId - 1))).getTableVO("table");
		 */

		// ManageItemVO[] itemVOs = (ManageItemVO[])dmvo.getChildrenVO();
		// modified by lirr 2008-12-30 排序打印问题
		ManageItemVO[] itemVOs = (ManageItemVO[]) getCtBillCardPanel()
				.getBillModel("table").getBodyValueVOs(
						ManageItemVO.class.getName());

		// 打印模板中使用model代替mod，避免打印模板的公式解析错误。
		// 邵兵 on April 25, 2005
		for (int i = 0; i < itemVOs.length; i++) {
			itemVOs[i].setAttributeValue("model", itemVOs[i]
					.getAttributeValue("mod"));
		}
		// voRet.setTableVO("table",((ExtendManageVO)(m_vBillVO.get(m_iId
		// -1))).getTableVO("table"));
		voRet.setTableVO("table", itemVOs);
		voRet.setTableVO("term", ((ExtendManageVO) (m_vBillVO.get(m_iId - 1)))
				.getTableVO("term"));

		// added by lirr 2008-08-20 增加打印合同费用和大事记
		if (m_vBillVO.get(m_iId - 1).getTableVO("cost") != null) {
			voRet.setTableVO("cost", ((ExtendManageVO) (m_vBillVO
					.get(m_iId - 1))).getTableVO("cost"));
			// (ManageItemVO[])
			for (int i = 0; i < voRet.getTableVO("cost").length; i++) {
				(voRet.getTableVO("cost"))[i].setAttributeValue("costmemo",
						voRet.getTableVO("cost")[i].getAttributeValue("memo"));
			}

		}
		if (m_vBillVO.get(m_iId - 1).getTableVO("note") != null) {
			voRet.setTableVO("note", ((ExtendManageVO) (m_vBillVO
					.get(m_iId - 1))).getTableVO("note"));
			for (int i = 0; i < voRet.getTableVO("note").length; i++) {
				(voRet.getTableVO("note"))[i].setAttributeValue("notememo",
						voRet.getTableVO("note")[i].getAttributeValue("memo"));
			}

		}
		/*
		 * HashMap<String,String[]> hs = new HashMap<String,String[]>();
		 * hs.put("BD501", new String[]{"amount","ordnum"}); try{
		 * ScaleSetter.getInstance().setScale(voRet.getTableVO("table"),
		 * getCorpPrimaryKey(), hs); }catch(Exception e){ }
		 */

		return voRet;
	}

	/**
	 * 此处插入方法说明。 创建日期：(2001-11-26 20:26:46)
	 */
	protected void initRefSql() {
		// modified by lirr 2008-10-30 在参照前加上非空判断
		if (null != ((UIRefPane) getCtBillCardPanel().getHeadItem("pername")
				.getComponent()).getRefModel()) {
			m_sPerWhereSql = ((UIRefPane) getCtBillCardPanel().getHeadItem(
					"pername").getComponent()).getRefModel().getWherePart();
		}
		nc.ui.bd.ref.AbstractRefModel depRefmodel = null;
		if (null != ((UIRefPane) getCtBillCardPanel().getHeadItem("depname")
				.getComponent()).getRefModel()) {
			depRefmodel = ((UIRefPane) getCtBillCardPanel().getHeadItem(
					"depname").getComponent()).getRefModel();
		}

		// added by lirr 2008-10-24
		/*
		 * nc.ui.bd.ref.AbstractRefModel custRefmodel = ((UIRefPane)
		 * getCtBillCardPanel()
		 * .getHeadItem("custname").getComponent()).getRefModel(); String
		 * sCustSql = custRefmodel.getWherePart();
		 */
		if (null != depRefmodel) {
			String sDepSql = depRefmodel.getWherePart();
			String sInitPsn = null;
			// 根据合同类型初始化部门属性
			if (m_iCtType == CtType.PUR) {
				depRefmodel.setWherePart(sDepSql
						+ " and (deptattr='2' or deptattr='4') ");
				// deleted by lirr 2009-11-6上午10:28:47 人员不再按照部门过滤
				/*m_sPerWhereSql += " and (bd_deptdoc.deptattr='2' or bd_deptdoc.deptattr='4')";
				sInitPsn = m_sPerWhereSql
						+ " and bd_psndoc.pk_deptdoc in (select a.pk_deptdoc from bd_deptdoc a where 1=1 and "
						+ sDepSql + " )";*/
			} else if (m_iCtType == CtType.SALE) {
				depRefmodel.setWherePart(sDepSql
						+ " and (deptattr='3' or deptattr='4') ");
				// deleted by lirr 2009-11-6上午10:28:47 人员不再按照部门过滤
				/*m_sPerWhereSql += " and (bd_deptdoc.deptattr='3' or bd_deptdoc.deptattr='4')";
				sInitPsn = m_sPerWhereSql
						+ " and bd_psndoc.pk_deptdoc in (select a.pk_deptdoc from bd_deptdoc a where 1=1 and "
						+ sDepSql + " ) ";*/
			} else if (m_iCtType == CtType.OTHER) {
			    // deleted by lirr 2009-12-1上午11:10:16 谢阳要求其他合同部门不过滤
				/*depRefmodel.setWherePart(sDepSql
						+ " and deptattr not in('2','3','4') ");*/
				// deleted by lirr 2009-11-6上午10:28:47 人员不再按照部门过滤
				/*m_sPerWhereSql += " and bd_deptdoc.deptattr not in('2','3','4')";
				sInitPsn = m_sPerWhereSql
				
				 * + " and bd_psndoc.pk_deptdoc in (select a,pk_deptdoc from
				 * bd_deptdoc a where 1=1 and " + sDepSql + " ) "
				 ;
				// 过滤人员参照。
				if (null != ((UIRefPane) getCtBillCardPanel().getHeadItem(
						"pername").getComponent()).getRefModel()) {
					nc.ui.bd.ref.AbstractRefModel psnRefModel = ((UIRefPane) getCtBillCardPanel()
							.getHeadItem("pername").getComponent())
							.getRefModel();
					psnRefModel.setWherePart(sInitPsn);
				}*/

			}
		}

		// 设置客商返回简称
		if (null != ((UIRefPane) getCtBillCardPanel().getHeadItem("custname")
				.getComponent()).getRefModel()) {
			nc.ui.bd.ref.AbstractRefModel custRefmodel = ((UIRefPane) getCtBillCardPanel()
					.getHeadItem("custname").getComponent()).getRefModel();
			custRefmodel.setRefNameField("bd_cubasdoc.custshortname");

		}
		if (null != (TermRefModel) ((UIRefPane) getCtBillCardPanel()
				.getBillModel("term").getItemByKey("termcode").getComponent())
				.getRefModel()) {
			TermRefModel termRef = (TermRefModel) ((UIRefPane) getCtBillCardPanel()
					.getBillModel("term").getItemByKey("termcode")
					.getComponent()).getRefModel();
			termRef.setPK_Corp(m_sPkCorp);
		}
		// 设置合同费用参照的公司过滤
		// 邵兵 on May 9, 2005
		if (null != (ExpRefModel) ((UIRefPane) getCtBillCardPanel()
				.getBillModel("cost").getItemByKey("expcode").getComponent())
				.getRefModel()) {
			ExpRefModel expRef = (ExpRefModel) ((UIRefPane) getCtBillCardPanel()
					.getBillModel("cost").getItemByKey("expcode")
					.getComponent()).getRefModel();
			expRef.setPk_corp(m_sPkCorp);
		}

	}

	/**
	 * 此处插入方法说明。 功能描述: 输入参数: 返回值: 异常处理: 日期:
	 */
	protected void setFormularForNat(UFBoolean bOrimode) {
		// 定义计算公式
		ArrayList alFormula = null;
		// 本币依乘除法置公式
		// 折本汇率变
		alFormula = new ArrayList();
		if (bFracmode) { // 汇率为乘法模式
			// 本币价税合计=折本汇率*原币价税合计
			alFormula.add(new String[] { "natitaxsummny",
					"currrate*oritaxsummny" });
			// 本币含税单价 = 原币含税单价*折本汇率
			alFormula
					.add(new String[] { "natitaxprice", "oritaxprice*currrate" });

		} else { // 汇率为除法模式
			// 本币价税合计=原币价税合计/折本汇率
			alFormula.add(new String[] { "natitaxsummny",
					"oritaxsummny/currrate" });
			// 本币含税单价 = 原币含税单价/折本汇率
			alFormula
					.add(new String[] { "natitaxprice", "oritaxprice/currrate" });
		}
		// 本币税额=税率*本币价税合计/(1+税率)
		alFormula.add(new String[] { "natitaxmny",
				"taxration/100*natitaxsummny/(1+taxration/100)" });
		// 本币金额=本币价税合计-本币税额
		alFormula.add(new String[] { "natisum", "natitaxsummny-natitaxmny" });
		// 本币无税单价 = 本币含税单价/（1+税率/100）
		alFormula.add(new String[] { "natiprice",
				"natitaxprice/(1+taxration/100)" });
		// 出入库金额=出入库数量*本币无税单价 inoutsum->inoutnum*natiprice
		alFormula.add(new String[] { "inoutsum", "inoutnum*natiprice" });
		// 开发票金额=开发票数量*本币无税单价 invoicesum->natiprice*invoicnum
		alFormula.add(new String[] { "invoicesum", "invoicnum*natiprice" });
		getCtBillCardPanel().setFormula("currrate", alFormula);

		// 清辅币公式
		// getCtBillCardPanel().setFormula("astcurrate", null);
	}

	/**
	 * 此处插入方法说明。 功能描述: 输入参数: 返回值: 异常处理: 日期: 根据5.5需求删除辅币信息 所以此方法将不再被调用 lirr
	 * 2008-08-20
	 */
	protected void setFormularForNatAst(UFBoolean bOrimode) {
		// 定义计算公式
		ArrayList alFormula = null;
		// 本币依乘除法置公式
		// 折本汇率变
		alFormula = new ArrayList();
		if (bFracmode) { // 汇率为乘法模式
			// 本币价税合计=折本汇率*辅币价税合计
			alFormula.add(new String[] { "natitaxsummny",
					"currrate*asttaxsummny" });
			// 本币含税单价 = 辅币含税单价*折本汇率
			alFormula
					.add(new String[] { "natitaxprice", "asttaxprice*currrate" });
		} else { // 汇率为除法模式
			// 本币价税合计=辅币价税合计/折本汇率
			alFormula.add(new String[] { "natitaxsummny",
					"asttaxsummny/currrate" });
			// 本币含税单价 = 辅币含税单价/折本汇率
			alFormula
					.add(new String[] { "natitaxprice", "asttaxprice/currrate" });
		}
		// 本币税额=税率*本币价税合计/(1+税率)
		alFormula.add(new String[] { "natitaxmny",
				"taxration/100*natitaxsummny/(1+taxration/100)" });
		// 本币金额=本币价税合计-本币税额
		alFormula.add(new String[] { "natisum", "natitaxsummny-natitaxmny" });
		// 本币无税单价 = 本币含税单价/（1+税率/100）
		alFormula.add(new String[] { "natiprice",
				"natitaxprice/(1+taxration/100)" });
		// 出入库金额=出入库数量*本币无税单价 inoutsum->inoutnum*natiprice
		alFormula.add(new String[] { "inoutsum", "inoutnum*natiprice" });
		// 开发票金额=开发票数量*本币无税单价 invoicesum->natiprice*invoicnum
		alFormula.add(new String[] { "invoicesum", "invoicnum*natiprice" });
		getCtBillCardPanel().setFormula("currrate", alFormula);

		// 辅币依乘除法置公式
		// 折辅汇率变
		alFormula = new ArrayList();
		if (bOrimode == null) { // 单主币核算方式
			// 清辅币公式
			getCtBillCardPanel().setFormula("astcurrate", null);
		} else {
			if (bOrimode.booleanValue()) { // 汇率为乘法模式
				// 辅币价税合计=折辅汇率*原币价税合计
				alFormula.add(new String[] { "asttaxsummny",
						"astcurrate*oritaxsummny" });
				// 辅币的含税单价=折辅汇率*原币含税单价
				alFormula.add(new String[] { "asttaxprice",
						"oritaxprice*astcurrate" });
			} else { // 汇率为除法模式
				// 辅币价税合计=原币价税合计/折辅汇率
				alFormula.add(new String[] { "asttaxsummny",
						"oritaxsummny/astcurrate" });
				// 辅币的含税单价=原币含税单价/折辅汇率
				alFormula.add(new String[] { "asttaxprice",
						"oritaxprice/astcurrate" });
			}
			// 辅币税额=税率*辅币价税合计/(1+税率)
			alFormula.add(new String[] { "asttaxmny",
					"taxration/100*asttaxsummny/(1+taxration/100)" });
			// 辅币金额=辅币价税合计-辅币税额
			alFormula.add(new String[] { "astsum", "asttaxsummny-asttaxmny" });
			// 辅币无税单价=辅币含税单价/（1+税率）
			alFormula.add(new String[] { "astprice",
					"asttaxprice/(1+taxration/100)" });
			getCtBillCardPanel().setFormula("astcurrate", alFormula);
		}
	}

	/**
	 * 此处插入方法说明。 功能描述: 输入参数: 返回值: 异常处理: 日期:
	 * 
	 * @return java.util.ArrayList
	 * @param alvos2
	 *            java.util.ArrayList int[] arySelListHeadRows 列表下选择的单据的索引
	 */
	public ArrayList getValueForListPrint(ArrayList alvos,
			int[] arySelListHeadRows) {

		if (alvos == null || alvos.size() == 0)
			return null;

		ArrayList alvos2 = new ArrayList();

		ExtendManageVO mvo = null;
		ManageHeaderVO mhvo = null;
		ManageItemVO[] itemVOs = null;
		ExtendManageVO vo = null;
		for (int i = 0; i < alvos.size(); i++) {
			mvo = (ExtendManageVO) alvos.get(i);
			vo = new ExtendManageVO();
			vo.setParentVO((CircularlyAccessibleValueObject) (mvo.getParentVO()
					.clone()));

			itemVOs = (ManageItemVO[]) mvo.getTableVO("table");
			// 打印模板中使用model代替mod，避免打印模板的公式解析错误。
			// 邵兵 on April 25, 2005
			// modified by lirr 2008-07-18 修改原因：查询后直接选多行预览时报错，
			// 原因是除第一行记录外其他记录的表体数据没有读取,加上了表体的非空判断
			if (itemVOs != null && itemVOs.length > 0) {
				for (int index = 0; index < itemVOs.length; index++) {
					itemVOs[index].setAttributeValue("model", itemVOs[index]
							.getAttributeValue("mod"));
				}
				vo.setTableVO("table", itemVOs);
			}

			if (mvo.getTableVO("term") != null)
				vo.setTableVO("term", mvo.getTableVO("term"));
			else
				vo.setTableVO("term", new TermBb4VO[] { new TermBb4VO() });
			if (mvo.getTableVO("cost") != null)
				vo.setTableVO("cost", mvo.getTableVO("cost"));
			else
				vo.setTableVO("cost", new TermBb4VO[] { new TermBb4VO() });
			if (mvo.getTableVO("note") != null)
				vo.setTableVO("note", mvo.getTableVO("note"));
			else
				vo.setTableVO("note", new TermBb4VO[] { new TermBb4VO() });
			alvos2.add(vo);

		}

		String[] strItemKeys = { "ct_type", "projectname", "currname",
				"depname", "pername", "transpmodename", "deliaddrname",
				"paytermname", "opername", "audiname" };

		String strItemKey;
		Object objPK;

		for (int i = alvos2.size() - 1; i >= 0; i--) {
			vo = (ExtendManageVO) alvos2.get(i);
			mhvo = (ManageHeaderVO) vo.getParentVO();

			for (int j = strItemKeys.length - 1; j >= 0; j--) {
				mhvo.setAttributeValue(strItemKeys[j],
						(String) getCtBillListPanel().getHeadBillModel()
								.getValueAt(arySelListHeadRows[i],
										strItemKeys[j]));
			}
			strItemKey = "custname";
			objPK = getCtBillListPanel().getHeadBillModel().getValueAt(
					arySelListHeadRows[i], strItemKey);
			;
			mhvo.setAttributeValue(strItemKey, (objPK == null ? ""
					: (String) objPK));

			// 设置合同状态为其正确的显示值，而非整型值。
			mhvo.setAttributeValue("ctflag", (String) getCtBillListPanel()
					.getHeadBillModel().getValueAt(arySelListHeadRows[i],
							"ctflag"));
		}

		return alvos2;
	}

	/**
	 * 此处插入方法说明。 功能描述:根据参照的pk值，检索其对应的名称 输入参数: 返回值: 异常处理: 日期:
	 * 
	 * @return java.lang.String
	 * @param strItemKey
	 *            java.lang.String
	 * @param objPK
	 *            java.lang.Object
	 */
	public String getListHeadRefNameByPK(String strItemKey, Object objPK) {
		if (objPK == null)
			return "";

		BillItem bi = getCtBillListPanel().getHeadItem(strItemKey);
		if (bi == null)
			return "";

		UIRefPane uiRefPane = (UIRefPane) bi.getComponent();

		String oldPK = uiRefPane.getRefPK(); // store the old pk

		uiRefPane.setPK((String) objPK);

		String strName = uiRefPane.getRefName();

		uiRefPane.setPK(oldPK);// restore the old pk

		if (null == strName)
			strName = "";

		return strName;

	}

	/**
	 * Invoked when an action occurs.
	 */
	public void actionPerformed(java.awt.event.ActionEvent e) {
		// added by lirr 2008-07-10 合同基本页签 表体“”按钮

		if (e.getSource() == miBoCardEdit) {// "卡片编辑"
			onButtonClicked(getButtonTree().getButton(
					CTButtonConst.BTN_CARD_EDIT));
		}
		if (e.getSource() == miaddNewRowNo) {// "卡片编辑"
			onButtonClicked(getButtonTree().getButton("重排行号"));
		}
	}

	/**
	 * 辅计量改变事件处理。
	 * 
	 * @author shaobing
	 * @param e
	 *            nc.ui.pub.bill.BillEditEvent
	 */
	protected void afterAstunitEdit(nc.ui.pub.bill.BillEditEvent e) {
		int iRow = e.getRow();
		// 辅单位名称
		Object oAstUnitName = e.getValue();
		// 取辅单位
		BillItem bt = getCtBillCardPanel().getBodyItem("astmeasname");
		UIRefPane refAstUnit = (UIRefPane) bt.getComponent();

		/*
		 * // 辅单位pk Object oAstUnitPk = refAstUnit.getRefPK();
		 */
		// modified by lirr 2008-12-25
		Object oAstUnitPk = null;
		if (!m_bAddFromBillFlag) {
			oAstUnitPk = refAstUnit.getRefPK();
		} else {
			oAstUnitPk = getCtBillCardPanel().getBodyValueAt(iRow, "astmeasid");
		}

		if (isNull(oAstUnitName) || isNull(oAstUnitPk)) { // 辅计量单位为空
			// 将辅计量数据清空
			getCtBillCardPanel().setBodyValueAt(null, iRow, "transrate");
			/*
			 * getCtBillCardPanel().calculateFirChangeAboutBodyHSL(iRow,
			 * "transrate"); getCtBillCardPanel().calculateWhenBodyChanged(iRow,
			 * "transrate");
			 */
			// modified by lirr 2008-12-25
			BillEditEvent evTransrate = new BillEditEvent(getCtBillCardPanel()
					.getBodyItem("transrate"), null, "transrate", iRow);
			calcCtLinePriceMny(evTransrate);
		} else {
			// 辅计量及换算率。
			String sInvBasID = getCtBillCardPanel().getBodyValueAt(iRow,
					"invbasid").toString();
			// modified by lirr 2009-9-3下午04:53:40 减少连接数
			/*
			 * m_voInvMeas.filterMeas(getCorpPrimaryKey(), sInvBasID,
			 * refAstUnit); MeasureRateVO voMeas =
			 * m_voInvMeas.getMeasureRate(sInvBasID, oAstUnitPk.toString()); //
			 * 取换算率 UFDouble ufdRate = voMeas.getMainmeasrate();
			 */
			UFDouble ufdRate = InvTool.getInvConvRateValue(sInvBasID,
					oAstUnitPk.toString());
			getCtBillCardPanel().setBodyValueAt(oAstUnitPk.toString(), iRow,
					"astmeasid");
			getCtBillCardPanel().setBodyValueAt(ufdRate, iRow, "transrate");
			/*
			 * // ADDED by lirr 2009-1-5 判断是否固定换算率 固定换算率时不可编辑 否则可编辑 boolean
			 * isFixFlag = true; if(null !=
			 * getCtBillCardPanel().getBodyValueAt(iRow,"invid") && null !=
			 * getCtBillCardPanel().getBodyValueAt(iRow,"astmeasid")){ if(null !=
			 * m_voInvMeas.getMeasureRate((String)getCtBillCardPanel().getBodyValueAt(iRow,"invid"),(String)getCtBillCardPanel().getBodyValueAt(iRow,"astmeasid")).getFixedflag())
			 * isFixFlag =
			 * m_voInvMeas.getMeasureRate((String)getCtBillCardPanel().getBodyValueAt(iRow,"invid"),(String)getCtBillCardPanel().getBodyValueAt(iRow,"astmeasid")).getFixedflag().booleanValue(); }
			 * if(!isFixFlag) {
			 * getCtBillCardPanel().setCellEditable(iRow,"transrate",true,CTTableCode.BASE); }
			 * else{
			 * getCtBillCardPanel().setCellEditable(iRow,"transrate",false,CTTableCode.BASE);
			 * }//end
			 */
			/*
			 * getCtBillCardPanel().calculateFirChangeAboutBodyHSL(iRow,
			 * "transrate"); getCtBillCardPanel().calculateWhenBodyChanged(iRow,
			 * "transrate");
			 */
			// modified by lirr 2008-12-25
			BillEditEvent evTransrate = new BillEditEvent(getCtBillCardPanel()
					.getBodyItem("transrate"), ufdRate, "transrate", iRow);
			calcCtLinePriceMny(evTransrate);
		}
		
	}

	/**
	 * 判断Object是否为空。 2005-04-09
	 * 
	 * @author: 邵兵
	 * @para: Object oValue
	 */
	protected boolean isNull(Object oValue) {
		if (oValue instanceof Object[][]) {
			return (oValue == null || ((Object[][]) oValue).length == 0);
		} else if (oValue instanceof Object[]) {
			return (oValue == null || ((Object[]) oValue).length == 0);
		} else {
			return (oValue == null || oValue.toString().trim().length() == 0);
		}
	}

	/**
	 * 此处插入方法说明。 当业务员改动带出对应的部门。 作者：程起伍 创建日期：(2003-9-23 13:36:07) *
	 * 修改日期：2008-10-23 修改原因：参照生成合同 触发该事件时manPk为空 李冉冉
	 * 
	 * @param e
	 *            nc.ui.pub.bill.BillEditEvent
	 * @throws BusinessException
	 */
	protected void afterPersonEdit(BillEditEvent e, Boolean isFromBill)
			throws BusinessException {
		// modified by lirr 2008-12-08 参照新增的单据 如果有部门信息则不清楚 没有时按照人员获得
		/*
		 * getCtBillCardPanel().getHeadItem("depname").clearViewData();
		 * getCtBillCardPanel().getHeadItem("depid").setValue(null);
		 */
		/*if (!isFromBill) {
			getCtBillCardPanel().getHeadItem("depname").clearViewData();
			getCtBillCardPanel().getHeadItem("depid").setValue(null);
		}*/
		/*
		 * String cemployeeid = ((UIRefPane) getCtBillCardPanel().getHeadItem(
		 * "pername").getComponent()).getRefPK();
		 */
		// modified by lirr 2008-10-23
		String cemployeeid = null;
		if (!isFromBill) {
			cemployeeid = ((UIRefPane) getCtBillCardPanel().getHeadItem(
					"pername").getComponent()).getRefPK();
		} else {
			cemployeeid = getCtBillCardPanel().getHeadItem("pername")
					.getValue();
		}
		getCtBillCardPanel().getHeadItem("personnelid").setValue(cemployeeid);
		if (cemployeeid == null || cemployeeid.trim().length() < 1)
			return;

		Object oTemp = nc.ui.scm.pub.CacheTool.getColumnValue("bd_psndoc",
				"pk_psndoc", "pk_deptdoc", new String[] { cemployeeid });

		if (oTemp != null) {
			Object[] oDeptID = (Object[]) oTemp;
			/*if (!isFromBill) {
				if (oDeptID[0] != null) {
					String sDeptID = oDeptID[0].toString();
					getCtBillCardPanel().getHeadItem("depname").clearViewData();
					getCtBillCardPanel().getHeadItem("depid").setValue(null);
					((UIRefPane) getCtBillCardPanel().getHeadItem("depname")
							.getComponent()).setPK(sDeptID);
					getCtBillCardPanel().getHeadItem("depid").setValue(sDeptID);
				}
			} else {// modified by lirr 2008-12-08 参照新增的单据 如果有部门信息则不清楚 没有时按照人员获得
				if ((null != getCtBillCardPanel().getHeadItem("depid") && (StringUtil
						.isEmptyWithTrim(getCtBillCardPanel().getHeadItem(
								"depid").getValue())))
						&& oDeptID[0] != null) {
					String sDeptID = oDeptID[0].toString();
					getCtBillCardPanel().getHeadItem("depname").clearViewData();
					getCtBillCardPanel().getHeadItem("depid").setValue(null);
					((UIRefPane) getCtBillCardPanel().getHeadItem("depname")
							.getComponent()).setPK(sDeptID);
					getCtBillCardPanel().getHeadItem("depid").setValue(sDeptID);
				}
			}*/
			// modified by lirr 2009-11-10下午03:22:57 部门为空时，录入业务员带出部门的值，部门不为空时，录入业务员不带出业务员所属部门的值
			if ((null != getCtBillCardPanel().getHeadItem("depid") && (StringUtil
					.isEmptyWithTrim(getCtBillCardPanel().getHeadItem(
							"depid").getValue())))
					&& oDeptID[0] != null) {
				String sDeptID = oDeptID[0].toString();
				getCtBillCardPanel().getHeadItem("depname").clearViewData();
				getCtBillCardPanel().getHeadItem("depid").setValue(null);
				((UIRefPane) getCtBillCardPanel().getHeadItem("depname")
						.getComponent()).setPK(sDeptID);
				getCtBillCardPanel().getHeadItem("depid").setValue(sDeptID);
			}
		}
	}

	/**
	 * 此处插入方法说明。 作者：cqw 创建日期：(2003-10-18 15:21:39)
	 * 
	 * @param e
	 *            nc.ui.pub.bill.BillEditEvent
	 */
	public void afterPricePolicyEdit(BillEditEvent e) {
		int rownum = e.getRow();
		UIRefPane refPolicy = (UIRefPane) getCtBillCardPanel().getBodyItem(
				CTTableCode.BASE, "sopricecode").getComponent();
		String sName = refPolicy.getRefName();
		String sPK = refPolicy.getRefPK();
		if (sPK != null) {
			getCtBillCardPanel().setBodyValueAt(sPK, rownum, "sopriceid");
			getCtBillCardPanel().setBodyValueAt(sName, rownum, "sopricename");
		} else {
			getCtBillCardPanel().setBodyValueAt(sPK, rownum, null);
			getCtBillCardPanel().setBodyValueAt(sName, rownum, null);
		}
	}

	/**
	 * 此处插入方法说明。 功能描述:实现在列表下单击表头排序。 输入参数: 返回值: 异常处理: 作者:程起伍 日期:
	 * 
	 * @param key
	 *            java.lang.String
	 */
	public void afterSort(String key) {

		// added by lirr 2009-04-03 深圳南玻出现数据串行问题 排序后缓存未更新
		/*
		 * 需要修改saveManage方法 ，在remove之后 添加一个itemvo ，然后在重设页面（代码顺序改变）
		 * getCurVO()在onsave之前，会先从缓存中remove掉一个itemVO，所以此时调用会数组越界 //qinchao 越界
		 * 
		 * if(m_bIsCard) { if(getCurVO()!=null){ ManageItemVO[] itemvos
		 * =(ManageItemVO[])getCtBillCardPanel().getBillModel("table").getBodyValueVOs(ManageItemVO.class.getName());
		 * m_vBillVO.get(m_iId - 1).setTableVO("table", itemvos); } } else {
		 * if(getCurVO()!=null){ ManageItemVO[] itemvos
		 * =(ManageItemVO[])getCtBillListPanel().getBodyBillModel("table").getBodyValueVOs(ManageItemVO.class.getName());
		 * m_vBillVO.get(m_iId - 1).setTableVO("table", itemvos); } }
		 */
		// 增加保存后，默认将新增的单据放到vo列表的最后；
		// 但如果恰逢列表下表头排序，这个将会出现问题。
		// By Shaw Sep 26, 2006
		if (!m_bIsCard) {

			return;

		}

		// 只对状态为0(free)的时候更新排序缓存
		if (m_iBillState == 0 && getCurVO() != null) {
			ManageItemVO[] itemvos = (ManageItemVO[]) getCtBillCardPanel()
					.getBillModel("table").getBodyValueVOs(
							ManageItemVO.class.getName());
			m_vBillVO.get(m_iId - 1).setTableVO("table", itemvos);
		}
		String sCurrID = (String) getCtBillCardPanel().getHeadItem(
				"pk_ct_manage").getValueObject();
		if (sCurrID == null || sCurrID.length() == 0)
			return;
		String sBillID = null;
		for (int i = 0; i < m_vBillVO.size(); i++) {
			sBillID = (String) m_vBillVO.get(i).getParentVO()
					.getAttributeValue("pk_ct_manage");
			if (sBillID.equals(sCurrID)) {
				m_iId = i + 1;
				return;
			}
		}

		/*
		 * // 增加保存后，默认将新增的单据放到vo列表的最后； // 但如果恰逢列表下表头排序，这个将会出现问题。 // By Shaw Sep
		 * 26, 2006 if (!m_bIsCard) return;
		 * 
		 * String sCurrID = (String) getCtBillCardPanel().getHeadItem(
		 * "pk_ct_manage").getValueObject(); if (sCurrID == null ||
		 * sCurrID.length() == 0) return;
		 * 
		 * String sBillID = null; for (int i = 0; i < m_vBillVO.size(); i++) {
		 * sBillID = (String) m_vBillVO.get(i).getParentVO()
		 * .getAttributeValue("pk_ct_manage"); if (sBillID.equals(sCurrID)) {
		 * m_iId = i + 1; return; } }
		 */
	}

	/**
	 * 此处插入方法说明。 检查数据长度的合法性[相乘除得到的数据超长] 创建日期：(2002-6-27 16:16:11)
	 */
	protected boolean checkOther(AggregatedValueObject vo, ScmBillCardPanel bcp)
			throws Exception {
		boolean bret = true;
		// modified by lirr 2008-8-20 根据5.5需求删除辅币信息
		/*
		 * String[] saBodyField = new String[] { "amount", "astnum", "orisum",
		 * "oritaxmny", "oritaxsummny", "natisum", "natitaxmny",
		 * "natitaxsummny", "astsum", "asttaxmny", "asttaxsummny" }; String[]
		 * saBodyFieldName = new String[] {
		 * nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
		 * "UC000-0002282") @res "数量" ,
		 * nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
		 * "UC000-0003971") @res "辅数量" ,
		 * nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
		 * "UC000-0000919") @res "原币无税金额" ,
		 * nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
		 * "UC000-0000922") @res "原币税额" ,
		 * nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
		 * "UC000-0000902") @res "原币价税合计" ,
		 * nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
		 * "UC000-0002607") @res "本币无税金额" ,
		 * nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
		 * "UC000-0002613") @res "本币税额" ,
		 * nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
		 * "UC000-0002594") @res "本币价税合计" ,
		 * nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
		 * "UC000-0003961") @res "辅币无税金额" ,
		 * nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
		 * "UC000-0003967") @res "辅币税额" ,
		 * nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
		 * "UC000-0003948") @res "辅币价税合计" };
		 */
		String[] saBodyField = new String[] { "amount", "astnum", "orisum",
				"oritaxmny", "oritaxsummny", "natisum", "natitaxmny",
				"natitaxsummny" };
		String[] saBodyFieldName = new String[] {
				nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
						"UC000-0002282")/*
										 * @res "数量"
										 */,
				nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
						"UC000-0003971")/*
										 * @res "辅数量"
										 */,
				nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
						"UC000-0000919")/*
										 * @res "原币无税金额"
										 */,
				nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
						"UC000-0000922")/*
										 * @res "原币税额"
										 */,
				nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
						"UC000-0000902")/*
										 * @res "原币价税合计"
										 */,
				nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
						"UC000-0002607")/*
										 * @res "本币无税金额"
										 */,
				nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
						"UC000-0002613")/*
										 * @res "本币税额"
										 */,
				nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
						"UC000-0002594") /*
											 * @res "本币价税合计"
											 */
		};
		int iLen = saBodyField.length;
		int iInputLen = 0;
		ManageItemVO[] voaItem = (ManageItemVO[]) vo.getChildrenVO();
		Object obj = null;

		if (voaItem == null)
			return false;

		int voLen = voaItem.length;
		ArrayList alError = new ArrayList();
		String sRowNo = null;
		for (int i = 0; i < voLen; i++) {
			sRowNo = (String) voaItem[i].getAttributeValue("crowno");
			StringBuffer sbErr = null;
			for (int j = 0; j < iLen; j++) {
				obj = voaItem[i].getAttributeValue(saBodyField[j]);
				if (obj != null) {
					iInputLen = bcp.getBodyItem(CTTableCode.BASE,
							saBodyField[j]).getLength();
					if (obj.toString().length() > iInputLen) {
						if (sbErr == null) {
							sbErr = new StringBuffer(nc.ui.ml.NCLangRes
									.getInstance().getStrByID("SCMCOMMON",
											"UPPSCMCommon-000289")/*
																	 * @res
																	 * "行号:"
																	 */);
							sbErr.append(sRowNo);
							sbErr.append(":");
						}
						sbErr.append(saBodyFieldName[j]);
						sbErr.append(" ");
					}
				}

			}
			if (sbErr != null)
				alError.add(sbErr);
		}
		if (alError.size() > 0) {
			int ilen = alError.size();
			StringBuffer sbAllErr = new StringBuffer(nc.ui.ml.NCLangRes
					.getInstance().getStrByID("4020pub", "UPP4020pub-000110")/*
																				 * @res
																				 * "下列数据输入异常!"
																				 */
					+ "\n");
			for (int i = 0; i < ilen; i++) {
				sbAllErr.append((StringBuffer) alError.get(i));
				sbAllErr.append("\n");
			}
			MessageDialog.showErrorDlg(this, null, sbAllErr.toString());
			return false;
		}

		// 检查合同预付款是否大于合同的价税合计.
		if (bcp.getHeadItem("nprepaylimitmny") != null) {
			Object olimit = bcp.getHeadItem("nprepaylimitmny").getValue();
			if (olimit != null && olimit.toString().length() > 0) {
				UFDouble ufdtaxmny = null;
				UFDouble ufdsum = new UFDouble(0);
				for (int i = 0; i < voLen; i++) {
					// modified by lirr 2009-7-30 下午03:15:53 oritaxsummny 改为原币控制
					/*
					 * ufdtaxmny = (UFDouble) voaItem[i]
					 * .getAttributeValue("natitaxsummny");
					 */
					ufdtaxmny = (UFDouble) voaItem[i]
							.getAttributeValue("oritaxsummny");
					if (ufdtaxmny != null)
						ufdsum = ufdsum.add(ufdtaxmny);
				}

				UFDouble ufdLimit = new UFDouble(olimit.toString());
				if (ufdLimit.compareTo(ufdsum) > 0) {
					MessageDialog.showErrorDlg(this, null, nc.ui.ml.NCLangRes
							.getInstance().getStrByID("4020pub",
									"UPP4020pub-000111")/*
														 * @res
														 * "合同预付款不能大于合同本币价税合计总和!"
														 */);
					return false;
				}

				// modified by liuzy 2007-12-18 预付款限额不能为负

				if (ufdLimit.compareTo(new UFDouble("0.0")) < 0) {
					MessageDialog.showErrorDlg(this, null, nc.ui.ml.NCLangRes
							.getInstance().getStrByID("4020pub",
									"UPT4020pub-000251")/*
														 * @res "预付款限额不能为负"
														 */);
					return false;
				}
			}
		}
		return bret;

	}

	/**
	 * 此处插入方法说明。 创建日期：(2004-4-3 10:49:51)
	 * 
	 * @author：程起伍
	 * @return nc.vo.ct.pub.ManageVO
	 */
	protected ManageVO getCurVO() {// modified by lirr 增加是否是参照其他单据的判断
		ManageVO voCur = null;
		ExtendManageVO voExtendCur = null;
		if (m_iId > 0) {
			if (!m_bAddFromBillFlag) {
				voCur = new ManageVO();
				/*
				 * ExtendManageVO voExtendCur = (ExtendManageVO) m_vBillVO
				 * .elementAt(m_iId - 1);
				 */
				voExtendCur = (ExtendManageVO) m_vBillVO.elementAt(m_iId - 1);

			} else {
				voCur = new ManageVO();
				voExtendCur = (ExtendManageVO) m_vBillVOForRef
						.elementAt(m_iId - 1);
			}
			voCur = getCurVO(voCur, voExtendCur);
		}
		return voCur;
	}

	protected ManageVO getCurVO(ManageVO voCur, ExtendManageVO voExtendCur) {// modified
		// by
		// lirr
		// 增加是否是参照其他单据的判断

		ManageHeaderVO voHead = (ManageHeaderVO) voExtendCur.getParentVO();
		voCur.setParentVO(voHead);

		voCur.setChildrenVO(voExtendCur.getTableVO("table"));
		// added by lirr 2009-9-16上午09:20:28 批审时表体可能没有数据需要重新加载
		if (null == voCur.getChildrenVO() || voCur.getChildrenVO().length < 1) {
			try {
				voCur = ContractQueryHelper.queryAllBodyData(voCur);
				execFormularAfterQueryWithoutCache(new ManageVO[] { voCur });
				if (null == voCur) {
					MessageDialog.showErrorDlg(this, null, NCLangRes
							.getInstance().getStrByID("4020pub",
									"UPP4020pub-000269")/* 查询表体数据时出错 */);
				}
				voExtendCur.setTableVO("table", voCur.getChildrenVO());
				voExtendCur.setTableVO("term", voCur.getTermBb4s());
				voExtendCur.setTableVO("cost", voCur.getExpBb3s());
				voExtendCur.setTableVO("note", voCur.getMemoraBb2s());
				voExtendCur.setTableVO("history", voCur.getChangeBb1s());
				voExtendCur.setTableVO("exec", voCur.getManageExecs());
				
			} catch (Exception e) {
				GenMethod.handleException(this, e.getMessage(), e);
			}
		}
		voCur.setTermBb4s((TermBb4VO[]) voExtendCur.getTableVO("term"));
		voCur.setExpBb3s((ExpBb3VO[]) voExtendCur.getTableVO("cost"));
		voCur.setChangeBb1s((ChangeBb1VO[]) voExtendCur.getTableVO("history"));
		voCur.setManageExecs((ManageExecVO[]) voExtendCur.getTableVO("exec"));
		voCur.setMemoraBb2s((MemoraBb2VO[]) voExtendCur.getTableVO("note"));

		voCur.setBillStatus(voHead.getStatus());
		voCur.setBillType(m_sBillType);
		voCur.setVBillCode(voHead.getCt_code());
		return voCur;
	}

	/**
	 * 此处插入方法说明。 创建日期：(2008-11-5 10:49:51) 若保存后直接送审，不管是否是参照其他单据的合同
	 * 当前vo都要从m_vBillVO中取
	 * 
	 * @author：lirr
	 * @return nc.vo.ct.pub.ManageVO
	 */
	protected ManageVO getCurBillVO() {
		ManageVO voCur = null;
		// ExtendManageVO voExtendCur = null;
		if (m_iId > 0) {

			voCur = new ManageVO();

			// ExtendManageVO voExtendCur = (ExtendManageVO)
			// m_vBillVO.elementAt(m_iId - 1);
			// modified by lirr 2008-12-12
			ExtendManageVO voExtendCur = null;
			if (m_bAddFromBillFlag) {
				voExtendCur = (ExtendManageVO) m_vBillVO.elementAt(m_vBillVO
						.size() - 1);
			} else {
				voExtendCur = (ExtendManageVO) m_vBillVO.elementAt(m_iId - 1);
			}
			voCur = getCurVO(voCur, voExtendCur);

		}
		return voCur;
	}

	/**
	 * 方法功能描述：根据选中的行号获得VO
	 * <p>
	 * <b>examples:</b>
	 * <p>
	 * 使用示例
	 * <p>
	 * <b>参数说明</b>
	 * 
	 * @param index
	 * @return
	 *            <p>
	 * @author liuzy
	 * @time 2007-4-12 下午08:49:32
	 */
	protected ManageVO getCurVO(int index) {
		return null;
	}

	/**
	 * Invoked when the mouse has been clicked on a component.
	 */
	public void mouseClicked(java.awt.event.MouseEvent e) {
		if (e.getSource() == getCtBillListPanel().getHeadTable()) {
			if (e.getClickCount() == 2
					&& getCtBillListPanel().getHeadTable().getSelectedRow() >= 0) {
				// MODIFIED by lirr 2008-8-11 增加判断
				if (!m_bAddFromBillFlag) {
					onList();
					// /m_iBillState = OperationState.EDIT;
					setButtonState();
				} else {
					onEdit();
					setButtonStateForCof();
				}

			}
		}
	}

	/**
	 * Invoked when the mouse enters a component.
	 */
	public void mouseEntered(java.awt.event.MouseEvent e) {
	}

	/**
	 * Invoked when the mouse exits a component.
	 */
	public void mouseExited(java.awt.event.MouseEvent e) {
	}

	/**
	 * Invoked when a mouse button has been pressed on a component.
	 */
	public void mousePressed(java.awt.event.MouseEvent e) {
	}

	/**
	 * Invoked when a mouse button has been released on a component.
	 */
	public void mouseReleased(java.awt.event.MouseEvent e) {
	}

	/**
	 * 此处插入方法说明。 作者： <author>创建日期：(2003-9-27 20:45:21)
	 */
	public String onCancelApprove(boolean isBatch) {
		String strRtn = null;
		try {
			if (!m_bIsCard) {
				int[] selrows = getCtBillListPanel().getHeadTable()
						.getSelectedRows();
				if (selrows.length > 1) {
					onBatchAction(selrows, BatchActionHelp.sUnAudit);
					return null;
				}
			}
			ManageVO mVO = getCurVO();
			ManageVO tempMVO = getUnAuditVO(true, mVO);
			/*
			 * 修改人: wuweiping 修改时间: 22/09/2009 13:00:00 修改原因: 其它合同弃审动作取消时报空框
			 */
			if (tempMVO == null) {
				return null;
			}

			ArrayList alRet = null;
			alRet = (ArrayList) nc.ui.pub.pf.PfUtilClient.processActionFlow(
					this, "UNAPPROVE" + m_sPkUser, m_sBillType,
					getClientEnvironment().getDate().toString(), tempMVO,
					tempMVO);
			if (!nc.ui.pub.pf.PfUtilClient.isSuccess()) {
				// 如果审核顺利
				if (!nc.ui.pub.pf.PfUtilClient.isSuccess()) {
					showHintMessage(nc.ui.ml.NCLangRes.getInstance()
							.getStrByID("SCMCOMMON", "UPPSCMCommon-000235")/*
																			 * @res
																			 * "审核失败"
																			 */);
					return null;
				}
			}
			// 如果审核顺利
			// 合同PK[刷新合同状态]
			String sPK = (String) mVO.getParentVO().getAttributeValue(
					"pk_ct_manage");
			int ctStateNew = qryCtStatus(sPK).intValue();// 获得弃审后合同状态
			// 检查合同状态
			if (BillState.FREE == ctStateNew) {

				mVO.getParentVO().setAttributeValue("ctflag",
						new Integer(BillState.FREE));
				mVO.getParentVO().setAttributeValue("audiid", null);
				mVO.getParentVO().setAttributeValue("auditdate", null);
				mVO.getParentVO().setAttributeValue("taudittime", null);

				getCtBillCardPanel().setHeadItem("ctflag",
						new Integer(BillState.FREE));
				getCtBillCardPanel().setTailItem("audiname", null);
				getCtBillCardPanel().setTailItem("audiid", null);
				getCtBillCardPanel().setTailItem("auditdate", null);
				getCtBillCardPanel().setTailItem("taudittime", null);

				getCtBillListPanel().getHeadBillModel().setValueAt(
						nc.ui.ml.NCLangRes.getInstance().getStrByID(
								"4020const", BillState.STATERESID_FREE),
						m_iId - 1, "ctflag");
				getCtBillListPanel().getHeadBillModel().setValueAt(null,
						m_iId - 1, "audiname");
				getCtBillListPanel().getHeadBillModel().setValueAt(null,
						m_iId - 1, "audiid");
				getCtBillListPanel().getHeadBillModel().setValueAt(null,
						m_iId - 1, "auditdate");
				getCtBillListPanel().getHeadBillModel().setValueAt(null,
						m_iId - 1, "taudittime");

				// modified by lirr 2009-8-24下午01:39:45
				// 从动作返回ts，减少连接数
				if (alRet != null && alRet.size() > 0)
					freshStatusTs(mVO, (String) alRet.get(1));
				setAuditTime(mVO, null);
			} /*
				 * else {//modified by lirr 2008-11-27 弃审后还可能是“正在审核状态” }
				 */
			else if (BillState.CHECKGOING == ctStateNew) {
				mVO = loadHeadData(m_sPkCorp, " ct.pk_ct_manage = '" + sPK
						+ "' ")[0];
				execFormularAfterQueryWithoutCache(new ManageVO[] { mVO });
				m_vBillVO.get(m_iId - 1).setParentVO(mVO.getParentVO());
				// m_vBillVO.remove(m_iId - 1);
				// mVO = loadHeadData(m_sPkCorp,
				// " ct.pk_ct_manage = '" + sPK + "' ")[0];
				// // 加载公式
				// execFormularAfterQuery(new ManageVO[] { mVO });

				getCtBillCardPanel().setHeadItem("ctflag",
						new Integer(BillState.CHECKGOING));
				// getCtBillCardPanel()
				// .getBillModel()
				// .execFormulasWithVO(
				// (CircularlyAccessibleValueObject[]) new
				// CircularlyAccessibleValueObject[] { mVO
				// .getParentVO() },
				// getHeadTailFormulas());
				// getCtBillListPanel()
				// .getHeadBillModel()
				// .execFormulasWithVO(
				// (CircularlyAccessibleValueObject[]) new
				// CircularlyAccessibleValueObject[] { mVO
				// .getParentVO() },
				// getHeadTailFormulas());
				getCtBillCardPanel().setTailItem("audiname",
						mVO.getParentVO().getAttributeValue("audiid"));
				getCtBillCardPanel().setTailItem("audiid",
						mVO.getParentVO().getAttributeValue("audiid"));
				getCtBillCardPanel().setTailItem("auditdate",
						mVO.getParentVO().getAttributeValue("auditdate"));
				getCtBillCardPanel().setTailItem("taudittime",
						mVO.getParentVO().getAttributeValue("taudittime"));

				getCtBillListPanel().getHeadBillModel().setValueAt(ctStateNew,
						m_iId - 1, "ctflag");
				getCtBillListPanel().getHeadBillModel().setValueAt(
						mVO.getParentVO().getAttributeValue("audiid"),
						m_iId - 1, "audiname");
				getCtBillListPanel().getHeadBillModel().setValueAt(
						mVO.getParentVO().getAttributeValue("audiid"),
						m_iId - 1, "audiid");
				getCtBillListPanel().getHeadBillModel().setValueAt(
						mVO.getParentVO().getAttributeValue("auditdate"),
						m_iId - 1, "auditdate");
				getCtBillListPanel().getHeadBillModel().setValueAt(
						mVO.getParentVO().getAttributeValue("taudittime"),
						m_iId - 1, "taudittime");
				// modified by lirr 2009-8-24下午01:39:45
				// 从动作返回ts，减少连接数
				if (alRet != null && alRet.size() > 0)
					freshStatusTs(mVO, (String) alRet.get(1));
			}
			// added by lirr 2008-11-27 设置状态 */);
			setButtonState();
			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"SCMCOMMON", "UPPSCMCommon-000184")/*
														 * @res "弃审成功"
														 */);

		} catch (java.sql.SQLException e) {
			MessageDialog.showErrorDlg(this,
					nc.ui.ml.NCLangRes.getInstance().getStrByID("4020pub",
							"UPP4020pub-000066")/* @res "数据库操作错误" */, e
							.getMessage());
			strRtn = e.getMessage();

		} catch (java.rmi.RemoteException e) {
			MessageDialog.showErrorDlg(this,
					nc.ui.ml.NCLangRes.getInstance().getStrByID("4020pub",
							"UPP4020pub-000067")/* @res "远程调用失败" */, e
							.getMessage());
			strRtn = e.getMessage();

		} catch (BusinessException e) {
			strRtn = e.getMessage();
			if (e.getCause() != null)
				MessageDialog.showErrorDlg(this, null, e.getCause()
						.getMessage());
			else
				MessageDialog.showErrorDlg(this, null, e.getMessage());

		} catch (Exception e) {
			MessageDialog.showErrorDlg(this, null, e.getMessage());
			reportException(e);
			strRtn = e.getMessage();

		}
		return strRtn;
	}

	/**
	 * 此处插入方法说明。 功能描述:合同执行情况页签 输入参数: 返回值: 异常处理: 作者:程起伍 日期:
	 */
	protected void onExec() {
		if (m_bIsCard) {
			getCtBillCardPanel().getBodyTabbedPane().setSelectedIndex(5);
		} else {
			getCtBillListPanel().getBodyTabbedPane().setSelectedIndex(5);
		}
		// setButtonState();
		if (!m_bAddFromBillFlag) {
			setButtonState();
		} else {
			setButtonStateForCof();
		}
	}

	/**
	 * 此处插入方法说明。 功能描述:显示合同费用页签。 输入参数: 返回值: 异常处理: 作者:程起伍 日期:
	 */
	protected void onExp() {
		if (m_bIsCard) {
			getCtBillCardPanel().getBodyTabbedPane().setSelectedIndex(3);
		} else {
			getCtBillListPanel().getBodyTabbedPane().setSelectedIndex(3);
		}
		// setButtonState();
		if (!m_bAddFromBillFlag) {
			setButtonState();
		} else {
			setButtonStateForCof();
		}
	}

	/**
	 * 方法功能描述：根据传入的参数判断是否刷新 声明为public是为了让LongTimeTask.procclongTime方法能够调用到
	 * <p>
	 * <b>examples:</b>
	 * <p>
	 * 使用示例
	 * <p>
	 * <b>参数说明</b>
	 * 
	 * @param isFresh
	 *            <p>
	 * @author liuzy
	 * @time 2007-4-16 下午08:27:14
	 */
	public void onFresh(boolean isFresh) {
		if (isFresh)
			onFresh();
	}

	/**
	 * 此处插入方法说明。 功能描述:根据查询条件刷新界面。[按钮状态在查询之后可用] 输入参数: 返回值: 异常处理: 作者:程起伍 日期:
	 */
	protected void onFresh() {
		if (m_sQryCondition == null || m_sQryCondition.length() <= 0)
			return;
		if (haveQuestion()) {
			// 将页签转移到"合同基本"上.
			getCtBillCardPanel().getBodyTabbedPane().setSelectedIndex(
					TabState.BILL);
			getCtBillListPanel().getBodyTabbedPane().setSelectedIndex(
					TabState.BILL);
			m_iTabbedFlag = TabState.BILL;
			getCtBillCardPanel().setEnabled(false); // 单据卡片不可编辑
			m_iBillState = OperationState.FREE;

			ArrayList<ExtendManageVO> listVO = new ArrayList<ExtendManageVO>();
			try {
				// ManageVO[] arrMangevos = ContractQueryHelper.queryBill(
				// m_sPkCorp, m_sQryCondition);
				ManageVO[] arrMangevos = loadHeadData(m_sPkCorp,
						m_sQryCondition);
				if (arrMangevos == null || arrMangevos.length == 0) {
					this.showHintMessage(nc.ui.ml.NCLangRes.getInstance()
							.getStrByID("4020pub", "UPP4020pub-000070")/*
																		 * @res
																		 * "没有找到符合条件的数据"
																		 */);
					m_vBillVO.clear();
					m_iId = 0;
					getCtBillCardPanel().getBillData().clearViewData();
					getCtBillListPanel().setHeaderValueVO(null);
					getCtBillListPanel().getBodyBillModel().clearBodyData();

					// setButtonState();
					if (!m_bAddFromBillFlag) {
						setButtonState();
					} else {
						setButtonStateForCof();
					}
					return;
				}
				// 加载公式
				execFormularAfterQuery(arrMangevos);
				ExtendManageVO[] arrExtendVO = new ExtendManageVO[arrMangevos.length];

				for (int k = 0; k < arrMangevos.length; k++) {
					arrExtendVO[k] = new ExtendManageVO();
					arrExtendVO[k].setParentVO(arrMangevos[k].getParentVO());
					arrExtendVO[k].setTableVO("table", arrMangevos[k]
							.getChildrenVO());
					arrExtendVO[k].setTableVO("term", arrMangevos[k]
							.getTermBb4s());
					arrExtendVO[k].setTableVO("cost", arrMangevos[k]
							.getExpBb3s());
					arrExtendVO[k].setTableVO("note", arrMangevos[k]
							.getMemoraBb2s());
					arrExtendVO[k].setTableVO("history", arrMangevos[k]
							.getChangeBb1s());
					arrExtendVO[k].setTableVO("exec", arrMangevos[k]
							.getManageExecs());
					// 向卡片置数前先设置原币和汇率精度
					setCardOriAndCurrDigit(((ManageHeaderVO) arrMangevos[k]
							.getParentVO()).getCurrid());
					// 向卡片界面置入VO
					getCtBillCardPanel().setBillValueVO(arrExtendVO[k]);
					// 从卡片界面取出VO
					getCtBillCardPanel().getBillModel().execLoadFormula();
					getCtBillCardPanel().getBillValueVOExtended(arrExtendVO[k]);
					listVO.add(arrExtendVO[k]);
				}
				if (listVO != null && listVO.size() > 0) {
					m_vBillVO.clear();
					for (int i = 0; i < listVO.size(); i++) {

						m_vBillVO.add(listVO.get(i));
					}
					// 判断是在卡片模式下，还是在列表模式下
					m_bIsFirstClick = true; // 标志是第一次点击
					if (m_bIsCard) {
						// 切换到列表模式下
						onList();
					} else {
						// 不切换当前的列表模式，显示查询结果
						// getTabbedPane().setSelectedIndex(0); //显示单据列表
						getCtBillListPanel().getBodyTabbedPane()
								.setSelectedIndex(0);
						setListRateDigit();
						setHeaderListData();
						getCtBillListPanel().getHeadTable()
								.setRowSelectionInterval(0, 0);
						// isNeedReInit =true; //所有Table都需要重新载入数据
					}
					// m_iId = 1;
					m_bChangeFlag = false; // 标志合同不可变更
					// 所有Table都需要重新载入数据
					for (int i = 1; i < 6; i++)
						m_bIsNeedReInit[i] = true;

					setCardVOData(); // 重新初始卡片数据

					showHintMessage(nc.ui.ml.NCLangRes.getInstance()
							.getStrByID("common", "UCH007")/* @res "刷新成功" */);
				} else {
					String sMessage = nc.ui.ml.NCLangRes.getInstance()
							.getStrByID("4020pub", "UPP4020pub-000072")/*
																		 * @res
																		 * "未查到符合条件的合同！"
																		 */;
					showHintMessage(sMessage);
					MessageDialog.showHintDlg(this, null, sMessage);
				}
			} catch (Exception e) {
				nc.vo.scm.pub.SCMEnv.out(e);
			}
		}
	}

	/**
	 * 此处插入方法说明。 功能描述:显示合同变更历史页签，并以列表的形式显示合同纪录。 输入参数: 返回值: 异常处理: 作者:程起伍 日期:
	 */
	protected void onHisitory() {

		ManageVO[] voaHistory = null; // 历史纪录
		int currDigit = 2; //当前合同vo原币金额精度
		String currid = null; //原币币种id
		// 以下操作显示以前合同的记录。
		if (m_vBillVO != null) {
			// 得到当前合同
			ManageVO voCur = getCurVO();
			if (voCur != null) {
				//得到当前vo的原币币种精度
				currid = ((ManageHeaderVO) voCur.getParentVO()).getCurrid();
				try {
					Integer integer = (Integer) m_hCurrDigit.get(currid);
					if (integer != null)
						currDigit = integer.intValue();
				} catch (Exception e) {
					currDigit = 2;
				}
				String sOrignPK = (String) voCur.getParentVO()
						.getAttributeValue("pk_ct_manage");
				if (sOrignPK != null && sOrignPK.length() > 0) {
					/**
					 * m_htChanged：主键：sOrignPK当前合同的PK
					 * 值：查询后得到的ManageVO[],或者String
					 * 如果哈希表中存在当前合同的主键，说明对该合同已经查过其变更历史。
					 * 但如果它的键值类型是String说明当前合同没有历史记录
					 * 如果哈希表中不存在当前合同主键，说明对该合同还没有查询过变更历史。
					 * 查之，如果查询没有结果，将合同的主键放入哈希表，键值为String
					 * 如果返回有结果，将合同的主键放入哈希表，键值为ManageVO[]
					 */
					if (m_htChanged == null)
						m_htChanged = new Hashtable();

					if (!m_htChanged.containsKey(sOrignPK)) {
						ArrayList alCondition = new ArrayList();
						alCondition.add(0, sOrignPK);
						try {
							voaHistory = ContractQueryHelper
									.queryChangedBills(alCondition);
						} catch (Exception e) {
							nc.vo.scm.pub.SCMEnv.out(e);
							showErrorMessage(nc.ui.ml.NCLangRes.getInstance()
									.getStrByID("4020pub", "UPP4020pub-000112")/*
																				 * @res
																				 * "查询合同变更历史出错！"
																				 */);
							return;
						}
						if (voaHistory != null && voaHistory.length > 1)
							m_htChanged.put(sOrignPK, voaHistory);
						else
							m_htChanged.put(sOrignPK, "HaveQryedButNotChanged");
					} else {
						Object obj = m_htChanged.get(sOrignPK);
						if (obj instanceof ManageVO[])
							voaHistory = (ManageVO[]) m_htChanged.get(sOrignPK);
						else
							voaHistory = null;
					}
				}
			}
		}
		// 判断voaHistory是否存在变更历史
		// 如果为空或者没有变更历史（vo的长度<1）,则给出提示；
		// 否则,对话框显示
		if (voaHistory != null && voaHistory.length > 1) {
			m_dlgHistory = new CtHistoryDlg(this, m_sBillType, m_sPkUser,
					m_sPkCorp,currDigit,getCurrateDigit(currid));
			// }
			ArrayList alvo = new ArrayList();
			int iLen = voaHistory.length;
			for (int i = 0; i < iLen; i++){
			    // added by lirr 2009-11-23上午09:47:41 变更历史查询自由项 自由项vfree0不显示的问题
			    CircularlyAccessibleValueObject[] tableVOs = ((ManageVO) voaHistory[i])
          .getChildrenVO();
			    nc.ui.scm.pub.FreeVOParse freeVOParse = new nc.ui.scm.pub.FreeVOParse();
	        freeVOParse.setFreeVO(tableVOs, null, "invid", false);
	        //end added
				alvo.add((ManageVO) voaHistory[i]);
			}
		   
			m_dlgHistory.setListVO(alvo);
			m_dlgHistory.show();
		} else {
			showWarningMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"4020pub", "UPP4020pub-000113")/* @res "本合同为原始合同，没有变更记录！" */);
			return;
		}
		// 显示变更历史的页签
		if (m_bIsCard) {
			getCtBillCardPanel().getBodyTabbedPane().setSelectedIndex(4);
		} else {
			getCtBillListPanel().getBodyTabbedPane().setSelectedIndex(4);
		}
		// setButtonState();
		if (!m_bAddFromBillFlag) {
			setButtonState();
		} else {
			setButtonStateForCof();
		}
	}

	/**
	 * 此处插入方法说明。 功能描述:根据合同ID和合同类型联查上下游单据。v2.3 输入参数: 返回值: 异常处理: 作者:程起伍 日期:
	 */
	protected void onJoin() {
		if (m_iId >= 1 && m_vBillVO != null && m_vBillVO.size() > 0
				&& m_vBillVO.get(m_iId - 1) != null) {

			ManageVO voTemp = getCurVO();
			ManageHeaderVO voHead = (ManageHeaderVO) voTemp.getParentVO();
			if (voHead == null) {
				showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"4020pub", "UPP4020pub-000114")/* @res "没有联查的合同！" */);
				return;
			}
			String sBillPK = voHead.getPk_ct_manage();
			String sBillTypeCode = getBillType();
			String sVBillCode = voHead.getCt_code();
			// 如果sBillPK和sBillTypeCode为空，联查没有意义。
			if (sBillPK == null || sBillTypeCode == null) {
				showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"4020pub", "UPP4020pub-000115")/* @res "该行没有对应单据！" */);
				return;
			}

			nc.ui.scm.sourcebill.SourceBillFlowDlg soureDlg = new nc.ui.scm.sourcebill.SourceBillFlowDlg(
					this, sBillTypeCode, sBillPK, null, m_sPkUser, /* m_sPkCorp */
					sVBillCode);
			soureDlg.showModal();

			/*
			 * BillData bdMy = new BillData(m_BillCardPanel.getTempletData(
			 * m_sBillType, null, m_sPkUser, m_sPkCorp)); if (bdMy!=null) {
			 * setCardPanel(bdMy); getCtBillCardPanel().setBillData(bdMy); }
			 */

		} else {
			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"SCMCOMMON", "UPPSCMCommon-000154")/* @res "没有联查的单据！" */);
		}
		// setButtonState();
		if (!m_bAddFromBillFlag) {
			setButtonState();
		} else {
			setButtonStateForCof();
		}
	}

	/**
	 * 右键菜单 此处插入方法说明。 创建日期：(2001-3-27 11:09:34)
	 * 
	 * @param e
	 *            java.awt.event.ActionEvent
	 */
	public void onMenuItemClick(java.awt.event.ActionEvent e) {
		UIMenuItem item = (UIMenuItem) e.getSource();
		if (item == getCtBillCardPanel().getCopyLineMenuItem()) {
			getCtBillCardPanel().copyLine();

			// modify by qinchao 20090224 解决：表体右键菜单复制行、粘贴行无效问题
			if (!m_bAddFromBillFlag) {
				setButtonState();
			} else {
				setButtonStateForCof();
			}
		} else if (item == getCtBillCardPanel().getPasteLineMenuItem()) {
			// modify by qinchao 20090224 解决：表体右键菜单复制行、粘贴行无效问题
			CellControl[][] ccs = null;
			if (null != getCtBillCardPanel().getBodyItems()) {
				ccs = new CellControl[getCtBillCardPanel().getRowCount() + 1][getCtBillCardPanel()
						.getBodyItems().length];
				for (int i = 0; i < getCtBillCardPanel().getRowCount(); i++) {
					if (null != getCtBillCardPanel().getDmdo()
							.getCellControls())
						ccs[i] = getCtBillCardPanel().getDmdo()
								.getCellControls()[i];
					else
						ccs[i] = getCtBillCardPanel()
								.getDefaultRowCellControl();
				}
				ccs[getCtBillCardPanel().getRowCount()] = getCtBillCardPanel()
						.getDefaultRowCellControl();
				getCtBillCardPanel().getDmdo().setCellControls(ccs);
			}

			getCtBillCardPanel().pasteLine();

			// add by qinchao 20090224 功能：把粘贴的行的"累计付款金额"置为0
			int row = getCtBillCardPanel().getBodyPanel().getTable()
					.getSelectedRow();
			int rowlength = getCtBillCardPanel().getBillModel()
					.getPasteLineNumer();
			clearExecInfo(row,rowlength);
			updateUI();
			// end 功能：把粘贴的行的"累计付款金额"置为0

			if (!m_bAddFromBillFlag) {
				setButtonState();
			} else {
				setButtonStateForCof();
			}
		} else if (item == getCtBillCardPanel().getInsertLineMenuItem()) {
			getCtBillCardPanel().insertLine();

		} else if (item == getCtBillCardPanel().getDelLineMenuItem()) {
			onDelLine();

		} else if (item == getCtBillCardPanel().getAddLineMenuItem()) {
			onAddLine();
		} else if (item == getCtBillCardPanel().getPasteLineToTailMenuItem()) {
			getCtBillCardPanel().pasteLineToTail();

			// add by qinchao 20090224 功能：把粘贴的行的"累计付款金额"置为0
			int row = getCtBillCardPanel().getBodyPanel().getTable()
					.getRowCount();
			int rowlength = getCtBillCardPanel().getBillModel()
					.getPasteLineNumer();
			clearExecInfo(row,rowlength);
			updateUI();
			// end 功能：把粘贴的行的"累计付款金额"置为0
		}
	}

	/**
	 * 此处插入方法说明。 功能描述:显示合同大事记页签 输入参数: 返回值: 异常处理: 作者:程起伍 日期:
	 */
	protected void onNotes() {
		if (m_bIsCard) {
			getCtBillCardPanel().getBodyTabbedPane().setSelectedIndex(2);
		} else {
			getCtBillListPanel().getBodyTabbedPane().setSelectedIndex(2);
		}
		// setButtonState();
		if (!m_bAddFromBillFlag) {
			setButtonState();
		} else {
			setButtonStateForCof();
		}
	}

	/**
	 * 此处插入方法说明。 手工触发审批流. 创建日期：(2004-4-9 14:11:32)
	 * * 修改日期 2009-11-10下午01:57:07 修改人，lirr 修改原因，注释标志：
	 *@param showError 是否抛出异常 保存即送审时不抛异常
	 * @author：程起伍
	 * @return void
	 * @param void
	 */
	
	protected void onSavetoAudit(boolean showError) {

		// 新增 或 修改操作时，先做保存操作。
		if (m_iBillState == OperationState.ADD
				|| m_iBillState == OperationState.EDIT
		// added by lirr 2009-02-18
		// || !(m_iTbState == OperationState.FREE)
		)
			onSave();

		try {
			if (!m_bIsCard) {
				int[] selrows = getCtBillListPanel().getHeadTable()
						.getSelectedRows();
				if (selrows.length > 1) {
					onBatchAction(selrows, BatchActionHelp.sSendAudit);
					return;
				}
			}
			// ManageVO voCur = getCurVO();
			// modified by lirr 2008-11-05 不管是否是参照其他单据的合同 当前vo都要从m_vBillVO中取
			ManageVO voCur = getCurBillVO();
			ManageVO tempVO = getSendAuditVO(true, voCur);
			// added by lirr 2009-10-17下午02:22:05
			if (tempVO == null) {
				return;
			}
			// 触发审批流
			voCur = sendtoAudit(tempVO);
			/*
			 * Integer iNewState = qryCtStatus((String) voCur
			 * .getParentVO().getAttributeValue("pk_ct_manage"));
			 */
			Integer iNewState = (Integer) voCur.getParentVO()
					.getAttributeValue("ctflag");
			if (BillState.CHECKGOING == iNewState.intValue()) {
				m_iBillState = OperationState.CHECKGOING;
				voCur.getParentVO().setAttributeValue("ctflag",
						new Integer(BillState.CHECKGOING));
				m_iBillState = OperationState.CHECKGOING;
				// 刷新ts
				freshStatusTs(voCur, (String) voCur.getParentVO()
						.getAttributeValue("ts"));
				// 设置回m_vbillVO
				((ExtendManageVO) m_vBillVO.get(m_iId - 1)).setParentVO(voCur
						.getParentVO());

				ExtendManageVO newExtendManageVO = (ExtendManageVO) m_vBillVO
						.get(m_iId - 1);
				getCtBillCardPanel().setBillValueVO(newExtendManageVO);
				setIdtoName();
				// 执行公式
				getCtBillCardPanel().getBillModel().execLoadFormula();
				setHeaderListData();
				getCtBillCardPanel().superupdateValue(); // 更新模板状态
				getCtBillCardPanel().setEnabled(false);
				m_bErrFlag = false;
				// added by lirr 2009-01-13
				m_iBillState = OperationState.FREE;

			}
			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"SCMCOMMON", "UPPSCMCommon-000269")/*
														 * @res "送审完毕"
														 */);

		} catch (Exception e) {
			if(showError){
				showErrorMessage(e.getMessage());	
			}
			else {
				nc.vo.scm.pub.SCMEnv.out(e);
			}
			
		}
		// setButtonState();
		if (!m_bAddFromBillFlag) {
			setButtonState();
		} else {
			setButtonStateForCof();
		}
	}

	/**
	 * 此处插入方法说明。 功能描述:直接打印合同页签。 输入参数: 返回值: 异常处理: 作者:程起伍 日期:
	 */
	protected void onTabPrint() {
		BillModel model = null;
		// 表头名称
		String title = null;
		switch (m_iTabbedFlag) {
		case TabState.TERM: // 条款页签
			title = nc.ui.ml.NCLangRes.getInstance().getStrByID("40202001",
					"UPT40202001-000008")/* @res "合同条款" */;
			model = getCtBillCardPanel().getBillModel("term");
			break;
		case TabState.EXP: // 费用页签
			title = nc.ui.ml.NCLangRes.getInstance().getStrByID("40202001",
					"UPT40202001-000009")/* @res "合同费用" */;
			model = getCtBillCardPanel().getBillModel("cost");
			break;
		case TabState.NOTE: // 大事记页签
			title = nc.ui.ml.NCLangRes.getInstance().getStrByID("40202001",
					"UPT40202001-000010")/* @res "合同大事记" */;
			model = getCtBillCardPanel().getBillModel("note");
			break;
		case TabState.CHANGE: // 变更页签
			title = nc.ui.ml.NCLangRes.getInstance().getStrByID("40202001",
					"UPT40202001-000030")/* @res "合同变更" */;
			model = getCtBillCardPanel().getBillModel("history");
			break;
		case TabState.EXEC: // 合同执行情况
			title = nc.ui.ml.NCLangRes.getInstance().getStrByID("4020pub",
					"UPP4020pub-000172")/* @res "合同执行过程" */;
			model = getCtBillCardPanel().getBillModel("exec");
			break;
		default:
			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"SCMCOMMON", "UPPSCMCommon-000173")/* @res "没有页签" */);
			return;
		}

		int rowCount = model.getRowCount();
		int colCount = model.getColumnCount();
		if (rowCount <= 0) {
			showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"SCMCOMMON", "UPPSCMCommon-000166")/* @res "没有数据需要打印" */);
			return;
		}
		Vector vColKey = new Vector();
		nc.ui.pub.bill.BillItem item = null;
		for (int i = 0; i < colCount; i++) {
			item = model.getBodyItems()[i];
			if (item.isShow())
				vColKey.add(item.getKey());
		}
		colCount = vColKey.size();
		if (colCount <= 0)
			return;

		String[][] colname = null;
		int[] colwidth = new int[colCount];
		int[] alignflag = new int[colCount];
		// 表头名称
		colname = new String[1][colCount];
		for (int i = 0; i < colCount; i++) {
			item = model.getItemByKey((String) vColKey.get(i));
			colname[0][i] = item.getName();
			colwidth[i] = item.getWidth();

			if (item.getDataType() == nc.ui.pub.bill.BillItem.STRING)
				alignflag[i] = 0;
			else if (item.getDataType() == nc.ui.pub.bill.BillItem.DECIMAL
					|| item.getDataType() == nc.ui.pub.bill.BillItem.INTEGER)
				alignflag[i] = 2;
			else
				alignflag[i] = 1;

		}

		// 表体数据（去掉隐藏列）
		Object[][] data = new Object[rowCount][colCount];
		for (int i = 0; i < rowCount; i++) {

			for (int j = 0; j < colCount; j++)
				data[i][j] = model.getValueAt(i, (String) vColKey.get(j));

		}
		// 准备打印
		nc.ui.pub.print.PrintDirectEntry print = new nc.ui.pub.print.PrintDirectEntry();
		print.setColNames(colname);
		print.setData(data);
		print.setTitle(title);
		print.setAlignFlag(alignflag);
		// 表格数据
		print.setColWidth(colwidth);
		print.preview(); // 打印预览
		// setButtonState();
		if (!m_bAddFromBillFlag) {
			setButtonState();
		} else {
			setButtonStateForCof();
		}
	}

	/**
	 * 此处插入方法说明。 功能描述:显示合同条款页签 输入参数: 返回值: 异常处理: 作者:程起伍 日期:
	 */
	protected void onTerm() {
		// getTabbedPane().setSelectedIndex(1);
		if (m_bIsCard) {
			getCtBillCardPanel().getBodyTabbedPane().setSelectedIndex(1);
		} else {
			getCtBillListPanel().getBodyTabbedPane().setSelectedIndex(1);
		}
		// setButtonState();
		if (!m_bAddFromBillFlag) {
			setButtonState();
		} else {
			setButtonStateForCof();
		}
	}

	/**
	 * 此处插入方法说明。 查询合同状态 作者：程起伍 创建日期：(2003-9-27 16:59:49)
	 * 
	 * @return java.lang.Integer
	 * @param sPK
	 *            java.lang.String
	 */
	protected Integer qryCtStatus(String sPK) {
		Integer iRet = null;
		try {
			iRet = ContractQueryHelper.qryCtStatus(sPK);
		} catch (Exception e) {
			nc.vo.scm.pub.SCMEnv.out(e);
		}
		return iRet;
	}

	/**
	 * 此处插入方法说明。 功能描述:保存变更后的合同。 输入参数: 返回值: 异常处理: 作者:程起伍 日期:
	 */
	protected boolean saveChangedCT() {
		try {
			m_timer.start("合同变更保存开始："); /*-=notranslate=-*/
			setNameToID();
			// 过滤空行
			getCtBillCardPanel().stopEditing();
			// 获得界面VO
			// 得到界面VOO
			ExtendManageVO vo = (ExtendManageVO) getCtBillCardPanel()
					.getBillValueVOExtended(
							ExtendManageVO.class.getName(),
							ManageHeaderVO.class.getName(),
							new String[] { ManageItemVO.class.getName(),
									TermBb4VO.class.getName(),
									ExpBb3VO.class.getName(),
									MemoraBb2VO.class.getName(),
									ChangeBb1VO.class.getName(),
									ManageExecVO.class.getName() });
			ManageVO newVO = transVO(vo);
			// 获得原来的VO
			ManageVO oldVO = getCurVO();
			ExtendManageVO oldExtendManageVO = (ExtendManageVO) m_vBillVO
					.elementAt(m_iId - 1);

			ExtendManageVO voExtendChanged = (ExtendManageVO) getCtBillCardPanel()
					.getBillValueChangeVOExtended(
							ExtendManageVO.class.getName(),
							ManageHeaderVO.class.getName(),
							new String[] { ManageItemVO.class.getName(),
									TermBb4VO.class.getName(),
									ExpBb3VO.class.getName(),
									MemoraBb2VO.class.getName(),
									ChangeBb1VO.class.getName(),
									ManageExecVO.class.getName() });
			ManageVO ChangedVO = transVO(voExtendChanged);
			ManageItemVO[] ChangedItemVO = (ManageItemVO[]) ChangedVO
					.getChildrenVO();
			Vector vDelItem = new Vector();
			for (int i = 0; i < ChangedItemVO.length; i++) {
				if (ChangedItemVO[i].getStatus() == 3)
					vDelItem.addElement(ChangedItemVO[i]);
			}

			// 如果有删除的表体项,则加入到NewManageVO中
			if (vDelItem.size() > 0) {
				ManageItemVO[] ItemVO = (ManageItemVO[]) newVO.getChildrenVO();
				ManageItemVO[] newItemVO = new ManageItemVO[ItemVO.length
						+ vDelItem.size()];
				for (int i = 0; i < ItemVO.length; i++) {
					newItemVO[i] = ItemVO[i];
				}
				for (int i = 0; i < vDelItem.size(); i++) {
					newItemVO[i + ItemVO.length] = (ManageItemVO) vDelItem
							.elementAt(i);
				}
				newVO.setChildrenVO(newItemVO);
			}

			// 检查输入的合法性
			newVO.validate();
			// 如果检查没有通过则返回
			if (checkSave(newVO) == false)
				return false;
			// 表头财 务累计原币收/付款总额
			UFDouble ufnarapamount = getAraptotalgpamount(newVO);
			UFDouble ufntotal = new UFDouble(0);
			UFDouble ufshntotal = new UFDouble(0);
			/*
			 * if (null !=
			 * newVO.getParentVO().getAttributeValue("ntotalgpamount")) {
			 * ufntotal = (UFDouble) ((ManageHeaderVO) newVO.getParentVO())
			 * .getAttributeValue("ntotalgpamount"); }
			 */
			// modified by lirr 2009-7-17 下午02:09:35 中铁物资改为原币控制
			if (null != newVO.getParentVO().getAttributeValue(
					"noritotalgpamount")) {
				ufntotal = (UFDouble) ((ManageHeaderVO) newVO.getParentVO())
						.getAttributeValue("noritotalgpamount");
			}
			 // added by lirr 2009-11-21下午01:32:46累计原币应收付款总额
			if (null != newVO.getParentVO().getAttributeValue(
      "norigpshamount")) {
        ufshntotal = (UFDouble) ((ManageHeaderVO) newVO.getParentVO())
        .getAttributeValue("norigpshamount");
  }
			if (ufnarapamount.compareTo(ufntotal) < 0) {

				String sMessage = nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"4020pub", "UPT4020pub-000260")
				/*
				 * @res 合同的“原币价税合计”之和不能小于“累计原币收/付款总额”！"
				 */;

				MessageDialog.showWarningDlg(this, null, sMessage);
				return false;
			}
			if (ufnarapamount.compareTo(ufshntotal) < 0) {

	        String sMessage = nc.ui.ml.NCLangRes.getInstance().getStrByID(
	            "4020pub", "UPT4020pub-000277")
	        /*
	         * @res 合同的“原币价税合计”之和不能小于“累计原币应收/付款总额”！"
	         */;

	        MessageDialog.showWarningDlg(this, null, sMessage);
	        return false;
	      }
			String currid = newVO.getParentVO().getCurrid();
			newVO.getParentVO().setAttributeValue(
					"naraptotalgpamount",
					new UFDouble(ufnarapamount.toDouble(),
							((Integer) m_hCurrDigitcw.get(currid)).intValue()));
			// 给表头VO赋上pk_corp和ifearly的值

			// 修改时间：2009/10/09 修改人:wuweiping 修改原因: 合同审批流中，修改合同把PK_corp更新了
			if (m_iBillState == OperationState.ADD) {
				((ManageHeaderVO) newVO.getParentVO()).setPk_corp(m_sPkCorp);
			}
			// ((ManageHeaderVO) newVO.getParentVO()).setPk_corp(m_sPkCorp);
			((ManageHeaderVO) newVO.getParentVO()).setIfearly(m_UFbIfEarly);
			// 判断是否保留最初制单人 added by lirr 2009-02-10
			if (m_isSaveInitOper.booleanValue() == false)
				((ManageHeaderVO) newVO.getParentVO()).setOperid(m_sPkUser);

			((ManageHeaderVO) newVO.getParentVO()).setVusername(m_sUserName);
			newVO.getParentVO().setAttributeValue("coperatoridnow", m_sPkUser);

			newVO.getParentVO().setAttributeValue("clastoperatorid", m_sPkUser);
			newVO.getParentVO().setAttributeValue("vlastoperatorname",
					m_sPkUser);

			// 给表体VO赋上pk_corp
			ManageItemVO[] bodyVO = (ManageItemVO[]) newVO.getChildrenVO();
			for (int i = 0; i < newVO.getChildrenVO().length; i++) {
				bodyVO[i].setPk_corp(m_sPkCorp);
				// 如果有新增的行，那么就必须给pk_ct_manage赋值
				if (bodyVO[i].getStatus() == 2)
					bodyVO[i].setPk_ct_manage(newVO.getParentVO()
							.getPrimaryKey().toString());
			}
			// 设置VO状态
			newVO.setIsChange(new UFBoolean(true));

			newVO.setChangeBb1s(oldVO.getChangeBb1s());
			newVO.setExpBb3s(oldVO.getExpBb3s());
			newVO.setManageExecs(oldVO.getManageExecs());
			newVO.setMemoraBb2s(oldVO.getMemoraBb2s());
			newVO.setTermBb4s(oldVO.getTermBb4s());

			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"4020pub", "UPP4020pub-000116")/* @res "开始保存变更后的合同……" */);

			newVO.setIsSaveCheck(true);
			/*
			 * UFDouble ufnarapamount = new UFDouble(0); if (null !=
			 * newVO.getChildrenVO() && newVO.getChildrenVO().length > 0) { for
			 * (int i = 0; i < newVO.getChildrenVO().length; i++) { if (null !=
			 * newVO.getChildrenVO()[i] .getAttributeValue("natitaxsummny"))
			 * ufnarapamount = ufnarapamount.add((UFDouble) newVO
			 * .getChildrenVO()[i] .getAttributeValue("natitaxsummny")); } } //
			 * 表头财 务本币累计收/付款总额
			 * newVO.getParentVO().setAttributeValue("naraptotalgpamount", new
			 * UFDouble(ufnarapamount.toDouble(), m_iMainCurrDigitcw));
			 */
			while (true) {
				try {
					newVO = (ManageVO) nc.ui.pub.pf.PfUtilClient.processAction(
							"MODIFY", m_sBillType, getClientEnvironment()
									.getDate().toString(), newVO, oldVO);
					break;
				} catch (OverMaxPriceException e) {
					if (showYesNoMessage(e.getMessage()) == MessageDialog.ID_YES) {
						newVO.setIsSaveCheck(false);
						continue;
					} else {
						m_bErrFlag = true;
						showHintMessage(nc.ui.ml.NCLangRes.getInstance()
								.getStrByID("4020pub", "UPP4020pub-000077")/*
																			 * @res
																			 * "保存失败！"
																			 */);
						return false;
					}
				}
			}

			newVO.setIsSaveCheck(true);
			// newVO = (ManageVO) nc.ui.pub.pf.PfUtilClient.processAction(
			// "MODIFY", m_sBillType, getClientEnvironment().getDate()
			// .toString(), newVO, oldVO);
			// 所有Table都需要重新载入数据
			for (int i = 1; i < 6; i++)
				m_bIsNeedReInit[i] = true;

			// 回写表头时间戳
			getCtBillCardPanel().setHeadItem("ts",
					newVO.getParentVO().getAttributeValue("ts"));
			ManageItemVO[] itemVO = (ManageItemVO[]) newVO.getChildrenVO();
			for (int i = 0; i < itemVO.length; i++) {
				// 回写表体时间戳
				getCtBillCardPanel().setBodyValueAt(itemVO[i].getTs(), i, "ts");

			}
			oldExtendManageVO.setParentVO(newVO.getParentVO());
			oldExtendManageVO.setTableVO("table", newVO.getChildrenVO());
			oldExtendManageVO.setTableVO("history", newVO.getChangeBb1s());
			getCtBillCardPanel().setBillValueVO(oldExtendManageVO);
			m_vBillVO.remove(m_iId - 1);
			m_vBillVO.insertElementAt(oldExtendManageVO, (m_iId - 1));

			m_iBillState = OperationState.FREE;
			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"4020pub", "UPP4020pub-000117")/* @res "变更成功！" */);

			getCtBillCardPanel().updateValue();
			getCtBillCardPanel().setEnabled(false);

			m_bErrFlag = false;
			// 执行公式
			getCtBillCardPanel().getBillModel().execLoadFormula();
			// 如果该合同已经查询过历史记录，应该在哈希表中清除纪录。以便能够重新查询。
			String pk_ct_manage = (String) newVO.getParentVO()
					.getAttributeValue("pk_ct_manage");
			if (m_htChanged != null && pk_ct_manage != null
					&& m_htChanged.containsKey(pk_ct_manage))
				m_htChanged.remove(pk_ct_manage);
			m_timer.showExecuteTime("变更保存结束"); /*-=notranslate=-*/

		} catch (Exception e) {

			nc.vo.scm.pub.SCMEnv.out(e);
			showErrorMessage(e.getMessage());
			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"4020pub", "UPP4020pub-000077")/* @res "保存失败！" */);
			return false;
		}
		return true;
	}
	/**
	 * 此处插入方法说明。 触发审批流. 创建日期：(2004-4-9 14:11:32)
	 * 
	 * @author：程起伍
	 * @return nc.vo.ct.pub.ManageVO
	 * @param vo
	 *            nc.vo.ct.pub.ManageVO
	 */
	protected ManageVO sendtoAudit(ManageVO tempMVO) throws Exception {
		if (tempMVO == null)
			return null;
		// added by lirr 2008-10-28 送审后合同状态改为“送审中”
		// ManageVO tempMVO = new ManageVO();

		// tempMVO = vo.clone(vo);
		//
		// tempMVO.getParentVO().setAttributeValue("ctflag",
		// new Integer(BillState.CHECKGOING));
		// tempMVO.getParentVO()
		// .setAttributeValue("coperatoridnow", getOperator());
		// // added by lirr 2009-7-9下午04:27:52 设置单据类型 送审时使用
		// tempMVO.setBillType(m_sBillType);
		// tempMVO.setOldBillStatus(BillState.FREE);
		// tempMVO.setBillStatus(BillState.CHECKGOING);
		ArrayList alRet = null;

		alRet = (ArrayList) nc.ui.pub.pf.PfUtilClient.processAction("SAVE",
				m_sBillType, getClientEnvironment().getDate().toString(),
				tempMVO, tempMVO);
		// modified by lirr 2009-8-24上午10:42:00 刷新ts不再走后台，在动作中返回
		if (alRet != null && alRet.size() > 0)
			tempMVO.getParentVO()
					.setAttributeValue("ts", (String) alRet.get(1));
		return tempMVO;
	}

	/**
	 * 编辑后事件。 创建日期：(2001-3-23 2:02:27)
	 * 
	 * @param e
	 *            ufbill.BillEditEvent
	 */
	public void setCardOriAndCurrDigit(String sCurrid) {

		// 获得此币种的小数位数
		int currDigit;
		try {
			Integer integer = (Integer) m_hCurrDigit.get(sCurrid);
			if (integer != null)
				currDigit = integer.intValue();
			else
				currDigit = 2;
		} catch (Exception e) {
			currDigit = 2;
		}

		// 定义原币金额小数位数
		getCtBillCardPanel().getBodyItem(CTTableCode.BASE, "orisum")
				.setDecimalDigits(currDigit);
		getCtBillCardPanel().getBodyItem(CTTableCode.BASE, "oritaxmny")
				.setDecimalDigits(currDigit);
		getCtBillCardPanel().getBodyItem(CTTableCode.BASE, "oritaxsummny")
				.setDecimalDigits(currDigit);

		// m_iRateDigit = PubSetUI.getBothExchRateDigit(m_sPkCorp, sCurrid);
		/*
		 * getCtBillCardPanel().getHeadItem("currrate").setDecimalDigits(
		 * m_iRateDigit[0]);
		 */
		// modified by lirr 2008-12-24 折本汇率精度新方法 since v55
		getCtBillCardPanel().getHeadItem("currrate").setDecimalDigits(
				getCurrateDigit(sCurrid));
		/*
		 * getCtBillCardPanel().getHeadItem("astcurrate").setDecimalDigits(
		 * m_iRateDigit[1]);
		 */

	}

	/**
	 * 此处插入方法说明。 设置数量金额等长度. 创建日期：(2003-11-10 10:24:19)
	 */
	protected void setInputLen() {
		int iZHENG = DBDataLeng.INT_LENG1;// ==12
		// 辅数量，辅币
		/*
		 * getCtBillCardPanel().getBodyItem(CTTableCode.BASE,
		 * "astnum").setLength( iZHENG + 1 + m_iFracAmountDigit);
		 * getCtBillCardPanel().getBodyItem(CTTableCode.BASE, "astprice")
		 * .setLength(iZHENG + 1 + m_iPriceDigit);
		 * getCtBillCardPanel().getBodyItem(CTTableCode.BASE, "asttaxprice")
		 * .setLength(iZHENG + 1 + m_iPriceDigit);
		 */

		// deleted by lirr 2008-8-20 根据5.5需求删除辅币信息
		/*
		 * getCtBillCardPanel().getBodyItem(CTTableCode.BASE,
		 * "astsum").setLength( iZHENG + 1 + m_iFracCurrDigit);
		 * getCtBillCardPanel().getBodyItem(CTTableCode.BASE, "asttaxmny")
		 * .setLength(iZHENG + 1 + m_iFracCurrDigit);
		 * getCtBillCardPanel().getBodyItem(CTTableCode.BASE, "asttaxsummny")
		 * .setLength(iZHENG + 1 + m_iFracCurrDigit);
		 */

		// 定义数量、单价
		getCtBillCardPanel().getBodyItem(CTTableCode.BASE, "amount").setLength(
				iZHENG + 1 + m_iAmountDigit);
		getCtBillCardPanel().getBodyItem(CTTableCode.BASE, "ordnum").setLength(
				iZHENG + 1 + m_iAmountDigit);

		getCtBillCardPanel().getBodyItem(CTTableCode.BASE, "oriprice")
				.setLength(iZHENG + 1 + m_iPriceDigit);

		getCtBillCardPanel().getBodyItem(CTTableCode.BASE, "oritaxprice")
				.setLength(iZHENG + 1 + m_iPriceDigit);

		getCtBillCardPanel().getBodyItem(CTTableCode.BASE, "invoicnum")
				.setLength(iZHENG + 1 + m_iAmountDigit);

		getCtBillCardPanel().getBodyItem(CTTableCode.BASE, "inoutsum")
				.setLength(iZHENG + 1 + m_iMainCurrDigit);
		getCtBillCardPanel().getBodyItem(CTTableCode.BASE, "invoicesum")
				.setLength(iZHENG + 1 + m_iMainCurrDigit);
		getCtBillCardPanel().getBodyItem(CTTableCode.BASE, "paysum").setLength(
				iZHENG + 1 + m_iMainCurrDigit);

		// 初始换算率
		getCtBillCardPanel().getBodyItem(CTTableCode.BASE, "transrate")
				.setLength(iZHENG + 1 + m_iHslDigit);

		// 预付款限额
		if (getCtBillCardPanel().getHeadItem("nprepaylimitmny") != null)
			getCtBillCardPanel().getHeadItem("nprepaylimitmny").setLength(
					iZHENG + 1 + m_iMainCurrDigitcw);
		//
		// 预付款added by lirr 2008-10-15
		if (getCtBillCardPanel().getHeadItem("nprepaymny") != null)
			getCtBillCardPanel().getHeadItem("nprepaymny").setLength(
					iZHENG + 1 + m_iMainCurrDigitcw);
		// 累计收付款总额added by lirr 2008-10-15
		if (getCtBillCardPanel().getHeadItem("ntotalgpamount") != null)
			getCtBillCardPanel().getHeadItem("ntotalgpamount").setLength(
					iZHENG + 1 + m_iMainCurrDigitcw);
		// 累计应收/付款总额added by lirr 2008-10-15
		if (getCtBillCardPanel().getHeadItem("ntotalgpshamount") != null)
			getCtBillCardPanel().getHeadItem("ntotalgpshamount").setLength(
					iZHENG + 1 + m_iMainCurrDigitcw);
		// 累计应收/付款金额added by lirr 2008-10-15
		if (getCtBillCardPanel().getBodyItem(CTTableCode.BASE, "ntotalgpmny") != null)
			getCtBillCardPanel().getBodyItem(CTTableCode.BASE, "ntotalgpmny")
					.setLength(iZHENG + 1 + m_iMainCurrDigitcw);
		// 累计应收/付款金额added by lirr 2008-10-15
		if (getCtBillCardPanel().getBodyItem(CTTableCode.BASE, "ntotalshgpmny") != null)
			getCtBillCardPanel().getBodyItem(CTTableCode.BASE, "ntotalshgpmny")
					.setLength(iZHENG + 1 + m_iMainCurrDigitcw);

		// 税率
		int iTaxdigit = getCtBillCardPanel().getBodyItem(CTTableCode.BASE,
				"taxration").getDecimalDigits();
		getCtBillCardPanel().getBodyItem(CTTableCode.BASE, "taxration")
				.setLength(DBDataLeng.INT_LENG2 + 1 + iTaxdigit);

		// 汇率
		int iCurrate = getCtBillCardPanel().getHeadItem("currrate")
				.getDecimalDigits();
		getCtBillCardPanel().getHeadItem("currrate").setLength(
				iZHENG + 1 + iCurrate);

		// 折辅汇率
		/*
		 * int iAstCurrate = getCtBillCardPanel().getHeadItem("astcurrate")
		 * .getDecimalDigits();
		 * getCtBillCardPanel().getHeadItem("currrate").setLength( iZHENG + 1 +
		 * iAstCurrate);
		 */
		getCtBillCardPanel().getHeadItem("currrate").setLength(iZHENG + 1);

		// 原币单价不能为负数:
		((nc.ui.pub.beans.UIRefPane) (getCtBillCardPanel().getBodyItem(
				CTTableCode.BASE, "oriprice").getComponent())).setDelStr("-");
		((nc.ui.pub.beans.UIRefPane) (getCtBillCardPanel().getBodyItem(
				CTTableCode.BASE, "oritaxprice").getComponent()))
				.setDelStr("-");
		((nc.ui.pub.beans.UIRefPane) (getCtBillCardPanel().getBodyItem(
				CTTableCode.BASE, "taxration").getComponent())).setDelStr("-");

	}

	/**
	 * 此处插入方法说明 创建日期：(2003-10-22 19:09:33)
	 * 
	 * @author：程起伍
	 */
	private void setNameToID() {
		getCtBillCardPanel().getHeadItem("projectid").setValue(
				getCtBillCardPanel().getHeadItem("projectname").getValue());

		getCtBillCardPanel().getHeadItem("pk_ct_type").setValue(
				getCtBillCardPanel().getHeadItem("ct_type").getValue());

		getCtBillCardPanel().getHeadItem("custid").setValue(
				getCtBillCardPanel().getHeadItem("custname").getValue());

		getCtBillCardPanel().getHeadItem("currid").setValue(
				getCtBillCardPanel().getHeadItem("currname").getValue());

		getCtBillCardPanel().getHeadItem("depid").setValue(
				getCtBillCardPanel().getHeadItem("depname").getValue());

		getCtBillCardPanel().getHeadItem("personnelid").setValue(
				getCtBillCardPanel().getHeadItem("pername").getValue());

		getCtBillCardPanel().getHeadItem("transpmode").setValue(
				getCtBillCardPanel().getHeadItem("transpmodename").getValue());

		getCtBillCardPanel().getHeadItem("deliaddr").setValue(
				getCtBillCardPanel().getHeadItem("deliaddrname").getValue());

		getCtBillCardPanel().getHeadItem("payterm").setValue(
				getCtBillCardPanel().getHeadItem("paytermname").getValue());

		getCtBillCardPanel().getTailItem("operid").setValue(
				getCtBillCardPanel().getTailItem("opername").getValue());

		getCtBillCardPanel().getTailItem("audiid").setValue(
				getCtBillCardPanel().getTailItem("audiname").getValue());
	}

	/**
	 * 此处插入方法说明。 功能描述:根据其它按钮下的子按钮状态和按钮的权限改变页签的状态，使页签具有权限控制。 输入参数: 返回值: 异常处理:
	 * 作者:程起伍 日期:
	 * 
	 * @param bTerm
	 *            boolean 合同条款按钮的状态
	 * @param bExp
	 *            boolean 合同费用按钮的状态
	 * @param bNotes
	 *            boolean 合同大记事按钮的状态
	 * @param bModify
	 *            boolean 合同变更按钮的状态
	 * @param bExec
	 *            boolean 执行过程按钮的状态
	 */
	protected void setTabbedState(boolean bTerm, boolean bExp, boolean bNotes,
			boolean bModify, boolean bExec) {
		int size = getCtBillCardPanel().getBodyTabbedPane().getTabCount();
		// 将页签按钮设置为不可用
		getButtonTree().getButton(CTButtonConst.BTN_CTOTHER_TERM).setVisible(
				false);
		getButtonTree().getButton(CTButtonConst.BTN_CTOTHER_NOTE).setVisible(
				false);
		getButtonTree().getButton(CTButtonConst.BTN_CTOTHER_EXP).setVisible(
				false);
		getButtonTree().getButton(CTButtonConst.BTN_CTOTHER_CHANGEHISTORY)
				.setVisible(false);
		getButtonTree().getButton(CTButtonConst.BTN_CTOTHER_CHANGEHISTORY)
				.setVisible(false);

		for (int i = 0; i < size; i++) {
			if (getCtBillCardPanel().getBodyTabbedPane().getComponentAt(i) != null) {
				UIScrollPane uiPanel = (UIScrollPane) getCtBillCardPanel()
						.getBodyTabbedPane().getComponentAt(i);
				String tablecode = getCtBillCardPanel().getTableCode(uiPanel,
						nc.ui.pub.bill.IBillItem.BODY);
				if ("term".equals(tablecode)) {
					getButtonTree().getButton(CTButtonConst.BTN_CTOTHER_TERM)
							.setVisible(true);
					getCtBillCardPanel().getBodyTabbedPane().setEnabledAt(
							i,
							bTerm
									&& getButtonTree().getButton(
											CTButtonConst.BTN_CTOTHER_TERM)
											.isPower()
									&& getButtonTree().getButton(
											CTButtonConst.BTN_CTOTHER)
											.isPower());
					getCtBillListPanel().getBodyTabbedPane().setEnabledAt(
							i,
							bTerm
									&& getButtonTree().getButton(
											CTButtonConst.BTN_CTOTHER_TERM)
											.isPower()
									&& getButtonTree().getButton(
											CTButtonConst.BTN_CTOTHER)
											.isPower());
				} else if ("cost".equals(tablecode)) {
					getButtonTree().getButton(CTButtonConst.BTN_CTOTHER_NOTE)
							.setVisible(true);
					getCtBillCardPanel().getBodyTabbedPane().setEnabledAt(
							i,
							bExp
									&& getButtonTree().getButton(
											CTButtonConst.BTN_CTOTHER_NOTE)
											.isPower()
									&& getButtonTree().getButton(
											CTButtonConst.BTN_CTOTHER)
											.isPower());
					getCtBillListPanel().getBodyTabbedPane().setEnabledAt(
							i,
							bExp
									&& getButtonTree().getButton(
											CTButtonConst.BTN_CTOTHER_NOTE)
											.isPower()
									&& getButtonTree().getButton(
											CTButtonConst.BTN_CTOTHER)
											.isPower());
				} else if ("note".equals(tablecode)) {
					getButtonTree().getButton(CTButtonConst.BTN_CTOTHER_EXP)
							.setVisible(true);
					getCtBillCardPanel().getBodyTabbedPane().setEnabledAt(
							i,
							bNotes
									&& getButtonTree().getButton(
											CTButtonConst.BTN_CTOTHER_EXP)
											.isPower()
									&& getButtonTree().getButton(
											CTButtonConst.BTN_CTOTHER)
											.isPower());
					getCtBillListPanel().getBodyTabbedPane().setEnabledAt(
							i,
							bNotes
									&& getButtonTree().getButton(
											CTButtonConst.BTN_CTOTHER_EXP)
											.isPower()
									&& getButtonTree().getButton(
											CTButtonConst.BTN_CTOTHER)
											.isPower());
				} else if ("history".equals(tablecode)) {
					getButtonTree().getButton(
							CTButtonConst.BTN_CTOTHER_CHANGEHISTORY)
							.setVisible(true);
					getCtBillCardPanel()
							.getBodyTabbedPane()
							.setEnabledAt(
									i,
									bModify
											&& getButtonTree()
													.getButton(
															CTButtonConst.BTN_CTOTHER_CHANGEHISTORY)
													.isPower()
											&& getButtonTree().getButton(
													CTButtonConst.BTN_CTOTHER)
													.isPower());
					getCtBillListPanel()
							.getBodyTabbedPane()
							.setEnabledAt(
									i,
									bModify
											&& getButtonTree()
													.getButton(
															CTButtonConst.BTN_CTOTHER_CHANGEHISTORY)
													.isPower()
											&& getButtonTree().getButton(
													CTButtonConst.BTN_CTOTHER)
													.isPower());
				} else if ("exec".equals(tablecode)) {
					getButtonTree().getButton(
							CTButtonConst.BTN_CTOTHER_CHANGEHISTORY)
							.setVisible(true);
					getCtBillCardPanel()
							.getBodyTabbedPane()
							.setEnabledAt(
									i,
									bExec
											&& getButtonTree()
													.getButton(
															CTButtonConst.BTN_CTOTHER_CHANGEHISTORY)
													.isPower()
											&& getButtonTree().getButton(
													CTButtonConst.BTN_CTOTHER)
													.isPower());
					getCtBillListPanel()
							.getBodyTabbedPane()
							.setEnabledAt(
									i,
									bExec
											&& getButtonTree()
													.getButton(
															CTButtonConst.BTN_CTOTHER_CHANGEHISTORY)
													.isPower()
											&& getButtonTree().getButton(
													CTButtonConst.BTN_CTOTHER)
													.isPower());
				}
			}
		}

	}

	/**
	 * 此处插入方法说明。 创建日期：(2004-4-7 15:12:07)
	 * 
	 * @author：程起伍
	 * @return nc.vo.ct.pub.ManageVO
	 * @param voExtend
	 *            nc.vo.ct.pub.ExtendManageVO
	 */
	private ManageVO transVO(ExtendManageVO voExtend) {
		ManageVO voreturn = null;
		if (voExtend != null) {
			voreturn = new ManageVO();
			ManageHeaderVO voHead = (ManageHeaderVO) voExtend.getParentVO();
			voreturn.setParentVO(voHead);
			voreturn.setChildrenVO(voExtend.getTableVO("table"));
			voreturn.setBillStatus(voHead.getStatus());
			voreturn.setBillType(m_sBillType);
			voreturn.setVBillCode(voHead.getCt_code());

		}
		return voreturn;
	}

	/*
	 * 增加一个内部类. 继承IFreshTsListener. 实现打印后对ts及printcount的更新. @author 邵兵 创建时间:
	 * 2004-12-23
	 */
	public class FreshTsListener implements IFreshTsListener {

		/*
		 * （非 Javadoc）
		 * 
		 * @see nc.ui.scm.print.IFreshTsListener#freshTs(java.lang.String,
		 *      java.lang.String)
		 */
		public void freshTs(String sBillID, String sTS, Integer iPrintCount) {
			// fresh local TS with voPr.getNewTs();

			if (m_vBillVO == null || m_vBillVO.size() == 0)
				return;

			// 判断打印的vo是否仍在缓存中．
			// 在打印预览状态打印时，缓存vo可能会有改变，故需要判断．
			int index = 0;
			ExtendManageVO extendManageVO = null;
			ManageHeaderVO headerVO = null;
			for (; index < m_vBillVO.size(); index++) {
				extendManageVO = (ExtendManageVO) m_vBillVO.elementAt(index);
				headerVO = (ManageHeaderVO) extendManageVO.getParentVO();

				// 在sBillID传入时，已经判断sBillID不为null.
				if (sBillID.equals(headerVO.getPrimaryKey()))
					break;
			}

			if (index == m_vBillVO.size()) // 不在缓存vo中，无需进行更新．
				return;

			// 在缓存vo中
			headerVO.setAttributeValue("ts", sTS);
			headerVO.setAttributeValue("iprintcount", iPrintCount);

			if (m_bIsCard == true) { // Card
				if (index == m_iId - 1) { // 如果为当前card显示vo.
					getCtBillCardPanel().setHeadItem("ts", sTS);
					getCtBillCardPanel()
							.setTailItem("iprintcount", iPrintCount);
				}
			} else { // List
				int iPrintColumn = getCtBillListPanel().getHeadBillModel()
						.getBodyColByKey("ts");
				getCtBillListPanel().getHeadBillModel().setValueAt(sTS, index,
						iPrintColumn);
				iPrintColumn = getCtBillListPanel().getHeadBillModel()
						.getBodyColByKey("iprintcount");
				getCtBillListPanel().getHeadBillModel().setValueAt(iPrintCount,
						index, iPrintColumn);
			}
		}
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see nc.ui.pub.bill.BillModelCellEditableController#isCellEditable(boolean,
	 *      int, java.lang.String) 编辑前控制 替换beforeEdit().
	 */
	public boolean isCellEditable(boolean value, int row, String itemkey) {
		if (m_iBillState == OperationState.EDIT
				|| m_iBillState == OperationState.ADD
				|| m_iBillState == OperationState.CHANGE) {
			getCtBillCardPanel().superStopEditing();

			Object oRefValue = ((UIRefPane) getCtBillCardPanel().getHeadItem(
					"ct_type").getComponent()).getRefValue("ninvctlstyle");

			if (itemkey.equals("transrate")) { // 主辅单位换算率
				if (getCtBillCardPanel().getBodyValueAt(row, "invid") == null)
					return false;
				String sInvManID = getCtBillCardPanel().getBodyValueAt(row,
						"invid").toString();

				// 取辅单位
				BillItem bt = getCtBillCardPanel().getBodyItem("astmeasname");
				UIRefPane refAstUnit = (UIRefPane) bt.getComponent();

				// 辅单位pk
				// Object oAstUnitPk = refAstUnit.getRefPK();
				// modified by lirr 2009-01-07
				Object oAstUnitPk = getCtBillCardPanel().getBodyValueAt(row,
						"astmeasid");
				if (isNull(oAstUnitPk)) {
					return false;
				}
				// modified by lirr 2009-9-3下午04:59:34 减少连接数
				/*
				 * m_voInvMeas.filterMeas(getCorpPrimaryKey(), sInvManID,
				 * refAstUnit); MeasureRateVO voMeas =
				 * m_voInvMeas.getMeasureRate(sInvManID, oAstUnitPk.toString()); //
				 * UFBoolean ufbFixedflag = voMeas.getFixedflag(); // added by
				 * lirr 2008-12-25 加空判断 UFBoolean ufbFixedflag = new
				 * UFBoolean(false); if (null != voMeas && null !=
				 * voMeas.getFixedflag()) ufbFixedflag = voMeas.getFixedflag(); //
				 * 固定换算率 if (ufbFixedflag.booleanValue()) { return false; } //
				 * 变动换算率 else if (!ufbFixedflag.booleanValue()) { return true; }
				 * return false;
				 */
				boolean ufbFixedflag = InvTool.isFixedConvertRate(
						getCtBillCardPanel().getBodyValueAt(row, "invbasid")
								.toString(), oAstUnitPk.toString());
				if (ufbFixedflag) {
				    return false; 
				    } //
        else if (!ufbFixedflag) {
            return true; 
            }
			}
			// 此处存在问题：应该限制辅计量名称不可编辑。
			// 出于v31发版时间考虑，暂不作修改(避免引发连环问题)。发版后切记修改。
			// 邵兵 on Jul 16, 2005
			else if (itemkey.equals("astmeasname")
					|| itemkey.equals("astmeascode")) {
				if (getCtBillCardPanel().getBodyValueAt(row, "invbasid") == null)
					return false;

				String sInvBasID = getCtBillCardPanel().getBodyValueAt(row,
						"invbasid").toString();
				UFBoolean isAstUnitMng = isAstUnitMng(new String[] { sInvBasID })[0];

				if (isAstUnitMng != null && isAstUnitMng.booleanValue() == true)
					return true;
				else {
					SCMEnv
							.out("The inventory is not in the auxiliary measure management");
					return false;
				}

			} else if (itemkey.equals("astnum")) {
				if (getCtBillCardPanel().getBodyValueAt(row, "astmeasid") == null)
					return false;

				return true;
			}

			else if (itemkey.equals("invcode") && null != oRefValue) {

				// if (oRefValue == null) {
				// MessageDialog.showHintDlg(this, null, nc.ui.ml.NCLangRes
				// .getInstance().getStrByID("4020pub",
				// "UPP4020pub-000026")/* @res "请先选择合同类型" */);
				// return false;
				// }

				// 根据合同类型的存货控制方式设置存货编码和存货分类编码是否可编辑
				int iInvctlstyle = Integer.parseInt(oRefValue.toString());

				// 如果合同类型的存货控制方式为存货
				if (iInvctlstyle == 0) {
					getCtBillCardPanel().getBodyItem("invclasscode")
							.setEnabled(false);
					getCtBillCardPanel().getBodyItem("invcode")
							.setEnabled(true);

					// 设置存货分类编码，存货分类名称为空
					getCtBillCardPanel().setBodyValueAt(null, row,
							"invclasscode");
					getCtBillCardPanel().setBodyValueAt(null, row,
							"invclassname");
					getCtBillCardPanel().setBodyValueAt(null, row, "invclid");

					// 将上次选中的存货清除
					// UIRefPane refInv = (UIRefPane) getCtBillCardPanel()
					// .getBodyItem("invcode").getComponent();
					// refInv.setPK(null);
					// refInv.getRefModel().setSelectedData(null);
				}

				// 如果合同类型的存货控制方式为存货分类
				else if (iInvctlstyle == 1) {
					getCtBillCardPanel().getBodyItem("invclasscode")
							.setEnabled(true);
					getCtBillCardPanel().getBodyItem("invcode").setEnabled(
							false);
					// 设置存货编码，存货名称,型号，规格，数量,计量单位为空
					getCtBillCardPanel().setBodyValueAt(null, row, "invcode");
					getCtBillCardPanel().setBodyValueAt(null, row, "invname");
					getCtBillCardPanel().setBodyValueAt(null, row, "invid");
					getCtBillCardPanel().setBodyValueAt(null, row, "spec");
					getCtBillCardPanel().setBodyValueAt(null, row, "mod");

					getCtBillCardPanel().setBodyValueAt(null, row, "measname");

				}
				// 如果合同类型的存货控制方式为空。将存货分类和存货都设置为空，不可编辑。
				else if (iInvctlstyle == 2) {
					getCtBillCardPanel().getBodyItem("invclasscode")
							.setEnabled(false);
					getCtBillCardPanel().getBodyItem("invcode").setEnabled(
							false);
					// 设置存货分类编码，存货分类名称为空
					getCtBillCardPanel().setBodyValueAt(null, row,
							"invclasscode");
					getCtBillCardPanel().setBodyValueAt(null, row,
							"invclassname");
					getCtBillCardPanel().setBodyValueAt(null, row, "invclid");
					// 设置存货编码，存货名称,型号，规格，数量,计量单位为空
					getCtBillCardPanel().setBodyValueAt(null, row, "invcode");
					getCtBillCardPanel().setBodyValueAt(null, row, "invname");
					getCtBillCardPanel().setBodyValueAt(null, row, "invid");
					getCtBillCardPanel().setBodyValueAt(null, row, "spec");
					getCtBillCardPanel().setBodyValueAt(null, row, "mod");

					getCtBillCardPanel().setBodyValueAt(null, row, "measname");
				}
			}

			else if (itemkey.equals("invclasscode") && null != oRefValue) {
				// if (oRefValue == null) {
				// MessageDialog.showHintDlg(this, null, nc.ui.ml.NCLangRes
				// .getInstance().getStrByID("4020pub",
				// "UPP4020pub-000026")/* @res "请先选择合同类型" */);
				// return false;
				// }

				// 根据合同类型的存货控制方式设置存货编码和存货分类编码是否可编辑
				int iInvctlstyle = Integer.parseInt(oRefValue.toString());

				// 合同类型的存货控制方式为合同存货
				if (iInvctlstyle == 0) {
					getCtBillCardPanel().getBodyItem("invclasscode")
							.setEnabled(false);
					getCtBillCardPanel().getBodyItem("invcode")
							.setEnabled(true);
					// 设置存货分类编码，存货分类名称为空
					getCtBillCardPanel().setBodyValueAt(null, row,
							"invclasscode");
					getCtBillCardPanel().setBodyValueAt(null, row,
							"invclassname");
					getCtBillCardPanel().setBodyValueAt(null, row, "invclid");

				}
				// 合同类型的存货控制方式为存货分类
				else if (iInvctlstyle == 1) {
					getCtBillCardPanel().getBodyItem("invclasscode")
							.setEnabled(true);
					getCtBillCardPanel().getBodyItem("invcode").setEnabled(
							false);
					// 设置存货编码，存货名称,型号，规格，数量,计量单位为空
					getCtBillCardPanel().setBodyValueAt(null, row, "invcode");
					getCtBillCardPanel().setBodyValueAt(null, row, "invname");
					getCtBillCardPanel().setBodyValueAt(null, row, "invid");
					getCtBillCardPanel().setBodyValueAt(null, row, "spec");
					getCtBillCardPanel().setBodyValueAt(null, row, "mod");
					// getCtBillCardPanel().setBodyValueAt(null, row,
					// "amount");
					getCtBillCardPanel().setBodyValueAt(null, row, "measname");

				}
				// 如果合同类型的存货控制方式为空。将存货分类和存货都设置为空，不可编辑。
				else if (iInvctlstyle == 2) {
					getCtBillCardPanel().getBodyItem("invclasscode")
							.setEnabled(false);
					getCtBillCardPanel().getBodyItem("invcode").setEnabled(
							false);
					// 设置存货分类编码，存货分类名称为空
					getCtBillCardPanel().setBodyValueAt(null, row,
							"invclasscode");
					getCtBillCardPanel().setBodyValueAt(null, row,
							"invclassname");
					getCtBillCardPanel().setBodyValueAt(null, row, "invclid");
					// 设置存货编码，存货名称,型号，规格，数量,计量单位为空
					getCtBillCardPanel().setBodyValueAt(null, row, "invcode");
					getCtBillCardPanel().setBodyValueAt(null, row, "invname");
					getCtBillCardPanel().setBodyValueAt(null, row, "invid");
					getCtBillCardPanel().setBodyValueAt(null, row, "spec");
					getCtBillCardPanel().setBodyValueAt(null, row, "mod");

					getCtBillCardPanel().setBodyValueAt(null, row, "measname");
				}

			}

			return getCtBillCardPanel().getBodyItem(CTTableCode.BASE, itemkey)
					.isEnabled();
		}

		return false;

	}

	// /*
	// * 由ConditionVO得到查询条件。 @author: 邵兵 on Jun 2, 2005
	// */
	// protected String getConditonSQL(nc.vo.pub.query.ConditionVO[] conVO) {
	//
	// ArrayList<ConditionVO> alist = new ArrayList<ConditionVO>(conVO.length);
	// String invclsql = null;
	// String dbilldatefrom = null;
	// String dbilldateend = null;
	// if (conVO != null) {
	// for (int i = 0; i < conVO.length; i++) {
	// if (conVO[i].getFieldCode().equals("ct_manage.pk_corp"))
	// continue;
	// if (conVO[i].getFieldCode().indexOf("invclid") > 0
	// && conVO[i].getOperaCode() != null
	// && !conVO[i].getOperaCode().equals("is")
	// && !conVO[i].getOperaCode().equals("in")
	// && conVO[i].getValue() != null
	// && conVO[i].getValue().trim().length() == 20) {
	// invclsql = conVO[i].getSQLStr();
	// invclsql = invclsql.replaceFirst("and", "and (");
	// invclsql += " or invid in (SELECT pk_invmandoc FROM bd_invmandoc ";
	// invclsql += "inner JOIN bd_invbasdoc ON bd_invmandoc.pk_invbasdoc =
	// bd_invbasdoc.pk_invbasdoc ";
	// invclsql += "inner JOIN bd_invcl ON bd_invbasdoc.pk_invcl =
	// bd_invcl.pk_invcl
	// WHERE ";
	// invclsql += "bd_invcl.pk_invcl='"
	// + conVO[i].getValue().trim() + "' ) )";
	// continue;
	// }
	// // modified by liuzy 2008-03-26 碧桂园增加默认查询条件
	// if (conVO[i].getFieldCode().equals("ct_manage.operdate.from")
	// || conVO[i].getFieldCode().equals(
	// "ct_manage.operdate.end")) {
	// if (conVO[i].getFieldCode().equals(
	// "ct_manage.operdate.from"))
	// dbilldatefrom = conVO[i].getValue();
	//
	// if (conVO[i].getFieldCode()
	// .equals("ct_manage.operdate.end"))
	// dbilldateend = conVO[i].getValue();
	//
	// conVO[i].setFieldCode("ct_manage.operdate");
	// }
	// alist.add(conVO[i]);
	// }
	//
	// if (null == dbilldatefrom || "".equals(dbilldatefrom.trim())
	// || null == dbilldateend || "".equals(dbilldateend.trim())) {
	// showErrorMessage("【制单日期从】与【制单日期到】同为必输查询条件");
	// return null;
	// }
	// }
	//
	// String sWhereClause = null;
	// if (alist != null && alist.size() > 0) {
	// sWhereClause = getQueryConditionDlg().getWhereSQL(
	// alist.toArray(new ConditionVO[alist.size()]));
	// if (invclsql != null)
	// sWhereClause += invclsql;
	// } else {
	// sWhereClause = getQueryConditionDlg().getWhereSQL(conVO);
	// }
	// StringBuffer sbWhere = new StringBuffer();
	//
	// // 增加合同类型及是否期初的条件
	// if (sWhereClause == null || sWhereClause.trim().length() == 0) {
	// sbWhere.append("ct_type.nbusitype=");
	// sbWhere.append(m_iCtType);
	// if (m_UFbIfEarly.booleanValue())
	// sbWhere.append(" and ct.ifearly='Y'");
	// else
	// sbWhere.append(" and ct.ifearly='N'");
	// } else {
	// sbWhere.append(sWhereClause);
	// sbWhere.append(" and ct_type.nbusitype=");
	// sbWhere.append(m_iCtType);
	// if (m_UFbIfEarly.booleanValue())
	// sbWhere.append(" and ct.ifearly='Y'");
	// else
	// sbWhere.append(" and ct.ifearly='N'");
	// }
	//
	// // 加公司条件
	// sbWhere.append(" and ct.pk_corp = '" + m_sPkCorp + "'");
	// // modified by liuzy 2008-04-11 增加表体、合同类型公司查询条件
	// // sbWhere.append(" and ct_b.pk_corp = '" + m_sPkCorp + "'");
	// sbWhere.append(" and ct_type.pk_corp = '" + m_sPkCorp + "'");
	//
	// // 纪录查询条件，以便刷新使用
	// m_sQryCondition = sbWhere.toString();
	//
	// return sbWhere.toString();
	// }

	/*
	 * 存货是否辅计量管理 邵兵 Jun 22, 2005
	 */
	protected UFBoolean[] isAstUnitMng(String[] saInvBasID) {

		UFBoolean[] bAstUnit = new UFBoolean[saInvBasID.length];

		// 是否辅计量管理
		Object[] oTemp = null;
		try {
			oTemp = (Object[]) CacheTool.getColumnValue("bd_invbasdoc",
					"pk_invbasdoc", "assistunit", saInvBasID);
		} catch (BusinessException e) {
			nc.vo.scm.pub.SCMEnv.out(e);
		}

		for (int i = 0; i < saInvBasID.length; i++) {
			if (oTemp != null && oTemp[i] != null)
				bAstUnit[i] = new UFBoolean(oTemp[i].toString());
			else
				bAstUnit[i] = new UFBoolean(false);
		}

		return bAstUnit;
	}

	/**
	 * 关闭窗口的客户端接口。可在本方法内完成窗口关闭前的工作。
	 * 
	 * @return boolean 返回值为true表示允许窗口关闭，返回值为false表示不允许窗口关闭。 创建日期：(2005-10-24
	 *         13:52:37)
	 */
	public boolean onClosing() {
		// 新增、编辑或变更
		if (m_iBillState == OperationState.ADD
				|| m_iBillState == OperationState.EDIT
				|| m_iBillState == OperationState.CHANGE) {
			int ret = MessageDialog.showYesNoCancelDlg(this, null,
					nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
							"UCH001")/* @res "是否保存已修改的数据？" */);
			if (ret == MessageDialog.ID_CANCEL) {
				return false;
			} else if (ret == MessageDialog.ID_YES) {
				if (!onSave())
					return false;
			}
		}
		return true;
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see nc.ui.scm.pub.bill.IBillExtendFun#getExtendBtns() 获取扩展按钮数组 注意:
	 *      扩展按钮默认加在合同基本页签之上.
	 */
	public ButtonObject[] getExtendBtns() {
		return null;
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see nc.ui.scm.pub.bill.IBillExtendFun#onExtendBtnsClick(nc.ui.pub.ButtonObject)
	 *      控制扩展按钮的事件
	 */
	public void onExtendBtnsClick(ButtonObject bo) {
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see nc.ui.scm.pub.bill.IBillExtendFun#setExtendBtnsStat(int)
	 *      OperationState.FREE 浏览 OperationState.ADD 新增 OperationState.EDIT 编辑
	 *      OperationState.CHANGE 变更 注意：扩展按钮默认加在合同基本页签之上.
	 *      本方法在setButtonState()中最后调用,其参数为合同的状态. ShawBing 2005-11-02
	 */
	public void setExtendBtnsStat(int iState) {

	}

	public List getRelaSortObject() {
		return m_vBillVO;
	}

	public int getSortTypeByBillItemKey(String key) {
		// 行号
		if ("crowno".equals(key))
			return BillItem.DECIMAL;

		if (m_bIsCard)
			return getCtBillCardPanel().getBillModel(CTTableCode.BASE)
					.getItemByKey(key).getDataType();
		else
			return getCtBillListPanel().getBodyItem(CTTableCode.BASE, key)
					.getDataType();
	}

	/**
	 * 审批流时数据加载方法。
	 */
	public void doApproveAction(ILinkApproveData approvedata) {
		qryCt(approvedata.getBillID());

		// 设置按钮状态
		m_iBillState = OperationState.FREE;
		// setButtonState();
		if (!m_bAddFromBillFlag) {
			setButtonState();
		} else {
			setButtonStateForCof();
		}
	}

	/**
	 * 联查时数据加载之用
	 */
	public void doQueryAction(ILinkQueryData querydata) {

		String sPkCorp;
		sPkCorp = "";
		StringBuffer sWhere = new StringBuffer(" ct_manage.pk_ct_manage='"
				+ querydata.getBillID().trim() + "' ");
		sWhere.append(" AND ct_type.nbusitype=");
		sWhere.append(m_iCtType);
		if (m_UFbIfEarly.booleanValue())
			sWhere.append(" and ct_manage.ifearly='Y'");
		else
			sWhere.append(" and ct_manage.ifearly='N'");

		ManageVO[] arrMangevos;
		try {
			// arrMangevos = ContractQueryHelper.queryBill(m_sPkCorp, sWhere
			// .toString());
			// modified by liuzy 2008-04-28 碧桂园查询改造，联查
			arrMangevos = ContractQueryHelper.queryAllHeadData(m_sPkCorp,
					sWhere.toString());
			if (arrMangevos == null || arrMangevos.length == 0) {
				this.showHintMessage(nc.ui.ml.NCLangRes.getInstance()
						.getStrByID("4020pub", "UPP4020pub-000070")/*
																	 * @res
																	 * "没有找到符合条件的数据"
																	 */);
				return;
			}
			sPkCorp = arrMangevos[0].getPk_corp();

		} catch (Exception e) {
			// TODO 自动生成 catch 块
			GenMethod.handleException(this, e.getMessage(), e);
		}

		/*
		 * if ((!sPkCorp.equals(m_sPkCorp))&&(!sPkCorp.equals(""))){ BillData
		 * bdMy = new BillData(m_BillCardPanel.getTempletData( m_sBillType,
		 * null, m_sPkUser, sPkCorp)); if (bdMy!=null) {
		 * getCtBillCardPanel().setBillData(bdMy); } }
		 */

		qryCt(querydata.getBillID());

		// 设置按钮状态
		// 修改人:刘家清 修改日期:200703201441 修改原因:当联查单据如果是跨公司打开时,按钮全部都隐藏
		if ((sPkCorp.equals(m_sPkCorp)) || (sPkCorp.equals(""))) {
			m_iBillState = OperationState.FREE;
			// setButtonState();
			if (!m_bAddFromBillFlag) {
				setButtonState();
			} else {
				setButtonStateForCof();
			}
		} else {
			ButtonObject[] btnGroupMy = getButtonTree().getButtonArray();
			for (int i = 0; i < btnGroupMy.length; i++)
				btnGroupMy[i].setVisible(false);
			updateButtons();
		}

	}

	/**
	 * 关联查询 （驳回至制单人时使用）
	 */
	public void doMaintainAction(ILinkMaintainData maintaindata) {
		qryCt(maintaindata.getBillID());

		// 设置按钮状态
		m_iBillState = OperationState.FREE;
		// setButtonState();
		if (!m_bAddFromBillFlag) {
			setButtonState();
		} else {
			setButtonStateForCof();
		}
	}

	/**
	 * 转换成单据状态页签的按钮显示
	 */
	protected void changeToBlButn() {
		getButtonTree().getButton(CTButtonConst.BTN_TAB_CANCEL).setVisible(
				false);
		getButtonTree().getButton(CTButtonConst.BTN_TAB_ADD).setVisible(false);
		getButtonTree().getButton(CTButtonConst.BTN_TAB_DEL).setVisible(false);
		getButtonTree().getButton(CTButtonConst.BTN_TAB_EDIT).setVisible(false);
		getButtonTree().getButton(CTButtonConst.BTN_TAB_SAVE).setVisible(false);
		getButtonTree().getButton(CTButtonConst.BTN_TAB_PRINT)
				.setVisible(false);
		getButtonTree().getButton(CTButtonConst.BTN_ADD).setVisible(true);
		getButtonTree().getButton(CTButtonConst.BTN_BILL_EDIT).setVisible(true);
		getButtonTree().getButton(CTButtonConst.BTN_SWITCH).setVisible(true);
		getButtonTree().getButton(CTButtonConst.BTN_BILL_DELETE).setVisible(
				true);
		getButtonTree().getButton(CTButtonConst.BTN_BILL_COPY).setVisible(true);
		getButtonTree().getButton(CTButtonConst.BTN_BILL).setVisible(true);

		getButtonTree().getButton(CTButtonConst.BTN_SAVE).setVisible(true);
		getButtonTree().getButton(CTButtonConst.BTN_BILL_CANCEL).setVisible(
				true);
		getButtonTree().getButton(CTButtonConst.BTN_LINE).setVisible(true);
		getButtonTree().getButton(CTButtonConst.BTN_LINE_ADD).setVisible(true);
		 getButtonTree().getButton(CTButtonConst.BTN_LINE_DELETE).setVisible(
		 true);
		getButtonTree().getButton(CTButtonConst.BTN_LINE_INSERT).setVisible(
				true);
		getButtonTree().getButton(CTButtonConst.BTN_LINE_COPY).setVisible(true);
		getButtonTree().getButton(CTButtonConst.BTN_LINE_PASTE)
				.setVisible(true);
		// added by lirr 2008-07-10
		getButtonTree().getButton(CTButtonConst.BTN_BATCH_EDIT)
				.setVisible(true);

		getButtonTree().getButton(CTButtonConst.BTN_EXECUTE).setVisible(true);
		getButtonTree().getButton(CTButtonConst.BTN_AUDIT).setVisible(true);
		getButtonTree().getButton(CTButtonConst.BTN_EXECUTE_CTFREEZE)
				.setVisible(true);
		getButtonTree().getButton(CTButtonConst.BTN_EXECUTE_CTFREEZE_CANCEL)
				.setVisible(true);
		getButtonTree().getButton(CTButtonConst.BTN_EXECUTE_TERMINATE)
				.setVisible(true);
		getButtonTree().getButton(CTButtonConst.BTN_EXECUTE_CTCHANGE)
				.setVisible(true);
		getButtonTree().getButton(CTButtonConst.BTN_EXECUTE_TERMINATE)
				.setVisible(true);
		getButtonTree().getButton(CTButtonConst.BTN_EXECUTE_VALIDATE)
				.setVisible(true);
		getButtonTree().getButton(CTButtonConst.BTN_PRINT_PRINT).setVisible(
				true);
		getButtonTree().getButton(CTButtonConst.BTN_PRINT).setVisible(true);
		getButtonTree().getButton(CTButtonConst.BTN_PRINT_PREVIEW).setVisible(
				true);
		getButtonTree().getButton(CTButtonConst.BTN_ASSIST_FUNC).setVisible(
				true);
		getButtonTree().getButton(CTButtonConst.BTN_BROWSE).setVisible(true);
		getButtonTree().getButton(CTButtonConst.BTN_EXECUTE_CANCELAPPRPVE)
				.setVisible(true);
		getButtonTree().getButton(CTButtonConst.BTN_CTOTHER_CHANGEHISTORY)
				.setVisible(true);
		getButtonTree().getButton(CTButtonConst.BTN_CTOTHER_CHANGEHISTORY)
				.setVisible(true);
		getButtonTree().getButton(CTButtonConst.BTN_CTOTHER_NOTE).setVisible(
				true);
		getButtonTree().getButton(CTButtonConst.BTN_BROWSE_REFRESH).setVisible(
				true);
		getButtonTree().getButton(CTButtonConst.BTN_ASSIST_QUERY_RELATED)
				.setVisible(true);
		getButtonTree().getButton(CTButtonConst.BTN_CTOTHER_EXP).setVisible(
				true);
		getButtonTree().getButton(CTButtonConst.BTN_EXECUTE_SENDAPPRPVE)
				.setVisible(true);
		getButtonTree().getButton(CTButtonConst.BTN_CTOTHER_TERM).setVisible(
				true);
		getButtonTree().getButton(CTButtonConst.BTN_ASSIST_QUERY_FLOWSTATUE)
				.setVisible(true);
		getButtonTree().getButton(CTButtonConst.BTN_BROWSE_NEXT).setVisible(
				true);// 下一页
		getButtonTree().getButton(CTButtonConst.BTN_BROWSE_PREVIOUS)
				.setVisible(true);// 上一页
		getButtonTree().getButton(CTButtonConst.BTN_ASSIST_FUNC_DOCUMENT)
				.setVisible(true);// 文档管理
		getButtonTree().getButton(CTButtonConst.BTN_QUERY).setVisible(true);// 查询
		getButtonTree().getButton(CTButtonConst.BTN_ASSIST_QUERY).setVisible(
				true);// 辅助查询

		// modified by liuzy 2008-05-07 漏掉了执行过程按钮
		getButtonTree().getButton(CTButtonConst.BTN_CTOTHER_EXEC).setVisible(
				true);
	}

	/**
	 * 转换成非单据状态页签的按钮显示
	 */
	protected void changeToTbButn() {
		getButtonTree().getButton(CTButtonConst.BTN_ADD).setVisible(false);// getButtonTree().getButton(CTButtonConst.BTN_ADD).setVisible(false);
		getButtonTree().getButton(CTButtonConst.BTN_BILL_EDIT)
				.setVisible(false);// getButtonTree().getButton(CTButtonConst.BTN_BILL_EDIT).setVisible(false);
		getButtonTree().getButton(CTButtonConst.BTN_SWITCH).setVisible(false);// getButtonTree().getButton(CTButtonConst.BTN_SWITCH).setVisible(false);
		getButtonTree().getButton(CTButtonConst.BTN_BILL_DELETE).setVisible(
				false);// getButtonTree().getButton(CTButtonConst.BTN_BILL_DELETE).setVisible(false);
		getButtonTree().getButton(CTButtonConst.BTN_BILL_COPY)
				.setVisible(false);// getButtonTree().getButton(CTButtonConst.BTN_BILL_COPY).setVisible(false);
		getButtonTree().getButton(CTButtonConst.BTN_BILL).setVisible(false);// getButtonTree().getButton(CTButtonConst.BTN_BILL).setVisible(false);
		getButtonTree().getButton(CTButtonConst.BTN_BROWSE_NEXT).setVisible(
				false);// 下一页
		getButtonTree().getButton(CTButtonConst.BTN_BROWSE_PREVIOUS)
				.setVisible(false);// 上一页
		getButtonTree().getButton(CTButtonConst.BTN_ASSIST_FUNC_DOCUMENT)
				.setVisible(false);// 文档管理
		getButtonTree().getButton(CTButtonConst.BTN_QUERY).setVisible(false);// 查询
		getButtonTree().getButton(CTButtonConst.BTN_SAVE).setVisible(false);// getButtonTree().getButton(CTButtonConst.BTN_SAVE).setVisible(false);
		getButtonTree().getButton(CTButtonConst.BTN_BILL_CANCEL).setVisible(
				false);// getButtonTree().getButton(CTButtonConst.BTN_BILL_CANCEL).setVisible(false);
		getButtonTree().getButton(CTButtonConst.BTN_LINE).setVisible(false);// getButtonTree().getButton(CTButtonConst.BTN_LINE).setVisible(false);
		getButtonTree().getButton(CTButtonConst.BTN_LINE_ADD).setVisible(false);// getButtonTree().getButton(CTButtonConst.BTN_LINE_ADD).setVisible(false);
		getButtonTree().getButton(CTButtonConst.BTN_LINE_DELETE).setVisible(
				false);// getButtonTree().getButton(CTButtonConst.BTN_LINE_DELETE).setVisible(false);
		getButtonTree().getButton(CTButtonConst.BTN_LINE_INSERT).setVisible(
				false);// getButtonTree().getButton(CTButtonConst.BTN_LINE_INSERT).setVisible(false);
		getButtonTree().getButton(CTButtonConst.BTN_LINE_COPY)
				.setVisible(false);// getButtonTree().getButton(CTButtonConst.BTN_LINE_COPY).setVisible(false);
		getButtonTree().getButton(CTButtonConst.BTN_LINE_PASTE).setVisible(
				false);// getButtonTree().getButton(CTButtonConst.BTN_LINE_PASTE).setVisible(false);
		// added by lirr 2008-07-10

		getButtonTree().getButton(CTButtonConst.BTN_EXECUTE).setVisible(false);// 执行
		getButtonTree().getButton(CTButtonConst.BTN_AUDIT).setVisible(false);// getButtonTree().getButton(CTButtonConst.BTN_AUDIT).setVisible(false);
		getButtonTree().getButton(CTButtonConst.BTN_EXECUTE_CTFREEZE)
				.setVisible(false);// getButtonTree().getButton(CTButtonConst.BTN_EXECUTE_CTFREEZE).setVisible(false);
		getButtonTree().getButton(CTButtonConst.BTN_EXECUTE_CTFREEZE_CANCEL)
				.setVisible(false);// getButtonTree().getButton(CTButtonConst.BTN_EXECUTE_CTFREEZE_CANCEL).setVisible(false);
		getButtonTree().getButton(CTButtonConst.BTN_EXECUTE_TERMINATE)
				.setVisible(false);// getButtonTree().getButton(CTButtonConst.BTN_EXECUTE_TERMINATE).setVisible(false);
		getButtonTree().getButton(CTButtonConst.BTN_EXECUTE_CTCHANGE)
				.setVisible(false);// getButtonTree().getButton(CTButtonConst.BTN_EXECUTE_CTCHANGE).setVisible(false);
		getButtonTree().getButton(CTButtonConst.BTN_TAB_CANCEL)
				.setVisible(true);// getButtonTree().getButton(CTButtonConst.BTN_TAB_CANCEL).setVisible(true);
		getButtonTree().getButton(CTButtonConst.BTN_TAB_ADD).setVisible(true);// getButtonTree().getButton(CTButtonConst.BTN_TAB_ADD).setVisible(true);
		getButtonTree().getButton(CTButtonConst.BTN_TAB_DEL).setVisible(true);// getButtonTree().getButton(CTButtonConst.BTN_TAB_DEL).setVisible(true);
		getButtonTree().getButton(CTButtonConst.BTN_TAB_EDIT).setVisible(true);// getButtonTree().getButton(CTButtonConst.BTN_TAB_EDIT).setVisible(true);
		getButtonTree().getButton(CTButtonConst.BTN_TAB_SAVE).setVisible(true);// getButtonTree().getButton(CTButtonConst.BTN_TAB_SAVE).setVisible(true);
		getButtonTree().getButton(CTButtonConst.BTN_EXECUTE_TERMINATE)
				.setVisible(false);// getButtonTree().getButton(CTButtonConst.BTN_EXECUTE_TERMINATE).setVisible(false);
		getButtonTree().getButton(CTButtonConst.BTN_EXECUTE_VALIDATE)
				.setVisible(false);// getButtonTree().getButton(CTButtonConst.BTN_EXECUTE_VALIDATE).setVisible(false);
		getButtonTree().getButton(CTButtonConst.BTN_PRINT_PRINT).setVisible(
				false);// getButtonTree().getButton(CTButtonConst.BTN_PRINT_PRINT).setVisible(false);
		getButtonTree().getButton(CTButtonConst.BTN_PRINT).setVisible(false);
		getButtonTree().getButton(CTButtonConst.BTN_PRINT_PREVIEW).setVisible(
				false);// getButtonTree().getButton(CTButtonConst.BTN_PRINT_PREVIEW).setVisible(false);
		getButtonTree().getButton(CTButtonConst.BTN_ASSIST_FUNC).setVisible(
				false);// getButtonTree().getButton(CTButtonConst.BTN_ASSIST_FUNC).setVisible(false);
		getButtonTree().getButton(CTButtonConst.BTN_BROWSE).setVisible(false);// getButtonTree().getButton(CTButtonConst.BTN_BROWSE).setVisible(false);
		getButtonTree().getButton(CTButtonConst.BTN_EXECUTE_CANCELAPPRPVE)
				.setVisible(false);// getButtonTree().getButton(CTButtonConst.BTN_EXECUTE_CANCELAPPRPVE).setVisible(false);
		getButtonTree().getButton(CTButtonConst.BTN_CTOTHER_CHANGEHISTORY)
				.setVisible(false);// getButtonTree().getButton(CTButtonConst.BTN_CTOTHER_CHANGEHISTORY).setVisible(false);
		getButtonTree().getButton(CTButtonConst.BTN_CTOTHER_EXEC).setVisible(
				false);// getButtonTree().getButton(CTButtonConst.BTN_CTOTHER_CHANGEHISTORY).setVisible(false);
		getButtonTree().getButton(CTButtonConst.BTN_CTOTHER_NOTE).setVisible(
				false);// getButtonTree().getButton(CTButtonConst.BTN_CTOTHER_NOTE).setVisible(false);
		getButtonTree().getButton(CTButtonConst.BTN_BROWSE_REFRESH).setVisible(
				false);// getButtonTree().getButton(CTButtonConst.BTN_BROWSE_REFRESH).setVisible(false);
		getButtonTree().getButton(CTButtonConst.BTN_ASSIST_QUERY_RELATED)
				.setVisible(false);// getButtonTree().getButton(CTButtonConst.BTN_ASSIST_QUERY_RELATED).setVisible(false);
		getButtonTree().getButton(CTButtonConst.BTN_CTOTHER_EXP).setVisible(
				false);// getButtonTree().getButton(CTButtonConst.BTN_CTOTHER_EXP).setVisible(false);合同费用
		getButtonTree().getButton(CTButtonConst.BTN_EXECUTE_SENDAPPRPVE)
				.setVisible(false);// getButtonTree().getButton(CTButtonConst.BTN_EXECUTE_SENDAPPRPVE).setVisible(false);
		getButtonTree().getButton(CTButtonConst.BTN_TAB_PRINT).setVisible(true);// getButtonTree().getButton(CTButtonConst.BTN_TAB_PRINT).setVisible(true);
		getButtonTree().getButton(CTButtonConst.BTN_CTOTHER_TERM).setVisible(
				false);// getButtonTree().getButton(CTButtonConst.BTN_CTOTHER_TERM).setVisible(false);
		getButtonTree().getButton(CTButtonConst.BTN_ASSIST_QUERY_FLOWSTATUE)
				.setVisible(false);// getButtonTree().getButton(CTButtonConst.BTN_ASSIST_QUERY_FLOWSTATUE).setVisible(false);
		getButtonTree().getButton(CTButtonConst.BTN_ASSIST_QUERY).setVisible(
				false);// 辅助查询
	};

	protected ButtonTree getButtonTree() {
		if (btTreeBase == null) {
			try {
				if (m_sBillType == "Z2")
					btTreeBase = new ButtonTree("40203001");
				if (m_sBillType == "Z4")
					btTreeBase = new ButtonTree("40203002");
				if (m_sBillType == "Z5")
					btTreeBase = new ButtonTree("40203003");
			} catch (BusinessException e) {
				MessageDialog.showErrorDlg(this, null, e.getMessage());
				reportException(e);
			}
		}
		return btTreeBase;
	}

	/**
	 * 显示首页 since V51
	 * 
	 * @author dgq
	 */
	protected void onTop() {

	}

	/**
	 * 显示末页 since V51
	 * 
	 * @author dgq
	 */
	protected void onBottom() {

	}

	public static String[] getDataPowerFieldFromDlgNotByProp(
			SCMQueryConditionDlg dlg, boolean isbypower,
			String[] notincludefield) {
		if (dlg == null)
			return null;
		QueryConditionVO[] vos = dlg.getConditionDatas();
		if (vos == null || vos.length <= 0)
			return null;
		ArrayList<String> codelist = new ArrayList<String>(20);

		HashSet<String> hsnotinclude = new HashSet<String>(30);
		if (notincludefield != null)
			for (int i = 0; i < notincludefield.length; i++)
				hsnotinclude.add(notincludefield[i]);

		for (int i = 0; i < vos.length; i++) {
			if (vos[i].getIfUsed().booleanValue()
					&& (!isbypower || dlg.isDataPower() || vos[i]
							.getIfDataPower().booleanValue())
					&& vos[i].getDataType().intValue() == QueryConditionClient.UFREF
					&& vos[i].getConsultCode() != null
					&& GenMethod.isDataPowerRef(vos[i].getConsultCode().trim())
					&& vos[i].getFieldCode() != null
					&& !hsnotinclude.contains(vos[i].getFieldCode().trim())
					&& !codelist.contains(vos[i].getFieldCode().trim()))
				codelist.add(vos[i].getFieldCode().trim());
		}
		if (codelist.size() > 0)
			return codelist.toArray(new String[codelist.size()]);
		return null;
	}

	/**
	 * 创建者：yb 功能：获取当前用户ID对应的业务员 参数： 返回： 例外： 日期：(2001-5-9 9:23:32)
	 * 修改日期，修改人，修改原因，注释标志：
	 */
	public static PsndocVO getPsndocByUserid() {
		return getPsndocByUserid(ClientEnvironment.getInstance()
				.getCorporation().getPrimaryKey(), ClientEnvironment
				.getInstance().getUser().getPrimaryKey());

	}

	/**
	 * 创建者：yb 功能：根据用户ID,获取对应的业务员 参数： 返回： 例外： 日期：(2001-5-9 9:23:32)
	 * 修改日期，修改人，修改原因，注释标志：
	 */
	public static PsndocVO getPsndocByUserid(String pk_corp, String userid) {
		if (pk_corp == null || userid == null)
			return null;
		try {
			/*
			 * return ((nc.itf.uap.rbac.IUserManageQuery)
			 * NCLocator.getInstance()
			 * .lookup("nc.itf.uap.rbac.IUserManageQuery"))
			 * .getPsndocByUserid(pk_corp, userid);
			 */
			// modified by lirr 2008-09-09 修改原因： 规范化
			return ((nc.itf.uap.rbac.IUserManageQuery) NCLocator.getInstance()
					.lookup(IUserManageQuery.class.getName()))
					.getPsndocByUserid(pk_corp, userid);
		} catch (Exception e) {
			GenMethod.handleException(null, null, e);
		}
		return null;
	}

	/**
	 * 方法功能描述：单据批处理操作
	 * <p>
	 * <b>examples:</b>
	 * <p>
	 * 使用示例
	 * <p>
	 * <b>参数说明</b>
	 * 
	 * @param sAction
	 *            批处理动作
	 *            <p>
	 * @author liuzy
	 * @time 2007-4-16 下午02:56:28
	 */
	// public void onBatchAction(String sAction) {
	//
	// int[] iaSeleRows = getCtBillListPanel().getHeadTable()
	// .getSelectedRows();
	// String shint = null;
	// if (iaSeleRows != null && iaSeleRows.length > 0) {
	// try {
	// isGlobelBatch = true;
	//
	// if ("审批".equals(sAction)) {
	// shint = nc.ui.ml.NCLangRes.getInstance().getStrByID(
	// "4020pub", "UPT4020pub-000243")/* @res "开始批量审批" */;
	// } else if ("弃审".equals(sAction)) {
	// shint = nc.ui.ml.NCLangRes.getInstance().getStrByID(
	// "4020pub", "UPT4020pub-000244")/* @res "开始批量弃审" */;
	// } else if ("生效".equals(sAction)) {
	// shint = nc.ui.ml.NCLangRes.getInstance().getStrByID(
	// "4020pub", "UPT4020pub-000245")/* @res "开始批量生效" */;
	// }
	//
	// Object[] oret = (Object[]) LongTimeTask.procclongTime(this,
	// shint, 10, 3, this.getClass().getName(), this,
	// "onBatchProcessInThread", new Class[] { int[].class,
	// String.class }, new Object[] { iaSeleRows,
	// sAction });
	//
	// if (oret != null && oret[1] != null)
	// showWarningMessage(oret[1].toString());
	// if (((Integer) oret[0]).intValue() != iaSeleRows.length)
	// LongTimeTask.procclongTime(this, nc.ui.ml.NCLangRes
	// .getInstance().getStrByID("4020pub",
	// "UPT4020pub-000242")/*
	// * @res "正在刷新界面..."
	// */, 0, 3, this.getClass().getName(), this, "onFresh",
	// new Class[] { boolean.class },
	// new Object[] { true });
	// } catch (Exception e) {
	// // 日志异常
	// nc.vo.scm.pub.SCMEnv.out(e);
	// // 按规范抛出异常
	// GenMethod.handleException(this, null, e);
	// } finally {
	// isGlobelBatch = false;
	// }
	//
	// this.updateUI();
	//
	// } else {
	// return;
	// }
	//
	// }
	/**
	 * 方法功能描述：阻塞当前线程，在新线程中处理sAction操作
	 * <p>
	 * <b>examples:</b>
	 * <p>
	 * 使用示例
	 * <p>
	 * <b>参数说明</b>
	 * 
	 * @param sAction
	 *            批处理动作
	 * @param iaSeleRows
	 *            表头所选的行
	 * @return
	 *            <p>
	 * @author liuzy
	 * @time 2007-4-16 下午06:02:04
	 */
	public Object[] onBatchProcessInThread(int[] iaSeleRows, String sAction) {
		// 返回消息串
		StringBuffer sb = new StringBuffer();
		// 合同单据VO
		ManageVO tempVO = null;
		// 当前单据下标
		int iIdtemp = m_iId;
		// 执行过程
		String stemp = null;
		// 错误数量
		int errcount = 0;
		// 休眠时间
		final int isleep = 200;
		// 返回值
		Object[] oret = new Object[2];
		for (int i = 0, j = iaSeleRows.length; i < j; i++) {
			stemp = null;
			tempVO = null;
			m_iId = iaSeleRows[i] + 1;
			try {
				tempVO = getCurVO();
				if (null == tempVO.getChildrenVO()
						|| tempVO.getChildrenVO().length < 1)
					loadBodyData();
				tempVO = getCurVO();
				if (tempVO != null) {
					if ("审批".equals(sAction)) {
						/*
						 * LongTimeTask.showHintMsg("单据" +
						 * tempVO.getParentVO().getCt_code() + "开始审批...");
						 */
						LongTimeTask.showHintMsg(nc.ui.ml.NCLangRes
								.getInstance().getStrByID(
										"4020pub",
										"UPT4020pub-000233",
										null,
										new String[] { tempVO.getParentVO()
												.getCt_code() }/*
																 * @res
																 * "合同{0}开始审批..."
																 */));
						// 调用审核方法

						String oncheckRtn = onCheck(true);
						if (null != oncheckRtn) {
							throw new nc.vo.pub.BusinessException(oncheckRtn);
						}
						stemp = nc.ui.ml.NCLangRes.getInstance().getStrByID(
								"4020pub",
								"UPT4020pub-000234",
								null,
								new String[] { tempVO.getParentVO()
										.getCt_code() }/* @res "合同{0}审批成功" */);
						LongTimeTask.showHintMsg(stemp);
					} else if ("弃审".equals(sAction)) {
						LongTimeTask.showHintMsg(nc.ui.ml.NCLangRes
								.getInstance().getStrByID(
										"4020pub",
										"UPT4020pub-000235",
										null,
										new String[] { tempVO.getParentVO()
												.getCt_code() }/*
																 * @res
																 * "合同{0}开始弃审..."
																 */));
						// 调用弃审方法
						// onCancelApprove(true);
						String onCancelAppRtn = onCancelApprove(true);
						if (null != onCancelAppRtn) {
							throw new nc.vo.pub.BusinessException(
									onCancelAppRtn);
						}
						stemp = nc.ui.ml.NCLangRes.getInstance().getStrByID(
								"4020pub",
								"UPT4020pub-000236",
								null,
								new String[] { tempVO.getParentVO()
										.getCt_code() }/* @res "合同{0}弃审成功" */);
						LongTimeTask.showHintMsg(stemp);
					} else if ("生效".equals(sAction)) {
						LongTimeTask.showHintMsg(nc.ui.ml.NCLangRes
								.getInstance().getStrByID(
										"4020pub",
										"UPT4020pub-000237",
										null,
										new String[] { tempVO.getParentVO()
												.getCt_code() }/*
																 * @res
																 * "合同{0}开始生效..."
																 */));
						onBatchValidate();
						if (!isExecPass) {
							isExecPass = true;
							String msg = nc.ui.ml.NCLangRes.getInstance()
									.getStrByID("4020pub", "UPT4020pub-000241")/*
																				 * @res
																				 * "合同{0}生效失败"
																				 */;
							throw new Exception("");
						}
						stemp = nc.ui.ml.NCLangRes.getInstance().getStrByID(
								"4020pub",
								"UPT4020pub-000238",
								null,
								new String[] { tempVO.getParentVO()
										.getCt_code() }/* @res "合同{0}生效成功" */);
						LongTimeTask.showHintMsg(stemp);
					}

					if (stemp != null) {
						sb.append(stemp + "\n");
						Thread.currentThread().sleep(isleep);
					}
				} else {
					continue;
				}
			} catch (Exception e) {
				if ("审批".equals(sAction))
					stemp = nc.ui.ml.NCLangRes
							.getInstance()
							.getStrByID(
									"4020pub",
									"UPT4020pub-000239",
									null,
									new String[] { tempVO.getParentVO()
											.getCt_code() }/* @res "合同{0}审批失败" */);
				else if ("弃审".equals(sAction))
					stemp = nc.ui.ml.NCLangRes
							.getInstance()
							.getStrByID(
									"4020pub",
									"UPT4020pub-000240",
									null,
									new String[] { tempVO.getParentVO()
											.getCt_code() }/* @res "合同{0}弃审失败" */);
				else if ("生效".equals(sAction))
					stemp = nc.ui.ml.NCLangRes
							.getInstance()
							.getStrByID(
									"4020pub",
									"UPT4020pub-000241",
									null,
									new String[] { tempVO.getParentVO()
											.getCt_code() }/* @res "合同{0}生效失败" */);
				LongTimeTask.showHintMsg(stemp);
				sb.append(stemp + "[" + e.getMessage() + "]");
				sb.append("\n");
				errcount++;
				try {
					Thread.currentThread().sleep(isleep);
				} catch (Exception ex) {
					// 日志异常
					nc.vo.scm.pub.SCMEnv.out(ex.getMessage());

				}
				// 日志异常
				nc.vo.scm.pub.SCMEnv.out(e);
			} finally {
				m_iId = iIdtemp;
			}
		}

		oret[0] = new Integer(errcount);
		oret[1] = sb.toString();
		return oret;
	}

	/**
	 * 方法功能描述：获得列表界面多选的单据
	 * <p>
	 * <b>examples:</b>
	 * <p>
	 * 使用示例
	 * <p>
	 * <b>参数说明</b>
	 * 
	 * @return
	 * <p>
	 * @author liuzy
	 * @time 2007-4-16 下午03:06:04
	 */
	public ManageVO[] getCurSeleVOs() {
		// 获得选取的行数
		int iRowCount = getCtBillListPanel().getHeadTable()
				.getSelectedRowCount();
		int[] iaSeleRows = getCtBillListPanel().getHeadTable()
				.getSelectedRows();
		if (iRowCount < 1 || iaSeleRows == null || iaSeleRows.length < 1) {
			return null;
		}
		ManageVO[] aMVOs = new ManageVO[iRowCount];// 返回的VO数组
		ExtendManageVO extMVO = new ExtendManageVO();// 从m_vBillVO中取出的VO
		ManageHeaderVO voHeader = new ManageHeaderVO();
		int j = -1;// 当前选中的行
		for (int i = 0; i < iRowCount; i++) {
			j = iaSeleRows[i];
			extMVO = (ExtendManageVO) m_vBillVO.elementAt(j);
			voHeader = (ManageHeaderVO) extMVO.getParentVO();
			if (extMVO != null && voHeader != null) {
				aMVOs[i].setParentVO(voHeader);
				aMVOs[i].setChildrenVO(extMVO.getTableVO("table"));
				aMVOs[i].setTermBb4s((TermBb4VO[]) extMVO.getTableVO("term"));
				aMVOs[i].setExpBb3s((ExpBb3VO[]) extMVO.getTableVO("cost"));
				aMVOs[i].setChangeBb1s((ChangeBb1VO[]) extMVO
						.getTableVO("history"));
				aMVOs[i].setManageExecs((ManageExecVO[]) extMVO
						.getTableVO("exec"));
				aMVOs[i].setMemoraBb2s((MemoraBb2VO[]) extMVO
						.getTableVO("note"));
				aMVOs[i].setBillStatus(voHeader.getStatus());
				aMVOs[i].setBillType(m_sBillType);
				aMVOs[i].setVBillCode(voHeader.getCt_code());
			}

		}

		return aMVOs;
	}

	protected ManageVO[] loadHeadData(String pk_corp, String sWhere)
			throws Exception {
		return ContractQueryHelper.queryAllHeadData(pk_corp, sWhere);
	}

	/**
	 * 父类方法重写
	 * 
	 * @see nc.ui.pub.linkoperate.ILinkAdd#doAddAction(nc.ui.pub.linkoperate.ILinkAddData)
	 *      added by lirr 2008-7-16
	 */
	public void doAddAction(ILinkAddData adddata) {
		Object paraVo = adddata.getUserObject();
		if (null != adddata && paraVo instanceof PUMessageVO) {
			// 价格审批单“生成合同”按钮调用
			// confBillFor28toThis((PUMessageVO) paraVo);

			PriceauditMergeVO priceauditVO = (PriceauditMergeVO) (((PUMessageVO) paraVo)
					.getAskvo());
			if (null == ((PriceauditHeaderVO) priceauditVO.getParentVO())
					.getBLinkOrder()) {
				((PriceauditHeaderVO) priceauditVO.getParentVO())
						.setBLinkOrder(new UFBoolean(true));
			}
			ManageVO manageVo = null;
			try {
				manageVo = (ManageVO) nc.ui.pub.change.PfChangeBO_Client
						.pfChangeBillToBill(priceauditVO,
								nc.vo.scm.constant.ScmConst.PO_PriceAudit,
								m_sBillType);
			} catch (BusinessException e) {
				// TODO 自动生成 catch 块
				// e.printStackTrace();
				SCMEnv.out(e.getMessage());

			}

			// ManageVO[] arrMangevos = splitManageVOs(manageVo);// 获得VO并分单
			ManageVO[] tempbillvos = null;
			tempbillvos = (ManageVO[]) nc.vo.scm.pub.vosplit.SplitBillVOs
					.getSplitVO(ManageVO.class.getName(), ManageHeaderVO.class
							.getName(), ManageItemVO.class.getName(), manageVo,
							new String[] { "currid", "bsc" },
							new String[] { "custid_b" });
			ManageVO[] arrMangevos = processAfterVoChange(tempbillvos);

			onAddCtRef(arrMangevos);

		}
	}

	/**
	 * 方法功能描述：简要描述本方法的功能。
	 * <p>
	 * <b>examples:</b>
	 * <p>
	 * 使用示例
	 * <p>
	 * <b>参数说明</b>
	 * 
	 * @param adddata
	 *            <p>
	 * @author lirr
	 * @time 2008-7-16 上午10:40:10
	 */
	private void confBillFor28toThis(PUMessageVO paraVo) {

		try {

			// if (null != adddata && adddata instanceof RefMsg) {
			// VO数据交换
			PriceauditMergeVO priceauditVO = (PriceauditMergeVO) paraVo
					.getAskvo();
			ManageVO manageVo = null;
			try {
				manageVo = (ManageVO) nc.ui.pub.change.PfChangeBO_Client
						.pfChangeBillToBill(priceauditVO,
								nc.vo.scm.constant.ScmConst.PO_PriceAudit,
								m_sBillType);
			} catch (BusinessException e) {
				// TODO 自动生成 catch 块
				// e.printStackTrace();
				SCMEnv.out(e.getMessage());
			}

			ManageVO[] arrMangevos = splitManageVOs(manageVo);// 获得VO并分单

			// m_vBillVO.clear();
			m_bAddFromBillFlag = true;
			m_vBillVOBeforTran = new Vector<ExtendManageVO>();

			m_vBillVOBeforTran.addAll(m_vBillVO);
			m_vBillVO.clear();

			if (arrMangevos == null || arrMangevos.length == 0) { // 
				m_iId = 0;
				m_iElementsNum = 0;
				getCtBillCardPanel().getBillData().clearViewData();
				getCtBillListPanel().setHeaderValueVO(null);
				getCtBillListPanel().getBodyBillModel().clearBodyData();

				setButtonStateForCof();

				this.showHintMessage(nc.ui.ml.NCLangRes.getInstance()
						.getStrByID("4020pub", "UPP4020pub-000070")/*
																	 * @res
																	 * "没有找到符合条件的数据"
																	 */);

				return;
			}
			for (int i = 0; i < arrMangevos.length; i++) {
				arrMangevos[i].getParentVO().setAttributeValue("ctflag",
						new Integer(BillState.FREE));

			}
			m_vBillVOForRef.clear();
			// 加载公式

			putInBillVOForRef(arrMangevos);

			if (m_vBillVOForRef != null && m_vBillVOForRef.size() > 0) {
				m_bIsCard = false;
				// 判断是在卡片模式下，还是在列表模式下
				m_bIsFirstClick = true; // 标志是第一次点击
				m_iId = 1;
				m_iElementsNum = m_vBillVOForRef.size();
				if (1 == m_iElementsNum) {
					m_bIsCard = false;
					onList();
					getCtBillCardPanel().setEnabled(true);
				} else {
					if (m_bIsCard) { // 卡片
						// 切换到列表模式下
						onList();

					} else { // 列表
						setListRateDigit();
						// 显示单据列表
						ManageHeaderVO[] vListVO = new ManageHeaderVO[m_vBillVOForRef
								.size()];
						if (m_vBillVOForRef.size() != 0)
							for (int i = 0; i < m_vBillVOForRef.size(); i++) {
								vListVO[i] = (ManageHeaderVO) ((ExtendManageVO) m_vBillVOForRef
										.elementAt(i)).getParentVO();
							}

						getCtBillListPanel().setHeaderValueVO(vListVO);

						// 执行公式
						getCtBillListPanel().getHeadBillModel()
								.execLoadFormula();
						getCtBillListPanel().getBodyBillModel("table")
								.execLoadFormula();

						getCtBillListPanel().getHeadTable()
								.setRowSelectionInterval(0, 0);
						getCtBillListPanel().setVisible(true);
						getCtBillCardPanel().setVisible(false);

					}
				}

				m_bChangeFlag = false; // 标志合同不可变更
				// 所有Table都需要重新载入数据
				for (int i = 1; i < 6; i++)
					m_bIsNeedReInit[i] = true;

			}

		} catch (Exception e) {
			// 日志异常
			nc.vo.scm.pub.SCMEnv.out(e);
			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"40081004", "UPT40081004-000020")/* @res "加载数据出错：" */
					+ e.getMessage());
		}
		// m_iBillState = OperationState.ADD ;
		setButtonStateForCof();
	}

	/**
	 * 根据供应商+币种+是否委外进行分单
	 * 
	 * @return ManageVO[]
	 * @param billvo
	 *            需要分单的ManageVO strParaArray java.lang.String[] 分单的依据
	 *            返回：ManageVO[] 经过分单的ManageVO数组
	 */
	private ManageVO[] splitManageVOs(ManageVO billvo) {
		if (billvo == null)
			return null;
		ManageVO[] retvos = null;
		ManageVO[] tempbillvos = null;
		ArrayList billvoslist = new ArrayList();
		PsndocVO psnvo = getPsndocByUserid();

		tempbillvos = (ManageVO[]) nc.vo.scm.pub.vosplit.SplitBillVOs
				.getSplitVO(ManageVO.class.getName(), ManageHeaderVO.class
						.getName(), ManageItemVO.class.getName(), billvo,
						new String[] { "custid_b", "currid", "bsc" }, null);

		if (tempbillvos != null) {

			for (int k = 0, loopk = tempbillvos.length; k < loopk; k++) {
				tempbillvos[k].getParentVO().setCustid(
						(String) tempbillvos[k].getChildrenVO()[0]
								.getAttributeValue("custid_b"));
				nc.bs.scm.pub.BillRowNoDMO.setVORowNoByRule(tempbillvos[k]
						.getChildrenVO(), getBillType(), "crowno");

				tempbillvos[k].getParentVO().setAttributeValue("unitname",
						m_sPkCorp);
				tempbillvos[k].getParentVO().setAttributeValue("unitname",
						m_sPkCorp);

				// 获取当前用户ID对应的业务员
				if (null == tempbillvos[k].getParentVO().getAttributeValue(
						"personnelid")) {
					if (psnvo != null) {// 如果对应有业务员
						if (null != psnvo.getPk_psndoc()) {
							// 置当前用户ID对应的业务员
							tempbillvos[k].getParentVO().setAttributeValue(
									"personnelid", psnvo.getPk_psndoc());
							tempbillvos[k].getParentVO().setAttributeValue(
									"pername", psnvo.getPk_psndoc());
						}
						if (null != psnvo.getPk_deptdoc()) {
							// 置当前用户ID对应的业务员对应的部门
							tempbillvos[k].getParentVO().setAttributeValue(
									"depid", psnvo.getPk_deptdoc());
							tempbillvos[k].getParentVO().setAttributeValue(
									"depname", psnvo.getPk_deptdoc());
						}

					}
				}

				tempbillvos[k].getParentVO().setAttributeValue("opername",
						m_sPkUser);
				tempbillvos[k].getParentVO().setAttributeValue("operid",
						m_sPkUser);
				tempbillvos[k].getParentVO().setAttributeValue("operdate",
						m_UFToday);
				// 合同签订日期置入默认值
				tempbillvos[k].getParentVO().setAttributeValue("subscribedate",
						m_UFToday);

				tempbillvos[k].getParentVO()
						.setAttributeValue("audiname", null);
				tempbillvos[k].getParentVO().setAttributeValue("auditdate",
						null);

				// set打印次数为０。
				tempbillvos[k].getParentVO().setAttributeValue("iprintcount",
						new Integer(0));

				// 一些关联的值

				billvoslist.add(tempbillvos[k]);

			}
		}

		if (billvoslist.size() > 0) {
			retvos = new ManageVO[billvoslist.size()];
			retvos = (ManageVO[]) billvoslist.toArray(retvos);
		}

		return retvos;

	}

	// added by lirr 2008-08-12 辅计量
	/**
	 * 作者：lirr 功能：辅计量单位转换、辅计量数量换算
	 * 
	 * 参数： 返回： 例外： 日期：(2008-08-12 08:25:09)
	 */
	public void setVODefaultMeas(ManageVO voManage) {
		ManageItemVO[] voaItem = (ManageItemVO[]) voManage.getChildrenVO();
		// 用于变量查询
		Vector vecBaseId = new Vector();
		Vector vecAssistId = new Vector();
		// 变量
		String sMangId = null;
		String sBaseId = null;
		String sAssistUnit = null;

		int iBodyLen = voaItem.length;
		// 获得存货基本主键、辅计量主键
		String[] invkeys = new String[voaItem.length];
		String[] astkeys = new String[voaItem.length];
		for (int i = 0; i < voaItem.length; i++) {
			invkeys[i] = (String) voaItem[i].getAttributeValue("invbasid");
			astkeys[i] = (String) voaItem[i].getAttributeValue("astmeasid");
		}
		// 获得存货信息
		InvVO[] invvos = getInvInfo(invkeys, astkeys);

	}

	/**
	 * 作者：lirr 功能：“收/付款”按钮调用，推式生成财务：付款单/收款单
	 * 
	 * 参数： 返回： 例外： 日期：(2008-08-12 08:25:09)
	 */
	protected void onAddSkOrFk() {
		// 要与财务单据进行VO对照的合同VO
		ManageVO billVOZ = null;
		// 临时的合同VO，取当前界面vo，过滤（本币价税合计—累计付款金额）大于0且状态为生效后 赋值给billVOZ
		ManageVO tempBillVOZ = new ManageVO();
		// 得到界面VO
		ExtendManageVO vo = (ExtendManageVO) getCtBillCardPanel()
				.getBillValueVOExtended(
						ExtendManageVO.class.getName(),
						ManageHeaderVO.class.getName(),
						new String[] { ManageItemVO.class.getName(),
								TermBb4VO.class.getName(),
								ExpBb3VO.class.getName(),
								MemoraBb2VO.class.getName(),
								ChangeBb1VO.class.getName(),
								ManageExecVO.class.getName() });
		tempBillVOZ = transVO(vo);
		try {
			// 列表界面转到卡片界面
			if (!m_bIsCard) {
				onList();
			}
			// 卡片界面获得当前界面VO

			else {
				/*
				 * if (null != getCurVO()) { tempBillVOZ = getCurVO(); }
				 */
				/*
				 * if (null != newManageVO) { tempBillVOZ =
				 * newManageVO.clone(newManageVO); } else {
				 * showHintMessage(nc.ui.ml.NCLangRes.getInstance()
				 * .getStrByID("4020pub", "UPT4020pub-000254") @res "请选择一条合同" );
				 * return; }
				 */
				if (null == tempBillVOZ) {
					showHintMessage(nc.ui.ml.NCLangRes.getInstance()
							.getStrByID("4020pub", "UPT4020pub-000254")
					/*
					 * @res "请选择一条合同"
					 */
					);
					return;
				}
			}

			// 过滤符合条件的合同 合同状态：生效，（本币价税合计—累计付款金额）大于0
			billVOZ = getBodyVOForArap(tempBillVOZ);

			if (null == billVOZ) {
				showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"4020pub", "UPT4020pub-000257")/*
														 * @res "没有符合条件的合同"
														 */);
				return;
			}

			ManageVO billVOZToArap = (ManageVO) billVOZ.clone(billVOZ);
			ManageHeaderVO head = billVOZToArap.getParentVO();
			head.setAttributeValue("currrate", head.getCurrrate());

			nc.vo.ct.pub.GenMethod.changeVoForFina(
					(ManageItemVO[]) (billVOZToArap.getChildrenVO()),
					billVOZToArap.getParentVO(), false);

			// 获得要推式生成的目标单据类型
			String destBillType = null;
			// 业务类型（财务需要，VO交换后，给财务的VO赋值）
			String busitype = null;
			// 单据大类（调用财务接口需要，采购合同调用时为“fk”，销售合同调用为“sk”,其他合同根据收付方向判断）
			String strDjdl = "fk";
			if (BillType.PURDAILY.equals(m_sBillType)) {
				strDjdl = "fk";
			} else if (BillType.SALEDAILY.equals(m_sBillType)) {
				strDjdl = "sk";
			} else if (BillType.OTHER.equals(m_sBillType)) {
				// 其他合同收付方向
				String strPayOrRec = null;
				/*
				 * strPayOrRec = ContractQueryHelper
				 * .queryRecorPayByCtType(tempBillVOZ.getParentVO()
				 * .getPk_ct_type());
				 */
				// select rec_or_pay from ct_type where pk_ct_type
				// modified by lirr 2009-8-27上午09:42:19
				// 修改时间:2009-09-11 修改人:吴伟平 修改原因: 类型转换错误
				Object[] obj = (Object[]) nc.ui.scm.pub.CacheTool.getCellValue(
						"ct_type", "pk_ct_type", "rec_or_pay", tempBillVOZ
								.getParentVO().getPk_ct_type());
				strPayOrRec = (String) obj[0];
				// 其他合同收付方向为“02” 付
				if ("02".equals(strPayOrRec)) {
					strDjdl = "fk";
				}
				// 其他合同收付方向为“01” 收
				else if ("01".equals(strPayOrRec)) {
					strDjdl = "sk";
				}

			}
			// 调用财务接口获得要推式生成的目标单据类型及业务类型
			/*
			 * ICreateCorpQueryService srv = (ICreateCorpQueryService) NCLocator
			 * .getInstance().lookup( ICreateCorpQueryService.class.getName());
			 */
			String beanName = IArapBillMapQureyPublic.class.getName();

			/* if (srv.isEnabled(m_sPkCorp, ProductCode.PROD_FI)) { */
			boolean isArAPEnabled = false;
			if (strDjdl.equals("fk")) {
				isArAPEnabled = nc.ui.sm.createcorp.CreateCorpQuery_Client
						.isEnabled(m_sPkCorp, ScmConst.m_sModuleCodeAP);
			} else if (strDjdl.equals("sk")) {
				isArAPEnabled = nc.ui.sm.createcorp.CreateCorpQuery_Client
						.isEnabled(m_sPkCorp, ScmConst.m_sModuleCodeAR);
			}

			if (isArAPEnabled) {
				IArapBillMapQureyPublic bo = (IArapBillMapQureyPublic) NCLocator
						.getInstance().lookup(beanName);

				BillTypeMapVO typeVo = bo.queryBillMap(m_sBillType, strDjdl,
						m_sPkCorp);
				if (null != typeVo) {
					// 要推式生成的目标单据类型
					destBillType = typeVo.getTargetbilltype();
					// 业务类型
					busitype = typeVo.getBusitype();
				} else {
					showHintMessage(nc.ui.ml.NCLangRes.getInstance()
							.getStrByID("4020pub", "UPT4020pub-000258")/*
																		 * @res
																		 * "没有要推式生成的单据类型"
																		 */);
					return;
				}
			} else {
				showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"4020pub", "UPT4020pub-000259")/*
														 * @res "财务模块没有启动"
														 */);
				return;
			}

			// VO数据交换
			DJZBVO destBillVoTemp = (DJZBVO) nc.ui.pub.change.PfChangeBO_Client
					.pfChangeBillToBill(billVOZToArap, m_sBillType,
							destBillType);
			if (null == destBillVoTemp) {
				showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"40080802", "UPT40080802-000316")/*
															 * @res "数据交换失败"
															 */);
				return;
			}
			// 为财务的VO设置“目标单据类型”及“业务类型”
			else {
				((DJZBHeaderVO) destBillVoTemp.getParentVO())
						.setDjlxbm(destBillType);
				((DJZBHeaderVO) destBillVoTemp.getParentVO())
						.setXslxbm(busitype);
			}
			// added by lirr 2008-11-03 解决流量问题 将vo压缩
			ObjectUtils.objectReference(destBillVoTemp);
			// 打开财务节点
			PfLinkData linkData = new PfLinkData();
			linkData
					.setUserObject(new Object[] { new DJZBVO[] { destBillVoTemp } });
			// 财务单据节点号
			String nodecode = PfUIDataCache.getBillType(destBillType)
					.getNodecode();
			// 打开财务节点
			SFClientUtil.openLinkedADDDialog(nodecode, this, linkData);

		} catch (Exception e) {
			// 日志异常
			nc.vo.scm.pub.SCMEnv.out(e);
			// 按规范抛出异常
			GenMethod.handleException(this, e.getMessage(), e);
		}

	}

	/*
	 * 将查询到的需转单的结果置入m_vBillVOForRef. lirr 2008-07-23,
	 * 
	 */
	protected void putInBillVOForRef(ManageVO[] arrMangevos) {
		// 存放所有表头vo
		CircularlyAccessibleValueObject[] headVosAll = new CircularlyAccessibleValueObject[arrMangevos.length];
		CircularlyAccessibleValueObject[] tableVOsAll = null;
		CircularlyAccessibleValueObject[] termVOsAll = null;

		ArrayList alTableVOs = new ArrayList(); // 所有表体vo
		ArrayList alTermVOs = new ArrayList(); // 所有条款vo

		int index = 0;

		ExtendManageVO[] arrExtendVO = new ExtendManageVO[arrMangevos.length];
		for (int k = 0; k < arrMangevos.length; k++) {
			arrExtendVO[k] = new ExtendManageVO();

			arrExtendVO[k].setParentVO(arrMangevos[k].getParentVO());
			headVosAll[k] = arrMangevos[k].getParentVO();

			// modified by liuzy 2008-04-17 V5.03查询改造，只返回第一条合同的表体与其他页签
			if (null == arrMangevos[k].getChildrenVO()) {
				m_vBillVOForRef.add(arrExtendVO[k]);
				continue;
			}
			tableVOsAll = arrMangevos[k].getChildrenVO();
			arrExtendVO[k].setTableVO("table", tableVOsAll);
			for (index = 0; index < tableVOsAll.length; index++)
				alTableVOs.add(tableVOsAll[index]);

			termVOsAll = arrMangevos[k].getTermBb4s();
			arrExtendVO[k].setTableVO("term", termVOsAll);
			if (termVOsAll != null && termVOsAll.length > 0) {
				for (index = 0; index < termVOsAll.length; index++)
					alTermVOs.add(termVOsAll[index]);
			}

			arrExtendVO[k].setTableVO("cost", arrMangevos[k].getExpBb3s());
			arrExtendVO[k].setTableVO("note", arrMangevos[k].getMemoraBb2s());
			arrExtendVO[k]
					.setTableVO("history", arrMangevos[k].getChangeBb1s());
			arrExtendVO[k].setTableVO("exec", arrMangevos[k].getManageExecs());

			m_vBillVOForRef.add(arrExtendVO[k]);
		}
		// 执行公式
		getCtBillListPanel().getBodyBillModel().execFormulasWithVO(headVosAll,
				getHeadTailFormulas());

		// 合同基本
		tableVOsAll = new CircularlyAccessibleValueObject[alTableVOs.size()];
		alTableVOs.toArray(tableVOsAll);

		/*
		 * // 自由项组合 for (int k = 0; k < tableVOsAll.length; k++) { FreeVO voFree =
		 * InvTool.getInvFreeVO((String) tableVOsAll[k]
		 * .getAttributeValue("invid")); // 存货管理主键 if (voFree != null) {
		 * voFree.setVfree1((String) tableVOsAll[k]
		 * .getAttributeValue("vfree1")); voFree.setVfree2((String)
		 * tableVOsAll[k] .getAttributeValue("vfree2"));
		 * voFree.setVfree3((String) tableVOsAll[k]
		 * .getAttributeValue("vfree3")); voFree.setVfree4((String)
		 * tableVOsAll[k] .getAttributeValue("vfree4"));
		 * voFree.setVfree5((String) tableVOsAll[k]
		 * .getAttributeValue("vfree5"));
		 * 
		 * tableVOsAll[k].setAttributeValue("vfree0", voFree
		 * .getWholeFreeItem()); } }
		 */
		// modified by lirr 2009-8-20下午07:26:55 减少连接数
		nc.ui.scm.pub.FreeVOParse freeVOParse = new nc.ui.scm.pub.FreeVOParse();
		freeVOParse.setFreeVO(tableVOsAll, null, "invid", false);
		// 合同基本执行公式
		/*
		 * BillItem[] tableItems = getCtBillCardPanel().getBillData()
		 * .getBodyItemsForTable(CTTableCode.BASE); BillItem invidItem =
		 * getCtBillCardPanel().getBodyItem(CTTableCode.BASE, "invcode");
		 * String[] strTableEditFormulas = null; strTableEditFormulas =
		 * BillUtil.getFormulas(tableItems, IBillItem.EDIT);
		 * strTableEditFormulas = invidItem.getEditFormulas();
		 */
		// getCtBillListPanel().getBodyBillModel().execFormulasWithVO(tableVOsAll,
		// strTableEditFormulas);
		getCtBillListPanel().getBodyBillModel().execFormulasWithVO(tableVOsAll,
				getTableFormulas());
		if (alTermVOs.size() > 0) {
			termVOsAll = new CircularlyAccessibleValueObject[alTermVOs.size()];
			alTermVOs.toArray(termVOsAll);
			getCtBillListPanel().getBodyBillModel().execFormulasWithVO(
					termVOsAll, getTermFormulas());
		}

	}

	/**
	 * 用于转单过来的列表表头显示。added by lirr 创建日期：(2008-7-23 14:43:41)
	 */
	protected void setListDataForRef() {

		if (m_vBillVOForRef.size() != 0) {
			ManageHeaderVO[] vListVO = new ManageHeaderVO[m_vBillVOForRef
					.size()];
			for (int i = 0; i < m_vBillVOForRef.size(); i++) {
				vListVO[i] = (ManageHeaderVO) ((ExtendManageVO) m_vBillVOForRef
						.elementAt(i)).getParentVO();
			}
			getCtBillListPanel().setHeaderValueVO(vListVO);

			// 执行公式
			getCtBillListPanel().getHeadBillModel().execLoadFormula();
			getCtBillListPanel().getHeadTable().setRowSelectionInterval(0, 0);
			// m_iId = m_vBillVOForRef.size() - 1;
			m_iId = 1;

		} else {
			if (m_vBillVO.size() != 0 || m_vBillVOBeforTran.size() != 0) {
				m_bAddFromBillFlag = false;
				/*
				 * Vector<ExtendManageVO> tempBillVO = null; tempBillVO = new
				 * Vector<ExtendManageVO>(); tempBillVO.addAll(m_vBillVO);
				 * tempBillVO.addAll(m_vBillVO.size(),m_vBillVOBeforTran);
				 * ManageHeaderVO[] vListVO = new
				 * ManageHeaderVO[tempBillVO.size()];
				 */
				// m_vBillVO.addAll(m_vBillVO.size(), m_vBillVOBeforTran);
				Vector<ExtendManageVO> vTempBillVO = new Vector<ExtendManageVO>(
						m_vBillVO.size() + m_vBillVOBeforTran.size());
				vTempBillVO.addAll(m_vBillVOBeforTran);
				vTempBillVO.addAll(m_vBillVOBeforTran.size(), m_vBillVO);
				m_vBillVO.clear();
				m_vBillVO.addAll(vTempBillVO);

				ManageHeaderVO[] vListVO = new ManageHeaderVO[m_vBillVO.size()];
				for (int i = 0; i < m_vBillVO.size(); i++) {
					vListVO[i] = (ManageHeaderVO) ((ExtendManageVO) m_vBillVO
							.elementAt(i)).getParentVO();
				}
				// added by lirr 2008 -12-12
				// 修改原因：转单完毕后m_iElementsNum需要更新，否则在删除时有问题
				m_iElementsNum = m_vBillVO.size();
				getCtBillListPanel().setHeaderValueVO(vListVO);

				// 执行公式
				getCtBillListPanel().getHeadBillModel().execLoadFormula();
				getCtBillListPanel().getHeadTable().setRowSelectionInterval(0,
						0);
				this.showHintMessage(NCLangRes.getInstance().getStrByID(
						"4020pub", "UPP4020pub-000271")/* 浏览 */);
				setButtonState();
			} else {
				//				 
				m_iId = 0;
				m_iElementsNum = 0;
				getCtBillCardPanel().getBillData().clearViewData();
				getCtBillListPanel().setHeaderValueVO(null);
				getCtBillListPanel().getBodyBillModel().clearBodyData();

				setButtonState();
				this.showHintMessage(nc.ui.ml.NCLangRes.getInstance()
						.getStrByID("4020pub", "UPP4020pub-000070")/*
																	 * @res
																	 * "没有找到符合条件的数据"
																	 * 
																	 */);

				m_bAddFromBillFlag = false;
			}
			return;

		}
	}

	public void setButtonStateForCof() {
		// 合同状态
		int iState = -1;

		switch (m_iTabbedFlag) {
		case 0:
			if (m_iBillState == OperationState.FREE) {
				iState = OperationState.FREE;

				// 当前没有一条单据时，设置其他的页签不可编辑，否则都可编辑
				getButtonTree().getButton(CTButtonConst.BTN_CTOTHER_TERM)
						.setEnabled(false);
				getButtonTree().getButton(CTButtonConst.BTN_CTOTHER_NOTE)
						.setEnabled(false);
				getButtonTree().getButton(CTButtonConst.BTN_CTOTHER_EXP)
						.setEnabled(false);
				getButtonTree().getButton(
						CTButtonConst.BTN_CTOTHER_CHANGEHISTORY).setEnabled(
						false);
				getButtonTree().getButton(
						CTButtonConst.BTN_CTOTHER_CHANGEHISTORY).setEnabled(
						false);

				getButtonTree().getButton(CTButtonConst.BTN_BILL_DELETE)
						.setEnabled(false);
				getButtonTree().getButton(CTButtonConst.BTN_BILL_COPY)
						.setEnabled(false);
				getButtonTree().getButton(CTButtonConst.BTN_BROWSE_PREVIOUS)
						.setEnabled(false);
				getButtonTree().getButton(CTButtonConst.BTN_BROWSE_NEXT)
						.setEnabled(false);
				getButtonTree().getButton(CTButtonConst.BTN_EXECUTE)
						.setEnabled(false);
				getButtonTree().getButton(CTButtonConst.BTN_PRINT_PRINT)
						.setEnabled(false);
				getButtonTree().getButton(CTButtonConst.BTN_PRINT_PREVIEW)
						.setEnabled(false);
				getButtonTree()
						.getButton(CTButtonConst.BTN_EXECUTE_SENDAPPRPVE)
						.setEnabled(false);
				getButtonTree().getButton(CTButtonConst.BTN_SWITCH).setEnabled(
						false);
				getButtonTree().getButton(
						CTButtonConst.BTN_ASSIST_FUNC_DOCUMENT).setEnabled(
						false);

				getButtonTree().getButton(CTButtonConst.BTN_CTOTHER_EXEC)
						.setEnabled(false);

				getButtonTree().getButton(CTButtonConst.BTN_AUDIT).setEnabled(
						false);

				getButtonTree().getButton(CTButtonConst.BTN_BILL).setEnabled(
						true);

				getButtonTree().getButton(CTButtonConst.BTN_BILL_CANCEL)
						.setEnabled(true);
				getButtonTree().getButton(CTButtonConst.BTN_QUERY).setEnabled(
						false);

				getButtonTree().getButton(CTButtonConst.BTN_SAVE).setEnabled(
						false);
				getButtonTree().getButton(CTButtonConst.BTN_LINE).setEnabled(
						false);
				getButtonTree().getButton(CTButtonConst.BTN_AUDIT).setEnabled(
						false);
				getButtonTree().getButton(CTButtonConst.BTN_EXECUTE)
						.setEnabled(false);
				getButtonTree().getButton(CTButtonConst.BTN_SWITCH).setEnabled(
						false);
				getButtonTree().getButton(CTButtonConst.BTN_ASSIST_QUERY)
						.setEnabled(false);
				getButtonTree().getButton(CTButtonConst.BTN_ASSIST_FUNC)
						.setEnabled(false);
				if (m_iId == 0) {
					getButtonTree().getButton(CTButtonConst.BTN_ADD)
							.setEnabled(true);
					getButtonTree().getButton(CTButtonConst.BTN_BILL_EDIT)
							.setEnabled(false);

				} else {
					getButtonTree().getButton(CTButtonConst.BTN_ADD)
							.setEnabled(false);
					getButtonTree().getButton(CTButtonConst.BTN_BILL_EDIT)
							.setEnabled(true);

				}

			} else if (m_iBillState == OperationState.ADD) {
				iState = OperationState.ADD;
				getButtonTree().getButton(CTButtonConst.BTN_AUDIT).setEnabled(
						false);
				getButtonTree().getButton(CTButtonConst.BTN_CTOTHER_TERM)
						.setEnabled(false);
				getButtonTree().getButton(CTButtonConst.BTN_CTOTHER_NOTE)
						.setEnabled(false);
				getButtonTree().getButton(CTButtonConst.BTN_CTOTHER_EXP)
						.setEnabled(false);
				getButtonTree().getButton(
						CTButtonConst.BTN_CTOTHER_CHANGEHISTORY).setEnabled(
						false);
				getButtonTree().getButton(
						CTButtonConst.BTN_CTOTHER_CHANGEHISTORY).setEnabled(
						false);

				// getButtonTree().getButton(CTButtonConst.BTN_BILL).setEnabled(false);
				getButtonTree().getButton(CTButtonConst.BTN_BILL).setEnabled(
						true);
				getButtonTree().getButton(CTButtonConst.BTN_ADD).setEnabled(
						false);
				getButtonTree().getButton(CTButtonConst.BTN_BILL_EDIT)
						.setEnabled(false);
				getButtonTree().getButton(CTButtonConst.BTN_BILL_COPY)
						.setEnabled(false);
				getButtonTree().getButton(CTButtonConst.BTN_BILL_DELETE)
						.setEnabled(false);
				getButtonTree().getButton(CTButtonConst.BTN_SAVE).setEnabled(
						true);
				getButtonTree().getButton(CTButtonConst.BTN_BILL_CANCEL)
						.setEnabled(true);
				getButtonTree().getButton(CTButtonConst.BTN_BILL_DELETE)
						.setEnabled(false);
				if (m_bIsCard) // 是卡片模式
					getButtonTree().getButton(CTButtonConst.BTN_LINE)
							.setEnabled(true);
				else
					getButtonTree().getButton(CTButtonConst.BTN_LINE)
							.setEnabled(false);

				getButtonTree().getButton(CTButtonConst.BTN_PRINT_PRINT)
						.setEnabled(false);
				getButtonTree().getButton(CTButtonConst.BTN_PRINT_PREVIEW)
						.setEnabled(false);
				getButtonTree()
						.getButton(CTButtonConst.BTN_EXECUTE_SENDAPPRPVE)
						.setEnabled(false);

				getButtonTree().getButton(CTButtonConst.BTN_BROWSE_PREVIOUS)
						.setEnabled(false);
				getButtonTree().getButton(CTButtonConst.BTN_BROWSE_NEXT)
						.setEnabled(false);
				getButtonTree().getButton(CTButtonConst.BTN_EXECUTE)
						.setEnabled(false);

				getButtonTree().getButton(CTButtonConst.BTN_SWITCH).setEnabled(
						false);
				getButtonTree().getButton(CTButtonConst.BTN_QUERY).setEnabled(
						false);
				getButtonTree().getButton(
						CTButtonConst.BTN_ASSIST_FUNC_DOCUMENT).setEnabled(
						false);
				getButtonTree().getButton(CTButtonConst.BTN_BROWSE_REFRESH)
						.setEnabled(false);
				getButtonTree().getButton(CTButtonConst.BTN_ASSIST_QUERY)
						.setEnabled(false);
				getButtonTree().getButton(CTButtonConst.BTN_ASSIST_FUNC)
						.setEnabled(false);
			}

			// 根据按钮的权限设置页签的状态[V2.3]
			setTabbedState(getButtonTree().getButton(
					CTButtonConst.BTN_CTOTHER_TERM).isEnabled(),
					getButtonTree().getButton(CTButtonConst.BTN_CTOTHER_NOTE)
							.isEnabled(), getButtonTree().getButton(
							CTButtonConst.BTN_CTOTHER_EXP).isEnabled(),
					getButtonTree().getButton(
							CTButtonConst.BTN_CTOTHER_CHANGEHISTORY)
							.isEnabled(), getButtonTree().getButton(
							CTButtonConst.BTN_CTOTHER_CHANGEHISTORY)
							.isEnabled());
			// 扩展按钮默认加在合同基本页签之上,不用考虑其他页签的处理情况.
			// iState: 合同的状态.

			// added by lirr 2008-09-10
			getButtonTree().getButton(CTButtonConst.BTN_PAY).setEnabled(false);
			getButtonTree().getButton(CTButtonConst.BTN_REC).setEnabled(false);
			getButtonTree().getButton(CTButtonConst.BTN_PAYORREC).setEnabled(
					false);
			break;

		default: // 设置1，2，3，4页签的状态
			setTbButtonState();
		}
		setExtendBtnsStat(iState);
		updateButtons();

	}

	/**
	 * 此处插入方法说明。 added by lirr 2008-07-10 参照请购单/价格审批单生成采购合同 创建日期：(2008-07-15)
	 */
	protected void onAddCtRef(ManageVO[] manageVos) {

		m_bAddFromBillFlag = true;
		m_vBillVOBeforTran = new Vector<ExtendManageVO>();
		m_vBillVOBeforTran.clear();
		m_vBillVOBeforTran.addAll(m_vBillVO);
		m_vBillVO.clear();
		try {
			if (manageVos == null || manageVos.length == 0) { // 
				m_iId = 0;
				m_iElementsNum = 0;
				getCtBillCardPanel().getBillData().clearViewData();
				getCtBillListPanel().setHeaderValueVO(null);
				getCtBillListPanel().getBodyBillModel().clearBodyData();

				setButtonState();
				this.showHintMessage(nc.ui.ml.NCLangRes.getInstance()
						.getStrByID("4020pub", "UPP4020pub-000070")/*
																	 * @res
																	 * "没有找到符合条件的数据"
																	 */);

				return;
			}
			for (int i = 0; i < manageVos.length; i++) {
				manageVos[i].getParentVO().setAttributeValue("ctflag",
						new Integer(BillState.FREE));

			}
			m_vBillVOForRef.clear();
			// 加载公式

			putInBillVOForRef(manageVos);

			if (m_vBillVOForRef != null && m_vBillVOForRef.size() > 0) {
				m_bIsCard = false;
				// 判断是在卡片模式下，还是在列表模式下
				m_bIsFirstClick = true; // 标志是第一次点击
				m_iId = 1;
				// m_iElementsNum = m_vBillVOForRef.size();
				if (m_bIsCard) { // 卡片
					// 切换到列表模式下
					onList();

				} else { // 列表
					setListRateDigit();
					// 显示单据列表
					ManageHeaderVO[] vListVO = new ManageHeaderVO[m_vBillVOForRef
							.size()];
					if (m_vBillVOForRef.size() != 0)
						for (int i = 0; i < m_vBillVOForRef.size(); i++) {
							vListVO[i] = (ManageHeaderVO) ((ExtendManageVO) m_vBillVOForRef
									.elementAt(i)).getParentVO();
						}

					getCtBillListPanel().setHeaderValueVO(vListVO);

					// 执行公式
					getCtBillListPanel().getHeadBillModel().execLoadFormula();
					getCtBillListPanel().getBodyBillModel("table")
							.execLoadFormula();

					getCtBillListPanel().getHeadTable()
							.setRowSelectionInterval(0, 0);
					getCtBillListPanel().setVisible(true);
					getCtBillCardPanel().setVisible(false);

				}
				// }

				m_bChangeFlag = false; // 标志合同不可变更
				// 所有Table都需要重新载入数据
				for (int i = 1; i < 6; i++)
					m_bIsNeedReInit[i] = true;

			}

		} catch (Exception e) {
			// 日志异常
			nc.vo.scm.pub.SCMEnv.out(e);
			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"40081004", "UPT40081004-000020")/* @res "加载数据出错：" */
					+ e.getMessage());
		}
		// m_iBillState = OperationState.ADD ;
		setButtonStateForCof();
	}

	// added by lirr 2008-08-29
	protected ManageVO[] processAfterVoChange(ManageVO[] arrMangevos) {

		CircularlyAccessibleValueObject[] tableVOsAll = null;
		ArrayList alTableVOs = new ArrayList(); // 所有表体vo
		ArrayList billvoslist = new ArrayList();
		ManageVO[] retvos = null;
		// UFBoolean bTaxration = new UFBoolean(true);
		UFBoolean bTaxration = UFBoolean.TRUE;
		for (int k = 0; k < arrMangevos.length; k++) {

			if (null == arrMangevos[k].getChildrenVO()) {

				continue;
			}
			tableVOsAll = arrMangevos[k].getChildrenVO();

			for (int index = 0; index < tableVOsAll.length; index++) {
				alTableVOs.add(tableVOsAll[index]);

			}
			for (int index = 0; index < tableVOsAll.length; index++) {
				/*
				 * if (null == tableVOsAll[index].getAttributeValue("taxration") ||
				 * "".equals((String) tableVOsAll[index]
				 * .getAttributeValue("taxration"))) {
				 */
				if (null == tableVOsAll[index].getAttributeValue("taxration")
						|| ((UFDouble) tableVOsAll[index]
								.getAttributeValue("taxration"))
								.compareTo(UFDouble.ZERO_DBL) == 0) {
					bTaxration = UFBoolean.FALSE;
					break;
				}
			}

		}

		// 合同基本
		tableVOsAll = new CircularlyAccessibleValueObject[alTableVOs.size()];
		alTableVOs.toArray(tableVOsAll);
		// 执行存货编辑公式
		BillItem invidItem = getCtBillCardPanel().getBodyItem(CTTableCode.BASE,
				"invcode");
		if (UFBoolean.TRUE == bTaxration) {
			String[] strTableEditFormulas = {
					"invid->getColValue(bd_invmandoc,pk_invmandoc,pk_invmandoc,invid)",
					"invbasid->getColValue(bd_invmandoc,pk_invbasdoc,pk_invmandoc,invid)",
					"spec->getColValue(bd_invbasdoc,invspec,pk_invbasdoc,invbasid)",
					"mod->getColValue(bd_invbasdoc,invtype,pk_invbasdoc,invbasid)",
					"measid->getColValue(bd_invbasdoc,pk_measdoc,pk_invbasdoc,invbasid)",
					"measname->getColValue(bd_measdoc,measname,pk_measdoc,measid)",
					"invname->getColValue(bd_invbasdoc,invname,pk_invbasdoc,invbasid)",
			// "pk_taxitems->getColValue(bd_invbasdoc,pk_taxitems,pk_invbasdoc,invbasid)",
			// "taxration->getColValue(bd_taxitems,taxratio,pk_taxitems,pk_taxitems)"
			};

			getCtBillListPanel().getBodyBillModel().execFormulasWithVO(
					tableVOsAll, strTableEditFormulas);
		}

		else if (UFBoolean.FALSE == bTaxration) {
			String[] strTableEditFormulasNoTax = {
					"invid->getColValue(bd_invmandoc,pk_invmandoc,pk_invmandoc,invid)",
					"invbasid->getColValue(bd_invmandoc,pk_invbasdoc,pk_invmandoc,invid)",
					"spec->getColValue(bd_invbasdoc,invspec,pk_invbasdoc,invbasid)",
					"mod->getColValue(bd_invbasdoc,invtype,pk_invbasdoc,invbasid)",
					"measid->getColValue(bd_invbasdoc,pk_measdoc,pk_invbasdoc,invbasid)",
					"measname->getColValue(bd_measdoc,measname,pk_measdoc,measid)",
					"invname->getColValue(bd_invbasdoc,invname,pk_invbasdoc,invbasid)",
					// "pk_taxitems->iif(getColValue(bd_invmandoc,mantaxitem,pk_invmandoc,invid)
					// == null
					// ,getColValue(bd_invmandoc,mantaxitem,pk_invmandoc,invid),getColValue(bd_invbasdoc,pk_taxitems,pk_invbasdoc,invbasid))",
					"pk_taxitems->iif(getColValue(bd_invmandoc,mantaxitem,pk_invmandoc,invid) == null ,getColValue(bd_invbasdoc,pk_taxitems,pk_invbasdoc,invbasid),getColValue(bd_invmandoc,mantaxitem,pk_invmandoc,invid))",
					"taxration->getColValue(bd_taxitems,taxratio,pk_taxitems,pk_taxitems)" };
			getCtBillListPanel().getBodyBillModel().execFormulasWithVO(
					tableVOsAll, strTableEditFormulasNoTax);
		}

		HashSet<String> hsInvbasids = new HashSet<String>();
		HashSet<String> hsInvbasidsAll = new HashSet<String>();
		for (CircularlyAccessibleValueObject vo : tableVOsAll) {
			hsInvbasidsAll.add((String) vo.getAttributeValue("invbasid"));
		}
		if (null != hsInvbasidsAll && hsInvbasidsAll.size() > 0) {
			String[] sInvbasidsAll = new String[hsInvbasids.size()];
			hsInvbasidsAll.toArray(sInvbasidsAll);
			InvTool.loadBatchAssistManaged(sInvbasidsAll);

			for (int i = 0; i < tableVOsAll.length; i++) {
				if (InvTool.isAssUnitManaged((String) tableVOsAll[i]
						.getAttributeValue("invbasid"))) {
					hsInvbasids.add((String) tableVOsAll[i]
							.getAttributeValue("invbasid"));

				}

			}
		}
		// 存货基本档案id 数组
		if (null != hsInvbasids && hsInvbasids.size() > 0) {
			String[] sInvbasids = new String[hsInvbasids.size()];
			hsInvbasids.toArray(sInvbasids);

			// 得到辅单位
			loadBatchDefaultPUAssUnit(sInvbasids, "pk_measdoc2");

			ArrayList<String> alassUnit = new ArrayList<String>();
			for (int i = 0; i < arrMangevos.length; i++) {
				for (int j = 0; j < arrMangevos[i].getChildrenVO().length; j++) {
					String sBaseId = arrMangevos[i].getChildrenVO()[j]
							.getAttributeValue("invbasid").toString();
					if (null == arrMangevos[i].getChildrenVO()[j]
							.getAttributeValue("astmeasid")
							|| StringUtil.isEmptyWithTrim(arrMangevos[i]
									.getChildrenVO()[j].getAttributeValue(
									"astmeasid").toString())) {

						// String sAstmeasid = arrMangevos[i].getChildrenVO()[j]
						// .getAttributeValue("astmeasid").toString();
						// if (InvTool.isAssUnitManaged(sBaseId) && null ==
						// sAstmeasid) {
						// if (InvTool.isAssUnitManaged(sBaseId)) {
						if (InvTool.isAssUnitManaged(sBaseId)) {
							arrMangevos[i].getChildrenVO()[j]
									.setAttributeValue("astmeasid",
									// getDefaultPUAssUnit(sBaseId));
											m_hmapCTDefaultAssUnit.get(sBaseId));
							/*
							 * String[] strTableEditFormulasAst = {
							 * "astmeasid->getColValue(bd_measdoc,pk_measdoc,shortname,astmeascode)",
							 * "astmeasname->getColValue(bd_measdoc,measname,shortname,astmeascode)" };
							 * 
							 * getCtBillListPanel().getBodyBillModel()
							 * .execFormulasWithVO(tableVOsAll,
							 * strTableEditFormulasAst);
							 */
						}

						// }
					}
				}

			}
		}

		PsndocVO psnvo = getPsndocByUserid();
		if (arrMangevos != null) {

			for (int k = 0, loopk = arrMangevos.length; k < loopk; k++) {
				arrMangevos[k].getParentVO().setCustid(
						(String) arrMangevos[k].getChildrenVO()[0]
								.getAttributeValue("custid_b"));
				nc.bs.scm.pub.BillRowNoDMO.setVORowNoByRule(arrMangevos[k]
						.getChildrenVO(), getBillType(), "crowno");

				arrMangevos[k].getParentVO().setAttributeValue("unitname",
						m_sPkCorp);

				// 获取当前用户ID对应的业务员
				if (null == arrMangevos[k].getParentVO().getAttributeValue(
						"personnelid")) {
					if (psnvo != null) {// 如果对应有业务员
						if (null != psnvo.getPk_psndoc()) {
							// 置当前用户ID对应的业务员
							arrMangevos[k].getParentVO().setAttributeValue(
									"personnelid", psnvo.getPk_psndoc());
							arrMangevos[k].getParentVO().setAttributeValue(
									"pername", psnvo.getPk_psndoc());
						}
						if (null != psnvo.getPk_deptdoc()) {
							// 置当前用户ID对应的业务员对应的部门
							arrMangevos[k].getParentVO().setAttributeValue(
									"depid", psnvo.getPk_deptdoc());
							arrMangevos[k].getParentVO().setAttributeValue(
									"depname", psnvo.getPk_deptdoc());
						}

					}
				}

				arrMangevos[k].getParentVO().setAttributeValue("opername",
						m_sPkUser);
				arrMangevos[k].getParentVO().setAttributeValue("operid",
						m_sPkUser);
				arrMangevos[k].getParentVO().setAttributeValue("operdate",
						m_UFToday);
				// 合同签订日期置入默认值
				arrMangevos[k].getParentVO().setAttributeValue("subscribedate",
						m_UFToday);

				arrMangevos[k].getParentVO()
						.setAttributeValue("audiname", null);
				arrMangevos[k].getParentVO().setAttributeValue("auditdate",
						null);

				// set打印次数为０。
				arrMangevos[k].getParentVO().setAttributeValue("iprintcount",
						new Integer(0));
				// arrMangevos[k].getParentVO().setAttributeValue("currrate",);
				billvoslist.add(arrMangevos[k]);
			}
		}
		if (billvoslist.size() > 0) {
			retvos = new ManageVO[billvoslist.size()];
			retvos = (ManageVO[]) billvoslist.toArray(retvos);
		}

		return retvos;

	}

	/**
	 * 作者：LIRR 功能：批次加载存货基本ID的辅单位 处理: 参数：String[] saBaseId 存货基本ID 返回：无 例外：无
	 * 日期：(2008-8-29 11:39:21) 修改日期 2009-3-5上午11:00:21 修改人，lirr
	 * 修改原因，注释标志：新增销售合同参照销售报价单 需要得到销售默认单位
	 */
	public static void loadBatchDefaultPUAssUnit(String[] saBaseId,
			String stypePk) {

		if (saBaseId == null) {
			return;
		}
		if (m_hmapCTDefaultAssUnit == null) {
			m_hmapCTDefaultAssUnit = new HashMap();
		}
		if (m_hmapCTSoDefaultAssUnit == null) {
			m_hmapCTSoDefaultAssUnit = new HashMap();
		}

		Object[] oaAssUnit = null;
		try {
			/*
			 * oaAssUnit = (Object[]) nc.ui.scm.pub.CacheTool.getColumnValue(
			 * "bd_invbasdoc", "pk_invbasdoc", "pk_measdoc1", saBaseId);
			 */
			// modified by lirr 2008-11-26 辅计量选采购默认单位（只有参照请购单时用）
			oaAssUnit = (Object[]) nc.ui.scm.pub.CacheTool.getColumnValue(
					"bd_invbasdoc", "pk_invbasdoc", stypePk, saBaseId);

		} catch (Exception ee) {
			/** 不必抛出 */
			SCMEnv.out(ee);
		}
		int iLen = saBaseId.length;
		for (int i = 0; i < iLen; i++) {
			/*
			 * if (oaAssUnit == null || oaAssUnit[i] == null) {//
			 * oaAssUnit[i].toString(),改为 // oaAssUnit[i] // 李冉冉2008-09-01 若 //
			 * oaAssUnit[i】为空则报空异常 m_hmapCTDefaultAssUnit.put(saBaseId[i], ""); }
			 * else { m_hmapCTDefaultAssUnit .put(saBaseId[i],
			 * oaAssUnit[i].toString()); }
			 */
			if ("pk_measdoc2".equals(stypePk)) {
				if (oaAssUnit != null && oaAssUnit[i] != null) {
					m_hmapCTDefaultAssUnit.put(saBaseId[i], oaAssUnit[i]
							.toString());
				} else {
					m_hmapCTDefaultAssUnit.put(saBaseId[i], "");
				}

			} else if ("pk_measdoc1".equals(stypePk)) {
				if (oaAssUnit != null && oaAssUnit[i] != null) {
					m_hmapCTSoDefaultAssUnit.put(saBaseId[i], oaAssUnit[i]
							.toString());
				} else {
					m_hmapCTSoDefaultAssUnit.put(saBaseId[i], "");
				}

			}
		}
	}

	/**
	 * 作者：LIRR 功能：得到某此存货基本ID的辅单位 处理: 参数：String sBaseId 存货基本ID 返回：无 例外：无
	 * 日期：(2008-8-29 11:39:21) 修改日期，修改人，修改原因，注释标志：
	 */
	public static String getDefaultPUAssUnit(String sBaseId) {

		loadBatchDefaultPUAssUnit(new String[] { sBaseId }, "pk_measdoc2");

		Object oValue = m_hmapCTDefaultAssUnit.get(sBaseId);
		if (oValue == null || oValue.equals("")) {
			return null;
		}
		return (String) oValue;

	}

	/**
	 * 设置模版批修改属性 (普通单)
	 */
	public void setBillCardPaneFillEnable(BillCardPanel cardpane) {
		if (cardpane == null)
			return;
		BillItem[] bims = cardpane.getBodyItems();
		if (bims == null)
			return;
		String[] enablefillkey = null;
		if (BillType.PURDAILY.equals(m_sBillType)) {
			enablefillkey = new String[] { "amount", "oriprice", "taxration",
					"oritaxprice", "delivdate", "vschemename" };
		} else if (BillType.SALEDAILY.equals(m_sBillType)) {
			enablefillkey = new String[] { "amount", "oriprice", "taxration",
					"oritaxprice", "delivdate", "vreceiptcorpname",
					"vreceiveaddress", "vreceiptareaname", "vrecaddrnodename" };
		} else if (BillType.OTHER.equals(m_sBillType)) {
			enablefillkey = new String[] { "amount", "oriprice", "taxration",
					"oritaxprice", "delivdate" };
		}

		HashSet<String> hs = new HashSet<String>();
		for (String key : enablefillkey)
			hs.add(key);
		for (BillItem bi : bims) {
			if (bi.getKey() != null && hs.contains(bi.getKey()))
				bi.setFillEnabled(true);
			else
				bi.setFillEnabled(false);
		}
	}

	/**
	 * 此处插入方法说明。 功能描述:部门 作者：王亮 输入参数: 返回值: 异常处理: 日期:(2003-7-3 9:34:29)
	 */
	public void afterTraspEdit() {

		String pk = ((UIRefPane) getCtBillCardPanel().getHeadItem("transpmode")
				.getComponent()).getRefPK();
		for (int i = 0; i < getCtBillCardPanel().getBillModel(CTTableCode.BASE)
				.getRowCount(); i++) {

			getCtBillCardPanel().getBillModel(CTTableCode.BASE).setValueAt(pk,
					i, "ctranspmodeid");
		/*	String[] strTableEditFormulas = { "transpnametable->getColValue(bd_sendtype,sendname,pk_sendtype,ctranspmodeid)" };

			getCtBillCardPanel().getBillModel().execFormulas(
					strTableEditFormulas);*/
			// 修改时间:2009-09-11 修改人: 吴伟平 修改原因: 当表头改变时执行执行公式，表体VO状态为UNCHANGGE
			if (null != getCtBillCardPanel().getHeadItem("transpmode")) {
			    // modified by lirr 2009-11-25下午03:30:36  修改合同表头的运输方式 只有未修改的行才应该改为修改状态，
			    //否则把新增行删除行改为修改状态 导致数据无法更新
			   if(getCtBillCardPanel().getBillModel().getRowState(i)  == BillModel.NORMAL){
  				getCtBillCardPanel().getBillModel().setRowState(i,
  						//VOStatus.UPDATED);
  						BillModel.MODIFICATION);
			   } 
			}

		}
		if(null != getCtBillCardPanel().getBodyItem("transpnametable")){
		    String[] strTableEditFormulas = { "transpnametable->getColValue(bd_sendtype,sendname,pk_sendtype,ctranspmodeid)" };

		    getCtBillCardPanel().getBillModel().execFormulas(
		        strTableEditFormulas);
		}
	 
		// getCtBillCardPanel().getBillModel().execLoadFormulaByKey("ctranspmodeid");
	}

	// added by lirr 推式生成财务单据 过滤合同 " ct.ctflag =" + BillState.VALIDATE" and
	// (ct_b.natitaxsummny-ct_b.ntotalgpmny) > 0";
	private ManageVO getBodyVOForArap(ManageVO vo) {

		if (null == vo) {
			String sMessage = nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"4020pub", "UPT4020pub-000254")/*
													 * @res "请选择一条合同"
													 */;
			MessageDialog.showErrorDlg(this, null, sMessage);
			return null;
		}
		/*
		 * // 检查时间戳 try {
		 * 
		 * GenMethod.callICService("nc.bs.ct.pub.CTCheckDMO", "checkTimeStamp",
		 * new Class[] { vo.getClass() }, new Object[] { vo }); } catch
		 * (Exception e) { // 日志异常 nc.vo.scm.pub.SCMEnv.out(e); // 按规范抛出异常
		 * GenMethod.handleException(this, e.getMessage(), e); }
		 */
		// 合同行
		ManageItemVO[] voItem = (ManageItemVO[]) vo.getChildrenVO();
		// 存放所有符合条件的合同行
		ArrayList<ManageItemVO> alItemVOs = new ArrayList<ManageItemVO>();
		// 若直接新增、审核、生效则缓存中getAttributeValue("natitaxsummny")，getAttributeValue("ntotalgpmny")为UFDouble类型
		// 本币价税合计
		// UFDouble ufNatitaxsummny = new UFDouble(0);
		// 原币价税合计 // added by lirr 2009-7-20 上午10:37:55 原因：改为原币控制
		UFDouble ufOritaxsummny = new UFDouble(0);
		// 累计付款金额
		UFDouble ufNtotalgpmny = new UFDouble(0);
		// 若从数据库中查询以后再放入缓存getAttributeValue("natitaxsummny")，getAttributeValue("ntotalgpmny")为BigDecimal类型
		// 本币价税合计
		// java.math.BigDecimal bNatitaxsummny = new BigDecimal(0);
		// 原币价税合计 // added by lirr 2009-7-20 上午10:37:55 原因：改为原币控制
		java.math.BigDecimal bOritaxsummny = new BigDecimal(0);
		// 累计付款金额
		java.math.BigDecimal bNtotalgpmny = new BigDecimal(0);
		// 最终要与财务单据进行VO对照的合同VO，即本方法返回的VO
		ManageVO finalVO = new ManageVO();
		// 合同状态为生效
		if (BillState.VALIDATE == Integer.parseInt(vo.getParentVO()
				.getAttributeValue("ctflag").toString())) {
			for (int i = 0; i < voItem.length; i++) {
			    ufNtotalgpmny = UFDouble.ZERO_DBL;
			    ufOritaxsummny = UFDouble.ZERO_DBL;
				/*
				 * // 获得本币价税合计 if (null !=
				 * voItem[i].getAttributeValue("natitaxsummny")) { if
				 * (voItem[i].getAttributeValue("natitaxsummny") instanceof
				 * java.math.BigDecimal) { bNatitaxsummny =
				 * (java.math.BigDecimal) voItem[i]
				 * .getAttributeValue("natitaxsummny"); ufNatitaxsummny = new
				 * UFDouble( (java.math.BigDecimal) bNatitaxsummny); } else if
				 * (voItem[i].getAttributeValue("natitaxsummny") instanceof
				 * UFDouble) { ufNatitaxsummny = (UFDouble) voItem[i]
				 * .getAttributeValue("natitaxsummny"); } }
				 */
				// 获得原币价税合计 modified by lirr 2009-7-20 上午10:37:55 原因：改为原币控制
				if (null != voItem[i].getAttributeValue("oritaxsummny")) {
					if (voItem[i].getAttributeValue("oritaxsummny") instanceof java.math.BigDecimal) {
						bOritaxsummny = (java.math.BigDecimal) voItem[i]
								.getAttributeValue("oritaxsummny");
						ufOritaxsummny = new UFDouble(
								(java.math.BigDecimal) bOritaxsummny);
					} else if (voItem[i].getAttributeValue("oritaxsummny") instanceof UFDouble) {
						ufOritaxsummny = (UFDouble) voItem[i]
								.getAttributeValue("oritaxsummny");
					}

				}
				// 累计付款金额
				if (null != voItem[i].getAttributeValue("noritotalgpmny")) {
					if (voItem[i].getAttributeValue("noritotalgpmny") instanceof java.math.BigDecimal) {
						bNtotalgpmny = (java.math.BigDecimal) voItem[i]
								.getAttributeValue("noritotalgpmny");
						ufNtotalgpmny = new UFDouble(
								(java.math.BigDecimal) bNtotalgpmny);
					} else if (voItem[i].getAttributeValue("noritotalgpmny") instanceof UFDouble) {
						ufNtotalgpmny = (UFDouble) voItem[i]
								.getAttributeValue("noritotalgpmny");
					}

				}
				// (ct_b.natitaxsummny-ct_b.ntotalgpmny) > 0,（本币价税合计—累计付款金额）大于0
				if (ufOritaxsummny.compareTo(ufNtotalgpmny) > 0) {
					alItemVOs.add(voItem[i]);

				}
			}
		}
		// 合同状态为其他状态（生效除外）
		else {
			return null;
		}
		// 有符合条件的合同行
		if (alItemVOs.size() > 0) {
			ManageItemVO[] itemVOs = new ManageItemVO[alItemVOs.size()];
			alItemVOs.toArray(itemVOs);
			finalVO.setParentVO(vo.getParentVO());
			finalVO.setChildrenVO(itemVOs);
		} else {
			return null;
		}

		return finalVO;
	}

	/**
	 * 
	 * 单据表体菜单动作监听.
	 * 
	 * @param e
	 *            ufbill.BillEditEvent
	 */
	public boolean onEditAction(int action) {

		if (!((CtBillCardPanel) getCtBillCardPanel()).isLineCardEdit())
			return true;

		boolean isSort = getCtBillCardPanel().getBillTable().isSortEnabled();
		try {
			getCtBillCardPanel().removeActionListener();
			if (isSort)
				getCtBillCardPanel().getBillTable().setSortEnabled(false);
			getCtBillCardPanel().getBillModel().setNeedCalculate(false);

			switch (action) {
			case BillTableLineAction.ADDLINE:
				onAddLine();
				break;
			case BillTableLineAction.INSERTLINE:
				getCtBillCardPanel().insertLine();
				break;
			case BillTableLineAction.DELLINE:
				onDelLine();
				break;
			case BillTableLineAction.COPYLINE:
				getCtBillCardPanel().copyLine();
				break;
			case BillTableLineAction.PASTELINE:
				getCtBillCardPanel().pasteLine();
				break;
			case BillTableLineAction.PASTELINETOTAIL:
				getCtBillCardPanel().pasteLineToTail();
				break;
			case BillTableLineAction.EDITLINE:
				onLineCardEdit();
				break;
			default:
				return true;
			}

		} finally {
			getCtBillCardPanel().addActionListener(this);
			if (isSort)
				getCtBillCardPanel().getBillTable().setSortEnabled(true);
			getCtBillCardPanel().getBillModel().setNeedCalculate(true);
		}

		return false;
	}

	/**
	 * 功能描述:浮动菜单右键功能权限控制
	 * <p>
	 * <b>变更历史：</b>
	 * <p>
	 * <hr>
	 * <p>
	 * 修改日期 2008.9.23
	 * <p>
	 * 修改人 lirr
	 * <p>
	 * 版本 5.5
	 * <p>
	 * 说明：
	 * <ul>
	 * <li>设定更多的右键菜单控制权限
	 * </ul>
	 * 
	 * 修改日期 2009-8-13下午06:49:19 修改人，lirr 修改原因，注释标志：多语情况下空指针
	 */
	protected void rightButtonRightControl() {
		/*
		 * UIMenuItem[] bodyMenuItems = getCtBillCardPanel().getBodyMenuItems();
		 * 
		 * for (int i = 0; i < getCtBillCardPanel().getBodyMenuItems().length;
		 * i++) {
		 * 
		 * String sButtonName = getCtBillCardPanel().getBodyMenuItems()[i]
		 * .getText().toString().trim();
		 * 
		 * ///System.out.println(sButtonName); //根据getButtonTree()得到button ，
		 * 必须用汉字名称 ，qinchao 2009-5-13 if(sButtonName.equals("Add a
		 * row")||sButtonName.equals("增行")){ sButtonName = "增行"; }else
		 * if(sButtonName.equals("Delete a row")||sButtonName.equals("刪行")){
		 * sButtonName = "删行"; }else if(sButtonName.equals("Insert a
		 * row")||sButtonName.equals("插入行")){ sButtonName = "插入行"; }else
		 * if(sButtonName.equals("Copy a row")||sButtonName.equals("複製行")){
		 * sButtonName = "复制行"; }else if(sButtonName.equals("Paste a
		 * row")||sButtonName.equals("粘貼行")){ sButtonName = "粘贴行"; }else
		 * if(sButtonName.equals("Paste a row to the table
		 * tail")||sButtonName.equals("粘貼行到表尾")){ sButtonName = "粘贴行到表尾"; }else
		 * if(sButtonName.equals("Start Card
		 * Edit")||sButtonName.equals("卡片編輯")){ sButtonName = "卡片编辑"; }else
		 * if(sButtonName.equals("Resort rowno")||sButtonName.equals("重排行號")){
		 * sButtonName = "重排行号"; } // modified by lirr 2009-02-23 修改原因：客户化
		 * 如果上按钮没有权限的话 本级按钮的权限为true if (null != getButtonTree().getButton(
		 * getCtBillCardPanel().getBodyMenuItems()[i].getText() .toString()) &&
		 * !(getButtonTree().getButton(
		 * getCtBillCardPanel().getBodyMenuItems()[i]
		 * .getText().toString()).isPower()
		 * ||(getButtonTree().getButton(getCtBillCardPanel().getBodyMenuItems()[i].getText().toString()).getParent().isPower())) ){
		 * 
		 * 
		 * 
		 * if (null != getButtonTree().getButton(sButtonName))
		 * if(!getButtonTree().getButton(sButtonName).getParent().isPower() ||
		 * (getButtonTree().getButton(sButtonName).getParent().isPower() &&
		 * !(getButtonTree().getButton(sButtonName).isPower()))){
		 * bodyMenuItems[i].setEnabled(false); } }
		 */

		if (null != getButtonTree().getButton("增行"))
			if (!getButtonTree().getButton("增行").getParent().isPower()
					|| (getButtonTree().getButton("增行").getParent().isPower() && !(getButtonTree()
							.getButton("增行").isPower()))) {
				getCtBillCardPanel().getAddLineMenuItem().setEnabled(false);
			}
		if (null != getButtonTree().getButton("删行"))
			if (!getButtonTree().getButton("删行").getParent().isPower()
					|| (getButtonTree().getButton("删行").getParent().isPower() && !(getButtonTree()
							.getButton("删行").isPower()))) {
				getCtBillCardPanel().getDelLineMenuItem().setEnabled(false);
			}
		if (null != getButtonTree().getButton("插入行"))
			if (!getButtonTree().getButton("插入行").getParent().isPower()
					|| (getButtonTree().getButton("插入行").getParent().isPower() && !(getButtonTree()
							.getButton("插入行").isPower()))) {
				getCtBillCardPanel().getInsertLineMenuItem().setEnabled(false);
			}
		if (null != getButtonTree().getButton("复制行"))
			if (!getButtonTree().getButton("复制行").getParent().isPower()
					|| (getButtonTree().getButton("复制行").getParent().isPower() && !(getButtonTree()
							.getButton("复制行").isPower()))) {
				getCtBillCardPanel().getCopyLineMenuItem().setEnabled(false);
			}
		if (null != getButtonTree().getButton("粘贴行"))
			if (!getButtonTree().getButton("粘贴行").getParent().isPower()
					|| (getButtonTree().getButton("粘贴行").getParent().isPower() && !(getButtonTree()
							.getButton("粘贴行").isPower()))) {
				getCtBillCardPanel().getPasteLineMenuItem().setEnabled(false);
			}
		if (null != getButtonTree().getButton("粘贴行到表尾"))
			if (!getButtonTree().getButton("粘贴行到表尾").getParent().isPower()
					|| (getButtonTree().getButton("粘贴行到表尾").getParent()
							.isPower() && !(getButtonTree().getButton("粘贴行到表尾")
							.isPower()))) {
				getCtBillCardPanel().getPasteLineToTailMenuItem().setEnabled(
						false);
			}
		if (null != getButtonTree().getButton("卡片编辑"))
			if (!getButtonTree().getButton("卡片编辑").getParent().isPower()
					|| (getButtonTree().getButton("卡片编辑").getParent().isPower() && !(getButtonTree()
							.getButton("卡片编辑").isPower()))) {
				miBoCardEdit.setEnabled(false);
			}
		if (null != getButtonTree().getButton("重排行号"))
			if (!getButtonTree().getButton("重排行号").getParent().isPower()
					|| (getButtonTree().getButton("重排行号").getParent().isPower() && !(getButtonTree()
							.getButton("重排行号").isPower()))) {
				miaddNewRowNo.setEnabled(false);
			}
	}

	protected void execFormularAfterQueryWithoutCache(ManageVO[] arrMangevos) {
		// 存放所有表头vo
		CircularlyAccessibleValueObject[] headVosAll = new CircularlyAccessibleValueObject[arrMangevos.length];
		CircularlyAccessibleValueObject[] tableVOsAll = null;
		CircularlyAccessibleValueObject[] termVOsAll = null;

		ArrayList alTableVOs = new ArrayList(); // 所有表体vo
		ArrayList alTermVOs = new ArrayList(); // 所有条款vo

		int index = 0;

		ExtendManageVO[] arrExtendVO = new ExtendManageVO[arrMangevos.length];
		for (int k = 0; k < arrMangevos.length; k++) {
			arrExtendVO[k] = new ExtendManageVO();

			arrExtendVO[k].setParentVO(arrMangevos[k].getParentVO());
			headVosAll[k] = arrMangevos[k].getParentVO();

			// modified by liuzy 2008-04-17 V5.03查询改造，只返回第一条合同的表体与其他页签
			if (null == arrMangevos[k].getChildrenVO()) {
				// m_vBillVO.add(arrExtendVO[k]);
				continue;
			}
			tableVOsAll = arrMangevos[k].getChildrenVO();
			arrExtendVO[k].setTableVO("table", tableVOsAll);
			for (index = 0; index < tableVOsAll.length; index++)
				alTableVOs.add(tableVOsAll[index]);

			termVOsAll = arrMangevos[k].getTermBb4s();
			arrExtendVO[k].setTableVO("term", termVOsAll);
			if (termVOsAll != null && termVOsAll.length > 0) {
				for (index = 0; index < termVOsAll.length; index++)
					alTermVOs.add(termVOsAll[index]);
			}

			arrExtendVO[k].setTableVO("cost", arrMangevos[k].getExpBb3s());
			arrExtendVO[k].setTableVO("note", arrMangevos[k].getMemoraBb2s());
			arrExtendVO[k]
					.setTableVO("history", arrMangevos[k].getChangeBb1s());
			arrExtendVO[k].setTableVO("exec", arrMangevos[k].getManageExecs());

			// m_vBillVO.add(arrExtendVO[k]);
		}
		// 执行公式
		getCtBillListPanel().getBodyBillModel().execFormulasWithVO(headVosAll,
				getHeadTailFormulas());

		// 合同基本
		tableVOsAll = new CircularlyAccessibleValueObject[alTableVOs.size()];
		alTableVOs.toArray(tableVOsAll);

		nc.ui.scm.pub.FreeVOParse freeVOParse = new nc.ui.scm.pub.FreeVOParse();
		freeVOParse.setFreeVO(tableVOsAll, null, "invid", false);

		// 合同基本执行公式
		getCtBillListPanel().getBodyBillModel().execFormulasWithVO(tableVOsAll,
				getTableFormulas());

		if (alTermVOs.size() > 0) {
			termVOsAll = new CircularlyAccessibleValueObject[alTermVOs.size()];
			alTermVOs.toArray(termVOsAll);
			getCtBillListPanel().getBodyBillModel().execFormulasWithVO(
					termVOsAll, getTermFormulas());
		}

		// 合同费用子表不再执行公式
		// 原因：合同费用子表中的pk_ct_exp非合同费用定义中的pk_ct_expset，二者之间的联系loadformula不能根据这两个键建立联系。
		// shaobing on Jul 4, 2005
		/** ***查询后公式执行结束**** */
	}

	/*
	 * added by lirr 获得财务精度的累计付款总额，在合同变更时要取此值与“累计付款总额”比较，若此值<"累计付款总额" 则不让变更；
	 * 合同保存时将此字段保存到数据库中 since 56 改为原币控制
	 * 获得财务精度的累计原币付款总额，在合同变更时要取此值与“累计本币付款总额”比较，若此值<"累计原币付款总额" 则不让变更；
	 */

	private UFDouble getAraptotalgpamount(ManageVO managevo) {
		UFDouble ufnarapamount = new UFDouble(0);
		if (null != managevo.getChildrenVO()
				&& managevo.getChildrenVO().length > 0) {
			for (int i = 0; i < managevo.getChildrenVO().length; i++) {
				// modified by lirr 2008-12-17 变更时vo里记录了所有的行，统计时应把删除的行去掉
				if (null != managevo.getChildrenVO()[i]
						.getAttributeValue("oritaxsummny")
						&& managevo.getChildrenVO()[i].getStatus() != VOStatus.DELETED)
					/*
					 * if (null != managevo.getChildrenVO()[i]
					 * .getAttributeValue("natitaxsummny") )
					 */
					ufnarapamount = ufnarapamount.add((UFDouble) managevo
							.getChildrenVO()[i]
							.getAttributeValue("oritaxsummny"));
			}
		}
		String currid = managevo.getParentVO().getCurrid();
		return new UFDouble(ufnarapamount.toDouble(), ((Integer) m_hCurrDigitcw
				.get(currid)).intValue());

	}

	/**
	 * 创建人：lirr 创建日期：2008-10-30上午09:47:05 创建原因：重新计算合同行单价和金额，包括原币、本币、辅币。
	 * 
	 * @param e
	 */
	protected void calcCtLinePriceMny(BillEditEvent e) {
		int row = e.getRow();
		// String pk = getCtBillCardPanel().getHeadItem("currid").getValue();
		getCtBillCardPanel().setAutoFiltNullValueRow(false);
		int[] iaDescription = {
				RelationsCal.DISCOUNT_TAX_TYPE_NAME,
				RelationsCal.NUM,
				RelationsCal.TAXRATE,
				RelationsCal.NET_PRICE_ORIGINAL,
				RelationsCal.NET_TAXPRICE_ORIGINAL,
				RelationsCal.MONEY_ORIGINAL,
				RelationsCal.SUMMNY_ORIGINAL,
				RelationsCal.TAX_ORIGINAL,
				RelationsCal.CURRTYPEPk,// 主计量本币价格、金额
				SCMRelationsCal.NET_PRICE_LOCAL,
				SCMRelationsCal.NET_TAXPRICE_LOCAL, SCMRelationsCal.TAX_LOCAL,
				SCMRelationsCal.MONEY_LOCAL, SCMRelationsCal.SUMMNY_LOCAL,
				SCMRelationsCal.BILLDATE, SCMRelationsCal.PK_CORP,
				SCMRelationsCal.EXCHANGE_O_TO_BRATE,
				// added by lirr 2008-12-25
				SCMRelationsCal.CONVERT_RATE, SCMRelationsCal.IS_FIXED_CONVERT,
				SCMRelationsCal.NUM_ASSIST };

		// ADDED by lirr 2008-12-25 判断是否固定换算率
		boolean isFixFlag = false;
		if (null != getCtBillCardPanel().getBodyValueAt(row, "invid")
				&& null != getCtBillCardPanel()
						.getBodyValueAt(row, "astmeasid")) {
			String sInvID = (String) getCtBillCardPanel().getBodyValueAt(row,
					"invid");
			if (null != sInvID) {
				filterMeas(sInvID, "astmeasname", m_sPkCorp);
			}
			/*
			 * if(null == m_voInvMeas.getMeasureRate( (String)
			 * getCtBillCardPanel().getBodyValueAt(row, "invid"), (String)
			 * getCtBillCardPanel().getBodyValueAt(row, "astmeasid"))){ String
			 * sInvID = (String) getCtBillCardPanel().getBodyValueAt(row,
			 * "invid"); if (null != sInvID) { filterMeas(sInvID, "astmeasname",
			 * m_sPkCorp); } }
			 * 
			 * if (null != m_voInvMeas.getMeasureRate( (String)
			 * getCtBillCardPanel().getBodyValueAt(row, "invid"), (String)
			 * getCtBillCardPanel().getBodyValueAt(row, "astmeasid")) && null !=
			 * m_voInvMeas.getMeasureRate( (String)
			 * getCtBillCardPanel().getBodyValueAt(row, "invid"), (String)
			 * getCtBillCardPanel().getBodyValueAt(row,
			 * "astmeasid")).getFixedflag()) isFixFlag =
			 * m_voInvMeas.getMeasureRate( (String)
			 * getCtBillCardPanel().getBodyValueAt(row, "invid"), (String)
			 * getCtBillCardPanel().getBodyValueAt(row,
			 * "astmeasid")).getFixedflag().booleanValue();
			 * 
			 */
			isFixFlag = InvTool.isFixedConvertRate(getCtBillCardPanel()
					.getBodyValueAt(row, "invbasid").toString(),
					(String) getCtBillCardPanel().getBodyValueAt(row,
							"astmeasid"));

		}

		String sFixFlag = null;
		if (isFixFlag)
			sFixFlag = "Y";
		else
			sFixFlag = "N";

		String[] saKeys = { "应税外加", "amount", "taxration", "oriprice",
				"oritaxprice", "orisum", "oritaxsummny", "oritaxmny", "currid",
				"natiprice", "natitaxprice", "natitaxmny", "natisum",
				"natitaxsummny", "subscribedate", "pk_corp", "currrate",
				"transrate", sFixFlag, "astnum" };

		/*
		 * RelationsCal.calculate(getCtBillCardPanel(),e,
		 * getCtBillCardPanel().getBillModel(), iaDescription, saKeys,
		 * ManageItemVO.class.getName());
		 */

		RelationsCal.calculate(row, e.getOldValue(), getCtBillCardPanel(),
				getCalculatePara(null), e.getKey(), iaDescription, saKeys,
				ManageItemVO.class.getName(), ManageHeaderVO.class.getName(),
				null);
	}

	/**
	 * 此处插入方法说明。 功能描述: 输入参数: 返回值: 异常处理: 日期:
	 */

	/**
	 * return int[] 0--含税无税优先 1--调折扣还是调单价 2--是否主辅币核算
	 * 
	 * Integer Prior --- 强制含税或无税优先 取得单价金额算法所需的参数
	 */
	public int[] getCalculatePara(Integer Prior) {
		// 含税优先机制:默认无税优先
		int m_iPrior_Price_TaxPrice = RelationsCalVO.PRICE_PRIOR_TO_TAXPRICE;
		if (sContractPriceRule.equals("无税单价")) {
			m_iPrior_Price_TaxPrice = RelationsCalVO.PRICE_PRIOR_TO_TAXPRICE;
		} else if (sContractPriceRule.equals("含税单价")) {
			m_iPrior_Price_TaxPrice = RelationsCalVO.TAXPRICE_PRIOR_TO_PRICE;
		}

		if (Prior != null)// 强制优先机制
			m_iPrior_Price_TaxPrice = Prior.intValue();

		int[] iaPrior = new int[] { m_iPrior_Price_TaxPrice,
				RelationsCalVO.YES_LOCAL_FRAC };

		return iaPrior;
	}

	/**
	 * 获得来源单据的表头ts和表体ts 作者：lirr 日期：2008-12-23下午04:38:13 修改日期
	 * 2008-12-23下午04:38:13 修改人，lirr 修改原因，注释标志：
	 * 
	 * @param pk
	 *            合同行pk
	 * @return
	 */
	private String[] getSouceBillTs(String pk) {
		String[] retTs = null;
		if (null == m_vBillVOForRef || m_vBillVOForRef.size() <= 0) {
			return null;
		}
		for (ExtendManageVO vo : m_vBillVOForRef) {
			ManageItemVO[] curItemvo = (ManageItemVO[]) vo
					.getTableVO(CTTableCode.BASE);
			if (null != curItemvo && curItemvo.length > 0) {
				for (ManageItemVO item : curItemvo) {
					if (item.getAttributeValue("csourcebillbid").equals(pk)) {
						retTs = new String[2];
						retTs[0] = (String) item
								.getAttributeValue("cupsourcehts");
						// retTs[0] =
						// (String)item.getAttributeValue("cupsourcebts");
						retTs[1] = (String) item
								.getAttributeValue("cupsourcebts");
					}
				}
			}
		}

		return retTs;
	}

	/**
	 * 根据币种获得折本汇率精度 作者：lirr 日期：2008-12-24下午03:45:43 修改日期 2008-12-24下午03:45:43
	 * 修改人，lirr 修改原因，注释标志：
	 * 
	 * @param currID
	 * @return
	 */
	protected int getCurrateDigit(String currID) {
		int currDigit = 5;
		try {
			BusinessCurrencyRateUtil currRateUtil = new BusinessCurrencyRateUtil(
					m_sPkCorp);
			if (null != currRateUtil.getCurrinfoVO(currID, m_sLocalCurrID)
					&& null != currRateUtil.getCurrinfoVO(currID,
							m_sLocalCurrID).getRatedigit()) {
				currDigit = currRateUtil.getCurrinfoVO(currID, m_sLocalCurrID)
						.getRatedigit();
			}

		} catch (Exception e) {
			nc.vo.scm.pub.SCMEnv.out(e);
		}
		return currDigit;
	}

	/**
	 * 收付款按钮后刷新界面 作者：lirr 日期：2008-12-26上午11:00:07 修改日期 2008-12-26上午11:00:07
	 * 修改人，lirr 修改原因，注释标志：
	 */
	private void freshCardVo() {
		String sWhereSQL = " ct.pk_ct_manage = '"
				+ getCtBillCardPanel().getHeadItem("pk_ct_manage").getValue()
				+ "'";
		try {
			ManageVO[] arrMangevos = loadHeadData(m_sPkCorp, sWhereSQL);
			if (null == arrMangevos && arrMangevos.length <= 0) {
				return;
			}
			ManageVO vo = arrMangevos[0];
			ManageItemVO[] voItems = (ManageItemVO[]) vo.getChildrenVO();
			HashMap<String, UFDouble> itemNtotalgpmny = new HashMap<String, UFDouble>(
					voItems.length);

			for (ManageItemVO voItem : voItems) {
				UFDouble mny = new UFDouble(0.0);
				if (null != voItem.getAttributeValue("ntotalgpmny")) {
					mny = (UFDouble) voItem.getAttributeValue("ntotalgpmny");
				}
				itemNtotalgpmny.put(voItem.getPk_ct_manage_b(), mny);
			}
			((ManageHeaderVO) ((ExtendManageVO) m_vBillVO.elementAt(m_iId - 1))
					.getParentVO()).setAttributeValue("naraptotalgpamount", vo
					.getParentVO().getAttributeValue("naraptotalgpamount"));
			((ManageHeaderVO) ((ExtendManageVO) m_vBillVO.elementAt(m_iId - 1))
					.getParentVO()).setAttributeValue("ntotalgpamount", vo
					.getParentVO().getAttributeValue("ntotalgpamount"));

			for (ManageItemVO item : (ManageItemVO[]) ((ExtendManageVO) m_vBillVO
					.elementAt(m_iId - 1)).getTableVO(CTTableCode.BASE)) {
				item.setAttributeValue("ntotalgpmny", itemNtotalgpmny.get(item
						.getPk_ct_manage_b()));
			}

		} catch (Exception e) {
			// 日志异常
			nc.vo.scm.pub.SCMEnv.out(e);
			// 按规范抛出异常
			GenMethod.handleException(this, e.getMessage(), e);
		}
	}

	/**
	 * 获得普通的业务日志VO 参数 作者：lirr 日期：2009-4-21上午10:15:51 修改日期 2009-4-21上午10:15:51
	 * 修改人，lirr 修改原因，注释标志：
	 * 
	 * @return
	 */
	public nc.vo.sm.log.OperatelogVO getNormalOperateLog() {
		nc.ui.pub.ClientEnvironment ce = getClientEnvironment();
		nc.vo.sm.log.OperatelogVO log = new nc.vo.sm.log.OperatelogVO();
		log.setCompanyname(ce.getCorporation().getUnitname());
		log.setEnterip(nc.ui.sm.cmenu.Desktop.getApplet().getParameter(
				"USER_IP"));

		// }

		log.setPKCorp(m_sPkCorp);

		//
		return log;
	}

	/**
	 * 设置表头表头财务原币累计收/付款总额 在合同保存前需要调用 作者：lirr 日期：2009-7-17下午02:29:43 修改日期
	 * 2009-7-17下午02:29:43 修改人，lirr 修改原因，注释标志：
	 * 
	 * @param newManageVO
	 */
	private void setAraptotalgpamount(ManageVO newManageVO) {

		UFDouble ufnarapamount = new UFDouble(0);
		if (null != newManageVO.getChildrenVO()
				&& newManageVO.getChildrenVO().length > 0) {
			for (int i = 0; i < newManageVO.getChildrenVO().length; i++) {
				if (null != newManageVO.getChildrenVO()[i]
						.getAttributeValue("oritaxsummny"))
					ufnarapamount = ufnarapamount.add((UFDouble) newManageVO
							.getChildrenVO()[i]
							.getAttributeValue("oritaxsummny"));

			}
		}
		// 表头财 务原币累计收/付款总额
		newManageVO.getParentVO().setAttributeValue(
				"naraptotalgpamount",
				new UFDouble(ufnarapamount.toDouble(),
						((Integer) m_hCurrDigitcw.get(newManageVO.getParentVO()
								.getCurrid())).intValue()));

	}

	/**
	 * 此处插入方法说明 作者：lirr 日期：2009-8-17上午11:24:12 修改日期 2009-8-17上午11:24:12 修改人，lirr
	 * 修改原因，注释标志：
	 * 
	 * @return
	 */
	public BillTempletVO getBillTempletVO(String billType) {
		if (billTempletVO == null) {
			billTempletVO = BillUIUtil.getDefaultTempletStatic(billType, null,
					m_sPkUser, m_sPkCorp, null, null);
		}
		return billTempletVO;
	}

	public boolean isM_bRateEnable() {
		return m_bRateEnable;
	}

	// 修改时间2009-09-10 修改人:wuweiping 修改原因: 当供应商改变的时候币种是否一致，如果一致折本汇率不可编辑，否则可以编辑
	public void setM_bRateEnable(String currID) {
		if (currID.equals(m_sLocalCurrID)) {
			m_bRateEnable = false;
		} else
			m_bRateEnable = true;
	}

	protected ManageVO getAuditVO(boolean isshowerr, ManageVO mVO) {

		// 操作员日志
		nc.vo.sm.log.OperatelogVO log = getNormalOperateLog();

		ManageVO tempMVO = new ManageVO();
		if (mVO == null)
			mVO = getCurVO();// 拿到当前选中的单据VO
		// modified by liuzy 2007-12-25 打开合同就点审核时报空错
		if (null == mVO) {

			if (!isshowerr)
				return null;
			showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"4020nodes", "UPP4020nodes-000120")/*
														 * @res "没有需要审核的合同"
														 */);
			return null;
		}
		tempMVO = mVO.clone(mVO);
		int ctState = Integer.parseInt(tempMVO.getParentVO().getAttributeValue(
				"ctflag").toString());// 获得合同状态

		// 对处于不同状态的合同进行不同的处理
		if (ctState == BillState.FREE || ctState == BillState.CHECKGOING) {
			if (isshowerr) {
				if (MessageDialog.showYesNoDlg(this, null, nc.ui.ml.NCLangRes
						.getInstance().getStrByID("4020nodes",
								"UPP4020nodes-000051")/*
														 * @res "您是否确定要审核该合同？"
														 */) != MessageDialog.ID_YES)
					return null;
			}
			tempMVO.getParentVO().setAttributeValue("ctflag",
					new Integer(BillState.AUDIT));// 设置合同状态为审核状态

			tempMVO.getParentVO().setAttributeValue("audiid", m_sPkUser);// 设置审核人为当前用户
			tempMVO.getParentVO().setAttributeValue("auditdate",
					getClientEnvironment().getDate());// 设置审核日期为当前日期

			if (ctState == BillState.FREE)
				tempMVO.setOldBillStatus(BillState.FREE);// 设置原单据状态为自由状态
			else if (ctState == BillState.CHECKGOING)
				tempMVO.setOldBillStatus(BillState.CHECKGOING);// 设置原单据状态为正在审核状态
			tempMVO.setBillStatus(BillState.AUDIT);// 设置单据状态为审核状态

			// added by lirr 2009-04-21 业务日志
			tempMVO.setOperatelogVO(log);

			return tempMVO;
		}
		return null;
	}

	protected ManageVO getUnAuditVO(boolean isshowerr, ManageVO mVO) {
		// 操作员日志
		nc.vo.sm.log.OperatelogVO log = getNormalOperateLog();
		// 修改人: wuwp 修改时间:2009-10-14 下午03:31:04 修改原因:合同批生效，不起作用。
		if (null == mVO)
			mVO = getCurVO();// 拿到当前选中的单据VO;
		ManageVO tempMVO = new ManageVO();
		tempMVO = mVO.clone(mVO);
		int ctState = Integer.parseInt(tempMVO.getParentVO().getAttributeValue(
				"ctflag").toString());

		// 对处于不同状态的合同进行不同的处理
		// if (ctState == BillState.AUDIT ) { // 审核状态
		if (ctState == BillState.AUDIT || ctState == BillState.CHECKGOING) { // 审核状态

			if (isshowerr) {
				if (MessageDialog.showYesNoDlg(this, null, nc.ui.ml.NCLangRes
						.getInstance().getStrByID("4020pub",
								"UPP4020pub-000158")/*
													 * @res "是否确定要弃审该合同？"
													 */) != MessageDialog.ID_YES)
					return null;
			}

			tempMVO.getParentVO().setAttributeValue("ctflag",
					new Integer(BillState.AUDIT));

			// 当前操作员
			tempMVO.getParentVO().setAttributeValue("coperatoridnow",
					getOperator());

			tempMVO.getParentVO().setAttributeValue("auditdate",
					getClientEnvironment().getDate());
			if (ctState == BillState.AUDIT)
				tempMVO.setOldBillStatus(BillState.AUDIT);

			tempMVO.setBillStatus(BillState.FREE);
			// added by lirr 2009-04-21 业务日志
			tempMVO.setOperatelogVO(log);
			return tempMVO;

		}

		return null;

	}

	protected ManageVO getValidateVO(boolean isshowerr, ManageVO mVO) {

		if (null == mVO)
			mVO = getCurVO();
		ManageVO tempMVO = new ManageVO();
		tempMVO = mVO.clone(mVO);
		int ctState = Integer.parseInt(tempMVO.getParentVO().getAttributeValue(
				"ctflag").toString());

		// 对处于不同状态的合同进行不同的处理
		if (ctState == BillState.AUDIT) { // 审核状态
			if (isshowerr) {
				String sMessage = nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"4020pub", "UPP4020pub-000090")/* @res "是否确定要使该合同生效？" */;

				if (MessageDialog.showYesNoDlg(this, null, sMessage) != MessageDialog.ID_YES)
					return null;
			}
			tempMVO.getParentVO().setAttributeValue("ctflag",
					new Integer(BillState.VALIDATE));

			// 当前操作员
			tempMVO.getParentVO().setAttributeValue("coperatoridnow",
					getOperator());

			tempMVO.getParentVO().setAttributeValue("actualvalidate",
					getClientEnvironment().getDate());

			tempMVO.setOldBillStatus(BillState.AUDIT);
			tempMVO.setBillStatus(BillState.VALIDATE);

			UFDate UFValDate = new UFDate(mVO.getParentVO().getAttributeValue(
					"valdate").toString());// 计划合同生效时间

			// if (UFValDate.compareTo(ClientEnvironment.getInstance()
			// .getDate()) != 0) {
			// return null;
			// }
			m_sExecFlag = CTExecFlow.VALIDATE; // "实际生效";
			// added by lirr 2009-9-14下午02:45:29 设置执行过程的动作及原因
			CTTool.setExecReason(tempMVO, this);
			return tempMVO;
		}
		return null;
	}

	protected ManageVO getSendAuditVO(boolean isshowerr, ManageVO mVO) {

		if (mVO == null)
			mVO = getCurBillVO();
		// added by lirr 2008-10-28 送审后合同状态改为“送审中”
		ManageVO tempMVO = new ManageVO();
		tempMVO = mVO.clone(mVO);
		String sId = ((ManageHeaderVO) tempMVO.getParentVO()).getPk_ct_manage();
		// 登陆人不是制单人不送审 added by lirr 2009-02-10
		if (!((ManageHeaderVO) tempMVO.getParentVO()).getOperid().equals(
				m_sPkUser)) {
			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"SCMCOMMON", "UPPSCMCommon-000023")/*
														 * @res "不需送审"
														 */);
			return null;
		}
		// 判断是否保留最初制单人 added by lirr 2009-02-10
		if (m_isSaveInitOper.booleanValue() == false) {
			((ManageHeaderVO) tempMVO.getParentVO()).setOperid(m_sPkUser);
		}
		tempMVO.getParentVO().setAttributeValue("audiid", null);
		tempMVO.getParentVO().setAttributeValue("auditdate", null);
		tempMVO.getParentVO().setAttributeValue("ctflag",
				new Integer(BillState.CHECKGOING));
		tempMVO.getParentVO()
				.setAttributeValue("coperatoridnow", getOperator());
		// added by lirr 2009-7-9下午04:27:52 设置单据类型 送审时使用
		tempMVO.setBillType(m_sBillType);
		tempMVO.setOldBillStatus(BillState.FREE);
		tempMVO.setBillStatus(BillState.CHECKGOING);

		return tempMVO;
	}

	public ManageVO[] getBillVOForAction(int[] iaSeleRows, String sAction) {
		if (iaSeleRows == null || iaSeleRows.length <= 0)
			return null;
		// 合同单据VO
		ManageVO tempVO = null;
		// 当前单据下标
		int iIdtemp = m_iId;
		// 执行过程
		String stemp = null;
		// 错误数量
		int errcount = 0;

		ArrayList<ManageVO> listbillvo = new ArrayList<ManageVO>();
		for (int i = 0, j = iaSeleRows.length; i < j; i++) {
			stemp = null;
			tempVO = null;
			m_iId = iaSeleRows[i] + 1;
			if ("审批".equals(sAction))
				tempVO = getAuditVO(false, null);
			else if ("弃审".equals(sAction))
				tempVO = getUnAuditVO(false, null);
			else if ("生效".equals(sAction))
				tempVO = getValidateVO(false, null);
			else if ("送审".equals(sAction))
				tempVO = getSendAuditVO(false, null);
			if (tempVO == null)
				continue;
			listbillvo.add(tempVO);
		}
		if (listbillvo == null || listbillvo.size() <= 0)
			return null;
		return listbillvo.toArray(new ManageVO[listbillvo.size()]);
	}

	public void onBatchAction(int[] iaSeleRows, String sAction)
			throws BusinessException {
		if (iaSeleRows == null || iaSeleRows.length <= 0 || sAction == null)
			return;

		ManageVO[] billvos = getBillVOForAction(iaSeleRows, sAction);
		if (billvos == null)
			return;
		BatchActionHelp help = new BatchActionHelp(this, m_sBillType,
				m_sPkUser, sAction);

		help.startBatchAction(billvos);
		showWarningMessage(help.getResultMsg());
		onFresh(true);
	}

	/**
	 * 查看审批流状态
	 * 
	 * @author dgq
	 * @param null
	 * @since 5.01 2006.12.27 修改日期 2009-9-21上午09:47:44 修改人，lirr 修改原因
	 *        三种合同都放在此处，注释标志：
	 */
	private void onFlowStatus() {
		if (m_bIsCard) {// 卡片状态
			String pk = getCtBillCardPanel().getHeadItem("pk_ct_manage")
					.getValue();
			if (!pk.equals("") && pk.length() > 0) {
				try {
					FlowStateDlg approvestatedlg = new FlowStateDlg(this,
							m_sBillType, pk);
					approvestatedlg.showModal();
				} catch (Exception e) {

				}
			} else {// 列表状态
				showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"SCMCOMMON", "UPPSCMCommon-000199")/* @res "请选择单据" */);
			}
		} else {
			if (m_iId > 0) {
				ExtendManageVO mVO = (ExtendManageVO) m_vBillVO
						.elementAt(m_iId - 1);
				String pk = (String) ((ManageHeaderVO) mVO.getParentVO())
						.getAttributeValue("pk_ct_manage");
				if (!pk.equals("") && pk.length() > 0) {
					try {
						FlowStateDlg approvestatedlg = new FlowStateDlg(this,
								m_sBillType, pk);
						approvestatedlg.showModal();
					} catch (Exception e) {

					}
				} else {
					showErrorMessage(nc.ui.ml.NCLangRes.getInstance()
							.getStrByID("SCMCOMMON", "UPPSCMCommon-000199")/*
																			 * @res
																			 * "请选择单据"
																			 */);
				}
			}
		}
	}
	
/**
 * 此处插入方法说明 粘贴行时清除执行的一些信息
 * 作者：lirr
 * 日期：2009-11-7下午03:48:16
 * 修改日期 2009-11-7下午03:48:16 修改人，lirr 修改原因，注释标志：
	@param nowRow 当前行，
	@param rowLen 要插入的列长度
 */
private void clearExecInfo (int nowRow, int rowLen){
	for (int i = (nowRow - rowLen); i < nowRow; i++) {
		getCtBillCardPanel().setBodyValueAt(null, i, "ntotalshgpmny"); // 累计应收/付款金额
		getCtBillCardPanel().setBodyValueAt(null, i, "ntotalgpmny"); // 累计收付款金额
		getCtBillCardPanel().setBodyValueAt(null, i, "csourcebillhid");// 来源单据主表ID
		getCtBillCardPanel().setBodyValueAt(null, i, "csourcebillbid");// 来源单据子表行ID
		getCtBillCardPanel().setBodyValueAt(null, i, "csourcebilltype");// 来源单据类型
		getCtBillCardPanel().setBodyValueAt(null, i, "csourcebillcode");// 来源单据号
		getCtBillCardPanel().setBodyValueAt(null, i, "csourcerowno");// 来源单据行号
		// added by lirr 2009-11-30下午01:36:31 来源单据子子表行ID
		getCtBillCardPanel().setBodyValueAt(null, i, "csourcebillbbid");// 来源单据行号
		
		getCtBillCardPanel().setBodyValueAt(null, i, "ordprice"); //
		getCtBillCardPanel().setBodyValueAt(null, i, "ordnum");// 
		getCtBillCardPanel().setBodyValueAt(null, i, "ordsum");// 
		getCtBillCardPanel().setBodyValueAt(null, i, "ts");// 
		getCtBillCardPanel().setBodyValueAt(null, i, "noritotalgpmny"); //
		getCtBillCardPanel().setBodyValueAt(null, i, "noritotalshgpmny");// 
		getCtBillCardPanel().setBodyValueAt(null, i, "pk_ct_manage");
		getCtBillCardPanel().setBodyValueAt(null, i, "pk_ct_manage_b");
	}
}
/**
 * 方法功能描述：简要描述本方法的功能。谢阳要求 复制整单时来源信息要清掉 复制行时56开始就清掉了
 * <p>
 * <b>examples:</b>
 * <p>
 * 使用示例
 * <p>
 * <b>参数说明</b>
 * @param item
 * <p>
 * @author lirr
 * @time 2009-11-30 下午01:35:51
 */
private void clearSouceInfo(ManageItemVO item){
    item.setAttributeValue("csourcebillhid", null);
    item.setAttributeValue("csourcebillbid", null);
    item.setAttributeValue("csourcebilltype", null);
    item.setAttributeValue("csourcebillcode", null);
    item.setAttributeValue("csourcerowno", null);
    item.setAttributeValue("csourcebillbbid", null);
}
/**
 * 方法功能描述：简要描述本方法的功能。复制有条款的合同时合同条款的TS应清除
 * <p>
 * <b>examples:</b>
 * <p>
 * 使用示例
 * <p>
 * <b>参数说明</b>
 * <p>
 * @author lirr
 * @time 2009-12-2 上午10:11:53
 */
private void clearTermsTs(){
    for (int i = 0; i <m_copyedTermVOs.length; i++) {
      m_copyedTermVOs[i].setAttributeValue("ts", null);
     }
   }
}