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
 * 创建者：仲瑞庆
 * 创建日期：2001-04-20
 * 功能：转库单界面
 * 修改日期，修改人，修改原因，注释标志：
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
	public InformationCostVO[] expenseVOs = null;//全局费用信息变量
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
  protected ButtonObject m_boLineCardEdit;//卡片编辑

	protected ButtonObject m_boAuditBill;
	protected ButtonObject m_boCancelAudit;
	protected ButtonObject m_boQuery;
	protected ButtonObject m_boLocate;
	protected ButtonObject m_boPrint;
	protected ButtonObject m_boList;

	protected ButtonObject m_boOut;
	protected ButtonObject m_boIn;

	protected ButtonObject m_boRowQuyQty; //存量查询

	protected ButtonObject m_billMng;
	protected ButtonObject m_billRowMng;
	protected ButtonObject[] m_aryButtonGroup;

	protected BillCardPanel ivjBillCardPanel = null;
	protected BillListPanel ivjBillListPanel = null;
	
	protected ButtonObject m_boBrowse;//浏览
/*	protected ButtonObject m_boTop;//首页
	protected ButtonObject m_boPrevious;//上页
	protected ButtonObject m_boNext;//下页
	protected ButtonObject m_boBottom;//末页
*/	
	// 翻页功能
	protected PageCtrlBtn m_pageBtn;

	protected String m_Title;

	//初始的单据编辑状态---浏览
	protected int m_iMode = BillMode.Browse;
	//确定是否已进行了行复制
	protected boolean m_bCopyRow = false;
	//进入列表状态时头一次选中的列表表头行
	protected int m_iFirstSelListHeadRow = -1;
	//最后选中的列表表头行
	protected int m_iLastSelListHeadRow = -1;
	protected int m_iLastSelCardBodyRow = -1;

	//列表表头目前存在的行数
	protected int m_iTotalListHeadNum;
	//列表表体目前存在的行数
	protected int m_iTotalListBodyNum;
  
  protected UIMenuItem miAddNewRowNo ;
  protected UIMenuItem miLineCardEdit ;






	//表格中的颜色
	protected Color m_cNormalColor = null;
	//交错行显示开关
	protected boolean m_bExchangeColor = false;
	//故障定位显示开关
	protected boolean m_bLocateErrorColor = true;
	//行故障定位显示开关
	protected boolean m_bRowLocateErrorColor = true;

	//常量
	protected  int m_iFirstAddRows = 2;
	protected String m_sBillTypeCode;
	protected String m_sBillCode;


	protected final String m_sNumItemKey = "dshldtransnum"; //表体数量字段名
	protected final String m_sAstItemKey = "nshldtransastnum"; //表体辅数量字段名
	protected final String m_sHeaderTableName = "ic_special_h";



	protected final String m_sLotWarehouseSource = "coutwarehouseid";
	//依使用的自创参照数量建一组参照
	protected FreeItemRefPane m_firpFreeItemRefPane = new FreeItemRefPane();
	protected LotNumbRefPane m_lnrpLotNumbRefPane = new LotNumbRefPane();

	protected int m_iFirstSelectRow = -1;
	protected int m_iFirstSelectCol = -1;

	protected ClientUIInAndOut m_dlgInOut;
	protected SpecialBillVO m_voBill;
	
	protected boolean m_isWhInvRef = false;

	//行复制VO
	protected SpecialBillItemVO[] m_voBillItem;

	protected String m_sCorpID; //公司ID
	protected String m_sUserID; //当前使用者ID
	protected String m_sLogDate; //当前登录日期
	protected String m_sUserName; //当前使用者名称

	protected InvOnHandDialog m_iohdDlg;

	protected InvMeasRate m_voInvMeas = new nc.ui.scm.ic.measurerate.InvMeasRate();

	//protected QueryConditionDlgForBill ivjQueryConditionDlg;

	//是否手工修改单据号
	protected boolean m_bIsEditBillCode = false;
	
	private ClientUISortCtl m_listHeadSortCtl;//列表表头排序控制
	private ClientUISortCtl m_listBodySortCtl;//列表表体排序控制
	private ClientUISortCtl m_cardBodySortCtl;//卡片表体排序控制
  
  private boolean isLineCardEdit ;
  
  protected QueryDlgHelpForSpec m_queryHelp;


	/**
	 * ClientUI 构造子注解。
	 */
	public SpecialBillBaseUI() {
		super();
		//initialize();
	}

/**
  * 创建者：仲瑞庆
  * 功能：当发生数据修改时
  * 参数：e单据编辑事件
  * 返回：如果表头afterEdit,e.getRow为-1. 否则为0...N
  * 例外：
  * 日期：(2001-5-8 19:08:05)
  * 修改日期，修改人，修改原因，注释标志：
  */
public void afterEdit(nc.ui.pub.bill.BillEditEvent e) {

	//自动更新VO中数据
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
					//置显示列
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
					//置显示列
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

	//检查表头行

	if (e.getKey().equals("coutwarehouseid")) { //出库仓库
		afterWhOutEdit(e);
	} else if (e.getKey().equals("cinwarehouseid")) { //入库仓库
		afterWhInEdit(e);
	}else if (e.getKey().equals("coutbsor")) { //入库仓库
		afterBsorEdit(new String[]{"coutbsor","coutbsorname"}, new String[]{"coutdeptid","coutdeptname"});
		
	}else if (e.getKey().equals("cinbsrid")) { //入库仓库
		afterBsorEdit(new String[]{"cinbsrid","cinbsrname"}, new String[]{"cindeptid","cindeptname"});
		
	}
	else if (BillMode.Browse != m_iMode && e.getKey().startsWith("vuserdef")) {// 自定义项处理zhy
		afterDefEdit(e);
	}


	//检查表体行
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
		//同步化VO
		m_voBill.setItemValue(
			rownum,
			m_sBillRowNo,
			getBillCardPanel().getBodyValueAt(rownum, m_sBillRowNo));

	}
}

	/**
	   * 此处插入方法说明。
	   * 创建日期：(2001-3-23 2:02:27)
	   * @param e ufbill.BillEditEvent
	   */
	public void bodyRowChange(nc.ui.pub.bill.BillEditEvent e) {
		int colnow, rownow;

		getBillCardPanel().rememberFocusComponent();
		//响应列表形式下表头选择改变事件。
		if (e.getSource() == this.getBillListPanel().getHeadTable())
		{
			//清除定位，2003-07-21 ydy
			clearOrientColor();

			listSelectionChanged(e);
		}


		if (e.getSource() == this.getBillListPanel().getBodyTable())
			m_iLastSelCardBodyRow= e.getRow();

		if (e.getSource() == this.getBillCardPanel().getBillTable()) {
			rownow= e.getRow();

			m_iLastSelCardBodyRow= rownow;
			
			//显示表尾数据
			setTailValue(rownow);


			//到未行则自动加行
			//if (((BillMode.New == m_iMode) || (BillMode.Update == m_iMode))
				//&& (rownow == getBillCardPanel().getRowCount() - 1)) {
				//this.onAddRow();
			//}
		}
		getBillCardPanel().restoreFocusComponent();
	}

	/**
	 * getTitle 方法注解。
	 */
	public String getTitle() {
		return m_Title;
	}

	/**
	 * 每当部件抛出异常时被调用
	 * @param exception java.lang.Throwable
	 */
	protected void handleException(java.lang.Throwable exception) {

		/* 除去下列各行的注释，以将未捕捉到的异常打印至 stdout。 */
		nc.vo.scm.pub.SCMEnv.out("--------- 未捕捉到的异常 ---------");
		nc.vo.scm.pub.SCMEnv.error(exception);
	}

/**
   * 初始化类。
   */
/* 警告：此方法将重新生成。 */
protected void initialize() {

		//得到环境变量，并保存到成员变量。
		getCEnvInfo();
		initialize(m_sCorpID,m_sUserID,m_sUserName,null,null,m_sLogDate);
	}

	/**
	 * 主入口点 - 当部件作为应用程序运行时，启动这个部件。
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
			nc.vo.scm.pub.SCMEnv.out("nc.ui.pub.ToftPanel 的 main() 中发生异常");
			nc.vo.scm.pub.SCMEnv.error(exception);
		}
	}

/**
 * onButtonClicked 方法注解。
 */
public void onButtonClicked(nc.ui.pub.ButtonObject bo) { //finished
	showHintMessage(bo.getName());
	//父菜单是<新增>
	//清除定位，2003-07-21 ydy
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
 * 创建人：刘家清
创建日期：2008-4-3上午10:48:42 
创建原因：在卡片模式下转向第一张。 
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
 * 创建人：刘家清
创建日期：2008-4-3上午10:48:42
创建原因：在卡片模式下指向前一张 
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
 * 创建人：刘家清
创建日期：2008-4-3上午10:48:42
创建原因：在卡片模式下指向下一张 
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
 * 创建人：刘家清
创建日期：2008-4-3上午10:48:42
创建原因：在卡片模式下指向最后一张 
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
 * 创建人：刘家清
创建日期：2008-4-3上午11:18:46
创建原因：处理翻页。
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
/** 设置表单按钮状态
  * 此处插入方法说明。
  * 创建日期：(2001-4-30 13:58:35)
  */
protected void setButtonState() {
	switch (m_iMode) {
		case BillMode.New : //新增
			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON","UPPSCMCommon-000287")/*@res "新增"*/);

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
		case BillMode.Update : //修改

			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UC001-0000045")/*@res "修改"*/);

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
		case BillMode.Browse : //浏览

			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UC001-0000021")/*@res "浏览"*/);

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
		case BillMode.List : //列表状态

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
			//禁止修改和删除
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
	//使设置生效
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
	  * 返回 BillCardPanel1 特性值 。 * @ return nc.ui.pub.bill.BillCardPanel
	  */
/* 警告：此方法将重新生成。 */
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

			//取得数据源
			//加载模版数据
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
				//改变新参照的长度
				getFreeItemRefPane().setMaxLength(bd.getBodyItem("vfree0").getLength());
				//设置新的参照，要求指出相应的字段名
				bd.getBodyItem("vfree0").setComponent(getFreeItemRefPane()); //表体,自由项
			}
			//--------------------
			if (bd.getBodyItem("vbatchcode") != null) {
				//改变新参照的长度
				getLotNumbRefPane().setMaxLength(bd.getBodyItem("vbatchcode").getLength());
				//设置新的参照，要求指出相应的字段名
				//bd.getHeadItem("pk_corp").setComponent(new LotNumbRefPane());//表头
				bd.getBodyItem("vbatchcode").setComponent(getLotNumbRefPane()); //表体,批次
				//bd.getTailItem("cwhsmanagerid").setComponent(new LotNumbRefPane());//表尾
			}

			//修改自定义项
			bd = changeBillDataByUserDef(getDefHeadVO(),getDefBodyVO(),bd);
			ivjBillCardPanel.setBillData(bd);
			
			bd = BatchCodeDefSetTool.changeBillDataByBCUserDef(m_sCorpID,bd);
			ivjBillCardPanel.setBillData(bd);

			//初始化小数位数，在initSysParam后调用
			bd = setScale(bd);
			//置入数据源
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

			//原来的取得与置入数据源方法
			//ivjBillCardPanelDemand.setBillData(new BillData(getBillTempletVO()));
			//ivjBillCardPanel.loadTemplet("System987679310419");

			//ivjBillCardPanelDemand.getBillData().getHeadItems();
			if (bd.getHeadItem("cbilltypecode") != null)
				m_Title = bd.getHeadItem("cbilltypecode").getName();
			if (ivjBillCardPanel.getTitle() != null
				&& ivjBillCardPanel.getTitle().trim().length() > 0)
				m_Title = ivjBillCardPanel.getTitle();
			//设置单据号是否可编辑
			if (bd.getHeadItem("vbillcode") != null)
				bd.getHeadItem("vbillcode").setEnabled(false);
			/*ivjBillCardPanel.getBillTable().setSelectionMode(
				javax.swing.ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);*/

			//ivjBillCardPanel.setBodyMenuShow(false);
			//ivjBillCardPanel.addEditListener(this);
			//ivjBillCardPanel.addBodyMenuListener(new BillMenuListener());

			////本单位存货
			//(
			//(nc.ui.pub.beans.UIRefPane) ivjBillCardPanel
			//.getBodyItem("cinventorycode")
			//.getComponent())
			//.setWhereString(
			//"bd_invmandoc.pk_corp='" + m_sCorpID + "'");
			//if (packageID.equals("221")) {
			////表头非废品仓库
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
			////表体非废品仓库
			//(
			//(nc.ui.pub.beans.UIRefPane) ivjBillCardPanel
			//.getBodyItem("cwarehouseid")
			//.getComponent())
			//.setWhereString(
			//"gubflag='N' and sealflag='N' and pk_corp='" + m_sCorpID + "'");
			//}
			//zhx new add billrowno
			nc.ui.scm.pub.report.BillRowNo.loadRowNoItem(ivjBillCardPanel,m_sBillRowNo);
			//将原单据模板的表体行隐藏!
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

	//进行自定义项定义用

//	defs= nc.ui.bd.service.BDDef.queryDefVO("供应链/ARAP单据头", m_sCorpID);
	if ((defHead != null)) {
		oldBillData.updateItemByDef(defHead, "vuserdef", true);
		for(int i = 1; i < 20; i++){
  			nc.ui.pub.bill.BillItem item = oldBillData.getHeadItem("vuserdef"+i);
  			if(item != null && item.getDataType() == nc.ui.pub.bill.BillItem.USERDEF){
   				((nc.ui.pub.beans.UIRefPane)item.getComponent()).setAutoCheck(true);
 			 }
		 }
	}
	//表体
	//查得对应于公司的该单据的自定义项设置
//	defs= nc.ui.bd.service.BDDef.queryDefVO("供应链/ARAP单据体", m_sCorpID);
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
	 * 返回 BillListPanel1 特性值。
	 * @return nc.ui.pub.bill.BillListPanel
	 * 
	 */
/* 警告：此方法将重新生成。 */
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
			//加载列表模版
			//ivjBillListPanel.loadTemplet(m_sBillTypeCode, null, m_sUserID, m_sCorpID);

			//BillListData bd= ivjBillListPanel.getBillListData();
			BillListData bd= new BillListData(getDefaultTemplet());
			bd= changeBillListDataByUserDef(bd);
			
			bd= BatchCodeDefSetTool.changeBillListDataByBCUserDef(m_sCorpID,bd);

			ivjBillListPanel.setListData(bd);
      
      //ivjBillListPanel.setMultiSelect(false);

//			ivjBillListPanel.getHeadTable().setSelectionMode(
//				javax.swing.ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
			//修改人：刘家清 修改日期：2007-9-3下午01:48:20 修改原因：更新新方法，要不然后不统计
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
   * 新增
   * 创建日期：(2001-4-18 19:45:17)
   */
public void onAdd() { //finished
	if (m_iMode == BillMode.Browse) {
	} else {
		onList();
	
	}
	
	
	getBillCardPanel().getBillModel().clearBodyData();
	//加入新的表
	getBillCardPanel().addNew();

	//是否需加入？
	getBillCardPanel().updateValue();

	m_voBill = new SpecialBillVO();
	for (int i = 1; i <= m_iFirstAddRows; i++) {
		onAddRow();
	}

	m_iMode = BillMode.New;
	setButtonState();
	setBillState();

	//设置新增单据的初始数据，如日期，制单人等。
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
	//设置单据号是否可编辑
	if (getBillCardPanel().getHeadItem("vbillcode") != null)
		getBillCardPanel().getHeadItem("vbillcode").setEnabled(m_bIsEditBillCode);

	getBillCardPanel().setTailItem("iprintcount", new Integer(0));
	getBillCardPanel().transferFocusTo(nc.ui.pub.bill.BillCardPanel.HEAD);
	
	//2005-04-29 自由项变色龙。
	InvAttrCellRenderer ficr = new InvAttrCellRenderer();
	ficr.setFreeItemRenderer(getBillCardPanel(), m_voBill);
	//addied by liuzy 2008-07-03 供应商变色龙
	SupplierItemCellRender supplier = new SupplierItemCellRender(getBillCardPanel(),m_voBill);
	supplier.setRenderer("cvendorname");

}

	/**
	* 增行
	* 创建日期：(2001-4-18 19:46:27)
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
	  * 取消
	  * 创建日期：(2001-4-18 19:47:41)
	  */
	public void onCancel() { //finished
		
		switch (nc.ui.pub.beans.MessageDialog.showYesNoDlg(this,
				null,
				nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
				"UCH067")/* @res "是否确定要取消？" */
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
		
		//2005-01-28 自由项变色龙。
		InvAttrCellRenderer ficr = new InvAttrCellRenderer();
		ficr.setFreeItemRenderer(getBillCardPanel(), null);
		//addied by liuzy 2008-07-03 供应商变色龙
		SupplierItemCellRender supplier = new SupplierItemCellRender(getBillCardPanel(),m_voBill);
		supplier.setRenderer("cvendorname");
	}
	
	public void clearBCOnCancel() {
		
	}

/**
  * 修改
  * 创建日期：(2001-4-18 19:45:39)
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

	//修改单据编码前进行保存
	m_sOldBillCode= getBillCardPanel().getHeadItem("vbillcode").getValue();
	//设置单据号是否可编辑
	//修改人：刘家清 修改时间：2008-7-14 下午03:38:48 修改原因：单据号在单据修改时，普通单和特殊单都可以进行修改。
	/*if (getBillCardPanel().getHeadItem("vbillcode") != null)
		getBillCardPanel().getHeadItem("vbillcode").setEnabled(false);*/

	if (m_sOldBillCode != null)
		m_sOldBillCode= m_sOldBillCode.trim();

	getBillCardPanel().transferFocusTo(nc.ui.pub.bill.BillCardPanel.HEAD);
	
	//2005-04-30 自由项变色龙。
	InvAttrCellRenderer ficr = new InvAttrCellRenderer();
	ficr.setFreeItemRenderer(getBillCardPanel(), m_voBill);
	//addied by liuzy 2008-07-03 供应商变色龙
	SupplierItemCellRender supplier = new SupplierItemCellRender(getBillCardPanel(),m_voBill);
	supplier.setRenderer("cvendorname");
	
	setUpdateBillInitData();

}
/**
 * 创建者：cqw 功能：设置修改单据的初始数据 参数： 返回： 例外： 日期：(2005-04-04 19:23:32)
 * 修改日期，修改人，修改原因，注释标志：
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
  * 创建者：仲瑞庆
  * 功能：拷贝单据
  				累计转入/出数量不应被复制
  * 参数：
  * 返回：
  * 例外：
  * 日期：(2001-5-10 下午 4:15)
  * 修改日期，修改人，修改原因，注释标志：
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
//	加入从刚才的表单中复制的数据
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
		//来源单据数据，
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
	//设置单据号是否可编辑
	if (getBillCardPanel().getHeadItem("vbillcode") != null)
		getBillCardPanel().getHeadItem("vbillcode").setEnabled(m_bIsEditBillCode);

	getBillCardPanel().getTailItem("iprintcount").setValue(new Integer(0));

	firstSetColEditable();
	
	//2005-04-30 自由项变色龙。
	InvAttrCellRenderer ficr = new InvAttrCellRenderer();
	ficr.setFreeItemRenderer(getBillCardPanel(), m_voBill);
	//addied by liuzy 2008-07-03 供应商变色龙
	SupplierItemCellRender supplier = new SupplierItemCellRender(getBillCardPanel(),m_voBill);
	supplier.setRenderer("cvendorname");
}

/**
  * 创建者：仲瑞庆
  * 功能：拷贝行
  * 参数：
  * 返回：
  * 例外：
  * 日期：(2001-5-10 下午 2:50)
  * 修改日期，修改人，修改原因，注释标志：
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
		showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill","UPP4008bill-000025")/*@res "请先选中行。"*/);
	}
}

/**
  * 删除
  * 创建日期：(2001-4-18 19:46:01)
  */
