package nc.ui.ic.pub.bill;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import javax.swing.table.TableColumn;

import nc.ui.ic.auditdlg.ClientUIInAndOut;
import nc.ui.ic.ic001.BatchCodeDefSetTool;
import nc.ui.ic.ic001.BatchcodeHelper;
import nc.ui.ic.jj.JJIcScmPubHelper;
import nc.ui.ic.pub.BillFormulaContainer;
import nc.ui.ic.pub.ICCommonBusi;
import nc.ui.ic.pub.InvOnHandDialog;
import nc.ui.ic.pub.PageCtrlBtn;
import nc.ui.ic.pub.bill.cell.SupplierItemCellRender;
import nc.ui.ic.pub.bill.initref.RefFilter;
import nc.ui.ic.pub.bill.query.QueryConditionDlgForBill;
import nc.ui.ic.pub.lot.LotNumbRefPane;
import nc.ui.ic.pub.print.PrintDataInterface;
import nc.ui.ic.pub.tools.GenMethod;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.ToftPanel;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIMenuItem;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.bill.BillActionListener;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.bill.BillCellEditor;
import nc.ui.pub.bill.BillData;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillListData;
import nc.ui.pub.bill.BillListPanel;
import nc.ui.pub.bill.BillModel;
import nc.ui.pub.bill.BillModelCellEditableController;
import nc.ui.pub.bill.BillSortListener2;
import nc.ui.pub.bill.BillUIUtil;
import nc.ui.pub.bill.action.BillTableLineAction;
import nc.ui.pub.linkoperate.ILinkApprove;
import nc.ui.pub.linkoperate.ILinkApproveData;
import nc.ui.pub.linkoperate.ILinkMaintain;
import nc.ui.pub.linkoperate.ILinkMaintainData;
import nc.ui.pub.linkoperate.ILinkQuery;
import nc.ui.pub.linkoperate.ILinkQueryData;
import nc.ui.scm.ic.exp.GeneralMethod;
import nc.ui.scm.ic.freeitem.FreeItemRefPane;
import nc.ui.scm.ic.measurerate.InvMeasRate;
import nc.ui.scm.print.IFreshTsListener;
import nc.ui.scm.pub.CacheTool;
import nc.ui.scm.pub.def.DefSetTool;
import nc.vo.bd.def.DefVO;
import nc.vo.ic.ic001.BatchcodeVO;
import nc.vo.ic.ic700.ICDataSet;
import nc.vo.ic.jjvo.InformationCostVO;
import nc.vo.ic.pub.BillRowType;
import nc.vo.ic.pub.BillTypeConst;
import nc.vo.ic.pub.SmartVOUtilExt;
import nc.vo.ic.pub.bill.DoubleScale;
import nc.vo.ic.pub.bill.GeneralBillHeaderVO;
import nc.vo.ic.pub.bill.GeneralBillItemVO;
import nc.vo.ic.pub.bill.GeneralBillVO;
import nc.vo.ic.pub.bill.IItemKey;
import nc.vo.ic.pub.bill.QryConditionVO;
import nc.vo.ic.pub.bill.QryInfoConst;
import nc.vo.ic.pub.bill.SpecialBillHeaderVO;
import nc.vo.ic.pub.bill.SpecialBillItemVO;
import nc.vo.ic.pub.bill.SpecialBillVO;
import nc.vo.ic.pub.billtype.BillTypeFactory;
import nc.vo.ic.pub.billtype.IBillType;
import nc.vo.ic.pub.check.VOCheck;
import nc.vo.ic.pub.smallbill.SMSpecialBillItemVO;
import nc.vo.ic.pub.smallbill.SMSpecialBillVO;
import nc.vo.pub.AggregatedValueObject;
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
import nc.vo.scm.ic.exp.ICDateException;
import nc.vo.scm.ic.exp.ICException;
import nc.vo.scm.ic.exp.ICHeaderNullFieldException;
import nc.vo.scm.ic.exp.ICLocatorException;
import nc.vo.scm.ic.exp.ICNullFieldException;
import nc.vo.scm.ic.exp.ICNumException;
import nc.vo.scm.ic.exp.ICPriceException;
import nc.vo.scm.ic.exp.ICRepeatException;
import nc.vo.scm.ic.exp.ICSNException;
import nc.vo.scm.print.PrintConst;
import nc.vo.scm.print.ScmPrintlogVO;
import nc.vo.scm.pub.SCMEnv;
import nc.vo.scm.pub.smart.ObjectUtils;
import nc.vo.scm.pub.smart.SmartFieldMeta;

/*
 * �����ߣ�������
 * �������ڣ�2001-04-20
 * ���ܣ�ת�ⵥ����
 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
 */
public class SpecialBillBaseUI
	extends ToftPanel
	implements
		javax.swing.event.TableModelListener,
		nc.ui.pub.bill.BillEditListener,
		nc.ui.pub.bill.BillEditListener2,
		java.awt.event.MouseListener,
		nc.ui.pub.bill.BillBodyMenuListener,IFreshTsListener ,BillModelCellEditableController,
		ILinkApprove,ILinkQuery,ILinkMaintain,BillSortListener2,BillActionListener{

	//--2010-11-08 MeiChao add
	public InformationCostVO[] expenseVOs = null;//ȫ�ַ�����Ϣ����
	//-----------------------------------------------------------------------------

	protected ButtonObject m_boAdd;
	protected ButtonObject m_boChange;
	protected ButtonObject m_boDelete;
	protected ButtonObject m_boCopyBill;
	protected ButtonObject m_boJointAdd;
	protected ButtonObject m_boSave;
	protected ButtonObject m_boCancel;
	protected ButtonObject m_boAddRow;
	protected ButtonObject m_boDeleteRow;
	protected ButtonObject m_boInsertRow;
	protected ButtonObject m_boCopyRow;
	protected ButtonObject m_boPasteRow;
	protected ButtonObject m_boPasteRowTail;
	protected ButtonObject m_boNewRowNo;
  protected ButtonObject m_boLineCardEdit;//��Ƭ�༭

	protected ButtonObject m_boAuditBill;
	protected ButtonObject m_boCancelAudit;
	protected ButtonObject m_boQuery;
	protected ButtonObject m_boLocate;
	protected ButtonObject m_boPrint;
	protected ButtonObject m_boList;

	protected ButtonObject m_boOut;
	protected ButtonObject m_boIn;

	protected ButtonObject m_boRowQuyQty; //������ѯ

	protected ButtonObject m_billMng;
	protected ButtonObject m_billRowMng;
	protected ButtonObject[] m_aryButtonGroup;

	protected BillCardPanel ivjBillCardPanel = null;
	protected BillListPanel ivjBillListPanel = null;
	
	protected ButtonObject m_boBrowse;//���
/*	protected ButtonObject m_boTop;//��ҳ
	protected ButtonObject m_boPrevious;//��ҳ
	protected ButtonObject m_boNext;//��ҳ
	protected ButtonObject m_boBottom;//ĩҳ
*/	
	// ��ҳ����
	protected PageCtrlBtn m_pageBtn;

	protected String m_Title;

	//��ʼ�ĵ��ݱ༭״̬---���
	protected int m_iMode = BillMode.Browse;
	//ȷ���Ƿ��ѽ������и���
	protected boolean m_bCopyRow = false;
	//�����б�״̬ʱͷһ��ѡ�е��б��ͷ��
	protected int m_iFirstSelListHeadRow = -1;
	//���ѡ�е��б��ͷ��
	protected int m_iLastSelListHeadRow = -1;
	protected int m_iLastSelCardBodyRow = -1;

	//�б��ͷĿǰ���ڵ�����
	protected int m_iTotalListHeadNum;
	//�б����Ŀǰ���ڵ�����
	protected int m_iTotalListBodyNum;
  
  protected UIMenuItem miAddNewRowNo ;
  protected UIMenuItem miLineCardEdit ;






	//����е���ɫ
	protected Color m_cNormalColor = null;
	//��������ʾ����
	protected boolean m_bExchangeColor = false;
	//���϶�λ��ʾ����
	protected boolean m_bLocateErrorColor = true;
	//�й��϶�λ��ʾ����
	protected boolean m_bRowLocateErrorColor = true;

	//����
	protected  int m_iFirstAddRows = 2;
	protected String m_sBillTypeCode;
	protected String m_sBillCode;


	protected final String m_sNumItemKey = "dshldtransnum"; //���������ֶ���
	protected final String m_sAstItemKey = "nshldtransastnum"; //���帨�����ֶ���
	protected final String m_sHeaderTableName = "ic_special_h";



	protected final String m_sLotWarehouseSource = "coutwarehouseid";
	//��ʹ�õ��Դ�����������һ�����
	protected FreeItemRefPane m_firpFreeItemRefPane = new FreeItemRefPane();
	protected LotNumbRefPane m_lnrpLotNumbRefPane = new LotNumbRefPane();

	protected int m_iFirstSelectRow = -1;
	protected int m_iFirstSelectCol = -1;

	protected ClientUIInAndOut m_dlgInOut;
	protected SpecialBillVO m_voBill;
	
	protected boolean m_isWhInvRef = false;

	//�и���VO
	protected SpecialBillItemVO[] m_voBillItem;

	protected String m_sCorpID; //��˾ID
	protected String m_sUserID; //��ǰʹ����ID
	protected String m_sLogDate; //��ǰ��¼����
	protected String m_sUserName; //��ǰʹ��������

	protected InvOnHandDialog m_iohdDlg;

	protected InvMeasRate m_voInvMeas = new nc.ui.scm.ic.measurerate.InvMeasRate();

	//protected QueryConditionDlgForBill ivjQueryConditionDlg;

	//�Ƿ��ֹ��޸ĵ��ݺ�
	protected boolean m_bIsEditBillCode = false;
	
	private ClientUISortCtl m_listHeadSortCtl;//�б��ͷ�������
	private ClientUISortCtl m_listBodySortCtl;//�б�����������
	private ClientUISortCtl m_cardBodySortCtl;//��Ƭ�����������
  
  private boolean isLineCardEdit ;
  
  protected QueryDlgHelpForSpec m_queryHelp;


	/**
	 * ClientUI ������ע�⡣
	 */
	public SpecialBillBaseUI() {
		super();
		//initialize();
	}

/**
  * �����ߣ�������
  * ���ܣ������������޸�ʱ
  * ������e���ݱ༭�¼�
  * ���أ������ͷafterEdit,e.getRowΪ-1. ����Ϊ0...N
  * ���⣺
  * ���ڣ�(2001-5-8 19:08:05)
  * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
  */
public void afterEdit(nc.ui.pub.bill.BillEditEvent e) {

	//�Զ�����VO������
	//********************************************************************//
	String strColName = e.getKey().trim();
	String sID_name =
		GeneralMethod.getFromIDtoName(
			getBillCardPanel(),
			getBillListPanel(),
			strColName);
	int rownum = e.getRow();
	if ((sID_name != null) && (m_voBill != null)) {
		if (e.getPos() == 0 && null != getBillCardPanel().getBillData().getHeadItem(strColName)) {
			if (getBillCardPanel().getHeadItem(strColName).getComponent()
				instanceof UIRefPane) {
				if (!sID_name.trim().equals(strColName)) {
					m_voBill.setHeaderValue(
						sID_name,
						((nc.ui.pub.beans.UIRefPane) getBillCardPanel()
							.getHeadItem(strColName)
							.getComponent())
							.getRefName());
					m_voBill.setHeaderValue(
						strColName,
						((nc.ui.pub.beans.UIRefPane) getBillCardPanel()
							.getHeadItem(strColName)
							.getComponent())
							.getRefPK());
				} else if (
					!GeneralMethod.getIDColName(getBillCardPanel(), strColName).equals(
						strColName)) {
					sID_name = GeneralMethod.getIDColName(getBillCardPanel(), strColName);
					m_voBill.setHeaderValue(
						strColName,
						((nc.ui.pub.beans.UIRefPane) getBillCardPanel()
							.getHeadItem(strColName)
							.getComponent())
							.getRefName());
					m_voBill.setHeaderValue(
						sID_name,
						((nc.ui.pub.beans.UIRefPane) getBillCardPanel()
							.getHeadItem(strColName)
							.getComponent())
							.getRefPK());
				} else {
//					m_voBill.setHeaderValue(sID_name, e.getValue());
				}
			} else {
				m_voBill.setHeaderValue(sID_name, e.getValue());
			}
		} else if (null != getBillCardPanel().getBillData().getTailItem(strColName)) {
		} else if (e.getPos() == 1 && null != getBillCardPanel().getBillData().getBodyItem(strColName)) {
			if (getBillCardPanel().getBodyItem(strColName).getComponent()
				instanceof UIRefPane) {
				if (!sID_name.trim().equals(strColName)) {
					m_voBill.setItemValue(
						rownum,
						sID_name,
						((nc.ui.pub.beans.UIRefPane) getBillCardPanel()
							.getBodyItem(strColName)
							.getComponent())
							.getRefName());
					m_voBill.setItemValue(
						rownum,
						strColName,
						((nc.ui.pub.beans.UIRefPane) getBillCardPanel()
							.getBodyItem(strColName)
							.getComponent())
							.getRefPK());
					//����ʾ��
					getBillCardPanel().setBodyValueAt(
						((nc.ui.pub.beans.UIRefPane) getBillCardPanel()
							.getBodyItem(strColName)
							.getComponent())
							.getRefName(),
						rownum,
						strColName);
				} else if (
					!GeneralMethod.getIDColName(getBillCardPanel(), strColName).equals(
						strColName)) {
					sID_name = GeneralMethod.getIDColName(getBillCardPanel(), strColName);
					m_voBill.setItemValue(
						rownum,
						strColName,
						((nc.ui.pub.beans.UIRefPane) getBillCardPanel()
							.getBodyItem(strColName)
							.getComponent())
							.getRefName());
					m_voBill.setItemValue(
						rownum,
						sID_name,
						((nc.ui.pub.beans.UIRefPane) getBillCardPanel()
							.getBodyItem(strColName)
							.getComponent())
							.getRefPK());
					//����ʾ��
					getBillCardPanel().setBodyValueAt(
						((nc.ui.pub.beans.UIRefPane) getBillCardPanel()
							.getBodyItem(strColName)
							.getComponent())
							.getRefName(),
						rownum,
						strColName);
				} else {
//					m_voBill.setItemValue(rownum, sID_name, e.getValue());
				}
			} else {
				m_voBill.setItemValue(rownum, sID_name, e.getValue());
			}
		}
	}
	//********************************************************************//

	//����ͷ��

	if (e.getKey().equals("coutwarehouseid")) { //����ֿ�
		afterWhOutEdit(e);
	} else if (e.getKey().equals("cinwarehouseid")) { //���ֿ�
		afterWhInEdit(e);
	}else if (e.getKey().equals("coutbsor")) { //���ֿ�
		afterBsorEdit(new String[]{"coutbsor","coutbsorname"}, new String[]{"coutdeptid","coutdeptname"});
		
	}else if (e.getKey().equals("cinbsrid")) { //���ֿ�
		afterBsorEdit(new String[]{"cinbsrid","cinbsrname"}, new String[]{"cindeptid","cindeptname"});
		
	}
	else if (BillMode.Browse != m_iMode && e.getKey().startsWith("vuserdef")) {// �Զ������zhy
		afterDefEdit(e);
	}


	//��������
	if (rownum == -1) {
		return;
	}
   	if (e.getKey().equals("cinventorycode")) {
		afterInvMutiEdit(e);
	} else if (e.getKey().equals("vfree0")) {
		afterFreeItemEdit(e);
	} else if (e.getKey().equals("vbatchcode")) {
		afterLotEdit(e);
	} else if (e.getKey().equals("castunitname")) {
		afterAstUOMEdit(rownum);
	} else if (e.getKey().equals("je")) {
		mustNoNegative(strColName, rownum, getBillCardPanel(), m_voBill);
	} else if (e.getKey().equals("nprice")) {
		mustNoNegative(strColName, rownum, getBillCardPanel(), m_voBill);
	} else if (e.getKey().equals(m_sNumItemKey)) {
		mustNoNegative(strColName, rownum, getBillCardPanel(), m_voBill);
	} else if (e.getKey().equals(m_sAstItemKey)) {
		mustNoNegative(strColName, rownum, getBillCardPanel(), m_voBill);
	} else if (e.getKey().equals("dvalidate")) {
		afterValidateEdit(e);
	} else if (e.getKey().equals("scrq")) {
		afterProducedateEdit(e);
	} else if (e.getKey().equals("hsl")) {
		mustNoNegative(strColName, rownum, getBillCardPanel(), m_voBill);
		afterHslEdit(rownum);
	} else if(e.getKey().equals("cvendorid")){
	    afterVendorEdit(rownum);
	}else if(e.getKey().equals("cvendorname")){
		afterVendorEdit(rownum);
	}
	
	//zhx added for bill row no, after edit process.
	else if (e.getKey().equals(m_sBillRowNo)) {
		nc.ui.scm.pub.report.BillRowNo.afterEditWhenRowNo(
			getBillCardPanel(),
			e,
			m_sBillTypeCode);
		//ͬ����VO
		m_voBill.setItemValue(
			rownum,
			m_sBillRowNo,
			getBillCardPanel().getBodyValueAt(rownum, m_sBillRowNo));

	}
}

	/**
	   * �˴����뷽��˵����
	   * �������ڣ�(2001-3-23 2:02:27)
	   * @param e ufbill.BillEditEvent
	   */
	public void bodyRowChange(nc.ui.pub.bill.BillEditEvent e) {
		int colnow, rownow;

		getBillCardPanel().rememberFocusComponent();
		//��Ӧ�б���ʽ�±�ͷѡ��ı��¼���
		if (e.getSource() == this.getBillListPanel().getHeadTable())
		{
			//�����λ��2003-07-21 ydy
			clearOrientColor();

			listSelectionChanged(e);
		}


		if (e.getSource() == this.getBillListPanel().getBodyTable())
			m_iLastSelCardBodyRow= e.getRow();

		if (e.getSource() == this.getBillCardPanel().getBillTable()) {
			rownow= e.getRow();

			m_iLastSelCardBodyRow= rownow;
			
			//��ʾ��β����
			setTailValue(rownow);


			//��δ�����Զ�����
			//if (((BillMode.New == m_iMode) || (BillMode.Update == m_iMode))
				//&& (rownow == getBillCardPanel().getRowCount() - 1)) {
				//this.onAddRow();
			//}
		}
		getBillCardPanel().restoreFocusComponent();
	}

	/**
	 * getTitle ����ע�⡣
	 */
	public String getTitle() {
		return m_Title;
	}

	/**
	 * ÿ�������׳��쳣ʱ������
	 * @param exception java.lang.Throwable
	 */
	protected void handleException(java.lang.Throwable exception) {

		/* ��ȥ���и��е�ע�ͣ��Խ�δ��׽�����쳣��ӡ�� stdout�� */
		nc.vo.scm.pub.SCMEnv.out("--------- δ��׽�����쳣 ---------");
		nc.vo.scm.pub.SCMEnv.error(exception);
	}

/**
   * ��ʼ���ࡣ
   */
/* ���棺�˷������������ɡ� */
protected void initialize() {

		//�õ����������������浽��Ա������
		getCEnvInfo();
		initialize(m_sCorpID,m_sUserID,m_sUserName,null,null,m_sLogDate);
	}

	/**
	 * ����ڵ� - ��������ΪӦ�ó�������ʱ���������������
	 * @param args java.lang.String[]
	 */
	public static void main(java.lang.String[] args) {
		try {
			javax.swing.JFrame frame= new javax.swing.JFrame();
			SpecialBillBaseUI aClientUI;
			aClientUI= new SpecialBillBaseUI();
			frame.setContentPane(aClientUI);
			frame.setSize(aClientUI.getSize());
			frame.addWindowListener(new java.awt.event.WindowAdapter() {
				public void windowClosing(java.awt.event.WindowEvent e) {
					System.exit(0);
				};
			});
			frame.show();
			java.awt.Insets insets= frame.getInsets();
			frame.setSize(
				frame.getWidth() + insets.left + insets.right,
				frame.getHeight() + insets.top + insets.bottom);
			frame.setVisible(true);
		} catch (Throwable exception) {
			nc.vo.scm.pub.SCMEnv.out("nc.ui.pub.ToftPanel �� main() �з����쳣");
			nc.vo.scm.pub.SCMEnv.error(exception);
		}
	}

/**
 * onButtonClicked ����ע�⡣
 */
public void onButtonClicked(nc.ui.pub.ButtonObject bo) { //finished
	showHintMessage(bo.getName());
	//���˵���<����>
	//�����λ��2003-07-21 ydy
	clearOrientColor();


	if (bo == m_boAdd)
		onAdd();
	else if (bo == m_boChange)
		onChange();
	else if (bo == m_boDelete)
		onDelete();
	else if (bo == m_boCopyBill)
		onCopyBill();
	else if (bo == m_boSave)
		onSave();
	else if (bo == m_boCancel)
		onCancel();

	else if (bo == m_boAddRow)
		onAddRow();
	else if (bo == m_boDeleteRow)
		onDeleteRow();
	else if (bo == m_boInsertRow)
		onInsertRow();
	else if (bo == m_boCopyRow)
		onCopyRow();
	else if (bo == m_boPasteRow)
		onPasteRow();
	else if (bo == m_boPasteRowTail)
		onPasteRowTail();
	else if (bo == m_boNewRowNo)
		onAddNewRowNo();
  else if (bo == m_boLineCardEdit)
    onLineCardEdit();

	else if (bo == m_boAuditBill)
		onAuditBill();
	else if (bo == m_boCancelAudit)
		onCancelAudit();

	else if (bo == m_boQuery)
		onQuery();
	else if (bo == m_boJointCheck)
		onJointCheck();
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
	
	else if (null != m_pageBtn && bo == m_pageBtn.getFirst())
		onFirst();
	else if (null != m_pageBtn && bo == m_pageBtn.getPre())
		onPrevious();
	else if (null != m_pageBtn && bo == m_pageBtn.getNext())
		onNext();
	else if (null != m_pageBtn && bo == m_pageBtn.getLast())
		onLast();

}

/**
 * �����ˣ�������
�������ڣ�2008-4-3����10:48:42 
����ԭ���ڿ�Ƭģʽ��ת���һ�š� 
 */
protected void onFirst() {
	if (m_alListData != null && m_alListData.size() > 0) {
		int iAll = m_alListData.size();
		scrollBill(0);
		m_pageBtn.first(iAll);
		updateButtons();
	}
}

/**
 * �����ˣ�������
�������ڣ�2008-4-3����10:48:42
����ԭ���ڿ�Ƭģʽ��ָ��ǰһ�� 
 */
protected void onPrevious() {
	if (m_alListData != null && m_alListData.size() > 0) {
		int iAll = m_alListData.size();
		int iCur = m_iLastSelListHeadRow - 1;
		scrollBill(iCur);
		m_pageBtn.previous(iAll, iCur);
		updateButtons();
	}
}

/**
 * �����ˣ�������
�������ڣ�2008-4-3����10:48:42
����ԭ���ڿ�Ƭģʽ��ָ����һ�� 
 */
protected void onNext() {
	if (m_alListData != null && m_alListData.size() > 0) {
		int iAll = m_alListData.size();
		int iCur = m_iLastSelListHeadRow + 1;
		scrollBill(iCur);
		m_pageBtn.next(iAll, iCur);
		updateButtons();
	}
}

/**
 * �����ˣ�������
�������ڣ�2008-4-3����10:48:42
����ԭ���ڿ�Ƭģʽ��ָ�����һ�� 
 */
protected void onLast() {
	if (m_alListData != null && m_alListData.size() > 0) {
		int iAll = m_alListData.size();
		int iCur = iAll - 1;
		scrollBill(iCur);
		m_pageBtn.last(iAll);
		updateButtons();
	}
}

protected int m_iCurDispBillNum = -1;
/**
 * �����ˣ�������
�������ڣ�2008-4-3����11:18:46
����ԭ�򣺴���ҳ��
 * 
 * @param iSelect
 *            int
 */
protected void scrollBill(int iCur) {
	if (m_alListData != null && m_alListData.size() > 0) {
		m_voBill = (SpecialBillVO) m_alListData.get(iCur);
		SpecialBillItemVO voitem[] = m_voBill.getItemVOs();
		if (voitem == null || voitem.length == 0)
			qryItems(new int[] { iCur }, new String[] { m_voBill.getPrimaryKey() });
		// re-get
		m_voBill = (SpecialBillVO) m_alListData.get(iCur);
		setBillValueVO(m_voBill);
		m_iLastSelListHeadRow = iCur;
		m_iCurDispBillNum = m_iLastSelListHeadRow;
		selectListBill(iCur);
	}
}
/** ���ñ���ť״̬
  * �˴����뷽��˵����
  * �������ڣ�(2001-4-30 13:58:35)
  */
protected void setButtonState() {
	switch (m_iMode) {
		case BillMode.New : //����
			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON","UPPSCMCommon-000287")/*@res "����"*/);

			m_billMng.setEnabled(true);
			m_boAdd.setEnabled(false);
			m_boChange.setEnabled(false);
			m_boDelete.setEnabled(false);
			m_boJointAdd.setEnabled(false);
			m_boCopyBill.setEnabled(false);
			m_boSave.setEnabled(true);
			m_boCancel.setEnabled(true);

			m_billRowMng.setEnabled(true);
			m_boAddRow.setEnabled(true);
			m_boDeleteRow.setEnabled(true);
			m_boInsertRow.setEnabled(true);
			m_boCopyRow.setEnabled(true);
			m_boPasteRow.setEnabled(m_bCopyRow);

			m_boAuditBill.setEnabled(false);
			m_boCancelAudit.setEnabled(false);
			m_boOut.setEnabled(false);
			m_boIn.setEnabled(false);

			m_boQuery.setEnabled(false);
			m_boLocate.setEnabled(false);
			m_boPrint.setEnabled(false);
			m_boPreview.setEnabled(false);
			m_boList.setEnabled(false);

			m_boRowQuyQty.setEnabled(true);
			setPageBtnStatus(0, 0);
			if (null != m_pageBtn)
				m_pageBtn.setPageBtnVisible(true);
			break;
		case BillMode.Update : //�޸�

			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UC001-0000045")/*@res "�޸�"*/);

			m_billMng.setEnabled(true);
			m_boAdd.setEnabled(false);
			m_boChange.setEnabled(false);
			m_boDelete.setEnabled(false);
			m_boJointAdd.setEnabled(false);
			m_boCopyBill.setEnabled(false);
			m_boSave.setEnabled(true);
			m_boCancel.setEnabled(true);

			m_billRowMng.setEnabled(true);
			m_boAddRow.setEnabled(true);
			m_boDeleteRow.setEnabled(true);
			m_boInsertRow.setEnabled(true);
			m_boCopyRow.setEnabled(true);
			m_boPasteRow.setEnabled(m_bCopyRow);

			m_boAuditBill.setEnabled(false);
			m_boCancelAudit.setEnabled(false);
			m_boOut.setEnabled(false);
			m_boIn.setEnabled(false);

			m_boQuery.setEnabled(false);
			m_boLocate.setEnabled(false);
			m_boPrint.setEnabled(false);
			m_boPreview.setEnabled(false);
			m_boList.setEnabled(false);

			m_boRowQuyQty.setEnabled(true);
			setPageBtnStatus(0, 0);
			if (null != m_pageBtn)
				m_pageBtn.setPageBtnVisible(true);
			break;
		case BillMode.Browse : //���

			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UC001-0000021")/*@res "���"*/);

			m_billMng.setEnabled(true);
			m_boAdd.setEnabled(true);
			m_boChange.setEnabled(
				(m_iTotalListHeadNum > 0)
					&& ((getBillCardPanel().getTailItem("cauditorid").getValue() == null)
						|| (getBillCardPanel().getTailItem("cauditorid").getValue().length() == 0)));
			m_boDelete.setEnabled(
				(m_iTotalListHeadNum > 0)
					&& ((getBillCardPanel().getTailItem("cauditorid").getValue() == null)
						|| (getBillCardPanel().getTailItem("cauditorid").getValue().length() == 0)));
			m_boJointAdd.setEnabled(true);
			
			m_boCopyBill.setEnabled(m_iTotalListHeadNum > 0);
			if(m_iMode!=BillMode.List){
				if((getBillCardPanel().getHeadItem("vbillcode").getValue() == null)
						|| (getBillCardPanel().getHeadItem("vbillcode").getValue().length() == 0))
					m_boCopyBill.setEnabled(false);
			}
			m_boSave.setEnabled(false);
			m_boCancel.setEnabled(false);

			m_billRowMng.setEnabled(false);
			m_boAddRow.setEnabled(false);
			m_boDeleteRow.setEnabled(false);
			m_boInsertRow.setEnabled(false);
			m_boCopyRow.setEnabled(false);
			m_boPasteRow.setEnabled(false);

			m_boCancelAudit.setEnabled(
				(m_iTotalListHeadNum > 0)
					&& (getBillCardPanel().getTailItem("cauditorid").getValue() != null)
					&& (getBillCardPanel().getTailItem("cauditorid").getValue().length() != 0));
			if (m_boCancelAudit.isEnabled()) {
				m_boAuditBill.setEnabled(false);
			} else {
				m_boAuditBill.setEnabled((m_iTotalListHeadNum > 0));
				//if (m_boAuditBill.isEnabled()) {
				m_boCancelAudit.setEnabled(false);
				//} else {
				//m_boCancelAudit.setEnabled(false);
				//}
			}

			m_boOut.setEnabled(m_iTotalListHeadNum > 0);
			m_boIn.setEnabled(m_iTotalListHeadNum > 0);

			m_boQuery.setEnabled(true);
			m_boLocate.setEnabled((m_iTotalListHeadNum > 0));
			m_boPrint.setEnabled(true);
			m_boPreview.setEnabled(true);
			m_boList.setEnabled(true);

			m_boRowQuyQty.setEnabled(true);
			if (null !=m_alListData)
			setPageBtnStatus(m_alListData.size(), m_iLastSelListHeadRow);
			if (null != m_pageBtn)
				m_pageBtn.setPageBtnVisible(true);
			break;
		case BillMode.List : //�б�״̬

			m_billMng.setEnabled(true);
			m_boAdd.setEnabled(true);

			m_boChange.setEnabled(
				(m_alListData != null
					&& m_alListData.size() > 0
					&& m_iLastSelListHeadRow >= 0
					&& m_iLastSelListHeadRow < m_alListData.size())
					&& ((getBillListPanel()
						.getHeadBillModel()
						.getValueAt(m_iLastSelListHeadRow, "cauditorname")
						== null)
						|| (getBillListPanel()
							.getHeadBillModel()
							.getValueAt(m_iLastSelListHeadRow, "cauditorname")
							.toString()
							.trim()
							.length()
							== 0)));
			m_boDelete.setEnabled(m_boChange.isEnabled());
			//��ֹ�޸ĺ�ɾ��
			//m_boChange.setEnabled(false);
			//m_boDelete.setEnabled(false);
			m_boJointAdd.setEnabled(true);
			m_boCopyBill.setEnabled(
				m_iTotalListHeadNum > 0
					&& getBillListPanel().getHeadTable().getSelectedRows().length == 1);
			m_boSave.setEnabled(false);
			m_boCancel.setEnabled(false);

			m_billRowMng.setEnabled(false);
			m_boAddRow.setEnabled(false);
			m_boDeleteRow.setEnabled(false);
			m_boInsertRow.setEnabled(false);
			m_boCopyRow.setEnabled(false);
			m_boPasteRow.setEnabled(false);

			m_boAuditBill.setEnabled(false);
			m_boCancelAudit.setEnabled(false);
			m_boOut.setEnabled(false);
			m_boIn.setEnabled(false);

			m_boQuery.setEnabled(true);
			m_boLocate.setEnabled(true);
			m_boPrint.setEnabled(true);
			m_boPreview.setEnabled(true);
			m_boList.setEnabled(true);

			m_boRowQuyQty.setEnabled(false);
			setPageBtnStatus(0, 0);
			if (null != m_pageBtn)
				m_pageBtn.setPageBtnVisible(false);
			break;
	}
	//ʹ������Ч
	if (m_aryButtonGroup != null) {
		updateButtons();
	}
}

private BillTempletVO billTempletVO = null;
public BillTempletVO getDefaultTemplet() {
	if (null == billTempletVO) {
		billTempletVO = BillUIUtil.getDefaultTempletStatic(m_sBillTypeCode, null, m_sUserID, m_sCorpID, null, null);
	}
	return billTempletVO;

}

/**
	  * ���� BillCardPanel1 ����ֵ �� * @ return nc.ui.pub.bill.BillCardPanel
	  */
/* ���棺�˷������������ɡ� */
protected BillCardPanel getBillCardPanel() {
	if (ivjBillCardPanel == null) {
		try {
			ivjBillCardPanel = new nc.ui.pub.bill.BillCardPanel();
			ivjBillCardPanel.setName("BillCardPanel");
			ivjBillCardPanel.setAlignmentY(0.0F);
			ivjBillCardPanel.setAlignmentX(0.0F);
			ivjBillCardPanel.setBounds(1, 1, 774, 418);
			// user code begin {1}
			/*
			nc.vo.pub.bill.BillTempletVO btv = ivjBillCardPanel.getTempletData("System987679310412");
			BillData bd = new nc.ui.pub.bill.BillData(btv);
			bd.getBodyItem("vbatchcode").setComponent(new LotNumbRefPane());
			bd.getHeadItem("pk_corp").setComponent(new LotNumbRefPane());
			bd.getTailItem("cwhsmanagerid").setComponent(new LotNumbRefPane());
			*/

			//ȡ������Դ
			//����ģ������
			//ivjBillCardPanel.loadTemplet(m_sBillTypeCode, null, m_sUserID, m_sCorpID);
			//BillData bd= ivjBillCardPanel.getBillData();
			BillData bd =
				new BillData(getDefaultTemplet());
			if (bd == null) {
				nc.vo.scm.pub.SCMEnv.out("--> billdata null.");
				return ivjBillCardPanel;
			}
			//------------
			if (bd.getBodyItem("vfree0") != null) {
				//�ı��²��յĳ���
				getFreeItemRefPane().setMaxLength(bd.getBodyItem("vfree0").getLength());
				//�����µĲ��գ�Ҫ��ָ����Ӧ���ֶ���
				bd.getBodyItem("vfree0").setComponent(getFreeItemRefPane()); //����,������
			}
			//--------------------
			if (bd.getBodyItem("vbatchcode") != null) {
				//�ı��²��յĳ���
				getLotNumbRefPane().setMaxLength(bd.getBodyItem("vbatchcode").getLength());
				//�����µĲ��գ�Ҫ��ָ����Ӧ���ֶ���
				//bd.getHeadItem("pk_corp").setComponent(new LotNumbRefPane());//��ͷ
				bd.getBodyItem("vbatchcode").setComponent(getLotNumbRefPane()); //����,����
				//bd.getTailItem("cwhsmanagerid").setComponent(new LotNumbRefPane());//��β
			}

			//�޸��Զ�����
			bd = changeBillDataByUserDef(getDefHeadVO(),getDefBodyVO(),bd);
			ivjBillCardPanel.setBillData(bd);
			
			bd = BatchCodeDefSetTool.changeBillDataByBCUserDef(m_sCorpID,bd);
			ivjBillCardPanel.setBillData(bd);

			//��ʼ��С��λ������initSysParam�����
			bd = setScale(bd);
			//��������Դ
			ivjBillCardPanel.setBillData(bd);

			//for (int i= 0;
			//i < ivjBillCardPanel.getBillData().getBodyShowItems().length;
			//i++) {
			////String dd=ivjBillCardPanel.getBillData().getBodyShowItems()[i].getDataType()RefType();
			//if (ivjBillCardPanel.getBillData().getBodyShowItems()[i].getDataType()
			//== BillItem.UFREF) {
			//(
			//(UIRefPane) ivjBillCardPanel
			//.getBillData()
			//.getBodyShowItems()[i]
			//.getComponent())
			//.setReturnCode(
			//false);
			//}
			//}

			//ԭ����ȡ������������Դ����
			//ivjBillCardPanelDemand.setBillData(new BillData(getBillTempletVO()));
			//ivjBillCardPanel.loadTemplet("System987679310419");

			//ivjBillCardPanelDemand.getBillData().getHeadItems();
			if (bd.getHeadItem("cbilltypecode") != null)
				m_Title = bd.getHeadItem("cbilltypecode").getName();
			if (ivjBillCardPanel.getTitle() != null
				&& ivjBillCardPanel.getTitle().trim().length() > 0)
				m_Title = ivjBillCardPanel.getTitle();
			//���õ��ݺ��Ƿ�ɱ༭
			if (bd.getHeadItem("vbillcode") != null)
				bd.getHeadItem("vbillcode").setEnabled(false);
			/*ivjBillCardPanel.getBillTable().setSelectionMode(
				javax.swing.ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);*/

			//ivjBillCardPanel.setBodyMenuShow(false);
			//ivjBillCardPanel.addEditListener(this);
			//ivjBillCardPanel.addBodyMenuListener(new BillMenuListener());

			////����λ���
			//(
			//(nc.ui.pub.beans.UIRefPane) ivjBillCardPanel
			//.getBodyItem("cinventorycode")
			//.getComponent())
			//.setWhereString(
			//"bd_invmandoc.pk_corp='" + m_sCorpID + "'");
			//if (packageID.equals("221")) {
			////��ͷ�Ƿ�Ʒ�ֿ�
			//(
			//(nc.ui.pub.beans.UIRefPane) ivjBillCardPanel
			//.getHeadItem("coutwarehouseid")
			//.getComponent())
			//.setWhereString(
			//"gubflag='N' and sealflag='N' and pk_corp='" + m_sCorpID + "'");
			//}
			//if ((packageID.equals("231"))
			//|| (packageID.equals("232"))
			//|| (packageID.equals("233"))) {
			////����Ƿ�Ʒ�ֿ�
			//(
			//(nc.ui.pub.beans.UIRefPane) ivjBillCardPanel
			//.getBodyItem("cwarehouseid")
			//.getComponent())
			//.setWhereString(
			//"gubflag='N' and sealflag='N' and pk_corp='" + m_sCorpID + "'");
			//}
			//zhx new add billrowno
			nc.ui.scm.pub.report.BillRowNo.loadRowNoItem(ivjBillCardPanel,m_sBillRowNo);
			//��ԭ����ģ��ı���������!
			ivjBillCardPanel.getBodyPanel().setRowNOShow(nc.ui.ic.pub.bill.Setup.bShowBillRowNo);
			ivjBillCardPanel.setTatolRowShow(true); 
      
      GeneralBillUICtl.setBillCardPaneFillEnableForSpecial(ivjBillCardPanel);
     
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjBillCardPanel;
}

protected BillData changeBillDataByUserDef(DefVO[] defHead,DefVO[] defBody,BillData oldBillData) {

	//�����Զ��������

//	defs= nc.ui.bd.service.BDDef.queryDefVO("��Ӧ��/ARAP����ͷ", m_sCorpID);
	if ((defHead != null)) {
		oldBillData.updateItemByDef(defHead, "vuserdef", true);
		for(int i = 1; i < 20; i++){
  			nc.ui.pub.bill.BillItem item = oldBillData.getHeadItem("vuserdef"+i);
  			if(item != null && item.getDataType() == nc.ui.pub.bill.BillItem.USERDEF){
   				((nc.ui.pub.beans.UIRefPane)item.getComponent()).setAutoCheck(true);
 			 }
		 }
	}
	//����
	//��ö�Ӧ�ڹ�˾�ĸõ��ݵ��Զ���������
//	defs= nc.ui.bd.service.BDDef.queryDefVO("��Ӧ��/ARAP������", m_sCorpID);
	if ((defBody == null)) {
		return oldBillData;
	}else{
		oldBillData.updateItemByDef(defBody, "vuserdef", false);
		for(int i = 1; i < 20; i++){
  			nc.ui.pub.bill.BillItem item = oldBillData.getBodyItem("vuserdef"+i);
  			if(item != null && item.getDataType() == nc.ui.pub.bill.BillItem.USERDEF){
   				((nc.ui.pub.beans.UIRefPane)item.getComponent()).setAutoCheck(true);
 			 }
		 }
		}

	return oldBillData;
}





/**
	 * ���� BillListPanel1 ����ֵ��
	 * @return nc.ui.pub.bill.BillListPanel
	 * 
	 */
/* ���棺�˷������������ɡ� */
protected nc.ui.pub.bill.BillListPanel getBillListPanel() {
	if (ivjBillListPanel == null) {
		try {
			ivjBillListPanel= new nc.ui.pub.bill.BillListPanel();
			ivjBillListPanel.setName("BillListPanel");
			ivjBillListPanel.setVisible(false);
			ivjBillListPanel.setMaximumSize(new java.awt.Dimension(2147483647, 2147483647));
			ivjBillListPanel.setBounds(0, 0, 774, 419);
			ivjBillListPanel.setMinimumSize(new java.awt.Dimension(26, 60));
			// user code begin {1}

			//ivjBillListPanel.loadTemplet(m_sTempletID);
			//�����б�ģ��
			//ivjBillListPanel.loadTemplet(m_sBillTypeCode, null, m_sUserID, m_sCorpID);

			//BillListData bd= ivjBillListPanel.getBillListData();
			BillListData bd= new BillListData(getDefaultTemplet());
			bd= changeBillListDataByUserDef(bd);
			
			bd= BatchCodeDefSetTool.changeBillListDataByBCUserDef(m_sCorpID,bd);

			ivjBillListPanel.setListData(bd);
      
      //ivjBillListPanel.setMultiSelect(false);

//			ivjBillListPanel.getHeadTable().setSelectionMode(
//				javax.swing.ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
			//�޸��ˣ������� �޸����ڣ�2007-9-3����01:48:20 �޸�ԭ�򣺸����·�����Ҫ��Ȼ��ͳ��
			//ivjBillListPanel.getChildListPanel().setTatolRowShow(true);
			ivjBillListPanel.getChildListPanel().setTotalRowShow(true);
      
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjBillListPanel;
}

/**
   * ����
   * �������ڣ�(2001-4-18 19:45:17)
   */
public void onAdd() { //finished
	if (m_iMode == BillMode.Browse) {
	} else {
		onList();
	
	}
	
	
	getBillCardPanel().getBillModel().clearBodyData();
	//�����µı�
	getBillCardPanel().addNew();

	//�Ƿ�����룿
	getBillCardPanel().updateValue();

	m_voBill = new SpecialBillVO();
	for (int i = 1; i <= m_iFirstAddRows; i++) {
		onAddRow();
	}

	m_iMode = BillMode.New;
	setButtonState();
	setBillState();

	//�����������ݵĳ�ʼ���ݣ������ڣ��Ƶ��˵ȡ�
	setNewBillInitData();

	dispBodyRow(getBillCardPanel().getBillTable());

	m_sOldBillCode = "";

	firstSetColEditable();
	//zhx add rowno process 2003-06-26
	//nc.ui.scm.pub.report.BillRowNo.addLineRowNo(
	nc.ui.scm.pub.report.BillRowNo.addNewRowNo(
		getBillCardPanel(),
		m_sBillTypeCode,
		m_sBillRowNo);
	//���õ��ݺ��Ƿ�ɱ༭
	if (getBillCardPanel().getHeadItem("vbillcode") != null)
		getBillCardPanel().getHeadItem("vbillcode").setEnabled(m_bIsEditBillCode);

	getBillCardPanel().setTailItem("iprintcount", new Integer(0));
	getBillCardPanel().transferFocusTo(nc.ui.pub.bill.BillCardPanel.HEAD);
	
	//2005-04-29 �������ɫ����
	InvAttrCellRenderer ficr = new InvAttrCellRenderer();
	ficr.setFreeItemRenderer(getBillCardPanel(), m_voBill);
	//addied by liuzy 2008-07-03 ��Ӧ�̱�ɫ��
	SupplierItemCellRender supplier = new SupplierItemCellRender(getBillCardPanel(),m_voBill);
	supplier.setRenderer("cvendorname");

}

	/**
	* ����
	* �������ڣ�(2001-4-18 19:46:27)
	*/
	public void onAddRow() { //finished
		getBillCardPanel().addLine();
		//zhx added rowno process 030626
/*		nc.ui.scm.pub.report.BillRowNo.addNewRowNo(
			getBillCardPanel(),
			m_sBillTypeCode,
			m_sBillRowNo);*/
		nc.ui.scm.pub.report.BillRowNo.addLineRowNo(
				getBillCardPanel(),
				m_sBillTypeCode,
				m_sBillRowNo);
		nc.ui.ic.pub.tools.GenMethod.reSetRowColorWhenNOException(getBillCardPanel(),getBillCardPanel().getBillTable().getSelectedRow());
	}

	/**
	  * ȡ��
	  * �������ڣ�(2001-4-18 19:47:41)
	  */
	public void onCancel() { //finished
		
		switch (nc.ui.pub.beans.MessageDialog.showYesNoDlg(this,
				null,
				nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
				"UCH067")/* @res "�Ƿ�ȷ��Ҫȡ����" */
				,MessageDialog.ID_NO)) {

		case nc.ui.pub.beans.MessageDialog.ID_YES:
			break;
		default:return;	
		}
		
		clearBCOnCancel();
		getBillCardPanel().resumeValue();

		//switchListToBill();
		nc.ui.ic.pub.tools.GenMethod.reSetRowColorWhenNOException(getBillCardPanel());

		m_iMode= BillMode.Browse;
    m_voBillItem = null;
	
		setButtonState();
		setBillState();
		
		//2005-01-28 �������ɫ����
		InvAttrCellRenderer ficr = new InvAttrCellRenderer();
		ficr.setFreeItemRenderer(getBillCardPanel(), null);
		//addied by liuzy 2008-07-03 ��Ӧ�̱�ɫ��
		SupplierItemCellRender supplier = new SupplierItemCellRender(getBillCardPanel(),m_voBill);
		supplier.setRenderer("cvendorname");
	}
	
	public void clearBCOnCancel() {
		
	}

/**
  * �޸�
  * �������ڣ�(2001-4-18 19:45:39)
  */
public void onChange() { //finished
	if (m_iMode == BillMode.Browse) {
	} else { //m_iMode==BillMode.List
		onList();
		//ListToBill();
		//DispBodyRow(getBillCardPanel().getBillTable());
	}
	m_iMode= BillMode.Update;
	setButtonState();
	setBillState();
	//if (getBillCardPanel().getRowCount() > m_iFirstAddRows) {
		//onAddRow();
	//}

	firstSetColEditable();

	//�޸ĵ��ݱ���ǰ���б���
	m_sOldBillCode= getBillCardPanel().getHeadItem("vbillcode").getValue();
	//���õ��ݺ��Ƿ�ɱ༭
	//�޸��ˣ������� �޸�ʱ�䣺2008-7-14 ����03:38:48 �޸�ԭ�򣺵��ݺ��ڵ����޸�ʱ����ͨ�������ⵥ�����Խ����޸ġ�
	/*if (getBillCardPanel().getHeadItem("vbillcode") != null)
		getBillCardPanel().getHeadItem("vbillcode").setEnabled(false);*/

	if (m_sOldBillCode != null)
		m_sOldBillCode= m_sOldBillCode.trim();

	getBillCardPanel().transferFocusTo(nc.ui.pub.bill.BillCardPanel.HEAD);
	
	//2005-04-30 �������ɫ����
	InvAttrCellRenderer ficr = new InvAttrCellRenderer();
	ficr.setFreeItemRenderer(getBillCardPanel(), m_voBill);
	//addied by liuzy 2008-07-03 ��Ӧ�̱�ɫ��
	SupplierItemCellRender supplier = new SupplierItemCellRender(getBillCardPanel(),m_voBill);
	supplier.setRenderer("cvendorname");
	
	setUpdateBillInitData();

}
/**
 * �����ߣ�cqw ���ܣ������޸ĵ��ݵĳ�ʼ���� ������ ���أ� ���⣺ ���ڣ�(2005-04-04 19:23:32)
 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
 * 
 * 
 * 
 *  
 */
protected void setUpdateBillInitData() {
	try {
		//----------------------------- tail values
		// -----------------------------

//		if (getBillCardPanel().getTailItem("clastmodiid") != null)
//			getBillCardPanel().setTailItem("clastmodiid", m_sUserID);
//		if (getBillCardPanel().getTailItem("clastmodiname") != null)
//			getBillCardPanel().setTailItem("clastmodiname", m_sUserName);
		//UFDateTime ufdPre = new UFDateTime(System.currentTimeMillis());
/*		if (getBillCardPanel().getTailItem("tlastmoditime") != null)
			getBillCardPanel().setTailItem("tlastmoditime",
					ufdPre.toString());*/
		if (m_voBill != null) {
//			m_voBill.setHeaderValue("clastmodiid", m_sUserID);
//			m_voBill.setHeaderValue("clastmodiname", m_sUserName);
			//m_voBill.setHeaderValue("tlastmoditime", ufdPre.toString());
		}

	} catch (Exception e) {
		nc.vo.scm.pub.SCMEnv.error(e);

	}

}

/**
  * �����ߣ�������
  * ���ܣ���������
  				�ۼ�ת��/��������Ӧ������
  * ������
  * ���أ�
  * ���⣺
  * ���ڣ�(2001-5-10 ���� 4:15)
  * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
  */
public void onCopyBill() {
	if (m_alListData == null
		|| m_alListData.size() < m_iLastSelListHeadRow
		|| m_iLastSelListHeadRow < 0) {

		return;
	}
	if (m_iMode == BillMode.Browse) {
	} else {
		onList();
	
	}
//	����Ӹղŵı��и��Ƶ�����
	m_voBill =
		(SpecialBillVO) ((SpecialBillVO) m_alListData
			.get(m_iLastSelListHeadRow))
			.clone();
	
	getBillCardPanel().addNew();
	getBillCardPanel().updateValue();

	m_iMode = BillMode.Browse;

	

	m_voBill.setHeaderValue("cspecialhid", null);
	m_voBill.setHeaderValue("vbillcode", null);
	m_voBill.setHeaderValue("ts", null);
	m_voBill.setHeaderValue("iprintcount", new Integer(0));

	SpecialBillItemVO[] voaMyItem = m_voBill.getItemVOs();
	for (int row = 0; row < m_voBill.getChildrenVO().length; row++) {
		voaMyItem[row].setPrimaryKey(null);
		voaMyItem[row].setNchecknum(null);
		voaMyItem[row].setNcheckastnum(null);
		voaMyItem[row].setNcheckgrsnum(null);
		voaMyItem[row].setNadjustnum(null);
		voaMyItem[row].setNadjustastnum(null);
		voaMyItem[row].setNadjustgrsnum(null);
		//��Դ�������ݣ�
		voaMyItem[row].setCsourcebillbid(null);
		voaMyItem[row].setCsourcebillhid(null);
		voaMyItem[row].setCsourcetype(null);
		voaMyItem[row].setVsourcebillcode(null);
		voaMyItem[row].setCfirstbillbid(null);
		voaMyItem[row].setCfirstbillhid(null);
		voaMyItem[row].setCfirsttype(null);
		//ts
		voaMyItem[row].setTs(null);
		
		voaMyItem[row].setSpecailBarCodeVOs(null);
		voaMyItem[row].setSpecailBarCode1VOs(null);
		voaMyItem[row].setNbarcodenum(null);
		voaMyItem[row].setIsloadaccountbc(UFBoolean.FALSE);
		voaMyItem[row].setIsloadcountbc(UFBoolean.FALSE);
		
		voaMyItem[row].setNaccountnum(null);
		voaMyItem[row].setNaccountastnum(null);
		voaMyItem[row].setNaccountgrsnum(null);
		
		voaMyItem[row].setAttributeValue("cysl", null);
		voaMyItem[row].setAttributeValue("cyfsl", null);
		voaMyItem[row].setAttributeValue("ndiffgrsnum", null);
		

	}
	setBillValueVO(m_voBill);

	setNewBillInitData();

	m_iMode = BillMode.New;
	setButtonState();
	setBillState();
	dispBodyRow(getBillCardPanel().getBillTable());

	m_sOldBillCode = "";
	//���õ��ݺ��Ƿ�ɱ༭
	if (getBillCardPanel().getHeadItem("vbillcode") != null)
		getBillCardPanel().getHeadItem("vbillcode").setEnabled(m_bIsEditBillCode);

	getBillCardPanel().getTailItem("iprintcount").setValue(new Integer(0));

	firstSetColEditable();
	
	//2005-04-30 �������ɫ����
	InvAttrCellRenderer ficr = new InvAttrCellRenderer();
	ficr.setFreeItemRenderer(getBillCardPanel(), m_voBill);
	//addied by liuzy 2008-07-03 ��Ӧ�̱�ɫ��
	SupplierItemCellRender supplier = new SupplierItemCellRender(getBillCardPanel(),m_voBill);
	supplier.setRenderer("cvendorname");
}

/**
  * �����ߣ�������
  * ���ܣ�������
  * ������
  * ���أ�
  * ���⣺
  * ���ڣ�(2001-5-10 ���� 2:50)
  * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
  */
public void onCopyRow() { //finished
	int[] iaRow = getBillCardPanel().getBillTable().getSelectedRows();
	if (iaRow != null && iaRow.length > 0) {
		m_bCopyRow = true;
		getBillCardPanel().copyLine();
		setButtonState();
		voBillCopyLine(iaRow);
	}
	else {
		showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill","UPP4008bill-000025")/*@res "����ѡ���С�"*/);
	}
}

/**
  * ɾ��
  * �������ڣ�(2001-4-18 19:46:01)
  */
public void onDelete() {
	if (m_alListData == null || m_alListData.size() == 0) {

		return;
	}

	try {
		
		switch (nc.ui.pub.beans.MessageDialog.showYesNoDlg(this,
				null,
				nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
				"UCH002")/* @res "�Ƿ�ȷ��Ҫɾ����" */
				,MessageDialog.ID_NO)) {
		case nc.ui.pub.beans.MessageDialog.ID_YES:
				if (m_iMode == BillMode.Browse) {
					SpecialBillVO vo = (SpecialBillVO)m_voBill;//new SpecialBillVO();
					SpecialBillHeaderVO voHead =(SpecialBillHeaderVO)vo.getHeaderVO().clone();// new SpecialBillHeaderVO();
					//voHead.setAttributeValue("cspecialhid", m_voBill.getHeaderValue("cspecialhid"));
					voHead.setAttributeValue("coperatorid", m_sUserID);
					//voHead.setCbilltypecode(m_sBillTypeCode);
					//voHead.setAttributeValue("pk_corp", m_sCorpID);
					//vo.setParentVO(voHead);

					//set delete flag-------  obligatory for ts test.
					vo.setStatus(VOStatus.UNCHANGED);
					vo.setHeaderValue("coperatoridnow", m_sUserID);
					

					//ArrayList alsPrimaryKey=
					//(ArrayList)
          
          //add by liuzy 2007-11-02 10:16 ѹ������
          ObjectUtils.objectReference(vo);
          
					nc.ui.pub.pf.PfUtilClient.processAction(
						"DELETE",
						m_sBillTypeCode,
						m_sLogDate,
						vo);

					minusBillVO();
					showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill","UPP4008bill-000010")/*@res "ɾ���ɹ���"*/);
					m_iFirstSelListHeadRow = -1;
					switchListToBill();
				} else {
					//ArrayList alRowNumbers = new ArrayList();
					int[] iSelectedRows=getBillListPanel().getHeadTable().getSelectedRows();
					if(iSelectedRows!=null&&iSelectedRows.length!=1){
						showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill","UPP4008bill-000144")/*@res "��ѡ��һ�ŵ���ɾ����"*/);
						return;
					}

					//for (int i = 0; i < m_iTotalListHeadNum; i++) {
						//if (getBillListPanel().getHeadTable().isRowSelected(i)) {
							//alRowNumbers.add(Integer.toString(i));
						//}
					//}
					//String[] sSpecialHVOpks = new String[iSelectedRows.length];
					//for (int i= 0; i < alRowNumbers.size(); i++) {
					//for (int i = alRowNumbers.size() - 1; i >= 0; i--) {
						//sSpecialHVOpks[i] =
							//((SpecialBillVO) m_alListData
								//.get(Integer.parseInt(alRowNumbers.get(i).toString())))
								//.getParentVO()
								//.getPrimaryKey()
								//.trim();
						SpecialBillVO vo =(SpecialBillVO) m_alListData.get(iSelectedRows[0]);//new SpecialBillVO();
						SpecialBillHeaderVO voHead = (SpecialBillHeaderVO)vo.getHeaderVO().clone();
						//voHead.setAttributeValue("cspecialhid", sSpecialHVOpks[i]);
						//voHead.setCbilltypecode(m_sBillTypeCode);
						voHead.setAttributeValue("coperatorid", m_sUserID);
						voHead.setAttributeValue("coperatoridnow", m_sUserID);
						//voHead.setAttributeValue("pk_corp", m_sCorpID);
						//vo.setParentVO(voHead);
						vo.setHeaderValue("coperatoridnow", m_sUserID);
            
            //add by liuzy 2007-11-02 10:16 ѹ������
            ObjectUtils.objectReference(vo);

						nc.ui.pub.pf.PfUtilClient.processAction(
							"DELETE",
							m_sBillTypeCode,
							m_sLogDate,
							vo);

						//m_iLastSelListHeadRow = Integer.parseInt(alRowNumbers.get(i).toString());
						minusBillVO();
					//}
					showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill","UPP4008bill-000010")/*@res "ɾ���ɹ���"*/);
					m_iFirstSelListHeadRow = -1;
					switchBillToList();
				}
				setButtonState();
				setBillState();
				break;
		default:return;	
		}
	} catch (Exception e) {
		nc.vo.scm.pub.SCMEnv.out("����ͨѶʧ�ܣ�");
		GenMethod.handleException(this, null, e);
		//handleException(e);
		//nc.ui.pub.beans.MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON","UPPSCMCommon-000059")/*@res "����"*/, e.getMessage());
	} finally {
		if (m_iMode == BillMode.Browse) {
			switchListToBill();
		} else {
			switchBillToList();
		}
		setButtonState();
		setBillState();
	}
}

	/**
	  * ɾ��
	  * �������ڣ�(2001-4-18 19:46:41)
	  */
	public void onDeleteRow() { //finished
		//switch (nc.ui.pub.beans.MessageDialog.showOkCancelDlg(this, "��ʾ", "��ȷ��ɾ����")) {
		//case nc.ui.pub.beans.MessageDialog.ID_OK :
		nc.ui.ic.pub.tools.GenMethod.reSetRowColorWhenNOException(getBillCardPanel(),getBillCardPanel().getBillTable().getSelectedRow());
		getBillCardPanel().delLine();
		if (getBillCardPanel().getRowCount() < 1)
			onAddRow();
		dispBodyRow(getBillCardPanel().getBillTable());
		//break;
		//case nc.ui.pub.beans.MessageDialog.ID_CANCEL :
		//return;
		//}
	}



	/**
	* ����¼��
	* �������ڣ�(2001-4-18 19:48:21)
	*/
	public void onJointAdd() {
		//m_iMode= BillMode.New;
		setButtonState();
		setBillState();
		//��������������ı��ѡ��,����������
	}

/** �б�\��״̬�л�
  * �˴����뷽��˵����
  * �������ڣ�(2001-4-18 19:48:05)
  */
public void onList() { //finished
	if (m_iMode == BillMode.Browse) {
		m_iMode = BillMode.List;
		switchBillToList();
	} else {
		m_iMode = BillMode.Browse;
		switchListToBill();
		//2010-11-08 MeiChao begin ����Ƭ�л�ʱ,��������Ϣ��������Ƭҳ��
		if(this.getBillCardPanel().getBillModel("jj_scm_informationcost")!=null){
			this.getBillCardPanel().getBillModel("jj_scm_informationcost")
			.setBodyDataVO(expenseVOs);
			this.getBillCardPanel().updateUI();
		}
		//2010-11-08 MeiChao end ����Ƭ�л�ʱ,��������Ϣ��������Ƭҳ��
	}
	m_iFirstSelListHeadRow = m_iLastSelListHeadRow;
	showBtnSwitch();
	setButtonState();
	setBillState();
}

/**
 * �����ߣ����˾�
 * ���ܣ���λ����
 * ������
 * ���أ�
 * ���⣺
 * ���ڣ�(2001-5-9 9:23:32)
 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
 *
 *
 *
 *
 */
protected void onLocate() {
	if (m_alListData == null || m_alListData.size() < 1)
		return;


	nc.ui.scm.pub.report.OrientDialog dlgOrient = null;
	if (m_iMode == BillMode.Card) {
		dlgOrient = new  nc.ui.scm.pub.report.OrientDialog(
			this,
			getBillCardPanel().getBillModel(),
			getBillCardPanel().getBodyItems(),
			getBillCardPanel().getBillTable()
			);
		dlgOrient.showModal();
		if (dlgOrient.getResult() == nc.ui.pub.beans.UIDialog.ID_OK) {
			m_isLocated = true;
		}
	}
	else {
		dlgOrient = new  nc.ui.scm.pub.report.OrientDialog(
			this,
			getBillListPanel().getHeadBillModel(),
			getBillListPanel().getBillListData().getHeadItems(),
			getBillListPanel().getHeadTable()
			);

		dlgOrient.showModal();
		if (dlgOrient.getResult() == nc.ui.pub.beans.UIDialog.ID_OK) {
			m_isLocated = true;
		}
	}

}



/**
  * �����ߣ�������
  * ���ܣ�ճ����
  * ������
  * ���أ�
  * ���⣺
  * ���ڣ�(2001-5-10 ���� 4:15)
  * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
  */
public void onPasteRow() {
  
  try{
	//finished added by zhx bill row no
	int iRowCount = getBillCardPanel().getBodyPanel().getTableModel().getRowCount();
	int row = getBillCardPanel().getBillTable().getSelectedRow();
	if (row < 0 ) {
		showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill","UPP4008bill-000145")/*@res "����ѡ��ճ���е�λ�á�"*/);
	} else {
    
    if(m_voBillItem==null || m_voBillItem.length<=0)
      return;
    
		getBillCardPanel().pasteLine();
    
//    int istartrow = getBillCardPanel().getBillTable().getSelectedRow() - m_voBillItem.length;
//    for(int i=0;i<m_voBillItem.length;i++)
//      getBillCardPanel().getBillModel().setBodyRowVO((SpecialBillItemVO)m_voBillItem[i].clone(), istartrow+i);
		//���ӵ�����
		iRowCount =
			getBillCardPanel().getBodyPanel().getTableModel().getRowCount() - iRowCount;
		nc.ui.scm.pub.report.BillRowNo.pasteLineRowNo(
			getBillCardPanel(),
			m_sBillTypeCode,
			m_sBillRowNo,
			iRowCount);

		voBillPastLine(row);

	}
  }finally{
    getBillCardPanel().getBillModel().setNeedCalculate(true);
  }
}

/**
* �����ߣ�������
* ���ܣ���ӡ
* ������
* ���أ�
* ���⣺
* ���ڣ�(2001-5-10 ���� 4:16)
* �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
*/
public void onPrint() {
//  ������ӡ����
	//����ǰ���б��Ǳ�������ӡ����
	if (m_iMode == BillMode.Browse) { //���
		filterNullLine();
		//׼������
		SpecialBillVO vo =
			(SpecialBillVO) getBillCardPanel().getBillValueVO(
				SpecialBillVO.class.getName(),
				SpecialBillHeaderVO.class.getName(),
				SpecialBillItemVO.class.getName());

		if (null == vo || null == vo.getParentVO() || null == vo.getChildrenVO()) {
		    return;
		}

		if (getPrintEntry().selectTemplate() < 0)
			return;

		String sBillID = vo.getPrimaryKey();//���������ID
	    ScmPrintlogVO voSpl = new ScmPrintlogVO();
	    voSpl.setCbillid(sBillID);
	    voSpl.setVbillcode(vo.getVBillCode());//���뵥�ݺţ�������ʾ��
	    voSpl.setCbilltypecode(vo.getBillTypeCode());//�������ͱ���
	    voSpl.setCoperatorid((String) (vo.getParentVO().getAttributeValue("coperatorid")) );//����ԱID
	    voSpl.setIoperatetype(new Integer(PrintConst.PAT_OK));//�̶�
	    voSpl.setPk_corp(m_sCorpID);//��˾
	    voSpl.setTs((String)(vo.getParentVO().getAttributeValue("ts")));//���������TS

	    nc.vo.scm.pub.SCMEnv.out("ts=========tata" + voSpl.getTs());
		nc.ui.scm.print.PrintLogClient plc = new nc.ui.scm.print.PrintLogClient();
		plc.setPrintEntry(getPrintEntry());
		//���õ�����Ϣ
		plc.setPrintInfo(voSpl);
		//����ts��printcountˢ�¼���.
		plc.addFreshTsListener(this);
		//���ô�ӡ����
		getPrintEntry().setPrintListener(plc);

		//���ӡ��������Դ�����д�ӡ
		getDataSource().setVO(vo);
		getPrintEntry().setDataSource(getDataSource());
		getPrintEntry().print();

	} else { //�б�
		if (null == m_alListData || m_alListData.size() == 0) {
			return;
		}

		if (getPrintEntry().selectTemplate() < 0)
			return;
		ArrayList alBill = getSelectedBills();
		if (alBill == null)
			return;

		SpecialBillVO vo = null;
		nc.ui.scm.print.PrintLogClient plc = new nc.ui.scm.print.PrintLogClient();
		plc.setBatchPrint(true);//����������
		PrintDataInterface ds = null;
		//���ô�ӡ����
		getPrintEntry().setPrintListener(plc);
		plc.setPrintEntry(getPrintEntry());

		//����TSˢ�¼���.
		plc.addFreshTsListener(this);
		//��ӡ����
		try{
		    getPrintEntry().beginBatchPrint();
		    for (int i = 0; i < alBill.size(); i++) {
		        vo = (SpecialBillVO)alBill.get(i);

		        ScmPrintlogVO voSpl = new ScmPrintlogVO();
			    voSpl.setCbillid(vo.getPrimaryKey());
			    voSpl.setVbillcode(vo.getVBillCode());//���뵥�ݺţ�������ʾ��
			    voSpl.setCbilltypecode(vo.getBillTypeCode());//�������ͱ���
			    voSpl.setCoperatorid((String) (vo.getParentVO().getAttributeValue("coperatorid")) );//����ԱID
			    voSpl.setIoperatetype(new Integer(PrintConst.PAT_OK));//�̶�
			    voSpl.setPk_corp(m_sCorpID);//��˾
			    voSpl.setTs((String)(vo.getParentVO().getAttributeValue("ts")));//���������TS

			    nc.vo.scm.pub.SCMEnv.out("ts=========tata" + voSpl.getTs());
			    //���õ�����Ϣ
				plc.setPrintInfo(voSpl);

				if (plc.check()) {//���ͨ����ִ�д�ӡ���д���Ļ��Զ������ӡ��־�����ﲻ�ô���
				     ds = getNewDataSource();
				     ds.setVO(vo);
				     getPrintEntry().setDataSource(ds);

				     //����������Setup����С���У����ֳ������׶�������
//				     while (getPrintEntry().dsCountInPool() > PrintConst.PL_MAX_TAST_NUM) {
//				         Thread.currentThread().sleep(PrintConst.PL_SLEEP_TIME); //�����PL_MAX_TAST_NUM���������񣬾͵ȴ�PL_SLEEP_TIME�롣
//				     }
				 }
		    }
		    getPrintEntry().endBatchPrint();

		} catch (Exception e) {
			nc.vo.scm.pub.SCMEnv.error(e);
			showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill","UPP4008bill-000051")/*@res "��ӡ����\n"*/ + e.getMessage());
		}
	}
	showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON","UPPSCMCommon-000133")/*@res "����"*/);
}


protected QueryDlgHelpForSpec getQueryHelp() {
  if(this.m_queryHelp==null){
    this.m_queryHelp = new QueryDlgHelpForSpec(this);
  }
  return this.m_queryHelp;
}
/**
  * �����ߣ�������
  * ���ܣ���ѯ
  * ������
  * ���أ�
  * ���⣺
  * ���ڣ�(2001-5-10 ���� 2:57)
  * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
  */
public void onQuery() {

	//getConditionDlg().hideNormal();
  try{

  	//String sWhereClause = "";
    QryConditionVO qcvo = getQueryHelp().getQryConditionVOForQuery(false);
  	if (qcvo != null) {
  
  //		nc.vo.pub.query.ConditionVO[] voCons = getConditionDlg().getConditionVO();
  //		//������״̬������ת��ΪSQL������
  //		voCons = packConditionVO(voCons);
  //		//��������������
  //		resetConditionVO(voCons);
  //		//�õ���ѯ������
  //		sWhereClause = getExtenWhere(voCons);
  //
  //		QryConditionVO qcvo = new nc.vo.ic.pub.bill.QryConditionVO();
  //		if (sWhereClause.length() != 0) {
  //			qcvo.setQryCond("head.cbilltypecode='" + m_sBillTypeCode + "' " + sWhereClause);
  //		} else {
  //			qcvo.setQryCond("head.cbilltypecode='" + m_sBillTypeCode + "'");
  //		}
  //		qcvo.setParam(
  //			QryConditionVO.QRY_CONDITIONVO,
  //			getConditionDlg().getConditionVO());
  //		qcvo.setIntParam(0, SpecialBillVO.QRY_WHOLE_PURE);
  
  		loadBillListPanel(qcvo);
  
  
  		setButtonState();
  		setBillState();
  
  		if (m_iTotalListHeadNum > 0)
  			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill","UPP4008bill-000146")/*@res "���鵽"*/ + m_iTotalListHeadNum + nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill","UPP4008bill-000147")/*@res "�ŵ��ݣ�"*/);
  		else
  			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill","UPP4008bill-000013")/*@res "δ�鵽���������ĵ��ݡ�"*/);
  
  	}
  	showBtnSwitch();
  }catch(Exception e){
    GenMethod.handleException(this, null, e);
  }
}

/**
  * ����
  * �������ڣ�(2001-4-18 19:47:08)
  */
public  boolean onSave() {

	try {
		if (m_iLastSelListHeadRow < 0) { //δѡ���κα�ֱ������ʱ�����߲�ѯ���Ϊ��ʱ���ᷢ��
			m_iLastSelListHeadRow = 0; //���ѡ�е��б��ͷ��
			//m_iLastSelListbodyRow = -1; //���ѡ�е��б������
			m_iTotalListHeadNum = 0; //�б��ͷĿǰ���ڵ�����
			//m_iTotalListBodyNum = 0; //�б����Ŀǰ���ڵ�����
		}
//		getBillCardPanel().getBillData().execBodyValidateFormulas();
		   if(!getBillCardPanel().getBillData().execValidateFormulas())
	           return false;


		//�������������
		getBillCardPanel().tableStopCellEditing();
		getBillCardPanel().stopEditing();

		//�˵����еĿ���
		filterNullLine();
		if (getBillCardPanel().getRowCount() == 0) {
			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill","UPP4008bill-000148")/*@res "�����ݣ�����������..."*/);
			return false;
		}
		//added by zhx 030626 ����кŵĺϷ���; �÷���Ӧ���ڹ��˿��еĺ��档
		if (!nc.ui.scm.pub.report.BillRowNo.verifyRowNosCorrect(getBillCardPanel(), m_sBillRowNo)) {
			return false;
		}

		//���뵥�ݺ�
		//String sBillCode=null;
		//if (BillMode.New == m_iMode) {
			//m_voBill.setVBillCode(m_sBillTypeCode);
			////if (!GeneralMethod
				////.setBillCode(m_voBill, m_sBillTypeCode, getBillCardPanel())) {
			//m_voBill.setVBillCode(null);
			//sBillCode=GeneralMethod.setBillCode((nc.vo.scm.pub.IBillCode)m_voBill);
			//if(sBillCode==null){
				//nc.ui.pub.beans.MessageDialog.showErrorDlg(this, "����", "��õ��ݺ�ʧ�ܣ�");
				//return;
			//}
			//if(getBillCardPanel().getHeadItem("vbillcode")!=null)
				//getBillCardPanel().getHeadItem("vbillcode").setValue(sBillCode);
		//}

		SpecialBillVO voNowBill = null;
		voNowBill = getBillVO();
		voNowBill.getParentVO().setAttributeValue("cbilltypecode", m_sBillTypeCode);
		//ͬ�����VO
		//voNowBill--->>>m_voBill
		m_voBill.setIDItems(voNowBill);
		voNowBill.setHeaderValue("fbillflag", m_voBill.getHeaderValue("fbillflag"));
		voNowBill.setHeaderValue("icheckmode", m_voBill.getHeaderValue("icheckmode"));
		voNowBill.setHeaderValue("pk_calbody_in",m_voBill.getHeaderValue("pk_calbody_in"));
		voNowBill.setHeaderValue("pk_calbody_out",m_voBill.getHeaderValue("pk_calbody_out"));
	
		m_voBill.setHeaderValue(
			"fassistantflag",
			voNowBill.getHeaderValue("fassistantflag"));

		//VOУ��
		if (!checkVO()) {
			return false;
		}
		String sHPK = null; //bill pk
		int iRowCount = getBillCardPanel().getRowCount();
		SpecialBillItemVO[] voaItem = null;	
		
		// �޸Ļ��߱���󷵻ص�СVO
		SMSpecialBillVO voSM = null;
		
		if (BillMode.New == m_iMode) {
			//����
			//��m_shvoBillSpecialHVO��д��FreeVO�����ӵ�ֵ
			voNowBill.setChildrenVO((SpecialBillItemVO[]) getChangedItemVOs());

			//�õ���ǰ��ItemVO
			voaItem = voNowBill.getItemVOs();
			//��ʱHVO[]
			SpecialBillVO[] voTempBill = new SpecialBillVO[1];
			voTempBill[0] = voNowBill;
			//д��,������PKs
			//���õ����к�zhx 0630:
			if (iRowCount > 0 && voNowBill.getChildrenVO() != null) {
				if (getBillCardPanel().getBodyItem(m_sBillRowNo) != null)
					for (int i = 0; i < iRowCount; i++) {

						voNowBill.setItemValue(
							i,
							m_sBillRowNo,
							getBillCardPanel().getBodyValueAt(i, m_sBillRowNo));

					}
			}
			ArrayList alsPrimaryKey =null;
//			try{
			if(m_sCorpID.equals(voNowBill.getVBillCode()))
				voNowBill.setVBillCode(null);
			voNowBill.getHeaderVO().setCoperatoridnow(m_sUserID);//��ǰ����Ա
			
      //add by liuzy 2007-11-02 10:16 ѹ������
      ObjectUtils.objectReference(voNowBill);
      
			alsPrimaryKey =
				(ArrayList) nc.ui.pub.pf.PfUtilClient.processAction(
					"WRITE",
					m_sBillTypeCode,
					m_sLogDate,
					voNowBill);
			voNowBill.setVBillCode(m_sCorpID);
			//}catch(Exception e){
				//if(sBillCode!=null){
					//GeneralMethod.returnBillCode((nc.vo.scm.pub.IBillCode)m_voBill);
					//if(getBillCardPanel().getHeadItem("vbillcode")!=null)
						//getBillCardPanel().getHeadItem("vbillcode").setValue(null);
				//}
				//throw e;
				//}
			if (alsPrimaryKey == null || alsPrimaryKey.size() < 2) {
				nc.vo.scm.pub.SCMEnv.out("return data error.");
				return true;
			} //��ʾ��ʾ��Ϣ
			if (alsPrimaryKey.get(0) != null)
				showErrorMessage((String) alsPrimaryKey.get(0));
			ArrayList alMyPK = (ArrayList) alsPrimaryKey.get(1);
			sHPK = alMyPK.get(0).toString();
			
			voSM = (SMSpecialBillVO) alsPrimaryKey
			.get(alsPrimaryKey.size() - 1);
			
			//д��HHeaderVO��PK
			//voNowBill.getParentVO().setPrimaryKey(sHPK);
			//m_voBill.getParentVO().setPrimaryKey(sHPK);
			//д��HItemVO��PK���Ӧ��ͷPK
/*			for (int i = 0; i < voaItem.length; i++) {
				voaItem[i].setPrimaryKey(alMyPK.get(i + 1).toString());
				voaItem[i].setAttributeValue("cspecialhid", sHPK);
				//m_voBill.getChildrenVO()[i].setAttributeValue("cspecialhid", sHPK);
			}
			
			
			voNowBill.setChildrenVO(voaItem);*/
			
/*			nc.ui.ic.pub.tools.GenMethod.fillVOValuesBy(new SpecialBillHeaderVO[]{voNowBill.getHeaderVO()},
	        		new String[]{"tmaketime","tlastmoditime"}, new int[]{SmartFieldMeta.JAVATYPE_STRING,SmartFieldMeta.JAVATYPE_STRING},
	        		IItemKey.cspecialhid, IItemKey.ic_special_h,
	        		new String[]{IItemKey.tmaketime, IItemKey.tlastmoditime}, IItemKey.cspecialhid, null);*/
			
			//ͬ�����VO
			//m_voBill.setIDItems(voNowBill);
      
      m_voBill.setIsYetExecBatchFormulas(false);
			
			BatchCodeDefSetTool.execFormulaForBatchCode(m_voBill.getChildrenVO());
			
			//����HVO
			m_iLastSelListHeadRow = m_iTotalListHeadNum;
			addBillVO();
		} else if (BillMode.Update == m_iMode) { //�޸�
			//�ӽ����л����Ҫ������
			voNowBill = null;
			voNowBill = getUpdatedBillVO();
			voNowBill.getParentVO().setAttributeValue("cbilltypecode", m_sBillTypeCode);
			voNowBill.setHeaderValue("fbillflag", m_voBill.getHeaderValue("fbillflag"));
			voNowBill.setHeaderValue("icheckmode", m_voBill.getHeaderValue("icheckmode"));
			m_voBill.setHeaderValue(
				"fassistantflag",
				voNowBill.getHeaderValue("fassistantflag"));

			if (null == voNowBill) {
				return false;
			} //��m_shvoBillSpecialHVO��д��FreeVO�����ӵ�ֵ
			voNowBill.setChildrenVO((SpecialBillItemVO[]) getChangedItemVOs());
			//��ʱ����VO��ȫ��HIVO
			SpecialBillItemVO[] voaTempItem =
				(SpecialBillItemVO[]) getBillVO().getChildrenVO();
			//�õ���ǰ��ItemVO
			voaItem = (SpecialBillItemVO[]) voNowBill.getChildrenVO();

			//��ʱHVO[]
			SpecialBillVO[] m_voTempBill = new SpecialBillVO[1];
			m_voTempBill[0] = voNowBill;
			//д��,������PKs
			//���õ����к�zhx 0630:
			if (iRowCount > 0 && voNowBill.getChildrenVO() != null) {
				if (getBillCardPanel().getBodyItem(m_sBillRowNo) != null)
					for (int i = 0; i < iRowCount; i++) {

						voNowBill.setItemValue(
							i,
							m_sBillRowNo,
							getBillCardPanel().getBodyValueAt(i, m_sBillRowNo));

					}
			}
			
			voNowBill.getHeaderVO().setCoperatoridnow(m_sUserID);//��ǰ����Ա

      
      //add by liuzy 2007-11-02 10:16 ѹ������
      ObjectUtils.objectReference(voNowBill);
			
//    �޸��ˣ������� �޸�ʱ�䣺2008-8-15 ����02:27:03 �޸�ԭ�򣺺�̨�жϵ��ݺ���û���޸��á�
      voNowBill.getHeaderVO().setAttributeValue("oldVBillCode", ((SpecialBillVO) m_alListData.get(m_iLastSelListHeadRow)).getVBillCode());
			ArrayList alsPrimaryKey =
				(ArrayList) nc.ui.pub.pf.PfUtilClient.processAction(
					"WRITE",
					m_sBillTypeCode,
					m_sLogDate,
					voNowBill,
					((SpecialBillVO) m_alListData.get(m_iLastSelListHeadRow)).clone());
			if (alsPrimaryKey == null || alsPrimaryKey.size() < 2) {
				nc.vo.scm.pub.SCMEnv.out("return data error.");
				return true;
			} //��ʾ��ʾ��Ϣ
			if (alsPrimaryKey.get(0) != null)
				showErrorMessage((String) alsPrimaryKey.get(0));
			ArrayList alMyPK = (ArrayList) alsPrimaryKey.get(1);
			sHPK = voNowBill.getParentVO().getPrimaryKey();

			voSM = (SMSpecialBillVO) alsPrimaryKey
					.get(alsPrimaryKey.size() - 1);
			filterDeletedItems(voSM);
			
			//д��HItemVO��PK���Ӧ��ͷPK,ɾȥ�����ItemVO
			ArrayList alItemVO = new ArrayList();
			//����ɱ�����û�иı��ItemVO
			//��Ӧƽ̨���޸�,���Ƿ��ص�һ���Ǳ�ͷ��PK		2001/09/26
			//changed to start from 1
			int iItemNumb = 1;
			for (int i = 0; i < voaTempItem.length; i++) {
				switch (voaTempItem[i].getStatus()) {
					case VOStatus.UNCHANGED :
						alItemVO.add(voaTempItem[i]);
						break;
					case VOStatus.NEW :
						if (iItemNumb < alMyPK.size()) {
							voaItem[i].setPrimaryKey(alMyPK.get(iItemNumb).toString());
							iItemNumb++;
							voaItem[i].setAttributeValue("cspecialhid", sHPK);
							alItemVO.add(voaItem[i]);
						} else {
							SCMEnv.out("����ʱ�����ж�Ӧ�������������Ա���...");
						}
						break;
					case VOStatus.UPDATED :
						alItemVO.add(voaItem[i]);
				}
			}

/*			voaItem = new SpecialBillItemVO[alItemVO.size()];
			for (int i = 0; i < alItemVO.size(); i++) {
				voaItem[i] = (SpecialBillItemVO) alItemVO.get(i);
			}
			voNowBill.setChildrenVO(voaItem);*/
			
/*			nc.ui.ic.pub.tools.GenMethod.fillVOValuesBy(new SpecialBillHeaderVO[]{voNowBill.getHeaderVO()},
	        		new String[]{"tlastmoditime","vbillcode"}, new int[]{SmartFieldMeta.JAVATYPE_STRING,SmartFieldMeta.JAVATYPE_STRING},
	        		IItemKey.cspecialhid, IItemKey.ic_special_h,
	        		new String[]{IItemKey.tlastmoditime,IItemKey.VBILLCODE}, IItemKey.cspecialhid, null);*/
			
			//ͬ�����VO
			//m_voBill.setIDItems(voNowBill);
      
      m_voBill.setIsYetExecBatchFormulas(false);
			
			BatchCodeDefSetTool.execFormulaForBatchCode(m_voBill.getChildrenVO());
			
			//�޸�HVO
			//m_alListData.set(m_iLastSelListHeadRow, m_voBill.clone());
		}
		// ����̨��Ϣ���µ�����
		freshVOBySmallVO(voSM);
		
		//2005-04-30 �������ɫ����
		InvAttrCellRenderer ficr = new InvAttrCellRenderer();
		ficr.setFreeItemRenderer(getBillCardPanel(), null);
		//addied by liuzy 2008-07-03 ��Ӧ�̱�ɫ��
		SupplierItemCellRender supplier = new SupplierItemCellRender(getBillCardPanel(),m_voBill);
		supplier.setRenderer("cvendorname");
    m_iMode = BillMode.Browse;
		//set ui pk below,so put it before freshts.
    // �޸��ˣ������� �޸�ʱ�䣺2009-9-1 ����07:34:44 �޸�ԭ�򣺻���Ч�ʿ��ǣ����ܵ��ô˷�������ʲôҪ���µ�ֱ��ȥ���ý��档
		//switchListToBill();
		//fresh timestamp
/*		if (sHPK != null) {
			ArrayList alLastTs = qryLastTs(sHPK);
			freshTs(alLastTs);
		}	*/	
		
		//����HVO
		m_iFirstSelListHeadRow = -1;
		setButtonState();
		setBillState();
		showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill","UPP4008bill-000103")/*@res "����ɹ���"*/);
		nc.ui.ic.pub.tools.GenMethod.reSetRowColorWhenNOException(getBillCardPanel());
		// added by lirr 2009-11-30����06:57:59 ���ģ�建���е�ɾ���� NCdp201093640 ��������
		filterDeletedItems(getBillCardPanel().getBillModel());
	} catch (Exception e) {
		showErrorMessage(e.getMessage());
		showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill","UPP4008bill-000149")/*@res "δ����ɣ����������ԣ�"*/);
		handleException(e);
	}
	
	return true;
}

/*public void reSetRowColorWhenNOException(){
	// ������ɫ
  ArrayList alRowNum = new ArrayList();
	SetColor.SetTableColor(
			getBillCardPanel().getBillModel(),
			getBillCardPanel().getBillTable(),
			getBillCardPanel(),
			alRowNum,
			m_cNormalColor,
			m_cNormalColor,
			m_bExchangeColor,
			m_bLocateErrorColor,
			"",m_bRowLocateErrorColor);
	SetColor.resetColor(getBillCardPanel().getBillTable());
	
}*/

/**
   * �����ߣ�������
   * ���ܣ���ʼ����ť
   * ������
   * ���أ�
   * ���⣺
   * ���ڣ�(2001-5-15 ���� 3:12)
   * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
   */
protected void initButtons() {

}

/**
   * �����ߣ�������
   * ���ܣ��б���ʽ�±�ͷѡ��ı�ʱ�����б���±�����ʾ��Ӧ�ı��塣
   * ������
   * ���أ�
   * ���⣺
   * ���ڣ�(2001-5-15 ���� 4:27)
   * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
   * @param e nc.ui.pub.bill.BillEditEvent
   */
protected void listSelectionChanged(nc.ui.pub.bill.BillEditEvent e) {
	int rownow;
	try {
		rownow = e.getRow();
		selectListBill(rownow);

	} catch (Exception e1) {
		nc.vo.scm.pub.SCMEnv.out("����ͨѶʧ�ܣ�");
		handleException(e1);
	}
}

/**
 * �����ˣ�������
�������ڣ�2008-4-3����11:13:29
����ԭ��ѡ���б���ʽ�µĵ�sn�ŵ��� ������sn �������
 * 
 * 
 * 
 */
public void selectListBill(int rownow) {
	if (m_alListData == null || rownow < 0 || m_iLastSelListHeadRow == rownow)
		return;

	m_iLastSelListHeadRow = rownow; //���ѡ�е��б��ͷ��
	getBillListPanel().getHeadTable().setRowSelectionInterval(rownow, rownow);
	getBillListPanel().getBodyBillModel().clearBodyData();

	SpecialBillVO sbvotemp =
		(SpecialBillVO) m_alListData.get(m_iLastSelListHeadRow);

	if (sbvotemp == null)
		return;

	//�õ���ǰ��body
	SpecialBillItemVO[] voItems = (SpecialBillItemVO[]) sbvotemp.getChildrenVO();
	if (voItems == null || voItems.length < 1) {
		qryItems(new int[] { rownow }, new String[] { sbvotemp.getPrimaryKey()});
	}
	//re-get
	sbvotemp = (SpecialBillVO) m_alListData.get(rownow);
	voItems = (SpecialBillItemVO[]) sbvotemp.getChildrenVO();
	//����Ƿ��б��壬���û����ʾ���ݿ��ܱ�ɾ����,���������ء�
	if (voItems == null||voItems.length<=0){
	    if (m_sBillTypeCode.equals("4R"))
		    showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill","UPP4008bill-000150")/*@res "���̵㵥û����ʵ�����ϵļ�¼,���ߴ˵����ѱ�ɾ����"*/);
	    else
	    	showWarningMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill","UPP4008bill-000151")/*@res "δ�ҵ�������Ϣ�����ܱ�ɾ����"*/);
	}

	//���ⵥû�б��滻���ʣ��䶯hsl���������������
	//GenMethod mthod = new GenMethod();
	//mthod.calAllConvRate(voItems,"fixedflag","hsl","dshldtransnum","nshldtransastnum","nchecknum","ncheckastnum");

	getBillListPanel().getBodyBillModel().setBodyDataVO(voItems);
	dispBodyRow(getBillListPanel().getBodyTable());
	setButtonState();
	setBillState();
	/**
	 * 2010-11-08 MeiChao 
	 * ��ǰ����Ϊ��̬ת����ʱ(����������ʱ�޷�ʵ��),ѡ���б���1�м�¼���ѯ����Ӧ�ķ�����Ϣ,
	 * ��������Ϣͬʱ�������б���Ƭҳ����,
	 */
	//2010-11-08 MeiChao begin
	//if("4N".equals(this.getBillCardPanel().getBillType())){
		//������ڷ�����Ϣҳǩ...
		if(this.getBillListPanel().getBodyBillModel("jj_scm_informationcost")!=null){
			// ��ȡPK
			String specialVOPk = sbvotemp.getHeaderVO().getPrimaryKey();
			// ��ȡЭ����
			JJIcScmPubHelper expenseManager = new JJIcScmPubHelper();
			
			try {
				// ����PK��ѯ��Ӧ������Ϣ
				expenseVOs = (InformationCostVO[]) expenseManager
						.querySmartVOs(InformationCostVO.class, null,
								" dr=0 and cbillid='" + specialVOPk + "'");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// ��������Ϣд�������Ϣҳǩ
			if (expenseVOs != null && expenseVOs.length > 0) {
				this.getBillListPanel().getBodyBillModel(
						"jj_scm_informationcost").setBodyDataVO(expenseVOs);
				this.getBillCardPanel().getBillModel("jj_scm_informationcost")
						.setBodyDataVO(expenseVOs);
			} else {
				// ��������ֵ,��ô��������Ϣҳǩ�ÿ�
				this.getBillListPanel().getBodyBillModel(
						"jj_scm_informationcost").setBodyDataVO(null);
				this.getBillCardPanel().getBillModel("jj_scm_informationcost")
						.setBodyDataVO(null);

			}
		}
	
	
}



/**
 * �����ߣ�������
 * ���ܣ�������
 * ������
 * ���أ�
 * ���⣺
 * ���ڣ�(2001-5-16 ���� 5:58)
 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
 */
public void onInsertRow() { //finished
	getBillCardPanel().insertLine();

	dispBodyRow(getBillCardPanel().getBillTable());
	//zhx added rowno 030626
	nc.ui.scm.pub.report.BillRowNo.insertLineRowNo(
		getBillCardPanel(),
		m_sBillTypeCode,
		m_sBillRowNo);
	nc.ui.ic.pub.tools.GenMethod.reSetRowColorWhenNOException(getBillCardPanel(),getBillCardPanel().getBillTable().getSelectedRow());
}

/**
   * �����ߣ�������
   * ���ܣ���ʼ������
   * ������
   * ���أ�
   * ���⣺
   * ���ڣ�(2001-5-24 ���� 6:27)
   * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
   */
protected void initVariable() {

}

/**
  * �����ߣ�������
  * ���ܣ�ִ������
  * ������
  * ���أ�
  * ���⣺
  * ���ڣ�(2001-5-10 ���� 2:54)
  * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
  */
public void onAuditBill() {

}

/**
* �����ߣ�������
* ���ܣ�ִ������(Ӧ���÷����ڵ����ε���BO�ϳ�һ��ҵ��)
* ������
* ���أ�
* ���⣺
* ���ڣ�(2001-5-10 ���� 2:50)
* �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
*/
public void onCancelAudit() {
	if (m_alListData == null
		|| m_alListData.size() < m_iLastSelListHeadRow
		|| m_iLastSelListHeadRow < 0) {

		return;
	}
	switch (nc.ui.pub.beans.MessageDialog.showYesNoDlg(this,
			null,
			nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
			"UCH068")/* @res "�Ƿ�ȷ��Ҫ����" */
			,MessageDialog.ID_NO)) {

	case nc.ui.pub.beans.MessageDialog.ID_YES:
		break;
	default:return;	
	}
	
	
	try {
		//��ѯԭ�����ɵ���������ⵥ
		//���ɲ�ѯ�Ӿ�

		//����
		SpecialBillVO vo = new SpecialBillVO();
		SpecialBillHeaderVO voHead = new SpecialBillHeaderVO();
		voHead.setAttributeValue("cspecialhid", m_voBill.getHeaderValue("cspecialhid"));
		voHead.setAttributeValue("coperatorid", m_sUserID);
		voHead.setAttributeValue("pk_corp", m_sCorpID);
		voHead.setAttributeValue("coperatoridnow", m_sUserID);
		voHead.setAttributeValue("cbilltypecode", m_sBillTypeCode);
		vo.setParentVO(voHead);

		//ArrayList alsPrimaryKey=
		//(ArrayList)
    
    //add by liuzy 2007-11-02 10:16 ѹ������
    ObjectUtils.objectReference(vo);

		nc.ui.pub.pf.PfUtilClient.processAction(
			"DELETEOTHER",
			m_sBillTypeCode,
			m_sLogDate,
			vo);

		//���±�β
		clearAuditBillFlag();
		filterNullLine();

		//����ʱ���־ by hanwei 2003-11-01
		String sHPK=vo.getPrimaryKey().trim();
		if (sHPK != null) {
			ArrayList alLastTs = qryLastTs(sHPK);
			freshTs(alLastTs);
		  }
		m_alListData.set(m_iLastSelListHeadRow, m_voBill.clone());

		m_iFirstSelListHeadRow = -1;
		// �޸��ˣ������� �޸�ʱ�䣺2009-9-2 ����09:37:23 �޸�ԭ�򣺲���Ҫ�˲����������ܼ���������
		//switchListToBill();

		setButtonState();
		setBillState();

	} catch (Exception e) {
		nc.vo.scm.pub.SCMEnv.out("����ͨѶʧ�ܣ�");
		handleException(e);
		nc.ui.pub.beans.MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill","UPPSCMCommon-000059")/*@res "����"*/, e.getMessage());
	}
}

/**
* �����ߣ�������
* ���ܣ����˵��޴����
* ������
* ���أ�
* ���⣺
* ���ڣ�(2001-5-10 ���� 2:57)
* �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
*/
protected void filterNullLine() {
	//��ʱ����VO��ȫ��HIVO
	SpecialBillItemVO[] voaItem = getBillVO().getItemVOs();

	if (null == voaItem) {
		return;
	}
	if (voaItem.length < 1) {
		return;
	}

	Vector vTemp = new Vector();

	for (int i = 0; i < voaItem.length; i++) {
		//����Ϊ�ջ��ߴ��δ��
		if (voaItem[i] == null
			|| voaItem[i].getCinventoryid() == null
			|| voaItem[i].getCinventoryid().trim().length() == 0) //��¼���к�
			vTemp.addElement(new Integer(i));
	}
	int size = vTemp.size();
	if (size > 0) {
		int iaRows[] = new int[size];
		for (int i = 0; i < size; i++)
			iaRows[i] = (new Integer(vTemp.elementAt(i).toString())).intValue();
		getBillCardPanel().getBillModel().delLine(iaRows);
	}

}

/**
  * �����ߣ�������
  * ���ܣ�VOУ��
  * ������
  * ���أ�
  * ���⣺
  * ���ڣ�(2001-5-24 ���� 5:17)
  * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
  */
protected boolean checkVO() {
	try {
		String sAllErrorMessage = "";

		//ִ�����¼�飬�������еļ��ע��------------------------------------------------
		//------------------------------------------------------------------------------
		//VO���ڼ��
		VOCheck.checkNullVO(m_voBill);
		//------------------------------------------------------------------------------
		//Ӧ���������,Ҫ����ǰ��
		//���ڵ�ʹ��=====================
		//if (packageID.equals("221")) {
		//��ֵ����ȫ���Լ��
		//try {
		VOCheck.checkNumInput(m_voBill.getChildrenVO(), m_sNumItemKey);
		//} catch (ICNumException e) {
		////��ʾ��ʾ
		//String sErrorMessage=
		//GeneralMethod.getBodyErrorMessage(
		//getBillCardPanel(),
		//e.getErrorRowNums(),
		//e.getHint());
		//sAllErrorMessage= sAllErrorMessage + sErrorMessage + "\n";
		//}
		//}
		//���ڵ�ʹ��=====================
		//------------------------------------------------------------------------------
		//��ͷ����ǿռ��
		try {
			VOCheck.validate(
				m_voBill,
				GeneralMethod.getHeaderCanotNullString(getBillCardPanel()),
				GeneralMethod.getBodyCanotNullString(getBillCardPanel()));
		} catch (ICNullFieldException e) {
			//��ʾ��ʾ
			String sErrorMessage =
				GeneralMethod.getBodyErrorMessage(
					getBillCardPanel(),
					e.getErrorRowNums(),
					e.getHint());
			sAllErrorMessage = sAllErrorMessage + sErrorMessage + "\n";
		} catch (ICHeaderNullFieldException e) {
			//��ʾ��ʾ
			String sErrorMessage =
				GeneralMethod.getHeaderErrorMessage(
					getBillCardPanel(),
					e.getErrorRowNums(),
					e.getHint());
			sAllErrorMessage = sAllErrorMessage + sErrorMessage + "\n";
		}
		//------------------------------------------------------------------------------
		//ҵ������
		/**V31������������ε�У�������ⵥ��ȡ��*/
		    /*
		//������
		try {
			VOCheck.checkFreeItemInput(m_voBill, m_sNumItemKey);
		} catch (ICNullFieldException e) {
			//��ʾ��ʾ
			String sErrorMessage =
				GeneralMethod.getBodyErrorMessage(
					getBillCardPanel(),
					e.getErrorRowNums(),
					e.getHint());
			sAllErrorMessage = sAllErrorMessage + sErrorMessage + "\n";
		}
		//���κ�
		try {
			VOCheck.checkLotInput(m_voBill, m_sNumItemKey);
		} catch (ICNullFieldException e) {
			//��ʾ��ʾ
			String sErrorMessage =
				GeneralMethod.getBodyErrorMessage(
					getBillCardPanel(),
					e.getErrorRowNums(),
					e.getHint());
			sAllErrorMessage = sAllErrorMessage + sErrorMessage + "\n";
		}
		//������
		try {
			VOCheck.checkAssistUnitInput(m_voBill, m_sNumItemKey, m_sAstItemKey);
		} catch (ICNullFieldException e) {
			//��ʾ��ʾ
			String sErrorMessage =
				GeneralMethod.getBodyErrorMessage(
					getBillCardPanel(),
					e.getErrorRowNums(),
					e.getHint());
			sAllErrorMessage = sAllErrorMessage + sErrorMessage + "\n";
		}
		//ʧЧ����
		try {
			VOCheck.checkInvalidateDateInput(m_voBill, m_sNumItemKey);
		} catch (ICNullFieldException e) {
			//��ʾ��ʾ
			String sErrorMessage =
				GeneralMethod.getBodyErrorMessage(
					getBillCardPanel(),
					e.getErrorRowNums(),
					e.getHint());
			sAllErrorMessage = sAllErrorMessage + sErrorMessage + "\n";
		} catch (NullFieldException e) {
			String sErrorMessage = e.getHint();
			sAllErrorMessage = sAllErrorMessage + sErrorMessage + "\n";
		}
		*/
		//������� ���������飬�ѷ���ǿ������У�Ϊϵͳ������
		//VOCheck.checkdbizdate(m_voBill, m_sNumItemKey);
		//����>=0���
		try {
			VOCheck.checkGreaterThanZeroInput(m_voBill.getChildrenVO(), "nprice",nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UC000-0000741")/*@res "����"*/);
		} catch (ICPriceException e) {
			//��ʾ��ʾ
			String sErrorMessage =
				GeneralMethod.getBodyErrorMessage(
					getBillCardPanel(),
					e.getErrorRowNums(),
					e.getHint());
			sAllErrorMessage = sAllErrorMessage + sErrorMessage + "\n";
		}
		//------------------------------------------------------------------------------

		//�Զ�У��ǰ���б����˳�
		if (sAllErrorMessage.trim().length() != 0) {
			showErrorMessage(sAllErrorMessage);
			return false;
		}

		//�Զ�У��
		//------------------------------------------------------------------------------

		//���κ��쳣
		//������ɫΪ������ɫ
		nc.ui.ic.pub.tools.GenMethod.reSetRowColorWhenNOException(getBillCardPanel());

		return true;

	} catch (ICDateException e) {
		//��ʾ��ʾ
		String sErrorMessage =
			GeneralMethod.getBodyErrorMessage(
				getBillCardPanel(),
				e.getErrorRowNums(),
				e.getHint());
		showErrorMessage(sErrorMessage);
		//������ɫ
		nc.ui.ic.pub.tools.GenMethod.setRowColorWhenException(getBillCardPanel(), e);
		return false;
	} catch (ICNullFieldException e) {
		//��ʾ��ʾ
		String sErrorMessage =
			GeneralMethod.getBodyErrorMessage(
				getBillCardPanel(),
				e.getErrorRowNums(),
				e.getHint());
		showErrorMessage(sErrorMessage);
		//������ɫ
		nc.ui.ic.pub.tools.GenMethod.setRowColorWhenException(getBillCardPanel(), e);
		return false;
	} catch (ICNumException e) {
		//��ʾ��ʾ
		String sErrorMessage =
			GeneralMethod.getBodyErrorMessage(
				getBillCardPanel(),
				e.getErrorRowNums(),
				e.getHint());
		showErrorMessage(sErrorMessage);
		nc.ui.ic.pub.tools.GenMethod.setRowColorWhenException(getBillCardPanel(), e);
		return false;
	} catch (ICPriceException e) {
		//��ʾ��ʾ
		String sErrorMessage =
			GeneralMethod.getBodyErrorMessage(
				getBillCardPanel(),
				e.getErrorRowNums(),
				e.getHint());
		showErrorMessage(sErrorMessage);
		nc.ui.ic.pub.tools.GenMethod.setRowColorWhenException(getBillCardPanel(), e);
		return false;
	} catch (ICSNException e) {
		//��ʾ��ʾ
		String sErrorMessage =
			GeneralMethod.getBodyErrorMessage(
				getBillCardPanel(),
				e.getErrorRowNums(),
				e.getHint());
		showErrorMessage(sErrorMessage);
		nc.ui.ic.pub.tools.GenMethod.setRowColorWhenException(getBillCardPanel(), e);
		return false;
	} catch (ICLocatorException e) {
		//��ʾ��ʾ
		String sErrorMessage =
			GeneralMethod.getBodyErrorMessage(
				getBillCardPanel(),
				e.getErrorRowNums(),
				e.getHint());
		showErrorMessage(sErrorMessage);
		nc.ui.ic.pub.tools.GenMethod.setRowColorWhenException(getBillCardPanel(), e);
		return false;
	} catch (ICRepeatException e) {
		//��ʾ��ʾ
		String sErrorMessage =
			GeneralMethod.getBodyErrorMessage(
				getBillCardPanel(),
				e.getErrorRowNums(),
				e.getHint());
		showErrorMessage(sErrorMessage);
		nc.ui.ic.pub.tools.GenMethod.setRowColorWhenException(getBillCardPanel(), e);
		return false;
	} catch (ICHeaderNullFieldException e) {
		//��ʾ��ʾ
		String sErrorMessage =
			GeneralMethod.getHeaderErrorMessage(
				getBillCardPanel(),
				e.getErrorRowNums(),
				e.getHint());
		showErrorMessage(sErrorMessage);
		return false;
	} catch (NullFieldException e) {
		showErrorMessage(e.getHint());
		//fullScreen(getBillCardPanel().getBillModel(), m_iFirstAddRows);
		return false;
	} catch (ValidationException e) {
		nc.vo.scm.pub.SCMEnv.out("У���쳣������δ֪����...");
		showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill","UPP4008bill-000152")/*@res "У���쳣������δ֪����..."*/);
		handleException(e);
		return false;
	}
	

}

	/**
	 * getTitle ����ע�⡣
	 */
	protected FreeItemRefPane getFreeItemRefPane() {
		return m_firpFreeItemRefPane;
	}

/**
   * ���β���
*/
protected LotNumbRefPane getLotNumbRefPane() {
  if(m_lnrpLotNumbRefPane!=null){
    m_lnrpLotNumbRefPane.setIsMutiSel(true);
    m_lnrpLotNumbRefPane.setIsBodyMutiSel(false);
  }
	return m_lnrpLotNumbRefPane;
}



	/**˫���ı��б���
	   * Invoked when the mouse has been clicked on a component.
	   */
	public void mouseClicked(java.awt.event.MouseEvent e) {
		if (e.getSource() == getBillListPanel().getHeadTable()) {
			if (e.getClickCount() == 2
				&& getBillListPanel().getHeadTable().getSelectedRow() != -1) {
				onList();
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
		Object edd= e.getSource();
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
	 * �����ߣ�������
	 * ���ܣ�������ı��¼�����
	 * ������e���ݱ༭�¼�
	 * ���أ�
	 * ���⣺
	 * ���ڣ�(2001-5-8 19:08:05)
	 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 *
	 *
	 *
	 *
	 */
	public void afterFreeItemEdit(nc.ui.pub.bill.BillEditEvent e) {
		try {
			FreeVO fvoFreeVO= getFreeItemRefPane().getFreeVO();
			m_voBill.setItemFreeVO(e.getRow(), fvoFreeVO);
/*			for (int i = 1; i <= FreeVO.FREE_ITEM_NUM; i++) {
				if (getBillCardPanel().getBodyItem("vfree" + i) != null)
					if (fvoFreeVO != null){
						getBillCardPanel().setBodyValueAt(
								fvoFreeVO.getAttributeValue("vfree" + i),
								e.getRow(), "vfree" + i);
						m_voBill.setItemValue(e.getRow(), "vfree" + i, fvoFreeVO.getAttributeValue("vfree" + i));
          }else{
						getBillCardPanel().setBodyValueAt(null, e.getRow(),
								"vfree" + i);
						m_voBill.setItemValue(e.getRow(), "vfree" + i, null);
          }
			}
			InvVO voInv = (InvVO) getBillCardPanel().getBodyValueAt(e.getRow(),
			"invvo");
	if (voInv != null) {
		voInv.setFreeItemVO(fvoFreeVO);
		getBillCardPanel().setBodyValueAt(voInv, e.getRow(), "invvo");
	}*/
			int row= e.getRow();
			String[] sIKs= getClearIDs(1, "vfree0");
			clearRowData(row, sIKs);
			//�޸��ˣ������� �޸����ڣ�2007-9-26����03:55:38 �޸�ԭ����������0�в���¼����������������������1..10�ϡ�
			for (int i = 1; i <= FreeVO.FREE_ITEM_NUM; i++) {
	             if (getBillCardPanel().getBodyItem("vfree" + i) != null)
	                 if (fvoFreeVO != null)
	                     getBillCardPanel().setBodyValueAt(
	                     fvoFreeVO.getAttributeValue("vfree" + i),
	                         e.getRow(),
	                         "vfree" + i);
	                 else
	                     getBillCardPanel().setBodyValueAt(null, e.getRow(), "vfree" + i);
	         }
			//���µ���Ϣ��Ҫ�Ż���������κ�δ��ʾ����������ʾ��
			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill","UPP4008bill-000153")/*@res "�������޸ģ�������ȷ�����Ρ�������"*/);
		} catch (Exception e2) {
			nc.vo.scm.pub.SCMEnv.error(e2);
		}

	}

/**
 * �˴����뷽��˵����
 * �����ߣ�������
 * ���ܣ����κŸ����¼�
 * ������
 * ���أ�
 * ���⣺
 * ���ڣ�(2001-6-20 21:43:07)
 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
 *
 * @param param nc.ui.pub.bill.BillEditEvent
 */
public void afterLotEdit(nc.ui.pub.bill.BillEditEvent e) {
	String s = e.getKey();
	WhVO whvo = m_voBill.getWhOut();
  String vbatchcode = (String)getBillCardPanel().getBodyValueAt(e.getRow(), "vbatchcode");
  if(nc.vo.ic.pub.GenMethod.isSEmptyOrNull(vbatchcode))
    getBillCardPanel().setBodyValueAt(null, e.getRow(), "vbatchcode");
	//getLotNumbRefPane().setParameter(whvo, m_voBill.getItemInv(e.getRow()));
	//getLotNumbRefPane().setParameter(m_whvo,m_invvo);
	getLotRefbyHand(s);
	pickupLotRef(s);
	//ͬ���ı�m_voBill����pickupValuefromLotNumbRef() �����С�
}

/**
 * ?user> ���ܣ� ������ ���أ� ���⣺ ���ڣ�(2005-1-11 18:53:03) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
 * 
 * @param voLot
 *            nc.vo.ic.pub.lot.LotNumbRefVO
 * @param irow
 *            int
 */
private void synlot(nc.vo.ic.pub.lot.LotNumbRefVO voLot, int irow,String key) {
  InvVO voInv = m_voBill.getItemInv(irow);
  
//������
  if (IItemKey.FREE0.equals(key) && voInv!=null && voInv.getIsFreeItemMgt() != null
      && voInv.getIsFreeItemMgt().intValue() == 1) {
    
    FreeVO freevo = voLot.getFreeVO();
    //if (freevo != null && !nc.vo.ic.pub.GenMethod.isSEmptyOrNull(freevo.getVfree0())) {
    if (freevo != null && freevo.getVfree0()!= null) {
      InvVO invvo = m_voBill.getItemInv(irow);
      if (invvo != null)
        invvo.setFreeItemVO(freevo);
      getFreeItemRefPane().setFreeItemParam(invvo);
      getBillCardPanel().setBodyValueAt(freevo.getVfree0(), irow,
          "vfree0");
      for (int i = 1; i <= FreeVO.FREE_ITEM_NUM; i++) {
        if (getBillCardPanel().getBodyItem("vfree" + i) != null)

          getBillCardPanel().setBodyValueAt(
              freevo.getAttributeValue("vfree" + i), irow,
              "vfree" + i);
        else
          getBillCardPanel().setBodyValueAt(null, irow,
              "vfree" + i);
      }
    }
    m_voBill.setItemFreeVO(irow, freevo);
    
  }
  
  m_voBill.setItemValue(irow, "vbatchcode", voLot.getVbatchcode());
  m_voBill.setItemValue(irow, "dvalidate", voLot.getDvalidate());
  
  
}

/**
 * �����ߣ�������
 * ���ܣ����ֿ�ı��¼�����
 * ������e���ݱ༭�¼�
 * ���أ�
 * ���⣺
 * ���ڣ�(2001-5-8 19:08:05)
 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
 *
 *
 *
 *
 */
public void afterWhInEdit(nc.ui.pub.bill.BillEditEvent e) {
	//�ֿ�
	try {
		WhVO voWh =getWhInfoByRef("cinwarehouseid");
		if (m_voBill != null)
			m_voBill.setWhIn(voWh);
		////�����֯����
		//nc.ui.pub.bill.BillItem biCalBody =
			//getBillCardPanel().getHeadItem("pk_calbody");
		//if (biCalBody != null) {
			//if (voWh != null)
				//biCalBody.setValue(voWh.getPk_calbody());
			//else
				//biCalBody.setValue(null);
		//}
		//nc.ui.pub.bill.BillItem biCalBodyname =
			//getBillCardPanel().getHeadItem("vcalbodyname");
		//if (biCalBodyname != null) {
			//if (voWh != null)
				//biCalBodyname.setValue(voWh.getVcalbodyname());
			//else
				//biCalBodyname.setValue(null);
		//}

	} catch (Exception e2) {
		nc.vo.scm.pub.SCMEnv.error(e2);
	}

}

/**
   * �����ߣ�������
   * ���ܣ�����ֿ�ı��¼�����
   * ������e���ݱ༭�¼�
   * ���أ�
   * ���⣺
   * ���ڣ�(2001-5-8 19:08:05)
   * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
   *
   *
   *
   *
   */
public void afterWhOutEdit(nc.ui.pub.bill.BillEditEvent e) {
//  �ֿ�
	try {
		WhVO voWh = getWhInfoByRef("coutwarehouseid");

		if (m_voBill != null) {
			m_voBill.setWhOut(voWh);
			//���β�ִ���
			m_voBill.clearInvQtyInfo();
			//������
			String[] sIKs = getClearIDs(1, "coutwarehouseid");
			int iRowCount = getBillCardPanel().getRowCount();
			for (int row = 0; row < iRowCount; row++)
				clearRowData(row, sIKs);

			//���¼���ƻ����ۣ���Ϊ���ܡ��ֿ��ѡ���ƻ���û�ܱ��������"
			String sWhID = null;
			String sCalID = null;
			if (getBillCardPanel().getHeadItem(m_sMainWhItemKey) != null) {
				sWhID = getBillCardPanel().getHeadItem(m_sMainWhItemKey).getValue();
			}
			
				if (sCalID == null && sWhID != null) {
					sCalID =
						(String) ((Object[]) nc
							.ui
							.scm
							.pub
							.CacheTool
							.getCellValue("bd_stordoc", "pk_stordoc", "pk_calbody", sWhID))[0];
					 ((SpecialBillHeaderVO) m_voBill.getParentVO()).setPk_calbody_in(sCalID);
					 ((SpecialBillHeaderVO) m_voBill.getParentVO()).setPk_calbody_out(sCalID);
					
				}
			
			nc.ui.pub.bill.BillItem biwh = getBillCardPanel().getHeadItem(
				"cinwarehouseid");
			if (null != biwh){
				if (null != sCalID && !"".equals(sCalID)){
					RefFilter.filtSpecialBillWh(biwh, m_sCorpID,
							new String[] { "and gubflag='N' and sealflag='N'  AND pk_calbody='"+sCalID+"'" });
				}else{
					RefFilter.filtSpecialBillWh(biwh, m_sCorpID,
							new String[] { "and gubflag='N' and sealflag='N' " });
				}
			}

			int rowCount = getBillCardPanel().getBillModel().getRowCount();
			String sID = null;
			ArrayList alIDs = new ArrayList();
			for (int i = 0; i < rowCount; i++) {
				sID = (String) getBillCardPanel().getBillModel().getValueAt(i, "cinventoryid");
				if (sID == null)
					continue;
				alIDs.add(sID);
			}

			if (m_sCorpID != null
				&& sCalID != null
				&& sCalID.length() > 0
				&& alIDs != null
				&& alIDs.size() > 0) {
				long ITime = System.currentTimeMillis();
				ArrayList alParam = new ArrayList();
				alParam.add(sCalID);
				alParam.add(m_sCorpID);
				alParam.add(alIDs);
				Object objValue =
					(Object) GeneralBillHelper.queryInfo(
						new Integer(QryInfoConst.CAL_PLANPRICES),
						alParam);
				if (objValue != null) {
					ArrayList alPrice = (ArrayList) objValue;
					UFDouble ufPrice = null;
					for (int i = 0; i < rowCount; i++) {
						if (alPrice.get(i) != null) {
							ufPrice = (UFDouble) alPrice.get(i);
							m_voBill.getItemInv(i).setNplannedprice(ufPrice);
						}
					}

				}
			}

		}

		showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill","UPP4008bill-000154")/*@res "����ֿ��޸ģ�������ȷ����������Ρ�������"*/);

	} catch (Exception e2) {
		nc.vo.scm.pub.SCMEnv.error(e2);
	}

}

/**
 * �����ߣ����˾�
 * ���ܣ����ָ���е�����
 * ������
 * ���أ�
 * ���⣺
 * ���ڣ�(2001-5-9 9:23:32)
 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
 *
 *
 *
 *
 */
protected void clearRowData(int row) {
	nc.ui.pub.bill.BillModel bmBill = getBillCardPanel().getBillModel();
	if (bmBill != null) {
		int iRowCount = bmBill.getRowCount();
		BillItem[] items = getBillCardPanel().getBodyItems();
		if (items == null || row >= iRowCount) {
			nc.vo.scm.pub.SCMEnv.out("row too big.");
			return;
		}
		//ɾ����������
		//ɾ����������
		String sColKey = null;
		int iColCount = items.length;
		for (int col = 0; col < iColCount; col++) {
			if (items[col] != null) {
				sColKey = items[col].getKey();
				if (!"cspecialhid".equals(sColKey)
					&& !"cspecialbid".equals(sColKey)
					&& !"crowno".equals(sColKey)
					&& !"ts".equals(sColKey)
					&& !"invsetparttype".equals(sColKey)
					&& !"cwarehousename".equals(sColKey))
					bmBill.setValueAt(null, row, col);
			}
		}
		//ͬ��vo
		if (m_voBill != null)
			m_voBill.clearItem(row);
	}
}

	/**
	   * �����ߣ����˾�
	   * ���ܣ����ָ���С�ָ���е�����
	   * ������
	   * ���أ�
	   * ���⣺
	   * ���ڣ�(2001-5-9 9:23:32)
	   * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	   *
	   *
	   *
	   *
	   */
	protected void clearRowData(int row, String[] saColKey) {
		nc.ui.pub.bill.BillModel bmBill= getBillCardPanel().getBillModel();
		int iRowCount= bmBill.getRowCount();
		if (row >= iRowCount || saColKey == null || saColKey.length == 0) {
			nc.vo.scm.pub.SCMEnv.out("row too big.");
			return;
		}
		//ɾ����������
		for (int col= 0; col < saColKey.length; col++)
			if (saColKey[col] != null
			&& getBillCardPanel().getBodyItem(saColKey[col]) != null) {
				try {
					bmBill.setValueAt(null, row, saColKey[col]);
					//ͬ��vo
					m_voBill.setItemValue(row, saColKey[col], null);
					if (saColKey[col].trim().equals("vfree0")) {
						for (int i= 0; i < 10; i++) {
							m_voBill.setItemValue(row, "vfree" + Integer.toString(i + 1).trim(), null);
						}
					}
				} catch (Exception e) {
					//nc.vo.scm.pub.SCMEnv.error(e);
					nc.vo.scm.pub.SCMEnv.out("nc.ui.ic.pub.bill.SpecialBillBaseUI.clearRowData(int, String [])��set value ERR.--->" + saColKey[col]);
				} finally {

				}
			}

	}

/**
   * �����ߣ�������
   * ���ܣ��б༭���
   * ������
   * ���أ�
   * ���⣺
   * ���ڣ�(2001-5-24 ���� 9:38)
   * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
   */
protected void colEditableSet(String sItemKey, int iRow) {
	nc.ui.pub.bill.BillItem bi =
		getBillCardPanel().getBillData().getBodyItem(sItemKey);
	//����㲻Ϊ�ֿ����ʱ���������޴�����ֹ��������ֵ
	if ((!sItemKey.equals("cinventorycode") && !sItemKey.equals("cwarehousename"))
		&& (null == m_voBill.getItemValue(iRow, "cinventoryid")
			|| m_voBill.getItemValue(iRow, "cinventoryid").toString().trim().length() == 0)) {
		bi.setEnabled(false);
		return;
	} else {
		bi.setEnabled(bi.isEdit());
	}
	/*
	//ת�ⵥ����Դ����ʱ����ֹ�޸�Ӧ��������Ӧ��������
	if ((null != m_voBill.getItemValue(iRow, "csourcetype"))
		&& (m_voBill.getItemValue(iRow, "csourcetype").toString().trim().length() != 0)) {
		if ((sItemKey.equals("dshldtransnum"))
			|| (sItemKey.equals("nshldtransastnum"))) {
			bi.setEnabled(false);
			return;
		}
	}
	*/

	if (sItemKey.equals("vfree0")) {
		if ((null != m_voBill.getItemValue(iRow, "isFreeItemMgt"))
			&& (Integer
				.valueOf(m_voBill.getItemValue(iRow, "isFreeItemMgt").toString())
				.intValue()
				!= 0)) {
			bi.setEnabled(true);
		} else {
			bi.setEnabled(false);
		}
	} else if (sItemKey.equals("vbatchcode")) {
		if ((null != m_voBill.getItemValue(iRow, "isLotMgt"))
			&& (Integer.valueOf(m_voBill.getItemValue(iRow, "isLotMgt").toString()).intValue()
				!= 0)) {
			String ColName =
				getBillCardPanel().getBillData().getBodyItem("vbatchcode").getName();
      TableColumn tc = GeneralBillUICtl.getColumn(getBillCardPanel().getBillTable(),ColName);
      if(tc!=null)
        tc.setCellEditor(new BillCellEditor(getLotNumbRefPane()));
		} else {
			bi.setEnabled(false);
		}
	} else if (sItemKey.equals("dvalidate")) {
		if ((null != m_voBill.getItemValue(iRow, "isValidateMgt"))
			&& (Integer
				.valueOf(m_voBill.getItemValue(iRow, "isValidateMgt").toString())
				.intValue()
				!= 0)) {
			bi.setEnabled(false);
		} else {
			bi.setEnabled(false);
		}
	} else if (sItemKey.equals("scrq")) {
		if ((null != m_voBill.getItemValue(iRow, "isValidateMgt"))
			&& (Integer
				.valueOf(m_voBill.getItemValue(iRow, "isValidateMgt").toString())
				.intValue()
				!= 0)) {
			bi.setEnabled(false);
		} else {
			bi.setEnabled(false);
		}
	} else if (sItemKey.equals("castunitname")) {
		if ((null != m_voBill.getItemValue(iRow, "isAstUOMmgt"))
			&& (Integer
				.valueOf(m_voBill.getItemValue(iRow, "isAstUOMmgt").toString())
				.intValue()
				!= 0)) {
			bi.setEnabled(true);
		} else {
			bi.setEnabled(false);
		}
	} else if (sItemKey.equals("hsl")) {
		if (m_voBill != null
			&& m_voBill.getItemValue(iRow, "isAstUOMmgt") != null
			&& ((Integer) m_voBill.getItemValue(iRow, "isAstUOMmgt")).intValue() == 1) {
			if (((null != m_voBill.getItemValue(iRow, "isSolidConvRate"))
				&& (Integer
					.valueOf(m_voBill.getItemValue(iRow, "isSolidConvRate").toString())
					.intValue()
					== 0))) {
				//�ǹ̶�������
				bi.setEnabled(true);
			} else {
				//�̶�������
				bi.setEnabled(false);
			}
		} else {
			//�Ǹ���������
			bi.setEnabled(false);
		}
	} else if (sItemKey.equals(m_sNumItemKey)) {
		Object oTempAstUnit = getBillCardPanel().getBodyValueAt(iRow, "castunitname");
		Object oTempAstNum = getBillCardPanel().getBodyValueAt(iRow, m_sAstItemKey);
		if (m_voBill != null
			&& m_voBill.getItemValue(iRow, "isAstUOMmgt") != null
			&& ((Integer) m_voBill.getItemValue(iRow, "isAstUOMmgt")).intValue() == 1) {
			if (oTempAstNum == null
				|| oTempAstNum.toString().trim().length() == 0
				|| oTempAstUnit == null
				|| oTempAstUnit.toString().trim().length() == 0) {
				getBillCardPanel().getBillData().getBodyItem(m_sNumItemKey).setEnabled(false);

				String[] args = new String[1];
		        args[0] = String.valueOf(iRow + 1);
		        String message = nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill","UPP4008bill-000343",null,args);
		        /*@res "�����{0}�д��Ϊ�����������������븨��λ�͸�������"*/

				showHintMessage(message);
			} else {
				getBillCardPanel().getBillData().getBodyItem(m_sNumItemKey).setEnabled(true);
			}
		} else {
			getBillCardPanel().getBillData().getBodyItem(m_sNumItemKey).setEnabled(true);
		}
	}
	if (sItemKey.equals("dshldtransnum")) {
		if ((null != m_voBill.getItemValue(iRow, "fbillrowflag"))
			&& (Integer
				.valueOf(m_voBill.getItemValue(iRow, "fbillrowflag").toString())
				.intValue()
				== BillRowType.part)) {
			bi.setEnabled(false);
		} else {
			//bi.setEnabled(true);
			Object oTempAstUnit = getBillCardPanel().getBodyValueAt(iRow, "castunitname");
			Object oTempAstNum =
				getBillCardPanel().getBodyValueAt(iRow, "nshldtransastnum");
			if (m_voBill != null
				&& m_voBill.getItemValue(iRow, "isAstUOMmgt") != null
				&& ((Integer) m_voBill.getItemValue(iRow, "isAstUOMmgt")).intValue() == 1) {
				if (oTempAstNum == null
					|| oTempAstNum.toString().trim().length() == 0
					|| oTempAstUnit == null
					|| oTempAstUnit.toString().trim().length() == 0) {
					bi.setEnabled(false);

					String[] args = new String[1];
			        args[0] = String.valueOf(iRow + 1);
			        String message = nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill","UPP4008bill-000344",null,args);
			        /*@res "�����{0}�д��Ϊ�����������������븨��λ��Ӧ����������"*/

					showHintMessage(message);
				} else {
					bi.setEnabled(true);
				}
			} else {
				bi.setEnabled(true);
			}
		}
	} else if (sItemKey.equals("nshldtransastnum")) {
		if ((null != m_voBill.getItemValue(iRow, "isAstUOMmgt"))
			&& (Integer
				.valueOf(m_voBill.getItemValue(iRow, "isAstUOMmgt").toString())
				.intValue()
				!= 0)) {
			bi.setEnabled(true);
		} else {
			//�Ǹ���������
			bi.setEnabled(false);
		}
	}
}

/**
 * ����û��ֹ��޸����κţ����⣬��ȷ����ʧЧ���ڼ���Ӧ���ݺţ�����ȷ����ա�
 * �����ߣ�����
 * ���ܣ�
 * ������
 * ���أ�
 * ���⣺
 * ���ڣ�(2001-6-14 10:25:33)
 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
 *
 */
public void getLotRefbyHand(String ColName) {
	int iSelrow = getBillCardPanel().getBillTable().getSelectedRow();
	String strColName = ColName;
	if (strColName == null) {
		return;
	}
	String sbatchcode =
		(String) getBillCardPanel().getBodyValueAt(iSelrow, "vbatchcode");
	/** �����κ�Ϊ�գ� */
	if ((sbatchcode != null && sbatchcode.trim().length() > 0)
		&& getLotNumbRefPane().isClicked())
		return;
	/** �û��ֹ���д���κ�ʱ����⣬����������ȷ��� */
	boolean isLotRight = getLotNumbRefPane().checkData();

	if (!isLotRight) {
		getBillCardPanel().setBodyValueAt("", iSelrow, "vbatchcode");
		//added by lirr ���κ���պ��Ӧ������Ӧ���
		getBillCardPanel().setBodyValueAt(null, iSelrow, "scrq");
		getBillCardPanel().setBodyValueAt(null, iSelrow, "dvalidate");
	}

}

/**
 * ���û�ѡ�����κź��Զ�������ʧЧ�������Ӧ����ţ��������͡�
 * �����ߣ�����
 * ���ܣ�
 * ������
 * ���أ�
 * ���⣺
 * ���ڣ�(2001-6-13 17:38:31)
 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
 * 
 * 
 *
 */
public void pickupLotRef(String colname) {
	String s = colname;
	//���κŲ��մ���ʧЧ���ںͶ�Ӧ���ݺż���Ӧ��������

	String sLot = null;
	int rownum = getBillCardPanel().getBillTable().getSelectedRow();
	if (s == null) {
		return;
	}
	if (s.equals("vbatchcode")) {
		//�޸��ˣ������� �޸����ڣ�2007-9-6����07:15:51 �޸�ԭ�򣺵����κŲ�����ʱ����Ҫȥ����	
		String vbatchcode = (String) getBillCardPanel().getBodyValueAt(
				rownum, "vbatchcode");
		if(!(vbatchcode!=null&&isExistInBatch((String)m_voBill.getItemValue(rownum,"cinventoryid"),vbatchcode)))
			return;
    
    nc.vo.ic.pub.lot.LotNumbRefVO[] voLot = null;
    try {
      nc.ui.ic.pub.lot.LotNumbRefPane lotRef = (nc.ui.ic.pub.lot.LotNumbRefPane) getBillCardPanel()
      .getBodyItem("vbatchcode").getComponent();
      // �ֹ����룬���ܻ����쳣��
      voLot = lotRef.getLotNumbRefVOs();
      if(isLineCardEdit() && voLot.length>1){
        voLot = new nc.vo.ic.pub.lot.LotNumbRefVO[]{voLot[0]};
      }
      if(voLot!=null && voLot.length>1){
        showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
            "4008other","UPP4008other-000509")/* @res "��ѡ��һ�������У�" */);
      }
      sLot = (String) getBillCardPanel().getBodyValueAt(rownum, "vbatchcode");
      BatchCodeDefSetTool.setBatchCodeInfo(getBillCardPanel(),rownum,(String)m_voBill.getItemValue(rownum,"cinventoryid"),sLot,m_sCorpID);
      if(voLot!=null && voLot.length>0){
        //String vfree0 = (String)getBillCardPanel().getBodyValueAt(rownum, IItemKey.FREE0);
        //if(nc.vo.ic.pub.GenMethod.isSEmptyOrNull(vfree0))
        if(nc.vo.ic.pub.GenMethod.isGTZero(voLot[0].getNinnum()))
          synlot(voLot[0], rownum,IItemKey.FREE0);
      }
    } catch (Exception e) {
      nc.vo.scm.pub.SCMEnv.error(e);
      return;
    }
	
//		if (sbatchcode != null && sbatchcode.trim().length() > 0) {
//			//�����ε�ʧЧ����
//			getBillCardPanel().setBodyValueAt(
//				getLotNumbRefPane().getRefInvalideDate() == null
//					? ""
//					: getLotNumbRefPane().getRefInvalideDate().toString(),
//				iSelrow,
//				"dvalidate");
//
//			////��������
//			nc.vo.scm.ic.bill.InvVO voInv = m_voBill.getItemInv(iSelrow);
//			if (voInv.getIsValidateMgt().intValue() == 1) {
//				nc.vo.pub.lang.UFDate dvalidate = getLotNumbRefPane().getRefInvalideDate();
//				if (dvalidate != null) {
//					getBillCardPanel().setBodyValueAt(
//						dvalidate.getDateBefore(voInv.getQualityDay().intValue()).toString(),
//						iSelrow,
//						"scrq");
//				}
//
//			}
//			///
//
//			//ͬ���ı�m_voBill
//			m_voBill.setItemValue(
//				iSelrow,
//				"vbatchcode",
//				getLotNumbRefPane().getRefLotNumb());
//			m_voBill.setItemValue(
//				iSelrow,
//				"dvalidate",
//				getLotNumbRefPane().getRefInvalideDate());
//
//
//		} else {
//			getBillCardPanel().setBodyValueAt("", iSelrow, "dvalidate"); //��ձ�������ʧЧ����
//			getBillCardPanel().setBodyValueAt("", iSelrow, "scrq"); //��ձ���������������
//			//ͬ���ı�m_voBill
//			m_voBill.setItemValue(iSelrow, "vbatchcode", null);
//			m_voBill.setItemValue(iSelrow, "dvalidate", null);
//
//		}
	}
}

/**
   * �����ߣ����˾�
   * ���ܣ��ڱ�������ʾVO
   * ������
   * ���أ�
   * ���⣺
   * ���ڣ�(2001-5-9 9:23:32)
   * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
   *
   *
   *
   *
   */
protected void setBillValueVO(SpecialBillVO bvo) {
	setBillValueVO(bvo,true);
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
 * �����ߣ����˾�
 * ���ܣ����ñ�����ʾ��
 * ������
 * ���أ�
 * ���⣺
 * ���ڣ�(2001-5-9 9:23:32)
 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
 *
 *
 *
 *
 */
protected void setBodyInvValue(int row, InvVO voInv) {
	try {
		getBillCardPanel().setBodyValueAt(voInv.getCinventoryid(), row, "cinventoryid");
	} catch (Exception e) {
	}
	try {
		getBillCardPanel().setBodyValueAt(voInv.getCinventorycode(), row, "cinventorycode");
	} catch (Exception e) {
	}
	try {
		getBillCardPanel().setBodyValueAt(voInv.getInvname(), row, "invname");
	} catch (Exception e) {
	}
	try {
		getBillCardPanel().setBodyValueAt(voInv.getInvspec(), row, "invspec");
	} catch (Exception e) {
	}
	try {
		getBillCardPanel().setBodyValueAt(voInv.getInvtype(), row, "invtype");
	} catch (Exception e) {
	}
	try {
		getBillCardPanel().setBodyValueAt(voInv.getMeasdocname(), row, "measdocname");
	} catch (Exception e) {
	}
	try {
		//ȱʡ������ID
		getBillCardPanel().setBodyValueAt(voInv.getCastunitid(), row, "castunitid");
	} catch (Exception e) {
	}
	try {
		//ȱʡ����������
		getBillCardPanel().setBodyValueAt(voInv.getCastunitname(), row, "castunitname");
	} catch (Exception e) {
	}
	
	try {
		getBillCardPanel().setBodyValueAt(voInv.getHsl(), row, "hsl");
		//������
		m_voBill.setItemValue(row, "hsl", voInv.getHsl());
		//�Ƿ�̶�����
		m_voBill.setItemValue(row, "isSolidConvRate", voInv.getIsSolidConvRate());

	} catch (Exception e) {
	}
	//�ƻ���
	try {
		getBillCardPanel().setBodyValueAt(
			voInv.getNplannedprice(),
			row,
			"jhdj");
	} catch (Exception e) {
	}
	try {
		//�������������ʾ��
		getBillCardPanel().setBodyValueAt("", row, "vfree0");
		if (null != voInv.getFreeItemVO())
			for(int i = 0 ;i <= 5;i++)
				getBillCardPanel().setBodyValueAt(voInv.getAttributeValue("vfree"+i), row, "vfree"+i);
			
	} catch (Exception e) {
	}
	
	String vbatchcode = voInv.getVbatchcode();
	
	if (vbatchcode != null
		&& vbatchcode.trim().length() > 0) {
		getBillCardPanel().setBodyValueAt(vbatchcode, row, IItemKey.VBATCHCODE);
			
		getLotNumbRefPane().setText(vbatchcode);
		BillEditEvent ev = new BillEditEvent(
				getBillCardPanel().getBodyItem(
						IItemKey.VBATCHCODE), null,
				vbatchcode, IItemKey.VBATCHCODE,
				row, BillItem.BODY);
		
		WhVO wvo = m_voBill.getWhOut();
		getLotNumbRefPane().setParameter(wvo, m_voBill.getItemInv(row));
		
		afterLotEdit(ev);
	}
}

/**
 * �����ߣ����˾�
 * ���ܣ������������ݵĳ�ʼ���ݣ������ڣ��Ƶ��˵ȡ�
 * ������
 * ���أ�
 * ���⣺
 * ���ڣ�(2001-5-9 9:23:32)
 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
 *
 *
 *
 *
 */
protected void setNewBillInitData() {
	try {
		nc.ui.pub.ClientEnvironment ce= nc.ui.pub.ClientEnvironment.getInstance();
		if (ce == null) {
			nc.vo.scm.pub.SCMEnv.out("ce null ERR.");
			return;
		}
		try {
			getBillCardPanel().setHeadItem("dbilldate", m_sLogDate);
			getBillCardPanel().setHeadItem("vshldarrivedate", m_sLogDate);
		} catch (Exception e) {

		}
		try {
			getBillCardPanel().setTailItem("coperatorid", m_sUserID);
			getBillCardPanel().setTailItem("coperatorname", m_sUserName);
			getBillCardPanel().setTailItem("cauditorid", null);
			getBillCardPanel().setTailItem("cauditorname", null);
			getBillCardPanel().setTailItem("vadjuster", null);
			getBillCardPanel().setTailItem("vadjustername", null);
			
			if (getBillCardPanel().getTailItem("clastmodiid") != null)
				getBillCardPanel().setTailItem("clastmodiid", m_sUserID);
			if (getBillCardPanel().getTailItem("clastmodiname") != null)
				getBillCardPanel().setTailItem("clastmodiname", m_sUserName);
			
			//UFDateTime ufdPre = new UFDateTime(System.currentTimeMillis());
/*			if (getBillCardPanel().getTailItem("tlastmoditime") != null)
				getBillCardPanel().setTailItem("tlastmoditime",
						ufdPre.toString());
			//v5�Ƶ�ʱ��
			if (getBillCardPanel().getTailItem(IItemKey.TFIRSTTIME) != null)
				getBillCardPanel().setTailItem(IItemKey.TFIRSTTIME,
						ufdPre.toString());*/
			
			if (m_voBill != null) {
				m_voBill.setHeaderValue("coperatorid", m_sUserID);
				m_voBill.setHeaderValue("coperatorname", m_sUserName);
				m_voBill.setHeaderValue("cauditorid", null);
				m_voBill.setHeaderValue("cauditorname", null);
				m_voBill.setHeaderValue("vadjuster", null);
				m_voBill.setHeaderValue("vadjustername", null);
				m_voBill.setHeaderValue("clastmodiid", m_sUserID);
				m_voBill.setHeaderValue("clastmodiname", m_sUserName);
/*				m_voBill.setHeaderValue("tlastmoditime", ufdPre.toString());
				m_voBill.setHeaderValue(IItemKey.TFIRSTTIME, ufdPre.toString());*/
			}
		} catch (Exception e) {

		}
		try {
			getBillCardPanel().setHeadItem("pk_corp", m_sCorpID);
			getBillCardPanel().setHeadItem("vbillcode", m_sCorpID);
			if (m_voBill != null){
				m_voBill.setHeaderValue("pk_corp", m_sCorpID);
				m_voBill.setHeaderValue("vbillcode", m_sCorpID);
			}
		} catch (Exception e) {

		}
		//try {
			//getBillCardPanel().setHeadItem(
				//"vbillcode",
				//nc.ui.pub.billcodemanage.BillcodeRuleBO_Client.getBillCode(
					//m_sBillTypeCode,
					//m_sCorpID,
					//null,
					//null));
		//} catch (Exception e) {
		//}

	} catch (Exception e) {

	}

}

/**
 * �����ߣ����˾�
 * ���ܣ����ñ�β��ʾ����,��m_voBill��ȡ�������״̬��Ҫ�ض��ִ���
 * ������
 * ���أ�
 * ���⣺
 * ���ڣ�(2001-5-9 9:23:32)
 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
 *
 *
 *
 *
 */
protected void setTailValue(int row) {
	if (m_voBill == null) {
		nc.vo.scm.pub.SCMEnv.out("no vobill.no taildata");
		return;
	}
//	boolean isNeedQuery=false;
	
	// �޸��ˣ������� ����09:48:07_2009-10-17 �޸�ԭ�� ���������к��Զ�����

	nc.ui.scm.pub.report.BillRowNo.addLineRowNo(
			getBillCardPanel(),
			m_sBillTypeCode,
			m_sBillRowNo);
	//�ֿ��Ƿ�Ϊ��
	Object oInWhID= null;
	oInWhID= m_voBill.getHeaderValue("cinwarehouseid");
	Object oOutWhID= null;
	oOutWhID= m_voBill.getHeaderValue("coutwarehouseid");
	if(oOutWhID==null)
		oOutWhID=(String)m_voBill.getItemValue(row,"cwarehouseid");
	if(oOutWhID==null)
			return;
	//����Ƿ�Ϊ��
	Object oInvID= m_voBill.getItemValue(row, "cinventoryid");
		if (oInvID == null) {
			nc.vo.scm.pub.SCMEnv.out(row + "row data ERR");
			return;
		}

	BillItem biMax = getBillCardPanel().getTailItem("nmaxstocknum");
	BillItem biMin = getBillCardPanel().getTailItem("nminstocknum");
	BillItem biOpt = getBillCardPanel().getTailItem("norderpointnum");
	BillItem biSafe = getBillCardPanel().getTailItem("nsafestocknum");
	BillItem biWhQty = getBillCardPanel().getTailItem("bkxcl");
	BillItem biBdQty = getBillCardPanel().getTailItem("xczl");
	BillItem biOutWhQty = getBillCardPanel().getTailItem("outbkxcl");

//	if ((biWhQty != null && biWhQty.isShow())
//	    || (biBdQty != null && biBdQty.isShow())
//	   	|| (biOutWhQty != null && biOutWhQty.isShow()) ){
//		   isNeedQuery=true;
//		   }

	Object oInWhQty= null;
	Object oOutWhQty= null;
	Object oTotalQty= null;
	Object nmaxstocknum= null;
	Object nminstocknum= null;
	Object norderpointnum= null;
	Object nsafestocknum= null;
	Object nchzl=null;

	int iFlag = 0;
    if (((biMax != null && biMax.isShow())
            || (biMin != null && biMin.isShow())
            || (biOpt != null && biOpt.isShow()) || (biSafe != null && biSafe
            .isShow()))
            && m_sCorpID != null && oOutWhID != null && oInvID != null) {
    		//���Ƿ��Ѿ�����������Ϣ
		    nmaxstocknum= m_voBill.getItemValue(row, "nmaxstocknum");
			nminstocknum= m_voBill.getItemValue(row, "nminstocknum");
			norderpointnum= m_voBill.getItemValue(row, "norderpointnum");
			nsafestocknum= m_voBill.getItemValue(row, "nsafestocknum");
			nchzl= m_voBill.getItemInv(row).getChzl();
			
		    if (nmaxstocknum == null
			&& nminstocknum == null
			&& norderpointnum == null
			&& nsafestocknum == null&&nchzl==null )
		    	 iFlag += 1;
	               
    }
    if (((biWhQty != null && biWhQty.isShow()) || (biBdQty != null && biBdQty
            .isShow()))
            && m_sCorpID != null && oOutWhID != null && oInvID != null) {
        iFlag += 2;
    }
    switch (iFlag) {
    case 0:
        break;
    case 1:
        iFlag = QryInfoConst.QTY_CTRL;
        break;
    case 2:
        iFlag = QryInfoConst.QTY_ONHAND;
        break;
    case 3:
        iFlag = QryInfoConst.QTY_ALL;
        break;
    }
    
//	if(!isNeedQuery){
//
//		if ((biMax != null && biMax.isShow())
//	    || (biMin != null && biMin.isShow())
//	    || (biOpt != null && biOpt.isShow())
//	    || (biSafe != null && biSafe.isShow())){
//		    //���Ƿ��Ѿ�����������Ϣ
//		    nmaxstocknum= m_voBill.getItemValue(row, "nmaxstocknum");
//			nminstocknum= m_voBill.getItemValue(row, "nminstocknum");
//			norderpointnum= m_voBill.getItemValue(row, "norderpointnum");
//			nsafestocknum= m_voBill.getItemValue(row, "nsafestocknum");
//			nchzl= m_voBill.getItemInv(row).getChzl();
//		    if (nmaxstocknum == null
//			&& nminstocknum == null
//			&& norderpointnum == null
//			&& nsafestocknum == null&&nchzl==null )
//		    	isNeedQuery=true;
//		    }
//
//		}

	if(iFlag>0){

	//ȡ��ǰvo�е�����

		ArrayList alIDs= new ArrayList();
		//alIDs.add(oInWhID);
		ArrayList alQty= null;
		try {
		String sCalID =(String) ((Object[]) nc.ui.scm.pub.CacheTool.getCellValue("bd_stordoc", "pk_stordoc", "pk_calbody", (String)oOutWhID))[0];
		
		alIDs.add(oOutWhID);
		alIDs.add(oInvID);
		alIDs.add(sCalID);
		alIDs.add(m_sCorpID);

		
		//����
		
			//alQty= (ArrayList) SpecialHBO_Client.queryInfo(new Integer(2), alIDs);
//			alQty=
		InvVO voInvTmp=
				(InvVO) SpecialBillHelper.queryInfo( new Integer(iFlag), alIDs);

		if (voInvTmp != null) {
//			oInWhQty= alQty.get(0);
			oOutWhQty= voInvTmp.getBkxcl();
			oTotalQty= voInvTmp.getXczl();
			nmaxstocknum= voInvTmp.getNmaxstocknum();
			nminstocknum= voInvTmp.getNminstocknum();
			norderpointnum= voInvTmp.getNorderpointnum();
			nsafestocknum= voInvTmp.getNsafestocknum();
			//д��vo����
			m_voBill.setItemValue(row, "inbkxcl", oInWhQty);
			m_voBill.setItemValue(row, "outbkxcl", oOutWhQty);
			m_voBill.setItemValue(row, "bkxcl", oOutWhQty);
			m_voBill.setItemValue(row, "xczl", oTotalQty);
			m_voBill.setItemValue(row, "nmaxstocknum", nmaxstocknum);
			m_voBill.setItemValue(row, "nminstocknum", nminstocknum);
			m_voBill.setItemValue(row, "norderpointnum", norderpointnum);
			m_voBill.setItemValue(row, "nsafestocknum", nsafestocknum);
			m_voBill.getItemInv(row).setChzl(new UFDouble(0.0));
			
           
        }
		} catch (Exception e) {
			nc.vo.scm.pub.SCMEnv.error(e);
		}
//		// ���ݸ�ʽ�������ִ������ִ���������߿��������Ϳ��������������������ȫ�����
//		if (alQty != null && alQty.size() >= 6) {
//
//			//oInWhQty= alQty.get(0);
//			oOutWhQty= alQty.get(0);
//			oTotalQty= alQty.get(1);
//			nmaxstocknum= alQty.get(2);
//			nminstocknum= alQty.get(3);
//			norderpointnum= alQty.get(4);
//			nsafestocknum= alQty.get(5);
//			//д��vo����
//			m_voBill.setItemValue(row, "inbkxcl", oInWhQty);
//			m_voBill.setItemValue(row, "outbkxcl", oOutWhQty);
//			m_voBill.setItemValue(row, "bkxcl", oOutWhQty);
//			m_voBill.setItemValue(row, "xczl", oTotalQty);
//			m_voBill.setItemValue(row, "nmaxstocknum", nmaxstocknum);
//			m_voBill.setItemValue(row, "nminstocknum", nminstocknum);
//			m_voBill.setItemValue(row, "norderpointnum", norderpointnum);
//			m_voBill.setItemValue(row, "nsafestocknum", nsafestocknum);
//			m_voBill.getItemInv(row).setChzl(new UFDouble(0.0));
//
//		}
	}

	//���ֿ��ִ���
	nc.ui.pub.bill.BillItem biTail= getBillCardPanel().getTailItem("inbkxcl");
	if (biTail != null)
		if (oInWhQty != null)
			biTail.setValue(new nc.vo.pub.lang.UFDouble(oInWhQty.toString()));
		else
			biTail.setValue(null);
	//����ֿ��ִ���
	biTail= getBillCardPanel().getTailItem("outbkxcl");
	if (biTail != null)
		if (oOutWhQty != null)
			biTail.setValue(new nc.vo.pub.lang.UFDouble(oOutWhQty.toString()));
		else
			biTail.setValue(null);
	//����ֿ��ִ���
	biTail= getBillCardPanel().getTailItem("bkxcl");
	if (biTail != null)
		if (oOutWhQty != null)
			biTail.setValue(new nc.vo.pub.lang.UFDouble(oOutWhQty.toString()));
		else
			biTail.setValue(null);
	//�ִ�����
	biTail= getBillCardPanel().getTailItem("xczl");
	if (biTail != null)
		if (oTotalQty != null)
			biTail.setValue(new nc.vo.pub.lang.UFDouble(oTotalQty.toString()));
		else
			biTail.setValue(null);
	//��߿��
	biTail= getBillCardPanel().getTailItem("nmaxstocknum");
	if (biTail != null)
		if (nmaxstocknum != null)
			biTail.setValue(new nc.vo.pub.lang.UFDouble(nmaxstocknum.toString()));
		else
			biTail.setValue(null);
	//��Ϳ��
	biTail= getBillCardPanel().getTailItem("nminstocknum");
	if (biTail != null)
		if (nminstocknum != null)
			biTail.setValue(new nc.vo.pub.lang.UFDouble(nminstocknum.toString()));
		else
			biTail.setValue(null);
	//��ȫ���
	biTail= getBillCardPanel().getTailItem("nsafestocknum");
	if (biTail != null)
		if (nsafestocknum != null)
			biTail.setValue(new nc.vo.pub.lang.UFDouble(nsafestocknum.toString()));
		else
			biTail.setValue(null);
	//��������
	biTail= getBillCardPanel().getTailItem("norderpointnum");
	if (biTail != null)
		if (norderpointnum != null)
			biTail.setValue(new nc.vo.pub.lang.UFDouble(norderpointnum.toString()));
		else
			biTail.setValue(null);

}

	/**
	 * �����ߣ����˾�
	 * ���ܣ����ñ�β��ʾ����,����null����ա�
	 * ������
	 * ���أ�
	 * ���⣺
	 * ���ڣ�(2001-5-9 9:23:32)
	 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 *
	 *
	 *
	 *
	 */
	protected void setTailValue(InvVO voInv) {
		//�����ִ���
		nc.ui.pub.bill.BillItem biTail= null;
		biTail= getBillCardPanel().getTailItem("bkxcl");
		if (biTail != null)
			if (voInv != null)
				biTail.setValue(voInv.getBkxcl());
			else
				biTail.setValue(null);

		biTail= getBillCardPanel().getTailItem("outbkxcl");
		if (biTail != null)
			if (voInv != null)
				biTail.setValue(voInv.getBkxcl());
			else
				biTail.setValue(null);
		//�ִ�����
		biTail= getBillCardPanel().getTailItem("xczl");
		if (biTail != null)
			if (voInv != null)
				biTail.setValue(voInv.getBkxcl());
			else
				biTail.setValue(null);
		//��߿��
		biTail= getBillCardPanel().getTailItem("nmaxstocknum");
		if (biTail != null)
			if (voInv != null)
				biTail.setValue(voInv.getNmaxstocknum());
			else
				biTail.setValue(null);
		//��Ϳ��
		biTail= getBillCardPanel().getTailItem("nminstocknum");
		if (biTail != null)
			if (voInv != null)
				biTail.setValue(voInv.getNminstocknum());
			else
				biTail.setValue(null);
		//��ȫ���
		biTail= getBillCardPanel().getTailItem("nsafestocknum");
		if (biTail != null)
			if (voInv != null)
				biTail.setValue(voInv.getNsafestocknum());
			else
				biTail.setValue(null);
		//��������
		biTail= getBillCardPanel().getTailItem("norderpointnum");
		if (biTail != null)
			if (voInv != null)
				biTail.setValue(voInv.getNorderpointnum());
			else
				biTail.setValue(null);

	}

	/**
	 * �����ߣ����˾�
	 * ���ܣ�ͬ�������ݣ����λ�����к�
	 * ������int iFirstLine,iLastLine �кţ�start from 0
	       int iCol�� start from 0
	       int type1: add
	             0: update
	             -1:delete

	 * ���أ�
	 * ���⣺
	 * ���ڣ�(2001-5-9 9:23:32)
	 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 2001-06-13. ͬ��VO
	 *
	 *
	 *
	 */
	protected void synchLineData(
		int iFirstLine,
		int iLastLine,
		int iCol,
		int iType) {
		if (iFirstLine < 0 || iLastLine < 0)
			return;
		//if(m_alLocatorData==null){
		//nc.vo.scm.pub.SCMEnv.out("init Locator data.");
		//m_alLocatorData=new ArrayList();
		////��ʼ�������ݣ������ڸ��Ƶ���ʱ��m_alLocatorData==null ������������Ϊ0��
		//m_alLocatorData=new ArrayList(getBillCardPanel().getBillModel().getRowCount());
		//}
		//if(m_alSerialData==null){
		//nc.vo.scm.pub.SCMEnv.out("init serial data.");
		//m_alSerialData=new ArrayList();
		////��ʼ�������ݣ������ڸ��Ƶ���ʱ��m_alSerialData==null ������������Ϊ0��
		//m_alSerialData=new ArrayList(getBillCardPanel().getBillModel().getRowCount());
		//}
		if (m_voBill == null)
			m_voBill= new SpecialBillVO();
//		 �޸��ˣ������� �޸����ڣ�2007-10-16����04:56:44 �޸�ԭ�򣺲����б仯��ֱ�ӷ���
		if(iType!=javax.swing.event.TableModelEvent.INSERT &&
				iType!=javax.swing.event.TableModelEvent.UPDATE &&
				iType!=javax.swing.event.TableModelEvent.DELETE &&
				getBillCardPanel().getRowCount()<=m_voBill.getItemCount())
			return;

		switch (iType) {
			case javax.swing.event.TableModelEvent.INSERT : //���У��塢׷�ӡ�ճ��
				m_voBill.insertItem(iFirstLine);
				break;
			case javax.swing.event.TableModelEvent.UPDATE :
        while(getBillCardPanel().getRowCount() > m_voBill.getItemCount()){
//        	 �޸��ˣ������� �޸����ڣ�2007-10-16����04:56:44 �޸�ԭ�򣺴����iFirstLine�����⣬������һ��
        	iFirstLine = getBillCardPanel().getRowCount()-1;  
          if(iFirstLine < 0)
            iFirstLine = 0;
          m_voBill.insertItem(iFirstLine);
          iFirstLine++;
        }
				break;
			case javax.swing.event.TableModelEvent.DELETE :
				m_voBill.removeItem(iFirstLine);
				break;
		}
	}

	/** ���·�������ֵ�޸�ʱ����
	   * This fine grain notification tells listeners the exact range
	   * of cells, rows, or columns that changed.
	   */
	public void tableChanged(javax.swing.event.TableModelEvent e) {
		if (/*((e.getType() == javax.swing.event.TableModelEvent.INSERT)
			|| (e.getType() == javax.swing.event.TableModelEvent.DELETE))
			&&*/ (e.getSource() == getBillCardPanel().getBillModel())) {
			synchLineData(e.getFirstRow(), e.getLastRow(), e.getColumn(), e.getType());
		}
	}





	/**
	 * Invoked when an action occurs.
	 */
	public void actionPerformed(java.awt.event.ActionEvent e) {
		if (e.getSource() == getAddNewRowNoItem()) {
			onAddNewRowNo();
		}else if (e.getSource() == getLineCardEditItem()) {
      onLineCardEdit();
    }
	}



/**
   * ���� ReturnDlg1 ����ֵ��
   * @return nc.ui.ic.ic205.ReturnDlg
   */
/* ���棺�˷������������ɡ� */
protected ClientUIInAndOut getAuditDlg(
	String sTitle,
	ArrayList alInVO,
	ArrayList alOutVO) {
	if (m_dlgInOut == null) {
		try {
			// user code begin {1}
			m_dlgInOut= new ClientUIInAndOut(this, sTitle);
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	m_dlgInOut.setVO(
		m_voBill,
		alInVO,
		alOutVO,
		m_sBillTypeCode,
		m_voBill.getPrimaryKey().trim(),
		m_sCorpID,
		m_sUserID);
	m_dlgInOut.setName("BillDlg");
	//m_dlgInOut.setDefaultCloseOperation(javax.swing.WindowConstants.HIDE_ON_CLOSE);
	return m_dlgInOut;
}

/**�Ҽ��˵�
 * �˴����뷽��˵����
 * �������ڣ�(2001-3-27 11:09:34)
 * @param e java.awt.event.ActionEvent
 */
public void onMenuItemClick(java.awt.event.ActionEvent e) {
	//	getBillCardPanel().onMenuItemClick(e);
	UIMenuItem item = (UIMenuItem) e.getSource();
	if (item == getBillCardPanel().getCopyLineMenuItem()) {
		onCopyRow();
	} else if (item == getBillCardPanel().getPasteLineMenuItem()) {
		onPasteRow();
	} else if (item == getBillCardPanel().getInsertLineMenuItem()) {
		onInsertRow();

	} else if (item == getBillCardPanel().getDelLineMenuItem()) {
		onDeleteRow();

	} else if (item == getBillCardPanel().getAddLineMenuItem()) {
		onAddRow();
	}
	else if (item == getBillCardPanel().getPasteLineToTailMenuItem()) {
		onPasteRowTail();
	}

}

/**
 * �����ߣ�������
 * ���ܣ������ⵥ��VO��Ϊ��ͨ����VO
 * ������
 * ���أ�
 * ���⣺
 * ���ڣ�(2001-6-26 ���� 4:43)
 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
 * @return nc.vo.ic.pub.bill.GeneralBillVO
 * @param sbvo nc.vo.ic.pub.bill.SpecialBillVO
 */
protected GeneralBillVO changeFromSpecialVOtoGeneralVO(
	SpecialBillVO sbvo,
	int iInOutFlag) {
	if ((sbvo == null)
		|| (sbvo.getHeaderVO() == null)
		|| (sbvo.getChildrenVO() == null)) {
		return null;
	}
	int iItemNumb= sbvo.getChildrenVO().length;
	if (iItemNumb < 1) {
		return null;
	}
	GeneralBillVO gbvo= new GeneralBillVO(iItemNumb);

	//�Ա�ͷ
	gbvo.setParentVO(
		changeFromSpecialVOtoGeneralVOAboutHeader(gbvo, sbvo, iInOutFlag));

	//�Ա���
	for (int j= 0; j < iItemNumb; j++) {
		gbvo.setItem(
			j,
			changeFromSpecialVOtoGeneralVOAboutItem(gbvo, sbvo, iInOutFlag, j));
	}

	return gbvo;
}







/**
	 * �����ߣ�������
	 * ���ܣ�����¼�����
	 * ������e���ݱ༭�¼�
	 * ���أ�
	 * ���⣺
	 * ���ڣ�(2001-5-8 19:08:05)
	 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 *
	 *
	 *
	 *
	 */
public void afterInvEdit(Object value, int row) {
	//nc.vo.scm.pub.SCMEnv.out("inv chg");
	try {
		setTailValue(null);
		//����������������������,��ȥ����β��ʾ
		if ((value == null) || (value.toString().trim().length() == 0)) {
			clearRowData(row);
			//��β

		} else {
			//String sTempID1 =
				//((nc.ui.pub.beans.UIRefPane) getBillCardPanel()
					//.getBodyItem("cinventorycode")
					//.getComponent())
					//.getRefPK();
			String sTempID1=(String)value;
			String sTempID2 = null;
			if (getBillCardPanel().getHeadItem("coutwarehouseid") != null)
				sTempID2 = getBillCardPanel().getHeadItem("coutwarehouseid").getValue();
			ArrayList alIDs = new ArrayList();
			alIDs.add(sTempID2);
			alIDs.add(sTempID1);
			alIDs.add(m_sUserID);
			alIDs.add(m_sCorpID);

			//nc.vo.scm.ic.bill.InvVO voInv=
			//(InvVO) SpecialHBO_Client.queryInfo(new Integer(QryInfoConst.INV), alIDs);
			InvVO voInv =
				(InvVO) SpecialBillHelper.queryInfo(new Integer(QryInfoConst.INV), alIDs);
			m_voBill.setItemInv(row, voInv);
			//����
			setBodyInvValue(row, voInv);
			//��β
			setTailValue(row);
			//������
			String[] sIKs = getClearIDs(1, "cinventorycode");
			//���ƻ������Զ�����
			clearRowData(row, sIKs);
			//���µ���Ϣ��Ҫ�Ż���������κ�δ��ʾ����������ʾ��
			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill","UPP4008bill-000155")/*@res "����޸ģ�������ȷ����������Ρ�������"*/);
		}
	} catch (Exception e2) {
		nc.vo.scm.pub.SCMEnv.error(e2);
	}
}



/**
  * �����ߣ�������
  * ���ܣ��׼��������޸�,�������������������
  * ������e���ݱ༭�¼�
  * ���أ�
  * ���⣺
  * ���ڣ�(2001-5-8 19:08:05)
  * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
  *
  *
  *
  *
  */
public void afterNshldtransastnumEdit(nc.ui.pub.bill.BillEditEvent e) {
	int iRow= e.getRow();
	//dshldtransnum->nshldtransastnum*hsl
	UFDouble value= ZERO;
	UFDouble hslnow= ZERO;
	if (getBillCardPanel().getBodyValueAt(iRow, m_sAstItemKey) != null) {
		value=
			new UFDouble(getBillCardPanel().getBodyValueAt(iRow, m_sAstItemKey).toString());
	}
	if (getBillCardPanel().getBodyValueAt(iRow, "hsl") != null) {
		hslnow= new UFDouble(getBillCardPanel().getBodyValueAt(iRow, "hsl").toString());
	}
	//�׼����������������
	try {
		if ((null != m_voBill.getItemValue(iRow, "isAstUOMmgt"))
			&& (Integer
				.valueOf(m_voBill.getItemValue(iRow, "isAstUOMmgt").toString())
				.intValue()
				!= 0)
			&& ((null == m_voBill.getItemValue(iRow, "isSolidConvRate"))
				|| (Integer
					.valueOf(m_voBill.getItemValue(iRow, "isSolidConvRate").toString())
					.intValue()
					!= 0))) {
			//int col= getBillCardPanel().getBodyColByKey("nshldtransastnum");
			//col= getBillCardPanel().getBillTable().convertColumnIndexToView(col);
			//getBillCardPanel().getBillModel().execEditFormula(iRow, col);
			getBillCardPanel().setBodyValueAt(value.multiply(hslnow), iRow, m_sNumItemKey);
			if (m_voBill != null) {
				m_voBill.setItemValue(
					iRow,
					m_sNumItemKey,
					getBillCardPanel().getBodyValueAt(iRow, m_sNumItemKey));
			}
		}
		calculateByHsl(iRow, m_sNumItemKey, m_sAstItemKey,1);
		//for (int i= 1; i < m_voBill.getItemCount(); i++) {
			//if (m_voBill.getItemInv(i).getCinventoryid() != null) {
				//getBillCardPanel().setBodyValueAt(null, i, m_sAstItemKey);
				//calculateByHsl(i, m_sNumItemKey, m_sAstItemKey,0);
			//}
		//}

	} catch (Exception e2) {
		nc.vo.scm.pub.SCMEnv.error(e2);
	}

}

	/**
	 * �����ߣ����˾�
	 * ���ܣ��õ�������ʼ���ݣ����Ƶ��˵ȡ�
	 * ������
	 * ���أ�
	 * ���⣺
	 * ���ڣ�(2001-5-9 9:23:32)
	 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 *
	 *
	 *
	 *
	 */
	protected void getCEnvInfo() {
		try {
			nc.ui.pub.ClientEnvironment ce= nc.ui.pub.ClientEnvironment.getInstance();

			//��ǰʹ����ID
			try {
				m_sUserID= ce.getUser().getPrimaryKey();
			} catch (Exception e) {
				nc.vo.scm.pub.SCMEnv.out("test user id is 2011000001");
				m_sUserID= "2011000001";
			}

			//��ǰʹ��������
			try {
				m_sUserName= ce.getUser().getUserName();
			} catch (Exception e) {
				nc.vo.scm.pub.SCMEnv.out("test user name is ����");
				m_sUserName= nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill","UPP4008bill-000156")/*@res "����"*/;
			}

			//��˾ID
			try {
				m_sCorpID= ce.getCorporation().getPrimaryKey();
				nc.vo.scm.pub.SCMEnv.out("---->corp id is " + m_sCorpID);
			} catch (Exception e) {

			}
			//����
			try {
				if (ce.getDate() != null)
					m_sLogDate= ce.getDate().toString();
			} catch (Exception e) {

			}

		} catch (Exception e) {
		}
	}



	/**
	 * �����ߣ�������
	 * ���ܣ�������Ա
	 * ������
	 * ���أ�
	 * ���⣺
	 * ���ڣ�(2001-5-9 9:23:32)
	 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 *
	 *
	 *
	 *
	 */
	protected void clearAuditBillFlag() {
		try {
			//nc.ui.pub.ClientEnvironment ce= nc.ui.pub.ClientEnvironment.getInstance();
			//if (ce == null) {
			//nc.vo.scm.pub.SCMEnv.out("ce null ERR.");
			//return;
			//}
			//try{
			//getBillCardPanel().setHeadItem("dbilldate", m_sLogDate);
			//} catch (Exception e) {
			//}
			try {
				getBillCardPanel().setTailItem("cauditorid", "");
				getBillCardPanel().setTailItem("cauditorname", "");
				if (m_voBill != null) {
					m_voBill.setHeaderValue("cauditorid", "");
					m_voBill.setHeaderValue("cauditorname", "");
				}
			} catch (Exception e) {
			}

			//try {
			//getBillCardPanel().setHeadItem("pk_corp", m_sCorpID);
			//if (m_voBill != null)
			//m_voBill.setHeaderValue("pk_corp", m_sCorpID);
			//} catch (Exception e) {
			//}

		} catch (Exception e) {
		}

	}

	/**
	   * �����ߣ�������
	   * ���ܣ���ʾ������
	   * ������
	   * ���أ�
	   * ���⣺
	   * ���ڣ�(2001-5-16 ���� 6:32)
	   * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	   */
	protected void dispBodyRow(nc.ui.pub.beans.UITable mUITable) {
		//��ʾ������
		mUITable.clearSelection();
		m_iTotalListBodyNum= mUITable.getRowCount();
		if (m_iTotalListBodyNum <= 0) {
			return;
		}
		if (m_iLastSelCardBodyRow < 0) {
			m_iLastSelCardBodyRow= 0; //���ѡ�е��б������
		}
		if (m_iLastSelCardBodyRow > m_iTotalListBodyNum - 1) {
			m_iLastSelCardBodyRow= m_iTotalListBodyNum - 1;
		}
		if (m_iLastSelCardBodyRow < 0) {
			m_iLastSelCardBodyRow= 0; //���ѡ�е��б������
		}

		mUITable.setRowSelectionInterval(m_iLastSelCardBodyRow, m_iLastSelCardBodyRow);

		//��ʾ��β����
		//setTailValue(m_iLastSelCardBodyRow);

	}

	/**
	   * �����ߣ�������
	   * ���ܣ�����������
	   * ������
	   * ���أ�
	   * ���⣺
	   * ���ڣ�(2001-5-16 ���� 6:32)
	   * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	   */
	protected void filterMeas(int row) {

		nc.vo.scm.ic.bill.InvVO voInv= m_voBill.getItemInv(row);

		nc.ui.pub.beans.UIRefPane refCast=
			(nc.ui.pub.beans.UIRefPane) getBillCardPanel()
				.getBodyItem("castunitname")
				.getComponent();

		m_voInvMeas.filterMeas(m_sCorpID, voInv.getCinventoryid(), refCast);

		/*
				String tempID = "0>1";
				try {
					nc.vo.scm.ic.bill.InvVO voInv = m_voBill.getItemInv(row);
					if (voInv.getIsAstUOMmgt() != null
						&& voInv.getIsAstUOMmgt().toString().equals("1")) {
						ArrayList alRes = null;
						if (htCastUnit.containsKey(voInv.getCinventoryid())) {
							alRes = (ArrayList) htCastUnit.get(voInv.getCinventoryid());
						} else {
							alRes =
								((ArrayList) (GeneralHBO_Client
									.queryInfo(new Integer(999), voInv.getCinventoryid())));
							//"20110003")));//
						}

						if (alRes != null && alRes.size() > 0) {
							getBillCardPanel().getBillData().getBodyItem("castunitname").setEnabled(true);
							getBillCardPanel().getBillData().getBodyItem(m_sAstItemKey).setEnabled(true);

							ArrayList alUnit = (ArrayList) alRes.get(0);
							String sPK = (String) (alUnit.get(0));
							tempID = "pk_measdoc='" + sPK + "'";
							if (!htCastUnit.containsKey(voInv.getCinventoryid()))
								htCastUnit.put(voInv.getCinventoryid(), alRes);
							for (int i = 1; i < alRes.size(); i++) {
								alUnit = (ArrayList) alRes.get(i);
								sPK = (String) (alUnit.get(0));
								//	if(!htCastUnit.containsKey(sPK))
								//		htCastUnit.put(sPK,alUnit);
								tempID = tempID + " or pk_measdoc='" + sPK + "'";
							}
							(
								(nc.ui.pub.beans.UIRefPane) getBillCardPanel()
									.getBodyItem("castunitname")
									.getComponent())
									.setWhereString(
								tempID);
							return;
						} else {
							getBillCardPanel().getBillData().getBodyItem("castunitname").setEnabled(false);
							getBillCardPanel().getBillData().getBodyItem(m_sAstItemKey).setEnabled(false);

						}
					}

				} catch (Exception e) {
					showHintMessage("δ�鵽�ô���ĸ�����");
				}
		*/
	}

/**
 * �˴����뷽��˵����
 * �����ߣ�������
 * ���ܣ���ʼ����ͷ�����еĲ���
 * ������
 * ���أ�
 * ���⣺
 * ���ڣ�(2001-7-17 10:33:20)
 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
 *�޸��ˣ������� �޸����ڣ�2007-10-24����04:51:53 �޸�ԭ���˵��ֿ������ͳһ����
 */
public void filterRef(String cropid) {
	//String calRef = " and pk_calbody in (select pk_calbody from  bd_calbody  where (sealflag is null or sealflag ='N') and property in (0,1))";
	//��ͷ�Ƿ�Ʒ�ֿ�
/*	if (getBillCardPanel().getHeadItem("cwarehouseid") != null
		&& getBillCardPanel().getHeadItem("cwarehouseid").getComponent() != null) {
		(
			(nc.ui.pub.beans.UIRefPane) getBillCardPanel()
				.getHeadItem("cwarehouseid")
				.getComponent())
				.setWhereString(
			"gubflag='N' and sealflag='N' and pk_corp='" + cropid + "'"+calRef);
	}*/
	nc.ui.pub.bill.BillItem biwh = getBillCardPanel().getHeadItem(
	"cwarehouseid");
	RefFilter.filtSpecialBillWh(biwh, cropid,
	new String[] { "and gubflag='N' and sealflag='N' AND isdirectstore = 'N' " });


	//��ͷ��Ʒ�ֿ�
/*	if (getBillCardPanel().getHeadItem("cwastewarehouseid") != null
		&& getBillCardPanel().getHeadItem("cwastewarehouseid").getComponent() != null) {
		(
			(nc.ui.pub.beans.UIRefPane) getBillCardPanel()
				.getHeadItem("cwastewarehouseid")
				.getComponent())
				.setWhereString(
			"gubflag='Y' and sealflag='N' and pk_corp='" + cropid + "'"+calRef);
	}*/
	biwh = getBillCardPanel().getHeadItem(
	"cwastewarehouseid");
	RefFilter.filtSpecialBillWh(biwh, cropid,
	new String[] { "and gubflag='Y' and sealflag='N' AND isdirectstore = 'N' " });
	//��ͷ�Ƿ�Ʒout�ֿ�
/*	if (getBillCardPanel().getHeadItem("coutwarehouseid") != null
		&& getBillCardPanel().getHeadItem("coutwarehouseid").getComponent() != null) {
		

		(
			(nc.ui.pub.beans.UIRefPane) getBillCardPanel()
				.getHeadItem("coutwarehouseid")
				.getComponent())
				.setWhereString(
			"gubflag='N' and sealflag='N' and pk_corp='" + cropid + "'"+calRef);
	}*/
	biwh = getBillCardPanel().getHeadItem(
			"coutwarehouseid");
	RefFilter.filtSpecialBillWh(biwh, cropid,
			new String[] { "and gubflag='N' and sealflag='N' AND isdirectstore = 'N' " });
	
	
	//��ͷ�Ƿ�Ʒin�ֿ�
/*	if (getBillCardPanel().getHeadItem("cinwarehouseid") != null
		&& getBillCardPanel().getHeadItem("cinwarehouseid").getComponent() != null) {

		(
			(nc.ui.pub.beans.UIRefPane) getBillCardPanel()
				.getHeadItem("cinwarehouseid")
				.getComponent())
				.setWhereString(
			"gubflag='N' and sealflag='N' and pk_corp='" + cropid + "'"+calRef);
	}*/
	biwh = getBillCardPanel().getHeadItem(
	"cinwarehouseid");
	RefFilter.filtSpecialBillWh(biwh, cropid,
	new String[] { "and gubflag='N' and sealflag='N' AND isdirectstore = 'N' " });
	
	biwh = getBillCardPanel().getBodyItem(
	"cwarehousename");
	RefFilter.filtSpecialBillWh(biwh, cropid,
	new String[] { "and gubflag='N' and sealflag='N' and isdirectstore = 'N'  " });
	
	//��ͷ�ͻ�
	if (getBillCardPanel().getHeadItem("ccustomerid") != null
		&& getBillCardPanel().getHeadItem("ccustomerid").getComponent() != null) {

		UIRefPane ref =
			(UIRefPane) getBillCardPanel().getHeadItem("ccustomerid").getComponent();
		ref.getRefModel().setRefNameField("bd_cubasdoc.custshortname");

		ref.setWhereString(
			"(custflag ='0' or custflag ='2') and bd_cumandoc.pk_corp='" + cropid + "'");
	}
	//��ͷ��Ӧ��
	if (getBillCardPanel().getHeadItem("cproviderid") != null
		&& getBillCardPanel().getHeadItem("cproviderid").getComponent() != null) {

		UIRefPane ref =
			(UIRefPane) getBillCardPanel().getHeadItem("cproviderid").getComponent();
		ref.getRefModel().setRefNameField("bd_cubasdoc.custshortname");

		ref.setWhereString(
			"(custflag ='1' or custflag ='3') and bd_cumandoc.pk_corp='" + cropid + "'");
	}

	//���嵥λ������˴����Ϊ�Ƿ����,
	//if (getBillCardPanel().getBodyItem("cinventorycode") != null
	//&& getBillCardPanel().getBodyItem("cinventorycode").getComponent() != null) {
	//(
	//(nc.ui.pub.beans.UIRefPane) getBillCardPanel()
	//.getBodyItem("cinventorycode")
	//.getComponent())
	//.setWhereString(
	//"bd_invmandoc.sealflag ='N' and bd_invmandoc.pk_corp='" + cropid + "'");
	//}
	//���嵥λ������˴����Ϊ�Ƿ����,�������������ۿ۴��
	BillItem bi = getBillCardPanel().getBodyItem("cinventorycode");
	if (bi!= null&& bi.getComponent() != null) {

			//���˴������

		nc.ui.pub.beans.UIRefPane invRef = (nc.ui.pub.beans.UIRefPane) bi.getComponent();
		invRef.setTreeGridNodeMultiSelected(true);
 		invRef.setMultiSelectedEnabled(true);

			invRef.setWhereString(
			" bd_invbasdoc.discountflag='N' and bd_invbasdoc.laborflag='N'  "
				+ "and bd_invmandoc.sealflag ='N' and bd_invmandoc.pk_corp='"
				+ cropid
				+ "'");
	}

	//���帨���������������������Ӧ���д�����ˣ�ֻ��ʾ�ڴ�������н��й����㶨��ĸ�������ͬʱ�������ʴ�����

	//����Ƿ�Ʒ�ֿ�
/*	if (getBillCardPanel().getBodyItem("cwarehouseid") != null
		&& getBillCardPanel().getBodyItem("cwarehouseid").getComponent() != null) {
		(
			(nc.ui.pub.beans.UIRefPane) getBillCardPanel()
				.getBodyItem("cwarehouseid")
				.getComponent())
				.setWhereString(
			"gubflag='N' and sealflag='N' and pk_corp='" + cropid + "'"+calRef);
	}*/
	biwh = getBillCardPanel().getHeadItem(
	"cwarehouseid");
	RefFilter.filtSpecialBillWh(biwh, cropid,
	new String[] { "and gubflag='N' and sealflag='N' AND isdirectstore = 'N' " });
	//����Ƿ�Ʒ�ֿ�2
	//�ԣ������װ��������ж���������̬ת����
/*	if (getBillCardPanel().getBodyItem("cwarehousename") != null
		&& getBillCardPanel().getBodyItem("cwarehousename").getComponent() != null) {
		(
			(nc.ui.pub.beans.UIRefPane) getBillCardPanel()
				.getBodyItem("cwarehousename")
				.getComponent())
				.setWhereString(
			"gubflag='N' and sealflag='N' and pk_corp='" + cropid + "'"+calRef);
	}*/
	biwh = getBillCardPanel().getHeadItem(
	"cwarehousename");
	RefFilter.filtSpecialBillWh(biwh, cropid,
	new String[] { "and gubflag='N' and sealflag='N' AND isdirectstore = 'N' " });

}

	/**
	   * �����ߣ�������
	   * ���ܣ�ȡ���ִ�������
	   * ������
	   * ���أ�
	   * ���⣺
	   * ���ڣ�(2001-5-16 ���� 6:32)
	   * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	   */
	protected nc.ui.ic.pub.InvOnHandDialog getIohdDlg() {
		if (null == m_iohdDlg) {
			m_iohdDlg= new InvOnHandDialog(this);
		}
		return m_iohdDlg;
	}

/**
 * �˴����뷽��˵����
 * �������ڣ�(2001-6-27 10:34:52)
 * @return boolean
 */
public nc.vo.pub.lang.UFBoolean isFixFlag(int row) {

	if (row >= 0 && getM_voBill().getItemVOs()[row] != null
			&& getM_voBill().getItemVOs()[row].getIsSolidConvRate() != null
			&& getM_voBill().getItemVOs()[row].getIsSolidConvRate()
			.intValue() == 1)
		return UFBoolean.TRUE;
	else
		return UFBoolean.FALSE;
}

/**������ѯ
  * �޸�
  * �������ڣ�(2001-4-18 19:45:39)
  * �޸��ˣ������� �޸�ʱ�䣺2008-7-29 ����11:15:16 �޸�ԭ��ת�ⵥ֧��ѡ���������Ĺ���
  */
public void onRowQuyQty()
{
	SpecialBillVO nowVObill = null;
	int rownow = -1;
	if (m_iMode != BillMode.List)
	{
		rownow = getBillCardPanel().getBillTable().getSelectedRow();
		nowVObill = m_voBill;
	}
	else
	{
		rownow = getBillListPanel().getBodyTable().getSelectedRow();
		nowVObill = (SpecialBillVO) m_alListData.get(m_iLastSelListHeadRow);
	}
	if (rownow < 0)
	return;

	String WhID ="";
	String InvID ="";

	if ((nowVObill != null) && (rownow >= 0))
	{
		if (m_sBillTypeCode == null)
		return;
		if (m_sBillTypeCode.trim().equalsIgnoreCase("4L")||m_sBillTypeCode.trim().equalsIgnoreCase("4M")||m_sBillTypeCode.trim().equalsIgnoreCase("4N"))
		{
		   WhID = (String) nowVObill.getItemValue(rownow,"cwarehouseid");
		}
		else{
		WhID = (String) nowVObill.getHeaderValue("coutwarehouseid");
		}
		InvID = (String) nowVObill.getItemValue(rownow, "cinventoryid");

	}
	if (WhID==null||WhID.trim().equals("")||InvID==null||InvID.trim().equals("")){
	    showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill","UPP4008bill-000157")/*@res "û�еõ��ֿ�ID���ߴ��ID"*/);
	    return;
	}

	getIohdDlg().setParam(WhID, InvID);
	if (getIohdDlg().showModal() == nc.ui.pub.beans.UIDialog.ID_OK){
		nc.vo.ic.pub.InvOnHandVO[] selectVOs = getIohdDlg().m_SelectVOs;
		afterSelectOnhandVOs(selectVOs);
	}
}

/**
 * �����ˣ������� ����ʱ�䣺2008-7-29 ����11:17:20 ����ԭ�� ֧��ѡ���������Ĺ���
 * @param selectVOs
 */
public void afterSelectOnhandVOs(nc.vo.ic.pub.InvOnHandVO[] selectVOs){
	
}


/**
 * �����ߣ����˾�
 * ���ܣ������������ݵĳ�ʼ���ݣ������ڣ��Ƶ��˵ȡ�
 * ������
 * ���أ�
 * ���⣺
 * ���ڣ�(2001-5-9 9:23:32)
 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
 *
 *
 *
 *
 */
protected void setAuditBillFlag() {
	try {
		nc.ui.pub.ClientEnvironment ce= nc.ui.pub.ClientEnvironment.getInstance();
		if (ce == null) {
			nc.vo.scm.pub.SCMEnv.out("ce null ERR.");
			return;
		}
		//try{
		//getBillCardPanel().setHeadItem("dbilldate", m_sLogDate);
		//} catch (Exception e) {
		//}
		try {
			getBillCardPanel().setTailItem("cauditorid", m_sUserID);
			getBillCardPanel().setTailItem("cauditorname", m_sUserName);
			if (m_voBill != null) {
				m_voBill.setHeaderValue("cauditorid", m_sUserID);
				m_voBill.setHeaderValue("cauditorname", m_sUserName);
			}
		} catch (Exception e) {
		}

		//try {
		//getBillCardPanel().setHeadItem("pk_corp", m_sCorpID);
		//if (m_voBill != null)
		//m_voBill.setHeaderValue("pk_corp", m_sCorpID);
		//} catch (Exception e) {
		//}

	} catch (Exception e) {
	}

}

	/**�����ִ����Ի���
	 * �˴����뷽��˵����
	 * �������ڣ�(2001-7-11 ���� 11:19)
	 * @param newDlg nc.ui.ic.pub.InvOnHandDialog
	 */
	protected void setDlg(nc.ui.ic.pub.InvOnHandDialog newDlg) {
		m_iohdDlg= newDlg;
	}

	/**
	 * �����ߣ����˾�
	 * ���ܣ������������ݵĳ�ʼ���ݣ������ڣ��Ƶ��˵ȡ�
	 * ������
	 * ���أ�
	 * ���⣺
	 * ���ڣ�(2001-5-9 9:23:32)
	 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 *
	 *
	 *
	 *
	 */
	protected void setInOrOutBillFlag(int iFlag) {
		try {
			nc.ui.pub.ClientEnvironment ce= nc.ui.pub.ClientEnvironment.getInstance();
			if (ce == null) {
				nc.vo.scm.pub.SCMEnv.out("ce null ERR.");
				return;
			}
			//try{
			//getBillCardPanel().setHeadItem("dbilldate", m_sLogDate);
			//} catch (Exception e) {
			//}
			try {
				if (InOutFlag.OUT == iFlag) {
					getBillCardPanel().setTailItem("cauditorid", m_sUserID);
					getBillCardPanel().setTailItem("cauditorname", m_sUserName);
					if (m_voBill != null) {
						m_voBill.setHeaderValue("cauditorid", m_sUserID);
						m_voBill.setHeaderValue("cauditorname", m_sUserName);
					}
				} else if (InOutFlag.IN == iFlag) {
					getBillCardPanel().setTailItem("vadjuster", m_sUserID);
					getBillCardPanel().setTailItem("vadjustername", m_sUserName);
					if (m_voBill != null) {
						m_voBill.setHeaderValue("vadjuster", m_sUserID);
						m_voBill.setHeaderValue("vadjustername", m_sUserName);
					}
				}
			} catch (Exception e) {
			}

			//try {
			//getBillCardPanel().setHeadItem("pk_corp", m_sCorpID);
			//if (m_voBill != null)
			//m_voBill.setHeaderValue("pk_corp", m_sCorpID);
			//} catch (Exception e) {
			//}

		} catch (Exception e) {
		}

	}

/**
   * �����ߣ�������
   * ���ܣ��ɱ��л����б�
   * ������
   * ���أ�
   * ���⣺
   * ���ڣ�(2001-5-16 ���� 6:53)
   * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
   */
protected void switchBillToList() {
	try {
		getBillListPanel().getHeadBillModel().clearBodyData();
		getBillListPanel().getBodyBillModel().clearBodyData();

		if (m_iTotalListHeadNum < 1) {
			getBillListPanel().getHeadTable().clearSelection();
      if(m_iTotalListHeadNum>0)
        getBillListPanel().getHeadTable().setRowSelectionInterval(0, 0);
			//dispBodyRow(getBillListPanel().getBodyTable());
			return;
		}

		if (m_iLastSelListHeadRow < 0)
			m_iLastSelListHeadRow = 0; //���ѡ�е��б��ͷ��
		SpecialBillVO voTemp = (SpecialBillVO) m_alListData.get(m_iLastSelListHeadRow);

		if (voTemp == null)
			return;

		//�õ���ǰ��body
		SpecialBillItemVO[] voItems = voTemp.getItemVOs();
		//�����������Ϊ�գ���֮��
		if (voItems == null || voItems.length < 1) {
			qryItems(
				new int[] { m_iLastSelListHeadRow },
				new String[] { voTemp.getPrimaryKey()});
		}
		//re-get after query.....
		voTemp = (SpecialBillVO) m_alListData.get(m_iLastSelListHeadRow);
		voItems = (SpecialBillItemVO[]) voTemp.getChildrenVO();

		//�����ͷ
		getBillListPanel().getHeadBillModel().setSortColumn(null);
		getBillListPanel().getHeadBillModel().setBodyDataVO(getListHeaderVOs());
		//��������
		getBillListPanel().getHeadTable().clearSelection();
		getBillListPanel().getHeadTable().setRowSelectionInterval(
			m_iLastSelListHeadRow,
			m_iLastSelListHeadRow);
	
		getBillListPanel().getBodyBillModel().setBodyDataVO(voItems);
		
//		getBillListPanel().getBodyBillModel().execLoadFormula();//zhyע�͵�,���������ѯ������д�������������ʾ������
		//modified by lirr 2008-01-12
		//getBillListPanel().getBodyBillModel().execLoadFormula();
		//getBillListPanel().getHeadBillModel().execLoadFormula();
		dispBodyRow(getBillListPanel().getBodyTable());
		
		
//		 ���Ʒ�ҳ��ť��״̬��
		setPageBtnStatus(0,0);

	} catch (Exception e1) {
		nc.vo.scm.pub.SCMEnv.out("����ͨѶʧ�ܣ�");
		handleException(e1);
	}
}

/**
   * �����ߣ�������
   * ���ܣ���listת��billʱ���Զ���list��ѡ�����ת��bill��
   * ������
   * ���أ�
   * ���⣺
   * ���ڣ�(2001-5-16 ���� 6:32)
   * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
   */
protected void switchListToBill() {
	//����Ӹղŵ��б���ѡ�������
	if (m_alListData != null
		&& m_alListData.size() > 0
		&& m_iLastSelListHeadRow >= 0
		&& m_iLastSelListHeadRow < m_alListData.size()) {
		m_voBill = null;
		m_voBill =(SpecialBillVO)((SpecialBillVO) m_alListData.get(m_iLastSelListHeadRow)).clone();
		getBillCardPanel().addNew();
		
		SpecialBillItemVO[] voaItem = m_voBill.getItemVOs();
		if (voaItem == null || voaItem.length < 1) {
			qryItems(
				new int[] { m_iLastSelListHeadRow },
				new String[] { m_voBill.getPrimaryKey()});
		}
		//re-get
		//m_voBill =(SpecialBillVO) ((SpecialBillVO) (m_alListData.get(m_iLastSelListHeadRow))).clone();
		voaItem = (SpecialBillItemVO[]) m_voBill.getChildrenVO();
		qryCalbodyByWhid(m_voBill.getHeaderVO());
		m_alListData.set(m_iLastSelListHeadRow,m_voBill);
    m_voBill = (SpecialBillVO)m_voBill.clone();
		setBillValueVO(m_voBill);
		//��Ƭִ�й�ʽ����
		//getBillCardPanel().getBillModel().execLoadFormula();
		//getBillCardPanel().execHeadTailLoadFormulas();
		
		//dispBodyRow(getBillCardPanel().getBillTable());

		getBillCardPanel().updateValue();
		
		if (null != m_alListData)
		setPageBtnStatus(m_alListData.size(), m_iLastSelListHeadRow);
		
	} else {
		getBillCardPanel().getBillData().clearViewData();
		getBillCardPanel().updateValue();
		//fullScreen(getBillCardPanel().getBillModel(), m_iFirstAddRows);
	}
}

/**
 * �����ߣ����˾�
 * ���ܣ����ݱ༭�¼�����
 * ������e���ݱ༭�¼�
 * ���أ�
 * ���⣺
 * ���ڣ�(2001-5-8 19:08:05)
 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
 *
 *
 *
 *
 */
public void afterAstUOMEdit(int rownow) {
	//��������λ
	String sAstUomPK =
		((nc.ui.pub.beans.UIRefPane) getBillCardPanel()
			.getBodyItem("castunitname")
			.getComponent())
			.getRefPK();
	String sAstUomname =
		((nc.ui.pub.beans.UIRefPane) getBillCardPanel()
			.getBodyItem("castunitname")
			.getComponent())
			.getRefName();
	m_voBill.setItemValue(rownow, "castunitid", sAstUomPK);
	m_voBill.setItemValue(rownow, "castunitname", sAstUomname);
	getBillCardPanel().setBodyValueAt(sAstUomPK, rownow, "castunitid");
	getBillCardPanel().setBodyValueAt(sAstUomname, rownow, "castunitname");

	//�������������б���ʽ����ʾ��
	try {

		nc.vo.bd.b15.MeasureRateVO voMeas =
			m_voInvMeas.getMeasureRate(
				m_voBill.getItemInv(rownow).getCinventoryid(),
				sAstUomPK);
		if (voMeas != null) {
			UFDouble hsl = voMeas.getMainmeasrate();
			getBillCardPanel().setBodyValueAt(hsl, rownow, "hsl");
			getBillCardPanel().updateUI();
			//getBillCardPanel().setBodyValueAt(null, e.getRow(), m_sAstItemKey);
			//m_voBill.setItemValue(rownow, m_sAstItemKey, null);
			//m_voBill.setItemValue(rownow, "hsl", hsl);
			hsl = (UFDouble) getBillCardPanel().getBodyValueAt(rownow, "hsl");
			m_voBill.setItemValue(rownow, "hsl", hsl);
			m_voBill.setItemValue(rownow, "isSolidConvRate", voMeas.getFixedflag());
		}
		calculateByHsl(rownow, "dshldtransnum", "nshldtransastnum", 1);
    
//  ����������λΪ��ʱ,��ջ������븨����
    if(null == sAstUomPK && null == sAstUomname){
      m_voBill.setItemValue(rownow, "hsl", null);
      m_voBill.setItemValue(rownow, "nshldtransastnum", null);
      getBillCardPanel().setBodyValueAt(null, rownow, "hsl");
      getBillCardPanel().setBodyValueAt(null, rownow, "nshldtransastnum");
    }

	} catch (Exception e2) {
		nc.vo.scm.pub.SCMEnv.error(e2);
	}
}


public void afterVendorEdit(int rownow) {
	//��Ӧ��
	String sVendorPK =
		((nc.ui.pub.beans.UIRefPane) getBillCardPanel()
			.getBodyItem("cvendorname")
			.getComponent())
			.getRefPK();
	String sVendorName =
		((nc.ui.pub.beans.UIRefPane) getBillCardPanel()
			.getBodyItem("cvendorname")
			.getComponent())
			.getRefName();
//	sVendorName=(String)getBillCardPanel().getBodyValueAt(rownow,"cvendorname");
	m_voBill.setItemValue(rownow, "cvendorid", sVendorPK);
	m_voBill.setItemValue(rownow, "cvendorname", sVendorName);
	getBillCardPanel().setBodyValueAt(sVendorPK, rownow, "cvendorid");
	getBillCardPanel().setBodyValueAt(sVendorName, rownow, "cvendorname");

}

public void afterDefEdit(nc.ui.pub.bill.BillEditEvent e){
	//�Զ������zhy
	if (e.getPos() == 0) {//��ͷ
		String sVdefPkKey = "pk_defdoc"
				+ e.getKey().substring("vuserdef".length());
        //lj ����Զ�������ֵ���򲻵���
//		    if (m_voBill.getHeaderValue(sVdefPkKey)==null) {
		 DefSetTool.afterEditHead(getBillCardPanel().getBillData(),
		        e.getKey(), sVdefPkKey);
		 //ͬ��m_voBill
		 m_voBill.setHeaderValue(e.getKey(), getBillCardPanel()

				.getHeadItem(e.getKey()).getValue());
		 m_voBill.setHeaderValue(sVdefPkKey, getBillCardPanel()
				.getHeadItem(sVdefPkKey).getValue());
//		    }
	} else if (e.getPos() == 1) {//����
		String sVdefPkKey = "pk_defdoc"
				+ e.getKey().substring("vuserdef".length());

//			if (m_voBill.getHeaderValue(sVdefPkKey) == null) {

		DefSetTool.afterEditBody(getBillCardPanel().getBillModel(),
				e.getRow(), e.getKey(), sVdefPkKey);

		//ͬ��m_voBill
		m_voBill.setItemValue(e.getRow(), e.getKey(), getBillCardPanel()
				.getBodyValueAt(e.getRow(), e.getKey()));
		m_voBill.setItemValue(e.getRow(), sVdefPkKey, getBillCardPanel()
				.getBodyValueAt(e.getRow(), sVdefPkKey));
//			}
	}
	
}

/**
 * �����ߣ�������
 * ���ܣ�
 * ������
 * ���أ�
 * ���⣺
 * ���ڣ�(2001-7-25 ���� 10:50)
 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
 * 
 */
public void afterValidateEdit(nc.ui.pub.bill.BillEditEvent e) {
	
	int iSelrow= e.getRow();
//	if ((null != getBillCardPanel().getBodyValueAt(iSelrow, e.getKey()))
//		&& (getBillCardPanel()
//			.getBodyValueAt(iSelrow, e.getKey())
//			.toString()
//			.trim()
//			.length()
//			!= 0)) {
//		//����������
//		//�޸��ˣ������� �޸����ڣ�2007-9-4����04:49:40 �޸�ԭ���������ڡ�ʧЧ���ڶ���ֵʱ��������������ʧЧ���ڣ���ʧЧ���ڲ�Ҫȥ����������(ͨ��������
//		if (null == getBillCardPanel().getBodyValueAt(iSelrow, "scrq")){
//			int days=
//				(m_voBill.getItemInv(iSelrow).getQualityDay() == null
//					? 0
//					: m_voBill.getItemInv(iSelrow).getQualityDay().intValue());
//			UFDate validateDate=
//				new UFDate(getBillCardPanel().getBodyValueAt(iSelrow, e.getKey()).toString());
//			UFDate productDate= validateDate.getDateBefore(days);
//			getBillCardPanel().setBodyValueAt(productDate, iSelrow, "scrq");
//		}
//	} else {
//		//����������
//		getBillCardPanel().setBodyValueAt("", iSelrow, "scrq");
//	}
	m_voBill.setItemValue(
		iSelrow,
		"dvalidate",
		getBillCardPanel().getBodyValueAt(iSelrow, "dvalidate"));
//	m_voBill.setItemValue(
//		iSelrow,
//		"scrq",
//		getBillCardPanel().getBodyValueAt(iSelrow, "scrq"));
}

/**
 * �����ߣ�������
 * ���ܣ��ɴ���ĵ������͡��ֶΣ���õ����ֶθı��Ӧ�ı�������ֶ��б�
 * ������iBillFlag �������ͣ���Ϊ��ͨ���ݣ�����0����Ϊ���ⵥ�ݣ�����1
 				����
 				���					cinventorycode��
 				����ֿ�			cwarehousename��
 				������				vfree0��
 				��ͷ����ֿ�	coutwarehouseid��
 				��ͷ�ֿ�			cwarehouseid
 * ���أ�
 * ���⣺
 * ���ڣ�(2001-7-18 ���� 9:20)
 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
 * @return java.lang.String[]
 * @param sWhatChange java.lang.String
 */
protected String[] getClearIDs(int iBillFlag, String sWhatChange) {
	if (sWhatChange == null)
		return null;
	String[] sReturnString = null;
	sWhatChange = sWhatChange.trim();
	if (sWhatChange.equals("cinventorycode")) {
		//���
		sReturnString = new String[6];
		sReturnString[0] = "vbatchcode";
		sReturnString[1] = "vfree0";
		sReturnString[2] = m_sNumItemKey;
		sReturnString[3] = m_sAstItemKey;
		//sReturnString[4]= "castunitid";
		//sReturnString[5]= "castunitname";
		//sReturnString[6]= "hsl";
		sReturnString[4] = "scrq";
		sReturnString[5] = "dvalidate";
	} else if ((sWhatChange.equals("cwarehousename")) && (iBillFlag == 1)) {
		//���ⵥ�ı������ڲֿ�
		sReturnString = new String[6];
		sReturnString[0] = "vbatchcode";
		sReturnString[2] = m_sNumItemKey;
		sReturnString[3] = m_sAstItemKey;
		sReturnString[4] = "scrq";
		sReturnString[5] = "dvalidate";
		//showWarningMessage("������ȷ�����κţ�");
		return null;
	} else if (sWhatChange.equals("vfree0")) {
		//������
//		sReturnString = new String[3];
//		sReturnString[0] = "vbatchcode";
//		sReturnString[1] = "scrq";
//		sReturnString[2] = "dvalidate";
		//showWarningMessage("������ȷ�����κţ�");
		return null;
	} else if (sWhatChange.equals("coutwarehouseid")) {
		sReturnString = new String[5];
		sReturnString[0] = "vbatchcode";
		sReturnString[1] = m_sNumItemKey;
		sReturnString[2] = m_sAstItemKey;
		sReturnString[3] = "scrq";
		sReturnString[4] = "dvalidate";
		//showWarningMessage("������ȷ�����κţ�");
		return null;
	} else if ((sWhatChange.equals("cwarehouseid")) && (iBillFlag == 0)) {
		sReturnString = new String[5];
		sReturnString[0] = "vbatchcode";
		sReturnString[1] = m_sNumItemKey;
		sReturnString[2] = m_sAstItemKey;
		sReturnString[3] = "scrq";
		sReturnString[4] = "dvalidate";
		//showWarningMessage("������ȷ�����κţ�");
		return null;
	}
	return sReturnString;
}

private BillFormulaContainer m_formulaParse = null;

protected BillFormulaContainer getFormulaContainer() {
	if (m_formulaParse == null) {
		m_formulaParse = new BillFormulaContainer(getBillListPanel());
	}
	return m_formulaParse;
}

public static CircularlyAccessibleValueObject[] getBillBodyData(ArrayList alData) {
	ArrayList alBody = new ArrayList();
	for (int i = 0; i < alData.size(); i++) {
		AggregatedValueObject vo = (AggregatedValueObject) alData.get(i);
		CircularlyAccessibleValueObject[] itemVos = (CircularlyAccessibleValueObject[]) vo.getChildrenVO();
		if (itemVos != null && itemVos.length > 0) {			
			for (int j = 0; j < itemVos.length; j++) {
					
					alBody.add(itemVos[j]);
				}
			}

		}
	return (CircularlyAccessibleValueObject[])alBody.toArray(new CircularlyAccessibleValueObject[alBody.size()]);

}
/**
   * �����ߣ�������
   * ���ܣ�����BillListPanel�е����� �൱�ڲ�ѯ
   * ������
   * ���أ�
   * ���⣺
   * ���ڣ�(2001-5-16 ���� 1:34)
   * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
   */
protected void loadBillListPanel(QryConditionVO qcvo) {
	try {

		m_alListData =
			(ArrayList) SpecialBillHelper.queryBills(m_sBillTypeCode, qcvo);
        
		if (m_alListData != null && m_alListData.size() > 0) {

			m_iTotalListHeadNum = m_alListData.size(); //�б��ͷĿǰ���ڵ�����

			m_iLastSelListHeadRow = 0; //���ѡ�е��б��ͷ��

			//�޸ĸ�����calConvRate
			SpecialBillVO sbvotemp =
				(SpecialBillVO) m_alListData.get(m_iLastSelListHeadRow);
			sbvotemp.calConvRate();
			m_alListData.set(m_iLastSelListHeadRow, sbvotemp);

			//v5:exec forumla
			getFormulaBillContainer().formulaBill(m_alListData,getFormulaItemHeader(),getFormulaItemBody());
			//ִ�����κŵ�����ʽ
			BatchCodeDefSetTool.execFormulaForBatchCode(getBillBodyData(m_alListData));
      
			switchBillToList();

		} else {
			dealNoData();
			switchBillToList();
		}
	m_iMode = BillMode.List;
	} catch (Exception e) {
		nc.vo.scm.pub.SCMEnv.out("����ͨѶʧ�ܣ�");
		showErrorMessage(e.getMessage());
		handleException(e);
	}

}

/**
   * �����ߣ�������
   * ���ܣ��������״̬�·��������޸�ʱִ�б���
   * ������
   * ���أ�
   * ���⣺
   * ���ڣ�(2001-5-16 ���� 6:32)
   * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
   */
protected void reLoadBill() {
	try {
		m_iMode = BillMode.Browse;
		//����µı�
		QryConditionVO qcvo = new nc.vo.ic.pub.bill.QryConditionVO();
		qcvo.setQryCond(
			"cbilltypecode='"
				+ m_sBillTypeCode
				+ "' and cspecialhid='"
				+ m_voBill.getPrimaryKey().trim()
				+ "'");
		m_voBill =
			(SpecialBillVO)
				(
					(ArrayList) SpecialBillHelper.queryBills(m_sBillTypeCode,qcvo )).get(
				0);
		if (m_voBill != null) {
			m_alListData.set(m_iLastSelListHeadRow, m_voBill.clone());
			switchListToBill();
		}
	} catch (Exception e) {
		handleException(e);
	}
}

/**
 * �����ߣ�������
 * ���ܣ�
 * ������
 * ���أ�
 * ���⣺
 * ���ڣ�(2001-7-19 ���� 10:51)
 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
 * @param bShowFlag boolean
 */
public void setBodyMenuShow(boolean bShowFlag) {
	getBillCardPanel().setBodyMenuShow(bShowFlag);
}

	//���ݱ��幫ʽ
	ArrayList m_alFormulBodyItem;
	//���ݱ�ͷ��ʽ
	ArrayList m_alFormulHeadItem;
	//�б��е�ArrayList�͵�HOV��������HVO����ɾ����
	protected ArrayList m_alListData = new ArrayList();
	//���ݹ�ʽ����
	BillFormulaContainer m_billFormulaContain;
	//����
	protected ButtonObject m_boJointCheck;
	protected ButtonObject m_boPreview;
	//��ʼ����ӡ�ӿ�
	protected PrintDataInterface m_dataSource;
	private DefVO[] m_defBody=null;
	private DefVO[] m_defHead=null;
	protected nc.ui.ic.pub.orient.OrientDialog m_dlgOrient;
	//�ֿ���Ϣ���档
	private Hashtable m_htWh = new Hashtable();
	//С�����ȶ���--->
	//����С��λ			2
	//����������С��λ	2
	//������				2
	//����ɱ�����С��λ	2
	protected int m_iaScale[] = new int[] { 2, 2, 2, 2, 2 };
	//��ʽ������Ҫ�����ȫ�ֱ��� by hanwei 2003-06-26

	private InvoInfoBYFormula m_InvoInfoBYFormula;
	boolean m_isLocated=false;
	//�Ƿ��ѯ�ƻ��۸��ڴ�����ն�ѡ����£�
	public boolean m_isQuryPlanprice = true;
	protected nc.ui.pub.print.PrintEntry m_print;
	protected ButtonObject m_PrintMng;
	//zhx 030626 �����к�
	//hanwei 2003-10-09
	public static final String m_sBillRowNo = "crowno";
	//�ֿ�PK�ֶ�
	public String m_sMainWhItemKey = "coutwarehouseid";
	//��¼�ɵĵ��ݺ�
	protected String m_sOldBillCode = "";
	protected String m_sPNodeCode;
	//���õĸ����ڵ��BO_Client�ӿ�,Ӧ�������޸�
	//protected String sSpecialHBO_Client;
	//ֻ��ʼ��һ�Σ�����õ���UFDouble.
	final protected UFDouble ZERO = new UFDouble("0.0");

/**
	 * �����ߣ�������
	 * ���ܣ�����SpecialHVO,��StationNumberλ��
	 * ������
	 * ���أ�
	 * ���⣺
	 * ���ڣ�(2001-5-16 ���� 7:22)
	 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * @param m_iStationNumber int
	 */
protected void addBillVO() {
	//m_shvoBillSpecialHVO
	if (m_alListData == null)
		m_alListData = new ArrayList();

	m_alListData.add(m_iLastSelListHeadRow, m_voBill.clone());

	m_iTotalListHeadNum = m_alListData.size();

	if (m_iLastSelListHeadRow < 0)
		m_iLastSelListHeadRow = 0; //���ѡ�е��б��ͷ��

}


/**
 * �����ߣ�������
 * ���ܣ��������޸�
 * ������
 * ���أ�
 * ���⣺
 * ���ڣ�(2001-11-20 14:01:52)
 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
 * @param row int
 */
public void afterHslEdit(int iRow) {
	if (getBillCardPanel().getBodyValueAt(iRow, "hsl") == null
		|| getBillCardPanel().getBodyValueAt(iRow, "hsl").toString().trim().length() == 0) {
		getBillCardPanel().setBodyValueAt(ZERO, iRow, "hsl");
	}
	UFDouble hsl= (UFDouble) getBillCardPanel().getBodyValueAt(iRow, "hsl");
	m_voBill.setItemValue(iRow, "hsl", hsl);

	if (m_voBill.getItemInv(iRow).getCinventoryid() != null) {
		calculateByHsl(iRow, "dshldtransnum", "nshldtransastnum", 2);
	}
}
protected void afterBsorEdit(String[] bsor ,String[] dept) {
	// ҵ��Ա
	if(bsor==null||dept==null)
		return;
	UIRefPane ref=(nc.ui.pub.beans.UIRefPane) getBillCardPanel().getHeadItem(bsor[0]).getComponent();
	String sName = ref.getRefName();
	String sPK = ref.getRefPK();
	// ��Ҫ����ҵ��Ա�Զ���������
//	�޸��ˣ������� �޸�ʱ�䣺2008-8-25 ����10:59:04 �޸�ԭ�򣺲�����ҵ��Ա�Զ���������
/*	String sDeptPK = null;
	String sDeptName = null;
	if (sPK != null && sPK.trim().length() > 0) {
		try {
			Object o=CacheTool.getCellValue("bd_psndoc", "pk_psndoc", "pk_deptdoc", sPK);
			if(o!=null)
				sDeptPK =(String) ((Object[])o)[0];
		} catch (Exception ex) {
			nc.vo.scm.pub.SCMEnv.error(ex);
		}
		BillItem itDpt = getBillCardPanel().getHeadItem(dept[0]);
		if (itDpt != null) {
			((nc.ui.pub.beans.UIRefPane) itDpt.getComponent())
					.setPK(sDeptPK);
			// ����
			sDeptName = ((nc.ui.pub.beans.UIRefPane) itDpt
					.getComponent()).getRefName();


		}

	}*/
//	 �������������б���ʽ����ʾ��
	if (m_voBill != null) {
		m_voBill.setHeaderValue(bsor[1], sName);
		//m_voBill.setHeaderValue(dept[1], sDeptName);
	}
}

/**
 * �����ߣ����˾�
 * ���ܣ�����¼�����
 * ������e���ݱ༭�¼�
 * ���أ�
 * ���⣺
 * ���ڣ�(2001-5-8 19:08:05)
 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
 *
 */
public void afterInvMutiEdit(nc.ui.pub.bill.BillEditEvent e){


	long ITimeAll = System.currentTimeMillis();

	int row = e.getRow();
	//�ֶ�itemkey
	String sItemKey = e.getKey();

	nc.ui.pub.beans.UIRefPane invRef =
		(nc.ui.pub.beans.UIRefPane) getBillCardPanel()
			.getBodyItem("cinventorycode")
			.getComponent();
	//������PK
	String[] refPks = invRef.getRefPKs();
  if(isLineCardEdit() && refPks.length>1){
    refPks = new String[]{refPks[0]};
  }
	
	//�������Ϊ�գ���յ�ǰ����
	if (refPks == null || refPks.length == 0) {
		clearRowData(row);
		return;
	}
	invRef.setPK(null);

	afterInvMutiEdit(refPks,row);
	SCMEnv.showTime(ITimeAll, "������ն�ѡ:");
	
}
/**
 * �����ˣ������� ����ʱ�䣺2008-7-29 ����01:42:08 ����ԭ�� �������pks��Ȼ�������Ӵ����
 * @param refPks
 * @param row
 */
public void afterInvMutiEdit(String[] refPks,Integer row){
	try{
		//�ֿ�Ϳ����֯��Ϣ
		String sWhID = null;
		String sCalID = null;
		if (getBillCardPanel().getHeadItem(m_sMainWhItemKey) != null) {
			sWhID = getBillCardPanel().getHeadItem(m_sMainWhItemKey).getValue();
		}
		long ITime = System.currentTimeMillis();
    if(sWhID==null && row!=null && row.intValue()>=0)
      sWhID = (String)getBillCardPanel().getBodyValueAt(row.intValue(), "cwarehouseid");
	
		if (isQuryPlanprice())
		{
		if (sCalID == null && sWhID != null) {
			
		    sCalID  = (String) ((Object[]) nc.ui.scm.pub.CacheTool.getCellValue(
					"bd_stordoc",
					"pk_stordoc",
					"pk_calbody",
					sWhID))[0];
			
		}
		}
	
		SCMEnv.showTime(ITime, "���������������:");
	
		ITime = System.currentTimeMillis();
		//�������
		InvVO[] invVOs = null;
		if (isQuryPlanprice())
		    invVOs = getInvoInfoBYFormula().getInvParseWithPlanPrice(refPks, sWhID, sCalID,true,true);
		 else
		    invVOs =getInvoInfoBYFormula().getBillQuryInvVOs(refPks,true,true);
	
		SCMEnv.showTime(ITime, "�������:");
	
	
		ITime = System.currentTimeMillis();
		
		afterInvMutiEdit(invVOs,row);

	}catch(Exception e1){
		showErrorMessage(e1.getMessage());
	
	}
}

/**
 * �����ˣ������� ����ʱ�䣺2008-7-29 ����01:42:08 ����ԭ�� �������InvVOs��Ȼ�������Ӵ����
 * @param refPks
 * @param row
 */
public void afterInvMutiEdit(InvVO[] invVOs,Integer row){/*
	//��������
	boolean isLastRow=false;
	
	long ITime = System.currentTimeMillis();

	if (invVOs != null && invVOs.length > 1) {
		if(row==getBillCardPanel().getRowCount()-1)
			isLastRow=true;

		for (int i = invVOs.length - 1; i >= 0; i--) {
			if (i < invVOs.length - 1) {
				getBillCardPanel().insertLine();
			}else{
		 		if(getBillCardPanel().getBillModel().getRowState(row)==BillModel.NORMAL)
		 			getBillCardPanel().getBillModel().setRowState(row,BillModel.MODIFICATION);
		 			 		
	 		}
		}
		if(isLastRow){

			nc.ui.scm.pub.report.BillRowNo.addLineRowNos(getBillCardPanel(),
			m_sBillTypeCode,
			m_sBillRowNo,

			invVOs.length);

			}

	//�����к�
	else{ nc.ui.scm.pub.report.BillRowNo.insertLineRowNos(
			getBillCardPanel(),
			m_sBillTypeCode,
			m_sBillRowNo,
			row + invVOs.length - 1,
			invVOs.length - 1);
	}
	}
	ITime = System.currentTimeMillis();

	setBodyInVO(invVOs,row);
	
	setTailValue(row);
	SCMEnv.showTime(ITime, "���ý�������:");
	//���µ���Ϣ��Ҫ�Ż���������κ�δ��ʾ����������ʾ��
	showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill","UPP4008bill-000155")@res "����޸ģ�������ȷ����������Ρ�������");

	SCMEnv.showTime(ITime, "�����������:");
*/
	afterInvMutiEdit(invVOs,row,false);
}

/**
 * �����ߣ�������
 * ���ܣ�������������ʧЧ����
 * ������
 * ���أ�
 * ���⣺
 * ���ڣ�(2001-7-25 ���� 10:50)
 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
 */
public void afterProducedateEdit(nc.ui.pub.bill.BillEditEvent e) {
	int iSelrow= e.getRow();
	if ((null != getBillCardPanel().getBodyValueAt(iSelrow, e.getKey()))
		&& (getBillCardPanel()
			.getBodyValueAt(iSelrow, e.getKey())
			.toString()
			.trim()
			.length()
			!= 0)) {
    
      InvVO voInv = m_voBill.getItemInv(iSelrow);
      if(voInv!=null && voInv.getIsValidateMgt() == null
          || voInv.getIsValidateMgt().intValue()!= 1){
    		//��ʧЧ����
//    		int days=
//    			(m_voBill.getItemInv(iSelrow).getQualityDay() == null
//    				? 0
//    				: m_voBill.getItemInv(iSelrow).getQualityDay().intValue());
    		UFDate productDate=
    			new UFDate(getBillCardPanel().getBodyValueAt(iSelrow, e.getKey()).toString());
    		UFDate validateDate= InvVO.calcQualityDate(
            productDate, voInv.getQualityperiodunit(), voInv.getQualityDay());
    		getBillCardPanel().setBodyValueAt(validateDate, iSelrow, "dvalidate");
      }else{
//      ��ʧЧ����
        getBillCardPanel().setBodyValueAt(null, iSelrow, "dvalidate");
      }
      
	} else {
		//��ʧЧ����
		getBillCardPanel().setBodyValueAt(null, iSelrow, "dvalidate");
	}
	m_voBill.setItemValue(
		iSelrow,
		"dvalidate",
		getBillCardPanel().getBodyValueAt(iSelrow, "dvalidate"));
	m_voBill.setItemValue(
		iSelrow,
		"scrq",
		getBillCardPanel().getBodyValueAt(iSelrow, "scrq"));
}


/**
 * �༭ǰ����
 * �������ڣ�(2001-3-23 2:02:27)
 * @param e ufbill.BillEditEvent
 */
public boolean beforeEdit(nc.ui.pub.bill.BillEditEvent e) {
	/*
	nc.vo.scm.pub.ctrl.GenMsgCtrl.printHint("beforeEdit:" + e.getKey());

	getBillCardPanel().stopEditing();
	boolean isEditable = true;
	String sItemKey = e.getKey();
	nc.ui.pub.bill.BillItem biCol = getBillCardPanel().getBodyItem(sItemKey);
	int iRow = e.getRow();
	int iPos = e.getPos();

	if (sItemKey == null || biCol == null)
		return false;

	//ģ������
	if (!biCol.isEdit()) {

		return false;
	}

	if (m_voBill == null) {
		biCol.setEnabled(false);
		return false;
	}


	//����㲻Ϊ�ֿ����ʱ���������޴�����ֹ��������ֵ
	if (sItemKey.equals("cinventorycode") || sItemKey.equals("cwarehousename")){
		biCol.setEnabled(true);
		return true;
	}else{
		if(null == m_voBill.getItemValue(iRow, "cinventoryid")
			|| m_voBill.getItemValue(iRow, "cinventoryid").toString().trim().length() == 0) {
		biCol.setEnabled(false);
		return false;
		}
	}




	InvVO voInv = m_voBill.getItemInv(iRow);


	//������
	if (sItemKey.equals("castunitname")
		|| sItemKey.equals("hsl")
		||sItemKey.equals("nshldtransastnum")
		||sItemKey.equals("nadjustastnum")
		||sItemKey.equals("ncheckastnum")
		||sItemKey.equals("cyfsl") ) {
		if (voInv.getIsAstUOMmgt() == null
			|| voInv.getIsAstUOMmgt().intValue() != 1) {
			isEditable = false;
		}
		//���˸���λ
		else {
			if (sItemKey.equals("castunitname"))
				filterMeas(iRow);
			//�̶������ʲ��ɱ༭
			else if (
				sItemKey.equals("hsl")
					&& m_voBill.getItemValue(iRow, "isSolidConvRate") != null
					&& ((Integer) m_voBill.getItemValue(iRow, "isSolidConvRate")).intValue() == 1) {
				isEditable = false;
			}

		}
	}
	//������
	else if (sItemKey.equals("vfree0")) {
		if (voInv.getIsFreeItemMgt() == null
			|| voInv.getIsFreeItemMgt().intValue() != 1) {
			isEditable = false;
		}
		//�������������
		else {
			//����������մ�������
			getFreeItemRefPane().setFreeItemParam(voInv);

		}
	}

	//����
	else if (sItemKey.equals("vbatchcode")) {
		if (voInv.getIsLotMgt() == null || voInv.getIsLotMgt().intValue() != 1) {

			isEditable = false;
		}
	}
	else if (sItemKey.equals("dvalidate") || sItemKey.equals("scrq")) {

		if (voInv.getIsValidateMgt() == null
			|| voInv.getIsValidateMgt().intValue()!= 1)


			isEditable = false;
	}



	biCol.setEnabled(isEditable);
	return isEditable;
	*/
	return true;
}

/**
 * �����ߣ�������
 * ���ܣ������������������㸨����
	 ��ʽ��������=������*������
 * ������ iWhichChanged:
 							0 �������޸�
 							1 �������޸�
 * ���أ�
 * ���⣺
 * ���ڣ�(2001-10-15 14:13:55)
 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
 * @param MainNum java.lang.String
 * @param AstNum java.lang.String
 */
protected void calculateByHsl(
	int iRow,
	String sMainNum,
	String sAstNum,
	int iWhichChanged) {
	//int col= getBillCardPanel().getBodyColByKey(sMainNum);
	//col= getBillCardPanel().getBillTable().convertColumnIndexToView(col);
	//getBillCardPanel().getBillModel().execEditFormula(iRow, col);
	Object temphsl= getBillCardPanel().getBodyValueAt(iRow, "hsl");
	if (temphsl == null || temphsl.toString().trim().length() == 0) {
		//�޻�����
		/*
		if (m_voBill.getItemValue(iRow, "isAstUOMmgt") != null
			&& m_voBill.getItemValue(iRow, "isAstUOMmgt").toString().trim().length() != 0) {
			//�Ǹ���������
			getBillCardPanel().setBodyValueAt(null, iRow, sMainNum);
			m_voBill.setItemValue(iRow, sMainNum, null);
			m_voBill.setItemValue(
				iRow,
				sAstNum,
				getBillCardPanel().getBodyValueAt(iRow, sAstNum));
		} else {
			//�Ǹ���������
			m_voBill.setItemValue(
				iRow,
				sMainNum,
				getBillCardPanel().getBodyValueAt(iRow, sMainNum));
			getBillCardPanel().setBodyValueAt(null, iRow, sAstNum);
			m_voBill.setItemValue(iRow, sAstNum, null);
		}
		*/
		temphsl= ZERO;
	} else {
		//UFDouble hsl= (UFDouble) getBillCardPanel().getBodyValueAt(iRow, "hsl");
		UFDouble hsl= (UFDouble) temphsl;
		if (hsl.doubleValue() <= 0) {
			//������С����
			if (m_voBill.getItemValue(iRow, "isAstUOMmgt") != null
				&& m_voBill.getItemValue(iRow, "isAstUOMmgt").toString().trim().length() != 0) {
				if ((null != m_voBill.getItemValue(iRow, "fbillrowflag"))
					&& (Integer
						.valueOf(m_voBill.getItemValue(iRow, "fbillrowflag").toString())
						.intValue()
						== BillRowType.part)) {
					//�������
					//�Ǹ���������,�帨�������廻���ʣ�����������
					getBillCardPanel().setBodyValueAt(null, iRow, sAstNum);
					m_voBill.setItemValue(iRow, sAstNum, null);
					m_voBill.setItemValue(
						iRow,
						sMainNum,
						getBillCardPanel().getBodyValueAt(iRow, sMainNum));
					getBillCardPanel().setBodyValueAt(null, iRow, "hsl");
					m_voBill.setItemValue(iRow, "hsl", null);
				} else {
					//�Ǹ���������,�����������廻���ʣ����븨����
					getBillCardPanel().setBodyValueAt(null, iRow, sMainNum);
					m_voBill.setItemValue(iRow, sMainNum, null);
					m_voBill.setItemValue(
						iRow,
						sAstNum,
						getBillCardPanel().getBodyValueAt(iRow, sAstNum));
					getBillCardPanel().setBodyValueAt(null, iRow, "hsl");
					m_voBill.setItemValue(iRow, "hsl", null);
				}
			} else {
				//�Ǹ���������,�帨�������廻���ʣ�����������
				m_voBill.setItemValue(
					iRow,
					sMainNum,
					getBillCardPanel().getBodyValueAt(iRow, sMainNum));
				getBillCardPanel().setBodyValueAt(null, iRow, sAstNum);
				getBillCardPanel().setBodyValueAt(null, iRow, "hsl");
				m_voBill.setItemValue(iRow, sAstNum, null);
				m_voBill.setItemValue(iRow, "hsl", null);
			}
			//} else if (hsl.doubleValue() == 0) {
			//�����ʵ�����
			//getBillCardPanel().setBodyValueAt(ZERO, iRow, sMainNum);
			//m_voBill.setItemValue(iRow, sMainNum, ZERO);
			//m_voBill.setItemValue(
			//iRow,
			//sAstNum,
			//getBillCardPanel().getBodyValueAt(iRow, sAstNum));
		} else {
			//�����ʴ�����
			UFDouble ufdMainNum= null;
			UFDouble ufdAstNum= null;
			if ((null != getBillCardPanel().getBodyValueAt(iRow, sMainNum))
				&& (getBillCardPanel().getBodyValueAt(iRow, sMainNum).toString().trim().length()
					!= 0))
				ufdMainNum= (UFDouble) getBillCardPanel().getBodyValueAt(iRow, sMainNum);
			if ((null != getBillCardPanel().getBodyValueAt(iRow, sAstNum))
				&& (getBillCardPanel().getBodyValueAt(iRow, sAstNum).toString().trim().length()
					!= 0))
				ufdAstNum= (UFDouble) getBillCardPanel().getBodyValueAt(iRow, sAstNum);
			if (isFixFlag(iRow).booleanValue()) {
				//�ǹ̶�������
				if (iWhichChanged == 1) {
					//�Ǹ���������
					if ((null != m_voBill.getItemValue(iRow, "fbillrowflag"))
						&& (Integer
							.valueOf(m_voBill.getItemValue(iRow, "fbillrowflag").toString())
							.intValue()
							== BillRowType.part)) {
						//�������
						if ((getBillCardPanel().getBodyValueAt(iRow, sMainNum) != null)
							&& (getBillCardPanel().getBodyValueAt(iRow, sMainNum).toString().trim().length()
								!= 0)
							&& (((UFDouble) getBillCardPanel().getBodyValueAt(iRow, sMainNum)).doubleValue()
								!= 0)) {
							ufdAstNum= ufdMainNum.div(hsl);
							getBillCardPanel().setBodyValueAt(ufdAstNum, iRow, sAstNum);
							m_voBill.setItemValue(
								iRow,
								sAstNum,
								getBillCardPanel().getBodyValueAt(iRow, sAstNum));
							m_voBill.setItemValue(iRow, sMainNum, ufdMainNum);
						} else if (
							(getBillCardPanel().getBodyValueAt(iRow, sMainNum) != null)
								&& (getBillCardPanel().getBodyValueAt(iRow, sMainNum).toString().trim().length()
									!= 0)
								&& (((UFDouble) getBillCardPanel().getBodyValueAt(iRow, sMainNum)).doubleValue()
									== 0)) {
							getBillCardPanel().setBodyValueAt(ZERO, iRow, sAstNum);
							m_voBill.setItemValue(iRow, sMainNum, ZERO);
							m_voBill.setItemValue(iRow, sAstNum, ZERO);
						} else {
							getBillCardPanel().setBodyValueAt(null, iRow, sAstNum);
							m_voBill.setItemValue(iRow, sMainNum, null);
							m_voBill.setItemValue(iRow, sAstNum, null);
						}
					} else {
						//���׼��л�������
						if ((getBillCardPanel().getBodyValueAt(iRow, sAstNum) != null)
							&& (getBillCardPanel().getBodyValueAt(iRow, sAstNum).toString().trim().length()
								!= 0)
							&& (((UFDouble) getBillCardPanel().getBodyValueAt(iRow, sAstNum)).doubleValue()
								!= 0)) {
							ufdAstNum= ufdMainNum.div(hsl);
							getBillCardPanel().setBodyValueAt(ufdAstNum, iRow, sAstNum);
							//m_voBill.setItemValue(iRow, sMainNum, ufdMainNum);
							m_voBill.setItemValue(
								iRow,
								sAstNum,
								getBillCardPanel().getBodyValueAt(iRow, sAstNum));
							m_voBill.setItemValue(iRow, sMainNum, ufdMainNum);
						} else if (
							(getBillCardPanel().getBodyValueAt(iRow, sAstNum) != null)
								&& (getBillCardPanel().getBodyValueAt(iRow, sAstNum).toString().trim().length()
									!= 0)
								&& (((UFDouble) getBillCardPanel().getBodyValueAt(iRow, sAstNum)).doubleValue()
									== 0)) {
							getBillCardPanel().setBodyValueAt(ZERO, iRow, sMainNum);
							m_voBill.setItemValue(iRow, sMainNum, ZERO);
							m_voBill.setItemValue(iRow, sAstNum, ZERO);
						} else {
							getBillCardPanel().setBodyValueAt(null, iRow, sMainNum);
							m_voBill.setItemValue(iRow, sMainNum, null);
							m_voBill.setItemValue(iRow, sAstNum, null);
						}
					}
				} else
					//if (
					//!getBillCardPanel().getBodyValueAt(iRow, sMainNum).equals(
					//m_voBill.getItemValue(iRow, sMainNum)))
					{
					//�����������Ļ�����������
					if ((getBillCardPanel().getBodyValueAt(iRow, sMainNum) != null)
						&& (getBillCardPanel().getBodyValueAt(iRow, sMainNum).toString().trim().length()
							!= 0)
						&& (((UFDouble) getBillCardPanel().getBodyValueAt(iRow, sMainNum)).doubleValue()
							!= 0)) {
						ufdAstNum= ufdMainNum.div(hsl);
						getBillCardPanel().setBodyValueAt(ufdAstNum, iRow, sAstNum);
						m_voBill.setItemValue(iRow, sMainNum, ufdMainNum);
						//m_voBill.setItemValue(iRow, sAstNum, ufdAstNum);
						m_voBill.setItemValue(
							iRow,
							sAstNum,
							getBillCardPanel().getBodyValueAt(iRow, sAstNum));
					} else if (
						(getBillCardPanel().getBodyValueAt(iRow, sMainNum) != null)
							&& (getBillCardPanel().getBodyValueAt(iRow, sMainNum).toString().trim().length()
								!= 0)
							&& (((UFDouble) getBillCardPanel().getBodyValueAt(iRow, sMainNum)).doubleValue()
								== 0)) {
						getBillCardPanel().setBodyValueAt(ZERO, iRow, sAstNum);
						m_voBill.setItemValue(iRow, sMainNum, ZERO);
						m_voBill.setItemValue(iRow, sAstNum, ZERO);
					} else {
						getBillCardPanel().setBodyValueAt(null, iRow, sAstNum);
						m_voBill.setItemValue(iRow, sMainNum, null);
						m_voBill.setItemValue(iRow, sAstNum, null);
					}
				}
			} else { //�ǹ̶�������
				if ((null != m_voBill.getItemValue(iRow, "fbillrowflag"))
					&& (Integer
						.valueOf(m_voBill.getItemValue(iRow, "fbillrowflag").toString())
						.intValue()
						== BillRowType.part)) {
					iWhichChanged= 1;
				}
				if (((null == ufdMainNum) || (ufdMainNum.doubleValue() == 0))
					&& ((null == ufdAstNum) || (ufdAstNum.doubleValue() == 0))) { //��Ϊ�ջ���
					//do nothing
				} else if ((iWhichChanged == 1)) {
					//�������޸�
					if ((null != m_voBill.getItemValue(iRow, "fbillrowflag"))
						&& (Integer
							.valueOf(m_voBill.getItemValue(iRow, "fbillrowflag").toString())
							.intValue()
							== BillRowType.part)) {
						//�������
						if ((null == ufdMainNum) || (ufdMainNum.doubleValue() == 0)) {
							getBillCardPanel().setBodyValueAt(null, iRow, sAstNum);
							getBillCardPanel().setBodyValueAt(null, iRow, sMainNum);
						} else {
							ufdAstNum= ufdMainNum.div(hsl);
							getBillCardPanel().setBodyValueAt(ufdAstNum, iRow, sAstNum);
						}
					} else {
						//���׼��л�������
						if (ufdAstNum != null) {
							ufdAstNum= ufdMainNum.div(hsl);
							getBillCardPanel().setBodyValueAt(ufdAstNum, iRow, sAstNum);
						}else if(ufdAstNum==null && ufdMainNum!=null && hsl!=null){
              ufdAstNum= ufdMainNum.div(hsl);
              getBillCardPanel().setBodyValueAt(ufdAstNum, iRow, sAstNum);
            }
					}
				}else if ((iWhichChanged == 2)) {
					//�������޸�
					if ((null == ufdMainNum) || (ufdMainNum.doubleValue() == 0)) {
						getBillCardPanel().setBodyValueAt(null, iRow, sAstNum);
						getBillCardPanel().setBodyValueAt(null, iRow, sMainNum);
					} else {
						ufdAstNum= ufdMainNum.div(hsl);
						getBillCardPanel().setBodyValueAt(ufdAstNum, iRow, sAstNum);
					}
				} else if (
					(iWhichChanged == 0)
						&& ((null == ufdAstNum) || (ufdAstNum.doubleValue() == 0))) {
					//������Ϊ�ջ���,�������޸�
					if(hsl!=null&&ufdMainNum!=null){
						ufdAstNum=ufdMainNum.div(hsl);
						getBillCardPanel().setBodyValueAt(ufdAstNum, iRow, sAstNum);
					
					}

//					getBillCardPanel().setBodyValueAt(null, iRow, sMainNum);
//					m_voBill.setItemValue(iRow, sMainNum, null);
//					m_voBill.setItemValue(
//						iRow,
//						sAstNum,
//						getBillCardPanel().getBodyValueAt(iRow, sAstNum));
				} else if (
					(iWhichChanged == 0)
						&& !((null == ufdAstNum) || (ufdAstNum.doubleValue() == 0))) {
					//��������Ϊ�ջ��㣬�������޸ģ���ʱ�޸Ļ�����
					//�޸��ˣ������� �޸����ڣ�2007-11-1����10:20:45 �޸�ԭ�򣺻�������ԶΪ��
					if ((0 > ufdMainNum.doubleValue() && 0 < ufdAstNum.doubleValue())
							||(0 < ufdMainNum.doubleValue() && 0 > ufdAstNum.doubleValue())){
						ufdAstNum = ufdAstNum.div(new UFDouble(-1));
						getBillCardPanel().setBodyValueAt(ufdAstNum, iRow, sAstNum);
					}
					
					hsl= ufdMainNum.div(ufdAstNum);
					getBillCardPanel().setBodyValueAt(hsl, iRow, "hsl");
					m_voBill.setItemValue(
						iRow,
						"hsl",
						getBillCardPanel().getBodyValueAt(iRow, "hsl"));
				}
				m_voBill.setItemValue(
					iRow,
					sMainNum,
					getBillCardPanel().getBodyValueAt(iRow, sMainNum));
				m_voBill.setItemValue(
					iRow,
					sAstNum,
					getBillCardPanel().getBodyValueAt(iRow, sAstNum));
			}
		}
	}

	getBillCardPanel().getBillModel().execEditFormulaByKey(iRow, sMainNum);
}

/**
 * �����ߣ�������
 * ���ܣ�
 * ������
 * ���أ�
 * ���⣺
 * ���ڣ�(2001-11-8 19:47:29)
 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
 * @return nc.ui.pub.bill.BillData
 * @param oldBillData nc.ui.pub.bill.BillData
 */
protected BillData changeBillDataByUserDef(BillData oldBillData) {
	//�����Զ��������
	DefVO[] defs = null;
	//��ͷ
	//��ö�Ӧ�ڹ�˾�ĸõ��ݵ��Զ���������
	defs = getDefHeadVO();
	if ((defs != null)) {

		oldBillData.updateItemByDef(defs, "vuserdef", true);
	}

	//����
	//��ö�Ӧ�ڹ�˾�ĸõ��ݵ��Զ���������
	defs = getDefBodyVO();
	if ((defs == null)) {
		return oldBillData;
	}
	oldBillData.updateItemByDef(defs, "vuserdef", false);
	return oldBillData;
}

/**
 * �����ߣ�������
 * ���ܣ�
 * ������
 * ���أ�
 * ���⣺
 * ���ڣ�(2001-11-8 19:47:29)
 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
 * @return nc.ui.pub.bill.BillData
 * @param oldBillData nc.ui.pub.bill.BillData
 */
protected BillListData changeBillListDataByUserDef(BillListData oldBillData) {
	//�����Զ��������
	DefVO[] defs = null;
	//��ͷ
	//��ö�Ӧ�ڹ�˾�ĸõ��ݵ��Զ���������
	defs = getDefHeadVO();
	if ((defs != null)) {
		oldBillData.updateItemByDef(defs, "vuserdef", true);
	}

	//����
	//��ö�Ӧ�ڹ�˾�ĸõ��ݵ��Զ���������
	defs = getDefBodyVO();
	if ((defs == null)) {
		return oldBillData;
	}
	oldBillData.updateItemByDef(defs, "vuserdef", false);
	return oldBillData;
}

/**
 * �����ߣ�������

 * ���ܣ�VO ת��

 * ������
 * ���أ�
 * ���⣺
 * ���ڣ�(2001-8-16 12:53:03)
 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
 * @param gbvo nc.vo.ic.pub.bill.GeneralBillVO
 * @param sbvo nc.vo.ic.pub.bill.SpecialBillVO
 * @param iInOutFlag int
 */
protected GeneralBillHeaderVO changeFromSpecialVOtoGeneralVOAboutHeader(
	GeneralBillVO gbvo,
	SpecialBillVO sbvo,
	int iInOutFlag) {
	String[] sHeaderItemKeyName = gbvo.getHeaderVO().getAttributeNames();
	for (int i = 0; i < sHeaderItemKeyName.length; i++) {
		if (sbvo.getHeaderValue(sHeaderItemKeyName[i]) != null) {
			gbvo.setHeaderValue(
				sHeaderItemKeyName[i],
				sbvo.getHeaderValue(sHeaderItemKeyName[i]));
		}
		if (sHeaderItemKeyName[i].trim().equals("cbilltypecode")) {
			if (iInOutFlag == InOutFlag.OUT)
				gbvo.setHeaderValue(sHeaderItemKeyName[i], BillTypeConst.m_otherOut);
			else
				gbvo.setHeaderValue(sHeaderItemKeyName[i], BillTypeConst.m_otherIn);
		} else if (sHeaderItemKeyName[i].trim().equals("cgeneralhid")) { //���ͷ����PK
			gbvo.setHeaderValue(sHeaderItemKeyName[i], "");
		} else if (sHeaderItemKeyName[i].trim().equals("vbillcode")) { //������ݺ�
			gbvo.setHeaderValue(sHeaderItemKeyName[i], "");
		} else if (sHeaderItemKeyName[i].trim().equals("coperatorid")) { //����Ա
			gbvo.setHeaderValue(sHeaderItemKeyName[i], m_sUserID);
		} else if (sHeaderItemKeyName[i].trim().equals("coperatorname")) { //����Ա
			gbvo.setHeaderValue(sHeaderItemKeyName[i], m_sUserName);
		} else if (sHeaderItemKeyName[i].trim().equals("dbilldate")) { //����Ա
			gbvo.setHeaderValue(sHeaderItemKeyName[i], m_sLogDate);
		} else if (
			sHeaderItemKeyName[i].trim().equals("cauditorid")) { //�����ˣ���ͨ�������ڴ�����
			gbvo.setHeaderValue(sHeaderItemKeyName[i], null);
		} else if (
			sHeaderItemKeyName[i].trim().equals("cauditorname")) { //�����ˣ���ͨ�������ڴ�����
			gbvo.setHeaderValue(sHeaderItemKeyName[i], null);
		} else if (sHeaderItemKeyName[i].trim().equals("vadjuster")) { //�����ˣ���ͨ�������ڴ�����
			gbvo.setHeaderValue(sHeaderItemKeyName[i], null);
		} else if (
			sHeaderItemKeyName[i].trim().equals("vadjustername")) { //�����ˣ���ͨ�������ڴ�����
			gbvo.setHeaderValue(sHeaderItemKeyName[i], null);
		}
		if (iInOutFlag == InOutFlag.OUT) {
			if (sHeaderItemKeyName[i].trim().equals("pk_calbody_out")) {
				gbvo.setHeaderValue("pk_calbody", sbvo.getHeaderValue("pk_calbody_out"));
			}
		} else {
			if (sHeaderItemKeyName[i].trim().equals("pk_calbody_in")) {
				gbvo.setHeaderValue("pk_calbody", sbvo.getHeaderValue("pk_calbody_in"));
			}
		}

	}
	//��ǰ����Ա
	gbvo.getHeaderVO().setCoperatorid(m_sUserID);
	gbvo.getHeaderVO().setCoperatoridnow(m_sUserID);
	gbvo.getHeaderVO().setPrimaryKey(null);
	//�ֹ�ǿ�Ʊ�ͷTS = null zhx 20030528
	gbvo.getHeaderVO().setTs(null);
	//ǩ����
	//gbvo.getHeaderVO().setCregister(m_sUserID);
	//���Բ��ǵ�ǰ��¼��λ�ĵ��ݣ����Բ���Ҫ�޸ĵ�λ��
	//voHead.setPk_corp(m_sCorpID);
	//��Ϊ��¼���ں͵��������ǿ��Բ�ͬ�ģ����Ա���Ҫ��¼���ڡ�
	//gbvo.getHeaderVO().setDaccountdate(new nc.vo.pub.lang.UFDate(m_sLogDate));
	//vo����Ҫ����ƽ̨������Ҫ���ɺ�ǩ�ֺ�ĵ���
	//gbvo.getHeaderVO().setFbillflag(
	//new Integer(nc.vo.ic.pub.bill.BillStatus.SIGNED));
	//end zhx 20030528
	//cwhsmanagerid
	//cwhsmanagername
	//cdptid coutdeptid  cindeptid
	//cdptname coutdeptname  cindeptname
	//cbizid coutbsor  cinbsrid
	//cbizname coutbsorname  cinbsrname
	//cproviderid
	//cprovidername
	//ccustomerid
	//ccustomername
	gbvo.getParentVO().setStatus(nc.vo.pub.VOStatus.NEW);
	return (GeneralBillHeaderVO) gbvo.getParentVO();
}

/**
 * �����ߣ�������
 * ���ܣ�
 * ������
 * ���أ�
 * ���⣺
 * ���ڣ�(2001-8-16 12:53:03)
 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
 * @param gbvo nc.vo.ic.pub.bill.GeneralBillVO
 * @param sbvo nc.vo.ic.pub.bill.SpecialBillVO
 * @param iInOutFlag int
 */
protected GeneralBillItemVO changeFromSpecialVOtoGeneralVOAboutItem(
	GeneralBillVO gbvo,
	SpecialBillVO sbvo,
	int iInOutFlag,
	int j) {
	 String sBilltypecode=null;
	 String sBilltypeName=null;
	 sBilltypecode=(String)sbvo.getHeaderValue("cbilltypecode");
	if (sBilltypecode != null) {
		nc.vo.ic.pub.billtype.IBillType billType = nc.vo.ic.pub.billtype.BillTypeFactory
					.getInstance().getBillType(sBilltypecode);
		if (billType.typeOf(nc.vo.ic.pub.billtype.ModuleCode.IC))
			sBilltypeName = billType.getBillTypeName();
	}


	String[] sBodyItemKeyName = gbvo.getChildrenVO()[0].getAttributeNames();
	for (int i = 0; i < sBodyItemKeyName.length; i++) {
		if (sbvo.getItemValue(j, sBodyItemKeyName[i]) != null) {
			gbvo.setItemValue(j, sBodyItemKeyName[i], sbvo.getItemValue(j, sBodyItemKeyName[i]));
		}
		if (sBodyItemKeyName[i].equalsIgnoreCase("vnotebody"))
		    gbvo.setItemValue(j, "vnotebody", sbvo.getItemValue(j, "vnote"));
		//ע�����ⵥSpecialItemVO����Ľ�cinvmanid��Ϊ��������ID���������
		if (sBodyItemKeyName[i].equalsIgnoreCase("cinvbasid"))
		    gbvo.setItemValue(j,"cinvbasid",sbvo.getItemValue(j,"cinvmanid"));

		//������ֵ
		UFDouble ufdTotal =
			(sbvo.getItemValue(j, "dshldtransnum") == null ? ZERO : (UFDouble) sbvo.getItemValue(j, "dshldtransnum"));
		//����ֵ
		UFDouble ufdAlreadyIn =
			(sbvo.getItemValue(j, "nadjustnum") == null ? ZERO : (UFDouble) sbvo.getItemValue(j, "nadjustnum"));
		//�۳�ֵ
		UFDouble ufdAlreadyOut =
			(sbvo.getItemValue(j, "nchecknum") == null ? ZERO : (UFDouble) sbvo.getItemValue(j, "nchecknum"));
		//������
		UFDouble ufdHsl =
			(sbvo.getItemValue(j, "hsl") == null
				|| sbvo.getItemValue(j, "hsl").toString().trim().length() == 0 ? ZERO : (UFDouble) sbvo.getItemValue(j, "hsl"));

		if (sBodyItemKeyName[i].trim().equals("nshouldinnum")) {
			if (iInOutFlag == InOutFlag.OUT) {
			} else {
				//gbvo.setItemValue(j, sBodyItemKeyName[i], ufdTotal.sub(ufdAlreadyIn));
				//�����������Ǵ�������:ת��,�������������Ϊ��;����(�ۼƳ�-�ۼ���)
				//2.30 wnj comments ������ʵ�������ĸ�ֵ
				//gbvo.setItemValue(j, sBodyItemKeyName[i], ufdAlreadyOut.sub(ufdAlreadyIn));

			}
		} else if (sBodyItemKeyName[i].trim().equals("nneedinassistnum")) {
			if (iInOutFlag == InOutFlag.OUT) {
			} else {
				if (sbvo.getItemValue(j, "nshldtransastnum") != null
					&& sbvo.getItemValue(j, "nshldtransastnum").toString().trim().length() != 0
					&& ufdHsl.doubleValue() != 0) {
					//gbvo.setItemValue(j, sBodyItemKeyName[i], ufdTotal.sub(ufdAlreadyIn).div(ufdHsl));
					//�����������Ǵ�������:ת��,�������������Ϊ��;����(�ۼƳ�-�ۼ���)
					//2.30 wnj comments ������ʵ�������ĸ�ֵ
					//gbvo.setItemValue(
					//j,
					//sBodyItemKeyName[i],
					//ufdAlreadyOut.sub(ufdAlreadyIn).div(ufdHsl));

				}
			}
		} else if (sBodyItemKeyName[i].trim().equals("ninnum")) {
			if (iInOutFlag == InOutFlag.OUT) {
			} else {
				//gbvo.setItemValue(j, sBodyItemKeyName[i], ufdTotal.sub(ufdAlreadyIn));
				//�����������Ǵ�������:ת��,�������������Ϊ��;����(�ۼƳ�-�ۼ���)
				//2.30 wnj comments ������ʵ�������ĸ�ֵ
				//gbvo.setItemValue(j, sBodyItemKeyName[i], ufdAlreadyOut.sub(ufdAlreadyIn));
			}
		} else if (sBodyItemKeyName[i].trim().equals("ninassistnum")) {
			if (iInOutFlag == InOutFlag.OUT) {
			} else {
				if (sbvo.getItemValue(j, "nshldtransastnum") != null
					&& sbvo.getItemValue(j, "nshldtransastnum").toString().trim().length() != 0
					&& ufdHsl.doubleValue() != 0) {

					//gbvo.setItemValue(j, sBodyItemKeyName[i], ufdTotal.sub(ufdAlreadyIn).div(ufdHsl));
					//�����������Ǵ�������:ת��,�������������Ϊ��;����(�ۼƳ�-�ۼ���)
					//2.30 wnj comments ������ʵ�������ĸ�ֵ
					//gbvo.setItemValue(
					//j,
					//sBodyItemKeyName[i],
					//ufdAlreadyOut.sub(ufdAlreadyIn).div(ufdHsl));
				}
			}
		} else if (sBodyItemKeyName[i].trim().equals("nshouldoutnum")) {
			if (iInOutFlag == InOutFlag.OUT) {
				//2.30 wnj comments ������ʵ�������ĸ�ֵ
				//gbvo.setItemValue(j, sBodyItemKeyName[i], ufdTotal.sub(ufdAlreadyOut));
			} else {
			}
		} else if (sBodyItemKeyName[i].trim().equals("nshouldoutassistnum")) {
			//2.30 wnj comments ������ʵ�������ĸ�ֵ
			if (iInOutFlag == InOutFlag.OUT) {
				//if (sbvo.getItemValue(j, "nshldtransastnum") != null
				//&& sbvo.getItemValue(j, "nshldtransastnum").toString().trim().length() != 0
				//&& ufdHsl.doubleValue() != 0) {
				//gbvo.setItemValue(
				//j,
				//sBodyItemKeyName[i],
				//ufdTotal.sub(ufdAlreadyOut).div(ufdHsl));
				//}
			} else {
			}
		} else if (sBodyItemKeyName[i].trim().equals("noutnum")) {
			if (iInOutFlag == InOutFlag.OUT) {
				//2.30 wnj comments ������ʵ�������ĸ�ֵ
				//gbvo.setItemValue(j, sBodyItemKeyName[i], ufdTotal.sub(ufdAlreadyOut));
			} else {
			}
		} else if (sBodyItemKeyName[i].trim().equals("noutassistnum")) {
			if (iInOutFlag == InOutFlag.OUT) {
				//2.30 wnj comments ������ʵ�������ĸ�ֵ
				//if (sbvo.getItemValue(j, "nshldtransastnum") != null
				//&& sbvo.getItemValue(j, "nshldtransastnum").toString().trim().length() != 0
				//&& ufdHsl.doubleValue() != 0) {
				//gbvo.setItemValue(
				//j,
				//sBodyItemKeyName[i],
				//ufdTotal.sub(ufdAlreadyOut).div(ufdHsl));
				//}
			} else {
			}
		} else if (sBodyItemKeyName[i].trim().equals("nmny")) {
			gbvo.setItemValue(j, sBodyItemKeyName[i], sbvo.getItemValue(j, "je"));
		} else if (sBodyItemKeyName[i].trim().equals("nplannedprice")) {
			gbvo.setItemValue(j, sBodyItemKeyName[i], sbvo.getItemValue(j, "jhdj"));
		} else if (sBodyItemKeyName[i].trim().equals("nplannedmny")) {
			gbvo.setItemValue(j, sBodyItemKeyName[i], sbvo.getItemValue(j, "jhje"));
		} else if (sBodyItemKeyName[i].trim().equals("csourcetype")) {
			gbvo.setItemValue(j, sBodyItemKeyName[i],  sbvo.getHeaderValue("cbilltypecode"));
		} else if (sBodyItemKeyName[i].trim().equals("vsourcebillcode")) {
			gbvo.setItemValue(j, sBodyItemKeyName[i], sbvo.getHeaderValue("vbillcode"));
		}
		 else if (sBodyItemKeyName[i].trim().equals("vsourcerowno")) {
			gbvo.setItemValue(j, sBodyItemKeyName[i], sbvo.getItemValue(j,"crowno"));
		}
		 else if (sBodyItemKeyName[i].trim().equals("csourcetypename")) {
			 if (sBilltypeName!=null)
			 gbvo.setItemValue(j, sBodyItemKeyName[i], sBilltypeName);
		}

		else if (sBodyItemKeyName[i].trim().equals("csourcebillhid")) {
			gbvo.setItemValue(j, sBodyItemKeyName[i], sbvo.getHeaderValue("cspecialhid"));
		//}else if (sBodyItemKeyName[i].trim().equals("csourceheadts")) {
			//gbvo.setItemValue(j, sBodyItemKeyName[i], sbvo.getHeaderValue("ts"));
		} else if (sBodyItemKeyName[i].trim().equals("csourcebillbid")) {
			gbvo.setItemValue(j, sBodyItemKeyName[i], sbvo.getItemValue(j, "cspecialbid"));
		//} else if (sBodyItemKeyName[i].trim().equals("csourcebodyts")) {
			//gbvo.setItemValue(j, sBodyItemKeyName[i], sbvo.getItemValue(j, "ts"));
		} else if (sBodyItemKeyName[i].trim().equals("dbizdate")) { //������������
			gbvo.setItemValue(j, sBodyItemKeyName[i], m_sLogDate);
		}

		gbvo.setItemValue(j, "isprimarybarcode", sbvo.getItemValue(j, "isprimarybarcode"));

		gbvo.setItemValue(j, "issecondarybarcode", sbvo.getItemValue(j, "issecondarybarcode"));
	}
	gbvo.setItemValue(j, "cgeneralbid", null);
	gbvo.setItemValue(j, "cgeneralhid", null);
	//�ֹ�ǿ�Ʊ���TS = null zhx 20030528
	gbvo.setItemValue(j, "ts", null);
	//nshouldinnum  ninnum
	//nneedinassistnum  ninassistnum
	//nshouldoutnum  noutnum
	//nshouldoutassistnum  noutassistnum
	//dshldtransnum  nshldtransastnum
	gbvo.getChildrenVO()[j].setStatus(nc.vo.pub.VOStatus.NEW);
	return (GeneralBillItemVO) gbvo.getItemVOs()[j];
}

/**
�����λ��ɫ
*/
public void clearOrientColor() {
	if ( m_isLocated) {
		if(m_iMode == BillMode.List )
			nc.ui.scm.pub.report.OrientDialog.clearOrientColor(getBillListPanel().getBodyTable());
		else
			nc.ui.scm.pub.report.OrientDialog.clearOrientColor(getBillCardPanel().getBillTable());
		m_isLocated = false;
	}


}

/**
 * �����ߣ����˾�
 * ���ܣ�����б�ͱ�����
 * ������
 * ���أ�
 * ���⣺
 * ���ڣ�(2001-5-9 9:23:32)
 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
 *
 *
 *
 *
 */
protected void clearUi() {
	try {
		//clear card panel()
		SpecialBillVO voNullBill=new SpecialBillVO();
		voNullBill.setParentVO(new SpecialBillHeaderVO());
		voNullBill.setChildrenVO(new SpecialBillItemVO[]{new SpecialBillItemVO()});
		getBillCardPanel().setBillValueVO(voNullBill);
		getBillCardPanel().getBillModel().clearBodyData();
		//clear list panel()
		getBillListPanel().getHeadBillModel().clearBodyData();
		getBillListPanel().getBodyBillModel().clearBodyData();

	} catch (Exception e) {

	}

}

/**
 * �����ߣ�������
 * ���ܣ����ڶ��������У���null�ҷǿյ�ֵ�����һ��VO�б���VO��
 * ������
 * ���أ�
 * ���⣺
 * ���ڣ�(2001-12-14 11:25:17)
 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
 * @param vo nc.vo.pub.AggregatedValueObject
 * @param itemVOs nc.vo.pub.CircularlyAccessibleValueObject[]
 */
protected void combinedItemVOsByNotNullValue(
	AggregatedValueObject vo,
	CircularlyAccessibleValueObject[] itemVOs,
	boolean bIsGeneralBillVO) {
	if (vo.getChildrenVO().length != itemVOs.length || itemVOs.length == 0) {
		return;
	}

	CircularlyAccessibleValueObject[] resultItemVOs= vo.getChildrenVO();
	String[] sNames= itemVOs[0].getAttributeNames();

	for (int i= 0; i < itemVOs.length; i++) {
		String sNextPK= "";
		if (bIsGeneralBillVO) {
			sNextPK= ((GeneralBillItemVO) itemVOs[i]).getCgeneralbid();
		} else {
			sNextPK= ((SpecialBillItemVO) itemVOs[i]).getCspecialbid();
		}
		for (int k= 0; k < vo.getChildrenVO().length; k++) {
			CircularlyAccessibleValueObject cvos= resultItemVOs[k];
			String sFirstPK= "";
			if (bIsGeneralBillVO) {
				sFirstPK= ((GeneralBillItemVO) cvos).getCgeneralbid();
			} else {
				sFirstPK= ((SpecialBillItemVO) cvos).getCspecialbid();
			}
			if (sFirstPK.equals(sNextPK)) {
				for (int j= 0; j < sNames.length; j++) {
					Object oValue= itemVOs[i].getAttributeValue(sNames[j]);
					if (null != oValue && oValue.toString().trim().length() != 0) {
						cvos.setAttributeValue(sNames[j], oValue);
					}
				}
				resultItemVOs[k]= (CircularlyAccessibleValueObject) cvos.clone();
				break;
			}
		}
	}
	vo.setChildrenVO(resultItemVOs);
}

/**
 * �˴����뷽��˵����
 * ��������:�����ѯʱû�в鵽���ݵ�����½���ġ�
 * ���ߣ�������
 * �������:
 * ����ֵ:
 * �쳣����:
 * ����:(2003-6-9 15:57:49)
 */
protected void dealNoData() {
	m_iTotalListHeadNum = 0;
	m_iFirstSelListHeadRow = -1;
	m_iLastSelListHeadRow = -1;
	m_iLastSelCardBodyRow = -1;
	clearUi();
	showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill","UPP4008bill-000013")/*@res "δ�鵽���������ĵ��ݡ�"*/);
}

/**
 * �����ߣ�������
 * ���ܣ���ѯ��ʱȫ�����й�ʽ��ִ�У��Ա���ӡ����
 * ������
 * ���أ�
 * ���⣺
 * ���ڣ�(2001-12-14 11:05:03)
 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
 * @param alVOs java.util.ArrayList
 */
protected void executeLoadFormularAfterQuery(ArrayList alVOs) {
	if (null == alVOs || alVOs.size() == 0)
		return;
	for (int i = 0; i < alVOs.size(); i++) {
		//����VO
		getBillListPanel().setBodyValueVO(
			((AggregatedValueObject) alVOs.get(i)).getChildrenVO());
		//ִ�й�ʽ
		try {
			//ִ�м��ع�ʽ
			getBillListPanel().getBodyBillModel().getFormulaParse().setNullAsZero(false);
			getBillListPanel().getBodyBillModel().execLoadFormula();
		} catch (Exception eee) {
			nc.vo.scm.pub.SCMEnv.error(eee);
		}
		//ȡ��VO
		if ((AggregatedValueObject) alVOs.get(i) instanceof SpecialBillVO) {
			SpecialBillItemVO[] shvoSpecialHVOnow =
				new SpecialBillItemVO[getBillListPanel().getBodyTable().getRowCount()];
			for (int j = 0; j < shvoSpecialHVOnow.length; j++) {
				shvoSpecialHVOnow[j] = new SpecialBillItemVO();
			}
			getBillListPanel().getBodyBillModel().getBodyValueVOs(shvoSpecialHVOnow);
			//�û���VO
			combinedItemVOsByNotNullValue(
				(SpecialBillVO) alVOs.get(i),
				shvoSpecialHVOnow,
				false);
			//((AggregatedValueObject) alVOs.get(i)).setChildrenVO(shvoSpecialHVOnow);
		} else {
			GeneralBillItemVO[] shvoSpecialHVOnow =
				new GeneralBillItemVO[getBillListPanel().getBodyTable().getRowCount()];
			for (int j = 0; j < shvoSpecialHVOnow.length; j++) {
				shvoSpecialHVOnow[j] = new GeneralBillItemVO();
			}
			getBillListPanel().getBodyBillModel().getBodyValueVOs(shvoSpecialHVOnow);
			//�û���VO
			combinedItemVOsByNotNullValue(
				(GeneralBillVO) alVOs.get(i),
				shvoSpecialHVOnow,
				true);
			//((AggregatedValueObject) alVOs.get(i)).setChildrenVO(shvoSpecialHVOnow);
		}
	}
}

/**
 * �����ߣ�������
 * ���ܣ�Ԥ�ȶ�λ
 * ������
 * ���أ�
 * ���⣺
 * ���ڣ�(2001-12-18 20:47:09)
 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
 *2003-11-19 zss ����궨λ�ڴ�����봦����Ϊ�����λ���к�ʱִ���̵㵥�ĵ����ȼ�ʱ�ͻ�����кš�
 */
public void firstSetColEditable() {
	//Ԥ�ȶ�λ
	if (getBillCardPanel().getBillTable().getRowCount() > 0
		&& getBillCardPanel().getBillTable().getColumnCount() > 0) {
		getBillCardPanel().getBillTable().setRowSelectionInterval(0, 0);
		getBillCardPanel().getBillTable().setColumnSelectionInterval(1, 1);

	}
}

/**
 * �����ߣ����˾�
 * ���ܣ��ڱ�������ʾVO,�����½���״̬updateValue()
 * ������
 * ���أ�
 * ���⣺
 * ���ڣ�(2001-5-9 9:23:32)
 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
 *
 *
 *
 *
 */
public void freshTs(ArrayList alTs) throws Exception {
	if (alTs == null || alTs.size() == 0)
		throw new Exception(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill","UPP4008bill-000027")/*@res "�����tsΪ�գ�"*/);
	setTs(m_voBill, alTs);
	setTs(m_iLastSelListHeadRow, alTs);
	setUiTs(alTs);
}

/**
   * ���� ReturnDlg1 ����ֵ��
   * @return nc.ui.ic.ic205.ReturnDlg
   */
/* ���棺�˷������������ɡ� */
protected ClientUIInAndOut getAuditDlg() {
	if (m_dlgInOut == null) {
		try {
			// user code begin {1}
			m_dlgInOut= new ClientUIInAndOut(this, "");
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}

	//m_dlgInOut.setDefaultCloseOperation(javax.swing.WindowConstants.HIDE_ON_CLOSE);
	return m_dlgInOut;
}

/**
   * �����ߣ�������
   * ���ܣ��ӱ������л��ȫ����������
   * ������
   * ���أ�
   * ���⣺
   * ���ڣ�(2001-5-16 ���� 12:30)
   * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
   * @return nc.vo.ic.ic221.SpecialHVO
   */
protected SpecialBillVO getBillVO() {
	SpecialBillVO voNowBill = new SpecialBillVO(getBillCardPanel().getRowCount());

	getBillCardPanel().getBillValueVO(voNowBill);
	if (voNowBill == null
		|| voNowBill.getParentVO() == null
		|| voNowBill.getChildrenVO() == null) {
		nc.vo.scm.pub.SCMEnv.out("����������!");
		return null;
	} else {
		return voNowBill;
	}
}

	/**
	 * �����ߣ����˾�
	 * ���ܣ��õ��޸ĺ��vo,�����޸ĺ�ı���
	 * ������
	 * ���أ�
	 * ���⣺
	 * ���ڣ�(2001-5-9 9:23:32)
	 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 *
	 *
	 *
	 *
	 */
	protected SpecialBillItemVO[] getChangedItemVOs() {
		nc.ui.pub.bill.BillModel bmTemp= getBillCardPanel().getBillModel();
		if (bmTemp == null) {
			nc.vo.scm.pub.SCMEnv.out("bm null ERROR!");
			return null;
		}
		Vector vBodyData= bmTemp.getDataVector();
		if (vBodyData == null || vBodyData.size() == 0) {
			nc.vo.scm.pub.SCMEnv.out("bd null ERROR!");
			return null;
		}
		SpecialBillItemVO[] voaItem= m_voBill.getItemVOs();
		//ɾ������

		//vo����ĳ���==��ǰ��ʾ������+ɾ����������
		int rowCount= vBodyData.size();
		int length= 0;
		Vector vDeleteRow= bmTemp.getDeleteRow();
		if (vDeleteRow != null)
			length= rowCount + vDeleteRow.size();
		else
			length= rowCount;
		//��ʼ�����ص�vo
		SpecialBillItemVO[] voaBody= new SpecialBillItemVO[length];

		int iRowStatus= nc.ui.pub.bill.BillModel.ADD;

		//����ǰ��������ʾ���У�����ԭ�С��޸ĺ���С��������С�
		for (int i= 0; i < vBodyData.size(); i++) {
			voaBody[i]= new SpecialBillItemVO();
			iRowStatus= bmTemp.getRowState(i);
			if (nc.ui.pub.bill.BillModel.ADD == iRowStatus
				|| nc.ui.pub.bill.BillModel.MODIFICATION == iRowStatus)
				for (int j= 0; j < bmTemp.getBodyItems().length; j++) {
					nc.ui.pub.bill.BillItem item= bmTemp.getBodyItems()[j];
					Object aValue= bmTemp.getValueAt(i, item.getKey());
//					aValue= item.converType(aValue);
					voaBody[i].setAttributeValue(item.getKey(), aValue);
				} else //δ�޸ĵ���ֻ��PK����
				if (nc.ui.pub.bill.BillModel.NORMAL == iRowStatus)
					for (int j= 0; j < bmTemp.getBodyItems().length; j++) {
						nc.ui.pub.bill.BillItem item= bmTemp.getBodyItems()[j];

						//if (m_sBillItemPKitemName.equals(item.getKey())) {
							Object aValue= bmTemp.getValueAt(i, item.getKey());
//							aValue= item.converType(aValue);
							voaBody[i].setAttributeValue(item.getKey(), aValue);
							//break;
						//}
					}
			//����״̬
			switch (iRowStatus) {
				case nc.ui.pub.bill.BillModel.ADD : //��������
					voaBody[i].setStatus(nc.vo.pub.VOStatus.NEW);
					break;
				case nc.ui.pub.bill.BillModel.MODIFICATION : //�޸ĺ����
					voaBody[i].setStatus(nc.vo.pub.VOStatus.UPDATED);
					break;
				case nc.ui.pub.bill.BillModel.NORMAL : //ԭ��
					voaBody[i].setStatus(nc.vo.pub.VOStatus.UNCHANGED);
					break;
			}
			//������
			voaBody[i].setFreeItemVO(voaItem[i].getFreeItemVO());
			voaBody[i].setInv(voaItem[i].getInv());

		} //ɾ������
		if (vDeleteRow != null) {
			for (int i= 0; i < vDeleteRow.size(); i++) {
				//��rowCount��ʼд�룡����
				voaBody[i + rowCount]= new SpecialBillItemVO();
				Vector rowVector= (Vector) vDeleteRow.elementAt(i);
				for (int j= 0; j < bmTemp.getBodyItems().length; j++) {
					nc.ui.pub.bill.BillItem item= bmTemp.getBodyItems()[j];
					int col= bmTemp.getBodyColByKey(item.getKey());
					Object aValue= rowVector.elementAt(col);
					if (aValue != null)
						if (item.getDataType() == nc.ui.pub.bill.BillItem.COMBO)
							for (int k= 0;
								k < ((nc.ui.pub.beans.UIComboBox) item.getComponent()).getItemCount();
								k++) {
								if (aValue
									.toString()
									.equals(((nc.ui.pub.beans.UIComboBox) item.getComponent()).getItemAt(k))) {
									aValue= new Integer(k);
									break;
								}
							}
					voaBody[i + rowCount].setAttributeValue(item.getKey(), aValue);
				} //����״̬
				voaBody[i + rowCount].setStatus(nc.vo.pub.VOStatus.DELETED);
			}
		}
		return voaBody;
	}

/**
 * ���� QueryConditionClient1 ����ֵ��
 * @return nc.ui.pub.query.QueryConditionClient
 */
//protected QueryConditionDlgForBill getConditionDlg() {
//	if (ivjQueryConditionDlg == null) {
//		ivjQueryConditionDlg= new QueryConditionDlgForBill(this);
//		//ivjQueryConditionDlg.setDefaultCloseOperation(ivjQueryConditionDlg.HIDE_ON_CLOSE);
//		ivjQueryConditionDlg.setTempletID(m_sCorpID, m_sPNodeCode, m_sUserID, null);
//
//		ArrayList alCorpIDs= new ArrayList();
//		try {
//			//alCorpIDs= SpecialHBO_Client.queryCorpIDs(m_sUserID);
//			//alCorpIDs=
//			//(ArrayList) SpecialHBO_Client.queryInfo(
//			//new Integer(QryInfoConst.USER_CORP),
//			//m_sUserID);
//			alCorpIDs=
//				(ArrayList) SpecialBillHelper.queryInfo(new Integer(QryInfoConst.USER_CORP), m_sUserID );
//		} catch (Exception e) {
//			nc.vo.scm.pub.SCMEnv.error(e);
//		}
//		//���õ�������Ϊ��ǰ��¼����
//		//modified by liuzy 2008-04-01 v5.03���󣺿�浥�ݲ�ѯ������ֹ����
////		ivjQueryConditionDlg.setInitDate("dbilldate", m_sLogDate);
//		ivjQueryConditionDlg.setInitDate("dbilldate.from", m_sLogDate);
//		ivjQueryConditionDlg.setInitDate("dbilldate.end", m_sLogDate);
//		
//		//��ѯ�Ի�����ʾ��ӡ����ҳǩ��
//		ivjQueryConditionDlg.setShowPrintStatusPanel(true);
//		
//		//����Ϊ�Բ��յĳ�ʼ��
//		ivjQueryConditionDlg.initQueryDlgRef();
//		ivjQueryConditionDlg.initCorpRef("pk_corp", m_sCorpID, alCorpIDs);
//
//	    Object[][] arycombox = new Object[2][2];
//		arycombox[0][0] = "Y";
//		arycombox[0][1] = "Y";
//		arycombox[1][0] = "N";
//		arycombox[1][1] = "N";
//
//		ivjQueryConditionDlg.setCombox("body.bqrybalrec",arycombox);
//
//		//�������ֿ�ͳ���ֿ�����Ȩ��
//		//zhy2005-06-10���ⵥֻ�����ֿ�ͳ���ֿ�
//		//ivjQueryConditionDlg.setCorpRefs("head.pk_corp",new String[]{"cinwarehouseid","coutwarehouseid","inv.invcode","invcl.invclasscode"});
//    ivjQueryConditionDlg.setCorpRefs("head.pk_corp",GenMethod.getDataPowerFieldFromDlgNotByProp(ivjQueryConditionDlg));
//		
//
//	}
//	return ivjQueryConditionDlg;
//}

/**
 * �����ߣ�������
 * ���ܣ�
 * ������
 * ���أ�
 * ���⣺
 * ���ڣ�(2001-10-30 15:06:35)
 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
 */
protected PrintDataInterface getDataSource() {
	if (null == m_dataSource) {
		m_dataSource= new PrintDataInterface();
		BillData bd= getBillCardPanel().getBillData();
		m_dataSource.setBillData(bd);
		m_dataSource.setModuleName(m_sPNodeCode);
		m_dataSource.setTotalLinesInOnePage(getPrintEntry().getBreakPos());
	}
	return m_dataSource;
}

/**
 * �����ߣ��۱�
 * ���ܣ�
 * ������
 * ���أ�
 * ���⣺
 * ���ڣ�2005-03-12
 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
 */
protected PrintDataInterface getNewDataSource() {
	//if (null == m_dataSource) {
	PrintDataInterface ds = new PrintDataInterface();
	BillData bd = getBillCardPanel().getBillData();
	ds.setBillData(bd);
	ds.setModuleName(m_sPNodeCode);
	ds.setTotalLinesInOnePage(getPrintEntry().getBreakPos());
	//}
	return ds;
}

/**
 * ?user>
 * ���ܣ�
 * ������
 * ���أ�
 * ���⣺
 * ���ڣ�(2004-6-30 17:57:26)
 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
 *
 * @return nc.vo.bd.def.DefVO[]
 * @param pk_corp java.lang.String
 * @param isHead boolean
 */
public DefVO[] getDefBodyVO() {
	try{
	if (m_defBody == null) {
		m_defBody=DefSetTool.getDefBody(m_sCorpID,m_sBillTypeCode);
	}
	}catch(Exception e){
		nc.vo.scm.pub.SCMEnv.error(e);
	
	}
	return m_defBody;
}

/**
 * ?user>
 * ���ܣ�
 * ������
 * ���أ�
 * ���⣺
 * ���ڣ�(2004-6-30 17:57:26)
 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
 *
 * @return nc.vo.bd.def.DefVO[]
 * @param pk_corp java.lang.String
 * @param isHead boolean
 */
public DefVO[] getDefHeadVO() {
	try{
	if(m_defHead==null){
		m_defHead=DefSetTool.getDefHead(m_sCorpID,m_sBillTypeCode);
	}
}catch(Exception e){
	nc.vo.scm.pub.SCMEnv.error(e);

}

return m_defHead;
}

/**
 * �˴����뷽��˵����
 * ��������:���ֶβ�ѯ��Ҫ��˾�ֶμ��ϱ��������ݲ�ѯVO����where �Ӿ䡣
 * ���ߣ�������
 * �������:
 * ����ֵ:
 * �쳣����:
 * ����:(2003-6-27 8:56:11)
 * @return java.lang.String
 */
//protected String getExtenWhere() {
//	//����ֵ
//	StringBuffer sbWhere = new StringBuffer();
//	//��ѯVO
//	ConditionVO[] voConds = getConditionDlg().getConditionVO();
//	//��ѯVO���Ƿ������˾
//	boolean isHaveCorp = false;
//	//�����в�ѯ����������£�
//	if(voConds != null && voConds.length > 0){
//		int ilen = voConds.length;
//		String sFieldCode = null;
//
//		for(int i=0;i<ilen;i++){
//			sFieldCode = voConds[i].getFieldCode();
//			if("pk_corp".equals(sFieldCode) || "head.pk_corp".equals(sFieldCode)){
//				isHaveCorp = true;
//				//ͳһ����˾�ֶμ��ϱ���head��
//				voConds[i].setFieldCode("head.pk_corp");
//			}
//			sbWhere.append(voConds[i].getSQLStr());
//		}
//		//�����ѯ�����в�������˾��Ҫ���ϡ�
//		if(!isHaveCorp){
//			int iAndBg = sbWhere.toString().indexOf("and");
//			if (iAndBg > 0){
//			sbWhere.insert(iAndBg+3,"(");
//			sbWhere.append(")");
//			}
//			sbWhere.append(" AND head.pk_corp ='");
//			sbWhere.append(m_sCorpID);
//			sbWhere.append("'");
//		}
//	}else{
//		//�����ѯ����Ϊ�գ����Ϲ�˾��
//		sbWhere.append(" and head.pk_corp='");
//		sbWhere.append(m_sCorpID);
//		sbWhere.append("'");
//	}
//
//	return sbWhere.toString();
//}

/**
 * �˴����뷽��˵����
 * ��������:���ֶβ�ѯ��Ҫ��˾�ֶμ��ϱ��������ݲ�ѯVO����where �Ӿ䡣
 * ���ߣ�������
 * �������:
 * ����ֵ:
 * �쳣����:
 * ����:(2003-6-27 8:56:11)
 * @return java.lang.String
 */
//protected String getExtenWhere(ConditionVO[] voConds) {
//	//����ֵ
//	StringBuffer sbWhere = new StringBuffer();
//	//��ѯVO���Ƿ������˾
//	boolean isHaveCorp = false;
//
//	//�����в�ѯ����������£�
//	if(voConds != null && voConds.length > 0){
//		int ilen = voConds.length;
//		String sFieldCode = null;
//		for(int i=0;i<ilen;i++){
//			sFieldCode = voConds[i].getFieldCode();
//			if("pk_corp".equals(sFieldCode) || "head.pk_corp".equals(sFieldCode)){
//				isHaveCorp = true;
//				//ͳһ����˾�ֶμ��ϱ���head��
//				voConds[i].setFieldCode("head.pk_corp");
//			}
//			//modified by liuzy 2008-04-01 v5.03���󣺿�浥�ݲ�ѯ������ֹ����
//			if (null != voConds[i] && null != voConds[i].getFieldCode()
//					&& (voConds[i].getFieldCode().equals("dbilldate.from") || voConds[i]
//							.getFieldCode().equals("dbilldate.end"))) {
//				voConds[i].setFieldCode("dbilldate");
//			}
//			
//			sbWhere.append(voConds[i].getSQLStr());
//		}
//		//�����ѯ�����в�������˾��Ҫ���ϡ�
//		if(!isHaveCorp){
//			int iAndBg = sbWhere.toString().indexOf("and");
//			if (iAndBg > 0){
//			sbWhere.insert(iAndBg+3,"(");
//			sbWhere.append(")");
//			}
//			sbWhere.append(" AND head.pk_corp ='");
//
//			sbWhere.append(m_sCorpID);
//			sbWhere.append("'");
//		}
//		
//	}else{
//		//�����ѯ����Ϊ�գ����Ϲ�˾��
//		sbWhere.append(" and head.pk_corp='");
//		sbWhere.append(m_sCorpID);
//		sbWhere.append("'");
//	}
//
//	return sbWhere.toString();
//}

/**
 * �˴����뷽��˵����
 * ��������:��� BillFormulaContainer
 * ���ߣ�����
 * �������:
 * ����ֵ:
 * �쳣����:
 * ����:(2003-7-2 9:48:12)
 * @return nc.ui.ic.pub.BillFormulaContainer
 */
public BillFormulaContainer getFormulaBillContainer() {
   if (m_billFormulaContain==null)
   {
	   m_billFormulaContain=new BillFormulaContainer(getBillListPanel());
	}
	return m_billFormulaContain;
}

/**
 * �˴����뷽��˵����
 * ��������:
 * ���ߣ����˾�
 * �������:
 * ����ֵ:
 * �쳣����:
 * ����:(2003-6-25 20:43:17)
 * @return java.util.ArrayList
 */
protected ArrayList getFormulaItemBody()
{
	if (m_alFormulBodyItem == null)
	{
		m_alFormulBodyItem = new ArrayList();

		//����������λ
		String[] aryItemField31 = new String[] { "measname", "castunitname", "castunitid" };
		m_alFormulBodyItem.add(aryItemField31);

		//��Դ��������
		String[] aryItemField9 = new String[] { "billtypename", "csourcetypename", "csourcetype" };
		m_alFormulBodyItem.add(aryItemField9);

		//Դͷ���ݺ�
		String[] aryItemField10 = new String[] { "billtypename", "cfirsttypename", "cfirsttype" };
		m_alFormulBodyItem.add(aryItemField10);

		//�ֿ�����
		String[] aryItemField15 = new String[] { "storname", "cwarehousename", "cwarehouseid" };
		m_alFormulBodyItem.add(aryItemField15);
		
	}
	return m_alFormulBodyItem;
}

/**
 * �˴����뷽��˵����
 * ��������:Ĭ�ϵ����ⵥ�ϵı�ͷ��ʽ
 * ���ߣ����˾�
 * �������:
 * ����ֵ:
 * �쳣����:
 * ����:(2003-6-25 20:43:17)
 * @return java.util.ArrayList
 */
protected ArrayList getFormulaItemHeader()
{
	if (m_alFormulHeadItem==null){
	 m_alFormulHeadItem = new ArrayList();

	//ԭ�еĹ�ʽ

	//���ҵ��Ա  ���Ա ������
	String[] aryItemField3 = new String[] { "psnname", "cinbsrname", "cinbsrid" };
	m_alFormulHeadItem.add(aryItemField3);

	//����ҵ��Ա  ���Ա �̵�Ա
	String[] aryItemField4 = new String[] { "psnname", "coutbsorname", "coutbsor" };
	m_alFormulHeadItem.add(aryItemField4);

	//��ⲿ��
	String[] aryItemField19 = new String[] { "deptname", "cindeptname", "cindeptid" };
	m_alFormulHeadItem.add(aryItemField19);

	//���ⲿ��
	String[] aryItemField29 = new String[] { "deptname", "coutdeptname", "coutdeptid" };
	m_alFormulHeadItem.add(aryItemField29);

	//��ֿ�����
	String [] aryItemField15=new String[] {"storname","cinwarehousename","cinwarehouseid"};
	m_alFormulHeadItem.add(aryItemField15);
	// ���ֿ�	 3
	String[] aryItemField25 = new String[] { "storname", "coutwarehousename", "coutwarehouseid" };
	m_alFormulHeadItem.add(aryItemField25);

	//����ֿ��λ����
	String[] aryItemField26 = new String[] { "csflag", "isLocatorMgtOut", "coutwarehouseid" };
	m_alFormulHeadItem.add(aryItemField26);

	//����ֿ��Ƿ��Ʒ��
	String[] aryItemField27 = new String[] { "gubflag", "iswastewhout", "coutwarehouseid" };
	m_alFormulHeadItem.add(aryItemField27);


	//������
	String[] aryItemField2 = new String[] { "user_name", "vadjustername", "vadjuster" };
	m_alFormulHeadItem.add(aryItemField2);

	////������ 5
	String[] aryItemField12 = new String[] { "user_name", "cauditorname", "cauditorid" };
	m_alFormulHeadItem.add(aryItemField12);

	////����Ա 6
	String[] aryItemField1 = new String[] { "user_name", "coperatorname", "coperatorid" };
	m_alFormulHeadItem.add(aryItemField1);
	
////����Ա 6
	String[] aryItemField111 = new String[] { "user_name", "clastmodiname", "clastmodiid" };
	m_alFormulHeadItem.add(aryItemField111);

	//for ���׼�����װ������ж��  ��ͨ�������д����ͷ ����Ҫ����ͨ����ʽ����ʵ��
	////��������
	//String [] aryItemField20=new String[] {"pk_invbasdoc","pk_invbasdoc","ctj"};
	//m_alFormulHeadItem.add(aryItemField20);

	////���� 14
	//String [] aryItemField21=new String[] {"invname","ctjname","pk_invbasdoc"};
	//m_alFormulHeadItem.add(aryItemField21);

	}
	return m_alFormulHeadItem;
}

/**
 * �������ڣ�(2003-3-4 17:13:59)
 * ���ߣ�����
 * �޸����ڣ�
 * �޸��ˣ�
 * �޸�ԭ��
 * ����˵����
 * @return nc.ui.ic.pub.bill.InvoInfoBYFormula
 */
public InvoInfoBYFormula getInvoInfoBYFormula() {
	if (m_InvoInfoBYFormula==null)
	   m_InvoInfoBYFormula= new InvoInfoBYFormula(getCorpPrimaryKey());
	return m_InvoInfoBYFormula;
}

/**
   * �����ߣ�������
   * ���ܣ�����б��еı�ͷVO����
   * ������
   * ���أ�
   * ���⣺
   * ���ڣ�(2001-5-18 ���� 12:09)
   * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
   * @return nc.vo.ic.ic221.SpecialHHeaderVO
   */
protected SpecialBillHeaderVO[] getListHeaderVOs() {
	SpecialBillHeaderVO[] voaHeader = null;
	if (m_alListData != null && m_alListData.size() > 0) {
		voaHeader = new SpecialBillHeaderVO[m_alListData.size()];
		for (int i = 0; i < m_alListData.size(); i++)
			voaHeader[i] = ((SpecialBillVO) m_alListData.get(i)).getHeaderVO();
	}
	return voaHeader;
}

/**
 * �˴����뷽��˵����
 * �������ڣ�(2001-9-19 16:02:07)
 * @return nc.ui.ic.ic101.OrientDialog
 * @author:���Ӣ
 */
public nc.ui.ic.pub.orient.OrientDialog getOrientDlg() {
		if (m_dlgOrient == null) {
		m_dlgOrient = new nc.ui.ic.pub.orient.OrientDialog(this);
	}
	return m_dlgOrient;
}

/**
 * �����ߣ�������
 * ���ܣ�
 * ������
 * ���أ�
 * ���⣺
 * ���ڣ�(2001-10-30 15:06:35)
 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
 */
protected nc.ui.pub.print.PrintEntry getPrintEntry() {
	if (null == m_print) {
		m_print= new nc.ui.pub.print.PrintEntry(this, null);
		m_print.setTemplateID(m_sCorpID, m_sPNodeCode, m_sUserID, null);
	}
	return m_print;
}

/**
 * ����˵����ȡ���б���ʽ��ѡ�еĵ���
 * �������ڣ�(2003-03-13 12:59:54)
 * ���ߣ������
 * �޸����ڣ�
 * �޸��ˣ�
 * �޸�ԭ��
 * �㷨˵����
 * @return java.util.ArrayList
 */
protected ArrayList getSelectedBills()
{

	ArrayList albill = new ArrayList();
	int iSelListHeadRowCount = getBillListPanel().getHeadTable().getSelectedRowCount();
	if (iSelListHeadRowCount <= 0)
	{
		showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill","UPP4008bill-000009")/*@res "����ѡ�е��ݣ�"*/);
		return null;
	}
	int[] arySelListHeadRows = new int[iSelListHeadRowCount];
	arySelListHeadRows = getBillListPanel().getHeadTable().getSelectedRows();

	SpecialBillVO voaBill[] = new SpecialBillVO[iSelListHeadRowCount];
	Vector vHeadPK = new Vector();
	Vector vIndex = new Vector();
	for (int i = 0; i < iSelListHeadRowCount; i++)
	{
		if (m_alListData != null && m_alListData.size() > arySelListHeadRows[i])
		{

			voaBill[i] = (SpecialBillVO) m_alListData.get(arySelListHeadRows[i]);
			if (voaBill[i].getChildrenVO() == null || voaBill[i].getChildrenVO().length == 0)
			{

				vHeadPK.addElement(((SpecialBillHeaderVO) voaBill[i].getParentVO()).getCspecialhid());
				vIndex.addElement(new Integer(arySelListHeadRows[i]));
			}

		}
	}

	//��ѯ��������
	if (vIndex.size() > 0)
	{
		String[] saPK = new String[vHeadPK.size()];
		int[] indexs = new int[vIndex.size()];
		vHeadPK.copyInto(saPK);

		for (int i = 0; i < vIndex.size(); i++)
		{
			indexs[i] = ((Integer) vIndex.get(i)).intValue();
		}
		qryItems(indexs, saPK);
	}
	for (int i = 0; i < arySelListHeadRows.length; i++)
	{
		if (m_alListData != null && m_alListData.size() > arySelListHeadRows[i])
		{

			albill.add((SpecialBillVO) m_alListData.get(arySelListHeadRows[i]));
		}
	}

	return albill;
}

/**
   * �����ߣ�������
   * ���ܣ��ӱ������л�ø�������
   * ������
   * ���أ�
   * ���⣺
   * ���ڣ�(2001-5-16 ���� 12:30)
   * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
   * @return nc.vo.ic.ic221.SpecialHVO
   */
protected SpecialBillVO getUpdatedBillVO() {
	SpecialBillVO voNowBill = new SpecialBillVO();

	voNowBill =
		(SpecialBillVO) getBillCardPanel().getBillValueChangeVO(
			SpecialBillVO.class.getName(),
			SpecialBillHeaderVO.class.getName(),
			SpecialBillItemVO.class.getName());

	if ((null == voNowBill)
		|| (voNowBill.getParentVO() == null)
		|| (voNowBill.getChildrenVO() == null)) {
		nc.vo.scm.pub.SCMEnv.out("����������!");
		return null;
	} else {
		return voNowBill;
	}
}

/**
 * �����ߣ����˾�
 * ���ܣ��õ��ֿ���Ϣ��
 * ������item key
 * ���أ�
 * ���⣺
 * ���ڣ�(2001-5-8 19:08:05)
 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
 *
 *
 *
 *
 */
protected WhVO getWhInfoByID(String sWhID) {
	//�ֿ�
	WhVO voWh = null;
	//���ڻ����в��Ҳֿ���Ϣ
	if (sWhID != null && m_htWh != null && m_htWh.containsKey(sWhID.trim()))
		voWh = (WhVO) m_htWh.get(sWhID.trim());
	else { //û�еĻ�ȥ��һ�¡�
		try {
			//�������������б���ʽ����ʾ��
			voWh =
				(WhVO) SpecialBillHelper.queryInfo(new Integer(QryInfoConst.WH), sWhID );
			m_htWh.put(sWhID.trim(), voWh);
		} catch (Exception e2) {
			nc.vo.scm.pub.SCMEnv.error(e2);
		}
	}
	return voWh;
}

/**
 * �����ߣ�������
 * ���ܣ��õ��ֿ���Ϣ��
 * ������item key
 * ���أ�
 * ���⣺
 * ���ڣ�(2001-5-8 19:08:05)
 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
 *
 *
 *
 *
 */
protected WhVO getWhInfoByRef(String sItemKey) {
	//�ֿ�
	WhVO voWh = null;
	try {
		String sID =
			((nc.ui.pub.beans.UIRefPane) getBillCardPanel()
				.getHeadItem(sItemKey)
				.getComponent())
				.getRefPK();

		//�������������б���ʽ����ʾ��
		voWh = getWhInfoByID(sID);

	} catch (Exception e2) {
		nc.vo.scm.pub.SCMEnv.error(e2);
	}
	return voWh;
}

/**
 * ÿ�������׳��쳣ʱ������
 * @param exception java.lang.Throwable
 */
protected void handleException(Exception exception) {

	/* ��ȥ���и��е�ע�ͣ��Խ�δ��׽�����쳣��ӡ�� stdout�� */
	nc.vo.scm.pub.SCMEnv.out("--------- δ��׽�����쳣 ---------");
	nc.vo.scm.pub.SCMEnv.error(exception);
}

/**
 * �򵥳�ʼ���ࡣ����������������������õĲ���Ա����˾�ȡ�
 */
/* ���棺�˷������������ɡ� */
protected void initialize(
	String pk_corp,
	String sOperatorid,
	String sOperatorname,
	String sBiztypeid,
	String sGroupid,
	String sLogDate) {
	try {
		// user code begin {1}
		initVariable();
		//��ʼ����ť
		initButtons();
		setButtons(m_aryButtonGroup);
		m_sUserID = sOperatorid;
		//m_sGroupID = sGroupid; //����
		m_sUserName = sOperatorname;
		m_sCorpID = pk_corp;
		m_sLogDate = sLogDate;
		//��ϵͳ����
		initSysParam();

		getBillCardPanel();
		getBillListPanel();
		setButtonState();
		setBillState();

		//���˲��գ��������getCEnvInfo()����֮��
		filterRef(m_sCorpID);
		//�õ���ǰ�ı�����ϵͳĬ�ϵı���ɫ
		m_cNormalColor = getBillCardPanel().getBillTable().getBackground();

		switchListToBill();

		// user code end
		setName("ClientUI");
		setLayout(null);
		//setSize(774, 419);
		setLayout(new java.awt.CardLayout());
		//	setSize(774, 419);
		add(getBillCardPanel(), "card");
		add(getBillListPanel(), "list");

		//ljun
		getBillCardPanel().getBillModel().setCellEditableController(this);
		
	
			if ( m_iMode == BillMode.List)
			setPageBtnStatus(0,0);
			else if (null != m_alListData)
				setPageBtnStatus(m_alListData.size(),0);
			
	
			

	} catch (java.lang.Throwable ivjExc) {
		handleException(ivjExc);
	}
	// user code begin {2}
	getBillListPanel().addEditListener(this);
	getBillCardPanel().addEditListener(this);
	getBillCardPanel().addBodyEditListener2(this);

	//getBillCardPanel().getBillTable().getSelectionModel().addListSelectionListener(
	//this);

	//getBillCardPanel().getBillTable().getColumnModel().addColumnModelListener(this);

	getBillListPanel().getHeadTable().addMouseListener(this);
	getBillListPanel().getHeadBillModel().addBillSortListener2(this);
	m_listHeadSortCtl = new ClientUISortCtl(this,false,BillItem.HEAD);
	m_listBodySortCtl = new ClientUISortCtl(this,false,BillItem.BODY);
	
	getBillCardPanel().getBillTable().addSortListener();
	m_cardBodySortCtl = new ClientUISortCtl(this,true,BillItem.BODY);
  getBillCardPanel().setAutoExecHeadEditFormula(true);
	
	//getBillListPanel().getHeadBillModel().addSortListener(this);
	getBillCardPanel().getBillModel().addTableModelListener(this);

	getBillCardPanel().addBodyMenuListener(this);
  getBillCardPanel().addActionListener(this);

	getBillCardPanel().setVisible(true);
	getBillListPanel().setVisible(true);
	((java.awt.CardLayout) getLayout()).show(this, "card");
	
  GeneralBillUICtl.setBillCardPaneSelectMode(getBillCardPanel());
  GeneralBillUICtl.setBillListPaneSelectMode(getBillListPanel());
	
	showBtnSwitch();

}

protected int m_Menu_AddNewRowNO_Index = -1;

protected UIMenuItem getAddNewRowNoItem() {
  if(miAddNewRowNo==null){
    miAddNewRowNo =  new UIMenuItem(nc.ui.ml.NCLangRes
        .getInstance().getStrByID("4008bill", "UPP4008bill-000551")/*
         * @res
         * "�����к�"
         */);
  }
	return miAddNewRowNo;
}

protected UIMenuItem getLineCardEditItem() {
  if(miLineCardEdit==null){
    miLineCardEdit = new UIMenuItem(nc.ui.ml.NCLangRes
        .getInstance().getStrByID("common", "SCMCOMMONIC55YB002")/*
         * @res
         * "��Ƭ�༭"
         */);
  }
  return miLineCardEdit;
}

/**
 * �����ߣ����˾�
 * ���ܣ���ϵͳ����
 * ������
 * ���أ�
 * ���⣺
 * ���ڣ�(2001-5-9 9:23:32)
 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
 *
 *
 *
 *
 */
protected void initSysParam() {
	try {
		//��������IC028:����ʱ�Ƿ�ָ����ⵥ;���β��ո����Ƿ񵽵��ݺ�.
		//IC010	�Ƿ񱣴漴ǩ�֡�

		//��������	����				ȱʡֵ
		//BD501	����С��λ			2
		//BD502	����������С��λ		2
		//BD503	������				2
		//BD504	����ɱ�����С��λ	2
//	  IC030 �Ƿ��ֹ��޸ĵ��ݺ�
		String[] saParam =
			new String[] { "IC028", "IC010", "BD501", "BD502", "BD503", "BD504", "BD301" ,"IC030","IC050"};

		//����Ĳ���
		ArrayList alAllParam = new ArrayList();
		//������ı�������
		ArrayList alParam = new ArrayList();
		alParam.add(m_sCorpID); //��һ���ǹ�˾
		alParam.add(saParam); //����Ĳ���
		alAllParam.add(alParam);
		//���û���Ӧ��˾�ı������
		alAllParam.add(m_sUserID);

		//���ص���������
		//ArrayList alRetData =null;
		
	
		//alRetData=(ArrayList)SpecialBillHelper.queryInfo(new Integer(QryInfoConst.INIT_PARAM), alAllParam);


		String[] saParamValue = nc.ui.ic.pub.tools.GenMethod.getSysParams(m_sCorpID, saParam);
		// Ŀǰ��������
		if (saParamValue == null || saParamValue.length <= 0) {
			showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"4008bill", "UPP4008bill-000045")/* @res "��ʼ����������" */);
			return;
		}
		//���صĲ���ֵ
		//String[] saParamValue = (String[]) alRetData.get(0);
		//׷�ٵ����ݲ���,Ĭ������Ϊ"N"
		if (saParamValue != null && saParamValue.length >= alAllParam.size()) {
			//�Ƿ񱣴漴ǩ�֡�Ĭ������Ϊ"N"
			//if(saParamValue[1]!=null)
			//m_sSaveAndSign = saParamValue[1].toUpperCase().trim();
			//BD501	����С��λ			2
			if (saParamValue[2] != null)
				m_iaScale[0] = Integer.parseInt(saParamValue[2]);
			//BD502	����������С��λ		2
			if (saParamValue[3] != null)
				m_iaScale[1] = Integer.parseInt(saParamValue[3]);
			//BD503	������				2
			if (saParamValue[4] != null)
				m_iaScale[2] = Integer.parseInt(saParamValue[4]);
			//BD504	����ɱ�����С��λ	2
			if (saParamValue[5] != null)
				m_iaScale[3] = Integer.parseInt(saParamValue[5]);
			//BD301	����С��λ
			if (saParamValue[6] != null)
				m_iaScale[4] = Integer.parseInt(saParamValue[6]);

			if (saParamValue[7] != null&&"Y".equalsIgnoreCase(saParamValue[7].trim()))
				m_bIsEditBillCode = true;
			
			//IC050 ��������Ƿ��ղֿ����
			if (saParamValue[8] != null&&"�ֿ�".equalsIgnoreCase(saParamValue[8].trim()))
				m_isWhInvRef = true;
			else
				m_isWhInvRef = false;
		}
		//���صĲ�����Ӧ�Ĺ�˾
		//m_alUserCorpID = (ArrayList) alRetData.get(1);

	} catch (Exception e) {
		nc.vo.scm.pub.SCMEnv.out("can not get para" + e.getMessage());
		if (e instanceof nc.vo.pub.BusinessException)
			showErrorMessage(e.getMessage());
	}
}

/**
 * �˴����뷽��˵�����Ƿ��ѯ�ƻ��۸�
 * �������ڣ�(2003-11-11 20:28:48)
 * @return boolean
 */
public boolean isQuryPlanprice() {
	return m_isQuryPlanprice;
}

/**
   * �����ߣ�������
   * ���ܣ��ݼ�SpecialHVO,��StationNumberλ��
   * ������
   * ���أ�
   * ���⣺
   * ���ڣ�(2001-5-16 ���� 7:22)
   * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
   * @param m_iStationNumber int
   */
protected void minusBillVO() {
	m_voBill = null;//�޸ģ�ɾ������յ�ǰ���ݣ����ɾ�����ݺ󣬴�ӡԤ���������ݡ� �޸��ˣ���ѧ�� �޸����ڣ�2007-12-3
	
	if (m_iTotalListHeadNum <= 1) { //���һ��ɾ��
		m_alListData = new ArrayList();
		m_alListData.trimToSize();
		m_iLastSelListHeadRow = -1;
		m_iTotalListHeadNum = 0;
		return;
	}
	m_alListData.remove(m_iLastSelListHeadRow);
	m_alListData.trimToSize();

	//m_iTotalListHeadNum--; //�б��ͷĿǰ���ڵ�����
	m_iTotalListHeadNum = m_alListData.size();

	if (m_iLastSelListHeadRow > m_iTotalListHeadNum - 1)
		m_iLastSelListHeadRow = m_iTotalListHeadNum - 1; //���ѡ�е��б��ͷ����Ϊ���һ���к�
}

/**
 * �����ߣ�������
 * ���ܣ�ָ��Ϊ�Ǹ���
 * ������
 * ���أ�
 * ���⣺
 * ���ڣ�(2001-12-5 14:31:47)
 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
 * @param sFieldCode java.lang.String
 * @param bcp nc.ui.pub.bill.BillCardPanel
 * @param vo nc.vo.ic.pub.bill.SpecialBillVO
 */
protected void mustNoNegative(
	String sFieldCode,
	int iRow,
	BillCardPanel bcp,
	SpecialBillVO vo) {
	Object oValue = bcp.getBodyValueAt(iRow, sFieldCode);
	if (null == oValue || oValue.toString().trim().length() == 0)
		return;
	UFDouble ufdValue = (UFDouble) oValue;
	ufdValue =
		new UFDouble(
			Math.abs(ufdValue.doubleValue()),
			-1 * bcp.getBodyItem(sFieldCode).getDecimalDigits());
	bcp.setBodyValueAt(ufdValue, iRow, sFieldCode);
	vo.setItemValue(iRow, sFieldCode, ufdValue);
}

/**
 * �����ߣ�����
 * ���ܣ�ѡ����ҵ������
 * ������
 * ���أ�
 * ���⣺
 * ���ڣ�(2001-5-9 9:23:32)
 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
 *
 *
 *
 *
 */
protected void onBizType(ButtonObject bo) {
	    //
		String sCurrentBillNode = "40081002";
		nc.ui.pub.pf.PfUtilClient.childButtonClicked(
			bo,
			m_sCorpID,
			sCurrentBillNode,
			m_sUserID,
			m_sBillTypeCode,
			this);
		if (!nc.ui.pub.pf.PfUtilClient.makeFlag) {
	        //�����Ʒ�ʽ
			if (nc.ui.pub.pf.PfUtilClient.isCloseOK()) {
			    
			    //2005-04-30 �������ɫ����
				InvAttrCellRenderer ficr = new InvAttrCellRenderer();
				ficr.setFreeItemRenderer(getBillCardPanel(), m_voBill);
				//addied by liuzy 2008-07-03 ��Ӧ�̱�ɫ��
				SupplierItemCellRender supplier = new SupplierItemCellRender(getBillCardPanel(),m_voBill);
				supplier.setRenderer("cvendorname");
				
				//result
				SpecialBillVO voRet = (SpecialBillVO) nc.ui.pub.pf.PfUtilClient.getRetVo();
				if(voRet!=null ){
					//��������
					onAdd();
					//��ʾ����
					setTempBillVO(voRet);
					//�������д������
					resetAllInvInfo(voRet);
					//�����������ݳ�ʼ���ݣ���ΪsetTempBillVO����������ˡ�
					setNewBillInitData();
				}

			}
		} else {
	        //���Ƶ���
			onAdd();
		}
}

/**
 * �����ߣ�����
 * ���ܣ�����¼��
 * ������
 * ���أ�
 * ���⣺
 * ���ڣ�(2001-5-9 9:23:32)
 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
 *
 *
 *
 *
 */
protected void onJointAdd(ButtonObject bo) {
	//��ǰ���б���ʽʱ�������л�������ʽ
	//========if (BillMode.List == m_iCurPanel)
	//======	onSwitch();
	nc.ui.pub.pf.PfUtilClient.retAddBtn(m_boAdd, m_sCorpID, m_sBillTypeCode, bo);
	updateButtons();

	try {
	} catch (Exception e) {
		handleException(e);
		nc.ui.pub.beans.MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON","UPPSCMCommon-000059")/*@res "����"*/, e.getMessage());

	}

}

/**
 * �˴����뷽��˵����
 * ��������:���ݱ����еĵ���ID�͵����������������ε��ݡ�
 * ���ߣ�������
 * �������:
 * ����ֵ:
 * �쳣����:
 * ����:(2003-4-22 16:09:14)
 */
public void onJointCheck() {

	if (m_iLastSelListHeadRow >= 0
		&& m_alListData != null
		&& m_alListData.size() > m_iLastSelListHeadRow
		&& m_alListData.get(m_iLastSelListHeadRow) != null) {

		SpecialBillVO voBill = null;
		SpecialBillHeaderVO voHeader = null;
		//�õ�����VO
		voBill = (SpecialBillVO) m_alListData.get(m_iLastSelListHeadRow);
		//�õ����ݱ�ͷVO
		voHeader = voBill.getHeaderVO();

		if (voHeader == null) {
			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill","UPP4008bill-000098")/*@res "û��Ҫ����ĵ��ݣ�"*/);
			return;
		}
		String sBillPK = null;
		String sBillTypeCode = null;
		String sBillCode = null;

		sBillPK = voHeader.getCspecialhid();
		sBillTypeCode = voHeader.getCbilltypecode();
		sBillCode = voHeader.getVbillcode();
		//���sBillPK��sBillTypeCodeΪ�գ�����û�����塣
		if (sBillPK == null || sBillTypeCode == null) {
			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill","UPP4008bill-000099")/*@res "����û�ж�Ӧ���ݣ�"*/);
			return;
		}
		nc.ui.scm.sourcebill.SourceBillFlowDlg soureDlg =
			new nc.ui.scm.sourcebill.SourceBillFlowDlg(
				this,
				sBillTypeCode,
				sBillPK,
				null,
				m_sUserID,
				/*m_sCorpID*/sBillCode);
		soureDlg.showModal();
	}else{
		showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON","UPPSCMCommon-000154")/*@res "û������ĵ��ݣ�"*/);
	}
}

/**
  * �����ߣ�������
  * ���ܣ�ճ���е���β
  * ������
  * ���أ�
  * ���⣺
  * ���ڣ�(2001-5-10 ���� 4:15)
  * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
  */
public void onPasteRowTail() {
	int iRowCount = getBillCardPanel().getBodyPanel().getTableModel().getRowCount();
	getBillCardPanel().pasteLineToTail();
	//���ӵ�����
	int iRowCount1 = getBillCardPanel().getBodyPanel().getTableModel().getRowCount();
	nc.ui.scm.pub.report.BillRowNo.addLineRowNos(getBillCardPanel(), m_sBillTypeCode, m_sBillRowNo, iRowCount1 - iRowCount);
	if (m_voBillItem != null) {
		int row = getBillCardPanel().getBillTable().getRowCount() - m_voBillItem.length;
		voBillPastLine(row);
	}

}

/**
* �����ߣ�������
* ���ܣ���ӡ
* ������
* ���أ�
* ���⣺
* ���ڣ�(2001-5-10 ���� 4:16)
* �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
*/
public void onPreview() {

		long ITime = System.currentTimeMillis();
		filterNullLine();

		SpecialBillVO vo = m_voBill;

		if (m_alListData != null && m_iMode == BillMode.List) {
			if (m_iLastSelListHeadRow == -1)
				vo = (SpecialBillVO) m_alListData.get(0);
			else
				vo = (SpecialBillVO) m_alListData.get(m_iLastSelListHeadRow);
		}
		if (null == vo) {
			vo = new SpecialBillVO();
		}
		if (null == vo.getParentVO()) {
			vo.setParentVO(new SpecialBillHeaderVO());
		}
		if ((null == vo.getChildrenVO())
			|| (vo.getChildrenVO().length == 0)
			|| (vo.getChildrenVO()[0] == null)) {
			SpecialBillItemVO[] ivo = new SpecialBillItemVO[1];
			ivo[0] = new SpecialBillItemVO();
			vo.setChildrenVO(ivo);
		}

        //construct print log info
	    String sBillID = vo.getPrimaryKey();//���������ID
	    ScmPrintlogVO voSpl = new ScmPrintlogVO();
	    voSpl.setCbillid(sBillID);
	    voSpl.setVbillcode(vo.getVBillCode());//���뵥�ݺţ�������ʾ��
	    voSpl.setCbilltypecode(vo.getBillTypeCode());//�������ͱ���
	    voSpl.setCoperatorid((String) (vo.getParentVO().getAttributeValue("coperatorid")) );//����ԱID
	    voSpl.setIoperatetype(new Integer(PrintConst.PAT_OK));//�̶�
	    voSpl.setPk_corp(m_sCorpID);//��˾
	    voSpl.setTs((String)(vo.getParentVO().getAttributeValue("ts")));//���������TS

	    nc.vo.scm.pub.SCMEnv.out("ts=========tata" + voSpl.getTs());
		nc.ui.scm.print.PrintLogClient plc = new nc.ui.scm.print.PrintLogClient();
		plc.setPrintEntry(getPrintEntry());
		//���õ�����Ϣ
		plc.setPrintInfo(voSpl);
		//����ts��printcountˢ�¼���.
		plc.addFreshTsListener(this);
		//���ô�ӡ����
		getPrintEntry().setPrintListener(plc);

		if (getPrintEntry().selectTemplate() < 0)
			return;

		nc.vo.scm.pub.SCMEnv.showTime(ITime, "1:");

		ITime = System.currentTimeMillis();
		getDataSource().setVO(vo);
		nc.vo.scm.pub.SCMEnv.showTime(ITime, "2:");

		ITime = System.currentTimeMillis();
		//���ӡ��������Դ�����д�ӡ
		getPrintEntry().setDataSource(getDataSource());
		nc.vo.scm.pub.SCMEnv.showTime(ITime, "3:");

		ITime = System.currentTimeMillis();
		getPrintEntry().preview();
		nc.vo.scm.pub.SCMEnv.showTime(ITime, "4:");



//	} else { //�б�
//		if (null == m_alListData || m_alListData.size() == 0) {
//			return;
//		}
//		long ITime = System.currentTimeMillis();
//		if (getPrintEntry().selectTemplate() < 0)
//			return;
//		ArrayList alBill = getSelectedBills();
//		if (alBill == null)
//			return;
//		getDataSource().setListVOs(alBill);
//		nc.vo.scm.pub.SCMEnv.showTime(ITime, "List1:");
//
//		ITime = System.currentTimeMillis();
//		getDataSource().setTotalLinesInOnePage(getPrintEntry().getBreakPos());
//		nc.vo.scm.pub.SCMEnv.showTime(ITime, "List2:");
//		//���ӡ��������Դ�����д�ӡ
//		ITime = System.currentTimeMillis();
//		getPrintEntry().setDataSource(getDataSource());
//		getPrintEntry().preview();
//		nc.vo.scm.pub.SCMEnv.showTime(ITime, "List3:");
//	}
}

/**
 * �����ߣ�������
 * ���ܣ�ѹ������VO��������BS�˴���Ĳ���
 * �������ɲ�ѯ���ڵõ�������VO
 * ���أ�ѹ���������VO
 * ���⣺
 * ���ڣ�(2001-8-2 ���� 3:18)
 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
 * @return boolean
 * @param cvo nc.vo.pub.query.QueryConditionVO
 */
//protected ConditionVO[] packConditionVO(ConditionVO[] cvonow) {
//
//	ConditionVO[] cvo= new ConditionVO[cvonow.length];
//	for (int i= 0; i < cvonow.length; i++) {
//		cvo[i]= (ConditionVO) (cvonow[i].clone());
//	}
//
//	ArrayList alcvo= new ArrayList();
//	ConditionVO[] cvoFromAlcvo= null;
//
//	for (int i= 0; i < cvo.length; i++) {
//		//����״̬
//		if (cvo[i].getFieldCode().trim().equals("qbillstatus")) {
//			String sValue= cvo[i].getRefResult().getRefPK().trim();
//			ConditionVO cvonew= (ConditionVO) cvo[i].clone();
//			//���϶��������ı������߼���ϵ�ı���
//			cvonew.setFieldCode("1");
//			cvonew.setOperaCode("=");
//			cvonew.setDataType(1);
//			cvonew.setValue("1");
//			cvonew.setNoRight(true);
//			alcvo.add(cvonew);
//			if (sValue.equals("0")) { //���
//				cvonew= (ConditionVO) cvo[i].clone();
//				cvonew.setLogic(true);
//				cvonew.setNoLeft(false);
//				cvonew.setFieldCode("cauditorid");
//				cvonew.setOperaCode("is not null");
//				cvonew.setNoRight(true);
//				alcvo.add(cvonew);
//				cvonew= (ConditionVO) cvo[i].clone();
//				cvonew.setLogic(true);
//				cvonew.setNoLeft(true);
//				cvonew.setFieldCode("len(rtrim(cauditorid))");
//				cvonew.setOperaCode("<>");
//				cvonew.setDataType(1);
//				cvonew.setValue("0");
//				cvonew.setNoRight(false);
//				alcvo.add(cvonew);
//			} else if (sValue.equals("1")) { //�Ƶ�
//				cvonew= (ConditionVO) cvo[i].clone();
//				cvonew.setLogic(true);
//				cvonew.setNoLeft(false);
//				cvonew.setFieldCode("cauditorid");
//				cvonew.setOperaCode("is null");
//				cvonew.setNoRight(true);
//				alcvo.add(cvonew);
//				cvonew= (ConditionVO) cvo[i].clone();
//				cvonew.setLogic(false);
//				cvonew.setNoLeft(true);
//				cvonew.setFieldCode("len(rtrim(cauditorid))");
//				cvonew.setOperaCode("=");
//				cvonew.setDataType(1);
//				cvonew.setValue("0");
//				cvonew.setNoRight(false);
//				alcvo.add(cvonew);
//			} else if (sValue.equals("2")) { //ȫ��
//				//do nothing
//			}
//			cvonew= (ConditionVO) cvo[i].clone();
//			//���϶��������ı���
//			cvonew.setLogic(true);
//			cvonew.setNoLeft(true);
//			cvonew.setFieldCode("1");
//			cvonew.setOperaCode("=");
//			cvonew.setDataType(1);
//			cvonew.setValue("1");
//			alcvo.add(cvonew);
//			continue;
//		} else {
//			alcvo.add(cvo[i]);
//		}
//	}
//
//	cvoFromAlcvo= new ConditionVO[alcvo.size()];
//	for (int i= 0; i < alcvo.size(); i++) {
//		cvoFromAlcvo[i]= (ConditionVO) alcvo.get(i);
//	}
//
//	return cvoFromAlcvo;
//}

/**
 * �����ߣ����˾�
 * ���ܣ���ѯָ���ĵ��ݡ�
 * ������
billType, ��ǰ��������
billID, ��ǰ����ID
businessType, ��ǰҵ������
operator, ��ǰ�û�ID
pk_corp, ��ǰ��˾ID

* ���� ������vo
* ���� ��
* ���� �� (2001 - 5 - 9 9 : 23 : 32)
* �޸����� �� �޸��� �� �޸�ԭ�� �� ע�ͱ�־ ��
*
*
*
*
*/
protected SpecialBillVO qryBill(
	String pk_corp,
	String billType,
	String businessType,
	String operator,
	String billID) {

	if (billID == null || billType == null || pk_corp == null) {
		nc.vo.scm.pub.SCMEnv.out("no bill param");
		return null;
	}
	SpecialBillVO voRet = null;
	try {
    voRet = this.queryForLinkOper(pk_corp, billType, billID);
		if(voRet==null)
      return null;
		ArrayList<SpecialBillVO> alListData = new ArrayList<SpecialBillVO>();
    alListData.add(voRet);

		//��ʽ��� ��һ�������¼��ʽ��ѯ�������� �޸� hanwei  2003-03-05
			executeLoadFormularAfterQuery(alListData);
			//��ֵ
			voRet = (SpecialBillVO) alListData.get(0);
			//�޸ĸ�����calConvRate
			voRet.calConvRate();


	} catch (Exception e) {
		handleException(e);
		showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill","UPP4008bill-000015")/*@res "��ѯ����"*/ + e.getMessage());
	}
	return voRet;
}

/**
 * �����ߣ����˾�
 * ���ܣ���ѯ���ݵı��壬���ѽ���õ�arraylist
 * ������	int iaIndex[],������alAlldata�е�������
 			String saBillPK[]����pk����
 * ���أ�
 * ���⣺
 * ���ڣ�(2001-5-9 9:23:32)
 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
 *
 *
 *
 *
 */
protected void qryItems(int iaIndex[], String saBillPK[]) {
	if (iaIndex == null || saBillPK == null || iaIndex.length != saBillPK.length) {
		nc.vo.scm.pub.SCMEnv.out("param value ERR.");
		return;
	}
	try {
		QryConditionVO voCond = new QryConditionVO();

		voCond.setIntParam(0, SpecialBillVO.QRY_ITEM_ONLY_PURE);
		voCond.setParam(0, saBillPK);
	
		ArrayList alRetData =
			(ArrayList)  SpecialBillHelper.queryBills(m_sBillTypeCode,voCond );
		if (alRetData == null
			|| alRetData.size() == 0
			|| iaIndex.length != alRetData.size()) {
			nc.vo.scm.pub.SCMEnv.out("ret item value ERR.");
			return;
		}
		//v5:exec formula where query body items.
		//getFormulaBillContainer().formulaBodys(null, alRetData);
		//�޸��ˣ������� �޸�ʱ�䣺2009-5-26 ����04:22:01 �޸�ԭ��ת�ⵥ��ѯʱ�б�״̬�£�������벻��ʾ��
		getFormulaBillContainer().formulaBill(alRetData, null,null);
		//--------------------------------------------
		SpecialBillVO voBill = null;
		for (int i = 0; i < alRetData.size(); i++) {
			//index
			voBill = (SpecialBillVO) m_alListData.get(iaIndex[i]);
			//set value
			if (alRetData.get(i) != null && voBill != null)
				voBill.setChildrenVO(((SpecialBillVO) alRetData.get(i)).getChildrenVO());

		}
	} catch (Exception e) {
		showErrorMessage(e.getMessage());
	}
}

/**
 * �����ߣ����˾�
 * ���ܣ��ڱ�������ʾVO,�����½���״̬updateValue()
 * ������
 * ���أ�
 * ���⣺
 * ���ڣ�(2001-5-9 9:23:32)
 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
 *
 *
 *
 *
 */
public ArrayList qryLastTs(String sBillPK) throws Exception {
	try {
		//query
		ArrayList alFreshRet =
			(ArrayList) SpecialBillHelper.queryInfo( new Integer(QryInfoConst.BILL_STATUS_TS), sBillPK );
		//if (alFreshRet == null || alFreshRet.get(0) == null) {
		//nc.vo.scm.pub.SCMEnv.out("Err,ret");
		//return null;
		//}
		//set
		//ts
		ArrayList alTs=null;
		if (alFreshRet != null
			&& alFreshRet.size() >= 2
			&& alFreshRet.get(1) != null) {
			alTs = (ArrayList) alFreshRet.get(1);

		}
		//first is billstatus
		//sBillStatus = alFreshRet.get(0).toString();
			//third is vbillcode
		if (alFreshRet != null && alFreshRet.size() >= 3) {
			if (m_sCorpID.equals(m_voBill.getVBillCode())) {
				String billcode = (String) alFreshRet.get(2);
				getBillCardPanel().getHeadItem("vbillcode").setValue(billcode);
				m_voBill.setVBillCode(billcode);
				((nc.vo.scm.pub.IBillCode) m_alListData.get(m_iLastSelListHeadRow)).setVBillCode(
					billcode);
			}
		}
	return alTs;
	} catch (Exception e) {
		nc.vo.scm.pub.SCMEnv.error(e);
	}
	return null;
}

/**
 * �����ߣ����˾�
 * ���ܣ��������ô��ID,��������������ݣ��������Ρ��������������������
 * �������кţ����ID
 * ���أ�
 * ���⣺
 * ���ڣ�(2001-5-8 19:08:05)
 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
 *
 *
 *
 *
 */
public void resetAllInvInfo(SpecialBillVO voBill) {
	try {
		if (voBill == null
			|| voBill.getItemVOs() == null
			|| voBill.getItemVOs().length == 0) {
			nc.vo.scm.pub.SCMEnv.out("---- no item ");
			return;
		}

		getBillCardPanel().getBillModel().setNeedCalculate(true);

		SpecialBillItemVO[] voaItem = voBill.getItemVOs();
		//������ֿ�
		ArrayList alInvID = new ArrayList(), alInvID2 = null;
		//����ÿһ�еĴ��ID,������
		for (int i = 0; i < voBill.getItemVOs().length; i++) {
			alInvID2 = new ArrayList();
			alInvID2.add(voaItem[i].getAttributeValue("cinventoryid"));
			alInvID2.add(voaItem[i].getAttributeValue("castunitid"));
			alInvID.add(alInvID2);
		}
		//�������������
		ArrayList alIDs = new ArrayList();
		alIDs.add(alInvID);
		alIDs.add(m_sUserID);
		alIDs.add(m_sCorpID);

		//���������
		//ArrayList alRetInvInfo=
		//(ArrayList) SpecialHBO_Client.queryInfo(
		//new Integer(nc.vo.ic.pub.bill.QryInfoConst.INVS_ASTUOM),
		//alIDs);
		ArrayList alRetInvInfo =
			(ArrayList) SpecialBillHelper.queryInfo( new Integer(QryInfoConst.INVS_ASTUOM), alIDs);
		//���ô������
		if (m_voBill != null && alRetInvInfo != null)
			for (int row = 0; row < alRetInvInfo.size(); row++) {
				nc.vo.scm.ic.bill.InvVO voInv = (InvVO) alRetInvInfo.get(row);
				if (voInv != null) {
					m_voBill.setItemInv(row, voInv);
					//����
					setBodyInvValue(row, voInv);
					//����setBodyInvValue()�����������룬
					try {
						getBillCardPanel().getBillModel().removeTableModelListener(this);
						try {
							//�������
							String sInvCode = null;
							if (voInv.getAttributeValue("cinventorycode") != null)
								sInvCode = voInv.getAttributeValue("cinventorycode").toString();
							//���ñ���VO
							getBillCardPanel().setBodyValueAt(sInvCode, row, "cinventorycode");
							//���ò�����ʾ
							nc.ui.pub.beans.UIRefPane refInv =
								((nc.ui.pub.beans.UIRefPane) getBillCardPanel()
									.getBodyItem("cinventorycode")
									.getComponent());
							if (refInv != null) {
								refInv.setValue(sInvCode);
								refInv.setPK(voInv.getAttributeValue("cinventoryid"));
							}
							//��Ŀ
							String sProjectName = null;
							if (voInv.getAttributeValue("cprojectname") != null)
								sInvCode = voInv.getAttributeValue("cprojectname").toString();
							//���ñ���VO
							getBillCardPanel().setBodyValueAt(sInvCode, row, "cprojectname");
							//���ò�����ʾ
							refInv =
								((nc.ui.pub.beans.UIRefPane) getBillCardPanel()
									.getBodyItem("cprojectname")
									.getComponent());
							if (refInv != null) {
								refInv.setValue(sInvCode);
								refInv.setPK(voInv.getAttributeValue("cprojectid"));
							}
							//��Ŀ�׶�
							String sProjectPhraseName = null;
							if (voInv.getAttributeValue("cprojectphrasename") != null)
								sInvCode = voInv.getAttributeValue("cprojectphrasename").toString();
							//���ñ���VO
							getBillCardPanel().setBodyValueAt(sInvCode, row, "cprojectphrasename");
							//���ò�����ʾ
							refInv =
								((nc.ui.pub.beans.UIRefPane) getBillCardPanel()
									.getBodyItem("cprojectphrasename")
									.getComponent());
							if (refInv != null) {
								refInv.setValue(sInvCode);
								refInv.setPK(voInv.getAttributeValue("cprojectphraseid"));
							}

						} catch (Exception e) {
						}

						getBillCardPanel().getBillModel().addTableModelListener(this);
					} catch (Exception e) {
						nc.vo.scm.pub.SCMEnv.out("--->" + e.getMessage());
					}
				} else
					nc.vo.scm.pub.SCMEnv.out("--->inv info nvl");

			}
		//��β
		//setTailValue(voInv);
		//������/�����������
		//clearRowData(0,row,"cinventorycode");
		//���µ���Ϣ��Ҫ�Ż���������κ�δ��ʾ����������ʾ��
		//showHintMessage("����޸ģ�������ȷ�����Ρ�������");
		getBillCardPanel().getBillModel().setNeedCalculate(true);
		getBillCardPanel().getBillModel().reCalcurateAll();

	} catch (Exception e2) {
		nc.vo.scm.pub.SCMEnv.error(e2);
	}
}

/**
 * ����˵����
 * �������ڣ�(2002-11-21 14:46:22)
 * ���ߣ�����
 * �޸����ڣ�
 * �޸��ˣ�
 * �޸�ԭ��
 * �㷨˵����
 */
//protected void resetConditionVO(nc.vo.pub.query.ConditionVO[] conVO)
//{
//
//	if (conVO != null && conVO.length != 0)
//	{
//		for (int i = 0; i < conVO.length; i++)
//		{
//			if ("like".equals(conVO[i].getOperaCode().trim()) && conVO[i].getFieldCode() != null)
//			{
//				String sFieldCode = conVO[i].getFieldCode().trim();
//				if ("invcl.invclasscode".equals(sFieldCode) && conVO[i].getValue().trim()!=null)
//				{
//					conVO[i].setValue(conVO[i].getValue() + "%");
//				}
//
//				else if (conVO[i].getValue().trim() != null)
//					conVO[i].setValue("%" + conVO[i].getValue() + "%");
//			}
//		}
//	}
//}

/** ���ñ���ť״̬
  * �˴����뷽��˵����
  * �������ڣ�(2001-4-30 13:58:35)
  */
protected void setBillState() {
	switch (m_iMode) {
		case BillMode.New : //����
			getBillCardPanel().setEnabled(true);
			getBillListPanel().setVisible(false);
			getBillCardPanel().setVisible(true);
			break;
		case BillMode.Update : //�޸�
			getBillCardPanel().setEnabled(true);
			getBillListPanel().setVisible(false);
			getBillCardPanel().setVisible(true);
			break;
		case BillMode.Browse : //���
			getBillCardPanel().setEnabled(false);
			getBillListPanel().setVisible(false);
			getBillCardPanel().setVisible(true);
			//�̵㵥��"��������Ϊȡ��γ��"Ĭ�Ͽ��Ա༭���κ������)
			if (getBillCardPanel().getHeadItem("ishsl")!=null){
			BillItem[] headItems = getBillCardPanel().getHeadItems();
			if (headItems!=null){
				for (int i=0;i<headItems.length;i++){
					if (headItems[i].getKey().equals("ishsl"))
						headItems[i].setEnabled(true);
					else
						headItems[i].setEnabled(false);
				}
			}
			}
			break;
		case BillMode.List : //�б�״̬
			getBillCardPanel().setVisible(false);
			getBillListPanel().setVisible(true);
			break;
	}
}

/**
   * �����ߣ����˾�
   * ���ܣ��ڱ�������ʾVO
   * ������
   * ���أ�
   * ���⣺
   * ���ڣ�(2001-5-9 9:23:32)
   * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 2004-7-7  hanwei bExeformula
   * bExeformula:
	 �Ƿ�ִ�й�ʽ��ִ�н��湫ʽЧ��̫�� ,3000��������Ҫ8�룬
	 261�̵�ѡ����Ҫִ�й�ʽ���Լ��������
   *
   *
   *
   */
protected void setBillValueVO(SpecialBillVO bvo, boolean bExeformula) {
	try {
		long ITime = System.currentTimeMillis();
		getBillCardPanel().getBillModel().removeTableModelListener(this);
		getBillCardPanel().removeBillEditListenerHeadTail();
    
    BatchCodeDefSetTool.execFormulaForBatchCode(bvo.getItemVOs());
		getBillCardPanel().setBillValueVO(bvo);
		nc.vo.scm.pub.SCMEnv.showTime(ITime, "setBillValueVO1ʱ�䣺");
		ITime = System.currentTimeMillis();
		//ˢ�±�β
		//setTailValue(0);
		nc.vo.scm.pub.SCMEnv.showTime(ITime, "setBillValueVO2:setTailValueʱ�䣺");
		//ִ�м��ع�ʽ
		ITime = System.currentTimeMillis();
		if (bExeformula)
		{
		getBillCardPanel().getBillModel().getFormulaParse().setNullAsZero(false);
		getBillCardPanel().getBillModel().execLoadFormula();
		}
		nc.vo.scm.pub.SCMEnv.showTime(ITime, "setBillValueVO3:execLoadFormula��");
	} catch (Exception e) {
		nc.vo.scm.pub.SCMEnv.error(e);
	} finally {
		long ITime = System.currentTimeMillis();
		getBillCardPanel().getBillModel().addTableModelListener(this);
		getBillCardPanel().addBillEditListenerHeadTail(this);
		nc.vo.scm.pub.SCMEnv.showTime(ITime, "setBillValueVO4ʱ�䣺");
	}
}

/**
 * �˴����뷽��˵����
   ������ն�ѡ�ý���
   ����̬ת��������������
 * �������ڣ�(2003-11-11 20:48:18)
 * @param invVOs nc.vo.scm.ic.bill.InvVO[]
 */
public void setBodyInVO(InvVO[] invVOs, int iBeginRow) {/*
	int iCurRow = 0;
	getBillCardPanel().getBillModel().setNeedCalculate(false);
	for (int i = 0; i < invVOs.length; i++) {
		iCurRow = iBeginRow + i;
		
		//������
		String[] sIKs = getClearIDs(1, "cinventorycode");
		//���ƻ������Զ�����
		clearRowData(iCurRow, sIKs);
		
		//���ý�������
		m_voBill.setItemInv(iCurRow, invVOs[i]);
		//����
		setBodyInvValue(iCurRow, invVOs[i]);
		//��β
//		setTailValue(iCurRow);

	}

	getBillCardPanel().getBillModel().setNeedCalculate(true);
	getBillCardPanel().getBillModel().reCalcurateAll();

*/
	setBodyInVO(invVOs,iBeginRow,false);
}

/**
 *
 * ��������:�С��иı�ʱ���õ�Ԫ����յ���Ϣ��

 * ���ߣ�wnj
 * �������:
 * ����ֵ:
 * �쳣����:
 * ����:(2003-9-27 15:57:49)

 */
protected void setCellRef(
	int rownow,
	int colnow,
	javax.swing.event.ListSelectionEvent e) {

}

/**
 * �˴����뷽��˵����
 * �������ڣ�(2003-11-11 20:28:48)
 * @param newQuryPlanprice boolean
 */
public void setIsQuryPlanprice(boolean newQuryPlanprice) {
	m_isQuryPlanprice = newQuryPlanprice;
}

/**
 * �˴����뷽��˵����
 * �������ڣ�(2003-11-11 20:28:48)
 * @param newQuryPlanprice boolean
 */
public void setQuryPlanprice(boolean newQuryPlanprice) {
	m_isQuryPlanprice = newQuryPlanprice;
}

/**
 * �����ߣ����˾�
 * ���ܣ����ñ���/��β��С��λ��
 * ������
 * ���أ�
 * ���⣺
 * ���ڣ�(2001-11-23 18:11:18)
 *  �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
 */
protected BillData setScale(BillData bd) {

	//���ǵ�����ģ���ڲ���Ч�ʣ������ó���Ч�ʸ���Щ����Ϊ���������ϴ��ù�ϣ��ʵ��.
	nc.ui.pub.bill.BillItem[] biaCardBody= bd.getBodyItems();
	nc.ui.pub.bill.BillItem[] biaListBody=
		getBillListPanel().getBodyBillModel().getBodyItems();
	//[itemkey,i]
	Hashtable htCardBody= new Hashtable();
	for (int i= 0; i < biaCardBody.length; i++)
		htCardBody.put(biaCardBody[i].getKey(), new Integer(i));
	//[itemkey,i]
	Hashtable htListBody= new Hashtable();
	for (int i= 0; i < biaListBody.length; i++)
		htListBody.put(biaListBody[i].getKey(), new Integer(i));

	//��������
	String[] saBodyNumItemKey=
		{
			"cysl",
			"dshldtransnum",
			"desl",
			"naccountnum",
			"nadjustnum",
			"nchecknum",
			"ztsl",
			"naccountgrsnum",
			"nadjustgrsnum",
			"ndiffgrsnum",
			"ncheckgrsnum","nshldtransgrsnum"};
	for (int k= 0; k < saBodyNumItemKey.length; k++) {
		//����Ǵ���
		if (htCardBody.containsKey(saBodyNumItemKey[k])) {
			biaCardBody[Integer
				.valueOf(htCardBody.get(saBodyNumItemKey[k]).toString())
				.intValue()]
				.setLength(DoubleScale.INT_LENGTH + 1 + m_iaScale[DoubleScale.NUM]);
			biaCardBody[Integer
				.valueOf(htCardBody.get(saBodyNumItemKey[k]).toString())
				.intValue()]
				.setDecimalDigits(m_iaScale[DoubleScale.NUM]);
		}
		//����Ǵ���
		if (htListBody.containsKey(saBodyNumItemKey[k])) {
			biaListBody[Integer
				.valueOf(htListBody.get(saBodyNumItemKey[k]).toString())
				.intValue()]
				.setLength(DoubleScale.INT_LENGTH + 1 + m_iaScale[DoubleScale.NUM]);
			biaListBody[Integer
				.valueOf(htListBody.get(saBodyNumItemKey[k]).toString())
				.intValue()]
				.setDecimalDigits(m_iaScale[DoubleScale.NUM]);
		}
	}
	//���帨����
	String[] saBodyAstNumItemKey=
		{
			"cyfsl",
			"naccountastnum",
			"ncheckastnum",
			"nadjustastnum",
			"nshldtransastnum" };
	for (int k= 0; k < saBodyAstNumItemKey.length; k++) {
		//����Ǵ���
		if (htCardBody.containsKey(saBodyAstNumItemKey[k])) {
			biaCardBody[Integer
				.valueOf(htCardBody.get(saBodyAstNumItemKey[k]).toString())
				.intValue()]
				.setLength(DoubleScale.INT_LENGTH + 1 + m_iaScale[DoubleScale.ASSIST_NUM]);
			biaCardBody[Integer
				.valueOf(htCardBody.get(saBodyAstNumItemKey[k]).toString())
				.intValue()]
				.setDecimalDigits(m_iaScale[DoubleScale.ASSIST_NUM]);
		}
		//����Ǵ���
		if (htListBody.containsKey(saBodyAstNumItemKey[k])) {
			biaListBody[Integer
				.valueOf(htListBody.get(saBodyAstNumItemKey[k]).toString())
				.intValue()]
				.setLength(DoubleScale.INT_LENGTH + 1 + m_iaScale[DoubleScale.ASSIST_NUM]);
			biaListBody[Integer
				.valueOf(htListBody.get(saBodyAstNumItemKey[k]).toString())
				.intValue()]
				.setDecimalDigits(m_iaScale[DoubleScale.ASSIST_NUM]);
		}
	}
	//���嵥��
	String[] saBodyPrice= { "jhdj", "nprice", "nplannedprice" };
	for (int k= 0; k < saBodyPrice.length; k++) {
		//����Ǵ���
		if (htCardBody.containsKey(saBodyPrice[k])) {
			biaCardBody[Integer
				.valueOf(htCardBody.get(saBodyPrice[k]).toString())
				.intValue()]
				.setLength(DoubleScale.INT_LENGTH + 1 + m_iaScale[DoubleScale.PRICE]);
			biaCardBody[Integer
				.valueOf(htCardBody.get(saBodyPrice[k]).toString())
				.intValue()]
				.setDecimalDigits(m_iaScale[DoubleScale.PRICE]);
		}
		//����Ǵ���
		if (htListBody.containsKey(saBodyPrice[k])) {
			biaListBody[Integer
				.valueOf(htListBody.get(saBodyPrice[k]).toString())
				.intValue()]
				.setLength(DoubleScale.INT_LENGTH + 1 + m_iaScale[DoubleScale.PRICE]);
			biaListBody[Integer
				.valueOf(htListBody.get(saBodyPrice[k]).toString())
				.intValue()]
				.setDecimalDigits(m_iaScale[DoubleScale.PRICE]);
		}
	}
	//������---->��ֹ����ģ���޸�ȱʡС������
	String[] saBodyMny= { "je", "jhje", "nmny", "nplannedmny" };
	for (int k= 0; k < saBodyMny.length; k++) {
		//����Ǵ���
		if (htCardBody.containsKey(saBodyMny[k])) {
			biaCardBody[Integer
				.valueOf(htCardBody.get(saBodyMny[k]).toString())
				.intValue()]
				.setLength(DoubleScale.INT_LENGTH + 1 + m_iaScale[DoubleScale.MNY]);
			biaCardBody[Integer
				.valueOf(htCardBody.get(saBodyMny[k]).toString())
				.intValue()]
				.setDecimalDigits(m_iaScale[DoubleScale.MNY]);
		}
		//����Ǵ���
		if (htListBody.containsKey(saBodyMny[k])) {
			biaListBody[Integer
				.valueOf(htListBody.get(saBodyMny[k]).toString())
				.intValue()]
				.setLength(DoubleScale.INT_LENGTH + 1 + m_iaScale[DoubleScale.MNY]);
			biaListBody[Integer
				.valueOf(htListBody.get(saBodyMny[k]).toString())
				.intValue()]
				.setDecimalDigits(m_iaScale[DoubleScale.MNY]);
		}
	}

	//��β����
	String[] saTailNumItemKey=
		{
			"xczl",
			"bkxcl",
			"neconomicnum",
			"nmaxstocknum",
			"nminstocknum",
			"norderpointnum",
			"nsafestocknum" };
	for (int k= 0; k < saTailNumItemKey.length; k++) {
		//����Ǵ���
		try {
			bd.getTailItem(saTailNumItemKey[k]).setLength(DoubleScale.INT_LENGTH + 1 + m_iaScale[DoubleScale.NUM]);
			bd.getTailItem(saTailNumItemKey[k]).setDecimalDigits(m_iaScale[DoubleScale.NUM]);
		} catch (Exception e) {

		}
	}

	//��װ���ã�����Ϊ���Ҿ��� ������2009-11-13
	String[] saHeaderNumItemKey= { "nfixdisassemblymny" };
	for (int k= 0; k < saHeaderNumItemKey.length; k++) {
		//����Ǵ���
		try {
			bd.getHeadItem(saHeaderNumItemKey[k]).setLength(
				DoubleScale.INT_LENGTH + 1 + m_iaScale[DoubleScale.MNY]);
			bd.getHeadItem(saHeaderNumItemKey[k]).setDecimalDigits(m_iaScale[DoubleScale.MNY]);
		} catch (Exception e) {

		}
	}
	for (int k= 0; k < saHeaderNumItemKey.length; k++) {
		//����Ǵ���
		try {
			getBillListPanel().getHeadItem(saHeaderNumItemKey[k]).setLength(
				DoubleScale.INT_LENGTH + 1 + m_iaScale[DoubleScale.NUM]);
			getBillListPanel().getHeadItem(saHeaderNumItemKey[k]).setDecimalDigits(
				m_iaScale[DoubleScale.NUM]);
		} catch (Exception e) {

		}
	}

	//������
	if (htCardBody.containsKey("hsl")) {
		biaCardBody[Integer
			.valueOf(htCardBody.get("hsl").toString())
			.intValue()]
			.setLength(DoubleScale.INT_LENGTH + 1 + m_iaScale[DoubleScale.CONVERT_RATE]);
		biaCardBody[Integer
			.valueOf(htCardBody.get("hsl").toString())
			.intValue()]
			.setDecimalDigits(m_iaScale[DoubleScale.CONVERT_RATE]);
	}
	if (htListBody.containsKey("hsl")) {
		biaListBody[Integer
			.valueOf(htListBody.get("hsl").toString())
			.intValue()]
			.setLength(DoubleScale.INT_LENGTH + 1 + m_iaScale[DoubleScale.CONVERT_RATE]);
		biaListBody[Integer
			.valueOf(htListBody.get("hsl").toString())
			.intValue()]
			.setDecimalDigits(m_iaScale[DoubleScale.CONVERT_RATE]);
	}

	return bd;
}

/**
 * �����ߣ����˾�
 * ���ܣ��ڱ�������ʾVO,�����½���״̬updateValue()
 * ������
 * ���أ�
 * ���⣺
 * ���ڣ�(2001-5-9 9:23:32)
 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
 *
 *
 *
 *
 */
public void setTempBillVO(SpecialBillVO bvo) {

	getBillCardPanel().getBillModel().removeTableModelListener(this);
	//����һ��clone()
	m_voBill = (SpecialBillVO) bvo.clone();
	//��������
	setBillValueVO(bvo);
	//����״̬ ---delete it to support CANCEL
	//getBillCardPanel().updateValue();
	//�����ִ�������
	bvo.clearInvQtyInfo();
	//ѡ�е�һ��
	getBillCardPanel().getBillTable().setRowSelectionInterval(0, 0);
	//ˢ���ִ�����ʾ
	//setTailValue(0);
	//������������
	nc.ui.pub.bill.BillModel bmTemp = getBillCardPanel().getBillModel();
	int iRowCount=0;
	if(bvo.getItemVOs()==null)
		iRowCount=bvo.getItemVOs().length;
	for (int row = 0; row < iRowCount; row++) {
		//������״̬Ϊ����
		if (bmTemp != null)
			bmTemp.setRowState(row, nc.ui.pub.bill.BillModel.ADD);
	}

	getBillCardPanel().getBillModel().addTableModelListener(this);
}

/**
 * �����ߣ����˾�
 * ���ܣ��ڱ�������ʾVO,�����½���״̬updateValue()
 * ������
 * ���أ�
 * ���⣺
 * ���ڣ�(2001-5-9 9:23:32)
 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
 *
 *
 *
 *
 */
public void setTs(int iIndex, ArrayList alTs) throws Exception {
	//update bill in list data ------- security -----------
	SpecialBillVO voListBill = null;
	//����m_voBill,�Զ�ȡ�������ݡ�
	if (iIndex >= 0
		&& m_alListData != null
		&& m_alListData.size() > iIndex
		&& m_alListData.get(iIndex) != null) {
		//���ﲻ��clone(),�ı���m_voBillͬʱ�ı�m_alArrayListSpecialHVO
		voListBill = (SpecialBillVO) m_alListData.get(iIndex);
	}
	if (voListBill != null)
		setTs(voListBill, alTs);
}

/**
 * �����ߣ����˾�
 * ���ܣ��ڱ�������ʾVO,�����½���״̬updateValue()
 * ������
 * ���أ�
 * ���⣺
 * ���ڣ�(2001-5-9 9:23:32)
 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
 *
 *
 *
 *
 */
public void setTs(SpecialBillVO voThisBill, ArrayList alTs) throws Exception {
	if (alTs == null
		|| alTs.size() < 2
		|| alTs.get(0) == null
		|| alTs.get(1) == null)
		throw new Exception(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill","UPP4008bill-000027")/*@res "�����tsΪ�գ�"*/);
	//put ts to a hashtable,KEy=body item,value=ts
	Hashtable htTs = new Hashtable();
	ArrayList alTemp = null;
	//start from 1 first is for head.
	for (int i = 1; i < alTs.size(); i++) {
		alTemp = (ArrayList) alTs.get(i);
		if (alTemp != null
			&& alTemp.size() == 2
			&& alTemp.get(0) != null
			&& alTemp.get(1) != null)
			htTs.put(alTemp.get(0), alTemp.get(1));
	}
	//nc.vo.scm.pub.SCMEnv.out(htTs.toString());
	if (voThisBill != null) {
		voThisBill.setStatus(VOStatus.UNCHANGED);
		voThisBill.getHeaderVO().setTs(alTs.get(0).toString());
		//��������
		int iRowCount = voThisBill.getItemCount();
		SpecialBillItemVO voaItem[] = voThisBill.getItemVOs();
		Object oTempTs = null;
		for (int row = 0; row < iRowCount; row++) {
			if (voaItem[row].getCinventoryid() != null //���ⵥ�п��а���
				&& voaItem[row].getPrimaryKey() != null) {
				oTempTs = htTs.get(voaItem[row].getPrimaryKey());
				if (oTempTs != null) {
					voaItem[row].setTs(oTempTs.toString());
				} else
					nc.vo.scm.pub.SCMEnv.out(
						"-------Err-------frh ts -------" + row + voaItem[row].getPrimaryKey());
			}

		}
	}
}

/**
 * �����ߣ����˾�
 * ���ܣ��ڱ�������ʾVO,�����½���״̬updateValue()
 * ������
 * ���أ�
 * ���⣺
 * ���ڣ�(2001-5-9 9:23:32)
 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
 *
 *
 *
 *
 */
public void setUiTs(ArrayList alTs) throws Exception {
	long lTimes = System.currentTimeMillis();
	if (alTs == null
		|| alTs.size() < 2
		|| alTs.get(0) == null
		|| alTs.get(1) == null)
		throw new Exception(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill","UPP4008bill-000027")/*@res "�����tsΪ�գ�"*/);
	//put ts to a hashtable,KEy=body item,value=ts
	Hashtable htTs = new Hashtable();
	ArrayList alTemp = null;
	for (int i = 1; i < alTs.size(); i++) {
		alTemp = (ArrayList) alTs.get(i);
		if (alTemp != null
			&& alTemp.size() == 2
			&& alTemp.get(0) != null
			&& alTemp.get(1) != null)
			htTs.put(alTemp.get(0), alTemp.get(1));
	}

	getBillCardPanel().getBillModel().setNeedCalculate(false);

	getBillCardPanel().setHeadItem("ts", alTs.get(0).toString());
	//��������
	int iRowCount = getBillCardPanel().getRowCount();
	Object oTempTs = null;
	String sRowPK = null, sInvID = null;
	for (int row = 0; row < iRowCount; row++) {
		//if (voaItem[row].getCinventoryid() != null //���ⵥ�п��а���
		//&& voaItem[row].getPrimaryKey() != null) {
		sRowPK = (String) getBillCardPanel().getBodyValueAt(row, "cspecialbid");
		sInvID = (String) getBillCardPanel().getBodyValueAt(row, "cinventoryid");
		if (sInvID != null && sRowPK != null) {
			oTempTs = htTs.get(sRowPK);
			if (oTempTs != null) {
				getBillCardPanel().setBodyValueAt(oTempTs.toString(), row, "ts");
			} else
				nc.vo.scm.pub.SCMEnv.out("-------Err-------frh ts -------");
		}
	}

	getBillCardPanel().getBillModel().setNeedCalculate(true);
	getBillCardPanel().getBillModel().reCalcurateAll();
	nc.vo.scm.pub.SCMEnv.showTime(lTimes, "setUiTs");

}

/**
* �����ߣ�������
* ���ܣ�������
* ������
* ���أ�
* ���⣺
* ���ڣ�(2001-6-26 ���� 9:32)
* �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
*/
protected void voBillCopyLine(int[] row) {
	if (row != null) {
		m_voBillItem = new SpecialBillItemVO[row.length];
    SpecialBillItemVO uicopyvo = null;
    InvVO tempinvVO = null;
		for (int i = 0; i < row.length; i++) {
			m_voBillItem[i] = (SpecialBillItemVO) (m_voBill.getChildrenVO()[row[i]].clone());
      uicopyvo = (SpecialBillItemVO)getBillCardPanel().getBillModel().getBodyValueRowVO(row[i], SpecialBillItemVO.class.getName());
      uicopyvo = (SpecialBillItemVO)uicopyvo.clone();
      String[] keys = uicopyvo.getAttributeNames();
//    �޸��ˣ������� �޸����ڣ�2007-8-21����02:08:51 �޸�ԭ�򣺸���ʱ��Ҫ��invVO�����ϣ�������Ρ����������������fql002����ճ����ʱ��û�и������������ճ���������κ�������ܱ༭����
      tempinvVO = m_voBillItem[i].getInv();
      SmartVOUtilExt.copyVOByVO(m_voBillItem[i], keys, uicopyvo, keys);
      m_voBillItem[i].setInv(tempinvVO);
		}
	}
	else {
		showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill","UPP4008bill-000025")/*@res "����ѡ���С�"*/);
	}
}

/**
 * �����ߣ�������
 * ���ܣ���Ӧ����ճ��ʱ��m_voBill��ͬ��
 * ������
 * ���أ�
 * ���⣺
 * ���ڣ�(2001-6-26 ���� 9:32)
 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
 * 
 */
protected void voBillPastLine(int row) {
	//Ҫ�����Ѿ������к�ִ��

	if (row >= 0 && m_voBillItem != null) {
		//row = row - m_voaBillItem.length;
		for (int i = 0; i < m_voBillItem.length; i++){

			m_voBill.getChildrenVO()[row + i] = (SpecialBillItemVO) m_voBillItem[i].clone();
			m_voBill.getChildrenVO()[row+i].setAttributeValue(m_sBillRowNo,getBillCardPanel().getBodyValueAt(row+i,m_sBillRowNo));
//�޸��ˣ������� �޸����ڣ�2007-8-21����02:08:51 �޸�ԭ��ճ��ʱ��Ҫ��invVOճ���ϣ�������Ρ����������������fql002����ճ����ʱ��û�и������������ճ���������κ�������ܱ༭����
      InvVO invvo = m_voBill.getItemVOs()[row + i].getInv();
      if(invvo!=null)
        invvo = (InvVO)invvo.clone();
      getBillCardPanel().getBillModel().setValueAt(invvo, row + i,"invvo");
			
		}
	}
}

/* ���� Javadoc��
 * @see nc.ui.scm.print.IFreshTsListener#freshTs(java.lang.String, java.lang.String, java.lang.Integer)
 *
 * ʵ�ִ�ӡ���ts��printcount�ĸ���.
 * @author �۱�
 * ����ʱ��: 2004-12-23
 */
public void freshTs(String sBillID, String sTS, Integer iPrintCount) {
    //fresh ts,iprintcount on model.
	/*
    getBillCardPanel().setHeadItem("ts", sTS);
    getBillCardPanel().setTailItem("iprintcount",iPrintCount);
    if  (m_iLastSelListHeadRow>=0){
     getBillListPanel().getHeadBillModel().setValueAt(sTS,m_iLastSelListHeadRow,"ts");
     getBillListPanel().getHeadBillModel().setValueAt(iPrintCount,m_iLastSelListHeadRow,"iprintcount");
    }
    //fresh ts,iprintcount on card,list
    m_voBill.setHeaderValue("ts",sTS);
    m_voBill.setHeaderValue("iprintcount",iPrintCount);

    if (m_alListData.size()<=0||m_iLastSelListHeadRow<0) {
    		SpecialBillVO voListBill = (SpecialBillVO) m_alListData.get(m_iLastSelListHeadRow);
    		voListBill.setHeaderValue("ts",sTS);
    		voListBill.setHeaderValue("iprintcount",iPrintCount);
    }
    */
	nc.vo.scm.pub.SCMEnv.out("new Ts = " + sTS);
	nc.vo.scm.pub.SCMEnv.out("new iPrintCount = " + iPrintCount);

	if (m_alListData == null || m_alListData.size() == 0)
		return;

	//�жϴ�ӡ��vo�Ƿ����ڻ����У�
	//�ڴ�ӡԤ��״̬��ӡʱ������vo���ܻ��иı䣬����Ҫ�жϣ�
	int index = 0;
	SpecialBillVO voBill = null;
	SpecialBillHeaderVO headerVO = null;
	for(; index < m_alListData.size(); index++){
		voBill = (SpecialBillVO) m_alListData.get(index);
		headerVO = voBill.getHeaderVO();

		//��sBillID����ʱ���Ѿ��ж�sBillID��Ϊnull.
		if (sBillID.equals(headerVO.getPrimaryKey()))
			break;
	}

	if (index == m_alListData.size())  //���ڻ���vo�У�������и��£�
		return;

	//�ڻ���vo��
	headerVO.setAttributeValue("ts", sTS);
	headerVO.setAttributeValue("iprintcount", iPrintCount);

	if (m_iMode == BillMode.List){		//List
		int iPrintColumn = getBillListPanel().getHeadBillModel().getBodyColByKey("ts");
		getBillListPanel().getHeadBillModel().setValueAt(sTS, index, iPrintColumn);
		iPrintColumn = getBillListPanel().getHeadBillModel().getBodyColByKey("iprintcount");
		getBillListPanel().getHeadBillModel().setValueAt(iPrintCount, index, iPrintColumn);
	}else{		//Card
		if (index == m_iLastSelListHeadRow)	{		//���Ϊ��ǰcard��ʾvo.
			getBillCardPanel().setHeadItem("ts", sTS);
			getBillCardPanel().setTailItem("iprintcount", iPrintCount);
		}
	}
}
/**
 * �
 * ���ܣ�
 * ������
 * ���أ�
 * ���⣺
 * ���ڣ�(2005-5-12 11:12:41)
 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
 * 
 * @param voHead nc.vo.ic.pub.bill.SpecialBillHeaderVO
 */
protected void qryCalbodyByWhid(SpecialBillHeaderVO voHead) {

		
}
/* (non-Javadoc)
 * @see nc.ui.pub.bill.BillModelCellEditableController#isCellEditable(boolean, int, java.lang.String)
 */
public boolean isCellEditable(boolean value, int row, String itemkey) {
	if (m_iMode == BillMode.Browse) return false;

	getBillCardPanel().stopEditing();

	boolean isEditable = true;
	String sItemKey = itemkey;
	nc.ui.pub.bill.BillItem biCol = getBillCardPanel().getBodyItem(sItemKey);
	int iRow = row;

	if (sItemKey == null || biCol == null)
		return false;

	//ģ������
	if (!biCol.isEdit()) {

		return false;
	}

	if (m_voBill == null) {
		biCol.setEnabled(false);
		return false;
	}


	//����㲻Ϊ�ֿ����ʱ���������޴�����ֹ��������ֵ
	if (sItemKey.equals("cinventorycode") || sItemKey.equals("cwarehousename")){
		if (sItemKey.equals("cinventorycode")){
			StringBuffer swherewh=new StringBuffer();
			BillItem biWh = getBillCardPanel().getHeadItem("coutwarehouseid");
			if (biWh == null) return true;
			String cwhid = biWh.getValue();
			if (cwhid != null) {
				swherewh.append(" pk_invmandoc in (select cinventoryid from ic_numctl where cwarehouseid='"
								+ cwhid
								+ "' )");
					}
		
			if(m_isWhInvRef&&swherewh.length()>0){
					RefFilter.filtInv(
						biCol,
						m_sCorpID,
						new String[] {swherewh.toString() });
			}	
		
		}
		return true;
	}else{
		if(null == m_voBill.getItemValue(iRow, "cinventoryid")
			|| m_voBill.getItemValue(iRow, "cinventoryid").toString().trim().length() == 0) {
		
		return false;
		}
	}




	InvVO voInv = m_voBill.getItemInv(iRow);


	//������
	if (sItemKey.equals("castunitname")
		|| sItemKey.equals("hsl")
		||sItemKey.equals("nshldtransastnum")
		||sItemKey.equals("nadjustastnum")
		||sItemKey.equals("ncheckastnum")
		||sItemKey.equals("cyfsl") ) {
		if (voInv.getIsAstUOMmgt() == null
			|| voInv.getIsAstUOMmgt().intValue() != 1) {
			isEditable = false;
		}
		//���˸���λ
		else {
			if (sItemKey.equals("castunitname"))
				filterMeas(iRow);
			//�̶������ʲ��ɱ༭
			else if (
				sItemKey.equals("hsl")
					&& m_voBill.getItemValue(iRow, "isSolidConvRate") != null
					&& ((Integer) m_voBill.getItemValue(iRow, "isSolidConvRate")).intValue() == 1) {
				isEditable = false;
			}

		}
	}
	//������
	else if (sItemKey.equals("vfree0")) {
		if (voInv.getIsFreeItemMgt() == null
			|| voInv.getIsFreeItemMgt().intValue() != 1) {
			isEditable = false;
		}
		//�������������
		else {
			//����������մ�������
			getFreeItemRefPane().setFreeItemParam(voInv);

		}
	}

	//����
	else if (sItemKey.equals("vbatchcode")) {
		if (voInv.getIsLotMgt() == null || voInv.getIsLotMgt().intValue() != 1) {
      Object oBatchcode = getBillCardPanel().getBodyValueAt(iRow,"vbatchcode");
      if(!nc.vo.ic.pub.GenMethod.isSEmptyOrNull((String)oBatchcode))
        getBillCardPanel().setBodyValueAt(oBatchcode.toString().trim() + " ", iRow, "vbatchcode");

			isEditable = false;
		}
	}	
	else if (sItemKey.equals("dvalidate") || sItemKey.equals("scrq")) {
		
		//�޸��ˣ������� �޸����ڣ�2007-9-7����10:59:21 �޸�ԭ�򣺸���20070907����л��������������ۺ󣬾����ڴ�����������ⵥ���������ں�ʧЧ�����ǲ��ɱ༭�����ŵ�����ģ�������
/*			isEditable = true;
		
		if (voInv.getIsValidateMgt() == null
			|| voInv.getIsValidateMgt().intValue()!= 1)*/
    if (voInv.getIsLotMgt() == null || voInv.getIsLotMgt().intValue() != 1) {
      if(sItemKey.equals("scrq")){
        isEditable = true;
      }else if (voInv.getIsValidateMgt() == null
          || voInv.getIsValidateMgt().intValue()!= 1){
			  isEditable = true;
      }else
        isEditable = false;
    }else{
      isEditable = false;
    }
	}else if(sItemKey.startsWith("vuserdef")){
		nc.ui.pub.bill.BillItem item = getBillCardPanel().getBodyItem(sItemKey);
 		if(item != null && item.getDataType() == nc.ui.pub.bill.BillItem.USERDEF){
 		   String pk = null;
		   pk = (String)m_voBill.getItemValue(iRow,"pk_defdoc"+sItemKey.substring("vuserdef".length()));
	    if(pk!=null && pk.length()>0&&getBillCardPanel().getBodyValueAt(iRow, sItemKey)!=null);
 			((UIRefPane)item.getComponent()).setPK(pk);
 		}else if(getBillCardPanel().getBodyValueAt(iRow, sItemKey)==null){
 			((UIRefPane)item.getComponent()).setPK(null);
 		}
	}
	return isEditable;
}

private BatchcodeVO getBCVO(String pk_invmandoc,String vbatchcode){
	ConditionVO[] voCons = new ConditionVO[2];
	voCons[0]=new ConditionVO();
	voCons[0].setFieldCode("pk_invbasdoc");
	voCons[0].setValue(pk_invmandoc);
	voCons[0].setLogic(true);
	voCons[0].setOperaCode("=");
	
	voCons[1]=new ConditionVO();
	voCons[1].setFieldCode("vbatchcode");
	voCons[1].setValue(vbatchcode);
	voCons[1].setLogic(true);
	voCons[1].setOperaCode("=");
	BatchcodeVO[] vos = null;
	try {
		vos = BatchcodeHelper.queryBatchcode(voCons,m_sCorpID) ;
	} catch (Exception e) {
		nc.vo.scm.pub.SCMEnv.error(e);
	}
	return (vos==null||vos.length==0)?null:vos[0];
}

/**
 * get
 * �������ڣ�(2001-10-26 14:31:14)
 * @param key java.lang.String
 */
protected ClientUISortCtl getListHeadSortCtl() {
	return m_listHeadSortCtl;
}

/**
 * get
 * �������ڣ�(2001-10-26 14:31:14)
 * @param key java.lang.String
 */
protected ClientUISortCtl getListBodySortCtl() {
	return m_listBodySortCtl;
}

/**
 * get
 * �������ڣ�(2001-10-26 14:31:14)
 * @param key java.lang.String
 */
protected ClientUISortCtl getCardBodySortCtl() {
	return m_cardBodySortCtl;
}

/**
 * ����󴥷���
 * �������ڣ�(2001-10-26 14:31:14)
 * @param key java.lang.String
 */
public void afterSortEvent(boolean iscard,boolean ishead,String key) {

	if(ishead){
		//����Ա�ͷһ����¼���򣬺�����Ҫ ��Ϊm_sortRelaDatasû��ֵ ������ 2009-11-12
		if(m_alListData.size()!=1||getListHeadSortCtl().getRelaSortData(0)!=null){
		   m_alListData = (ArrayList)getListHeadSortCtl().getRelaSortData(0);
		}
	}else{
		if(iscard){
			if(m_voBill!=null){
				SpecialBillItemVO[] itemvos = (SpecialBillItemVO[])
					getCardBodySortCtl().getRelaSortDataAsArray(0);
				m_voBill.setChildrenVO(itemvos);
				if (m_iMode!=BillMode.New && m_iMode!=BillMode.QuickNew && m_iMode!=BillMode.Update && m_alListData != null && m_alListData.size() > m_iLastSelListHeadRow) {
					if(m_voBill.getHeaderVO().getCspecialhid()!=null && m_voBill.getHeaderVO().getCspecialhid().equals(
							((SpecialBillVO) m_alListData.get(m_iLastSelListHeadRow)).getHeaderVO().getCspecialhid()))
						try{
							((SpecialBillVO) m_alListData.get(m_iLastSelListHeadRow)).setChildrenVO(
										(SpecialBillItemVO[])ObjectUtils.serializableClone(itemvos)	);
						}catch(Exception e){
							nc.vo.scm.pub.SCMEnv.error(e);
						}
				}
			}
		}else{
			if(m_alListData!=null && m_alListData.size()>0){
				SpecialBillItemVO[] itemvos = (SpecialBillItemVO[])
					getListBodySortCtl().getRelaSortDataAsArray(0);
				((SpecialBillVO) m_alListData.get(m_iLastSelListHeadRow)).setChildrenVO(
						itemvos);
				if(m_voBill!=null && m_voBill.getHeaderVO().getCspecialhid()!=null &&
						m_voBill.getHeaderVO().getCspecialhid().equals(
						 ((SpecialBillVO) m_alListData.get(m_iLastSelListHeadRow)).getHeaderVO().getCspecialhid())){
					try{
						m_voBill.setChildrenVO(
									(SpecialBillItemVO[])ObjectUtils.serializableClone(itemvos)	);
					}catch(Exception e){
						nc.vo.scm.pub.SCMEnv.error(e);
					}
				}
			}
		}
	}

}

/**
 * ����ǰ������
 * �������ڣ�(2001-10-26 14:31:14)
 * @param key java.lang.String
 */
public void beforeSortEvent(boolean iscard,boolean ishead,String key) {
	clearOrientColor();
	// ����Ǳ�ͷ����
	if (ishead) {
		SCMEnv.out("��ͷ����");
		if (m_alListData == null || m_alListData.size() <= 1) {
			// ˵��û������ı�Ҫ
			return;
		}
		getListHeadSortCtl().addRelaSortData(m_alListData);
		
	} else {
		SCMEnv.out("��������");
		
		if(iscard){
			if(m_voBill!=null)
				getCardBodySortCtl().addRelaSortData(m_voBill.getItemVOs());
		}else{
			if(m_alListData!=null && m_alListData.size()>0)
				getListBodySortCtl().addRelaSortData(((SpecialBillVO) m_alListData
						.get(m_iLastSelListHeadRow)).getItemVOs());
		}
	}
}

/**
 * �б��ͷ����󴥷�,��ǰ�б仯
 * �������ڣ�(2001-10-26 14:31:14)
 * @param key java.lang.String
 */
public void currentRowChange(int newrow){
	if(newrow >=0){
		
		if( m_iLastSelListHeadRow!=newrow){
			m_iLastSelListHeadRow = newrow;
			m_iFirstSelListHeadRow = -1;
			switchBillToList();
			m_iMode = BillMode.List;
			setButtonState();
			setBillState();
		}
	}else{
		if(m_iLastSelListHeadRow<0 || m_iLastSelListHeadRow>=getBillListPanel().getHeadBillModel().getRowCount()){
			m_iLastSelListHeadRow = 0;
			m_iFirstSelListHeadRow = -1;
		}
		switchBillToList();
		m_iMode = BillMode.List;
		setButtonState();
		setBillState();
	}
}

/**
 * ��������:�˳�ϵͳ
 */
public boolean onClosing() {
 //���ڱ༭����ʱ�˳���ʾ    
 if(m_iMode==BillMode.New || m_iMode==BillMode.Update){
	 
	 int iret=MessageDialog.showYesNoCancelDlg(this,null,nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UCH001")/*@res "�Ƿ񱣴����޸ĵ����ݣ�"*/);
	 if(iret==MessageDialog.ID_YES){
	  try{
		 boolean isok= onSave();
		 if(!isok){
			 
			 return false;
		 }
	  }catch(Exception e){
		  
		  return false;
	  }
	  return true;
  }
  else if(iret==MessageDialog.ID_NO){
		
	  return true;
 }else
	 return false;
 }
 return true;
}	

/* (non-Javadoc)
 * @see nc.ui.pub.linkoperate.ILinkQuery#doQueryAction(nc.ui.pub.linkoperate.ILinkQueryData)
 */
public void doQueryAction(ILinkQueryData querydata) {
	if(querydata==null)
		return;
	queryForLinkOper(querydata.getPkOrg(),querydata.getBillType(),querydata.getBillID());
}

/**
 * UI��������-����
 * 
 * @author cch 2006-5-9-11:04:16
 */
public void doApproveAction(ILinkApproveData approvedata){
	if(approvedata==null)
		return;
  queryForLinkOper(approvedata.getPkOrg(),approvedata.getBillType(),approvedata.getBillID());
//	SpecialBillVO voBill = queryForLinkOper(approvedata.getPkOrg(),approvedata.getBillType(),approvedata.getBillID());
//	if(voBill!=null){
//		m_boAuditBill.setEnabled(true);
//		setButtons(new ButtonObject[]{m_boAuditBill});
//	}
}

/* (non-Javadoc)
 * @see nc.ui.pub.linkoperate.ILinkQuery#doQueryAction(nc.ui.pub.linkoperate.ILinkQueryData)
 */
public SpecialBillVO queryForLinkOper(String PkOrg,String billtype,String billid) {
	if(billid==null)
		return null;
	
  ICDataSet datas = nc.ui.ic.pub.tools.GenMethod.queryData("ic_special_h", "cspecialhid", new String[]{"pk_corp"}, 
      new int[]{SmartFieldMeta.JAVATYPE_STRING}, new String[]{billid}, " dr=0 ");
  String cbillpkcorp = datas==null?null:(String)datas.getValueAt(0, 0);
  
  if (cbillpkcorp == null || cbillpkcorp.trim().length()<=0)
    nc.ui.pub.beans.MessageDialog.showHintDlg(this, nc.ui.ml.NCLangRes
        .getInstance().getStrByID("SCMCOMMON",
            "UPPSCMCommon-000270")/* @res "��ʾ" */,
        nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill",
            "UPP4008bill-000062")/* @res "û�з��ϲ�ѯ�����ĵ��ݣ�" */);
  else {
    QueryConditionDlgForBill qrydlg = new QueryConditionDlgForBill(this);
    qrydlg.setTempletID(cbillpkcorp, m_sPNodeCode,m_sUserID, null);
    String swhere = " head.cbilltypecode='" + billtype + "' and head.cspecialhid='"+billid+"' ";
    QryConditionVO qcvo = new nc.vo.ic.pub.bill.QryConditionVO(swhere);
    String[] refcodes = nc.ui.ic.pub.tools.GenMethod.getDataPowerFieldFromDlg(qrydlg,false,null);
    qrydlg.setCorpRefs("head.pk_corp",refcodes);
    ConditionVO[] convos = ICCommonBusi.getDataPowerConsFromDlg(qrydlg, m_sPNodeCode, cbillpkcorp, m_sUserID, refcodes);
    if(convos!=null && convos.length>0){
      qcvo.setParam(
          QryConditionVO.QRY_CONDITIONVO,convos
          );
      String spartwhere = convos[0].getWhereSQL(convos);
      if(spartwhere!=null && spartwhere.trim().length()>0)
        qcvo.setQryCond(swhere+" and "+spartwhere);
    }
    qcvo.setIntParam(0, SpecialBillVO.QRY_WHOLE_PURE);
    loadBillListPanel(qcvo);
  }

	SpecialBillVO voBill = null;
	if(m_alListData!=null && m_alListData.size()>0){
		voBill = (SpecialBillVO)m_alListData.get(0);
	}
	
	onList();
	
	if(voBill!=null){
		String sbill_pk_corp = voBill.getHeaderVO().getPk_corp();
		if(!getClientEnvironment().getCorporation().getPrimaryKey().equals(sbill_pk_corp))
			setButtons(new ButtonObject[]{m_boPrint});
	}

	setButtonState();
	setBillState();
	
	return voBill;
}

/**
 * showBtnSwitch ���Ͻ���淶
 * 
 * @author leijun 2006-5-24
 */
public void showBtnSwitch(){
	if(m_iMode == BillMode.List)
		m_boList.setName(nc.ui.ml.NCLangRes.getInstance().getStrByID(
				"common", "UCH021")/* @res "��Ƭ��ʾ" */);
	else
		m_boList.setName(nc.ui.ml.NCLangRes.getInstance().getStrByID(
				"common", "UCH022")/* @res "�б���ʾ" */);
		
	updateButton(m_boList);
	
}

/**
 * 
 * 
 * @author 
 */
public void setSortEnable(boolean iscard,boolean ishead,boolean isenable){
	if(iscard){
		getBillCardPanel().getBillTable().setSortEnabled(isenable);
		getBillCardPanel().getBillTable().removeSortListener();
	}else{
		if(ishead){
			getBillListPanel().getHeadTable().setSortEnabled(isenable);
			getBillListPanel().getHeadTable().removeSortListener();
		}else{
			getBillListPanel().getBodyTable().setSortEnabled(isenable);
			getBillListPanel().getBodyTable().removeSortListener();
		}
	}
	
}

public void doMaintainAction(ILinkMaintainData maintaindata) {
	if(maintaindata==null)
		return;
  queryForLinkOper(m_sCorpID,m_sBillTypeCode,maintaindata.getBillID());
	
}

private boolean isExistInBatch(String pk_invmandoc,String vbatchcode){
	BatchcodeVO vos = getBCVO(pk_invmandoc,vbatchcode);
	if(vos==null)
		return false;
	else
		return true;
}

/**
 * �����ˣ�������
�������ڣ�2007-12-26����09:27:52
����ԭ���Զ����ý��������е������е��к�
 *
 */
protected void onAddNewRowNo() {

	nc.ui.scm.pub.report.BillRowNo.addNewRowNo(getBillCardPanel(),
			m_sBillTypeCode, m_sBillRowNo);
	
	for (int i = 0; i < getBillCardPanel().getRowCount(); i++) {
		if(getBillCardPanel().getBillModel().getRowState(i)==BillModel.NORMAL)
 			getBillCardPanel().getBillModel().setRowState(i,BillModel.MODIFICATION);
		m_voBill.setItemValue(i, "crowno", getBillCardPanel().getBodyValueAt(i, "crowno"));
	}
}

public boolean isLineCardEdit() {
  return isLineCardEdit;
}

public void setLineCardEdit(boolean isLineCardEdit) {
  this.isLineCardEdit = isLineCardEdit;
}

/**
 * ������ yb
�������ڣ�2007-12-26����09:27:52
����ԭ�򣺿�Ƭ�༭
 *
 */
protected void onLineCardEdit() {

  setLineCardEdit(true);
  boolean ise = getBillCardPanel().getBillModel().getRowEditState();
  try{
    boolean isenableaddrow = false;
    if(m_boAddRow!=null && m_boAddRow.isEnabled())
      isenableaddrow = true;
    if(m_sBillTypeCode!=null && (m_sBillTypeCode.equals(BillTypeConst.m_assembly) || 
        m_sBillTypeCode.equals(BillTypeConst.m_disassembly) ||
        m_sBillTypeCode.equals(BillTypeConst.m_transform) ||
        m_sBillTypeCode.equals(BillTypeConst.m_check) 
        ))
      isenableaddrow = false;
    getBillCardPanel().getBillModel().setRowEditState(!isenableaddrow);
    getBillCardPanel().startRowCardEdit();
  }finally{
    getBillCardPanel().getBillModel().setRowEditState(ise);
    setLineCardEdit(false);
  }
  
}


public void setPageBtnStatus(int iBillCount, int iCur) {
	if (null != m_pageBtn)
		m_pageBtn.setPageBtnStatus(iBillCount,iCur);
}

/**
 * �����ˣ������� ����ʱ�䣺2008-7-16 ����03:43:33 ����ԭ�� ������Ϣ��ʾ������漰��ĳЩ�е����ݣ��б���ɫ��Ϊ��ɫ
 * @param e
 */ 
/* public void setRowColorWhenException(ICException e){
		// ������ɫ
		SetColor.SetTableColor(
				getBillCardPanel().getBillModel(),
				getBillCardPanel().getBillTable(),
				getBillCardPanel(),
				e.getErrorRowNums(),
				m_cNormalColor,
				e.getExceptionColor(m_bRowLocateErrorColor),
				m_bExchangeColor,
				m_bLocateErrorColor,
				e.getHint(),m_bRowLocateErrorColor);
		SetColor.setErrRowColor(getBillCardPanel().getBillTable(), e.getErrorRowNums());
 }*/


public void setSortEnable(boolean isenable) {
  if(!isenable){
    getBillCardPanel().getBillTable().setSortEnabled(false);
    getBillCardPanel().getBillTable().removeSortListener();
  }else{
    getBillCardPanel().getBillTable().setSortEnabled(true);
    getBillCardPanel().getBillTable().addSortListener();
  }
}

/**
 * 
 * ���ݱ���˵���������.
 * @param e ufbill.BillEditEvent
 */
public boolean onEditAction(int action){
  
  if(!isLineCardEdit()) 
    return true;
  
  boolean isSort = getBillCardPanel().getBillTable().isSortEnabled();
  try {
    getBillCardPanel().removeActionListener();
    if (isSort)
      setSortEnable(false);
    getBillCardPanel().getBillModel().setNeedCalculate(false);
  
    switch(action){
      case BillTableLineAction.ADDLINE:
        onAddRow();
        break;
      case BillTableLineAction.INSERTLINE:
        onInsertRow();
        break;  
      case BillTableLineAction.DELLINE:
        onDeleteRow();
        break;
      case BillTableLineAction.COPYLINE:
        onCopyRow();
        break;
      case BillTableLineAction.PASTELINE:
        onPasteRow();
        break;
      case BillTableLineAction.PASTELINETOTAIL:
        onPasteRowTail();
        break;
      case BillTableLineAction.EDITLINE:
        onLineCardEdit();
        break;  
      default:
        return true;
    }
  
  } finally {
    getBillCardPanel().addActionListener(this);
    if (isSort)
      setSortEnable(true);
    getBillCardPanel().getBillModel().setNeedCalculate(true);
  }
  
  return false;
}

public int getM_iMode() {
  return m_iMode;
}


public String getM_sBillTypeCode() {
  return m_sBillTypeCode;
}


public String getM_sCorpID() {
  return m_sCorpID;
}


public String getM_sLogDate() {
  return m_sLogDate;
}


public String getM_sPNodeCode() {
  return m_sPNodeCode;
}


public String getM_sUserID() {
  return m_sUserID;
}


public String getM_sUserName() {
  return m_sUserName;
}


public SpecialBillVO getM_voBill() {
  return m_voBill;
}

/**
 * ����̨������VO���õ�Model�С�
 * 
 * @param voSaved
 */
public void freshVOBySmallVO(SMSpecialBillVO voSaved) {
	// 1.����ˢ��ʱ�����OID����Ϣ��Model
	BillModel bm = getBillCardPanel().getBillModel();
	BillCardPanel bcp = getBillCardPanel();

	GeneralBillUICtl ctl = new GeneralBillUICtl();
	ctl.setBillCardPanelData(bcp, voSaved);
	// 2.ˢ��ʱ�����OID����Ϣ��m_voBill
	m_voBill.setSmallVO(voSaved);
	//����VO״̬
	m_voBill.setStatus(VOStatus.UNCHANGED);
	// 3.�����б�����
	m_alListData.set(m_iLastSelListHeadRow, m_voBill.clone());

}
/**
 * �˴����뷽��˵��
 * ���ߣ�lirr
 * ���ڣ�2009-10-30����03:39:35
 * �޸����� 2009-10-30����03:39:35 �޸��ˣ�lirr �޸�ԭ�� ������ת��ʱ��Ӧ��������Σ�ע�ͱ�־��
	@param invVOs
	@param row
	@param isClearBcode ���Ӳ����Ƿ�������κ� 
 */
public void afterInvMutiEdit(InvVO[] invVOs,Integer row,boolean isClearBcode){
	//��������
	boolean isLastRow=false;
	
	long ITime = System.currentTimeMillis();

	if (invVOs != null && invVOs.length > 1) {
		if(row==getBillCardPanel().getRowCount()-1)
			isLastRow=true;

		for (int i = invVOs.length - 1; i >= 0; i--) {
			if (i < invVOs.length - 1) {
				getBillCardPanel().insertLine();
			}else{
		 		if(getBillCardPanel().getBillModel().getRowState(row)==BillModel.NORMAL)
		 			getBillCardPanel().getBillModel().setRowState(row,BillModel.MODIFICATION);
		 			 		
	 		}
		}
		if(isLastRow){

			nc.ui.scm.pub.report.BillRowNo.addLineRowNos(getBillCardPanel(),
			m_sBillTypeCode,
			m_sBillRowNo,

			invVOs.length);

			}

	//�����к�
	else{ nc.ui.scm.pub.report.BillRowNo.insertLineRowNos(
			getBillCardPanel(),
			m_sBillTypeCode,
			m_sBillRowNo,
			row + invVOs.length - 1,
			invVOs.length - 1);
	}
	}
	ITime = System.currentTimeMillis();
	if(isClearBcode)
		setBodyInVO(invVOs,row,true);
	else
		setBodyInVO(invVOs,row,false);
	
	setTailValue(row);
	SCMEnv.showTime(ITime, "���ý�������:");
	//���µ���Ϣ��Ҫ�Ż���������κ�δ��ʾ����������ʾ��
	showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill","UPP4008bill-000155")/*@res "����޸ģ�������ȷ����������Ρ�������"*/);

	SCMEnv.showTime(ITime, "�����������:");
}
/**
 * �˴����뷽��˵��
 * ���ߣ�lirr
 * ���ڣ�2009-10-30����03:43:50
 * �޸����� 2009-10-30����03:43:50 �޸��ˣ�lirr �޸�ԭ�� ����ת��ʱ��Ӧ��������Σ���ע�ͱ�־��
	@param invVOs
	@param iBeginRow
	@param isClearBcode
 */
public void setBodyInVO(InvVO[] invVOs, int iBeginRow,boolean isClearBcode) {
	int iCurRow = 0;
	getBillCardPanel().getBillModel().setNeedCalculate(false);
	for (int i = 0; i < invVOs.length; i++) {
		iCurRow = iBeginRow + i;
		if(!isClearBcode){
			//������
			String[] sIKs = getClearIDs(1, "cinventorycode");
			//���ƻ������Զ�����
			clearRowData(iCurRow, sIKs);
		}
		//���ý�������
		m_voBill.setItemInv(iCurRow, invVOs[i]);
		//����
		setBodyInvValue(iCurRow, invVOs[i]);
		//��β
//		setTailValue(iCurRow);

	}

	getBillCardPanel().getBillModel().setNeedCalculate(true);
	getBillCardPanel().getBillModel().reCalcurateAll();

}


/**
 * ����״̬��Deleted��ɾ��   ������ 2009-11-24
 * @param smVO
 */
   private void filterDeletedItems(SMSpecialBillVO smVO){
     List<CircularlyAccessibleValueObject> tempList=new ArrayList<CircularlyAccessibleValueObject>();
	 CircularlyAccessibleValueObject[] itemVO =smVO.getChildrenVO();
	 for(int i=0;i<itemVO.length;i++){
		 switch (itemVO[i].getStatus()) {
			case VOStatus.UNCHANGED :
				tempList.add(itemVO[i]);
				break;
			case VOStatus.NEW :
				tempList.add(itemVO[i]);
				break;
			case VOStatus.UPDATED :
				tempList.add(itemVO[i]);
				break;
			case VOStatus.DELETED:
				break;
			default:
				break;
	 }
   }
	 SMSpecialBillItemVO[] newItemVOs=new  SMSpecialBillItemVO[tempList.size()];
	 newItemVOs=tempList.toArray(newItemVOs);
	 smVO.setChildrenVO(newItemVOs);
  }
  
   /**
 * ����������������Ҫ�����������Ĺ��ܡ� ���ģ�建���е�ɾ����
 * <p>
 * <b>examples:</b>
 * <p>
 * ʹ��ʾ��
 * <p>
 * <b>����˵��</b>
 * @param bm
 * <p>
 * @author lirr
 * @time 2009-11-30 ����06:56:51
 */
private void filterDeletedItems(BillModel bm){
       Vector vDeleteRow= bm.getDeleteRow();
       if(vDeleteRow!= null && vDeleteRow.size() > 0)
       vDeleteRow.removeAllElements();
    }
   


}