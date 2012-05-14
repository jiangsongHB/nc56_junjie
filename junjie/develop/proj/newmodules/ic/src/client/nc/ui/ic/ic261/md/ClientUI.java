package nc.ui.ic.ic261.md;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.JFileChooser;
import javax.swing.table.DefaultTableCellRenderer;

import nc.bs.dao.DAOException;
import nc.bs.framework.common.NCLocator;
import nc.itf.ic.md.IMDTools;
import nc.itf.uap.IUAPQueryBS;
import nc.itf.uap.IVOPersistence;
import nc.jdbc.framework.processor.ArrayListProcessor;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.jdbc.framework.processor.BeanProcessor;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.ui.ic.ic261.CheckTypeChooser;
import nc.ui.ic.ic261.DataGetSource;
import nc.ui.ic.ic261.QueryDlgHelpForSpecExt;
import nc.ui.ic.ic261.SpecialHHelper;
import nc.ui.ic.pub.ICCommonBusi;
import nc.ui.ic.pub.PageCtrlBtn;
import nc.ui.ic.pub.bc.BarCodeDlg;
import nc.ui.ic.pub.bc.BarCodeViewDlg;
import nc.ui.ic.pub.bill.BarcodeCtrl;
import nc.ui.ic.pub.bill.GeneralBillUICtl;
import nc.ui.ic.pub.bill.InvAttrCellRenderer;
import nc.ui.ic.pub.bill.InvoInfoBYFormula;
import nc.ui.ic.pub.bill.MultiCardMode;
import nc.ui.ic.pub.bill.QueryDlgHelpForSpec;
import nc.ui.ic.pub.bill.SpecialBillBaseUI;
import nc.ui.ic.pub.bill.SpecialBillHelper;
import nc.ui.ic.pub.bill.SpecialBillUICtl;
import nc.ui.ic.pub.bill.query.QueryConditionDlgForBill;
import nc.ui.ic.pub.device.DevInputCtrl;
import nc.ui.ic.pub.locatorref.LocatorRefPane;
import nc.ui.ic.pub.tools.GenMethod;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UILabel;
import nc.ui.pub.beans.UIMenuItem;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.bill.BillCellEditor;
import nc.ui.pub.bill.BillData;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillModel;
import nc.ui.pub.bill.BillUIUtil;
import nc.ui.scm.ic.exp.GeneralMethod;
import nc.ui.scm.pub.panel.SetColor;
import nc.ui.trade.businessaction.IPFACTION;
import nc.vo.ic.ic261.CheckMode;
import nc.vo.ic.md.MdcrkVO;
import nc.vo.ic.pub.BillRowType;
import nc.vo.ic.pub.BillTypeConst;
import nc.vo.ic.pub.IBillItemBarcodeVO;
import nc.vo.ic.pub.ICConst;
import nc.vo.ic.pub.SmartVOUtilExt;
import nc.vo.ic.pub.barcodeparse.BarCodeGroupVO;
import nc.vo.ic.pub.barcodeparse.BarCodeParseVO;
import nc.vo.ic.pub.barcodeparse.BarcodeparseCtrl;
import nc.vo.ic.pub.barcoderule.BarCodeRuleCache;
import nc.vo.ic.pub.barcoderule.BarcoderuleHeaderVO;
import nc.vo.ic.pub.barcoderule.BarcoderuleVO;
import nc.vo.ic.pub.bc.BarCodeVO;
import nc.vo.ic.pub.bc.BarCodeViewVO;
import nc.vo.ic.pub.bc.SpecailBarCode1VO;
import nc.vo.ic.pub.bc.SpecailBarCodeVO;
import nc.vo.ic.pub.bill.GeneralBillHeaderVO;
import nc.vo.ic.pub.bill.GeneralBillItemVO;
import nc.vo.ic.pub.bill.GeneralBillVO;
import nc.vo.ic.pub.bill.IICParaConst;
import nc.vo.ic.pub.bill.IItemKey;
import nc.vo.ic.pub.bill.QryConditionVO;
import nc.vo.ic.pub.bill.QryInfoConst;
import nc.vo.ic.pub.bill.SpecialBillHeaderVO;
import nc.vo.ic.pub.bill.SpecialBillItemVO;
import nc.vo.ic.pub.bill.SpecialBillVO;
import nc.vo.ic.pub.bill.Timer;
import nc.vo.ic.pub.check.VOCheck;
import nc.vo.ic.pub.locator.LocatorVO;
import nc.vo.ic.pub.smallbill.SMSpecialBillVO;
import nc.vo.ic.pub.tools.KeyObject;
import nc.vo.ic.pub.tools.StringKeyJudge;
import nc.vo.ic.xcl.MdxclBVO;
import nc.vo.ic.xcl.MdxclVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.NullFieldException;
import nc.vo.pub.VOStatus;
import nc.vo.pub.ValidationException;
import nc.vo.pub.bill.BillTempletVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.query.ConditionVO;
import nc.vo.scm.constant.ic.BillMode;
import nc.vo.scm.constant.ic.InOutFlag;
import nc.vo.scm.ic.bill.FreeVO;
import nc.vo.scm.ic.bill.InvVO;
import nc.vo.scm.ic.bill.WhVO;
import nc.vo.scm.ic.exp.HeaderRenderer;
import nc.vo.scm.ic.exp.ICDateException;
import nc.vo.scm.ic.exp.ICHeaderNullFieldException;
import nc.vo.scm.ic.exp.ICLocatorException;
import nc.vo.scm.ic.exp.ICNullFieldException;
import nc.vo.scm.ic.exp.ICNumException;
import nc.vo.scm.ic.exp.ICPriceException;
import nc.vo.scm.ic.exp.ICRepeatException;
import nc.vo.scm.ic.exp.ICSNException;
import nc.vo.scm.pub.BillRowNoVO;
import nc.vo.scm.pub.SCMEnv;
import nc.vo.scm.pub.ScmDataSet;
import nc.vo.scm.pub.smart.SmartFieldMeta;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

/*
 * 创建者：仲瑞庆 创建日期：2001-04-20 功能：盘点单界面 修改日期，修改人，修改原因，注释标志：
 */
public class ClientUI extends SpecialBillBaseUI implements
		nc.ui.pf.query.ICheckRetVO,nc.ui.ic.pub.barcode.BarCodeInputListenerNew {

	// 账面取数
	private ButtonObject m_boGetAccNumber = null;
	
	private ButtonObject m_boGetBCAccNumber = null;

	private UFDouble UFD_ZERO = new UFDouble(0);

	// 盘点选择
	private ButtonObject m_boCheckChoose = null;

	// 调整
	private ButtonObject m_boAdjustGoods = null;

	// 取消调整
	private ButtonObject m_boCancelAdjust = null;

	public int m_iBqrybalrec = 0;// 记录查询帐实相符查询框中的选择

	private DataGetSource ivjDataGetSourceDlg = null;

	private ButtonObject m_addMng = null;

	// 调整
	private ButtonObject m_adjustMng = null;

	// 保存后状态,区别于帐面取数状态
	private int iSaveFlag = 0;

	// 辅助功能
	private ButtonObject m_assistMng = null;

	// 下拉菜单
	// 审批
	private ButtonObject m_auditMng = null;

	boolean m_bBillInit = false; // 单据是否是初始状态 add by hanwei 2003-12-30

	// 当前状态是否实盘点录入
	private boolean m_bcheckInput = false;

	private boolean m_bIsByFormula = true;

	// 是否有导入操作
	private boolean m_bIsImportData = false;

	// 审批状态查询
	private ButtonObject m_boAuditState = null;

	// 差异计算
	private ButtonObject m_boCalculate = null;

	// 盘点机支持
	private ButtonObject m_boCheckInv = null;

	// 实盘录入
	private ButtonObject m_boCheckNum = null;

	// 自动填写实数量
	private ButtonObject m_boFillNum = null;

	private nc.ui.ic.pub.device.DevInputCtrl m_dictrl = null;

	private CheckTypeChooser m_dlgCtc = null; // 存货选择

	private ButtonObject m_fillMng = null;

	// 导入实盘条码
	private ButtonObject m_boCountBCImport = null;

	// 结存条码明细
	private ButtonObject m_boViewOnhandBC = null;

	// 实盘条码明细
	private ButtonObject m_boViewCountBC = null;

	// 条码盘点差异明细
	private ButtonObject m_boViewDiffBC = null;

	// 条形码
	private ButtonObject m_boBarCode = null;

	// 货位参照
	private LocatorRefPane m_refLocator = null;
	
	private ButtonObject m_boCommit = null;

	private Timer m_timer = new Timer();
	
	private nc.ui.ic.pub.barcode.UIBarCodeTextFieldNew m_utfBarCode = null;
	
	protected UIPanel m_pnlBarCode;
	
	private ToftLayoutManager m_layoutManager = new ToftLayoutManager(this);// 布局管理器
	
	
	private ArrayList<String> loadBarCodeBids = new ArrayList<String>();
	private ArrayList<String> loadBarCode1Bids = new ArrayList<String>();

	/**
	 * ClientUI 构造子注解。
	 */
	public ClientUI() {
		super();
		initialize();
	}
	
	public class ToftLayoutManager {

		private nc.ui.ic.ic261.md.ClientUI m_client;
	  


		public ToftLayoutManager(nc.ui.ic.ic261.md.ClientUI toft) {
			super();
			m_client = toft;
			init();
		}

		/**
		 * 初始化时，不添加各个控件，而是动态添加，避免界面刷新的复杂度
		 */
		private void init() {
			//初始化布局
			m_client.setLayout(new java.awt.CardLayout());
		
		}



		public void show(boolean isCard) {

			if (isCard) {
				
				showOnlyCard();
			} 

			else  {
				m_client.add(m_client.getBillListPanel(), "list");
				((CardLayout) m_client.getLayout()).show(m_client,
						"list");
			}
		}


		private void showOnlyCard() {
			m_client.removeAll();
			m_client.add(getPnlCardAndBarCode(), MultiCardMode.CARD_PURE);
			((CardLayout) m_client.getLayout()).show(m_client,
					MultiCardMode.CARD_PURE);
		}

		
		//纯Card界面：CardPanel+条码panel
		private UIPanel m_pnlPure = null;
		
		private UIPanel getPnlCardAndBarCode() {
			if (m_pnlPure == null){
				m_pnlPure = new UIPanel();
				m_pnlPure.setLayout(new BorderLayout());
				m_pnlPure.add(m_client.getPnlBc(), BorderLayout.SOUTH);
				m_pnlPure.add(m_client.getBillCardPanel(), BorderLayout.CENTER);
			}
			else{
				m_pnlPure.removeAll();
				m_pnlPure.add(m_client.getPnlBc(), BorderLayout.SOUTH);
				m_pnlPure.add(m_client.getBillCardPanel(), BorderLayout.CENTER);
			}
			return m_pnlPure;
		}
	

	}

	/**
	 * 此处插入方法说明。 创建日期：(2001-3-23 2:02:27)
	 * 
	 * @param e
	 *            ufbill.BillEditEvent
	 */
	String[] mdabjuct = {"","","",""};
	
	public void afterEdit(nc.ui.pub.bill.BillEditEvent e) {
		// 1.处理参照的ID和Name以及Value到m_voBill
		String strColName = e.getKey().trim();
		int rownum = e.getRow();
		String sID_name = GeneralMethod.getFromIDtoName(getBillCardPanel(),
				getBillListPanel(), strColName);
		if ((sID_name != null) && (m_voBill != null)) {
			if (e.getPos() == 0
					&& null != getBillCardPanel().getBillData().getHeadItem(
							strColName)) {
				if (getBillCardPanel().getHeadItem(strColName).getComponent() instanceof UIRefPane) {
					if (!sID_name.trim().equals(strColName)) {
						m_voBill
								.setHeaderValue(
										sID_name,
										((nc.ui.pub.beans.UIRefPane) getBillCardPanel()
												.getHeadItem(strColName)
												.getComponent()).getRefName());
						m_voBill
								.setHeaderValue(
										strColName,
										((nc.ui.pub.beans.UIRefPane) getBillCardPanel()
												.getHeadItem(strColName)
												.getComponent()).getRefPK());
					} else if (!GeneralMethod.getIDColName(getBillCardPanel(),
							strColName).equals(strColName)) {
						sID_name = GeneralMethod.getIDColName(
								getBillCardPanel(), strColName);
						m_voBill
								.setHeaderValue(
										strColName,
										((nc.ui.pub.beans.UIRefPane) getBillCardPanel()
												.getHeadItem(strColName)
												.getComponent()).getRefName());
						m_voBill
								.setHeaderValue(
										sID_name,
										((nc.ui.pub.beans.UIRefPane) getBillCardPanel()
												.getHeadItem(strColName)
												.getComponent()).getRefPK());
					} else {
						// m_voBill.setHeaderValue(sID_name, e.getValue());
					}
				} else {
					m_voBill.setHeaderValue(sID_name, e.getValue());
				}
			} else if (e.getPos() == 2
					&& null != getBillCardPanel().getBillData().getTailItem(
							strColName)) {
			} else if (e.getPos() == 1
					&& null != getBillCardPanel().getBillData().getBodyItem(
							strColName)) {
				if (getBillCardPanel().getBodyItem(strColName).getComponent() instanceof UIRefPane) {
					if (!sID_name.trim().equals(strColName)) {
						m_voBill
								.setItemValue(
										rownum,
										sID_name,
										((nc.ui.pub.beans.UIRefPane) getBillCardPanel()
												.getBodyItem(strColName)
												.getComponent()).getRefName());
						m_voBill
								.setItemValue(
										rownum,
										strColName,
										((nc.ui.pub.beans.UIRefPane) getBillCardPanel()
												.getBodyItem(strColName)
												.getComponent()).getRefPK());
						// 置显示列
						getBillCardPanel()
								.setBodyValueAt(
										((nc.ui.pub.beans.UIRefPane) getBillCardPanel()
												.getBodyItem(strColName)
												.getComponent()).getRefName(),
										rownum, strColName);
					} else if (!GeneralMethod.getIDColName(getBillCardPanel(),
							strColName).equals(strColName)) {
						sID_name = GeneralMethod.getIDColName(
								getBillCardPanel(), strColName);
						m_voBill
								.setItemValue(
										rownum,
										strColName,
										((nc.ui.pub.beans.UIRefPane) getBillCardPanel()
												.getBodyItem(strColName)
												.getComponent()).getRefName());
						m_voBill
								.setItemValue(
										rownum,
										sID_name,
										((nc.ui.pub.beans.UIRefPane) getBillCardPanel()
												.getBodyItem(strColName)
												.getComponent()).getRefPK());
						// 置显示列
						getBillCardPanel()
								.setBodyValueAt(
										((nc.ui.pub.beans.UIRefPane) getBillCardPanel()
												.getBodyItem(strColName)
												.getComponent()).getRefName(),
										rownum, strColName);
					} else {
						// m_voBill.setItemValue(rownum, sID_name,
						// e.getValue());
					}
				} else {
					m_voBill.setItemValue(rownum, sID_name, e.getValue());
				}
			}
		}

		// 2.表头仓库编辑控制
		int firstRow = rownum;
		if (strColName.equals("coutwarehouseid")) { // 出库仓库
			afterWhOutEdit(true);

		} else if (e.getKey().equals("coutbsor")) { // 入库仓库
			afterBsorEdit(new String[] { "coutbsor", "coutbsorname" },
					new String[] { "coutdeptid", "coutdeptname" });
		} else if (BillMode.Browse != m_iMode
				&& e.getKey().startsWith("vuserdef")) {// 自定义项处理zhy
			super.afterDefEdit(e);
		}

		if (firstRow == -1) {
			return;
		}
		// 3.表体控制
		int column = getBillCardPanel().getBillModel().getBodyColByKey(
				strColName);
		int listcolumn = getBillCardPanel().getBillTable()
				.convertColumnIndexToView(column);
		// 存货编码改变
		if (strColName.equals("cinventorycode")) {
			afterInvMutiEdit(e);
		} else if (strColName.equals("vfree0")) {
			afterFreeItemEdit(e);
		} else if (strColName.equals("vbatchcode")) {
			afterLotEdit(strColName, rownum);
		} else if (strColName.equals("castunitname")) {
			afterAstUOMEdit(rownum);
		}
		// 差异数量，差异辅数量： 差异数量修改，根据差异数量的编辑公式自动修改盘点数量（数据库以及UAP代码实现），同时根据公式自动修改调整数量。
		// 公式由UAP自动调用，这里处理与换算率相关的代码。
		else if (strColName.equals("cysl")) {
			calHslNumAstNum(rownum, "cysl", "cyfsl", 0);
			calHslNumAstNum(rownum, "nchecknum", "ncheckastnum", 0);
			calHslNumAstNum(rownum, "nadjustnum", "nadjustastnum", 0);
			execEditFormula(rownum, "nadjustnum");
		} else if (strColName.equals("cyfsl")) {
			calHslNumAstNum(rownum, "cysl", "cyfsl", 1);
			getBillCardPanel().getBillModel().execEditFormulaByKey(rownum,
					"cysl");
			calHslNumAstNum(rownum, "nadjustnum", "nadjustastnum", 1);
		} else if (strColName.equals("ndiffgrsnum")) {
			m_voBill.setItemValue(rownum, strColName, e.getValue());
			m_voBill.setItemValue(rownum, "nadjustgrsnum", getBillCardPanel()
					.getBodyValueAt(rownum, "nadjustgrsnum"));
			m_voBill.setItemValue(rownum, "ncheckgrsnum", getBillCardPanel()
					.getBodyValueAt(rownum, "ncheckgrsnum"));
		}
		// 盘点数量以及盘点辅助数量编辑后控制
		else if (strColName.equals("nchecknum")) {
			calHslNumAstNum(rownum, "nchecknum", "ncheckastnum", 0);
			calHslNumAstNum(rownum, "nadjustnum", "nadjustastnum", 0);
			calHslNumAstNum(rownum, "cysl", "cyfsl", 0);
			execEditFormula(rownum, "nadjustnum");// zhy2005-11-04盘点数量变化时触发调整数量改变，同时需要触发调整金额和差异率改变
			// 修改人：刘家清 修改日期：2007-9-12下午03:43:42 修改原因：
			getBillCardPanel().getBillModel().execEditFormulaByKey(rownum,
					"ncheckastnum");
		} else if (strColName.equals("ncheckastnum")) {
			calHslNumAstNum(rownum, "nchecknum", "ncheckastnum", 1);
			getBillCardPanel().getBillModel().execEditFormulaByKey(rownum,
					"nchecknum");
			execEditFormula(rownum, "nadjustnum");// zhy2005-11-04同上
		} else if (strColName.equals("ncheckgrsnum")) {
			m_voBill.setItemValue(rownum, strColName, e.getValue());
			m_voBill.setItemValue(rownum, "nadjustgrsnum", getBillCardPanel()
					.getBodyValueAt(rownum, "nadjustgrsnum"));
		}
		// 调整数量以及调整辅数量编辑后控制
		else if (strColName.equals("nadjustnum")) {
			calHslNumAstNum(rownum, "nadjustnum", "nadjustastnum", 0);
		} else if (strColName.equals("nadjustastnum")) {
			calHslNumAstNum(rownum, "nadjustnum", "nadjustastnum", 1);
			execEditFormula(rownum, "nadjustnum");
		} else if (strColName.equals("nadjustgrsnum")) {
			m_voBill.setItemValue(rownum, strColName, e.getValue());
			m_voBill.setItemValue(rownum, "ncheckgrsnum", getBillCardPanel()
					.getBodyValueAt(rownum, "ncheckgrsnum"));
		} else if (strColName.equals("cspacename")) {
			afterSpaceEdit(e);
		} else if (strColName.equals("hsl")) {
			mustNoNegative(strColName, firstRow, getBillCardPanel(), m_voBill);
			afterHslEdit(rownum);
		} // zhx added for bill row no, after edit process.
		else if (strColName.equals(m_sBillRowNo)) {
			nc.ui.scm.pub.report.BillRowNo.afterEditWhenRowNo(
					getBillCardPanel(), e, m_sBillTypeCode);
			// 同步化VO
			m_voBill.setItemValue(rownum, m_sBillRowNo, getBillCardPanel()
					.getBodyValueAt(rownum, m_sBillRowNo));
		}

		// 生产日期
		else if (strColName.equals("scrq")) {
			nc.vo.scm.ic.bill.InvVO voInv = m_voBill.getItemInv(rownum);
			if (voInv != null && voInv.getIsValidateMgt() != null
					&& voInv.getIsValidateMgt().intValue() == 1) {
				nc.vo.pub.lang.UFDate dScrq = null;
				if (e.getValue() != null
						&& GenMethod.isAllowDate(e.getValue().toString())) {
					dScrq = new nc.vo.pub.lang.UFDate(e.getValue().toString());
					getBillCardPanel().setBodyValueAt(
							dScrq
									.getDateAfter(
											voInv.getQualityDay().intValue())
									.toString(), rownum, "dvalidate");
				}
			}
		}
		// 失效日期
		else if (strColName.equals("dvalidate")) {
			nc.vo.scm.ic.bill.InvVO voInv = m_voBill.getItemInv(rownum);
			if (voInv != null && voInv.getIsValidateMgt() != null
					&& voInv.getIsValidateMgt().intValue() == 1) {
				nc.vo.pub.lang.UFDate dvalidate = null;
				if (e.getValue() != null
						&& GenMethod.isAllowDate(e.getValue().toString())) {
					dvalidate = new nc.vo.pub.lang.UFDate(e.getValue()
							.toString());
					getBillCardPanel().setBodyValueAt(
							dvalidate.getDateBefore(
									voInv.getQualityDay().intValue())
									.toString(), rownum, "scrq");
				}
			}
		} else if (strColName.equals("cvendorname")) {
			super.afterVendorEdit(rownum);
			String[] sIKs = getClearIDs(1, "cvendorname");
			int row = e.getRow();
			clearRowData(row, sIKs);
		}
		
		//add by ouyangzhb 2012-04-25  码单盘点处理
		BillItem mditembz = getBillCardPanel().getHeadItem("vuserdef1");
		if(mditembz !=null ){
			String ismd = mditembz.getValue();
			if(ismd !=null&&new UFBoolean(ismd).booleanValue()){
				if("vuserdef5,vuserdef6,vuserdef7,vuserdef8,nchecknum,ncheckastnum,ncheckgrsnum,pspacename".contains(strColName)){
					//新定义一个标识，如果一下数据项组中存在不相等的，则认为是修改过的，取值为true
					String vuserdef2 = (String) getBillCardPanel().getBodyValueAt(e.getRow(), "vuserdef2") == null ? "":(String) getBillCardPanel().getBodyValueAt(e.getRow(), "vuserdef2");//验收长
					String vuserdef3 = (String) getBillCardPanel().getBodyValueAt(e.getRow(), "vuserdef3") == null ? "":(String) getBillCardPanel().getBodyValueAt(e.getRow(), "vuserdef3");//验收宽
					String vuserdef4 = (String) getBillCardPanel().getBodyValueAt(e.getRow(), "vuserdef4") == null ? "":(String) getBillCardPanel().getBodyValueAt(e.getRow(), "vuserdef4");//验收米数
					String vuserdef5 = (String) getBillCardPanel().getBodyValueAt(e.getRow(), "vuserdef5") == null ? "":(String) getBillCardPanel().getBodyValueAt(e.getRow(), "vuserdef5");//盘点长
					String vuserdef6 = (String) getBillCardPanel().getBodyValueAt(e.getRow(), "vuserdef6") == null ? "":(String) getBillCardPanel().getBodyValueAt(e.getRow(), "vuserdef6");//盘点宽
					String vuserdef7 = (String) getBillCardPanel().getBodyValueAt(e.getRow(), "vuserdef7") == null ? "":(String) getBillCardPanel().getBodyValueAt(e.getRow(), "vuserdef7");//盘点米数
					String vuserdef8 = (String) getBillCardPanel().getBodyValueAt(e.getRow(), "vuserdef8") == null ? "":(String) getBillCardPanel().getBodyValueAt(e.getRow(), "vuserdef8");//盘点货位
					String cspaceid = (String) getBillCardPanel().getBodyValueAt(e.getRow(), "cspaceid") == null ? "":(String) getBillCardPanel().getBodyValueAt(e.getRow(), "cspaceid");//货位
					 UFDouble nchecknum =  getBillCardPanel().getBodyValueAt(e.getRow(), "nchecknum") == null ? UFDouble.ZERO_DBL:(UFDouble) getBillCardPanel().getBodyValueAt(e.getRow(), "nchecknum");//盘点数量
					 UFDouble ncheckastnum = getBillCardPanel().getBodyValueAt(e.getRow(), "ncheckastnum") == null ? UFDouble.ZERO_DBL:(UFDouble) getBillCardPanel().getBodyValueAt(e.getRow(), "ncheckastnum");//盘点辅数量
					 UFDouble naccountastnum = getBillCardPanel().getBodyValueAt(e.getRow(), "naccountastnum") == null ? UFDouble.ZERO_DBL:(UFDouble) getBillCardPanel().getBodyValueAt(e.getRow(), "naccountastnum");//账面辅数量
					 UFDouble naccountnum = getBillCardPanel().getBodyValueAt(e.getRow(), "naccountnum") == null ? UFDouble.ZERO_DBL:(UFDouble) getBillCardPanel().getBodyValueAt(e.getRow(), "naccountnum");//账面数量
					if((vuserdef2 != null && vuserdef5 != null && !vuserdef2.endsWith(vuserdef5))
							|| (vuserdef3 != null && vuserdef6 != null && !vuserdef3.endsWith(vuserdef6))
							|| (vuserdef4 != null && vuserdef7 != null && !vuserdef4.endsWith(vuserdef7))
							|| (vuserdef8 != null && cspaceid != null && !vuserdef8.endsWith(cspaceid))
							|| (nchecknum != null && naccountnum != null && nchecknum.compareTo(naccountnum) !=0)
							|| (ncheckastnum != null && naccountastnum != null && ncheckastnum.compareTo(naccountastnum) !=0)
							){
						getBillCardPanel().setBodyValueAt("Y",e.getRow(), "vuserdef9");
					}else {
						getBillCardPanel().setBodyValueAt("N",e.getRow(), "vuserdef9");
					}
					
				}
			}
		}
			
		

	}

	/**
	 * 初始化类。
	 */
	/* 警告：此方法将重新生成。 */
	protected void initialize() {
		try {
			super.initialize();
			// 修改人：刘家清 修改日期：2007-12-26上午11:05:02 修改原因：右键增加"重排行号"
			UIMenuItem[] oldUIMenuItems = getBillCardPanel().getBodyMenuItems();
			if (oldUIMenuItems.length > 0) {
				ArrayList<UIMenuItem> newMenuList = new ArrayList<UIMenuItem>();
				for (UIMenuItem oldUIMenuItem : oldUIMenuItems)
					newMenuList.add(oldUIMenuItem);
        newMenuList.add(getLineCardEditItem());
				newMenuList.add(getAddNewRowNoItem());
				getAddNewRowNoItem().removeActionListener(this);
				getAddNewRowNoItem().addActionListener(this);
				UIMenuItem[] newUIMenuItems = new UIMenuItem[newMenuList.size()];
				m_Menu_AddNewRowNO_Index = newMenuList.size() - 1;
				newMenuList.toArray(newUIMenuItems);
				// getBillCardPanel().setBodyMenu(newUIMenuItems);
				getBillCardPanel().getBodyPanel().setMiBody(newUIMenuItems);
				getBillCardPanel().getBodyPanel().setBBodyMenuShow(true);
				getBillCardPanel().getBodyPanel().addTableBodyMenu();
        
        getLineCardEditItem().removeActionListener(this);
        getLineCardEditItem().addActionListener(this);

			}

			// setButtons(m_aryButtonGroup);

			// 得到当前的表体中系统默认的背景色
			m_cNormalColor = getBillCardPanel().getBillTable().getBackground();

			// 更改背景色后的表体中Header着色器
			DefaultTableCellRenderer tcrold = (DefaultTableCellRenderer) getBillCardPanel()
					.getBillTable().getColumnModel().getColumn(1)
					.getHeaderRenderer();
			HeaderRenderer tcr = new HeaderRenderer(tcrold);

			// 分别得到表头和表体中需着重显示的字段
			ArrayList alHeaderColChangeColorString = GeneralMethod
					.getHeaderCanotNullString(getBillCardPanel());
			ArrayList alBodyColChangeColorString = GeneralMethod
					.getBodyCanotNullString(getBillCardPanel());

			// 修改表单中的表头颜色
			SetColor.SetBillCardHeaderColor(getBillCardPanel(),
					alHeaderColChangeColorString);
			// SetBillCardHeaderColor(alHeaderColChangeColorString);

			// 置入各个着色器于表格的Header中
			SetColor.SetTableHeaderColor(getBillCardPanel().getBillModel(),
					getBillCardPanel().getBillTable(),
					alBodyColChangeColorString, tcr);
			SetColor.SetTableHeaderColor(getBillListPanel().getHeadBillModel(),
					getBillListPanel().getHeadTable(),
					alHeaderColChangeColorString, tcr);
			SetColor.SetTableHeaderColor(getBillListPanel().getBodyBillModel(),
					getBillListPanel().getBodyTable(),
					alBodyColChangeColorString, tcr);

			// 置入各个着色器于表格的Row中
/*			SetColor.SetTableColor(getBillCardPanel().getBillModel(),
					getBillCardPanel().getBillTable(), getBillCardPanel(),
					new ArrayList(), m_cNormalColor, m_cNormalColor,
					m_bExchangeColor, m_bLocateErrorColor, "");
			SetColor.SetTableColor(getBillListPanel().getHeadBillModel(),
					getBillListPanel().getHeadTable(), getBillCardPanel(),
					new ArrayList(), m_cNormalColor, m_cNormalColor,
					m_bExchangeColor, m_bLocateErrorColor, "");
			SetColor.SetTableColor(getBillListPanel().getBodyBillModel(),
					getBillListPanel().getBodyTable(), getBillCardPanel(),
					new ArrayList(), m_cNormalColor, m_cNormalColor,
					m_bExchangeColor, m_bLocateErrorColor, "");*/
			
			SetColor.resetColor(getBillCardPanel().getBillTable());
			SetColor.resetColor(getBillListPanel().getHeadTable());
			SetColor.resetColor(getBillListPanel().getBodyTable());
			
			/*nc.ui.ic.pub.tools.GenMethod.reSetRowColorWhenNOException(getBillCardPanel().getBillTable());
			nc.ui.ic.pub.tools.GenMethod.reSetRowColorWhenNOException(getBillListPanel().getHeadTable());
			nc.ui.ic.pub.tools.GenMethod.reSetRowColorWhenNOException(getBillListPanel().getBodyTable());*/


			m_layoutManager.show(true);

		} catch (java.lang.Throwable ivjExc) {
			handleException(ivjExc);
		}
	}

	/**
	 * 主入口点 - 当部件作为应用程序运行时，启动这个部件。
	 * 
	 * @param args
	 *            java.lang.String[]
	 */
	public static void main(java.lang.String[] args) {
		try {
			javax.swing.JFrame frame = new javax.swing.JFrame();
			ClientUI aClientUI;
			aClientUI = new ClientUI();
			frame.setContentPane(aClientUI);
			frame.setSize(aClientUI.getSize());
			frame.addWindowListener(new java.awt.event.WindowAdapter() {
				public void windowClosing(java.awt.event.WindowEvent e) {
					System.exit(0);
				};
			});
			frame.show();
			java.awt.Insets insets = frame.getInsets();
			frame.setSize(frame.getWidth() + insets.left + insets.right, frame
					.getHeight()
					+ insets.top + insets.bottom);
			frame.setVisible(true);
		} catch (Throwable exception) {
			nc.vo.scm.pub.SCMEnv.out("nc.ui.pub.ToftPanel 的 main() 中发生异常");
			nc.vo.scm.pub.SCMEnv.error(exception);
		}
	}

	/**
	 * onButtonClicked 方法注解。
	 */
	public void onButtonClicked(nc.ui.pub.ButtonObject bo) { // finished
		showHintMessage(bo.getName());
		if (bo == m_boAdd) {
			m_bBillInit = true;
			onAdd();
		} else if (bo == m_boChange)
			onChange();
		else if (bo == m_boDelete)
			onDelete();
		else if (bo == m_boCopyBill) {
			m_bBillInit = true;
			onCopyBill();
		} else if (bo == m_boSave)
			onSave();
		else if (bo == m_boCancel) {
			onCancel();
		} else if (bo == m_boAddRow)
			onAddRow();
		else if (bo == m_boDeleteRow)
			onDeleteRow();
		else if (bo == m_boInsertRow)
			onInsertRow();
		else if (bo == m_boCopyRow)
			onCopyRow();
		else if (bo == m_boPasteRow)
			onPasteRow();

		else if (bo == m_boGetAccNumber) {
			onQryAccNumber();
		} else if (bo == m_boGetBCAccNumber) {
			onQryBCAccNumber();
		}else if (bo == m_boCheckChoose) {
			m_bBillInit = true;
			onCheckChoose();

		} else if (bo == m_boAdjustGoods)
			onAdjustGoods();
		else if (bo == m_boCancelAdjust) {
			onCancelAdjust();
		} else if (bo == m_boQuery)
			onQuery();
		else if (bo == m_boLocate)
			onLocate();
		else if (bo == m_boPrint)
			onPrint();
		else if (bo == m_boPreview)
			onPreview();
		else if (bo == m_boList)
			onList();
		else if (bo == m_boRowQuyQty)
			onRowQuyQty();
		else if (bo == m_boCheckInv)
			onImportData();
		else if (bo == m_boAuditState)
			onAuditState();
		else if (bo == m_boFillNum) {
			onFillNum();
		} else if (bo == m_boCheckNum) {
			onChange();
			// 设置实盘录入 需要单独放在函数外面设置，
			// 其他的情况setBillCheckInputStatus都在在函数内部面设置
			// 避免状态设置不及时
			setBillCheckInputStatus(nc.vo.ic.pub.bill.BillStatus.CHECKINPUT);
		} else if (bo == m_boCalculate)
			onCalculate();
		else if (bo == m_boAuditBill)
			onAuditBill();
		else if (bo == m_boCancelAudit)
			onCancelAudit();
		else if (bo == m_boViewOnhandBC)
			onViewOnhandBC();
		else if (bo == m_boViewCountBC)
			onViewCountBC();
		else if (bo == m_boViewDiffBC)
			onViewDiffBC();
		else if (bo == m_boCountBCImport)
			onCountBCImport();
		else if (bo == m_boBarCode)
			onBarCodeEdit();
		else if (bo == m_boCommit)
			onCommit();
		else
			super.onButtonClicked(bo);

		// 设置是否可以表体排序add by hanwei 2003-12-30
		setBillSortEnable();

	}
	
	
	protected BarCodeDlg m_dlgBarCodeEdit = null;
	public BarCodeDlg getBarCodeDlg() {
		m_dlgBarCodeEdit = new BarCodeDlg(this, m_sCorpID);
		return m_dlgBarCodeEdit;
	}
	
	public BarCodeDlg getBarCodeDlg(boolean bEditable, boolean bSaveable) {
		m_dlgBarCodeEdit = new BarCodeDlg(this, m_sCorpID);
		return m_dlgBarCodeEdit;
	}
	
	public void setBillCrowNo(SpecialBillVO voBill,
			nc.ui.pub.bill.BillCardPanel billcardpanel) {
		if (voBill == null || voBill.getChildrenVO() == null
				|| voBill.getChildrenVO().length == 0)
			return;

		SpecialBillItemVO[] billItems = (SpecialBillItemVO[]) voBill
				.getChildrenVO();

		for (int i = 0; i < billItems.length; i++) {
			if (billItems[i] != null && billItems[i].getCinventoryid() != null) {
				if (billItems[i].getCrowno() == null)
					billItems[i].setCrowno((String) billcardpanel
							.getBodyValueAt(i, "crowno"));

			}
		}

	}
	// 条码编辑界面控制类
	protected nc.ui.ic.pub.bill.BarcodeCtrl m_BarcodeCtrl = null;
	public BarcodeCtrl getBarcodeCtrl() {
		if (m_BarcodeCtrl == null) {
			m_BarcodeCtrl = new BarcodeCtrl();
			m_BarcodeCtrl.m_sCorpID = m_sCorpID;
		}
		return m_BarcodeCtrl;
	}
	
	// 条码编辑框的颜色列：每X+1行的颜色需要设置
	protected int m_iBarcodeUIColorRow = 20;
	
	public void setBarCodeOnUI(SpecialBillVO billVO,
			SpecialBillItemVO[] voaItems) {

		if (voaItems.length <= 0)
			return;
		Hashtable htbItemBarcodeVos = getBarcodeCtrl().getHtbItemBarcodeVos(
				voaItems);

		SpecialBillItemVO[] billItemsAll = (SpecialBillItemVO[]) billVO
				.getChildrenVO();
		if (billItemsAll.length <= 0)
			return;

		if (htbItemBarcodeVos != null && htbItemBarcodeVos.size() > 0) {

			SpecialBillItemVO billItemTemp = null;
			if (htbItemBarcodeVos != null) {

				String sRowNo = null;
				for (int i = 0; i < billItemsAll.length; i++) {
					sRowNo = billItemsAll[i].getCrowno();
					if (sRowNo != null && htbItemBarcodeVos.containsKey(sRowNo)) {
						billItemTemp = (SpecialBillItemVO) htbItemBarcodeVos
								.get(sRowNo);
						billItemsAll[i].setBarCodeVOs(billItemTemp
								.getBarCodeVOs());
					}

				}

			}

		}

	}
	
	private void onBarCodeEditUpdateBill(int iCurFixLine,
			SpecialBillItemVO billItemvo) {

		boolean bNegative = false; // 是否负数
		// 修改实发数量和应发数量
		UFDouble ufdNum = null;
		// 根据"是否按辅单位增加数量"属性，如果是则辅数量自动加一；否则主数量自动加一。
		String m_sMyItemKey = null;
		String m_sMyShouldItemKey = null;
		if (billItemvo.getBarCodeVOs() != null
				&& billItemvo.getBarCodeVOs().length > 0
				&& ((billItemvo.getBarCodeVOs().length == 1
						&& billItemvo.getBarCodeVOs()[billItemvo
								.getBarCodeVOs().length - 1]
								.getAttributeValue("vbarcode") != null
						&& billItemvo.getBarCodeVOs()[billItemvo
								.getBarCodeVOs().length - 1]
								.getAttributeValue("vbarcodesub") == null && billItemvo
						.getBarCodeVOs()[billItemvo.getBarCodeVOs().length - 1]
						.getBasstaddflag().booleanValue())
						|| (billItemvo.getBarCodeVOs().length > 1
								&& billItemvo.getBarCodeVOs()[billItemvo
										.getBarCodeVOs().length - 2]
										.getAttributeValue("vbarcode") != null
								&& billItemvo.getBarCodeVOs()[billItemvo
										.getBarCodeVOs().length - 2]
										.getAttributeValue("vbarcodesub") == null && billItemvo
								.getBarCodeVOs()[billItemvo.getBarCodeVOs().length - 2]
								.getBasstaddflag().booleanValue())
						|| (billItemvo.getBarCodeVOs()[billItemvo
								.getBarCodeVOs().length - 1]
								.getAttributeValue("vbarcode") == null
								&& billItemvo.getBarCodeVOs()[billItemvo
										.getBarCodeVOs().length - 1]
										.getAttributeValue("vbarcodesub") != null && billItemvo
								.getBarCodeVOs()[billItemvo.getBarCodeVOs().length - 1]
								.getBasstaddflagsub().booleanValue())
						|| (billItemvo.getBarCodeVOs()[billItemvo
								.getBarCodeVOs().length - 1]
								.getAttributeValue("vbarcode") != null
								&& billItemvo.getBarCodeVOs()[billItemvo
										.getBarCodeVOs().length - 1]
										.getAttributeValue("vbarcodesub") != null && billItemvo
								.getBarCodeVOs()[billItemvo.getBarCodeVOs().length - 1]
								.getBasstaddflagsub().booleanValue()) || (billItemvo
						.getBarCodeVOs().length > 1
						&& billItemvo.getBarCodeVOs()[billItemvo
								.getBarCodeVOs().length - 2]
								.getAttributeValue("vbarcode") != null
						&& billItemvo.getBarCodeVOs()[billItemvo
								.getBarCodeVOs().length - 2]
								.getAttributeValue("vbarcodesub") != null && billItemvo
						.getBarCodeVOs()[billItemvo.getBarCodeVOs().length - 2]
						.getBasstaddflagsub().booleanValue()))
				&& billItemvo.getCastunitname() != null) {

			m_sMyItemKey = "nchecknum";
			m_sMyShouldItemKey = "nchecknum";

		} else {
			m_sMyItemKey = "nchecknum";
			m_sMyShouldItemKey = "nchecknum";
		}
		Object oTemp = getBillCardPanel().getBodyValueAt(iCurFixLine,
				m_sMyItemKey);
		if (oTemp == null) {
			ufdNum = nc.vo.ic.pub.GenMethod.ZERO;
		} else {
			ufdNum = (UFDouble) oTemp;
		}

		UFDouble ufdShouldNum = null;
		Object oShouldTemp = getBillCardPanel().getBodyValueAt(iCurFixLine,
				m_sMyShouldItemKey);
		if (oShouldTemp == null) {
			ufdShouldNum = nc.vo.ic.pub.GenMethod.ZERO;
		} else {
			ufdShouldNum = (UFDouble) oShouldTemp;
		}
		if (ufdNum.doubleValue() < 0 || ufdShouldNum.doubleValue() < 0) {
			bNegative = true;
		}

		// 删除的数据
		// UFDouble ufdZero = UFDZERO;
		UFDouble ufdNumDlg = nc.vo.ic.pub.GenMethod.ZERO;
		nc.vo.ic.pub.bc.BarCodeVO[] barcodevosAll = billItemvo.getBarCodeVOs();

		if (barcodevosAll != null) {
			for (int n = 0; n < barcodevosAll.length; n++) {
				if (barcodevosAll[n] != null
						&& barcodevosAll[n].getStatus() != nc.vo.pub.VOStatus.DELETED) {
					ufdNumDlg = ufdNumDlg.add(barcodevosAll[n].getNnumber());
				}
			}
		}

		if (ufdNumDlg == null)
			ufdNumDlg = nc.vo.ic.pub.GenMethod.ZERO;

		// 设置条码数量字段
		try {
			getBillCardPanel().setBodyValueAt(ufdNumDlg.abs(), iCurFixLine,
					"nchecknum");

		} catch (Exception e) {
			nc.vo.scm.pub.SCMEnv.error(e);
		}
		// 是否修改单据数量要根据下面的情况判断
		if (true) {
			// 修改卡片界面状态

			getBillCardPanel().setBodyValueAt(ufdNumDlg, iCurFixLine,
					m_sMyItemKey);

			nc.ui.pub.bill.BillEditEvent event1 = new nc.ui.pub.bill.BillEditEvent(
					getBillCardPanel().getBodyItem(m_sMyItemKey), ufdNumDlg,
					m_sMyItemKey, iCurFixLine);
			afterEdit(event1);
			// 执行模版公式
			/*execEditFormula(getBillCardPanel(), iCurFixLine,
					m_sMyItemKey);*/
			// 触发单据行状态为修改
			if (getBillCardPanel().getBodyValueAt(iCurFixLine,
					"cspecialbid") != null)
				getBillCardPanel().getBillModel().setRowState(iCurFixLine,
						BillMode.Update);

		}
	}

	private void onBarCodeEdit() {
		
		try{
		
		if (!m_iscountflag.booleanValue()){
			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec",
			"UPP4008spec-000567")/* @res "非条码盘点单不能进行条码操作！" */);
			return;
		}
		// TODO 自动生成方法存根
		// 判断是否能够进行条码编辑
		SpecialBillVO voBill = null;

		int iCurFixLine = 0;
		// 是否可以编辑
		boolean bDirectSave = false;
		if (m_iMode == BillMode.Browse ) {
			bDirectSave = true;
		} else {
			bDirectSave = false;
		}

			voBill = m_voBill;
			iCurFixLine = getBillCardPanel().getBillTable().getSelectedRow();
	

		// 滤去表单形式下的空行
		filterNullLine();
		if (getBillCardPanel().getRowCount() <= 0) {
			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"4008bill", "UPP4008bill-000072")/* @res "请输入表体行!" */);
			getBillCardPanel().addLine();
			nc.ui.scm.pub.report.BillRowNo.addLineRowNo(getBillCardPanel(),
					m_sBillTypeCode, "crowno");
			return;
		}
		// 检查行号的合法性; 该方法应放在过滤空行的后面。
		// 需要按行号确定唯一行
		if (!nc.ui.scm.pub.report.BillRowNo.verifyRowNosCorrect(
				getBillCardPanel(), "crowno")) {
			return;
		}
		boolean bEditable = true;

		m_dlgBarCodeEdit = getBarCodeDlg(bEditable, bDirectSave);

		if (voBill != null && iCurFixLine >= 0
				&& iCurFixLine < voBill.getItemCount()) {

			fillLineSpecailBarCode1VO(iCurFixLine);
			
			SpecialBillItemVO itemvo = voBill.getItemVOs()[iCurFixLine];
			// 是条码管理的存货
			if (itemvo.getBarcodeManagerflag().booleanValue() ) {
				// 得到表头的单据号, 表体行号, 存货名称,存货编码
				// ArrayList altemp = new ArrayList();

				// 新增情况下，行号没有在m_voBill中存在,这里设置行号
				setBillCrowNo(voBill, getBillCardPanel());

				// 获得条码管理的Items

				ArrayList alReuslt = getBarcodeCtrl().getCurBarcodeItems(
						voBill, iCurFixLine);
				if (alReuslt == null || alReuslt.size() < 2) {
					return;
				}
				SpecialBillItemVO[] itemBarcodeVos = (SpecialBillItemVO[]) alReuslt
						.get(0);
				int iFixLine = ((Integer) alReuslt.get(1)).intValue();
				SpecialBillHeaderVO headervo = voBill.getHeaderVO();
				m_dlgBarCodeEdit.setHeaderItemvo(headervo);

				if (itemBarcodeVos[iFixLine].getBarCodeVOs() != null
						&& itemBarcodeVos[iFixLine].getBarCodeVOs().length > 0
						&& ((itemBarcodeVos[iFixLine].getBarCodeVOs()[itemBarcodeVos[iFixLine]
								.getBarCodeVOs().length - 1]
								.getAttributeValue("vbarcode") != null
								&& itemBarcodeVos[iFixLine].getBarCodeVOs()[itemBarcodeVos[iFixLine]
										.getBarCodeVOs().length - 1]
										.getAttributeValue("vbarcodesub") == null && itemBarcodeVos[iFixLine]
								.getBarCodeVOs()[itemBarcodeVos[iFixLine]
								.getBarCodeVOs().length - 1].getBasstaddflag()
								.booleanValue())
								|| (itemBarcodeVos[iFixLine].getBarCodeVOs()[itemBarcodeVos[iFixLine]
										.getBarCodeVOs().length - 1]
										.getAttributeValue("vbarcode") == null
										&& itemBarcodeVos[iFixLine]
												.getBarCodeVOs()[itemBarcodeVos[iFixLine]
												.getBarCodeVOs().length - 1]
												.getAttributeValue("vbarcodesub") != null && itemBarcodeVos[iFixLine]
										.getBarCodeVOs()[itemBarcodeVos[iFixLine]
										.getBarCodeVOs().length - 1]
										.getBasstaddflagsub().booleanValue())
								|| (itemBarcodeVos[iFixLine].getBarCodeVOs()[itemBarcodeVos[iFixLine]
										.getBarCodeVOs().length - 1]
										.getAttributeValue("vbarcode") != null
										&& itemBarcodeVos[iFixLine]
												.getBarCodeVOs()[itemBarcodeVos[iFixLine]
												.getBarCodeVOs().length - 1]
												.getAttributeValue("vbarcodesub") != null && itemBarcodeVos[iFixLine]
										.getBarCodeVOs()[itemBarcodeVos[iFixLine]
										.getBarCodeVOs().length - 1]
										.getBasstaddflagsub().booleanValue()) || (itemBarcodeVos[iFixLine]
								.getBarCodeVOs().length > 1
								&& itemBarcodeVos[iFixLine].getBarCodeVOs()[itemBarcodeVos[iFixLine]
										.getBarCodeVOs().length - 2]
										.getAttributeValue("vbarcode") != null
								&& itemBarcodeVos[iFixLine].getBarCodeVOs()[itemBarcodeVos[iFixLine]
										.getBarCodeVOs().length - 2]
										.getAttributeValue("vbarcodesub") != null && itemBarcodeVos[iFixLine]
								.getBarCodeVOs()[itemBarcodeVos[iFixLine]
								.getBarCodeVOs().length - 2]
								.getBasstaddflagsub().booleanValue()))
						&& itemBarcodeVos[iFixLine].getCastunitname() != null) {

					m_dlgBarCodeEdit.m_sNumItemKey = "nchecknum";
					m_dlgBarCodeEdit.m_sShouldNumItemKey = "nchecknum";

				} else {
					m_dlgBarCodeEdit.m_sNumItemKey = "nchecknum";
					m_dlgBarCodeEdit.m_sShouldNumItemKey = "nchecknum";
				}
				m_dlgBarCodeEdit.m_iBarcodeUIColorRow = m_iBarcodeUIColorRow;
				
				//m_dlgBarCodeEdit.setColor(m_sColorRow);
				//m_dlgBarCodeEdit.setScale(m_ScaleValue.getScaleValueArray());

				// 将条码是否保存参数设置到条码编辑界面，便于在编辑界面保存条码前控制
				// m_dlgBarCodeEdit.setSaveBarCode(m_bBarcodeSave);
				m_dlgBarCodeEdit.setSaveBadBarCode(false);
				// 修改人:刘家清 修改日期:2007-04-10
				// 修改原因:逻辑不对,对于一个单据,是否保存条码是用bSaveBarcodeFinal
				
				// 如果条码关闭，设置条码编辑框不能编辑
				boolean bbarcodeclose = itemvo.getBarcodeClose().booleanValue();
				m_dlgBarCodeEdit.setUIEditableBarcodeClose(!bbarcodeclose);
				m_dlgBarCodeEdit.setUIEditable(m_iMode);
				// 设置到Items中
				m_dlgBarCodeEdit.setCurBarcodeItems(itemBarcodeVos, iFixLine);

				if (m_dlgBarCodeEdit.showModal() == nc.ui.pub.beans.UIDialog.ID_OK) {
					getBillCardPanel().getBillModel().setNeedCalculate(false);
					// 设置条码框数据
					m_utfBarCode.setCurBillItem(itemBarcodeVos);
					// 还有要设置条码框的删除数据？m_utfBarCode.setRemoveBarcode(m_dlgBarCodeEdit.getBarCodeDelofAllVOs());

					// 目的设置m_billvo的条码数据，修改卡片界面状态

					setBarCodeOnUI(voBill, itemBarcodeVos);

					Hashtable htbItemBarcodeVos = getBarcodeCtrl()
							.getHtbItemBarcodeVos(itemBarcodeVos);

					if (htbItemBarcodeVos != null
							&& htbItemBarcodeVos.size() > 0) {

						SpecialBillItemVO billItemTemp = null;
						if (htbItemBarcodeVos != null) {
							SpecialBillItemVO[] billItemsAll = (SpecialBillItemVO[]) voBill
									.getChildrenVO();
							String sRowNo = null;
							for (int i = 0; i < billItemsAll.length; i++) {
								sRowNo = billItemsAll[i].getCrowno();
								if (sRowNo != null
										&& htbItemBarcodeVos
												.containsKey(sRowNo)) {
									billItemTemp = (SpecialBillItemVO) htbItemBarcodeVos
											.get(sRowNo);

									onBarCodeEditUpdateBill(i, billItemTemp);

								}

							}

							if (!m_dlgBarCodeEdit.m_bModifyBillUINum) { // 修改卡片界面状态
								showHintMessage(nc.ui.ml.NCLangRes
										.getInstance().getStrByID("4008bill",
												"UPP4008bill-000073")/*
																		 * @res
																		 * "条码编辑框的参数设置是不修改界面数量，单据界面实际数量不被修改！"
																		 */);
							}

							if (!getBarcodeCtrl().isModifyBillUINum()) {
								showHintMessage(nc.ui.ml.NCLangRes
										.getInstance().getStrByID("4008bill",
												"UPP4008bill-000074")/*
																		 * @res
																		 * "当前单据界面不允许修改通过条码数量修改实际数量，单据界面实际数量不被修改！"
																		 */);

							}

							if (m_dlgBarCodeEdit.m_bModifyBillUINum
									&& getBarcodeCtrl().isModifyBillUINum()) { // 修改卡片界面状态
								showHintMessage(nc.ui.ml.NCLangRes
										.getInstance().getStrByID("4008bill",
												"UPP4008bill-000075")/*
																		 * @res
																		 * "条码编辑框的参数设置是修改界面数量并且界面规则允许修改实际数量，单据界面实际数量已经被修改！"
																		 */);
							}

						}

					}
					// dw
					//.resetSpace(iCurFixLine);

					getBillCardPanel().getBillModel().setNeedCalculate(true);
					getBillCardPanel().getBillModel().reCalcurateAll();

				}

			} else {// 修改人:刘家清 修改日期:2007-04-05 修改原因:不是存货管理的物品
				nc.ui.ic.pub.tools.GenMethod.handleException(this, null,
						new BusinessException(nc.ui.ml.NCLangRes.getInstance()
								.getStrByID("4008bill", "UPP4008bill-000002")/*
																				 * @res
																				 * "下列存货非主条码管理或次条码管理，请修改存货管理档案的属性："
																				 */
								+ itemvo.getCinventorycode()));
			}

		} else {
			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"SCMCOMMON", "UPPSCMCommon-000356")/* @res "请选择表体行！" */);
		}
		}catch (Exception e) {
			// TODO: handle exception
			GenMethod.handleException(this, null, e);
		}
	}

	protected BarCodeViewDlg m_onhandBCViewDlg = null;//结存条码明细
	protected BarCodeViewDlg m_countBCViewDlg = null;//实盘条码明细
	protected BarCodeViewDlg m_DiffBCViewDlg = null;//条码盘点差异明细

	/**
	 * 创建人：刘家清 创建时间：2008-7-10 下午08:27:13 创建原因： 盘点单行条码盘点后的结存条码明细信息
	 *
	 */
	private void onViewOnhandBC(){
		// TODO 自动生成方法存根

		try{
		int iCurFixLine = 0;
		iCurFixLine = getBillCardPanel().getBillTable().getSelectedRow();
		
		if (iCurFixLine <0)
			iCurFixLine = 0;
		fillLineSpecailBarCodeVO(iCurFixLine);
		if (null != m_voBill && null != m_voBill.getItemVOs()
				&& iCurFixLine < m_voBill.getItemVOs().length
				&& null != m_voBill.getItemVOs()[iCurFixLine].getSpecailBarCodeVOs()){
			/*ArrayList<BarCodeViewVO> viewList = new ArrayList<BarCodeViewVO>();
			for (SpecailBarCodeVO  specailBarCodeVO : m_voBill.getItemVOs()[iCurFixLine].getSpecailBarCodeVOs()) {
				BarCodeViewVO bcVO = new BarCodeViewVO();
				bcVO.setVbarcode(specailBarCodeVO.getVbarcode());
				bcVO.setVbarcodesub(specailBarCodeVO.getVbarcodesub());
				bcVO.setNnumber(specailBarCodeVO.getNnumber());
				viewList.add(bcVO);
			}
			BarCodeViewVO[] barCodeViewVOs = new BarCodeViewVO[viewList.size()];
			viewList.toArray(barCodeViewVOs);*/
		getOnhandBCViewDlg().setDataVO(m_voBill.getItemVOs()[iCurFixLine].getSpecailBarCodeVOs());
		getOnhandBCViewDlg().showModal();
		}else
			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec",
			"UPP4008spec-000568")/* @res "没有可显示的条码" */);
		
		} catch (Exception e) {
			showErrorMessage(e.getMessage());
			handleException(e);
		}
	}
	/**
	 * 创建人：刘家清 创建时间：2008-7-10 下午08:28:01 创建原因： 盘点单行条码盘点后的实盘条码明细信息
	 *
	 */
	private void onViewCountBC(){
		// TODO 自动生成方法存根

		try{
		int iCurFixLine = 0;
		iCurFixLine = getBillCardPanel().getBillTable().getSelectedRow();
		
		if (iCurFixLine <0)
			iCurFixLine = 0;
		
		fillLineSpecailBarCode1VO(iCurFixLine);
		
		if (null != m_voBill && null != m_voBill.getItemVOs()
				&& iCurFixLine < m_voBill.getItemVOs().length
				&& null != m_voBill.getItemVOs()[iCurFixLine].getSpecailBarCode1VOs()){
			/*ArrayList<BarCodeViewVO> viewList = new ArrayList<BarCodeViewVO>();
			for (SpecailBarCodeVO  specailBarCodeVO : m_voBill.getItemVOs()[iCurFixLine].getSpecailBarCodeVOs()) {
				BarCodeViewVO bcVO = new BarCodeViewVO();
				bcVO.setVbarcode(specailBarCodeVO.getVbarcode());
				bcVO.setVbarcodesub(specailBarCodeVO.getVbarcodesub());
				bcVO.setNnumber(specailBarCodeVO.getNnumber());
				viewList.add(bcVO);
			}
			BarCodeViewVO[] barCodeViewVOs = new BarCodeViewVO[viewList.size()];
			viewList.toArray(barCodeViewVOs);*/
			getCountViewDlg().setDataVO(m_voBill.getItemVOs()[iCurFixLine].getSpecailBarCode1VOs());
			getCountViewDlg().showModal();
		}else
			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec",
			"UPP4008spec-000568")/* @res "没有可显示的条码" */);
		} catch (Exception e) {
			showErrorMessage(e.getMessage());
			handleException(e);
		}
	}

	/**
	 * 创建人：刘家清 创建时间：2008-7-10 下午08:28:15 创建原因： 盘点单行条码盘点后的条码盘点差异明细信息
	 *
	 */
	private void onViewDiffBC(){
		// TODO 自动生成方法存根

		try{
		int iCurFixLine = 0;
		iCurFixLine = getBillCardPanel().getBillTable().getSelectedRow();
		
		if (iCurFixLine <0)
			iCurFixLine = 0;
		fillLineSpecailBarCodeVO(iCurFixLine);
		fillLineSpecailBarCode1VO(iCurFixLine);
		
		if (null != m_voBill && null != m_voBill.getItemVOs()
				&& iCurFixLine < m_voBill.getItemVOs().length
				&& (null != m_voBill.getItemVOs()[iCurFixLine].getSpecailBarCodeVOs()
						||null != m_voBill.getItemVOs()[iCurFixLine].getSpecailBarCode1VOs())){
			
			
			ArrayList<BarCodeViewVO> viewList = new ArrayList<BarCodeViewVO>();
			if (null != m_voBill.getItemVOs()[iCurFixLine].getSpecailBarCodeVOs()
					&& null == m_voBill.getItemVOs()[iCurFixLine].getSpecailBarCode1VOs()){
				for (SpecailBarCodeVO  specailBarCodeVO : m_voBill.getItemVOs()[iCurFixLine].getSpecailBarCodeVOs()) {
					BarCodeViewVO bcVO = new BarCodeViewVO();
					bcVO.setVbarcode(specailBarCodeVO.getVbarcode());
					bcVO.setVbarcodesub(specailBarCodeVO.getVbarcodesub());
					bcVO.setNnumber(specailBarCodeVO.getNnumber());
					viewList.add(bcVO);
				}
			} else if (null == m_voBill.getItemVOs()[iCurFixLine].getSpecailBarCodeVOs()
					&& null != m_voBill.getItemVOs()[iCurFixLine].getSpecailBarCode1VOs()){
				for (SpecailBarCode1VO  specailBarCode1VO : m_voBill.getItemVOs()[iCurFixLine].getSpecailBarCode1VOs()) {
					BarCodeViewVO bcVO = new BarCodeViewVO();
					bcVO.setVbarcode1(specailBarCode1VO.getVbarcode());
					bcVO.setVbarcodesub1(specailBarCode1VO.getVbarcodesub());
					bcVO.setNnumber1(specailBarCode1VO.getNnumber());
					viewList.add(bcVO);
				}
			}else if (null != m_voBill.getItemVOs()[iCurFixLine].getSpecailBarCodeVOs()
					&& null != m_voBill.getItemVOs()[iCurFixLine].getSpecailBarCode1VOs()){
				HashMap<String,SpecailBarCode1VO> hsSpecailBarCode1VO = new HashMap<String,SpecailBarCode1VO>();
				String keyStr = null;
				for (SpecailBarCode1VO  specailBarCode1VO : m_voBill.getItemVOs()[iCurFixLine].getSpecailBarCode1VOs()) {
					keyStr = specailBarCode1VO.getVbarcode();
					if (null != specailBarCode1VO.getVbarcodesub() && !"".equals(specailBarCode1VO.getVbarcodesub()))
						keyStr = keyStr + specailBarCode1VO.getVbarcodesub();
					else
						keyStr = keyStr + "NULL";
					hsSpecailBarCode1VO.put(keyStr, specailBarCode1VO);
				}
				for (SpecailBarCodeVO  specailBarCodeVO : m_voBill.getItemVOs()[iCurFixLine].getSpecailBarCodeVOs()) {
					BarCodeViewVO bcVO = new BarCodeViewVO();
					bcVO.setVbarcode(specailBarCodeVO.getVbarcode());
					bcVO.setVbarcodesub(specailBarCodeVO.getVbarcodesub());
					bcVO.setNnumber(specailBarCodeVO.getNnumber());
					keyStr = specailBarCodeVO.getVbarcode();
					if (null != specailBarCodeVO.getVbarcodesub() && !"".equals(specailBarCodeVO.getVbarcodesub()))
						keyStr = keyStr + specailBarCodeVO.getVbarcodesub();
					else
						keyStr = keyStr + "NULL";
					if (null != hsSpecailBarCode1VO.get(keyStr)){
						bcVO.setVbarcode1(hsSpecailBarCode1VO.get(keyStr).getVbarcode());
						bcVO.setVbarcodesub1(hsSpecailBarCode1VO.get(keyStr).getVbarcodesub());
						bcVO.setNnumber1(hsSpecailBarCode1VO.get(keyStr).getNnumber());
						hsSpecailBarCode1VO.remove(keyStr);
					} else
						viewList.add(bcVO);
				}
				if (0 < hsSpecailBarCode1VO.size()){
					for(String skey : hsSpecailBarCode1VO.keySet()){
						BarCodeViewVO bcVO = new BarCodeViewVO();
						bcVO.setVbarcode1(hsSpecailBarCode1VO.get(skey).getVbarcode());
						bcVO.setVbarcodesub1(hsSpecailBarCode1VO.get(skey).getVbarcodesub());
						bcVO.setNnumber1(hsSpecailBarCode1VO.get(skey).getNnumber());
						viewList.add(bcVO);
					}
				}
			}
			BarCodeViewVO[] barCodeViewVOs = new BarCodeViewVO[viewList.size()];
			viewList.toArray(barCodeViewVOs);
			getDiffViewDlg().setDataVO(barCodeViewVOs);
			getDiffViewDlg().showModal();
		}else
			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec",
			"UPP4008spec-000568")/* @res "没有可显示的条码" */);
		} catch (Exception e) {
			showErrorMessage(e.getMessage());
			handleException(e);
		}
	}	
	/**
	 * 创建人：刘家清 创建时间：2008-7-10 下午08:28:39 创建原因： 结存条码明细显示框
	 * @return
	 */
	private BarCodeViewDlg getOnhandBCViewDlg() {
		if (m_onhandBCViewDlg == null) {
			m_onhandBCViewDlg = new BarCodeViewDlg(this,  nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec",
			"UPP4008spec-000569")/* @res "结存条码明细" */,
					ICConst.BarCodeViewDlgType.ViewOnhandBC);
		}
		return m_onhandBCViewDlg;
	}
	/**
	 * 创建人：刘家清 创建时间：2008-7-10 下午08:29:01 创建原因： 实盘条码明细显示框
	 * @return
	 */
	private BarCodeViewDlg getCountViewDlg() {
		if (m_countBCViewDlg == null) {
			m_countBCViewDlg = new BarCodeViewDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec",
			"UPP4008spec-000570")/* @res "实盘条码明细" */,
					ICConst.BarCodeViewDlgType.ViewCountBC);
		}
		return m_countBCViewDlg;
	}
	/**
	 * 创建人：刘家清 创建时间：2008-7-10 下午08:29:10 创建原因： 条码盘点差异明细显示框
	 * @return
	 */
	private BarCodeViewDlg getDiffViewDlg() {
		if (m_DiffBCViewDlg == null) {
			m_DiffBCViewDlg = new BarCodeViewDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec",
			"UPP4008spec-000571")/* @res "条码盘点差异明细" */,
					ICConst.BarCodeViewDlgType.ViewDiffBC);
		}
		return m_DiffBCViewDlg;
	}

	/**
	 * 李俊:根据V31需求 帐面取数后修改可用。
	 * nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec",
			"UPP4008spec-000571")/* @res "条码盘点差异明细" 
	 * 行仍然允许操作。 允许保存，取消。帐面取数后实际盘点菜单可用。
	 */
	protected void setAfterGetAcNumButn() {
		getBillCardPanel().setEnabled(true);
		m_iMode = BillMode.Update;
		// 行操作放开
		m_billRowMng.setEnabled(true);
		m_boAddRow.setEnabled(true);
		m_boDeleteRow.setEnabled(true);
		m_boInsertRow.setEnabled(true);
		m_boCopyRow.setEnabled(true);
		m_boPasteRow.setEnabled(m_bCopyRow);
		// 保存，取消放开
		m_boSave.setEnabled(true);
		m_boCancel.setEnabled(true);
		m_boCheckNum.setEnabled(true);
		// 盘点可用
		m_fillMng.setEnabled(true);
		if (null == m_iscountflag || !m_iscountflag.booleanValue()){
			m_boFillNum.setEnabled(true);
			
		}else{
			m_boFillNum.setEnabled(false);
	
		}

		if (m_aryButtonGroup != null) {
			updateButtons();
		}
	}

	/**
	 * 设置新增和修改时的按钮状态
	 */
	private void setButtonStateNewUpdate() {

		//联查和打印按钮不可用  add by qinchao 20090112
		m_boJointCheck.setEnabled(false);
		m_PrintMng.setEnabled(false);
		//EndOF
		
		m_addMng.setEnabled(true);
		m_boAdd.setEnabled(false);
		// 修改状态下，盘点选择不可用；盘点机支持可用
		m_boCheckChoose.setEnabled(false);
		m_boCheckInv.setEnabled(true);
		// 修改状态下修改，删除不可用
		m_boChange.setEnabled(false);
		m_boDelete.setEnabled(false);
		// 修改情况下，保存，取消可用
		m_boSave.setEnabled(true);
		m_boCancel.setEnabled(true);
		// 修改情况下行操作可用
		m_billRowMng.setEnabled(true);
		m_boAddRow.setEnabled(true);
		m_boDeleteRow.setEnabled(true);
		m_boInsertRow.setEnabled(true);
		m_boCopyRow.setEnabled(true);
		m_boPasteRow.setEnabled(m_bCopyRow);

		// 修改状态审批不可用
		m_auditMng.setEnabled(false);
		m_boAuditBill.setEnabled(false);
		m_boCancelAudit.setEnabled(false);
		m_boAuditState.setEnabled(false);
		// 修改状态调整不可用
		m_adjustMng.setEnabled(false);
		m_boAdjustGoods.setEnabled(false);
		m_boCancelAdjust.setEnabled(false);
		// 修改状态下查询，打印，切换不可用
		m_boQuery.setEnabled(false);
		m_boPrint.setEnabled(false);
		m_boPreview.setEnabled(false);
		m_boList.setEnabled(false);
		// 修改情况下辅助定位，复制等不可用
		m_assistMng.setEnabled(false);
		m_boLocate.setEnabled(false);
		m_boCopyBill.setEnabled(false);
		// 修改状态下帐面取数可用
		m_boGetAccNumber.setEnabled(true);
		m_boGetBCAccNumber.setEnabled(false);
		if (null != m_iscountflag && m_iscountflag.booleanValue() && m_iMode != BillMode.List){
			m_boViewOnhandBC.setEnabled(true);
			m_boViewCountBC.setEnabled(true);
			m_boViewDiffBC.setEnabled(true);
		}
		else{
			m_boViewOnhandBC.setEnabled(false);
			m_boViewCountBC.setEnabled(false);
			m_boViewDiffBC.setEnabled(false);
		}
		// modify by liuzy 2007-08-31 NCdp200118965
		// 新增状态下预览按钮不可用
		m_boPreview.setEnabled(false);
		
		m_fillMng.setEnabled(true);
		m_boCheckNum.setEnabled(true);
		//m_boFillNum.setEnabled(false);
		if (null != m_iscountflag && m_iscountflag.booleanValue() &&(m_iMode == BillMode.New || m_iMode == BillMode.Update))
			m_boCountBCImport.setEnabled(true);
		else
			m_boCountBCImport.setEnabled(false);

		if (null != m_utfBarCode)
			m_utfBarCode.setEnabled(true);
	}

	/**
	 * 设置浏览状态的按钮状态
	 * 
	 */
	private void setButtonStateBrowse() {
		m_addMng.setEnabled(true);
		m_boAdd.setEnabled(true);
		m_boCheckChoose.setEnabled(true);
		m_boCheckInv.setEnabled(false);
		
		if (null != m_iscountflag && m_iscountflag.booleanValue()&& m_iMode != BillMode.List){
			m_boViewOnhandBC.setEnabled(true);
			m_boViewCountBC.setEnabled(true);
			m_boViewDiffBC.setEnabled(true);
		}
		else{
			m_boViewOnhandBC.setEnabled(false);
			m_boViewCountBC.setEnabled(false);
			m_boViewDiffBC.setEnabled(false);
		}

		if (m_alListData == null || m_alListData.size() < m_iLastSelListHeadRow
				|| m_iLastSelListHeadRow < 0) {

			//联查和打印按钮不可用  add by qinchao 20090112
			m_boJointCheck.setEnabled(false);
			m_PrintMng.setEnabled(false);
			//EndOF
			m_boChange.setEnabled(false);
			m_boDelete.setEnabled(false);
			m_adjustMng.setEnabled(false);
			m_auditMng.setEnabled(false);
			m_boAuditBill.setEnabled(false);
			m_boAdjustGoods.setEnabled(false);
			m_boCancelAudit.setEnabled(false);
			m_boCancelAdjust.setEnabled(false);
			m_boGetAccNumber.setEnabled(false);
			m_boGetBCAccNumber.setEnabled(false);
			if (null != m_iscountflag && m_iscountflag.booleanValue()&& m_iMode != BillMode.List){
				m_boViewOnhandBC.setEnabled(true);
				m_boViewCountBC.setEnabled(true);
				m_boViewDiffBC.setEnabled(true);
			}
			else{
				m_boViewOnhandBC.setEnabled(false);
				m_boViewCountBC.setEnabled(false);
				m_boViewDiffBC.setEnabled(false);
			}
			m_fillMng.setEnabled(false);
			m_boCheckNum.setEnabled(true);
			m_boFillNum.setEnabled(false);
			if (null != m_iscountflag && m_iscountflag.booleanValue()&&(m_iMode == BillMode.New || m_iMode == BillMode.Update))
				m_boCountBCImport.setEnabled(true);
			else
				m_boCountBCImport.setEnabled(false);
			m_boCalculate.setEnabled(false);
			// 帐面取数不可可用
			m_boGetAccNumber.setEnabled(false);

		} else {
			//联查和打印按钮不可用  add by qinchao 20090112
			m_boJointCheck.setEnabled(true);
			m_PrintMng.setEnabled(true);
			//EndOF
			m_adjustMng.setEnabled(true);
			m_auditMng.setEnabled(true);
			m_boCountBCImport.setEnabled(false);
			// 已经审批或调整，调整后不能帐面取数
			if (isAudited() || isAdjusted()) {
				m_boChange.setEnabled(false);
				m_boDelete.setEnabled(false);
				m_boAuditBill.setEnabled(false);
				m_fillMng.setEnabled(false);
				m_boCheckNum.setEnabled(true);
				m_boFillNum.setEnabled(false);
				if (null != m_iscountflag && m_iscountflag.booleanValue()&&(m_iMode == BillMode.New || m_iMode == BillMode.Update))
					m_boCountBCImport.setEnabled(true);
				else
					m_boCountBCImport.setEnabled(false);
				m_boCalculate.setEnabled(false);
				// 调整
				if (isAdjusted()) {

					m_boAdjustGoods.setEnabled(false);
					m_boCancelAudit.setEnabled(false);
					m_boCancelAdjust.setEnabled(true);
					// 调整后不能帐面取数
					m_boGetAccNumber.setEnabled(false);
					m_boGetBCAccNumber.setEnabled(false);
					
				} else {
					m_boCancelAudit.setEnabled(true);
					m_boAdjustGoods.setEnabled(true);
					m_boCancelAdjust.setEnabled(false);
					// 审批后不能帐面取数
					m_boGetAccNumber.setEnabled(false);
					m_boGetBCAccNumber.setEnabled(false);
				}

			}
			// 非调整和审批状态
			else {
				m_adjustMng.setEnabled(false);
				m_boAuditBill.setEnabled(true);
				m_boCancelAudit.setEnabled(false);
				m_boAdjustGoods.setEnabled(false);
				m_boCancelAdjust.setEnabled(false);
				m_boDelete.setEnabled(true);
				m_fillMng.setEnabled(true);
				m_boCheckNum.setEnabled(true);
				if (null == m_iscountflag || !m_iscountflag.booleanValue()){
					m_boFillNum.setEnabled(true);
					
				}else{
					m_boFillNum.setEnabled(false);
				
				}
				m_boCalculate.setEnabled(true);
				// 帐面取数可用
				m_boGetAccNumber.setEnabled(true);
				if (null != m_iscountflag && m_iscountflag.booleanValue())
					m_boGetBCAccNumber.setEnabled(true);
				else
					m_boGetBCAccNumber.setEnabled(false);
				m_boChange.setEnabled(true);
			}

		}
		m_boSave.setEnabled(false);
		m_boCancel.setEnabled(false);
		// 浏览情况下行操作不能用
		m_billRowMng.setEnabled(false);
		m_boAddRow.setEnabled(false);
		m_boDeleteRow.setEnabled(false);
		m_boInsertRow.setEnabled(false);
		m_boCopyRow.setEnabled(false);
		m_boPasteRow.setEnabled(false);
		// 查询打印等可用
		m_boQuery.setEnabled(true);
		m_boPrint.setEnabled(true);
		m_boPreview.setEnabled(true);
		m_boList.setEnabled(true);
		m_boAuditState.setEnabled(true);
		m_assistMng.setEnabled(true);
		m_boCopyBill.setEnabled(m_iTotalListHeadNum > 0);
		if (m_iMode != BillMode.List) {
			if ((getBillCardPanel().getHeadItem("vbillcode").getValue() == null)
					|| (getBillCardPanel().getHeadItem("vbillcode").getValue()
							.length() == 0))
				m_boCopyBill.setEnabled(false);
		}
		
		if (null != m_utfBarCode)
			m_utfBarCode.setEnabled(false);
	}

	private void setButtonStateList() {
		m_addMng.setEnabled(false);
		m_boAdd.setEnabled(true);
		m_boCheckChoose.setEnabled(true);
		m_boCheckInv.setEnabled(false);
		if (m_iTotalListHeadNum > 0) {
			//联查和打印按钮不可用  add by qinchao 20090112
			m_boJointCheck.setEnabled(true);
			m_PrintMng.setEnabled(true);
			//EndOF
			m_boChange.setEnabled(!(isAudited()) && !(isAdjusted()));
			m_boDelete.setEnabled(!(isAudited()) && !(isAdjusted()));
		} else {
			//联查和打印按钮不可用  add by qinchao 20090112
			m_boJointCheck.setEnabled(false);
			m_PrintMng.setEnabled(false);
			//EndOF
			m_boChange.setEnabled(false);
			m_boDelete.setEnabled(false);

		}
		m_boCopyBill
				.setEnabled(m_iTotalListHeadNum > 0
						&& getBillListPanel().getHeadTable().getSelectedRows().length == 1);
		m_boSave.setEnabled(false);
		m_boCancel.setEnabled(false);

		m_billRowMng.setEnabled(false);
		m_boAddRow.setEnabled(false);
		m_boDeleteRow.setEnabled(false);
		m_boInsertRow.setEnabled(false);
		m_boCopyRow.setEnabled(false);
		m_boPasteRow.setEnabled(false);

		m_auditMng.setEnabled(false);
		m_boAuditBill.setEnabled(false);
		m_boCancelAudit.setEnabled(false);
		m_boAuditState.setEnabled(false);
		

		m_boGetAccNumber.setEnabled(false);
		m_boGetBCAccNumber.setEnabled(false);
		
		if (null != m_iscountflag && m_iscountflag.booleanValue()&& m_iMode != BillMode.List){
			m_boViewOnhandBC.setEnabled(true);
			m_boViewCountBC.setEnabled(true);
			m_boViewDiffBC.setEnabled(true);
		}
		else{
			m_boViewOnhandBC.setEnabled(false);
			m_boViewCountBC.setEnabled(false);
			m_boViewDiffBC.setEnabled(false);
		}

		m_fillMng.setEnabled(true);
		m_boCheckNum.setEnabled(true);
		m_boFillNum.setEnabled(false);
		if (null != m_iscountflag && m_iscountflag.booleanValue()&&(m_iMode == BillMode.New || m_iMode == BillMode.Update))
			m_boCountBCImport.setEnabled(true);
		else
			m_boCountBCImport.setEnabled(false);

		m_boCalculate.setEnabled(false);
		m_adjustMng.setEnabled(false);
		m_boAdjustGoods.setEnabled(false);
		m_boCancelAdjust.setEnabled(false);

		m_boQuery.setEnabled(true);
		m_boLocate.setEnabled(true);
		m_boPrint.setEnabled(true);
		m_boPreview.setEnabled(true);
		m_boList.setEnabled(true);
	}

	/**
	 * 其它状态下的按钮控制，在基本浏览，修改，列表等状态后调用
	 */
	private void setButtonStateOther() {

		// 只有在实盘录入和帐面取数状态后审批按纽才可以使用
		m_boAuditBill.setEnabled(false);
		m_boCancelAudit.setEnabled(false);
		m_boChange.setEnabled(false);
		m_boCommit.setEnabled(false);
		m_adjustMng.setEnabled(false);
		if (m_voBill == null)
			m_fillMng.setEnabled(false);
		else
			m_fillMng.setEnabled(true);
		m_boCheckNum.setEnabled(true);
		m_boFillNum.setEnabled(false);
		m_boAdjustGoods.setEnabled(false);
		m_boCancelAdjust.setEnabled(false);
		//m_boCountBCImport.setEnabled(false);
		setBodyMenuShow(true);

		SpecialBillVO sbvotemp = null;
		String sFlag = "";
		Integer iFlag = null;
		if (m_alListData != null && m_alListData.size() > 0
				&& m_iLastSelListHeadRow != -1) {
			sbvotemp = (SpecialBillVO) m_alListData.get(m_iLastSelListHeadRow);
			iFlag = ((SpecialBillHeaderVO) sbvotemp.getParentVO())
					.getFbillFlag();
			if (iFlag != null)
				sFlag = iFlag.toString();
		}
		// 第一增加状态，有可能取的是上次的单据状态，所以直接绕过下面的判断
		if (!m_bBillInit && m_alListData != null && m_alListData.size() > 0) {

			if (sbvotemp != null && sbvotemp.getParentVO() != null) {
				
				m_fillMng.setEnabled(true);

				if (iFlag != null) {

					// 审批中
					if (sFlag.equals(nc.vo.ic.pub.bill.BillStatus.APPROVEDING)) {
						m_boDelete.setEnabled(false);
						m_boChange.setEnabled(false);

						m_boAuditBill.setEnabled(true);
						m_boCancelAudit.setEnabled(true);
						m_boCommit.setEnabled(false);

						m_fillMng.setEnabled(false);
						m_boCheckNum.setEnabled(false);
						m_boFillNum.setEnabled(false);
						m_boCountBCImport.setEnabled(false);
						// 审批中不能帐面取数
						m_boGetAccNumber.setEnabled(false);
						m_boGetBCAccNumber.setEnabled(false);

						setBodyMenuShow(false);

					} // 帐面取数
					else if (sFlag
							.equals(nc.vo.ic.pub.bill.BillStatus.CHECKING)) {

						if (iSaveFlag == 1)
							m_boChange.setEnabled(true);
						else
							// 帐面取数时相当于修改,故修改不可用
							m_boChange.setEnabled(false);
						// 不能审批
						// m_boAuditBill.setEnabled(false);

						m_fillMng.setEnabled(true);
						m_boCheckNum.setEnabled(true);
						if (null == m_iscountflag || !m_iscountflag.booleanValue()){
							m_boFillNum.setEnabled(true);
					
						}else{
							m_boFillNum.setEnabled(false);
			
						}

					}
					// 实盘录入
					else if (sFlag
							.equals(nc.vo.ic.pub.bill.BillStatus.CHECKINPUT)) {

						m_boChange.setEnabled(false);

						m_boAuditBill.setEnabled(true);
						m_boCancelAudit.setEnabled(false);
						m_fillMng.setEnabled(true);
						m_boCommit.setEnabled(true);
						m_boCheckNum.setEnabled(true);
						if (null == m_iscountflag || !m_iscountflag.booleanValue()){
							m_boFillNum.setEnabled(true);
						
						}else{
							m_boFillNum.setEnabled(false);
			
						}
						// 可帐面取数
						m_boGetAccNumber.setEnabled(true);
						m_boGetBCAccNumber.setEnabled(false);

					} else if (sFlag
							.equals(nc.vo.ic.pub.bill.BillStatus.APPROVED)) {
						m_boChange.setEnabled(false);
						m_boAuditBill.setEnabled(false);
						m_boCancelAudit.setEnabled(true);
						m_boCommit.setEnabled(false);
						m_fillMng.setEnabled(false);
						m_boCheckNum.setEnabled(false);
						m_boFillNum.setEnabled(false);
						m_boCountBCImport.setEnabled(false);
						m_boGetBCAccNumber.setEnabled(false);
						m_boGetAccNumber.setEnabled(false);
						m_boDelete.setEnabled(false);
						if (m_iMode == BillMode.Browse){
							m_adjustMng.setEnabled(true);
							if(!isAdjusted())
								m_boAdjustGoods.setEnabled(true);
							else
								m_boCancelAdjust.setEnabled(true);
						}
						// 不可使用行编辑的功能
						setBodyMenuShow(false);
					}

				}
			}
		}

		// 第一次帐面取数，在新增时候，实盘可用
		if (m_bBillInit == true && isChecking()) {
			if (iSaveFlag == 1)
				m_boChange.setEnabled(true);
			else
				m_boChange.setEnabled(false);
			if (sFlag
					.equals(nc.vo.ic.pub.bill.BillStatus.CHECKINPUT)){
				m_boAuditBill.setEnabled(true);
				m_boCommit.setEnabled(true);
			}
	
			m_boCheckNum.setEnabled(true);
			m_fillMng.setEnabled(true);
			if (null == m_iscountflag || !m_iscountflag.booleanValue()){
				m_boFillNum.setEnabled(true);
			
			}else{
				m_boFillNum.setEnabled(false);
			
			}
		}
		// 调整后帐面取数不可用
		if (isAdjusted()){
			m_boGetAccNumber.setEnabled(false);
			m_boGetBCAccNumber.setEnabled(false);
			m_fillMng.setEnabled(false);
			m_boCheckNum.setEnabled(false);
			m_boFillNum.setEnabled(false);
			m_boCountBCImport.setEnabled(false);
		}

		// v35 审批按钮状态： 表体行存在差异数量，且m_iMode=BillMode.Browse,且当前单据不是调整和审批完成状态，
		// 审批按钮可用，否则不能用
		/*if (m_iMode == BillMode.Browse
				&& !sFlag.equals(nc.vo.ic.pub.bill.BillStatus.SIGNED)
				&& !isAdjusted()) {
			m_boAuditBill.setEnabled(true);
		} else
			m_boAuditBill.setEnabled(false);*/

		// v35 弃审后可以修改
		if (m_iMode == BillMode.Browse
				&& !sFlag.equals(nc.vo.ic.pub.bill.BillStatus.ADJUST)
				&& !sFlag.equals(nc.vo.ic.pub.bill.BillStatus.SIGNED)) {
			if (sFlag.trim().length() > 0) // 打开接点时修改不能编辑
				m_boChange.setEnabled(true);
			else
				m_boChange.setEnabled(false);

			if (m_iMode == BillMode.List) {
				m_boChange.setEnabled(false);
			}
		}

	}

	/**
	 * 此处插入方法说明。 创建日期：(2001-4-30 13:58:35)
	 */
	protected void setButtonState() {

		switch (m_iMode) {
		case BillMode.Update: // 修改
		case BillMode.New: // 新增
			setButtonStateNewUpdate();
			setPageBtnStatus(0, 0);
			if (null != m_pageBtn)
				m_pageBtn.setPageBtnVisible(true);
			break;
		case BillMode.Browse: // 浏览
			setButtonStateBrowse();
			if (null != m_alListData)
				setPageBtnStatus(m_alListData.size(), m_iLastSelListHeadRow);
			if (null != m_pageBtn)
				m_pageBtn.setPageBtnVisible(true);
			break;
		case BillMode.List: // 列表状态
			setButtonStateList();
			setPageBtnStatus(0, 0);
			if (null != m_pageBtn)
				m_pageBtn.setPageBtnVisible(false);
			break;
		}
		// 其它情况
		setButtonStateOther();

		if (m_aryButtonGroup != null) {
			updateButtons();
		}
	}

	/**
	 * 帐面数量和实际数量不符合时，不能进行绝大部分操作，只能打印和重新查询
	 */
	protected void setButtonStatusAccYN() {
		m_addMng.setEnabled(false);
		m_fillMng.setEnabled(true);
		m_boCheckNum.setEnabled(true);
		m_boFillNum.setEnabled(false);
		if (null != m_iscountflag && m_iscountflag.booleanValue()&&(m_iMode == BillMode.New || m_iMode == BillMode.Update))
			m_boCountBCImport.setEnabled(true);
		else
			m_boCountBCImport.setEnabled(false);
		
		m_boChange.setEnabled(false);
		m_boDelete.setEnabled(false);
		m_boSave.setEnabled(false);
		m_boCancel.setEnabled(false);

		m_billRowMng.setEnabled(false);

		m_auditMng.setEnabled(false);
		m_adjustMng.setEnabled(false);

		m_boQuery.setEnabled(true);

		m_PrintMng.setEnabled(true);
		// 切换不能用
		m_boList.setEnabled(false);
		m_assistMng.setEnabled(false);

		m_boGetAccNumber.setEnabled(false);
		m_boGetBCAccNumber.setEnabled(false);
		if (m_aryButtonGroup != null) {
			updateButtons();
		}
	}

	/**
	 * 返回 BillCardPanel1 特性值 。 * @ return nc.ui.pub.bill.BillCardPanel
	 */
	/* 警告：此方法将重新生成。 */
	protected nc.ui.pub.bill.BillCardPanel getBillCardPanel() {
		if (ivjBillCardPanel == null) {
			BillTempletVO billtempletVO = BillUIUtil.getDefaultTempletStatic("40081017", "4R", 
					getClientEnvironment().getUser().getPrimaryKey(), getCorpPrimaryKey(), null, null);
			BillData bd = new BillData(billtempletVO);
//			BillData bd = super.getBillCardPanel().getBillData();
			// 第一次使用时，初始化货位参照。
			if (bd != null) {
				if (bd.getBodyItem("cspacename") != null)
					bd.getBodyItem("cspacename").setComponent(
							getLocatorRefPane());

				// 置入数据源
				super.getBillCardPanel().setBillData(bd);
				// 盘点单设置成排序 add by hanwei 2003-12-24
			}
		}
		return super.getBillCardPanel();
	}

	/**
	 * 新增 创建日期：(2001-4-18 19:45:17)
	 */
	public void onAdd() { // finished

		// 默认只增加一行
		m_iFirstAddRows = 1;
		super.onAdd();
		
		m_utfBarCode.setCurBillItem(null);

		getBillCardPanel().getBillData().getHeadItem("coutwarehouseid")
				.setEdit(true);
		// 默认不是导入数据 add by hanwei 2003-10-30
		m_bIsImportData = false;

	}

	/**
	 * 此处插入方法说明。 创建日期：(2003-12-22 17:39:03)
	 */
	public void onCancel() {

		super.onCancel();
		setBillCheckInputStatus(nc.vo.ic.pub.bill.BillStatus.FREE);
		m_bBillInit = false;

	}
	
	public void clearBCOnCancel() {
		if (getM_voBill() != null)
			m_utfBarCode.setRemoveBarcode(getM_voBill().getItemVOs());
	}

	/**
	 * 修改 创建日期：(2001-4-18 19:45:39)
	 */
	public void onChange() { // finished

		onChange(false);
		m_iscountflag = m_voBill.getHeaderVO().getBccountflag();
		if (m_utfBarCode != null)
			m_utfBarCode.setCurBillItem(m_voBill.getItemVOs());
	}

	/**
	 * 创建者：仲瑞庆 功能： 参数： 返回： 例外： 日期：(2001-5-10 下午 4:15) 修改日期，修改人，修改原因，注释标志：
	 */
	public void onCopyBill() {

		super.onCopyBill();
		m_utfBarCode.setCurBillItem(null);
		getBillCardPanel().getBillData().getHeadItem("coutwarehouseid")
				.setEdit(false);
		m_voBill.setHeaderValue("fbillflag", nc.vo.ic.pub.bill.BillStatus.FREE);
	}

	/**
	 * 创建者：仲瑞庆 功能：查询 参数： 返回： 例外： 日期：(2001-5-10 下午 2:57) 修改日期，修改人，修改原因，注释标志：
	 */
	public void onQuery() {

	  try{
      QryConditionVO qcvo = getQueryHelp().getQryConditionVOForQuery(false);
  		if (qcvo!=null) {
  
//  			nc.vo.pub.query.ConditionVO[] voCons = getConditionDlg()
//  					.getConditionVO();
//  			// 处理单据状态，将其转化为SQL条件。
//  			voCons = packConditionVO(voCons);
//  			// 处理包含的情况。
//  			resetConditionVO(voCons);
//  			// 处理表体的附加查询，如是否帐实相符
  		    m_iBqrybalrec = getQueryHelp().getBalrec();
//  
//  			// 得到查询条件。
//  			sWhereClause = getExtenWhere(voCons);
//  
//  			QryConditionVO qcvo = new nc.vo.ic.pub.bill.QryConditionVO();
//  			if (sWhereClause.length() != 0) {
//  				qcvo.setQryCond("head.cbilltypecode='" + m_sBillTypeCode + "' "
//  						+ sWhereClause);
//  			} else {
//  				qcvo.setQryCond("head.cbilltypecode='" + m_sBillTypeCode + "'");
//  			}
//  			qcvo.setParam(QryConditionVO.QRY_CONDITIONVO, getConditionDlg()
//  					.getConditionVO());
  			qcvo.setIntParam(0, SpecialBillVO.QRY_HEAD_ONLY_PURE);
  			if (m_iBqrybalrec == 2)
  				qcvo
  						.setStrParam(1,
  								"and isnull(body.naccountnum,0)<>isnull(body.nchecknum,0) ");
  
  			loadBillListPanel(qcvo);
  
  			setButtonState();
  			setBillState();
  
  			if (m_iTotalListHeadNum > 0)
  				showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
  						"4008spec",
  						"UPP4008spec-000123",
  						null,
  						new String[] { (new Integer(m_iTotalListHeadNum))
  								.toString() })/* @res "共查到{0}张单据！" */);// +
  			// m_iTotalListHeadNum
  			// +
  			// "张单据！");
  			else
  				showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
  						"4008spec", "UPP4008spec-000124")/*
  															 * @res
  															 * "未查到符合条件的单据。"
  															 */);
  			m_bBillInit = false;
  
  			// 如果查询条件中包含”帐实是否相符“为否的条件，则禁止使用大部分菜单（截面数据和实际数据不一样）
  			if (m_iBqrybalrec == 2) {
  				setButtonStatusAccYN();
  				showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
  						"4008spec", "UPP4008spec-000548")/*
  															 * @res
  															 * "请重新查询帐实相符的记录进行后续操作！"
  															 */);
  			}
  
  		}
  		showBtnSwitch();
    }catch(Exception e){
      GenMethod.handleException(this, null, e);
    }
	}

	/**
	 * 保存 创建日期：(2001-4-18 19:47:08)
	 */
	public boolean onSave() {
		boolean isAult = false; // 是否已经审批 by hanwei 2003-12-19

		try {
			if (m_iLastSelListHeadRow < 0) { // 未选择任何表，直接新增时；或者查询结果为空时；会发生
				m_iLastSelListHeadRow = 0; // 最后选中的列表表头行
				m_iTotalListHeadNum = 0; // 列表表头目前存在的行数
			}

			long ITimeAll = System.currentTimeMillis();
			long ITime = System.currentTimeMillis();
			// 填入界面中数据
			getBillCardPanel().tableStopCellEditing();
			getBillCardPanel().stopEditing();

			// 滤掉所有的空行
			filterNullLine();
			if (getBillCardPanel().getRowCount() == 0) {
				showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"4008spec", "UPP4008spec-000184")/* @res "无数据，请重新输入！" */);
				return false;
			}
			// 检查行号的合法性; 该方法应放在过滤空行的后面。
			if (!nc.ui.scm.pub.report.BillRowNo.verifyRowNosCorrect(
					getBillCardPanel(), m_sBillRowNo)) {
				return false;
			}

			SpecialBillVO voNowBill = getBillVO();
			voNowBill.getParentVO().setAttributeValue("cbilltypecode", m_sBillTypeCode);
			// 修改人：刘家清 修改日期：2007-10-30上午10:01:00
			// 修改原因：在保存时一定要更新下备份VO，数据检查有依赖于数据VO的
			m_voBill.setIDItems(voNowBill);

			voNowBill.setHeaderValue("fbillflag", m_voBill
					.getHeaderValue("fbillflag"));
			voNowBill.setHeaderValue("icheckmode", m_voBill
					.getHeaderValue("icheckmode"));
			m_voBill.setHeaderValue("fassistantflag", voNowBill
					.getHeaderValue("fassistantflag"));

			// VO校验
			if (!checkVO()) {
				return false;
			}

			// 如果当前的状态是实际盘点录入,保存动作触发审批流程
			String sActionName = "WRITE";
/*			if (m_voBill.getHeaderValue("fbillflag") != null) {
				Integer iBillflag = (Integer) m_voBill
						.getHeaderValue("fbillflag");
				String sBillflag = iBillflag.toString();
				if (nc.vo.ic.pub.bill.BillStatus.CHECKINPUT
						.equalsIgnoreCase(sBillflag)) {
					sActionName = "SAVE";
				}
			}*/

			int iRowCount = getBillCardPanel().getRowCount();
			SpecialBillItemVO[] voaItem = null;
			// 修改或者保存后返回的小VO
			SMSpecialBillVO voSM = null;

			if (BillMode.New == m_iMode || m_bBillInit == true) {

				ITime = System.currentTimeMillis();
				voNowBill
						.setChildrenVO((SpecialBillItemVO[]) getChangedItemVOs());
				// 写入HVO中BillTypeCode表单类型
				voNowBill.getParentVO().setAttributeValue("cbilltypecode",
						m_sBillTypeCode);
				
				voNowBill.setHeaderValue("fbillflag", m_voBill
						.getHeaderValue("fbillflag"));
				voNowBill.setHeaderValue("icheckmode", m_voBill
						.getHeaderValue("icheckmode"));

				// 重置单据行号zhx 0630:
				if (iRowCount > 0 && voNowBill.getChildrenVO() != null) {
					if (getBillCardPanel().getBodyItem(m_sBillRowNo) != null)
						for (int i = 0; i < iRowCount; i++) {
							voNowBill.setItemValue(i, m_sBillRowNo,
									getBillCardPanel().getBodyValueAt(i,
											m_sBillRowNo));
						}
				}

				SpecialBillItemVO[] itemVOs = voNowBill.getItemVOs();
				SpecialBillItemVO[] m_voitemVOs = m_voBill.getItemVOs();
				for (int i = 0; i < itemVOs.length; i++)
					if (i < m_voitemVOs.length) {
						itemVOs[i]
								.setSpecailBarCodeVOs(m_voitemVOs[i].getSpecailBarCodeVOs());
						itemVOs[i].setSpecailBarCode1VOs(m_voitemVOs[i]
								.getSpecailBarCode1VOs());
						itemVOs[i].setNbarcodenum(m_voitemVOs[i]
								.getNbarcodenum());
					}

				// 保存
				if (m_sCorpID.equals(voNowBill.getVBillCode()))
					voNowBill.setVBillCode(null);
				voNowBill.setHeaderValue("coperatoridnow", m_sUserID);
				ArrayList alsPrimaryKey = (ArrayList) nc.ui.pub.pf.PfUtilClient
						.processAction(sActionName, m_sBillTypeCode,
								m_sLogDate, voNowBill);
				nc.vo.scm.pub.SCMEnv.showTime(ITime, "盘点单后台新增保存：");

				ITime = System.currentTimeMillis();
				if (alsPrimaryKey == null || alsPrimaryKey.size() < 2) {
					nc.vo.scm.pub.SCMEnv.out("return data error.");
					return true;
				}
				// 显示提示信息
				if (alsPrimaryKey.get(0) != null)
					showErrorMessage((String) alsPrimaryKey.get(0));

				voSM = (SMSpecialBillVO) alsPrimaryKey
						.get(alsPrimaryKey.size() - 1);
				// m_voBill.setIDItems(voNowBill);

				// 增加HVO
				m_iLastSelListHeadRow = m_iTotalListHeadNum;
				addBillVO();

				// 保存即审批情况下 by hanwei 2003-12-19
				Integer iIntFlag = (Integer) (voSM.getParentVO()
						.getAttributeValue("fbillflag"));
				if (nc.vo.ic.pub.bill.BillStatus.APPROVED
						.equalsIgnoreCase(iIntFlag.toString())) {
					isAult = true;
				}

				m_voBill.setIDItems(voNowBill);

				nc.vo.scm.pub.SCMEnv.showTime(ITime, "盘点单新增保存后界面设置时间：");

			} else if (BillMode.Update == m_iMode) {
				ITime = System.currentTimeMillis();
				// 从界面中获得需要的数据
				voNowBill = getUpdatedBillVO();
				//设置
				if (getBillCardPanel().getTailItem("clastmodiid") != null)
				getBillCardPanel().setTailItem("clastmodiid", m_sUserID);
			   if (getBillCardPanel().getTailItem("clastmodiname") != null)
				 getBillCardPanel().setTailItem("clastmodiname", m_sUserName);
				m_voBill.setHeaderValue("clastmodiid", m_sUserID);
				m_voBill.setHeaderValue("clastmodiname", m_sUserName);
				voNowBill.setHeaderValue("clastmodiid", m_sUserID);
				voNowBill.setHeaderValue("clastmodiname", m_sUserName);
				voNowBill.setHeaderValue("fbillflag", m_voBill.getHeaderValue("fbillflag"));
				voNowBill.setHeaderValue("icheckmode", m_voBill.getHeaderValue("icheckmode"));
				// 写入HVO中BillTypeCode表单类型
				voNowBill.getParentVO().setAttributeValue("cbilltypecode",
						m_sBillTypeCode);
				voNowBill.setHeaderValue("fbillflag", m_voBill
						.getHeaderValue("fbillflag"));
				voNowBill.setHeaderValue("icheckmode", m_voBill
						.getHeaderValue("icheckmode"));
				m_voBill.setHeaderValue("fassistantflag", voNowBill
						.getHeaderValue("fassistantflag"));
				if (null == voNowBill) {
					return false;
				}
				// 向m_shvoBillSpecialHVO中写入FreeVO中增加的值
				voNowBill
						.setChildrenVO((SpecialBillItemVO[]) getChangedItemVOs());
				// //临时表体VO，全部HIVO
				// SpecialBillItemVO[] voaTempItem = (SpecialBillItemVO[])
				// getBillVO()
				// .getChildrenVO();
				// //得到当前的ItemVO
				// voaItem = (SpecialBillItemVO[]) voNowBill.getChildrenVO();
				// //临时HVO[]
				// SpecialBillVO[] m_voTempBill = new SpecialBillVO[1];
				// m_voTempBill[0] = voNowBill;

				// 重置单据行号zhx 0630:
				if (iRowCount > 0 && voNowBill.getChildrenVO() != null) {
					if (getBillCardPanel().getBodyItem(m_sBillRowNo) != null)
						for (int i = 0; i < iRowCount; i++) {
							voNowBill.setItemValue(i, m_sBillRowNo,
									getBillCardPanel().getBodyValueAt(i,
											m_sBillRowNo));
						}
				}

				SpecialBillItemVO[] itemVOs = voNowBill.getItemVOs();
				SpecialBillItemVO[] m_voitemVOs = m_voBill.getItemVOs();
				for (int i = 0; i < itemVOs.length; i++)
					if (i < m_voitemVOs.length) {
						itemVOs[i]
								.setSpecailBarCodeVOs(m_voitemVOs[i].getSpecailBarCodeVOs());
						itemVOs[i].setSpecailBarCode1VOs(m_voitemVOs[i]
								.getSpecailBarCode1VOs());
						itemVOs[i].setNbarcodenum(m_voitemVOs[i]
								.getNbarcodenum());
					}

				m_alListData.set(m_iLastSelListHeadRow, m_voBill.clone());
				voNowBill.setHeaderValue("coperatoridnow", m_sUserID);
				ArrayList alsPrimaryKey = (ArrayList) nc.ui.pub.pf.PfUtilClient
						.processAction(sActionName, m_sBillTypeCode,
								m_sLogDate, voNowBill,
								((SpecialBillVO) m_alListData
										.get(m_iLastSelListHeadRow)).clone());

				nc.vo.scm.pub.SCMEnv.showTime(ITime, "盘点单修改保存后台：");

				ITime = System.currentTimeMillis();

				if (alsPrimaryKey == null || alsPrimaryKey.size() < 2) {
					nc.vo.scm.pub.SCMEnv.out("return data error.");
					return true;
				} // 显示提示信息
				if (alsPrimaryKey.get(0) != null)
					showErrorMessage((String) alsPrimaryKey.get(0));

				voSM = (SMSpecialBillVO) alsPrimaryKey
						.get(alsPrimaryKey.size() - 1);

				// //同步最大化VO
				m_voBill.setIDItems(voNowBill);
				// m_voBill=voNowBill;
				// 修改HVO
				m_alListData.set(m_iLastSelListHeadRow, m_voBill.clone());
				// 保存即审批情况下 by hanwei 2003-12-19
				if (voSM.getParentVO() != null
						&& voSM.getParentVO().getAttributeValue("fbillflag") != null) {
					Integer iIntFlag = (Integer) voSM.getParentVO()
							.getAttributeValue("fbillflag");
					if (nc.vo.ic.pub.bill.BillStatus.APPROVED
							.equalsIgnoreCase(iIntFlag.toString())) {
						isAult = true;
					}
				}

				nc.vo.scm.pub.SCMEnv.showTime(ITime, "盘点单修改保存界面设置：");

			}
			// 将后台信息更新到界面
			freshVOBySmallVO(voSM);

			// 2005-01-28 自由项变色龙。
			InvAttrCellRenderer ficr = new InvAttrCellRenderer();
			ficr.setFreeItemRenderer(getBillCardPanel(), null);

			ITime = System.currentTimeMillis();
			nc.vo.scm.pub.SCMEnv.showTime(ITime, "卡片下刷新界面：");

			ITime = System.currentTimeMillis();
			if (isAult)
				setAuditBillFlag();

			// 重显HVO
			m_iMode = BillMode.Browse;
			m_iFirstSelListHeadRow = -1;
			iSaveFlag = 1;
			setButtonState();
			setBillState();
			getBillCardPanel().updateValue();

			nc.vo.scm.pub.SCMEnv.showTime(ITime, "盘点单修改保存界面设置3：");
			nc.vo.scm.pub.SCMEnv.showTime(ITimeAll, "盘点单保存总时间：");
			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"4008spec", "UPP4008spec-000136")/* @res "保存成功！" */);
			// 保存之后单据为非初始状态
			m_bBillInit = false;
			return true;
		} catch (Exception e) {
			showErrorMessage(e.getMessage());
			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"4008spec", "UPP4008spec-000137")/* @res "未能完成，请再做尝试！" */);
			handleException(e);
			return false;
		}
	}



	/**
	 * 创建者：仲瑞庆 功能： 参数： 返回： 例外： 日期：(2001-5-15 下午 3:12) 修改日期，修改人，修改原因，注释标志：
	 */
	public void initButtons() {

		m_addMng = new ButtonObject(nc.ui.ml.NCLangRes.getInstance()
				.getStrByID("common", "UC001-0000002")/* @res "增加" */,
				nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec",
						"UPPSCMCommon-000308")/* @res "增加单据" */, 0, "增加"); /*-=notranslate=-*/
		m_boAdd = new ButtonObject(nc.ui.ml.NCLangRes.getInstance().getStrByID(
				"4008spec", "UPT40081016-000028")/* @res "直接录入" */,
				nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec",
						"UPPSCMCommon-000308")/* @res "增加单据" */, 0, "直接录入"); /*-=notranslate=-*/
		m_boCheckChoose = new ButtonObject(
				nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec",
						"UPT40081016-000035")/* @res "盘点选择" */,
				nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec",
						"UPT40081016-000035")/* @res "盘点选择" */, 0, "盘点选择"); /*-=notranslate=-*/
		m_boCheckInv = new ButtonObject(
				nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec",
						"UPT40081016-000029")/* @res "导入记录" */,
				nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec",
						"UPP4008spec-000508")/* @res "导入盘点机上传的存货记录" */,"导入记录");
		m_boCopyBill = new ButtonObject(nc.ui.ml.NCLangRes.getInstance()
				.getStrByID("common", "UC001-0000043")/* @res "复制" */,
				nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec",
						"UPP4008spec-000505")/* @res "复制单据" */, 0, "复制"); /*-=notranslate=-*/
		m_addMng.addChildButton(m_boAdd);
		m_addMng.addChildButton(m_boCopyBill);
		m_addMng.addChildButton(m_boCheckChoose);
		m_addMng.addChildButton(m_boCheckInv);

		m_boChange = new ButtonObject(nc.ui.ml.NCLangRes.getInstance()
				.getStrByID("common", "UC001-0000045")/* @res "修改" */,
				nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec",
						"UPPSCMCommon-000291")/* @res "修改单据" */, 0, "修改"); /*-=notranslate=-*/
		m_boDelete = new ButtonObject(nc.ui.ml.NCLangRes.getInstance()
				.getStrByID("common", "UC001-0000039")/* @res "删除" */,
				nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec",
						"UPP4008spec-000504")/* @res "删除单据" */, 0, "删除"); /*-=notranslate=-*/

		// m_boJointAdd = new ButtonObject("关联录入", "关联录入", 0);
		m_boSave = new ButtonObject(nc.ui.ml.NCLangRes.getInstance()
				.getStrByID("common", "UC001-0000001")/* @res "保存" */,
				nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
						"UC001-0000001")/* @res "保存" */, 0, "保存"); /*-=notranslate=-*/
		m_boCancel = new ButtonObject(nc.ui.ml.NCLangRes.getInstance()
				.getStrByID("common", "UC001-0000008")/* @res "取消" */,
				nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
						"UC001-0000008")/* @res "取消" */, 0, "取消"); /*-=notranslate=-*/

		m_boGetAccNumber = new ButtonObject(
				nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec",
						"UPT40081016-000036")/* @res "账面取数" */,
				nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec",
						"UPT40081016-000036")/* @res "账面取数" */, 0, "账面取数"); /*-=notranslate=-*/
		
		m_boGetBCAccNumber = new ButtonObject(
				nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec",
						"UPP4008spec-000576")/* @res "条码结存后台取数" */,
				nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec",
						"UPP4008spec-000576")/* @res "条码结存后台取数" */, 0, "条码结存后台取数");

		m_fillMng = new ButtonObject(
				nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec",
						"UPT40081016-000030")/* @res "实盘录入" */,
				nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec",
						"UPP4008spec-000509")/* @res "填写存货实际盘点数量" */, 0,
				"实盘录入"); /*-=notranslate=-*/
		m_boCheckNum = new ButtonObject(
				nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec",
						"UPT40081016-000031")/* @res "手工录入" */,
				nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec",
						"UPP4008spec-000510")/* @res "手工填写存货实际盘点数量" */, 0,
				"手工录入"); /*-=notranslate=-*/
		m_boFillNum = new ButtonObject(
				nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec",
						"UPT40080402-000036")/* @res "自动取数" */,
				nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec",
						"UPP4008spec-000511")/* @res "自动将账面数量填写到盘点数量" */, 0,
				"自动取数"); /*-=notranslate=-*/
		m_boCountBCImport = new ButtonObject(
				nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec",
						"UPP4008spec-000560")/* @res "导入实盘条码" */,
				nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec",
						"UPP4008spec-000560")/* @res "导入实盘条码" */, 0, "导入实盘条码"); /*-=notranslate=-*/
    
    m_boLineCardEdit = new ButtonObject(nc.ui.ml.NCLangRes.getInstance()
        .getStrByID("common", "SCMCOMMONIC55YB002")/* @res "卡片编辑" */,
        nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
            "SCMCOMMONIC55YB002")/* @res "卡片编辑" */, 0, "卡片编辑"); /*-=notranslate=-*/

		m_fillMng.addChildButton(m_boCheckNum);
		m_fillMng.addChildButton(m_boFillNum);
		m_fillMng.addChildButton(m_boCountBCImport);
		m_boCalculate = new ButtonObject(
				nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec",
						"UPP4008spec-000512")/* @res "差异计算" */,
				nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec",
						"UPP4008spec-000513")/* @res "计算账面数量与盘点数量的差值" */, 0,
				"差异计算"); /*-=notranslate=-*/

		m_boQuery = new ButtonObject(nc.ui.ml.NCLangRes.getInstance()
				.getStrByID("common", "UC001-0000006")/* @res "查询" */,
				nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
						"UC001-0000006")/* @res "查询" */, 0, "查询"); /*-=notranslate=-*/
		// 2003-05-03联查
		m_boJointCheck = new ButtonObject(nc.ui.ml.NCLangRes.getInstance()
				.getStrByID("SCMCOMMON", "UPPSCMCommon-000145")/* @res "联查" */,
				nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON",
						"UPPSCMCommon-000145")/* @res "联查" */, 0, "联查"); /*-=notranslate=-*/
		m_PrintMng = new ButtonObject(nc.ui.ml.NCLangRes.getInstance()
				.getStrByID("common", "UC001-0000007")/* @res "打印" */,
				nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
						"UC001-0000007")/* @res "打印" */, 0, "打印管理"); /*-=notranslate=-*/
		m_boPrint = new ButtonObject(nc.ui.ml.NCLangRes.getInstance()
				.getStrByID("common", "UC001-0000007")/* @res "打印" */,
				nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
						"UC001-0000007")/* @res "打印" */, 0, "打印"); /*-=notranslate=-*/
		m_boPreview = new ButtonObject(nc.ui.ml.NCLangRes.getInstance()
				.getStrByID("4008spec", "UPPSCMCommon-000305")/* @res "预览" */,
				nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec",
						"UPPSCMCommon-000305")/* @res "预览" */, 0, "预览"); /*-=notranslate=-*/
		{
			m_PrintMng.addChildButton(m_boPrint);
			m_PrintMng.addChildButton(m_boPreview);
		}
		m_boList = new ButtonObject(nc.ui.ml.NCLangRes.getInstance()
				.getStrByID("4008spec", "UPPSCMCommon-000186")/* @res "切换" */,
				nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec",
						"UPPSCMCommon-000186")/* @res "切换" */, 0, "切换"); /*-=notranslate=-*/

		// m_boOut = new ButtonObject("转出", "转出", 0);
		// m_boIn = new ButtonObject("转入", "转入", 0);

		// m_billMng = new ButtonObject("单据维护", "单据维护操作", 0);

		m_billRowMng = new ButtonObject(nc.ui.ml.NCLangRes.getInstance()
				.getStrByID("common", "UC001-0000011")/* @res "行操作" */,
				nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
						"UC001-0000011")/* @res "行操作" */, 0, "行操作"); /*-=notranslate=-*/
		m_boAddRow = new ButtonObject(nc.ui.ml.NCLangRes.getInstance()
				.getStrByID("common", "UC001-0000012")/* @res "增行" */,
				nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
						"UC001-0000012")/* @res "增行" */, 0, "增行"); /*-=notranslate=-*/
		m_boDeleteRow = new ButtonObject(nc.ui.ml.NCLangRes.getInstance()
				.getStrByID("common", "UC001-0000013")/* @res "删行" */,
				nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
						"UC001-0000013")/* @res "删行" */, 0, "删行"); /*-=notranslate=-*/
		m_boInsertRow = new ButtonObject(nc.ui.ml.NCLangRes.getInstance()
				.getStrByID("common", "UC001-0000016")/* @res "插入行" */,
				nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
						"UC001-0000016")/* @res "插入行" */, 0, "插入行"); /*-=notranslate=-*/
		m_boCopyRow = new ButtonObject(nc.ui.ml.NCLangRes.getInstance()
				.getStrByID("common", "UC001-0000014")/* @res "复制行" */,
				nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
						"UC001-0000014")/* @res "复制行" */, 0, "复制行"); /*-=notranslate=-*/
		m_boPasteRow = new ButtonObject(nc.ui.ml.NCLangRes.getInstance()
				.getStrByID("common", "UC001-0000015")/* @res "粘贴行" */,
				nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
						"UC001-0000015")/* @res "粘贴行" */, 0, "粘贴行"); /*-=notranslate=-*/
		
		m_boPasteRowTail = new ButtonObject(nc.ui.ml.NCLangRes.getInstance()
				.getStrByID("4008bill", "UPP4008bill-000556")/* @res "粘贴行到表尾" */,
				nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill",
						"UPP4008bill-000556")/* @res "粘贴行到表尾" */, 0, "粘贴行到表尾"); /*-=notranslate=-*/
		m_boNewRowNo = new ButtonObject(nc.ui.ml.NCLangRes.getInstance()
				.getStrByID("4008bill", "UPP4008bill-000551")/* @res "重排行号" */,
				nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill",
						"UPP4008bill-000551")/* @res "重排行号" */, 0, "重排行号"); /*-=notranslate=-*/

		m_boBarCode = new ButtonObject(nc.ui.ml.NCLangRes.getInstance()
				.getStrByID("4008spec", "UPP4008spec-000564")/* @res "条形码" */,
				nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
						"UPP4008spec-000564")/* @res "条形码" */, 0, "条形码"); /*-=notranslate=-*/

		m_billRowMng.addChildButton(m_boAddRow);
		m_billRowMng.addChildButton(m_boDeleteRow);
		m_billRowMng.addChildButton(m_boInsertRow);
    m_billRowMng.addChildButton(m_boLineCardEdit);

		m_billRowMng.addChildButton(m_boCopyRow);
		m_billRowMng.addChildButton(m_boPasteRow);
		m_billRowMng.addChildButton(m_boPasteRowTail);
		m_billRowMng.addChildButton(m_boNewRowNo);
		m_billRowMng.addChildButton(m_boBarCode);

		m_auditMng = new ButtonObject(nc.ui.ml.NCLangRes.getInstance()
				.getStrByID("common", "UC001-0000027")/* @res "审批" */,
				nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
						"UC001-0000027")/* @res "审批" */, 0, "审批"); /*-=notranslate=-*/
		m_boAuditBill = new ButtonObject(nc.ui.ml.NCLangRes.getInstance()
				.getStrByID("common", "UC001-0000027")/* @res "审批" */,
				nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
						"UC001-0000027")/* @res "审批" */, 0, "审批"); /*-=notranslate=-*/
		m_boCommit = new ButtonObject(nc.ui.ml.NCLangRes.getInstance()
				.getStrByID("common", "SCMCOMMON000000080")/* @res "送审" */,
				nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
						"SCMCOMMON000000080")/* @res "送审" */, 0, "送审"); /*-=notranslate=-*/
		m_boCancelAudit = new ButtonObject(nc.ui.ml.NCLangRes.getInstance()
				.getStrByID("common", "UC001-0000028")/* @res "弃审" */,
				nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
						"UC001-0000028")/* @res "弃审" */, 0, "弃审"); /*-=notranslate=-*/
		m_boAuditState = new ButtonObject(nc.ui.ml.NCLangRes.getInstance()
				.getStrByID("common", "UC000-0001558")/* @res "审批状态" */,
				nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec",
						"UPP4008spec-000514")/* @res "查询盘点单审批状态" */, 0, "审批状态"); /*-=notranslate=-*/
		m_auditMng.addChildButton(m_boAuditBill);
		m_auditMng.addChildButton(m_boCommit);
		m_auditMng.addChildButton(m_boCancelAudit);
		m_auditMng.addChildButton(m_boAuditState);

		m_adjustMng = new ButtonObject(nc.ui.ml.NCLangRes.getInstance()
				.getStrByID("4008spec", "UPT40081016-000033")/* @res "调整" */,
				nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec",
						"UPT40081016-000033")/* @res "调整" */, 0, "调整"); /*-=notranslate=-*/
		m_boAdjustGoods = new ButtonObject(nc.ui.ml.NCLangRes.getInstance()
				.getStrByID("4008spec", "UPT40081016-000033")/* @res "调整" */,
				nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec",
						"UPT40081016-000033")/* @res "调整" */, 0, "调整"); /*-=notranslate=-*/
		m_boCancelAdjust = new ButtonObject(
				nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec",
						"UPT40081016-000034")/* @res "取消调整" */,
				nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec",
						"UPT40081016-000034")/* @res "取消调整" */, 0, "取消调整"); /*-=notranslate=-*/
		m_adjustMng.addChildButton(m_boAdjustGoods);
		m_adjustMng.addChildButton(m_boCancelAdjust);

		m_assistMng = new ButtonObject(
				nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec",
						"UPT40081016-000032")/* @res "辅助功能" */,
				nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec",
						"UPT40081016-000032")/* @res "辅助功能" */, 0, "辅助功能"); /*-=notranslate=-*/

		m_boLocate = new ButtonObject(nc.ui.ml.NCLangRes.getInstance()
				.getStrByID("4008spec", "UPPSCMCommon-000089")/* @res "定位" */,
				nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec",
						"UPPSCMCommon-000089")/* @res "定位" */, 0, "定位"); /*-=notranslate=-*/
		m_boRowQuyQty = new ButtonObject(
				nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec",
						"UPPSCMCommon-000359")/* @res "存量查询" */,
				nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec",
						"UPPSCMCommon-000359")/* @res "存量查询" */, 0, "存量查询"); /*-=notranslate=-*/

		m_boViewOnhandBC = new ButtonObject(
				nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec",
						"UPP4008spec-000561")/* @res "结存条码明细" */,
				nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec",
						"UPP4008spec-000561")/* @res "结存条码明细" */, 0, "结存条码明细"); /*-=notranslate=-*/

		m_boViewCountBC = new ButtonObject(
				nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec",
						"UPP4008spec-000562")/* @res "实盘条码明细" */,
				nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec",
						"UPP4008spec-000562")/* @res "实盘条码明细" */, 0, "实盘条码明细"); /*-=notranslate=-*/

		m_boViewDiffBC = new ButtonObject(
				nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec",
						"UPP4008spec-000563")/* @res "条码盘点差异明细" */,
				nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec",
						"UPP4008spec-000563")/* @res "条码盘点差异明细" */, 0,
				"条码盘点差异明细"); /*-=notranslate=-*/

		m_assistMng.addChildButton(m_boRowQuyQty);

		m_assistMng.addChildButton(m_boViewOnhandBC);

		m_assistMng.addChildButton(m_boViewCountBC);

		m_assistMng.addChildButton(m_boViewDiffBC);

		// 上下翻页的控制
		m_pageBtn = new PageCtrlBtn(this);
		m_boBrowse = new ButtonObject(nc.ui.ml.NCLangRes.getInstance()
				.getStrByID("common", "UC001-0000021")/* @res "浏览" */,
				nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
						"UC001-0000021")/* @res "浏览" */, 0, "浏览"); /*-=notranslate=-*/

		m_boBrowse.addChildButton(m_pageBtn.getFirst());
		m_boBrowse.addChildButton(m_pageBtn.getPre());
		m_boBrowse.addChildButton(m_pageBtn.getNext());
		m_boBrowse.addChildButton(m_pageBtn.getLast());

		// m_assistMng.addChildButton(m_boLocate);

		m_aryButtonGroup = new ButtonObject[] { m_addMng, m_boChange,
				m_boDelete, m_boSave, m_boCancel, m_billRowMng,
				m_boGetAccNumber,m_boGetBCAccNumber, m_fillMng,
				// m_boCalculate,
				m_auditMng, m_adjustMng, m_boQuery, m_boBrowse,m_boJointCheck, m_PrintMng,
				m_boList, m_assistMng };
	}

	/**
	 * 创建者：仲瑞庆 功能：初始化变量 参数： 返回： 例外： 日期：(2001-5-24 下午 6:27) 修改日期，修改人，修改原因，注释标志：
	 */
	protected void initVariable() {
		m_sBillTypeCode = nc.vo.ic.pub.BillTypeConst.m_check;
		// 调用的各个节点的BO_Client接口,应被子类修改
		// sSpecialHBO_Client = "nc.ui.ic.ic261.SpecialHBO_Client";
		m_sPNodeCode = "40081017";
		// 设置是通过公式来查询:是
		setIsByFormula(true);

	}

	/**
	 * 此处插入方法说明。 创建日期：(2001-3-23 2:02:27)
	 * 
	 * @param e
	 *            ufbill.BillEditEvent
	 */
	public void bodyRowChange(nc.ui.pub.bill.BillEditEvent e) {
		int colnow, rownow;

		getBillCardPanel().rememberFocusComponent();
		// 响应列表形式下表头选择改变事件。
		if (e.getSource() == this.getBillListPanel().getHeadTable()) {
			// 清除定位，2003-07-21 ydy
			clearOrientColor();
			rownow = e.getRow();
			
			if (null != m_alListData){
				SpecialBillVO sbvotemp =
					(SpecialBillVO) m_alListData.get(rownow);
				if (null != sbvotemp)
					m_iscountflag = sbvotemp.getHeaderVO().getBccountflag();
			}

			listSelectionChanged(e);

			// 控制帐面实际是否相符的菜单
			if (m_iBqrybalrec == 2)
				setButtonStatusAccYN();
		}

		if (e.getSource() == this.getBillListPanel().getBodyTable())
			m_iLastSelCardBodyRow = e.getRow();

		if (e.getSource() == this.getBillCardPanel().getBillTable()) {
			rownow = e.getRow();

			m_iLastSelCardBodyRow = rownow;

			// 显示表尾数据
			setTailValue(rownow);

		}
		getBillCardPanel().restoreFocusComponent();
	}

	/**
	 * 判断浏览状态下界面是否存在差异数量
	 * 
	 * @return bRet true:表体行存在差异数量， false: 表体行不存在
	 */
	private String ifExistCYPD(SpecialBillVO voBill) {

		if (voBill == null)
			return null;
		SpecialBillHeaderVO voHead = voBill.getHeaderVO();
		SpecialBillItemVO[] voaItem = voBill.getItemVOs();

		if (voHead == null)
			return null;
		if (voaItem == null || voaItem.length == 0)
			return null;

		ArrayList al = new ArrayList();
		for (int i = 0; i < voaItem.length; i++) {
			if (voaItem[i] == null)
				continue;
			if (voaItem[i].getCysl() != null
					&& voaItem[i].getCysl().doubleValue() != 0) {
				continue;

			}
			if (voaItem[i].getNchecknum() != null
					&& voaItem[i].getNchecknum().doubleValue() != 0) {
				continue;
			}
			if (voaItem[i].getCyfsl() != null
					&& voaItem[i].getCyfsl().doubleValue() != 0) {
				continue;
			}
			//
			String crowno = voaItem[i].getCrowno();
			al.add(crowno);
		}

		if (al.size() > 0) {
			StringBuffer sb = new StringBuffer();
			sb.append("下列行没有帐面数量或者没有盘点数量,或者数量为0,是否审批:");
			for (int x = 0; x < al.size(); x++) {
				if (x == al.size() - 1) {
					sb.append(al.get(x));
					continue;
				}
				sb.append(al.get(x) + ",");
			}
			return sb.toString();
		}

		return null;
	}

	/**
	 * 创建者：仲瑞庆 功能： 参数： 返回： 例外： 日期：(2001-5-10 下午 2:54) 修改日期，修改人，修改原因，注释标志：
	 */
	public void onAuditBill() {

		SpecialBillVO voNow = getBillVO();
		showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec",
				"UPP4008spec-000584")/* @res "提交" */);

		// v35 ljun 审批前检查盘点数量或者帐面数量是否输入
		String msg = ifExistCYPD(voNow);
		if (msg != null) {
			int yesno = showYesNoMessage(msg);
			if (yesno == nc.ui.pub.beans.MessageDialog.ID_NO)
				return;
		}

		// 更新表尾
		try {

			if (voNow == null || voNow.getChildrenVO() == null
					|| voNow.getChildrenVO().length == 0) {
				nc.vo.scm.pub.SCMEnv.out("no data");
				showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"4008spec", "UPP4008spec-000185")/*
															 * @res
															 * "盘点单没有数据，不能审批"
															 */);
				return;
			}
			nc.vo.scm.pub.SCMEnv.out("++++"
					+ voNow.getHeaderVO().getCspecialhid() + "+++");
			voNow.setHeaderValue("cauditorid", m_sUserID);
			voNow.setHeaderValue("coperatoridnow", m_sUserID);
			// 使用审批流
			nc.ui.pub.pf.PfUtilClient.processActionFlow(this, "APPROVE",
					m_sBillTypeCode, m_sLogDate, voNow, null);
			// nc.ui.pub.pf.PfUtilClient.processAction(
			// "APPROVE",
			// m_sBillTypeCode,
			// m_sLogDate,
			// voNow);
			if (nc.ui.pub.pf.PfUtilClient.isSuccess()) {

				// setAuditBillFlag();
				filterNullLine();
				// 单据PK
				String sBillPK = voNow.getPrimaryKey();
				// 刷新ts
				ArrayList alRet = (ArrayList) SpecialBillHelper.queryInfo(
						new Integer(QryInfoConst.BILL_STATUS_TS), sBillPK);

				// qryLastTs(sBillPK);
				String sBillFlag = null;

				if (alRet != null && alRet.size() == 3) {
					sBillFlag = (String) alRet.get(0);
					freshTs((ArrayList) alRet.get(1));
				}
				if (sBillFlag != null) {
					m_voBill.setFbillflagStatus(Integer.parseInt(sBillFlag));
				}
				if (nc.vo.ic.pub.bill.BillStatus.APPROVED
						.equalsIgnoreCase(sBillFlag)) {
					if (getBillCardPanel().getTailItem("cauditorid") != null)
						getBillCardPanel().setTailItem("cauditorid", m_sUserID);
					if (getBillCardPanel().getTailItem("cauditorname") != null)
						getBillCardPanel().setTailItem("cauditorname",
								m_sUserName);
					if (m_voBill != null) {
						m_voBill.setHeaderValue("cauditorid", m_sUserID);
						m_voBill.setHeaderValue("cauditorname", m_sUserName);
					}
				} else {
					if (getBillCardPanel().getTailItem("cauditorid") != null)
						getBillCardPanel().setTailItem("cauditorid", null);
					if (getBillCardPanel().getTailItem("cauditorname") != null)
						getBillCardPanel().setTailItem("cauditorname", null);
					if (m_voBill != null) {
						m_voBill.setHeaderValue("cauditorid", null);
						m_voBill.setHeaderValue("cauditorname", null);
					}
				}
				// 不能修改删除

				// 补充查询：审批中和已经审批都不能修改删除单据

				m_alListData.set(m_iLastSelListHeadRow, m_voBill.clone());

				m_iFirstSelListHeadRow = -1;
				//switchListToBill();
				setButtonState();
				setBillState();
				showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"4008spec", "UPP4008spec-000186")/* @res "审批成功！" */);
			} else
				showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"4008spec", "UPP4008spec-000187")/* @res "审批失败！" */);

		} catch (Exception e) {
			nc.vo.scm.pub.SCMEnv.out("数据通讯失败！");
			handleException(e);
			nc.ui.pub.beans.MessageDialog.showErrorDlg(this,
					nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec",
							"UPPSCMCommon-000059")/* @res "错误" */, e
							.getMessage());
		}
	}
	
	public void onCommit() {

		SpecialBillVO voNow = getBillVO();
		showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec",
				"UPP4008spec-000584")/* @res "提交" */);


		// 更新表尾
		try {

			if (voNow == null || voNow.getChildrenVO() == null
					|| voNow.getChildrenVO().length == 0) {
				nc.vo.scm.pub.SCMEnv.out("no data");
				showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"4008spec", "UPP4008spec-000185")/*
															 * @res
															 * "盘点单没有数据，不能审批"
															 */);
				return;
			}
			nc.vo.scm.pub.SCMEnv.out("++++"
					+ voNow.getHeaderVO().getCspecialhid() + "+++");
			voNow.setHeaderValue("cauditorid", m_sUserID);
			voNow.setHeaderValue("coperatoridnow", m_sUserID);
			// 使用审批流
			/*nc.ui.pub.pf.PfUtilClient.processActionFlow(this, "SAVE",
					m_sBillTypeCode, m_sLogDate, voNow, null);*/
			Object[] retAry = (Object[]) nc.ui.pub.pf.PfUtilClient
			.processAction(IPFACTION.COMMIT, m_sBillTypeCode,
					m_sLogDate, voNow);
			// nc.ui.pub.pf.PfUtilClient.processAction(
			// "APPROVE",
			// m_sBillTypeCode,
			// m_sLogDate,
			// voNow);
			if (nc.ui.pub.pf.PfUtilClient.isSuccess()) {

				// setAuditBillFlag();
				filterNullLine();
				// 单据PK
				String sBillPK = voNow.getPrimaryKey();
				// 刷新ts
				ArrayList alRet = (ArrayList) SpecialBillHelper.queryInfo(
						new Integer(QryInfoConst.BILL_STATUS_TS), sBillPK);

				// qryLastTs(sBillPK);
				String sBillFlag = null;

				if (alRet != null && alRet.size() == 3) {
					sBillFlag = (String) alRet.get(0);
					freshTs((ArrayList) alRet.get(1));
				}
				if (sBillFlag != null) {
					m_voBill.setFbillflagStatus(Integer.parseInt(sBillFlag));
				}
				if (nc.vo.ic.pub.bill.BillStatus.APPROVED
						.equalsIgnoreCase(sBillFlag)) {
					if (getBillCardPanel().getTailItem("cauditorid") != null)
						getBillCardPanel().setTailItem("cauditorid", m_sUserID);
					if (getBillCardPanel().getTailItem("cauditorname") != null)
						getBillCardPanel().setTailItem("cauditorname",
								m_sUserName);
					if (m_voBill != null) {
						m_voBill.setHeaderValue("cauditorid", m_sUserID);
						m_voBill.setHeaderValue("cauditorname", m_sUserName);
					}
				} else {
					if (getBillCardPanel().getTailItem("cauditorid") != null)
						getBillCardPanel().setTailItem("cauditorid", null);
					if (getBillCardPanel().getTailItem("cauditorname") != null)
						getBillCardPanel().setTailItem("cauditorname", null);
					if (m_voBill != null) {
						m_voBill.setHeaderValue("cauditorid", null);
						m_voBill.setHeaderValue("cauditorname", null);
					}
				}
				// 不能修改删除

				// 补充查询：审批中和已经审批都不能修改删除单据

				m_alListData.set(m_iLastSelListHeadRow, m_voBill.clone());

				m_iFirstSelListHeadRow = -1;
				// switchListToBill();
				setButtonState();
				setBillState();
				showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"4008spec", "UPP4008spec-000582")/* @res "提交成功！" */);
			} else
				showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"4008spec", "UPP4008spec-000583")/* @res "提交失败！" */);

		} catch (Exception e) {
			nc.vo.scm.pub.SCMEnv.out("数据通讯失败！");
			handleException(e);
			nc.ui.pub.beans.MessageDialog.showErrorDlg(this,
					nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec",
							"UPPSCMCommon-000059")/* @res "错误" */, e
							.getMessage());
		}
	}


	/**
	 * 创建者：仲瑞庆 功能： 参数： 返回： 例外： 日期：(2001-5-10 下午 2:50) 修改日期，修改人，修改原因，注释标志：
	 */
	public void onCancelAudit() {
		switch (nc.ui.pub.beans.MessageDialog.showYesNoDlg(this, null,
				nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UCH068")/*
																				 * @res
																				 * "是否确定要弃审？"
																				 */
				, MessageDialog.ID_NO)) {

		case nc.ui.pub.beans.MessageDialog.ID_YES:
			break;

		default:
			return;
		}

		showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
				"UCH049")/* @res "正在弃审" */);
		// 更新表尾
		try {
			SpecialBillVO voNow = getBillVO();
			if (voNow == null) {
				nc.vo.scm.pub.SCMEnv.out("no data");
				return;
			}
			voNow.setHeaderValue("cauditorid", m_sUserID);
			voNow.setHeaderValue("coperatoridnow", m_sUserID);
			nc.ui.pub.pf.PfUtilClient.processActionFlow(this, "UNAPPROVE"
					+ m_sUserID, m_sBillTypeCode, m_sLogDate, voNow, null);

			clearAuditBillFlag();
			filterNullLine();
			// 单据PK
			String sBillPK = voNow.getPrimaryKey();
			// 刷新ts
			freshTs(qryLastTs(sBillPK));
			// 设置回实盘录入状态
			setBillCheckInputStatus(nc.vo.ic.pub.bill.BillStatus.CHECKINPUT);
			m_alListData.set(m_iLastSelListHeadRow, m_voBill.clone());

			m_iFirstSelListHeadRow = -1;
			// switchListToBill();

			setButtonState();
			setBillState();

			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"4008spec", "UPP4008spec-000188")/* @res "弃审成功！" */);

		} catch (Exception e) {
			nc.vo.scm.pub.SCMEnv.out("数据通讯失败！");
			handleException(e);
			nc.ui.pub.beans.MessageDialog.showErrorDlg(this,
					nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec",
							"UPPSCMCommon-000059")/* @res "错误" */, e
							.getMessage());
		}
	}

	/**
	 * 检查表体行是否有关键字重复，包含的情况，如果存在，则提示不能输入：
	 * 如，行1关键字为123，行2关键字为12345，这种情况下两行取出的现存量会有交叉；
	 */
	private boolean checkUIKeyRepeat() {
		ArrayList alRepeatNo = StringKeyJudge
				.IsKeyRepeat((SpecialBillItemVO[]) m_voBill.getChildrenVO());
		if (alRepeatNo == null) {
			return true;
		} else {
			// int iRow1 = ((Integer) alRepeatNo.get(0)).intValue();
			// int iRow2 = ((Integer) alRepeatNo.get(1)).intValue();

			String[] args = new String[2];
			args[0] = (String) alRepeatNo.get(0);
			args[1] = (String) alRepeatNo.get(1);
			String sMsg = nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"4008spec", "UPP4008spec-000545", null, args);

			showWarningMessage(sMsg);

			return false;
		}
	}

	/**
	 * 创建者：仲瑞庆 功能： 参数： 返回： 例外： 日期：(2001-5-24 下午 5:17) 修改日期，修改人，修改原因，注释标志：
	 */
	protected boolean checkVO() {
		try {
			String sAllErrorMessage = "";
			// 盘点单导入检查add by hanwei 2003-09-17
			if (m_bIsImportData)
				sAllErrorMessage = sAllErrorMessage + checkImportBodyVO();

			VOCheck.checkNullVO(m_voBill);

			try {
				VOCheck.validate(m_voBill, GeneralMethod
						.getHeaderCanotNullString(getBillCardPanel()),
						GeneralMethod
								.getBodyCanotNullString(getBillCardPanel()));
			} catch (ICNullFieldException e) {
				String sErrorMessage = GeneralMethod.getBodyErrorMessage(
						getBillCardPanel(), e.getErrorRowNums(), e.getHint());
				sAllErrorMessage = sAllErrorMessage + sErrorMessage + "\n";
			} catch (ICHeaderNullFieldException e) {

				String sErrorMessage = GeneralMethod.getHeaderErrorMessage(
						getBillCardPanel(), e.getErrorRowNums(), e.getHint());
				sAllErrorMessage = sAllErrorMessage + sErrorMessage + "\n";
			}
			// 单价>=0检查
			try {
				VOCheck.checkGreaterThanZeroInput(m_voBill.getChildrenVO(),
						"nprice", nc.ui.ml.NCLangRes.getInstance().getStrByID(
								"common", "UC000-0000741")/* @res "单价" */);
			} catch (ICPriceException e) {
				String sErrorMessage = GeneralMethod.getBodyErrorMessage(
						getBillCardPanel(), e.getErrorRowNums(), e.getHint());
				sAllErrorMessage = sAllErrorMessage + sErrorMessage + "\n";
			}
			// 盘点数量>0检查
			try {
				VOCheck.checkGreaterThanZeroInput(m_voBill.getChildrenVO(),
						"nchecknum", nc.ui.ml.NCLangRes.getInstance()
								.getStrByID("40081016", "UPT40081016-000012")/*
																				 * @res
																				 * "盘点数量"
																				 */);
			} catch (ICPriceException e) {
				String sErrorMessage = GeneralMethod.getBodyErrorMessage(
						getBillCardPanel(), e.getErrorRowNums(), e.getHint());
				sAllErrorMessage = sAllErrorMessage + sErrorMessage + "\n";
			}
			// 盘点辅数量>0检查
			try {
				VOCheck.checkGreaterThanZeroInput(m_voBill.getChildrenVO(),
						"ncheckastnum", nc.ui.ml.NCLangRes.getInstance()
								.getStrByID("40081016", "UPT40081016-000013")/*
																				 * @res
																				 * "盘点辅数量"
																				 */);
			} catch (ICPriceException e) {
				String sErrorMessage = GeneralMethod.getBodyErrorMessage(
						getBillCardPanel(), e.getErrorRowNums(), e.getHint());
				sAllErrorMessage = sAllErrorMessage + sErrorMessage + "\n";
			}
			// 调整数量同方向检查
			try {
				VOCheck.checkSamedirect(m_voBill, new String[] { "nadjustnum",
						"nadjustastnum" }, new String[] {
						nc.ui.ml.NCLangRes.getInstance().getStrByID("40081016",
								"UPT40081016-000001")/* @res "调整数量" */,
						nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
								"UC000-0003758") /* @res "调整辅数量" */});
			} catch (NullFieldException e) {
				String sErrorMessage = e.getHint();
				sAllErrorMessage = sAllErrorMessage + sErrorMessage + nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
				"UC000-0003758") /* @res "调整辅数量" */+"\n";
			}
			// 差异数量同方向检查
			try {
				VOCheck.checkSamedirect(m_voBill, new String[] { "cysl",
						"cyfsl" }, new String[] {
						nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
								"UC000-0001698")/* @res "差异数量" */,
						nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
								"UC000-0001701") /* @res "差异辅数量" */});
			} catch (NullFieldException e) {
				String sErrorMessage = e.getHint();
				sAllErrorMessage = sAllErrorMessage + sErrorMessage + nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
				"UC000-0001701") /* @res "差异辅数量" */+"\n";
			}
			// 盘点数量同方向检查
			try {
				VOCheck.checkSameDirection(m_voBill, new String[] {
						"nchecknum", "ncheckastnum" }, new String[] {
						nc.ui.ml.NCLangRes.getInstance().getStrByID("40081016",
								"UPT40081016-000012")/* @res "盘点数量" */,
						nc.ui.ml.NCLangRes.getInstance().getStrByID("40081016",
								"UPT40081016-000013") /* @res "盘点辅数量" */});
			} catch (NullFieldException e) {
				String sErrorMessage = e.getHint();
				sAllErrorMessage = sAllErrorMessage + sErrorMessage + "\n";
			}
			if (sAllErrorMessage.trim().length() != 0) {
				showErrorMessage(sAllErrorMessage);
				return false;
			}

			// 检查当为有货位仓库时表体行应全有货位，无是应全无货位 (V31根据需求，放开货位校验)
			// boolean bIsLocatorWh = false;
			// if (m_voBill.getWhOut() != null
			// && m_voBill.getWhOut().getIsLocatorMgt() != null)
			// bIsLocatorWh = (m_voBill.getWhOut().getIsLocatorMgt()
			// .intValue() == 1);
			// for (int i = 0; i < m_voBill.getItemVOs().length; i++) {
			// if ((m_voBill.getItemValue(i, "cinventoryid") != null)) {
			// if (bIsLocatorWh
			// && m_voBill.getItemValue(i, "cspaceid") != null
			// && m_voBill.getItemValue(i, "cspaceid").toString()
			// .trim().length() != 0
			// || !bIsLocatorWh
			// && (m_voBill.getItemValue(i, "cspaceid") == null || m_voBill
			// .getItemValue(i, "cspaceid").toString()
			// .trim().length() == 0)) {
			// continue;
			// } else {
			// nc.ui.pub.beans.MessageDialog
			// .showErrorDlg(
			// this,
			// nc.ui.ml.NCLangRes.getInstance()
			// .getStrByID("4008spec",
			// "UPPSCMCommon-000059")/*
			// * @res
			// * "错误"
			// */,
			// nc.ui.ml.NCLangRes.getInstance()
			// .getStrByID("4008spec",
			// "UPP4008spec-000189")/*
			// * @res
			// * "表体行货位与表头仓库是否货位管理标志不一致！"
			// */);
			// return false;
			// }
			// }
			// }
			// 检查盘点单，要求表体行没有包含关系
			if(m_voBill.getHeaderVO().getIcheckmode() != CheckMode.md ){
				if (!checkUIKeyRepeat())
					return false;
			}
			
//			if (!checkUIKeyRepeat())
//				return false;

			// 更改颜色为正常颜色
			/*SetColor.SetTableColor(getBillCardPanel().getBillModel(),
					getBillCardPanel().getBillTable(), getBillCardPanel(),
					new ArrayList(), m_cNormalColor, m_cNormalColor,
					m_bExchangeColor, m_bLocateErrorColor, "");*/
			nc.ui.ic.pub.tools.GenMethod.reSetRowColorWhenNOException(getBillCardPanel());
			
			return true;
		} catch (ICDateException e) {
			// 显示提示
			String sErrorMessage = GeneralMethod.getBodyErrorMessage(
					getBillCardPanel(), e.getErrorRowNums(), e.getHint());
			// showHintMessage(sErrorMessage);
			nc.ui.pub.beans.MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes
					.getInstance()
					.getStrByID("4008spec", "UPPSCMCommon-000059")/*
																	 * @res "错误"
																	 */, sErrorMessage);
			// 更改颜色
			/*SetColor.SetTableColor(getBillCardPanel().getBillModel(),
					getBillCardPanel().getBillTable(), getBillCardPanel(), e
							.getErrorRowNums(), m_cNormalColor, e
							.getExceptionColor(), m_bExchangeColor,
					m_bLocateErrorColor, e.getHint());*/
			//SetColor.setErrRowColor(getBillCardPanel().getBillTable(), e.getErrorRowNums());
			nc.ui.ic.pub.tools.GenMethod.setRowColorWhenException(getBillCardPanel(), e);
			return false;
		} catch (ICNullFieldException e) {
			// 显示提示
			String sErrorMessage = GeneralMethod.getBodyErrorMessage(
					getBillCardPanel(), e.getErrorRowNums(), e.getHint());
			nc.ui.pub.beans.MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes
					.getInstance()
					.getStrByID("4008spec", "UPPSCMCommon-000059")/*
																	 * @res "错误"
																	 */, sErrorMessage);
			// 更改颜色
/*			SetColor.SetTableColor(getBillCardPanel().getBillModel(),
					getBillCardPanel().getBillTable(), getBillCardPanel(), e
							.getErrorRowNums(), m_cNormalColor, e
							.getExceptionColor(), m_bExchangeColor,
					m_bLocateErrorColor, e.getHint());*/
			//SetColor.setErrRowColor(getBillCardPanel().getBillTable(), e.getErrorRowNums());
			nc.ui.ic.pub.tools.GenMethod.setRowColorWhenException(getBillCardPanel(), e);
			return false;
		} catch (ICNumException e) {
			// 显示提示
			String sErrorMessage = GeneralMethod.getBodyErrorMessage(
					getBillCardPanel(), e.getErrorRowNums(), e.getHint());
			nc.ui.pub.beans.MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes
					.getInstance()
					.getStrByID("4008spec", "UPPSCMCommon-000059")/*
																	 * @res "错误"
																	 */, sErrorMessage);
			/*SetColor.SetTableColor(getBillCardPanel().getBillModel(),
					getBillCardPanel().getBillTable(), getBillCardPanel(), e
							.getErrorRowNums(), m_cNormalColor, e
							.getExceptionColor(), m_bExchangeColor,
					m_bLocateErrorColor, e.getHint());*/
			//SetColor.setErrRowColor(getBillCardPanel().getBillTable(), e.getErrorRowNums());
			nc.ui.ic.pub.tools.GenMethod.setRowColorWhenException(getBillCardPanel(), e);
			return false;
		} catch (ICPriceException e) {
			// 显示提示
			String sErrorMessage = GeneralMethod.getBodyErrorMessage(
					getBillCardPanel(), e.getErrorRowNums(), e.getHint());
			// showHintMessage(sErrorMessage);
			nc.ui.pub.beans.MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes
					.getInstance()
					.getStrByID("4008spec", "UPPSCMCommon-000059")/*
																	 * @res "错误"
																	 */, sErrorMessage);
/*			SetColor.SetTableColor(getBillCardPanel().getBillModel(),
					getBillCardPanel().getBillTable(), getBillCardPanel(), e
							.getErrorRowNums(), m_cNormalColor, e
							.getExceptionColor(), m_bExchangeColor,
					m_bLocateErrorColor, e.getHint());*/
			//SetColor.setErrRowColor(getBillCardPanel().getBillTable(), e.getErrorRowNums());
			nc.ui.ic.pub.tools.GenMethod.setRowColorWhenException(getBillCardPanel(), e);
			return false;
		} catch (ICSNException e) {
			// 显示提示
			String sErrorMessage = GeneralMethod.getBodyErrorMessage(
					getBillCardPanel(), e.getErrorRowNums(), e.getHint());
			// showHintMessage(sErrorMessage);
			nc.ui.pub.beans.MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes
					.getInstance()
					.getStrByID("4008spec", "UPPSCMCommon-000059")/*
																	 * @res "错误"
																	 */, sErrorMessage);
/*			SetColor.SetTableColor(getBillCardPanel().getBillModel(),
					getBillCardPanel().getBillTable(), getBillCardPanel(), e
							.getErrorRowNums(), m_cNormalColor, e
							.getExceptionColor(), m_bExchangeColor,
					m_bLocateErrorColor, e.getHint());*/
			//SetColor.setErrRowColor(getBillCardPanel().getBillTable(), e.getErrorRowNums());
			nc.ui.ic.pub.tools.GenMethod.setRowColorWhenException(getBillCardPanel(), e);
			return false;
		} catch (ICLocatorException e) {
			// 显示提示
			String sErrorMessage = GeneralMethod.getBodyErrorMessage(
					getBillCardPanel(), e.getErrorRowNums(), e.getHint());
			nc.ui.pub.beans.MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes
					.getInstance()
					.getStrByID("4008spec", "UPPSCMCommon-000059")/*
																	 * @res "错误"
																	 */, sErrorMessage);
/*			SetColor.SetTableColor(getBillCardPanel().getBillModel(),
					getBillCardPanel().getBillTable(), getBillCardPanel(), e
							.getErrorRowNums(), m_cNormalColor, e
							.getExceptionColor(), m_bExchangeColor,
					m_bLocateErrorColor, e.getHint());*/
			//SetColor.setErrRowColor(getBillCardPanel().getBillTable(), e.getErrorRowNums());
			nc.ui.ic.pub.tools.GenMethod.setRowColorWhenException(getBillCardPanel(), e);
			return false;
		} catch (ICRepeatException e) {
			// 显示提示
			String sErrorMessage = GeneralMethod.getBodyErrorMessage(
					getBillCardPanel(), e.getErrorRowNums(), e.getHint());
			// showHintMessage(sErrorMessage);
			nc.ui.pub.beans.MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes
					.getInstance()
					.getStrByID("4008spec", "UPPSCMCommon-000059")/*
																	 * @res "错误"
																	 */, sErrorMessage);
		/*	SetColor.SetTableColor(getBillCardPanel().getBillModel(),
					getBillCardPanel().getBillTable(), getBillCardPanel(), e
							.getErrorRowNums(), m_cNormalColor, e
							.getExceptionColor(), m_bExchangeColor,
					m_bLocateErrorColor, e.getHint());*/
			//SetColor.setErrRowColor(getBillCardPanel().getBillTable(), e.getErrorRowNums());
			nc.ui.ic.pub.tools.GenMethod.setRowColorWhenException(getBillCardPanel(), e);
			return false;
		} catch (ICHeaderNullFieldException e) {
			// 显示提示
			String sErrorMessage = GeneralMethod.getHeaderErrorMessage(
					getBillCardPanel(), e.getErrorRowNums(), e.getHint());
			// showHintMessage(sErrorMessage);
			nc.ui.pub.beans.MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes
					.getInstance()
					.getStrByID("4008spec", "UPPSCMCommon-000059")/*
																	 * @res "错误"
																	 */, sErrorMessage);
			return false;
		} catch (NullFieldException e) {
			nc.ui.pub.beans.MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes
					.getInstance()
					.getStrByID("4008spec", "UPPSCMCommon-000059")/*
																	 * @res "错误"
																	 */, e.getHint());
			return false;
		} catch (ValidationException e) {
			nc.vo.scm.pub.SCMEnv.out("校验异常！其他未知故障...");
			nc.ui.pub.beans.MessageDialog.showErrorDlg(this,
					nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec",
							"UPPSCMCommon-000059")/* @res "错误" */,
					nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec",
							"UPP4008spec-000193")/* @res "校验异常！其他未知故障。" */);
			handleException(e);
			return false;
		}
	}

	/**
	 * Invoked when the mouse has been clicked on a component.
	 */
	public void mouseClicked(java.awt.event.MouseEvent e) {
		if (e.getSource() == getBillListPanel().getHeadTable()) {
			if (e.getClickCount() == 2
					&& getBillListPanel().getHeadTable().getSelectedRow() != -1) {
				if (m_iBqrybalrec != 2)
					onList();
				setBillSortEnable();
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
		Object edd = e.getSource();
	}

	/**
	 * Invoked when a mouse button has been released on a component.
	 */
	public void mouseReleased(java.awt.event.MouseEvent e) {
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
	 * 创建者：仲瑞庆 功能： 参数： 返回： 例外： 日期：(2001-5-24 上午 9:38) 修改日期，修改人，修改原因，注释标志：
	 */
	protected void colEditableSet(String sItemKey, int iRow) {
		nc.ui.pub.bill.BillItem bi = getBillCardPanel().getBillData()
				.getBodyItem(sItemKey);

		if (isChecking()) {
			if (!sItemKey.equals("nchecknum") && !sItemKey.equals("nprice")
					&& !sItemKey.equals("nadjustnum") && !sItemKey.equals("je")
					&& !sItemKey.equals("ncheckastnum")
					&& !sItemKey.equals("yy") && !sItemKey.equals("cysl")
					&& !sItemKey.equals("cyfsl")) {
				bi.setEnabled(false);
				return;
			}
		} else {
			bi.setEnabled(true);
			return;
		}

		// 当入点不为仓库或存货时，表体行无存货则禁止输入其他值
		if ((!sItemKey.equals("cinventorycode") && !sItemKey
				.equals("cwarehousename"))
				&& (null == m_voBill.getItemValue(iRow, "cinventoryid") || m_voBill
						.getItemValue(iRow, "cinventoryid").toString().trim()
						.length() == 0)) {
			bi.setEnabled(false);
			return;
		} else {
			bi.setEnabled(bi.isEdit());
		}

		// add by hanwei 2003-07-30
		if (sItemKey.equals("dvalidate")) {
			bi.setEnabled(false); // 盘点单上的保质期不可以编辑
		} else if (sItemKey.equals("nchecknum")) {

			// bi.setEnabled(true);
			Object oTempAstUnit = getBillCardPanel().getBodyValueAt(iRow,
					"castunitname");
			Object oTempAstNum = getBillCardPanel().getBodyValueAt(iRow,
					"ncheckastnum");
			if (m_voBill != null
					&& m_voBill.getItemValue(iRow, "isAstUOMmgt") != null
					&& ((Integer) m_voBill.getItemValue(iRow, "isAstUOMmgt"))
							.intValue() == 1) {
				if (oTempAstNum == null
						|| oTempAstNum.toString().trim().length() == 0
						|| oTempAstUnit == null
						|| oTempAstUnit.toString().trim().length() == 0) {
					bi.setEnabled(false);
					showHintMessage(nc.ui.ml.NCLangRes.getInstance()
							.getStrByID("4008spec", "UPP4008spec-000194")/*
																			 * @res
																			 * "表体第"
																			 */
							+ (iRow + 1)
							+ nc.ui.ml.NCLangRes.getInstance().getStrByID(
									"4008spec", "UPP4008spec-000195")/*
																		 * @res
																		 * "行存货为辅计量管理，请先输入辅单位和盘点辅数量！"
																		 */);
				} else {
					bi.setEnabled(true);
				}
			} else {
				bi.setEnabled(true);
			}

		} else if ((sItemKey.equals("nshldtransastnum"))
				|| (sItemKey.equals("nadjustastnum"))
				|| (sItemKey.equals("ncheckastnum"))
				|| (sItemKey.equals("cyfsl"))) {

			if ((null != m_voBill.getItemValue(iRow, "isAstUOMmgt"))
					&& (Integer.valueOf(
							m_voBill.getItemValue(iRow, "isAstUOMmgt")
									.toString()).intValue() != 0)) {
				bi.setEnabled(true);
			} else { // 非辅计量管理
				bi.setEnabled(false);
			}

		} else if (sItemKey.equals("cspacename")) {
			if ((null != m_voBill.getWhOut().getIsLocatorMgt())
					&& (m_voBill.getWhOut().getIsLocatorMgt().intValue() != 0)) {
				bi.setEnabled(true);
				getLocatorRefPane().setOldValue(
						(String) m_voBill.getItemValue(iRow, sItemKey), null,
						(String) m_voBill.getItemValue(iRow, "cspaceid"));
				String ColName = getBillCardPanel().getBillData().getBodyItem(
						"cspacename").getName();
				getBillCardPanel().getBodyPanel().getTable().getColumn(ColName)
						.setCellEditor(new BillCellEditor(getLocatorRefPane()));
				filterSpace(iRow);
			} else {
				bi.setEnabled(false);
			}
		} else
			super.colEditableSet(sItemKey, iRow);
	}

	/**
	 * 如果用户手工修改批次号，则查库，正确带出失效日期及对应单据号，不正确，不清空，允许用户手工输入。 创建者：张欣 功能： 参数： 返回： 例外：
	 * 日期：(2001-6-14 10:25:33) 修改日期，修改人，修改原因，注释标志：
	 * 
	 */
	public void getLotRefbyHand(String ColName) {
		int iSelrow = getBillCardPanel().getBillTable().getSelectedRow();
		String strColName = ColName;
		if (strColName == null || iSelrow < 0) {
			nc.vo.scm.pub.SCMEnv.out("w:data ex..");
			return;
		}
		String sbatchcode = (String) getBillCardPanel().getBodyValueAt(iSelrow,
				"vbatchcode");
		/** 当批次号为空， */
		if ((sbatchcode != null && sbatchcode.trim().length() > 0)
				&& getLotNumbRefPane().isClicked())
			return;

		// /** 用户手工填写批次号时，查库，检查输入的正确与否？ */
		// boolean isLotRight = getLotNumbRefPane().checkData();
		//
		// if (!isLotRight) {
		// getBillCardPanel().setBodyValueAt("", iSelrow, "vbatchcode");
		// }

	}

	/**
	 * 创建者：王乃军 功能：设置表体显示的 参数： 返回： 例外： 日期：(2001-5-9 9:23:32) 修改日期，修改人，修改原因，注释标志：
	 * 
	 * 
	 * 
	 * 
	 */
	protected void setBodyInvValue(int row, InvVO voInv) {
		if (getBillCardPanel().getBodyItem("cinventoryid") != null)
			getBillCardPanel().setBodyValueAt(voInv.getCinventoryid(), row,
					"cinventoryid");
		if (getBillCardPanel().getBodyItem("cinventorycode") != null)
			getBillCardPanel().setBodyValueAt(voInv.getCinventorycode(), row,
					"cinventorycode");
		if (getBillCardPanel().getBodyItem("invname") != null)
			getBillCardPanel().setBodyValueAt(voInv.getInvname(), row,
					"invname");

		if (getBillCardPanel().getBodyItem("invspec") != null)
			getBillCardPanel().setBodyValueAt(voInv.getInvspec(), row,
					"invspec");
		if (getBillCardPanel().getBodyItem("invtype") != null)
			getBillCardPanel().setBodyValueAt(voInv.getInvtype(), row,
					"invtype");
		if (getBillCardPanel().getBodyItem("hsl") != null)
			getBillCardPanel().setBodyValueAt(voInv.getHsl(), row, "hsl");
		// add by hanwei 07-30
		if (getBillCardPanel().getBodyItem("pk_measdoc") != null)
			getBillCardPanel().setBodyValueAt(voInv.getPk_measdoc(), row,
					"pk_measdoc");

		if (getBillCardPanel().getBodyItem("measdocname") != null)
			getBillCardPanel().setBodyValueAt(voInv.getMeasdocname(), row,
					"measdocname");
		// /////////////
		if (getBillCardPanel().getBodyItem("castunitid") != null)
			getBillCardPanel().setBodyValueAt(voInv.getCastunitid(), row,
					"castunitid");
		if (getBillCardPanel().getBodyItem("castunitname") != null)
			getBillCardPanel().setBodyValueAt(voInv.getCastunitname(), row,
					"castunitname");

		if (getBillCardPanel().getBodyItem("vbatchcode") != null)
			getBillCardPanel().setBodyValueAt(voInv.getVbatchcode(), row,
					"vbatchcode");
		if (getBillCardPanel().getBodyItem("dvalidate") != null)
			getBillCardPanel().setBodyValueAt(voInv.getDvalidate(), row,
					"dvalidate");
		if (getBillCardPanel().getBodyItem("scrq") != null)
			getBillCardPanel().setBodyValueAt(voInv.getDproducedate(), row,
					"scrq");

		if (getBillCardPanel().getBodyItem("cspaceid") != null)
			getBillCardPanel().setBodyValueAt(voInv.getCspaceid(), row,
					"cspaceid");
		if (getBillCardPanel().getBodyItem("cspacename") != null)
			getBillCardPanel().setBodyValueAt(voInv.getCspacename(), row,
					"cspacename");

		if (getBillCardPanel().getBodyItem("hlzl") != null)
			getBillCardPanel().setBodyValueAt(voInv.getKeepwasterate(), row,
					"hlzl");

		// 自由项 from 0--->10
		String sTemp = null;
		for (int i = 0; i <= FreeVO.FREE_ITEM_NUM; i++) {
			sTemp = "vfree" + i;
			if (getBillCardPanel().getBodyItem(sTemp) != null)
				getBillCardPanel().setBodyValueAt(
						voInv.getAttributeValue(sTemp), row, sTemp);
		}
	}

	/**
	 * 创建者：王乃军 功能：设置新增单据的初始数据，如日期，制单人等。 参数： 返回： 例外： 日期：(2001-5-9 9:23:32)
	 * 修改日期，修改人，修改原因，注释标志：
	 * 
	 * 
	 * 
	 * 
	 */
	protected void setNewBillInitData() {
		super.setNewBillInitData();
		// try {
		// nc.ui.pub.ClientEnvironment ce = nc.ui.pub.ClientEnvironment
		// .getInstance();
		// if (ce == null) {
		// nc.vo.scm.pub.SCMEnv.out("ce null ERR.");
		// return;
		// }
		// try {
		// getBillCardPanel().setHeadItem("dbilldate", m_sLogDate);
		// } catch (Exception e) {
		//
		// }
		// try {
		// getBillCardPanel().setTailItem("coperatorid", m_sUserID);
		// getBillCardPanel().setTailItem("coperatorname", m_sUserName);
		// getBillCardPanel().setTailItem("cauditorid", null);
		// getBillCardPanel().setTailItem("cauditorname", null);
		// getBillCardPanel().setTailItem("vadjuster", null);
		// getBillCardPanel().setTailItem("vadjustername", null);
		// if (m_voBill != null) {
		// m_voBill.setHeaderValue("coperatorid", m_sUserID);
		// m_voBill.setHeaderValue("coperatorname", m_sUserName);
		// m_voBill.setHeaderValue("cauditorid", null);
		// m_voBill.setHeaderValue("cauditorname", null);
		// m_voBill.setHeaderValue("vadjuster", null);
		// m_voBill.setHeaderValue("vadjustername", null);
		// }
		// } catch (Exception e) {
		//
		// }
		// try {
		// getBillCardPanel().setHeadItem("pk_corp", m_sCorpID);
		// getBillCardPanel().setHeadItem("vbillcode", m_sCorpID);
		// if (m_voBill != null) {
		// m_voBill.setHeaderValue("pk_corp", m_sCorpID);
		// m_voBill.setHeaderValue("vbillcode", m_sCorpID);
		// }
		// } catch (Exception e) {
		//
		// }
		//		
		//
		// } catch (Exception e) {
		//
		// }

	}

	/**
	 * This fine grain notification tells listeners the exact range of cells,
	 * rows, or columns that changed. 修改人：刘家清 修改日期：2007-10-18下午03:59:29 修改原因：
	 */
	public void tableChanged(javax.swing.event.TableModelEvent e) {
		if (/*
			 * ((e.getType() == javax.swing.event.TableModelEvent.INSERT) || (e
			 * .getType() == javax.swing.event.TableModelEvent.DELETE)) &&
			 */(e.getSource() == getBillCardPanel().getBillModel())) {
			synchLineData(e.getFirstRow(), e.getLastRow(), e.getColumn(), e
					.getType());
		}
	}



	/**
	 * 创建者：仲瑞庆 功能：由特殊单据VO改为普通单据VO 参数： 返回： 例外： 日期：(2001-6-26 下午 4:43)
	 * 修改日期，修改人，修改原因，注释标志：
	 * 
	 * @return nc.vo.ic.pub.bill.GeneralBillVO
	 * @param sbvo
	 *            nc.vo.ic.pub.bill.SpecialBillVO
	 */
	protected GeneralBillVO changeFromSpecialVOtoGeneralVO(SpecialBillVO sbvo,
			int iInOutFlag) {
		if ((sbvo == null) || (sbvo.getHeaderVO() == null)
				|| (sbvo.getChildrenVO() == null)) {
			return null;
		}
		int iItemNumb = sbvo.getChildrenVO().length;
		if (iItemNumb < 1) {
			return null;
		}
		GeneralBillVO gbvo = new GeneralBillVO(iItemNumb);

		// 对表头
		gbvo.setParentVO(changeFromSpecialVOtoGeneralVOAboutHeader(gbvo, sbvo,
				iInOutFlag));

		// 对表体
		for (int row = 0; row < iItemNumb; row++) {
			gbvo.setItem(row, changeFromSpecialVOtoGeneralVOAboutItem(gbvo,
					sbvo, iInOutFlag, row));
		}

		return gbvo;
	}

	/**
	 * 创建者：王乃军 功能：清调整人 参数： 返回： 例外： 日期：(2001-5-9 9:23:32) 修改日期，修改人，修改原因，注释标志：
	 * 
	 * 
	 * 
	 * 
	 */
	protected void clearAdjustBillFlag() {
		if (getBillCardPanel().getTailItem("vadjuster") != null)
			getBillCardPanel().setTailItem("vadjuster", null);
		if (getBillCardPanel().getTailItem("vadjustername") != null)
			getBillCardPanel().setTailItem("vadjustername", null);
		if (m_voBill != null) {
			m_voBill.setHeaderValue("vadjuster", null);
			m_voBill.setHeaderValue("vadjustername", null);
		}
	}

	/**
	 * 创建者：王乃军 功能：清审批人。 参数： 返回： 例外： 日期：(2001-5-9 9:23:32) 修改日期，修改人，修改原因，注释标志：
	 * 
	 * 
	 * 
	 * 
	 */
	protected void clearAuditBillFlag() {
		if (getBillCardPanel().getTailItem("cauditorid") != null)
			getBillCardPanel().setTailItem("cauditorid", null);
		if (getBillCardPanel().getTailItem("cauditorname") != null)
			getBillCardPanel().setTailItem("cauditorname", null);
		if (m_voBill != null) {
			m_voBill.setHeaderValue("cauditorid", null);
			m_voBill.setHeaderValue("cauditorname", null);
		}
	}

	/**
	 * 此处插入方法说明。 创建日期：(2001-6-27 10:34:52)
	 * 
	 * @return boolean
	 */
	public nc.vo.pub.lang.UFBoolean isFixFlag(int row) {

		Integer isFixFlag = (Integer) m_voBill.getItemValue(row,
				"isSolidConvRate");
		// if (isFixFlag == null) return new UFBoolean (false);
		if (isFixFlag == null)
			return null;
		return new UFBoolean(isFixFlag.intValue() == 1);
	}

	public boolean isHsl(int row) {
		InvVO invvo = m_voBill.getItemInv(row);
		Integer isHsl = invvo.getIsStoreByConvert();
		if (isHsl != null && isHsl.intValue() == 1)
			return true;
		else {
			return false;
		}
	}

	/**
	 * 创建者：仲瑞庆 功能： 参数： 返回： 例外： 日期：(2001-5-10 下午 2:54) 修改日期，修改人，修改原因，注释标志：
	 * 修改人：刘家清 修改时间：2008-7-29 上午09:46:19 修改原因：支持对调整辅数量不为0、调整数量为0的盘点记录进行盘盈盘亏处理。
	 */
	public void onAdjustGoods() {
		// 调整存货
		showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec",
				"UPP4008spec-000196")/* @res "调整存货" */);
		try {
		if (getM_voBill().getHeaderVO().getBccountflag().booleanValue()){
			for(int i = 0 ; i < getBillCardPanel().getRowCount() ; i++){
				fillLineSpecailBarCodeVO(i);
				fillLineSpecailBarCode1VO(i);
			}
		}
		/*SpecialBillVO voNowBill = (SpecialBillVO) m_alListData
				.get(m_iLastSelListHeadRow);*/
		SpecialBillVO voNowBill = getM_voBill();

		if (!(voNowBill.getHeaderVO().getFbillFlag().toString()
		.equals(nc.vo.ic.pub.bill.BillStatus.SIGNED)||voNowBill.getHeaderVO().getFbillFlag().toString()
		.equals(nc.vo.ic.pub.bill.BillStatus.AUDITED))) {
			showErrorMessage("盘点单不是审核状态，不能进行调整操作！");
			return;
		}
		if (null == voNowBill.getHeaderVO().getPk_calbody_out())
			voNowBill.getHeaderVO().setPk_calbody_out(GenMethod.getsCalbodyidByWhid(new String[]{voNowBill.getHeaderVO().getCoutwarehouseid()})[0]);
		// vo交换
		// 将特殊单入库VO通过VO对照转换为普通单VO
		// 置出库VO
		SpecialBillVO tempVO = new SpecialBillVO();
		tempVO.setParentVO(voNowBill.getHeaderVO());
		Vector vRowItemVOs = new Vector();
		ArrayList<String> arrBC = new ArrayList<String>();
		HashMap<String,SpecailBarCode1VO> hsBC = new HashMap<String,SpecailBarCode1VO>();
		BarCodeVO[] barCodeVOs = null;
		BarCodeVO gBarCodeVO = null;
		ArrayList<BarCodeVO> arrBarCodes = null;
		HashMap<String, BarCodeVO[]> hsBarCodes = new HashMap<String, BarCodeVO[]>();
		HashMap<String, UFDouble> hsBarCodeNum = new HashMap<String, UFDouble>();
		UFDouble barCodeNum = new UFDouble(0);
		UFDouble rowaccountnum =null;
		UFDouble rowbarcodenum =null;
		SpecailBarCode1VO barcodecomparevo  = null;
		
		boolean isBC = false;
		for (int i = 0; i < voNowBill.getChildrenVO().length; i++) {
			SpecialBillItemVO RowItemVO = (SpecialBillItemVO) (voNowBill
					.getChildrenVO()[i].clone());
			if (voNowBill.getHeaderVO().getBccountflag().booleanValue()) {
				if (null != RowItemVO.getBarcodeManagerflag() && RowItemVO.getBarcodeManagerflag().booleanValue()){
					isBC = false;
					arrBC.clear();
					arrBarCodes = new ArrayList<BarCodeVO>();
					barCodeNum = new UFDouble(0);
					if (null != RowItemVO.getSpecailBarCode1VOs())
						for (SpecailBarCode1VO barcodevo1 : RowItemVO
								.getSpecailBarCode1VOs())
							hsBC.put(barcodevo1.getVbarcode(),barcodevo1);
					if (null != RowItemVO.getSpecailBarCodeVOs())
						for (SpecailBarCodeVO barcodevo : RowItemVO.getSpecailBarCodeVOs()){
							barcodecomparevo = hsBC.get(barcodevo.getVbarcode());
							if (null == barcodecomparevo) {
								isBC = true;
								gBarCodeVO = new BarCodeVO();
								gBarCodeVO.setVbarcode(barcodevo.getVbarcode());
								gBarCodeVO.setPk_corp(barcodevo.getPk_corp());
								gBarCodeVO.setNnumber(barcodevo.getNnumber());
								gBarCodeVO.setStatus(VOStatus.NEW);
								barCodeNum = barCodeNum.add(barcodevo.getNnumber());
								arrBarCodes.add(gBarCodeVO);
							}else if (null != barcodecomparevo && barcodevo.getNnumber().compareTo(barcodecomparevo.getNnumber()) > 0) {
								isBC = true;
								gBarCodeVO = new BarCodeVO();
								gBarCodeVO.setVbarcode(barcodevo.getVbarcode());
								gBarCodeVO.setPk_corp(barcodevo.getPk_corp());
								gBarCodeVO.setNnumber(barcodevo.getNnumber().sub(barcodecomparevo.getNnumber()));
								gBarCodeVO.setStatus(VOStatus.NEW);
								barCodeNum = barCodeNum.add(barcodevo.getNnumber().sub(barcodecomparevo.getNnumber()));
								arrBarCodes.add(gBarCodeVO);
							}
						}
					
					if (null != RowItemVO.getNaccountnum())
						rowaccountnum = RowItemVO.getNaccountnum();
					else
						rowaccountnum = UFDouble.ZERO_DBL;
					if (null != RowItemVO.getNbarcodenum())
						rowbarcodenum = RowItemVO.getNbarcodenum();
					else
						rowbarcodenum = UFDouble.ZERO_DBL;
					
					if (null != arrBarCodes && arrBarCodes.size() > 0) {
						barCodeVOs = new BarCodeVO[arrBarCodes.size()];
						arrBarCodes.toArray(barCodeVOs);
						hsBarCodes.put("4I" + RowItemVO.getCspecialbid(),
								barCodeVOs);
	
						if (rowaccountnum.compareTo(rowbarcodenum) > 0)
							hsBarCodeNum.put("4I" + RowItemVO.getCspecialbid(),
									barCodeNum.add(rowaccountnum.sub(rowbarcodenum)));
						else
							hsBarCodeNum.put("4I" + RowItemVO.getCspecialbid(),
								barCodeNum);
					}else{
						if (rowaccountnum.compareTo(rowbarcodenum) > 0){
							hsBarCodeNum.put("4I" + RowItemVO.getCspecialbid(),
									barCodeNum.add(rowaccountnum.sub(rowbarcodenum)));
							isBC = true;
						}
					}
	
					if (isBC)
						vRowItemVOs.addElement(RowItemVO);
				}
				/**add by ouyangzhb 2012-04-25 新增码单盘点的判断*/
			} else if (voNowBill.getHeaderVO().getIcheckmode() == CheckMode.md){
				UFDouble Naccountnum = RowItemVO.getNaccountnum()==null ? UFDouble.ZERO_DBL:RowItemVO.getNaccountnum();
				if(null != RowItemVO.getVuserdef9() && new UFBoolean(RowItemVO.getVuserdef9()).booleanValue()
						&&Naccountnum.compareTo(UFDouble.ZERO_DBL)!=0){
					RowItemVO.setNadjustnum(RowItemVO.getNaccountnum());
					RowItemVO
							.setNadjustastnum(((RowItemVO.getNaccountastnum() == null || RowItemVO.getNaccountastnum()
									.toString().trim().length() == 0) ? null : RowItemVO.getNaccountastnum()));
					RowItemVO
							.setNadjustgrsnum(RowItemVO.getNaccountnum());
					RowItemVO
							.setAttributeValue(
									"je",
									((RowItemVO.getNprice() == null || RowItemVO
											.getNprice().toString().trim().length() == 0) ? null
											: ((UFDouble) RowItemVO
													.getNprice().multiply(RowItemVO.getNaccountnum()))));
					vRowItemVOs.addElement(RowItemVO);
				}
				/**add by ouyangzhb 2012-04-25 新增码单盘点的判断*/
			}else {
				if ((null != RowItemVO.getNadjustnum()
						&& RowItemVO.getNadjustnum().doubleValue() < 0)
						||(null != RowItemVO.getNadjustastnum()
								&& RowItemVO.getNadjustastnum().doubleValue() < 0)){
					RowItemVO.setNadjustnum(RowItemVO.getNadjustnum().multiply(
							-1));
					RowItemVO
							.setNadjustastnum(((RowItemVO.getNadjustastnum() == null || RowItemVO
									.getNadjustastnum().toString().trim()
									.length() == 0) ? null : RowItemVO
									.getNadjustastnum().multiply(-1)));
					RowItemVO
							.setNadjustgrsnum(RowItemVO.getNadjustgrsnum() == null ? null
									: RowItemVO.getNadjustgrsnum().multiply(-1));
					RowItemVO
							.setAttributeValue(
									"je",
									((RowItemVO.getAttributeValue("je") == null || RowItemVO
											.getAttributeValue("je").toString()
											.trim().length() == 0) ? null
											: ((UFDouble) RowItemVO
													.getAttributeValue("je"))
													.multiply(-1)));
					vRowItemVOs.addElement(RowItemVO);
				}
			}
		}
		
		ArrayList alGeneralVO = new ArrayList();
		SpecialBillItemVO[] tempItemVO = null;
		ArrayList alOutGeneralVO = null;
		if (vRowItemVOs.size() != 0) {
			tempItemVO = new SpecialBillItemVO[vRowItemVOs.size()];
			vRowItemVOs.copyInto(tempItemVO);
			tempVO.setChildrenVO(tempItemVO);

			alOutGeneralVO = new ArrayList();
			GeneralBillVO[] gbvo = pfVOConvert(new SpecialBillVO[] { tempVO },
					"4R", "4I");// changeFromSpecialVOtoGeneralVO((SpecialBillVO)
								// tempVO.clone(), InOutFlag.OUT);
			for (GeneralBillVO bvo : gbvo)
				for (GeneralBillItemVO itemvo : bvo.getItemVOs()){
					if (null != hsBarCodes.get("4I"
							+ itemvo.getCsourcebillbid())) {
						itemvo.setBarCodeVOs(hsBarCodes.get("4I"
								+ itemvo.getCsourcebillbid()));
					}
					if (null != hsBarCodeNum.get("4I"
							+ itemvo.getCsourcebillbid())) {
						itemvo.setNoutnum(hsBarCodeNum.get("4I"
								+ itemvo.getCsourcebillbid()));
						itemvo.setNshouldoutnum(hsBarCodeNum.get("4I"
								+ itemvo.getCsourcebillbid()));
						if (null != itemvo.getNoutnum() && null != itemvo.getNprice())
							itemvo.setNmny(itemvo.getNoutnum().multiply(itemvo.getNprice()));
						if (null != itemvo.getNoutnum() && null != itemvo.getHsl() && nc.vo.ic.pub.GenMethod.ZERO.compareTo(itemvo.getHsl()) != 0){
							itemvo.setNoutassistnum(itemvo.getNoutnum().div(itemvo.getHsl()));
							itemvo.setNshouldoutassistnum(itemvo.getNoutnum().div(itemvo.getHsl()));
						}
						if (null != itemvo.getLocator() && 1 == itemvo.getLocator().length)
							itemvo.getLocator()[0].setNoutspacenum(hsBarCodeNum.get("4I"
									+ itemvo.getCsourcebillbid()));
						
					}
				}
			if (gbvo != null && gbvo[0] != null) {
				gbvo[0].setHeaderValue("pk_calbody", tempVO.getWhOut()
						.getPk_calbody());
				gbvo[0].setHeaderValue("vcalbodyname", tempVO.getWhOut()
						.getVcalbodyname());
				// 设置行号
				setRowNo(gbvo[0], "4I");
				nc.vo.ic.pub.GenMethod.fillGeneralBillVOByBarcode(gbvo[0]);
				
				/**add by ouyangzhb 2012-04-25 码单盘点，在交换到其他出入库的时候，需要把交换过去的自定义项的值清空*/
				if(voNowBill.getHeaderVO().getIcheckmode() == CheckMode.md){
					alGeneralVO.add(gbvo[0]);
					gbvo[0].getHeaderVO().setVuserdef1(null);
					for (GeneralBillItemVO itemvo : gbvo[0].getItemVOs()){
						itemvo.setVuserdef1(null);
						itemvo.setVuserdef2(null);
						itemvo.setVuserdef3(null);
						itemvo.setVuserdef4(null);
						itemvo.setVuserdef5(null);
						itemvo.setVuserdef6(null);
						itemvo.setVuserdef7(null);
						itemvo.setVuserdef8(null);
						itemvo.setVuserdef9(null);
					}
				}
				/**add by ouyangzhb 2012-04-25 码单盘点，在交换到其他出入库的时候，需要把交换过去的自定义项的值清空*/
				
				alOutGeneralVO.add(gbvo[0]);
			}
		}

		// 置入库VO
		tempVO = new SpecialBillVO();
		tempVO.setParentVO(voNowBill.getHeaderVO());
		vRowItemVOs = null;
		vRowItemVOs = new Vector();
		for (int i = 0; i < voNowBill.getChildrenVO().length; i++) {
			SpecialBillItemVO RowItemVO = (SpecialBillItemVO) (voNowBill
					.getChildrenVO()[i].clone());

			if (voNowBill.getHeaderVO().getBccountflag().booleanValue()) {
				if (null != RowItemVO.getBarcodeManagerflag() && RowItemVO.getBarcodeManagerflag().booleanValue()){
					isBC = false;
					arrBC.clear();
					arrBarCodes = new ArrayList<BarCodeVO>();
					barCodeNum = new UFDouble(0);
					if (null != RowItemVO.getSpecailBarCodeVOs())
						for (SpecailBarCodeVO barcodevo : RowItemVO.getSpecailBarCodeVOs())
							arrBC.add(barcodevo.getVbarcode());
					if (null != RowItemVO.getSpecailBarCode1VOs())
						for (SpecailBarCode1VO barcode1vo : RowItemVO
								.getSpecailBarCode1VOs())
							if (!arrBC.contains(barcode1vo.getVbarcode())) {
								isBC = true;
								gBarCodeVO = new BarCodeVO();
								gBarCodeVO.setVbarcode(barcode1vo.getVbarcode());
								gBarCodeVO.setPk_corp(barcode1vo.getPk_corp());
								gBarCodeVO.setNnumber(barcode1vo.getNnumber());
								gBarCodeVO.setStatus(VOStatus.NEW);
								barCodeNum = barCodeNum
										.add(barcode1vo.getNnumber());
								arrBarCodes.add(gBarCodeVO);
							}
					if (null != RowItemVO.getNaccountnum())
						rowaccountnum = RowItemVO.getNaccountnum();
					else
						rowaccountnum = UFDouble.ZERO_DBL;
					if (null != RowItemVO.getNbarcodenum())
						rowbarcodenum = RowItemVO.getNbarcodenum();
					else
						rowbarcodenum = UFDouble.ZERO_DBL;
					
					if (null != arrBarCodes && arrBarCodes.size() > 0) {
						barCodeVOs = new BarCodeVO[arrBarCodes.size()];
						arrBarCodes.toArray(barCodeVOs);
						hsBarCodes.put("4A" + RowItemVO.getCspecialbid(),
								barCodeVOs);
	
						if (rowaccountnum.compareTo(rowbarcodenum) < 0)
							hsBarCodeNum.put("4A" + RowItemVO.getCspecialbid(),
									barCodeNum.add(rowbarcodenum.sub(rowaccountnum)));
						else
							hsBarCodeNum.put("4A" + RowItemVO.getCspecialbid(),
									barCodeNum);
					}else{
						if (rowaccountnum.compareTo(rowbarcodenum) < 0){
							hsBarCodeNum.put("4A" + RowItemVO.getCspecialbid(),
									barCodeNum.add(rowbarcodenum.sub(rowaccountnum)));
							isBC = true;
						}
					}
					if (isBC)
						vRowItemVOs.addElement(RowItemVO);
				}

				/**add by ouyangzhb 2012-04-25 新增码单盘点的判断*/
			} else if (voNowBill.getHeaderVO().getIcheckmode() == CheckMode.md){
				UFDouble Nchecknum = RowItemVO.getNchecknum()==null ? UFDouble.ZERO_DBL:RowItemVO.getNchecknum();
				if(null != RowItemVO.getVuserdef9() && new UFBoolean(RowItemVO.getVuserdef9()).booleanValue()
						&&Nchecknum.compareTo(UFDouble.ZERO_DBL)!=0){
					RowItemVO.setNadjustnum(RowItemVO.getNchecknum());
					RowItemVO
							.setNadjustastnum(((RowItemVO.getNcheckastnum()  == null || RowItemVO.getNcheckastnum()
									.toString().trim().length() == 0) ? null : RowItemVO.getNcheckastnum()));
					RowItemVO
							.setNadjustgrsnum(RowItemVO.getNchecknum());
					RowItemVO.setAttributeValue(
											"je",
											((RowItemVO.getNprice() == null || RowItemVO
													.getNprice().toString().trim().length() == 0) ? null
													: ((UFDouble) RowItemVO
															.getNprice().multiply(RowItemVO.getNchecknum()))));
					vRowItemVOs.addElement(RowItemVO);
				}
				/**add by ouyangzhb 2012-04-25 新增码单盘点的判断*/
			} else {
				if ((null != RowItemVO.getNadjustnum()
						&& RowItemVO.getNadjustnum().doubleValue() > 0)
						||(null != RowItemVO.getNadjustastnum()
								&& RowItemVO.getNadjustastnum().doubleValue() > 0)){
					vRowItemVOs.addElement(RowItemVO);
				}
			}
		}
		ArrayList alInGeneralVO = null;
		if (vRowItemVOs.size() != 0) {
			tempItemVO = new SpecialBillItemVO[vRowItemVOs.size()];
			vRowItemVOs.copyInto(tempItemVO);
			tempVO.setChildrenVO(tempItemVO);

			alInGeneralVO = new ArrayList();
			GeneralBillVO[] gbvo = pfVOConvert(new SpecialBillVO[] { tempVO },
					"4R", "4A");// changeFromSpecialVOtoGeneralVO((SpecialBillVO)
								// tempVO.clone(), InOutFlag.IN);
			for (GeneralBillVO bvo : gbvo)
				for (GeneralBillItemVO itemvo : bvo.getItemVOs()){
					if (null != hsBarCodes.get("4A"
							+ itemvo.getCsourcebillbid())) {
						itemvo.setBarCodeVOs(hsBarCodes.get("4A"
								+ itemvo.getCsourcebillbid()));
					}
					if (null != hsBarCodeNum.get("4A"
							+ itemvo.getCsourcebillbid())) {
						itemvo.setNinnum(hsBarCodeNum.get("4A"
								+ itemvo.getCsourcebillbid()));
						itemvo.setNshouldinnum(hsBarCodeNum.get("4A"
								+ itemvo.getCsourcebillbid()));
						if (null != itemvo.getNinnum() && null != itemvo.getNprice())
							itemvo.setNmny(itemvo.getNinnum().multiply(itemvo.getNprice()));
						if (null != itemvo.getNinnum() && null != itemvo.getHsl() && nc.vo.ic.pub.GenMethod.ZERO.compareTo(itemvo.getHsl()) != 0){
							itemvo.setNinassistnum(itemvo.getNinnum().div(itemvo.getHsl()));
							itemvo.setNneedinassistnum(itemvo.getNinnum().div(itemvo.getHsl()));
						}
						if (null != itemvo.getLocator() && 1 == itemvo.getLocator().length)
							itemvo.getLocator()[0].setNinspacenum(hsBarCodeNum.get("4A"
									+ itemvo.getCsourcebillbid()));
					}
				}
			if (gbvo != null && gbvo[0] != null) {
				gbvo[0].setHeaderValue("pk_calbody", tempVO.getWhOut()
						.getPk_calbody());
				gbvo[0].setHeaderValue("vcalbodyname", tempVO.getWhOut()
						.getVcalbodyname());
				// 设置行号
				setRowNo(gbvo[0], "4A");
				nc.vo.ic.pub.GenMethod.fillGeneralBillVOByBarcode(gbvo[0]);
				
				/**add by ouyangzhb 2012-04-25 码单盘点，在交换到其他出入库的时候，需要把交换过去的自定义项的值清空*/
				if(voNowBill.getHeaderVO().getIcheckmode() == CheckMode.md){
					gbvo[0].getHeaderVO().setVuserdef1(null);
					for (GeneralBillItemVO itemvo : gbvo[0].getItemVOs()){
						itemvo.setVuserdef1(null);
						itemvo.setVuserdef2(null);
						itemvo.setVuserdef3(null);
						itemvo.setVuserdef4(null);
						itemvo.setVuserdef5(null);
						itemvo.setVuserdef6(null);
						itemvo.setVuserdef7(null);
						itemvo.setVuserdef8(null);
						itemvo.setVuserdef9(null);
					}
				}
				/**add by ouyangzhb 2012-04-25 码单盘点，在交换到其他出入库的时候，需要把交换过去的自定义项的值清空*/
				alInGeneralVO.add(gbvo[0]);
			}
		}

		if ((alInGeneralVO == null || alInGeneralVO.size() == 0)
				&& (alOutGeneralVO == null || alOutGeneralVO.size() == 0)) {
			nc.ui.pub.beans.MessageDialog.showErrorDlg(this,
					nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec",
							"UPP4008spec-000197")/* @res "检查" */,
					nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec",
							"UPP4008spec-000198")/* @res "没有调整数量！" */);
			return;
		}

		/*getAuditDlg().setVO(tempVO, alInGeneralVO, alOutGeneralVO,
				m_sBillTypeCode, m_voBill.getPrimaryKey().trim(), m_sCorpID,
				m_sUserID);*/
		getAuditDlg().setVO((SpecialBillVO)m_voBill.clone(), alInGeneralVO, alOutGeneralVO,
				m_sBillTypeCode, m_voBill.getPrimaryKey().trim(), m_sCorpID,
				m_sUserID);

		// 设置调整的其他入和出不能超应发数量 add by hanwei 2004-6-5
		getAuditDlg().getChldClientUIIn().getBarcodeCtrl().setOverShouldNum(
				false);
		getAuditDlg().getChldClientUIOut().getBarcodeCtrl().setOverShouldNum(
				false);

		getAuditDlg().getChldClientUIIn().getBarcodeCtrl().setModifyBillUINum(
				false);
		getAuditDlg().getChldClientUIOut().getBarcodeCtrl().setModifyBillUINum(
				false);

		getAuditDlg().setName("BillDlg");
		getAuditDlg().showModal();
		// if (ret == nc.ui.pub.beans.UIDialog.ID_OK) {
		if (getAuditDlg().isOK()) {
			// int ret= getAuditDlg("转入/转出单", alInGeneralVO,
			// alOutGeneralVO).showModal();

			// if (ret == nc.ui.pub.beans.UIDialog.ID_OK) {
			
				// 更新表尾
				setAdjustBillFlag();
				filterNullLine();
				// 单据PK
				String sBillPK = m_voBill.getPrimaryKey();
				// 刷新ts
				freshTs(qryLastTs(sBillPK));
				
				/**add by ouyangzhb 2012-04-25 根据盘点记录生成码单明细*/
				if(voNowBill.getHeaderVO().getIcheckmode() == CheckMode.md){
					createMDcrk(m_voBill);
				}
				/**add by ouyangzhb 2012-04-25 根据盘点记录生成码单明细*/
				
				m_alListData.set(m_iLastSelListHeadRow, m_voBill.clone());

				m_iFirstSelListHeadRow = -1;
				switchListToBill();

				setButtonState();
				setBillState();
				showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"4008spec", "UPP4008spec-000199")/* @res "调整存货完成！" */);
				return;

			
		}
		} catch (Exception e) {
			handleException(e);
			nc.ui.pub.beans.MessageDialog.showErrorDlg(this,
					nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec",
							"UPPSCMCommon-000059")/* @res "错误" */, e
							.getMessage());
		}
		showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec",
				"UPP4008spec-000200")/* @res "调整存货取消！" */);
	}

	/**
	 * 创建者：仲瑞庆 功能：取消调整(应将两次调用BO改为一次调用) 参数： 返回： 例外： 日期：(2001-5-10 下午 2:50)
	 * 修改日期，修改人，修改原因，注释标志：
	 */
	public void onCancelAdjust() {
		switch (nc.ui.pub.beans.MessageDialog.showYesNoDlg(this, null,
				nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UCH067")/*
																				 * @res
																				 * "是否确定要取消？"
																				 */
				, MessageDialog.ID_NO)) {

		case nc.ui.pub.beans.MessageDialog.ID_YES:
			break;

		default:
			return;
		}

		showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec",
				"UPP4008spec-000201")/* @res "取消调整存货" */);
		try {

			SpecialBillVO vo = new SpecialBillVO();
			SpecialBillHeaderVO voHead = new SpecialBillHeaderVO();
			voHead.setAttributeValue("cspecialhid", m_voBill
					.getHeaderValue("cspecialhid"));
			voHead.setAttributeValue("coperatorid", m_sUserID);
			voHead.setAttributeValue("coperatoridnow", m_sUserID);
			voHead.setAttributeValue("pk_corp", m_sCorpID);
			voHead.setAttributeValue("coperatoridnow", m_sUserID);
			voHead.setAttributeValue("cbilltypecode", m_sBillTypeCode);
			vo.setParentVO(voHead);

			// ArrayList alsPrimaryKey=
			// (ArrayList)
			nc.ui.pub.pf.PfUtilClient.processAction("CANCELADJUST",
					m_sBillTypeCode, m_sLogDate, vo);

			// 更新表尾
			clearAdjustBillFlag();
			filterNullLine();
			// 单据PK
			String sBillPK = vo.getPrimaryKey();
			// 刷新ts
			freshTs(qryLastTs(sBillPK));
			m_alListData.set(m_iLastSelListHeadRow, m_voBill.clone());
			m_iFirstSelListHeadRow = -1;
			switchListToBill();

			setButtonState();
			setBillState();
			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"4008spec", "UPP4008spec-000202")/* @res "取消调整存货成功！" */);

		} catch (Exception e) {
			nc.vo.scm.pub.SCMEnv.out("数据通讯失败！");
			handleException(e);
			nc.ui.pub.beans.MessageDialog.showErrorDlg(this,
					nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec",
							"UPPSCMCommon-000059")/* @res "错误" */, e
							.getMessage());
		}
	}

	// 是否条码盘点
	private UFBoolean m_iscountflag = UFBoolean.FALSE;
	private HashMap<String, WhVO> hm_whid_whvo = new HashMap<String, WhVO>();

	/**
	 * 盘点方式选择已经返回处理代码
	 */
	public void onCheckChoose() {
		try {
			Timer t = new Timer();
			// 盘点选择
			long lTimeBegin = System.currentTimeMillis();
			long lTime = System.currentTimeMillis();
			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"4008spec", "UPT40081016-000035")/* @res "盘点选择" */);
			if (m_dlgCtc == null) {
				m_dlgCtc = new CheckTypeChooser(this);
				m_dlgCtc.setCorpID(m_sCorpID);
				m_dlgCtc.setUserID(m_sUserID);
				m_dlgCtc.setLogDate(m_sLogDate);
			}
			m_dlgCtc.clearOldCache();
			if (m_dlgCtc.showModal() == nc.ui.pub.beans.UIDialog.ID_OK) {
				t.start();
				ArrayList alCheckChoose = m_dlgCtc.getvos();
				// 初始新增的情况：需要一个vo
				onAdd();

				m_voBill = new SpecialBillVO(getBillCardPanel().getRowCount());
				getBillCardPanel().getBillValueVO(m_voBill);

				// 获得盘点方式，将其置入Model和m_voBill中
				int radFlag = ((Integer) ((ArrayList) alCheckChoose.get(1))
						.get(0)).intValue();
				// 修改人：陈倪娜 上午10:25:44_2009-10-31 修改原因: 如果是库管员盘点，则补表头库管员
			   if(radFlag==CheckMode.Keeper){ //保管员盘点
	                String ManagerID = ((ArrayList) ((ArrayList) alCheckChoose
	                        .get(1)).get(1)).get(0).toString();
	                m_voBill.getHeaderVO().setCinbsrid(ManagerID);
			   }
				if (3 < alCheckChoose.size() && null != alCheckChoose.get(3))
					m_iscountflag = (UFBoolean) alCheckChoose.get(3);
				m_voBill.getHeaderVO().setBccountflag(m_iscountflag);

				String sCheckModeNow = CheckMode.getCheckMode()[radFlag];
				getBillCardPanel().setHeadItem("pdfs", sCheckModeNow);
				m_voBill.setHeaderValue("pdfs", sCheckModeNow);
				m_voBill.setHeaderValue("icheckmode", new Integer(radFlag));
			
				// 根据盘点方式查询数据库
				lTime = System.currentTimeMillis();
				// 是否按照换算率维度取数
				// alCheckChoose.add(new Boolean(isHslConfig()));
				HashMap<String,Object> hmRet = SpecialHHelper.queryCheckChooseInfo_NEW(
            m_sCorpID, alCheckChoose, m_sUserID,alCheckChoose.get(0).toString().trim());
				ArrayList alInvResult = (ArrayList)hmRet.get(IICParaConst.CheckInvsPara);
				WhVO whvo = (WhVO)hmRet.get(IICParaConst.CheckWareHousePara);
				if(null != whvo && !hm_whid_whvo.containsKey(whvo.getCwarehouseid()))
				  hm_whid_whvo.put(whvo.getCwarehouseid(), whvo);
//				ArrayList alInvResult = SpecialHHelper.queryCheckChooseInfo(
//						m_sCorpID, alCheckChoose, m_sUserID);
				showTime(lTime, "qry invs：");
				// 没查到任何数据
				if (alInvResult == null || alInvResult.size() == 0) {
					showHintMessage(nc.ui.ml.NCLangRes.getInstance()
							.getStrByID("4008spec", "UPP4008spec-000203")/*
																			 * @res
																			 * "无存货信息！"
																			 */);
					m_voBill.setChildrenVO(null);
					getBillCardPanel().setBillValueVO(m_voBill);
					getBillCardPanel().updateValue();
					dispBodyRow(getBillCardPanel().getBillTable());

					return;
				}

				getBillCardPanel().getBillModel().setNeedCalculate(false);
				// 置仓库
				getBillCardPanel().setHeadItem("coutwarehouseid",
						alCheckChoose.get(0).toString().trim());
				lTime = System.currentTimeMillis();
				afterWhOutEdit(false);
				showTime(lTime, "置仓库查询：");

				int rowcount = getBillCardPanel().getRowCount();

				lTime = System.currentTimeMillis();
				if (alInvResult.size() > rowcount) {
					getBillCardPanel().getBodyPanel().addLine(
							alInvResult.size() - rowcount);
				}
				showTime(lTime, "addLine：");
				m_timer.start();
				rowcount = alInvResult.size();
				// 向界面置了数据。
				lTime = System.currentTimeMillis();
				 if(radFlag==CheckMode.md){
					 afterInvsEdit_MD(alInvResult);
				 }else{
						afterInvsEdit(alInvResult);
				 }
			
				showTime(lTime, "afterInvsEdit_MD：");
				// 货位盘点需要执行货位公式
				lTime = System.currentTimeMillis();
				// if (radFlag == CheckMode.Space) {
				getBillCardPanel().getBillModel().execLoadFormulaByKey(
						"cspacename");
				//getBillCardPanel().getBillModel().execFormulasWithVO(m_voBill.getItemVOs(), getBillCardPanel().getBodyItem("cspacename").getLoadFormula());
				getBillCardPanel().getBillModel().execLoadFormulaByKey(
						"cvendorname");
				//getBillCardPanel().getBillModel().execFormulasWithVO(m_voBill.getItemVOs(), getBillCardPanel().getBodyItem("cvendorname").getLoadFormula());

				// }
				showTime(lTime, "execLoadFormulaByKey：");
				// filterNullLine();
				m_timer.stopAndShow("界面公式");
				getBillCardPanel().getBillModel().setNeedCalculate(true);
				
//				 填入界面中数据
				getBillCardPanel().tableStopCellEditing();
				getBillCardPanel().stopEditing();

				// 滤掉所有的空行
				filterNullLine();
				//保持与界面上的一至
				SpecialBillVO voNowBill = getBillVO();
				m_voBill.setIDItems(voNowBill);
				
				if (null != m_voBill && 0 < m_voBill.getItemVOs().length){
					if ((null == m_voBill.getWhOut().getIsLocatorMgt())
							|| (m_voBill.getWhOut().getIsLocatorMgt().intValue() != 1)) {
						
					} else {
						getLocatorRefPane().setOldValue(
								(String) m_voBill.getItemValue(0, "cspacename"),
								null,
								(String) m_voBill.getItemValue(0, "cspaceid"));
						String ColName = getBillCardPanel().getBillData()
								.getBodyItem("cspacename").getName();
						getBillCardPanel().getBodyPanel().getTable().getColumn(
								ColName).setCellEditor(
								new BillCellEditor(getLocatorRefPane()));
						filterSpace(0);
				}
				}
			}
			t.stopAndShow("check choose total=");
			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"SCMCOMMON", "UPPSCMCommon-000133")/* @res "就绪" */);
		} catch (Exception e) {
			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"4008spec", "UPP4008spec-000137")/* @res "未能完成，请再做尝试！" */);
			handleException(e);
			nc.ui.pub.beans.MessageDialog.showErrorDlg(this,
					nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec",
							"UPPSCMCommon-000059")/* @res "错误" */, e
							.getMessage());
		}

		return;
	}

	// private void restoreHslConfig() {
	// if (getBillCardPanel().getHeadItem("ishsl") != null)
	// ((UICheckBox) getBillCardPanel().getHeadItem("ishsl")
	// .getComponent()).setSelected(m_bHslWeiDu);
	//
	// }

	/**
	 * 创建者：王乃军 功能：设置调整人 参数： 返回： 例外： 日期：(2001-5-9 9:23:32) 修改日期，修改人，修改原因，注释标志：
	 * 
	 * 
	 * 
	 * 
	 */
	protected void setAdjustBillFlag() {
		try {
			nc.ui.pub.ClientEnvironment ce = nc.ui.pub.ClientEnvironment
					.getInstance();
			if (ce == null) {
				nc.vo.scm.pub.SCMEnv.out("ce null ERR.");
				return;
			}
			if (getBillCardPanel().getTailItem("vadjuster") != null)
				getBillCardPanel().setTailItem("vadjuster", m_sUserID);
			if (getBillCardPanel().getTailItem("vadjustername") != null)
				getBillCardPanel().setTailItem("vadjustername", m_sUserName);
			if (m_voBill != null) {
				m_voBill.setHeaderValue("vadjuster", m_sUserID);
				m_voBill.setHeaderValue("vadjustername", m_sUserName);
			}
		} catch (Exception e) {
		}

	}

	/**
	 * 创建者：王乃军 功能：设置审批人。 参数： 返回： 例外： 日期：(2001-5-9 9:23:32) 修改日期，修改人，修改原因，注释标志：
	 * 
	 * 
	 * 
	 * 
	 */
	protected void setAuditBillFlag() {
		try {
			nc.ui.pub.ClientEnvironment ce = nc.ui.pub.ClientEnvironment
					.getInstance();
			if (ce == null) {
				nc.vo.scm.pub.SCMEnv.out("ce null ERR.");
				return;
			}
			if (getBillCardPanel().getTailItem("cauditorid") != null)
				getBillCardPanel().setTailItem("cauditorid", m_sUserID);
			if (getBillCardPanel().getTailItem("cauditorname") != null)
				getBillCardPanel().setTailItem("cauditorname", m_sUserName);
			if (m_voBill != null) {
				m_voBill.setHeaderValue("cauditorid", m_sUserID);
				m_voBill.setHeaderValue("cauditorname", m_sUserName);
			}
		} catch (Exception e) {
		}

	}

	/**
	 * 创建者：王乃军 功能：单据编辑事件处理 参数：e单据编辑事件 返回： 例外： 日期：(2001-5-8 19:08:05)
	 * 修改日期，修改人，修改原因，注释标志：
	 * 
	 * 
	 * 
	 * 
	 */
	public void afterAstUOMEdit(int rownow) {
		// 辅计量单位
		String sAstUomPK = ((nc.ui.pub.beans.UIRefPane) getBillCardPanel()
				.getBodyItem("castunitname").getComponent()).getRefPK();
		String sAstUomname = ((nc.ui.pub.beans.UIRefPane) getBillCardPanel()
				.getBodyItem("castunitname").getComponent()).getRefName();
		m_voBill.setItemValue(rownow, "castunitid", sAstUomPK);
		m_voBill.setItemValue(rownow, "castunitname", sAstUomname);
		getBillCardPanel().setBodyValueAt(sAstUomPK, rownow, "castunitid");
		getBillCardPanel().setBodyValueAt(sAstUomname, rownow, "castunitname");

		// 保存名称以在列表形式下显示。
		try {
			nc.vo.bd.b15.MeasureRateVO voMeas = m_voInvMeas.getMeasureRate(
					m_voBill.getItemInv(rownow).getCinventoryid(), sAstUomPK);
			if (voMeas != null) {
				UFDouble hsl = voMeas.getMainmeasrate();
				getBillCardPanel().setBodyValueAt(hsl, rownow, "hsl");
				getBillCardPanel().updateUI();

				hsl = (UFDouble) getBillCardPanel().getBodyValueAt(rownow,
						"hsl");
				m_voBill.setItemValue(rownow, "hsl", hsl);
				m_voBill.setItemValue(rownow, "isSolidConvRate", voMeas
						.getFixedflag());
			}
			String[] sIKs = getClearIDs(1, "castunitname");
			clearRowData(rownow, sIKs);

		} catch (Exception e2) {
			nc.vo.scm.pub.SCMEnv.error(e2);
		}
	}

	/**
	 * 此处插入方法说明。 创建者：张欣 功能： 参数： 返回： 例外： 日期：(2001-6-20 21:43:07)
	 * 修改日期，修改人，修改原因，注释标志：
	 * 
	 * @param param
	 *            nc.ui.pub.bill.BillEditEvent
	 */
	public void afterLotEdit(String s, int rownow) {
		getLotRefbyHand(s);
		pickupLotRef(s);
		String sLot = (String) getBillCardPanel().getBodyValueAt(rownow, "vbatchcode");
		if (sLot == null || sLot.trim().length() == 0){
			String[] sIKs = getClearIDs(1, "vbatchcode");
			clearRowData(rownow, sIKs);
		}
	}

	/**
	 * 创建者：仲瑞庆 功能：由传入的单据类型、字段，获得当该字段改变后，应改变的其他字段列表 参数：iBillFlag
	 * 单据类型，当为普通单据，传入0，当为特殊单据，传入1 已有 存货 cinventorycode， 表体仓库 cwarehousename， 自由项
	 * vfree0， 表头出库仓库 coutwarehouseid， 表头仓库 cwarehouseid 返回： 例外： 日期：(2001-7-18
	 * 上午 9:20) 修改日期，修改人，修改原因，注释标志：
	 * 
	 * @return java.lang.String[]
	 * @param sWhatChange
	 *            java.lang.String
	 */
	protected String[] getClearIDs(int iBillFlag, String sWhatChange) {
		if (sWhatChange == null)
			return null;
		String[] sReturnString = null;
		sWhatChange = sWhatChange.trim();
		if (sWhatChange.equals("cinventorycode")) {
			// 存货
			sReturnString = new String[19];
			int i = 0;
			sReturnString[i++] = "vbatchcode";
			sReturnString[i++] = "vfree0";
			sReturnString[i++] = m_sNumItemKey;
			sReturnString[i++] = m_sAstItemKey;
			sReturnString[i++] = "scrq";
			sReturnString[i++] = "dvalidate";
			sReturnString[i++] = "naccountnum";
			sReturnString[i++] = "naccountastnum";
			sReturnString[i++] = "nchecknum";
			sReturnString[i++] = "ncheckastnum";
			sReturnString[i++] = "cysl";
			sReturnString[i++] = "cyfsl";
			sReturnString[i++] = "je";
			sReturnString[i++] = "nadjustnum"; // 调整数量
			sReturnString[i++] = "nadjustastnum"; // 调整辅数量
			sReturnString[i++] = "naccountgrsnum";
			sReturnString[i++] = "ncheckgrsnum";
			sReturnString[i++] = "nadjustgrsnum";
			sReturnString[i++] = "ndiffgrsnum";

		} else if (sWhatChange.equals("vfree0")
				|| sWhatChange.equals("cvendorname")
				|| sWhatChange.equals("hsl")) {
			// 自由项
			sReturnString = new String[12];
			int i = 0;
			sReturnString[i++] = "naccountnum";
			sReturnString[i++] = "naccountastnum";
			sReturnString[i++] = "cysl";
			sReturnString[i++] = "cyfsl";
			sReturnString[i++] = m_sNumItemKey;
			sReturnString[i++] = m_sAstItemKey;
			sReturnString[i++] = "je";
			sReturnString[i++] = "nadjustnum"; // 调整数量
			sReturnString[i++] = "nadjustastnum"; // 调整辅数量
			sReturnString[i++] = "naccountgrsnum";
			sReturnString[i++] = "nadjustgrsnum";
			sReturnString[i++] = "ndiffgrsnum";

			// showWarningMessage("请重新确认批次号，再执行账面取数！");
			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"4008spec", "UPP4008spec-000204")/*
														 * @res
														 * "自由项修改，请重新确认批次号，再执行账面取数!"
														 */);
			return sReturnString;
		} else if (sWhatChange.equals("vbatchcode")) {
			// 批次号
			sReturnString = new String[12];
			int i = 0;
			sReturnString[i++] = "naccountnum";
			sReturnString[i++] = "naccountastnum";
			sReturnString[i++] = "cysl";
			sReturnString[i++] = "cyfsl";
			sReturnString[i++] = m_sNumItemKey;
			sReturnString[i++] = m_sAstItemKey;
			sReturnString[i++] = "je";
			sReturnString[i++] = "nadjustnum"; // 调整数量
			sReturnString[i++] = "nadjustastnum"; // 调整辅数量
			sReturnString[i++] = "naccountgrsnum";
			sReturnString[i++] = "nadjustgrsnum";
			sReturnString[i++] = "ndiffgrsnum";

			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"4008spec", "UPP4008spec-000205")/*
														 * @res
														 * "批次号修改，请再次执行账面取数!"
														 */);
			return sReturnString;
		} else if (sWhatChange.equals("coutwarehouseid")
				|| sWhatChange.equals("cinwarehouseid")) {
			sReturnString = new String[14];
			int i = 0;
			sReturnString[i++] = "naccountnum";
			sReturnString[i++] = "naccountastnum";
			sReturnString[i++] = "cysl";
			sReturnString[i++] = "cyfsl";
			sReturnString[i++] = m_sNumItemKey;
			sReturnString[i++] = m_sAstItemKey;
			sReturnString[i++] = "je";
			sReturnString[i++] = "nadjustnum"; // 调整数量
			sReturnString[i++] = "nadjustastnum"; // 调整辅数量
			sReturnString[i++] = "scpacename";
			sReturnString[i++] = "scpaceid";
			sReturnString[i++] = "naccountgrsnum";
			sReturnString[i++] = "nadjustgrsnum";
			sReturnString[i++] = "ndiffgrsnum";

			// showWarningMessage("请重新确认批次号，再执行帐面取数！");
			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"4008spec", "UPP4008spec-000206")/*
														 * @res
														 * "仓库修改，请确认表体行各项后，再次执行账面取数!"
														 */);
			return sReturnString;
		} else if ((sWhatChange.equals("cwarehouseid"))) {
			sReturnString = new String[18];
			int i = 0;
			sReturnString[i++] = "vbatchcode";
			sReturnString[i++] = m_sNumItemKey;
			sReturnString[i++] = m_sAstItemKey;
			sReturnString[i++] = "scrq";
			sReturnString[i++] = "dvalidate";
			sReturnString[i++] = "naccountnum";
			sReturnString[i++] = "naccountastnum";
			sReturnString[i++] = "nchecknum";
			sReturnString[i++] = "ncheckastnum";
			sReturnString[i++] = "cysl";
			sReturnString[i++] = "cyfsl";
			sReturnString[i++] = "je";
			sReturnString[i++] = "nadjustnum"; // 调整数量
			sReturnString[i++] = "nadjustastnum"; // 调整辅数量
			sReturnString[i++] = "naccountgrsnum";
			sReturnString[i++] = "ncheckgrsnum";
			sReturnString[i++] = "nadjustgrsnum";
			sReturnString[i++] = "ndiffgrsnum";

			// showWarningMessage("请重新确认批次号，再执行账面取数！");
			return null;
		} else if (sWhatChange.equals("castunitname")) {
			// 辅计量
			sReturnString = new String[12];
			int i = 0;
			sReturnString[i++] = m_sNumItemKey;
			sReturnString[i++] = m_sAstItemKey;
			sReturnString[i++] = "naccountnum";
			sReturnString[i++] = "naccountastnum";
			sReturnString[i++] = "cysl";
			sReturnString[i++] = "cyfsl";
			sReturnString[i++] = "je";
			sReturnString[i++] = "nadjustnum"; // 调整数量
			sReturnString[i++] = "nadjustastnum"; // 调整辅数量
			sReturnString[i++] = "naccountgrsnum";
			sReturnString[i++] = "nadjustgrsnum";
			sReturnString[i++] = "ndiffgrsnum";

			// showWarningMessage("请重新确认批次号，再执行账面取数！");
			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"4008spec", "UPP4008spec-000207")/*
														 * @res
														 * "辅计量修改，请再次执行账面取数!"
														 */);
			return sReturnString;
		} else if (sWhatChange.equals("cspaceid")) {
			sReturnString = new String[12];
			int i = 0;
			sReturnString[i++] = m_sNumItemKey;
			sReturnString[i++] = m_sAstItemKey;
			sReturnString[i++] = "naccountnum";
			sReturnString[i++] = "naccountastnum";
			sReturnString[i++] = "cysl";
			sReturnString[i++] = "cyfsl";
			sReturnString[i++] = "je";
			sReturnString[i++] = "nadjustnum";
			sReturnString[i++] = "nadjustastnum";
			sReturnString[i++] = "naccountgrsnum";
			sReturnString[i++] = "nadjustgrsnum";
			sReturnString[i++] = "ndiffgrsnum";

			// showWarningMessage("请重新执行账面取数！");
			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"4008spec", "UPP4008spec-000208")/*
														 * @res
														 * "货位修改，请再次执行账面取数!"
														 */);
			return sReturnString;
		}
		return sReturnString;
	}

	/**
	 * ClientUI 构造子注解。 nc 2.2 提供的单据联查功能构造子。
	 * 
	 */
	public ClientUI(String pk_corp, String billType, String businessType,
			String operator, String billID) {

		super();
		nc.vo.scm.pub.SCMEnv.out("+++ydy++ClientUI+operator:" + operator);
		m_sBillTypeCode = nc.vo.ic.pub.BillTypeConst.m_check;
		// 调用的各个节点的BO_Client接口,应被子类修改
		// sSpecialHBO_Client = "nc.ui.ic.ic261.SpecialHBO_Client";
		m_sPNodeCode = "40081016";
		// 初始化界面
		initialize(pk_corp, operator, "jc", businessType, "0001", "2003-04-17");
		// 查单据
		SpecialBillVO voBill = qryBill(pk_corp, m_sBillTypeCode, businessType,
				operator, billID);
		if (voBill == null)
			nc.ui.pub.beans.MessageDialog.showHintDlg(this,
					nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec",
							"UPPSCMCommon-000270")/* @res "提示" */,
					nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec",
							"UPP4008spec-000121")/* @res "没有符合查询条件的单据！" */);
		else
			// 显示单据
			setBillValueVO(voBill);

	}

	/**
	 * 创建者： 功能： 参数：e单据编辑事件 返回： 例外： 日期：(2001-5-8 19:08:05) 修改日期，修改人，修改原因，注释标志：
	 * 
	 * 
	 * 
	 * 
	 */
	public void afterDshldtransnumEdit() {
		// 套件数量更改配件数量
		try {
			if (null != m_voBill.getItemValue(0, "dshldtransnum")) {
				// 得到总价率
				UFDouble ufdTotal = new UFDouble("0");
				if (m_voBill != null) {
					for (int i = 0; i < getBillCardPanel().getRowCount(); i++) {
						UFDouble ufdChildsnum = new UFDouble("0");
						UFDouble ufdPartpercent = new UFDouble("0");
						if (m_voBill.getItemValue(i, "childsnum") != null) {
							ufdChildsnum = new UFDouble(m_voBill.getItemValue(
									i, "childsnum").toString());
						}
						if (m_voBill.getItemValue(i, "partpercent") != null) {
							ufdPartpercent = new UFDouble(m_voBill
									.getItemValue(i, "partpercent").toString());
						}
						ufdTotal = ufdChildsnum.multiply(ufdPartpercent);
					}
				}
				UFDouble iBaseNum = new UFDouble(m_voBill.getItemValue(0,
						"dshldtransnum").toString().trim());
				int col = getBillCardPanel().getBodyColByKey("dshldtransnum");
				col = getBillCardPanel().getBillTable()
						.convertColumnIndexToView(col);
				if (m_voBill != null) {

					getBillCardPanel().getBillModel().setNeedCalculate(false);
					for (int i = 1; i < getBillCardPanel().getRowCount(); i++) {
						if ((m_voBill.getItemValue(i, "fbillrowflag") != null)
								&& (Integer.parseInt(m_voBill.getItemValue(i,
										"fbillrowflag").toString().trim()) == BillRowType.part)) {
							// 对每一行计算数量
							UFDouble childs = new UFDouble("0");
							if (m_voBill.getItemValue(i, "childsnum") != null) {
								childs = new UFDouble(m_voBill.getItemValue(i,
										"childsnum").toString());
							}
							UFDouble value = childs.multiply(iBaseNum);
							m_voBill.setItemValue(i, "dshldtransnum", value);
							getBillCardPanel().setBodyValueAt(value, i,
									"dshldtransnum");

							// 计算单价
							UFDouble pricenow = new UFDouble("0");
							UFDouble ufdPartpercent = new UFDouble("0");
							if (m_voBill.getItemValue(i, "partpercent") != null) {
								ufdPartpercent = new UFDouble(m_voBill
										.getItemValue(i, "partpercent")
										.toString());
							}
							UFDouble ufdPriceSet = new UFDouble("0");
							if (m_voBill.getItemValue(0, "nprice") == null) {
								ufdPriceSet = (UFDouble) m_voBill.getItemValue(
										0, "nprice");
							}
							;
							pricenow = ((UFDouble) m_voBill.getItemValue(i,
									"partpercent")).div(ufdTotal).multiply(
									ufdPriceSet);
							m_voBill.setItemValue(i, "nprice", pricenow);
							getBillCardPanel().setBodyValueAt(pricenow, i,
									"nprice");

							// 执行编辑公式，计算辅数量，金额，计划金额
							// getBillCardPanel().getBillModel().execEditFormula(i,
							// col);
						}
					}

					getBillCardPanel().getBillModel().setNeedCalculate(true);
					getBillCardPanel().getBillModel().reCalcurateAll();

				}
			}
		} catch (Exception e2) {
			nc.vo.scm.pub.SCMEnv.error(e2);
		}

	}

	/**
	 * 创建者：仲瑞庆 功能：换算率修改 参数： 返回： 例外： 日期：(2001-11-20 14:01:52) 修改日期，修改人，修改原因，注释标志：
	 * 
	 * @param row
	 *            int
	 */
	public void afterHslEdit(int iRow) {
		if (getBillCardPanel().getBodyValueAt(iRow, "hsl") == null
				|| getBillCardPanel().getBodyValueAt(iRow, "hsl").toString()
						.trim().length() == 0) {
			getBillCardPanel().setBodyValueAt(new UFDouble(0), iRow, "hsl");
		}
		UFDouble hsl = (UFDouble) getBillCardPanel()
				.getBodyValueAt(iRow, "hsl");
		m_voBill.setItemValue(iRow, "hsl", hsl);

		String[] sIKs = getClearIDs(1, "castunitname");
		clearRowData(iRow, sIKs);

		// if (m_voBill.getItemInv(iRow).getCinventoryid() != null) {
		// calculateByHsl(iRow, "dshldtransnum", "nshldtransastnum", 1);
		// }
	}

	/**
	 * 创建者：王乃军 功能：存货事件处理,只在盘点选择后使用 参数：e单据编辑事件 返回： 例外： 日期：(2001-5-8 19:08:05)
	 * 修改日期，修改人，修改原因，注释标志：
	 */
	public ArrayList afterInvsEdit(ArrayList alInvvo) {
		try {
			if (alInvvo == null || alInvvo.size() == 0)
				return null;
			nc.vo.scm.pub.SCMEnv.out("读存货数据。。。。");

			long lTimeBegin = System.currentTimeMillis();
			long lTime = System.currentTimeMillis();
			ArrayList alInvFullInfo = new ArrayList();
			String sTempID2 = null;
			if (getBillCardPanel().getHeadItem("coutwarehouseid") != null)
				sTempID2 = getBillCardPanel().getHeadItem("coutwarehouseid")
						.getValue();

			// repair by hanwei 2003-2-18 按公式解析
			nc.vo.scm.ic.bill.InvVO[] voInvs = null;
			if (isByFormula()) {
				lTime = System.currentTimeMillis();
				String sWarehouseid = ((nc.ui.pub.beans.UIRefPane) getBillCardPanel()
						.getHeadItem("coutwarehouseid").getComponent())
						.getRefPK();
				// 通过仓库获得库存组织ID
				String sCalID = null;
				if (sWarehouseid != null) {
					try {
						Object[] ov = (Object[]) nc.ui.scm.pub.CacheTool
								.getCellValue("bd_stordoc", "pk_stordoc",
										"pk_calbody", sWarehouseid);
						if (ov != null && ov.length > 0)
							sCalID = ov[0].toString();
					} catch (Exception e1) {
						nc.vo.scm.pub.SCMEnv.out(e1.toString());
					}
				}

				InvoInfoBYFormula invoInfoBYFormula = getInvoInfoBYFormula();
				// 修改 hanwei 2003-07-04
				int iLen = alInvvo.size();
				voInvs = new InvVO[iLen];
				alInvvo.toArray(voInvs);
				voInvs = invoInfoBYFormula.getInvParseWithPlanPrice(voInvs,
						sWarehouseid, sCalID, false, true);
				nc.vo.scm.pub.SCMEnv.showTime(lTime, "再次公式查询存货档案时间：");

			} else {
				lTime = System.currentTimeMillis();
				String[] saInvCode = new String[alInvvo.size()];
				for (int i = 0; i < alInvvo.size(); i++) {
					saInvCode[i] = (String) ((InvVO) alInvvo.get(i))
							.getAttributeValue("cinventoryid");
				}
				voInvs = SpecialHHelper.queryInvsInfo(sTempID2, saInvCode,
						m_sUserID, m_sCorpID);
				// reportTime(lTime, "再次DB查询存货档案时间：");
			}

			InvVO voInv = null;
			UFDate productDate = null;
			lTime = System.currentTimeMillis();
			for (int row = 0; row < voInvs.length; row++) {
				voInv = voInvs[row];
				// 算生产日期
				voInv.calPrdDate();
				m_voBill.setItemInv(row, voInv);
				if (voInv.getCvendorid() != null)
					m_voBill.setItemValue(row, "cvendorid", voInv
							.getCvendorid());// zhy2006-11-28在外围为供应商赋值,否则盘点选择后界面上没有供应商的值
				m_voBill.getItemVOs()[row].setCrowno(null);
				alInvFullInfo.add(voInv);
				// //表体
				// setBodyInvValue(row, voInv);
			}
			nc.vo.scm.pub.SCMEnv.showTime(lTime, "算生产日期");

			lTime = System.currentTimeMillis();
			try {
				// 按存货+批次号+货位id+供应商id+hsl,排序
				ArrayList alSortResut = nc.vo.scm.pub.SortMethod.sortByKeys(
						new String[] { "cinventorycode", "vbatchcode",
								"cspaceid", "cvendorid", "hsl" }, new int[] {
								nc.vo.scm.pub.SortType.ASC,
								nc.vo.scm.pub.SortType.ASC,
								nc.vo.scm.pub.SortType.ASC }, m_voBill
								.getItemVOs());
				// element size 2
				if (alSortResut != null
						&& alSortResut.size() == 2
						&& alSortResut.get(1) != null
						&& ((SpecialBillItemVO[]) alSortResut.get(1)).length == m_voBill
								.getItemCount()) {
					m_voBill.setChildrenVO((SpecialBillItemVO[]) (alSortResut
							.get(1)));
				}
			} catch (NoClassDefFoundError e) {
				nc.vo.scm.pub.SCMEnv.out("cannot find..........."
						+ e.getMessage());
			}

			nc.vo.scm.pub.SCMEnv.showTime(lTime, "按存货+批次号+货位id,排序");

			lTime = System.currentTimeMillis();
			// 增加单据行号：zhx added on 20030630 support for incoming bill
			nc.ui.scm.pub.report.BillRowNo.setVORowNoByRule(m_voBill,
					m_sBillTypeCode, m_sBillRowNo);
			// /

			nc.vo.scm.pub.SCMEnv.showTime(lTime, "行号");
			lTime = System.currentTimeMillis();
			// 不需要执行界面公式
			setBillValueVO(m_voBill, false);
			nc.vo.scm.pub.SCMEnv.showTime(lTime, "setBillValueVO(m_voBill);");
			// 以下的信息需要优化，如果批次号未显示，则无需显示。
			if (isByFormula()) {
				showTime(lTimeBegin, "存货VO用公式查询，总时间：");
			} else {
				showTime(lTimeBegin, "存货VO用DB查询，总时间：");
			}

			// showHintMessage("存货修改，请重新确认自由项、批次、数量。");
			return alInvvo;
		} catch (Exception e2) {
			nc.vo.scm.pub.SCMEnv.error(e2);
		}
		return null;
	}
	
	
	
	public ArrayList afterInvsEdit_MD(ArrayList alInvvo) {
		try {
			if (alInvvo == null || alInvvo.size() == 0)
				return null;
			nc.vo.scm.pub.SCMEnv.out("读存货数据。。。。");

			long lTimeBegin = System.currentTimeMillis();
			long lTime = System.currentTimeMillis();
			ArrayList alInvFullInfo = new ArrayList();
			String sTempID2 = null;
			if (getBillCardPanel().getHeadItem("coutwarehouseid") != null)
				sTempID2 = getBillCardPanel().getHeadItem("coutwarehouseid")
						.getValue();

			// repair by hanwei 2003-2-18 按公式解析
			nc.vo.scm.ic.bill.InvVO[] voInvs = null;
			if (isByFormula()) {
				lTime = System.currentTimeMillis();
				String sWarehouseid = ((nc.ui.pub.beans.UIRefPane) getBillCardPanel()
						.getHeadItem("coutwarehouseid").getComponent())
						.getRefPK();
				// 通过仓库获得库存组织ID
				String sCalID = null;
				if (sWarehouseid != null) {
					try {
						Object[] ov = (Object[]) nc.ui.scm.pub.CacheTool
								.getCellValue("bd_stordoc", "pk_stordoc",
										"pk_calbody", sWarehouseid);
						if (ov != null && ov.length > 0)
							sCalID = ov[0].toString();
					} catch (Exception e1) {
						nc.vo.scm.pub.SCMEnv.out(e1.toString());
					}
				}

				InvoInfoBYFormula invoInfoBYFormula = getInvoInfoBYFormula();
				// 修改 hanwei 2003-07-04
				int iLen = alInvvo.size();
				voInvs = new InvVO[iLen];
				alInvvo.toArray(voInvs);
				voInvs = invoInfoBYFormula.getInvParseWithPlanPrice(voInvs,
						sWarehouseid, sCalID, false, true);
				nc.vo.scm.pub.SCMEnv.showTime(lTime, "再次公式查询存货档案时间：");

			} else {
				lTime = System.currentTimeMillis();
				String[] saInvCode = new String[alInvvo.size()];
				for (int i = 0; i < alInvvo.size(); i++) {
					saInvCode[i] = (String) ((InvVO) alInvvo.get(i))
							.getAttributeValue("cinventoryid");
				}
				voInvs = SpecialHHelper.queryInvsInfo(sTempID2, saInvCode,
						m_sUserID, m_sCorpID);
				// reportTime(lTime, "再次DB查询存货档案时间：");
			}

			InvVO voInv = null;
			UFDate productDate = null;
			lTime = System.currentTimeMillis();
			for (int row = 0; row < voInvs.length; row++) {
				voInv = voInvs[row];
				// 算生产日期
				voInv.calPrdDate();
				m_voBill.setItemInv(row, voInv);
				
				//add by ouyangzhb 2012-04-24 
				m_voBill.setItemValue(row, "vuserdef1", voInv.getVdef1());
				m_voBill.setItemValue(row, "vuserdef2", voInv.getVdef2());
				m_voBill.setItemValue(row, "vuserdef3", voInv.getVdef3());
				m_voBill.setItemValue(row, "vuserdef4", voInv.getVdef4());
				//add end 
				
				if (voInv.getCvendorid() != null)
					m_voBill.setItemValue(row, "cvendorid", voInv
							.getCvendorid());// zhy2006-11-28在外围为供应商赋值,否则盘点选择后界面上没有供应商的值
				m_voBill.getItemVOs()[row].setCrowno(null);
				alInvFullInfo.add(voInv);
				// //表体
				// setBodyInvValue(row, voInv);
			}
			nc.vo.scm.pub.SCMEnv.showTime(lTime, "算生产日期");

			lTime = System.currentTimeMillis();
			try {
				// 按存货+批次号+货位id+供应商id+hsl,排序
				ArrayList alSortResut = nc.vo.scm.pub.SortMethod.sortByKeys(
						new String[] { "cinventorycode", "vbatchcode",
								"cspaceid", "cvendorid", "hsl" }, new int[] {
								nc.vo.scm.pub.SortType.ASC,
								nc.vo.scm.pub.SortType.ASC,
								nc.vo.scm.pub.SortType.ASC }, m_voBill
								.getItemVOs());
				// element size 2
				if (alSortResut != null
						&& alSortResut.size() == 2
						&& alSortResut.get(1) != null
						&& ((SpecialBillItemVO[]) alSortResut.get(1)).length == m_voBill
								.getItemCount()) {
					m_voBill.getHeaderVO().setVuserdef1("Y");
					m_voBill.setChildrenVO((SpecialBillItemVO[]) (alSortResut
							.get(1)));
				}
			} catch (NoClassDefFoundError e) {
				nc.vo.scm.pub.SCMEnv.out("cannot find..........."
						+ e.getMessage());
			}

			nc.vo.scm.pub.SCMEnv.showTime(lTime, "按存货+批次号+货位id,排序");

			lTime = System.currentTimeMillis();
			// 增加单据行号：zhx added on 20030630 support for incoming bill
			nc.ui.scm.pub.report.BillRowNo.setVORowNoByRule(m_voBill,
					m_sBillTypeCode, m_sBillRowNo);
			// /

			nc.vo.scm.pub.SCMEnv.showTime(lTime, "行号");
			lTime = System.currentTimeMillis();
			// 不需要执行界面公式
			setBillValueVO(m_voBill, false);
			nc.vo.scm.pub.SCMEnv.showTime(lTime, "setBillValueVO(m_voBill);");
			// 以下的信息需要优化，如果批次号未显示，则无需显示。
			if (isByFormula()) {
				showTime(lTimeBegin, "存货VO用公式查询，总时间：");
			} else {
				showTime(lTimeBegin, "存货VO用DB查询，总时间：");
			}

			// showHintMessage("存货修改，请重新确认自由项、批次、数量。");
			return alInvvo;
		} catch (Exception e2) {
			nc.vo.scm.pub.SCMEnv.error(e2);
		}
		return null;
	}

	

	/**
	 * 创建者：仲瑞庆 功能：货位修改事件处理 参数：e单据编辑事件 返回： 例外： 日期：(2001-5-8 19:08:05)
	 * 修改日期，修改人，修改原因，注释标志：
	 * 
	 * 
	 * 
	 * 
	 */
	public void afterSpaceEdit(nc.ui.pub.bill.BillEditEvent e) {
		// 出货位参照
		nc.ui.pub.beans.UIRefPane refOutSpace = null;
		if (getBillCardPanel().getBodyItem("cspacename") != null)
			refOutSpace = ((nc.ui.pub.beans.UIRefPane) getBillCardPanel()
					.getBodyItem("cspacename").getComponent());
		// 修改列Key
		String sItemKey = e.getKey();
		// 当前行
		int row = e.getRow();
		String cOutsname = refOutSpace.getRefName();
		String cOutspaceid = refOutSpace.getRefPK();
		// 如果ID是空则清除name
		if (cOutspaceid == null)
			cOutsname = null;
		// String cOutsname = refOutSpace.getRefName();
		// String cOutspaceid = refOutSpace.getRefPK();
		// String cInsname =refInSpace.getRefName();
		// String cInspaceid = refInSpace.getRefPK();
		// 两个货位不能相同
		showHintMessage("");
		// 设置m_vo
		m_voBill.setItemValue(row, "cspaceid", cOutspaceid);
		m_voBill.setItemValue(row, "cspacename", cOutsname);

		// 置入界面
		getBillCardPanel().setBodyValueAt(cOutsname, row, "cspacename");
		getBillCardPanel().setBodyValueAt(cOutspaceid, row, "cspaceid");

		String[] sIKs = getClearIDs(1, "cspaceid");
		// int iRowCount= getBillCardPanel().getRowCount();
		// for (int rownow= 0; rownow < iRowCount; rownow++)
		clearRowData(row, sIKs);
		// 以下的信息需要优化，如果批次号未显示，则无需显示。
		showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec",
				"UPP4008spec-000209")/* @res "货位修改，请重做账面取数。" */);
	}

	// private boolean m_bHslWeiDu = false;//hsl是否作为取数维度

	// /**
	// * 获得界面是否将hsl作为纬度
	// *
	// * @param bWarehouseRefEnable
	// */
	// private boolean isHslConfig() {
	//
	// return m_bHslWeiDu;
	//
	// }

	/**
	 * 创建者：仲瑞庆 功能：仓库改变事件处理 参数：e单据编辑事件 返回： 例外： 日期：(2001-5-8 19:08:05)
	 * 修改日期，修改人，修改原因，注释标志：
	 * 
	 * bWarehouseRefEnable:编辑后，仓库参照是否可用 对于盘点选择，编辑后，仓库参照不可用
	 * 
	 * 
	 */
	public void afterWhOutEdit(boolean bWarehouseRefEnable) {
		afterWhOutEditByDBQury(bWarehouseRefEnable);
	}

	/**
	 * 创建者：仲瑞庆 功能：仓库改变事件处理 参数：e单据编辑事件 返回： 例外： 日期：(2001-5-8 19:08:05)
	 * 修改日期，修改人，修改原因，注释标志： bWarehouseRefEnable:编辑后，仓库参照是否可用 对于盘点选择，编辑后，仓库参照不可用
	 * 
	 * 
	 * 
	 * 
	 */
	public void afterWhOutEditByDBQury(boolean bWarehouseRefEnable) {
		// 仓库
		try {
			String sName = ((nc.ui.pub.beans.UIRefPane) getBillCardPanel()
					.getHeadItem("coutwarehouseid").getComponent())
					.getRefName();
			String sID = ((nc.ui.pub.beans.UIRefPane) getBillCardPanel()
					.getHeadItem("coutwarehouseid").getComponent()).getRefPK();
			
			nc.vo.scm.ic.bill.WhVO voWh = null;
			if(!hm_whid_whvo.containsKey(sID))
			  voWh = (nc.vo.scm.ic.bill.WhVO) SpecialBillHelper
					.queryInfo(new Integer(1), sID);
			  else
			    voWh = hm_whid_whvo.get(sID);
			if (m_voBill != null) {
				m_voBill.setWhOut(voWh);
				// 清表尾现存量
				m_voBill.clearInvQtyInfo();
				// 清批次/自由项
				String[] sIKs = getClearIDs(1, "coutwarehouseid");
				int iRowCount = getBillCardPanel().getRowCount();
				for (int row = 0; row < iRowCount; row++)
					clearRowData(row, sIKs);
				// 刷新现存量显示
				// setTailValue(0);

				// 清表体行所有数据
				// for (int i= 0; i < getBillCardPanel().getRowCount(); i++) {
				// clearRowData(i);
				// }
				// 以下的信息需要优化，如果批次号未显示，则无需显示。
				showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"4008spec", "UPP4008spec-000210")/*
															 * @res
															 * "出库仓库修改，请重新确认自由项、批次、数量。"
															 */);
				// 对于盘点选择，编辑后，仓库参照不可用
				getBillCardPanel().getBillData().getHeadItem("coutwarehouseid")
						.setEnabled(false);
				// 保存名称以在列表形式下显示。
				getBillCardPanel().getBillData().getHeadItem("coutwarehouseid")
						.setEnabled(bWarehouseRefEnable);
				getBillCardPanel().getBillData().getHeadItem("coutwarehouseid")
						.setEdit(bWarehouseRefEnable);
			}
		} catch (Exception e2) {
			nc.vo.scm.pub.SCMEnv.error(e2);
		}

	}

	/**
	 * 创建者：仲瑞庆 功能：仓库改变事件处理 参数：e单据编辑事件 返回： 例外： 日期：(2001-5-8 19:08:05)
	 * 修改日期，修改人，修改原因，注释标志：
	 * 
	 * 
	 * 
	 * 
	 */
	public void afterWhOutEditByFormula() {
		// 仓库
		try {
			String sName = ((nc.ui.pub.beans.UIRefPane) getBillCardPanel()
					.getHeadItem("coutwarehouseid").getComponent())
					.getRefName();
			String sID = ((nc.ui.pub.beans.UIRefPane) getBillCardPanel()
					.getHeadItem("coutwarehouseid").getComponent()).getRefPK();

			getBillCardPanel().getBillData().getHeadItem("coutwarehouseid")
					.setEnabled(false);
			// 保存名称以在列表形式下显示。

			nc.vo.scm.ic.bill.WhVO voWh = (nc.vo.scm.ic.bill.WhVO) SpecialBillHelper
					.queryInfo(new Integer(1), sID);
			if (m_voBill != null) {
				m_voBill.setWhOut(voWh);
				// 清表尾现存量
				m_voBill.clearInvQtyInfo();
				// 清批次/自由项
				String[] sIKs = getClearIDs(1, "coutwarehouseid");
				int iRowCount = getBillCardPanel().getRowCount();
				for (int row = 0; row < iRowCount; row++)
					clearRowData(row, sIKs);
				// 刷新现存量显示
				// setTailValue(0);

				// 清表体行所有数据
				// for (int i= 0; i < getBillCardPanel().getRowCount(); i++) {
				// clearRowData(i);
				// }
				// 以下的信息需要优化，如果批次号未显示，则无需显示。
				showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"4008spec", "UPP4008spec-000210")/*
															 * @res
															 * "出库仓库修改，请重新确认自由项、批次、数量。"
															 */);
				getBillCardPanel().getBillData().getHeadItem("coutwarehouseid")
						.setEdit(false);
			}
		} catch (Exception e2) {
			nc.vo.scm.pub.SCMEnv.error(e2);
		}

	}

	/**
	 * 编辑前处理。 创建日期：(2001-3-23 2:02:27)
	 * 
	 * @param e
	 *            ufbill.BillEditEvent
	 */
	public boolean beforeEdit(nc.ui.pub.bill.BillEditEvent e) {
		return true;
	}

	public boolean isCellEditable(boolean value, int row, String itemkey) {
		boolean isEditable = super.isCellEditable(value, row, itemkey);
		String sItemKey = itemkey;
		BillItem bi = getBillCardPanel().getBodyItem(sItemKey);
		int iRow = row;
		if (isEditable) {
			// 帐面取数后仍然可以增行，修改，所以下面的限制取消
			if (sItemKey.equals("naccountnum")
					|| sItemKey.equals("naccountastnum")) {
				return false;
			}

			// 2003-09-03 ydy如果是账面取数状态,只能修改盘点数量和辅数量和差异数量和理由
			// if (isChecking()) {
			// if (!sItemKey.equals("nchecknum") && !sItemKey.equals("nprice")
			// && !sItemKey.equals("nadjustnum")
			// && !sItemKey.equals("je")
			// && !sItemKey.equals("ncheckastnum")
			// && !sItemKey.equals("yy") && !sItemKey.equals("cysl")) {
			// isEditable = false;
			//
			// }
			//
			// }

			// if (sItemKey.equals("dvalidate") || sItemKey.equals("scrq")) {
			// isEditable = false; //盘点单上的保质期不可以编辑
			// }
			// else
			if (sItemKey.equals("cspacename")) {
				if ((null == m_voBill.getWhOut().getIsLocatorMgt())
						|| (m_voBill.getWhOut().getIsLocatorMgt().intValue() != 1)) {
					isEditable = false;
				} else {
					getLocatorRefPane().setOldValue(
							(String) m_voBill.getItemValue(iRow, sItemKey),
							null,
							(String) m_voBill.getItemValue(iRow, "cspaceid"));
					String ColName = getBillCardPanel().getBillData()
							.getBodyItem("cspacename").getName();
					getBillCardPanel().getBodyPanel().getTable().getColumn(
							ColName).setCellEditor(
							new BillCellEditor(getLocatorRefPane()));
					filterSpace(iRow);
				}
			}

			// 向批次号传递参数
			else if (itemkey.equals("vbatchcode")) {
				WhVO wvo = m_voBill.getWhOut();
				getLotNumbRefPane().setParameter(wvo, m_voBill.getItemInv(row));
			}

			if (sItemKey.equals("nchecknum") || sItemKey.equals("ncheckastnum")) {
				if (null != m_voBill.getHeaderVO().getBccountflag()
						&& m_voBill.getHeaderVO().getBccountflag().booleanValue())
				if ((row < m_voBill.getItemVOs().length)
						&& m_voBill.getItemVOs()[row].getBarcodeManagerflag()
								.booleanValue())
					return false;
			}
			if (sItemKey.equals("nbarcodenum")) {
				return false;
			}
		}

		return isEditable;

	}

	private UFDouble getUFValueByItemKey(int iRow, String sKey) {
		if (getBillCardPanel().getBodyValueAt(iRow, sKey) == null
				|| getBillCardPanel().getBodyValueAt(iRow, sKey).toString()
						.trim().length() == 0
				|| ((UFDouble) getBillCardPanel().getBodyValueAt(iRow, sKey))
						.doubleValue() == 0)
			return UFD_ZERO;
		else
			return (UFDouble) getBillCardPanel().getBodyValueAt(iRow, sKey);
	}

	/**
	 * 说明：李俊 主辅数量以及换算率的关系处理： 1。换算率小于0 2。换算率大于0 固定换算率时，主辅数量根据换算率互算。
	 * 变动换算率时，主数量修改：根据辅助数量重新计算换算率；辅数量修改：根据换算率重新计算数量。 换算率变化：根据辅助数量计算数量。 优先级：
	 * 辅数量>换算率>数量
	 * 
	 * 公式：主数量=辅数量*换算率 参数： iWhichChanged: 0 主数量修改 1 辅数量修改
	 * 
	 * 浮动换算率时，如果有换算率 ＝ 数量/辅数量 ， 当数量和辅助数量均为空且换算率非空时，根据非空值算出空值；
	 * 
	 * bHsl是否要修改换算率,由于联动的数量组较多,通常认为第一次调用该方法时置为true,接下来的调用则置为false
	 */
	protected void calculateByHsl(int iRow, String sMainNum, String sAstNum,
			int iWhichChanged, boolean bHsl) {

		UFDouble hsl = getUFValueByItemKey(iRow, "hsl");

		// 换算率大于零
		UFDouble ufdMainNum = null;
		UFDouble ufdAstNum = null;
		ufdMainNum = getUFValueByItemKey(iRow, sMainNum);
		ufdAstNum = getUFValueByItemKey(iRow, sAstNum);
		// 固定换算率辅助计量修改，根据辅助数量和换算率计算主数量
		if (isFixFlag(iRow) == null)
			return;

		if (isFixFlag(iRow).booleanValue()) {
			if (iWhichChanged == 1) {
				ufdMainNum = ufdAstNum.multiply(hsl);
				getBillCardPanel().setBodyValueAt(ufdMainNum, iRow, sMainNum);
				m_voBill.setItemValue(iRow, sAstNum, ufdAstNum);
				m_voBill.setItemValue(iRow, sMainNum, ufdMainNum);
			} else if (iWhichChanged == 0) {
				ufdAstNum = ufdMainNum.div(hsl);
				getBillCardPanel().setBodyValueAt(ufdAstNum, iRow, sAstNum);
				m_voBill.setItemValue(iRow, sAstNum, ufdAstNum);
				m_voBill.setItemValue(iRow, sMainNum, ufdMainNum);
			}
		} else {
			// 变动换算率：辅数量>换算率>主数量
			if (iWhichChanged == 1) {
				if (hsl == UFD_ZERO)
					getBillCardPanel().setBodyValueAt(null, iRow, sMainNum);
				else {
					getBillCardPanel().setBodyValueAt(ufdAstNum.multiply(hsl),
							iRow, sMainNum);
					m_voBill.setItemValue(iRow, sMainNum, ufdAstNum
							.multiply(hsl));
				}
			}
			// 主数量改变(只有数量和换算率，不能带出辅数量）
			else if (iWhichChanged == 0) {
				// 辅助数量为空
				if (ufdAstNum == UFD_ZERO) {
					if (hsl != UFD_ZERO && ufdMainNum != UFD_ZERO) {
						if (!sMainNum.equals("nadjustnum")
								|| !sAstNum.equals("nadjustastnum"))
							getBillCardPanel().setBodyValueAt(
									ufdMainNum.div(hsl), iRow, "sAstNum");
					} else {
						if (!sMainNum.equals("nadjustnum")
								|| !sAstNum.equals("nadjustastnum"))
							getBillCardPanel()
									.setBodyValueAt(null, iRow, "hsl");
					}
				}
				// 辅助数量不为空
				else {
					// 此处的条件去掉,加在调用处,将bHsl设为false
					// if (!sMainNum.equals("nadjustnum")
					// || !sAstNum.equals("nadjustastnum")) {
					if (bHsl) {
						getBillCardPanel().setBodyValueAt(
								ufdMainNum.div(ufdAstNum), iRow, "hsl");
						m_voBill.setItemValue(iRow, "hsl", ufdMainNum
								.div(ufdAstNum));
					} else {
						getBillCardPanel().setBodyValueAt(ufdMainNum.div(hsl),
								iRow, sAstNum);
						m_voBill.setItemValue(iRow, sAstNum, ufdMainNum
								.div(hsl));
					}

					// }
					// //调整辅助数量修改不能修改换算率，因为调整数量修改不能影响数量和辅助数量
					// if (!sMainNum.equals("nadjustnum")
					// || !sAstNum.equals("nadjustastnum")) {
					// //
					// getBillCardPanel().setBodyValueAt(ufdMainNum.div(ufdAstNum),
					// iRow, "hsl");
					// // m_voBill.setItemValue(iRow, "hsl",
					// ufdMainNum.div(ufdAstNum));
					// getBillCardPanel().setBodyValueAt(ufdMainNum.div(hsl),
					// iRow, sAstNum);
					// m_voBill.setItemValue(iRow, "hsl", ufdMainNum.div(hsl));
					// }
				}
			}
		}

	}

	/**
	 * 创建者：仲瑞庆 功能：计算调整数量 参数： 返回： 例外： 日期：(2001-10-26 9:09:14) 修改日期，修改人，修改原因，注释标志：
	 * 
	 * @param row
	 *            int
	 */
	protected void calculateTzsl(int row) {
		// if ((getBillCardPanel().getBodyValueAt(row, "nadjustnum") == null)
		// || (getBillCardPanel().getBodyValueAt(row,
		// "nadjustnum").toString().trim().length()
		// == 0)) {
		// 自动计算主数量
		UFDouble ufdZmsl = m_voBill.getItemVOs()[row].getNaccountnum();
		UFDouble ufdpdsl = m_voBill.getItemVOs()[row].getNchecknum();
		UFDouble ufdTzsl = new UFDouble(0);
		if (ufdZmsl == null)
			ufdZmsl = new UFDouble(0);

		if ((null != ufdZmsl) && (null != ufdpdsl)) {
			ufdTzsl = ufdpdsl.sub(ufdZmsl);
		} else {
			ufdTzsl = null;
		}
		m_voBill.setItemValue(row, "nadjustnum", ufdTzsl);
		getBillCardPanel().setBodyValueAt(ufdTzsl, row, "nadjustnum");
		// }
		// if ((getBillCardPanel().getBodyValueAt(row, "nadjustastnum") == null)
		// || (getBillCardPanel()
		// .getBodyValueAt(row, "nadjustastnum")
		// .toString()
		// .trim()
		// .length()
		// == 0)) {
		// 自动计算辅数量
		UFDouble ufdZmfsl = m_voBill.getItemVOs()[row].getNaccountastnum();
		UFDouble ufdpdfsl = m_voBill.getItemVOs()[row].getNcheckastnum();
		UFDouble ufdTzfsl = new UFDouble(0);
		if (ufdZmfsl == null)
			ufdZmfsl = new UFDouble(0);
		if ((null != ufdZmfsl) && (null != ufdpdfsl)) {
			ufdTzfsl = ufdpdfsl.sub(ufdZmfsl);
		} else {
			ufdTzfsl = null;
		}
		m_voBill.setItemValue(row, "nadjustastnum", ufdTzfsl);
		getBillCardPanel().setBodyValueAt(ufdTzfsl, row, "nadjustastnum");

		// 自动计算调整毛重
		UFDouble ufdZmmz = m_voBill.getItemVOs()[row].getNaccountgrsnum();
		UFDouble ufdpdmz = m_voBill.getItemVOs()[row].getNcheckgrsnum();
		UFDouble ufdTzmz = new UFDouble(0);
		if (ufdZmmz == null)
			ufdZmmz = new UFDouble(0);
		if ((null != ufdZmmz) && (null != ufdpdmz)) {
			ufdTzmz = ufdpdmz.sub(ufdZmmz);
		} else {
			ufdTzmz = null;
		}
		m_voBill.setItemValue(row, "nadjustgrsnum", ufdTzmz);
		getBillCardPanel().setBodyValueAt(ufdTzmz, row, "nadjustgrsnum");

		// }
	}

	/**
	 * 创建者：仲瑞庆 功能： 参数： 返回： 例外： 日期：(2001-8-16 12:53:03) 修改日期，修改人，修改原因，注释标志：
	 * 
	 * @param gbvo
	 *            nc.vo.ic.pub.bill.GeneralBillVO
	 * @param sbvo
	 *            nc.vo.ic.pub.bill.SpecialBillVO
	 * @param iInOutFlag
	 *            int
	 */
	protected GeneralBillHeaderVO changeFromSpecialVOtoGeneralVOAboutHeader(
			GeneralBillVO gbvo, SpecialBillVO sbvo, int iInOutFlag) {
		String[] sHeaderItemKeyName = gbvo.getHeaderVO().getAttributeNames();
		String sName = null;
		for (int i = 0; i < sHeaderItemKeyName.length; i++) {
			if (sHeaderItemKeyName[i] == null)
				continue;
			sName = sHeaderItemKeyName[i].trim();
			if (sbvo.getHeaderValue(sName) != null) {
				gbvo.setHeaderValue(sName, sbvo.getHeaderValue(sName));
			}
			if (sName.equals("cwarehouseid")) {
				gbvo.setHeaderValue(sName, sbvo
						.getHeaderValue("coutwarehouseid"));
			} else if (sName.equals("isLocatorMgt")) {
				gbvo.setWh(sbvo.getWhOut());
			} else if (sName.equals("cwarehousename")) {
				gbvo.setHeaderValue(sName, sbvo
						.getHeaderValue("coutwarehousename"));
			} else if (sName.equals("cdptid")) {
				gbvo.setHeaderValue(sName, sbvo.getHeaderValue("coutdeptid"));
			} else if (sName.equals("cdptname")) {
				gbvo.setHeaderValue(sName, sbvo.getHeaderValue("coutdeptname"));
			} else if (sName.equals("cbizid")) {
				gbvo.setHeaderValue(sName, sbvo.getHeaderValue("coutbsor"));
			} else if (sName.equals("cbizname")) {
				gbvo.setHeaderValue(sName, sbvo.getHeaderValue("coutbsorname"));
			} else if (sName.equals("cbilltypecode")) {
				if (iInOutFlag == InOutFlag.OUT) {
					gbvo.setHeaderValue(sName, BillTypeConst.m_otherOut);
				} else {
					gbvo.setHeaderValue(sName, BillTypeConst.m_otherIn);
				}
			} else if (sName.equals("cgeneralhid")) { // 清表头行中PK
				gbvo.setHeaderValue(sName, "");
			} else if (sName.equals("vbillcode")) { // 清表单单据号
				gbvo.setHeaderValue(sName, "");
			} else if (sName.equals("coperatorid")) { // 操作员
				gbvo.setHeaderValue(sName, m_sUserID);
			} else if (sName.equals("coperatorname")) { // 操作员
				gbvo.setHeaderValue(sName, m_sUserName);
			} else if (sName.equals("cauditorid")) { // 调出人，普通单不能在此填入
				gbvo.setHeaderValue(sName, null);
			} else if (sName.equals("cauditorname")) { // 调出人，普通单不能在此填入
				gbvo.setHeaderValue(sName, null);
			} else if (sName.equals("vadjuster")) { // 调入人，普通单不能在此填入
				gbvo.setHeaderValue(sName, null);
			} else if (sName.equals("vadjustername")) { // 调入人，普通单不能在此填入
				gbvo.setHeaderValue(sName, null);
				// 库管员 wnj:2003-07-29。
			} else if (sName.equals("cwhsmanagerid")) {
				gbvo.setHeaderValue(sName, sbvo.getHeaderValue("cinbsrid"));
			} else if (sName.equals("cwhsmanagername")) {
				gbvo.setHeaderValue(sName, sbvo.getHeaderValue("cinbsrname"));
			}
		}
		// cwhsmanagerid
		// cwhsmanagername
		// cdptid coutdeptid cindeptid
		// cdptname coutdeptname cindeptname
		// cbizid coutbsor cinbsrid
		// cbizname coutbsorname cinbsrname
		// cproviderid
		// cprovidername
		// ccustomerid
		// ccustomername
		gbvo.getParentVO().setStatus(nc.vo.pub.VOStatus.NEW);
		return (GeneralBillHeaderVO) gbvo.getParentVO();
	}

	/**
	 * 创建者：仲瑞庆 功能： 参数： 返回： 例外： 日期：(2001-8-16 12:53:03) 修改日期，修改人，修改原因，注释标志：
	 * 
	 * @param gbvo
	 *            nc.vo.ic.pub.bill.GeneralBillVO
	 * @param sbvo
	 *            nc.vo.ic.pub.bill.SpecialBillVO
	 * @param iInOutFlag
	 *            int
	 */
	protected GeneralBillItemVO changeFromSpecialVOtoGeneralVOAboutItem(
			GeneralBillVO gbvo, SpecialBillVO sbvo, int iInOutFlag, int row) {
		String[] sBodyItemKeyName = gbvo.getChildrenVO()[0].getAttributeNames();
		String sName = null;
		for (int item = 0; item < sBodyItemKeyName.length; item++) {
			if (sBodyItemKeyName[item] == null)
				continue;
			sName = sBodyItemKeyName[item].trim();
			if (sbvo.getItemValue(row, sName) != null) {
				gbvo.setItemValue(row, sName, sbvo.getItemValue(row, sName));
			}
			if (sName.equals("nshouldinnum")) {
				if (iInOutFlag == InOutFlag.OUT) {
				} else {
					gbvo.setItemValue(row, sName, sbvo.getItemValue(row,
							"nadjustnum"));
				}
			} else if (sName.equals("nneedinassistnum")) {
				if (iInOutFlag == InOutFlag.OUT) {
				} else {
					gbvo.setItemValue(row, sName, sbvo.getItemValue(row,
							"nadjustastnum"));
				}
			} else if (sName.equals("ninnum")) {
				if (iInOutFlag == InOutFlag.OUT) {
				} else {
					gbvo.setItemValue(row, sName, sbvo.getItemValue(row,
							"nadjustnum"));
				}
			} else if (sName.equals("ninassistnum")) {
				if (iInOutFlag == InOutFlag.OUT) {
				} else {
					gbvo.setItemValue(row, sName, sbvo.getItemValue(row,
							"nadjustastnum"));
				}
			} else if (sName.equals("ningrossnum")) {
				if (iInOutFlag == InOutFlag.OUT) {
				} else {
					gbvo.setItemValue(row, sName, sbvo.getItemValue(row,
							"nadjustgrsnum"));
				}
			} else if (sName.equals("nshouldoutnum")) {
				if (iInOutFlag == InOutFlag.OUT) {
					gbvo.setItemValue(row, sName, sbvo.getItemValue(row,
							"nadjustnum"));
				} else {
				}
			} else if (sName.equals("nshouldoutassistnum")) {
				if (iInOutFlag == InOutFlag.OUT) {
					gbvo.setItemValue(row, sName, sbvo.getItemValue(row,
							"nadjustastnum"));
				} else {
				}
			} else if (sName.equals("noutnum")) {
				if (iInOutFlag == InOutFlag.OUT) {
					gbvo.setItemValue(row, sName, sbvo.getItemValue(row,
							"nadjustnum"));
				} else {
				}
			} else if (sName.equals("noutassistnum")) {
				if (iInOutFlag == InOutFlag.OUT) {
					gbvo.setItemValue(row, sName, sbvo.getItemValue(row,
							"nadjustastnum"));
				} else {
				}
			} else if (sName.equals("noutgrossnum")) {
				if (iInOutFlag == InOutFlag.OUT) {
					gbvo.setItemValue(row, sName, sbvo.getItemValue(row,
							"nadjustgrsnum"));
				} else {
				}
			} else if (sName.equals("nmny")) {
				gbvo.setItemValue(row, sName, sbvo.getItemValue(row, "je"));
			} else if (sName.equals("nplannedprice")) {
				gbvo.setItemValue(row, sName, sbvo.getItemValue(row, "jhdj"));
			} else if (sName.equals("nplannedmny")) {
				gbvo.setItemValue(row, sName, sbvo.getItemValue(row, "jhje"));
			} else if (sName.equals("csourcetype")) {
				gbvo.setItemValue(row, sName, m_sBillTypeCode);
			} else if (sName.equals("vsourcebillcode")) {
				gbvo.setItemValue(row, sName, sbvo.getHeaderValue("vbillcode"));
			} else if (sName.equals("csourcebillhid")) {
				gbvo.setItemValue(row, sName, sbvo
						.getHeaderValue("cspecialhid"));
			} else if (sName.equals("csourcebillbid")) {
				gbvo.setItemValue(row, sName, sbvo.getItemValue(row,
						"cspecialbid"));
			} else if (sName.equals("cgeneralbid")) { // 清表体行中PK
				gbvo.setItemValue(row, sName, "");
			} else if (sName.equals("dbizdate")) { // 表体出入库日期
				gbvo.setItemValue(row, sName, m_sLogDate);
			} else if (sName.equals("hsl")) {
				gbvo.setItemValue(row, sName, sbvo.getItemValue(row, "hsl"));
			} else if (sName.equals("cvendorid")) {
				gbvo.setItemValue(row, sName, sbvo.getItemValue(row,
						"cvendorid"));
			}
		}
		// nshouldinnum ninnum
		// nneedinassistnum ninassistnum
		// nshouldoutnum noutnum
		// nshouldoutassistnum noutassistnum
		// dshldtransnum nshldtransastnum

		// 有关货位VO
		SpecialBillItemVO voTemp = null;
		if ((sbvo.getItemValue(row, "cspaceid") != null)
				&& (sbvo.getItemValue(row, "cspaceid").toString().trim()
						.length() != 0)) {
			LocatorVO[] lvos = new LocatorVO[1];
			lvos[0] = new LocatorVO();
			lvos[0].setCspaceid((String) sbvo.getItemValue(row, "cspaceid"));
			// lvos[0].setVspacecode(sbvo.getItemValue(row,
			// "cspaceid").toString().trim());
			if (sbvo.getItemValue(row, "cspacename") != null)
				lvos[0].setVspacename((String) sbvo.getItemValue(row,
						"cspacename"));
			voTemp = ((SpecialBillItemVO) sbvo.getChildrenVO()[row]);
			if (iInOutFlag == InOutFlag.OUT) {
				lvos[0].setNoutspacenum(voTemp.getNadjustnum());
				lvos[0].setNoutspaceassistnum(voTemp.getNadjustastnum());
				lvos[0].setNoutgrossnum(voTemp.getNadjustgrsnum());
			} else {
				lvos[0].setNinspacenum(voTemp.getNadjustnum());
				lvos[0].setNinspaceassistnum(voTemp.getNadjustastnum());
				lvos[0].setNingrossnum(voTemp.getNadjustgrsnum());
			}
			gbvo.setItemValue(row, "locator", lvos);
		}

		gbvo.getChildrenVO()[row].setStatus(nc.vo.pub.VOStatus.NEW);

		return (GeneralBillItemVO) gbvo.getItemVOs()[row];
	}

	/**
	 * 程起伍 功能： 参数： 返回： 例外： 日期：(2004-12-3 17:21:30) 修改日期，修改人，修改原因，注释标志：
	 * 
	 * @return boolean
	 * @param voaCond
	 *            nc.vo.pub.query.ConditionVO[]
	 */

	/**
	 * 此处插入方法说明。 功能描述: //首先判断是否有导入数据的操作 //如果有继续 //否则，不运行判断操作
	 * 
	 * //检查表体数据VO的正确性 不存在的存货，会自动清空，所以不用校验 // 辅助计量单位是否存在 获得存货PK+辅助计量PK
	 * hashtable:getAssitUnitHashtable(ArrayList alInvID);
	 * 
	 * 判断辅助计量单位是否存在 // 批次号是否存在 获得存货PK+批次号HashTable
	 * 
	 * 判断批次号是否存在 // 货位是否与对应仓库一致 （1） 获得仓库PK （2） 获得本公司、本仓库下所有的货位，用公式从货位基本档案表中
	 * getCargdocHashtable(String sPk_stordoc);
	 * 
	 * （3） 判断货位是否存在hashtable中
	 * 
	 * 输入参数: 返回值: 异常处理: 日期:
	 * 
	 * @return java.lang.String
	 */
	private String checkImportBodyVO() {
		String sError = "";

		// 存货PK+辅助计量PK hashtable
		Hashtable htbInvAssitUnit = null;
		CircularlyAccessibleValueObject[] aryitemVOs = m_voBill.getChildrenVO();
		String sCinventoryid = null;
		ArrayList alCinventoryid = new ArrayList();
		for (int i = 0; i < aryitemVOs.length; i++) {
			Object objTempIvID = aryitemVOs[i]
					.getAttributeValue("cinventoryid");
			if (objTempIvID != null) {
				sCinventoryid = (String) objTempIvID;
				alCinventoryid.add(sCinventoryid);
			}
		}
		if (alCinventoryid != null && alCinventoryid.size() > 0)
			htbInvAssitUnit = nc.ui.ic.pub.BDFormulaContainers
					.getAssitUnitHashtableFromDB(alCinventoryid);

		String cinventorycode = null;
		String castunitid = null;
		String castunitname = null;
		String cspacecode = null;
		String cspaceid = null;
		String sBatchcode = null;
		Object oCinventoryid = null;
		Object oCinventorycode = null;
		Object oCastunitid = null;
		Object oCastunitname = null;
		Object oCspacecode = null;
		Object oCspaceid = null;
		Object oPk_measdoc = null;
		Object oBatchcode = null;

		StringBuffer sbError = new StringBuffer(" ");

		boolean isHasError = false;

		String sOneLineErro = null;

		for (int i = 0; i < aryitemVOs.length; i++) {

			oCinventoryid = aryitemVOs[i].getAttributeValue("cinventoryid");
			oCinventorycode = aryitemVOs[i].getAttributeValue("cinventorycode");
			oCastunitid = aryitemVOs[i].getAttributeValue("castunitid");
			oCastunitname = aryitemVOs[i].getAttributeValue("castunitname");
			oCspacecode = aryitemVOs[i].getAttributeValue("cspacecode");
			oCspaceid = aryitemVOs[i].getAttributeValue("cspaceid");
			oPk_measdoc = aryitemVOs[i].getAttributeValue("pk_measdoc");
			oBatchcode = aryitemVOs[i].getAttributeValue("vbatchcode");
			isHasError = false;
			sOneLineErro = "";

			if (oCinventorycode != null) {

				cinventorycode = (String) oCinventorycode;
				// 目前判断存货是否存在的下面代码无作用，之前的基类代码功能：自动清除没有存货ID的行
				// if (oCinventoryid == null)
				// sbError.append("存货：" + cinventorycode + "不存在; ");

				if (oCastunitname != null && oCastunitid == null) {
					castunitname = (String) oCastunitname;
					sOneLineErro = sOneLineErro
							+ nc.ui.ml.NCLangRes.getInstance().getStrByID(
									"4008spec", "UPP4008spec-000211")/*
																		 * @res
																		 * "，不存在辅单位："
																		 */
							+ castunitname;
					isHasError = true;
				} else if (oCastunitid != null && oCastunitname != null) {
					castunitname = (String) oCastunitname;
					if (htbInvAssitUnit == null
							|| (!htbInvAssitUnit
									.containsKey((String) oCinventoryid
											+ (String) oCastunitid) && !oCastunitid
									.equals(oPk_measdoc))) {

						sOneLineErro = sOneLineErro
								+ nc.ui.ml.NCLangRes.getInstance().getStrByID(
										"4008spec", "UPP4008spec-000211")/*
																			 * @res
																			 * "，不存在辅单位："
																			 */
								+ castunitname;
						isHasError = true;
					}
				}

				if (oCspacecode != null && oCspaceid == null) {
					cspacecode = (String) oCspacecode;
					sOneLineErro = sOneLineErro
							+ nc.ui.ml.NCLangRes.getInstance().getStrByID(
									"4008spec", "UPP4008spec-000212")/*
																		 * @res
																		 * "，不存在货位："
																		 */
							+ cspacecode;
					isHasError = true;
				}

				if (oBatchcode != null) {
					sBatchcode = (String) oBatchcode;
					if (sBatchcode.length() > 4
							&& nc.ui.ml.NCLangRes.getInstance().getStrByID(
									"4008spec", "UPP4008spec-000213")
							/* @res "不存在" */.equalsIgnoreCase(
									sBatchcode.substring(0, 3))) {
						sOneLineErro = sOneLineErro
								+ nc.ui.ml.NCLangRes.getInstance().getStrByID(
										"4008spec", "UPP4008spec-000214")/*
																			 * @res
																			 * "，不存在批次号："
																			 */
								+ sBatchcode.substring(4);
						isHasError = true;
					}
				}

				if (isHasError) {
					sbError.append(nc.ui.ml.NCLangRes.getInstance().getStrByID(
							"4008spec", "UPP4008spec-000052")/* @res "存货：" */
							+ cinventorycode
							+ ""
							+ sOneLineErro
							+ nc.ui.ml.NCLangRes.getInstance().getStrByID(
									"4008spec", "UPP4008spec-000215")/*
																		 * @res
																		 * "。"
																		 */);
				}
				sCinventoryid = (String) oCinventoryid;
			}
		}
		sError = (sbError.toString()).trim();

		return sError;
	}

	/**
	 * 创建者：仲瑞庆 功能：计算指定字段的编辑公式 参数： 返回： 例外： 日期：(2001-10-26 9:25:22)
	 * 修改日期，修改人，修改原因，注释标志：
	 * 
	 * @param row
	 *            int
	 * @param key
	 *            java.lang.String
	 */
	protected void execEditFormula(int row, String key) {
		getBillCardPanel().getBillModel().execEditFormulaByKey(row, key);
	}

	/**
	 * 如果是固定货位的存货，过滤出存货的固定货位 作者：余大英 创建日期：(2001-7-6 16:53:38)
	 */
	private void filterSpace(int row) {
		if (m_voBill == null)
			return;
		nc.vo.scm.ic.bill.WhVO voWh = m_voBill.getWhOut();
		if (voWh == null || voWh.getIsLocatorMgt() == null
				|| voWh.getIsLocatorMgt().intValue() == 0) {
			getBillCardPanel().getBillData().getBodyItem("cspacename")
					.setEnabled(false);
			return;
		}

		String whid = voWh.getCwarehouseid();

		String sInv = null;
		InvVO invvo = new InvVO();

		if (m_voBill.getItemInv(row) != null) {
			invvo = m_voBill.getItemInv(row);
			sInv = m_voBill.getItemInv(row).getCinventoryid();
		}

		String sName = (String) getBillCardPanel().getBodyValueAt(row,
				"cspacename");
		String spk = (String) getBillCardPanel()
				.getBodyValueAt(row, "cspaceid");

		

		getLocatorRefPane().setParam(voWh, invvo);
		
		getLocatorRefPane().setOldValue(sName, null, spk);

		return;

	}

	public void firstSetColEditable() {
		// 预先定位
		if (getBillCardPanel().getBillTable().getRowCount() > 0
				&& getBillCardPanel().getBillTable().getColumnCount() > 0) {
			getBillCardPanel().getBillTable().setColumnSelectionInterval(1, 1);
		}
	}
  
  
  protected QueryDlgHelpForSpecExt getQueryHelp() {
    if(this.m_queryHelp==null){
      this.m_queryHelp = new QueryDlgHelpForSpecExt(this);
    }
    return (QueryDlgHelpForSpecExt)this.m_queryHelp;
  }
  

	/**
	 * 返回 QueryConditionClient1 特性值。
	 * 
	 * @return nc.ui.pub.query.QueryConditionClient
	 * 
	 */
//	protected QueryConditionDlgForBill getConditionDlg() {
//		if (ivjQueryConditionDlg == null) {
//			ivjQueryConditionDlg = new QueryConditionDlgForBill(this);
//			// ivjQueryConditionDlg.setDefaultCloseOperation(ivjQueryConditionDlg.HIDE_ON_CLOSE);
//			ivjQueryConditionDlg.setTempletID(m_sCorpID, m_sPNodeCode,
//					m_sUserID, null);
//
//			ArrayList alCorpIDs = new ArrayList();
//			/*
//			 * try { //alCorpIDs= SpecialHBO_Client.queryCorpIDs(m_sUserID);
//			 * //alCorpIDs= //(ArrayList) SpecialHBO_Client.queryInfo( //new
//			 * Integer(QryInfoConst.USER_CORP), //m_sUserID); alCorpIDs =
//			 * (ArrayList)SpecialBillHelper.queryInfo( new
//			 * Integer(QryInfoConst.USER_CORP), m_sUserID); } catch (Exception
//			 * e) { nc.vo.scm.pub.SCMEnv.error(e); }
//			 */
//			// 修改人：刘家清 修改日期：2007-10-18下午06:40:42
//			// 修改原因：多公司时，数据权限对于不分配到公司的基础档案会有问题，比如存货分类
//			alCorpIDs.add(m_sCorpID);
//			// 设置单据日期为当前登录日期
//			// modified by liuzy 2008-04-02 v5.03需求：库存单据查询增加起止日期
//			ivjQueryConditionDlg.setInitDate("dbilldate.from", m_sLogDate);
//			ivjQueryConditionDlg.setInitDate("dbilldate.end", m_sLogDate);
//			// 以下为对参照的初始化
//			ivjQueryConditionDlg.initQueryDlgRef();
//			ivjQueryConditionDlg.initCorpRef("pk_corp", m_sCorpID, alCorpIDs);
//			//
//			// // 盘点单仓库的数据权限
//			// UIRefPane oRef=getRefPaneByCode("coutwarehouseid");
//			// if(oRef!=null){
//			// oRef.getRefModel().setUseDataPower(true);
//			// setValueRef("coutwarehouseid",oRef);
//			// }
//			//
//
//			// ivjQueryConditionDlg.setCorpRefs("head.pk_corp",
//			// new String[] { "coutwarehouseid"
//			// ,"inv.invcode","invcl.invclasscode"});
//
//			ivjQueryConditionDlg.setCorpRefs("head.pk_corp", GenMethod
//					.getDataPowerFieldFromDlgNotByProp(ivjQueryConditionDlg));
//
//			ivjQueryConditionDlg.setCombox("body.bqrybalrec", new String[][] {
//					{
//							"0",
//							nc.ui.ml.NCLangRes.getInstance().getStrByID(
//									"SCMCOMMON", "UPPSCMCommon-000244") /*
//																		 * @res
//																		 * "是"
//																		 */},
//					{
//							"1",
//							nc.ui.ml.NCLangRes.getInstance().getStrByID(
//									"SCMCOMMON", "UPPSCMCommon-000108") /*
//																		 * @res
//																		 * "否"
//																		 */} });
//
//			// Object[][] arycombox = new Object[2][2];
//			// arycombox[0][0] = "Y";
//			// arycombox[0][1] = "Y";
//			// arycombox[1][0] = "N";
//			// arycombox[1][1] = "N";
//
//			// ivjQueryConditionDlg.setCombox("",arycombox);
//
//		}
//		return ivjQueryConditionDlg;
//	}

	/**
	 * 创建者：王乃军 功能：构造外设输入控制类 参数： 返回： 例外： 日期：(2001-11-24 12:15:42)
	 * 修改日期，修改人，修改原因，注释标志：
	 */
	public nc.ui.ic.pub.device.DevInputCtrl getDevInputCtrl() {
		try {
			if (m_dictrl == null) {
				m_dictrl = new nc.ui.ic.pub.device.DevInputCtrl();
				m_dictrl.setPk_corp(m_sCorpID);
				m_dictrl.setBillTypeCode(m_sBillTypeCode);
				m_dictrl.setCard(getBillCardPanel());
				m_dictrl.setTp(this);
				m_dictrl.setup();
				m_dictrl.readFileFmt();
			}
		} catch (Exception e) {
			nc.vo.scm.pub.SCMEnv.error(e);
			// showErrorMessage(e.toString()); 为什么不起作用
			m_dictrl = null;
		}

		return m_dictrl;
	}

	/**
	 * 返回 LotNumbRefPane1 特性值。
	 * 
	 * @return nc.ui.ic.pub.lot.LotNumbRefPane
	 */
	/* 警告：此方法将重新生成。 */
	private LocatorRefPane getLocatorRefPane() {
		if (m_refLocator == null) {
			try {
				m_refLocator = new LocatorRefPane(InOutFlag.OUT);
				m_refLocator.setName("LotNumbRefPane");
				m_refLocator.setLocation(38, 1);
				// user code begin {1}
				m_refLocator.setInOutFlag(InOutFlag.OUT);
				// BillData bd = getBillCardPanel().getBillData();
				// 设置货位参照
				// if(bd.getBodyItem("vspacename")!=null)
				// bd.getBodyItem("vspacename").setComponent(m_refLocator);
				// getBillCardPanel().setBillData(bd);
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return m_refLocator;
	}

	/**
	 * 此处插入方法说明。 创建日期：(2001-12-18 16:59:11)
	 * 
	 * @return nc.vo.pub.AggregatedValueObject
	 */
	public nc.vo.pub.AggregatedValueObject getVo() throws java.lang.Exception {
		SpecialBillVO vo = getBillVO();
		if (m_sUserID == null) {
			try {

				m_sUserID = nc.ui.pub.ClientEnvironment.getInstance().getUser()
						.getPrimaryKey();
			} catch (Exception e) {
				nc.vo.scm.pub.SCMEnv.out("test user id is 2011000001");

			}
		}
		nc.vo.scm.pub.SCMEnv.out("+++ydy+getVo++userid:" + m_sUserID);
		vo.getHeaderVO().setCoperatoridnow(m_sUserID);
		vo.getHeaderVO().setCauditorid(m_sUserID);

		return vo;
	}

	/*
	 * protected void initSysParam() { //m_sSaveAndSign = "N"; try {
	 * //库存参数表IC028:出库时是否指定入库单;批次参照跟踪是否到单据号. //IC010 是否保存即签字。
	 * 
	 * //参数编码 含义 缺省值 //BD501 数量小数位 2 //BD502 辅计量数量小数位 2 //BD503 换算率 2 //BD504
	 * 存货成本单价小数位 2 String[] saParam = new String[] { "IC028", "IC010", "BD501",
	 * "BD502", "BD503", "BD504", "BD301" };
	 * 
	 * //传入的参数 ArrayList alAllParam = new ArrayList(); //查参数的必须数据 ArrayList
	 * alParam = new ArrayList(); alParam.add(m_sCorpID); //第一个是公司
	 * alParam.add(saParam); //待查的参数 alAllParam.add(alParam); //查用户对应公司的必须参数
	 * alAllParam.add(m_sUserID);
	 * 
	 * //返回的设置数据 ArrayList alRetData = (ArrayList) invokeClient("queryInfo", new
	 * Class[] { Integer.class, Object.class }, new Object[] { new
	 * Integer(QryInfoConst.INIT_PARAM), alAllParam });
	 * 
	 * //目前读两个。 if (alRetData == null || alRetData.size() < 2) { return; }
	 * //读回的参数值 String[] saParamValue = (String[]) alRetData.get(0);
	 * //追踪到单据参数,默认设置为"N" if (saParamValue != null && saParamValue.length >=
	 * alAllParam.size()) { //if(saParamValue[0]!=null) //m_sTrackedBillFlag =
	 * saParamValue[0].toUpperCase().trim(); //是否保存即签字。默认设置为"N"
	 * //if(saParamValue[1]!=null) //m_sSaveAndSign =
	 * saParamValue[1].toUpperCase().trim(); //BD501 数量小数位 2 if (saParamValue[2] !=
	 * null) m_iaScale[0] = Integer.parseInt(saParamValue[2]); //BD502 辅计量数量小数位
	 * 2 if (saParamValue[3] != null) m_iaScale[1] =
	 * Integer.parseInt(saParamValue[3]); //BD503 换算率 2 if (saParamValue[4] !=
	 * null) m_iaScale[2] = Integer.parseInt(saParamValue[4]); //BD504 存货成本单价小数位
	 * 2 if (saParamValue[5] != null) m_iaScale[3] =
	 * Integer.parseInt(saParamValue[5]); //BD301 本币小数位 if (saParamValue[6] !=
	 * null) m_iaScale[4] = Integer.parseInt(saParamValue[6]); } } catch
	 * (Exception e) { nc.vo.scm.pub.SCMEnv.out("can not get para" +
	 * e.getMessage()); if (e instanceof nc.vo.pub.BusinessException)
	 * showErrorMessage(e.getMessage()); } }
	 */
	/**
	 * 此处插入方法说明。 创建日期：(2003-9-4 10:55:57)
	 * 
	 * @return boolean
	 */
	private boolean isAdjusted() {
		if (m_iTotalListHeadNum > 0) {
			if (m_iMode == BillMode.List) {
				Object value = getBillListPanel().getHeadBillModel()
						.getValueAt(m_iLastSelListHeadRow, "vadjustername");
				if (value != null && value.toString().trim().length() > 0)
					return true;
			} else {
				BillItem bi = getBillCardPanel().getTailItem("vadjuster");
				if (bi != null && bi.getValue() != null
						&& bi.getValue().length() > 0)
					return true;
			}
		}
		return false;

	}

	/**
	 * 此处插入方法说明。 创建日期：(2003-9-4 10:55:43)
	 * 
	 * @return boolean
	 */
	private boolean isAudited() {
		if (m_iTotalListHeadNum > 0) {
			if (m_iMode == BillMode.List) {
				Object value = getBillListPanel().getHeadBillModel()
						.getValueAt(m_iLastSelListHeadRow, "fbillflag");
				if (value != null && (value.toString().equals(nc.vo.ic.pub.bill.BillStatus.APPROVED)
						||value.toString().equals(nc.vo.ic.pub.bill.BillStatus.ADJUST)))
					return true;
			} else {
				BillItem bi = getBillCardPanel().getHeadItem("fbillflag");
				if (bi != null && bi.getValue() != null
						&& (bi.getValue().equals(nc.vo.ic.pub.bill.BillStatus.APPROVED)
								||bi.getValue().equals(nc.vo.ic.pub.bill.BillStatus.ADJUST)))
					return true;
			}
		}
		return false;
	}

	/**
	 * 创建日期：(2003-2-18 9:59:58) 作者：王乃军 修改日期： 修改人： 修改原因： 方法说明：
	 * 
	 * @return boolean
	 */
	private boolean isByFormula() {
		return m_bIsByFormula;
	}

	/**
	 * 此处插入方法说明。 创建日期：(2003-9-4 11:58:58)
	 * 
	 * @return boolean
	 */
	private boolean isChecking() {
		SpecialBillVO vo = null;
		if (m_iMode == BillMode.List && m_iTotalListHeadNum > 0
				&& m_iLastSelListHeadRow >= 0) {
			vo = (SpecialBillVO) m_alListData.get(m_iLastSelListHeadRow);
		} else
			vo = m_voBill;

		if (vo != null && vo.getHeaderValue("fbillflag") != null) {
			String flag = vo.getHeaderValue("fbillflag").toString();
			// 帐面取数或实盘录入 修改 by hanwei 2003-12-22
			if (flag.equals(nc.vo.ic.pub.bill.BillStatus.CHECKING)
					|| flag.equals(nc.vo.ic.pub.bill.BillStatus.CHECKINPUT))
				return true;
		}
		return false;
	}

	/**
	 * 功能：查询单据审批状态 创建日期：(2002-12-25 16:34:26) 作者：王乃军 修改日期： 修改人： 修改原因： 算法说明：
	 */
	private void onAuditState() {

		// if(BillgetBillListPanel().getHeadSelectedCount()!=1) {
		if (m_voBill == null) {
			MessageDialog.showHintDlg(this,
					nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec",
							"UPPSCMCommon-000270")/* @res "提示" */,
					nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec",
							"UPP4008spec-000216")/* @res "请选择一张单据!" */);
			return;
		}

		showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
				"UCH050")/* @res "正在进行审批状态查询" */);

		String hid = m_voBill.getHeaderVO().getCspecialhid();

		// 如果该单据处于正在审批状态，执行下列语句：
		nc.ui.pub.workflownote.FlowStateDlg approvestatedlg = new nc.ui.pub.workflownote.FlowStateDlg(
				this, BillTypeConst.m_check, hid);
		approvestatedlg.showModal();

	}

	/**
	 * 此处插入方法说明。 创建日期：(2003-9-3 13:26:36)
	 */
	private void onCalculate() {
		if (showYesNoMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
				"4008spec", "UPP4008spec-000217")/* @res "差异计算是否包含业务期间量?" */) == nc.ui.pub.beans.UIDialog.ID_OK) {
			// 帐面取数
			// showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
			// "4008spec", "UPP4008spec-000218")/* @res "请稍候..." */);

			// 滤掉所有的空行
			filterNullLine();
			if (getBillCardPanel().getRowCount() == 0) {
				showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"4008spec", "UPP4008spec-000134")/*
															 * @res
															 * "无数据，请重新输入..."
															 */);
				return;
			}

			try {
				m_timer.start();
				ArrayList alInvNum = null;
				String WhID = ((nc.ui.pub.beans.UIRefPane) getBillCardPanel()
						.getHeadItem("coutwarehouseid").getComponent())
						.getRefPK();
				if ((WhID == null) || (WhID.trim().length() == 0)) {
					showHintMessage(nc.ui.ml.NCLangRes.getInstance()
							.getStrByID("4008spec", "UPP4008spec-000219")/*
																			 * @res
																			 * "未输入仓库！"
																			 */);
					return;
				}

				// 现存量
				ArrayList alParam = new ArrayList();
				alParam.add(m_sCorpID);
				alParam.add(null); // pk_calbody
				alParam.add(WhID);
				alParam.add(m_voBill.getPrimaryKey());
				// alParam.add(new Boolean(isHslConfig()));
				alInvNum = SpecialHHelper.queryAccNum(alParam);
				if ((null == alInvNum) || (alInvNum.size() == 0)) {
					showHintMessage(nc.ui.ml.NCLangRes.getInstance()
							.getStrByID("4008spec", "UPP4008spec-000220")/*
																			 * @res
																			 * "未查到任何数据！"
																			 */);
					return;
				}
				m_timer.showExecuteTime("qry acc num");
				// 自动执行修改。
				// onChange();

				// 置入账面数量
				m_voBill.setPeriodNum(alInvNum);
				m_voBill.calcAdjstDiffNum();
				setBillValueVO(m_voBill);

				// //计算对应数量的公式
				// int col1 = getBillCardPanel().getBodyColByKey("nchecknum");
				// col1 =
				// getBillCardPanel().getBillTable().convertColumnIndexToView(col1);
				// int col2 =
				// getBillCardPanel().getBodyColByKey("ncheckastnum");
				// col2 =
				// getBillCardPanel().getBillTable().convertColumnIndexToView(col2);
				// String sInvID = null;
				// for (int i = 0; i < getBillCardPanel().getRowCount(); i++) {
				// //行为修改状态。原来的行修改为被修改状态，但新增的行不能改。否则清空行时会被当作删除行。
				// //nc.vo.scm.pub.SCMEnv.out("" +
				// getBillCardPanel().getBillModel().getRowState(i));
				// sInvID = (String) getBillCardPanel().getBodyValueAt(i,
				// "cinventoryid");
				// if (sInvID != null && sInvID.trim().length() > 0)
				// getBillCardPanel().getBillModel().setRowState(i,
				// BillModel.MODIFICATION);
				// else
				// getBillCardPanel().getBillModel().setRowState(i,
				// BillModel.ADD);
				// getBillCardPanel().getBillModel().execEditFormula(i, col1);
				// getBillCardPanel().getBillModel().execEditFormula(i, col2);
				// calculateTzsl(i);
				// }
				// fullScreen(getBillCardPanel().getBillModel(),
				// m_iFirstAddRows);
				// 设置单据状态为“账面取数”
				// m_voBill.setHeaderValue("fbillflag",
				// nc.vo.ic.pub.bill.BillStatus.CALCULATE);
				showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"SCMCOMMON", "UPPSCMCommon-000133")/* @res "就绪" */);
				m_timer.stopAndShow("other deal acc num");
			} catch (Exception e) {
				handleException(e);
				nc.ui.pub.beans.MessageDialog.showErrorDlg(this,
						nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec",
								"UPPSCMCommon-000059")/* @res "错误" */, e
								.getMessage());
			}

		}
		return;
	}

	/**
	 * 修改
	 */
	public void onChange(boolean bAddRow) {

		showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
				"UCH027")/* @res "正在修改" */);
		// 保存标设置为非保存状态,保存状态为1，非保存状态为－1；
		iSaveFlag = -1;
		// 列表情况下点编辑，先切换到卡片
		if (m_iMode == BillMode.Browse || m_iMode == BillMode.New
				|| m_iMode == BillMode.Update) {
		} else {
			onList();
		}

		if (m_iMode == BillMode.Browse) {

			getBillCardPanel().getBillData().getHeadItem("coutwarehouseid")
					.setEdit(false);
			getBillCardPanel().getBillData().getHeadItem("dbilldate").setEdit(
					false);
			getBillCardPanel().getBillData().getHeadItem("vbillcode").setEdit(
					false);
		}
		if (m_iMode == BillMode.New || m_iMode == BillMode.Update) {
			getBillCardPanel().getBillData().getHeadItem("coutwarehouseid")
					.setEdit(true);
			getBillCardPanel().getBillData().getHeadItem("dbilldate").setEdit(
					true);
			if (m_bIsEditBillCode && m_bBillInit)
				getBillCardPanel().getBillData().getHeadItem("vbillcode")
						.setEdit(true);
			else
				getBillCardPanel().getBillData().getHeadItem("vbillcode")
						.setEdit(false);
		}

		// 状态变成编辑，第一次帐面取数时必须设置新增状态，不然不能保存
		if (m_bBillInit == true)
			m_iMode = BillMode.New;
		else
			m_iMode = BillMode.Update;
		setButtonState();
		setBillState();

		setUpdateBillInitData();

		// 帐面取数：bAddRow=false
		if (bAddRow) {
			onAddRow();
		}

		firstSetColEditable();
		// 修改单据编码前进行保存
		m_sOldBillCode = getBillCardPanel().getHeadItem("vbillcode").getValue();
		if (m_sOldBillCode != null)
			m_sOldBillCode = m_sOldBillCode.trim();

		// 默认不是导入数据 add by hanwei 2003-10-30
		m_bIsImportData = false;

		// 2005-01-28 自由项变色龙。
		InvAttrCellRenderer ficr = new InvAttrCellRenderer();
		ficr.setFreeItemRenderer(getBillCardPanel(), m_voBill);
	}

	/**
	 * 此处插入方法说明。 创建日期：(2003-8-25 13:53:58) 由应发（收）自动填写实发（收）
	 */
	private void onFillNum() {
		if (m_voBill == null)
			return;

		onChange();

		// 设置实盘录入状态
		setBillCheckInputStatus(nc.vo.ic.pub.bill.BillStatus.CHECKINPUT);

		// 辅数量
		if (getBillCardPanel().getBodyItem("naccountastnum") != null
				&& getBillCardPanel().getBodyItem("ncheckastnum") != null)
			SpecialBillUICtl.fillValue(getBillCardPanel(), this,
					"naccountastnum", "ncheckastnum");

		// 数量
		if (getBillCardPanel().getBodyItem("naccountnum") != null
				&& getBillCardPanel().getBodyItem("nchecknum") != null)
			SpecialBillUICtl.fillValue(getBillCardPanel(), this, "naccountnum",
					"nchecknum");

		// 数量
		if (getBillCardPanel().getBodyItem("naccountgrsnum") != null
				&& getBillCardPanel().getBodyItem("ncheckgrsnum") != null)
			SpecialBillUICtl.fillValue(getBillCardPanel(), this,
					"naccountgrsnum", "ncheckgrsnum");
		String mdbz = getBillCardPanel().getHeadItem("vuserdef1").getValue();
		if(mdbz!=null&&  mdbz.toString().endsWith("true")){
			if (getBillCardPanel().getBodyItem("vuserdef2") != null
					&& getBillCardPanel().getBodyItem("vuserdef5") != null)
				SpecialBillUICtl.fillValue(getBillCardPanel(), this,
						"vuserdef2", "vuserdef5");
			if (getBillCardPanel().getBodyItem("vuserdef3") != null
					&& getBillCardPanel().getBodyItem("vuserdef6") != null)
				SpecialBillUICtl.fillValue(getBillCardPanel(), this,
						"vuserdef3", "vuserdef6");
			if (getBillCardPanel().getBodyItem("vuserdef4") != null
					&& getBillCardPanel().getBodyItem("vuserdef7") != null)
				SpecialBillUICtl.fillValue(getBillCardPanel(), this,
						"vuserdef4", "vuserdef7");
			if (getBillCardPanel().getBodyItem("cspaceid") != null
					&& getBillCardPanel().getBodyItem("vuserdef8") != null)
				SpecialBillUICtl.fillValue(getBillCardPanel(), this,
						"cspaceid", "vuserdef8");
			if (getBillCardPanel().getBodyItem("cspacename") != null
					&& getBillCardPanel().getBodyItem("pspacename") != null)
				SpecialBillUICtl.fillValue(getBillCardPanel(), this,
						"cspacename", "pspacename");
			
		}
		return;

	}

	/**
	 * 
	 * 功能： 导入数据事件响应 参数： 返回： 例外： 日期：(2002-04-18 10:43:46) 修改日期，修改人，修改原因，注释标志：
	 */
	public void onImportData() {
		try {
			nc.ui.ic.pub.device.DevInputCtrl devInputCtr = getDevInputCtrl();

			if (devInputCtr != null) {
				// devInputCtr.setPk_corp(get)
				devInputCtr.setWarehouseidFieldName("coutwarehouseid");
				devInputCtr.setWarehouseNameFieldName("coutwarehousename");
				// 过滤空行
				filterNullLine();
				// 处理行号
				int iRowCount = getBillCardPanel().getRowCount();
				if (iRowCount > 0 && m_voBill.getChildrenVO() != null) {
					if (getBillCardPanel().getBodyItem(m_sBillRowNo) != null)
						for (int i = 0; i < iRowCount; i++) {
							m_voBill.setItemValue(i, m_sBillRowNo,
									getBillCardPanel().getBodyValueAt(i,
											m_sBillRowNo));
						}
				}

				SpecialBillVO vonew = (SpecialBillVO) (getBillCardPanel()
						.getBillValueVO(SpecialBillVO.class.getName(),
								SpecialBillHeaderVO.class.getName(),
								SpecialBillItemVO.class.getName()));
				devInputCtr.setBillVOUI(vonew);

				devInputCtr.setBillVO(m_voBill);

				java.util.ArrayList alResult = devInputCtr
						.onOpenFile(DevInputCtrl.ACT_ADD_ITEM);
				if (alResult == null || alResult.size() == 0) {
					showHintMessage(nc.ui.ml.NCLangRes.getInstance()
							.getStrByID("4008spec", "UPP4008spec-000221")/*
																			 * @res
																			 * "没有导入成功！"
																			 */);
					return;
				}
				String sAppendType = (String) alResult.get(0);
				int iAppendType = Integer.parseInt(sAppendType);
				nc.vo.pub.CircularlyAccessibleValueObject[] voaDi = (nc.vo.pub.CircularlyAccessibleValueObject[]) alResult
						.get(1);

				// 同步vo.
				m_bIsImportData = false;
				if (iAppendType == DevInputCtrl.ACT_ADD_ITEM) {
					synVO(voaDi);
					if (voaDi != null && voaDi.length > 0)
						m_bIsImportData = true;
					// 用于是否在保存时校验导入数据的正确性
					else
						m_bIsImportData = false;

				}
			} else {
				// showWarningMessage("请配置模版有关参数设置！");
				showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"4008spec", "UPP4008spec-000222")/*
															 * @res
															 * "请配置模版有关参数设置！"
															 */);
			}
		} catch (Exception e) {
			String sErrorMsg = null;
			sErrorMsg = e.getMessage();
			showWarningMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"4008spec", "UPP4008spec-000223")/* @res "错误提示：" */
					+ sErrorMsg);
			// showHintMessage("错误提示：" + sErrorMsg);
		}
	}

	/**
	 * 帐面取数：单据自动进入修改状态 单据浏览状态下整单取数，单据修改情况下鼠标选择取数
	 */
	public void onQryAccNumber() {
		if (m_iMode == BillMode.Browse) {
			QryAccNumberBrowse();
		} else if (m_iMode == BillMode.New || m_iMode == BillMode.Update) {
			QryAccNumberNew();
		}

		// 调用编辑
		if (m_iMode == BillMode.Browse)
			onChange(false);
	}
	/**
	 * 创建人：刘家清 创建时间：2008-7-10 下午08:26:24 创建原因： 只能对条码盘点单进行直接条码帐面取数，从条码结存表取数，并且直接在服务器端保存结存条码信息，最后只在前台刷新条码盘点单的条码结存数量。
	 *
	 */
	private void onQryBCAccNumber() {
		// 帐面取数
		showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec",
				"UPP4008spec-000574")/* @res "正在执行直接条码帐面取数，请稍候..." */);

		// 填入界面中数据
		getBillCardPanel().tableStopCellEditing();

		// 滤掉所有的空行
		filterNullLine();
		if (getBillCardPanel().getRowCount() == 0) {
			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"4008spec", "UPP4008spec-000184")/* @res "无数据，请重新输入！" */);
			return;
		}

		try {
			m_timer.start();
			ArrayList alReturn = null;
			HashMap<KeyObject, UFDouble> returnBillBarcodeNums = null;
			ArrayList alInvNum = null;
			String WhID = ((nc.ui.pub.beans.UIRefPane) getBillCardPanel()
					.getHeadItem("coutwarehouseid").getComponent()).getRefPK();
			if ((WhID == null) || (WhID.trim().length() == 0)) {
				showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"4008spec", "UPP4008spec-000219")/* @res "未输入仓库！" */);
				return;
			}
			
			if (null != m_voBill && m_voBill.getHeaderVO().getBccountflag().booleanValue()){

				
				
				
				SpecialBillVO voNowBill = (SpecialBillVO)m_voBill.clone();
				
				voNowBill.setHeaderValue("coperatoridnow", m_sUserID);
				ArrayList alsPrimaryKey = (ArrayList) nc.ui.pub.pf.PfUtilClient
				.processAction("Direct", m_sBillTypeCode,
						m_sLogDate, voNowBill,
						((SpecialBillVO) m_alListData
								.get(m_iLastSelListHeadRow)).clone());

				if (alsPrimaryKey == null || alsPrimaryKey.size() < 2) {
					nc.vo.scm.pub.SCMEnv.out("return data error.");
					return ;
				} // 显示提示信息
				
				if (alsPrimaryKey.get(0) != null)
					showErrorMessage((String) alsPrimaryKey.get(0));

				SMSpecialBillVO voSM = (SMSpecialBillVO) alsPrimaryKey
						.get(alsPrimaryKey.size() - 1);

				// //同步最大化VO
				//m_voBill.setIDItems(voNowBill);

				//m_alListData.set(m_iLastSelListHeadRow, m_voBill.clone());

				if (null != m_voBill
						&& null != m_voBill.getItemVOs() && 0 < m_voBill.getItemVOs().length){
					for(SpecialBillItemVO itemVO: m_voBill.getItemVOs()){
						itemVO.setIsloadaccountbc(UFBoolean.FALSE);
						
					}
				}
					


				// 将后台信息更新到界面
				freshVOBySmallVO(voSM);
				
				for (int i = 0; i < getBillCardPanel().getRowCount(); i++) {


					getBillCardPanel().setBodyValueAt(UFBoolean.FALSE, i, "isloadaccountbc");
		
					execEditFormula(i, "nprice");
					getBillCardPanel().getBillModel().execEditFormulaByKey(i,
							"nchecknum");
					getBillCardPanel().getBillModel().execEditFormulaByKey(i,
							"ncheckastnum");
					getBillCardPanel().getBillModel().execEditFormulaByKey(i,
							"ncheckgrsnum");
					calculateTzsl(i);
					


				}

				// 2005-01-28 自由项变色龙。
				InvAttrCellRenderer ficr = new InvAttrCellRenderer();
				ficr.setFreeItemRenderer(getBillCardPanel(), null);


				// 重显HVO
				m_iMode = BillMode.Browse;
				m_iFirstSelListHeadRow = -1;
				iSaveFlag = 1;
				setButtonState();
				setBillState();
				getBillCardPanel().updateValue();
				
				showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec",
				"UPP4008spec-000575")/* @res "直接条码帐面取数成功" */);

			}
			else
				showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec",
				"UPP4008spec-000566")/* @res "只能对条码盘点单进行直接条码帐面取数" */);
		} catch (Exception e) {
			handleException(e);
			nc.ui.pub.beans.MessageDialog.showErrorDlg(this,
					nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec",
							"UPPSCMCommon-000059")/* @res "错误" */, e
							.getMessage());
		}

	}


	/**
	 * 创建者：仲瑞庆 原帐面取数，只在浏览状态下帐面取数
	 */
	private void QryAccNumberBrowse() {
		// 帐面取数
		showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec",
				"UPP4008spec-000224")/* @res "正在执行账面取数，请稍候..." */);

		// 填入界面中数据
		getBillCardPanel().tableStopCellEditing();

		// 滤掉所有的空行
		filterNullLine();
		if (getBillCardPanel().getRowCount() == 0) {
			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"4008spec", "UPP4008spec-000184")/* @res "无数据，请重新输入！" */);
			return;
		}

		try {
			m_timer.start();
			ArrayList alReturn = null;
			ArrayList returnBillBarcode = null;
			ArrayList alInvNum = null;
			String WhID = ((nc.ui.pub.beans.UIRefPane) getBillCardPanel()
					.getHeadItem("coutwarehouseid").getComponent()).getRefPK();
			if ((WhID == null) || (WhID.trim().length() == 0)) {
				showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"4008spec", "UPP4008spec-000219")/* @res "未输入仓库！" */);
				return;
			}

			// 现存量
			ArrayList alParam = new ArrayList();
			alParam.add(m_sCorpID);
			alParam.add(null); // pk_calbody
			alParam.add(WhID);
			alParam.add(m_voBill.getPrimaryKey());
			// alInvNum = SpecialHHelper.queryAccNumPrice(alParam);
			alReturn = SpecialHHelper.queryAccNumPrice(alParam,m_iscountflag);
			alInvNum = (ArrayList) alReturn.get(0);
			returnBillBarcode = (ArrayList) alReturn
					.get(1);
			
			HashMap<String,ArrayList<KeyObject>> returnHSKeySet = null;
			HashMap<String,ArrayList<SpecailBarCodeVO[]>> returnHSSPBarCodes = null;
			if (null != returnBillBarcode){
				if (null != returnBillBarcode.get(0))
					returnHSKeySet = (HashMap<String,ArrayList<KeyObject>>) returnBillBarcode.get(0);
				if (null != returnBillBarcode.get(1))
					returnHSSPBarCodes = (HashMap<String,ArrayList<SpecailBarCodeVO[]>>) returnBillBarcode.get(1);
			}
			
			for (SpecialBillItemVO voitemSelecte: m_voBill.getItemVOs()) {
				voitemSelecte.setNaccountastnum(null);
				voitemSelecte.setNaccountgrsnum(null);
				voitemSelecte.setNaccountnum(null);
				voitemSelecte.setSpecailBarCodeVOs(null);
				voitemSelecte.setNbarcodenum(null);
			}
			
			if ((null == alInvNum) || (alInvNum.size() == 0)) {
				showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"4008spec", "UPP4008spec-000220")/* @res "未查到任何数据！" */);
				getBillCardPanel().getBillModel().setNeedCalculate(false);
				for (int i = 0; i < getBillCardPanel().getRowCount(); i++) {
					// 行为修改状态。原来的行修改为被修改状态，但新增的行不能改。否则清空行时会被当作删除行。
					// nc.vo.scm.pub.SCMEnv.out("" +
					// getBillCardPanel().getBillModel().getRowState(i));
					// sInvID = (String) getBillCardPanel().getBodyValueAt(i,
					// "cinventoryid");
					// if (sInvID != null && sInvID.trim().length() > 0)
					getBillCardPanel().getBillModel().setRowState(i,
							BillModel.MODIFICATION);
					// else
					// getBillCardPanel().getBillModel().setRowState(i,
					// BillModel.ADD);
					getBillCardPanel().setBodyValueAt(
							m_voBill.getItemVOs()[i].getNaccountnum(), i,
							"naccountnum");
					getBillCardPanel().setBodyValueAt(
							m_voBill.getItemVOs()[i].getNbarcodenum(), i,
							"nbarcodenum");
					getBillCardPanel().setBodyValueAt(
							m_voBill.getItemVOs()[i].getNaccountastnum(), i,
							"naccountastnum");
					getBillCardPanel().setBodyValueAt(
							m_voBill.getItemVOs()[i].getNaccountgrsnum(), i,
							"naccountgrsnum");
					getBillCardPanel().setBodyValueAt(
							m_voBill.getItemVOs()[i].getNprice(), i, "nprice");
					execEditFormula(i, "nprice");
					getBillCardPanel().getBillModel().execEditFormulaByKey(i,
							"nchecknum");
					getBillCardPanel().getBillModel().execEditFormulaByKey(i,
							"ncheckastnum");
					getBillCardPanel().getBillModel().execEditFormulaByKey(i,
							"ncheckgrsnum");
					calculateTzsl(i);
					
					if (getBillCardPanel().getBodyValueAt(i,IItemKey.cspecialbid) != null)
						getBillCardPanel().getBillModel().setRowState(i, BillModel.MODIFICATION);

				}

				getBillCardPanel().getBillModel().setNeedCalculate(true);
				getBillCardPanel().getBillModel().reCalcurateAll();
				setBillCheckInputStatus(nc.vo.ic.pub.bill.BillStatus.CHECKING);
				return;
			}
			m_timer.showExecuteTime("qry acc num");
			
			

			
			// 置入账面数量
			setOnHandNum(m_voBill, alInvNum);
			SpecailBarCodeVO[] specailBarCodeVOs = null;
			UFDouble ufNum = new UFDouble(0);
			for (SpecialBillItemVO itemVO : m_voBill.getItemVOs()) {
				if (!itemVO.getBarcodeManagerflag().booleanValue())
					continue;
				itemVO.setSpecailBarCodeVOs(null);
				itemVO.setNbarcodenum(null);
			}
			KeyObject keyTempItemVO = null;
			if (null != returnHSKeySet && 0 < returnHSKeySet.size()
					&& null != returnHSSPBarCodes && 0 < returnHSSPBarCodes.size())
				for (SpecialBillItemVO itemVO : m_voBill.getItemVOs()) {
					
					if (!itemVO.getBarcodeManagerflag().booleanValue())
						continue;
					
					keyTempItemVO = StringKeyJudge.getUKey(itemVO);

					ArrayList<KeyObject> listBarcodeKey  = returnHSKeySet.get(itemVO.getCinventoryid());
					ArrayList<SpecailBarCodeVO[]> listBarCodeArr = returnHSSPBarCodes.get(itemVO.getCinventoryid());
					if (null !=  listBarcodeKey && 0 < listBarcodeKey.size()
							&& null != listBarCodeArr && 0 < listBarCodeArr.size() ){
						for (int i = 0 ;i< listBarcodeKey.size();i++ ) {
	
						
							if (StringKeyJudge.compareTwoKey(keyTempItemVO,listBarcodeKey.get(i))) {
								ufNum = new UFDouble(0);
								specailBarCodeVOs = listBarCodeArr.get(i);
								if (null != specailBarCodeVOs) {
									for (SpecailBarCodeVO specailBarCodeVO : specailBarCodeVOs) {
										ufNum = ufNum.add(specailBarCodeVO.getNnumber());
										specailBarCodeVO.setPk_corp(m_voBill.getPk_corp());
	
									}
									itemVO.setSpecailBarCodeVOs(specailBarCodeVOs);
									itemVO.setNbarcodenum(ufNum);
	
								}
							}
	
					}
				}
			}

			getBillCardPanel().getBillModel().setNeedCalculate(false);

			for (int i = 0; i < getBillCardPanel().getRowCount(); i++) {
				// 行为修改状态。原来的行修改为被修改状态，但新增的行不能改。否则清空行时会被当作删除行。
				// nc.vo.scm.pub.SCMEnv.out("" +
				// getBillCardPanel().getBillModel().getRowState(i));
				// sInvID = (String) getBillCardPanel().getBodyValueAt(i,
				// "cinventoryid");
				// if (sInvID != null && sInvID.trim().length() > 0)
				getBillCardPanel().getBillModel().setRowState(i,
						BillModel.MODIFICATION);
				// else
				// getBillCardPanel().getBillModel().setRowState(i,
				// BillModel.ADD);
				getBillCardPanel().setBodyValueAt(
						m_voBill.getItemVOs()[i].getNaccountnum(), i,
						"naccountnum");
				getBillCardPanel().setBodyValueAt(
						m_voBill.getItemVOs()[i].getNbarcodenum(), i,
						"nbarcodenum");
				getBillCardPanel().setBodyValueAt(
						m_voBill.getItemVOs()[i].getNaccountastnum(), i,
						"naccountastnum");
				getBillCardPanel().setBodyValueAt(
						m_voBill.getItemVOs()[i].getNaccountgrsnum(), i,
						"naccountgrsnum");
				getBillCardPanel().setBodyValueAt(
						m_voBill.getItemVOs()[i].getNprice(), i, "nprice");
				execEditFormula(i, "nprice");
				getBillCardPanel().getBillModel().execEditFormulaByKey(i,
						"nchecknum");
				getBillCardPanel().getBillModel().execEditFormulaByKey(i,
						"ncheckastnum");
				getBillCardPanel().getBillModel().execEditFormulaByKey(i,
						"ncheckgrsnum");
				calculateTzsl(i);
				
				if (getBillCardPanel().getBodyValueAt(i,IItemKey.cspecialbid) != null)
					getBillCardPanel().getBillModel().setRowState(i, BillModel.MODIFICATION);

			}

			getBillCardPanel().getBillModel().setNeedCalculate(true);
			getBillCardPanel().getBillModel().reCalcurateAll();

			// fullScreen(getBillCardPanel().getBillModel(), m_iFirstAddRows);
			// 设置单据状态为“账面取数”
			// m_voBill.setHeaderValue("fbillflag",nc.vo.ic.pub.bill.BillStatus.CHECKING);

			setBillCheckInputStatus(nc.vo.ic.pub.bill.BillStatus.CHECKING);
			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"SCMCOMMON", "UPPSCMCommon-000133")/* @res "就绪" */);
			m_timer.stopAndShow("other deal acc num");
		} catch (Exception e) {
			handleException(e);
			nc.ui.pub.beans.MessageDialog.showErrorDlg(this,
					nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec",
							"UPPSCMCommon-000059")/* @res "错误" */, e
							.getMessage());
		}

	}

	/**
	 * 将从后台只按存货查询出的存量数据按照关键字包含原则分配到每一行去。
	 * 后台传递回的参数alInvVO已经按照存货的关键字包含关系过滤，此方法目的是比较
	 * m_voBill中的各个行与关键字的关系，将帐面数量累计和帐面辅助数量以及结存单价置入。
	 * 
	 * 算法说明： 循环处理界面ItemVOs, 循环处理InvVo，只要InvVo的关键字包含外层的ItemVO的关键字，即将其现存量累加；
	 * 在外层置入累计的现存量。
	 * 
	 * @param m_voBill
	 * @param alInvVO
	 *            修改人：刘家清 修改日期：2007-9-20上午10:40:40
	 *            修改原因：根据2007-09-19，与杨波、刘辉讨论后结果，修改成如果一行存货对应该多个结存，就报错，必须输入最详细现存量维度。
	 */
	private void setOnHandNum(SpecialBillVO m_voBill, ArrayList alInvVO) {
		SpecialBillItemVO[] voaItem = (SpecialBillItemVO[]) m_voBill
				.getChildrenVO();
		KeyObject keyTempItemVO = null;
		KeyObject keyTempInvVO = null;

		int comparednum = 0;

		StringBuilder sErr = new StringBuilder();

		// 参数判断？
		if (alInvVO == null)
			return;
		if(m_voBill.getHeaderVO().getIcheckmode() !=null && m_voBill.getHeaderVO().getIcheckmode() ==CheckMode.md ){
			InvVO voInv = null;
			HashMap map = new HashMap();
			for(int j=0;j<alInvVO.size();j++){
				voInv = (InvVO) alInvVO.get(j);
				String key = voInv.getCspaceid()+voInv.getVdef1();		
				map.put(key, voInv);
				
				}
			for (int i = 0; i < voaItem.length; i++) {
				UFDouble ufNum = UFD_ZERO;
				UFDouble ufAstNum = UFD_ZERO;
				UFDouble ufGrsNum = UFD_ZERO;
				UFDouble ufPrice = UFD_ZERO;
				if (voaItem[i].getCinventoryid() == null)
					continue;
				String key = voaItem[i].getCspaceid()+voaItem[i].getVuserdef1();
				if(map.containsKey(key)){
					InvVO vo = (InvVO) map.get(key);
					voaItem[i].setNaccountnum(vo.getNonhandnum());
					voaItem[i].setNaccountastnum(vo.getNonhandassistnum());
					voaItem[i].setNprice(vo.getNplannedprice());
					voaItem[i].setNaccountgrsnum(vo.getNonhandgrossnum());
				}
			}
		}else{
			InvVO voInv = null;
			for (int i = 0; i < voaItem.length; i++) {

				UFDouble ufNum = UFD_ZERO;
				UFDouble ufAstNum = UFD_ZERO;
				UFDouble ufGrsNum = UFD_ZERO;
				UFDouble ufPrice = UFD_ZERO;

				comparednum = 0;

				if (voaItem[i].getCinventoryid() == null)
					continue;
				keyTempItemVO = StringKeyJudge.getUKey(voaItem[i]);

				int flag = 0;
				for (int j = 0; j < alInvVO.size(); j++) {
					voInv = (InvVO) alInvVO.get(j);
					if (voInv == null)
						continue;
					if (!voInv.getCinventoryid().equals(
							voaItem[i].getCinventoryid()))
						continue;

					keyTempInvVO = StringKeyJudge.getUKey(voInv);
					// 如果InvVo的关键字包含ItemVo的关键字，其数量应计入此ItemVO的现存数量
					if (StringKeyJudge.compareTwoKey(keyTempInvVO, keyTempItemVO)) {
						flag = 1;
						comparednum = comparednum + 1;
						ufNum = ufNum.add(voInv.getNonhandnum());

						if (voaItem[i].getIsAstUOMmgt() != null
								&& voaItem[i].getIsAstUOMmgt().intValue() == 1)
							ufAstNum = ufAstNum.add(voInv.getNonhandassistnum());

						ufGrsNum = ufGrsNum.add(voInv.getNonhandgrossnum());

						ufPrice = voInv.getNplannedprice();
					}
				}
				if (flag == 1 && comparednum == 1) {
					// put the num and ast num in m_vobill
					voaItem[i].setNaccountnum(ufNum);
					if (voaItem[i].getIsAstUOMmgt() != null
							&& voaItem[i].getIsAstUOMmgt().intValue() == 1)
						voaItem[i].setNaccountastnum(ufAstNum);
					else
						voaItem[i].setNaccountastnum(null);
					// 设置结存单价
					voaItem[i].setNprice(ufPrice);

					voaItem[i].setNaccountgrsnum(ufGrsNum);
				}
				if (comparednum > 1) {
					if (sErr.length() == 0)
						sErr.append(nc.ui.ml.NCLangRes.getInstance().getStrByID(
								"4008spec", "UPP4008spec-000551")/*
																	 * @res
																	 * "下列行结存不唯一，帐面取数失败，请检查："
																	 */
								+ " \n");
					sErr.append(nc.ui.ml.NCLangRes.getInstance().getStrByID(
							"4008check", "UPP4008check-000095")/* @res "行号：" */
							+ voaItem[i].getCrowno() + " \n");

				}
			}
		}

		

		if (sErr.length() > 0)
			showWarningMessage(sErr.toString());

	}

	/**
	 * 创建者：李俊 功能：帐面取数，根据鼠标选中行进行帐面取数
	 * 
	 */
	private void QryAccNumberNew() {
		showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec",
				"UPP4008spec-000224")/* @res "正在执行账面取数，请稍候..." */);
		// 1. 得到鼠标选中行，得不到提示
		int[] iaSel = null;
		if (BillMode.List == m_iMode) {
			showWarningMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"4008spec", "UPP4008spec-000225")/* @res "请在卡片模式下帐面取数！" */);
			return;
		} else {
			iaSel = getBillCardPanel().getBillTable().getSelectedRows();
			if (iaSel == null || iaSel.length == 0) {
				showWarningMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"4008spec", "UPP4008spec-000226")/* @res "请选中行再帐面取数！" */);
				return;
			}
		}

		// 修改人：刘家清 修改日期：2007-9-20上午10:12:41 修改原因：把界面上的修改更新到m_voBill里，以便帐面取数时的准确性
		SpecialBillVO voNowBill = getBillVO();
		m_voBill.setIDItems(voNowBill);
		// 2. 得到选中行的PK门，组成PKHashMap,如果有相同行，提示不能有相同行
		SpecialBillItemVO[] voaItem = (SpecialBillItemVO[]) m_voBill
				.getChildrenVO();
		Vector vInv = new Vector();
		for (int i = 0; i < iaSel.length; i++) {
			vInv.add(m_voBill.getItemInv(i));
		}
		InvVO[] invvos = new InvVO[vInv.size()];
		vInv.copyInto(invvos);
		getInvoInfoBYFormula().getInVoOfHSLByHashCach(invvos);

		SpecialBillItemVO voitemSelecte = null;
		HashMap hmUK_Inv = new HashMap();
		for (int i = 0; i < iaSel.length; i++) {
			voitemSelecte = voaItem[iaSel[i]];
			if (voitemSelecte.getCinventoryid() == null)
				continue;
			KeyObject sKeyUI = StringKeyJudge.getUKey(voitemSelecte);// v5
																		// 需要根据存货是否换算率修改
			
			/**add by ouyangzhb 2012-04-25 码单盘点-对存货的判断*/
			if(m_voBill.getHeaderVO().getIcheckmode() !=null && m_voBill.getHeaderVO().getIcheckmode() ==CheckMode.md ){
				hmUK_Inv.put(sKeyUI, voitemSelecte.getCinventoryid());
			}else{
				if (!StringKeyJudge.containString(hmUK_Inv, sKeyUI)) {
					hmUK_Inv.put(sKeyUI, voitemSelecte.getCinventoryid());
				} else {
					showWarningMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
							"4008spec", "UPP4008spec-000227")/*
																 * @res
																 * "有重复存货行，请将其合并成一行！"
																 */);
					return;
				}
			}
//			if (!StringKeyJudge.containString(hmUK_Inv, sKeyUI)) {
//				hmUK_Inv.put(sKeyUI, voitemSelecte.getCinventoryid());
//			} else {
//				showWarningMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
//						"4008spec", "UPP4008spec-000227")/*
//															 * @res
//															 * "有重复存货行，请将其合并成一行！"
//															 */);
//				return;
//			}
		}
		if (hmUK_Inv == null || hmUK_Inv.size() <= 0) {
			showWarningMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"4008spec", "UPP4008spec-000228")/* @res "选中行无存货，不能帐面取数！" */);
			return;
		}

		// 3. 传到后台查询
		ArrayList alReturn = null;
		ArrayList returnBillBarcode = null;
		ArrayList alInvNum = null;
		HashMap<String,ArrayList<KeyObject>> returnHSKeySet = null;
		HashMap<String,ArrayList<SpecailBarCodeVO[]>> returnHSSPBarCodes = null;
		String WhID = ((nc.ui.pub.beans.UIRefPane) getBillCardPanel()
				.getHeadItem("coutwarehouseid").getComponent()).getRefPK();
		try {
			//add by ouyangzhb 2012-04-24 增加码单账面取数方法 begin 
			UFBoolean ismd = UFBoolean.FALSE;
			String mdbz = m_voBill.getHeaderVO().getVuserdef1();
			if(mdbz !=null&&!"".endsWith(mdbz))
				ismd = new UFBoolean(mdbz);
			if(ismd.booleanValue() ){
				alReturn = (ArrayList) GenMethod.callICEJBService(
						"nc.bs.ic.ic261.SpecialHDMO", "queryOnHandNumPrice_MD",
						new Class[] { String.class, String.class, String.class,
								HashMap.class ,UFBoolean.class}, new Object[] { m_sCorpID, null,
								WhID, hmUK_Inv ,m_iscountflag});
			}
			else{
				alReturn = (ArrayList) GenMethod.callICEJBService(
						"nc.bs.ic.ic261.SpecialHDMO", "queryOnHandNumPrice",
						new Class[] { String.class, String.class, String.class,
								HashMap.class ,UFBoolean.class}, new Object[] { m_sCorpID, null,
								WhID, hmUK_Inv ,m_iscountflag});
			}
			//add by ouyangzhb 2012-04-24 增加码单账面取数方法 end 
			
			//增加空判断 陈倪娜 2009-07-10
			if (alReturn != null) {
				alInvNum = (ArrayList) alReturn.get(0);
				returnBillBarcode = (ArrayList) alReturn.get(1);
			}
			
			if (null != returnBillBarcode){
				if (null != returnBillBarcode.get(0))
					returnHSKeySet = (HashMap<String,ArrayList<KeyObject>>) returnBillBarcode.get(0);
				if (null != returnBillBarcode.get(1))
					returnHSSPBarCodes = (HashMap<String,ArrayList<SpecailBarCodeVO[]>>) returnBillBarcode.get(1);
			}
			
		} catch (Exception e) {
			nc.vo.scm.pub.SCMEnv.error(e);
			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"4008spec", "UPP4008spec-000229")/* @res "帐面取数错误!" */);
			return;

		}
		
		for (int i = 0; i < iaSel.length; i++) {
			voitemSelecte = voaItem[iaSel[i]];
			voitemSelecte.setNaccountastnum(null);
			voitemSelecte.setNaccountgrsnum(null);
			voitemSelecte.setNaccountnum(null);
			voitemSelecte.setBarCodeVOs(null);
			voitemSelecte.setNbarcodenum(null);
		}
		// 4. 根据查询结果InvVOs设置m_voBill,设置界面
		if ((null == alInvNum) || (alInvNum.size() == 0)) {
			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"4008spec", "UPP4008spec-000220")/* @res "未查到任何数据！" */);
			getBillCardPanel().getBillModel().setNeedCalculate(false);
			for (int i = 0; i < getBillCardPanel().getRowCount(); i++) {
				getBillCardPanel()
						.setBodyValueAt(m_voBill.getItemVOs()[i].getNaccountnum(),
								i, "naccountnum");
				getBillCardPanel()
						.setBodyValueAt(m_voBill.getItemVOs()[i].getNbarcodenum(),
								i, "nbarcodenum");
				getBillCardPanel().setBodyValueAt(
						m_voBill.getItemVOs()[i].getNaccountastnum(), i,
						"naccountastnum");
				getBillCardPanel().setBodyValueAt(
						m_voBill.getItemVOs()[i].getNprice(), i, "nprice");
				execEditFormula(i, "nprice");
				getBillCardPanel().setBodyValueAt(
						m_voBill.getItemVOs()[i].getNaccountgrsnum(), i,
						"naccountgrsnum");

				getBillCardPanel().getBillModel().execEditFormulaByKey(i,
						"nchecknum");
				getBillCardPanel().getBillModel().execEditFormulaByKey(i,
						"ncheckastnum");
				getBillCardPanel().getBillModel().execEditFormulaByKey(i,
						"ncheckgrsnum");

				calculateTzsl(i);
				
				if (getBillCardPanel().getBodyValueAt(i,IItemKey.cspecialbid) != null)
					getBillCardPanel().getBillModel().setRowState(i, BillModel.MODIFICATION);

			}
			getBillCardPanel().getBillModel().setNeedCalculate(true);
			getBillCardPanel().getBillModel().reCalcurateAll();
			setBillCheckInputStatus(nc.vo.ic.pub.bill.BillStatus.CHECKING);
			return;
		}
		m_timer.showExecuteTime("qry acc num");


		
		// 置入账面数量到m_voBill
		setOnHandNum(m_voBill, alInvNum);

		SpecailBarCodeVO[] specailBarCodeVOs = null;
		UFDouble ufNum = new UFDouble(0);
		/*
		 * for (SpecialBillItemVO itemVO:m_voBill.getItemVOs()){
		 * itemVO.setBarCodeVOs(null); itemVO.setNbarcodenum(null); }
		 */

		for (int i = 0; i < iaSel.length; i++) {
			voitemSelecte = voaItem[iaSel[i]];
			if (!voitemSelecte.getBarcodeManagerflag().booleanValue())
				continue;
			voitemSelecte.setBarCodeVOs(null);
			voitemSelecte.setNbarcodenum(null);
		}

		KeyObject keyTempItemVO = null ;
		if (null != returnHSKeySet &&  0 < returnHSKeySet.size()
				&& null != returnHSSPBarCodes && 0 < returnHSSPBarCodes.size())
			for (int i = 0; i < iaSel.length; i++) {
				voitemSelecte = voaItem[iaSel[i]];
				if (!voitemSelecte.getBarcodeManagerflag().booleanValue())
					continue;
				
				keyTempItemVO = StringKeyJudge.getUKey(voitemSelecte);
				ArrayList<KeyObject> listBarcodeKey  = returnHSKeySet.get(voitemSelecte.getCinventoryid());
				ArrayList<SpecailBarCodeVO[]> listBarCodeArr = returnHSSPBarCodes.get(voitemSelecte.getCinventoryid());
				if (null != listBarcodeKey && 0 < listBarcodeKey.size())
					for (int j = 0 ;j< listBarcodeKey.size();j++) {

						if (StringKeyJudge.compareTwoKey(keyTempItemVO,listBarcodeKey.get(j))) {
							ufNum = new UFDouble(0);
							specailBarCodeVOs = listBarCodeArr.get(j);
							if (null != specailBarCodeVOs) {
								for (SpecailBarCodeVO specailBarCodeVO : specailBarCodeVOs) {
									ufNum = ufNum.add(specailBarCodeVO.getNnumber());
									specailBarCodeVO.setPk_corp(m_voBill.getPk_corp());
	
								}
								voitemSelecte.setSpecailBarCodeVOs(specailBarCodeVOs);
								voitemSelecte.setNbarcodenum(ufNum);
	
							}
						}
							
					}
				
			}
		

		/*
		 * if (null != returnBillBarcode) for (SpecialBillItemVO
		 * itemVO:m_voBill.getItemVOs()){ ufNum = new UFDouble(0);
		 * specailBarCodeVOs = returnBillBarcode.get(itemVO.getCinventoryid());
		 * if (null != specailBarCodeVOs){ for(SpecailBarCodeVO
		 * specailBarCodeVO:specailBarCodeVOs){ ufNum =
		 * ufNum.add(specailBarCodeVO.getNnumber());
		 * specailBarCodeVO.setPk_corp(m_voBill.getPk_corp());
		 *  } itemVO.setBarCodeVOs(specailBarCodeVOs);
		 * itemVO.setNbarcodenum(ufNum);
		 *  } }
		 */

		// 设置界面 ( 包含执行盘点公式)
		String sInvID = null;

		getBillCardPanel().getBillModel().setNeedCalculate(false);
		for (int i = 0; i < getBillCardPanel().getRowCount(); i++) {
			getBillCardPanel()
					.setBodyValueAt(m_voBill.getItemVOs()[i].getNaccountnum(),
							i, "naccountnum");
			getBillCardPanel()
					.setBodyValueAt(m_voBill.getItemVOs()[i].getNbarcodenum(),
							i, "nbarcodenum");
			getBillCardPanel().setBodyValueAt(
					m_voBill.getItemVOs()[i].getNaccountastnum(), i,
					"naccountastnum");
			getBillCardPanel().setBodyValueAt(
					m_voBill.getItemVOs()[i].getNprice(), i, "nprice");
			execEditFormula(i, "nprice");
			getBillCardPanel().setBodyValueAt(
					m_voBill.getItemVOs()[i].getNaccountgrsnum(), i,
					"naccountgrsnum");

			getBillCardPanel().getBillModel().execEditFormulaByKey(i,
					"nchecknum");
			getBillCardPanel().getBillModel().execEditFormulaByKey(i,
					"ncheckastnum");
			getBillCardPanel().getBillModel().execEditFormulaByKey(i,
					"ncheckgrsnum");

			calculateTzsl(i);
			
			if (getBillCardPanel().getBodyValueAt(i,IItemKey.cspecialbid) != null)
				getBillCardPanel().getBillModel().setRowState(i, BillModel.MODIFICATION);

		}
		getBillCardPanel().getBillModel().setNeedCalculate(true);
		getBillCardPanel().getBillModel().reCalcurateAll();

		// 设置单据状态为“账面取数”
		setBillCheckInputStatus(nc.vo.ic.pub.bill.BillStatus.CHECKING);
		showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
				"SCMCOMMON", "UPPSCMCommon-000133")/* @res "就绪" */);
		m_timer.stopAndShow("other deal acc num");

	}

	/**
	 * 功能：取出当前界面上的存货的当前最新结存单价 创建日期：(2002-12-25 16:34:26) 作者：王乃军 修改日期： 修改人： 修改原因：
	 * 算法说明：
	 */
	private void onQueryPrice() {
		try {
			// 取当前界面显示的数据
			SpecialBillVO voCurBill = getBillVO();
			if (voCurBill != null && voCurBill.getHeaderVO() != null
					&& voCurBill.getItemVOs() != null
					&& voCurBill.getItemVOs().length > 0) {
				SpecialBillHeaderVO voHead = voCurBill.getHeaderVO();
				SpecialBillItemVO[] voaItem = voCurBill.getItemVOs();
				// 准备参数数据。
				// String pk_corp = m_sCorpID; //公司
				String pk_calbody = voHead.getPk_calbody_out(); // 库存组织
				String cwarehouseid = voHead.getCoutwarehouseid(); // 仓库
				if (cwarehouseid != null && cwarehouseid.trim().length() > 0) {
					String[] cinventoryids = null; // 存货们
					Vector vInv = new Vector();
					for (int i = 0; i < voaItem.length; i++) {
						// 如果vector中没有此存货管理id的话，就加进去。
						if (voaItem[i] != null
								&& voaItem[i].getCinventoryid() != null
								&& !vInv.contains(voaItem[i].getCinventoryid()))
							vInv.addElement(voaItem[i].getCinventoryid());
					}
					// 有存货！
					if (vInv.size() > 0) {
						cinventoryids = new String[vInv.size()];
						vInv.copyInto(cinventoryids);
						ArrayList alParam = new ArrayList();
						alParam.add(m_sCorpID);
						alParam.add(pk_calbody);
						alParam.add(cwarehouseid);
						alParam.add(cinventoryids);
						// 执行查询
						ArrayList alRet = (ArrayList) SpecialBillHelper
								.queryInfo(new Integer(
										QryInfoConst.SETTLEMENT_PRICE), alParam);
						if (alRet != null && alRet.size() > 0) {
							// 设置到界面上
							setPrice(alRet);
							showHintMessage(nc.ui.ml.NCLangRes.getInstance()
									.getStrByID("4008spec",
											"UPP4008spec-000230")/*
																	 * @res
																	 * "读取结存单价完毕。"
																	 */);
						} else {
							showErrorMessage(nc.ui.ml.NCLangRes.getInstance()
									.getStrByID("4008spec",
											"UPP4008spec-000231")/*
																	 * @res
																	 * "没有查询到当前存货的结存单价！"
																	 */);
						}
					} else {
						showHintMessage(nc.ui.ml.NCLangRes.getInstance()
								.getStrByID("4008spec", "UPP4008spec-000232")/*
																				 * @res
																				 * "请先录入存货，然后再试。"
																				 */);
					}
				} else {
					showHintMessage(nc.ui.ml.NCLangRes.getInstance()
							.getStrByID("4008spec", "UPP4008spec-000233")/*
																			 * @res
																			 * "请先录入仓库，然后再试。"
																			 */);
				}
			} else
				showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"4008spec", "UPP4008spec-000234")/*
															 * @res
															 * "当前没有可用单据，请查询或新增单据。"
															 */);
		} catch (Exception e) {
			showErrorMessage(e.getMessage());
		}
	}

	/**
	 * 创建者：王乃军 功能：查询单据的表体，并把结果置到arraylist 参数： int iaIndex[],单据在alAlldata中的索引。
	 * String saBillPK[]单据pk数组 返回： 例外： 日期：(2001-5-9 9:23:32) 修改日期，修改人，修改原因，注释标志：
	 * 
	 * 
	 * 
	 * 
	 */
	protected void qryItems(int iaIndex[], String saBillPK[]) {
		if (iaIndex == null || saBillPK == null
				|| iaIndex.length != saBillPK.length) {
			nc.vo.scm.pub.SCMEnv.out("param value ERR.");
			return;
		}
		try {
			QryConditionVO voCond = new QryConditionVO();

			voCond.setIntParam(0, SpecialBillVO.QRY_ITEM_ONLY_PURE);
			voCond.setParam(0, saBillPK);
			if (m_iBqrybalrec == 2)
				voCond
						.setStrParam(1,
								"and isnull(body.naccountnum,0)<>isnull(body.nchecknum,0) ");
			// 启用进度条
			// getPrgBar(PB_QRY).start();
			long lTime = System.currentTimeMillis();
			ArrayList alRetData = (ArrayList) SpecialBillHelper.queryBills(
					m_sBillTypeCode, voCond);
			if (alRetData == null || alRetData.size() == 0
					|| iaIndex.length != alRetData.size()) {
				nc.vo.scm.pub.SCMEnv.out("ret item value ERR.");
				return;
			}
			// v5:exec formula where query body items.
			getFormulaBillContainer().formulaBill(alRetData,
					getFormulaItemHeader(), getFormulaItemBody());
			// --------------------------------------------
			SpecialBillVO voBill = null;
			for (int i = 0; i < alRetData.size(); i++) {
				// index
				voBill = (SpecialBillVO) m_alListData.get(iaIndex[i]);
				// set value
				if (alRetData.get(i) != null && voBill != null)
					voBill.setChildrenVO(((SpecialBillVO) alRetData.get(i))
							.getChildrenVO());

			}
		} catch (Exception e) {
			showErrorMessage(e.getMessage());
		}
	}

	/**
	 * 创建者：王乃军 功能：显示消耗的时间 参数： 返回： 例外： 日期：(2001-11-23 18:11:18)
	 * 修改日期，修改人，修改原因，注释标志：
	 */
	protected void reportTime(long lStartTime, String sTaskHint) {
		long lTime = System.currentTimeMillis() - lStartTime;
		String sMessage = "执行<" + sTaskHint + ">消耗的时间为：" + (lTime / 60000)
				+ "分" + ((lTime / 1000) % 60) + "秒" + (lTime % 1000) + "毫秒";
		showWarningMessage(sMessage);

	}

	/**
	 * 
	 * 功能： 导入文件后同步状态vo. 参数： 返回： 例外： 日期：(2002-04-18 10:43:46) 修改日期，修改人，修改原因，注释标志：
	 */
	public void resumeVO() {
		if (m_voBill != null) { // 同步vo.
			SpecialBillItemVO voaItem[] = m_voBill.getItemVOs();
			if (voaItem != null && voaItem.length > 0) {
				java.util.ArrayList alOldItem = new ArrayList();
				int iStatus = 0;
				for (int i = 0; i < voaItem.length; i++) {
					iStatus = voaItem[i].getStatus();
					if (iStatus != nc.vo.pub.VOStatus.NEW)
						alOldItem.add(voaItem[i]);
				}
				if (alOldItem != null && alOldItem.size() > 0) {

				}
			}
		}

	}

	/**
	 * 此处插入方法说明。 设置当前的盘点的的状态 (0)初始状态 (1)帐面取数 (2)实盘录入
	 * 
	 * 创建日期：(2003-12-22 19:01:42)
	 */
	public void setBillCheckInputStatus(String sStatues) {
		if (sStatues == null)
			return;
		m_voBill.setHeaderValue("fbillflag", sStatues);
	}

	/**
	 * 说明：设置单据是否可以排序 创建日期：(2001-4-18 19:45:39)
	 */
	public void setBillSortEnable() { // finished
	// if (m_iMode == BillMode.Browse) {
	// super.getBillCardPanel().getBodyPanel().addTableSortListener();
	// } else if (m_iMode == BillMode.List) {
	// //super.getBillListPanel().getBodyScrollPane(BillListData.DEFAULT_TABLECODE).removeTableSortListener();
	// super.getBillListPanel().getParentListPanel()
	// .removeTableSortListener();
	// super.getBillCardPanel().getBodyPanel().removeTableSortListener();
	// } else {
	// boolean bAddSortListener = false;
	// if (m_bBillInit) {//第一增加状态，有可能取的是上次的单据状态，所以直接绕过下面的判断
	// bAddSortListener = false;
	// } else {
	// if (m_alListData != null && m_alListData.size() > 0) {
	// SpecialBillVO sbvotemp = (SpecialBillVO) m_alListData
	// .get(m_iLastSelListHeadRow);
	// if (sbvotemp != null && sbvotemp.getParentVO() != null) {
	// Integer iFlag = ((SpecialBillHeaderVO) sbvotemp
	// .getParentVO()).getFbillFlag();
	// if (iFlag != null) {
	// String sFlag = iFlag.toString();
	// //审批中
	// if (sFlag
	// .equals(nc.vo.ic.pub.bill.BillStatus.APPROVEDING)) {
	// bAddSortListener = true;
	// } //帐面取数或实盘录入
	// else if (sFlag
	// .equals(nc.vo.ic.pub.bill.BillStatus.CHECKING)) {
	// bAddSortListener = true;
	// } else if (sFlag
	// .equals(nc.vo.ic.pub.bill.BillStatus.CHECKINPUT)) {
	// bAddSortListener = true;
	// } else if (sFlag
	// .equals(nc.vo.ic.pub.bill.BillStatus.AUDITED)) {
	// bAddSortListener = true;
	// }
	//
	// }
	//
	// }
	// }
	// }
	//
	// if (bAddSortListener) {
	// super.getBillCardPanel().getBodyPanel().addTableSortListener();
	// } else {
	// super.getBillCardPanel().getBodyPanel()
	// .removeTableSortListener();
	// }
	// }
	}

	/**
	 * 创建日期：(2003-2-18 9:59:58) 作者：王乃军 修改日期： 修改人： 修改原因： 方法说明：
	 * 
	 * @param newIsByFormula
	 *            boolean
	 */
	private void setIsByFormula(boolean newIsByFormula) {
		m_bIsByFormula = newIsByFormula;
	}

	/**
	 * 功能：置当前界面上的存货的当前最新结存单价 创建日期：(2002-12-25 16:34:26) 作者：王乃军 修改日期： 修改人： 修改原因：
	 * 算法说明：
	 */
	private void setPrice(ArrayList alData) {

		final String IK_INV = "cinventoryid"; // 表体存货的itemkey
		final String IK_PRICE = "nprice"; // 表体存货的itemkey

		if (alData != null && alData.size() > 0) {
			// 建立HASHTABLE:KEY=INVID,VALUE=PRICE，提高查询效率
			Hashtable htData = new Hashtable();
			ArrayList alTempData = null;
			getBillCardPanel().getBillModel().setNeedCalculate(false);

			for (int i = 0; i < alData.size(); i++) {
				if (alData.get(i) != null) {
					alTempData = (ArrayList) alData.get(i);
					if (alTempData.size() >= 2 && alTempData.get(0) != null
							&& alTempData.get(1) != null
							&& !htData.containsKey(alTempData.get(0)))
						// invid,price
						htData.put(alTempData.get(0), alTempData.get(1));

				}
			}
			int rowcount = getBillCardPanel().getRowCount();
			String cinventoryid = null;
			for (int i = 0; i < rowcount; i++) {
				cinventoryid = (String) getBillCardPanel().getBodyValueAt(i,
						IK_INV);
				if (cinventoryid != null && htData.containsKey(cinventoryid))
					getBillCardPanel().setBodyValueAt(htData.get(cinventoryid),
							i, IK_PRICE);
			}
			// 同步vo
			// 不需要
			getBillCardPanel().getBillModel().setNeedCalculate(true);
			getBillCardPanel().getBillModel().reCalcurateAll();
		} else {
			nc.vo.scm.pub.SCMEnv.out("no price to be set");
		}
	}

	/**
	 * 创建者：王乃军 功能：显示消耗的时间 参数： 返回： 例外： 日期：(2001-11-23 18:11:18)
	 * 修改日期，修改人，修改原因，注释标志：
	 */
	protected void showTime(long lStartTime, String sTaskHint) {
		long lTime = System.currentTimeMillis() - lStartTime;
		nc.vo.scm.pub.SCMEnv.out("执行<" + sTaskHint + ">消耗的时间为："
				+ (lTime / 60000) + "分" + ((lTime / 1000) % 60) + "秒"
				+ (lTime % 1000) + "毫秒");

	}

	/**
	 * 
	 * 功能： 导入文件后同步状态vo. 参数： 返回： 例外： 日期：(2002-04-18 10:43:46) 修改日期，修改人，修改原因，注释标志：
	 */
	public void synVO(nc.vo.ic.pub.device.DevInputVO[] voaDi) {
		if (m_voBill != null) {
			// 同步vo.
			SpecialBillItemVO voaItem[] = m_voBill.getItemVOs();
			int start = getDevInputCtrl().getStartItem();
			if (start >= 0 && voaDi.length > 0
					&& voaItem.length >= (start + voaDi.length)) {
				for (int line = 0; line < voaDi.length; line++)
					voaDi[line].copy(voaItem[start + line]);
			} else {
				nc.vo.scm.pub.SCMEnv.out("date error.");
			}
		}
	}

	/**
	 * 
	 * 功能： 导入文件后同步状态vo. 参数： 返回： 例外： 日期：(2002-04-18 10:43:46) 修改日期，修改人，修改原因，注释标志：
	 */
	public void synVO(nc.vo.pub.CircularlyAccessibleValueObject[] voaDi) {
		if (m_voBill != null && voaDi != null) {
			// 同步vo.
			SpecialBillItemVO voaItem[] = m_voBill.getItemVOs();
			int start = getDevInputCtrl().getStartItem();
			if (start >= 0 && voaDi.length > 0
					&& voaItem.length >= (start + voaDi.length)) {
				for (int line = 0; line < voaDi.length; line++) {
					SpecialBillItemVO spBillItemVO = (SpecialBillItemVO) voaDi[line];
					spBillItemVO.setStatus(nc.vo.pub.VOStatus.NEW);
					if (spBillItemVO != null) {
						voaItem[start + line] = spBillItemVO;
					}

				}
			} else {
				nc.vo.scm.pub.SCMEnv.out("date error.");
			}
		}
	}

	/**
	 * 张海燕 计算换算率/数量/辅数量关系 whj定义规则: 1.如果没有hsl,则输入数量不计算辅数量
	 * 2.如果存货为不按hsl记结存,则无论界面上换算率为何值,只在初录数量时计算辅数量,一旦数量/辅数量有值,则不再使用换算关系
	 * 3.如果存货为按hsl记结存,则将hsl按照固定的看待,此hsl一经录入,则不再根据数量/辅数量的改变而改变,当然,界面上是允许改的,修改后应清掉账面数量
	 * 4.差异数量[辅数量]=盘点数量[辅数量]-账面数量[辅数量] 5.调整数量[辅数量]始终不会影响其他数量[辅数量]和换算率
	 * 
	 * iWhichChanged: 0 主数量修改 1 辅数量修改
	 */
	private void calHslNumAstNum(int iRow, String sMainNum, String sAstNum,
			int iWhichChanged) {
		UFDouble hsl = getUFValueByItemKey(iRow, "hsl");

		UFDouble ufdMainNum = null;
		UFDouble ufdAstNum = null;
		ufdMainNum = getUFValueByItemKey(iRow, sMainNum);
		ufdAstNum = getUFValueByItemKey(iRow, sAstNum);
		// 按hsl记结存
		if (isHsl(iRow) || (null != m_iscountflag && m_iscountflag.booleanValue())) {
			if (iWhichChanged == 1) {// 辅数量修改
				ufdMainNum = ufdAstNum.multiply(hsl);
				getBillCardPanel().setBodyValueAt(ufdMainNum, iRow, sMainNum);
				m_voBill.setItemValue(iRow, sAstNum, ufdAstNum);
				m_voBill.setItemValue(iRow, sMainNum, ufdMainNum);
			} else if (iWhichChanged == 0) {// 数量修改
				ufdAstNum = ufdMainNum.div(hsl);
				getBillCardPanel().setBodyValueAt(ufdAstNum, iRow, sAstNum);
				m_voBill.setItemValue(iRow, sAstNum, ufdAstNum);
				m_voBill.setItemValue(iRow, sMainNum, ufdMainNum);
			}
		} else {
			// 如果不按hsl记结存
			if (iWhichChanged == 0) {// 数量修改
				// 辅助数量为空
				if (ufdAstNum == UFD_ZERO) {
					if (hsl != UFD_ZERO && ufdMainNum != UFD_ZERO) {
						if (!sMainNum.equals("nadjustnum")
								|| !sAstNum.equals("nadjustastnum")) {
							ufdAstNum = ufdMainNum.div(hsl);
							getBillCardPanel().setBodyValueAt(ufdAstNum, iRow,
									sAstNum);
							m_voBill.setItemValue(iRow, sAstNum, ufdAstNum);
						}
					}
				}
			} else if (iWhichChanged == 1) {
				if (nc.vo.ic.pub.GenMethod.isEQZeroOrNull(ufdMainNum)) {
					ufdMainNum = SmartVOUtilExt.mult(ufdAstNum, hsl);
					if (!nc.vo.ic.pub.GenMethod.isEQZeroOrNull(ufdMainNum)) {
						if (!sMainNum.equals("nadjustnum")
								|| !sAstNum.equals("nadjustastnum")) {
							getBillCardPanel().setBodyValueAt(ufdMainNum, iRow,
									sMainNum);
							m_voBill.setItemValue(iRow, sMainNum, ufdMainNum);
						}
					}
				}
			}
		}
		// 执行金额计算公式
		if (sMainNum.equals("nadjustnum")) {
			execEditFormula(iRow, "nadjustnum");
		}
	}

	private GeneralBillVO[] pfVOConvert(SpecialBillVO[] voSp,
			String sSrcBillType, String sDesBillType) {
		GeneralBillVO[] gbvo = null;
		try {
			gbvo = (GeneralBillVO[]) nc.ui.pub.change.PfChangeBO_Client
					.pfChangeBillToBillArray(voSp, sSrcBillType, sDesBillType);
		} catch (Exception e) {
			nc.vo.scm.pub.SCMEnv.error(e);
		}
		return gbvo;
	}

	/**
	 * 设置行号
	 * 
	 * @param voBill
	 */
	private void setRowNo(GeneralBillVO voBill, String sBillType) {
		if (voBill == null || voBill.getChildrenVO().length <= 0)
			return;
		BillRowNoVO.setVOsRowNoByRule(voBill.getItemVOs(), sBillType, "crowno");
	}

	/**
	 * 创建人：刘家清 创建日期：2007-10-22下午04:47:04 创建原因：
	 */
	public SpecialBillVO queryForLinkOper(String PkOrg, String billtype,
			String billid) {

		if (billid == null)
			return null;

		ScmDataSet datas = nc.ui.ic.pub.tools.GenMethod.queryData(
				"ic_special_h", "cspecialhid", new String[] { "pk_corp" },
				new int[] { SmartFieldMeta.JAVATYPE_STRING },
				new String[] { billid }, " dr=0 ");
		String cbillpkcorp = datas == null ? null : (String) datas.getValueAt(
				0, 0);

		if (cbillpkcorp == null || cbillpkcorp.trim().length() <= 0)
			nc.ui.pub.beans.MessageDialog.showHintDlg(this, nc.ui.ml.NCLangRes
					.getInstance().getStrByID("SCMCOMMON",
							"UPPSCMCommon-000270")/* @res "提示" */,
					nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill",
							"UPP4008bill-000062")/* @res "没有符合查询条件的单据！" */);
		else {
			QueryConditionDlgForBill qrydlg = new QueryConditionDlgForBill(this);
			qrydlg.setTempletID(cbillpkcorp, m_sPNodeCode, m_sUserID, null);
			String swhere = " head.cbilltypecode='" + billtype
					+ "' and head.cspecialhid='" + billid + "' ";
			QryConditionVO qcvo = new nc.vo.ic.pub.bill.QryConditionVO(swhere);
			String[] refcodes = nc.ui.ic.pub.tools.GenMethod
					.getDataPowerFieldFromDlg(qrydlg, false, null);
			qrydlg.setCorpRefs("head.pk_corp", refcodes);
			ConditionVO[] convos = ICCommonBusi.getDataPowerConsFromDlg(qrydlg,
					m_sPNodeCode, cbillpkcorp, m_sUserID, refcodes);
			if (convos != null && convos.length > 0) {
				qcvo.setParam(QryConditionVO.QRY_CONDITIONVO, convos);
				String spartwhere = convos[0].getWhereSQL(convos);
				if (spartwhere != null && spartwhere.trim().length() > 0)
					qcvo.setQryCond(swhere + " and " + spartwhere);
			}
			qcvo.setIntParam(0, SpecialBillVO.QRY_WHOLE_PURE);
			loadBillListPanel(qcvo);
		}

		SpecialBillVO voBill = null;
		if (m_alListData != null && m_alListData.size() > 0) {
			voBill = (SpecialBillVO) m_alListData.get(0);
		}

		onList();

		if (voBill != null) {
			String sbill_pk_corp = voBill.getHeaderVO().getPk_corp();
			if (!getClientEnvironment().getCorporation().getPrimaryKey()
					.equals(sbill_pk_corp))
				setButtons(new ButtonObject[] { m_boPrint, m_auditMng });
		}

		setButtonState();
		setBillState();

		return voBill;

	}

	private void onCountBCImport() {
		// TODO 自动生成方法存根

		BarCodeParseVO[] voaImport = null;

		try {
			nc.ui.scm.pub.excel.ExcelBarcodeDialog m_eDlg = new nc.ui.scm.pub.excel.ExcelBarcodeDialog(
					this);
			m_eDlg.setVOName("nc.vo.ic.pub.barcodeparse.BarCodeParseVO");
			m_eDlg.setCHandENnames(getVOStringType());
			m_eDlg.setRadioAddSelected(false);
			// m_eDlg.setRadioAddEnabled(false);
			m_eDlg.setRadioCoverSelected(true);
			// m_eDlg.setRadioCoverEnabled(false);
			m_eDlg.setckbFirstRowSelected(true);

			m_eDlg.showModal();
			voaImport = null;
			if (m_eDlg.isExportOK()) {
				voaImport = (BarCodeParseVO[]) m_eDlg.getExportVO();
				boolean isCover = true;
				isCover = m_eDlg.getRadioSelect();
				if (voaImport == null || voaImport.length == 0
						|| voaImport[0] == null) {
					MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes
							.getInstance().getStrByID("SCMCOMMON",
									"UPPSCMCommon-000059")/*
															 * @res "错误"
															 */, nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill",
							"UPP4008bill-000088")/*
													 * @res "Excel文件条码为空！"
													 */);
					return;
				}

				importItemVO(voaImport, isCover);

			} else
				return;

		} catch (Exception e) {
			nc.vo.scm.pub.SCMEnv.error(e);
			showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"4008bill", "UPP4008bill-000089")/* @res "打开Excel条码文件出错！" */
					+ e.getMessage());
		}

	}
/**
 * 创建人：刘家清 创建时间：2008-8-27 下午03:59:50 创建原因： 
 *
 * @param voaImports
 * @param isCover
 */
	private void importItemVO(BarCodeParseVO[] voaImports, boolean isCover) {
		// TODO 自动生成方法存根
		//onChange();
		setBillCheckInputStatus(nc.vo.ic.pub.bill.BillStatus.CHECKINPUT);

		boolean bShow = true;
		for (BarCodeParseVO voaImport : voaImports) {
			voaImport.setStatus(VOStatus.NEW);
			voaImport.setAttributeValue("pk_corp",m_sCorpID);
			voaImport.setNnumber(nc.vo.ic.pub.GenMethod.ONE_UFDOUBLE);
			voaImport.setBsingletype(nc.vo.ic.pub.GenMethod.UFBOOLEAN_TRUE);
		}

		nc.ui.scm.pub.billutil.ClientCacheHelper.getColValue(voaImports,
				new String[] { "cinvbasid" }, "bd_invbasdoc", "invcode",
				new String[] { "pk_invbasdoc" }, "cinventorycode");
		nc.ui.scm.pub.billutil.ClientCacheHelper.getColValue2(voaImports,
				new String[] { "cinventoryid" }, "bd_invmandoc",
				"pk_invbasdoc", "pk_corp", new String[] { "pk_invmandoc" },
				"cinvbasid", "pk_corp");
		HashMap<KeyObject, SpecailBarCode1VO[]> hsvoaImports = new HashMap<KeyObject, SpecailBarCode1VO[]>();
		HashMap<KeyObject, ArrayList<SpecailBarCode1VO>> hsArrImports = new HashMap<KeyObject, ArrayList<SpecailBarCode1VO>>();
		HashMap<String, ArrayList<KeyObject>> hsRefImports = new HashMap<String, ArrayList<KeyObject>>();
		ArrayList<KeyObject> curKeyObjects = null;
		ArrayList<SpecailBarCode1VO> curBarCode1VOs = null;
		ArrayList<BarCodeParseVO> errImports = new ArrayList<BarCodeParseVO>();
		SpecailBarCode1VO specailBbcs[] = null;
		StringBuilder sErr = new StringBuilder();
		ArrayList alBarcodes = null;
		Integer excelRow = 0;
		KeyObject keyTempItemVO = null;
		for (BarCodeParseVO voaImport : voaImports) {
			excelRow = excelRow + 1;
			if (null != voaImport.getAttributeValue("cinventoryid")) {

				if (voaImport.getAttributeValue("vbarcode") == null
						|| "".equals(voaImport.getAttributeValue("vbarcode").toString())) {
					if (0 == sErr.length()) {
						sErr.append("Excel文件里下列行有错误：\n");
						sErr.append("第" + excelRow.toString() + "行，存货编码："
								+ voaImport.getAttributeValue("cinventorycode")
								+ " ，没有主条码 \n");
					} else
						sErr.append("第" + excelRow.toString() + "行，存货编码："
								+ voaImport.getAttributeValue("cinventorycode")
								+ " ，没有主条码 \n");
					errImports.add(voaImport);
					continue;
				}

				if (!getM_bcRuleArrLen().contains(voaImport.getAttributeValue("vbarcode").toString().length())) {
					if (0 == sErr.length()) {
						sErr.append("Excel文件里下列行有错误：\n");
						sErr.append("第" + excelRow.toString() + "行，存货编码："
								+ voaImport.getAttributeValue("cinventorycode")
								+ " ，条码不符合条码规则 \n");
					} else
						sErr.append("第" + excelRow.toString() + "行，存货编码："
								+ voaImport.getAttributeValue("cinventorycode")
								+ " ，条码不符合条码规则 \n");
					errImports.add(voaImport);
					continue;
				}
				keyTempItemVO = StringKeyJudge.getUKey(voaImport);
				SpecailBarCode1VO barCode1VO = new SpecailBarCode1VO();
				barCode1VO.setStatus(VOStatus.NEW);
				barCode1VO.setPk_corp(m_sCorpID);
				barCode1VO.setNnumber(nc.vo.ic.pub.GenMethod.ONE_UFDOUBLE);
				barCode1VO.setBsingletype(nc.vo.ic.pub.GenMethod.UFBOOLEAN_TRUE);
				barCode1VO.setVbarcode((String)voaImport.getAttributeValue("vbarcode"));
				barCode1VO.setVbarcodesub((String)voaImport.getAttributeValue("vbarcodesub"));
				barCode1VO.setVpackcode((String)voaImport.getAttributeValue("vpackcode"));
				
				curKeyObjects = hsRefImports.get(voaImport.getAttributeValue("cinventoryid").toString());
				if (null == curKeyObjects){
					curKeyObjects = new ArrayList<KeyObject>();
					curKeyObjects.add(keyTempItemVO);
					hsRefImports.put(voaImport.getAttributeValue("cinventoryid").toString(), curKeyObjects);
					
					curBarCode1VOs = new ArrayList<SpecailBarCode1VO>();
					curBarCode1VOs.add(barCode1VO);
					hsArrImports.put(keyTempItemVO, curBarCode1VOs);
				}else{
					Boolean isHave = false;
					for(KeyObject itemKey : curKeyObjects){
						if (StringKeyJudge.compareTwoKeyAll(itemKey,keyTempItemVO)){
							isHave = true;
							curBarCode1VOs = hsArrImports.get(itemKey);
							curBarCode1VOs.add(barCode1VO);
						}
					}
					if (!isHave){
						curKeyObjects.add(keyTempItemVO);
						curBarCode1VOs = new ArrayList<SpecailBarCode1VO>();
						curBarCode1VOs.add(barCode1VO);
						hsArrImports.put(keyTempItemVO, curBarCode1VOs);
					}
				}
				
			} else if (null == voaImport.getAttributeValue("cinventoryid") 
					&& null != voaImport.getAttributeValue("cinventorycode")) {
				if (0 == sErr.length()) {
					sErr.append("Excel文件里下列行有错误：\n");
					sErr.append("第" + excelRow.toString() + "行，存货编码："
							+ voaImport.getAttributeValue("cinventorycode")
							+ " ，存货编码不正确 \n");
				} else
					sErr.append("第" + excelRow.toString() + "行，存货编码："
							+ voaImport.getAttributeValue("cinventorycode")
							+ " ，存货编码不正确 \n");
				errImports.add(voaImport);
			}
		}

		for (KeyObject key : hsArrImports.keySet()) {
			alBarcodes = (ArrayList) hsArrImports.get(key);
			specailBbcs = new SpecailBarCode1VO[alBarcodes.size()];
			alBarcodes.toArray(specailBbcs);
			hsvoaImports.put(key, specailBbcs);
		}

		UFDouble ufNum = new UFDouble(0);
/*		for (SpecialBillItemVO itemVO : m_voBill.getItemVOs()) {
			if (!itemVO.getBarcodeManagerflag().booleanValue())
				continue;
			if (isCover || hsRefImports.containsKey(itemVO.getCinventoryid())) {
				itemVO.setSpecailBarCode1VOs(null);
				itemVO.setNchecknum(null);
			}
		}*/
		SpecailBarCode1VO[] specailBarCode1VOs = null;
		ArrayList<SpecailBarCode1VO> specailBarCode1VOList = null;
		ArrayList<String> hasCountBarCode = new ArrayList<String>();
		ArrayList<String> hasItemBarCode = new ArrayList<String>();
		BarcoderuleVO rulevo = null;
		
		if (null != hsvoaImports)
			for (SpecialBillItemVO itemVO : m_voBill.getItemVOs()) {
				curKeyObjects = hsRefImports.get(itemVO.getCinventoryid());
				if (null == curKeyObjects)
					continue;
				keyTempItemVO = StringKeyJudge.getUKey(itemVO);
				if (!itemVO.getBarcodeManagerflag().booleanValue()) {
					for(KeyObject itemKey : curKeyObjects){
							hsvoaImports.remove(itemKey);
					}
					hsRefImports.remove(itemVO.getCinventoryid());
					continue;
				}
				
				
				
				ufNum = new UFDouble(0);
				specailBarCode1VOList = new ArrayList<SpecailBarCode1VO>();
				for(KeyObject itemKey : curKeyObjects){
					if (StringKeyJudge.compareTwoKey(keyTempItemVO,itemKey)){
						// 修改人：刘家清 修改时间：2008-8-27 下午03:59:54 修改原因：追加模式统一成与“供应商条码文件”一致
						hasCountBarCode.clear();
						specailBarCode1VOs = hsvoaImports.get(itemKey);
						if (null != specailBarCode1VOs) 
							for (SpecailBarCode1VO specailBarCode1VO : specailBarCode1VOs)
								hasCountBarCode.add(specailBarCode1VO.getVbarcode());
						
						hasItemBarCode.clear();
						if (null !=  itemVO.getSpecailBarCode1VOs() && 0 < itemVO.getSpecailBarCode1VOs().length){
							for(SpecailBarCode1VO specailBarCode1VO : itemVO.getSpecailBarCode1VOs()){
								if(isCover){
									if (!hasCountBarCode.contains(specailBarCode1VO.getVbarcode())){										
										ufNum = ufNum.add(specailBarCode1VO.getNnumber());
										specailBarCode1VOList.add(specailBarCode1VO);
									}
								}else{
									hasItemBarCode.add(specailBarCode1VO.getVbarcode());
									ufNum = ufNum.add(specailBarCode1VO.getNnumber());
									specailBarCode1VOList.add(specailBarCode1VO);
								}
							}
						}
						
						
						if (null != specailBarCode1VOs) {
							for (SpecailBarCode1VO specailBarCode1VO : specailBarCode1VOs) {
								if(isCover || !hasItemBarCode.contains(specailBarCode1VO.getVbarcode())){								
									ufNum = ufNum.add(specailBarCode1VO.getNnumber());
									specailBarCode1VOList.add(specailBarCode1VO);
								}
							}
							
							
							hsvoaImports.remove(itemKey);
						}

					}
				}

				if (null != specailBarCode1VOList && 0 < specailBarCode1VOList.size()){
					specailBarCode1VOs = new SpecailBarCode1VO[specailBarCode1VOList.size()];
					specailBarCode1VOList.toArray(specailBarCode1VOs);
					itemVO.setSpecailBarCode1VOs(specailBarCode1VOs);
					if (null != specailBarCode1VOs[0].getVbarcodesub() && !"".equals(specailBarCode1VOs[0].getVbarcodesub()))
						rulevo = BarCodeRuleCache.getBarCodeRule(specailBarCode1VOs[0].getVbarcodesub(), m_sCorpID);
					else
						rulevo = BarCodeRuleCache.getBarCodeRule(specailBarCode1VOs[0].getVbarcode(), m_sCorpID);
					if (null != rulevo && null != ((BarcoderuleHeaderVO)rulevo.getParentVO()).getBasstaddflag()
							&& ((BarcoderuleHeaderVO)rulevo.getParentVO()).getBasstaddflag().booleanValue())
						itemVO.setNcheckastnum(ufNum);
					else
						itemVO.setNchecknum(ufNum);
				}


			}

		try {
			if (0 < hsvoaImports.keySet().size()) {
				String[] refPks = new String[hsvoaImports.keySet().size()];
				KeyObject[] keyObjects = new KeyObject[hsvoaImports.keySet().size()];
				hsvoaImports.keySet().toArray(keyObjects);
				for(int i = 0 ;i<keyObjects.length;i++)
					refPks[i] = keyObjects[i].getAttributeValue("cinventoryid");
				String sWhID = null;
				String sCalID = null;
				if (getBillCardPanel().getHeadItem(m_sMainWhItemKey) != null) {
					sWhID = getBillCardPanel().getHeadItem(m_sMainWhItemKey)
							.getValue();
				}

				if (isQuryPlanprice()) {
					if (sCalID == null && sWhID != null) {

						sCalID = (String) ((Object[]) nc.ui.scm.pub.CacheTool
								.getCellValue("bd_stordoc", "pk_stordoc",
										"pk_calbody", sWhID))[0];

					}
				}

				InvVO[] invVOs = null;
				InvVO[] allinvVOs = null;
				ArrayList<InvVO> arrinvVOs = new ArrayList<InvVO>();
				if (isQuryPlanprice())
					allinvVOs = getInvoInfoBYFormula()
							.getInvParseWithPlanPrice(refPks, sWhID, sCalID,
									true, true);
				else
					allinvVOs = getInvoInfoBYFormula().getBillQuryInvVOs(
							refPks, true, true);

				boolean isLastRow = false;
				
				if (null != allinvVOs)
					for (int i=0;i< allinvVOs.length;i++)
						if (allinvVOs[i].getIsprimarybarcode().booleanValue()
								|| allinvVOs[i].getIssecondarybarcode()
										.booleanValue()){
							if (null != keyObjects[i].getAttributeValue("hsl"))
								allinvVOs[i].setHsl(new UFDouble(keyObjects[i].getAttributeValue("hsl")));
							allinvVOs[i].setCastunitid(keyObjects[i].getAttributeValue("castunitid"));
							allinvVOs[i].setVbatchcode(keyObjects[i].getAttributeValue("batchcode"));
							allinvVOs[i].setcVendorid(keyObjects[i].getAttributeValue("cvendorid"));
							allinvVOs[i].setFreeItemValue("vfree1", keyObjects[i].getAttributeValue("vfree1"));
							allinvVOs[i].setFreeItemValue("vfree2", keyObjects[i].getAttributeValue("vfree2"));
							allinvVOs[i].setFreeItemValue("vfree3", keyObjects[i].getAttributeValue("vfree3"));
							allinvVOs[i].setFreeItemValue("vfree4", keyObjects[i].getAttributeValue("vfree4"));
							allinvVOs[i].setFreeItemValue("vfree5", keyObjects[i].getAttributeValue("vfree5"));
							arrinvVOs.add(allinvVOs[i]);
						}
				if (arrinvVOs.size() > 0) {
					invVOs = new InvVO[arrinvVOs.size()];
					arrinvVOs.toArray(invVOs);
					getBillCardPanel().addLine();
					int curRow = getBillCardPanel().getRowCount() - 1;

					if (invVOs != null && invVOs.length > 0) {
						if (curRow == getBillCardPanel().getRowCount() - 1)
							isLastRow = true;

						for (int i = invVOs.length - 1; i >= 0; i--) {
							if (i < invVOs.length - 1) {
								getBillCardPanel().addLine();
							} else {
								if (getBillCardPanel().getBillModel()
										.getRowState(curRow) == BillModel.NORMAL)
									getBillCardPanel().getBillModel()
											.setRowState(curRow,
													BillModel.MODIFICATION);

							}
						}
						if (isLastRow) {

							nc.ui.scm.pub.report.BillRowNo.addLineRowNos(
									getBillCardPanel(), m_sBillTypeCode,
									m_sBillRowNo,

									invVOs.length);

						}

						// 单据行号
						else {
							nc.ui.scm.pub.report.BillRowNo.insertLineRowNos(
									getBillCardPanel(), m_sBillTypeCode,
									m_sBillRowNo, curRow + invVOs.length - 1,
									invVOs.length - 1);
						}
					}

					setBodyInVO(invVOs, curRow);
					

					if (null != hsvoaImports)
						for (SpecialBillItemVO itemVO : m_voBill.getItemVOs()) {
							curKeyObjects = hsRefImports.get(itemVO.getCinventoryid());
							if (null == curKeyObjects)
								continue;
							keyTempItemVO = StringKeyJudge.getUKey(itemVO);
							if (!itemVO.getBarcodeManagerflag().booleanValue()) {
								for(KeyObject itemKey : curKeyObjects){
										hsvoaImports.remove(itemKey);
								}
								hsRefImports.remove(itemVO.getCinventoryid());
								continue;
							}
							
							
							
							ufNum = new UFDouble(0);
							specailBarCode1VOList = new ArrayList<SpecailBarCode1VO>();
							for(KeyObject itemKey : curKeyObjects){
								if (StringKeyJudge.compareTwoKey(keyTempItemVO,itemKey)){
									
									specailBarCode1VOs = hsvoaImports.get(itemKey);
									if (null != specailBarCode1VOs) {
										for (SpecailBarCode1VO specailBarCode1VO : specailBarCode1VOs) {
											ufNum = ufNum.add(specailBarCode1VO.getNnumber());
											specailBarCode1VOList.add(specailBarCode1VO);
										}
										
										
										hsvoaImports.remove(itemKey);
									}

								}
							}
							if (null != specailBarCode1VOList && 0 < specailBarCode1VOList.size()){
								specailBarCode1VOs = new SpecailBarCode1VO[specailBarCode1VOList.size()];
								specailBarCode1VOList.toArray(specailBarCode1VOs);
								itemVO.setSpecailBarCode1VOs(specailBarCode1VOs);
								if (null != specailBarCode1VOs[0].getVbarcodesub() && !"".equals(specailBarCode1VOs[0].getVbarcodesub()))
									rulevo = BarCodeRuleCache.getBarCodeRule(specailBarCode1VOs[0].getVbarcodesub(), m_sCorpID);
								else
									rulevo = BarCodeRuleCache.getBarCodeRule(specailBarCode1VOs[0].getVbarcode(), m_sCorpID);
								if (null != rulevo && null != ((BarcoderuleHeaderVO)rulevo.getParentVO()).getBasstaddflag()
										&& ((BarcoderuleHeaderVO)rulevo.getParentVO()).getBasstaddflag().booleanValue())
									itemVO.setNcheckastnum(ufNum);
								else
									itemVO.setNchecknum(ufNum);
							}


						}
				}

			}
		} catch (Exception e) {
			bShow = false;
			GenMethod.handleException(this, null, e);
		}

		for (int i = 0; i < getBillCardPanel().getRowCount(); i++) {
			if (!m_voBill.getItemVOs()[i].getBarcodeManagerflag()
					.booleanValue())
				continue;
			getBillCardPanel().setBodyValueAt(
					m_voBill.getItemVOs()[i].getNchecknum(), i, "nchecknum");
			execEditFormula(i, "nchecknum");
			if (null != getBillCardPanel().getBodyValueAt(i, "hsl")){
				calHslNumAstNum(i, "nchecknum", "ncheckastnum", 0);
				calHslNumAstNum(i, "nadjustnum", "nadjustastnum", 0);
				execEditFormula(i, "ncheckastnum");
			}
			
			calHslNumAstNum(i, "cysl", "cyfsl", 0);
			execEditFormula(i, "nadjustnum");
			

			if (null == m_voBill.getItemVOs()[i].getCrowno()
					&& null != getBillCardPanel().getBodyValueAt(i,
							m_sBillRowNo))
				m_voBill.getItemVOs()[i].setCrowno((String) getBillCardPanel()
						.getBodyValueAt(i, m_sBillRowNo));

			if (getBillCardPanel().getBillModel().getRowState(i) == BillModel.NORMAL)
				getBillCardPanel().getBillModel().setRowState(i,
						BillModel.MODIFICATION);
		}

		if (null != sErr && sErr.length() > 0) {
			bShow = false;
			sErr.append("是否导出未成功导入的条码！");
			if (showYesNoMessage(sErr.toString()) == MessageDialog.ID_YES){
				BarCodeParseVO[] barCodeParseVOs = new BarCodeParseVO[errImports.size()];
				errImports.toArray(barCodeParseVOs);
				// TODO 自动生成方法存根
				String sFilePathDir = null;
	
				if (getChooser().showSaveDialog(this) == javax.swing.JFileChooser.CANCEL_OPTION)
					return;
				sFilePathDir = getChooser().getSelectedFile().toString();
				if (sFilePathDir == null) {
					showHintMessage("请输入文件名保存!");
					return;
				}
	
				if (sFilePathDir.indexOf(".xls") < 0)
					sFilePathDir = sFilePathDir + ".xls";
				// 空数据时支持导出格式文件
				/*
				 * if (packbillItemVOs == null) { nc.vo.scm.pub.ctrl.GenMsgCtrl
				 * .printErr("num error,or list null.Method:CardPanelCtrl::show(int)");
				 * return; }
				 */
	
				exportVOToExcel(barCodeParseVOs, sFilePathDir);
			}
		}
		showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec",
		"UPP4008spec-000572")/* @res "实盘条码导入成功"*/);
		if (bShow)
			showWarningMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec",
			"UPP4008spec-000572")/* @res "实盘条码导入成功"*/);

	}
	
	protected javax.swing.JFileChooser m_chooser = null;

	protected javax.swing.JFileChooser getChooser() {

		if (m_chooser == null) {
			m_chooser = new JFileChooser();
			m_chooser.setDialogType(JFileChooser.SAVE_DIALOG);

			// m_chooser.setFileSelectionMode(UIFileChooser.DIRECTORIES_ONLY);
		}
		return m_chooser;

	}

	public final static String[][] getVOStringType() {
		String[][] s = null;
		s = new String[][] {

				{
						nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID(
								"4008bill", "UPP4008bill-000456")/*
																	 * @res
																	 * "存货编码"
																	 */, "cinventorycode" },
				{
						nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
								"UC000-0002060")/* @res "批次号" */, "vbatchcode" },

				{
						nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
								"UC000-0003938")/* @res "辅计量单位" */,
						"castunitcode" },
				{
						nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
								"UC000-0002161")/* @res "换算率" */, "hsl" },
				{
						nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
								"UC000-0000275")/* @res "供应商" */,
						"cvendorcode" },

				{
						nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
								"UC000-0003330")/* @res "自由项1" */, "vfree1" },
				{
						nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
								"UC000-0003333")/* @res "自由项2" */, "vfree2" },
				{
						nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
								"UC000-0003335")/* @res "自由项3" */, "vfree3" },
				{
						nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
								"UC000-0003337")/* @res "自由项4" */, "vfree4" },
				{
						nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
								"UC000-0003339")/* @res "自由项5" */, "vfree5" },
				{
					nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID(
							"40080216", "UPT40080216-000015")/*
																 * @res
																 * "箱条码"
																 */, "vpackcode" },
				{
						nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID(
								"common", "UC000-0000077")/* @res "主条码" */,
						"vbarcode" },
				{
						nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID(
								"common", "UC000-0002819")/* @res "次条码" */,
						"vbarcodesub" },
				{
						nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
								"UC000-0002282")/* @res "数量" */, "nnum" } };
		return s;
	
	}

	private ArrayList<Integer> m_bcRuleArrLen = null;
	
	
	protected UIPanel getPnlBc() {
		if (m_pnlBarCode == null) {
			m_pnlBarCode = new UIPanel();
			try {

				UILabel lbName = new UILabel(nc.ui.ml.NCLangRes.getInstance()
						.getStrByID("4008bill", "UPP4008bill-000063")/*
																		 * @res
																		 * "请输入条形码: "
																		 */);
				lbName.setPreferredSize(new java.awt.Dimension(80, 22));

				m_pnlBarCode.setLayout(new java.awt.FlowLayout());
				((java.awt.FlowLayout) m_pnlBarCode.getLayout()).setHgap(20);
				((java.awt.FlowLayout) m_pnlBarCode.getLayout())
						.setAlignment(java.awt.FlowLayout.LEFT);

				m_utfBarCode = new nc.ui.ic.pub.barcode.UIBarCodeTextFieldNew();

				m_utfBarCode.addBarCodeInputListener(this, m_sCorpID);
				m_utfBarCode.setPreferredSize(new java.awt.Dimension(300, 22));
				m_utfBarCode.setMaxLength(100);
				m_utfBarCode.setMaxLength(300);

				m_pnlBarCode.add(lbName);
				m_pnlBarCode.add(m_utfBarCode);

			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		} else
			m_pnlBarCode.add(m_utfBarCode);
/*		if (m_bAddBarCodeField == false) {
			m_pnlBarCode.setVisible(false);
		}*/
		return m_pnlBarCode;
	}

	
	public void errormessageshow(String error) {
		// TODO 自动生成方法存根
		nc.ui.pub.beans.MessageDialog.showHintDlg(this, nc.ui.ml.NCLangRes
				.getInstance().getStrByID("SCMCOMMON",
						"UPPSCMCommon-000270")/* @res "提示" */, error);
	}

	public String getBusiTypeItemKey() {
		// TODO 自动生成方法存根
		return "";
	}

	public void scanAddBarcodeline(BarCodeParseVO vo) throws Exception {
		// TODO 自动生成方法存根
		try {
			if (vo == null)
				return;
	
			if (!m_iscountflag.booleanValue()){
				showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec",
				"UPP4008spec-000567")/* @res "非条码盘点单不能进行条码操作！" */);
				throw new BusinessException(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec",
				"UPP4008spec-000567")/* @res "非条码盘点单不能进行条码操作！" */);

			}
			
			String sRowPrimaryKey = getBarcodeCtrl().getBarcodeRowPrimaryKey(
					m_sCorpID, vo);
	
			// 通过条码规则中定义的关键字列
			String[] sPrimaryKeyItems = vo.getMatchPrimaryKeyItems();
	
			BarCodeParseVO[] barCodeParseVOs = new BarCodeParseVO[] { vo };
			boolean bBox = false;
			scanadd(sRowPrimaryKey, barCodeParseVOs, bBox, sPrimaryKeyItems);
			// 光标回条码扫描框
			m_utfBarCode.requestFocus();
			return;
		} catch (Exception e) {
			throw nc.ui.ic.pub.tools.GenMethod.handleException(null, null, e);
		}
	}
	
	protected void scanadd(String sRowPrimaryKey,
			BarCodeParseVO[] barCodeParseVOs, boolean bBox,
			String[] sPrimaryKeyItems) throws Exception {
		try {
			if (sPrimaryKeyItems != null)
				for (BarCodeParseVO barCodeParseVO : barCodeParseVOs)
					for (String sPrimaryKeyItem : sPrimaryKeyItems)
						if (null == barCodeParseVO
								.getAttributeValue(sPrimaryKeyItem))
							throw new BusinessException(nc.ui.ml.NCLangRes
									.getInstance().getStrByID("4008bill",
											"UPP4008bill-000537")/*
																	 * @res
																	 * "条码解析失败，请检查条码联合关键字有无对应档案"
																	 */);
			// 取当前行
			int iRow = getBillCardPanel().getBillTable().getSelectedRow();
			if (sRowPrimaryKey != null && barCodeParseVOs != null
					&& sRowPrimaryKey.length() > 4
					&& !sRowPrimaryKey.startsWith("NULL")) // 箱条码中存在存货信息
			{
				
				String checkInvManId = null;
				String checkInvcastunitid = null;
				if (sPrimaryKeyItems != null)
					for (BarCodeParseVO barCodeParseVO : barCodeParseVOs){
						for (String sPrimaryKeyItem : sPrimaryKeyItems){
							if (BarcodeparseCtrl.InvManKey.equals(sPrimaryKeyItem))
								checkInvManId = (String)barCodeParseVO.getAttributeValue(sPrimaryKeyItem);
							if (BarcodeparseCtrl.InvcastunitidKey.equals(sPrimaryKeyItem))
								checkInvcastunitid = (String)barCodeParseVO.getAttributeValue(sPrimaryKeyItem);	
							
	
						}
						
						if (null != checkInvManId && !"".equals(checkInvManId) 
								&& null != checkInvcastunitid && !"".equals(checkInvcastunitid)){
							nc.vo.bd.b15.MeasureRateVO measureVO = m_voInvMeas.getMeasureRateDirect(m_sCorpID, checkInvManId, checkInvcastunitid);
							if (null == measureVO)
								throw new BusinessException(nc.ui.ml.NCLangRes
										.getInstance().getStrByID("4008bill",
										"UPP4008bill-000557")/*
																 * @res
																 * "条码对应的辅单位不合法"
																 */);
							
						}
						
					}

				// 到单据中查找行符合条件的行arraylist ,优先处理选中行
				ArrayList alResultTemp = getBarcodeCtrl().scanBillCardItem(
						sRowPrimaryKey, getM_voBill(), iRow, sPrimaryKeyItems);

				ArrayList alResult = new ArrayList();

				// 对箱条码，只适用第一行，处于实际数量填充的校验
				// 否则无法处理这种情况的数据异常回滚
				if (bBox && alResultTemp != null && alResultTemp.size() > 0) {
					alResult.add(alResultTemp.get(0));
				} else
					alResult = alResultTemp;

				// 如果arraylist为空，或len==0，提示没有对应存货数据
				if ((alResult == null || alResult.size() == 0)) {

					if (barCodeParseVOs.length > 0 && !bBox)
						if (barCodeParseVOs[0]
								.getAttributeValue(BarcodeparseCtrl.VBARCODESUB) != null
								&& barCodeParseVOs[0]
										.getAttributeValue(BarcodeparseCtrl.VBARCODE) == null) {

							throw new BusinessException(nc.ui.ml.NCLangRes
									.getInstance().getStrByID("4008bill",
											"UPP4008bill-000524")/*
																	 * @res
																	 * "请先扫描录入存货主条码"
																	 */);

						}

					// (1)对调拨出入库单不允许自动增加行的情况下
					// (2)对其他入和其他出界面上，也不允许扫描自动增加行
					if (true) {
						// 没有对应的存货，自动增加一行存货
						java.util.ArrayList alInvID = new java.util.ArrayList();
						alInvID.add(sRowPrimaryKey);

						boolean needCastunitname = false;
						if (((barCodeParseVOs[0]
								.getAttributeValue(BarcodeparseCtrl.VBARCODE) != null
								&& barCodeParseVOs[0]
										.getAttributeValue(BarcodeparseCtrl.VBARCODESUB) == null && barCodeParseVOs[0]
								.getBasstaddflag().booleanValue())
								|| (barCodeParseVOs[0]
										.getAttributeValue(BarcodeparseCtrl.VBARCODE) == null
										&& barCodeParseVOs[0]
												.getAttributeValue(BarcodeparseCtrl.VBARCODESUB) != null && barCodeParseVOs[0]
										.getBasstaddflagsub().booleanValue()) || (barCodeParseVOs[0]
								.getAttributeValue(BarcodeparseCtrl.VBARCODE) != null
								&& barCodeParseVOs[0]
										.getAttributeValue(BarcodeparseCtrl.VBARCODESUB) != null && barCodeParseVOs[0]
								.getBasstaddflagsub().booleanValue()))) {
							needCastunitname = true;

						}

						int iCurFixLine = getBarcodeCtrl().fixBlankLineWithInv(
								this, getM_voBill(), alInvID, IItemKey.INVID,
								IItemKey.WAREHOUSE, IItemKey.CALBODY,
								"4R", IItemKey.CROWNO,
								sPrimaryKeyItems, needCastunitname);
						// 置回光标
						m_utfBarCode.requestFocus();

						// int rowNow = 0;
						boolean bAllforFix = true;
						int iNumUsed = 0;
						scanfixline(barCodeParseVOs, iCurFixLine, iNumUsed,
								bAllforFix);
						int icurline = getBillCardPanel().getBillTable()
								.getSelectedRow();
						if (icurline >= 0) {
							String vbatchcode = (String) getBillCardPanel()
									.getBodyValueAt(icurline,
											IItemKey.VBATCHCODE);
							if (vbatchcode != null
									&& vbatchcode.trim().length() > 0) {
								BillEditEvent ev = new BillEditEvent(
										getBillCardPanel().getBodyItem(
												IItemKey.VBATCHCODE), null,
										vbatchcode, IItemKey.VBATCHCODE,
										icurline, BillItem.BODY);
								afterLotEdit(ev);
							}

						}
					} else {

						/*
						 * errormessageshow(nc.ui.ml.NCLangRes.getInstance()
						 * .getStrByID("4008bill", "UPP4008bill-000127") @res
						 * "扫描识别出新的存货条码，但当前单据界面不允许新增加存货行！" );
						 */
						throw new BusinessException(nc.ui.ml.NCLangRes
								.getInstance().getStrByID("4008bill",
										"UPP4008bill-000127")
						/*
						 * @res "扫描识别出新的存货条码，但当前单据界面不允许新增加存货行！"
						 */
						);

					}
				} else {
					int icurline = Integer.parseInt(alResult.get(0).toString());

					if (barCodeParseVOs.length > 0) {
						if ((barCodeParseVOs[0]
								.getAttributeValue(BarcodeparseCtrl.VBARCODESUB) != null)
								&& (barCodeParseVOs[0]
										.getAttributeValue(BarcodeparseCtrl.VBARCODE) == null)
								&& (icurline != iRow)) {

							throw new BusinessException(nc.ui.ml.NCLangRes
									.getInstance().getStrByID("4008bill",
											"UPP4008bill-000524")/*
																	 * @res
																	 * "请先扫描录入存货主条码"
																	 */);

						}
						if ((barCodeParseVOs[0]
								.getAttributeValue(BarcodeparseCtrl.VBARCODESUB) != null)
								&& (!getM_voBill().getItemVOs()[icurline]
										.getIssecondarybarcode().booleanValue())) {

							throw new BusinessException(nc.ui.ml.NCLangRes
									.getInstance().getStrByID("4008bill",
											"UPP4008bill-000527")/*
																	 * @res
																	 * "非次条码管理存货不支持扫描录录入次条码"
																	 */);

						}

						if ((barCodeParseVOs[0]
								.getAttributeValue(BarcodeparseCtrl.VBARCODE) != null
								&& barCodeParseVOs[0].getCbarcoderuleid() != null
								&& getM_voBill().getItemVOs()[icurline]
										.getBarCodeVOs() != null
								&& getM_voBill().getItemVOs()[icurline]
										.getBarCodeVOs().length > 0
								&& getM_voBill().getItemVOs()[icurline]
										.getBarCodeVOs()[getM_voBill()
										.getItemVOs()[icurline].getBarCodeVOs().length - 1]
										.getCbarcoderuleid() != null && (!getM_voBill()
								.getItemVOs()[icurline].getBarCodeVOs()[getM_voBill()
								.getItemVOs()[icurline].getBarCodeVOs().length - 1]
								.getCbarcoderuleid().equals(
										barCodeParseVOs[0].getCbarcoderuleid())))) {

							throw new BusinessException(nc.ui.ml.NCLangRes
									.getInstance().getStrByID("4008bill",
											"UPP4008bill-000532")/*
																	 * @res
																	 * "同一存货，同一行，只能扫描录入同一条码规则的主条码"
																	 */);

						}

						if ((barCodeParseVOs[0]
								.getAttributeValue(BarcodeparseCtrl.VBARCODESUB) != null
								&& barCodeParseVOs[0].getCbarcoderuleidsub() != null
								&& getM_voBill().getItemVOs()[icurline]
										.getBarCodeVOs() != null
								&& getM_voBill().getItemVOs()[icurline]
										.getBarCodeVOs().length > 1
								&& getM_voBill().getItemVOs()[icurline]
										.getBarCodeVOs()[getM_voBill()
										.getItemVOs()[icurline].getBarCodeVOs().length - 2]
										.getCbarcoderuleidsub() != null && (!getM_voBill()
								.getItemVOs()[icurline].getBarCodeVOs()[getM_voBill()
								.getItemVOs()[icurline].getBarCodeVOs().length - 2]
								.getCbarcoderuleidsub().equals(
										barCodeParseVOs[0]
												.getCbarcoderuleidsub())))) {

							throw new BusinessException(nc.ui.ml.NCLangRes
									.getInstance().getStrByID("4008bill",
											"UPP4008bill-000533")/*
																	 * @res
																	 * "同一存货，同一行，只能扫描录入同一条码规则的次条码"
																	 */);

						}

						if ((barCodeParseVOs[0]
								.getAttributeValue(BarcodeparseCtrl.VBARCODESUB) != null)
								&& (barCodeParseVOs[0]
										.getAttributeValue(BarcodeparseCtrl.VBARCODE) == null)
								&& (getM_voBill().getItemVOs()[icurline]
										.getBarCodeVOs() != null)
								&& getM_voBill().getItemVOs()[icurline]
										.getBarCodeVOs().length > 0
								&& (getM_voBill().getItemVOs()[icurline]
										.getBarCodeVOs()[getM_voBill()
										.getItemVOs()[icurline].getBarCodeVOs().length - 1]
										.getBsingletype().booleanValue())
								&& (getM_voBill().getItemVOs()[icurline]
										.getBarCodeVOs()[getM_voBill()
										.getItemVOs()[icurline].getBarCodeVOs().length - 1]
										.getAttributeValue(BarcodeparseCtrl.VBARCODESUB) != null)) {

							throw new BusinessException(nc.ui.ml.NCLangRes
									.getInstance().getStrByID("4008bill",
											"UPP4008bill-000524")/*
																	 * @res
																	 * "请先扫描录入存货主条码"
																	 */);

						}
						if ((barCodeParseVOs[0]
								.getAttributeValue(BarcodeparseCtrl.VBARCODE) != null)
								&& (!getM_voBill().getItemVOs()[icurline]
										.getIsprimarybarcode().booleanValue())) {

							throw new BusinessException(nc.ui.ml.NCLangRes
									.getInstance().getStrByID("4008bill",
											"UPP4008bill-000526")/*
																	 * @res
																	 * "非主条码管理存货不支持扫描录录入主条码"
																	 */);

						}
						if ((barCodeParseVOs[0]
								.getAttributeValue(BarcodeparseCtrl.VBARCODE) != null)
								&& (getM_voBill().getItemVOs()[icurline]
										.getIsprimarybarcode().booleanValue())
								&& (getM_voBill().getItemVOs()[icurline]
										.getIssecondarybarcode().booleanValue())
								&& (getM_voBill().getItemVOs()[icurline]
										.getBarCodeVOs() != null)
								&& getM_voBill().getItemVOs()[icurline]
										.getBarCodeVOs().length > 0
								&& (getM_voBill().getItemVOs()[icurline]
										.getBarCodeVOs()[getM_voBill()
										.getItemVOs()[icurline].getBarCodeVOs().length - 1]
										.getAttributeValue(BarcodeparseCtrl.VBARCODESUB) == null)) {
							if (icurline != iRow)
								getBillCardPanel().getBillTable()
										.getSelectionModel()
										.setSelectionInterval(icurline,
												icurline);
							throw new BusinessException(nc.ui.ml.NCLangRes
									.getInstance().getStrByID("4008bill",
											"UPP4008bill-000525")/*
																	 * @res
																	 * "请先扫描录入存货次条码"
																	 */);

						}

						if (((barCodeParseVOs[0]
								.getAttributeValue(BarcodeparseCtrl.VBARCODE) != null
								&& barCodeParseVOs[0]
										.getAttributeValue(BarcodeparseCtrl.VBARCODESUB) == null && barCodeParseVOs[0]
								.getBasstaddflag().booleanValue())
								|| (barCodeParseVOs[0]
										.getAttributeValue(BarcodeparseCtrl.VBARCODE) == null
										&& barCodeParseVOs[0]
												.getAttributeValue(BarcodeparseCtrl.VBARCODESUB) != null && barCodeParseVOs[0]
										.getBasstaddflagsub().booleanValue()) || (barCodeParseVOs[0]
								.getAttributeValue(BarcodeparseCtrl.VBARCODE) != null
								&& barCodeParseVOs[0]
										.getAttributeValue(BarcodeparseCtrl.VBARCODESUB) != null && barCodeParseVOs[0]
								.getBasstaddflagsub().booleanValue()))
								&& (getM_voBill().getItemVOs()[icurline]
										.getCastunitname() == null
										|| (null != getM_voBill().getItemVOs()[icurline]
												.getCastunitname() && ""
												.equals(getM_voBill()
														.getItemVOs()[icurline]
														.getCastunitname()))
										|| null == getBillCardPanel()
												.getBodyValueAt(icurline,
														"castunitname") || (null != getBillCardPanel()
										.getBodyValueAt(icurline,
												"castunitname") && ""
										.equals(getBillCardPanel()
												.getBodyValueAt(icurline,
														"castunitname")
												.toString().trim())))) {

							throw new BusinessException(nc.ui.ml.NCLangRes
									.getInstance().getStrByID("4008bill",
											"UPP4008bill-000531")/*
																	 * @res
																	 * "非辅计量管理存货不支持扫描录录入按辅数量增加的条码"
																	 */);

						}
					}

					getBillCardPanel().getBillTable().getSelectionModel()
							.setSelectionInterval(icurline, icurline);
					scanUpdateLine(barCodeParseVOs, alResult);
				}
			} else // 条码中不存在存货信息
			{
				// 需要找当前焦点行，进行扫描处理
				if (barCodeParseVOs.length > 0) {

					if (iRow < 0 && getBillCardPanel().getRowCount() == 1) {
						iRow = 0;
						getBillCardPanel().getBillTable().getSelectionModel()
								.setSelectionInterval(0, 0);
					}

					if (getM_voBill().getItemVOs() == null
							|| iRow < 0
							|| getM_voBill().getItemVOs().length < (iRow + 1)
							|| getM_voBill().getItemVOs()[iRow] == null
							|| getM_voBill().getItemVOs()[iRow]
									.getCinventorycode() == null
									|| getM_voBill().getItemVOs()[iRow]
																	.getCinventoryid() == null) {

						throw new BusinessException(nc.ui.ml.NCLangRes
								.getInstance().getStrByID("4008bill",
										"UPP4008bill-000534")/*
																 * @res
																 * "请先扫描录入存在存货信息的主条码"
																 */);

					}
					
					String checkInvManId = null;
					String checkInvcastunitid = null;
					if (sPrimaryKeyItems != null)
						for (BarCodeParseVO barCodeParseVO : barCodeParseVOs){
							checkInvManId = getM_voBill().getItemVOs()[iRow].getCinventoryid();
							for (String sPrimaryKeyItem : sPrimaryKeyItems){

								if (BarcodeparseCtrl.InvcastunitidKey.equals(sPrimaryKeyItem))
									checkInvcastunitid = (String)barCodeParseVO.getAttributeValue(sPrimaryKeyItem);	
								
		
							}
							
							if (null != checkInvManId && !"".equals(checkInvManId) 
									&& null != checkInvcastunitid && !"".equals(checkInvcastunitid)){
								nc.vo.bd.b15.MeasureRateVO measureVO = m_voInvMeas.getMeasureRateDirect(m_sCorpID, checkInvManId, checkInvcastunitid);
								if (null == measureVO)
									throw new BusinessException(nc.ui.ml.NCLangRes
											.getInstance().getStrByID("4008bill",
											"UPP4008bill-000557")/*
																	 * @res
																	 * "条码对应的辅单位不合法"
																	 */);
								
							}
							
						}

					if ((barCodeParseVOs[0]
							.getAttributeValue(BarcodeparseCtrl.VBARCODESUB) != null)
							&& (getM_voBill().getItemVOs()[iRow]
									.getCinventorycode() == null || getM_voBill()
									.getItemVOs()[iRow].getBarCodeVOs() == null)) {

						throw new BusinessException(nc.ui.ml.NCLangRes
								.getInstance().getStrByID("4008bill",
										"UPP4008bill-000524")/*
																 * @res
																 * "请先扫描录入存货主条码"
																 */);

					}
					
					
					

					if ((barCodeParseVOs[0]
							.getAttributeValue(BarcodeparseCtrl.VBARCODESUB) != null)
							&& (!getM_voBill().getItemVOs()[iRow]
									.getIssecondarybarcode().booleanValue())) {

						throw new BusinessException(nc.ui.ml.NCLangRes
								.getInstance().getStrByID("4008bill",
										"UPP4008bill-000527")/*
																 * @res
																 * "非次条码管理存货不支持扫描录录入次条码"
																 */);

					}

					if ((barCodeParseVOs[0]
							.getAttributeValue(BarcodeparseCtrl.VBARCODE) != null
							&& barCodeParseVOs[0].getCbarcoderuleid() != null
							&& getM_voBill().getItemVOs()[iRow].getBarCodeVOs() != null
							&& getM_voBill().getItemVOs()[iRow].getBarCodeVOs()[getM_voBill()
									.getItemVOs()[iRow].getBarCodeVOs().length - 1]
									.getCbarcoderuleid() != null && (!getM_voBill()
							.getItemVOs()[iRow].getBarCodeVOs()[getM_voBill()
							.getItemVOs()[iRow].getBarCodeVOs().length - 1]
							.getCbarcoderuleid().equals(
									barCodeParseVOs[0].getCbarcoderuleid())))) {

						throw new BusinessException(nc.ui.ml.NCLangRes
								.getInstance().getStrByID("4008bill",
										"UPP4008bill-000532")/*
																 * @res
																 * "同一存货，同一行，只能扫描录入同一条码规则的主条码"
																 */);

					}

					if ((barCodeParseVOs[0]
							.getAttributeValue(BarcodeparseCtrl.VBARCODESUB) != null
							&& barCodeParseVOs[0].getCbarcoderuleidsub() != null
							&& getM_voBill().getItemVOs()[iRow].getBarCodeVOs() != null
							&& getM_voBill().getItemVOs()[iRow].getBarCodeVOs().length > 1
							&& getM_voBill().getItemVOs()[iRow].getBarCodeVOs()[getM_voBill()
									.getItemVOs()[iRow].getBarCodeVOs().length - 2]
									.getCbarcoderuleidsub() != null && (!getM_voBill()
							.getItemVOs()[iRow].getBarCodeVOs()[getM_voBill()
							.getItemVOs()[iRow].getBarCodeVOs().length - 2]
							.getCbarcoderuleidsub().equals(
									barCodeParseVOs[0].getCbarcoderuleidsub())))) {

						throw new BusinessException(nc.ui.ml.NCLangRes
								.getInstance().getStrByID("4008bill",
										"UPP4008bill-000533")/*
																 * @res
																 * "同一存货，同一行，只能扫描录入同一条码规则的次条码"
																 */);

					}

					if ((barCodeParseVOs[0]
							.getAttributeValue(BarcodeparseCtrl.VBARCODESUB) != null)
							&& (barCodeParseVOs[0]
									.getAttributeValue(BarcodeparseCtrl.VBARCODE) == null)
							&& (getM_voBill().getItemVOs()[iRow]
									.getBarCodeVOs() != null)
							&& (getM_voBill().getItemVOs()[iRow]
									.getBarCodeVOs()[getM_voBill().getItemVOs()[iRow]
									.getBarCodeVOs().length - 1]
									.getBsingletype().booleanValue())
							&& (getM_voBill().getItemVOs()[iRow]
									.getBarCodeVOs()[getM_voBill().getItemVOs()[iRow]
									.getBarCodeVOs().length - 1]
									.getAttributeValue(BarcodeparseCtrl.VBARCODESUB) != null)) {

						throw new BusinessException(nc.ui.ml.NCLangRes
								.getInstance().getStrByID("4008bill",
										"UPP4008bill-000524")/*
																 * @res
																 * "请先扫描录入存货主条码"
																 */);

					}
					if ((barCodeParseVOs[0]
							.getAttributeValue(BarcodeparseCtrl.VBARCODE) != null)
							&& (!getM_voBill().getItemVOs()[iRow]
									.getIsprimarybarcode().booleanValue())) {

						throw new BusinessException(nc.ui.ml.NCLangRes
								.getInstance().getStrByID("4008bill",
										"UPP4008bill-000526")/*
																 * @res
																 * "非主条码管理存货不支持扫描录录入主条码"
																 */);

					}
					if ((barCodeParseVOs[0]
							.getAttributeValue(BarcodeparseCtrl.VBARCODE) != null)
							&& (getM_voBill().getItemVOs()[iRow]
									.getIsprimarybarcode().booleanValue())
							&& (getM_voBill().getItemVOs()[iRow]
									.getIssecondarybarcode().booleanValue())
							&& (getM_voBill().getItemVOs()[iRow]
									.getBarCodeVOs() != null)
							&& (getM_voBill().getItemVOs()[iRow]
									.getBarCodeVOs()[getM_voBill().getItemVOs()[iRow]
									.getBarCodeVOs().length - 1]
									.getAttributeValue(BarcodeparseCtrl.VBARCODESUB) == null)) {

						throw new BusinessException(nc.ui.ml.NCLangRes
								.getInstance().getStrByID("4008bill",
										"UPP4008bill-000525")/*
																 * @res
																 * "请先扫描录入存货次条码"
																 */);

					}

					if (((barCodeParseVOs[0]
							.getAttributeValue(BarcodeparseCtrl.VBARCODE) != null
							&& barCodeParseVOs[0]
									.getAttributeValue(BarcodeparseCtrl.VBARCODESUB) == null && barCodeParseVOs[0]
							.getBasstaddflag().booleanValue())
							|| (barCodeParseVOs[0]
									.getAttributeValue(BarcodeparseCtrl.VBARCODE) == null
									&& barCodeParseVOs[0]
											.getAttributeValue(BarcodeparseCtrl.VBARCODESUB) != null && barCodeParseVOs[0]
									.getBasstaddflagsub().booleanValue()) || (barCodeParseVOs[0]
							.getAttributeValue(BarcodeparseCtrl.VBARCODE) != null
							&& barCodeParseVOs[0]
									.getAttributeValue(BarcodeparseCtrl.VBARCODESUB) != null && barCodeParseVOs[0]
							.getBasstaddflagsub().booleanValue()))
							&& (getM_voBill().getItemVOs()[iRow]
									.getCastunitname() == null
									|| (null != getM_voBill().getItemVOs()[iRow]
											.getCastunitname() && ""
											.equals(getM_voBill().getItemVOs()[iRow]
													.getCastunitname()))
									|| null == getBillCardPanel()
											.getBodyValueAt(iRow,
													"castunitname") || (null != getBillCardPanel()
									.getBodyValueAt(iRow, "castunitname") && ""
									.equals(getBillCardPanel().getBodyValueAt(
											iRow, "castunitname").toString()
											.trim())))) {

						throw new BusinessException(nc.ui.ml.NCLangRes
								.getInstance().getStrByID("4008bill",
										"UPP4008bill-000531")/*
																 * @res
																 * "非辅计量管理存货不支持扫描录录入按辅数量增加的条码"
																 */);

					}
				}

				scanUpdateLineSelect(barCodeParseVOs);
			}
		} catch (Exception e) {
			throw nc.ui.ic.pub.tools.GenMethod.handleException(null, null, e);
		}
	}
	
	
	
	public int scanfixline(BarCodeParseVO[] barCodeParseVOs, int iCurFixLine,
			int iNumUsed, boolean bAllforFix) throws Exception {

		if (barCodeParseVOs == null || barCodeParseVOs.length == 0
				|| iCurFixLine < 0) {
			return 0;
		}
		String sInvID = (String) getBillCardPanel().getBodyValueAt(iCurFixLine,
				IItemKey.INVID);
		if (sInvID == null || sInvID.length() == 0) {
			throw new Exception(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"4008bill", "UPP4008bill-000111")/* @res "请选择有存货数据的行！" */);
		}
		
		fillLineSpecailBarCode1VO(iCurFixLine);
		for(BarCodeParseVO barCodeParseVO:barCodeParseVOs)
			if (checkInvBarcodeRepeat(barCodeParseVO))
				return 0;
		
		// 填充数量
		int iNumforUse = scanfixline_fix(barCodeParseVOs, iCurFixLine,
				iNumUsed, bAllforFix); // 本次填充条码的数量

		// 保存条码
		getBarcodeCtrl().scanfixline_save(barCodeParseVOs, iCurFixLine,
				iNumUsed, iNumforUse, getM_voBill().getItemVOs(),true); // 本次填充条码的数量

		return iNumforUse;
	}
	
	
	private boolean checkInvBarcodeRepeat(BarCodeParseVO barcodevo) throws BusinessException {
		// if (1==1)
		// m_htbInvBarcode=new Hashtable();
		if (barcodevo == null)
			return false;
		String sBarcode = (String) barcodevo
				.getAttributeValue(BarcodeparseCtrl.VBARCODE);
		String sBarcodeSub = (String) barcodevo
				.getAttributeValue(BarcodeparseCtrl.VBARCODESUB);
		if (sBarcode != null
				&&barcodevo.getBsingletype().booleanValue()) {
			if (m_utfBarCode.getHtbInvBarcode().containsKey(sBarcode)) {
				throw new BusinessException(nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"4008bill", "UPP4008bill-000379")/*
															 * @res
															 * "扫描的条码是单件管理，有重复存货主条码"
															 */
						+ "'" + sBarcode + "'");
			}
		}
		if (sBarcodeSub != null && barcodevo.getBsingletypesub().booleanValue()) {
			if (m_utfBarCode.getHtbInvBarcodeSub().containsKey(sBarcodeSub)) {
				throw new BusinessException(nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"4008bill", "UPP4008bill-000380")/*
															 * @res
															 * "扫描的条码是单件管理，有重复存货次条码"
															 */
						+ "'" + sBarcodeSub + "'");
			}
		}
		return false;
	}
	
	/**
	 * 创建人：刘家清 创建时间：2008-7-11 上午09:21:43 创建原因： 加载对应的帐面条码。
	 * @param iCurFixLine
	 * @throws Exception
	 */
	private void fillLineSpecailBarCodeVO(int iCurFixLine) throws Exception{
		if ((null == getBillCardPanel().getBodyValueAt(iCurFixLine, "isloadaccountbc")
						|| !(Boolean)getBillCardPanel().getBodyValueAt(iCurFixLine, "isloadaccountbc"))){
			if (null != getBillCardPanel().getBodyValueAt(iCurFixLine,
					IItemKey.cspecialbid)){
				String bid = getBillCardPanel().getBodyValueAt(iCurFixLine,
						IItemKey.cspecialbid).toString();
				ArrayList<String> bids = new ArrayList<String>();
				bids.add(bid);
				HashMap<String,SpecailBarCodeVO[]> hsBarCode = SpecialHHelper.queryBarcodeByBID(bids);
				if (null != hsBarCode && 0 < hsBarCode.size())
					getM_voBill().getItemVOs()[iCurFixLine].setSpecailBarCodeVOs(hsBarCode.get(bid));
				getM_voBill().getItemVOs()[iCurFixLine].setIsloadaccountbc(UFBoolean.TRUE);
				getBillCardPanel().setBodyValueAt("Y", iCurFixLine, "isloadaccountbc");
				loadBarCodeBids.add(bid);
			}else
			{
				getM_voBill().getItemVOs()[iCurFixLine].setIsloadaccountbc(UFBoolean.TRUE);
				getBillCardPanel().setBodyValueAt("Y", iCurFixLine, "isloadaccountbc");
				
			}
			
		}
	}
	/**
	 * 创建人：刘家清 创建时间：2008-7-11 上午09:21:26 创建原因： 加载对应的盘点条码。
	 * @param iCurFixLine
	 * @throws Exception
	 */
	private void fillLineSpecailBarCode1VO(int iCurFixLine) throws Exception{
		if ((null == getBillCardPanel().getBodyValueAt(iCurFixLine, "isloadcountbc")
						||  !(Boolean)getBillCardPanel().getBodyValueAt(iCurFixLine, "isloadcountbc"))){
			if (null != getBillCardPanel().getBodyValueAt(iCurFixLine,
					IItemKey.cspecialbid)){
				String bid = getBillCardPanel().getBodyValueAt(iCurFixLine,
						IItemKey.cspecialbid).toString();
				ArrayList<String> bids = new ArrayList<String>();
				bids.add(bid);
				HashMap<String,SpecailBarCode1VO[]> hsBarCode1 = SpecialHHelper.queryBarcode1ByBID(bids);
				if (null != hsBarCode1 && 0 < hsBarCode1.size())
					getM_voBill().getItemVOs()[iCurFixLine].setSpecailBarCode1VOs(hsBarCode1.get(bid));
				getM_voBill().getItemVOs()[iCurFixLine].setIsloadcountbc(UFBoolean.TRUE);
				getBillCardPanel().setBodyValueAt("Y", iCurFixLine, "isloadcountbc");			
				loadBarCode1Bids.add(bid);
				if (m_utfBarCode != null && null != hsBarCode1 && 0 < hsBarCode1.size())
					m_utfBarCode.setAddeBarcode(hsBarCode1.get(bid));
			}else{
				getM_voBill().getItemVOs()[iCurFixLine].setIsloadcountbc(UFBoolean.TRUE);
				getBillCardPanel().setBodyValueAt("Y", iCurFixLine, "isloadcountbc");	
			}
			
		}
	}
	
	/**
	 * 创建人：刘家清 创建时间：2008-7-11 上午09:20:32 创建原因： 条形码编辑框选择不同存货时，加载对应的盘点条码。
	 * @param m_billItemVOs
	 * @param iCurLine
	 * @return
	 */
	public SpecailBarCode1VO[] fillLineSpecailBarCode1VO(IBillItemBarcodeVO m_billItemVO){
		try{
			if (null == m_billItemVO || null ==  m_billItemVO.getCgeneralbid())
				return null;
			int iCurFixLine = -1;
			for(int i = 0 ;i<getBillCardPanel().getRowCount();i++)
				if (null != getBillCardPanel().getBodyValueAt(i,IItemKey.cspecialbid)
						&& m_billItemVO.getCgeneralbid().equals(getBillCardPanel().getBodyValueAt(i,IItemKey.cspecialbid).toString()))
					iCurFixLine = i;
			if (-1 == iCurFixLine)
				return null;
			
			if ((null == getBillCardPanel().getBodyValueAt(iCurFixLine, "isloadcountbc")
							||  !(Boolean)getBillCardPanel().getBodyValueAt(iCurFixLine, "isloadcountbc"))){
				if (null != getBillCardPanel().getBodyValueAt(iCurFixLine,
						IItemKey.cspecialbid)){
					String bid = getBillCardPanel().getBodyValueAt(iCurFixLine,
							IItemKey.cspecialbid).toString();
					ArrayList<String> bids = new ArrayList<String>();
					bids.add(bid);
					HashMap<String,SpecailBarCode1VO[]> hsBarCode1 = SpecialHHelper.queryBarcode1ByBID(bids);
					if (null != hsBarCode1 && 0 < hsBarCode1.size())
						getM_voBill().getItemVOs()[iCurFixLine].setSpecailBarCode1VOs(hsBarCode1.get(bid));
					getM_voBill().getItemVOs()[iCurFixLine].setIsloadcountbc(UFBoolean.TRUE);
					getBillCardPanel().setBodyValueAt("Y", iCurFixLine, "isloadcountbc");			
					loadBarCode1Bids.add(bid);
					if (m_utfBarCode != null && null != hsBarCode1 && 0 < hsBarCode1.size())
						m_utfBarCode.setAddeBarcode(hsBarCode1.get(bid));
					return getM_voBill().getItemVOs()[iCurFixLine].getSpecailBarCode1VOs();
				}else{
					getM_voBill().getItemVOs()[iCurFixLine].setIsloadcountbc(UFBoolean.TRUE);
					getBillCardPanel().setBodyValueAt("Y", iCurFixLine, "isloadcountbc");	
				}
				
			}
		}catch (Exception e) {
			// TODO: handle exception
			GenMethod.handleException(this, null, e);
		}
		return null;
		
	}
	
	
	
	protected int scanfixline_fix(BarCodeParseVO[] barCodeParseVOs,
			int iCurFixLine, int iNumUsed, boolean bAllforFix) throws Exception {

		if (barCodeParseVOs == null || barCodeParseVOs.length == 0
				|| iCurFixLine < 0) {
			return 0;
		}

		// 根据"是否按辅单位增加数量"属性，如果是则辅数量自动加一；否则主数量自动加一。
		String m_sMyItemKey = null;
		String m_sMyShouldItemKey = null;

		if (((barCodeParseVOs[0].getAttributeValue(BarcodeparseCtrl.VBARCODE) != null
				&& barCodeParseVOs[0]
						.getAttributeValue(BarcodeparseCtrl.VBARCODESUB) == null && barCodeParseVOs[0]
				.getBasstaddflag().booleanValue())
				|| (barCodeParseVOs[0]
						.getAttributeValue(BarcodeparseCtrl.VBARCODE) == null
						&& barCodeParseVOs[0]
								.getAttributeValue(BarcodeparseCtrl.VBARCODESUB) != null && barCodeParseVOs[0]
						.getBasstaddflagsub().booleanValue()) || (barCodeParseVOs[0]
				.getAttributeValue(BarcodeparseCtrl.VBARCODE) != null
				&& barCodeParseVOs[0]
						.getAttributeValue(BarcodeparseCtrl.VBARCODESUB) != null && barCodeParseVOs[0]
				.getBasstaddflagsub().booleanValue()))
				&& getBillCardPanel().getBodyValueAt(iCurFixLine,
						"castunitname") != null) {
			m_sMyItemKey = "ncheckastnum";
			m_sMyShouldItemKey = "ncheckastnum";
		} else {
			m_sMyItemKey = "nchecknum";
			m_sMyShouldItemKey = "nchecknum";
		}

		int iNumforUse = 0; // 本次填充条码的数量
		if (getBillCardPanel().getBodyItem("cinventorycode") != null) {
			// 实际发数量
			UFDouble nFactNum = null;
			// 应发数量
			UFDouble nShouldNum = null;
			UFDouble nFactBarCodeNum = null; // 实际发，实际入条码数量

			nc.vo.ic.pub.bc.BarCodeVO[] oldBarcodevos = getM_voBill()
					.getItemVOs()[iCurFixLine].getBarCodeVOs();

			if (oldBarcodevos == null || oldBarcodevos.length == 0)
				nFactBarCodeNum = nc.vo.ic.pub.GenMethod.ZERO;
			else {
				nFactBarCodeNum = nc.vo.ic.pub.GenMethod.ZERO;
				for (int i = 0; i < oldBarcodevos.length; i++) {
					// 修改人：刘家清 修改日期：2007-11-5上午11:25:59 修改原因：已删除条码不能统计进来
					if (oldBarcodevos[i] != null
							&& oldBarcodevos[i].getNnumber() != null
							&& oldBarcodevos[i].getStatus() != nc.vo.pub.VOStatus.DELETED)
						nFactBarCodeNum = nFactBarCodeNum.add(oldBarcodevos[i]
								.getNnumber());
				}
			}

			// 实际发数量
			Object oNum = getBillCardPanel().getBodyValueAt(iCurFixLine,
					m_sMyItemKey);
			if (oNum == null || oNum.toString().trim().length() == 0) {
				nFactNum = null;
				// 如果没有应发数量，则填充全部数量
			} else
				nFactNum = (UFDouble) oNum;

			// 应发数量

			try {
				oNum = getBillCardPanel().getBodyValueAt(iCurFixLine,
						m_sMyShouldItemKey);
			} catch (Exception e) {
				oNum = null;
				nc.vo.scm.pub.SCMEnv.error(e);
			}
			if (oNum == null || oNum.toString().trim().length() == 0) {
				nShouldNum = null;
				// 如果没有应发数量，则填充全部数量
			} else
				nShouldNum = (UFDouble) oNum;

			boolean bNegative = false; // 是否负数
			if ((nFactNum != null && nFactNum.doubleValue() < 0)
					|| (nShouldNum != null && nShouldNum.doubleValue() < 0)) {
				bNegative = true;
			}

			// 分配条码数据到多个匹配行的算法
			iNumforUse = getBarcodeCtrl().scanfixlinenum(barCodeParseVOs,
					oldBarcodevos, iCurFixLine, iNumUsed, bAllforFix, nFactNum,
					nShouldNum);

			nFactBarCodeNum = nFactBarCodeNum.add(iNumforUse);

			// add by hanwei 2004-6-2
			// 条码数量大于应发数量,并且在盘点单生成的其他入出情况霞
			// 不能超过应发数量，这样修改的实发数量等于应发数量
			if (nShouldNum != null && nFactBarCodeNum != null
					&& nFactBarCodeNum.doubleValue() > nShouldNum.doubleValue()
					&& !getBarcodeCtrl().isOverShouldNum()) {
				nFactBarCodeNum = nShouldNum.abs();
			}

			if (nFactNum == null)
				nFactNum = nc.vo.ic.pub.GenMethod.ZERO;
			// 修改人：刘家清 修改日期：2007-9-10下午02:15:22
			// 修改原因：对于单条码管理，并且不保存条码的，每次按条码数量是更新数量
			if ((barCodeParseVOs[0]
					.getAttributeValue(BarcodeparseCtrl.VBARCODE) != null)
					&& (barCodeParseVOs[0]
							.getAttributeValue(BarcodeparseCtrl.VBARCODESUB) == null)
					&& (m_voBill.getItemVOs()[iCurFixLine]
							.getIsprimarybarcode().booleanValue())
					&& (!m_voBill.getItemVOs()[iCurFixLine]
							.getIssecondarybarcode().booleanValue())
					&& !barCodeParseVOs[0].getBsavebarcode().booleanValue()) {

				// 同步实发数量
				if (bNegative )
					nFactNum = nFactNum.sub(iNumforUse);
				else
					nFactNum = nFactNum.add(iNumforUse);

				getBillCardPanel().setBodyValueAt(nFactNum, iCurFixLine,
						m_sMyItemKey);

				execEditFormula(iCurFixLine, m_sMyItemKey);
				// 应发数量不用同步
				nc.ui.pub.bill.BillEditEvent event1 = new nc.ui.pub.bill.BillEditEvent(
						getBillCardPanel().getBodyItem(m_sMyItemKey), nFactNum,
						m_sMyItemKey, iCurFixLine);
				// 检查数量编辑业务逻辑
				//scanCheckNumEdit(event1);
				afterEdit(event1);
				// 执行模版公式
				/*getGenBillUICtl().execEditFormula(getBillCardPanel(),
						iCurFixLine, m_sMyItemKey);*/

				// 触发单据行状态为修改
				if (getBillCardPanel().getBodyValueAt(iCurFixLine,
						IItemKey.cspecialbid) != null)
					getBillCardPanel().getBillModel().setRowState(iCurFixLine,
							BillMode.Update);

			} else {

				// 实发数量小于条码数量，才去修改界面上的实发数量
				if (nFactNum.doubleValue() < nFactBarCodeNum.doubleValue()
						&& !((barCodeParseVOs[0]
								.getAttributeValue(BarcodeparseCtrl.VBARCODE) != null)
								&& (barCodeParseVOs[0]
										.getAttributeValue(BarcodeparseCtrl.VBARCODESUB) == null)
								&& (m_voBill.getItemVOs()[iCurFixLine]
										.getIsprimarybarcode().booleanValue()) && (m_voBill
								.getItemVOs()[iCurFixLine]
								.getIssecondarybarcode().booleanValue()))) {

					// 同步实发数量
					if (bNegative )
						nFactBarCodeNum = nFactBarCodeNum.multiply(nc.vo.ic.pub.GenMethod.NEGONE_UFDOUBLE);

					getBillCardPanel().setBodyValueAt(nFactBarCodeNum,
							iCurFixLine, m_sMyItemKey);

					execEditFormula(iCurFixLine, m_sMyItemKey);
					// 应发数量不用同步
					nc.ui.pub.bill.BillEditEvent event1 = new nc.ui.pub.bill.BillEditEvent(
							getBillCardPanel().getBodyItem(m_sMyItemKey),
							nFactBarCodeNum, m_sMyItemKey, iCurFixLine);
					// 检查数量编辑业务逻辑
					//scanCheckNumEdit(event1);
					afterEdit(event1);
					// 执行模版公式
					/*getGenBillUICtl().execEditFormula(getBillCardPanel(),
							iCurFixLine, m_sMyItemKey);*/

					// 触发单据行状态为修改
					if (getBillCardPanel().getBodyValueAt(iCurFixLine,
							IItemKey.cspecialbid) != null)
						getBillCardPanel().getBillModel().setRowState(
								iCurFixLine, BillMode.Update);
				}
			}

		}

		return iNumforUse;
	}
	
	protected void scanUpdateLine(BarCodeParseVO[] barCodeParseVOs,
			ArrayList alFixRowNO) throws Exception {

		if (barCodeParseVOs == null || barCodeParseVOs.length == 0
				|| alFixRowNO == null || alFixRowNO.size() == 0) {
			return;
		}

		int iNumAll = barCodeParseVOs.length;
		int iNumUsed = 0; // 累计统计行数
		int ifixRows = alFixRowNO.size();
		int iCurFixLine = 0; // 更新当前行
		int ifixSingleLineNum = 0;

		for (int i = 0; i < ifixRows; i++) {
			iCurFixLine = Integer.parseInt((String) alFixRowNO.get(i));

			if (ifixRows == 1) {
				// 只有一行，全部填充当前行
				ifixSingleLineNum = scanfixline(barCodeParseVOs, iCurFixLine,
						0, true);
				break;
			} else {
				if (i == ifixRows - 1) // 最后一行，填充所有的数量
				{
					ifixSingleLineNum = scanfixline(barCodeParseVOs,
							iCurFixLine, iNumUsed, true);
					break;
				} else // 中间行填充应发的数量
				{
					ifixSingleLineNum = scanfixline(barCodeParseVOs,
							iCurFixLine, iNumUsed, false);
				}
				iNumUsed = iNumUsed + ifixSingleLineNum;
				if (iNumUsed == iNumAll) {
					// 填充完毕了
					break;
				}
			}

		}
	}
	
	protected void scanUpdateLineSelect(BarCodeParseVO[] barCodeParseVOs)
	throws Exception {
		// 取当前行
		int iCurFixLine = 0;
		int rowNow = getBillCardPanel().getBillTable().getSelectedRow();
		if (rowNow < 0) {
			// 提示错误信息
			throw new nc.vo.pub.BusinessException(nc.ui.ml.NCLangRes
					.getInstance().getStrByID("4008bill", "UPP4008bill-000112")/*
																				 * @res
																				 * "请选择对应条码的存货行！"
																				 */);
		} else {
			iCurFixLine = rowNow;
		}
		boolean bAllforFix = true;
		int iNumUsed = 0;
		
		if (getM_voBill() != null && getM_voBill().getItemVOs() != null
				&& getM_voBill().getItemVOs().length > iCurFixLine
				&& getM_voBill().getItemVOs()[iCurFixLine] != null) {
			if (getM_voBill().getItemVOs()[iCurFixLine].getBarcodeManagerflag()
					.booleanValue()
					&& getM_voBill().getItemVOs()[iCurFixLine]
							.getBarcodeClose().booleanValue() == false) {
				scanfixline(barCodeParseVOs, iCurFixLine, iNumUsed, bAllforFix);
			} else {
				throw new nc.vo.pub.BusinessException(nc.ui.ml.NCLangRes
						.getInstance().getStrByID("4008bill",
								"UPP4008bill-000113")/*
														 * @res
														 * "当前行非条码管理或者行条码已关闭！！"
														 */);
			}
		}
}

	public void scanAddBoxline(BarCodeGroupVO vo) throws Exception {
		// TODO 自动生成方法存根
		
	}
	
	protected void switchListToBill() {
		//加入从刚才的列表中选择的数据
		super.switchListToBill();
		m_layoutManager.show(true);
	}
	
	protected void switchBillToList() {
		super.switchBillToList();
		m_layoutManager.show(false);
	}
	
//	protected SpecialBillVO getM_voBill() {
//		return m_voBill;
//	}
  
	public static final String[] sbillVOName = new String[] { "invcode",
		"vbatchcode", "castunitcode", "hsl", "cvendorcode", "vfree1",
		"vfree2", "vfree3", "vfree4", "vfree5", "vbarcode", "nnum" };
	
	/** 对应Excel表的列 */
	public final String[] sbillCaption = new String[] {
			nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
					"UC000-0001480")/* @res "存货编码" */,
			nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
					"UC000-0002060")/* @res "批次号" */,
			nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
					"UC000-0003938")/* @res "辅计量单位" */,
			nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
					"UC000-0002161")/* @res "换算率" */,
			nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
					"UC000-0000275")/* @res "供应商" */,
			nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
					"UC000-0003330")/* @res "自由项1" */,
			nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
					"UC000-0003333")/* @res "自由项2" */,
			nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
					"UC000-0003335")/* @res "自由项3" */,
			nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
					"UC000-0003337")/* @res "自由项4" */,
			nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
					"UC000-0003339")/* @res "自由项5" */,
			nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
					"UC000-0002733")/* @res "条形码" */,
			nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
					"UC000-0002282") /* @res "数量" */

	};
	
	private HSSFWorkbook wb = null;// Excel文件对象

	private HSSFSheet[] sheet = null;// sheet数组,代表页们
	private void exportVOToExcel(BarCodeParseVO[] barCodeParseVOs,
			String fileName) {
		// TODO 自动生成方法存根
		try {
			// 修改人：刘家清 修改日期：2007-9-4下午01:23:55
			// 修改原因：解决每次导出时，如果后面导出的packbillItemVOs比前面的少时，会多导出。
			wb = null;
			boolean fileExists = false;
			java.io.File f = new java.io.File(fileName);

			// TODO Liujq 修改成是否替换
			if (!f.exists()) {
				f.createNewFile();
			} else {
				fileExists = true;
			}
			FileOutputStream fileOut = new FileOutputStream(fileName, false);

			if (wb == null) {
				wb = new HSSFWorkbook();
				sheet = new HSSFSheet[1];
				sheet[0] = wb.createSheet(nc.ui.ml.NCLangRes.getInstance()
						.getStrByID("4008bill", "UPP4008bill-000418")/*
																		 * @res
																		 * "条形码模拟解析"
																		 */);
			}

			createExcelCaption(sheet[0]);
			if (fileExists)
				clearRestLines(1, 1000);

			if (barCodeParseVOs != null && barCodeParseVOs.length > 0) {

				for (int i = 0; i < barCodeParseVOs.length; i++) {
					setVOAtLine(barCodeParseVOs[i], i + 1);
				}
			}

			// Write the output to a file
			wb.write(fileOut);
			fileOut.close();

			showWarningMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"4008bill", "UPP4008bill-000120")/* @res "导出完成" */);

		} catch (Exception e) {
			nc.ui.ic.pub.tools.GenMethod.showErro(this, e.getMessage());
		}
	}

	private void createExcelCaption(HSSFSheet sheet) {
		if (sheet == null)
			return;
		HSSFRow row = sheet.getRow(0);
		if (row == null)
			row = sheet.createRow(0);

		for (short i = 0; i < sbillCaption.length; i++) {
			HSSFCell cell = row.getCell(i);
			if (cell == null)
				cell = row.createCell(i);
			cell.setCellType(HSSFCell.CELL_TYPE_STRING);
			cell.setCellValue(sbillCaption[i]);

		}

	}
	
	
	private void clearRestLines(int len, int lenAll) {
		if (len >= lenAll)
			return;

		for (int i = len; i <= lenAll; i++) {
			HSSFRow row = sheet[0].getRow(i);
			if (row == null)
				continue;
			for (short j = 0; j < sbillVOName.length; j++) {
				HSSFCell cell = row.getCell(j);
				if (cell == null)
					continue;
				cell.setCellValue("");

			}

		}

	}
	
	
	public void setVOAtLine(BarCodeParseVO barCodeParseVO, int line) {
		try {
			if (line <= 0)
				return;
			if (barCodeParseVO == null)
				return;

			HSSFRow row = sheet[0].getRow(line);
			if (row == null)
				row = sheet[0].createRow((short) line);

			for (short i = 0; i < sbillVOName.length; i++) {
				HSSFCell cell = row.getCell(i);
				if (cell == null)
					cell = row.createCell(i);
				// 如果之前文件为数字，下面的语句会抛异常，所以需要屏蔽
				// cell.setCellType(HSSFCell.CELL_TYPE_STRING);

				String strCellValue = null;
				if (barCodeParseVO.getAttributeValue(sbillVOName[i]) != null)
					strCellValue = barCodeParseVO.getAttributeValue(
							sbillVOName[i]).toString();

				if (strCellValue == null)
					cell.setCellValue("");
				else
					cell.setCellValue(strCellValue);

			}

		} catch (Exception e) {
			nc.ui.ic.pub.tools.GenMethod.showErro(this, e.getMessage());
		}
	}
	
	
	public void onDeleteRow() { //finished
		//switch (nc.ui.pub.beans.MessageDialog.showOkCancelDlg(this, "提示", "你确定删除吗？")) {
		//case nc.ui.pub.beans.MessageDialog.ID_OK :
		
		int[] selrow = getBillCardPanel().getBillTable().getSelectedRows();
		int length = selrow.length;
		SCMEnv.out("count is " + length);

		// 删除条码数据 add by hanwei
		if (getM_voBill() != null && selrow != null) {
			for (int i = 0; i < selrow.length; i++) {
				if (getM_voBill().getItemVOs()[selrow[i]].getBarCodeVOs() != null) {
					m_utfBarCode.setRemoveBarcode(getM_voBill().getItemVOs()[selrow[i]].getBarCodeVOs());
				}
			}
		}

		super.onDeleteRow();
	}

	public ArrayList<Integer> getM_bcRuleArrLen() {
		if (null == m_bcRuleArrLen)
			m_bcRuleArrLen = BarCodeRuleCache.getArrLen();
		return m_bcRuleArrLen;
	}
	
	// addied by liuzy 2009-9-4 下午03:19:43 连接数问题，向界面置VO不需要执行公式，翻页时已经执行过了
	protected void setBillValueVO(SpecialBillVO bvo) {
	  setBillValueVO(bvo,false);
	  for(int i = 1; i <= 20; i++){
	    String key = "vuserdef"+i;
	    nc.ui.pub.bill.BillItem item = getBillCardPanel().getHeadItem(key);
	    if(item != null && item.getDataType() == nc.ui.pub.bill.BillItem.USERDEF){
	       String pk = null;
	       pk = (String)bvo.getHeaderValue("pk_defdoc"+i);
	      if(pk!=null && pk.length()>0)
	      ((UIRefPane)item.getComponent()).setPK(bvo.getHeaderValue("pk_defdoc"+i));
	    }
	  } 
	}
	
	/**
	 * 根据盘点的信息，生成码单明细
	 * @author Ouyang 2012-04-25 
	 * @
	 * @param m_voBill
	 */
	public void createMDcrk(SpecialBillVO m_voBill){
		IVOPersistence ivopersistence = (IVOPersistence) NCLocator.getInstance().lookup(IVOPersistence.class.getName());
		IUAPQueryBS querybs = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
		if(m_voBill==null)
			return;
		
		SpecialBillItemVO[] bvos = m_voBill.getItemVOs();
		if(bvos ==null||bvos.length<=0)
			return;
		
		ArrayList<Object[]> invs = new ArrayList<Object[]>();
		
		HashMap<String, String[]> keyMap = new HashMap<String, String[]>();
		
		String sBillPK = m_voBill.getPrimaryKey();
		
		/**构建码单明细*/
		IMDTools tool = (IMDTools) NCLocator.getInstance().lookup(IMDTools.class.getName());
		/**1、根据来源单据主键，获取所生成的的*/
		String sql = "select b.cbodybilltypecode,b.csourcebillbid,b.cgeneralbid,h.vbillcode from ic_general_b b inner join ic_general_h h on b.cgeneralhid = h.cgeneralhid where nvl(b.dr,0)=0 and nvl(h.dr,0)=0 and b.csourcebillhid='"+sBillPK+"' ";
		try {
			invs = (ArrayList<Object[]>) querybs.executeQuery(sql,new ArrayListProcessor());
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (invs != null && invs.size() > 0) {
			for (int y = 0; y < invs.size(); y++) {
				String cbodybilltypecode = invs.get(y)[0] == null ? null : invs
						.get(y)[0].toString();
				String csourcebillbid = invs.get(y)[1] == null ? null : invs
						.get(y)[1].toString();
				String cgeneralbid = invs.get(y)[2] == null ? null : invs
						.get(y)[2].toString();
				String vbillcode = invs.get(y)[3] == null ? null : invs
						.get(y)[3].toString();
				String key = cbodybilltypecode+csourcebillbid;
				keyMap.put(key, new String[]{cgeneralbid,vbillcode});
			}
				
			}
		for (int i = 0; i < bvos.length; i++) {
			try {
				
				MdxclBVO mdxclvo=null;
				UFDouble Srkzl = UFDouble.ZERO_DBL;
				
				/** 2、码单出库明细 */
				if (keyMap.containsKey("4I" + bvos[i].getPrimaryKey())) {
					MdcrkVO mdoutvo = new MdcrkVO();
					if (keyMap.containsKey("4I" + bvos[i].getPrimaryKey())) {
						//查出当前现存量
						String mdxcl = "select * from nc_mdxcl_b b where b.cspaceid='"+ bvos[i].getCspaceid()
						+ "' and b.jbh='"
						+ bvos[i].getVuserdef1()
						+ "' and isnull(b.dr,0)=0";
						 mdxclvo = (MdxclBVO) querybs.executeQuery(
						mdxcl, new BeanProcessor(MdxclBVO.class));
						
						/**构建码单出库明细vo*/
						
						mdoutvo.setCbodybilltypecode("4I");
						mdoutvo.setCgeneralbid(keyMap.get("4I"
								+ bvos[i].getPrimaryKey())[0]);
						mdoutvo.setDef10(keyMap
										.get("4I" + bvos[i].getPrimaryKey())[1]);
						mdoutvo.setCspaceid(mdxclvo.getCspaceid());
						mdoutvo.setJbh(mdxclvo.getJbh());
						mdoutvo.setMd_width(mdxclvo.getMd_width());
						mdoutvo.setMd_length(mdxclvo.getMd_length());
						mdoutvo.setMd_lph(mdxclvo.getMd_lph());
						mdoutvo.setMd_meter(mdxclvo.getMd_meter());
						mdoutvo.setMd_note(mdxclvo.getMd_note());
						mdoutvo.setMd_zlzsh(mdxclvo.getMd_zlzsh());
						mdoutvo.setMd_zyh(mdxclvo.getMd_zyh());
						mdoutvo.setDef6(mdxclvo.getDef6());
						mdoutvo.setRemark(mdxclvo.getRemark());// 备注
						mdoutvo.setSrkzl(mdxclvo.getZhongliang());// 验收重量
						mdoutvo.setSrkzs(bvos[i].getNaccountastnum());
						mdoutvo.setDr(0);
						mdoutvo.setDef1(mdxclvo.getDef1());// 钢厂重量
						mdoutvo.setDef2(mdxclvo.getDef2());// 自定义项2
						mdoutvo.setDef3(mdxclvo.getDef3());// 自定义项3
						mdoutvo.setDef4(mdxclvo.getDef4());// 非计算标识
						mdoutvo.setDef7(mdxclvo.getDef7());// 自定义项7
						mdoutvo.setDef8(mdxclvo.getDef8());// 自定义项8
						mdoutvo.setDef9(mdxclvo.getDef9());// 自定义项9
						mdoutvo.setDef10(mdxclvo.getDef10());// 自定义项10
						mdoutvo.setDef11(mdxclvo.getDef11());// 自定义项11
						mdoutvo.setDef12(mdxclvo.getDef12());// 自定义项12
						mdoutvo.setDef13(mdxclvo.getDef13());// 自定义项13
						mdoutvo.setDef14(mdxclvo.getDef14());// 自定义项14
						mdoutvo.setDef15(mdxclvo.getDef15());// 自定义项15
						mdoutvo.setSfbj(UFBoolean.FALSE);
						mdoutvo.setSfgczl(UFBoolean.FALSE);
						Srkzl = mdoutvo.getSrkzl();
						
						
						mdoutvo.setPk_mdxcl_b(mdxclvo.getPk_mdxcl_b());
						mdoutvo.setStatus(VOStatus.NEW);
						//新增码单出库记录
						ivopersistence.insertVO(mdoutvo);
						
						/**更新码单现存量*/
						mdxclvo.setZhishu(mdxclvo.getZhishu().sub(
								mdoutvo.getSrkzs()));
						mdxclvo.setZhongliang(mdxclvo.getZhongliang().sub(
								mdoutvo.getSrkzl()));
						mdxclvo.setDef1(mdxclvo.getDef1()
								.sub(mdoutvo.getDef1()));
						ivopersistence.updateVO(mdxclvo);
					}

				}

				/** 3、码单入库明细 */
				if (keyMap.containsKey("4A" + bvos[i].getPrimaryKey())) {
					MdcrkVO mdcrkVO = new MdcrkVO();
					mdcrkVO.setDef1(bvos[i].getNchecknum());
//					mdcrkVO.setSrkzl(bvos[i].getNchecknum());
					mdcrkVO.setSrkzs(bvos[i].getNcheckastnum());
					mdcrkVO.setCbodybilltypecode("4A");
					mdcrkVO.setCgeneralbid(keyMap.get("4A"
							+ bvos[i].getPrimaryKey())[0]);
					mdcrkVO.setDef10(keyMap
									.get("4A" + bvos[i].getPrimaryKey())[1]);
					mdcrkVO.setJbh(bvos[i].getVuserdef1());
					mdcrkVO.setCspaceid(bvos[i].getVuserdef8());
					mdcrkVO.setCwarehouseidb(m_voBill.getHeaderVO()
							.getCoutwarehouseid());
					mdcrkVO.setCcalbodyidb(m_voBill.getHeaderVO()
							.getPk_calbody_out());
					mdcrkVO.setPk_corp(m_voBill.getHeaderVO().getPk_corp());
					mdcrkVO.setDef7(bvos[i].getVuserdef5());
					mdcrkVO.setDef8(bvos[i].getVuserdef6());
					mdcrkVO.setDef9(bvos[i].getVuserdef7());
					mdcrkVO.setVoperatorid(m_voBill.getHeaderVO()
							.getCoperatorid());
					
					/*
					 * 如果为改货位的，则取未改之前的现存量的钢厂重量，
					 * 如果mdxclvo 为空，则表示是新增的，直接取盘点数量
					 */
					if(mdxclvo != null){
						mdcrkVO.setSrkzl(Srkzl);
					}else{
						mdcrkVO.setSrkzl(bvos[i].getNchecknum());
					}
					
					String pk_mdxcl_b = null;
					String mdxcl = "select * from nc_mdxcl_b b where b.cspaceid='"
							+ mdcrkVO.getCspaceid()
							+ "' and b.jbh='"
							+ mdcrkVO.getJbh() + "' and isnull(b.dr,0)=0";
					MdxclBVO mdxclvotem;
					mdxclvotem = (MdxclBVO) querybs.executeQuery(mdxcl,
							new BeanProcessor(MdxclBVO.class));

					if (mdxclvotem == null || "".equals(mdxclvotem)) {
						String pk_mdxclsql = "select pk_mdxcl from  nc_mdxcl where isnull(dr,0)=0 and  pk_corp ='"
								+ mdcrkVO.getPk_corp()
								+ "' and ccalbodyidb='"
								+ mdcrkVO.getCcalbodyidb()
								+ "' and cwarehouseidb ='"
								+ mdcrkVO.getCwarehouseidb()
								+ "'  and cinventoryidb='"
								+ bvos[i].getCinventoryid() + "' ";
						Object pk_mdxcl = querybs.executeQuery(pk_mdxclsql,
								new ColumnProcessor());
						
						/**如果现存量表头为空，则构建一条现存量*/
						if(pk_mdxcl==null||pk_mdxcl.equals("")){
							MdxclVO hvo = new MdxclVO();
							hvo.setCcalbodyidb(mdcrkVO.getCcalbodyidb());
							hvo.setCwarehouseidb(mdcrkVO.getCwarehouseidb());
							hvo.setCinventoryidb(bvos[i].getCinventoryid());
							hvo.setPk_corp(mdcrkVO.getPk_corp());
							hvo.setDr(0);
							pk_mdxcl =  ivopersistence.insertVO(hvo);
						}
						
						MdxclBVO bvo = new MdxclBVO();
						bvo.setCspaceid(mdcrkVO.getCspaceid());
						bvo.setJbh(mdcrkVO.getJbh());
						bvo.setMd_width(mdcrkVO.getMd_width());
						bvo.setMd_length(mdcrkVO.getMd_length());
						bvo.setMd_lph(mdcrkVO.getMd_lph());
						bvo.setMd_meter(mdcrkVO.getMd_meter());
						bvo.setMd_note(mdcrkVO.getMd_note());
						bvo.setMd_zlzsh(mdcrkVO.getMd_zlzsh());
						bvo.setMd_zyh(mdcrkVO.getMd_zyh());
						bvo.setDef6(mdcrkVO.getDef6());
						bvo.setRemark(mdcrkVO.getRemark());// 备注
						bvo.setZhongliang(mdcrkVO.getSrkzl());// 验收重量
						bvo.setZhishu(mdcrkVO.getSrkzs());
						bvo.setDr(0);
						bvo.setDef1(mdcrkVO.getDef1());// 钢厂重量
						bvo.setDef2(mdcrkVO.getDef2());// 自定义项2
						bvo.setDef3(mdcrkVO.getDef3());// 自定义项3
						bvo.setDef4(mdcrkVO.getDef4());// 非计算标识
						bvo.setDef7(mdcrkVO.getDef7());// 自定义项7
						bvo.setDef8(mdcrkVO.getDef8());// 自定义项8
						bvo.setDef9(mdcrkVO.getDef9());// 自定义项9
						bvo.setDef10(mdcrkVO.getDef10());// 自定义项10
						bvo.setDef11(mdcrkVO.getDef11());// 自定义项11
						bvo.setDef12(mdcrkVO.getDef12());// 自定义项12
						bvo.setDef13(mdcrkVO.getDef13());// 自定义项13
						bvo.setDef14(mdcrkVO.getDef14());// 自定义项14
						bvo.setDef15(mdcrkVO.getDef15());// 自定义项15
						bvo.setPk_mdxcl(pk_mdxcl.toString());

						bvo.setStatus(VOStatus.NEW);
						pk_mdxcl_b = ivopersistence.insertVO(bvo);
					} else {
						pk_mdxcl_b = mdxclvotem.getPk_mdxcl_b();
						mdxclvotem.setZhishu(mdxclvotem.getZhishu().add(
								mdcrkVO.getSrkzs()));
						mdxclvotem.setZhongliang(mdxclvotem.getZhongliang()
								.add(mdcrkVO.getSrkzl()));
						mdxclvotem.setDef1(mdxclvotem.getDef1()
								.add(mdcrkVO.getDef1()));//mdxclvo.getZhongliang().add(mdcrkVO.getSrkzl())
						//add by ouyangzhb 2012-05-03 把现存量的其他字段的值也改成调整后的数据
						mdxclvotem.setMd_width(mdcrkVO.getMd_width());
						mdxclvotem.setMd_length(mdcrkVO.getMd_length());
						mdxclvotem.setMd_meter(mdcrkVO.getMd_meter());
						mdxclvotem.setDef7(bvos[i].getVuserdef5());
						mdxclvotem.setDef8(bvos[i].getVuserdef6());
						mdxclvotem.setDef9(bvos[i].getVuserdef7());
						ivopersistence.updateVO(mdxclvotem);
					}
					mdcrkVO.setDr(0);
					mdcrkVO.setStatus(VOStatus.NEW);
					mdcrkVO.setPk_mdxcl_b(pk_mdxcl_b);
					ivopersistence.insertVO(mdcrkVO);
				}

			} catch (BusinessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
}