public void onDelete() {
	if (m_alListData == null || m_alListData.size() == 0) {

		return;
	}

	try {
		
		switch (nc.ui.pub.beans.MessageDialog.showYesNoDlg(this,
				null,
				nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
				"UCH002")/* @res "是否确认要删除？" */
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
          
          //add by liuzy 2007-11-02 10:16 压缩数据
          ObjectUtils.objectReference(vo);
          
					nc.ui.pub.pf.PfUtilClient.processAction(
						"DELETE",
						m_sBillTypeCode,
						m_sLogDate,
						vo);

					minusBillVO();
					showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill","UPP4008bill-000010")/*@res "删除成功！"*/);
					m_iFirstSelListHeadRow = -1;
					switchListToBill();
				} else {
					//ArrayList alRowNumbers = new ArrayList();
					int[] iSelectedRows=getBillListPanel().getHeadTable().getSelectedRows();
					if(iSelectedRows!=null&&iSelectedRows.length!=1){
						showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill","UPP4008bill-000144")/*@res "请选中一张单据删除！"*/);
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
            
            //add by liuzy 2007-11-02 10:16 压缩数据
            ObjectUtils.objectReference(vo);

						nc.ui.pub.pf.PfUtilClient.processAction(
							"DELETE",
							m_sBillTypeCode,
							m_sLogDate,
							vo);

						//m_iLastSelListHeadRow = Integer.parseInt(alRowNumbers.get(i).toString());
						minusBillVO();
					//}
					showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill","UPP4008bill-000010")/*@res "删除成功！"*/);
					m_iFirstSelListHeadRow = -1;
					switchBillToList();
				}
				setButtonState();
				setBillState();
				break;
		default:return;	
		}
	} catch (Exception e) {
		nc.vo.scm.pub.SCMEnv.out("数据通讯失败！");
		GenMethod.handleException(this, null, e);
		//handleException(e);
		//nc.ui.pub.beans.MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON","UPPSCMCommon-000059")/*@res "错误"*/, e.getMessage());
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
	  * 删行
	  * 创建日期：(2001-4-18 19:46:41)
	  */
	public void onDeleteRow() { //finished
		//switch (nc.ui.pub.beans.MessageDialog.showOkCancelDlg(this, "提示", "你确定删除吗？")) {
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
	* 关联录入
	* 创建日期：(2001-4-18 19:48:21)
	*/
	public void onJointAdd() {
		//m_iMode= BillMode.New;
		setButtonState();
		setBillState();
		//加入调出所关联的表的选择,并传回数据
	}

/** 列表\表单状态切换
  * 此处插入方法说明。
  * 创建日期：(2001-4-18 19:48:05)
  */
public void onList() { //finished
	if (m_iMode == BillMode.Browse) {
		m_iMode = BillMode.List;
		switchBillToList();
	} else {
		m_iMode = BillMode.Browse;
		switchListToBill();
		//2010-11-08 MeiChao begin 当卡片切换时,将费用信息更新至卡片页面
		if(this.getBillCardPanel().getBillModel("jj_scm_informationcost")!=null){
			this.getBillCardPanel().getBillModel("jj_scm_informationcost")
			.setBodyDataVO(expenseVOs);
			this.getBillCardPanel().updateUI();
		}
		//2010-11-08 MeiChao end 当卡片切换时,将费用信息更新至卡片页面
	}
	m_iFirstSelListHeadRow = m_iLastSelListHeadRow;
	showBtnSwitch();
	setButtonState();
	setBillState();
}

/**
 * 创建者：王乃军
 * 功能：定位单据
 * 参数：
 * 返回：
 * 例外：
 * 日期：(2001-5-9 9:23:32)
 * 修改日期，修改人，修改原因，注释标志：
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
  * 创建者：仲瑞庆
  * 功能：粘贴行
  * 参数：
  * 返回：
  * 例外：
  * 日期：(2001-5-10 下午 4:15)
  * 修改日期，修改人，修改原因，注释标志：
  */
public void onPasteRow() {
  
  try{
	//finished added by zhx bill row no
	int iRowCount = getBillCardPanel().getBodyPanel().getTableModel().getRowCount();
	int row = getBillCardPanel().getBillTable().getSelectedRow();
	if (row < 0 ) {
		showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill","UPP4008bill-000145")/*@res "请先选中粘贴行的位置。"*/);
	} else {
    
    if(m_voBillItem==null || m_voBillItem.length<=0)
      return;
    
		getBillCardPanel().pasteLine();
    
//    int istartrow = getBillCardPanel().getBillTable().getSelectedRow() - m_voBillItem.length;
//    for(int i=0;i<m_voBillItem.length;i++)
//      getBillCardPanel().getBillModel().setBodyRowVO((SpecialBillItemVO)m_voBillItem[i].clone(), istartrow+i);
		//增加的行数
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
* 创建者：仲瑞庆
* 功能：打印
* 参数：
* 返回：
* 例外：
* 日期：(2001-5-10 下午 4:16)
* 修改日期，修改人，修改原因，注释标志：
*/
public void onPrint() {
//  调出打印窗口
	//依当前是列表还是表单而定打印内容
	if (m_iMode == BillMode.Browse) { //浏览
		filterNullLine();
		//准备数据
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

		String sBillID = vo.getPrimaryKey();//单据主表的ID
	    ScmPrintlogVO voSpl = new ScmPrintlogVO();
	    voSpl.setCbillid(sBillID);
	    voSpl.setVbillcode(vo.getVBillCode());//传入单据号，用于显示。
	    voSpl.setCbilltypecode(vo.getBillTypeCode());//单据类型编码
	    voSpl.setCoperatorid((String) (vo.getParentVO().getAttributeValue("coperatorid")) );//操作员ID
	    voSpl.setIoperatetype(new Integer(PrintConst.PAT_OK));//固定
	    voSpl.setPk_corp(m_sCorpID);//公司
	    voSpl.setTs((String)(vo.getParentVO().getAttributeValue("ts")));//单据主表的TS

	    nc.vo.scm.pub.SCMEnv.out("ts=========tata" + voSpl.getTs());
		nc.ui.scm.print.PrintLogClient plc = new nc.ui.scm.print.PrintLogClient();
		plc.setPrintEntry(getPrintEntry());
		//设置单据信息
		plc.setPrintInfo(voSpl);
		//设置ts和printcount刷新监听.
		plc.addFreshTsListener(this);
		//设置打印监听
		getPrintEntry().setPrintListener(plc);

		//向打印置入数据源，进行打印
		getDataSource().setVO(vo);
		getPrintEntry().setDataSource(getDataSource());
		getPrintEntry().print();

	} else { //列表
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
		plc.setBatchPrint(true);//设置是批打
		PrintDataInterface ds = null;
		//设置打印监听
		getPrintEntry().setPrintListener(plc);
		plc.setPrintEntry(getPrintEntry());

		//设置TS刷新监听.
		plc.addFreshTsListener(this);
		//打印操作
		try{
		    getPrintEntry().beginBatchPrint();
		    for (int i = 0; i < alBill.size(); i++) {
		        vo = (SpecialBillVO)alBill.get(i);

		        ScmPrintlogVO voSpl = new ScmPrintlogVO();
			    voSpl.setCbillid(vo.getPrimaryKey());
			    voSpl.setVbillcode(vo.getVBillCode());//传入单据号，用于显示。
			    voSpl.setCbilltypecode(vo.getBillTypeCode());//单据类型编码
			    voSpl.setCoperatorid((String) (vo.getParentVO().getAttributeValue("coperatorid")) );//操作员ID
			    voSpl.setIoperatetype(new Integer(PrintConst.PAT_OK));//固定
			    voSpl.setPk_corp(m_sCorpID);//公司
			    voSpl.setTs((String)(vo.getParentVO().getAttributeValue("ts")));//单据主表的TS

			    nc.vo.scm.pub.SCMEnv.out("ts=========tata" + voSpl.getTs());
			    //设置单据信息
				plc.setPrintInfo(voSpl);

				if (plc.check()) {//检查通过才执行打印，有错误的话自动插入打印日志，这里不用处理。
				     ds = getNewDataSource();
				     ds.setVO(vo);
				     getPrintEntry().setDataSource(ds);

				     //常量定义在Setup（很小）中，在现场很容易定制它。
//				     while (getPrintEntry().dsCountInPool() > PrintConst.PL_MAX_TAST_NUM) {
//				         Thread.currentThread().sleep(PrintConst.PL_SLEEP_TIME); //如果有PL_MAX_TAST_NUM个以上任务，就等待PL_SLEEP_TIME秒。
//				     }
				 }
		    }
		    getPrintEntry().endBatchPrint();

		} catch (Exception e) {
			nc.vo.scm.pub.SCMEnv.error(e);
			showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill","UPP4008bill-000051")/*@res "打印出错！\n"*/ + e.getMessage());
		}
	}
	showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON","UPPSCMCommon-000133")/*@res "就绪"*/);
}


protected QueryDlgHelpForSpec getQueryHelp() {
  if(this.m_queryHelp==null){
    this.m_queryHelp = new QueryDlgHelpForSpec(this);
  }
  return this.m_queryHelp;
}
/**
  * 创建者：仲瑞庆
  * 功能：查询
  * 参数：
  * 返回：
  * 例外：
  * 日期：(2001-5-10 下午 2:57)
  * 修改日期，修改人，修改原因，注释标志：
  */
public void onQuery() {

	//getConditionDlg().hideNormal();
  try{

  	//String sWhereClause = "";
    QryConditionVO qcvo = getQueryHelp().getQryConditionVOForQuery(false);
  	if (qcvo != null) {
  
  //		nc.vo.pub.query.ConditionVO[] voCons = getConditionDlg().getConditionVO();
  //		//处理单据状态，将其转化为SQL条件。
  //		voCons = packConditionVO(voCons);
  //		//处理包含的情况。
  //		resetConditionVO(voCons);
  //		//得到查询条件。
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
  			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill","UPP4008bill-000146")/*@res "共查到"*/ + m_iTotalListHeadNum + nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill","UPP4008bill-000147")/*@res "张单据！"*/);
  		else
  			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill","UPP4008bill-000013")/*@res "未查到符合条件的单据。"*/);
  
  	}
  	showBtnSwitch();
  }catch(Exception e){
    GenMethod.handleException(this, null, e);
  }
}

/**
  * 保存
  * 创建日期：(2001-4-18 19:47:08)
  */
public  boolean onSave() {

	try {
		if (m_iLastSelListHeadRow < 0) { //未选择任何表，直接新增时；或者查询结果为空时；会发生
			m_iLastSelListHeadRow = 0; //最后选中的列表表头行
			//m_iLastSelListbodyRow = -1; //最后选中的列表表体行
			m_iTotalListHeadNum = 0; //列表表头目前存在的行数
			//m_iTotalListBodyNum = 0; //列表表体目前存在的行数
		}
//		getBillCardPanel().getBillData().execBodyValidateFormulas();
		   if(!getBillCardPanel().getBillData().execValidateFormulas())
	           return false;


		//填入界面中数据
		getBillCardPanel().tableStopCellEditing();
		getBillCardPanel().stopEditing();

		//滤掉所有的空行
		filterNullLine();
		if (getBillCardPanel().getRowCount() == 0) {
			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill","UPP4008bill-000148")/*@res "无数据，请重新输入..."*/);
			return false;
		}
		//added by zhx 030626 检查行号的合法性; 该方法应放在过滤空行的后面。
		if (!nc.ui.scm.pub.report.BillRowNo.verifyRowNosCorrect(getBillCardPanel(), m_sBillRowNo)) {
			return false;
		}

		//加入单据号
		//String sBillCode=null;
		//if (BillMode.New == m_iMode) {
			//m_voBill.setVBillCode(m_sBillTypeCode);
			////if (!GeneralMethod
				////.setBillCode(m_voBill, m_sBillTypeCode, getBillCardPanel())) {
			//m_voBill.setVBillCode(null);
			//sBillCode=GeneralMethod.setBillCode((nc.vo.scm.pub.IBillCode)m_voBill);
			//if(sBillCode==null){
				//nc.ui.pub.beans.MessageDialog.showErrorDlg(this, "错误", "获得单据号失败！");
				//return;
			//}
			//if(getBillCardPanel().getHeadItem("vbillcode")!=null)
				//getBillCardPanel().getHeadItem("vbillcode").setValue(sBillCode);
		//}

		SpecialBillVO voNowBill = null;
		voNowBill = getBillVO();
		voNowBill.getParentVO().setAttributeValue("cbilltypecode", m_sBillTypeCode);
		//同步最大化VO
		//voNowBill--->>>m_voBill
		m_voBill.setIDItems(voNowBill);
		voNowBill.setHeaderValue("fbillflag", m_voBill.getHeaderValue("fbillflag"));
		voNowBill.setHeaderValue("icheckmode", m_voBill.getHeaderValue("icheckmode"));
		voNowBill.setHeaderValue("pk_calbody_in",m_voBill.getHeaderValue("pk_calbody_in"));
		voNowBill.setHeaderValue("pk_calbody_out",m_voBill.getHeaderValue("pk_calbody_out"));
	
		m_voBill.setHeaderValue(
			"fassistantflag",
			voNowBill.getHeaderValue("fassistantflag"));

		//VO校验
		if (!checkVO()) {
			return false;
		}
		String sHPK = null; //bill pk
		int iRowCount = getBillCardPanel().getRowCount();
		SpecialBillItemVO[] voaItem = null;	
		
		// 修改或者保存后返回的小VO
		SMSpecialBillVO voSM = null;
		
		if (BillMode.New == m_iMode) {
			//新增
			//向m_shvoBillSpecialHVO中写入FreeVO中增加的值
			voNowBill.setChildrenVO((SpecialBillItemVO[]) getChangedItemVOs());

			//得到当前的ItemVO
			voaItem = voNowBill.getItemVOs();
			//临时HVO[]
			SpecialBillVO[] voTempBill = new SpecialBillVO[1];
			voTempBill[0] = voNowBill;
			//写入,并返回PKs
			//重置单据行号zhx 0630:
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
			voNowBill.getHeaderVO().setCoperatoridnow(m_sUserID);//当前操作员
			
      //add by liuzy 2007-11-02 10:16 压缩数据
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
			} //显示提示信息
			if (alsPrimaryKey.get(0) != null)
				showErrorMessage((String) alsPrimaryKey.get(0));
			ArrayList alMyPK = (ArrayList) alsPrimaryKey.get(1);
			sHPK = alMyPK.get(0).toString();
			
			voSM = (SMSpecialBillVO) alsPrimaryKey
			.get(alsPrimaryKey.size() - 1);
			
			//写入HHeaderVO的PK
			//voNowBill.getParentVO().setPrimaryKey(sHPK);
			//m_voBill.getParentVO().setPrimaryKey(sHPK);
			//写入HItemVO的PK与对应表头PK
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
			
			//同步最大化VO
			//m_voBill.setIDItems(voNowBill);
      
      m_voBill.setIsYetExecBatchFormulas(false);
			
			BatchCodeDefSetTool.execFormulaForBatchCode(m_voBill.getChildrenVO());
			
			//增加HVO
			m_iLastSelListHeadRow = m_iTotalListHeadNum;
			addBillVO();
		} else if (BillMode.Update == m_iMode) { //修改
			//从界面中获得需要的数据
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
			} //向m_shvoBillSpecialHVO中写入FreeVO中增加的值
			voNowBill.setChildrenVO((SpecialBillItemVO[]) getChangedItemVOs());
			//临时表体VO，全部HIVO
			SpecialBillItemVO[] voaTempItem =
				(SpecialBillItemVO[]) getBillVO().getChildrenVO();
			//得到当前的ItemVO
			voaItem = (SpecialBillItemVO[]) voNowBill.getChildrenVO();

			//临时HVO[]
			SpecialBillVO[] m_voTempBill = new SpecialBillVO[1];
			m_voTempBill[0] = voNowBill;
			//写入,并返回PKs
			//重置单据行号zhx 0630:
			if (iRowCount > 0 && voNowBill.getChildrenVO() != null) {
				if (getBillCardPanel().getBodyItem(m_sBillRowNo) != null)
					for (int i = 0; i < iRowCount; i++) {

						voNowBill.setItemValue(
							i,
							m_sBillRowNo,
							getBillCardPanel().getBodyValueAt(i, m_sBillRowNo));

					}
			}
			
			voNowBill.getHeaderVO().setCoperatoridnow(m_sUserID);//当前操作员

      
      //add by liuzy 2007-11-02 10:16 压缩数据
      ObjectUtils.objectReference(voNowBill);
			
//    修改人：刘家清 修改时间：2008-8-15 下午02:27:03 修改原因：后台判断单据号有没有修改用。
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
			} //显示提示信息
			if (alsPrimaryKey.get(0) != null)
				showErrorMessage((String) alsPrimaryKey.get(0));
			ArrayList alMyPK = (ArrayList) alsPrimaryKey.get(1);
			sHPK = voNowBill.getParentVO().getPrimaryKey();

			voSM = (SMSpecialBillVO) alsPrimaryKey
					.get(alsPrimaryKey.size() - 1);
			filterDeletedItems(voSM);
			
			//写入HItemVO的PK与对应表头PK,删去多余的ItemVO
			ArrayList alItemVO = new ArrayList();
			//加入旧表体中没有改变的ItemVO
			//适应平台的修改,总是返回第一个是表头的PK		2001/09/26
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
							SCMEnv.out("保存时出现行对应不上现象，请程序员检查...");
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
			
			//同步最大化VO
			//m_voBill.setIDItems(voNowBill);
      
      m_voBill.setIsYetExecBatchFormulas(false);
			
			BatchCodeDefSetTool.execFormulaForBatchCode(m_voBill.getChildrenVO());
			
			//修改HVO
			//m_alListData.set(m_iLastSelListHeadRow, m_voBill.clone());
		}
		// 将后台信息更新到界面
		freshVOBySmallVO(voSM);
		
		//2005-04-30 自由项变色龙。
		InvAttrCellRenderer ficr = new InvAttrCellRenderer();
		ficr.setFreeItemRenderer(getBillCardPanel(), null);
		//addied by liuzy 2008-07-03 供应商变色龙
		SupplierItemCellRender supplier = new SupplierItemCellRender(getBillCardPanel(),m_voBill);
		supplier.setRenderer("cvendorname");
    m_iMode = BillMode.Browse;
		//set ui pk below,so put it before freshts.
    // 修改人：刘家清 修改时间：2009-9-1 下午07:34:44 修改原因：基于效率考虑，不能调用此方法，有什么要更新的直接去设置界面。
		//switchListToBill();
		//fresh timestamp
/*		if (sHPK != null) {
			ArrayList alLastTs = qryLastTs(sHPK);
			freshTs(alLastTs);
		}	*/	
		
		//重显HVO
		m_iFirstSelListHeadRow = -1;
		setButtonState();
		setBillState();
		showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill","UPP4008bill-000103")/*@res "保存成功！"*/);
		nc.ui.ic.pub.tools.GenMethod.reSetRowColorWhenNOException(getBillCardPanel());
		// added by lirr 2009-11-30下午06:57:59 清除模板缓存中的删除行 NCdp201093640 并发问题
		filterDeletedItems(getBillCardPanel().getBillModel());
	} catch (Exception e) {
		showErrorMessage(e.getMessage());
		showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill","UPP4008bill-000149")/*@res "未能完成，请再做尝试！"*/);
		handleException(e);
	}
	
	return true;
}

/*public void reSetRowColorWhenNOException(){
	// 更改颜色
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
   * 创建者：仲瑞庆
   * 功能：初始化按钮
   * 参数：
   * 返回：
   * 例外：
   * 日期：(2001-5-15 下午 3:12)
   * 修改日期，修改人，修改原因，注释标志：
   */
protected void initButtons() {

}

/**
   * 创建者：仲瑞庆
   * 功能：列表形式下表头选择改变时，在列表的下表中显示相应的表体。
   * 参数：
   * 返回：
   * 例外：
   * 日期：(2001-5-15 下午 4:27)
   * 修改日期，修改人，修改原因，注释标志：
   * @param e nc.ui.pub.bill.BillEditEvent
   */
protected void listSelectionChanged(nc.ui.pub.bill.BillEditEvent e) {
	int rownow;
	try {
		rownow = e.getRow();
		selectListBill(rownow);

	} catch (Exception e1) {
		nc.vo.scm.pub.SCMEnv.out("数据通讯失败！");
		handleException(e1);
	}
}

/**
 * 创建人：刘家清
创建日期：2008-4-3上午11:13:29
创建原因：选中列表形式下的第sn张单据 参数：sn 单据序号
 * 
 * 
 * 
 */
public void selectListBill(int rownow) {
	if (m_alListData == null || rownow < 0 || m_iLastSelListHeadRow == rownow)
		return;

	m_iLastSelListHeadRow = rownow; //最后选中的列表表头行
	getBillListPanel().getHeadTable().setRowSelectionInterval(rownow, rownow);
	getBillListPanel().getBodyBillModel().clearBodyData();

	SpecialBillVO sbvotemp =
		(SpecialBillVO) m_alListData.get(m_iLastSelListHeadRow);

	if (sbvotemp == null)
		return;

	//得到当前的body
	SpecialBillItemVO[] voItems = (SpecialBillItemVO[]) sbvotemp.getChildrenVO();
	if (voItems == null || voItems.length < 1) {
		qryItems(new int[] { rownow }, new String[] { sbvotemp.getPrimaryKey()});
	}
	//re-get
	sbvotemp = (SpecialBillVO) m_alListData.get(rownow);
	voItems = (SpecialBillItemVO[]) sbvotemp.getChildrenVO();
	//检查是否有表体，如果没有提示单据可能被删除了,但并不返回。
	if (voItems == null||voItems.length<=0){
	    if (m_sBillTypeCode.equals("4R"))
		    showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill","UPP4008bill-000150")/*@res "此盘点单没有帐实不符合的记录,或者此单据已被删除！"*/);
	    else
	    	showWarningMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill","UPP4008bill-000151")/*@res "未找到表体信息，可能被删除！"*/);
	}

	//特殊单没有保存换算率，变动hsl必须根据数量计算
	//GenMethod mthod = new GenMethod();
	//mthod.calAllConvRate(voItems,"fixedflag","hsl","dshldtransnum","nshldtransastnum","nchecknum","ncheckastnum");

	getBillListPanel().getBodyBillModel().setBodyDataVO(voItems);
	dispBodyRow(getBillListPanel().getBodyTable());
	setButtonState();
	setBillState();
	/**
	 * 2010-11-08 MeiChao 
	 * 当前单据为形态转换单时(此项条件暂时无法实现),选择列表中1行记录便查询出对应的费用信息,
	 * 将费用信息同时更新至列表及卡片页面下,
	 */
	//2010-11-08 MeiChao begin
	//if("4N".equals(this.getBillCardPanel().getBillType())){
		//如果存在费用信息页签...
		if(this.getBillListPanel().getBodyBillModel("jj_scm_informationcost")!=null){
			// 获取PK
			String specialVOPk = sbvotemp.getHeaderVO().getPrimaryKey();
			// 获取协助类
			JJIcScmPubHelper expenseManager = new JJIcScmPubHelper();
			
			try {
				// 根据PK查询对应费用信息
				expenseVOs = (InformationCostVO[]) expenseManager
						.querySmartVOs(InformationCostVO.class, null,
								" dr=0 and cbillid='" + specialVOPk + "'");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// 将费用信息写入费用信息页签
			if (expenseVOs != null && expenseVOs.length > 0) {
				this.getBillListPanel().getBodyBillModel(
						"jj_scm_informationcost").setBodyDataVO(expenseVOs);
				this.getBillCardPanel().getBillModel("jj_scm_informationcost")
						.setBodyDataVO(expenseVOs);
			} else {
				// 如果查出空值,那么将费用信息页签置空
				this.getBillListPanel().getBodyBillModel(
						"jj_scm_informationcost").setBodyDataVO(null);
				this.getBillCardPanel().getBillModel("jj_scm_informationcost")
						.setBodyDataVO(null);

			}
		}
	
	
}



/**
 * 创建者：仲瑞庆
 * 功能：插入行
 * 参数：
 * 返回：
 * 例外：
 * 日期：(2001-5-16 下午 5:58)
 * 修改日期，修改人，修改原因，注释标志：
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
   * 创建者：仲瑞庆
   * 功能：初始化变量
   * 参数：
   * 返回：
   * 例外：
   * 日期：(2001-5-24 下午 6:27)
   * 修改日期，修改人，修改原因，注释标志：
   */
protected void initVariable() {

}

/**
  * 创建者：仲瑞庆
  * 功能：执行审批
  * 参数：
  * 返回：
  * 例外：
  * 日期：(2001-5-10 下午 2:54)
  * 修改日期，修改人，修改原因，注释标志：
  */
public void onAuditBill() {

}

/**
* 创建者：仲瑞庆
* 功能：执行弃审(应将该方法内的两次调用BO合成一次业务)
* 参数：
* 返回：
* 例外：
* 日期：(2001-5-10 下午 2:50)
* 修改日期，修改人，修改原因，注释标志：
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
			"UCH068")/* @res "是否确定要弃审？" */
			,MessageDialog.ID_NO)) {

	case nc.ui.pub.beans.MessageDialog.ID_YES:
		break;
	default:return;	
	}
	
	
	try {
		//查询原来生成的其他入出库单
		//生成查询子句

		//弃审
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
    
    //add by liuzy 2007-11-02 10:16 压缩数据
    ObjectUtils.objectReference(vo);

		nc.ui.pub.pf.PfUtilClient.processAction(
			"DELETEOTHER",
			m_sBillTypeCode,
			m_sLogDate,
			vo);

		//更新表尾
		clearAuditBillFlag();
		filterNullLine();

		//更新时间标志 by hanwei 2003-11-01
		String sHPK=vo.getPrimaryKey().trim();
		if (sHPK != null) {
			ArrayList alLastTs = qryLastTs(sHPK);
			freshTs(alLastTs);
		  }
		m_alListData.set(m_iLastSelListHeadRow, m_voBill.clone());

		m_iFirstSelListHeadRow = -1;
		// 修改人：刘家清 修改时间：2009-9-2 上午09:37:23 修改原因：不需要此操作，并且能减少流量。
		//switchListToBill();

		setButtonState();
		setBillState();

	} catch (Exception e) {
		nc.vo.scm.pub.SCMEnv.out("数据通讯失败！");
		handleException(e);
		nc.ui.pub.beans.MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill","UPPSCMCommon-000059")/*@res "错误"*/, e.getMessage());
	}
}

