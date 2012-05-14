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
 * �����ߣ������� �������ڣ�2001-04-20 ���ܣ��̵㵥���� �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
 */
public class ClientUI extends SpecialBillBaseUI implements
		nc.ui.pf.query.ICheckRetVO,nc.ui.ic.pub.barcode.BarCodeInputListenerNew {

	// ����ȡ��
	private ButtonObject m_boGetAccNumber = null;
	
	private ButtonObject m_boGetBCAccNumber = null;

	private UFDouble UFD_ZERO = new UFDouble(0);

	// �̵�ѡ��
	private ButtonObject m_boCheckChoose = null;

	// ����
	private ButtonObject m_boAdjustGoods = null;

	// ȡ������
	private ButtonObject m_boCancelAdjust = null;

	public int m_iBqrybalrec = 0;// ��¼��ѯ��ʵ�����ѯ���е�ѡ��

	private DataGetSource ivjDataGetSourceDlg = null;

	private ButtonObject m_addMng = null;

	// ����
	private ButtonObject m_adjustMng = null;

	// �����״̬,����������ȡ��״̬
	private int iSaveFlag = 0;

	// ��������
	private ButtonObject m_assistMng = null;

	// �����˵�
	// ����
	private ButtonObject m_auditMng = null;

	boolean m_bBillInit = false; // �����Ƿ��ǳ�ʼ״̬ add by hanwei 2003-12-30

	// ��ǰ״̬�Ƿ�ʵ�̵�¼��
	private boolean m_bcheckInput = false;

	private boolean m_bIsByFormula = true;

	// �Ƿ��е������
	private boolean m_bIsImportData = false;

	// ����״̬��ѯ
	private ButtonObject m_boAuditState = null;

	// �������
	private ButtonObject m_boCalculate = null;

	// �̵��֧��
	private ButtonObject m_boCheckInv = null;

	// ʵ��¼��
	private ButtonObject m_boCheckNum = null;

	// �Զ���дʵ����
	private ButtonObject m_boFillNum = null;

	private nc.ui.ic.pub.device.DevInputCtrl m_dictrl = null;

	private CheckTypeChooser m_dlgCtc = null; // ���ѡ��

	private ButtonObject m_fillMng = null;

	// ����ʵ������
	private ButtonObject m_boCountBCImport = null;

	// ���������ϸ
	private ButtonObject m_boViewOnhandBC = null;

	// ʵ��������ϸ
	private ButtonObject m_boViewCountBC = null;

	// �����̵������ϸ
	private ButtonObject m_boViewDiffBC = null;

	// ������
	private ButtonObject m_boBarCode = null;

	// ��λ����
	private LocatorRefPane m_refLocator = null;
	
	private ButtonObject m_boCommit = null;

	private Timer m_timer = new Timer();
	
	private nc.ui.ic.pub.barcode.UIBarCodeTextFieldNew m_utfBarCode = null;
	
	protected UIPanel m_pnlBarCode;
	
	private ToftLayoutManager m_layoutManager = new ToftLayoutManager(this);// ���ֹ�����
	
	
	private ArrayList<String> loadBarCodeBids = new ArrayList<String>();
	private ArrayList<String> loadBarCode1Bids = new ArrayList<String>();

	/**
	 * ClientUI ������ע�⡣
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
		 * ��ʼ��ʱ������Ӹ����ؼ������Ƕ�̬��ӣ��������ˢ�µĸ��Ӷ�
		 */
		private void init() {
			//��ʼ������
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

		
		//��Card���棺CardPanel+����panel
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
	 * �˴����뷽��˵���� �������ڣ�(2001-3-23 2:02:27)
	 * 
	 * @param e
	 *            ufbill.BillEditEvent
	 */
	String[] mdabjuct = {"","","",""};
	
	public void afterEdit(nc.ui.pub.bill.BillEditEvent e) {
		// 1.������յ�ID��Name�Լ�Value��m_voBill
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
						// ����ʾ��
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
						// ����ʾ��
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

		// 2.��ͷ�ֿ�༭����
		int firstRow = rownum;
		if (strColName.equals("coutwarehouseid")) { // ����ֿ�
			afterWhOutEdit(true);

		} else if (e.getKey().equals("coutbsor")) { // ���ֿ�
			afterBsorEdit(new String[] { "coutbsor", "coutbsorname" },
					new String[] { "coutdeptid", "coutdeptname" });
		} else if (BillMode.Browse != m_iMode
				&& e.getKey().startsWith("vuserdef")) {// �Զ������zhy
			super.afterDefEdit(e);
		}

		if (firstRow == -1) {
			return;
		}
		// 3.�������
		int column = getBillCardPanel().getBillModel().getBodyColByKey(
				strColName);
		int listcolumn = getBillCardPanel().getBillTable()
				.convertColumnIndexToView(column);
		// �������ı�
		if (strColName.equals("cinventorycode")) {
			afterInvMutiEdit(e);
		} else if (strColName.equals("vfree0")) {
			afterFreeItemEdit(e);
		} else if (strColName.equals("vbatchcode")) {
			afterLotEdit(strColName, rownum);
		} else if (strColName.equals("castunitname")) {
			afterAstUOMEdit(rownum);
		}
		// �������������츨������ ���������޸ģ����ݲ��������ı༭��ʽ�Զ��޸��̵����������ݿ��Լ�UAP����ʵ�֣���ͬʱ���ݹ�ʽ�Զ��޸ĵ���������
		// ��ʽ��UAP�Զ����ã����ﴦ���뻻������صĴ��롣
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
		// �̵������Լ��̵㸨�������༭�����
		else if (strColName.equals("nchecknum")) {
			calHslNumAstNum(rownum, "nchecknum", "ncheckastnum", 0);
			calHslNumAstNum(rownum, "nadjustnum", "nadjustastnum", 0);
			calHslNumAstNum(rownum, "cysl", "cyfsl", 0);
			execEditFormula(rownum, "nadjustnum");// zhy2005-11-04�̵������仯ʱ�������������ı䣬ͬʱ��Ҫ�����������Ͳ����ʸı�
			// �޸��ˣ������� �޸����ڣ�2007-9-12����03:43:42 �޸�ԭ��
			getBillCardPanel().getBillModel().execEditFormulaByKey(rownum,
					"ncheckastnum");
		} else if (strColName.equals("ncheckastnum")) {
			calHslNumAstNum(rownum, "nchecknum", "ncheckastnum", 1);
			getBillCardPanel().getBillModel().execEditFormulaByKey(rownum,
					"nchecknum");
			execEditFormula(rownum, "nadjustnum");// zhy2005-11-04ͬ��
		} else if (strColName.equals("ncheckgrsnum")) {
			m_voBill.setItemValue(rownum, strColName, e.getValue());
			m_voBill.setItemValue(rownum, "nadjustgrsnum", getBillCardPanel()
					.getBodyValueAt(rownum, "nadjustgrsnum"));
		}
		// ���������Լ������������༭�����
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
			// ͬ����VO
			m_voBill.setItemValue(rownum, m_sBillRowNo, getBillCardPanel()
					.getBodyValueAt(rownum, m_sBillRowNo));
		}

		// ��������
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
		// ʧЧ����
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
		
		//add by ouyangzhb 2012-04-25  �뵥�̵㴦��
		BillItem mditembz = getBillCardPanel().getHeadItem("vuserdef1");
		if(mditembz !=null ){
			String ismd = mditembz.getValue();
			if(ismd !=null&&new UFBoolean(ismd).booleanValue()){
				if("vuserdef5,vuserdef6,vuserdef7,vuserdef8,nchecknum,ncheckastnum,ncheckgrsnum,pspacename".contains(strColName)){
					//�¶���һ����ʶ�����һ�����������д��ڲ���ȵģ�����Ϊ���޸Ĺ��ģ�ȡֵΪtrue
					String vuserdef2 = (String) getBillCardPanel().getBodyValueAt(e.getRow(), "vuserdef2") == null ? "":(String) getBillCardPanel().getBodyValueAt(e.getRow(), "vuserdef2");//���ճ�
					String vuserdef3 = (String) getBillCardPanel().getBodyValueAt(e.getRow(), "vuserdef3") == null ? "":(String) getBillCardPanel().getBodyValueAt(e.getRow(), "vuserdef3");//���տ�
					String vuserdef4 = (String) getBillCardPanel().getBodyValueAt(e.getRow(), "vuserdef4") == null ? "":(String) getBillCardPanel().getBodyValueAt(e.getRow(), "vuserdef4");//��������
					String vuserdef5 = (String) getBillCardPanel().getBodyValueAt(e.getRow(), "vuserdef5") == null ? "":(String) getBillCardPanel().getBodyValueAt(e.getRow(), "vuserdef5");//�̵㳤
					String vuserdef6 = (String) getBillCardPanel().getBodyValueAt(e.getRow(), "vuserdef6") == null ? "":(String) getBillCardPanel().getBodyValueAt(e.getRow(), "vuserdef6");//�̵��
					String vuserdef7 = (String) getBillCardPanel().getBodyValueAt(e.getRow(), "vuserdef7") == null ? "":(String) getBillCardPanel().getBodyValueAt(e.getRow(), "vuserdef7");//�̵�����
					String vuserdef8 = (String) getBillCardPanel().getBodyValueAt(e.getRow(), "vuserdef8") == null ? "":(String) getBillCardPanel().getBodyValueAt(e.getRow(), "vuserdef8");//�̵��λ
					String cspaceid = (String) getBillCardPanel().getBodyValueAt(e.getRow(), "cspaceid") == null ? "":(String) getBillCardPanel().getBodyValueAt(e.getRow(), "cspaceid");//��λ
					 UFDouble nchecknum =  getBillCardPanel().getBodyValueAt(e.getRow(), "nchecknum") == null ? UFDouble.ZERO_DBL:(UFDouble) getBillCardPanel().getBodyValueAt(e.getRow(), "nchecknum");//�̵�����
					 UFDouble ncheckastnum = getBillCardPanel().getBodyValueAt(e.getRow(), "ncheckastnum") == null ? UFDouble.ZERO_DBL:(UFDouble) getBillCardPanel().getBodyValueAt(e.getRow(), "ncheckastnum");//�̵㸨����
					 UFDouble naccountastnum = getBillCardPanel().getBodyValueAt(e.getRow(), "naccountastnum") == null ? UFDouble.ZERO_DBL:(UFDouble) getBillCardPanel().getBodyValueAt(e.getRow(), "naccountastnum");//���渨����
					 UFDouble naccountnum = getBillCardPanel().getBodyValueAt(e.getRow(), "naccountnum") == null ? UFDouble.ZERO_DBL:(UFDouble) getBillCardPanel().getBodyValueAt(e.getRow(), "naccountnum");//��������
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
	 * ��ʼ���ࡣ
	 */
	/* ���棺�˷������������ɡ� */
	protected void initialize() {
		try {
			super.initialize();
			// �޸��ˣ������� �޸����ڣ�2007-12-26����11:05:02 �޸�ԭ���Ҽ�����"�����к�"
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

			// �õ���ǰ�ı�����ϵͳĬ�ϵı���ɫ
			m_cNormalColor = getBillCardPanel().getBillTable().getBackground();

			// ���ı���ɫ��ı�����Header��ɫ��
			DefaultTableCellRenderer tcrold = (DefaultTableCellRenderer) getBillCardPanel()
					.getBillTable().getColumnModel().getColumn(1)
					.getHeaderRenderer();
			HeaderRenderer tcr = new HeaderRenderer(tcrold);

			// �ֱ�õ���ͷ�ͱ�������������ʾ���ֶ�
			ArrayList alHeaderColChangeColorString = GeneralMethod
					.getHeaderCanotNullString(getBillCardPanel());
			ArrayList alBodyColChangeColorString = GeneralMethod
					.getBodyCanotNullString(getBillCardPanel());

			// �޸ı��еı�ͷ��ɫ
			SetColor.SetBillCardHeaderColor(getBillCardPanel(),
					alHeaderColChangeColorString);
			// SetBillCardHeaderColor(alHeaderColChangeColorString);

			// ���������ɫ���ڱ���Header��
			SetColor.SetTableHeaderColor(getBillCardPanel().getBillModel(),
					getBillCardPanel().getBillTable(),
					alBodyColChangeColorString, tcr);
			SetColor.SetTableHeaderColor(getBillListPanel().getHeadBillModel(),
					getBillListPanel().getHeadTable(),
					alHeaderColChangeColorString, tcr);
			SetColor.SetTableHeaderColor(getBillListPanel().getBodyBillModel(),
					getBillListPanel().getBodyTable(),
					alBodyColChangeColorString, tcr);

			// ���������ɫ���ڱ���Row��
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
	 * ����ڵ� - ��������ΪӦ�ó�������ʱ���������������
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
			nc.vo.scm.pub.SCMEnv.out("nc.ui.pub.ToftPanel �� main() �з����쳣");
			nc.vo.scm.pub.SCMEnv.error(exception);
		}
	}

	/**
	 * onButtonClicked ����ע�⡣
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
			// ����ʵ��¼�� ��Ҫ�������ں����������ã�
			// ���������setBillCheckInputStatus�����ں����ڲ�������
			// ����״̬���ò���ʱ
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

		// �����Ƿ���Ա�������add by hanwei 2003-12-30
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
	// ����༭���������
	protected nc.ui.ic.pub.bill.BarcodeCtrl m_BarcodeCtrl = null;
	public BarcodeCtrl getBarcodeCtrl() {
		if (m_BarcodeCtrl == null) {
			m_BarcodeCtrl = new BarcodeCtrl();
			m_BarcodeCtrl.m_sCorpID = m_sCorpID;
		}
		return m_BarcodeCtrl;
	}
	
	// ����༭�����ɫ�У�ÿX+1�е���ɫ��Ҫ����
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

		boolean bNegative = false; // �Ƿ���
		// �޸�ʵ��������Ӧ������
		UFDouble ufdNum = null;
		// ����"�Ƿ񰴸���λ��������"���ԣ�������������Զ���һ�������������Զ���һ��
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

		// ɾ��������
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

		// �������������ֶ�
		try {
			getBillCardPanel().setBodyValueAt(ufdNumDlg.abs(), iCurFixLine,
					"nchecknum");

		} catch (Exception e) {
			nc.vo.scm.pub.SCMEnv.error(e);
		}
		// �Ƿ��޸ĵ�������Ҫ�������������ж�
		if (true) {
			// �޸Ŀ�Ƭ����״̬

			getBillCardPanel().setBodyValueAt(ufdNumDlg, iCurFixLine,
					m_sMyItemKey);

			nc.ui.pub.bill.BillEditEvent event1 = new nc.ui.pub.bill.BillEditEvent(
					getBillCardPanel().getBodyItem(m_sMyItemKey), ufdNumDlg,
					m_sMyItemKey, iCurFixLine);
			afterEdit(event1);
			// ִ��ģ�湫ʽ
			/*execEditFormula(getBillCardPanel(), iCurFixLine,
					m_sMyItemKey);*/
			// ����������״̬Ϊ�޸�
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
			"UPP4008spec-000567")/* @res "�������̵㵥���ܽ������������" */);
			return;
		}
		// TODO �Զ����ɷ������
		// �ж��Ƿ��ܹ���������༭
		SpecialBillVO voBill = null;

		int iCurFixLine = 0;
		// �Ƿ���Ա༭
		boolean bDirectSave = false;
		if (m_iMode == BillMode.Browse ) {
			bDirectSave = true;
		} else {
			bDirectSave = false;
		}

			voBill = m_voBill;
			iCurFixLine = getBillCardPanel().getBillTable().getSelectedRow();
	

		// ��ȥ����ʽ�µĿ���
		filterNullLine();
		if (getBillCardPanel().getRowCount() <= 0) {
			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"4008bill", "UPP4008bill-000072")/* @res "�����������!" */);
			getBillCardPanel().addLine();
			nc.ui.scm.pub.report.BillRowNo.addLineRowNo(getBillCardPanel(),
					m_sBillTypeCode, "crowno");
			return;
		}
		// ����кŵĺϷ���; �÷���Ӧ���ڹ��˿��еĺ��档
		// ��Ҫ���к�ȷ��Ψһ��
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
			// ���������Ĵ��
			if (itemvo.getBarcodeManagerflag().booleanValue() ) {
				// �õ���ͷ�ĵ��ݺ�, �����к�, �������,�������
				// ArrayList altemp = new ArrayList();

				// ��������£��к�û����m_voBill�д���,���������к�
				setBillCrowNo(voBill, getBillCardPanel());

				// �����������Items

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

				// �������Ƿ񱣴�������õ�����༭���棬�����ڱ༭���汣������ǰ����
				// m_dlgBarCodeEdit.setSaveBarCode(m_bBarcodeSave);
				m_dlgBarCodeEdit.setSaveBadBarCode(false);
				// �޸���:������ �޸�����:2007-04-10
				// �޸�ԭ��:�߼�����,����һ������,�Ƿ񱣴���������bSaveBarcodeFinal
				
				// �������رգ���������༭���ܱ༭
				boolean bbarcodeclose = itemvo.getBarcodeClose().booleanValue();
				m_dlgBarCodeEdit.setUIEditableBarcodeClose(!bbarcodeclose);
				m_dlgBarCodeEdit.setUIEditable(m_iMode);
				// ���õ�Items��
				m_dlgBarCodeEdit.setCurBarcodeItems(itemBarcodeVos, iFixLine);

				if (m_dlgBarCodeEdit.showModal() == nc.ui.pub.beans.UIDialog.ID_OK) {
					getBillCardPanel().getBillModel().setNeedCalculate(false);
					// �������������
					m_utfBarCode.setCurBillItem(itemBarcodeVos);
					// ����Ҫ����������ɾ�����ݣ�m_utfBarCode.setRemoveBarcode(m_dlgBarCodeEdit.getBarCodeDelofAllVOs());

					// Ŀ������m_billvo���������ݣ��޸Ŀ�Ƭ����״̬

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

							if (!m_dlgBarCodeEdit.m_bModifyBillUINum) { // �޸Ŀ�Ƭ����״̬
								showHintMessage(nc.ui.ml.NCLangRes
										.getInstance().getStrByID("4008bill",
												"UPP4008bill-000073")/*
																		 * @res
																		 * "����༭��Ĳ��������ǲ��޸Ľ������������ݽ���ʵ�����������޸ģ�"
																		 */);
							}

							if (!getBarcodeCtrl().isModifyBillUINum()) {
								showHintMessage(nc.ui.ml.NCLangRes
										.getInstance().getStrByID("4008bill",
												"UPP4008bill-000074")/*
																		 * @res
																		 * "��ǰ���ݽ��治�����޸�ͨ�����������޸�ʵ�����������ݽ���ʵ�����������޸ģ�"
																		 */);

							}

							if (m_dlgBarCodeEdit.m_bModifyBillUINum
									&& getBarcodeCtrl().isModifyBillUINum()) { // �޸Ŀ�Ƭ����״̬
								showHintMessage(nc.ui.ml.NCLangRes
										.getInstance().getStrByID("4008bill",
												"UPP4008bill-000075")/*
																		 * @res
																		 * "����༭��Ĳ����������޸Ľ����������ҽ�����������޸�ʵ�����������ݽ���ʵ�������Ѿ����޸ģ�"
																		 */);
							}

						}

					}
					// dw
					//.resetSpace(iCurFixLine);

					getBillCardPanel().getBillModel().setNeedCalculate(true);
					getBillCardPanel().getBillModel().reCalcurateAll();

				}

			} else {// �޸���:������ �޸�����:2007-04-05 �޸�ԭ��:���Ǵ���������Ʒ
				nc.ui.ic.pub.tools.GenMethod.handleException(this, null,
						new BusinessException(nc.ui.ml.NCLangRes.getInstance()
								.getStrByID("4008bill", "UPP4008bill-000002")/*
																				 * @res
																				 * "���д�������������������������޸Ĵ�������������ԣ�"
																				 */
								+ itemvo.getCinventorycode()));
			}

		} else {
			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"SCMCOMMON", "UPPSCMCommon-000356")/* @res "��ѡ������У�" */);
		}
		}catch (Exception e) {
			// TODO: handle exception
			GenMethod.handleException(this, null, e);
		}
	}

	protected BarCodeViewDlg m_onhandBCViewDlg = null;//���������ϸ
	protected BarCodeViewDlg m_countBCViewDlg = null;//ʵ��������ϸ
	protected BarCodeViewDlg m_DiffBCViewDlg = null;//�����̵������ϸ

	/**
	 * �����ˣ������� ����ʱ�䣺2008-7-10 ����08:27:13 ����ԭ�� �̵㵥�������̵��Ľ��������ϸ��Ϣ
	 *
	 */
	private void onViewOnhandBC(){
		// TODO �Զ����ɷ������

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
			"UPP4008spec-000568")/* @res "û�п���ʾ������" */);
		
		} catch (Exception e) {
			showErrorMessage(e.getMessage());
			handleException(e);
		}
	}
	/**
	 * �����ˣ������� ����ʱ�䣺2008-7-10 ����08:28:01 ����ԭ�� �̵㵥�������̵���ʵ��������ϸ��Ϣ
	 *
	 */
	private void onViewCountBC(){
		// TODO �Զ����ɷ������

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
			"UPP4008spec-000568")/* @res "û�п���ʾ������" */);
		} catch (Exception e) {
			showErrorMessage(e.getMessage());
			handleException(e);
		}
	}

	/**
	 * �����ˣ������� ����ʱ�䣺2008-7-10 ����08:28:15 ����ԭ�� �̵㵥�������̵��������̵������ϸ��Ϣ
	 *
	 */
	private void onViewDiffBC(){
		// TODO �Զ����ɷ������

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
			"UPP4008spec-000568")/* @res "û�п���ʾ������" */);
		} catch (Exception e) {
			showErrorMessage(e.getMessage());
			handleException(e);
		}
	}	
	/**
	 * �����ˣ������� ����ʱ�䣺2008-7-10 ����08:28:39 ����ԭ�� ���������ϸ��ʾ��
	 * @return
	 */
	private BarCodeViewDlg getOnhandBCViewDlg() {
		if (m_onhandBCViewDlg == null) {
			m_onhandBCViewDlg = new BarCodeViewDlg(this,  nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec",
			"UPP4008spec-000569")/* @res "���������ϸ" */,
					ICConst.BarCodeViewDlgType.ViewOnhandBC);
		}
		return m_onhandBCViewDlg;
	}
	/**
	 * �����ˣ������� ����ʱ�䣺2008-7-10 ����08:29:01 ����ԭ�� ʵ��������ϸ��ʾ��
	 * @return
	 */
	private BarCodeViewDlg getCountViewDlg() {
		if (m_countBCViewDlg == null) {
			m_countBCViewDlg = new BarCodeViewDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec",
			"UPP4008spec-000570")/* @res "ʵ��������ϸ" */,
					ICConst.BarCodeViewDlgType.ViewCountBC);
		}
		return m_countBCViewDlg;
	}
	/**
	 * �����ˣ������� ����ʱ�䣺2008-7-10 ����08:29:10 ����ԭ�� �����̵������ϸ��ʾ��
	 * @return
	 */
	private BarCodeViewDlg getDiffViewDlg() {
		if (m_DiffBCViewDlg == null) {
			m_DiffBCViewDlg = new BarCodeViewDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec",
			"UPP4008spec-000571")/* @res "�����̵������ϸ" */,
					ICConst.BarCodeViewDlgType.ViewDiffBC);
		}
		return m_DiffBCViewDlg;
	}

	/**
	 * �:����V31���� ����ȡ�����޸Ŀ��á�
	 * nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec",
			"UPP4008spec-000571")/* @res "�����̵������ϸ" 
	 * ����Ȼ��������� �����棬ȡ��������ȡ����ʵ���̵�˵����á�
	 */
	protected void setAfterGetAcNumButn() {
		getBillCardPanel().setEnabled(true);
		m_iMode = BillMode.Update;
		// �в����ſ�
		m_billRowMng.setEnabled(true);
		m_boAddRow.setEnabled(true);
		m_boDeleteRow.setEnabled(true);
		m_boInsertRow.setEnabled(true);
		m_boCopyRow.setEnabled(true);
		m_boPasteRow.setEnabled(m_bCopyRow);
		// ���棬ȡ���ſ�
		m_boSave.setEnabled(true);
		m_boCancel.setEnabled(true);
		m_boCheckNum.setEnabled(true);
		// �̵����
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
	 * �����������޸�ʱ�İ�ť״̬
	 */
	private void setButtonStateNewUpdate() {

		//����ʹ�ӡ��ť������  add by qinchao 20090112
		m_boJointCheck.setEnabled(false);
		m_PrintMng.setEnabled(false);
		//EndOF
		
		m_addMng.setEnabled(true);
		m_boAdd.setEnabled(false);
		// �޸�״̬�£��̵�ѡ�񲻿��ã��̵��֧�ֿ���
		m_boCheckChoose.setEnabled(false);
		m_boCheckInv.setEnabled(true);
		// �޸�״̬���޸ģ�ɾ��������
		m_boChange.setEnabled(false);
		m_boDelete.setEnabled(false);
		// �޸�����£����棬ȡ������
		m_boSave.setEnabled(true);
		m_boCancel.setEnabled(true);
		// �޸�������в�������
		m_billRowMng.setEnabled(true);
		m_boAddRow.setEnabled(true);
		m_boDeleteRow.setEnabled(true);
		m_boInsertRow.setEnabled(true);
		m_boCopyRow.setEnabled(true);
		m_boPasteRow.setEnabled(m_bCopyRow);

		// �޸�״̬����������
		m_auditMng.setEnabled(false);
		m_boAuditBill.setEnabled(false);
		m_boCancelAudit.setEnabled(false);
		m_boAuditState.setEnabled(false);
		// �޸�״̬����������
		m_adjustMng.setEnabled(false);
		m_boAdjustGoods.setEnabled(false);
		m_boCancelAdjust.setEnabled(false);
		// �޸�״̬�²�ѯ����ӡ���л�������
		m_boQuery.setEnabled(false);
		m_boPrint.setEnabled(false);
		m_boPreview.setEnabled(false);
		m_boList.setEnabled(false);
		// �޸�����¸�����λ�����ƵȲ�����
		m_assistMng.setEnabled(false);
		m_boLocate.setEnabled(false);
		m_boCopyBill.setEnabled(false);
		// �޸�״̬������ȡ������
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
		// ����״̬��Ԥ����ť������
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
	 * �������״̬�İ�ť״̬
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

			//����ʹ�ӡ��ť������  add by qinchao 20090112
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
			// ����ȡ�����ɿ���
			m_boGetAccNumber.setEnabled(false);

		} else {
			//����ʹ�ӡ��ť������  add by qinchao 20090112
			m_boJointCheck.setEnabled(true);
			m_PrintMng.setEnabled(true);
			//EndOF
			m_adjustMng.setEnabled(true);
			m_auditMng.setEnabled(true);
			m_boCountBCImport.setEnabled(false);
			// �Ѿ������������������������ȡ��
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
				// ����
				if (isAdjusted()) {

					m_boAdjustGoods.setEnabled(false);
					m_boCancelAudit.setEnabled(false);
					m_boCancelAdjust.setEnabled(true);
					// ������������ȡ��
					m_boGetAccNumber.setEnabled(false);
					m_boGetBCAccNumber.setEnabled(false);
					
				} else {
					m_boCancelAudit.setEnabled(true);
					m_boAdjustGoods.setEnabled(true);
					m_boCancelAdjust.setEnabled(false);
					// ������������ȡ��
					m_boGetAccNumber.setEnabled(false);
					m_boGetBCAccNumber.setEnabled(false);
				}

			}
			// �ǵ���������״̬
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
				// ����ȡ������
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
		// ���������в���������
		m_billRowMng.setEnabled(false);
		m_boAddRow.setEnabled(false);
		m_boDeleteRow.setEnabled(false);
		m_boInsertRow.setEnabled(false);
		m_boCopyRow.setEnabled(false);
		m_boPasteRow.setEnabled(false);
		// ��ѯ��ӡ�ȿ���
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
			//����ʹ�ӡ��ť������  add by qinchao 20090112
			m_boJointCheck.setEnabled(true);
			m_PrintMng.setEnabled(true);
			//EndOF
			m_boChange.setEnabled(!(isAudited()) && !(isAdjusted()));
			m_boDelete.setEnabled(!(isAudited()) && !(isAdjusted()));
		} else {
			//����ʹ�ӡ��ť������  add by qinchao 20090112
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
	 * ����״̬�µİ�ť���ƣ��ڻ���������޸ģ��б��״̬�����
	 */
	private void setButtonStateOther() {

		// ֻ����ʵ��¼�������ȡ��״̬��������Ŧ�ſ���ʹ��
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
		// ��һ����״̬���п���ȡ�����ϴεĵ���״̬������ֱ���ƹ�������ж�
		if (!m_bBillInit && m_alListData != null && m_alListData.size() > 0) {

			if (sbvotemp != null && sbvotemp.getParentVO() != null) {
				
				m_fillMng.setEnabled(true);

				if (iFlag != null) {

					// ������
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
						// �����в�������ȡ��
						m_boGetAccNumber.setEnabled(false);
						m_boGetBCAccNumber.setEnabled(false);

						setBodyMenuShow(false);

					} // ����ȡ��
					else if (sFlag
							.equals(nc.vo.ic.pub.bill.BillStatus.CHECKING)) {

						if (iSaveFlag == 1)
							m_boChange.setEnabled(true);
						else
							// ����ȡ��ʱ�൱���޸�,���޸Ĳ�����
							m_boChange.setEnabled(false);
						// ��������
						// m_boAuditBill.setEnabled(false);

						m_fillMng.setEnabled(true);
						m_boCheckNum.setEnabled(true);
						if (null == m_iscountflag || !m_iscountflag.booleanValue()){
							m_boFillNum.setEnabled(true);
					
						}else{
							m_boFillNum.setEnabled(false);
			
						}

					}
					// ʵ��¼��
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
						// ������ȡ��
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
						// ����ʹ���б༭�Ĺ���
						setBodyMenuShow(false);
					}

				}
			}
		}

		// ��һ������ȡ����������ʱ��ʵ�̿���
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
		// ����������ȡ��������
		if (isAdjusted()){
			m_boGetAccNumber.setEnabled(false);
			m_boGetBCAccNumber.setEnabled(false);
			m_fillMng.setEnabled(false);
			m_boCheckNum.setEnabled(false);
			m_boFillNum.setEnabled(false);
			m_boCountBCImport.setEnabled(false);
		}

		// v35 ������ť״̬�� �����д��ڲ�����������m_iMode=BillMode.Browse,�ҵ�ǰ���ݲ��ǵ������������״̬��
		// ������ť���ã���������
		/*if (m_iMode == BillMode.Browse
				&& !sFlag.equals(nc.vo.ic.pub.bill.BillStatus.SIGNED)
				&& !isAdjusted()) {
			m_boAuditBill.setEnabled(true);
		} else
			m_boAuditBill.setEnabled(false);*/

		// v35 ���������޸�
		if (m_iMode == BillMode.Browse
				&& !sFlag.equals(nc.vo.ic.pub.bill.BillStatus.ADJUST)
				&& !sFlag.equals(nc.vo.ic.pub.bill.BillStatus.SIGNED)) {
			if (sFlag.trim().length() > 0) // �򿪽ӵ�ʱ�޸Ĳ��ܱ༭
				m_boChange.setEnabled(true);
			else
				m_boChange.setEnabled(false);

			if (m_iMode == BillMode.List) {
				m_boChange.setEnabled(false);
			}
		}

	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2001-4-30 13:58:35)
	 */
	protected void setButtonState() {

		switch (m_iMode) {
		case BillMode.Update: // �޸�
		case BillMode.New: // ����
			setButtonStateNewUpdate();
			setPageBtnStatus(0, 0);
			if (null != m_pageBtn)
				m_pageBtn.setPageBtnVisible(true);
			break;
		case BillMode.Browse: // ���
			setButtonStateBrowse();
			if (null != m_alListData)
				setPageBtnStatus(m_alListData.size(), m_iLastSelListHeadRow);
			if (null != m_pageBtn)
				m_pageBtn.setPageBtnVisible(true);
			break;
		case BillMode.List: // �б�״̬
			setButtonStateList();
			setPageBtnStatus(0, 0);
			if (null != m_pageBtn)
				m_pageBtn.setPageBtnVisible(false);
			break;
		}
		// �������
		setButtonStateOther();

		if (m_aryButtonGroup != null) {
			updateButtons();
		}
	}

	/**
	 * ����������ʵ������������ʱ�����ܽ��о��󲿷ֲ�����ֻ�ܴ�ӡ�����²�ѯ
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
		// �л�������
		m_boList.setEnabled(false);
		m_assistMng.setEnabled(false);

		m_boGetAccNumber.setEnabled(false);
		m_boGetBCAccNumber.setEnabled(false);
		if (m_aryButtonGroup != null) {
			updateButtons();
		}
	}

	/**
	 * ���� BillCardPanel1 ����ֵ �� * @ return nc.ui.pub.bill.BillCardPanel
	 */
	/* ���棺�˷������������ɡ� */
	protected nc.ui.pub.bill.BillCardPanel getBillCardPanel() {
		if (ivjBillCardPanel == null) {
			BillTempletVO billtempletVO = BillUIUtil.getDefaultTempletStatic("40081017", "4R", 
					getClientEnvironment().getUser().getPrimaryKey(), getCorpPrimaryKey(), null, null);
			BillData bd = new BillData(billtempletVO);
//			BillData bd = super.getBillCardPanel().getBillData();
			// ��һ��ʹ��ʱ����ʼ����λ���ա�
			if (bd != null) {
				if (bd.getBodyItem("cspacename") != null)
					bd.getBodyItem("cspacename").setComponent(
							getLocatorRefPane());

				// ��������Դ
				super.getBillCardPanel().setBillData(bd);
				// �̵㵥���ó����� add by hanwei 2003-12-24
			}
		}
		return super.getBillCardPanel();
	}

	/**
	 * ���� �������ڣ�(2001-4-18 19:45:17)
	 */
	public void onAdd() { // finished

		// Ĭ��ֻ����һ��
		m_iFirstAddRows = 1;
		super.onAdd();
		
		m_utfBarCode.setCurBillItem(null);

		getBillCardPanel().getBillData().getHeadItem("coutwarehouseid")
				.setEdit(true);
		// Ĭ�ϲ��ǵ������� add by hanwei 2003-10-30
		m_bIsImportData = false;

	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2003-12-22 17:39:03)
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
	 * �޸� �������ڣ�(2001-4-18 19:45:39)
	 */
	public void onChange() { // finished

		onChange(false);
		m_iscountflag = m_voBill.getHeaderVO().getBccountflag();
		if (m_utfBarCode != null)
			m_utfBarCode.setCurBillItem(m_voBill.getItemVOs());
	}

	/**
	 * �����ߣ������� ���ܣ� ������ ���أ� ���⣺ ���ڣ�(2001-5-10 ���� 4:15) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 */
	public void onCopyBill() {

		super.onCopyBill();
		m_utfBarCode.setCurBillItem(null);
		getBillCardPanel().getBillData().getHeadItem("coutwarehouseid")
				.setEdit(false);
		m_voBill.setHeaderValue("fbillflag", nc.vo.ic.pub.bill.BillStatus.FREE);
	}

	/**
	 * �����ߣ������� ���ܣ���ѯ ������ ���أ� ���⣺ ���ڣ�(2001-5-10 ���� 2:57) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 */
	public void onQuery() {

	  try{
      QryConditionVO qcvo = getQueryHelp().getQryConditionVOForQuery(false);
  		if (qcvo!=null) {
  
//  			nc.vo.pub.query.ConditionVO[] voCons = getConditionDlg()
//  					.getConditionVO();
//  			// ������״̬������ת��ΪSQL������
//  			voCons = packConditionVO(voCons);
//  			// ��������������
//  			resetConditionVO(voCons);
//  			// �������ĸ��Ӳ�ѯ�����Ƿ���ʵ���
  		    m_iBqrybalrec = getQueryHelp().getBalrec();
//  
//  			// �õ���ѯ������
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
  								.toString() })/* @res "���鵽{0}�ŵ��ݣ�" */);// +
  			// m_iTotalListHeadNum
  			// +
  			// "�ŵ��ݣ�");
  			else
  				showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
  						"4008spec", "UPP4008spec-000124")/*
  															 * @res
  															 * "δ�鵽���������ĵ��ݡ�"
  															 */);
  			m_bBillInit = false;
  
  			// �����ѯ�����а�������ʵ�Ƿ������Ϊ������������ֹʹ�ô󲿷ֲ˵����������ݺ�ʵ�����ݲ�һ����
  			if (m_iBqrybalrec == 2) {
  				setButtonStatusAccYN();
  				showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
  						"4008spec", "UPP4008spec-000548")/*
  															 * @res
  															 * "�����²�ѯ��ʵ����ļ�¼���к���������"
  															 */);
  			}
  
  		}
  		showBtnSwitch();
    }catch(Exception e){
      GenMethod.handleException(this, null, e);
    }
	}

	/**
	 * ���� �������ڣ�(2001-4-18 19:47:08)
	 */
	public boolean onSave() {
		boolean isAult = false; // �Ƿ��Ѿ����� by hanwei 2003-12-19

		try {
			if (m_iLastSelListHeadRow < 0) { // δѡ���κα�ֱ������ʱ�����߲�ѯ���Ϊ��ʱ���ᷢ��
				m_iLastSelListHeadRow = 0; // ���ѡ�е��б��ͷ��
				m_iTotalListHeadNum = 0; // �б��ͷĿǰ���ڵ�����
			}

			long ITimeAll = System.currentTimeMillis();
			long ITime = System.currentTimeMillis();
			// �������������
			getBillCardPanel().tableStopCellEditing();
			getBillCardPanel().stopEditing();

			// �˵����еĿ���
			filterNullLine();
			if (getBillCardPanel().getRowCount() == 0) {
				showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"4008spec", "UPP4008spec-000184")/* @res "�����ݣ����������룡" */);
				return false;
			}
			// ����кŵĺϷ���; �÷���Ӧ���ڹ��˿��еĺ��档
			if (!nc.ui.scm.pub.report.BillRowNo.verifyRowNosCorrect(
					getBillCardPanel(), m_sBillRowNo)) {
				return false;
			}

			SpecialBillVO voNowBill = getBillVO();
			voNowBill.getParentVO().setAttributeValue("cbilltypecode", m_sBillTypeCode);
			// �޸��ˣ������� �޸����ڣ�2007-10-30����10:01:00
			// �޸�ԭ���ڱ���ʱһ��Ҫ�����±���VO�����ݼ��������������VO��
			m_voBill.setIDItems(voNowBill);

			voNowBill.setHeaderValue("fbillflag", m_voBill
					.getHeaderValue("fbillflag"));
			voNowBill.setHeaderValue("icheckmode", m_voBill
					.getHeaderValue("icheckmode"));
			m_voBill.setHeaderValue("fassistantflag", voNowBill
					.getHeaderValue("fassistantflag"));

			// VOУ��
			if (!checkVO()) {
				return false;
			}

			// �����ǰ��״̬��ʵ���̵�¼��,���涯��������������
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
			// �޸Ļ��߱���󷵻ص�СVO
			SMSpecialBillVO voSM = null;

			if (BillMode.New == m_iMode || m_bBillInit == true) {

				ITime = System.currentTimeMillis();
				voNowBill
						.setChildrenVO((SpecialBillItemVO[]) getChangedItemVOs());
				// д��HVO��BillTypeCode������
				voNowBill.getParentVO().setAttributeValue("cbilltypecode",
						m_sBillTypeCode);
				
				voNowBill.setHeaderValue("fbillflag", m_voBill
						.getHeaderValue("fbillflag"));
				voNowBill.setHeaderValue("icheckmode", m_voBill
						.getHeaderValue("icheckmode"));

				// ���õ����к�zhx 0630:
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

				// ����
				if (m_sCorpID.equals(voNowBill.getVBillCode()))
					voNowBill.setVBillCode(null);
				voNowBill.setHeaderValue("coperatoridnow", m_sUserID);
				ArrayList alsPrimaryKey = (ArrayList) nc.ui.pub.pf.PfUtilClient
						.processAction(sActionName, m_sBillTypeCode,
								m_sLogDate, voNowBill);
				nc.vo.scm.pub.SCMEnv.showTime(ITime, "�̵㵥��̨�������棺");

				ITime = System.currentTimeMillis();
				if (alsPrimaryKey == null || alsPrimaryKey.size() < 2) {
					nc.vo.scm.pub.SCMEnv.out("return data error.");
					return true;
				}
				// ��ʾ��ʾ��Ϣ
				if (alsPrimaryKey.get(0) != null)
					showErrorMessage((String) alsPrimaryKey.get(0));

				voSM = (SMSpecialBillVO) alsPrimaryKey
						.get(alsPrimaryKey.size() - 1);
				// m_voBill.setIDItems(voNowBill);

				// ����HVO
				m_iLastSelListHeadRow = m_iTotalListHeadNum;
				addBillVO();

				// ���漴��������� by hanwei 2003-12-19
				Integer iIntFlag = (Integer) (voSM.getParentVO()
						.getAttributeValue("fbillflag"));
				if (nc.vo.ic.pub.bill.BillStatus.APPROVED
						.equalsIgnoreCase(iIntFlag.toString())) {
					isAult = true;
				}

				m_voBill.setIDItems(voNowBill);

				nc.vo.scm.pub.SCMEnv.showTime(ITime, "�̵㵥����������������ʱ�䣺");

			} else if (BillMode.Update == m_iMode) {
				ITime = System.currentTimeMillis();
				// �ӽ����л����Ҫ������
				voNowBill = getUpdatedBillVO();
				//����
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
				// д��HVO��BillTypeCode������
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
				// ��m_shvoBillSpecialHVO��д��FreeVO�����ӵ�ֵ
				voNowBill
						.setChildrenVO((SpecialBillItemVO[]) getChangedItemVOs());
				// //��ʱ����VO��ȫ��HIVO
				// SpecialBillItemVO[] voaTempItem = (SpecialBillItemVO[])
				// getBillVO()
				// .getChildrenVO();
				// //�õ���ǰ��ItemVO
				// voaItem = (SpecialBillItemVO[]) voNowBill.getChildrenVO();
				// //��ʱHVO[]
				// SpecialBillVO[] m_voTempBill = new SpecialBillVO[1];
				// m_voTempBill[0] = voNowBill;

				// ���õ����к�zhx 0630:
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

				nc.vo.scm.pub.SCMEnv.showTime(ITime, "�̵㵥�޸ı����̨��");

				ITime = System.currentTimeMillis();

				if (alsPrimaryKey == null || alsPrimaryKey.size() < 2) {
					nc.vo.scm.pub.SCMEnv.out("return data error.");
					return true;
				} // ��ʾ��ʾ��Ϣ
				if (alsPrimaryKey.get(0) != null)
					showErrorMessage((String) alsPrimaryKey.get(0));

				voSM = (SMSpecialBillVO) alsPrimaryKey
						.get(alsPrimaryKey.size() - 1);

				// //ͬ�����VO
				m_voBill.setIDItems(voNowBill);
				// m_voBill=voNowBill;
				// �޸�HVO
				m_alListData.set(m_iLastSelListHeadRow, m_voBill.clone());
				// ���漴��������� by hanwei 2003-12-19
				if (voSM.getParentVO() != null
						&& voSM.getParentVO().getAttributeValue("fbillflag") != null) {
					Integer iIntFlag = (Integer) voSM.getParentVO()
							.getAttributeValue("fbillflag");
					if (nc.vo.ic.pub.bill.BillStatus.APPROVED
							.equalsIgnoreCase(iIntFlag.toString())) {
						isAult = true;
					}
				}

				nc.vo.scm.pub.SCMEnv.showTime(ITime, "�̵㵥�޸ı���������ã�");

			}
			// ����̨��Ϣ���µ�����
			freshVOBySmallVO(voSM);

			// 2005-01-28 �������ɫ����
			InvAttrCellRenderer ficr = new InvAttrCellRenderer();
			ficr.setFreeItemRenderer(getBillCardPanel(), null);

			ITime = System.currentTimeMillis();
			nc.vo.scm.pub.SCMEnv.showTime(ITime, "��Ƭ��ˢ�½��棺");

			ITime = System.currentTimeMillis();
			if (isAult)
				setAuditBillFlag();

			// ����HVO
			m_iMode = BillMode.Browse;
			m_iFirstSelListHeadRow = -1;
			iSaveFlag = 1;
			setButtonState();
			setBillState();
			getBillCardPanel().updateValue();

			nc.vo.scm.pub.SCMEnv.showTime(ITime, "�̵㵥�޸ı����������3��");
			nc.vo.scm.pub.SCMEnv.showTime(ITimeAll, "�̵㵥������ʱ�䣺");
			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"4008spec", "UPP4008spec-000136")/* @res "����ɹ���" */);
			// ����֮�󵥾�Ϊ�ǳ�ʼ״̬
			m_bBillInit = false;
			return true;
		} catch (Exception e) {
			showErrorMessage(e.getMessage());
			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"4008spec", "UPP4008spec-000137")/* @res "δ����ɣ����������ԣ�" */);
			handleException(e);
			return false;
		}
	}



	/**
	 * �����ߣ������� ���ܣ� ������ ���أ� ���⣺ ���ڣ�(2001-5-15 ���� 3:12) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 */
	public void initButtons() {

		m_addMng = new ButtonObject(nc.ui.ml.NCLangRes.getInstance()
				.getStrByID("common", "UC001-0000002")/* @res "����" */,
				nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec",
						"UPPSCMCommon-000308")/* @res "���ӵ���" */, 0, "����"); /*-=notranslate=-*/
		m_boAdd = new ButtonObject(nc.ui.ml.NCLangRes.getInstance().getStrByID(
				"4008spec", "UPT40081016-000028")/* @res "ֱ��¼��" */,
				nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec",
						"UPPSCMCommon-000308")/* @res "���ӵ���" */, 0, "ֱ��¼��"); /*-=notranslate=-*/
		m_boCheckChoose = new ButtonObject(
				nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec",
						"UPT40081016-000035")/* @res "�̵�ѡ��" */,
				nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec",
						"UPT40081016-000035")/* @res "�̵�ѡ��" */, 0, "�̵�ѡ��"); /*-=notranslate=-*/
		m_boCheckInv = new ButtonObject(
				nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec",
						"UPT40081016-000029")/* @res "�����¼" */,
				nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec",
						"UPP4008spec-000508")/* @res "�����̵���ϴ��Ĵ����¼" */,"�����¼");
		m_boCopyBill = new ButtonObject(nc.ui.ml.NCLangRes.getInstance()
				.getStrByID("common", "UC001-0000043")/* @res "����" */,
				nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec",
						"UPP4008spec-000505")/* @res "���Ƶ���" */, 0, "����"); /*-=notranslate=-*/
		m_addMng.addChildButton(m_boAdd);
		m_addMng.addChildButton(m_boCopyBill);
		m_addMng.addChildButton(m_boCheckChoose);
		m_addMng.addChildButton(m_boCheckInv);

		m_boChange = new ButtonObject(nc.ui.ml.NCLangRes.getInstance()
				.getStrByID("common", "UC001-0000045")/* @res "�޸�" */,
				nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec",
						"UPPSCMCommon-000291")/* @res "�޸ĵ���" */, 0, "�޸�"); /*-=notranslate=-*/
		m_boDelete = new ButtonObject(nc.ui.ml.NCLangRes.getInstance()
				.getStrByID("common", "UC001-0000039")/* @res "ɾ��" */,
				nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec",
						"UPP4008spec-000504")/* @res "ɾ������" */, 0, "ɾ��"); /*-=notranslate=-*/

		// m_boJointAdd = new ButtonObject("����¼��", "����¼��", 0);
		m_boSave = new ButtonObject(nc.ui.ml.NCLangRes.getInstance()
				.getStrByID("common", "UC001-0000001")/* @res "����" */,
				nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
						"UC001-0000001")/* @res "����" */, 0, "����"); /*-=notranslate=-*/
		m_boCancel = new ButtonObject(nc.ui.ml.NCLangRes.getInstance()
				.getStrByID("common", "UC001-0000008")/* @res "ȡ��" */,
				nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
						"UC001-0000008")/* @res "ȡ��" */, 0, "ȡ��"); /*-=notranslate=-*/

		m_boGetAccNumber = new ButtonObject(
				nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec",
						"UPT40081016-000036")/* @res "����ȡ��" */,
				nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec",
						"UPT40081016-000036")/* @res "����ȡ��" */, 0, "����ȡ��"); /*-=notranslate=-*/
		
		m_boGetBCAccNumber = new ButtonObject(
				nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec",
						"UPP4008spec-000576")/* @res "�������̨ȡ��" */,
				nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec",
						"UPP4008spec-000576")/* @res "�������̨ȡ��" */, 0, "�������̨ȡ��");

		m_fillMng = new ButtonObject(
				nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec",
						"UPT40081016-000030")/* @res "ʵ��¼��" */,
				nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec",
						"UPP4008spec-000509")/* @res "��д���ʵ���̵�����" */, 0,
				"ʵ��¼��"); /*-=notranslate=-*/
		m_boCheckNum = new ButtonObject(
				nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec",
						"UPT40081016-000031")/* @res "�ֹ�¼��" */,
				nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec",
						"UPP4008spec-000510")/* @res "�ֹ���д���ʵ���̵�����" */, 0,
				"�ֹ�¼��"); /*-=notranslate=-*/
		m_boFillNum = new ButtonObject(
				nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec",
						"UPT40080402-000036")/* @res "�Զ�ȡ��" */,
				nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec",
						"UPP4008spec-000511")/* @res "�Զ�������������д���̵�����" */, 0,
				"�Զ�ȡ��"); /*-=notranslate=-*/
		m_boCountBCImport = new ButtonObject(
				nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec",
						"UPP4008spec-000560")/* @res "����ʵ������" */,
				nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec",
						"UPP4008spec-000560")/* @res "����ʵ������" */, 0, "����ʵ������"); /*-=notranslate=-*/
    
    m_boLineCardEdit = new ButtonObject(nc.ui.ml.NCLangRes.getInstance()
        .getStrByID("common", "SCMCOMMONIC55YB002")/* @res "��Ƭ�༭" */,
        nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
            "SCMCOMMONIC55YB002")/* @res "��Ƭ�༭" */, 0, "��Ƭ�༭"); /*-=notranslate=-*/

		m_fillMng.addChildButton(m_boCheckNum);
		m_fillMng.addChildButton(m_boFillNum);
		m_fillMng.addChildButton(m_boCountBCImport);
		m_boCalculate = new ButtonObject(
				nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec",
						"UPP4008spec-000512")/* @res "�������" */,
				nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec",
						"UPP4008spec-000513")/* @res "���������������̵������Ĳ�ֵ" */, 0,
				"�������"); /*-=notranslate=-*/

		m_boQuery = new ButtonObject(nc.ui.ml.NCLangRes.getInstance()
				.getStrByID("common", "UC001-0000006")/* @res "��ѯ" */,
				nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
						"UC001-0000006")/* @res "��ѯ" */, 0, "��ѯ"); /*-=notranslate=-*/
		// 2003-05-03����
		m_boJointCheck = new ButtonObject(nc.ui.ml.NCLangRes.getInstance()
				.getStrByID("SCMCOMMON", "UPPSCMCommon-000145")/* @res "����" */,
				nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON",
						"UPPSCMCommon-000145")/* @res "����" */, 0, "����"); /*-=notranslate=-*/
		m_PrintMng = new ButtonObject(nc.ui.ml.NCLangRes.getInstance()
				.getStrByID("common", "UC001-0000007")/* @res "��ӡ" */,
				nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
						"UC001-0000007")/* @res "��ӡ" */, 0, "��ӡ����"); /*-=notranslate=-*/
		m_boPrint = new ButtonObject(nc.ui.ml.NCLangRes.getInstance()
				.getStrByID("common", "UC001-0000007")/* @res "��ӡ" */,
				nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
						"UC001-0000007")/* @res "��ӡ" */, 0, "��ӡ"); /*-=notranslate=-*/
		m_boPreview = new ButtonObject(nc.ui.ml.NCLangRes.getInstance()
				.getStrByID("4008spec", "UPPSCMCommon-000305")/* @res "Ԥ��" */,
				nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec",
						"UPPSCMCommon-000305")/* @res "Ԥ��" */, 0, "Ԥ��"); /*-=notranslate=-*/
		{
			m_PrintMng.addChildButton(m_boPrint);
			m_PrintMng.addChildButton(m_boPreview);
		}
		m_boList = new ButtonObject(nc.ui.ml.NCLangRes.getInstance()
				.getStrByID("4008spec", "UPPSCMCommon-000186")/* @res "�л�" */,
				nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec",
						"UPPSCMCommon-000186")/* @res "�л�" */, 0, "�л�"); /*-=notranslate=-*/

		// m_boOut = new ButtonObject("ת��", "ת��", 0);
		// m_boIn = new ButtonObject("ת��", "ת��", 0);

		// m_billMng = new ButtonObject("����ά��", "����ά������", 0);

		m_billRowMng = new ButtonObject(nc.ui.ml.NCLangRes.getInstance()
				.getStrByID("common", "UC001-0000011")/* @res "�в���" */,
				nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
						"UC001-0000011")/* @res "�в���" */, 0, "�в���"); /*-=notranslate=-*/
		m_boAddRow = new ButtonObject(nc.ui.ml.NCLangRes.getInstance()
				.getStrByID("common", "UC001-0000012")/* @res "����" */,
				nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
						"UC001-0000012")/* @res "����" */, 0, "����"); /*-=notranslate=-*/
		m_boDeleteRow = new ButtonObject(nc.ui.ml.NCLangRes.getInstance()
				.getStrByID("common", "UC001-0000013")/* @res "ɾ��" */,
				nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
						"UC001-0000013")/* @res "ɾ��" */, 0, "ɾ��"); /*-=notranslate=-*/
		m_boInsertRow = new ButtonObject(nc.ui.ml.NCLangRes.getInstance()
				.getStrByID("common", "UC001-0000016")/* @res "������" */,
				nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
						"UC001-0000016")/* @res "������" */, 0, "������"); /*-=notranslate=-*/
		m_boCopyRow = new ButtonObject(nc.ui.ml.NCLangRes.getInstance()
				.getStrByID("common", "UC001-0000014")/* @res "������" */,
				nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
						"UC001-0000014")/* @res "������" */, 0, "������"); /*-=notranslate=-*/
		m_boPasteRow = new ButtonObject(nc.ui.ml.NCLangRes.getInstance()
				.getStrByID("common", "UC001-0000015")/* @res "ճ����" */,
				nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
						"UC001-0000015")/* @res "ճ����" */, 0, "ճ����"); /*-=notranslate=-*/
		
		m_boPasteRowTail = new ButtonObject(nc.ui.ml.NCLangRes.getInstance()
				.getStrByID("4008bill", "UPP4008bill-000556")/* @res "ճ���е���β" */,
				nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill",
						"UPP4008bill-000556")/* @res "ճ���е���β" */, 0, "ճ���е���β"); /*-=notranslate=-*/
		m_boNewRowNo = new ButtonObject(nc.ui.ml.NCLangRes.getInstance()
				.getStrByID("4008bill", "UPP4008bill-000551")/* @res "�����к�" */,
				nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill",
						"UPP4008bill-000551")/* @res "�����к�" */, 0, "�����к�"); /*-=notranslate=-*/

		m_boBarCode = new ButtonObject(nc.ui.ml.NCLangRes.getInstance()
				.getStrByID("4008spec", "UPP4008spec-000564")/* @res "������" */,
				nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
						"UPP4008spec-000564")/* @res "������" */, 0, "������"); /*-=notranslate=-*/

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
				.getStrByID("common", "UC001-0000027")/* @res "����" */,
				nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
						"UC001-0000027")/* @res "����" */, 0, "����"); /*-=notranslate=-*/
		m_boAuditBill = new ButtonObject(nc.ui.ml.NCLangRes.getInstance()
				.getStrByID("common", "UC001-0000027")/* @res "����" */,
				nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
						"UC001-0000027")/* @res "����" */, 0, "����"); /*-=notranslate=-*/
		m_boCommit = new ButtonObject(nc.ui.ml.NCLangRes.getInstance()
				.getStrByID("common", "SCMCOMMON000000080")/* @res "����" */,
				nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
						"SCMCOMMON000000080")/* @res "����" */, 0, "����"); /*-=notranslate=-*/
		m_boCancelAudit = new ButtonObject(nc.ui.ml.NCLangRes.getInstance()
				.getStrByID("common", "UC001-0000028")/* @res "����" */,
				nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
						"UC001-0000028")/* @res "����" */, 0, "����"); /*-=notranslate=-*/
		m_boAuditState = new ButtonObject(nc.ui.ml.NCLangRes.getInstance()
				.getStrByID("common", "UC000-0001558")/* @res "����״̬" */,
				nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec",
						"UPP4008spec-000514")/* @res "��ѯ�̵㵥����״̬" */, 0, "����״̬"); /*-=notranslate=-*/
		m_auditMng.addChildButton(m_boAuditBill);
		m_auditMng.addChildButton(m_boCommit);
		m_auditMng.addChildButton(m_boCancelAudit);
		m_auditMng.addChildButton(m_boAuditState);

		m_adjustMng = new ButtonObject(nc.ui.ml.NCLangRes.getInstance()
				.getStrByID("4008spec", "UPT40081016-000033")/* @res "����" */,
				nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec",
						"UPT40081016-000033")/* @res "����" */, 0, "����"); /*-=notranslate=-*/
		m_boAdjustGoods = new ButtonObject(nc.ui.ml.NCLangRes.getInstance()
				.getStrByID("4008spec", "UPT40081016-000033")/* @res "����" */,
				nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec",
						"UPT40081016-000033")/* @res "����" */, 0, "����"); /*-=notranslate=-*/
		m_boCancelAdjust = new ButtonObject(
				nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec",
						"UPT40081016-000034")/* @res "ȡ������" */,
				nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec",
						"UPT40081016-000034")/* @res "ȡ������" */, 0, "ȡ������"); /*-=notranslate=-*/
		m_adjustMng.addChildButton(m_boAdjustGoods);
		m_adjustMng.addChildButton(m_boCancelAdjust);

		m_assistMng = new ButtonObject(
				nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec",
						"UPT40081016-000032")/* @res "��������" */,
				nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec",
						"UPT40081016-000032")/* @res "��������" */, 0, "��������"); /*-=notranslate=-*/

		m_boLocate = new ButtonObject(nc.ui.ml.NCLangRes.getInstance()
				.getStrByID("4008spec", "UPPSCMCommon-000089")/* @res "��λ" */,
				nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec",
						"UPPSCMCommon-000089")/* @res "��λ" */, 0, "��λ"); /*-=notranslate=-*/
		m_boRowQuyQty = new ButtonObject(
				nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec",
						"UPPSCMCommon-000359")/* @res "������ѯ" */,
				nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec",
						"UPPSCMCommon-000359")/* @res "������ѯ" */, 0, "������ѯ"); /*-=notranslate=-*/

		m_boViewOnhandBC = new ButtonObject(
				nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec",
						"UPP4008spec-000561")/* @res "���������ϸ" */,
				nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec",
						"UPP4008spec-000561")/* @res "���������ϸ" */, 0, "���������ϸ"); /*-=notranslate=-*/

		m_boViewCountBC = new ButtonObject(
				nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec",
						"UPP4008spec-000562")/* @res "ʵ��������ϸ" */,
				nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec",
						"UPP4008spec-000562")/* @res "ʵ��������ϸ" */, 0, "ʵ��������ϸ"); /*-=notranslate=-*/

		m_boViewDiffBC = new ButtonObject(
				nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec",
						"UPP4008spec-000563")/* @res "�����̵������ϸ" */,
				nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec",
						"UPP4008spec-000563")/* @res "�����̵������ϸ" */, 0,
				"�����̵������ϸ"); /*-=notranslate=-*/

		m_assistMng.addChildButton(m_boRowQuyQty);

		m_assistMng.addChildButton(m_boViewOnhandBC);

		m_assistMng.addChildButton(m_boViewCountBC);

		m_assistMng.addChildButton(m_boViewDiffBC);

		// ���·�ҳ�Ŀ���
		m_pageBtn = new PageCtrlBtn(this);
		m_boBrowse = new ButtonObject(nc.ui.ml.NCLangRes.getInstance()
				.getStrByID("common", "UC001-0000021")/* @res "���" */,
				nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
						"UC001-0000021")/* @res "���" */, 0, "���"); /*-=notranslate=-*/

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
	 * �����ߣ������� ���ܣ���ʼ������ ������ ���أ� ���⣺ ���ڣ�(2001-5-24 ���� 6:27) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 */
	protected void initVariable() {
		m_sBillTypeCode = nc.vo.ic.pub.BillTypeConst.m_check;
		// ���õĸ����ڵ��BO_Client�ӿ�,Ӧ�������޸�
		// sSpecialHBO_Client = "nc.ui.ic.ic261.SpecialHBO_Client";
		m_sPNodeCode = "40081017";
		// ������ͨ����ʽ����ѯ:��
		setIsByFormula(true);

	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2001-3-23 2:02:27)
	 * 
	 * @param e
	 *            ufbill.BillEditEvent
	 */
	public void bodyRowChange(nc.ui.pub.bill.BillEditEvent e) {
		int colnow, rownow;

		getBillCardPanel().rememberFocusComponent();
		// ��Ӧ�б���ʽ�±�ͷѡ��ı��¼���
		if (e.getSource() == this.getBillListPanel().getHeadTable()) {
			// �����λ��2003-07-21 ydy
			clearOrientColor();
			rownow = e.getRow();
			
			if (null != m_alListData){
				SpecialBillVO sbvotemp =
					(SpecialBillVO) m_alListData.get(rownow);
				if (null != sbvotemp)
					m_iscountflag = sbvotemp.getHeaderVO().getBccountflag();
			}

			listSelectionChanged(e);

			// ��������ʵ���Ƿ�����Ĳ˵�
			if (m_iBqrybalrec == 2)
				setButtonStatusAccYN();
		}

		if (e.getSource() == this.getBillListPanel().getBodyTable())
			m_iLastSelCardBodyRow = e.getRow();

		if (e.getSource() == this.getBillCardPanel().getBillTable()) {
			rownow = e.getRow();

			m_iLastSelCardBodyRow = rownow;

			// ��ʾ��β����
			setTailValue(rownow);

		}
		getBillCardPanel().restoreFocusComponent();
	}

	/**
	 * �ж����״̬�½����Ƿ���ڲ�������
	 * 
	 * @return bRet true:�����д��ڲ��������� false: �����в�����
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
			sb.append("������û��������������û���̵�����,��������Ϊ0,�Ƿ�����:");
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
	 * �����ߣ������� ���ܣ� ������ ���أ� ���⣺ ���ڣ�(2001-5-10 ���� 2:54) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 */
	public void onAuditBill() {

		SpecialBillVO voNow = getBillVO();
		showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec",
				"UPP4008spec-000584")/* @res "�ύ" */);

		// v35 ljun ����ǰ����̵������������������Ƿ�����
		String msg = ifExistCYPD(voNow);
		if (msg != null) {
			int yesno = showYesNoMessage(msg);
			if (yesno == nc.ui.pub.beans.MessageDialog.ID_NO)
				return;
		}

		// ���±�β
		try {

			if (voNow == null || voNow.getChildrenVO() == null
					|| voNow.getChildrenVO().length == 0) {
				nc.vo.scm.pub.SCMEnv.out("no data");
				showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"4008spec", "UPP4008spec-000185")/*
															 * @res
															 * "�̵㵥û�����ݣ���������"
															 */);
				return;
			}
			nc.vo.scm.pub.SCMEnv.out("++++"
					+ voNow.getHeaderVO().getCspecialhid() + "+++");
			voNow.setHeaderValue("cauditorid", m_sUserID);
			voNow.setHeaderValue("coperatoridnow", m_sUserID);
			// ʹ��������
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
				// ����PK
				String sBillPK = voNow.getPrimaryKey();
				// ˢ��ts
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
				// �����޸�ɾ��

				// �����ѯ�������к��Ѿ������������޸�ɾ������

				m_alListData.set(m_iLastSelListHeadRow, m_voBill.clone());

				m_iFirstSelListHeadRow = -1;
				//switchListToBill();
				setButtonState();
				setBillState();
				showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"4008spec", "UPP4008spec-000186")/* @res "�����ɹ���" */);
			} else
				showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"4008spec", "UPP4008spec-000187")/* @res "����ʧ�ܣ�" */);

		} catch (Exception e) {
			nc.vo.scm.pub.SCMEnv.out("����ͨѶʧ�ܣ�");
			handleException(e);
			nc.ui.pub.beans.MessageDialog.showErrorDlg(this,
					nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec",
							"UPPSCMCommon-000059")/* @res "����" */, e
							.getMessage());
		}
	}
	
	public void onCommit() {

		SpecialBillVO voNow = getBillVO();
		showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec",
				"UPP4008spec-000584")/* @res "�ύ" */);


		// ���±�β
		try {

			if (voNow == null || voNow.getChildrenVO() == null
					|| voNow.getChildrenVO().length == 0) {
				nc.vo.scm.pub.SCMEnv.out("no data");
				showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"4008spec", "UPP4008spec-000185")/*
															 * @res
															 * "�̵㵥û�����ݣ���������"
															 */);
				return;
			}
			nc.vo.scm.pub.SCMEnv.out("++++"
					+ voNow.getHeaderVO().getCspecialhid() + "+++");
			voNow.setHeaderValue("cauditorid", m_sUserID);
			voNow.setHeaderValue("coperatoridnow", m_sUserID);
			// ʹ��������
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
				// ����PK
				String sBillPK = voNow.getPrimaryKey();
				// ˢ��ts
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
				// �����޸�ɾ��

				// �����ѯ�������к��Ѿ������������޸�ɾ������

				m_alListData.set(m_iLastSelListHeadRow, m_voBill.clone());

				m_iFirstSelListHeadRow = -1;
				// switchListToBill();
				setButtonState();
				setBillState();
				showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"4008spec", "UPP4008spec-000582")/* @res "�ύ�ɹ���" */);
			} else
				showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"4008spec", "UPP4008spec-000583")/* @res "�ύʧ�ܣ�" */);

		} catch (Exception e) {
			nc.vo.scm.pub.SCMEnv.out("����ͨѶʧ�ܣ�");
			handleException(e);
			nc.ui.pub.beans.MessageDialog.showErrorDlg(this,
					nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec",
							"UPPSCMCommon-000059")/* @res "����" */, e
							.getMessage());
		}
	}


	/**
	 * �����ߣ������� ���ܣ� ������ ���أ� ���⣺ ���ڣ�(2001-5-10 ���� 2:50) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 */
	public void onCancelAudit() {
		switch (nc.ui.pub.beans.MessageDialog.showYesNoDlg(this, null,
				nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UCH068")/*
																				 * @res
																				 * "�Ƿ�ȷ��Ҫ����"
																				 */
				, MessageDialog.ID_NO)) {

		case nc.ui.pub.beans.MessageDialog.ID_YES:
			break;

		default:
			return;
		}

		showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
				"UCH049")/* @res "��������" */);
		// ���±�β
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
			// ����PK
			String sBillPK = voNow.getPrimaryKey();
			// ˢ��ts
			freshTs(qryLastTs(sBillPK));
			// ���û�ʵ��¼��״̬
			setBillCheckInputStatus(nc.vo.ic.pub.bill.BillStatus.CHECKINPUT);
			m_alListData.set(m_iLastSelListHeadRow, m_voBill.clone());

			m_iFirstSelListHeadRow = -1;
			// switchListToBill();

			setButtonState();
			setBillState();

			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"4008spec", "UPP4008spec-000188")/* @res "����ɹ���" */);

		} catch (Exception e) {
			nc.vo.scm.pub.SCMEnv.out("����ͨѶʧ�ܣ�");
			handleException(e);
			nc.ui.pub.beans.MessageDialog.showErrorDlg(this,
					nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec",
							"UPPSCMCommon-000059")/* @res "����" */, e
							.getMessage());
		}
	}

	/**
	 * ���������Ƿ��йؼ����ظ��������������������ڣ�����ʾ�������룺
	 * �磬��1�ؼ���Ϊ123����2�ؼ���Ϊ12345���������������ȡ�����ִ������н��棻
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
	 * �����ߣ������� ���ܣ� ������ ���أ� ���⣺ ���ڣ�(2001-5-24 ���� 5:17) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 */
	protected boolean checkVO() {
		try {
			String sAllErrorMessage = "";
			// �̵㵥������add by hanwei 2003-09-17
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
			// ����>=0���
			try {
				VOCheck.checkGreaterThanZeroInput(m_voBill.getChildrenVO(),
						"nprice", nc.ui.ml.NCLangRes.getInstance().getStrByID(
								"common", "UC000-0000741")/* @res "����" */);
			} catch (ICPriceException e) {
				String sErrorMessage = GeneralMethod.getBodyErrorMessage(
						getBillCardPanel(), e.getErrorRowNums(), e.getHint());
				sAllErrorMessage = sAllErrorMessage + sErrorMessage + "\n";
			}
			// �̵�����>0���
			try {
				VOCheck.checkGreaterThanZeroInput(m_voBill.getChildrenVO(),
						"nchecknum", nc.ui.ml.NCLangRes.getInstance()
								.getStrByID("40081016", "UPT40081016-000012")/*
																				 * @res
																				 * "�̵�����"
																				 */);
			} catch (ICPriceException e) {
				String sErrorMessage = GeneralMethod.getBodyErrorMessage(
						getBillCardPanel(), e.getErrorRowNums(), e.getHint());
				sAllErrorMessage = sAllErrorMessage + sErrorMessage + "\n";
			}
			// �̵㸨����>0���
			try {
				VOCheck.checkGreaterThanZeroInput(m_voBill.getChildrenVO(),
						"ncheckastnum", nc.ui.ml.NCLangRes.getInstance()
								.getStrByID("40081016", "UPT40081016-000013")/*
																				 * @res
																				 * "�̵㸨����"
																				 */);
			} catch (ICPriceException e) {
				String sErrorMessage = GeneralMethod.getBodyErrorMessage(
						getBillCardPanel(), e.getErrorRowNums(), e.getHint());
				sAllErrorMessage = sAllErrorMessage + sErrorMessage + "\n";
			}
			// ��������ͬ������
			try {
				VOCheck.checkSamedirect(m_voBill, new String[] { "nadjustnum",
						"nadjustastnum" }, new String[] {
						nc.ui.ml.NCLangRes.getInstance().getStrByID("40081016",
								"UPT40081016-000001")/* @res "��������" */,
						nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
								"UC000-0003758") /* @res "����������" */});
			} catch (NullFieldException e) {
				String sErrorMessage = e.getHint();
				sAllErrorMessage = sAllErrorMessage + sErrorMessage + nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
				"UC000-0003758") /* @res "����������" */+"\n";
			}
			// ��������ͬ������
			try {
				VOCheck.checkSamedirect(m_voBill, new String[] { "cysl",
						"cyfsl" }, new String[] {
						nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
								"UC000-0001698")/* @res "��������" */,
						nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
								"UC000-0001701") /* @res "���츨����" */});
			} catch (NullFieldException e) {
				String sErrorMessage = e.getHint();
				sAllErrorMessage = sAllErrorMessage + sErrorMessage + nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
				"UC000-0001701") /* @res "���츨����" */+"\n";
			}
			// �̵�����ͬ������
			try {
				VOCheck.checkSameDirection(m_voBill, new String[] {
						"nchecknum", "ncheckastnum" }, new String[] {
						nc.ui.ml.NCLangRes.getInstance().getStrByID("40081016",
								"UPT40081016-000012")/* @res "�̵�����" */,
						nc.ui.ml.NCLangRes.getInstance().getStrByID("40081016",
								"UPT40081016-000013") /* @res "�̵㸨����" */});
			} catch (NullFieldException e) {
				String sErrorMessage = e.getHint();
				sAllErrorMessage = sAllErrorMessage + sErrorMessage + "\n";
			}
			if (sAllErrorMessage.trim().length() != 0) {
				showErrorMessage(sAllErrorMessage);
				return false;
			}

			// ��鵱Ϊ�л�λ�ֿ�ʱ������Ӧȫ�л�λ������Ӧȫ�޻�λ (V31�������󣬷ſ���λУ��)
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
			// * "����"
			// */,
			// nc.ui.ml.NCLangRes.getInstance()
			// .getStrByID("4008spec",
			// "UPP4008spec-000189")/*
			// * @res
			// * "�����л�λ���ͷ�ֿ��Ƿ��λ�����־��һ�£�"
			// */);
			// return false;
			// }
			// }
			// }
			// ����̵㵥��Ҫ�������û�а�����ϵ
			if(m_voBill.getHeaderVO().getIcheckmode() != CheckMode.md ){
				if (!checkUIKeyRepeat())
					return false;
			}
			
