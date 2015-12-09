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
 * ����˵���� ���ߣ�����
 * 
 * @version ����޸�����(2002-8-28 11:33:34) �޸��ˣ��۱� �޸�ʱ�䣺2004-12-08 15:22
 *          �޸�˵�����ڴ�ӡ�����ӶԴ�ӡ�����Ŀ���ʵ�֡�
 */
public abstract class CtBaseUI extends ToftPanel implements
		TableColumnModelListener, ItemListener, ChangeListener,
		ListSelectionListener, ICheckRetVO, BillEditListener,
		BillEditListener2, BillSortListener, AlreadyAfterEditListener,
		BillBodyMenuListener, java.awt.event.MouseListener,
		BillModelCellEditableController, nc.ui.scm.pub.bill.IBillExtendFun,
		IBillRelaSortListener, IBillModelSortPrepareListener, ILinkApprove, // �������
		// ����������
		ILinkQuery, // ������ѯ ����ѯʹ�ã�
		ILinkMaintain, // ������ѯ ���������Ƶ���ʱʹ�ã�
		ILinkAdd,// �۸�������������������ʹ��
		BillActionListener

// IBillModelRowStateChangeEventListener

{
	// �����ͬ�۸����ȹ������
	protected String sContractPriceRule = "";

	protected ButtonTree btTreeBase = null;// ��ͬ����ҳǩbuttonTree

	protected ButtonObject[] m_aryButtonGroup = null;

	// �����б�panel
	protected ScmBillListPanel m_BillListPanel = null;

	// ���ݿ�Ƭpanel
	protected ScmBillCardPanel m_BillCardPanel = null;

	// ��ѯ�Ի���
	protected CTBillQueryConditionDlgNew m_QueryConditionDlg = null;

	// ������ò���
	protected UIRefPane m_RefExpPane = null;

	protected UIRefCellEditor m_RefExpCellE = null;

	// �����������
	protected UIRefPane m_RefTermPane = null;

	protected UIRefCellEditor m_RefTermCellE = null;

	// ������Ա����
	protected UIRefPane m_RefPersonsPane = null;

	protected UIRefCellEditor m_RefPersonsCellE = null;

	// ���嵥�ݵĺ�ͬ���Ͳ���
	protected UIRefPane m_RefCtTypeBillPane = null;

	protected UIRefCellEditor m_RefCtTypeBillCellE = null;

	// �����ѯʱ�ĺ�ͬ���Ͳ���
	protected UIRefPane m_RefCtTypeQryPane = null;

	protected UIRefCellEditor m_RefCtTypeQryCellE = null;

	// ��ͬ����״̬��־
	protected int m_iBillState = 0;

	// ����ҳǩ����״̬��־
	protected int m_iTbState = 0;

	// ��ִͬ�й���flag
	protected String m_sExecFlag = null;

	// ��ʱ�洢����Table�е�VO���к�
	protected Vector m_vTableVO = null;

	// ��˾����
	protected String m_sPkCorp = null;

	// �û���
	protected String m_sUserName = null;

	// �û�����
	protected String m_sPkUser = null;

	// ϵͳ����
	protected UFDate m_UFToday = null;

	// ������
	protected String m_sNodeCode = null;

	// ��������
	protected String m_sBillType = null;

	// ��ͬ�������
	protected int m_iCtType = 0;

	// ��ʱ�洢����
	protected Vector<ExtendManageVO> m_vBillVO = null;

	// m_vListVO�е�ǰ�����±�
	protected int m_iId = 0;

	// ��ǰm_vListVO�е��ݵ�����
	protected int m_iElementsNum = 0;

	// ��־��ǰ������ҳǩ
	protected int m_iTabbedFlag = 0;

	// ������һ�����ҳǩ��Index
	protected int m_iTabbedOldFlag = 0;

	// ��־ҳǩTable�Ƿ���Ҫ���µ�������
	protected boolean[] m_bIsNeedReInit = null;

	// ��־������Card��ʽ,����List��ʽ
	protected boolean m_bIsCard = true;

	// ��־�Ƿ��ǲ�ѯ���һ�ε������Ƭ����ť
	protected boolean m_bIsFirstClick = false;

	// ��־��ͬ�Ƿ�ɱ��
	protected boolean m_bChangeFlag = false;

	// ��־�Ƿ����
	protected boolean m_bErrFlag = false;

	// ��־�Ƿ���Ҫ����stateChanged()����
	protected boolean m_bNeedChange = true;

	// �����ʼ����ԱWhere�Ӿ�
	protected String m_sPerWhereSql = null;

	// ��������ģʽ
	protected boolean m_bCurrArithMode = true;

	// ��Ź�˾���ֵľ���[ҵ�񾫶�]
	protected Hashtable m_hCurrDigit = null;

	// ����С������[ҵ�񾫶�]
	protected int m_iMainCurrDigit = 2;

	// ����С��λ
	protected int m_iPriceDigit = 2;

	// ����С��λ
	protected int m_iAmountDigit = 2;

	// protected int[] m_iRateDigit = null;
	protected int m_iRateDigit;

	// //����С������
	// protected int m_iRateDigit= 4;
	// Ĭ�ϻ���
	protected UFDouble m_UFCurrRate = null;

	// ���ұ���ID
	protected String m_sLocalCurrID = null;

	// ���ֶ�Ӧ�Ļ���
	protected Hashtable m_hRateDigit = null;

	// ������
	protected UFDouble m_UFTransRate = null;

	// ��ӡʵ��
	protected nc.ui.pub.print.PrintEntry m_print = null;

	// �Ƿ��ڳ�
	protected UFBoolean m_UFbIfEarly = null;

	// �����������Ƽ����ݴ�������
	protected java.lang.String sCtBillCodeKeyName;

	protected java.lang.String sCtPrimaryKeyName;

	protected boolean bCalType = false; // ��Ϊ �����Һ��� ,��Ϊ �����Һ���

	// ���ҶԱ��ҵ�����ģʽ
	boolean bFracmode = true;

	// business currency rate
	protected BusinessCurrencyRateUtil currArith = null;

	// added by lirr 2008-12-24
	protected BusinessCurrencyRateUtil currRateUtil = null;

	protected boolean m_bRateEnable = false;

	// �۱��۸�����
	protected UFDouble m_dRate = null;

	// ������С��λ`
	protected int m_iFracAmountDigit = 2;

	// ����С������
	protected int m_iFracCurrDigit = 2;

	// ���ұ���ID
	protected String m_sFracCurrID = null;

	// ��������������
	// protected InvMeasRate m_voInvMeas = new InvMeasRate();

	// ��ʾ��ͬ�����ʷ�ĶԻ���
	private CtHistoryDlg m_dlgHistory = null;

	// ��Ź�˾���ֵľ���[���񾫶�]
	protected Hashtable m_hCurrDigitcw = null;

	// ��¼�Ѿ���ѯ���ĺ�ͬ�����ʷ����onHistory�г�ʼ����
	private Hashtable m_htChanged = null;

	// �����ʾ���
	protected int m_iHslDigit = 2;

	// ����С������[���񾫶�]
	protected int m_iMainCurrDigitcw = 2;

	// v3.0�Ƿ��Զ��������,Ĭ�����Զ�.
	private boolean m_isAutoSendApprove = true;

	// ��¼��ѯ��Ĳ�ѯ�������Ա�ˢ�¡�
	private String m_sQryCondition = null;

	// ʱ����
	nc.vo.ct.pub.Timer m_timer = new nc.vo.ct.pub.Timer();

	// ����ҳǩ�Ķ�ǰ��VO���Ա����ʱ�ָ�[��ҳǩ�༭ʱ����]
	ManageVO m_voBeforeChange = null;

	SCMMutiTabPrintDataInterface m_dataSource = null;

	// �Ƿ�������Ƶ���
	protected UFBoolean m_isSaveInitOper = new UFBoolean(true); // Ĭ��Ϊ��

	// �б��µ���������VO
	protected ArrayList m_alAllVOs = new ArrayList();

	// Ŀǰ��ͬ����ģ����ֻ�����ĸ�������loadformula.
	// Jun 17, 2005 by Shawbing.
	protected String[] m_headTailFormulas = null; // ����ģ���б�ͷ/��β��ʽ

	protected String[] m_tableFormulas = null; // ����ģ���б���ĺ�ͬ������ʽ

	protected String[] m_termFormulas = null; // ����ģ���б���ĺ�ͬ���ʽ

	protected String[] m_costFormulas = null; // ����ģ���б���ĺ�ͬ���ù�ʽ

	// �ɹ���ͬͨ������������ͬ,��ԭ���ĺ�ͬ���������ҲҪ���Ƶ��µĺ�ͬ��
	// v31sp1����
	// ʵ�֣�����m_copyedTermVOs��¼����ʱ��ͬ���������
	// ��ͬ������������m_copyedTermVOs��Ϊ�գ����Զ���ת��ͬ����ҳǩ
	// �۱� 2005-08-26
	private TermBb4VO[] m_copyedTermVOs = null;

	// �Ƿ�Ϊ������ add by liuzy 2007-04-17
	protected boolean isGlobelBatch = false;

	// createExec������ִ���Ƿ�ͨ��
	protected boolean isExecPass = true;

	// added by lirr 2008-08-12 ��ͬ����ҳǩ ���塰��Ƭ�༭����ť
	protected UIMenuItem miBoCardEdit = new UIMenuItem(nc.ui.ml.NCLangRes
			.getInstance().getStrByID("common", "SCMCOMMON000000267"));

	// added by lirr 2008-08-12 ��ͬ����ҳǩ ���塰�����кš���ť
	protected UIMenuItem miaddNewRowNo = new UIMenuItem(nc.ui.ml.NCLangRes
			.getInstance().getStrByID("SCMCOMMON", "UPP4008bill-000551"));

	// added by lirr 2008-07-23 �Ƿ������������(�빺�����۸�������)���ɺ�ͬ ��trueΪ����
	protected boolean m_bAddFromBillFlag = false;

	// ��ʱ�洢����
	protected Vector<ExtendManageVO> m_vBillVOForRef = null;

	// ��ʱ�洢ת��ǰm_vBillVO�еĵ���
	protected Vector<ExtendManageVO> m_vBillVOBeforTran = null;

	// ��������λ
	protected static HashMap m_hmapCTDefaultAssUnit = null;

	// ���۸�������λ
	protected static HashMap m_hmapCTSoDefaultAssUnit = null;

	private ScmCurrLocRateBizDecimalListener scmCurrRateDigit;

	protected ScmCurrMnyBizDecimalListener scmMnyRateDigit;

	// added by lirr 2009-8-17����11:25:23 ģ��vo���� ����������
	private BillTempletVO billTempletVO = null;

	/**
	 * CtBaseUI ������ע�⡣
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
	 * �༭���¼��� �������ڣ�(2001-3-23 2:02:27)
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

			if (strColName.equals("astmeasname")) { // ����λ
				afterAstunitEdit(e);
			} else if (strColName.equals("vfree0")) { // ������
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
			} else if (strColName.startsWith("def")) {// �Զ������zhy
				if (pos == 0) {// ��ͷ
					String sVdefPkKey = "pk_defdoc"
							+ strColName.substring("def".length());
					DefSetTool.afterEditHead(
							getCtBillCardPanel().getBillData(), strColName,
							sVdefPkKey);
				} else if (pos == 1) {// ����
					String sVdefPkKey = "pk_defdoc"
							+ strColName.substring("def".length());

					// ���ݱ���ʹ�ã�afterEditBody(BillModel billModel, int iRow,String
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
	 * ����˵�������ݺ�ͬ���͵Ĵ�����Ʒ�ʽȷ������Ĵ������ʹ����������ֶ��Ƿ�ɱ༭ �������ڣ�(2001-3-23 2:02:27)
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
									"UPP4020pub-000026")/* @res "����ѡ���ͬ����" */);
					return false;
				}

				// ���ݺ�ͬ���͵Ĵ�����Ʒ�ʽ���ô������ʹ����������Ƿ�ɱ༭
				int iInvctlstyle = Integer.parseInt(oRefValue.toString());

				// �����ͬ���͵Ĵ�����Ʒ�ʽΪ���
				if (iInvctlstyle == 0) {
					getCtBillCardPanel().getBodyItem("invclasscode")
							.setEnabled(false);
					getCtBillCardPanel().getBodyItem("invcode")
							.setEnabled(true);

					// ���ô��������룬�����������Ϊ��
					getCtBillCardPanel().setBodyValueAt(null, iRow,
							"invclasscode");
					getCtBillCardPanel().setBodyValueAt(null, iRow,
							"invclassname");
					getCtBillCardPanel().setBodyValueAt(null, iRow, "invclid");

					// ���ϴ�ѡ�еĴ�����
					// UIRefPane refInv = (UIRefPane) getCtBillCardPanel()
					// .getBodyItem("invcode").getComponent();
					// refInv.setPK(null);
					// refInv.getRefModel().setSelectedData(null);
				}

				// �����ͬ���͵Ĵ�����Ʒ�ʽΪ�������
				else if (iInvctlstyle == 1) {
					getCtBillCardPanel().getBodyItem("invclasscode")
							.setEnabled(true);
					getCtBillCardPanel().getBodyItem("invcode").setEnabled(
							false);
					// ���ô�����룬�������,�ͺţ��������,������λΪ��
					getCtBillCardPanel().setBodyValueAt(null, iRow, "invcode");
					getCtBillCardPanel().setBodyValueAt(null, iRow, "invname");
					getCtBillCardPanel().setBodyValueAt(null, iRow, "invid");
					getCtBillCardPanel().setBodyValueAt(null, iRow, "spec");
					getCtBillCardPanel().setBodyValueAt(null, iRow, "mod");

					getCtBillCardPanel().setBodyValueAt(null, iRow, "measname");

				}
				// �����ͬ���͵Ĵ�����Ʒ�ʽΪ�ա����������ʹ��������Ϊ�գ����ɱ༭��
				else if (iInvctlstyle == 2) {
					getCtBillCardPanel().getBodyItem("invclasscode")
							.setEnabled(false);
					getCtBillCardPanel().getBodyItem("invcode").setEnabled(
							false);
					// ���ô��������룬�����������Ϊ��
					getCtBillCardPanel().setBodyValueAt(null, iRow,
							"invclasscode");
					getCtBillCardPanel().setBodyValueAt(null, iRow,
							"invclassname");
					getCtBillCardPanel().setBodyValueAt(null, iRow, "invclid");
					// ���ô�����룬�������,�ͺţ��������,������λΪ��
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
									"UPP4020pub-000026")/* @res "����ѡ���ͬ����" */);
					return false;
				}

				// ���ݺ�ͬ���͵Ĵ�����Ʒ�ʽ���ô������ʹ����������Ƿ�ɱ༭
				int iInvctlstyle = Integer.parseInt(oRefValue.toString());

				// ��ͬ���͵Ĵ�����Ʒ�ʽΪ��ͬ���
				if (iInvctlstyle == 0) {
					getCtBillCardPanel().getBodyItem("invclasscode")
							.setEnabled(false);
					getCtBillCardPanel().getBodyItem("invcode")
							.setEnabled(true);
					// ���ô��������룬�����������Ϊ��
					getCtBillCardPanel().setBodyValueAt(null, iRow,
							"invclasscode");
					getCtBillCardPanel().setBodyValueAt(null, iRow,
							"invclassname");
					getCtBillCardPanel().setBodyValueAt(null, iRow, "invclid");

				}
				// ��ͬ���͵Ĵ�����Ʒ�ʽΪ�������
				else if (iInvctlstyle == 1) {
					getCtBillCardPanel().getBodyItem("invclasscode")
							.setEnabled(true);
					getCtBillCardPanel().getBodyItem("invcode").setEnabled(
							false);
					// ���ô�����룬�������,�ͺţ��������,������λΪ��
					getCtBillCardPanel().setBodyValueAt(null, iRow, "invcode");
					getCtBillCardPanel().setBodyValueAt(null, iRow, "invname");
					getCtBillCardPanel().setBodyValueAt(null, iRow, "invid");
					getCtBillCardPanel().setBodyValueAt(null, iRow, "spec");
					getCtBillCardPanel().setBodyValueAt(null, iRow, "mod");
					// getCtBillCardPanel().setBodyValueAt(null, iRow,
					// "amount");
					getCtBillCardPanel().setBodyValueAt(null, iRow, "measname");

				}
				// �����ͬ���͵Ĵ�����Ʒ�ʽΪ�ա����������ʹ��������Ϊ�գ����ɱ༭��
				else if (iInvctlstyle == 2) {
					getCtBillCardPanel().getBodyItem("invclasscode")
							.setEnabled(false);
					getCtBillCardPanel().getBodyItem("invcode").setEnabled(
							false);
					// ���ô��������룬�����������Ϊ��
					getCtBillCardPanel().setBodyValueAt(null, iRow,
							"invclasscode");
					getCtBillCardPanel().setBodyValueAt(null, iRow,
							"invclassname");
					getCtBillCardPanel().setBodyValueAt(null, iRow, "invclid");
					// ���ô�����룬�������,�ͺţ��������,������λΪ��
					getCtBillCardPanel().setBodyValueAt(null, iRow, "invcode");
					getCtBillCardPanel().setBodyValueAt(null, iRow, "invname");
					getCtBillCardPanel().setBodyValueAt(null, iRow, "invid");
					getCtBillCardPanel().setBodyValueAt(null, iRow, "spec");
					getCtBillCardPanel().setBodyValueAt(null, iRow, "mod");

					getCtBillCardPanel().setBodyValueAt(null, iRow, "measname");
				}

			} else if (e.getKey().equals("vfree0")) {
				// �����������
				String sMangId = (String) getCtBillCardPanel().getBodyValueAt(
						iRow, "invid");
				if (sMangId == null)
					return false;
				return beforeEditBodyInvFreeItem(e, sMangId);
			}

			else if (e.getKey().equals("vbatchcode")) {
				// �����������
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
					// added by lirr 2009-05-25 �Զ���������޷�ɾ��
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
	 * ���ܣ� �༭ǰ���� �ú�����Ҫ���ڴ���㿪���������ʱ��һЩ�༭����. �۱� 2005-08-17 @parameter: e
	 * BillEditEvent ��׽����BillEditEvent�¼� sMangId String �����������
	 */
	private boolean beforeEditBodyInvFreeItem(BillEditEvent e, String sMangId) {

		int iRow = e.getRow();
		if (iRow < 0) {
			return false;
		}

		if (InvTool.isFreeMngt(sMangId)) {

			// ������������ϢVO
			// ������ٰ��� ���ID���������, �������, ������, ����ͺ�,�Ƿ����������
			InvVO invVO = new InvVO();

			// Ϊ���������
			invVO.setIsFreeItemMgt(new Integer(1));
			// �������ID
			invVO.setCinventoryid(sMangId);

			// CODE �������
			Object strTemp = getCtBillCardPanel().getBodyValueAt(iRow,
					"invcode");
			invVO
					.setCinventorycode(strTemp == null ? null : strTemp
							.toString());

			// NAME �������
			strTemp = getCtBillCardPanel().getBodyValueAt(iRow, "invname");
			invVO.setInvname(strTemp == null ? null : strTemp.toString());

			// SPEC ���
			strTemp = getCtBillCardPanel().getBodyValueAt(iRow, "spec");
			invVO.setInvspec(strTemp == null ? null : strTemp.toString());

			// TYPE �ͺ�
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
			// ����FreeVO
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
	 * �иı��¼��� �������ڣ�(2001-3-23 2:02:27)
	 * 
	 * @param e
	 *            ufbill.BillEditEvent
	 */
	public void bodyRowChange(nc.ui.pub.bill.BillEditEvent e) {

	}

	/**
	 * ����˵��������ǰ��� �������ڣ�(2002-4-28 10:57:38)
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
														 * "��ǰ��ͬ������������������Ϊ�ջ��㣡"
														 */;
			}

			// added by lirr 2009-01-08 ί��ģ��û������ʱ ���ܹ�ѡ���Ƿ�ί�⡱
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
															 * "ί��ģ��û���������ɹ���ͬ������ί�������"
															 */;
					MessageDialog.showErrorDlg(this, null, sMessage);
					return false;
				}
			}

			// ���ݺ�ͬ�����ж�
			int iInvctlstyle = Integer
					.parseInt(((UIRefPane) getCtBillCardPanel().getHeadItem(
							"ct_type").getComponent()).getRefValue(
							"ninvctlstyle").toString());

			int iDatactlstyle = Integer
					.parseInt(((UIRefPane) getCtBillCardPanel().getHeadItem(
							"ct_type").getComponent()).getRefValue(
							"ndatactlstyle").toString());

			// modify by liuzy 2007-04-27
			// �޸�ԭ��:��ͬ��������޸�,ͬʱ����������,��ɾ��ԭ���,���汨��
			// ����ԭ��:ԭ����û��У��VO״̬
			// ManageItemVO[] bodyVO = (ManageItemVO[])
			// manageVO.getChildrenVO();
			// ����״̬���VO����
			ManageItemVO[] bodyVO = null;
			// δ����״̬��VO����
			ManageItemVO[] allBodyVO = (ManageItemVO[]) manageVO
					.getChildrenVO();

			// ���˵�״̬ΪDelete��VO
			if (allBodyVO != null && allBodyVO.length > 0) {
				// ȷ�����˺�����鳤��
				int len = 0;
				for (int i = 0, j = allBodyVO.length; i < j; i++) {
					if (allBodyVO[i].getStatus() != nc.vo.pub.VOStatus.DELETED)
						len++;
				}

				bodyVO = new ManageItemVO[len];
				// ����������
				int k = 0;// �������±�
				for (int i = 0; i < len; i++) {
					if (allBodyVO[i].getStatus() != nc.vo.pub.VOStatus.DELETED) {
						bodyVO[k] = allBodyVO[i];
						k++;
					}
				}
			}

			// �жϺ�ͬ�Ƿ��д��Э��
			if (bodyVO.length == 0 || bodyVO == null) {
				sMessage = nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"4020pub", "UPP4020pub-000027")/* @res "ǩ���ĺ�ͬ����û�д��Э�飡" */;
				MessageDialog.showErrorDlg(this, null, sMessage);
				return false;
			}

			// �ж������������������λ�Ƿ����� added by lirr 2009-01-06
			// added by lirr 2009-9-22����10:25:24 һ�μ��ػ���
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
									.getAttributeValue("crowno") })/* ��{0}�д��Ϊ�����������������λ���Ʋ���Ϊ�� */;
					MessageDialog.showErrorDlg(this, null, sMessage);
					return false;
				}
			}

			Vector vID = new Vector();
			switch (iInvctlstyle) { // ���Ʒ�ʽΪ���
			case 0:
				for (int i = 0; i < bodyVO.length; i++) {
					if (bodyVO[i].getInvid() == null
							|| bodyVO[i].getInvid().toString().length() == 0) {
						sMessage = nc.ui.ml.NCLangRes.getInstance().getStrByID(
								"4020pub", "UPP4020pub-000028")/*
																 * @res
																 * "�������Ϊ�գ�"
																 */;
						MessageDialog.showErrorDlg(this, null, sMessage);
						return false;
					}
					if (bodyVO[i].getTaxration() == null
							|| bodyVO[i].getTaxration().toString().length() <= 0) {
						sMessage = nc.ui.ml.NCLangRes.getInstance().getStrByID(
								"4020pub", "UPP4020pub-000029")/*
																 * @res
																 * "˰�ʲ���Ϊ�գ�"
																 */;
						MessageDialog.showErrorDlg(this, null, sMessage);
						return false;
					}
					vID.addElement(bodyVO[i].getInvid());
				}

				// ��ʱȥ������Ƿ��ظ����жϣ����ں�̨���
				// �жϴ��ID�Ƿ��ظ�
				// for (int i= 0; i < bodyVO.length; i++)
				// {
				// if (isRepeatCode(vID, bodyVO[i].getInvid(), "Manage"))
				// {
				// MessageDialog.showWarningDlg(this, "�������", "���������ظ��Ĵ��");
				// showHintMessage("�������");
				// return false;
				// }
				// }

				break;
			// ���Ʒ�ʽΪ�������
			case 1:

				for (int i = 0; i < bodyVO.length; i++) {

					if (bodyVO[i].getInvclid() == null
							|| bodyVO[i].getInvclid().toString().length() == 0) {
						sMessage = nc.ui.ml.NCLangRes.getInstance().getStrByID(
								"4020pub", "UPP4020pub-000030")/*
																 * @res
																 * "������಻��Ϊ�գ�"
																 */;
						MessageDialog.showErrorDlg(this, null, sMessage);
						return false;
					}

					vID.addElement(bodyVO[i].getInvclid());
				}

				// //�жϴ������ID�Ƿ��ظ�
				// for (int i= 0; i < bodyVO.length; i++)
				// {
				// if (isRepeatCode(vID, bodyVO[i].getInvclid(), "Manage"))
				// {
				// MessageDialog.showWarningDlg(this, "�������", "���������ظ��Ĵ������");
				// showHintMessage("�������");
				// return false;
				// }
				// }

				break;
			}

			// ���ݿ�������
			switch (iDatactlstyle) {

			case TypeDataCtrl.PRICE: // ����
				for (int i = 0; i < bodyVO.length; i++) {
					if (bodyVO[i].getOriprice() == null
							|| bodyVO[i].getOriprice().equals(
									DataTypeConst.UFDOUBLE_0)) {

						sMessage = nc.ui.ml.NCLangRes.getInstance().getStrByID(
								"4020pub", "UPP4020pub-000213")/*
																 * @res
																 * "��ǰ��ͬ���Ƶ��ۣ����۲���Ϊ�ջ��㣡"
																 */;
						MessageDialog.showErrorDlg(this, null, sMessage);
						return false;
					}
				}
				break;

			case TypeDataCtrl.NUM: // ����
				for (int i = 0; i < bodyVO.length; i++) {
					if (bodyVO[i].getAmount() == null
							|| bodyVO[i].getAmount().equals(
									DataTypeConst.UFDOUBLE_0)) {

						sMessage = nc.ui.ml.NCLangRes.getInstance().getStrByID(
								"4020pub", "UPP4020pub-000214")/*
																 * @res
																 * "��ǰ��ͬ������������������Ϊ�ջ��㣡"
																 */;
						MessageDialog.showErrorDlg(this, null, sMessage);
						return false;
					}
				}
				break;

			case TypeDataCtrl.MONEY: // ���
				for (int i = 0; i < bodyVO.length; i++) {
					if (bodyVO[i].getOrisum() == null
							|| bodyVO[i].getOrisum().equals(
									DataTypeConst.UFDOUBLE_0)) {
						sMessage = nc.ui.ml.NCLangRes.getInstance().getStrByID(
								"4020pub", "UPP4020pub-000215")/*
																 * @res
																 * "��ǰ��ͬ���ƽ�����Ϊ�ջ��㣡"
																 */;
						MessageDialog.showErrorDlg(this, null, sMessage);
						return false;
					}
				}
				break;

			case TypeDataCtrl.PRICE_NUM: // ����+����
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
																 * "��ǰ��ͬ���Ƶ��ۼ����������ۻ���������Ϊ�ջ��㣡"
																 */;
						MessageDialog.showWarningDlg(this, null, sMessage);
						return false;
					}
				}
				break;

			case TypeDataCtrl.PRICE_MONEY: // ����+���
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
																 * "��ǰ��ͬ���Ƶ��ۼ������ۻ����Ϊ�ջ��㣡"
																 */;
						MessageDialog.showWarningDlg(this, null, sMessage);
						return false;
					}
				}
				break;

			case TypeDataCtrl.NUM_MONEY: // ����+���
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
																 * "��ǰ��ͬ���������������������Ϊ�ջ��㣡"
																 */;
						MessageDialog.showWarningDlg(this, null, sMessage);
						return false;
					}
				}
				break;

			case TypeDataCtrl.PRICE_NUM_MONEY: // ����+����+���
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
																 * "��ǰ��ͬ���Ƶ��ۡ������������ۣ����������Ϊ�ջ��㣡"
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
	 * ����˵��������ִ�й��̼�¼ �������ڣ�(2002-4-8 14:12:59)
	 * 
	 * @return java.lang.String ���ߣ�����
	 */
	// protected void createExec(ManageVO mVO) {
	//
	// if (mVO == null)
	// return;
	//
	// try {
	// String sPk_ct = mVO.getParentVO().getPrimaryKey();
	// // �ƻ���ͬ��Чʱ��
	// UFDate UFValDate = new UFDate(mVO.getParentVO().getAttributeValue(
	// "valdate").toString());
	//
	// // �ƻ���ͬ��ֹʱ��
	// UFDate UFInvalDate = new UFDate(mVO.getParentVO()
	// .getAttributeValue("invallidate").toString());
	//
	// String sReason = null;
	// String sTitle = nc.ui.ml.NCLangRes.getInstance().getStrByID(
	// "common", "UC000-0000900")/* @res "ԭ��" */;
	// String sMessage = null;
	// // ִ����������
	// String sExecName = null;
	//
	// if (CTExecFlow.VALIDATE.equals(m_sExecFlag)) { // "ʵ����Ч"
	// if (UFValDate.compareTo(ClientEnvironment.getInstance()
	// .getDate()) != 0) {
	// if (isGlobelBatch) {
	// // �����������ֱ�ӷ��ظ����÷�������ʾ���û��Ի���
	// isExecPass = false;
	// return;
	// }
	// sMessage = nc.ui.ml.NCLangRes.getInstance().getStrByID(
	// "4020pub", "UPP4020pub-000038")/*
	// * @res
	// * "ʵ����Ч������ƻ���Ч���ڲ�����������ԭ��"
	// */;
	// sReason = (String) MessageDialog.showInputDlg(this, sTitle,
	// sMessage, null, 120);
	// }
	// sExecName = CTExecFlow.getValidateName()/* @res "ʵ����Ч" */;
	//
	// } else if (CTExecFlow.TERMINATE.equals(m_sExecFlag)) { // "ʵ����ֹ"
	// if (UFInvalDate.compareTo(ClientEnvironment.getInstance()
	// .getDate()) != 0) {
	// sMessage = nc.ui.ml.NCLangRes.getInstance().getStrByID(
	// "4020pub", "UPP4020pub-000039")/*
	// * @res
	// * "ʵ����ֹ������ƻ���ֹ���ڲ�����������ԭ��"
	// */;
	// sReason = (String) MessageDialog.showInputDlg(this, sTitle,
	// sMessage, null, 120);
	// }
	// sExecName = CTExecFlow.geTerminateName()/* @res "ʵ����ֹ" */;
	//
	// } else if (CTExecFlow.FREEZE.equals(m_sExecFlag)) { // "����"
	// sMessage = nc.ui.ml.NCLangRes.getInstance().getStrByID(
	// "4020pub", "UPP4020pub-000040")/* @res "�����붳���ͬ��ԭ��" */;
	// sReason = (String) MessageDialog.showInputDlg(this, sTitle,
	// sMessage, null, 120);
	// sExecName = CTExecFlow.getFreezeName()/* @res "����" */;
	//
	// } else if (CTExecFlow.UNFREEZE.equals(m_sExecFlag)) { // "�ⶳ"
	// sMessage = nc.ui.ml.NCLangRes.getInstance().getStrByID(
	// "4020pub", "UPP4020pub-000041")/* @res "������ⶳ��ͬ��ԭ��" */;
	// sReason = (String) MessageDialog.showInputDlg(this, sTitle,
	// sMessage, null, 120);
	// sExecName = CTExecFlow.getUnfreezeName()/* @res "�ⶳ" */;
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
	// // ����.
	// execVO = ContractWriterHelper.insertManageExecs(execVO);
	// // ���½���ִ��VO׷�ӵ����е�ִ��VO��.
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
	 * �˴����뷽��˵��:��ѯ��ͬ�ı�ͷʱ��� �������ڣ�(2003-11-3 17:04:19)
	 * 
	 * @param vo
	 *            nc.vo.ct.pub.ManageVO �޸����� 2009-8-21����04:36:49 �޸��ˣ�lirr
	 *            �޸�ԭ��ע�ͱ�־�� ���ٴӺ�̨��ѯts�����ڵ��ô˷���ǰ�Ķ����з���ts
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
	 * ����ǩ��ʱ�� taudittime
	 */
	protected void setAuditTime(ManageVO mVO, String sTS) {
		getCtBillCardPanel().setTailItem("taudittime", sTS);
		mVO.getParentVO().setAttributeValue("taudittime", sTS);
	}

	/**
	 * ����˵�����õ�����˾���б��ֶ�Ӧ�Ļ��� �������ڣ�(2001-12-26 17:20:55)
	 */
	protected void getAllRateDigit() {
	}

	/**
	 * ����˵�����õ����ֶ�Ӧ�ľ��� �������ڣ�(2001-11-27 9:55:51)
	 */
	protected void getCurrDigit() {
		try {
			m_hCurrDigit = new Hashtable();
			m_hCurrDigitcw = new Hashtable();
			// ��øñ��ֵ�С��λ��
			/*
			 * nc.vo.bd.b20.CurrtypeVO[] vos = nc.ui.bd.b20.CurrtypeBO_Client
			 * .queryAll(m_sPkCorp);
			 */
			// modified by lirr 2009-8-20����06:39:16 ����������
			nc.vo.bd.b20.CurrtypeVO[] vos = nc.ui.bd.b21.CurrtypeQuery
					.getInstance().getAllCurrtypeVOs();

			if (vos != null || vos.length > 0) {
				for (int i = 0; i < vos.length; i++) {
					// ҵ�񾫶�
					m_hCurrDigit.put(vos[i].getPk_currtype(), vos[i]
							.getCurrbusidigit());
					// ���񾫶�
					m_hCurrDigitcw.put(vos[i].getPk_currtype(), vos[i]
							.getCurrdigit());
				}

			} else {
				String sMessage = nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"SCMCOMMON", "UPPSCMCommon-000015")/*
															 * @res
															 * "����δ���壬���ȶ�����֣�"
															 */;
				MessageDialog.showErrorDlg(this, null, sMessage);
			}

		} catch (Exception e) {
			String sMessage = nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"4020pub", "UPP4020pub-000020")/* @res "�������ݿ�ʧ�ܣ�" */;
			MessageDialog.showErrorDlg(this, null, sMessage + "\n"
					+ e.getMessage());
		}
	}

	/**
	 * ����˵������ÿ��̶�Ӧ���� �������ڣ�(2001-12-11 13:18:01)
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
	 * ����˵�����ɱ��֣����ڵõ����ʾ��ȣ����ʺ�����ģʽ �������ڣ�(2001-11-27 9:55:51)
	 */
	protected void getCurrInfo(String pkCurrid, String subDate) {
	}

	/**
	 * �˴����뷽��˵���� ���ܣ� ������ ���أ� ���⣺ ���ڣ�(2002-8-28 11:21:41) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 */
	protected void getEnvironment() {
		ClientEnvironment env = getClientEnvironment();
		// ��˾����
		m_sPkCorp = env.getCorporation().getPk_corp().toString();
		// �õ��û������û�����
		m_sUserName = env.getUser().getUserName();
		m_sPkUser = env.getUser().getPrimaryKey();
		// ��ǰ����
		m_UFToday = env.getDate();

		// ��ò���
		// �����ͬ�۸����ȹ������
		// CT001:�����ͬ�۸����ȹ���,ϵͳĬ�ϲ���ֵΪ��˰�۸����ȣ����ں�˰���ۻ���˰������ѡȡ��
		// CT002:���ݱ���ʱ�Ƿ񴥷�������
		// CT010:�Ƿ�������Ƶ���
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

			// �Ƿ��Զ��������,Ĭ�����Զ�.
			String sAutoSend = sParam[1];
			// if (sAutoSend != null && sAutoSend.equals("��"))
			// /*-=notranslate=-*/
			// modified by lirr 2009-8-25����03:14:07 queryBatchParaValuesȡ��ΪN
			if (sAutoSend != null && sAutoSend.equals("N")) /*-=notranslate=-*/
				m_isAutoSendApprove = false;

			// �Ƿ�������Ƶ���
			String sIsSaveInitOper = sParam[2];
			if (sIsSaveInitOper != null && sIsSaveInitOper.equals("N"))
				m_isSaveInitOper = new UFBoolean("false");
		} catch (Exception e) {
			sContractPriceRule = "��˰����"; /*-=notranslate=-*/
		}
	}

	/**
	 * ��ò�ѯ������ �������ڣ�(2001-9-10 13:13:55)
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

			// ��ʼ����ѯģ��
			// m_QueryConditionDlg = new CTBillQueryConditionDlg(this);
			// m_QueryConditionDlg.setTempletID(m_sPkCorp, m_sNodeCode,
			// m_sPkUser,
			// null);
			m_QueryConditionDlg
					.setDefaultCloseOperation(SCMQueryConditionDlg.HIDE_ON_CLOSE);
			m_QueryConditionDlg.setICtType(m_iCtType);
			m_QueryConditionDlg.setRefCtTypeQryEditor("ct_manage.pk_ct_type");

			// 5.1���ӵ�������Ȩ�ޣ������ڲ�ѯģ���������˹�˾�������������ʼ��
			// modify by dgq
			ArrayList alCorpIDs = new ArrayList();
			alCorpIDs.add(m_sPkCorp);
			// Ĭ�ϵ�ǰ��¼��˾����ֻ��ѡ��ǰ��¼��˾
			m_QueryConditionDlg.initCorpRef("ct_manage.pk_corp", m_sPkCorp,
					alCorpIDs);

			// �ճ���ͬά�������Ӳ�ѯ��������ͬ״̬�����°汾��
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
									 * @res "ȫ��״̬"
									 */} });

			// �޸��Զ�����Ŀ��Ϊ�Զ�����Ӳ��ա� ShawBing 2005-06-09
			m_QueryConditionDlg.updateQueryConditionClientUserDef(m_sPkCorp,
					m_sBillType, "ct_manage.def", "ct_manage_b.def");

			// modified by liuzy 2008-01-02 ����ͬǩ������һ��Ĭ��ֵ
			// m_QueryConditionDlg.setInitDate("ct_manage.subscribedate",
			// m_UFToday.toString());
			// modified by liuzy 2008-03-26 �̹�԰��ͬ��ѯ���ӵ�������Ĭ������
			m_QueryConditionDlg.setInitDate("ct_manage.operdate.from",
					getClientEnvironment().getDate().toString());
			m_QueryConditionDlg.setInitDate("ct_manage.operdate.end",
					getClientEnvironment().getDate().toString());

			m_QueryConditionDlg.setInitDate("ct_manage.ctflag", "2");
			// m_QueryConditionDlg.setInitDate("ct_manage.pk_corp",m_sPkCorp);
			// ��ѯ�Ի�����ʾ��ӡ����ҳǩ��
			m_QueryConditionDlg.setShowPrintStatusPanel(true);
			m_QueryConditionDlg.hideNormal();
			// ��������Ȩ�޵Ĵ���
			m_QueryConditionDlg.setCorpRefs("ct_manage.pk_corp", new String[] {
					"ct_manage_b.invclid", "ct_manage_b.invid",
					"ct_manage.depid"/* ���� */,
					"ct_manage.personnelid"/* ��Ա */,
					"ct_manage.custid"/* ��Ӧ�� */, "ct_manage.projectid"/* ��Ŀ */
			});

		}

		return m_QueryConditionDlg;
	}

	/**
	 * �˴����뷽��˵��:��ñ����յ�Ԫ��༭�� �������ڣ�(2001-9-22 12:56:04)
	 * 
	 * @return
	 */
	private UIRefCellEditor getRefCtTypeQryCellEditor() {
		if (m_RefCtTypeQryCellE == null) {
			m_RefCtTypeQryCellE = new UIRefCellEditor(getRefCtTypeQryPane());
			// ����Ϊ˫���󵯳����հ�ť
			// m_RefCtTypeCellE.setClickCountToStart(1);
		}
		return m_RefCtTypeQryCellE;
	}

	/**
	 * �˴����뷽��˵��:��ò������ �������ڣ�(2001-9-21 19:36:00)
	 */
	private UIRefPane getRefCtTypeQryPane() {

		if (m_RefCtTypeQryPane == null) {
			m_RefCtTypeQryPane = new UIRefPane();
			m_RefCtTypeQryPane.setIsCustomDefined(true); // ���ò���Ϊ�Զ���
			// ���ò���Model
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
			// ����Ϊ���ش���
			m_RefCtTypeQryPane.setReturnCode(true);
		}
		return m_RefCtTypeQryPane;
	}

	/**
	 * ����˵�����õ����������ۡ�ԭ�ҽ����ҽ��� �������ڣ�(2001-12-4 15:53:46)
	 */
	protected void getSysInit() {

		// Ϊ���Ч�ʣ�����������ķ�ʽ��
		try {
			/*
			 * String[] saParaCode = new String[] { "BD301", "BD303", "BD505",
			 * "BD501", "BD502", "BD503" };
			 */
			// modified by lirr 2009-8-17����01:24:16 ɾ��"BD303"
			String[] saParaCode = new String[] { "BD301", "BD505", "BD501",
					"BD502", "BD503" };
			Hashtable htPara = nc.ui.pub.para.SysInitBO_Client
					.queryBatchParaValues(m_sPkCorp, saParaCode);
			if (htPara != null) {
				// ���ұ���С��λ��
				m_sLocalCurrID = (String) htPara.get("BD301");
				if (m_sLocalCurrID != null) {
					// ҵ�񾫶�
					m_iMainCurrDigit = ((Integer) m_hCurrDigit
							.get(m_sLocalCurrID)).intValue();
					// ���񾫶�
					m_iMainCurrDigitcw = ((Integer) m_hCurrDigitcw
							.get(m_sLocalCurrID)).intValue();
				}
				// ���ұ���С��λ�� modify by liuzy 2007-04-25
				// deleted by lirr 2008-8-20 ����5.5����ɾ��������Ϣ

				/*
				 * if ( !(htPara.get("BD303").toString().trim().equals("null")) &&
				 * htPara.get("BD303") != null &&
				 * !htPara.get("BD303").toString().trim().equals(""))
				 * m_sFracCurrID = (String) htPara.get("BD303");
				 * if(m_sFracCurrID != null && m_sFracCurrID.trim().length() >
				 * 0) m_iFracCurrDigit = ((Integer) m_hCurrDigit
				 * .get(m_sFracCurrID)).intValue();
				 */

				// ����С��λ
				String priceDigit = (String) htPara.get("BD505");
				if (priceDigit != null) {
					m_iPriceDigit = new Integer(priceDigit).intValue();
				}
				// ����С��λ
				String amountDigit = (String) htPara.get("BD501");
				if (amountDigit != null) {
					m_iAmountDigit = new Integer(amountDigit).intValue();
				}
				// ����������С��λ
				String fracAmountDigit = (String) htPara.get("BD502");
				if (fracAmountDigit != null) {
					m_iFracAmountDigit = new Integer(fracAmountDigit)
							.intValue();
				}
				// �����ʾ���
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
	 * ����ʵ�ָ÷���������ҵ�����ı��⡣
	 * 
	 * @version (00-6-6 13:33:25)
	 * @return java.lang.String
	 */
	public String getTitle() {
		return getCtBillCardPanel().getTitle();
	}

	/**
	 * �˴����뷽��˵��:�õ�����ۺ�VO �������ڣ�(2001-12-18 16:59:11)
	 * 
	 * @return nc.vo.pub.AggregatedValueObject
	 */
	public nc.vo.pub.AggregatedValueObject getVo() throws java.lang.Exception {
		/*
		 * ManageVO vo = new ManageVO(); vo = getCurVO();
		 */
		ManageVO vo = null;

		// �õ�����VO
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
			// ������ڳ���ͬ����ô��������Ч��
			if (m_UFbIfEarly.booleanValue())
				vo.setBillStatus(BillState.VALIDATE);
			else
				vo.setBillStatus(BillState.AUDIT);

			return vo;
		} else {
			showWarningMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"4020pub", "UPP4020pub-000042")/* @res "����״̬�������ɻ�������У�" */);
			return null;
		}
	}

	/**
	 * �˴����뷽��˵��:���һ���Ƿ����Ӱ����һ��ִ�е����� �������ڣ�(2001-10-28 19:27:46)
	 */
	private boolean haveQuestion() {
		/*
		 * if (!(m_iBillState == OperationState.FREE) || !(m_iTbState ==
		 * OperationState.FREE)) {
		 */
		// modified by lirr 2008-07-23 �޸�ԭ������ǲ������ʱ �޸�ת�������ĺ�ͬȡ��ʱ �����
		if ((!(m_iBillState == OperationState.FREE) || !(m_iTbState == OperationState.FREE))
				&& !m_bAddFromBillFlag) {
			/* @res "{0}�����ѱ����ģ��Ƿ���Ҫ���棿" */
			String sMessage = nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"4020pub",
					"UPP4020pub-000193",
					null,
					new String[] { getCtBillCardPanel().getBodyTabbedPane()
							.getTitleAt(m_iTabbedFlag) });

			int ync = MessageDialog.showYesNoCancelDlg(this, null, sMessage);
			if (ync == 4) { // ��Ҫ����
				onSave(); // �����������޸�
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
		return true; // ��ʾ����ִ��һ�´���
	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2001-8-22 13:01:54)
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
		// ���� ��ͬ����ҳǩ �Ҽ��˵���ӡ����ġ���ť added by lirr 2008-07-10
		UIMenuItem[] oldUIMenuItem = getCtBillCardPanel().getBodyMenuItems();
		UIMenuItem[] newUIMenuItem = new UIMenuItem[oldUIMenuItem.length + 1];
		for (int i = 0; i < oldUIMenuItem.length; i++) {
			newUIMenuItem[i] = oldUIMenuItem[i];
		}

		miaddNewRowNo.removeActionListener(this);
		miaddNewRowNo.addActionListener(this);

		newUIMenuItem[oldUIMenuItem.length] = miaddNewRowNo; // ���������к�
		getCtBillCardPanel().setBodyMenu("table", newUIMenuItem);

		/*
		 * for (int i = 0 ; i < getCtBillCardPanel().getBodyMenuItems().length ;
		 * i++) {
		 * if(!getButtonTree().getButton(getCtBillCardPanel().getBodyMenuItems()[i].getText().toString()).isPower()){
		 * 
		 * newUIMenuItem[i].setEnabled(false); } }
		 */

		// �������ɫ��
		new InvAttrCellRenderer().setFreeItemRenderer(getCtBillCardPanel());

		add(getCtBillCardPanel(), "Center");
		add(getCtBillListPanel(), "North");
		getCtBillCardPanel().setVisible(true);
		getCtBillListPanel().setVisible(false);

		// ���õ��ݿ�Ƭ���ɱ༭
		getCtBillCardPanel().setEnabled(false);
		CTTool tool = new CTTool();
		tool.setColorCard(getCtBillCardPanel(), null, null, null);
		// added by lirr 2008-08-14 ����ɫ
		// modified by lirr 2008-11-19 ����ҳǩ��Ҫ���ϱ���ɫҪ��
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
				// �б� ����ӱ���ɫ
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
		// ��ͷ�ӱ���ɫ
		getCtBillListPanel().getHeadTable().setRowSelectionAllowed(true);
		getCtBillListPanel().getHeadTable().setColumnSelectionAllowed(false);
		getCtBillListPanel().getHeadTable().setSelectionMode(
				javax.swing.ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		/*
		 * // ����ӱ���ɫ getCtBillListPanel().getBodyTable(CTTableCode.BASE)
		 * .setRowSelectionAllowed(true);
		 * getCtBillListPanel().getBodyTable(CTTableCode.BASE)
		 * .setColumnSelectionAllowed(false);
		 * getCtBillListPanel().getBodyTable(CTTableCode.BASE).setSelectionMode(
		 * javax.swing.ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		 */

		getCtBillCardPanel().addActionListener(this);

		setBillCardPaneFillEnable(getCtBillCardPanel());

		// added by lirr 2008-11-11 �޸�ԭ���б��²�ͬ�ı��ֵ��۱����ʾ��Ȳ�ͬ
		scmCurrRateDigit = new ScmCurrLocRateBizDecimalListener(
				getCtBillListPanel().getHeadBillModel(), "currid",
				new String[] { "currrate" }, m_sPkCorp);
		// added by lirr 2009-11-16����04:31:14 ֧�ֱ༭��ʽ
		getCtBillCardPanel().setAutoExecHeadEditFormula(true);
	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2008-8-12 13:01:54) added by lirr ��Ƭ�༭
	 */
	protected void onLineCardEdit() {
		String pk = ((UIRefPane) getCtBillCardPanel().getHeadItem("ct_type")
				.getComponent()).getRefPK();

		if (pk == null || pk.length() <= 0) {
			showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"4020pub", "UPP4020pub-000092")/* @res "��ͬ���Ͳ���Ϊ��" */);
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
	 * �˴����뷽��˵���� �������ڣ�(2008-8-12 13:01:54) added by lirr �����к�
	 */
	protected void onAddNewRowNo() {
		nc.ui.scm.pub.report.BillRowNo.addNewRowNo(getCtBillCardPanel(),
				m_sBillType, getCtBillCardPanel().getRowNumKey());
	}

	/**
	 * ���������������л��б�/��Ƭ��ť
	 * <p>
	 * <b>examples:</b>
	 * <p>
	 * ʹ��ʾ��
	 * <p>
	 * <b>����˵��</b>
	 * <p>
	 * 
	 * @author liuzy
	 * @time 2007-5-17 ����04:52:09
	 */
	public void showBtnSwitch() {
		if (m_bIsCard) {
			getButtonTree().getButton(CTButtonConst.BTN_SWITCH).setName(
					nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
							"UCH022")/*
										 * @res "�б���ʾ"
										 */);
			getButtonTree().getButton(CTButtonConst.BTN_SWITCH).setHint(
					nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
							"UCH022")/*
										 * @res "�б���ʾ"
										 */);
		} else {
			getButtonTree().getButton(CTButtonConst.BTN_SWITCH).setName(
					nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
							"UCH021")/*
										 * @res "��Ƭ��ʾ"
										 */);
			getButtonTree().getButton(CTButtonConst.BTN_SWITCH).setHint(
					nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
							"UCH021")/*
										 * @res "��Ƭ��ʾ"
										 */);
		}
		updateButton(getButtonTree().getButton(CTButtonConst.BTN_SWITCH));

	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2001-9-7 12:44:23)
	 */
	protected void initListener() {
		getCtBillCardPanel().addEditListener(this);
		getCtBillListPanel().addEditListener(this);

		// ���ӵ����б�˫����ͷ�������
		getCtBillListPanel().getHeadBillModel().addSortListener(this);
		getCtBillListPanel().getHeadBillModel().addSortRelaObjectListener(this);

		// ���ӵ����б�ı�ͷ��ѡ������
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

		// ��ʼ���༭ǰ������
		getCtBillCardPanel().getBillModel().setCellEditableController(this);

		// ����ǰ����
		getCtBillCardPanel().getBillModel(CTTableCode.BASE)
				.setSortPrepareListener(this);
		getCtBillListPanel().getBodyBillModel(CTTableCode.BASE)
				.setSortPrepareListener(this);

		// ���ӱ����������
		// added by qinchao 2009-04-21 �����ϲ��������ݴ������� ����󻺴�δ����
		getCtBillListPanel().getBodyBillModel(CTTableCode.BASE)
				.addSortListener(this);
		getCtBillCardPanel().getBillModel(CTTableCode.BASE).addSortListener(
				this);
	}

	/**
	 * ��ʼ�����е�Table �������ڣ�(2001-9-3 9:46:05)
	 */
	protected void initTables() {
	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2001-10-24 8:33:41)
	 */
	protected void initVariable() {

		m_bIsNeedReInit = new boolean[6];
		// ��־�Ƿ���Ҫ���³�ʼ��Table����
		for (int i = 1; i < 6; i++)
			m_bIsNeedReInit[i] = true; // "ture"Ϊ��Ҫ

		m_vBillVO = new Vector<ExtendManageVO>(); // ���ڵ��ݵ���ʱ����
		// added by lirr 2008-7-23
		m_vBillVOForRef = new Vector<ExtendManageVO>(); // ����ת�����ݵ���ʱ����
		m_vTableVO = new Vector(); // ����Table����ʱ����

	}

	/**
	 * Invoked when an item has been selected or deselected. The code written
	 * for this method performs the operations that need to occur when an item
	 * is selected (or deselected).
	 */
	public void itemStateChanged(java.awt.event.ItemEvent e) {
	}

	/**
	 * �˴����뷽��˵���� ���ܣ� ������ ���أ� ���⣺ ���ڣ�(2002-9-16 16:39:54) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 */
	protected final void loadData() {

		// added by lirr 2009-02-17
		switch (m_iTabbedFlag) {

		case TabState.BILL: // ��ͬ����ҳǩ
			getCtBillCardPanel().getBillModel("table").setBodyDataVO(
					getCurVO().getChildrenVO());
			break;

		case TabState.TERM: // ����ҳǩ
			getCtBillCardPanel().getBillModel("term").setBodyDataVO(
					getCurVO().getTermBb4s());
			break;

		case TabState.EXP: // ����ҳǩ
			getCtBillCardPanel().getBillModel("cost").setBodyDataVO(
					getCurVO().getExpBb3s());
			break;

		case TabState.NOTE: // ��ͬ���¼�ҳǩ
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
	 * �˴����뷽��˵���� �������ڣ�(2001-8-24 13:43:20)
	 */
	protected void onAdd() {
		try {

			if (!m_bIsCard) { // ������ڵ����б�ģʽ�£����л�����Ƭģʽ��
				onList();
				m_bIsCard = true;
			}
			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"4020pub", "UPP4020pub-000044")/*
													 * @res
													 * "���һ���µĺ�ͬ����ȷ¼���ͬ���������桱��ť���б��档"
													 */);
			// added by lirr 2008-09-23
			// ���ú�ͬ�Ĳ������ͣ�ԭ����onedit()ʱ����setCtRef()�����ϲ����������ݺ�����˿��ƣ�
			// ת����Ϻ��޷����¼��أ��ʷ��ڴ˴�

			((UIRefPane) getCtBillCardPanel().getHeadItem("ct_type")
					.getComponent()).setWhereString("pk_corp = '" + m_sPkCorp
					+ "' and nbusitype=" + m_iCtType);

			m_iBillState = OperationState.ADD;
			// ����Ϊ�ɱ༭
			getCtBillCardPanel().setEnabled(true);
			getCtBillCardPanel().addNew();

			// �ϼ��б��
			getCtBillCardPanel().getTotalTableModel().setValueAt(
					nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
							"UC000-0001146")/* @res "�ϼ�" */, 0, 0);
			// ���ݸ���

			// �ı䵥�ݣ�Table��Ҫ������������
			for (int i = 1; i < 6; i++)
				m_bIsNeedReInit[i] = true;

			getCtBillCardPanel().setHeadItem("ctflag",
					new Integer(BillState.FREE));
			// �õ�ǰ��˾
			getCtBillCardPanel().setHeadItem("pk_corp", m_sPkCorp);
			getCtBillCardPanel().setHeadItem("unitname", m_sPkCorp);

			// ��ȡ��ǰ�û�ID��Ӧ��ҵ��Ա
			PsndocVO psnvo = getPsndocByUserid();
			if (psnvo != null) {// �����Ӧ��ҵ��Ա
				if (getCtBillCardPanel().getHeadItem("personnelid").getValue()
						.toString().trim().equals("")) {
					// �õ�ǰ�û�ID��Ӧ��ҵ��Ա
					getCtBillCardPanel().setHeadItem("personnelid",
							psnvo.getPk_psndoc());
					getCtBillCardPanel().setHeadItem("pername",
							psnvo.getPk_psndoc());
				}
				if (getCtBillCardPanel().getHeadItem("depid").getValue()
						.toString().trim().equals("")) {
					// �õ�ǰ�û�ID��Ӧ��ҵ��Ա��Ӧ�Ĳ���
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

			// set��ӡ����Ϊ����
			getCtBillCardPanel().setTailItem("iprintcount", new Integer(0));

			// ��ͬǩ����������Ĭ��ֵ
			getCtBillCardPanel().setHeadItem("subscribedate", m_UFToday);
			// ����Ĭ��Ϊ��λ��
			((UIRefPane) getCtBillCardPanel().getHeadItem("currname")
					.getComponent()).setPK(m_sLocalCurrID);
			afterCurrNameEdit(m_bAddFromBillFlag);
			getCtBillCardPanel().getHeadItem("currrate").setEdit(false);

			getCtBillCardPanel().setEnabled(true);
			// ִ�й�ʽ
			getCtBillCardPanel().getBillModel().execLoadFormula();

			rightButtonRightControl();
			// ����ʱ������һ�У�
			onAddLine();
			setButtonState();
		} catch (Exception e) {
			nc.vo.scm.pub.SCMEnv.out(e);
		}
	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2001-8-28 16:28:12)
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
	 * �˴����뷽��˵���� �������ڣ�(2001-9-1 17:13:11)
	 */
	protected void onCancel() {
		int iSelectResult = MessageDialog
				.showYesNoDlg(this, null, nc.ui.ml.NCLangRes.getInstance()
						.getStrByID("common", "UCH067")/* @res "�Ƿ�ȷ��Ҫȡ����" */);
		if (iSelectResult != MessageDialog.ID_YES)
			return;

		getCtBillCardPanel().setEnabled(false);
		// added by lirr 2008-07-23 �ж��Ƿ��Ƕ�ת������Ҫ���ɵĺ�ͬ ȡ��
		if (m_bAddFromBillFlag) {
			m_vBillVOForRef.remove(m_iId - 1);
			// onList();

			if (m_bIsCard) { // ��Ƭ
				// �л����б�ģʽ��
				onList();

			} else { // �б�
				setListDataForRef();
				getCtBillListPanel().setVisible(true);
				getCtBillCardPanel().setVisible(false);
			}
			// setButtonStateForCof();
			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"common", "UCH008")/* @res "ȡ���ɹ�" */);
			return;
		}

		else {
			if (m_vBillVO != null && m_vBillVO.size() > 0) {
				// added by lirr 2009-7-3����09:24:54 ȡ��ʱ��Ƭ������Ҫ���������۱����ʾ���

				getCtBillCardPanel().getHeadItem("currrate").setDecimalDigits(
						getCurrateDigit(((ManageHeaderVO) getCurVO()
								.getParentVO()).getCurrid()));

				getCtBillCardPanel().superresumeValue();// �ӱ��������лָ����ݵ�����
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

			case TabState.BILL: // ��ͬ����ҳǩ
				m_iBillState = OperationState.FREE;
				getCtBillCardPanel().stopEditing();
				getCtBillCardPanel().setEnabled(false);
				getCtBillCardPanel().superupdateValue();// ���ݽ����ֵ���±��ݵ�����
				break;

			case TabState.TERM: // ����ҳǩ
				m_iTbState = OperationState.FREE;
				getCtBillCardPanel().getBillModel("term").resumeValue();
				getCtBillCardPanel().getBillModel("term").setEnabled(false);
				getCtBillCardPanel().getBillModel("term").updateValue();
				break;

			case TabState.EXP: // ����ҳǩ
				m_iTbState = OperationState.FREE;
				getCtBillCardPanel().getBillModel("cost").resumeValue();
				getCtBillCardPanel().getBillModel("cost").setEnabled(false);
				getCtBillCardPanel().getBillModel("cost").updateValue();
				break;

			case TabState.NOTE: // ��ͬ���¼�ҳǩ
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

		// ȡ��������Զ����� ��qinchao 2009-04-08
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
		// added by lirr 2009-12-2����09:56:12 ȡ�����������getDmdo().setCellControls ���������������������������� ����Խ��
		getCtBillCardPanel().getDmdo().setCellControls(null);
		
		showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
				"UCH008")/* @res "ȡ���ɹ�" */);
	}

	/**
	 * ����˵���������ͬ�� 1����ͬ��������Խ��б�������� 2�����ʱ��¼����˺ͱ�����ڡ� 3����ͬ���������ֶΣ��汾�ź��Ƿ񼤻
	 * ����������ɵĺ�ͬ�汾���Զ���1 ���ߣ������� �������ڣ�(2003-09-02 09:22:55)
	 */
	protected void onChange() {
		// ������ڵ����б�ģʽ�£����л�����Ƭģʽ��
		if (!m_bIsCard)
			onList();
		// ����Ϊ���״̬
		m_iBillState = OperationState.CHANGE;

		showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4020pub",
				"UPP4020pub-000045")/* @res "�����ǰ��ͬ��Ȼ����Ե�������桱��ť���б���" */);

		// ����Ϊ�ɱ༭
		getCtBillCardPanel().setEnabled(true);
		// ��ͬ���룬���ͣ���Ŀ�ţ���ͬǩ�������ڣ����̣����֣����ʣ����������࣬������ɱ༭��
		getCtBillCardPanel().getHeadItem("ct_code").setEnabled(false);
		getCtBillCardPanel().getHeadItem("ct_type").setEnabled(false);
		getCtBillCardPanel().getHeadItem("projectname").setEnabled(false);
		getCtBillCardPanel().getHeadItem("custname").setEnabled(false);
		getCtBillCardPanel().getHeadItem("currname").setEnabled(false);
		getCtBillCardPanel().getHeadItem("currrate").setEnabled(false);
		getCtBillCardPanel().getHeadItem("subscribedate").setEnabled(false);
		// getCtBillCardPanel().getHeadItem("astcurrate").setEnabled(false);

		/**
		 * ��������ֶε�ֵ �����ۼ�ִ�������������ۼ�ִ�н��ڳ�������������ڳ�������� �ڳ�����Ʊ�������ڳ�����Ʊ���ڳ��ظ���
		 * ���ϲ�Ϊ�ջ�Ϊ����������ô���Ͳ��ܱ༭����ʹ�����ࡣ
		 */
		Object oRefValue = ((UIRefPane) getCtBillCardPanel().getHeadItem(
				"ct_type").getComponent()).getRefValue("ninvctlstyle");
		// ���ݺ�ͬ���͵Ĵ�����Ʒ�ʽ���ô������ʹ����������Ƿ�ɱ༭
		int iInvctlstyle = Integer.parseInt(oRefValue.toString());

		UFDouble ufd = null; // �����ۼ�ִ������
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
			// �ճ����ڳ���Ҫ���
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
			// ����ڳ���
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
			} else { // �����ͬ���͵Ĵ�����Ʒ�ʽΪ���
				if (iInvctlstyle == 0) {
					getCtBillCardPanel().setCellEditable(i, "invclasscode",
							false);
					getCtBillCardPanel().setCellEditable(i, "invcode", true);
				} else if (iInvctlstyle == 1) {
					// �����ͬ���͵Ĵ��������Ʒ�ʽ�ĺ�ͬ
					getCtBillCardPanel().setCellEditable(i, "invclasscode",
							true);
					getCtBillCardPanel().setCellEditable(i, "invcode", false);
				} else if (iInvctlstyle == 2) {
					// �����ͬ����Ϊ����ʹ������Ϊ��
					getCtBillCardPanel().setCellEditable(i, "invclasscode",
							false);
					getCtBillCardPanel().setCellEditable(i, "invcode", false);
				}

			}
			/*
			 * �����ۼ�ִ������������ִ���ۼƼ�˰�ϼƲ��ɱ�� �ڳ����������������������Ʊ����������Ʊ���ظ�������Ա༭��
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
		} // ���������Ҽ��㹫ʽ
		String pk = getCtBillCardPanel().getHeadItem("currid").getValue();
		// modified by lirr 2008-11-22 �޸�ԭ�򣺱����������·�ʽ
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
	 * �˴����뷽��˵���� ���ܣ� ������ ���أ� ���⣺ ���ڣ�(2002-5-11 16:44:36) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 */
	protected void onCopy() {

		try {
			if (!m_bIsCard) { // ������ڵ����б�ģʽ�£����л�����Ƭģʽ��
				onList();
				m_bIsCard = true;
			}
			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"4020pub", "UPP4020pub-000046")/*
													 * @res
													 * "����һ�ź�ͬ���޸ĺ��������桱��ť���б��档"
													 */);

			m_iBillState = OperationState.ADD;
			m_timer.start("ִ�и��Ƴ���"); /*-=notranslate=-*/

			getCtBillCardPanel().addNew();
			// �����Զ�����
			getCtBillCardPanel().setAutoAddEditLine(true);
			// ��������Ҽ���ɾ��
			getCtBillCardPanel().setBodyMenuShow(true);

			m_timer.showExecuteTime("getCtBillCardPanel().addNew()");
			// ���ݸ���

			if (m_iId != 0) {
				ManageVO mVO = getCurVO();
				ManageVO voTemp = mVO.clone(mVO);

				voTemp.getParentVO().setPrimaryKey(null);
				voTemp.getParentVO().setAttributeValue("actualvalidate", null);
				voTemp.getParentVO()
						.setAttributeValue("actualinvalidate", null);
				voTemp.getParentVO().setAttributeValue("ts", null);
				voTemp.getParentVO().setAttributeValue("nprepaymny", null);
				voTemp.setChangeBb1s(null); // ��ͬ���
				voTemp.setExpBb3s(null);
				voTemp.setManageExecs(null);
				voTemp.setMemoraBb2s(null);

				// ����ͬ����ŵ�һ�������У��Ա��ͬ�����ͬʱ���ơ�
				if (voTemp.getTermBb4s() != null
						&& voTemp.getTermBb4s().length > 0){
					    m_copyedTermVOs = voTemp.getTermBb4s();
					 // added by lirr 2009-12-2����10:13:44 ����������ĺ�ͬʱ��ͬ�����TSӦ���
              clearTermsTs();
              //end
				}
				// �����Ļ�д����
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
					// modified by lirr 2008-09-22 ���ۼƸ����� �ۼ�Ӧ������
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

					// added by lirr 2009-7-27 ����03:53:23
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
// added by lirr 2009-11-30����01:16:49л��Ҫ�� ��������ʱ��Դ��ϢҪ��� ������ʱ56��ʼ�������
					clearSouceInfo((ManageItemVO)voTemp.getChildrenVO()[i]);
				}
				m_timer.showExecuteTime("�����Ļ�д����"); /*-=notranslate=-*/

				getCtBillCardPanel().setBillValueVO(voTemp);
				m_timer
						.showExecuteTime("getCtBillCardPanel().setBillValueVO(mVO)");
				getCtBillCardPanel().resetAllRowNo();
				m_timer.showExecuteTime("getCtBillCardPanel().resetAllRowNo()");
				// ��Id�������
				setIdtoName();
				m_timer.showExecuteTime("setIdtoName()");
				// �ı䵥�ݣ�Table��Ҫ������������
				for (int i = 1; i < 6; i++)
					m_bIsNeedReInit[i] = true;

				getCtBillCardPanel().setHeadItem("ctflag",
						new Integer(BillState.FREE));
				getCtBillCardPanel().getHeadItem("ct_code").clearViewData();
				getCtBillCardPanel().getHeadItem("ct_name").clearViewData();
				// ��ͬ�汾�ų�ʼ��Ϊ1.0
				getCtBillCardPanel().setHeadItem("version", "1.0");
				// ���Ԥ����
				if (getCtBillCardPanel().getHeadItem("nprepaymny") != null)
					getCtBillCardPanel().getHeadItem("nprepaymny")
							.clearViewData();
				// modified by lirr 2008-09-22 ���ۼƸ����ܶ� Ӧ�ո����
				if (null != getCtBillCardPanel().getHeadItem("ntotalgpamount")) {
					getCtBillCardPanel().getHeadItem("ntotalgpamount")
							.clearViewData();
				}
				if (null != getCtBillCardPanel()
						.getHeadItem("ntotalgpshamount")) {
					getCtBillCardPanel().getHeadItem("ntotalgpshamount")
							.clearViewData();
				}
				// added by lirr 2009-7-27 ����03:52:40 ���ۼƸ����ܶ� Ӧ�ո���� ԭ��
				if (null != getCtBillCardPanel().getHeadItem(
						"noritotalgpamount")) {
					getCtBillCardPanel().getHeadItem("noritotalgpamount")
							.clearViewData();
				}
				if (null != getCtBillCardPanel().getHeadItem("norigpshamount")) {
					getCtBillCardPanel().getHeadItem("norigpshamount")
							.clearViewData();
				}
				// added by lirr 2009-8-11����03:09:15 ��ԭ��Ԥ����

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

				// �ô�ӡ����Ϊ0��
				getCtBillCardPanel().setTailItem("iprintcount", new Integer(0));

				// ����޸���
				getCtBillCardPanel().getTailItem("clastoperatorid")
						.clearViewData();
				getCtBillCardPanel().getTailItem("vlastoperatorname")
						.clearViewData();
				// ����޸�ʱ��
				getCtBillCardPanel().getTailItem("tlastmaketime")
						.clearViewData();

				getCtBillCardPanel().setEnabled(true);
				m_timer.showExecuteTime("��ձ�ͷ����˵�����"); /*-=notranslate=-*/

				// ���������Ҽ��㹫ʽ
				// modified by lirr 2008-11-22 �޸�ԭ�򣺱����������·�ʽ
				/*
				 * String pk = getCtBillCardPanel().getHeadItem("currid")
				 * .getValue(); if (pk != null) setFormularByCur(pk);
				 */

				m_timer.showExecuteTime("afterCurrNameEdit()");
				// ִ�й�ʽ
				getCtBillCardPanel().getBillModel().execLoadFormula();

				m_timer.showExecuteTime("ִ�й�ʽ"); /*-=notranslate=-*/

				// added by lirr 2008-11-11 ����ԭ�򣺸����������������ͬ�۱����ʲ������޸�
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
			// �ϼ��б��
			getCtBillCardPanel().getTotalTableModel().setValueAt(
					nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
							"UC000-0001146")/* @res "�ϼ�" */, 0, 0);

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
	 * �˴����뷽��˵���� �������ڣ�(2001-8-29 10:02:05)
	 */
	protected void onDel() {
		// ����Ա��־
		nc.vo.sm.log.OperatelogVO log = getNormalOperateLog();

		ManageVO tempMVO = getCurVO();
		int ctState = Integer.parseInt(tempMVO.getParentVO().getAttributeValue(
				"ctflag").toString());
		String sMessage = null;

		// �Դ��ڲ�ͬ״̬�ĺ�ͬ���в�ͬ�Ĵ���
		if (ctState == BillState.AUDIT) { // ���״̬
			sMessage = nc.ui.ml.NCLangRes.getInstance().getStrByID("4020pub",
					"UPP4020pub-000051")/* @res "����ɾ�����ú�ͬ���������!" */;
			MessageDialog.showWarningDlg(this, null, sMessage);

			return;
		}

		if (ctState == BillState.FREEZE) { // ����״̬
			sMessage = nc.ui.ml.NCLangRes.getInstance().getStrByID("4020pub",
					"UPP4020pub-000052")/* @res "����ɾ�����ú�ͬ�ѱ����ᣡ" */;
			MessageDialog.showWarningDlg(this, null, sMessage);

			return;
		}

		if (ctState == BillState.VALIDATE) { // ��Ч״̬
			sMessage = nc.ui.ml.NCLangRes.getInstance().getStrByID("4020pub",
					"UPP4020pub-000053")/* @res "����ɾ�����ú�ͬ����Ч��" */;
			MessageDialog.showWarningDlg(this, null, sMessage);

			return;
		}

		if (ctState == BillState.TERMINATE) { // ��ֹ״̬
			sMessage = nc.ui.ml.NCLangRes.getInstance().getStrByID("4020pub",
					"UPP4020pub-000054")/* @res "����ɾ�����ú�ͬ������ֹ״̬��" */;
			MessageDialog.showWarningDlg(this, null, sMessage);

			return;
		}

		// ��������״̬����ֹ״̬�ĺ�ͬ����ɾ��

		sMessage = nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
				"UCH002")/*
							 * @res "�Ƿ�ȷ��Ҫɾ����"
							 */;

		if (MessageDialog.showYesNoDlg(this, null, sMessage,
				MessageDialog.ID_NO) != UIDialog.ID_YES)
			return;

		try {

			sMessage = nc.ui.ml.NCLangRes.getInstance().getStrByID("4020pub",
					"UPP4020pub-000056")/* @res "��ʼɾ����ǰ��ͬ����" */;
			showHintMessage(sMessage);

			String sBillOwnerID = null; // Ҫɾ�����ݵ�ӵ���ߣ���Բ������Ƿ�����ɾ�������Ƶ��ĵ��ݣ�
			if (m_bIsCard) // ��Ƭ��ȡ��β��operid
				sBillOwnerID = getCtBillCardPanel().getTailItem("operid")
						.getValue();
			else
				// �б���ȡ��Ӧ�е�operid
				sBillOwnerID = (String) getCtBillListPanel().getHeadBillModel()
						.getValueAt(m_iId - 1, "operid");

			tempMVO.getParentVO().setAttributeValue("operid", sBillOwnerID);
			tempMVO.getParentVO()
					.setAttributeValue("coperatoridnow", m_sPkUser);
			// added by lirr 2009-04-21ҵ����־
			tempMVO.setOperatelogVO(log);

			nc.ui.pub.pf.PfUtilClient.processAction("DELETE", m_sBillType,
					getClientEnvironment().getDate().toString(), tempMVO,
					tempMVO);

			getCtBillListPanel().getHeadBillModel().removeRow(m_iId - 1);

			m_vBillVO.remove(m_iId - 1);
			// �ı䵥�ݣ�Table��Ҫ������������
			for (int i = 1; i < 6; i++)
				m_bIsNeedReInit[i] = true;
			m_iElementsNum--;

			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"4020pub", "UPP4020pub-000057")/* @res "��ͬɾ���ɹ���" */);

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
					// ִ�й�ʽ
					getCtBillCardPanel().getBillModel().execLoadFormula();
					// ��id�Ÿ����ݿ�Ƭ�ı�ͷԪ��
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
					"4020pub", "UPP4020pub-000058")/* @res "��ͬɾ��ʧ�ܣ�" */);
			MessageDialog.showHintDlg(this, null, nc.ui.ml.NCLangRes
					.getInstance().getStrByID("4020pub", "UPP4020pub-000201")/*
																				 * @res
																				 * "��ͬɾ��ʧ�ܣ���ˢ�º��������ԣ�"
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
	 * �˴����뷽��˵���� �������ڣ�(2001-8-28 16:29:04)
	 */
	protected void onDelLine() {
		/**
		 * ��������ֶε�ֵ �����ۼ�ִ�������������ۼ�ִ�н��ڳ�������������ڳ�������� �ڳ�����Ʊ�������ڳ�����Ʊ���ڳ��ظ���
		 * ���ϲ�Ϊ�ջ�Ϊ����������ô���Ͳ���ɾ�С�
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
			// modified by lirr 2008-12-15 �����ͬ���Ѿ�����������ɾ������
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
					// �ճ����ڳ���Ҫ���
					for (int j = 0; j < iCheckNameLen; j++) {
						obj = getCtBillCardPanel().getBodyValueAt(row[i],
								saCheckName[j]);
						// added by lirr 2008-12-15 �����ͬ���Ѿ�����������ɾ������
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
					// ����ڳ���
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
																	 * "�����п��ܱ��������û������ڳ�����:"
																	 */).append("\n");
				} else {
					err.append(
							nc.ui.ml.NCLangRes.getInstance().getStrByID(
									"4020pub", "UPP4020pub-000061")/*
																	 * @res
																	 * "�����п��ܱ���������:"
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
																	 * "���ܱ�ɾ��!"
																	 */);

				MessageDialog.showErrorDlg(this, null, err.toString());
				return;
			}
		}
		if (!getCtBillCardPanel().delLine()) {
			showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"SCMCOMMON", "UPPSCMCommon-000172")/* @res "û��ѡ����!" */);
		}
		// setButtonState();
		if (!m_bAddFromBillFlag) {
			setButtonState();
		} else {
			setButtonStateForCof();
		}
	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2001-10-27 16:56:00)
	 */
	protected void onDelTbLine() {

		String sMessage = nc.ui.ml.NCLangRes.getInstance().getStrByID(
				"SCMCOMMON", "UPPSCMCommon-000172")/* @res "û��ѡ����!" */;
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
		default: // ���ҳǩ
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
	 * �˴����뷽��˵���� �������ڣ�(2001-8-29 10:28:29) modified by lirr 2008-07-24
	 * ����m_bAddFromBillFlag���ж�
	 */
	protected void onEdit() {

		if (!m_bIsCard) // ������ڵ����б�ģʽ�£����л�����Ƭģʽ��
			onList();
		// added by lirr 2009-9-25����11:05:11 �Ƿ���Ҫ�ϼ�
		boolean isneedcal = getCtBillCardPanel().getBillModel(CTTableCode.BASE)
				.isNeedCalculate();
		getCtBillCardPanel().getBillModel(CTTableCode.BASE).setNeedCalculate(
				false);

		showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4020pub",
				"UPP4020pub-000062")/* @res "�޸ĵ�ǰ��ͬ��Ȼ����Ե�������桱��ť���б���" */);
		CTTool tool = new CTTool();
		Object[] objAssunit = tool.getInvids(getCurVO());
		// �޸�ʱ��: 2009-09-10 �޸���: wuwp �޸���: ����ָ���쳣���Ӷ�Ӱ���˰�ť��״̬
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
						// TODO �Զ����� catch ��
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
				// ��д�����ͷVO����
				getCtBillCardPanel().setHeadItem("pk_ct_manage",
						curVO.getParentVO().getPrimaryKey().toString());
				// ��д�������VO����
				for (int i = 0; i < curItemvo.length; i++) {
					getCtBillCardPanel().setBodyValueAt(
							curItemvo[i].getPrimaryKey().toString(), i,
							"pk_ct_manage_b");
					getCtBillCardPanel().setBodyValueAt(
							curItemvo[i].getPk_ct_manage().toString(), i,
							"pk_ct_manage");
				}

				// �ж��Ƿ�������Ƶ���
				if (m_isSaveInitOper.booleanValue() == false) {
					getCtBillCardPanel().setTailItem("opername", m_sPkUser);
					getCtBillCardPanel().setTailItem("operid", m_sPkUser);
				}
			} catch (Exception e) {
				SCMEnv.out("��д����VOʱ�׳��쳣��"); /*-=notranslate=-*/
				nc.vo.scm.pub.SCMEnv.out(e);
			}

		} else {
			curVO = (ExtendManageVO) m_vBillVOForRef.elementAt(m_iId - 1);
			curItemvo = (ManageItemVO[]) curVO.getTableVO("table");
		}

		try {
			getCtBillCardPanel().setEnabled(true);
			// modified by lirr 2008-10-08 ת������ ��ͬ����Ӧ�����޸�

			if (!m_bAddFromBillFlag) {
				// ��ͬ���벻���޸�
				getCtBillCardPanel().getHeadItem("ct_code").setEnabled(false);
			} else {
				getCtBillCardPanel().getHeadItem("ct_code").setEnabled(true);
			}
			/*
			 * // ��ͬ���벻���޸�
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
				// modified by lirr 2008-11-22 �޸�ԭ�򣺱����������·�ʽ
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
	 * �˴����뷽��˵���� �������ڣ�(2001-10-24 9:55:57)
	 */
	protected void onEditTbLine() {
		getCtBillCardPanel().setEnabled(true);
		BillItem[] headitem = getCtBillCardPanel().getHeadItems();
		for (int i = 0; i < headitem.length; i++)
			headitem[i].setEnabled(false);
		// int selectedRow= -1;
		switch (m_iTabbedFlag) {
		// modified by lirr 2009-7-9����03:46:35 ���޸�ҳǩǰupdateValue()

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
		default: // ���ҳǩ

			int rowCount = getCtBillCardPanel().getBillModel("history")
					.getRowCount();
			if (rowCount <= 1) {
				showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"4020pub", "UPP4020pub-000064")/* @res "�����ʷĿǰ�����޸�." */);
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
		// added by lirr 2009-7-13����10:59:20

		getCtBillCardPanel()
				.getBodyPanel(CTTableCode.CT_TABCODE[m_iTabbedFlag])
				.setBBodyMenuShow(false);

	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2001-9-19 15:56:57)
	 */
	protected void onFreeze() {

		String sMessage = null;

		try {

			ManageVO tempMVO = new ManageVO();
			ManageVO mVO = getCurVO();
			tempMVO = mVO.clone(mVO);
			int ctState = Integer.parseInt(tempMVO.getParentVO()
					.getAttributeValue("ctflag").toString());

			if (ctState == BillState.VALIDATE) { // ��Ч״̬
				sMessage = nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"4020pub", "UPP4020pub-000065")/* @res "�Ƿ�ȷ��Ҫ����ú�ͬ��" */;

				if (MessageDialog.showYesNoDlg(this, null, sMessage) == 4) {
					tempMVO.getParentVO().setAttributeValue("ctflag",
							new Integer(BillState.FREEZE));

					// ��ǰ����Ա
					tempMVO.getParentVO().setAttributeValue("coperatoridnow",
							getOperator());

					tempMVO.setOldBillStatus(BillState.VALIDATE);
					tempMVO.setBillStatus(BillState.FREEZE);

					m_sExecFlag = CTExecFlow.FREEZE; // "����";
					// added by lirr 2009-9-14����02:45:29 ����ִ�й��̵Ķ�����ԭ��
					CTTool.setExecReason(tempMVO, this);
					ArrayList alRet = null;

					// ManageBO_Client.update(tempMVO);
					alRet = (ArrayList) nc.ui.pub.pf.PfUtilClient
							.processAction(
									"FREEZE",
									m_sBillType,
									getClientEnvironment().getDate().toString(),
									tempMVO, tempMVO);

					// m_sExecFlag = CTExecFlow.FREEZE; // "����";
					// createExec(mVO); // ����ִ�й��̼�¼

					m_bIsNeedReInit[TabState.EXEC] = true; // ִ�й��̸��£���������������

					mVO.getParentVO().setAttributeValue("ctflag",
							new Integer(BillState.FREEZE));
					getCtBillCardPanel().setHeadItem("ctflag",
							new Integer(BillState.FREEZE));
					getCtBillListPanel().getHeadBillModel().setValueAt(
							nc.ui.ml.NCLangRes.getInstance().getStrByID(
									"4020const", BillState.STATERESID_FREEZE),
							m_iId - 1, "ctflag");
					// modified by lirr 2009-8-24����10:42:00 ˢ��ts�����ߺ�̨���ڶ����з���
					if (alRet != null && alRet.size() > 0)
						freshStatusTs(mVO, (String) alRet.get(1));
					ExtendManageVO voCur = (ExtendManageVO) m_vBillVO
							.get(m_iId - 1);
					voCur.setParentVO(mVO.getParentVO());
					// modified by lirr 2009-9-14����03:37:37
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
			} else { // ״̬��һ��
				MessageDialog.showWarningDlg(this, null, nc.ui.ml.NCLangRes
						.getInstance().getStrByID("SCMCOMMON",
								"UPPSCMCommon-000329")/*
														 * @res
														 * "״̬���������²�ѯ���ٲ�����"
														 */);
			}

		} catch (java.sql.SQLException e) {
			MessageDialog.showErrorDlg(this,
					nc.ui.ml.NCLangRes.getInstance().getStrByID("4020pub",
							"UPP4020pub-000066")/* @res "���ݿ��������" */, e
							.getMessage());

		} catch (java.rmi.RemoteException e) {
			MessageDialog.showErrorDlg(this,
					nc.ui.ml.NCLangRes.getInstance().getStrByID("4020pub",
							"UPP4020pub-000067")/* @res "Զ�̵���ʧ��" */, e
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
	 * �˴����뷽��˵���� �������ڣ�(2001-9-6 17:22:38) modified by lirr 2008-07-23
	 * �����빺�����۸����������ɺ�ͬʱ�������б�õ�VO���������ݣ�ѡ��һ����Ƭ��ʾ����ԭ�����б𡪡�>��Ƭ��ͬ
	 */
	protected void onList() {
		// �Ƿ񱣴洦��
		try {
			if (haveQuestion()) {

				getCtBillCardPanel().setEnabled(false);
				m_iBillState = OperationState.FREE;

				// ��ҳǩת�Ƶ�"��ͬ����"��.
				getCtBillCardPanel().getBodyTabbedPane().setSelectedIndex(
						TabState.BILL);
				getCtBillListPanel().getBodyTabbedPane().setSelectedIndex(
						TabState.BILL);
				m_iTabbedFlag = TabState.BILL;

				if (m_bIsCard) { // ��Ƭ --���б�
					m_bIsCard = false;
					getButtonTree().getButton(CTButtonConst.BTN_SWITCH)
							.setName(
									nc.ui.ml.NCLangRes.getInstance()
											.getStrByID("common", "UCH021")/*
																			 * @res
																			 * "��Ƭ��ʾ"
																			 */);
					getButtonTree().getButton(CTButtonConst.BTN_SWITCH)
							.setHint(
									nc.ui.ml.NCLangRes.getInstance()
											.getStrByID("common", "UCH021")/*
																			 * @res
																			 * "��Ƭ��ʾ"
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

				} else { // �б� --����Ƭ

					m_bIsCard = true;
					getButtonTree().getButton(CTButtonConst.BTN_SWITCH)
							.setName(
									nc.ui.ml.NCLangRes.getInstance()
											.getStrByID("common", "UCH022")/*
																			 * @res
																			 * "�б���ʾ"
																			 */);
					getButtonTree().getButton(CTButtonConst.BTN_SWITCH)
							.setHint(
									nc.ui.ml.NCLangRes.getInstance()
											.getStrByID("common", "UCH022")/*
																			 * @res
																			 * "�б���ʾ"
																			 */);

					if (m_iId > 0) {

						if (m_bAddFromBillFlag) {// �Ƿ������������(�빺�����۸�������)���ɺ�ͬ
							ExtendManageVO mVOForRef = (ExtendManageVO) m_vBillVOForRef
									.elementAt(m_iId - 1);
							m_iBillState = OperationState.ADD;

							getCtBillCardPanel().addNew();
							// �����Զ�����
							getCtBillCardPanel().setAutoAddEditLine(true);
							// ��������Ҽ���ɾ��
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
							voTemp.setChangeBb1s(null); // ��ͬ���
							voTemp.setExpBb3s(null);
							voTemp.setManageExecs(null);
							voTemp.setMemoraBb2s(null);

							// �����Ļ�д����
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
							// added by lirr 2009-9-8����04:31:28 ��������������������͵�item
							CTTool.processNameItems(getCtBillCardPanel(),
									voTemp);
							getCtBillCardPanel().setBillValueVO(voTemp);

							getCtBillCardPanel().resetAllRowNo();

							// �ı䵥�ݣ�Table��Ҫ������������
							for (int i = 1; i < 6; i++)
								m_bIsNeedReInit[i] = true;
							getCtBillCardPanel().setEnabled(true);

						} else {
							ExtendManageVO mVO = (ExtendManageVO) m_vBillVO
									.elementAt(m_iId - 1);
							String currid = ((ManageHeaderVO) mVO.getParentVO())
									.getCurrid();

							// ��ô˱��ֵ�С��λ��
							int currDigit = 2;
							try {
								Integer integer = (Integer) m_hCurrDigit
										.get(currid);
								if (integer != null)
									currDigit = integer.intValue();

							} catch (Exception e) {
								currDigit = 2;
							}

							// ����ԭ�ҽ��С��λ��
							getCtBillCardPanel().getBodyItem(CTTableCode.BASE,
									"orisum").setDecimalDigits(currDigit);
							getCtBillCardPanel().getBodyItem(CTTableCode.BASE,
									"oritaxmny").setDecimalDigits(currDigit);
							getCtBillCardPanel().getBodyItem(CTTableCode.BASE,
									"oritaxsummny").setDecimalDigits(currDigit);

							// added by lirr 2009-7-23 ����03:30:09 �ۼ��ո���� �ܶ��
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
							// //����ִ���ۼƱ��Ҽ�˰�ϼ�

							// ��ͬ����ҳǩ�н��ľ���
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
							// modified by lirr 2008-12-24 �۱����ʾ����·��� since v55
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
						// ִ�й�ʽ
						getCtBillCardPanel().getBillModel().execLoadFormula();
						// ��id�Ÿ����ݿ�Ƭ�ı�ͷԪ��
						setIdtoName();
						getCtBillCardPanel().setAutoFiltNullValueRow(false);
						getCtBillCardPanel().updateValue();
						// �ϼ��б��
						getCtBillCardPanel().getTotalTableModel().setValueAt(
								nc.ui.ml.NCLangRes.getInstance().getStrByID(
										"common", "UC000-0001146")/*
																	 * @res "�ϼ�"
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
	 * �˴����뷽��˵���� �������ڣ�(2001-8-29 14:15:36)
	 */
	protected void onNext() {
		// �Ƿ񱣴洦��
		if (haveQuestion()) {
			// ��ҳǩת�Ƶ�"��ͬ����"��.
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

				// ��ô˱��ֵ�С��λ��
				// ��ô˱��ֵ�С��λ��
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
				// ����ԭ�ҽ��С��λ��
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
				// modified by lirr 2008-12-24 �۱����ʾ����·��� since v55
				getCtBillCardPanel().getHeadItem("currrate").setDecimalDigits(
						getCurrateDigit(currid));

				/*
				 * getCtBillCardPanel().getHeadItem("astcurrate")
				 * .setDecimalDigits(m_iRateDigit[1]);
				 */

				getCtBillCardPanel().setBillValueVO(voExtend);

				// ��Id�������
				setIdtoName();
				// ִ�й�ʽ
				getCtBillCardPanel().getBillModel().execLoadFormula();

				getCtBillCardPanel().updateValue();
				// �ϼ��б��
				getCtBillCardPanel().getTotalTableModel().setValueAt(
						nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
								"UC000-0001146")/* @res "�ϼ�" */, 0, 0);

				m_bChangeFlag = false; // ��־��ͬ���ɱ��
				// �ı䵥�ݣ�Table��Ҫ������������
				for (int i = 1; i < 6; i++)
					m_bIsNeedReInit[i] = true;
				if (m_iTabbedFlag != TabState.BILL) { // ��������ҳǩ
					loadData();
					m_bIsNeedReInit[m_iTabbedFlag] = false;
				}

				// tableOperaState = "����";
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
	 * �˴����뷽��˵���� �������ڣ�(2001-8-29 14:16:37)
	 */
	protected void onPre() {
		// �Ƿ񱣴洦��
		if (haveQuestion()) {
			// ��ҳǩת�Ƶ�"��ͬ����"��.
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

				// ��ô˱��ֵ�С��λ��
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

				// ����ԭ�ҽ��С��λ��
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
				// modified by lirr 2008-12-24 �۱����ʾ����·��� since v55
				getCtBillCardPanel().getHeadItem("currrate").setDecimalDigits(
						getCurrateDigit(currid));
				/*
				 * getCtBillCardPanel().getHeadItem("astcurrate")
				 * .setDecimalDigits(m_iRateDigit[1]);
				 */

				getCtBillCardPanel().setBillValueVO(voExtend);

				// getCtBillCardPanel().setBillValueVO(mVO);

				// ��Id�������
				setIdtoName();
				// ִ�й�ʽ
				getCtBillCardPanel().getBillModel().execLoadFormula();

				getCtBillCardPanel().updateValue();
				// �ϼ��б��
				getCtBillCardPanel().getTotalTableModel().setValueAt(
						nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
								"UC000-0001146")/* @res "�ϼ�" */, 0, 0);

				m_bChangeFlag = false; // ��־��ͬ���ɱ��
				// �ı䵥�ݣ�����Table����Ҫ������������
				for (int i = 1; i < 6; i++)
					m_bIsNeedReInit[i] = true;
				if (m_iTabbedFlag != TabState.BILL) { // ��������ҳǩ
					loadData();
					m_bIsNeedReInit[m_iTabbedFlag] = false;

				}
				// tableOperaState = "����";
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
	 * �õ�����ģ���б�ͷ/��β��ʽ. �۱� on Jun 17, 2005
	 */
	protected String[] getHeadTailFormulas() {
		if (m_headTailFormulas == null) {
			// ��ͬ��ͷ&����
			BillItem[] headTailItems = getCtBillCardPanel().getBillData()
					.getHeadTailItems();
			m_headTailFormulas = BillUtil.getFormulas(headTailItems,
					IBillItem.LOAD);
		}

		return m_headTailFormulas;
	}

	/*
	 * �õ�����ģ���б���ĺ�ͬ������ʽ. �۱� on Jun 17, 2005
	 */
	protected String[] getTableFormulas() {
		if (m_tableFormulas == null) {
			// ��ͬ����
			BillItem[] tableItems = getCtBillCardPanel().getBillData()
					.getBodyItemsForTable(CTTableCode.BASE);
			m_tableFormulas = BillUtil.getFormulas(tableItems, IBillItem.LOAD);
		}

		return m_tableFormulas;
	}

	/*
	 * �õ�����ģ���б���ĺ�ͬ���ʽ. �۱� on Jun 17, 2005
	 */
	protected String[] getTermFormulas() {
		if (m_termFormulas == null) {
			// ��ͬ����
			BillItem[] termItems = getCtBillCardPanel().getBillData()
					.getBodyItemsForTable(CTTableCode.TERM);
			m_termFormulas = BillUtil.getFormulas(termItems, IBillItem.LOAD);
		}

		return m_termFormulas;
	}

	/*
	 * �õ�����ģ���б���ĺ�ͬ���ù�ʽ. �۱� on Jun 17, 2005
	 */
	protected String[] getCostFormulas() {
		if (m_costFormulas == null) {
			// ��ͬ����
			BillItem[] costItems = getCtBillCardPanel().getBillData()
					.getBodyItemsForTable(CTTableCode.COST);
			m_costFormulas = BillUtil.getFormulas(costItems, IBillItem.LOAD);
		}

		return m_costFormulas;
	}

	/*
	 * ��������: 1 �Բ�ѯ�����Ľ��ִ�й�ʽ.(main function) 2 ����ѯ�������m_vBillVO. �۱� on Jun 17,
	 * 2005
	 */
	protected void execFormularAfterQuery(ManageVO[] arrMangevos) {
		// ������б�ͷvo
		CircularlyAccessibleValueObject[] headVosAll = new CircularlyAccessibleValueObject[arrMangevos.length];
		CircularlyAccessibleValueObject[] tableVOsAll = null;
		CircularlyAccessibleValueObject[] termVOsAll = null;

		ArrayList alTableVOs = new ArrayList(); // ���б���vo
		ArrayList alTermVOs = new ArrayList(); // ��������vo

		int index = 0;

		ExtendManageVO[] arrExtendVO = new ExtendManageVO[arrMangevos.length];
		for (int k = 0; k < arrMangevos.length; k++) {
			arrExtendVO[k] = new ExtendManageVO();

			arrExtendVO[k].setParentVO(arrMangevos[k].getParentVO());
			headVosAll[k] = arrMangevos[k].getParentVO();

			// modified by liuzy 2008-04-17 V5.03��ѯ���죬ֻ���ص�һ����ͬ�ı���������ҳǩ
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
		// ִ�й�ʽ
		getCtBillListPanel().getBodyBillModel().execFormulasWithVO(headVosAll,
				getHeadTailFormulas());
		
		// ��ͬ����
		tableVOsAll = new CircularlyAccessibleValueObject[alTableVOs.size()];
		alTableVOs.toArray(tableVOsAll);

		// ���������
		/*
		 * for (int k = 0; k < tableVOsAll.length; k++) { FreeVO voFree =
		 * InvTool.getInvFreeVO((String) tableVOsAll[k]
		 * .getAttributeValue("invid")); // ����������� if (voFree != null) {
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
		// modified by lirr 2009-8-20����07:24:22 ����������
		nc.ui.scm.pub.FreeVOParse freeVOParse = new nc.ui.scm.pub.FreeVOParse();
		freeVOParse.setFreeVO(tableVOsAll, null, "invid", false);
		// ��ͬ����ִ�й�ʽ
		getCtBillListPanel().getBodyBillModel().execFormulasWithVO(tableVOsAll,
				getTableFormulas());

		if (alTermVOs.size() > 0) {
			termVOsAll = new CircularlyAccessibleValueObject[alTermVOs.size()];
			alTermVOs.toArray(termVOsAll);
			getCtBillListPanel().getBodyBillModel().execFormulasWithVO(
					termVOsAll, getTermFormulas());
		}

		// ��ͬ�����ӱ���ִ�й�ʽ
		// ԭ�򣺺�ͬ�����ӱ��е�pk_ct_exp�Ǻ�ͬ���ö����е�pk_ct_expset������֮�����ϵloadformula���ܸ�����������������ϵ��
		// shaobing on Jul 4, 2005
		/** ***��ѯ��ʽִ�н���**** */
	}

	/*
	 * ��ȡ�߼����Ĳ�ѯ���� �� ���ݵ�һЩ���в�ѯ����
	 */
	public String getLogConditonSQLForQryExecImprest(
			nc.vo.pub.query.ConditionVO[] conVO) {
		if (conVO == null || conVO.length <= 0)
			return null;
		// modified by lirr 2008-11-27 �޸�ԭ�����like % ��DB2�ⱨ��
		ArrayList<ConditionVO> alist = new ArrayList<ConditionVO>(conVO.length);

		// �����ͬ״̬
		for (int i = 0; i < conVO.length; i++) {

			if (conVO[i].getFieldCode().equals("ct_manage.ctflag")
					&& "%".equals(conVO[i].getValue()))
				continue;

			/*
			 * if (conVO[i].getFieldCode().equals("ct_manage.ctflag")) { String
			 * sValue = conVO[i].getValue(); // modified by lirr 2008-11-27
			 * �޸�ԭ�����like % ��DB2�ⱨ��
			 * 
			 * 
			 * if ("%".equals(sValue)) { conVO[i].setOperaCode("like"); } }
			 */
			// modified by liuzy 2008-03-26 �̹�԰����Ĭ�ϲ�ѯ����
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
	 * ��ȡ��ͬ�����в�ѯ���� ���� �߼���弰���ݹ��е�����
	 */
	public String getConditonSQLForQryExecImprest() {
		return QueryDlgUtil.andTowWhere(getQueryConditionDlg().getWhereSQL(),
				getLogConditonSQLForQryExecImprest(getQueryConditionDlg()
						.getLogicalConditionVOs()));
	}

	/*
	 * ��ȡ�߼����Ĳ�ѯ���� �� ���ݵ�һЩ���в�ѯ����
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

				// ���ô���������µĲ�ѯģ�岻�ô������

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
				// modified by liuzy 2008-03-26 �̹�԰����Ĭ�ϲ�ѯ����
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
						"UPP4020pub-000268")/* ���Ƶ����ڴӡ��롾�Ƶ����ڵ���ͬΪ�����ѯ���� */);
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

		// ���Ӻ�ͬ���ͼ��Ƿ��ڳ�������
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

		// �ӹ�˾����
		sbWhere.append(" and ct.pk_corp = '" + m_sPkCorp + "'");
		// modified by liuzy 2008-04-11 ���ӱ��塢��ͬ���͹�˾��ѯ����
		// sbWhere.append(" and ct_b.pk_corp = '" + m_sPkCorp + "'");
		sbWhere.append(" and ct_type.pk_corp = '" + m_sPkCorp + "'");

		// ��¼��ѯ�������Ա�ˢ��ʹ��
		return sbWhere.toString();
	}

	/*
	 * ��ȡ��ͬ�����в�ѯ���� ���� �߼���弰���ݹ��е�����
	 */
	protected String getConditonSQL() {

		String sWhereSQL = QueryDlgUtil.andTowWhere(getQueryConditionDlg()
				.getWhereSQL(), getLogConditonSQL(getQueryConditionDlg()
				.getLogicalConditionVOs()));

		// ��¼��ѯ�������Ա�ˢ��ʹ��
		m_sQryCondition = sWhereSQL;

		return sWhereSQL;
	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2001-9-10 13:36:31)
	 */
	protected void onQuery() {

		if (haveQuestion()) {

			// ��ҳǩת�Ƶ�"��ͬ����"��.
			getCtBillCardPanel().getBodyTabbedPane().setSelectedIndex(
					TabState.BILL);
			getCtBillListPanel().getBodyTabbedPane().setSelectedIndex(
					TabState.BILL);
			m_iTabbedFlag = TabState.BILL;

			getCtBillCardPanel().setEnabled(false); // ���ݿ�Ƭ���ɱ༭
			m_iBillState = OperationState.FREE;

			// ��ʾ��ѯ�Ի���
			getQueryConditionDlg().showModal();

			if (getQueryConditionDlg().getResult() != nc.ui.pub.beans.UIDialog.ID_OK)
				return;

			try {
				m_timer.start("��ͬ��ѯ��ʼ"); /*-=notranslate=-*/
				showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"4020pub", "UPP4020pub-000069")/* @res "��ȴ������ڲ�ѯ��ͬ����" */);

				// nc.vo.pub.query.ConditionVO[] conVO = getQueryConditionDlg()
				// .getConditionVO();

				// �õ���ѯ����
				String sWhereSQL = getConditonSQL();

				// modified by liuzy 2008-04-09 ��ͬ��ѯ���ڿ�ȹ����·�����down��
				if (null == sWhereSQL) {
					showHintMessage("");
					return;
				}

				ManageVO[] arrMangevos = loadHeadData(m_sPkCorp, sWhereSQL);

				// ��ѯ
				// ManageVO[] arrMangevos = ContractQueryHelper.queryBill(
				// m_sPkCorp, sWhereSQL);

				// ��ջ���vo��
				m_vBillVO.clear();

				if (arrMangevos == null || arrMangevos.length == 0) { // ��ѯ���Ϊ��
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
																		 * "û���ҵ���������������"
																		 */);

					return;
				}
				// ���ع�ʽ
				execFormularAfterQuery(arrMangevos);

				if (m_vBillVO != null && m_vBillVO.size() > 0) {

					// �ж����ڿ�Ƭģʽ�£��������б�ģʽ��
					m_bIsFirstClick = true; // ��־�ǵ�һ�ε��
					m_iId = 1;
					m_iElementsNum = m_vBillVO.size();

					if (m_bIsCard) { // ��Ƭ
						// �л����б�ģʽ��
						onList();

					} else { // �б�
						// ���л���ǰ���б�ģʽ����ʾ��ѯ���
						//setListRateDigit();
						// ��ʾ�����б�
						setHeaderListData();
						getCtBillListPanel().getHeadTable()
								.setRowSelectionInterval(0, 0);

					}

					m_bChangeFlag = false; // ��־��ͬ���ɱ��
					// ����Table����Ҫ������������
					for (int i = 1; i < 6; i++)
						m_bIsNeedReInit[i] = true;

					// setCardVOData(); //���³�ʼ��Ƭ����

					/* @res "��ѯ���������鵽{0}����ͬ��" */
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
																		 * "δ�鵽���������ĺ�ͬ��"
																		 */;
					showHintMessage(sMessage);
					MessageDialog.showHintDlg(this, null, sMessage);
				}
				m_timer.showExecuteTime("��ͬ��ѯ������"); /*-=notranslate=-*/

			} catch (Exception e) {
				nc.vo.scm.pub.SCMEnv.out(e);
				MessageDialog.showHintDlg(this, null, nc.ui.ml.NCLangRes
						.getInstance().getStrByID("4020pub",
								"UPP4020pub-000073")/* @res "��ͬ��ѯ����" */
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
	 * �˴����뷽��˵���� �������ڣ�(2001-8-24 14:24:22)
	 */
	// �޸�˵�����޸ģ���sava�Լ�Ƕ�׵ı��淽������������void��Ϊboolean �޸��ˣ���ѧ�� �޸����ڣ�2007-10-31
	protected boolean onSave() {
		Boolean result = false;
		switch (m_iTabbedFlag) {
		case TabState.BILL: // ��ͬ����ҳǩ
			if (m_iBillState == OperationState.CHANGE)
				result = saveChangedCT(); // �������
			else
				result = saveManage(); // �޸ı���
			break;

		case TabState.TERM: // ����ҳǩ
			result = saveTerm();
			// modified by liuzy 2007-12-17 �ÿ�ݼ���������༭��Item��Ȼ�������㣬�ֹ����ý���
			getCtBillCardPanel().transferFocusTo(BillCardPanel.BODY);
			break;

		case TabState.EXP: // ����ҳǩ
			result = saveExp();
			// modified by liuzy 2007-12-17 �ÿ�ݼ���������༭��Item��Ȼ�������㣬�ֹ����ý���
			getCtBillCardPanel().transferFocusTo(BillCardPanel.BODY);
			break;

		case TabState.NOTE: // ���¼�ҳǩ
			result = saveNotes();
			// modified by liuzy 2007-12-17 �ÿ�ݼ���������༭��Item��Ȼ�������㣬�ֹ����ý���
			getCtBillCardPanel().transferFocusTo(BillCardPanel.BODY);
			break;

		case TabState.CHANGE: // ���ҳǩ
			result = saveChange();
		}
		// setButtonState();
		// added by lirr 2008-08-07 ����ǲ������ɺ�ͬ������������ť�ͼ۸����������ɺ�ͬ����ť״̬��һ��
		if (m_bAddFromBillFlag) {
			setButtonStateForCof();
		} else {
			setButtonState();
		}
	// added by lirr 2009-12-2����09:56:12���涯�������getDmdo().setCellControls ���������������������������� ����Խ��
    getCtBillCardPanel().getDmdo().setCellControls(null);
    
		return result;
	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2001-9-19 15:57:11)
	 */
	protected void onTerminate() {

		String sMessage = null;

		try {

			ManageVO tempMVO = new ManageVO();
			ManageVO mVO = getCurVO();
			tempMVO = mVO.clone(mVO);
			int ctState = Integer.parseInt(tempMVO.getParentVO()
					.getAttributeValue("ctflag").toString());

			// �Դ��ڲ�ͬ״̬�ĺ�ͬ���в�ͬ�Ĵ���
			if (ctState == BillState.VALIDATE) { // ��Ч״̬
				sMessage = nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"4020pub", "UPP4020pub-000074")/* @res "�Ƿ�ȷ��Ҫ��ֹ�ú�ͬ��" */;

				if (MessageDialog.showYesNoDlg(this, null, sMessage) == 4) {
					tempMVO.getParentVO().setAttributeValue("ctflag",
							new Integer(BillState.TERMINATE));

					// ��ǰ����Ա
					tempMVO.getParentVO().setAttributeValue("coperatoridnow",
							getOperator());

					tempMVO.getParentVO().setAttributeValue("actualinvalidate",
							m_UFToday);

					tempMVO.setOldBillStatus(BillState.VALIDATE);
					tempMVO.setBillStatus(BillState.TERMINATE);
					m_sExecFlag = CTExecFlow.TERMINATE; // "ʵ����ֹ";
					// added by lirr 2009-9-14����02:45:29 ����ִ�й��̵Ķ�����ԭ��
					CTTool.setExecReason(tempMVO, this);
					ArrayList alRet = null;
					// ManageBO_Client.update(tempMVO);
					alRet = (ArrayList) nc.ui.pub.pf.PfUtilClient
							.processAction("TERMINATE", m_sBillType, m_UFToday
									.toString(), tempMVO, tempMVO);

					// m_sExecFlag = CTExecFlow.TERMINATE; // "ʵ����ֹ";
					// createExec(mVO); // ����ִ�й��̼�¼

					m_bIsNeedReInit[TabState.EXEC] = true; // ִ�й��̸��£���������������

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
					// modified by lirr 2009-8-24����10:42:00 ˢ��ts�����ߺ�̨���ڶ����з���
					if (alRet != null && alRet.size() > 0)
						freshStatusTs(mVO, (String) alRet.get(1));

					ExtendManageVO voCur = (ExtendManageVO) m_vBillVO
							.get(m_iId - 1);
					voCur.setParentVO(mVO.getParentVO());
					// voCur.setTableVO("exec", mVO.getManageExecs());
					// modified by lirr 2009-9-14����03:38:19
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
			} else { // ״̬��һ��
				MessageDialog.showWarningDlg(this, null, nc.ui.ml.NCLangRes
						.getInstance().getStrByID("SCMCOMMON",
								"UPPSCMCommon-000329")/*
														 * @res
														 * "״̬���������²�ѯ���ٲ�����"
														 */);
			}

		} catch (java.sql.SQLException e) {
			MessageDialog.showErrorDlg(this,
					nc.ui.ml.NCLangRes.getInstance().getStrByID("4020pub",
							"UPP4020pub-000066")/* @res "���ݿ��������" */, e
							.getMessage());

		} catch (BusinessException e) {
			MessageDialog.showErrorDlg(this, null, e.getMessage());
			reportException(e);

		} catch (java.rmi.RemoteException e) {
			MessageDialog.showErrorDlg(this,
					nc.ui.ml.NCLangRes.getInstance().getStrByID("4020pub",
							"UPP4020pub-000067")/* @res "Զ�̵���ʧ��" */, e
							.getMessage());

		} catch (Exception e) {
			MessageDialog.showErrorDlg(this, null, e.getMessage());
			reportException(e);

		}
		setButtonState();
	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2001-10-24 14:40:45)
	 */
	protected void onUnFreeze() {
		String sMessage = null;
		try {
			ManageVO tempMVO = new ManageVO();
			ManageVO mVO = getCurVO();
			tempMVO = mVO.clone(mVO);
			int ctState = Integer.parseInt(tempMVO.getParentVO()
					.getAttributeValue("ctflag").toString());

			// �Դ��ڲ�ͬ״̬�ĺ�ͬ���в�ͬ�Ĵ���
			if (ctState == BillState.FREEZE) { // �ⶳ״̬

				sMessage = nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"4020pub", "UPP4020pub-000075")/* @res "�Ƿ�ȷ��Ҫ�ⶳ�ö����ͬ��" */;

				if (MessageDialog.showYesNoDlg(this, null, sMessage) == 4) {

					tempMVO.getParentVO().setAttributeValue("ctflag",
							new Integer(BillState.VALIDATE));
					// ��ǰ����Ա
					tempMVO.getParentVO().setAttributeValue("coperatoridnow",
							getOperator());

					tempMVO.setOldBillStatus(BillState.FREEZE);
					tempMVO.setBillStatus(BillState.VALIDATE);
					m_sExecFlag = CTExecFlow.UNFREEZE; // "�ⶳ";
					// added by lirr 2009-9-14����02:45:29 ����ִ�й��̵Ķ�����ԭ��
					CTTool.setExecReason(tempMVO, this);

					ArrayList alRet = null;
					// ManageBO_Client.update(tempMVO);
					alRet = (ArrayList) nc.ui.pub.pf.PfUtilClient
							.processAction(
									"UNFREEZE",
									m_sBillType,
									getClientEnvironment().getDate().toString(),
									tempMVO, tempMVO);

					// m_sExecFlag = CTExecFlow.UNFREEZE; // "�ⶳ";
					// createExec(mVO); // ����ִ�й��̼�¼

					m_bIsNeedReInit[TabState.EXEC] = true; // ִ�й��̸��£���������������

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
					// modified by lirr 2009-8-24����10:42:00 ˢ��ts�����ߺ�̨���ڶ����з���
					if (alRet != null && alRet.size() > 0)
						freshStatusTs(mVO, (String) alRet.get(1));
					ExtendManageVO voCur = (ExtendManageVO) m_vBillVO
							.get(m_iId - 1);
					voCur.setParentVO(mVO.getParentVO());
					// voCur.setTableVO("exec", mVO.getManageExecs());
					// modified by lirr 2009-9-14����03:38:19
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
			} else { // ״̬��һ��
				MessageDialog.showWarningDlg(this, null, nc.ui.ml.NCLangRes
						.getInstance().getStrByID("SCMCOMMON",
								"UPPSCMCommon-000329")/*
														 * @res
														 * "״̬���������²�ѯ���ٲ�����"
														 */);
			}

		} catch (java.sql.SQLException e) {
			MessageDialog.showErrorDlg(this,
					nc.ui.ml.NCLangRes.getInstance().getStrByID("4020pub",
							"UPP4020pub-000066")/* @res "���ݿ��������" */, e
							.getMessage());

		} catch (java.rmi.RemoteException e) {
			MessageDialog.showErrorDlg(this,
					nc.ui.ml.NCLangRes.getInstance().getStrByID("4020pub",
							"UPP4020pub-000067")/* @res "Զ�̵���ʧ��" */, e
							.getMessage());

		} catch (Exception e) {
			MessageDialog.showErrorDlg(this, null, e.getMessage());
			reportException(e);

		}
		setButtonState();
	}

	/**
	 * �˴����뷽��˵���� �����ʷֻ�����޸�. ������������ɾ����¼. �������ڣ�(2001-9-19 14:47:18)
	 */
	private boolean saveChange() {
		try {

			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"SCMCOMMON", "UPPSCMCommon-000134")/* @res "��ʼ���桭��" */);

			ChangeBb1VO[] aryNew = (ChangeBb1VO[]) (getCtBillCardPanel()
					.getBillModel("history").getBodyValueVOs(ChangeBb1VO.class
					.getName()));
			ChangeBb1VO[] arySave = aryNew;

			if (arySave != null && arySave.length > 0) {
				// �������ݿ�
				ContractWriterHelper.updateChangeBb1s(arySave, m_sPkUser);
				((ExtendManageVO) m_vBillVO.get(m_iId - 1)).setTableVO(
						"history", arySave);
				getCtBillCardPanel().getBillModel("history").updateValue();
			}

			m_iTbState = OperationState.FREE;
			getCtBillCardPanel().setEnabled(false);

			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"SCMCOMMON", "UPPSCMCommon-000006")/* @res "����ɹ�" */);

		} catch (ValidationException e) {
			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"4020pub", "UPP4020pub-000076")/* @res "�������" */);
			MessageDialog.showErrorDlg(this, null, e.getMessage());
			m_bErrFlag = true;
			return false;
		} catch (java.sql.SQLException e) {
			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"4020pub", "UPP4020pub-000077")/* @res "����ʧ�ܣ�" */);

			if (e.getErrorCode() == 1401) {
				MessageDialog.showErrorDlg(this, null, nc.ui.ml.NCLangRes
						.getInstance().getStrByID("4020pub",
								"UPP4020pub-000078")/*
													 * @res "����Ƿ����ַ���������̫�����ַ�����"
													 */);
			} else if (e.getErrorCode() == 8152) {
				MessageDialog.showErrorDlg(this, null, nc.ui.ml.NCLangRes
						.getInstance().getStrByID("4020pub",
								"UPP4020pub-000078")/*
													 * @res "����Ƿ����ַ���������̫�����ַ�����"
													 */);
			} else
				MessageDialog.showErrorDlg(this, null, e.getMessage());

			m_bErrFlag = true;

			return false;

		} catch (java.rmi.RemoteException e) {
			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"4020pub", "UPP4020pub-000077")/* @res "����ʧ�ܣ�" */);
			MessageDialog.showErrorDlg(this,
					nc.ui.ml.NCLangRes.getInstance().getStrByID("4020pub",
							"UPP4020pub-000067")/* @res "Զ�̵���ʧ��" */, e
							.getMessage());

			m_bErrFlag = true;
			return false;
		} catch (Exception e) {
			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"4020pub", "UPP4020pub-000077")/* @res "����ʧ�ܣ�" */);
			MessageDialog.showErrorDlg(this, null, e.getMessage());
			reportException(e);
			m_bErrFlag = true;
			return false;
		}
		return true;
	}

	/**
	 * �˴����뷽��˵���� ��ͬ���õ�����,�޸�,ɾ������. �������ڣ�(2001-9-19 14:46:21) �޸�����: (2004-04-02)
	 * �˷����޸ĺ�,��ͬ���õı��治�ٸ�����״̬���ֱ���ú�̨�ķ���. �����ں�̨������״̬��ѡ����������ķ���,��:������,��ɾ��,���޸�.
	 * �޸���:cqw
	 */
	private boolean saveExp() {
		String sMessage = null;
		try {
			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"SCMCOMMON", "UPPSCMCommon-000134")/* @res "��ʼ���桭��" */);

			// �ӽ������޸ĺ������
			ExpBb3VO[] aryChange = (ExpBb3VO[]) (getCtBillCardPanel()
					.getBillModel("cost").getBodyValueChangeVOs(ExpBb3VO.class
					.getName()));
			// �ӽ�����Ŀǰ�����ϵ�����
			ExpBb3VO[] aryNew = (ExpBb3VO[]) (getCtBillCardPanel()
					.getBillModel("cost").getBodyValueVOs(ExpBb3VO.class
					.getName()));
			// ��Ҫ���͵���̨��VO����:
			ExpBb3VO[] arySave = null;
			// ��ɾ������׷��
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
			// �Ƿ����������,�������,��ô����˾�ͱ�ͷPK����.
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
					// ��¼ԭʼλ��
					arySave[i].setPosition(new Integer(i));
				}
			}
			// �������ݿ�
			if (arySave == null || arySave.length <= 0 || arySave[0] == null) {
				sMessage = nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"4020pub", "UPP4020pub-000079")/* @res "û��������Ҫ����." */;
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
			// ��дModel.
			getCtBillCardPanel().setHeadItem("ts", alRet.getParentVO().getAttributeValue("ts"));
			((ExtendManageVO) m_vBillVO.get(m_iId - 1)).getParentVO().setAttributeValue("ts", alRet.getParentVO().getAttributeValue("ts"));
			// ��дm_vBillVO.
			((ExtendManageVO) m_vBillVO.get(m_iId - 1)).setTableVO("cost",alRet.getExpBb3s());
			getCtBillCardPanel().getBillModel("cost").setBodyDataVO(alRet.getExpBb3s());
			// ���±���״̬
			getCtBillCardPanel().getBillModel("cost").updateValue();
			// ��ҳǩ״̬��Ϊ����.
			m_iTbState = OperationState.FREE;
			// ��Ƭģ�岻�ɱ༭.
			getCtBillCardPanel().setEnabled(false);

			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"SCMCOMMON", "UPPSCMCommon-000006")/* @res "����ɹ�" */);

		} catch (ValidationException e) {
			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"4020pub", "UPP4020pub-000076")/* @res "�������" */);
			MessageDialog.showErrorDlg(this, null, e.getMessage());
			m_bErrFlag = true;
			return false;
		} catch (java.sql.SQLException e) {
			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"4020pub", "UPP4020pub-000077")/* @res "����ʧ�ܣ�" */);

			if (e.getErrorCode() == 1401) {
				MessageDialog.showErrorDlg(this, null, nc.ui.ml.NCLangRes
						.getInstance().getStrByID("4020pub",
								"UPP4020pub-000078")/*
													 * @res "����Ƿ����ַ���������̫�����ַ�����"
													 */);
			} else if (e.getErrorCode() == 8152) {
				MessageDialog.showErrorDlg(this, null, nc.ui.ml.NCLangRes
						.getInstance().getStrByID("4020pub",
								"UPP4020pub-000078")/*
													 * @res "����Ƿ����ַ���������̫�����ַ�����"
													 */);
			} else
				MessageDialog.showErrorDlg(this, null, e.getMessage());

			m_bErrFlag = true;
			return false;

		} catch (java.rmi.RemoteException e) {
			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"4020pub", "UPP4020pub-000077")/* @res "����ʧ�ܣ�" */);
			MessageDialog.showErrorDlg(this,
					nc.ui.ml.NCLangRes.getInstance().getStrByID("4020pub",
							"UPP4020pub-000067")/* @res "Զ�̵���ʧ��" */, e
							.getMessage());

			m_bErrFlag = true;
			return false;
		} catch (Exception e) {
			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"4020pub", "UPP4020pub-000077")/* @res "����ʧ�ܣ�" */);
			MessageDialog.showErrorDlg(this, null, e.getMessage());
			reportException(e);
			m_bErrFlag = true;
			return false;
		}
		return true;
	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2001-9-19 14:45:37)
	 */
	private boolean saveManage() {

		// ����Ա��־
		nc.vo.sm.log.OperatelogVO log = getNormalOperateLog();

		// ��id�Ÿ����ݿ�Ƭ�ı�ͷԪ��
		m_timer.start("��ͬ���濪ʼ��"); /*-=notranslate=-*/
		// added by lirr 2009-11-16����04:30:19 ֧����֤��ʽ
		if(!getCtBillCardPanel().getBillData().execValidateFormulas())
		    return false;
		setNameToID();
		ManageVO newManageVO = null;
		ManageVO oldManageVO = null;
		ExtendManageVO oldExtendManageVO = null;
		// ���˿���
		getCtBillCardPanel().stopEditing();
		// �õ�����VO
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

		// ����ͷVO����pk_corp��ifearly��ֵ
		// �޸�ʱ�䣺2009/10/09 �޸���:wuweiping �޸�ԭ��: ��ͬ�������У��޸ĺ�ͬ��PK_corp������
		if (m_iBillState == OperationState.ADD) {
			((ManageHeaderVO) newManageVO.getParentVO()).setPk_corp(m_sPkCorp);
		}
		// ((ManageHeaderVO) newManageVO.getParentVO()).setPk_corp(m_sPkCorp);
		((ManageHeaderVO) newManageVO.getParentVO()).setIfearly(m_UFbIfEarly);

		// ��������޸���
		newManageVO.getParentVO().setAttributeValue("clastoperatorid",
				m_sPkUser);
		newManageVO.getParentVO().setAttributeValue("vlastoperatorname",
				m_sPkUser);

		newManageVO.setBillType(m_sBillType);
		// ������VO����pk_ct_manage��pk_corp
		ManageItemVO[] bodyVO = (ManageItemVO[]) newManageVO.getChildrenVO();
		for (int i = 0; i < newManageVO.getChildrenVO().length; i++) {
			if (m_iBillState == OperationState.ADD) {
				bodyVO[i].setPk_corp(m_sPkCorp);
			}
		}

		// �����ǷǱ������
		newManageVO.setIsChange(new UFBoolean(false));
		// ���ô����������:
		Object oRefValue = ((UIRefPane) getCtBillCardPanel().getHeadItem(
				"ct_type").getComponent()).getRefValue("ninvctlstyle");
		if (oRefValue != null)
			((ManageHeaderVO) newManageVO.getParentVO())
					.setInvControlType(new Integer(oRefValue.toString()));

		// ��������޸ģ���õ���ɾ���ı�����,�����뵽MewManageVO��
		if (m_iBillState == OperationState.EDIT) { // �õ�ɾ���ı�����
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
			// �����ɾ���ı�����,����뵽NewManageVO��
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
			// ����ͷVO����pk_corp��ifearly��ֵ
			// �޸�ʱ�䣺2009/10/09 �޸���:wuweiping �޸�ԭ��: ��ͬ�������У��޸ĺ�ͬ��PK_corp������
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
					"4020pub", "UPP4020pub-000081")/* @res "��ʼ���浱ǰ��ͬ����" */);
			// �������ĺϷ���
			newManageVO.validate();
			// ������û��ͨ���򷵻�
			if (checkSave(newManageVO) == false) {
				throw new ValidationException();
			}
			// added by lirr 2009-04-21 ҵ����־
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
				// Ϊ�����Դts׼�� added by lirr 2008-11-04
				//
				if (m_bAddFromBillFlag) {
					ExtendManageVO curVO = null;
					ManageItemVO[] curItemvo = null;
					// modified by lirr 2008-11-26 �޸�ԭ�� �����п��ܷ����仯 ����ɾ��
					// curVO = (ExtendManageVO) m_vBillVOForRef.elementAt(m_iId
					// - 1);
					curVO = vo;
					// modified by lirr 2008-12-12 �޸�ԭ�� �ӽ���ȡcurItemvo
					// ��Դ��ts�޷���õ��²����޷�����
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
				// modified by lirr 2009-7-17 ����02:24:01 ��ͷ�� �񱾱��ۼ���/�����ܶ�
				// ��Ϊ��ͷ����ԭ���ۼ���/�����ܶ�
				setAraptotalgpamount(newManageVO);
				// add by lirr 2009-06-18 ѹ������
				ManageItemVO[] bakbvos = newManageVO.getItemVOs();
				newManageVO.compressBodyWhenSave();
				while (true) {
					try {
						// modified by lirr 2009-05-31 �޸�ԭ�� �������� �������� voOld Ӧ��=
						// voNew����˲��ٴ�ǰ̨��

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
						// added by lirr �жϱ�����Ϻ�ת��ʲô���Ľ���

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
							// add by lirr 2009-06-18 ѹ������
							// newManageVO.setChildrenVO(bakbvos);

							m_iBillState = OperationState.FREE;

							// �ѵ�ǰNewManageVO������m_vBillVO��
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
							 * // modified by lirr �����ж� if (!m_bAddFromBillFlag) {
							 * m_iId = m_iElementsNum;
							 * 
							 * getCtBillCardPanel().setBillValueVO(
							 * newExtendManageVO); setHeaderListData(); // ִ�й�ʽ }
							 * else { m_iId = m_vBillVOForRef.size() - 1; }
							 */

							if (m_iId > 1) { //
								getButtonTree().getButton(
										CTButtonConst.BTN_BROWSE_PREVIOUS)
										.setEnabled(true); // ��ʱ
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
																				 * "����ʧ�ܣ�"
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
					// ��¼��״̬Ϊ2ʱ����ʾ�����ӵļ�¼,��ô�ͱ����pk_ct_manage��ֵ
					if (newManageVO.getChildrenVO()[i].getStatus() == 2) {
						((ManageItemVO) newManageVO.getChildrenVO()[i])
								.setPk_ct_manage(newManageVO.getParentVO()
										.getPrimaryKey().toString());
						// added by lirr 2009-11-2����12:17:42 û�й�˾��Ϣ ��̨�޷����oid
						((ManageItemVO) newManageVO.getChildrenVO()[i]).setPk_corp(m_sPkCorp);
						
					} else
						continue;
				}

				newManageVO.getParentVO().setAttributeValue("audiid", null);
				newManageVO.getParentVO().setAttributeValue("auditdate", null);
				newManageVO.getParentVO().setAttributeValue("coperatoridnow",
						m_sPkUser);
				newManageVO.setIsSaveCheck(true);

				// modified by lirr 2009-7-17 ����02:24:01 ��ͷ�� �񱾱��ۼ���/�����ܶ�
				// ��Ϊ��ͷ����ԭ���ۼ���/�����ܶ�
				setAraptotalgpamount(newManageVO);
				// added by lirr 2009-05-31 �޸�ԭ��oldVo�����ݿ��в�ѯ���ٴ���
				// oldManageVO = null;

				// added by lirr 20092009-6-18����04:49:09
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
																				 * "����ʧ�ܣ�"
																				 */);
							return false;
						}
					} finally {
						// newManageVO.setChildrenVO(bakcurbodyvos);
						oldManageVO.setChildrenVO(bakoldbodyvos);
					}
				}

				newManageVO.setIsSaveCheck(true);

				// //��д��ͷʱ���
				// getCtBillCardPanel().setHeadItem("ts",
				// NewManageVO.getParentVO().getAttributeValue("ts"));
				// ManageItemVO[] itemVO = (ManageItemVO[])
				// NewManageVO.getChildrenVO();
				// for (int i = 0; i < itemVO.length; i++) {
				// //��д����ʱ���
				// getCtBillCardPanel().setBodyValueAt(itemVO[i].getTs(), i,
				// "ts");
				// }

				m_iBillState = OperationState.FREE;

				m_vBillVO.remove(m_iId - 1);

				// modified by liuzy 2007-12-04 �̹�԰��ͬ�޸ı��汨�ѱ��������޸ĵĴ�
				// ��ʱ����
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

				// ��ͬ������޸ģ�����ִ������� �����Ӵ��м����� ����Ҫ�ڴ��޸ķ�ֹ����getCurVO()����Խ��
				// ��������2���˳�� qinchao lirr 2009-04-21 qinchao Խ��
				m_vBillVO.insertElementAt(oldExtendManageVO, (m_iId - 1));
				getCtBillCardPanel().setBillValueVO(oldExtendManageVO);

				// getCtBillCardPanel().setBillValueVO(oldExtendManageVO);
				// m_vBillVO.insertElementAt(oldExtendManageVO, (m_iId - 1));

			}

			// ����Table����Ҫ������������
			for (int i = 1; i < 6; i++)
				m_bIsNeedReInit[i] = true;
			// ִ�й�ʽ
			getCtBillCardPanel().getBillModel().execLoadFormula();
			getCtBillCardPanel().superupdateValue(); // ����ģ��״̬

			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"common", "UCH005")/* @res "����ɹ�" */);

			getCtBillCardPanel().setEnabled(false);
			m_bErrFlag = false;
			// getCtBillCardPanel().setHeadItem("ct_code",
			// NewManageVO.getParentVO().getAttributeValue("ct_code"));
			//			
			// // ����޸���
			// getCtBillCardPanel().setTailItem("clastoperatorid", m_sPkUser);
			// getCtBillCardPanel().setTailItem("vlastoperatorname", m_sPkUser);
			// // ����޸�ʱ��
			// getCtBillCardPanel().setTailItem("tlastmaketime",
			// NewManageVO.getParentVO().getAttributeValue("tlastmaketime"));
			//			

			m_timer.showExecuteTime("�������"); /*-=notranslate=-*/
			// added by lirr 2008-10-28 ��CT002Ϊ��Ҫ�Զ�����������

			if (m_isAutoSendApprove) {
				int ctflag = getCurBillVO().getParentVO().getCtflag()
						.intValue();
				if (ctflag != BillState.CHECKGOING)
					onSavetoAudit(false);
			}

			// ��ͬ������������m_copyedTermVOs��Ϊ�գ����Զ���ת��ͬ����ҳǩ
			// ��ͬ����ͬʱ���Ƶ��º�ͬ��
			// ���˲��ŵ��������Ϊ�����̶ȱ�֤��ͬ��������Ķ����ԡ�
			copyTermtoNewCnt();
			/*
			 * if (m_isAutoSendApprove) { int ctflag =
			 * getCurBillVO().getParentVO().getCtflag() .intValue(); if (ctflag !=
			 * BillState.CHECKGOING) onSavetoAudit(); }
			 */

		} catch (ValidationException e) {
			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"SCMCOMMON", "UPPSCMCommon-000250")/* @res "�������" */);
			// �޸�:e.getMessage()ΪNull��ʱ����׿�ָ�룬��˼���һ����������e.getMessage()!=null
			// �޸��ˣ���ѧ��
			// �޸����ڣ�2007-10-31
			nc.vo.scm.pub.SCMEnv.error(e);
			if (e.getMessage() != null && e.getMessage().length() > 0)
				MessageDialog.showErrorDlg(this, null, e.getMessage());

			m_bErrFlag = true;
			return false;

		} catch (java.sql.SQLException e) {
			nc.vo.scm.pub.SCMEnv.error(e);
			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"4020pub", "UPP4020pub-000077")/* @res "����ʧ�ܣ�" */);

			if (e.getErrorCode() == 1) {
				MessageDialog.showErrorDlg(this, null, nc.ui.ml.NCLangRes
						.getInstance().getStrByID("4020pub",
								"UPP4020pub-000083")/*
													 * @res "���ܱ��棡���������ظ��ĺ�ͬ���룡"
													 */);
			} else if (e.getErrorCode() == 1401) {
				MessageDialog.showErrorDlg(this, null, nc.ui.ml.NCLangRes
						.getInstance().getStrByID("4020pub",
								"UPP4020pub-000084")/*
													 * @res "����Ƿ����ַ���̫�����ַ�����"
													 */);
			} else if (e.getErrorCode() == 1438) {
				MessageDialog.showErrorDlg(this, null, nc.ui.ml.NCLangRes
						.getInstance().getStrByID("SCMCOMMON",
								"UPPSCMCommon-000251")/* @res "��������ֳ�����Χ��" */
						+ "\n"
						+ nc.ui.ml.NCLangRes.getInstance().getStrByID(
								"SCMCOMMON", "UPPSCMCommon-000263")/*
																	 * @res
																	 * "˰�ʣ�3λ������4λС��"
																	 */
						+ "\n"
						+ nc.ui.ml.NCLangRes.getInstance().getStrByID(
								"SCMCOMMON", "UPPSCMCommon-000064")/*
																	 * @res
																	 * "���ۻ��16λ��"
																	 */
				);
			} else if (e.getErrorCode() == 2601 || e.getErrorCode() == 2627) {
				MessageDialog.showErrorDlg(this, null, nc.ui.ml.NCLangRes
						.getInstance().getStrByID("4020pub",
								"UPP4020pub-000083")/*
													 * @res "���ܱ��棡���������ظ��ĺ�ͬ���룡"
													 */);
			} else if (e.getErrorCode() == 8152) {
				MessageDialog.showErrorDlg(this, null, nc.ui.ml.NCLangRes
						.getInstance().getStrByID("4020pub",
								"UPP4020pub-000084")/*
													 * @res "����Ƿ����ַ���̫�����ַ�����"
													 */);
			} else if (e.getErrorCode() == 8115) {
				MessageDialog.showErrorDlg(this, null, nc.ui.ml.NCLangRes
						.getInstance().getStrByID("SCMCOMMON",
								"UPPSCMCommon-000251")/* @res "��������ֳ�����Χ��" */
						+ "\n"
						+ nc.ui.ml.NCLangRes.getInstance().getStrByID(
								"SCMCOMMON", "UPPSCMCommon-000263")/*
																	 * @res
																	 * "˰�ʣ�3λ������4λС��"
																	 */
						+ "\n"
						+ nc.ui.ml.NCLangRes.getInstance().getStrByID(
								"SCMCOMMON", "UPPSCMCommon-000064")/*
																	 * @res
																	 * "���ۻ��16λ��"
																	 */
				);
			} else
				MessageDialog.showErrorDlg(this, null, e.getMessage());

			m_bErrFlag = true;
			return false;

		} catch (java.rmi.RemoteException e) {
			nc.vo.scm.pub.SCMEnv.error(e);
			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"4020pub", "UPP4020pub-000077")/* @res "����ʧ�ܣ�" */);
			MessageDialog.showErrorDlg(this,
					nc.ui.ml.NCLangRes.getInstance().getStrByID("4020pub",
							"UPP4020pub-000067")/* @res "Զ�̵���ʧ��" */, e
							.getMessage());
			m_bErrFlag = true;
			return false;
		} catch (Exception e) {
			nc.vo.scm.pub.SCMEnv.error(e);
			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"4020pub", "UPP4020pub-000077")/* @res "����ʧ�ܣ�" */);
			m_bErrFlag = true;
			MessageDialog.showErrorDlg(this, null, e.getMessage());
			return false;
		} catch (Throwable e) {
			nc.vo.scm.pub.SCMEnv.error(e);
			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"4020pub", "UPP4020pub-000077")/* @res "����ʧ�ܣ�" */);
			MessageDialog.showErrorDlg(this, null, e.getMessage());
			m_bErrFlag = true;
			return false;
		}
		return true;
	}

	/**
	 * @author shaobing ��ͬ������������m_copyedTermVOs��Ϊ�գ����Զ���ת��ͬ����ҳǩ ��ͬ����ͬʱ���Ƶ��º�ͬ��
	 */
	private void copyTermtoNewCnt() {
		if (m_copyedTermVOs != null) {

			// ��ҳǩת�Ƶ�"��ͬ����"��.
			getCtBillCardPanel().getBodyTabbedPane().setSelectedIndex(
					TabState.TERM);
			m_iTabbedFlag = TabState.TERM;

			getCtBillCardPanel().getBillModel("term").setBodyDataVO(
					m_copyedTermVOs);

			// ��ҳǩ״̬��Ϊ����.
			m_iTbState = OperationState.EDIT;
			getCtBillCardPanel().getBillModel("term").setEnabled(true);

			m_copyedTermVOs = null;

		}

	}

	/**
	 * �˴����뷽��˵���� ��ͬ���¼�����,�޸�,ɾ������. �������ڣ�(2001-9-19 14:45:51) �޸�����: (2004-04-02)
	 * �˷����޸ĺ�,��ͬ���¼ǵı��治�ٸ�����״̬���ֱ���ú�̨�ķ���. �����ں�̨������״̬��ѡ����������ķ���,��:������,��ɾ��,���޸�.
	 * �޸���:cqw
	 */
	private boolean saveNotes() {
		try {
			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"SCMCOMMON", "UPPSCMCommon-000134")/* @res "��ʼ���桭��" */);
			// �ӽ������޸ĺ������
			MemoraBb2VO[] aryChange = (MemoraBb2VO[]) (getCtBillCardPanel()
					.getBillModel("note")
					.getBodyValueChangeVOs(MemoraBb2VO.class.getName()));
			// �ӽ�����Ŀǰ�����ϵ�����
			MemoraBb2VO[] aryNew = (MemoraBb2VO[]) (getCtBillCardPanel()
					.getBillModel("note").getBodyValueVOs(MemoraBb2VO.class
					.getName()));
			// ��Ҫ���͵���̨��VO����:
			MemoraBb2VO[] arySave = null;
			// ��ɾ������׷��
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

			// �Ƿ����������,�������,��ô����˾�ͱ�ͷPK����.
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
					// ��¼ԭʼλ��
					arySave[i].setPosition(new Integer(i));
				}
			}
			// �������ݿ�.
			if (arySave == null || arySave.length <= 0 || arySave[0] == null) {
				throw new ValidationException(nc.ui.ml.NCLangRes.getInstance()
						.getStrByID("4020pub", "UPP4020pub-000079")/*
																	 * @res
																	 * "û��������Ҫ����."
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
			// ��дModel.
			getCtBillCardPanel().getBillModel("note").setBodyDataVO(alRet.getMemoraBb2s());
			// ��дm_vBillVO.
			((ExtendManageVO) m_vBillVO.get(m_iId - 1)).setTableVO("note",alRet.getMemoraBb2s());
			// ���±���״̬.
			getCtBillCardPanel().getBillModel("note").updateValue();
			// ��ҳǩ״̬��Ϊ����.
			m_iTbState = OperationState.FREE;
			// ��Ƭģ�岻�ɱ༭.
			getCtBillCardPanel().setEnabled(false);

			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"SCMCOMMON", "UPPSCMCommon-000006")/* @res "����ɹ�" */);

		} catch (ValidationException e) {
			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"4020pub", "UPP4020pub-000076")/* @res "�������" */);
			MessageDialog.showErrorDlg(this, null, e.getMessage());
			m_bErrFlag = true;
			return false;
		} catch (java.sql.SQLException e) {
			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"4020pub", "UPP4020pub-000077")/* @res "����ʧ�ܣ�" */);
			if (e.getErrorCode() == 1401) {
				MessageDialog.showErrorDlg(this, null, nc.ui.ml.NCLangRes
						.getInstance().getStrByID("4020pub",
								"UPP4020pub-000078")/*
													 * @res "����Ƿ����ַ���������̫�����ַ�����"
													 */);
			} else if (e.getErrorCode() == 8152) {
				MessageDialog.showErrorDlg(this, null, nc.ui.ml.NCLangRes
						.getInstance().getStrByID("4020pub",
								"UPP4020pub-000078")/*
													 * @res "����Ƿ����ַ���������̫�����ַ�����"
													 */);
			} else
				MessageDialog.showErrorDlg(this, null, e.getMessage());

			m_bErrFlag = true;
			return false;
		} catch (java.rmi.RemoteException e) {
			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"4020pub", "UPP4020pub-000077")/* @res "����ʧ�ܣ�" */);
			MessageDialog.showErrorDlg(this,
					nc.ui.ml.NCLangRes.getInstance().getStrByID("4020pub",
							"UPP4020pub-000067")/* @res "Զ�̵���ʧ��" */, e
							.getMessage());
			m_bErrFlag = true;
			return false;
		} catch (Exception e) {
			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"4020pub", "UPP4020pub-000077")/* @res "����ʧ�ܣ�" */);
			MessageDialog.showErrorDlg(this, null, e.getMessage());
			reportException(e);
			m_bErrFlag = true;
			return false;
		}
		return true;
	}

	/**
	 * �˴����뷽��˵���� ��ͬ��������,�޸�,ɾ������. �������ڣ�(2001-9-19 14:45:51) �޸�����: (2004-04-02)
	 * �˷����޸ĺ�,��ͬ����ı��治�ٸ�����״̬���ֱ���ú�̨�ķ���. �����ں�̨������״̬��ѡ����������ķ���,��:������,��ɾ��,���޸�.
	 * �޸���:cqw
	 */
	private boolean saveTerm() {
		try {
			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"SCMCOMMON", "UPPSCMCommon-000134")/* @res "��ʼ���桭��" */);

			// �ӽ������޸ĺ������
			TermBb4VO[] aryChange = (TermBb4VO[]) (getCtBillCardPanel()
					.getBillModel("term").getBodyValueChangeVOs(TermBb4VO.class
					.getName()));
			// �ӽ�����Ŀǰ�����ϵ�����
			TermBb4VO[] aryNew = (TermBb4VO[]) (getCtBillCardPanel()
					.getBillModel("term").getBodyValueVOs(TermBb4VO.class
					.getName()));
			// ��Ҫ���͵���̨��VO����:
			TermBb4VO[] arySave = null;
			// ��ɾ������׷��
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

			// �Ƿ����������,�������,��ô����˾�ͱ�ͷPK����.
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
					// ��¼ԭʼλ��
					arySave[i].setPosition(new Integer(i));
				}
			}
			// �������ݿ�.
			if (arySave == null || arySave.length <= 0 || arySave[0] == null) {
				throw new ValidationException(nc.ui.ml.NCLangRes.getInstance()
						.getStrByID("4020pub", "UPP4020pub-000079")/*
																	 * @res
																	 * "û��������Ҫ����."
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
			// ��дModel.
			getCtBillCardPanel().setHeadItem("ts", alRet.getParentVO().getAttributeValue("ts"));
			((ExtendManageVO) m_vBillVO.get(m_iId - 1)).getParentVO().setAttributeValue("ts", alRet.getParentVO().getAttributeValue("ts"));
			// ��дm_vBillVO.
			((ExtendManageVO) m_vBillVO.get(m_iId - 1)).setTableVO("term",
					alRet.getTermBb4s());
			
			getCtBillCardPanel().getBillModel("term").setBodyDataVO(alRet.getTermBb4s());
			// ��дm_vBillVO.
			((ExtendManageVO) m_vBillVO.get(m_iId - 1)).setTableVO("term",
					alRet.getTermBb4s());
			// ���±���״̬.
			getCtBillCardPanel().getBillModel("term").updateValue();
			// ��ҳǩ״̬��Ϊ����.
			m_iTbState = OperationState.FREE;
			// ��Ƭģ�岻�ɱ༭.
			getCtBillCardPanel().setEnabled(false);

			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"SCMCOMMON", "UPPSCMCommon-000006")/* @res "����ɹ�" */);
		} catch (ValidationException e) {
			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"4020pub", "UPP4020pub-000076")/* @res "�������" */);
			MessageDialog.showErrorDlg(this, null, e.getMessage());
			m_bErrFlag = true;
			return false;
		} catch (java.sql.SQLException e) {
			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"4020pub", "UPP4020pub-000077")/* @res "����ʧ�ܣ�" */);

			if (e.getErrorCode() == 1401) {
				MessageDialog.showErrorDlg(this, null, nc.ui.ml.NCLangRes
						.getInstance().getStrByID("4020pub",
								"UPP4020pub-000078")/*
													 * @res "����Ƿ����ַ���������̫�����ַ�����"
													 */);
			} else if (e.getErrorCode() == 8152) {
				MessageDialog.showErrorDlg(this, null, nc.ui.ml.NCLangRes
						.getInstance().getStrByID("4020pub",
								"UPP4020pub-000078")/*
													 * @res "����Ƿ����ַ���������̫�����ַ�����"
													 */);
			} else
				MessageDialog.showErrorDlg(this, null, e.getMessage());

			m_bErrFlag = true;
			return false;
		} catch (java.rmi.RemoteException e) {
			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"4020pub", "UPP4020pub-000077")/* @res "����ʧ�ܣ�" */);
			MessageDialog.showErrorDlg(this,
					nc.ui.ml.NCLangRes.getInstance().getStrByID("4020pub",
							"UPP4020pub-000067")/* @res "Զ�̵���ʧ��" */, e
							.getMessage());
			m_bErrFlag = true;
			return false;
		} catch (Exception e) {
			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"4020pub", "UPP4020pub-000077")/* @res "����ʧ�ܣ�" */);
			MessageDialog.showErrorDlg(this, null, e.getMessage());
			reportException(e);
			m_bErrFlag = true;
			return false;
		}
		return true;
	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2001-12-10 19:19:14)
	 */
	private void setBackWhere() {
		((UIRefPane) getCtBillCardPanel().getHeadItem("pername").getComponent())
				.setWhereString(m_sPerWhereSql);
		((UIRefPane) getCtBillCardPanel().getHeadItem("deliaddrname")
				.getComponent()).setWhereString(null);
	}

	/**
	 * ���ܣ��������͸�ֵ ������ ���أ� ���⣺ ���ڣ�(2002-8-29 10:43:43) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 */
	protected void setBillType(String sBillType) {
		m_sBillType = sBillType;
	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2001-9-7 16:52:51)
	 */
	private void setBodyListData(int row) {// modified by lirr 2008-07-24
		// ����m_bAddFromBillFlag���ж�

		// ExtendManageVO mVO = (ExtendManageVO) m_vBillVO.elementAt(row);
		ExtendManageVO mVO = null;
		if (!m_bAddFromBillFlag) {
			mVO = (ExtendManageVO) m_vBillVO.elementAt(row);
		} else {
			mVO = (ExtendManageVO) m_vBillVOForRef.elementAt(row);

		}
		String currid = ((ManageHeaderVO) mVO.getParentVO()).getCurrid();
		// int childNum= mVO.getChildrenVO().length;

		// ��ô˱��ֵ�С��λ��
		// ��ô˱��ֵ�С��λ��
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
		// ����ԭ�ҽ��С��λ��
		getCtBillListPanel().getBodyItem(CTTableCode.BASE, "orisum")
				.setDecimalDigits(currDigit);
		getCtBillListPanel().getBodyItem(CTTableCode.BASE, "oritaxmny")
				.setDecimalDigits(currDigit);
		getCtBillListPanel().getBodyItem(CTTableCode.BASE, "oritaxsummny")
				.setDecimalDigits(currDigit);
		// added by lirr 2009-7-28 ����03:27:40 �ۼ�ԭ�ң�Ӧ���ո������
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
	 * �˴����뷽��˵���� �������ڣ�(2001-9-1 19:01:18)
	 */
	public void setButtonState() {

		// ��ͬ״̬
		int iState = -1;

		switch (m_iTabbedFlag) {
		case 0:

			if (m_iBillState == OperationState.FREE) {
				iState = OperationState.FREE;
				if (m_iId == 0) {
					// ��ǰû��һ������ʱ������������ҳǩ���ɱ༭�����򶼿ɱ༭
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

					if (m_bIsCard) { // �ڿ�Ƭģʽ��
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
					} else { // ���б�ģʽ�²���Ҫ���°�ť
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

				// ����ˢ�°�ť״̬[V2.3]
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
				if (m_bIsCard) // �ǿ�Ƭģʽ
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

				// addied by liuzy 2008-10-29 ����10:16:27
				getButtonTree().getButton(CTButtonConst.BTN_QUERY).setEnabled(
						false);
			}

			// ���ݺ�ͬ״̬��ȷ����ͬ�޸ģ�ɾ����ť�Ŀ�����
			if (m_iId > 0) { // ���к�ͬʱ
				int ctflag = ((ManageHeaderVO) ((ExtendManageVO) m_vBillVO
						.elementAt(m_iId - 1)).getParentVO()).getCtflag()
						.intValue();
				if (ctflag == BillState.TERMINATE || ctflag == BillState.FREEZE
						|| ctflag == BillState.VALIDATE
						|| ctflag == BillState.AUDIT
						|| ctflag == BillState.ABOLISH
				/* || (ctflag == BillState.CHECKGOING) */) {
					// ��ͬ��ˡ���Ч�����ᡢ��ֹ�󶼲����޸ĺ�ɾ��
					getButtonTree().getButton(CTButtonConst.BTN_BILL_EDIT)
							.setEnabled(false);
					getButtonTree().getButton(CTButtonConst.BTN_BILL_DELETE)
							.setEnabled(false);
					// getTabbedPane().setEnabledAt(1,false); //��˺�����ܸ���
				}
				// modified by lirr 2008-11-27 ��������еĵ�����������������������޸�
				if (ctflag == BillState.CHECKGOING) {
					getButtonTree().getButton(CTButtonConst.BTN_BILL_DELETE)
							.setEnabled(false);
					// �޸���:wuweiping �޸�ʱ��:2009-10-19 ����03:38:30 �޸�ԭ��: ��������еĵ�����������������������޸�
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
					 * //added by lirr 2008-12-17 ��ͬ��������� �����󡱰�ť����
					 * getButtonTree().getButton(CTButtonConst.BTN_EXECUTE_CANCELAPPRPVE)
					 * .setEnabled(true);
					 */
					// ������ ���û�������������ťsetEnabled false added by lirr
					// 2009-02-11
					if (!StringUtil
							.isEmptyWithTrim(((ManageHeaderVO) ((ExtendManageVO) m_vBillVO
									.elementAt(m_iId - 1)).getParentVO())
									.getAudiid())) {
						// added by lirr 2008-12-17 ��ͬ��������� �����󡱰�ť����
						getButtonTree().getButton(
								CTButtonConst.BTN_EXECUTE_CANCELAPPRPVE)
								.setEnabled(true);
					}
				}

				if (ctflag != BillState.FREE)
					getButtonTree().getButton(
							CTButtonConst.BTN_EXECUTE_SENDAPPRPVE).setEnabled(
							false);
				// ��ǰ��¼�˷��Ƶ�����������
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

			// ���ú�ͬ״̬���Ƶİ�ť״̬
			// modify by liujq 2007-05-14
			// ֻ�е����ݲ���״̬ΪFREEʱ���Ÿ��ݺ�ͬ����״̬ȥ���°�ť
			if (m_iBillState == OperationState.FREE)
				setFlagControlStates();

			// ���ݰ�ť��Ȩ������ҳǩ��״̬[V2.3]
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

		default: // ����1��2��3��4ҳǩ��״̬
			setTbButtonState();

		}
		// ��չ��ťĬ�ϼ��ں�ͬ����ҳǩ֮��,���ÿ�������ҳǩ�Ĵ������.
		// iState: ��ͬ��״̬.
		setExtendBtnsStat(iState);
		updateButtons();
	}

	/**
	 * �˷������ڲ�ѯ�󷵻ؿ�Ƭģʽ�¶Ե��ݿ�Ƭ���ݵĳ�ʼ�� �������ڣ�(2001-9-14 10:26:26)
	 */
	private void setCardVOData() {
		// id=1;
		m_iElementsNum = m_vBillVO.size();
		// ���õ��ݿ�ƬVO
		getCtBillCardPanel().setBillValueVO(
				(ExtendManageVO) m_vBillVO.elementAt(m_iId - 1));
		getCtBillCardPanel().updateValue();
		// �ϼ��б��
		getCtBillCardPanel().getTotalTableModel().setValueAt(
				nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
						"UC000-0001146")/* @res "�ϼ�" */, 0, 0);
		// isNeedReInit = true;//�ı䵥�ݣ�����Table����Ҫ������������
	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2001-9-21 19:45:13)
	 */
	protected void setCtRef() {

		// ���ú�ͬ���Ͳ��ղ���
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

		// ���ý����ص����ʧȥ����ʱ��ʾ�����ص�
		((UIRefPane) getCtBillCardPanel().getHeadItem("deliaddrname")
				.getComponent()).setReturnCode(true);

		((UIRefPane) getCtBillCardPanel().getHeadItem("projectname")
				.getComponent()).setWhereString("bd_jobmngfil.pk_corp = '"
				+ m_sPkCorp + "'");

		// ���ý������Ŀ���
		UITextField cell = new UITextField();
		cell.setTextType("TextDbl");
		cell.setMaxLength(16);
		cell.setNumPoint(m_iMainCurrDigit);

	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2001-10-23 17:24:19)
	 */
	public void setDifferButtons() {
		if (m_iTabbedFlag == TabState.BILL) {
			// setButtons(m_aryButtonGroup);
			changeToBlButn();
		} else {
			if (m_iTabbedOldFlag == TabState.BILL)
				// ���ǰһ��ҳǩ�ǵ���ҳǩ����ť�Ķ�
				changeToTbButn();
			// setButtons(m_aryTbButtonGroup);

		}
		m_iTabbedOldFlag = m_iTabbedFlag;
	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2001-12-5 22:09:16)
	 */
	public void setFlagControlStates() {

	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2001-9-7 10:21:41)
	 */
	private void setHeaderListData() {

		ManageHeaderVO[] vListVO = new ManageHeaderVO[m_vBillVO.size()];
		if (m_vBillVO.size() != 0)
			for (int i = 0; i < m_vBillVO.size(); i++) {
				vListVO[i] = (ManageHeaderVO) ((ExtendManageVO) m_vBillVO
						.elementAt(i)).getParentVO();
			}
		
		getCtBillListPanel().setHeaderValueVO(vListVO);
		// ִ�й�ʽ
		getCtBillListPanel().getHeadBillModel().execLoadFormula();

	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2001-11-21 10:15:36)
	 */
	private void setIdtoName() {
		// �ûز���ԭ����where����
		m_timer.start("��ʼִ��setIdToname()"); /*-=notranslate=-*/
		setBackWhere();
		m_timer.showExecuteTime("setBackWhere()");

		// ��ͷ��˾
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

		// ����޸���
		getCtBillCardPanel().getTailItem("vlastoperatorname").setValue(
				getCtBillCardPanel().getTailItem("clastoperatorid")
						.getValueObject());
		// added by lirr 2009-05-25 ���ƺ��Զ��������
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
	 * ����˵����Ϊ�����б����û��ʾ��� ������ ���أ� ���⣺ ���ڣ�(2002-7-23 20:20:05) ���ߣ����� modified by
	 * lirr �޸�ԭ�򣺴��빺�����۸�������ת�������ĺ�ͬ ������һ�������� Ҳ��Ҫ���о�������
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
				// modifeid by lirr 2008-12-24 ����
				iRateDecimals[i] = getCurrateDigit(sCurrid[i]);
				// iAstRateDecimals[i] = iTemp[1];
			}

			nc.ui.pub.bill.BillListData bd = getCtBillListPanel()
					.getBillListData();
			nc.ui.pub.bill.BillItem rateitem = bd.getHeadItem("currrate");
			if (null != rateitem) {// &&
				MyTableCellRenderer rateCellRenderer = new MyTableCellRenderer(
						rateitem/* , true, false */);
				rateCellRenderer.setPrecision(iRateDecimals);// ���þ���
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
			 * AstRateCellRenderer.setPrecision(iAstRateDecimals);// ���þ��� if
			 * (astRateitem.isShow()) { javax.swing.table.TableColumn
			 * astRateColumn = getCtBillListPanel()
			 * .getHeadTable().getColumn(astRateitem.getName());
			 * astRateColumn.setCellRenderer(AstRateCellRenderer); } }
			 */
		}

	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2001-11-29 10:58:05) �޸�˵�������Ѿ���˻���Ч�ĺ�ͬ���к�ͬ����ı��. �޸��ߣ��۱�
	 * �޸�ʱ�䣺��Ԫ2005��4��8��
	 */
	private void setTbButonByflag() {
		if (m_iId > 0 && !m_bAddFromBillFlag) { // ���к�ͬʱ modified by lirr
			// 2008-07-24 ���Ӳ��ǲ����������ݵ�
			int ctflag = Integer.parseInt(((ExtendManageVO) m_vBillVO
					.get(m_iId - 1)).getParentVO().getAttributeValue("ctflag")
					.toString());
			// ���ú�ͬ�������ɾ��״̬
			//if (m_iTabbedFlag == TabState.TERM
			if ((m_iTabbedFlag == TabState.TERM || m_iTabbedFlag == TabState.EXP)
					&& m_iTbState == OperationState.FREE) {
				if (ctflag == BillState.FREE /*|| ctflag == BillState.AUDIT
						|| ctflag == BillState.VALIDATE*/)
					setTbGroupState(false, false, true, false, false);
				//begin ncm  wangminp NC2014122600148_�͸�����_2014-12-29_ר
				/*
				 * ԭʼҵ��Ϊ����״̬�����״̬��
				 * ��Ч״̬����ͬ�����ǩҳ���޸İ�ť�����ǿ���״̬
				 * ���ڸ����͸������Ϊ�������Ч�󲻿��޸ġ���ͬ���
				 */
				else if(ctflag == BillState.AUDIT
						|| ctflag == BillState.VALIDATE){
					setTbGroupState(false, false, false, false, false);
				}
				//end ncm  wangminp NC2014122600148_�͸�����_2014-12-29_ר
				else
					setTbGroupState(false, false, false, false, false);
			}
			// add by liuzy ��ͬ��ֹ�����²��ɱ༭
			else if (m_iTabbedFlag == TabState.NOTE) {
				if (ctflag == BillState.TERMINATE)
					setTbGroupState(false, false, false, false, false);
			}
		}

	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2001-10-24 13:56:49)
	 */
	protected void setTbButtonState() {

		if (m_iTbState == OperationState.FREE) {
			if (m_iTabbedFlag == TabState.EXEC) {
				setTbGroupState(false, false, false, false, false);
			} else if (m_iTabbedFlag == TabState.CHANGE
					&& m_bChangeFlag == false) {

				int rowCount = getCtBillCardPanel().getBillModel("history")
						.getRowCount();
				// ��������ʷֻ��һ��,˵��û��ִ�й���ͬ���,Ҳ���޴��޸ı����ʷ.
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

		} else { // tbOperaState.equals("ɾ��")

			if (m_iTabbedFlag == TabState.EXEC) {
				setTbGroupState(false, false, false, false, false);
			} else
				setTbGroupState(true, true, false, true, true);

		}

		setTbButonByflag();
	}

	/**
	 * ����Table��ť���״̬ �������ڣ�(2001-10-28 13:42:57)
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
	 * �˴����뷽��˵���� �������ڣ�(2001-10-28 14:45:10)
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
					/* @res "{0}�����ѱ����ģ��Ƿ���Ҫ���棿" */
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

					if (ync == UIDialog.ID_YES) { // ��Ҫ����
						int temp = m_iTabbedFlag;
						m_iTabbedFlag = m_iTabbedOldFlag;
						getCtBillCardPanel().getBodyPanel().setTableCode(
								CTTableCode.CT_TABCODE[m_iTabbedFlag]);
						onSave(); // �����������޸�
						if (!m_bErrFlag) {
							m_iTabbedFlag = temp; // �õ����ڵ�ҳǩ��
							getCtBillCardPanel().getBodyPanel().setTableCode(
									CTTableCode.CT_TABCODE[m_iTabbedFlag]);
						} else {
							m_bNeedChange = false; // �����ٴ���stateChanged()����
							getCtBillCardPanel().getBodyTabbedPane()
									.setSelectedIndex(m_iTabbedOldFlag);
							// ҳǩ�ظ�
							m_iTabbedFlag = m_iTabbedOldFlag;

							return;
						}

					} else if (ync == UIDialog.ID_NO) { // ����Ҫ����

						m_bIsNeedReInit[m_iTabbedOldFlag] = true;
						int temp = m_iTabbedFlag;
						m_iTabbedFlag = m_iTabbedOldFlag;
						// getCtBillCardPanel().getBodyPanel().setTableCode(
						// CTTableCode.CT_TABCODE[m_iTabbedFlag]);
						// ȡ��
						onCancel();
						// added by lirr 2009-02-17
						m_iBillState = OperationState.FREE;

						if (!m_bErrFlag) {
							m_iTabbedFlag = temp; // �õ����ڵ�ҳǩ��
							getCtBillCardPanel().getBodyPanel().setTableCode(
									CTTableCode.CT_TABCODE[m_iTabbedFlag]);

						} else {
							m_bNeedChange = false; // �����ٴ���stateChanged()����
							getCtBillCardPanel().getBodyTabbedPane()
									.setSelectedIndex(m_iTabbedOldFlag);
							// ҳǩ�ظ�
							m_iTabbedFlag = m_iTabbedOldFlag;
							return;
						}

					} else { // ȡ������
						m_bNeedChange = false; // �����ٴ���stateChanged()����
						getCtBillCardPanel().getBodyTabbedPane()
								.setSelectedIndex(m_iTabbedOldFlag);
						// ҳǩ�ظ�
						m_iTabbedFlag = m_iTabbedOldFlag;

						return;
					}

				}
			}
			// �ı䰴ť��
			setDifferButtons();

			// �����Ҫ������������
			if (m_iTabbedFlag != TabState.BILL
					&& m_bIsNeedReInit[m_iTabbedFlag] == true) {
				// �����Ӧ��Table��¼
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
			m_bNeedChange = true; // ���Դ���
		}
	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2001-9-7 12:53:43)
	 * 
	 * @param param
	 *            javax.swing.event.ListSelectionEvent
	 */
	public void valueChanged(ListSelectionEvent e) {
		showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4020pub",
				"UPP4020pub-000085")/* @res "����鿴��Ӧ��ͬ�Ĵ�����" */);

		int row = -1;

		try {
			if (e.getSource() == getCtBillListPanel().getHeadTable()
					.getSelectionModel()) {

				row = getCtBillListPanel().getHeadTable().getSelectedRow();
				// ���δѡ���У�����
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

				// ���õ�������ť��״̬
				// if (ctFlag == BillState.FREE) {
				// modified by lirr ������� ҲҪ�����޸�
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
                // �޸���:wuweiping �޸�ʱ��:2009-10-21 ����03:45:34 �޸�ԭ��:��������еĵ�����������������������޸�
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

				m_bChangeFlag = false; // ��־��ͬ���ɱ��
				// �ı䵥�ݣ�����Table����Ҫ������������
				for (int i = 1; i < 6; i++)
					m_bIsNeedReInit[i] = true;
				// ��ö�Ӧ��ͬ�ı����¼
				setBodyListData(row);

				// tableOperaState = "����";

				// }
				// }
				if (e.getSource() == getCtBillListPanel().getHeadTable()
						.getSelectionModel()) {
					// ���ñ�ͷѡ��
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
				// modified by lirr 2009-02-18 �޸�ԭ���ڷǻ���ҳǩ��ѡ����ʱRow index out of
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
						// �򸨼������ݲ���
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
			// ��־�쳣
			nc.vo.scm.pub.SCMEnv.out(ex);
		}
	}

	// added by lirr 2008-08-14 ��ѡ��ѡ��
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
	 * ֻ�Ա�ͷ���д���
	 * <li>���л� �¼�
	 * <li>˫�� �¼�
	 * <li>WARN::���л��¼�������˫���¼�֮ǰ
	 * 
	 * @param iNewRow
	 */
	private synchronized void headRowChange(int iNewRow) {
		if (getCtBillListPanel().getHeadBillModel().getValueAt(iNewRow, 0) != null) {
			if (!getCtBillListPanel().setBodyModelData(iNewRow)) {
				// 1.���������������
				loadBodyData();
				// 2.���ݵ�ģ����
				getCtBillListPanel().setBodyModelDataCopy(iNewRow);
			}
		}
		getCtBillListPanel().repaint();
	}

	/**
	 * �������������������ر�������
	 * <p>
	 * <b>examples:</b>
	 * <p>
	 * ʹ��ʾ��
	 * <p>
	 * <b>����˵��</b>
	 * <p>
	 * 
	 * @author liuzy
	 * @time 2008-4-17 ����01:38:37
	 */
	protected void loadBodyData() {
		ManageVO manageVO = getCurVO();
		// if (null == manageVO.getChildrenVO()
		// || manageVO.getChildrenVO().length < 1) {
		try {
			// manageVO = ContractQueryHelper.queryAllBodyData(manageVO);
			// if (null == manageVO) {
			// showHintMessage(NCLangRes.getInstance().getStrByID(
			// "4020pub", "UPP4020pub-000269")/* ��ѯ��������ʱ���� */);
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
			 * // ��������� for (int k = 0; k < tableVOs.length; k++) { FreeVO
			 * voFree = InvTool.getInvFreeVO((String) tableVOs[k]
			 * .getAttributeValue("invid")); // ����������� if (voFree != null) {
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
			// modified by lirr 2009-8-20����07:25:39 ����������
			nc.ui.scm.pub.FreeVOParse freeVOParse = new nc.ui.scm.pub.FreeVOParse();
			freeVOParse.setFreeVO(tableVOs, null, "invid", false);

			getCtBillListPanel().getBodyBillModel().execFormulasWithVO(
					tableVOs, getTableFormulas());
			getCtBillListPanel().getBodyBillModel().execFormulasWithVO(termVOs,
					getTableFormulas());
			m_vBillVO.setElementAt(voExtendCur, m_iId - 1);

		} catch (Exception e1) {
			// ��־�쳣
			nc.vo.scm.pub.SCMEnv.out(e1);
			// ���淶�׳��쳣
			showErrorMessage(NCLangRes.getInstance()
					.getStrByID("4020pub", "UPP4020pub-000270", null,
							new String[] { e1.getMessage() })/* ��ѯ�������ݳ���\n{0} */);
			// }
		}
	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2001-8-22 12:36:36)
	 * 
	 * @return nc.ui.pub.bill.BillCardPanel
	 */
	protected ScmBillCardPanel getCtBillCardPanel() {
		if (m_BillCardPanel == null) {
			try {
				m_BillCardPanel = new CtBillCardPanel();
				m_BillCardPanel.setTatolRowShow(true);
				m_BillCardPanel.setRowNumKey("crowno");

				// ��ʼ�����ݿ�Ƭģ��
				if (m_sBillType == null) {
					MessageDialog.showErrorDlg(this, null, nc.ui.ml.NCLangRes
							.getInstance().getStrByID("4020pub",
									"UPP4020pub-000086")/* @res "��������δ��ֵ" */);
				} else {
					BillData bd = null;
					if (getFrame() == null)
						/*
						 * bd = new BillData(m_BillCardPanel.getTempletData(
						 * m_sBillType, null, m_sPkUser, m_sPkCorp));
						 */
						// modified by lirr 2009-8-17����01:11:58 ����������
						// �б��뿨Ƭһ����getBillTempletVO
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

					// ������
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
									"common", "UC000-0001146")/* @res "�ϼ�" */,
							0, 0);

					// m_BillCardPanel.getBillModel().

					// setCardPanelFormular(); // �������й�ʽ
					// setCardPanelActionTable(); // �������ж���˳��
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

				// �ǹ̶������ʲ������븺��
				UIRefPane refTransrate = (UIRefPane) m_BillCardPanel
						.getBodyItem(CTTableCode.BASE, "transrate")
						.getComponent();
				refTransrate.setDelStr("-");
				// ��ͬ���Ͳ��ղ�Ҫ����
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
	 * �˴����뷽��˵���� �������ڣ�(2001-8-22 19:00:00)
	 * 
	 * @return nc.ui.pub.bill.BillListPanel
	 */
	protected ScmBillListPanel getCtBillListPanel() {
		if (m_BillListPanel == null) {
			try {
				m_BillListPanel = new ScmBillListPanel(true);

				// ��ʼ�������б�ģ��
				/*
				 * BillListData bd = new BillListData(m_BillListPanel
				 * .getDefaultTemplet(m_sBillType, null, m_sPkUser, m_sPkCorp));
				 */
				// modified by lirr 2009-8-17����01:17:44 ����������
				// ��Ƭ���б�ͬһ��getBillTempletVO(m_sBillType)
				BillListData bd = new BillListData(
						getBillTempletVO(m_sBillType));
				// �ı����
				setListPanelByPara(bd);
               // �޸���:wuweiping �޸�ʱ��:2009-10-15 ����06:55:23 �޸�ԭ��:��д��������ٴ�����
				bd = CTTool.changeBillListDataByUserDef(m_sPkCorp, m_sBillType,bd);
				//changeBillListDataByUserDef(bd);
				// try {
				// // �޸��Զ�����
				// bd = changeBillDataByUserDef(bd);
				// }
				// catch (Exception e) {
				// }

				// ���ý��棬��������Դ
				m_BillListPanel.setListData(bd);
				// ��ʾ�ϼ���
				m_BillListPanel.getChildListPanel().setTotalRowShow(true);
				// ���ñ�ͷѡ��ģʽ����ͷ��ѡ
				m_BillListPanel.getHeadTable().setSelectionMode(
						ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

			} catch (Throwable e) {
				nc.vo.scm.pub.SCMEnv.out(e);
			}
		}
		return m_BillListPanel;
	}

	/**
	 * �������ڣ�(2003-3-18 10:16:08) ���ߣ����� �޸����ڣ� �޸��ˣ� �޸�ԭ�� ����˵���� modified by lirr
	 * 2008-08-05 ���� ���� sCustType��ԭ�򣺿��̿���ͬʱΪ��Ӧ�̻��߿��̣���Ϊ���̺͹�Ӧ�̵ı��ֲ�ͬ sCustType "and
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
	 * �������ڣ�(2008-08-05 10:16:08) ���ߣ���ȽȽ �޸����ڣ� �޸��ˣ� �޸�ԭ�� ����˵���� sCustType "and
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
	 * �˴����뷽��˵���� �������ڣ�(2001-8-22 16:25:45)
	 */
	protected void initButtons() {

		// ������չ��ť
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

		// ���ϰ�ť��
		setButtons(m_aryButtonGroup);
		changeToBlButn();

	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2001-9-19 15:56:35)
	 */
	protected void onAbolish() {
		String sMessage = null;
		try {

			ManageVO tempMVO = new ManageVO();
			ManageVO mVO = getCurVO();
			tempMVO = mVO.clone(mVO);
			int ctState = Integer.parseInt(tempMVO.getParentVO()
					.getAttributeValue("ctflag").toString());

			// �Դ��ڲ�ͬ״̬�ĺ�ͬ���в�ͬ�Ĵ���
			if (ctState == BillState.FREE) { // ����״̬
				sMessage = nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"4020pub", "UPP4020pub-000087")/* @res "�Ƿ�ȷ��Ҫ��ֹ�ú�ͬ��" */;

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
			} else { // ״̬��һ��
				sMessage = nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"4020pub", "UPP4020pub-000088")/*
														 * @res
														 * "״̬���������²�ѯ���ٽ��в�����"
														 */;
				MessageDialog.showWarningDlg(this, null, sMessage);
			}

		} catch (java.sql.SQLException e) {
			sMessage = nc.ui.ml.NCLangRes.getInstance().getStrByID("4020pub",
					"UPP4020pub-000066")/* @res "���ݿ��������" */;
			MessageDialog.showErrorDlg(this, sMessage, e.getMessage());

		} catch (java.rmi.RemoteException e) {
			sMessage = nc.ui.ml.NCLangRes.getInstance().getStrByID("4020pub",
					"UPP4020pub-000067")/* @res "Զ�̵���ʧ��" */;
			MessageDialog.showErrorDlg(this, sMessage, e.getMessage());

		} catch (Exception e) {
			MessageDialog.showErrorDlg(this, null, e.getMessage());
			reportException(e);

		}
		setButtonState();
	}

	/**
	 * �˴����뷽��˵���� ���ܣ� ������ ���أ� ���⣺ ���ڣ�(2002-9-11 9:46:03) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 * @param ButtongObject
	 *            nc.ui.pub.ButtonObject
	 */
	// �޸�˵������setButtonState();������onButtonClicked����������ƶ�����ÿ���¼�����ĺ��档�޸�ԭ��Ӱ�콹�� �޸���
	// ����ѧ�� �޸�����:2007-11-1
	public void onButtonClicked(ButtonObject bo) {
		showHintMessage("");

		if (bo == getButtonTree().getButton(CTButtonConst.BTN_ADD)) {
			/*
			 * onAdd();
			 * getCtBillCardPanel().transferFocusTo(BillCardPanel.HEAD);
			 */

			// modified by lirr 2009-03-11 ���ۺ�ͬ���ӹ���ɾ�� sincev56 ��Ϊ�����ơ����յ�
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
													 * @res "����ѡ�������"
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

			// add by qinchao 20090224 ���ܣ���ճ�����е�"�ۼƸ�����"��Ϊ0
			int row = getCtBillCardPanel().getBodyPanel().getTable()
					.getSelectedRow();
			int rowlength = getCtBillCardPanel().getBillModel()
					.getPasteLineNumer();
			clearExecInfo(row,rowlength);
			// updateUI();
			// end ���ܣ���ճ�����е�"�ۼƸ�����"��Ϊ0

			if (!m_bAddFromBillFlag) {
				setButtonState();
			} else {
				setButtonStateForCof();
			}
		}

		else if (bo == getButtonTree().getButton(
				CTButtonConst.BTN_LINE_LineToTail)) {
			getCtBillCardPanel().pasteLineToTail();

			// add by qinchao 20090224 ���ܣ���ճ�����е�"�ۼƸ�����"��Ϊ0
			int row = getCtBillCardPanel().getBodyPanel().getTable()
					.getRowCount();
			int rowlength = getCtBillCardPanel().getBillModel()
					.getPasteLineNumer();
			clearExecInfo(row,rowlength);
			
			updateUI();
			// end ���ܣ���ճ�����е�"�ۼƸ�����"��Ϊ0

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
			// MessageDialog.showErrorDlg(this, null, "�༭״̬�²����Խ�����˲���");
			// else{
			// if (!m_bIsCard
			// && getCtBillListPanel().getHeadTable()
			// .getSelectedRowCount() > 1
			// && (MessageDialog.showYesNoDlg(this, null,
			// nc.ui.ml.NCLangRes.getInstance().getStrByID(
			// "4020nodes", "UPP4020nodes-000051")/*
			// * @res
			// * "���Ƿ�ȷ��Ҫ��˸ú�ͬ��"
			// */) == 4))
			// onBatchAction("����");
			// else
			if (m_iBillState == OperationState.EDIT) {
				showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"4020pub", "UPT4020pub-000232")/*
														 * @res
														 * "�༭״̬�²����Խ�����˲���������ȡ���༭�򱣴�"
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
			// * "�Ƿ�ȷ��Ҫʹ�ú�ͬ��Ч��"
			// */) == 4)) {
			// onBatchAction("��Ч");
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
			// * "�Ƿ�ȷ��Ҫ����ú�ͬ��"
			// */) == 4))
			// onBatchAction("����");
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
				CTButtonConst.BTN_ASSIST_FUNC_DOCUMENT)) // �ĵ�����
			onDocument();
		else if (bo == getButtonTree().getButton(CTButtonConst.BTN_TAB_PRINT))// ҳǩ��ӡ
			onTabPrint();
		else if (bo == getButtonTree().getButton(
				CTButtonConst.BTN_BROWSE_REFRESH))// ˢ��
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
		// added by lirr 2008-08-12 ��ӿ�Ƭ�༭
		else if (bo == getButtonTree().getButton(ScmButtonConst.BTN_CARD_EDIT)) {
			onLineCardEdit();
		} else if (bo == getButtonTree().getButton(
				CTButtonConst.BTN_ADD_NEWROWNO)) {
			onAddNewRowNo();
		} else if (bo == getButtonTree().getButton(
				ScmButtonConst.BTN_LINE_PASTE_TAIL)) {
			getCtBillCardPanel().pasteLineToTail();
		}
		// added by lirr 2009-9-21����09:54:44 �鿴������״̬ �Ӹ��������г��
		else if (bo == getButtonTree().getButton(
				CTButtonConst.BTN_ASSIST_QUERY_FLOWSTATUE))
			onFlowStatus();

		// ��չ��ť��Ӧ
		else
			onExtendBtnsClick(bo);

		// else if(bo == boAllotImprest)
		// onAllotImprest();
	}

	/**
	 * �Ե��ݽ�����ˣ��ı䵥�ݵ�״̬ �������ڣ�(2001-9-14 14:50:53)
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

			ManageVO mVO = getCurVO();// �õ���ǰѡ�еĵ���VO
			ManageVO tempMVO = getAuditVO(true, mVO);
			if (tempMVO == null){
			    // added by lirr 2009-11-28����01:46:58 ��Ҫ��˵ĵ���Ϊ�� ���� �Ƿ����ʱѡ���m_iBillState = OperationState.FREE
			    m_iBillState = OperationState.FREE;
				return null;
			}
			ArrayList alRet = null;

			alRet = (ArrayList) nc.ui.pub.pf.PfUtilClient.processActionFlow(
					this, "APPROVE", m_sBillType, getClientEnvironment()
							.getDate().toString(), tempMVO, tempMVO);// ��������ƽ̨�ͻ��˹�����ִ������
			// ������˳��
			if (!nc.ui.pub.pf.PfUtilClient.isSuccess()) {

				showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"SCMCOMMON", "UPPSCMCommon-000235")/*
															 * @res "���ʧ��"
															 */);
				return null;
			}
			// ��ͬPK[ˢ�º�ͬ״̬]
			String sPK = (String) mVO.getParentVO().getAttributeValue(
					"pk_ct_manage");
			int ctStateNew = qryCtStatus(sPK).intValue();// �����˺��ͬ״̬

			if (BillState.AUDIT == ctStateNew
					|| BillState.CHECKGOING == ctStateNew) {
				/*
				 * mVO.getParentVO().setAttributeValue("ctflag", new
				 * Integer(BillState.AUDIT));
				 */// ���ú�ͬΪ���״̬(�������õ���ʱVO)
				/*
				 * mVO.getParentVO().setAttributeValue("ctflag",
				 * qryCtStatus(sPK).intValue());
				 */
				mVO.getParentVO().setAttributeValue("ctflag", ctStateNew);
				mVO.getParentVO().setAttributeValue("audiid", m_sPkUser);
				mVO.getParentVO().setAttributeValue("auditdate",
						getClientEnvironment().getDate());

				// modified by lirr 2009-8-26����04:27:17 ������
				getCtBillCardPanel().setHeadItem("ctflag", ctStateNew);

				getCtBillCardPanel().setTailItem("audiname", m_sPkUser);// ���ñ�����������(��Ƭ����)
				getCtBillCardPanel().setTailItem("audiid", m_sPkUser);
				getCtBillCardPanel().setTailItem("auditdate",
						getClientEnvironment().getDate());// ����ʱ��
				getCtBillListPanel().getHeadBillModel().setValueAt(ctStateNew,
						m_iId - 1, "ctflag");// ���ú�ͬ״̬(�б����)

				getCtBillListPanel().getHeadBillModel().setValueAt(m_sUserName,
						m_iId - 1, "audiname");
				getCtBillListPanel().getHeadBillModel().setValueAt(m_sPkUser,
						m_iId - 1, "audiid");
				getCtBillListPanel().getHeadBillModel().setValueAt(
						getClientEnvironment().getDate(), m_iId - 1,
						"auditdate");

				if (alRet != null && alRet.size() > 0)
					freshStatusTs(mVO, (String) alRet.get(1));
				// modify reason : �б������˺���ʾ����ʱ�� modified by lirr
				getCtBillListPanel().getHeadBillModel().setValueAt(
						(String) mVO.getParentVO().getAttributeValue("ts"),
						m_iId - 1, "taudittime");

				// ǩ��ʱ�� ȡ����ֵts
				setAuditTime(mVO, (String) mVO.getParentVO().getAttributeValue(
						"ts"));
			}

			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"SCMCOMMON", "UPPSCMCommon-000234")/*
														 * @res "��˳ɹ�"
														 */);

			// added by lirr 2008-12-27
			/**
			 * ���״̬����д������ �� ���ʱ�״� ����Ͳ�������δ��� �����԰� ״̬����д��catch()֮�� qinchao
			 * 2009-05-07
			 */
			// m_iBillState = OperationState.FREE;
		} catch (java.sql.SQLException e) {
			MessageDialog.showErrorDlg(this,
					nc.ui.ml.NCLangRes.getInstance().getStrByID("4020nodes",
							"UPP4020nodes-000043")/* @res "���ݿ��������" */, e
							.getMessage());
			strRtn = e.getMessage();
			
		} catch (java.rmi.RemoteException e) {
			MessageDialog.showErrorDlg(this,
					nc.ui.ml.NCLangRes.getInstance().getStrByID("4020nodes",
							"UPP4020nodes-000044")/* @res "Զ�̵���ʧ��" */, e
							.getMessage());
			strRtn = e.getMessage();
		} catch (Exception e) {
			MessageDialog.showErrorDlg(this, null, e.getMessage());
			reportException(e);
			strRtn = e.getMessage();
		}

		// added by lirr 2008-12-27 ���״̬����д������ �� ���ʱ�״� ����Ͳ�������δ��� �����԰�
		// ״̬����д��catch()֮�� qinchao 2009-05-07
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
	 * �\n�������ڣ�(2003-6-16 11:52) ���ߣ���ѫƽ �޸����ڣ� �޸��ˣ� �޸�ԭ�� �㷨˵����
	 */
	public void onDocument() {
		String[] sIDs = null;
		String[] sCodes = null;
		// addied by liuzy 2008-04-11
		// V5.03���󣺲ɹ���ͬ�����ۺ�ͬ���ĵ������У����Ƶ����ݴ���������������������Ч���ر�״̬ʱ�����ĵ������е�ɾ����ť�����ã�
		int[] iCTFlag = null;
		String[] sCTFlag = null;
		HashMap<String, BtnPowerVO> hmBtn = new HashMap<String, BtnPowerVO>();
		if (m_bIsCard == true) { // �� "��Ƭ" ��״̬��
			if (getCtBillCardPanel().getBillData().getEnabled()) {
				showWarningMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"SCMCOMMON", "UPPSCMCommon-000016")/*
															 * @res
															 * "�༭״̬����ִ�д˲�����"
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
			// modified by liuzy �Ӻ�̨��ѯ��ͬ״̬
			iCTFlag[0] = qryCtStatus(sIDs[0]);
			if (null == sIDs[0] || sIDs[0].trim().length() == 0
					|| null == sCodes[0] || sCodes[0].trim().length() == 0) {
				showWarningMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"4020pub", "UPP4020pub-000089")/* @res "û�е���ID�򵥾ݺţ�" */);
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

		} else { // �� "�б�" ��״̬��
			int[] iRows = getCtBillListPanel().getHeadTable().getSelectedRows();
			if (iRows.length == 0) {
				showWarningMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"SCMCOMMON", "UPPSCMCommon-000275")/* @res "δѡ���У�" */);
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
				// modified by lirr �ж���Զ�� fasle ���¡�ɾ������ť���ɼ�
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

		// TODO liuzy ������
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
	 * �˴����뷽��˵���� �������ڣ�(2001-9-19 15:56:12)
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
			// �޸�ʱ��: 2009/10/09 �޸���:wuweiping �޸�ԭ��: ��ͬ��Ч����񣬱��տ�
			if (tempMVO == null) {
				return;
			}
			// modified by lirr 2009-8-24����10:58:19 ���ӷ���ֵ
			// ����ts����ˢ��tsʱ��������
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
																				 * "ҵ�����"
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
			m_bIsNeedReInit[TabState.EXEC] = true; // ִ�й��̸��£���������������

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
			// modified by lirr 2009-8-24����10:42:00 ˢ��ts�����ߺ�̨���ڶ����з���
			if (alRet != null && alRet.size() > 0)
				freshStatusTs(mVO, (String) alRet.get(1));
			ExtendManageVO voCur = (ExtendManageVO) m_vBillVO.get(m_iId - 1);
			voCur.setParentVO(mVO.getParentVO());
			// voCur.setTableVO("exec", mVO.getManageExecs());
			// modified by lirr 2009-9-14����03:38:19
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
													 * @res "ʵ����Ч"
													 */);

		} catch (java.sql.SQLException e) {
			MessageDialog.showErrorDlg(this,
					nc.ui.ml.NCLangRes.getInstance().getStrByID("4020pub",
							"UPP4020pub-000066")/* @res "���ݿ��������" */, e
							.getMessage());

		} catch (java.rmi.RemoteException e) {
			MessageDialog.showErrorDlg(this,
					nc.ui.ml.NCLangRes.getInstance().getStrByID("4020pub",
							"UPP4020pub-000067")/* @res "Զ�̵���ʧ��" */, e
							.getMessage());

		} catch (BusinessException e) {
			MessageDialog.showHintDlg(this,
					nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON",
							"UPPSCMCommon-000297")/* @res "ҵ�����" */, e
							.getMessage());

		} catch (Exception e) {
			MessageDialog.showErrorDlg(this, null, e.getMessage());
			reportException(e);

		}

	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2001-9-10 13:36:31)
	 */
	protected void qryCt(String sCtid) {
		try {
			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"4020pub", "UPP4020pub-000069")/* @res "��ȴ������ڲ�ѯ��ͬ����" */);

			ArrayList<ExtendManageVO> listVO = new ArrayList<ExtendManageVO>();
			StringBuffer sWhere = new StringBuffer(" ct_manage.pk_ct_manage='"
					+ sCtid + "' ");
			sWhere.append(" AND ct_type.nbusitype=");
			sWhere.append(m_iCtType);
			if (m_UFbIfEarly.booleanValue())
				sWhere.append(" and ct_manage.ifearly='Y'");
			else
				sWhere.append(" and ct_manage.ifearly='N'");

			// modified by liuzy 2008-04-28 �̹�԰��ѯ����
			// ManageVO[] arrMangevos = ContractQueryHelper.queryBill(m_sPkCorp,
			// sWhere.toString());
			ManageVO[] arrMangevos = ContractQueryHelper.queryAllHeadData(
					m_sPkCorp, sWhere.toString());
			if (arrMangevos == null || arrMangevos.length == 0) {
				this.showHintMessage(nc.ui.ml.NCLangRes.getInstance()
						.getStrByID("4020pub", "UPP4020pub-000070")/*
																	 * @res
																	 * "û���ҵ���������������"
																	 */);
				return;
			}

			/** *****************׷��Ȩ�޲���V51********************** */
			SCMQueryConditionDlg qrydlg = new SCMQueryConditionDlg(this);
			if (qrydlg.getAllTempletDatas() == null
					|| qrydlg.getAllTempletDatas().length <= 0)
				qrydlg.setTempletID(m_sPkCorp, m_sNodeCode, m_sPkUser, null);
			String sPkCorp = null;
			if (arrMangevos[0].getPk_corp() == null) {
				this.showHintMessage(nc.ui.ml.NCLangRes.getInstance()
						.getStrByID("4020pub", "UPP4020pub-000070")/*
																	 * @res
																	 * "û���ҵ���������������"
																	 */);
				return;
			}
			sPkCorp = arrMangevos[0].getPk_corp();
			String[] refcodes = getDataPowerFieldFromDlgNotByProp(qrydlg,
					false, null);
			qrydlg.setCorpRefs("ct_manage.pk_corp", refcodes);// ��������Ȩ���ֶ�
			ConditionVO[] cons = qrydlg.getDataPowerConVOs(sPkCorp, refcodes);
			String swhere = null;
			if (cons != null && cons.length > 0)
				swhere = qrydlg.getWhereSQL(cons);
			if (swhere != null && swhere.trim().length() > 0)
				sWhere.append(" and " + swhere);

			// modified by liuzy 2008-04-28 �̹�԰��ѯ����
			// arrMangevos = ContractQueryHelper.queryBill(m_sPkCorp, sWhere
			// .toString());
			if (arrMangevos == null || arrMangevos.length == 0) {
				this.showHintMessage(nc.ui.ml.NCLangRes.getInstance()
						.getStrByID("4020pub", "UPT4020pub-000230")/*
																	 * @res
																	 * "û�в쿴���ݵ�Ȩ��"
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
				// �ж����ڿ�Ƭģʽ�£��������б�ģʽ��
				m_bIsFirstClick = true; // ��־�ǵ�һ�ε��
				// �л����б�ģʽ��
				onList();
				m_iId = 1;
				m_bChangeFlag = false; // ��־��ͬ���ɱ��
				// ����Table����Ҫ������������
				for (int i = 1; i < 6; i++)
					m_bIsNeedReInit[i] = true;

				setCardVOData(); // ���³�ʼ��Ƭ����
				// �л��ؿ�Ƭ
				onList();

				/* @res "��ѯ���������鵽{0}����ͬ��" */
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
														 * "�Բ���δ�鵽���������ĺ�ͬ��"
														 */;
				showHintMessage(sMessage);
				MessageDialog.showHintDlg(this, null, sMessage);
			}
		} catch (Exception e) {
			nc.vo.scm.pub.SCMEnv.out(e);
			MessageDialog.showHintDlg(this, null, nc.ui.ml.NCLangRes
					.getInstance().getStrByID("4020pub", "UPP4020pub-000073")/*
																				 * @res
																				 * "��ͬ��ѯ����"
																				 */
					+ "\n" + e.getMessage());
		}

	}

	/**
	 * �˴����뷽��˵���� ���ܣ� ������ ���أ� ���⣺ ���ڣ�(2002-9-5 14:58:46) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 * @param iBusyType
	 *            int
	 */
	protected void setCtType(int iCtType) {
		m_iCtType = iCtType;
	}

	/**
	 * �˴����뷽��˵���� ���ܣ� ������ ���أ� ���⣺ ���ڣ�(2002-9-13 13:15:44) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 * @param bIfEarly
	 *            boolean
	 */
	protected void setIfEarly(UFBoolean UFbIfEarly) {
		m_UFbIfEarly = UFbIfEarly;
	}

	/**
	 * �˴����뷽��˵���� ���ܣ� ������ ���أ� ���⣺ ���ڣ�(2002-9-6 10:36:30) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 * @param sNodeCode
	 *            java.lang.String
	 */
	protected void setNodeCode(String sNodeCode) {
		m_sNodeCode = sNodeCode;
	}

	/**
	 * �������ڣ�(2003-3-6 10:53:02) ���ߣ���ѫƽ �޸����ڣ� �޸��ˣ� �޸�ԭ�� �㷨˵����
	 * 
	 * @param newPrimaryKeyName
	 *            java.lang.String
	 */
	protected void setCtPrimaryKeyName(java.lang.String newCtPrimaryKeyName) {
		sCtPrimaryKeyName = newCtPrimaryKeyName;
	}

	/**
	 * �������ڣ�(2003-3-6 10:53:35) ���ߣ���ѫƽ �޸����ڣ� �޸��ˣ� �޸�ԭ�� �㷨˵����
	 * 
	 * @param newBillCodeKeyName
	 *            java.lang.String
	 */
	protected void setCtBillCodeKeyName(java.lang.String newCtBillCodeKeyName) {
		sCtBillCodeKeyName = newCtBillCodeKeyName;
	}

	/**
	 * �������ڣ�(2003-3-6 10:53:35) ���ߣ���ѫƽ �޸����ڣ� �޸��ˣ� �޸�ԭ�� �㷨˵����
	 * 
	 * @return java.lang.String
	 */
	protected java.lang.String getCtBillCodeKeyName() {
		if (null == sCtBillCodeKeyName)
			sCtBillCodeKeyName = "";
		return sCtBillCodeKeyName;
	}

	/**
	 * �������ڣ�(2003-3-6 10:53:02) ���ߣ���ѫƽ �޸����ڣ� �޸��ˣ� �޸�ԭ�� �㷨˵����
	 * 
	 * @return java.lang.String
	 */
	protected java.lang.String getCtPrimaryKeyName() {
		if (null == sCtPrimaryKeyName)
			sCtPrimaryKeyName = "";
		return sCtPrimaryKeyName;
	}

	/**
	 * �����ߣ������� ���ܣ� ������ ���أ� ���⣺ ���ڣ�(2001-11-8 19:47:29) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 * @return nc.ui.pub.bill.BillData
	 * @param bdData
	 *            nc.ui.pub.bill.BillData
	 */
	protected BillData changeBillDataByUserDef(BillData bdData) {
		// �����Զ��������
		DefVO[] defs = null;

		/*
		 * �۱� �޸��Զ�������ط�ʽ 2005-06-23 �Զ����ͷ������DefVO[]����̬���� �������û���½�ڼ䣬�Զ�������ֻ��һ�κ�̨��ѯ
		 * ����ֻ����ʼ��һ�Σ������Ĺ�Ӧ���ڵ㶼��ʹ��
		 */
		defs = DefSetTool.getDefHead(m_sPkCorp, m_sBillType);
		if ((defs != null)) {
			bdData.updateItemByDef(defs, "def", true);
		}

		// ����
		// ��ö�Ӧ�ڹ�˾�ĸõ��ݵ��Զ���������
		defs = DefSetTool.getDefBody(m_sPkCorp, m_sBillType);
		if ((defs == null)) {
			return bdData;
		}

		bdData.updateItemByDef(defs, "def", false);

		return bdData;
	}

	/**
	 * �����ߣ������� ���ܣ� ������ ���أ� ���⣺ ���ڣ�(2001-11-8 19:47:29) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 * @return nc.ui.pub.bill.BillData
	 * @param oldBillData
	 *            nc.ui.pub.bill.BillData
	 */
	protected BillListData changeBillListDataByUserDef(BillListData oldBillData) {
		// �����Զ��������
		/*
		 * ����Ⱥ �޸��Զ�������ط�ʽ 2006-03-16 �Զ����ͷ������DefVO[]����̬����
		 * �������û���½�ڼ䣬�Զ�������ֻ��һ�κ�̨��ѯ ����ֻ����ʼ��һ�Σ������Ĺ�Ӧ���ڵ㶼��ʹ��
		 */
		DefVO[] defs = null;
		// ��ͷ
		// ��ö�Ӧ�ڹ�˾�ĸõ��ݵ��Զ���������  
		defs = DefSetTool.getDefHead(m_sPkCorp, m_sBillType);//nc.ui.bd.service.BDDef.queryDefVO("��Ӧ��/ARAP����ͷ",m_sPkCorp);
		// 
		// /*-=notranslate=-*/
		if ((defs != null)) {
			oldBillData.updateItemByDef(defs, "def", true);
		}

		// ����
		// ��ö�Ӧ�ڹ�˾�ĸõ��ݵ��Զ���������
		defs = DefSetTool.getDefBody(m_sPkCorp, m_sBillType);// nc.ui.bd.service.BDDef.queryDefVO("��Ӧ��/ARAP������",
		// m_sPkCorp);
		// /*-=notranslate=-*/
		if ((defs == null)) {
			return oldBillData;
		}

		oldBillData.updateItemByDef(defs, "def", false);

		return oldBillData;
	}
	

	/**
	 * �������ڣ�(2003-6-16 15:01:57) ���ߣ����� �޸����ڣ� �޸��ˣ� �޸�ԭ�� �㷨˵����
	 */
	public void initialize() {
		setCtPrimaryKeyName("pk_ct_manage");
		setCtBillCodeKeyName("ct_code");

	}

	/**
	 * ��ʼ��ǰ�ı���档 �������ڣ�(2001-11-15 9:20:13)
	 * 
	 * @param bdData
	 *            nc.ui.pub.bill.BillData
	 */
	public void setCardPanel(BillData bdData) {
		setCardPanelByPara(bdData);
		setCardPanelByOther(bdData);
	}

	/**
	 * �˴����뷽��˵���� ��������: �������: ����ֵ: �쳣����: ����:
	 */
	protected void setCardPanelActionTable() {
		// ���嶯��˳���
		ArrayList alActionTable = null;

		// �۸����ʱ�
		alActionTable = new ArrayList();
		// �۱�����
		// alActionTable.add("currrate");
		// getCtBillCardPanel().setActionTable("astcurrate", alActionTable);

		// ��˰���۱�
		alActionTable = new ArrayList();
		// �۱��۸�����
		// alActionTable.add("astcurrate");
		// getCtBillCardPanel().setActionTable("oritaxprice", alActionTable);
		alActionTable.add("currrate");
		getCtBillCardPanel().setActionTable("oritaxprice", alActionTable);

		// ��˰���۱�
		alActionTable = new ArrayList();
		// �۱��۸�����
		// alActionTable.add("astcurrate");
		// getCtBillCardPanel().setActionTable("oriprice", alActionTable);
		alActionTable.add("currrate");
		getCtBillCardPanel().setActionTable("oriprice", alActionTable);

		// ����
		alActionTable = new ArrayList();
		// �۱��۸�����
		// alActionTable.add("astcurrate");
		// getCtBillCardPanel().setActionTable("orisum", alActionTable);
		alActionTable.add("currrate");
		getCtBillCardPanel().setActionTable("orisum", alActionTable);

		// ˰���
		alActionTable = new ArrayList();
		// �۱��۸�����
		// alActionTable.add("astcurrate");
		// getCtBillCardPanel().setActionTable("oritaxmny", alActionTable);
		alActionTable.add("currrate");
		getCtBillCardPanel().setActionTable("oritaxmny", alActionTable);

		// ��˰�ϼƱ�
		alActionTable = new ArrayList();
		// �۱��۸�����
		// alActionTable.add("astcurrate");
		// getCtBillCardPanel().setActionTable("oritaxsummny", alActionTable);
		alActionTable.add("currrate");
		getCtBillCardPanel().setActionTable("oritaxsummny", alActionTable);

		// ˰�ʱ�
		alActionTable = new ArrayList();
		// �۱��۸�����
		// alActionTable.add("astcurrate");
		// getCtBillCardPanel().setActionTable("taxration", alActionTable);
		alActionTable.add("currrate");
		getCtBillCardPanel().setActionTable("taxration", alActionTable);

		// ������
		alActionTable = new ArrayList();
		// �۱��۸�����
		// alActionTable.add("astcurrate");
		// getCtBillCardPanel().setActionTable("amount", alActionTable);
		alActionTable.add("currrate");
		getCtBillCardPanel().setActionTable("amount", alActionTable);

		// ��������
		alActionTable = new ArrayList();
		// ����
		alActionTable.add("amount");
		getCtBillCardPanel().setActionTable("astnum", alActionTable);

		// �����ʱ�
		alActionTable = new ArrayList();
		// ����
		alActionTable.add("amount");
		getCtBillCardPanel().setActionTable("transrate", alActionTable);

		// ��������λ�����
		alActionTable = new ArrayList();
		// ������
		alActionTable.add("transrate");
		getCtBillCardPanel().setActionTable("astmeascode", alActionTable);

		// ��������
		alActionTable = new ArrayList();
		// ��������λ����
		alActionTable.add("astmeascode");
		getCtBillCardPanel().setActionTable("invcode", alActionTable);
	}

	/**
	 * ������ԭ��ı���档 �������ڣ�(2001-11-15 9:18:13)
	 * 
	 * @param bdData
	 *            nc.ui.pub.bill.BillData
	 */
	protected void setCardPanelByOther(BillData bdData) {

		// ��õ���Ԫ�ض�Ӧ�Ŀؼ�
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

		// ���ü���״̬
		UIComboBox comActiveItem = (UIComboBox) bdData
				.getHeadItem("activeflag").getComponent();
		comActiveItem.setTranslate(true);
		comActiveItem.addItem(nc.ui.ml.NCLangRes.getInstance().getStrByID(
				"SCMCOMMON", "UPPSCMCommon-000244")/* @res "��" */);
		comActiveItem.addItem(nc.ui.ml.NCLangRes.getInstance().getStrByID(
				"SCMCOMMON", "UPPSCMCommon-000108")/* @res "��" */);
		comActiveItem.setSelectedIndex(0);
		bdData.getHeadItem("activeflag").setWithIndex(true);

	}

	/**
	 * ���ݲ����ı���档 �������ڣ�(2001-9-27 16:13:57)
	 */
	protected void setCardPanelByPara(BillData bdData) {
		try {
			// �޸��Զ�����
			bdData = changeBillDataByUserDef(bdData);
		} catch (Exception e) {
		}
	}

	/**
	 * �˴����뷽��˵���� ��������: �������: ����ֵ: �쳣����: ����:
	 */
	protected void setCardPanelFormular() {
		// ������㹫ʽ
		ArrayList alFormula = null;

		// ԭ�Һ�˰���۱�
		alFormula = new ArrayList();
		// ԭ�Ҽ�˰�ϼ�=����*ԭ�Һ�˰����
		alFormula.add(new String[] { "oritaxsummny", "amount*oritaxprice" });
		// ԭ��˰��=˰��*ԭ�Ҽ�˰�ϼ�/(1+˰��)
		alFormula.add(new String[] { "oritaxmny",
				"taxration/100*oritaxsummny/(1+taxration/100)" });
		// ԭ�ҽ��=ԭ�Ҽ�˰�ϼ�-ԭ��˰��
		alFormula.add(new String[] { "orisum", "oritaxsummny-oritaxmny" });
		// ԭ����˰����=ԭ�Һ�˰����/��1+˰�ʣ�
		alFormula.add(new String[] { "oriprice",
				"oritaxprice/(1+taxration/100)" });
		getCtBillCardPanel().setFormula("oritaxprice", alFormula);

		// ԭ����˰���۱�
		alFormula = new ArrayList();
		// ԭ�ҽ��=����*ԭ����˰����
		alFormula.add(new String[] { "orisum", "amount*oriprice" });
		// ԭ��˰��=˰��*ԭ�ҽ��
		alFormula.add(new String[] { "oritaxmny", "taxration/100*orisum" });
		// ԭ�Ҽ�˰�ϼ�=ԭ�ҽ��+˰��
		alFormula.add(new String[] { "oritaxsummny", "orisum+oritaxmny" });
		// ԭ�Һ�˰����=ԭ����˰����*��1+˰�ʣ�
		alFormula.add(new String[] { "oritaxprice",
				"oriprice*(1+taxration/100)" });
		getCtBillCardPanel().setFormula("oriprice", alFormula);

		// ԭ�ҽ���
		alFormula = new ArrayList();
		// ԭ����˰����=ԭ�ҽ��/����
		alFormula.add(new String[] { "oriprice", "orisum/amount" });
		// ԭ��˰��=˰��*ԭ�ҽ��
		alFormula.add(new String[] { "oritaxmny", "taxration/100*orisum" });
		// ԭ�Ҽ�˰�ϼ�=ԭ�ҽ��+ԭ��˰��
		alFormula.add(new String[] { "oritaxsummny", "orisum+oritaxmny" });
		// ԭ�Һ�˰����=ԭ�Ҽ�˰�ϼ�/����
		alFormula.add(new String[] { "oritaxprice",
				"oriprice*(1+taxration/100)" });
		getCtBillCardPanel().setFormula("orisum", alFormula);

		// ԭ��˰���
		alFormula = new ArrayList();
		// ԭ�Ҽ�˰�ϼ�=ԭ�ҽ��+ԭ��˰��
		alFormula.add(new String[] { "oritaxsummny", "orisum+oritaxmny" });
		// ԭ�Һ�˰����=ԭ�Ҽ�˰�ϼ�/����
		alFormula.add(new String[] { "oritaxprice", "oritaxsummny/amount" });
		getCtBillCardPanel().setFormula("oritaxmny", alFormula);

		// ԭ�Ҽ�˰�ϼƱ�
		alFormula = new ArrayList();
		// ԭ�Һ�˰����=ԭ�Ҽ�˰�ϼ�/����
		alFormula.add(new String[] { "oritaxprice", "oritaxsummny/amount" });
		// ԭ��˰��=˰��*ԭ�Ҽ�˰�ϼ�/(1+˰��)
		alFormula.add(new String[] { "oritaxmny",
				"taxration/100*oritaxsummny/(1+taxration/100)" });
		// ���=��˰�ϼ�-˰��
		alFormula.add(new String[] { "orisum", "oritaxsummny-oritaxmny" });
		// ��˰����=��˰����/��1+˰�ʣ�
		alFormula.add(new String[] { "oriprice",
				"oritaxprice/(1+taxration/100)" });
		getCtBillCardPanel().setFormula("oritaxsummny", alFormula);

		// ȡ�ü����ͬ�۸����ȹ������
		// ������ֵ��ͬ,���岻ͬ�ļ��㹫ʽ
		if (sContractPriceRule.equals("��˰����")) { /*-=notranslate=-*/

			// ������
			alFormula = new ArrayList();
			// ��˰�ϼ�=����*��˰����
			alFormula
					.add(new String[] { "oritaxsummny", "amount*oritaxprice" });
			// ˰��=˰��*��˰�ϼ�/(1+˰��)
			alFormula.add(new String[] { "oritaxmny",
					"taxration/100*oritaxsummny/(1+taxration/100)" });
			// ���=��˰�ϼ�-˰��
			alFormula.add(new String[] { "orisum", "oritaxsummny-oritaxmny" });
			// ��˰����=��˰����/��1+˰�ʣ�
			alFormula.add(new String[] { "oriprice",
					"oritaxprice/(1+taxration/100)" });
			getCtBillCardPanel().setFormula("amount", alFormula);

			// ˰�ʱ�
			alFormula = new ArrayList();
			// ˰��=˰��*��˰�ϼ�/(1+˰��)
			alFormula.add(new String[] { "oritaxmny",
					"taxration/100*oritaxsummny/(1+taxration/100)" });
			// ���=��˰�ϼ�-˰��
			alFormula.add(new String[] { "orisum", "oritaxsummny-oritaxmny" });
			// ��˰����=��˰����/��1+˰�ʣ�
			alFormula.add(new String[] { "oriprice",
					"oritaxprice/(1+taxration/100)" });
			getCtBillCardPanel().setFormula("taxration", alFormula);

		} else if (sContractPriceRule.equals("��˰����")) { /*-=notranslate=-*/

			// ������
			alFormula = new ArrayList();
			// ���=����*��˰����
			alFormula.add(new String[] { "orisum", "amount*oriprice" });
			// ˰��=˰��*���
			alFormula.add(new String[] { "oritaxmny", "taxration/100*orisum" });
			// ��˰�ϼ�=���+˰��
			alFormula.add(new String[] { "oritaxsummny", "orisum+oritaxmny" });
			// ��˰����=��˰����*��1+˰�ʣ�
			alFormula.add(new String[] { "oritaxprice",
					"oriprice*(1+taxration/100)" });
			getCtBillCardPanel().setFormula("amount", alFormula);

			// ˰�ʱ�
			alFormula = new ArrayList();
			// ˰��=˰��*���
			alFormula.add(new String[] { "oritaxmny", "taxration/100*orisum" });
			// ��˰�ϼ�=���+˰��
			alFormula.add(new String[] { "oritaxsummny", "orisum+oritaxmny" });
			// ��˰����=��˰����*��1+˰�ʣ�
			alFormula.add(new String[] { "oritaxprice",
					"oriprice*(1+taxration/100)" });
			getCtBillCardPanel().setFormula("taxration", alFormula);
		}

		try {
			// ����ģʽ
			// true : �˷�ģʽ
			// false: ����ģʽ
			currArith = nc.ui.scm.pub.PubSetUI.getCurrArith(m_sPkCorp);
			// ���㷽ʽ
			// bCalType = currArith.isBlnLocalFrac(); // ��Ϊ �����Һ��� ,��Ϊ �����Һ���

			// String sFracCurrPk = currArith.getFracCurrPK();
			// CurrinfoVO fracVO = currArith.getCurrinfoVO(sFracCurrPk,
			// currArith
			// .getLocalCurrPK());
			// modified by lirr �޸�ԭ��uapɾ����ԭ����
			// deleted by lirr 2008-8-20 ����5.5����ɾ��������Ϣ

			CurrParamQuery cpq = CurrParamQuery.getInstance(); // ���㷽ʽ
			bCalType = cpq.isBlnLocalFrac(m_sPkCorp); // ��Ϊ �����Һ��� ,��Ϊ �����Һ���

			String sFracCurrPk = cpq.getFracCurrPK(m_sPkCorp);
			CurrinfoVO fracVO = currArith.getCurrinfoVO(sFracCurrPk, cpq
					.getLocalCurrPK(m_sPkCorp)); // ���ҶԱ��ҵ�����ģʽ
			bFracmode = fracVO.getConvmode().booleanValue();

		} catch (Exception e) {
			showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"SCMCOMMON", "UPPSCMCommon-000116")/* @res "��ʽ�������" */);
		}
	}

	/**
	 * �˴����뷽��˵���� ��������: �������: ����ֵ: �쳣����: ����:
	 * 
	 * @param bdData
	 *            nc.ui.pub.bill.BillListData
	 */
	protected void setListPanelByPara(BillListData bdData) {

		// ��õ���Ԫ�ض�Ӧ�Ŀؼ�
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

		// ���ü���״̬
		UIComboBox comActiveItem = (UIComboBox) bdData
				.getHeadItem("activeflag").getComponent();
		comActiveItem.setTranslate(true);
		comActiveItem.addItem(nc.ui.ml.NCLangRes.getInstance().getStrByID(
				"SCMCOMMON", "UPPSCMCommon-000244")/* @res "��" */);
		comActiveItem.addItem(nc.ui.ml.NCLangRes.getInstance().getStrByID(
				"SCMCOMMON", "UPPSCMCommon-000108")/* @res "��" */);
		comActiveItem.setSelectedIndex(0);
		bdData.getHeadItem("activeflag").setComponent(comActiveItem);
		bdData.getHeadItem("activeflag").setWithIndex(true);
	}

	/**
	 * ���ں�ͬ�вɹ������ۡ������Ȳ�ͬ�����࣬ ���Լ�ʹ��ͬһ�����ID,��Ӧ��Ĭ�ϸ���λҲ�᲻һ���� �Ӷ���Ӧ�Ļ�����Ҳ����ͬ�� ����: ��ѫƽ
	 * ��������: ���ظô���Ļ����� �������: strInvid java.lang.String ���id ����ֵ: UFDouble �쳣����:
	 * ����:2003-6-25 16:20
	 */
	public UFDouble getTransRate(String strInvid) {
		if (m_UFTransRate == null) {
		}
		return m_UFTransRate;
	}

	/**
	 * �����Ĺ��ܡ���;�������Եĸ��ģ��Լ�����ִ��ǰ������״̬�� �����������޸�
	 * 
	 * @param ����˵��
	 * @return ����ֵ
	 * @exception �쳣����
	 * @see ��Ҫ�μ�����������
	 * @since �������һ���汾���˷�������ӽ���������ѡ��
	 * @author zhongyue
	 * @param row
	 *            int
	 * @param col
	 *            int
	 */
	protected void afterFreeItemEdit(BillModel bm, int iRow) {

		try {
			// �õ�������vo
			FreeItemRefPane freeItemRef = (FreeItemRefPane) bm.getItemByKey(
					"vfree0").getComponent();
			FreeVO voFree = freeItemRef.getFreeVO();

			if (voFree != null) {
				Object oFreexName = null;
				Object oFreexValue = null;

				// ������������ݲ��Ϊ������1-5
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
	 * �˴����뷽��˵���� ��������:��ͬ���� ���ߣ����� �������: ����ֵ: �쳣����: ����:(2003-7-3 9:34:29)
	 */
	public void afterCtTypeEdit() {

		String pk = ((UIRefPane) getCtBillCardPanel().getHeadItem("ct_type")
				.getComponent()).getRefPK();

		if (pk == null || pk.length() <= 0) {
			showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"4020pub", "UPP4020pub-000092")/* @res "��ͬ���Ͳ���Ϊ��" */);
			return;
		}
		Object oRefValue = ((UIRefPane) getCtBillCardPanel().getHeadItem(
				"ct_type").getComponent()).getRefValue("ninvctlstyle");

		if (oRefValue != null) {

			int iInvctlstyle = Integer.parseInt(oRefValue.toString());

			// ���ѡ���˴����ͬ���ͣ����ж�ģ�����õĴ�������Ƿ�ɼ������������롢�����Ƿ����
			if (iInvctlstyle == 0) { // ���invcode���ɼ�
				if (getCtBillCardPanel().getBodyItem(CTTableCode.BASE,
						"invcode").isShow() == false) {
					showWarningMessage(nc.ui.ml.NCLangRes.getInstance()
							.getStrByID("4020pub", "UPP4020pub-000093")/*
																		 * @res
																		 * "��ѡ������������Ϊ����ĺ�ͬ���ͣ�����������Ӧ�ÿɼ������������õ���ģ�棡"
																		 */);
				}
				// ���invclasscode����
				if (getCtBillCardPanel().getBodyItem(CTTableCode.BASE,
						"invclasscode").isNull() == true
						|| getCtBillCardPanel().getBodyItem(CTTableCode.BASE,
								"invclassname").isNull() == true) {
					showWarningMessage(nc.ui.ml.NCLangRes.getInstance()
							.getStrByID("4020pub", "UPP4020pub-000094")/*
																		 * @res
																		 * "��ѡ������������Ϊ����ĺ�ͬ���ͣ������������������������Ʋ�ӦΪ��������������õ���ģ�棡"
																		 */);
				}

			}

			// ���ѡ���˴�������ͬ���ͣ����ж�ģ�����õĴ����������Ƿ�ɼ���������롢�����Ƿ����
			else if (iInvctlstyle == 1) { // ���invclasscode���ɼ�
				if (getCtBillCardPanel().getBodyItem(CTTableCode.BASE,
						"invclasscode").isShow() == false) {
					showWarningMessage(nc.ui.ml.NCLangRes.getInstance()
							.getStrByID("4020pub", "UPP4020pub-000095")/*
																		 * @res
																		 * "��ѡ������������Ϊ�������ĺ�ͬ���ͣ��������������Ӧ�ÿɼ������������õ���ģ�棡"
																		 */);
				}
				// ���invcode����
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
																		 * "��ѡ������������Ϊ�������ĺ�ͬ���ͣ�����������������ƻ��ͺŻ���������λ��ӦΪ��������������õ���ģ�棡"
																		 */);
				}

			}
			// �����ͬ���͵Ĵ�����Ʒ�ʽΪ�ա����������ʹ��������Ϊ�գ����ɱ༭��
			else if (iInvctlstyle == 2) {
				// ���ô��������룬�����������Ϊ��
				int iRowLen = getCtBillCardPanel().getRowCount();
				for (int i = 0; i < iRowLen; i++) {
					getCtBillCardPanel()
							.setBodyValueAt(null, i, "invclasscode");
					getCtBillCardPanel()
							.setBodyValueAt(null, i, "invclassname");
					getCtBillCardPanel().setBodyValueAt(null, i, "invclid");
					// ���ô�����룬�������,�ͺţ��������,������λΪ��
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
					// added by lirr 20092009-6-29����09:56:48 ��մ������id

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
	 * ��������:ԭ�ұ��� �༭���� �޸��ˣ��� �� �޸�����:(2005-06-21 9:34:29) �޸����ڣ�2008-10-23
	 * �޸�ԭ�򣺲������ɺ�ͬ �������¼�ʱcurrIDΪ�� ��ȽȽ
	 */
	public void afterCurrNameEdit(boolean isFromBill) {

		ManageItemVO[] itemVOs = (ManageItemVO[]) getCtBillCardPanel()
				.getBillModel().getBodyValueVOs(ManageItemVO.class.getName());

		for (int i = 0; i < itemVOs.length; i++) { // �������У�����Ϊ�༭״̬
			if (itemVOs[i].getStatus() != 2)
				getCtBillCardPanel().getBillModel().setRowState(i, 2);
		}
		// added by lirr 2008-11-10 ���Ч�����⣬ֻ�л��ʷ����仯�����¼������
		String sOldCurrate = null;
		if (null != getCtBillCardPanel().getHeadItem("currrate")
				&& null != getCtBillCardPanel().getHeadItem("currrate")
						.getValue()) {
			sOldCurrate = getCtBillCardPanel().getHeadItem("currrate")
					.getValue();
		}
		// ����۱�����
		getCtBillCardPanel().getHeadItem("currrate").clearViewData();
		// getCtBillCardPanel().getHeadItem("astcurrate").clearViewData(); //
		// �۸�����

		/*
		 * // �޸ĺ��ԭ�ұ���ID String currID = ((UIRefPane)
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
							"UPPSCMCommon-000015")/* @res "����δ���壬���ȶ�����֣�" */);
			return;
		}
		getCtBillCardPanel().getHeadItem("currid").setValue(currID);

		// ��ô˱��ֵ�С��λ��
		int currDigit = 2;
		if (m_hCurrDigit.containsKey(currID)) {
			currDigit = ((Integer) m_hCurrDigit.get(currID)).intValue();
		}
		// ����ԭ�ҽ��С��λ��
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

		// ��ͬ����ҳǩ�н��ľ���
		getCtBillCardPanel().getBodyItem(CTTableCode.COST, "expsum")
				.setDecimalDigits(currDigit);
		getCtBillListPanel().getBodyItem(CTTableCode.COST, "expsum")
				.setDecimalDigits(currDigit);

		// ����ǩ������ȡ�û���
		String subDate = getCtBillCardPanel().getHeadItem("subscribedate")
				.getValue();
		if (subDate == null || subDate.trim().length() == 0) {
			MessageDialog.showHintDlg(this, null, nc.ui.ml.NCLangRes
					.getInstance().getStrByID("4020pub", "UPP4020pub-000097")/*
																				 * @res
																				 * "�����Ҫ�õ�Ĭ�ϻ��ʣ����������ͬǩ�����ڣ�"
																				 */);
			return;
		}
		try {
			BusinessCurrencyRateUtil currRateUtil = new BusinessCurrencyRateUtil(
					m_sPkCorp);
			// modified by lirr 2009-8-27����08:26:09 �޸ķ���

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
		// modified by lirr 2008-12-24 �۱����ʾ����·��� since v55
		getCtBillCardPanel().getHeadItem("currrate").setDecimalDigits(
				getCurrateDigit(currID));
		// deleted by lirr 2008-8-20 ����5.5����ɾ��������Ϣ
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
		// modified by lirr 2008-11-22 �޸�ԭ�򣺱����������·�ʽ
		// ���������Ҽ��㹫ʽ
		// setFormularByCur(currID);
		// getCtBillCardPanel().calculateWhenHeadChanged("currrate");
		String sNewCurrate = getCtBillCardPanel().getHeadItem("currrate")
				.getValue();
		// modified by lirr 2008-11-22 �۱����ʱ仯����ԭ���Ĺ�ʽ����
		// ������calcCtLinePriceMny��ʽ����

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
	 * �˴����뷽��˵���� ��������:���� ���ߣ����� �������: ����ֵ: �쳣����: ����:(2003-7-3 13:23:58)
	 */
	public void afterCurrrateEdit(nc.ui.pub.bill.BillEditEvent e) {
		if (e.getValue() == null
				|| new UFDouble(e.getValue().toString()).doubleValue() <= 0) {
			MessageDialog.showErrorDlg(this, null, nc.ui.ml.NCLangRes
					.getInstance().getStrByID("4020pub", "UPP4020pub-000098")/*
																				 * @res
																				 * "���ʲ���Ϊ�գ�Ҳ����С�ڵ���0��"
																				 */);
			getCtBillCardPanel().getHeadItem("currrate").clearViewData();
		}

		ManageItemVO[] itemVOs = (ManageItemVO[]) getCtBillCardPanel()
				.getBillModel().getBodyValueVOs(ManageItemVO.class.getName());

		// �޸Ļ��ʺ󣬽����еķ�������������Ϊ�޸�״̬�����屾����Ϣ���޸ģ�
		for (int i = 0; i < itemVOs.length; i++) { // �������У�����Ϊ�༭״̬
			if (itemVOs[i].getStatus() != 2)
				getCtBillCardPanel().getBillModel().setRowState(i, 2);
		}
		// added by lirr 2008-10-30 ���ʱ仯�� ������û������
		UFBoolean bOrimode = null;
		setFormularForNat(bOrimode);
		// getCtBillCardPanel().calculateWhenHeadChanged("currrate");
		String sNewCurrate = getCtBillCardPanel().getHeadItem("currrate")
				.getValue();
		// modified by lirr 2008-11-22 �۱����ʱ仯����ԭ���Ĺ�ʽ����
		// ������calcCtLinePriceMny��ʽ����
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
	 * �˴����뷽��˵���� ��������:���� ���ߣ����� �������: ����ֵ: �쳣����: ����:(2003-7-3 9:34:29) �޸ģ�cqw
	 * �޸�����:2003-10-08 �ı����/��Ӧ�������������Ϣ��Ϊ�����滻��ص���Ϣ�������������ϢΪ�գ��������Ϣ������
	 * �޸����ڣ�2008-10-23 �޸�ԭ�򣺲������ɺ�ͬ �������¼�ʱmanPkΪ�� ��ȽȽ
	 */
	public void afterCustNameEdit(Boolean isFromBill) {

		/*
		 * // �Զ������Ӧ����Ĭ�ϵı��� String manPk = ((UIRefPane) getCtBillCardPanel()
		 * .getHeadItem("custname").getComponent()).getRefPK();
		 */
		// added by lirr 2008-11-10 ���Ч�����⣬ֻ�л��ʷ����仯�����¼������
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
			// modified by liuzy 2008-10-29 ����09:58:46 ֻ��������ͬ��¼�빩Ӧ��ʱ��Ҫ����ͬ����
			/*
			 * if (m_sBillType.equals(nc.vo.scm.constant.ScmConst.CT_OTHER) &&
			 * oRefValue == null) { MessageDialog.showHintDlg(this, null,
			 * nc.ui.ml.NCLangRes .getInstance().getStrByID("4020pub",
			 * "UPP4020pub-000026") @res "����ѡ���ͬ����" ); }
			 */
		} else {
			manPk = getCtBillCardPanel().getHeadItem("custname").getValue();
		}

		if (manPk == null || manPk.length() <= 0)
			return;

		ManageItemVO[] itemVOs = (ManageItemVO[]) getCtBillCardPanel()
				.getBillModel().getBodyValueVOs(ManageItemVO.class.getName());

		// �޸ı��ֺ󣬽����еķ�������������Ϊ�޸�״̬�����屾����Ϣ���޸ģ�
		for (int i = 0; i < itemVOs.length; i++) { // �������У�����Ϊ�༭״̬
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
				// modified by lirr 2009-9-3����03:57:10 ��������
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
				// ��ô˱��ֵ�С��λ��
				String pk = ((UIRefPane) getCtBillCardPanel().getHeadItem(
						"currname").getComponent()).getRefPK();
				int currDigit = 2;
				if (pk != null && m_hCurrDigit.containsKey(pk)) {
					currDigit = ((Integer) m_hCurrDigit.get(pk)).intValue();
				}
				// ����ԭ�ҽ��С��λ��
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

				// ���û��ʡ����ʾ��ȡ��Ƿ�ɱ༭
				String subDate = getCtBillCardPanel().getHeadItem(
						"subscribedate").getValue();

				if (subDate == null || subDate.trim().length() == 0) {
					MessageDialog.showHintDlg(this, null, nc.ui.ml.NCLangRes
							.getInstance().getStrByID("4020pub",
									"UPP4020pub-000097")/*
														 * @res
														 * "�����Ҫ�õ�Ĭ�ϻ��ʣ����������ͬǩ�����ڣ�"
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
				// modified by lirr 2008-12-24 �۱����ʾ����·��� since v55
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
				// modified by lirr 2008-11-22 �޸�ԭ�򣺱����������·�ʽ
				// ���������Ҽ��㹫ʽ
				// setFormularByCur(pk);
				// getCtBillCardPanel().calculateWhenHeadChanged("currrate");
				String sNewCurrate = getCtBillCardPanel().getHeadItem(
						"currrate").getValue();
				// modified by lirr 2008-11-22 �۱����ʱ仯����ԭ���Ĺ�ʽ����
				// ������calcCtLinePriceMny��ʽ����

				/*
				 * if(!sOldCurrate.equals(sNewCurrate)){
				 * getCtBillCardPanel().getBillModel(CTTableCode.BASE).execFormulas(getFormularForNat(sNewCurrate),0,getCtBillCardPanel().getRowCount()); }
				 */
//				for (int i = 0; i < getCtBillCardPanel().getRowCount(); i++) {  //wanglei 2013-12-19 ����ע��
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
	 * �˴����뷽��˵���� ��������:�ƻ��շ������� ���ߣ����� �������: ����ֵ: �쳣����: ����:(2003-7-3 13:23:58)
	 */
	public void afterDelivDateEdit(nc.ui.pub.bill.BillEditEvent e) {
		Object oDelivdate = e.getValue();
		if (oDelivdate != null && oDelivdate.toString().trim().length() > 0) {

			Object oSubDate = getCtBillCardPanel().getHeadItem("subscribedate")
					.getValue();
			Object oInvlliDate = getCtBillCardPanel()
					.getHeadItem("invallidate").getValue();

			// �����ͬǩ������Ϊ��
			if (oSubDate == null || oSubDate.toString().trim().length() <= 0) {
				MessageDialog.showWarningDlg(this, null, nc.ui.ml.NCLangRes
						.getInstance().getStrByID("4020pub",
								"UPP4020pub-000099")/*
													 * @res "��ͬǩ�����ڲ���Ϊ�ա�"
													 */);

			} else if (oInvlliDate == null
					|| oInvlliDate.toString().trim().length() <= 0) {
				MessageDialog.showWarningDlg(this, null, nc.ui.ml.NCLangRes
						.getInstance().getStrByID("4020pub",
								"UPP4020pub-000100")/*
													 * @res "�ƻ���ֹ���ڲ���Ϊ�ա�"
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
														 * "�ƻ��շ�������Ӧ���ں�ͬǩ�����ڲ���С�ں�ͬ�ƻ���ֹ���ڡ�"
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
	 * �˴����뷽��˵���� ��������:���� ���ߣ����� �������: ����ֵ: �쳣����: ����:(2003-7-3 9:34:29)
	 */
	public void afterDepNameEdit() {
		// deleted by lirr 2009-11-6����10:30:22 ������Ų��������Ա
		//getCtBillCardPanel().getHeadItem("pername").clearViewData();

		String pk = ((UIRefPane) getCtBillCardPanel().getHeadItem("depname")
				.getComponent()).getRefPK();

		if (pk != null) {
			getCtBillCardPanel().getHeadItem("depid").setValue(pk);
			// deleted by lirr 2009-11-6����10:34:10 ���ٸ��ݲ��ű任��Ա
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
	 * �˴����뷽��˵���� ��������:�ƻ���ֹ���� ���ߣ����� �������: ����ֵ: �쳣����: ����:(2003-7-3 13:23:58)
	 */
	public void afterInvalliDateEdit(nc.ui.pub.bill.BillEditEvent e) {
		// У��ǩ�����ڡ���Ч���ڡ�ʧЧ���ڵĴ�С��ϵ
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
														 * "�ƻ���ֹ���ڲ����ڼƻ���Ч����֮ǰ��Ҳ������ƻ���Ч����ͬ�죡"
														 */);

					getCtBillCardPanel().getHeadItem("invallidate")
							.clearViewData();
				}
			}
		}
	}

	/**
	 * �����޸� �������ڣ�(2002-5-31 9:36:55)
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
	 * �˴����뷽��˵���� ��������:��ͬǩ������ ���ߣ����� �������: ����ֵ: �쳣����: ����:(2003-7-3 13:23:58)
	 */
	public void afterSubscribeDateEdit(nc.ui.pub.bill.BillEditEvent e) {
		// У��ǩ�����ڡ���Ч���ڡ�ʧЧ���ڵĴ�С��ϵ
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
														 * "ǩ�����ڲ����ڼƻ���Ч����֮��"
														 */);
					getCtBillCardPanel().getHeadItem("subscribedate")
							.clearViewData();
				}
			}
			// ���ù�Ӧ�̸ı��¼������Ĭ�ϻ��ʵ�
			//afterCustNameEdit(m_bAddFromBillFlag);  //wanglei 2013-12-19 �����޸�
		}
	}

	/**
	 * �˴����뷽��˵���� ��������:˰�� ���ߣ����� �������: ����ֵ: �쳣����: ����:(2003-7-3 13:23:58) �޸ģ�cqw
	 * ˰�ʱ༭�������ֵ����С���㡣[��Ϊ�������ĺ�ͬ��˰�ʿ���Ϊ��] ˰�ʵķǿռ��ͳһ������ʱ��顣
	 */
	public void afterTaxrationEdit(nc.ui.pub.bill.BillEditEvent e) {

		if (e.getValue() != null
				&& new UFDouble(e.getValue().toString()).doubleValue() < 0) {
			MessageDialog.showErrorDlg(this, null, nc.ui.ml.NCLangRes
					.getInstance().getStrByID("SCMCOMMON",
							"UPPSCMCommon-000264")/* @res "˰�ʲ���С��0��" */);
			return;
		}
	}

	/**
	 * �˴����뷽��˵���� ��������:�ƻ���ֹ���� ���ߣ����� �������: ����ֵ: �쳣����: ����:(2003-7-3 13:23:58)
	 */
	public void afterValDateEdit(nc.ui.pub.bill.BillEditEvent e) {
		// У��ǩ�����ڡ���Ч���ڡ�ʧЧ���ڵĴ�С��ϵ
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
														 * "�ƻ���Ч���ڲ�����ǩ������֮ǰ��"
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
														 * "�ƻ���Ч���ڲ����ڼƻ���ֹ����֮��Ҳ������ƻ���ֹ����ͬ�죡"
														 */);
					getCtBillCardPanel().getHeadItem("valdate").clearViewData();
				}
			}
		}
	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2002-6-27 16:16:11)
	 */
	protected boolean checkVO(AggregatedValueObject vo, ScmBillCardPanel bcp) {
		try {
			if (!bcp.checkVO((DMVO) vo, true, true))
				return false;
			return checkOther(vo, bcp);
		} catch (ICDateException e) {
			// ��ʾ��ʾ
			String sErrorMessage = GeneralMethod.getBodyErrorMessage(bcp, e
					.getErrorRowNums(), e.getHint());
			showErrorMessage(sErrorMessage);
			return false;
		} catch (ICNullFieldException e) {
			// ��ʾ��ʾ
			String sErrorMessage = GeneralMethod.getBodyErrorMessage(bcp, e
					.getErrorRowNums(), e.getHint());
			showErrorMessage(sErrorMessage);
			return false;
		} catch (ICNumException e) {
			// ��ʾ��ʾ
			String sErrorMessage = GeneralMethod.getBodyErrorMessage(bcp, e
					.getErrorRowNums(), e.getHint());
			showErrorMessage(sErrorMessage);
			return false;
		} catch (ICPriceException e) {
			// ��ʾ��ʾ
			String sErrorMessage = GeneralMethod.getBodyErrorMessage(bcp, e
					.getErrorRowNums(), e.getHint());
			showErrorMessage(sErrorMessage);
			return false;
		} catch (ICSNException e) {
			// ��ʾ��ʾ
			String sErrorMessage = GeneralMethod.getBodyErrorMessage(bcp, e
					.getErrorRowNums(), e.getHint());
			showErrorMessage(sErrorMessage);
			return false;
		} catch (ICLocatorException e) {
			// ��ʾ��ʾ
			String sErrorMessage = GeneralMethod.getBodyErrorMessage(bcp, e
					.getErrorRowNums(), e.getHint());
			showErrorMessage(sErrorMessage);
			return false;
		} catch (ICRepeatException e) {
			// ��ʾ��ʾ
			String sErrorMessage = GeneralMethod.getBodyErrorMessage(bcp, e
					.getErrorRowNums(), e.getHint());
			showErrorMessage(sErrorMessage);
			return false;
		} catch (ICHeaderNullFieldException e) {
			// ��ʾ��ʾ
			String sErrorMessage = GeneralMethod.getHeaderErrorMessage(bcp, e
					.getErrorRowNums(), e.getHint());
			showErrorMessage(sErrorMessage);
			return false;
		} catch (NullFieldException e) {
			showErrorMessage(e.getHint());
			return false;
		} catch (ValidationException e) {
			String sMessage = nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"4020pub", "UPP4020pub-000106")/* @res "У���쳣������δ֪����..." */;
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
	 * �����ߣ������� ���ܣ����������� ������ ���أ� ���⣺ ���ڣ�(2001-5-16 ���� 6:32) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
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
	 * �����Ĺ��ܡ���;�������Եĸ��ģ��Լ�����ִ��ǰ������״̬�� �����������ʼ��
	 * 
	 * @param ����˵��
	 * @return ����ֵ
	 * @exception �쳣����
	 * @see ��Ҫ�μ�����������
	 * @since �������һ���汾���˷�������ӽ���������ѡ��
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
	 * ���ܣ�������Ϣ������������λ�� ������ ���أ� ���⣺ ���ڣ�(2002-8-1 20:24:56) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
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
	 * ���ܣ�������Ϣ������������λ�� ������ ���أ� ���⣺ ���ڣ�(2002-8-1 20:24:56) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 */
	protected InvVO getInvInfo(String sInv, String sAstID) {
		return null;
	}

	/**
	 * ÿ�������׳��쳣ʱ������
	 * 
	 * @param exception
	 *            java.lang.Throwable
	 */
	protected void handleException(Exception exception) {
		/* ��ȥ���и��е�ע�ͣ��Խ�δ��׽�����쳣��ӡ�� stdout�� */
		SCMEnv.out("--------- δ��׽�����쳣 ---------"); /*-=notranslate=-*/
		SCMEnv.out(exception);
	}

	/**
	 * �����Ĺ��ܡ���;�������Եĸ��ģ��Լ�����ִ��ǰ������״̬�� �����������ʼ��
	 * 
	 * @param ����˵��
	 * @return ����ֵ
	 * @exception �쳣����
	 * @see ��Ҫ�μ�����������
	 * @since �������һ���汾���˷�������ӽ���������ѡ��
	 * @author zhongyue
	 * @param row
	 *            int
	 * @param col
	 *            int
	 */
	protected void initBodyFreeItem(int row, int col, ScmBillCardPanel dmbcp) {
		try {
			// ȡֵ
			InvVO invvo = getBodyInvVOFromPanel(row, col, dmbcp);

			// ����������մ�������
			// ��ʼ��
			((FreeItemRefPane) getCtBillCardPanel().getBodyItem("vfree0")
					.getComponent()).setFreeItemParam(invvo);
			((FreeItemRefPane) getCtBillCardPanel().getBodyItem("vfree0")
					.getComponent()).setEditable(false);

		} catch (Exception e) {
		}
	}

	/**
	 * �˴����뷽��˵���� ���ܣ���ʼ��ÿһ��Ĳ��� ������ ���أ� ���⣺ ���ڣ�(2002-5-8 19:04:08)
	 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־�� 2002-06-19 ���� �޸� ��1������жϺ���ʾ������֯ѡ����ʾ��
	 * ��2�����m_OrgNoShowFlag��������getOrgnoshowFlag��setOrgnoshowFlag����
	 */
	public void initRef(int rownow, int colnow, ScmBillCardPanel bcp) {
		String sItemKey = bcp.getBillModel().getBodyKeyByCol(colnow).trim();
		if (!bcp.getBillData().getBodyItem(CTTableCode.BASE, sItemKey)
				.isEnabled()) {
			return;
		}
	}

	/**
	 * �˴����뷽��˵���� ��������:��ӡ �������: ����ֵ: �쳣����: ����: �޸�˵�������Ӵ�ӡ�������� �޸��ߣ��۱� 2004-12-08
	 */
	private void onCardPrint(BillCardPanel bcp) {
		/* ���Ӵ�ӡ�������ƺ�Ĵ�ӡ���� */
		showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4020pub",
				"UPP4020pub-000107")/* @res "���ڴ�ӡ�����Ժ�..." */);
		// ׼�����ݣ����Ҫ��ӡ��vo.
		/*---��ͬ������Ϣ���ͬ����ĺϲ���ӡ---*/
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
		/*---׼�����ݽ���---*/

		// ��ͬ��������
		ManageHeaderVO headerVO = (ManageHeaderVO) vo.getParentVO();
		String sBillID = headerVO.getPrimaryKey();

		// ����PrintLogClient������PrintInfo.
		ScmPrintlogVO voSpl = new ScmPrintlogVO();
		voSpl.setCbillid(sBillID); // ���������ID
		voSpl.setVbillcode(headerVO.getCt_code());// �����ͬ�ţ�������ʾ��
		voSpl.setCbilltypecode(getBillType());
		voSpl.setCoperatorid(getOperator());
		voSpl.setIoperatetype(new Integer(PrintConst.PAT_OK));// �̶�
		voSpl.setPk_corp(getCorpPrimaryKey());
		voSpl.setTs(headerVO.getTs());// ���������TS

		nc.ui.scm.print.PrintLogClient plc = new nc.ui.scm.print.PrintLogClient();
		// ���õ�����Ϣ
		plc.setPrintInfo(voSpl);
		// ����TSˢ�¼���.
		plc.addFreshTsListener(new FreshTsListener());
		// ���ô�ӡ����
		getPrintEntry().setPrintListener(plc);

		plc.setPrintEntry(getPrintEntry());// ���ڵ���ʱ
		// ���õ�����Ϣ
		plc.setPrintInfo(voSpl);

		// ���ӡ��������Դ�����д�ӡ
		getDataSource().setBillVO(vo);
		getPrintEntry().setDataSource(getDataSource());
		getPrintEntry().print();

		MessageDialog.showHintDlg(this, null, plc.getPrintResultMsg(false));

		showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
				"SCMCOMMON", "UPPSCMCommon-000133")/* @res "����" */);

	}

	/**
	 * �˴����뷽��˵���� ��������:��ӡ���� �������: ����ֵ: �쳣����: ����:2003-7-7 10:27
	 */
	public void onPrint() {
		// m_bIsCard ��־������Card��ʽ,����List��ʽ
		// bill card
		// addied by liuzy 2008-01-10 ��Ŀ����ϲ� ����ţ�200712141125493518
		// cy Ϊ��ÿ��Ԥ��������ѡ���ӡģ�棬��Ҫ���³�ʼ��
		// UAP
		// ���棺�����ڵĴ�ӡģ��ѡ�����⣬����Ϊ���ǵĽ���һֱʹ��ͬһ��PrintEntry����ʵ���������Ƕ�PrintEntry����ʵ����ε��ô�ӡ֧�ֲ�ȫ�棬����ǰ�������ǵ���ص��ô��룬Ӧ����һ��getPrintEntry()������
		// if(this.printEntry != null) return this.printEntry; else return new
		// PrintEntry()�����ԣ��򵥵Ľ����ʽ, ÿ�ζ�newһ��PrintEntry���ء�

		newPrintEntry();

		if (m_bIsCard) {
			onCardPrint(getCtBillCardPanel());

		} else { // �б��ӡ
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
	 * �˴����뷽��˵���� ��������: �������: ����ֵ: �쳣����: ����:
	 */
	public void onPrintPreview() {
		// m_bIsCard ��־������Card��ʽ,����List��ʽ
		// card
		// addied by liuzy 2008-01-10 ��Ŀ����ϲ� ����ţ�200712141125493518
		// cy Ϊ��ÿ��Ԥ��������ѡ���ӡģ�棬��Ҫ���³�ʼ��
		// UAP
		// ���棺�����ڵĴ�ӡģ��ѡ�����⣬����Ϊ���ǵĽ���һֱʹ��ͬһ��PrintEntry����ʵ���������Ƕ�PrintEntry����ʵ����ε��ô�ӡ֧�ֲ�ȫ�棬����ǰ�������ǵ���ص��ô��룬Ӧ����һ��getPrintEntry()������
		// if(this.printEntry != null) return this.printEntry; else return new
		// PrintEntry()�����ԣ��򵥵Ľ����ʽ, ÿ�ζ�newһ��PrintEntry���ء�

		newPrintEntry();

		if (m_bIsCard) {
			onCardPrintPreview(getCtBillCardPanel());
		} else { // �б���Ԥ��
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
	 * �˴����뷽��˵���� ��������: �������: ����ֵ: �쳣����: ����:
	 * 
	 * @param sCurrentID
	 *            java.lang.String
	 */
	/*
	 * protected void setFormularByCur(String sCurrentID) { // ԭ�ҶԸ��ҵ�����ģʽ
	 * UFBoolean bOrimode = null;
	 * 
	 * try { // sCurrentID ����ID // CurrinfoVO currVO =
	 * currArith.getCurrinfoVO(sCurrentID, currArith // .getFracCurrPK()); //
	 * modidied by lirr 2008-07-10 �޸�ԭ��UAPȥ����ԭ���� CurrParamQuery cpq =
	 * CurrParamQuery.getInstance(); CurrinfoVO currVO =
	 * currArith.getCurrinfoVO(sCurrentID, cpq .getLocalCurrPK(m_sPkCorp)); //
	 * ԭ�ҶԸ��ҵ�����ģʽ bOrimode = currVO.getConvmode(); } catch (BusinessException e) { //
	 * eat exception SCMEnv.out("δ�ܻ�ô˱��ֶ�Ӧ������ģʽ!"); } // ���ԭ���Ǳ�λ�ң���ôֻ�۸����۱� if
	 * (m_sLocalCurrID != null && m_sLocalCurrID.equals(sCurrentID)) { //
	 * deleted by lirr 2008-8-20 ����5.5����ɾ��������Ϣ // ���������Ϣ
	 * 
	 * int irowcount = getCtBillCardPanel().getRowCount(); String[] sAstField =
	 * new String[] { "astprice", "astsum", "asttaxprice", "asttaxmny",
	 * "asttaxsummny" }; for (int i = 0; i < irowcount; i++)
	 * getCtBillCardPanel().getBillModel().clearRowData(i, sAstField); // ��ʽ
	 * setFormularForNat(bOrimode); return; } // deleted by lirr 2008-8-20
	 * ����5.5����ɾ��������Ϣ // if (bCalType) { // �����Һ��㷽ʽ //
	 * setFormularForNatAst(bOrimode); // } else { // �����Һ��㷽ʽ
	 * setFormularForNat(bOrimode); // } }
	 */

	/**
	 * ���ߣ����� �������� �������ڣ�(2002-6-11 9:47:30)
	 * 
	 * @param sInv
	 *            java.lang.String[]
	 */
	protected void setInvItemEditable(ScmBillCardPanel bcp) {
		// ��ý�������
		DMVO dvo = (DMVO) bcp.getBillValueVO(DMVO.class.getName(),
				DMDataVO.class.getName(), DMDataVO.class.getName());
		DMDataVO[] dmdvos = dvo.getBodyVOs();
		// ��ô������������������
		String[] invkeys = new String[dmdvos.length];
		String[] astkeys = new String[dmdvos.length];
		for (int i = 0; i < dmdvos.length; i++) {
			invkeys[i] = (String) dmdvos[i].getAttributeValue("invid");
			astkeys[i] = (String) dmdvos[i].getAttributeValue("astmeasid");
		}
		// ��ô����Ϣ
		InvVO[] invvos = getInvInfo(invkeys, astkeys);
		// �ô�������Ƿ�ɱ༭
		for (int i = 0; i < dmdvos.length; i++) {
			// �Ƿ񸨼���
			// ������
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
	 * �˴����뷽��˵���� ���ܣ������������ƶ�ʱ���ոս�������и�ʱ���� ������ ���أ� ���⣺ ���ڣ�(2002-8-6 13:50:21)
	 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
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
		// ����յȶ���������ֵ
		if (null != bcp.getBodyItem(CTTableCode.BASE, key)
				&& bcp.getBodyItem(CTTableCode.BASE, key).getComponent() instanceof UIRefPane) {
			((UIRefPane) bcp.getBodyItem(CTTableCode.BASE, key).getComponent())
					.setValue(value == null ? null : value.toString());
		}
		// ��������ݲ���
		if (key.equals("vfree0"))
			initBodyFreeItem(row, col, bcp);

		// �򸨼������ݲ���
		if (key.equals("astmeascode")) {
			String sInvID = (String) bcp.getBodyValueAt(row, "invid");
			if (null != sInvID) {
				filterMeas(sInvID, key, m_sPkCorp);
			}
		}
	}

	/**
	 * �˴����뷽��˵���� ��������:������Ҫ��ӡ���б����ݵ�VO �������: ����ֵ:java.util.ArrayList �쳣����: ����:
	 */
	public ArrayList getAllVOs() {
		return m_alAllVOs;
	}

	/**
	 * �˴����뷽��˵���� ��������:������Ҫ��ӡ���б����ݵ�VO �������:java.util.ArrayList ����ֵ: �쳣����: ����:
	 * 
	 * @param newAllVOs
	 *            java.util.ArrayList
	 */
	public void setAllVOs(java.util.ArrayList newAllVOs) {
		m_alAllVOs = newAllVOs;
	}

	/**
	 * �˴����뷽��˵���� ��������:��ӡ �������: ����ֵ: �쳣����: ����: �޸�˵�������Ӵ�ӡ�������� �޸��ߣ��۱� 2004-12-08
	 * 
	 * @param bcp
	 *            nc.ui.pub.bill.BillCardPanel
	 */
	public void onCardPrintPreview(BillCardPanel bcp) {
		/*---- �޸Ĵ�ӡ������Ĵ�ӡԤ����----*/
		// ׼������
		/*---��ͬ������Ϣ���ͬ����ĺϲ���ӡ---*/
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
		/*---׼�����ݽ���---*/

		// ��ͬ��������
		ManageHeaderVO headerVO = (ManageHeaderVO) vo.getParentVO();
		String sBillID = headerVO.getPrimaryKey();

		// ����PringLogClient�Լ�����PrintInfo
		ScmPrintlogVO voSpl = new ScmPrintlogVO();
		voSpl = new ScmPrintlogVO();
		voSpl.setCbillid(sBillID); // ���������ID
		voSpl.setVbillcode(headerVO.getCt_code());// �����ͬ�ţ�������ʾ��
		voSpl.setCbilltypecode(getBillType());
		voSpl.setCoperatorid(getOperator());
		voSpl.setIoperatetype(new Integer(PrintConst.PAT_OK));// �̶�
		voSpl.setPk_corp(getCorpPrimaryKey());
		voSpl.setTs(headerVO.getTs());// ���������TS

		nc.ui.scm.print.PrintLogClient plc = new nc.ui.scm.print.PrintLogClient();
		plc.setPrintEntry(getPrintEntry());
		// ���õ�����Ϣ
		plc.setPrintInfo(voSpl);
		// ����ts��printcountˢ�¼���.
		plc.addFreshTsListener(new FreshTsListener());
		// ���ô�ӡ����
		getPrintEntry().setPrintListener(plc);

		// ��ӡԤ��
		getDataSource().setBillVO(vo);
		getPrintEntry().setDataSource(getDataSource());
		getPrintEntry().preview();

		showHintMessage(plc.getPrintResultMsg(true));
	}

	/*
	 * �õ��б���ѡ��ĵ��� �۱� 2005-08-09
	 */
	protected ArrayList getSelectedBills() {
		// ׼������
		int iSelListHeadRowCount = getCtBillListPanel().getHeadTable()
				.getSelectedRowCount();
		if (iSelListHeadRowCount <= 0) {
			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"SCMCOMMON", "UPPSCMCommon-000167")/* @res "û��ѡ�񵥾�!" */);
			return null;
		}

		// �õ�ѡ�񵥾ݵ�������
		int[] arySelListHeadRows = new int[iSelListHeadRowCount];
		arySelListHeadRows = getCtBillListPanel().getHeadTable()
				.getSelectedRows();

		ArrayList alvos = new ArrayList();
		ExtendManageVO voExtend = null;

		// modified by lirr 2008-07-25 �޸�ԭ���޸�ǰֻ�ܴ�ӡ��һ����ͬ�ı�ͷ�ͱ��壬�����ĺ�ֻͬ��ӡ��ͷ

		/*
		 * for (int i = 0; i < iSelListHeadRowCount; i++) { voExtend =
		 * (ExtendManageVO) m_vBillVO .elementAt(arySelListHeadRows[i]);
		 * alvos.add(voExtend); }
		 */

		int index = 0;
		ManageVO[] allDataManagevos = new ManageVO[iSelListHeadRowCount];// �������б���ĺ�ͬ����
		ManageVO[] allVoOnlyHaveHead = new ManageVO[iSelListHeadRowCount];// ֻ�е�һ����¼�б���ĺ�ͬ����
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
			// ��ѯ���ݿ� ��ú������б���ĺ�ͬ����
			allDataManagevos = ContractQueryHelper
					.queryAllArrBodyData(allVoOnlyHaveHead);
		} catch (Exception e) {
			// TODO �Զ����� catch ��
			// e.printStackTrace();
			SCMEnv.out(e.getMessage());

		}
		// modified by liuzy 2008-09-08 ˢ��ʽ
		execFormularAfterQueryWithoutCache(allDataManagevos);
		// added by lirr �����ӡ���� �ϲ����
		for (int i = 0; i < allDataManagevos.length; i++) {
			if (allDataManagevos[i].getParentVO().getPrimaryKey().equals(
					pkHasItem)) {
				allDataManagevos[i].setTableVO("table", getCtBillListPanel()
						.getBodyBillModel("table").getBodyValueVOs(
								ManageItemVO.class.getName()));
			}

		}

		// ��ManageVO[] allVoOnlyHaveHead��ת��ΪExtendManageVO ���Ͳ�����alvos
		if (null != allDataManagevos && allDataManagevos.length > 0) {
			// added by lirr 2008-11-28 ��ӡģ�����þ���
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
				// TODO �Զ����� catch ��
				GenMethod.handleException(this, e.getMessage(), e);
			}

		}

		return getValueForListPrint(alvos, arySelListHeadRows);

	}

	/*
	 * Ϊ��֧������ӡ ��Ҫ�õ����е�ѡ���еı������� added by lirr 2008-07-24
	 */
	protected ArrayList getSelectedBillsForListPrint() {
		// ׼������
		int iSelListHeadRowCount = getCtBillListPanel().getHeadTable()
				.getSelectedRowCount();
		if (iSelListHeadRowCount <= 0) {
			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"SCMCOMMON", "UPPSCMCommon-000167")/* @res "û��ѡ�񵥾�!" */);
			return null;
		}

		// �õ�ѡ�񵥾ݵ�������
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

			// modified by lirr �޸�ǰֻ��ú�ͬͷ�Լ���һ����ͬ�ı��壬Ϊ��֧������ӡ�������к�ͬ��

			ManageHeaderVO voHead = (ManageHeaderVO) voExtend.getParentVO();
			allVoOnlyHaveHead[i] = new ManageVO();
			allVoOnlyHaveHead[i].setParentVO(voHead);
		}

		try {
			allDataManagevos = ContractQueryHelper
					.queryAllArrBodyData(allVoOnlyHaveHead);
		} catch (Exception e) {
			// TODO �Զ����� catch ��
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
	 * �õ��б���ѡ��ĵ�һ�ŵ���(�����ѡ) �۱� 2005-08-09
	 */
	protected ArrayList getFirstSelectedBill() {
		// ׼������
		int iSelListHeadRowCount = getCtBillListPanel().getHeadTable()
				.getSelectedRowCount();
		if (iSelListHeadRowCount <= 0) {
			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"SCMCOMMON", "UPPSCMCommon-000167")/* @res "û��ѡ�񵥾�!" */);
			return null;
		}

		// �õ�ѡ�񵥾ݵ�������
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
	 * �б��ӡ �޸�˵�������Ӵ�ӡ�������� �޸��ߣ��۱� 2004-12-08
	 */
	public void onListPrint() {
		// �õ�Ҫ��ӡ���б�vo,ArrayList.
		ArrayList alPrintVOs = getSelectedBills();
		if (alPrintVOs == null)
			return;

		/* ���Ӵ�ӡ�������ƺ�Ĵ�ӡ���� */
		showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4020pub",
				"UPP4020pub-000107")/* @res "���ڴ�ӡ�����Ժ�..." */);

		SCMMutiTabPrintDataInterface ds = null;
		ExtendManageVO mvo = null;
		// ��ͬ��������
		ManageHeaderVO headerVO = null;

		nc.ui.scm.print.PrintLogClient plc = new nc.ui.scm.print.PrintLogClient();
		plc.setBatchPrint(true);// ����������

		getPrintEntry().beginBatchPrint();
		// ���ô�ӡ����
		getPrintEntry().setPrintListener(plc);
		plc.setPrintEntry(getPrintEntry());
		// ��ӡ����
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
				voSpl.setIoperatetype(new Integer(PrintConst.PAT_OK));// �̶�
				voSpl.setPk_corp(getCorpPrimaryKey());
				voSpl.setCbillid(headerVO.getPrimaryKey()); // ���������ID
				voSpl.setVbillcode(headerVO.getCt_code());// �����ͬ�ţ�������ʾ��
				voSpl.setTs(headerVO.getTs());// ���������TS

				// ���õ�����Ϣ
				plc.setPrintInfo(voSpl);
				// ����TSˢ�¼���.
				plc.addFreshTsListener(new FreshTsListener());

				if (plc.check()) {// ���ͨ����ִ�д�ӡ���д���Ļ��Զ������ӡ��־�����ﲻ�ô���

					ds = getNewDataSource();
					ds.setBillVO(mvo);
					getPrintEntry().setDataSource(ds);

					// ����������Setup����С���У����ֳ������׶�������
					/*
					 * while (getPrintEntry().dsCountInPool() >
					 * PrintConst.PL_MAX_TAST_NUM) {
					 * Thread.sleep(PrintConst.PL_SLEEP_TIME); //
					 * �����PL_MAX_TAST_NUM���������񣬾͵ȴ�PL_SLEEP_TIME�롣 }
					 */
				}
			}
			getPrintEntry().endBatchPrint();
			MessageDialog.showHintDlg(this, null, plc.getPrintResultMsg(false));

		} catch (Exception e) {
			nc.vo.scm.pub.SCMEnv.out(e);
			showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"4020pub", "UPP4020pub-000108")/* @res "��ӡ����" */
					+ "\n" + e.getMessage());
		}

		showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
				"SCMCOMMON", "UPPSCMCommon-000133")/* @res "����" */);
	}

	/**
	 * �б���Ԥ�� �޸�˵�������Ӵ�ӡ�������� �޸��ߣ��۱� 2004-12-08
	 */
	public void onListPrintPreview() {
		// ׼�����ݣ����Ҫ��ӡ��vo.
		// �õ�Ҫ��ӡ���б�vo,ArrayList.
		ArrayList alPrintVOs = getSelectedBills();
		if (alPrintVOs == null)
			return;

		// ���Ӵ�ӡ�������ƺ�Ĵ�ӡ����
		showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4020pub",
				"UPP4020pub-000109") /* @res "�������ɵ�һ�ŵ��ݵĴ�ӡԤ�����ݣ����Ժ�..." */);

		ExtendManageVO dmvo = (ExtendManageVO) alPrintVOs.get(0);
		// ��ͬ��������
		ManageHeaderVO headerVO = (ManageHeaderVO) dmvo.getParentVO();
		String sBillID = headerVO.getPrimaryKey();

		// ����PringLogClient�Լ�����PrintInfo
		ScmPrintlogVO voSpl = new ScmPrintlogVO();
		voSpl = new ScmPrintlogVO();
		voSpl.setCbillid(sBillID); // ���������ID
		voSpl.setVbillcode(headerVO.getCt_code());// �����ͬ�ţ�������ʾ��
		voSpl.setCbilltypecode(getBillType());
		voSpl.setCoperatorid(getOperator());
		voSpl.setIoperatetype(new Integer(PrintConst.PAT_OK));// �̶�
		voSpl.setPk_corp(getCorpPrimaryKey());
		voSpl.setTs(headerVO.getTs());// ���������TS

		nc.ui.scm.print.PrintLogClient plc = new nc.ui.scm.print.PrintLogClient();
		plc.setPrintEntry(getPrintEntry());
		// ���õ�����Ϣ
		plc.setPrintInfo(voSpl);
		// ����ts��printcountˢ�¼���.
		plc.addFreshTsListener(new FreshTsListener());
		// ���ô�ӡ����
		getPrintEntry().setPrintListener(plc);

		// ��ӡԤ��
		getDataSource().setBillVO(dmvo);

		getPrintEntry().setDataSource(getDataSource());

		// modified by liuzy 2008-01-11 ѡ���ӡģ��
		if (getPrintEntry().selectTemplate() < 0)
			return;

		getPrintEntry().preview();

		showHintMessage(plc.getPrintResultMsg(true));

	}

	/**
	 * getPrintEntry()�� �������ڣ�(2004-12-09)
	 * 
	 * @author���۱�
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
	 * �õ�һ��PrintDataInterface����ʵ��������card�µĴ�ӡ�� 2004-12-17 �۱�
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
	 * getDataSourceNew()�� �������ڣ�(2004-12-09)
	 * ÿ����һ�ξʹ���һ���µ�PrintDataInterface����ʵ��������list�µĴ�ӡ��
	 * 
	 * @author���۱�
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
	 * �˴����뷽��˵���� ��������:��ȡ�ڵ���� �������: ����ֵ:java.lang.String �쳣����: ����:
	 * �������ڣ�(2002-6-27 20:14:10)
	 * 
	 * @return java.lang.String
	 */
	public String getNodeCode() {
		return m_sNodeCode;
	}

	/**
	 * ���ز���Ա��
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
	 * ��õ������͡� �������ڣ�(2001-11-15 8:52:43)
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getBillType() {
		return m_sBillType;
	}

	/**
	 * �˴����뷽��˵���� ��������:���ݲ��յ�pkֵ���������Ӧ������ �쳣����: ����:
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
	 * �˴����뷽��˵���� ��������: �ӹ����ݣ��õ���Ƭ�б�Ĵ�ӡ���ߴ�ӡԤ�������� �������: ����ֵ: �쳣����: ����:
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

		// ���ú�ͬ״̬Ϊ����ȷ����ʾֵ����������ֵ��
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
		// added by lirr 2008-11-28 ��ӡģ�����þ���
		try {
			nc.vo.ct.pub.GenMethod.setVOScale(new ManageVO[] { dmvo });
		} catch (Exception e) {
			// TODO �Զ����� catch ��
			GenMethod.handleException(this, e.getMessage(), e);
		}

		// return dmvo;
		voRet.setParentVO(dmvo.getParentVO());
		// modified by lirr 2008-11-28 ��ӡģ�����þ���
		/*
		 * ManageItemVO[] itemVOs = (ManageItemVO[]) ((ExtendManageVO)
		 * (m_vBillVO .get(m_iId - 1))).getTableVO("table");
		 */

		// ManageItemVO[] itemVOs = (ManageItemVO[])dmvo.getChildrenVO();
		// modified by lirr 2008-12-30 �����ӡ����
		ManageItemVO[] itemVOs = (ManageItemVO[]) getCtBillCardPanel()
				.getBillModel("table").getBodyValueVOs(
						ManageItemVO.class.getName());

		// ��ӡģ����ʹ��model����mod�������ӡģ��Ĺ�ʽ��������
		// �۱� on April 25, 2005
		for (int i = 0; i < itemVOs.length; i++) {
			itemVOs[i].setAttributeValue("model", itemVOs[i]
					.getAttributeValue("mod"));
		}
		// voRet.setTableVO("table",((ExtendManageVO)(m_vBillVO.get(m_iId
		// -1))).getTableVO("table"));
		voRet.setTableVO("table", itemVOs);
		voRet.setTableVO("term", ((ExtendManageVO) (m_vBillVO.get(m_iId - 1)))
				.getTableVO("term"));

		// added by lirr 2008-08-20 ���Ӵ�ӡ��ͬ���úʹ��¼�
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
	 * �˴����뷽��˵���� �������ڣ�(2001-11-26 20:26:46)
	 */
	protected void initRefSql() {
		// modified by lirr 2008-10-30 �ڲ���ǰ���Ϸǿ��ж�
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
			// ���ݺ�ͬ���ͳ�ʼ����������
			if (m_iCtType == CtType.PUR) {
				depRefmodel.setWherePart(sDepSql
						+ " and (deptattr='2' or deptattr='4') ");
				// deleted by lirr 2009-11-6����10:28:47 ��Ա���ٰ��ղ��Ź���
				/*m_sPerWhereSql += " and (bd_deptdoc.deptattr='2' or bd_deptdoc.deptattr='4')";
				sInitPsn = m_sPerWhereSql
						+ " and bd_psndoc.pk_deptdoc in (select a.pk_deptdoc from bd_deptdoc a where 1=1 and "
						+ sDepSql + " )";*/
			} else if (m_iCtType == CtType.SALE) {
				depRefmodel.setWherePart(sDepSql
						+ " and (deptattr='3' or deptattr='4') ");
				// deleted by lirr 2009-11-6����10:28:47 ��Ա���ٰ��ղ��Ź���
				/*m_sPerWhereSql += " and (bd_deptdoc.deptattr='3' or bd_deptdoc.deptattr='4')";
				sInitPsn = m_sPerWhereSql
						+ " and bd_psndoc.pk_deptdoc in (select a.pk_deptdoc from bd_deptdoc a where 1=1 and "
						+ sDepSql + " ) ";*/
			} else if (m_iCtType == CtType.OTHER) {
			    // deleted by lirr 2009-12-1����11:10:16 л��Ҫ��������ͬ���Ų�����
				/*depRefmodel.setWherePart(sDepSql
						+ " and deptattr not in('2','3','4') ");*/
				// deleted by lirr 2009-11-6����10:28:47 ��Ա���ٰ��ղ��Ź���
				/*m_sPerWhereSql += " and bd_deptdoc.deptattr not in('2','3','4')";
				sInitPsn = m_sPerWhereSql
				
				 * + " and bd_psndoc.pk_deptdoc in (select a,pk_deptdoc from
				 * bd_deptdoc a where 1=1 and " + sDepSql + " ) "
				 ;
				// ������Ա���ա�
				if (null != ((UIRefPane) getCtBillCardPanel().getHeadItem(
						"pername").getComponent()).getRefModel()) {
					nc.ui.bd.ref.AbstractRefModel psnRefModel = ((UIRefPane) getCtBillCardPanel()
							.getHeadItem("pername").getComponent())
							.getRefModel();
					psnRefModel.setWherePart(sInitPsn);
				}*/

			}
		}

		// ���ÿ��̷��ؼ��
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
		// ���ú�ͬ���ò��յĹ�˾����
		// �۱� on May 9, 2005
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
	 * �˴����뷽��˵���� ��������: �������: ����ֵ: �쳣����: ����:
	 */
	protected void setFormularForNat(UFBoolean bOrimode) {
		// ������㹫ʽ
		ArrayList alFormula = null;
		// �������˳����ù�ʽ
		// �۱����ʱ�
		alFormula = new ArrayList();
		if (bFracmode) { // ����Ϊ�˷�ģʽ
			// ���Ҽ�˰�ϼ�=�۱�����*ԭ�Ҽ�˰�ϼ�
			alFormula.add(new String[] { "natitaxsummny",
					"currrate*oritaxsummny" });
			// ���Һ�˰���� = ԭ�Һ�˰����*�۱�����
			alFormula
					.add(new String[] { "natitaxprice", "oritaxprice*currrate" });

		} else { // ����Ϊ����ģʽ
			// ���Ҽ�˰�ϼ�=ԭ�Ҽ�˰�ϼ�/�۱�����
			alFormula.add(new String[] { "natitaxsummny",
					"oritaxsummny/currrate" });
			// ���Һ�˰���� = ԭ�Һ�˰����/�۱�����
			alFormula
					.add(new String[] { "natitaxprice", "oritaxprice/currrate" });
		}
		// ����˰��=˰��*���Ҽ�˰�ϼ�/(1+˰��)
		alFormula.add(new String[] { "natitaxmny",
				"taxration/100*natitaxsummny/(1+taxration/100)" });
		// ���ҽ��=���Ҽ�˰�ϼ�-����˰��
		alFormula.add(new String[] { "natisum", "natitaxsummny-natitaxmny" });
		// ������˰���� = ���Һ�˰����/��1+˰��/100��
		alFormula.add(new String[] { "natiprice",
				"natitaxprice/(1+taxration/100)" });
		// �������=���������*������˰���� inoutsum->inoutnum*natiprice
		alFormula.add(new String[] { "inoutsum", "inoutnum*natiprice" });
		// ����Ʊ���=����Ʊ����*������˰���� invoicesum->natiprice*invoicnum
		alFormula.add(new String[] { "invoicesum", "invoicnum*natiprice" });
		getCtBillCardPanel().setFormula("currrate", alFormula);

		// �帨�ҹ�ʽ
		// getCtBillCardPanel().setFormula("astcurrate", null);
	}

	/**
	 * �˴����뷽��˵���� ��������: �������: ����ֵ: �쳣����: ����: ����5.5����ɾ��������Ϣ ���Դ˷��������ٱ����� lirr
	 * 2008-08-20
	 */
	protected void setFormularForNatAst(UFBoolean bOrimode) {
		// ������㹫ʽ
		ArrayList alFormula = null;
		// �������˳����ù�ʽ
		// �۱����ʱ�
		alFormula = new ArrayList();
		if (bFracmode) { // ����Ϊ�˷�ģʽ
			// ���Ҽ�˰�ϼ�=�۱�����*���Ҽ�˰�ϼ�
			alFormula.add(new String[] { "natitaxsummny",
					"currrate*asttaxsummny" });
			// ���Һ�˰���� = ���Һ�˰����*�۱�����
			alFormula
					.add(new String[] { "natitaxprice", "asttaxprice*currrate" });
		} else { // ����Ϊ����ģʽ
			// ���Ҽ�˰�ϼ�=���Ҽ�˰�ϼ�/�۱�����
			alFormula.add(new String[] { "natitaxsummny",
					"asttaxsummny/currrate" });
			// ���Һ�˰���� = ���Һ�˰����/�۱�����
			alFormula
					.add(new String[] { "natitaxprice", "asttaxprice/currrate" });
		}
		// ����˰��=˰��*���Ҽ�˰�ϼ�/(1+˰��)
		alFormula.add(new String[] { "natitaxmny",
				"taxration/100*natitaxsummny/(1+taxration/100)" });
		// ���ҽ��=���Ҽ�˰�ϼ�-����˰��
		alFormula.add(new String[] { "natisum", "natitaxsummny-natitaxmny" });
		// ������˰���� = ���Һ�˰����/��1+˰��/100��
		alFormula.add(new String[] { "natiprice",
				"natitaxprice/(1+taxration/100)" });
		// �������=���������*������˰���� inoutsum->inoutnum*natiprice
		alFormula.add(new String[] { "inoutsum", "inoutnum*natiprice" });
		// ����Ʊ���=����Ʊ����*������˰���� invoicesum->natiprice*invoicnum
		alFormula.add(new String[] { "invoicesum", "invoicnum*natiprice" });
		getCtBillCardPanel().setFormula("currrate", alFormula);

		// �������˳����ù�ʽ
		// �۸����ʱ�
		alFormula = new ArrayList();
		if (bOrimode == null) { // �����Һ��㷽ʽ
			// �帨�ҹ�ʽ
			getCtBillCardPanel().setFormula("astcurrate", null);
		} else {
			if (bOrimode.booleanValue()) { // ����Ϊ�˷�ģʽ
				// ���Ҽ�˰�ϼ�=�۸�����*ԭ�Ҽ�˰�ϼ�
				alFormula.add(new String[] { "asttaxsummny",
						"astcurrate*oritaxsummny" });
				// ���ҵĺ�˰����=�۸�����*ԭ�Һ�˰����
				alFormula.add(new String[] { "asttaxprice",
						"oritaxprice*astcurrate" });
			} else { // ����Ϊ����ģʽ
				// ���Ҽ�˰�ϼ�=ԭ�Ҽ�˰�ϼ�/�۸�����
				alFormula.add(new String[] { "asttaxsummny",
						"oritaxsummny/astcurrate" });
				// ���ҵĺ�˰����=ԭ�Һ�˰����/�۸�����
				alFormula.add(new String[] { "asttaxprice",
						"oritaxprice/astcurrate" });
			}
			// ����˰��=˰��*���Ҽ�˰�ϼ�/(1+˰��)
			alFormula.add(new String[] { "asttaxmny",
					"taxration/100*asttaxsummny/(1+taxration/100)" });
			// ���ҽ��=���Ҽ�˰�ϼ�-����˰��
			alFormula.add(new String[] { "astsum", "asttaxsummny-asttaxmny" });
			// ������˰����=���Һ�˰����/��1+˰�ʣ�
			alFormula.add(new String[] { "astprice",
					"asttaxprice/(1+taxration/100)" });
			getCtBillCardPanel().setFormula("astcurrate", alFormula);
		}
	}

	/**
	 * �˴����뷽��˵���� ��������: �������: ����ֵ: �쳣����: ����:
	 * 
	 * @return java.util.ArrayList
	 * @param alvos2
	 *            java.util.ArrayList int[] arySelListHeadRows �б���ѡ��ĵ��ݵ�����
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
			// ��ӡģ����ʹ��model����mod�������ӡģ��Ĺ�ʽ��������
			// �۱� on April 25, 2005
			// modified by lirr 2008-07-18 �޸�ԭ�򣺲�ѯ��ֱ��ѡ����Ԥ��ʱ����
			// ԭ���ǳ���һ�м�¼��������¼�ı�������û�ж�ȡ,�����˱���ķǿ��ж�
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

			// ���ú�ͬ״̬Ϊ����ȷ����ʾֵ����������ֵ��
			mhvo.setAttributeValue("ctflag", (String) getCtBillListPanel()
					.getHeadBillModel().getValueAt(arySelListHeadRows[i],
							"ctflag"));
		}

		return alvos2;
	}

	/**
	 * �˴����뷽��˵���� ��������:���ݲ��յ�pkֵ���������Ӧ������ �������: ����ֵ: �쳣����: ����:
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
		// added by lirr 2008-07-10 ��ͬ����ҳǩ ���塰����ť

		if (e.getSource() == miBoCardEdit) {// "��Ƭ�༭"
			onButtonClicked(getButtonTree().getButton(
					CTButtonConst.BTN_CARD_EDIT));
		}
		if (e.getSource() == miaddNewRowNo) {// "��Ƭ�༭"
			onButtonClicked(getButtonTree().getButton("�����к�"));
		}
	}

	/**
	 * �������ı��¼�����
	 * 
	 * @author shaobing
	 * @param e
	 *            nc.ui.pub.bill.BillEditEvent
	 */
	protected void afterAstunitEdit(nc.ui.pub.bill.BillEditEvent e) {
		int iRow = e.getRow();
		// ����λ����
		Object oAstUnitName = e.getValue();
		// ȡ����λ
		BillItem bt = getCtBillCardPanel().getBodyItem("astmeasname");
		UIRefPane refAstUnit = (UIRefPane) bt.getComponent();

		/*
		 * // ����λpk Object oAstUnitPk = refAstUnit.getRefPK();
		 */
		// modified by lirr 2008-12-25
		Object oAstUnitPk = null;
		if (!m_bAddFromBillFlag) {
			oAstUnitPk = refAstUnit.getRefPK();
		} else {
			oAstUnitPk = getCtBillCardPanel().getBodyValueAt(iRow, "astmeasid");
		}

		if (isNull(oAstUnitName) || isNull(oAstUnitPk)) { // ��������λΪ��
			// ���������������
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
			// �������������ʡ�
			String sInvBasID = getCtBillCardPanel().getBodyValueAt(iRow,
					"invbasid").toString();
			// modified by lirr 2009-9-3����04:53:40 ����������
			/*
			 * m_voInvMeas.filterMeas(getCorpPrimaryKey(), sInvBasID,
			 * refAstUnit); MeasureRateVO voMeas =
			 * m_voInvMeas.getMeasureRate(sInvBasID, oAstUnitPk.toString()); //
			 * ȡ������ UFDouble ufdRate = voMeas.getMainmeasrate();
			 */
			UFDouble ufdRate = InvTool.getInvConvRateValue(sInvBasID,
					oAstUnitPk.toString());
			getCtBillCardPanel().setBodyValueAt(oAstUnitPk.toString(), iRow,
					"astmeasid");
			getCtBillCardPanel().setBodyValueAt(ufdRate, iRow, "transrate");
			/*
			 * // ADDED by lirr 2009-1-5 �ж��Ƿ�̶������� �̶�������ʱ���ɱ༭ ����ɱ༭ boolean
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
	 * �ж�Object�Ƿ�Ϊ�ա� 2005-04-09
	 * 
	 * @author: �۱�
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
	 * �˴����뷽��˵���� ��ҵ��Ա�Ķ�������Ӧ�Ĳ��š� ���ߣ������� �������ڣ�(2003-9-23 13:36:07) *
	 * �޸����ڣ�2008-10-23 �޸�ԭ�򣺲������ɺ�ͬ �������¼�ʱmanPkΪ�� ��ȽȽ
	 * 
	 * @param e
	 *            nc.ui.pub.bill.BillEditEvent
	 * @throws BusinessException
	 */
	protected void afterPersonEdit(BillEditEvent e, Boolean isFromBill)
			throws BusinessException {
		// modified by lirr 2008-12-08 ���������ĵ��� ����в�����Ϣ����� û��ʱ������Ա���
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
			} else {// modified by lirr 2008-12-08 ���������ĵ��� ����в�����Ϣ����� û��ʱ������Ա���
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
			// modified by lirr 2009-11-10����03:22:57 ����Ϊ��ʱ��¼��ҵ��Ա�������ŵ�ֵ�����Ų�Ϊ��ʱ��¼��ҵ��Ա������ҵ��Ա�������ŵ�ֵ
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
	 * �˴����뷽��˵���� ���ߣ�cqw �������ڣ�(2003-10-18 15:21:39)
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
	 * �˴����뷽��˵���� ��������:ʵ�����б��µ�����ͷ���� �������: ����ֵ: �쳣����: ����:������ ����:
	 * 
	 * @param key
	 *            java.lang.String
	 */
	public void afterSort(String key) {

		// added by lirr 2009-04-03 �����ϲ��������ݴ������� ����󻺴�δ����
		/*
		 * ��Ҫ�޸�saveManage���� ����remove֮�� ���һ��itemvo ��Ȼ��������ҳ�棨����˳��ı䣩
		 * getCurVO()��onsave֮ǰ�����ȴӻ�����remove��һ��itemVO�����Դ�ʱ���û�����Խ�� //qinchao Խ��
		 * 
		 * if(m_bIsCard) { if(getCurVO()!=null){ ManageItemVO[] itemvos
		 * =(ManageItemVO[])getCtBillCardPanel().getBillModel("table").getBodyValueVOs(ManageItemVO.class.getName());
		 * m_vBillVO.get(m_iId - 1).setTableVO("table", itemvos); } } else {
		 * if(getCurVO()!=null){ ManageItemVO[] itemvos
		 * =(ManageItemVO[])getCtBillListPanel().getBodyBillModel("table").getBodyValueVOs(ManageItemVO.class.getName());
		 * m_vBillVO.get(m_iId - 1).setTableVO("table", itemvos); } }
		 */
		// ���ӱ����Ĭ�Ͻ������ĵ��ݷŵ�vo�б�����
		// �����ǡ���б��±�ͷ�����������������⡣
		// By Shaw Sep 26, 2006
		if (!m_bIsCard) {

			return;

		}

		// ֻ��״̬Ϊ0(free)��ʱ��������򻺴�
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
		 * // ���ӱ����Ĭ�Ͻ������ĵ��ݷŵ�vo�б����� // �����ǡ���б��±�ͷ�����������������⡣ // By Shaw Sep
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
	 * �˴����뷽��˵���� ������ݳ��ȵĺϷ���[��˳��õ������ݳ���] �������ڣ�(2002-6-27 16:16:11)
	 */
	protected boolean checkOther(AggregatedValueObject vo, ScmBillCardPanel bcp)
			throws Exception {
		boolean bret = true;
		// modified by lirr 2008-8-20 ����5.5����ɾ��������Ϣ
		/*
		 * String[] saBodyField = new String[] { "amount", "astnum", "orisum",
		 * "oritaxmny", "oritaxsummny", "natisum", "natitaxmny",
		 * "natitaxsummny", "astsum", "asttaxmny", "asttaxsummny" }; String[]
		 * saBodyFieldName = new String[] {
		 * nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
		 * "UC000-0002282") @res "����" ,
		 * nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
		 * "UC000-0003971") @res "������" ,
		 * nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
		 * "UC000-0000919") @res "ԭ����˰���" ,
		 * nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
		 * "UC000-0000922") @res "ԭ��˰��" ,
		 * nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
		 * "UC000-0000902") @res "ԭ�Ҽ�˰�ϼ�" ,
		 * nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
		 * "UC000-0002607") @res "������˰���" ,
		 * nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
		 * "UC000-0002613") @res "����˰��" ,
		 * nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
		 * "UC000-0002594") @res "���Ҽ�˰�ϼ�" ,
		 * nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
		 * "UC000-0003961") @res "������˰���" ,
		 * nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
		 * "UC000-0003967") @res "����˰��" ,
		 * nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
		 * "UC000-0003948") @res "���Ҽ�˰�ϼ�" };
		 */
		String[] saBodyField = new String[] { "amount", "astnum", "orisum",
				"oritaxmny", "oritaxsummny", "natisum", "natitaxmny",
				"natitaxsummny" };
		String[] saBodyFieldName = new String[] {
				nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
						"UC000-0002282")/*
										 * @res "����"
										 */,
				nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
						"UC000-0003971")/*
										 * @res "������"
										 */,
				nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
						"UC000-0000919")/*
										 * @res "ԭ����˰���"
										 */,
				nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
						"UC000-0000922")/*
										 * @res "ԭ��˰��"
										 */,
				nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
						"UC000-0000902")/*
										 * @res "ԭ�Ҽ�˰�ϼ�"
										 */,
				nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
						"UC000-0002607")/*
										 * @res "������˰���"
										 */,
				nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
						"UC000-0002613")/*
										 * @res "����˰��"
										 */,
				nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
						"UC000-0002594") /*
											 * @res "���Ҽ�˰�ϼ�"
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
																	 * "�к�:"
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
																				 * "�������������쳣!"
																				 */
					+ "\n");
			for (int i = 0; i < ilen; i++) {
				sbAllErr.append((StringBuffer) alError.get(i));
				sbAllErr.append("\n");
			}
			MessageDialog.showErrorDlg(this, null, sbAllErr.toString());
			return false;
		}

		// ����ͬԤ�����Ƿ���ں�ͬ�ļ�˰�ϼ�.
		if (bcp.getHeadItem("nprepaylimitmny") != null) {
			Object olimit = bcp.getHeadItem("nprepaylimitmny").getValue();
			if (olimit != null && olimit.toString().length() > 0) {
				UFDouble ufdtaxmny = null;
				UFDouble ufdsum = new UFDouble(0);
				for (int i = 0; i < voLen; i++) {
					// modified by lirr 2009-7-30 ����03:15:53 oritaxsummny ��Ϊԭ�ҿ���
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
														 * "��ͬԤ����ܴ��ں�ͬ���Ҽ�˰�ϼ��ܺ�!"
														 */);
					return false;
				}

				// modified by liuzy 2007-12-18 Ԥ�����޶��Ϊ��

				if (ufdLimit.compareTo(new UFDouble("0.0")) < 0) {
					MessageDialog.showErrorDlg(this, null, nc.ui.ml.NCLangRes
							.getInstance().getStrByID("4020pub",
									"UPT4020pub-000251")/*
														 * @res "Ԥ�����޶��Ϊ��"
														 */);
					return false;
				}
			}
		}
		return bret;

	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2004-4-3 10:49:51)
	 * 
	 * @author��������
	 * @return nc.vo.ct.pub.ManageVO
	 */
	protected ManageVO getCurVO() {// modified by lirr �����Ƿ��ǲ����������ݵ��ж�
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
		// �����Ƿ��ǲ����������ݵ��ж�

		ManageHeaderVO voHead = (ManageHeaderVO) voExtendCur.getParentVO();
		voCur.setParentVO(voHead);

		voCur.setChildrenVO(voExtendCur.getTableVO("table"));
		// added by lirr 2009-9-16����09:20:28 ����ʱ�������û��������Ҫ���¼���
		if (null == voCur.getChildrenVO() || voCur.getChildrenVO().length < 1) {
			try {
				voCur = ContractQueryHelper.queryAllBodyData(voCur);
				execFormularAfterQueryWithoutCache(new ManageVO[] { voCur });
				if (null == voCur) {
					MessageDialog.showErrorDlg(this, null, NCLangRes
							.getInstance().getStrByID("4020pub",
									"UPP4020pub-000269")/* ��ѯ��������ʱ���� */);
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
	 * �˴����뷽��˵���� �������ڣ�(2008-11-5 10:49:51) �������ֱ�����󣬲����Ƿ��ǲ����������ݵĺ�ͬ
	 * ��ǰvo��Ҫ��m_vBillVO��ȡ
	 * 
	 * @author��lirr
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
	 * ������������������ѡ�е��кŻ��VO
	 * <p>
	 * <b>examples:</b>
	 * <p>
	 * ʹ��ʾ��
	 * <p>
	 * <b>����˵��</b>
	 * 
	 * @param index
	 * @return
	 *            <p>
	 * @author liuzy
	 * @time 2007-4-12 ����08:49:32
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
				// MODIFIED by lirr 2008-8-11 �����ж�
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
	 * �˴����뷽��˵���� ���ߣ� <author>�������ڣ�(2003-9-27 20:45:21)
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
			 * �޸���: wuweiping �޸�ʱ��: 22/09/2009 13:00:00 �޸�ԭ��: ������ͬ������ȡ��ʱ���տ�
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
				// ������˳��
				if (!nc.ui.pub.pf.PfUtilClient.isSuccess()) {
					showHintMessage(nc.ui.ml.NCLangRes.getInstance()
							.getStrByID("SCMCOMMON", "UPPSCMCommon-000235")/*
																			 * @res
																			 * "���ʧ��"
																			 */);
					return null;
				}
			}
			// ������˳��
			// ��ͬPK[ˢ�º�ͬ״̬]
			String sPK = (String) mVO.getParentVO().getAttributeValue(
					"pk_ct_manage");
			int ctStateNew = qryCtStatus(sPK).intValue();// ���������ͬ״̬
			// ����ͬ״̬
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

				// modified by lirr 2009-8-24����01:39:45
				// �Ӷ�������ts������������
				if (alRet != null && alRet.size() > 0)
					freshStatusTs(mVO, (String) alRet.get(1));
				setAuditTime(mVO, null);
			} /*
				 * else {//modified by lirr 2008-11-27 ����󻹿����ǡ��������״̬�� }
				 */
			else if (BillState.CHECKGOING == ctStateNew) {
				mVO = loadHeadData(m_sPkCorp, " ct.pk_ct_manage = '" + sPK
						+ "' ")[0];
				execFormularAfterQueryWithoutCache(new ManageVO[] { mVO });
				m_vBillVO.get(m_iId - 1).setParentVO(mVO.getParentVO());
				// m_vBillVO.remove(m_iId - 1);
				// mVO = loadHeadData(m_sPkCorp,
				// " ct.pk_ct_manage = '" + sPK + "' ")[0];
				// // ���ع�ʽ
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
				// modified by lirr 2009-8-24����01:39:45
				// �Ӷ�������ts������������
				if (alRet != null && alRet.size() > 0)
					freshStatusTs(mVO, (String) alRet.get(1));
			}
			// added by lirr 2008-11-27 ����״̬ */);
			setButtonState();
			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"SCMCOMMON", "UPPSCMCommon-000184")/*
														 * @res "����ɹ�"
														 */);

		} catch (java.sql.SQLException e) {
			MessageDialog.showErrorDlg(this,
					nc.ui.ml.NCLangRes.getInstance().getStrByID("4020pub",
							"UPP4020pub-000066")/* @res "���ݿ��������" */, e
							.getMessage());
			strRtn = e.getMessage();

		} catch (java.rmi.RemoteException e) {
			MessageDialog.showErrorDlg(this,
					nc.ui.ml.NCLangRes.getInstance().getStrByID("4020pub",
							"UPP4020pub-000067")/* @res "Զ�̵���ʧ��" */, e
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
	 * �˴����뷽��˵���� ��������:��ִͬ�����ҳǩ �������: ����ֵ: �쳣����: ����:������ ����:
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
	 * �˴����뷽��˵���� ��������:��ʾ��ͬ����ҳǩ�� �������: ����ֵ: �쳣����: ����:������ ����:
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
	 * �����������������ݴ���Ĳ����ж��Ƿ�ˢ�� ����Ϊpublic��Ϊ����LongTimeTask.procclongTime�����ܹ����õ�
	 * <p>
	 * <b>examples:</b>
	 * <p>
	 * ʹ��ʾ��
	 * <p>
	 * <b>����˵��</b>
	 * 
	 * @param isFresh
	 *            <p>
	 * @author liuzy
	 * @time 2007-4-16 ����08:27:14
	 */
	public void onFresh(boolean isFresh) {
		if (isFresh)
			onFresh();
	}

	/**
	 * �˴����뷽��˵���� ��������:���ݲ�ѯ����ˢ�½��档[��ť״̬�ڲ�ѯ֮�����] �������: ����ֵ: �쳣����: ����:������ ����:
	 */
	protected void onFresh() {
		if (m_sQryCondition == null || m_sQryCondition.length() <= 0)
			return;
		if (haveQuestion()) {
			// ��ҳǩת�Ƶ�"��ͬ����"��.
			getCtBillCardPanel().getBodyTabbedPane().setSelectedIndex(
					TabState.BILL);
			getCtBillListPanel().getBodyTabbedPane().setSelectedIndex(
					TabState.BILL);
			m_iTabbedFlag = TabState.BILL;
			getCtBillCardPanel().setEnabled(false); // ���ݿ�Ƭ���ɱ༭
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
																		 * "û���ҵ���������������"
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
				// ���ع�ʽ
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
					// ��Ƭ����ǰ������ԭ�Һͻ��ʾ���
					setCardOriAndCurrDigit(((ManageHeaderVO) arrMangevos[k]
							.getParentVO()).getCurrid());
					// ��Ƭ��������VO
					getCtBillCardPanel().setBillValueVO(arrExtendVO[k]);
					// �ӿ�Ƭ����ȡ��VO
					getCtBillCardPanel().getBillModel().execLoadFormula();
					getCtBillCardPanel().getBillValueVOExtended(arrExtendVO[k]);
					listVO.add(arrExtendVO[k]);
				}
				if (listVO != null && listVO.size() > 0) {
					m_vBillVO.clear();
					for (int i = 0; i < listVO.size(); i++) {

						m_vBillVO.add(listVO.get(i));
					}
					// �ж����ڿ�Ƭģʽ�£��������б�ģʽ��
					m_bIsFirstClick = true; // ��־�ǵ�һ�ε��
					if (m_bIsCard) {
						// �л����б�ģʽ��
						onList();
					} else {
						// ���л���ǰ���б�ģʽ����ʾ��ѯ���
						// getTabbedPane().setSelectedIndex(0); //��ʾ�����б�
						getCtBillListPanel().getBodyTabbedPane()
								.setSelectedIndex(0);
						setListRateDigit();
						setHeaderListData();
						getCtBillListPanel().getHeadTable()
								.setRowSelectionInterval(0, 0);
						// isNeedReInit =true; //����Table����Ҫ������������
					}
					// m_iId = 1;
					m_bChangeFlag = false; // ��־��ͬ���ɱ��
					// ����Table����Ҫ������������
					for (int i = 1; i < 6; i++)
						m_bIsNeedReInit[i] = true;

					setCardVOData(); // ���³�ʼ��Ƭ����

					showHintMessage(nc.ui.ml.NCLangRes.getInstance()
							.getStrByID("common", "UCH007")/* @res "ˢ�³ɹ�" */);
				} else {
					String sMessage = nc.ui.ml.NCLangRes.getInstance()
							.getStrByID("4020pub", "UPP4020pub-000072")/*
																		 * @res
																		 * "δ�鵽���������ĺ�ͬ��"
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
	 * �˴����뷽��˵���� ��������:��ʾ��ͬ�����ʷҳǩ�������б����ʽ��ʾ��ͬ��¼�� �������: ����ֵ: �쳣����: ����:������ ����:
	 */
	protected void onHisitory() {

		ManageVO[] voaHistory = null; // ��ʷ��¼
		int currDigit = 2; //��ǰ��ͬvoԭ�ҽ���
		String currid = null; //ԭ�ұ���id
		// ���²�����ʾ��ǰ��ͬ�ļ�¼��
		if (m_vBillVO != null) {
			// �õ���ǰ��ͬ
			ManageVO voCur = getCurVO();
			if (voCur != null) {
				//�õ���ǰvo��ԭ�ұ��־���
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
					 * m_htChanged��������sOrignPK��ǰ��ͬ��PK
					 * ֵ����ѯ��õ���ManageVO[],����String
					 * �����ϣ���д��ڵ�ǰ��ͬ��������˵���Ըú�ͬ�Ѿ����������ʷ��
					 * ��������ļ�ֵ������String˵����ǰ��ͬû����ʷ��¼
					 * �����ϣ���в����ڵ�ǰ��ͬ������˵���Ըú�ͬ��û�в�ѯ�������ʷ��
					 * ��֮�������ѯû�н��������ͬ�����������ϣ����ֵΪString
					 * ��������н��������ͬ�����������ϣ����ֵΪManageVO[]
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
																				 * "��ѯ��ͬ�����ʷ����"
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
		// �ж�voaHistory�Ƿ���ڱ����ʷ
		// ���Ϊ�ջ���û�б����ʷ��vo�ĳ���<1��,�������ʾ��
		// ����,�Ի�����ʾ
		if (voaHistory != null && voaHistory.length > 1) {
			m_dlgHistory = new CtHistoryDlg(this, m_sBillType, m_sPkUser,
					m_sPkCorp,currDigit,getCurrateDigit(currid));
			// }
			ArrayList alvo = new ArrayList();
			int iLen = voaHistory.length;
			for (int i = 0; i < iLen; i++){
			    // added by lirr 2009-11-23����09:47:41 �����ʷ��ѯ������ ������vfree0����ʾ������
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
					"4020pub", "UPP4020pub-000113")/* @res "����ͬΪԭʼ��ͬ��û�б����¼��" */);
			return;
		}
		// ��ʾ�����ʷ��ҳǩ
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
	 * �˴����뷽��˵���� ��������:���ݺ�ͬID�ͺ�ͬ�������������ε��ݡ�v2.3 �������: ����ֵ: �쳣����: ����:������ ����:
	 */
	protected void onJoin() {
		if (m_iId >= 1 && m_vBillVO != null && m_vBillVO.size() > 0
				&& m_vBillVO.get(m_iId - 1) != null) {

			ManageVO voTemp = getCurVO();
			ManageHeaderVO voHead = (ManageHeaderVO) voTemp.getParentVO();
			if (voHead == null) {
				showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"4020pub", "UPP4020pub-000114")/* @res "û������ĺ�ͬ��" */);
				return;
			}
			String sBillPK = voHead.getPk_ct_manage();
			String sBillTypeCode = getBillType();
			String sVBillCode = voHead.getCt_code();
			// ���sBillPK��sBillTypeCodeΪ�գ�����û�����塣
			if (sBillPK == null || sBillTypeCode == null) {
				showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"4020pub", "UPP4020pub-000115")/* @res "����û�ж�Ӧ���ݣ�" */);
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
					"SCMCOMMON", "UPPSCMCommon-000154")/* @res "û������ĵ��ݣ�" */);
		}
		// setButtonState();
		if (!m_bAddFromBillFlag) {
			setButtonState();
		} else {
			setButtonStateForCof();
		}
	}

	/**
	 * �Ҽ��˵� �˴����뷽��˵���� �������ڣ�(2001-3-27 11:09:34)
	 * 
	 * @param e
	 *            java.awt.event.ActionEvent
	 */
	public void onMenuItemClick(java.awt.event.ActionEvent e) {
		UIMenuItem item = (UIMenuItem) e.getSource();
		if (item == getCtBillCardPanel().getCopyLineMenuItem()) {
			getCtBillCardPanel().copyLine();

			// modify by qinchao 20090224 ����������Ҽ��˵������С�ճ������Ч����
			if (!m_bAddFromBillFlag) {
				setButtonState();
			} else {
				setButtonStateForCof();
			}
		} else if (item == getCtBillCardPanel().getPasteLineMenuItem()) {
			// modify by qinchao 20090224 ����������Ҽ��˵������С�ճ������Ч����
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

			// add by qinchao 20090224 ���ܣ���ճ�����е�"�ۼƸ�����"��Ϊ0
			int row = getCtBillCardPanel().getBodyPanel().getTable()
					.getSelectedRow();
			int rowlength = getCtBillCardPanel().getBillModel()
					.getPasteLineNumer();
			clearExecInfo(row,rowlength);
			updateUI();
			// end ���ܣ���ճ�����е�"�ۼƸ�����"��Ϊ0

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

			// add by qinchao 20090224 ���ܣ���ճ�����е�"�ۼƸ�����"��Ϊ0
			int row = getCtBillCardPanel().getBodyPanel().getTable()
					.getRowCount();
			int rowlength = getCtBillCardPanel().getBillModel()
					.getPasteLineNumer();
			clearExecInfo(row,rowlength);
			updateUI();
			// end ���ܣ���ճ�����е�"�ۼƸ�����"��Ϊ0
		}
	}

	/**
	 * �˴����뷽��˵���� ��������:��ʾ��ͬ���¼�ҳǩ �������: ����ֵ: �쳣����: ����:������ ����:
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
	 * �˴����뷽��˵���� �ֹ�����������. �������ڣ�(2004-4-9 14:11:32)
	 * * �޸����� 2009-11-10����01:57:07 �޸��ˣ�lirr �޸�ԭ��ע�ͱ�־��
	 *@param showError �Ƿ��׳��쳣 ���漴����ʱ�����쳣
	 * @author��������
	 * @return void
	 * @param void
	 */
	
	protected void onSavetoAudit(boolean showError) {

		// ���� �� �޸Ĳ���ʱ���������������
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
			// modified by lirr 2008-11-05 �����Ƿ��ǲ����������ݵĺ�ͬ ��ǰvo��Ҫ��m_vBillVO��ȡ
			ManageVO voCur = getCurBillVO();
			ManageVO tempVO = getSendAuditVO(true, voCur);
			// added by lirr 2009-10-17����02:22:05
			if (tempVO == null) {
				return;
			}
			// ����������
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
				// ˢ��ts
				freshStatusTs(voCur, (String) voCur.getParentVO()
						.getAttributeValue("ts"));
				// ���û�m_vbillVO
				((ExtendManageVO) m_vBillVO.get(m_iId - 1)).setParentVO(voCur
						.getParentVO());

				ExtendManageVO newExtendManageVO = (ExtendManageVO) m_vBillVO
						.get(m_iId - 1);
				getCtBillCardPanel().setBillValueVO(newExtendManageVO);
				setIdtoName();
				// ִ�й�ʽ
				getCtBillCardPanel().getBillModel().execLoadFormula();
				setHeaderListData();
				getCtBillCardPanel().superupdateValue(); // ����ģ��״̬
				getCtBillCardPanel().setEnabled(false);
				m_bErrFlag = false;
				// added by lirr 2009-01-13
				m_iBillState = OperationState.FREE;

			}
			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"SCMCOMMON", "UPPSCMCommon-000269")/*
														 * @res "�������"
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
	 * �˴����뷽��˵���� ��������:ֱ�Ӵ�ӡ��ͬҳǩ�� �������: ����ֵ: �쳣����: ����:������ ����:
	 */
	protected void onTabPrint() {
		BillModel model = null;
		// ��ͷ����
		String title = null;
		switch (m_iTabbedFlag) {
		case TabState.TERM: // ����ҳǩ
			title = nc.ui.ml.NCLangRes.getInstance().getStrByID("40202001",
					"UPT40202001-000008")/* @res "��ͬ����" */;
			model = getCtBillCardPanel().getBillModel("term");
			break;
		case TabState.EXP: // ����ҳǩ
			title = nc.ui.ml.NCLangRes.getInstance().getStrByID("40202001",
					"UPT40202001-000009")/* @res "��ͬ����" */;
			model = getCtBillCardPanel().getBillModel("cost");
			break;
		case TabState.NOTE: // ���¼�ҳǩ
			title = nc.ui.ml.NCLangRes.getInstance().getStrByID("40202001",
					"UPT40202001-000010")/* @res "��ͬ���¼�" */;
			model = getCtBillCardPanel().getBillModel("note");
			break;
		case TabState.CHANGE: // ���ҳǩ
			title = nc.ui.ml.NCLangRes.getInstance().getStrByID("40202001",
					"UPT40202001-000030")/* @res "��ͬ���" */;
			model = getCtBillCardPanel().getBillModel("history");
			break;
		case TabState.EXEC: // ��ִͬ�����
			title = nc.ui.ml.NCLangRes.getInstance().getStrByID("4020pub",
					"UPP4020pub-000172")/* @res "��ִͬ�й���" */;
			model = getCtBillCardPanel().getBillModel("exec");
			break;
		default:
			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"SCMCOMMON", "UPPSCMCommon-000173")/* @res "û��ҳǩ" */);
			return;
		}

		int rowCount = model.getRowCount();
		int colCount = model.getColumnCount();
		if (rowCount <= 0) {
			showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"SCMCOMMON", "UPPSCMCommon-000166")/* @res "û��������Ҫ��ӡ" */);
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
		// ��ͷ����
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

		// �������ݣ�ȥ�������У�
		Object[][] data = new Object[rowCount][colCount];
		for (int i = 0; i < rowCount; i++) {

			for (int j = 0; j < colCount; j++)
				data[i][j] = model.getValueAt(i, (String) vColKey.get(j));

		}
		// ׼����ӡ
		nc.ui.pub.print.PrintDirectEntry print = new nc.ui.pub.print.PrintDirectEntry();
		print.setColNames(colname);
		print.setData(data);
		print.setTitle(title);
		print.setAlignFlag(alignflag);
		// �������
		print.setColWidth(colwidth);
		print.preview(); // ��ӡԤ��
		// setButtonState();
		if (!m_bAddFromBillFlag) {
			setButtonState();
		} else {
			setButtonStateForCof();
		}
	}

	/**
	 * �˴����뷽��˵���� ��������:��ʾ��ͬ����ҳǩ �������: ����ֵ: �쳣����: ����:������ ����:
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
	 * �˴����뷽��˵���� ��ѯ��ͬ״̬ ���ߣ������� �������ڣ�(2003-9-27 16:59:49)
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
	 * �˴����뷽��˵���� ��������:��������ĺ�ͬ�� �������: ����ֵ: �쳣����: ����:������ ����:
	 */
	protected boolean saveChangedCT() {
		try {
			m_timer.start("��ͬ������濪ʼ��"); /*-=notranslate=-*/
			setNameToID();
			// ���˿���
			getCtBillCardPanel().stopEditing();
			// ��ý���VO
			// �õ�����VOO
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
			// ���ԭ����VO
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

			// �����ɾ���ı�����,����뵽NewManageVO��
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

			// �������ĺϷ���
			newVO.validate();
			// ������û��ͨ���򷵻�
			if (checkSave(newVO) == false)
				return false;
			// ��ͷ�� ���ۼ�ԭ����/�����ܶ�
			UFDouble ufnarapamount = getAraptotalgpamount(newVO);
			UFDouble ufntotal = new UFDouble(0);
			UFDouble ufshntotal = new UFDouble(0);
			/*
			 * if (null !=
			 * newVO.getParentVO().getAttributeValue("ntotalgpamount")) {
			 * ufntotal = (UFDouble) ((ManageHeaderVO) newVO.getParentVO())
			 * .getAttributeValue("ntotalgpamount"); }
			 */
			// modified by lirr 2009-7-17 ����02:09:35 �������ʸ�Ϊԭ�ҿ���
			if (null != newVO.getParentVO().getAttributeValue(
					"noritotalgpamount")) {
				ufntotal = (UFDouble) ((ManageHeaderVO) newVO.getParentVO())
						.getAttributeValue("noritotalgpamount");
			}
			 // added by lirr 2009-11-21����01:32:46�ۼ�ԭ��Ӧ�ո����ܶ�
			if (null != newVO.getParentVO().getAttributeValue(
      "norigpshamount")) {
        ufshntotal = (UFDouble) ((ManageHeaderVO) newVO.getParentVO())
        .getAttributeValue("norigpshamount");
  }
			if (ufnarapamount.compareTo(ufntotal) < 0) {

				String sMessage = nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"4020pub", "UPT4020pub-000260")
				/*
				 * @res ��ͬ�ġ�ԭ�Ҽ�˰�ϼơ�֮�Ͳ���С�ڡ��ۼ�ԭ����/�����ܶ��"
				 */;

				MessageDialog.showWarningDlg(this, null, sMessage);
				return false;
			}
			if (ufnarapamount.compareTo(ufshntotal) < 0) {

	        String sMessage = nc.ui.ml.NCLangRes.getInstance().getStrByID(
	            "4020pub", "UPT4020pub-000277")
	        /*
	         * @res ��ͬ�ġ�ԭ�Ҽ�˰�ϼơ�֮�Ͳ���С�ڡ��ۼ�ԭ��Ӧ��/�����ܶ��"
	         */;

	        MessageDialog.showWarningDlg(this, null, sMessage);
	        return false;
	      }
			String currid = newVO.getParentVO().getCurrid();
			newVO.getParentVO().setAttributeValue(
					"naraptotalgpamount",
					new UFDouble(ufnarapamount.toDouble(),
							((Integer) m_hCurrDigitcw.get(currid)).intValue()));
			// ����ͷVO����pk_corp��ifearly��ֵ

			// �޸�ʱ�䣺2009/10/09 �޸���:wuweiping �޸�ԭ��: ��ͬ�������У��޸ĺ�ͬ��PK_corp������
			if (m_iBillState == OperationState.ADD) {
				((ManageHeaderVO) newVO.getParentVO()).setPk_corp(m_sPkCorp);
			}
			// ((ManageHeaderVO) newVO.getParentVO()).setPk_corp(m_sPkCorp);
			((ManageHeaderVO) newVO.getParentVO()).setIfearly(m_UFbIfEarly);
			// �ж��Ƿ�������Ƶ��� added by lirr 2009-02-10
			if (m_isSaveInitOper.booleanValue() == false)
				((ManageHeaderVO) newVO.getParentVO()).setOperid(m_sPkUser);

			((ManageHeaderVO) newVO.getParentVO()).setVusername(m_sUserName);
			newVO.getParentVO().setAttributeValue("coperatoridnow", m_sPkUser);

			newVO.getParentVO().setAttributeValue("clastoperatorid", m_sPkUser);
			newVO.getParentVO().setAttributeValue("vlastoperatorname",
					m_sPkUser);

			// ������VO����pk_corp
			ManageItemVO[] bodyVO = (ManageItemVO[]) newVO.getChildrenVO();
			for (int i = 0; i < newVO.getChildrenVO().length; i++) {
				bodyVO[i].setPk_corp(m_sPkCorp);
				// ������������У���ô�ͱ����pk_ct_manage��ֵ
				if (bodyVO[i].getStatus() == 2)
					bodyVO[i].setPk_ct_manage(newVO.getParentVO()
							.getPrimaryKey().toString());
			}
			// ����VO״̬
			newVO.setIsChange(new UFBoolean(true));

			newVO.setChangeBb1s(oldVO.getChangeBb1s());
			newVO.setExpBb3s(oldVO.getExpBb3s());
			newVO.setManageExecs(oldVO.getManageExecs());
			newVO.setMemoraBb2s(oldVO.getMemoraBb2s());
			newVO.setTermBb4s(oldVO.getTermBb4s());

			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"4020pub", "UPP4020pub-000116")/* @res "��ʼ��������ĺ�ͬ����" */);

			newVO.setIsSaveCheck(true);
			/*
			 * UFDouble ufnarapamount = new UFDouble(0); if (null !=
			 * newVO.getChildrenVO() && newVO.getChildrenVO().length > 0) { for
			 * (int i = 0; i < newVO.getChildrenVO().length; i++) { if (null !=
			 * newVO.getChildrenVO()[i] .getAttributeValue("natitaxsummny"))
			 * ufnarapamount = ufnarapamount.add((UFDouble) newVO
			 * .getChildrenVO()[i] .getAttributeValue("natitaxsummny")); } } //
			 * ��ͷ�� �񱾱��ۼ���/�����ܶ�
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
																			 * "����ʧ�ܣ�"
																			 */);
						return false;
					}
				}
			}

			newVO.setIsSaveCheck(true);
			// newVO = (ManageVO) nc.ui.pub.pf.PfUtilClient.processAction(
			// "MODIFY", m_sBillType, getClientEnvironment().getDate()
			// .toString(), newVO, oldVO);
			// ����Table����Ҫ������������
			for (int i = 1; i < 6; i++)
				m_bIsNeedReInit[i] = true;

			// ��д��ͷʱ���
			getCtBillCardPanel().setHeadItem("ts",
					newVO.getParentVO().getAttributeValue("ts"));
			ManageItemVO[] itemVO = (ManageItemVO[]) newVO.getChildrenVO();
			for (int i = 0; i < itemVO.length; i++) {
				// ��д����ʱ���
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
					"4020pub", "UPP4020pub-000117")/* @res "����ɹ���" */);

			getCtBillCardPanel().updateValue();
			getCtBillCardPanel().setEnabled(false);

			m_bErrFlag = false;
			// ִ�й�ʽ
			getCtBillCardPanel().getBillModel().execLoadFormula();
			// ����ú�ͬ�Ѿ���ѯ����ʷ��¼��Ӧ���ڹ�ϣ���������¼���Ա��ܹ����²�ѯ��
			String pk_ct_manage = (String) newVO.getParentVO()
					.getAttributeValue("pk_ct_manage");
			if (m_htChanged != null && pk_ct_manage != null
					&& m_htChanged.containsKey(pk_ct_manage))
				m_htChanged.remove(pk_ct_manage);
			m_timer.showExecuteTime("����������"); /*-=notranslate=-*/

		} catch (Exception e) {

			nc.vo.scm.pub.SCMEnv.out(e);
			showErrorMessage(e.getMessage());
			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"4020pub", "UPP4020pub-000077")/* @res "����ʧ�ܣ�" */);
			return false;
		}
		return true;
	}
	/**
	 * �˴����뷽��˵���� ����������. �������ڣ�(2004-4-9 14:11:32)
	 * 
	 * @author��������
	 * @return nc.vo.ct.pub.ManageVO
	 * @param vo
	 *            nc.vo.ct.pub.ManageVO
	 */
	protected ManageVO sendtoAudit(ManageVO tempMVO) throws Exception {
		if (tempMVO == null)
			return null;
		// added by lirr 2008-10-28 ������ͬ״̬��Ϊ�������С�
		// ManageVO tempMVO = new ManageVO();

		// tempMVO = vo.clone(vo);
		//
		// tempMVO.getParentVO().setAttributeValue("ctflag",
		// new Integer(BillState.CHECKGOING));
		// tempMVO.getParentVO()
		// .setAttributeValue("coperatoridnow", getOperator());
		// // added by lirr 2009-7-9����04:27:52 ���õ������� ����ʱʹ��
		// tempMVO.setBillType(m_sBillType);
		// tempMVO.setOldBillStatus(BillState.FREE);
		// tempMVO.setBillStatus(BillState.CHECKGOING);
		ArrayList alRet = null;

		alRet = (ArrayList) nc.ui.pub.pf.PfUtilClient.processAction("SAVE",
				m_sBillType, getClientEnvironment().getDate().toString(),
				tempMVO, tempMVO);
		// modified by lirr 2009-8-24����10:42:00 ˢ��ts�����ߺ�̨���ڶ����з���
		if (alRet != null && alRet.size() > 0)
			tempMVO.getParentVO()
					.setAttributeValue("ts", (String) alRet.get(1));
		return tempMVO;
	}

	/**
	 * �༭���¼��� �������ڣ�(2001-3-23 2:02:27)
	 * 
	 * @param e
	 *            ufbill.BillEditEvent
	 */
	public void setCardOriAndCurrDigit(String sCurrid) {

		// ��ô˱��ֵ�С��λ��
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

		// ����ԭ�ҽ��С��λ��
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
		// modified by lirr 2008-12-24 �۱����ʾ����·��� since v55
		getCtBillCardPanel().getHeadItem("currrate").setDecimalDigits(
				getCurrateDigit(sCurrid));
		/*
		 * getCtBillCardPanel().getHeadItem("astcurrate").setDecimalDigits(
		 * m_iRateDigit[1]);
		 */

	}

	/**
	 * �˴����뷽��˵���� �����������ȳ���. �������ڣ�(2003-11-10 10:24:19)
	 */
	protected void setInputLen() {
		int iZHENG = DBDataLeng.INT_LENG1;// ==12
		// ������������
		/*
		 * getCtBillCardPanel().getBodyItem(CTTableCode.BASE,
		 * "astnum").setLength( iZHENG + 1 + m_iFracAmountDigit);
		 * getCtBillCardPanel().getBodyItem(CTTableCode.BASE, "astprice")
		 * .setLength(iZHENG + 1 + m_iPriceDigit);
		 * getCtBillCardPanel().getBodyItem(CTTableCode.BASE, "asttaxprice")
		 * .setLength(iZHENG + 1 + m_iPriceDigit);
		 */

		// deleted by lirr 2008-8-20 ����5.5����ɾ��������Ϣ
		/*
		 * getCtBillCardPanel().getBodyItem(CTTableCode.BASE,
		 * "astsum").setLength( iZHENG + 1 + m_iFracCurrDigit);
		 * getCtBillCardPanel().getBodyItem(CTTableCode.BASE, "asttaxmny")
		 * .setLength(iZHENG + 1 + m_iFracCurrDigit);
		 * getCtBillCardPanel().getBodyItem(CTTableCode.BASE, "asttaxsummny")
		 * .setLength(iZHENG + 1 + m_iFracCurrDigit);
		 */

		// ��������������
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

		// ��ʼ������
		getCtBillCardPanel().getBodyItem(CTTableCode.BASE, "transrate")
				.setLength(iZHENG + 1 + m_iHslDigit);

		// Ԥ�����޶�
		if (getCtBillCardPanel().getHeadItem("nprepaylimitmny") != null)
			getCtBillCardPanel().getHeadItem("nprepaylimitmny").setLength(
					iZHENG + 1 + m_iMainCurrDigitcw);
		//
		// Ԥ����added by lirr 2008-10-15
		if (getCtBillCardPanel().getHeadItem("nprepaymny") != null)
			getCtBillCardPanel().getHeadItem("nprepaymny").setLength(
					iZHENG + 1 + m_iMainCurrDigitcw);
		// �ۼ��ո����ܶ�added by lirr 2008-10-15
		if (getCtBillCardPanel().getHeadItem("ntotalgpamount") != null)
			getCtBillCardPanel().getHeadItem("ntotalgpamount").setLength(
					iZHENG + 1 + m_iMainCurrDigitcw);
		// �ۼ�Ӧ��/�����ܶ�added by lirr 2008-10-15
		if (getCtBillCardPanel().getHeadItem("ntotalgpshamount") != null)
			getCtBillCardPanel().getHeadItem("ntotalgpshamount").setLength(
					iZHENG + 1 + m_iMainCurrDigitcw);
		// �ۼ�Ӧ��/������added by lirr 2008-10-15
		if (getCtBillCardPanel().getBodyItem(CTTableCode.BASE, "ntotalgpmny") != null)
			getCtBillCardPanel().getBodyItem(CTTableCode.BASE, "ntotalgpmny")
					.setLength(iZHENG + 1 + m_iMainCurrDigitcw);
		// �ۼ�Ӧ��/������added by lirr 2008-10-15
		if (getCtBillCardPanel().getBodyItem(CTTableCode.BASE, "ntotalshgpmny") != null)
			getCtBillCardPanel().getBodyItem(CTTableCode.BASE, "ntotalshgpmny")
					.setLength(iZHENG + 1 + m_iMainCurrDigitcw);

		// ˰��
		int iTaxdigit = getCtBillCardPanel().getBodyItem(CTTableCode.BASE,
				"taxration").getDecimalDigits();
		getCtBillCardPanel().getBodyItem(CTTableCode.BASE, "taxration")
				.setLength(DBDataLeng.INT_LENG2 + 1 + iTaxdigit);

		// ����
		int iCurrate = getCtBillCardPanel().getHeadItem("currrate")
				.getDecimalDigits();
		getCtBillCardPanel().getHeadItem("currrate").setLength(
				iZHENG + 1 + iCurrate);

		// �۸�����
		/*
		 * int iAstCurrate = getCtBillCardPanel().getHeadItem("astcurrate")
		 * .getDecimalDigits();
		 * getCtBillCardPanel().getHeadItem("currrate").setLength( iZHENG + 1 +
		 * iAstCurrate);
		 */
		getCtBillCardPanel().getHeadItem("currrate").setLength(iZHENG + 1);

		// ԭ�ҵ��۲���Ϊ����:
		((nc.ui.pub.beans.UIRefPane) (getCtBillCardPanel().getBodyItem(
				CTTableCode.BASE, "oriprice").getComponent())).setDelStr("-");
		((nc.ui.pub.beans.UIRefPane) (getCtBillCardPanel().getBodyItem(
				CTTableCode.BASE, "oritaxprice").getComponent()))
				.setDelStr("-");
		((nc.ui.pub.beans.UIRefPane) (getCtBillCardPanel().getBodyItem(
				CTTableCode.BASE, "taxration").getComponent())).setDelStr("-");

	}

	/**
	 * �˴����뷽��˵�� �������ڣ�(2003-10-22 19:09:33)
	 * 
	 * @author��������
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
	 * �˴����뷽��˵���� ��������:����������ť�µ��Ӱ�ť״̬�Ͱ�ť��Ȩ�޸ı�ҳǩ��״̬��ʹҳǩ����Ȩ�޿��ơ� �������: ����ֵ: �쳣����:
	 * ����:������ ����:
	 * 
	 * @param bTerm
	 *            boolean ��ͬ���ť��״̬
	 * @param bExp
	 *            boolean ��ͬ���ð�ť��״̬
	 * @param bNotes
	 *            boolean ��ͬ����°�ť��״̬
	 * @param bModify
	 *            boolean ��ͬ�����ť��״̬
	 * @param bExec
	 *            boolean ִ�й��̰�ť��״̬
	 */
	protected void setTabbedState(boolean bTerm, boolean bExp, boolean bNotes,
			boolean bModify, boolean bExec) {
		int size = getCtBillCardPanel().getBodyTabbedPane().getTabCount();
		// ��ҳǩ��ť����Ϊ������
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
	 * �˴����뷽��˵���� �������ڣ�(2004-4-7 15:12:07)
	 * 
	 * @author��������
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
	 * ����һ���ڲ���. �̳�IFreshTsListener. ʵ�ִ�ӡ���ts��printcount�ĸ���. @author �۱� ����ʱ��:
	 * 2004-12-23
	 */
	public class FreshTsListener implements IFreshTsListener {

		/*
		 * ���� Javadoc��
		 * 
		 * @see nc.ui.scm.print.IFreshTsListener#freshTs(java.lang.String,
		 *      java.lang.String)
		 */
		public void freshTs(String sBillID, String sTS, Integer iPrintCount) {
			// fresh local TS with voPr.getNewTs();

			if (m_vBillVO == null || m_vBillVO.size() == 0)
				return;

			// �жϴ�ӡ��vo�Ƿ����ڻ����У�
			// �ڴ�ӡԤ��״̬��ӡʱ������vo���ܻ��иı䣬����Ҫ�жϣ�
			int index = 0;
			ExtendManageVO extendManageVO = null;
			ManageHeaderVO headerVO = null;
			for (; index < m_vBillVO.size(); index++) {
				extendManageVO = (ExtendManageVO) m_vBillVO.elementAt(index);
				headerVO = (ManageHeaderVO) extendManageVO.getParentVO();

				// ��sBillID����ʱ���Ѿ��ж�sBillID��Ϊnull.
				if (sBillID.equals(headerVO.getPrimaryKey()))
					break;
			}

			if (index == m_vBillVO.size()) // ���ڻ���vo�У�������и��£�
				return;

			// �ڻ���vo��
			headerVO.setAttributeValue("ts", sTS);
			headerVO.setAttributeValue("iprintcount", iPrintCount);

			if (m_bIsCard == true) { // Card
				if (index == m_iId - 1) { // ���Ϊ��ǰcard��ʾvo.
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
	 * ���� Javadoc��
	 * 
	 * @see nc.ui.pub.bill.BillModelCellEditableController#isCellEditable(boolean,
	 *      int, java.lang.String) �༭ǰ���� �滻beforeEdit().
	 */
	public boolean isCellEditable(boolean value, int row, String itemkey) {
		if (m_iBillState == OperationState.EDIT
				|| m_iBillState == OperationState.ADD
				|| m_iBillState == OperationState.CHANGE) {
			getCtBillCardPanel().superStopEditing();

			Object oRefValue = ((UIRefPane) getCtBillCardPanel().getHeadItem(
					"ct_type").getComponent()).getRefValue("ninvctlstyle");

			if (itemkey.equals("transrate")) { // ������λ������
				if (getCtBillCardPanel().getBodyValueAt(row, "invid") == null)
					return false;
				String sInvManID = getCtBillCardPanel().getBodyValueAt(row,
						"invid").toString();

				// ȡ����λ
				BillItem bt = getCtBillCardPanel().getBodyItem("astmeasname");
				UIRefPane refAstUnit = (UIRefPane) bt.getComponent();

				// ����λpk
				// Object oAstUnitPk = refAstUnit.getRefPK();
				// modified by lirr 2009-01-07
				Object oAstUnitPk = getCtBillCardPanel().getBodyValueAt(row,
						"astmeasid");
				if (isNull(oAstUnitPk)) {
					return false;
				}
				// modified by lirr 2009-9-3����04:59:34 ����������
				/*
				 * m_voInvMeas.filterMeas(getCorpPrimaryKey(), sInvManID,
				 * refAstUnit); MeasureRateVO voMeas =
				 * m_voInvMeas.getMeasureRate(sInvManID, oAstUnitPk.toString()); //
				 * UFBoolean ufbFixedflag = voMeas.getFixedflag(); // added by
				 * lirr 2008-12-25 �ӿ��ж� UFBoolean ufbFixedflag = new
				 * UFBoolean(false); if (null != voMeas && null !=
				 * voMeas.getFixedflag()) ufbFixedflag = voMeas.getFixedflag(); //
				 * �̶������� if (ufbFixedflag.booleanValue()) { return false; } //
				 * �䶯������ else if (!ufbFixedflag.booleanValue()) { return true; }
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
			// �˴��������⣺Ӧ�����Ƹ��������Ʋ��ɱ༭��
			// ����v31����ʱ�俼�ǣ��ݲ����޸�(����������������)��������м��޸ġ�
			// �۱� on Jul 16, 2005
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
				// "UPP4020pub-000026")/* @res "����ѡ���ͬ����" */);
				// return false;
				// }

				// ���ݺ�ͬ���͵Ĵ�����Ʒ�ʽ���ô������ʹ����������Ƿ�ɱ༭
				int iInvctlstyle = Integer.parseInt(oRefValue.toString());

				// �����ͬ���͵Ĵ�����Ʒ�ʽΪ���
				if (iInvctlstyle == 0) {
					getCtBillCardPanel().getBodyItem("invclasscode")
							.setEnabled(false);
					getCtBillCardPanel().getBodyItem("invcode")
							.setEnabled(true);

					// ���ô��������룬�����������Ϊ��
					getCtBillCardPanel().setBodyValueAt(null, row,
							"invclasscode");
					getCtBillCardPanel().setBodyValueAt(null, row,
							"invclassname");
					getCtBillCardPanel().setBodyValueAt(null, row, "invclid");

					// ���ϴ�ѡ�еĴ�����
					// UIRefPane refInv = (UIRefPane) getCtBillCardPanel()
					// .getBodyItem("invcode").getComponent();
					// refInv.setPK(null);
					// refInv.getRefModel().setSelectedData(null);
				}

				// �����ͬ���͵Ĵ�����Ʒ�ʽΪ�������
				else if (iInvctlstyle == 1) {
					getCtBillCardPanel().getBodyItem("invclasscode")
							.setEnabled(true);
					getCtBillCardPanel().getBodyItem("invcode").setEnabled(
							false);
					// ���ô�����룬�������,�ͺţ��������,������λΪ��
					getCtBillCardPanel().setBodyValueAt(null, row, "invcode");
					getCtBillCardPanel().setBodyValueAt(null, row, "invname");
					getCtBillCardPanel().setBodyValueAt(null, row, "invid");
					getCtBillCardPanel().setBodyValueAt(null, row, "spec");
					getCtBillCardPanel().setBodyValueAt(null, row, "mod");

					getCtBillCardPanel().setBodyValueAt(null, row, "measname");

				}
				// �����ͬ���͵Ĵ�����Ʒ�ʽΪ�ա����������ʹ��������Ϊ�գ����ɱ༭��
				else if (iInvctlstyle == 2) {
					getCtBillCardPanel().getBodyItem("invclasscode")
							.setEnabled(false);
					getCtBillCardPanel().getBodyItem("invcode").setEnabled(
							false);
					// ���ô��������룬�����������Ϊ��
					getCtBillCardPanel().setBodyValueAt(null, row,
							"invclasscode");
					getCtBillCardPanel().setBodyValueAt(null, row,
							"invclassname");
					getCtBillCardPanel().setBodyValueAt(null, row, "invclid");
					// ���ô�����룬�������,�ͺţ��������,������λΪ��
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
				// "UPP4020pub-000026")/* @res "����ѡ���ͬ����" */);
				// return false;
				// }

				// ���ݺ�ͬ���͵Ĵ�����Ʒ�ʽ���ô������ʹ����������Ƿ�ɱ༭
				int iInvctlstyle = Integer.parseInt(oRefValue.toString());

				// ��ͬ���͵Ĵ�����Ʒ�ʽΪ��ͬ���
				if (iInvctlstyle == 0) {
					getCtBillCardPanel().getBodyItem("invclasscode")
							.setEnabled(false);
					getCtBillCardPanel().getBodyItem("invcode")
							.setEnabled(true);
					// ���ô��������룬�����������Ϊ��
					getCtBillCardPanel().setBodyValueAt(null, row,
							"invclasscode");
					getCtBillCardPanel().setBodyValueAt(null, row,
							"invclassname");
					getCtBillCardPanel().setBodyValueAt(null, row, "invclid");

				}
				// ��ͬ���͵Ĵ�����Ʒ�ʽΪ�������
				else if (iInvctlstyle == 1) {
					getCtBillCardPanel().getBodyItem("invclasscode")
							.setEnabled(true);
					getCtBillCardPanel().getBodyItem("invcode").setEnabled(
							false);
					// ���ô�����룬�������,�ͺţ��������,������λΪ��
					getCtBillCardPanel().setBodyValueAt(null, row, "invcode");
					getCtBillCardPanel().setBodyValueAt(null, row, "invname");
					getCtBillCardPanel().setBodyValueAt(null, row, "invid");
					getCtBillCardPanel().setBodyValueAt(null, row, "spec");
					getCtBillCardPanel().setBodyValueAt(null, row, "mod");
					// getCtBillCardPanel().setBodyValueAt(null, row,
					// "amount");
					getCtBillCardPanel().setBodyValueAt(null, row, "measname");

				}
				// �����ͬ���͵Ĵ�����Ʒ�ʽΪ�ա����������ʹ��������Ϊ�գ����ɱ༭��
				else if (iInvctlstyle == 2) {
					getCtBillCardPanel().getBodyItem("invclasscode")
							.setEnabled(false);
					getCtBillCardPanel().getBodyItem("invcode").setEnabled(
							false);
					// ���ô��������룬�����������Ϊ��
					getCtBillCardPanel().setBodyValueAt(null, row,
							"invclasscode");
					getCtBillCardPanel().setBodyValueAt(null, row,
							"invclassname");
					getCtBillCardPanel().setBodyValueAt(null, row, "invclid");
					// ���ô�����룬�������,�ͺţ��������,������λΪ��
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
	// * ��ConditionVO�õ���ѯ������ @author: �۱� on Jun 2, 2005
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
	// // modified by liuzy 2008-03-26 �̹�԰����Ĭ�ϲ�ѯ����
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
	// showErrorMessage("���Ƶ����ڴӡ��롾�Ƶ����ڵ���ͬΪ�����ѯ����");
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
	// // ���Ӻ�ͬ���ͼ��Ƿ��ڳ�������
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
	// // �ӹ�˾����
	// sbWhere.append(" and ct.pk_corp = '" + m_sPkCorp + "'");
	// // modified by liuzy 2008-04-11 ���ӱ��塢��ͬ���͹�˾��ѯ����
	// // sbWhere.append(" and ct_b.pk_corp = '" + m_sPkCorp + "'");
	// sbWhere.append(" and ct_type.pk_corp = '" + m_sPkCorp + "'");
	//
	// // ��¼��ѯ�������Ա�ˢ��ʹ��
	// m_sQryCondition = sbWhere.toString();
	//
	// return sbWhere.toString();
	// }

	/*
	 * ����Ƿ񸨼������� �۱� Jun 22, 2005
	 */
	protected UFBoolean[] isAstUnitMng(String[] saInvBasID) {

		UFBoolean[] bAstUnit = new UFBoolean[saInvBasID.length];

		// �Ƿ񸨼�������
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
	 * �رմ��ڵĿͻ��˽ӿڡ����ڱ���������ɴ��ڹر�ǰ�Ĺ�����
	 * 
	 * @return boolean ����ֵΪtrue��ʾ�����ڹرգ�����ֵΪfalse��ʾ�������ڹرա� �������ڣ�(2005-10-24
	 *         13:52:37)
	 */
	public boolean onClosing() {
		// �������༭����
		if (m_iBillState == OperationState.ADD
				|| m_iBillState == OperationState.EDIT
				|| m_iBillState == OperationState.CHANGE) {
			int ret = MessageDialog.showYesNoCancelDlg(this, null,
					nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
							"UCH001")/* @res "�Ƿ񱣴����޸ĵ����ݣ�" */);
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
	 * ���� Javadoc��
	 * 
	 * @see nc.ui.scm.pub.bill.IBillExtendFun#getExtendBtns() ��ȡ��չ��ť���� ע��:
	 *      ��չ��ťĬ�ϼ��ں�ͬ����ҳǩ֮��.
	 */
	public ButtonObject[] getExtendBtns() {
		return null;
	}

	/*
	 * ���� Javadoc��
	 * 
	 * @see nc.ui.scm.pub.bill.IBillExtendFun#onExtendBtnsClick(nc.ui.pub.ButtonObject)
	 *      ������չ��ť���¼�
	 */
	public void onExtendBtnsClick(ButtonObject bo) {
	}

	/*
	 * ���� Javadoc��
	 * 
	 * @see nc.ui.scm.pub.bill.IBillExtendFun#setExtendBtnsStat(int)
	 *      OperationState.FREE ��� OperationState.ADD ���� OperationState.EDIT �༭
	 *      OperationState.CHANGE ��� ע�⣺��չ��ťĬ�ϼ��ں�ͬ����ҳǩ֮��.
	 *      ��������setButtonState()��������,�����Ϊ��ͬ��״̬. ShawBing 2005-11-02
	 */
	public void setExtendBtnsStat(int iState) {

	}

	public List getRelaSortObject() {
		return m_vBillVO;
	}

	public int getSortTypeByBillItemKey(String key) {
		// �к�
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
	 * ������ʱ���ݼ��ط�����
	 */
	public void doApproveAction(ILinkApproveData approvedata) {
		qryCt(approvedata.getBillID());

		// ���ð�ť״̬
		m_iBillState = OperationState.FREE;
		// setButtonState();
		if (!m_bAddFromBillFlag) {
			setButtonState();
		} else {
			setButtonStateForCof();
		}
	}

	/**
	 * ����ʱ���ݼ���֮��
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
			// modified by liuzy 2008-04-28 �̹�԰��ѯ���죬����
			arrMangevos = ContractQueryHelper.queryAllHeadData(m_sPkCorp,
					sWhere.toString());
			if (arrMangevos == null || arrMangevos.length == 0) {
				this.showHintMessage(nc.ui.ml.NCLangRes.getInstance()
						.getStrByID("4020pub", "UPP4020pub-000070")/*
																	 * @res
																	 * "û���ҵ���������������"
																	 */);
				return;
			}
			sPkCorp = arrMangevos[0].getPk_corp();

		} catch (Exception e) {
			// TODO �Զ����� catch ��
			GenMethod.handleException(this, e.getMessage(), e);
		}

		/*
		 * if ((!sPkCorp.equals(m_sPkCorp))&&(!sPkCorp.equals(""))){ BillData
		 * bdMy = new BillData(m_BillCardPanel.getTempletData( m_sBillType,
		 * null, m_sPkUser, sPkCorp)); if (bdMy!=null) {
		 * getCtBillCardPanel().setBillData(bdMy); } }
		 */

		qryCt(querydata.getBillID());

		// ���ð�ť״̬
		// �޸���:������ �޸�����:200703201441 �޸�ԭ��:�����鵥������ǿ繫˾��ʱ,��ťȫ��������
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
	 * ������ѯ ���������Ƶ���ʱʹ�ã�
	 */
	public void doMaintainAction(ILinkMaintainData maintaindata) {
		qryCt(maintaindata.getBillID());

		// ���ð�ť״̬
		m_iBillState = OperationState.FREE;
		// setButtonState();
		if (!m_bAddFromBillFlag) {
			setButtonState();
		} else {
			setButtonStateForCof();
		}
	}

	/**
	 * ת���ɵ���״̬ҳǩ�İ�ť��ʾ
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
				true);// ��һҳ
		getButtonTree().getButton(CTButtonConst.BTN_BROWSE_PREVIOUS)
				.setVisible(true);// ��һҳ
		getButtonTree().getButton(CTButtonConst.BTN_ASSIST_FUNC_DOCUMENT)
				.setVisible(true);// �ĵ�����
		getButtonTree().getButton(CTButtonConst.BTN_QUERY).setVisible(true);// ��ѯ
		getButtonTree().getButton(CTButtonConst.BTN_ASSIST_QUERY).setVisible(
				true);// ������ѯ

		// modified by liuzy 2008-05-07 ©����ִ�й��̰�ť
		getButtonTree().getButton(CTButtonConst.BTN_CTOTHER_EXEC).setVisible(
				true);
	}

	/**
	 * ת���ɷǵ���״̬ҳǩ�İ�ť��ʾ
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
				false);// ��һҳ
		getButtonTree().getButton(CTButtonConst.BTN_BROWSE_PREVIOUS)
				.setVisible(false);// ��һҳ
		getButtonTree().getButton(CTButtonConst.BTN_ASSIST_FUNC_DOCUMENT)
				.setVisible(false);// �ĵ�����
		getButtonTree().getButton(CTButtonConst.BTN_QUERY).setVisible(false);// ��ѯ
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

		getButtonTree().getButton(CTButtonConst.BTN_EXECUTE).setVisible(false);// ִ��
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
				false);// getButtonTree().getButton(CTButtonConst.BTN_CTOTHER_EXP).setVisible(false);��ͬ����
		getButtonTree().getButton(CTButtonConst.BTN_EXECUTE_SENDAPPRPVE)
				.setVisible(false);// getButtonTree().getButton(CTButtonConst.BTN_EXECUTE_SENDAPPRPVE).setVisible(false);
		getButtonTree().getButton(CTButtonConst.BTN_TAB_PRINT).setVisible(true);// getButtonTree().getButton(CTButtonConst.BTN_TAB_PRINT).setVisible(true);
		getButtonTree().getButton(CTButtonConst.BTN_CTOTHER_TERM).setVisible(
				false);// getButtonTree().getButton(CTButtonConst.BTN_CTOTHER_TERM).setVisible(false);
		getButtonTree().getButton(CTButtonConst.BTN_ASSIST_QUERY_FLOWSTATUE)
				.setVisible(false);// getButtonTree().getButton(CTButtonConst.BTN_ASSIST_QUERY_FLOWSTATUE).setVisible(false);
		getButtonTree().getButton(CTButtonConst.BTN_ASSIST_QUERY).setVisible(
				false);// ������ѯ
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
	 * ��ʾ��ҳ since V51
	 * 
	 * @author dgq
	 */
	protected void onTop() {

	}

	/**
	 * ��ʾĩҳ since V51
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
	 * �����ߣ�yb ���ܣ���ȡ��ǰ�û�ID��Ӧ��ҵ��Ա ������ ���أ� ���⣺ ���ڣ�(2001-5-9 9:23:32)
	 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 */
	public static PsndocVO getPsndocByUserid() {
		return getPsndocByUserid(ClientEnvironment.getInstance()
				.getCorporation().getPrimaryKey(), ClientEnvironment
				.getInstance().getUser().getPrimaryKey());

	}

	/**
	 * �����ߣ�yb ���ܣ������û�ID,��ȡ��Ӧ��ҵ��Ա ������ ���أ� ���⣺ ���ڣ�(2001-5-9 9:23:32)
	 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
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
			// modified by lirr 2008-09-09 �޸�ԭ�� �淶��
			return ((nc.itf.uap.rbac.IUserManageQuery) NCLocator.getInstance()
					.lookup(IUserManageQuery.class.getName()))
					.getPsndocByUserid(pk_corp, userid);
		} catch (Exception e) {
			GenMethod.handleException(null, null, e);
		}
		return null;
	}

	/**
	 * ���������������������������
	 * <p>
	 * <b>examples:</b>
	 * <p>
	 * ʹ��ʾ��
	 * <p>
	 * <b>����˵��</b>
	 * 
	 * @param sAction
	 *            ��������
	 *            <p>
	 * @author liuzy
	 * @time 2007-4-16 ����02:56:28
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
	// if ("����".equals(sAction)) {
	// shint = nc.ui.ml.NCLangRes.getInstance().getStrByID(
	// "4020pub", "UPT4020pub-000243")/* @res "��ʼ��������" */;
	// } else if ("����".equals(sAction)) {
	// shint = nc.ui.ml.NCLangRes.getInstance().getStrByID(
	// "4020pub", "UPT4020pub-000244")/* @res "��ʼ��������" */;
	// } else if ("��Ч".equals(sAction)) {
	// shint = nc.ui.ml.NCLangRes.getInstance().getStrByID(
	// "4020pub", "UPT4020pub-000245")/* @res "��ʼ������Ч" */;
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
	// * @res "����ˢ�½���..."
	// */, 0, 3, this.getClass().getName(), this, "onFresh",
	// new Class[] { boolean.class },
	// new Object[] { true });
	// } catch (Exception e) {
	// // ��־�쳣
	// nc.vo.scm.pub.SCMEnv.out(e);
	// // ���淶�׳��쳣
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
	 * ��������������������ǰ�̣߳������߳��д���sAction����
	 * <p>
	 * <b>examples:</b>
	 * <p>
	 * ʹ��ʾ��
	 * <p>
	 * <b>����˵��</b>
	 * 
	 * @param sAction
	 *            ��������
	 * @param iaSeleRows
	 *            ��ͷ��ѡ����
	 * @return
	 *            <p>
	 * @author liuzy
	 * @time 2007-4-16 ����06:02:04
	 */
	public Object[] onBatchProcessInThread(int[] iaSeleRows, String sAction) {
		// ������Ϣ��
		StringBuffer sb = new StringBuffer();
		// ��ͬ����VO
		ManageVO tempVO = null;
		// ��ǰ�����±�
		int iIdtemp = m_iId;
		// ִ�й���
		String stemp = null;
		// ��������
		int errcount = 0;
		// ����ʱ��
		final int isleep = 200;
		// ����ֵ
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
					if ("����".equals(sAction)) {
						/*
						 * LongTimeTask.showHintMsg("����" +
						 * tempVO.getParentVO().getCt_code() + "��ʼ����...");
						 */
						LongTimeTask.showHintMsg(nc.ui.ml.NCLangRes
								.getInstance().getStrByID(
										"4020pub",
										"UPT4020pub-000233",
										null,
										new String[] { tempVO.getParentVO()
												.getCt_code() }/*
																 * @res
																 * "��ͬ{0}��ʼ����..."
																 */));
						// ������˷���

						String oncheckRtn = onCheck(true);
						if (null != oncheckRtn) {
							throw new nc.vo.pub.BusinessException(oncheckRtn);
						}
						stemp = nc.ui.ml.NCLangRes.getInstance().getStrByID(
								"4020pub",
								"UPT4020pub-000234",
								null,
								new String[] { tempVO.getParentVO()
										.getCt_code() }/* @res "��ͬ{0}�����ɹ�" */);
						LongTimeTask.showHintMsg(stemp);
					} else if ("����".equals(sAction)) {
						LongTimeTask.showHintMsg(nc.ui.ml.NCLangRes
								.getInstance().getStrByID(
										"4020pub",
										"UPT4020pub-000235",
										null,
										new String[] { tempVO.getParentVO()
												.getCt_code() }/*
																 * @res
																 * "��ͬ{0}��ʼ����..."
																 */));
						// �������󷽷�
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
										.getCt_code() }/* @res "��ͬ{0}����ɹ�" */);
						LongTimeTask.showHintMsg(stemp);
					} else if ("��Ч".equals(sAction)) {
						LongTimeTask.showHintMsg(nc.ui.ml.NCLangRes
								.getInstance().getStrByID(
										"4020pub",
										"UPT4020pub-000237",
										null,
										new String[] { tempVO.getParentVO()
												.getCt_code() }/*
																 * @res
																 * "��ͬ{0}��ʼ��Ч..."
																 */));
						onBatchValidate();
						if (!isExecPass) {
							isExecPass = true;
							String msg = nc.ui.ml.NCLangRes.getInstance()
									.getStrByID("4020pub", "UPT4020pub-000241")/*
																				 * @res
																				 * "��ͬ{0}��Чʧ��"
																				 */;
							throw new Exception("");
						}
						stemp = nc.ui.ml.NCLangRes.getInstance().getStrByID(
								"4020pub",
								"UPT4020pub-000238",
								null,
								new String[] { tempVO.getParentVO()
										.getCt_code() }/* @res "��ͬ{0}��Ч�ɹ�" */);
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
				if ("����".equals(sAction))
					stemp = nc.ui.ml.NCLangRes
							.getInstance()
							.getStrByID(
									"4020pub",
									"UPT4020pub-000239",
									null,
									new String[] { tempVO.getParentVO()
											.getCt_code() }/* @res "��ͬ{0}����ʧ��" */);
				else if ("����".equals(sAction))
					stemp = nc.ui.ml.NCLangRes
							.getInstance()
							.getStrByID(
									"4020pub",
									"UPT4020pub-000240",
									null,
									new String[] { tempVO.getParentVO()
											.getCt_code() }/* @res "��ͬ{0}����ʧ��" */);
				else if ("��Ч".equals(sAction))
					stemp = nc.ui.ml.NCLangRes
							.getInstance()
							.getStrByID(
									"4020pub",
									"UPT4020pub-000241",
									null,
									new String[] { tempVO.getParentVO()
											.getCt_code() }/* @res "��ͬ{0}��Чʧ��" */);
				LongTimeTask.showHintMsg(stemp);
				sb.append(stemp + "[" + e.getMessage() + "]");
				sb.append("\n");
				errcount++;
				try {
					Thread.currentThread().sleep(isleep);
				} catch (Exception ex) {
					// ��־�쳣
					nc.vo.scm.pub.SCMEnv.out(ex.getMessage());

				}
				// ��־�쳣
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
	 * ������������������б�����ѡ�ĵ���
	 * <p>
	 * <b>examples:</b>
	 * <p>
	 * ʹ��ʾ��
	 * <p>
	 * <b>����˵��</b>
	 * 
	 * @return
	 * <p>
	 * @author liuzy
	 * @time 2007-4-16 ����03:06:04
	 */
	public ManageVO[] getCurSeleVOs() {
		// ���ѡȡ������
		int iRowCount = getCtBillListPanel().getHeadTable()
				.getSelectedRowCount();
		int[] iaSeleRows = getCtBillListPanel().getHeadTable()
				.getSelectedRows();
		if (iRowCount < 1 || iaSeleRows == null || iaSeleRows.length < 1) {
			return null;
		}
		ManageVO[] aMVOs = new ManageVO[iRowCount];// ���ص�VO����
		ExtendManageVO extMVO = new ExtendManageVO();// ��m_vBillVO��ȡ����VO
		ManageHeaderVO voHeader = new ManageHeaderVO();
		int j = -1;// ��ǰѡ�е���
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
	 * ���෽����д
	 * 
	 * @see nc.ui.pub.linkoperate.ILinkAdd#doAddAction(nc.ui.pub.linkoperate.ILinkAddData)
	 *      added by lirr 2008-7-16
	 */
	public void doAddAction(ILinkAddData adddata) {
		Object paraVo = adddata.getUserObject();
		if (null != adddata && paraVo instanceof PUMessageVO) {
			// �۸������������ɺ�ͬ����ť����
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
				// TODO �Զ����� catch ��
				// e.printStackTrace();
				SCMEnv.out(e.getMessage());

			}

			// ManageVO[] arrMangevos = splitManageVOs(manageVo);// ���VO���ֵ�
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
	 * ����������������Ҫ�����������Ĺ��ܡ�
	 * <p>
	 * <b>examples:</b>
	 * <p>
	 * ʹ��ʾ��
	 * <p>
	 * <b>����˵��</b>
	 * 
	 * @param adddata
	 *            <p>
	 * @author lirr
	 * @time 2008-7-16 ����10:40:10
	 */
	private void confBillFor28toThis(PUMessageVO paraVo) {

		try {

			// if (null != adddata && adddata instanceof RefMsg) {
			// VO���ݽ���
			PriceauditMergeVO priceauditVO = (PriceauditMergeVO) paraVo
					.getAskvo();
			ManageVO manageVo = null;
			try {
				manageVo = (ManageVO) nc.ui.pub.change.PfChangeBO_Client
						.pfChangeBillToBill(priceauditVO,
								nc.vo.scm.constant.ScmConst.PO_PriceAudit,
								m_sBillType);
			} catch (BusinessException e) {
				// TODO �Զ����� catch ��
				// e.printStackTrace();
				SCMEnv.out(e.getMessage());
			}

			ManageVO[] arrMangevos = splitManageVOs(manageVo);// ���VO���ֵ�

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
																	 * "û���ҵ���������������"
																	 */);

				return;
			}
			for (int i = 0; i < arrMangevos.length; i++) {
				arrMangevos[i].getParentVO().setAttributeValue("ctflag",
						new Integer(BillState.FREE));

			}
			m_vBillVOForRef.clear();
			// ���ع�ʽ

			putInBillVOForRef(arrMangevos);

			if (m_vBillVOForRef != null && m_vBillVOForRef.size() > 0) {
				m_bIsCard = false;
				// �ж����ڿ�Ƭģʽ�£��������б�ģʽ��
				m_bIsFirstClick = true; // ��־�ǵ�һ�ε��
				m_iId = 1;
				m_iElementsNum = m_vBillVOForRef.size();
				if (1 == m_iElementsNum) {
					m_bIsCard = false;
					onList();
					getCtBillCardPanel().setEnabled(true);
				} else {
					if (m_bIsCard) { // ��Ƭ
						// �л����б�ģʽ��
						onList();

					} else { // �б�
						setListRateDigit();
						// ��ʾ�����б�
						ManageHeaderVO[] vListVO = new ManageHeaderVO[m_vBillVOForRef
								.size()];
						if (m_vBillVOForRef.size() != 0)
							for (int i = 0; i < m_vBillVOForRef.size(); i++) {
								vListVO[i] = (ManageHeaderVO) ((ExtendManageVO) m_vBillVOForRef
										.elementAt(i)).getParentVO();
							}

						getCtBillListPanel().setHeaderValueVO(vListVO);

						// ִ�й�ʽ
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

				m_bChangeFlag = false; // ��־��ͬ���ɱ��
				// ����Table����Ҫ������������
				for (int i = 1; i < 6; i++)
					m_bIsNeedReInit[i] = true;

			}

		} catch (Exception e) {
			// ��־�쳣
			nc.vo.scm.pub.SCMEnv.out(e);
			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"40081004", "UPT40081004-000020")/* @res "�������ݳ���" */
					+ e.getMessage());
		}
		// m_iBillState = OperationState.ADD ;
		setButtonStateForCof();
	}

	/**
	 * ���ݹ�Ӧ��+����+�Ƿ�ί����зֵ�
	 * 
	 * @return ManageVO[]
	 * @param billvo
	 *            ��Ҫ�ֵ���ManageVO strParaArray java.lang.String[] �ֵ�������
	 *            ���أ�ManageVO[] �����ֵ���ManageVO����
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

				// ��ȡ��ǰ�û�ID��Ӧ��ҵ��Ա
				if (null == tempbillvos[k].getParentVO().getAttributeValue(
						"personnelid")) {
					if (psnvo != null) {// �����Ӧ��ҵ��Ա
						if (null != psnvo.getPk_psndoc()) {
							// �õ�ǰ�û�ID��Ӧ��ҵ��Ա
							tempbillvos[k].getParentVO().setAttributeValue(
									"personnelid", psnvo.getPk_psndoc());
							tempbillvos[k].getParentVO().setAttributeValue(
									"pername", psnvo.getPk_psndoc());
						}
						if (null != psnvo.getPk_deptdoc()) {
							// �õ�ǰ�û�ID��Ӧ��ҵ��Ա��Ӧ�Ĳ���
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
				// ��ͬǩ����������Ĭ��ֵ
				tempbillvos[k].getParentVO().setAttributeValue("subscribedate",
						m_UFToday);

				tempbillvos[k].getParentVO()
						.setAttributeValue("audiname", null);
				tempbillvos[k].getParentVO().setAttributeValue("auditdate",
						null);

				// set��ӡ����Ϊ����
				tempbillvos[k].getParentVO().setAttributeValue("iprintcount",
						new Integer(0));

				// һЩ������ֵ

				billvoslist.add(tempbillvos[k]);

			}
		}

		if (billvoslist.size() > 0) {
			retvos = new ManageVO[billvoslist.size()];
			retvos = (ManageVO[]) billvoslist.toArray(retvos);
		}

		return retvos;

	}

	// added by lirr 2008-08-12 ������
	/**
	 * ���ߣ�lirr ���ܣ���������λת������������������
	 * 
	 * ������ ���أ� ���⣺ ���ڣ�(2008-08-12 08:25:09)
	 */
	public void setVODefaultMeas(ManageVO voManage) {
		ManageItemVO[] voaItem = (ManageItemVO[]) voManage.getChildrenVO();
		// ���ڱ�����ѯ
		Vector vecBaseId = new Vector();
		Vector vecAssistId = new Vector();
		// ����
		String sMangId = null;
		String sBaseId = null;
		String sAssistUnit = null;

		int iBodyLen = voaItem.length;
		// ��ô����������������������
		String[] invkeys = new String[voaItem.length];
		String[] astkeys = new String[voaItem.length];
		for (int i = 0; i < voaItem.length; i++) {
			invkeys[i] = (String) voaItem[i].getAttributeValue("invbasid");
			astkeys[i] = (String) voaItem[i].getAttributeValue("astmeasid");
		}
		// ��ô����Ϣ
		InvVO[] invvos = getInvInfo(invkeys, astkeys);

	}

	/**
	 * ���ߣ�lirr ���ܣ�����/�����ť���ã���ʽ���ɲ��񣺸��/�տ
	 * 
	 * ������ ���أ� ���⣺ ���ڣ�(2008-08-12 08:25:09)
	 */
	protected void onAddSkOrFk() {
		// Ҫ����񵥾ݽ���VO���յĺ�ͬVO
		ManageVO billVOZ = null;
		// ��ʱ�ĺ�ͬVO��ȡ��ǰ����vo�����ˣ����Ҽ�˰�ϼơ��ۼƸ��������0��״̬Ϊ��Ч�� ��ֵ��billVOZ
		ManageVO tempBillVOZ = new ManageVO();
		// �õ�����VO
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
			// �б����ת����Ƭ����
			if (!m_bIsCard) {
				onList();
			}
			// ��Ƭ�����õ�ǰ����VO

			else {
				/*
				 * if (null != getCurVO()) { tempBillVOZ = getCurVO(); }
				 */
				/*
				 * if (null != newManageVO) { tempBillVOZ =
				 * newManageVO.clone(newManageVO); } else {
				 * showHintMessage(nc.ui.ml.NCLangRes.getInstance()
				 * .getStrByID("4020pub", "UPT4020pub-000254") @res "��ѡ��һ����ͬ" );
				 * return; }
				 */
				if (null == tempBillVOZ) {
					showHintMessage(nc.ui.ml.NCLangRes.getInstance()
							.getStrByID("4020pub", "UPT4020pub-000254")
					/*
					 * @res "��ѡ��һ����ͬ"
					 */
					);
					return;
				}
			}

			// ���˷��������ĺ�ͬ ��ͬ״̬����Ч�������Ҽ�˰�ϼơ��ۼƸ��������0
			billVOZ = getBodyVOForArap(tempBillVOZ);

			if (null == billVOZ) {
				showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"4020pub", "UPT4020pub-000257")/*
														 * @res "û�з��������ĺ�ͬ"
														 */);
				return;
			}

			ManageVO billVOZToArap = (ManageVO) billVOZ.clone(billVOZ);
			ManageHeaderVO head = billVOZToArap.getParentVO();
			head.setAttributeValue("currrate", head.getCurrrate());

			nc.vo.ct.pub.GenMethod.changeVoForFina(
					(ManageItemVO[]) (billVOZToArap.getChildrenVO()),
					billVOZToArap.getParentVO(), false);

			// ���Ҫ��ʽ���ɵ�Ŀ�굥������
			String destBillType = null;
			// ҵ�����ͣ�������Ҫ��VO�����󣬸������VO��ֵ��
			String busitype = null;
			// ���ݴ��ࣨ���ò���ӿ���Ҫ���ɹ���ͬ����ʱΪ��fk�������ۺ�ͬ����Ϊ��sk��,������ͬ�����ո������жϣ�
			String strDjdl = "fk";
			if (BillType.PURDAILY.equals(m_sBillType)) {
				strDjdl = "fk";
			} else if (BillType.SALEDAILY.equals(m_sBillType)) {
				strDjdl = "sk";
			} else if (BillType.OTHER.equals(m_sBillType)) {
				// ������ͬ�ո�����
				String strPayOrRec = null;
				/*
				 * strPayOrRec = ContractQueryHelper
				 * .queryRecorPayByCtType(tempBillVOZ.getParentVO()
				 * .getPk_ct_type());
				 */
				// select rec_or_pay from ct_type where pk_ct_type
				// modified by lirr 2009-8-27����09:42:19
				// �޸�ʱ��:2009-09-11 �޸���:��ΰƽ �޸�ԭ��: ����ת������
				Object[] obj = (Object[]) nc.ui.scm.pub.CacheTool.getCellValue(
						"ct_type", "pk_ct_type", "rec_or_pay", tempBillVOZ
								.getParentVO().getPk_ct_type());
				strPayOrRec = (String) obj[0];
				// ������ͬ�ո�����Ϊ��02�� ��
				if ("02".equals(strPayOrRec)) {
					strDjdl = "fk";
				}
				// ������ͬ�ո�����Ϊ��01�� ��
				else if ("01".equals(strPayOrRec)) {
					strDjdl = "sk";
				}

			}
			// ���ò���ӿڻ��Ҫ��ʽ���ɵ�Ŀ�굥�����ͼ�ҵ������
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
					// Ҫ��ʽ���ɵ�Ŀ�굥������
					destBillType = typeVo.getTargetbilltype();
					// ҵ������
					busitype = typeVo.getBusitype();
				} else {
					showHintMessage(nc.ui.ml.NCLangRes.getInstance()
							.getStrByID("4020pub", "UPT4020pub-000258")/*
																		 * @res
																		 * "û��Ҫ��ʽ���ɵĵ�������"
																		 */);
					return;
				}
			} else {
				showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"4020pub", "UPT4020pub-000259")/*
														 * @res "����ģ��û������"
														 */);
				return;
			}

			// VO���ݽ���
			DJZBVO destBillVoTemp = (DJZBVO) nc.ui.pub.change.PfChangeBO_Client
					.pfChangeBillToBill(billVOZToArap, m_sBillType,
							destBillType);
			if (null == destBillVoTemp) {
				showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"40080802", "UPT40080802-000316")/*
															 * @res "���ݽ���ʧ��"
															 */);
				return;
			}
			// Ϊ�����VO���á�Ŀ�굥�����͡�����ҵ�����͡�
			else {
				((DJZBHeaderVO) destBillVoTemp.getParentVO())
						.setDjlxbm(destBillType);
				((DJZBHeaderVO) destBillVoTemp.getParentVO())
						.setXslxbm(busitype);
			}
			// added by lirr 2008-11-03 ����������� ��voѹ��
			ObjectUtils.objectReference(destBillVoTemp);
			// �򿪲���ڵ�
			PfLinkData linkData = new PfLinkData();
			linkData
					.setUserObject(new Object[] { new DJZBVO[] { destBillVoTemp } });
			// ���񵥾ݽڵ��
			String nodecode = PfUIDataCache.getBillType(destBillType)
					.getNodecode();
			// �򿪲���ڵ�
			SFClientUtil.openLinkedADDDialog(nodecode, this, linkData);

		} catch (Exception e) {
			// ��־�쳣
			nc.vo.scm.pub.SCMEnv.out(e);
			// ���淶�׳��쳣
			GenMethod.handleException(this, e.getMessage(), e);
		}

	}

	/*
	 * ����ѯ������ת���Ľ������m_vBillVOForRef. lirr 2008-07-23,
	 * 
	 */
	protected void putInBillVOForRef(ManageVO[] arrMangevos) {
		// ������б�ͷvo
		CircularlyAccessibleValueObject[] headVosAll = new CircularlyAccessibleValueObject[arrMangevos.length];
		CircularlyAccessibleValueObject[] tableVOsAll = null;
		CircularlyAccessibleValueObject[] termVOsAll = null;

		ArrayList alTableVOs = new ArrayList(); // ���б���vo
		ArrayList alTermVOs = new ArrayList(); // ��������vo

		int index = 0;

		ExtendManageVO[] arrExtendVO = new ExtendManageVO[arrMangevos.length];
		for (int k = 0; k < arrMangevos.length; k++) {
			arrExtendVO[k] = new ExtendManageVO();

			arrExtendVO[k].setParentVO(arrMangevos[k].getParentVO());
			headVosAll[k] = arrMangevos[k].getParentVO();

			// modified by liuzy 2008-04-17 V5.03��ѯ���죬ֻ���ص�һ����ͬ�ı���������ҳǩ
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
		// ִ�й�ʽ
		getCtBillListPanel().getBodyBillModel().execFormulasWithVO(headVosAll,
				getHeadTailFormulas());

		// ��ͬ����
		tableVOsAll = new CircularlyAccessibleValueObject[alTableVOs.size()];
		alTableVOs.toArray(tableVOsAll);

		/*
		 * // ��������� for (int k = 0; k < tableVOsAll.length; k++) { FreeVO voFree =
		 * InvTool.getInvFreeVO((String) tableVOsAll[k]
		 * .getAttributeValue("invid")); // ����������� if (voFree != null) {
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
		// modified by lirr 2009-8-20����07:26:55 ����������
		nc.ui.scm.pub.FreeVOParse freeVOParse = new nc.ui.scm.pub.FreeVOParse();
		freeVOParse.setFreeVO(tableVOsAll, null, "invid", false);
		// ��ͬ����ִ�й�ʽ
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
	 * ����ת���������б��ͷ��ʾ��added by lirr �������ڣ�(2008-7-23 14:43:41)
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

			// ִ�й�ʽ
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
				// �޸�ԭ��ת����Ϻ�m_iElementsNum��Ҫ���£�������ɾ��ʱ������
				m_iElementsNum = m_vBillVO.size();
				getCtBillListPanel().setHeaderValueVO(vListVO);

				// ִ�й�ʽ
				getCtBillListPanel().getHeadBillModel().execLoadFormula();
				getCtBillListPanel().getHeadTable().setRowSelectionInterval(0,
						0);
				this.showHintMessage(NCLangRes.getInstance().getStrByID(
						"4020pub", "UPP4020pub-000271")/* ��� */);
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
																	 * "û���ҵ���������������"
																	 * 
																	 */);

				m_bAddFromBillFlag = false;
			}
			return;

		}
	}

	public void setButtonStateForCof() {
		// ��ͬ״̬
		int iState = -1;

		switch (m_iTabbedFlag) {
		case 0:
			if (m_iBillState == OperationState.FREE) {
				iState = OperationState.FREE;

				// ��ǰû��һ������ʱ������������ҳǩ���ɱ༭�����򶼿ɱ༭
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
				if (m_bIsCard) // �ǿ�Ƭģʽ
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

			// ���ݰ�ť��Ȩ������ҳǩ��״̬[V2.3]
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
			// ��չ��ťĬ�ϼ��ں�ͬ����ҳǩ֮��,���ÿ�������ҳǩ�Ĵ������.
			// iState: ��ͬ��״̬.

			// added by lirr 2008-09-10
			getButtonTree().getButton(CTButtonConst.BTN_PAY).setEnabled(false);
			getButtonTree().getButton(CTButtonConst.BTN_REC).setEnabled(false);
			getButtonTree().getButton(CTButtonConst.BTN_PAYORREC).setEnabled(
					false);
			break;

		default: // ����1��2��3��4ҳǩ��״̬
			setTbButtonState();
		}
		setExtendBtnsStat(iState);
		updateButtons();

	}

	/**
	 * �˴����뷽��˵���� added by lirr 2008-07-10 �����빺��/�۸����������ɲɹ���ͬ �������ڣ�(2008-07-15)
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
																	 * "û���ҵ���������������"
																	 */);

				return;
			}
			for (int i = 0; i < manageVos.length; i++) {
				manageVos[i].getParentVO().setAttributeValue("ctflag",
						new Integer(BillState.FREE));

			}
			m_vBillVOForRef.clear();
			// ���ع�ʽ

			putInBillVOForRef(manageVos);

			if (m_vBillVOForRef != null && m_vBillVOForRef.size() > 0) {
				m_bIsCard = false;
				// �ж����ڿ�Ƭģʽ�£��������б�ģʽ��
				m_bIsFirstClick = true; // ��־�ǵ�һ�ε��
				m_iId = 1;
				// m_iElementsNum = m_vBillVOForRef.size();
				if (m_bIsCard) { // ��Ƭ
					// �л����б�ģʽ��
					onList();

				} else { // �б�
					setListRateDigit();
					// ��ʾ�����б�
					ManageHeaderVO[] vListVO = new ManageHeaderVO[m_vBillVOForRef
							.size()];
					if (m_vBillVOForRef.size() != 0)
						for (int i = 0; i < m_vBillVOForRef.size(); i++) {
							vListVO[i] = (ManageHeaderVO) ((ExtendManageVO) m_vBillVOForRef
									.elementAt(i)).getParentVO();
						}

					getCtBillListPanel().setHeaderValueVO(vListVO);

					// ִ�й�ʽ
					getCtBillListPanel().getHeadBillModel().execLoadFormula();
					getCtBillListPanel().getBodyBillModel("table")
							.execLoadFormula();

					getCtBillListPanel().getHeadTable()
							.setRowSelectionInterval(0, 0);
					getCtBillListPanel().setVisible(true);
					getCtBillCardPanel().setVisible(false);

				}
				// }

				m_bChangeFlag = false; // ��־��ͬ���ɱ��
				// ����Table����Ҫ������������
				for (int i = 1; i < 6; i++)
					m_bIsNeedReInit[i] = true;

			}

		} catch (Exception e) {
			// ��־�쳣
			nc.vo.scm.pub.SCMEnv.out(e);
			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"40081004", "UPT40081004-000020")/* @res "�������ݳ���" */
					+ e.getMessage());
		}
		// m_iBillState = OperationState.ADD ;
		setButtonStateForCof();
	}

	// added by lirr 2008-08-29
	protected ManageVO[] processAfterVoChange(ManageVO[] arrMangevos) {

		CircularlyAccessibleValueObject[] tableVOsAll = null;
		ArrayList alTableVOs = new ArrayList(); // ���б���vo
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

		// ��ͬ����
		tableVOsAll = new CircularlyAccessibleValueObject[alTableVOs.size()];
		alTableVOs.toArray(tableVOsAll);
		// ִ�д���༭��ʽ
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
		// �����������id ����
		if (null != hsInvbasids && hsInvbasids.size() > 0) {
			String[] sInvbasids = new String[hsInvbasids.size()];
			hsInvbasids.toArray(sInvbasids);

			// �õ�����λ
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

				// ��ȡ��ǰ�û�ID��Ӧ��ҵ��Ա
				if (null == arrMangevos[k].getParentVO().getAttributeValue(
						"personnelid")) {
					if (psnvo != null) {// �����Ӧ��ҵ��Ա
						if (null != psnvo.getPk_psndoc()) {
							// �õ�ǰ�û�ID��Ӧ��ҵ��Ա
							arrMangevos[k].getParentVO().setAttributeValue(
									"personnelid", psnvo.getPk_psndoc());
							arrMangevos[k].getParentVO().setAttributeValue(
									"pername", psnvo.getPk_psndoc());
						}
						if (null != psnvo.getPk_deptdoc()) {
							// �õ�ǰ�û�ID��Ӧ��ҵ��Ա��Ӧ�Ĳ���
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
				// ��ͬǩ����������Ĭ��ֵ
				arrMangevos[k].getParentVO().setAttributeValue("subscribedate",
						m_UFToday);

				arrMangevos[k].getParentVO()
						.setAttributeValue("audiname", null);
				arrMangevos[k].getParentVO().setAttributeValue("auditdate",
						null);

				// set��ӡ����Ϊ����
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
	 * ���ߣ�LIRR ���ܣ����μ��ش������ID�ĸ���λ ����: ������String[] saBaseId �������ID ���أ��� ���⣺��
	 * ���ڣ�(2008-8-29 11:39:21) �޸����� 2009-3-5����11:00:21 �޸��ˣ�lirr
	 * �޸�ԭ��ע�ͱ�־���������ۺ�ͬ�������۱��۵� ��Ҫ�õ�����Ĭ�ϵ�λ
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
			// modified by lirr 2008-11-26 ������ѡ�ɹ�Ĭ�ϵ�λ��ֻ�в����빺��ʱ�ã�
			oaAssUnit = (Object[]) nc.ui.scm.pub.CacheTool.getColumnValue(
					"bd_invbasdoc", "pk_invbasdoc", stypePk, saBaseId);

		} catch (Exception ee) {
			/** �����׳� */
			SCMEnv.out(ee);
		}
		int iLen = saBaseId.length;
		for (int i = 0; i < iLen; i++) {
			/*
			 * if (oaAssUnit == null || oaAssUnit[i] == null) {//
			 * oaAssUnit[i].toString(),��Ϊ // oaAssUnit[i] // ��ȽȽ2008-09-01 �� //
			 * oaAssUnit[i��Ϊ���򱨿��쳣 m_hmapCTDefaultAssUnit.put(saBaseId[i], ""); }
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
	 * ���ߣ�LIRR ���ܣ��õ�ĳ�˴������ID�ĸ���λ ����: ������String sBaseId �������ID ���أ��� ���⣺��
	 * ���ڣ�(2008-8-29 11:39:21) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
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
	 * ����ģ�����޸����� (��ͨ��)
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
	 * �˴����뷽��˵���� ��������:���� ���ߣ����� �������: ����ֵ: �쳣����: ����:(2003-7-3 9:34:29)
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
			// �޸�ʱ��:2009-09-11 �޸���: ��ΰƽ �޸�ԭ��: ����ͷ�ı�ʱִ��ִ�й�ʽ������VO״̬ΪUNCHANGGE
			if (null != getCtBillCardPanel().getHeadItem("transpmode")) {
			    // modified by lirr 2009-11-25����03:30:36  �޸ĺ�ͬ��ͷ�����䷽ʽ ֻ��δ�޸ĵ��в�Ӧ�ø�Ϊ�޸�״̬��
			    //�����������ɾ���и�Ϊ�޸�״̬ ���������޷�����
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

	// added by lirr ��ʽ���ɲ��񵥾� ���˺�ͬ " ct.ctflag =" + BillState.VALIDATE" and
	// (ct_b.natitaxsummny-ct_b.ntotalgpmny) > 0";
	private ManageVO getBodyVOForArap(ManageVO vo) {

		if (null == vo) {
			String sMessage = nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"4020pub", "UPT4020pub-000254")/*
													 * @res "��ѡ��һ����ͬ"
													 */;
			MessageDialog.showErrorDlg(this, null, sMessage);
			return null;
		}
		/*
		 * // ���ʱ��� try {
		 * 
		 * GenMethod.callICService("nc.bs.ct.pub.CTCheckDMO", "checkTimeStamp",
		 * new Class[] { vo.getClass() }, new Object[] { vo }); } catch
		 * (Exception e) { // ��־�쳣 nc.vo.scm.pub.SCMEnv.out(e); // ���淶�׳��쳣
		 * GenMethod.handleException(this, e.getMessage(), e); }
		 */
		// ��ͬ��
		ManageItemVO[] voItem = (ManageItemVO[]) vo.getChildrenVO();
		// ������з��������ĺ�ͬ��
		ArrayList<ManageItemVO> alItemVOs = new ArrayList<ManageItemVO>();
		// ��ֱ����������ˡ���Ч�򻺴���getAttributeValue("natitaxsummny")��getAttributeValue("ntotalgpmny")ΪUFDouble����
		// ���Ҽ�˰�ϼ�
		// UFDouble ufNatitaxsummny = new UFDouble(0);
		// ԭ�Ҽ�˰�ϼ� // added by lirr 2009-7-20 ����10:37:55 ԭ�򣺸�Ϊԭ�ҿ���
		UFDouble ufOritaxsummny = new UFDouble(0);
		// �ۼƸ�����
		UFDouble ufNtotalgpmny = new UFDouble(0);
		// �������ݿ��в�ѯ�Ժ��ٷ��뻺��getAttributeValue("natitaxsummny")��getAttributeValue("ntotalgpmny")ΪBigDecimal����
		// ���Ҽ�˰�ϼ�
		// java.math.BigDecimal bNatitaxsummny = new BigDecimal(0);
		// ԭ�Ҽ�˰�ϼ� // added by lirr 2009-7-20 ����10:37:55 ԭ�򣺸�Ϊԭ�ҿ���
		java.math.BigDecimal bOritaxsummny = new BigDecimal(0);
		// �ۼƸ�����
		java.math.BigDecimal bNtotalgpmny = new BigDecimal(0);
		// ����Ҫ����񵥾ݽ���VO���յĺ�ͬVO�������������ص�VO
		ManageVO finalVO = new ManageVO();
		// ��ͬ״̬Ϊ��Ч
		if (BillState.VALIDATE == Integer.parseInt(vo.getParentVO()
				.getAttributeValue("ctflag").toString())) {
			for (int i = 0; i < voItem.length; i++) {
			    ufNtotalgpmny = UFDouble.ZERO_DBL;
			    ufOritaxsummny = UFDouble.ZERO_DBL;
				/*
				 * // ��ñ��Ҽ�˰�ϼ� if (null !=
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
				// ���ԭ�Ҽ�˰�ϼ� modified by lirr 2009-7-20 ����10:37:55 ԭ�򣺸�Ϊԭ�ҿ���
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
				// �ۼƸ�����
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
				// (ct_b.natitaxsummny-ct_b.ntotalgpmny) > 0,�����Ҽ�˰�ϼơ��ۼƸ��������0
				if (ufOritaxsummny.compareTo(ufNtotalgpmny) > 0) {
					alItemVOs.add(voItem[i]);

				}
			}
		}
		// ��ͬ״̬Ϊ����״̬����Ч���⣩
		else {
			return null;
		}
		// �з��������ĺ�ͬ��
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
	 * ���ݱ���˵���������.
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
	 * ��������:�����˵��Ҽ�����Ȩ�޿���
	 * <p>
	 * <b>�����ʷ��</b>
	 * <p>
	 * <hr>
	 * <p>
	 * �޸����� 2008.9.23
	 * <p>
	 * �޸��� lirr
	 * <p>
	 * �汾 5.5
	 * <p>
	 * ˵����
	 * <ul>
	 * <li>�趨������Ҽ��˵�����Ȩ��
	 * </ul>
	 * 
	 * �޸����� 2009-8-13����06:49:19 �޸��ˣ�lirr �޸�ԭ��ע�ͱ�־����������¿�ָ��
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
		 * ///System.out.println(sButtonName); //����getButtonTree()�õ�button ��
		 * �����ú������� ��qinchao 2009-5-13 if(sButtonName.equals("Add a
		 * row")||sButtonName.equals("����")){ sButtonName = "����"; }else
		 * if(sButtonName.equals("Delete a row")||sButtonName.equals("�h��")){
		 * sButtonName = "ɾ��"; }else if(sButtonName.equals("Insert a
		 * row")||sButtonName.equals("������")){ sButtonName = "������"; }else
		 * if(sButtonName.equals("Copy a row")||sButtonName.equals("�}�u��")){
		 * sButtonName = "������"; }else if(sButtonName.equals("Paste a
		 * row")||sButtonName.equals("ճ�N��")){ sButtonName = "ճ����"; }else
		 * if(sButtonName.equals("Paste a row to the table
		 * tail")||sButtonName.equals("ճ�N�е���β")){ sButtonName = "ճ���е���β"; }else
		 * if(sButtonName.equals("Start Card
		 * Edit")||sButtonName.equals("��Ƭ��݋")){ sButtonName = "��Ƭ�༭"; }else
		 * if(sButtonName.equals("Resort rowno")||sButtonName.equals("������̖")){
		 * sButtonName = "�����к�"; } // modified by lirr 2009-02-23 �޸�ԭ�򣺿ͻ���
		 * ����ϰ�ťû��Ȩ�޵Ļ� ������ť��Ȩ��Ϊtrue if (null != getButtonTree().getButton(
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

		if (null != getButtonTree().getButton("����"))
			if (!getButtonTree().getButton("����").getParent().isPower()
					|| (getButtonTree().getButton("����").getParent().isPower() && !(getButtonTree()
							.getButton("����").isPower()))) {
				getCtBillCardPanel().getAddLineMenuItem().setEnabled(false);
			}
		if (null != getButtonTree().getButton("ɾ��"))
			if (!getButtonTree().getButton("ɾ��").getParent().isPower()
					|| (getButtonTree().getButton("ɾ��").getParent().isPower() && !(getButtonTree()
							.getButton("ɾ��").isPower()))) {
				getCtBillCardPanel().getDelLineMenuItem().setEnabled(false);
			}
		if (null != getButtonTree().getButton("������"))
			if (!getButtonTree().getButton("������").getParent().isPower()
					|| (getButtonTree().getButton("������").getParent().isPower() && !(getButtonTree()
							.getButton("������").isPower()))) {
				getCtBillCardPanel().getInsertLineMenuItem().setEnabled(false);
			}
		if (null != getButtonTree().getButton("������"))
			if (!getButtonTree().getButton("������").getParent().isPower()
					|| (getButtonTree().getButton("������").getParent().isPower() && !(getButtonTree()
							.getButton("������").isPower()))) {
				getCtBillCardPanel().getCopyLineMenuItem().setEnabled(false);
			}
		if (null != getButtonTree().getButton("ճ����"))
			if (!getButtonTree().getButton("ճ����").getParent().isPower()
					|| (getButtonTree().getButton("ճ����").getParent().isPower() && !(getButtonTree()
							.getButton("ճ����").isPower()))) {
				getCtBillCardPanel().getPasteLineMenuItem().setEnabled(false);
			}
		if (null != getButtonTree().getButton("ճ���е���β"))
			if (!getButtonTree().getButton("ճ���е���β").getParent().isPower()
					|| (getButtonTree().getButton("ճ���е���β").getParent()
							.isPower() && !(getButtonTree().getButton("ճ���е���β")
							.isPower()))) {
				getCtBillCardPanel().getPasteLineToTailMenuItem().setEnabled(
						false);
			}
		if (null != getButtonTree().getButton("��Ƭ�༭"))
			if (!getButtonTree().getButton("��Ƭ�༭").getParent().isPower()
					|| (getButtonTree().getButton("��Ƭ�༭").getParent().isPower() && !(getButtonTree()
							.getButton("��Ƭ�༭").isPower()))) {
				miBoCardEdit.setEnabled(false);
			}
		if (null != getButtonTree().getButton("�����к�"))
			if (!getButtonTree().getButton("�����к�").getParent().isPower()
					|| (getButtonTree().getButton("�����к�").getParent().isPower() && !(getButtonTree()
							.getButton("�����к�").isPower()))) {
				miaddNewRowNo.setEnabled(false);
			}
	}

	protected void execFormularAfterQueryWithoutCache(ManageVO[] arrMangevos) {
		// ������б�ͷvo
		CircularlyAccessibleValueObject[] headVosAll = new CircularlyAccessibleValueObject[arrMangevos.length];
		CircularlyAccessibleValueObject[] tableVOsAll = null;
		CircularlyAccessibleValueObject[] termVOsAll = null;

		ArrayList alTableVOs = new ArrayList(); // ���б���vo
		ArrayList alTermVOs = new ArrayList(); // ��������vo

		int index = 0;

		ExtendManageVO[] arrExtendVO = new ExtendManageVO[arrMangevos.length];
		for (int k = 0; k < arrMangevos.length; k++) {
			arrExtendVO[k] = new ExtendManageVO();

			arrExtendVO[k].setParentVO(arrMangevos[k].getParentVO());
			headVosAll[k] = arrMangevos[k].getParentVO();

			// modified by liuzy 2008-04-17 V5.03��ѯ���죬ֻ���ص�һ����ͬ�ı���������ҳǩ
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
		// ִ�й�ʽ
		getCtBillListPanel().getBodyBillModel().execFormulasWithVO(headVosAll,
				getHeadTailFormulas());

		// ��ͬ����
		tableVOsAll = new CircularlyAccessibleValueObject[alTableVOs.size()];
		alTableVOs.toArray(tableVOsAll);

		nc.ui.scm.pub.FreeVOParse freeVOParse = new nc.ui.scm.pub.FreeVOParse();
		freeVOParse.setFreeVO(tableVOsAll, null, "invid", false);

		// ��ͬ����ִ�й�ʽ
		getCtBillListPanel().getBodyBillModel().execFormulasWithVO(tableVOsAll,
				getTableFormulas());

		if (alTermVOs.size() > 0) {
			termVOsAll = new CircularlyAccessibleValueObject[alTermVOs.size()];
			alTermVOs.toArray(termVOsAll);
			getCtBillListPanel().getBodyBillModel().execFormulasWithVO(
					termVOsAll, getTermFormulas());
		}

		// ��ͬ�����ӱ���ִ�й�ʽ
		// ԭ�򣺺�ͬ�����ӱ��е�pk_ct_exp�Ǻ�ͬ���ö����е�pk_ct_expset������֮�����ϵloadformula���ܸ�����������������ϵ��
		// shaobing on Jul 4, 2005
		/** ***��ѯ��ʽִ�н���**** */
	}

	/*
	 * added by lirr ��ò��񾫶ȵ��ۼƸ����ܶ�ں�ͬ���ʱҪȡ��ֵ�롰�ۼƸ����ܶ�Ƚϣ�����ֵ<"�ۼƸ����ܶ�" ���ñ����
	 * ��ͬ����ʱ�����ֶα��浽���ݿ��� since 56 ��Ϊԭ�ҿ���
	 * ��ò��񾫶ȵ��ۼ�ԭ�Ҹ����ܶ�ں�ͬ���ʱҪȡ��ֵ�롰�ۼƱ��Ҹ����ܶ�Ƚϣ�����ֵ<"�ۼ�ԭ�Ҹ����ܶ�" ���ñ����
	 */

	private UFDouble getAraptotalgpamount(ManageVO managevo) {
		UFDouble ufnarapamount = new UFDouble(0);
		if (null != managevo.getChildrenVO()
				&& managevo.getChildrenVO().length > 0) {
			for (int i = 0; i < managevo.getChildrenVO().length; i++) {
				// modified by lirr 2008-12-17 ���ʱvo���¼�����е��У�ͳ��ʱӦ��ɾ������ȥ��
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
	 * �����ˣ�lirr �������ڣ�2008-10-30����09:47:05 ����ԭ�����¼����ͬ�е��ۺͽ�����ԭ�ҡ����ҡ����ҡ�
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
				RelationsCal.CURRTYPEPk,// ���������Ҽ۸񡢽��
				SCMRelationsCal.NET_PRICE_LOCAL,
				SCMRelationsCal.NET_TAXPRICE_LOCAL, SCMRelationsCal.TAX_LOCAL,
				SCMRelationsCal.MONEY_LOCAL, SCMRelationsCal.SUMMNY_LOCAL,
				SCMRelationsCal.BILLDATE, SCMRelationsCal.PK_CORP,
				SCMRelationsCal.EXCHANGE_O_TO_BRATE,
				// added by lirr 2008-12-25
				SCMRelationsCal.CONVERT_RATE, SCMRelationsCal.IS_FIXED_CONVERT,
				SCMRelationsCal.NUM_ASSIST };

		// ADDED by lirr 2008-12-25 �ж��Ƿ�̶�������
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

		String[] saKeys = { "Ӧ˰���", "amount", "taxration", "oriprice",
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
	 * �˴����뷽��˵���� ��������: �������: ����ֵ: �쳣����: ����:
	 */

	/**
	 * return int[] 0--��˰��˰���� 1--���ۿۻ��ǵ����� 2--�Ƿ������Һ���
	 * 
	 * Integer Prior --- ǿ�ƺ�˰����˰���� ȡ�õ��۽���㷨����Ĳ���
	 */
	public int[] getCalculatePara(Integer Prior) {
		// ��˰���Ȼ���:Ĭ����˰����
		int m_iPrior_Price_TaxPrice = RelationsCalVO.PRICE_PRIOR_TO_TAXPRICE;
		if (sContractPriceRule.equals("��˰����")) {
			m_iPrior_Price_TaxPrice = RelationsCalVO.PRICE_PRIOR_TO_TAXPRICE;
		} else if (sContractPriceRule.equals("��˰����")) {
			m_iPrior_Price_TaxPrice = RelationsCalVO.TAXPRICE_PRIOR_TO_PRICE;
		}

		if (Prior != null)// ǿ�����Ȼ���
			m_iPrior_Price_TaxPrice = Prior.intValue();

		int[] iaPrior = new int[] { m_iPrior_Price_TaxPrice,
				RelationsCalVO.YES_LOCAL_FRAC };

		return iaPrior;
	}

	/**
	 * �����Դ���ݵı�ͷts�ͱ���ts ���ߣ�lirr ���ڣ�2008-12-23����04:38:13 �޸�����
	 * 2008-12-23����04:38:13 �޸��ˣ�lirr �޸�ԭ��ע�ͱ�־��
	 * 
	 * @param pk
	 *            ��ͬ��pk
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
	 * ���ݱ��ֻ���۱����ʾ��� ���ߣ�lirr ���ڣ�2008-12-24����03:45:43 �޸����� 2008-12-24����03:45:43
	 * �޸��ˣ�lirr �޸�ԭ��ע�ͱ�־��
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
	 * �ո��ť��ˢ�½��� ���ߣ�lirr ���ڣ�2008-12-26����11:00:07 �޸����� 2008-12-26����11:00:07
	 * �޸��ˣ�lirr �޸�ԭ��ע�ͱ�־��
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
			// ��־�쳣
			nc.vo.scm.pub.SCMEnv.out(e);
			// ���淶�׳��쳣
			GenMethod.handleException(this, e.getMessage(), e);
		}
	}

	/**
	 * �����ͨ��ҵ����־VO ���� ���ߣ�lirr ���ڣ�2009-4-21����10:15:51 �޸����� 2009-4-21����10:15:51
	 * �޸��ˣ�lirr �޸�ԭ��ע�ͱ�־��
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
	 * ���ñ�ͷ��ͷ����ԭ���ۼ���/�����ܶ� �ں�ͬ����ǰ��Ҫ���� ���ߣ�lirr ���ڣ�2009-7-17����02:29:43 �޸�����
	 * 2009-7-17����02:29:43 �޸��ˣ�lirr �޸�ԭ��ע�ͱ�־��
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
		// ��ͷ�� ��ԭ���ۼ���/�����ܶ�
		newManageVO.getParentVO().setAttributeValue(
				"naraptotalgpamount",
				new UFDouble(ufnarapamount.toDouble(),
						((Integer) m_hCurrDigitcw.get(newManageVO.getParentVO()
								.getCurrid())).intValue()));

	}

	/**
	 * �˴����뷽��˵�� ���ߣ�lirr ���ڣ�2009-8-17����11:24:12 �޸����� 2009-8-17����11:24:12 �޸��ˣ�lirr
	 * �޸�ԭ��ע�ͱ�־��
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

	// �޸�ʱ��2009-09-10 �޸���:wuweiping �޸�ԭ��: ����Ӧ�̸ı��ʱ������Ƿ�һ�£����һ���۱����ʲ��ɱ༭��������Ա༭
	public void setM_bRateEnable(String currID) {
		if (currID.equals(m_sLocalCurrID)) {
			m_bRateEnable = false;
		} else
			m_bRateEnable = true;
	}

	protected ManageVO getAuditVO(boolean isshowerr, ManageVO mVO) {

		// ����Ա��־
		nc.vo.sm.log.OperatelogVO log = getNormalOperateLog();

		ManageVO tempMVO = new ManageVO();
		if (mVO == null)
			mVO = getCurVO();// �õ���ǰѡ�еĵ���VO
		// modified by liuzy 2007-12-25 �򿪺�ͬ�͵����ʱ���մ�
		if (null == mVO) {

			if (!isshowerr)
				return null;
			showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"4020nodes", "UPP4020nodes-000120")/*
														 * @res "û����Ҫ��˵ĺ�ͬ"
														 */);
			return null;
		}
		tempMVO = mVO.clone(mVO);
		int ctState = Integer.parseInt(tempMVO.getParentVO().getAttributeValue(
				"ctflag").toString());// ��ú�ͬ״̬

		// �Դ��ڲ�ͬ״̬�ĺ�ͬ���в�ͬ�Ĵ���
		if (ctState == BillState.FREE || ctState == BillState.CHECKGOING) {
			if (isshowerr) {
				if (MessageDialog.showYesNoDlg(this, null, nc.ui.ml.NCLangRes
						.getInstance().getStrByID("4020nodes",
								"UPP4020nodes-000051")/*
														 * @res "���Ƿ�ȷ��Ҫ��˸ú�ͬ��"
														 */) != MessageDialog.ID_YES)
					return null;
			}
			tempMVO.getParentVO().setAttributeValue("ctflag",
					new Integer(BillState.AUDIT));// ���ú�ͬ״̬Ϊ���״̬

			tempMVO.getParentVO().setAttributeValue("audiid", m_sPkUser);// ���������Ϊ��ǰ�û�
			tempMVO.getParentVO().setAttributeValue("auditdate",
					getClientEnvironment().getDate());// �����������Ϊ��ǰ����

			if (ctState == BillState.FREE)
				tempMVO.setOldBillStatus(BillState.FREE);// ����ԭ����״̬Ϊ����״̬
			else if (ctState == BillState.CHECKGOING)
				tempMVO.setOldBillStatus(BillState.CHECKGOING);// ����ԭ����״̬Ϊ�������״̬
			tempMVO.setBillStatus(BillState.AUDIT);// ���õ���״̬Ϊ���״̬

			// added by lirr 2009-04-21 ҵ����־
			tempMVO.setOperatelogVO(log);

			return tempMVO;
		}
		return null;
	}

	protected ManageVO getUnAuditVO(boolean isshowerr, ManageVO mVO) {
		// ����Ա��־
		nc.vo.sm.log.OperatelogVO log = getNormalOperateLog();
		// �޸���: wuwp �޸�ʱ��:2009-10-14 ����03:31:04 �޸�ԭ��:��ͬ����Ч���������á�
		if (null == mVO)
			mVO = getCurVO();// �õ���ǰѡ�еĵ���VO;
		ManageVO tempMVO = new ManageVO();
		tempMVO = mVO.clone(mVO);
		int ctState = Integer.parseInt(tempMVO.getParentVO().getAttributeValue(
				"ctflag").toString());

		// �Դ��ڲ�ͬ״̬�ĺ�ͬ���в�ͬ�Ĵ���
		// if (ctState == BillState.AUDIT ) { // ���״̬
		if (ctState == BillState.AUDIT || ctState == BillState.CHECKGOING) { // ���״̬

			if (isshowerr) {
				if (MessageDialog.showYesNoDlg(this, null, nc.ui.ml.NCLangRes
						.getInstance().getStrByID("4020pub",
								"UPP4020pub-000158")/*
													 * @res "�Ƿ�ȷ��Ҫ����ú�ͬ��"
													 */) != MessageDialog.ID_YES)
					return null;
			}

			tempMVO.getParentVO().setAttributeValue("ctflag",
					new Integer(BillState.AUDIT));

			// ��ǰ����Ա
			tempMVO.getParentVO().setAttributeValue("coperatoridnow",
					getOperator());

			tempMVO.getParentVO().setAttributeValue("auditdate",
					getClientEnvironment().getDate());
			if (ctState == BillState.AUDIT)
				tempMVO.setOldBillStatus(BillState.AUDIT);

			tempMVO.setBillStatus(BillState.FREE);
			// added by lirr 2009-04-21 ҵ����־
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

		// �Դ��ڲ�ͬ״̬�ĺ�ͬ���в�ͬ�Ĵ���
		if (ctState == BillState.AUDIT) { // ���״̬
			if (isshowerr) {
				String sMessage = nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"4020pub", "UPP4020pub-000090")/* @res "�Ƿ�ȷ��Ҫʹ�ú�ͬ��Ч��" */;

				if (MessageDialog.showYesNoDlg(this, null, sMessage) != MessageDialog.ID_YES)
					return null;
			}
			tempMVO.getParentVO().setAttributeValue("ctflag",
					new Integer(BillState.VALIDATE));

			// ��ǰ����Ա
			tempMVO.getParentVO().setAttributeValue("coperatoridnow",
					getOperator());

			tempMVO.getParentVO().setAttributeValue("actualvalidate",
					getClientEnvironment().getDate());

			tempMVO.setOldBillStatus(BillState.AUDIT);
			tempMVO.setBillStatus(BillState.VALIDATE);

			UFDate UFValDate = new UFDate(mVO.getParentVO().getAttributeValue(
					"valdate").toString());// �ƻ���ͬ��Чʱ��

			// if (UFValDate.compareTo(ClientEnvironment.getInstance()
			// .getDate()) != 0) {
			// return null;
			// }
			m_sExecFlag = CTExecFlow.VALIDATE; // "ʵ����Ч";
			// added by lirr 2009-9-14����02:45:29 ����ִ�й��̵Ķ�����ԭ��
			CTTool.setExecReason(tempMVO, this);
			return tempMVO;
		}
		return null;
	}

	protected ManageVO getSendAuditVO(boolean isshowerr, ManageVO mVO) {

		if (mVO == null)
			mVO = getCurBillVO();
		// added by lirr 2008-10-28 ������ͬ״̬��Ϊ�������С�
		ManageVO tempMVO = new ManageVO();
		tempMVO = mVO.clone(mVO);
		String sId = ((ManageHeaderVO) tempMVO.getParentVO()).getPk_ct_manage();
		// ��½�˲����Ƶ��˲����� added by lirr 2009-02-10
		if (!((ManageHeaderVO) tempMVO.getParentVO()).getOperid().equals(
				m_sPkUser)) {
			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"SCMCOMMON", "UPPSCMCommon-000023")/*
														 * @res "��������"
														 */);
			return null;
		}
		// �ж��Ƿ�������Ƶ��� added by lirr 2009-02-10
		if (m_isSaveInitOper.booleanValue() == false) {
			((ManageHeaderVO) tempMVO.getParentVO()).setOperid(m_sPkUser);
		}
		tempMVO.getParentVO().setAttributeValue("audiid", null);
		tempMVO.getParentVO().setAttributeValue("auditdate", null);
		tempMVO.getParentVO().setAttributeValue("ctflag",
				new Integer(BillState.CHECKGOING));
		tempMVO.getParentVO()
				.setAttributeValue("coperatoridnow", getOperator());
		// added by lirr 2009-7-9����04:27:52 ���õ������� ����ʱʹ��
		tempMVO.setBillType(m_sBillType);
		tempMVO.setOldBillStatus(BillState.FREE);
		tempMVO.setBillStatus(BillState.CHECKGOING);

		return tempMVO;
	}

	public ManageVO[] getBillVOForAction(int[] iaSeleRows, String sAction) {
		if (iaSeleRows == null || iaSeleRows.length <= 0)
			return null;
		// ��ͬ����VO
		ManageVO tempVO = null;
		// ��ǰ�����±�
		int iIdtemp = m_iId;
		// ִ�й���
		String stemp = null;
		// ��������
		int errcount = 0;

		ArrayList<ManageVO> listbillvo = new ArrayList<ManageVO>();
		for (int i = 0, j = iaSeleRows.length; i < j; i++) {
			stemp = null;
			tempVO = null;
			m_iId = iaSeleRows[i] + 1;
			if ("����".equals(sAction))
				tempVO = getAuditVO(false, null);
			else if ("����".equals(sAction))
				tempVO = getUnAuditVO(false, null);
			else if ("��Ч".equals(sAction))
				tempVO = getValidateVO(false, null);
			else if ("����".equals(sAction))
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
	 * �鿴������״̬
	 * 
	 * @author dgq
	 * @param null
	 * @since 5.01 2006.12.27 �޸����� 2009-9-21����09:47:44 �޸��ˣ�lirr �޸�ԭ��
	 *        ���ֺ�ͬ�����ڴ˴���ע�ͱ�־��
	 */
	private void onFlowStatus() {
		if (m_bIsCard) {// ��Ƭ״̬
			String pk = getCtBillCardPanel().getHeadItem("pk_ct_manage")
					.getValue();
			if (!pk.equals("") && pk.length() > 0) {
				try {
					FlowStateDlg approvestatedlg = new FlowStateDlg(this,
							m_sBillType, pk);
					approvestatedlg.showModal();
				} catch (Exception e) {

				}
			} else {// �б�״̬
				showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"SCMCOMMON", "UPPSCMCommon-000199")/* @res "��ѡ�񵥾�" */);
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
																			 * "��ѡ�񵥾�"
																			 */);
				}
			}
		}
	}
	
/**
 * �˴����뷽��˵�� ճ����ʱ���ִ�е�һЩ��Ϣ
 * ���ߣ�lirr
 * ���ڣ�2009-11-7����03:48:16
 * �޸����� 2009-11-7����03:48:16 �޸��ˣ�lirr �޸�ԭ��ע�ͱ�־��
	@param nowRow ��ǰ�У�
	@param rowLen Ҫ������г���
 */
private void clearExecInfo (int nowRow, int rowLen){
	for (int i = (nowRow - rowLen); i < nowRow; i++) {
		getCtBillCardPanel().setBodyValueAt(null, i, "ntotalshgpmny"); // �ۼ�Ӧ��/������
		getCtBillCardPanel().setBodyValueAt(null, i, "ntotalgpmny"); // �ۼ��ո�����
		getCtBillCardPanel().setBodyValueAt(null, i, "csourcebillhid");// ��Դ��������ID
		getCtBillCardPanel().setBodyValueAt(null, i, "csourcebillbid");// ��Դ�����ӱ���ID
		getCtBillCardPanel().setBodyValueAt(null, i, "csourcebilltype");// ��Դ��������
		getCtBillCardPanel().setBodyValueAt(null, i, "csourcebillcode");// ��Դ���ݺ�
		getCtBillCardPanel().setBodyValueAt(null, i, "csourcerowno");// ��Դ�����к�
		// added by lirr 2009-11-30����01:36:31 ��Դ�������ӱ���ID
		getCtBillCardPanel().setBodyValueAt(null, i, "csourcebillbbid");// ��Դ�����к�
		
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
 * ����������������Ҫ�����������Ĺ��ܡ�л��Ҫ�� ��������ʱ��Դ��ϢҪ��� ������ʱ56��ʼ�������
 * <p>
 * <b>examples:</b>
 * <p>
 * ʹ��ʾ��
 * <p>
 * <b>����˵��</b>
 * @param item
 * <p>
 * @author lirr
 * @time 2009-11-30 ����01:35:51
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
 * ����������������Ҫ�����������Ĺ��ܡ�����������ĺ�ͬʱ��ͬ�����TSӦ���
 * <p>
 * <b>examples:</b>
 * <p>
 * ʹ��ʾ��
 * <p>
 * <b>����˵��</b>
 * <p>
 * @author lirr
 * @time 2009-12-2 ����10:11:53
 */
private void clearTermsTs(){
    for (int i = 0; i <m_copyedTermVOs.length; i++) {
      m_copyedTermVOs[i].setAttributeValue("ts", null);
     }
   }
}