/**
* 创建者：仲瑞庆
* 功能：过滤掉无存货行
* 参数：
* 返回：
* 例外：
* 日期：(2001-5-10 下午 2:57)
* 修改日期，修改人，修改原因，注释标志：
*/
protected void filterNullLine() {
	//临时表体VO，全部HIVO
	SpecialBillItemVO[] voaItem = getBillVO().getItemVOs();

	if (null == voaItem) {
		return;
	}
	if (voaItem.length < 1) {
		return;
	}

	Vector vTemp = new Vector();

	for (int i = 0; i < voaItem.length; i++) {
		//数据为空或者存货未填
		if (voaItem[i] == null
			|| voaItem[i].getCinventoryid() == null
			|| voaItem[i].getCinventoryid().trim().length() == 0) //记录此行号
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
  * 创建者：仲瑞庆
  * 功能：VO校验
  * 参数：
  * 返回：
  * 例外：
  * 日期：(2001-5-24 下午 5:17)
  * 修改日期，修改人，修改原因，注释标志：
  */
protected boolean checkVO() {
	try {
		String sAllErrorMessage = "";

		//执行以下检查，将不具有的检查注释------------------------------------------------
		//------------------------------------------------------------------------------
		//VO存在检查
		VOCheck.checkNullVO(m_voBill);
		//------------------------------------------------------------------------------
		//应发数量检查,要放在前面
		//本节点使用=====================
		//if (packageID.equals("221")) {
		//数值输入全部性检查
		//try {
		VOCheck.checkNumInput(m_voBill.getChildrenVO(), m_sNumItemKey);
		//} catch (ICNumException e) {
		////显示提示
		//String sErrorMessage=
		//GeneralMethod.getBodyErrorMessage(
		//getBillCardPanel(),
		//e.getErrorRowNums(),
		//e.getHint());
		//sAllErrorMessage= sAllErrorMessage + sErrorMessage + "\n";
		//}
		//}
		//本节点使用=====================
		//------------------------------------------------------------------------------
		//表头表体非空检查
		try {
			VOCheck.validate(
				m_voBill,
				GeneralMethod.getHeaderCanotNullString(getBillCardPanel()),
				GeneralMethod.getBodyCanotNullString(getBillCardPanel()));
		} catch (ICNullFieldException e) {
			//显示提示
			String sErrorMessage =
				GeneralMethod.getBodyErrorMessage(
					getBillCardPanel(),
					e.getErrorRowNums(),
					e.getHint());
			sAllErrorMessage = sAllErrorMessage + sErrorMessage + "\n";
		} catch (ICHeaderNullFieldException e) {
			//显示提示
			String sErrorMessage =
				GeneralMethod.getHeaderErrorMessage(
					getBillCardPanel(),
					e.getErrorRowNums(),
					e.getHint());
			sAllErrorMessage = sAllErrorMessage + sErrorMessage + "\n";
		}
		//------------------------------------------------------------------------------
		//业务项检查
		/**V31需求，自由项，批次等校验在特殊单中取消*/
		    /*
		//自由项
		try {
			VOCheck.checkFreeItemInput(m_voBill, m_sNumItemKey);
		} catch (ICNullFieldException e) {
			//显示提示
			String sErrorMessage =
				GeneralMethod.getBodyErrorMessage(
					getBillCardPanel(),
					e.getErrorRowNums(),
					e.getHint());
			sAllErrorMessage = sAllErrorMessage + sErrorMessage + "\n";
		}
		//批次号
		try {
			VOCheck.checkLotInput(m_voBill, m_sNumItemKey);
		} catch (ICNullFieldException e) {
			//显示提示
			String sErrorMessage =
				GeneralMethod.getBodyErrorMessage(
					getBillCardPanel(),
					e.getErrorRowNums(),
					e.getHint());
			sAllErrorMessage = sAllErrorMessage + sErrorMessage + "\n";
		}
		//辅计量
		try {
			VOCheck.checkAssistUnitInput(m_voBill, m_sNumItemKey, m_sAstItemKey);
		} catch (ICNullFieldException e) {
			//显示提示
			String sErrorMessage =
				GeneralMethod.getBodyErrorMessage(
					getBillCardPanel(),
					e.getErrorRowNums(),
					e.getHint());
			sAllErrorMessage = sAllErrorMessage + sErrorMessage + "\n";
		}
		//失效日期
		try {
			VOCheck.checkInvalidateDateInput(m_voBill, m_sNumItemKey);
		} catch (ICNullFieldException e) {
			//显示提示
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
		//入库日期 不做此项检查，已放入非空项检查中，为系统必填项
		//VOCheck.checkdbizdate(m_voBill, m_sNumItemKey);
		//单价>=0检查
		try {
			VOCheck.checkGreaterThanZeroInput(m_voBill.getChildrenVO(), "nprice",nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UC000-0000741")/*@res "单价"*/);
		} catch (ICPriceException e) {
			//显示提示
			String sErrorMessage =
				GeneralMethod.getBodyErrorMessage(
					getBillCardPanel(),
					e.getErrorRowNums(),
					e.getHint());
			sAllErrorMessage = sAllErrorMessage + sErrorMessage + "\n";
		}
		//------------------------------------------------------------------------------

		//自定校验前先行报错，退出
		if (sAllErrorMessage.trim().length() != 0) {
			showErrorMessage(sAllErrorMessage);
			return false;
		}

		//自定校验
		//------------------------------------------------------------------------------

		//无任何异常
		//更改颜色为正常颜色
		nc.ui.ic.pub.tools.GenMethod.reSetRowColorWhenNOException(getBillCardPanel());

		return true;

	} catch (ICDateException e) {
		//显示提示
		String sErrorMessage =
			GeneralMethod.getBodyErrorMessage(
				getBillCardPanel(),
				e.getErrorRowNums(),
				e.getHint());
		showErrorMessage(sErrorMessage);
		//更改颜色
		nc.ui.ic.pub.tools.GenMethod.setRowColorWhenException(getBillCardPanel(), e);
		return false;
	} catch (ICNullFieldException e) {
		//显示提示
		String sErrorMessage =
			GeneralMethod.getBodyErrorMessage(
				getBillCardPanel(),
				e.getErrorRowNums(),
				e.getHint());
		showErrorMessage(sErrorMessage);
		//更改颜色
		nc.ui.ic.pub.tools.GenMethod.setRowColorWhenException(getBillCardPanel(), e);
		return false;
	} catch (ICNumException e) {
		//显示提示
		String sErrorMessage =
			GeneralMethod.getBodyErrorMessage(
				getBillCardPanel(),
				e.getErrorRowNums(),
				e.getHint());
		showErrorMessage(sErrorMessage);
		nc.ui.ic.pub.tools.GenMethod.setRowColorWhenException(getBillCardPanel(), e);
		return false;
	} catch (ICPriceException e) {
		//显示提示
		String sErrorMessage =
			GeneralMethod.getBodyErrorMessage(
				getBillCardPanel(),
				e.getErrorRowNums(),
				e.getHint());
		showErrorMessage(sErrorMessage);
		nc.ui.ic.pub.tools.GenMethod.setRowColorWhenException(getBillCardPanel(), e);
		return false;
	} catch (ICSNException e) {
		//显示提示
		String sErrorMessage =
			GeneralMethod.getBodyErrorMessage(
				getBillCardPanel(),
				e.getErrorRowNums(),
				e.getHint());
		showErrorMessage(sErrorMessage);
		nc.ui.ic.pub.tools.GenMethod.setRowColorWhenException(getBillCardPanel(), e);
		return false;
	} catch (ICLocatorException e) {
		//显示提示
		String sErrorMessage =
			GeneralMethod.getBodyErrorMessage(
				getBillCardPanel(),
				e.getErrorRowNums(),
				e.getHint());
		showErrorMessage(sErrorMessage);
		nc.ui.ic.pub.tools.GenMethod.setRowColorWhenException(getBillCardPanel(), e);
		return false;
	} catch (ICRepeatException e) {
		//显示提示
		String sErrorMessage =
			GeneralMethod.getBodyErrorMessage(
				getBillCardPanel(),
				e.getErrorRowNums(),
				e.getHint());
		showErrorMessage(sErrorMessage);
		nc.ui.ic.pub.tools.GenMethod.setRowColorWhenException(getBillCardPanel(), e);
		return false;
	} catch (ICHeaderNullFieldException e) {
		//显示提示
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
		nc.vo.scm.pub.SCMEnv.out("校验异常！其他未知故障...");
		showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill","UPP4008bill-000152")/*@res "校验异常！其他未知故障..."*/);
		handleException(e);
		return false;
	}
	

}

	/**
	 * getTitle 方法注解。
	 */
	protected FreeItemRefPane getFreeItemRefPane() {
		return m_firpFreeItemRefPane;
	}

/**
   * 批次参照
*/
protected LotNumbRefPane getLotNumbRefPane() {
  if(m_lnrpLotNumbRefPane!=null){
    m_lnrpLotNumbRefPane.setIsMutiSel(true);
    m_lnrpLotNumbRefPane.setIsBodyMutiSel(false);
  }
	return m_lnrpLotNumbRefPane;
}



	/**双击改变列表到表单
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
	 * 创建者：仲瑞庆
	 * 功能：自由项改变事件处理
	 * 参数：e单据编辑事件
	 * 返回：
	 * 例外：
	 * 日期：(2001-5-8 19:08:05)
	 * 修改日期，修改人，修改原因，注释标志：
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
			//修改人：刘家清 修改日期：2007-9-26下午03:55:38 修改原因：在自由项0中参照录入自由项，带到表体的自由项1..10上。
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
			//以下的信息需要优化，如果批次号未显示，则无需显示。
			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill","UPP4008bill-000153")/*@res "自由项修改，请重新确认批次、数量。"*/);
		} catch (Exception e2) {
			nc.vo.scm.pub.SCMEnv.error(e2);
		}

	}

/**
 * 此处插入方法说明。
 * 创建者：仲瑞庆
 * 功能：批次号更改事件
 * 参数：
 * 返回：
 * 例外：
 * 日期：(2001-6-20 21:43:07)
 * 修改日期，修改人，修改原因，注释标志：
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
	//同步改变m_voBill并入pickupValuefromLotNumbRef() 方法中。
}

/**
 * ?user> 功能： 参数： 返回： 例外： 日期：(2005-1-11 18:53:03) 修改日期，修改人，修改原因，注释标志：
 * 
 * @param voLot
 *            nc.vo.ic.pub.lot.LotNumbRefVO
 * @param irow
 *            int
 */