//			if (!checkUIKeyRepeat())
//				return false;

			// ������ɫΪ������ɫ
			/*SetColor.SetTableColor(getBillCardPanel().getBillModel(),
					getBillCardPanel().getBillTable(), getBillCardPanel(),
					new ArrayList(), m_cNormalColor, m_cNormalColor,
					m_bExchangeColor, m_bLocateErrorColor, "");*/
			nc.ui.ic.pub.tools.GenMethod.reSetRowColorWhenNOException(getBillCardPanel());
			
			return true;
		} catch (ICDateException e) {
			// ��ʾ��ʾ
			String sErrorMessage = GeneralMethod.getBodyErrorMessage(
					getBillCardPanel(), e.getErrorRowNums(), e.getHint());
			// showHintMessage(sErrorMessage);
			nc.ui.pub.beans.MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes
					.getInstance()
					.getStrByID("4008spec", "UPPSCMCommon-000059")/*
																	 * @res "����"
																	 */, sErrorMessage);
			// ������ɫ
			/*SetColor.SetTableColor(getBillCardPanel().getBillModel(),
					getBillCardPanel().getBillTable(), getBillCardPanel(), e
							.getErrorRowNums(), m_cNormalColor, e
							.getExceptionColor(), m_bExchangeColor,
					m_bLocateErrorColor, e.getHint());*/
			//SetColor.setErrRowColor(getBillCardPanel().getBillTable(), e.getErrorRowNums());
			nc.ui.ic.pub.tools.GenMethod.setRowColorWhenException(getBillCardPanel(), e);
			return false;
		} catch (ICNullFieldException e) {
			// ��ʾ��ʾ
			String sErrorMessage = GeneralMethod.getBodyErrorMessage(
					getBillCardPanel(), e.getErrorRowNums(), e.getHint());
			nc.ui.pub.beans.MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes
					.getInstance()
					.getStrByID("4008spec", "UPPSCMCommon-000059")/*
																	 * @res "����"
																	 */, sErrorMessage);
			// ������ɫ
