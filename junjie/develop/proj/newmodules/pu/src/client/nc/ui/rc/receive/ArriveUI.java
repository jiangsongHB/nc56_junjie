package nc.ui.rc.receive;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Vector;

import javax.swing.JComponent;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ListSelectionListener;

import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.itf.po.outer.IQueryForIc;
import nc.itf.rc.receive.IArriveorder;
import nc.itf.rc.receive.IQueryForInitUI;
import nc.itf.uap.IUAPQueryBS;
import nc.itf.uap.IVOPersistence;
import nc.itf.uap.pf.IPFWorkflowQry;
import nc.itf.uap.rbac.IUserManageQuery;
import nc.itf.uap.sf.ICreateCorpQueryService;
import nc.itf.uap.workshop.plugins.formdev.IUAPBillService;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.jdbc.framework.processor.MapProcessor;
import nc.jdbc.framework.processor.ResultSetProcessor;
import nc.ui.bd.ref.IBusiType;
import nc.ui.ic.pub.lot.LotNumbRefPane;
import nc.ui.ml.NCLangRes;
import nc.ui.pf.query.ICheckRetVO;
import nc.ui.po.OrderHelper;
import nc.ui.po.pub.PoPublicUIClass;
import nc.ui.po.pub.PoToBackRcQueDLG;
import nc.ui.po.ref.OtherRefModel;
import nc.ui.pr.pray.IButtonConstPr;
import nc.ui.pu.jj.JJPuScmPubHelper;
import nc.ui.pu.jjpanel.InfoCostPanel;
import nc.ui.pu.pub.ATPForOneInvMulCorpUI;
import nc.ui.pu.pub.CheckISSellProxyHelper;
import nc.ui.pu.pub.PuGetUIValueTool;
import nc.ui.pu.pub.PuProjectPhaseRefModel;
import nc.ui.pu.pub.PuTool;
import nc.ui.pu.pub.PubHelper;
import nc.ui.pu.pub.SerialAllocationDlg;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.FramePanel;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UIMenuItem;
import nc.ui.pub.beans.UIRefPane;
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
import nc.ui.pub.bill.BillListPanel;
import nc.ui.pub.bill.BillModel;
import nc.ui.pub.bill.BillScrollPane;
import nc.ui.pub.bill.BillTableMouseListener;
import nc.ui.pub.bill.BillUIUtil;
import nc.ui.pub.bill.IBillData;
import nc.ui.pub.bill.IBillModelSortPrepareListener;
import nc.ui.pub.bill.IBillRelaSortListener2;
import nc.ui.pub.linkoperate.ILinkAdd;
import nc.ui.pub.linkoperate.ILinkAddData;
import nc.ui.pub.linkoperate.ILinkApprove;
import nc.ui.pub.linkoperate.ILinkApproveData;
import nc.ui.pub.linkoperate.ILinkMaintain;
import nc.ui.pub.linkoperate.ILinkMaintainData;
import nc.ui.pub.linkoperate.ILinkQuery;
import nc.ui.pub.linkoperate.ILinkQueryData;
import nc.ui.pub.para.SysInitBO_Client;
import nc.ui.pub.pf.PfUtilClient;
import nc.ui.pub.workflownote.FlowStateDlg;
import nc.ui.rc.pub.BackReasonRefModel;
import nc.ui.rc.pub.CPurchseMethods;
import nc.ui.rc.pub.InvRefModelForRepl;
import nc.ui.rc.pub.LocateDlg;
import nc.ui.rc.pub.PurchasePrintDS;
import nc.ui.scm.ic.freeitem.FreeItemRefPane;
import nc.ui.scm.ic.setpart.SetPartDlg;
import nc.ui.scm.inv.InvTool;
import nc.ui.scm.print.BillPrintTool;
import nc.ui.scm.print.SplitParams;
import nc.ui.scm.print.SplitPrintParamDlg;
import nc.ui.scm.pu.ParaVOForBatch;
import nc.ui.scm.pub.BillTools;
import nc.ui.scm.pub.BusiBillManageTool;
import nc.ui.scm.pub.CacheTool;
import nc.ui.scm.pub.CollectSettingDlg;
import nc.ui.scm.pub.bill.ButtonTree;
import nc.ui.scm.pub.bill.IBillExtendFun;
import nc.ui.scm.pub.def.DefSetTool;
import nc.ui.scm.pub.panel.RelationsCal;
import nc.ui.scm.pub.print.ISetBillVO;
import nc.ui.scm.pub.print.ScmPrintTool;
import nc.ui.scm.pub.report.BillRowNo;
import nc.ui.scm.pub.sourceref.SourceRefDlg;
import nc.ui.scm.ref.WarehouseRefModel;
import nc.vo.bd.b06.PsndocVO;
import nc.vo.bd.def.DefVO;
import nc.vo.pf.change.PfUtilBaseTools;
import nc.vo.po.OrderVO;
import nc.vo.po.pub.Operlog;
import nc.vo.po.pub.OrderPubVO;
import nc.vo.po.pub.SerialUiVO;
import nc.vo.pu.exception.RwtRcToPoException;
import nc.vo.pu.exception.RwtRcToScException;
import nc.vo.pu.pub.InitValue;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.CommonConstant;
import nc.vo.pub.ProductCode;
import nc.vo.pub.VOStatus;
import nc.vo.pub.ValidationException;
import nc.vo.pub.bill.BillTempletVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.lang.UFTime;
import nc.vo.pub.query.ConditionVO;
import nc.vo.rc.receive.ArrivePubVO;
import nc.vo.rc.receive.ArriveorderHeaderVO;
import nc.vo.rc.receive.ArriveorderItemVO;
import nc.vo.rc.receive.ArriveorderVO;
import nc.vo.scm.constant.ScmConst;
import nc.vo.scm.datapower.BtnPowerVO;
import nc.vo.scm.ic.bill.FreeVO;
import nc.vo.pu.jjvo.InformationCostVO;
import nc.vo.scm.pu.BillStatus;
import nc.vo.scm.pu.BillTypeConst;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.scm.pub.SCMEnv;
import nc.vo.scm.pub.session.ClientLink;
import nc.vo.scm.service.ServcallVO;
import nc.vo.uap.pf.PFBusinessException;


/**
 * @功能描述：到货单维护
 * @作者：晁志平
 * @创建日期：(2001-5-24 9:42:56)
 */
public class ArriveUI
  extends nc.ui.pub.ToftPanel
  implements BillEditListener,  BillTableMouseListener, ActionListener, 
  BillEditListener2, ListSelectionListener, BillBodyMenuListener, IBillModelSortPrepareListener,
  ISetBillVO, IBillExtendFun, ICheckRetVO, IBillRelaSortListener2, ILinkMaintain,//关联修改
  ILinkAdd,//关联新增 
  ILinkApprove,//审批流
  ILinkQuery,//逐级联查
  BillActionListener
  {
	private InformationCostVO[] vos = null;
	private ButtonObject boInfoCost ;//费用录入按钮  add by QuSida 2010-8-28 (佛山骏杰)
	private ButtonObject[] extendBtns ; //二次开发按钮数组  add by QuSida 2010-8-28 (佛山骏杰)
	private UFDouble arrnumber ;//实际到货数量
	private UFDouble arrnum ;//累计到货数量
//	private UFDouble arrmny;//累计到货金额
	
    //取系统参数：
    private int m_iPowerAssNum = 2;
    private int m_iPowerConvertRate= 2;
    private int m_iPowerNum = 2;
    private int m_iPowerMoney = 2;
    private int m_iPowerPrice = 2;
    //AIM是否启用
    private boolean m_bAIMEnabled = false;
    //QC是否启用
    private boolean m_bQcEnabled = false;
    //按钮树实例,since v51
    private ButtonTree m_btnTree = null;  
    
    //	表体排序监听
    class IBillRelaSortListener2Body implements IBillRelaSortListener2 {
        public Object[] getRelaSortObjectArray() {
            return ArriveUI.this.getRelaSortObjectArrayBody();
        }
    }
    private boolean isRevise = false;
    private nc.itf.uap.pf.IPFWorkflowQry ipflowqry = ((IPFWorkflowQry) NCLocator.getInstance().lookup(IPFWorkflowQry.class.getName()));

    //since v55,重排行号
    UIMenuItem m_miReSortRowNo = null;
    UIMenuItem m_miCardEdit = null;
    private ButtonObject m_btnReSortRowNo = null;
    private ButtonObject m_btnCardEdit = null;
    //since v55,重排行号监听
    class IMenuItemListener implements ActionListener{
        public void actionPerformed(ActionEvent event){
            ArriveUI.this.onMenuItemClick(event);
        };
    }  
  // 序列号对话框
  private SerialAllocationDlg m_dlgSerialAllocation = null;
  // 保存当前新增或修改的单据的序列号分配数据
  public HashMap m_alSerialData =  new HashMap();
  // 缓存当前新增或修改的单据的序列号分配数据
  public HashMap m_alSerialDataCancel =  new HashMap();

  //列表是否加载过
  private boolean m_bLoaded = false;
  
  //是否需要加载来源单据行号等信息
  //private boolean m_bNeedLoadSourceInfo = true;
  
  private boolean m_bLoadedCard = false;
  //打不开此节点的原因
  private String m_strNoOpenReasonMsg = null;
  //
  private boolean m_bQueriedFlag = false;
  
    //解决自由项不能正确加载问题：
    class MyBillData implements IBillData{
    public void prepareBillData(nc.ui.pub.bill.BillData bd) {
        ArriveUI.this.initBillBeforeLoad(bd);
    }
  }
  /*所有表体缓存*/
  private Hashtable hBodyItem = new Hashtable();
  //是否改变了业务类型
  boolean isChangeBusitype = true;
  /**揸序时特殊处理用*/
  private boolean isFrmList = false;
  /**存放当前表头对应的表体*/
  private ArriveorderItemVO[] items = null;

  //定位对话框
  private LocateDlg locatedlg = null;
  //到货卡片
  private BillCardPanel m_billPanel = null;
  //到货查询结果
  private ArriveorderVO[] m_arriveVOs = null;
  //到货列表
  private BillListPanel m_arrListPanel = null;
    //存量查询对话框
  ATPForOneInvMulCorpUI m_atpDlg = null;
  /*采购退货查询框*/
  private PoToBackRcQueDLG m_backQuePoDlg = null;
  /*委外退货查询框*/
  private RcToScQueDLG m_backQueScDlg = null;
  /*退货参照采购订单选择界面*/
  private ArrFrmOrdUI m_backRefUIPo = null;
  /*退货参照委外订单选择界面*/
  private ArrFrmOrdUI m_backRefUISc = null;
  
  //多语翻译工具
  private NCLangRes m_lanResTool = NCLangRes.getInstance();
  
  /*卡片按钮定义*/
  private ButtonObject m_btnCheck = null;
  //业务类型
  private ButtonObject m_btnBusiTypes = null;
  //增加组
  private ButtonObject m_btnAdds = null;
  //退货组
  private ButtonObject m_btnBackPo = null;
  private ButtonObject m_btnBackSc = null;
  private ButtonObject m_btnBacks = null;
  //单据维护组
  private ButtonObject m_btnModify = null;
  private ButtonObject m_btnSave = null;
  private ButtonObject m_btnCancel = null;
  private ButtonObject m_btnDiscard = null;
  private ButtonObject m_btnSendAudit = null;  
  private ButtonObject m_btnMaintains = null;
  //行操作组
  private ButtonObject m_btnDelLine = null;
  private ButtonObject m_btnCpyLine = null;
  private ButtonObject m_btnPstLine = null;
  //since v55, 粘贴行到表尾
  private ButtonObject m_btnPstLineTail = null;
  private ButtonObject m_btnLines = null;
  //单据浏览组
  private ButtonObject m_btnBrowses = null;
  private ButtonObject m_btnQuery = null;
  private ButtonObject m_btnFirst = null;
  private ButtonObject m_btnPrev = null;
  private ButtonObject m_btnNext = null;
  private ButtonObject m_btnLast = null;
  private ButtonObject m_btnLocate = null;
  private ButtonObject m_btnRefresh = null;
  //切换
  private ButtonObject m_btnList = null;
  //执行组(审批、弃审与消息中心共用)
  private ButtonObject m_btnActions = null;
  //辅助组
  private ButtonObject m_btnOthers = null;
  public  ButtonObject m_btnCombin = null;
  private ButtonObject m_btnPrints = null;
  private ButtonObject m_btnPrint = null;
  private ButtonObject m_btnPrintPreview = null;
  private ButtonObject m_btnUsable = null;
  private ButtonObject m_btnQueryBOM = null;
  private ButtonObject m_btnQuickReceive = null;
  protected ButtonObject m_btnDocument = null;
  protected ButtonObject m_btnLookSrcBill = null;
  private ButtonObject m_btnQueryForAudit = null;
  private ButtonObject m_btnCreateCard = null;
  private ButtonObject m_btnDeleteCard = null;
//  private ButtonObject m_btnSerialNO = null;


  /*卡片按钮菜单*/
  private ButtonObject m_aryArrCardButtons[] = null;
  
  /*列表按钮定义*/
  private ButtonObject m_btnSelectAll = null;
  private ButtonObject m_btnSelectNo = null;
  private ButtonObject m_btnModifyList = null;
  private ButtonObject m_btnDiscardList = null;
  private ButtonObject m_btnQueryList = null;
  private ButtonObject m_btnCard = null;
  private ButtonObject m_btnEndCreate = null;
  private ButtonObject m_btnRefreshList = null;
  
  //辅助组
  private ButtonObject m_btnUsableList = null;
  protected ButtonObject m_btnDocumentList = null;
  private ButtonObject m_btnQueryBOMList = null;
  private ButtonObject m_btnPrintPreviewList = null;
  private ButtonObject m_btnSplitPrint = null;
  private ButtonObject m_btnPrintList = null;
  private ButtonObject m_btnOthersList = null;

  /*列表按钮组*/
  private ButtonObject m_aryArrListButtons[] = null;
  /*消息中心按钮组*/
  private ButtonObject m_btnAudit = null;
  private ButtonObject m_btnUnAudit = null;
  private ButtonObject m_btnOthersMsgCenter = null;
  private ButtonObject m_btnActionMsgCenter = null;
  private ButtonObject m_aryMsgCenter[] = null;
  
  //到货查询条件对话框
  private RcQueDlg m_dlgArrQueryCondition = null;
  /*快速收货对话框*/
  private QueryOrdDlg m_dlgQuickArr = null;
  //缓存单据行ID对应的存货管理ID
  private Hashtable m_hBillIDsForCmangids = new Hashtable();
  /*缓存时间戳用于处理并发*/
  private HashMap m_hTS = null;
  ////缓存是否辅计量管理标志
  //Hashtable m_hIsAssMana = new Hashtable();
  //boolean isFlagsCache = false;
  ////缓存换算率、是否固定换算率
  //Hashtable m_hConvertIsfixed = new Hashtable();
  //缓存保质期天数
  private Hashtable m_hValiddays = new Hashtable();
  //到货当前行
  private int m_iArrCurrRow = 0;
  /*记录:转单前用户显示数据缓存位置*/
  private int m_OldCardVOPos = 0;
  /*记录:转单据前用户缓存数据长度*/
  private int m_OldVOsLen = 0;
  /*批号参照*/
  private UIRefPane m_PnlLotRef = null;
  //批量打印保存列表表头单据行号
  protected ArrayList listSelectBillsPos = null;
  /*缓存推式保存VOs*/
  private ArriveorderVO[] m_pushSaveVOs = null;
  //单据状态:初始化；到货浏览；到货修改；到货列表；转入列表；转入修改；消息中心
  private String m_strState = "初始化";
  /*支持转单后界面不清空显示定义类变量*/
  /*记录:转单后缓存中所有到货单VO[],初始值为转单前用户浏览数据,用户保存成功一张单据,则本数组增加一张单据*/
  private ArriveorderVO[] m_VOsAll = null;
  /**关键字对应的计算公式类的常量 ( 参见 RelationsCal )*/
  private int[] m_iDescriptions = new int[] {
      RelationsCal.IS_FIXED_CONVERT, RelationsCal.CONVERT_RATE, RelationsCal.NUM_ASSIST,
      RelationsCal.NUM, RelationsCal.NET_PRICE_ORIGINAL, RelationsCal.MONEY_ORIGINAL };
  //
  private ScmPrintTool printList = null;
  private ScmPrintTool printCard = null;
  
  //业务类型(过滤订单、过滤到货单时均用到)
  private UIRefPane m_refBusi = null;
  /**公式计算用到数组*/
  /**调用计算公式关键字列表(驱动计算时也要包括驱动列关键字)*/
  private String[] m_saKey = new String[] { "Y", "convertrate", "nassistnum", "narrvnum", "nprice", "nmoney" };
  //记录被拆分删除行
  private Vector v_DeletedItems = new Vector();
  //记录未被拆分的删除行
  private Vector vDelNoSplitted = new Vector();
  //当前登录操作员有权限的公司[]
  private String[] saPkCorp = null;

  //是否保留制单人
  private boolean m_bSaveMaker = true;
  //消息中心单据ID
  private String m_strBillId = null;
  
  //快速收货过程中出现异常标志
  private boolean m_bQuickException = false;  
/**
 * 获取是否快速收货过程中出现异常
 */
  public boolean isQuickException(){
    return m_bQuickException;
  }
/**
 * 设置是否快速收货过程中出现异常
 */
  public void setQuickExceptionFlag(boolean newVal){
    m_bQuickException = newVal;
  }


/**
 * ArriveUI 构造子注解。
 */
public ArriveUI() {
  super();
  initialize();
}

/**
 * ArriveUI 构造子注解。
 */
public ArriveUI(
  String pk_corp,
  String billType,
  String businessType,
  String operator,
  String billID) {

  super();
  //setCauditid(billID);
  initialize();
  ArriveorderVO vo = null;
  try {
    vo = ArriveorderHelper.findByPrimaryKey(billID);
    if (vo != null) {
      setCacheVOs(new ArriveorderVO[] { vo });
      setDispIndex(0);
      loadDataToCard();
    }
  } catch (Exception e) {
    SCMEnv.out(e);
  }
}

//For V56
public ArriveUI(FramePanel fp) {
  super();
  setFrame(fp);
  initialize();
}

/**
 * 处理点击自由项参照按钮事件
 * 有无自由项管理由bodyRowChange()保证
 * 如果没有自由项管理则在bodyRowChange()方法中隐藏掉自由项按钮
 * 创建日期：(2001-10-20 11:25:46)
 * @param e java.awt.event.ActionEvent
 */
public void actionPerformed(java.awt.event.ActionEvent e) {

  //if (getArrBillCardPanel().getBodyItem("vfree0") != null) {
    //if (e.getSource() == ((FreeItemRefPane) getArrBillCardPanel().getBodyItem("vfree0").getComponent()).getUIButton()) {
      //PuTool.actionPerformedFree(
        //getArrBillCardPanel(),
        //e,
        //new String[] { "cmangid", "cinventorycode", "cinventoryname", "cinventoryspec", "cinventorytype" },
        //new String[] { "vfree0", "vfree1", "vfree2", "vfree3", "vfree4", "vfree5" });
    //}
  //}
}
/**
 * 编辑后事件--表头库存组织
 * @param e
 */
private void afterEditWhenHeadStorOrg(BillEditEvent e){
  int iSize = getBillCardPanel().getRowCount();
  if(iSize <= 0){
    return;
  }
  for(int i=0 ;i < iSize ; i++){
    getBillCardPanel().setBodyValueAt(null,i,"cwarehouseid");
    getBillCardPanel().setBodyValueAt(null,i,"cwarehousename");
    getBillCardPanel().getBillModel().setRowState(i, BillModel.MODIFICATION);
  }
}
/**
 * @功能：编辑后事件 --> 某属性改变后触发的逻辑
 */
public void afterEdit(BillEditEvent e) {
  if (getBillCardPanel().getBillTable().getEditingRow() >= 0) {
    getBillCardPanel().getBillTable().editingStopped(new ChangeEvent(getBillCardPanel().getBillTable()));
  }
  BillModel bm = getBillCardPanel().getBillModel();
  if(e.getKey().equals("cstoreorganization")){
    afterEditWhenHeadStorOrg(e);
  }
  //单据行号
  else if (e.getKey().equals("crowno")) {
    BillRowNo.afterEditWhenRowNo(getBillCardPanel(), e, nc.vo.scm.pu.BillTypeConst.PO_ARRIVE);
  } else if (
    //数量
    (e.getKey().equals("convertrate")
      || e.getKey().equals("nassistnum")
      || e.getKey().equals("narrvnum")
      || e.getKey().equals("nprice")
      || e.getKey().equals("nmoney")
      || e.getKey().equals("nelignum")
      || e.getKey().equals("nnotelignum"))) {
    //公式计算
    afterEditWhenNum(e);
    //数量或辅数量为相反符号时的处理
    if (e.getKey().equals("narrvnum") || e.getKey().equals("nassistnum"))
      afterSignChged(e);
    //处理赠品
    afterEditWhenBodyLargessNums(e.getRow());
  } else if (e.getKey().equals("npresentnum")|| e.getKey().equals("npresentassistnum") || e.getKey().equals("nwastnum") || e.getKey().equals("nwastassistnum")) {
	  //赠品数量、赠品辅数量、途耗数量、途耗辅计量数量
	  afterEditWhenPresentnum(e);
  } else if (e.getKey().equals("cassistunitname")) {
    //辅计量
    afterEditWhenAssistUnit(e);
  } else if (e.getKey().equals("cinventorycode")) {
    //存货
    afterEditWhenInv(e);
  } else if (e.getKey().equals("cemployeeid")) {
    //采购员
    afterEditWhenHeadEmployee(e);
  } else if (e.getKey().equals("vproducenum")) {
    //批号
    afterEditWhenProdNum(e);
  } else if (e.getKey().equals("dproducedate")) {
    //生产日期
    afterEditWhenProdDate(e);
  } else if (e.getKey().equals("ivalidday")) {
    //保质期天数
    afterEditWhenValidDays(e);
  } else if (e.getKey().equals("vmemo") && e.getPos() == 1) {
    if (getBillCardPanel().getBodyItem("vmemo") != null) {
      //表体备注
      UIRefPane refBodyVmemo = (UIRefPane) getBillCardPanel().getBodyItem("vmemo").getComponent();
      bm.setValueAt(refBodyVmemo.getUITextField().getText(), e.getRow(), "vmemo");
    }
  } else if (e.getKey().equals("vbackreasonb") && e.getPos() == 1) {
    if (getBillCardPanel().getBodyItem("vbackreasonb") != null) {
      //表体退货理由
      UIRefPane refBodyReason = (UIRefPane) getBillCardPanel().getBodyItem("vbackreasonb").getComponent();
      bm.setValueAt(refBodyReason.getUITextField().getText(), e.getRow(), "vbackreasonb");
    }
  } else if (e.getKey().equals("vfree0")) {
    //自由项
    afterEditFree(e);
  } else if (e.getKey().equals("cwarehousename")) {
    //仓库
    afterEditWhenWareHouse(e);
  } else if (e.getKey().equals("cproject")) {
    //项目
    afterEditWhenProject(e);
  //来源单据行是赠品行，赠品标志不能更改
  } else if(e.getKey().equals("blargess")){
    afterEditWhenBodyLargessNums(e.getRow());
  }
  //自定义项PK处理
  if(e.getPos() == 1)
    setBodyDefPK(e);
  else
    setHeadDefPK(e);
}
private void afterEditWhenPresentnum(BillEditEvent e) {
	UFDouble convert = new UFDouble(0);
	//存货ID
	String sBaseID =(String) getBillCardPanel().getBillModel().getValueAt(e.getRow(), "cbaseid");
	//辅计量主键
	String sCassId =(String) getBillCardPanel().getBillModel().getValueAt(e.getRow(),"cassistunit");
	if (sCassId == null || sCassId.trim().equals("")) {
		UIRefPane refAss =(UIRefPane) getBillCardPanel().getBodyItem("cassistunitname").getComponent();
		sCassId = refAss.getRefPK();
		String sCassName = refAss.getRefName();
		getBillCardPanel().getBillModel().setValueAt(sCassId,e.getRow(),"cassistunit");
		getBillCardPanel().getBillModel().setValueAt(sCassName,e.getRow(),"cassistunitname");
	}
	//获取换算率
	convert = nc.ui.pu.pub.PuTool.getInvConvRateValue(sBaseID, sCassId);
	getBillCardPanel().getBillModel().setValueAt(convert,e.getRow(),"convertrate");

	UFDouble aa = new UFDouble(0);
	UFDouble bb = new UFDouble(0);

	if (e.getKey().equals("npresentnum")){
		aa = getBillCardPanel().getBodyValueAt(e.getRow(),"npresentnum")==null?new UFDouble(0):(UFDouble)getBillCardPanel().getBodyValueAt(e.getRow(),"npresentnum");//原币税额
		getBillCardPanel().getBillModel().setValueAt(aa.div(convert),e.getRow(),"npresentassistnum");
	}else if(e.getKey().equals("npresentassistnum")){
		bb = getBillCardPanel().getBodyValueAt(e.getRow(),"npresentassistnum")==null?new UFDouble(0):(UFDouble)getBillCardPanel().getBodyValueAt(e.getRow(),"npresentassistnum");//原币无税金额
		getBillCardPanel().getBillModel().setValueAt(bb.multiply(convert),e.getRow(),"npresentnum");
	}else if(e.getKey().equals("nwastnum")){
		bb = getBillCardPanel().getBodyValueAt(e.getRow(),"nwastnum")==null?new UFDouble(0):(UFDouble)getBillCardPanel().getBodyValueAt(e.getRow(),"nwastnum");//原币无税金额
		getBillCardPanel().getBillModel().setValueAt(bb.div(convert),e.getRow(),"nwastassistnum");
	}else if(e.getKey().equals("nwastassistnum")){
		bb = getBillCardPanel().getBodyValueAt(e.getRow(),"nwastassistnum")==null?new UFDouble(0):(UFDouble)getBillCardPanel().getBodyValueAt(e.getRow(),"nwastassistnum");//原币无税金额
		getBillCardPanel().getBillModel().setValueAt(bb.multiply(convert),e.getRow(),"nwastnum");
	}

}

/**
 * 数量相关字段及赠品标志字段编辑后事件处理
 * @param e
 * @since v50
 * @author czp 
 * @date 2006-10-09
 */
private void afterEditWhenBodyLargessNums(int iRow){

  UFBoolean bLargessUpRow = (UFBoolean) PuPubVO.getUFBoolean_NullAs(getBillCardPanel().getBillModel().getValueAt(iRow,"blargessuprow"), UFBoolean.FALSE);
  if(bLargessUpRow.booleanValue()){
    getBillCardPanel().getBillModel().setValueAt(new UFBoolean(true),iRow,"blargess");
  }
  UFBoolean bLargess = (UFBoolean) PuPubVO.getUFBoolean_NullAs(getBillCardPanel().getBillModel().getValueAt(iRow,"blargess"), UFBoolean.FALSE);
  if(bLargess.booleanValue()){
    Object oTemp = getBillCardPanel().getBillModel().getValueAt(iRow,"narrvnum");
    getBillCardPanel().getBillModel().setValueAt(oTemp,iRow,"npresentnum");
    oTemp = getBillCardPanel().getBillModel().getValueAt(iRow,"nassistnum");
    getBillCardPanel().getBillModel().setValueAt(oTemp,iRow,"npresentassistnum");     
  }else{
    getBillCardPanel().getBillModel().setValueAt(null,iRow,"npresentnum");
    getBillCardPanel().getBillModel().setValueAt(null,iRow,"npresentassistnum");            
  } 
}
/**
 * 自由项编辑事件
 * 创建日期：(2001-11-28 12:13:08)
 * @param e nc.ui.pub.bill.BillEditEvent
 */
private void afterEditFree(BillEditEvent e) {
  if (e.getPos() != 1 || !e.getKey().equals("vfree0"))
    return;
  if (getBillCardPanel().getBodyItem("vfree0") != null) {
    FreeVO freeVO = ((FreeItemRefPane) getBillCardPanel().getBodyItem("vfree0").getComponent()).getFreeVO();
    if (freeVO == null) {
      for (int i = 0; i < 5; i++) {
        String str = "vfree" + new Integer(i + 1).toString();
        getBillCardPanel().setBodyValueAt(null, e.getRow(), str);
      }
    } else {
      for (int i = 0; i < 5; i++) {
        String strName = "vfreename" + new Integer(i + 1).toString();
        if (freeVO.getAttributeValue(strName) != null) {
          String str = "vfree" + new Integer(i + 1).toString();
          Object ob = freeVO.getAttributeValue(str);
          getBillCardPanel().setBodyValueAt(ob, e.getRow(), str);
        }
      }
    }
  }
}

/**
 * 辅计量编辑事件
 * 处理：
    # 辅计量主键编辑时触发处理 ==>> 换算率       |
    # 选取的计量ID是主计量ID：变换率置为1，固定换算率    |
    # 由“换算率”驱动公式计算               > 步完成均处理合格数量是否可编辑
    # 同步更新单据模板中的换算率和是否固定换算率属性   |
    # 更新换算率属性列可编辑性
 * @param e nc.ui.pub.bill.BillEditEvent
 */
private void afterEditWhenAssistUnit(BillEditEvent e) {
  //是否固定变化率改变时要同步更改：strKeys[0]的值
  UFBoolean isfixed = new UFBoolean(true);
  UFDouble convert = new UFDouble(0);
  //存货ID
  String sBaseID =
    (String) getBillCardPanel().getBillModel().getValueAt(e.getRow(), "cbaseid");
  //辅计量主键
  String sCassId =
    (String) getBillCardPanel().getBillModel().getValueAt(
      e.getRow(),
      "cassistunit");
  if (sCassId == null || sCassId.trim().equals("")) {
    UIRefPane refAss =
      (UIRefPane) getBillCardPanel().getBodyItem("cassistunitname").getComponent();
    sCassId = refAss.getRefPK();
    String sCassName = refAss.getRefName();
    getBillCardPanel().getBillModel().setValueAt(
      sCassId,
      e.getRow(),
      "cassistunit");
    getBillCardPanel().getBillModel().setValueAt(
      sCassName,
      e.getRow(),
      "cassistunitname");
  }
  //获取换算率
  convert = nc.ui.pu.pub.PuTool.getInvConvRateValue(sBaseID, sCassId);
  //是否固定换算率
  isfixed = new UFBoolean(PuTool.isFixedConvertRate(sBaseID, sCassId));
  //辅计量主键编辑
  if (e.getKey().equals("cassistunitname")) { //设置辅计量参照
    setRefPaneAssistunit(e.getRow());
    //设置辅信息
    setAssisUnitEditState2(e);
    convert = nc.ui.pu.pub.PuTool.getInvConvRateValue(sBaseID, sCassId);
    getBillCardPanel().getBillModel().setValueAt(
      convert,
      e.getRow(),
      "convertrate");
    isfixed = new UFBoolean(PuTool.isFixedConvertRate(sBaseID, sCassId));
    if (isfixed.booleanValue())
      m_saKey[0] = "Y";
    else
      m_saKey[0] = "N";
    //用换算率驱动计算：到货数量，辅数量，合格数量，不合格数量，单价，金额
    e.setKey("convertrate");
    RelationsCal.calculate(getBillCardPanel(), e, getBillCardPanel().getBillModel(),
      "convertrate",
      m_iDescriptions,
      m_saKey, ArriveorderItemVO.class.getName());
  }
}
/**
 * 表头编辑后事件-采购员
 * @param e
 */
private void afterEditWhenHeadEmployee(BillEditEvent e){

  Logger.info("进入afterEditWhenHeadEmployee()");/*-=notranslate=-*/
  
    String sPsnId = ((UIRefPane) getBillCardPanel().getHeadItem("cemployeeid").getComponent()).getRefPK();
    
  Logger.info("sPsnId = ((UIRefPane) getBillCardPanel().getHeadItem(“cemployeeid”).getComponent()).getRefPK()="+sPsnId);/*-=notranslate=-*/
    
  if (PuPubVO.getString_TrimZeroLenAsNull(sPsnId) == null) {
    
    Logger.info("PuPubVO.getString_TrimZeroLenAsNull(sPsnId) == null ：true");/*-=notranslate=-*/
        
    return;
    }
    //由业务员带出默认部门
    UIRefPane ref = (UIRefPane) (getBillCardPanel().getHeadItem("cemployeeid").getComponent());
  
    Logger.info("getBillCardPanel().getHeadItem(“cemployeeid”).getComponent()："+ref.toString());/*-=notranslate=-*/
    
    //业务员所属部门
    Object sDeptId = ref.getRefModel().getValue("bd_psndoc.pk_deptdoc");
  
    Logger.info("ref.getRefModel().getValue(“bd_psndoc.pk_deptdoc”)"+sDeptId);/*-=notranslate=-*/
    
    getBillCardPanel().getHeadItem("cdeptid").setValue(sDeptId);
  
    Logger.info("从方法afterEditWhenHeadEmployee()正常返回");/*-=notranslate=-*/
}

/**
 * 存货编辑事件
 * 创建日期：(2001-11-28 12:13:08)
 * @param e nc.ui.pub.bill.BillEditEvent
 */
private void afterEditWhenInv(BillEditEvent e) {
  //改变存货时,清空自由项,辅数量,数量,单价,金额等到相关信息
  getBillCardPanel().setBodyValueAt(null, e.getRow(), "narrvnum");
  getBillCardPanel().setBodyValueAt(null, e.getRow(), "nassistnum");
  getBillCardPanel().setBodyValueAt(null, e.getRow(), "nprice");
  getBillCardPanel().setBodyValueAt(null, e.getRow(), "nmoney");
  getBillCardPanel().setBodyValueAt(null, e.getRow(), "nelignum");
  getBillCardPanel().setBodyValueAt(null, e.getRow(), "nnotelignum");
  getBillCardPanel().setBodyValueAt(null, e.getRow(), "vfree0");
  getBillCardPanel().setBodyValueAt(null, e.getRow(), "vfree1");
  getBillCardPanel().setBodyValueAt(null, e.getRow(), "vfree2");
  getBillCardPanel().setBodyValueAt(null, e.getRow(), "vfree3");
  getBillCardPanel().setBodyValueAt(null, e.getRow(), "vfree4");
  getBillCardPanel().setBodyValueAt(null, e.getRow(), "vfree5");
  getBillCardPanel().setBodyValueAt(null, e.getRow(), "dproducedate");
  getBillCardPanel().setBodyValueAt(null, e.getRow(), "ivalidday");
  getBillCardPanel().setBodyValueAt(null, e.getRow(), "dvaliddate");
  getBillCardPanel().setBodyValueAt(null, e.getRow(), "npresentnum");
  getBillCardPanel().setBodyValueAt(null, e.getRow(), "nwastnum");
  getBillCardPanel().setBodyValueAt(null, e.getRow(), "vproducenum");
  String[] aryAssistunit =
    new String[] {
      "cassistunit->getColValue(bd_invbasdoc,pk_measdoc2,pk_invbasdoc,cbaseid)",
      "cassistunitname->getColValue(bd_measdoc,measname,pk_measdoc,cassistunit)" };
  getBillCardPanel().getBillModel().execFormulas(e.getRow(), aryAssistunit);
  //辅计量设置
  setAssisUnitEditState2(e);
  
  //存货批次号管理处理
  String strCmangid = (String) getBillCardPanel().getBillModel().getValueAt(e.getRow(), "cmangid");
  if (PuTool.isBatchManaged(strCmangid))
    getBillCardPanel().setCellEditable(e.getRow(), "vproducenum", getBillCardPanel().getBodyItem("vproducenum").isEdit());
  else
    getBillCardPanel().setCellEditable(e.getRow(), "vproducenum", false);
  
  String strCbaseid = (String) getBillListPanel().getBodyBillModel().getValueAt(e.getRow(), "cbaseid");
  String strCassid = (String) getBillListPanel().getBodyBillModel().getValueAt(e.getRow(), "cassistunit");

  // 是否固定换算率
  if(PuTool.isFixedConvertRate(strCbaseid, strCassid)){
    getBillListPanel().getBodyBillModel().setValueAt(new UFBoolean(true), e.getRow(), "isfixedrate");
  }


}

/**
 * 数量编辑事件
 * 创建日期：(2001-11-28 12:13:08)
 * @param e nc.ui.pub.bill.BillEditEvent
 */
private void afterEditWhenNum(BillEditEvent e) {
  if(e.getKey().equals("narrvnum")){
    BillItem item = getBillCardPanel().getHeadItem("bisback");
    UFBoolean bBack = new UFBoolean(false);
    if(item != null) bBack = new UFBoolean(item.getValue());
    if(bBack.booleanValue()){
      Object oTemp = getBillCardPanel().getBodyValueAt(e.getRow(),"narrvnum");
      if(oTemp != null){
        UFDouble d = new UFDouble(oTemp.toString());
        if(d.doubleValue() > 0){
          getBillCardPanel().setBodyValueAt(e.getOldValue(), e.getRow(), "narrvnum");
          MessageDialog.showErrorDlg(this,m_lanResTool.getStrByID("SCMCOMMON","UPPSCMCommon-000059")/*@res "错误"*/,m_lanResTool.getStrByID("40040301","UPP40040301-000275")/*@res"退货单数量必须为负!"*/);
          //2010-10-13 MeiChao (佛山-骏杰) ----begin  
          //修改当界面中实到数量被修改时.根据是否固定换算率,修改对应实到辅数量,(当前BUG为实到数量修改,对应实到辅数量不变化)
          //2010-10-13 MeiChao (佛山-骏杰) ----end  
          
          return;
        }
      }
    }
    //add by QuSida 2010-9-2 (佛山骏杰)  --- begin
    //function 当已到货数量修改后及时更新费用信息中的数量
    int temp = getBillCardPanel().getBillModel("table").getRowCount();
//  UFDouble taxmny = null;
    UFDouble mny = null; 
    arrnumber = new UFDouble(0.0);
    for (int i = 0; i < temp; i++) {
    	arrnumber = arrnumber.add(new UFDouble((getBillCardPanel().getBodyValueAt(i,"narrvnum")==null?0:getBillCardPanel().getBodyValueAt(i,"narrvnum")).toString()));    
	}
    temp = getBillCardPanel().getBillModel("jj_scm_informationcost").getRowCount();
    //查出累计到货数量
//    IUAPQueryBS query = NCLocator.getInstance().lookup(IUAPQueryBS.class);
//    String sql = "select sum(naccumarrvnum) from po_order_b where dr = 0 and corderid = '"+vos[0].getCbillid()+"'";
//    Object o = null;
//    try {
//		 o = query.executeQuery(sql, new ColumnProcessor());
//	} catch (BusinessException e1) {
//		// TODO Auto-generated catch block
//		e1.printStackTrace();
//		return;
//	}
	UFDouble arrmny = null;
	int length = 0;
//	if(vos!=null) length = vos.length;
    for (int i = length; i < temp; i++) {
    	Boolean ismny = (Boolean)getBillCardPanel().getBillModel("jj_scm_informationcost").getValueAt(i, "ismny");
    	
    	getBillCardPanel().getBillModel("jj_scm_informationcost").setValueAt(arrnumber, i, "nnumber");
    	

    	if(ismny == null || !ismny){
    	mny = new UFDouble(getBillCardPanel().getBillModel("jj_scm_informationcost").getValueAt(i, "noriginalcurprice").toString()).multiply(arrnumber);
    	//累计到货金额
    	arrmny  = new UFDouble(getBillCardPanel().getBillModel("jj_scm_informationcost").getValueAt(i, "noriginalcurprice").toString()).multiply(arrnumber.add(arrnum));
        
//   	taxmny = new UFDouble(getBillCardPanel().getBillModel("jj_scm_informationcost").getValueAt(i, "noriginalcurtaxprice").toString()).multiply(arrnumber);
    	
//    	getBillCardPanel().getBillModel("jj_scm_informationcost").setValueAt(taxmny, i, "noriginalcursummny");
    	getBillCardPanel().getBillModel("jj_scm_informationcost").setValueAt(mny, i, "noriginalcurmny");
    	
//   	getBillCardPanel().getBillModel("jj_scm_informationcost").setValueAt(taxmny, i, "ninvoriginalcursummny");
    	getBillCardPanel().getBillModel("jj_scm_informationcost").setValueAt(arrmny, i, "ninvoriginalcurmny");
    	}
    	else{
    	UFDouble price =  new UFDouble(getBillCardPanel().getBillModel("jj_scm_informationcost").getValueAt(i, "noriginalcurmny").toString()).div(arrnumber);
    		getBillCardPanel().getBillModel("jj_scm_informationcost").setValueAt(price, i, "noriginalcurprice");	
    
    	}
    	
    	//修改缓存VO
//    	if(vos!=null&&i<vos.length){
//    		
////    		vos[i].setAttributeValue("noriginalcursummny", taxmny);
//    		vos[i].setAttributeValue("noriginalcurmny", mny);
////   		vos[i].setAttributeValue("ninvoriginalcursummny", taxmny);
//    		vos[i].setAttributeValue("ninvoriginalcurmny", arrmny);
//    		vos[i].setAttributeValue("nnumber", arrnumber);
//    	}
    	
        
    }
    //add by QuSida 2010-9-2 (佛山骏杰)  --- end
    
  }
  
  UFBoolean isfixed = new UFBoolean(true);
  //存货ID
  String sBaseID =
    (String) getBillCardPanel().getBillModel().getValueAt(e.getRow(), "cbaseid");
  //辅计量主键
  String sCassId =
    (String) getBillCardPanel().getBillModel().getValueAt(
      e.getRow(),
      "cassistunit");
  if (sCassId == null || sCassId.trim().equals("")) {
    UIRefPane refAss =
      (UIRefPane) getBillCardPanel().getBodyItem("cassistunitname").getComponent();
    sCassId = refAss.getRefPK();
    String sCassName = refAss.getRefName();
    getBillCardPanel().getBillModel().setValueAt(
      sCassId,
      e.getRow(),
      "cassistunit");
    getBillCardPanel().getBillModel().setValueAt(
      sCassName,
      e.getRow(),
      "cassistunitname");
  }
  //获取换算率
  //convert = nc.ui.pu.pub.PuTool.getInvConvRateValue(sBaseID, sCassId);
  //是否固定换算率
  isfixed = new UFBoolean(PuTool.isFixedConvertRate(sBaseID, sCassId));
  
  //自动计算：到货数量，辅数量，换算率，合格数量，不合格数量，单价，金额
  if ((e.getKey().equals("convertrate")
    || e.getKey().equals("nassistnum")
    || e.getKey().equals("narrvnum")
    || e.getKey().equals("nprice")
    || e.getKey().equals("nmoney")
    || e.getKey().equals("nelignum")
    || e.getKey().equals("nnotelignum"))) {
    //检查数据合法性，不合法恢复原值返回
    String strErr = getErrMsg(e);
    if (strErr != null) {
      MessageDialog.showErrorDlg(this, m_lanResTool.getStrByID("40040301","UPP40040301-000085")/*@res "数据错误"*/, m_lanResTool.getStrByID("40040301","UPP40040301-000086")/*@res "输入数据错误：\n"*/ + strErr);
      getBillCardPanel().getBillModel().setValueAt(
        e.getOldValue(),
        e.getRow(),
        e.getKey());
      return;
    }
    if (isfixed.booleanValue())
      m_saKey[0] = "Y";
    else
      m_saKey[0] = "N";
    RelationsCal.calculate(getBillCardPanel(), e, getBillCardPanel().getBillModel(),m_iDescriptions, m_saKey, ArriveorderItemVO.class.getName());
    
    //只有数量和辅数量编辑后才置正负属性
    if (e.getKey().equals("nassistnum") || e.getKey().equals("narrvnum"))
      setEditAndDirect(e);
  }
}

/**
 * 生产日期编辑事件
 * 处理：生产日期 + 保质期天数 = 失效日期(不可编辑)
 *   注：生产日期或保质期天数一方为空则失效日期为空
 */
private void afterEditWhenProdDate(BillEditEvent e) {
  BillModel bm = getBillCardPanel().getBillModel();
  /**获取当前编辑表体行VO -- item ,注意：不能在初始化时由服务器端传递过来，因为可能是订单转入的单据*/
  ArriveorderItemVO item =
    (ArriveorderItemVO) bm.getBodyValueRowVO(
      e.getRow(),
      ArriveorderItemVO.class.getName());
  Object dproducedate = bm.getValueAt(e.getRow(), "dproducedate");
  if (dproducedate == null
    || dproducedate.toString().trim().equals("")
    || item.getIvalidday() == null
    || item.getIvalidday().toString().trim().equals("")) {
    item.setDvaliddate(null);
    bm.setValueAt(null, e.getRow(), "dvaliddate");
  } else {
    UFDate dvaliddate =
      item.getDproducedate().getDateAfter(item.getIvalidday().intValue());
    //失效日期(不可编辑)
    bm.setValueAt(dvaliddate, e.getRow(), "dvaliddate");
    item.setDvaliddate(dvaliddate);
  }
}

/**
 * 批号编辑事件
 */
private void afterEditWhenProdNum(BillEditEvent e) {
  try {
	  
	  if (m_PnlLotRef == null) {
		  return;
	  }
    int iRow = e.getRow();
	  BillModel bm = getBillCardPanel().getBillModel();
	  Object vproducenum = bm.getValueAt(iRow, "vproducenum");
	  if (vproducenum == null || vproducenum.toString().trim().equals("")) {
		  bm.setValueAt(null, iRow, "dproducedate");
		  bm.setValueAt(null, iRow, "dvaliddate");
		  return;
	  }
//	  UFDate dateValid = m_PnlLotRef.getRefInvalideDate();
	  UFDate dateValid = new UFDate(System.currentTimeMillis());

	  //Object dproducedate = bm.getValueAt(e.getRow(), "dproducedate");
	  Object ivalidday = bm.getValueAt(iRow, "ivalidday");
	  int iDays = 0;
	  if (ivalidday == null || ivalidday.toString().trim().equals("")) {
		  iDays = 0;
	  } else {
		  iDays = Integer.parseInt(ivalidday.toString().trim());
	  }
	  //生产日期＝失效日期－保质期天数
	  UFDate dateProduce = dateValid.getDateBefore(iDays);
	  bm.setValueAt(dateProduce, iRow, "dproducedate");
	  bm.setValueAt(dateValid, iRow, "dvaliddate");

    //调用库存接口，获取批次档案信息
    RcTool.setBatchCodeInfo(getBillCardPanel(), e
        .getRow(), (String) getBillCardPanel().getBodyValueAt(iRow,
            "cmangid"), (String) getBillCardPanel().getBodyValueAt(iRow,
            "cbaseid"), (String) getBillCardPanel().getBodyValueAt(iRow,
            "vproducenum"), getCorpPrimaryKey());
    
  } catch (Exception ex) {
    SCMEnv.out("批号编辑后计算生产日期出现异常：详细信息如下：");
    reportException(ex);
    showHintMessage("批号编辑后计算生产日期出现异常：详细信息如下：" + ex);
  }


}

/**
 * 项目编辑事件
 * @param e nc.ui.pub.bill.BillEditEvent
 */
private void afterEditWhenProject(BillEditEvent e) {
  int row = e.getRow();
  //项目
  Object o = getBillCardPanel().getBillModel().getValueAt(row, "cprojectid");
  if (o != null && o.toString().trim().length() > 0) {
    String cprojectid = o.toString();
    PuProjectPhaseRefModel refjobphase = new PuProjectPhaseRefModel(getCorpPrimaryKey(), cprojectid);
    ((UIRefPane) (getBillCardPanel().getBodyItem("cprojectphase").getComponent())).setIsCustomDefined(true);
    ((UIRefPane) (getBillCardPanel().getBodyItem("cprojectphase").getComponent())).setRefModel(refjobphase);
    //设置项目阶段不可编辑
    getBillCardPanel().setCellEditable(row, "cprojectphase", getBillCardPanel().getBodyItem("cprojectphase").isEdit());
  } else {
    getBillCardPanel().getBillModel().setValueAt(null, row, "cprojectphase");
    getBillCardPanel().getBillModel().setValueAt(null, row, "cprojectphasebaseid");
    getBillCardPanel().getBillModel().setValueAt(null, row, "cprojectphaseid");
    //设置项目阶段不可编辑
    getBillCardPanel().setCellEditable(row, "cprojectphase", false);
  }
}

/**
 * 保质期天数编辑事件
 * 处理：生产日期 + 保质期天数 = 失效日期(不可编辑)
 *   注：生产日期或保质期天数一方为空则失效日期为空
 */
private void afterEditWhenValidDays(BillEditEvent e) {
  BillModel bm = getBillCardPanel().getBillModel();
  /**获取当前编辑表体行VO -- item ,注意：不能在初始化时由服务器端传递过来，因为可能是订单转入的单据*/
  ArriveorderItemVO item =
    (ArriveorderItemVO) bm.getBodyValueRowVO(
      e.getRow(),
      ArriveorderItemVO.class.getName());
  //保质期天数
  Object ivalidday = bm.getValueAt(e.getRow(), "ivalidday");
  if (item.getDproducedate() == null
    || item.getDproducedate().toString().trim().equals("")
    || ivalidday == null
    || ivalidday.toString().trim().equals("")) {
    item.setDvaliddate(null);
    bm.setValueAt(null, e.getRow(), "dvaliddate");
  } else {
    UFDate dvaliddate =
      item.getDproducedate().getDateAfter(item.getIvalidday().intValue());
    //失效日期(不可编辑)
    bm.setValueAt(dvaliddate, e.getRow(), "dvaliddate");
    item.setDvaliddate(dvaliddate);
  }
}

/**
 * 仓库编辑事件
 * 创建日期：(2001-11-28 12:13:08)
 * @param e nc.ui.pub.bill.BillEditEvent
 */
private void afterEditWhenWareHouse(BillEditEvent e) {
  //BillModel bm = getArrBillCardPanel().getBillModel();
  ////更新仓库是否货位管理缓存
  //String cwarehouseid = (String) bm.getValueAt(e.getRow(), "cwarehouseid");
  //UFBoolean isAllot = null;
  //if (cwarehouseid != null && !cwarehouseid.trim().equals("")) { //改变货位参照模型
    //UIRefPane refCargDoc =
      //(UIRefPane) getArrBillCardPanel().getBodyItem("cstorename").getComponent();
    //refCargDoc.getRefModel().addWherePart(
      //"and bd_cargdoc.pk_stordoc = '"
        //+ cwarehouseid
        //+ "' and  UPPER(bd_cargdoc.sealflag) <> 'Y' ");
    //if (m_hIsAllot == null)
      //m_hIsAllot = new Hashtable();
    //if (!m_hIsAllot.containsKey(cwarehouseid)) {
      //try {
        //ArrayList ary = ArriveorderBO_Client.getStorFlags(cwarehouseid);
        //m_hIsAllot.put(cwarehouseid, ary);
        //isAllot = (UFBoolean) ary.get(0);
        ////if (isAllot.booleanValue()) {
          ////m_btnAllotCarg.setEnabled(true);
          ////updateButton(m_btnAllotCarg);
        ////} else {
          ////m_btnAllotCarg.setEnabled(false);
          ////updateButton(m_btnAllotCarg);
        ////}
      //} catch (Exception exx) {
        //reportException(exx);
        //SCMEnv.out("afterEdit()");
      //}
    //} else {
      //isAllot = (UFBoolean) ((ArrayList) m_hIsAllot.get(cwarehouseid)).get(0);
      //if (isAllot.booleanValue()) {
        //m_btnAllotCarg.setEnabled(true);
        //updateButton(m_btnAllotCarg);
      //} else {
        //m_btnAllotCarg.setEnabled(false);
        //updateButton(m_btnAllotCarg);
      //}
    //}
  //} else {
    //m_btnAllotCarg.setEnabled(false);
    //updateButton(m_btnAllotCarg);
  //}
}

/**
 * 数量符号相反编辑事件
 * 创建日期：(2001-11-28 12:13:08)
 * @param e nc.ui.pub.bill.BillEditEvent
 */
private void afterSignChged(BillEditEvent e) {
  UFDouble ufdOld = new UFDouble(e.getOldValue().toString().trim());
  UFDouble ufdNew = new UFDouble(e.getValue().toString().trim());
  if (ufdOld.multiply(ufdNew).doubleValue() < 0) {
    getBillCardPanel().setBodyValueAt(null, e.getRow(), "npresentnum");
    getBillCardPanel().setBodyValueAt(null, e.getRow(), "nwastnum");
  }
  return;
}

/**
 * @功能：插入当前保存的转入到货单到记录所有单据的缓存中
 */
private void appArriveorderVOSaved(ArriveorderVO voSaved) {
  if (voSaved == null)
    return;
  /*以下处理类变量 m_VOsAll*/
  if (m_VOsAll == null) {
    m_VOsAll = new ArriveorderVO[] { voSaved };
    return;
  }
  ArriveorderVO[] saVOTmp = new ArriveorderVO[m_VOsAll.length + 1];
  for (int i = 0; i < m_VOsAll.length; i++) {
    saVOTmp[i] = m_VOsAll[i];
  }
  saVOTmp[saVOTmp.length - 1] = voSaved;
  m_VOsAll = saVOTmp;
}

/**
 * 编辑前处理
 */
public boolean beforeEdit(nc.ui.pub.bill.BillEditEvent e) {
  if (e.getKey().equals("vfree0")) {
    //自由项处理
    boolean bCanEdit = PuTool.beforeEditInvBillBodyFree(
      getBillCardPanel(),
      e,
      new String[] {
        "cmangid",
        "cinventorycode",
        "cinventoryname",
        "cinventoryspec",
        "cinventorytype" },
      new String[] { "vfree0", "vfree1", "vfree2", "vfree3", "vfree4", "vfree5" });
    return  bCanEdit;
  }
    else if (e.getKey().equals("narrvnum") || e.getKey().equals("vfree0") || e.getKey().equals("vfree1") || e.getKey().equals("vfree2") || e.getKey().equals("vfree3") || e.getKey().equals("vfree4") || e.getKey().equals("vfree5")) {
      //报检过不能编辑
      UFDouble nArr = PuPubVO.getUFDouble_NullAsZero(getBillCardPanel().getBillModel().getValueAt(e.getRow(), "narrvnum"));
      UFDouble nAcc = PuPubVO.getUFDouble_NullAsZero(getBillCardPanel().getBillModel().getValueAt(e.getRow(), "naccumchecknum"));
      if (nArr == null || nArr.doubleValue() < 0)// “负到货”情况同“免检”，即不认为报过检
        return true;
      if (!(nAcc == null || nAcc.doubleValue() == 0))// 是否存在“非免检”存货报过检
        return false;
  } else if (e.getKey().equalsIgnoreCase("vproducenum")) {
    //批次号处理
    return beforeEditProdNum(e);
    //return true;
  } else if (e.getKey().equals("cprojectphase")) {
    Object oTmp =
      getBillCardPanel().getBillModel().getValueAt(e.getRow(), "cproject");
    if (oTmp == null || oTmp.toString().trim().equals(""))
      return false;
  }
    //仓库
    else if (e.getKey().equals("cwarehousename")) {
        ((UIRefPane) getBillCardPanel().getBodyItem("cwarehousename").getComponent()).setPk_corp(getCorpPrimaryKey());
        PuTool.restrictWarehouseRefByStoreOrg(getBillCardPanel(), 
            getCorpPrimaryKey(),
        getBillCardPanel().getHeadItem("cstoreorganization").getValue(),
        "cwarehousename");

        UIRefPane refStore = (UIRefPane) getBillCardPanel().getBodyItem("cwarehousename").getComponent();
        UIRefPane refpane = (UIRefPane)getBillCardPanel().getHeadItem("cbiztype").getComponent();
        String sBiztype = refpane.getRefPK();
        WarehouseRefModel model = (WarehouseRefModel)refStore.getRefModel();
        Object[] oRule = null;
        try{
        oRule =  (Object[])CacheTool.getCellValue("bd_busitype","pk_busitype","verifyrule" , sBiztype );
        }catch(Exception ee){
         SCMEnv.out(ee);
        }
        model.setM_pk_corp(getClientEnvironment().getCorporation().getPrimaryKey());
        model.setPk_calbody(getBillCardPanel().getHeadItem("cstoreorganization").getValue());
        //业务类型直运
        if(oRule!=null&&oRule.length>0&&"Z".equalsIgnoreCase(oRule[0].toString())){
//         refStore.setRefModel(new WarehouseRefModel(getClientEnvironment().getCorporation().getPrimaryKey(),getBillCardPanel().getHeadItem("cstoreorganization").getValue(),true));
        	model.setBDirect(true);
        }else{
//         refStore.setRefModel(new WarehouseRefModel(getClientEnvironment().getCorporation().getPrimaryKey(),getBillCardPanel().getHeadItem("cstoreorganization").getValue(),false));
        	model.setBDirect(false);
        }
      
    }
    //存货参照设置
    else if (e.getKey().equalsIgnoreCase("cinventorycode")){ 
    return beforeEditInv(e);
    }
  //项目阶段
    else if(e.getKey().equalsIgnoreCase("cprojectphase")){
      return beforeEditProjectPhase(e);
    }
  	//换算率
    else if(e.getKey().equalsIgnoreCase("convertrate")){

    	  //存货ID
    	  String sBaseID =
    	    (String) getBillCardPanel().getBillModel().getValueAt(e.getRow(), "cbaseid");
    	  //辅计量主键
    	  String sCassId =
    	    (String) getBillCardPanel().getBillModel().getValueAt(
    	      e.getRow(),
    	      "cassistunit");
    	  if (sCassId == null || sCassId.trim().equals("")) {
    	    UIRefPane refAss =
    	      (UIRefPane) getBillCardPanel().getBodyItem("cassistunitname").getComponent();
    	    sCassId = refAss.getRefPK();
    	    String sCassName = refAss.getRefName();
    	    getBillCardPanel().getBillModel().setValueAt(
    	      sCassId,
    	      e.getRow(),
    	      "cassistunit");
    	    getBillCardPanel().getBillModel().setValueAt(
    	      sCassName,
    	      e.getRow(),
    	      "cassistunitname");
    	  }
    	  //非固定换算率才可以编辑
    	  return !PuTool.isFixedConvertRate(sBaseID, sCassId) 
    	  	&& getBillCardPanel().getBodyItem("convertrate").isEdit();
    }
  return true;
}
private boolean beforeEditInv(BillEditEvent e){

  UFBoolean bLargessUpRow = PuPubVO.getUFBoolean_NullAs(getBillCardPanel().getBodyValueAt(e.getRow(),"blargessuprow"),UFBoolean.FALSE);
  UFBoolean bLargess = PuPubVO.getUFBoolean_NullAs(getBillCardPanel().getBodyValueAt(e.getRow(),"blargess"),UFBoolean.FALSE);
  BillModel bm = getBillCardPanel().getBillModel();
  String strBillLinKey = getStateStr().equals("转入修改") ? "corder_bid" : "carriveorder_bid";
  if (bm.getValueAt(e.getRow(), strBillLinKey) == null){
    SCMEnv.out("1-到货单无来源，但不能获取到货单或订单ID，不允许编辑到货单");
    return false;
  }
  if (m_hBillIDsForCmangids == null){
    SCMEnv.out("2-到货单无来源，但不能获取到货单或订单ID，不允许编辑到货单");
    return false;
  }
  String cmangid = (String) m_hBillIDsForCmangids.get(bm.getValueAt(e.getRow(), strBillLinKey));
  if (cmangid == null || cmangid.trim().equals("")){
    SCMEnv.out("3-到货单无来源，但不能获取到货单或订单ID，不允许编辑到货单");
    return false;
  }
  UIRefPane refCinventorycode = (UIRefPane) getBillCardPanel().getBodyItem("cinventorycode").getComponent();
//  refCinventorycode.setIsCustomDefined(true);
//  refCinventorycode.setRefType(IBusiType.GRID);
  InvRefModelForRepl refmodel = (InvRefModelForRepl)refCinventorycode.getRefModel();
  refmodel.setPk_invmandoc(cmangid);
  refmodel.setCmagidOrderRow(PuPubVO.getString_TrimZeroLenAsNull(getBillCardPanel().getBillModel().getValueAt(e.getRow(), "corder_bid")));
  if(!bLargessUpRow.booleanValue() && bLargess.booleanValue()){
    //订单行不是赠品且到货单行是赠品存货参照内容存货参照取所有（含订单存货＋替换件）
//    refmodel = new InvRefModelForRepl(cmangid,ClientEnvironment.getInstance().getCorporation().getPrimaryKey(),true);
	  refmodel.setBAllInv(true);
  }
  else{
    //存货参照内容为订单存货＋替换件
//    refmodel = new InvRefModelForRepl(cmangid,ClientEnvironment.getInstance().getCorporation().getPrimaryKey(),false);
	  refmodel.setBAllInv(false);
  }
  refmodel.setMyWherePart();
//  refCinventorycode.setRefModel(refmodel);    
  return true;
}
/**
 * 功能：编辑批次号前对批次号参照处理
 * 参数：
 * 返回：
 * 例外：
 * 日期：(2002-9-16 13:01:38)
 * 修改日期，修改人，修改原因，注释标志：
 * @return boolean
 * @param e nc.ui.pub.bill.BillEditEvent
 */
private boolean beforeEditProdNum(BillEditEvent e) {

    int iRow = e.getRow();
    ParaVOForBatch vo = new ParaVOForBatch();
    //传入FieldName
    vo.setMangIdField("cmangid");
    vo.setInvCodeField("cinventorycode");
    vo.setInvNameField("cinventoryname");
    vo.setSpecificationField("cinventoryspec");
    vo.setInvTypeField("cinventorytype");
    vo.setMainMeasureNameField("cmainmeasname");
    vo.setAssistUnitIDField("cassistunit");
    vo.setIsAstMg(new UFBoolean(PuTool
            .isAssUnitManaged((String) getBillCardPanel().getBodyValueAt(iRow, "cbaseid"))));
    vo.setWarehouseIDField("cwarehouseid");
    vo.setFreePrefix("vfree");
    //设置卡片模板,公司等
    vo.setCardPanel(getBillCardPanel());
    vo.setPk_corp(getCorpPrimaryKey());
    vo.setEvent(e);
    vo.setStoreorganization(getBillCardPanel().getHeadItem("cstoreorganization").getValue());

    try {
        m_PnlLotRef = nc.ui.pu.pub.PuTool.beforeEditWhenBodyBatch(vo,true);
    } catch (Exception exp) {
        PuTool.outException(this, exp);
    }
  BillModel bm = getBillCardPanel().getBillModel();
  String cmangid = (String) bm.getValueAt(e.getRow(), "cmangid");
  if (m_PnlLotRef == null || !PuTool.isBatchManaged(cmangid)){
    return false;
  }
    return true;
}
/**
 * 到货修改行变换时特殊控制(行变换事件)
 */
private void bmrcSetForModify(BillEditEvent e) {
  BillModel bm = getBillCardPanel().getBillModel();
  Object obj = bm.getValueAt(e.getRow(), "naccumchecknum");
  Object objElg = bm.getValueAt(e.getRow(), "nelignum");
  Object objNotElg = bm.getValueAt(e.getRow(), "nnotelignum");
  /*不可编辑逻辑(满足其一)：
   *1)、  质检数量  != 0 并且 非修改行、非增加行、非免检行、合格数量非负、不合格数量非负
   *2)、  合格数量  != 0 并且 非修改行、非增加行、非免检行、合格数量非负、不合格数量非负
   *3)、不合格数量  != 0 并且 非修改行、非增加行、非免检行、合格数量非负、不合格数量非负
  */
  if ((obj != null
    && !obj.toString().trim().equals("")
    && !(new UFDouble(obj.toString().trim()).compareTo(new UFDouble(0)) == 0)
    || objElg != null
    && !objElg.toString().trim().equals("")
    && !(new UFDouble(objElg.toString().trim()).compareTo(new UFDouble(0)) == 0)
    || objNotElg != null
    && !objNotElg.toString().trim().equals("")
    && !(new UFDouble(objNotElg.toString().trim()).compareTo(new UFDouble(0)) == 0))
    && bm.getRowState(e.getRow()) != BillModel.MODIFICATION
    && bm.getRowState(e.getRow()) != BillModel.ADD
    && !isCheckFree(e)
    && !((objElg != null && new UFDouble(objElg.toString().trim()).compareTo(new UFDouble(0)) < 0) || (objNotElg != null && new UFDouble(objNotElg.toString().trim()).compareTo(new UFDouble(0)) < 0))) {
    //按钮逻辑
    //m_btnDelLine.setEnabled(false);
    setBtnLines(false);
    //updateButton(m_btnDelLine);
    //m_btnAllotCarg.setEnabled(false);
    //updateButton(m_btnAllotCarg);
    //所有可编辑的项置为不可编辑
    getBillCardPanel().setCellEditable(e.getRow(), "cinventorycode", false);
    getBillCardPanel().setCellEditable(e.getRow(), "convertrate", false);
    getBillCardPanel().setCellEditable(e.getRow(), "cassistunitname", false);
    getBillCardPanel().setCellEditable(e.getRow(), "nassistnum", false);
    getBillCardPanel().setCellEditable(e.getRow(), "narrvnum", false);
    getBillCardPanel().setCellEditable(e.getRow(), "nprice", false);
    getBillCardPanel().setCellEditable(e.getRow(), "nmoney", false);
    getBillCardPanel().setCellEditable(e.getRow(), "nelignum", false);
    getBillCardPanel().setCellEditable(e.getRow(), "npresentnum", false);
    getBillCardPanel().setCellEditable(e.getRow(), "nwastnum", false);
    getBillCardPanel().setCellEditable(e.getRow(), "cwarehousename", false);
    getBillCardPanel().setCellEditable(e.getRow(), "cstorename", false);
    getBillCardPanel().setCellEditable(e.getRow(), "vproducenum", false);
    getBillCardPanel().setCellEditable(e.getRow(), "dproducedate", false);
    getBillCardPanel().setCellEditable(e.getRow(), "ivalidday", false);
    getBillCardPanel().setCellEditable(e.getRow(), "vmemo", false);
    if (getBillCardPanel().getBodyItem("vbackreasonb") != null)
      getBillCardPanel().setCellEditable(e.getRow(), "vbackreasonb", false);
    getBillCardPanel().setCellEditable(e.getRow(), "vfree0", false);
    getBillCardPanel().setCellEditable(e.getRow(), "vdef1", false);
    getBillCardPanel().setCellEditable(e.getRow(), "vdef2", false);
    getBillCardPanel().setCellEditable(e.getRow(), "vdef3", false);
    getBillCardPanel().setCellEditable(e.getRow(), "vdef4", false);
    getBillCardPanel().setCellEditable(e.getRow(), "vdef5", false);
    getBillCardPanel().setCellEditable(e.getRow(), "vdef6", false);
    getBillCardPanel().setCellEditable(e.getRow(), "cproject", false);
    getBillCardPanel().setCellEditable(e.getRow(), "cprojectphase", false);
  } else {
    //没有报过检按钮逻辑
    //m_btnDelLine.setEnabled(true);
    setBtnLines(true);
    //updateButton(m_btnDelLine);
  }
}
/**
 * 功能：设置项目阶段参照
 */
private boolean beforeEditProjectPhase(BillEditEvent e) {
  int row = e.getRow();
  if (row < 0){
    return false;
  }
  getBillCardPanel().stopEditing();
  //项目
  Object o = getBillCardPanel().getBillModel().getValueAt(row, "cprojectid");
  Object pk_corp =
    getBillCardPanel().getBillModel().getValueAt(row, "pk_corp");
  String cprojectid = null;
  //项目阶段参照
  if ((o != null) && (!o.toString().trim().equals(""))) {
    cprojectid = o.toString().trim();
    PuProjectPhaseRefModel refjobphase =
      new PuProjectPhaseRefModel(pk_corp.toString(), cprojectid);
    (
      (UIRefPane)
        (
          getBillCardPanel()
            .getBodyItem("cprojectphase")
            .getComponent()))
            .setIsCustomDefined(
      true);
    (
      (UIRefPane)
        (
          getBillCardPanel()
            .getBodyItem("cprojectphase")
            .getComponent()))
            .setRefModel(
      refjobphase);
    return true;
  } else {
    return false;
  }
}

/**
 * 仓库是否进行货位管理(行变换事件)
 */
private void bmrcSetForWareAlot(BillEditEvent e) {
  /*
  BillModel bm = getArrBillCardPanel().getBillModel();
  String cwarehouseid = (String) bm.getValueAt(e.getRow(), "cwarehouseid");
  if (cwarehouseid == null || cwarehouseid.trim().equals("")) {
    m_btnAllotCarg.setEnabled(false);
    updateButton(m_btnAllotCarg);
  } else {
    ArrayList ary1 = (ArrayList) m_hIsAllot.get(cwarehouseid);
    UFBoolean isAllot = (UFBoolean) ary1.get(0);
    if (isAllot.booleanValue()) {
      m_btnAllotCarg.setEnabled(true);
      updateButton(m_btnAllotCarg);
    } else {
      m_btnAllotCarg.setEnabled(false);
      updateButton(m_btnAllotCarg);
    }
    //过滤货位
    UIRefPane refCarg =
      (UIRefPane) getArrBillCardPanel().getBodyItem("cstorename").getComponent();
    refCarg.getRefModel().addWherePart(
      "and bd_cargdoc.pk_stordoc = '"
        + cwarehouseid
        + "' and  UPPER(bd_cargdoc.sealflag) <> 'Y' ");
  }
  */
}
/**
 * 功能：行改变
 1.到货列表
 2.转入列表
 3.到货修改及转入修改
 */
public void bodyRowChange(BillEditEvent e) {
    if (getStateStr().equals("到货列表")) {
      bodyRowChangeLookList(e);
    }
    else
    if (getStateStr().equals("转入修改") || getStateStr().equals("到货修改")) {
      bodyRowChangeEdit(e);
    }
}

/**
 * 功能：单据行改变时的处理(到货修改及转入修改)
 */
private void bodyRowChangeEdit(BillEditEvent e) {
  /**设置合格数量编辑及各数量的正负属性*/
  setEditAndDirect(e);
  /**是否显示自由项按钮(是否自由项管理)*/
  //bmrcSetForFree(e); V31 czp del 统一在编辑前处理
  /**设置辅计量信息*/
  setAssisUnitEditState2(e);
  /**存货替换参照设置*/
//  bmrcSetForInvRef(e);V5移动到编辑前事件
  /**仓库：是否进行货位管理*/
  bmrcSetForWareAlot(e);
  /**到货修改时特殊控制*/
  if (getStateStr().equals("到货修改")) {
    bmrcSetForModify(e);
  }
  //卡片编辑时，换行时需要出发此事件
  if(e.getSource().getClass().getName().equals("nc.ui.pub.bill.BillModelRowEditPanel")){
	  beforeEditInv(e);
  }
  /**存货是否批次号管理*/
//  bmrcSetForProdNum(e);V5移动到编辑前事件
  /**项目参照设置*/ 
//  bmrcSetForRefPaneProject(e);V5移动到编辑前事件

  //浮动菜单右键功能权限控制
//  rightButtonRightControl();
}
/**
 * 浏览列表行变换时处理
 * 创建日期：(2001-11-18 15:25:26)
 */
private void bodyRowChangeLookList(BillEditEvent e) {
  //选中行逻辑
  int iLen = getBillListPanel().getHeadTable().getRowCount();
  for (int i = 0; i < iLen; i++) {
    if (getBillListPanel().getHeadTable().isRowSelected(i)) {
      getBillListPanel().getHeadBillModel().setRowState(i, BillModel.SELECTED);
      setDispIndex(i);
      setListBodyData(i);
      setButtonsList();
      updateButtons();
    } else {
      getBillListPanel().getHeadBillModel().setRowState(i, BillModel.NORMAL);
    }
  }
}
/**
 * @功能：检验修改数据的合法性
 * @作者：晁志平
 * 创建日期：(2001-6-20 14:47:40)
 * @return boolean
 * @param newvo nc.vo.rc.receive.ArriveorderVO
 * @param oldvo nc.vo.rc.receive.ArriveorderVO
 */
private boolean checkModifyData(ArriveorderVO newvo, ArriveorderVO oldvo) {
  try {
    /**检查存货辅计量管理时辅信息为必输项*/
    BillModel bm = getBillCardPanel().getBillModel();
    Hashtable hErr = new Hashtable();
    String strInvBasId = null, strErr = null;
    Object objAssUnit = null, objAssNum = null, objExhRate = null;
    if (bm != null) {
      for (int i = 0; i < bm.getRowCount(); i++) {
        strErr = "";
        strInvBasId = (String) bm.getValueAt(i, "cbaseid");
        if (PuTool.isAssUnitManaged(strInvBasId)) {
          objAssUnit = bm.getValueAt(i, "cassistunitname");
          objAssNum = bm.getValueAt(i, "nassistnum");
          objExhRate = bm.getValueAt(i, "convertrate");
          if (objAssUnit == null || objAssUnit.toString().trim().equals(""))
            strErr += m_lanResTool.getStrByID("common","UC000-0003938")/*@res "辅单位"*/;
          if (objAssNum == null || objAssNum.toString().trim().equals("")) {
            if (strErr.length() > 0)
              strErr += m_lanResTool.getStrByID("SCMCOMMON","UPPSCMCommon-000000")/*@res "、"*/;
            strErr += m_lanResTool.getStrByID("common","UC000-0003971")/*@res "辅数量"*/;
          }
          if (objExhRate == null || objExhRate.toString().trim().equals("")) {
            if (strErr.length() > 0)
              strErr += m_lanResTool.getStrByID("SCMCOMMON","UPPSCMCommon-000000")/*@res "、"*/;
            strErr += m_lanResTool.getStrByID("common","UC000-0002161")/*@res "换算率"*/;
          }
          if (strErr.trim().length() > 0)
            hErr.put(new Integer(i + 1), strErr);
        }
      }
    }
    if (hErr.size() > 0) {
      Vector vTmp = new Vector();
      Enumeration keys = hErr.keys();
      Integer iKey = null;
      strErr = "";
      strErr += m_lanResTool.getStrByID("40040301","UPP40040301-000100")/*@res "有辅计量管理的存货出现空项：\n"*/;
      while (keys.hasMoreElements()) {
        iKey = (Integer) keys.nextElement();
        vTmp.addElement(m_lanResTool.getStrByID("40040301","UPP40040301-000101")/*@res "表体 "*/ + iKey + ": "  + hErr.get(iKey) + "\n");
      }
      for (int i = vTmp.size() - 1; i >= 0; i--) {
        strErr += vTmp.elementAt(i);
      }
      MessageDialog.showErrorDlg(this, m_lanResTool.getStrByID("SCMCOMMON","UPPSCMCommon-000059")/*@res "错误"*/, strErr);
      return false;
    }
    ArriveorderHeaderVO head = (ArriveorderHeaderVO) newvo.getParentVO();
    //到货日期
    String arrdate = null;
    if (!(head.getAttributeValue("dreceivedate") == null
      || head.getAttributeValue("dreceivedate").toString().trim().equals(""))) {
      arrdate = head.getAttributeValue("dreceivedate").toString();
    }
    //供应商
    String vendor = (String) head.getAttributeValue("cvendormangid");
    //部门
    //String dept = (String) head.getAttributeValue("cdeptid");
    //业务员
    //String empl = (String) head.getAttributeValue("cemployeeid");
    //业务类型
    String busi = (String) head.getAttributeValue("cbiztype");
    //库存组织
    String sStoreOrgId = head.getCstoreorganization();

    if (arrdate == null || arrdate.trim().equals("")) {
      showErrorMessage(m_lanResTool.getStrByID("40040301","UPP40040301-000102")/*@res "到货日期不能为空"*/);
      return false;
    } else if (vendor == null || vendor.trim().equals("")) {
      showErrorMessage(m_lanResTool.getStrByID("40040301","UPP40040301-000103")/*@res "供应商不能为空"*/);
      return false;
//    } else if (dept == null || dept.trim().equals("")) {
//      showErrorMessage(m_lanResTool.getStrByID("40040301","UPP40040301-000104")/*@res "部门不能为空"*/);
//      return false;
//    } else if (empl == null || empl.trim().equals("")) {
//      showErrorMessage(m_lanResTool.getStrByID("40040301","UPP40040301-000105")/*@res "业务员不能为空"*/);
//      return false;
    } else if (busi == null || busi.trim().equals("")) {
      showErrorMessage(m_lanResTool.getStrByID("40040301","UPP40040301-000106")/*@res "业务类型不能为空"*/);
      return false;
    } else if (sStoreOrgId == null || sStoreOrgId.trim().equals("")) {
      showErrorMessage(m_lanResTool.getStrByID("40040301","UPP40040301-000107")/*@res "库存组织不能为空"*/);
      return false;
    }

    //处理表体有空存货的行
    String lines = "";
    int line = 0;
    for (line = 0;
      line < getBillCardPanel().getBillModel().getRowCount();
      line++) {
      if (getBillCardPanel().getBillModel().getValueAt(line, "cinventorycode")
        == null
        || getBillCardPanel()
          .getBillModel()
          .getValueAt(line, "cinventorycode")
          .toString()
          .trim()
          .equals("")) {
        if (!lines.trim().equals("")) {
          lines += m_lanResTool.getStrByID("SCMCOMMON","UPPSCMCommon-000000")/*@res "、"*/;
        }
        lines += line + 1;
      }
    }
    if (!lines.trim().equals("")) {
      MessageDialog.showWarningDlg(
        this,
        m_lanResTool.getStrByID("SCMCOMMON","UPPSCMCommon-000270")/*@res "提示"*/,
        CommonConstant.BEGIN_MARK
          + lines
          + CommonConstant.END_MARK
          + m_lanResTool.getStrByID("40040301","UPP40040301-000108")/*@res "行存货编码不能为空，请输入存货编码"*/);
      return false;
    }
    //处理到货数量为零的行
    lines = "";
    line = 0;
    for (line = 0;
      line < getBillCardPanel().getBillModel().getRowCount();
      line++) {
      if (getBillCardPanel().getBillModel().getValueAt(line, "narrvnum") == null
        || getBillCardPanel()
          .getBillModel()
          .getValueAt(line, "narrvnum")
          .toString()
          .trim()
          .equals("")
        || ((UFDouble) getBillCardPanel().getBillModel().getValueAt(line, "narrvnum"))
          .doubleValue()
          == 0) {
        if (!lines.trim().equals("")) {
          lines += m_lanResTool.getStrByID("SCMCOMMON","UPPSCMCommon-000000")/*@res "、"*/;
        }
        lines += line + 1;
      }
    }
    if (!lines.trim().equals("")) {
      MessageDialog.showWarningDlg(
        this,
        m_lanResTool.getStrByID("SCMCOMMON","UPPSCMCommon-000270")/*@res "提示"*/,
        CommonConstant.BEGIN_MARK
          + lines
          + CommonConstant.END_MARK
          + m_lanResTool.getStrByID("40040301","UPP40040301-000087")/*@res "行存货到货数量不能为零，也不能为空，请输入到货数量"*/);
      return false;
    }
    //处理到货单价为负的行
    lines = "";
    line = 0;
    for (line = 0;
      line < getBillCardPanel().getBillModel().getRowCount();
      line++) {
      if (!(getBillCardPanel().getBillModel().getValueAt(line, "nprice") == null
        || getBillCardPanel()
          .getBillModel()
          .getValueAt(line, "nprice")
          .toString()
          .trim()
          .equals("")
        || ((UFDouble) getBillCardPanel().getBillModel().getValueAt(line, "nprice"))
          .doubleValue()
          == 0)) {
        //非空非零时如果为负
        if (((UFDouble) getBillCardPanel()
          .getBillModel()
          .getValueAt(line, "nprice"))
          .doubleValue()
          < 0) {
          if (!lines.trim().equals("")) {
            lines += m_lanResTool.getStrByID("SCMCOMMON","UPPSCMCommon-000000")/*@res "、"*/;
          }
          lines += line + 1;
        }
      }
    }
    if (!lines.trim().equals("")) {
      MessageDialog.showWarningDlg(
        this,
        m_lanResTool.getStrByID("SCMCOMMON","UPPSCMCommon-000270")/*@res "提示"*/,
        CommonConstant.BEGIN_MARK
          + lines
          + CommonConstant.END_MARK
          + m_lanResTool.getStrByID("40040301","UPP40040301-000109")/*@res "行存货到货价格小于零，请重新输入到货价格"*/);
      return false;
    }
//    //|赠品数量|<=|到货数量|
//    UFDouble givenum = null;
//    UFDouble arrnum = null;
//    lines = "";
//    line = 0;
//    for (line = 0;
//      line < getArrBillCardPanel().getBillModel().getRowCount();
//      line++) {
//      if ((getArrBillCardPanel().getBillModel().getValueAt(line, "npresentnum")
//        == null
//        || getArrBillCardPanel()
//          .getBillModel()
//          .getValueAt(line, "npresentnum")
//          .toString()
//          .trim()
//          .equals("")
//        || ((UFDouble) getArrBillCardPanel()
//          .getBillModel()
//          .getValueAt(line, "npresentnum"))
//          .doubleValue()
//          == 0)) {
//        givenum = new UFDouble(0);
//      } else {
//        givenum =
//          (UFDouble) getArrBillCardPanel().getBillModel().getValueAt(line, "npresentnum");
//      }
//      if (getArrBillCardPanel().getBillModel().getValueAt(line, "narrvnum") == null
//        || getArrBillCardPanel()
//          .getBillModel()
//          .getValueAt(line, "narrvnum")
//          .toString()
//          .trim()
//          .equals("")
//        || ((UFDouble) getArrBillCardPanel().getBillModel().getValueAt(line, "narrvnum"))
//          .doubleValue()
//          == 0) {
//        arrnum = new UFDouble(0);
//      } else {
//        arrnum =
//          (UFDouble) getArrBillCardPanel().getBillModel().getValueAt(line, "narrvnum");
//      }
//      givenum = new UFDouble(Math.abs(givenum.doubleValue()));
//      arrnum = new UFDouble(Math.abs(arrnum.doubleValue()));
//      if (arrnum.compareTo(givenum) < 0) {
//        if (!lines.trim().equals("")) {
//          lines += "、";
//        }
//        lines += line + 1;
//      }
//    }
//    if (!lines.trim().equals("")) {
//      MessageDialog.showWarningDlg(
//        this,
//        "提示",
//        "到货数量包含赠品数量，"
//          + CommonConstant.BEGIN_MARK
//          + lines
//          + CommonConstant.END_MARK
//          + "行存货赠品数量大于到货数量不合理，请重新输入到货数量或赠品数量");
//      return false;
//    }
  } catch (Exception e) {
    reportException(e);
    return false;
  }
  return true;
}

/**
 * @功能：删除当前作废的到货单(卡片)
 * @作者：晁志平
 * 创建日期：(2001-10-08 20:16:16)
 */
private void delArriveorderVODiscarded() {
  ArriveorderVO[] arrives = null;
  Vector v = new Vector();
  int delIndex = 0;
  try {
    for (int i = 0; i < getCacheVOs().length; i++){
      if (i == getDispIndex()) {
        delIndex = i;
      }
      v.add(i, getCacheVOs()[i]);
    }
    v.remove(delIndex);

    if (v.size() > 0) {
      arrives = new ArriveorderVO[v.size()];
      v.copyInto(arrives);
      setCacheVOs(arrives);
    }else{
      setCacheVOs(null);
    }
    //转入修改或修改两面种状态在保存到货单后重置显示位置
    if (getDispIndex() > 0) {
      setDispIndex(getDispIndex()-1);
    }else{
      setDispIndex(0);
    }

  } catch (Exception e) {
    SCMEnv.out(e);
  }
}

/**
 * @功能：删除当前保存的转入到货单(卡片)
 * @作者：晁志平
 * 创建日期：(2001-7-31 20:11:16)
 * @param:<|>
 * @return:
 * @exception:
 * @see;
 * @since:
 *
 */
private void delArriveorderVOSaved() {
  ArriveorderVO[] arrives = null;
  Vector v = new Vector();
  int delIndex = 0;
  try {
    for (int i = 0; i < getCacheVOs().length; i++){
      if (i == getDispIndex()) {
        delIndex = i;
      }
      v.add(i, getCacheVOs()[i]);
    }
    v.remove(delIndex);

    if (v.size() > 0) {
      arrives = new ArriveorderVO[v.size()];
      v.copyInto(arrives);
      setCacheVOs(arrives);
    }else{
      setCacheVOs(null);
    }
    //转入修改或修改两面种状态在保存到货单后重置显示位置
    if (getDispIndex() > 0) {
      setDispIndex(getDispIndex()-1);
    }else{
      setDispIndex(0);
    }

  } catch (Exception e) {
    SCMEnv.out(e);
  }
}

/**
 * @功能：删除当前作废的到货单(列表)
 * @作者：晁志平
 * 创建日期：(2001-10-09 20:10:16)
 */
private void delArriveorderVOsDiscarded(Vector v_removed) {
  ArriveorderVO[] arrives = null;
  Vector v_all = new Vector();
  try {
    for (int i = 0; i < getCacheVOs().length; i++){
      v_all.add(i, getCacheVOs()[i]);
    }
    for (int i = 0; i < v_removed.size(); i++){
      v_all.remove(v_removed.elementAt(i));
    }
    if (v_all.size() > 0) {
      arrives = new ArriveorderVO[v_all.size()];
      v_all.copyInto(arrives);
      setCacheVOs(arrives);
    }else{
      setCacheVOs(null);
    }

  } catch (Exception e) {
    reportException(e);
    MessageDialog.showErrorDlg(this,m_lanResTool.getStrByID("SCMCOMMON","UPPSCMCommon-000059")/*@res "错误"*/,m_lanResTool.getStrByID("40040301","UPP40040301-000110")/*@res "刷新显示前端缓存时出错"*/);
  }
}

/**
 * @功能：切换到列表界面(维护修改=>>列表)
 * @作者：晁志平
 * 创建日期：(2001-6-20 9:11:08)
 * @param:<|>
 * @return:
 * @exception:
 * @see;
 * @since:
 *
 */
private void displayArrBillListPanel() {
  
  //设置界面状态
  setM_strState("到货列表");
  
  //显示数据到列表界面
  loadDataToList();
  
  //显示列表界面
  if(!m_bLoaded){
    add(getBillListPanel(), "Center");
    m_bLoaded = true;
  }
  getBillCardPanel().setVisible(false);
  getBillListPanel().setVisible(true);
  
  //默认显示第一张
  if (getCacheVOs() != null && getCacheVOs().length > 0) {
    getBillListPanel().getHeadTable().setRowSelectionInterval(
      getDispIndex(),
      getDispIndex());
    getBillListPanel().getHeadBillModel().setRowState(
      getDispIndex(),
      BillModel.SELECTED);
    setListBodyData(getDispIndex());
  }
  setButtonsState();
  updateUI();
}

/**
 * @功能：切换到列表界面(转入修改=>>列表)
 */
private void displayArrBillListPanelNew() {

  //设置界面状态
  setM_strState("转入列表");
  setButtonsState();
  //显示数据到列表界面
  loadDataToList();
  //显示列表界面
  if(!m_bLoaded){
    add(getBillListPanel(), "Center");
    m_bLoaded = true;
  }
  getBillListPanel().setVisible(true);
  getBillCardPanel().setVisible(false);
  //列表状态默认显示处理
  setDispIndex(0);
  if (getCacheVOs() != null && getCacheVOs().length > 0) {
    getBillListPanel().getHeadTable().setRowSelectionInterval(getDispIndex(), getDispIndex());
    getBillListPanel().getHeadBillModel().setRowState(getDispIndex(), BillModel.SELECTED);
    setListBodyData(getDispIndex());
  }
  setButtonsListValueChangedNew(1);
  updateUI();
}

/**
 * 作者：汪维敏
 * 功能：此处插入方法说明
 * 参数：
 * 返回：
 * 例外：
 * 日期：(2004-3-16 15:28:25)
 */
public String getAccYear() {
  return getClientEnvironment().getAccountYear();
}
/**
 * 功能描述:获得到货单卡片
 * 初始化处理：
  1.表体自由项
  2.表头备注设定：
    (1).不自动检返回值查
    (2).返回值设为名称
  3.表头备注设定：
    (1).不自动检返回值查
    (2).返回值设为名称
  4.换算率>=0
  5.单价>=0
  6.收货仓库参照过滤掉废品库
  7.货位非封存
  8.单据模板监听器
  9.精度处理
 * 2002-08-07 wyf   修改采购部门及业务员的数据库类型不匹配问题 DB2
 * 2002-08-08 wyf   修改一个SQL错误
 */
public BillCardPanel getBillCardPanel() {
//  m_arrBillPanel = null;
  if (m_billPanel == null) {
    try {
      m_billPanel = new BillCardPanel();
      BillData bd = new BillData(getBillTempletVO());
      if(bd==null){
    	  m_billPanel.loadTemplet("40040301010000000000");
    	  bd=m_billPanel.getBillData();
      }
      
      initBillBeforeLoad(bd);
      m_billPanel.setBillData(bd);
      /*try {
        m_billPanel.loadTemplet(getModuleCode(), null, getOperatorId(), getCorpPrimaryKey(),new MyBillData());
      } catch (Exception ex) {
        reportException(ex);
        m_billPanel.loadTemplet("40040301010000000000");
      }*/      
      // 单据模板中表体活动功能菜单初始化
      m_billPanel.setBodyMenuShow(true);
      UIMenuItem[] miBody = m_billPanel.getBodyMenuItems();
      if (miBody != null && miBody.length >= 3) {
        miBody[0].setVisible(false);
        miBody[2].setVisible(false);
      }
      m_billPanel.addBodyMenuListener(this);
      //设置千分位
      /*m_billPanel.setBodyShowThMark(true);取消千分位硬编码*/
      //质量等级
      m_billPanel.hideBodyTableCol("squalitylevelname");
      //建议处理意见
      m_billPanel.hideBodyTableCol("cdealname");
      //行号的设置
      if (m_billPanel.getBodyItem("crowno") != null) {
        BillRowNo.loadRowNoItem(m_billPanel, "crowno");
      }
      //处理自定义项
      nc.ui.scm.pub.def.DefSetTool.updateBillCardPanelUserDef(m_billPanel, getClientEnvironment().getCorporation().getPk_corp(),
          ScmConst.PO_Arrive, //单据类型 
          "vdef", "vdef");
      //加合计行
      m_billPanel.setTatolRowShow(true);
      //单据模板编辑监听器
      m_billPanel.addEditListener(this);
      //编辑前监听
      m_billPanel.addBodyEditListener2(this);
      //表体排序监听
      m_billPanel.getBodyPanel().addTableSortListener();
      m_billPanel.getBillModel().setRowSort(true);
      //增加行号排序监听
      m_billPanel.getBillModel().setSortPrepareListener(this) ;

      // 卡片表体排序监听
      m_billPanel.getBillModel().addSortRelaObjectListener2(new IBillRelaSortListener2Body());

      bd = m_billPanel.getBillData();
      DefVO[] defBody = DefSetTool.getBatchcodeDef(getClientEnvironment().getCorporation().getPk_corp());
      if(defBody!=null){
        bd.updateItemByDef(defBody, "bc_vdef", false);
      }
      m_billPanel.setBillData(bd);
      
  	  //V55，支持整行选中
  	  PuTool.setLineSelected(m_billPanel);
  	  m_billPanel.addActionListener(this);

      //since v56, 设置不允许批改的栏目(原则是：尽量不要限制应用。本版仅约束自由项，因为 目前自由项处理机制下支持不了批改)
      PuTool.setBatchModifyForbid(m_billPanel, new String[]{"vfree0"});
    
    } catch (java.lang.Throwable e) {
      SCMEnv.out("初始化单据模板(卡片)时出现异常：");
      SCMEnv.out(e);
    }
  }
  return m_billPanel;
}

/**
 * 获得到货单列表
 *
 */
public BillListPanel getBillListPanel() {
  if (m_arrListPanel == null) {
    try {
      m_arrListPanel = new BillListPanel();
      BillListData bd = new BillListData(getBillTempletVO());
      if(bd==null){
    	  m_arrListPanel.loadTemplet("40040301010000000000");
      }else
      {
    	  m_arrListPanel.setListData(bd);
      }
      /*// 加载模板
      try {
        m_arrListPanel.loadTemplet(getModuleCode(), null, getOperatorId(), getCorpPrimaryKey());
      } catch (Exception ex) {
        reportException(ex);
        m_arrListPanel.loadTemplet("40040301010000000000");
      }*/
      //设置千分位
      /*m_arrListPanel.getParentListPanel().setShowThMark(true);
      m_arrListPanel.getChildListPanel().setShowThMark(true);*/

      //初始化列表精度
      initListDecimal();

      if (m_arrListPanel.getHeadItem("cbiztype") != null)
        m_arrListPanel.hideHeadTableCol("cbiztype");
      if (m_arrListPanel.getHeadItem("cvendormangid") != null)
        m_arrListPanel.hideHeadTableCol("cvendormangid");
      if (m_arrListPanel.getHeadItem("cemployeeid") != null)
        m_arrListPanel.hideHeadTableCol("cemployeeid");
      if (m_arrListPanel.getHeadItem("cdeptid") != null)
        m_arrListPanel.hideHeadTableCol("cdeptid");
      if (m_arrListPanel.getHeadItem("ctransmodeid") != null)
        m_arrListPanel.hideHeadTableCol("ctransmodeid");
      if (m_arrListPanel.getHeadItem("creceivepsn") != null)
        m_arrListPanel.hideHeadTableCol("creceivepsn");
      if (m_arrListPanel.getHeadItem("cstoreorganization") != null)
        m_arrListPanel.hideHeadTableCol("cstoreorganization");
      if (m_arrListPanel.getHeadItem("coperator") != null)
        m_arrListPanel.hideHeadTableCol("coperator");
      if (m_arrListPanel.getHeadItem("cauditpsn") != null)
        m_arrListPanel.hideHeadTableCol("cauditpsn");

      //质量等级
      if (m_arrListPanel.getBodyItem("squalitylevelname") != null)
        m_arrListPanel.hideBodyTableCol("squalitylevelname");
      //建议处理意见
      if (m_arrListPanel.getBodyItem("cdealname") != null)
        m_arrListPanel.hideBodyTableCol("cdealname");

      //处理列表自定义项
      nc.ui.scm.pub.def.DefSetTool.updateBillListPanelUserDef(m_arrListPanel, getCorpPrimaryKey(),
          ScmConst.PO_Arrive, //单据类型 
          "vdef", "vdef");
      m_arrListPanel.setListData(m_arrListPanel.getBillListData());
      //设置列表合计
      m_arrListPanel.getChildListPanel().setTotalRowShow(true);

      //单据编辑监听
      m_arrListPanel.addEditListener(this);
      m_arrListPanel.addMouseListener(this);
      //多选监听
      m_arrListPanel.getHeadTable().setCellSelectionEnabled(false);
      m_arrListPanel.getHeadTable().setSelectionMode(javax.swing.ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
      m_arrListPanel.getHeadTable().getSelectionModel().addListSelectionListener(this);
      //行号排序监听
      m_arrListPanel.getBodyBillModel().setSortPrepareListener(this) ;
      //列表其它字段排序监听
      m_arrListPanel.getHeadBillModel().addSortRelaObjectListener2(this);

  	  //V55，支持整行选中
  	  PuTool.setLineSelectedList(m_arrListPanel);
    } catch (Exception e) {
      SCMEnv.out("初始化单据模板(列表)时出现异常：");
      reportException(e);
    }
  }
  return m_arrListPanel;
}

/**
 * @功能：返回到货单VO数组的头VO数组
 * @作者：Administrator
 * 创建日期：(2001-6-8 21:53:25)
 * @param:<|>
 * @return:
 * @exception:
 * @see;
 * @since:
 *
 * @return nc.vo.pp.ask.AskbillHeaderVO[]
 * @param askbillvos nc.vo.pp.ask.AskbillVO[]
 */
private ArriveorderHeaderVO[] getArriveHeaderVOs(ArriveorderVO[] arrivevos) {
  ArriveorderHeaderVO[] headers = null;
  if (arrivevos != null) {
    headers = new ArriveorderHeaderVO[arrivevos.length];
    for (int i = 0; i < arrivevos.length; i++){
      headers[i] = (ArriveorderHeaderVO)arrivevos[i].getParentVO();
    }
  }
  return headers;
}

/**
 * @功能：查询并设置到货单缓存VO
 */
private void getArriveVOsFromDB() {
  try {
    /*用户自定义条件*/
    ConditionVO[] condsUserDef = getQueryConditionDlg().getConditionVO();
    
    /*用户常用条件*/
    ConditionVO[] condsNormal = getQueryConditionDlg().getNormalCondsVO();
    /*来源单据信息条件*/
    String strUpSrcSQlPart = getQueryConditionDlg().getUpSrcPnl().getSubSQL();
    /*查询数据库*/
    setCacheVOs(
      ArriveorderHelper.queryAllArriveMy(
        condsUserDef,
        condsNormal,
        getCorpPrimaryKey(),
        getBusitype(),
        strUpSrcSQlPart, getClientEnvironment().getUser().getPrimaryKey()));
    /*没有查询到数据时的处理*/
    if (getCacheVOs() == null || !(getCacheVOs().length > 0)) {
      MessageDialog.showWarningDlg(this, m_lanResTool.getStrByID("SCMCOMMON","UPPSCMCommon-000270")/*@res "提示"*/, m_lanResTool.getStrByID("40040301","UPP40040301-000111")/*@res "没有符合条件的记录！"*/);
      if (getBillCardPanel().getBillData() != null) {
        getBillCardPanel().getBillData().clearViewData();
      }
      if (getBillListPanel().getHeadBillModel() != null) {
        getBillListPanel().getHeadBillModel().clearBodyData();
      }
      if (getBillListPanel().getBodyBillModel() != null) {
        getBillListPanel().getBodyBillModel().clearBodyData();
      }
      updateUI();
    }
    /*把查询到数据缓存{处理删除表体,否则会影响可用量}*/
    else {
      checkVprocessbatch(getCacheVOs());
      for (int i = 0; i < getCacheVOs().length; i++) {
        if (getCacheVOs()[i].getChildrenVO() != null
          && getCacheVOs()[i].getChildrenVO().length > 0) {
          //给单据赋来源属性
          String cupsourcebilltype =
            ((ArriveorderItemVO[]) getCacheVOs()[i].getChildrenVO())[0]
              .getCupsourcebilltype();
          ((ArriveorderVO) getCacheVOs()[i]).setUpBillType(cupsourcebilltype);
          //刷新表体哈希表缓存
          for (int j = 0; j < getCacheVOs()[i].getChildrenVO().length; j++) {
            if (getCacheVOs()[i].getChildrenVO()[j].getPrimaryKey() == null)
              continue;
            if (getCacheVOs()[i].getChildrenVO()[j] == null)
              continue;
            hBodyItem.put(
              getCacheVOs()[i].getChildrenVO()[j].getPrimaryKey(),
              getCacheVOs()[i].getChildrenVO()[j]);
          }
        }
      }
    }
  } catch (Exception e) {
    reportException(e);
    MessageDialog.showErrorDlg(this, m_lanResTool.getStrByID("40040301","UPP40040301-000099")/*@res "报错"*/, e.getMessage());
  }
}

/**
 * 功能：获取存量查询对话框
 */
private ATPForOneInvMulCorpUI getAtpDlg() {
  if (null == m_atpDlg) {
    m_atpDlg = new ATPForOneInvMulCorpUI(this);
  }
  return m_atpDlg;
}

/**
 * @功能：退货查询条件对话框-采购
 */
private PoToBackRcQueDLG getBackQueDlgPo() {
  if (m_backQuePoDlg == null) {
    m_backQuePoDlg = new PoToBackRcQueDLG(this, getCorpPrimaryKey());
  }
  return m_backQuePoDlg;
}

/**
 * @功能：退货查询条件对话框-委外
 */
private RcToScQueDLG getBackQueDlgSc() {
  if (m_backQueScDlg == null) {
    m_backQueScDlg =
      new RcToScQueDLG(
        this,
        getCorpPrimaryKey(),
        getOperatorId(),
        "40041015", //虚节点：到货单退货至委外订单
        getBusitype(),
        BillTypeConst.PO_ARRIVE,
        BillTypeConst.SC_ORDER,
        null);
    m_backQueScDlg.addExtraDate("dorderdate", getClientEnvironment().getDate(), getClientEnvironment().getDate());
  }
  return m_backQueScDlg;
}

/**
 * @功能：获取退货参照界面-采购
 */
private ArrFrmOrdUI getBackRefUIPo() {
  if (m_backRefUIPo == null) {
    m_backRefUIPo =
      new ArrFrmOrdUI(
        "corderid",
        getCorpPrimaryKey(),
        getOperatorId(),
        "40041002",
        "1>0",
        BillTypeConst.PO_ORDER,
        getBusitype(),
        PoToBackRcQueDLG.class.getName(),
        BillTypeConst.PO_ARRIVE,
        this,
        true);
  }
  return m_backRefUIPo;
}

/**
 * @功能：获取退货参照界面-采购
 */
private ArrFrmOrdUI getBackRefUISc() {
  if (m_backRefUISc == null) {
    m_backRefUISc =
      new ArrFrmOrdUI(
        "corderid",
        getCorpPrimaryKey(),
        getOperatorId(),
        "40041003",
        "1>0",
        BillTypeConst.SC_ORDER,
        getBusitype(),
        ArrFrmOrdQueDLG.class.getName(),
        BillTypeConst.PO_ARRIVE,
        this,
        true);
  }
  return m_backRefUISc;
}
/**
 * @功能：获取业务类型
 * @作者：晁志平
 * 创建日期：(2001-9-4 15:25:00)
 * @param:<|>
 * @return:
 * @exception:
 * @see;
 * @since:
 *
 * @return java.lang.String
 */
private String getBusitype() {
  if (m_refBusi != null){
    return m_refBusi.getRefPK();
  }else{
    return null;
  }
}

/**
 * 功能：检查数量编辑时合法性
     1. narrvnum != null && narrvnum != 0
     2. nassistnum != null && nassistnum != 0
     3.|nelignum| <= |narrvnum|
     4.|npresentnum| <= |narrvnum|
 * 参数：单据模板编辑事件
 * 返回：错误串
 * 例外：
 * 日期：(2002-7-26 9:03:59)
 * 修改日期，修改人，修改原因，注释标志：
 * @return java.lang.String
 * @param e nc.ui.pub.bill.BillEditEvent
 */
public String getErrMsg(BillEditEvent e) {
  String strErr = null;
  Object oArr = null, oAss = null, oElg = null, oPrsnt = null;
  UFDouble uArr = null, uAss = null, uElg = null, uPrsnt = null;
  BillModel bm = getBillCardPanel().getBillModel();
  oArr = bm.getValueAt(e.getRow(), "narrvnum");
  oAss = bm.getValueAt(e.getRow(), "nassistnum");
  oElg = bm.getValueAt(e.getRow(), "nelignum");
  oPrsnt = bm.getValueAt(e.getRow(), "npresentnum");
  if (oArr != null && !oArr.toString().trim().equals(""))
    uArr = new UFDouble(oArr.toString().trim());
  if (oAss != null && !oAss.toString().trim().equals(""))
    uAss = new UFDouble(oAss.toString().trim());
  if (oElg != null && !oElg.toString().trim().equals(""))
    uElg = new UFDouble(oElg.toString().trim());
  if (oPrsnt != null && !oPrsnt.toString().trim().equals(""))
    uPrsnt = new UFDouble(oPrsnt.toString().trim());
  //数量编辑时
  if (e.getKey().equals("narrvnum")) {
    if (uArr == null)
      return m_lanResTool.getStrByID("40040301","UPP40040301-000090")/*@res "数量为空"*/;
    if (uArr.doubleValue() == 0)
      return m_lanResTool.getStrByID("40040301","UPP40040301-000091")/*@res "数量为零"*/;
  }
  //辅数量编辑时
  if (e.getKey().equals("nassistnum")) {
    if (uAss == null)
      return m_lanResTool.getStrByID("40040301","UPP40040301-000092")/*@res "辅数量为空"*/;
    if (uAss.doubleValue() == 0)
      return m_lanResTool.getStrByID("40040301","UPP40040301-000093")/*@res "辅数量为零"*/;
  }
  //合格数量编辑时
  if (e.getKey().equals("nelignum")) {
    if (uElg != null && uElg.abs().doubleValue() > uArr.abs().doubleValue()) {
      strErr = m_lanResTool.getStrByID("40040301","UPP40040301-000114")/*@res "合格数量绝对值大于到货数量绝对值"*/;
    }
  }
  //赠品数量编辑时
  if (e.getKey().equals("npresentnum")) {
    if (uPrsnt != null && uPrsnt.abs().doubleValue() > uArr.abs().doubleValue()) {
      strErr = m_lanResTool.getStrByID("40040301","UPP40040301-000094")/*@res "赠品数量绝对值大于到货数量绝对值"*/;
    }
  }
  return strErr;
}

/**
 * 获取本节点功能节点ID
 * 创建日期：(2001-10-20 17:29:24)
 * @return java.lang.String
 */
private String getFuncId() {
  String funId = null;
  //funId = this.getModuleCode();
  if (funId == null || funId.trim().equals("")) {
    funId = "40040301";
  }
  return funId;

}

/**
 * @功能：获取定位对话框
 * @作者：晁志平
 * 创建日期：(2001-9-15 13:50:13)
 * @param:<|>
 * @return:
 * @exception:
 * @see;
 * @since:
 *
 * @return nc.ui.rc.receive.LocateDlg
 */
private LocateDlg getLocateDlg() {
  if (locatedlg == null) {
    locatedlg = new LocateDlg(this,(AggregatedValueObject[])getCacheVOs(),getDispIndex(),m_lanResTool.getStrByID("40040301","UPP40040301-000244")/*@res "到货单定位"*/,m_lanResTool.getStrByID("40040301","UPP40040301-000245")/*@res "到货单"*/);
  }
  return locatedlg;
}

/**
 * @功能：返回到货单VO数组
 * @作者：晁志平
 * 创建日期：(2001-6-19 20:13:12)
 * @param:<|>
 * @return:
 * @exception:
 * @see;
 * @since:
 *
 * @return nc.vo.rc.receive.ArriveorderVO[]
 */
public nc.vo.rc.receive.ArriveorderVO[] getCacheVOs() {
  return m_arriveVOs;
}

/**
 * @功能：获取到货单查询条件输入对话框
 */
private RcQueDlg getQueryConditionDlg() {
  if (m_dlgArrQueryCondition == null && isChangeBusitype) {
    m_dlgArrQueryCondition = new RcQueDlg(this, getBusitype(), getFuncId(), getOperatorId(), getCorpPrimaryKey());
    m_dlgArrQueryCondition.addExtraDate("po_arriveorder.dreceivedate", getClientEnvironment().getDate(), getClientEnvironment().getDate());
    isChangeBusitype = false;
  }
  return m_dlgArrQueryCondition;
}

/**
 * @功能：返回当前显示的VO位置
 * @作者：晁志平
 * 创建日期：(2001-6-20 8:47:47)
 * @param:<|>
 * @return:
 * @exception:
 * @see;
 * @since:
 *
 * @return int
 */
private int getDispIndex() {
  return m_iArrCurrRow;
}

/**
 * @功能：返回单据状态
 * @作者：晁志平
 * 创建日期：(2001-6-19 20:18:22)
 * @param:<|>
 * @return:
 * @exception:
 * @see;
 * @since:
 *
 * @return java.lang.String
 */
private java.lang.String getStateStr() {
  return m_strState;
}
/**
 * @功能：返回操作员ID
 * @作者：晁志平
 * 创建日期：(2001-10-24 14:12:52)
 * @return java.lang.String
 */
public String getOperatorId() {
  String operatorid = getClientEnvironment().getUser().getPrimaryKey();
  if (operatorid == null || operatorid.trim().equals("") || operatorid.equals("88888888888888888888")) {
    operatorid = "10013488065564590288";
  }
  return operatorid;
}
/**
 * 作者：汪维敏
 * 功能：此处插入方法说明
 * 参数：
 * 返回：
 * 例外：
 * 日期：(2004-3-16 15:34:30)
 * @return java.lang.String
 * @param userid java.lang.String
 */
public String getPsnIdByOperID(String userid) {
  if (userid == null)
    return null;
  if (userid.trim().equals(""))
    return null;
  userid = userid.trim();
  String psnid = null;
  try {
    psnid = ArriveorderHelper.getPkPsnByPkOper(userid);
  } catch (Exception e) {
    //reportException(e);
    SCMEnv.out("根据操作员关联人员档案时出错");
    psnid = null;
  }
  return psnid;
}

/**
 * 作者：汪维敏
 * 功能：此处插入方法说明
 * 参数：
 * 返回：
 * 例外：
 * 日期：(2004-3-15 13:34:10)
 */
private QueryOrdDlg getQuickArrDlg() {
  if(m_dlgQuickArr == null)
    m_dlgQuickArr = new QueryOrdDlg(this,this);
  return m_dlgQuickArr;
}

/**
 *  功能：1、补齐未改变的到货单表体VO
      2、处理删除表体更新为表体缓存哈希表中之表体
 *  说明：为了对整单加锁所作的特殊处理 czp 2002-11-13
 */
private ArriveorderVO getSaveVO(ArriveorderVO vo) {
  //所有表体向量
  Vector vAllBody = new Vector();
  //未改变的表体VO
  ArriveorderItemVO[] voaUIAllBody =
    (ArriveorderItemVO[]) getBillCardPanel().getBillModel().getBodyValueVOs(
      ArriveorderItemVO.class.getName());
  if (voaUIAllBody == null || voaUIAllBody.length <= 0)
    return vo;
  //未改变的表体VO
  int iNoChangeLen = voaUIAllBody.length;
  for (int i = 0; i < iNoChangeLen; i++) {
    if (voaUIAllBody[i].getStatus() == VOStatus.UNCHANGED) {
      vAllBody.addElement((ArriveorderItemVO) voaUIAllBody[i]);
    }
  }
  //改变的表体VO（拼接本方法参数VO的表体VO）
  int iChangeLen = -1;
  if (vo.getChildrenVO() != null && vo.getChildrenVO().length > 0)
    iChangeLen = vo.getChildrenVO().length;
  if (iChangeLen > 0) {
    for (int i = 0; i < iChangeLen; i++) {
      vAllBody.addElement((ArriveorderItemVO) vo.getChildrenVO()[i]);
    }
  }
  ArriveorderItemVO[] voaAllBody = new ArriveorderItemVO[vAllBody.size()];
  vAllBody.copyInto(voaAllBody);
  //处理删除表体更新为表体缓存哈希表中之表体
  if (hBodyItem != null && hBodyItem.size() > 0) {
    if (voaAllBody != null && voaAllBody.length > 0) {
      for (int i = 0; i < voaAllBody.length; i++) {
        if (voaAllBody[i].getStatus() == VOStatus.DELETED) {
          if (voaAllBody[i].getPrimaryKey() == null)
            continue;
          if (hBodyItem.get(voaAllBody[i].getPrimaryKey()) == null)
            continue;
          voaAllBody[i] =
            (ArriveorderItemVO) hBodyItem.get(voaAllBody[i].getPrimaryKey());
          voaAllBody[i].setStatus(VOStatus.DELETED);
        }
      }
    }
  }
  vo.setChildrenVO(voaAllBody);
  return vo;
}

private ArrayList getSelectedBills() {

  Vector vAll = new Vector();
  ArriveorderVO[] allvos = null;
  //全部选中询价单
  int iPos = 0;
  for (int i = 0; i < getBillListPanel().getHeadBillModel().getRowCount(); i++) {
    if (getBillListPanel().getHeadBillModel().getRowState(i) == BillModel.SELECTED) {
      iPos = i;
      iPos = nc.ui.pu.pub.PuTool.getIndexBeforeSort(getBillListPanel(), iPos);
      vAll.add(getCacheVOs()[iPos]);
    }
  }
  allvos = new ArriveorderVO[vAll.size()];
  vAll.copyInto(allvos);

  //查询未被浏览过的单据体
  try {
    allvos = RcTool.getRefreshedVOs(allvos);
  } catch (BusinessException b) {
    showHintMessage(m_lanResTool.getStrByID("40040301","UPP40040301-000116")/*@res "发现错误:"*/ + b.getMessage());
  } catch (Exception e) {
    SCMEnv.out(e);
    showHintMessage(m_lanResTool.getStrByID("40040301","UPP40040301-000117")/*@res "发现未知错误"*/);
  }
  ArrayList aryRslt = new ArrayList();
  if (allvos != null) {
    for (int i = 0; i < allvos.length; i++) {
      aryRslt.add(allvos[i]);
    }
  }
  return aryRslt;
}

/**
 * 作者：汪维敏
 * 功能：接口IBillModelSortPrepareListener 的实现方法
 * 参数：String sItemKey   ITEMKEY
 * 返回：无
 * 例外：无
 * 日期：(2004-03-24  11:39:21)
 * 修改日期，修改人，修改原因，注释标志：
 */
public int getSortTypeByBillItemKey(String sItemKey) {

  if ("crowno".equals(sItemKey)) {
    return BillItem.DECIMAL;
  } else if("csourcebilllinecode".equals(sItemKey)) {
    return BillItem.DECIMAL;
  } else if("cancestorbillrowno".equals(sItemKey)) {
    return BillItem.DECIMAL;
  }

  return getBillCardPanel().getBillModel().getItemByKey(sItemKey).getDataType();
}

/**
 * 作者：汪维敏
 * 功能：此处插入方法说明
 * 参数：
 * 返回：
 * 例外：
 * 日期：(2004-3-16 15:36:26)
 * @return nc.vo.pub.lang.UFDate
 */
public UFDate getSysDate() {
  return getClientEnvironment().getDate();
}

/**
 * 子类实现该方法，返回业务界面的标题。
 * @version (00-6-6 13:33:25)
 *
 * @return java.lang.String
 */
public String getTitle() {
  String title = m_lanResTool.getStrByID("40040301","UPP40040301-000248")/*@res "到货单维护"*/;
  if (m_billPanel != null)
    title = m_billPanel.getTitle();
  return title;
}
/**
 * V51重构需要的匹配,按钮实例变量化。
 * <p>
 * <b>examples:</b>
 * <p>
 * 使用示例
 * <p>
 * <b>参数说明</b>
 * <p>
 * @author czp
 * @time 2007-3-13 下午01:15:06
 */
private void createBtnInstances(){

  //序列号
//  m_btnSerialNO=getBtnTree().getButton(IButtonConstRc.BTN_ASSIST_FUNC_SERIALNO);
  //生成资产卡片
  m_btnCreateCard=getBtnTree().getButton(IButtonConstRc.BTN_ASSIST_FUNC_CRTCARD);
  //删除资产卡片
  m_btnDeleteCard=getBtnTree().getButton(IButtonConstRc.BTN_ASSIST_FUNC_DELCARD);
	
  //检验
  m_btnCheck = getBtnTree().getButton(IButtonConstRc.BTN_CHECK);
  //业务类型
  m_btnBusiTypes = getBtnTree().getButton(IButtonConstRc.BTN_BUSINESS_TYPE);
  //增加组
  m_btnAdds = getBtnTree().getButton(IButtonConstRc.BTN_ADD);
  //退货组
  m_btnBackPo = getBtnTree().getButton(IButtonConstRc.BTN_BACK_PU);
  m_btnBackSc = getBtnTree().getButton(IButtonConstRc.BTN_BACK_SC);
  m_btnBacks = getBtnTree().getButton(IButtonConstRc.BTN_BACK);
  //单据维护组
  m_btnModify = getBtnTree().getButton(IButtonConstRc.BTN_BILL_EDIT);
  m_btnSave = getBtnTree().getButton(IButtonConstRc.BTN_SAVE);
  m_btnCancel = getBtnTree().getButton(IButtonConstRc.BTN_BILL_CANCEL);
  m_btnDiscard = getBtnTree().getButton(IButtonConstRc.BTN_BILL_DELETE);
  m_btnSendAudit = getBtnTree().getButton(IButtonConstRc.BTN_EXECUTE_AUDIT);  
  m_btnMaintains = getBtnTree().getButton(IButtonConstRc.BTN_BILL);
  //行操作组
  m_btnDelLine = getBtnTree().getButton(IButtonConstRc.BTN_LINE_DELETE);
  m_btnCpyLine = getBtnTree().getButton(IButtonConstRc.BTN_LINE_COPY);
  m_btnPstLine = getBtnTree().getButton(IButtonConstRc.BTN_LINE_PASTE);
  m_btnPstLineTail = getBtnTree().getButton(IButtonConstRc.BTN_LINE_PASTE_TAIL);
  m_btnLines = getBtnTree().getButton(IButtonConstRc.BTN_LINE);  
  //支持弹出菜单中“重排行号”功能
  m_btnReSortRowNo = getBtnTree().getButton(IButtonConstPr.BTN_ADD_NEWROWNO);
  m_btnCardEdit = getBtnTree().getButton(IButtonConstPr.BTN_CARDEDIT);
  m_miReSortRowNo = BillTools.addBodyMenuItem(getBillCardPanel(), m_btnReSortRowNo, null);
  m_miCardEdit = BillTools.addBodyMenuItem(getBillCardPanel(), m_btnCardEdit, null);
  m_miReSortRowNo.addActionListener(new IMenuItemListener());
  m_miCardEdit.addActionListener(new IMenuItemListener());
  //单据浏览组
  m_btnQuery = getBtnTree().getButton(IButtonConstRc.BTN_QUERY);
  m_btnFirst = getBtnTree().getButton(IButtonConstRc.BTN_BROWSE_TOP);
  m_btnPrev = getBtnTree().getButton(IButtonConstRc.BTN_BROWSE_PREVIOUS);
  m_btnNext = getBtnTree().getButton(IButtonConstRc.BTN_BROWSE_NEXT);
  m_btnLast = getBtnTree().getButton(IButtonConstRc.BTN_BROWSE_BOTTOM);
  m_btnLocate = getBtnTree().getButton(IButtonConstRc.BTN_BROWSE_LOCATE);
  m_btnRefresh = getBtnTree().getButton(IButtonConstRc.BTN_BROWSE_REFRESH);
  m_btnBrowses = getBtnTree().getButton(IButtonConstRc.BTN_BROWSE);
  //切换
  m_btnList = getBtnTree().getButton(IButtonConstRc.BTN_SWITCH);
  //执行组(审批、弃审与消息中心共用)
  m_btnActions = getBtnTree().getButton(IButtonConstRc.BTN_EXECUTE);
  //辅助组
  m_btnCombin = getBtnTree().getButton(IButtonConstRc.BTN_PRINT_DISTINCT);
  m_btnPrints = getBtnTree().getButton(IButtonConstRc.BTN_PRINT);
  m_btnPrint = getBtnTree().getButton(IButtonConstRc.BTN_PRINT_PRINT);
  m_btnPrintPreview = getBtnTree().getButton(IButtonConstRc.BTN_PRINT_PREVIEW);
  m_btnSplitPrint = getBtnTree().getButton(IButtonConstRc.BTN_SPLITPRINT);
  m_btnUsable = getBtnTree().getButton(IButtonConstRc.BTN_ASSIST_QUERY_ONHAND);
  m_btnQueryBOM = getBtnTree().getButton(IButtonConstRc.BTN_ASSIST_QUERY_SUITE);
  m_btnQuickReceive = getBtnTree().getButton(IButtonConstRc.BTN_ASSIST_FUNC_QUICK_RECEIVE);
  m_btnDocument = getBtnTree().getButton(IButtonConstRc.BTN_ASSIST_FUNC_DOCUMENT);
  m_btnLookSrcBill = getBtnTree().getButton(IButtonConstRc.BTN_ASSIST_QUERY_RELATED);
  m_btnQueryForAudit = getBtnTree().getButton(IButtonConstRc.BTN_ASSIST_QUERY_WORKFLOW);
  m_btnOthers = getBtnTree().getButton(IButtonConstRc.BTN_ASSIST_QUERY);
  /*
  { m_btnUsable, m_btnQueryBOM,m_btnQuickReceive, m_btnDocument, m_btnLookSrcBill, m_btnQueryForAudit, m_btnCombin, m_btnPrint, m_btnPrintPreview};
  */
  
  /*卡片按钮菜单*/

  m_aryArrCardButtons = getBtnTree().getButtonArray();
  
  /*列表按钮定义*/
  
  m_btnSelectAll = getBtnTree().getButton(IButtonConstRc.BTN_BROWSE_SELECT_ALL);
  m_btnSelectNo = getBtnTree().getButton(IButtonConstRc.BTN_BROWSE_SELECT_NONE);
  m_btnModifyList = m_btnModify;
  m_btnDiscardList = m_btnDiscard;
  m_btnQueryList = m_btnQuery;
  m_btnCard = m_btnList;
  m_btnEndCreate = getBtnTree().getButton(IButtonConstRc.BTN_REF_CANCEL);
  m_btnRefreshList = m_btnRefresh;
  
  //辅助组
  m_btnUsableList = m_btnUsable;
  m_btnDocumentList = m_btnDocument;
  m_btnQueryBOMList = m_btnQueryBOM;
  m_btnPrintPreviewList = m_btnPrintPreview;
  m_btnPrintList = m_btnPrint;
  m_btnOthersList = m_btnOthers;

  /*列表按钮组*/
  m_aryArrListButtons = m_aryArrCardButtons;
  
  /*消息中心按钮组*/
  m_btnAudit = getBtnTree().getButton(IButtonConstRc.BTN_AUDIT);
  m_btnAudit.setTag("APPROVE");
  m_btnUnAudit = getBtnTree().getButton(IButtonConstRc.BTN_EXECUTE_AUDIT_CANCEL);
  m_btnUnAudit.setTag("UNAPPROVE");
  m_btnOthersMsgCenter = m_btnOthers;
  m_btnActionMsgCenter = m_btnActions;
  m_aryMsgCenter = new ButtonObject[]{ m_btnAudit, m_btnActionMsgCenter, m_btnOthersMsgCenter};
}
/**
 * 初始化按钮
 * 创建日期：(01-2-26 13:29:17)
 */
private void setButtonsInit() {
  //特殊功能
  m_btnList.setName(m_lanResTool.getStrByID("SCMCOMMON", "UPPSCMCommon-000464")/*
       * @res
       * "列表显示"
       */);
  //
  for (int i = 0; i < m_aryArrCardButtons.length; i++){
    if(PuTool.isExist(getExtendBtns(),m_aryArrCardButtons[i])){
      continue;
    }
    m_aryArrCardButtons[i].setEnabled(false);
  }
  //放弃转单
  m_btnEndCreate.setVisible(false);
  //辅助功能
  getBtnTree().getButton(IButtonConstRc.BTN_ASSIST_FUNC).setEnabled(true);
  
  getBtnTree().getButton(IButtonConstRc.BTN_PRINT).setEnabled(true);
  m_btnActions.setEnabled(true);
  m_btnAudit.setEnabled(false);
  m_btnUnAudit.setEnabled(false);
  //单据维护
  m_btnMaintains.setEnabled(true);
  m_btnModify.setEnabled(false);
  m_btnSave.setEnabled(false);
  m_btnCancel.setEnabled(false);
  m_btnDiscard.setEnabled(false);
  m_btnSelectAll.setEnabled(false);
  m_btnSendAudit.setEnabled(false);
  //浏览
  m_btnBrowses.setEnabled(true);
  m_btnQuery.setEnabled(true);
  m_btnFirst.setEnabled(false);
  m_btnPrev.setEnabled(false);
  m_btnNext.setEnabled(false);
  m_btnLast.setEnabled(false);
  m_btnSelectAll.setEnabled(false);
  m_btnSelectNo.setEnabled(false);
  //辅助
  m_btnOthers.setEnabled(true);
  m_btnRefresh.setEnabled(false);
  m_btnLocate.setEnabled(false);
  m_btnUsable.setEnabled(false);
  m_btnQueryBOM.setEnabled(false);
  m_btnQuickReceive.setEnabled(true);
  m_btnDocument.setEnabled(false);
  m_btnLookSrcBill.setEnabled(false);
  m_btnPrint.setEnabled(false);
  m_btnSplitPrint.setEnabled(false);
  m_btnCombin.setEnabled(false);
  m_btnPrintPreview.setEnabled(false);
  m_btnQueryForAudit.setEnabled(false);

  m_btnCheck.setEnabled(false);
  
  m_btnBusiTypes.setEnabled(true);
  m_btnAdds.setEnabled(true);

  m_btnList.setEnabled(true);

  m_btnBacks.setEnabled(true);
  m_btnBackPo.setEnabled(true);
  m_btnBackSc.setEnabled(true);
  
  m_btnCreateCard.setEnabled(false);
  m_btnDeleteCard.setEnabled(false);

  //
  updateButtonsAll();
}

/**
 * 功能：初始化精度
 * 参数：
 * 返回：
 * 例外：
 * 日期：(2002-8-21 10:01:19)
 * 修改日期，修改人，修改原因，注释标志：
 */
private void initDecimal() {
  //精度设置

  //数量
  if (m_billPanel.getBodyItem("narrvnum") != null)
    m_billPanel.getBodyItem("narrvnum").setDecimalDigits(m_iPowerNum);
  if (m_billPanel.getBodyItem("nplanarrvnum") != null)
	  m_billPanel.getBodyItem("nplanarrvnum").setDecimalDigits(m_iPowerNum);
  if (m_billPanel.getBodyItem("nelignum") != null)
    m_billPanel.getBodyItem("nelignum").setDecimalDigits(m_iPowerNum);
  if (m_billPanel.getBodyItem("nnotelignum") != null)
    m_billPanel.getBodyItem("nnotelignum").setDecimalDigits(m_iPowerNum);
  if (m_billPanel.getBodyItem("npresentnum") != null)
    m_billPanel.getBodyItem("npresentnum").setDecimalDigits(m_iPowerNum);
  if (m_billPanel.getBodyItem("nwastnum") != null)
    m_billPanel.getBodyItem("nwastnum").setDecimalDigits(m_iPowerNum);
  if (m_billPanel.getBodyItem("nwastassistnum") != null)
	    m_billPanel.getBodyItem("nwastassistnum").setDecimalDigits(m_iPowerNum);
  //辅数量
  if (m_billPanel.getBodyItem("nassistnum") != null)
    m_billPanel.getBodyItem("nassistnum").setDecimalDigits(m_iPowerAssNum);
  //辅数量
  if (m_billPanel.getBodyItem("npresentassistnum") != null)
    m_billPanel.getBodyItem("npresentassistnum").setDecimalDigits(m_iPowerAssNum);
  //换算率
  if (m_billPanel.getBodyItem("convertrate") != null)
    m_billPanel.getBodyItem("convertrate").setDecimalDigits(m_iPowerConvertRate);
  //单价
  if (m_billPanel.getBodyItem("nprice") != null)
    m_billPanel.getBodyItem("nprice").setDecimalDigits(m_iPowerPrice);
  //金额
  if (m_billPanel.getBodyItem("nmoney") != null) {
    m_billPanel.getBodyItem("nmoney").setDecimalDigits(m_iPowerMoney);
  }

}

/**
 * 初始化按钮。
 * <p>
 * <b>examples:</b>
 * <p>
 * 使用示例
 * <p>
 * <b>参数说明</b>
 * <p>
 * @author czp
 * @time 2007-3-19 上午09:26:36
 */
private void initButtons() {
  
  // V51重构需要的匹配,按钮实例变量化

	createBtnInstances();
  /*优化连接数
   * //业务类型按钮组子菜单
  PfUtilClient.retBusinessBtn(m_btnBusiTypes, getCorpPrimaryKey(), BillTypeConst.PO_ARRIVE);
  
  //业务类型按钮打钩处理
  PuTool.initBusiAddBtns(m_btnBusiTypes, m_btnAdds, BillTypeConst.PO_ARRIVE, getCorpPrimaryKey());*/
  PfUtilClient.retBusiAddBtn(m_btnBusiTypes,m_btnAdds, getCorpPrimaryKey(), BillTypeConst.PO_ARRIVE);
  
  retInitBusiAddBtns();
  // 加载扩展按钮
  addExtendBtns();

  // 加载卡片按钮
  setButtons(m_btnTree.getButtonArray());
}

private void retInitBusiAddBtns(){
if (m_btnBusiTypes.getChildButtonGroup() != null
		&& m_btnBusiTypes.getChildButtonGroup().length > 0) {

	ButtonObject[] bo = m_btnBusiTypes.getChildButtonGroup();
	for (int i = 0; i < bo.length; i++) {
		bo[i].setName(bo[i].getName());
	}
	m_btnBusiTypes.setChildButtonGroup(bo);

	m_btnBusiTypes.setTag(m_btnBusiTypes.getChildButtonGroup()[0].getTag());
	m_btnBusiTypes.getChildButtonGroup()[0].setSelected(true);
	m_btnBusiTypes.setCheckboxGroup(true);

	bo = m_btnAdds.getChildButtonGroup();
	for (int i = 0; i < bo.length; i++) {
		bo[i].setName(bo[i].getName());
	}
	m_btnAdds.setChildButtonGroup(bo);
}
}
private void initInvRef(){
	  UIRefPane refCinventorycode = (UIRefPane) getBillCardPanel().getBodyItem("cinventorycode").getComponent();
	  refCinventorycode.setIsCustomDefined(true);
	  refCinventorycode.setRefType(IBusiType.GRID);
	  InvRefModelForRepl refmodel = new InvRefModelForRepl(null,ClientEnvironment.getInstance().getCorporation().getPrimaryKey(),true);
	  refCinventorycode.setRefModel(refmodel);
}

/**
 * 增加扩展按钮。
 * <p>
 * <b>examples:</b>
 * <p>
 * 使用示例
 * <p>
 * <b>参数说明</b>
 * <p>
 * @author czp
 * @time 2007-3-19 上午09:23:34
 */
private void addExtendBtns() {
  ButtonObject[] btnsExtend = getExtendBtns();    
  if(btnsExtend == null || btnsExtend.length <= 0){
    return;
  }
  ButtonObject boExtTop = getBtnTree().getExtTopButton();
  getBtnTree().addMenu(boExtTop);
  int iLen = btnsExtend.length;
  try{
    for(int j=0; j<iLen;j++){
        getBtnTree().addChildMenu(boExtTop, btnsExtend[j]);
    }
  }catch(BusinessException be){
    showHintMessage(be.getMessage());
    return;
  }
}
/**
 * 涉及远程调用的初始化
 * <p>
 * <b>examples:</b>
 * <p>
 * 使用示例
 * <p>
 * <b>参数说明</b>
 * <p>
 * @author czp
 * @time 2009-7-23 下午02:18:54
 */
private void initParaBetter(){
    //
    IQueryForInitUI srvInitQry = NCLocator.getInstance().lookup(IQueryForInitUI.class);
    
    //精度：数量、辅数量、单价、金额
    InitValue initValue = null;    
    try{
        initValue = srvInitQry.qeryInitValue();   
    }catch(BusinessException e){
        showHintMessage(e.getMessage());
        return;
    }
    m_iPowerAssNum = initValue.m_iPowerAssNum;
    m_iPowerConvertRate = initValue.m_iPowerConvertRate;
    m_iPowerNum = initValue.m_iPowerNum;
    m_iPowerPrice = initValue.m_iPowerPrice;
    //本币金额精度
    m_iPowerMoney = initValue.m_iPowerNmoney;
    m_bSaveMaker = initValue.m_bSaveMaker;
    //质量管理模块启用
    m_bQcEnabled = initValue.m_bQcEnabled;
    m_bAIMEnabled = initValue.m_bAIMEnabled;
    //ButtonTree
    //m_btnTree = initValue.m_btnTree;
}
/**
 * 功能描述:节点初始化
 *
 * 获取业务类型、加载单据模板、初始化按钮状态

 */
private void initialize() {

  //初始化精度
  //initPara();
  initParaBetter();
    
  //初始化按钮
  initButtons();  

  //设置按钮状态
  setButtonsState();
  
  //显示单据
  setLayout(new java.awt.BorderLayout());
  add(getBillCardPanel(), BorderLayout.CENTER);

  //初始化表体备注
  initVmemoBody();

  //初始化精度
  initDecimal();

  //初始化到货单状态
  getBillCardPanel().setEnabled(false);
//  getBillListPanel().setListData(getBillListPanel().getBillListData());

}
/**
 * 功能：初始化表体备注
 */
private void initVmemoBody(){
  if (getBillCardPanel().getBodyItem("vmemo") != null) {
    UIRefPane nRefPanel = (UIRefPane) getBillCardPanel().getBodyItem("vmemo").getComponent();
    nRefPanel.setTable(getBillCardPanel().getBillTable());
    nRefPanel.getRefModel().setRefCodeField(nRefPanel.getRefModel().getRefNameField());
    nRefPanel.getRefModel().setBlurFields(new String[] { nRefPanel.getRefModel().getRefNameField()});
    nRefPanel.setAutoCheck(false);
  }
}
/**
 * 功能：初始化精度
 * 参数：
 * 返回：
 * 例外：
 * 日期：(2002-8-21 10:01:19)
 * 修改日期，修改人，修改原因，注释标志：
 */
private void initListDecimal() {
  //精度设置

  //数量
  if (m_arrListPanel.getBodyItem("narrvnum") != null)
    m_arrListPanel.getBodyItem("narrvnum").setDecimalDigits(m_iPowerNum);
  if (m_arrListPanel.getBodyItem("nplanarrvnum") != null)
	  m_arrListPanel.getBodyItem("nplanarrvnum").setDecimalDigits(m_iPowerNum);
  if (m_arrListPanel.getBodyItem("nelignum") != null)
    m_arrListPanel.getBodyItem("nelignum").setDecimalDigits(m_iPowerNum);
  if (m_arrListPanel.getBodyItem("nnotelignum") != null)
    m_arrListPanel.getBodyItem("nnotelignum").setDecimalDigits(m_iPowerNum);
  if (m_arrListPanel.getBodyItem("npresentnum") != null)
    m_arrListPanel.getBodyItem("npresentnum").setDecimalDigits(m_iPowerNum);
  if (m_arrListPanel.getBodyItem("nwastnum") != null)
    m_arrListPanel.getBodyItem("nwastnum").setDecimalDigits(m_iPowerNum);
  //辅数量
  if (m_arrListPanel.getBodyItem("nassistnum") != null)
    m_arrListPanel.getBodyItem("nassistnum").setDecimalDigits(m_iPowerAssNum);
  //换算率
  if (m_arrListPanel.getBodyItem("convertrate") != null)
    m_arrListPanel.getBodyItem("convertrate").setDecimalDigits(m_iPowerConvertRate);
  //单价
  if (m_arrListPanel.getBodyItem("nprice") != null)
    m_arrListPanel.getBodyItem("nprice").setDecimalDigits(m_iPowerPrice);
  //金额
  if (m_arrListPanel.getBodyItem("nmoney") != null)
    m_arrListPanel.getBodyItem("nmoney").setDecimalDigits(m_iPowerMoney);

}

/**
 * 功能描述:初始化参数
 */
public void initPara() {
  try {
    ServcallVO[] scDisc = new ServcallVO[2];
    //初始化精度（数量、单价）
    scDisc[0] = new ServcallVO();
    scDisc[0].setBeanName("nc.itf.pu.pub.IPub");
    scDisc[0].setMethodName("getDigitBatch");
    scDisc[0].setParameter(new Object[] { getCorpPrimaryKey(), new String[] { "BD502", "BD503", "BD501", "BD505" }});
    scDisc[0].setParameterTypes(new Class[] { String.class, String[].class });

    scDisc[1] = new ServcallVO();
    scDisc[1].setBeanName("nc.itf.rc.receive.IArriveorder");
    scDisc[1].setMethodName("getCurrDecimal");
    scDisc[1].setParameter(new Object[] { getCorpPrimaryKey()});
    scDisc[1].setParameterTypes(new Class[] { String.class });
    
    //
    String strPara0 = SysInitBO_Client.getParaString(PoPublicUIClass.getLoginPk_corp(),"PO060");
    
    //后台一次调用
    Object[] oParaValue = nc.ui.scm.service.LocalCallService.callService(scDisc);
    if (oParaValue != null && oParaValue.length == scDisc.length) {
          //数量、单据精度
      int[] iDigits = (int[]) oParaValue[0];
      if (iDigits != null && iDigits.length == 4) {
          m_iPowerAssNum = iDigits[0];
          m_iPowerConvertRate = iDigits[1];
          m_iPowerNum = iDigits[2];
          m_iPowerPrice = iDigits[3];
      }
      //本币金额精度
      m_iPowerMoney = ((Integer) oParaValue[1]).intValue();
      String s = strPara0;
      if(s != null) m_bSaveMaker = (new UFBoolean(s)).booleanValue();
    }
    //质量管理模块启用
    ICreateCorpQueryService tt = NCLocator.getInstance().lookup(ICreateCorpQueryService.class);
    m_bQcEnabled = tt.isEnabled(getCorpPrimaryKey(), ProductCode.PROD_QC);
    m_bAIMEnabled = tt.isEnabled(getCorpPrimaryKey(), "AIM");
  } catch (Exception e) {
      showHintMessage(e.getMessage());
      reportException(e);
  }

}

/**
 * 功能：初始化参照
 * 参数：
 * 返回：
 * 例外：
 * 日期：(2002-8-21 10:01:19)
 * 修改日期，修改人，修改原因，注释标志：
 */
private void initRefPane(BillData bd) {
  //－－－－－－－－表头－－－－－－－－

  //业务类型初始化
  if (bd.getHeadItem("cbiztype") != null) {
    m_refBusi = (UIRefPane) bd.getHeadItem("cbiztype").getComponent();
    m_refBusi.setEnabled(false);
  }
  //部门
  if (bd.getHeadItem("cdeptid") != null) {
    String sql = "and (bd_deptdoc.deptattr IN ('2','4'))";
    UIRefPane refDept = (UIRefPane) (bd.getHeadItem("cdeptid").getComponent());
    /*
    String sqltemp = refDept.getRefModel().getWherePart();
    if ((sqltemp != null) && (!(sqltemp.trim().equals("")))) {
      sql = sql + " and " + sqltemp;
    }
    */
    refDept.getRefModel().addWherePart(sql);
  }
  //业务员
  if (bd.getHeadItem("cemployeeid") != null) {
//    String sql = "and (bd_psndoc.pk_deptdoc IN (SELECT pk_deptdoc FROM bd_deptdoc WHERE (deptattr IN ('2','4')) AND dr = 0))";
    UIRefPane refEmpl = (UIRefPane) (bd.getHeadItem("cemployeeid").getComponent());
    nc.ui.pu.pub.PurPsnRefModel refPsnModel = new nc.ui.pu.pub.PurPsnRefModel(getCorpPrimaryKey());
    refEmpl.setRefModel(refPsnModel);
    refEmpl.getRefModel().setHiddenFieldCode(new String[] { "bd_psndoc.pk_psndoc", "bd_psndoc.pk_deptdoc" });
    /*
    String sqltemp = refEmpl.getRefModel().getWherePart();
    if ((sqltemp != null) && (!(sqltemp.trim().equals("")))) {
      sql = sql + " and " + sqltemp;
    }
    */
//    refEmpl.getRefModel().addWherePart(sql);
  }
  //表头退货理由
  if (bd.getHeadItem("vbackreasonh") != null) {
    UIRefPane refPaneReason = (UIRefPane) bd.getHeadItem("vbackreasonh").getComponent();
    refPaneReason.setRefModel(new BackReasonRefModel());
    refPaneReason.setAutoCheck(false);
  }
  //表头备注
  if (bd.getHeadItem("vmemo") != null) {
    UIRefPane refVmemo = (UIRefPane) bd.getHeadItem("vmemo").getComponent();
    refVmemo.setRefNodeName("常用摘要");
    refVmemo.getRefModel().setRefCodeField(refVmemo.getRefModel().getRefNameField());
    refVmemo.getRefModel().setBlurFields(new String[] { refVmemo.getRefModel().getRefNameField()});
    refVmemo.setAutoCheck(false);
  }

  //库存组织
  if (bd.getHeadItem("cstoreorganization") != null) {
    UIRefPane refPane = (UIRefPane) bd.getHeadItem("cstoreorganization").getComponent();
    refPane.getRefModel().addWherePart(" and (bd_calbody.property = 0 or bd_calbody.property = 1) ");
  }

  //－－－－－－－－表体－－－－－－－－

  //自由项
  /*
  if (bd.getBodyItem("vfree0") != null && bd.getBodyItem("vfree0") != null) {
    FreeItemRefPane m_firpFreeItemRefPane = new FreeItemRefPane();
    m_firpFreeItemRefPane.setMaxLength(bd.getBodyItem("vfree0").getLength());
    //加监听器
    m_firpFreeItemRefPane.getUIButton().addActionListener(this);
    bd.getBodyItem("vfree0").setComponent(m_firpFreeItemRefPane);

  }
  */
  //表体批次号参照
  try {
      if (bd.getBodyItem("vproducenum") != null && bd.getBodyItem("vproducenum").isShow()) {
//      UIRefPane lotRef = (UIRefPane)InterServUI.getInterInstance(ProductCode.PROD_IC,InterRegister.IC0012);
      UIRefPane lotRef = (UIRefPane) new LotNumbRefPane();
      lotRef.setIsCustomDefined(true);
      lotRef.setMaxLength(bd.getBodyItem("vproducenum").getLength());
      bd.getBodyItem("vproducenum").setComponent(lotRef);
    }
    } catch (Exception e) {
        SCMEnv.out(e);
    }

//  //表体备注处理
//  if (bd.getBodyItem("vmemo") != null) {
//    UIRefPane nRefPanel = (UIRefPane) bd.getBodyItem("vmemo").getComponent();
//    nRefPanel.setTable(bd.getBillTable());
//    nRefPanel.getRefModel().setRefCodeField(nRefPanel.getRefModel().getRefNameField());
//    nRefPanel.getRefModel().setBlurFields(new String[] { nRefPanel.getRefModel().getRefNameField()});
//    nRefPanel.setAutoCheck(false);
//  }

  //项目参照
  if (bd.getBodyItem("cproject") != null) {
    String sql = "(upper(isnull(bd_jobbasfil.sealflag,'N')) = 'N')";
    UIRefPane ref = (UIRefPane) (bd.getBodyItem("cproject").getComponent());
    String sqltemp = ref.getRefModel().getWherePart();
    if ((sqltemp != null) && (!(sqltemp.trim().equals("")))) {
      sql = sql + " and " + sqltemp;
    }
    ref.getRefModel().setWherePart(sql);
  }
  //价格非负
  if (bd.getBodyItem("nprice") != null) {
    UIRefPane refPrice = (UIRefPane) bd.getBodyItem("nprice").getComponent();
    refPrice.setMinValue(0);
    refPrice.setMaxValue(nc.vo.scm.field.pu.FieldMinMaxValue.MAX_NUM);
  }
  //换算率非负
  if (bd.getBodyItem("convertrate") != null) {
    UIRefPane refConvert = (UIRefPane) bd.getBodyItem("convertrate").getComponent();
    refConvert.setMinValue(0);
    refConvert.setMaxValue(nc.vo.scm.field.pu.FieldMinMaxValue.MAX_NUM);
  }
  //保质期天数非负
  if (bd.getBodyItem("ivalidday") != null) {
    UIRefPane refVld = (UIRefPane) bd.getBodyItem("ivalidday").getComponent();
    refVld.setMinValue(0);
    refVld.setMaxValue(nc.vo.scm.field.pu.FieldMinMaxValue.MAX_NUM);
  }

  //表体退货理由
  if (bd.getBodyItem("vbackreasonb") != null) {
    UIRefPane refPaneReason1 = new UIRefPane();
    refPaneReason1.setRefModel(new BackReasonRefModel());
    bd.getBodyItem("vbackreasonb").setComponent(refPaneReason1);
    refPaneReason1.getRefModel().setRefCodeField(refPaneReason1.getRefModel().getRefNameField());
    refPaneReason1.getRefModel().setBlurFields(new String[] { refPaneReason1.getRefModel().getRefNameField()});
    refPaneReason1.setAutoCheck(false);
    refPaneReason1.setReturnCode(true);
  }
  //表体退货理由处理
  //if (bd.getBodyItem("vbackreasonb") != null) {
    //UIRefPane refPanel = (UIRefPane) bd.getBodyItem("vbackreasonb").getComponent();
    //refPanel.setTable(bd.getBillTable());
    //refPanel.getRefModel().setRefCodeField(refPanel.getRefModel().getRefNameField());
    //refPanel.getRefModel().setBlurFields(new String[] { refPanel.getRefModel().getRefNameField()});
    //refPanel.setAutoCheck(false);
  //}

  //收货仓库
  if (bd.getBodyItem("cwarehousename") != null) {
    UIRefPane refStore = (UIRefPane) bd.getBodyItem("cwarehousename").getComponent();
    refStore.setRefModel(new WarehouseRefModel(getCorpPrimaryKey()));
    refStore.getRefModel().addWherePart(" and UPPER(bd_stordoc.gubflag) <> 'Y' and UPPER(bd_stordoc.sealflag) <> 'Y' ");
  }
  //货位非封存
  if (bd.getBodyItem("cstorename") != null) {
    UIRefPane refCarg = (UIRefPane) bd.getBodyItem("cstorename").getComponent();
    refCarg.getRefModel().addWherePart("and UPPER(bd_cargdoc.sealflag) <> 'Y' ");
  }
  //辅计量参照
  if (bd.getBodyItem("cassistunitname") != null) {
    ((UIRefPane) (bd.getBodyItem("cassistunitname").getComponent())).setIsCustomDefined(true);
    ((UIRefPane) (bd.getBodyItem("cassistunitname").getComponent())).setRefModel(new OtherRefModel("辅计量单位"));
    ((UIRefPane) (bd.getBodyItem("cassistunitname").getComponent())).setReturnCode(false);
    ((UIRefPane) (bd.getBodyItem("cassistunitname").getComponent())).setRefInputType(UIRefPane.REFINPUTTYPE_CODE);
    ((UIRefPane) (bd.getBodyItem("cassistunitname").getComponent())).setCacheEnabled(false);
  }

}

/**
 * 是否退货单据
 */
private boolean isBackBill() {
  if (!getBillCardPanel().isVisible()) {
    SCMEnv.out("1.bisback='N'");
    return false;
  }
  if (getBillCardPanel().getHeadItem("bisback") == null
    || getBillCardPanel().getHeadItem("bisback").getValue() == null
    || getBillCardPanel().getHeadItem("bisback").getValue().trim().equals("")) {
    SCMEnv.out("2.bisback='N'");
    return false;
  }
  if (new UFBoolean(getBillCardPanel().getHeadItem("bisback").getValue())
    .booleanValue()) {
    SCMEnv.out("3.bisback='Y'");
    return true;
  }
  SCMEnv.out("4.bisback='N'");
  return false;
}

/**
 * 功能：(适用于修改的表行)根据到货数量关系判断到货单行是否免检
 * 参数：
 * 返回：算法(适用于修改的表行):
     naccumchecknum == null(0) && (nelignum + nnotelignum != null(0)) 返回 true
 * 例外：
 * 日期：(2002-9-12 13:03:45)
 * 修改日期，修改人，修改原因，注释标志：
 * @return boolean
 * @param e nc.ui.pub.bill.BillEditEvent
 */
private boolean isCheckFree(BillEditEvent e) {
  //当前单据表体哈希表（key = 行ID）
  Hashtable hRowIdBody = new Hashtable();
  for (int i = 0;
    i < getCacheVOs()[getDispIndex()].getChildrenVO().length;
    i++) {
    hRowIdBody.put(
      getCacheVOs()[getDispIndex()].getChildrenVO()[i].getAttributeValue(
        "carriveorder_bid"),
      getCacheVOs()[getDispIndex()].getChildrenVO()[i]);
  }
  String sRowId =
    (String) getBillCardPanel().getBillModel().getValueAt(
      e.getRow(),
      "carriveorder_bid");
  ArriveorderItemVO item = (ArriveorderItemVO) hRowIdBody.get(sRowId);
  UFDouble nAcc = null, nElg = null, nNotElg = null;
  nAcc =
    (item.getNaccumchecknum() == null
      || item.getNaccumchecknum().doubleValue() == 0)
      ? new UFDouble(0)
      : item.getNaccumchecknum();
  nElg =
    (item.getNelignum() == null || item.getNelignum().doubleValue() == 0)
      ? new UFDouble(0)
      : item.getNelignum();
  nNotElg =
    (item.getNnotelignum() == null || item.getNnotelignum().doubleValue() == 0)
      ? new UFDouble(0)
      : item.getNnotelignum();
  if (nAcc.doubleValue() == 0 && nElg.add(nNotElg).doubleValue() != 0) {
    return true;
  }
  return false;
}

/**
 * 是否只存在到货或退货一种单据(即不存在交叉)
 */
private boolean isOnlyOneTypeBill() {
  BillModel bm = getBillListPanel().getHeadBillModel();
  int iRowCnt = bm.getRowCount();
  if (iRowCnt <= 0)
    return true;
  iRowCnt = getBillListPanel().getHeadTable().getSelectedRowCount();
  if (iRowCnt <= 1)
    return true;
  Object objTmp = null;
  Vector vJudge = new Vector();
  for (int i = 0; i < iRowCnt; i++) {
    if (bm.getRowState(i) == BillModel.SELECTED) {
      objTmp = bm.getValueAt(i, "bisback");
      if (vJudge.size() > 0 && !vJudge.contains(objTmp))
        return false;
      vJudge.addElement(objTmp);
    }
  }
  vJudge = null;
  return true;
}

/**
 * @功能：加载表头基础数据
 */
private void loadBDData() {
  /*变量定义*/
  String strFormula[] = new String[]{"vendor->getColValue(bd_cubasdoc,custshortname,pk_cubasdoc,cvendorbaseid)",
      "cdeptname->getColValue(bd_deptdoc,deptname,pk_deptdoc,cdeptid)",
      "cemployee->getColValue(bd_psndoc,psnname,pk_psndoc,cemployeeid)",
      "ctransmode->getColValue(bd_sendtype,sendname,pk_sendtype,ctransmodeid)",
      "creceivepsnlist->getColValue(bd_psndoc,psnname,pk_psndoc,creceivepsn)",
      "cstoreorganizationname->getColValue(bd_calbody,bodyname,pk_calbody,cstoreorganization)",
      "cbiztypename->getColValue(bd_busitype,businame,pk_busitype,cbiztype)"};
  String strVarValue = null, strValue = null;
  nc.ui.pub.formulaparse.FormulaParse parse = new nc.ui.pub.formulaparse.FormulaParse();
  Hashtable hData[] = new Hashtable[7];
  UIRefPane refpnl[] = new UIRefPane[7];
  
  for(int i = 0; i < 7; i++) hData[i] = new Hashtable();
  /*供应商*/
  if (getCacheVOs()[getDispIndex()] != null && getCacheVOs()[getDispIndex()].getParentVO() != null) {
    strVarValue = (String) getCacheVOs()[getDispIndex()].getParentVO().getAttributeValue("cvendorbaseid");
    refpnl[0] = (UIRefPane) getBillCardPanel().getHeadItem("cvendormangid").getComponent();
    strValue = refpnl[0].getUITextField().getText();
    if (strVarValue != null && (strValue == null || strValue.trim().equals(""))) {
      hData[0].put("cvendorbaseid", strVarValue);
    }
  }
  /*部门*/
  strVarValue = getBillCardPanel().getHeadItem("cdeptid").getValue();
  refpnl[1] = (UIRefPane) getBillCardPanel().getHeadItem("cdeptid").getComponent();
  strValue = refpnl[1].getUITextField().getText();
  if (strVarValue != null && (strValue == null || strValue.trim().equals(""))) {
    hData[1].put("cdeptid", strVarValue);
  }
  /*业务员*/
  strVarValue = getBillCardPanel().getHeadItem("cemployeeid").getValue();
  refpnl[2] = (UIRefPane) getBillCardPanel().getHeadItem("cemployeeid").getComponent();
  strValue = refpnl[2].getUITextField().getText();
  if (strVarValue != null && (strValue == null || strValue.trim().equals(""))) {
    hData[2].put("cemployeeid", strVarValue);
  }
  /*发运方式*/
  strVarValue = getBillCardPanel().getHeadItem("ctransmodeid").getValue();
  refpnl[3] = (UIRefPane) getBillCardPanel().getHeadItem("ctransmodeid").getComponent();
  strValue = refpnl[3].getUITextField().getText();
  if (strVarValue != null && (strValue == null || strValue.trim().equals(""))) {
    hData[3].put("ctransmodeid", strVarValue);
  }
  /*收货人*/
  strVarValue = getBillCardPanel().getHeadItem("creceivepsn").getValue();
  refpnl[4] = (UIRefPane) getBillCardPanel().getHeadItem("creceivepsn").getComponent();
  strValue = refpnl[4].getUITextField().getText();
  if (strVarValue != null && (strValue == null || strValue.trim().equals(""))) {
    hData[4].put("creceivepsn", strVarValue);
  }
  /*库存组织*/
  strVarValue = getBillCardPanel().getHeadItem("cstoreorganization").getValue();
  refpnl[5] = (UIRefPane) getBillCardPanel().getHeadItem("cstoreorganization").getComponent();
  strValue = refpnl[5].getUITextField().getText();
  if (strVarValue != null && (strValue == null || strValue.trim().equals(""))) {
    hData[5].put("cstoreorganization", strVarValue);
  }
  /*业务类型*/
  strVarValue = getBillCardPanel().getHeadItem("cbiztype").getValue();
  refpnl[6] = (UIRefPane) getBillCardPanel().getHeadItem("cbiztype").getComponent();
  strValue = refpnl[6].getUITextField().getText();
  if (strVarValue != null && (strValue == null || strValue.trim().equals(""))) {
    hData[6].put("cbiztype", strVarValue);
  }
  
  Vector v1 = new Vector(), v2 = new Vector(), v3 = new Vector();
  for(int i = 0; i < 7; i++){
    if(hData[i].size() > 0){
      v1.addElement(strFormula[i]);
      v2.addElement(hData[i]);
      v3.addElement(new Integer(i));
    }
  }
  if(v1.size() > 0){
    strFormula = new String[v1.size()];
    v1.copyInto(strFormula);
    hData = new Hashtable[v2.size()];
    v2.copyInto(hData);
    
    parse.setExpressArray(strFormula);
    parse.setDataSArray(hData);
    String s[] = parse.getValueS();
    if(s != null && s.length == v1.size()){
      for(int i = 0; i < v1.size(); i++){
        int j = ((Integer)v3.elementAt(i)).intValue();
        if (s != null && j < s.length) {
          refpnl[j].getUITextField().setText(s[j]);
        }
      }
    }
  }
}



public void mouse_doubleclick(nc.ui.pub.bill.BillMouseEnent e) {
  if (e.getPos() == BillItem.HEAD) {
    if (getStateStr().equals("转入列表")) {
    	InformationCostVO[] vos = null;
    	vos = (InformationCostVO[])getBillListPanel().getBodyBillModel("jj_scm_informationcost").getBodyValueVOs(InformationCostVO.class.getName());
      setM_strState("转入修改");
      onCardNew();
//  	String pk =	getBillCardPanel().getHeadItem("carriveorderid").getValue();
//	String sql = "cbillid = '"+pk+"' and dr = 0";
//	
//try {
//	 vos = (InformationCostVO[])JJPuScmPubHelper.querySmartVOs(InformationCostVO.class, null, sql);
//} catch (Exception e1) {
//	// TODO Auto-generated catch block
//	e1.printStackTrace();
//}
if(vos!=null&&vos.length!=0){
	getBillCardPanel().getBillModel("jj_scm_informationcost").setBodyDataVO(vos);
	getBillCardPanel().getBillModel("jj_scm_informationcost").execLoadFormula();
}else{
	//20101013-11-48  MeiChao 费用为空时,清空历史费用信息.
	getBillCardPanel().getBillModel("jj_scm_informationcost").setBodyDataVO(null);
}
    } else {
      //如果没有单据体，则认为并发并返回
      ArriveorderItemVO[] items =
        (ArriveorderItemVO[]) getBillListPanel().getBodyBillModel().getBodyValueVOs(
          ArriveorderItemVO.class.getName());
      if (items == null || items.length <= 0)
        return;
      //
      isFrmList = true;
      setM_strState("到货浏览");
      onCard();
    }
  }
}

/**
 * 功能:执行审批
 */
private void onAudit(ButtonObject bo) {
  showHintMessage(m_lanResTool.getStrByID("40040301","UPP40040301-000118")/*@res "正在审批..."*/);
  ArriveorderVO vo = getCacheVOs()[getDispIndex()];  
  //回退审批人及审批日期哈希表，审批失败时用到
  String strPsnOld = vo.getHeadVO().getCauditpsn();
  UFDate dateAuditOld = vo.getHeadVO().getDauditdate();
  //
  try {
    //设置审批人、审批日期
    if (vo == null || vo.getParentVO() == null)
      return;
    //V31SP1:增加审批日期不能小于到货日期限制
//    String strErr = PuTool.getAuditLessThanMakeMsg(new ArriveorderVO[] { vo },"dreceivedate","varrordercode", getClientEnvironment().getDate(),ScmConst.PO_Arrive);
//    if (strErr != null) {
//      throw new BusinessException(strErr);
//    }
    vo.getParentVO().setAttributeValue("dauditdate", getClientEnvironment().getDate());
    vo.getParentVO().setAttributeValue("cauditpsn", getOperatorId());
    vo.getParentVO().setAttributeValue("cuserid", getOperatorId());

    
    HashMap<String, String> mapConvertrate = new HashMap<String, String>();
    String sRowId = "";
    for (int i = 0; i < getBillCardPanel().getBodyPanel().getTable().getRowCount(); i++) {
      sRowId = PuPubVO.getString_TrimZeroLenAsNull(getBillCardPanel().getBillModel().getValueAt(i, "carriveorder_bid"));
      if(sRowId!=null){
        mapConvertrate.put(sRowId, PuPubVO.getString_TrimZeroLenAsNull(getBillCardPanel().getBillModel().getValueAt(i, "convertrate")));
      }
    }
    for (int j = 0; j < vo.getChildrenVO().length; j++) {
      vo.getChildrenVO()[j].setAttributeValue("convertrate",new UFDouble(mapConvertrate.get(vo.getChildrenVO()[j].getPrimaryKey())));
    }

    /*审批*/
    PfUtilClient.processBatchFlow(null, "APPROVE", ScmConst.PO_Arrive, getClientEnvironment().getDate().toString(), new ArriveorderVO[] { vo }, null);
    
    if (!PfUtilClient.isSuccess()) {
      //操作失败，恢复审批人及审批日期
      vo.getHeadVO().setCauditpsn(strPsnOld);
      vo.getHeadVO().setDauditdate(dateAuditOld);
      //
      showHintMessage(m_lanResTool.getStrByID("40040301","UPP40040301-000119")/*@res "审批未成功"*/);
      return;
    }
    /*审批成功后刷新*/   
    refreshVoFieldsByKey(vo,vo.getParentVO().getPrimaryKey());
    //
    getCacheVOs()[getDispIndex()] = vo;
    /*加载单据*/
    try {
      /*loadDataToCard();*/
    	getBillCardPanel().setBillValueVO(getCacheVOs()[getDispIndex()]);
    	lightRefreshUI();
    	setHeadValueByKey("vmemo", (String)getCacheVOs()[getDispIndex()].getParentVO().getAttributeValue("vmemo"));
    	  //function:查询相关费用信息
    	  String pk = (String)getCacheVOs()[getDispIndex()].getHeadVO().getAttributeValue("carriveorderid");
    		String sql = "cbillid = '"+pk+"' and dr = 0";
    		InformationCostVO[] vos = null;

    		 try {
    			vos = (InformationCostVO[])JJPuScmPubHelper.querySmartVOs(InformationCostVO.class, null, sql);
    		} catch (Exception e) {
    			// TODO Auto-generated catch block
    			SCMEnv.out(e);
    		}
    	if(vos!=null&&vos.length!=0){
     		 getBillCardPanel().getBillModel("jj_scm_informationcost").setBodyDataVO(vos);
      		 getBillCardPanel().getBillModel("jj_scm_informationcost").execLoadFormula();
      		 }else{
      			 //20101014-11:51 MeiChao 如果费用信息为空,则清除费用信息页签历史数据.
      			getBillCardPanel().getBillModel("jj_scm_informationcost").setBodyDataVO(null); 
      		 }
    } catch (Exception e) {
      showHintMessage(m_lanResTool.getStrByID("40040301","UPP40040301-000120")/*@res "审批成功,但加载单据时出现异常,请刷新界面再进行其它操作"*/);
    }
    /*刷新按钮状态*/
    setButtonsState();
    //
    showHintMessage(m_lanResTool.getStrByID("SCMCOMMON","UPPSCMCommon-000236")/*@res "审批成功"*/);
  } catch (Exception e) {
    //操作失败，恢复审批人及审批日期
    vo.getHeadVO().setCauditpsn(strPsnOld);
    vo.getHeadVO().setDauditdate(dateAuditOld);
    //
    showHintMessage(m_lanResTool.getStrByID("40040301","UPP40040301-000121")/*@res "出现异常,审批失败"*/);
    SCMEnv.out(e);
    if (e instanceof java.rmi.RemoteException || e instanceof BusinessException || e instanceof PFBusinessException || e instanceof java.lang.NullPointerException ) {
      MessageDialog.showErrorDlg(this, m_lanResTool.getStrByID("40040301","UPP40040301-000099")/*@res "报错"*/, e.getMessage());
    }
  }
}
private void lightRefreshUI() {
	BillModel bm = getBillCardPanel().getBillModel();	
//    	关闭合计开关
	boolean  bOldNeedCalc = bm.isNeedCalculate() ; 
	bm.setNeedCalculate(false) ;
	//执行加载公式    
	bm.execLoadFormula();   
	    //打开合计开关
	bm.setNeedCalculate(bOldNeedCalc) ;
	getBillCardPanel().updateValue();
	//加载下来源
	loadSourceInfo();
	
	getBillCardPanel().updateUI();
}


/**
 * @功能：到货区退货-采购
 */
private void onBackPo() {
  /*设置查询条件框*/
  getBackRefUIPo().setQueyDlg(getBackQueDlgPo());
  /*调用onQuery(),并加载数据到参照界面*/
  int iType = getBackRefUIPo().onQuery();
  if (iType == UIDialog.ID_CANCEL || iType == UIDialog.ID_NO) {
    return;
  }
  /*显示参照界面*/
  iType = getBackRefUIPo().showModal();
  if (iType == UIDialog.ID_CANCEL || iType == UIDialog.ID_NO){
    return;
  }
  /*处理返回*/
  if (getBackRefUIPo().getRetVos() == null || getBackRefUIPo().getRetVos().length <= 0)
    return;
  onExitFrmOrd((ArriveorderVO[]) getBackRefUIPo().getRetVos());
}

/**
 * @功能：到货区退货-委外
 */
private void onBackSc() {
  /*设置查询条件框*/
  getBackRefUISc().setQueyDlg(getBackQueDlgSc());
  /*调用onQuery(),并加载数据到参照界面*/
  int iType = getBackRefUISc().onQuery();
  if (iType == UIDialog.ID_CANCEL || iType == UIDialog.ID_NO) {
    return;
  }
  /*显示参照界面*/
  iType = getBackRefUISc().showModal();
  if (iType == UIDialog.ID_CANCEL || iType == UIDialog.ID_NO){
    return;
  }
  /*处理返回*/
  if (getBackRefUISc().getRetVos() == null || getBackRefUISc().getRetVos().length <= 0)
    return;
  onExitFrmOrd((ArriveorderVO[]) getBackRefUISc().getRetVos());
}

/**
 * @功能：选取一个业务类型后处理
 */
private void onBusi(ButtonObject bo) {
  showHintMessage(m_lanResTool.getStrByID("40040301","UPP40040301-000122")/*@res "正在初始化业务类型:"*/ + bo.getHint() + "...");
  /*重新加载业务类型按钮组*/
  PfUtilClient.retAddBtn(
    m_btnAdds,
    getCorpPrimaryKey(),
    nc.vo.scm.pu.BillTypeConst.PO_ARRIVE,
    bo);
 // setButtons(m_aryArrCardButtons);
  setButtons(m_btnTree.getButtonArray());//add by QuSida 2010-8-31 (佛山骏杰) 因为setButtons(m_aryArrCardButtons)中的m_aryArrCardButtons不包含二次开发的按钮,用m_btnTree.getButtonArray()代替
  updateButton(boInfoCost);
  m_btnAdds.setEnabled(true);
  updateButton(m_btnAdds);
  /*刷新处理*/
  updateButtons();
  updateButtonsAll();
  updateUI();
  showHintMessage(m_lanResTool.getStrByID("40040301","UPP40040301-000123")/*@res "当前操作业务类型:"*/ + bo.getHint());
}


/**
 * 子类实现该方法，响应按钮事件。
 * @version (00-6-1 10:32:59)
 *
 * @param bo ButtonObject
 */
public void onButtonClicked(nc.ui.pub.ButtonObject bo) {
  //
  boolean bCardShowing = getBillCardPanel().isShowing();
  //
  if(bCardShowing){
    onButtonClickedCard(bo);
  }else{
    onButtonClickedList(bo);
  }
}
/**
 * 到货单整单检验功能实现(质量管理启用)。注：修改此方法要对应修改nc.ui.rc.check.CheckUI.onCheck()方法
 * <p>
 * <b>examples:</b>
 * <p>
 * 使用示例
 * <p>
 * <b>参数说明</b>
 * <p>
 * @author czp
 * @time 2007-3-21 下午07:20:31
 * @
 */
private void onCheck(){
  if(getCacheVOs() == null 
      || getCacheVOs().length == 0
      || getCacheVOs()[getDispIndex()] == null
      ){
    return;
  }
  nc.vo.scm.pu.Timer timer = new nc.vo.scm.pu.Timer();
  timer.start();
  Hashtable<String, UFBoolean> hStorByChk = new Hashtable<String, UFBoolean>();
  
  //质量管理启用时查询是否根据质量检验结果入库参数
  ArriveorderVO voCurr = getCacheVOs()[getDispIndex()];
  int iLen = voCurr.getBodyLen();
  String[] saBid = new String[iLen];
  for(int i=0; i<iLen; i++){
    saBid[i] = voCurr.getBodyVo()[i].getPrimaryKey();
  }
  ArrayList aryTmp = null;
  try{
    aryTmp = ArriveorderHelper.getStoreByChkArray(saBid);
    if (aryTmp != null && aryTmp.size() > 0) {
      for (int i = 0; i < iLen; i++) {
        if (aryTmp.get(i) != null)
          hStorByChk.put(saBid[i], (UFBoolean) aryTmp.get(i));
      }
    }
  }catch(Exception e){
    MessageDialog.showErrorDlg(this, m_lanResTool.getStrByID("SCMCOMMON","UPPSCMCommon-000270")/*@res "提示"*/, e.getMessage());
  }
  String strErrInfo = getCacheVOs()[getDispIndex()].judgeCanCheck(m_bQcEnabled, hStorByChk);
  if(strErrInfo != null){
    MessageDialog.showErrorDlg(this, m_lanResTool.getStrByID("SCMCOMMON","UPPSCMCommon-000270")/*@res "提示"*/, strErrInfo);
    return;
  }
  timer.addExecutePhase(">>>是否根据质量检验结果入库参数ArriveorderHelper.getStoreByChkArray()");
  //组织回写变量
  
  String carriveorder_bid = null;
  String carriveorderid = null;
  String carriveorder_bts = null;
  String carriveorderts = null;
  ArrayList aryRewriteNum = new ArrayList();
  Vector<String> vStrLineId = new Vector<String>();
  Vector<String> vStrHeadId = new Vector<String>();
  Vector<String> vStrLineTs = new Vector<String>();
  Vector<String> vStrHeadTs = new Vector<String>();
  Vector<String> vStrCreporterid = new Vector<String>();
  Vector<String> vStrDreportdate = new Vector<String>();

  int iCnt = voCurr.getBodyLen();
  for (int i = 0; i < iCnt; i++) {
    //回写用到单据ID及单据行ID
    carriveorder_bid = voCurr.getBodyVo()[i].getCarriveorder_bid();
    carriveorderid = voCurr.getBodyVo()[i].getCarriveorderid();
    carriveorder_bts = voCurr.getBodyVo()[i].getTs();
    carriveorderts = voCurr.getHeadVO().getTs();
    //组织回写数据
    UFDouble[] rewriteNum = new UFDouble[3];
    rewriteNum[0] = new UFDouble(0.0);
    rewriteNum[1] = new UFDouble(0.0);
    //累计检验数据
    rewriteNum[2] = voCurr.getBodyVo()[i].getNarrvnum();
    aryRewriteNum.add(rewriteNum);
    vStrLineId.addElement(carriveorder_bid);
    vStrLineTs.addElement(carriveorder_bts);
    vStrHeadId.addElement(carriveorderid);
    vStrHeadTs.addElement(carriveorderts);

    //从界面获取报告人ID、报告日期、报检人ID V502
    String creporterid = getBillCardPanel().getBillModel().getValueAt(i, "creporterid")==null?null:getBillCardPanel().getBillModel().getValueAt(i, "creporterid").toString();//报告人ID
    String dreportdate = getBillCardPanel().getBillModel().getValueAt(i, "dreportdate")==null?null:getBillCardPanel().getBillModel().getValueAt(i, "dreportdate").toString();//报告日期
    vStrCreporterid.addElement(creporterid);
    vStrDreportdate.addElement(dreportdate);

  }

  boolean isTsChanged = false;
  //后台调用参数
  ArrayList listPara = new ArrayList();
  /*
   * 参数说明：
   * 0-----质检是否启用
   * 1..4--生成质检单参数：ArriveorderBO_Client.crtQcBills
   * 5-----是否回写累计质检数量
   * 6..15-回写累计质检数量参数：ArriveorderBO_Client.rewriteNaccumchecknumMy
   */
  listPara.add(0,new UFBoolean(m_bQcEnabled));
  //登陆日期
  UFDate dBusinessDate = getClientEnvironment().getDate();
  //服务器时间
  UFDateTime dtServerDateTime = ClientEnvironment.getServerTime();
  //目标参数
  UFDateTime dtDateTime = new UFDateTime(dBusinessDate,new UFTime(dtServerDateTime.getTime()));
  //修正不在同一事务导致的并发错误，将此方法移到后台
  listPara.add(1,null);
  listPara.add(2,voCurr.getBodyVo());
  listPara.add(3,getOperatorId());
  listPara.add(4,dtDateTime);
  //是否回写累计质检数量
  listPara.add(5,UFBoolean.TRUE);
  //组织旧TS数组
  String[] saLineIds = new String[vStrLineId.size()];
  String[] saHeadIds = new String[vStrHeadId.size()];
  String[] saLineTss = new String[vStrLineTs.size()];
  String[] saHeadTss = new String[vStrHeadTs.size()];
  vStrLineId.copyInto(saLineIds);
  vStrHeadId.copyInto(saHeadIds);
  vStrLineTs.copyInto(saLineTss);
  vStrHeadTs.copyInto(saHeadTss);
  //
  listPara.add(6,getCorpPrimaryKey());
  listPara.add(7,UFBoolean.TRUE);//质量管理启用(不启用时本功能不可用)
  listPara.add(8,saLineIds);
  listPara.add(9,saHeadIds);
  listPara.add(10,aryRewriteNum);
  listPara.add(11,getOperatorId());
  listPara.add(12,saLineTss);
  listPara.add(13,saHeadTss);
  listPara.add(14,new UFBoolean(voCurr.isCheckOver()));//是否为重复报检(整单报检完成)
  listPara.add(15,UFBoolean.FALSE);//重构后，将两次前后台操作合并为一次，时间戳没有变化

  String[] arrStrCreporterid = new String[vStrCreporterid.size()];
  String[] arrStrSreportdate = new String[vStrDreportdate.size()];
  vStrCreporterid.copyInto(arrStrCreporterid);
  vStrDreportdate.copyInto(arrStrSreportdate);
  listPara.add(16,new UFBoolean(isTsChanged));//
  listPara.add(17,new UFBoolean(isTsChanged));//
  listPara.add(18,new UFBoolean(isTsChanged));//
  listPara.add(19,arrStrCreporterid);
  listPara.add(20,arrStrSreportdate);
  timer.addExecutePhase(">>>生成质检单前参数设置");
  try {
    //重构，修正不在同一个事务导致的并发错误
    String sRet = ArriveorderHelper.crtQcAndRewriteNum(listPara);
    //
    if (sRet != null) {
      MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON","UPPSCMCommon-000059")/*@res "错误"*/, nc.ui.ml.NCLangRes.getInstance().getStrByID("40040303","UPP40040303-000013")/*@res "本次报检失败："*/ + sRet);
      showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("common","4004COMMON000000060")/*@res "检验失败"*/);
      return;
    }
    timer.addExecutePhase(">>>生成质检及回写时间");
    //
    if (sRet == null) {
      showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("common","4004COMMON000000058")/*@res "送检成功"*/);
    }
    /*送检成功后刷新*/   
    refreshVoFieldsByKey(voCurr,voCurr.getParentVO().getPrimaryKey());
    //并且需要把累计报检数量置到当前vo上
    for (int i = 0; i < voCurr.getBodyLen(); i++) {
    	voCurr.getBodyVo()[i].setNaccumchecknum(voCurr.getBodyVo()[i].getNarrvnum());
    }
    //
    getCacheVOs()[getDispIndex()] = voCurr;
    /*加载单据*/
    try {
      /*loadDataToCard();*/
    	getBillCardPanel().setBillValueVO(getCacheVOs()[getDispIndex()]);
    	setHeadValueByKey("vmemo", (String)getCacheVOs()[getDispIndex()].getParentVO().getAttributeValue("vmemo"));
    	lightRefreshUI();
    	refreshCardData();
    } catch (Exception e) {
      SCMEnv.out(e);
      showHintMessage(m_lanResTool.getStrByID("common","4004COMMON000000060")/*@res "检验失败"*/);
    }
    timer.addExecutePhase(">>>刷新界面时间");
    /*刷新按钮状态*/
    setButtonsState();
    timer.addExecutePhase(">>>刷新按钮状态");
    //
    showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("common","4004COMMON000000058")/*@res "送检成功"*/);
  } catch (BusinessException b) {
    MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON","UPPSCMCommon-000059")/*@res "错误"*/, b.getMessage());
    showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("common","4004COMMON000000060")/*@res "检验失败"*/);
  } catch (Exception b) {
    MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON","UPPSCMCommon-000059")/*@res "错误"*/, b.getMessage());
    showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("common","4004COMMON000000060")/*@res "检验失败"*/);
  }
  timer.showAllExecutePhase("生成质检完毕");
}



/**
 * 卡片按钮事件响应。
 * <p>
 * <b>examples:</b>
 * <p>
 * 使用示例
 * <p>
 * <b>参数说明</b>
 * @param bo
 * <p>
 * @author czp
 * @time 2007-3-20 下午04:12:37
 */
private void onButtonClickedCard(ButtonObject bo){

   if (bo == m_btnReSortRowNo){
    onReSortRowNo();
  } else if (bo == m_btnSplitPrint) {
    //分单打印
    onSplitPrint();
  } else if (bo == m_btnCardEdit){
	  onCardEdit();
  }else if (bo == m_btnPstLineTail){
	    onPasteLineToTail();
  } else if (bo == m_btnCheck) {
    onCheck();
  } else if (bo.getParent() == m_btnBusiTypes) {
    //
    bo.setSelected(true);
    //
    isChangeBusitype = true;
    onBusi(bo);
  } else if (bo == m_btnDiscard){
    onDiscard();
  } else if (bo.getParent() == m_btnAdds) {
    //onAdd();
    int iIndexBillType = bo.getTag().indexOf(":");
    String strBillType = bo.getTag().substring(0, iIndexBillType);
    if (strBillType.equals(ScmConst.SC_Order)) {
      if (!nc.ui.sm.user.UserPowerUI.isEnabled(getCorpPrimaryKey(), "SC")) {
        MessageDialog.showErrorDlg(this, m_lanResTool.getStrByID("SCMCOMMON","UPPSCMCommon-000059")/*@res "错误"*/, m_lanResTool.getStrByID("40040301","UPP40040301-000124")/*@res "委外订单模块没有启用！"*/);
        return;
      }
    } else if (!strBillType.equals("21")) {
      MessageDialog.showErrorDlg(this, m_lanResTool.getStrByID("SCMCOMMON","UPPSCMCommon-000059")/*@res "错误"*/, m_lanResTool.getStrByID("40040301","UPP40040301-000125")/*@res "到货单只能由采购订单或委外订单生成！"*/);
      return;
    }
//    PfUtilClient.childButtonClicked(bo, getCorpPrimaryKey(), getFuncId(), getOperatorId(), ScmConst.PO_Arrive, this);
//    if (PfUtilClient.isCloseOK()) {
//      ArriveorderVO[] retVOs = (ArriveorderVO[]) PfUtilClient.getRetVos();
//      onExitFrmOrd(retVOs);
//    }
    //n502修改 支持新转单格式
   SourceRefDlg.childButtonClicked(bo, getCorpPrimaryKey(), getFuncId(), getOperatorId(), ScmConst.PO_Arrive, this);
    if (SourceRefDlg.isCloseOK()) {
    	 Object[] o1 = SourceRefDlg.getRetSrcVos();
    	 Object o2 = SourceRefDlg.getRetSrcVo();
      ArriveorderVO[] retVOs = (ArriveorderVO[]) SourceRefDlg.getRetsVos();
     
      
      //add by QuSida 2010-9-2 (佛山骏杰) --- begin
      //function： 根据到货单VO查询出单据对应的费用信息
      ArrayList<String> pkList = new ArrayList<String>();
      //用List存储来源单据的主键
      for(int i = 0; i < retVOs.length ; i++){
    	  ArriveorderItemVO[] items = retVOs[i].getBodyVo();
    	  for (int j = 0; j < items.length; j++) {
			String srcPK = items[i].m_cupsourcebillid;
			//排除因多个存货来自同一单据而导致List中存储多个重复单据主键的可能
			if(pkList.size() == 0 || !pkList.contains(srcPK)){
				pkList.add(srcPK);
			}
		}
      }
      //查询的where条件
      StringBuffer whereSql = new StringBuffer("11 = 11");
      if(pkList.size()>0&&pkList!=null){
    	  whereSql.append(" and");
      }
      for (int i = 0; i < pkList.size(); i++) {
    	  if(i == pkList.size()-1){
    		  whereSql.append(" cbillid = '"+pkList.get(i)+"'");
    	  }
    	  else {
    		  whereSql.append(" cbillid = '"+pkList.get(i)+"' or");
    	  }
  	}      
      String sql = "select nvl(sum(naccumarrvnum),0) from po_order_b where dr = 0 and corderid = '"+pkList.get(0)+"'";
      Object o = null;
           try {
        	    IUAPQueryBS query1 = NCLocator.getInstance().lookup(IUAPQueryBS.class);
                o = query1.executeQuery(sql, new ColumnProcessor());        		  		
               
              //第二个参数为null表示查询全部字段,将查询结果存储在内存中
              vos =  (InformationCostVO[]) JJPuScmPubHelper.querySmartVOs(InformationCostVO.class, null, whereSql.toString());
        	   
		} catch (Exception e) {
			// TODO Auto-generated catch block
			SCMEnv.out(e);
			return;
		}
		 arrnum = new UFDouble(o.toString());
	 //add by QuSida 2010-9-2 (佛山骏杰) --- end
      onExitFrmOrd(retVOs);
		CircularlyAccessibleValueObject[] a = retVOs[0].getChildrenVO();
	  // int temp = getBillCardPanel().getBillModel("table").getRowCount();
	   arrnumber = new UFDouble(0.0);
				for (int i = 0; i < a.length; i++) {
					arrnumber = arrnumber.add(new UFDouble(a[i].getAttributeValue("narrvnum").toString()));
				}

      for (int i = 0; i < vos.length; i++) {
//		vos[i].setAttributeValue("ninvoriginalcursummny", vos[i].getNoriginalcursummny());
		vos[i].setAttributeValue("ninvoriginalcurmny", vos[i].getNoriginalcurmny().add(arrnum.multiply(vos[i].getNoriginalcurprice())));
	}

      getBillListPanel().getBodyBillModel("jj_scm_informationcost").setBodyDataVO(vos); //add by QuSida 2010-9-2 (佛山骏杰) 将查询出来的费用信息写到界面上
      getBillListPanel().getBodyBillModel("jj_scm_informationcost").execLoadFormula();
    }
  } else if (bo == m_btnBackPo) {
    onBackPo();
  } else if (bo == m_btnBackSc) {
    onBackSc();
  } else if (bo == m_btnLocate) {
    onLocate();
  } else if (bo == m_btnPrint) {
    //打印并计算打印次数
    onCardPrint();
  } else if (bo == m_btnCombin) {
    //合并显示打印
    onCombin();
  } else if (bo == m_btnPrintPreview) {
    //打印预览并计算打印次数
    onCardPrintPreview();
  } else if (bo == m_btnList){
    onList();
  } else if (bo == m_btnModify){
    onModify();
    //置光标到表头第一个可编辑项目
    getBillCardPanel().transferFocusTo(BillCardPanel.HEAD);
  } else if (bo == m_btnDelLine){
    onDeleteLine();
  } else if (bo == m_btnCpyLine){
    onCopyLine();
  } else if (bo == m_btnPstLine){
    onPasteLine();
  } else if (bo == m_btnSave){
    onSave();
  } else if (bo == m_btnCancel){
    onCancel();
  } else if (bo == m_btnQuery){
    onQuery();
  } else if (bo == m_btnFirst){
    onFirst();
  } else if (bo == m_btnPrev){
    onPrevious();
  } else if (bo == m_btnNext){
    onNext();
  } else if (bo == m_btnLast){
    onLast();
  } else if (bo == m_btnRefresh){
    onRefresh();
  }
  /*以下V5支持审批流*******************************/
  else if(bo == m_btnSendAudit){
    onSendAudit();
  } else if(bo == m_btnAudit){
    onAudit(bo);
  } else if(bo == m_btnUnAudit){
    onUnAudit(bo);
  } else if(bo == m_btnQueryForAudit){
    onQueryForAudit();
  /*以上V5支持审批流******************************/
  } else if (bo == m_btnUsable) {
    onQueryInvOnHand();
  } else if (bo == m_btnQueryBOM) {
    onQueryBOM();
  } else if (bo == m_btnDocument){
    onDocument();
  } else if (bo == m_btnLookSrcBill){
    onLnkQuery();
  } else if(bo == m_btnQuickReceive){
    onQuickArr();
  } else if (bo == m_btnCreateCard){
	  onCreateCard();
  } else if (bo == m_btnDeleteCard){
	  onDeleteCard();
//  } else if (bo == m_btnSerialNO){
//	  onSNAssign();
  }
  //支持产业链功能扩展
  else{
    onExtendBtnsClick(bo);
  }
}

/**
 * 列表按钮事件响应。
 * <p>
 * <b>examples:</b>
 * <p>
 * 使用示例
 * <p>
 * <b>参数说明</b>
 * @param bo
 * <p>
 * @author czp
 * @time 2007-3-20 下午04:13:02
 */
private void onButtonClickedList(ButtonObject bo){

  if (bo == m_btnPrintList) {
    //批打印
    onBatchPrint();
  } else if (bo == m_btnSplitPrint) {
    //分单打印
    onSplitPrint();
  } else if (bo == m_btnPrintPreviewList) {
    //批打印预览
    onBatchPrintPreview();
  } else if (bo == m_btnDiscardList){
    onDiscardSelected();
  } else if (bo == m_btnCard) {
    onCard();
  } else if (bo == m_btnModifyList) {
    loadBatchDocInfo(true);
    if (getStateStr().equals("转入列表")) {
      onCardNew();
      if(vos!=null&&vos.length!=0){
    	  getBillCardPanel().getBillData().setBodyValueVO("jj_scm_informationcost", vos);//add by QuSida 2010-9-2 (佛山骏杰) 将查询出来的费用信息写到界面上
          getBillCardPanel().getBillModel("jj_scm_informationcost").execLoadFormula();
      }else{
			 //20101014-11:51 MeiChao 如果费用信息为空,则清除费用信息页签历史数据.
			getBillCardPanel().getBillModel("jj_scm_informationcost").setBodyDataVO(null); 
		 }
    } else {
      onModifyList();
      if(vos!=null&&vos.length!=0){
    	  getBillCardPanel().getBillData().setBodyValueVO("jj_scm_informationcost", vos);//add by QuSida 2010-9-2 (佛山骏杰) 将查询出来的费用信息写到界面上

      }else{
			 //20101014-11:51 MeiChao 如果费用信息为空,则清除费用信息页签历史数据.
			getBillCardPanel().getBillModel("jj_scm_informationcost").setBodyDataVO(null); 
		 }
    }
    //置光标到表头第一个可编辑项目
    getBillCardPanel().transferFocusTo(BillCardPanel.HEAD);
  } else if (bo == m_btnEndCreate) {
    onEndCreate();
  } else if (bo == m_btnQueryList){
    onQuery();
  } else if (bo == m_btnSelectAll){
    onSelectAll();
  } else if (bo == m_btnSelectNo){
    onSelectNo();
  } else if (bo == m_btnRefreshList){
    onRefresh();
  } else if (bo == m_btnUsableList) {
    onQueryInvOnHand();
  } else if (bo == m_btnQueryBOMList) {
    onQueryBOM();
  } else if (bo == m_btnDocumentList){
    onDocument();
  } else if(bo == m_btnAudit){
    onAuditList(bo);
  } else if(bo == m_btnUnAudit){
    onUnAuditList(bo);
  } else{
    onExtendBtnsClick(bo);
  }
}
/**
 * 批量审批功能。
 * <p>
 * <b>examples:</b>
 * <p>
 * 使用示例
 * <p>
 * <b>参数说明</b>
 * <p>
 * @author czp
 * @time 2007-3-27 下午01:17:02
 */
private void onAuditList(ButtonObject bo) {

  Vector<ArriveorderVO> vSubVos = new Vector<ArriveorderVO>();
  ArriveorderHeaderVO headVO = new ArriveorderHeaderVO();
  
  //回退审批人及审批日期哈希表，操作失败时用到
  HashMap<String, ArrayList<Object>> mapAuditInfo = new HashMap<String, ArrayList<Object>>();
  ArrayList<Object> listAuditInfo = null;
  
  //处理排过序后的缓存索引
  int iRealPos = 0;
  for (int i = 0;
    i < getBillListPanel().getHeadBillModel().getRowCount();
    i++) {
    if (getBillListPanel().getHeadBillModel().getRowState(i) == BillModel.SELECTED) {
      iRealPos = i;
      iRealPos =
      nc.ui.pu.pub.PuTool.getIndexBeforeSort(getBillListPanel(), iRealPos);
      //审批人、审批日期
      headVO = (ArriveorderHeaderVO) getCacheVOs()[iRealPos].getParentVO();
      //操作失败时用到，回退审批人及审批日期哈希表
      if(PuPubVO.getString_TrimZeroLenAsNull(headVO.getCauditpsn()) != null){
        listAuditInfo = new ArrayList<Object>();
        listAuditInfo.add(headVO.getCauditpsn());
        listAuditInfo.add(headVO.getDauditdate());
        mapAuditInfo.put(headVO.getPrimaryKey(),listAuditInfo);
      }
      //审批人是操作员
      headVO.setCauditpsn(getOperatorId());
      headVO.setDauditdate(PoPublicUIClass.getLoginDate());
      //
      vSubVos.add(getCacheVOs()[iRealPos]);
    }
  }
  ArriveorderVO[] arrivevos = null;
  if (vSubVos.size() > 0) {
    arrivevos = new ArriveorderVO[vSubVos.size()];
    vSubVos.copyInto(arrivevos);
  }
  try {
    //设置操作员
    for (int i = 0; i < arrivevos.length; i++) {
      arrivevos[i].getParentVO().setAttributeValue("cuserid", getOperatorId());
    }
    boolean isSucc = false;
//    //V31SP1:增加审批日期不能小于到货日期限制 V5.02 重构
//    String strErr = PuTool.getAuditLessThanMakeMsg(arrivevos,"dreceivedate","varrordercode", ClientEnvironment.getInstance().getDate(),ScmConst.PO_Arrive);
//    if (strErr != null && strErr.trim().length() > 0) {
//      throw new BusinessException(strErr);
//    }
    //审批前处理表体
    ArriveorderHeaderVO[] heads = new ArriveorderHeaderVO[arrivevos.length];
    for (int i = 0; i < arrivevos.length; i++) {
      heads[i] = (ArriveorderHeaderVO) arrivevos[i].getParentVO();
    }
    //加载未浏览过的单据体
    //arrivevos = ArriveorderBO_Client.getAllWithBody(heads);
    arrivevos = RcTool.getRefreshedVOs(arrivevos);
    //

    PfUtilClient.processBatchFlow(
        this,
        "APPROVE",
        ScmConst.PO_Arrive,
        ClientEnvironment.getInstance().getDate().toString(),
        arrivevos,
        null);

    isSucc = PfUtilClient.isSuccess();
    if (isSucc) {
      //刷新前端显示内容
      //displayOthersVOs(vSubVos);
      onRefresh();
/*      *//************记录业务日志*************//*
      if(arrivevos!=null && arrivevos.length > 0){
    	  Operlog operlog=new Operlog();
    	  for (ArriveorderVO arriveorderVO : arrivevos) {
    		  arriveorderVO.getOperatelogVO().setOpratorname(nc.ui.pub.ClientEnvironment.getInstance().getUser().getUserName());
    		  arriveorderVO.getOperatelogVO().setCompanyname(nc.ui.pub.ClientEnvironment.getInstance().getCorporation().getUnitname());
    		  arriveorderVO.getOperatelogVO().setOperatorId(nc.ui.pub.ClientEnvironment.getInstance().getUser().getPrimaryKey());
    		  operlog.insertBusinessExceptionlog(arriveorderVO, "审批", "审批", nc.vo.scm.funcs.Businesslog.MSGMESSAGE, nc.vo.scm.pu.BillTypeConst.PO_ARRIVE,
						nc.ui.sm.cmenu.Desktop.getApplet().getParameter("USER_IP"));
    	  }
      }
      *//************记录业务日志* end ************/
      showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("common","4004COMMON000000071")/*@res "审核成功"*/);
    } else {
      //回退审批人及审批日期
      if(mapAuditInfo.size() > 0){
        for (int i = 0; i < arrivevos.length; i++) {
          headVO = arrivevos[i].getHeadVO();
          if(!mapAuditInfo.containsKey(headVO.getPrimaryKey())){
            headVO.setCauditpsn(null);
            headVO.setDauditdate(null);
            continue;
          }          
          listAuditInfo = (ArrayList<Object>)mapAuditInfo.get(headVO.getPrimaryKey());
          headVO.setCauditpsn((String)listAuditInfo.get(0));
          headVO.setDauditdate((UFDate)listAuditInfo.get(1));
        }
      }
      showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("common","4004COMMON000000072")/*@res "审核失败"*/);
    }
  } catch (nc.vo.pub.BusinessException e) {
    //回退审批人及审批日期
    if(mapAuditInfo.size() > 0){
      for (int i = 0; i < arrivevos.length; i++) {
        headVO = arrivevos[i].getHeadVO();
        if(!mapAuditInfo.containsKey(headVO.getPrimaryKey())){
          headVO.setCauditpsn(null);
          headVO.setDauditdate(null);
          continue;
        }          
        listAuditInfo = (ArrayList<Object>)mapAuditInfo.get(headVO.getPrimaryKey());
        headVO.setCauditpsn((String)listAuditInfo.get(0));
        headVO.setDauditdate((UFDate)listAuditInfo.get(1));
      }
    }
    reportException(e);
    showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("common","4004COMMON000000072")/*@res "审核失败"*/);
    MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON","UPPSCMCommon-000422")/*@res "业务异常"*/, e.getMessage());
  } catch (Exception e) {
    //回退审批人及审批日期
    if(mapAuditInfo.size() > 0){
      for (int i = 0; i < arrivevos.length; i++) {
        headVO = arrivevos[i].getHeadVO();
        if(!mapAuditInfo.containsKey(headVO.getPrimaryKey())){
          headVO.setCauditpsn(null);
          headVO.setDauditdate(null);
          continue;
        }          
        listAuditInfo = (ArrayList<Object>)mapAuditInfo.get(headVO.getPrimaryKey());
        headVO.setCauditpsn((String)listAuditInfo.get(0));
        headVO.setDauditdate((UFDate)listAuditInfo.get(1));
      }
    }
    reportException(e);
    showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("common","4004COMMON000000072")/*@res "审核失败"*/);
    if (e instanceof java.rmi.RemoteException){
      MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON","UPPSCMCommon-000422")/*@res "业务异常"*/, e.getMessage());
    }
  }
}
/**
 * 批量弃审。
 * <p>
 * <b>examples:</b>
 * <p>
 * 使用示例
 * <p>
 * <b>参数说明</b>
 * <p>
 * @author czp
 * @time 2007-3-27 下午01:30:13
 */
private void onUnAuditList(ButtonObject bo) {

  Vector<ArriveorderVO> vSubVos = new Vector<ArriveorderVO>();
  int rowCount = getBillListPanel().getHeadBillModel().getRowCount();
  BillModel bm = getBillListPanel().getHeadBillModel();
  ArriveorderVO vo = null;
  ArriveorderVO[] arrivevos = null;
  
  //回退审批人及审批日期哈希表，操作失败时用到
  HashMap<String, ArrayList<Object>> mapAuditInfo = new HashMap<String, ArrayList<Object>>();
  ArrayList<Object> listAuditInfo = null;
  
  //处理排过序后的缓存索引
  int iRealPos = 0;
  for (int i = 0; i < rowCount; i++) {
    if (bm.getRowState(i) == BillModel.SELECTED) {
      iRealPos = i;
      iRealPos =
        nc.ui.pu.pub.PuTool.getIndexBeforeSort(getBillListPanel(), iRealPos);
      vo = getCacheVOs()[iRealPos];
      vSubVos.add(vo);
    }
  }
  ArriveorderHeaderVO headVO = null;
  if (vSubVos.size() > 0) {
    arrivevos = new ArriveorderVO[vSubVos.size()];
    vSubVos.copyInto(arrivevos);
    try {
      //设置操作员
      for (int i = 0; i < arrivevos.length; i++) {
        //操作失败时用到，回退审批人及审批日期哈希表
        headVO = arrivevos[i].getHeadVO();
        if(PuPubVO.getString_TrimZeroLenAsNull(headVO.getCauditpsn()) != null){
          listAuditInfo = new ArrayList<Object>();
          listAuditInfo.add(headVO.getCauditpsn());
          listAuditInfo.add(headVO.getDauditdate());
          mapAuditInfo.put(headVO.getPrimaryKey(),listAuditInfo);
        }
        arrivevos[i].getParentVO().setAttributeValue("cuserid", getOperatorId());
      }
      //弃审前处理表体
      ArriveorderHeaderVO[] heads = new ArriveorderHeaderVO[arrivevos.length];
      for (int i = 0; i < arrivevos.length; i++) {
        heads[i] = (ArriveorderHeaderVO) arrivevos[i].getParentVO();
      }
      //加载未浏览过的单据体
      arrivevos = RcTool.getRefreshedVOs(arrivevos);
      //
      boolean isSucess = false;

      PfUtilClient.processBatch(
          this,
          "UNAPPROVE",
          ScmConst.PO_Arrive,
          ClientEnvironment.getInstance().getDate().toString(),
          arrivevos,
          null);

      isSucess = PfUtilClient.isSuccess();
      if (isSucess) {
        //刷新前端显示内容
        //displayOthersVOs(vSubVos);
    	  /************记录业务日志*************/
    	  if(arrivevos!=null && arrivevos.length > 0){
    		  Operlog operlog=new Operlog();
    		  for (ArriveorderVO arriveorderVO : arrivevos) {
    			  arriveorderVO.getOperatelogVO().setOpratorname(nc.ui.pub.ClientEnvironment.getInstance().getUser().getUserName());
    			  arriveorderVO.getOperatelogVO().setCompanyname(nc.ui.pub.ClientEnvironment.getInstance().getCorporation().getUnitname());
    			  arriveorderVO.getOperatelogVO().setOperatorId(nc.ui.pub.ClientEnvironment.getInstance().getUser().getPrimaryKey());
    			  operlog.insertBusinessExceptionlog(arriveorderVO, "审批", "审批", nc.vo.scm.funcs.Businesslog.MSGMESSAGE, nc.vo.scm.pu.BillTypeConst.PO_ARRIVE,
  						nc.ui.sm.cmenu.Desktop.getApplet().getParameter("USER_IP"));
    		  }
    	  }
          /************记录业务日志* end ************/
    	  onRefresh();
    	  setButtonsRevise();

        showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON","UPPSCMCommon-000184")/*@res "弃审成功"*/);
      } else {
        //回退审批人及审批日期
        if(mapAuditInfo.size() > 0){
          for (int i = 0; i < arrivevos.length; i++) {
            headVO = arrivevos[i].getHeadVO();
            if(!mapAuditInfo.containsKey(headVO.getPrimaryKey())){
              headVO.setCauditpsn(null);
              headVO.setDauditdate(null);
              continue;
            }          
            listAuditInfo = (ArrayList<Object>)mapAuditInfo.get(headVO.getPrimaryKey());
            headVO.setCauditpsn((String)listAuditInfo.get(0));
            headVO.setDauditdate((UFDate)listAuditInfo.get(1));
          }
        }
        showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON","UPPSCMCommon-000185")/*@res "弃审失败"*/);
      }
    } catch (nc.vo.pub.BusinessException e) {
      //回退审批人及审批日期
      if(mapAuditInfo.size() > 0){
        for (int i = 0; i < arrivevos.length; i++) {
          headVO = arrivevos[i].getHeadVO();
          if(!mapAuditInfo.containsKey(headVO.getPrimaryKey())){
            headVO.setCauditpsn(null);
            headVO.setDauditdate(null);
            continue;
          }          
          listAuditInfo = (ArrayList<Object>)mapAuditInfo.get(headVO.getPrimaryKey());
          headVO.setCauditpsn((String)listAuditInfo.get(0));
          headVO.setDauditdate((UFDate)listAuditInfo.get(1));
        }
      }
      reportException(e);
      showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON","UPPSCMCommon-000185")/*@res "弃审失败"*/);
      MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON","UPPSCMCommon-000422")/*@res "业务异常"*/, e.getMessage());
    } catch (Exception ex) {
      //回退审批人及审批日期
      if(mapAuditInfo.size() > 0){
        for (int i = 0; i < arrivevos.length; i++) {
          headVO = arrivevos[i].getHeadVO();
          if(!mapAuditInfo.containsKey(headVO.getPrimaryKey())){
            headVO.setCauditpsn(null);
            headVO.setDauditdate(null);
            continue;
          }          
          listAuditInfo = (ArrayList<Object>)mapAuditInfo.get(headVO.getPrimaryKey());
          headVO.setCauditpsn((String)listAuditInfo.get(0));
          headVO.setDauditdate((UFDate)listAuditInfo.get(1));
        }
      }
      reportException(ex);
      showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON","UPPSCMCommon-000185")/*@res "弃审失败"*/);
      if (ex instanceof java.rmi.RemoteException){
        MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON","UPPSCMCommon-000422")/*@res "业务异常"*/, nc.ui.ml.NCLangRes.getInstance().getStrByID("40040302","UPP40040302-000003")/*@res "调用流程配置批弃审时出错:"*/ + ex.getMessage());
      }
    }
  }
}
/**
 * 显示操作完成后的单据。
 * <p>
 * <b>examples:</b>
 * <p>
 * 使用示例
 * <p>
 * <b>参数说明</b>
 * @param subVOs
 * <p>
 * @author czp
 * @time 2007-3-27 下午01:21:47
 */
private void displayOthersVOs(Vector<ArriveorderVO> subVOs) {
  Vector<ArriveorderVO> allVOs = new Vector<ArriveorderVO>();
  Vector<ArriveorderVO> newVOs = new Vector<ArriveorderVO>();
  ArriveorderVO[] arrvos = null;
  for (int i = 0; i < getCacheVOs().length; i++) {
    allVOs.addElement(getCacheVOs()[i]);
  }
  for (int i = 0; i < allVOs.size(); i++) {
    if (!subVOs.contains(allVOs.elementAt(i))) {
      newVOs.addElement(allVOs.elementAt(i));
    }
  }
  if (newVOs.size() > 0) {
    arrvos = new ArriveorderVO[newVOs.size()];
    newVOs.copyInto(arrvos);
    setCacheVOs(arrvos);
  } else {
    setCacheVOs(null);
  }
  //显示数据、处理按钮状态
  loadDataToList();
  //默认显示第一张
  if (getCacheVOs() != null && getCacheVOs().length > 0) {
    onSelectNo();
    getBillListPanel().getHeadTable().setRowSelectionInterval(0, 0);
    getBillListPanel().getHeadBillModel().setRowState(0, BillModel.SELECTED);
    //设置当前位置
    setDispIndex(0);
  }
  //刷新按钮逻辑
  setButtonsState();
}
/**
 * 功能:放弃本次修改(包括浏览和转单两种情况的处理)
 */
public void onCancel() {
  if (getStateStr().equals("转入修改")) {
    delArriveorderVOSaved();
    if (getCacheVOs() != null) {
      displayArrBillListPanelNew();
      showHintMessage(m_lanResTool.getStrByID("40040301","UPP40040301-000126")/*@res "放弃上张单据,继续转单"*/);
    } else {
      onEndCreate();
    }
    return;
  }
  onCard();
  showHintMessage(m_lanResTool.getStrByID("common","UCH008")/*@res "取消成功"*/);
}

/**
 * @功能：到货单列表界面“切换”按钮事件,切换到“到货浏览”“转入修改”状态
 * @作者：晁志平
 * 创建日期：(2001-6-20 8:10:39)
 * @param:<|>
 * @return:
 * @exception:
 * @see;
 * @since:
 *
 */
private void onCard() { 
  
  showHintMessage(m_lanResTool.getStrByID("40040301","UPP40040301-000128")/*@res "正在加载数据..."*/);
    
  //排序索引
  int index = getBillListPanel().getBodyBillModel().getSortColumn();
  boolean bSortAsc = getBillListPanel().getBodyBillModel().isSortAscending();
  
  //排序改变顺序的同步
//  int iPos = getBillListPanel().getHeadTable().getSelectedRow();
//  setDispIndex(iPos);
  /*转入列表*/
  if (getStateStr().equals("转入列表")) {
    onCardNew();
    if(index >= 0){
      getBillCardPanel().getBillModel().sortByColumn(index,bSortAsc);
    }
    return;
  }
  /*非转入列表*/
  setM_strState("到货浏览");
  setButtonsState();
  /*
  if (m_arrListPanel != null) {
    remove(getBillListPanel());
  }
  setLayout(new java.awt.BorderLayout());
  add(getBillCardPanel(), java.awt.BorderLayout.CENTER);
  */
  getBillListPanel().setVisible(false);
  getBillCardPanel().setVisible(true);
  getBillCardPanel().setEnabled(false);
  //
  try {
    loadDataToCard();
    setButtonsState();
  } catch (Exception e) {
    SCMEnv.out("加载单据时报错");
    showHintMessage(m_lanResTool.getStrByID("40040301","UPP40040301-000129")/*@res "加载数据失败"*/);
  }
  if(index >= 0){
    getBillCardPanel().getBillModel().sortByColumn(index,bSortAsc);
  }
  updateUI();
  //add by QuSida 2010-9-11 (佛山骏杰) --- begin
  //function:查询出相关的费用信息
	String pk =	getBillCardPanel().getHeadItem("carriveorderid").getValue();
	String sql = "cbillid = '"+pk+"' and dr = 0";
	InformationCostVO[] vos = null;
try {
	 vos = (InformationCostVO[])JJPuScmPubHelper.querySmartVOs(InformationCostVO.class, null, sql);
} catch (Exception e) {
	// TODO Auto-generated catch block
	e.printStackTrace();
}
if(vos!=null&&vos.length!=0){
	getBillCardPanel().getBillModel("jj_scm_informationcost").setBodyDataVO(vos);
	getBillCardPanel().getBillModel("jj_scm_informationcost").execLoadFormula();
}else{
		 //20101014-11:51 MeiChao 如果费用信息为空,则清除费用信息页签历史数据.
		getBillCardPanel().getBillModel("jj_scm_informationcost").setBodyDataVO(null); 
	 }
//add by QuSida 2010-9-11 (佛山骏杰) --- end
  showHintMessage(m_lanResTool.getStrByID("common","UCH021")/*@res "卡片显示"*/);
}

/**
 * @功能：转入到货单修改
 */
private void onCardNew() {
  showHintMessage(m_lanResTool.getStrByID("40040301","UPP40040301-000131")/*@res "正在加载单据..."*/);
  isFrmList = true;
  setM_strState("转入修改");
  getBillListPanel().setVisible(false);
  getBillCardPanel().setVisible(true);
  getBillCardPanel().setEnabled(true);
  if(!m_bLoadedCard){
    setLayout(new java.awt.BorderLayout());
    add(getBillCardPanel(), java.awt.BorderLayout.CENTER);
    m_bLoadedCard = true;
  }
  setButtonsState();

  //过滤存货参照,保证在业务类型是受托代销业务类型时替换件参照是受托代销属性
    //取业务类型
    String cBizType = getBillCardPanel().getHeadItem("cbiztype").getValue();

    UFBoolean checker = new UFBoolean(false);
    try {
    loadDataToCard();
    //退货理由(头体)
    setBackReasonEditable();
    //来源委外赠品行不允许编辑
    restrictSCBlargess();
    //批量加载单据行号
    BillRowNo.addNewRowNo(getBillCardPanel(), nc.vo.scm.pu.BillTypeConst.PO_ARRIVE, "crowno");

    checker = CheckISSellProxyHelper.CheckIsSellProxyType(cBizType);
        //过滤存货参照
        if(checker.booleanValue()){
          String sql = " and (sellproxyflag = 'Y')";
          UIRefPane refCinventorycode = (UIRefPane) (getBillCardPanel().getBodyItem("cinventorycode").getComponent());
          refCinventorycode.getRefModel().addWherePart(sql);
        }
  } catch (Exception e) {
    SCMEnv.out("加载单据时报错");
    showHintMessage(m_lanResTool.getStrByID("40040301","UPP40040301-000132")/*@res "加载单据失败"*/);
  }
  /**设置库存组织与仓库匹配*/
  setOrgWarhouse();
  
  //根据操作员设置采购员及采购部门
  String strUserId = getClientEnvironment().getUser().getPrimaryKey();
  if(PuPubVO.getString_TrimZeroLenAsNull(getBillCardPanel().getHeadItem("cemployeeid").getValueObject()) == null){
    IUserManageQuery iSrvUser = (IUserManageQuery) NCLocator.getInstance().lookup(IUserManageQuery.class.getName());
    PsndocVO voPsnDoc = null;
    try{
      voPsnDoc = iSrvUser.getPsndocByUserid(getCorpPrimaryKey(), strUserId);
    }catch(BusinessException be){
      SCMEnv.out(be);
    }
    if(voPsnDoc != null){
      UIRefPane refPanePrayPsn = (UIRefPane) getBillCardPanel().getHeadItem("cemployeeid").getComponent();
      refPanePrayPsn.setPK(voPsnDoc.getPrimaryKey());
      //由采购员带出采购部门(如果采购部门无值时才带出)
      if(PuPubVO.getString_TrimZeroLenAsNull(getBillCardPanel().getHeadItem("cdeptid").getValueObject()) == null){
        afterEditWhenHeadEmployee(null);
      }
    }
  }
  
  //打印次数不能修改
  if (getBillCardPanel().getTailItem("iprintcount") != null)
    getBillCardPanel().getTailItem("iprintcount").setEnabled(false);    
  //累计入库数量应不可编辑
  getBillCardPanel().getBodyItem("naccumwarehousenum").setEdit(false);  
  updateUI();

  //浮动菜单右键功能权限控制
//  rightButtonRightControl();
  updateButtons();

  showHintMessage(m_lanResTool.getStrByID("common","4004COMMON000000030")/*@res "正在修改"*/);
}

/**
 * 关闭窗口的客户端接口。可在本方法内完成窗口关闭前的工作。
 * @return boolean 返回值为true表示允许窗口关闭，返回值为false表示不允许窗口关闭。
 *
 * 创建日期：(2001-8-8 13:52:37)
 */
public boolean onClosing() {
  if (getStateStr().equals("到货修改") || getStateStr().equals("转入修改")) {
    int iRet = MessageDialog.showYesNoCancelDlg(this, 
        m_lanResTool.getStrByID("SCMCOMMON", "UPPSCMCommon-000270")/* "提示"*/, 
        m_lanResTool.getStrByID("common","UCH001")/*@res "是否保存已修改的数据？"*/);
    //保存成功后才退出
    if (iRet == MessageDialog.ID_YES) {
      return onSave();
    }
    //退出
    else if(iRet == MessageDialog.ID_NO) {
      return true;
    }
    //取消关闭
    else{
      return false;
    }
  }
  return true;
}

/**
 * 功能描述:行拷贝
 */
private void onCopyLine() {
	if (getCacheVOs()[getDispIndex()].isHaveCheckLine()) {
		setPopMenuBtnsEnable(false);
		return;
	}

  if (getBillCardPanel().getBodyPanel().getTable().getSelectedRows() != null
    && getBillCardPanel().getBodyPanel().getTable().getSelectedRows().length > 0) {
    getBillCardPanel().copyLine();
  }
  showHintMessage(m_lanResTool.getStrByID("common","UCH039")/*@res "复制行成功"*/);
}

/**
 * 功能描述:删行
 */
private void onDeleteLine() {
  if (getCacheVOs()[getDispIndex()].isHaveCheckLine()) {
  	setPopMenuBtnsEnable(false);
  	return;
  }

  if (getBillCardPanel().getBodyPanel().getTable().getSelectedRows() == null
    || getBillCardPanel().getBodyPanel().getTable().getSelectedRows().length
      <= 0) {
    showHintMessage(m_lanResTool.getStrByID("40040301","UPP40040301-000136")/*@res "未选取行，删除行未成功"*/);
    return;
  }
  int iSelRowCnt =
    getBillCardPanel().getBodyPanel().getTable().getSelectedRows().length;
  if (iSelRowCnt
    == getBillCardPanel().getBodyPanel().getTable().getRowCount()) {
    showHintMessage(m_lanResTool.getStrByID("40040301","UPP40040301-000137")/*@res "表体单据行至少有一行才能保存，删除行未成功！"*/);
    return;
  }
  getBillCardPanel().delLine();
  showHintMessage(m_lanResTool.getStrByID("common","UCH037")/*@res "删行成功"*/);
}

/**
 * @功能：作废单据 流程方法 deleteMy() + rewriteOnDiscardMy()
 * 此作废功能已经在向卡片写表体时得到限制
 * @作者：晁志平
 * 创建日期：(2001-6-20 10:40:17)
 * @param:<|>
 * @return:
 * @exception:
 * @see;
 * @since:
 *
 */
private void onDiscard() {
    int iRet = 
      MessageDialog.showYesNoDlg(this,m_lanResTool.getStrByID("SCMCOMMON","UPPSCMCommon-000219")/*@res "确定"*/,nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UCH002")/*@res "是否确认要删除？"*/,UIDialog.ID_NO);
    if(iRet != UIDialog.ID_YES){
        return;
    }
  showHintMessage(m_lanResTool.getStrByID("40040301","UPP40040301-000139")/*@res "正在作废..."*/);
  ArriveorderVO arrivevo = new ArriveorderVO();
  arrivevo = getCacheVOs()[getDispIndex()];
  /*//增加是否能删除的校验
  if (!arrivevo.isCanBeModified() || arrivevo.isHaveCheckLine()) {
	  MessageDialog.showWarningDlg(
		        this,
		        m_lanResTool.getStrByID("SCMCOMMON","UPPSCMCommon-000270")@res "提示",
		        CommonConstant.BEGIN_MARK
		          + getDispIndex()
		          + CommonConstant.END_MARK
		          + m_lanResTool.getStrByID("40040301","UPP40040301-000141")@res " 行到货单已经审批或正在审批或已经报检,这张单据不会被作废");
	  return;
		    
  }*/
  
  //作废后的显示位置处理
  int IndexLast = getCacheVOs().length - 1;
  int IndexCurr = getDispIndex();
  boolean isLast = false;
  if (IndexLast == IndexCurr) {
    isLast = true;
  }
  //作废
  try {
    //赋操作员
    arrivevo.setCoperatorid(getOperatorId());
    //为判断是否可修改、作废其他人单据
    ((ArriveorderHeaderVO) arrivevo.getParentVO()).setCoperatoridnow(getOperatorId());
    //加锁需要
    arrivevo.getParentVO().setAttributeValue("cuserid", getOperatorId());
    PfUtilClient.processBatch("DISCARD", ScmConst.PO_Arrive, ClientEnvironment.getInstance().getDate().toString(), new ArriveorderVO[] { arrivevo });
    boolean bIsSucc = PfUtilClient.isSuccess();
    //add by QuSida 2010-9-11 (佛山骏杰)  --- begin
    //function:删除费用信息
	InformationCostVO[] vos = (InformationCostVO[])getBillCardPanel().getBillModel("jj_scm_information").getBodyValueVOs(InformationCostVO.class.getName());
	if(vos!=null&&vos.length!=0){
		try {
			JJPuScmPubHelper.deleteSmartVOs(vos);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			SCMEnv.out(e);
		}
	}
	//add by QuSida 2010-9-11 (佛山骏杰)  --- end
    //刷新前端缓存
    if (bIsSucc) {
      delArriveorderVODiscarded();
      if (getCacheVOs() == null || !(getCacheVOs().length > 0)) {
        getBillCardPanel().addNew();
        setButtonsState();
      } else {
        getBillCardPanel().getBillData().clearViewData();
        updateUI();
        if (isLast) {
          setDispIndex(getCacheVOs().length - 1);
        } else {
          setDispIndex(IndexCurr);
        }
        onCard();
      }
    }
  } catch (BusinessException b) {
    MessageDialog.showErrorDlg(this, m_lanResTool.getStrByID("SCMCOMMON","UPPSCMCommon-000059")/*@res "错误"*/, b.getMessage());
    showHintMessage(m_lanResTool.getStrByID("SCMCOMMON","UPPSCMCommon-000347")/*@res "作废失败"*/);
    return;
  } catch (Exception e) {
    reportException(e);
    if (e.getMessage() != null
      && (e.getMessage().indexOf(m_lanResTool.getStrByID("40040301","UPP40040301-000250")/*@res "到货"*/) >= 0 || e.getMessage().indexOf(m_lanResTool.getStrByID("40040301","UPP40040301-000212")/*@res ""*/) >= 0 || e.getMessage().indexOf(m_lanResTool.getStrByID("40040301","UPT40040301-000025")/*@res "退货"*/) >= 0)
      || e.getMessage().indexOf(m_lanResTool.getStrByID("40040301","UPP40040301-000207")/*@res "收货"*/) >= 0
      || e.getMessage().indexOf(m_lanResTool.getStrByID("40040301","UPP40040301-000251")/*@res "单据"*/) >= 0
      || e.getMessage().indexOf(m_lanResTool.getStrByID("40040301","UPP40040301-000252")/*@res "容差"*/) >= 0
      || e.getMessage().indexOf(m_lanResTool.getStrByID("40040301","UPP40040301-000253")/*@res "号"*/) >= 0) {
      MessageDialog.showErrorDlg(this, m_lanResTool.getStrByID("SCMCOMMON","UPPSCMCommon-000059")/*@res "错误"*/, e.getMessage());
    } else
      MessageDialog.showErrorDlg(this, m_lanResTool.getStrByID("SCMCOMMON","UPPSCMCommon-000059")/*@res "错误"*/, m_lanResTool.getStrByID("40040301","UPP40040301-000140")/*@res "作废单据失败"*/);
    showHintMessage(m_lanResTool.getStrByID("SCMCOMMON","UPPSCMCommon-000347")/*@res "作废失败"*/);
    return;
  }
  /************记录业务日志*************//*
   * 连接数优化，插日志操作放到后台
	Operlog operlog=new Operlog();
	arrivevo.getOperatelogVO().setOpratorname(nc.ui.pub.ClientEnvironment.getInstance().getUser().getUserName());
	arrivevo.getOperatelogVO().setCompanyname(nc.ui.pub.ClientEnvironment.getInstance().getCorporation().getUnitname());
	arrivevo.getOperatelogVO().setOperatorId(nc.ui.pub.ClientEnvironment.getInstance().getUser().getPrimaryKey());
	operlog.insertBusinessExceptionlog(arrivevo, "删除", "删除", nc.vo.scm.funcs.Businesslog.MSGMESSAGE, nc.vo.scm.pu.BillTypeConst.PO_ARRIVE,
			nc.ui.sm.cmenu.Desktop.getApplet().getParameter("USER_IP"));
	*//************记录业务日志* end ************/
  showHintMessage(m_lanResTool.getStrByID("common","4004COMMON000000068")/*@res "作废成功"*/);
}

/**
 * @功能：作废列表
 * @作废条件：能修改且没有报检过的行;
 * @不满足条件处理：
   1.作废可以作废的单据
   2.给出不能作废的单据行号
 * @作者：晁志平
 * 创建日期：(2001-06-20 10:40:17)
 * 修改日期：(2001-10-29 14:40:17)
 */
private void onDiscardSelected() {

    int iRet = 
      MessageDialog.showYesNoDlg(this,m_lanResTool.getStrByID("SCMCOMMON","UPPSCMCommon-000219")/*@res "确定"*/,nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UCH002")/*@res "是否确认要删除？"*/,UIDialog.ID_NO);
    if(iRet != UIDialog.ID_YES){
        return;
    }
  showHintMessage(m_lanResTool.getStrByID("40040301","UPP40040301-000139")/*@res "正在作废..."*/);
  Vector v = new Vector();
  String lines = "";
  int i = 0, iRealPos = 0;
  boolean isSortFlag = false;
  if (getBillListPanel().getHeadBillModel().getSortIndex() != null) {
    isSortFlag = true;
  }
  int rowcount = getBillListPanel().getHeadBillModel().getRowCount();
  BillModel bm = getBillListPanel().getHeadBillModel();
  ArriveorderVO arrivevo = null;
  for (i = 0; i < rowcount; i++) {
    if (bm.getRowState(i) == BillModel.SELECTED) {
      iRealPos = i;
      if (isSortFlag) {
        iRealPos = getBillListPanel().getHeadBillModel().getSortIndex()[i];
      }
      //选中的单据(缓存中)
      arrivevo = getCacheVOs()[iRealPos];
      //给出不符合作废条件的行数
      if (!arrivevo.isCanBeDiscarded() || arrivevo.isHaveCheckLine()) {
        if (!lines.trim().equals("")) {
          lines += m_lanResTool.getStrByID("SCMCOMMON","UPPSCMCommon-000000")/*@res "、"*/;
        }
        lines += i + 1;
      } else {
        v.add(arrivevo);
      }
    }
  }
  if (!lines.trim().equals("")) {
    if (lines.length() == 1) {
      MessageDialog.showWarningDlg(
        this,
        m_lanResTool.getStrByID("SCMCOMMON","UPPSCMCommon-000270")/*@res "提示"*/,
        CommonConstant.BEGIN_MARK
          + lines
          + CommonConstant.END_MARK
          + m_lanResTool.getStrByID("40040301","UPP40040301-000141")/*@res " 行到货单已经审批或正在审批或已经报检,这张单据不会被作废"*/);
    } else {
      MessageDialog.showWarningDlg(
        this,
        m_lanResTool.getStrByID("SCMCOMMON","UPPSCMCommon-000270")/*@res "提示"*/,
        CommonConstant.BEGIN_MARK
          + lines
          + CommonConstant.END_MARK
          + m_lanResTool.getStrByID("40040301","UPP40040301-000142")/*@res " 行到货单已经审批或正在审批或已经报检,这些单据不会被作废"*/);
    }
  }
  ArriveorderVO[] arrivevos = null;
  if (v.size() > 0) {
    arrivevos = new ArriveorderVO[v.size()];
    v.copyInto(arrivevos);
    try {
      //赋操作员
      for (int j = 0; j < arrivevos.length; j++) {
        arrivevos[j].setCoperatorid(getOperatorId());
        //为判断是否可修改、作废其他人单据
        ((ArriveorderHeaderVO) arrivevos[j].getParentVO()).setCoperatoridnow(getOperatorId());
        //加锁需要
        arrivevos[j].getParentVO().setAttributeValue("cuserid", getOperatorId());
      }
      //加载表体
      arrivevos = RcTool.getRefreshedVOs(arrivevos);
      PfUtilClient.processBatch(
        "DISCARD",
        ScmConst.PO_Arrive,
        ClientEnvironment.getInstance().getDate().toString(),
        arrivevos);
      boolean bIsSucc = PfUtilClient.isSuccess();
      //刷新前端缓存
      if (bIsSucc) {
        //全部作废
        if (v.size() == getCacheVOs().length) {
          getBillListPanel().getBodyBillModel().clearBodyData();
          getBillListPanel().getHeadBillModel().clearBodyData();
          setCacheVOs(null);
          updateUI();
        } else {
          //刷新显示
          delArriveorderVOsDiscarded(v);
          getBillListPanel().getBodyBillModel().clearBodyData();
          getBillListPanel().getHeadBillModel().clearBodyData();
          setDispIndex(0);
          onList();
        }
      }
    } catch (BusinessException b) {
      MessageDialog.showErrorDlg(this, m_lanResTool.getStrByID("SCMCOMMON","UPPSCMCommon-000059")/*@res "错误"*/, b.getMessage());
      showHintMessage(m_lanResTool.getStrByID("SCMCOMMON","UPPSCMCommon-000347")/*@res "作废失败"*/);
      return;
    } catch (Exception e) {
      reportException(e);
      MessageDialog.showErrorDlg(this, m_lanResTool.getStrByID("SCMCOMMON","UPPSCMCommon-000059")/*@res "错误"*/, m_lanResTool.getStrByID("40040301","UPP40040301-000143")/*@res "作废到货单失败"*/);
      showHintMessage(m_lanResTool.getStrByID("SCMCOMMON","UPPSCMCommon-000347")/*@res "作废失败"*/);
      return;
    }
    /************记录业务日志*************/
    Operlog operlog=new Operlog();
    for (ArriveorderVO arriveorderVO : arrivevos) {
    	arriveorderVO.getOperatelogVO().setOpratorname(nc.ui.pub.ClientEnvironment.getInstance().getUser().getUserName());
    	arriveorderVO.getOperatelogVO().setCompanyname(nc.ui.pub.ClientEnvironment.getInstance().getCorporation().getUnitname());
    	arriveorderVO.getOperatelogVO().setOperatorId(nc.ui.pub.ClientEnvironment.getInstance().getUser().getPrimaryKey());
    	operlog.insertBusinessExceptionlog(arriveorderVO, "删除", "删除", nc.vo.scm.funcs.Businesslog.MSGMESSAGE, nc.vo.scm.pu.BillTypeConst.PO_ARRIVE,
				nc.ui.sm.cmenu.Desktop.getApplet().getParameter("USER_IP"));
    }
    /************记录业务日志* end ************/
    showHintMessage(m_lanResTool.getStrByID("common","UCH006")/*@res "删除成功"*/);
  } else {
    onSelectNo();
    showHintMessage(m_lanResTool.getStrByID("40040301","UPP40040301-000144")/*@res "作废失败:所选单据均不符合作废条件"*/);
  }
  //
  setButtonsList();
  //
  showHintMessage(m_lanResTool.getStrByID("common","4004COMMON000000068")/*@res "作废成功"*/);
}

/**
 * 功能 ：文管管理
 * 适用单据状态：“到货浏览”、“到货列表”
 */
private void onDocument() {
  String[] strPks = null;
  String[] strCodes = null;
  if (!(getStateStr().equalsIgnoreCase("到货浏览")
    || getStateStr().equalsIgnoreCase("到货列表")
    || getStateStr().equalsIgnoreCase("消息中心"))) {
    showHintMessage(m_lanResTool.getStrByID("40040301","UPP40040301-000145")/*@res "获取不到单据号,文档管理功能不可用"*/);
  }
  HashMap mapBtnPowerVo = new HashMap();
  Integer iBillStatus = null;
  //卡片
  if (getStateStr().equalsIgnoreCase("到货浏览")
      || getStateStr().equalsIgnoreCase("消息中心")) {
    if (getCacheVOs() != null
      && getCacheVOs().length > 0
      && getCacheVOs()[getDispIndex()] != null
      && getCacheVOs()[getDispIndex()].getParentVO() != null) {
      strPks =
        new String[] {
          (String) getCacheVOs()[getDispIndex()].getParentVO().getAttributeValue(
            "carriveorderid")};
      strCodes =
        new String[] {
          (String) getCacheVOs()[getDispIndex()].getParentVO().getAttributeValue(
            "varrordercode")};
      // 处理文档管理框删除按钮是否可用
      BtnPowerVO pVo = new BtnPowerVO(strCodes[0]);
      iBillStatus = PuPubVO.getInteger_NullAs(getCacheVOs()[getDispIndex()].getParentVO().getAttributeValue("ibillstatus"),new Integer(BillStatus.FREE));
      if (iBillStatus.intValue() == 2 || iBillStatus.intValue() == 3) {
        pVo.setFileDelEnable("false");
      }
      mapBtnPowerVo.put(strCodes[0], pVo);
    }
  }
  //列表
  if (getStateStr().equalsIgnoreCase("到货列表")) {
    if (getCacheVOs() != null && getCacheVOs().length > 0) {
      ArriveorderHeaderVO[] headers = null;
      headers =
        (ArriveorderHeaderVO[]) getBillListPanel()
          .getHeadBillModel()
          .getBodySelectedVOs(ArriveorderHeaderVO.class.getName());
      if (headers == null || headers.length <= 0)
        return;
      strPks = new String[headers.length];
      strCodes = new String[headers.length];
      BtnPowerVO pVo = null;
      for (int i = 0; i < headers.length; i++) {
        strPks[i] = headers[i].getPrimaryKey();
        strCodes[i] = headers[i].getVarrordercode();
        // 处理文档管理框删除按钮是否可用
        pVo = new BtnPowerVO(strCodes[i]);
        iBillStatus = PuPubVO.getInteger_NullAs(headers[i]
            .getIbillstatus(), new Integer(0));
        if (iBillStatus.intValue() == 2 || iBillStatus.intValue() == 3) {
          pVo.setFileDelEnable("false");
        }
        mapBtnPowerVo.put(strCodes[i], pVo);
      }
    }
  }
  if (strPks == null || strPks.length <= 0)
    return;
  //调用文档管理对话框
  nc.ui.scm.file.DocumentManager.showDM(this,ScmConst.PO_Arrive ,strPks, mapBtnPowerVo);
  showHintMessage(m_lanResTool.getStrByID("common","4004COMMON000000025")/*@res "文档管理成功"*/);
}

/**
 * @功能：放弃转单/转单结束
 */
private void onEndCreate() {
  showHintMessage(m_lanResTool.getStrByID("40040301","UPP40040301-000146")/*@res "正在退出转单..."*/);
  /*重置缓存VO[]:有无转单成功均要重置*/
  setCacheVOs(m_VOsAll);
  /*清除列表数据*/
  getBillListPanel().getBillListData().clearCopyData();
  /*重置显示位置:区分有无转入成功的单据作不同处理*/
  int iNewCnt = 0;
  if (getCacheVOs() != null && getCacheVOs().length > m_OldVOsLen) {
    iNewCnt = getCacheVOs().length - m_OldVOsLen;
    setDispIndex(getCacheVOs().length - 1);
  } else {
    setDispIndex(m_OldCardVOPos);
  }
  /*向卡片加载数据*/
  setM_strState("到货浏览");
  /*与切换到卡片时作相同的处理*/
  onCard();
  /*初始化转入用变量*/
  m_VOsAll = null;
  m_OldCardVOPos = 0;
  m_OldVOsLen = 0;
  if (iNewCnt > 0) {
    showHintMessage(m_lanResTool.getStrByID("40040301","UPP40040301-000147")/*@res "转单结束:生成 "*/ + iNewCnt + m_lanResTool.getStrByID("40040301","UPP40040301-000148")/*@res " 张新单据"*/);
  } else {
    showHintMessage(m_lanResTool.getStrByID("40040301","UPP40040301-000149")/*@res "转单结束:没有新单据生成"*/);
  }
  return;
}
private void setBodyFreeVO(ArriveorderVO[] retVOs){
	nc.ui.scm.pub.FreeVOParse freeVOParse = new nc.ui.scm.pub.FreeVOParse();
	for(ArriveorderVO vo:retVOs){
		freeVOParse.setFreeVO(vo.getChildrenVO(), "vfree0", "vfree", "cbaseid", "cmangid", true);
	}
}
/**
 * @功能：从订单生成对话框中退回时的处理
 */
private void onExitFrmOrd(ArriveorderVO[] retVOs) {
  /*如果从选单界面取消返回，则 retVOs = null,由onButtonClicked()保证*/
  if (retVOs != null && retVOs.length > 0) {
    //集中采购跨公司情况下的公司间ID转换
    try{
      RcTool.chgDataForArrvCorp(retVOs,getCorpPrimaryKey());
    }catch(BusinessException e){
      MessageDialog.showHintDlg(this, m_lanResTool.getStrByID("SCMCOMMON","UPPSCMCommon-000270")/*@res "提示"*/, e.getMessage());
      return;
    }
    //
    showHintMessage(m_lanResTool.getStrByID("40040301","UPP40040301-000150")/*@res "正在向列表加载数据..."*/);
    /*保存转单前缓存数据信息*/
    m_VOsAll = getCacheVOs();
    if (m_VOsAll != null && m_VOsAll.length > 0) {
      m_OldVOsLen = m_VOsAll.length;
    } else {
      m_OldVOsLen = 0;
      m_VOsAll = null;
    }
    m_OldCardVOPos = getDispIndex();
    setBodyFreeVO(retVOs);
    /*当前缓存置入待保存数据信息*/
    setCacheVOs(retVOs);
    //用于缓存TS
    m_hTS = new HashMap();
    m_pushSaveVOs = null;
    setDispIndex(0);
    /*显示数据*/
    displayArrBillListPanelNew();
    String[] value = new String[]{String.valueOf(getCacheVOs().length)};
    showHintMessage(m_lanResTool.getStrByID("40040301","UPP40040301-000151",null,value)/*@res "列表加载数据完成: 共加载"+ getCacheVOs().length + " 张待保存单据"*/ );
  }
  this.repaint();
}

/**
 * 功能描述:首页
 */
private void onFirst() {
  int iRollBack = getDispIndex();
  setDispIndex(0);
  try {
    loadDataToCard();
    setButtonsState();
    showHintMessage(m_lanResTool.getStrByID("common","4004COMMON000000026")/*@res "成功显示首页"*/);
  } catch (Exception e) {
    SCMEnv.out("加载单据时报错");
    setDispIndex(iRollBack);
    showHintMessage(m_lanResTool.getStrByID("40040301","UPP40040301-000153")/*@res "显示第一张单据失败"*/);
  }
}

/**
 * 功能描述:末页
 */
private void onLast() {
  int iRollBack = getDispIndex();
  setDispIndex(getCacheVOs().length - 1);
  try {
    loadDataToCard();
    setButtonsState();
    showHintMessage(m_lanResTool.getStrByID("common","4004COMMON000000029")/*@res "成功显示末页"*/);
  } catch (Exception e) {
    SCMEnv.out("加载单据时报错");
    setDispIndex(iRollBack);
    showHintMessage(m_lanResTool.getStrByID("40040301","UPP40040301-000155")/*@res "显示最后一张单据失败"*/);
  }
}
/**
 * @功能：列表(区分两种状态：维护修改和转入修改)
 * @作者：晁志平
 * 创建日期：(2001-5-24 9:19:15)
 * @param:<|>
 * @return:
 * @exception:
 * @see;
 * @since:
 *
 */
private void onList() {
  showHintMessage(m_lanResTool.getStrByID("40040301","UPP40040301-000156")/*@res "正在向列表加载数据"*/);
  //清空状态图片
  //V5 Del : setImageType(this.IMAGE_NULL);
  //排序索引
  int index = getBillCardPanel().getBillModel().getSortColumn();
  boolean bSortAsc = getBillCardPanel().getBillModel().isSortAscending();
  if (getStateStr().equals("转入修改")) {
    displayArrBillListPanelNew();
  } else {
    displayArrBillListPanel();
  }
  if(index >= 0){
    getBillListPanel().getBodyBillModel().sortByColumn(index,bSortAsc);
  }
  updateUI();
  showHintMessage(m_lanResTool.getStrByID("common","UCH022")/*@res "列表显示"*/);
}

/**
 * 单据逐级联查
 */
private void onLnkQuery() {
  ArriveorderVO vo = getCacheVOs()[getDispIndex()];
  if (vo == null || vo.getParentVO() == null)
    return;
  nc.ui.scm.sourcebill.SourceBillFlowDlg soureDlg =
    new nc.ui.scm.sourcebill.SourceBillFlowDlg(
      this,
      nc.vo.scm.pu.BillTypeConst.PO_ARRIVE,
      ((ArriveorderHeaderVO) vo.getParentVO()).getPrimaryKey(),
      null,
      getClientEnvironment().getUser().getPrimaryKey(),
      ((ArriveorderHeaderVO) vo.getParentVO()).getVarrordercode());
  soureDlg.showModal();
  showHintMessage(m_lanResTool.getStrByID("common","4004COMMON000000019")/*@res "联查成功"*/);
}

/**
 * @功能：定位
 */
private void onLocate() {
  showHintMessage(m_lanResTool.getStrByID("40040301","UPP40040301-000157")/*@res "选择定位位置..."*/);
  //每次凋用时重置单据数及显示索引
  //UILabel
  getLocateDlg().setCurrBillCount(getCacheVOs().length);
  getLocateDlg().setCurrBillIndex(getDispIndex() + 1);
  String txt = getLocateDlg().getUILabel_Locate().getText();
  getLocateDlg().getUILabel_Locate().setText(
    txt.substring(0, txt.indexOf("{"))
      + "{1-"
      + (getLocateDlg().getCurrBillCount())
      + "}");
  //UITextField
  getLocateDlg().getUITextField_Locate().setMaxValue(
    getLocateDlg().getCurrBillCount());
  getLocateDlg().getUITextField_Locate().setMinValue(1);
  getLocateDlg().getUITextField_Locate().setText(
    (new Integer(getLocateDlg().getCurrBillIndex())).toString());

  getLocateDlg().showModal();
  showHintMessage(m_lanResTool.getStrByID("40040301","UPP40040301-000131")/*@res "正在加载单据..."*/);
  if (getLocateDlg().isCloseOK()) {
    int iRollBack = getDispIndex();
    int currIndex = getLocateDlg().getLocateIndex();
    setDispIndex(currIndex - 1);
    setButtonsState();
    try {
      loadDataToCard();
    } catch (Exception e) {
      SCMEnv.out("加载单据时报错");
      setDispIndex(iRollBack);
      showHintMessage(m_lanResTool.getStrByID("40040301","UPP40040301-000158")/*@res "定位失败"*/);
    }

    showHintMessage(m_lanResTool.getStrByID("common","4004COMMON000000035")/*@res "定位成功"*/);
    updateUI();
  } else {
    showHintMessage(m_lanResTool.getStrByID("40040301","UPP40040301-000160")/*@res "取消定位"*/);
  }
}

/**
 * 功能描述:处理浮动菜单
 * 创建日期：(2001-3-27 11:09:34)
 * @param e java.awt.event.ActionEvent
 */
public void onMenuItemClick(java.awt.event.ActionEvent event) {
  UIMenuItem menuItem = (UIMenuItem) event.getSource();
  if (menuItem.equals(getBillCardPanel().getCopyLineMenuItem())) {
    onCopyLine();
  } else if (menuItem.equals(getBillCardPanel().getDelLineMenuItem())) {
    onDeleteLine();
  } else if (menuItem.equals(getBillCardPanel().getPasteLineMenuItem())) {
    onPasteLine();
  }else if (menuItem.equals(getBillCardPanel().getPasteLineToTailMenuItem())) {
    onPasteLineToTail();
  }
  //重排行号
  else if(menuItem.equals(m_miReSortRowNo)){
    onReSortRowNo();
  }
  else if(menuItem.equals(m_miCardEdit)){
	  onCardEdit();
  }
}
/*
 * 重排行号
 */
private void onReSortRowNo(){
	if (getCacheVOs()[getDispIndex()].isHaveCheckLine()) {
		setPopMenuBtnsEnable(false);
		return;
	}

  PuTool.resortRowNo(getBillCardPanel(), ScmConst.PO_Arrive, "crowno");
  showHintMessage(NCLangRes.getInstance().getStrByID("common","SCMCOMMON000000284")/*@res "重排行号成功"*/);
}
private void onCardEdit(){
	if (getCacheVOs()[getDispIndex()].isHaveCheckLine()) {
		setPopMenuBtnsEnable(false);
		return;
	}

	//对于像备注这样，允许输入参照中不存在的“无效值”的字段，卡片编辑时会带不出用户录入的无效值。
	//UAP55 (2008.8.22)存在这个问题
	//处理办法是先将item数据类型设为String，这样卡片中就会带出值，后面再改回原（模板中定义的）类型
	BillItem memoItem=getBillCardPanel().getBodyItem("vmemo");
	int orgDataType=BillItem.STRING;
	if(memoItem!=null){
		orgDataType=memoItem.getDataType();
		memoItem.setDataType(BillItem.STRING);
	}
	//解决卡片编辑界面，触发beforeedit事件方式不起作用，判断批次管理
	if(getBillCardPanel().getBodyItem("vproducenum").isShow()){
	for(int i=0; i<getBillCardPanel().getRowCount(); i++){		
		if(beforeEditProdNum(new nc.ui.pub.bill.BillEditEvent(getBillCardPanel().getBodyItem("vproducenum")
				,null,null,"vproducenum",i,1))){
			getBillCardPanel().getBodyItem("vproducenum").setEnabled(true);
		}
		else{
			getBillCardPanel().getBodyItem("vproducenum").setEnabled(false);
		}
		}
	}
	//去掉卡片编辑界面的增加/删行按钮
	boolean oldRowEditState = getBillCardPanel().getBillModel().getRowEditState();
	getBillCardPanel().getBillModel().setRowEditState(true);
	//启动卡片编辑
	getBillCardPanel().startRowCardEdit();
	//改回item的原始（模板中定义的）数据类型
	if(memoItem!=null) memoItem.setDataType(orgDataType);
	
	
	getBillCardPanel().getBillModel().setRowEditState(oldRowEditState);
	
}
/**
 * 来源委外的到货单赠品行不允许编辑
 *
 */
private void restrictSCBlargess(){
if(getBillCardPanel().getBillModel().getItemByKey("blargess")!=null){
	if(isFromSC()){
		getBillCardPanel().getBodyItem("blargess").setEnabled(false);
	}else{
		getBillCardPanel().getBodyItem("blargess").setEnabled(getBillCardPanel().getBodyItem("blargess").isEdit());

	}
}
	
}

/**
 * 功能描述:更改
 */
private void onModify() {

  setM_strState(OrderPubVO.RC_ARRIVEMODIFY_STATE);

  int iCurSelectedRow = getBillCardPanel().getBillTable().getSelectedRow();
  getBillCardPanel().updateValue();
  if (iCurSelectedRow >= 0)
    getBillCardPanel().getBillTable().setRowSelectionInterval(iCurSelectedRow, iCurSelectedRow);
  //add by 付世超 2010-10-12 (佛山俊杰) begin
  getBillCardPanel().getBodyTabbedPane().setSelectedIndex(0);//重置表体页签显示为首页签!
  //add by 付世超 2010-10-12 (佛山俊杰) end
  //初始化删除集合(库位分配时用到)
  v_DeletedItems = new Vector();

  //置是否拆分生成的行属性为 new UFBoolean(false)
  if (getCacheVOs() != null && getCacheVOs().length > 0) {
    for (int i = 0; i < (getCacheVOs()[getDispIndex()].getChildrenVO().length); i++) {
      ((ArriveorderItemVO) ((ArriveorderVO) getCacheVOs()[getDispIndex()]).getChildrenVO()[i]).setIssplit(new UFBoolean(false));
      getBillCardPanel().getBillModel().setValueAt(new UFBoolean(false), i, "issplit");
    }
  }
  getBillCardPanel().setEnabled(true);
  setAuditInfo();
  //业务类型不能修改
  if (getBillCardPanel().getHeadItem("cbiztype") != null)
    getBillCardPanel().getHeadItem("cbiztype").setEnabled(false);
//  //单据号不能修改
//  if (getBillCardPanel().getHeadItem("varrordercode") != null)
//    getBillCardPanel().getHeadItem("varrordercode").setEnabled(false);
  //是否退货不能修改
  if (getBillCardPanel().getHeadItem("bisback") != null)
    getBillCardPanel().getHeadItem("bisback").setEnabled(false);
  //打印次数不能修改
  if (getBillCardPanel().getTailItem("iprintcount") != null)
    getBillCardPanel().getTailItem("iprintcount").setEnabled(false);  
  //累计入库数量应不可编辑
  getBillCardPanel().getBodyItem("naccumwarehousenum").setEdit(false);  
  //退货理由(头体)
  setBackReasonEditable();
  //来源委外赠品不可编辑
  restrictSCBlargess();
  //根据操作员设置对应采购员及部门
  setDefaultValueByUser();
  
  //库存组织限制仓库
  if (getStateStr().equals(OrderPubVO.RC_ARRIVEMODIFY_STATE) || getStateStr().equals(OrderPubVO.RC_OUTMODIFY_STATE)) {
    setOrgWarhouse();
  }
  setButtonsState();
  updateButtons();

  //过滤存货参照,保证在业务类型是受托代销业务类型时替换件参照是受托代销属性
    //取业务类型
    String cBizType = getBillCardPanel().getHeadItem("cbiztype").getValue();
    UFBoolean checker = new UFBoolean(false);
    try {
      checker = CheckISSellProxyHelper.CheckIsSellProxyType(cBizType);
        //过滤存货参照
        if(checker.booleanValue()){
          String sql = " and (sellproxyflag = 'Y')";
          UIRefPane refCinventorycode = (UIRefPane) (getBillCardPanel().getBodyItem("cinventorycode").getComponent());
          refCinventorycode.getRefModel().addWherePart(sql);
       }
  } catch (Exception e) {
    SCMEnv.out(e);
  }

  showHintMessage(m_lanResTool.getStrByID("common","4004COMMON000000030")/*@res "正在修改"*/);
}
/**
 * 根据操作员设置采购员及采购部门。
 * <p>
 * <b>examples:</b>
 * <p>
 * 使用示例
 * <p>
 * <b>参数说明</b>
 * <p>
 * @author czp
 * @time 2007-3-26 下午04:49:35
 */
private void setDefaultValueByUser(){
  //取操作员对应业务员，设置采购员(采购员无值时才设置)
  if(PuPubVO.getString_TrimZeroLenAsNull(getBillCardPanel().getHeadItem("cemployeeid").getValueObject()) == null){
    IUserManageQuery iSrvUser = (IUserManageQuery) NCLocator.getInstance().lookup(IUserManageQuery.class.getName());
    PsndocVO voPsnDoc = null;
    try{
      voPsnDoc = iSrvUser.getPsndocByUserid(getBillCardPanel().getCorp(), PoPublicUIClass.getLoginUser());
    }catch(BusinessException be){
      SCMEnv.out(be);
    }
    if(voPsnDoc != null){
      UIRefPane refPanePrayPsn = (UIRefPane) getBillCardPanel().getHeadItem("cemployeeid").getComponent();
      refPanePrayPsn.setPK(voPsnDoc.getPrimaryKey());
      //由采购员带出采购部门(如果采购员部门无值时才带出)
      if(PuPubVO.getString_TrimZeroLenAsNull(getBillCardPanel().getHeadItem("cdeptid").getValueObject()) == null){
        afterEditWhenHeadEmployee(null);
      }
    }
  }
}
/**
 * 功能描述:浮动菜单右键功能权限控制
 */
private void rightButtonRightControl(){
  //  没有分配行操作权限
    if(m_btnLines == null || m_btnLines.getChildCount() == 0){
      getBillCardPanel().getCopyLineMenuItem().setEnabled(false);
      getBillCardPanel().getDelLineMenuItem().setEnabled(false);
      getBillCardPanel().getPasteLineMenuItem().setEnabled(false);
      getBillCardPanel().getPasteLineToTailMenuItem().setEnabled(false);
      m_miReSortRowNo.setEnabled(false);
    }
    //分配行操作权限
    else{
      getBillCardPanel().getCopyLineMenuItem().setEnabled(m_btnCpyLine.isPower() && m_btnCpyLine.isEnabled());
      getBillCardPanel().getDelLineMenuItem().setEnabled(m_btnDelLine.isPower() && m_btnDelLine.isEnabled());
      getBillCardPanel().getPasteLineMenuItem().setEnabled(m_btnPstLine.isPower() && m_btnPstLine.isEnabled());
      //粘贴到行尾与粘贴可用性逻辑相同
      getBillCardPanel().getPasteLineToTailMenuItem().setEnabled(m_btnPstLine.isPower() && m_btnPstLine.isEnabled());
      m_miReSortRowNo.setEnabled(m_btnReSortRowNo.isPower() && m_btnReSortRowNo.isEnabled());      
     }
}

/**
 * @功能：列表状态下的修改
 * @作者：晁志平
 * 创建日期：(2001-9-13 20:02:06)
 * @param:<|>
 * @return:
 * @exception:
 * @see;
 * @since:
 *
 */
private void onModifyList() {
  showHintMessage(m_lanResTool.getStrByID("40040301","UPP40040301-000161")/*@res "正在切换到卡片..."*/);
  isFrmList = true;
  onCard();
  onModify();
  showHintMessage(m_lanResTool.getStrByID("common","4004COMMON000000030")/*@res "正在修改"*/);
}
/**
 * 功能描述:下页
 */
private void onNext() {
  int iRollBack = getDispIndex();
  setDispIndex(getDispIndex() + 1);
  try {
    loadDataToCard();
    setButtonsState();
    showHintMessage(m_lanResTool.getStrByID("common","4004COMMON000000028")/*@res "成功显示下一页"*/);
  } catch (Exception e) {
    SCMEnv.out("加载单据时报错");
    setDispIndex(iRollBack);
    showHintMessage(m_lanResTool.getStrByID("40040301","UPP40040301-000163")/*@res "显示下一张单据失败"*/);
  }
}

/**
 * 功能描述:粘贴行
 */
private void onPasteLine() {
  int iOldRowCnt = getBillCardPanel().getBillModel().getRowCount();
  if (getCacheVOs()[getDispIndex()].isHaveCheckLine()) {
	  setPopMenuBtnsEnable(false);
	  return;
  }

  try {
    getBillCardPanel().pasteLine();
  } catch (Exception e) {
    SCMEnv.out("粘贴行时出错：" + e.getMessage());
  }
  int iNewRowCnt = getBillCardPanel().getBillModel().getRowCount();
  if (iOldRowCnt > 0 && iNewRowCnt > 0 && iOldRowCnt == iNewRowCnt) {
    showHintMessage(m_lanResTool.getStrByID("40040301","UPP40040301-000164")/*@res "粘贴未成功,可能原因：没有拷贝内容或未确定要粘贴的位置"*/);
  } else if (iOldRowCnt <= 0 || iNewRowCnt <= 0) {
    showHintMessage("");
  } else {
    showHintMessage(m_lanResTool.getStrByID("common","UCH040")/*@res "粘贴行成功"*/);
    //单据行号
    BillRowNo.pasteLineRowNo(getBillCardPanel(),nc.vo.scm.pu.BillTypeConst.PO_ARRIVE,"crowno",iNewRowCnt - iOldRowCnt);
//    //
//    int iEndRow = getBillCardPanel().getBillTable().getSelectedRow() - 1;
//    int iBeginRow = iEndRow - (iNewRowCnt-iOldRowCnt) + 1;
//    for(int i=iBeginRow; i<=iEndRow;i++){
//      getBillCardPanel().getBillModel().setRowState(i, BillModel.ADD);
//    }
  }
}

/**
 * 功能描述:粘贴行到行尾
 */
private void onPasteLineToTail() {
  int iOldRowCnt = getBillCardPanel().getBillModel().getRowCount();
  if (getCacheVOs()[getDispIndex()].isHaveCheckLine()) {
	  setPopMenuBtnsEnable(false);
	  return;
  }

  try {
    getBillCardPanel().pasteLineToTail();
  } catch (Exception e) {
    SCMEnv.out("粘贴行到行尾时出错：" + e.getMessage());
  }
  int iNewRowCnt = getBillCardPanel().getBillModel().getRowCount();
  if (iOldRowCnt > 0 && iNewRowCnt > 0 && iOldRowCnt == iNewRowCnt) {
    showHintMessage(m_lanResTool.getStrByID("SCMCOMMON","UPPSCMCommon-000424")/*@res "粘贴行到行尾未成功,可能原因：没有拷贝内容或未确定要粘贴的位置"*/);
  } else if (iOldRowCnt <= 0 || iNewRowCnt <= 0) {
    showHintMessage("");
  } else {
    showHintMessage(m_lanResTool.getStrByID("common","UCH040")/*@res "粘贴行成功"*/);
    //单据行号
    BillRowNo.addLineRowNos(getBillCardPanel(),nc.vo.scm.pu.BillTypeConst.PO_ARRIVE,"crowno",iNewRowCnt - iOldRowCnt);
    //
//    int iEndRow = getBillCardPanel().getRowCount() - 1;
//    int iBeginRow = iEndRow - (iNewRowCnt-iOldRowCnt) + 1;
//    for(int i=iBeginRow; i<=iEndRow;i++){
//      getBillCardPanel().getBillModel().setRowState(i, BillModel.ADD);
//    }
  }
}

/**
 * 功能描述:上页
 */
private void onPrevious() {
  int iRollBack = getDispIndex();
  setDispIndex(getDispIndex() - 1);
  try {
    loadDataToCard();
    setButtonsState();
    showHintMessage(m_lanResTool.getStrByID("common","4004COMMON000000027")/*@res "成功显示上一页"*/);
  } catch (Exception e) {
    SCMEnv.out("加载单据时报错");
    setDispIndex(iRollBack);
    showHintMessage(m_lanResTool.getStrByID("40040301","UPP40040301-000167")/*@res "显示上一张单据失败"*/);
  }
}

/**
 * @功能：到货查询
 * @作者：晁志平
 * @创建：(2001-7-18 12:41:25)
 */
private void onQuery() {
  /**/
  m_hTS = null;
  int iRetType = getQueryConditionDlg().showModal();
  
  
  if (iRetType == UIDialog.ID_OK) {
    m_bQueriedFlag = true;
    showHintMessage(m_lanResTool.getStrByID("40040301","UPP40040301-000168")/*@res "正在查询单据..."*/);
    getArriveVOsFromDB();
    setDispIndex(0);
    if (getStateStr().equals("到货列表")) {
      onList();
    } else {
      isFrmList = false;
      onCard();
    }
    if (getCacheVOs() == null || getCacheVOs().length <= 0) {
      showHintMessage(m_lanResTool.getStrByID("40040301","UPP40040301-000169")/*@res "查询完成:没有符合条件的单据"*/);
    } else {
      String[] value = new String[]{String.valueOf(getCacheVOs().length)};
      showHintMessage(m_lanResTool.getStrByID("40040301","UPP40040301-000170",null,value)/*@res "查询完成:查询到"+ getCacheVOs().length + "张单据"*/ );
    }
    
    if (m_dlgSerialAllocation != null) {
    	setM_alSerialData(null,null);
    }
    setButtonsRevise();
    showHintMessage(m_lanResTool.getStrByID("common","UCH009")/*@res "查询完成"*/);
  }
}

/**
 * 作者：汪维敏
 * 功能：此处插入方法说明
 * 参数：
 * 返回：
 * 例外：
 * 日期：(2004-3-8 10:35:43)
 */
private void onQueryBOM() {
  String sState = getStateStr();
  String sCmangId = null;
  int iPos;
  ArriveorderItemVO itemVO = null;
  if (sState != null && (sState.equals("转入列表") || sState.equals("到货列表"))) {
      if(getBillListPanel().getBodyTable().getRowCount() == 0)
        return;
      iPos = getBillListPanel().getBodyTable().getSelectedRow();
    if (iPos == -1) {
      MessageDialog.showHintDlg(this, m_lanResTool.getStrByID("SCMCOMMON","UPPSCMCommon-000270")/*@res "提示"*/, m_lanResTool.getStrByID("SCMCOMMON","UPPSCMCommon-000364")/*@res "请选中有存货的行!"*/);
      return;
    }
    itemVO = (ArriveorderItemVO)getBillListPanel().getBodyBillModel().getBodyValueRowVO(iPos,ArriveorderItemVO.class.getName());
    sCmangId = itemVO.getCmangid();
  } else {
    if (getBillCardPanel().getRowCount() == 0) {
      return;
    }
    iPos = getBillCardPanel().getBillTable().getSelectedRow();
    if (iPos == -1) {
      MessageDialog.showHintDlg(this, m_lanResTool.getStrByID("SCMCOMMON","UPPSCMCommon-000270")/*@res "提示"*/, m_lanResTool.getStrByID("SCMCOMMON","UPPSCMCommon-000364")/*@res "请选中有存货的行!"*/);
      return;
    }
    itemVO = (ArriveorderItemVO) getCacheVOs()[getDispIndex()].getChildrenVO()[iPos];
    sCmangId = itemVO.getCmangid();
  }
  if (PuPubVO.getString_TrimZeroLenAsNull(sCmangId) == null) {
    MessageDialog.showHintDlg(this, m_lanResTool.getStrByID("SCMCOMMON","UPPSCMCommon-000270")/*@res "提示"*/, m_lanResTool.getStrByID("SCMCOMMON","UPPSCMCommon-000364")/*@res "请选中有存货的行!"*/);
    return;
  }
  SetPartDlg dlg = new SetPartDlg(this);
  dlg.setParam(getCorpPrimaryKey(), sCmangId);
  dlg.showSetpartDlg();
  showHintMessage(m_lanResTool.getStrByID("common","UCH009")/*@res "查询完成"*/);
}

/**
 * 功能：存量查询
 * 创建：(2002-10-31 19:45:39)
 * 修改：2003-04-21/czp/统一走销售对话框
 * 单据状态:初始化；到货浏览；到货修改；到货列表；转入列表；转入修改
 */
private void onQueryInvOnHand() {
  ArriveorderVO voPara = null;
  ArriveorderItemVO item = null;
  ArriveorderItemVO[] items = null;
  /*卡片*/
  if (getStateStr().equals("到货浏览") || getStateStr().equals("到货修改") || getStateStr().equals("转入修改")) {
    voPara = (ArriveorderVO) getBillCardPanel().getBillValueVO(ArriveorderVO.class.getName(), ArriveorderHeaderVO.class.getName(), ArriveorderItemVO.class.getName());
    if (voPara == null) {
      showHintMessage(m_lanResTool.getStrByID("40040301","UPP40040301-000171")/*@res "未选取单据,不能查询存量"*/);
      return;
    }
    /*表体信息完整性检查*/
    int[] iSelRows = getBillCardPanel().getBillTable().getSelectedRows();
    if (iSelRows != null && iSelRows.length > 0) {
      /*得到用户选取的第一行*/
      item = (ArriveorderItemVO) getBillCardPanel().getBillModel().getBodyValueRowVO(iSelRows[0], ArriveorderItemVO.class.getName());
    } else {
      /*用户未选择时，取单据第一行*/
      items = (ArriveorderItemVO[]) getBillCardPanel().getBillModel().getBodyValueVOs(ArriveorderItemVO.class.getName());
      if (items == null || items.length <= 0) {
        showHintMessage(m_lanResTool.getStrByID("40040301","UPP40040301-000172")/*@res "公司、存货、需求日期信息不完整,不能查询存量"*/);
        return;
      }
      item = items[0];
    }
    /*计划执行日期=到货日期*/
    item.setArrvdate((UFDate) voPara.getParentVO().getAttributeValue("dreceivedate"));
    /*信息完整性检查*/
    if (voPara.getParentVO().getAttributeValue("pk_corp") == null
      || voPara.getParentVO().getAttributeValue("pk_corp").toString().trim().equals("")
      || item.getAttributeValue("cinventoryid") == null
      || item.getAttributeValue("cinventoryid").toString().trim().equals("")
      || item.getAttributeValue("arrvdate") == null
      || item.getAttributeValue("arrvdate").toString().trim().equals("")) {
      showHintMessage(m_lanResTool.getStrByID("40040301","UPP40040301-000172")/*@res "公司、存货、需求日期信息不完整,不能查询存量"*/);
      return;
    }
    /*组合新VO初始化并调用存量查询对话框*/
    voPara.setChildrenVO(new ArriveorderItemVO[] { item });
    if (saPkCorp == null) {
      try {
        IUserManageQuery myService = (IUserManageQuery)nc.bs.framework.common.NCLocator.getInstance().lookup(IUserManageQuery.class.getName());
        nc.vo.bd.CorpVO[] vos =
          myService.queryAllCorpsByUserPK(getClientEnvironment().getUser().getPrimaryKey());
        if (vos == null || vos.length == 0){
                  SCMEnv.out("未查询到有权限公司，直接返回!");
                  return;
        }
        final int iLen = vos.length;
        saPkCorp = new String[iLen];
        for (int i = 0; i < iLen; i++){
          saPkCorp[i] = vos[i].getPrimaryKey();
        }
      } catch (Exception e) {
        MessageDialog.showErrorDlg(this, m_lanResTool.getStrByID("40040301","UPP40040301-000173")/*@res "获取有权限公司异常"*/, m_lanResTool.getStrByID("40040301","UPP40040301-000174")/*@res "获取有权限公司时出现异常(详细信息参见控制台日志)!"*/);
        SCMEnv.out(e);
        return;
      }
    }
    getAtpDlg().setPkCorps(saPkCorp);
    getAtpDlg().initData(voPara);
    getAtpDlg().showModal();
  }
  /*列表*/
  else if (getStateStr().equals("到货列表") || getStateStr().equals("转入列表")) {
    /*表头信息完整性检查*/
    ArriveorderHeaderVO head = null;
    if (getBillListPanel().getHeadBillModel().getBodySelectedVOs(ArriveorderHeaderVO.class.getName()) == null
      || getBillListPanel().getHeadBillModel().getBodySelectedVOs(ArriveorderHeaderVO.class.getName()).length <= 0) {
      showHintMessage(m_lanResTool.getStrByID("40040301","UPP40040301-000171")/*@res "未选取单据,不能查询存量"*/);
      return;
    }
    head = (ArriveorderHeaderVO) getBillListPanel().getHeadBillModel().getBodySelectedVOs(ArriveorderHeaderVO.class.getName())[0];
    if (head == null || head.getAttributeValue("pk_corp") == null || head.getAttributeValue("pk_corp").toString().trim().equals("")) {
      showHintMessage(m_lanResTool.getStrByID("40040301","UPP40040301-000175")/*@res "未明确公司,不能查询存量"*/);
      return;
    }
    /*表体信息完整性检查*/
    int[] iSelRows = getBillListPanel().getBodyTable().getSelectedRows();
    if (iSelRows != null && iSelRows.length > 0) {
      /*得到用户选取的第一行*/
      item = (ArriveorderItemVO) getBillListPanel().getBodyBillModel().getBodyValueRowVO(iSelRows[0], ArriveorderItemVO.class.getName());
    } else {
      /*用户未选择时，取单据第一行*/
      items = (ArriveorderItemVO[]) getBillListPanel().getBodyBillModel().getBodyValueVOs(ArriveorderItemVO.class.getName());
      if (items == null || items.length <= 0) {
        showHintMessage(m_lanResTool.getStrByID("40040301","UPP40040301-000172")/*@res "公司、存货、需求日期信息不完整,不能查询存量"*/);
        return;
      }
      item = items[0];
    }
    /*计划执行日期=到货日期*/
    item.setArrvdate(head.getDreceivedate());
    /*信息完整性检查*/
    if (item.getAttributeValue("cinventoryid") == null
      || item.getAttributeValue("cinventoryid").toString().trim().equals("")
      || item.getAttributeValue("arrvdate") == null
      || item.getAttributeValue("arrvdate").toString().trim().equals("")) {
      showHintMessage(m_lanResTool.getStrByID("40040301","UPP40040301-000172")/*@res "公司、存货、需求日期信息不完整,不能查询存量"*/);
      return;
    }
    /*组合新VO初始化并调用存量查询对话框*/
    voPara = new ArriveorderVO();
    voPara.setParentVO(head);
    voPara.setChildrenVO(new ArriveorderItemVO[] { item });
    if (saPkCorp == null) {
      try {
        IUserManageQuery myService = (IUserManageQuery)nc.bs.framework.common.NCLocator.getInstance().lookup(IUserManageQuery.class.getName());
        nc.vo.bd.CorpVO[] vos =
          myService.queryAllCorpsByUserPK(getClientEnvironment().getUser().getPrimaryKey());
        if (vos == null || vos.length == 0){
                  SCMEnv.out("未查询到有权限公司，直接返回!");
                  return;
        }
        final int iLen = vos.length;
        saPkCorp = new String[iLen];
        for (int i = 0; i < iLen; i++){
          saPkCorp[i] = vos[i].getPrimaryKey();
        }
      } catch (Exception e) {
        MessageDialog.showErrorDlg(this, m_lanResTool.getStrByID("40040301","UPP40040301-000173")/*@res "获取有权限公司异常"*/, m_lanResTool.getStrByID("40040301","UPP40040301-000174")/*@res "获取有权限公司时出现异常(详细信息参见控制台日志)!"*/);
        SCMEnv.out(e);
        return;
      }
    }
    getAtpDlg().setPkCorps(saPkCorp);
    getAtpDlg().initData(voPara);
    getAtpDlg().showModal();
  }
  showHintMessage(m_lanResTool.getStrByID("common","4004COMMON000000032")/*@res "存量查询完成"*/);
}

/**
 * 作者：汪维敏
 * 功能：快速收货
 * 参数：
 * 返回：
 * 例外：
 * 日期：(2004-3-15 10:20:43)
 */
private void onQuickArr() {
  int iRetType = UIDialog.ID_OK;
  do{
    getQuickArrDlg().setCheckBoxSel(false);
    getQuickArrDlg().setBillCodeValue("");
    getQuickArrDlg().showModal();
    iRetType = getQuickArrDlg().getResult();
    if (iRetType == UIDialog.ID_OK){
      String sBillCode = getQuickArrDlg().getBillCodeValue();
      if (sBillCode == null || sBillCode.trim().length() == 0) {
        showWarningMessage(m_lanResTool.getStrByID("40040301","UPP40040301-000176")/*@res "单据号为空，请输入单据号!"*/);
      }else iRetType = UIDialog.ID_NO;
    }else return;
  }while(iRetType == UIDialog.ID_OK);
  
  //V35BUG:
  if(isQuickException()){
    setQuickExceptionFlag(false);
    return;
  }

  try {
    ArriveorderVO[] retVOs = (ArriveorderVO[]) getQuickArrDlg().getRetVos();
    if (retVOs == null || retVOs.length == 0 || retVOs[0] == null) {
      if(getQuickArrDlg().getErrorMsg() != null){
        showWarningMessage(getQuickArrDlg().getErrorMsg());
      }else{
        showWarningMessage(m_lanResTool.getStrByID("40040301","UPP40040301-000177")/*@res "没有符合条件的单据！"*/);
      }
      return;
    }
    //区分是否保存前浏览
    if (getQuickArrDlg().isLookBefSave()) {
      onExitFrmOrd(retVOs);
      //推式保存到货单
    } else {
      //since v502 , 加入部分非空项目检查
      verifyNotNullFields(retVOs, getQuickArrDlg().getBillCodeValue());
      //
      m_hTS = new HashMap();
      m_pushSaveVOs = retVOs;
      for (int i = 0; i < retVOs.length; i++) {
        ArriveorderVO saveVO = retVOs[i];

        //流程保存方法的参数

        //aryPara0 : 0，公司主键；1，修改前的到货单；2，当前单据编辑状态；3.修改后的VO（界面VO）
        ArrayList aryPara = new ArrayList(2);
        ArrayList aryPara0 = new ArrayList();

        aryPara0.add(getCorpPrimaryKey());
        aryPara0.add(saveVO);
        aryPara0.add("insert");
        aryPara0.add(saveVO);

        aryPara.add(aryPara0);
        aryPara.add(null);
        aryPara.add(new Integer(0));
        aryPara.add(new String("cvendormangid"));

        //重置表头表体状态(新增)
         ((ArriveorderHeaderVO) saveVO.getParentVO()).setStatus(VOStatus.NEW);
        for (int j = 0; j < saveVO.getChildrenVO().length; j++) {
          ((ArriveorderItemVO[]) saveVO.getChildrenVO())[j].setStatus(VOStatus.NEW);
        }
        saveVO.setOprType(VOStatus.NEW);
        //赋操作员
        saveVO.setCoperatorid(getOperatorId());
        //判断并发的公用方法 PubDMO.checkVoNoChanged() 需要
        saveVO.getParentVO().setAttributeValue("cuserid", getOperatorId());

        ArrayList aryRet = (ArrayList) PfUtilClient.processAction("SAVEBASE", ScmConst.PO_Arrive, ClientEnvironment.getInstance().getDate().toString(), saveVO, aryPara);

        // 处理并发，刷新缓存VO的TS
        refreshVOTs((ArriveorderVO) aryRet.get(1));
        String strRetKey = (aryRet == null) ? null : (String) aryRet.get(0);
        boolean bIsSucc = PfUtilClient.isSuccess();
        //保存后处理：
        if (strRetKey != null && bIsSucc) {
          /*刷新保存成功单据*/
          /*脚本 N_23_SAVEBASE 中设定新VO*/
          ArriveorderVO voTmp = (ArriveorderVO) aryRet.get(1);
          if (voTmp == null)
            throw new BusinessException(m_lanResTool.getStrByID("40040301","UPP40040301-000178")/*@res "保存数据成功，但刷新数据时出错，请稍后再试！"*/);
          /*增加保存成功单据到待显示单据缓存末尾*/
          appArriveorderVOSaved(voTmp);
        }
      }
      onEndCreate();
      m_pushSaveVOs = null;
      showHintMessage(m_lanResTool.getStrByID("40040301","UPP40040301-000179")/*@res "保存成功,转单结束"*/);
    }
    showHintMessage(m_lanResTool.getStrByID("common","4004COMMON000000036")/*@res "快速收货完成"*/);
  } catch (Exception e) {
    reportException(e);
    showHintMessage(e.getMessage());
  }

}
/**
 * 检查表头必输项是否非空：库存组织等。
 * <p>
 * <b>examples:</b>
 * <p>
 * 使用示例
 * <p>
 * <b>参数说明</b>
 * @param voaArr 到货单
 * <p>
 * @author czp
 * @time 2007-10-16 上午10:24:33
 */
private void verifyNotNullFields(ArriveorderVO[] voaArr, String strOrderBillCode) throws Exception{  
  ArrayList<String> listOrderBid = new ArrayList<String>();
  ArriveorderHeaderVO head = null;
  ArriveorderItemVO[] items = null;
  for(ArriveorderVO voArrCurr : voaArr){
    head = voArrCurr.getHeadVO();
    if(PuPubVO.getString_TrimZeroLenAsNull(head.getCstoreorganization()) == null){
      items = voArrCurr.getBodyVo();
      for(ArriveorderItemVO item : items){
        listOrderBid.add(item.getCorder_bid());
      }
    }
  }
  if(listOrderBid.size() > 0){
    Object[][] oaRet = PubHelper.queryArrayValue("po_order_b", 
        "corder_bid", new String[]{"crowno"}, 
        listOrderBid.toArray(new String[listOrderBid.size()]));
    String strErrRowNo = "";
    for(int i=0; i<oaRet.length; i++){
      strErrRowNo += oaRet[i][0];
      strErrRowNo += ",";
    }
    strErrRowNo = strErrRowNo.substring(0,strErrRowNo.length()-1);
    //
    throw new Exception(NCLangRes.getInstance().getStrByID(
        "40040301", "UPP40040301-000288", null, new String[]{strOrderBillCode, strErrRowNo})
        /*"快速收货失败。原因，订单号为[{0}]的订单下列订单行号收货库存组织为空：{1}"*/);
  }
  
  
  BillItem[] headItems = getBillCardPanel().getHeadShowItems();

  //获得非空单据头Item的Key
  String[] headNotNullKeys = null;
  Vector v1 = new Vector();
  for (int i = 0; headItems != null && i < headItems.length; i++) {
    if (headItems[i].isNull()) {
      v1.add(headItems[i].getKey());
    }
  }
  if (v1.size() > 0) {
    headNotNullKeys = new String[v1.size()];
    v1.copyInto(headNotNullKeys);
  }

  //验证单据头非空项的值是否为空
  String hErrorMessage = "";
  String value = null;
  for (int i = 0; headNotNullKeys != null && i < headNotNullKeys.length; i++) {
    for(ArriveorderVO voArrCurr : voaArr){
      value = PuPubVO.getString_TrimZeroLenAsNull(voArrCurr.getHeadVO().getAttributeValue(headNotNullKeys[i]));
      String name = getBillCardPanel().getHeadItem(headNotNullKeys[i]).getName();
      if (value == null || value.trim().equals("")) {
        hErrorMessage += "[" + name + "],";
      }
    }
  }
  if (!hErrorMessage.equals(""))
    hErrorMessage = hErrorMessage.substring(0, hErrorMessage.length() - 1);


  //如果提示信息不为空则抛出异常.
  if (!hErrorMessage.equals("")) {
    String errorMessage = "快速收货失败:"+ nc.ui.ml.NCLangRes.getInstance().getStrByID("scmpub","UPPscmpub-000444")/*@res "下列属性的值不能为空:"*/;
    if (!hErrorMessage.equals(""))
      errorMessage += "\n" + nc.ui.ml.NCLangRes.getInstance().getStrByID("scmpub","UPPscmpub-000445")/*@res "\n表头:"*/ + hErrorMessage;

    throw new BusinessException(errorMessage);
  }
}
/**
 * @功能：刷新数据（卡片列表界面）
 * @作者：晁志平
 * 创建日期：(2001-6-20 13:35:04)
 * @param:<|>
 * @return:
 * @exception:
 * @see;
 * @since:
 *
 */
private void onRefresh() {
  showHintMessage(m_lanResTool.getStrByID("40040301","UPP40040301-000168")/*@res "正在查询单据..."*/);
  //更新数据
  getArriveVOsFromDB();
  /*初始化浏览单据位置(考虑到删除数据)*/
  setDispIndex(0);
  //显示数据、处理按钮状态
  if (getStateStr().equals("到货浏览") || getStateStr().equals("初始化")) {
    try {
      loadDataToCard();
    } catch (Exception e) {
      SCMEnv.out("加载单据时报错");
    }
  } else if (getStateStr().equals("到货列表")) {
    loadDataToList();
    //默认显示第一张
    if (getCacheVOs() != null && getCacheVOs().length > 0) {
      getBillListPanel().getHeadTable().setRowSelectionInterval(
        getDispIndex(),
        getDispIndex());
      getBillListPanel().getHeadBillModel().setRowState(
        getDispIndex(),
        BillModel.SELECTED);
      setListBodyData(getDispIndex());
    }
  }
  //
  setButtonsState();
  //
  if (getCacheVOs() == null || getCacheVOs().length <= 0) {
    showHintMessage(m_lanResTool.getStrByID("40040301","UPP40040301-000169")/*@res "查询完成:没有符合条件的单据"*/);
  } else {
    showHintMessage(m_lanResTool.getStrByID("40040301","UPP40040301-000170")/*@res "查询完成:查询到"*/ + getCacheVOs().length + m_lanResTool.getStrByID("40040301","UPP40040301-000180")/*@res "张单据"*/);
  }

  if (m_dlgSerialAllocation != null) {
	  	setM_alSerialData(null,null);
	  }

  showHintMessage(m_lanResTool.getStrByID("common","UCH007")/*@res "刷新成功"*/);
}

/**
 * 如果此次到货数量、单价均与订单匹配，则采用订单本币金额(不处理委外)。
 * <p>
 * <b>examples:</b>
 * <p>
 * 使用示例
 * <p>
 * <b>参数说明</b>
 * <p>
 * @author czp
 * @time 2007-5-11 下午02:28:42
 */
private void dealNmoneyBeforeSave(){
  ArrayList<String> listOrderBid = new ArrayList<String>();
  ArrayList<UFDouble> listNum = new ArrayList<UFDouble>();
  ArrayList<UFDouble> listPrice = new ArrayList<UFDouble>();
  int iLen = getBillCardPanel().getRowCount();  
  String strOrderBid = null;
  UFDouble ufdNum = null;
  UFDouble ufdPrice = null;
  BillModel bm = getBillCardPanel().getBillModel();
  for(int i=0; i<iLen; i++){
    strOrderBid = PuPubVO.getString_TrimZeroLenAsNull(bm.getValueAt(i, "corder_bid"));
    if(strOrderBid == null){
      continue;
    }
    ufdNum = PuPubVO.getUFDouble_NullAsZero(bm.getValueAt(i, "narrvnum"));
    if(ufdNum == null){
      continue;
    }
    ufdPrice = PuPubVO.getUFDouble_NullAsZero(bm.getValueAt(i, "nprice"));
    if(ufdPrice == null){
      continue;
    }
    //一行订单只需返回一个单价
    if(listOrderBid.contains(strOrderBid)){
      continue;
    }
    listOrderBid.add(strOrderBid);
    listNum.add(ufdNum);
    listPrice.add(ufdPrice);
  }
  if(listOrderBid.size() == 0){
    return;
  }
  int iSize = listOrderBid.size(); 
  String[] saBid = (String[]) listOrderBid.toArray(new String[iSize]);
  UFDouble[] uaNum = (UFDouble[]) listNum.toArray(new UFDouble[iSize]);
  UFDouble[] uaNprice = (UFDouble[]) listPrice.toArray(new UFDouble[iSize]);
  IQueryForIc srvQueryPrice = (IQueryForIc) NCLocator.getInstance().lookup(IQueryForIc.class.getName());
  UFDouble[] uaOrderNmoney = null;
  try{
    uaOrderNmoney = srvQueryPrice.queryForIcPrice(getCorpPrimaryKey(), saBid, uaNum, uaNprice);
  }catch(BusinessException e){
    SCMEnv.out(e.getMessage());
  }
  if(uaOrderNmoney == null){
    return;
  }
  //哈希单价
  HashMap<String, UFDouble> mapOrderNmoney = new HashMap<String, UFDouble>();
  for(int i=0; i<iSize; i++){
    if(uaOrderNmoney[i] == null){
      continue;
    }
    mapOrderNmoney.put(saBid[i], uaOrderNmoney[i]);
  }
  //回写到单据模板
  for(int i=0; i<iLen; i++){
    strOrderBid = PuPubVO.getString_TrimZeroLenAsNull(bm.getValueAt(i, "corder_bid"));
    if(strOrderBid == null){
      continue;
    }
    ufdNum = PuPubVO.getUFDouble_NullAsZero(bm.getValueAt(i, "narrvnum"));
    if(ufdNum == null){
      continue;
    }
    ufdPrice = PuPubVO.getUFDouble_NullAsZero(bm.getValueAt(i, "nprice"));
    if(ufdPrice == null){
      continue;
    }
    if(!mapOrderNmoney.containsKey(strOrderBid)){
      continue;
    }
    bm.setValueAt(mapOrderNmoney.get(strOrderBid), i, "nmoney");
    //
    if(bm.getRowState(i) == BillModel.UNSTATE){
      bm.setRowState(i, BillModel.MODIFICATION);
    }
  }
}
/**
 * @功能：保存修改结果
 */
private boolean onSave() {
	getBillCardPanel().getBodyTabbedPane().setSelectedIndex(0);
  nc.vo.scm.pu.Timer timer= new nc.vo.scm.pu.Timer();
  timer.start("采购到货保存操作onSave开始");

  showHintMessage(m_lanResTool.getStrByID("40040301","UPP40040301-000181")/*@res "正在保存单据..."*/);
  //终止编辑
  getBillCardPanel().stopEditing();
  
  // 增加对校验公式的支持,错误显示由UAP处理 since v501
  if ( ! getBillCardPanel().getBillData().execValidateFormulas()){
      return false;
  }  
  //since v51 : 保存前金额处理:如果此次到货数量、单价均与订单匹配，则采用订单本币金额
  //dealNmoneyBeforeSave();
  
  //用于保存的VO
  ArriveorderVO saveVO = null;
  //单据模板中显示的VO(转入修改及到货修改的结果)
  ArriveorderVO newvo = (ArriveorderVO) getBillCardPanel().getBillValueVO(ArriveorderVO.class.getName(), ArriveorderHeaderVO.class.getName(), ArriveorderItemVO.class.getName());
  ArriveorderVO oldvo = (ArriveorderVO) getCacheVOs()[getDispIndex()];
  ((ArriveorderHeaderVO) newvo.getParentVO()).setPk_corp(getClientEnvironment().getCorporation().getPrimaryKey());
  ((ArriveorderHeaderVO) oldvo.getParentVO()).setPk_corp(getClientEnvironment().getCorporation().getPrimaryKey());

  if(!m_bSaveMaker && !isRevise) ((ArriveorderHeaderVO) newvo.getParentVO()).setCoperator(getClientEnvironment().getUser().getPrimaryKey());

  //检查数据
  if(!chechDataBeforeSave(newvo,oldvo))
    return false;
  
  //流程保存方法的参数
  //aryPara0 : 0，公司主键；1，修改前的到货单；2，当前单据编辑状态；3.修改后的VO（界面VO）
  //aryPara1 : 0，是否有用户交互；1，上次操作时的订单行ID及订单数量

  ArrayList aryPara = new ArrayList(2);
  ArrayList aryPara0 = new ArrayList();
  
  aryPara0.add(getCorpPrimaryKey());
  aryPara0.add(null);
  aryPara0.add(getStateStr().equals(OrderPubVO.RC_OUTMODIFY_STATE/*转入修改*/) ? "insert" : "update");
  //到货数量精度*(-1) 传入后台作为订单数量的 power
  newvo.setDigitsNumPower(CPurchseMethods.getMeasDecimal(getCorpPrimaryKey()) * (-1));
  aryPara0.add(null);
  aryPara.add(aryPara0);
  aryPara.add(null);
  aryPara.add(new Integer(0));
  aryPara.add(new String("cvendormangid"));
  InformationCostVO[] infoCostVOs = (InformationCostVO[])getBillCardPanel().getBillData().getBodyValueVOs("jj_scm_informationcost", InformationCostVO.class.getName());//add by QuSida 得到费用信息VO
  
  if (getStateStr().equals(OrderPubVO.RC_ARRIVEMODIFY_STATE/*到货修改*/)) {
    //用于修改保存的VO
    saveVO = (ArriveorderVO) getBillCardPanel().getBillValueChangeVO(ArriveorderVO.class.getName(), ArriveorderHeaderVO.class.getName(), ArriveorderItemVO.class.getName());
    if(!m_bSaveMaker && !isRevise) ((ArriveorderHeaderVO) saveVO.getParentVO()).setCoperator(getClientEnvironment().getUser().getPrimaryKey());

    //在浏览修改状态下（转入状态不用）
    //如果有被分配库位的行 或 货位分配过程中存在未补分配而删除的行,则加回来，一并传给后端处理(它们将被从数据库中删除)
    if (vDelNoSplitted.size() > 0) {
      for (int i = 0; i < vDelNoSplitted.size(); i++) {
        v_DeletedItems.addElement(vDelNoSplitted.elementAt(i));
      }
    }
    if (v_DeletedItems.size() > 0) {
      ArriveorderItemVO[] allItems = new ArriveorderItemVO[v_DeletedItems.size() + saveVO.getChildrenVO().length];
      if (v_DeletedItems.size() > 0) {
        for (int i = 0; i < saveVO.getChildrenVO().length; i++) {
          v_DeletedItems.addElement(((ArriveorderItemVO[]) saveVO.getChildrenVO())[i]);
        }
        v_DeletedItems.copyInto(allItems);
      }
      saveVO.setChildrenVO(allItems);
    }
    saveVO.setOprType(VOStatus.UPDATED);
    saveVO.setUpBillType(((ArriveorderItemVO) newvo.getChildrenVO()[0]).getCupsourcebilltype());
    //赋操作员
    if(!isRevise){
    	saveVO.setCoperatorid(getOperatorId());
    }
    //加锁需要
    saveVO.getParentVO().setAttributeValue("cuserid", getOperatorId());
    //设置自由状态
    if (!isRevise // 送审后，修改保存，状态不变，应仍审批进行中，billstatus = 2
            && (saveVO.getHeadVO().getIbillstatus() == null || saveVO.getHeadVO().getIbillstatus().intValue() != 2)) {
    	saveVO.getHeadVO().setIbillstatus(new Integer(0));
    }
    //add by QuSida 2010-9-11 (佛山骏杰) --- begin
    //function:修改费用信息
        String cbillid = saveVO.getHeadVO().getPrimaryKey();
        if(infoCostVOs!=null&&infoCostVOs.length!=0){
        	for (int i = 0; i < infoCostVOs.length; i++) {
				infoCostVOs[i].setCbillid(cbillid);
			}
        	try {
    			JJPuScmPubHelper.updateSmartVOs(infoCostVOs,cbillid);
    		} catch (Exception e) {
    			// TODO Auto-generated catch block
    			SCMEnv.out(e);
    		}
        }
    	
    
    //add by QuSida 2010-9-11 (佛山骏杰) --- end
  } else {
    //用于新增的VO
    saveVO = newvo;
    //重置表头表体状态(新增)
     ((ArriveorderHeaderVO) saveVO.getParentVO()).setStatus(VOStatus.NEW);
    //制单人
     ((ArriveorderHeaderVO) saveVO.getParentVO()).setCoperator(getOperatorId());
    for (int i = 0; i < saveVO.getChildrenVO().length; i++) {
      ((ArriveorderItemVO[]) saveVO.getChildrenVO())[i].setStatus(VOStatus.NEW);
    }
    saveVO.setOprType(VOStatus.NEW);
    //流程回写用：上层来源单据类型
    saveVO.setUpBillType(((ArriveorderItemVO) oldvo.getChildrenVO()[0]).getCupsourcebilltype());
    //赋操作员
    saveVO.setCoperatorid(getOperatorId());
    //判断并发的公用方法 PubDMO.checkVoNoChanged() 需要
    saveVO.getParentVO().setAttributeValue("cuserid", getOperatorId());
  }
  //处理非自检参照--备注
  UIRefPane nRefPanel = (UIRefPane) getBillCardPanel().getHeadItem("vmemo").getComponent();
  UITextField vMemoField = nRefPanel.getUITextField();
  String vmemo = vMemoField.getText();
  ((ArriveorderHeaderVO) saveVO.getParentVO()).setVmemo(vmemo);
  //处理非自检参照--退货理由
  if (getBillCardPanel().getHeadItem("vbackreasonh") != null) {
    UIRefPane refPanel = (UIRefPane) getBillCardPanel().getHeadItem("vbackreasonh").getComponent();
    UITextField txtBack = refPanel.getUITextField();
    String strBack = txtBack.getText();
    ((ArriveorderHeaderVO) saveVO.getParentVO()).setVbackreasonh(strBack);
  }
  timer.addExecutePhase(m_lanResTool.getStrByID("40040301","UPP40040301-000254")/*@res "保存前的准备操作"*/);
  //为判断是否可修改、作废其他人单据
  ((ArriveorderHeaderVO) saveVO.getParentVO()).setCoperatoridnow(getOperatorId());
  String strRetKey = null;
  boolean isCycle = true;
  //修改时：拼接未改变的表体VO(说明参见getSaveVO()方法)
  if (getStateStr().equals("到货修改")) {
    saveVO = getSaveVO(saveVO);
    timer.addExecutePhase("getSaveVO");
  }
  //是否需要回退单据号:新增且手工录入单据号
  if (getStateStr().equals("转入修改")) {
    if (saveVO.getParentVO() != null && saveVO.getParentVO().getAttributeValue("varrordercode") != null && saveVO.getParentVO().getAttributeValue("varrordercode").toString().trim().length() > 0) {
    }
  }
  doCycle : while (isCycle) {
    isCycle = false;
    try {
      setReWriteData(saveVO,newvo,oldvo);
   
      ArrayList aryRet = (ArrayList) PfUtilClient.processAction("SAVEBASE", ScmConst.PO_Arrive, ClientEnvironment.getInstance().getDate().toString(), saveVO, aryPara);
      
    //add by QuSida 2010-8-31 (佛山骏杰)  --- begin
	//function:当订单保存成功后,将费用信息VO保存到数据库中	
      if(infoCostVOs!=null&&infoCostVOs.length!=0){

			//将单据主键射入vo数组中
    	  String pk_bill = (String)aryRet.get(0);
		//String pk_bill = saveVO.getParentVO().getPrimaryKey();
			for (int i = 0; i < infoCostVOs.length; i++) {
				infoCostVOs[i].setCbillid(pk_bill);
			}
		JJPuScmPubHelper.updateSmartVOs(infoCostVOs,pk_bill);
		}
    //add by QuSida 2010-8-31 (佛山骏杰)  --- end
      timer.addExecutePhase("执行SAVE脚本");
      // 处理并发，刷新缓存VO的TS
      refreshVOTs((ArriveorderVO)aryRet.get(1));
      timer.addExecutePhase("刷新缓存VO的TS");
      strRetKey = aryRet == null ? null : (String) aryRet.get(0);
      boolean bIsSucc = PfUtilClient.isSuccess();
      //保存后处理：
      if (getStateStr().equals("到货修改")) {
        if (strRetKey != null && bIsSucc) {
          setM_strState("到货浏览");
          //脚本中设定新VO
          getCacheVOs()[getDispIndex()] = (ArriveorderVO) aryRet.get(1);
          if (getCacheVOs()[getDispIndex()] == null)
            throw new BusinessException(m_lanResTool.getStrByID("40040301","UPP40040301-000178")/*@res "保存数据成功，但刷新数据时出错，请稍后再试！"*/);
          //ArriveorderBO_Client.findByPrimaryKey(oldvo.getParentVO().getPrimaryKey());
          //顺序不能置换
          setButtonsState();
          checkVprocessbatch(new ArriveorderVO[]{getCacheVOs()[getDispIndex()]});
          loadDataToCard();
          getBillCardPanel().setEnabled(false);
          showHintMessage(m_lanResTool.getStrByID("SCMCOMMON","UPPSCMCommon-000006")/*@res "保存成功"*/);
          updateUI();
        }
      } else if (getStateStr().equals("转入修改")) {
        if (strRetKey != null && bIsSucc) {
          //从当前未保存单据中删除保存成功单据
          delArriveorderVOSaved();
          //已更新ts的VO
          ArriveorderVO voTmp = (ArriveorderVO) aryRet.get(1);
          if (voTmp == null)
            throw new BusinessException(m_lanResTool.getStrByID("40040301","UPP40040301-000178")/*@res "保存数据成功，但刷新数据时出错，请稍后再试！"*/);
          
//          refreshVoFieldsByKey(voTmp,strRetKey);
          checkVprocessbatch(new ArriveorderVO[]{voTmp});
          //增加保存成功单据到待显示单据缓存末尾
          appArriveorderVOSaved(voTmp);
          //根据转单是否结束作不同处理
          if (getCacheVOs() != null) {
            displayArrBillListPanelNew();
            showHintMessage(m_lanResTool.getStrByID("40040301","UPP40040301-000182")/*@res "保存成功,继续转单"*/);
          } else {
            onEndCreate();
            showHintMessage(m_lanResTool.getStrByID("40040301","UPP40040301-000179")/*@res "保存成功,转单结束"*/);
          }
        }
      }
      //ArriveorderVO voTmp = (ArriveorderVO) aryRet.get(1);
      /************记录业务日志*************//*
       * 连接数优化，插日志操作放到后台
		Operlog operlog=new Operlog();
		voTmp.getOperatelogVO().setOpratorname(nc.ui.pub.ClientEnvironment.getInstance().getUser().getUserName());
		voTmp.getOperatelogVO().setCompanyname(nc.ui.pub.ClientEnvironment.getInstance().getCorporation().getUnitname());
		voTmp.getOperatelogVO().setOperatorId(nc.ui.pub.ClientEnvironment.getInstance().getUser().getPrimaryKey());
		operlog.insertBusinessExceptionlog(voTmp, "保存", "保存", nc.vo.scm.funcs.Businesslog.MSGMESSAGE, nc.vo.scm.pu.BillTypeConst.PO_ARRIVE,
				nc.ui.sm.cmenu.Desktop.getApplet().getParameter("USER_IP"));
		*//************记录业务日志* end ************/
      timer.addExecutePhase("保存后处理");
      setButtonsRevise();
      
      //回写
//      getBillCardPanel().getBillData().setBodyValueVO("jj_scm_informationcost", infoCostVOs);
      getBillCardPanel().getBillModel("jj_scm_informationcost").setBodyDataVO(infoCostVOs);
      
      showHintMessage(m_lanResTool.getStrByID("common","UCH005")/*@res "保存成功"*/);
    } catch (Exception e) {
//      //回退单据号
//      if (isBackCode) {
//        SCMEnv.out("回退单据号开始[ArriveUI]...");
//        try {
//          PubHelper.returnBillCode(newvo);
//        } catch (Exception ex) {
//          SCMEnv.out("回退单据号异常结束[ArriveUI]");
//        }
//        SCMEnv.out("回退单据号正常结束[ArriveUI]");
//      }
      //处理回写采购订单超容差提示情况
      if (e instanceof RwtRcToPoException) {
        //请购单累计数量超出提示
        int iRet = showYesNoMessage(e.getMessage()) ;
        if (iRet==MessageDialog.ID_YES) {
          //继续循环
          isCycle = true ;
          //是否用户确认
          saveVO.setUserConfirm(true);
        } else {
          return false;
        }
      }
      //处理回写委外订单超容差提示情况
      else if (e instanceof RwtRcToScException) {
        //到货累计数量超出提示
        int iRet = showYesNoMessage(e.getMessage()) ;
        if (iRet==MessageDialog.ID_YES) {
          //继续循环
          isCycle = true ;
          //是否用户确认
          saveVO.setUserConfirm(true);
        } else {
          return false;
        }
      }else if (e instanceof BusinessException || e instanceof java.rmi.RemoteException || e instanceof ValidationException) {
        MessageDialog.showErrorDlg(this, m_lanResTool.getStrByID("SCMCOMMON","UPPSCMCommon-000059")/*@res "错误"*/, e.getMessage());
      } else if (
        e.getMessage() != null
          && (e.getMessage().indexOf(m_lanResTool.getStrByID("40040301","UPP40040301-000250")/*@res "到货"*/) >= 0
            || e.getMessage().indexOf(m_lanResTool.getStrByID("40040301","UPP40040301-000212")/*@res "订单"*/) >= 0
            || e.getMessage().indexOf(m_lanResTool.getStrByID("40040301","UPT40040301-000025")/*@res "退货"*/) >= 0
            || e.getMessage().indexOf(m_lanResTool.getStrByID("40040301","UPP40040301-000207")/*@res "收货"*/) >= 0
            || e.getMessage().indexOf(m_lanResTool.getStrByID("40040301","UPP40040301-000251")/*@res "单据"*/) >= 0
            || e.getMessage().indexOf(m_lanResTool.getStrByID("40040301","UPP40040301-000252")/*@res "容差"*/) >= 0
            || e.getMessage().indexOf(m_lanResTool.getStrByID("40040301","UPP40040301-000253")/*@res "号"*/) >= 0
            || e.getMessage().indexOf("BusinessException") >= 0
            || e.getMessage().indexOf("RemoteException") >= 0)) {
        MessageDialog.showErrorDlg(this, m_lanResTool.getStrByID("SCMCOMMON","UPPSCMCommon-000059")/*@res "错误"*/, e.getMessage());
      } else{
        MessageDialog.showErrorDlg(this, m_lanResTool.getStrByID("SCMCOMMON","UPPSCMCommon-000059")/*@res "错误"*/, m_lanResTool.getStrByID("40040301","UPP40040301-000183")/*@res "系统异常，保存失败"*/);
      }
      showHintMessage(m_lanResTool.getStrByID("SCMCOMMON","UPPSCMCommon-000010")/*@res "保存失败"*/);
    }
  }
  timer.showAllExecutePhase("采购到货保存操作onSave操作结束") ;
  
  return true;
}
/**
 * 刷新保存成功单据(因为保存及审批操作中需要，要刷新审批日期和审批人,单据状态，TS，制单时间，审批时间，最后修改时间，表体时间戳)
 * @param vo
 * @param strKey
 * @author czp
 * @date 2006-05-18
 */
private void refreshVoFieldsByKey(ArriveorderVO vo,String strKey) throws Exception{
  //
  ArrayList arrRet = ArriveorderHelper.queryForSaveAudit(strKey);
  ((ArriveorderHeaderVO)vo.getParentVO()).setDauditdate((UFDate)arrRet.get(0));
  ((ArriveorderHeaderVO)vo.getParentVO()).setCauditpsn((String)arrRet.get(1));
  ((ArriveorderHeaderVO)vo.getParentVO()).setIbillstatus((Integer)arrRet.get(2));
  ((ArriveorderHeaderVO)vo.getParentVO()).setTs((String)arrRet.get(3));
  ((ArriveorderHeaderVO)vo.getParentVO()).setTmaketime((UFDateTime) arrRet.get(4));
  ((ArriveorderHeaderVO)vo.getParentVO()).setTaudittime((UFDateTime) arrRet.get(5));
  ((ArriveorderHeaderVO)vo.getParentVO()).setTlastmaketime((UFDateTime) arrRet.get(6));
  
  ArriveorderItemVO[] itemVOS = (ArriveorderItemVO[])vo.getChildrenVO();
  Map btsHP = (HashMap)arrRet.get(7);
  for(ArriveorderItemVO item:itemVOS){
	  if(btsHP.get(item.getPrimaryKey())!=null)
		  item.setTs(btsHP.get(item.getPrimaryKey()).toString());
  }
}
/**
 * @功能：选定所有到货单
 * @作者：晁志平
 * 创建日期：(2001-6-8 14:21:35)
 * @param:<|>
 * @return:
 * @exception:
 * @see;
 * @since:
 *
 */
private void onSelectAll() {
  int iLen = getBillListPanel().getHeadBillModel().getRowCount();
  getBillListPanel().getHeadTable().setRowSelectionInterval(0, iLen - 1);
  for (int i = 0; i < iLen; i++) {
    getBillListPanel().getHeadBillModel().setRowState(i, BillModel.SELECTED);
  }
  //设置按钮状态
  setButtonsList();
  //
  showHintMessage(m_lanResTool.getStrByID("common","4004COMMON000000033")/*@res "全部选中成功"*/);
}

/**
 * @功能：取消所有选定的到货单表行
 * @作者：晁志平
 * 创建日期：(2001-6-8 14:22:12)
 * @param:<|>
 * @return:
 * @exception:
 * @see;
 * @since:
 *
 */
private void onSelectNo() {
  int iLen = getBillListPanel().getHeadBillModel().getRowCount();
  getBillListPanel().getHeadTable().removeRowSelectionInterval(0, iLen - 1);
  for (int i = 0; i < iLen; i++) {
    getBillListPanel().getHeadBillModel().setRowState(i, BillModel.NORMAL);
  }
  //设置按钮状态
  setButtonsList();
  //
  showHintMessage(m_lanResTool.getStrByID("common","4004COMMON000000034")/*@res "全部取消成功"*/);
}

/**
 * 功能:执行弃审
 */
private void onUnAudit(ButtonObject bo) {
  showHintMessage(m_lanResTool.getStrByID("40040301","UPP40040301-000186")/*@res "正在弃审..."*/);
  ArriveorderVO vo = getCacheVOs()[getDispIndex()];  
  //设置当前操作员
  vo.getHeadVO().setCoperatoridnow(nc.ui.pub.ClientEnvironment.getInstance().getUser().getPrimaryKey());
  //回退审批人及审批日期哈希表，审批失败时用到
  String strPsnOld = vo.getHeadVO().getCauditpsn();
  //设置前审批人
  vo.getHeadVO().setCauditpsnold(strPsnOld);
  UFDate dateAuditOld = vo.getHeadVO().getDauditdate();
  //
  try {
    //弃审
    PfUtilClient.processBatchFlow(
      null,
      "UNAPPROVE"+ getClientEnvironment().getUser().getPrimaryKey(),
      ScmConst.PO_Arrive,
      getClientEnvironment().getDate().toString(),
      new ArriveorderVO[] { vo },
      null);
    
    if (!PfUtilClient.isSuccess()) {
      //操作失败，恢复审批人及审批日期
      vo.getHeadVO().setCauditpsn(strPsnOld);
      vo.getHeadVO().setDauditdate(dateAuditOld);
      //
      showHintMessage(m_lanResTool.getStrByID("40040301","UPP40040301-000187")/*@res "弃审失败(平台调用出现异常)"*/);
      return;
    }
    //弃审成功后刷新
    refreshVoFieldsByKey(vo,vo.getParentVO().getPrimaryKey());

    getCacheVOs()[getDispIndex()] = vo;
    //加载单据
    try {
    	getBillCardPanel().setBillValueVO(getCacheVOs()[getDispIndex()]);
    	lightRefreshUI();
    	setHeadValueByKey("vmemo", (String)getCacheVOs()[getDispIndex()].getParentVO().getAttributeValue("vmemo"));
  	  //function:查询相关费用信息
  	  String pk = (String)getCacheVOs()[getDispIndex()].getHeadVO().getAttributeValue("carriveorderid");
  		String sql = "cbillid = '"+pk+"' and dr = 0";
  		InformationCostVO[] vos = null;

  		 try {
  			vos = (InformationCostVO[])JJPuScmPubHelper.querySmartVOs(InformationCostVO.class, null, sql);
  		} catch (Exception e) {
  			// TODO Auto-generated catch block
  			SCMEnv.out(e);
  		}
  	if(vos!=null&&vos.length!=0){
  		 getBillCardPanel().getBillModel("jj_scm_informationcost").setBodyDataVO(vos);
  		 getBillCardPanel().getBillModel("jj_scm_informationcost").execLoadFormula();
  	}else{
			 //20101014-11:51 MeiChao 如果费用信息为空,则清除费用信息页签历史数据.
			getBillCardPanel().getBillModel("jj_scm_informationcost").setBodyDataVO(null); 
		 }
    } catch (Exception e) {
      showHintMessage(m_lanResTool.getStrByID("40040301","UPP40040301-000188")/*@res "弃审成功,但加载单据时出现异常,请刷新界面再进行其它操作"*/);
    }
    //刷新按钮状态
    setButtonsState();
    setButtonsRevise();

    //
    showHintMessage(m_lanResTool.getStrByID("common","UCH011")/*@res "弃审成功"*/);
  } catch (Exception e) {
    //操作失败，恢复审批人及审批日期
    vo.getHeadVO().setCauditpsn(strPsnOld);
    vo.getHeadVO().setDauditdate(dateAuditOld);
    //
    showHintMessage(m_lanResTool.getStrByID("40040301","UPP40040301-000189")/*@res "出现异常,弃审失败"*/);
    SCMEnv.out(e);
    if (e instanceof java.rmi.RemoteException || e instanceof BusinessException){
      MessageDialog.showErrorDlg(this,m_lanResTool.getStrByID("40040301","UPP40040301-000099")/*@res "报错"*/,e.getMessage());
    }
  }
}

/**
 * 作者：汪维敏
 * 功能：刷新表体行或者TS缓存的TS，用于处理并发
 * 参数：
 * 返回：
 * 例外：
 * 日期：(2004-4-1 10:55:04)
 */
private void refreshVOTs(ArriveorderVO vo) {
  if (m_hTS == null)
    return;
  if (vo == null)
    return;
  ArriveorderItemVO[] items = (ArriveorderItemVO[]) vo.getChildrenVO();
  if (items == null || items.length == 0)
    return;
  String sUpSourceBillType = items[0].getCupsourcebilltype();
  if (sUpSourceBillType == null || sUpSourceBillType.trim().length() == 0)
    return;
  int size = items.length;
  //刷新缓存VO的表体TS,处理并发
  try {
    if(items[0].getCupsourcebilltype().equals("21")){
      String[] sID = new String[size];
      for(int i = 0;i < size;i++){
        sID[i] = items[i].getCorder_bid();
      }
      //刷新TS
      HashMap hTs = ArriveorderHelper.queryNewTs(sID);
      for(int i = 0;i < size;i++){
        Object[] ob = (Object[])hTs.get(items[i].getCorder_bid());
        if(ob != null && ob[0] != null)
          items[i].setTsbup(ob[0].toString());
      }
    }
    ArriveorderVO[] vos = getCacheVOs();
    //用于推式保存
    if (m_pushSaveVOs != null && m_pushSaveVOs.length > 0)
      vos = m_pushSaveVOs;
    if (vos == null || vos.length == 0)
      return;

    String sUpSourceBTs = null;
    String sUpsourceRowid = null;

    //将新TS放入TS缓存
    for (int i = 0; i < size; i++) {
      sUpSourceBTs = items[i].getTsbup();
      sUpsourceRowid = items[i].getCupsourcebillrowid();
      String sTs = null;
      if (m_hTS.containsKey(sUpsourceRowid)) {
        sTs = (String) m_hTS.get(sUpsourceRowid);
        if (sTs != null && sTs.trim().length() > 0 && !sTs.equals(sUpSourceBTs)) {
          m_hTS.remove(sUpsourceRowid);
          m_hTS.put(sUpsourceRowid, sUpSourceBTs);
        }
      } else {
        m_hTS.put(sUpsourceRowid, sUpSourceBTs);
      }
    }

    //用TS缓存去更新VO缓存
    for (int i = 0; i < vos.length; i++) {
      ArriveorderItemVO[] itemVOs = (ArriveorderItemVO[]) vos[i].getChildrenVO();
      for (int j = 0; j < itemVOs.length; j++) {
        sUpSourceBTs = itemVOs[j].getTsbup();
        sUpsourceRowid = itemVOs[j].getCupsourcebillrowid();
        String sTs = (String) m_hTS.get(sUpsourceRowid);
        if (sTs != null && sTs.trim().length() > 0 && !sTs.equals(sUpSourceBTs)) {
          itemVOs[j].setTsbup(sTs);
        }
      }
    }
  } catch (Exception e) {
    reportException(e);
  }
}

public void setBillVO(nc.vo.pub.AggregatedValueObject vo){
  UIRefPane refPane = null;
  BillModel bm = getBillCardPanel().getBillModel();
  ArriveorderVO VO = (ArriveorderVO) vo;
  
  //设置VO到卡片单据模板
  getBillCardPanel().setBillValueVO(VO);
  //处理基础数据被作废(DR!=0)或冻结时不能正确显示名称问题
  loadBDData();
  //处理单据模板自定义项
  ArriveorderHeaderVO voHead = (ArriveorderHeaderVO) VO.getParentVO();
  String[] saKey = new String[] { "vdef1", "vdef2", "vdef3", "vdef4", "vdef5", "vdef6", "vdef7", "vdef8", "vdef9", "vdef10","vdef11", "vdef12", "vdef13", "vdef14", "vdef15", "vdef16", "vdef17", "vdef18", "vdef19", "vdef20" };
  int iLen = saKey.length;
  for (int i = 0; i < iLen; i++) {
    voHead.setAttributeValue(saKey[i],getBillCardPanel().getHeadItem(saKey[i]).getValueObject());
  }
    //关闭合计开关
  boolean  bOldNeedCalc = bm.isNeedCalculate() ; 
  bm.setNeedCalculate(false) ;
  //执行加载公式    
  bm.execLoadFormula();   
    //打开合计开关
  bm.setNeedCalculate(bOldNeedCalc) ;
  getBillCardPanel().updateValue();
  
  
  //显示表头备注
  setHeadValueByKey("vmemo",(String) VO.getParentVO().getAttributeValue("vmemo"));
  
  //显示表头退货理由
  if (getBillCardPanel().getHeadItem("vbackreasonh") != null) {
	  setHeadValueByKey("vbackreasonh",(String) VO.getParentVO().getAttributeValue("vbackreasonh"));
  }
  //缓存替换件参照构造子参数 {到货单行或订单行ID = cmangid }
  Vector vCmangids = new Vector();
  String strCmangid = null;
  m_hBillIDsForCmangids = new Hashtable();
  String strKey = (getStateStr().equals("转入修改")) ? "corder_bid" : "carriveorder_bid";
  for (int i = 0; i < bm.getRowCount(); i++) {
    strCmangid = (String) bm.getValueAt(i, "cmangid");
    if (strCmangid != null && strCmangid.trim().length() > 0 && !vCmangids.contains(strCmangid))
      vCmangids.addElement(strCmangid);
    if (bm.getValueAt(i, strKey) == null)
      continue;
    if (!m_hBillIDsForCmangids.containsKey(bm.getValueAt(i, strKey)))
      m_hBillIDsForCmangids.put(bm.getValueAt(i, strKey), bm.getValueAt(i, "cmangid"));
  }
  //逐行处理表体备注、数量
  String strCmain = null;
  String strCbaseid = null;
  String strCassid = null;
  Object oNarrvnum = null;
  Object oNassinum = null;
  UFDouble ufdNarrvnum = null;
  UFDouble ufdNassinum = null;
  Object oValue = null;
  for (int i = 0; i < bm.getRowCount(); i++) {
    //表体备注初始化:否则不能触发afterEdit()
    if (bm.getValueAt(i, "vmemo") == null) {
      bm.setValueAt("", i, "vmemo");
    }
    //表体退货理由初始化:否则不能触发afterEdit()
    if (getBillCardPanel().getBodyItem("vbackreasonb") != null) {
      if (bm.getValueAt(i, "vbackreasonb") == null) {
        bm.setValueAt("", i, "vbackreasonb");
      }
    }
    //是否拆分生成的行--暂未用
    bm.setValueAt(new UFBoolean(false), i, "issplit");

    strCbaseid = (String) bm.getValueAt(i, "cbaseid");
    strCmangid = (String) bm.getValueAt(i, "cmangid");
    strCassid = (String) bm.getValueAt(i, "cassistunit");
    strCmain = (String) bm.getValueAt(i, "cmainmeasid");
    //是否辅计量管理
    UFBoolean bIsAssMana = new UFBoolean(PuTool.isAssUnitManaged(strCbaseid));
    if (bIsAssMana.booleanValue()) {
      if (strCassid == null || strCassid.trim().equals("")) {
        MessageDialog.showErrorDlg(this, m_lanResTool.getStrByID("SCMCOMMON","UPPSCMCommon-000059")/*@res "错误"*/, m_lanResTool.getStrByID("40040301","UPP40040301-000190")/*@res "有辅计量管理的存货行存在空辅计量！"*/);
        return;
      }
      //设置换算率
      UFDouble convert = PuTool.getInvConvRateValue(strCbaseid, strCassid);
      bm.setValueAt(convert, i, "convertrate");
      //主辅计量相同则换算率置为 1.0
      if (strCmain != null && strCmain.equals(strCassid)) {
        bm.setValueAt(new UFDouble(1.0), i, "convertrate");
      }
      //非固定换算率，换算率=主数量/辅数量
      if (!PuTool.isFixedConvertRate(strCbaseid, strCassid)) {
        oNarrvnum = bm.getValueAt(i, "narrvnum");
        oNassinum = bm.getValueAt(i, "nassistnum");
        if (!(oNarrvnum == null || oNarrvnum.toString().trim().equals("")) && !(oNassinum == null || oNassinum.toString().trim().equals(""))) {
          ufdNarrvnum = new UFDouble(oNarrvnum.toString().trim());
          ufdNassinum = new UFDouble(oNassinum.toString().trim());
          oValue = ufdNassinum == new UFDouble(0) ? null : ufdNarrvnum.div(ufdNassinum);
        } else
          oValue = null;
        bm.setValueAt(oValue, i, "convertrate");
      }
    } else {
      bm.setValueAt(null, i, "convertrate");
    }
  }
  PuTool.loadSourceInfoAll(getBillCardPanel(),BillTypeConst.PO_ARRIVE);
  initInvRef();

}
/**
 * @功能：卡片显示数据
 */
private void loadDataToCard() throws Exception {
  UIRefPane refPane = null;
  BillModel bm = getBillCardPanel().getBillModel();
  if (getCacheVOs() != null) {    
    //从列表向卡片切换时处理排序
    if (isFrmList) {
      isFrmList = false;
      //获取排序后的实际位置
      int iShowPos = getBillListPanel().getHeadTable().getSelectedRow();
      if (iShowPos >= 0) {
        iShowPos = PuTool.getIndexBeforeSort(getBillListPanel(), iShowPos);
        setDispIndex(iShowPos);
      }
    }
    //新增单据时不必处理刷新表体,V5,新增、修改要刷新，因为加入制单时间、最后修改时间、审批时间
    if (!getStateStr().equals("转入修改")) {
      //获取表体(未被加载过才刷新)
      try {
        getCacheVOs()[getDispIndex()] = RcTool.getRefreshedVO(getCacheVOs()[getDispIndex()]);
      } catch (Exception be) {
        if (be instanceof BusinessException)
          MessageDialog.showErrorDlg(this, m_lanResTool.getStrByID("SCMCOMMON","UPPSCMCommon-000422")/*@res "业务异常"*/, be.getMessage());
        throw be;
      }
    } 
    //审批推式生成入库单时的如果出现异常，要将审批人和审批日期置空
    ArriveorderHeaderVO ArrBillHeadVO = (ArriveorderHeaderVO)getCacheVOs()[getDispIndex()].getParentVO();
    if(ArrBillHeadVO.getIbillstatus().intValue() == 0){
      ArrBillHeadVO.setCauditpsn(null);
      ArrBillHeadVO.setDauditdate(null);
    }
    //按采购公司初始化的参照：{业务员、部门、供应商}
    String strPurCorp = ArrBillHeadVO.getPk_purcorp();    
    ((UIRefPane)getBillCardPanel().getHeadItem("cemployeeid").getComponent()).getRefModel().setPk_corp(strPurCorp);
    ((UIRefPane)getBillCardPanel().getHeadItem("cdeptid").getComponent()).getRefModel().setPk_corp(strPurCorp);
    
    /* V50 发版前， 2006-11-24 : Wangyf&Xy&Xhq&Czp,注释掉,按收货公司处理供应商参照
    ((UIRefPane)getBillCardPanel().getHeadItem("cvendormangid").getComponent()).getRefModel().setPk_corp(strPurCorp);
    */
    
    //执行批次档案公式,加载批次档案信息
    RcTool.execFormulaForBatchCode(((ArriveorderVO)getCacheVOs()[getDispIndex()]).getBodyVo());

    //设置VO到卡片单据模板
    getBillCardPanel().setBillValueVO(getCacheVOs()[getDispIndex()]);
    //处理基础数据被作废(DR!=0)或冻结时不能正确显示名称问题
    loadBDData();
    //处理单据模板自定义项
    ArriveorderHeaderVO voHead = (ArriveorderHeaderVO) getCacheVOs()[getDispIndex()].getParentVO();
    String[] saKey = new String[] { "vdef1", "vdef2", "vdef3", "vdef4", "vdef5", "vdef6", "vdef7", "vdef8", "vdef9", "vdef10","vdef11", "vdef12", "vdef13", "vdef14", "vdef15", "vdef16", "vdef17", "vdef18", "vdef19", "vdef20" };
    int iLen = saKey.length;
    for (int i = 0; i < iLen; i++) {
      voHead.setAttributeValue(saKey[i],getBillCardPanel().getHeadItem(saKey[i]).getValue());
    }
        //关闭合计开关
    boolean  bOldNeedCalc = bm.isNeedCalculate() ; 
    bm.setNeedCalculate(false) ;
    //执行加载公式    
    bm.execLoadFormula();   
        //打开合计开关
    bm.setNeedCalculate(bOldNeedCalc) ;
    getBillCardPanel().updateValue();
    
    //显示表头备注
    refPane = (UIRefPane) getBillCardPanel().getHeadItem("vmemo").getComponent();
    refPane.setValue((String) getCacheVOs()[getDispIndex()].getParentVO().getAttributeValue("vmemo"));
    //显示表头退货理由
    if (getBillCardPanel().getHeadItem("vbackreasonh") != null) {
      refPane = (UIRefPane) getBillCardPanel().getHeadItem("vbackreasonh").getComponent();
      refPane.setValue((String) getCacheVOs()[getDispIndex()].getParentVO().getAttributeValue("vbackreasonh"));
//      if (getBillCardPanel().getHeadItem("bisback") != null){
//    	  boolean bIsEdit = getStateStr().equals("到货修改") || getStateStr().equals("转入修改");
//    	  getBillCardPanel().getHeadItem("bisback").setEnabled(isBackBill() && bIsEdit);
//      }

    }
    //缓存替换件参照构造子参数 {到货单行或订单行ID = cmangid }
    Vector vCmangids = new Vector();
    String strCmangid = null;
    m_hBillIDsForCmangids = new Hashtable();
    String strKey = (getStateStr().equals("转入修改")) ? "corder_bid" : "carriveorder_bid";
    for (int i = 0; i < bm.getRowCount(); i++) {
      strCmangid = (String) bm.getValueAt(i, "cmangid");
      if (strCmangid != null && strCmangid.trim().length() > 0 && !vCmangids.contains(strCmangid))
        vCmangids.addElement(strCmangid);
      if (bm.getValueAt(i, strKey) == null)
        continue;
      if (!m_hBillIDsForCmangids.containsKey(bm.getValueAt(i, strKey)))
        m_hBillIDsForCmangids.put(bm.getValueAt(i, strKey), bm.getValueAt(i, "cmangid"));
    }
    //逐行处理表体备注、数量
    String strCmain = null;
    String strCbaseid = null;
    String strCassid = null;
    Object oNarrvnum = null;
    Object oNassinum = null;
    UFDouble ufdNarrvnum = null;
    UFDouble ufdNassinum = null;
    Object oValue = null;
    
    UFDate dvaliddate = null;
    int intValiddays = 0;
    
    if(!refreshCardData())
    	return;
    //浏览修改：设置功能按钮是否有效
    if (!getStateStr().equals("转入修改")) {
      getBillCardPanel().getHeadItem("varrordercode").setEnabled(false);
      if (getStateStr().equals("到货浏览")) {
        setBtnLines(false);
      }
    }
    //转入修改：设置单据号、置保质期天数
    if (getStateStr().equals("转入修改")) {
      getBillCardPanel().getHeadItem("varrordercode").setValue(null);
      getBillCardPanel().getHeadItem("varrordercode").setEnabled(getBillCardPanel().getHeadItem("varrordercode").isEdit());
      //缓存保质期天数
      String[] arrStrCmangids = null;
      if (vCmangids.size() > 0) {
        arrStrCmangids = new String[vCmangids.size()];
        vCmangids.copyInto(arrStrCmangids);
      }
      Object qualitydaynum=null;
      Object qualityperiodunit=null;
      Object[][] retOb = null;
      
      for (int i = 0; i < bm.getRowCount(); i++) {
        strCmangid = (String) bm.getValueAt(i, "cmangid");
        try{
          retOb = CacheTool.getMultiColValue("bd_invmandoc", "pk_invmandoc", new String[] { "qualitydaynum", "qualityperiodunit" }, new String[] { strCmangid });
          if (retOb != null && retOb.length > 0 && retOb[0] != null && retOb[0].length > 1) {
              qualitydaynum=retOb[0][0];
              qualityperiodunit=retOb[0][1];
          }
          intValiddays = PuTool.calcQualityDay(PuPubVO.getInteger_NullAs(qualitydaynum, 0),PuPubVO.getInteger_NullAs(qualityperiodunit, 0));
          bm.setValueAt(intValiddays, i, "ivalidday");
          dvaliddate = (UFDate) getBillCardPanel().getBillModel().getValueAt(i, "dvaliddate");
          if(dvaliddate!=null){
            bm.setValueAt(dvaliddate.getDateBefore(intValiddays), i, "dproducedate");
          }
        }catch(Exception ee){
         SCMEnv.out(ee);
        }

      }
    }
    //设置状态图片
    try {
      getBillCardPanel().update(getGraphics());
    } catch (Exception e) {
      SCMEnv.out("加载图片时出错(不影响业务操作)");
    }
    
    loadSourceInfo();
    
  } else {
    if (getBillCardPanel().getBillData() != null) {
      //V5 Del : setImageType(this.IMAGE_NULL);
      getBillCardPanel().getBillData().clearViewData();
      getBillCardPanel().updateUI();
    }
  }
  //无论有无数据业务类型总有值
  if (getBusitype() != null) {
    m_refBusi.setPK(getBusitype());
  }
  //数值型字段正负属性设置
  setNumFieldsNeg(isBackBill()); //isBackBill()必须在单据模板置值之后
  //退货理由(头体)
  boolean bIsEdit = getStateStr().equals("到货修改") || getStateStr().equals("转入修改");
  if(bIsEdit){
    if (getBillCardPanel().getHeaderPanel("vbackreasonh") != null) {
      getBillCardPanel().getHeadItem("vbackreasonh").setEnabled(isBackBill() && getBillCardPanel().getHeadItem("vbackreasonh").isEdit());
      getBillCardPanel().getHeadItem("vbackreasonh").setEdit(isBackBill() && getBillCardPanel().getHeadItem("vbackreasonh").isEdit());
    }
    if (getBillCardPanel().getBodyItem("vbackreasonb") != null)
      getBillCardPanel().getBodyItem("vbackreasonb").setEdit(isBackBill() && getBillCardPanel().getBodyItem("vbackreasonb").isEdit());
  }
  initInvRef();
}
private boolean refreshCardData() {
	String strCmangid;
	String strCmain;
	String strCbaseid;
	String strCassid;
	Object oNarrvnum;
	Object oNassinum;
	UFDouble ufdNarrvnum;
	UFDouble ufdNassinum;
	Object oValue;
	UFDate dvaliddate;
	int intValiddays;
	BillModel bm = getBillCardPanel().getBillModel();
	for (int i = 0; i < bm.getRowCount(); i++) {
      //表体备注初始化:否则不能触发afterEdit()
      if (bm.getValueAt(i, "vmemo") == null) {
        bm.setValueAt("", i, "vmemo");
      }
      //表体退货理由初始化:否则不能触发afterEdit()
      if (getBillCardPanel().getBodyItem("vbackreasonb") != null) {
        if (bm.getValueAt(i, "vbackreasonb") == null) {
          bm.setValueAt("", i, "vbackreasonb");
        }
      }
      //是否拆分生成的行--暂未用
      bm.setValueAt(new UFBoolean(false), i, "issplit");

      strCbaseid = (String) bm.getValueAt(i, "cbaseid");
      strCmangid = (String) bm.getValueAt(i, "cmangid");
      strCassid = (String) bm.getValueAt(i, "cassistunit");
      strCmain = (String) bm.getValueAt(i, "cmainmeasid");
      //是否辅计量管理
      UFBoolean bIsAssMana = new UFBoolean(PuTool.isAssUnitManaged(strCbaseid));
      if (bIsAssMana.booleanValue()) {
        if (strCassid == null || strCassid.trim().equals("")) {
          MessageDialog.showErrorDlg(this, m_lanResTool.getStrByID("SCMCOMMON","UPPSCMCommon-000059")/*@res "错误"*/, m_lanResTool.getStrByID("40040301","UPP40040301-000190")/*@res "有辅计量管理的存货行存在空辅计量！"*/);
          return false;
        }
        //设置换算率
        UFDouble convert = PuTool.getInvConvRateValue(strCbaseid, strCassid);
        bm.setValueAt(convert, i, "convertrate");
        //主辅计量相同则换算率置为 1.0
        if (strCmain != null && strCmain.equals(strCassid)) {
          bm.setValueAt(new UFDouble(1.0), i, "convertrate");
        }
        //非固定换算率，换算率=主数量/辅数量
        if (!PuTool.isFixedConvertRate(strCbaseid, strCassid)) {
          oNarrvnum = bm.getValueAt(i, "narrvnum");
          oNassinum = bm.getValueAt(i, "nassistnum");
          if (!(oNarrvnum == null || oNarrvnum.toString().trim().equals("")) && !(oNassinum == null || oNassinum.toString().trim().equals(""))) {
            ufdNarrvnum = new UFDouble(oNarrvnum.toString().trim());
            ufdNassinum = new UFDouble(oNassinum.toString().trim());
            oValue = ufdNassinum == new UFDouble(0) ? null : ufdNarrvnum.div(ufdNassinum);
          } else{
            oValue = null;
          }
          bm.setValueAt(oValue, i, "convertrate");
        }else
        	bm.setValueAt(new UFBoolean(true), i, "isfixedrate");
      } else {
    	bm.setValueAt(new UFBoolean(true), i, "isfixedrate");
        bm.setValueAt(null, i, "convertrate");
      }
      
      intValiddays = PuPubVO.getInteger_NullAs(getBillCardPanel().getBillModel().getValueAt(i, "ivalidday") ,new Integer(BillStatus.FREE));
      dvaliddate = (UFDate) getBillCardPanel().getBillModel().getValueAt(i, "dvaliddate");
      if(dvaliddate!=null){
        bm.setValueAt(dvaliddate.getDateBefore(intValiddays), i, "dproducedate");
      }

    }
	return true;
}

/**
 * 加载表体来源信息
 *
 */
private void loadSourceInfo() {
	if (getBillCardPanel().getBillModel().getItemByKey("csourcebillcode").isShow()) {
	    //加载来源信息
	    PuTool.loadSourceInfoAll(getBillCardPanel(),BillTypeConst.PO_ARRIVE);
    }
}

/**
 * @功能：向询价单列表界面写数据
 * @作者：晁志平
 * 创建日期：(2001-6-7 17:25:38)
 */
private void loadDataToList() {

  if (getCacheVOs() != null) {
    getBillListPanel().getBodyBillModel().clearBodyData();
    ArriveorderHeaderVO[] headers = null;
    headers = getArriveHeaderVOs(getCacheVOs());
    getBillListPanel().setHeaderValueVO(headers);
    //显示表头备注、退货理由
    for (int i = 0; i < headers.length; i++) {
      getBillListPanel().getHeadBillModel().setValueAt(headers[i].getVmemo(), i, "vmemo");
      getBillListPanel().getHeadBillModel().setValueAt(headers[i].getVbackreasonh(), i, "vbackreasonh");
    }
    getBillListPanel().getHeadBillModel().execLoadFormula();

    //执行批次档案公式,加载批次档案信息
    if ((null != ((ArriveorderVO) getCacheVOs()[getDispIndex()]).getBodyVo())
          && ((ArriveorderVO) getCacheVOs()[getDispIndex()]).getBodyVo().length > 0) {
        RcTool
            .execFormulaForBatchCode(((ArriveorderVO) getCacheVOs()[getDispIndex()])
                .getBodyVo());
        }

  } else {
    if (getBillListPanel().getHeadBillModel() != null) {
      getBillListPanel().getHeadBillModel().clearBodyData();
    }
    if (getBillListPanel().getBodyBillModel() != null) {
      getBillListPanel().getBodyBillModel().clearBodyData();
    }
  }
}

private void loadBatchDocInfo(boolean isList){
  String[] saBaseId = null;
  String[] saAssistUnit = null;
  String[] saMangId = null;

  if(isList){
    saBaseId = (String[]) PuGetUIValueTool.getArray(getBillListPanel().getBodyBillModel(), "cbaseid", String.class, 0, getBillListPanel().getBodyBillModel().getRowCount());
    saAssistUnit = (String[]) PuGetUIValueTool.getArray(getBillListPanel().getBodyBillModel(), "cassistunit", String.class, 0, getBillListPanel().getBodyBillModel().getRowCount());
    saMangId = (String[]) PuGetUIValueTool.getArrayNotNull(getBillListPanel().getBodyBillModel(), "cmangid", String.class, 0, getBillListPanel().getBodyBillModel().getRowCount());
  }else{
    saBaseId = (String[]) PuGetUIValueTool.getArray(getBillCardPanel().getBillModel(), "cbaseid", String.class, 0, getBillCardPanel().getRowCount());
    saAssistUnit = (String[]) PuGetUIValueTool.getArray(getBillCardPanel().getBillModel(), "cassistunit", String.class, 0, getBillCardPanel().getRowCount());
    saMangId = (String[]) PuGetUIValueTool.getArrayNotNull(getBillCardPanel().getBillModel(), "cmangid", String.class, 0, getBillCardPanel().getRowCount());
  }
    
  // 是否批次号管理批量加载
  PuTool.loadBatchProdNumMngt(saMangId);

  // 批量加载
  PuTool.loadBatchInvConvRateInfo(saBaseId, saAssistUnit);

  // 批量加载
  PuTool.loadBatchAssistManaged(saBaseId);

}

/**
 *  功能描述:行变化或编辑后处理：
   1。设置辅计量参照；
   2。设置换算率；
   //3。设置是否固定换算率；
   4。控制辅计量及辅信息的编辑状态
 */
private void setAssisUnitEditState2(BillEditEvent event) {
  
  if(event.getRow()<0){
    return;
  }
  loadBatchDocInfo(false);
  //是否进行辅计量管理
  String strCbaseid = (String) getBillCardPanel().getBillModel().getValueAt(event.getRow(), "cbaseid");

  if (strCbaseid != null && !strCbaseid.trim().equals("") && nc.ui.pu.pub.PuTool.isAssUnitManaged(strCbaseid)) {
    // 设置辅计量参照
    setRefPaneAssistunit(event.getRow());
    //设置可编辑性
    getBillCardPanel().setCellEditable(event.getRow(), "convertrate", getBillCardPanel().getBodyItem("convertrate").isEdit());
    getBillCardPanel().setCellEditable(event.getRow(), "nassistnum", getBillCardPanel().getBodyItem("nassistnum").isEdit());
    getBillCardPanel().setCellEditable(event.getRow(), "cassistunitname", getBillCardPanel().getBodyItem("cassistunitname").isEdit());
    String cassistunit =
      (String) getBillCardPanel().getBillModel().getValueAt(
        event.getRow(),
        "cassistunit");
    //辅计量为空,各数量不可编辑
    if (cassistunit == null || cassistunit.trim().equals("")) {
      getBillCardPanel().setCellEditable(event.getRow(), "cassistunitname", getBillCardPanel().getBodyItem("cassistunitname").isEdit());
      getBillCardPanel().setCellEditable(event.getRow(), "nassistnum", getBillCardPanel().getBodyItem("nassistnum").isEdit());
      getBillCardPanel().setCellEditable(event.getRow(), "nmoney", false);
      getBillCardPanel().setCellEditable(event.getRow(), "narrvnum", false);
      getBillCardPanel().setCellEditable(event.getRow(), "nelignum", false);
      getBillCardPanel().setCellEditable(event.getRow(), "npresentnum", false);
      getBillCardPanel().setCellEditable(event.getRow(), "nwastnum", false);
      getBillCardPanel().setCellEditable(event.getRow(), "convertrate", false);
    } else { //辅计量不为空
      //设置换算率
      UFDouble ufdConv =
        nc.ui.pu.pub.PuTool.getInvConvRateValue(strCbaseid, cassistunit);
      Object oTmp =
        getBillCardPanel().getBillModel().getValueAt(event.getRow(), "convertrate");
      if (oTmp == null || oTmp.toString().trim().equals("")) {
        getBillCardPanel().getBillModel().setValueAt(
          ufdConv,
          event.getRow(),
          "convertrate");
      }
      //设置可编辑性
      getBillCardPanel().setCellEditable(event.getRow(), "cassistunitname", getBillCardPanel().getBodyItem("cassistunitname").isEdit());
      getBillCardPanel().setCellEditable(event.getRow(), "nassistnum", getBillCardPanel().getBodyItem("nassistnum").isEdit());
      getBillCardPanel().setCellEditable(event.getRow(), "nmoney", getBillCardPanel().getBodyItem("nmoney").isEdit());
      getBillCardPanel().setCellEditable(event.getRow(), "narrvnum", getBillCardPanel().getBodyItem("narrvnum").isEdit());
      //如果到货是正到货
      /* delete 2003-10-22
      if (!bIsNegative) {
        getArrBillCardPanel().setCellEditable(event.getRow(), "nelignum", false);
      } else {
        getArrBillCardPanel().setCellEditable(event.getRow(), "nelignum", true);
      }
      */
      getBillCardPanel().setCellEditable(event.getRow(), "npresentnum", getBillCardPanel().getBodyItem("npresentnum").isEdit());
      getBillCardPanel().setCellEditable(event.getRow(), "nwastnum", getBillCardPanel().getBodyItem("nwastnum").isEdit());
      getBillCardPanel().setCellEditable(event.getRow(), "convertrate", getBillCardPanel().getBodyItem("convertrate").isEdit());
      //如果辅计量是固定换算率
      if (nc.ui.pu.pub.PuTool.isFixedConvertRate(strCbaseid, cassistunit)) {
        getBillCardPanel().setCellEditable(event.getRow(), "convertrate", false);
      }
      //如果是主辅计量相同,则换算率不可编辑
      String ass =
        (String) getBillCardPanel().getBillModel().getValueAt(
          event.getRow(),
          "cassistunitname");
      String main =
        (String) getBillCardPanel().getBillModel().getValueAt(
          event.getRow(),
          "cmainmeasname");
      if (ass != null && ass.equals(main)) {
        getBillCardPanel().getBillModel().setValueAt(
          new UFDouble(1),
          event.getRow(),
          "convertrate");
        getBillCardPanel().setCellEditable(event.getRow(), "convertrate", false);

      }
    }
  } else {
    //没有辅计量管理时处理辅信息为空(模板中部分辅信息要保存时处理，用户不可见)
    getBillCardPanel().setCellEditable(event.getRow(), "convertrate", false);
    getBillCardPanel().setCellEditable(event.getRow(), "nassistnum", false);
    getBillCardPanel().setCellEditable(event.getRow(), "cassistunitname", false);
    getBillCardPanel().getBillModel().setValueAt(
      null,
      event.getRow(),
      "convertrate");
    getBillCardPanel().getBillModel().setValueAt(
      null,
      event.getRow(),
      "nassistnum");
    getBillCardPanel().getBillModel().setValueAt(
      null,
      event.getRow(),
      "cassistunitname");
    getBillCardPanel().getBillModel().setValueAt(
      null,
      event.getRow(),
      "nassistnum");
    getBillCardPanel().getBillModel().setValueAt(
      null,
      event.getRow(),
      "cassistunit");
  }
}

/**
 * 功能描述:设置退货理由是否可编辑
 */
private void setBackReasonEditable() {
  if (isBackBill()) {
    if (getBillCardPanel().getHeadItem("vbackreasonh") != null) {
      getBillCardPanel().getHeadItem("vbackreasonh").setEnabled(getBillCardPanel().getHeadItem("vbackreasonh").isEdit());
      getBillCardPanel().getHeadItem("vbackreasonh").setEdit(getBillCardPanel().getHeadItem("vbackreasonh").isEdit());
    }
    if (getBillCardPanel().getBodyItem("vbackreasonb") != null) {
      getBillCardPanel().getBodyItem("vbackreasonb").setEnabled(getBillCardPanel().getBodyItem("vbackreasonb").isEdit());
      getBillCardPanel().getBodyItem("vbackreasonb").setEdit(getBillCardPanel().getBodyItem("vbackreasonb").isEdit());
    }
  } else {
    if (getBillCardPanel().getHeadItem("vbackreasonh") != null)
      getBillCardPanel().getHeadItem("vbackreasonh").setEnabled(false);
    if (getBillCardPanel().getBodyItem("vbackreasonb") != null) {
      getBillCardPanel().getBodyItem("vbackreasonb").setEnabled(false);
      getBillCardPanel().getBodyItem("vbackreasonb").setEdit(false);
    }
  }
}

/**
 * 设置行操作是否可用
 */
private void setBtnLines(boolean isEnable) {

  m_btnLines.setEnabled(isEnable);
  int iLne = m_btnLines.getChildCount();
  for (int i = 0; i < iLne; i++) {
    ((ButtonObject)m_btnLines.getChildren().elementAt(i)).setEnabled(isEnable);
    updateButton((ButtonObject)m_btnLines.getChildren().elementAt(i));
  }
  UIMenuItem[] menuitems = getBillCardPanel().getBodyMenuItems();
  if (menuitems != null && menuitems.length > 0) {
    for (int i = 0; i < menuitems.length; i++) {
      menuitems[i].setEnabled(isEnable);
    }
  }
  //since v502
  rightButtonRightControl();
}

/**
 * @功能：设置到货列表状态下的按钮
 * @作者：晁志平
 * 创建日期：(2001-6-20 7:58:28)
 */
private void setButtonsList() {
 
  //列表特殊
  m_btnCard.setName(m_lanResTool.getStrByID("SCMCOMMON", "UPPSCMCommon-000463")/*
       * @res
       * "卡片显示"
       */);
  
  /*结束转单*/
  m_btnEndCreate.setEnabled(false);
  m_btnEndCreate.setVisible(false);
  //业务类型
  m_btnBusiTypes.setEnabled(false);
  m_btnAdds.setEnabled(false);
  m_btnSave.setEnabled(false);
  m_btnBacks.setEnabled(false);
  m_btnCancel.setEnabled(false);
  m_btnLines.setEnabled(false);
  m_btnRefresh.setEnabled(m_bQueriedFlag);
  m_btnLocate.setEnabled(false);
  m_btnFirst.setEnabled(false);
  m_btnPrev.setEnabled(false);
  m_btnNext.setEnabled(false);
  m_btnLast.setEnabled(false);
  m_btnCombin.setEnabled(false);
  m_btnCheck.setEnabled(false);
  m_btnQueryForAudit.setEnabled(false);
  m_btnSendAudit.setEnabled(false);
  m_btnLookSrcBill.setEnabled(false);
  m_btnQuickReceive.setEnabled(false);
  
  m_btnMaintains.setEnabled(true);
  m_btnBrowses.setEnabled(true);
  m_btnPrints.setEnabled(true);
    
  /*存量查询、成套件查询、文档管理、预览、打印、修改、作废、审批、弃审、全选、全消、卡片显示/列表显示*/
  
  int iDataCnt = getCacheVOs() == null ? 0 : getCacheVOs().length;
  int iSeltCnt = getBillListPanel().getHeadTable().getSelectedRowCount();
  
  //缓存无数据
  if(iDataCnt == 0){
    m_btnUsable.setEnabled(false);
    m_btnQueryBOM.setEnabled(false);
    m_btnDocument.setEnabled(false);
    m_btnPrint.setEnabled(false);
    m_btnSplitPrint.setEnabled(false);
    m_btnPrintPreview.setEnabled(false);
    m_btnModify.setEnabled(false);
    m_btnDiscard.setEnabled(false);
    m_btnAudit.setEnabled(false);
    m_btnUnAudit.setEnabled(false);
    m_btnSelectAll.setEnabled(false);
    m_btnSelectNo.setEnabled(false);
    m_btnCard.setEnabled(true);
    //
    updateButtonsAll();
    return;
  }
  //未选中数据
  if(iSeltCnt == 0){
    m_btnUsable.setEnabled(false);
    m_btnQueryBOM.setEnabled(false);
    m_btnDocument.setEnabled(false);
    m_btnPrint.setEnabled(false);
    m_btnSplitPrint.setEnabled(false);
    m_btnPrintPreview.setEnabled(false);
    m_btnModify.setEnabled(false);
    m_btnDiscard.setEnabled(false);
    m_btnAudit.setEnabled(false);
    m_btnUnAudit.setEnabled(false);
    m_btnSelectAll.setEnabled(false);
    m_btnSelectNo.setEnabled(false);
    m_btnCard.setEnabled(false);
    //
    updateButtonsAll();
    return;
  }
  
  /*存量查询、成套件查询、文档管理、预览、打印、修改、作废、审批、弃审、全选、全消、卡片显示/列表显示*/
  boolean bOnlyOneSelected = (iSeltCnt == 1);
  boolean bAllSelected = (iSeltCnt == iDataCnt);
  
  //
  m_btnUsable.setEnabled(bOnlyOneSelected);
  m_btnQueryBOM.setEnabled(bOnlyOneSelected);
  m_btnCard.setEnabled(bOnlyOneSelected);
  if(bOnlyOneSelected){
    m_btnModify.setEnabled(getCacheVOs()[getDispIndex()].isCanBeModified());
    m_btnDiscard.setEnabled(getCacheVOs()[getDispIndex()].isCanBeDiscarded());
    m_btnAudit.setEnabled(getCacheVOs()[getDispIndex()].isCanAudit());
    m_btnUnAudit.setEnabled(getCacheVOs()[getDispIndex()].isCanUnAudit());
  }else{
    m_btnModify.setEnabled(false);
    m_btnDiscard.setEnabled(true);
    m_btnAudit.setEnabled(true);
    m_btnUnAudit.setEnabled(true);
    
    ArrayList a = getSelectedBills();
    for (int i = 0; i < a.size(); i++) {
      ArriveorderVO aa = (ArriveorderVO) a.get(i);
      if (aa != null && aa.getHeadVO() != null) {
        if (3 == aa.getHeadVO().getIbillstatus().intValue()) {
          m_btnAudit.setEnabled(false);
          break;
        }else if (0 == aa.getHeadVO().getIbillstatus().intValue()) {
          m_btnUnAudit.setEnabled(false);
          break;
        }
      }
    }
  }
  m_btnDocument.setEnabled(true);
  m_btnPrint.setEnabled(true);
  m_btnSplitPrint.setEnabled(true);
  m_btnPrintPreview.setEnabled(true);
  m_btnSelectNo.setEnabled(true);
  m_btnSelectAll.setEnabled(!bAllSelected);
  
  m_btnCreateCard.setEnabled(false);
  m_btnDeleteCard.setEnabled(false);

  //
  updateButtonsAll();

}

/**
 * @功能：订单生成的到货单列表(未保存时)按钮逻辑
 */
private void setButtonsListNew() {
  //
  for (int i = 0; i < m_aryArrListButtons.length; i++) {
    m_aryArrListButtons[i].setEnabled(false);
  }
  m_btnCancel.setEnabled(false);
  /*只有“切换”、“放弃转单”按钮可用*/
  m_btnModifyList.setEnabled(true);
  m_btnEndCreate.setVisible(true);
  m_btnEndCreate.setEnabled(true);
  //
  updateButtonsAll();
}
/**
 * 消息中心按钮逻辑
 *
 */
private void setButtonsMsgCenter(){

  //审批
  m_btnAudit.setEnabled(true);
  updateButton(m_btnAudit);
  //弃审
  m_btnUnAudit.setEnabled(true);  
  updateButton(m_btnUnAudit);
  //状态查询
  m_btnQueryForAudit.setEnabled(true);
  updateButton(m_btnQueryForAudit);
  //文档管理
  m_btnDocument.setEnabled(true);
  updateButton(m_btnDocument);
  //逐级联查
  m_btnLookSrcBill.setEnabled(true);
  updateButton(m_btnLookSrcBill);
}

/**
 * @功能：到货浏览状态下按钮设置
 */
private void setButtonsCard() {
  //
  setButtonsInit();
  
  if (getCacheVOs() != null && getCacheVOs().length >= 1) {
    //打印、定位、刷新、修改、作废、辅助
    m_btnPrint.setEnabled(true);
    m_btnSplitPrint.setEnabled(true);
    m_btnCombin.setEnabled(true);
    m_btnPrintPreview.setEnabled(true);
    m_btnLocate.setEnabled(true);
    m_btnRefresh.setEnabled(m_bQueriedFlag);
    m_btnRefreshList.setEnabled(m_bQueriedFlag);
    //修改
    if (getCacheVOs()[getDispIndex()].isCanBeModified()) {
      m_btnModify.setEnabled(true);
    } else {
      m_btnModify.setEnabled(false);
    }
    //作废
    if (getCacheVOs()[getDispIndex()].isCanBeDiscarded()) {
      if (getCacheVOs()[getDispIndex()].isHaveCheckLine()) {
        m_btnDiscard.setEnabled(false);
      } else {
        m_btnDiscard.setEnabled(true);
      }
    } else {
      m_btnDiscard.setEnabled(false);
    }
    //辅助
    m_btnOthers.setEnabled(true);
    //单据维护
    m_btnMaintains.setEnabled(true);
    //浏览
    m_btnBrowses.setEnabled(true);
    //存量查询
    m_btnUsable.setEnabled(true);
    //成套件
    m_btnQueryBOM.setEnabled(true);
    //行操作
    setBtnLines(false);
    //文档管理
    m_btnDocument.setEnabled(true);
    //状态查询
    m_btnQueryForAudit.setEnabled(true);
    //逐级联查
    m_btnLookSrcBill.setEnabled(true);
    //快速收货
    m_btnQuickReceive.setEnabled(true);
    //上下首末张逻辑
    if (getCacheVOs().length == 1) {
      m_btnFirst.setEnabled(false);
      m_btnPrev.setEnabled(false);
      m_btnNext.setEnabled(false);
      m_btnLast.setEnabled(false);
    } else if (getDispIndex() != getCacheVOs().length - 1 && getDispIndex() != 0) {
      m_btnFirst.setEnabled(true);
      m_btnPrev.setEnabled(true);
      m_btnNext.setEnabled(true);
      m_btnLast.setEnabled(true);
    } else if (getDispIndex() == 0) {
      m_btnFirst.setEnabled(false);
      m_btnPrev.setEnabled(false);
      m_btnNext.setEnabled(true);
      m_btnLast.setEnabled(true);
    } else {
      m_btnFirst.setEnabled(true);
      m_btnPrev.setEnabled(true);
      m_btnNext.setEnabled(false);
      m_btnLast.setEnabled(false);
    }
    //送审按钮
    m_btnSendAudit.setEnabled(isNeedSendAudit(getCacheVOs()[getDispIndex()]));
    //
    m_btnAudit.setEnabled(getCacheVOs()[getDispIndex()].isCanAudit());
    m_btnUnAudit.setEnabled(getCacheVOs()[getDispIndex()].isCanUnAudit());
    
    //不支持质量管理未启用情况，因为维护没有录入合格、不合格数量的功能
    m_btnCheck.setEnabled(m_bQcEnabled);
    
  }
  //查询刷新
  m_btnQuery.setEnabled(true);
  m_btnRefresh.setEnabled(m_bQueriedFlag);
  //
  updateButtonsAll();
}

/**
 * @功能：修改按钮逻辑
 * @作者：晁志平
 * 创建日期：(2001-6-20 13:39:39)
 * @param:<|>
 * @return:
 * @exception:
 * @see;
 * @since:
 *
 */
private void setButtonsModify() {
  //
  m_btnEndCreate.setVisible(false);
  //
  for (int i = 0; i < m_aryArrCardButtons.length; i++) {
    if(PuTool.isExist(getExtendBtns(),m_aryArrCardButtons[i])){
      continue;
    }
    m_aryArrCardButtons[i].setEnabled(false);
  }
  int iLen = m_btnOthers.getChildCount();
  for (int i = 0; i < iLen; i++) {
    ((ButtonObject)m_btnOthers.getChildren().elementAt(i)).setEnabled(false);
  }
  iLen = m_btnBacks.getChildCount();
  for (int i = 0; i < iLen; i++) {
    ((ButtonObject)m_btnBacks.getChildren().elementAt(i)).setEnabled(false);
  }
  //辅助
  m_btnOthers.setEnabled(true);
  //快速收货
  m_btnQuickReceive.setEnabled(false);
  //存量查询
  m_btnUsable.setEnabled(true);
  //成套件
  m_btnQueryBOM.setEnabled(true);
  m_btnRefresh.setEnabled(false);
  m_btnLocate.setEnabled(false);
  m_btnDocument.setEnabled(false);
  m_btnLookSrcBill.setEnabled(false);

  //单据维护
  m_btnMaintains.setEnabled(true);
  m_btnSave.setEnabled(true);
  m_btnCancel.setEnabled(true);
  m_btnModify.setEnabled(false);
  m_btnDiscard.setEnabled(false);
  //浏览
  m_btnBrowses.setEnabled(true);
  m_btnQuery.setEnabled(false);
  m_btnFirst.setEnabled(false);
  m_btnPrev.setEnabled(false);
  m_btnNext.setEnabled(false);
  m_btnLast.setEnabled(false);
  m_btnUnAudit.setEnabled(false);

  for (int iRow = 0; iRow < getBillCardPanel().getBillModel().getRowCount();iRow++) {
    for (int i = 0; i < RcTool.sTargetFields.length;i++) {
      getBillCardPanel().setCellEditable(iRow, RcTool.sTargetFields[i], false);//采购到货单目前不提供对已有批次档案的信息在到货单上进行修改的功能 V502
    }
  }

  //执行按钮组
  m_btnActions.setEnabled(true);
  //送审按钮
  m_btnSendAudit.setEnabled(isNeedSendAudit(getCacheVOs()[getDispIndex()]));
  
  setBtnLines(true);

  /** 设置右键菜单与按钮组“行操作”权限相同 */
  setPopMenuBtnsEnable();
  //
  updateButtonsAll();
}

/**
 * @功能：订单生成的到货单卡片界面按钮逻辑
 * @作者：晁志平
 * 创建日期：(2001-7-31 18:42:07)
 * @param:<|>
 * @return:
 * @exception:
 * @see;
 * @since:
 *
 */
private void setButtonsModifyNew() {

  setButtonsModify();

  m_btnList.setEnabled(false);
  updateButton(m_btnList);
  
}
/**
 * 设置按钮状态
 * 创建日期：(2001-3-17 9:00:09)
 */
private void setButtonsState() {
  
  int iVal = -999;//支持产业链功能扩展

  if (getStateStr().equals("初始化")) {
    setButtonsInit();
    iVal = 0;
  }
  else if (getStateStr().equals("到货浏览")) {
    setButtonsCard();
    iVal = 1;
  }
  else if (getStateStr().equals("到货修改")) {
    setButtonsModify();
    iVal = 2;
  }
  else if (getStateStr().equals("到货列表")) {
    setButtonsList();
    iVal = 3;
  }
  else if (getStateStr().equals("转入列表")) {
    setButtonsListNew();
    iVal = 4;
  }
  else if (getStateStr().equals("转入修改")) {
    setButtonsModifyNew();
    iVal = 5;
  }
  else if (getStateStr().equals("消息中心")) {
    setButtonsMsgCenter();
    iVal = 6;
  }
  for (int i = 0; i < getBillCardPanel().getBillModel().getRowCount(); i++) {
    getBillCardPanel().setCellEditable(i, "creporterid", false);// 报告人ID
    getBillCardPanel().setCellEditable(i, "creportername", false);// 报告人名称
    getBillCardPanel().setCellEditable(i, "dreportdate", false);// 报告日期
  }
  setBtnStatusSN();
  setExtendBtnsStat(iVal);
}

/**
 * 功能描述:改变界面按钮状态
 */
private void updateButtonsAll() {
  int iLen = getBtnTree().getButtonArray().length;
  for (int i = 0; i < iLen; i++) {
    update(getBtnTree().getButtonArray()[i]);
  }
}

/**
 * 创建日期： 2005-9-20 功能描述： 更新按钮状态（递归方式）
 */
private void update(ButtonObject bo) {
  updateButton(bo);
  if (bo.getChildCount() > 0) {
    for (int i = 0, len = bo.getChildCount(); i < len; i++)
      update(bo.getChildButtonGroup()[i]);
  }
}
/**
 * 获取按钮树，类唯一实例。
 * <p>
 * <b>examples:</b>
 * <p>
 * 使用示例
 * <p>
 * <b>参数说明</b>
 * @return
 * <p>
 * @author czp
 * @time 2007-3-13 下午01:16:48
 */
protected ButtonTree getBtnTree(){//因为getExtendBtns()这个方法现在不用了，为二次开发放开为protected
  if(m_btnTree == null){
    try {
      m_btnTree = new ButtonTree("40040301"); 
    }
    catch (BusinessException be) {
      showHintMessage(be.getMessage());
      return null;
    }
  }
  return m_btnTree;
}
/**
 * 功能：在编辑数量或辅数量后设置:
   1.如果为空或者大于0，合格数量、不合格数量不可编辑；赠品数量、途耗数量、金额同正；
   2.如果小于0，合格数量可编辑，赠品数量、途耗数量、金额、合格、不合格同负；
 * @param e nc.ui.pub.bill.BillEditEvent
 */
private void setEditAndDirect(BillEditEvent e) {
  boolean bIsNegative = false;
  if (getBillCardPanel().getBodyValueAt(e.getRow(), "narrvnum") == null
    || getBillCardPanel().getBodyValueAt(e.getRow(), "narrvnum").toString().equals(
      "")
    || (new UFDouble(getBillCardPanel()
      .getBodyValueAt(e.getRow(), "narrvnum")
      .toString()))
      .doubleValue()
      >= 0.0) {
    bIsNegative = false;
  } else {
    bIsNegative = true;
  }
  //#正到货
  if (!bIsNegative) {
    //合格数量不可编辑
    /* delete 2003-10-22
    getArrBillCardPanel().setCellEditable(e.getRow(), "nelignum", false);
    */
    //合格数量与到货数量同正
    ((UIRefPane) getBillCardPanel().getBodyItem("nelignum").getComponent())
    .getUITextField()
    .setMaxValue(nc.vo.scm.field.pu.FieldMinMaxValue.MAX_NUM);
    ((UIRefPane) getBillCardPanel().getBodyItem("nelignum").getComponent())
    .getUITextField()
    .setMinValue(0.0);

    //赠品同正
    ((UIRefPane) getBillCardPanel().getBodyItem("npresentnum").getComponent())
    .getUITextField()
    .setMinValue(0.0);

    ((UIRefPane) getBillCardPanel().getBodyItem("npresentnum").getComponent())
    .getUITextField()
    .setMaxValue(nc.vo.scm.field.pu.FieldMinMaxValue.MAX_NUM);
    ////途耗同正
    ((UIRefPane) getBillCardPanel().getBodyItem("nwastnum").getComponent())
    .getUITextField()
    .setMinValue(0.0);

    ((UIRefPane) getBillCardPanel().getBodyItem("nwastnum").getComponent())
    .getUITextField()
    .setMaxValue(nc.vo.scm.field.pu.FieldMinMaxValue.MAX_NUM);
    //金额同正
    ((UIRefPane) getBillCardPanel().getBodyItem("nmoney").getComponent())
    .getUITextField()
    .setMinValue(0.0);

    ((UIRefPane) getBillCardPanel().getBodyItem("nmoney").getComponent())
    .getUITextField()
    .setMaxValue(nc.vo.scm.field.pu.FieldMinMaxValue.MAX_NUM);
  }
  //#负到货
  else {
    //合格数量可编辑
    /* delete 2003-10-22
    getArrBillCardPanel().setCellEditable(e.getRow(), "nelignum", true);
    */
    //合格数量与到货数量同负
    ((UIRefPane) getBillCardPanel().getBodyItem("nelignum").getComponent())
    .getUITextField()
    .setMaxValue(0.0);
    ((UIRefPane) getBillCardPanel().getBodyItem("nelignum").getComponent())
    .getUITextField()
    .setMinValue(nc.vo.scm.field.pu.FieldMinMaxValue.MIN_NUM);
    //赠品同负
    ((UIRefPane) getBillCardPanel().getBodyItem("npresentnum").getComponent())
    .getUITextField()
    .setMaxValue(0.0);
    ((UIRefPane) getBillCardPanel().getBodyItem("npresentnum").getComponent())
    .getUITextField()
    .setMinValue(nc.vo.scm.field.pu.FieldMinMaxValue.MIN_NUM);
    //途耗同负
    ((UIRefPane) getBillCardPanel().getBodyItem("nwastnum").getComponent())
      .getUITextField()
      .setMaxValue(0.0);
    ((UIRefPane) getBillCardPanel().getBodyItem("nwastnum").getComponent())
      .getUITextField()
      .setMinValue(nc.vo.scm.field.pu.FieldMinMaxValue.MIN_NUM);
    //金额同负
    ((UIRefPane) getBillCardPanel().getBodyItem("nmoney").getComponent())
    .getUITextField()
    .setMaxValue(0.0);
    ((UIRefPane) getBillCardPanel().getBodyItem("nmoney").getComponent())
    .getUITextField()
    .setMinValue(nc.vo.scm.field.pu.FieldMinMaxValue.MIN_NUM);
  }
}

/**
 * @功能：到货单列表头变换时向表体表中写入相应子表内容
 * @作者：晁志平
 * 创建日期：(2001-6-8 16:41:42)
 * 修改：为提高效率，要增加对指定表头加载表体的操作 0530
 * @param row0 int
 */
private boolean setListBodyData(int row0) {
  boolean isErr = false;
  if (!getStateStr().equals("转入列表")) {
    items = null;
    try {
      getCacheVOs()[row0] = RcTool.getRefreshedVO(getCacheVOs()[row0]);
      if (getCacheVOs()[row0] != null
        && getCacheVOs()[row0].getChildrenVO() != null
        && getCacheVOs()[row0].getChildrenVO().length > 0) {
        items = (ArriveorderItemVO[]) getCacheVOs()[row0].getChildrenVO();
      }
    } catch (Exception be) {
      getBillListPanel().getBodyBillModel().clearBodyData();
      if (be instanceof BusinessException) {
        MessageDialog.showErrorDlg(this, m_lanResTool.getStrByID("SCMCOMMON","UPPSCMCommon-000422")/*@res "业务异常"*/, be.getMessage());
      }
      return true;
    }
    getBillListPanel().setBodyValueVO(items);
  } else {
    getBillListPanel().setBodyValueVO(getCacheVOs()[getDispIndex()].getChildrenVO());
  }
  getBillListPanel().getBodyBillModel().execLoadFormula();
  BillModel bm = getBillListPanel().getBodyBillModel();
  //逐行处理 ------------------------------------------------ 开始
  String strCbaseid = null;
  String strCmain = null;
  String strCassid = null;
  Object oNarrvnum = null;
  Object oNassinum = null;
  UFDouble ufdNarrvnum = null;
  UFDouble ufdNassinum = null;
  Object oValue = null;
  
  
  UFDate dvaliddate = null;
  int intValiddays = 0;

  for (int i = 0; i < bm.getRowCount(); i++) {
    //表体备注初始化
    if (bm.getValueAt(i, "vmemo") == null) {
      bm.setValueAt("", i, "vmemo");
    }
    strCbaseid = (String) bm.getValueAt(i, "cbaseid");
    //strCmangid = (String) bm.getValueAt(i, "cmangid");
    strCassid = (String) bm.getValueAt(i, "cassistunit");
    strCmain = (String) bm.getValueAt(i, "cmainmeasid");
    //是否辅计量管理
    UFBoolean bIsAssMana = new UFBoolean(PuTool.isAssUnitManaged(strCbaseid));
    if (bIsAssMana.booleanValue()) {
      if (strCassid == null || strCassid.trim().equals("")) {
        MessageDialog.showErrorDlg(this, m_lanResTool.getStrByID("SCMCOMMON","UPPSCMCommon-000059")/*@res "错误"*/, m_lanResTool.getStrByID("40040301","UPP40040301-000190")/*@res "有辅计量管理的存货行存在空辅计量！"*/);
        return true;
      }
      //设置换算率
      UFDouble convert = PuTool.getInvConvRateValue(strCbaseid, strCassid);
      bm.setValueAt(convert, i, "convertrate");
      //如果主辅计量相同
      if (strCmain != null && strCmain.equals(strCassid)) {
        bm.setValueAt(new UFDouble(1.0), i, "convertrate");
      }
      //如果不是固定换算率，则用主数量/辅数量取得换算率
      if (!PuTool.isFixedConvertRate(strCbaseid, strCassid)) {
        oNarrvnum = bm.getValueAt(i, "narrvnum");
        oNassinum = bm.getValueAt(i, "nassistnum");
        if (!(oNarrvnum == null || oNarrvnum.toString().trim().equals(""))
          && !(oNassinum == null || oNassinum.toString().trim().equals(""))) {
          ufdNarrvnum = new UFDouble(oNarrvnum.toString().trim());
          ufdNassinum = new UFDouble(oNassinum.toString().trim());
          oValue = ufdNassinum == new UFDouble(0) ? null : ufdNarrvnum.div(ufdNassinum);
        } else
          oValue = null;
        bm.setValueAt(oValue, i, "convertrate");
      }
    } else {
      bm.setValueAt(null, i, "convertrate");
      bm.setValueAt(null, i, "isfixedrate");
    }
    // 是否固定换算率
    if(PuTool.isFixedConvertRate(strCbaseid, strCassid)){
      bm.setValueAt(new UFBoolean(true), i, "isfixedrate");
    }
    
    intValiddays = PuPubVO.getInteger_NullAs(getBillCardPanel().getBillModel().getValueAt(i, "ivalidday") ,new Integer(BillStatus.FREE));
    dvaliddate = (UFDate) getBillCardPanel().getBillModel().getValueAt(i, "dvaliddate");
    if(dvaliddate!=null){
      bm.setValueAt(dvaliddate.getDateBefore(intValiddays), i, "dproducedate");
    }

  }
  checkVprocessbatch(getCacheVOs());
  //逐行处理 -------------------------------------------------- 结束

  //加载来源信息、源头信息
  PuTool.loadSourceInfoAll(getBillListPanel(),BillTypeConst.PO_ARRIVE);
  //add by QuSida 2010-9-11 (佛山骏杰)  --- begin
  //function:查询相关费用信息
  String pk = (String)getCacheVOs()[row0].getHeadVO().getAttributeValue("carriveorderid");
	String sql = "cbillid = '"+pk+"' and dr = 0";
	InformationCostVO[] vos = null;

	 try {
		vos = (InformationCostVO[])JJPuScmPubHelper.querySmartVOs(InformationCostVO.class, null, sql);
	} catch (Exception e) {
		// TODO Auto-generated catch block
		SCMEnv.out(e);
	}
if(vos!=null&&vos.length!=0){
	
	 getBillListPanel().getBodyBillModel("jj_scm_informationcost").setBodyDataVO(vos);
	 getBillListPanel().getBodyBillModel().execLoadFormula();//20101010-Meichao 执行表体所有加载公式
//	 getBillListPanel().getBodyBillModel("jj_scm_informationcost").execLoadFormula();
}else{
	//20101010 2204 Meichao新增此else判断: 当费用信息为空时.清空之前的费用页签数据.
	getBillListPanel().getBodyBillModel("jj_scm_informationcost").setBodyDataVO(null);
}
//add by QuSida 2010-9-11 (佛山骏杰)  --- end
  return isErr;
}

/**
 * 功能：列表换行时发生并发后列表按钮逻辑设置
 * 作者：晁志平
 * 日期：(2003-2-24 17:02:24)
 */
private void setButtonsListWhenErr() {
  //
  for (int i = 0; i < m_aryArrListButtons.length; i++) {
    m_aryArrListButtons[i].setEnabled(false);
  }
  //
  m_btnQuery.setEnabled(true);
  m_btnBrowses.setEnabled(true);
  m_btnRefresh.setEnabled(true);
  //
  updateButtonsAll();
}

/**
 * @功能：设置到货单据数组
 * @作者：晁志平
 * 创建日期：(2001-6-19 20:13:12)
 * @param:<|>
 * @return:
 * @exception:
 * @see;
 * @since:
 *
 * @param newM_arriveVOs nc.vo.rc.receive.ArriveorderVO[]
 */
public void setCacheVOs(nc.vo.rc.receive.ArriveorderVO[] newM_arriveVOs) {
  m_arriveVOs = newM_arriveVOs;
}

/**
 * @功能：设置当前显示索引号
 * @作者：晁志平
 * 创建日期：(2001-6-20 8:47:47)
 * @param:<|>
 * @return:
 * @exception:
 * @see;
 * @since:
 *
 * @param newM_iArrCurrRow int
 */
private void setDispIndex(int newM_iArrCurrRow) {
  m_iArrCurrRow = newM_iArrCurrRow;
}
/**
 * @功能：设置当前单据维护状态
 * @作者：晁志平
 * 创建日期：(2001-6-19 20:18:22)
 * @param:<|>
 * @return:
 * @exception:
 * @see;
 * @since:
 *
 * @param newM_strState java.lang.String
 */
private void setM_strState(java.lang.String newM_strState) {
  m_strState = newM_strState;
}


/**
 * 功能描述:设置数值类型字段取值范围
 *      /
 *      | false: 任意取值
 * isBack = |
 *      | true : 负
 *      \
 */
private void setNumFieldsNeg(boolean isBack) {
  double iMin = nc.vo.scm.field.pu.FieldMinMaxValue.MIN_NUM;
  double iMax = nc.vo.scm.field.pu.FieldMinMaxValue.MAX_NUM;
  if (isBack) {
    iMax = 0;
  }
  //本次收货
  UIRefPane refNarrvnum = (UIRefPane) getBillCardPanel().getBodyItem("narrvnum").getComponent();
  refNarrvnum.setMinValue(iMin);
  refNarrvnum.setMaxValue(iMax);
  //辅数量
  UIRefPane refNassistnum = (UIRefPane) getBillCardPanel().getBodyItem("nassistnum").getComponent();
  refNassistnum.setMinValue(iMin);
  refNassistnum.setMaxValue(iMax);
  refNarrvnum.setMinValue(iMin);
  refNarrvnum.setMaxValue(iMax);
  refNarrvnum.setMinValue(iMin);
  refNarrvnum.setMaxValue(iMax);
  refNarrvnum.setMinValue(iMin);
  refNarrvnum.setMaxValue(iMax);
}

/**
 * 功能：到货转入时切换、修改、作废、全选、全消、文档管理按钮显示逻辑及当前单据定位
 * 按钮逻辑
 *   @切换：有且只有一行选中时有效
 *   @修改：无效
 *   @打印：无效
 *   @作废：无效
 *   @全选：无效
 *   @全消：无效
 *   @文档管理：无效
 *   @存量查询：有效
 *   @放弃转单：有效
 */
private void setButtonsListValueChangedNew(int cnt) {
  for (int i = 0; i < m_aryArrListButtons.length; i++) {
    m_aryArrListButtons[i].setEnabled(false);
  }
  m_btnMaintains.setEnabled(true);
  //
  m_btnDiscard.setEnabled(false);
  //
  int iLen = m_btnOthers.getChildCount();
  for (int i = 0; i < iLen; i++) {
    ((ButtonObject)m_btnOthers.getChildren().elementAt(i)).setEnabled(false);
  }
  m_btnOthersList.setEnabled(true);
  /*“放弃转单”可用*/
  m_btnEndCreate.setVisible(true);
  m_btnEndCreate.setEnabled(true);
  m_btnUsableList.setEnabled(true);
  m_btnQueryBOMList.setEnabled(true);
  if (cnt == 1) {
    m_btnModifyList.setEnabled(true);
  }
  //
  updateButtonsAll();
}

/**
 * @功能：设置库存组织与仓库匹配
 */
private void setOrgWarhouse() {
  UIRefPane ref =
    (UIRefPane) getBillCardPanel()
      .getHeadItem("cstoreorganization")
      .getComponent();
  String sPkCalBody = ref.getRefPK();
  PuTool.restrictWarehouseRefByStoreOrg(
    getBillCardPanel(),
    getCorpPrimaryKey(),
    sPkCalBody,
    "cwarehousename");
}

private void setRefPaneAssistunit(int row) {
  //存货基本ID与主计量ID
  Object cbaseid = getBillCardPanel().getBillModel().getValueAt(row, "cbaseid");
  //设置辅计量单位参照
  UIRefPane ref =
    (UIRefPane) getBillCardPanel().getBodyItem("cassistunitname").getComponent();
  String wherePart = "bd_convert.pk_invbasdoc='" + cbaseid + "' ";
  ref.setWhereString(wherePart);
  String unionPart = " union all \n";
  unionPart
    += "(select bd_measdoc.shortname,bd_measdoc.measname,bd_invbasdoc.pk_measdoc \n";
  unionPart += "from bd_invbasdoc \n";
  unionPart += "left join bd_measdoc  \n";
  unionPart += "on bd_invbasdoc.pk_measdoc=bd_measdoc.pk_measdoc \n";
  unionPart += "where bd_invbasdoc.pk_invbasdoc='" + cbaseid + "') \n";
  ref.getRefModel().setGroupPart(unionPart);
}
/**
 * 更换按钮
 * 创建日期：(2001-3-17 9:00:09)
 */
private void updateButtonsMy() {
  if (getStateStr().equals("到货单列表"))
    setButtons(m_aryArrListButtons);
  else
    for (int i = 0; i < m_aryArrCardButtons.length; i++){
      updateButton(m_aryArrCardButtons[i]);
    }
}

/**
  * Called whenever the value of the selection changes.
  * @param e the event that characterizes the change.
  */
public void valueChanged(javax.swing.event.ListSelectionEvent e) {
  boolean isErr = false;
  if (!e.getValueIsAdjusting())
    return;
  int m_nFirstSelectedIndex = -1;
  //选中行数
  int iSelCnt = 0;
  //所有置为未选中
  int iCount = getBillListPanel().getHeadTable().getRowCount();
  for (int i = 0; i < iCount; i++) {
    getBillListPanel().getHeadBillModel().setRowState(i, BillModel.NORMAL);
  }
  //得到被选中的行
  int[] iaSelectedRow = getBillListPanel().getHeadTable().getSelectedRows();
  if (iaSelectedRow == null || iaSelectedRow.length == 0) {
    m_nFirstSelectedIndex = -1;
  } else {
    iSelCnt = iaSelectedRow.length;
    //m_nFirstSelectedIndex = iaSelectedRow[0];
    //选中的行表示为打＊号
    for (int i = 0; i < iSelCnt; i++) {
      getBillListPanel().getHeadBillModel().setRowState(
        iaSelectedRow[i],
        BillModel.SELECTED);
    }
  }
  if (iSelCnt == 1 && iaSelectedRow != null && iaSelectedRow.length > 0) {
    m_nFirstSelectedIndex = iaSelectedRow[0];
  }
  if (m_nFirstSelectedIndex < 0) {
    getBillListPanel().setBodyValueVO(null);
  } else {
    int nCurIndex =
      nc.ui.pu.pub.PuTool.getIndexBeforeSort(
        getBillListPanel(),
        m_nFirstSelectedIndex);
    setDispIndex(nCurIndex);
    isErr = setListBodyData(nCurIndex);
    //刷新
    getBillListPanel().getBodyTabbedPane().setSelectedIndex(0);//20101010-Meichao 修改焦点页签为子表页签.从而使公式执行成功显示.
    getBillListPanel().getBodyBillModel("jj_scm_informationcost").execLoadFormula();
    getBillListPanel().getBodyTable().updateUI();
    
  }
  //按钮逻辑
  if ("转入列表".equals(getStateStr())) {
    setButtonsListValueChangedNew(iSelCnt);
  } else {
    setButtonsList();
  }
  //如果发生业务异常则重新设置功能按钮
  if (isErr){
    setButtonsListWhenErr();
  }
  updateButtons();
}
/**
 * 功能描述:自定义项保存PK(表体)
 */
private void setBodyDefPK(BillEditEvent event) {
  if (event.getKey().equals("vdef1")) {
    DefSetTool.afterEditBody(getBillCardPanel().getBillModel(), event
        .getRow(), "vdef1", "pk_defdoc1");
  } else if (event.getKey().equals("vdef2")) {
    DefSetTool.afterEditBody(getBillCardPanel().getBillModel(), event
        .getRow(), "vdef2", "pk_defdoc2");
  } else if (event.getKey().equals("vdef3")) {
    DefSetTool.afterEditBody(getBillCardPanel().getBillModel(), event
        .getRow(), "vdef3", "pk_defdoc3");
  } else if (event.getKey().equals("vdef4")) {
    DefSetTool.afterEditBody(getBillCardPanel().getBillModel(), event
        .getRow(), "vdef4", "pk_defdoc4");
  } else if (event.getKey().equals("vdef5")) {
    DefSetTool.afterEditBody(getBillCardPanel().getBillModel(), event
        .getRow(), "vdef5", "pk_defdoc5");
  } else if (event.getKey().equals("vdef6")) {
    DefSetTool.afterEditBody(getBillCardPanel().getBillModel(), event
        .getRow(), "vdef6", "pk_defdoc6");
  } else if (event.getKey().equals("vdef7")) {
    DefSetTool.afterEditBody(getBillCardPanel().getBillModel(), event
        .getRow(), "vdef7", "pk_defdoc7");
  } else if (event.getKey().equals("vdef8")) {
    DefSetTool.afterEditBody(getBillCardPanel().getBillModel(), event
        .getRow(), "vdef8", "pk_defdoc8");
  } else if (event.getKey().equals("vdef9")) {
    DefSetTool.afterEditBody(getBillCardPanel().getBillModel(), event
        .getRow(), "vdef9", "pk_defdoc9");
  } else if (event.getKey().equals("vdef10")) {
    DefSetTool.afterEditBody(getBillCardPanel().getBillModel(), event
        .getRow(), "vdef10", "pk_defdoc10");
  } else if (event.getKey().equals("vdef11")) {
    DefSetTool.afterEditBody(getBillCardPanel().getBillModel(), event
        .getRow(), "vdef11", "pk_defdoc11");
  } else if (event.getKey().equals("vdef12")) {
    DefSetTool.afterEditBody(getBillCardPanel().getBillModel(), event
        .getRow(), "vdef12", "pk_defdoc12");
  } else if (event.getKey().equals("vdef13")) {
    DefSetTool.afterEditBody(getBillCardPanel().getBillModel(), event
        .getRow(), "vdef13", "pk_defdoc13");
  } else if (event.getKey().equals("vdef14")) {
    DefSetTool.afterEditBody(getBillCardPanel().getBillModel(), event
        .getRow(), "vdef14", "pk_defdoc14");
  } else if (event.getKey().equals("vdef15")) {
    DefSetTool.afterEditBody(getBillCardPanel().getBillModel(), event
        .getRow(), "vdef15", "pk_defdoc15");
  } else if (event.getKey().equals("vdef16")) {
    DefSetTool.afterEditBody(getBillCardPanel().getBillModel(), event
        .getRow(), "vdef16", "pk_defdoc16");
  } else if (event.getKey().equals("vdef17")) {
    DefSetTool.afterEditBody(getBillCardPanel().getBillModel(), event
        .getRow(), "vdef17", "pk_defdoc17");
  } else if (event.getKey().equals("vdef18")) {
    DefSetTool.afterEditBody(getBillCardPanel().getBillModel(), event
        .getRow(), "vdef18", "pk_defdoc18");
  } else if (event.getKey().equals("vdef19")) {
    DefSetTool.afterEditBody(getBillCardPanel().getBillModel(), event
        .getRow(), "vdef19", "pk_defdoc19");
  } else if (event.getKey().equals("vdef20")) {
    DefSetTool.afterEditBody(getBillCardPanel().getBillModel(), event
        .getRow(), "vdef20", "pk_defdoc20");
  }
}

/**
 * 功能描述:自定义项保存PK(表头)
 */
private void setHeadDefPK(BillEditEvent event) {
  if (event.getKey().equals("vdef1")) {
    DefSetTool.afterEditHead(getBillCardPanel().getBillData(), "vdef1",
        "pk_defdoc1");
  } else if (event.getKey().equals("vdef2")) {
    DefSetTool.afterEditHead(getBillCardPanel().getBillData(), "vdef2",
        "pk_defdoc2");
  } else if (event.getKey().equals("vdef3")) {
    DefSetTool.afterEditHead(getBillCardPanel().getBillData(), "vdef3",
        "pk_defdoc3");
  } else if (event.getKey().equals("vdef4")) {
    DefSetTool.afterEditHead(getBillCardPanel().getBillData(), "vdef4",
        "pk_defdoc4");
  } else if (event.getKey().equals("vdef5")) {
    DefSetTool.afterEditHead(getBillCardPanel().getBillData(), "vdef5",
        "pk_defdoc5");
  } else if (event.getKey().equals("vdef6")) {
    DefSetTool.afterEditHead(getBillCardPanel().getBillData(), "vdef6",
        "pk_defdoc6");
  } else if (event.getKey().equals("vdef7")) {
    DefSetTool.afterEditHead(getBillCardPanel().getBillData(), "vdef7",
        "pk_defdoc7");
  } else if (event.getKey().equals("vdef8")) {
    DefSetTool.afterEditHead(getBillCardPanel().getBillData(), "vdef8",
        "pk_defdoc8");
  } else if (event.getKey().equals("vdef9")) {
    DefSetTool.afterEditHead(getBillCardPanel().getBillData(), "vdef9",
        "pk_defdoc9");
  } else if (event.getKey().equals("vdef10")) {
    DefSetTool.afterEditHead(getBillCardPanel().getBillData(), "vdef10",
        "pk_defdoc10");
  } else if (event.getKey().equals("vdef11")) {
    DefSetTool.afterEditHead(getBillCardPanel().getBillData(), "vdef11",
        "pk_defdoc11");
  } else if (event.getKey().equals("vdef12")) {
    DefSetTool.afterEditHead(getBillCardPanel().getBillData(), "vdef12",
        "pk_defdoc12");
  } else if (event.getKey().equals("vdef13")) {
    DefSetTool.afterEditHead(getBillCardPanel().getBillData(), "vdef13",
        "pk_defdoc13");
  } else if (event.getKey().equals("vdef14")) {
    DefSetTool.afterEditHead(getBillCardPanel().getBillData(), "vdef14",
        "pk_defdoc14");
  } else if (event.getKey().equals("vdef15")) {
    DefSetTool.afterEditHead(getBillCardPanel().getBillData(), "vdef15",
        "pk_defdoc15");
  } else if (event.getKey().equals("vdef16")) {
    DefSetTool.afterEditHead(getBillCardPanel().getBillData(), "vdef16",
        "pk_defdoc16");
  } else if (event.getKey().equals("vdef17")) {
    DefSetTool.afterEditHead(getBillCardPanel().getBillData(), "vdef17",
        "pk_defdoc17");
  } else if (event.getKey().equals("vdef18")) {
    DefSetTool.afterEditHead(getBillCardPanel().getBillData(), "vdef18",
        "pk_defdoc18");
  } else if (event.getKey().equals("vdef19")) {
    DefSetTool.afterEditHead(getBillCardPanel().getBillData(), "vdef19",
        "pk_defdoc19");
  } else if (event.getKey().equals("vdef20")) {
    DefSetTool.afterEditHead(getBillCardPanel().getBillData(), "vdef20",
        "pk_defdoc20");
  }
}
private boolean chechDataBeforeSave(ArriveorderVO newvo,ArriveorderVO oldvo){
  nc.vo.scm.pu.Timer timer= new nc.vo.scm.pu.Timer();
  timer.start("采购到货保存检查操作chechDataBeforeSave开始");

  int nError = -1;
  //检查单据行号
  if (!BillRowNo.verifyRowNosCorrect(getBillCardPanel(), "crowno")) {
    showHintMessage(m_lanResTool.getStrByID("SCMCOMMON","UPPSCMCommon-000010")/*@res "保存失败"*/);
    return false;
  }
  timer.addExecutePhase("检查单据行号verifyRowNosCorrect");
  //检查单据模板非空项
  try {
    nc.ui.pu.pub.PuTool.validateNotNullField(getBillCardPanel());
  } catch (Exception e) {
    showHintMessage(m_lanResTool.getStrByID("40040301","UPP40040301-000191")/*@res "保存失败:单据项目存在空项"*/);
    MessageDialog.showWarningDlg(this, m_lanResTool.getStrByID("40040301","UPP40040301-000192")/*@res "单据模板非空项检查"*/, e.getMessage());
    return false;
  }
  timer.addExecutePhase(m_lanResTool.getStrByID("40040301","UPP40040301-000193")/*@res "检查单据模板非空项validateNotNullField"*/);
  try {
    //检查表体数据正负合法性
    ArriveorderItemVO bodyVO[] = (ArriveorderItemVO[]) newvo.getChildrenVO();
    ArriveorderHeaderVO headVO = (ArriveorderHeaderVO) newvo.getParentVO();
    for (nError = 0; nError < bodyVO.length; nError++){
      if(headVO.getBisback().booleanValue() && bodyVO[nError].getNarrvnum() != null && bodyVO[nError].getNarrvnum().doubleValue() > 0) throw new ValidationException(m_lanResTool.getStrByID("40040301","UPP40040301-000275")/*@res"退货单数量必须为负!"*/);
      bodyVO[nError].validate();
    }
    //检查录入数据其它合法性
    if (!checkModifyData(newvo, oldvo)) {
      showHintMessage(m_lanResTool.getStrByID("SCMCOMMON","UPPSCMCommon-000010")/*@res "保存失败"*/);
      return false;
    }
    //检查录入数据是否超出数据库可容纳范围
    if (!nc.vo.scm.field.pu.FieldDBValidate.validate((ArriveorderItemVO[]) newvo.getChildrenVO())) {
      showHintMessage(m_lanResTool.getStrByID("40040301","UPP40040301-000194")/*@res "存在部分数据超出数据库可容纳范围,请检查"*/);
      return false;
    }
  } catch (ValidationException e) {
    String[] value = new String[]{String.valueOf(nError + 1),e.getMessage()};
    MessageDialog.showErrorDlg(this, m_lanResTool.getStrByID("40040301","UPP40040301-000195")/*@res "合法性检查"*/, m_lanResTool.getStrByID("40040301","UPP40040301-000196",null,value)/*@res "表体行"*/+  e.getMessage() );
    showHintMessage(m_lanResTool.getStrByID("SCMCOMMON","UPPSCMCommon-000010")/*@res "保存失败"*/);
    return false;
  }
  timer.addExecutePhase("数据检查合法性");
  timer.showAllExecutePhase("采购到货保存检查操作chechDataBeforeSave结束");

  return true;
}

/**
 * 作者：汪维敏
 * 功能：计算打印次数
 * 参数：无
 * 返回：无
 * 例外：无
 * 日期：(2004-12-15 11:39:21)
 * @throws BusinessException 
 * @throws ClassNotFoundException 
 * @throws IllegalAccessException 
 * @throws InstantiationException 
 */
private void onCardPrint() {
  ArriveorderVO vo = (ArriveorderVO) getCacheVOs()[getDispIndex()];
  ArrayList aryRslt = new ArrayList();
  aryRslt.add(vo);
  try {
    if (printCard == null){
        //目前还有南京蒲镇不想补空行
      if(nc.vo.scm.pub.CustomerConfigVO.getCustomerName().equalsIgnoreCase(nc.vo.scm.pub.CustomerConfigVO.NAME_NANJINGPUZHEN)){       
          PurchasePrintDS printData = new PurchasePrintDS(getModuleCode(),getBillCardPanel());      
//        printCard = new ScmPrintTool(getArrBillCardPanel(),printData,aryRslt);
        printCard = new ScmPrintTool(this,getBillCardPanel(),printData,aryRslt,getModuleCode());
      }else{
          printCard = new ScmPrintTool(this,getBillCardPanel(),aryRslt,getModuleCode());
      }
    } else{
      printCard.setData(aryRslt);     
    }
    printCard.onCardPrint(getBillCardPanel(),getBillListPanel(),ScmConst.PO_Arrive);
    if(PuPubVO.getString_TrimZeroLenAsNull(printCard.getPrintMessage()) != null){
      MessageDialog.showHintDlg(this,m_lanResTool.getStrByID("SCMCOMMON","UPPSCMCommon-000270")/*@res "提示"*/,printCard.getPrintMessage());
    }
  } catch (Exception e1) {
    SCMEnv.out(e1);
  }
}
/**
 * 作者：汪维敏
 * 功能：计算打印次数
 * 参数：无
 * 返回：无
 * 例外：无
 * 日期：(2004-12-15 11:39:21)
 */
  private void onCardPrintPreview() {
    
    if(getCacheVOs() == null || getCacheVOs().length == 0){
      return;
    }
    ArriveorderVO vo = (ArriveorderVO) getCacheVOs()[getDispIndex()];
    ArrayList aryRslt = new ArrayList();
    aryRslt.add(vo);
    try {
      if (printCard == null){
          //目前还有南京蒲镇不想补空行
        if(nc.vo.scm.pub.CustomerConfigVO.getCustomerName().equalsIgnoreCase(nc.vo.scm.pub.CustomerConfigVO.NAME_NANJINGPUZHEN)){       
            PurchasePrintDS printData = new PurchasePrintDS(getModuleCode(),getBillCardPanel());      
          printCard = new ScmPrintTool(getBillCardPanel(),printData,aryRslt,getModuleCode());
        }else{
            printCard = new ScmPrintTool(this,getBillCardPanel(),aryRslt,getModuleCode());
        }
      } else{
        printCard.setData(aryRslt);     
      }
      printCard.onCardPrintPreview(getBillCardPanel(),getBillListPanel(),ScmConst.PO_Arrive);
      try{
    	  //loadDataToCard();
      }catch(Exception e){
    	  SCMEnv.out("加载到货单数据时出错：");/*-=notranslate=-*/
    	  SCMEnv.out(e);
      } 

      if(PuPubVO.getString_TrimZeroLenAsNull(printCard.getPrintMessage()) != null){
        //MessageDialog.showHintDlg(this,m_lanResTool.getStrByID("SCMCOMMON","UPPSCMCommon-000270")/*@res "提示"*/,printCard.getPrintMessage());
    	showHintMessage(printCard.getPrintMessage());
      }
      //显示表头备注
      setHeadValueByKey("vmemo",(String) vo.getParentVO().getAttributeValue("vmemo"));
    } catch (Exception e1) {
      SCMEnv.out(e1);
    }
}
  /**
   * 解决表头备注显示问题
   * @param value
   */
  private void setHeadValueByKey(String key,String value){
	  if(getBillCardPanel().getHeadItem(key)!=null &&
			  getBillCardPanel().getHeadItem(key).getComponent() instanceof UIRefPane){
		  UIRefPane refPane = (UIRefPane) getBillCardPanel().getHeadItem(key).getComponent();
	      refPane.setValue(value);
	  }else
		  getBillCardPanel().getHeadItem(key).setValue(value);
  }
/**
 * 作者：汪维敏
 * 功能：批打印
 * 参数：无
 * 返回：无
 * 例外：无
 * 日期：(2004-12-15 11:39:21)
 */
private void onBatchPrint() {
  if (printList == null){
    printList = new ScmPrintTool(this,getBillCardPanel(),getSelectedBills(),getModuleCode());
  } else{
    try {
      printList.setData(getSelectedBills());
    } catch (BusinessException e1) {
      MessageDialog.showHintDlg(this,m_lanResTool.getStrByID("SCMCOMMON","UPPSCMCommon-000270")/*@res "提示"*/,e1.getMessage());
    }
  }
  try {
    printList.onBatchPrint(getBillListPanel(),getBillCardPanel(),ScmConst.PO_Arrive);
    if(PuPubVO.getString_TrimZeroLenAsNull(printList.getPrintMessage()) != null){
    	this.showHintMessage(printList.getPrintMessage());
    }
  } catch (BusinessException e) {
    SCMEnv.out(e.getMessage());
  }
}
private void onSplitPrint() {

	if (getBillListPanel().getHeadTable().getRowCount() > 0) {
		int rowSelected = getBillListPanel().getHeadTable().getSelectedRowCount();
		if (rowSelected == 0 || rowSelected > 1) {
			showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("401201", "UPP401201-000045")/*@res "请选中一张单据"*/);
			return;
		}
	}

	if (getCacheVOs() == null || getCacheVOs().length == 0) {
		return;
	}
	SplitPrintParamDlg s=new SplitPrintParamDlg(this,initSplitParams());
	s.showModal();
//	批量打印工具
	SplitParams[] paramvos = null;

	if(s.isCloseByBtnOK()){

		ArrayList<AggregatedValueObject> prlistvo = new ArrayList<AggregatedValueObject>();
		ArriveorderVO vo = getCacheVOs()[getDispIndex()];
		if(vo == null || vo.getBodyLen() < 1){
			return;
		}
		ArriveorderVO newvo = null;
		//从界面上获取数据。缓存VO里"存货编码"、"存货名称"等字段无值
		if(getBillCardPanel().isShowing()){
			newvo = (ArriveorderVO) getBillCardPanel().getBillValueVO(ArriveorderVO.class.getName(), ArriveorderHeaderVO.class.getName(), ArriveorderItemVO.class.getName());
		}else{
			int selrow = getBillListPanel().getHeadTable().getSelectedRow();
			newvo = (ArriveorderVO) getBillListPanel().getBillValueVO(selrow, ArriveorderVO.class.getName(),ArriveorderHeaderVO.class.getName(), ArriveorderItemVO.class.getName());
			ArriveorderItemVO[] items = (ArriveorderItemVO[]) getBillListPanel().getBodyBillModel().getBodyValueVOs(ArriveorderItemVO.class.getName());
			newvo.setChildrenVO(items);
		}
		prlistvo.add(newvo);

		paramvos = s.getSplitParams();

		BillPrintTool printTool = null;
		try {
			printTool = new BillPrintTool(getFuncId(), prlistvo,getBillCardPanel().getBillData(), null, getCorpPrimaryKey(),getOperatorId(), "varrordercode", "carriveorderid");
			printTool.onBatchSplitPrintPreview(getBillListPanel(),BillTypeConst.PO_ARRIVE, paramvos);
		}
		catch (BusinessException e) {
			MessageDialog.showHintDlg(this, m_lanResTool.getStrByID("SCMCOMMON","UPPSCMCommon-000270")/* @res "提示" */, e.getMessage());
		}
		catch (InstantiationException e) {
			MessageDialog.showHintDlg(this, m_lanResTool.getStrByID("SCMCOMMON","UPPSCMCommon-000270")/* @res "提示" */, e.getMessage());
		}
		catch (IllegalAccessException e) {
			MessageDialog.showHintDlg(this, m_lanResTool.getStrByID("SCMCOMMON","UPPSCMCommon-000270")/* @res "提示" */, e.getMessage());
		}
		catch (InterruptedException e) {
			MessageDialog.showHintDlg(this, m_lanResTool.getStrByID("SCMCOMMON","UPPSCMCommon-000270")/* @res "提示" */, e.getMessage());
		}
		catch (ClassNotFoundException e) {
			MessageDialog.showHintDlg(this, m_lanResTool.getStrByID("SCMCOMMON","UPPSCMCommon-000270")/*@res "提示"*/, e.getMessage());
		}
	}
}
/**
 * 作者：汪维敏
 * 功能：批打印
 * 参数：无
 * 返回：无
 * 例外：无
 * 日期：(2004-12-15 11:39:21)
 */
private void onBatchPrintPreview() {
  if (printList == null){
    printList = new ScmPrintTool(this,getBillCardPanel(),getSelectedBills(),getModuleCode());
  } else{
    try {
      printList.setData(getSelectedBills());
    } catch (BusinessException e1) {
      MessageDialog.showHintDlg(this,m_lanResTool.getStrByID("SCMCOMMON","UPPSCMCommon-000270")/*@res "提示"*/,e1.getMessage());
    }
  }
  try {
    printList.onBatchPrintPreview(getBillListPanel(),getBillCardPanel(),ScmConst.PO_Arrive);
    if(PuPubVO.getString_TrimZeroLenAsNull(printList.getPrintMessage()) != null){
    	this.showHintMessage(printList.getPrintMessage());
       //MessageDialog.showHintDlg(this,m_lanResTool.getStrByID("SCMCOMMON","UPPSCMCommon-000270")/*@res "提示"*/,printList.getPrintMessage());
    }
  } catch (BusinessException e) {
  }
}
/**
 * 功能描述:加载单据模板之前的初始化
 */

private void initBillBeforeLoad(BillData bd) {

  //---------单据模板加载前的初始化－－－－－－－
  if (bd != null && bd.getBodyItem("vfree0") != null && bd.getBodyItem("vfree0") != null) {
    FreeItemRefPane m_firpFreeItemRefPane = new FreeItemRefPane();
    m_firpFreeItemRefPane.setMaxLength(bd.getBodyItem("vfree0").getLength());
    //加监听器
    m_firpFreeItemRefPane.getUIButton().addActionListener(this);
    bd.getBodyItem("vfree0").setComponent(m_firpFreeItemRefPane);
  }
  //初始化参照
  initRefPane(bd);

  //初始化ComboBox
//  initComboBox(bd);
  //初始化精度
//  initDecimal(bd);

}

/**
 * 二次开发功能扩展按钮，要求二次开发子类给出具体实现
 * 
 * @see nc.ui.scm.pub.bill.IBillExtendFun#getExtendBtns()
 */
public ButtonObject[] getExtendBtns() {
	if(extendBtns == null || extendBtns.length == 0){
		//加入费用录入按钮 add by QuSida 2010-8-10  （佛山骏杰）
		extendBtns = new ButtonObject[]{getBoInfoCost()};
	return extendBtns;}
	else return extendBtns;
}

/**
 * 点击二次开发按钮后的响应处理，要求二次开发子类给出具体实现
 * 
 * @see nc.ui.scm.pub.bill.IBillExtendFun#onExtendBtnsClick(nc.ui.pub.ButtonObject)
 */
public void onExtendBtnsClick(ButtonObject bo) {
	 if(bo == getBoInfoCost()){
	    	this.onBoInfoCost();
	    }
}
/**
 * 二次开发状态与原有界面状态处理绑定，要求二次开发子类给出具体实现
 *
 *  状态数值对照表：
 * 
 *  0：初始化
 *  1：到货浏览
 *  2：到货修改
 *  3：到货列表
 *  4：转入列表
 *  5：转入修改
 */
public void setExtendBtnsStat(int iState) {

	//设置费用录入按钮的状态  add by QuSida 2010-8-29 (佛山骏杰)
	if(iState==0){
		getBoInfoCost().setEnabled(false);
		updateButton(getBoInfoCost());
	}
	else if(iState==1){
		getBoInfoCost().setEnabled(false);
		updateButton(getBoInfoCost());
	}
	else if(iState==2){
		getBoInfoCost().setEnabled(true);
		updateButton(getBoInfoCost());
	}
	else if(iState==3){
		getBoInfoCost().setEnabled(false);
		updateButton(getBoInfoCost());
	}
	else if(iState==4){
		getBoInfoCost().setEnabled(false);
		updateButton(getBoInfoCost());
	}
	else if(iState==5){
		getBoInfoCost().setEnabled(true);
		updateButton(getBoInfoCost());
	}
	else if(iState==6){
		getBoInfoCost().setEnabled(false);
		updateButton(getBoInfoCost());
	}
		

}
/**
 *  送审到货单
 * <p>
 * <strong>调用模块：</strong>采购管理
 * <p>
 * <strong>最后修改人：</strong>czp
 * <p>
 * <strong>最后修改日期：</strong>2006-02-09
 * <p>
 * <strong>用例描述：</strong>
 * <p>
 * @param    无
 * @return   无
 * @throws   无
 * @since    NC50
 * @see      
 */
private void onSendAudit(){

  // 编辑状态送审＝“保存”＋“送审”
  if (getStateStr().equals("转入修改") || getStateStr().equals("到货修改")) {
    onSave();
  }
  //该操作人是否有审批任务
  if(getCacheVOs() == null || getDispIndex() < 0)
    return;
  ArriveorderVO vo = getCacheVOs()[getDispIndex()];
  
  if(isNeedSendAudit(getCacheVOs()[getDispIndex()])){
    try {
      HashMap<String, String> hmPfExParams = new HashMap<String, String>();
      hmPfExParams.put(PfUtilBaseTools.PARAM_RELOAD_VO, PfUtilBaseTools.PARAM_RELOAD_VO);
      PfUtilClient.processAction("SAVE",BillTypeConst.PO_ARRIVE, ClientEnvironment.getInstance().getDate().toString(), vo);
      
      /*审批成功后刷新*/   
      refreshVoFieldsByKey(vo,vo.getParentVO().getPrimaryKey());
      //
      getCacheVOs()[getDispIndex()] = vo;
      /*加载单据:有效率优化空间*/
      loadDataToCard();
      /*刷新按钮状态*/
      setButtonsState();
    } catch (Exception e) {
      SCMEnv.out("到货单送审失败：");
      SCMEnv.out(e);
      if (e instanceof BusinessException || e instanceof RuntimeException) {
        MessageDialog.showErrorDlg(
            this, 
            m_lanResTool.getStrByID("SCMCOMMON", "UPPSCMCommon-000270")/*@res "提示"*/, 
            e.getMessage());
      } else {
        MessageDialog.showErrorDlg(
            this, 
            m_lanResTool.getStrByID("SCMCOMMON", "UPPSCMCommon-000270")/*@res "提示"*/, 
            m_lanResTool.getStrByID("SCMCOMMON", "UPPSCMCommon-000408")/*@res"送审失败！"*/);
      }
    }
  }
  showHintMessage(m_lanResTool.getStrByID("common","UCH023")/*@res "已送审"*/);
}
/**
 *  判断到货单是否有必要送审
 * <p>
 * <strong>调用模块：</strong>采购管理
 * <p>
 * <strong>最后修改人：</strong>czp
 * <p>
 * <strong>最后修改日期：</strong>2006-02-09
 * <p>
 * <strong>用例描述：</strong>要求同时满足，
 * <p>
 * 1)、单据状态为“自由”(目前与请购单、采购订单保持一致，审批不通过需要[修改]-[保存]，即将单据状态置为自由，才可送审)
 * <p>
 * 2)、定义了审批流
 * <p>
 * @param    无
 * @return   无
 * @throws   无
 * @since    NC50
 * @see      
 */
private boolean isNeedSendAudit(ArriveorderVO vo){

  boolean bRet = false;
  if(vo == null || vo.getHeadVO() == null)
    return false;
  String billid = vo.getHeadVO().getCarriveorderid();
  String busiType = vo.getHeadVO().getCbiztype();
  boolean isNeedSendToAuditQ = BusiBillManageTool.isNeedSendToAudit(BillTypeConst.PO_ARRIVE,getCorpPrimaryKey(), busiType, billid, getClientEnvironment().getUser().getPrimaryKey());
  bRet = (isNeedSendToAuditQ
      && vo.getHeadVO().getIbillstatus() != null
      && (vo.getHeadVO().getIbillstatus().intValue() == 0 || vo.getHeadVO().getIbillstatus().intValue() == 2 || vo.getHeadVO().getIbillstatus().intValue() == 4));
  return bRet;
}

/**
 *  获取当前VO，消息中心用
 * <p>
 * <strong>调用模块：</strong>采购管理
 * <p>
 * <strong>最后修改人：</strong>czp
 * <p>
 * <strong>最后修改日期：</strong>2006-02-10
 * <p>
 * <strong>用例描述：</strong>
 * <p>
 * @param    无
 * @return   消息中心显示的业务单据VO
 * @throws   无
 * @since    NC50
 * @see      
 */
public AggregatedValueObject getVo() throws Exception {
  ArriveorderVO vo = null;
  if(getCacheVOs() != null && getCacheVOs().length == 1 && getCacheVOs()[0] != null){
    SCMEnv.out("缓存中有值，不必重新查询!");
    return getCacheVOs()[0];
  }
  try{
    vo = ArriveorderHelper.findByPrimaryKey(m_strBillId);
  }catch(Exception e){
    PuTool.outException(this,e);
  }
  return vo;
}
/**
 * 查询当前单据审批状态
 */
private void onQueryForAudit() {
  if (getCacheVOs() != null 
      && getCacheVOs().length > 0 
      && getCacheVOs()[0] != null 
      && getCacheVOs()[0].getHeadVO() != null) {
    FlowStateDlg approvestatedlg = new FlowStateDlg(
        this,
        BillTypeConst.PO_ARRIVE,
        getCacheVOs()[0].getHeadVO().getCbiztype(),
        getCacheVOs()[0].getHeadVO().getPrimaryKey());
    approvestatedlg.showModal();
  }
  showHintMessage(m_lanResTool.getStrByID("common","UCH035")/*@res "审批状态查询成功"*/);
}
/**
 * <p>排序方法，返回要排序的缓存VO数组
 * @since V50
 */
public Object[] getRelaSortObjectArray() {
  return getCacheVOs();
}

/**
 * 界面关联接口方法实现 -- 维护
 **/
public void doMaintainAction(ILinkMaintainData maintaindata) {
  SCMEnv.out("进入维护接口...");

  if(maintaindata == null || maintaindata.getBillID() == null){
    SCMEnv.out("msgVo 为空，直接返回!");
    SCMEnv.out("****************以下是调用堆栈不是错误：****************");
    SCMEnv.out(new Exception());
    SCMEnv.out("****************以上是调用堆栈不是错误：****************");
    return;
  }
  //加载卡片
  setLayout(new java.awt.BorderLayout());
  add(getBillCardPanel(),java.awt.BorderLayout.CENTER);
  //设置按钮组
  setButtons(m_aryArrCardButtons);
  /*
  for(int i=0;i<m_aryMsgCenter.length;i++){
    m_aryMsgCenter[i].setEnabled(true);
  }
  */
  //查询、加载数据
  ArriveorderVO vo = null;
  //记录单据ID，getVo()用
  m_strBillId = maintaindata.getBillID();
  try{
    vo = ArriveorderHelper.findByPrimaryKey(m_strBillId);
  }catch(Exception e){
    PuTool.outException(this,e);
    return;
  }
  
  //如果当前登录公司不是操作员制单所在公司，则界面无操作按钮，仅提供浏览功能，by chao , xy , 2006-11-07
  String strLoginCorpId = PoPublicUIClass.getLoginPk_corp();
  String strPrayCorpId = vo == null ? null : vo.getHeadVO().getPk_corp();
  boolean bSameCorpFlag = strLoginCorpId.equals(strPrayCorpId);
  
  if(vo == null){
    if(!bSameCorpFlag){
      setButtonsNull();
    }
    return;
  }
  setCacheVOs(new ArriveorderVO[]{vo});
  for (int i = 0; i < getCacheVOs().length; i++) {
    if (getCacheVOs()[i].getChildrenVO() != null
      && getCacheVOs()[i].getChildrenVO().length > 0) {
      //给单据赋来源属性
      String cupsourcebilltype =
        ((ArriveorderItemVO[]) getCacheVOs()[i].getChildrenVO())[0]
          .getCupsourcebilltype();
      ((ArriveorderVO) getCacheVOs()[i]).setUpBillType(cupsourcebilltype);
      //刷新表体哈希表缓存
      for (int j = 0; j < getCacheVOs()[i].getChildrenVO().length; j++) {
        try{
          if (getCacheVOs()[i].getChildrenVO()[j].getPrimaryKey() == null){
            continue;
          }
          if (getCacheVOs()[i].getChildrenVO()[j] == null){
            continue;
          }
          hBodyItem.put(
            getCacheVOs()[i].getChildrenVO()[j].getPrimaryKey(),
            getCacheVOs()[i].getChildrenVO()[j]);
        }catch(BusinessException e){
          PuTool.outException(this,e);
          return;
        }
      }
    }
  }
  setDispIndex(0);
  try{
    loadDataToCard();
  }catch(Exception e){
    SCMEnv.out("加载到货单数据时出错：");/*-=notranslate=-*/
    SCMEnv.out(e);
  } 
  //不是同一公司，所有按钮不可见
  if(!bSameCorpFlag){
    setButtonsNull();
    return;
  }
  //如果是同一公司 
//  if(vo.isCanBeModified()){
//    onModify();
//  }else{
    setButtonsCard(); 
//  }
}
/**
 * 界面关联接口方法实现 -- 新增
 **/
public void doAddAction(ILinkAddData adddata) {

  SCMEnv.out("进入新增接口...");
  
  //默认此节点可打开
  m_strNoOpenReasonMsg = null;
  
  if(adddata == null){
    SCMEnv.out("ILinkAddData::adddata参数为空，直接返回");/*-=notranslate=-*/
    return;
  }
  String strUpBillType = adddata.getSourceBillType();
  //上游为采购订单
  if(BillTypeConst.PO_ORDER.equals(strUpBillType)){
    OrderVO voOrder = null;
    try{
      voOrder = OrderHelper.queryForOrderBillLinkAdd(new ClientLink(ClientEnvironment.getInstance()),adddata.getSourceBillID());
    }catch(Exception e){      
      SCMEnv.out(e);
      return;
    }
    //此节点是否可打开
    if(voOrder == null){
      MessageDialog.showHintDlg(this, 
          NCLangRes.getInstance().getStrByID("SCMCOMMON","UPPSCMCommon-000270")/* @res "提示" */, 
          NCLangRes.getInstance().getStrByID("40040301","UPP40040301-000287")/* @res "订单数据不能生成到货单，可能原因：1、订单业务类型走到货计划，但未生成到货计划；2、所有订单行均为劳务折扣属性；3、订单已经完全到货"*/);
      return;
    }
    ArrFrmOrdUI billReferUI = new ArrFrmOrdUI(
        "corderid",
        ClientEnvironment.getInstance().getCorporation().getPrimaryKey(),
        ClientEnvironment.getInstance().getUser().getPrimaryKey(),
        "4004020201",
        "null1",
        ScmConst.PO_Order,
        null,
        "nc.ui.po.pub.PoToRcQueDLG",
        ScmConst.PO_Arrive,
        this,
        false,
        true);
    // 加载数据
    billReferUI.loadDataForMsgCenter(new OrderVO[]{voOrder});
    //
    billReferUI.showModal();
    //
    if (billReferUI.getResult() == UIDialog.ID_OK) {
      ArriveorderVO[] retVOs = (ArriveorderVO[]) billReferUI.getRetVos();
      onExitFrmOrd(retVOs);
    }else{
      SCMEnv.out("取消本次生成操作");/*-=notranslate=-*/
      billReferUI.closeCancel();
      billReferUI.destroy();
    }
  }
  
}
/**
 * 检查打开该节点的前提条件。用于处理“只有满足某一条件时，才能打开该节点” 的情况。
 * 需要判断“只有满足某一条件时，才能打开该节点”的节点，需要实现本方法。 在方法内进行条件判断。
 * 基类根据返回值进行相应处理，如果返回值为一个非空字符串，那么基类不打开
 * 该节点，只在一个对话框中显示返回的字符串；如果返回值为null，那么基类象对待 其他节点一样打开该节点。
 * 
 * 创建日期：(2002-3-11 10:39:16)
 * 
 * @return java.lang.String
 */
protected String checkPrerequisite() {
    return m_strNoOpenReasonMsg;
}
/**
 * 界面关联接口方法实现 -- 审批
 **/
public void doApproveAction(ILinkApproveData approvedata) {
  SCMEnv.out("进入审批接口...");
  isRevise = true;
  if(approvedata == null || approvedata.getBillID() == null){
    SCMEnv.out("msgVo 为空，直接返回!");
    SCMEnv.out("****************以下是调用堆栈不是错误：****************");
    SCMEnv.out(new Exception());
    SCMEnv.out("****************以上是调用堆栈不是错误：****************");
    return;
  }
  //加载卡片
  setLayout(new java.awt.BorderLayout());
  add(getBillCardPanel(),java.awt.BorderLayout.CENTER);
  //查询、加载数据
  ArriveorderVO vo = null;
  //记录单据ID，getVo()用
  m_strBillId = approvedata.getBillID();
  try{
    vo = ArriveorderHelper.findByPrimaryKey(m_strBillId);
    if(vo == null) 
      return;
    setCacheVOs(new ArriveorderVO[]{vo});
    setDispIndex(0);
    loadDataToCard();
  }catch(Exception e){
    PuTool.outException(this,e);
  }
  getBillCardPanel().setEnabled(false);
  
  //登录公司与单据所属公司是否相同
  boolean bCorpSameFlag = getCorpPrimaryKey().equals(vo.getPk_corp()) ;
  //设置按钮组
  if(bCorpSameFlag){
    setButtons(m_aryArrCardButtons);
    setM_strState("到货浏览");
  }else{
    if(m_btnActionMsgCenter.getChildCount() == 0){
      m_btnActionMsgCenter.addChildButton(m_btnAudit);
      m_btnActionMsgCenter.addChildButton(m_btnUnAudit); 
    }
    if(m_btnOthersMsgCenter.getChildCount() == 0){
      m_btnOthersMsgCenter.addChildButton(m_btnQueryForAudit);
      m_btnOthersMsgCenter.addChildButton(m_btnDocument);
      m_btnOthersMsgCenter.addChildButton(m_btnLookSrcBill); 
    }
    setButtons(m_aryMsgCenter);
    //
    setM_strState("消息中心");
  }
  //
  setButtonsState();
  setButtonsRevise();

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
 * @param isCurAuditOPER
 *          是否当前操作人
 *          <p>
 * @author donggq
 * @time 2008-10-24 上午10:26:37
 */
private void setButtonsRevise() {
  if (isRevise) {
    m_btnAdds.setEnabled(false);
    m_btnDiscard.setEnabled(false);
    m_btnDiscardList.setEnabled(false);
    updateButton(m_btnDiscard);
    updateButton(m_btnDiscardList);
    updateButton(m_btnAdds);
    updateButton(m_btnModify);
    updateButton(m_btnModifyList);
  }

  if (getCacheVOs() != null) {
    Integer iBillStatus = PuPubVO.getInteger_NullAs(getCacheVOs()[getDispIndex()].getParentVO().getAttributeValue("ibillstatus"), new Integer(BillStatus.FREE));
    String strPsnOld = (getCacheVOs()[getDispIndex()]).getHeadVO().getCauditpsn();
    if (iBillStatus.intValue() == 2 && PuPubVO.getString_TrimZeroLenAsNull(strPsnOld) == null) {
      m_btnModify.setEnabled(true);
      m_btnModifyList.setEnabled(true);
      updateButton(m_btnModify);
      updateButton(m_btnModifyList);
    }
  }
}

/**
 * 界面关联接口方法实现 -- 逐级联查 
 **/
public void doQueryAction(ILinkQueryData querydata) {
  SCMEnv.out("进入逐级联查接口...");
  
  String billID = querydata.getBillID();
  
  
  //initialize();
  //
  setM_strState("到货浏览");
  //
  //ArriveorderVO vo = null;
  
  try {
    /*vo = ArriveorderHelper.findByPrimaryKey(billID);
    if(vo == null){
      MessageDialog.showHintDlg(this, NCLangRes.getInstance().getStrByID("SCMCOMMON","UPPSCMCommon-000270") "提示" , 
          m_lanResTool.getStrByID("common", "SCMCOMMON000000161") "没有察看单据的权限" );
      return;
    }*/
    //
    String strPkCorp = querydata.getPkOrg()==null?getCorpPrimaryKey():querydata.getPkOrg();/*vo.getPk_corp();*/
    
    //按照单据所属公司加载查询模版 
    RcQueDlg queryDlg = new RcQueDlg(this, getBusitype(), getFuncId(), getOperatorId(), strPkCorp);//查询模板中没有公司时，要设置虚拟公司
    queryDlg.setDefaultValue("po_arriveorder.dreceivedate","po_arriveorder.dreceivedate","");
    queryDlg.initCorpsRefs();
    //调用公共方法获取该公司中控制权限的档案条件VO数组
    ConditionVO[] condsUserDef = queryDlg.getDataPowerConVOs(strPkCorp,RcQueDlg.REFKEYS);
    //组织第二次查询单据，按照权限和单据PK过滤
    ArriveorderVO[] voaRet = ArriveorderHelper.queryAllArriveMy(condsUserDef, 
        null,
        strPkCorp, 
        null, 
        "po_arriveorder.carriveorderid = '" + billID + "' ");
    if(voaRet == null || voaRet.length <= 0 || voaRet[0] == null){
      MessageDialog.showHintDlg(this, NCLangRes.getInstance().getStrByID("SCMCOMMON","UPPSCMCommon-000270")/* "提示" */, 
          m_lanResTool.getStrByID("common", "SCMCOMMON000000161")/* "没有察看单据的权限" */);
      setButtonsNull();
      return;
    }
    setCacheVOs(voaRet);
    setDispIndex(0);
    loadDataToCard();
  } catch (Exception e) {
    SCMEnv.out(e);
    MessageDialog.showErrorDlg(this, NCLangRes.getInstance().getStrByID("SCMCOMMON","UPPSCMCommon-000270")/* "提示" */, 
        e.getMessage());
    setButtonsNull();
    return;
  }
  boolean bCorpSameFlag = getCorpPrimaryKey().equals(querydata.getPkOrg()/*vo.getPk_corp()*/) ;
  //设置按钮组
  if(bCorpSameFlag){
    setButtons(m_aryArrCardButtons);
    setButtonsCard();
  }else{
    setButtonsNull();
  }
}
/**
 * 清空当前界面按钮
 */
private void setButtonsNull(){
  ButtonObject[] objs = getButtons();
  int iLen = objs == null ? 0 : objs.length;
  for (int i = 0; i < iLen; i++) {
    if (objs[i] == null) {
      continue;
    }
    objs[i].setVisible(false);
    updateButton(objs[i]);
  }
}
/**
 * 合并显示、打印功能
 * 
 * @since v50
 */
private void onCombin() {
  CollectSettingDlg dlg = new CollectSettingDlg(this,
      NCLangRes.getInstance().getStrByID("common",
      "4004COMMON000000089")/*@res "合并显示"*/,
      ScmConst.PO_Arrive,
      "40040301",getCorpPrimaryKey(),
      ClientEnvironment.getInstance().getUser().getPrimaryKey(),
      ArriveorderVO.class.getName(),
      ArriveorderHeaderVO.class.getName(),
      ArriveorderItemVO.class.getName());
  //
  
  JComponent tempCom = null; 
  if(getBillCardPanel().getHeadItem("vmemo").getComponent() instanceof UIRefPane){
    tempCom = getBillCardPanel().getHeadItem("vmemo").getComponent(); 
    getBillCardPanel().getHeadItem("vmemo").setDataType(BillItem.STRING);
  } 

  dlg.initData(getBillCardPanel(),
  // 固定分组列
      new String[] { "cinventorycode", "cinventoryname",
    "cinventoryspec", "cinventorytype", "prodarea" },
      // 缺省分组列
      null,
      // 求和列
      new String[] { "nmoney", "narrvnum","nelignum","nnotelignum","nwastnum" },
      // 求平均列
      null,
      // 求加权平均列
      new String[] { "nprice" },
      // 数量列
      "narrvnum");
  dlg.showModal();

  if(tempCom != null){
      getBillCardPanel().getHeadItem("vmemo").setDataType(BillItem.UFREF);
      getBillCardPanel().getHeadItem("vmemo").setComponent(tempCom);
    }

  showHintMessage(m_lanResTool
      .getStrByID("common", "4004COMMON000000039")/* @res "合并显示完成" */);
  try{
	    loadDataToCard();
	  }catch(Exception e){
	    SCMEnv.out("加载到货单数据时出错：");/*-=notranslate=-*/
	    SCMEnv.out(e);
	  } 

}

/**
 * 设置资产卡片按钮状态。
 * <p>
 * <b>examples:</b>
 * <p>
 * 使用示例
 * <p>
 * <b>参数说明</b>
 * <p>
 * @author lixiaodong
 * @time 2008-5-12 下午02:48:14
 */
private void setBtnStatusSN() {
  if (getCacheVOs() == null || getCacheVOs().length <= 0) {
    return;
  }
  ArriveorderVO vo = getCacheVOs()[getDispIndex()];
  int row = -1;

  //只有卡片下可用
  int iBillStatus = PuPubVO.getInteger_NullAs(getCacheVOs()[getDispIndex()].getParentVO().getAttributeValue("ibillstatus"),new Integer(BillStatus.FREE));
  if (!m_bAIMEnabled || !getStateStr().equals("到货浏览") || m_btnAudit.isEnabled() || ((ArriveorderVO) vo).isReturn() || iBillStatus != BillStatus.AUDITED) {
    m_btnCreateCard.setEnabled(false);
    m_btnDeleteCard.setEnabled(false);
    return;
  }

  String bfaflag = "false";
  int intCrtNum = 0;
  int intLen = vo.getBodyLen();
  for (int i = 0; i < intLen;i++){
    bfaflag=getBillCardPanel().getBillModel().getValueAt(i, "bfaflag")==null?"":getBillCardPanel().getBillModel().getValueAt(i, "bfaflag").toString();
    if("true".equalsIgnoreCase(bfaflag)){
      intCrtNum++;
    }
  }

  if(intCrtNum==0){//没有生成卡片
    m_btnCreateCard.setEnabled(true);
    m_btnDeleteCard.setEnabled(false);
  }else if(intCrtNum==intLen){//全部生成卡片
    m_btnCreateCard.setEnabled(false);
    m_btnDeleteCard.setEnabled(true);
  }else{//部分生成卡片
    m_btnCreateCard.setEnabled(true);
    m_btnDeleteCard.setEnabled(true);
  }

  updateButtonsAll();
}

/**
 * 序列号分配。
 * <p>
 * <b>examples:</b>
 * <p>
 * 使用示例
 * <p>
 * <b>参数说明</b>
 * @param
 * @return
 * @throws
 * <p>
 * @author lixiaodong
 * @time 2008-5-28 下午01:08:26
 */
public boolean onSNAssign() {
  showHintMessage(m_lanResTool.getStrByID("40040301","UPT40040301-000137")/*@res"序列号分配开始!"*/);
  int row = -1;

  if (getCacheVOs() == null || getCacheVOs().length <= 0) {
    return false;
  }

  ArriveorderVO cardVos = null;
  if(cardVos==null){
    cardVos = (ArriveorderVO) getCacheVOs()[getDispIndex()].clone();
  }
  if (cardVos == null) {
    return false;
  }

  if(!cardVos.getHeadVO().isAuditted()){
    showHintMessage(m_lanResTool.getStrByID("40040301", "UPT40040301-000140")/* @res"单据未审批!" */);
    return false;
  }
  
  if(isFromSC())
  {
	  String sTitle = m_lanResTool.getStrByID("SCMCOMMON", "UPPSCMCommon-000270")/* @res "提示" */;
	  String sError = m_lanResTool.getStrByID("40040301", "UPT40040301-000157")/* @res"不支持来源为委外的到货单生成资产卡片" */;
      MessageDialog.showErrorDlg(this, sTitle, sError);
      return false;
  }
  
  String sTitle = m_lanResTool.getStrByID("SCMCOMMON", "UPPSCMCommon-000270")/* @res "提示" */;
  String sError = m_lanResTool.getStrByID("40040301", "UPT40040301-000139")/* @res"没有可分配序列号的行!" */;  
  if(filterVos(cardVos)==null){
    MessageDialog.showErrorDlg(this, sTitle, sError);
    return false;
  }
  if(m_alSerialData !=null){
    setM_alSerialDataCancel((HashMap) m_alSerialData.clone());
  }
  HashMap RowIdAndWillstorenum = null;
  try {
      RowIdAndWillstorenum = loadBodyDataForCard(cardVos);
      if (RowIdAndWillstorenum == null || RowIdAndWillstorenum.size() < 1) {
        MessageDialog.showErrorDlg(this, sTitle, sError);
        return false;
      }
      else {
        ArriveorderItemVO[] itemVO = cardVos.getBodyVo();
        Vector vctItemVO = new Vector();
        int rowCount = RowIdAndWillstorenum.size();
        for (int j = 0; j < itemVO.length; j++) {
          if (RowIdAndWillstorenum.containsKey(itemVO[j].getPrimaryKey())) {
            vctItemVO.add(itemVO[j]);
          }
        }
        if (vctItemVO == null || vctItemVO.size() < 1) {
          MessageDialog.showErrorDlg(this, sTitle, sError);
          return false;
        }
        
        ArriveorderItemVO[] temp = new ArriveorderItemVO[vctItemVO.size()];
        vctItemVO.copyInto(temp);
        cardVos.setChildrenVO(temp);
        
      }
    }
  catch (BusinessException e) {
      MessageDialog.showErrorDlg(this, sTitle, e.getMessage());
    }
  SerialAllocationDlg temp = getSerialAllocationDlg(cardVos,RowIdAndWillstorenum);
  if (temp != null) {
      int iRetType = temp.showModal();
      if (iRetType == UIDialog.ID_OK) {
        showHintMessage(m_lanResTool.getStrByID("40040301", "UPT40040301-000138")/* @res"序列号分配结束!" */);
        return true;
      }
    }
  return false;
}
/**
 * 是否来源委外
 * @return
 */
private boolean isFromSC() {
	return getBillCardPanel().getBillModel().getValueAt(0, "cupsourcebilltype")!=null &&
			  getBillCardPanel().getBillModel().getValueAt(0, "cupsourcebilltype").toString().equals(BillTypeConst.SC_ORDER);
}


/**
 * 得到到货单行的可生成卡片数，算法同入库数。
 * <p>
 * <b>examples:</b>
 * <p>
 * 使用示例
 * <p>
 * <b>参数说明</b>
 * @param bID
 * @param bTS
 * @return
 * <p>
 * @author lixiaodong
 * @throws BusinessException 
 * @time 2008-6-21 上午11:19:42
 */
public HashMap loadBodyDataForCard(ArriveorderVO arriveorderVO) throws BusinessException {
  HashMap RowIdAndWillstorenum  = new HashMap();
  try {
    if (arriveorderVO == null) {
      return null;
    }

    // 查询过的数据不再查询
    Object[] oaRet = null;
    if (arriveorderVO.getChildrenVO() != null) {

      /*参数ArrayList结构
       *   |-String,公司主键
       *   |-String,查询条件串
       *   |-UFBoolean,质量管理是否启用
       *   |-String,操作员ID
       */
      java.util.ArrayList listPara = new java.util.ArrayList();
      listPara.add(getCorpPrimaryKey());
      listPara.add("carriveorderid = '"+arriveorderVO.getHeadVO().getPrimaryKey()+"'");
      listPara.add(new UFBoolean(m_bQcEnabled));
      listPara.add(ClientEnvironment.getInstance().getUser().getPrimaryKey());

      oaRet = ArriveorderHelper.queryVOsForIc(listPara);
      if (oaRet != null && oaRet[0] != null && ((Object[]) oaRet[0]).length > 0 &&oaRet[1] != null && ((Object[]) oaRet[0]).length > 0 ) {
        nc.vo.rc.receive.ArriveorderItemVO[] tmpBodyVo = (ArriveorderItemVO[]) oaRet[1];
        if (tmpBodyVo != null) {
          for (int i = 0; i < tmpBodyVo.length; i++) {
            if(PuPubVO.getUFDouble_ZeroAsNull(tmpBodyVo[i].getNwillstorenum()) != null){
            	if(RowIdAndWillstorenum.containsKey(tmpBodyVo[i].getPrimaryKey())){
            		UFDouble oldValue = (UFDouble)RowIdAndWillstorenum.get(tmpBodyVo[i].getPrimaryKey());
            		RowIdAndWillstorenum.put(tmpBodyVo[i].getPrimaryKey(), 
            				oldValue.add(tmpBodyVo[i].getNwillstorenum()));
            	}else
            		RowIdAndWillstorenum.put(tmpBodyVo[i].getPrimaryKey(), tmpBodyVo[i].getNwillstorenum());
            }
          }
        }else{
          return null;
        }
      }else{
        return null;
      }

    }
  } catch (Exception e) {
    SCMEnv.out(e);
    throw new BusinessException(e.getMessage());
  }
  return RowIdAndWillstorenum;
}

private SerialAllocationDlg getSerialAllocationDlg(nc.vo.rc.receive.ArriveorderVO vo,HashMap RowIdAndWillstorenum) {
  try {
    m_dlgSerialAllocation = new SerialAllocationDlg(this,vo,getOperatorId(), getCorpPrimaryKey(),new MyBillData(),m_bQcEnabled,RowIdAndWillstorenum);
    m_dlgSerialAllocation.setName("SerialAllocationDlg");
  } catch (java.lang.Throwable ivjExc) {
    nc.vo.scm.pub.SCMEnv.error(ivjExc);
    showWarningMessage(ivjExc.getMessage());
    return null;
  }
  return m_dlgSerialAllocation;
}

/**
 * 过滤掉非序列号管理存货、非资产仓且没生成卡片的到货单行。
 * <p>
 * <b>examples:</b>
 * <p>
 * 使用示例
 * <p>
 * <b>参数说明</b>
 * @param vo 到货单聚合VO
 * @return 过滤后到货单聚合VO
 * <p>
 * @author lixiaodong
 * @time 2008-6-28 下午01:21:04
 */
private ArriveorderVO filterVos(ArriveorderVO vo) {
  if (vo ==null||!vo.getHeadVO().isAuditted()||vo.getBodyLen() < 1) {
    return null;
  }
  ArriveorderItemVO[] itemVO = vo.getBodyVo();
  Vector vctItemVO  = new Vector();

  String bfaflag = "";//是否已生成卡片
  String isserialmanaflag  = "";//序列号管理存货
  String iscapitalstor  = "";//是否资产仓
  String rowID  = "";//行ID
  String naccumwarehousenum  = "";//累计入库数量
  String rowNO  = "";//行号
  String cinventoryname  = "";
  String err  = "";

  int rowCount = getBillCardPanel().getBillModel().getRowCount();
  if(rowCount < 1){
    return null;
  }
  
  for (int i = 0; i < itemVO.length; i++) {
    for (int j = 0; j < rowCount; j++) {
      bfaflag=getBillCardPanel().getBillModel().getValueAt(j, "bfaflag")==null?"":getBillCardPanel().getBillModel().getValueAt(j, "bfaflag").toString();
      isserialmanaflag = getBillCardPanel().getBillModel().getValueAt(j, "isserialmanaflag")==null?"":getBillCardPanel().getBillModel().getValueAt(j, "isserialmanaflag").toString();
      iscapitalstor = PuPubVO.getString_TrimZeroLenAsNull(getBillCardPanel().getBillModel().getValueAt(j, "iscapitalstor"))==null?"":getBillCardPanel().getBillModel().getValueAt(j, "iscapitalstor").toString();
      rowID = getBillCardPanel().getBillModel().getValueAt(j, "carriveorder_bid")==null?"":getBillCardPanel().getBillModel().getValueAt(j, "carriveorder_bid").toString();
      naccumwarehousenum = getBillCardPanel().getBillModel().getValueAt(j, "naccumwarehousenum")==null?"":getBillCardPanel().getBillModel().getValueAt(j, "naccumwarehousenum").toString();
      rowNO = PuPubVO.getString_TrimZeroLenAsNull(getBillCardPanel().getBillModel().getValueAt(j,"crowno"));
      cinventoryname = PuPubVO.getString_TrimZeroLenAsNull(getBillCardPanel().getBillModel().getValueAt(j,"cinventoryname"));
      err  = "第" + rowNO + "行," + cinventoryname + ":";
      
      if(rowID.equalsIgnoreCase(itemVO[i].getPrimaryKey())){
        if ((PuPubVO.getString_TrimZeroLenAsNull(iscapitalstor) == null || "true".equalsIgnoreCase(iscapitalstor))) {
          if ("true".equalsIgnoreCase(isserialmanaflag)) {
            if(!"true".equalsIgnoreCase(bfaflag)){
              if (InvTool.isAsset((String) getBillCardPanel().getBillModel().getValueAt(j, "cbaseid"))) {// 资产类存货
                if (!(new UFDouble(naccumwarehousenum.toString().trim()).compareTo(new UFDouble(0)) > 0)) {// 累计入库数量大于零
                  vctItemVO.add(itemVO[i]);
                }else{
                  SCMEnv.out(err + " 累计入库数量大于零" + "\n");  
                }
              }else{
                SCMEnv.out(err + " 非资产类存货" + "\n");
              }
            }else{
              SCMEnv.out(err + " 已生成卡片" + "\n");
            }
          }else{
            SCMEnv.out(err + " 非序列号管理存货" + "\n");
          }
        }else{
          SCMEnv.out(err + " 非资产仓" + "\n");
        }
      }
    }
  }
  if(vctItemVO==null||vctItemVO.size()<1){
    return null;
  }

  ArriveorderItemVO[] temp = new ArriveorderItemVO[vctItemVO.size()];
  vctItemVO.copyInto(temp);
  vo.setChildrenVO(temp);
  return vo;
}

/**
 * 组织到货单数据，调用后台：到货单生成资产卡片。
 * <p>
 * <b>examples:</b>
 * <p>
 * 使用示例
 * <p>
 * <b>参数说明</b>
 * <p>
 * @author lixiaodong
 * @time 2008-5-21 下午02:23:31
 */
private void onCreateCard() {
  if (!m_bAIMEnabled) {
    MessageDialog.showErrorDlg(this, m_lanResTool.getStrByID("SCMCOMMON","UPPSCMCommon-000059")/*@res "错误"*/, m_lanResTool.getStrByID("40040301","UPT40040301-000141")/*@res "资产模块未启用，不能生成卡片!"*/);
    return;
  }

  if (!onSNAssign()) {
    showHintMessage("");
    return;
  }

  try {

    String sRowId = "";// 当前选中行ID
    String sOrderRowId = "";// 当前选中行对应的订单行ID
    Vector vctRowId = new Vector();
    Vector vctOrderRowId = new Vector();
    Vector vctPlanarrvdates = new Vector();
    nc.vo.po.pub.SerialUiVO serialUiVO=new nc.vo.po.pub.SerialUiVO();
    ArriveorderItemVO item = null;
    BillModel bm = getBillCardPanel().getBillModel();
    int iRowCnt =getBillCardPanel().getBodyPanel().getTable().getRowCount();
    if(iRowCnt<1){
      showHintMessage("");
      return;
    }


    //得到填写了序列号但未生成卡片的行
    SerialUiVO[] serial = null;

    StringBuffer rowno=new StringBuffer();
    int noserial=0;
    for (int i = 0; i < iRowCnt; i++) {
      sRowId = (String) getBillCardPanel().getBillModel().getValueAt(i, "carriveorder_bid");
      sOrderRowId = (String) getBillCardPanel().getBillModel().getValueAt(i, "corder_bid");
      serial = (SerialUiVO[]) getM_alSerialData(sRowId, null).get(sRowId);
      if (getM_alSerialData(sRowId, null) == null || getM_alSerialData(sRowId, null).size() < 1 || serial == null) {
        rowno.append((String) getBillCardPanel().getBillModel().getValueAt(i, "crowno") + ",");
        noserial++;
        continue;
      }
      if ("true".equalsIgnoreCase(getBillCardPanel().getBillModel().getValueAt(i, "bfaflag")==null?"":getBillCardPanel().getBillModel().getValueAt(i, "bfaflag").toString())){
        continue;
      }

      if (serial != null && serial.length>0) {
        String[] serialNo=serial[0].getSerialNo(serial);
        if(serialNo==null||serialNo.length<1){//从缓存里取序列号
          rowno.append((String) getBillCardPanel().getBillModel().getValueAt(i, "crowno") + ",");
          noserial++;
          continue;
        }
      }

      String bfaflag = "";
      for (int ii = 0; ii < getCacheVOs()[getDispIndex()].getBodyLen();ii++){
        bfaflag=getBillCardPanel().getBillModel().getValueAt(ii, "bfaflag")==null?"":getBillCardPanel().getBillModel().getValueAt(ii, "bfaflag").toString();
        if((!"true".equalsIgnoreCase(bfaflag))&&((String) getBillCardPanel().getBillModel().getValueAt(i, "carriveorder_bid")).equalsIgnoreCase(getCacheVOs()[getDispIndex()].getBodyVo()[ii].getPrimaryKey())){
          String[] temp=serialUiVO.getSerialNo(getCacheVOs()[getDispIndex()].getBodyVo()[ii].getSerialUiVO());

          if(temp==null||temp.length<1){//从缓存里取序列号
            temp=serialUiVO.getSerialNo((SerialUiVO[]) getM_alSerialData(sRowId, null).get(getCacheVOs()[getDispIndex()].getBodyVo()[ii].getPrimaryKey()));
          }

          if(temp!=null&&temp.length>0){
            if (!vctRowId.contains(sRowId)) {
              vctRowId.add(sRowId);
              vctPlanarrvdates.add(getBillCardPanel().getHeadItem("dreceivedate").getValue());
              vctOrderRowId.add(sOrderRowId);
            }
          }
        }
      }
    }
    if (PuPubVO.getString_TrimZeroLenAsNull(rowno) != null && noserial==iRowCnt) {
      showHintMessage(m_lanResTool.getStrByID("40040301","UPT40040301-000136")/*@res"未填写序列号的行号:"*/ + rowno.toString().substring(0, rowno.toString().length()-1));
      return;
    }

    if (vctRowId == null || vctRowId.size() < 1 || vctPlanarrvdates == null || vctPlanarrvdates.size() < 1 || vctRowId.size() != vctPlanarrvdates.size()) {
      showHintMessage(m_lanResTool.getStrByID("40040301","UPT40040301-000135")/*@res"没有可生成卡片的到货单行!"*/);
      return;
    }

    int iSize = getBillCardPanel().getRowCount();
    if(iSize <= 0){
      showHintMessage("");
      return;
    }
    ArriveorderItemVO[] arriveorderItemVO = null;
    Vector vctItemVO=new Vector();
    for(int i=0 ;i < iSize ; i++){
      item = (ArriveorderItemVO) bm.getBodyValueRowVO(i,ArriveorderItemVO.class.getName());
      sRowId = (String) getBillCardPanel().getBillModel().getValueAt(i, "carriveorder_bid");// 行ID
      if ((SerialUiVO[]) getM_alSerialData(sRowId, null).get(sRowId) != null
          && ((SerialUiVO[]) getM_alSerialData(sRowId, null).get(sRowId)).length > 0
          && ((SerialUiVO[]) getM_alSerialData(sRowId, null).get(sRowId))[0].getVstartsn() != null
          && vctRowId.contains(sRowId)) {
        item.setSerialUiVO((SerialUiVO[]) getM_alSerialData(sRowId, null).get(sRowId));
        vctItemVO.add(item);
      }
    }

    if(vctItemVO==null||vctItemVO.size()<1){
      showHintMessage(m_lanResTool.getStrByID("40040301","UPT40040301-000135")/*@res"没有可生成卡片的到货单行!"*/);
      return;
    }

    ArriveorderVO vo = new ArriveorderVO();
    arriveorderItemVO = new ArriveorderItemVO[vctItemVO.size()];
    vctItemVO.copyInto(arriveorderItemVO);
    vo.setParentVO(getCacheVOs()[getDispIndex()].getHeadVO());
    vo.setChildrenVO(arriveorderItemVO);


    ArrayList arr=new ArrayList();
    arr.add(vo);
    arr.add(getOperatorId());//当前登录的用户的ID
    arr.add(getSysDate());//当前登录日期
    arr.add(PoPublicUIClass.getLoginPk_corp());//当前登录公司

    // 调用后台：到货单生成资产卡片
    IArriveorder bo2 = (IArriveorder) nc.bs.framework.common.NCLocator.getInstance().lookup(IArriveorder.class.getName());
    bo2.onCreateCard(arr);


    // 重新刷新界面，保持"bfaflag"、"TS"等保持最新
    String billID = vo.getHeadVO().getPrimaryKey();
    getCacheVOs()[getDispIndex()] = ArriveorderHelper.findByPrimaryKey(billID);
    /*加载单据*/
    try {
      loadDataToCard();
      refreshCardData();
    } catch (Exception e) {
      showHintMessage("成功,但加载单据时出现异常,请刷新界面再进行其它操作");
    }
    /*刷新按钮状态*/
    setButtonsState();


    showHintMessage(m_lanResTool.getStrByID("40040301","UPT40040301-000134")/*@res"生成卡片成功!"*/); 
  } catch (BusinessException e) {
    SCMEnv.out(e.getMessage());
    showHintMessage(e.getMessage());
    MessageDialog.showErrorDlg(this, m_lanResTool.getStrByID("SCMCOMMON","UPPSCMCommon-000059")/*@res "错误"*/, e.getMessage());
  } catch (Exception e) {
    SCMEnv.out(e);
    showHintMessage(e.getMessage());
    MessageDialog.showErrorDlg(this, m_lanResTool.getStrByID("SCMCOMMON","UPPSCMCommon-000059")/*@res "错误"*/, e.getMessage());
  }
}

/**
 * 组织到货单数据，删除到货单生成的资产卡片。
 * <p>
 * <b>examples:</b>
 * <p>
 * 使用示例
 * <p>
 * <b>参数说明</b>
 * <p>
 * @author lixiaodong
 * @time 2008-5-21 下午02:29:42
 */
private void onDeleteCard() {
  try {

    int iSize = getBillCardPanel().getRowCount();
    if(iSize <= 0){
      return;
    }
    if (getCacheVOs() == null || getCacheVOs().length <= 0) {
      showHintMessage(m_lanResTool.getStrByID("40040301","UPT40040301-000133")/*@res"没有已生成资产卡片的行!"*/);
      return;
    }

    ArriveorderVO m_Vos = getCacheVOs()[getDispIndex()];
    if (m_Vos == null) {
      showHintMessage(m_lanResTool.getStrByID("40040301","UPT40040301-000133")/*@res"没有已生成资产卡片的行!"*/);
      return;
    }
    if(m_Vos.getBodyLen()<1){
      showHintMessage(m_lanResTool.getStrByID("40040301","UPT40040301-000133")/*@res"没有已生成资产卡片的行!"*/);
      return;
    }
    ArriveorderHeaderVO head = null;
    ArriveorderItemVO[] items = null;

    items = m_Vos.getBodyVo();
    head=m_Vos.getHeadVO();
    Vector v =  new Vector();// 保存已生成卡片的到货单行
    Vector rowID =  new Vector();// 保存已生成卡片的到货单行ID
    for (int i = 0; i < iSize; i++) {
      if (items[i].getBfaflag() == null || !items[i].getBfaflag().booleanValue()) {// 当前选中行是否生成资产片标志
      } else {
        v.add(items[i]);
        rowID.add(items[i].getPrimaryKey());
      }
    }
    if(v==null||v.size()<1){
      showHintMessage(m_lanResTool.getStrByID("40040301","UPT40040301-000133")/*@res"没有已生成资产卡片的行!"*/);
      return;
    }

    //组织新的到货单聚合VO传到后台
    ArriveorderVO vo = new ArriveorderVO();
    ArriveorderItemVO[] arriveorderItemVO = new ArriveorderItemVO[v.size()];
    v.copyInto(arriveorderItemVO);
    vo.setParentVO(getCacheVOs()[getDispIndex()].getHeadVO());
    vo.setChildrenVO(arriveorderItemVO);


    // 调用固定资产的接口：删除到货单生成的资产卡片、删除下游库存单据。
    ArrayList arr=new ArrayList();
    arr.add(vo);
    arr.add(getOperatorId());//当前登录的用户的ID
    arr.add(getSysDate());//当前登录日期

    IArriveorder bo2 = (IArriveorder) nc.bs.framework.common.NCLocator.getInstance().lookup(IArriveorder.class.getName());
    bo2.onDeleteCard(arr);


    // 重新刷新界面，保持"bfaflag"、"TS"等保持最新
    String billID = vo.getHeadVO().getPrimaryKey();
    getCacheVOs()[getDispIndex()] = ArriveorderHelper.findByPrimaryKey(billID);

    /*加载单据*/
    loadDataToCard();
    /*刷新按钮状态*/
    setButtonsState();

    showHintMessage(m_lanResTool.getStrByID("common","UCH006")/*@res "删除成功"*/);
  } catch (Exception e) {
    showHintMessage(e.getMessage());
  }
}

public void setM_alSerialData(String sRowId, SerialUiVO[] temp) {
  if(sRowId == null){
    m_alSerialData = null;
  }
  if (m_alSerialData != null) {
    if (m_alSerialData.containsKey(sRowId)) {
      m_alSerialData.remove(sRowId);
    }
    m_alSerialData.put(sRowId, temp);
  }
}

public HashMap getM_alSerialData(String sRowId, UFDouble willstorenum) {
  if (m_alSerialData == null || m_alSerialData.size() < 1) {
    SerialUiVO[] temp = new SerialUiVO[m_billPanel.getBillModel().getRowCount()];
    temp[0] = new SerialUiVO();
    temp[0].setNqty(willstorenum == null ? 0 : PuTool.getQty(willstorenum.doubleValue()));

    m_alSerialData = new HashMap();
    if (!m_alSerialData.containsKey(sRowId)) {
      m_alSerialData.put(sRowId, temp);
    }
  }
  return m_alSerialData;
}

/**
 * 方法功能描述：初始化打印参数。
 * <p>
 * <b>examples:</b>
 * <p>
 * 使用示例
 * <p>
 * <b>参数说明</b>
 * @return
 * <p>
 * @author lixiaodong
 * @time 2008-8-4 上午11:38:49
 */
private SplitParams[] initSplitParams() {
  //批量打印工具
  SplitParams[] paramvos = null;

  if (paramvos == null || paramvos.length < 1) {
    paramvos = new SplitParams[2];
    paramvos[0] = new SplitParams("cwarehouseid", NCLangRes.getInstance().getStrByID("common", "UC000-0002236"), 0, null, true);// UC000-0002236=收货仓库
    paramvos[1] = new SplitParams("cinvclid", NCLangRes.getInstance().getStrByID("common", "UC000-0001443"), 0, null, true);// UC000-0001443=存货分类
  }

//  // 从界面获取存货分类主键
//  int iLen = vo.getBodyLen();
//  String[] saBid = new String[iLen];
//  String cinvclid = null;
//  String cbid = null;
//  for (int i = 0; i < getBillCardPanel().getBillModel().getRowCount(); i++) {
//    cinvclid = getBillCardPanel().getBillModel().getValueAt(i, "cinvclid") == null ? null : getBillCardPanel().getBillModel().getValueAt(i, "cinvclid").toString();
//    cbid = getBillCardPanel().getBillModel().getValueAt(i, "carriveorder_bid") == null ? null : getBillCardPanel().getBillModel().getValueAt(i, "carriveorder_bid").toString();
//    for (int ii = 0; ii < iLen; ii++) {
//      saBid[ii] = vo.getBodyVo()[ii].getPrimaryKey();
//      if (PuPubVO.getString_TrimZeroLenAsNull(saBid[ii]) != null && PuPubVO.getString_TrimZeroLenAsNull(saBid[ii]).equalsIgnoreCase(cbid)) {
//        vo.getBodyVo()[ii].setAttributeValue("cinvclid", cinvclid);
//      }
//    }
//  }
//  if (vo != null) {
//    setCacheVOs(new ArriveorderVO[] {vo});
//  }
  return paramvos;
}
public boolean onEditAction(int action) {
	if(action == BillScrollPane.ADDLINE){
		return false;
	}
	return true;
}
/*
 * 设置单据卡片右键菜单行操作与按钮组“行操作”权限相同
 */
private void setPopMenuBtnsEnable(boolean b) {
	m_btnDelLine.setEnabled(false);
	m_btnCpyLine.setEnabled(false);
	m_btnPstLine.setEnabled(false);
	m_btnPstLineTail.setEnabled(false);
	m_miReSortRowNo.setEnabled(false);
	m_btnReSortRowNo.setEnabled(false);
	m_btnCardEdit.setEnabled(false);
	m_miCardEdit.setEnabled(false);

	updateButtonsAll();


	// 没有分配行操作权限
	if (m_btnLines == null || m_btnLines.getChildCount() == 0) {
		getBillCardPanel().getBodyPanel().getMiCopyLine().setEnabled(false);
		getBillCardPanel().getBodyPanel().getMiDelLine().setEnabled(false);
		getBillCardPanel().getBodyPanel().getMiPasteLine().setEnabled(false);
		getBillCardPanel().getBodyPanel().getMiPasteLineToTail().setEnabled(false);
		m_miReSortRowNo.setEnabled(false);
		m_miCardEdit.setEnabled(false);
	}
	// 分配行操作权限
	else {
		getBillCardPanel().getBodyPanel().getMiCopyLine().setEnabled(b);
		getBillCardPanel().getBodyPanel().getMiDelLine().setEnabled(b);
		getBillCardPanel().getBodyPanel().getMiPasteLine().setEnabled(b);
		// 粘贴到行尾与粘贴可用性逻辑相同
		getBillCardPanel().getBodyPanel().getMiPasteLineToTail().setEnabled(b);
		//
		m_miReSortRowNo.setEnabled(b);
		m_btnCardEdit.setEnabled(b);

	}

}

private void setPopMenuBtnsEnable() {
  
  // 没有分配行操作权限
  if (m_btnLines == null || m_btnLines.getChildCount() == 0) {
    getBillCardPanel().getBodyPanel().getMiCopyLine().setEnabled(false);
    getBillCardPanel().getBodyPanel().getMiDelLine().setEnabled(false);
    getBillCardPanel().getBodyPanel().getMiPasteLine().setEnabled(false);
    getBillCardPanel().getBodyPanel().getMiPasteLineToTail().setEnabled(false);
    m_miReSortRowNo.setEnabled(false);
    m_miCardEdit.setEnabled(false);
  }
  // 分配行操作权限
  else {
    getBillCardPanel().getBodyPanel().getMiCopyLine().setEnabled(isPowerBtn(m_btnCpyLine.getCode()));
    getBillCardPanel().getBodyPanel().getMiDelLine().setEnabled(isPowerBtn(m_btnDelLine.getCode()));
    getBillCardPanel().getBodyPanel().getMiPasteLine().setEnabled(isPowerBtn(m_btnPstLine.getCode()));
    // 粘贴到行尾与粘贴可用性逻辑相同
    getBillCardPanel().getBodyPanel().getMiPasteLineToTail().setEnabled(isPowerBtn(m_btnPstLineTail.getCode()));
    //
    m_miReSortRowNo.setEnabled(isPowerBtn(m_btnReSortRowNo.getCode()));
    m_btnCardEdit.setEnabled(isPowerBtn(m_btnCardEdit.getCode()));

  }
}

/**
 * 弹出查询对话框向用户询问查询条件。
 * 如果用户在对话框点击了“确定”，那么返回true,否则返回false
 * 查询条件通过传入的StringBuffer返回给调用者
 *
 * @param sqlWhereBuf
 *            保存查询条件的StringBuffer
 * @return 用户选确定返回true否则返回false
 */
protected boolean askForQueryCondition(StringBuffer sqlWhereBuf)
    throws Exception {

  // para sqlWhereBuf can not be null
  if (sqlWhereBuf == null)
    throw new IllegalArgumentException("askForQueryCondition().sqlWhereBuf can't be null!");

  String strWhere = getQueryConditionDlg().getWhereSQL();//getWhereSql();
  if (PuPubVO.getString_TrimZeroLenAsNull(strWhere) != null){
    strWhere = "(" + strWhere + ") and po_arriveorder.dr=0 ";
  }else{
    strWhere = " and po_arriveorder.dr=0 ";
  }

  sqlWhereBuf.append(strWhere);
  return true;
}
public HashMap getM_alSerialDataCancel() {
  return m_alSerialDataCancel;
}
public void setM_alSerialDataCancel(HashMap serialDataCancel) {
  m_alSerialDataCancel = serialDataCancel;
}

public void checkVprocessbatch(nc.vo.rc.receive.ArriveorderVO[] VOs) {
  try {
    if (VOs == null || VOs.length < 1) {
      return;
    }

    for (int i = 0; i < VOs.length; i++) {
      if(VOs[i].getChildrenVO() == null){
        continue;
      }
      int rownum = VOs[i].getChildrenVO().length;
      boolean flag = true;
      ArriveorderItemVO itemVO = null;
      String cmangid = "";
      String sBaseID = "";
      String sCassId = "";
      Object[] o = null;
      HashMap<String, String> mapMangID = new HashMap<String, String>();
      
      for (int j = 0; j < rownum; j++) {
        itemVO = (ArriveorderItemVO) VOs[i].getChildrenVO()[j];
        cmangid = PuPubVO.getString_TrimZeroLenAsNull(itemVO.getCmangid());
        sBaseID = PuPubVO.getString_TrimZeroLenAsNull(itemVO.getCbaseid());
        sCassId = PuPubVO.getString_TrimZeroLenAsNull(itemVO.getCassistunit());
        if (cmangid != null) {
          if(mapMangID.containsKey(cmangid)){
            if("Y".equalsIgnoreCase(PuPubVO.getString_TrimZeroLenAsNull(mapMangID.get(cmangid)))){
              itemVO.setIswholemanaflag(UFBoolean.TRUE);
            }
          }else{
            o = (Object[]) CacheTool.getCellValue("bd_invmandoc","pk_invmandoc", "wholemanaflag", cmangid);
            if (o != null && o.length > 0 && o[0] != null && o[0].toString().equals("Y")) {
              mapMangID.put(cmangid, o[0].toString());
              itemVO.setIswholemanaflag(UFBoolean.TRUE);
            }else{
              mapMangID.put(cmangid, null);
            }
          }
          // 是否固定换算率
          if(PuTool.isFixedConvertRate(sBaseID, sCassId)){
            itemVO.setIsfixedrate(UFBoolean.TRUE);
          }

        }
      }
    }
  }
  catch (BusinessException be) {
    SCMEnv.out(be.getMessage());
  }

}
private void setReWriteData(ArriveorderVO savevo,ArriveorderVO newvo,ArriveorderVO oldvo) {
  try {
    savevo.setH_arrNumNew(ArrivePubVO.getAccArrNum(newvo));
    savevo.setH_arrNumOld(ArrivePubVO.getAccArrNum(oldvo));
    savevo.setH_wasNumNew(ArrivePubVO.getAccWasNum(newvo));
    savevo.setH_wasNumOld(ArrivePubVO.getAccWasNum(oldvo));
    savevo.setH_givNumNew(ArrivePubVO.getAccGivNum(newvo));
    savevo.setH_givNumOld(ArrivePubVO.getAccGivNum(oldvo));
    savevo.setH_accArrNumBB1New(ArrivePubVO.getAccArrNumBB1(newvo));
    savevo.setH_accArrNumBB1Old(ArrivePubVO.getAccArrNumBB1(oldvo));
  }
  catch (BusinessException e) {
    nc.vo.scm.pub.SCMEnv.out(e);
  }
}
/**
 * <p>
 * 排序方法，返回要排序的当前VO表体VO数组
 * 
 * @since V50
 */
public Object[] getRelaSortObjectArrayBody() {
	if (getCacheVOs() != null && getCacheVOs().length > 0 && getDispIndex() >= 0 && getCacheVOs()[getDispIndex()] != null
			&& getCacheVOs()[getDispIndex()].getChildrenVO() != null) {
		return getCacheVOs()[getDispIndex()].getChildrenVO();
	}
	return null;
}
/**
 *弃审返回到制单人时，需要在此清空审批人、审批日期、审批时间
 */
private void setAuditInfo() {
	Integer iVoStatus = BillStatus.FREE;
	if(getBillCardPanel().getHeadItem("ibillstatus") != null){
		iVoStatus = PuPubVO.getInteger_NullAs(getBillCardPanel().getHeadItem("ibillstatus").getValueObject(), BillStatus.FREE);
	}else if (getBillCardPanel().getTailItem("ibillstatus") != null){
		iVoStatus = PuPubVO.getInteger_NullAs(getBillCardPanel().getTailItem("ibillstatus").getValueObject(), BillStatus.FREE);
	}
	if(BillStatus.AUDITFAIL.intValue() == iVoStatus.intValue()){
		if (getBillCardPanel().getTailItem("cauditpsn") != null){
			getBillCardPanel().getTailItem("cauditpsn").setValue(null);
		}else if(getBillCardPanel().getHeadItem("cauditpsn") != null){
			getBillCardPanel().getHeadItem("cauditpsn").setValue(null);
		}
		if (getBillCardPanel().getTailItem("dauditdate") != null){
			getBillCardPanel().getTailItem("dauditdate").setValue(null);
		}else if(getBillCardPanel().getHeadItem("dauditdate") != null){
			getBillCardPanel().getHeadItem("dauditdate").setValue(null);
		}
		if (getBillCardPanel().getTailItem("taudittime") != null){
			getBillCardPanel().getTailItem("taudittime").setValue(null);
		}else if(getBillCardPanel().getHeadItem("taudittime") != null){
			getBillCardPanel().getHeadItem("taudittime").setValue(null);
		}
	}
}
private BillTempletVO m_billTempletVO = null;
/**
 * 获取该节点的模板数据
 */
private BillTempletVO getBillTempletVO(){
	if(m_billTempletVO==null){
		m_billTempletVO = BillUIUtil.getDefaultTempletStatic(BillTypeConst.PO_ARRIVE, null, 
				getOperatorId(), getCorpPrimaryKey(),null ,null);	
		// m_billPanel.loadTemplet(getModuleCode(), null, getOperatorId(), getCorpPrimaryKey(),new MyBillData());
	}
	return m_billTempletVO;
}
/**
 * @function 得到费用录入按钮
 *
 * @author QuSida
 *
 * @return ButtonObject
 *
 * @date 2010-8-28 上午10:03:53
 */
private ButtonObject getBoInfoCost(){
	if(boInfoCost == null){
		//费用录入按钮 add by QuSida 2010-8-10  （佛山骏杰）
		boInfoCost = new ButtonObject("费用录入","费用录入","费用录入");
		return boInfoCost;
	}
	else return boInfoCost;
}
/**
 * @function 费用信息录入功能
 *
 * @author QuSida
 * 
 * @return void
 *
 * @date 2010-8-28 上午10:03:17
 */
private void onBoInfoCost() {
	InformationCostVO[] ivos = (InformationCostVO[] )getBillCardPanel().getBillModel("jj_scm_informationcost").getBodyValueVOs(InformationCostVO.class.getName());
	ArrayList voList = new ArrayList();
	for (int i = 0; i < ivos.length; i++) {
		//modify by 付世超 2010-10-12 原判断条件错误  
		if(ivos[i] != null){
//		if(ivos[i].getPrimaryKey() == null||ivos[i].getPrimaryKey().length() == 0){
			voList.add(ivos[i]);
		}
	}
	InfoCostPanel c = null;
	if(voList.size()!=0&&voList!=null){
		InformationCostVO[]  ivos1 = new InformationCostVO[voList.size()]; 
	voList.toArray(ivos1);
	c = new InfoCostPanel(getBillCardPanel(),ivos1);
//	if(ivos == null || ivos.length == 0)
			
		}
	else 
		
	c = new InfoCostPanel(getBillCardPanel());
	// 打开费用录入界面
	c.showModal();
//	if(c.showModal()==UIDialog.ID_OK){//showModal为显示Dialog,如果该方法返回值为1,那么表示用户点了确定.
//		20101010 15:26 梅超.添加
//	}
	// 当费用录入界面关闭时,将录入的数据存放到费用信息页签上
	if (c.isCloseOK()) {
		InformationCostVO[] infoCostVOs = c.getInfoCostVOs();
		if (infoCostVOs != null && infoCostVOs.length != 0){
			// 当费用录入界面的vo数组不为空时,将vo存到费用录入页签上
			if(infoCostVOs.length!=0&&infoCostVOs!=null){
				
				   int temp = getBillCardPanel().getBillModel("table").getRowCount();
				   UFDouble arrmny = null;
				   UFDouble mny = null;
				    arrnumber = new UFDouble(0.0);
			    for (int i = 0; i < temp; i++) {
			    	arrnumber = arrnumber.add(new UFDouble(getBillCardPanel().getBillModel("table").getValueAt(i,"narrvnum").toString()));    			    	  
				}

				for (int i = 0; i < infoCostVOs.length; i++) {
					infoCostVOs[i].setNnumber(arrnumber);
					UFBoolean ismny = (UFBoolean)infoCostVOs[i].getAttributeValue("ismny");
					if(ismny == null || !ismny.booleanValue()){
				//add by  付世超 2010-10-12 begin
						if(arrnum == null){//设置累计存货数量
							arrnum = new UFDouble(0.0);
						}
				//add by  付世超 2010-10-12 end
			    	mny = new UFDouble(infoCostVOs[i].getAttributeValue("noriginalcurprice").toString()).multiply(arrnumber);
			    	arrmny  = new UFDouble(infoCostVOs[i].getAttributeValue("noriginalcurprice").toString()).multiply(arrnumber.add(arrnum));
//			    	taxmny = new UFDouble(infoCostVOs[i].getAttributeValue("noriginalcurtaxprice").toString()).multiply(arrnumber);
//			    	infoCostVOs[i].setAttributeValue("noriginalcursummny", taxmny);
			    	infoCostVOs[i].setAttributeValue("noriginalcurmny", mny);
//			    	infoCostVOs[i].setAttributeValue("ninvoriginalcursummny", taxmny);
			    	infoCostVOs[i].setAttributeValue("ninvoriginalcurmny", arrmny);	
					}else{
						infoCostVOs[i].setAttributeValue("noriginalcurprice", infoCostVOs[i].getNoriginalcurmny().div(arrnumber));	
						infoCostVOs[i].setAttributeValue("ninvoriginalcurmny", infoCostVOs[i].getNoriginalcurmny());	
					}
				}
				// by 付世超 2010-10-12 注释掉代码	
//				InformationCostVO[] newVOs = new InformationCostVO[vos.length+infoCostVOs.length];
//				if(vos.length != 0 && vos != null){
//					//int temp = vos.length>infoCostVOs.length?vos.length:infoCostVOs.length;
//					
//					System.arraycopy(vos, 0, newVOs, 0, vos.length);
//					
//					System.arraycopy(infoCostVOs, 0, newVOs, vos.length, infoCostVOs.length);
//					
//					
//						getBillCardPanel().getBillData().setBodyValueVO(
//								"jj_scm_informationcost", newVOs);
//					//	getBillCardPanel().getBillModel("jj_scm_informationcost").execLoadFormula();
//				}
//				else 
				getBillCardPanel().getBillData().setBodyValueVO(
						"jj_scm_informationcost", infoCostVOs);
				getBillCardPanel().getBillModel("jj_scm_informationcost").execLoadFormula();
			}		
		}
	}
}
private void costInfoDistribute(InformationCostVO[] infoCostVOs,OrderVO orderVO){
//	if(infoCostVOs==null||infoCostVOs.length==0||orderVO==null){
//		SCMEnv.out("Warning:Params is null!");
//		return;
//	}
//	UFDouble number = new UFDouble(0.0);
//	UFDouble laborSummy = new UFDouble(0.0);
//	for (int i = 0; i < infoCostVOs.length; i++) {
//		if(infoCostVOs[i].getNoriginalcursummny()!=null)
//		laborSummy = laborSummy.add(infoCostVOs[i].getNoriginalcursummny());
//	}
//	OrderItemVO[] childVOs = (OrderItemVO[])orderVO.getChildrenVO();
//	if(childVOs.length == 0||childVOs == null){
//		SCMEnv.out("Warning:OrderItemVO[] is null!");
//		return ;
//	}
//	for (int i = 0; i < childVOs.length; i++) {
//          if(childVOs[i].getNordernum()!=null)
//        	  number = number.add(childVOs[i].getNordernum());
//	}
//	UFDouble templaborSummy = new UFDouble(0.0);
//	for (int i = 0; i < childVOs.length; i++) {
//		  if(i!=childVOs.length-1){
//		UFDouble param1 = childVOs[i].getNordernum().div(
//				number).multiply(laborSummy);        
//        	childVOs[i].setNoriginaltaxpricemny(childVOs[i].getNoriginaltaxpricemny().add(param1));
//        	childVOs[i].setNorgtaxprice(childVOs[i].getNoriginaltaxpricemny().div(childVOs[i].getNordernum()));
//        	templaborSummy = templaborSummy.add(param1);
//		  }else {
//				UFDouble param1 = laborSummy.sub(templaborSummy); 
//				childVOs[i].setNoriginaltaxpricemny(childVOs[i].getNoriginaltaxpricemny().add(param1));
//            	childVOs[i].setNorgtaxprice(childVOs[i].getNoriginaltaxpricemny().div(childVOs[i].getNordernum()));
//		  }
//	}
}
   

}