private void synlot(nc.vo.ic.pub.lot.LotNumbRefVO voLot, int irow,String key) {
  InvVO voInv = m_voBill.getItemInv(irow);
  
//自由项
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
 * 创建者：仲瑞庆
 * 功能：入库仓库改变事件处理
 * 参数：e单据编辑事件
 * 返回：
 * 例外：
 * 日期：(2001-5-8 19:08:05)
 * 修改日期，修改人，修改原因，注释标志：
 *
 *
 *
 *
 */
public void afterWhInEdit(nc.ui.pub.bill.BillEditEvent e) {
	//仓库
	try {
		WhVO voWh =getWhInfoByRef("cinwarehouseid");
		if (m_voBill != null)
			m_voBill.setWhIn(voWh);
		////库存组织处理
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
   * 创建者：仲瑞庆
   * 功能：出库仓库改变事件处理
   * 参数：e单据编辑事件
   * 返回：
   * 例外：
   * 日期：(2001-5-8 19:08:05)
   * 修改日期，修改人，修改原因，注释标志：
   *
   *
   *
   *
   */
public void afterWhOutEdit(nc.ui.pub.bill.BillEditEvent e) {
//  仓库
	try {
		WhVO voWh = getWhInfoByRef("coutwarehouseid");

		if (m_voBill != null) {
			m_voBill.setWhOut(voWh);
			//清表尾现存量
			m_voBill.clearInvQtyInfo();
			//清批次
			String[] sIKs = getClearIDs(1, "coutwarehouseid");
			int iRowCount = getBillCardPanel().getRowCount();
			for (int row = 0; row < iRowCount; row++)
				clearRowData(row, sIKs);

			//重新计算计划单价，因为可能“仓库后选，计划价没能被存货代出"
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

		showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill","UPP4008bill-000154")/*@res "出库仓库修改，请重新确认自由项、批次、数量。"*/);

	} catch (Exception e2) {
		nc.vo.scm.pub.SCMEnv.error(e2);
	}

}

/**
 * 创建者：王乃军
 * 功能：清除指定行的数据
 * 参数：
 * 返回：
 * 例外：
 * 日期：(2001-5-9 9:23:32)
 * 修改日期，修改人，修改原因，注释标志：
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
		//删除界面数据
		//删除界面数据
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
		//同步vo
		if (m_voBill != null)
			m_voBill.clearItem(row);
	}
}

	/**
	   * 创建者：王乃军
	   * 功能：清除指定行、指定列的数据
	   * 参数：
	   * 返回：
	   * 例外：
	   * 日期：(2001-5-9 9:23:32)
	   * 修改日期，修改人，修改原因，注释标志：
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
		//删除界面数据
		for (int col= 0; col < saColKey.length; col++)
			if (saColKey[col] != null
			&& getBillCardPanel().getBodyItem(saColKey[col]) != null) {
				try {
					bmBill.setValueAt(null, row, saColKey[col]);
					//同步vo
					m_voBill.setItemValue(row, saColKey[col], null);
					if (saColKey[col].trim().equals("vfree0")) {
						for (int i= 0; i < 10; i++) {
							m_voBill.setItemValue(row, "vfree" + Integer.toString(i + 1).trim(), null);
						}
					}
				} catch (Exception e) {
					//nc.vo.scm.pub.SCMEnv.error(e);
					nc.vo.scm.pub.SCMEnv.out("nc.ui.ic.pub.bill.SpecialBillBaseUI.clearRowData(int, String [])：set value ERR.--->" + saColKey[col]);
				} finally {

				}
			}

	}

/**
   * 创建者：仲瑞庆
   * 功能：列编辑检测
   * 参数：
   * 返回：
   * 例外：
   * 日期：(2001-5-24 上午 9:38)
   * 修改日期，修改人，修改原因，注释标志：
   */
protected void colEditableSet(String sItemKey, int iRow) {
	nc.ui.pub.bill.BillItem bi =
		getBillCardPanel().getBillData().getBodyItem(sItemKey);
	//当入点不为仓库或存货时，表体行无存货则禁止输入其他值
	if ((!sItemKey.equals("cinventorycode") && !sItemKey.equals("cwarehousename"))
		&& (null == m_voBill.getItemValue(iRow, "cinventoryid")
			|| m_voBill.getItemValue(iRow, "cinventoryid").toString().trim().length() == 0)) {
		bi.setEnabled(false);
		return;
	} else {
		bi.setEnabled(bi.isEdit());
	}
	/*
	//转库单有来源单据时，禁止修改应发数量和应发辅数量
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
				//非固定换算率
				bi.setEnabled(true);
			} else {
				//固定换算率
				bi.setEnabled(false);
			}
		} else {
			//非辅计量管理
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
		        /*@res "表体第{0}行存货为辅计量管理，请先输入辅单位和辅数量！"*/

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
			        /*@res "表体第{0}行存货为辅计量管理，请先输入辅单位和应发辅数量！"*/

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
			//非辅计量管理
			bi.setEnabled(false);
		}
	}
}

/**
 * 如果用户手工修改批次号，则查库，正确带出失效日期及对应单据号，不正确，清空。
 * 创建者：张欣
 * 功能：
 * 参数：
 * 返回：
 * 例外：
 * 日期：(2001-6-14 10:25:33)
 * 修改日期，修改人，修改原因，注释标志：
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
	/** 当批次号为空， */
	if ((sbatchcode != null && sbatchcode.trim().length() > 0)
		&& getLotNumbRefPane().isClicked())
		return;
	/** 用户手工填写批次号时，查库，检查输入的正确与否？ */
	boolean isLotRight = getLotNumbRefPane().checkData();

	if (!isLotRight) {
		getBillCardPanel().setBodyValueAt("", iSelrow, "vbatchcode");
		//added by lirr 批次号清空后对应的日期应清空
		getBillCardPanel().setBodyValueAt(null, iSelrow, "scrq");
		getBillCardPanel().setBodyValueAt(null, iSelrow, "dvalidate");
	}

}

/**
 * 当用户选择批次号后，自动带出，失效日期与对应单句号，单据类型。
 * 创建者：张欣
 * 功能：
 * 参数：
 * 返回：
 * 例外：
 * 日期：(2001-6-13 17:38:31)
 * 修改日期，修改人，修改原因，注释标志：
 * 
 * 
 *
 */
public void pickupLotRef(String colname) {
	String s = colname;
	//批次号参照带出失效日期和对应单据号及对应单据类型

	String sLot = null;
	int rownum = getBillCardPanel().getBillTable().getSelectedRow();
	if (s == null) {
		return;
	}
	if (s.equals("vbatchcode")) {
		//修改人：刘家清 修改日期：2007-9-6下午07:15:51 修改原因：当批次号不存在时，不要去更新	
		String vbatchcode = (String) getBillCardPanel().getBodyValueAt(
				rownum, "vbatchcode");
		if(!(vbatchcode!=null&&isExistInBatch((String)m_voBill.getItemValue(rownum,"cinventoryid"),vbatchcode)))
			return;
    
    nc.vo.ic.pub.lot.LotNumbRefVO[] voLot = null;
    try {
      nc.ui.ic.pub.lot.LotNumbRefPane lotRef = (nc.ui.ic.pub.lot.LotNumbRefPane) getBillCardPanel()
      .getBodyItem("vbatchcode").getComponent();
      // 手工输入，可能会有异常。
      voLot = lotRef.getLotNumbRefVOs();
      if(isLineCardEdit() && voLot.length>1){
        voLot = new nc.vo.ic.pub.lot.LotNumbRefVO[]{voLot[0]};
      }
      if(voLot!=null && voLot.length>1){
        showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
            "4008other","UPP4008other-000509")/* @res "请选择一个批次行！" */);
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
//			//该批次的失效日期
//			getBillCardPanel().setBodyValueAt(
//				getLotNumbRefPane().getRefInvalideDate() == null
//					? ""
//					: getLotNumbRefPane().getRefInvalideDate().toString(),
//				iSelrow,
//				"dvalidate");
//
//			////生产日期
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
//			//同步改变m_voBill
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
//			getBillCardPanel().setBodyValueAt("", iSelrow, "dvalidate"); //清空表体所有失效日期
//			getBillCardPanel().setBodyValueAt("", iSelrow, "scrq"); //清空表体所有生产日期
//			//同步改变m_voBill
//			m_voBill.setItemValue(iSelrow, "vbatchcode", null);
//			m_voBill.setItemValue(iSelrow, "dvalidate", null);
//
//		}
	}
}

/**
   * 创建者：王乃军
   * 功能：在表单设置显示VO
   * 参数：
   * 返回：
   * 例外：
   * 日期：(2001-5-9 9:23:32)
   * 修改日期，修改人，修改原因，注释标志：
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
 * 创建者：王乃军
 * 功能：设置表体显示的
 * 参数：
 * 返回：
 * 例外：
 * 日期：(2001-5-9 9:23:32)
 * 修改日期，修改人，修改原因，注释标志：
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
		//缺省辅计量ID
		getBillCardPanel().setBodyValueAt(voInv.getCastunitid(), row, "castunitid");
	} catch (Exception e) {
	}
	try {
		//缺省辅计量名称
		getBillCardPanel().setBodyValueAt(voInv.getCastunitname(), row, "castunitname");
	} catch (Exception e) {
	}
	
	try {
		getBillCardPanel().setBodyValueAt(voInv.getHsl(), row, "hsl");
		//换算率
		m_voBill.setItemValue(row, "hsl", voInv.getHsl());
		//是否固定换算
		m_voBill.setItemValue(row, "isSolidConvRate", voInv.getIsSolidConvRate());

	} catch (Exception e) {
	}
	//计划价
	try {
		getBillCardPanel().setBodyValueAt(
			voInv.getNplannedprice(),
			row,
			"jhdj");
	} catch (Exception e) {
	}
	try {
		//清界面自由项显示。
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
 * 创建者：王乃军
 * 功能：设置新增单据的初始数据，如日期，制单人等。
 * 参数：
 * 返回：
 * 例外：
 * 日期：(2001-5-9 9:23:32)
 * 修改日期，修改人，修改原因，注释标志：
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
			//v5制单时间
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
 * 创建者：王乃军
 * 功能：设置表尾显示数据,从m_voBill中取数。浏览状态下要重读现存量
 * 参数：
 * 返回：
 * 例外：
 * 日期：(2001-5-9 9:23:32)
 * 修改日期，修改人，修改原因，注释标志：
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
	
	// 修改人：陈倪娜 上午09:48:07_2009-10-17 修改原因 键盘下拉行号自动增加

	nc.ui.scm.pub.report.BillRowNo.addLineRowNo(
			getBillCardPanel(),
			m_sBillTypeCode,
			m_sBillRowNo);
	//仓库是否为空
	Object oInWhID= null;
	oInWhID= m_voBill.getHeaderValue("cinwarehouseid");
	Object oOutWhID= null;
	oOutWhID= m_voBill.getHeaderValue("coutwarehouseid");
	if(oOutWhID==null)
		oOutWhID=(String)m_voBill.getItemValue(row,"cwarehouseid");
	if(oOutWhID==null)
			return;
	//存货是否为空
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
    		//看是否已经读过控制信息
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
//		    //看是否已经读过控制信息
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

	//取当前vo中的数据

		ArrayList alIDs= new ArrayList();
		//alIDs.add(oInWhID);
		ArrayList alQty= null;
		try {
		String sCalID =(String) ((Object[]) nc.ui.scm.pub.CacheTool.getCellValue("bd_stordoc", "pk_stordoc", "pk_calbody", (String)oOutWhID))[0];
		
		alIDs.add(oOutWhID);
		alIDs.add(oInvID);
		alIDs.add(sCalID);
		alIDs.add(m_sCorpID);

		
		//读数
		
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
			//写入vo保存
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
//		// 数据格式：本库现存量，现存总量，最高库存量，最低库存量，订购点库存量，安全库存量
//		if (alQty != null && alQty.size() >= 6) {
//
//			//oInWhQty= alQty.get(0);
//			oOutWhQty= alQty.get(0);
//			oTotalQty= alQty.get(1);
//			nmaxstocknum= alQty.get(2);
//			nminstocknum= alQty.get(3);
//			norderpointnum= alQty.get(4);
//			nsafestocknum= alQty.get(5);
//			//写入vo保存
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

	//入库仓库现存量
	nc.ui.pub.bill.BillItem biTail= getBillCardPanel().getTailItem("inbkxcl");
	if (biTail != null)
		if (oInWhQty != null)
			biTail.setValue(new nc.vo.pub.lang.UFDouble(oInWhQty.toString()));
		else
			biTail.setValue(null);
	//出库仓库现存量
	biTail= getBillCardPanel().getTailItem("outbkxcl");
	if (biTail != null)
		if (oOutWhQty != null)
			biTail.setValue(new nc.vo.pub.lang.UFDouble(oOutWhQty.toString()));
		else
			biTail.setValue(null);
	//本库仓库现存量
	biTail= getBillCardPanel().getTailItem("bkxcl");
	if (biTail != null)
		if (oOutWhQty != null)
			biTail.setValue(new nc.vo.pub.lang.UFDouble(oOutWhQty.toString()));
		else
			biTail.setValue(null);
	//现存总量
	biTail= getBillCardPanel().getTailItem("xczl");
	if (biTail != null)
		if (oTotalQty != null)
			biTail.setValue(new nc.vo.pub.lang.UFDouble(oTotalQty.toString()));
		else
			biTail.setValue(null);
	//最高库存
	biTail= getBillCardPanel().getTailItem("nmaxstocknum");
	if (biTail != null)
		if (nmaxstocknum != null)
			biTail.setValue(new nc.vo.pub.lang.UFDouble(nmaxstocknum.toString()));
		else
			biTail.setValue(null);
	//最低库存
	biTail= getBillCardPanel().getTailItem("nminstocknum");
	if (biTail != null)
		if (nminstocknum != null)
			biTail.setValue(new nc.vo.pub.lang.UFDouble(nminstocknum.toString()));
		else
			biTail.setValue(null);
	//安全库存
	biTail= getBillCardPanel().getTailItem("nsafestocknum");
	if (biTail != null)
		if (nsafestocknum != null)
			biTail.setValue(new nc.vo.pub.lang.UFDouble(nsafestocknum.toString()));
		else
			biTail.setValue(null);
	//订货点量
	biTail= getBillCardPanel().getTailItem("norderpointnum");
	if (biTail != null)
		if (norderpointnum != null)
			biTail.setValue(new nc.vo.pub.lang.UFDouble(norderpointnum.toString()));
		else
			biTail.setValue(null);

}

	/**
	 * 创建者：王乃军
	 * 功能：设置表尾显示数据,传入null则清空。
	 * 参数：
	 * 返回：
	 * 例外：
	 * 日期：(2001-5-9 9:23:32)
	 * 修改日期，修改人，修改原因，注释标志：
	 *
	 *
	 *
	 *
	 */
	protected void setTailValue(InvVO voInv) {
		//本库现存量
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
		//现存总量
		biTail= getBillCardPanel().getTailItem("xczl");
		if (biTail != null)
			if (voInv != null)
				biTail.setValue(voInv.getBkxcl());
			else
				biTail.setValue(null);
		//最高库存
		biTail= getBillCardPanel().getTailItem("nmaxstocknum");
		if (biTail != null)
			if (voInv != null)
				biTail.setValue(voInv.getNmaxstocknum());
			else
				biTail.setValue(null);
		//最低库存
		biTail= getBillCardPanel().getTailItem("nminstocknum");
		if (biTail != null)
			if (voInv != null)
				biTail.setValue(voInv.getNminstocknum());
			else
				biTail.setValue(null);
		//安全库存
		biTail= getBillCardPanel().getTailItem("nsafestocknum");
		if (biTail != null)
			if (voInv != null)
				biTail.setValue(voInv.getNsafestocknum());
			else
				biTail.setValue(null);
		//订货点量
		biTail= getBillCardPanel().getTailItem("norderpointnum");
		if (biTail != null)
			if (voInv != null)
				biTail.setValue(voInv.getNorderpointnum());
			else
				biTail.setValue(null);

	}

	/**
	 * 创建者：王乃军
	 * 功能：同步行数据，如货位、序列号
	 * 参数：int iFirstLine,iLastLine 行号，start from 0
	       int iCol列 start from 0
	       int type1: add
	             0: update
	             -1:delete

	 * 返回：
	 * 例外：
	 * 日期：(2001-5-9 9:23:32)
	 * 修改日期，修改人，修改原因，注释标志：
	 * 2001-06-13. 同步VO
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
		////初始化行数据，比如在复制单据时，m_alLocatorData==null 但单据行数不为0。
		//m_alLocatorData=new ArrayList(getBillCardPanel().getBillModel().getRowCount());
		//}
		//if(m_alSerialData==null){
		//nc.vo.scm.pub.SCMEnv.out("init serial data.");
		//m_alSerialData=new ArrayList();
		////初始化行数据，比如在复制单据时，m_alSerialData==null 但单据行数不为0。
		//m_alSerialData=new ArrayList(getBillCardPanel().getBillModel().getRowCount());
		//}
		if (m_voBill == null)
			m_voBill= new SpecialBillVO();
//		 修改人：刘家清 修改日期：2007-10-16下午04:56:44 修改原因：不是行变化就直接返回
		if(iType!=javax.swing.event.TableModelEvent.INSERT &&
				iType!=javax.swing.event.TableModelEvent.UPDATE &&
				iType!=javax.swing.event.TableModelEvent.DELETE &&
				getBillCardPanel().getRowCount()<=m_voBill.getItemCount())
			return;

		switch (iType) {
			case javax.swing.event.TableModelEvent.INSERT : //增行：插、追加、粘贴
				m_voBill.insertItem(iFirstLine);
				break;
			case javax.swing.event.TableModelEvent.UPDATE :
        while(getBillCardPanel().getRowCount() > m_voBill.getItemCount()){
//        	 修改人：刘家清 修改日期：2007-10-16下午04:56:44 修改原因：传入的iFirstLine有问题，更正了一下
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

	/** 表单下发生行中值修改时触发
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
   * 返回 ReturnDlg1 特性值。
   * @return nc.ui.ic.ic205.ReturnDlg
   */
/* 警告：此方法将重新生成。 */
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

/**右键菜单
 * 此处插入方法说明。
 * 创建日期：(2001-3-27 11:09:34)
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
 * 创建者：仲瑞庆
 * 功能：由特殊单据VO改为普通单据VO
 * 参数：
 * 返回：
 * 例外：
 * 日期：(2001-6-26 下午 4:43)
 * 修改日期，修改人，修改原因，注释标志：
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

	//对表头
	gbvo.setParentVO(
		changeFromSpecialVOtoGeneralVOAboutHeader(gbvo, sbvo, iInOutFlag));

	//对表体
	for (int j= 0; j < iItemNumb; j++) {
		gbvo.setItem(
			j,
			changeFromSpecialVOtoGeneralVOAboutItem(gbvo, sbvo, iInOutFlag, j));
	}

	return gbvo;
}







/**
	 * 创建者：仲瑞庆
	 * 功能：存货事件处理
	 * 参数：e单据编辑事件
	 * 返回：
	 * 例外：
	 * 日期：(2001-5-8 19:08:05)
	 * 修改日期，修改人，修改原因，注释标志：
	 *
	 *
	 *
	 *
	 */
public void afterInvEdit(Object value, int row) {
	//nc.vo.scm.pub.SCMEnv.out("inv chg");
	try {
		setTailValue(null);
		//如果清除存货编码则清掉此行,并去掉表尾显示
		if ((value == null) || (value.toString().trim().length() == 0)) {
			clearRowData(row);
			//表尾

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
			//表体
			setBodyInvValue(row, voInv);
			//表尾
			setTailValue(row);
			//清批次
			String[] sIKs = getClearIDs(1, "cinventorycode");
			//金额，计划金额会自动清吗？
			clearRowData(row, sIKs);
			//以下的信息需要优化，如果批次号未显示，则无需显示。
			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill","UPP4008bill-000155")/*@res "存货修改，请重新确认自由项、批次、数量。"*/);
		}
	} catch (Exception e2) {
		nc.vo.scm.pub.SCMEnv.error(e2);
	}
}



/**
  * 创建者：仲瑞庆
  * 功能：套件辅数量修改,更改主数量和配件数量
  * 参数：e单据编辑事件
  * 返回：
  * 例外：
  * 日期：(2001-5-8 19:08:05)
  * 修改日期，修改人，修改原因，注释标志：
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
	//套件数量更改配件数量
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
	 * 创建者：王乃军
	 * 功能：得到环境初始数据，如制单人等。
	 * 参数：
	 * 返回：
	 * 例外：
	 * 日期：(2001-5-9 9:23:32)
	 * 修改日期，修改人，修改原因，注释标志：
	 *
	 *
	 *
	 *
	 */
	protected void getCEnvInfo() {
		try {
			nc.ui.pub.ClientEnvironment ce= nc.ui.pub.ClientEnvironment.getInstance();

			//当前使用者ID
			try {
				m_sUserID= ce.getUser().getPrimaryKey();
			} catch (Exception e) {
				nc.vo.scm.pub.SCMEnv.out("test user id is 2011000001");
				m_sUserID= "2011000001";
			}

			//当前使用者姓名
			try {
				m_sUserName= ce.getUser().getUserName();
			} catch (Exception e) {
				nc.vo.scm.pub.SCMEnv.out("test user name is 张三");
				m_sUserName= nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill","UPP4008bill-000156")/*@res "张三"*/;
			}

			//公司ID
			try {
				m_sCorpID= ce.getCorporation().getPrimaryKey();
				nc.vo.scm.pub.SCMEnv.out("---->corp id is " + m_sCorpID);
			} catch (Exception e) {

			}
			//日期
			try {
				if (ce.getDate() != null)
					m_sLogDate= ce.getDate().toString();
			} catch (Exception e) {

			}

		} catch (Exception e) {
		}
	}



	/**
	 * 创建者：仲瑞庆
	 * 功能：清除审核员
	 * 参数：
	 * 返回：
	 * 例外：
	 * 日期：(2001-5-9 9:23:32)
	 * 修改日期，修改人，修改原因，注释标志：
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
	   * 创建者：仲瑞庆
	   * 功能：显示表体行
	   * 参数：
	   * 返回：
	   * 例外：
	   * 日期：(2001-5-16 下午 6:32)
	   * 修改日期，修改人，修改原因，注释标志：
	   */
	protected void dispBodyRow(nc.ui.pub.beans.UITable mUITable) {
		//显示表体行
		mUITable.clearSelection();
		m_iTotalListBodyNum= mUITable.getRowCount();
		if (m_iTotalListBodyNum <= 0) {
			return;
		}
		if (m_iLastSelCardBodyRow < 0) {
			m_iLastSelCardBodyRow= 0; //最后选中的列表表体行
		}
		if (m_iLastSelCardBodyRow > m_iTotalListBodyNum - 1) {
			m_iLastSelCardBodyRow= m_iTotalListBodyNum - 1;
		}
		if (m_iLastSelCardBodyRow < 0) {
			m_iLastSelCardBodyRow= 0; //最后选中的列表表体行
		}

		mUITable.setRowSelectionInterval(m_iLastSelCardBodyRow, m_iLastSelCardBodyRow);

		//显示表尾数据
		//setTailValue(m_iLastSelCardBodyRow);

	}

	/**
	   * 创建者：仲瑞庆
	   * 功能：产生辅计量
	   * 参数：
	   * 返回：
	   * 例外：
	   * 日期：(2001-5-16 下午 6:32)
	   * 修改日期，修改人，修改原因，注释标志：
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
					showHintMessage("未查到该存货的辅计量");
				}
		*/
	}

/**
 * 此处插入方法说明。
 * 创建者：仲瑞庆
 * 功能：初始化表头表体中的参照
 * 参数：
 * 返回：
 * 例外：
 * 日期：(2001-7-17 10:33:20)
 * 修改日期，修改人，修改原因，注释标志：
 *修改人：刘家清 修改日期：2007-10-24下午04:51:53 修改原因：滤掉仓库更换成统一方法
 */
public void filterRef(String cropid) {
	//String calRef = " and pk_calbody in (select pk_calbody from  bd_calbody  where (sealflag is null or sealflag ='N') and property in (0,1))";
	//表头非废品仓库
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


	//表头废品仓库
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
	//表头非废品out仓库
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
	
	
	//表头非废品in仓库
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
	
	//表头客户
	if (getBillCardPanel().getHeadItem("ccustomerid") != null
		&& getBillCardPanel().getHeadItem("ccustomerid").getComponent() != null) {

		UIRefPane ref =
			(UIRefPane) getBillCardPanel().getHeadItem("ccustomerid").getComponent();
		ref.getRefModel().setRefNameField("bd_cubasdoc.custshortname");

		ref.setWhereString(
			"(custflag ='0' or custflag ='2') and bd_cumandoc.pk_corp='" + cropid + "'");
	}
	//表头供应商
	if (getBillCardPanel().getHeadItem("cproviderid") != null
		&& getBillCardPanel().getHeadItem("cproviderid").getComponent() != null) {

		UIRefPane ref =
			(UIRefPane) getBillCardPanel().getHeadItem("cproviderid").getComponent();
		ref.getRefModel().setRefNameField("bd_cubasdoc.custshortname");

		ref.setWhereString(
			"(custflag ='1' or custflag ='3') and bd_cumandoc.pk_corp='" + cropid + "'");
	}

	//表体单位编码过滤存货且为非封存存货,
	//if (getBillCardPanel().getBodyItem("cinventorycode") != null
	//&& getBillCardPanel().getBodyItem("cinventorycode").getComponent() != null) {
	//(
	//(nc.ui.pub.beans.UIRefPane) getBillCardPanel()
	//.getBodyItem("cinventorycode")
	//.getComponent())
	//.setWhereString(
	//"bd_invmandoc.sealflag ='N' and bd_invmandoc.pk_corp='" + cropid + "'");
	//}
	//表体单位编码过滤存货且为非封存存货,非劳务存货，非折扣存货
	BillItem bi = getBillCardPanel().getBodyItem("cinventorycode");
	if (bi!= null&& bi.getComponent() != null) {

			//过滤存货编码

		nc.ui.pub.beans.UIRefPane invRef = (nc.ui.pub.beans.UIRefPane) bi.getComponent();
		invRef.setTreeGridNodeMultiSelected(true);
 		invRef.setMultiSelectedEnabled(true);

			invRef.setWhereString(
			" bd_invbasdoc.discountflag='N' and bd_invbasdoc.laborflag='N'  "
				+ "and bd_invmandoc.sealflag ='N' and bd_invmandoc.pk_corp='"
				+ cropid
				+ "'");
	}

	//表体辅计量；存货辅计量参照中应进行存货过滤，只显示在存货档案中进行过换算定义的辅计量，同时将换算率带出。

	//表体非废品仓库
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
	//表体非废品仓库2
	//对：库存组装单、库存拆卸单、库存形态转换单
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
	   * 创建者：仲瑞庆
	   * 功能：取得现存量界面
	   * 参数：
	   * 返回：
	   * 例外：
	   * 日期：(2001-5-16 下午 6:32)
	   * 修改日期，修改人，修改原因，注释标志：
	   */
	protected nc.ui.ic.pub.InvOnHandDialog getIohdDlg() {
		if (null == m_iohdDlg) {
			m_iohdDlg= new InvOnHandDialog(this);
		}
		return m_iohdDlg;
	}

/**
 * 此处插入方法说明。
 * 创建日期：(2001-6-27 10:34:52)
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

/**存量查询
  * 修改
  * 创建日期：(2001-4-18 19:45:39)
  * 修改人：刘家清 修改时间：2008-7-29 上午11:15:16 修改原因：转库单支持选择存量出库的功能
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
	    showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill","UPP4008bill-000157")/*@res "没有得到仓库ID或者存货ID"*/);
	    return;
	}

	getIohdDlg().setParam(WhID, InvID);
	if (getIohdDlg().showModal() == nc.ui.pub.beans.UIDialog.ID_OK){
		nc.vo.ic.pub.InvOnHandVO[] selectVOs = getIohdDlg().m_SelectVOs;
		afterSelectOnhandVOs(selectVOs);
	}
}

/**
 * 创建人：刘家清 创建时间：2008-7-29 上午11:17:20 创建原因： 支持选择存量出库的功能
 * @param selectVOs
 */
public void afterSelectOnhandVOs(nc.vo.ic.pub.InvOnHandVO[] selectVOs){
	
}


/**
 * 创建者：王乃军
 * 功能：设置新增单据的初始数据，如日期，制单人等。
 * 参数：
 * 返回：
 * 例外：
 * 日期：(2001-5-9 9:23:32)
 * 修改日期，修改人，修改原因，注释标志：
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

	/**设置现存量对话框
	 * 此处插入方法说明。
	 * 创建日期：(2001-7-11 下午 11:19)
	 * @param newDlg nc.ui.ic.pub.InvOnHandDialog
	 */
	protected void setDlg(nc.ui.ic.pub.InvOnHandDialog newDlg) {
		m_iohdDlg= newDlg;
	}

	/**
	 * 创建者：王乃军
	 * 功能：设置新增单据的初始数据，如日期，制单人等。
	 * 参数：
	 * 返回：
	 * 例外：
	 * 日期：(2001-5-9 9:23:32)
	 * 修改日期，修改人，修改原因，注释标志：
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
   * 创建者：仲瑞庆
   * 功能：由表单切换到列表
   * 参数：
   * 返回：
   * 例外：
   * 日期：(2001-5-16 下午 6:53)
   * 修改日期，修改人，修改原因，注释标志：
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
			m_iLastSelListHeadRow = 0; //最后选中的列表表头行
		SpecialBillVO voTemp = (SpecialBillVO) m_alListData.get(m_iLastSelListHeadRow);

		if (voTemp == null)
			return;

		//得到当前的body
		SpecialBillItemVO[] voItems = voTemp.getItemVOs();
		//如果表体数量为空，查之。
		if (voItems == null || voItems.length < 1) {
			qryItems(
				new int[] { m_iLastSelListHeadRow },
				new String[] { voTemp.getPrimaryKey()});
		}
		//re-get after query.....
		voTemp = (SpecialBillVO) m_alListData.get(m_iLastSelListHeadRow);
		voItems = (SpecialBillItemVO[]) voTemp.getChildrenVO();

		//置入表头
		getBillListPanel().getHeadBillModel().setSortColumn(null);
		getBillListPanel().getHeadBillModel().setBodyDataVO(getListHeaderVOs());
		//给定焦点
		getBillListPanel().getHeadTable().clearSelection();
		getBillListPanel().getHeadTable().setRowSelectionInterval(
			m_iLastSelListHeadRow,
			m_iLastSelListHeadRow);
	
		getBillListPanel().getBodyBillModel().setBodyDataVO(voItems);
		
//		getBillListPanel().getBodyBillModel().execLoadFormula();//zhy注释掉,该行引起查询后表体行存货编码和名称显示不出来
		//modified by lirr 2008-01-12
		//getBillListPanel().getBodyBillModel().execLoadFormula();
		//getBillListPanel().getHeadBillModel().execLoadFormula();
		dispBodyRow(getBillListPanel().getBodyTable());
		
		
//		 控制翻页按钮的状态：
		setPageBtnStatus(0,0);

	} catch (Exception e1) {
		nc.vo.scm.pub.SCMEnv.out("数据通讯失败！");
		handleException(e1);
	}
}

/**
   * 创建者：仲瑞庆
   * 功能：由list转入bill时，自动将list的选择情况转入bill中
   * 参数：
   * 返回：
   * 例外：
   * 日期：(2001-5-16 下午 6:32)
   * 修改日期，修改人，修改原因，注释标志：
   */