/*			SetColor.SetTableColor(getBillCardPanel().getBillModel(),
					getBillCardPanel().getBillTable(), getBillCardPanel(), e
							.getErrorRowNums(), m_cNormalColor, e
							.getExceptionColor(), m_bExchangeColor,
					m_bLocateErrorColor, e.getHint());*/
			//SetColor.setErrRowColor(getBillCardPanel().getBillTable(), e.getErrorRowNums());
			nc.ui.ic.pub.tools.GenMethod.setRowColorWhenException(getBillCardPanel(), e);
			return false;
		} catch (ICNumException e) {
			// ��ʾ��ʾ
			String sErrorMessage = GeneralMethod.getBodyErrorMessage(
					getBillCardPanel(), e.getErrorRowNums(), e.getHint());
			nc.ui.pub.beans.MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes
					.getInstance()
					.getStrByID("4008spec", "UPPSCMCommon-000059")/*
																	 * @res "����"
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
			// ��ʾ��ʾ
			String sErrorMessage = GeneralMethod.getBodyErrorMessage(
					getBillCardPanel(), e.getErrorRowNums(), e.getHint());
			// showHintMessage(sErrorMessage);
			nc.ui.pub.beans.MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes
					.getInstance()
					.getStrByID("4008spec", "UPPSCMCommon-000059")/*
																	 * @res "����"
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
			// ��ʾ��ʾ
			String sErrorMessage = GeneralMethod.getBodyErrorMessage(
					getBillCardPanel(), e.getErrorRowNums(), e.getHint());
			// showHintMessage(sErrorMessage);
			nc.ui.pub.beans.MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes
					.getInstance()
					.getStrByID("4008spec", "UPPSCMCommon-000059")/*
																	 * @res "����"
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
			// ��ʾ��ʾ
			String sErrorMessage = GeneralMethod.getBodyErrorMessage(
					getBillCardPanel(), e.getErrorRowNums(), e.getHint());
			nc.ui.pub.beans.MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes
					.getInstance()
					.getStrByID("4008spec", "UPPSCMCommon-000059")/*
																	 * @res "����"
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
			// ��ʾ��ʾ
			String sErrorMessage = GeneralMethod.getBodyErrorMessage(
					getBillCardPanel(), e.getErrorRowNums(), e.getHint());
			// showHintMessage(sErrorMessage);
			nc.ui.pub.beans.MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes
					.getInstance()
					.getStrByID("4008spec", "UPPSCMCommon-000059")/*
																	 * @res "����"
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
			// ��ʾ��ʾ
			String sErrorMessage = GeneralMethod.getHeaderErrorMessage(
					getBillCardPanel(), e.getErrorRowNums(), e.getHint());
			// showHintMessage(sErrorMessage);
			nc.ui.pub.beans.MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes
					.getInstance()
					.getStrByID("4008spec", "UPPSCMCommon-000059")/*
																	 * @res "����"
																	 */, sErrorMessage);
			return false;
		} catch (NullFieldException e) {
			nc.ui.pub.beans.MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes
					.getInstance()
					.getStrByID("4008spec", "UPPSCMCommon-000059")/*
																	 * @res "����"
																	 */, e.getHint());
			return false;
		} catch (ValidationException e) {
			nc.vo.scm.pub.SCMEnv.out("У���쳣������δ֪����...");
			nc.ui.pub.beans.MessageDialog.showErrorDlg(this,
					nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec",
							"UPPSCMCommon-000059")/* @res "����" */,
					nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec",
							"UPP4008spec-000193")/* @res "У���쳣������δ֪���ϡ�" */);
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
	 * �����ߣ������� ���ܣ� ������ ���أ� ���⣺ ���ڣ�(2001-5-24 ���� 9:38) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
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

		// ����㲻Ϊ�ֿ����ʱ���������޴�����ֹ��������ֵ
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
			bi.setEnabled(false); // �̵㵥�ϵı����ڲ����Ա༭
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
																			 * "�����"
																			 */
							+ (iRow + 1)
							+ nc.ui.ml.NCLangRes.getInstance().getStrByID(
									"4008spec", "UPP4008spec-000195")/*
																		 * @res
																		 * "�д��Ϊ�����������������븨��λ���̵㸨������"
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
			} else { // �Ǹ���������
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
	 * ����û��ֹ��޸����κţ����⣬��ȷ����ʧЧ���ڼ���Ӧ���ݺţ�����ȷ������գ������û��ֹ����롣 �����ߣ����� ���ܣ� ������ ���أ� ���⣺
	 * ���ڣ�(2001-6-14 10:25:33) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
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
		/** �����κ�Ϊ�գ� */
		if ((sbatchcode != null && sbatchcode.trim().length() > 0)
				&& getLotNumbRefPane().isClicked())
			return;

		// /** �û��ֹ���д���κ�ʱ����⣬����������ȷ��� */
		// boolean isLotRight = getLotNumbRefPane().checkData();
		//
		// if (!isLotRight) {
		// getBillCardPanel().setBodyValueAt("", iSelrow, "vbatchcode");
		// }

	}

	/**
	 * �����ߣ����˾� ���ܣ����ñ�����ʾ�� ������ ���أ� ���⣺ ���ڣ�(2001-5-9 9:23:32) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
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

		// ������ from 0--->10
		String sTemp = null;
		for (int i = 0; i <= FreeVO.FREE_ITEM_NUM; i++) {
			sTemp = "vfree" + i;
			if (getBillCardPanel().getBodyItem(sTemp) != null)
				getBillCardPanel().setBodyValueAt(
						voInv.getAttributeValue(sTemp), row, sTemp);
		}
	}

	/**
	 * �����ߣ����˾� ���ܣ������������ݵĳ�ʼ���ݣ������ڣ��Ƶ��˵ȡ� ������ ���أ� ���⣺ ���ڣ�(2001-5-9 9:23:32)
	 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
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
	 * rows, or columns that changed. �޸��ˣ������� �޸����ڣ�2007-10-18����03:59:29 �޸�ԭ��
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
	 * �����ߣ������� ���ܣ������ⵥ��VO��Ϊ��ͨ����VO ������ ���أ� ���⣺ ���ڣ�(2001-6-26 ���� 4:43)
	 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
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

		// �Ա�ͷ
		gbvo.setParentVO(changeFromSpecialVOtoGeneralVOAboutHeader(gbvo, sbvo,
				iInOutFlag));

		// �Ա���
		for (int row = 0; row < iItemNumb; row++) {
			gbvo.setItem(row, changeFromSpecialVOtoGeneralVOAboutItem(gbvo,
					sbvo, iInOutFlag, row));
		}

		return gbvo;
	}

	/**
	 * �����ߣ����˾� ���ܣ�������� ������ ���أ� ���⣺ ���ڣ�(2001-5-9 9:23:32) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
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
	 * �����ߣ����˾� ���ܣ��������ˡ� ������ ���أ� ���⣺ ���ڣ�(2001-5-9 9:23:32) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
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
	 * �˴����뷽��˵���� �������ڣ�(2001-6-27 10:34:52)
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
	 * �����ߣ������� ���ܣ� ������ ���أ� ���⣺ ���ڣ�(2001-5-10 ���� 2:54) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * �޸��ˣ������� �޸�ʱ�䣺2008-7-29 ����09:46:19 �޸�ԭ��֧�ֶԵ�����������Ϊ0����������Ϊ0���̵��¼������ӯ�̿�����
	 */
	public void onAdjustGoods() {
		// �������
		showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec",
				"UPP4008spec-000196")/* @res "�������" */);
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
			showErrorMessage("�̵㵥�������״̬�����ܽ��е���������");
			return;
		}
		if (null == voNowBill.getHeaderVO().getPk_calbody_out())
			voNowBill.getHeaderVO().setPk_calbody_out(GenMethod.getsCalbodyidByWhid(new String[]{voNowBill.getHeaderVO().getCoutwarehouseid()})[0]);
		// vo����
		// �����ⵥ���VOͨ��VO����ת��Ϊ��ͨ��VO
		// �ó���VO
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
				/**add by ouyangzhb 2012-04-25 �����뵥�̵���ж�*/
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
				/**add by ouyangzhb 2012-04-25 �����뵥�̵���ж�*/
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
				// �����к�
				setRowNo(gbvo[0], "4I");
				nc.vo.ic.pub.GenMethod.fillGeneralBillVOByBarcode(gbvo[0]);
				
				/**add by ouyangzhb 2012-04-25 �뵥�̵㣬�ڽ���������������ʱ����Ҫ�ѽ�����ȥ���Զ������ֵ���*/
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
				/**add by ouyangzhb 2012-04-25 �뵥�̵㣬�ڽ���������������ʱ����Ҫ�ѽ�����ȥ���Զ������ֵ���*/
				
				alOutGeneralVO.add(gbvo[0]);
			}
		}

		// �����VO
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

				/**add by ouyangzhb 2012-04-25 �����뵥�̵���ж�*/
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
				/**add by ouyangzhb 2012-04-25 �����뵥�̵���ж�*/
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
				// �����к�
				setRowNo(gbvo[0], "4A");
				nc.vo.ic.pub.GenMethod.fillGeneralBillVOByBarcode(gbvo[0]);
				
				/**add by ouyangzhb 2012-04-25 �뵥�̵㣬�ڽ���������������ʱ����Ҫ�ѽ�����ȥ���Զ������ֵ���*/
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
				/**add by ouyangzhb 2012-04-25 �뵥�̵㣬�ڽ���������������ʱ����Ҫ�ѽ�����ȥ���Զ������ֵ���*/
				alInGeneralVO.add(gbvo[0]);
			}
		}

		if ((alInGeneralVO == null || alInGeneralVO.size() == 0)
				&& (alOutGeneralVO == null || alOutGeneralVO.size() == 0)) {
			nc.ui.pub.beans.MessageDialog.showErrorDlg(this,
					nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec",
							"UPP4008spec-000197")/* @res "���" */,
					nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec",
							"UPP4008spec-000198")/* @res "û�е���������" */);
			return;
		}

		/*getAuditDlg().setVO(tempVO, alInGeneralVO, alOutGeneralVO,
				m_sBillTypeCode, m_voBill.getPrimaryKey().trim(), m_sCorpID,
				m_sUserID);*/
		getAuditDlg().setVO((SpecialBillVO)m_voBill.clone(), alInGeneralVO, alOutGeneralVO,
				m_sBillTypeCode, m_voBill.getPrimaryKey().trim(), m_sCorpID,
				m_sUserID);

		// ���õ�����������ͳ����ܳ�Ӧ������ add by hanwei 2004-6-5
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
			// int ret= getAuditDlg("ת��/ת����", alInGeneralVO,
			// alOutGeneralVO).showModal();

			// if (ret == nc.ui.pub.beans.UIDialog.ID_OK) {
			
				// ���±�β
				setAdjustBillFlag();
				filterNullLine();
				// ����PK
				String sBillPK = m_voBill.getPrimaryKey();
				// ˢ��ts
				freshTs(qryLastTs(sBillPK));
				
				/**add by ouyangzhb 2012-04-25 �����̵��¼�����뵥��ϸ*/
				if(voNowBill.getHeaderVO().getIcheckmode() == CheckMode.md){
					createMDcrk(m_voBill);
				}
				/**add by ouyangzhb 2012-04-25 �����̵��¼�����뵥��ϸ*/
				
				m_alListData.set(m_iLastSelListHeadRow, m_voBill.clone());

				m_iFirstSelListHeadRow = -1;
				switchListToBill();

				setButtonState();
				setBillState();
				showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"4008spec", "UPP4008spec-000199")/* @res "���������ɣ�" */);
				return;

			
		}
		} catch (Exception e) {
			handleException(e);
			nc.ui.pub.beans.MessageDialog.showErrorDlg(this,
					nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec",
							"UPPSCMCommon-000059")/* @res "����" */, e
							.getMessage());
		}
		showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec",
				"UPP4008spec-000200")/* @res "�������ȡ����" */);
	}

	/**
	 * �����ߣ������� ���ܣ�ȡ������(Ӧ�����ε���BO��Ϊһ�ε���) ������ ���أ� ���⣺ ���ڣ�(2001-5-10 ���� 2:50)
	 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 */
	public void onCancelAdjust() {
		switch (nc.ui.pub.beans.MessageDialog.showYesNoDlg(this, null,
				nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UCH067")/*
																				 * @res
																				 * "�Ƿ�ȷ��Ҫȡ����"
																				 */
				, MessageDialog.ID_NO)) {

		case nc.ui.pub.beans.MessageDialog.ID_YES:
			break;

		default:
			return;
		}

		showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec",
				"UPP4008spec-000201")/* @res "ȡ���������" */);
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

			// ���±�β
			clearAdjustBillFlag();
			filterNullLine();
			// ����PK
			String sBillPK = vo.getPrimaryKey();
			// ˢ��ts
			freshTs(qryLastTs(sBillPK));
			m_alListData.set(m_iLastSelListHeadRow, m_voBill.clone());
			m_iFirstSelListHeadRow = -1;
			switchListToBill();

			setButtonState();
			setBillState();
			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"4008spec", "UPP4008spec-000202")/* @res "ȡ����������ɹ���" */);

		} catch (Exception e) {
			nc.vo.scm.pub.SCMEnv.out("����ͨѶʧ�ܣ�");
			handleException(e);
			nc.ui.pub.beans.MessageDialog.showErrorDlg(this,
					nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec",
							"UPPSCMCommon-000059")/* @res "����" */, e
							.getMessage());
		}
	}

	// �Ƿ������̵�
	private UFBoolean m_iscountflag = UFBoolean.FALSE;
	private HashMap<String, WhVO> hm_whid_whvo = new HashMap<String, WhVO>();

	/**
	 * �̵㷽ʽѡ���Ѿ����ش������
	 */
	public void onCheckChoose() {
		try {
			Timer t = new Timer();
			// �̵�ѡ��
			long lTimeBegin = System.currentTimeMillis();
			long lTime = System.currentTimeMillis();
			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"4008spec", "UPT40081016-000035")/* @res "�̵�ѡ��" */);
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
				// ��ʼ�������������Ҫһ��vo
				onAdd();

				m_voBill = new SpecialBillVO(getBillCardPanel().getRowCount());
				getBillCardPanel().getBillValueVO(m_voBill);

				// ����̵㷽ʽ����������Model��m_voBill��
				int radFlag = ((Integer) ((ArrayList) alCheckChoose.get(1))
						.get(0)).intValue();
				// �޸��ˣ������� ����10:25:44_2009-10-31 �޸�ԭ��: ����ǿ��Ա�̵㣬�򲹱�ͷ���Ա
			   if(radFlag==CheckMode.Keeper){ //����Ա�̵�
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
			
				// �����̵㷽ʽ��ѯ���ݿ�
				lTime = System.currentTimeMillis();
				// �Ƿ��ջ�����ά��ȡ��
				// alCheckChoose.add(new Boolean(isHslConfig()));
				HashMap<String,Object> hmRet = SpecialHHelper.queryCheckChooseInfo_NEW(
            m_sCorpID, alCheckChoose, m_sUserID,alCheckChoose.get(0).toString().trim());
				ArrayList alInvResult = (ArrayList)hmRet.get(IICParaConst.CheckInvsPara);
				WhVO whvo = (WhVO)hmRet.get(IICParaConst.CheckWareHousePara);
				if(null != whvo && !hm_whid_whvo.containsKey(whvo.getCwarehouseid()))
				  hm_whid_whvo.put(whvo.getCwarehouseid(), whvo);
//				ArrayList alInvResult = SpecialHHelper.queryCheckChooseInfo(
//						m_sCorpID, alCheckChoose, m_sUserID);
				showTime(lTime, "qry invs��");
				// û�鵽�κ�����
				if (alInvResult == null || alInvResult.size() == 0) {
					showHintMessage(nc.ui.ml.NCLangRes.getInstance()
							.getStrByID("4008spec", "UPP4008spec-000203")/*
																			 * @res
																			 * "�޴����Ϣ��"
																			 */);
					m_voBill.setChildrenVO(null);
					getBillCardPanel().setBillValueVO(m_voBill);
					getBillCardPanel().updateValue();
					dispBodyRow(getBillCardPanel().getBillTable());

					return;
				}

				getBillCardPanel().getBillModel().setNeedCalculate(false);
				// �òֿ�
				getBillCardPanel().setHeadItem("coutwarehouseid",
						alCheckChoose.get(0).toString().trim());
				lTime = System.currentTimeMillis();
				afterWhOutEdit(false);
				showTime(lTime, "�òֿ��ѯ��");

				int rowcount = getBillCardPanel().getRowCount();

				lTime = System.currentTimeMillis();
				if (alInvResult.size() > rowcount) {
					getBillCardPanel().getBodyPanel().addLine(
							alInvResult.size() - rowcount);
				}
				showTime(lTime, "addLine��");
				m_timer.start();
				rowcount = alInvResult.size();
				// ������������ݡ�
				lTime = System.currentTimeMillis();
				 if(radFlag==CheckMode.md){
					 afterInvsEdit_MD(alInvResult);
				 }else{
						afterInvsEdit(alInvResult);
				 }
			
				showTime(lTime, "afterInvsEdit_MD��");
				// ��λ�̵���Ҫִ�л�λ��ʽ
				lTime = System.currentTimeMillis();
				// if (radFlag == CheckMode.Space) {
				getBillCardPanel().getBillModel().execLoadFormulaByKey(
						"cspacename");
				//getBillCardPanel().getBillModel().execFormulasWithVO(m_voBill.getItemVOs(), getBillCardPanel().getBodyItem("cspacename").getLoadFormula());
				getBillCardPanel().getBillModel().execLoadFormulaByKey(
						"cvendorname");
				//getBillCardPanel().getBillModel().execFormulasWithVO(m_voBill.getItemVOs(), getBillCardPanel().getBodyItem("cvendorname").getLoadFormula());

				// }
				showTime(lTime, "execLoadFormulaByKey��");
				// filterNullLine();
				m_timer.stopAndShow("���湫ʽ");
				getBillCardPanel().getBillModel().setNeedCalculate(true);
				
//				 �������������
				getBillCardPanel().tableStopCellEditing();
				getBillCardPanel().stopEditing();

				// �˵����еĿ���
				filterNullLine();
				//����������ϵ�һ��
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
					"SCMCOMMON", "UPPSCMCommon-000133")/* @res "����" */);
		} catch (Exception e) {
			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"4008spec", "UPP4008spec-000137")/* @res "δ����ɣ����������ԣ�" */);
			handleException(e);
			nc.ui.pub.beans.MessageDialog.showErrorDlg(this,
					nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec",
							"UPPSCMCommon-000059")/* @res "����" */, e
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
	 * �����ߣ����˾� ���ܣ����õ����� ������ ���أ� ���⣺ ���ڣ�(2001-5-9 9:23:32) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
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
	 * �����ߣ����˾� ���ܣ����������ˡ� ������ ���أ� ���⣺ ���ڣ�(2001-5-9 9:23:32) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
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
	 * �����ߣ����˾� ���ܣ����ݱ༭�¼����� ������e���ݱ༭�¼� ���أ� ���⣺ ���ڣ�(2001-5-8 19:08:05)
	 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 * 
	 * 
	 * 
	 */
	public void afterAstUOMEdit(int rownow) {
		// ��������λ
		String sAstUomPK = ((nc.ui.pub.beans.UIRefPane) getBillCardPanel()
				.getBodyItem("castunitname").getComponent()).getRefPK();
		String sAstUomname = ((nc.ui.pub.beans.UIRefPane) getBillCardPanel()
				.getBodyItem("castunitname").getComponent()).getRefName();
		m_voBill.setItemValue(rownow, "castunitid", sAstUomPK);
		m_voBill.setItemValue(rownow, "castunitname", sAstUomname);
		getBillCardPanel().setBodyValueAt(sAstUomPK, rownow, "castunitid");
		getBillCardPanel().setBodyValueAt(sAstUomname, rownow, "castunitname");

		// �������������б���ʽ����ʾ��
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
	 * �˴����뷽��˵���� �����ߣ����� ���ܣ� ������ ���أ� ���⣺ ���ڣ�(2001-6-20 21:43:07)
	 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
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
	 * �����ߣ������� ���ܣ��ɴ���ĵ������͡��ֶΣ���õ����ֶθı��Ӧ�ı�������ֶ��б� ������iBillFlag
	 * �������ͣ���Ϊ��ͨ���ݣ�����0����Ϊ���ⵥ�ݣ�����1 ���� ��� cinventorycode�� ����ֿ� cwarehousename�� ������
	 * vfree0�� ��ͷ����ֿ� coutwarehouseid�� ��ͷ�ֿ� cwarehouseid ���أ� ���⣺ ���ڣ�(2001-7-18
	 * ���� 9:20) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
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
			// ���
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
			sReturnString[i++] = "nadjustnum"; // ��������
			sReturnString[i++] = "nadjustastnum"; // ����������
			sReturnString[i++] = "naccountgrsnum";
			sReturnString[i++] = "ncheckgrsnum";
			sReturnString[i++] = "nadjustgrsnum";
			sReturnString[i++] = "ndiffgrsnum";

		} else if (sWhatChange.equals("vfree0")
				|| sWhatChange.equals("cvendorname")
				|| sWhatChange.equals("hsl")) {
			// ������
			sReturnString = new String[12];
			int i = 0;
			sReturnString[i++] = "naccountnum";
			sReturnString[i++] = "naccountastnum";
			sReturnString[i++] = "cysl";
			sReturnString[i++] = "cyfsl";
			sReturnString[i++] = m_sNumItemKey;
			sReturnString[i++] = m_sAstItemKey;
			sReturnString[i++] = "je";
			sReturnString[i++] = "nadjustnum"; // ��������
			sReturnString[i++] = "nadjustastnum"; // ����������
			sReturnString[i++] = "naccountgrsnum";
			sReturnString[i++] = "nadjustgrsnum";
			sReturnString[i++] = "ndiffgrsnum";

			// showWarningMessage("������ȷ�����κţ���ִ������ȡ����");
			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"4008spec", "UPP4008spec-000204")/*
														 * @res
														 * "�������޸ģ�������ȷ�����κţ���ִ������ȡ��!"
														 */);
			return sReturnString;
		} else if (sWhatChange.equals("vbatchcode")) {
			// ���κ�
			sReturnString = new String[12];
			int i = 0;
			sReturnString[i++] = "naccountnum";
			sReturnString[i++] = "naccountastnum";
			sReturnString[i++] = "cysl";
			sReturnString[i++] = "cyfsl";
			sReturnString[i++] = m_sNumItemKey;
			sReturnString[i++] = m_sAstItemKey;
			sReturnString[i++] = "je";
			sReturnString[i++] = "nadjustnum"; // ��������
			sReturnString[i++] = "nadjustastnum"; // ����������
			sReturnString[i++] = "naccountgrsnum";
			sReturnString[i++] = "nadjustgrsnum";
			sReturnString[i++] = "ndiffgrsnum";

			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"4008spec", "UPP4008spec-000205")/*
														 * @res
														 * "���κ��޸ģ����ٴ�ִ������ȡ��!"
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
			sReturnString[i++] = "nadjustnum"; // ��������
			sReturnString[i++] = "nadjustastnum"; // ����������
			sReturnString[i++] = "scpacename";
			sReturnString[i++] = "scpaceid";
			sReturnString[i++] = "naccountgrsnum";
			sReturnString[i++] = "nadjustgrsnum";
			sReturnString[i++] = "ndiffgrsnum";

			// showWarningMessage("������ȷ�����κţ���ִ������ȡ����");
			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"4008spec", "UPP4008spec-000206")/*
														 * @res
														 * "�ֿ��޸ģ���ȷ�ϱ����и�����ٴ�ִ������ȡ��!"
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
			sReturnString[i++] = "nadjustnum"; // ��������
			sReturnString[i++] = "nadjustastnum"; // ����������
			sReturnString[i++] = "naccountgrsnum";
			sReturnString[i++] = "ncheckgrsnum";
			sReturnString[i++] = "nadjustgrsnum";
			sReturnString[i++] = "ndiffgrsnum";

			// showWarningMessage("������ȷ�����κţ���ִ������ȡ����");
			return null;
		} else if (sWhatChange.equals("castunitname")) {
			// ������
			sReturnString = new String[12];
			int i = 0;
			sReturnString[i++] = m_sNumItemKey;
			sReturnString[i++] = m_sAstItemKey;
			sReturnString[i++] = "naccountnum";
			sReturnString[i++] = "naccountastnum";
			sReturnString[i++] = "cysl";
			sReturnString[i++] = "cyfsl";
			sReturnString[i++] = "je";
			sReturnString[i++] = "nadjustnum"; // ��������
			sReturnString[i++] = "nadjustastnum"; // ����������
			sReturnString[i++] = "naccountgrsnum";
			sReturnString[i++] = "nadjustgrsnum";
			sReturnString[i++] = "ndiffgrsnum";

			// showWarningMessage("������ȷ�����κţ���ִ������ȡ����");
			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"4008spec", "UPP4008spec-000207")/*
														 * @res
														 * "�������޸ģ����ٴ�ִ������ȡ��!"
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

			// showWarningMessage("������ִ������ȡ����");
			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"4008spec", "UPP4008spec-000208")/*
														 * @res
														 * "��λ�޸ģ����ٴ�ִ������ȡ��!"
														 */);
			return sReturnString;
		}
		return sReturnString;
	}

	/**
	 * ClientUI ������ע�⡣ nc 2.2 �ṩ�ĵ������鹦�ܹ����ӡ�
	 * 
	 */
	public ClientUI(String pk_corp, String billType, String businessType,
			String operator, String billID) {

		super();
		nc.vo.scm.pub.SCMEnv.out("+++ydy++ClientUI+operator:" + operator);
		m_sBillTypeCode = nc.vo.ic.pub.BillTypeConst.m_check;
		// ���õĸ����ڵ��BO_Client�ӿ�,Ӧ�������޸�
		// sSpecialHBO_Client = "nc.ui.ic.ic261.SpecialHBO_Client";
		m_sPNodeCode = "40081016";
		// ��ʼ������
		initialize(pk_corp, operator, "jc", businessType, "0001", "2003-04-17");
		// �鵥��
		SpecialBillVO voBill = qryBill(pk_corp, m_sBillTypeCode, businessType,
				operator, billID);
		if (voBill == null)
			nc.ui.pub.beans.MessageDialog.showHintDlg(this,
					nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec",
							"UPPSCMCommon-000270")/* @res "��ʾ" */,
					nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec",
							"UPP4008spec-000121")/* @res "û�з��ϲ�ѯ�����ĵ��ݣ�" */);
		else
			// ��ʾ����
			setBillValueVO(voBill);

	}

	/**
	 * �����ߣ� ���ܣ� ������e���ݱ༭�¼� ���أ� ���⣺ ���ڣ�(2001-5-8 19:08:05) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 * 
	 * 
	 * 
	 */
	public void afterDshldtransnumEdit() {
		// �׼����������������
		try {
			if (null != m_voBill.getItemValue(0, "dshldtransnum")) {
				// �õ��ܼ���
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
							// ��ÿһ�м�������
							UFDouble childs = new UFDouble("0");
							if (m_voBill.getItemValue(i, "childsnum") != null) {
								childs = new UFDouble(m_voBill.getItemValue(i,
										"childsnum").toString());
							}
							UFDouble value = childs.multiply(iBaseNum);
							m_voBill.setItemValue(i, "dshldtransnum", value);
							getBillCardPanel().setBodyValueAt(value, i,
									"dshldtransnum");

							// ���㵥��
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

							// ִ�б༭��ʽ�����㸨���������ƻ����
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
	 * �����ߣ������� ���ܣ��������޸� ������ ���أ� ���⣺ ���ڣ�(2001-11-20 14:01:52) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
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
	 * �����ߣ����˾� ���ܣ�����¼�����,ֻ���̵�ѡ���ʹ�� ������e���ݱ༭�¼� ���أ� ���⣺ ���ڣ�(2001-5-8 19:08:05)
	 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 */
	public ArrayList afterInvsEdit(ArrayList alInvvo) {
		try {
			if (alInvvo == null || alInvvo.size() == 0)
				return null;
			nc.vo.scm.pub.SCMEnv.out("��������ݡ�������");

			long lTimeBegin = System.currentTimeMillis();
			long lTime = System.currentTimeMillis();
			ArrayList alInvFullInfo = new ArrayList();
			String sTempID2 = null;
			if (getBillCardPanel().getHeadItem("coutwarehouseid") != null)
				sTempID2 = getBillCardPanel().getHeadItem("coutwarehouseid")
						.getValue();

			// repair by hanwei 2003-2-18 ����ʽ����
			nc.vo.scm.ic.bill.InvVO[] voInvs = null;
			if (isByFormula()) {
				lTime = System.currentTimeMillis();
				String sWarehouseid = ((nc.ui.pub.beans.UIRefPane) getBillCardPanel()
						.getHeadItem("coutwarehouseid").getComponent())
						.getRefPK();
				// ͨ���ֿ��ÿ����֯ID
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
				// �޸� hanwei 2003-07-04
				int iLen = alInvvo.size();
				voInvs = new InvVO[iLen];
				alInvvo.toArray(voInvs);
				voInvs = invoInfoBYFormula.getInvParseWithPlanPrice(voInvs,
						sWarehouseid, sCalID, false, true);
				nc.vo.scm.pub.SCMEnv.showTime(lTime, "�ٴι�ʽ��ѯ�������ʱ�䣺");

			} else {
				lTime = System.currentTimeMillis();
				String[] saInvCode = new String[alInvvo.size()];
				for (int i = 0; i < alInvvo.size(); i++) {
					saInvCode[i] = (String) ((InvVO) alInvvo.get(i))
							.getAttributeValue("cinventoryid");
				}
				voInvs = SpecialHHelper.queryInvsInfo(sTempID2, saInvCode,
						m_sUserID, m_sCorpID);
				// reportTime(lTime, "�ٴ�DB��ѯ�������ʱ�䣺");
			}

			InvVO voInv = null;
			UFDate productDate = null;
			lTime = System.currentTimeMillis();
			for (int row = 0; row < voInvs.length; row++) {
				voInv = voInvs[row];
				// ����������
				voInv.calPrdDate();
				m_voBill.setItemInv(row, voInv);
				if (voInv.getCvendorid() != null)
					m_voBill.setItemValue(row, "cvendorid", voInv
							.getCvendorid());// zhy2006-11-28����ΧΪ��Ӧ�̸�ֵ,�����̵�ѡ��������û�й�Ӧ�̵�ֵ
				m_voBill.getItemVOs()[row].setCrowno(null);
				alInvFullInfo.add(voInv);
				// //����
				// setBodyInvValue(row, voInv);
			}
			nc.vo.scm.pub.SCMEnv.showTime(lTime, "����������");

			lTime = System.currentTimeMillis();
			try {
				// �����+���κ�+��λid+��Ӧ��id+hsl,����
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

			nc.vo.scm.pub.SCMEnv.showTime(lTime, "�����+���κ�+��λid,����");

			lTime = System.currentTimeMillis();
			// ���ӵ����кţ�zhx added on 20030630 support for incoming bill
			nc.ui.scm.pub.report.BillRowNo.setVORowNoByRule(m_voBill,
					m_sBillTypeCode, m_sBillRowNo);
			// /

			nc.vo.scm.pub.SCMEnv.showTime(lTime, "�к�");
			lTime = System.currentTimeMillis();
			// ����Ҫִ�н��湫ʽ
			setBillValueVO(m_voBill, false);
			nc.vo.scm.pub.SCMEnv.showTime(lTime, "setBillValueVO(m_voBill);");
			// ���µ���Ϣ��Ҫ�Ż���������κ�δ��ʾ����������ʾ��
			if (isByFormula()) {
				showTime(lTimeBegin, "���VO�ù�ʽ��ѯ����ʱ�䣺");
			} else {
				showTime(lTimeBegin, "���VO��DB��ѯ����ʱ�䣺");
			}

			// showHintMessage("����޸ģ�������ȷ����������Ρ�������");
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
			nc.vo.scm.pub.SCMEnv.out("��������ݡ�������");

			long lTimeBegin = System.currentTimeMillis();
			long lTime = System.currentTimeMillis();
			ArrayList alInvFullInfo = new ArrayList();
			String sTempID2 = null;
			if (getBillCardPanel().getHeadItem("coutwarehouseid") != null)
				sTempID2 = getBillCardPanel().getHeadItem("coutwarehouseid")
						.getValue();

			// repair by hanwei 2003-2-18 ����ʽ����
			nc.vo.scm.ic.bill.InvVO[] voInvs = null;
			if (isByFormula()) {
				lTime = System.currentTimeMillis();
				String sWarehouseid = ((nc.ui.pub.beans.UIRefPane) getBillCardPanel()
						.getHeadItem("coutwarehouseid").getComponent())
						.getRefPK();
				// ͨ���ֿ��ÿ����֯ID
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
				// �޸� hanwei 2003-07-04
				int iLen = alInvvo.size();
				voInvs = new InvVO[iLen];
				alInvvo.toArray(voInvs);
				voInvs = invoInfoBYFormula.getInvParseWithPlanPrice(voInvs,
						sWarehouseid, sCalID, false, true);
				nc.vo.scm.pub.SCMEnv.showTime(lTime, "�ٴι�ʽ��ѯ�������ʱ�䣺");

			} else {
				lTime = System.currentTimeMillis();
				String[] saInvCode = new String[alInvvo.size()];
				for (int i = 0; i < alInvvo.size(); i++) {
					saInvCode[i] = (String) ((InvVO) alInvvo.get(i))
							.getAttributeValue("cinventoryid");
				}
				voInvs = SpecialHHelper.queryInvsInfo(sTempID2, saInvCode,
						m_sUserID, m_sCorpID);
				// reportTime(lTime, "�ٴ�DB��ѯ�������ʱ�䣺");
			}

			InvVO voInv = null;
			UFDate productDate = null;
			lTime = System.currentTimeMillis();
			for (int row = 0; row < voInvs.length; row++) {
				voInv = voInvs[row];
				// ����������
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
							.getCvendorid());// zhy2006-11-28����ΧΪ��Ӧ�̸�ֵ,�����̵�ѡ��������û�й�Ӧ�̵�ֵ
				m_voBill.getItemVOs()[row].setCrowno(null);
				alInvFullInfo.add(voInv);
				// //����
				// setBodyInvValue(row, voInv);
			}
			nc.vo.scm.pub.SCMEnv.showTime(lTime, "����������");

			lTime = System.currentTimeMillis();
			try {
				// �����+���κ�+��λid+��Ӧ��id+hsl,����
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

			nc.vo.scm.pub.SCMEnv.showTime(lTime, "�����+���κ�+��λid,����");

			lTime = System.currentTimeMillis();
			// ���ӵ����кţ�zhx added on 20030630 support for incoming bill
			nc.ui.scm.pub.report.BillRowNo.setVORowNoByRule(m_voBill,
					m_sBillTypeCode, m_sBillRowNo);
			// /

			nc.vo.scm.pub.SCMEnv.showTime(lTime, "�к�");
			lTime = System.currentTimeMillis();
			// ����Ҫִ�н��湫ʽ
			setBillValueVO(m_voBill, false);
			nc.vo.scm.pub.SCMEnv.showTime(lTime, "setBillValueVO(m_voBill);");
			// ���µ���Ϣ��Ҫ�Ż���������κ�δ��ʾ����������ʾ��
			if (isByFormula()) {
				showTime(lTimeBegin, "���VO�ù�ʽ��ѯ����ʱ�䣺");
			} else {
				showTime(lTimeBegin, "���VO��DB��ѯ����ʱ�䣺");
			}

			// showHintMessage("����޸ģ�������ȷ����������Ρ�������");
			return alInvvo;
		} catch (Exception e2) {
			nc.vo.scm.pub.SCMEnv.error(e2);
		}
		return null;
	}

	

	/**
	 * �����ߣ������� ���ܣ���λ�޸��¼����� ������e���ݱ༭�¼� ���أ� ���⣺ ���ڣ�(2001-5-8 19:08:05)
	 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 * 
	 * 
	 * 
	 */
	public void afterSpaceEdit(nc.ui.pub.bill.BillEditEvent e) {
		// ����λ����
		nc.ui.pub.beans.UIRefPane refOutSpace = null;
		if (getBillCardPanel().getBodyItem("cspacename") != null)
			refOutSpace = ((nc.ui.pub.beans.UIRefPane) getBillCardPanel()
					.getBodyItem("cspacename").getComponent());
		// �޸���Key
		String sItemKey = e.getKey();
		// ��ǰ��
		int row = e.getRow();
		String cOutsname = refOutSpace.getRefName();
		String cOutspaceid = refOutSpace.getRefPK();
		// ���ID�ǿ������name
		if (cOutspaceid == null)
			cOutsname = null;
		// String cOutsname = refOutSpace.getRefName();
		// String cOutspaceid = refOutSpace.getRefPK();
		// String cInsname =refInSpace.getRefName();
		// String cInspaceid = refInSpace.getRefPK();
		// ������λ������ͬ
		showHintMessage("");
		// ����m_vo
		m_voBill.setItemValue(row, "cspaceid", cOutspaceid);
		m_voBill.setItemValue(row, "cspacename", cOutsname);

		// �������
		getBillCardPanel().setBodyValueAt(cOutsname, row, "cspacename");
		getBillCardPanel().setBodyValueAt(cOutspaceid, row, "cspaceid");

		String[] sIKs = getClearIDs(1, "cspaceid");
		// int iRowCount= getBillCardPanel().getRowCount();
		// for (int rownow= 0; rownow < iRowCount; rownow++)
		clearRowData(row, sIKs);
		// ���µ���Ϣ��Ҫ�Ż���������κ�δ��ʾ����������ʾ��
		showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec",
				"UPP4008spec-000209")/* @res "��λ�޸ģ�����������ȡ����" */);
	}

	// private boolean m_bHslWeiDu = false;//hsl�Ƿ���Ϊȡ��ά��

	// /**
	// * ��ý����Ƿ�hsl��Ϊγ��
	// *
	// * @param bWarehouseRefEnable
	// */
	// private boolean isHslConfig() {
	//
	// return m_bHslWeiDu;
	//
	// }

	/**
	 * �����ߣ������� ���ܣ��ֿ�ı��¼����� ������e���ݱ༭�¼� ���أ� ���⣺ ���ڣ�(2001-5-8 19:08:05)
	 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 * bWarehouseRefEnable:�༭�󣬲ֿ�����Ƿ���� �����̵�ѡ�񣬱༭�󣬲ֿ���ղ�����
	 * 
	 * 
	 */
	public void afterWhOutEdit(boolean bWarehouseRefEnable) {
		afterWhOutEditByDBQury(bWarehouseRefEnable);
	}

	/**
	 * �����ߣ������� ���ܣ��ֿ�ı��¼����� ������e���ݱ༭�¼� ���أ� ���⣺ ���ڣ�(2001-5-8 19:08:05)
	 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־�� bWarehouseRefEnable:�༭�󣬲ֿ�����Ƿ���� �����̵�ѡ�񣬱༭�󣬲ֿ���ղ�����
	 * 
	 * 
	 * 
	 * 
	 */
	public void afterWhOutEditByDBQury(boolean bWarehouseRefEnable) {
		// �ֿ�
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
				// ���β�ִ���
				m_voBill.clearInvQtyInfo();
				// ������/������
				String[] sIKs = getClearIDs(1, "coutwarehouseid");
				int iRowCount = getBillCardPanel().getRowCount();
				for (int row = 0; row < iRowCount; row++)
					clearRowData(row, sIKs);
				// ˢ���ִ�����ʾ
				// setTailValue(0);

				// ���������������
				// for (int i= 0; i < getBillCardPanel().getRowCount(); i++) {
				// clearRowData(i);
				// }
				// ���µ���Ϣ��Ҫ�Ż���������κ�δ��ʾ����������ʾ��
				showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"4008spec", "UPP4008spec-000210")/*
															 * @res
															 * "����ֿ��޸ģ�������ȷ����������Ρ�������"
															 */);
				// �����̵�ѡ�񣬱༭�󣬲ֿ���ղ�����
				getBillCardPanel().getBillData().getHeadItem("coutwarehouseid")
						.setEnabled(false);
				// �������������б���ʽ����ʾ��
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
	 * �����ߣ������� ���ܣ��ֿ�ı��¼����� ������e���ݱ༭�¼� ���أ� ���⣺ ���ڣ�(2001-5-8 19:08:05)
	 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 * 
	 * 
	 * 
	 */
	public void afterWhOutEditByFormula() {
		// �ֿ�
		try {
			String sName = ((nc.ui.pub.beans.UIRefPane) getBillCardPanel()
					.getHeadItem("coutwarehouseid").getComponent())
					.getRefName();
			String sID = ((nc.ui.pub.beans.UIRefPane) getBillCardPanel()
					.getHeadItem("coutwarehouseid").getComponent()).getRefPK();

			getBillCardPanel().getBillData().getHeadItem("coutwarehouseid")
					.setEnabled(false);
			// �������������б���ʽ����ʾ��

			nc.vo.scm.ic.bill.WhVO voWh = (nc.vo.scm.ic.bill.WhVO) SpecialBillHelper
					.queryInfo(new Integer(1), sID);
			if (m_voBill != null) {
				m_voBill.setWhOut(voWh);
				// ���β�ִ���
				m_voBill.clearInvQtyInfo();
				// ������/������
				String[] sIKs = getClearIDs(1, "coutwarehouseid");
				int iRowCount = getBillCardPanel().getRowCount();
				for (int row = 0; row < iRowCount; row++)
					clearRowData(row, sIKs);
				// ˢ���ִ�����ʾ
				// setTailValue(0);

				// ���������������
				// for (int i= 0; i < getBillCardPanel().getRowCount(); i++) {
				// clearRowData(i);
				// }
				// ���µ���Ϣ��Ҫ�Ż���������κ�δ��ʾ����������ʾ��
				showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"4008spec", "UPP4008spec-000210")/*
															 * @res
															 * "����ֿ��޸ģ�������ȷ����������Ρ�������"
															 */);
				getBillCardPanel().getBillData().getHeadItem("coutwarehouseid")
						.setEdit(false);
			}
		} catch (Exception e2) {
			nc.vo.scm.pub.SCMEnv.error(e2);
		}

	}

	/**
	 * �༭ǰ���� �������ڣ�(2001-3-23 2:02:27)
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
			// ����ȡ������Ȼ�������У��޸ģ��������������ȡ��
			if (sItemKey.equals("naccountnum")
					|| sItemKey.equals("naccountastnum")) {
				return false;
			}

			// 2003-09-03 ydy���������ȡ��״̬,ֻ���޸��̵������͸������Ͳ�������������
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
			// isEditable = false; //�̵㵥�ϵı����ڲ����Ա༭
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

			// �����κŴ��ݲ���
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
	 * ˵����� ���������Լ������ʵĹ�ϵ���� 1��������С��0 2�������ʴ���0 �̶�������ʱ�������������ݻ����ʻ��㡣
	 * �䶯������ʱ���������޸ģ����ݸ����������¼��㻻���ʣ��������޸ģ����ݻ��������¼��������� �����ʱ仯�����ݸ����������������� ���ȼ���
	 * ������>������>����
	 * 
	 * ��ʽ��������=������*������ ������ iWhichChanged: 0 �������޸� 1 �������޸�
	 * 
	 * ����������ʱ������л����� �� ����/������ �� �������͸���������Ϊ���һ����ʷǿ�ʱ�����ݷǿ�ֵ�����ֵ��
	 * 
	 * bHsl�Ƿ�Ҫ�޸Ļ�����,����������������϶�,ͨ����Ϊ��һ�ε��ø÷���ʱ��Ϊtrue,�������ĵ�������Ϊfalse
	 */
	protected void calculateByHsl(int iRow, String sMainNum, String sAstNum,
			int iWhichChanged, boolean bHsl) {

		UFDouble hsl = getUFValueByItemKey(iRow, "hsl");

		// �����ʴ�����
		UFDouble ufdMainNum = null;
		UFDouble ufdAstNum = null;
		ufdMainNum = getUFValueByItemKey(iRow, sMainNum);
		ufdAstNum = getUFValueByItemKey(iRow, sAstNum);
		// �̶������ʸ��������޸ģ����ݸ��������ͻ����ʼ���������
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
			// �䶯�����ʣ�������>������>������
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
			// �������ı�(ֻ�������ͻ����ʣ����ܴ�����������
			else if (iWhichChanged == 0) {
				// ��������Ϊ��
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
				// ����������Ϊ��
				else {
					// �˴�������ȥ��,���ڵ��ô�,��bHsl��Ϊfalse
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
					// //�������������޸Ĳ����޸Ļ����ʣ���Ϊ���������޸Ĳ���Ӱ�������͸�������
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
	 * �����ߣ������� ���ܣ������������ ������ ���أ� ���⣺ ���ڣ�(2001-10-26 9:09:14) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 * @param row
	 *            int
	 */
	protected void calculateTzsl(int row) {
		// if ((getBillCardPanel().getBodyValueAt(row, "nadjustnum") == null)
		// || (getBillCardPanel().getBodyValueAt(row,
		// "nadjustnum").toString().trim().length()
		// == 0)) {
		// �Զ�����������
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
		// �Զ����㸨����
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

		// �Զ��������ë��
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
	 * �����ߣ������� ���ܣ� ������ ���أ� ���⣺ ���ڣ�(2001-8-16 12:53:03) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
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
			} else if (sName.equals("cgeneralhid")) { // ���ͷ����PK
				gbvo.setHeaderValue(sName, "");
			} else if (sName.equals("vbillcode")) { // ������ݺ�
				gbvo.setHeaderValue(sName, "");
			} else if (sName.equals("coperatorid")) { // ����Ա
				gbvo.setHeaderValue(sName, m_sUserID);
			} else if (sName.equals("coperatorname")) { // ����Ա
				gbvo.setHeaderValue(sName, m_sUserName);
			} else if (sName.equals("cauditorid")) { // �����ˣ���ͨ�������ڴ�����
				gbvo.setHeaderValue(sName, null);
			} else if (sName.equals("cauditorname")) { // �����ˣ���ͨ�������ڴ�����
				gbvo.setHeaderValue(sName, null);
			} else if (sName.equals("vadjuster")) { // �����ˣ���ͨ�������ڴ�����
				gbvo.setHeaderValue(sName, null);
			} else if (sName.equals("vadjustername")) { // �����ˣ���ͨ�������ڴ�����
				gbvo.setHeaderValue(sName, null);
				// ���Ա wnj:2003-07-29��
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
	 * �����ߣ������� ���ܣ� ������ ���أ� ���⣺ ���ڣ�(2001-8-16 12:53:03) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
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
			} else if (sName.equals("cgeneralbid")) { // ���������PK
				gbvo.setItemValue(row, sName, "");
			} else if (sName.equals("dbizdate")) { // ������������
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

		// �йػ�λVO
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
	 * ������ ���ܣ� ������ ���أ� ���⣺ ���ڣ�(2004-12-3 17:21:30) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 * @return boolean
	 * @param voaCond
	 *            nc.vo.pub.query.ConditionVO[]
	 */

	/**
	 * �˴����뷽��˵���� ��������: //�����ж��Ƿ��е������ݵĲ��� //����м��� //���򣬲������жϲ���
	 * 
	 * //����������VO����ȷ�� �����ڵĴ�������Զ���գ����Բ���У�� // ����������λ�Ƿ���� ��ô��PK+��������PK
	 * hashtable:getAssitUnitHashtable(ArrayList alInvID);
	 * 
	 * �жϸ���������λ�Ƿ���� // ���κ��Ƿ���� ��ô��PK+���κ�HashTable
	 * 
	 * �ж����κ��Ƿ���� // ��λ�Ƿ����Ӧ�ֿ�һ�� ��1�� ��òֿ�PK ��2�� ��ñ���˾�����ֿ������еĻ�λ���ù�ʽ�ӻ�λ������������
	 * getCargdocHashtable(String sPk_stordoc);
	 * 
	 * ��3�� �жϻ�λ�Ƿ����hashtable��
	 * 
	 * �������: ����ֵ: �쳣����: ����:
	 * 
	 * @return java.lang.String
	 */
	private String checkImportBodyVO() {
		String sError = "";

		// ���PK+��������PK hashtable
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
				// Ŀǰ�жϴ���Ƿ���ڵ�������������ã�֮ǰ�Ļ�����빦�ܣ��Զ����û�д��ID����
				// if (oCinventoryid == null)
				// sbError.append("�����" + cinventorycode + "������; ");

				if (oCastunitname != null && oCastunitid == null) {
					castunitname = (String) oCastunitname;
					sOneLineErro = sOneLineErro
							+ nc.ui.ml.NCLangRes.getInstance().getStrByID(
									"4008spec", "UPP4008spec-000211")/*
																		 * @res
																		 * "�������ڸ���λ��"
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
																			 * "�������ڸ���λ��"
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
																		 * "�������ڻ�λ��"
																		 */
							+ cspacecode;
					isHasError = true;
				}

				if (oBatchcode != null) {
					sBatchcode = (String) oBatchcode;
					if (sBatchcode.length() > 4
							&& nc.ui.ml.NCLangRes.getInstance().getStrByID(
									"4008spec", "UPP4008spec-000213")
							/* @res "������" */.equalsIgnoreCase(
									sBatchcode.substring(0, 3))) {
						sOneLineErro = sOneLineErro
								+ nc.ui.ml.NCLangRes.getInstance().getStrByID(
										"4008spec", "UPP4008spec-000214")/*
																			 * @res
																			 * "�����������κţ�"
																			 */
								+ sBatchcode.substring(4);
						isHasError = true;
					}
				}

				if (isHasError) {
					sbError.append(nc.ui.ml.NCLangRes.getInstance().getStrByID(
							"4008spec", "UPP4008spec-000052")/* @res "�����" */
							+ cinventorycode
							+ ""
							+ sOneLineErro
							+ nc.ui.ml.NCLangRes.getInstance().getStrByID(
									"4008spec", "UPP4008spec-000215")/*
																		 * @res
																		 * "��"
																		 */);
				}
				sCinventoryid = (String) oCinventoryid;
			}
		}
		sError = (sbError.toString()).trim();

		return sError;
	}

	/**
	 * �����ߣ������� ���ܣ�����ָ���ֶεı༭��ʽ ������ ���أ� ���⣺ ���ڣ�(2001-10-26 9:25:22)
	 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
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
	 * ����ǹ̶���λ�Ĵ�������˳�����Ĺ̶���λ ���ߣ����Ӣ �������ڣ�(2001-7-6 16:53:38)
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
		// Ԥ�ȶ�λ
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
	 * ���� QueryConditionClient1 ����ֵ��
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
//			// �޸��ˣ������� �޸����ڣ�2007-10-18����06:40:42
//			// �޸�ԭ�򣺶๫˾ʱ������Ȩ�޶��ڲ����䵽��˾�Ļ��������������⣬����������
//			alCorpIDs.add(m_sCorpID);
//			// ���õ�������Ϊ��ǰ��¼����
//			// modified by liuzy 2008-04-02 v5.03���󣺿�浥�ݲ�ѯ������ֹ����
//			ivjQueryConditionDlg.setInitDate("dbilldate.from", m_sLogDate);
//			ivjQueryConditionDlg.setInitDate("dbilldate.end", m_sLogDate);
//			// ����Ϊ�Բ��յĳ�ʼ��
//			ivjQueryConditionDlg.initQueryDlgRef();
//			ivjQueryConditionDlg.initCorpRef("pk_corp", m_sCorpID, alCorpIDs);
//			//
//			// // �̵㵥�ֿ������Ȩ��
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
//																		 * "��"
//																		 */},
//					{
//							"1",
//							nc.ui.ml.NCLangRes.getInstance().getStrByID(
//									"SCMCOMMON", "UPPSCMCommon-000108") /*
//																		 * @res
//																		 * "��"
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
	 * �����ߣ����˾� ���ܣ������������������ ������ ���أ� ���⣺ ���ڣ�(2001-11-24 12:15:42)
	 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
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
			// showErrorMessage(e.toString()); Ϊʲô��������
			m_dictrl = null;
		}

		return m_dictrl;
	}

	/**
	 * ���� LotNumbRefPane1 ����ֵ��
	 * 
	 * @return nc.ui.ic.pub.lot.LotNumbRefPane
	 */
	/* ���棺�˷������������ɡ� */
	private LocatorRefPane getLocatorRefPane() {
		if (m_refLocator == null) {
			try {
				m_refLocator = new LocatorRefPane(InOutFlag.OUT);
				m_refLocator.setName("LotNumbRefPane");
				m_refLocator.setLocation(38, 1);
				// user code begin {1}
				m_refLocator.setInOutFlag(InOutFlag.OUT);
				// BillData bd = getBillCardPanel().getBillData();
				// ���û�λ����
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
	 * �˴����뷽��˵���� �������ڣ�(2001-12-18 16:59:11)
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
	 * //��������IC028:����ʱ�Ƿ�ָ����ⵥ;���β��ո����Ƿ񵽵��ݺ�. //IC010 �Ƿ񱣴漴ǩ�֡�
	 * 
	 * //�������� ���� ȱʡֵ //BD501 ����С��λ 2 //BD502 ����������С��λ 2 //BD503 ������ 2 //BD504
	 * ����ɱ�����С��λ 2 String[] saParam = new String[] { "IC028", "IC010", "BD501",
	 * "BD502", "BD503", "BD504", "BD301" };
	 * 
	 * //����Ĳ��� ArrayList alAllParam = new ArrayList(); //������ı������� ArrayList
	 * alParam = new ArrayList(); alParam.add(m_sCorpID); //��һ���ǹ�˾
	 * alParam.add(saParam); //����Ĳ��� alAllParam.add(alParam); //���û���Ӧ��˾�ı������
	 * alAllParam.add(m_sUserID);
	 * 
	 * //���ص��������� ArrayList alRetData = (ArrayList) invokeClient("queryInfo", new
	 * Class[] { Integer.class, Object.class }, new Object[] { new
	 * Integer(QryInfoConst.INIT_PARAM), alAllParam });
	 * 
	 * //Ŀǰ�������� if (alRetData == null || alRetData.size() < 2) { return; }
	 * //���صĲ���ֵ String[] saParamValue = (String[]) alRetData.get(0);
	 * //׷�ٵ����ݲ���,Ĭ������Ϊ"N" if (saParamValue != null && saParamValue.length >=
	 * alAllParam.size()) { //if(saParamValue[0]!=null) //m_sTrackedBillFlag =
	 * saParamValue[0].toUpperCase().trim(); //�Ƿ񱣴漴ǩ�֡�Ĭ������Ϊ"N"
	 * //if(saParamValue[1]!=null) //m_sSaveAndSign =
	 * saParamValue[1].toUpperCase().trim(); //BD501 ����С��λ 2 if (saParamValue[2] !=
	 * null) m_iaScale[0] = Integer.parseInt(saParamValue[2]); //BD502 ����������С��λ
	 * 2 if (saParamValue[3] != null) m_iaScale[1] =
	 * Integer.parseInt(saParamValue[3]); //BD503 ������ 2 if (saParamValue[4] !=
	 * null) m_iaScale[2] = Integer.parseInt(saParamValue[4]); //BD504 ����ɱ�����С��λ
	 * 2 if (saParamValue[5] != null) m_iaScale[3] =
	 * Integer.parseInt(saParamValue[5]); //BD301 ����С��λ if (saParamValue[6] !=
	 * null) m_iaScale[4] = Integer.parseInt(saParamValue[6]); } } catch
	 * (Exception e) { nc.vo.scm.pub.SCMEnv.out("can not get para" +
	 * e.getMessage()); if (e instanceof nc.vo.pub.BusinessException)
	 * showErrorMessage(e.getMessage()); } }
	 */
	/**
	 * �˴����뷽��˵���� �������ڣ�(2003-9-4 10:55:57)
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
	 * �˴����뷽��˵���� �������ڣ�(2003-9-4 10:55:43)
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
	 * �������ڣ�(2003-2-18 9:59:58) ���ߣ����˾� �޸����ڣ� �޸��ˣ� �޸�ԭ�� ����˵����
	 * 
	 * @return boolean
	 */
	private boolean isByFormula() {
		return m_bIsByFormula;
	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2003-9-4 11:58:58)
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
			// ����ȡ����ʵ��¼�� �޸� by hanwei 2003-12-22
			if (flag.equals(nc.vo.ic.pub.bill.BillStatus.CHECKING)
					|| flag.equals(nc.vo.ic.pub.bill.BillStatus.CHECKINPUT))
				return true;
		}
		return false;
	}

	/**
	 * ���ܣ���ѯ��������״̬ �������ڣ�(2002-12-25 16:34:26) ���ߣ����˾� �޸����ڣ� �޸��ˣ� �޸�ԭ�� �㷨˵����
	 */
	private void onAuditState() {

		// if(BillgetBillListPanel().getHeadSelectedCount()!=1) {
		if (m_voBill == null) {
			MessageDialog.showHintDlg(this,
					nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec",
							"UPPSCMCommon-000270")/* @res "��ʾ" */,
					nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec",
							"UPP4008spec-000216")/* @res "��ѡ��һ�ŵ���!" */);
			return;
		}

		showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
				"UCH050")/* @res "���ڽ�������״̬��ѯ" */);

		String hid = m_voBill.getHeaderVO().getCspecialhid();

		// ����õ��ݴ�����������״̬��ִ��������䣺
		nc.ui.pub.workflownote.FlowStateDlg approvestatedlg = new nc.ui.pub.workflownote.FlowStateDlg(
				this, BillTypeConst.m_check, hid);
		approvestatedlg.showModal();

	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2003-9-3 13:26:36)
	 */
	private void onCalculate() {
		if (showYesNoMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
				"4008spec", "UPP4008spec-000217")/* @res "��������Ƿ����ҵ���ڼ���?" */) == nc.ui.pub.beans.UIDialog.ID_OK) {
			// ����ȡ��
			// showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
			// "4008spec", "UPP4008spec-000218")/* @res "���Ժ�..." */);

			// �˵����еĿ���
			filterNullLine();
			if (getBillCardPanel().getRowCount() == 0) {
				showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"4008spec", "UPP4008spec-000134")/*
															 * @res
															 * "�����ݣ�����������..."
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
																			 * "δ����ֿ⣡"
																			 */);
					return;
				}

				// �ִ���
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
																			 * "δ�鵽�κ����ݣ�"
																			 */);
					return;
				}
				m_timer.showExecuteTime("qry acc num");
				// �Զ�ִ���޸ġ�
				// onChange();

				// ������������
				m_voBill.setPeriodNum(alInvNum);
				m_voBill.calcAdjstDiffNum();
				setBillValueVO(m_voBill);

				// //�����Ӧ�����Ĺ�ʽ
				// int col1 = getBillCardPanel().getBodyColByKey("nchecknum");
				// col1 =
				// getBillCardPanel().getBillTable().convertColumnIndexToView(col1);
				// int col2 =
				// getBillCardPanel().getBodyColByKey("ncheckastnum");
				// col2 =
				// getBillCardPanel().getBillTable().convertColumnIndexToView(col2);
				// String sInvID = null;
				// for (int i = 0; i < getBillCardPanel().getRowCount(); i++) {
				// //��Ϊ�޸�״̬��ԭ�������޸�Ϊ���޸�״̬�����������в��ܸġ����������ʱ�ᱻ����ɾ���С�
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
				// ���õ���״̬Ϊ������ȡ����
				// m_voBill.setHeaderValue("fbillflag",
				// nc.vo.ic.pub.bill.BillStatus.CALCULATE);
				showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"SCMCOMMON", "UPPSCMCommon-000133")/* @res "����" */);
				m_timer.stopAndShow("other deal acc num");
			} catch (Exception e) {
				handleException(e);
				nc.ui.pub.beans.MessageDialog.showErrorDlg(this,
						nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec",
								"UPPSCMCommon-000059")/* @res "����" */, e
								.getMessage());
			}

		}
		return;
	}

	/**
	 * �޸�
	 */
	public void onChange(boolean bAddRow) {

		showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
				"UCH027")/* @res "�����޸�" */);
		// ���������Ϊ�Ǳ���״̬,����״̬Ϊ1���Ǳ���״̬Ϊ��1��
		iSaveFlag = -1;
		// �б�����µ�༭�����л�����Ƭ
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

		// ״̬��ɱ༭����һ������ȡ��ʱ������������״̬����Ȼ���ܱ���
		if (m_bBillInit == true)
			m_iMode = BillMode.New;
		else
			m_iMode = BillMode.Update;
		setButtonState();
		setBillState();

		setUpdateBillInitData();

		// ����ȡ����bAddRow=false
		if (bAddRow) {
			onAddRow();
		}

		firstSetColEditable();
		// �޸ĵ��ݱ���ǰ���б���
		m_sOldBillCode = getBillCardPanel().getHeadItem("vbillcode").getValue();
		if (m_sOldBillCode != null)
			m_sOldBillCode = m_sOldBillCode.trim();

		// Ĭ�ϲ��ǵ������� add by hanwei 2003-10-30
		m_bIsImportData = false;

		// 2005-01-28 �������ɫ����
		InvAttrCellRenderer ficr = new InvAttrCellRenderer();
		ficr.setFreeItemRenderer(getBillCardPanel(), m_voBill);
	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2003-8-25 13:53:58) ��Ӧ�����գ��Զ���дʵ�����գ�
	 */
	private void onFillNum() {
		if (m_voBill == null)
			return;

		onChange();

		// ����ʵ��¼��״̬
		setBillCheckInputStatus(nc.vo.ic.pub.bill.BillStatus.CHECKINPUT);

		// ������
		if (getBillCardPanel().getBodyItem("naccountastnum") != null
				&& getBillCardPanel().getBodyItem("ncheckastnum") != null)
			SpecialBillUICtl.fillValue(getBillCardPanel(), this,
					"naccountastnum", "ncheckastnum");

		// ����
		if (getBillCardPanel().getBodyItem("naccountnum") != null
				&& getBillCardPanel().getBodyItem("nchecknum") != null)
			SpecialBillUICtl.fillValue(getBillCardPanel(), this, "naccountnum",
					"nchecknum");

		// ����
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
	 * ���ܣ� ���������¼���Ӧ ������ ���أ� ���⣺ ���ڣ�(2002-04-18 10:43:46) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 */
	public void onImportData() {
		try {
			nc.ui.ic.pub.device.DevInputCtrl devInputCtr = getDevInputCtrl();

			if (devInputCtr != null) {
				// devInputCtr.setPk_corp(get)
				devInputCtr.setWarehouseidFieldName("coutwarehouseid");
				devInputCtr.setWarehouseNameFieldName("coutwarehousename");
				// ���˿���
				filterNullLine();
				// �����к�
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
																			 * "û�е���ɹ���"
																			 */);
					return;
				}
				String sAppendType = (String) alResult.get(0);
				int iAppendType = Integer.parseInt(sAppendType);
				nc.vo.pub.CircularlyAccessibleValueObject[] voaDi = (nc.vo.pub.CircularlyAccessibleValueObject[]) alResult
						.get(1);

				// ͬ��vo.
				m_bIsImportData = false;
				if (iAppendType == DevInputCtrl.ACT_ADD_ITEM) {
					synVO(voaDi);
					if (voaDi != null && voaDi.length > 0)
						m_bIsImportData = true;
					// �����Ƿ��ڱ���ʱУ�鵼�����ݵ���ȷ��
					else
						m_bIsImportData = false;

				}
			} else {
				// showWarningMessage("������ģ���йز������ã�");
				showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"4008spec", "UPP4008spec-000222")/*
															 * @res
															 * "������ģ���йز������ã�"
															 */);
			}
		} catch (Exception e) {
			String sErrorMsg = null;
			sErrorMsg = e.getMessage();
			showWarningMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"4008spec", "UPP4008spec-000223")/* @res "������ʾ��" */
					+ sErrorMsg);
			// showHintMessage("������ʾ��" + sErrorMsg);
		}
	}

	/**
	 * ����ȡ���������Զ������޸�״̬ �������״̬������ȡ���������޸���������ѡ��ȡ��
	 */
	public void onQryAccNumber() {
		if (m_iMode == BillMode.Browse) {
			QryAccNumberBrowse();
		} else if (m_iMode == BillMode.New || m_iMode == BillMode.Update) {
			QryAccNumberNew();
		}

		// ���ñ༭
		if (m_iMode == BillMode.Browse)
			onChange(false);
	}
	/**
	 * �����ˣ������� ����ʱ�䣺2008-7-10 ����08:26:24 ����ԭ�� ֻ�ܶ������̵㵥����ֱ����������ȡ�������������ȡ��������ֱ���ڷ������˱�����������Ϣ�����ֻ��ǰ̨ˢ�������̵㵥��������������
	 *
	 */
	private void onQryBCAccNumber() {
		// ����ȡ��
		showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec",
				"UPP4008spec-000574")/* @res "����ִ��ֱ����������ȡ�������Ժ�..." */);

		// �������������
		getBillCardPanel().tableStopCellEditing();

		// �˵����еĿ���
		filterNullLine();
		if (getBillCardPanel().getRowCount() == 0) {
			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"4008spec", "UPP4008spec-000184")/* @res "�����ݣ����������룡" */);
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
						"4008spec", "UPP4008spec-000219")/* @res "δ����ֿ⣡" */);
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
				} // ��ʾ��ʾ��Ϣ
				
				if (alsPrimaryKey.get(0) != null)
					showErrorMessage((String) alsPrimaryKey.get(0));

				SMSpecialBillVO voSM = (SMSpecialBillVO) alsPrimaryKey
						.get(alsPrimaryKey.size() - 1);

				// //ͬ�����VO
				//m_voBill.setIDItems(voNowBill);

				//m_alListData.set(m_iLastSelListHeadRow, m_voBill.clone());

				if (null != m_voBill
						&& null != m_voBill.getItemVOs() && 0 < m_voBill.getItemVOs().length){
					for(SpecialBillItemVO itemVO: m_voBill.getItemVOs()){
						itemVO.setIsloadaccountbc(UFBoolean.FALSE);
						
					}
				}
					


				// ����̨��Ϣ���µ�����
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

				// 2005-01-28 �������ɫ����
				InvAttrCellRenderer ficr = new InvAttrCellRenderer();
				ficr.setFreeItemRenderer(getBillCardPanel(), null);


				// ����HVO
				m_iMode = BillMode.Browse;
				m_iFirstSelListHeadRow = -1;
				iSaveFlag = 1;
				setButtonState();
				setBillState();
				getBillCardPanel().updateValue();
				
				showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec",
				"UPP4008spec-000575")/* @res "ֱ����������ȡ���ɹ�" */);

			}
			else
				showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec",
				"UPP4008spec-000566")/* @res "ֻ�ܶ������̵㵥����ֱ����������ȡ��" */);
		} catch (Exception e) {
			handleException(e);
			nc.ui.pub.beans.MessageDialog.showErrorDlg(this,
					nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec",
							"UPPSCMCommon-000059")/* @res "����" */, e
							.getMessage());
		}

	}


	/**
	 * �����ߣ������� ԭ����ȡ����ֻ�����״̬������ȡ��
	 */
	private void QryAccNumberBrowse() {
		// ����ȡ��
		showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec",
				"UPP4008spec-000224")/* @res "����ִ������ȡ�������Ժ�..." */);

		// �������������
		getBillCardPanel().tableStopCellEditing();

		// �˵����еĿ���
		filterNullLine();
		if (getBillCardPanel().getRowCount() == 0) {
			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"4008spec", "UPP4008spec-000184")/* @res "�����ݣ����������룡" */);
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
						"4008spec", "UPP4008spec-000219")/* @res "δ����ֿ⣡" */);
				return;
			}

			// �ִ���
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
						"4008spec", "UPP4008spec-000220")/* @res "δ�鵽�κ����ݣ�" */);
				getBillCardPanel().getBillModel().setNeedCalculate(false);
				for (int i = 0; i < getBillCardPanel().getRowCount(); i++) {
					// ��Ϊ�޸�״̬��ԭ�������޸�Ϊ���޸�״̬�����������в��ܸġ����������ʱ�ᱻ����ɾ���С�
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
			
			

			
			// ������������
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
				// ��Ϊ�޸�״̬��ԭ�������޸�Ϊ���޸�״̬�����������в��ܸġ����������ʱ�ᱻ����ɾ���С�
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
			// ���õ���״̬Ϊ������ȡ����
			// m_voBill.setHeaderValue("fbillflag",nc.vo.ic.pub.bill.BillStatus.CHECKING);

			setBillCheckInputStatus(nc.vo.ic.pub.bill.BillStatus.CHECKING);
			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"SCMCOMMON", "UPPSCMCommon-000133")/* @res "����" */);
			m_timer.stopAndShow("other deal acc num");
		} catch (Exception e) {
			handleException(e);
			nc.ui.pub.beans.MessageDialog.showErrorDlg(this,
					nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec",
							"UPPSCMCommon-000059")/* @res "����" */, e
							.getMessage());
		}

	}

	/**
	 * ���Ӻ�ֻ̨�������ѯ���Ĵ������ݰ��չؼ��ְ���ԭ����䵽ÿһ��ȥ��
	 * ��̨���ݻصĲ���alInvVO�Ѿ����մ���Ĺؼ��ְ�����ϵ���ˣ��˷���Ŀ���ǱȽ�
	 * m_voBill�еĸ�������ؼ��ֵĹ�ϵ�������������ۼƺ����渨�������Լ���浥�����롣
	 * 
	 * �㷨˵���� ѭ���������ItemVOs, ѭ������InvVo��ֻҪInvVo�Ĺؼ��ְ�������ItemVO�Ĺؼ��֣��������ִ����ۼӣ�
	 * ����������ۼƵ��ִ�����
	 * 
	 * @param m_voBill
	 * @param alInvVO
	 *            �޸��ˣ������� �޸����ڣ�2007-9-20����10:40:40
	 *            �޸�ԭ�򣺸���2007-09-19��������������ۺ������޸ĳ����һ�д����Ӧ�ö����棬�ͱ���������������ϸ�ִ���ά�ȡ�
	 */
	private void setOnHandNum(SpecialBillVO m_voBill, ArrayList alInvVO) {
		SpecialBillItemVO[] voaItem = (SpecialBillItemVO[]) m_voBill
				.getChildrenVO();
		KeyObject keyTempItemVO = null;
		KeyObject keyTempInvVO = null;

		int comparednum = 0;

		StringBuilder sErr = new StringBuilder();

		// �����жϣ�
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
					// ���InvVo�Ĺؼ��ְ���ItemVo�Ĺؼ��֣�������Ӧ�����ItemVO���ִ�����
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
					// ���ý�浥��
					voaItem[i].setNprice(ufPrice);

					voaItem[i].setNaccountgrsnum(ufGrsNum);
				}
				if (comparednum > 1) {
					if (sErr.length() == 0)
						sErr.append(nc.ui.ml.NCLangRes.getInstance().getStrByID(
								"4008spec", "UPP4008spec-000551")/*
																	 * @res
																	 * "�����н�治Ψһ������ȡ��ʧ�ܣ����飺"
																	 */
								+ " \n");
					sErr.append(nc.ui.ml.NCLangRes.getInstance().getStrByID(
							"4008check", "UPP4008check-000095")/* @res "�кţ�" */
							+ voaItem[i].getCrowno() + " \n");

				}
			}
		}

		

		if (sErr.length() > 0)
			showWarningMessage(sErr.toString());

	}

	/**
	 * �����ߣ�� ���ܣ�����ȡ�����������ѡ���н�������ȡ��
	 * 
	 */
	private void QryAccNumberNew() {
		showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec",
				"UPP4008spec-000224")/* @res "����ִ������ȡ�������Ժ�..." */);
		// 1. �õ����ѡ���У��ò�����ʾ
		int[] iaSel = null;
		if (BillMode.List == m_iMode) {
			showWarningMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"4008spec", "UPP4008spec-000225")/* @res "���ڿ�Ƭģʽ������ȡ����" */);
			return;
		} else {
			iaSel = getBillCardPanel().getBillTable().getSelectedRows();
			if (iaSel == null || iaSel.length == 0) {
				showWarningMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"4008spec", "UPP4008spec-000226")/* @res "��ѡ����������ȡ����" */);
				return;
			}
		}

		// �޸��ˣ������� �޸����ڣ�2007-9-20����10:12:41 �޸�ԭ�򣺰ѽ����ϵ��޸ĸ��µ�m_voBill��Ա�����ȡ��ʱ��׼ȷ��
		SpecialBillVO voNowBill = getBillVO();
		m_voBill.setIDItems(voNowBill);
		// 2. �õ�ѡ���е�PK�ţ����PKHashMap,�������ͬ�У���ʾ��������ͬ��
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
																		// ��Ҫ���ݴ���Ƿ������޸�
			
			/**add by ouyangzhb 2012-04-25 �뵥�̵�-�Դ�����ж�*/
			if(m_voBill.getHeaderVO().getIcheckmode() !=null && m_voBill.getHeaderVO().getIcheckmode() ==CheckMode.md ){
				hmUK_Inv.put(sKeyUI, voitemSelecte.getCinventoryid());
			}else{
				if (!StringKeyJudge.containString(hmUK_Inv, sKeyUI)) {
					hmUK_Inv.put(sKeyUI, voitemSelecte.getCinventoryid());
				} else {
					showWarningMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
							"4008spec", "UPP4008spec-000227")/*
																 * @res
																 * "���ظ�����У��뽫��ϲ���һ�У�"
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
//															 * "���ظ�����У��뽫��ϲ���һ�У�"
//															 */);
//				return;
//			}
		}
		if (hmUK_Inv == null || hmUK_Inv.size() <= 0) {
			showWarningMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"4008spec", "UPP4008spec-000228")/* @res "ѡ�����޴������������ȡ����" */);
			return;
		}

		// 3. ������̨��ѯ
		ArrayList alReturn = null;
		ArrayList returnBillBarcode = null;
		ArrayList alInvNum = null;
		HashMap<String,ArrayList<KeyObject>> returnHSKeySet = null;
		HashMap<String,ArrayList<SpecailBarCodeVO[]>> returnHSSPBarCodes = null;
		String WhID = ((nc.ui.pub.beans.UIRefPane) getBillCardPanel()
				.getHeadItem("coutwarehouseid").getComponent()).getRefPK();
		try {
			//add by ouyangzhb 2012-04-24 �����뵥����ȡ������ begin 
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
			//add by ouyangzhb 2012-04-24 �����뵥����ȡ������ end 
			
			//���ӿ��ж� ������ 2009-07-10
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
					"4008spec", "UPP4008spec-000229")/* @res "����ȡ������!" */);
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
		// 4. ���ݲ�ѯ���InvVOs����m_voBill,���ý���
		if ((null == alInvNum) || (alInvNum.size() == 0)) {
			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"4008spec", "UPP4008spec-000220")/* @res "δ�鵽�κ����ݣ�" */);
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


		
		// ��������������m_voBill
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

		// ���ý��� ( ����ִ���̵㹫ʽ)
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

		// ���õ���״̬Ϊ������ȡ����
		setBillCheckInputStatus(nc.vo.ic.pub.bill.BillStatus.CHECKING);
		showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
				"SCMCOMMON", "UPPSCMCommon-000133")/* @res "����" */);
		m_timer.stopAndShow("other deal acc num");

	}

	/**
	 * ���ܣ�ȡ����ǰ�����ϵĴ���ĵ�ǰ���½�浥�� �������ڣ�(2002-12-25 16:34:26) ���ߣ����˾� �޸����ڣ� �޸��ˣ� �޸�ԭ��
	 * �㷨˵����
	 */
	private void onQueryPrice() {
		try {
			// ȡ��ǰ������ʾ������
			SpecialBillVO voCurBill = getBillVO();
			if (voCurBill != null && voCurBill.getHeaderVO() != null
					&& voCurBill.getItemVOs() != null
					&& voCurBill.getItemVOs().length > 0) {
				SpecialBillHeaderVO voHead = voCurBill.getHeaderVO();
				SpecialBillItemVO[] voaItem = voCurBill.getItemVOs();
				// ׼���������ݡ�
				// String pk_corp = m_sCorpID; //��˾
				String pk_calbody = voHead.getPk_calbody_out(); // �����֯
				String cwarehouseid = voHead.getCoutwarehouseid(); // �ֿ�
				if (cwarehouseid != null && cwarehouseid.trim().length() > 0) {
					String[] cinventoryids = null; // �����
					Vector vInv = new Vector();
					for (int i = 0; i < voaItem.length; i++) {
						// ���vector��û�д˴������id�Ļ����ͼӽ�ȥ��
						if (voaItem[i] != null
								&& voaItem[i].getCinventoryid() != null
								&& !vInv.contains(voaItem[i].getCinventoryid()))
							vInv.addElement(voaItem[i].getCinventoryid());
					}
					// �д����
					if (vInv.size() > 0) {
						cinventoryids = new String[vInv.size()];
						vInv.copyInto(cinventoryids);
						ArrayList alParam = new ArrayList();
						alParam.add(m_sCorpID);
						alParam.add(pk_calbody);
						alParam.add(cwarehouseid);
						alParam.add(cinventoryids);
						// ִ�в�ѯ
						ArrayList alRet = (ArrayList) SpecialBillHelper
								.queryInfo(new Integer(
										QryInfoConst.SETTLEMENT_PRICE), alParam);
						if (alRet != null && alRet.size() > 0) {
							// ���õ�������
							setPrice(alRet);
							showHintMessage(nc.ui.ml.NCLangRes.getInstance()
									.getStrByID("4008spec",
											"UPP4008spec-000230")/*
																	 * @res
																	 * "��ȡ��浥����ϡ�"
																	 */);
						} else {
							showErrorMessage(nc.ui.ml.NCLangRes.getInstance()
									.getStrByID("4008spec",
											"UPP4008spec-000231")/*
																	 * @res
																	 * "û�в�ѯ����ǰ����Ľ�浥�ۣ�"
																	 */);
						}
					} else {
						showHintMessage(nc.ui.ml.NCLangRes.getInstance()
								.getStrByID("4008spec", "UPP4008spec-000232")/*
																				 * @res
																				 * "����¼������Ȼ�����ԡ�"
																				 */);
					}
				} else {
					showHintMessage(nc.ui.ml.NCLangRes.getInstance()
							.getStrByID("4008spec", "UPP4008spec-000233")/*
																			 * @res
																			 * "����¼��ֿ⣬Ȼ�����ԡ�"
																			 */);
				}
			} else
				showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"4008spec", "UPP4008spec-000234")/*
															 * @res
															 * "��ǰû�п��õ��ݣ����ѯ���������ݡ�"
															 */);
		} catch (Exception e) {
			showErrorMessage(e.getMessage());
		}
	}

	/**
	 * �����ߣ����˾� ���ܣ���ѯ���ݵı��壬���ѽ���õ�arraylist ������ int iaIndex[],������alAlldata�е�������
	 * String saBillPK[]����pk���� ���أ� ���⣺ ���ڣ�(2001-5-9 9:23:32) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
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
			// ���ý�����
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
	 * �����ߣ����˾� ���ܣ���ʾ���ĵ�ʱ�� ������ ���أ� ���⣺ ���ڣ�(2001-11-23 18:11:18)
	 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 */
	protected void reportTime(long lStartTime, String sTaskHint) {
		long lTime = System.currentTimeMillis() - lStartTime;
		String sMessage = "ִ��<" + sTaskHint + ">���ĵ�ʱ��Ϊ��" + (lTime / 60000)
				+ "��" + ((lTime / 1000) % 60) + "��" + (lTime % 1000) + "����";
		showWarningMessage(sMessage);

	}

	/**
	 * 
	 * ���ܣ� �����ļ���ͬ��״̬vo. ������ ���أ� ���⣺ ���ڣ�(2002-04-18 10:43:46) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 */
	public void resumeVO() {
		if (m_voBill != null) { // ͬ��vo.
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
	 * �˴����뷽��˵���� ���õ�ǰ���̵�ĵ�״̬ (0)��ʼ״̬ (1)����ȡ�� (2)ʵ��¼��
	 * 
	 * �������ڣ�(2003-12-22 19:01:42)
	 */
	public void setBillCheckInputStatus(String sStatues) {
		if (sStatues == null)
			return;
		m_voBill.setHeaderValue("fbillflag", sStatues);
	}

	/**
	 * ˵�������õ����Ƿ�������� �������ڣ�(2001-4-18 19:45:39)
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
	// if (m_bBillInit) {//��һ����״̬���п���ȡ�����ϴεĵ���״̬������ֱ���ƹ�������ж�
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
	// //������
	// if (sFlag
	// .equals(nc.vo.ic.pub.bill.BillStatus.APPROVEDING)) {
	// bAddSortListener = true;
	// } //����ȡ����ʵ��¼��
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
	 * �������ڣ�(2003-2-18 9:59:58) ���ߣ����˾� �޸����ڣ� �޸��ˣ� �޸�ԭ�� ����˵����
	 * 
	 * @param newIsByFormula
	 *            boolean
	 */
	private void setIsByFormula(boolean newIsByFormula) {
		m_bIsByFormula = newIsByFormula;
	}

	/**
	 * ���ܣ��õ�ǰ�����ϵĴ���ĵ�ǰ���½�浥�� �������ڣ�(2002-12-25 16:34:26) ���ߣ����˾� �޸����ڣ� �޸��ˣ� �޸�ԭ��
	 * �㷨˵����
	 */
	private void setPrice(ArrayList alData) {

		final String IK_INV = "cinventoryid"; // ��������itemkey
		final String IK_PRICE = "nprice"; // ��������itemkey

		if (alData != null && alData.size() > 0) {
			// ����HASHTABLE:KEY=INVID,VALUE=PRICE����߲�ѯЧ��
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
			// ͬ��vo
			// ����Ҫ
			getBillCardPanel().getBillModel().setNeedCalculate(true);
			getBillCardPanel().getBillModel().reCalcurateAll();
		} else {
			nc.vo.scm.pub.SCMEnv.out("no price to be set");
		}
	}

	/**
	 * �����ߣ����˾� ���ܣ���ʾ���ĵ�ʱ�� ������ ���أ� ���⣺ ���ڣ�(2001-11-23 18:11:18)
	 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 */
	protected void showTime(long lStartTime, String sTaskHint) {
		long lTime = System.currentTimeMillis() - lStartTime;
		nc.vo.scm.pub.SCMEnv.out("ִ��<" + sTaskHint + ">���ĵ�ʱ��Ϊ��"
				+ (lTime / 60000) + "��" + ((lTime / 1000) % 60) + "��"
				+ (lTime % 1000) + "����");

	}

	/**
	 * 
	 * ���ܣ� �����ļ���ͬ��״̬vo. ������ ���أ� ���⣺ ���ڣ�(2002-04-18 10:43:46) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 */
	public void synVO(nc.vo.ic.pub.device.DevInputVO[] voaDi) {
		if (m_voBill != null) {
			// ͬ��vo.
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
	 * ���ܣ� �����ļ���ͬ��״̬vo. ������ ���أ� ���⣺ ���ڣ�(2002-04-18 10:43:46) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 */
	public void synVO(nc.vo.pub.CircularlyAccessibleValueObject[] voaDi) {
		if (m_voBill != null && voaDi != null) {
			// ͬ��vo.
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
	 * �ź��� ���㻻����/����/��������ϵ whj�������: 1.���û��hsl,���������������㸨����
	 * 2.������Ϊ����hsl�ǽ��,�����۽����ϻ�����Ϊ��ֵ,ֻ�ڳ�¼����ʱ���㸨����,һ������/��������ֵ,����ʹ�û����ϵ
	 * 3.������Ϊ��hsl�ǽ��,��hsl���չ̶��Ŀ���,��hslһ��¼��,���ٸ�������/�������ĸı���ı�,��Ȼ,������������ĵ�,�޸ĺ�Ӧ�����������
	 * 4.��������[������]=�̵�����[������]-��������[������] 5.��������[������]ʼ�ղ���Ӱ����������[������]�ͻ�����
	 * 
	 * iWhichChanged: 0 �������޸� 1 �������޸�
	 */
	private void calHslNumAstNum(int iRow, String sMainNum, String sAstNum,
			int iWhichChanged) {
		UFDouble hsl = getUFValueByItemKey(iRow, "hsl");

		UFDouble ufdMainNum = null;
		UFDouble ufdAstNum = null;
		ufdMainNum = getUFValueByItemKey(iRow, sMainNum);
		ufdAstNum = getUFValueByItemKey(iRow, sAstNum);
		// ��hsl�ǽ��
		if (isHsl(iRow) || (null != m_iscountflag && m_iscountflag.booleanValue())) {
			if (iWhichChanged == 1) {// �������޸�
				ufdMainNum = ufdAstNum.multiply(hsl);
				getBillCardPanel().setBodyValueAt(ufdMainNum, iRow, sMainNum);
				m_voBill.setItemValue(iRow, sAstNum, ufdAstNum);
				m_voBill.setItemValue(iRow, sMainNum, ufdMainNum);
			} else if (iWhichChanged == 0) {// �����޸�
				ufdAstNum = ufdMainNum.div(hsl);
				getBillCardPanel().setBodyValueAt(ufdAstNum, iRow, sAstNum);
				m_voBill.setItemValue(iRow, sAstNum, ufdAstNum);
				m_voBill.setItemValue(iRow, sMainNum, ufdMainNum);
			}
		} else {
			// �������hsl�ǽ��
			if (iWhichChanged == 0) {// �����޸�
				// ��������Ϊ��
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
		// ִ�н����㹫ʽ
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
	 * �����к�
	 * 
	 * @param voBill
	 */
	private void setRowNo(GeneralBillVO voBill, String sBillType) {
		if (voBill == null || voBill.getChildrenVO().length <= 0)
			return;
		BillRowNoVO.setVOsRowNoByRule(voBill.getItemVOs(), sBillType, "crowno");
	}

	/**
	 * �����ˣ������� �������ڣ�2007-10-22����04:47:04 ����ԭ��
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
							"UPPSCMCommon-000270")/* @res "��ʾ" */,
					nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill",
							"UPP4008bill-000062")/* @res "û�з��ϲ�ѯ�����ĵ��ݣ�" */);
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
		// TODO �Զ����ɷ������

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
															 * @res "����"
															 */, nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill",
							"UPP4008bill-000088")/*
													 * @res "Excel�ļ�����Ϊ�գ�"
													 */);
					return;
				}

				importItemVO(voaImport, isCover);

			} else
				return;

		} catch (Exception e) {
			nc.vo.scm.pub.SCMEnv.error(e);
			showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"4008bill", "UPP4008bill-000089")/* @res "��Excel�����ļ�����" */
					+ e.getMessage());
		}

	}
