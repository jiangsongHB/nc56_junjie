package nc.ui.pi.invoice;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import nc.bs.bd.b21.BusinessCurrencyRateUtil;
import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.itf.pi.IDataPowerForInv;
import nc.itf.pi.IInvoiceD;
import nc.itf.scm.cenpur.service.TempTableUtil;
import nc.itf.uap.pf.IPFMetaModel;
import nc.ui.bd.b21.CurrParamQuery;
import nc.ui.bd.b39.PhaseRefModel;
import nc.ui.ml.NCLangRes;
import nc.ui.pf.query.ICheckRetVO;
import nc.ui.pi.InvoiceHelper;
import nc.ui.pi.pub.PiPqPublicUIClass;
import nc.ui.po.oper.IButtonConstPo;
import nc.ui.po.pub.PoPublicUIClass;
import nc.ui.po.ref.OtherRefModel;
import nc.ui.pps.PricStlHelper;
import nc.ui.pr.pray.IButtonConstPr;
import nc.ui.pu.pub.PIPluginUI;
import nc.ui.pu.pub.POPubSetUI;
import nc.ui.pu.pub.POPubSetUI2;
import nc.ui.pu.pub.PuGetUIValueTool;
import nc.ui.pu.pub.PuTool;
import nc.ui.pu.pub.PubHelper;
import nc.ui.pu.pub.PurPsnRefModel;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.FramePanel;
import nc.ui.pub.ToftPanel;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UICheckBox;
import nc.ui.pub.beans.UIComboBox;
import nc.ui.pub.beans.UIMenuItem;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.beans.UITextField;
import nc.ui.pub.bill.BillActionListener;
import nc.ui.pub.bill.BillBodyMenuListener;
import nc.ui.pub.bill.BillCardBeforeEditListener;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.bill.BillData;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillEditListener;
import nc.ui.pub.bill.BillEditListener2;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillItemEvent;
import nc.ui.pub.bill.BillModel;
import nc.ui.pub.bill.BillScrollPane;
import nc.ui.pub.bill.BillTableMouseListener;
import nc.ui.pub.bill.BillUIUtil;
import nc.ui.pub.bill.DefaultCurrTypeBizDecimalListener;
import nc.ui.pub.bill.DefaultCurrTypeDecimalAdapter;
import nc.ui.pub.bill.IBillData;
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
import nc.ui.scm.file.DocumentManager;
import nc.ui.scm.ic.freeitem.FreeItemRefPane;
import nc.ui.scm.plugin.InvokeEventProxy;
import nc.ui.scm.pub.BillTools;
import nc.ui.scm.pub.BusiBillManageTool;
import nc.ui.scm.pub.CacheTool;
import nc.ui.scm.pub.CollectSettingDlg;
import nc.ui.scm.pub.bill.ButtonTree;
import nc.ui.scm.pub.billutil.BillCardPanelHelper;
import nc.ui.scm.pub.billutil.ClientCacheHelper;
import nc.ui.scm.pub.def.DefSetTool;
import nc.ui.scm.pub.panel.RelationsCal;
import nc.ui.scm.pub.print.ISetBillVO;
import nc.ui.scm.pub.print.ScmPrintTool;
import nc.ui.scm.pub.query.SCMQueryConditionDlg;
import nc.ui.scm.pub.report.BillRowNo;
import nc.ui.scm.pub.sourceref.SourceRefDlg;
import nc.vo.bd.b06.PsndocVO;
import nc.vo.ic.pub.vmi.VmiSumHeaderVO;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.pi.ConstantVO;
import nc.vo.pi.HintMessageException;
import nc.vo.pi.InvoiceHeaderVO;
import nc.vo.pi.InvoiceItemVO;
import nc.vo.pi.InvoiceVO;
import nc.vo.pi.NormalCondVO;
import nc.vo.po.pub.Operlog;
import nc.vo.po.pub.OrderPubVO;
import nc.vo.pps.PricParaVO;
import nc.vo.pu.exception.RwtPiToPoException;
import nc.vo.pu.exception.RwtPiToScException;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CommonConstant;
import nc.vo.pub.VOStatus;
import nc.vo.pub.ValidationException;
import nc.vo.pub.bill.BillTempletVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.para.SysInitVO;
import nc.vo.pub.pfflow04.MessagedriveVO;
import nc.vo.pub.query.ConditionVO;
import nc.vo.scm.constant.ScmConst;
import nc.vo.scm.ic.bill.FreeVO;
import nc.vo.scm.ic.bill.InvVO;
import nc.vo.scm.pu.BillStatus;
import nc.vo.scm.pu.BillTypeConst;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.scm.pu.VariableConst;
import nc.vo.scm.pub.SCMEnv;
import nc.vo.scm.pub.session.ClientLink;
import nc.vo.scm.pub.smart.ObjectUtils;
import nc.vo.wfengine.engine.exception.EngineException;
import nc.vo.wfengine.engine.exception.TaskInvalidateException;

/**
 * 发票维护UI 作者：王印芬
 * 
 * @version 最后修改日期
 * @see 需要参见的其它类
 * @since 从产品的那一个版本，此类被添加进来。（可选）
 * <li><b>根据V56需求，发票类型增加“其它”</b></li>
 */
public class InvoiceUI extends nc.ui.pub.ToftPanel implements BillEditListener, BillEditListener2,
    BillCardBeforeEditListener, BillTableMouseListener, BillBodyMenuListener, ICheckRetVO, ListSelectionListener,
    ISetBillVO, nc.ui.scm.pub.bill.IBillExtendFun, IBillRelaSortListener2, ILinkMaintain,// 关联修改
    ILinkAdd,// 关联新增
    ILinkApprove,// 审批流
    ILinkQuery,// 逐级联查
    BillActionListener {
  
  String sContinueBillTypeName = "";
  boolean feeFlag = false;//费用发票审核标志 add by QuSida (佛山骏杰) 2010-9-26
  boolean bIswaitaudit = false;

  // {业务类型主键=是否配置了审批驱动传应付}
  HashMap<String, UFBoolean> m_mapBusitypeAppriveDrive = null;

  // since v55,重排行号
  UIMenuItem m_miReSortRowNo = null;

  private ButtonObject m_btnReSortRowNo = null;

  UIMenuItem m_miCardEdit = null;

  private ButtonObject m_btnCardEdit = null;
  
  /**
   * 二次开发插件支持 by zhaoyha at 2009.1.19
   */
  private InvokeEventProxy invokeEventProxy;
  
  /**
   * 二次开发插件支持 by zhaoyha at 2009.1.19
   */
  private PIPluginUI pluginui;

  // since v55,重排行号监听
  class IMenuItemListener implements ActionListener {
    public void actionPerformed(ActionEvent event) {
      InvoiceUI.this.onMenuItemClick(event);
    };
  }

  class MyBillData implements IBillData {
    public void prepareBillData(nc.ui.pub.bill.BillData bd) {
      InvoiceUI.this.initRefpane(bd);
    }
  }

  // 按钮树实例,since v51
  private ButtonTree m_btnTree = null;

  private final static int INV_PANEL_CARD = 0;

  private final static int INV_PANEL_LIST = 1;

  // 某业务类型是否有自制单据
  private boolean m_bCouldMaked;

  // 是否曾经进行过查询,以确定刷新按钮是否可用
  private boolean m_bEverQueryed = false;

  // 是否增加请购单(包括复制请购单)
  private boolean m_bAdd = false;

  // 是否保留最初制单人
  private boolean isAllowedModifyByOther = false;

  // 发票审批时对“在检”状态批次的控制方式:不控制、提示、控制，默认为不控制。
  private String m_sAuditControlMode = null;

  private ButtonObject m_bizButton = null;// 当前业务类型按钮

  // 消息中心审批界面按钮
  private ButtonObject m_btnAudit = new ButtonObject(nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
      "UC001-0000027")/* @res "审批" */,
      nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UC001-0000027")/*
                                                                             * @res
                                                                             * "审批"
                                                                             */, 2, "审批"); /*-=notranslate=-*/

  private ButtonObject m_btnUnAudit = new ButtonObject(nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
      "UC001-0000028")/* @res "弃审" */,
      nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPP40040401-000149")/*
                                                                                     * @res
                                                                                     * "执行弃审操作"
                                                                                     */, 5, "弃审"); /*-=notranslate=-*/

  // 业务类型组按钮
  private ButtonObject m_btnInvBillBusiType = null;// 业务类型

  // 增加组按钮
  private ButtonObject m_btnInvBillNew = null;

  // 保存组按钮
  private ButtonObject m_btnInvBillSave = null;

  // 维护组按钮
  private ButtonObject m_btnBillMaintain = null;// 维护

  private ButtonObject m_btnInvBillModify = null;// 修改

  private ButtonObject m_btnInvBillCancel = null;// 取消

  private ButtonObject m_btnInvBillDiscard = null;// 删除

  private ButtonObject m_btnInvBillCopy = null;// 复制

  private ButtonObject m_btnInvBillConversion = null;// 放弃转单

  // 行操作组按钮
  private ButtonObject m_btnInvBillRowOperation = null;// 行操作

  private ButtonObject m_btnInvBillAddRow = null;// 增行

  private ButtonObject m_btnInvBillDeleteRow = null;// 删行

  private ButtonObject m_btnInvBillInsertRow = null;// 插入行

  private ButtonObject m_btnInvBillCopyRow = null;// 复制行

  private ButtonObject m_btnInvBillPasteRow = null;// 粘贴行

  private ButtonObject m_btnInvBillPasteRowTail = null;// 粘贴行到表尾

  // 参照增行
  private ButtonObject btnBillAddContinue = null;

  // 执行组按钮
  private ButtonObject m_btnInvBillExcute = null;

  private ButtonObject m_btnSendAudit = null;// 送审

  private ButtonObject m_btnInvBillAudit = null;// 审核

  private ButtonObject m_btnInvBillUnAudit = null;// 弃审

  // 查询组按钮
  private ButtonObject m_btnInvBillQuery = null;// 查询

  // 浏览组按钮
  private ButtonObject m_btnBillBrowsed = null;// 浏览

  private ButtonObject m_btnInvBillRefresh = null;// 刷新

  private ButtonObject m_btnInvBillGoFirstOne = null;// 首页

  private ButtonObject m_btnInvBillGoNextOne = null;// 下一页

  private ButtonObject m_btnInvBillGoPreviousOne = null;// 上一页

  private ButtonObject m_btnInvBillGoLastOne = null;// 末页

  private ButtonObject m_btnInvSelectAll = null;// 全选

  private ButtonObject m_btnInvDeselectAll = null;// 全消

  // 卡片显示/列表显示(切换)
  private ButtonObject m_btnInvShift = null;// 卡片显示/列表显示

  // 打印管理组按钮
  private ButtonObject m_btnPrints = null;

  private ButtonObject m_btnInvBillPreview = null;// 预览

  private ButtonObject m_btnInvBillPrint = null;// 打印

  private ButtonObject btnBillCombin = null;// 合并显示

  // 辅助查询组按钮
  private ButtonObject m_btnInvBillAssist = null;

  private ButtonObject m_btnLnkQuery = null;// 联查

  private ButtonObject m_btnQueryForAudit = null;// 审批流状态(状态查询)

  private ButtonObject m_btnHqhp = null;// 优质优价取价

  // 辅助功能组按钮
  private ButtonObject m_btnOthersFuncs = null;

  private ButtonObject m_btnCrtAPBill = null;;// 传应付

  private ButtonObject m_btnDelAPBill = null;;// 取消传应付

  private ButtonObject m_btnDocManage = null;;// 文档管理

  // 用于审批流的ID
  private String m_cauditid = null;

  // 审批流辅助按钮组
  private ButtonObject m_btnAuditFlowAssist = null;// 辅助

  private ButtonObject[] aryForAudit = new ButtonObject[] {
      m_btnAudit, m_btnUnAudit, m_btnInvBillAssist, m_btnOthersFuncs
  };

  // 卡片界面,列表界面,模板数据
  private InvBillPanel m_billPanel = null;

  private InvListPanel m_listPanel = null;

  // 发票查询对话框
  private InvoiceUIQueDlg m_InvQueDlg = null;

  // 缓存中的VO数组，当前的VO位置
  private InvoiceVO[] m_InvVOs;

  // 是否是自制单据
  private boolean m_isMakedBill = false;

  // 卡片的浏览状态:普通浏览,单据转入浏览
  private int m_nBillBrowseState;

  private int m_nCurInvVOPos;

  // 列表的状态:普通列表,单据转入列表
  private int m_nCurOperState;

  // ///======界面状态: 单据卡片 单据列表
  private int m_nCurPanelMode = 0;

  private int m_nListOperState;

  private int m_nLstInvVOPos;

  // 选中的行数目，用于确定按钮的状态
  private int m_nSelectedRowCount;

  // 上一次的币种小数位
  private int m_oldCurrMoneyDigit = 2;

  // 批量打印保存列表表头单据行号
  protected ArrayList listSelectBillsPos = null;

  // 存货管理档案字段名
  private final String m_sInvMngIDItemKey = "invcode";

  // //=======发票维护
  // 当前业务类型,原有业务类型
  private String m_strCurBizeType;

  private String m_strBtnTag;// 保存"业务类型"

  // 状态条的头
  private String m_strHeadHintText = 
//    nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPP40040401-000101")/*
//                                                                                                                   * @res
//                                                                                                                   * "未选择业务类型"
//                                                                                                                   */
//      + "    " + 
      nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPP40040401-000026")/*
                                                                                               * @res
                                                                                               * "操作状态"
                                                                                               */
      + nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPP40040401-000102")/*
                                                                                       * @res
                                                                                       * "："
                                                                                       */;

  // 批量打印工具
  private ScmPrintTool printList = null;

  private ScmPrintTool printCard = null;

  private final int STATE_BROWSE_NORMAL = 2;

  private final int STATE_EDIT = 1;

  // 用户的当前及操作状态:初始化,普通浏览,单据转入浏览,编辑,普通列表,单据转入列表
  private final int STATE_INIT = 0;

  private final int STATE_LIST_FROM_BILLS = 5;

  private final int STATE_LIST_NORMAL = 4;

  private boolean m_bCopy = false;// 单据复制

  // 订单转发票结算方式
  private String m_sOrder2InvoiceSettleMode = null;

  // 入库转发票结算方式
  private String m_sStock2InvoiceSettleMode = null;

  // 是否暂估应付
  private String m_sZGYF = null;

  private Hashtable hBillStatusBeforeEdit = new Hashtable();

  private int cunpos = 0;

  // 最大的金额精度 用于合计
  private int iMaxMnyDigit = 8;

  // 发票所在当前行
  private int currentPos = 0;

  private boolean splitFlag = false;

  // 存放受托代销业务类型
  Set<String> cBizTypeTable = new HashSet<String>();

  // 转单时列表下VO在缓存中的位置
  private String[] VOsPos = null;

  private POPubSetUI2 m_cardPoPubSetUI2 = null;

  // 由界面类传递过来的汇率精度设置工具实例
  private POPubSetUI2 m_listPoPubSetUI2 = new POPubSetUI2();

  // 是否为编辑币种后
  private int isEditCur = 0;

  // 转单保存成功后的发票VO
  private Vector m_vSavedVO = null;

  // 金额,税额,价税合计主键, 设置精度
  private String m_itemMny_fi[] = new String[] {
    "noriginalpaymentmny"
  };// 原币累计付款

  // 付款金额主键, 设置精度
  private String m_itemMny_bu[] = new String[] {
      "noriginalcurmny", "noriginaltaxmny", "noriginalsummny"
  };// 原币无税金额、原币税额、原币价税合计

  // 调整金额还是折扣
  private int iChange = 7;
  
  //业务类型是否，发票有订单和入库单两个来源，即发票只能从订单参照费用行
  private Map<String,UFBoolean> onlyRefFeeOrder = new HashMap<String,UFBoolean>();
  
  //保存当前用户选择的发票类型
  private int curInvType = 0;
  
  //缓存单据模板VO，列表和卡片只加载一次
  private BillTempletVO billTempletVo = null;
  
//  private ButtonObject[] extendBtns = null;
//  private ButtonObject feeInvoice = null;
//  private ButtonObject[] btnss = null;
// 
//private ButtonObject[] getBtnss(){
//	if(btnss == null ){
//		 btnss = new ButtonObject[m_btnTree.getButtonArray().length+getExtendBtns().length];
//	      System.arraycopy(m_btnTree.getButtonArray(), 0, btnss, 0, m_btnTree.getButtonArray().length);
//	      System.arraycopy(getExtendBtns(), 0, btnss, m_btnTree.getButtonArray().length, getExtendBtns().length);
//	      return btnss;
//	}
//	return btnss;
//}
  /**
   * InvBillClientUI 构造子注解。
   */
  public InvoiceUI() {
    super();
    initi();
  }

  //For V56
  public InvoiceUI(FramePanel fp) {
    super();
    setFrame(fp);
    initi();
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
   * 
   * @author lxd
   * @time 2007-3-15 下午02:23:07
   */
  private void createBtnInstances() {

    // 业务类型组按钮
    m_btnInvBillBusiType = getBtnTree().getButton(IButtonConstInv.BTN_BUSINESS_TYPE);// 业务类型

    // 增加组按钮
    m_btnInvBillNew = getBtnTree().getButton(IButtonConstInv.BTN_ADD);

    // 保存组按钮
    m_btnInvBillSave = getBtnTree().getButton(IButtonConstInv.BTN_SAVE);

    // 维护组按钮
    m_btnBillMaintain = getBtnTree().getButton(IButtonConstInv.BTN_BILL);// 维护
    m_btnInvBillModify = getBtnTree().getButton(IButtonConstInv.BTN_BILL_EDIT);// 修改
    m_btnInvBillCancel = getBtnTree().getButton(IButtonConstInv.BTN_BILL_CANCEL);// 取消
    m_btnInvBillDiscard = getBtnTree().getButton(IButtonConstInv.BTN_BILL_DELETE);// 删除
    m_btnInvBillCopy = getBtnTree().getButton(IButtonConstInv.BTN_BILL_COPY);// 复制

    // 行操作组按钮
    m_btnInvBillRowOperation = getBtnTree().getButton(IButtonConstInv.BTN_LINE);// 行操作
    m_btnInvBillAddRow = getBtnTree().getButton(IButtonConstInv.BTN_LINE_ADD);// 增行
    m_btnInvBillDeleteRow = getBtnTree().getButton(IButtonConstInv.BTN_LINE_DELETE);// 删行
    m_btnInvBillInsertRow = getBtnTree().getButton(IButtonConstInv.BTN_LINE_INSERT);// 插入行
    m_btnInvBillCopyRow = getBtnTree().getButton(IButtonConstInv.BTN_LINE_COPY);// 复制行
    m_btnInvBillPasteRow = getBtnTree().getButton(IButtonConstInv.BTN_LINE_PASTE);// 粘贴行
    m_btnInvBillPasteRowTail = getBtnTree().getButton(IButtonConstInv.BTN_LINE_PASTE_TAIL);// 粘贴行到表尾
    // 支持弹出菜单中“重排行号”功能
    m_btnReSortRowNo = getBtnTree().getButton(IButtonConstPr.BTN_ADD_NEWROWNO);
    m_btnCardEdit = getBtnTree().getButton(IButtonConstPr.BTN_CARDEDIT);
    m_miReSortRowNo = BillTools.addBodyMenuItem(getBillCardPanel(), m_btnReSortRowNo, null);
    m_miCardEdit = BillTools.addBodyMenuItem(getBillCardPanel(), m_btnCardEdit, null);
    if(m_miReSortRowNo != null){
      m_miReSortRowNo.addActionListener(new IMenuItemListener());
    }
    if(m_miCardEdit != null){
      m_miCardEdit.addActionListener(new IMenuItemListener());
    }
    //
    btnBillAddContinue = getBtnTree().getButton(IButtonConstPo.BTN_ADDCONTINUE);// 参照增行
    // 执行组按钮
    m_btnInvBillExcute = getBtnTree().getButton(IButtonConstInv.BTN_EXECUTE);
    m_btnSendAudit = getBtnTree().getButton(IButtonConstInv.BTN_EXECUTE_AUDIT);// 送审
    m_btnInvBillAudit = getBtnTree().getButton(IButtonConstInv.BTN_AUDIT);// 审核
    m_btnInvBillUnAudit = getBtnTree().getButton(IButtonConstInv.BTN_EXECUTE_AUDIT_CANCEL);// 弃审
    m_btnInvBillConversion = getBtnTree().getButton(IButtonConstInv.BTN_REF_CANCEL);// 放弃转单

    // 查询组按钮
    m_btnInvBillQuery = getBtnTree().getButton(IButtonConstInv.BTN_QUERY);// 卡片查询

    // 浏览组按钮
    m_btnBillBrowsed = getBtnTree().getButton(IButtonConstInv.BTN_BROWSE);// 浏览
    m_btnInvBillRefresh = getBtnTree().getButton(IButtonConstInv.BTN_BROWSE_REFRESH);// 卡片刷新
    m_btnInvBillGoFirstOne = getBtnTree().getButton(IButtonConstInv.BTN_BROWSE_TOP);// 首页
    m_btnInvBillGoNextOne = getBtnTree().getButton(IButtonConstInv.BTN_BROWSE_NEXT);// 下一页
    m_btnInvBillGoPreviousOne = getBtnTree().getButton(IButtonConstInv.BTN_BROWSE_PREVIOUS);// 上一页
    m_btnInvBillGoLastOne = getBtnTree().getButton(IButtonConstInv.BTN_BROWSE_BOTTOM);// 末页

    m_btnInvSelectAll = getBtnTree().getButton(IButtonConstInv.BTN_BROWSE_SELECT_ALL);// 全选
    m_btnInvDeselectAll = getBtnTree().getButton(IButtonConstInv.BTN_BROWSE_SELECT_NONE);// 全消

    // 卡片显示/列表显示(切换)
    m_btnInvShift = getBtnTree().getButton(IButtonConstInv.BTN_SWITCH);// 卡片显示/列表显示

    // 打印管理组按钮
    m_btnPrints = getBtnTree().getButton(IButtonConstInv.BTN_PRINT);// 打印管理
    m_btnInvBillPreview = getBtnTree().getButton(IButtonConstInv.BTN_PRINT_PREVIEW);// 预览
    m_btnInvBillPrint = getBtnTree().getButton(IButtonConstInv.BTN_PRINT_PRINT);// 打印
    btnBillCombin = getBtnTree().getButton(IButtonConstInv.BTN_PRINT_DISTINCT);// 合并显示

    // 辅助查询组按钮
    m_btnInvBillAssist = getBtnTree().getButton(IButtonConstInv.BTN_ASSIST_QUERY);// 辅助查询
    m_btnLnkQuery = getBtnTree().getButton(IButtonConstInv.BTN_ASSIST_QUERY_RELATED);// 联查
    m_btnQueryForAudit = getBtnTree().getButton(IButtonConstInv.BTN_ASSIST_QUERY_WORKFLOW);// 审批流状态(状态查询)
    m_btnHqhp = getBtnTree().getButton(IButtonConstInv.BTN_HQHP);// 优质优价取价

    // 辅助功能组按钮
    m_btnOthersFuncs = getBtnTree().getButton(IButtonConstInv.BTN_ASSIST_FUNC);
    m_btnCrtAPBill = getBtnTree().getButton(IButtonConstInv.BTN_CrtAPBill);
    m_btnDelAPBill = getBtnTree().getButton(IButtonConstInv.BTN_DelAPBill);
    m_btnDocManage = getBtnTree().getButton(IButtonConstInv.BTN_ASSIST_FUNC_DOCUMENT);// 文档管理
  }

  /**
   * 获取按钮树，类唯一实例。
   * <p>
   * <b>examples:</b>
   * <p>
   * 使用示例
   * <p>
   * <b>参数说明</b>
   * 
   * @return
   * <p>
   * @author lxd
   * @time 2007-3-15 下午05:04:22
   */
  private ButtonTree getBtnTree() {
    if (m_btnTree == null) {
      try {
        m_btnTree = new ButtonTree(getModuleCode());
//        ButtonObject[] btnsExtend = getExtendBtns();
//        
//            for(int j=0; j<btnsExtend.length;j++){
//                getBtnTree().addMenu(btnsExtend[j]);
//            }
//        
      }
      catch (BusinessException be) {
        showHintMessage(be.getMessage());
        return null;
      }
    }
    return m_btnTree;
  }

  /**
   * 根据ID得到显示的状态
   * 
   * @param
   * @return
   * @exception 异常描述
   * @see 需要参见的其它内容
   * @since 从类的那一个版本，此方法被添加进来。（可选）
   */
  private void setMaxMnyDigit(int iMaxDigit) {
    // int iMaxDigit = nMaxDigit.intValue() ;
    // 金额相关
    getBillCardPanel().getBodyItem("noriginalcurmny").setDecimalDigits(iMaxDigit);
    getBillCardPanel().getBodyItem("noriginaltaxmny").setDecimalDigits(iMaxDigit);
    getBillCardPanel().getBodyItem("noriginalsummny").setDecimalDigits(iMaxDigit);
    getBillCardPanel().getBodyItem("noriginalpaymentmny").setDecimalDigits(iMaxDigit);
    getBillCardPanel().getBodyItem("nmoney").setDecimalDigits(iMaxDigit);
    getBillCardPanel().getBodyItem("ntaxmny").setDecimalDigits(iMaxDigit);
    getBillCardPanel().getBodyItem("nsummny").setDecimalDigits(iMaxDigit);
    getBillCardPanel().getBodyItem("npaymentmny").setDecimalDigits(iMaxDigit);
    getBillCardPanel().getBodyItem("nexchangeotobrate").setDecimalDigits(iMaxDigit);
    try{
      int size = getBillCardPanel().getRowCount();
      int iCurMnyDigit = m_listPoPubSetUI2.getCCurrDecimal();
      for (int i = 0; i < size; i++) {
  
//        // 取得币种
//        String sCurrId = (String) getBillListPanel().getBodyBillModel().getValueAt(i, "ccurrencytypeid");
//        int[] iaExchRateDigit = null;
//        if(sCurrId == null){
//          sCurrId = CurrParamQuery.getInstance().getLocalCurrPK(getPk_corp());//本币ID
//          BusinessCurrencyRateUtil  caCurCorp  = new BusinessCurrencyRateUtil(getPk_corp());
//        }
//        // 得到折本、折辅汇率精度
//        iaExchRateDigit = m_listPoPubSetUI2.getBothExchRateDigit(getPk_corp(), sCurrId);
//        if (iaExchRateDigit == null || iaExchRateDigit.length == 0) {
//        }
//        else {
          getBillCardPanel().getBodyItem("nsummny").setDecimalDigits(iCurMnyDigit);// 本币价税合计
          getBillCardPanel().getBodyItem("ntaxmny").setDecimalDigits(iCurMnyDigit);// 本币税额
          getBillCardPanel().getBodyItem("nmoney").setDecimalDigits(iCurMnyDigit);// 本币无税金额
          getBillCardPanel().getBodyItem("npaymentmny").setDecimalDigits(iCurMnyDigit);// 本币累计付款
//        }
      }
    }catch(Exception e){
      showErrorMessage(e.getMessage());
      SCMEnv.out(e);
    }
  }

  /**
   * 根据ID得到显示的状态
   * 
   * @param
   * @return
   * @exception 异常描述
   * @see 需要参见的其它内容
   * @since 从类的那一个版本，此方法被添加进来。（可选）
   */
  private void setMaxMnyDigitList(int iMaxDigit) {
    // if(true) return;

    // int iMaxDigit = nMaxDigit.intValue() ;
    // 金额相关
    getBillListPanel().getBodyItem("noriginalcurmny").setDecimalDigits(iMaxDigit);
    getBillListPanel().getBodyItem("noriginaltaxmny").setDecimalDigits(iMaxDigit);
    getBillListPanel().getBodyItem("noriginalsummny").setDecimalDigits(iMaxDigit);
    getBillListPanel().getBodyItem("noriginalpaymentmny").setDecimalDigits(iMaxDigit);
    getBillListPanel().getBodyItem("nmoney").setDecimalDigits(iMaxDigit);
    getBillListPanel().getBodyItem("ntaxmny").setDecimalDigits(iMaxDigit);
    getBillListPanel().getBodyItem("nsummny").setDecimalDigits(iMaxDigit);
    getBillListPanel().getBodyItem("npaymentmny").setDecimalDigits(iMaxDigit);
    getBillListPanel().getBodyItem("nexchangeotobrate").setDecimalDigits(iMaxDigit);

    int size = getBillCardPanel().getRowCount();
    for (int i = 0; i < size; i++) {

      int iCurMnyDigit = m_listPoPubSetUI2.getCCurrDecimal();

      getBillListPanel().getBodyItem("nsummny").setDecimalDigits(iCurMnyDigit);// 本币价税合计
      getBillListPanel().getBodyItem("ntaxmny").setDecimalDigits(iCurMnyDigit);// 本币税额
      getBillListPanel().getBodyItem("nmoney").setDecimalDigits(iCurMnyDigit);// 本币无税金额
      getBillListPanel().getBodyItem("npaymentmny").setDecimalDigits(iCurMnyDigit);// 本币累计付款

    }

  }

  /**
   * 作者：王印芬 功能：点击一个参照的按钮时，其他相关项的变化。 参数：ActionEvent e 捕捉到的ActionEvent事件 返回：无 例外：无
   * 日期：(2002-3-13 11:39:21) 修改日期，修改人，修改原因，注释标志： 2002-03-20 wyf 提高效率，在该函数中即完成分支
   * wyf add/modify/delete 2002-03-21 begin/end 2002-09-18 wyf 加入对仓库参照的响应
   */
  public void actionPerformed(ActionEvent e) {

    if (e.getSource() == ((UIRefPane) getBillCardPanel().getHeadItem("cemployeeid").getComponent()).getUIButton()) {
      actionPerformedEmployee(e);
    }
    // else if (e.getSource() == ((UIRefPane)
    // getInvBillPanel().getBodyItem("vfree0").getComponent()).getUIButton()) {
    // PuTool.actionPerformedFree(getInvBillPanel(), e, new String[] {
    // "cmangid", "invcode", "invname", "invspec", "invtype" }, new String[] {
    // "vfree0", "vfree1", "vfree2", "vfree3", "vfree4", "vfree5" });
    // }
    else if (e.getSource() == ((UIRefPane) getBillCardPanel().getBodyItem("cprojectphasename").getComponent())
        .getUIButton()) {
      actionPerformedProjectPhase(e);
    }
    else if (e.getSource() == ((UIRefPane) getBillCardPanel().getBodyItem("cwarehousename").getComponent())
        .getUIButton()) {
      PuTool.restrictWarehouseRefByStoreOrg(getBillCardPanel(), getPk_corp(), getBillCardPanel().getHeadItem(
          "cstoreorganization").getValue(), "cwarehousename");
    }

  }

  /**
   * 作者：王印芬 功能：点击表头的业务员参照时，立刻更新部门参照，修改为该业务员对应的部门 参数：ActionEvent e
   * 捕捉到的ActionEvent事件 返回：无 例外：无 日期：(2002-3-13 11:39:21) 修改日期，修改人，修改原因，注释标志：
   * 2002-03-21 wyf 提高效率 wyf add/modify/delete 2002-03-21 begin/end 2008-10-23
   * wyf 业务员参照修改为树表状
   */
  private void actionPerformedEmployee(ActionEvent e) {
    // 得到项目ID

    // 设置该部门的默认业务员, 该部门对应的业务员动态参照

    UIRefPane pane = (UIRefPane) getBillCardPanel().getHeadItem("cemployeeid").getComponent();
    String cemployeeid = pane.getRefPK();
    pane.setRefModel(new PurPsnRefModel(getPk_corp(), null));
    pane.setPK(cemployeeid);

  }

  /**
   * 作者：李亮 功能：修改表体币种后动作 参数：BillEditEvent e 捕捉到的BillEditEvent事件 返回：无 例外：无
   * 日期：(2002-3-13 11:39:21) 修改日期，修改人，修改原因，注释标志： 2002-05-15 wyf
   * 提取代码到公共方法setBodyCurrRelated()中 wyf add/modify/delete 2002-05-15 begin/end
   * 2002-07-12 wyf 原先币种变化后取的为默认价，现取为合同价 2002-10-18 wyf 修改前：驱动数据重新计算的KEY为数量，
   * 修改后：为原币单价，该改变为限制所有的改变都会影响计划到货日期的修改
   */
  private void afterEditWhenBodyCurrency(BillEditEvent e) {

    int iRow = e.getRow();
    // 设置表体折本及折辅汇率的精度和值并设置其可编辑性
    setExchangeRateBody(iRow, true, null);
    if (e.getValue() == null || e.getValue().toString().trim().equals("")) {
      return;
    }
    getBillCardPanel().setDefaultPrice(iRow, iRow, null);

    String sCurrTypeId = PuPubVO
        .getString_TrimZeroLenAsNull(getBillCardPanel().getBodyValueAt(iRow, "ccurrencytypeid"));
    if (sCurrTypeId == null) {
      return;
    }
    // 以下值应重新触发，因可能币种位数不同，需重新计算
    // 因无法得到旧币种ID，因此不管币种精度是否改变，均重新计算，虽然有可能导致数据改变。

    UFDouble ninvoicenum = (UFDouble) getBillCardPanel().getBodyValueAt(iRow, "nexchangeotobrate");
    UFDouble ncurprice = (UFDouble) getBillCardPanel().getBodyValueAt(iRow, "noriginalcurprice");
    UFDouble summy = (UFDouble) getBillCardPanel().getBodyValueAt(iRow, "noriginalsummny");
    String sChangedKey = null;
    if (ninvoicenum != null && ninvoicenum.doubleValue() > 0.00 && ncurprice != null && ncurprice.doubleValue() > 0.00) {
      sChangedKey = "ninvoicenum";
    }
    else if ((summy != null && summy.doubleValue() > 0.00)) {
      sChangedKey = "noriginalsummny";
    }
    else {
      sChangedKey = "ninvoicenum";
    }
    afterEditWhenBodyRelationsCal(new BillEditEvent(getBillCardPanel().getBodyItem(sChangedKey), getBillCardPanel()
        .getBodyValueAt(iRow, sChangedKey), sChangedKey, iRow, BillItem.BODY));
    updateUI();
  }

  /**
   * 作者：李亮 功能：修改数量、单价、金额后动作 参数：BillEditEvent e 捕捉到的BillEditEvent事件 返回：无 例外：无
   * 日期：(2002-3-13 11:39:21) 修改日期，修改人，修改原因，注释标志： 2002-10-18 wyf
   * 修改前：驱动数据重新计算的KEY为数量， 修改后：为原币单价，该改变为适应：只有数量改变会影响计划到货日期的修改 2003-01-20 wyf
   * 修改前：数量变化导致计划到货日期变化， 修改后：如果数量由空或变为有值，则导致计划到货日期变化，否则不变
   */

  private void afterEditWhenBodyRelationsCal(BillEditEvent e) {

    int iRow = e.getRow();
    if ((!(e.getKey().equals("idiscounttaxtype"))) && (e.getValue() != null)
        && (!(e.getValue().toString().trim().equals("")))) {
      UFDouble ndata = new UFDouble(e.getValue().toString().trim());
      if (ndata.doubleValue() < 0) {
        if (e.getKey().equals("ntaxrate")) {
          MessageDialog.showWarningDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON",
              "UPPSCMCommon-000270")/* @res "提示" */, nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401",
              "UPP40040401-000238")/* @res "税率不能小于0" */);
          getBillCardPanel().getBillModel().setValueAt(null, iRow, "ntaxrate");
          return;
        }
        else if (e.getKey().equals("noriginalcurprice")) {
          MessageDialog.showWarningDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON",
              "UPPSCMCommon-000270")/* @res "提示" */, nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401",
              "UPP40040401-000239")/* @res "单价不能小于0" */);
          getBillCardPanel().getBillModel().setValueAt(null, iRow, "noriginalcurprice");
          return;
        }
      }
      if (e.getKey().equals("ndiscountrate")) {
        // if ((ndata.doubleValue() < 0) || (ndata.doubleValue() > 100))
        // {
        if (ndata.compareTo(VariableConst.ZERO) < 0) {
          // MessageDialog.showWarningDlg(this,"提示","扣率必须大于0小于100！");
          MessageDialog.showWarningDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON",
              "UPPSCMCommon-000270")/* @res "提示" */, nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401",
              "UPP40040401-000240")/* @res "扣率必须大于0" */);
          getBillCardPanel().getBillModel().setValueAt(null, iRow, "ndiscountrate");
          return;
        }
      }
    }

    // wyf 2002-10-21 add begin
    // 原有的订单数量
    // Object oNum = null;
    // if (e.getKey().equals("ninvoicenum")) {
    // oNum = e.getOldValue();
    // } else {
    // oNum = getInvBillPanel().getBodyValueAt(iRow, "ninvoicenum");
    // }
    // UFDouble dOldNum = (oNum == null || oNum.toString().trim().equals("")) ?
    // VariableConst.ZERO
    // : new UFDouble(oNum.toString());
    // wyf 2002-10-21 add end

    // 计算数量关系
    // calRelation(e);
    afterEditInvBillRelations(e);

  }

  /**
   * 作者：王印芬 功能：设置表体折本及折辅汇率的精度和值并设置其可编辑性 参数： int iRow boolean bResetExchValue
   * 是否需要重新设置表体行的汇率值 返回：无 例外：无 日期：(2002-5-13 11:39:21) 修改日期，修改人，修改原因，注释标志：
   */
  private void setExchangeRateBody(int iRow, boolean bResetExchValue, InvoiceItemVO items) {

    String dInvoiceDate = getBillCardPanel().getHeadItem("dinvoicedate").getValue();
    String sCurrId = (String) getBillCardPanel().getBodyValueAt(iRow, "ccurrencytypeid");
    if (sCurrId == null || sCurrId.trim().length() == 0) {
      sCurrId = getBillCardPanel().getHeadItem("ccurrencytypeid").getValue();
    }
    // 首先设置显示精度
    setRowDigits_ExchangeRate(getPk_corp(), iRow, getBillCardPanel().getBillModel(), m_cardPoPubSetUI2);
    UFDouble nexchangeotobrate = null;
    if (items != null) {
      nexchangeotobrate = items.getNexchangeotobrate();
    }
    else {
      nexchangeotobrate = PuPubVO.getUFDouble_NullAsZero(getBillCardPanel().getBodyValueAt(iRow, "nexchangeotobrate"));
    }
    // 设置值
    if (bResetExchValue && dInvoiceDate != null && dInvoiceDate.trim().length() > 0) {
      UFDouble[] daRate = null;
      String strCurrDate = dInvoiceDate;
      if (strCurrDate == null || strCurrDate.trim().length() == 0) {
        strCurrDate = PoPublicUIClass.getLoginDate() + "";
      }
      daRate = m_cardPoPubSetUI2.getBothExchRateValue(getPk_corp(), sCurrId, new UFDate(dInvoiceDate));
      // UFDouble nexchangeotobrate =
      // (UFDouble)getInvBillPanel().getBodyValueAt(iRow, "nexchangeotobrate");
      // 币种编辑后加入默认值
      if (isEditCur == 1) {
        getBillCardPanel().setBodyValueAt(daRate[0], iRow, "nexchangeotobrate");
      }
      else {
        if (nexchangeotobrate != null) {
          getBillCardPanel().setBodyValueAt(nexchangeotobrate, iRow, "nexchangeotobrate");
        }
        else {
          getBillCardPanel().setBodyValueAt(daRate[0], iRow, "nexchangeotobrate");
        }
      }
    }
    else if (nexchangeotobrate != null && nexchangeotobrate.doubleValue() > 0) {
      getBillCardPanel().setBodyValueAt(nexchangeotobrate, iRow, "nexchangeotobrate");
    }
    else if (nexchangeotobrate == null && dInvoiceDate != null && dInvoiceDate.trim().length() > 0) {
      UFDouble[] daRate = null;
      String strCurrDate = dInvoiceDate;
      if (strCurrDate == null || strCurrDate.trim().length() == 0) {
        strCurrDate = PoPublicUIClass.getLoginDate() + "";
      }
      daRate = m_cardPoPubSetUI2.getBothExchRateValue(getPk_corp(), sCurrId, new UFDate(dInvoiceDate));
      getBillCardPanel().setBodyValueAt(daRate[0], iRow, "nexchangeotobrate");
    }

    // 设置可编辑性
    boolean[] baEditable = null;

    baEditable = m_cardPoPubSetUI2.getBothExchRateEditable(getPk_corp(), sCurrId);

    getBillCardPanel().getBillModel().setCellEditable(iRow, "nexchangeotobrate", baEditable[0]);

    // 设置修改标志
    getBillCardPanel().getBillModel().setRowState(iRow, BillModel.MODIFICATION);
  }

  /**
   * 作者：李亮 功能：设置折本及折辅汇率小数位 参数：String pk_corp 公司ID int iflag 返回：无 例外：无
   * 日期：(2002-3-13 11:39:21) 修改日期，修改人，修改原因，注释标志： 2002-05-20 wyf 修改原先取最大精度为取各币种精度
   * wyf add/modify/delete 2002-05-20 begin/end
   */
  protected static void setRowDigits_ExchangeRate(String sPk_corp, int iRow, BillModel billModel, POPubSetUI2 setUi) {
    // 取得币种
    String sCurrId = (String) billModel.getValueAt(iRow, "ccurrencytypeid");
    int[] iaExchRateDigit = null;
    // 得到折本、折辅汇率精度
    iaExchRateDigit = setUi.getBothExchRateDigit(sPk_corp, sCurrId);
    if (iaExchRateDigit == null || iaExchRateDigit.length == 0) {
      billModel.getItemByKey("nexchangeotobrate").setDecimalDigits(5);
    }
    else {
      billModel.getItemByKey("nexchangeotobrate").setDecimalDigits(iaExchRateDigit[0]);
    }
  }

  /**
   * 作者：王印芬 功能：点击表体的项目阶段参照时，立刻捕捉该行项目管理ID，并得到该项目阶段的参照。 参数：ActionEvent e
   * 捕捉到的ActionEvent事件 返回：无 例外：无 日期：(2002-3-13 11:39:21) 修改日期，修改人，修改原因，注释标志：
   * 2002-03-21 wyf 提高效率 wyf add/modify/delete 2002-03-21 begin/end
   */
  private void actionPerformedProjectPhase(ActionEvent e) {

    int nRow = getBillCardPanel().getBillTable().getSelectedRow();
    // 项目管理ID
    String strProjectMngPk = (String) getBillCardPanel().getBodyValueAt(nRow, "cprojectid");
    // 设置根据项目ID的项目阶段参照
    UIRefPane pane = ((UIRefPane) getBillCardPanel().getBodyItem("cprojectphasename").getComponent());
    // pane.setRefModel(new CTIPhaseRefModel(strProjectMngPk)) ;
    pane.setRefModel(new PhaseRefModel(strProjectMngPk));

  }

  /**
   * 所有符合查询条件的入库单->发票VO
   * 
   * @param 参数说明
   * @return 返回值
   * @exception 异常描述
   * @see 需要参见的其它内容
   * @since 从类的那一个版本，此方法被添加进来
   * @return nc.vo.pi.InvoiceVO[]
   */
  private int addNewOneIntoInvVOs(InvoiceVO newVO) {

    if (getInvVOs() == null) {
      setInvVOs(new InvoiceVO[] {
        newVO
      });
      setCurVOPos(0);
      return 0;
    }

    InvoiceVO[] vos = new InvoiceVO[getInvVOs().length + 1];
    for (int i = 0; i < getInvVOs().length; i++) {
      vos[i] = getInvVOs()[i];
    }
    vos[vos.length - 1] = newVO;

    setInvVOs(vos);
    setCurVOPos(getInvVOs().length - 1);
    return getCurVOPos();
  }

  /**
   * 作者：王印芬 功能：处理一项变化完成时，其他相关项的变化 参数：BillEditEvent e 捕捉到的BillEditEvent事件 返回：无
   * 例外：无 日期：(2002-3-13 11:39:21) 修改日期，修改人，修改原因，注释标志： 2002-03-20 wyf
   * 提高效率，在该函数中即完成分支 wyf add/modify/delete 2002-03-21 begin/end 2002-09-17 wyf
   * 加入对库存组织改变后批次号的响应
   */
  public void afterEdit(BillEditEvent e) {

    if (getCurPanelMode() == INV_PANEL_CARD) {
      // 表头
      // 处理自定义项PK
      setHeadDefPK(e);
      if (e.getSource() == getBillCardPanel().getHeadItem("cvendormangid").getComponent()) {
        afterEditInvBillVendor(e);
      }
      else if (e.getSource() == getBillCardPanel().getHeadItem("cdeptid").getComponent()) {
        afterEditInvBillDept(e);
      }
      else if (e.getSource() == getBillCardPanel().getHeadItem("cemployeeid").getComponent()) {
        afterEditInvBillEmployee(e);
      }
      else if (e.getSource() == getBillCardPanel().getHeadItem("caccountbankid").getComponent()) {
        afterEditInvBillBank(e);
      }
      else if (e.getSource() == getBillCardPanel().getHeadItem("cfreecustid").getComponent()) {
        afterEditInvBillFreeCust(e);
      }
      else if (e.getSource() == getBillCardPanel().getHeadItem("ccurrencytypeid").getComponent()) {
        afterEditWhenHeadCurrency(e);
      }
      else if (e.getSource() == getBillCardPanel().getHeadItem("ntaxrate").getComponent()) {
        if (getBillCardPanel().getHeadItem("ntaxrate") != null) {
          getBillCardPanel().setHeadItem("ntaxrate", getBillCardPanel().getHeadItem("ntaxrate").getValue());
        }
      }
      else if (e.getSource() == getBillCardPanel().getHeadItem("darrivedate").getComponent()) {
        afterEditInvBillDArriveDate(e);
      }
      else if (e.getSource() == getBillCardPanel().getHeadItem("cstoreorganization").getComponent()) {
        PuTool.afterEditStoreOrgToWarehouse(getBillCardPanel(), e, getPk_corp(), getBillCardPanel().getHeadItem(
            "cstoreorganization").getValue(), "cwarehousename", new String[] {
          "cwarehouseid"
        });

        // 库存组织变后，重置所有行的计划价
        int size = getBillCardPanel().getRowCount();
        String[] sMangIds = new String[size];

        // 库存组织ID
        String sCstoreorganization = getBillCardPanel().getHeadItem("cstoreorganization").getValue();
        for (int i = 0; i < size; i++) {
          sMangIds[i] = (String) getBillCardPanel().getBillModel().getValueAt(i, "cmangid");
        }
        UFDouble[] uPrice = queryPlanPrices(sMangIds, sCstoreorganization);
        if (uPrice != null) {
          for (int i = 0; i < size; i++)
            getBillCardPanel().getBillModel().setValueAt(uPrice[i], i, "nplanprice");
        }
      }
      else if (e.getSource() == getBillCardPanel().getHeadItem("nexchangeotobrate").getComponent()) {
        // 计算本币数据
        try {
          int size = getBillCardPanel().getRowCount();
          for (int i = 0; i < size; i++) {
            computeValueFrmOriginal(i);
          }
        }
        catch (Exception ex) {
          reportException(ex);
          PuTool.outException(this, ex);
          int size = getBillCardPanel().getRowCount();
          for (int i = 0; i < size; i++) {
            getBillCardPanel().setBodyValueAt(null, i, "nmoney");
            getBillCardPanel().setBodyValueAt(null, i, "ntaxmny");
            getBillCardPanel().setBodyValueAt(null, i, "nsummny");
          }
          return;
        }

      }
      // 发票类型修改后,控制不允许自制虚拟发票
      else if ("iinvoicetype".equals(e.getKey())){
        afterEditInvoicetype(e);
      }

      // 表体
      else if (e.getPos() == 1) {
        if (e.getKey().equals("cprojectname")) {
          afterEditInvBillProject(e);
        }
        else if (e.getKey().equals("idiscounttaxtype")
            || e.getKey().equals("ninvoicenum")
            || e.getKey().equals("noriginalcurprice") // e.getKey().equals("noriginalcurpriceinctax")
            || e.getKey().equals("norgnettaxprice") || e.getKey().equals("ntaxrate")
            || e.getKey().equals("noriginalcurmny") || e.getKey().equals("noriginaltaxmny")
            || e.getKey().equals("noriginalsummny") || e.getKey().equals("ndiscountrate")
            || e.getKey().equals("npricediscountrate") || e.getKey().equals("nexchangeotobrate")) {
          // 计算本币数据
          try {
            if (e.getKey().equals("ninvoicenum")) {
              aftereditWhenBodyInvoicenum(this, getBillCardPanel(), e);
            }
            afterEditInvBillRelations(e);
            computeValueFrmOriginal(e.getRow());
          }
          catch (Exception ex) {
            reportException(ex);
            PuTool.outException(this, ex);
            getBillCardPanel().setBodyValueAt(null, e.getRow(), "nmoney");
            getBillCardPanel().setBodyValueAt(null, e.getRow(), "ntaxmny");
            getBillCardPanel().setBodyValueAt(null, e.getRow(), "nsummny");
            return;
          }
        }
        else if (e.getKey().equals("vfree0")) {
          afterEditInvBillFree(e);
        }
        else if (e.getKey().equals("vmemo")) {
          afterEditInvBillBodyMemo(e);
        }
        else if (e.getKey().equals("invcode")) {
          afterEditInvBillInvcode(e);
        }
        // 发票支持多币种
        else if (e.getKey().equals("currname")) {
          // 币种
          isEditCur = 1;
          afterEditWhenBodyCurrency(e);
          isEditCur = 0;
        }
        // 辅计量 森工NC
        else if (e.getKey().trim().equals("cassistunitname")) {
          afterEditWhenBodyAssist(this, getBillCardPanel(), e);
        }
        // 换算率 森工NC
        else if (e.getKey().trim().equals("nexchangerate")) {
          afterEditWhenBodyRate(this, getBillCardPanel(), e);
        }
        // 辅数量 森工NC
        else if (e.getKey().trim().equals("nassistnum")) {
          afterEditWhenBodyAssNum(this, getBillCardPanel(), e);
          
        }


        // 处理自定义项PK
        setBodyDefPK(e);
        return;
        
      }

      getBillCardPanel().execHeadEditFormulas();

    }
    if (e.getKey().equals("crowno")) {
      BillRowNo.afterEditWhenRowNo(getBillCardPanel(), e, ScmConst.PO_Invoice);
    }
    if (e.getKey().equals("vinvoicecode")) {
      // 清单据号空格
      if (PuPubVO.getString_TrimZeroLenAsNull(getBillCardPanel().getHeadItem("vinvoicecode").getValue()) != null) {
        getBillCardPanel().getHeadItem("vinvoicecode").setValue(
            getBillCardPanel().getHeadItem("vinvoicecode").getValue().toString().trim());
      }

    }
    
    /**
     * 二次开发插件支持 by zhaoyha at 2009.2.16
     */
    getInvokeEventProxy().afterEdit(e);
    
  }

  /**
   * 作者：王印芬 功能：表头开户银行变化后，银行帐号相应变化 参数：BillEditEvent e 捕捉到的BillEditEvent事件 返回：无
   * 例外：无 日期：(2002-3-13 11:39:21) 修改日期，修改人，修改原因，注释标志： 2002-03-20 wyf
   * 提高效率，在该函数中即完成分支 wyf add/modify/delete 2002-03-21 begin/end
   */
  private void afterEditInvBillBank(BillEditEvent e) {
    UIRefPane pane = (UIRefPane) getBillCardPanel().getHeadItem("caccountbankid").getComponent();
    getBillCardPanel().setHeadItem("cvendoraccount", pane.getRefCode());

  }

  /**
   * 作者：王印芬 功能：表体备注变化完成后，保证如果是参照结果，保留参照；如果是手工输入，保存手工输入 参数：BillEditEvent e
   * 捕捉到的BillEditEvent事件 返回：无 例外：无 日期：(2002-3-13 11:39:21) 修改日期，修改人，修改原因，注释标志：
   * 2002-03-20 wyf 提高效率，在该函数中即完成分支 wyf add/modify/delete 2002-03-21 begin/end
   */
  private void afterEditInvBillBodyMemo(BillEditEvent e) {

    String str = ((UIRefPane) getBillCardPanel().getBodyItem("vmemo").getComponent()).getRefName();
    if (str != null && !str.trim().equals("")) {
      getBillCardPanel().setBodyValueAt(str, e.getRow(), "vmemo");
    }
    else {
      getBillCardPanel().setBodyValueAt(((UIRefPane) getBillCardPanel().getBodyItem("vmemo").getComponent()).getText(),
          e.getRow(), "vmemo");
    }
  }

  /**
   * 作者：李亮 功能：修改表头币种后动作 参数：BillEditEvent e 捕捉到的BillEditEvent事件 返回：无 例外：无
   * 日期：(2002-3-13 11:39:21) 修改日期，修改人，修改原因，注释标志： 2001-12-11 ljq
   * 根据不同的币种，设置表头、表体折辅汇率、折本汇率的精度 2002-06-07 wyf 修改行状态，以便保存时更新 2002-11-11 WYF
   * 加入对最高限价的处理
   */
  private void afterEditWhenHeadCurrency(BillEditEvent e) {

    // 关闭合计开关
    boolean bOldNeedCalc = getBillCardPanel().getBillModel().isNeedCalculate();
    getBillCardPanel().getBillModel().setNeedCalculate(false);

    try {
      String sCurrId = getBillCardPanel().getHeadItem("ccurrencytypeid").getValue();
      String dInvoiceDate = getBillCardPanel().getHeadItem("dinvoicedate").getValue();
      //add by QuSida (佛山骏杰) 2010-9-24  防止发票日期为空时，setExchangeRateHead(dInvoiceDate, sCurrId)方法报错
      if(dInvoiceDate == null||dInvoiceDate.equals("")){
    	  dInvoiceDate = ClientEnvironment.getInstance().getDate().toString();
      }

      // =================表头
      // 设置折本及折辅汇率
      setExchangeRateHead(dInvoiceDate, sCurrId);
      // 金额等重新计算
      setCurrMoneyDigitAndReCalToBill(sCurrId);
      // =================表体
      if (sCurrId == null || sCurrId.trim().equals("") || getBillCardPanel().getBillModel().getRowCount() == 0) {
     // 打开合计开关
        getBillCardPanel().getBillModel().setNeedCalculate(bOldNeedCalc);
        return;
      }
      ArrayList listAllCurrId = new ArrayList();
      listAllCurrId.add(sCurrId);
      BusinessCurrencyRateUtil bca = new BusinessCurrencyRateUtil(getPk_corp());
      // 取得主辅币PK
      if (CurrParamQuery.getInstance().getLocalCurrPK(getPk_corp()) != null
          && !listAllCurrId.contains(CurrParamQuery.getInstance().getLocalCurrPK(getPk_corp()))) {
        listAllCurrId.add(CurrParamQuery.getInstance().getLocalCurrPK(getPk_corp()));
      }

      // 改变表体没有币种的行
      String sBodyCurrId = null;
      int iLen = getBillCardPanel().getRowCount();
      for (int i = 0; i < iLen; i++) {
        sBodyCurrId = (String) getBillCardPanel().getBillModel().getValueAt(i, "ccurrencytypeid");
        if (sBodyCurrId == null || sBodyCurrId.equals("")) {
          getBillCardPanel().getBillModel().setValueAt(sCurrId, i, "ccurrencytypeid");
          // 获取币种名称
          // execBodyFormula(i, "ccurrencytype");//优化 V31
          // 驱动表体相关币种的改动
          afterEditWhenBodyCurrency(new BillEditEvent(getBillCardPanel().getBodyItem("ccurrencytypeid").getComponent(),
              sCurrId, "ccurrencytypeid", i));
          // 设置修改标志
          getBillCardPanel().getBillModel().setRowState(i, BillModel.MODIFICATION);
        }
      }
      getBillCardPanel().getBillModel().execEditFormulaByKey(-1, "currname");// 优化
                                                                              // V31

      // 计算本币数据
      try {
        int size = getBillCardPanel().getRowCount();
        for (int i = 0; i < size; i++) {
          computeValueFrmOriginal(i);
        }
      }
      catch (Exception ex) {
        reportException(ex);
        PuTool.outException(this, ex);
        int size = getBillCardPanel().getRowCount();
        for (int i = 0; i < size; i++) {
          getBillCardPanel().setBodyValueAt(null, i, "nmoney");
          getBillCardPanel().setBodyValueAt(null, i, "ntaxmny");
          getBillCardPanel().setBodyValueAt(null, i, "nsummny");
        }
      }
    }
    catch (Exception exp) {
      PuTool.outException(this, exp);
      // 打开合计开关
      getBillCardPanel().getBillModel().setNeedCalculate(bOldNeedCalc);
      return;
    }
    // 打开合计开关
    getBillCardPanel().getBillModel().setNeedCalculate(bOldNeedCalc);
  }

  /**
   * 作者：王印芬 功能：设置表头币种及汇率的可编辑性 参数：无 返回：无 例外：无 日期：(2002-5-13 11:39:21)
   * 修改日期，修改人，修改原因，注释标志：
   */
  private void setExchangeRateHead(String dInvoicedate, String sCurrId) {

    sCurrId = (sCurrId == null || sCurrId.trim().length() == 0) ? null : sCurrId;
    // 首先设置显示精度
    int[] iaExchRateDigit = m_cardPoPubSetUI2.getBothExchRateDigit(getPk_corp(), sCurrId);
    getBillCardPanel().getHeadItem("nexchangeotobrate").setDecimalDigits(iaExchRateDigit[0]);
    // 设置值
    UFDouble[] daRate = m_cardPoPubSetUI2.getBothExchRateValue(getPk_corp(), sCurrId, new UFDate(dInvoicedate));
    UFDouble temp = daRate[0] == null?UFDouble.ONE_DBL:daRate[0];
    getBillCardPanel().getHeadItem("nexchangeotobrate").setValue(temp);
    // 可编辑性
    boolean[] iaEditable = m_cardPoPubSetUI2.getBothExchRateEditable(getPk_corp(), sCurrId);
    getBillCardPanel().getHeadItem("nexchangeotobrate").setEnabled(iaEditable[0]);

  }

  /**
   * 作者：王印芬 功能：表头票到日期变化后，汇率相应变化。 参数：BillEditEvent e 捕捉到的BillEditEvent事件 返回：无
   * 例外：无 日期：(2002-3-13 11:39:21) 修改日期，修改人，修改原因，注释标志： 2002-03-20 wyf
   * 提高效率，在该函数中即完成分支 wyf add/modify/delete 2002-03-21 begin/end
   */
  private void afterEditInvBillDArriveDate(BillEditEvent e) {
    String strCurr = getBillCardPanel().getHeadItem("ccurrencytypeid").getValue();
    setCurrRateToBillHead(strCurr, null);

  }

  /**
   * 作者：王印芬 功能：表头部门变化后，业务员相应变化 参数：BillEditEvent e 捕捉到的BillEditEvent事件 返回：无 例外：无
   * 日期：(2002-3-13 11:39:21) 修改日期，修改人，修改原因，注释标志： 2002-03-20 wyf 提高效率，在该函数中即完成分支
   * wyf add/modify/delete 2002-03-21 begin/end
   */
  private void afterEditInvBillDept(BillEditEvent e) {

    UIRefPane pane = (UIRefPane) getBillCardPanel().getHeadItem("cdeptid").getComponent();
    if (pane.getRefPK() == null || pane.getRefPK().trim().length() < 1)
      return;
  }

  /**
   * 作者：王印芬 功能：表头业务员变化后，部门相应变化 参数：BillEditEvent e 捕捉到的BillEditEvent事件 返回：无 例外：无
   * 日期：(2002-3-13 11:39:21) 修改日期，修改人，修改原因，注释标志： 2002-03-20 wyf 提高效率，在该函数中即完成分支
   * wyf add/modify/delete 2002-03-21 begin/end
   */
  private void afterEditInvBillEmployee(BillEditEvent e) {

    // 设置该部门的默认业务员, 该部门对应的业务员动态参照
    String cemployeeid = ((UIRefPane) getBillCardPanel().getHeadItem("cemployeeid").getComponent()).getRefPK();
    if (cemployeeid == null || cemployeeid.trim().length() < 1)
      return;

    String newDeptid = (String) PiPqPublicUIClass.getAResultFromTable("bd_psndoc", "pk_deptdoc", "pk_psndoc",
        cemployeeid);

    UIRefPane pane = (UIRefPane) getBillCardPanel().getHeadItem("cdeptid").getComponent();
    pane.setPK(newDeptid);
  }

  /**
   * 作者：王印芬 功能：表体自由项变化后，五个自由项的值相应改变 参数：BillEditEvent e 捕捉到的BillEditEvent事件 返回：无
   * 例外：无 日期：(2002-3-13 11:39:21) 修改日期，修改人，修改原因，注释标志： 2002-03-20 wyf
   * 提高效率，在该函数中即完成分支 wyf add/modify/delete 2002-03-21 begin/end
   */
  private void afterEditInvBillFree(BillEditEvent e) {

    FreeVO freeVO = ((FreeItemRefPane) getBillCardPanel().getBodyItem("vfree0").getComponent()).getFreeVO();
    if (freeVO == null) {
      for (int i = 0; i < 5; i++) {
        String str = "vfree" + new Integer(i + 1).toString();
        getBillCardPanel().setBodyValueAt(null, e.getRow(), str);
      }
    }
    else {
      // 发票自由项由1到5 //getVfreeid7
      for (int i = 0; i < 5; i++) {
        // vfreename2
        String strName = "vfreename" + new Integer(i + 1).toString();
        if (freeVO.getAttributeValue(strName) != null) {
          String str = "vfree" + new Integer(i + 1).toString();
          Object ob = freeVO.getAttributeValue(str);
          getBillCardPanel().setBodyValueAt(ob, e.getRow(), str);
        }
      }
    }

    // 以免当前值被代入下一个自由项参照
    InvVO invVO = new InvVO();
    ((FreeItemRefPane) getBillCardPanel().getBodyItem("vfree0").getComponent()).setFreeItemParam(invVO);
  }

  /**
   * 作者：王印芬 功能：表头散户变化后，相应电话等变化 参数：BillEditEvent e 捕捉到的BillEditEvent事件 返回：无 例外：无
   * 日期：(2002-3-13 11:39:21) 修改日期，修改人，修改原因，注释标志： 2002-03-20 wyf 提高效率，在该函数中即完成分支
   * wyf add/modify/delete 2002-03-21 begin/end
   */
  private void afterEditInvBillFreeCust(BillEditEvent e) {

    String freeId = getBillCardPanel().getHeadItem("cfreecustid").getValue();
    setDefaultInfoForAFreeCust(freeId);
  }

  /**
   * 作者：王印芬 功能：表体存货变化后，自由项及税率相应改变 参数：BillEditEvent e 捕捉到的BillEditEvent事件 返回：无
   * 例外：无 日期：(2002-3-13 11:39:21) 修改日期，修改人，修改原因，注释标志： 2002-03-20 wyf
   * 提高效率，在该函数中即完成分支 wyf add/modify/delete 2002-03-21 begin/end 2002-08-26 wyf
   * 税率改变后触发值重新计算事件 2002-09-18 wyf 存货修改后，批次号清空，并控制可编辑性
   */
  private void afterEditInvBillInvcode(BillEditEvent e) {

    UIRefPane refpane = (UIRefPane) getBillCardPanel().getBodyItem("invcode").getComponent();
    Object[] saMangId = ((Object[]) refpane.getRefValues("bd_invmandoc.pk_invmandoc"));

    int nRow = getBillCardPanel().getRowCount();
    InvoiceVO VO = new InvoiceVO(nRow);
    getBillCardPanel().getBillValueVO(VO);

    // 为多选作准备
    int iInsertLen = (saMangId == null ? 0 : saMangId.length - 1);
    int iBeginRow = e.getRow();
    if (iBeginRow == getBillCardPanel().getRowCount() - 1) {
      // 选中的行已是最后一行则增行
      for (int i = 0; i < iInsertLen; i++) {
        onActionAppendLine();
      }
    }
    else {
      // 插行
      onActionInsertLines(iBeginRow, iBeginRow + 1, iInsertLen);
    }
    int iEndRow = iBeginRow + iInsertLen;

    // 自由项
    // 取表头币种
    String sCurrId = getBillCardPanel().getHeadItem("ccurrencytypeid").getValue();
    String sBodyCurrId = null;
    for (int i = iBeginRow; i <= iEndRow; i++) {
      getBillCardPanel().setBodyValueAt(null, i, "vfree0");
      getBillCardPanel().setBodyValueAt(null, i, "vfree1");
      getBillCardPanel().setBodyValueAt(null, i, "vfree2");
      getBillCardPanel().setBodyValueAt(null, i, "vfree3");
      getBillCardPanel().setBodyValueAt(null, i, "vfree4");
      getBillCardPanel().setBodyValueAt(null, i, "vfree5");
      // 新增表体行币种默认为表头币种
      sBodyCurrId = (String) getBillCardPanel().getBillModel().getValueAt(i, "ccurrencytypeid");
      if (sBodyCurrId == null || sBodyCurrId.equals("")) {
        getBillCardPanel().getBillModel().setValueAt(sCurrId, i, "ccurrencytypeid");
        // 获取币种名称
        // 驱动表体相关币种的改动
        afterEditWhenBodyCurrency(new BillEditEvent(getBillCardPanel().getBodyItem("ccurrencytypeid").getComponent(),
            sCurrId, "ccurrencytypeid", i));
        // 设置修改标志
        getBillCardPanel().getBillModel().setRowState(i, BillModel.MODIFICATION);
        getBillCardPanel().getBillModel().getValueAt(i, "ccurrencytypeid");
      }
      // 设置换算率
      setExchangeRateBody(i, true, null);

    }
    // 执行表体公式（批量）
    setInvEditFormulaInfo(getBillCardPanel(), refpane, iBeginRow, iEndRow);
    getBillCardPanel().getBillModel().execEditFormulaByKey(-1, "currname");// 优化
                                                                            // V31
    // 带出存货的默认税率
    setRelated_Taxrate(iBeginRow, iEndRow);

    for (int i = iBeginRow; i <= iEndRow; i++) {
      // 批次号的可编辑性
      String sMangId = PuPubVO.getString_TrimZeroLenAsNull(getBillCardPanel().getBodyValueAt(i, "cmangid"));
      if (sMangId == null || sMangId.trim().length() == 0) {
        getBillCardPanel().getBillModel().setCellEditable(i, "vproducenum", false);
      }
      else {
        getBillCardPanel().getBillModel().setCellEditable(i, "vproducenum", PuTool.isBatchManaged(sMangId));
      }
      // 批次号清空
      getBillCardPanel().setBodyValueAt(null, i, "vproducenum");
    }

    // 获取计划价格
    String sCstoreorganization = getBillCardPanel().getHeadItem("cstoreorganization").getValue();
    int size = saMangId == null ? 0 : saMangId.length;
    String[] sMangIds = new String[size];
    for (int i = 0; i < size; i++) {
      sMangIds[i] = saMangId[i].toString();
    }
    UFDouble[] uPrice = queryPlanPrices(sMangIds, sCstoreorganization);
    if (uPrice != null) {
      for (int i = iBeginRow; i <= iEndRow; i++) {
        getBillCardPanel().getBillModel().setValueAt(uPrice[i - iBeginRow], i, "nplanprice");
      }
    }
    BillItem it = getBillCardPanel().getBodyItem(e.getKey());
    if (it.getEditFormulas() != null && it.getEditFormulas().length > 0) {
      getBillCardPanel().getBillModel().execFormulas(it.getEditFormulas(), iBeginRow, iEndRow);
    }
    //For V56 缓存费用存货信息
    PuTool.loadFeeInventoryBatch(new InvoiceVO[]{getCurVOonCard()}, "cbaseid");
    getBillCardPanel().setDefaultPrice(iBeginRow, iEndRow, null);
    // 辅计量
    setRelated_AssistUnit(iBeginRow, iEndRow);
  }

  /**
   * 作者：王印芬 功能：表体项目变化后，项目阶段ID清空 参数：BillEditEvent e 捕捉到的BillEditEvent事件 返回：无 例外：无
   * 日期：(2002-3-13 11:39:21) 修改日期，修改人，修改原因，注释标志： 2002-03-20 wyf 提高效率，在该函数中即完成分支
   * wyf add/modify/delete 2002-03-21 begin/end
   */
  private void afterEditInvBillProject(BillEditEvent e) {
    int n = e.getRow();
    if (n < 0)
      return;
    // 判断项目是否为空。若为空，则项目阶段不可编辑
    Object oTemp = getBillCardPanel().getBodyValueAt(n, "cprojectname");
    if (oTemp == null || oTemp.toString().length() == 0) {
      getBillCardPanel().getBillModel().setCellEditable(n, "cprojectphasename", false);
      getBillCardPanel().setBodyValueAt(null, n, "cprojectphaseid");
      getBillCardPanel().setBodyValueAt(null, n, "cprojectphasename");
    }
    else {
      getBillCardPanel().getBillModel().setCellEditable(n, "cprojectphasename", true);
    }
  }

  /**
   * 作者：王印芬 功能：表体任一项数值变化后，其他项相应改变 参数：BillEditEvent e 捕捉到的BillEditEvent事件 返回：无
   * 例外：无 日期：(2002-3-13 11:39:21) 修改日期，修改人，修改原因，注释标志： 2002-03-20 wyf
   * 提高效率afterEdit()中完成分支 wyf add/modify/delete 2002-03-21 begin/end 2002-08-26
   * wyf 扣税类别未选中时不进行计算 wyf add/modify/delete 2002-03-21 begin/end
   */
  public void afterEditInvBillRelations(BillEditEvent e) {
    int[] descriptions = new int[] {
        RelationsCal.DISCOUNT_TAX_TYPE_NAME, RelationsCal.DISCOUNT_TAX_TYPE_KEY, RelationsCal.NUM,
        RelationsCal.NET_PRICE_ORIGINAL, RelationsCal.NET_TAXPRICE_ORIGINAL, RelationsCal.TAXRATE,
        RelationsCal.MONEY_ORIGINAL, RelationsCal.TAX_ORIGINAL, RelationsCal.SUMMNY_ORIGINAL
    };
    // 扣税类别 0应税内含 1应税外加 2不计税
    String s=(String) getBillCardPanel().getBodyValueAt(e.getRow(), "idiscounttaxtype");
    if(StringUtil.isEmptyWithTrim(s)) s="应税内含";
    
    String[] keys = new String[] {
        s, "idiscounttaxtype", "ninvoicenum", "noriginalcurprice", "norgnettaxprice", "ntaxrate", "noriginalcurmny",
        "noriginaltaxmny", "noriginalsummny"
    };
    // fangy*********************************
    RelationsCal.calculate(getBillCardPanel(), e, getBillCardPanel().getBillModel(),new int[] {
        PuTool.getPricePriorPolicy(this.getPk_corp()), iChange
    }, descriptions, keys, InvoiceItemVO.class.getName());
    // fangy*********************************
  }

  /**
   * 作者：王印芬 功能：表头供应商变化后，相应的其他项变化 参数：BillEditEvent e 捕捉到的BillEditEvent事件 返回：无
   * 例外：无 日期：(2002-3-13 11:39:21) 修改日期，修改人，修改原因，注释标志： 2002-03-20 wyf
   * 提高效率，在该函数中即完成分支 wyf add/modify/delete 2002-03-21 begin/end
   */
  private void afterEditInvBillVendor(BillEditEvent e) {

    // added by fangy 2002-10-23 12:22 begin
    // 处理清空的情况
    if (e.getValue() == null || e.getValue().equals("")) {
      // 币种
      getBillCardPanel().setHeadItem("ccurrencytypeid", null);
      // 收付款协议
      getBillCardPanel().setHeadItem("ctermprotocolid", null);
      // 银行和帐户
      getBillCardPanel().getHeadItem("caccountbankid").setValue(null);
      getBillCardPanel().getHeadItem("caccountbankid").setEnabled(false);
      getBillCardPanel().setHeadItem("cvendoraccount", null);
      getBillCardPanel().setHeadItem("cvendorphone", null);
      // 部门和业务员
      // getInvBillPanel().setHeadItem("cdeptid",null) ;
      // getInvBillPanel().setHeadItem("cemployeeid",null) ;
    }
    // added by fangy 2002-10-23 12:22 end
    else {
      // 供应商
      String cvendormangid = getBillCardPanel().getHeadItem("cvendormangid").getValue();
      // String cvendorbaseid =
      // getResultFromFormula("getColValue(bd_cumandoc,pk_cubasdoc,pk_cumandoc,cvendormangid)","cvendormangid",cvendormangid)
      // ;
      String cvendorbaseid = (String) PiPqPublicUIClass.getAResultFromTable("bd_cumandoc", "pk_cubasdoc",
          "pk_cumandoc", cvendormangid);
      getBillCardPanel().setHeadItem("cvendorbaseid", cvendorbaseid);

      // 默认交易币种:取默认交易币种,如果没有,则不变
      // String strCurr =
      // getResultFromFormula("getColValue(bd_cumandoc,pk_currtype1,pk_cumandoc,cvendormangid)","cvendormangid",cvendormangid)
      // ;
      String strCurr = (String) PiPqPublicUIClass.getAResultFromTable("bd_cumandoc", "pk_currtype1", "pk_cumandoc",
          cvendormangid);
      if (strCurr != null && strCurr.trim().length() > 1) {
        getBillCardPanel().setHeadItem("ccurrencytypeid", strCurr);
        setCurrRateToBillHead(strCurr, null);
        setCurrMoneyDigitAndReCalToBill(strCurr);

        afterEditWhenHeadCurrency(e);
      }
      // 收付款协议
      String payTermId = getBillCardPanel().getHeadItem("ctermprotocolid").getValue();
      if (payTermId == null || payTermId.trim().length() < 1) {
        // payTermId =
        // getResultFromFormula("getColValue(bd_cumandoc,pk_payterm,pk_cumandoc,cvendormangid)","cvendormangid",cvendormangid)
        // ;
        payTermId = (String) PiPqPublicUIClass.getAResultFromTable("bd_cumandoc", "pk_payterm", "pk_cumandoc",
            cvendormangid);
        getBillCardPanel().setHeadItem("ctermprotocolid", payTermId);
      }
      // 收付款单位
      String sPayUnit = getBillCardPanel().getHeadItem("cpayunit").getValue();
      // 丰原生化项目问题：修改发票上的供应商为B单位，保存签字后，生成应付单上的供应商还是是A单位
      // if (sPayUnit == null || sPayUnit.trim().length() == 0) {
      // sPayUnit = (String)
      // PiPqPublicUIClass.getAResultFromTable("bd_cumandoc", "pk_payterm",
      // "pk_cumandoc", cvendormangid);
      getBillCardPanel().setHeadItem("cpayunit", cvendormangid);
      // }

      // ====是否散户, 电话,开户银行,帐号
      // String strFreeFlag =
      // getResultFromFormula("getColValue(bd_cubasdoc,freecustflag,pk_cubasdoc,cvendorbaseid)","cvendorbaseid",cvendorbaseid)
      // ;
      String strFreeFlag = (String) PiPqPublicUIClass.getAResultFromTable("bd_cubasdoc", "freecustflag", "pk_cubasdoc",
          cvendorbaseid);
      if (strFreeFlag.equals("N")) {
        // 散户置灰,清空相应信息
        getBillCardPanel().getHeadItem("cfreecustid").setEnabled(false);
        getBillCardPanel().getHeadItem("cfreecustid").setValue(null);
        ((UIRefPane) getBillCardPanel().getHeadItem("cfreecustid").getComponent()).getUITextField().setText(null);
        ((UIRefPane) getBillCardPanel().getHeadItem("caccountbankid").getComponent()).setButtonVisible(true);
        getBillCardPanel().getHeadItem("caccountbankid").setEnabled(true);

        // 该供应商对应的开户银行动态参照及默认开户银行
        setDefaultBankAccountForAVendor(cvendorbaseid);
        // 供应商电话
        setDefaultPhoneForAVendor(cvendorbaseid);
      }
      else {
        // 散户可用
        getBillCardPanel().getHeadItem("cfreecustid").setEnabled(true);
        // 开户银行不可编辑,按钮不可见
        ((UIRefPane) getBillCardPanel().getHeadItem("caccountbankid").getComponent()).setButtonVisible(false);
        getBillCardPanel().getHeadItem("caccountbankid").setEnabled(false);

        // 影响的信息
        getBillCardPanel().setHeadItem("cvendorphone", null);
        getBillCardPanel().setHeadItem("caccountbankid", null);
        ((UIRefPane) getBillCardPanel().getHeadItem("caccountbankid").getComponent()).getUITextField().setText(null);
        getBillCardPanel().setHeadItem("cvendoraccount", null);
      }
      // 可能会影响到部门及业务员ID
      if (getBillCardPanel().getHeadItem("cdeptid").getValue() == null) {
        // String deptID =
        // getResultFromFormula("getColValue(bd_cumandoc,pk_respdept1,pk_cumandoc,cvendormangid)","cvendormangid",cvendormangid)
        // ;
        String deptID = (String) PiPqPublicUIClass.getAResultFromTable("bd_cumandoc", "pk_respdept1", "pk_cumandoc",
            cvendormangid);
        getBillCardPanel().setHeadItem("cdeptid", deptID);
      }
      // 默认业务员
      if (getBillCardPanel().getHeadItem("cemployeeid").getValue() == null) {
        // String employeeID =
        // getResultFromFormula("getColValue(bd_cumandoc,pk_resppsn1,pk_cumandoc,cvendormangid)","cvendormangid",cvendormangid)
        // ;
        String employeeID = (String) PiPqPublicUIClass.getAResultFromTable("bd_cumandoc", "pk_resppsn1", "pk_cumandoc",
            cvendormangid);
        getBillCardPanel().setHeadItem("cemployeeid", employeeID);
        UIRefPane ref = (UIRefPane) (getBillCardPanel().getHeadItem("cemployeeid").getComponent());
        ref.setPK(employeeID);
      }
    }
  }

  /**
   * 作者：王印芬 功能：编辑前处理。 参数：BillEditEvent e 捕捉到的BillEditEvent事件 返回：无 例外：true
   * 日期：(2002-3-13 11:39:21) 修改日期，修改人，修改原因，注释标志： 2002-09-17 wyf 加入对批次号的处理
   */
  public boolean beforeEdit(BillEditEvent e) {
    /**
     * 二次开发插件支持 by zhaoyha at 2009.2.16
     */
    boolean pluginRet=getInvokeEventProxy().beforeEdit(e);

    if (getCurPanelMode() == INV_PANEL_CARD) {
      if (e.getPos() == BillItem.BODY) {
        // 自由项
        if (e.getKey().equals("vfree0")) {
          return PuTool.beforeEditInvBillBodyFree(getBillCardPanel(), e, new String[] {
              "cmangid", "invcode", "invname", "invspec", "invtype"
          }, new String[] {
              "vfree0", "vfree1", "vfree2", "vfree3", "vfree4", "vfree5"
          });
        }
        // 备注
        else if (e.getKey().equals("vmemo")) {
          return beforeEditInvBillBodyMemo(e);
        }
        // 折本汇率
        else if (e.getKey().equals("nexchangeotobrate")) {
          // 设置换算率
          setExchangeRateBody(e.getRow(), true, null);
        }
        // 设置原/本/辅币金额精度
        else if (e.getKey().equals("noriginalcurmny") || e.getKey().equals("noriginaltaxmny")
            || e.getKey().equals("noriginalsummny") || e.getKey().equals("nmoney") || e.getKey().equals("ntaxmny")
            || e.getKey().equals("nsummny")) {
          // setRowDigits_Mny(getPk_corp(),
          // e.getRow(),getInvBillPanel().getBillModel(),m_cardPoPubSetUI2);
          try {
            computeValueFrmOriginal(e.getRow());
          }
          catch (Exception ex) {
            SCMEnv.out(e);
          }
        }
        // 批次号
        else if (e.getKey().equals("vproducenum")) {
          PuTool.beforeEditWhenBodyBatch(getBillCardPanel(), getPk_corp(), e, new String[] {
              // 存货管理ID、编码、名称、规格、型号、单位
              "cmangid", "invcode", "invname", "invspec", "invtype", "measname",
              // 辅计量单位、是否辅计量管理
              // "cassistunit", "assistunitflag",
              null, null,
              // 仓库ID
              "cwarehouseid"
          }, "vfree");
        }
        // 仓库,从采购入库单生成的发票仓库不能更改
        else if (e.getKey().equals("cwarehousename")) {
          Object oCupsourceBillType = getBillCardPanel().getBodyValueAt(e.getRow(), "cupsourcebilltype");
          if (oCupsourceBillType != null && oCupsourceBillType.toString().trim().length() > 0) {
            if (oCupsourceBillType.toString().equals("45")) {
              showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPP40040401-000009")/*
                                                                                                             * @res
                                                                                                             * "从采购入库单生成的发票仓库不能更改"
                                                                                                             */);
              getBillCardPanel().getBodyItem("cwarehousename").setEnabled(false);
            }
          }
          else
            getBillCardPanel().getBodyItem("cwarehousename").setEnabled(true);
        }
        // 存货
        else if (e.getKey().equals("invcode")) {

        }
        // 项目阶段
        else if (e.getKey().equals("cprojectphasename")) {
          Object oTemp = getBillCardPanel().getBodyValueAt(e.getRow(), "cprojectid");
          if (oTemp == null || oTemp.toString().trim().length() == 0) {
            getBillCardPanel().setCellEditable(e.getRow(), "cprojectphasename", false);
          }
          else {
            getBillCardPanel().setCellEditable(e.getRow(), "cprojectphasename", true);
            UIRefPane refpane = (UIRefPane) getBillCardPanel().getBodyItem("cprojectphasename").getComponent();
            String s = refpane.getRefModel().getWherePart();
            s += " and bd_jobobjpha.pk_jobmngfil = '" + oTemp.toString() + "' ";
            refpane.setWhereString(s);
          }
        }
        // 数量、金额、换算率、辅数量、辅计量 森工NC
        else if ("ninvoicenum".equals(e.getKey()) || "nmoney".equals(e.getKey()) || "nexchangerate".equals(e.getKey())
            || "nassistnum".equals(e.getKey()) || "cassistunitname".equals(e.getKey())) {
          beforeEditBodyAssistUnitNumber(getBillCardPanel(), e.getRow());          
        }
        // 采购发票参照消耗汇总单开票后无币种录入单价提示错
        else if (e.getKey().equals("noriginalcurprice")) {
          if (PuPubVO.getString_TrimZeroLenAsNull(getBillCardPanel().getHeadItem("ccurrencytypeid").getValue()) == null) {
            showHintMessage(getHeadHintText()
                + nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPP40040401-000256")/* @res"请输入币种!" */);
            return false;
          }
        }
        else if (e.getKey().startsWith("vdef")){
            if(getBillCardPanel().getBodyItem(e.getKey()).getComponent() instanceof UIRefPane){
              ((UIRefPane)getBillCardPanel().getBodyItem(e.getKey()).getComponent()).getUITextField().setEditable(true);
            }
         }

      }
    }
    return true;
  }

  /**
   * 作者：汪维敏 功能：表头表尾编辑前处理 参数：BillItemEvent e 捕捉到的BillItemEvent事件 返回：无 例外：无
   * 日期：(2002-7-22 11:39:21) 修改日期，修改人，修改原因，注释标志
   */
  public boolean beforeEdit(BillItemEvent e) {
    //<p>二次开发插件支持 by zhaoyha at 2009.2.16
    boolean pluginRet=getInvokeEventProxy().beforeEdit(e);

    // 表头

    // 限制为仓储属性库存组织
    if (e.getSource().equals(getBillCardPanel().getHeadItem("cstoreorganization"))) {
      PuTool.restrictStoreOrg(getBillCardPanel().getHeadItem("cstoreorganization").getComponent(), false);
    }

    // 用供应商限制开户银行
    if(e.getSource().equals(getBillCardPanel().getHeadItem("caccountbankid"))){
        String cvendormangid = getBillCardPanel().getHeadItem("cvendormangid").getValue();
      String cvendorbaseid = (String) PiPqPublicUIClass.getAResultFromTable("bd_cumandoc", "pk_cubasdoc", "pk_cumandoc", cvendormangid);
      if(cvendorbaseid != null){
        setDefaultBankAccountForAVendor(cvendorbaseid);
      }
    }
    //发票类型
    if("iinvoicetype".equals(e.getItem().getKey())){
      setCurInvoiceType();
    }
    
    return true;
  }

  /**
   * 编辑前处理。 创建日期：(2001-3-23 2:02:27)
   * 
   * @param e
   *          ufbill.BillEditEvent
   */
  private boolean beforeEditInvBillBodyMemo(BillEditEvent e) {

    // 必须调用:停止编辑!!!!!!
    getBillCardPanel().stopEditing();

    Object ob = getBillCardPanel().getBodyValueAt(e.getRow(), "vmemo");
    ((UIRefPane) getBillCardPanel().getBodyItem("vmemo").getComponent()).setText((String) ob);

    return true;
  }

  public void bodyRowChange(BillEditEvent e) {
    /**
     * 二次开发插件支持 by zhaoyha at 2009.2.16
     */
    getInvokeEventProxy().bodyRowChange(e);
  }

  /**
   * 作者：王印芬 功能：计算本币及辅币的金额、税额、价税合计及累计付款 参数：InvoiceVO invVO 待计算的发票VO
   * 返回：计算完毕，返回true； 否则，如果出现如本币未设定无法计算的情况，返回false，以提示用户 例外：无 日期：(2002-3-13
   * 11:39:21) 修改日期，修改人，修改原因，注释标志： 2002-11-01 wyf
   * 修正用户未输入汇率，但默认按库中所存计算，但发票未保存汇率的问题
   */
  private boolean calNativeAndAssistCurrValue(InvoiceVO invVO) {

    // 汇率取票到日期
    UFDouble nmoney, nsummny;
    String pk_corp = invVO.getHeadVO().getPk_corp();
    try {
      if (CurrParamQuery.getInstance().getLocalCurrPK(pk_corp) == null) {
        MessageDialog.showErrorDlg(this,
            nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPPSCMCommon-000059")/*
                                                                                           * @res
                                                                                           * "错误"
                                                                                           */,
            nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPP40040401-000010")/*
                                                                                           * @res
                                                                                           * "未成功指定本币，币种折算错误！"
                                                                                           */);
        return false;
      }

      // wyf 2002-11-01 add begin
      // 检查两汇率是否可为空
      String sHint = PiPqPublicUIClass.checkBothExchRateNull(invVO);
      if (sHint != null) {
        MessageDialog.showErrorDlg(this,
            nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPPSCMCommon-000059")/*
                                                                                           * @res
                                                                                           * "错误"
                                                                                           */, sHint);
        return false;
      }
      // wyf 2002-11-01 add end

      String strRateDate = invVO.getHeadVO().getDarrivedate().toString();
      // 计算
      for (int i = 0; i < invVO.getBodyVO().length; i++) {
        InvoiceItemVO itemVO = invVO.getBodyVO()[i];

        nmoney = POPubSetUI.getCurrArith_Busi(pk_corp).getAmountByOpp(itemVO.getCcurrencytypeid(),
            CurrParamQuery.getInstance().getLocalCurrPK(pk_corp), itemVO.getNoriginalcurmny(),
            itemVO.getNexchangeotobrate(), strRateDate);
        nsummny = POPubSetUI.getCurrArith_Busi(pk_corp).getAmountByOpp(itemVO.getCcurrencytypeid(),
            CurrParamQuery.getInstance().getLocalCurrPK(pk_corp), itemVO.getNoriginalsummny(),
            itemVO.getNexchangeotobrate(), strRateDate);
        // 设值
        invVO.getBodyVO()[i].setNmoney(nmoney);
        invVO.getBodyVO()[i].setNsummny(nsummny);
        if (nsummny != null && nmoney != null) {
          invVO.getBodyVO()[i].setNtaxmny(nsummny.sub(nmoney));
        }
        // 累计付款
        nmoney = POPubSetUI.getCurrArith_Finance(pk_corp).getAmountByOpp(itemVO.getCcurrencytypeid(),
            CurrParamQuery.getInstance().getLocalCurrPK(pk_corp),// POPubSetUI.getCurrArith_Busi(pk_corp).getLocalCurrPK(),
            itemVO.getNoriginalpaymentmny(), itemVO.getNexchangeotobrate(), strRateDate);
        invVO.getBodyVO()[i].setNpaymentmny(nmoney);
      }
    }
    catch (Exception e) {
      SCMEnv.out(e);
      MessageDialog
          .showErrorDlg(this,
              nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPPSCMCommon-000059")/*
                                                                                             * @res
                                                                                             * "错误"
                                                                                             */,
              nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPP40040401-000012")/*
                                                                                             * @res
                                                                                             * "币种折算错误。请检查以下项：\n
                                                                                             * 1、是否系统正常；\n
                                                                                             * 2、是否成功指定本币（辅币）；\n
                                                                                             * 3、是否指定票到日期的汇率。\n提示：可录入汇率。"
                                                                                             */);
      return false;
    }

    return true;

  }

  /**
   * //计算本币金额、本币税额、本币价税合计 创建日期：(2004-3-23 19:16:20)
   */
  public void computeValueFrmOriginal(int row) throws Exception {

    // 折本汇率
    Object bRate = getBillCardPanel().getBodyValueAt(row, "nexchangeotobrate");
    if (bRate == null || bRate.toString().trim().length() == 0) {
      bRate = getBillCardPanel().getHeadItem("nexchangeotobrate").getValue();
      getBillCardPanel().setBodyValueAt(bRate, row, "nexchangeotobrate");
    }
    UFDouble ufBRate = new UFDouble(bRate.toString());

    // 折辅汇率
    // 原币金额
    UFDouble ufNoriginalcurmny = PuPubVO.getUFDouble_ZeroAsNull(getBillCardPanel().getBodyValueAt(row,
        "noriginalcurmny"));
    UFDouble ufMoney = null;
    // 原币税额
    UFDouble ufNoriginaltaxmny = PuPubVO.getUFDouble_ZeroAsNull(getBillCardPanel().getBodyValueAt(row,
        "noriginaltaxmny"));
    UFDouble ufTaxMny = null;
    // 原币价税合计
    UFDouble ufNoriginalsummny = PuPubVO.getUFDouble_ZeroAsNull(getBillCardPanel().getBodyValueAt(row,
        "noriginalsummny"));
    UFDouble ufSumMny = null;

    if (getBillCardPanel().getBodyValueAt(row, "ccurrencytypeid") == null
        || getBillCardPanel().getBodyValueAt(row, "ccurrencytypeid").toString().trim().equals("")) {
      String strCurrTypeId = ((UIRefPane) getBillCardPanel().getHeadItem("ccurrencytypeid").getComponent()).getRefPK();
      getBillCardPanel().setBodyValueAt(strCurrTypeId, row, "ccurrencytypeid");
    }
    try {
      // 原币金额
      if (ufNoriginalcurmny == null || getBillCardPanel().getBodyValueAt(row, "ccurrencytypeid") == null) {
        ufMoney = null;
      }
      else {
        ufMoney = POPubSetUI.getCurrArith_Busi(getPk_corp()).getAmountByOpp(
            getBillCardPanel().getBodyValueAt(row, "ccurrencytypeid").toString(),
            CurrParamQuery.getInstance().getLocalCurrPK(getPk_corp()), ufNoriginalcurmny, ufBRate,
            nc.ui.po.pub.PoPublicUIClass.getLoginDate().toString());
      }
      getBillCardPanel().setBodyValueAt(ufMoney, row, "nmoney");
      // 原币税额
      if (ufNoriginaltaxmny == null || getBillCardPanel().getBodyValueAt(row, "ccurrencytypeid") == null) {
        ufTaxMny = null;
      }
      else {
        ufTaxMny = POPubSetUI.getCurrArith_Busi(getPk_corp()).getAmountByOpp(
            getBillCardPanel().getBodyValueAt(row, "ccurrencytypeid").toString(),
            CurrParamQuery.getInstance().getLocalCurrPK(getPk_corp()), ufNoriginaltaxmny, ufBRate,
            nc.ui.po.pub.PoPublicUIClass.getLoginDate().toString());
      }
      getBillCardPanel().setBodyValueAt(ufTaxMny, row, "ntaxmny");
      // 原币价税合计
      if (ufNoriginalsummny == null || getBillCardPanel().getBodyValueAt(row, "ccurrencytypeid") == null) {
        ufSumMny = null;
      }
      else {
        ufSumMny = POPubSetUI.getCurrArith_Busi(getPk_corp()).getAmountByOpp(
            getBillCardPanel().getBodyValueAt(row, "ccurrencytypeid").toString(),
            CurrParamQuery.getInstance().getLocalCurrPK(getPk_corp()), ufNoriginalsummny, ufBRate,
            nc.ui.po.pub.PoPublicUIClass.getLoginDate().toString());
      }
      getBillCardPanel().setBodyValueAt(ufSumMny, row, "nsummny");

    }
    catch (Exception ex) {
      reportException(ex);
      throw ex;
    }
  }

  /**
   * //计算本币金额、本币税额、本币价税合计 创建日期：(2004-3-23 19:16:20)
   */
  private void computeValueFrmOtherBill(InvoiceVO vo) throws Exception {

    InvoiceItemVO[] items = vo.getBodyVO();
    try {
      for (int i = 0; i < items.length; i++) {
        InvoiceItemVO itemVO = items[i];
        // 原币金额
        UFDouble ufNoriginalcurmny = PuPubVO.getUFDouble_ZeroAsNull(itemVO.getNoriginalcurmny());
        UFDouble ufMoney = null;
        // 原币税额
        UFDouble ufNoriginaltaxmny = PuPubVO.getUFDouble_ZeroAsNull(itemVO.getNoriginaltaxmny());
        UFDouble ufTaxMny = null;
        // 原币价税合计
        UFDouble ufNoriginalsummny = PuPubVO.getUFDouble_ZeroAsNull(itemVO.getNoriginalsummny());
        UFDouble ufSumMny = null;
        String strCurrTypeId = itemVO.getCcurrencytypeid();

        UFDate dOrderDate = vo.getHeadVO().getDinvoicedate();
        // 首先设置显示精度
        // setRowDigits_ExchangeRate(getPk_corp(), iRow,
        // getInvBillPanel().getBillModel(),m_cardPoPubSetUI2);
        // 值
        UFDouble ufBRate = null;
        UFDouble ufARate = null;
        if (dOrderDate != null) {
          UFDouble[] daRate = null;
          String strCurrDate = dOrderDate.toString();
          if (strCurrDate == null || strCurrDate.trim().length() == 0) {
            strCurrDate = PoPublicUIClass.getLoginDate() + "";
          }
          daRate = m_listPoPubSetUI2.getBothExchRateValue(getPk_corp(), strCurrTypeId, new UFDate(strCurrDate));
          // 折本汇率
          ufBRate = daRate[0];
          itemVO.setNexchangeotobrate(ufBRate);
        }
        // 本币金额
        if (ufNoriginalcurmny == null) {
          ufMoney = null;
        }
        else {
          ufMoney = POPubSetUI.getCurrArith_Busi(getPk_corp()).getAmountByOpp(items[i].getCcurrencytypeid(),
              CurrParamQuery.getInstance().getLocalCurrPK(getPk_corp()), ufNoriginalcurmny, ufBRate,
              nc.ui.po.pub.PoPublicUIClass.getLoginDate().toString());
        }
        itemVO.setNmoney(ufMoney);
        // 本币税额
        if (ufNoriginaltaxmny == null) {
          ufTaxMny = null;
        }
        else {
          ufTaxMny = POPubSetUI.getCurrArith_Busi(getPk_corp()).getAmountByOpp(items[i].getCcurrencytypeid(),
              CurrParamQuery.getInstance().getLocalCurrPK(getPk_corp()), ufNoriginaltaxmny, ufBRate,
              nc.ui.po.pub.PoPublicUIClass.getLoginDate().toString());
        }
        itemVO.setNtaxmny(ufTaxMny);
        // 本币价税合计
        if (ufNoriginalsummny == null) {
          ufSumMny = null;
        }
        else {
          ufSumMny = POPubSetUI.getCurrArith_Busi(getPk_corp()).getAmountByOpp(items[i].getCcurrencytypeid(),
              CurrParamQuery.getInstance().getLocalCurrPK(getPk_corp()), ufNoriginalsummny, ufBRate,
              nc.ui.po.pub.PoPublicUIClass.getLoginDate().toString());
        }
        itemVO.setNsummny(ufSumMny);
      }
    }
    catch (Exception ex) {
      reportException(ex);
      throw ex;
    }

  }

  /**
   * 作者：汪维敏 功能：发票复制处理 参数： 返回： 例外： 日期：(2004-6-14 14:58:38) 修改日期，修改人，修改原因，注释标志：
   */
  private void dealWhenCopy() {
    // 显示状态图片
    // V5 Del : setImageType(this.IMAGE_NULL);

    // 清空表头数据
    getBillCardPanel().getHeadItem("vinvoicecode").setValue(null);
    //getBillCardPanel().getHeadItem("vinvoicecode").setEdit(true);
    getBillCardPanel().getTailItem("iprintcount").setValue(new Integer(0));    
    getBillCardPanel().getHeadItem("cinvoiceid").setValue(null);
    getBillCardPanel().getHeadItem("ibillstatus").setValue(new Integer(0));
    getBillCardPanel().getHeadItem("ts").setValue(null);
    getBillCardPanel().getHeadItem("dinvoicedate").setValue(nc.ui.pub.ClientEnvironment.getInstance().getDate());
    getBillCardPanel().getHeadItem("darrivedate").setValue(nc.ui.pub.ClientEnvironment.getInstance().getDate());

    // 设置制单人
    ((UIRefPane) getBillCardPanel().getTailItem("coperator").getComponent()).setPK(nc.ui.pub.ClientEnvironment
        .getInstance().getUser().getPrimaryKey());
    ((UIRefPane) getBillCardPanel().getTailItem("dauditdate").getComponent()).setValue(null);
    getBillCardPanel().getTailItem("cauditpsn").setValue(null);
    getBillCardPanel().getTailItem("tmaketime").setValue(null);
    getBillCardPanel().getTailItem("taudittime").setValue(null);
    getBillCardPanel().getTailItem("tlastmaketime").setValue(null);

    try {
      // 过滤存货参照,保证在业务类型是受托代销业务类型时替换件参照是受托代销属性
      setInventoryRefFilter(getBillCardPanel().getHeadItem("cbiztype").getValue());
    }
    catch (Exception e) {
      SCMEnv.out(e);
    }
    // 清空表体数据
    BillModel bm = getBillCardPanel().getBillModel();
    //按照czp要求,整单复制相当于新增,不带上游信息 by zhaoyha at 2009.1.8
    for (int i = 0; i < bm.getRowCount(); i++) {
      bm.setRowState(i, BillModel.ADD);
       bm.setValueAt(null, i, "corderid");
       bm.setValueAt(null, i, "corder_bid");
      bm.setValueAt(null, i, "naccumorignsettmny");
      bm.setValueAt(null, i, "naccumsettmny");
      bm.setValueAt(null, i, "naccumsettnum");
       bm.setValueAt(null, i, "cupsourcehts");
       bm.setValueAt(null, i, "cupsourcebts");
       bm.setValueAt(null, i, "csourcehts");
       bm.setValueAt(null, i, "csourcebts");
      bm.setValueAt(null, i, "npaymentmny");
      bm.setValueAt(null, i, "noriginalpaymentmny");
      bm.setValueAt(null, i, "cinvoice_bid");
      bm.setValueAt(null, i, "cinvoiceid");
       bm.setValueAt(null, i, "cupsourcebillrowid");
       bm.setValueAt(null, i, "cupsourcebilltype");
       bm.setValueAt(null, i, "cupsourcebillid");
       bm.setValueAt(null, i, "csourcebillid");
       bm.setValueAt(null, i, "csourcebillrowid");
       bm.setValueAt(null, i, "csourcebilltype");
       bm.setValueAt(null, i, "csourcebillname");
       bm.setValueAt(null, i, "csourcebillcode");
       bm.setValueAt(null, i, "csourcebillrowno");
       bm.setValueAt(null, i, "cancestorbillname");
       bm.setValueAt(null, i, "cancestorbillcode");
       bm.setValueAt(null, i, "cancestorbillrowno");
      bm.setValueAt(null, i, "ts");
    }
    getBillCardPanel().getBodyItem("invcode").setEnabled(true);
  }

  /**
   * 作者：王印芬 功能：执行卡片查询,由卡片的"查询"及"刷新"调用 参数：InvQueDlg curDlg 查询对话框 返回：无 例外：无
   * 日期：(2002-3-13 11:39:21) 修改日期，修改人，修改原因，注释标志： 2002-11-14 wyf 加入对LIKE处理
   */
  private void execBillQuery(InvQueDlg curDlg) {

    showHintMessage(getHeadHintText() +nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401","UPP40040401-000013")/*@res "正在查询"*/ + CommonConstant.SPACE_MARK + CommonConstant.BEGIN_MARK + "......." + CommonConstant.END_MARK);

    NormalCondVO[] normalVOs = curDlg.getNormalCondVOs();
    ConditionVO[] definedVOs = curDlg.getConditionVO();

    //wyf 2002-11-14  add begin
    PiPqPublicUIClass.processLIKEInCondVOs(definedVOs);
    //wyf 2002-11-14  add end
    
    
    InvoiceVO[] VOs = null;
    try {
      VOs = InvoiceHelper.queryInvoiceVOsByCondsMy(getClientEnvironment().getUser().getPrimaryKey(), normalVOs, definedVOs);
    } catch (java.lang.Exception e) {
      SCMEnv.out(e);
      MessageDialog.showHintDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401","UPPSCMCommon-000270")/*@res "提示"*/, nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401","UPP40040401-000014")/*@res "系统故障!"*/);
      return;
    }

    // 没有符合条件的发票
    if (VOs == null) {
      setInvVOs(null);
      MessageDialog.showHintDlg(this,
          nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPPSCMCommon-000270")/*
                                                                                         * @res
                                                                                         * "提示"
                                                                                         */,
          nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPP40040401-000015")/*
                                                                                         * @res
                                                                                         * "没有符合查询条件的发票!"
                                                                                         */);
      getBillCardPanel().addNew();
      return;
    }

    // 设置发票VO数组
    for (int i = 0; i < VOs.length; i++) {
      VOs[i].setSource(InvoiceVO.FROM_QUERY);
    }
    setInvVOs(VOs);
    setCurVOPos(0);

    // 设置卡片及列表的显示状态
    setBillBrowseState(STATE_BROWSE_NORMAL);
    setListOperState(STATE_LIST_NORMAL);

    // 设置卡片界面数据
    setVOToBillPanel();
  }

  /**
   * 作者：汪维敏 功能：填充表头公式数据，避免不可参照出的数据 参数： 返回： 例外： 日期：(2004-5-20 14:18:41)
   * 修改日期，修改人，修改原因，注释标志：
   * 
   * @param vo
   *          nc.vo.pr.pray.PraybillVO
   */
  public void execHeadTailFormula(InvoiceVO vo) {
    if (vo == null) {
      return;
    }
    // 单个公式
    InvoiceHeaderVO voHead = vo.getHeadVO();
    UIRefPane refpane = null;

    String[] saFormula = new String[] {
      "getColValue(bd_calbody,bodyname,pk_calbody,cstoreorganization)"
    };
    PuTool.getFormulaParse().setExpressArray(saFormula);

    int iFormulaLen = saFormula.length;
    for (int i = 0; i < iFormulaLen; i++) {
      PuTool.getFormulaParse().addVariable("cstoreorganization", voHead.getCstoreorganization());
    }

    String[][] saRet = PuTool.getFormulaParse().getValueSArray();

    refpane = (UIRefPane) getBillCardPanel().getHeadItem("cstoreorganization").getComponent();

    if (nc.vo.scm.pu.PuPubVO.getString_TrimZeroLenAsNull(refpane.getUITextField().getText()) == null) {
      if (saRet != null && saRet[0] != null)
        refpane.getUITextField().setText(saRet[0][0]);
    }
  }

  /**
   * 作者：王印芬 功能：执行列表查询,由列表的"查询"及"刷新"调用 参数：InvQueDlg curDlg 查询对话框 返回：无 例外：无
   * 日期：(2002-3-13 11:39:21) 修改日期，修改人，修改原因，注释标志： 2002-11-14 wyf 加入对LIKE处理
   */
  private void execListQuery(InvQueDlg curDlg) {

    showHintMessage(getHeadHintText() + nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401","UPP40040401-000013")/*@res "正在查询"*/ + "......." );

    NormalCondVO[] normalVOs = curDlg.getNormalCondVOs();
    ConditionVO[] definedVOs = curDlg.getConditionVO();
    //未查询直接点刷新
    if(definedVOs == null){
      return ;
    }
    //wyf 2002-11-14  add begin
    PiPqPublicUIClass.processLIKEInCondVOs(definedVOs);
    //wyf 2002-11-14  add end

    
    InvoiceVO[] VOs = null;
    try {
      VOs = InvoiceHelper.queryInvoiceVOsByCondsMy(getClientEnvironment().getUser().getPrimaryKey(), normalVOs, definedVOs);
    }
    catch (java.lang.Exception e) {
      SCMEnv.out(e);
      MessageDialog.showHintDlg(this,
          nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPPSCMCommon-000270")/*
                                                                                         * @res
                                                                                         * "提示"
                                                                                         */,
          nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPP40040401-000014")/*
                                                                                         * @res
                                                                                         * "系统故障!"
                                                                                         */);
      return;
    }

    // 没有符合条件的发票
    if (VOs == null) {
      setInvVOs(null);
      MessageDialog.showHintDlg(this,
          nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPPSCMCommon-000270")/*
                                                                                         * @res
                                                                                         * "提示"
                                                                                         */,
          nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPP40040401-000015")/*
                                                                                         * @res
                                                                                         * "没有符合查询条件的发票!"
                                                                                         */);
      setVOsToListPanel();
      return;
    }

    // 设置发票VO数组
    for (int i = 0; i < VOs.length; i++) {
      VOs[i].setSource(InvoiceVO.FROM_QUERY);
    }
    setInvVOs(VOs);

    // 显示列表当前所有VO,并对经过公式计算的值赋值
    setCurVOPos(0);
    setVOsToListPanel();

    // 设置卡片及列表的显示状态
    setBillBrowseState(STATE_BROWSE_NORMAL);
    setListOperState(STATE_LIST_NORMAL);
    setCurOperState(getListOperState());
  }

  /**
   * 此处插入方法说明。 功能描述:过虑掉空行 作者：汪维敏 输入参数: 返回值: 异常处理: 日期:
   */
  private void filterNullLine() {
    // 存货列值暂存
    Object oTempValue = null;
    // 表体model
    nc.ui.pub.bill.BillModel bmBill = getBillCardPanel().getBillModel();

    // 存货列号，效率高一些。
    int iInvCol = bmBill.getBodyColByKey(m_sInvMngIDItemKey);

    // 必须有存货列
    if (bmBill != null && iInvCol >= 0 && iInvCol < bmBill.getColumnCount()) {
      // 行数
      int iRowCount = getBillCardPanel().getRowCount();
      // 从后向前删
      for (int line = iRowCount - 1; line >= 0; line--) {
        // 存货未填
        oTempValue = bmBill.getValueAt(line, iInvCol);
        if (oTempValue == null || oTempValue.toString().trim().length() == 0)
          // 删行
          getBillCardPanel().getBillModel().delLine(new int[] {
            line
          });
      }
    }
    if (bmBill.getRowCount() <= 0 && isCouldMaked())
      onAppendLine();
  }

  /**
   * 此处插入方法说明。 创建日期：(2001-9-19 9:38:47)
   * 
   * @return java.lang.String
   */
  public int getBillBrowseState() {
    return m_nBillBrowseState;
  }

  /**
   * 子类实现该方法，返回业务界面的标题。
   * 
   * @version (00-6-6 13:33:25)
   * @return java.lang.String
   */
  private String getCauditid() {

    return m_cauditid;
  }

  /**
   * 函数的功能、用途、对属性的更改，以及函数执行前后对象的状态。
   * 
   * @param 参数说明
   * @return 返回值
   * @exception 异常描述
   * @see 需要参见的其它内容
   * @since 从类的那一个版本，此方法被添加进来
   * @return java.lang.String
   */
  private java.lang.String getCurBizeType() {
    return m_strCurBizeType;
  }

  /**
   * 根据原币计算本币
   */
  private String getCurOperator() {
    return nc.ui.pub.ClientEnvironment.getInstance().getUser().getPrimaryKey();
  }

  /**
   * 得到当前界面的操作状态
   * 
   * @param 参数说明
   * @return 返回值
   * @exception 异常描述
   * @see 需要参见的其它内容
   * @since 从类的那一个版本，此方法被添加进来
   * @return java.lang.String
   */
  public int getCurOperState() {
    return m_nCurOperState;
  }

  /**
   * 得到当前界面PANEL
   * 
   * @param 参数说明
   * @return 返回值
   * @exception 异常描述
   * @see 需要参见的其它内容
   * @since 从类的那一个版本，此方法被添加进来。（可选）
   * @deprecated 该方法从类的那一个版本后，已经被其它方法替换。（可选）
   */
  private Component getCurPanel() {
    if (getCurPanelMode() == INV_PANEL_CARD)
      return getBillCardPanel();
    else if (getCurPanelMode() == INV_PANEL_LIST)
      return getBillListPanel();
    return null;

  }

  /**
   * 得到当前的界面PANEL的类型
   * 
   * @param 参数说明
   * @return 返回值
   * @exception 异常描述
   * @see 需要参见的其它内容
   * @since 从类的那一个版本，此方法被添加进来
   * @return int
   */
  private int getCurPanelMode() {
    return m_nCurPanelMode;
  }

  /**
   * 得到当前显示的VO的位置
   * 
   * @param 参数说明
   * @return 返回值
   * @exception 异常描述
   * @see 需要参见的其它内容
   * @since 从类的那一个版本，此方法被添加进来
   * @return nc.vo.pi.InvoiceVO[]
   */
  private int getCurVOPos() {

    return m_nCurInvVOPos;
  }

  /**
   * 子类实现该方法，返回业务界面的标题。
   * 
   * @version (00-6-6 13:33:25)
   * @return java.lang.String
   */
  private String getHeadHintText() {

    return m_strHeadHintText;
  }

  /**
   * 得到卡片界面
   * 
   * @param 参数说明
   * @return 返回值
   * @exception 异常描述
   * @see 需要参见的其它内容
   * @since 从类的那一个版本，此方法被添加进来。（可选）
   */
  public InvBillPanel getBillCardPanel() {
    if (m_billPanel == null) {
      try {
        m_billPanel = new InvBillPanel(nc.ui.pub.ClientEnvironment.getInstance(), this);
      }
      catch (java.lang.Exception e) {
        SCMEnv.out(e);
        MessageDialog.showHintDlg(this,NCLangRes.getInstance().getStrByID("40040401", "UPPSCMCommon-000270")/*@res"提示"*/, e.getMessage());
        return null;
      }
      
      // 表头编辑前事件监听
      m_billPanel.setBillBeforeEditListenerHeadTail(this);
      
      // 按行不同币种设置金额精度
      new DefaultCurrTypeBizDecimalListener(m_billPanel.getBillModel(), "ccurrencytypeid", m_itemMny_bu);
      new DefaultCurrTypeDecimalAdapter(m_billPanel.getBillModel(), "ccurrencytypeid", m_itemMny_fi);
      // 表体排序监听
      m_billPanel.getBillModel().addSortRelaObjectListener2(new IBillRelaSortListener2(){
        public Object[] getRelaSortObjectArray() {
          return getCurrBodyVOs();
        }
      });

      //since v56, 设置不允许批改的栏目(原则是：尽量不要限制应用。本版仅约束自由项，因为 目前自由项处理机制下支持不了批改)
      PuTool.setBatchModifyForbid(m_billPanel, new String[]{"vfree0"});
    }
    
    return m_billPanel;
  }

  /**
   * 所有符合查询条件的发票HVO
   * 
   * @param 参数说明
   * @return 返回值
   * @exception 异常描述
   * @see 需要参见的其它内容
   * @since 从类的那一个版本，此方法被添加进来
   * @return nc.vo.pi.InvoiceVO[]
   */
  private nc.vo.pi.InvoiceHeaderVO[] getInvHVOs() {

    if (getInvVOs() == null)
      return null;

    Vector vec = new Vector();
    for (int i = 0; i < getInvVOs().length; i++) {
      vec.addElement(getInvVOs()[i].getHeadVO());
    }
    InvoiceHeaderVO[] hVO = new InvoiceHeaderVO[getInvVOs().length];
    vec.copyInto(hVO);

    return hVO;
  }

  /**
   * 得到发票列表PANEL
   * 
   * @param 参数说明
   * @return 返回值
   * @exception 异常描述
   * @see 需要参见的其它内容
   * @since 从类的那一个版本，此方法被添加进来。（可选）
   * @deprecated 该方法从类的那一个版本后，已经被其它方法替换。（可选）
   * @return nc.ui.pa.mi.InvListPanel
   */
  public InvListPanel getBillListPanel() {
    if (m_listPoPubSetUI2 == null) {
      m_listPoPubSetUI2 = new POPubSetUI2();
    }

    if (m_listPanel == null) {
      try {
        m_listPanel = new InvListPanel(nc.ui.pub.ClientEnvironment.getInstance(),this);

        m_listPanel.getHeadBillModel().addSortRelaObjectListener2(this);
      }
      catch (java.lang.Throwable ivjExc) {
        SCMEnv.out(ivjExc.toString());
        MessageDialog.showHintDlg(this,
            nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPP40040401-000016")/*
                                                                                           * @res
                                                                                           * "系统故障"
                                                                                           */,
            nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPP40040401-000017")/*
                                                                                           * @res
                                                                                           * "列表模板加载失败！"
                                                                                           */);
        return null;
      }
      m_listPanel.getHeadItem("cauditpsn").setShow(false);
      initListListener();

      new DefaultCurrTypeBizDecimalListener(m_listPanel.getBodyBillModel(), "ccurrencytypeid", m_itemMny_bu);
      new DefaultCurrTypeDecimalAdapter(m_listPanel.getBodyBillModel(), "ccurrencytypeid", m_itemMny_fi);

      // since v55, 支持整行选中
//      PuTool.setLineSelectedList(m_listPanel);
      // 表体排序监听
      m_listPanel.getBodyBillModel().addSortRelaObjectListener2(new IBillRelaSortListener2(){
        public Object[] getRelaSortObjectArray() {
          return getCurrBodyVOs();
          }
      });
    }

    return m_listPanel;
  }

  /**
   * 所有符合查询条件的发票VO
   * 
   * @param 参数说明
   * @return 返回值
   * @exception 异常描述
   * @see 需要参见的其它内容
   * @since 从类的那一个版本，此方法被添加进来
   * @return nc.vo.pi.InvoiceVO[]
   */
  private nc.vo.pi.InvoiceVO[] getInvVOs() {
    return m_InvVOs;
  }

  /**
   * V55 参照增行专用：缓存发票VO，考虑到编辑时删行情况，从界面重新得到ITEMVO.
   * 
   * @param 参数说明
   * @return 返回值
   * @exception 异常描述
   * @see 需要参见的其它内容
   * @since 从类的那一个版本，此方法被添加进来
   * @return nc.vo.pi.InvoiceVO[]
   */
  public nc.vo.pi.InvoiceVO[] getInvVOsByContinue() {
    int bodyNum = getBillCardPanel().getRowCount();
    if (bodyNum < 1) {
      return null;
    }
    if (m_InvVOs == null) {
      return null;
    }
    InvoiceVO billVO = new InvoiceVO(bodyNum);
    getBillCardPanel().getBillValueVO(billVO);
    //重新设置汇率,只从界面上取可能带出汇率的精度不正确
    // by zhaoyha at 2009.6.8
    InvoiceItemVO[] invoiceItemVO = billVO.getBodyVO();
    for (int i=0;i<invoiceItemVO.length;++i) {
    UFDouble nexchgRate=(UFDouble) getBillCardPanel().getBodyValueAt(i, "nexchangeotobrate");
    invoiceItemVO[i].setNexchangeotobrate(nexchgRate);
    
  }
    m_InvVOs[getCurVOPos()].setChildrenVO(invoiceItemVO);
    return m_InvVOs;
  }

  /**
   * 此处插入方法说明。 创建日期：(2001-9-14 21:19:56)
   * 
   * @return java.lang.String
   */
  private int getListOperState() {
    return m_nListOperState;
  }

  /**
   * 返回模块编码 创建日期：(2000-10-17 15:10:42)
   * 
   * @return java.lang.String
   */
  //For V56 by zhaoyha
//  public String getModuleCode() {
//    return ConstantVO.NODE_INV_MAINTAIN;
//  }

  /**
   * 项目阶段参照
   */
  private int getOldCurrMoneyDigit() {

    return m_oldCurrMoneyDigit;
  }

  /**
   * 此处插入方法说明。 创建日期：(2001-9-14 12:03:06)
   * 
   * @return int[]
   */
  private String getPk_corp() {
    return nc.ui.pub.ClientEnvironment.getInstance().getCorporation().getPrimaryKey();
  }

  /**
   * 作者：王印芬 功能：得到发票的查询条件对话框 参数：无 返回：无 例外：无 日期：(2002-4-22 11:39:21)
   * 修改日期，修改人，修改原因，注释标志： 2002-06-07 wyf 取默认查询模板
   */
  public InvQueDlg getQueDlg() {

    if(m_InvQueDlg == null ){
      m_InvQueDlg = new InvoiceUIQueDlg(this,getModuleCode(),null,ScmConst.PO_Invoice, getCurOperator(),getPk_corp()) ;
      //根据V56需求，调整日期查询条件 by zhaoyha at 2009.9
      m_InvQueDlg.addCurToCurDate("po_invoice.dinvoicedate");
      //加载自定义项名称
      nc.ui.scm.pub.def.DefSetTool.updateQueryConditionClientUserDef(m_InvQueDlg,
      //当前模板
      getPk_corp(), //公司主键
      ScmConst.PO_Invoice,//单据类型
      "po_invoice.vdef", //单据模板中单据头的自定义项前缀
      "po_invoice_b.vdef" //单据模板中单据体的自定义项前缀
      );
    }

    return m_InvQueDlg ;
  }

  /**
   * 作者：王印芬 功能：从界面得到需保存的VO 参数：无 返回：无 例外：无 日期：(2001-5-13 11:39:21)
   * 修改日期，修改人，修改原因，注释标志： 2002-06-07 wyf 去掉区分采购还是委外发票的代码 2002-11-12 wyf
   * 加入对单据模板的必输项检查 2002-11-18 wyf 修改已存在的单据时，制单人不修改
   */
  private InvoiceVO getSavedInvVOFromBill() {

    // =======得到界面上对应VO(billVO),用于保存前的校验
    int bodyNum = getBillCardPanel().getRowCount();
    if (bodyNum < 1) {
      MessageDialog.showHintDlg(this,
          nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPPSCMCommon-000270")/*
                                                                                         * @res
                                                                                         * "提示"
                                                                                         */,
          nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPP40040401-000018")/*
                                                                                         * @res
                                                                                         * "请输入发票明细！"
                                                                                         */);
      return null;
    }
    InvoiceVO billVO = new InvoiceVO(bodyNum);
    //重新设置折本汇率的转换器，解决从界面去数值时，将value的精度变小的问题
    if (getBillCardPanel().getBillData().getBodyItem("nexchangeotobrate") != null){
      BillItem exchangeItem = getBillCardPanel().getBillData().getBodyItem("nexchangeotobrate");
      exchangeItem.setConverter(new nc.ui.scm.pub.SCMUFDoubleConverter());
    }
    getBillCardPanel().getBillValueVO(billVO);

    // =======得到需保存的发票的其他属性
    // ===表头
    // 当表头中的某些属性发生更改后，可能导致属性为空，这里为其赋值
    InvoiceHeaderVO head = billVO.getHeadVO();
    // 业务类型
    if(PuPubVO.getString_TrimZeroLenAsNull(head.getCbiztype())==null)
      head.setCbiztype(getCurBizeType());
    // 单据来源
    if (getInvVOs() != null && getInvVOs().length > 0) {
      InvoiceVO vo = getInvVOs()[getCurVOPos()];
      billVO.setSource(vo.getSource());
    }

    // iinvoicetype
    UIComboBox comItem = (UIComboBox) getBillCardPanel().getHeadItem("iinvoicetype").getComponent();
    head.setIinvoicetype(new Integer(comItem.getSelectedIndex()));
    // finitflag
    UICheckBox initCheck = (UICheckBox) getBillCardPanel().getHeadItem("finitflag").getComponent();
    if (initCheck.isSelected()) {
      head.setFinitflag(new Integer(1));
    }
    else {
      head.setFinitflag(new Integer(0));
    }
    // cvendorbaseid
    if ((head.getCvendorbaseid() == null || head.getCvendorbaseid().trim().equals(""))
        && (head.getCvendormangid() != null && head.getCvendormangid().trim() != "")) {
      // String cvendorbaseid =
      // getResultFromFormula("getColValue(bd_cumandoc,pk_cubasdoc,pk_cumandoc,cvendormangid)","cvendormangid",head.getCvendormangid())
      // ;
      String cvendorbaseid = (String) PiPqPublicUIClass.getAResultFromTable("bd_cumandoc", "pk_cubasdoc",
          "pk_cumandoc", head.getCvendormangid());
      head.setCvendorbaseid(cvendorbaseid);
    }
    // cvendormaneid
    if ((head.getCvendormangid() == null || head.getCvendormangid().trim().equals(""))
        && (head.getCvendorbaseid() != null && head.getCvendorbaseid().trim() != "")) {
      // String cvendorbaseid =
      // getResultFromFormula("getColValue(bd_cumandoc,pk_cubasdoc,pk_cumandoc,cvendormangid)","cvendormangid",head.getCvendormangid())
      // ;
      String cvendormangeid = (String) PiPqPublicUIClass.getAResultFromTable("bd_cumandoc", "pk_cumandoc",
          "pk_cubasdoc", head.getCvendorbaseid());
      head.setCvendormangid(cvendormangeid);
    }
    // pk_corp caccountyear coperator ibillstatus dr cbilltype vmemo
    head.setPk_corp(nc.ui.pub.ClientEnvironment.getInstance().getCorporation().getPrimaryKey());
    head.setCaccountyear(nc.ui.pub.ClientEnvironment.getInstance().getAccountYear());

    // wyf 2002-11-18 modify begin
    // head.setCoperator(nc.ui.pub.ClientEnvironment.getInstance().getUser().getPrimaryKey())
    // ;
    if (head.getCinvoiceid() == null || head.getCinvoiceid().trim().equals("")) {
      head.setCoperator(nc.ui.pub.ClientEnvironment.getInstance().getUser().getPrimaryKey());
    }
    // wyf 2002-11-18 modify end
    // czp 2009-10-22 modify bgn
    if(head.getIbillstatus() == null || head.getIbillstatus().intValue() != 2){
        head.setIbillstatus(new Integer(0));
    }
    // czp 2009-10-22 modify end
    head.setDr(new Integer(0));
    // 区分是委外还是采购
    head.setCbilltype(nc.vo.scm.pu.BillTypeConst.PO_INVOICE);
    int iLen = billVO.getBodyVO().length;
    if(getBillCardPanel().getHeadItem("vmemo").getComponent() instanceof UIRefPane){
      UIRefPane nRefPanel = (UIRefPane) getBillCardPanel().getHeadItem("vmemo").getComponent();
      UITextField vMemoField = nRefPanel.getUITextField();
      head.setVmemo(vMemoField.getText());
    }

    // head.setVmemo(
    // ((UIRefPane)getInvBillPanel().getHeadItem("vmemo").getComponent()).getText()
    // ) ;

    // ===表体
    for (int i = 0; i < iLen; i++) {
      // pk_corp dr
      billVO.getBodyVO()[i].setPk_corp(head.getPk_corp());
      billVO.getBodyVO()[i].setDr(head.getDr());
      // cbaseid
      if (billVO.getBodyVO()[i].getCbaseid() == null || billVO.getBodyVO()[i].getCbaseid().trim().equals("")) {
        String cbaseid = (String) PiPqPublicUIClass.getAResultFromTable("bd_invmandoc", "pk_invbasdoc", "pk_invmandoc",
            billVO.getBodyVO()[i].getCmangid());
        billVO.getBodyVO()[i].setCbaseid(cbaseid);
      }

      // 表头币种，折本及折辅汇率带到表体
      // billVO.getBodyVO()[i].setCcurrencytypeid(head.getCcurrencytypeid()) ;
      // billVO.getBodyVO()[i].setNexchangeotobrate(head.getNexchangeotobrate())
      // ;
      // billVO.getBodyVO()[i].setNexchangeotoarate(head.getNexchangeotoarate())
      // ;
      // 如果原币某数据项为空,则置为0
      UFDouble dTemp = new UFDouble(0.0);
      if (billVO.getBodyVO()[i].getNtaxrate() == null)
        billVO.getBodyVO()[i].setNtaxrate(dTemp);
      if (billVO.getBodyVO()[i].getNoriginalcurmny() == null)
        billVO.getBodyVO()[i].setNoriginalcurmny(dTemp);
      if (billVO.getBodyVO()[i].getNoriginalcurprice() == null)
        billVO.getBodyVO()[i].setNoriginalcurprice(dTemp);
      if (billVO.getBodyVO()[i].getNoriginalpaymentmny() == null)
        billVO.getBodyVO()[i].setNoriginalpaymentmny(dTemp);
      if (billVO.getBodyVO()[i].getNoriginalsummny() == null)
        billVO.getBodyVO()[i].setNoriginalsummny(dTemp);
      if (billVO.getBodyVO()[i].getNoriginaltaxmny() == null)
        billVO.getBodyVO()[i].setNoriginaltaxmny(dTemp);
      setExchangeRateBody(i, true, null);
      // //折本汇率
      UFDouble nexchangeotobrate = billVO.getBodyVO()[i].getNexchangeotobrate();
      // 公司对应的币种信息
      String pk_corp = billVO.getHeadVO().getPk_corp();
      String[] value = new String[] {
        new Integer(i + 1).toString()
      };
      if (nexchangeotobrate == null
          || (nexchangeotobrate != null && (nexchangeotobrate.doubleValue() < 0 || nexchangeotobrate.doubleValue() == 0))) {
        // return "表头：\n折本汇率不能为空！" ;
        MessageDialog.showWarningDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON",
            "UPPSCMCommon-000270")/* @res "提示" */, nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401",
            "UPP40040401-000236", null, value)/* @res "表体行{0}\n折本汇率不能为空" */);
        return null;
      }
    }
    //用第一行表体填表头币种汇率
    if(StringUtil.isEmptyWithTrim(head.getCcurrencytypeid())||
        UFDouble.ZERO_DBL.equals(PuPubVO.getUFDouble_NullAsZero(
            head.getNexchangeotobrate()))){
      head.setCcurrencytypeid(billVO.getBodyVO()[0].getCcurrencytypeid());
      head.setNexchangeotobrate(billVO.getBodyVO()[0].getNexchangeotobrate());
    }
    
      
    
    // wyf 2002-11-12 add begin
    // 作界面的必输项检查
    try {
      PuTool.validateNotNullField(getBillCardPanel());
    }
    catch (Exception e) {
      MessageDialog.showHintDlg(this,
          nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPPSCMCommon-000270")/*
                                                                                         * @res
                                                                                         * "提示"
                                                                                         */, e.getMessage());
      return null;
    }
    // wyf 2002-11-12 add end

    // =======计算主辅币数据
    if (!calNativeAndAssistCurrValue(billVO)) {
      return null;
    }

    // //////////////有效性检查--------只对界面上显示的进行检查
    try {
      billVO.validate();
    }
    catch (ValidationException e) {
      MessageDialog.showHintDlg(this,
          nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPPSCMCommon-000270")/*
                                                                                         * @res
                                                                                         * "提示"
                                                                                         */, e.getMessage());
      return null;
    }
    catch (HintMessageException e) {
      int ret = MessageDialog.showYesNoDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401",
          "UPPSCMCommon-000270")/* @res "提示" */, e.getMessage()
          + nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPP40040401-000159")/*
                                                                                           * @res
                                                                                           * "，是否继续保存？"
                                                                                           */);
      if (ret == MessageDialog.ID_NO) {
        return null;
      }
    }
    // =======改变的项：正常，新增，修改，删除
    InvoiceItemVO[] changedBodyVOs = (InvoiceItemVO[]) getBillCardPanel().getBillModel().getBodyValueChangeVOs(
        InvoiceItemVO.class.getName());
    if (changedBodyVOs != null && changedBodyVOs.length != 0) {
      Vector vec = new Vector();
      for (int i = 0; i < changedBodyVOs.length; i++) {
        if (changedBodyVOs[i].getStatus() == VOStatus.DELETED && changedBodyVOs[i].getCinvoice_bid() != null
            && changedBodyVOs[i].getCinvoice_bid().trim().length() > 0) {
          vec.addElement(changedBodyVOs[i]);
        }
      }
      if (vec.size() != 0) {
        for (int i = 0; i < billVO.getBodyVO().length; i++) {
          vec.addElement(billVO.getBodyVO()[i]);
        }
        InvoiceItemVO[] itemVOs = new InvoiceItemVO[vec.size()];
        vec.copyInto(itemVOs);
        billVO.setChildrenVO(itemVOs);
      }
    }

    return billVO;
  }

  /**
   * 作者：王印芬 功能：得到需保存的VO 参数：无 返回：无 例外：无 日期：(2002-4-22 11:39:21)
   * 修改日期，修改人，修改原因，注释标志：
   */
  private InvoiceVO getSavedVO() {
    // 从界面得到的VO,包括所有新增等的VO
    InvoiceVO retVO = getSavedInvVOFromBill();
    if (retVO == null) {
      return null;
    }
    String sKey = retVO.getHeadVO().getCinvoiceid();
    String sUpsourceBillType = retVO.getBodyVO()[0].getCupsourcebilltype();
    if (sUpsourceBillType != null && sUpsourceBillType.trim().length() > 0 && sUpsourceBillType.equals("50")) {
      retVO.setSource(InvoiceVO.FROM_ORDER);
    }
    // 设置发票来源

    // 发票修改
    if (sKey != null && sKey.trim().length() > 0) {
      retVO.setSource(InvoiceVO.FROM_QUERY);
      // 自制的单据，拷贝的单据
    }
    else if (sUpsourceBillType == null || sUpsourceBillType.trim().length() == 0) {
      retVO.setSource(InvoiceVO.FROM_HAND);
    }
    else if (sUpsourceBillType != null && sUpsourceBillType.trim().length() > 0) {
      if (sUpsourceBillType.equals(ScmConst.PO_Order) || sUpsourceBillType.equals(ScmConst.SC_Order))
        retVO.setSource(InvoiceVO.FROM_ORDER);
      else if (sUpsourceBillType.equals("45") || sUpsourceBillType.equals("47") || sUpsourceBillType.equals("4T"))
        retVO.setSource(InvoiceVO.FROM_STO);
      else
        retVO.setSource(InvoiceVO.FROM_VMI);
    }
    if (m_bCopy) {
      retVO.setSource(InvoiceVO.FROM_HAND);// 拷贝的单据
    }

    // 自制或拷贝向缓存中添加VO
    if (retVO.getSource() == InvoiceVO.FROM_HAND) {
      if (getInvVOs() == null || getInvVOs().length == 0)
        addNewOneIntoInvVOs(retVO);
      // 多次点击保存，只向缓存添加一个VO
      else {
        String sUpBillType = getInvVOs()[getCurVOPos()].getBodyVO()[0].getCupsourcebilltype();
        if (sUpBillType != null && sUpBillType.trim().length() > 0)
          addNewOneIntoInvVOs(retVO);
      }
    }

    return retVO;
  }

  /**
   * 此处插入方法说明。 创建日期：(2003-11-4 19:59:42)
   */
  private ArrayList getSelectedBills() {

    ArrayList aryRet = new ArrayList();
    InvoiceVO[] allvos = null;
    // 全部选中询价单
    int iPos = 0;
    for (int i = 0; i < getBillListPanel().getHeadBillModel().getRowCount(); i++) {
      if (getBillListPanel().getHeadBillModel().getRowState(i) == BillModel.SELECTED) {
        iPos = i;
        iPos = nc.ui.pu.pub.PuTool.getIndexBeforeSort(getBillListPanel(), iPos);
        aryRet.add(getInvVOs()[iPos]);
      }
    }
    allvos = (InvoiceVO[]) aryRet.toArray(new InvoiceVO[aryRet.size()]);

    // vAll.copyInto(allvos);
    // 更新表头列表.
    InvoiceVO curVO = null;
    for (int j = 0; j < allvos.length; j++) {
      curVO = allvos[j];
      if (!loadItemsForInvoiceVOs(new InvoiceVO[] {
        curVO
      })) {
        // setVoBodyToListPanle(-1);
      }
      else {
        // 设置表体列表.
        // setVoBodyToListPanle(j);

      }
    }
    return aryRet;
  }

  /**
   * 此处插入方法说明。 创建日期：(2001-9-14 12:03:06)
   * 
   * @return int[]
   */
  private int getSelectedRowCount() {
    return m_nSelectedRowCount;
  }

  /**
   * 子类实现该方法，返回业务界面的标题。
   * 
   * @version (00-6-6 13:33:25)
   * @return java.lang.String
   */
  public String getTitle() {

    String title = nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPP40040401-000160")/*
                                                                                                 * @res
                                                                                                 * "维护发票"
                                                                                                 */;

    // 如果不是缺省的系统模板则，返回用户定义的标题
    if (m_billPanel != null && !nc.ui.pub.ClientEnvironment.getInstance().getCorporation().getPk_corp().equals("@@@@")) {

      title = m_billPanel.getTitle();
    }
    return title;
  }

  /**
   * @param 参数说明
   * @return 返回值
   * @exception 异常描述
   * @see 需要参见的其它内容
   * @since 从类的那一个版本，此方法被添加进来。（可选）
   */
  private UFDate getToday() {
    return nc.ui.pub.ClientEnvironment.getInstance().getDate();
  }

  /**
   * 作者：王印芬 功能：该方法是实现接口ICheckRetVO的方法 该接口为审批流设计，以实现在审批人点击单据时，出现整张发票
   * 请不要随意删除及修改该方法，以避免错误 参数：无 返回：nc.vo.pub.AggregatedValueObject 为发票VO 例外：无
   * 日期：(2001-5-13 11:39:21) 修改日期，修改人，修改原因，注释标志：
   */
  public nc.vo.pub.AggregatedValueObject getVo() throws Exception {

    showHintMessage(getHeadHintText() + nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPP40040401-000013")/*
                                                                                                                       * @res
                                                                                                                       * "正在查询"
                                                                                                                       */
        + ".......");

    InvoiceVO[] VOs = null;
    try {
      VOs = InvoiceHelper.queryVoByHid(getCauditid());
      // added by czp on 2003-03-01 begin
      if (VOs != null && VOs.length > 0) {
        loadItemsForInvoiceVOs(new InvoiceVO[] {
          VOs[0]
        });
      }
      // added by czp on 2003-03-01 end

    }
    catch (java.lang.Exception e) {
      SCMEnv.out(e);
      throw e;
    }

    // 没有符合条件的发票
    if (VOs == null) {
      return null;
    }

    return VOs[0];

  }

  /**
   * 作者：王印芬 功能：该方法是实现接口ICheckRetVO的方法 该接口为审批流设计，以实现在审批人点击单据时，出现整张发票
   * 请不要随意删除及修改该方法，以避免错误 参数：无 返回：nc.vo.pub.AggregatedValueObject 为发票VO 例外：无
   * 日期：(2001-5-13 11:39:21) 修改日期，修改人，修改原因，注释标志：
   */
  public nc.vo.pub.AggregatedValueObject getLinkQueryVo(String pk_corp) throws Exception {

    showHintMessage(getHeadHintText() + nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPP40040401-000013")/*
                                                                                                                       * @res
                                                                                                                       * "正在查询"
                                                                                                                       */
        + ".......");

    NormalCondVO[] normalVOs = new NormalCondVO[2];
    // normalVOs[0] = new
    // NormalCondVO(nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UC000-0000791")/*@res
    // "单据ID"*/, getCauditid());
    normalVOs[0] = new NormalCondVO("单据ID", getCauditid());
    normalVOs[1] = new NormalCondVO("公司", pk_corp);

    InvoiceVO[] VOs = null;
    try {
      VOs = InvoiceHelper.queryInvoiceVOsByCondsMy(getClientEnvironment().getUser().getPrimaryKey(), normalVOs, null);
      // added by czp on 2003-03-01 begin
      if (VOs != null && VOs.length > 0) {
        loadItemsForInvoiceVOs(new InvoiceVO[] {
          VOs[0]
        });
      }
      // added by czp on 2003-03-01 end

    }
    catch (java.lang.Exception e) {
      SCMEnv.out(e);
      throw e;
    }

    // 没有符合条件的发票
    if (VOs == null) {
      return null;
    }

    return VOs[0];

  }

  /**
   * 每当部件抛出异常时被调用
   * 
   * @param exception
   *          java.lang.Throwable
   */
  public void handleException(java.lang.Throwable exception) {

    /* 除去下列各行的注释，以将未捕捉到的异常打印至 stdout。 */
    SCMEnv.out("--------- 未捕捉到的异常 ---------");
    SCMEnv.out(exception);
  }

  /**
   * 作者：王印芬 功能：发票卡片界面的监听初始化 参数：无 返回：无 例外：无 日期：(2001-5-13 11:39:21)
   * 修改日期，修改人，修改原因，注释标志： 2002-09-18 wyf 加入对仓库按钮的监听
   */
  private void initBillListener() {
    // 增加单据编辑监听
    getBillCardPanel().addEditListener(this);
    getBillCardPanel().addBodyEditListener2(this);
    // 项目参照加监听
    ((UIRefPane) getBillCardPanel().getBodyItem("cprojectphasename").getComponent()).getUIButton().addActionListener(
        this);
    // 自由项加监听
    ((UIRefPane) getBillCardPanel().getBodyItem("vfree0").getComponent()).getUITextField().addActionListener(this);
    // ((UIRefPane)getInvBillPanel().getBodyItem("vfree0").getComponent()).getUIButton().addActionListener(this)
    // ;
    // 业务员监听
    ((UIRefPane) getBillCardPanel().getHeadItem("cemployeeid").getComponent()).getUIButton().addActionListener(this);
    // 表体对菜单项的监听
    getBillCardPanel().addBodyMenuListener(this);
    // wyf 2002-09-18 add begin
    // 业务员监听
    ((UIRefPane) getBillCardPanel().getBodyItem("cwarehousename").getComponent()).getUIButton().addActionListener(this);
    // wyf 2002-09-18 add end

    getBillCardPanel().addActionListener(this);
  }

  /**
   * 按钮初始化
   * 
   * @param 参数说明
   * @return 返回值
   * @exception 异常描述
   * @see 需要参见的其它内容
   * @since 从类的那一个版本，此方法被添加进来。（可选）
   */
  private void initButtons() {

    // getBtnBrowsed();
    // getBtnMaintain();
    // 业务类型初始化
    PfUtilClient.retBusinessBtn(m_btnInvBillBusiType, ClientEnvironment.getInstance().getCorporation().getPk_corp(),
        nc.vo.scm.pu.BillTypeConst.PO_INVOICE);

    if (m_btnInvBillBusiType.getChildButtonGroup().length == 0) {
      // 没有业务类型
      m_btnInvBillBusiType.setVisible(false);
    }
    else {
      m_btnInvBillBusiType.setVisible(true);
    }
    m_btnBillMaintain.setEnabled(true);
    m_btnBillBrowsed.setEnabled(true);
    m_btnInvBillConversion.setEnabled(false);

    m_btnInvBillNew.setEnabled(true);
    ButtonObject btns[] = m_btnInvBillBusiType.getChildButtonGroup();
    if (btns != null && btns.length > 0 && btns[0] != null) {
      setCurBizeType(btns[0].getTag());
      m_bizButton = btns[0];
    }
    PuTool.initBusiAddBtns(m_btnInvBillBusiType, m_btnInvBillNew, ScmConst.PO_Invoice, getPk_corp());
    // 支持产业链功能扩展
    // if(getExtendBtns() != null){
    // m_BillBtnGroup = (ButtonObject[])
    // PuTool.appendArrayToArray(m_BillBtnGroup,getExtendBtns(),ButtonObject.class);
    // }

    setButtonsList();
//    setButtons(getBtnss());
    setButtons(m_btnTree.getButtonArray());
  }

  /**
   * 发票卡片界面的初始化
   * 
   * @param 参数说明
   * @return 返回值
   * @exception 异常描述
   * @see 需要参见的其它内容
   * @since 从类的那一个版本，此方法被添加进来。（可选）
   * @deprecated 该方法从类的那一个版本后，已经被其它方法替换。（可选）
   */
  private void initCard() {

    setLayout(new java.awt.BorderLayout());
    add(getBillCardPanel(), java.awt.BorderLayout.CENTER);
    m_btnInvShift.setName(NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000464")/** @res "列表显示" */
    );

  }

  /**
   * @功能：获取公司ID
   */
  private String getCorpId() {
    String corpid = null;
    corpid = nc.ui.pub.ClientEnvironment.getInstance().getCorporation().getPrimaryKey();
    if (corpid == null || corpid.trim().equals("")) {
      corpid = getCorpPrimaryKey();
    }
    return corpid;
  }

  /**
   * 函数的功能、用途、对属性的更改，以及函数执行前后对象的状态。
   * 
   * @param 参数说明
   * @return 返回值
   * @exception 异常描述
   * @see 需要参见的其它内容
   * @since 从类的那一个版本，此方法被添加进来。（可选）
   * @deprecated 该方法从类的那一个版本后，已经被其它方法替换。（可选）
   */
  private void initi() {
    setCurPanelMode(INV_PANEL_CARD);
    // V51重构需要的匹配,按钮实例变量化
    createBtnInstances();
    initButtons();
    initRefpane(getBillCardPanel().getBillData());
    initCard();
    initState();
    initBillListener();

    getPoPubSetUi2();

    BillRowNo.loadRowNoItem(getBillCardPanel(), "crowno");

    try {
      // 初始化:是否保留最初制单人,入库单开票结算方式,订单开票结算方式
      Hashtable t = SysInitBO_Client.queryBatchParaValues(PoPublicUIClass.getLoginPk_corp(), new String[] {
          "PO060", "PO30", "PO46", "PO52", "PO84"
      });

      if (t != null && t.size() > 0) {
        Object oTemp = t.get("PO060");
        isAllowedModifyByOther = (oTemp == null || oTemp.equals("N")) ? false : true;
        oTemp = t.get("PO30");
        m_sStock2InvoiceSettleMode = (oTemp == null) ? null : oTemp.toString();
        oTemp = t.get("PO46");
        m_sOrder2InvoiceSettleMode = (oTemp == null) ? null : oTemp.toString();
        oTemp = t.get("PO52");
        m_sZGYF = (oTemp == null) ? null : oTemp.toString();
        oTemp = t.get("PO84");
        iChange = "调整折扣".equalsIgnoreCase((oTemp == null) ? null : oTemp.toString()) ? 7 : 8;

      }
      getBillCardPanel().setBodyMenuShow(true);
    }
    catch (Exception e) {
      SCMEnv.out(e);
    }
    setMaxMnyDigit(iMaxMnyDigit);
    getBillCardPanel().setBillData(getBillCardPanel().getBillData());
    //since v55,选中模式调整
    PuTool.setLineSelected(getBillCardPanel());
    PuTool.setLineSelectedList(getBillListPanel());

  }

  /**
   * 发票列表界面的初始化
   * 
   * @param 参数说明
   * @return 返回值
   * @exception 异常描述
   * @see 需要参见的其它内容
   * @since 从类的那一个版本，此方法被添加进来。（可选）
   * @deprecated 该方法从类的那一个版本后，已经被其它方法替换。（可选）
   */
  private void initList() {
    setLayout(new java.awt.BorderLayout());
    add(getBillListPanel(), java.awt.BorderLayout.CENTER);
    setMaxMnyDigitList(iMaxMnyDigit);
  }

  /**
   * 作者：汪维敏 功能： 参数： 返回： 例外： 日期：(2004-9-8 10:18:55) 修改日期，修改人，修改原因，注释标志：
   */
  private void initRefpane(BillData bd) {
    UIRefPane refpane = null;
    String pk_corp = getClientEnvironment().getCorporation().getPrimaryKey();

    // 采购部门(采购或采购销售的部门)
    refpane = (UIRefPane) bd.getHeadItem("cdeptid").getComponent();
    refpane.getRefModel().setWherePart(
        " (bd_deptdoc.deptattr = '2' or bd_deptdoc.deptattr = '4') and bd_deptdoc.pk_corp = '" + pk_corp + "' ");
    refpane.setCacheEnabled(false);
    refpane.getRefModel().setPk_corp(pk_corp);

    // 业务员(采购部门的)
    UIRefPane refBiz = (UIRefPane) bd.getHeadItem("cemployeeid").getComponent();
    // refpane.getRefModel().addWherePart(" and (bd_psndoc.pk_deptdoc in (select
    // bd_deptdoc.pk_deptdoc from bd_deptdoc where (bd_deptdoc.deptattr = 2 or
    // bd_deptdoc.deptattr = 4) and bd_deptdoc.pk_corp ='"+pk_corp+"'))");
    refBiz
        .getRefModel()
        .addWherePart(
            " and (bd_psndoc.pk_deptdoc in (select bd_deptdoc.pk_deptdoc from bd_deptdoc where (bd_deptdoc.deptattr = '2' or bd_deptdoc.deptattr = '4') and bd_deptdoc.pk_corp ='"
                + pk_corp + "'))");
    String s = " bd_psndoc.indocflag='Y' and (bd_psndoc.pk_deptdoc in (select bd_deptdoc.pk_deptdoc from bd_deptdoc where (bd_deptdoc.deptattr = '2' or bd_deptdoc.deptattr = '4') and bd_deptdoc.pk_corp ='"
        + pk_corp + "')) ";
    refBiz.getRefModel().setWherePart(s);
    refBiz.getRefModel().setPk_corp(pk_corp);

    // 辅计量数量
    UIRefPane refpanel = null;
    if (bd.getBodyItem("nassistnum") != null) {
      refpanel = (UIRefPane) bd.getBodyItem("nassistnum").getComponent();
      UITextField nAssistNumUI = refpanel.getUITextField();
      nAssistNumUI.setDelStr("-");
    }

    // 辅计量参照
    if (bd.getBodyItem("cassistunitname") != null) {
      refpanel = ((UIRefPane) (bd.getBodyItem("cassistunitname").getComponent()));
      refpanel.setIsCustomDefined(true);
      refpanel.setRefModel(new OtherRefModel("辅计量单位"));
      refpanel.setReturnCode(false);
      refpanel.setRefInputType(1);
      refpanel.setCacheEnabled(false);
    }

    // 换算率
    if (bd.getBodyItem("nexchangerate") != null) {
      refpanel = (UIRefPane) bd.getBodyItem("nexchangerate").getComponent();
      UITextField nExchangeRateUI = refpanel.getUITextField();
      nExchangeRateUI.setDelStr("-");
    }

  }

  /**
   * 发票列表界面的初始化
   * 
   * @param 参数说明
   * @return 返回值
   * @exception 异常描述
   * @see 需要参见的其它内容
   * @since 从类的那一个版本，此方法被添加进来。（可选）
   * @deprecated 该方法从类的那一个版本后，已经被其它方法替换。（可选）
   */
  private void initListListener() {
    // 多选监听
    m_listPanel.getHeadTable().setCellSelectionEnabled(false);
    m_listPanel.getHeadTable().setSelectionMode(javax.swing.ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
    m_listPanel.getHeadTable().getSelectionModel().addListSelectionListener(this);

    // 换行等监听
    m_listPanel.addEditListener(this);
    m_listPanel.addMouseListener(this);
  }

  /**
   * 一些初始化状态的设置
   * 
   * @param
   * @return
   * @exception 异常描述
   * @see 需要参见的其它内容
   * @since 从类的那一个版本，此方法被添加进来。（可选）
   */
  private void initState() {
    setCurPanelMode(INV_PANEL_CARD);
    setCurOperState(STATE_INIT);
    setListOperState(STATE_LIST_NORMAL);
    setBillBrowseState(STATE_BROWSE_NORMAL);
    setButtonsAndPanelState();
  }

  /**
   * @param 参数说明
   * @return 返回值
   * @exception 异常描述
   * @see 需要参见的其它内容
   * @since 从类的那一个版本，此方法被添加进来。（可选）
   */
  private boolean isCouldMaked() {
    return m_bCouldMaked;
  }

  /**
   * 根据原币计算本币
   */
  private boolean isCouldMakedForABizType(String strBizType) {
    return true;

    // 该种业务类型是否有自制单据
    // BillbusinessVO[] billSourceVOs = null;
    // String beanName=IPFConfig.class.getName();
    // IPFConfig bo =
    // (IPFConfig)nc.bs.framework.common.NCLocator.getInstance().lookup(beanName);
    // try {
    // billSourceVOs =
    // bo.querybillSource(nc.ui.pub.ClientEnvironment.getInstance().getCorporation().getPrimaryKey(),
    // nc.vo.scm.pu.BillTypeConst.PO_INVOICE, strBizType);
    // } catch (Exception e) {
    // SCMEnv.out(e);
    // return false;
    // }
    //
    // if (billSourceVOs != null) {
    // //该种业务类型是否有自制单据
    // for (int i = 0; i < billSourceVOs.length; i++) {
    // //有自制单据，则可增行
    // if (billSourceVOs[i].getPk_billtype().toUpperCase().equals("MAKEFLAG")) {
    // return true;
    // }
    // }
    // }
    //
    // return false;
  }

  /**
   * @param 参数说明
   * @return 返回值
   * @exception 异常描述
   * @see 需要参见的其它内容
   * @since 从类的那一个版本，此方法被添加进来。（可选）
   */
  private boolean isEverQueryed() {

    return m_bEverQueryed;
  }

  /**
   * @功能：加载表头基础数据 创建日期：(2003-10-15 11:08:23)
   */
  private void loadBDData() {
    /* 变量定义 */
    String strFormula = null, strVarValue = null, strValue = null;
    UIRefPane refpnl = null;
    InvoiceVO vo = getInvVOs()[getCurVOPos()];
    if (vo == null || vo.getParentVO() == null)
      return;

    /* 业务类型 */
    strVarValue = (String) vo.getParentVO().getAttributeValue("cbiztype");
    refpnl = (UIRefPane) getBillCardPanel().getHeadItem("cbiztype").getComponent();
    strValue = refpnl.getUITextField().getText();
    if (strVarValue != null && (strValue == null || strValue.trim().equals(""))) {
      strFormula = "cbiztypename->getColValue(bd_busitype,businame,pk_busitype,cbiztype)";
      Object ob = getBillCardPanel().execHeadFormula(strFormula);
      refpnl.getUITextField().setText((String) ob);
    }
    /* 库存组织 */
    strVarValue = (String) vo.getParentVO().getAttributeValue("cstoreorganization");
    refpnl = (UIRefPane) getBillCardPanel().getHeadItem("cstoreorganization").getComponent();
    strValue = refpnl.getUITextField().getText();
    if (strVarValue != null && (strValue == null || strValue.trim().equals(""))) {
      strFormula = "cstoreorganizationname->getColValue(bd_calbody,bodyname,pk_calbody,cstoreorganization)";
      Object ob = getBillCardPanel().execHeadFormula(strFormula);
      refpnl.getUITextField().setText((String) ob);
    }
    /* 供应商 */
    strVarValue = (String) vo.getParentVO().getAttributeValue("cvendorbaseid");
    refpnl = (UIRefPane) getBillCardPanel().getHeadItem("cvendormangid").getComponent();
    strValue = refpnl.getUITextField().getText();
    if (strVarValue != null && (strValue == null || strValue.trim().equals(""))) {
      strFormula = "vendor->getColValue(bd_cubasdoc,custname,pk_cubasdoc,cvendorbaseid)";
      Object ob = getBillCardPanel().execHeadFormula(strFormula);
      refpnl.getUITextField().setText((String) ob);
    }
    /* 业务员 */
    // strVarValue = (String) vo.getParentVO().getAttributeValue("cemployeeid");
    // refpnl = (UIRefPane)
    // getInvBillPanel().getHeadItem("cemployeeid").getComponent();
    // strValue = refpnl.getUITextField().getText();
    // if (strVarValue != null && (strValue == null ||
    // strValue.trim().equals(""))) {
    // strFormula =
    // "cemployee->getColValue(bd_psndoc,psnname,pk_psndoc,cemployeeid)";
    // Object obb = getInvBillPanel().execHeadFormula(strFormula);
    // refpnl.getUITextField().setText((String) obb);
    // }
    /* 部门 */
    // strVarValue = (String) vo.getParentVO().getAttributeValue("cdeptid");
    // refpnl = (UIRefPane)
    // getInvBillPanel().getHeadItem("cdeptid").getComponent();
    // strValue = refpnl.getUITextField().getText();
    // if (strVarValue != null && (strValue == null ||
    // strValue.trim().equals(""))) {
    // strFormula =
    // "cdeptname->getColValue(bd_deptdoc,deptname,pk_deptdoc,cdeptid)";
    // Object ob = getInvBillPanel().execHeadFormula(strFormula);
    // refpnl.getUITextField().setText((String) ob);
    // }
    /* 散户 */
    strVarValue = (String) vo.getParentVO().getAttributeValue("cfreecustid");
    refpnl = (UIRefPane) getBillCardPanel().getHeadItem("cfreecustid").getComponent();
    strValue = refpnl.getUITextField().getText();
    if (strVarValue != null && (strValue == null || strValue.trim().equals(""))) {
      strFormula = "cfreecustname->getColValue(so_freecust,vcustname,cfreecustid,cfreecustid)";
      Object ob = getBillCardPanel().execHeadFormula(strFormula);
      refpnl.getUITextField().setText((String) ob);
    }
    /* 付款协议 */
    strVarValue = (String) vo.getParentVO().getAttributeValue("ctermprotocolid");
    refpnl = (UIRefPane) getBillCardPanel().getHeadItem("ctermprotocolid").getComponent();
    strValue = refpnl.getUITextField().getText();
    if (strVarValue != null && (strValue == null || strValue.trim().equals(""))) {
      strFormula = "ctermprotocolname->getColValue(bd_payterm,termname,pk_payterm,ctermprotocolid)";
      Object ob = getBillCardPanel().execHeadFormula(strFormula);
      refpnl.getUITextField().setText((String) ob);
    }
    /* 币种 */
    strVarValue = (String) vo.getParentVO().getAttributeValue("ccurrencytypeid");
    refpnl = (UIRefPane) getBillCardPanel().getHeadItem("ccurrencytypeid").getComponent();
    strValue = refpnl.getUITextField().getText();
    if (strVarValue != null && (strValue == null || strValue.trim().equals(""))) {
      strFormula = "currname->getColValue(bd_currtype,currtypename,pk_currtype,ccurrencytypeid)";
      Object ob = getBillCardPanel().execHeadFormula(strFormula);
      refpnl.getUITextField().setText((String) ob);
    }
    /* 开户银行 */
    if (getBillCardPanel().getHeadItem("cvendoraccount") != null) {
      strVarValue = getBillCardPanel().getHeadItem("cvendoraccount").getValue();
      if (strVarValue != null && strVarValue.trim().length() == 20) {
        String cfreecustid = getBillCardPanel().getHeadItem("cfreecustid").getValue();
        if (cfreecustid != null && cfreecustid.trim().length() > 0) {
          strFormula = "cvendoraccount->getColValue(so_freecust,vaccount,cfreecustid,cfreecustid)";
        }
        else {
          UIRefPane refpane = (UIRefPane) getBillCardPanel().getHeadItem("caccountbankid").getComponent();
          String pkcorp = getBillCardPanel().getCorp();
          String vendorbaseid = (String) getBillCardPanel().getHeadItem("cvendorbaseid").getValue();
          refpane.getRefModel().addWherePart(
                  " and bd_bankaccbas.pk_bankaccbas in (select  k.pk_accbank from bd_custbank k,bd_cumandoc m   where   m.pk_corp ='"
                      + pkcorp
                      + "'  and k.pk_cubasdoc=m.pk_cubasdoc and k.pk_cubasdoc='"
                      + vendorbaseid + "')");

          strFormula = "cvendoraccount->getColValue(bd_bankaccbas,accountname,pk_bankaccbas,caccountbankid)";
        }
        Object ob = getBillCardPanel().execHeadFormula(strFormula);
        getBillCardPanel().getHeadItem("cvendoraccount").setValue((String) ob);
      }
    }
  }

  /**
   * 作者：方益 功能：动态装载表体VO 参数： 返回： 例外： 日期：(2003-02-08 13:09:22) 修改日期，修改人，修改原因，注释标志：
   * 
   * @param inv
   *          nc.vo.pi.InvoiceVO
   */
  public boolean loadItemsForInvoiceVOs(InvoiceVO[] invs) {

    // 输入条件合法性判断
    if (invs == null || invs.length == 0)
      return true;

    // 选出表体为空的InvoiceVO, 重新装备一个轻量级InvoiceVO
    Vector v = new Vector();
    Hashtable hash = new Hashtable();
    for (int i = 0; i < invs.length; i++) {
      if (invs == null)
        continue;
      InvoiceHeaderVO head = (InvoiceHeaderVO) invs[i].getHeadVO();
      InvoiceItemVO[] items = (InvoiceItemVO[]) invs[i].getChildrenVO();
      if (head != null && head.getPrimaryKey() != null && (items == null || items.length == 0)) {
        InvoiceHeaderVO lightHead = new InvoiceHeaderVO();

        // 设置轻量级VO:只有主键和时间戳
        lightHead.setPrimaryKey(head.getPrimaryKey());
        lightHead.setTs(head.getTs());

        InvoiceVO lightVO = new InvoiceVO();
        lightVO.setParentVO(lightHead);
        v.add(lightVO);
        hash.put(head.getPrimaryKey(), invs[i]);
      }
    }

    // 不需要更新,返回True
    if (v.size() == 0)
      return true;

    // 从Vector中提取轻量级VO数组
    InvoiceVO[] lightVOs = null;
    lightVOs = new InvoiceVO[v.size()];
    v.copyInto(lightVOs);

    // 从后台刷新lightVOs的表体
    try {
      NormalCondVO[] normalVOs = getQueDlg().getNormalCondVOs();
      ConditionVO[] definedVOs = getQueDlg().getConditionVO();
      lightVOs = InvoiceHelper.queryItemsForInvoices(normalVOs, definedVOs, lightVOs);

    }
    catch (BusinessException e) {
      SCMEnv.out(e);
      MessageDialog.showHintDlg(this,
          nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPPSCMCommon-000270")/*
                                                                                         * @res
                                                                                         * "提示"
                                                                                         */, e.getMessage());
      return false;
    }
    catch (Exception e2) {
      SCMEnv.out(e2);
      return false;
    }
    if (lightVOs != null && lightVOs.length > 0) {
      // 设置表体
      for (int i = 0; i < lightVOs.length; i++) {
        InvoiceVO vo = (InvoiceVO) hash.get(lightVOs[i].getHeadVO().getPrimaryKey());
        vo.setChildrenVO(lightVOs[i].getChildrenVO());
      }
    }
    return true;
  }

  public void mouse_doubleclick(nc.ui.pub.bill.BillMouseEnent e) {
    onListBill();

    setButtonsAndPanelState();

    updateButtons();
    /**
     * 二次开发插件支持 by zhaoyha at 2009.2.16
     */
    getInvokeEventProxy().mouse_doubleclick(e);
  }

  /**
   * 功能：向单据卡片追加单据体（批量）
   */
  public void onActionAppendLine() {
    getBillCardPanel().addLine();
    // 设置行号
    nc.ui.scm.pub.report.BillRowNo.addLineRowNo(getBillCardPanel(), nc.vo.scm.pu.BillTypeConst.PO_INVOICE, "crowno");
    int iNewLine = getBillCardPanel().getBillModel().getRowCount() - 1;
    // 设置新增行的默认值
    setNewLineDefaultValue(iNewLine);
    setDefaultBody(iNewLine);
  }

  /**
   * 功能：向单据卡片插入单据体（批量）
   */
  public void onActionInsertLines(int iBeginRow, int iEndRow, int iInsertCount) {

    if (getBillCardPanel().getBillTable().getSelectedRowCount() <= 0) {
      showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPPSCMCommon-000354")/*
                                                                                                     * @res
                                                                                                     * "插行前请先选择表体行！"
                                                                                                     */);
      return;
    }
    int iCurRow = 0;
    for (int i = 0; i < iInsertCount; i++) {
      iCurRow = iBeginRow + i;
      getBillCardPanel().getBillModel().insertRow(iCurRow + 1);
      // 设置新插行的默认值
      // if (getInvBillPanel().getBillTable().getSelectedRows().length > 0) {
      setDefaultBody(iCurRow + 1);
      setNewLineDefaultValue(iCurRow + 1);
      // }
    }
    int iFinalEndRow = iBeginRow + iInsertCount + 1;

    // 设置行号
    BillRowNo.insertLineRowNos(getBillCardPanel(), nc.vo.scm.pu.BillTypeConst.PO_INVOICE, "crowno", iFinalEndRow,
        iInsertCount);

  }

  /**
   * 功能描述:浮动菜单右键功能权限控制
   * 
   * @deprecated 改为使用 {@link #setPopMenuBtnsEnable()}
   */
  private void rightButtonRightControl() {
    setPopMenuBtnsEnable();
  }

  /**
   * 功能描述:浮动菜单右键功能权限控制
   */
  private void setPopMenuBtnsEnable() {
    // 没有分配行操作权限
    if (m_btnInvBillRowOperation == null || m_btnInvBillRowOperation.getChildCount() == 0) {
      getBillCardPanel().getBodyPanel().getMiAddLine().setEnabled(false);
      getBillCardPanel().getBodyPanel().getMiCopyLine().setEnabled(false);
      getBillCardPanel().getBodyPanel().getMiDelLine().setEnabled(false);
      getBillCardPanel().getBodyPanel().getMiInsertLine().setEnabled(false);
      getBillCardPanel().getBodyPanel().getMiPasteLine().setEnabled(false);
      getBillCardPanel().getBodyPanel().getMiPasteLineToTail().setEnabled(false);
      if (m_miReSortRowNo != null) {
        m_miReSortRowNo.setEnabled(false);
      }
      if (m_miCardEdit != null) {
        m_miCardEdit.setEnabled(false);
      }
    }
    // 分配行操作权限
    else {
      getBillCardPanel().getBodyPanel().getMiAddLine().setEnabled(isPowerBtn(m_btnInvBillAddRow.getCode()));
      getBillCardPanel().getBodyPanel().getMiCopyLine().setEnabled(isPowerBtn(m_btnInvBillCopyRow.getCode()));
      getBillCardPanel().getBodyPanel().getMiDelLine().setEnabled(isPowerBtn(m_btnInvBillDeleteRow.getCode()));
      getBillCardPanel().getBodyPanel().getMiInsertLine().setEnabled(isPowerBtn(m_btnInvBillInsertRow.getCode()));
      getBillCardPanel().getBodyPanel().getMiPasteLine().setEnabled(isPowerBtn(m_btnInvBillPasteRow.getCode()));
      // 粘贴到行尾与粘贴可用性逻辑相同
      getBillCardPanel().getBodyPanel().getMiPasteLineToTail().setEnabled(
          isPowerBtn(m_btnInvBillPasteRowTail.getCode()));
      if (m_miReSortRowNo != null) {
        m_miReSortRowNo.setEnabled(isPowerBtn(m_btnReSortRowNo.getCode()));
      }
      if (m_miCardEdit != null) {
        m_miCardEdit.setEnabled(isPowerBtn(m_btnCardEdit.getCode()));
      }
    }
  }

  /**
   * 手工增加一张新发票
   * 
   * @param 参数说明
   * @return 返回值
   * @exception 异常描述
   * @see 需要参见的其它内容
   * @since 从类的那一个版本，此方法被添加进来。（可选）
   */
  private void onAdd() {

    setInvoiceTypeComItem();// 避免自制“虚拟”发票

    // InvoiceVO vo = new InvoiceVO();
    m_bAdd = true;
    // //////////////////////////////////////////
    getBillCardPanel().addNew();
    getBillCardPanel().setEnabled(true);

    setCurOperState(STATE_EDIT);
    setButtonsAndPanelState();

    updateButtons();
    // 浮动菜单右键功能权限控制
    setPopMenuBtnsEnable();
    // 设置VO
    // setVOToBillPanel() ;

    // ////////////////////////////////////////////////////////

    // V5 Del : setImageType(IMAGE_NULL);

    // 业务类型
    UIRefPane busiRef = (UIRefPane) getBillCardPanel().getHeadItem("cbiztype").getComponent();
    busiRef.setValue(m_bizButton.getName());
    busiRef.setPK(m_bizButton.getTag());
    setCurBizeType(m_bizButton.getTag());
    // 单据状态
    getBillCardPanel().getHeadItem("ibillstatus").setValue(new Integer(0));
    // 发票日期,收票日期,发票类型，扣税类别
    getBillCardPanel().getHeadItem("dinvoicedate").setValue(getToday());
    getBillCardPanel().getHeadItem("darrivedate").setValue(getToday());
    getBillCardPanel().getHeadItem("iinvoicetype").setValue(new Integer(0));
    getBillCardPanel().getHeadItem("idiscounttaxtype").setValue(new Integer(1));
    // 制单人
    ((UIRefPane) getBillCardPanel().getTailItem("coperator").getComponent()).setPK(nc.ui.pub.ClientEnvironment
        .getInstance().getUser().getPrimaryKey());
    ;

    try {
      // 币种
      String sLocalCurrId = CurrParamQuery.getInstance().getLocalCurrPK(getPk_corp());
      getBillCardPanel().getHeadItem("ccurrencytypeid").setValue(sLocalCurrId);
      // 汇率
      // setCurrRateToBillHead(sLocalCurrId, null, null);

      afterEditWhenHeadCurrency(null);
    }
    catch (Exception e) {
      SCMEnv.out(e);
    }

    // 是否有自制单据
    setCouldMaked(true);

    try {
      // 过滤存货参照,保证在业务类型是受托代销业务类型时替换件参照是受托代销属性
      setInventoryRefFilter(getCurBizeType());

      // since v51, 设置业务员默认值 根据操作员带出业务员
      setDefaultValueByUser();

    }
    catch (Exception e) {
      SCMEnv.out(e);
    }

    // 易用性修改
    onAppendLine();

    SCMEnv.out("m_btnInvBillDeleteRow==" + m_btnInvBillDeleteRow.isPower());
  }

  /**
   * 方法功能描述：设置存货参照过滤条件,根据业务类型只显示受托代销存货或全部存货。
   * <p>
   * <b>examples:</b>
   * <p>
   * 使用示例
   * <p>
   * <b>参数说明</b>
   * @param cBizType
   * @throws BusinessException
   * <p>
   * @author zhaoyha
   * @time 2009-4-22 下午03:12:09
   */
  private void setInventoryRefFilter(String cBizType) throws BusinessException {
    boolean checker = false;
    if (!cBizTypeTable.contains(cBizType)) {
      Object oTemp = CacheTool.getCellValue("bd_busitype", "pk_busitype", "verifyrule", cBizType);
      if (oTemp != null) {
        Object o[] = (Object[]) oTemp;
        if (o != null && o.length > 0 && o[0] != null && o[0].equals("S")){
          checker = true;
          cBizTypeTable.add(cBizType);
        }
      }
    }
    else 
      checker = true;
    
    // 过滤存货参照
    String sql = " and ( 1 =1 )";
    if (checker) 
       sql = " and (sellproxyflag = 'Y')";
    
    UIRefPane refCinventorycode = (UIRefPane) (getBillCardPanel().getBodyItem("invcode").getComponent());
    refCinventorycode.getRefModel().addWherePart(sql);
  }

  /**
   * 作者：王印芬 功能：增加一个发票行 参数：无 返回：无 例外：无 日期：(2001-5-13 11:39:21)
   * 修改日期，修改人，修改原因，注释标志： 2002-09-26 wyf 因增加一行与插入一行基本相同， 故提取原有方法为一公用方法，相应代码修改
   */
  private void onAppendLine() {

    getBillCardPanel().addLine();
    int iNewRow = getBillCardPanel().getRowCount() - 1;
    setNewLineDefaultValueForAddLine(iNewRow);
    BillRowNo.addLineRowNo(getBillCardPanel(), ScmConst.PO_Invoice, "crowno");
    SCMEnv.out("m_btnInvBillDeleteRow==" + m_btnInvBillDeleteRow.isPower());
    // 浮动菜单右键功能权限控制
    setPopMenuBtnsEnable();
  }

  /**
   * 此处插入方法说明。 功能：消息中心专用 参数： 返回： 例外： 日期：(2002-10-15 16:15:35)
   * 修改日期，修改人，修改原因，注释标志：
   */
  public void onUnAudit() {
    InvoiceVO[] proceVOs = new InvoiceVO[] {
      getInvVOs()[getCurVOPos()]
    };
    // 回退审批人及审批日期哈希表，操作失败时用到
    HashMap<String, ArrayList<Object>> mapAuditInfo = new HashMap<String, ArrayList<Object>>();
    ArrayList<Object> listAuditInfo = null;

    try {
      // 配合审批流
      for (int i = 0; i < proceVOs.length; i++) {
        // 操作失败时用到，回退审批人及审批日期哈希表
        if (PuPubVO.getString_TrimZeroLenAsNull(proceVOs[i].getHeadVO().getCauditpsn()) != null) {
          listAuditInfo = new ArrayList<Object>();
          listAuditInfo.add(proceVOs[i].getHeadVO().getCauditpsn());
          listAuditInfo.add(proceVOs[i].getHeadVO().getDauditdate());
          mapAuditInfo.put(proceVOs[i].getHeadVO().getPrimaryKey(), listAuditInfo);
        }
        // 赋操作员和原审批人ID：为判断是否允许弃审他人的单据
        proceVOs[i].getHeadVO().setCoperatoridnow(getClientEnvironment().getUser().getPrimaryKey());
        proceVOs[i].getHeadVO().setAttributeValue("cauditpsnold", proceVOs[i].getHeadVO().getCauditpsn());

        proceVOs[i].getHeadVO().setCauditpsn(nc.ui.pub.ClientEnvironment.getInstance().getUser().getPrimaryKey());
        proceVOs[i].getHeadVO().setCuserid(nc.ui.pub.ClientEnvironment.getInstance().getUser().getPrimaryKey());
      }
      if (!loadItemsForInvoiceVOs(proceVOs)) {
        showHintMessage("");
        return;
      }

      // 已结算的发票能否弃审控制:
      // IF 发票的业务类型核算规则为受托代销,直运采购, 或者为虚拟发票或者为自制发票, 不受控制
      // 否则 IF 发票来源于入库单而且根据入库开票能否结算参数为审批结算, 抛异常
      if (m_sZGYF == null || !(new UFBoolean(m_sZGYF).booleanValue())) {
        InvoiceVO invoiceVO = new InvoiceVO();
        for (int i = 0; i < proceVOs.length; i++) {
          invoiceVO = proceVOs[i];
          Object oTemp = CacheTool.getCellValue("bd_busitype", "pk_busitype", "verifyrule", invoiceVO.getHeadVO()
              .getCbiztype());
          if (oTemp == null)
            continue;
          Object oTemp1[] = (Object[]) oTemp;
          if (oTemp1[0].equals("S") || oTemp1[0].equals("Z"))
            continue;// 受托代销或直运采购业务类型
          if (invoiceVO.getHeadVO().getIinvoicetype().intValue() == 3)
            continue;// 虚拟发票
          InvoiceItemVO tempBodyVO[] = invoiceVO.getBodyVO();
          for (int j = 0; j < tempBodyVO.length; j++) {
            if (tempBodyVO[j].getCupsourcebilltype() == null)
              continue;// 自制采购发票
            if ((tempBodyVO[j].getCupsourcebilltype().equals("45") || tempBodyVO[j].getCupsourcebilltype().equals("47"))
                && m_sOrder2InvoiceSettleMode != null && m_sStock2InvoiceSettleMode.equals("审批时自动结算")) {
              if (tempBodyVO[j].getNaccumsettmny() != null && tempBodyVO[j].getNaccumsettmny().doubleValue() != 0.0) {
                MessageDialog.showHintDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401",
                    "UPPSCMCommon-000270")/* @res "提示" */, nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401",
                    "UPP40040401-000022")/* @res "该发票已结算，不能弃审！" */);
                showHintMessage("");
                return;
              }
            }
          }
        }
      }
      /**
       * 二次开发插件支持 by zhaoyha at 2009.2.16
       */
      getInvokeEventProxy().beforeAction(nc.vo.scm.plugin.Action.UNAUDIT, proceVOs);
 
      PfUtilClient.processBatch("UNAPPROVE" + nc.ui.pub.ClientEnvironment.getInstance().getUser().getPrimaryKey(),
          nc.vo.scm.pu.BillTypeConst.PO_INVOICE, ClientEnvironment.getInstance().getDate().toString(), proceVOs);

      //
      if (PfUtilClient.isSuccess()) {
        /**
         * 二次开发插件支持 by zhaoyha at 2009.2.16
         */
        getInvokeEventProxy().afterAction(nc.vo.scm.plugin.Action.UNAUDIT, proceVOs);
 
        // 业务日志
//        for (InvoiceVO invVO : proceVOs) {
//          invVO.getOperatelogVO().setOpratorname(nc.ui.pub.ClientEnvironment.getInstance().getUser().getUserName());
//          invVO.getOperatelogVO().setCompanyname(
//              nc.ui.pub.ClientEnvironment.getInstance().getCorporation().getUnitname());
//          invVO.getOperatelogVO().setOperatorId(nc.ui.pub.ClientEnvironment.getInstance().getUser().getPrimaryKey());
//          Operlog operlog = new Operlog();
//          operlog.insertBusinessExceptionlog(invVO, "审批", "审批", nc.vo.scm.funcs.Businesslog.MSGMESSAGE,
//              nc.vo.scm.pu.BillTypeConst.PO_INVOICE, nc.ui.sm.cmenu.Desktop.getApplet().getParameter("USER_IP"));
//        }
        InvoiceHeaderVO headVO = getInvVOs()[getCurVOPos()].getHeadVO();
        ArrayList arrRet = InvoiceHelper.queryForSaveAudit(headVO.getPrimaryKey());
        headVO.setDauditdate((UFDate) arrRet.get(0));
        headVO.setCauditpsn((String) arrRet.get(1));
        headVO.setIbillstatus((Integer) arrRet.get(2));
        headVO.setTs((String) arrRet.get(3));
        headVO.setTaudittime((String) arrRet.get(4));
        // 设置卡片界面数据
        setVOToBillPanel();
        // 设置按钮状态
        m_btnAudit.setEnabled(true);
        m_btnUnAudit.setEnabled(false);
        updateButtons();
      }
      else {
        // 回退审批人及审批日期
        if (mapAuditInfo.size() > 0) {
          for (int i = 0; i < proceVOs.length; i++) {
            if (!mapAuditInfo.containsKey(proceVOs[i].getHeadVO().getPrimaryKey())) {
              proceVOs[i].getHeadVO().setCauditpsn(null);
              proceVOs[i].getHeadVO().setDauditdate(null);
              continue;
            }
            listAuditInfo = (ArrayList<Object>) mapAuditInfo.get(proceVOs[i].getHeadVO().getPrimaryKey());
            proceVOs[i].getHeadVO().setCauditpsn((String) listAuditInfo.get(0));
            proceVOs[i].getHeadVO().setDauditdate((UFDate) listAuditInfo.get(1));
          }
        }
      }
    }
    catch (Exception e) {
      String strErrMsg = e.getMessage();
      if ((e instanceof TaskInvalidateException) || (e instanceof EngineException)) {
        MessageDialog.showHintDlg(this,
            nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPPSCMCommon-000270")/*
                                                                                           * @res
                                                                                           * "提示"
                                                                                           */, strErrMsg);
      }
      else if (strErrMsg != null
          && (strErrMsg
              .indexOf(nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPP40040401-000163")/*
                                                                                                     * @res
                                                                                                     * "后续"
                                                                                                     */) >= 0 || strErrMsg
              .indexOf(nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPP40040401-000164")/*
                                                                                                     * @res
                                                                                                     * "不能删除"
                                                                                                     */) >= 0)) {
        strErrMsg = nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPP40040401-000023")/*
                                                                                                   * @res
                                                                                                   * "采购发票弃审失败：单据存在后续处理或已经生成了实时凭证，不可弃审!"
                                                                                                   */;
        MessageDialog.showHintDlg(this,
            nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPPSCMCommon-000270")/*
                                                                                           * @res
                                                                                           * "提示"
                                                                                           */, strErrMsg);
      }
      else if (e instanceof BusinessException) {
        MessageDialog.showHintDlg(this,
            nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPPSCMCommon-000270")/*
                                                                                           * @res
                                                                                           * "提示"
                                                                                           */, strErrMsg);
      }
      else {
        strErrMsg = nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPP40040401-000024")/*
                                                                                                   * @res
                                                                                                   * "弃审发失败!可能是运行环境原因，如：\n1)、网络未正确连接\n2)、NC服务器终断\n请确认环境正确后再次操作!"
                                                                                                   */;
        SCMEnv.out("!!! 以下信息可发给系统管理员参考：\n");
        SCMEnv.out(e);
        MessageDialog.showHintDlg(this,
            nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPPSCMCommon-000270")/*
                                                                                           * @res
                                                                                           * "提示"
                                                                                           */, strErrMsg);
      }
      showHintMessage("");
      return;
    }
    finally {
      // 回退审批人及审批日期
      if (mapAuditInfo.size() > 0) {
        for (int i = 0; i < proceVOs.length; i++) {
          if (!mapAuditInfo.containsKey(proceVOs[i].getHeadVO().getPrimaryKey())) {
            proceVOs[i].getHeadVO().setCauditpsn(null);
            proceVOs[i].getHeadVO().setDauditdate(null);
            continue;
          }
          listAuditInfo = (ArrayList<Object>) mapAuditInfo.get(proceVOs[i].getHeadVO().getPrimaryKey());
          proceVOs[i].getHeadVO().setCauditpsn((String) listAuditInfo.get(0));
          proceVOs[i].getHeadVO().setDauditdate((UFDate) listAuditInfo.get(1));
        }
      }
    }

    showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UCH011"));
  }

  /**
   * 此处插入方法说明。 功能：消息中心专用 参数： 返回： 例外： 日期：(2002-10-15 16:15:35)
   * 修改日期，修改人，修改原因，注释标志：
   */
  public void onAudit() {

    InvoiceVO[] proceVOs = new InvoiceVO[] {
      getInvVOs()[getCurVOPos()]
    };

    // 填审批人与审批日期
    String strOperPk = nc.ui.pub.ClientEnvironment.getInstance().getUser().getPrimaryKey();
    UFDate today = nc.ui.pub.ClientEnvironment.getInstance().getDate();

    // 回退审批人及审批日期哈希表，操作失败时用到
    HashMap<String, ArrayList<Object>> mapAuditInfo = new HashMap<String, ArrayList<Object>>();
    ArrayList<Object> listAuditInfo = null;

    for (int i = 0; i < proceVOs.length; i++) {
      // 操作失败时用到，回退审批人及审批日期哈希表
      if (PuPubVO.getString_TrimZeroLenAsNull(proceVOs[i].getHeadVO().getCauditpsn()) != null) {
        listAuditInfo = new ArrayList<Object>();
        listAuditInfo.add(proceVOs[i].getHeadVO().getCauditpsn());
        listAuditInfo.add(proceVOs[i].getHeadVO().getDauditdate());
        mapAuditInfo.put(proceVOs[i].getHeadVO().getPrimaryKey(), listAuditInfo);
      }
      proceVOs[i].getHeadVO().setCauditpsn(strOperPk);
      proceVOs[i].getHeadVO().setDauditdate(today);
      proceVOs[i].getHeadVO().setCuserid(strOperPk);
      proceVOs[i].getHeadVO().setCoperatoridnow(getClientEnvironment().getUser().getPrimaryKey());
    }
    // 审批日期不能小于制单日期检查
    ClientLink cl = new ClientLink(nc.ui.pub.ClientEnvironment.getInstance());// 环境信息
    String errMsg = PuTool.getAuditLessThanMakeMsg(proceVOs, "dinvoicedate", "vinvoicecode", cl.getLogonDate(),
        ScmConst.PO_Invoice);
    if (errMsg != null) {
      MessageDialog.showErrorDlg(this,
          nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPPSCMCommon-000270")/*
           * @res
           * "提示"
           */, errMsg);
      showHintMessage("");
      return;
    }
    
    try {
      /**
       * 二次开发插件支持 by zhaoyha at 2009.2.16
       */
      getInvokeEventProxy().beforeAction(nc.vo.scm.plugin.Action.AUDIT, proceVOs);
      
    	  PfUtilClient.processBatchFlow(this, "APPROVE", nc.vo.scm.pu.BillTypeConst.PO_INVOICE, ClientEnvironment
          .getInstance().getDate().toString(), proceVOs, null);
      if (PfUtilClient.isSuccess()) {
        /**
         * 二次开发插件支持 by zhaoyha at 2009.2.16
         */
        getInvokeEventProxy().afterAction(nc.vo.scm.plugin.Action.AUDIT, proceVOs);


        // 设置发票VO数组
        setInvVOs(new InvoiceVO[] {
          (InvoiceVO) getVo()
        });
        setCurVOPos(0);
        // 业务日志
//        for (InvoiceVO invVO : proceVOs) {
//          invVO.getHeadVO().setCoperatoridnow(getClientEnvironment().getUser().getPrimaryKey());
//          invVO.getOperatelogVO().setOpratorname(nc.ui.pub.ClientEnvironment.getInstance().getUser().getUserName());
//          invVO.getOperatelogVO().setCompanyname(
//              nc.ui.pub.ClientEnvironment.getInstance().getCorporation().getUnitname());
//          invVO.getOperatelogVO().setOperatorId(nc.ui.pub.ClientEnvironment.getInstance().getUser().getPrimaryKey());
//          Operlog operlog = new Operlog();
//          operlog.insertBusinessExceptionlog(invVO, "审批", "审批", nc.vo.scm.funcs.Businesslog.MSGMESSAGE,
//              nc.vo.scm.pu.BillTypeConst.PO_INVOICE, nc.ui.sm.cmenu.Desktop.getApplet().getParameter("USER_IP"));
//        }
        // 设置卡片界面数据
        setVOToAuditedBillPanel();
        // 设置按钮状态
        m_btnAudit.setEnabled(false);
        m_btnUnAudit.setEnabled(true);
        updateButtons();
      }
      else {
        // 回退审批人及审批日期
        if (mapAuditInfo.size() > 0) {
          for (int i = 0; i < proceVOs.length; i++) {
            if (!mapAuditInfo.containsKey(proceVOs[i].getHeadVO().getPrimaryKey())) {
              proceVOs[i].getHeadVO().setCauditpsn(null);
              proceVOs[i].getHeadVO().setDauditdate(null);
              continue;
            }
            listAuditInfo = (ArrayList<Object>) mapAuditInfo.get(proceVOs[i].getHeadVO().getPrimaryKey());
            proceVOs[i].getHeadVO().setCauditpsn((String) listAuditInfo.get(0));
            proceVOs[i].getHeadVO().setDauditdate((UFDate) listAuditInfo.get(1));
          }
        }
      }
    }
    catch (Exception e) {
      SCMEnv.out(e);
      String strErrMsg = e.getMessage();
      if ((e instanceof TaskInvalidateException) || (e instanceof EngineException)) {
        MessageDialog.showHintDlg(this,
            nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPPSCMCommon-000270")/*
                                                                                           * @res
                                                                                           * "提示"
                                                                                           */, strErrMsg);
      }
      else if (strErrMsg != null
          && strErrMsg
              .indexOf(nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPP40040401-000161")/*
                                                                                                     * @res
                                                                                                     * "应付系统已结帐"
                                                                                                     */) >= 0) {
        MessageDialog.showHintDlg(this,
            nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPPSCMCommon-000270")/*
                                                                                           * @res
                                                                                           * "提示"
                                                                                           */,
            nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPP40040401-000019")/*
                                                                                           * @res
                                                                                           * "应付系统已结帐，发票不能审批！"
                                                                                           */);
      }
      else if (e instanceof BusinessException || e instanceof java.rmi.RemoteException) {
        MessageDialog.showHintDlg(this,
            nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPPSCMCommon-000270")/*
                                                                                           * @res
                                                                                           * "提示"
                                                                                           */, e
                .getMessage());
      }
      else {
        MessageDialog.showHintDlg(this,
            nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPPSCMCommon-000270")/*
                                                                                           * @res
                                                                                           * "提示"
                                                                                           */,
            nc.ui.ml.NCLangRes.getInstance().getStrByID("40040402", "UPP40040402-000005")/*
                                                                                           * @res
                                                                                           * "操作未成功"
                                                                                           */+ ":"
                + e.getMessage());
      }
      showHintMessage("");
      return;
    }
    finally {
      // 回退审批人及审批日期
      if (mapAuditInfo.size() > 0) {
        for (int i = 0; i < proceVOs.length; i++) {
          if (!mapAuditInfo.containsKey(proceVOs[i].getHeadVO().getPrimaryKey())) {
            proceVOs[i].getHeadVO().setCauditpsn(null);
            proceVOs[i].getHeadVO().setDauditdate(null);
            continue;
          }
          listAuditInfo = (ArrayList<Object>) mapAuditInfo.get(proceVOs[i].getHeadVO().getPrimaryKey());
          proceVOs[i].getHeadVO().setCauditpsn((String) listAuditInfo.get(0));
          proceVOs[i].getHeadVO().setDauditdate((UFDate) listAuditInfo.get(1));
        }
      }
    }

    showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UCH010"));
  }

  /**
   * 此处插入方法说明。 功能:卡片状态下审批. 参数： 返回： 例外： 日期：(2002-10-15 16:15:35)
   * 修改日期，修改人，修改原因，注释标志：
   */
  private void onBillAudit() {
    nc.vo.scm.pu.Timer timer = new nc.vo.scm.pu.Timer();
    timer.start("发票审批操作开始");
    int iInit = getInvVOs()[getCurVOPos()].getHeadVO().getFinitflag().intValue();
    if (iInit == 1) {
      MessageDialog.showHintDlg(this,
          nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPPSCMCommon-000270")/*
                                                                                         * @res
                                                                                         * "提示"
                                                                                         */,
          nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPP40040401-000021")/*
                                                                                         * @res
                                                                                         * "所选发票为期初发票,不能审批!"
                                                                                         */);
      showHintMessage("");
      return;
    }
    InvoiceVO[] proceVOs = new InvoiceVO[] {
      getInvVOs()[getCurVOPos()]
    };

    for (int i = 0; i < proceVOs.length; i++) {
      if (proceVOs[i] != null && proceVOs[i].getHeadVO().getPrimaryKey() != null) {
        for (int j = 0; j < proceVOs[i].getChildrenVO().length; j++) {
          try {
            if (PuPubVO.getString_TrimZeroLenAsNull(proceVOs[i].getChildrenVO()[j].getPrimaryKey()) == null) {
              proceVOs[i] = InvoiceHelper.findByPrimaryKey(getInvVOs()[getCurVOPos()].getHeadVO().getPrimaryKey());
              getInvVOs()[getCurVOPos()] = proceVOs[i];
            }
          }
          catch (BusinessException e) {
          }
          catch (Exception e) {
          }
        }
      }

    }
    // 填审批人与审批日期
    String strOperPk = nc.ui.pub.ClientEnvironment.getInstance().getUser().getPrimaryKey();
    UFDate today = nc.ui.pub.ClientEnvironment.getInstance().getDate();
    // 审批日期不能小于制单日期检查
    ClientLink cl = new ClientLink(nc.ui.pub.ClientEnvironment.getInstance());// 环境信息
    String errMsg = PuTool.getAuditLessThanMakeMsg(proceVOs, "dinvoicedate", "vinvoicecode", cl.getLogonDate(),
        ScmConst.PO_Invoice);
    if (errMsg != null) {
      MessageDialog.showErrorDlg(this,
          nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPPSCMCommon-000270")/*
           * @res
           * "提示"
           */, errMsg);
      showHintMessage("");
      return;
    }
    String[] oldAuditPsn=new String[proceVOs.length];
    String[] oldUserid=new String[proceVOs.length];
    UFDate[] oldAuditDate=new UFDate[proceVOs.length];
    String[] oldAuditTime=new String[proceVOs.length];
    
    for (int i = 0; i < proceVOs.length; i++) {
      //保存原信息，逐级审批时，就算出现异常不能直接清空，要恢复原数据 
      //by zhaoyha at 2009.6.29
      oldAuditPsn[i]=proceVOs[i].getHeadVO().getCauditpsn();
      oldUserid[i]=proceVOs[i].getHeadVO().getCuserid();
      oldAuditDate[i]=proceVOs[i].getHeadVO().getDauditdate();
      oldAuditTime[i]=proceVOs[i].getHeadVO().getTaudittime();
      
      proceVOs[i].getHeadVO().setCauditpsn(strOperPk);
      proceVOs[i].getHeadVO().setDauditdate(today);
      proceVOs[i].getHeadVO().setCuserid(strOperPk);
      proceVOs[i].getHeadVO().setTaudittime((new UFDateTime(new Date())).toString());
    }

    try {
    	//费用发票审批  add by QuSida (佛山骏杰) 2010-9-27
    	if(feeFlag)
      	  PfUtilClient.processBatchFlow(this, "FEEAPPROVE", nc.vo.scm.pu.BillTypeConst.PO_INVOICE, ClientEnvironment
      	          .getInstance().getDate().toString(), proceVOs, null);
        else
      PfUtilClient.processBatchFlow(this, "APPROVE", nc.vo.scm.pu.BillTypeConst.PO_INVOICE, ClientEnvironment
          .getInstance().getDate().toString(), proceVOs, null);
      timer.addExecutePhase("执行审批操作APPROVE");
      InvoiceVO resultVO = null;
      Integer iBillStatus = null;
      if (PfUtilClient.isSuccess()) {
        resultVO = InvoiceHelper.findByPrimaryKey(getInvVOs()[getCurVOPos()].getHeadVO().getPrimaryKey());
        getInvVOs()[getCurVOPos()] = resultVO;
        iBillStatus = new Integer(88);
        if (resultVO != null && resultVO.getHeadVO() != null && resultVO.getHeadVO().getIbillstatus() != null) {
          iBillStatus = resultVO.getHeadVO().getIbillstatus();
        }

        // 业务日志
//        resultVO.getOperatelogVO().setOpratorname(nc.ui.pub.ClientEnvironment.getInstance().getUser().getUserName());
//        resultVO.getOperatelogVO().setCompanyname(
//            nc.ui.pub.ClientEnvironment.getInstance().getCorporation().getUnitname());
//        resultVO.getOperatelogVO().setOperatorId(nc.ui.pub.ClientEnvironment.getInstance().getUser().getPrimaryKey());
//        Operlog operlog = new Operlog();
//        operlog.insertBusinessExceptionlog(resultVO, "审批", "审批", nc.vo.scm.funcs.Businesslog.MSGMESSAGE,
//            nc.vo.scm.pu.BillTypeConst.PO_INVOICE, nc.ui.sm.cmenu.Desktop.getApplet().getParameter("USER_IP"));

        // 设置卡片界面数据
        setVOToBillPanel();
        // 更新按钮状态
        // setButtonsAndPanelState();

        timer.addExecutePhase("setVOToBillPanel");
        timer.showAllExecutePhase("发票审批操作结束");
        if (iBillStatus.compareTo(nc.vo.scm.pu.BillStatus.FREE) == 0
            || iBillStatus.compareTo(nc.vo.scm.pu.BillStatus.AUDITFAIL) == 0) {
          showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPP40040401-000234")/*
                                                                                                         * @res
                                                                                                         * "审批未成功"
                                                                                                         */);

        }
        else if (iBillStatus.compareTo(nc.vo.scm.pu.BillStatus.AUDITED) == 0
            || iBillStatus.compareTo(nc.vo.scm.pu.BillStatus.AUDITING) == 0) {
          showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000236")/*
                                                                                                           * @res
                                                                                                           * "审批成功"
                                                                                                           */);
        }
      }
    }
    catch (Exception e) {
      for (int i = 0; i < proceVOs.length; i++) {
        proceVOs[i].getHeadVO().setCauditpsn(oldAuditPsn[i]);
        proceVOs[i].getHeadVO().setDauditdate(oldAuditDate[i]);
        proceVOs[i].getHeadVO().setCuserid(oldUserid[i]);
        proceVOs[i].getHeadVO().setTaudittime(oldAuditTime[i]);
      }

      SCMEnv.out(e);
      String strErrMsg = e.getMessage();
      if (strErrMsg != null
          && strErrMsg
              .indexOf(nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPP40040401-000161")/*
                                                                                                     * @res
                                                                                                     * "应付系统已结帐"
                                                                                                     */) >= 0) {
        MessageDialog.showHintDlg(this,
            nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPPSCMCommon-000270")/*
                                                                                           * @res
                                                                                           * "提示"
                                                                                           */,
            nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPP40040401-000019")/*
                                                                                           * @res
                                                                                           * "应付系统已结帐，发票不能审批！"
                                                                                           */);
      }
      else if (e instanceof BusinessException || e instanceof java.rmi.RemoteException) {
        MessageDialog.showHintDlg(this,
            nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPPSCMCommon-000270")/*
                                                                                           * @res
                                                                                           * "提示"
                                                                                           */, e
                .getMessage());
      }
      else {
        MessageDialog.showHintDlg(this,
            nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPPSCMCommon-000270")/*
                                                                                           * @res
                                                                                           * "提示"
                                                                                           */,
            nc.ui.ml.NCLangRes.getInstance().getStrByID("40040402", "UPP40040402-000005")/*
                                                                                           * @res
                                                                                           * "操作未成功"
                                                                                           */+ ":"
                + e.getMessage());
      }
      showHintMessage("");
      return;
    }

    showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UCH010"));
  }

  /**
   * 作者：王印芬 功能：审批列表的一批选中的单据 不能审批的条件：期初、已审批 参数：无 返回：无 例外：无 日期：(2002-3-13
   * 11:39:21) 修改日期，修改人，修改原因，注释标志： 2002-04-15 wyf
   * 配合后端只进行TS判断是否单据改变，本代码加入一些如是否审批的判断 2002-05-23 wyf 审批后的发票从界面去掉
   */
  private void onListAudit() {
    // 得到被选中的行
    Vector validVOsVEC = new Vector();
    // 可以审批的行
    Vector validIndexVEC = new Vector();
    // 可以审批的行头ID
    Vector validHidKeyVEC = new Vector();
    for (int i = 0; i < getInvVOs().length; i++) {
      if (m_listPanel.getHeadBillModel().getRowState(i) == BillModel.SELECTED) {
        // 有可能经过了排序，得到真正的表头INDEX
        int nCurIndex = PuTool.getIndexBeforeSort(m_listPanel, i);
        // 排除期初的 0-普通 1-期初 2-系统
        int iInit = getInvVOs()[nCurIndex].getHeadVO().getFinitflag().intValue();
        if (iInit == 1) {
          MessageDialog.showHintDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON",
              "UPPSCMCommon-000270")/* @res "提示" */, nc.ui.ml.NCLangRes.getInstance().getStrByID("40040402",
              "UPP40040402-000002")/* @res "所选发票存在期初发票！" */);
          showHintMessage("");
          return;
        }
        // 可以审批的
        validIndexVEC.addElement(new Integer(nCurIndex));
        validVOsVEC.addElement(getInvVOs()[nCurIndex]);
        validHidKeyVEC.add(getInvVOs()[nCurIndex].getHeadVO().getPrimaryKey());
      }
    }

    if (validVOsVEC.size() == 0) {
      MessageDialog.showHintDlg(this,
          nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000270")/*
                                                                                           * @res
                                                                                           * "提示"
                                                                                           */,
          nc.ui.ml.NCLangRes.getInstance().getStrByID("40040402", "UPP40040402-000003")/*
                                                                                         * @res
                                                                                         * "请先选择要审批的发票！"
                                                                                         */);
      setSelectedRowCount(0);
      showHintMessage("");
      return;
    }
    else if (validVOsVEC.size() > 0) {
      for (int i = 0; i < validVOsVEC.size(); i++) {
        if (PuPubVO.getString_TrimZeroLenAsNull(((InvoiceVO) validVOsVEC.get(i)).getHeadVO().getCinvoiceid()) == null) {
          MessageDialog.showHintDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON",
              "UPPSCMCommon-000270")/* @res "提示" */, nc.ui.ml.NCLangRes.getInstance().getStrByID("40040402",
              "UPP40040402-000003")/* @res "请先选择要审批的发票！" */);
          setSelectedRowCount(0);
          showHintMessage("");
          return;
        }
      }
    }

    // 得到需审批的VO及相应INDEX
    InvoiceVO[] proceVOs = new InvoiceVO[validVOsVEC.size()];
    validVOsVEC.copyInto(proceVOs);

    // 填审批人与审批日期
    String strOperPk = nc.ui.pub.ClientEnvironment.getInstance().getUser().getPrimaryKey();
    UFDate today = nc.ui.pub.ClientEnvironment.getInstance().getDate();

    // 回退审批人及审批日期哈希表，操作失败时用到
    HashMap<String, ArrayList<Object>> mapAuditInfo = new HashMap<String, ArrayList<Object>>();
    ArrayList<Object> listAuditInfo = null;

    // 审批日期不能小于制单日期检查
    ClientLink cl = new ClientLink(nc.ui.pub.ClientEnvironment.getInstance());// 环境信息
    String errMsg = PuTool.getAuditLessThanMakeMsg(proceVOs, "dinvoicedate", "vinvoicecode", cl.getLogonDate(),
        ScmConst.PO_Invoice);
    if (errMsg != null) {
      MessageDialog.showErrorDlg(this,
          nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPPSCMCommon-000270")/*
                                                                                         * @res
                                                                                         * "提示"
                                                                                         */, errMsg);
      showHintMessage("");
      return;
    }

    for (int i = 0; i < proceVOs.length; i++) {
      // 操作失败时用到，回退审批人及审批日期哈希表
      if (PuPubVO.getString_TrimZeroLenAsNull(proceVOs[i].getHeadVO().getCauditpsn()) != null) {
        listAuditInfo = new ArrayList<Object>();
        listAuditInfo.add(proceVOs[i].getHeadVO().getCauditpsn());
        listAuditInfo.add(proceVOs[i].getHeadVO().getDauditdate());
        mapAuditInfo.put(proceVOs[i].getHeadVO().getPrimaryKey(), listAuditInfo);
      }
      proceVOs[i].getHeadVO().setCauditpsn(strOperPk);
      proceVOs[i].getHeadVO().setDauditdate(today);
      proceVOs[i].getHeadVO().setCuserid(strOperPk);
      proceVOs[i].getHeadVO().setTaudittime((new UFDateTime(new Date())).toString());
      proceVOs[i].getHeadVO().setCoperatoridnow(getClientEnvironment().getUser().getPrimaryKey());
    }

    try {
      if (!loadItemsForInvoiceVOs(proceVOs)) {
        showHintMessage("");
        return;
      }
      PfUtilClient.processBatchFlow(this, "APPROVE", nc.vo.scm.pu.BillTypeConst.PO_INVOICE, ClientEnvironment
          .getInstance().getDate().toString(), proceVOs, null);
    }
    catch (Exception e) {
      for (int i = 0; i < proceVOs.length; i++) {
        proceVOs[i].getHeadVO().setCauditpsn(null);
        proceVOs[i].getHeadVO().setDauditdate(null);
        proceVOs[i].getHeadVO().setCuserid(null);
        proceVOs[i].getHeadVO().setTaudittime(null);
      }

      SCMEnv.out(e);
      String strErrMsg = e.getMessage();
      if (strErrMsg != null
          && strErrMsg
              .indexOf(nc.ui.ml.NCLangRes.getInstance().getStrByID("40040402", "UPP40040402-000019")/*
                                                                                                     * @res
                                                                                                     * "应付系统已结帐"
                                                                                                     */) >= 0) {
        MessageDialog.showHintDlg(this,
            nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000270")/*
                                                                                             * @res
                                                                                             * "提示"
                                                                                             */,
            nc.ui.ml.NCLangRes.getInstance().getStrByID("40040402", "UPP40040402-000004")/*
                                                                                           * @res
                                                                                           * "应付系统已结帐，发票不能审批！"
                                                                                           */);
      }
      else if (e instanceof BusinessException || e instanceof java.rmi.RemoteException) {
        MessageDialog.showHintDlg(this,
            nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000270")/*
                                                                                             * @res
                                                                                             * "提示"
                                                                                             */, e
                .getMessage());
      }
      else {
        MessageDialog.showHintDlg(this,
            nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000270")/*
                                                                                             * @res
                                                                                             * "提示"
                                                                                             */,
            nc.ui.ml.NCLangRes.getInstance().getStrByID("40040402", "UPP40040402-000005")/*
                                                                                           * @res
                                                                                           * "操作未成功"
                                                                                           */+ ":"
                + e.getMessage());
      }
      showHintMessage("");
      return;
    }
    finally {
      // 回退审批人及审批日期
      if (mapAuditInfo.size() > 0) {
        for (int i = 0; i < proceVOs.length; i++) {
          if (!mapAuditInfo.containsKey(proceVOs[i].getHeadVO().getPrimaryKey())) {
            proceVOs[i].getHeadVO().setCauditpsn(null);
            proceVOs[i].getHeadVO().setDauditdate(null);
            continue;
          }
          listAuditInfo = (ArrayList<Object>) mapAuditInfo.get(proceVOs[i].getHeadVO().getPrimaryKey());
          proceVOs[i].getHeadVO().setCauditpsn((String) listAuditInfo.get(0));
          proceVOs[i].getHeadVO().setDauditdate((UFDate) listAuditInfo.get(1));
        }
      }
    }

    if (PfUtilClient.isSuccess()) {

      // //从缓存中除去
      // Integer[] iaIndex = new Integer[validIndexVEC.size()] ;
      // validIndexVEC.copyInto(iaIndex) ;
      //
      // removeSomeFromInvVOs(iaIndex) ;
      // setVOsToListPanel() ;
      // 刷新
      // 业务日志
//      Operlog operlog = new Operlog();
//      for (InvoiceVO vo : proceVOs) {
//        vo.getOperatelogVO().setOpratorname(nc.ui.pub.ClientEnvironment.getInstance().getUser().getUserName());
//        vo.getOperatelogVO().setCompanyname(nc.ui.pub.ClientEnvironment.getInstance().getCorporation().getUnitname());
//        vo.getOperatelogVO().setOperatorId(nc.ui.pub.ClientEnvironment.getInstance().getUser().getPrimaryKey());
//        operlog.insertBusinessExceptionlog(vo, "审批", "审批", nc.vo.scm.funcs.Businesslog.MSGMESSAGE,
//            nc.vo.scm.pu.BillTypeConst.PO_INVOICE, nc.ui.sm.cmenu.Desktop.getApplet().getParameter("USER_IP"));
//      }
      onListRefresh();
      ConditionVO[] definedVOs = null;
      // 未查询直接点刷新
      if (definedVOs == null) {
        Hashtable<String, InvoiceVO> resultH = new Hashtable<String, InvoiceVO>();
        for (int i = 0; i < validIndexVEC.size(); i++) {
          try {
            resultH = InvoiceHelper.findByPrimaryKeyBantch(validHidKeyVEC);
          }
          catch (Exception e) {
            // TODO 自动生成 catch 块
            SCMEnv.out(e);
          }
          if (resultH != null
              && resultH.get(getInvVOs()[((Integer) validIndexVEC.get(i)).intValue()].getHeadVO().getPrimaryKey()) != null) {
            getInvVOs()[((Integer) validIndexVEC.get(i)).intValue()] = resultH.get(getInvVOs()[((Integer) validIndexVEC
                .get(i)).intValue()].getHeadVO().getPrimaryKey());
          }
        }
        setInvVOs(getInvVOs());

        // 显示列表当前所有VO,并对经过公式计算的值赋值
        setVOsToListPanel();

        // 设置卡片及列表的显示状态
        setBillBrowseState(STATE_BROWSE_NORMAL);
        setListOperState(STATE_LIST_NORMAL);
        setCurOperState(getListOperState());

      }
      showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UCH010"));
      return;
    }
    else {
      // 回退审批人及审批日期
      if (mapAuditInfo.size() > 0) {
        for (int i = 0; i < proceVOs.length; i++) {
          if (!mapAuditInfo.containsKey(proceVOs[i].getHeadVO().getPrimaryKey())) {
            proceVOs[i].getHeadVO().setCauditpsn(null);
            proceVOs[i].getHeadVO().setDauditdate(null);
            continue;
          }
          listAuditInfo = (ArrayList<Object>) mapAuditInfo.get(proceVOs[i].getHeadVO().getPrimaryKey());
          proceVOs[i].getHeadVO().setCauditpsn((String) listAuditInfo.get(0));
          proceVOs[i].getHeadVO().setDauditdate((UFDate) listAuditInfo.get(1));
        }
      }
    }

    showHintMessage("");
    return;
  }

  /**
   * 作者：王印芬 功能：弃审列表的一批选中的单据 不能弃审的条件：自由状态 参数：无 返回：无 例外：无 日期：(2002-3-13 11:39:21)
   * 修改日期，修改人，修改原因，注释标志： 2002-04-15 wyf 配合后端只进行TS判断是否单据改变，本代码加入一些如是否审批的判断
   * 2002-05-23 wyf 弃审后的发票从界面去掉
   */
  private void onListUnAudit() {
    // 得到被选中的行
    Vector validVOsVEC = new Vector();
    // 可以弃审的行
    Vector validIndexVEC = new Vector();
    for (int i = 0; i < getInvVOs().length; i++) {
      if (getBillListPanel().getHeadBillModel().getRowState(i) == BillModel.SELECTED) {
        // 有可能经过了排序，得到真正的表头INDEX
        int nCurIndex = PuTool.getIndexBeforeSort(getBillListPanel(), i);
        // 可以弃审的
        validIndexVEC.addElement(new Integer(nCurIndex));
        validVOsVEC.addElement(getInvVOs()[nCurIndex]);
      }
    }
    if (validVOsVEC.size() == 0) {
      MessageDialog.showHintDlg(this,
          nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000270")/*
                                                                                           * @res
                                                                                           * "提示"
                                                                                           */,
          nc.ui.ml.NCLangRes.getInstance().getStrByID("40040402", "UPP40040402-000006")/*
                                                                                         * @res
                                                                                         * "请先选择要弃审的发票！"
                                                                                         */);
      setSelectedRowCount(0);
      showHintMessage("");
      return;
    }

    InvoiceVO[] proceVOs = new InvoiceVO[validVOsVEC.size()];
    validVOsVEC.copyInto(proceVOs);

    String[] strAuditIDs = new String[proceVOs.length];

    // 回退审批人及审批日期哈希表，操作失败时用到
    HashMap<String, ArrayList<Object>> mapAuditInfo = new HashMap<String, ArrayList<Object>>();
    ArrayList<Object> listAuditInfo = null;

    try {
      // 配合审批流
      for (int i = 0; i < proceVOs.length; i++) {
        strAuditIDs[i] = proceVOs[i].getHeadVO().getCauditpsn();
        // 操作失败时用到，回退审批人及审批日期哈希表
        if (PuPubVO.getString_TrimZeroLenAsNull(strAuditIDs[i]) != null) {
          listAuditInfo = new ArrayList<Object>();
          listAuditInfo.add(proceVOs[i].getHeadVO().getCauditpsn());
          listAuditInfo.add(proceVOs[i].getHeadVO().getDauditdate());
          mapAuditInfo.put(proceVOs[i].getHeadVO().getPrimaryKey(), listAuditInfo);
        }
        // 赋操作员和原审批人ID：为判断是否允许弃审他人的单据
        proceVOs[i].getHeadVO().setCoperatoridnow(getClientEnvironment().getUser().getPrimaryKey());
        proceVOs[i].getHeadVO().setAttributeValue("cauditpsnold", proceVOs[i].getHeadVO().getCauditpsn());

        proceVOs[i].getHeadVO().setCauditpsn(nc.ui.pub.ClientEnvironment.getInstance().getUser().getPrimaryKey());
        proceVOs[i].getHeadVO().setCuserid(nc.ui.pub.ClientEnvironment.getInstance().getUser().getPrimaryKey());
        proceVOs[i].getHeadVO().setTaudittime(null);
      }
      if (!loadItemsForInvoiceVOs(proceVOs)) {
        showHintMessage("");
        return;
      }

      // 已结算的发票能否弃审控制:
      // IF 发票的业务类型核算规则为受托代销,直运采购, 或者为虚拟发票或者为自制发票, 不受控制
      // 否则 IF 发票来源于入库单而且根据入库开票能否结算参数为审批结算, 抛异常
      if (m_sZGYF == null || !(new UFBoolean(m_sZGYF).booleanValue())) {
        InvoiceVO invoiceVO = new InvoiceVO();
        for (int i = 0; i < proceVOs.length; i++) {
          invoiceVO = proceVOs[i];
          Object oTemp = CacheTool.getCellValue("bd_busitype", "pk_busitype", "verifyrule", invoiceVO.getHeadVO()
              .getCbiztype());
          if (oTemp == null)
            continue;
          Object oTemp1[] = (Object[]) oTemp;
          if (oTemp1[0].equals("S") || oTemp1[0].equals("Z"))
            continue;// 受托代销或直运采购业务类型
          if (invoiceVO.getHeadVO().getIinvoicetype().intValue() == 3)
            continue;// 虚拟发票
          InvoiceItemVO tempBodyVO[] = invoiceVO.getBodyVO();
          for (int j = 0; j < tempBodyVO.length; j++) {
            if (tempBodyVO[j].getCupsourcebilltype() == null)
              continue;// 自制采购发票
            if ((tempBodyVO[j].getCupsourcebilltype().equals("45") || tempBodyVO[j].getCupsourcebilltype().equals("47"))
                && m_sOrder2InvoiceSettleMode != null && m_sStock2InvoiceSettleMode.equals("审批时自动结算")) {
              if (tempBodyVO[j].getNaccumsettmny() != null && tempBodyVO[j].getNaccumsettmny().doubleValue() != 0.0) {
                MessageDialog.showHintDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401",
                    "UPPSCMCommon-000270")/* @res "提示" */, nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401",
                    "UPP40040401-000022")/* @res "该发票已结算，不能弃审！" */);
                showHintMessage("");
                return;
              }
            }
          }
        }
      }
      PfUtilClient.processBatch("UNAPPROVE", nc.vo.scm.pu.BillTypeConst.PO_INVOICE, ClientEnvironment.getInstance().getDate().toString(), proceVOs);
    }
    catch (Exception e) {
      String strErrMsg = e.getMessage();
      if (strErrMsg != null
          && (strErrMsg
              .indexOf(nc.ui.ml.NCLangRes.getInstance().getStrByID("40040402", "UPP40040402-000020")/*
                                                                                                     * @res
                                                                                                     * "后续"
                                                                                                     */) >= 0 || strErrMsg
              .indexOf(nc.ui.ml.NCLangRes.getInstance().getStrByID("40040402", "UPP40040402-000021")/*
                                                                                                     * @res
                                                                                                     * "不能删除"
                                                                                                     */) >= 0)) {
        strErrMsg = nc.ui.ml.NCLangRes.getInstance().getStrByID("40040402", "UPP40040402-000007")/*
                                                                                                   * @res
                                                                                                   * "采购发票弃审失败：单据存在后续处理或已经生成了实时凭证，不可弃审!"
                                                                                                   */;
        MessageDialog.showHintDlg(this,
            nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000270")/*
                                                                                             * @res
                                                                                             * "提示"
                                                                                             */, strErrMsg);
      }
      else if (e instanceof BusinessException) {
        MessageDialog.showHintDlg(this,
            nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000270")/*
                                                                                             * @res
                                                                                             * "提示"
                                                                                             */, strErrMsg);
      }
      else {
        strErrMsg = nc.ui.ml.NCLangRes.getInstance().getStrByID("40040402", "UPP40040402-000008")/*
                                                                                                   * @res
                                                                                                   * "弃审发失败!可能是运行环境原因，如：\n1)、网络未正确连接\n2)、NC服务器终断\n请确认环境正确后再次操作!"
                                                                                                   */;
        SCMEnv.out("!!! 以下信息可发给系统管理员参考：\n");
        SCMEnv.out(e);
        MessageDialog.showHintDlg(this,
            nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000270")/*
                                                                                             * @res
                                                                                             * "提示"
                                                                                             */, strErrMsg);
      }
      showHintMessage("");
      return;
    }
    finally {
      // 回退审批人及审批日期
      if (mapAuditInfo.size() > 0) {
        for (int i = 0; i < proceVOs.length; i++) {
          if (!mapAuditInfo.containsKey(proceVOs[i].getHeadVO().getPrimaryKey())) {
            proceVOs[i].getHeadVO().setCauditpsn(null);
            proceVOs[i].getHeadVO().setDauditdate(null);
            continue;
          }
          listAuditInfo = (ArrayList<Object>) mapAuditInfo.get(proceVOs[i].getHeadVO().getPrimaryKey());
          proceVOs[i].getHeadVO().setCauditpsn((String) listAuditInfo.get(0));
          proceVOs[i].getHeadVO().setDauditdate((UFDate) listAuditInfo.get(1));
        }
      }

    }

    if (PfUtilClient.isSuccess()) {
      // //从缓存中除去
      // Integer[] iaIndex = new Integer[validIndexVEC.size()] ;
      // validIndexVEC.copyInto(iaIndex) ;
      // removeSomeFromInvVOs(iaIndex) ;
      // setVOsToListPanel();
      // 业务日志
//      Operlog operlog = new Operlog();
//      for (InvoiceVO vo : proceVOs) {
//        vo.getOperatelogVO().setOpratorname(nc.ui.pub.ClientEnvironment.getInstance().getUser().getUserName());
//        vo.getOperatelogVO().setCompanyname(nc.ui.pub.ClientEnvironment.getInstance().getCorporation().getUnitname());
//        vo.getOperatelogVO().setOperatorId(nc.ui.pub.ClientEnvironment.getInstance().getUser().getPrimaryKey());
//        operlog.insertBusinessExceptionlog(vo, "弃审", "弃审", nc.vo.scm.funcs.Businesslog.MSGMESSAGE,
//            nc.vo.scm.pu.BillTypeConst.PO_INVOICE, nc.ui.sm.cmenu.Desktop.getApplet().getParameter("USER_IP"));
//      }
      // 刷新
      onListRefresh();

      showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UCH011"));
      return;
    }
    else {
      // 回退审批人及审批日期
      if (mapAuditInfo.size() > 0) {
        for (int i = 0; i < proceVOs.length; i++) {
          if (!mapAuditInfo.containsKey(proceVOs[i].getHeadVO().getPrimaryKey())) {
            proceVOs[i].getHeadVO().setCauditpsn(null);
            proceVOs[i].getHeadVO().setDauditdate(null);
            continue;
          }
          listAuditInfo = (ArrayList<Object>) mapAuditInfo.get(proceVOs[i].getHeadVO().getPrimaryKey());
          proceVOs[i].getHeadVO().setCauditpsn((String) listAuditInfo.get(0));
          proceVOs[i].getHeadVO().setDauditdate((UFDate) listAuditInfo.get(1));
        }
      }
    }
    showHintMessage("");
  }

  /**
   * 函数的功能、用途、对属性的更改，以及函数执行前后对象的状态。
   * 
   * @param 参数说明
   * @return 返回值
   * @exception 异常描述
   * @see 需要参见的其它内容
   * @since 从类的那一个版本，此方法被添加进来。（可选）
   * @deprecated 该方法从类的那一个版本后，已经被其它方法替换。（可选）
   */
  private void onBillCancel() {
    // 一张新的未经保存的发票
    // /ID_OK = 1;ID_CANCEL = 2;ID_YES = 4; ID_NO = 8;
    int result = MessageDialog.ID_YES;// MessageDialog.showYesNoCancelDlg(this,
                                      // nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401","UPPSCMCommon-000270")/*@res
                                      // "提示"*/,
                                      // nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401","UPP40040401-000162")/*@res
                                      // "是否放弃保存?"*/);
    if (result == MessageDialog.ID_YES) {
      // 单据转入状态
      int nVOStatus = 0;
      if (getInvVOs() != null){
        nVOStatus = getInvVOs()[getCurVOPos()].getSource();
      }
      try {
        if (getInvVOs() != null) {
          getInvVOs()[getCurVOPos()] = InvoiceHelper.findByPrimaryKey(getInvVOs()[getCurVOPos()].getHeadVO().getPrimaryKey());
          getInvVOs()[getCurVOPos()].setSource(InvoiceVO.FROM_QUERY);
        }
      }
      catch (Exception e) {
        SCMEnv.out(e);
      }

      // 不是查询出的单据从缓存中删除(修改状态除外)
      if (getInvVOs() != null && nVOStatus != InvoiceVO.FROM_QUERY) {
        removeOneFromInvVOs(getCurVOPos());
      }

      if (getInvVOs() == null) {
        setListOperState(STATE_LIST_NORMAL);
        setBillBrowseState(STATE_BROWSE_NORMAL);
      }

      // 从其他单据转入则转入列表
      if (nVOStatus != InvoiceVO.FROM_HAND && nVOStatus != InvoiceVO.FROM_QUERY) {
        // 设置列表界面数据
        setCurOperState(getListOperState());
        shiftShowModeTo(INV_PANEL_LIST);
        setVOsToListPanel();
        m_bCopy = false;
        return;
      }
      else {
        // 设置卡片界面数据
        if (getInvVOs() == null) {
          // V5 Del : setImageType(this.IMAGE_NULL);
          getBillCardPanel().addNew();
          getBillCardPanel().setEnabled(false);
          setCurOperState(getBillBrowseState());
        }
        else {
          if (getInvVOs() != null && getInvVOs().length > 0) {
            InvoiceVO curVO = getInvVOs()[getCurVOPos()];
            String id = curVO.getHeadVO().getCinvoiceid();
            if (id != null) {
              Object o = hBillStatusBeforeEdit.get(id);
              if (o != null) {
                Integer iBillStatus = new Integer(o.toString());
                if (iBillStatus.intValue() == 4) {
                  curVO.getHeadVO().setIbillstatus(new Integer(4));
                  hBillStatusBeforeEdit.put(id, new Integer(0));
                }
              }
            }
          }
          setCurOperState(getBillBrowseState());
          setVOToBillPanel();
        }
      }
    }
    else if (result == MessageDialog.ID_NO) {
      onSave();
    }
    m_bAdd = false;
    m_bCopy = false;
  }

  /**
   * 合并显示 创建日期：(2003-10-31 14:05:25)
   */
  private void onBillCombin() {
    CollectSettingDlg dlg = new CollectSettingDlg(this, NCLangRes.getInstance().getStrByID("common",
        "4004COMMON000000089")/* @res "合并显示" */, ScmConst.PO_Invoice, "40040401", getCorpId(), ClientEnvironment
        .getInstance().getUser().getPrimaryKey(), InvoiceVO.class.getName(), InvoiceHeaderVO.class.getName(),
        InvoiceItemVO.class.getName());
    //
    dlg.initData(getBillCardPanel(), new String[] {
        "invcode", "invname", "invspec", "invtype", "cproducearea", "noriginaltaxmny", "noriginalsummny"
    }, // 固定分组列
        null, // new String[]{"dplanarrvdate"},缺省分组列
        new String[] {
            "ninvoicenum", "noriginalcurprice", "noriginalcurmny", "noriginaltaxpricemny"
        },// 求和列
        null, new String[] {
            "noriginalcurprice", "norgnettaxprice"
        }, "ninvoicenum");
    dlg.showModal();
    // 更新当前缓存VO
    if(getBillCardPanel()!=null){
      if(getInvVOs()[getCurVOPos()].getPrimaryKey().equals(getCurVOonCard().getPrimaryKey()))
        getInvVOs()[getCurVOPos()]=getCurVOonCard();
    }
  }

  /**
   * 查询所有符合条件的发票
   * 
   * @param 参数说明
   * @return 返回值
   * @exception 异常描述
   * @see 需要参见的其它内容
   * @since 从类的那一个版本，此方法被添加进来。（可选）
   */

  // 暂时取出所有该单位的发票,并同时显示第一张
  private void onBillQuery() {
    //显示查询对话框
    getQueDlg();
    m_InvQueDlg.showModal();

    //设置数据权限:放在查询对话框的showModal()之后。因为此时的数据权限要取决于多公司页签所选择的公司. lixiaodong , v51
    m_InvQueDlg.setRefsDataPowerConVOs(getCurOperator(), new String[] {getPk_corp()}, IDataPowerForInv.REFNAMES, IDataPowerForInv.REFKEYS,
          IDataPowerForInv.RETURNTYPES);

    //得到查询条件
    if (m_InvQueDlg.isCloseOK()) {
      //设置业务类型
//      setOldBizeType(getCurBizeType());

      //执行查询
      execBillQuery(m_InvQueDlg);

      //设置是否曾经查过
      setEverQueryed(true);
    }
  }

  /**
   * 按上一次的查询条件重新查询
   * 
   * @param 参数说明
   * @return 返回值
   * @exception 异常描述
   * @see 需要参见的其它内容
   * @since 从类的那一个版本，此方法被添加进来。（可选）
   * @deprecated 该方法从类的那一个版本后，已经被其它方法替换。（可选）
   */
  private void onBillRefresh() {

    // 未销毁的对话框
    execBillQuery(getQueDlg());
  }

  /**
   * 此处插入方法说明。 功能：消息中心专用 参数： 返回： 例外： 日期：(2002-10-15 16:15:35)
   * 修改日期，修改人，修改原因，注释标志：
   */
  private void onBillUnAudit() {
    nc.vo.scm.pu.Timer timer = new nc.vo.scm.pu.Timer();
    timer.start("采购发票弃审开始");

    InvoiceVO[] proceVOs = new InvoiceVO[] {
      getInvVOs()[getCurVOPos()]
    };

    // 回退审批人及审批日期哈希表，操作失败时用到
    HashMap<String, ArrayList<Object>> mapAuditInfo = new HashMap<String, ArrayList<Object>>();
    ArrayList<Object> listAuditInfo = null;

    try {

      // 配合审批流
      for (int i = 0; i < proceVOs.length; i++) {
        // 操作失败时用到，回退审批人及审批日期哈希表
        if (PuPubVO.getString_TrimZeroLenAsNull(proceVOs[i].getHeadVO().getCauditpsn()) != null) {
          listAuditInfo = new ArrayList<Object>();
          listAuditInfo.add(proceVOs[i].getHeadVO().getCauditpsn());
          listAuditInfo.add(proceVOs[i].getHeadVO().getDauditdate());
          mapAuditInfo.put(proceVOs[i].getHeadVO().getPrimaryKey(), listAuditInfo);
        }
        // 赋操作员和原审批人ID：为判断是否允许弃审他人的单据
        proceVOs[i].getHeadVO().setCoperatoridnow(getClientEnvironment().getUser().getPrimaryKey());
        proceVOs[i].getHeadVO().setAttributeValue("cauditpsnold", proceVOs[i].getHeadVO().getCauditpsn());

        proceVOs[i].getHeadVO().setCauditpsn(nc.ui.pub.ClientEnvironment.getInstance().getUser().getPrimaryKey());
        proceVOs[i].getHeadVO().setCuserid(nc.ui.pub.ClientEnvironment.getInstance().getUser().getPrimaryKey());
      }
      if (!loadItemsForInvoiceVOs(proceVOs)) {
        showHintMessage("");
        return;
      }
      timer.addExecutePhase("动态加载表体loadItemsForInvoiceVOs");

      // 已结算的发票能否弃审控制:
      // IF 发票的业务类型核算规则为受托代销,直运采购, 或者为虚拟发票或者为自制发票, 不受控制
      // 否则 IF 发票来源于入库单而且根据入库开票能否结算参数为审批结算, 抛异常
      if (m_sZGYF == null || !(new UFBoolean(m_sZGYF).booleanValue())) {
        InvoiceVO invoiceVO = new InvoiceVO();
        for (int i = 0; i < proceVOs.length; i++) {
          invoiceVO = proceVOs[i];
          Object oTemp = CacheTool.getCellValue("bd_busitype", "pk_busitype", "verifyrule", invoiceVO.getHeadVO()
              .getCbiztype());
          if (oTemp == null)
            continue;
          Object oTemp1[] = (Object[]) oTemp;
          if (oTemp1[0].equals("S") || oTemp1[0].equals("Z"))
            continue;// 受托代销或直运采购业务类型
          if (invoiceVO.getHeadVO().getIinvoicetype().intValue() == 3)
            continue;// 虚拟发票
          InvoiceItemVO tempBodyVO[] = invoiceVO.getBodyVO();
          for (int j = 0; j < tempBodyVO.length; j++) {
            if (tempBodyVO[j].getCupsourcebilltype() == null)
              continue;// 自制采购发票
            if ((tempBodyVO[j].getCupsourcebilltype().equals("45") || tempBodyVO[j].getCupsourcebilltype().equals("47"))
                && m_sOrder2InvoiceSettleMode != null && m_sStock2InvoiceSettleMode.equals("审批时自动结算")) {
              if (tempBodyVO[j].getNaccumsettmny() != null && tempBodyVO[j].getNaccumsettmny().doubleValue() != 0.0) {
                MessageDialog.showHintDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401",
                    "UPPSCMCommon-000270")/* @res "提示" */, nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401",
                    "UPP40040401-000022")/* @res "该发票已结算，不能弃审！" */);
                showHintMessage("");
                return;
              }
            }
          }
        }
      }
      PfUtilClient.processBatch("UNAPPROVE" + nc.ui.pub.ClientEnvironment.getInstance().getUser().getPrimaryKey(),
          nc.vo.scm.pu.BillTypeConst.PO_INVOICE, ClientEnvironment.getInstance().getDate().toString(), proceVOs);

      //
      InvoiceVO resultVO = null;
      if (PfUtilClient.isSuccess()) {
//        resultVO = InvoiceHelper.findByPrimaryKey(getInvVOs()[getCurVOPos()].getHeadVO().getPrimaryKey());
        // 业务日志
//        resultVO.getOperatelogVO().setOpratorname(nc.ui.pub.ClientEnvironment.getInstance().getUser().getUserName());
//        resultVO.getOperatelogVO().setCompanyname(
//            nc.ui.pub.ClientEnvironment.getInstance().getCorporation().getUnitname());
//        resultVO.getOperatelogVO().setOperatorId(nc.ui.pub.ClientEnvironment.getInstance().getUser().getPrimaryKey());
//        Operlog operlog = new Operlog();
//        operlog.insertBusinessExceptionlog(resultVO, "弃审", "弃审", nc.vo.scm.funcs.Businesslog.MSGMESSAGE,
//            nc.vo.scm.pu.BillTypeConst.PO_INVOICE, nc.ui.sm.cmenu.Desktop.getApplet().getParameter("USER_IP"));
//        getInvVOs()[getCurVOPos()] = resultVO;
      }
      else {
        // 回退审批人及审批日期
        if (mapAuditInfo.size() > 0) {
          for (int i = 0; i < proceVOs.length; i++) {
            if (!mapAuditInfo.containsKey(proceVOs[i].getHeadVO().getPrimaryKey())) {
              proceVOs[i].getHeadVO().setCauditpsn(null);
              proceVOs[i].getHeadVO().setDauditdate(null);
              continue;
            }
            listAuditInfo = (ArrayList<Object>) mapAuditInfo.get(proceVOs[i].getHeadVO().getPrimaryKey());
            proceVOs[i].getHeadVO().setCauditpsn((String) listAuditInfo.get(0));
            proceVOs[i].getHeadVO().setDauditdate((UFDate) listAuditInfo.get(1));
          }
        }
      }

      timer.addExecutePhase("执行UNAPPROVE脚本");

      if (PfUtilClient.isSuccess()) {
        InvoiceHeaderVO headVO = getInvVOs()[getCurVOPos()].getHeadVO();
        ArrayList arrRet = InvoiceHelper.queryForSaveAudit(headVO.getPrimaryKey());
        headVO.setDauditdate((UFDate) arrRet.get(0));
        headVO.setCauditpsn((String) arrRet.get(1));
        headVO.setIbillstatus((Integer) arrRet.get(2));
        headVO.setTs((String) arrRet.get(3));
        headVO.setTaudittime((String) arrRet.get(4));

        timer.addExecutePhase("查询queryForSaveAudit操作");

        // 设置卡片界面数据
        setVOToBillPanel();
        // 更新按钮状态
        m_btnInvBillUnAudit.setEnabled(false);
        // setButtonsAndPanelState();

        timer.addExecutePhase("setVOToBillPanel");
        timer.showAllExecutePhase("采购发票弃审结束");
      }
    }
    catch (Exception e) {
      String strErrMsg = e.getMessage();
      if (strErrMsg != null
          && (strErrMsg
              .indexOf(nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPP40040401-000163")/*
                                                                                                     * @res
                                                                                                     * "后续"
                                                                                                     */) >= 0 || strErrMsg
              .indexOf(nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPP40040401-000164")/*
                                                                                                     * @res
                                                                                                     * "不能删除"
                                                                                                     */) >= 0)) {
        strErrMsg = nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPP40040401-000023")/*
                                                                                                   * @res
                                                                                                   * "采购发票弃审失败：单据存在后续处理或已经生成了实时凭证，不可弃审!"
                                                                                                   */;
        MessageDialog.showHintDlg(this,
            nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPPSCMCommon-000270")/*
                                                                                           * @res
                                                                                           * "提示"
                                                                                           */, strErrMsg);
      }
      else if (e instanceof BusinessException) {
        MessageDialog.showHintDlg(this,
            nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPPSCMCommon-000270")/*
                                                                                           * @res
                                                                                           * "提示"
                                                                                           */, strErrMsg);
      }
      else {
        strErrMsg = nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPP40040401-000024")/*
                                                                                                   * @res
                                                                                                   * "弃审发失败!可能是运行环境原因，如：\n1)、网络未正确连接\n2)、NC服务器终断\n请确认环境正确后再次操作!"
                                                                                                   */;
        SCMEnv.out("!!! 以下信息可发给系统管理员参考：\n");
        SCMEnv.out(e);
        MessageDialog.showHintDlg(this,
            nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPPSCMCommon-000270")/*
                                                                                           * @res
                                                                                           * "提示"
                                                                                           */, strErrMsg);
      }
      showHintMessage("");
      return;
    }
    finally {
      // 回退审批人及审批日期
      if (mapAuditInfo.size() > 0) {
        for (int i = 0; i < proceVOs.length; i++) {
          if (!mapAuditInfo.containsKey(proceVOs[i].getHeadVO().getPrimaryKey())) {
            proceVOs[i].getHeadVO().setCauditpsn(null);
            proceVOs[i].getHeadVO().setDauditdate(null);
            continue;
          }
          listAuditInfo = (ArrayList<Object>) mapAuditInfo.get(proceVOs[i].getHeadVO().getPrimaryKey());
          proceVOs[i].getHeadVO().setCauditpsn((String) listAuditInfo.get(0));
          proceVOs[i].getHeadVO().setDauditdate((UFDate) listAuditInfo.get(1));
        }
      }
    }

    showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UCH011"));
  }

  /**
   * 子类实现该方法，响应按钮事件。
   * 
   * @version (00-6-1 10:32:59)
   * @param bo
   *          ButtonObject
   */
  public void onButtonClicked(nc.ui.pub.ButtonObject btn) {
    // 二次开发插件支持 by zhaoyha at 2009.3.2
    try{
      getInvokeEventProxy().beforeButtonClicked(btn);
    }catch(Exception e){
      showErrorMessage(e.getMessage());
      return;
    }
    if (getCurPanelMode() == INV_PANEL_CARD) {
      onButtonClickedBill(btn);
    }
    else if (getCurPanelMode() == INV_PANEL_LIST) {
      onButtonClickedList(btn);
    }

    setButtonsAndPanelState();

    updateButtons();

    // 置光标到表头第一个可编辑项目
    if (m_isMakedBill)
      getBillCardPanel().transferFocusTo(BillCardPanel.HEAD);

    // 二次开发插件支持 by zhaoyha at 2009.3.2
    try{
      getInvokeEventProxy().afterButtonClicked(btn);
    }catch(Exception e){
      showErrorMessage(e.getMessage());
      return;
    }
  }

  /**
   * VMI消耗汇总处理VO对照
   * 
   * @version (00-6-1 10:32:59)
   * @param bo
   *          ButtonObject
   */
  private InvoiceVO[] processAfterChange(String busiType, AggregatedValueObject[] arySourceVOs) {
    // 进行分单并做一些后续处理
    InvoiceVO[] retVOs = getDistributedICVMISumVOs(arySourceVOs);
    return retVOs;
  }

  /**
   * 作者：王印芬 功能：对采购订单进行分单，默认按：业务类型、供应商、币种、库存组织 参数： 返回：无 例外：无 日期：(2001-10-13
   * 11:39:21) 修改日期，修改人，修改原因，注释标志： 2002-09-19 wyf 加入默认按库存组织分单
   */
  private InvoiceVO[] getDistributedICVMISumVOs(AggregatedValueObject[] arySourceVOs) {
    
    // 按分单方式得到所有发票
    Hashtable ordTable = new Hashtable();
    //是否需要设置采购默认辅单位
    boolean isTransPUAssUnit = false;
    SysInitVO para = null;
    try{
      para = nc.ui.pub.para.SysInitBO_Client.queryByParaCode(getCorpId(),"PO08");
    }catch(BusinessException e){
      SCMEnv.error(e);
    }
    if (para != null || para.getValue().equals("是")){
      isTransPUAssUnit = true;
    }
    int SourceVOsLength = arySourceVOs.length;
    nc.vo.ic.pub.vmi.VmiSumHeaderVO headVO = null;
    for (int i = 0; i < SourceVOsLength; i++) {

      // 得到属于第几张订单
      headVO = (VmiSumHeaderVO) arySourceVOs[i].getParentVO();
      // 供应商是VMI消耗汇总生成发票的默认分单方式
      String curKey = ((headVO.getCvendorid() == null || headVO.getCvendorid().trim().equals("")) ? "NULL" : headVO
          .getCvendorid());
      // 分单方式
      String isplitmode = ((headVO.getAttributeValue("isplitmode") == null || headVO.getAttributeValue("isplitmode")
          .toString().trim().equals("")) ? curKey : headVO.getAttributeValue("isplitmode").toString().trim());

      // 根据分单方式得到KEY依据
      if (isplitmode.trim().equals("1")) { // 供应商+存货
        curKey += headVO.getCinventoryid();
      }
      else if (isplitmode.trim().equals("2")) { // 消耗汇总记录
        curKey = headVO.getPrimaryKey();
      }
      // 加入到HASH表中
      if (!ordTable.containsKey(curKey)) {
        Vector vec = new Vector();
        vec.addElement(headVO);
        ordTable.put(curKey, vec);
      }
      else {
        Vector vec = (Vector) ordTable.get(curKey);
        vec.addElement(headVO);
      }

    }

    // 从哈希表中取出所有VO
    InvoiceVO[] allVOs = null;
    InvoiceHeaderVO invoiceHeadVO = null;
    InvoiceItemVO[] items = null;
    if (ordTable.size() > 0) {
      allVOs = new InvoiceVO[ordTable.size()];
      Enumeration elems = ordTable.keys();
      int i = 0;
      ClientLink cl = new ClientLink(nc.ui.pub.ClientEnvironment.getInstance());// 环境信息
      while (elems.hasMoreElements()) {
        Object curKey = elems.nextElement();
        Vector vec = (Vector) ordTable.get(curKey);

        VmiSumHeaderVO[] headVOs = new VmiSumHeaderVO[vec.size()];
        vec.copyInto(headVOs);

        int headVOsLength = headVOs.length;
        // 形成发票表头
        invoiceHeadVO = new InvoiceHeaderVO();
        // 发票日期
        invoiceHeadVO.setAttributeValue("dinvoicedate", cl.getLogonDate());
        // 扣税类别
        invoiceHeadVO.setIdiscounttaxtype(new Integer(1));

        // 票到日期
        invoiceHeadVO.setAttributeValue("darrivedate", cl.getLogonDate());
        // 公司
        invoiceHeadVO.setAttributeValue("pk_corp", ((headVOs[0].getAttributeValue("pk_corp") == null || headVOs[0]
            .getAttributeValue("pk_corp").toString().trim().equals("")) ? "" : headVOs[0].getAttributeValue("pk_corp")
            .toString().trim()));
        // 期初标志(发票标志)
        invoiceHeadVO.setAttributeValue("finitfalg", new Integer(0));
        // 业务类型
        invoiceHeadVO.setAttributeValue("cbiztype", getCurBizeType());
        // 发票类型(国内专用(0))
        invoiceHeadVO.setAttributeValue("iinvoicetype", new Integer(0));
        // 库存组织
        invoiceHeadVO.setAttributeValue("cstoreorganization", headVOs[0].getCcalbodyid());
        // 供应商(管理档案)
        invoiceHeadVO.setAttributeValue("cvendormangid", headVOs[0].getCvendorid());
        // 供应商(基本档案)
        invoiceHeadVO.setAttributeValue("cvendorbaseid", headVOs[0].getAttributeValue("cvendorbasid"));
        // 付款单位
        invoiceHeadVO.setAttributeValue("cpayunit", headVOs[0].getCvendorid());
        // 设置开户银行与银行帐号的值
        setDefaultBankAccountForAVendor(invoiceHeadVO.getCvendorbaseid(), invoiceHeadVO);
        // 付款协议
        String payTermId = (String) PiPqPublicUIClass.getAResultFromTable("bd_cumandoc", "pk_payterm", "pk_cumandoc",
            invoiceHeadVO.getCvendormangid());
        invoiceHeadVO.setAttributeValue("ctermprotocolid", payTermId);
        // 业务员
        String cemployeeid = (String) PiPqPublicUIClass.getAResultFromTable("bd_cumandoc", "pk_resppsn1",
            "pk_cumandoc", invoiceHeadVO.getCvendormangid());
        invoiceHeadVO.setAttributeValue("cemployeeid", cemployeeid);
        // 部门
        String cdeptid = (String) PiPqPublicUIClass.getAResultFromTable("bd_cumandoc", "pk_respdept1", "pk_cumandoc",
            invoiceHeadVO.getCvendormangid());
        invoiceHeadVO.setAttributeValue("cdeptid", cdeptid);
        // 币种
        String ccurrencytypeid = (String) PiPqPublicUIClass.getAResultFromTable("bd_cumandoc", "pk_currtype1",
            "pk_cumandoc", invoiceHeadVO.getCvendormangid());
        if (ccurrencytypeid == null || ccurrencytypeid.trim().length() == 0) 
          ccurrencytypeid=PiPqPublicUIClass.getNativeCurrencyID();// 消耗汇总只支持本位币？
        invoiceHeadVO.setAttributeValue("ccurrencytypeid",ccurrencytypeid); 
        
        // 制单人
        invoiceHeadVO.setAttributeValue("coperator", cl.getUser());
        // 单据状态
        invoiceHeadVO.setIbillstatus(new Integer(0));
        // 形成发票表体
        items = new InvoiceItemVO[headVOsLength];
        for (int k = 0; k < headVOsLength; k++) {
          items[k] = new InvoiceItemVO();
        }
        // 本期出库

        // 本期出库退回

        // 累计开票数量

        for (int j = 0; j < headVOsLength; j++) {

          // 存货(管理档案)
          items[j].setCbaseid(((headVOs[j].getAttributeValue("cinvbasdocid") == null || headVOs[j].getAttributeValue(
              "cinvbasdocid").toString().trim().equals("")) ? "" : headVOs[j].getAttributeValue("cinvbasdocid")
              .toString().trim()));
          // 存货(基本档案)
          items[j].setCmangid(((headVOs[j].getAttributeValue("cinventoryid") == null || headVOs[j].getAttributeValue(
              "cinventoryid").toString().trim().equals("")) ? "" : headVOs[j].getAttributeValue("cinventoryid")
              .toString().trim()));
          // 默认采购发票数量
          items[j].setNinvoicenum(((headVOs[j].getAttributeValue("ninvoicenum") == null || headVOs[j]
              .getAttributeValue("ninvoicenum").toString().trim().equals("")) ? new UFDouble(0) : new UFDouble(
              headVOs[j].getAttributeValue("ninvoicenum").toString().trim())));
          // 币种
          items[j].setCcurrencytypeid(ccurrencytypeid);
          // 折本折辅汇率
          UFDouble[] daRate = null;
          String strCurrDate = invoiceHeadVO.getAttributeValue("dinvoicedate").toString();
          if (strCurrDate == null || strCurrDate.trim().length() == 0) {
            strCurrDate = PoPublicUIClass.getLoginDate() + "";
          }
          daRate = m_cardPoPubSetUI2.getBothExchRateValue(getPk_corp(), ccurrencytypeid, new UFDate(strCurrDate));
          items[j].setNexchangeotobrate(daRate[0]);
          // items[j].setNexchangeotoarate(daRate[1]);

          // 来源单据类型
          items[j].setCsourcebilltype("50");
          // 来源单据ID
          items[j].setCsourcebillid(headVOs[j].getPrimaryKey());
          // 来源单据行ID
          items[j].setCsourcebillrowid(null);
          // 上层来源单据类型
          items[j].setCupsourcebilltype("50");
          // 上层来源单据ID
          items[j].setCupsourcebillid(headVOs[j].getPrimaryKey());
          // 上层来源单据行ID
          items[j].setCupsourcebillrowid(null);

          items[j].setCupsourcehts(headVOs[j].getTs());
          items[j].setCupsourcebts(headVOs[j].getTs());
          items[j].setCwarehouseid(headVOs[j].getCwarehouseid());
          //
          items[j].setVfree1(headVOs[j].getVfree1());
          items[j].setVfree2(headVOs[j].getVfree2());
          items[j].setVfree3(headVOs[j].getVfree3());
          items[j].setVfree4(headVOs[j].getVfree4());
          items[j].setVfree5(headVOs[j].getVfree5());
          //批次号
          items[j].setVproducenum(headVOs[j].getVlot());
        }

        allVOs[i] = new InvoiceVO();
        allVOs[i].setParentVO(invoiceHeadVO);
        allVOs[i].setChildrenVO(items);
        allVOs[i].setSource(InvoiceVO.FROM_VMI);
        // 更新行号设置
        BillRowNo.setVORowNoByRule(allVOs[i], ScmConst.PO_Invoice, "crowno");
        // 取计划价
        String sCstoreorganization = allVOs[i].getHeadVO().getCstoreorganization();
        int size = allVOs[i].getChildrenVO().length;
        String[] sMangIds = new String[size];
        for (int p = 0; p < size; p++) {
          sMangIds[p] = items[p].getCmangid();
        }
        // 上游转单至发票带出存货的默认税率
        UFDouble[] uPrice = queryPlanPrices(sMangIds, sCstoreorganization);
        if (uPrice != null) {
          for (int p = 0; p < size; p++) {
            items[p].setNplanprice(uPrice[p]);
          }
        }
        i++;
      }
    }
    //设置默认辅计量单位
    if(isTransPUAssUnit ){
      for(int i  = 0 ; i < allVOs.length ; i ++){
        for(int j  = 0 ; j < allVOs[i].getBodyLen() ; j++){
          if(PuTool.isAssUnitManaged(allVOs[i].getBodyVO()[j].getCbaseid())){
            String cassistunit = PuTool.getDefaultPUAssUnit(allVOs[i].getBodyVO()[j].getCbaseid());
            if(PuPubVO.getString_TrimZeroLenAsNull(cassistunit) != null
                && PuPubVO.getUFDouble_ZeroAsNull(allVOs[i].getBodyVO()[j].getNinvoicenum()) != null){
              allVOs[i].getBodyVO()[j].setCassistunit(cassistunit);
              UFDouble ufdConv = PuTool.getInvConvRateValue(allVOs[i].getBodyVO()[j].getCbaseid(), cassistunit);
              allVOs[i].getBodyVO()[j].setNexchangerate(ufdConv);
              allVOs[i].getBodyVO()[j].setNassistnum(allVOs[i].getBodyVO()[j].getNinvoicenum().div(ufdConv));
            }
          }
        }
      }
    }
    return allVOs;
  }

  /**
   * 提供公式,从该公式得到查询结果 参考BillModel.execloadFormula(int)写出
   * 
   * @param 参数说明
   * @return 返回值
   * @exception 异常描述
   * @see 需要参见的其它内容
   * @since 从类的那一个版本，此方法被添加进来
   * @return java.lang.String
   */
  private void setDefaultBankAccountForAVendor(String strVendorBase, InvoiceHeaderVO invoiceHeadVO) {

    if (strVendorBase == null || strVendorBase.trim().equals("")) {
      return;
    }

    Object[][] retOb = CacheTool.getMultiColValue("bd_custbank", "pk_cubasdoc", new String[] {
        "pk_custbank", "account", "defflag"
    }, new String[] {
      strVendorBase
    });
    Vector v1 = new Vector(), v2 = new Vector();
    if (retOb != null && retOb.length > 0 && retOb[0] != null && retOb[0].length > 1) {
      for (int i = 0; i < retOb.length; i++) {
        if (retOb[i][2] != null && retOb[i][2].equals("Y")) {
          v1.add(retOb[i][0]);
          v2.add(retOb[i][1]);
        }
      }
    }

    // added by fangy 2002-10-22 18:34 begin
    //UIRefPane pane = (UIRefPane) getBillCardPanel().getHeadItem("caccountbankid").getComponent();
    //((AccountsForVendorRefModel) pane.getRef().getRefModel()).setCvendorbaseid(strVendorBase);
    // added by fangy 2002-10-22 18:34 begin

    if (v1.size() == 0) {
      // 开户银行参照
      // UIRefPane pane =
      // (UIRefPane)getInvBillPanel().getHeadItem("caccountbankid").getComponent()
      // ;
      // ((AccountsForVendorRefModel)pane.getRef().getRefModel()).setCvendorbaseid(null)
      // ;
      // 设置参照主键:开户银行
      invoiceHeadVO.setAttributeValue("caccountbankid", null);
      // 帐号
      invoiceHeadVO.setAttributeValue("cvendoraccount", null);
    }
    else {
      // 开户银行参照
      // UIRefPane pane =
      // (UIRefPane)getInvBillPanel().getHeadItem("caccountbankid").getComponent()
      // ;
      // ((AccountsForVendorRefModel)pane.getRef().getRefModel()).setCvendorbaseid(strVendorBase)
      // ;
      // 设置参照主键:开户银行
      invoiceHeadVO.setAttributeValue("caccountbankid", (String) v1.elementAt(0));
      // 帐号
      invoiceHeadVO.setAttributeValue("cvendoraccount", (String) v1.elementAt(0));
    }

  }

  /**
   * 子类实现该方法，响应按钮事件。
   * 
   * @version (00-6-1 10:32:59)
   * @param btn
   *          ButtonObject 修改日期，修改人，修改原因，注释标志： 2003-02-20 方益,委外订单表头币种无法带出.
   */
  private void onButtonClickedBill(nc.ui.pub.ButtonObject btn) {
    if (btn == m_btnReSortRowNo) {
      onReSortRowNo();
    }
    else if (btn == m_btnCardEdit) {
      onCardEdit();
    }
    else if (btn == m_btnInvBillPasteRowTail) {
      // 粘贴行
      showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "SCMCOMMON000000266"));
      onPasteLineToTail();
      showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "SCMCOMMON000000266"));
    }
    // ================业务类型
    else if (btn.getParent() == m_btnInvBillBusiType) {
      showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPPSCMCommon-000293")/*
                                                                                                     * @res
                                                                                                     * "选择业务类型"
                                                                                                     */);

      setCurOperState(STATE_BROWSE_NORMAL);

      // 当前业务类型
      // setOldBizeType(getCurBizeType());
      setCurBizeType(btn.getTag());
      m_strBtnTag = btn.getTag();
      // 设置状态条显示
      StringBuffer sbufHint = new StringBuffer("");
      setHeadHintText(sbufHint.toString());

      // 业务类型导出来源单据类型
      PfUtilClient.retAddBtn(m_btnInvBillNew, nc.ui.pub.ClientEnvironment.getInstance().getCorporation()
          .getPrimaryKey(), nc.vo.scm.pu.BillTypeConst.PO_INVOICE, btn);
      btn.setSelected(true);
      m_btnInvBillBusiType.setEnabled(true);
      //
      setButtons(m_btnTree.getButtonArray());
  //        setButtons(getBtnss());
      m_bizButton = btn;
      updateButtons();
    }
    else if (btn.getParent() == m_btnInvBillNew || btn.getParent() == btnBillAddContinue) {
      showHintMessage(getHeadHintText() + nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UCH028")/*
                                                                                                           * @res
                                                                                                           * "增加发票"
                                                                                                           */);

      //自制单据
      if (btn.getTag() != null && btn.getTag().indexOf("makeflag") >= 0) {
        setCurOperState(STATE_EDIT);
        // 当前业务类型
        if(m_strBtnTag!=null) setCurBizeType(m_strBtnTag);
        onAdd();

        m_isMakedBill = true;

        // 采购发票自制时，采购公司默认为当前登录公司
        getBillCardPanel().getHeadItem("pk_purcorp").setValue(getPk_corp());
      }
      else {
        // 获取来源单据类型
        String tag = btn.getTag();
        int index = tag.indexOf(":");
        String strUpBillType = tag.substring(0, index);// 来源单据类型
        // 如果来源单据类型是：“50”(根据消耗汇总记录生成采购发票时)，则作如下处理
        if ("50".equalsIgnoreCase(strUpBillType)) {
          PfUtilClient.childButtonClicked(btn, getCorpPrimaryKey(), "400404", nc.ui.pub.ClientEnvironment.getInstance()
              .getUser().getPrimaryKey(), ScmConst.PO_Invoice, this);
        }
      

        // if(m_bizButton!=null&&m_bizButton.getName()!=null&&m_bizButton.getName().equals("")){
        else {
        	  if ("D1".equalsIgnoreCase(strUpBillType)||"F1".equalsIgnoreCase(strUpBillType)){
                  feeFlag = true;  //费用发票标志 add by QuSida (佛山骏杰) 2010-9-26
              }
          PfUtilClient.childButtonClicked(btn, nc.ui.pub.ClientEnvironment.getInstance().getCorporation().getPk_corp(),
              getModuleCode(), nc.ui.pub.ClientEnvironment.getInstance().getUser().getPrimaryKey(),
              nc.vo.scm.pu.BillTypeConst.PO_INVOICE, this);
        }
        if (PfUtilClient.isCloseOK()) {
          InvoiceVO[] vos = null;
          if ("50".equalsIgnoreCase(strUpBillType)) {
            AggregatedValueObject[] arySourceVOs = PfUtilClient.getRetVos();
            vos = processAfterChange("50", arySourceVOs);// 处理VO对照
            for (int i = 0; i < vos.length; i++) {
              if (PuPubVO.getString_TrimZeroLenAsNull(vos[i].getHeadVO().getPk_purcorp()) == null) {// 采购公司为空则默认为当前登录公司
                vos[i].getHeadVO().setPk_purcorp(getPk_corp());
              }
            }
          }
          else {
            vos = (InvoiceVO[]) PfUtilClient.getRetVos();
            if (vos == null) {
              MessageDialog.showHintDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401",
                  "UPPSCMCommon-000270")/* @res "提示" */, nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401",
                  "UPP40040401-000003")/* @res "单据转换失败！" */);
              return;
            }
            else {
              BillItem item = getBillListPanel().getBodyBillModel().getItemByKey("nplanprice");
              for (int i = 0; i < vos.length; i++) {

                if (PuPubVO.getString_TrimZeroLenAsNull(vos[i].getHeadVO().getPk_purcorp()) == null) {// 采购公司为空则默认为当前登录公司
                  vos[i].getHeadVO().setPk_purcorp(getPk_corp());
                }
                // 更新币种和行号设置
                BillRowNo.setVORowNoByRule(vos[i], ScmConst.PO_Invoice, "crowno");
                setHeadCurrency(vos[i]);
                try {
                  InvoiceItemVO[] items = vos[i].getBodyVO();
                  // 计算本币数据
                  computeValueFrmOtherBill(vos[i]);

                  // 供应商管理ID
                  String sCvendormangid = vos[i].getHeadVO().getCvendormangid();
                  // 收付款协议
                  String payTermId = vos[i].getHeadVO().getCtermprotocolid();
                  if (payTermId == null || payTermId.trim().length() < 1) {
                    Object oTemp = CacheTool.getCellValue("bd_cumandoc", "pk_cumandoc", "pk_payterm", sCvendormangid);
                    if (oTemp != null)
                      vos[i].getHeadVO().setCtermprotocolid(oTemp.toString());
                  }
                  // 收付款单位
                  String sPayUnit = vos[i].getHeadVO().getCpayunit();
                  // 丰原生化项目问题：修改发票上的供应商为B单位，保存签字后，生成应付单上的供应商还是是A单位
                  // if (sPayUnit == null || sPayUnit.trim().length() == 0) {
                  vos[i].getHeadVO().setCpayunit(sCvendormangid);
                  // }
                  // 取计划价
                  if (item != null && (item.isShow())) {
                    String sCstoreorganization = vos[i].getHeadVO().getCstoreorganization();
                    int size = items.length;
                    String[] sMangIds = new String[size];
                    for (int j = 0; j < size; j++) {
                      sMangIds[j] = items[j].getCmangid();
                    }
                    UFDouble[] uPrice = queryPlanPrices(sMangIds, sCstoreorganization);
                    if (uPrice != null) {
                      for (int j = 0; j < size; j++) {
                        items[j].setNplanprice(uPrice[j]);
                      }
                    }
                  }
                }
                catch (Exception ex) {
                  reportException(ex);
                  PuTool.outException(this, ex);
                }
              }
              if (BillTypeConst.PO_ORDER.equalsIgnoreCase(strUpBillType)
                  || BillTypeConst.STORE_PO.equalsIgnoreCase(strUpBillType)) {
                resetStoreorgAndStordoc(vos, strUpBillType);
              }

            }
          }

          //setCurVOPos(0);
          sContinueBillTypeName = btn.getName();
          if (btn.getParent() == m_btnInvBillNew) {
            setInvVOs(vos);
          }
          else if (btn.getParent() == btnBillAddContinue) {

            AggregatedValueObject[] arySourceVOs = SourceRefDlg.getRetsVos();

            // 追加的表体数据
            Vector<InvoiceItemVO> continueItemVO = new Vector<InvoiceItemVO>();
            if (arySourceVOs != null && arySourceVOs.length > 0) {

              InvoiceHeaderVO voCur = getCurVOonCard().getHeadVO();// 当前卡片上的表头数据
              InvoiceHeaderVO newvo = null;
              try {
                newvo = (InvoiceHeaderVO) ObjectUtils.serializableClone(voCur);
              }
              catch (Exception e) {
                SCMEnv.out(e);
              }

              BillItem[] headItems = getBillCardPanel().getHeadShowItems();// 必输项目的itemkey

              String[] headNotNullKeys = null;// 非空单据头ItemKey
              Vector<String> v1 = new Vector<String>();
              for (int i = 0; headItems != null && i < headItems.length; i++) {
                if (headItems[i].isNull()) {
                  v1.add(headItems[i].getKey());
                }
              }
              if (v1.size() > 0) {
                headNotNullKeys = new String[v1.size()];
                v1.copyInto(headNotNullKeys);
              }

              for (int i = 0; i < arySourceVOs.length; i++) {
                boolean bRight = true;
                for (int j = 0; j < headNotNullKeys.length; j++) {
                  if (arySourceVOs[i].getParentVO().getAttributeValue(headNotNullKeys[j]) != null
                      && !(arySourceVOs[i].getParentVO().getAttributeValue(headNotNullKeys[j]) instanceof UFDate)
                      && !arySourceVOs[i].getParentVO().getAttributeValue(headNotNullKeys[j]).equals(
                          newvo.getAttributeValue(headNotNullKeys[j]))) {
                    bRight = false;
                    break;// 新表头与原表头必输项不一致则不能参照增行
                  }
                }
                if (bRight) {
                  InvoiceVO invoiceVO = (InvoiceVO) arySourceVOs[i];
                  if (getInvVOs() != null && getInvVOs()[getCurVOPos()] != null) {
                    InvoiceItemVO[] items = (InvoiceItemVO[]) getInvVOs()[getCurVOPos()].getChildrenVO();
                    for (int j = 0; j < items.length; j++) {// 界面上原来的行
                      continueItemVO.add(items[j]);
                    }
                    for (int jj = 0; jj < invoiceVO.getChildrenVO().length; jj++) {// 新追加的行
                      ((InvoiceItemVO[]) invoiceVO.getChildrenVO())[jj].setCrowno(null);
                      continueItemVO.add(((InvoiceItemVO[]) invoiceVO.getChildrenVO())[jj]);
                    }
                  }
                }
              }

            }
            if (continueItemVO == null || continueItemVO.size() < 1) {
              return;
            }
            getInvVOs()[getCurVOPos()].setChildrenVO((InvoiceItemVO[]) continueItemVO
                .toArray(new InvoiceItemVO[continueItemVO.size()]));
            // 更新币种和行号设置
            BillRowNo.setVORowNoByRule(getInvVOs()[getCurVOPos()], ScmConst.PO_Invoice, "crowno");
          }
          // 显示发票
          setListOperState(STATE_LIST_FROM_BILLS);
          // setBillBrowseState(STATE_BROWSE_FROM_BILL) ;
          setCurOperState(getListOperState());
          shiftShowModeTo(INV_PANEL_LIST);
          // 设置列表界面的数据 
          //by zhaoyha at 2009.6.16流量修改，多个单据时才转到列表，只有一个时直接到卡片
          if (vos != null && vos.length > 1) {
            setVOsToListPanel();
          }

          m_vSavedVO = new Vector();

          if (vos != null && vos.length == 1) {
            onListBill();
          }
//          if("D1".equals(strUpBillType)){
//        	  getBillCardPanel().getHeadItem("nexchangeotobrate").setEnabled(true);
//          }
        }
      }
    }
    // 修改
    else if (btn == m_btnInvBillModify) {// 卡片下修改
      showHintMessage(getHeadHintText() + nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UCH027")/*
                                                                                                           * @res
                                                                                                           * "修改发票"
                                                                                                           */);
      setCurOperState(STATE_EDIT);
      onModify();
      // //审批
      m_btnInvBillAudit.setEnabled(false);
      updateButtons();

      // 置光标到表头第一个可编辑项目
      getBillCardPanel().transferFocusTo(BillCardPanel.HEAD);
    }
    // 保存
    else if (btn == m_btnInvBillSave) {
      // showHintMessage(getHeadHintText() +
      // nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UCH044")/*@res
      // "保存发票"*/);

      boolean bSucceed = onSave();

      if (bSucceed) {
        showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UCH005")/*
                                                                                         * res@
                                                                                         * "保存成功"
                                                                                         */);
      }
      else {
        showHintMessage("");
      }
    }
    // 作废
    else if (btn == m_btnInvBillDiscard) {
      showHintMessage(getHeadHintText() + nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UCH051")/*
                                                                                                           * @res
                                                                                                           * "作废发票"
                                                                                                           */);

      onDiscard();
    }
    // 放弃
    else if (btn == m_btnInvBillCancel) {
      showHintMessage(getHeadHintText() + nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UCH045")/*
                                                                                                           * @res
                                                                                                           * "取消发票保存"
                                                                                                           */);

      onBillCancel();
      showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UCH008")/*
                                                                                       * res@
                                                                                       * "取消成功"
                                                                                       */);
    }
    // ================行操作
    else if (btn == m_btnInvBillAddRow) {
      // 增行
      showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UCH056"));
      onAppendLine();
      showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UCH036"));
    }
    else if (btn == m_btnInvBillDeleteRow) {
      // 删行
      showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UCH057"));
      onDeleteLine();
      showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UCH037"));
    }
    else if (btn == m_btnInvBillInsertRow) {
      // 插行
      showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UCH058"));
      onInsertLine();
      showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UCH038"));
    }
    else if (btn == m_btnInvBillCopyRow) {
      // 复制行
      showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UCH059"));
      onCopyLine();
      showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UCH039"));
    }
    else if (btn == m_btnInvBillPasteRow) {
      // 粘贴行
      showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UCH060"));
      onPasteLine();
      showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UCH040"));
    }
    // ================页操作
    else if (btn == m_btnInvBillGoFirstOne) {
      // 首张
      showHintMessage(getHeadHintText() + nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UCH031")/*
                                                                                                           * @res
                                                                                                           * "浏览发票"
                                                                                                           */);
      setCurOperState(getBillBrowseState());
      onFirst();
    }
    else if (btn == m_btnInvBillGoLastOne) {
      // 末张
      showHintMessage(getHeadHintText() + nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UCH032")/*
                                                                                                           * @res
                                                                                                           * "浏览发票"
                                                                                                           */);
      setCurOperState(getBillBrowseState());
      onLast();
    }
    else if (btn == m_btnInvBillGoPreviousOne) {
      // 上张
      showHintMessage(getHeadHintText() + nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UCH033")/*
                                                                                                           * @res
                                                                                                           * "浏览发票"
                                                                                                           */);
      setCurOperState(getBillBrowseState());
      onPrevious();
    }
    else if (btn == m_btnInvBillGoNextOne) {
      // 下张
      showHintMessage(getHeadHintText() + nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UCH034")/*
                                                                                                           * @res
                                                                                                           * "浏览发票"
                                                                                                           */);

      setCurOperState(getBillBrowseState());
      onNext();
    }
    // 列表
    else if (btn == m_btnInvShift) {
      showHintMessage(getHeadHintText() + nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UCH055")/*
                                                                                                           * @res
                                                                                                           * "发票列表状态"
                                                                                                           */);

      setCurOperState(getListOperState());
      onList();
    }
    // 查询
    else if (btn == m_btnInvBillQuery) {
      showHintMessage(getHeadHintText() + nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UCH046")/*
                                                                                                           * @res
                                                                                                           * "查询发票"
                                                                                                           */);

      setCurOperState(STATE_BROWSE_NORMAL);
      onBillQuery();
      showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UCH009"));
    }
    // 卡片下复制
    else if (btn == m_btnInvBillCopy) {
      showHintMessage(getHeadHintText() + nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPP40040401-000035")/*
                                                                                                                         * @res
                                                                                                                         * "复制发票"
                                                                                                                         */);

      setCurOperState(STATE_EDIT);
      onCopy();
    }
    // 预览
    else if (btn == m_btnInvBillPreview) {
      showHintMessage(getHeadHintText() + nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UCH061")/*
                                                                                                           * @res
                                                                                                           * "打印发票"
                                                                                                           */);
      setCurOperState(getBillBrowseState());
      // 打印预览并计算打印次数
      onCardPrintPreview();
      // PuTool.onPreview(getPrintEntry(), new
      // nc.ui.rc.pub.PurchasePrintDS(getModuleCode(), getInvBillPanel()));
      showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UCH041"));
    }
    // 打印
    else if (btn == m_btnInvBillPrint) {
      showHintMessage(getHeadHintText() + nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPP40040401-000036")/*
                                                                                                                         * @res
                                                                                                                         * "打印发票"
                                                                                                                         */);
      setCurOperState(getBillBrowseState());
      // 打印并计算打印次数
      onCardPrint();
      // PuTool.onPrint(getPrintEntry(), new
      // nc.ui.rc.pub.PurchasePrintDS(getModuleCode(), getInvBillPanel()));
      showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UCH041"));
    }

    // 刷新
    else if (btn == m_btnInvBillRefresh) {
      showHintMessage(getHeadHintText() + nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UCH046")/*
                                                                                                           * @res
                                                                                                           * "查询发票"
                                                                                                           */);

      setCurOperState(STATE_BROWSE_NORMAL);
      onBillRefresh();
      showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UCH007"));
    }
    else if (btn == m_btnLnkQuery) {// 卡片下联查
      // showHintMessage(getHeadHintText() + CommonConstant.SPACE_MARK +"查询发票" +
      // CommonConstant.SPACE_MARK ) ;
      int preState=m_nCurOperState;
      onLnkQuery();
      setCurOperState(preState);
    }
    else if (btn == m_btnInvBillAudit) {
      // 卡片下审批
      showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UCH048"));
      onBillAudit();
      setCurOperState(STATE_BROWSE_NORMAL);
    }
    else if (btn == m_btnInvBillUnAudit) {
      // 卡片下弃审
      showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UCH049"));
      onBillUnAudit();
    }
    else if (btn == m_btnCrtAPBill) {
      onCrtAPBill();
    }
    else if (btn == m_btnDelAPBill) {
      onDelAPBill();
    }
    else if (btn == m_btnDocManage) {
      showHintMessage(getHeadHintText()
          + nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPPSCMCommon-000278")/*
                                                                                           * @res
                                                                                           * "文档管理"
                                                                                           */);
      // 文档管理
      onDocManage();

    }
    else if (btn == m_btnHqhp) {
      // 优质优价取价
      onHqhp();
    }

    // 送审
    else if (btn == m_btnSendAudit) {
      showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UCH047"));
      boolean bContinue = false;
      if (getInvVOs() != null && getInvVOs()[getCurVOPos()] != null) {
        if (m_bAdd) {
          bContinue = onSendAudit(null);
        }
        else {
          bContinue = onSendAudit(getInvVOs()[getCurVOPos()]);
        }
      }
      else {
        bContinue = onSendAudit(null);
      }
      if (!bContinue) {
        return;
      }

      if (getInvVOs() != null && getInvVOs()[getCurVOPos()] != null) {
        // setButtonsAndPanelState() ;
      }
      else if (m_bAdd) {
        boolean isNeedSendToAuditQ = BusiBillManageTool.isNeedSendToAudit(ScmConst.PO_Invoice, getPk_corp(),
            getCurBizeType(), null, getCurOperator());

        Integer iBillStatus = new Integer(88);
        if (getInvVOs() != null) {
          InvoiceVO vo = getInvVOs()[getCurVOPos()];

          if (vo != null && vo.getHeadVO() != null) {
            iBillStatus = vo.getHeadVO().getIbillstatus();
          }
        }

        if (isNeedSendToAuditQ
            && (iBillStatus.compareTo(nc.vo.scm.pu.BillStatus.FREE) == 0 || (iBillStatus
                .compareTo(nc.vo.scm.pu.BillStatus.AUDITFAIL) == 0))) {
          m_btnSendAudit.setEnabled(true);
        }
      }
      try {
        // InvoiceVO resultVO =
        // InvoiceHelper.findByPrimaryKey(getInvVOs()[getCurVOPos()].getHeadVO().getPrimaryKey());
        // getInvVOs()[getCurVOPos()] = resultVO;
      }
      catch (Exception e) {
        SCMEnv.out(e);
      }

      setVOToBillPanel();
      if (getCurOperState() != STATE_LIST_FROM_BILLS)
        setCurOperState(STATE_BROWSE_NORMAL);
      showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UCH023"));
    }
    else if (btn == btnBillCombin)// 卡片下合并显示
      onBillCombin();
    else if (btn == m_btnAudit) {
      showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UCH048"));
      onAudit();
    }
    else if (btn == m_btnQueryForAudit)
      onQueryForAudit();
    else if (btn == m_btnUnAudit) {
      showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UCH049"));
      onUnAudit();
    }
    else if (PuTool.isExist(getExtendBtns(), btn)) {
      onExtendBtnsClick(btn);
    }
    else if (btn == m_btnInvBillConversion) {
      showHintMessage(getHeadHintText() + nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPP40040401-000039")/*
                                                                                                                         * @res
                                                                                                                         * "放弃转单"
                                                                                                                         */);
      // 放弃转单
      onGiveupBillConversion();
    }
    

  }

  /*
   * 获得计划价
   */
  private UFDouble[] queryPlanPrices(String cMangID[], String pk_calbody) {
    if (cMangID == null || cMangID.length == 0)
      return null;
    UFDouble nPrice[] = new UFDouble[cMangID.length];

    if (pk_calbody != null && pk_calbody.trim().length() > 0) {
      // 从物料生产档案获取计划价
      String whereInPart=new TempTableUtil().getSubSql(cMangID); 
      /** ***************修改物料生产档案的查询方式（此档案缓存被取消，需要从后台查询）***************** */
      Object[][] retObj = null;
      try {
//        retObj = PubHelper.queryResultsFromTableByWhere("bd_produce", new String[] {
//          "jhj","pk_invmandoc"
//        }, alWhere.toArray(new String[alWhere.size()]));
        retObj = PubHelper.queryResultsFromAnyTable("bd_produce",new String[] {
            "pk_invmandoc","jhj"
        }, "PK_CALBODY='" + pk_calbody + "' and PK_INVMANDOC in " + whereInPart);
      }
      catch (Exception be) {
        SCMEnv.out(be);
        showErrorMessage(be.getMessage());
      }
      //将查询出的数据建立Map
      Map<String,UFDouble> planPrices=new HashMap<String,UFDouble>();
      if(retObj != null && retObj.length > 0){
        for(int i=0;i<retObj.length;++i){
          Object[] oneRow=(Object[])retObj[i];
          if(oneRow==null || oneRow[0]==null || oneRow[0].toString().length()==0) continue;
          planPrices.put(oneRow[0].toString(), PuPubVO.getUFDouble_NullAsZero(oneRow[1]));
        }
      }
      for(int i = 0; i < cMangID.length; i++)
        nPrice[i]=planPrices.get(cMangID[i]);
      /** ************************************************ */
    }
    else {
      // 从存货管理档案货物计划价
      try {
        Object o = CacheTool.getColumnValue("bd_invmandoc", "pk_invmandoc", "planprice", cMangID);
        if (o != null) {
          Object oTemp[] = (Object[]) o;
          for (int i = 0; i < cMangID.length; i++) {
            if (oTemp[i] != null && oTemp[i].toString().trim().length() > 0)
              nPrice[i] = new UFDouble(oTemp[i].toString());
          }
        }
      }
      catch (Exception e) {
        SCMEnv.out(e);
      }
    }

    return nPrice;
  }

  /**
   * 子类实现该方法，响应按钮事件。
   * 
   * @version (00-6-1 10:32:59)
   * @param bo
   *          ButtonObject
   */
  private void onButtonClickedList(nc.ui.pub.ButtonObject btn) {

    // 查询 修改 审批 弃审 作废 打印 刷新 辅助▼
    // ////////列表状态
    if (btn == m_btnInvBillQuery) {
      showHintMessage(getHeadHintText() + nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UCH046")/*
                                                                                                           * @res
                                                                                                           * "查询发票"
                                                                                                           */);

      // 查询
      onListQuery();
      showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UCH009"));

    }
    else if (btn == m_btnInvSelectAll) {
      showHintMessage(getHeadHintText() + nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPP40040401-000032")/*
                                                                                                                         * @res
                                                                                                                         * "浏览发票"
                                                                                                                         */);

      // 全选
      onSelectAll();
      showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "4004COMMON000000033")/*
                                                                                                   * @res
                                                                                                   * "全选成功"
                                                                                                   */);
    }
    else if (btn == m_btnInvDeselectAll) {
      showHintMessage(getHeadHintText() + nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPP40040401-000032")/*
                                                                                                                         * @res
                                                                                                                         * "浏览发票"
                                                                                                                         */);

      // 全消
      onSelectNo();
      showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "4004COMMON000000034")/*
                                                                                                   * @res
                                                                                                   * "全消成功"
                                                                                                   */);
    }
    else if (btn == m_btnInvBillDiscard) {
      showHintMessage(getHeadHintText() + nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UCH051")/*
                                                                                                           * @res
                                                                                                           * "批次作废发票"
                                                                                                           */);

      // 作废
      onListDiscard();
      showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UCH006"));

    }
    else if (btn == m_btnInvBillModify) {
      showHintMessage(getHeadHintText() + nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UCH027")/*
                                                                                                           * @res
                                                                                                           * "修改发票"
                                                                                                           */);

      // 修改
      onListModify();
      // 置光标到表头第一个可编辑项目
      getBillCardPanel().transferFocusTo(BillCardPanel.HEAD);

    }
    else if (btn == m_btnInvShift) {
      showHintMessage(getHeadHintText() + nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UCH055")/*
                                                                                                           * @res
                                                                                                           * "切换到单据界面"
                                                                                                           */);
      // 切换
      onListBill();
    }
    else if (btn == m_btnInvBillRefresh) {
      showHintMessage(getHeadHintText() + nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UCH046")/*
                                                                                                           * @res
                                                                                                           * "查询发票"
                                                                                                           */);

      // 刷新
      onListRefresh();
      showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UCH007"));

    }
    else if (btn == m_btnInvBillConversion) {
      showHintMessage(getHeadHintText() + nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPP40040401-000039")/*
                                                                                                                         * @res
                                                                                                                         * "放弃转单"
                                                                                                                         */);
      // 放弃转单
      onGiveupBillConversion();
    }
    else if (btn == m_btnCrtAPBill) {
      onCrtAPBill();
    }
    else if (btn == m_btnDelAPBill) {
      onDelAPBill();
    }
    else if (btn == m_btnDocManage) {
      showHintMessage(getHeadHintText()
          + nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPPSCMCommon-000278")/*
                                                                                           * @res
                                                                                           * "文档管理"
                                                                                           */);
      // 文档管理
      onDocManage();

    }
    else if (btn == m_btnQueryForAudit) {
      onQueryForAudit();
    }
    // 列表打印
    else if (btn == m_btnInvBillPrint) {
      // if (printList == null)
      // printList = new nc.ui.pu.print.PuPrintTool(getInvBillPanel());
      // printList.setData(getSelectedBills());
      // printList.print();
      // 批打印
      onBatchPrint();
      // 列表打印预览
    }
    else if (btn == m_btnInvBillPreview) {
      // if (printList == null)
      // printList = new nc.ui.pu.print.PuPrintTool(getInvBillPanel());
      // printList.setData(getSelectedBills());
      // printList.preview();
      // 批打印
      onBatchPrintPreview();
    }
    else if (PuTool.isExist(getExtendBtns(), btn)) {
      onExtendBtnsClick(btn);
    }

    else if (btn == m_btnInvBillCopy) {// 列表下复制
      showHintMessage(getHeadHintText() + nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPP40040401-000035")/*
                                                                                                                         * @res
                                                                                                                         * "复制发票"
                                                                                                                         */);
      setCurOperState(STATE_EDIT);
      onCopy();
      shiftShowModeTo(INV_PANEL_CARD);
    }
    else if (btn == btnBillCombin) {// 列表下合并显示
      onBillCombin();
    }
    else if (btn == m_btnLnkQuery) {// 列表下联查
      setCurOperState(STATE_BROWSE_NORMAL);
      onLnkQuery();
    }
    else if (btn == m_btnInvBillAudit) {// 列表下审批
      showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UCH048"));
      onListAudit();
      setCurOperState(STATE_BROWSE_NORMAL);
    }
    else if (btn == m_btnSendAudit) //列表下送审（批送审）
      onSendAuditBatch();
    else if (btn == m_btnInvBillUnAudit) {// 列表下弃审
      showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UCH049"));
      onListUnAudit();
    }

  }

  /**
   * 关闭窗口的客户端接口。可在本方法内完成窗口关闭前的工作。
   * 
   * @return boolean 返回值为true表示允许窗口关闭，返回值为false表示不允许窗口关闭。 创建日期：(2001-8-8
   *         13:52:37)
   */
  public boolean onClosing() {
    if (getListOperState() == STATE_LIST_FROM_BILLS) {
      int ret = MessageDialog
          .showYesNoCancelDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPPSCMCommon-000270"),
              nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPP40040401-000040")/*
                                                                                             * @res
                                                                                             * "是否放弃转入未转入完毕的单据，退出?"
                                                                                             */);
      if (ret == MessageDialog.ID_YES) {
        return true;
      }
      return false;
    }

    if (getCurOperState() == STATE_EDIT) {
      int ret = MessageDialog
          .showYesNoCancelDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPPSCMCommon-000270"),
              nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UCH001")/*
                                                                               * @res
                                                                               * "是否保存已修改的数据?"
                                                                               */);
      if (ret == MessageDialog.ID_YES) {
        if (onSave())
          return true;
      }
      else if (ret == MessageDialog.ID_NO) {
        return true;
      }
      return false;
    }

    return true;

  }

  /**
   * 按当前发票复制一张发票,转入等的信息为空
   * 
   * @param 参数说明
   * @return 返回值
   * @exception 异常描述
   * @see 需要参见的其它内容
   * @since 从类的那一个版本，此方法被添加进来。（可选）
   */

  private void onCopy() {

    // 列表下不能复制
    // 得到复制的单据
    // InvoiceVO vo = (InvoiceVO) getInvVOs()[getCurVOPos()].clone();
    // 设置制单人!!!!!!!!!
    // vo.getHeadVO().setCoperator(getCurOperator());
    // 设置日期
    // vo.getHeadVO().setDinvoicedate(nc.ui.pub.ClientEnvironment.getInstance().getDate());
    // vo.getHeadVO().setDarrivedate(nc.ui.pub.ClientEnvironment.getInstance().getDate());

    // 显示
    // addNewOneIntoInvVOs(vo);
    setCurOperState(STATE_EDIT);
    // 设置VO
    // setVOToBillPanel();

    dealWhenCopy();
    // 检查是否有封存的存货
    BillCardPanelHelper.clearSealedInventories(getBillCardPanel(), "cmangid", new String[] {
        "cmangid", "cbaseid", "invcode", "invname", "invspec", "invtype"
    });

    // 是否有自制单据
    setCouldMaked(true);
    m_bAdd = true;

    setButtonsAndPanelState();
    updateButtons();

    m_bCopy = true;
  }

  /**
   * 作者：王印芬 功能：行复制 参数：无 返回：无 例外：无 日期：(2001-5-13 11:39:21) 修改日期，修改人，修改原因，注释标志：
   */
  private void onCopyLine() {

    getBillCardPanel().copyLine();
  }

  /**
   * 删行
   * 
   * @param 参数说明
   * @return 返回值
   * @exception 异常描述
   * @see 需要参见的其它内容
   * @since 从类的那一个版本，此方法被添加进来。（可选）
   * @deprecated 该方法从类的那一个版本后，已经被其它方法替换。（可选）
   */
  private void onDeleteLine() {

    if (getBillCardPanel().getBillModel().getRowCount() <= 0) {
      return;
    }
    // 只剩最后一行
    /*
     * if(getInvBillPanel().getRowCount() == 1 ){
     * showWarningMessage("最后一行不能删除！") ; return ; }
     */
    getBillCardPanel().delLine();
  }

  /**
   * 作者：王印芬 功能：行复制 不能作废的条件： a) 已作废； b) 已审批（部分或全部） c) 已经结算； 参数：无 返回：无 例外：无
   * 日期：(2001-5-13 11:39:21) 修改日期，修改人，修改原因，注释标志：
   */
  private void onDiscard() {

    int ret = MessageDialog
        .showYesNoDlg(this,
            nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPP40040401-000042")/*
                                                                                           * @res
                                                                                           * "询问"
                                                                                           */,
            nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UCH002")/*
                                                                             * @res
                                                                             * "作废当前发票吗？\n作废操作不可恢复！"
                                                                             */);
    if (ret == MessageDialog.ID_NO || ret == MessageDialog.ID_CANCEL) {
      showHintMessage("");
      return;
    }

    // 返回结果

    // 参数LIST
    ArrayList paraList = new ArrayList();
    paraList.add(null);

    // 当前需作废的发票VO
    InvoiceVO invVO = getInvVOs()[getCurVOPos()];
    for (int i = 0; i < invVO.getBodyVO().length; i++) {
      invVO.getBodyVO()[i].setNShowRow(i + 1);
    }
    invVO.getHeadVO().setCuserid(nc.ui.pub.ClientEnvironment.getInstance().getUser().getPrimaryKey());
    // 为判断是否可修改、作废其他人单据
    ((InvoiceHeaderVO) invVO.getParentVO()).setCoperatoridnow(nc.ui.pub.ClientEnvironment.getInstance().getUser()
        .getPrimaryKey());

    // 状态条
    showHintMessage(getHeadHintText() + CommonConstant.SPACE_MARK
        + nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UC001-0000005")/*
                                                                                 * @res
                                                                                 * "作废"
                                                                                 */
        + CommonConstant.SPACE_MARK + CommonConstant.BEGIN_MARK + "......" + CommonConstant.END_MARK);

    boolean bConfirm = false;
    invVO.setUserConfirmFlag(new UFBoolean(false));
    while (!invVO.getUserConfirmFlag().booleanValue()) {
      try {
        if (bConfirm)
          invVO.setUserConfirmFlag(new UFBoolean(true));

        // 佛山碧桂园:记录业务日志
        invVO.getOperatelogVO().setOpratorname(nc.ui.pub.ClientEnvironment.getInstance().getUser().getUserName());
        invVO.getOperatelogVO()
            .setCompanyname(nc.ui.pub.ClientEnvironment.getInstance().getCorporation().getUnitname());
        invVO.getOperatelogVO().setOperatorId(nc.ui.pub.ClientEnvironment.getInstance().getUser().getPrimaryKey());
        Operlog operlog = new Operlog();
        operlog.insertBusinessExceptionlog(invVO, "删除", "删除", nc.vo.scm.funcs.Businesslog.MSGMESSAGE,
            nc.vo.scm.pu.BillTypeConst.PO_INVOICE, nc.ui.sm.cmenu.Desktop.getApplet().getParameter("USER_IP"));
        /**
         * 二次开发插件支持 by zhaoyha at 2009.2.16
         */
        getInvokeEventProxy().beforeAction(nc.vo.scm.plugin.Action.DELETE, new AggregatedValueObject[]{invVO});
 
        PfUtilClient.processBatch("DISCARD", nc.vo.scm.pu.BillTypeConst.PO_INVOICE, getToday().toString(),
            new InvoiceVO[] {
              invVO
            }, new Object[] {
              paraList
            });
        /**
         * 二次开发插件支持 by zhaoyha at 2009.2.16
         */
        getInvokeEventProxy().afterAction(nc.vo.scm.plugin.Action.DELETE, new AggregatedValueObject[]{invVO});

      }
      catch (RwtPiToPoException ex) {
        SCMEnv.out(ex);
         /*if(MessageDialog.showYesNoDlg(this,
         nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401","UPPSCMCommon-000270")@res
         "提示", ex.getMessage()) == MessageDialog.ID_YES){
         //继续
        bConfirm = true;
         }else{*/
        showHintMessage(ex.getMessage());
        /* 
        return;
         }*/
      }
      catch (RwtPiToScException ex) {
        SCMEnv.out(ex);
        // if(MessageDialog.showYesNoDlg(this,
        // nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401","UPPSCMCommon-000270")/*@res
        // "提示"*/, ex.getMessage()) == MessageDialog.ID_YES){
        // 继续
        bConfirm = true;
        // }else{
        showHintMessage(ex.getMessage());
        // return;
        // }
      }
      catch (Exception e) {
        SCMEnv.out(e);
        MessageDialog.showErrorDlg(this,
            nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPP40040401-000016")/*
                                                                                           * @res
                                                                                           * "系统故障"
                                                                                           */,
            nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPP40040401-000044")/*
                                                                                           * @res
                                                                                           * "作废未成功！"
                                                                                           */
            +System.getProperty("line.separator")+e.getMessage()); //增加异常信息
        showHintMessage(e.getMessage());
        return;
      }
      if (!bConfirm)
        break;// 未抛异常,跳出
    }

    // =================================================
    if (PfUtilClient.isSuccess()) {
      // 提示用户
      // MessageDialog.showHintDlg(this, "提示", "作废发票结束！");
      // showHintMessage(getHeadHintText() + CommonConstant.SPACE_MARK +
      // nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401","UPP40040401-000045")/*@res
      // "作废结束"*/ + CommonConstant.SPACE_MARK);
      showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UCH006")/*
                                                                                       * res@
                                                                                       * "删除成功"
                                                                                       */);
      // 从缓存中除去
      removeOneFromInvVOs(getCurVOPos());
      // 设置卡片界面数据
      setVOToBillPanel();
    }
    //showHintMessage("");
    return;
  }

  private void onDocManage() {
    if(getCurPanelMode() == INV_PANEL_CARD){
      InvoiceVO vo = (InvoiceVO) getInvVOs()[getCurVOPos()];
      if (vo != null && vo.getHeadVO().getPrimaryKey() != null){
        DocumentManager.showDM(this,ScmConst.PO_Invoice, 
            new String[]{vo.getHeadVO().getPrimaryKey()});
      }
    }else {
      if (getInvVOs() != null && getInvVOs().length > 0) {
        String[] billIDs = null;
        String[] billCodes = null;
        Vector v1 = new Vector();
        Vector v2 = new Vector();
        
        for (int i = 0; i < getInvVOs().length; i++) {
          if (getInvVOs()[i].getHeadVO().getPrimaryKey() != null
              && getBillListPanel().getHeadBillModel().getRowState(i) == BillModel.SELECTED) {
            
            v1.add(getInvVOs()[i].getHeadVO().getPrimaryKey());
            v2.add(getInvVOs()[i].getHeadVO().getVinvoicecode());
          }
        }
        if (v1.size() > 0 && v1.size() == v2.size()) {
          billIDs = new String[v1.size()];
          billCodes = new String[v2.size()];
          v1.copyInto(billIDs);
          v2.copyInto(billCodes);
          DocumentManager.showDM(this,ScmConst.PO_Invoice, billIDs);
        }
      }
    }
  }

  private void onHqhp() {

    // 只有在编辑状态下才能使用本功能
    if (getCurOperState() != STATE_EDIT) {
      return;
    }

    //
    int bodyNum = getBillCardPanel().getRowCount();
    if (bodyNum < 1) {
      return;
    }

    // 从来源入库单对应的价格结算单上取出“本币含税单价”
    InvoiceVO billVO = new InvoiceVO(bodyNum);
    getBillCardPanel().getBillValueVO(billVO);
    int iLen = billVO.getBodyVO().length;
    String lStr_NativeCurrencyID = PiPqPublicUIClass.getNativeCurrencyID();
    Vector lVec_RowIndex = new Vector();
    Vector lVec_Temp = new Vector();
    for (int i = 0; i < iLen; i++) {
      if (billVO.getBodyVO()[i].getCupsourcebilltype().equals("45")
          && billVO.getBodyVO()[i].getCcurrencytypeid().equals(lStr_NativeCurrencyID)) {// 根据入库单生成的，且币种为本位币的采购发票可以进行“优质优价取价”

        PricParaVO tempVO = new PricParaVO();
        tempVO.setCgeneralbid(billVO.getBodyVO()[i].getCupsourcebillrowid());
        lVec_Temp.addElement(tempVO);

        lVec_RowIndex.addElement(new Integer(i));

      }
    }

    PricParaVO VOs[] = new PricParaVO[lVec_Temp.size()];
    lVec_Temp.copyInto(VOs);
    try {
      VOs = PricStlHelper.queryPricStlPrices(VOs);
    }
    catch (Exception e) {
      SCMEnv.out(e);
    }

    // 取本币含税单价，注意：发票上没有本币含税单价，而从库存来的发票，原币==本币，所以可以对应到发票上的“原币净含税单价”（xhq）
    StringBuffer error = new StringBuffer();

    if (VOs != null && VOs.length > 0) {
      for (int i = 0; i < VOs.length; i++) {

        if (VOs[i].getNtaxprice() != null) {
          int lint_row = ((Integer) lVec_RowIndex.elementAt(i)).intValue();

          if (getUFDouble(VOs[i].getNtaxprice()) != null && getUFDouble(VOs[i].getNtaxprice()).doubleValue() < 0.0) {
            error.append(billVO.getBodyVO()[lint_row].getCrowno() + ",");
          }
          else {
            getBillCardPanel().setBodyValueAt(VOs[i].getNtaxprice(), lint_row, "norgnettaxprice");
            BillEditEvent event = new BillEditEvent(getBillCardPanel().getBillTable(), VOs[i].getNtaxprice(),
                "norgnettaxprice", lint_row);// 原币净含税单价
            afterEditInvBillRelations(event);// 触发相关计算
          }
        }
      }
    }
    if (error != null && error.length() > 0) {
      MessageDialog.showHintDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("scmcommon", "UPPSCMCommon-000270")/** @res* "提示" */
      , "行" + error.toString() + "\n" + nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UC000-0000450")/* @res:净单价 */
          + nc.ui.ml.NCLangRes.getInstance().getStrByID("scmpub", "UPPscmpub-000507")/*
                                                                                       * @res
                                                                                       * "不能为负，请重新输入。"
                                                                                       */);
    }

  }

  /* 根据ITEMKEY得到得到当前行的值 只要有值，不管是否为零，都取这个值 为空或空串等时，返回NULL */
  private UFDouble getUFDouble(UFDouble double1) {

    if (double1 == null || double1.equals("")) {
      return null;
    }

    return PuPubVO.getUFDouble_ValueAsValue(double1);

  }

  /**
   * 首张
   * 
   * @param 参数说明
   * @return 返回值
   * @exception 异常描述
   * @see 需要参见的其它内容
   * @since 从类的那一个版本，此方法被添加进来。（可选）
   * @deprecated 该方法从类的那一个版本后，已经被其它方法替换。（可选）
   */
  private void onFirst() {
    // 保存原来的位置
    m_nLstInvVOPos = getCurVOPos();

    setCurVOPos(0);
    // 设置金额等位数为最大的位数
    setMaxMnyDigit(iMaxMnyDigit);

    // 设置卡片界面数据
    setVOToBillPanel();
  }

  /**
   * 作者：方益 功能：放弃转单 参数：无 返回：无 例外：无 日期：(2001-5-13 11:39:21) 修改日期，修改人，修改原因，注释标志：
   */
  private void onGiveupBillConversion() {

    Vector v = new Vector();
    for (int i = 0; getInvVOs() != null && i < getInvVOs().length; i++) {
      if (getInvVOs()[i].getPrimaryKey() == null) {
        v.add(new Integer(i));
      }
    }

    Integer[] indexes = null;
    if (v.size() > 0) {
      indexes = new Integer[v.size()];
      v.copyInto(indexes);
    }
    if (indexes != null) {
      removeSomeFromInvVOs(indexes);
    }

    if (m_vSavedVO != null && m_vSavedVO.size() > 0) {
      InvoiceVO tempVO[] = new InvoiceVO[m_vSavedVO.size()];
      m_vSavedVO.copyInto(tempVO);
      setInvVOs(tempVO);
    }
    else {
      setInvVOs(getInvVOsByContinue());
    }
    m_vSavedVO = null;

    if (getInvVOs() == null) {
      setCurVOPos(-1);
      // 设置卡片界面数据
      setVOToBillPanel();
    }
    else {
      setCurVOPos(getInvVOs().length - 1);
      setVOToBillPanel();
    }

    setListOperState(STATE_LIST_NORMAL);
    setCurOperState(getBillBrowseState());
    shiftShowModeTo(INV_PANEL_CARD);
    m_btnInvBillConversion.setEnabled(false);

  }

  /**
   * 作者：王印芬 功能：插入行 参数：无 返回：无 例外：无 日期：(2001-5-13 11:39:21) 修改日期，修改人，修改原因，注释标志：
   * 2002-09-26 wyf 加入对新插入行的一些值的默认设置
   */
  private void onInsertLine() {
    int iRow = getBillCardPanel().getBillTable().getSelectedRow();

    getBillCardPanel().insertLine();

    if (iRow >= 0) {
      // wyf 2002-09-26 add begin
      setNewLineDefaultValue(iRow);
      // wyf 2002-09-26 add end
    }

    BillRowNo.insertLineRowNo(getBillCardPanel(), ScmConst.PO_Invoice, "crowno");
  }

  /**
   * 末张
   * 
   * @param 参数说明
   * @return 返回值
   * @exception 异常描述
   * @see 需要参见的其它内容
   * @since 从类的那一个版本，此方法被添加进来。（可选）
   * @deprecated 该方法从类的那一个版本后，已经被其它方法替换。（可选）
   */
  private void onLast() {
    // 保存原来的位置
    m_nLstInvVOPos = getCurVOPos();

    setCurVOPos(getInvVOs().length - 1);
    // 设置金额等位数为最大的位数
    setMaxMnyDigit(iMaxMnyDigit);

    // 设置卡片界面数据
    setVOToBillPanel();

  }

  /**
   * 切换到列表状态
   * 
   * @param 参数说明
   * @return 返回值
   * @exception 异常描述
   * @see 需要参见的其它内容
   * @since 从类的那一个版本，此方法被添加进来。（可选）
   * @deprecated 该方法从类的那一个版本后，已经被其它方法替换。（可选）
   */
  private void onList() {

    if (getInvVOs() != null) {
      int nVOStatus = 0;
      int len = getInvVOs().length;
      for (int i = 0; i < len; i++) {
        nVOStatus = getInvVOs()[i].getSource();
        // 不是查询出的单据从缓存中删除
        if (PuPubVO.getString_TrimZeroLenAsNull(getInvVOs()[i].getHeadVO().getVinvoicecode()) == null
            || PuPubVO.getString_TrimZeroLenAsNull(getInvVOs()[i].getHeadVO().getVinvoicecode()) == null) {
          removeOneFromInvVOs(i);
        }

      }
    }

    shiftShowModeTo(INV_PANEL_LIST);
    // 设置VO数组到列表界面
    setVOsToListPanel();
    // 列表状态
    setCurOperState(getListOperState());
  }

  /**
   * 切换到卡片状态
   * 
   * @param 参数说明
   * @return 返回值
   * @exception 异常描述
   * @see 需要参见的其它内容
   * @since 从类的那一个版本，此方法被添加进来。（可选）
   */
  private void onListBill() {

    // 如果是其他单据转入,则双击不可用
    if (getCurOperState() == STATE_LIST_FROM_BILLS) {
      showHintMessage(getHeadHintText() + nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UCH027")/*
                                                                                                           * @res
                                                                                                           * "修改发票"
                                                                                                           */);
      // 修改
      onListModify();
      // 置光标到表头第一个可编辑项目
      getBillCardPanel().transferFocusTo(BillCardPanel.HEAD);
      return;
    }

    if (getInvVOs() == null)
      getBillCardPanel().addNew();
    else {
      int row = getSelectedRowOnList();
      row = PuTool.getIndexBeforeSort(getBillListPanel(), row);

      if (getInvVOs()[row].getBodyVO() == null) {
        return;
      }
      setCurVOPos(row);
      // 设置金额等位数为最大的位数
      setMaxMnyDigit(iMaxMnyDigit);
      // 设置卡片界面数据
      setVOToBillPanel();
      // 设置可编辑性
      getBillCardPanel().getHeadItem("nexchangeotobrate").setEdit(false);
    }
    setCurOperState(getBillBrowseState());
    shiftShowModeTo(INV_PANEL_CARD);
  }


  /**
   * 作者：王印芬 功能：作废列表的一批选中的单据 参数：无 返回：无 例外：无 日期：(2002-3-13 11:39:21)
   * 修改日期，修改人，修改原因，注释标志： 2002-04-15 wyf 配合后端只进行TS判断是否单据改变，本代码加入一些如是否审批的判断
   * 2002-06-19 wyf 修改提示 2002-09-26 wyf 加入对虚拟发票的控制
   */
  private void onListDiscard() {

    // 得到被选中的VO
    Vector vDiscardVO = new Vector();
    Vector vDiscardIndex = new Vector();
    for (int i = 0; i < getInvVOs().length; i++) {
      if (getBillListPanel().getHeadBillModel().getRowState(i) == BillModel.SELECTED) {
        // 有可能经过了排序，得到真正的表头INDEX
        int nCurIndex = PuTool.getIndexBeforeSort(getBillListPanel(), i);
        vDiscardVO.add(getInvVOs()[nCurIndex]);
        vDiscardIndex.add(new Integer(nCurIndex));
      }
    }

    // 装载表体
    InvoiceVO[] vos = null;
    if (vDiscardVO.size() > 0) {
      vos = new InvoiceVO[vDiscardVO.size()];
      vDiscardVO.copyInto(vos);
      if (!loadItemsForInvoiceVOs(vos))
        return;
    }

    // 判断将要删除的VO合法性
    for (int i = 0; vos != null && i < vos.length; i++) {
      // 如果存在已审批发票则返回
      Integer iBillStatus = vos[i].getHeadVO().getIbillstatus();
      if (iBillStatus.compareTo(nc.vo.scm.pu.BillStatus.AUDITED) == 0
          || iBillStatus.compareTo(nc.vo.scm.pu.BillStatus.AUDITING) == 0) {
        MessageDialog
            .showHintDlg(this,
                nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPPSCMCommon-000270")/*
                                                                                               * @res
                                                                                               * "提示"
                                                                                               */,
                nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPP40040401-000046")/*
                                                                                               * @res
                                                                                               * "所选发票存在已审批或正在审批的发票！"
                                                                                               */);
        return;
      }
      // 如果存在虚拟发票则返回
      if (vos[i].isVirtual()) {
        MessageDialog.showHintDlg(this,
            nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPPSCMCommon-000270")/*
                                                                                           * @res
                                                                                           * "提示"
                                                                                           */,
            nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPP40040401-000047")/*
                                                                                           * @res
                                                                                           * "所选发票存在虚拟发票！"
                                                                                           */);
        return;
      }
      // 如果存在结算的发票则返回
      InvoiceItemVO[] voaItem = vos[i].getBodyVO();
      int iSettle = -1;
      int iLen = voaItem.length;
      for (int j = 0; j < iLen; j++) {
        if (voaItem[j].getNaccumsettmny() != null
            && voaItem[j].getNaccumsettmny().compareTo(nc.vo.scm.pu.VariableConst.ZERO) != 0) {
          iSettle = i + 1;
          break;
        }
      }
      if (iSettle != -1) {
        MessageDialog.showHintDlg(this,
            nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPPSCMCommon-000270")/*
                                                                                           * @res
                                                                                           * "提示"
                                                                                           */,
            nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPP40040401-000048")/*
                                                                                           * @res
                                                                                           * "所选发票存在已结算的发票！"
                                                                                           */);
        return;
      }
    }

    if (vDiscardVO.size() == 0) {
      MessageDialog.showHintDlg(this,
          nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPPSCMCommon-000270")/*
                                                                                         * @res
                                                                                         * "提示"
                                                                                         */,
          nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPP40040401-000049")/*
                                                                                         * @res
                                                                                         * "请先选择要作废的发票！"
                                                                                         */);
      setSelectedRowCount(0);
      return;
    }

    int ret = MessageDialog.showYesNoDlg(this,
        nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UCH002")/*
                                                                         * @res
                                                                         * "询问"
                                                                         */, nc.ui.ml.NCLangRes.getInstance()
            .getStrByID("40040401", "UPP40040401-000050")/*
                                                           * @res
                                                           * "作废选中的发票吗？\n作废操作不可恢复！"
                                                           */);

    if (ret == MessageDialog.ID_NO || ret == MessageDialog.ID_CANCEL) {
      return;
    }

    // 得到需作废的VO
    InvoiceVO[] proceVOs = new InvoiceVO[vDiscardVO.size()];
    vDiscardVO.copyInto(proceVOs);

    // 当前需作废的发票VO
    int len = proceVOs.length;
    for (int i = 0; i < len; i++) {
      int bLen = proceVOs[i].getBodyVO().length;
      for (int j = 0; j < bLen; j++) {
        proceVOs[i].getBodyVO()[j].setNShowRow(j + 1);

      }
    }
    // 参数LIST
    Object[] paraLists = new Object[len];
    len = paraLists.length;
    for (int i = 0; i < len; i++) {
      paraLists[i] = new ArrayList();
      ((ArrayList) paraLists[i]).add(null);
    }

    for (int i = 0; i < proceVOs.length; i++) {
      proceVOs[i].getHeadVO().setCuserid(nc.ui.pub.ClientEnvironment.getInstance().getUser().getPrimaryKey());
      // 为判断是否可修改、作废其他人单据
      ((InvoiceHeaderVO) proceVOs[i].getParentVO()).setCoperatoridnow(nc.ui.pub.ClientEnvironment.getInstance()
          .getUser().getPrimaryKey());

      // 佛山碧桂园:记录业务日志
      proceVOs[i].getOperatelogVO().setOpratorname(nc.ui.pub.ClientEnvironment.getInstance().getUser().getUserName());
      proceVOs[i].getOperatelogVO().setCompanyname(
          nc.ui.pub.ClientEnvironment.getInstance().getCorporation().getUnitname());
      proceVOs[i].getOperatelogVO().setOperatorId(nc.ui.pub.ClientEnvironment.getInstance().getUser().getPrimaryKey());
      Operlog operlog = new Operlog();
      operlog.insertBusinessExceptionlog(proceVOs[i], "删除", "删除", nc.vo.scm.funcs.Businesslog.MSGMESSAGE,
          nc.vo.scm.pu.BillTypeConst.PO_INVOICE, nc.ui.sm.cmenu.Desktop.getApplet().getParameter("USER_IP"));

    }

    // 状态条
    showHintMessage(getHeadHintText() + CommonConstant.SPACE_MARK
        + nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPP40040401-000051")/*
                                                                                         * @res
                                                                                         * "批次作废"
                                                                                         */
        + CommonConstant.SPACE_MARK + CommonConstant.BEGIN_MARK + "......" + CommonConstant.END_MARK);

    boolean bConfirm = false;
    for (int i = 0; i < len; i++) {
      vos[i].setUserConfirmFlag(new UFBoolean(false));
      while (!vos[i].getUserConfirmFlag().booleanValue()) {
        try {
          if (bConfirm)
            vos[0].setUserConfirmFlag(new UFBoolean(true));
          PfUtilClient.processBatch("DISCARD", BillTypeConst.PO_INVOICE, ClientEnvironment.getInstance().getDate()
              .toString(), new InvoiceVO[] {
            proceVOs[i]
          }, paraLists);
          break;
        }
        catch (RwtPiToPoException ex) {
          SCMEnv.out(ex);
          // if(MessageDialog.showYesNoDlg(this,
          // nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401","UPPSCMCommon-000270")/*@res
          // "提示"*/, ex.getMessage()) == MessageDialog.ID_YES){
          // 继续
          bConfirm = true;
          // continue;
          // }else{
          // showHintMessage("");
          // return;
          // }
        }
        catch (RwtPiToScException ex) {
          SCMEnv.out(ex);
          // if(MessageDialog.showYesNoDlg(this,
          // nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401","UPPSCMCommon-000270")/*@res
          // "提示"*/, ex.getMessage()) == MessageDialog.ID_YES){
          // 继续
          bConfirm = true;
          // }else{
          // showHintMessage("");
          // return;
          // }
        }
        catch (Exception e) {
          SCMEnv.out(e);
          MessageDialog.showHintDlg(this,
              nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPP40040401-000016")/*
                                                                                             * @res
                                                                                             * "系统故障"
                                                                                             */,
              nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPP40040401-000052")/*
                                                                                             * @res
                                                                                             * "批次作废未成功！"
                                                                                             */);
          return;
        }

      }
    }
    if (PfUtilClient.isSuccess()) {
      // 从缓存中除去
      Integer[] iaIndex = new Integer[vDiscardIndex.size()];
      vDiscardIndex.copyInto(iaIndex);
      removeSomeFromInvVOs(iaIndex);
      setVOsToListPanel();
      // onListRefresh();
    }

  }

  /**
   * 作者：王印芬 功能：列表按钮“修改”对应的函数 参数：无 返回：无 例外：无 日期：(2001-5-13 11:39:21)
   * 修改日期，修改人，修改原因，注释标志： 2002-09-26 wyf 加入对虚拟发票的控制 2002-11-12 wyf
   * 修改审批失败的发票不能修改的问题
   */
  private void onListModify() {

    int nCurIndex = -1;
    int VOPos = -1;
    for (int i = 0; i < getInvVOs().length; i++) {
      if (getBillListPanel().getHeadBillModel().getRowState(i) == BillModel.SELECTED) {
        nCurIndex = i;
        break;
      }
    }

    // 有可能经过了排序，得到真正的表头INDEX
    nCurIndex = PuTool.getIndexBeforeSort(getBillListPanel(), nCurIndex);
    VOPos = nCurIndex;
    if (VOsPos != null && VOsPos.length > 0) {
      nCurIndex = new Integer(VOsPos[nCurIndex]).intValue();
      VOsPos = null;
    }

    if (getCurOperState() != STATE_LIST_FROM_BILLS) {
      // ////////////////////有如下特征的发票不可修改
      // 已审批的:不能修改
      Integer iStatus = getInvVOs()[nCurIndex].getHeadVO().getIbillstatus();
      if (iStatus != null && iStatus.compareTo(nc.vo.scm.pu.BillStatus.FREE) != 0
          && iStatus.compareTo(nc.vo.scm.pu.BillStatus.AUDITFAIL) != 0) { // 自由,审批未通过
        MessageDialog.showHintDlg(this,
            nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPPSCMCommon-000270")/*
                                                                                           * @res
                                                                                           * "提示"
                                                                                           */,
            nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPP40040401-000055")/*
                                                                                           * @res
                                                                                           * "该发票已审批，不能修改！"
                                                                                           */);
        return;
      }
      // 已结算的发票：不可修改
      for (int i = 0; i < getInvVOs()[nCurIndex].getBodyVO().length; i++) {
        if ((getInvVOs()[nCurIndex].getBodyVO()[i].getNaccumsettmny() != null && getInvVOs()[nCurIndex].getBodyVO()[i]
            .getNaccumsettmny().doubleValue() != 0.0)) {
          MessageDialog.showHintDlg(this, nc.ui.ml.NCLangRes.getInstance()
              .getStrByID("40040401", "UPPSCMCommon-000270")/* @res "提示" */, nc.ui.ml.NCLangRes.getInstance()
              .getStrByID("40040401", "UPP40040401-000056")/*
                                                             * @res
                                                             * "该发票已结算，不能修改！"
                                                             */);
          return;
        }
      }
      // 虚拟发票:不能修改
      if (getInvVOs()[nCurIndex].isVirtual()) {
        MessageDialog.showHintDlg(this,
            nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPPSCMCommon-000270")/*
                                                                                           * @res
                                                                                           * "提示"
                                                                                           */,
            nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPP40040401-000057")/*
                                                                                           * @res
                                                                                           * "该发票为虚拟发票，不能修改！"
                                                                                           */);
        return;
      }

      // 业务类型
      setCurBizeType(getInvVOs()[nCurIndex].getHeadVO().getCbiztype());
    }
    if (getBillListPanel().getHeadBillModel().getValueAt(VOPos, "cinvoiceid") == null && splitFlag) {
      // if(nCurIndex != 0){
      setCurVOPos(currentPos + VOPos);
      // }else{
      // setCurVOPos(currentPos);
      // }
      splitFlag = false;
    }
    else {
      setCurVOPos(nCurIndex);
    }
    setCurOperState(STATE_EDIT);
    setInvoiceTypeComItem();// 避免自制“虚拟”发票
    shiftShowModeTo(INV_PANEL_CARD);
    // 设置卡片界面数据
    setVOToBillPanel();

    // 取业务类型
    String cBizType = getBillCardPanel().getHeadItem("cbiztype").getValue();
    if (cBizType == null || cBizType.toString().trim().length() == 0) {
      cBizType = getCurBizeType();
      getBillCardPanel().setHeadItem("cbiztype", cBizType);
    }
    try {
      // 过滤存货参照,保证在业务类型是受托代销业务类型时替换件参照是受托代销属性
      setInventoryRefFilter(cBizType);
    }
    catch (Exception e) {
      SCMEnv.out(e);
    }

    for (int i = 0; i < getBillCardPanel().getRowCount(); i++) {
      // 设置表体行的扣税类别与表头相同
      if (getBillCardPanel().getBodyValueAt(i, "idiscounttaxtype") == null) {
        getBillCardPanel().setBodyValueAt(
            ((UIComboBox) getBillCardPanel().getHeadItem("idiscounttaxtype").getComponent()).getSelectedItem(), i,
            "idiscounttaxtype");
      }
      // 带出存货的默认税率
      if (getBillCardPanel().getBodyValueAt(i, "ntaxrate") == null) {
        setRelated_Taxrate(i, i);
      }
    }
    //效率优化 by zhaoyha at 2009.9
    boolean isNeedCal=getBillCardPanel().getBillModel().isNeedCalculate();
    getBillCardPanel().getBillModel().setNeedCalculate(false);
    getBillCardPanel().setDefaultPrice(getBillCardPanel());
    //效率优化 by zhaoyha at 2009.9
    getBillCardPanel().getBillModel().setNeedCalculate(isNeedCal);
    if (getCurOperState() != STATE_LIST_FROM_BILLS) {
      afterEditWhenHeadCurrency(null);
    }
    // since v51, 设置业务员默认值 根据操作员带出业务员
    setDefaultValueByUser();
    //For V56 审批流审批不通过调整,清除审批人信息
    clearAuditInfo(null, getBillCardPanel());
    //clear
    // 浮动菜单右键功能权限控制
    setPopMenuBtnsEnable();
    createAddContinueBtn();
    updateButtons();
  }

  /**
   * 查询所有符合条件的发票
   * 
   * @param 参数说明
   * @return 返回值
   * @exception 异常描述
   * @see 需要参见的其它内容
   * @since 从类的那一个版本，此方法被添加进来。（可选）
   */

  // 暂时取出所有该单位的发票,并同时显示第一张
  private void onListQuery() {

    //显示查询对话框
    InvQueDlg curDlg = getQueDlg();
    curDlg.showModal();

    //查询
    if (curDlg.isCloseOK()) {

      // 设置业务类型
      // setOldBizeType(getCurBizeType());

      // 查询
      execListQuery(curDlg);
      // 设置是否曾经查过
      setEverQueryed(true);
    }

  }

  /**
   * 查询所有符合条件的发票
   * 
   * @param 参数说明
   * @return 返回值
   * @exception 异常描述
   * @see 需要参见的其它内容
   * @since 从类的那一个版本，此方法被添加进来。（可选）
   */

  // 暂时取出所有该单位的发票,并同时显示第一张
  private void onListRefresh() {
    // 未销毁的对话框
    execListQuery(getQueDlg());
  }

  /**
   * 联查
   * 
   * @param 参数说明
   * @return 返回值
   * @exception 异常描述
   * @see 需要参见的其它内容
   * @since 从类的那一个版本，此方法被添加进来。（可选）
   * @deprecated 该方法从类的那一个版本后，已经被其它方法替换。（可选）
   */
  private void onLnkQuery() {

    InvoiceVO vo = null;
    if(m_nCurOperState==STATE_EDIT)
      vo=(InvoiceVO)getBillCardPanel().getBillValueVO(InvoiceVO.class.getName(), 
          InvoiceHeaderVO.class.getName(), InvoiceItemVO.class.getName());
    else
      vo= getInvVOs()[getCurVOPos()];

    if (vo == null || vo.getParentVO() == null)
      return;

    nc.ui.scm.sourcebill.SourceBillFlowDlg soureDlg = new nc.ui.scm.sourcebill.SourceBillFlowDlg(this,
        ScmConst.PO_Invoice, ((InvoiceHeaderVO) vo.getParentVO()).getPrimaryKey(), null, nc.ui.pub.ClientEnvironment
            .getInstance().getUser().getPrimaryKey(), ((InvoiceHeaderVO) vo.getParentVO()).getVinvoicecode());
    soureDlg.showModal();
  }

  /**
   * 表体的快捷方式
   */
  public void onMenuItemClick(java.awt.event.ActionEvent e) {

    UIMenuItem menuItem = (UIMenuItem) e.getSource();
    if (menuItem == getBillCardPanel().getAddLineMenuItem()) {
      // 增行
      onAppendLine();
    }
    else if (menuItem == getBillCardPanel().getDelLineMenuItem()) {
      // 删行
      onDeleteLine();
    }
    else if (menuItem == getBillCardPanel().getCopyLineMenuItem()) {
      // 复制
      onCopyLine();
    }
    else if (menuItem == getBillCardPanel().getPasteLineMenuItem()) {
      // 粘贴
      onPasteLine();
    }
    else if (menuItem == getBillCardPanel().getInsertLineMenuItem()) {
      // 插入
      onInsertLine();
    }
    else if (menuItem == getBillCardPanel().getPasteLineToTailMenuItem()) {
      onPasteLineToTail();
    }
    // 重排行号
    else if (menuItem.equals(m_miReSortRowNo)) {
      onReSortRowNo();
    }
    else if (menuItem.equals(m_miCardEdit)) {
      onCardEdit();
    }
    /**
     * 二次开发插件支持 by zhaoyha at 2009.2.16
     */
    getInvokeEventProxy().onMenuItemClick(e);
  }

  /*
   * 重排行号
   */
  private void onReSortRowNo() {
    PuTool.resortRowNo(getBillCardPanel(), ScmConst.PO_Invoice, "crowno");
    showHintMessage(NCLangRes.getInstance().getStrByID("common", "SCMCOMMON000000284")/*
                                                                                       * @res
                                                                                       * "重排行号成功"
                                                                                       */);
  }

  private void onCardEdit() {
    //设置辅计量等可编辑状态
    int rows=getBillCardPanel().getRowCount();
    for(int i=0;i<rows;++i){
      beforeEditBodyAssistUnitNumber(getBillCardPanel(), i);
      //自由项处理，主要为了初始化参照
      Object vf=getBillCardPanel().getBodyValueAt(i, "vfree0");
      if(null!=vf && !StringUtil.isEmptyWithTrim(vf.toString())){
        beforeEdit(new BillEditEvent(this,null,null,"vfree0",i,BillItem.BODY));
      }
    }
    getBillCardPanel().startRowCardEdit();
  }

  /**
   * 函数的功能、用途、对属性的更改，以及函数执行前后对象的状态。
   * 
   * @param 参数说明
   * @return 返回值
   * @exception 异常描述
   * @see 需要参见的其它内容
   * @since 从类的那一个版本，此方法被添加进来。（可选）
   * @deprecated 该方法从类的那一个版本后，已经被其它方法替换。（可选）
   */
  private void onModify() {

    setInvoiceTypeComItem();// 避免自制“虚拟”发票

    // 取业务类型
    String cBizType = getBillCardPanel().getHeadItem("cbiztype").getValue();
    // 过滤存货参照
    try {
      // 过滤存货参照,保证在业务类型是受托代销业务类型时替换件参照是受托代销属性
      setInventoryRefFilter(cBizType);
    }
    catch (Exception e) {
      SCMEnv.out(e);
    }

    // String cinventorycode =
    // getInvBillPanel().getBodyItem("cinventorycode").getValue();
    //For V56 审批流审批不通过调整,清除审批人信息
    clearAuditInfo(null, getBillCardPanel());
    // 浮动菜单右键功能权限控制
    setPopMenuBtnsEnable();
    setButtonsAndPanelState();
    createAddContinueBtn();
    updateButtons();
  }

  /**
   * 下一张
   * 
   * @param 参数说明
   * @return 返回值
   * @exception 异常描述
   * @see 需要参见的其它内容
   * @since 从类的那一个版本，此方法被添加进来。（可选）
   * @deprecated 该方法从类的那一个版本后，已经被其它方法替换。（可选）
   */
  private void onNext() {
    // 保存原来的位置
    m_nLstInvVOPos = getCurVOPos();

    setCurVOPos(getCurVOPos() + 1);
    // 设置金额等位数为最大的位数
    setMaxMnyDigit(iMaxMnyDigit);

    // 设置卡片界面数据
    setVOToBillPanel();

  }

  /**
   * 作者：王印芬 功能：行粘贴 参数：无 返回：无 例外：无 日期：(2001-5-13 11:39:21) 修改日期，修改人，修改原因，注释标志：
   * 2002-09-20 wyf 加入对来源单据类型及来源单据号的显示 2002-11-25 wyf 对TS赋空
   */
  private void onPasteLine() {

    // 获得粘贴前的表体行数
    int nRowCount1 = getBillCardPanel().getRowCount();
    getBillCardPanel().pasteLine();
    // 获得粘贴后的表体行数
    int nRowCount2 = getBillCardPanel().getRowCount();
    // 获得粘贴的行数
    int nPastRowCount = nRowCount2 - nRowCount1;

    // 对被粘贴的行,逐行修改某写字段的值
    if (nPastRowCount > 0) {
      // 取表头币种
//      String strCurr = getBillCardPanel().getHeadItem("ccurrencytypeid").getValue();
      int iSelectRow = getBillCardPanel().getBillTable().getSelectedRow();
      for (int i = iSelectRow - nPastRowCount; i < iSelectRow; i++) {
        // 必须置空的值
        getBillCardPanel().setBodyValueAt(null, i, "cinvoiceid");
        getBillCardPanel().setBodyValueAt(null, i, "cinvoice_bid");
        // 与来源、上层来源有关
        // getInvBillPanel().setBodyValueAt(null, i, "corderid");
        // getInvBillPanel().setBodyValueAt(null, i, "corder_bid");
        getBillCardPanel().setBodyValueAt(null, i, "ts");
        // 设置新插入的表体行币种默认为表头币种
       // getBillCardPanel().setBodyValueAt(strCurr, i, "ccurrencytypeid");
        // 设置汇率
        setExchangeRateBody(i, true, null);
      }
      BillRowNo.pasteLineRowNo(getBillCardPanel(), ScmConst.PO_Invoice, "crowno", nPastRowCount);
    }
  }

  /**
   * 此处插入方法说明。 创建日期：(2003-11-10 11:19:18)
   */
  private void onPasteLineToTail() {
    int iOldRowCnt = getBillCardPanel().getRowCount();
    getBillCardPanel().pasteLineToTail();
    int iNewRowCnt = getBillCardPanel().getRowCount();
    if (iOldRowCnt > 0 && iNewRowCnt > 0 && iOldRowCnt == iNewRowCnt)
      showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPPSCMCommon-000424")/*
                                                                                                     * @res
                                                                                                     * "粘贴行到行尾未成功,可能原因：没有拷贝内容或未确定要粘贴的位置"
                                                                                                     */);
    else {
      // 获得粘贴的行数
      int nPastRowCount = iNewRowCnt - iOldRowCnt;

      // 对被粘贴的行,逐行修改某写字段的值
      if (nPastRowCount > 0) {
        // int iSelectRow = getInvBillPanel().getBillTable().getSelectedRow();
        // 取表头币种
        String strCurr = getBillCardPanel().getHeadItem("ccurrencytypeid").getValue();
        for (int i = iOldRowCnt; i < iNewRowCnt; i++) {
          // 必须置空的值
          getBillCardPanel().setBodyValueAt(null, i, "cinvoiceid");
          getBillCardPanel().setBodyValueAt(null, i, "cinvoice_bid");
          // 与来源、上层来源有关
          // getInvBillPanel().setBodyValueAt(null, i, "corderid");
          // getInvBillPanel().setBodyValueAt(null, i, "corder_bid");

          getBillCardPanel().setBodyValueAt(null, i, "ts");
          // 设置新插入的表体行币种默认为表头币种
          getBillCardPanel().setBodyValueAt(strCurr, i, "ccurrencytypeid");
          // 设置汇率
          setExchangeRateBody(i, true, null);
        }
        // 单据行号
        BillRowNo.addLineRowNos(getBillCardPanel(), ScmConst.PO_Invoice, "crowno", nPastRowCount);
      }
      showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPP40040401-000058")/*
                                                                                                     * @res
                                                                                                     * (iNewRowCnt -
                                                                                                     * iOldRowCnt) +
                                                                                                     * "行发票被粘贴"
                                                                                                     */);
    }
  }

  /**
   * 上一张
   * 
   * @param 参数说明
   * @return 返回值
   * @exception 异常描述
   * @see 需要参见的其它内容
   * @since 从类的那一个版本，此方法被添加进来。（可选）
   * @deprecated 该方法从类的那一个版本后，已经被其它方法替换。（可选）
   */
  private void onPrevious() {
    // 保存原来的位置
    m_nLstInvVOPos = getCurVOPos();

    setCurVOPos(getCurVOPos() - 1);
    // 设置金额等位数为最大的位数
    setMaxMnyDigit(iMaxMnyDigit);

    // 设置卡片界面数据
    setVOToBillPanel();
  }

  /**
   * 查询当前单据审批状态
   */

  public void onQueryForAudit() {

    if (getInvVOs() != null && getInvVOs().length > 0) {
      // 如果该单据处于正在审批状态，执行下列语句：
      // by zhaoyha 增加业务流程
      nc.ui.pub.workflownote.FlowStateDlg approvestatedlg = new nc.ui.pub.workflownote.FlowStateDlg(this, "25",
          getInvVOs()[getCurVOPos()].getHeadVO().getCbiztype(),getInvVOs()[getCurVOPos()].getHeadVO().getPrimaryKey());
      approvestatedlg.showModal();
      showHintMessage(NCLangRes.getInstance().getStrByID("common", "UCH035")/*
                                                                             * @res
                                                                             * "审批状态查询成功"
                                                                             */);
    }
    else {
      showHintMessage(NCLangRes.getInstance().getStrByID("40040101", "UPP40040101-000539")/*
                                                                                           * @res
                                                                                           * "界面无数据"
                                                                                           */);
    }

  }

  /**
   * 保存当前发票
   * 
   * @param 参数说明
   * @return 返回值
   * @exception 异常描述
   * @see 需要参见的其它内容
   * @since 从类的那一个版本，此方法被添加进来。（可选）
   */
  private boolean onSave() {

    nc.vo.scm.pu.Timer timer = new nc.vo.scm.pu.Timer();
    timer.start("采购发票保存开始");

    // showHintMessage(getHeadHintText() + CommonConstant.SPACE_MARK +
    // nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401","UPP40040401-000059")/*@res
    // "保存前校验"*/ + CommonConstant.SPACE_MARK);
    // 终止编辑
    getBillCardPanel().stopEditing();
    // 过虑空行
    filterNullLine();

    if (!BillRowNo.verifyRowNosCorrect(getBillCardPanel(), "crowno")) {
      return false;
    }

    int nRow = getBillCardPanel().getRowCount();
    for (int i = 0; i < nRow; i++) {
      UFDouble nNum = new UFDouble(0), nMoney = new UFDouble(0);
      Object oTemp = getBillCardPanel().getBodyValueAt(i, "ninvoicenum");
      if (oTemp != null)
        nNum = new UFDouble(oTemp.toString());
      oTemp = getBillCardPanel().getBodyValueAt(i, "noriginalcurmny");
      if (oTemp != null)
        nMoney = new UFDouble(oTemp.toString());
      if (nNum.doubleValue() * nMoney.doubleValue() < 0) {
        MessageDialog.showWarningDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401",
            "UPPSCMCommon-000132")/* @res "警告" */, nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401",
            "UPP40040401-000253")/* @res"数量和金额方向不一致!" */);
        return false;
      }
    }

    // 得到需保存的VO
    InvoiceVO willSavedVO = getSavedVO();
    if (willSavedVO == null) {
      showHintMessage(getHeadHintText() + CommonConstant.SPACE_MARK
          + nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPP40040401-000060")/*
                                                                                           * @res
                                                                                           * "编辑发票"
                                                                                           */
          + CommonConstant.SPACE_MARK);
      return false;
    }

    timer.addExecutePhase("得到需要保存的VO");

    // showHintMessage(getHeadHintText() + CommonConstant.SPACE_MARK +
    // nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401","UPP40040401-000062")/*@res
    // "发票保存"*/ + CommonConstant.SPACE_MARK + CommonConstant.BEGIN_MARK +
    // "........" + CommonConstant.END_MARK);

    // 参数ArrayList
    // 0 ArrayList
    // 1 ArrayList
    // 2 ArrayList 作结算用的参数ArrayList 0当前日期,1是否集团,2暂估处理方式,3差异转入方式
    ArrayList paraList = new ArrayList();
    paraList.add(null);
    paraList.add(null);
    // 结果LIST 0主键 1发票VO 2作容差时的ArrayLIST
    ArrayList retList = null;

    InvoiceVO vos = null;

    try {
      // 参数LIST
      if (paraList.size() != 3) {
        // 结算LIST
        ArrayList forSettleList = new ArrayList();
        forSettleList.add(nc.ui.pub.ClientEnvironment.getInstance().getDate());
        forSettleList.add(new UFBoolean(nc.ui.pub.ClientEnvironment.getInstance().isGroup()));
        String sKey = willSavedVO.getHeadVO().getCinvoiceid();
        if (sKey == null || sKey.trim().length() == 0)
          forSettleList.add(new UFBoolean(false));
        else
          forSettleList.add(new UFBoolean(true));

        paraList.add(forSettleList);
      }
      // 是否保留最初制单人
      if (willSavedVO != null
          && willSavedVO.getHeadVO() != null
          && !isAllowedModifyByOther
          && ((InvoiceHeaderVO) willSavedVO.getParentVO()).getIbillstatus().equals(BillStatus.FREE)) {
        ((InvoiceHeaderVO) willSavedVO.getParentVO()).setCoperator(nc.ui.pub.ClientEnvironment.getInstance().getUser()
            .getPrimaryKey());
      }
      // 为满足审批流，传入当前操作员
      ((InvoiceHeaderVO) willSavedVO.getParentVO()).setCuserid(nc.ui.pub.ClientEnvironment.getInstance().getUser()
          .getPrimaryKey());
      // 为判断是否可修改、作废其他人单据
      ((InvoiceHeaderVO) willSavedVO.getParentVO()).setCoperatoridnow(nc.ui.pub.ClientEnvironment.getInstance()
          .getUser().getPrimaryKey());

      // 支持供应商核准检查
      paraList.add(new Integer(0));
      paraList.add("cvendormangid");

      InvoiceVO oldVO = getInvVOs()[getCurVOPos()];
      if (oldVO != null)
        paraList.add(oldVO);
      else
        paraList.add(null);
      // 发票同正负控制
      // if(!negativeAndPlusCtrl(willSavedVO)){
      // MessageDialog.showWarningDlg(this,nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401","UPPSCMCommon-000132")/*@res
      // "警告"*/,nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401","UPP40040401-000063")/*@res
      // "存在发票数量和来源单据数量正负不一致,请检查！"*/);
      // return false;
      // }

      timer
          .addExecutePhase(nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPP40040401-000103")/*
                                                                                                         * @res
                                                                                                         * "保存发票前的准备"
                                                                                                         */);

      // 设置表头税率
      String ntaxrate = getBillCardPanel().getHeadItem("ntaxrate").getValue();
      /**
       * 二次开发插件支持 by zhaoyha at 2009.2.16
       */
      getInvokeEventProxy().beforeAction(nc.vo.scm.plugin.Action.SAVE, new AggregatedValueObject[]{willSavedVO});

      InvoiceItemVO tempVO[] = willSavedVO.getBodyVO();
      if (tempVO[0].getCupsourcebilltype() != null) {
        boolean bConfirm = false;
        willSavedVO.setUserConfirmFlag(new UFBoolean(false));
        while (!willSavedVO.getUserConfirmFlag().booleanValue()) {
          try {
            if (bConfirm)
              willSavedVO.setUserConfirmFlag(new UFBoolean(true));
            retList = (ArrayList) PfUtilClient.processActionNoSendMessage(this, "SAVEBASE",
                nc.vo.scm.pu.BillTypeConst.PO_INVOICE, ClientEnvironment.getInstance().getDate().toString(),
                willSavedVO, paraList, null, null);
          }
          catch (RwtPiToPoException ex) {
            SCMEnv.out(ex);
            if (MessageDialog.showYesNoDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401",
                "UPPSCMCommon-000270")/* @res "提示" */, ex.getMessage()) == MessageDialog.ID_YES) {
              // 继续保存
              bConfirm = true;
            }
            else {
              return false;
            }
          }
          catch (RwtPiToScException ex) {
            SCMEnv.out(ex);
            if (MessageDialog.showYesNoDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401",
                "UPPSCMCommon-000270")/* @res "提示" */, ex.getMessage()) == MessageDialog.ID_YES) {
              // 继续保存
              bConfirm = true;
            }
            else {
              return false;
            }
          }
          catch (nc.vo.pu.exception.MaxStockException ex) {
            SCMEnv.out(ex);
            if (MessageDialog.showYesNoDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401",
                "UPPSCMCommon-000270")/* @res "提示" */, ex.getMessage()) == MessageDialog.ID_YES) {
              // 继续保存
              bConfirm = true;
            }
            else {
              return false;
            }
          }
          if (!bConfirm)
            break;// 未抛异常,跳出
        }
      }
      else {
        retList = (ArrayList) PfUtilClient.processActionNoSendMessage(this, "SAVEBASE",
            nc.vo.scm.pu.BillTypeConst.PO_INVOICE, ClientEnvironment.getInstance().getDate().toString(), willSavedVO,
            paraList, null, null);
      }
      timer
          .addExecutePhase(nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPP40040401-000029")/*
                                                                                                         * @res
                                                                                                         * "保存发票"
                                                                                                         */);

      timer
          .addExecutePhase(nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPP40040401-000104")/*
                                                                                                         * @res
                                                                                                         * "送审发票"
                                                                                                         */);
      vos = (InvoiceVO) retList.get(1);

      // 用于审批流批配置了条件函数，当满足条件时制单人可以直接审批单据，这时需要刷新显示
      // ArrayList arrRet = InvoiceHelper.queryForSaveAudit(sInvoiceId);
      ((InvoiceHeaderVO) vos.getParentVO()).setDauditdate(vos.getHeadVO().getDauditdate());
      ((InvoiceHeaderVO) vos.getParentVO()).setCauditpsn(vos.getHeadVO().getCauditpsn());
      ((InvoiceHeaderVO) vos.getParentVO()).setIbillstatus(vos.getHeadVO().getIbillstatus());
      ((InvoiceHeaderVO) vos.getParentVO()).setTs(vos.getHeadVO().getTs());
      // 设置表头税率
      UFDouble ntaxrateUF = null;
      if (ntaxrate != null && ntaxrate.length() > 0) {
        ntaxrateUF = new UFDouble(ntaxrate);
        ((InvoiceHeaderVO) vos.getParentVO()).setNtaxrate(ntaxrateUF);
      }

      if (vos != null) {
        InvoiceVO tempVOs[] = getInvVOs();
        tempVOs[getCurVOPos()] = vos;
        setInvVOs(tempVOs);
        getInvVOs()[getCurVOPos()].setSource(InvoiceVO.FROM_QUERY);
        // 佛山碧桂园:记录业务日志
        // 效率优化，移到后台执行 by zhaoyha at 2009.8
//        vos.getOperatelogVO().setOpratorname(nc.ui.pub.ClientEnvironment.getInstance().getUser().getUserName());
//        vos.getOperatelogVO().setCompanyname(nc.ui.pub.ClientEnvironment.getInstance().getCorporation().getUnitname());
//        vos.getOperatelogVO().setOperatorId(nc.ui.pub.ClientEnvironment.getInstance().getUser().getPrimaryKey());
//        Operlog operlog = new Operlog();
//        operlog.insertBusinessExceptionlog(vos, "保存", "保存", nc.vo.scm.funcs.Businesslog.MSGMESSAGE,
//            nc.vo.scm.pu.BillTypeConst.PO_INVOICE, nc.ui.sm.cmenu.Desktop.getApplet().getParameter("USER_IP"));
      }

    }
    catch (ValidationException e) {
      SCMEnv.out(e);
      MessageDialog.showErrorDlg(this,
          nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPPSCMCommon-000270")/*
                                                                                         * @res
                                                                                         * "提示"
                                                                                         */, e.getMessage());
      return false;
    }
    catch (Exception e) {
      SCMEnv.out(e);
      if (e instanceof BusinessException
          || e instanceof java.rmi.RemoteException
          || (e.getMessage() != null && (e.getMessage().indexOf(
              nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UC000-0000275")/*
                                                                                     * @res
                                                                                     * "供应商"
                                                                                     */) >= 0
              || e.getMessage()
                  .indexOf(nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPP40040401-000165")/*
                                                                                                         * @res
                                                                                                         * "采购"
                                                                                                         */) >= 0
              || e.getMessage()
                  .indexOf(nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPP40040401-000166")/*
                                                                                                         * @res
                                                                                                         * "发票"
                                                                                                         */) >= 0
              || e.getMessage()
                  .indexOf(nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPP40040401-000110")/*
                                                                                                         * @res
                                                                                                         * "订单"
                                                                                                         */) >= 0
              || e.getMessage()
                  .indexOf(nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPP40040401-000167")/*
                                                                                                         * @res
                                                                                                         * "核准"
                                                                                                         */) >= 0 || e
              .getMessage()
              .indexOf(nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPP40040401-000168")/*
                                                                                                     * @res
                                                                                                     * "结算"
                                                                                                     */) >= 0))) {
        MessageDialog.showErrorDlg(this,
            nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPPSCMCommon-000270")/*
                                                                                           * @res
                                                                                           * "提示"
                                                                                           */, e
                .getMessage());
      }
      else {
        MessageDialog.showErrorDlg(this,
            nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPPSCMCommon-000270")/*
                                                                                           * @res
                                                                                           * "提示"
                                                                                           */,
            nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPP40040401-000064")/*
                                                                                           * @res
                                                                                           * "保存未成功！"
                                                                                           */);
      }
      return false;
    }

    // 进行界面显示状态切换:
    // 如果其他单据转入,则从缓存中去除该张发票,并转入列表状态
    if (willSavedVO.getSource() == InvoiceVO.FROM_HAND) {
      // 保存后的VO
      InvoiceVO resultVO = null;
      if (!PfUtilClient.isSuccess()) { // 用户选择“否”
        removeOneFromInvVOs(getCurVOPos());
        // 设置卡片界面数据
        setCurOperState(getBillBrowseState());
        setVOToBillPanel();
      }
      else {
        // resultVO = (InvoiceVO) retList.get(1);
        resultVO = vos;
        resultVO.setSource(InvoiceVO.FROM_QUERY);
        getInvVOs()[getCurVOPos()] = resultVO;
        setCurOperState(getBillBrowseState());
        // 设置卡片界面数据
        setVOToBillPanel();
      }
    }
    else if (willSavedVO.getSource() == InvoiceVO.FROM_QUERY) {
      // 保存后的VO
      InvoiceVO resultVO = null;
      if (!PfUtilClient.isSuccess()) { // 用户选择“否”
        setCurOperState(getBillBrowseState());
        // 设置卡片界面数据
        setVOToBillPanel();
      }
      else {
        // resultVO = (InvoiceVO) retList.get(1);
        resultVO = vos;
        getInvVOs()[getCurVOPos()] = resultVO;
        setCurOperState(getBillBrowseState());
        if(getListOperState()==STATE_LIST_FROM_BILLS)
          setListOperState(STATE_LIST_NORMAL);
        // 设置卡片界面数据
        setVOToBillPanel();
      }
    }
    else {

      /** ***********修改转入操作易用性**************** */
      // 将成功转单的单据保存在对应位置的缓存中
      // InvoiceVO resultVO = (InvoiceVO) retList.get(1);
      InvoiceVO resultVO = vos;
      resultVO.setSource(InvoiceVO.FROM_QUERY);
      // getInvVOs()[getCurVOPos()] = resultVO;cunpos
      if (cunpos == 0)
        getInvVOs()[getCurVOPos()] = resultVO;
      else
        getInvVOs()[cunpos] = resultVO;

      if (m_vSavedVO == null)
        m_vSavedVO = new Vector();
      m_vSavedVO.addElement(resultVO);
      removeOneFromInvVOs(getCurVOPos());

      // 检查是否还有单据没有转单完成.
      if (getInvVOs() != null && getInvVOs().length > 0) {
        // 设置操作状态
        setCurOperState(getListOperState());
        shiftShowModeTo(INV_PANEL_LIST);
        // 设置列表界面数据
        setSpiltVOsToListPanel();
      }
      else {
        setCurOperState(getBillBrowseState());
        setListOperState(STATE_LIST_NORMAL);

        InvoiceVO tempVO[] = new InvoiceVO[m_vSavedVO.size()];
        m_vSavedVO.copyInto(tempVO);
        setInvVOs(tempVO);
        setCurVOPos(tempVO.length - 1);
        m_vSavedVO = null;

        // 设置卡片界面数据
        setVOToBillPanel();
      }
      cunpos = 0;
    }
    timer.addExecutePhase("保存后的显示操作");
    /**
     * 二次开发插件支持 by zhaoyha at 2009.2.16
     */
    try {
      getInvokeEventProxy().afterAction(nc.vo.scm.plugin.Action.SAVE, new AggregatedValueObject[]{willSavedVO});
    }
    catch (BusinessException e) {
      //日志异常
      nc.vo.scm.pub.SCMEnv.out(e);
      //按规范抛出异常
      return false;
    }


    // showHintMessage(getHeadHintText() + CommonConstant.SPACE_MARK +
    // nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401","UPPSCMCommon-000006")/*@res
    // "保存成功"*/ + CommonConstant.SPACE_MARK);
    timer.showAllExecutePhase("采购发票保存结束");
    m_bAdd = false;
    m_bCopy = false;
    return true;

  }

  /**
   * 列表状态下选择所有发票
   * 
   * @param 参数说明
   * @return 返回值
   * @exception 异常描述
   * @see 需要参见的其它内容
   * @since 从类的那一个版本，此方法被添加进来。（可选）
   * @deprecated 该方法从类的那一个版本后，已经被其它方法替换。（可选）
   */
  private void onSelectAll() {

    int iLen = getInvVOs().length;
    // 设为全部选中
    getBillListPanel().getHeadTable().setRowSelectionInterval(0, iLen - 1);
  }

  /**
   * 列表状态下所有发票均不被先中
   * 
   * @param 参数说明
   * @return 返回值
   * @exception 异常描述
   * @see 需要参见的其它内容
   * @since 从类的那一个版本，此方法被添加进来。（可选）
   * @deprecated 该方法从类的那一个版本后，已经被其它方法替换。（可选）
   */
  private void onSelectNo() {
    int iLen = getInvVOs().length;
    // 设为全部选中
    getBillListPanel().getHeadTable().removeRowSelectionInterval(0, iLen - 1);
  }

  /**
   * 此处插入方法说明。 创建日期：(2003-10-29 12:10:06)
   */
  private boolean onSendAudit(InvoiceVO vo) {
    // //填审批人与审批日期
    // String strOperPk =
    // nc.ui.pub.ClientEnvironment.getInstance().getUser().getPrimaryKey();
    // UFDate today = nc.ui.pub.ClientEnvironment.getInstance().getDate();

    String billid = null;
    if (vo != null && vo.getHeadVO() != null) {
      billid = vo.getHeadVO().getCinvoiceid();
    }

    boolean isNeedSendToAuditQ = BusiBillManageTool.isNeedSendToAudit(ScmConst.PO_Invoice, getPk_corp(),
        getCurBizeType(), billid, getCurOperator());
    try {
      // 编辑状态送审＝“保存”＋“送审”
      if (vo == null && m_btnBillMaintain.isEnabled() == true) {
        boolean bContinue = onSave();
        if (!bContinue) {
          return false;
        }
        vo = getInvVOs()[getCurVOPos()];
      }

      else if (getCurOperState() == STATE_EDIT && isNeedSendToAuditQ && vo.getHeadVO().getIbillstatus() != null
          && (vo.getHeadVO().getIbillstatus().intValue() == 0 || vo.getHeadVO().getIbillstatus().intValue() == 4)) {
        boolean bContinue = onSave();
        if (!bContinue) {
          return false;
        }
        vo = getInvVOs()[getCurVOPos()];
      }

      if (isNeedSendToAuditQ && vo.getHeadVO().getIbillstatus() != null
          && (vo.getHeadVO().getIbillstatus().intValue() == 0 || vo.getHeadVO().getIbillstatus().intValue() == 4)) {
        
        Object[] retValues = (Object[]) PfUtilClient.processAction("SAVE", nc.vo.scm.pu.BillTypeConst.PO_INVOICE,
            ClientEnvironment.getInstance().getDate().toString(), vo);
        //
        InvoiceVO resultVO = null;
        if (PfUtilClient.isSuccess()) {
          updateCacheVo((Map<String, Map<String, Object>>) retValues[1]);
//          resultVO = InvoiceHelper.findByPrimaryKey(getInvVOs()[getCurVOPos()].getHeadVO().getPrimaryKey());
//          getInvVOs()[getCurVOPos()] = resultVO;
          
        }
      }
      m_btnSendAudit.setEnabled(false);// 送审
      m_btnInvBillAudit.setEnabled(false);// 审批
      m_btnInvBillUnAudit.setEnabled(true);// 弃审
      // isAlreadySendToAudit =true;
    }
    catch (Exception e) {
      SCMEnv.out(e);
      MessageDialog.showErrorDlg(this,
          nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPPSCMCommon-000270")/*
                                                                                         * @res
                                                                                         * "提示"
                                                                                         */, e.getMessage());
      showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPP40040401-000257")/*
                                                                                                     * @res
                                                                                                     * "送审未成功！"
                                                                                                     */);
      return false;
    }
    return true;
  }

  /**
   * 从缓存中去除一张单据
   * 
   * @param 参数说明
   * @return 缓冲为空，返回－1 移走的是最后一张,返回第一张. 否则返回原有位置.
   * @exception 异常描述
   * @see 需要参见的其它内容
   * @since 从类的那一个版本，此方法被添加进来
   * @return nc.vo.pi.InvoiceVO[]
   */
  private int removeOneFromInvVOs(int removedPos) {

    if (removedPos < 0 || removedPos >= getInvVOs().length)
      return -1;

    if (getInvVOs().length == 1) {
      setInvVOs(null);
      setCurVOPos(-1);
      return -1;
    }
    else {
      InvoiceVO[] vos = new InvoiceVO[getInvVOs().length - 1];
      int j = 0;
      for (int i = 0; i < getInvVOs().length; i++) {
        if (i != removedPos)
          vos[j++] = getInvVOs()[i];
      }
      setInvVOs(vos);

      // 原来显示的是最后一张
      if (removedPos == getInvVOs().length) {
        setCurVOPos(getInvVOs().length - 1);
        return 0;
      }
      else {
        setCurVOPos(removedPos);
        return removedPos;
      }

    }
  }

  /**
   * 从缓存中去除若干张单据
   * 
   * @param 参数说明
   * @return 缓冲为空，返回－1 否则置当前VO位置为第一张
   * @exception 异常描述
   * @see 需要参见的其它内容
   * @since 从类的那一个版本，此方法被添加进来
   * @return nc.vo.pi.InvoiceVO[]
   */
  private int removeSomeFromInvVOs(Integer removedPos[]) {

    if (removedPos == null)
      return 0;

    // 因为removedPos可能未排序，置于一Hashtable中
    Hashtable hashRevIndex = new Hashtable();
    for (int i = 0; i < removedPos.length; i++) {
      hashRevIndex.put(removedPos[i], "");
    }

    // 保留的放于一VECTOR中
    Vector reservedVEC = new Vector();

    if (getInvVOs() != null && getInvVOs().length > 0) {
      for (int i = 0; i < getInvVOs().length; i++) {
        if (!hashRevIndex.containsKey(new Integer(i)))
          reservedVEC.addElement(getInvVOs()[i]);
      }
    }
    // 已全部去掉
    if (reservedVEC.size() == 0) {
      setInvVOs(null);
      return -1;
    }

    // 重设缓存
    InvoiceVO[] vos = new InvoiceVO[reservedVEC.size()];
    reservedVEC.copyInto(vos);
    setInvVOs(vos);
    setCurVOPos(0);
    return 0;
  }

  /**
   * 此处插入方法说明。 创建日期：(2001-9-19 9:38:47)
   * 
   * @param newBillBrowseState
   *          java.lang.String
   */
  private void setBillBrowseState(int newBillBrowseState) {
    m_nBillBrowseState = newBillBrowseState;
  }

  /**
   * 作者：王印芬 功能：对按钮状态及PANEL是否可用进行设置 参数：无 返回：无 例外：无 日期：(2002-3-13 11:39:21)
   * 修改日期，修改人，修改原因，注释标志：
   */
  private void setButtonsAndPanelState() {
    int iVal = -999;// 支持产业链功能扩展
    boolean isNeedSendToAuditQ = false;
    if (getCurOperState() == STATE_INIT) {
      // showHintMessage(getHeadHintText() + CommonConstant.SPACE_MARK +
      // nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401","UPP40040401-000065")/*@res
      // "初始化"*/ + CommonConstant.SPACE_MARK);
      setButtonsStateInit();
      getBillCardPanel().setEnabled(false);
      iVal = 0;
    }
    else if (getCurOperState() == STATE_BROWSE_NORMAL) {
      // showHintMessage(getHeadHintText() + CommonConstant.SPACE_MARK +
      // nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401","UPP40040401-000032")/*@res
      // "浏览发票"*/ + CommonConstant.SPACE_MARK);
      isNeedSendToAuditQ = setSendAuditBtnState();
      setButtonsStateBrowseNormal();
      Integer iBillStatus = new Integer(88);
      String billid = null;
      if (getInvVOs() != null && getCurVOPos() > -1) {
        InvoiceVO vo = getInvVOs()[getCurVOPos()];

        // Integer iBillStatus = new Integer(88);
        if (vo != null && vo.getHeadVO() != null) {
          iBillStatus = vo.getHeadVO().getIbillstatus();
          billid = vo.getHeadVO().getCinvoiceid();
        }
      }

      // if((iBillStatus.compareTo(nc.vo.scm.pu.BillStatus.FREE) == 0 ||
      // (iBillStatus.compareTo(nc.vo.scm.pu.BillStatus.AUDITFAIL) == 0))){
      // if(iBillStatus.compareTo(nc.vo.scm.pu.BillStatus.FREE) == 0 ){
      // m_btnSendAudit.setEnabled(isNeedSendToAuditQ);}
      // else if((iBillStatus.compareTo(nc.vo.scm.pu.BillStatus.AUDITFAIL) == 0)
      // || (iBillStatus.compareTo(nc.vo.scm.pu.BillStatus.AUDITFAIL) == 0)){
      // m_btnSendAudit.setEnabled(false);
      // }
      // }
      //审批中  但是没有审批人，可以审批，不可以弃审
      if (!isNeedSendToAuditQ 
          && (iBillStatus.compareTo(nc.vo.scm.pu.BillStatus.AUDITING) == 0) 
          && PuPubVO.getString_TrimZeroLenAsNull(getInvVOs()[getCurVOPos()].getHeadVO().getCauditpsn()) == null) {
        m_btnInvBillAudit.setEnabled(true);
        m_btnInvBillUnAudit.setEnabled(false);
        // }else {
        // m_btnInvBillAudit.setEnabled(false);
        // m_btnInvBillUnAudit.setEnabled(true);
      }//审批中  有审批人，可以审批，可以弃审
      else if(!isNeedSendToAuditQ 
          && (iBillStatus.compareTo(nc.vo.scm.pu.BillStatus.AUDITING) == 0) 
          && PuPubVO.getString_TrimZeroLenAsNull(getInvVOs()[getCurVOPos()].getHeadVO().getCauditpsn()) != null){
        m_btnInvBillAudit.setEnabled(true);
          m_btnInvBillUnAudit.setEnabled(true);
      }
      getBillCardPanel().setEnabled(false);
      iVal = 1;
      if (getInvVOs() == null || getInvVOs().length == 0)
        setButtonsStateInit();
    }
    else if (getCurOperState() == STATE_EDIT) {
      // showHintMessage(getHeadHintText() + CommonConstant.SPACE_MARK +
      // nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401","UPP40040401-000060")/*@res
      // "编辑发票"*/ + CommonConstant.SPACE_MARK);

      setButtonsStateEdit();

      getBillCardPanel().setEnabled(true);

      Integer iBillStatus = new Integer(88);
      String cbizType = null;
      String billid = null;
      if (getInvVOs() != null && getCurVOPos() > -1) {
        InvoiceVO vo = getInvVOs()[getCurVOPos()];
        if (vo != null && vo.getHeadVO() != null) {
          iBillStatus = vo.getHeadVO().getIbillstatus();
          cbizType = vo.getHeadVO().getCbiztype();
          billid = vo.getHeadVO().getCinvoiceid();
        }
      }
      if (m_bAdd) {
        isNeedSendToAuditQ = BusiBillManageTool.isNeedSendToAudit(ScmConst.PO_Invoice, getPk_corp(), 
            m_bizButton.getTag(),
            null, getClientEnvironment().getUser().getPrimaryKey());
      }
      else {
        isNeedSendToAuditQ = setSendAuditBtnState();
      }

      if (m_bAdd
          || (iBillStatus.compareTo(nc.vo.scm.pu.BillStatus.FREE) == 0 || (iBillStatus
              .compareTo(nc.vo.scm.pu.BillStatus.AUDITFAIL) == 0))) {
        m_btnSendAudit.setEnabled(isNeedSendToAuditQ);
      }

      // 本币信息不可编辑
      getBillCardPanel().getBodyItem("nmoney").setEnabled(false);
      getBillCardPanel().getBodyItem("ntaxmny").setEnabled(false);
      getBillCardPanel().getBodyItem("nsummny").setEnabled(false);
      m_btnAudit.setEnabled(false);

      // 设置散户及折本汇率等的可用状态
      setEditableWhenEdit();
      iVal = 2;

    }
    else if (getCurOperState() == STATE_LIST_NORMAL) {
      // showHintMessage(getHeadHintText() + CommonConstant.SPACE_MARK +
      // nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401","UPP40040401-000032")/*@res
      // "浏览发票"*/ + CommonConstant.SPACE_MARK);
      setButtonsStateListNormal();
      getBillCardPanel().setEnabled(false);
      setButtonsStateBrowseNormal();
      iVal = 3;
    }
    else if (getCurOperState() == STATE_LIST_FROM_BILLS) {
      // showHintMessage(getHeadHintText() + CommonConstant.SPACE_MARK +
      // nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401","UPP40040401-000032")/*@res
      // "浏览发票"*/ + CommonConstant.SPACE_MARK);
      setButtonsStateListFromBills();
      getBillCardPanel().setEnabled(false);
      iVal = 4;
    }
    setExtendBtnsStat(iVal);
  }

  /**
   * 作者：王印芬 功能：对浏览状态的按钮进行设置 参数：无 返回：无 例外：无 日期：(2002-3-13 11:39:21)
   * 修改日期，修改人，修改原因，注释标志： 2002-07-16 wyf 加入对审批未通过的处理 2002-09-26 wyf
   * 加入对虚拟发票的处理，同已审批发票，但复制不可用
   */
  private void setButtonsStateBrowseNormal() {
    if (getCurPanelMode() == INV_PANEL_CARD) {
      m_btnInvBillBusiType.setEnabled(true);// 业务类型
      m_btnInvSelectAll.setEnabled(false);
      m_btnInvDeselectAll.setEnabled(false);
      m_btnInvBillConversion.setEnabled(false);// 放弃转单
      m_btnInvShift.setName(NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000464")/** @res* "列表显示" */
      );
    }
    else if (getCurPanelMode() == INV_PANEL_LIST) {
      m_btnInvBillBusiType.setEnabled(false);// 业务类型
      m_btnInvSelectAll.setEnabled(true);
      m_btnInvDeselectAll.setEnabled(true);
      m_btnInvShift.setName(NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000463")/** @res* "卡片显示" */
      );
    }

    m_btnInvBillExcute.setEnabled(true);
    // 增加
    if (getCurPanelMode() == INV_PANEL_LIST || m_btnInvBillBusiType.isVisible() == false || getCurBizeType() == null) {
      // 没有业务类型则增加按钮可用
      m_btnInvBillNew.setEnabled(false);
    }
    else if (getCurPanelMode() == INV_PANEL_CARD) {
      m_btnInvBillNew.setEnabled(true);
    }
    // 保存
    m_btnInvBillSave.setEnabled(false);
    // 放弃
    m_btnInvBillCancel.setEnabled(false);
    // 行操作
    m_btnInvBillRowOperation.setEnabled(false);
    // 打印
    m_btnInvBillPrint.setEnabled(true);
    // 查询
    m_btnInvBillQuery.setEnabled(true);
    // 列表
    m_btnInvShift.setEnabled(true);

    // 优质优价取价
    m_btnHqhp.setEnabled(false);
    // 刷新
    if (isEverQueryed()) {
      m_btnInvBillRefresh.setEnabled(true);
    }
    else {
      m_btnInvBillRefresh.setEnabled(false);
    }
    // 翻页
    // m_btnInvBillPageOperation.setEnabled(true);
    m_btnQueryForAudit.setEnabled(true);
    // 合并显示
    btnBillCombin.setEnabled(true);
    // 首上下末张
    if (getInvVOs() == null) {
      // 作废
      m_btnInvBillDiscard.setEnabled(false);
      // 修改
      m_btnInvBillModify.setEnabled(false);
      // 单据复制
      m_btnInvBillCopy.setEnabled(false);

      // 翻页
      // m_btnInvBillPageOperation.setEnabled(false);
      m_btnInvBillGoFirstOne.setEnabled(false);
      m_btnInvBillGoPreviousOne.setEnabled(false);
      m_btnInvBillGoNextOne.setEnabled(false);
      m_btnInvBillGoLastOne.setEnabled(false);

      // 文档管理
      m_btnDocManage.setEnabled(false);
      // 联查按钮
      m_btnLnkQuery.setEnabled(false);
    }
    else {

      // 设置联查按钮状态
      m_btnLnkQuery.setEnabled(true);
      // 文档管理
      m_btnDocManage.setEnabled(true);

      // ================================单据复制:
      // 1、当前单据的业务类型只有可自制单据时，才可复制。
      // 2、如果当前单据为虚拟单据，不可复制。
      // wyf 2002-09-27 modify begin
      // if(isCouldMakedForABizType(getInvVOs()[getCurVOPos()].getHeadVO().getCbiztype())){
      if (getInvVOs() != null && getInvVOs().length > 0 && getCurVOPos() > -1
          && isCouldMakedForABizType(getInvVOs()[getCurVOPos()].getHeadVO().getCbiztype())
          && !getInvVOs()[getCurVOPos()].isVirtual() && getCurPanelMode() != INV_PANEL_LIST) {
        // wyf 2002-09-27 modify end
        m_btnInvBillCopy.setEnabled(true);
      }
      else {
        m_btnInvBillCopy.setEnabled(false);
      }
      
      // 送审
      Integer iBillStatus = -1;
      if (getInvVOs() != null && getCurVOPos() > -1) {
        InvoiceVO vo = getInvVOs()[getCurVOPos()];

        if (vo != null && vo.getHeadVO() != null) {
          iBillStatus = vo.getHeadVO().getIbillstatus();
        }
      }
      boolean isNeedSendToAuditQ = isNeedSendAudit(iBillStatus);

      // ================================作废、修改
      m_btnInvBillModify.setEnabled(true);
      m_btnInvBillDiscard.setEnabled(true);
      if (getCurVOPos() != -1) {
        int intBillStatus = getInvVOs()[getCurVOPos()].getHeadVO().getIbillstatus().intValue();
        if (intBillStatus == 0) { // 自由

          if (m_btnSendAudit.isEnabled()) {// 送审
            m_btnInvBillAudit.setEnabled(false);// 审批
            m_btnInvBillUnAudit.setEnabled(false);// 弃审
          }
          else if (m_btnInvBillAudit.isEnabled()) {// 审批
            m_btnInvBillUnAudit.setEnabled(false);
          }
          else if (m_btnInvBillUnAudit.isEnabled()) {
            m_btnInvBillAudit.setEnabled(false);
          }
          else if (!m_btnInvBillUnAudit.isEnabled()) {
            m_btnInvBillAudit.setEnabled(true);
          }

          if ((iBillStatus.compareTo(nc.vo.scm.pu.BillStatus.FREE) == 0 || (iBillStatus
              .compareTo(nc.vo.scm.pu.BillStatus.AUDITFAIL) == 0))) {
            if (isNeedSendToAuditQ) {
              m_btnSendAudit.setEnabled(true);
            }
            else {
              m_btnSendAudit.setEnabled(false);
              m_btnInvBillAudit.setEnabled(true);
              m_btnInvBillUnAudit.setEnabled(false);
              m_btnInvBillModify.setEnabled(true);// 修改
              m_btnInvBillDiscard.setEnabled(true);// 作废
            }
          }
          m_btnInvBillModify.setEnabled(true);// 修改
          m_btnInvBillDiscard.setEnabled(true);// 作废
        }
        else if (intBillStatus == 0 || intBillStatus == 4) { // 自由、审批未通过
          m_btnInvBillAudit.setEnabled(true);
          m_btnInvBillUnAudit.setEnabled(false);
          m_btnInvBillModify.setEnabled(true);// 修改
          m_btnInvBillDiscard.setEnabled(true);// 作废

        }
        else if (intBillStatus == 2) {// 正在审批
          m_btnInvBillAudit.setEnabled(true);
          if(PuPubVO.getString_TrimZeroLenAsNull(getInvVOs()[getCurVOPos()].getHeadVO().getCauditpsn()) != null){
            m_btnInvBillUnAudit.setEnabled(true);
          }else{
            m_btnInvBillUnAudit.setEnabled(false);
          }
          m_btnInvBillDiscard.setEnabled(false);// 作废
          //V55，如果审批人为空，即此时是第一个审批人来操作，则允许修改
          BillItem item = getBillCardPanel().getHeadItem("cauditpsn");
          if(item == null){
            item = getBillCardPanel().getTailItem("cauditpsn");
          }
          if(item != null){
              String strAuditPsn = PuPubVO.getString_TrimZeroLenAsNull(item.getValueObject());            
            m_btnInvBillModify.setEnabled(strAuditPsn == null);
          }
        }
        else if (intBillStatus == 3) {// 审批通过
          m_btnInvBillAudit.setEnabled(false);
          m_btnInvBillUnAudit.setEnabled(true);
          m_btnInvBillModify.setEnabled(false);// 修改
          m_btnInvBillDiscard.setEnabled(false);// 作废
        }
      }

      if (getCurVOPos() < 0) {// 列表状态下全选、全消时
        m_btnInvBillModify.setEnabled(false);
        return;

      }

      // 已结算的发票：不可修改，不可作废
      double d1 = 0.0;
      InvoiceItemVO bodyVO[] = getInvVOs()[getCurVOPos()].getBodyVO();
      if (bodyVO != null && bodyVO.length > 0) {
        for (int i = 0; i < bodyVO.length; i++) {
          d1 = 0.0;
          if (getInvVOs()[getCurVOPos()].getBodyVO()[i].getNaccumsettmny() != null) {
            d1 = getInvVOs()[getCurVOPos()].getBodyVO()[i].getNaccumsettmny().doubleValue();
          }
          if (d1 != 0.0) {
            m_btnInvBillModify.setEnabled(false);
            m_btnInvBillDiscard.setEnabled(false);
            break;
          }
        }
      }
      // 新生成的:不能作废
      if (getInvVOs()[getCurVOPos()].getHeadVO().getCinvoiceid() == null
          || getInvVOs()[getCurVOPos()].getHeadVO().getCinvoiceid().trim().length() < 1) {
        m_btnInvBillDiscard.setEnabled(false);
      }
      // 已审批的:不能修改，不能作废
      Integer iStatus = getInvVOs()[getCurVOPos()].getHeadVO().getIbillstatus();
      if (iStatus != null && iStatus.compareTo(nc.vo.scm.pu.BillStatus.FREE) != 0
          && iStatus.compareTo(nc.vo.scm.pu.BillStatus.AUDITFAIL) != 0) { // 自由,审批未通过
        m_btnInvBillDiscard.setEnabled(false);
      }
      // 虚拟发票：不可修改，不可作废
      // since v55, 不可审批，不可弃审，虚拟发票只在结算时维护，产生自无发票结算，删除于作废结算单
      if (getInvVOs()[getCurVOPos()].isVirtual()) {
        m_btnInvBillModify.setEnabled(false);
        m_btnInvBillDiscard.setEnabled(false);
        m_btnInvBillAudit.setEnabled(false);
        m_btnInvBillUnAudit.setEnabled(false);
      }

      // ================================翻页
      if (getInvVOs().length == 1) {
        // 只有一张发票
        // m_btnInvBillPageOperation.setEnabled(false);
        m_btnInvBillGoFirstOne.setEnabled(false); // 首张
        m_btnInvBillGoPreviousOne.setEnabled(false); // 上张
        m_btnInvBillGoNextOne.setEnabled(false); // 下张
        m_btnInvBillGoLastOne.setEnabled(false); // 末张
      }
      else {
        // 已是第一张发票
        // m_btnInvBillPageOperation.setEnabled(true);
        if (getCurVOPos() == 0) {
          m_btnInvBillGoFirstOne.setEnabled(false); // 首张
          m_btnInvBillGoPreviousOne.setEnabled(false); // 上张
          m_btnInvBillGoNextOne.setEnabled(true); // 下张
          m_btnInvBillGoLastOne.setEnabled(true); // 末张
        }
        // 已是最后一张发票
        else if (getCurVOPos() == getInvVOs().length - 1) {
          m_btnInvBillGoFirstOne.setEnabled(true); // 首张
          m_btnInvBillGoPreviousOne.setEnabled(true); // 上张
          m_btnInvBillGoNextOne.setEnabled(false); // 下张
          m_btnInvBillGoLastOne.setEnabled(false); // 末张
        }
        // 中间任何一张
        else {
          m_btnInvBillGoFirstOne.setEnabled(true); // 首张
          m_btnInvBillGoPreviousOne.setEnabled(true); // 上张
          m_btnInvBillGoNextOne.setEnabled(true); // 下张
          m_btnInvBillGoLastOne.setEnabled(true); // 末张
        }
      }
    }
    btnBillAddContinue.setEnabled(false);
    setButtonsBAPFlag();
    setButtonsList();

  }

  /**
   * 对按钮状态进行设置
   * 
   * @param
   * @return
   * @exception 异常描述
   * @see 需要参见的其它内容
   * @since 从类的那一个版本，此方法被添加进来。（可选）
   * @deprecated 该方法从类的那一个版本后，已经被其它方法替换。（可选）
   */
  private void setButtonsStateEdit() {
    // 业务类型
    m_btnInvBillBusiType.setEnabled(false);
    // 增加
    m_btnInvBillNew.setEnabled(false);
    // 修改
    m_btnInvBillModify.setEnabled(false);
    // 作废
    m_btnInvBillDiscard.setEnabled(false);
    // 保存
    m_btnInvBillSave.setEnabled(true);
    // 复制
    m_btnInvBillCopy.setEnabled(false);
    // 放弃
    m_btnInvBillCancel.setEnabled(true);

    // 审批
    m_btnInvBillAudit.setEnabled(false);
    // 弃审
    m_btnInvBillUnAudit.setEnabled(false);
    // 行操作
    m_btnInvBillRowOperation.setEnabled(true);
    // 用于决定该单据是否可以增行等
    setCouldMaked(isCouldMakedForABizType(getCurBizeType()));
    // ======菜单
    m_btnInvBillAddRow.setEnabled(isCouldMaked());
    m_btnInvBillCopyRow.setEnabled(isCouldMaked());
    m_btnInvBillPasteRow.setEnabled(isCouldMaked());
    m_btnInvBillInsertRow.setEnabled(isCouldMaked());
    // 任何时候均可删行
    if (getBillCardPanel().getRowCount() <= 0) {
      m_btnInvBillDeleteRow.setEnabled(false);
    }
    else {
      m_btnInvBillDeleteRow.setEnabled(true);
    }

    // 打印
    m_btnInvBillPrint.setEnabled(false);
    // 查询
    m_btnInvBillQuery.setEnabled(false);
    // 列表
    m_btnInvShift.setEnabled(false);
    // 首上下末张
    // 翻页
    m_btnInvBillGoFirstOne.setEnabled(false);
    m_btnInvBillGoPreviousOne.setEnabled(false);
    m_btnInvBillGoNextOne.setEnabled(false);
    m_btnInvBillGoLastOne.setEnabled(false);
    // 辅助
    if (m_bAdd) {// 自制增加单剧
      m_btnInvSelectAll.setEnabled(false);
      m_btnInvDeselectAll.setEnabled(false);
      // m_btnInvBillAssist.setEnabled(false);
    }
    else {
      m_btnInvSelectAll.setEnabled(true);
      m_btnInvDeselectAll.setEnabled(true);
      m_btnInvBillAudit.setEnabled(false);
      m_btnInvBillAssist.setEnabled(true);
      m_btnInvBillRefresh.setEnabled(false);
      btnBillCombin.setEnabled(false);
      m_btnQueryForAudit.setEnabled(false);
      m_btnInvBillPrint.setEnabled(false);
      m_btnInvBillPreview.setEnabled(false);
      m_btnLnkQuery.setEnabled(false);
      m_btnDocManage.setEnabled(false);
      m_btnHqhp.setEnabled(true);// 优质优价取价
    }

    // 转单按钮可见
    m_btnInvBillConversion.setEnabled(false);

    // 送审
    Integer iBillStatus = -1;
    if (getInvVOs() != null && getCurVOPos() > -1) {
      InvoiceVO vo = getInvVOs()[getCurVOPos()];

      if (vo != null && vo.getHeadVO() != null) {
        iBillStatus = vo.getHeadVO().getIbillstatus();
      }
    }
    boolean isNeedSendToAuditQ = isNeedSendAudit(iBillStatus);

    if (isNeedSendToAuditQ
        && (iBillStatus.compareTo(nc.vo.scm.pu.BillStatus.FREE) == 0 || (iBillStatus
            .compareTo(nc.vo.scm.pu.BillStatus.AUDITFAIL) == 0))) {
      m_btnSendAudit.setEnabled(true);
    }
    setButtonsBAPFlag();

    createAddContinueBtn();
  }

  /**
   * 对按钮状态进行设置
   * 
   * @param
   * @return
   * @exception 异常描述
   * @see 需要参见的其它内容
   * @since 从类的那一个版本，此方法被添加进来。（可选）
   * @deprecated 该方法从类的那一个版本后，已经被其它方法替换。（可选）
   */
  private void setButtonsStateInit() {
	 
    m_btnInvBillCopy.setEnabled(false);
    // 列表下的刷新按钮
    m_btnInvBillRefresh.setEnabled(false);
    m_btnLnkQuery.setEnabled(false);

    if (getCurPanelMode() == INV_PANEL_CARD) {
      m_btnInvBillBusiType.setEnabled(true);// 业务类型
    }
    else if (getCurPanelMode() == INV_PANEL_LIST) {
      m_btnInvBillBusiType.setEnabled(false);// 业务类型
    }

    // 增加
    if (getCurPanelMode() == INV_PANEL_LIST || m_btnInvBillBusiType.isVisible() == false || getCurBizeType() == null) {
      // 没有业务类型则增加按钮可用
      m_btnInvBillNew.setEnabled(false);
    }
    else if (getCurPanelMode() == INV_PANEL_CARD) {
      m_btnInvBillNew.setEnabled(true);
    }
    // 修改
    m_btnInvBillModify.setEnabled(false);
    // 保存
    m_btnInvBillSave.setEnabled(false);
    // 作废
    m_btnInvBillDiscard.setEnabled(false);
    // 放弃
    m_btnInvBillCancel.setEnabled(false);
    // 审批
    m_btnInvBillAudit.setEnabled(false);
    // 弃审
    m_btnInvBillUnAudit.setEnabled(false);
    // 行操作
    m_btnInvBillRowOperation.setEnabled(false);

    // 优质优价取价
    m_btnHqhp.setEnabled(false);

    // //打印
    // m_btnInvBillPrint.setEnabled(true);
    // 查询
    m_btnInvBillQuery.setEnabled(true);
    // 列表
    m_btnInvShift.setEnabled(true);
    // 首上下末张
    // 翻页
    // m_btnInvBillPageOperation.setEnabled(false);
    m_btnInvBillGoFirstOne.setEnabled(false);
    m_btnInvBillGoPreviousOne.setEnabled(false);
    m_btnInvBillGoNextOne.setEnabled(false);
    m_btnInvBillGoLastOne.setEnabled(false);
    // //辅助查询
    // m_btnInvBillAssist.setEnabled(false);
    // 文档管理
    m_btnDocManage.setEnabled(false);
    // 送审
    m_btnSendAudit.setEnabled(false);
    // 合并显示
    btnBillCombin.setEnabled(false);
    // 状态查询
    m_btnQueryForAudit.setEnabled(false);

    btnBillAddContinue.setEnabled(false);

    setButtonsBAPFlag();
  }

  /**
   * 方法功能描述：针对卡片状态和列表状态下重复按钮进行设置
   * <p>
   * <b>examples:</b>
   * <p>
   * 使用示例
   * <p>
   * <b>参数说明</b>
   * <p>
   * 
   * @author lixiaodong
   * @time 2007-3-20 下午03:23:45
   */
  private void setButtonsList() {

    if (getInvVOs() != null && getInvVOs().length > 0) {
      m_btnInvBillPreview.setEnabled(true);// 预览
      m_btnInvBillPrint.setEnabled(true);// 打印
      btnBillCombin.setEnabled(true);// 合并显示
    }
    else {
      m_btnInvBillPreview.setEnabled(false);// 预览
      m_btnInvBillPrint.setEnabled(false);// 打印
      btnBillCombin.setEnabled(false);// 合并显示
      m_btnInvBillAudit.setEnabled(false);// 审批
    }

    if (getCurPanelMode() == INV_PANEL_CARD) {
    }
    else if (getCurPanelMode() == INV_PANEL_LIST) {
      m_btnInvBillGoFirstOne.setEnabled(false);// 首页
      m_btnInvBillGoNextOne.setEnabled(false);// 下一页
      m_btnInvBillGoPreviousOne.setEnabled(false);// 上一页
      m_btnInvBillGoLastOne.setEnabled(false);// 末页
    }

  }

  /**
   * 对按钮状态进行设置:其他单据转入时,此时界面为列表状态
   * 
   * @param
   * @return
   * @exception 异常描述
   * @see 需要参见的其它内容
   * @since 从类的那一个版本，此方法被添加进来。（可选）
   * @deprecated 该方法从类的那一个版本后，已经被其它方法替换。（可选）
   */
  private void setButtonsStateListFromBills() {
    if (getSelectedRowCount() == 0) {
      m_btnInvBillModify.setEnabled(false);
      m_btnInvDeselectAll.setEnabled(false);

      if (getInvVOs() != null && getInvVOs().length > 0) {
        m_btnInvSelectAll.setEnabled(true);
      }
      else {
        m_btnInvSelectAll.setEnabled(false);
      }
    }
    else {
      if (getSelectedRowCount() == 1) {
        m_btnInvBillModify.setEnabled(true);
      }
      else {
        m_btnInvBillModify.setEnabled(false);
      }

      m_btnInvDeselectAll.setEnabled(true);

      if (getInvVOs() != null && getInvVOs().length > getSelectedRowCount()) {
        m_btnInvSelectAll.setEnabled(true);
      }
      else {
        m_btnInvSelectAll.setEnabled(false);
      }
    }

    m_btnInvBillBusiType.setEnabled(false);// 业务类型
    m_btnInvBillNew.setEnabled(false);

    // 转单按钮可见
    m_btnInvBillConversion.setEnabled(true);
    // 审核
    m_btnInvBillAudit.setEnabled(false);
    // 送审
    m_btnSendAudit.setEnabled(false);
    // 查询
    m_btnInvBillQuery.setEnabled(false);
    // 作废
    m_btnInvBillDiscard.setEnabled(false);
    // 刷新
    m_btnInvBillRefresh.setEnabled(false);
    // 切换
    m_btnInvShift.setEnabled(false);

    m_btnDocManage.setEnabled(false);

    // 取消
    m_btnInvBillCancel.setEnabled(false);
    // 复制
    m_btnInvBillCopy.setEnabled(false);
    // 保存
    m_btnInvBillSave.setEnabled(false);
  }

  /**
   * 对按钮状态进行设置
   * 
   * @param
   * @return
   * @exception 异常描述
   * @see 需要参见的其它内容
   * @since 从类的那一个版本，此方法被添加进来。（可选）
   * @deprecated 该方法从类的那一个版本后，已经被其它方法替换。（可选）
   */
  private void setButtonsStateListNormal() {

    if (getInvVOs() == null) {
      m_btnInvSelectAll.setEnabled(false);
      m_btnInvDeselectAll.setEnabled(false);
      m_btnInvBillDiscard.setEnabled(false);
      m_btnInvBillModify.setEnabled(false);
      m_btnInvShift.setEnabled(true);
      m_btnDocManage.setEnabled(false);
      m_btnQueryForAudit.setEnabled(false);
    }
    else {

      if (getSelectedRowCount() == 0) {
        m_btnInvSelectAll.setEnabled(true);
        m_btnInvDeselectAll.setEnabled(false);
        m_btnInvBillDiscard.setEnabled(false);
        m_btnInvBillModify.setEnabled(false);
        m_btnInvShift.setEnabled(false);
        m_btnDocManage.setEnabled(false);
        m_btnQueryForAudit.setEnabled(false);
      }
      else {
        m_btnDocManage.setEnabled(true);
        m_btnInvDeselectAll.setEnabled(true);

        if (getSelectedRowCount() == getInvVOs().length) {
          m_btnInvSelectAll.setEnabled(false);
        }
        else {
          m_btnInvSelectAll.setEnabled(true);
        }

        boolean bModify = true;
        if (getSelectedRowCount() == 1 && getInvVOs()[getCurVOPos()].getBodyVO() != null) {
          bModify = isModifyButnEditableList(new InvoiceVO[] {
            getInvVOs()[getCurVOPos()]
          });
          m_btnInvShift.setEnabled(true);
        }
        else {
          ArrayList list = getSelectedBills();
          InvoiceVO VO[] = new InvoiceVO[list.size()];
          list.toArray(VO);
          bModify = isModifyButnEditableList(VO);
          m_btnInvShift.setEnabled(false);
        }
        m_btnInvBillModify.setEnabled(bModify);

        if (getSelectedRowCount() == 1) {
          m_btnQueryForAudit.setEnabled(true);
          m_btnLnkQuery.setEnabled(true);
        }
        else {
          m_btnQueryForAudit.setEnabled(false);
          m_btnInvBillModify.setEnabled(false);
        }
      }
    }
    // 查询
    m_btnInvBillQuery.setEnabled(true);
    m_btnInvBillConversion.setEnabled(false);
    // 刷新
    if (isEverQueryed()) {
      m_btnInvBillRefresh.setEnabled(true);
    }
    else {
      m_btnInvBillRefresh.setEnabled(false);
    }

    setButtonsList();
  }

  private boolean isModifyButnEditableList(InvoiceVO VO[]) {
    boolean bModify = true;

    for (int i = 0; i < VO.length; i++) {
      if (VO[i].getHeadVO().getIbillstatus().intValue() != 0) {
        bModify = false;
        break;
      }
      else {
        InvoiceItemVO bodyVO[] = VO[i].getBodyVO();
        for (int j = 0; j < bodyVO.length; j++) {
          if (bodyVO[j].getNaccumsettmny() != null && Math.abs(bodyVO[j].getNaccumsettmny().doubleValue()) > 0) {
            bModify = false;
            break;
          }
        }
      }
    }

    return bModify;
  }

  /**
   * 子类实现该方法，返回业务界面的标题。
   * 
   * @version (00-6-6 13:33:25)
   * @return java.lang.String
   */
  private void setCauditid(String newId) {

    m_cauditid = newId;
  }

  /**
   * @param 参数说明
   * @return 返回值
   * @exception 异常描述
   * @see 需要参见的其它内容
   * @since 从类的那一个版本，此方法被添加进来。（可选）
   */
  private void setCouldMaked(boolean newBoolean) {
    m_bCouldMaked = newBoolean;
  }

  /**
   * 作者：王印芬 功能：设置当前业务类型 参数：java.lang.String newCurBizeType 新的业务类型 返回：无 例外：无
   * 日期：(2002-3-13 11:39:21) 修改日期，修改人，修改原因，注释标志： 2002-10-29 wyf 暂时去掉业务类型对存货的限制
   */
  private void setCurBizeType(java.lang.String newCurBizeType) {
    m_strCurBizeType = newCurBizeType;
  }

  /**
   * 设置当前操作状态(如"增加","查询"等)
   * 
   * @param 参数说明
   * @return 返回值
   * @exception 异常描述
   * @see 需要参见的其它内容
   * @since 从类的那一个版本，此方法被添加进来
   * @param newCurOperState
   *          java.lang.String
   */
  private void setCurOperState(int newCurOperState) {
    m_nCurOperState = newCurOperState;
  }

  /**
   * 设置当前界面显示方式是卡片还是列表
   * 
   * @param 参数说明
   * @return 返回值
   * @exception 异常描述
   * @see 需要参见的其它内容
   * @since 从类的那一个版本，此方法被添加进来
   * @param newCurPaneModel
   *          int
   */
  private void setCurPanelMode(int newCurPaneModel) {
    m_nCurPanelMode = newCurPaneModel;
  }

  /**
   * 由卡片切换到列表界面
   * 
   * @param 参数说明
   * @return 返回值
   * @exception 异常描述
   * @see 需要参见的其它内容
   * @since 从类的那一个版本，此方法被添加进来。（可选）
   * @deprecated 该方法从类的那一个版本后，已经被其它方法替换。（可选）
   */
  private void setCurrMoneyDigitAndReCalToBill(String strCurr) {

    // 设置小数位
    if (strCurr == null || strCurr.length() < 1) {
      return;
    }

    // =================表体金额，税额，价税合计的精度
    // 业务精度
    int nDigit = POPubSetUI.getMoneyDigitByCurr_Busi(strCurr);
    // 财务精度
    int nDigit1 = POPubSetUI.getMoneyDigitByCurr_Finance(strCurr);
    
    int localDigit=getBillCardPanel().getNMoneyDecimal();
    int localFiDigit=getBillCardPanel().getNFiMoneyDecimal();

    getBillCardPanel().getBodyItem("noriginalcurmny").setDecimalDigits(nDigit);
    getBillCardPanel().getBodyItem("noriginaltaxmny").setDecimalDigits(nDigit);
    getBillCardPanel().getBodyItem("noriginalsummny").setDecimalDigits(nDigit);
    getBillCardPanel().getBodyItem("noriginalpaymentmny").setDecimalDigits(nDigit1);
    //by zhaoyha at 2009.11.28
    getBillCardPanel().getBodyItem("nmoney").setDecimalDigits(localDigit);
    getBillCardPanel().getBodyItem("ntaxmny").setDecimalDigits(localDigit);
    getBillCardPanel().getBodyItem("nsummny").setDecimalDigits(localDigit);
    getBillCardPanel().getBodyItem("npaymentmny").setDecimalDigits(localFiDigit);

    if (getOldCurrMoneyDigit() == nDigit) {
      // 如果当前币种与上一次币种的金额位数相同，则不用重新计算
      return;
    }
    else {
      setOldCurrMoneyDigit(nDigit);
    }

    // 重新计算
    if (getBillCardPanel().getBillModel().getRowCount() <= 0) {
      return;
    }

    UFDouble noriginaltaxmny = new UFDouble(0);
    UFDouble noriginalcurmny = new UFDouble(0);
    UFDouble noriginalsummny = new UFDouble(0);

    // V53增加尾差处理，发票参照订单时优先满足税额+金额+价税合计，其次满足单价*数量=金额
    for (int i = 0; i < getBillCardPanel().getBillModel().getRowCount(); i++) {
      noriginaltaxmny = getBillCardPanel().getBodyValueAt(i, "noriginaltaxmny") == null ? new UFDouble(0)
          : (UFDouble) getBillCardPanel().getBodyValueAt(i, "noriginaltaxmny");// 原币税额
      noriginalcurmny = getBillCardPanel().getBodyValueAt(i, "noriginalcurmny") == null ? new UFDouble(0)
          : (UFDouble) getBillCardPanel().getBodyValueAt(i, "noriginalcurmny");// 原币无税金额
      noriginalsummny = getBillCardPanel().getBodyValueAt(i, "noriginalsummny") == null ? new UFDouble(0)
          : (UFDouble) getBillCardPanel().getBodyValueAt(i, "noriginalsummny");// 原币价税合计
      if (noriginaltaxmny.add(noriginalcurmny).compareTo(noriginalsummny) != 0) {
        afterEditInvBillRelations(new BillEditEvent(getBillCardPanel().getBodyItem("ninvoicenum"), getBillCardPanel()
            .getBodyValueAt(i, "ninvoicenum"), "ninvoicenum", i, BillItem.BODY));
      }
    }

  }

  /**
   * 由卡片切换到列表界面
   * 
   * @param 参数说明
   * @return 返回值
   * @exception 异常描述
   * @see 需要参见的其它内容
   * @since 从类的那一个版本，此方法被添加进来。（可选）
   * @deprecated 该方法从类的那一个版本后，已经被其它方法替换。（可选）
   */
  private void setCurrMoneyDigitToBill(String strCurr) {

    if (strCurr == null || strCurr.length() < 1) {
      return;
    }

    // =================表体金额，税额，价税合计的精度
    // 业务精度
    int nDigit = POPubSetUI.getMoneyDigitByCurr_Busi(strCurr);
    // 财务精度
    int nDigit1 = POPubSetUI.getMoneyDigitByCurr_Finance(strCurr);

    getBillCardPanel().getBodyItem("noriginalcurmny").setDecimalDigits(nDigit);
    getBillCardPanel().getBodyItem("noriginaltaxmny").setDecimalDigits(nDigit);
    getBillCardPanel().getBodyItem("noriginalsummny").setDecimalDigits(nDigit);
    getBillCardPanel().getBodyItem("noriginalpaymentmny").setDecimalDigits(nDigit1);

    getBillCardPanel().getBodyItem("nmoney").setDecimalDigits(nDigit);
    getBillCardPanel().getBodyItem("ntaxmny").setDecimalDigits(nDigit);
    getBillCardPanel().getBodyItem("nsummny").setDecimalDigits(nDigit);
    getBillCardPanel().getBodyItem("npaymentmny").setDecimalDigits(nDigit1);

  }

  /**
   * 作者：王印芬 功能：设置表头折本及折辅汇率 参数：String strCurr 币种ID UFDouble dBRate 折本汇率
   * 如果传入空，则自动计算折本汇率，否则取该值 UFDouble dARate 折辅汇率 如果传入空，则自动计算折辅汇率，否则取该值 返回：无 例外：无
   * 日期：(2002-3-13 11:39:21) 修改日期，修改人，修改原因，注释标志： 2002-07-11 wyf 改为使用公共方法
   * 2002-07-17 wyf 修改折辅汇率写错KEY的问题
   */
  private void setCurrRateToBillHead(String sCurr, UFDouble dBRate) {

    String sRateDate = getBillCardPanel().getHeadItem("darrivedate").getValue();
    UFDate dOrderDate = null;
    if (sRateDate != null && !sRateDate.trim().equals("")) {
      dOrderDate = new UFDate(sRateDate);
    }

    // 位数
    int[] iaDigit = POPubSetUI.getBothExchRateDigit(getPk_corp(), sCurr);
    getBillCardPanel().getHeadItem("nexchangeotobrate").setDecimalDigits(iaDigit[0]);

    // 是否可编辑
    boolean[] baEditable = POPubSetUI.getBothExchRateEditable(getPk_corp(), sCurr);
    getBillCardPanel().getHeadItem("nexchangeotobrate").setEnabled(baEditable[0]);

    // 值
    UFDouble[] daValue = null;
    if (dBRate == null) {
      daValue = POPubSetUI.getBothExchRateValue(getPk_corp(), sCurr, dOrderDate);
      //add by QuSida (佛山骏杰)防止折本汇率为空
      UFDouble temp = daValue[0] == null?UFDouble.ONE_DBL: daValue[0];
      getBillCardPanel().setHeadItem("nexchangeotobrate", temp);
    }
    else {
      getBillCardPanel().setHeadItem("nexchangeotobrate", dBRate);
    }

  }

  /**
   * 设置当前显示的VO位置
   * 
   * @param 参数说明
   * @return 返回值
   * @exception 异常描述
   * @see 需要参见的其它内容
   * @since 从类的那一个版本，此方法被添加进来
   * @param newPreOperState
   *          java.lang.String
   */
  private void setCurVOPos(int pos) {
    m_nCurInvVOPos = pos;
  }

  /**
   * 提供公式,从该公式得到查询结果 参考BillModel.execloadFormula(int)写出
   * 
   * @param 参数说明
   * @return 返回值
   * @exception 异常描述
   * @see 需要参见的其它内容
   * @since 从类的那一个版本，此方法被添加进来
   * @return java.lang.String
   */
  private void setDefaultBankAccountForAVendor(String strVendorBase) {
    String pkcorp = getBillCardPanel().getCorp();
    String sWherePart=" bd_bankaccbas.pk_bankaccbas in ("+
    "select pk_accbank from bd_custbank where pk_cubasdoc='"+
    strVendorBase+"')";

//    String sWherePart = " and bd_bankaccbas.pk_bankaccbas in (" +
//    		"select  k.pk_accbank from bd_custbank k,bd_cumandoc m   where   m.pk_corp ='"
//      + pkcorp + "'  and k.pk_cubasdoc=m.pk_cubasdoc ";
//    if (nc.vo.scm.pu.PuPubVO.getString_TrimZeroLenAsNull(strVendorBase) != null) {
//      sWherePart += "and k.pk_cubasdoc='" + strVendorBase + "'";
//    }
//    sWherePart += ")";
    UIRefPane ref = (UIRefPane) getBillCardPanel().getHeadItem("caccountbankid").getComponent();

    ref.getRefModel().setWherePart(sWherePart);

    //ref.getRefModel().reloadData();

  }

  /**
   * 功能：增加表体行时，设置默认数据
   */
  private void setDefaultBody(int iCurRow) {

    // ID
    getBillCardPanel().setBodyValueAt(null, iCurRow, "cinvoice_bid");
    getBillCardPanel().setBodyValueAt(null, iCurRow, "cinvoiceid");
    // 公司编码
    getBillCardPanel().setBodyValueAt(getPk_corp(), iCurRow, "pk_corp");
    //
    updateUI();
  }

  /**
   * @param 参数说明
   * @return 返回值
   * @exception 异常描述
   * @see 需要参见的其它内容
   * @since 从类的那一个版本，此方法被添加进来。（可选）
   */
  private void setDefaultInfoForAFreeCust(String cfreeId) {

    if (cfreeId == null) {
      getBillCardPanel().setHeadItem("cvendorphone", null);
      getBillCardPanel().setHeadItem("caccountbankid", null);
      getBillCardPanel().setHeadItem("cvendoraccount", null);
      return;
    }

    Object[][] strInfo = null;
    try {
      strInfo = nc.ui.pu.pub.PubHelper.queryResultsFromAnyTable("so_freecust", new String[] {
          "vphone", "vaccname", "vaccount"
      }, "cfreecustid='" + cfreeId + "'");
    }
    catch (Exception e1) {
      SCMEnv.out(e1);
      return;
    }
    if (strInfo != null) {
      getBillCardPanel().setHeadItem("cvendorphone", strInfo[0][0]);
      ((UIRefPane) getBillCardPanel().getHeadItem("caccountbankid").getComponent()).getUITextField().setText(
          (String) strInfo[0][1]);
      getBillCardPanel().setHeadItem("cvendoraccount", strInfo[0][2]);
    }
  }

  /**
   * 提供公式,从该公式得到查询结果 参考BillModel.execloadFormula(int)写出
   * 
   * @param 参数说明
   * @return 返回值
   * @exception 异常描述
   * @see 需要参见的其它内容
   * @since 从类的那一个版本，此方法被添加进来
   * @return java.lang.String
   */
  private void setDefaultPhoneForAVendor(String strVendorBase) {

    if (strVendorBase == null || strVendorBase.trim().equals("")) {
      return;
    }

    BillItem item = getBillCardPanel().getHeadItem("cvendorphone");
    if (item == null || !item.isShow())
      return;

    Object[][] retOb = CacheTool.getMultiColValue("bd_cubasdoc", "pk_cubasdoc", new String[] {
        "phone1", "phone2", "phone3"
    }, new String[] {
      strVendorBase
    });

    if (retOb == null || retOb.length == 0) {
      getBillCardPanel().setHeadItem("cvendorphone", null);
    }
    else {
      String phone = null;
      for (int i = 0; i < retOb.length; i++) {
        if (retOb[i][0] != null && retOb[i][0].toString().trim().length() > 0) {
          phone = retOb[i][0].toString();
          break;
        }
      }
      getBillCardPanel().setHeadItem("cvendorphone", phone);
    }
  }

  /**
   * 作者：王印芬 功能：发票编辑时，设置项目的可编辑性 只用于编辑状态，且界面已有发票VO 参数：无 返回：无 例外：无 日期：(2001-10-13
   * 11:39:21) 修改日期，修改人，修改原因，注释标志： 2002-09-18 wyf 加入对批次号的处理
   */
  private void setEditableWhenEdit() {
    // 发票号
    //setEditableWhenEdit_Vinvoicecode();
    // 散户,开户银行
    setEditableWhenEdit_FreeCustAndBank();
    // 汇率是否可编辑
    setEditableWhenEdit_ExchRate();

    // wyf add 2002-07-18 begin
    // 如果来源单据为入库单，则存货不能修改
    setEditableWhenEdit_Inventory();
    // wyf add 2002-07-18 end

    // wyf add 2002-09-18 begin
    setEditableWhenEdit_VProduceNum();
    // wyf add 2002-09-18 end
    
    //符合业务类型要求的来源于订单的费用行数量字段不允许修改 For V56 by zhaoyha
    //setNumEditable();
  }

  /**
   * 作者：王印芬 功能：发票编辑时，设置汇率的可编辑性 只用于编辑状态，且界面已有发票VO 参数：无 返回：无 例外：无 日期：(2001-10-13
   * 11:39:21) 修改日期，修改人，修改原因，注释标志： 2002-09-18 wyf 加入对批次号的处理
   */
  private void setEditableWhenEdit_ExchRate() {
    // 汇率是否可编辑
    String sCurr = getBillCardPanel().getHeadItem("ccurrencytypeid").getValue();
//    if(!StringUtil.isEmptyWithTrim(sCurr)){
    boolean[] baEditable = POPubSetUI.getBothExchRateEditable(getPk_corp(), sCurr);
    getBillCardPanel().getHeadItem("nexchangeotobrate").setEnabled(baEditable[0]);
    
  }

  /**
   * 作者：王印芬 功能：发票编辑时，设置散户、开户银行的可编辑性 只用于编辑状态，且界面已有发票VO 参数：无 返回：无 例外：无
   * 日期：(2001-10-13 11:39:21) 修改日期，修改人，修改原因，注释标志：
   */
  private void setEditableWhenEdit_FreeCustAndBank() {
    // 散户,开户银行
    if (getBillCardPanel().getHeadItem("cfreecustid").getValue() == null) {
      // 判断供应商是否为散户，若为散户，则散户仍可用
      if (getBillCardPanel().getHeadItem("cvendorbaseid").getValue() == null
          || getBillCardPanel().getHeadItem("cvendorbaseid").getValue().trim().length() < 1) {
        getBillCardPanel().getHeadItem("cfreecustid").setEnabled(false);
        getBillCardPanel().getHeadItem("caccountbankid").setEnabled(false);
      }
      else {
        String sFreeFlag = "N";
        Object oTemp = getBillCardPanel().getHeadItem("cvendorbaseid").getValue();
        if (oTemp != null)
          oTemp = PiPqPublicUIClass.getAResultFromTable("bd_cubasdoc", "freecustflag", "pk_cubasdoc", oTemp.toString());
        if (oTemp != null)
          sFreeFlag = oTemp.toString();
        if (sFreeFlag != null && sFreeFlag.trim() != null && sFreeFlag.equals("Y")) {
          getBillCardPanel().getHeadItem("cfreecustid").setEnabled(true);
          getBillCardPanel().getHeadItem("caccountbankid").setEnabled(false);
        }
        else {
          getBillCardPanel().getHeadItem("cfreecustid").setEnabled(false);
          getBillCardPanel().getHeadItem("caccountbankid").setEnabled(true);
        }
      }
    }
    else {
      // 散户可用
      getBillCardPanel().getHeadItem("cfreecustid").setEnabled(true);
      getBillCardPanel().getHeadItem("caccountbankid").setEnabled(false);
    }
    // added by fangy 2002-10-29 begin
    // getInvBillPanel().getHeadItem("caccountbankid").setEdit(false);
    getBillCardPanel().getHeadItem("cvendorphone").getComponent().setEnabled(false);
    // added by fangy 2002-10-29 end

  }

  /**
   * 作者：王印芬 功能：发票编辑时，设置存货的可编辑性 只用于编辑状态，且界面已有发票VO 参数：无 返回：无 例外：无 日期：(2002-07-18
   * 11:39:21) 修改日期，修改人，修改原因，注释标志：
   */
  private void setEditableWhenEdit_Inventory() {

    // 如果来源单据为入库单，则存货不能修改
    int iCount = getBillCardPanel().getRowCount();
    for (int i = 0; i < iCount; i++) {
      String sUpSourceBillType = (String) getBillCardPanel().getBodyValueAt(i, "cupsourcebilltype");
      if (sUpSourceBillType != null
          && (sUpSourceBillType.trim().equals(nc.vo.scm.pu.BillTypeConst.STORE_PO)
              || sUpSourceBillType.trim().equals(nc.vo.scm.pu.BillTypeConst.STORE_SC)
              || sUpSourceBillType.trim().equals(nc.vo.scm.pu.BillTypeConst.STORE_ES) || sUpSourceBillType.trim()
              .equals("50"))) {
        getBillCardPanel().setCellEditable(i, "invcode", false);
      }
      else {
        getBillCardPanel().setCellEditable(i, "invcode", true);
      }
    }
  }

  /**
   * 作者：王印芬 功能：发票编辑时，设置发票号的可编辑性 只用于编辑状态，且界面已有发票VO 参数：无 返回：无 例外：无 日期：(2001-10-13
   * 11:39:21) 修改日期，修改人，修改原因，注释标志：
   */
  private void setEditableWhenEdit_Vinvoicecode() {
    // ////发票号
    if (getBillCardPanel().getHeadItem("vinvoicecode").getValue() == null
        || getBillCardPanel().getHeadItem("vinvoicecode").getValue().trim().equals("")) {
      getBillCardPanel().getHeadItem("vinvoicecode").setEnabled(true);
    }
    else {
      if (getBillCardPanel().getHeadItem("cinvoiceid").getValue() == null
          || getBillCardPanel().getHeadItem("cinvoiceid").getValue().trim().equals("")) {
        getBillCardPanel().getHeadItem("vinvoicecode").setEnabled(true);
      }
      else {
        getBillCardPanel().getHeadItem("vinvoicecode").setEnabled(true);
      }
    }
  }

  /**
   * 作者：王印芬 功能：设定批次号相关的可编辑性 参数：无 返回：无 例外：无 日期：(2002-9-18 11:39:21)
   * 修改日期，修改人，修改原因，注释标志：
   */
  private void setEditableWhenEdit_VProduceNum() {

    // =========表体 合同号及存货不可编辑
    int iRow = getBillCardPanel().getRowCount();
    if (iRow <= 0) {
      return;
    }
    for (int i = 0; i < iRow; i++) {
      String sMangId = (String) getBillCardPanel().getBodyValueAt(i, "cmangid");
      if (sMangId == null || sMangId.trim().length() < 1) {
        // =====不可编辑
        getBillCardPanel().setCellEditable(i, "vproducenum", false);
      }
      else {
        // =====是否可编辑
        getBillCardPanel().setCellEditable(i, "vproducenum", PuTool.isBatchManaged(sMangId));
      }
    }
  }

  /**
   * @param 参数说明
   * @return 返回值
   * @exception 异常描述
   * @see 需要参见的其它内容
   * @since 从类的那一个版本，此方法被添加进来。（可选）
   */
  private void setEverQueryed(boolean newQueryed) {

    m_bEverQueryed = newQueryed;
  }

  /**
   * 作者：方益 功能：更新表头的币种.(从表体获得) 参数： 返回： 例外： 日期：(2003-02-08 13:09:22)
   * 修改日期，修改人，修改原因，注释标志：
   * 
   * @param inv
   *          nc.vo.pi.InvoiceVO
   */
  public void setHeadCurrency(InvoiceVO inv) {

    InvoiceHeaderVO head = (InvoiceHeaderVO) inv.getHeadVO();
    InvoiceItemVO[] items = (InvoiceItemVO[]) inv.getChildrenVO();

    // 如果表体为空,则从后台读取.
    if (head != null && items != null && items.length > 0) {
      if (m_listPoPubSetUI2 == null) {
        m_listPoPubSetUI2 = new POPubSetUI2();
      }
      // 币种
      int[] iaExchRateDigit = null;
      for (int i = 0; i < items.length; i++) {
        // 供应商管理ID
        String sCvendormangid = head.getCvendormangid();
        String upSourceType = items[i].getCupsourcebilltype();
        String currencytypeid = items[i].getCcurrencytypeid();
        if (currencytypeid != null) {
          head.setCcurrencytypeid(items[i].getCcurrencytypeid());
        }
        else {
          // 如果有供应商，应带出默认币种
          if (upSourceType != null && upSourceType.trim().length() > 0
              && (upSourceType.equals("45") || upSourceType.equals("47") || upSourceType.equals("4T"))) {
            if (sCvendormangid != null && sCvendormangid.trim().length() > 0) {
              try {
                Object oTemp = CacheTool.getCellValue("bd_cumandoc", "pk_cumandoc", "pk_currtype1", sCvendormangid);
                if (oTemp != null) {
                  head.setCcurrencytypeid(oTemp.toString());
                  items[i].setCcurrencytypeid(oTemp.toString());
                }
              }
              catch (Exception e) {
                SCMEnv.out(e);
              }
            }
          }
        }
        // 取得币种
        String sCurrId = items[i].getCcurrencytypeid();
        // 得到折本、折辅汇率精度
        iaExchRateDigit = m_listPoPubSetUI2.getBothExchRateDigit(getPk_corp(), sCurrId);
        if (iaExchRateDigit == null || iaExchRateDigit.length == 0) {
          getBillCardPanel().getBillModel().getItemByKey("nexchangeotobrate").setDecimalDigits(5);
        }
        else {
          getBillCardPanel().getBillModel().getItemByKey("nexchangeotobrate").setDecimalDigits(iaExchRateDigit[0]);
        }
      }

      if (iaExchRateDigit == null || iaExchRateDigit.length == 0) {
        getBillCardPanel().getHeadItem("nexchangeotobrate").setDecimalDigits(5);
      }
      else {
        getBillCardPanel().getHeadItem("nexchangeotobrate").setDecimalDigits(iaExchRateDigit[0]);
      }
      // 设置折本及折辅汇率的值
      UFDouble[] d = POPubSetUI.getBothExchRateValue(getPk_corp(), head.getCcurrencytypeid(), head.getDarrivedate());
      head.setNexchangeotobrate(d[0]);
    }

  }

  /**
   * 子类实现该方法，返回业务界面的标题。
   * 
   * @version (00-6-6 13:33:25)
   * @return java.lang.String
   */
  private void setHeadHintText(String strHint) {

    m_strHeadHintText = strHint;
  }

  /**
   * 作者：汪维敏 功能：批次设置存货的管理相关信息 参数: BillCardPanel pnlBillCard 单据模板 UIRefPane
   * refpaneInv 存货参照 int iBeginRow 开始位置 int iEndRow 结束位置 返回：无 例外：无
   * 日期：(2004-02-11 13:45:10) 修改日期，修改人，修改原因，注释标志：
   */
  private void setInvEditFormulaInfo(BillCardPanel pnlBillCard, UIRefPane refpaneInv, int iBeginRow, int iEndRow) {
    if (pnlBillCard == null || refpaneInv == null) {
      SCMEnv.out("传入参数不正确！");
      return;
    }
    try {
      Object[] saMangIdRef = ((Object[]) refpaneInv.getRefValues("bd_invmandoc.pk_invmandoc"));
      Object[] saBaseIdRef = ((Object[]) refpaneInv.getRefValues("bd_invmandoc.pk_invbasdoc"));
      Object[] saMeasUnitRef = ((Object[]) refpaneInv.getRefValues("bd_invbasdoc.pk_measdoc"));
      Object[] saMangId = new Object[saMangIdRef.length];
      Object[] saBaseId = new Object[saBaseIdRef.length];
      Object[] saMeasUnit = new Object[saMeasUnitRef.length];
      if (saMangId == null || saBaseId == null || saMangId.length != saBaseId.length) {
        SCMEnv.out("数据错误：存货管理档案ID为空或存货档案ID为空或二者长度不等，直接返回");
        return;
      }
      for (int i = 0; i < saMangId.length; i++) {
        saMangId[i] = saMangIdRef[i];
        saBaseId[i] = saBaseIdRef[i];
        saMeasUnit[i] = saMeasUnitRef[i];
      }
      for (int i = 0; i < saMangId.length; i++) {
        saMangIdRef[i] = (String) saMangIdRef[i];
        saBaseIdRef[i] = (String) saBaseIdRef[i];
        saMeasUnitRef[i] = (String) saMeasUnitRef[i];
      }
      int iLen = saMangId.length;

      // ================解析基本档案：计量档案
      String[] saFormula = new String[] {
        "getColValue(bd_measdoc,measname,pk_measdoc,cmessureunit)"
      };
      PuTool.getFormulaParse().setExpressArray(saFormula);
      int iFormulaLen = saFormula.length;

      String[] sMeasUnits = new String[saMangId.length];
      String[] sMangIds = new String[saMangId.length];
      for (int i = 0; i < saMangId.length; i++) {
        sMeasUnits[i] = (String) sMeasUnits[i];
        sMangIds[i] = (String) saMangId[i];
      }
      for (int i = 0; i < iFormulaLen; i++) {
        PuTool.getFormulaParse().addVariable("cmessureunit", saMeasUnitRef);
      }

      String[][] saRet = PuTool.getFormulaParse().getValueSArray();
      String[] saUnitName = new String[iLen];
      if (saRet != null) {
        for (int i = 0; i < iLen; i++) {
          if (saRet[0] != null) {
            saUnitName[i] = saRet[0][i];
          }
        }
      }
      // ================解析管理档案:产地
      saFormula = new String[] {
        "getColValue(bd_invmandoc,prodarea,pk_invmandoc,cmangid)"
      };
      iFormulaLen = saFormula.length;
      PuTool.getFormulaParse().setExpressArray(saFormula);

      for (int i = 0; i < iFormulaLen; i++) {
        PuTool.getFormulaParse().addVariable("cmangid", saMangIdRef);
      }

      saRet = PuTool.getFormulaParse().getValueSArray();
      // 产地
      String[] saArea = new String[iLen];
      if (saRet != null) {
        for (int i = 0; i < iLen; i++) {
          if (saRet[0] != null) {
            saArea[i] = saRet[0][i];
          }
        }
      }
      // ================对表体各行设置值
      Object[] saCode = ((Object[]) refpaneInv.getRefValues("bd_invbasdoc.invcode"));
      Object[] saName = ((Object[]) refpaneInv.getRefValues("bd_invbasdoc.invname"));
      Object[] saSpec = ((Object[]) refpaneInv.getRefValues("bd_invbasdoc.invspec"));
      Object[] saType = ((Object[]) refpaneInv.getRefValues("bd_invbasdoc.invtype"));

      // 执行表体公式
      int iPkIndex = 0;
      for (int i = iBeginRow; i <= iEndRow; i++) {
        iPkIndex = i - iBeginRow;
        // 管理ID
        pnlBillCard.setBodyValueAt(saMangId[iPkIndex], i, "cmangid");
        // 基本ID
        pnlBillCard.setBodyValueAt(saBaseId[iPkIndex], i, "cbaseid");
        // 编码
        pnlBillCard.setBodyValueAt(saCode[iPkIndex], i, "invcode");
        // 名称
        pnlBillCard.setBodyValueAt(saName[iPkIndex], i, "invname");
        // 规格
        pnlBillCard.setBodyValueAt(saSpec[iPkIndex], i, "invspec");
        // 型号
        pnlBillCard.setBodyValueAt(saType[iPkIndex], i, "invtype");
        // 计量单位NAME
        pnlBillCard.setBodyValueAt(saUnitName[iPkIndex], i, "measname");
        // 产地
        pnlBillCard.setBodyValueAt(saArea[iPkIndex], i, "cproducearea");
      }
    }
    catch (Exception e) {
      SCMEnv.out("录入多存货时设置出错");
    }
  }

  /**
   * 作者：王印芬 功能：根据当前的操作状态决定发票类型下拉框是否有“虚拟”ITEM 参数：无 返回：无 例外：无 日期：(2002-9-27
   * 11:39:21) 修改日期，修改人，修改原因，注释标志：
   * @deprecated
   */
  private void setInvoiceTypeComItem() {
    
//    // 发票类型 默认为“专用”
//    // 国内专用、国内普通、国外专用、虚拟 、其它01234
//    UIComboBox comItem = (UIComboBox) getBillCardPanel().getHeadItem("iinvoicetype").getComponent();
//    if (getCurOperState() == STATE_EDIT) {
//      if (comItem.getItemCount() == 4) {
//        comItem.removeItem(nc.ui.ml.NCLangRes.getInstance().getStrByID(nc.vo.scm.pu.InvoiceType.m_sModuleNamePath,
//            nc.vo.scm.pu.InvoiceType.INVOICE_TYPE_VIRTUALID));
//      }
//    }
//    else {
//      if (comItem.getItemCount() == 3) {
//        comItem.addItem(nc.ui.ml.NCLangRes.getInstance().getStrByID(nc.vo.scm.pu.InvoiceType.m_sModuleNamePath,
//            nc.vo.scm.pu.InvoiceType.INVOICE_TYPE_VIRTUALID));
//      }
//    }
  }

  /**
   * 设置所有符合条件的VO
   * 
   * @param 参数说明
   * @return 返回值
   * @exception 异常描述
   * @see 需要参见的其它内容
   * @since 从类的那一个版本，此方法被添加进来
   * @param newAllQualifiedVOs
   *          nc.vo.pi.InvoiceVO[]
   */
  public void setInvVOs(nc.vo.pi.InvoiceVO[] newInvVOs) {
    m_InvVOs = newInvVOs;
  }

  /**
   * 此处插入方法说明。 创建日期：(2001-9-14 21:19:56)
   * 
   * @param newListOperState
   *          java.lang.String
   */
  private void setListOperState(int newListOperState) {
    m_nListOperState = newListOperState;
  }

  /**
   * 作者：王印芬 功能：增加一个发票行 该方法由onAppendLine()及onInsertLine()调用 参数：int iNewRow
   * 要加入的行号，从0开始 返回：无 例外：无 日期：(2002-9-26 11:39:21) 修改日期，修改人，修改原因，注释标志：
   */
  private void setNewLineDefaultValue(int iNewRow) {
    // 设置新增的表体行的扣税类别与表头相同
    getBillCardPanel().setBodyValueAt(
        ((UIComboBox) getBillCardPanel().getHeadItem("idiscounttaxtype").getComponent()).getSelectedItem(), iNewRow,
        "idiscounttaxtype");
    // 税率=表头税率
    if (getBillCardPanel().getHeadItem("ntaxrate").getValue() != null) {
      getBillCardPanel().setBodyValueAt(getBillCardPanel().getHeadItem("ntaxrate").getValue(), iNewRow, "ntaxrate");
    }
    // 折本汇率=表头折本汇率,折辅汇率=表辅折本汇率
    getBillCardPanel().setBodyValueAt(getBillCardPanel().getHeadItem("nexchangeotobrate").getValue(), iNewRow,
        "nexchangeotobrate");
    // 币种=表头币种 （1）为币种ID设值（2）显示币种名称
    String strCurrTypeId = ((UIRefPane) getBillCardPanel().getHeadItem("ccurrencytypeid").getComponent()).getRefPK();
    getBillCardPanel().setBodyValueAt(strCurrTypeId, iNewRow, "ccurrencytypeid");
    getBillCardPanel().setBodyValueAt(
        ((UIRefPane) getBillCardPanel().getHeadItem("ccurrencytypeid").getComponent()).getRefName(), iNewRow,
        "currname");
    // 设置汇率
    setExchangeRateBody(iNewRow, true, null);
    // 备注填空串，保证触发afterEdit
    getBillCardPanel().setBodyValueAt(null, iNewRow, "vmemo");
    // 请空存货编码cmangid
    getBillCardPanel().setBodyValueAt(null, iNewRow, "cmangid");
    getBillCardPanel().setBodyValueAt(null, iNewRow, "cbaseid");
    getBillCardPanel().setBodyValueAt(null, iNewRow, "invcode");
    getBillCardPanel().setBodyValueAt(null, iNewRow, "invname");
    getBillCardPanel().setBodyValueAt(null, iNewRow, "invspec");
    getBillCardPanel().setBodyValueAt(null, iNewRow, "invtype");
    getBillCardPanel().setBodyValueAt(null, iNewRow, "measname");
    getBillCardPanel().setBodyValueAt(null, iNewRow, "noriginalcurmny");
    getBillCardPanel().setBodyValueAt(null, iNewRow, "noriginaltaxmny");
    getBillCardPanel().setBodyValueAt(null, iNewRow, "noriginalsummny");
    getBillCardPanel().setBodyValueAt(null, iNewRow, "nmoney");
    getBillCardPanel().setBodyValueAt(null, iNewRow, "ntaxmny");
    getBillCardPanel().setBodyValueAt(null, iNewRow, "nsummny");
    // 默认把行id信息带来
    getBillCardPanel().setBodyValueAt(null, iNewRow, "cinvoiceid");
    getBillCardPanel().setBodyValueAt(null, iNewRow, "cinvoice_bid");

    // 清除来源单据信息
    getBillCardPanel().setBodyValueAt(null, iNewRow, "corderid");
    getBillCardPanel().setBodyValueAt(null, iNewRow, "corder_bid");
    getBillCardPanel().setBodyValueAt(null, iNewRow, "csourcebillid");
    getBillCardPanel().setBodyValueAt(null, iNewRow, "csourcebillrowid");
    getBillCardPanel().setBodyValueAt(null, iNewRow, "csourcebilltype");
    getBillCardPanel().setBodyValueAt(null, iNewRow, "cupsourcebillid");
    getBillCardPanel().setBodyValueAt(null, iNewRow, "cupsourcebillrowid");
    getBillCardPanel().setBodyValueAt(null, iNewRow, "cupsourcebilltype");

  }

  /**
   * 作者：王印芬 功能：增加一个发票行 该方法由onAppendLine()及onInsertLine()调用 参数：int iNewRow
   * 要加入的行号，从0开始 返回：无 例外：无 日期：(2002-9-26 11:39:21) 修改日期，修改人，修改原因，注释标志：
   */
  private void setNewLineDefaultValueForAddLine(int iNewRow) {
    // 设置新增的表体行的扣税类别与表头相同
    getBillCardPanel().setBodyValueAt(
        ((UIComboBox) getBillCardPanel().getHeadItem("idiscounttaxtype").getComponent()).getSelectedItem(), iNewRow,
        "idiscounttaxtype");
    // 税率=表头税率
    if (getBillCardPanel().getHeadItem("ntaxrate").getValue() != null) {
      getBillCardPanel().setBodyValueAt(getBillCardPanel().getHeadItem("ntaxrate").getValue(), iNewRow, "ntaxrate");
    }
    // 折本汇率=表头折本汇率,折辅汇率=表辅折本汇率
    getBillCardPanel().setBodyValueAt(getBillCardPanel().getHeadItem("nexchangeotobrate").getValue(), iNewRow,
        "nexchangeotobrate");
    // 币种=表头币种 （1）为币种ID设值（2）显示币种名称
    String strCurrTypeId = ((UIRefPane) getBillCardPanel().getHeadItem("ccurrencytypeid").getComponent()).getRefPK();
    getBillCardPanel().setBodyValueAt(strCurrTypeId, iNewRow, "ccurrencytypeid");
    getBillCardPanel().setBodyValueAt(
        ((UIRefPane) getBillCardPanel().getHeadItem("ccurrencytypeid").getComponent()).getRefName(), iNewRow,
        "currname");
    // 设置汇率
    setExchangeRateBody(iNewRow, true, null);
    // 备注填空串，保证触发afterEdit
    getBillCardPanel().setBodyValueAt(null, iNewRow, "vmemo");
    // 请空存货编码cmangid
    getBillCardPanel().setBodyValueAt(null, iNewRow, "cmangid");
    getBillCardPanel().setBodyValueAt(null, iNewRow, "cbaseid");
    getBillCardPanel().setBodyValueAt(null, iNewRow, "invcode");
    getBillCardPanel().setBodyValueAt(null, iNewRow, "invname");
    getBillCardPanel().setBodyValueAt(null, iNewRow, "invspec");
    getBillCardPanel().setBodyValueAt(null, iNewRow, "invtype");
    getBillCardPanel().setBodyValueAt(null, iNewRow, "measname");
    getBillCardPanel().setBodyValueAt(null, iNewRow, "noriginalcurmny");
    getBillCardPanel().setBodyValueAt(null, iNewRow, "noriginaltaxmny");
    getBillCardPanel().setBodyValueAt(null, iNewRow, "noriginalsummny");
    getBillCardPanel().setBodyValueAt(null, iNewRow, "nmoney");
    getBillCardPanel().setBodyValueAt(null, iNewRow, "ntaxmny");
    getBillCardPanel().setBodyValueAt(null, iNewRow, "nsummny");
    // 默认把行id信息带来
    getBillCardPanel().setBodyValueAt(null, iNewRow, "cinvoiceid");
    getBillCardPanel().setBodyValueAt(null, iNewRow, "cinvoice_bid");

    // 清除来源单据信息
    getBillCardPanel().setBodyValueAt(null, iNewRow, "corderid");
    getBillCardPanel().setBodyValueAt(null, iNewRow, "corder_bid");
    getBillCardPanel().setBodyValueAt(null, iNewRow, "csourcebillid");
    getBillCardPanel().setBodyValueAt(null, iNewRow, "csourcebillrowid");
    getBillCardPanel().setBodyValueAt(null, iNewRow, "csourcebilltype");
    getBillCardPanel().setBodyValueAt(null, iNewRow, "cupsourcebillid");
    getBillCardPanel().setBodyValueAt(null, iNewRow, "cupsourcebillrowid");
    getBillCardPanel().setBodyValueAt(null, iNewRow, "cupsourcebilltype");
  }

  /**
   * 项目阶段参照
   */
  private void setOldCurrMoneyDigit(int newValue) {

    m_oldCurrMoneyDigit = newValue;
  }

  /**
   * 作者：汪维敏 功能：存货修改后相应的税率变化 该函数由afterEdit存货、订单日期、币种改变后调用。 参数： int iBeginRow
   * 需计算税率相关信息的表体开始行 int iEndRow 需计算税率相关信息的表体结束行 返回：无 例外：无 日期：(2003-11-3
   * 11:39:21) 修改日期，修改人，修改原因，注释标志：
   */
  public void setRelated_Taxrate(int iBeginRow, int iEndRow) {

    HashMap mapId = new HashMap();
    Vector vecRow = new Vector();
    for (int i = iBeginRow; i <= iEndRow; i++) {
      String sBaseId = PuPubVO.getString_TrimZeroLenAsNull((String) getBillCardPanel().getBodyValueAt(i, "cbaseid"));
      if (sBaseId != null) {
        mapId.put(sBaseId, "");
        vecRow.add(new Integer(i));
      }
    }
    int iSize = vecRow.size();
    if (iSize == 0) {
      return;
    }
    // 批量加载税率
    PuTool.loadBatchTaxrate((String[]) mapId.keySet().toArray(new String[iSize]), getCorpId());

    // 重置
    int iRow = 0;
    for (int i = 0; i < iSize; i++) {
      iRow = ((Integer) vecRow.get(i)).intValue();

      String sBaseId = (String) getBillCardPanel().getBodyValueAt(iRow, "cbaseid");

      UFDouble dCurTaxRate = PuTool.getInvTaxRate(sBaseId);
      if (dCurTaxRate != null) {
        getBillCardPanel().setBodyValueAt(dCurTaxRate, iRow, "ntaxrate");
        // 税率改变，计算（数量，单价，含税单价，金额，税率，税额，价税合计,扣率，扣率单价）之间的关系
        BillEditEvent tempE = new BillEditEvent(getBillCardPanel().getBodyItem("ntaxrate"), dCurTaxRate, "ntaxrate",
            iRow);
        // 触发数值重新计算事件
        afterEditInvBillRelations(tempE);
      }
    }

  }

  /**
   * 此处插入方法说明。 创建日期：(2001-9-14 12:03:06)
   * 
   * @param newSelectedRows
   *          int[]
   */
  private void setSelectedRowCount(int newSelectedRow) {
    m_nSelectedRowCount = newSelectedRow;
  }

  /**
   * 此处插入方法说明。 创建日期：(2003-10-29 12:11:52)
   */
  private boolean setSendAuditBtnState() {
    boolean b = false;
    if (getInvVOs() == null || getInvVOs().length <= 0) {
      m_btnSendAudit.setEnabled(false);
      return b;
    }

    InvoiceVO curVO = null;
    if (getCurVOPos() > -1) {
      curVO = getInvVOs()[getCurVOPos()];
    }
    // ClientLink cl = new
    // ClientLink(nc.ui.pub.ClientEnvironment.getInstance());//环境信息
    if (curVO == null)
      return b;
    if (curVO.getHeadVO().getCoperator() == null) {
      curVO.getHeadVO().setCoperator(nc.ui.pub.ClientEnvironment.getInstance().getUser().getPrimaryKey());
    }
    if (curVO.getHeadVO().getIbillstatus().intValue() == 4 && getCurOperState() == STATE_EDIT) {
      // 审批未通过,修改时,应设置为自由态
      curVO.getHeadVO().setIbillstatus(new Integer(0));
      hBillStatusBeforeEdit.put(curVO.getHeadVO().getCinvoiceid(), new Integer(4));
    }
    //效率优化 by zhaoyha at 2009.8
    UFBoolean needSend=(UFBoolean) curVO.getHeadVO().getAttributeValue(ConstantVO.EXT_ATTR_ISNEEDSENDAUDIT);
    if(null!=needSend)
      b=needSend.booleanValue();
    // 送审
    else 
      b = PuTool.isNeedSendToAudit(nc.vo.scm.pu.BillTypeConst.PO_INVOICE, curVO);
    // if (b){
    // m_btnSendAudit.setEnabled(true);
    // m_btnInvBillAudit.setEnabled(false);
    // }
    // else{
    // m_btnSendAudit.setEnabled(false);
    // if(m_btnInvBillConversion.isEnabled()) {
    // m_btnInvBillAudit.setEnabled(false);
    // // }else {
    // // m_btnInvBillAudit.setEnabled(true);
    // }
    // }
    return b;
  }

  /**
   * 作者：王印芬 功能：显示当前VO的表体到列表界面 参数：无 返回：无 例外：无 日期：(2002-4-22 11:39:21)
   * 修改日期，修改人，修改原因，注释标志：
   */

  private void setVoBodyToListPanle(int nCurIndex) {

    // nCurIndex不满足条件 清空界面的表体
    if (nCurIndex < 0 || nCurIndex >= getInvVOs().length) {
      getBillListPanel().setBodyValueVO(null);

    }
    else {

      // 设置表体金额等精度
      // PiPqPublicUIClass.setCurrMoneyDigitToListBody(getInvListPanel(),
      // getInvVOs()[nCurIndex].getHeadVO().getCcurrencytypeid());
      // 设置自由项
      // PiPqPublicUIClass.setVfree0ForAInvoice(getInvVOs()[nCurIndex]);
      InvoiceItemVO bodyVO[] = getInvVOs()[nCurIndex].getBodyVO();
      Vector vTemp = new Vector();
      for (int i = 0; i < bodyVO.length; i++) {
        if (bodyVO[i].getVfree1() != null || bodyVO[i].getVfree2() != null || bodyVO[i].getVfree3() != null
            || bodyVO[i].getVfree4() != null || bodyVO[i].getVfree5() != null)
          vTemp.addElement(bodyVO[i]);
      }
      if (vTemp.size() > 0) {
        InvoiceItemVO tempbodyVO[] = new InvoiceItemVO[vTemp.size()];
        vTemp.copyInto(tempbodyVO);
        new nc.ui.scm.pub.FreeVOParse().setFreeVO(tempbodyVO, "vfree0", "vfree", null, "cmangid", false);
      }
      // 设置金额等位数为最大的位数
      setMaxMnyDigitList(iMaxMnyDigit);
      // 设置VO
      getBillListPanel().setBodyValueVO(getInvVOs()[nCurIndex].getBodyVO());
      // 表体币种精度
      try {
        resetBodyValueRelated_Curr(getPk_corp(), getInvVOs()[nCurIndex].getHeadVO().getCcurrencytypeid(),
            getBillListPanel().getBodyBillModel(), new BusinessCurrencyRateUtil(getPk_corp()), getBillListPanel()
                .getBodyBillModel().getRowCount(), m_listPoPubSetUI2);
      }
      catch (Exception e) {
        SCMEnv.out(e);
      }

      // int length = getInvVOs()[nCurIndex].getBodyVO().length;
      // String ccur = null;
      // for(int i = 0 ; i < length ; i ++){
      // ccur = getInvVOs()[nCurIndex].getBodyVO()[i].getCcurrencytypeid();
      // //设置表体金额等精度
      // PiPqPublicUIClass.setCurrMoneyDigitToListBody(getInvListPanel(), ccur);
      // //表体币种精度
      // resetBodyValueRelated_Curr(getPk_corp(),ccur,getInvListPanel().getBodyBillModel(),new
      // BcurrArith(getPk_corp()),getInvListPanel().getBodyBillModel().getRowCount(),m_listPoPubSetUI2);
      // }
      // 执行公式
      getBillListPanel().getBodyBillModel().execLoadFormula();

      // 加载来源信息
      // PuTool.loadSourceBillData(getInvListPanel(),
      // nc.vo.pu.pub.BillTypeConst.PO_INVOICE);
      // 加载源头信息
      // PuTool.loadAncestorBillData(getInvListPanel(),
      // nc.vo.pu.pub.BillTypeConst.PO_INVOICE);
      if(isNeedLoadSrcInfo(getBillListPanel().getBodyBillModel()))
        PuTool.loadSourceInfoAll(getBillListPanel(), BillTypeConst.PO_INVOICE);
      getBillListPanel().getBodyTable().clearSelection();
    }
  }

  /**
   * 作者：王印芬 功能：对列表模板设置一个已知的VO(转单专用) 参数：无 返回：无 例外：无 日期：(2001-7-01 11:39:21)
   * 修改日期，修改人，修改原因，注释标志： 2002-09-18 wyf 加入对来源单据类型、来源单据号的处理 修改列表体的显示为调用公共方法
   */
  private void setSpiltVOsToListPanel() {
    cunpos = 0;
    getBillListPanel().getHeadBillModel().clearBodyData();
    getBillListPanel().getBodyBillModel().clearBodyData();
    // V5 Del : setImageType(this.IMAGE_NULL);

    if (getInvVOs() == null)
      return;
    // showHintMessage(getHeadHintText() + CommonConstant.SPACE_MARK +
    // nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401","UPP40040401-000066")/*@res
    // "正在计算公式"*/ + CommonConstant.SPACE_MARK + CommonConstant.BEGIN_MARK +
    // "......." + CommonConstant.END_MARK);

    if (getCurVOPos() == -1)
      setCurVOPos(0);
    int pos = 0;
    int j = 0;
    int count = 0;
    Vector contains = new Vector();
    Vector poss = new Vector();
    // 设置VO到界面,不用普通的setBodyVOs()方法
    for (int i = 0; i < getInvHVOs().length; i++) {
      if (getInvVOs()[i].getHeadVO().getCinvoiceid() == null) {

        // 加行
        getBillListPanel().getHeadBillModel().addLine();
        // 设置折本及折辅汇率
        if (getInvHVOs()[i].getNexchangeotobrate() == null) {

          UFDouble[] d = POPubSetUI.getBothExchRateValue(getPk_corp(), getInvHVOs()[i].getCcurrencytypeid(),
              getInvHVOs()[i].getDarrivedate());

          getInvHVOs()[i].setNexchangeotobrate(d[0]);
        }
        // 设置折本及折辅汇率的精度
        PiPqPublicUIClass.setCurrRateDigitToListHead(getBillListPanel(), getInvHVOs()[i].getCcurrencytypeid());
        // 设置VO
        if (j == 0 && count == 0) {
          getBillListPanel().getHeadBillModel().setBodyRowVO(getInvHVOs()[i], 0);
          pos = i;
        }
        else {
          getBillListPanel().getHeadBillModel().setBodyRowVO(getInvHVOs()[i], j);
        }
        // pos =1;
        j++;
        count++;
        contains.add(getInvHVOs()[i]);
        poss.add(String.valueOf(i));
      }
    }
    VOsPos = null;
    if (count > 0) {
      VOsPos = new String[poss.size()];
      poss.copyInto(VOsPos);
    }
    // setCurVOPos(0);
    // 执行列表头公式
    getBillListPanel().getHeadBillModel().execLoadFormula();
    if (count > 0) {
      InvoiceHeaderVO[] invoiceHeaderVOs = new InvoiceHeaderVO[contains.size()];
      contains.copyInto(invoiceHeaderVOs);
      InvoiceHeaderVO invoiceHeaderVO = new InvoiceHeaderVO();
      for (int i = 0; i < count; i++) {
        // 0普通 1期初 2系统
        // 期初标志
        invoiceHeaderVO = invoiceHeaderVOs[i];
        if (invoiceHeaderVO.getCinvoiceid() == null) {
          if (invoiceHeaderVO.getFinitflag() == null || invoiceHeaderVO.getFinitflag().intValue() == 0) // 普通
            getBillListPanel().getHeadBillModel().setValueAt(new Boolean(false), i, "finitflag");
          else if (invoiceHeaderVO.getFinitflag().intValue() == 1)
            getBillListPanel().getHeadBillModel().setValueAt(new Boolean(true), i, "finitflag");

          // 是否审批的标志
          if (invoiceHeaderVO.getIbillstatus() != null && invoiceHeaderVO.getIbillstatus().intValue() == 3)
            getBillListPanel().getHeadBillModel().setValueAt(new Boolean(true), i, "isaudited");
          else
            getBillListPanel().getHeadBillModel().setValueAt(new Boolean(false), i, "isaudited");

        }

      }
    }

    // wyf 2002-09-18 modify begin
    // setVoBodyToListPanle(getCurVOPos()) ;
    // wyf 2002-09-18 modify begin
    // 当前张打标记并定位
    // if(count==1){
    getBillListPanel().getHeadTable().setRowSelectionInterval(0, 0);
    // }
    // else{
    // getInvListPanel().getHeadTable().setRowSelectionInterval(pos, pos);
    // }
    dispPlanPrice(false);
    setVoBodyToListPanle(pos);
    cunpos = pos;
    currentPos = pos;
    splitFlag = true;
  }

  /**
   * 作者：王印芬 功能：对列表模板设置一个已知的VO 参数：无 返回：无 例外：无 日期：(2001-7-01 11:39:21)
   * 修改日期，修改人，修改原因，注释标志： 2002-09-18 wyf 加入对来源单据类型、来源单据号的处理 修改列表体的显示为调用公共方法
   */
  private void setVOsToListPanel() {

    getBillListPanel().getHeadBillModel().clearBodyData();
    getBillListPanel().getBodyBillModel().clearBodyData();
    // V5 Del : setImageType(this.IMAGE_NULL);

    if (getInvVOs() == null)
      return;

    if (getCurVOPos() == -1)
      setCurVOPos(0);
    // 设置VO到界面,不用普通的setBodyVOs()方法
    for (int i = 0; i < getInvHVOs().length; i++) {
      // 加行
      getBillListPanel().getHeadBillModel().addLine();
      getBillListPanel().getHeadBillModel().setBodyRowVO(getInvHVOs()[i], i);

    }

    // 执行列表头公式
    getBillListPanel().getHeadBillModel().execLoadFormula();

    for (int i = 0; i < getInvVOs().length; i++) {
      // 0普通 1期初 2系统
      // 期初标志
      if (getInvVOs()[i].getHeadVO().getFinitflag() == null
          || getInvVOs()[i].getHeadVO().getFinitflag().intValue() == 0) // 普通
        getBillListPanel().getHeadBillModel().setValueAt(new Boolean(false), i, "finitflag");
      else if (getInvVOs()[i].getHeadVO().getFinitflag().intValue() == 1)
        getBillListPanel().getHeadBillModel().setValueAt(new Boolean(true), i, "finitflag");

      // 是否审批的标志
      if (getInvVOs()[i].getHeadVO().getIbillstatus() != null
          && getInvVOs()[i].getHeadVO().getIbillstatus().intValue() == 3)
        getBillListPanel().getHeadBillModel().setValueAt(new Boolean(true), i, "isaudited");
      else
        getBillListPanel().getHeadBillModel().setValueAt(new Boolean(false), i, "isaudited");

    }
    getBillListPanel().getHeadTable().setRowSelectionInterval(getCurVOPos(), getCurVOPos());

    dispPlanPrice(false);
  }

  /**
   * 作者：王印芬 功能：专门为审批流审批人点出的单据界面设计 与setVOToBillPanel的唯一区别在于//V5 Del :
   * setImageType()没有 因在审批流中该函数报错 不要随意修改及删除该函数，否则可能引起审批流错误 参数：无 返回：无 例外：无
   * 日期：(2002-7-01 11:39:21) 修改日期，修改人，修改原因，注释标志： 2002-09-19 wyf 加入来源单据信息
   * 2008-10-23 wyf 业务员参照修改为树表状
   */
  private void setVOToAuditedBillPanel() {

    // showHintMessage(getHeadHintText() + CommonConstant.SPACE_MARK +
    // nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401","UPP40040401-000066")/*@res
    // "正在计算公式"*/ + CommonConstant.SPACE_MARK + CommonConstant.BEGIN_MARK +
    // "......." + CommonConstant.END_MARK);
    getBillCardPanel().addNew();
    InvoiceVO curVO = getInvVOs()[getCurVOPos()];
    InvoiceHeaderVO head = getInvVOs()[getCurVOPos()].getHeadVO();

    // 表头币种与表体币种保持一致
    if (head.getCcurrencytypeid() == null || head.getCcurrencytypeid().trim().length() < 1) {
      // 查出的发票
      if (curVO.getBodyVO() != null) {
        head.setCcurrencytypeid(curVO.getBodyVO()[0].getCcurrencytypeid());
        head.setNexchangeotobrate(curVO.getBodyVO()[0].getNexchangeotobrate());
        // head.setNexchangeotoarate(curVO.getBodyVO()[0].getNexchangeotoarate());
      }
    }
    // 计算自由项
    // PiPqPublicUIClass.setVfree0ForAInvoice(curVO);
    InvoiceItemVO bodyVO[] = curVO.getBodyVO();
    Vector vTemp = new Vector();
    for (int i = 0; i < bodyVO.length; i++) {
      if (bodyVO[i].getVfree1() != null || bodyVO[i].getVfree2() != null || bodyVO[i].getVfree3() != null
          || bodyVO[i].getVfree4() != null || bodyVO[i].getVfree5() != null)
        vTemp.addElement(bodyVO[i]);
//    //设置这本换算率精度
//      setExchangeRateBody(i, false, curVO.getBodyVO()[i]);
    
    }
    if (vTemp.size() > 0) {
      InvoiceItemVO tempbodyVO[] = new InvoiceItemVO[vTemp.size()];
      vTemp.copyInto(tempbodyVO);
      new nc.ui.scm.pub.FreeVOParse().setFreeVO(tempbodyVO, "vfree0", "vfree", null, "cmangid", false);
    }

    // 得到供应商基本ID
    String strVendorBase = head.getCvendorbaseid();
    if (head.getCvendormangid() != null && (strVendorBase == null || strVendorBase.trim().equals(""))) {
      // strVendorBase =
      // getResultFromFormula("getColValue(bd_cumandoc,pk_cubasdoc,pk_cumandoc,cvendormangid)","cvendormangid",head.getCvendormangid())
      // ;
      strVendorBase = (String) PiPqPublicUIClass.getAResultFromTable("bd_cumandoc", "pk_cubasdoc", "pk_cumandoc", head
          .getCvendormangid());
      head.setCvendorbaseid(strVendorBase);
    }

    // =============设置VO前设置小数位
    // 金额的小数位等
    setCurrMoneyDigitToBill(head.getCcurrencytypeid());
    // 设置VO
    getBillCardPanel().setBillValueVO(curVO);
    // 汇率
    setCurrRateToBillHead(head.getCcurrencytypeid(), head.getNexchangeotobrate());

    // /////========电话,开户银行,帐号,取供应商还是散户
    if (curVO.getHeadVO().getCfreecustid() == null) {
      // 散户置灰,清空相应信息
      getBillCardPanel().getHeadItem("cfreecustid").setValue(null);
      ((UIRefPane) getBillCardPanel().getHeadItem("caccountbankid").getComponent()).setButtonVisible(true);

      // 供应商电话
      setDefaultPhoneForAVendor(strVendorBase);
      // 开户银行:
      //UIRefPane pane1 = (UIRefPane) getBillCardPanel().getHeadItem("caccountbankid").getComponent();
      //((AccountsForVendorRefModel) pane1.getRef().getRefModel()).setCvendorbaseid(strVendorBase);
      if (head.getCaccountbankid() != null) {
        //pane1.setPK(head.getCaccountbankid());
        String strAccount = null;
        try {
          Object[][] retOb = nc.ui.pu.pub.PubHelper.queryResultsFromAnyTable("bd_bankaccbas", new String[] {
            "account"
          }, "pk_bankaccbas='" + head.getCaccountbankid() + "'");
          strAccount = (String) retOb[0][0];
        }
        catch (Exception e) {
          SCMEnv.out(e);
          SCMEnv.out("取供应商默认银行时出现异常!");
        }
        // 帐号
        getBillCardPanel().setHeadItem("cvendoraccount", strAccount);
      }
      else {
        if (getCurOperState() == STATE_EDIT) {
          setDefaultBankAccountForAVendor(strVendorBase);
        }
      }
      // 帐号
      if (head.getCaccountbankid() != null)
        getBillCardPanel().setHeadItem("cvendoraccount",
            PiPqPublicUIClass.getAResultFromTable("bd_bankaccbas", "account", "pk_bankaccbas", head.getCaccountbankid()));
      }
    else {
      // 开户银行不可编辑,按钮不可见
      ((UIRefPane) getBillCardPanel().getHeadItem("caccountbankid").getComponent()).setButtonVisible(false);
      // 影响的信息
      setDefaultInfoForAFreeCust(curVO.getHeadVO().getCfreecustid());
    }

    // 设置业务员
    UIRefPane pane2 = (UIRefPane) getBillCardPanel().getHeadItem("cemployeeid").getComponent();
    // wyf 2002-10-23 modify begin
    // ((EmployeesForDeptRefModel)pane2.getRef().getRefModel()).setCdeptid(head.getCdeptid())
    // ;
    // String pk_corp =
    // nc.ui.pub.ClientEnvironment.getInstance().getCorporation().getPrimaryKey();
    // String addWherePart = " and (bd_psndoc.pk_deptdoc in (select
    // bd_deptdoc.pk_deptdoc from bd_deptdoc where (bd_deptdoc.deptattr = '2' or
    // bd_deptdoc.deptattr = '4') and bd_deptdoc.pk_corp ='"+pk_corp+"'))";
    // pane2.setRefModel(new
    // EmployeesForDeptRefModel(head.getCdeptid(),addWherePart));
    String pk_corp = head.getPk_purcorp();
    String s = " bd_psndoc.indocflag='Y' and (bd_psndoc.pk_deptdoc in (select bd_deptdoc.pk_deptdoc from bd_deptdoc where (bd_deptdoc.deptattr = '2' or bd_deptdoc.deptattr = '4') and bd_deptdoc.pk_corp ='"
        + pk_corp + "')) ";
    // pane2.setRefModel(new
    // EmployeesForDeptRefModel(head.getCdeptid(),addWherePart));
    pane2.getRefModel().setWherePart(s);
    // wyf 2002-10-23 modify end
    if (head.getCemployeeid() != null) {
      pane2.setPK(head.getCemployeeid());
    }
    // 期初标志
    UICheckBox initCheck = (UICheckBox) getBillCardPanel().getHeadItem("finitflag").getComponent();
    if (head.getFinitflag() == null || head.getFinitflag().intValue() == 0) {
      initCheck.setSelected(false);
    }
    else {
      initCheck.setSelected(true);
    }
    // 扣税类别
    int selectedIndex = ((UIComboBox) getBillCardPanel().getHeadItem("idiscounttaxtype").getComponent())
        .getSelectedIndex();
    ((UIComboBox) getBillCardPanel().getHeadItem("idiscounttaxtype").getComponent()).setSelectedIndex(selectedIndex);
    // 备注
    if(getBillCardPanel().getHeadItem("vmemo").getComponent() instanceof UIRefPane){
      ((UIRefPane) getBillCardPanel().getHeadItem("vmemo").getComponent()).setText(head.getVmemo());
    }

    // 执行表体公式
    if (curVO.getBodyVO() != null) {
      getBillCardPanel().getBillModel().execLoadFormula();
    }
 // 取表头币种
    String sCurrId = getBillCardPanel().getHeadItem("ccurrencytypeid").getValue();
    String sBodyCurrId = null;
    // 备注填空串，保证触发afterEdit
    if (curVO.getBodyVO() != null) {
      for (int i = 0; i < curVO.getBodyVO().length; i++) {
        if (curVO.getBodyVO()[i].getVmemo() == null) {
          getBillCardPanel().setBodyValueAt("", i, "vmemo");
        }
        // 新增表体行币种默认为表头币种
        sBodyCurrId = (String) getBillCardPanel().getBillModel().getValueAt(i, "ccurrencytypeid");
        if (sBodyCurrId == null || sBodyCurrId.equals("")) {
          getBillCardPanel().getBillModel().setValueAt(sCurrId, i, "ccurrencytypeid");
          // 获取币种名称
          // 驱动表体相关币种的改动
          afterEditWhenBodyCurrency(new BillEditEvent(getBillCardPanel().getBodyItem("ccurrencytypeid").getComponent(),
              sCurrId, "ccurrencytypeid", i));
          // 设置修改标志
          getBillCardPanel().getBillModel().setRowState(i, BillModel.MODIFICATION);
          getBillCardPanel().getBillModel().getValueAt(i, "ccurrencytypeid");
          setExchangeRateBody(i, true, null);
        }
        setExchangeRateBody(i, false, curVO.getBodyVO()[i]);
        // 设置原\本\辅精度
        // setRowDigits_Mny(getPk_corp(),
        // i,getInvBillPanel().getBillModel(),m_cardPoPubSetUI2);
        // 原币金额类(原币金额--noriginalcurmny;原币税额--noriginaltaxmny;原币价税合计--noriginalsummny)
        getBillCardPanel()
        .setBodyValueAt(getBillCardPanel().getBodyValueAt(i, "noriginalcurmny"), i, "noriginalcurmny");
        getBillCardPanel()
        .setBodyValueAt(getBillCardPanel().getBodyValueAt(i, "noriginaltaxmny"), i, "noriginaltaxmny");
        getBillCardPanel()
        .setBodyValueAt(getBillCardPanel().getBodyValueAt(i, "noriginalsummny"), i, "noriginalsummny");
        getBillCardPanel().setBodyValueAt(getBillCardPanel().getBodyValueAt(i, "noriginalpaymentmny"), i,
        "noriginalpaymentmny");
        
        getBillCardPanel().setBodyValueAt(getBillCardPanel().getBodyValueAt(i, "nmoney"), i, "nmoney");
        getBillCardPanel().setBodyValueAt(getBillCardPanel().getBodyValueAt(i, "ntaxmny"), i, "ntaxmny");
        getBillCardPanel().setBodyValueAt(getBillCardPanel().getBodyValueAt(i, "nsummny"), i, "nsummny");
        getBillCardPanel().setBodyValueAt(getBillCardPanel().getBodyValueAt(i, "npaymentmny"), i, "npaymentmny");
      }
    }
    // wyf 2002-09-18 add begin
    // 加载来源信息
    // PuTool.loadSourceBillData(getInvBillPanel(),
    // nc.vo.pu.pub.BillTypeConst.PO_INVOICE);
    // wyf 2002-09-18 add end
    // 加载源头信息
    // PuTool.loadAncestorBillData(getInvBillPanel(),
    // nc.vo.pu.pub.BillTypeConst.PO_INVOICE);
    if(isNeedLoadSrcInfo(getBillCardPanel().getBillModel()))
      PuTool.loadSourceInfoAll(getBillCardPanel(), BillTypeConst.PO_INVOICE);
    // 填充表头公式数据，避免不可参照出的数据
    execHeadTailFormula(curVO);

    // 主要用于SAVE时的标志
    getBillCardPanel().getBillModel().updateValue();

    if (getCurOperState() == STATE_EDIT) {
      showHintMessage(getHeadHintText() + CommonConstant.SPACE_MARK
          + nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPP40040401-000060")/*
                                                                                           * @res
                                                                                           * "编辑发票"
                                                                                           */
          + CommonConstant.SPACE_MARK);
    }
    else {
      showHintMessage(getHeadHintText() + CommonConstant.SPACE_MARK
          + nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPP40040401-000032")/*
                                                                                           * @res
                                                                                           * "浏览发票"
                                                                                           */
          + CommonConstant.SPACE_MARK);
    }
    getBillCardPanel().getHeadItem("iinvoicetype").setValue(head.getIinvoicetype().intValue());
    setInvoiceTypeComItem();
    UIComboBox comItem = (UIComboBox) getBillCardPanel().getHeadItem("iinvoicetype").getComponent();
    comItem.setSelectedIndex(head.getIinvoicetype().intValue());
  }

  /**
   * 作者：王印芬 功能：对单据模板设置一个已知的VO !!!!!请同时更新方法setVOToAuditedBillPanel() 参数：无 返回：无
   * 例外：无 日期：(2001-7-01 11:39:21) 修改日期，修改人，修改原因，注释标志： 2002-09-18 wyf
   * 加入对来源单据类型、来源单据号的处理 2002-09-27 wyf 加入对发票类型下拉框的限制 2002-10-23 wyf 业务员参照修改为树表状
   * 2003-02-25 fangy 修改
   */
  private void setVOToBillPanel() {

    nc.vo.scm.pu.Timer timer = new nc.vo.scm.pu.Timer();
    timer.start("setVOToBillPanel");

    // wyf 2002-09-27 add begin
    setInvoiceTypeComItem();
    // wyf 2002-09-27 add end

    if (getInvVOs() == null || getInvVOs().length == 0) {
      // V5 Del : setImageType(this.IMAGE_NULL);

      getBillCardPanel().addNew();

      dispPlanPrice(true);
      return;
    }
    if(getCurVOPos()==-1)
      setCurVOPos(0);
    // added by Fangy 2002-10-11 14:28
    InvoiceVO curVO = getInvVOs()[getCurVOPos()];
    InvoiceHeaderVO head = getInvVOs()[getCurVOPos()].getHeadVO();
    if (curVO.getChildrenVO() == null || curVO.getChildrenVO().length == 0) {
      // 装载表体
      if (!loadItemsForInvoiceVOs(new InvoiceVO[] {
        curVO
      })) {
        setCurVOPos(m_nLstInvVOPos);
        dispPlanPrice(true);
        return;
      }
    }
    timer.addExecutePhase("loadItemsForInvoiceVOs");

    // 设置当前单据的业务类型.
    setCurBizeType(head.getCbiztype());

    // 计算自由项
    InvoiceItemVO[] items = (InvoiceItemVO[]) curVO.getChildrenVO();
    Vector vTemp = new Vector();
    if (items != null && items.length > 0) {
      for (int i = 0; i < items.length; i++) {
        if (items[i].getVfree1() != null || items[i].getVfree2() != null || items[i].getVfree3() != null
            || items[i].getVfree4() != null || items[i].getVfree5() != null) {
          vTemp.addElement(items[i]);
        }
      }
      if (vTemp.size() > 0) {
        InvoiceItemVO[] bodyVO = new InvoiceItemVO[vTemp.size()];
        vTemp.copyInto(bodyVO);
        new nc.ui.scm.pub.FreeVOParse().setFreeVO(bodyVO, "vfree0", "vfree", null, "cmangid", false);
      }
    }

    timer.addExecutePhase("计算自由项");

    // 得到供应商基本ID
    String strVendorBase = head.getCvendorbaseid();
    if (head.getCvendormangid() != null && (strVendorBase == null || strVendorBase.trim().equals(""))) {
      // strVendorBase =
      // getResultFromFormula("getColValue(bd_cumandoc,pk_cubasdoc,pk_cumandoc,cvendormangid)","cvendormangid",head.getCvendormangid())
      // ;
      Object oTemp = null;
      try {
        oTemp = CacheTool.getColumnValue("bd_cumandoc", "pk_cumandoc", "pk_cubasdoc", new String[] {
          head.getCvendormangid()
        });
      }
      catch (Exception e) {
        /** 不必抛出 */
        SCMEnv.out(e);
      }
      if (oTemp != null) {
        Object oo[] = (Object[]) oTemp;
        if (oo != null && oo.length > 0 && oo[0] != null)
          strVendorBase = oo[0].toString();
      }
      head.setCvendorbaseid(strVendorBase);
    }

    timer.addExecutePhase("得到供应商基本PiPqPublicUIClass.getAResultFromTable");

    // 设置部门
    String pk_corp = head.getPk_purcorp();
    UIRefPane pane2 = (UIRefPane) getBillCardPanel().getHeadItem("cdeptid").getComponent();
    // pane2.getRefModel().setWherePart(" (bd_deptdoc.deptattr = '2' or
    // bd_deptdoc.deptattr = '4') and bd_deptdoc.pk_corp = '" + pk_corp + "' ");
    pane2.getRefModel().setPk_corp(pk_corp);

    // 设置业务员
    pane2 = (UIRefPane) getBillCardPanel().getHeadItem("cemployeeid").getComponent();
    // wyf 2002-10-23 modify begin
    // ((EmployeesForDeptRefModel)pane2.getRef().getRefModel()).setCdeptid(head.getCdeptid())
    // ;
    // String s = " bd_psndoc.indocflag='Y' and (bd_psndoc.pk_deptdoc in (select
    // bd_deptdoc.pk_deptdoc from bd_deptdoc where (bd_deptdoc.deptattr = '2' or
    // bd_deptdoc.deptattr = '4') and bd_deptdoc.pk_corp ='"+pk_corp+"')) ";
    // pane2.setRefModel(new
    // EmployeesForDeptRefModel(head.getCdeptid(),addWherePart));
    // pane2.getRefModel().setWherePart(s);
    pane2.getRefModel().setPk_corp(pk_corp);
    // wyf 2002-10-23 modify end
    if (head.getCemployeeid() != null) {
      pane2.setPK(head.getCemployeeid());
    }
    //重新根据币种设置换算率精度 by zhaoyha at 2009.9
    setVoExchgRateDigit(items);
    // 设置VO
    getBillCardPanel().setBillValueVO(curVO);

    // =============设置VO后设置小数位

    // 汇率
    // 设置表头快捷录入的显示：取第一行的值
    InvoiceItemVO voFirstItem = curVO.getBodyVO()[0];

    getBillCardPanel().getHeadItem("ccurrencytypeid").setValue(voFirstItem.getCcurrencytypeid());
    head.setCcurrencytypeid(voFirstItem.getCcurrencytypeid());
    // 设置表头币种精度
    resetHeadCurrDigits();
    getBillCardPanel().getHeadItem("nexchangeotobrate").setValue(voFirstItem.getNexchangeotobrate());
    head.setNexchangeotobrate(voFirstItem.getNexchangeotobrate());

      
    // getInvBillPanel().getHeadItem("cprojectid").setValue(voFirstItem.getCprojectid());
    if (voFirstItem.getIdiscounttaxtype() != null && voFirstItem.getIdiscounttaxtype().toString().trim().length() > 0) {
      getBillCardPanel().getHeadItem("idiscounttaxtype").setValue(voFirstItem.getIdiscounttaxtype());
    }
    else {
      getBillCardPanel().getHeadItem("idiscounttaxtype").setValue(new Integer(1));
    }
    getBillCardPanel().getHeadItem("ntaxrate").setValue(voFirstItem.getNtaxrate());
    setCurrRateToBillHead(head.getCcurrencytypeid(), head.getNexchangeotobrate());
    // 金额的小数位等
    // setCurrMoneyDigitToBill(head.getCcurrencytypeid());

    // /////========电话,开户银行,帐号,取供应商还是散户
    if (curVO.getHeadVO().getCfreecustid() == null) {
      // 散户置灰,清空相应信息
      getBillCardPanel().getHeadItem("cfreecustid").setValue(null);
      ((UIRefPane) getBillCardPanel().getHeadItem("caccountbankid").getComponent()).setButtonVisible(true);

      // 供应商电话
      setDefaultPhoneForAVendor(strVendorBase);
      // 开户银行:
      UIRefPane pane1 = (UIRefPane) getBillCardPanel().getHeadItem("caccountbankid").getComponent();
      //((AccountsForVendorRefModel) pane1.getRef().getRefModel()).setCvendorbaseid(strVendorBase);
      if (head.getCaccountbankid() != null) {
        pane1.setPK(head.getCaccountbankid());
        String strAccount = null;
        try {
          Object oTemp = CacheTool.getColumnValue("bd_bankaccbas", "pk_bankaccbas", "accountname", new String[] {
            head.getCaccountbankid()
          });
          if (oTemp != null)
            strAccount = ((Object[]) oTemp)[0] == null ? null : ((Object[]) oTemp)[0].toString();

          timer.addExecutePhase("queryResultsFromAnyTable");
        }
        catch (Exception e) {
          SCMEnv.out(e);
          SCMEnv.out("取供应商默认银行时出现异常!");
        }
        // 帐号
        getBillCardPanel().setHeadItem("cvendoraccount", strAccount);
      }
      else {
        if (getCurOperState() == STATE_EDIT) {
          setDefaultBankAccountForAVendor(strVendorBase);
        }
      }
      // //帐号
      // if (head.getCaccountbankid() != null){
      // Object oTemp = null;
      // try {
      // oTemp = CacheTool.getColumnValue("bd_custbank", "pk_custbank",
      // "account",new String[]{head.getCaccountbankid()});
      // } catch (Exception e) {
      // /**不必抛出*/
      // SCMEnv.out(e);
      // }
      // if(oTemp != null)
      // getInvBillPanel().setHeadItem("cvendoraccount",((Object[])oTemp)[0].toString());
      //
      // timer.addExecutePhase("PiPqPublicUIClass.getAResultFromTable");
      // }

    }
    else {
      // 开户银行不可编辑,按钮不可见
      ((UIRefPane) getBillCardPanel().getHeadItem("caccountbankid").getComponent()).setButtonVisible(false);
      // 影响的信息
      setDefaultInfoForAFreeCust(curVO.getHeadVO().getCfreecustid());
    }

    getBillCardPanel().getHeadItem("nexchangeotobrate").getValue();

    this.loadBDData();

    timer.addExecutePhase("loadBDData");

    // 期初标志
    UICheckBox initCheck = (UICheckBox) getBillCardPanel().getHeadItem("finitflag").getComponent();
    if (head.getFinitflag() == null || head.getFinitflag().intValue() == 0) {
      initCheck.setSelected(false);
    }
    else {
      initCheck.setSelected(true);
    }
    // 扣税类别
    int selectedIndex = ((UIComboBox) getBillCardPanel().getHeadItem("idiscounttaxtype").getComponent())
        .getSelectedIndex();
    ((UIComboBox) getBillCardPanel().getHeadItem("idiscounttaxtype").getComponent()).setSelectedIndex(selectedIndex);
    // 备注
    if(getBillCardPanel().getHeadItem("vmemo").getComponent() instanceof UIRefPane){
      ((UIRefPane) getBillCardPanel().getHeadItem("vmemo").getComponent()).setText(head.getVmemo());
    }

    // 执行表体公式
    if (curVO.getBodyVO() != null) {
      getBillCardPanel().getBillModel().execLoadFormula();
    }

    timer.addExecutePhase("execLoadFormula");

    getBillCardPanel().getHeadItem("nexchangeotobrate").getValue();

    // 备注填空串，保证触发afterEdit
    // 设置金额等位数为最大的位数
    setMaxMnyDigit(iMaxMnyDigit);
    // 取表头币种
    String sCurrId = getBillCardPanel().getHeadItem("ccurrencytypeid").getValue();
    String sBodyCurrId = null;
    //效率优化 by zhaoyha at 2009.9
    boolean isNeedCal=getBillCardPanel().getBillModel().isNeedCalculate();
    getBillCardPanel().getBillModel().setNeedCalculate(false);
    if (curVO.getBodyVO() != null) {
      for (int i = 0; i < curVO.getBodyVO().length; i++) {
        if (curVO.getBodyVO()[i].getVmemo() == null) {
          getBillCardPanel().setBodyValueAt("", i, "vmemo");
        }
        // 新增表体行币种默认为表头币种
        sBodyCurrId = (String) getBillCardPanel().getBillModel().getValueAt(i, "ccurrencytypeid");
        if (sBodyCurrId == null || sBodyCurrId.equals("")) {
          getBillCardPanel().getBillModel().setValueAt(sCurrId, i, "ccurrencytypeid");
          // 获取币种名称
          // 驱动表体相关币种的改动
          afterEditWhenBodyCurrency(new BillEditEvent(getBillCardPanel().getBodyItem("ccurrencytypeid").getComponent(),
              sCurrId, "ccurrencytypeid", i));
          // 设置修改标志
          getBillCardPanel().getBillModel().setRowState(i, BillModel.MODIFICATION);
          getBillCardPanel().getBillModel().getValueAt(i, "ccurrencytypeid");
          setExchangeRateBody(i, true, null);
        }
        setExchangeRateBody(i, false, curVO.getBodyVO()[i]);
        // 设置原\本\辅精度
        // setRowDigits_Mny(getPk_corp(),
        // i,getInvBillPanel().getBillModel(),m_cardPoPubSetUI2);
        // 原币金额类(原币金额--noriginalcurmny;原币税额--noriginaltaxmny;原币价税合计--noriginalsummny)
        getBillCardPanel()
            .setBodyValueAt(getBillCardPanel().getBodyValueAt(i, "noriginalcurmny"), i, "noriginalcurmny");
        getBillCardPanel()
            .setBodyValueAt(getBillCardPanel().getBodyValueAt(i, "noriginaltaxmny"), i, "noriginaltaxmny");
        getBillCardPanel()
            .setBodyValueAt(getBillCardPanel().getBodyValueAt(i, "noriginalsummny"), i, "noriginalsummny");
        getBillCardPanel().setBodyValueAt(getBillCardPanel().getBodyValueAt(i, "noriginalpaymentmny"), i,
            "noriginalpaymentmny");

        getBillCardPanel().setBodyValueAt(getBillCardPanel().getBodyValueAt(i, "nmoney"), i, "nmoney");
        getBillCardPanel().setBodyValueAt(getBillCardPanel().getBodyValueAt(i, "ntaxmny"), i, "ntaxmny");
        getBillCardPanel().setBodyValueAt(getBillCardPanel().getBodyValueAt(i, "nsummny"), i, "nsummny");
        getBillCardPanel().setBodyValueAt(getBillCardPanel().getBodyValueAt(i, "npaymentmny"), i, "npaymentmny");
      }
    }


    // 加载来源信息,加载源头信息

    if(isNeedLoadSrcInfo(getBillCardPanel().getBillModel()))
      PuTool.loadSourceInfoAll(getBillCardPanel(), BillTypeConst.PO_INVOICE);
    timer.addExecutePhase("加载来源、源头");

    // 主要用于SAVE时的标志
    getBillCardPanel().getBillModel().updateValue();

    // 填充表头公式数据，避免不可参照出的数据
    execHeadTailFormula(curVO);
    getBillCardPanel().execHeadEditFormulas();

    timer.addExecutePhase("execHeadTailFormula");

    // ----------------------------
    // modified by Czp
    // int iStatus = 0;
    // if (curVO.getHeadVO().getIbillstatus() != null)
    // iStatus = curVO.getHeadVO().getIbillstatus().intValue();
    // if (iStatus == 0)
    // V5 Del : setImageType(this.IMAGE_NULL);
    // else if (iStatus == 1)
    // V5 Del : setImageType(this.IMAGE_CANCEL);
    // else if (iStatus == 2)
    // V5 Del : setImageType(this.IMAGE_APPROVING);
    // else if (iStatus == 3)
    // V5 Del : setImageType(this.IMAGE_AUDIT);
    // else if (iStatus == 4)
    // V5 Del : setImageType(this.IMAGE_APPROVEANDFAIL);
    // else
    // V5 Del : setImageType(this.IMAGE_NULL);
    // ----------------------------
    if (getCurOperState() == STATE_EDIT) {
      // ----------------------------
      // modified by Czp
      // V5 Del : setImageType(this.IMAGE_NULL);
      // ----------------------------
      showHintMessage(getHeadHintText() + CommonConstant.SPACE_MARK
          + nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPP40040401-000060")/*
                                                                                           * @res
                                                                                           * "编辑发票"
                                                                                           */
          + CommonConstant.SPACE_MARK);
    }
    else {
      showHintMessage(getHeadHintText() + CommonConstant.SPACE_MARK
          + nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPP40040401-000032")/*
                                                                                           * @res
                                                                                           * "浏览发票"
                                                                                           */
          + CommonConstant.SPACE_MARK);
    }
    timer.addExecutePhase("其他操作");

    timer.showAllExecutePhase("setVOToBillPanel");

    dispPlanPrice(true);

    // 设置采购组织
    String cpurorganization = head.getCpurorganization();
    String strFormula = null;
    UIRefPane refpnl = null;
    strFormula = "cpurorganizationname->getColValue(bd_purorg,name,pk_purorg,cpurorganization)";
    Object ob = getBillCardPanel().execHeadFormula(strFormula);
    if (getBillCardPanel().getHeadItem("cpurorganizationname") != null) {
      refpnl = (UIRefPane) getBillCardPanel().getHeadItem("cpurorganizationname").getComponent();
      refpnl.setValue((String) ob);
    }

    // 收货公司
    String pk_arrvcorp = head.getPk_arrvcorp();
    strFormula = "arrvcorpname->getColValue(bd_corp,unitname,pk_corp,pk_arrvcorp)";
    ob = getBillCardPanel().execHeadFormula(strFormula);
    if (getBillCardPanel().getHeadItem("arrvcorpname") != null) {
      refpnl = (UIRefPane) getBillCardPanel().getHeadItem("arrvcorpname").getComponent();
      refpnl.setValue((String) ob);
    }
    
    //效率优化 by zhaoyha at 2009.9
    getBillCardPanel().getBillModel().setNeedCalculate(isNeedCal);

  }

  /**
   * 作者：晁志平 功能：设置折本及折辅汇率小数位 参数：无 返回：无 例外：无 日期：2005-6-17 14:39:21
   * 修改日期，修改人，修改原因，注释标志
   */
  private void resetHeadCurrDigits() {
    // =====表头
    String sCurrId = getBillCardPanel().getHeadItem("ccurrencytypeid").getValue();
    if (sCurrId != null && sCurrId.trim().length() > 0) {
      // 得到汇率精度
      int[] iaExchRateDigit = m_cardPoPubSetUI2.getBothExchRateDigit(getPk_corp(), sCurrId);
      // 设置精度
      getBillCardPanel().getHeadItem("nexchangeotobrate").setDecimalDigits(iaExchRateDigit[0]);
    }
  }

  /**
   * 作者：WYF 功能：设置表体与币中有关的值的精度，此方法与列表界面同时使用 参数：String pk_corp 公司ID BillModel
   * billModel BillModel 返回：无 例外：无 日期：(2004-6-11 11:39:21) 修改日期，修改人，修改原因，注释标志：
   */
  protected static void resetBodyValueRelated_Curr(String pk_corp, String strHeadCurId, BillModel billModel,
      BusinessCurrencyRateUtil bca, int iLen, POPubSetUI2 setUi) {
    // =====表体
    // 金额类需合计
    String[] saMnyItem = new String[] {
        "noriginalcurmny", "noriginaltaxmny", "noriginalsummny", "noriginalpaymentmny", "nmoney", "ntaxmny", "nsummny",
        "npaymentmny"
    };
    int iMnyLen = saMnyItem.length;
    // 金额的最大位数
    int iMaxMnyDigit = 0;

    // 设为不合计，否则将占用非常多的时间
    boolean bOldNeedCalculate = billModel.isNeedCalculate();
    billModel.setNeedCalculate(false);

    for (int i = 0; i < iLen; i++) {
      // 折本、折辅汇率
      setRowDigits_ExchangeRate(pk_corp, i, billModel, setUi);
      billModel.setValueAt(billModel.getValueAt(i, "nexchangeotobrate"), i, "nexchangeotobrate");
      // 金额、税额、价税合计
      // setRowDigits_Mny(pk_corp, i, billModel,setUi);
      for (int j = 0; j < iMnyLen; j++) {
        billModel.setValueAt(billModel.getValueAt(i, saMnyItem[j]), i, saMnyItem[j]);
      }
      if (billModel.getItemByKey(saMnyItem[0]).getDecimalDigits() > iMaxMnyDigit) {
        iMaxMnyDigit = billModel.getItemByKey(saMnyItem[0]).getDecimalDigits();
      }
    }

    // 计算合计
    for (int i = 0; i < iMnyLen; i++) {
      billModel.getItemByKey(saMnyItem[0]).setDecimalDigits(iMaxMnyDigit);
      billModel.reCalcurate(billModel.getBodyColByKey(saMnyItem[i]));
    }
    billModel.setNeedCalculate(bOldNeedCalculate);
  }

  private void dispPlanPrice(boolean bCard) {

    // 显示计划价*****
    if (bCard && getBillCardPanel().getRowCount() > 0) {
      BillItem item = getBillCardPanel().getBodyItem("nplanprice");
      if (item != null && item.isShow()) {
        int nRowCount = getBillCardPanel().getRowCount();
        String cMangID[] = new String[nRowCount];
        for (int i = 0; i < nRowCount; i++)
          cMangID[i] = (String) getBillCardPanel().getBodyValueAt(i, "cmangid");
        UFDouble nPrice[] = queryPlanPrices(cMangID, getBillCardPanel().getHeadItem("cstoreorganization").getValue());
        if (nPrice != null) {
          for (int i = 0; i < nRowCount; i++)
            getBillCardPanel().setBodyValueAt(nPrice[i], i, "nplanprice");
        }

      }
    }
    else if (!bCard && getBillListPanel().getBodyBillModel().getRowCount() > 0) {
      BillItem item = getBillListPanel().getBodyItem("nplanprice");
      if (item != null && item.isShow()) {
        int nRowCount = getBillListPanel().getBodyBillModel().getRowCount();
        String cMangID[] = new String[nRowCount];
        for (int i = 0; i < nRowCount; i++)
          cMangID[i] = (String) getBillListPanel().getBodyBillModel().getValueAt(i, "cmangid");
        UFDouble nPrice[] = queryPlanPrices(cMangID, (String) getBillListPanel().getHeadBillModel().getValueAt(
            getCurVOPos(), "cstoreorganization"));
        if (nPrice != null) {
          for (int i = 0; i < nRowCount; i++)
            getBillListPanel().getBodyBillModel().setValueAt(nPrice[i], i, "nplanprice");
        }
      }
    }
    // *****

  }

  /**
   * 作者：王印芬 功能：在卡片及列表之间切换 参数：无 返回：无 例外：无 日期：(2001-7-01 11:39:21)
   * 修改日期，修改人，修改原因，注释标志： 2002-08-20 wyf 去掉对票题的注释
   */
  private void shiftShowModeTo(int stateIndex) {
    if (stateIndex == INV_PANEL_LIST && getCurPanelMode() != INV_PANEL_LIST) {
      // wyf 2002-08-20 delete begin
      // setTitleText("维护发票") ;
      // wyf 2002-08-20 delete end
      remove(getCurPanel());
      setCurPanelMode(INV_PANEL_LIST);
      initList();
      updateUI();
//    setButtons(getBtnss());
      setButtons(m_btnTree.getButtonArray());
      // setButtonsAndPanelState();
      updateButtons();
    }
    else if (stateIndex == INV_PANEL_CARD && getCurPanelMode() != INV_PANEL_CARD) {
      // wyf 2002-08-20 delete begin
      // setTitleText("维护发票") ;
      // wyf 2002-08-20 delete end
      remove(getCurPanel());
      setCurPanelMode(INV_PANEL_CARD);
      initCard();
      updateUI();
      //setButtons(getBtnss());
      setButtons(m_btnTree.getButtonArray());
      // setButtonsAndPanelState();
      updateButtons();
    }

  }

  /**
   * 作者：王印芬 功能：对列表模板设置一个已知的VO 参数：无 返回：无 例外：无 日期：(2001-7-01 11:39:21)
   * 修改日期，修改人，修改原因，注释标志： 2002-09-18 wyf 加入对来源单据类型、来源单据号的处理 修改列表体的显示为调用公共方法
   * 2003-02-21 方益, 效率修改,换行修改,单选多选修改
   */
  private void showSelectedInvoice() {

    // 装载表体
    int row = getCurVOPos();

    // 更新表头列表.
    if (row >= 0 && row < getInvVOs().length) {
      InvoiceVO curVO = getInvVOs()[row];
      if (!loadItemsForInvoiceVOs(new InvoiceVO[] {
        curVO
      })) {
        setVoBodyToListPanle(-1);
      }
      else {
        // 设置表体列表.
        setVoBodyToListPanle(row);

      }
    }
    else {
      setVoBodyToListPanle(-1);
    }
  }

  /**
   * 作者：王印芬 功能：实现ListSelectionListener的监听方法 参数：ListSelectionEvent e 监听事件 返回：无
   * 例外：无 日期：(2002-5-23 11:39:21) 修改日期，修改人，修改原因，注释标志： 2003-02-21 方益,
   * 效率修改,换行修改,单选多选修改
   */
  public void valueChanged(ListSelectionEvent e) {

    // if(e.getValueIsAdjusting())
    // return;

    int iCount = getBillListPanel().getHeadTable().getRowCount();
    for (int i = 0; i < iCount; i++) {
      getBillListPanel().getHeadBillModel().setRowState(i, BillModel.NORMAL);
    }

    // 得到被选中的行
    int[] iaSelectedRow = getBillListPanel().getHeadTable().getSelectedRows();
    if (iaSelectedRow == null || iaSelectedRow.length == 0) {
      setSelectedRowCount(0);
    }
    else {
      iCount = iaSelectedRow.length;
      // 选中的行表示为打＊号
      for (int i = 0; i < iCount; i++)
        getBillListPanel().getHeadBillModel().setRowState(iaSelectedRow[i], BillModel.SELECTED);
      // 影响按钮逻辑
      setSelectedRowCount(iCount);
      if (iCount == 1) {
        // 单选时,才显示表体
        int nCurIndex = PuTool.getIndexBeforeSort(getBillListPanel(), iaSelectedRow[0]);
        setCurVOPos(nCurIndex);
      }
      else {
        // 多选时,不显示任何表体
        setCurVOPos(-1);
      }
      showSelectedInvoice();
    }
    // 更新按钮状态
    setButtonsAndPanelState();
    updateButtons();
  }

  /**
   * 功能描述:自定义项保存PK(表体)
   */
  private void setBodyDefPK(BillEditEvent event) {
    if (event.getKey().equals("vdef1")) {
      DefSetTool.afterEditBody(getBillCardPanel().getBillModel(), event.getRow(), "vdef1", "pk_defdoc1");
    }
    else if (event.getKey().equals("vdef2")) {
      DefSetTool.afterEditBody(getBillCardPanel().getBillModel(), event.getRow(), "vdef2", "pk_defdoc2");
    }
    else if (event.getKey().equals("vdef3")) {
      DefSetTool.afterEditBody(getBillCardPanel().getBillModel(), event.getRow(), "vdef3", "pk_defdoc3");
    }
    else if (event.getKey().equals("vdef4")) {
      DefSetTool.afterEditBody(getBillCardPanel().getBillModel(), event.getRow(), "vdef4", "pk_defdoc4");
    }
    else if (event.getKey().equals("vdef5")) {
      DefSetTool.afterEditBody(getBillCardPanel().getBillModel(), event.getRow(), "vdef5", "pk_defdoc5");
    }
    else if (event.getKey().equals("vdef6")) {
      DefSetTool.afterEditBody(getBillCardPanel().getBillModel(), event.getRow(), "vdef6", "pk_defdoc6");
    }
    else if (event.getKey().equals("vdef7")) {
      DefSetTool.afterEditBody(getBillCardPanel().getBillModel(), event.getRow(), "vdef7", "pk_defdoc7");
    }
    else if (event.getKey().equals("vdef8")) {
      DefSetTool.afterEditBody(getBillCardPanel().getBillModel(), event.getRow(), "vdef8", "pk_defdoc8");
    }
    else if (event.getKey().equals("vdef9")) {
      DefSetTool.afterEditBody(getBillCardPanel().getBillModel(), event.getRow(), "vdef9", "pk_defdoc9");
    }
    else if (event.getKey().equals("vdef10")) {
      DefSetTool.afterEditBody(getBillCardPanel().getBillModel(), event.getRow(), "vdef10", "pk_defdoc10");
    }
    else if (event.getKey().equals("vdef11")) {
      DefSetTool.afterEditBody(getBillCardPanel().getBillModel(), event.getRow(), "vdef11", "pk_defdoc11");
    }
    else if (event.getKey().equals("vdef12")) {
      DefSetTool.afterEditBody(getBillCardPanel().getBillModel(), event.getRow(), "vdef12", "pk_defdoc12");
    }
    else if (event.getKey().equals("vdef13")) {
      DefSetTool.afterEditBody(getBillCardPanel().getBillModel(), event.getRow(), "vdef13", "pk_defdoc13");
    }
    else if (event.getKey().equals("vdef14")) {
      DefSetTool.afterEditBody(getBillCardPanel().getBillModel(), event.getRow(), "vdef14", "pk_defdoc14");
    }
    else if (event.getKey().equals("vdef15")) {
      DefSetTool.afterEditBody(getBillCardPanel().getBillModel(), event.getRow(), "vdef15", "pk_defdoc15");
    }
    else if (event.getKey().equals("vdef16")) {
      DefSetTool.afterEditBody(getBillCardPanel().getBillModel(), event.getRow(), "vdef16", "pk_defdoc16");
    }
    else if (event.getKey().equals("vdef17")) {
      DefSetTool.afterEditBody(getBillCardPanel().getBillModel(), event.getRow(), "vdef17", "pk_defdoc17");
    }
    else if (event.getKey().equals("vdef18")) {
      DefSetTool.afterEditBody(getBillCardPanel().getBillModel(), event.getRow(), "vdef18", "pk_defdoc18");
    }
    else if (event.getKey().equals("vdef19")) {
      DefSetTool.afterEditBody(getBillCardPanel().getBillModel(), event.getRow(), "vdef19", "pk_defdoc19");
    }
    else if (event.getKey().equals("vdef20")) {
      DefSetTool.afterEditBody(getBillCardPanel().getBillModel(), event.getRow(), "vdef20", "pk_defdoc20");
    }
  }

  /**
   * 功能描述:自定义项保存PK(表头)
   */
  private void setHeadDefPK(BillEditEvent event) {
    if (event.getKey().equals("vdef1")) {
      DefSetTool.afterEditHead(getBillCardPanel().getBillData(), "vdef1", "pk_defdoc1");
    }
    else if (event.getKey().equals("vdef2")) {
      DefSetTool.afterEditHead(getBillCardPanel().getBillData(), "vdef2", "pk_defdoc2");
    }
    else if (event.getKey().equals("vdef3")) {
      DefSetTool.afterEditHead(getBillCardPanel().getBillData(), "vdef3", "pk_defdoc3");
    }
    else if (event.getKey().equals("vdef4")) {
      DefSetTool.afterEditHead(getBillCardPanel().getBillData(), "vdef4", "pk_defdoc4");
    }
    else if (event.getKey().equals("vdef5")) {
      DefSetTool.afterEditHead(getBillCardPanel().getBillData(), "vdef5", "pk_defdoc5");
    }
    else if (event.getKey().equals("vdef6")) {
      DefSetTool.afterEditHead(getBillCardPanel().getBillData(), "vdef6", "pk_defdoc6");
    }
    else if (event.getKey().equals("vdef7")) {
      DefSetTool.afterEditHead(getBillCardPanel().getBillData(), "vdef7", "pk_defdoc7");
    }
    else if (event.getKey().equals("vdef8")) {
      DefSetTool.afterEditHead(getBillCardPanel().getBillData(), "vdef8", "pk_defdoc8");
    }
    else if (event.getKey().equals("vdef9")) {
      DefSetTool.afterEditHead(getBillCardPanel().getBillData(), "vdef9", "pk_defdoc9");
    }
    else if (event.getKey().equals("vdef10")) {
      DefSetTool.afterEditHead(getBillCardPanel().getBillData(), "vdef10", "pk_defdoc10");
    }
    else if (event.getKey().equals("vdef11")) {
      DefSetTool.afterEditHead(getBillCardPanel().getBillData(), "vdef11", "pk_defdoc11");
    }
    else if (event.getKey().equals("vdef12")) {
      DefSetTool.afterEditHead(getBillCardPanel().getBillData(), "vdef12", "pk_defdoc12");
    }
    else if (event.getKey().equals("vdef13")) {
      DefSetTool.afterEditHead(getBillCardPanel().getBillData(), "vdef13", "pk_defdoc13");
    }
    else if (event.getKey().equals("vdef14")) {
      DefSetTool.afterEditHead(getBillCardPanel().getBillData(), "vdef14", "pk_defdoc14");
    }
    else if (event.getKey().equals("vdef15")) {
      DefSetTool.afterEditHead(getBillCardPanel().getBillData(), "vdef15", "pk_defdoc15");
    }
    else if (event.getKey().equals("vdef16")) {
      DefSetTool.afterEditHead(getBillCardPanel().getBillData(), "vdef16", "pk_defdoc16");
    }
    else if (event.getKey().equals("vdef17")) {
      DefSetTool.afterEditHead(getBillCardPanel().getBillData(), "vdef17", "pk_defdoc17");
    }
    else if (event.getKey().equals("vdef18")) {
      DefSetTool.afterEditHead(getBillCardPanel().getBillData(), "vdef18", "pk_defdoc18");
    }
    else if (event.getKey().equals("vdef19")) {
      DefSetTool.afterEditHead(getBillCardPanel().getBillData(), "vdef19", "pk_defdoc19");
    }
    else if (event.getKey().equals("vdef20")) {
      DefSetTool.afterEditHead(getBillCardPanel().getBillData(), "vdef20", "pk_defdoc20");
    }
  }

  /**
   * 作者：汪维敏 功能：计算打印次数 参数：无 返回：无 例外：无 日期：(2004-12-15 11:39:21)
   */
  private void onCardPrint() {
    // 打印时不要再进行排序 For V56 by zhaoyha at 2009.3.26
    getBillCardPanel().getBillModel().setSortColumn(null);
    InvoiceVO vo = (InvoiceVO) getInvVOs()[getCurVOPos()];
    ArrayList aryRslt = new ArrayList();
    aryRslt.add(vo);
    if (printCard == null) {
      printCard = new ScmPrintTool(this, getBillCardPanel(), aryRslt, getModuleCode());
    }
    else {
      try {
        printCard.setData(aryRslt);
      }
      catch (Exception e1) {
        SCMEnv.out(e1);
        return;
      }
    }
    try {
      //先屏蔽参照，防止打印时清空数据 by zhaoyha at 2009.3.27
      //List<BillItem> maskedItems=PuTool.maskUIRefType(getBillCardPanel());
      printCard.onCardPrint(getBillCardPanel(), getBillListPanel(), ScmConst.PO_Invoice);
      if (PuPubVO.getString_TrimZeroLenAsNull(printCard.getPrintMessage()) != null) {
        MessageDialog.showHintDlg(this,
            nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000270")/*
                                                                                             * @res
                                                                                             * "提示"
                                                                                             */, printCard
                .getPrintMessage());
      }
      //恢复先前屏蔽的参照
      //PuTool.restoreUIRefType(maskedItems);
    }
    catch (BusinessException e) {
      SCMEnv.out(e);
    }
  }

  /**
   * 作者：汪维敏 功能：计算打印次数 参数：无 返回：无 例外：无 日期：(2004-12-15 11:39:21)
   */
  private void onCardPrintPreview() {
    // 打印时不要再进行排序 For V56 by zhaoyha at 2009.3.26
    getBillCardPanel().getBillModel().setSortColumn(null);   
    InvoiceVO vo = (InvoiceVO) getInvVOs()[getCurVOPos()];
    ArrayList aryRslt = new ArrayList();
    aryRslt.add(vo);
    if (printCard == null) {
      printCard = new ScmPrintTool(this, getBillCardPanel(), aryRslt, getModuleCode());
    }
    else {
      try {
        printCard.setData(aryRslt);
      }
      catch (Exception e1) {
        SCMEnv.out(e1);
        return;
      }
    }
    try {
      //先屏蔽参照，防止打印时清空数据 by zhaoyha at 2009.3.27
      //List<BillItem> maskedItems=PuTool.maskUIRefType(getBillCardPanel());
      printCard.onCardPrintPreview(getBillCardPanel(), getBillListPanel(), ScmConst.PO_Invoice);
      if (PuPubVO.getString_TrimZeroLenAsNull(printCard.getPrintMessage()) != null) {
        showHintMessage(printCard.getPrintMessage());
//        MessageDialog.showHintDlg(this,
//            nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000270")/*
//                                                                                             * @res
//                                                                                             * "提示"
//                                                                                             */, printCard
//                .getPrintMessage());
      }
      //恢复先前屏蔽的参照
      //PuTool.restoreUIRefType(maskedItems);
    }
    catch (BusinessException e) {
      SCMEnv.out(e);
    }
    getBillCardPanel().updateUI();
  }

  /**
   * 作者：汪维敏 功能：批打印 参数：无 返回：无 例外：无 日期：(2004-12-15 11:39:21)
   */
  private void onBatchPrint() {
    if (printList == null) {
      printList = new ScmPrintTool(this, getBillCardPanel(), getSelectedBills(), getModuleCode());
    }
    else {
      try {
        printList.setData(getSelectedBills());
      }
      catch (Exception e1) {
        SCMEnv.out(e1);
        return;
      }
    }
    try {
      printList.onBatchPrint(getBillListPanel(), getBillCardPanel(), ScmConst.PO_Invoice);
      if (PuPubVO.getString_TrimZeroLenAsNull(printList.getPrintMessage()) != null) {
        showHintMessage(printList.getPrintMessage());
//        MessageDialog.showHintDlg(this,
//            nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPPSCMCommon-000270")/*
//                                                                                           * @res
//                                                                                           * "提示"
//                                                                                           */, printList
//                .getPrintMessage());
      }
    }
    catch (BusinessException e) {
      SCMEnv.out(e);
    }
  }

  /**
   * 作者：汪维敏 功能：批打印 参数：无 返回：无 例外：无 日期：(2004-12-15 11:39:21)
   */
  private void onBatchPrintPreview() {
    if (printList == null) {
      printList = new ScmPrintTool(this, getBillCardPanel(), getSelectedBills(), getModuleCode());
    }
    else {
      try {
        printList.setData(getSelectedBills());
      }
      catch (Exception e1) {
        SCMEnv.out(e1);
        return;
      }
    }
    try {
      printList.onBatchPrintPreview(getBillListPanel(), getBillCardPanel(), ScmConst.PO_Invoice);
      if (PuPubVO.getString_TrimZeroLenAsNull(printList.getPrintMessage()) != null) {
        showHintMessage(printCard.getPrintMessage());
//        MessageDialog.showHintDlg(this,
//            nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000270")/*
//                                                                                             * @res
//                                                                                             * "提示"
//                                                                                             */, printList
//                .getPrintMessage());
      }
    }
    catch (BusinessException e) {
      SCMEnv.out(e);
    }
  }

  /*
   * （非 Javadoc）
   * 
   * @see nc.ui.scm.pub.print.ISetBillVO#setBillVO(nc.vo.pub.AggregatedValueObject)
   */
  public void setBillVO(AggregatedValueObject vo) {
    // TODO 自动生成方法存根
    // 保存原来的位置
    m_nLstInvVOPos = getCurVOPos();

    if (getCurPanelMode() == INV_PANEL_LIST) {
      for (int i = 0; i < getInvVOs().length; i++) {
        if (getInvVOs()[i].getPrimaryKey().equalsIgnoreCase(((InvoiceVO) vo).getPrimaryKey())) {
          setCurVOPos(i);
        }

      }
    }
    // 设置卡片界面数据
    setVOToBillPanel();
  }

  /**
   * 获取汇率精度设置工具
   */
  public POPubSetUI2 getPoPubSetUi2() {
    if (m_cardPoPubSetUI2 == null) {
      m_cardPoPubSetUI2 = new POPubSetUI2();
    }
    return m_cardPoPubSetUI2;
  }

  /*
   * （非 Javadoc）
   * 
   * @see nc.ui.scm.pub.bill.IBillExtendFun#getExtendBtns()
   */
  public ButtonObject[] getExtendBtns() {
    // TODO 自动生成方法存根
//		if (extendBtns == null || extendBtns.length == 0) {
//			// 加入费用发票按钮 add by QuSida 2010-9-22 （佛山骏杰）
//			extendBtns = new ButtonObject[] { getBoFeeInvoice()
//					};
//			return extendBtns;
//		}
//		return extendBtns;
	  return null;
  }
//  public ButtonObject getBoFeeInvoice(){
//	  if(feeInvoice == null){
//		  feeInvoice = new ButtonObject("费用发票","费用发票","费用发票");
//		  return feeInvoice;
//	  }
//	  return feeInvoice;
//  }

  /*
   * （非 Javadoc）
   * 
   * @see nc.ui.scm.pub.bill.IBillExtendFun#onExtendBtnsClick(nc.ui.pub.ButtonObject)
   */
  public void onExtendBtnsClick(ButtonObject bo) {
    // TODO 自动生成方法存根
//		 if(bo == getBoFeeInvoice()){
//		    	this.onBoFeeInvoice();
//		    }
  }

  /*
   * （非 Javadoc）
   * 
   * @see nc.ui.scm.pub.bill.IBillExtendFun#setExtendBtnsStat(int)
   */
  public void setExtendBtnsStat(int iState) {
//    if(iState == 0){
//    	 getBoFeeInvoice().setEnabled(true);
//    }
//    else if(iState == 1){
//    	 getBoFeeInvoice().setEnabled(true);
//    }
//   else if(iState == 2){
//	   getBoFeeInvoice().setEnabled(false);
//      }
// else if(iState == 3){
//	 getBoFeeInvoice().setEnabled(false);
// }
// else if(iState == 4){
//	 getBoFeeInvoice().setEnabled(false);
// }
// else if(iState == 4){
//	 getBoFeeInvoice().setEnabled(false);
// }

  }
//private void onBoFeeInvoice(){
////	getBoFeeInvoice().setTag("D1:"+m_strCurBizeType);
// 	getBoFeeInvoice().setTag("D1:");
//	PfUtilClient.childButtonClicked(getBoFeeInvoice(), nc.ui.pub.ClientEnvironment.getInstance().getCorporation().getPk_corp(),
//            getModuleCode(), nc.ui.pub.ClientEnvironment.getInstance().getUser().getPrimaryKey(),
//            "25", this);
////	PfUtilClient.childButtonClicked(getBoFeeInvoice(),this.getCurrentCorp(),this.getNodeCode(),this.getDjSettingParam().getPk_user(),
////			getDjDataBuffer().getCurrentDjlxbm(),this);]
//	if(PfUtilClient.isCloseOK()){
//	AggregatedValueObject[] vos = PfUtilClient.getRetVos();
//	ArrayList list = new ArrayList();
//	for (int i = 0; i < vos.length; i++) {
//		InvoiceVO vo = new InvoiceVO();
//		
//	}
//	}
//	
//}
private InvoiceVO voProcess(AggregatedValueObject avo){
	InvoiceVO vo = new InvoiceVO();
	
	
	return vo;
}
  public Object[] getRelaSortObjectArray() {
    // TODO 自动生成方法存根
    return m_InvVOs;
  }

  public void doMaintainAction(ILinkMaintainData maintaindata) {
    // TODO 自动生成方法存根
    initi();
    String billID = maintaindata.getBillID();

    setCauditid(billID);

    // 没有符合条件的发票
    InvoiceVO vo = null;
    try {
      vo = (InvoiceVO) getVo();
    }
    catch (Exception e) {
      SCMEnv.out(e);
      MessageDialog.showHintDlg(this,
          nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPPSCMCommon-000270")/*
                                                                                         * @res
                                                                                         * "提示"
                                                                                         */,
          nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPPSCMCommon-000428")/*
                                                                                         * @res
                                                                                         * "系统故障！"
                                                                                         */);
    }

    if (vo == null) {
      setInvVOs(null);
      MessageDialog.showHintDlg(this,
          nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPPSCMCommon-000270")/*
                                                                                         * @res
                                                                                         * "提示"
                                                                                         */,
          nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPP40040401-000008")/*
                                                                                         * @res
                                                                                         * "没有符合查询条件的发票！"
                                                                                         */);
      getBillCardPanel().addNew();
      return;
    }

    //
    UIComboBox comItem = (UIComboBox) getBillCardPanel().getHeadItem("iinvoicetype").getComponent();
    getBillCardPanel().getHeadItem("iinvoicetype").setWithIndex(true);
    comItem.addItem(nc.ui.ml.NCLangRes.getInstance().getStrByID(nc.vo.scm.pu.InvoiceType.m_sModuleNamePath,
        nc.vo.scm.pu.InvoiceType.INVOICE_TYPE_SPECIALID));
    comItem.addItem(nc.ui.ml.NCLangRes.getInstance().getStrByID(nc.vo.scm.pu.InvoiceType.m_sModuleNamePath,
        nc.vo.scm.pu.InvoiceType.INVOICE_TYPE_GENERALID));
    comItem.addItem(nc.ui.ml.NCLangRes.getInstance().getStrByID(nc.vo.scm.pu.InvoiceType.m_sModuleNamePath,
        nc.vo.scm.pu.InvoiceType.INVOICE_TYPE_DEFINEID));
    comItem.addItem(nc.ui.ml.NCLangRes.getInstance().getStrByID(nc.vo.scm.pu.InvoiceType.m_sModuleNamePath,
        nc.vo.scm.pu.InvoiceType.INVOICE_TYPE_VIRTUALID));
    comItem.setTranslate(true);
    //

    // 设置发票VO数组
    setInvVOs(new InvoiceVO[] {
      vo
    });
    setCurVOPos(0);

    // 设置卡片及列表的显示状态
    // setBillBrowseState(STATE_BROWSE_NORMAL) ;

    // 设置卡片界面数据
    // setVOToBillPanel() ;
    setVOToAuditedBillPanel();
    InvoiceItemVO bodyVO[] = vo.getBodyVO();
    for (int i = 0; i < bodyVO.length; i++) {
      if (bodyVO[i].getNaccumsettmny() != null && Math.abs(bodyVO[i].getNaccumsettmny().doubleValue()) > 0)
        return;
    }

    // setCurOperState(STATE_BROWSE_NORMAL);
    setButtonsStateBrowseNormal();
    // 审批
    m_btnInvBillAudit.setEnabled(false);
    m_btnInvSelectAll.setEnabled(false);
    m_btnInvDeselectAll.setEnabled(false);
    updateButtons();

  }

  public void doQueryAction(ILinkQueryData querydata) {
    // TODO 自动生成方法存根
    initi();
    String billID = querydata.getBillID();
//    String pk_corp = querydata.getPkOrg();

    // 先按照单据PK查询单据所属的公司corpvalue
    InvoiceVO vo = null;
    try {
      vo = InvoiceHelper.findByPrimaryKey(billID);
      if (vo == null) {
        MessageDialog.showHintDlg(this,
            NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000270")/* "提示" */, NCLangRes.getInstance()
                .getStrByID("SCMCOMMON", "UPPSCMCommon-000397")/* "没有符合条件的数据" */);
        return;
      }
      String strPkCorp = vo.getPk_corp();

      // 跨公司单据联查，所有按钮不可用
      if (strPkCorp != null && strPkCorp.trim().length() > 0 && getCorpPrimaryKey() != null
          && getCorpPrimaryKey().trim().length() > 0) {
        if (!strPkCorp.equals(getCorpPrimaryKey())) {// 逐级联查跨公司查询不显示任何按钮
          ButtonObject[] arrButtonObject = m_btnTree.getButtonArray();
          for (int i = 0; i < arrButtonObject.length; i++) {
            arrButtonObject[i].setVisible(false);
            updateButton(arrButtonObject[i]);
          }
        }
      }

      // v5.1数据权限：加载查询模版(注意去掉模板默认值)；查询模板中没有公司时，要设置虚拟公司
      SCMQueryConditionDlg queryDlg = new SCMQueryConditionDlg(this);
      if (queryDlg.getAllTempletDatas() == null || queryDlg.getAllTempletDatas().length <= 0)
        queryDlg.setTempletID(strPkCorp, getModuleCode(), nc.ui.pub.ClientEnvironment.getInstance()
            .getUser().getPrimaryKey(), null);

      ArrayList<String> alcorp = new ArrayList<String>();
      alcorp.add(ClientEnvironment.getInstance().getCorporation().getPrimaryKey());
      queryDlg.initCorpRef(IDataPowerForInv.CORPKEY, ClientEnvironment.getInstance().getCorporation().getPrimaryKey(),
          alcorp);
      queryDlg.setCorpRefs(IDataPowerForInv.CORPKEY, IDataPowerForInv.REFKEYS);
      //调用公共方法获取该公司中控制权限的档案条件VO数组
      ConditionVO[] voaCond = queryDlg.getDataPowerConVOs(strPkCorp,IDataPowerForInv.REFKEYS);

      InvoiceVO[] VOs = null;
      VOs = InvoiceHelper.queryInvoiceVOsByCondsMy(getClientEnvironment().getUser().getPrimaryKey(), new NormalCondVO[] {new  NormalCondVO( "公司",strPkCorp),new  NormalCondVO( "单据ID",billID)}, voaCond);//设置NormalCondVO条件：按照单据所属公司加载
      if (VOs == null || VOs.length <= 0 || VOs[0] == null) {
        MessageDialog.showHintDlg(this,
            NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000270")/* "提示" */, NCLangRes.getInstance()
                .getStrByID("common", "SCMCOMMON000000161")/* "没有察看单据的权限" */);
        return;
      }
      billID = ((InvoiceVO) VOs[0]).getPrimaryKey();// 只查一条单据
    }
    catch (Exception e) {
      // 日志异常
      nc.vo.scm.pub.SCMEnv.out(e);
      MessageDialog.showHintDlg(this,
          nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPPSCMCommon-000270")/*
                                                                                         * @res
                                                                                         * "提示"
                                                                                         */,
          nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPPSCMCommon-000428")/*
                                                                                         * @res
                                                                                         * "系统故障！"
                                                                                         */);
    }

    setCauditid(billID);

    //
    // UIComboBox comItem = (UIComboBox)
    // getInvBillPanel().getHeadItem("iinvoicetype").getComponent();
    // getInvBillPanel().getHeadItem("iinvoicetype").setWithIndex(true);
    // comItem.addItem(nc.ui.ml.NCLangRes.getInstance().getStrByID(nc.vo.scm.pu.InvoiceType.m_sModuleNamePath,nc.vo.scm.pu.InvoiceType.INVOICE_TYPE_SPECIALID
    // ));
    // comItem.addItem(nc.ui.ml.NCLangRes.getInstance().getStrByID(nc.vo.scm.pu.InvoiceType.m_sModuleNamePath,nc.vo.scm.pu.InvoiceType.INVOICE_TYPE_GENERALID
    // ));
    // comItem.addItem(nc.ui.ml.NCLangRes.getInstance().getStrByID(nc.vo.scm.pu.InvoiceType.m_sModuleNamePath,nc.vo.scm.pu.InvoiceType.INVOICE_TYPE_DEFINEID
    // ));
    // comItem.addItem(nc.ui.ml.NCLangRes.getInstance().getStrByID(nc.vo.scm.pu.InvoiceType.m_sModuleNamePath,nc.vo.scm.pu.InvoiceType.INVOICE_TYPE_VIRTUALID
    // ));
    // comItem.setTranslate(true);
    //

    // 设置发票VO数组
    setInvVOs(new InvoiceVO[] {
      vo
    });
    setCurVOPos(0);

    // 设置卡片及列表的显示状态
    // setBillBrowseState(STATE_BROWSE_NORMAL) ;

    // 设置卡片界面数据
    // setVOToBillPanel() ;
    setVOToAuditedBillPanel();
    
    // 设置表头快捷录入的显示：取第一行的值
    InvoiceVO curVO = getInvVOs()[getCurVOPos()];
    InvoiceItemVO voFirstItem = curVO.getBodyVO()[0];

    if (voFirstItem.getIdiscounttaxtype() != null && voFirstItem.getIdiscounttaxtype().toString().trim().length() > 0) {
      getBillCardPanel().getHeadItem("idiscounttaxtype").setValue(voFirstItem.getIdiscounttaxtype());
    }
    else {
      getBillCardPanel().getHeadItem("idiscounttaxtype").setValue(new Integer(1));
    }
    getBillCardPanel().getHeadItem("ntaxrate").setValue(voFirstItem.getNtaxrate());

    getBillCardPanel().setEnabled(false);
    setButtonsStateBrowseNormal();
    updateButtons();
  }

  public void doAddAction(ILinkAddData adddata) {
    // TODO 自动生成方法存根
    return;
  }

  public void doApproveAction(ILinkApproveData approvedata) {
    // TODO 自动生成方法存根
    if (approvedata == null)
      return;

    String billID = approvedata.getBillID();
    String pk_corp = approvedata.getPkOrg();

    initi();

    setCauditid(billID);

    // 没有符合条件的发票
    InvoiceVO vo = null;
    try {
      vo = (InvoiceVO) getVo();
      //
      vo.setSource(InvoiceVO.FROM_QUERY);
      //
      setInvVOs(new InvoiceVO[] {vo});
    }
    catch (Exception e) {
      SCMEnv.out(e);
      MessageDialog.showHintDlg(this,
          nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPPSCMCommon-000270")/*
                                                                                         * @res
                                                                                         * "提示"
                                                                                         */,
          nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPPSCMCommon-000428")/*
                                                                                         * @res
                                                                                         * "系统故障！"
                                                                                         */);
    }
    // //设置按钮组
    // if(m_btnAuditFlowAssist.getChildCount() == 0){
    // m_btnAuditFlowAssist.addChildButton(m_btnQueryForAudit);
    // m_btnAuditFlowAssist.addChildButton(m_btnDocManage);
    // }

    if (vo == null) {
      setInvVOs(null);
      MessageDialog.showHintDlg(this,
          nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPPSCMCommon-000270")/*
                                                                                         * @res
                                                                                         * "提示"
                                                                                         */,
          nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPP40040401-000008")/*
                                                                                         * @res
                                                                                         * "没有符合查询条件的发票！"
                                                                                         */);
      getBillCardPanel().addNew();
      // 设置按钮状态
      /* 各按钮不可用 */
      for (int i = 0; i < aryForAudit.length; i++) {
        aryForAudit[i].setEnabled(false);
        if (aryForAudit[i].getChildCount() > 0) {
          for (int j = 0; j < aryForAudit[i].getChildCount(); j++)
            aryForAudit[i].getChildButtonGroup()[j].setEnabled(false);
        }
      }
      for (int i = 0; i < aryForAudit.length; i++) {
        updateButton(aryForAudit[i]);
      }
      return;
    }

    // 设置按钮状态
    /* 各按钮可用 */
    for (int i = 0; i < aryForAudit.length; i++) {
      if (aryForAudit[i] != null) {
        aryForAudit[i].setEnabled(true);
        if (aryForAudit[i].getChildCount() > 0) {
          for (int j = 0; j < aryForAudit[i].getChildCount(); j++)
            aryForAudit[i].getChildButtonGroup()[j].setEnabled(true);
        }
      }
    }
    for (int i = 0; i < aryForAudit.length; i++) {
      updateButton(aryForAudit[i]);
    }

    // 设置发票VO数组
    setInvVOs(new InvoiceVO[] {
      vo
    });
    setCurVOPos(0);

    // 设置卡片及列表的显示状态
    setCurOperState(STATE_BROWSE_NORMAL);
    // 设置卡片界面数据
    setVOToAuditedBillPanel();
    // 设置列表界面数据，但列表界面并不显示
    setVOsToListPanel();
    getBillCardPanel().setEnabled(false);

    // V5.1 审批流按钮逻辑变化
    if (pk_corp != null && pk_corp.trim().length() > 0 && getCorpPrimaryKey() != null
        && getCorpPrimaryKey().trim().length() > 0) {
      if (pk_corp.equals(getCorpPrimaryKey())) {
        setButtons(m_btnTree.getButtonArray());// 本公司 加载单据卡片浏览按钮
       // setButtons(getBtnss());
        setButtonsStateInit();
        setButtonsStateBrowseNormal();
        updateButtons();
      }
      else {
        setButtons(aryForAudit);// 跨公司 保持原有按钮处理不变
      }
    }
    // 为了使文档管理按钮有效，需要将列表界面第一行设置为已选择
    getBillListPanel().getHeadBillModel().setRowState(0, BillModel.SELECTED);
  }

  /**
   * 作者：汪维敏 功能：(本次开票数量+累计开票数量)*订单或入库单的数量>0 参数： 返回： 例外： 创建日期：(2004-4-15 11:23:07)
   */
  private boolean negativeAndPlusCtrl(InvoiceVO billVO) throws Exception {
    InvoiceItemVO[] newItems = (InvoiceItemVO[]) billVO.getChildrenVO();

    InvoiceVO oldVO = getInvVOs()[getCurVOPos()];
    InvoiceItemVO[] oldItems = (InvoiceItemVO[]) oldVO.getChildrenVO();

    Vector vTemp = new Vector();
    String sUpSourceRowID = null;
    String sUpSourceType = null;
    for (int i = 0; i < newItems.length; i++) {
      sUpSourceRowID = newItems[i].getCupsourcebillrowid();
      if (sUpSourceRowID != null)
        vTemp.addElement(sUpSourceRowID);
      if (newItems[i].getCupsourcebilltype() != null)
        sUpSourceType = newItems[i].getCupsourcebilltype();
    }

    if (vTemp.size() == 0 || sUpSourceType == null)
      return true;

    String sUpSourceRowIDs[] = new String[vTemp.size()];
    vTemp.copyInto(sUpSourceRowIDs);
    if (!sUpSourceType.equals(ScmConst.PO_Order) && !sUpSourceType.equals(ScmConst.SC_Order)
        && !sUpSourceType.equals("45") && !sUpSourceType.equals("47"))
      return true;

    Object oTemp[][] = null;
    try {
      String beanName = IInvoiceD.class.getName();
      IInvoiceD bo = (IInvoiceD) nc.bs.framework.common.NCLocator.getInstance().lookup(beanName);
      Object o = bo.queryRelatedData(sUpSourceType, sUpSourceRowIDs);
      if (o != null) {
        oTemp = (Object[][]) o;
      }
    }
    catch (Exception e) {
      SCMEnv.out(e);
      throw e;
    }

    if (oTemp == null)
      return true;
    for (int i = 0; i < newItems.length; i++) {
      sUpSourceRowID = newItems[i].getCupsourcebillrowid();
      if (sUpSourceRowID == null || newItems[i].getStatus() == VOStatus.DELETED)
        continue;
      UFDouble dInvoiceNum = newItems[i].getNinvoicenum();
      if (dInvoiceNum == null)
        continue;
      UFDouble dOldInvoiceNum = new UFDouble(0);
      for (int j = 0; j < oldItems.length; j++) {
        if (sUpSourceRowID.equals(oldItems[j].getCupsourcebillrowid()) && oldItems[j].getCinvoice_bid() != null) {
          dOldInvoiceNum = oldItems[j].getNinvoicenum();
          break;
        }
      }
      for (int j = 0; j < sUpSourceRowIDs.length; j++) {
        if (sUpSourceRowID.equals(sUpSourceRowIDs[j])) {
          Object o1 = oTemp[j][0];
          Object o2 = oTemp[j][1];
          if (o1 != null && o2 != null) {
            UFDouble dAccumInvoiceNum = new UFDouble(o1.toString());
            UFDouble nNum = new UFDouble(o2.toString());
            if ((dInvoiceNum.doubleValue() - dOldInvoiceNum.doubleValue() + dAccumInvoiceNum.doubleValue())
                * nNum.doubleValue() < 0)
              return false;
          }
        }
      }
    }

    return true;
  }

  /**
   * 方法功能描述：设置业务员默认值 根据操作员带出业务员。
   * <p>
   * <b>examples:</b>
   * <p>
   * 使用示例
   * <p>
   * <b>参数说明</b>
   * <p>
   * 
   * @author lixiaodong
   * @time 2007-3-29 上午10:42:05
   */
  private void setDefaultValueByUser() {
    // 设置业务员默认值 根据操作员带出业务员
    UIRefPane cemployeeid = (UIRefPane) getBillCardPanel().getHeadItem("cemployeeid").getComponent();
    if (cemployeeid != null && cemployeeid.getRefPK() == null) {
      PsndocVO voPsnDoc = PuTool.getPsnByUser(cemployeeid.getRefPK(), getPk_corp());
//      IUserManageQuery qrySrv = (IUserManageQuery) NCLocator.getInstance().lookup(IUserManageQuery.class.getName());
//      PsndocVO voPsnDoc = null;
//      try {
//        voPsnDoc = qrySrv.getPsndocByUserid(nc.ui.pub.ClientEnvironment.getInstance().getCorporation().getPrimaryKey(),
//            getCurOperator());
//      }
//      catch (BusinessException be) {
//        SCMEnv.out(be);
//      }
      if (voPsnDoc != null) {
        cemployeeid.setPK(voPsnDoc.getPk_psndoc());

        UIRefPane cdeptid = (UIRefPane) getBillCardPanel().getHeadItem("cdeptid").getComponent();// 带出部门
        cdeptid.setPK(voPsnDoc.getPk_deptdoc());
      }
    }
  }

  /**
   * 送审按钮逻辑处理
   * 
   * @author czp
   * @since v50
   * @date 2006-09-23
   * @注意：本方法的约定，新增单据时调用此方法，要传递状态值为 -1
   */
  private boolean isNeedSendAudit(int iBillStatus) {

    // 审批未通过
    boolean isNeedSendToAuditQ = (iBillStatus == BillStatus.AUDITFAIL);

    // 增加
    if (iBillStatus == -1) {
      if(getSelectedRowCount()>1) return m_btnSendAudit.isEnabled();
      if (m_bizButton != null && m_bizButton.getTag() != null) {
        isNeedSendToAuditQ = BusiBillManageTool.isNeedSendToAudit("25", getClientEnvironment().getCorporation()
            .getPrimaryKey(), m_bizButton.getTag(), null, getClientEnvironment().getUser().getPrimaryKey());
      }
    }
    // 自由(修改情况)
    else if (iBillStatus == BillStatus.FREE) {
      String billid = getInvVOs()[getCurVOPos()].getHeadVO().getCinvoiceid();
      String cbizType = getInvVOs()[getCurVOPos()].getHeadVO().getCbiztype();
      //效率优化 by zhaoyha at 2009.8
      UFBoolean needSend=(UFBoolean)  getInvVOs()[getCurVOPos()].getHeadVO().getAttributeValue(ConstantVO.EXT_ATTR_ISNEEDSENDAUDIT);
      if(null==needSend && PuPubVO.getString_TrimZeroLenAsNull(cbizType) != null) {
        isNeedSendToAuditQ = BusiBillManageTool.isNeedSendToAudit("25", getClientEnvironment().getCorporation()
            .getPrimaryKey(), cbizType, billid, getClientEnvironment().getUser().getPrimaryKey());
      }else if(null!=needSend)
        isNeedSendToAuditQ=needSend.booleanValue();
    }
    //如果当前操作员与当前位置单据制单人不一致，则不允许送审 by zhaoyha at 2009.8
    if(getCurVOPos()>=0 && null!=getInvVOs() &&
        !getCurOperator().equals(getInvVOs()[getCurVOPos()].getHeadVO().getCoperator()))
      isNeedSendToAuditQ=false;
    
    m_btnSendAudit.setEnabled(isNeedSendToAuditQ);
    updateButton(m_btnSendAudit);
    //
    return isNeedSendToAuditQ;
  }

  /**
   * 金六福ERP项目:集采分收集结模式下，采购发票表头库存组织应该是采购组织对应的内部结算库存组织；表体仓库应为直运仓。
   * <p>
   * <b>examples:</b>
   * <p>
   * 使用示例
   * <p>
   * <b>参数说明</b>
   * 
   * @param vos
   *          InvoiceVO
   *          <p>
   * @author lixiaodong
   * @time 2007-8-27 上午11:00:41
   */
  private void resetStoreorgAndStordoc(InvoiceVO vos[], String strUpBillType) {
    int intLen = vos.length;

    // 过滤掉非分收集结业务类型VO
    Vector<InvoiceVO> vctIsFSJJPurVO = new Vector<InvoiceVO>();
    InvoiceVO[] aryIsFSJJPurVO = null;
    for (int j = 0; j < intLen; j++) {
      String strPurCorp = vos[j].getHeadVO().getPk_purcorp();// 采购公司
      String strArrCorp = vos[j].getHeadVO().getPk_arrvcorp();// 到货公司
      String strInvoiceCorp = vos[j].getHeadVO().getPk_corp();// 收票公司
      boolean isFSJJPur = OrderPubVO.isFSJJPur(strPurCorp, strArrCorp, strInvoiceCorp);
      if (isFSJJPur) {
        vctIsFSJJPurVO.add(vos[j]);
      }
    }

    if (vctIsFSJJPurVO == null || vctIsFSJJPurVO.size() < 1) {
      return;
    }
    else {
      aryIsFSJJPurVO = new InvoiceVO[vctIsFSJJPurVO.size()];
      vctIsFSJJPurVO.copyInto(aryIsFSJJPurVO);
    }

    // 采购发票表头库存组织替换为采购组织对应的内部结算库存组织
    Vector<String> vctStoreorganization = new Vector<String>();
    String sCstoreorganization = null;
    int IsFSJJPurVOLen = aryIsFSJJPurVO.length;

    if (BillTypeConst.STORE_PO.equalsIgnoreCase(strUpBillType)) {// 来源于库存入库单的清空表头库存组织和表体仓库
      for (int j = 0; j < IsFSJJPurVOLen; j++) {
        aryIsFSJJPurVO[j].getHeadVO().setCstoreorganization(null);
        InvoiceItemVO[] items = aryIsFSJJPurVO[j].getBodyVO();
        if (items != null) {
          int itemSize = items.length;
          for (int jj = 0; jj < itemSize; jj++) {
            items[jj].setCwarehouseid(null);
          }
        }
      }
    }
    else if (BillTypeConst.PO_ORDER.equalsIgnoreCase(strUpBillType)) {// 来源与采购订单的表头库存组织取采购组织对应的内部结算库存组织，表体仓库取库存组织对应的直运仓

      for (int j = 0; j < IsFSJJPurVOLen; j++) {
        // V502
        // 集采分收集结模式下，采购发票表头库存组织应该是采购组织对应的内部结算库存组织:从采购组织表bd_purorg中查找对应的内部结算库存组织settlestockorg，查询条件：vos.getHeadVO().getAttributeValue("cpurorganization");结果放到cstoreorganization发票表头中
        ClientCacheHelper.getColValue(new InvoiceHeaderVO[] {
          aryIsFSJJPurVO[j].getHeadVO()
        }, new String[] {
          "cstoreorganization"
        }, "bd_purorg", "pk_purorg", new String[] {
          "settlestockorg"
        }, "cpurorganization");
        sCstoreorganization = aryIsFSJJPurVO[j].getHeadVO().getCstoreorganization();
        if (PuPubVO.getString_TrimZeroLenAsNull(sCstoreorganization) != null) {
          vctStoreorganization.add(sCstoreorganization);// 库存组织ID
        }
      }
      String[] saCstoreorganization = new String[vctStoreorganization.size()];
      vctStoreorganization.copyInto(saCstoreorganization);
      Hashtable t = null;
      try {
        t = InvoiceHelper.queryStordocByStoreorg(saCstoreorganization);// 根据库存组织ID批量查找对应的直运仓ID
      }
      catch (java.lang.Exception e) {
        SCMEnv.out(e);
        MessageDialog.showHintDlg(this,
            nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPPSCMCommon-000270")/*
                                                                                           * @res
                                                                                           * "提示"
                                                                                           */,
            nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPP40040401-000014")/*
                                                                                           * @res
                                                                                           * "系统故障!"
                                                                                           */);
        return;
      }

      // 将直运仓ID赋表体仓库
      if (t != null && t.size() > 0) {
        for (int jj = 0; jj < IsFSJJPurVOLen; jj++) {
          InvoiceItemVO[] items = aryIsFSJJPurVO[jj].getBodyVO();
          if (items != null) {
            int itemSize = items.length;
            String Cstoreorganization = aryIsFSJJPurVO[jj].getHeadVO().getCstoreorganization();// 库存组织
            for (int jjj = 0; jjj < itemSize; jjj++) {
              Object oTemp = t.get(Cstoreorganization);// 采购发票表体仓库应为直运仓
              items[jjj].setCwarehouseid(oTemp == null ? null : oTemp.toString());
            }
          }
        }
      }
    }
  }

  /**
   * 设置辅计量参照
   * 
   * @param bcp
   * @param row
   *          森工NC
   */
  private void setRefPaneAssistunit(BillCardPanel bcp, int row) {
    Object cbaseid = bcp.getBillModel().getValueAt(row, "cbaseid");
    bcp.getBillModel().getValueAt(row, "cinventoryunit");

    UIRefPane ref = (UIRefPane) bcp.getBodyItem("cassistunitname").getComponent();
    String wherePart = "bd_convert.pk_invbasdoc='" + cbaseid + "' ";
    ref.setWhereString(wherePart);
    StringBuffer unionPart = new StringBuffer();
    unionPart.append(" union all \n");
    unionPart.append("(select bd_measdoc.shortname,bd_measdoc.measname,bd_invbasdoc.pk_measdoc \n");
    unionPart.append("from bd_invbasdoc \n");
    unionPart.append("left join bd_measdoc  \n");
    unionPart.append("on bd_invbasdoc.pk_measdoc=bd_measdoc.pk_measdoc \n");
    unionPart.append("where bd_invbasdoc.pk_invbasdoc='" + cbaseid + "') \n");
    ref.getRefModel().setGroupPart(unionPart.toString());
  }

  /**
   * 功能:数量、金额、换算率、辅数量可编辑性、辅计量参照内容及可编辑性
   * 
   * @since V50
   */
  public void beforeEditBodyAssistUnitNumber(BillCardPanel bcp, int iRow) {
    try {
      String strCbaseid;
      String cassistunit;
      Object oTmp;
      String ass;
      String main;
      UFDouble ufdConv;
      // 是否进行辅计量管理
      strCbaseid = (String) bcp.getBillModel().getValueAt(iRow, "cbaseid");
      // 有存货
      if (strCbaseid != null && !strCbaseid.trim().equals("")) {
        if (PuTool.isAssUnitManaged(strCbaseid)) {
          // 设置辅计量参照
          setRefPaneAssistunit(bcp, iRow);
          cassistunit = (String) bcp.getBillModel().getValueAt(iRow, "cassistunit");
          // 辅计量不为空
          oTmp = bcp.getBillModel().getValueAt(iRow, "nexchangerate");
          if (oTmp == null || oTmp.toString().trim().equals("")) {
            // 设置换算率(如果原来存在换算率则不设置，因为可能是已经改变了的非固定换算率)
            ufdConv = PuTool.getInvConvRateValue(strCbaseid, cassistunit);
            bcp.getBillModel().setValueAt(ufdConv, iRow, "nexchangerate");
          }
          // 设置可编辑性
          bcp.setCellEditable(iRow, "cassistunitname", true);
          bcp.setCellEditable(iRow, "ninvoicenum", true);
          bcp.setCellEditable(iRow, "nassistnum", true);
          bcp.setCellEditable(iRow, "nmoney", true);
          bcp.setCellEditable(iRow, "nexchangerate", true);
          // 如果辅计量是固定换算率
          if (PuTool.isFixedConvertRate(strCbaseid, cassistunit)) {
            bcp.setCellEditable(iRow, "nexchangerate", false);
          }
          else {
            bcp.setCellEditable(iRow, "nexchangerate", true);
          }
          // 如果是主辅计量相同,则换算率不可编辑
          ass = (String) bcp.getBillModel().getValueAt(iRow, "cassistunitname");
          main = (String) bcp.getBillModel().getValueAt(iRow, "cinventoryunit");
          if (ass != null && ass.equals(main)) {
            bcp.getBillModel().setValueAt(new UFDouble(1), iRow, "nexchangerate");
            bcp.setCellEditable(iRow, "nexchangerate", false);
          }
        }
        else {
          bcp.setCellEditable(iRow, "ninvoicenum", true);
          bcp.setCellEditable(iRow, "nmoney", true);
          bcp.setCellEditable(iRow, "nexchangerate", false);
          bcp.setCellEditable(iRow, "nassistnum", false);
          bcp.setCellEditable(iRow, "cassistunitname", false);
        }
      }
      // 无存货
      else {
        //bcp.setCellEditable(iRow, "ninvoicenum", false);
        bcp.setCellEditable(iRow, "nmoney", false);
        bcp.setCellEditable(iRow, "nexchangerate", false);
        bcp.setCellEditable(iRow, "nassistnum", false);
        bcp.setCellEditable(iRow, "cassistunitname", false);
      }
      //来源于订单的费用行数量字段不允许修改 For V56 by zhaoyha
     setNumEditable();
    }
    catch (Exception e) {
      Logger.debug("录入多存货时设置出错");
    }
  }

  /**
   * 编辑后事件--辅计量
   * 
   * @param bcp
   * @param e
   *          森工NC
   */
  public void afterEditWhenBodyAssist(ToftPanel uiPanel, BillCardPanel bcp, BillEditEvent e) {
    int iRow = e.getRow();

    // 存货基础档案ID
    String sBaseID = (String) bcp.getBillModel().getValueAt(iRow, "cbaseid");
    // 辅计量主键
    String sCassId = (String) bcp.getBillModel().getValueAt(iRow, "cassistunit");
    //
    if (e.getValue() == null || e.getValue().toString().trim().length() == 0) {
      bcp.getBillModel().setValueAt(null, iRow, "nassistnum");
      bcp.getBillModel().setValueAt(null, iRow, "nexchangerate");
      bcp.getBillModel().setValueAt(null, iRow, "cassistunitname");
      return;
    }
    // 获取换算率
    UFDouble nExchangeRate = PuTool.getInvConvRateValue(sBaseID, sCassId);
    // //////////////////
    UIRefPane ref = (UIRefPane) bcp.getBodyItem("cassistunitname").getComponent();
    String pk_measdoc = ref.getRefPK();
    String name = ref.getRefName();
    bcp.getBillModel().setValueAt(pk_measdoc, iRow, "cassistunit");
    bcp.getBillModel().setValueAt(name, iRow, "cassistunitname");
    sCassId = (String) bcp.getBillModel().getValueAt(iRow, "cassistunit");
    nExchangeRate = PuTool.getInvConvRateValue(sBaseID, sCassId);
    bcp.getBillModel().setValueAt(nExchangeRate, iRow, "nexchangerate");

    // 换算率改变，重新计算
    BillEditEvent tempE = new BillEditEvent(bcp.getBodyItem("nexchangerate"),
        bcp.getBodyValueAt(iRow, "nexchangerate"), "nexchangerate", iRow);
    afterEditWhenBodyRate(uiPanel, bcp, tempE);

    // 此处放开所在可编辑，在编辑前作具体控制
    bcp.setCellEditable(iRow, "ninvoicenum", true);
    bcp.setCellEditable(iRow, "noriginalcurmny", true);
    bcp.setCellEditable(iRow, "nexchangerate", true);
    bcp.setCellEditable(iRow, "nassistnum", true);
    bcp.setCellEditable(iRow, "cassistunitname", true);
  }

  /**
   * 编辑后事件--换算率
   * 
   * @param ui
   * @param bcp
   * @param e
   */
  public void afterEditWhenBodyRate(ToftPanel ui, BillCardPanel bcp, BillEditEvent e) {
    UFDouble ninvoicenum = PuPubVO.getUFDouble_NullAsZero(bcp.getBodyValueAt(e.getRow(), "ninvoicenum"));
    String sBaseID = (String) bcp.getBillModel().getValueAt(e.getRow(), "cbaseid");
    String sCassId = (String) bcp.getBillModel().getValueAt(e.getRow(), "cassistunit");
    UFDouble nExchangeRate = null;// nc.ui.pu.pub.PuTool.getInvConvRateValue(sBaseID,
                                  // sCassId);
    // 单价
    UFDouble nOriginalCurPrice = PuPubVO.getUFDouble_NullAsZero(bcp.getBodyValueAt(e.getRow(), "noriginalcurprice"));
    // 修改计量换算率,请购数量变化
    Object oTemp = bcp.getBodyValueAt(e.getRow(), "nexchangerate");
    if (oTemp != null && oTemp.toString().length() > 0)
      nExchangeRate = (UFDouble) oTemp;
    else
      nExchangeRate = null;
    if (nExchangeRate != null) {
      if (nExchangeRate.doubleValue() < 0) {
        MessageDialog.showErrorDlg(ui, nc.ui.ml.NCLangRes.getInstance().getStrByID("40040101", "UPP40040101-000040")
        /*
         * @res "计算数据"
         */, nc.ui.ml.NCLangRes.getInstance().getStrByID("40040101", "UPP40040101-000045")
        /*
         * @res "计量换算率不能为负！"
         */);
        return;
      }
      UFDouble nAssistNum = PuPubVO.getUFDouble_NullAsZero(bcp.getBodyValueAt(e.getRow(), "nassistnum"));
      if (ninvoicenum.doubleValue() != 0) {
        bcp.setBodyValueAt(ninvoicenum.div(nExchangeRate), e.getRow(), "nassistnum");
      }
      else {
        bcp.setBodyValueAt(nAssistNum.multiply(nExchangeRate), e.getRow(), "ninvoicenum");
        afterEditWhenBodyRelationsCal(new BillEditEvent(bcp.getBodyItem("ninvoicenum"), bcp.getBodyValueAt(e.getRow(),
            "ninvoicenum"), "ninvoicenum", e.getRow(), BillItem.BODY));
      }
      // 发票数量变化,金额自动变化
      // ninvoicenum = PuPubVO.getUFDouble_NullAsZero(bcp.getBodyValueAt(e
      // .getRow(), "ninvoicenum"));
      // if (nOriginalCurPrice != null) {
      // final double d = ninvoicenum.doubleValue()
      // * nOriginalCurPrice.doubleValue();
      // bcp.setBodyValueAt(new UFDouble(d), e.getRow(), "noriginalcurmny");
      // }
    }
    else {
      bcp.setBodyValueAt(null, e.getRow(), "noriginalcurmny");

    }
  }

  /**
   * 编辑后事件--辅数量
   * 
   * @param ui
   * @param bcp
   * @param e
   *          森工NC
   */
  public void afterEditWhenBodyAssNum(ToftPanel ui, BillCardPanel bcp, BillEditEvent e) {
    UFDouble ninvoicenum = PuPubVO.getUFDouble_NullAsZero(bcp.getBodyValueAt(e.getRow(), "ninvoicenum"));
    UFDouble nOriginalCurPrice = PuPubVO.getUFDouble_NullAsZero(bcp.getBodyValueAt(e.getRow(), "noriginalcurprice"));
    String sBaseID = (String) bcp.getBillModel().getValueAt(e.getRow(), "cbaseid");
    String sCassId = (String) bcp.getBillModel().getValueAt(e.getRow(), "cassistunit");
    UFDouble nExchangeRate = nc.ui.pu.pub.PuTool.getInvConvRateValue(sBaseID, sCassId);
    // 修改辅数量,发票数量变化
    Object oTemp = bcp.getBodyValueAt(e.getRow(), "nassistnum");
    UFDouble nAssistNum = null;
    if (oTemp != null && oTemp.toString().length() > 0)
      nAssistNum = (UFDouble) oTemp;
    if (nAssistNum != null) {
      if (nAssistNum.doubleValue() < 0) {
        MessageDialog.showErrorDlg(ui, nc.ui.ml.NCLangRes.getInstance().getStrByID("40040101", "UPP40040101-000040")
        /*
         * @res "计算数据"
         */, nc.ui.ml.NCLangRes.getInstance().getStrByID("40040101", "UPP40040101-000044")/*
                                                                                           * @res
                                                                                           * "辅数量不能为负！"
                                                                                           */);
        return;
      }
      // 可能是非固定换算率，所以不能用 m_nExchangeRate, 要从模板中取
      Object exc = bcp.getBillModel().getValueAt(e.getRow(), "nexchangerate");
      if (exc != null && !exc.toString().trim().equals(""))
        nExchangeRate = new UFDouble(exc.toString().trim());
      if (nExchangeRate != null) {
        final double d = nAssistNum.doubleValue() * nExchangeRate.doubleValue();
        bcp.setBodyValueAt(new UFDouble(d), e.getRow(), "ninvoicenum");
        afterEditWhenBodyRelationsCal(new BillEditEvent(bcp.getBodyItem("ninvoicenum"), bcp.getBodyValueAt(e.getRow(),
            "ninvoicenum"), "ninvoicenum", e.getRow(), BillItem.BODY));

      }
      // 发票数量变化,金额自动变化
      // ninvoicenum = (UFDouble) bcp.getBodyValueAt(e.getRow(), "ninvoicenum");
      // if (nOriginalCurPrice != null && ninvoicenum != null) {
      // final double d = ninvoicenum.doubleValue()
      // * nOriginalCurPrice.doubleValue();
      // bcp.setBodyValueAt(new UFDouble(d), e.getRow(), "noriginalcurmny");
      // }
    }
    else {
      bcp.setBodyValueAt(null, e.getRow(), "noriginalcurmny");// 金额
      bcp.setBodyValueAt(null, e.getRow(), "ninvoicenum");
      bcp.setBodyValueAt(null, e.getRow(), "noriginaltaxmny");// 税额
      bcp.setBodyValueAt(null, e.getRow(), "noriginalsummny");// 价税合计
    }
  }

  private void aftereditWhenBodyInvoicenum(ToftPanel ui, BillCardPanel bcp, BillEditEvent e) {
    // 发票数量
    UFDouble ninvoicenum = PuPubVO.getUFDouble_NullAsZero(bcp.getBodyValueAt(e.getRow(), "ninvoicenum"));
    // 存货基础档案ID
    String sBaseID = (String) bcp.getBillModel().getValueAt(e.getRow(), "cbaseid");
    // 辅计量主键
    String sCassId = (String) bcp.getBillModel().getValueAt(e.getRow(), "cassistunit");
    // 获取换算率
    UFDouble nExchangeRate = PuTool.getInvConvRateValue(sBaseID, sCassId);
    // 是否固定换算率
    boolean bFixedFlag = PuTool.isFixedConvertRate(sBaseID, sCassId);
    // 固定换算率,辅数量随发票数量变化;否则,换算率随发票数量变化
    if (bFixedFlag) {
      if (nExchangeRate != null && nExchangeRate.doubleValue() != 0.0) {
        bcp.setBodyValueAt(ninvoicenum.div(nExchangeRate), e.getRow(), "nassistnum");
      }
    }
    else {
      // 非固定换算率
      UFDouble nAssistNum = null;
      Object oTemp = bcp.getBodyValueAt(e.getRow(), "nassistnum");
      if (oTemp != null && oTemp.toString().length() > 0)
        nAssistNum = (UFDouble) oTemp;
      if (nAssistNum != null) {
        if (nAssistNum.doubleValue() != 0.0) {
          bcp.setBodyValueAt(ninvoicenum.div(nAssistNum), e.getRow(), "nexchangerate");
        }
        else {
          // 辅数量为0,如果修改换算率,会出现主数量/换算率!=0,而辅数量为0的矛盾
          // 为此,修改辅数量,不改变换算率
          if (nExchangeRate != null && nExchangeRate.doubleValue() != 0.0) {
            bcp.setBodyValueAt(ninvoicenum.div(nExchangeRate), e.getRow(), "nassistnum");
          }
        }
      }
      else {
        Object objTmp = bcp.getBodyValueAt(e.getRow(), "nexchangerate");
        if (objTmp != null && !objTmp.toString().trim().equals("")) {
          nExchangeRate = new UFDouble(objTmp.toString());
          bcp.setBodyValueAt(ninvoicenum.div(nExchangeRate), e.getRow(), "nassistnum");
        }
        else {
          bcp.setBodyValueAt(null, e.getRow(), "nassistnum");
          bcp.setBodyValueAt(null, e.getRow(), "nexchangerate");
        }
      }
    }
  }

  /**
   * 加载"参照增行"的子按钮
   * 
   * 目前参照增行只支持来源单据为采购订单和采购入库单
   * 
   */
  private void createAddContinueBtn() {
    // 为参照增行增加子按钮
    String strBizType = getBillCardPanel().getHeadItem("cbiztype").getValue();
    btnBillAddContinue.setEnabled(false);
    if (PuPubVO.getString_TrimZeroLenAsNull(strBizType) == null) {
      return;
    }
    InvoiceVO[] contiVos=getInvVOsByContinue();
    if (contiVos == null || contiVos.length < 1)   return;
    String sUpsourcebilltype = PuPubVO.getString_TrimZeroLenAsNull(getBillCardPanel().getBodyValueAt(0, "cupsourcebilltype"));
    if (!BillTypeConst.PO_ORDER.equalsIgnoreCase(sUpsourcebilltype) && !BillTypeConst.STORE_PO.equalsIgnoreCase(sUpsourcebilltype)) {
      return;
    }
    ButtonObject bo = new ButtonObject("btncontinue");
    bo.setTag(strBizType);
    PfUtilClient.retAddBtn(btnBillAddContinue, nc.ui.pub.ClientEnvironment.getInstance().getCorporation()
        .getPrimaryKey(), nc.vo.scm.pu.BillTypeConst.PO_INVOICE, bo);
    Vector vecBtn = btnBillAddContinue.getChildren();
    if (vecBtn.size() > 0) {
      int intLen = vecBtn.size();
      for (int i = 0; i < intLen; i++) {
        ButtonObject btn = (ButtonObject) vecBtn.get(i);
        if ("自制单据".equalsIgnoreCase(btn.getName()) || "Self-prepared document".equalsIgnoreCase(btn.getName()) || !sContinueBillTypeName.equalsIgnoreCase(btn.getName())) {
          // btnBillAddContinue.removeChildButton(btn);
          btn.setVisible(false);
        }
        //For V56 by zhaoyha
        if (BillTypeConst.PO_ORDER.equalsIgnoreCase(sUpsourcebilltype) ||
            isRefFeeOrder(strBizType).booleanValue()) {
          if (BillTypeConst.getBillTypeName(BillTypeConst.PO_ORDER).equalsIgnoreCase(btn.getName())) {
            btn.setVisible(true);
          }
        }
        //For V56 by zhaoyha
        if (BillTypeConst.STORE_PO.equalsIgnoreCase(sUpsourcebilltype) ||
            isRefFeeOrder(strBizType).booleanValue()) {
          if (BillTypeConst.getBillTypeName(BillTypeConst.STORE_PO).equalsIgnoreCase(btn.getName())) {
            btn.setVisible(true);
          }
        }

      }
      for (int i = 0; i < intLen; i++) {
        if(((ButtonObject) vecBtn.get(i)).isVisible()) {
          btnBillAddContinue.setEnabled(true);
          break;
        }
      }
    }
    
   setButtons(m_btnTree.getButtonArray());
//    setButtons(getBtnss());
  }

  private void setButtonsBAPFlag() {
    if (getCurOperState() == STATE_EDIT || getCurOperState() == STATE_LIST_FROM_BILLS) {
      m_btnCrtAPBill.setEnabled(false);
      m_btnDelAPBill.setEnabled(false);
      return;
    }
    if (getInvVOs() == null || getInvVOs()[getCurVOPos()] == null
        || BillStatus.AUDITED.compareTo(getInvVOs()[getCurVOPos()].getHeadVO().getIbillstatus()) != 0) {
      m_btnCrtAPBill.setEnabled(false);
      m_btnDelAPBill.setEnabled(false);
      return;
    }
    // 虚拟发票，传应付与取消传应付均不可用
    Integer iInvoiceType = getInvVOs()[getCurVOPos()].getHeadVO().getIinvoicetype();
    boolean bVirtFlag = (iInvoiceType != null && iInvoiceType.intValue() == 3);
    //
    UFBoolean bapflag = getInvVOs()[getCurVOPos()].getHeadVO().getBapflag();
    if (PuPubVO.getUFBoolean_NullAs(bapflag, UFBoolean.FALSE).booleanValue()) {
      m_btnCrtAPBill.setEnabled(false);
      m_btnDelAPBill.setEnabled(!bVirtFlag);
    }
    else {
      m_btnCrtAPBill.setEnabled(!bVirtFlag);
      m_btnDelAPBill.setEnabled(false);
    }
    /*
     * 弃审: 要确保不出现传过应付，但还能弃审成功的情况 如果业务类型配置了审批驱动，则应付是由审批传递的，此时允许使用弃审功能
     * 但是否能完成取消传应付功能，要看弃审组件是否驱动取消传应付动作
     */
    // 传过应付: 审批通过+(流程配置完整{审批传应付且弃审取消应付}||虚拟发票)
    if (PuPubVO.getUFBoolean_NullAs(bapflag, UFBoolean.FALSE).booleanValue()) {
      m_btnInvBillUnAudit.setEnabled(BillStatus.AUDITED.compareTo(getInvVOs()[getCurVOPos()].getHeadVO()
          .getIbillstatus()) == 0
          && (isBusitypeAppriveDrive(getInvVOs()[getCurVOPos()].getHeadVO().getCbiztype()) || bVirtFlag));
    }
    // 未传应付：审批通过,流程配置与否不必关心
    else {
      m_btnInvBillUnAudit.setEnabled(BillStatus.AUDITED.compareTo(getInvVOs()[getCurVOPos()].getHeadVO()
          .getIbillstatus()) == 0);
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
   * <p>
   * 
   * @author lixiaodong
   * @time 2008-8-15 上午10:04:12
   */
  private void onCrtAPBill() {
    if (getInvVOs() == null) {
      return;
    }
    Vector vctKey = new Vector();
    Vector vctInvoice = new Vector();
    if (getCurPanelMode() == INV_PANEL_CARD) {
      vctKey.add(getInvVOs()[getCurVOPos()].getHeadVO().getPrimaryKey());
      vctInvoice.add(getInvVOs()[getCurVOPos()]);
    }
    else if (getCurPanelMode() == INV_PANEL_LIST) {
      ArrayList list = getSelectedBills();
      if (list == null || list.size() < 1) {
        return;
      }
      InvoiceVO inVO[] = new InvoiceVO[list.size()];
      list.toArray(inVO);
      for (int i = 0; i < inVO.length; i++) {
        if (!PuPubVO.getUFBoolean_NullAs(inVO[i].getHeadVO().getBapflag(), UFBoolean.FALSE).booleanValue()) {
          vctKey.add(inVO[i].getHeadVO().getPrimaryKey());
          vctInvoice.add(inVO[i]);
        }
      }
    }

    if (vctKey == null || vctKey.size() < 1) {
      return;
    }
    String[] key = new String[vctKey.size()];
    vctKey.copyInto(key);

    InvoiceVO[] voaInvoice = new InvoiceVO[vctInvoice.size()];
    vctInvoice.copyInto(voaInvoice);
    String[] cinvoiceid = null;
    Object oaUserOne = "MANUAL";
    Object[] oaUser = new Object[voaInvoice.length];
    for (int i = 0; i < oaUser.length; i++) {
      oaUser[i] = oaUserOne;
    }
    try {
      voaInvoice[0].getHeadVO().setCoperatoridnow(ClientEnvironment.getInstance().getUser().getPrimaryKey());
      PfUtilClient.processBatch(this, "CRTAPBILL", nc.vo.scm.pu.BillTypeConst.PO_INVOICE, ClientEnvironment
          .getInstance().getDate().toString(), voaInvoice, oaUser);
      boolean bSucced = PfUtilClient.isSuccess();
      if (bSucced) {
        cinvoiceid = new String[voaInvoice.length];
        for (int i = 0; i < voaInvoice.length; i++) {
          cinvoiceid[i] = voaInvoice[i].getHeadVO().getPrimaryKey();
        }
      }
    }
    catch (Exception e) {
      MessageDialog.showErrorDlg(this,
          nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPPSCMCommon-000270")/*
                                                                                         * @res
                                                                                         * "提示"
                                                                                         */, e.getMessage());
    }

    if (cinvoiceid != null && cinvoiceid.length > 0) {
      Hashtable<String, InvoiceVO> resultH = new Hashtable<String, InvoiceVO>();
      try {
        resultH = InvoiceHelper.findByPrimaryKeyBantch(vctKey);
      }
      catch (Exception e) {
        SCMEnv.out(e);
        MessageDialog.showErrorDlg(this,
            nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPPSCMCommon-000270")/*
                                                                                           * @res
                                                                                           * "提示"
                                                                                           */, e
                .getMessage());
      }
      if (resultH != null) {
        for (int i = 0; i < getInvVOs().length; i++) {
          if (resultH.containsKey(getInvVOs()[i].getHeadVO().getPrimaryKey())) {
            getInvVOs()[i] = resultH.get(getInvVOs()[i].getHeadVO().getPrimaryKey());
          }
        }
      }

      // 设置卡片界面数据
      setVOToBillPanel();

      setButtonsBAPFlag();
      showHintMessage("操作成功");
    }
    else {
      showHintMessage("不能传应付");
    }

  }

  /**
   * 方法功能描述：取消传应付。
   * <p>
   * <b>examples:</b>
   * <p>
   * 使用示例
   * <p>
   * <b>参数说明</b>
   * <p>
   * 
   * @author lixiaodong
   * @throws BusinessException
   * @time 2008-8-15 上午10:04:15
   */
  private void onDelAPBill() {
    if (getInvVOs() == null) {
      return;
    }
    String[] key = null;
    Vector vctKey = new Vector();
    Vector vctInvoice = new Vector();

    if (getCurPanelMode() == INV_PANEL_CARD) {
      vctKey.add(getInvVOs()[getCurVOPos()].getHeadVO().getPrimaryKey());
      vctInvoice.add(getInvVOs()[getCurVOPos()]);
    }
    else if (getCurPanelMode() == INV_PANEL_LIST) {
      ArrayList list = getSelectedBills();
      if (list == null || list.size() < 1) {
        return;
      }
      InvoiceVO inVO[] = new InvoiceVO[list.size()];
      list.toArray(inVO);
      key = new String[list.size()];
      for (int i = 0; i < inVO.length; i++) {
        if (PuPubVO.getUFBoolean_NullAs(inVO[i].getHeadVO().getBapflag(), UFBoolean.FALSE).booleanValue()) {
          vctKey.add(inVO[i].getHeadVO().getPrimaryKey());
          vctInvoice.add(inVO[i]);
        }
      }
    }

    if (vctKey == null || vctKey.size() < 1) {
      return;
    }

    key = new String[vctKey.size()];
    vctKey.copyInto(key);

    InvoiceVO[] voaInvoice = new InvoiceVO[vctInvoice.size()];
    vctInvoice.copyInto(voaInvoice);
    String[] cinvoiceid = null;
    Object oaUserOne = "MANUAL";
    Object[] oaUser = new Object[voaInvoice.length];
    for (int i = 0; i < oaUser.length; i++) {
      oaUser[i] = oaUserOne;
    }
    try {
      Object o = PfUtilClient.processBatch(this, "DELAPBILL", nc.vo.scm.pu.BillTypeConst.PO_INVOICE, ClientEnvironment
          .getInstance().getDate().toString(), voaInvoice, oaUser);
      boolean bSucced = PfUtilClient.isSuccess();
      if (bSucced) {
        cinvoiceid = new String[voaInvoice.length];
        for (int i = 0; i < voaInvoice.length; i++) {
          cinvoiceid[i] = voaInvoice[i].getHeadVO().getPrimaryKey();
        }
      }
    }
    catch (Exception e) {
      MessageDialog.showErrorDlg(this,
          nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPPSCMCommon-000270")/*
                                                                                         * @res
                                                                                         * "提示"
                                                                                         */, e.getMessage());
      showHintMessage(e.getMessage());
      return;
    }

    Hashtable<String, InvoiceVO> resultH = new Hashtable<String, InvoiceVO>();
    try {
      resultH = InvoiceHelper.findByPrimaryKeyBantch(vctKey);
    }
    catch (Exception e) {
      SCMEnv.out(e);
    }
    if (resultH != null) {
      for (int i = 0; i < getInvVOs().length; i++) {
        if (resultH.containsKey(getInvVOs()[i].getHeadVO().getPrimaryKey())) {
          getInvVOs()[i] = resultH.get(getInvVOs()[i].getHeadVO().getPrimaryKey());
        }
      }

      if (getCurPanelMode() == INV_PANEL_CARD) {
        // 设置卡片界面数据
        setVOToBillPanel();
      }else{
        // 设置卡片界面数据
        setVOsToListPanel();
      }
      setButtonsBAPFlag();
      showHintMessage("操作成功");
    }
    else {
      showHintMessage("不能取消传应付");
    }
  }

  public boolean onEditAction(int action) {
    if (action == BillScrollPane.ADDLINE) {
      getBillCardPanel().getBillModel().addLine();
      // 设置行号
      nc.ui.scm.pub.report.BillRowNo.addLineRowNo(getBillCardPanel(), nc.vo.scm.pu.BillTypeConst.PO_INVOICE, "crowno");
      int iNewLine = getBillCardPanel().getBillModel().getRowCount() - 1;
      // 设置新增行的默认值
      setNewLineDefaultValue(iNewLine);
      setDefaultBody(iNewLine);
      return false;
    }
    return true;
  }

  /**
   * 作者：王印芬 功能：存货、订单日期、币种修改后相应的最高限价变化 该函数由afterEdit存货、订单日期、币种改变后调用。 参数： int
   * iBeginRow 需计算合同相关信息的表体开始行 int iEndRow 需计算合同相关信息的表体结束行 返回：无 例外：无
   * 日期：(2002-3-13 11:39:21) 修改日期，修改人，修改原因，注释标志： 2003-11-14 wyf 批量加载
   */
  private void setRelated_AssistUnit(int iBeginRow, int iEndRow) {

    // 批量加载
    String[] saBaseId = (String[]) PuGetUIValueTool.getArrayNotNull(getBillCardPanel().getBillModel(), "cbaseid",
        String.class, iBeginRow, iEndRow);
    PuTool.loadBatchAssistManaged(saBaseId);

    // 辅计量的行
    Vector vecAssistUnitIndex = new Vector();
    Vector vecBaseId = new Vector();
    Vector vecAssistId = new Vector();

    // 计算值

    // 设置默认辅计量
    String[] aryAssistunit = new String[] {
        // "<formulaset><cachetype>FOREDBCACHE</cachetype></formulaset>" +
        "cassistunit->getColValue(bd_invbasdoc,pk_measdoc2,pk_invbasdoc,cbaseid)",
        "cassistunitname->getColValue(bd_measdoc,measname,pk_measdoc,cassistunit)"
    };
    getBillCardPanel().getBillModel().execFormulas(aryAssistunit, iBeginRow, iEndRow);
    //
    String sBaseId = null;
    String sAssistUnit = null;
    for (int iRow = iBeginRow; iRow <= iEndRow; iRow++) {
      sBaseId = (String) getBillCardPanel().getBodyValueAt(iRow, "cbaseid");
      if (PuTool.isAssUnitManaged(sBaseId)) {
        sAssistUnit = (String) getBillCardPanel().getBodyValueAt(iRow, "cassistunit");
        // 为批量加载作准备
        if (PuPubVO.getString_TrimZeroLenAsNull(sAssistUnit) != null) {
          vecAssistUnitIndex.add(new Integer(iRow));
          vecBaseId.add(sBaseId);
          vecAssistId.add(sAssistUnit);
        }
      }
      else {
        getBillCardPanel().getBillModel().setValueAt(null, iRow, "cassistunitname");
        getBillCardPanel().getBillModel().setValueAt(null, iRow, "cassistunit");
        getBillCardPanel().getBillModel().setValueAt(null, iRow, "nassistnum");
        getBillCardPanel().getBillModel().setValueAt(null, iRow, "nexchangerate");
      }
    }

    // 批量设置辅计量的行
    int iAssistUnitLen = vecAssistUnitIndex.size();
    if (iAssistUnitLen > 0) {

      // 批量加载
      PuTool.loadBatchInvConvRateInfo((String[]) vecBaseId.toArray(new String[iAssistUnitLen]), (String[]) vecAssistId
          .toArray(new String[iAssistUnitLen]));

      // String[] saCurrId =
      // getAllCurrIdFromCard(getRowCount(),getCorp(),getHeadItem("ccurrencytypeid").getValue(),getBillModel());
      // HashMap mapRateMny =
      // m_cardPoPubSetUI2.getMoneyDigitByCurr_Busi(saCurrId);
      // BusinessCurrencyRateUtil bca =
      // m_cardPoPubSetUI2.getCurrArith_Busi(getCorp());

      // 循环执行
      int iRow = 0;
      for (int i = 0; i < iAssistUnitLen; i++) {
        iRow = ((Integer) vecAssistUnitIndex.get(i)).intValue();

        Object[] oConvRate = PuTool.getInvConvRateInfo((String) vecBaseId.get(i), (String) vecAssistId.get(i));
        if (oConvRate == null) {
          getBillCardPanel().getBillModel().setValueAt(null, iRow, "nexchangerate");
        }
        else {
          getBillCardPanel().getBillModel().setValueAt((UFDouble) oConvRate[0], iRow, "nexchangerate");
        }

        // 换算率改变，重新计算
        BillEditEvent tempE = new BillEditEvent(getBillCardPanel().getBodyItem("nexchangerate"), getBillCardPanel()
            .getBodyValueAt(iRow, "nexchangerate"), "nexchangerate", iRow);
        afterEditInvBillRelations(tempE);
      }
    }

    // 设置可编辑性
    // setEnabled_BodyAssistUnitRelated(iBeginRow,iEndRow) ;

  }

  /**
   * 根据业务类型主键，返回是否配置了审批驱动传应付 + 弃审驱动取消传应付
   * <p>
   * <b>examples:</b>
   * <p>
   * 使用示例
   * <p>
   * <b>参数说明</b>
   * 
   * @param strBiztypeId
   * @return
   *          <p>
   * @author czp
   * @time 2008-10-8 下午12:42:38
   */
  protected boolean isBusitypeAppriveDrive(String strBiztypeId) {
    if (strBiztypeId == null)
      return false;
    if (m_mapBusitypeAppriveDrive == null) {
      m_mapBusitypeAppriveDrive = new HashMap<String, UFBoolean>();
    }
    if (m_mapBusitypeAppriveDrive.containsKey(strBiztypeId)) {
      return m_mapBusitypeAppriveDrive.get(strBiztypeId).booleanValue();
    }
    IPFMetaModel srvPFMetaModel = (IPFMetaModel) NCLocator.getInstance().lookup(IPFMetaModel.class.getName());
    try {
      MessagedriveVO[] driveVOs = srvPFMetaModel.queryAllMsgdrvVOs(getCorpPrimaryKey(), BillTypeConst.PO_INVOICE,
          strBiztypeId, "APPROVE");
      if (driveVOs == null || driveVOs.length == 0) {
        m_mapBusitypeAppriveDrive.put(strBiztypeId, UFBoolean.FALSE);
      }
      else {
        driveVOs = srvPFMetaModel.queryAllMsgdrvVOs(getCorpPrimaryKey(), BillTypeConst.PO_INVOICE, strBiztypeId,
            "UNAPPROVE");
        if (driveVOs == null || driveVOs.length == 0) {
          m_mapBusitypeAppriveDrive.put(strBiztypeId, UFBoolean.FALSE);
        }
        else {
          m_mapBusitypeAppriveDrive.put(strBiztypeId, UFBoolean.TRUE);
        }
      }
    }
    catch (BusinessException e) {
      showHintMessage(e.getMessage());
      return false;
    }
    return m_mapBusitypeAppriveDrive.get(strBiztypeId).booleanValue();
  }
  
  /**
   * 
   * 方法功能描述：得到当前卡片上的发票VO。
   * <p>
   * <b>examples:</b>
   * <p>
   * 使用示例
   * <p>
   * <b>参数说明</b>
   * @return
   * <p>
   * @author zhaoyha
   * @time 2009-1-19 上午11:45:34
   */
  private InvoiceVO getCurVOonCard(){
    return (InvoiceVO)getBillCardPanel().getBillValueVO(InvoiceVO.class.getName(), 
        InvoiceHeaderVO.class.getName(), InvoiceItemVO.class.getName());
  }
  
  /**
   * 
   * 方法功能描述：简要描述本方法的功能。
   * <p>
   * <p>二次开发插件支持 by zhaoyha at 2009.1.19
   * <p>
   * <b>examples:</b>
   * <p>
   * 使用示例
   * <p>
   * <b>参数说明</b>
   * @return
   * <p>
   * @author zhyhang
   * @time 2009-1-19 下午08:19:18
   */
  public PIPluginUI getPluginUI() {
    if(pluginui==null)
      pluginui = new PIPluginUI(this);
    return pluginui;
  }
  
  /**
   * 
   * 方法功能描述：简要描述本方法的功能。
   * <p>
   * <p>二次开发插件支持 by zhaoyha at 2009.1.19
   * <p>
   * <b>examples:</b>
   * <p>
   * 使用示例
   * <p>
   * <b>参数说明</b>
   * @return
   * <p>
   * @author zhyhang
   * @time 2009-1-19 下午08:14:18
   */
  public InvokeEventProxy getInvokeEventProxy() {
    if(invokeEventProxy == null){
      invokeEventProxy = new InvokeEventProxy("pu",BillTypeConst.PO_INVOICE, getPluginUI());
    }
    return invokeEventProxy;
  }
  
  public void clearAuditInfo(InvoiceVO vo,BillCardPanel bcp){
    if(vo!=null && vo.getHeadVO()!=null){
      vo.getHeadVO().setCauditpsn(null);
      vo.getHeadVO().setDauditdate(null);
      vo.getHeadVO().setTaudittime(null);
    }
    if(bcp!=null){
      bcp.getTailItem("cauditpsn").setValue(null);
      bcp.getTailItem("dauditdate").setValue(null);
      bcp.getTailItem("taudittime").setValue(null);
    }
  }
 
  /**
   * 
   * 方法功能描述：得到当前的发票表体VO数组。
   * <p>
   * <b>examples:</b>
   * <p>
   * 使用示例
   * <p>
   * <b>参数说明</b>
   * @return
   * <p>
   * @author zhaoyha
   * @time 2009-3-18 下午02:15:36
   */
  public Object[] getCurrBodyVOs(){
    if(getInvVOs()!=null && getInvVOs().length>0)
      return getInvVOs()[getCurVOPos()].getBodyVO();
    else
      return null;   
  }
  
  /**
   * 
   * 方法功能描述：业务类型是否，发票有订单和入库单两个来源，即发票只能从订单参照费用行
   * <p>For V56
   * <p> 
   * <b>examples:</b>
   * <p>
   * 使用示例
   * <p>
   * <b>参数说明</b>
   * @return
   * <p>
   * @author zhaoyha
   * @time 2009-3-23 上午09:44:55
   */
  private UFBoolean isRefFeeOrder(){ 
    String cbiztype=(String) getBillCardPanel().getHeadItem("cbiztype").getValueObject();
    if(!StringUtil.isEmptyWithTrim(cbiztype)) return isRefFeeOrder(cbiztype);
    return UFBoolean.FALSE;
  }
  
  /**
   * 方法功能描述：业务类型是否，发票有订单和入库单两个来源，即发票只能从订单参照费用行
   * @param bizType 
   * @return UFBoolean
   */
  private UFBoolean isRefFeeOrder(String bizType){ 
    if(!onlyRefFeeOrder.containsKey(bizType))
      onlyRefFeeOrder.put(bizType, new UFBoolean(InvoiceHelper.isPIOnlyRefFeeOrder(bizType)));
    return onlyRefFeeOrder.get(bizType);
  }
  
  /**
   * 
   * 方法功能描述：符合业务类型要求的来源于订单的费用行数量字段不允许修改。
   * <p>For V56
   * <p>
   * <b>examples:</b>
   * <p>
   * 使用示例
   * <p>
   * <b>参数说明</b>
   * <p>
   * @author zhaoyha
   * @time 2009-3-23 上午10:27:10
   */
  private void setNumEditable(){
    if(!isRefFeeOrder().booleanValue()) return;
    int iCount = getBillCardPanel().getRowCount();
    for (int i = 0; i < iCount; i++) {
      if(ScmConst.PO_Order.equals(getBillCardPanel().getBodyValueAt(i, "cupsourcebilltype")) &&
          PuTool.isFee(PuPubVO.getString_TrimZeroLenAsNull(getBillCardPanel().getBodyValueAt(i, "cbaseid")))){
        getBillCardPanel().setCellEditable(i, "ninvoicenum", false);
        getBillCardPanel().setCellEditable(i, "nassistnum", false);
      }
    }
  }
  
  /**
   * 
   * 方法功能描述：避免自制虚拟发票。
   * <p>
   * <b>examples:</b>
   * <p>
   * 使用示例
   * <p>
   * <b>参数说明</b>
   * @param e
   * <p>
   * @author zhaoyha
   * @time 2009-7-17 上午10:26:06
   */
  private void afterEditInvoicetype(BillEditEvent e){
    UIComboBox comItem = (UIComboBox) getBillCardPanel().getHeadItem("iinvoicetype").getComponent();
    //如果是选的是虚拟发票，则改回原发票类型
    if(comItem.getSelectedIndex()==3){
      showErrorMessage(NCLangRes.getInstance().getStrByID("40040401",
          "UPP40040401-000273")/*"不能自制虚拟发票！"*/);
      comItem.setSelectedIndex(curInvType);
    }
    else
      setCurInvoiceType();
  }
 
  /**
   * 
   * 方法功能描述：保存当前发票类型。
   * <p>
   * <b>examples:</b>
   * <p>
   * 使用示例
   * <p>
   * <b>参数说明</b>
   * <p>
   * @author zhaoyha
   * @time 2009-7-17 上午10:25:52
   */
  private void setCurInvoiceType(){
    UIComboBox comItem = (UIComboBox) getBillCardPanel().getHeadItem("iinvoicetype").getComponent();
    curInvType=comItem.getSelectedIndex(); 
  }
 
  
  /**
   * 
   * 方法功能描述：列表下批送审。
   * <p>
   * <b>examples:</b>
   * <p>
   * 使用示例
   * <p>
   * <b>参数说明</b>
   * <p>
   * @author zhaoyha
   * @time 2009-8-20 下午03:26:21
   */
  private void onSendAuditBatch(){
    InvoiceVO[] vos=getSendAuditVos();
    if(0==vos.length) return;
    try {
      //返回真正的信息放在数组第n个开始位置(n=vos.length)
      Object[] retValues=PfUtilClient.runBatch(this, "SAVE", ScmConst.PO_Invoice, ClientEnvironment.getInstance().getDate().toString(),
          vos, null, null, null);
      if(PfUtilClient.isSuccess()){
        //更新缓存及UI
        updateCacheVo((Map<String, Map<String, Object>>) retValues[vos.length]);
        updateListUI((Map<String, Map<String, Object>>) retValues[vos.length]);
        showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UCH023"));
        return;
      }
    }
    catch (Exception e) {
      //日志异常
      SCMEnv.out(e);
      MessageDialog.showErrorDlg(this, null, e.getMessage());
    }
    showHintMessage(NCLangRes.getInstance().getStrByID("40040401", "UPP40040401-000257")
        /*
         * @res
         * "送审未成功！"
         */);
  }
  
  private InvoiceVO[] getSendAuditVos(){
    List<InvoiceVO> selectedVos=new ArrayList<InvoiceVO>();
    for(int row:getBillListPanel().getHeadTable().getSelectedRows()){
      int status=(Integer) getBillListPanel().getHeadBillModel().getValueAt(row, "ibillstatus");
      String billMaker=(String) getBillListPanel().getHeadBillModel().getValueAt(row, "coperator");
      if((BillStatus.FREE.intValue()==status || BillStatus.AUDITFAIL.intValue()==status) && getCurOperator().equals(billMaker)){
        InvoiceVO vo=new InvoiceVO();
        vo.setParentVO(new InvoiceHeaderVO());
        setHeadInfoFrmList(vo.getHeadVO(),row);
        selectedVos.add(vo);
      }
    }
    return selectedVos.toArray(new InvoiceVO[selectedVos.size()]);
  }
  
  /**
   * 
   * 方法功能描述：设置表头VO要传到流程平台的必要信息。
   * <p>
   * <b>examples:</b>
   * <p>
   * 使用示例
   * <p>
   * <b>参数说明</b>
   * @param head
   * @param row
   * <p>
   * @author zhaoyha
   * @time 2009-8-20 上午10:50:33
   */
  private void setHeadInfoFrmList(InvoiceHeaderVO head,int row){
    head.setCinvoiceid((String) getBillListPanel().getHeadBillModel().getValueAt(row, "cinvoiceid"));
    head.setVinvoicecode((String) getBillListPanel().getHeadBillModel().getValueAt(row, "vinvoicecode"));
    head.setIbillstatus((Integer) getBillListPanel().getHeadBillModel().getValueAt(row, "ibillstatus"));
    head.setCoperator((String) getBillListPanel().getHeadBillModel().getValueAt(row, "coperator"));
    head.setPk_corp((String) getBillListPanel().getHeadBillModel().getValueAt(row, "pk_corp"));
    head.setCbiztype((String) getBillListPanel().getHeadBillModel().getValueAt(row, "cbiztype"));
  }
  
  /**
   * 
   * 方法功能描述：更新缓存VO信息。
   * <p>
   * <b>examples:</b>
   * <p>
   * 使用示例
   * <p>
   * <b>参数说明</b>
   * @param newInfos
   * <p>
   * @author zhaoyha
   * @time 2009-8-20 上午10:51:10
   */
  private void updateCacheVo(Map<String,Map<String,Object>> newInfos){
    Map<String, InvoiceVO> cacheVos = getCacheVoMap();
    for(String id:newInfos.keySet()){
      Map<String,Object> info=newInfos.get(id);
      //更新缓存VO
      InvoiceVO vo=cacheVos.get(id);
      if(null!=vo)
        for(String column:info.keySet())
          vo.getHeadVO().setAttributeValue(column, info.get(column));
    }
  }

  /**
   * 方法功能描述：更新列表界面的信息。
   * <p>
   * <b>examples:</b>
   * <p>
   * 使用示例
   * <p>
   * <b>参数说明</b>
   * @param newInfos
   * @param cacheVos
   * @param billsOnList
   * <p>
   * @author zhaoyha
   * @time 2009-8-20 上午10:53:51
   */
  private void updateListUI(Map<String, Map<String, Object>> newInfos) {
    Map<String,Integer> billsOnList=getBillListIndexMap();
    for(String id:newInfos.keySet()){
      Map<String,Object> info=newInfos.get(id);
      //更新列表界面
      Integer row=billsOnList.get(id);
      if(null!=row)
        for(String column:info.keySet())
          getBillListPanel().getHeadBillModel().setValueAt(info.get(column), row, column);
    }
  }

  /**
   * 方法功能描述：得到的列表发票主键与列表行的Map。
   * <p>
   * <b>examples:</b>
   * <p>
   * 使用示例
   * <p>
   * <b>参数说明</b>
   * @return
   * <p>
   * @author zhaoyha
   * @time 2009-8-20 上午10:43:23
   */
  private Map<String, Integer> getBillListIndexMap() {
    //建立列表VO的map
    Map<String,Integer> billsOnList=new HashMap<String, Integer>();
    for (int i = 0; i < getBillListPanel().getHeadBillModel().getRowCount(); i++)
      billsOnList.put((String) getBillListPanel().getHeadBillModel().getValueAt(i, "cinvoiceid"), i);
    return billsOnList;
  }

  /**
   * 方法功能描述：得到的发票VO主键与VO的Map。
   * <p>
   * <b>examples:</b>
   * <p>
   * 使用示例
   * <p>
   * <b>参数说明</b>
   * @return
   * <p>
   * @author zhaoyha
   * @time 2009-8-20 上午10:42:05
   */
  private Map<String, InvoiceVO> getCacheVoMap() {
    //建立缓存VO的map
    Map<String,InvoiceVO> cacheVos=new HashMap<String, InvoiceVO>();
    for(InvoiceVO vo:getInvVOs())
      cacheVos.put(vo.getHeadVO().getCinvoiceid(), vo);
    return cacheVos;
  }

  /**
   * 方法功能描述：得到列表当前选中行(注意有时为了处理getCurVOPos()可能与此不一致，
   * 但本方法始终返回列表当前选中行，多选时即为鼠标下的行。
   * <p>
   * <b>examples:</b>
   * <p>
   * 使用示例
   * <p>
   * <b>参数说明</b>
   * @return
   * <p>
   * @author zhaoyha
   * @time 2009-8-20 下午02:58:17
   */
  private int getSelectedRowOnList() {
    // 得到选中的行
    int row = -1;
    for (int i = 0; i < getInvVOs().length; i++) {
      if (getBillListPanel().getHeadBillModel().getRowState(i) == BillModel.SELECTED) {
        row = i;
        break;
      }
    }
    return row;
  }

  /**
   * @return billTempletVo
   */
  public BillTempletVO getBillTempletVo() {
    if(null == billTempletVo)
      billTempletVo=BillUIUtil.getDefaultTempletStatic(getModuleCode(), 
          null, getCurOperator(), getCorpId(), null, null);
    return billTempletVo;
  }
  
  /**
   * 
   * 方法功能描述：判断是否需要加载来源和源头单据信息。
   * <p>
   * <b>examples:</b>
   * <p>
   * 使用示例
   * <p>
   * <b>参数说明</b>
   * @param model
   * @return
   * <p>
   * @author zhaoyha
   * @time 2009-8-24 上午09:54:22
   */
  private boolean isNeedLoadSrcInfo(BillModel model){
    String[] checkItems=new String[]{"csourcebillcode","csourcebillrowno",
        "cancestorbillcode","cancestorbillrowno"};
    for(String key:checkItems)
      if(null!=model.getItemByKey(key) && model.getItemByKey(key).isShow())
        return true;
    return false;
  }
  
  private void setVoExchgRateDigit(InvoiceItemVO[] items){
    if(null==items || 0==items.length) return;
    for(InvoiceItemVO item:items){
      if(null==item.getNexchangeotobrate() || StringUtil.isEmptyWithTrim(item.getCcurrencytypeid())) continue;
      int[] digits=getPoPubSetUi2().getBothExchRateDigit(getPk_corp(), item.getCcurrencytypeid());
      if(null==digits || 0==digits.length) continue;
      item.setNexchangeotobrate(item.getNexchangeotobrate().add(UFDouble.ZERO_DBL, digits[0]));
    }
  }
  
}