protected void switchListToBill() {
	//加入从刚才的列表中选择的数据
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
		//卡片执行公式解析
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
 * 创建者：王乃军
 * 功能：单据编辑事件处理
 * 参数：e单据编辑事件
 * 返回：
 * 例外：
 * 日期：(2001-5-8 19:08:05)
 * 修改日期，修改人，修改原因，注释标志：
 *
 *
 *
 *
 */
public void afterAstUOMEdit(int rownow) {
	//辅计量单位
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

	//保存名称以在列表形式下显示。
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
    
//  当辅计量单位为空时,清空换算率与辅数量
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
	//供应商
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
	//自定义项处理zhy
	if (e.getPos() == 0) {//表头
		String sVdefPkKey = "pk_defdoc"
				+ e.getKey().substring("vuserdef".length());
        //lj 如果自定义项有值，则不调用
//		    if (m_voBill.getHeaderValue(sVdefPkKey)==null) {
		 DefSetTool.afterEditHead(getBillCardPanel().getBillData(),
		        e.getKey(), sVdefPkKey);
		 //同步m_voBill
		 m_voBill.setHeaderValue(e.getKey(), getBillCardPanel()

				.getHeadItem(e.getKey()).getValue());
		 m_voBill.setHeaderValue(sVdefPkKey, getBillCardPanel()
				.getHeadItem(sVdefPkKey).getValue());
//		    }
	} else if (e.getPos() == 1) {//表体
		String sVdefPkKey = "pk_defdoc"
				+ e.getKey().substring("vuserdef".length());

//			if (m_voBill.getHeaderValue(sVdefPkKey) == null) {

		DefSetTool.afterEditBody(getBillCardPanel().getBillModel(),
				e.getRow(), e.getKey(), sVdefPkKey);

		//同步m_voBill
		m_voBill.setItemValue(e.getRow(), e.getKey(), getBillCardPanel()
				.getBodyValueAt(e.getRow(), e.getKey()));
		m_voBill.setItemValue(e.getRow(), sVdefPkKey, getBillCardPanel()
				.getBodyValueAt(e.getRow(), sVdefPkKey));
//			}
	}
	
}

/**
 * 创建者：仲瑞庆
 * 功能：
 * 参数：
 * 返回：
 * 例外：
 * 日期：(2001-7-25 上午 10:50)
 * 修改日期，修改人，修改原因，注释标志：
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
//		//算生产日期
//		//修改人：刘家清 修改日期：2007-9-4下午04:49:40 修改原因：生产日期、失效日期都有值时，变生产日期算失效日期，变失效日期不要去算生产日期(通过保持期
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
//		//清生产日期
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
 * 创建者：仲瑞庆
 * 功能：由传入的单据类型、字段，获得当该字段改变后，应改变的其他字段列表
 * 参数：iBillFlag 单据类型，当为普通单据，传入0，当为特殊单据，传入1
 				已有
 				存货					cinventorycode，
 				表体仓库			cwarehousename，
 				自由项				vfree0，
 				表头出库仓库	coutwarehouseid，
 				表头仓库			cwarehouseid
 * 返回：
 * 例外：
 * 日期：(2001-7-18 上午 9:20)
 * 修改日期，修改人，修改原因，注释标志：
 * @return java.lang.String[]
 * @param sWhatChange java.lang.String
 */
protected String[] getClearIDs(int iBillFlag, String sWhatChange) {
	if (sWhatChange == null)
		return null;
	String[] sReturnString = null;
	sWhatChange = sWhatChange.trim();
	if (sWhatChange.equals("cinventorycode")) {
		//存货
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
		//特殊单的表体行内仓库
		sReturnString = new String[6];
		sReturnString[0] = "vbatchcode";
		sReturnString[2] = m_sNumItemKey;
		sReturnString[3] = m_sAstItemKey;
		sReturnString[4] = "scrq";
		sReturnString[5] = "dvalidate";
		//showWarningMessage("请重新确认批次号！");
		return null;
	} else if (sWhatChange.equals("vfree0")) {
		//自由项
//		sReturnString = new String[3];
//		sReturnString[0] = "vbatchcode";
//		sReturnString[1] = "scrq";
//		sReturnString[2] = "dvalidate";
		//showWarningMessage("请重新确认批次号！");
		return null;
	} else if (sWhatChange.equals("coutwarehouseid")) {
		sReturnString = new String[5];
		sReturnString[0] = "vbatchcode";
		sReturnString[1] = m_sNumItemKey;
		sReturnString[2] = m_sAstItemKey;
		sReturnString[3] = "scrq";
		sReturnString[4] = "dvalidate";
		//showWarningMessage("请重新确认批次号！");
		return null;
	} else if ((sWhatChange.equals("cwarehouseid")) && (iBillFlag == 0)) {
		sReturnString = new String[5];
		sReturnString[0] = "vbatchcode";
		sReturnString[1] = m_sNumItemKey;
		sReturnString[2] = m_sAstItemKey;
		sReturnString[3] = "scrq";
		sReturnString[4] = "dvalidate";
		//showWarningMessage("请重新确认批次号！");
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
   * 创建者：仲瑞庆
   * 功能：调入BillListPanel中的数据 相当于查询
   * 参数：
   * 返回：
   * 例外：
   * 日期：(2001-5-16 下午 1:34)
   * 修改日期，修改人，修改原因，注释标志：
   */
protected void loadBillListPanel(QryConditionVO qcvo) {
	try {

		m_alListData =
			(ArrayList) SpecialBillHelper.queryBills(m_sBillTypeCode, qcvo);
        
		if (m_alListData != null && m_alListData.size() > 0) {

			m_iTotalListHeadNum = m_alListData.size(); //列表表头目前存在的行数

			m_iLastSelListHeadRow = 0; //最后选中的列表表头行

			//修改辅计量calConvRate
			SpecialBillVO sbvotemp =
				(SpecialBillVO) m_alListData.get(m_iLastSelListHeadRow);
			sbvotemp.calConvRate();
			m_alListData.set(m_iLastSelListHeadRow, sbvotemp);

			//v5:exec forumla
			getFormulaBillContainer().formulaBill(m_alListData,getFormulaItemHeader(),getFormulaItemBody());
			//执行批次号档案公式
			BatchCodeDefSetTool.execFormulaForBatchCode(getBillBodyData(m_alListData));
      
			switchBillToList();

		} else {
			dealNoData();
			switchBillToList();
		}
	m_iMode = BillMode.List;
	} catch (Exception e) {
		nc.vo.scm.pub.SCMEnv.out("数据通讯失败！");
		showErrorMessage(e.getMessage());
		handleException(e);
	}

}

/**
   * 创建者：仲瑞庆
   * 功能：当在浏览状态下发生单据修改时执行保存
   * 参数：
   * 返回：
   * 例外：
   * 日期：(2001-5-16 下午 6:32)
   * 修改日期，修改人，修改原因，注释标志：
   */
protected void reLoadBill() {
	try {
		m_iMode = BillMode.Browse;
		//查出新的表单
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
 * 创建者：仲瑞庆
 * 功能：
 * 参数：
 * 返回：
 * 例外：
 * 日期：(2001-7-19 下午 10:51)
 * 修改日期，修改人，修改原因，注释标志：
 * @param bShowFlag boolean
 */
public void setBodyMenuShow(boolean bShowFlag) {
	getBillCardPanel().setBodyMenuShow(bShowFlag);
}

	//单据表体公式
	ArrayList m_alFormulBodyItem;
	//单据表头公式
	ArrayList m_alFormulHeadItem;
	//列表中的ArrayList型的HOV，用来给HVO增，删，改
	protected ArrayList m_alListData = new ArrayList();
	//单据公式容器
	BillFormulaContainer m_billFormulaContain;
	//联查
	protected ButtonObject m_boJointCheck;
	protected ButtonObject m_boPreview;
	//初始化打印接口
	protected PrintDataInterface m_dataSource;
	private DefVO[] m_defBody=null;
	private DefVO[] m_defHead=null;
	protected nc.ui.ic.pub.orient.OrientDialog m_dlgOrient;
	//仓库信息缓存。
	private Hashtable m_htWh = new Hashtable();
	//小数精度定义--->
	//数量小数位			2
	//辅计量数量小数位	2
	//换算率				2
	//存货成本单价小数位	2
	protected int m_iaScale[] = new int[] { 2, 2, 2, 2, 2 };
	//公式解析需要的相关全局变量 by hanwei 2003-06-26

	private InvoInfoBYFormula m_InvoInfoBYFormula;
	boolean m_isLocated=false;
	//是否查询计划价格，在存货参照多选情况下：
	public boolean m_isQuryPlanprice = true;
	protected nc.ui.pub.print.PrintEntry m_print;
	protected ButtonObject m_PrintMng;
	//zhx 030626 单据行号
	//hanwei 2003-10-09
	public static final String m_sBillRowNo = "crowno";
	//仓库PK字段
	public String m_sMainWhItemKey = "coutwarehouseid";
	//记录旧的单据号
	protected String m_sOldBillCode = "";
	protected String m_sPNodeCode;
	//调用的各个节点的BO_Client接口,应被子类修改
	//protected String sSpecialHBO_Client;
	//只初始化一次，多次用到的UFDouble.
	final protected UFDouble ZERO = new UFDouble("0.0");

/**
	 * 创建者：仲瑞庆
	 * 功能：递增SpecialHVO,在StationNumber位置
	 * 参数：
	 * 返回：
	 * 例外：
	 * 日期：(2001-5-16 下午 7:22)
	 * 修改日期，修改人，修改原因，注释标志：
	 * @param m_iStationNumber int
	 */
protected void addBillVO() {
	//m_shvoBillSpecialHVO
	if (m_alListData == null)
		m_alListData = new ArrayList();

	m_alListData.add(m_iLastSelListHeadRow, m_voBill.clone());

	m_iTotalListHeadNum = m_alListData.size();

	if (m_iLastSelListHeadRow < 0)
		m_iLastSelListHeadRow = 0; //最后选中的列表表头行

}


/**
 * 创建者：仲瑞庆
 * 功能：换算率修改
 * 参数：
 * 返回：
 * 例外：
 * 日期：(2001-11-20 14:01:52)
 * 修改日期，修改人，修改原因，注释标志：
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
	// 业务员
	if(bsor==null||dept==null)
		return;
	UIRefPane ref=(nc.ui.pub.beans.UIRefPane) getBillCardPanel().getHeadItem(bsor[0]).getComponent();
	String sName = ref.getRefName();
	String sPK = ref.getRefPK();
	// 需要根据业务员自动带出部门
//	修改人：刘家清 修改时间：2008-8-25 上午10:59:04 修改原因：不根据业务员自动带出部门
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
			// 部门
			sDeptName = ((nc.ui.pub.beans.UIRefPane) itDpt
					.getComponent()).getRefName();


		}

	}*/
//	 保存名称以在列表形式下显示。
	if (m_voBill != null) {
		m_voBill.setHeaderValue(bsor[1], sName);
		//m_voBill.setHeaderValue(dept[1], sDeptName);
	}
}

/**
 * 创建者：王乃军
 * 功能：存货事件处理
 * 参数：e单据编辑事件
 * 返回：
 * 例外：
 * 日期：(2001-5-8 19:08:05)
 * 修改日期，修改人，修改原因，注释标志：
 *
 */
public void afterInvMutiEdit(nc.ui.pub.bill.BillEditEvent e){


	long ITimeAll = System.currentTimeMillis();

	int row = e.getRow();
	//字段itemkey
	String sItemKey = e.getKey();

	nc.ui.pub.beans.UIRefPane invRef =
		(nc.ui.pub.beans.UIRefPane) getBillCardPanel()
			.getBodyItem("cinventorycode")
			.getComponent();
	//管理档案PK
	String[] refPks = invRef.getRefPKs();
  if(isLineCardEdit() && refPks.length>1){
    refPks = new String[]{refPks[0]};
  }
	
	//如果返回为空，清空当前环境
	if (refPks == null || refPks.length == 0) {
		clearRowData(row);
		return;
	}
	invRef.setPK(null);

	afterInvMutiEdit(refPks,row);
	SCMEnv.showTime(ITimeAll, "存货参照多选:");
	
}
/**
 * 创建人：刘家清 创建时间：2008-7-29 下午01:42:08 创建原因： 给出存货pks，然后单行增加存货。
 * @param refPks
 * @param row
 */
public void afterInvMutiEdit(String[] refPks,Integer row){
	try{
		//仓库和库存组织信息
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
	
		SCMEnv.showTime(ITime, "存货解析参数设置:");
	
		ITime = System.currentTimeMillis();
		//存货解析
		InvVO[] invVOs = null;
		if (isQuryPlanprice())
		    invVOs = getInvoInfoBYFormula().getInvParseWithPlanPrice(refPks, sWhID, sCalID,true,true);
		 else
		    invVOs =getInvoInfoBYFormula().getBillQuryInvVOs(refPks,true,true);
	
		SCMEnv.showTime(ITime, "存货解析:");
	
	
		ITime = System.currentTimeMillis();
		
		afterInvMutiEdit(invVOs,row);

	}catch(Exception e1){
		showErrorMessage(e1.getMessage());
	
	}
}

/**
 * 创建人：刘家清 创建时间：2008-7-29 下午01:42:08 创建原因： 给出存货InvVOs，然后单行增加存货。
 * @param refPks
 * @param row
 */
public void afterInvMutiEdit(InvVO[] invVOs,Integer row){/*
	//界面增行
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

	//单据行号
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
	SCMEnv.showTime(ITime, "设置界面数据:");
	//以下的信息需要优化，如果批次号未显示，则无需显示。
	showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill","UPP4008bill-000155")@res "存货修改，请重新确认自由项、批次、数量。");

	SCMEnv.showTime(ITime, "存货界面设置:");
*/
	afterInvMutiEdit(invVOs,row,false);
}

/**
 * 创建者：仲瑞庆
 * 功能：由生产日期算失效日期
 * 参数：
 * 返回：
 * 例外：
 * 日期：(2001-7-25 上午 10:50)
 * 修改日期，修改人，修改原因，注释标志：
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
    		//算失效日期
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
//      清失效日期
        getBillCardPanel().setBodyValueAt(null, iSelrow, "dvalidate");
      }
      
	} else {
		//清失效日期
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
 * 编辑前处理。
 * 创建日期：(2001-3-23 2:02:27)
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

	//模版设置
	if (!biCol.isEdit()) {

		return false;
	}

	if (m_voBill == null) {
		biCol.setEnabled(false);
		return false;
	}


	//当入点不为仓库或存货时，表体行无存货则禁止输入其他值
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


	//辅计量
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
		//过滤辅单位
		else {
			if (sItemKey.equals("castunitname"))
				filterMeas(iRow);
			//固定换算率不可编辑
			else if (
				sItemKey.equals("hsl")
					&& m_voBill.getItemValue(iRow, "isSolidConvRate") != null
					&& ((Integer) m_voBill.getItemValue(iRow, "isSolidConvRate")).intValue() == 1) {
				isEditable = false;
			}

		}
	}
	//自由项
	else if (sItemKey.equals("vfree0")) {
		if (voInv.getIsFreeItemMgt() == null
			|| voInv.getIsFreeItemMgt().intValue() != 1) {
			isEditable = false;
		}
		//设置自由项参数
		else {
			//向自由项参照传入数据
			getFreeItemRefPane().setFreeItemParam(voInv);

		}
	}

	//批次
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
 * 创建者：仲瑞庆
 * 功能：由主数量依换算率算辅数量
	 公式：主数量=辅数量*换算率
 * 参数： iWhichChanged:
 							0 主数量修改
 							1 辅数量修改
 * 返回：
 * 例外：
 * 日期：(2001-10-15 14:13:55)
 * 修改日期，修改人，修改原因，注释标志：
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
		//无换算率
		/*
		if (m_voBill.getItemValue(iRow, "isAstUOMmgt") != null
			&& m_voBill.getItemValue(iRow, "isAstUOMmgt").toString().trim().length() != 0) {
			//是辅计量管理
			getBillCardPanel().setBodyValueAt(null, iRow, sMainNum);
			m_voBill.setItemValue(iRow, sMainNum, null);
			m_voBill.setItemValue(
				iRow,
				sAstNum,
				getBillCardPanel().getBodyValueAt(iRow, sAstNum));
		} else {
			//非辅计量管理
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
			//换算率小于零
			if (m_voBill.getItemValue(iRow, "isAstUOMmgt") != null
				&& m_voBill.getItemValue(iRow, "isAstUOMmgt").toString().trim().length() != 0) {
				if ((null != m_voBill.getItemValue(iRow, "fbillrowflag"))
					&& (Integer
						.valueOf(m_voBill.getItemValue(iRow, "fbillrowflag").toString())
						.intValue()
						== BillRowType.part)) {
					//是配件行
					//是辅计量管理,清辅数量，清换算率，置入主数量
					getBillCardPanel().setBodyValueAt(null, iRow, sAstNum);
					m_voBill.setItemValue(iRow, sAstNum, null);
					m_voBill.setItemValue(
						iRow,
						sMainNum,
						getBillCardPanel().getBodyValueAt(iRow, sMainNum));
					getBillCardPanel().setBodyValueAt(null, iRow, "hsl");
					m_voBill.setItemValue(iRow, "hsl", null);
				} else {
					//是辅计量管理,清主数量，清换算率，置入辅数量
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
				//非辅计量管理,清辅数量，清换算率，置入主数量
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
			//换算率等于零
			//getBillCardPanel().setBodyValueAt(ZERO, iRow, sMainNum);
			//m_voBill.setItemValue(iRow, sMainNum, ZERO);
			//m_voBill.setItemValue(
			//iRow,
			//sAstNum,
			//getBillCardPanel().getBodyValueAt(iRow, sAstNum));
		} else {
			//换算率大于零
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
				//是固定换算率
				if (iWhichChanged == 1) {
					//是辅数量更改
					if ((null != m_voBill.getItemValue(iRow, "fbillrowflag"))
						&& (Integer
							.valueOf(m_voBill.getItemValue(iRow, "fbillrowflag").toString())
							.intValue()
							== BillRowType.part)) {
						//是配件行
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
						//是套件行或其他行
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
					//是主数量更改或主数量不变
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
			} else { //非固定换算率
				if ((null != m_voBill.getItemValue(iRow, "fbillrowflag"))
					&& (Integer
						.valueOf(m_voBill.getItemValue(iRow, "fbillrowflag").toString())
						.intValue()
						== BillRowType.part)) {
					iWhichChanged= 1;
				}
				if (((null == ufdMainNum) || (ufdMainNum.doubleValue() == 0))
					&& ((null == ufdAstNum) || (ufdAstNum.doubleValue() == 0))) { //都为空或零
					//do nothing
				} else if ((iWhichChanged == 1)) {
					//辅数量修改
					if ((null != m_voBill.getItemValue(iRow, "fbillrowflag"))
						&& (Integer
							.valueOf(m_voBill.getItemValue(iRow, "fbillrowflag").toString())
							.intValue()
							== BillRowType.part)) {
						//是配件行
						if ((null == ufdMainNum) || (ufdMainNum.doubleValue() == 0)) {
							getBillCardPanel().setBodyValueAt(null, iRow, sAstNum);
							getBillCardPanel().setBodyValueAt(null, iRow, sMainNum);
						} else {
							ufdAstNum= ufdMainNum.div(hsl);
							getBillCardPanel().setBodyValueAt(ufdAstNum, iRow, sAstNum);
						}
					} else {
						//是套件行或其他行
						if (ufdAstNum != null) {
							ufdAstNum= ufdMainNum.div(hsl);
							getBillCardPanel().setBodyValueAt(ufdAstNum, iRow, sAstNum);
						}else if(ufdAstNum==null && ufdMainNum!=null && hsl!=null){
              ufdAstNum= ufdMainNum.div(hsl);
              getBillCardPanel().setBodyValueAt(ufdAstNum, iRow, sAstNum);
            }
					}
				}else if ((iWhichChanged == 2)) {
					//换算率修改
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
					//辅数量为空或零,主数量修改
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
					//辅数量不为空或零，主数量修改，此时修改换算率
					//修改人：刘家清 修改日期：2007-11-1上午10:20:45 修改原因：换算率永远为正
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
 * 创建者：仲瑞庆
 * 功能：
 * 参数：
 * 返回：
 * 例外：
 * 日期：(2001-11-8 19:47:29)
 * 修改日期，修改人，修改原因，注释标志：
 * @return nc.ui.pub.bill.BillData
 * @param oldBillData nc.ui.pub.bill.BillData
 */
protected BillData changeBillDataByUserDef(BillData oldBillData) {
	//进行自定义项定义用
	DefVO[] defs = null;
	//表头
	//查得对应于公司的该单据的自定义项设置
	defs = getDefHeadVO();
	if ((defs != null)) {

		oldBillData.updateItemByDef(defs, "vuserdef", true);
	}

	//表体
	//查得对应于公司的该单据的自定义项设置
	defs = getDefBodyVO();
	if ((defs == null)) {
		return oldBillData;
	}
	oldBillData.updateItemByDef(defs, "vuserdef", false);
	return oldBillData;
}

/**
 * 创建者：仲瑞庆
 * 功能：
 * 参数：
 * 返回：
 * 例外：
 * 日期：(2001-11-8 19:47:29)
 * 修改日期，修改人，修改原因，注释标志：
 * @return nc.ui.pub.bill.BillData
 * @param oldBillData nc.ui.pub.bill.BillData
 */
protected BillListData changeBillListDataByUserDef(BillListData oldBillData) {
	//进行自定义项定义用
	DefVO[] defs = null;
	//表头
	//查得对应于公司的该单据的自定义项设置
	defs = getDefHeadVO();
	if ((defs != null)) {
		oldBillData.updateItemByDef(defs, "vuserdef", true);
	}

	//表体
	//查得对应于公司的该单据的自定义项设置
	defs = getDefBodyVO();
	if ((defs == null)) {
		return oldBillData;
	}
	oldBillData.updateItemByDef(defs, "vuserdef", false);
	return oldBillData;
}

/**
 * 创建者：仲瑞庆

 * 功能：VO 转换

 * 参数：
 * 返回：
 * 例外：
 * 日期：(2001-8-16 12:53:03)
 * 修改日期，修改人，修改原因，注释标志：
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
		} else if (sHeaderItemKeyName[i].trim().equals("cgeneralhid")) { //清表头行中PK
			gbvo.setHeaderValue(sHeaderItemKeyName[i], "");
		} else if (sHeaderItemKeyName[i].trim().equals("vbillcode")) { //清表单单据号
			gbvo.setHeaderValue(sHeaderItemKeyName[i], "");
		} else if (sHeaderItemKeyName[i].trim().equals("coperatorid")) { //操作员
			gbvo.setHeaderValue(sHeaderItemKeyName[i], m_sUserID);
		} else if (sHeaderItemKeyName[i].trim().equals("coperatorname")) { //操作员
			gbvo.setHeaderValue(sHeaderItemKeyName[i], m_sUserName);
		} else if (sHeaderItemKeyName[i].trim().equals("dbilldate")) { //操作员
			gbvo.setHeaderValue(sHeaderItemKeyName[i], m_sLogDate);
		} else if (
			sHeaderItemKeyName[i].trim().equals("cauditorid")) { //调出人，普通单不能在此填入
			gbvo.setHeaderValue(sHeaderItemKeyName[i], null);
		} else if (
			sHeaderItemKeyName[i].trim().equals("cauditorname")) { //调出人，普通单不能在此填入
			gbvo.setHeaderValue(sHeaderItemKeyName[i], null);
		} else if (sHeaderItemKeyName[i].trim().equals("vadjuster")) { //调入人，普通单不能在此填入
			gbvo.setHeaderValue(sHeaderItemKeyName[i], null);
		} else if (
			sHeaderItemKeyName[i].trim().equals("vadjustername")) { //调入人，普通单不能在此填入
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
	//当前操作员
	gbvo.getHeaderVO().setCoperatorid(m_sUserID);
	gbvo.getHeaderVO().setCoperatoridnow(m_sUserID);
	gbvo.getHeaderVO().setPrimaryKey(null);
	//手工强制表头TS = null zhx 20030528
	gbvo.getHeaderVO().setTs(null);
	//签字人
	//gbvo.getHeaderVO().setCregister(m_sUserID);
	//可以不是当前登录单位的单据，所以不需要修改单位。
	//voHead.setPk_corp(m_sCorpID);
	//因为登录日期和单据日期是可以不同的，所以必须要登录日期。
	//gbvo.getHeaderVO().setDaccountdate(new nc.vo.pub.lang.UFDate(m_sLogDate));
	//vo可能要传给平台，所以要做成和签字后的单据
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
 * 创建者：仲瑞庆
 * 功能：
 * 参数：
 * 返回：
 * 例外：
 * 日期：(2001-8-16 12:53:03)
 * 修改日期，修改人，修改原因，注释标志：
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
		//注意特殊单SpecialItemVO错误的将cinvmanid作为基本档案ID，这里纠正
		if (sBodyItemKeyName[i].equalsIgnoreCase("cinvbasid"))
		    gbvo.setItemValue(j,"cinvbasid",sbvo.getItemValue(j,"cinvmanid"));

		//总入或出值
		UFDouble ufdTotal =
			(sbvo.getItemValue(j, "dshldtransnum") == null ? ZERO : (UFDouble) sbvo.getItemValue(j, "dshldtransnum"));
		//累入值
		UFDouble ufdAlreadyIn =
			(sbvo.getItemValue(j, "nadjustnum") == null ? ZERO : (UFDouble) sbvo.getItemValue(j, "nadjustnum"));
		//累出值
		UFDouble ufdAlreadyOut =
			(sbvo.getItemValue(j, "nchecknum") == null ? ZERO : (UFDouble) sbvo.getItemValue(j, "nchecknum"));
		//换算率
		UFDouble ufdHsl =
			(sbvo.getItemValue(j, "hsl") == null
				|| sbvo.getItemValue(j, "hsl").toString().trim().length() == 0 ? ZERO : (UFDouble) sbvo.getItemValue(j, "hsl"));

		if (sBodyItemKeyName[i].trim().equals("nshouldinnum")) {
			if (iInOutFlag == InOutFlag.OUT) {
			} else {
				//gbvo.setItemValue(j, sBodyItemKeyName[i], ufdTotal.sub(ufdAlreadyIn));
				//根据刘剑锋亚大新需求:转库,其它入库数量改为在途数量(累计出-累计入)
				//2.30 wnj comments 在子类实现数量的赋值
				//gbvo.setItemValue(j, sBodyItemKeyName[i], ufdAlreadyOut.sub(ufdAlreadyIn));

			}
		} else if (sBodyItemKeyName[i].trim().equals("nneedinassistnum")) {
			if (iInOutFlag == InOutFlag.OUT) {
			} else {
				if (sbvo.getItemValue(j, "nshldtransastnum") != null
					&& sbvo.getItemValue(j, "nshldtransastnum").toString().trim().length() != 0
					&& ufdHsl.doubleValue() != 0) {
					//gbvo.setItemValue(j, sBodyItemKeyName[i], ufdTotal.sub(ufdAlreadyIn).div(ufdHsl));
					//根据刘剑锋亚大新需求:转库,其它入库数量改为在途数量(累计出-累计入)
					//2.30 wnj comments 在子类实现数量的赋值
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
				//根据刘剑锋亚大新需求:转库,其它入库数量改为在途数量(累计出-累计入)
				//2.30 wnj comments 在子类实现数量的赋值
				//gbvo.setItemValue(j, sBodyItemKeyName[i], ufdAlreadyOut.sub(ufdAlreadyIn));
			}
		} else if (sBodyItemKeyName[i].trim().equals("ninassistnum")) {
			if (iInOutFlag == InOutFlag.OUT) {
			} else {
				if (sbvo.getItemValue(j, "nshldtransastnum") != null
					&& sbvo.getItemValue(j, "nshldtransastnum").toString().trim().length() != 0
					&& ufdHsl.doubleValue() != 0) {

					//gbvo.setItemValue(j, sBodyItemKeyName[i], ufdTotal.sub(ufdAlreadyIn).div(ufdHsl));
					//根据刘剑锋亚大新需求:转库,其它入库数量改为在途数量(累计出-累计入)
					//2.30 wnj comments 在子类实现数量的赋值
					//gbvo.setItemValue(
					//j,
					//sBodyItemKeyName[i],
					//ufdAlreadyOut.sub(ufdAlreadyIn).div(ufdHsl));
				}
			}
		} else if (sBodyItemKeyName[i].trim().equals("nshouldoutnum")) {
			if (iInOutFlag == InOutFlag.OUT) {
				//2.30 wnj comments 在子类实现数量的赋值
				//gbvo.setItemValue(j, sBodyItemKeyName[i], ufdTotal.sub(ufdAlreadyOut));
			} else {
			}
		} else if (sBodyItemKeyName[i].trim().equals("nshouldoutassistnum")) {
			//2.30 wnj comments 在子类实现数量的赋值
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
				//2.30 wnj comments 在子类实现数量的赋值
				//gbvo.setItemValue(j, sBodyItemKeyName[i], ufdTotal.sub(ufdAlreadyOut));
			} else {
			}
		} else if (sBodyItemKeyName[i].trim().equals("noutassistnum")) {
			if (iInOutFlag == InOutFlag.OUT) {
				//2.30 wnj comments 在子类实现数量的赋值
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
		} else if (sBodyItemKeyName[i].trim().equals("dbizdate")) { //表体出入库日期
			gbvo.setItemValue(j, sBodyItemKeyName[i], m_sLogDate);
		}

		gbvo.setItemValue(j, "isprimarybarcode", sbvo.getItemValue(j, "isprimarybarcode"));

		gbvo.setItemValue(j, "issecondarybarcode", sbvo.getItemValue(j, "issecondarybarcode"));
	}
	gbvo.setItemValue(j, "cgeneralbid", null);
	gbvo.setItemValue(j, "cgeneralhid", null);
	//手工强制表体TS = null zhx 20030528
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
清除定位颜色
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
 * 创建者：王乃军
 * 功能：清空列表和表单界面
 * 参数：
 * 返回：
 * 例外：
 * 日期：(2001-5-9 9:23:32)
 * 修改日期，修改人，修改原因，注释标志：
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
 * 创建者：仲瑞庆
 * 功能：将第二个参数中，非null且非空的值填入第一个VO中表体VO中
 * 参数：
 * 返回：
 * 例外：
 * 日期：(2001-12-14 11:25:17)
 * 修改日期，修改人，修改原因，注释标志：
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
 * 此处插入方法说明。
 * 功能描述:处理查询时没有查到数据的情况下界面的。
 * 作者：程起伍
 * 输入参数:
 * 返回值:
 * 异常处理:
 * 日期:(2003-6-9 15:57:49)
 */
protected void dealNoData() {
	m_iTotalListHeadNum = 0;
	m_iFirstSelListHeadRow = -1;
	m_iLastSelListHeadRow = -1;
	m_iLastSelCardBodyRow = -1;
	clearUi();
	showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill","UPP4008bill-000013")/*@res "未查到符合条件的单据。"*/);
}

/**
 * 创建者：仲瑞庆
 * 功能：查询后即时全部进行公式的执行，以备打印所用
 * 参数：
 * 返回：
 * 例外：
 * 日期：(2001-12-14 11:05:03)
 * 修改日期，修改人，修改原因，注释标志：
 * @param alVOs java.util.ArrayList
 */
protected void executeLoadFormularAfterQuery(ArrayList alVOs) {
	if (null == alVOs || alVOs.size() == 0)
		return;
	for (int i = 0; i < alVOs.size(); i++) {
		//置入VO
		getBillListPanel().setBodyValueVO(
			((AggregatedValueObject) alVOs.get(i)).getChildrenVO());
		//执行公式
		try {
			//执行加载公式
			getBillListPanel().getBodyBillModel().getFormulaParse().setNullAsZero(false);
			getBillListPanel().getBodyBillModel().execLoadFormula();
		} catch (Exception eee) {
			nc.vo.scm.pub.SCMEnv.error(eee);
		}
		//取出VO
		if ((AggregatedValueObject) alVOs.get(i) instanceof SpecialBillVO) {
			SpecialBillItemVO[] shvoSpecialHVOnow =
				new SpecialBillItemVO[getBillListPanel().getBodyTable().getRowCount()];
			for (int j = 0; j < shvoSpecialHVOnow.length; j++) {
				shvoSpecialHVOnow[j] = new SpecialBillItemVO();
			}
			getBillListPanel().getBodyBillModel().getBodyValueVOs(shvoSpecialHVOnow);
			//置回总VO
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
			//置回总VO
			combinedItemVOsByNotNullValue(
				(GeneralBillVO) alVOs.get(i),
				shvoSpecialHVOnow,
				true);
			//((AggregatedValueObject) alVOs.get(i)).setChildrenVO(shvoSpecialHVOnow);
		}
	}
}

/**
 * 创建者：仲瑞庆
 * 功能：预先定位
 * 参数：
 * 返回：
 * 例外：
 * 日期：(2001-12-18 20:47:09)
 * 修改日期，修改人，修改原因，注释标志：
 *2003-11-19 zss 将光标定位在存货编码处，因为如果定位在行号时执行盘点单的调整热键时就会清空行号。
 */
public void firstSetColEditable() {
	//预先定位
	if (getBillCardPanel().getBillTable().getRowCount() > 0
		&& getBillCardPanel().getBillTable().getColumnCount() > 0) {
		getBillCardPanel().getBillTable().setRowSelectionInterval(0, 0);
		getBillCardPanel().getBillTable().setColumnSelectionInterval(1, 1);

	}
}

/**
 * 创建者：王乃军
 * 功能：在表单设置显示VO,不更新界面状态updateValue()
 * 参数：
 * 返回：
 * 例外：
 * 日期：(2001-5-9 9:23:32)
 * 修改日期，修改人，修改原因，注释标志：
 *
 *
 *
 *
 */
public void freshTs(ArrayList alTs) throws Exception {
	if (alTs == null || alTs.size() == 0)
		throw new Exception(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill","UPP4008bill-000027")/*@res "传入的ts为空！"*/);
	setTs(m_voBill, alTs);
	setTs(m_iLastSelListHeadRow, alTs);
	setUiTs(alTs);
}

/**
   * 返回 ReturnDlg1 特性值。
   * @return nc.ui.ic.ic205.ReturnDlg
   */
/* 警告：此方法将重新生成。 */
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
   * 创建者：仲瑞庆
   * 功能：从表单界面中获得全部所需数据
   * 参数：
   * 返回：
   * 例外：
   * 日期：(2001-5-16 下午 12:30)
   * 修改日期，修改人，修改原因，注释标志：
   * @return nc.vo.ic.ic221.SpecialHVO
   */
protected SpecialBillVO getBillVO() {
	SpecialBillVO voNowBill = new SpecialBillVO(getBillCardPanel().getRowCount());

	getBillCardPanel().getBillValueVO(voNowBill);
	if (voNowBill == null
		|| voNowBill.getParentVO() == null
		|| voNowBill.getChildrenVO() == null) {
		nc.vo.scm.pub.SCMEnv.out("表中无数据!");
		return null;
	} else {
		return voNowBill;
	}
}

	/**
	 * 创建者：王乃军
	 * 功能：得到修改后的vo,用于修改后的保存
	 * 参数：
	 * 返回：
	 * 例外：
	 * 日期：(2001-5-9 9:23:32)
	 * 修改日期，修改人，修改原因，注释标志：
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
		//删除的行

		//vo数组的长度==当前显示的行数+删除的总行数
		int rowCount= vBodyData.size();
		int length= 0;
		Vector vDeleteRow= bmTemp.getDeleteRow();
		if (vDeleteRow != null)
			length= rowCount + vDeleteRow.size();
		else
			length= rowCount;
		//初始化返回的vo
		SpecialBillItemVO[] voaBody= new SpecialBillItemVO[length];

		int iRowStatus= nc.ui.pub.bill.BillModel.ADD;

		//整理当前界面上显示的行，包括原行、修改后的行、新增的行。
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
				} else //未修改的行只传PK即可
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
			//设置状态
			switch (iRowStatus) {
				case nc.ui.pub.bill.BillModel.ADD : //新增的行
					voaBody[i].setStatus(nc.vo.pub.VOStatus.NEW);
					break;
				case nc.ui.pub.bill.BillModel.MODIFICATION : //修改后的行
					voaBody[i].setStatus(nc.vo.pub.VOStatus.UPDATED);
					break;
				case nc.ui.pub.bill.BillModel.NORMAL : //原行
					voaBody[i].setStatus(nc.vo.pub.VOStatus.UNCHANGED);
					break;
			}
			//自由项
			voaBody[i].setFreeItemVO(voaItem[i].getFreeItemVO());
			voaBody[i].setInv(voaItem[i].getInv());

		} //删除的行
		if (vDeleteRow != null) {
			for (int i= 0; i < vDeleteRow.size(); i++) {
				//从rowCount开始写入！！！
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
				} //设置状态
				voaBody[i + rowCount].setStatus(nc.vo.pub.VOStatus.DELETED);
			}
		}
		return voaBody;
	}

/**
 * 返回 QueryConditionClient1 特性值。
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
//		//设置单据日期为当前登录日期
//		//modified by liuzy 2008-04-01 v5.03需求：库存单据查询增加起止日期
////		ivjQueryConditionDlg.setInitDate("dbilldate", m_sLogDate);
//		ivjQueryConditionDlg.setInitDate("dbilldate.from", m_sLogDate);
//		ivjQueryConditionDlg.setInitDate("dbilldate.end", m_sLogDate);
//		
//		//查询对话框显示打印次数页签。
//		ivjQueryConditionDlg.setShowPrintStatusPanel(true);
//		
//		//以下为对参照的初始化
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
//		//过滤入库仓库和出库仓库数据权限
//		//zhy2005-06-10特殊单只有入库仓库和出库仓库
//		//ivjQueryConditionDlg.setCorpRefs("head.pk_corp",new String[]{"cinwarehouseid","coutwarehouseid","inv.invcode","invcl.invclasscode"});
//    ivjQueryConditionDlg.setCorpRefs("head.pk_corp",GenMethod.getDataPowerFieldFromDlgNotByProp(ivjQueryConditionDlg));
//		
//
//	}
//	return ivjQueryConditionDlg;
//}

/**
 * 创建者：仲瑞庆
 * 功能：
 * 参数：
 * 返回：
 * 例外：
 * 日期：(2001-10-30 15:06:35)
 * 修改日期，修改人，修改原因，注释标志：
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
 * 创建者：邵兵
 * 功能：
 * 参数：
 * 返回：
 * 例外：
 * 日期：2005-03-12
 * 修改日期，修改人，修改原因，注释标志：
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
 * 功能：
 * 参数：
 * 返回：
 * 例外：
 * 日期：(2004-6-30 17:57:26)
 * 修改日期，修改人，修改原因，注释标志：
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
 * 功能：
 * 参数：
 * 返回：
 * 例外：
 * 日期：(2004-6-30 17:57:26)
 * 修改日期，修改人，修改原因，注释标志：
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
 * 此处插入方法说明。
 * 功能描述:纯字段查询需要公司字段加上别名。根据查询VO构造where 子句。
 * 作者：程起伍
 * 输入参数:
 * 返回值:
 * 异常处理:
 * 日期:(2003-6-27 8:56:11)
 * @return java.lang.String
 */
//protected String getExtenWhere() {
//	//返回值
//	StringBuffer sbWhere = new StringBuffer();
//	//查询VO
//	ConditionVO[] voConds = getConditionDlg().getConditionVO();
//	//查询VO中是否包含公司
//	boolean isHaveCorp = false;
//	//处理有查询条件的情况下：
//	if(voConds != null && voConds.length > 0){
//		int ilen = voConds.length;
//		String sFieldCode = null;
//
//		for(int i=0;i<ilen;i++){
//			sFieldCode = voConds[i].getFieldCode();
//			if("pk_corp".equals(sFieldCode) || "head.pk_corp".equals(sFieldCode)){
//				isHaveCorp = true;
//				//统一给公司字段加上别名head。
//				voConds[i].setFieldCode("head.pk_corp");
//			}
//			sbWhere.append(voConds[i].getSQLStr());
//		}
//		//如果查询条件中不包含公司，要加上。
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
//		//如果查询条件为空，加上公司。
//		sbWhere.append(" and head.pk_corp='");
//		sbWhere.append(m_sCorpID);
//		sbWhere.append("'");
//	}
//
//	return sbWhere.toString();
//}

/**
 * 此处插入方法说明。
 * 功能描述:纯字段查询需要公司字段加上别名。根据查询VO构造where 子句。
 * 作者：程起伍
 * 输入参数:
 * 返回值:
 * 异常处理:
 * 日期:(2003-6-27 8:56:11)
 * @return java.lang.String
 */
//protected String getExtenWhere(ConditionVO[] voConds) {
//	//返回值
//	StringBuffer sbWhere = new StringBuffer();
//	//查询VO中是否包含公司
//	boolean isHaveCorp = false;
//
//	//处理有查询条件的情况下：
//	if(voConds != null && voConds.length > 0){
//		int ilen = voConds.length;
//		String sFieldCode = null;
//		for(int i=0;i<ilen;i++){
//			sFieldCode = voConds[i].getFieldCode();
//			if("pk_corp".equals(sFieldCode) || "head.pk_corp".equals(sFieldCode)){
//				isHaveCorp = true;
//				//统一给公司字段加上别名head。
//				voConds[i].setFieldCode("head.pk_corp");
//			}
//			//modified by liuzy 2008-04-01 v5.03需求：库存单据查询增加起止日期
//			if (null != voConds[i] && null != voConds[i].getFieldCode()
//					&& (voConds[i].getFieldCode().equals("dbilldate.from") || voConds[i]
//							.getFieldCode().equals("dbilldate.end"))) {
//				voConds[i].setFieldCode("dbilldate");
//			}
//			
//			sbWhere.append(voConds[i].getSQLStr());
//		}
//		//如果查询条件中不包含公司，要加上。
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
//		//如果查询条件为空，加上公司。
//		sbWhere.append(" and head.pk_corp='");
//		sbWhere.append(m_sCorpID);
//		sbWhere.append("'");
//	}
//
//	return sbWhere.toString();
//}

/**
 * 此处插入方法说明。
 * 功能描述:获得 BillFormulaContainer
 * 作者：韩卫
 * 输入参数:
 * 返回值:
 * 异常处理:
 * 日期:(2003-7-2 9:48:12)
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
 * 此处插入方法说明。
 * 功能描述:
 * 作者：王乃军
 * 输入参数:
 * 返回值:
 * 异常处理:
 * 日期:(2003-6-25 20:43:17)
 * @return java.util.ArrayList
 */
protected ArrayList getFormulaItemBody()
{
	if (m_alFormulBodyItem == null)
	{
		m_alFormulBodyItem = new ArrayList();

		//辅助计量单位
		String[] aryItemField31 = new String[] { "measname", "castunitname", "castunitid" };
		m_alFormulBodyItem.add(aryItemField31);

		//来源单据类型
		String[] aryItemField9 = new String[] { "billtypename", "csourcetypename", "csourcetype" };
		m_alFormulBodyItem.add(aryItemField9);

		//源头单据号
		String[] aryItemField10 = new String[] { "billtypename", "cfirsttypename", "cfirsttype" };
		m_alFormulBodyItem.add(aryItemField10);

		//仓库名称
		String[] aryItemField15 = new String[] { "storname", "cwarehousename", "cwarehouseid" };
		m_alFormulBodyItem.add(aryItemField15);
		
	}
	return m_alFormulBodyItem;
}

/**
 * 此处插入方法说明。
 * 功能描述:默认的特殊单上的表头公式
 * 作者：王乃军
 * 输入参数:
 * 返回值:
 * 异常处理:
 * 日期:(2003-6-25 20:43:17)
 * @return java.util.ArrayList
 */
protected ArrayList getFormulaItemHeader()
{
	if (m_alFormulHeadItem==null){
	 m_alFormulHeadItem = new ArrayList();

	//原有的公式

	//入库业务员  库管员 经手人
	String[] aryItemField3 = new String[] { "psnname", "cinbsrname", "cinbsrid" };
	m_alFormulHeadItem.add(aryItemField3);

	//出库业务员  库管员 盘店员
	String[] aryItemField4 = new String[] { "psnname", "coutbsorname", "coutbsor" };
	m_alFormulHeadItem.add(aryItemField4);

	//入库部门
	String[] aryItemField19 = new String[] { "deptname", "cindeptname", "cindeptid" };
	m_alFormulHeadItem.add(aryItemField19);

	//出库部门
	String[] aryItemField29 = new String[] { "deptname", "coutdeptname", "coutdeptid" };
	m_alFormulHeadItem.add(aryItemField29);

	//入仓库名称
	String [] aryItemField15=new String[] {"storname","cinwarehousename","cinwarehouseid"};
	m_alFormulHeadItem.add(aryItemField15);
	// 出仓库	 3
	String[] aryItemField25 = new String[] { "storname", "coutwarehousename", "coutwarehouseid" };
	m_alFormulHeadItem.add(aryItemField25);

	//出库仓库货位管理
	String[] aryItemField26 = new String[] { "csflag", "isLocatorMgtOut", "coutwarehouseid" };
	m_alFormulHeadItem.add(aryItemField26);

	//出库仓库是否废品库
	String[] aryItemField27 = new String[] { "gubflag", "iswastewhout", "coutwarehouseid" };
	m_alFormulHeadItem.add(aryItemField27);


	//调整人
	String[] aryItemField2 = new String[] { "user_name", "vadjustername", "vadjuster" };
	m_alFormulHeadItem.add(aryItemField2);

	////审批人 5
	String[] aryItemField12 = new String[] { "user_name", "cauditorname", "cauditorid" };
	m_alFormulHeadItem.add(aryItemField12);

	////操作员 6
	String[] aryItemField1 = new String[] { "user_name", "coperatorname", "coperatorid" };
	m_alFormulHeadItem.add(aryItemField1);
	
////操作员 6
	String[] aryItemField111 = new String[] { "user_name", "clastmodiname", "clastmodiid" };
	m_alFormulHeadItem.add(aryItemField111);

	//for 成套件：组装单、拆卸单  是通过表体回写到表头 不需要这里通过公式解析实现
	////基本档案
	//String [] aryItemField20=new String[] {"pk_invbasdoc","pk_invbasdoc","ctj"};
	//m_alFormulHeadItem.add(aryItemField20);

	////名称 14
	//String [] aryItemField21=new String[] {"invname","ctjname","pk_invbasdoc"};
	//m_alFormulHeadItem.add(aryItemField21);

	}
	return m_alFormulHeadItem;
}

/**
 * 创建日期：(2003-3-4 17:13:59)
 * 作者：韩卫
 * 修改日期：
 * 修改人：
 * 修改原因：
 * 方法说明：
 * @return nc.ui.ic.pub.bill.InvoInfoBYFormula
 */
public InvoInfoBYFormula getInvoInfoBYFormula() {
	if (m_InvoInfoBYFormula==null)
	   m_InvoInfoBYFormula= new InvoInfoBYFormula(getCorpPrimaryKey());
	return m_InvoInfoBYFormula;
}

/**
   * 创建者：仲瑞庆
   * 功能：获得列表中的表头VO数组
   * 参数：
   * 返回：
   * 例外：
   * 日期：(2001-5-18 下午 12:09)
   * 修改日期，修改人，修改原因，注释标志：
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
 * 此处插入方法说明。
 * 创建日期：(2001-9-19 16:02:07)
 * @return nc.ui.ic.ic101.OrientDialog
 * @author:余大英
 */
public nc.ui.ic.pub.orient.OrientDialog getOrientDlg() {
		if (m_dlgOrient == null) {
		m_dlgOrient = new nc.ui.ic.pub.orient.OrientDialog(this);
	}
	return m_dlgOrient;
}

/**
 * 创建者：仲瑞庆
 * 功能：
 * 参数：
 * 返回：
 * 例外：
 * 日期：(2001-10-30 15:06:35)
 * 修改日期，修改人，修改原因，注释标志：
 */
protected nc.ui.pub.print.PrintEntry getPrintEntry() {
	if (null == m_print) {
		m_print= new nc.ui.pub.print.PrintEntry(this, null);
		m_print.setTemplateID(m_sCorpID, m_sPNodeCode, m_sUserID, null);
	}
	return m_print;
}

/**
 * 方法说明：取得列表形式下选中的单据
 * 创建日期：(2003-03-13 12:59:54)
 * 作者：赵宇煌
 * 修改日期：
 * 修改人：
 * 修改原因：
 * 算法说明：
 * @return java.util.ArrayList
 */
protected ArrayList getSelectedBills()
{

	ArrayList albill = new ArrayList();
	int iSelListHeadRowCount = getBillListPanel().getHeadTable().getSelectedRowCount();
	if (iSelListHeadRowCount <= 0)
	{
		showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill","UPP4008bill-000009")/*@res "请先选中单据！"*/);
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

	//查询表体数据
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
   * 创建者：仲瑞庆
   * 功能：从表单界面中获得更新数据
   * 参数：
   * 返回：
   * 例外：
   * 日期：(2001-5-16 下午 12:30)
   * 修改日期，修改人，修改原因，注释标志：
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
		nc.vo.scm.pub.SCMEnv.out("表中无数据!");
		return null;
	} else {
		return voNowBill;
	}
}

/**
 * 创建者：王乃军
 * 功能：得到仓库信息。
 * 参数：item key
 * 返回：
 * 例外：
 * 日期：(2001-5-8 19:08:05)
 * 修改日期，修改人，修改原因，注释标志：
 *
 *
 *
 *
 */
protected WhVO getWhInfoByID(String sWhID) {
	//仓库
	WhVO voWh = null;
	//先在缓存中查找仓库信息
	if (sWhID != null && m_htWh != null && m_htWh.containsKey(sWhID.trim()))
		voWh = (WhVO) m_htWh.get(sWhID.trim());
	else { //没有的话去读一下。
		try {
			//保存名称以在列表形式下显示。
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
 * 创建者：仲瑞庆
 * 功能：得到仓库信息。
 * 参数：item key
 * 返回：
 * 例外：
 * 日期：(2001-5-8 19:08:05)
 * 修改日期，修改人，修改原因，注释标志：
 *
 *
 *
 *
 */
protected WhVO getWhInfoByRef(String sItemKey) {
	//仓库
	WhVO voWh = null;
	try {
		String sID =
			((nc.ui.pub.beans.UIRefPane) getBillCardPanel()
				.getHeadItem(sItemKey)
				.getComponent())
				.getRefPK();

		//保存名称以在列表形式下显示。
		voWh = getWhInfoByID(sID);

	} catch (Exception e2) {
		nc.vo.scm.pub.SCMEnv.error(e2);
	}
	return voWh;
}

/**
 * 每当部件抛出异常时被调用
 * @param exception java.lang.Throwable
 */
protected void handleException(Exception exception) {

	/* 除去下列各行的注释，以将未捕捉到的异常打印至 stdout。 */
	nc.vo.scm.pub.SCMEnv.out("--------- 未捕捉到的异常 ---------");
	nc.vo.scm.pub.SCMEnv.error(exception);
}

/**
 * 简单初始化类。按传入参数，不读环境设置的操作员，公司等。
 */
/* 警告：此方法将重新生成。 */
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
		//初始化按钮
		initButtons();
		setButtons(m_aryButtonGroup);
		m_sUserID = sOperatorid;
		//m_sGroupID = sGroupid; //集团
		m_sUserName = sOperatorname;
		m_sCorpID = pk_corp;
		m_sLogDate = sLogDate;
		//读系统参数
		initSysParam();

		getBillCardPanel();
		getBillListPanel();
		setButtonState();
		setBillState();

		//过滤参照，必须放在getCEnvInfo()方法之后。
		filterRef(m_sCorpID);
		//得到当前的表体中系统默认的背景色
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
         * "重排行号"
         */);
  }
	return miAddNewRowNo;
}

protected UIMenuItem getLineCardEditItem() {
  if(miLineCardEdit==null){
    miLineCardEdit = new UIMenuItem(nc.ui.ml.NCLangRes
        .getInstance().getStrByID("common", "SCMCOMMONIC55YB002")/*
         * @res
         * "卡片编辑"
         */);
  }
  return miLineCardEdit;
}

/**
 * 创建者：王乃军
 * 功能：读系统参数
 * 参数：
 * 返回：
 * 例外：
 * 日期：(2001-5-9 9:23:32)
 * 修改日期，修改人，修改原因，注释标志：
 *
 *
 *
 *
 */
protected void initSysParam() {
	try {
		//库存参数表IC028:出库时是否指定入库单;批次参照跟踪是否到单据号.
		//IC010	是否保存即签字。

		//参数编码	含义				缺省值
		//BD501	数量小数位			2
		//BD502	辅计量数量小数位		2
		//BD503	换算率				2
		//BD504	存货成本单价小数位	2
//	  IC030 是否手工修改单据号
		String[] saParam =
			new String[] { "IC028", "IC010", "BD501", "BD502", "BD503", "BD504", "BD301" ,"IC030","IC050"};

		//传入的参数
		ArrayList alAllParam = new ArrayList();
		//查参数的必须数据
		ArrayList alParam = new ArrayList();
		alParam.add(m_sCorpID); //第一个是公司
		alParam.add(saParam); //待查的参数
		alAllParam.add(alParam);
		//查用户对应公司的必须参数
		alAllParam.add(m_sUserID);

		//返回的设置数据
		//ArrayList alRetData =null;
		
	
		//alRetData=(ArrayList)SpecialBillHelper.queryInfo(new Integer(QryInfoConst.INIT_PARAM), alAllParam);


		String[] saParamValue = nc.ui.ic.pub.tools.GenMethod.getSysParams(m_sCorpID, saParam);
		// 目前读两个。
		if (saParamValue == null || saParamValue.length <= 0) {
			showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"4008bill", "UPP4008bill-000045")/* @res "初始化参数错误！" */);
			return;
		}
		//读回的参数值
		//String[] saParamValue = (String[]) alRetData.get(0);
		//追踪到单据参数,默认设置为"N"
		if (saParamValue != null && saParamValue.length >= alAllParam.size()) {
			//是否保存即签字。默认设置为"N"
			//if(saParamValue[1]!=null)
			//m_sSaveAndSign = saParamValue[1].toUpperCase().trim();
			//BD501	数量小数位			2
			if (saParamValue[2] != null)
				m_iaScale[0] = Integer.parseInt(saParamValue[2]);
			//BD502	辅计量数量小数位		2
			if (saParamValue[3] != null)
				m_iaScale[1] = Integer.parseInt(saParamValue[3]);
			//BD503	换算率				2
			if (saParamValue[4] != null)
				m_iaScale[2] = Integer.parseInt(saParamValue[4]);
			//BD504	存货成本单价小数位	2
			if (saParamValue[5] != null)
				m_iaScale[3] = Integer.parseInt(saParamValue[5]);
			//BD301	本币小数位
			if (saParamValue[6] != null)
				m_iaScale[4] = Integer.parseInt(saParamValue[6]);

			if (saParamValue[7] != null&&"Y".equalsIgnoreCase(saParamValue[7].trim()))
				m_bIsEditBillCode = true;
			
			//IC050 存货参照是否按照仓库过滤
			if (saParamValue[8] != null&&"仓库".equalsIgnoreCase(saParamValue[8].trim()))
				m_isWhInvRef = true;
			else
				m_isWhInvRef = false;
		}
		//读回的操作对应的公司
		//m_alUserCorpID = (ArrayList) alRetData.get(1);

	} catch (Exception e) {
		nc.vo.scm.pub.SCMEnv.out("can not get para" + e.getMessage());
		if (e instanceof nc.vo.pub.BusinessException)
			showErrorMessage(e.getMessage());
	}
}

/**
 * 此处插入方法说明。是否查询计划价格
 * 创建日期：(2003-11-11 20:28:48)
 * @return boolean
 */
public boolean isQuryPlanprice() {
	return m_isQuryPlanprice;
}

/**
   * 创建者：仲瑞庆
   * 功能：递减SpecialHVO,在StationNumber位置
   * 参数：
   * 返回：
   * 例外：
   * 日期：(2001-5-16 下午 7:22)
   * 修改日期，修改人，修改原因，注释标志：
   * @param m_iStationNumber int
   */
protected void minusBillVO() {
	m_voBill = null;//修改：删除后，清空当前单据，解决删除单据后，打印预览仍有数据。 修改人：张学坤 修改日期：2007-12-3
	
	if (m_iTotalListHeadNum <= 1) { //最后一次删除
		m_alListData = new ArrayList();
		m_alListData.trimToSize();
		m_iLastSelListHeadRow = -1;
		m_iTotalListHeadNum = 0;
		return;
	}
	m_alListData.remove(m_iLastSelListHeadRow);
	m_alListData.trimToSize();

	//m_iTotalListHeadNum--; //列表表头目前存在的行数
	m_iTotalListHeadNum = m_alListData.size();

	if (m_iLastSelListHeadRow > m_iTotalListHeadNum - 1)
		m_iLastSelListHeadRow = m_iTotalListHeadNum - 1; //最后选中的列表表头行置为最后一个行号
}

/**
 * 创建者：仲瑞庆
 * 功能：指定为非负项
 * 参数：
 * 返回：
 * 例外：
 * 日期：(2001-12-5 14:31:47)
 * 修改日期，修改人，修改原因，注释标志：
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
 * 创建者：张欣
 * 功能：选择了业务类型
 * 参数：
 * 返回：
 * 例外：
 * 日期：(2001-5-9 9:23:32)
 * 修改日期，修改人，修改原因，注释标志：
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
	        //非自制方式
			if (nc.ui.pub.pf.PfUtilClient.isCloseOK()) {
			    
			    //2005-04-30 自由项变色龙。
				InvAttrCellRenderer ficr = new InvAttrCellRenderer();
				ficr.setFreeItemRenderer(getBillCardPanel(), m_voBill);
				//addied by liuzy 2008-07-03 供应商变色龙
				SupplierItemCellRender supplier = new SupplierItemCellRender(getBillCardPanel(),m_voBill);
				supplier.setRenderer("cvendorname");
				
				//result
				SpecialBillVO voRet = (SpecialBillVO) nc.ui.pub.pf.PfUtilClient.getRetVo();
				if(voRet!=null ){
					//新增单据
					onAdd();
					//显示单据
					setTempBillVO(voRet);
					//重设所有存货数据
					resetAllInvInfo(voRet);
					//重设新增单据初始数据，因为setTempBillVO把他们清掉了。
					setNewBillInitData();
				}

			}
		} else {
	        //自制单据
			onAdd();
		}
}

/**
 * 创建者：张欣
 * 功能：关联录入
 * 参数：
 * 返回：
 * 例外：
 * 日期：(2001-5-9 9:23:32)
 * 修改日期，修改人，修改原因，注释标志：
 *
 *
 *
 *
 */
protected void onJointAdd(ButtonObject bo) {
	//当前是列表形式时，首先切换到表单形式
	//========if (BillMode.List == m_iCurPanel)
	//======	onSwitch();
	nc.ui.pub.pf.PfUtilClient.retAddBtn(m_boAdd, m_sCorpID, m_sBillTypeCode, bo);
	updateButtons();

	try {
	} catch (Exception e) {
		handleException(e);
		nc.ui.pub.beans.MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON","UPPSCMCommon-000059")/*@res "错误"*/, e.getMessage());

	}

}

/**
 * 此处插入方法说明。
 * 功能描述:根据表体行的单据ID和单据类型联查上下游单据。
 * 作者：程起伍
 * 输入参数:
 * 返回值:
 * 异常处理:
 * 日期:(2003-4-22 16:09:14)
 */
public void onJointCheck() {

	if (m_iLastSelListHeadRow >= 0
		&& m_alListData != null
		&& m_alListData.size() > m_iLastSelListHeadRow
		&& m_alListData.get(m_iLastSelListHeadRow) != null) {

		SpecialBillVO voBill = null;
		SpecialBillHeaderVO voHeader = null;
		//得到单据VO
		voBill = (SpecialBillVO) m_alListData.get(m_iLastSelListHeadRow);
		//得到单据表头VO
		voHeader = voBill.getHeaderVO();

		if (voHeader == null) {
			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill","UPP4008bill-000098")/*@res "没有要联查的单据！"*/);
			return;
		}
		String sBillPK = null;
		String sBillTypeCode = null;
		String sBillCode = null;

		sBillPK = voHeader.getCspecialhid();
		sBillTypeCode = voHeader.getCbilltypecode();
		sBillCode = voHeader.getVbillcode();
		//如果sBillPK和sBillTypeCode为空，联查没有意义。
		if (sBillPK == null || sBillTypeCode == null) {
			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill","UPP4008bill-000099")/*@res "该行没有对应单据！"*/);
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
		showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON","UPPSCMCommon-000154")/*@res "没有联查的单据！"*/);
	}
}

/**
  * 创建者：仲瑞庆
  * 功能：粘贴行到表尾
  * 参数：
  * 返回：
  * 例外：
  * 日期：(2001-5-10 下午 4:15)
  * 修改日期，修改人，修改原因，注释标志：
  */
public void onPasteRowTail() {
	int iRowCount = getBillCardPanel().getBodyPanel().getTableModel().getRowCount();
	getBillCardPanel().pasteLineToTail();
	//增加的行数
	int iRowCount1 = getBillCardPanel().getBodyPanel().getTableModel().getRowCount();
	nc.ui.scm.pub.report.BillRowNo.addLineRowNos(getBillCardPanel(), m_sBillTypeCode, m_sBillRowNo, iRowCount1 - iRowCount);
	if (m_voBillItem != null) {
		int row = getBillCardPanel().getBillTable().getRowCount() - m_voBillItem.length;
		voBillPastLine(row);
	}

}

/**
* 创建者：仲瑞庆
* 功能：打印
* 参数：
* 返回：
* 例外：
* 日期：(2001-5-10 下午 4:16)
* 修改日期，修改人，修改原因，注释标志：
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
	    String sBillID = vo.getPrimaryKey();//单据主表的ID
	    ScmPrintlogVO voSpl = new ScmPrintlogVO();
	    voSpl.setCbillid(sBillID);
	    voSpl.setVbillcode(vo.getVBillCode());//传入单据号，用于显示。
	    voSpl.setCbilltypecode(vo.getBillTypeCode());//单据类型编码
	    voSpl.setCoperatorid((String) (vo.getParentVO().getAttributeValue("coperatorid")) );//操作员ID
	    voSpl.setIoperatetype(new Integer(PrintConst.PAT_OK));//固定
	    voSpl.setPk_corp(m_sCorpID);//公司
	    voSpl.setTs((String)(vo.getParentVO().getAttributeValue("ts")));//单据主表的TS

	    nc.vo.scm.pub.SCMEnv.out("ts=========tata" + voSpl.getTs());
		nc.ui.scm.print.PrintLogClient plc = new nc.ui.scm.print.PrintLogClient();
		plc.setPrintEntry(getPrintEntry());
		//设置单据信息
		plc.setPrintInfo(voSpl);
		//设置ts和printcount刷新监听.
		plc.addFreshTsListener(this);
		//设置打印监听
		getPrintEntry().setPrintListener(plc);

		if (getPrintEntry().selectTemplate() < 0)
			return;

		nc.vo.scm.pub.SCMEnv.showTime(ITime, "1:");

		ITime = System.currentTimeMillis();
		getDataSource().setVO(vo);
		nc.vo.scm.pub.SCMEnv.showTime(ITime, "2:");

		ITime = System.currentTimeMillis();
		//向打印置入数据源，进行打印
		getPrintEntry().setDataSource(getDataSource());
		nc.vo.scm.pub.SCMEnv.showTime(ITime, "3:");

		ITime = System.currentTimeMillis();
		getPrintEntry().preview();
		nc.vo.scm.pub.SCMEnv.showTime(ITime, "4:");



//	} else { //列表
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
//		//向打印置入数据源，进行打印
//		ITime = System.currentTimeMillis();
//		getPrintEntry().setDataSource(getDataSource());
//		getPrintEntry().preview();
//		nc.vo.scm.pub.SCMEnv.showTime(ITime, "List3:");
//	}
}

/**
 * 创建者：仲瑞庆
 * 功能：压缩条件VO，生成向BS端传入的参数
 * 参数：由查询窗口得到的条件VO
 * 返回：压缩后的条件VO
 * 例外：
 * 日期：(2001-8-2 下午 3:18)
 * 修改日期，修改人，修改原因，注释标志：
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
//		//单据状态
//		if (cvo[i].getFieldCode().trim().equals("qbillstatus")) {
//			String sValue= cvo[i].getRefResult().getRefPK().trim();
//			ConditionVO cvonew= (ConditionVO) cvo[i].clone();
//			//补上对左括弧的保留和逻辑关系的保留
//			cvonew.setFieldCode("1");
//			cvonew.setOperaCode("=");
//			cvonew.setDataType(1);
//			cvonew.setValue("1");
//			cvonew.setNoRight(true);
//			alcvo.add(cvonew);
//			if (sValue.equals("0")) { //审核
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
//			} else if (sValue.equals("1")) { //制单
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
//			} else if (sValue.equals("2")) { //全部
//				//do nothing
//			}
//			cvonew= (ConditionVO) cvo[i].clone();
//			//补上对右括弧的保留
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
 * 创建者：王乃军
 * 功能：查询指定的单据。
 * 参数：
billType, 当前单据类型
billID, 当前单据ID
businessType, 当前业务类型
operator, 当前用户ID
pk_corp, 当前公司ID

* 返回 ：单据vo
* 例外 ：
* 日期 ： (2001 - 5 - 9 9 : 23 : 32)
* 修改日期 ， 修改人 ， 修改原因 ， 注释标志 ：
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

		//公式情况 第一条表体记录公式查询补充数据 修改 hanwei  2003-03-05
			executeLoadFormularAfterQuery(alListData);
			//新值
			voRet = (SpecialBillVO) alListData.get(0);
			//修改辅计量calConvRate
			voRet.calConvRate();


	} catch (Exception e) {
		handleException(e);
		showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill","UPP4008bill-000015")/*@res "查询出错："*/ + e.getMessage());
	}
	return voRet;
}

/**
 * 创建者：王乃军
 * 功能：查询单据的表体，并把结果置到arraylist
 * 参数：	int iaIndex[],单据在alAlldata中的索引。
 			String saBillPK[]单据pk数组
 * 返回：
 * 例外：
 * 日期：(2001-5-9 9:23:32)
 * 修改日期，修改人，修改原因，注释标志：
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
		//修改人：刘家清 修改时间：2009-5-26 下午04:22:01 修改原因：转库单查询时列表状态下，存货编码不显示。
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
 * 创建者：王乃军
 * 功能：在表单设置显示VO,不更新界面状态updateValue()
 * 参数：
 * 返回：
 * 例外：
 * 日期：(2001-5-9 9:23:32)
 * 修改日期，修改人，修改原因，注释标志：
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
 * 创建者：王乃军
 * 功能：重新设置存货ID,带出存货其它数据，不清批次、自由项、数量等其它数据
 * 参数：行号，存货ID
 * 返回：
 * 例外：
 * 日期：(2001-5-8 19:08:05)
 * 修改日期，修改人，修改原因，注释标志：
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
		//存货，仓库
		ArrayList alInvID = new ArrayList(), alInvID2 = null;
		//重设每一行的存货ID,辅计量
		for (int i = 0; i < voBill.getItemVOs().length; i++) {
			alInvID2 = new ArrayList();
			alInvID2.add(voaItem[i].getAttributeValue("cinventoryid"));
			alInvID2.add(voaItem[i].getAttributeValue("castunitid"));
			alInvID.add(alInvID2);
		}
		//读存货数据条件
		ArrayList alIDs = new ArrayList();
		alIDs.add(alInvID);
		alIDs.add(m_sUserID);
		alIDs.add(m_sCorpID);

		//读存货数据
		//ArrayList alRetInvInfo=
		//(ArrayList) SpecialHBO_Client.queryInfo(
		//new Integer(nc.vo.ic.pub.bill.QryInfoConst.INVS_ASTUOM),
		//alIDs);
		ArrayList alRetInvInfo =
			(ArrayList) SpecialBillHelper.queryInfo( new Integer(QryInfoConst.INVS_ASTUOM), alIDs);
		//设置存货数据
		if (m_voBill != null && alRetInvInfo != null)
			for (int row = 0; row < alRetInvInfo.size(); row++) {
				nc.vo.scm.ic.bill.InvVO voInv = (InvVO) alRetInvInfo.get(row);
				if (voInv != null) {
					m_voBill.setItemInv(row, voInv);
					//表体
					setBodyInvValue(row, voInv);
					//补充setBodyInvValue()，重设存货编码，
					try {
						getBillCardPanel().getBillModel().removeTableModelListener(this);
						try {
							//存货编码
							String sInvCode = null;
							if (voInv.getAttributeValue("cinventorycode") != null)
								sInvCode = voInv.getAttributeValue("cinventorycode").toString();
							//设置表体VO
							getBillCardPanel().setBodyValueAt(sInvCode, row, "cinventorycode");
							//设置参照显示
							nc.ui.pub.beans.UIRefPane refInv =
								((nc.ui.pub.beans.UIRefPane) getBillCardPanel()
									.getBodyItem("cinventorycode")
									.getComponent());
							if (refInv != null) {
								refInv.setValue(sInvCode);
								refInv.setPK(voInv.getAttributeValue("cinventoryid"));
							}
							//项目
							String sProjectName = null;
							if (voInv.getAttributeValue("cprojectname") != null)
								sInvCode = voInv.getAttributeValue("cprojectname").toString();
							//设置表体VO
							getBillCardPanel().setBodyValueAt(sInvCode, row, "cprojectname");
							//设置参照显示
							refInv =
								((nc.ui.pub.beans.UIRefPane) getBillCardPanel()
									.getBodyItem("cprojectname")
									.getComponent());
							if (refInv != null) {
								refInv.setValue(sInvCode);
								refInv.setPK(voInv.getAttributeValue("cprojectid"));
							}
							//项目阶段
							String sProjectPhraseName = null;
							if (voInv.getAttributeValue("cprojectphrasename") != null)
								sInvCode = voInv.getAttributeValue("cprojectphrasename").toString();
							//设置表体VO
							getBillCardPanel().setBodyValueAt(sInvCode, row, "cprojectphrasename");
							//设置参照显示
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
		//表尾
		//setTailValue(voInv);
		//清批次/自由项等数据
		//clearRowData(0,row,"cinventorycode");
		//以下的信息需要优化，如果批次号未显示，则无需显示。
		//showHintMessage("存货修改，请重新确认批次、数量。");
		getBillCardPanel().getBillModel().setNeedCalculate(true);
		getBillCardPanel().getBillModel().reCalcurateAll();

	} catch (Exception e2) {
		nc.vo.scm.pub.SCMEnv.error(e2);
	}
}

/**
 * 类型说明：
 * 创建日期：(2002-11-21 14:46:22)
 * 作者：王亮
 * 修改日期：
 * 修改人：
 * 修改原因：
 * 算法说明：
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

/** 设置表单按钮状态
  * 此处插入方法说明。
  * 创建日期：(2001-4-30 13:58:35)
  */
protected void setBillState() {
	switch (m_iMode) {
		case BillMode.New : //新增
			getBillCardPanel().setEnabled(true);
			getBillListPanel().setVisible(false);
			getBillCardPanel().setVisible(true);
			break;
		case BillMode.Update : //修改
			getBillCardPanel().setEnabled(true);
			getBillListPanel().setVisible(false);
			getBillCardPanel().setVisible(true);
			break;
		case BillMode.Browse : //浏览
			getBillCardPanel().setEnabled(false);
			getBillListPanel().setVisible(false);
			getBillCardPanel().setVisible(true);
			//盘点单的"换算率作为取数纬度"默认可以编辑（任何情况下)
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
		case BillMode.List : //列表状态
			getBillCardPanel().setVisible(false);
			getBillListPanel().setVisible(true);
			break;
	}
}

/**
   * 创建者：王乃军
   * 功能：在表单设置显示VO
   * 参数：
   * 返回：
   * 例外：
   * 日期：(2001-5-9 9:23:32)
   * 修改日期，修改人，修改原因，注释标志：
	 2004-7-7  hanwei bExeformula
   * bExeformula:
	 是否执行公式，执行界面公式效率太低 ,3000条数据需要8秒，
	 261盘点选择不需要执行公式所以加这个参数
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
		nc.vo.scm.pub.SCMEnv.showTime(ITime, "setBillValueVO1时间：");
		ITime = System.currentTimeMillis();
		//刷新表尾
		//setTailValue(0);
		nc.vo.scm.pub.SCMEnv.showTime(ITime, "setBillValueVO2:setTailValue时间：");
		//执行加载公式
		ITime = System.currentTimeMillis();
		if (bExeformula)
		{
		getBillCardPanel().getBillModel().getFormulaParse().setNullAsZero(false);
		getBillCardPanel().getBillModel().execLoadFormula();
		}
		nc.vo.scm.pub.SCMEnv.showTime(ITime, "setBillValueVO3:execLoadFormula：");
	} catch (Exception e) {
		nc.vo.scm.pub.SCMEnv.error(e);
	} finally {
		long ITime = System.currentTimeMillis();
		getBillCardPanel().getBillModel().addTableModelListener(this);
		getBillCardPanel().addBillEditListenerHeadTail(this);
		nc.vo.scm.pub.SCMEnv.showTime(ITime, "setBillValueVO4时间：");
	}
}

/**
 * 此处插入方法说明。
   存货参照多选置界面
   供形态转换单等子类重载
 * 创建日期：(2003-11-11 20:48:18)
 * @param invVOs nc.vo.scm.ic.bill.InvVO[]
 */
public void setBodyInVO(InvVO[] invVOs, int iBeginRow) {/*
	int iCurRow = 0;
	getBillCardPanel().getBillModel().setNeedCalculate(false);
	for (int i = 0; i < invVOs.length; i++) {
		iCurRow = iBeginRow + i;
		
		//清批次
		String[] sIKs = getClearIDs(1, "cinventorycode");
		//金额，计划金额会自动清吗？
		clearRowData(iCurRow, sIKs);
		
		//设置界面数据
		m_voBill.setItemInv(iCurRow, invVOs[i]);
		//表体
		setBodyInvValue(iCurRow, invVOs[i]);
		//表尾
//		setTailValue(iCurRow);

	}

	getBillCardPanel().getBillModel().setNeedCalculate(true);
	getBillCardPanel().getBillModel().reCalcurateAll();

*/
	setBodyInVO(invVOs,iBeginRow,false);
}

/**
 *
 * 功能描述:行、列改变时设置单元格参照等信息。

 * 作者：wnj
 * 输入参数:
 * 返回值:
 * 异常处理:
 * 日期:(2003-9-27 15:57:49)

 */
protected void setCellRef(
	int rownow,
	int colnow,
	javax.swing.event.ListSelectionEvent e) {

}

/**
 * 此处插入方法说明。
 * 创建日期：(2003-11-11 20:28:48)
 * @param newQuryPlanprice boolean
 */
public void setIsQuryPlanprice(boolean newQuryPlanprice) {
	m_isQuryPlanprice = newQuryPlanprice;
}

/**
 * 此处插入方法说明。
 * 创建日期：(2003-11-11 20:28:48)
 * @param newQuryPlanprice boolean
 */
public void setQuryPlanprice(boolean newQuryPlanprice) {
	m_isQuryPlanprice = newQuryPlanprice;
}

/**
 * 创建者：王乃军
 * 功能：设置表体/表尾的小数位数
 * 参数：
 * 返回：
 * 例外：
 * 日期：(2001-11-23 18:11:18)
 *  修改日期，修改人，修改原因，注释标志：
 */
protected BillData setScale(BillData bd) {

	//考虑到单据模版内部的效率，单独拿出来效率更高些。因为表体数量较大，用哈希表实现.
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

	//表体数量
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
		//如果是此列
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
		//如果是此列
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
	//表体辅数量
	String[] saBodyAstNumItemKey=
		{
			"cyfsl",
			"naccountastnum",
			"ncheckastnum",
			"nadjustastnum",
			"nshldtransastnum" };
	for (int k= 0; k < saBodyAstNumItemKey.length; k++) {
		//如果是此列
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
		//如果是此列
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
	//表体单价
	String[] saBodyPrice= { "jhdj", "nprice", "nplannedprice" };
	for (int k= 0; k < saBodyPrice.length; k++) {
		//如果是此列
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
		//如果是此列
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
	//表体金额---->防止单据模版修改缺省小数长度
	String[] saBodyMny= { "je", "jhje", "nmny", "nplannedmny" };
	for (int k= 0; k < saBodyMny.length; k++) {
		//如果是此列
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
		//如果是此列
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

	//表尾数量
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
		//如果是此列
		try {
			bd.getTailItem(saTailNumItemKey[k]).setLength(DoubleScale.INT_LENGTH + 1 + m_iaScale[DoubleScale.NUM]);
			bd.getTailItem(saTailNumItemKey[k]).setDecimalDigits(m_iaScale[DoubleScale.NUM]);
		} catch (Exception e) {

		}
	}

	//组装费用，设置为本币精度 陈倪娜2009-11-13
	String[] saHeaderNumItemKey= { "nfixdisassemblymny" };
	for (int k= 0; k < saHeaderNumItemKey.length; k++) {
		//如果是此列
		try {
			bd.getHeadItem(saHeaderNumItemKey[k]).setLength(
				DoubleScale.INT_LENGTH + 1 + m_iaScale[DoubleScale.MNY]);
			bd.getHeadItem(saHeaderNumItemKey[k]).setDecimalDigits(m_iaScale[DoubleScale.MNY]);
		} catch (Exception e) {

		}
	}
	for (int k= 0; k < saHeaderNumItemKey.length; k++) {
		//如果是此列
		try {
			getBillListPanel().getHeadItem(saHeaderNumItemKey[k]).setLength(
				DoubleScale.INT_LENGTH + 1 + m_iaScale[DoubleScale.NUM]);
			getBillListPanel().getHeadItem(saHeaderNumItemKey[k]).setDecimalDigits(
				m_iaScale[DoubleScale.NUM]);
		} catch (Exception e) {

		}
	}

	//换算率
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
 * 创建者：王乃军
 * 功能：在表单设置显示VO,不更新界面状态updateValue()
 * 参数：
 * 返回：
 * 例外：
 * 日期：(2001-5-9 9:23:32)
 * 修改日期，修改人，修改原因，注释标志：
 *
 *
 *
 *
 */
public void setTempBillVO(SpecialBillVO bvo) {

	getBillCardPanel().getBillModel().removeTableModelListener(this);
	//保存一个clone()
	m_voBill = (SpecialBillVO) bvo.clone();
	//设置数据
	setBillValueVO(bvo);
	//更新状态 ---delete it to support CANCEL
	//getBillCardPanel().updateValue();
	//清存货现存量数据
	bvo.clearInvQtyInfo();
	//选中第一行
	getBillCardPanel().getBillTable().setRowSelectionInterval(0, 0);
	//刷新现存量显示
	//setTailValue(0);
	//重置其它数据
	nc.ui.pub.bill.BillModel bmTemp = getBillCardPanel().getBillModel();
	int iRowCount=0;
	if(bvo.getItemVOs()==null)
		iRowCount=bvo.getItemVOs().length;
	for (int row = 0; row < iRowCount; row++) {
		//设置行状态为新增
		if (bmTemp != null)
			bmTemp.setRowState(row, nc.ui.pub.bill.BillModel.ADD);
	}

	getBillCardPanel().getBillModel().addTableModelListener(this);
}

/**
 * 创建者：王乃军
 * 功能：在表单设置显示VO,不更新界面状态updateValue()
 * 参数：
 * 返回：
 * 例外：
 * 日期：(2001-5-9 9:23:32)
 * 修改日期，修改人，修改原因，注释标志：
 *
 *
 *
 *
 */
public void setTs(int iIndex, ArrayList alTs) throws Exception {
	//update bill in list data ------- security -----------
	SpecialBillVO voListBill = null;
	//设置m_voBill,以读取控制数据。
	if (iIndex >= 0
		&& m_alListData != null
		&& m_alListData.size() > iIndex
		&& m_alListData.get(iIndex) != null) {
		//这里不能clone(),改变了m_voBill同时改变m_alArrayListSpecialHVO
		voListBill = (SpecialBillVO) m_alListData.get(iIndex);
	}
	if (voListBill != null)
		setTs(voListBill, alTs);
}

/**
 * 创建者：王乃军
 * 功能：在表单设置显示VO,不更新界面状态updateValue()
 * 参数：
 * 返回：
 * 例外：
 * 日期：(2001-5-9 9:23:32)
 * 修改日期，修改人，修改原因，注释标志：
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
		throw new Exception(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill","UPP4008bill-000027")/*@res "传入的ts为空！"*/);
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
		//表体行数
		int iRowCount = voThisBill.getItemCount();
		SpecialBillItemVO voaItem[] = voThisBill.getItemVOs();
		Object oTempTs = null;
		for (int row = 0; row < iRowCount; row++) {
			if (voaItem[row].getCinventoryid() != null //特殊单有空行啊。
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
 * 创建者：王乃军
 * 功能：在表单设置显示VO,不更新界面状态updateValue()
 * 参数：
 * 返回：
 * 例外：
 * 日期：(2001-5-9 9:23:32)
 * 修改日期，修改人，修改原因，注释标志：
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
		throw new Exception(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill","UPP4008bill-000027")/*@res "传入的ts为空！"*/);
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
	//表体行数
	int iRowCount = getBillCardPanel().getRowCount();
	Object oTempTs = null;
	String sRowPK = null, sInvID = null;
	for (int row = 0; row < iRowCount; row++) {
		//if (voaItem[row].getCinventoryid() != null //特殊单有空行啊。
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
* 创建者：仲瑞庆
* 功能：复制行
* 参数：
* 返回：
* 例外：
* 日期：(2001-6-26 下午 9:32)
* 修改日期，修改人，修改原因，注释标志：
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
//    修改人：刘家清 修改日期：2007-8-21下午02:08:51 修改原因：复制时，要把invVO复制上，解决批次、自由项管理存货（如fql002），粘贴的时候没有复制自由项，另外粘贴的行批次和自由项不能编辑问题
      tempinvVO = m_voBillItem[i].getInv();
      SmartVOUtilExt.copyVOByVO(m_voBillItem[i], keys, uicopyvo, keys);
      m_voBillItem[i].setInv(tempinvVO);
		}
	}
	else {
		showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill","UPP4008bill-000025")/*@res "请先选中行。"*/);
	}
}

/**
 * 创建者：仲瑞庆
 * 功能：对应于行粘贴时的m_voBill的同步
 * 参数：
 * 返回：
 * 例外：
 * 日期：(2001-6-26 下午 9:32)
 * 修改日期，修改人，修改原因，注释标志：
 * 
 */
protected void voBillPastLine(int row) {
	//要求在已经增完行后执行

	if (row >= 0 && m_voBillItem != null) {
		//row = row - m_voaBillItem.length;
		for (int i = 0; i < m_voBillItem.length; i++){

			m_voBill.getChildrenVO()[row + i] = (SpecialBillItemVO) m_voBillItem[i].clone();
			m_voBill.getChildrenVO()[row+i].setAttributeValue(m_sBillRowNo,getBillCardPanel().getBodyValueAt(row+i,m_sBillRowNo));
//修改人：刘家清 修改日期：2007-8-21下午02:08:51 修改原因：粘贴时，要把invVO粘贴上，解决批次、自由项管理存货（如fql002），粘贴的时候没有复制自由项，另外粘贴的行批次和自由项不能编辑问题
      InvVO invvo = m_voBill.getItemVOs()[row + i].getInv();
      if(invvo!=null)
        invvo = (InvVO)invvo.clone();
      getBillCardPanel().getBillModel().setValueAt(invvo, row + i,"invvo");
			
		}
	}
}

/* （非 Javadoc）
 * @see nc.ui.scm.print.IFreshTsListener#freshTs(java.lang.String, java.lang.String, java.lang.Integer)
 *
 * 实现打印后对ts及printcount的更新.
 * @author 邵兵
 * 创建时间: 2004-12-23
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

	//判断打印的vo是否仍在缓存中．
	//在打印预览状态打印时，缓存vo可能会有改变，故需要判断．
	int index = 0;
	SpecialBillVO voBill = null;
	SpecialBillHeaderVO headerVO = null;
	for(; index < m_alListData.size(); index++){
		voBill = (SpecialBillVO) m_alListData.get(index);
		headerVO = voBill.getHeaderVO();

		//在sBillID传入时，已经判断sBillID不为null.
		if (sBillID.equals(headerVO.getPrimaryKey()))
			break;
	}

	if (index == m_alListData.size())  //不在缓存vo中，无需进行更新．
		return;

	//在缓存vo中
	headerVO.setAttributeValue("ts", sTS);
	headerVO.setAttributeValue("iprintcount", iPrintCount);

	if (m_iMode == BillMode.List){		//List
		int iPrintColumn = getBillListPanel().getHeadBillModel().getBodyColByKey("ts");
		getBillListPanel().getHeadBillModel().setValueAt(sTS, index, iPrintColumn);
		iPrintColumn = getBillListPanel().getHeadBillModel().getBodyColByKey("iprintcount");
		getBillListPanel().getHeadBillModel().setValueAt(iPrintCount, index, iPrintColumn);
	}else{		//Card
		if (index == m_iLastSelListHeadRow)	{		//如果为当前card显示vo.
			getBillCardPanel().setHeadItem("ts", sTS);
			getBillCardPanel().setTailItem("iprintcount", iPrintCount);
		}
	}
}
/**
 * 李俊
 * 功能：
 * 参数：
 * 返回：
 * 例外：
 * 日期：(2005-5-12 11:12:41)
 * 修改日期，修改人，修改原因，注释标志：
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

	//模版设置
	if (!biCol.isEdit()) {

		return false;
	}

	if (m_voBill == null) {
		biCol.setEnabled(false);
		return false;
	}


	//当入点不为仓库或存货时，表体行无存货则禁止输入其他值
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


	//辅计量
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
		//过滤辅单位
		else {
			if (sItemKey.equals("castunitname"))
				filterMeas(iRow);
			//固定换算率不可编辑
			else if (
				sItemKey.equals("hsl")
					&& m_voBill.getItemValue(iRow, "isSolidConvRate") != null
					&& ((Integer) m_voBill.getItemValue(iRow, "isSolidConvRate")).intValue() == 1) {
				isEditable = false;
			}

		}
	}
	//自由项
	else if (sItemKey.equals("vfree0")) {
		if (voInv.getIsFreeItemMgt() == null
			|| voInv.getIsFreeItemMgt().intValue() != 1) {
			isEditable = false;
		}
		//设置自由项参数
		else {
			//向自由项参照传入数据
			getFreeItemRefPane().setFreeItemParam(voInv);

		}
	}

	//批次
	else if (sItemKey.equals("vbatchcode")) {
		if (voInv.getIsLotMgt() == null || voInv.getIsLotMgt().intValue() != 1) {
      Object oBatchcode = getBillCardPanel().getBodyValueAt(iRow,"vbatchcode");
      if(!nc.vo.ic.pub.GenMethod.isSEmptyOrNull((String)oBatchcode))
        getBillCardPanel().setBodyValueAt(oBatchcode.toString().trim() + " ", iRow, "vbatchcode");

			isEditable = false;
		}
	}	
	else if (sItemKey.equals("dvalidate") || sItemKey.equals("scrq")) {
		
		//修改人：刘家清 修改日期：2007-9-7上午10:59:21 修改原因：根据20070907，与谢阳、杨波、刘辉讨论后，决定在代码里控制特殊单的生产日期和失效日期是不可编辑，不放到单据模板里控制
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
 * 创建日期：(2001-10-26 14:31:14)
 * @param key java.lang.String
 */
protected ClientUISortCtl getListHeadSortCtl() {
	return m_listHeadSortCtl;
}

/**
 * get
 * 创建日期：(2001-10-26 14:31:14)
 * @param key java.lang.String
 */
protected ClientUISortCtl getListBodySortCtl() {
	return m_listBodySortCtl;
}

/**
 * get
 * 创建日期：(2001-10-26 14:31:14)
 * @param key java.lang.String
 */
protected ClientUISortCtl getCardBodySortCtl() {
	return m_cardBodySortCtl;
}

/**
 * 排序后触发。
 * 创建日期：(2001-10-26 14:31:14)
 * @param key java.lang.String
 */
public void afterSortEvent(boolean iscard,boolean ishead,String key) {

	if(ishead){
		//当针对表头一条记录排序，后处理不需要 因为m_sortRelaDatas没有值 陈倪娜 2009-11-12
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
 * 排序前触发。
 * 创建日期：(2001-10-26 14:31:14)
 * @param key java.lang.String
 */
public void beforeSortEvent(boolean iscard,boolean ishead,String key) {
	clearOrientColor();
	// 如果是表头排序
	if (ishead) {
		SCMEnv.out("表头排序");
		if (m_alListData == null || m_alListData.size() <= 1) {
			// 说明没有排序的必要
			return;
		}
		getListHeadSortCtl().addRelaSortData(m_alListData);
		
	} else {
		SCMEnv.out("表体排序");
		
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
 * 列表表头排序后触发,当前行变化
 * 创建日期：(2001-10-26 14:31:14)
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
 * 功能描述:退出系统
 */
public boolean onClosing() {
 //正在编辑单据时退出提示    
 if(m_iMode==BillMode.New || m_iMode==BillMode.Update){
	 
	 int iret=MessageDialog.showYesNoCancelDlg(this,null,nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UCH001")/*@res "是否保存已修改的数据？"*/);
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
 * UI关联操作-审批
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
            "UPPSCMCommon-000270")/* @res "提示" */,
        nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill",
            "UPP4008bill-000062")/* @res "没有符合查询条件的单据！" */);
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
 * showBtnSwitch 符合界面规范
 * 
 * @author leijun 2006-5-24
 */
public void showBtnSwitch(){
	if(m_iMode == BillMode.List)
		m_boList.setName(nc.ui.ml.NCLangRes.getInstance().getStrByID(
				"common", "UCH021")/* @res "卡片显示" */);
	else
		m_boList.setName(nc.ui.ml.NCLangRes.getInstance().getStrByID(
				"common", "UCH022")/* @res "列表显示" */);
		
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
 * 创建人：刘家清
创建日期：2007-12-26上午09:27:52
创建原因：自动设置界面上已有的所有行的行号
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
 * 创建人 yb
创建日期：2007-12-26上午09:27:52
创建原因：卡片编辑
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
 * 创建人：刘家清 创建时间：2008-7-16 下午03:43:33 创建原因： 错误信息提示，如果涉及到某些行的数据，行背景色变为黄色
 * @param e
 */ 
/* public void setRowColorWhenException(ICException e){
		// 更改颜色
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
 * 单据表体菜单动作监听.
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
 * 将后台保存后的VO设置到Model中。
 * 
 * @param voSaved
 */
public void freshVOBySmallVO(SMSpecialBillVO voSaved) {
	// 1.首先刷新时间戳，OID等信息到Model
	BillModel bm = getBillCardPanel().getBillModel();
	BillCardPanel bcp = getBillCardPanel();

	GeneralBillUICtl ctl = new GeneralBillUICtl();
	ctl.setBillCardPanelData(bcp, voSaved);
	// 2.刷新时间戳，OID等信息到m_voBill
	m_voBill.setSmallVO(voSaved);
	//重设VO状态
	m_voBill.setStatus(VOStatus.UNCHANGED);
	// 3.更新列表数据
	m_alListData.set(m_iLastSelListHeadRow, m_voBill.clone());

}
/**
 * 此处插入方法说明
 * 作者：lirr
 * 日期：2009-10-30下午03:39:35
 * 修改日期 2009-10-30下午03:39:35 修改人，lirr 修改原因 ：存量转出时不应该清除批次，注释标志：
	@param invVOs
	@param row
	@param isClearBcode 增加参数是否清除批次号 
 */
public void afterInvMutiEdit(InvVO[] invVOs,Integer row,boolean isClearBcode){
	//界面增行
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

	//单据行号
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
	SCMEnv.showTime(ITime, "设置界面数据:");
	//以下的信息需要优化，如果批次号未显示，则无需显示。
	showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill","UPP4008bill-000155")/*@res "存货修改，请重新确认自由项、批次、数量。"*/);

	SCMEnv.showTime(ITime, "存货界面设置:");
}
/**
 * 此处插入方法说明
 * 作者：lirr
 * 日期：2009-10-30下午03:43:50
 * 修改日期 2009-10-30下午03:43:50 修改人，lirr 修改原因 存量转出时不应该清除批次，，注释标志：
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
			//清批次
			String[] sIKs = getClearIDs(1, "cinventorycode");
			//金额，计划金额会自动清吗？
			clearRowData(iCurRow, sIKs);
		}
		//设置界面数据
		m_voBill.setItemInv(iCurRow, invVOs[i]);
		//表体
		setBodyInvValue(iCurRow, invVOs[i]);
		//表尾
//		setTailValue(iCurRow);

	}

	getBillCardPanel().getBillModel().setNeedCalculate(true);
	getBillCardPanel().getBillModel().reCalcurateAll();

}


/**
 * 表体状态是Deleted的删除   陈倪娜 2009-11-24
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
 * 方法功能描述：简要描述本方法的功能。 清除模板缓存中的删除行
 * <p>
 * <b>examples:</b>
 * <p>
 * 使用示例
 * <p>
 * <b>参数说明</b>
 * @param bm
 * <p>
 * @author lirr
 * @time 2009-11-30 下午06:56:51
 */
private void filterDeletedItems(BillModel bm){
       Vector vDeleteRow= bm.getDeleteRow();
       if(vDeleteRow!= null && vDeleteRow.size() > 0)
       vDeleteRow.removeAllElements();
    }
   


}