/**
 * �����ˣ������� ����ʱ�䣺2008-8-27 ����03:59:50 ����ԭ�� 
 *
 * @param voaImports
 * @param isCover
 */
	private void importItemVO(BarCodeParseVO[] voaImports, boolean isCover) {
		// TODO �Զ����ɷ������
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
						sErr.append("Excel�ļ����������д���\n");
						sErr.append("��" + excelRow.toString() + "�У�������룺"
								+ voaImport.getAttributeValue("cinventorycode")
								+ " ��û�������� \n");
					} else
						sErr.append("��" + excelRow.toString() + "�У�������룺"
								+ voaImport.getAttributeValue("cinventorycode")
								+ " ��û�������� \n");
					errImports.add(voaImport);
					continue;
				}

				if (!getM_bcRuleArrLen().contains(voaImport.getAttributeValue("vbarcode").toString().length())) {
					if (0 == sErr.length()) {
						sErr.append("Excel�ļ����������д���\n");
						sErr.append("��" + excelRow.toString() + "�У�������룺"
								+ voaImport.getAttributeValue("cinventorycode")
								+ " �����벻����������� \n");
					} else
						sErr.append("��" + excelRow.toString() + "�У�������룺"
								+ voaImport.getAttributeValue("cinventorycode")
								+ " �����벻����������� \n");
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
					sErr.append("Excel�ļ����������д���\n");
					sErr.append("��" + excelRow.toString() + "�У�������룺"
							+ voaImport.getAttributeValue("cinventorycode")
							+ " ��������벻��ȷ \n");
				} else
					sErr.append("��" + excelRow.toString() + "�У�������룺"
							+ voaImport.getAttributeValue("cinventorycode")
							+ " ��������벻��ȷ \n");
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
						// �޸��ˣ������� �޸�ʱ�䣺2008-8-27 ����03:59:54 �޸�ԭ��׷��ģʽͳһ���롰��Ӧ�������ļ���һ��
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

						// �����к�
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
			sErr.append("�Ƿ񵼳�δ�ɹ���������룡");
			if (showYesNoMessage(sErr.toString()) == MessageDialog.ID_YES){
				BarCodeParseVO[] barCodeParseVOs = new BarCodeParseVO[errImports.size()];
				errImports.toArray(barCodeParseVOs);
				// TODO �Զ����ɷ������
				String sFilePathDir = null;
	
				if (getChooser().showSaveDialog(this) == javax.swing.JFileChooser.CANCEL_OPTION)
					return;
				sFilePathDir = getChooser().getSelectedFile().toString();
				if (sFilePathDir == null) {
					showHintMessage("�������ļ�������!");
					return;
				}
	
				if (sFilePathDir.indexOf(".xls") < 0)
					sFilePathDir = sFilePathDir + ".xls";
				// ������ʱ֧�ֵ�����ʽ�ļ�
				/*
				 * if (packbillItemVOs == null) { nc.vo.scm.pub.ctrl.GenMsgCtrl
				 * .printErr("num error,or list null.Method:CardPanelCtrl::show(int)");
				 * return; }
				 */
	
				exportVOToExcel(barCodeParseVOs, sFilePathDir);
			}
		}
		showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec",
		"UPP4008spec-000572")/* @res "ʵ�����뵼��ɹ�"*/);
		if (bShow)
			showWarningMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec",
			"UPP4008spec-000572")/* @res "ʵ�����뵼��ɹ�"*/);

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
																	 * "�������"
																	 */, "cinventorycode" },
				{
						nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
								"UC000-0002060")/* @res "���κ�" */, "vbatchcode" },

				{
						nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
								"UC000-0003938")/* @res "��������λ" */,
						"castunitcode" },
				{
						nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
								"UC000-0002161")/* @res "������" */, "hsl" },
				{
						nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
								"UC000-0000275")/* @res "��Ӧ��" */,
						"cvendorcode" },

				{
						nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
								"UC000-0003330")/* @res "������1" */, "vfree1" },
				{
						nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
								"UC000-0003333")/* @res "������2" */, "vfree2" },
				{
						nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
								"UC000-0003335")/* @res "������3" */, "vfree3" },
				{
						nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
								"UC000-0003337")/* @res "������4" */, "vfree4" },
				{
						nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
								"UC000-0003339")/* @res "������5" */, "vfree5" },
				{
					nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID(
							"40080216", "UPT40080216-000015")/*
																 * @res
																 * "������"
																 */, "vpackcode" },
				{
						nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID(
								"common", "UC000-0000077")/* @res "������" */,
						"vbarcode" },
				{
						nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID(
								"common", "UC000-0002819")/* @res "������" */,
						"vbarcodesub" },
				{
						nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
								"UC000-0002282")/* @res "����" */, "nnum" } };
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
																		 * "������������: "
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
		// TODO �Զ����ɷ������
		nc.ui.pub.beans.MessageDialog.showHintDlg(this, nc.ui.ml.NCLangRes
				.getInstance().getStrByID("SCMCOMMON",
						"UPPSCMCommon-000270")/* @res "��ʾ" */, error);
	}

	public String getBusiTypeItemKey() {
		// TODO �Զ����ɷ������
		return "";
	}

	public void scanAddBarcodeline(BarCodeParseVO vo) throws Exception {
		// TODO �Զ����ɷ������
		try {
			if (vo == null)
				return;
	
			if (!m_iscountflag.booleanValue()){
				showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec",
				"UPP4008spec-000567")/* @res "�������̵㵥���ܽ������������" */);
				throw new BusinessException(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec",
				"UPP4008spec-000567")/* @res "�������̵㵥���ܽ������������" */);

			}
			
			String sRowPrimaryKey = getBarcodeCtrl().getBarcodeRowPrimaryKey(
					m_sCorpID, vo);
	
			// ͨ����������ж���Ĺؼ�����
			String[] sPrimaryKeyItems = vo.getMatchPrimaryKeyItems();
	
			BarCodeParseVO[] barCodeParseVOs = new BarCodeParseVO[] { vo };
			boolean bBox = false;
			scanadd(sRowPrimaryKey, barCodeParseVOs, bBox, sPrimaryKeyItems);
			// ��������ɨ���
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
																	 * "�������ʧ�ܣ������������Ϲؼ������޶�Ӧ����"
																	 */);
			// ȡ��ǰ��
			int iRow = getBillCardPanel().getBillTable().getSelectedRow();
			if (sRowPrimaryKey != null && barCodeParseVOs != null
					&& sRowPrimaryKey.length() > 4
					&& !sRowPrimaryKey.startsWith("NULL")) // �������д��ڴ����Ϣ
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
																 * "�����Ӧ�ĸ���λ���Ϸ�"
																 */);
							
						}
						
					}

				// �������в����з�����������arraylist ,���ȴ���ѡ����
				ArrayList alResultTemp = getBarcodeCtrl().scanBillCardItem(
						sRowPrimaryKey, getM_voBill(), iRow, sPrimaryKeyItems);

				ArrayList alResult = new ArrayList();

				// �������룬ֻ���õ�һ�У�����ʵ����������У��
				// �����޷�������������������쳣�ع�
				if (bBox && alResultTemp != null && alResultTemp.size() > 0) {
					alResult.add(alResultTemp.get(0));
				} else
					alResult = alResultTemp;

				// ���arraylistΪ�գ���len==0����ʾû�ж�Ӧ�������
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
																	 * "����ɨ��¼����������"
																	 */);

						}

					// (1)�Ե�������ⵥ�������Զ������е������
					// (2)��������������������ϣ�Ҳ������ɨ���Զ�������
					if (true) {
						// û�ж�Ӧ�Ĵ�����Զ�����һ�д��
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
						// �ûع��
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
						 * "ɨ��ʶ����µĴ�����룬����ǰ���ݽ��治���������Ӵ���У�" );
						 */
						throw new BusinessException(nc.ui.ml.NCLangRes
								.getInstance().getStrByID("4008bill",
										"UPP4008bill-000127")
						/*
						 * @res "ɨ��ʶ����µĴ�����룬����ǰ���ݽ��治���������Ӵ���У�"
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
																	 * "����ɨ��¼����������"
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
																	 * "�Ǵ������������֧��ɨ��¼¼�������"
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
																	 * "ͬһ�����ͬһ�У�ֻ��ɨ��¼��ͬһ��������������"
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
																	 * "ͬһ�����ͬһ�У�ֻ��ɨ��¼��ͬһ�������Ĵ�����"
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
																	 * "����ɨ��¼����������"
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
																	 * "���������������֧��ɨ��¼¼��������"
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
																	 * "����ɨ��¼����������"
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
																	 * "�Ǹ�������������֧��ɨ��¼¼�밴���������ӵ�����"
																	 */);

						}
					}

					getBillCardPanel().getBillTable().getSelectionModel()
							.setSelectionInterval(icurline, icurline);
					scanUpdateLine(barCodeParseVOs, alResult);
				}
			} else // �����в����ڴ����Ϣ
			{
				// ��Ҫ�ҵ�ǰ�����У�����ɨ�账��
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
																 * "����ɨ��¼����ڴ����Ϣ��������"
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
																	 * "�����Ӧ�ĸ���λ���Ϸ�"
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
																 * "����ɨ��¼����������"
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
																 * "�Ǵ������������֧��ɨ��¼¼�������"
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
																 * "ͬһ�����ͬһ�У�ֻ��ɨ��¼��ͬһ��������������"
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
																 * "ͬһ�����ͬһ�У�ֻ��ɨ��¼��ͬһ�������Ĵ�����"
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
																 * "����ɨ��¼����������"
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
																 * "���������������֧��ɨ��¼¼��������"
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
																 * "����ɨ��¼����������"
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
																 * "�Ǹ�������������֧��ɨ��¼¼�밴���������ӵ�����"
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
					"4008bill", "UPP4008bill-000111")/* @res "��ѡ���д�����ݵ��У�" */);
		}
		
		fillLineSpecailBarCode1VO(iCurFixLine);
		for(BarCodeParseVO barCodeParseVO:barCodeParseVOs)
			if (checkInvBarcodeRepeat(barCodeParseVO))
				return 0;
		
		// �������
		int iNumforUse = scanfixline_fix(barCodeParseVOs, iCurFixLine,
				iNumUsed, bAllforFix); // ����������������

		// ��������
		getBarcodeCtrl().scanfixline_save(barCodeParseVOs, iCurFixLine,
				iNumUsed, iNumforUse, getM_voBill().getItemVOs(),true); // ����������������

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
															 * "ɨ��������ǵ����������ظ����������"
															 */
						+ "'" + sBarcode + "'");
			}
		}
		if (sBarcodeSub != null && barcodevo.getBsingletypesub().booleanValue()) {
			if (m_utfBarCode.getHtbInvBarcodeSub().containsKey(sBarcodeSub)) {
				throw new BusinessException(nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"4008bill", "UPP4008bill-000380")/*
															 * @res
															 * "ɨ��������ǵ����������ظ����������"
															 */
						+ "'" + sBarcodeSub + "'");
			}
		}
		return false;
	}
	
	/**
	 * �����ˣ������� ����ʱ�䣺2008-7-11 ����09:21:43 ����ԭ�� ���ض�Ӧ���������롣
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
	 * �����ˣ������� ����ʱ�䣺2008-7-11 ����09:21:26 ����ԭ�� ���ض�Ӧ���̵����롣
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
	 * �����ˣ������� ����ʱ�䣺2008-7-11 ����09:20:32 ����ԭ�� ������༭��ѡ��ͬ���ʱ�����ض�Ӧ���̵����롣
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

		// ����"�Ƿ񰴸���λ��������"���ԣ�������������Զ���һ�������������Զ���һ��
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

		int iNumforUse = 0; // ����������������
		if (getBillCardPanel().getBodyItem("cinventorycode") != null) {
			// ʵ�ʷ�����
			UFDouble nFactNum = null;
			// Ӧ������
			UFDouble nShouldNum = null;
			UFDouble nFactBarCodeNum = null; // ʵ�ʷ���ʵ������������

			nc.vo.ic.pub.bc.BarCodeVO[] oldBarcodevos = getM_voBill()
					.getItemVOs()[iCurFixLine].getBarCodeVOs();

			if (oldBarcodevos == null || oldBarcodevos.length == 0)
				nFactBarCodeNum = nc.vo.ic.pub.GenMethod.ZERO;
			else {
				nFactBarCodeNum = nc.vo.ic.pub.GenMethod.ZERO;
				for (int i = 0; i < oldBarcodevos.length; i++) {
					// �޸��ˣ������� �޸����ڣ�2007-11-5����11:25:59 �޸�ԭ����ɾ�����벻��ͳ�ƽ���
					if (oldBarcodevos[i] != null
							&& oldBarcodevos[i].getNnumber() != null
							&& oldBarcodevos[i].getStatus() != nc.vo.pub.VOStatus.DELETED)
						nFactBarCodeNum = nFactBarCodeNum.add(oldBarcodevos[i]
								.getNnumber());
				}
			}

			// ʵ�ʷ�����
			Object oNum = getBillCardPanel().getBodyValueAt(iCurFixLine,
					m_sMyItemKey);
			if (oNum == null || oNum.toString().trim().length() == 0) {
				nFactNum = null;
				// ���û��Ӧ�������������ȫ������
			} else
				nFactNum = (UFDouble) oNum;

			// Ӧ������

			try {
				oNum = getBillCardPanel().getBodyValueAt(iCurFixLine,
						m_sMyShouldItemKey);
			} catch (Exception e) {
				oNum = null;
				nc.vo.scm.pub.SCMEnv.error(e);
			}
			if (oNum == null || oNum.toString().trim().length() == 0) {
				nShouldNum = null;
				// ���û��Ӧ�������������ȫ������
			} else
				nShouldNum = (UFDouble) oNum;

			boolean bNegative = false; // �Ƿ���
			if ((nFactNum != null && nFactNum.doubleValue() < 0)
					|| (nShouldNum != null && nShouldNum.doubleValue() < 0)) {
				bNegative = true;
			}

			// �����������ݵ����ƥ���е��㷨
			iNumforUse = getBarcodeCtrl().scanfixlinenum(barCodeParseVOs,
					oldBarcodevos, iCurFixLine, iNumUsed, bAllforFix, nFactNum,
					nShouldNum);

			nFactBarCodeNum = nFactBarCodeNum.add(iNumforUse);

			// add by hanwei 2004-6-2
			// ������������Ӧ������,�������̵㵥���ɵ�����������ϼ
			// ���ܳ���Ӧ�������������޸ĵ�ʵ����������Ӧ������
			if (nShouldNum != null && nFactBarCodeNum != null
					&& nFactBarCodeNum.doubleValue() > nShouldNum.doubleValue()
					&& !getBarcodeCtrl().isOverShouldNum()) {
				nFactBarCodeNum = nShouldNum.abs();
			}

			if (nFactNum == null)
				nFactNum = nc.vo.ic.pub.GenMethod.ZERO;
			// �޸��ˣ������� �޸����ڣ�2007-9-10����02:15:22
			// �޸�ԭ�򣺶��ڵ�����������Ҳ���������ģ�ÿ�ΰ����������Ǹ�������
			if ((barCodeParseVOs[0]
					.getAttributeValue(BarcodeparseCtrl.VBARCODE) != null)
					&& (barCodeParseVOs[0]
							.getAttributeValue(BarcodeparseCtrl.VBARCODESUB) == null)
					&& (m_voBill.getItemVOs()[iCurFixLine]
							.getIsprimarybarcode().booleanValue())
					&& (!m_voBill.getItemVOs()[iCurFixLine]
							.getIssecondarybarcode().booleanValue())
					&& !barCodeParseVOs[0].getBsavebarcode().booleanValue()) {

				// ͬ��ʵ������
				if (bNegative )
					nFactNum = nFactNum.sub(iNumforUse);
				else
					nFactNum = nFactNum.add(iNumforUse);

				getBillCardPanel().setBodyValueAt(nFactNum, iCurFixLine,
						m_sMyItemKey);

				execEditFormula(iCurFixLine, m_sMyItemKey);
				// Ӧ����������ͬ��
				nc.ui.pub.bill.BillEditEvent event1 = new nc.ui.pub.bill.BillEditEvent(
						getBillCardPanel().getBodyItem(m_sMyItemKey), nFactNum,
						m_sMyItemKey, iCurFixLine);
				// ��������༭ҵ���߼�
				//scanCheckNumEdit(event1);
				afterEdit(event1);
				// ִ��ģ�湫ʽ
				/*getGenBillUICtl().execEditFormula(getBillCardPanel(),
						iCurFixLine, m_sMyItemKey);*/

				// ����������״̬Ϊ�޸�
				if (getBillCardPanel().getBodyValueAt(iCurFixLine,
						IItemKey.cspecialbid) != null)
					getBillCardPanel().getBillModel().setRowState(iCurFixLine,
							BillMode.Update);

			} else {

				// ʵ������С��������������ȥ�޸Ľ����ϵ�ʵ������
				if (nFactNum.doubleValue() < nFactBarCodeNum.doubleValue()
						&& !((barCodeParseVOs[0]
								.getAttributeValue(BarcodeparseCtrl.VBARCODE) != null)
								&& (barCodeParseVOs[0]
										.getAttributeValue(BarcodeparseCtrl.VBARCODESUB) == null)
								&& (m_voBill.getItemVOs()[iCurFixLine]
										.getIsprimarybarcode().booleanValue()) && (m_voBill
								.getItemVOs()[iCurFixLine]
								.getIssecondarybarcode().booleanValue()))) {

					// ͬ��ʵ������
					if (bNegative )
						nFactBarCodeNum = nFactBarCodeNum.multiply(nc.vo.ic.pub.GenMethod.NEGONE_UFDOUBLE);

					getBillCardPanel().setBodyValueAt(nFactBarCodeNum,
							iCurFixLine, m_sMyItemKey);

					execEditFormula(iCurFixLine, m_sMyItemKey);
					// Ӧ����������ͬ��
					nc.ui.pub.bill.BillEditEvent event1 = new nc.ui.pub.bill.BillEditEvent(
							getBillCardPanel().getBodyItem(m_sMyItemKey),
							nFactBarCodeNum, m_sMyItemKey, iCurFixLine);
					// ��������༭ҵ���߼�
					//scanCheckNumEdit(event1);
					afterEdit(event1);
					// ִ��ģ�湫ʽ
					/*getGenBillUICtl().execEditFormula(getBillCardPanel(),
							iCurFixLine, m_sMyItemKey);*/

					// ����������״̬Ϊ�޸�
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
		int iNumUsed = 0; // �ۼ�ͳ������
		int ifixRows = alFixRowNO.size();
		int iCurFixLine = 0; // ���µ�ǰ��
		int ifixSingleLineNum = 0;

		for (int i = 0; i < ifixRows; i++) {
			iCurFixLine = Integer.parseInt((String) alFixRowNO.get(i));

			if (ifixRows == 1) {
				// ֻ��һ�У�ȫ����䵱ǰ��
				ifixSingleLineNum = scanfixline(barCodeParseVOs, iCurFixLine,
						0, true);
				break;
			} else {
				if (i == ifixRows - 1) // ���һ�У�������е�����
				{
					ifixSingleLineNum = scanfixline(barCodeParseVOs,
							iCurFixLine, iNumUsed, true);
					break;
				} else // �м������Ӧ��������
				{
					ifixSingleLineNum = scanfixline(barCodeParseVOs,
							iCurFixLine, iNumUsed, false);
				}
				iNumUsed = iNumUsed + ifixSingleLineNum;
				if (iNumUsed == iNumAll) {
					// ��������
					break;
				}
			}

		}
	}
	
	protected void scanUpdateLineSelect(BarCodeParseVO[] barCodeParseVOs)
	throws Exception {
		// ȡ��ǰ��
		int iCurFixLine = 0;
		int rowNow = getBillCardPanel().getBillTable().getSelectedRow();
		if (rowNow < 0) {
			// ��ʾ������Ϣ
			throw new nc.vo.pub.BusinessException(nc.ui.ml.NCLangRes
					.getInstance().getStrByID("4008bill", "UPP4008bill-000112")/*
																				 * @res
																				 * "��ѡ���Ӧ����Ĵ���У�"
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
														 * "��ǰ�з������������������ѹرգ���"
														 */);
			}
		}
}

	public void scanAddBoxline(BarCodeGroupVO vo) throws Exception {
		// TODO �Զ����ɷ������
		
	}
	
	protected void switchListToBill() {
		//����Ӹղŵ��б���ѡ�������
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
	
	/** ��ӦExcel����� */
	public final String[] sbillCaption = new String[] {
			nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
					"UC000-0001480")/* @res "�������" */,
			nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
					"UC000-0002060")/* @res "���κ�" */,
			nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
					"UC000-0003938")/* @res "��������λ" */,
			nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
					"UC000-0002161")/* @res "������" */,
			nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
					"UC000-0000275")/* @res "��Ӧ��" */,
			nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
					"UC000-0003330")/* @res "������1" */,
			nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
					"UC000-0003333")/* @res "������2" */,
			nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
					"UC000-0003335")/* @res "������3" */,
			nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
					"UC000-0003337")/* @res "������4" */,
			nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
					"UC000-0003339")/* @res "������5" */,
			nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
					"UC000-0002733")/* @res "������" */,
			nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
					"UC000-0002282") /* @res "����" */

	};
	
	private HSSFWorkbook wb = null;// Excel�ļ�����

	private HSSFSheet[] sheet = null;// sheet����,����ҳ��
	private void exportVOToExcel(BarCodeParseVO[] barCodeParseVOs,
			String fileName) {
		// TODO �Զ����ɷ������
		try {
			// �޸��ˣ������� �޸����ڣ�2007-9-4����01:23:55
			// �޸�ԭ�򣺽��ÿ�ε���ʱ��������浼����packbillItemVOs��ǰ�����ʱ����ർ����
			wb = null;
			boolean fileExists = false;
			java.io.File f = new java.io.File(fileName);

			// TODO Liujq �޸ĳ��Ƿ��滻
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
																		 * "������ģ�����"
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
					"4008bill", "UPP4008bill-000120")/* @res "�������" */);

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
				// ���֮ǰ�ļ�Ϊ���֣�������������쳣��������Ҫ����
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
		//switch (nc.ui.pub.beans.MessageDialog.showOkCancelDlg(this, "��ʾ", "��ȷ��ɾ����")) {
		//case nc.ui.pub.beans.MessageDialog.ID_OK :
		
		int[] selrow = getBillCardPanel().getBillTable().getSelectedRows();
		int length = selrow.length;
		SCMEnv.out("count is " + length);

		// ɾ���������� add by hanwei
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
	
	// addied by liuzy 2009-9-4 ����03:19:43 ���������⣬�������VO����Ҫִ�й�ʽ����ҳʱ�Ѿ�ִ�й���
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
	 * �����̵����Ϣ�������뵥��ϸ
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
		
		/**�����뵥��ϸ*/
		IMDTools tool = (IMDTools) NCLocator.getInstance().lookup(IMDTools.class.getName());
		/**1��������Դ������������ȡ�����ɵĵ�*/
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
				
				/** 2���뵥������ϸ */
				if (keyMap.containsKey("4I" + bvos[i].getPrimaryKey())) {
					MdcrkVO mdoutvo = new MdcrkVO();
					if (keyMap.containsKey("4I" + bvos[i].getPrimaryKey())) {
						//�����ǰ�ִ���
						String mdxcl = "select * from nc_mdxcl_b b where b.cspaceid='"+ bvos[i].getCspaceid()
						+ "' and b.jbh='"
						+ bvos[i].getVuserdef1()
						+ "' and isnull(b.dr,0)=0";
						 mdxclvo = (MdxclBVO) querybs.executeQuery(
						mdxcl, new BeanProcessor(MdxclBVO.class));
						
						/**�����뵥������ϸvo*/
						
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
						mdoutvo.setRemark(mdxclvo.getRemark());// ��ע
						mdoutvo.setSrkzl(mdxclvo.getZhongliang());// ��������
						mdoutvo.setSrkzs(bvos[i].getNaccountastnum());
						mdoutvo.setDr(0);
						mdoutvo.setDef1(mdxclvo.getDef1());// �ֳ�����
						mdoutvo.setDef2(mdxclvo.getDef2());// �Զ�����2
						mdoutvo.setDef3(mdxclvo.getDef3());// �Զ�����3
						mdoutvo.setDef4(mdxclvo.getDef4());// �Ǽ����ʶ
						mdoutvo.setDef7(mdxclvo.getDef7());// �Զ�����7
						mdoutvo.setDef8(mdxclvo.getDef8());// �Զ�����8
						mdoutvo.setDef9(mdxclvo.getDef9());// �Զ�����9
						mdoutvo.setDef10(mdxclvo.getDef10());// �Զ�����10
						mdoutvo.setDef11(mdxclvo.getDef11());// �Զ�����11
						mdoutvo.setDef12(mdxclvo.getDef12());// �Զ�����12
						mdoutvo.setDef13(mdxclvo.getDef13());// �Զ�����13
						mdoutvo.setDef14(mdxclvo.getDef14());// �Զ�����14
						mdoutvo.setDef15(mdxclvo.getDef15());// �Զ�����15
						mdoutvo.setSfbj(UFBoolean.FALSE);
						mdoutvo.setSfgczl(UFBoolean.FALSE);
						Srkzl = mdoutvo.getSrkzl();
						
						
						mdoutvo.setPk_mdxcl_b(mdxclvo.getPk_mdxcl_b());
						mdoutvo.setStatus(VOStatus.NEW);
						//�����뵥�����¼
						ivopersistence.insertVO(mdoutvo);
						
						/**�����뵥�ִ���*/
						mdxclvo.setZhishu(mdxclvo.getZhishu().sub(
								mdoutvo.getSrkzs()));
						mdxclvo.setZhongliang(mdxclvo.getZhongliang().sub(
								mdoutvo.getSrkzl()));
						mdxclvo.setDef1(mdxclvo.getDef1()
								.sub(mdoutvo.getDef1()));
						ivopersistence.updateVO(mdxclvo);
					}

				}

				/** 3���뵥�����ϸ */
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
					 * ���Ϊ�Ļ�λ�ģ���ȡδ��֮ǰ���ִ����ĸֳ�������
					 * ���mdxclvo Ϊ�գ����ʾ�������ģ�ֱ��ȡ�̵�����
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
						
						/**����ִ�����ͷΪ�գ��򹹽�һ���ִ���*/
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
						bvo.setRemark(mdcrkVO.getRemark());// ��ע
						bvo.setZhongliang(mdcrkVO.getSrkzl());// ��������
						bvo.setZhishu(mdcrkVO.getSrkzs());
						bvo.setDr(0);
						bvo.setDef1(mdcrkVO.getDef1());// �ֳ�����
						bvo.setDef2(mdcrkVO.getDef2());// �Զ�����2
						bvo.setDef3(mdcrkVO.getDef3());// �Զ�����3
						bvo.setDef4(mdcrkVO.getDef4());// �Ǽ����ʶ
						bvo.setDef7(mdcrkVO.getDef7());// �Զ�����7
						bvo.setDef8(mdcrkVO.getDef8());// �Զ�����8
						bvo.setDef9(mdcrkVO.getDef9());// �Զ�����9
						bvo.setDef10(mdcrkVO.getDef10());// �Զ�����10
						bvo.setDef11(mdcrkVO.getDef11());// �Զ�����11
						bvo.setDef12(mdcrkVO.getDef12());// �Զ�����12
						bvo.setDef13(mdcrkVO.getDef13());// �Զ�����13
						bvo.setDef14(mdcrkVO.getDef14());// �Զ�����14
						bvo.setDef15(mdcrkVO.getDef15());// �Զ�����15
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
						//add by ouyangzhb 2012-05-03 ���ִ����������ֶε�ֵҲ�ĳɵ����������
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