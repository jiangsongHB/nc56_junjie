package nc.ui.so.so002;

import nc.ui.pub.ButtonObject;
import nc.ui.pub.bill.BillStatus;
import nc.ui.pub.pf.PfUtilClient;
import nc.ui.scm.pub.bill.ButtonTree;
import nc.ui.scm.pub.bill.ScmButtonConst;
import nc.ui.scm.so.SaleBillType;
import nc.vo.pub.BusinessException;
import nc.vo.scm.pub.SCMEnv;

/**
 * <p>
 * <b>本类主要完成以下功能：</b>
 * <ul>
 * <li>功能条目1
 * <li>功能条目2
 * <li>...
 * </ul>
 * <p>
 * <b>变更历史（可选）：</b>
 * <p>
 * XXX版本增加XXX的支持。
 * <p>
 * <p>
 * 
 * @version 本版本号
 * @since 上一版本号
 * @author wangyf
 * @time 2007-3-6 下午04:14:39
 */
public class SaleInvoiceBtn {

  public final String BTN_ATP = "可用量";

  public final String BTN_CUSTINFO = "客户信息";

  public final String BTN_CUSTCREDIT = "客户信用";

  public final String BTN_PRIFIT = "毛利预估";

  public final String BTN_SoTax = "传金税";

  public final String BTN_UniteInvoice = "合并开票";

  public final String BTN_UniteCancel = "放弃合并";

  public final String BTN_Gathe = "出库汇总开票";

  public final String BTN_ExecRpt = "发票执行情况";

  public final String BTN_AuditFlowStatus = "审批流状态";

  public final String BTN_OpposeAct = "生成对冲发票";
  
  public final String BTN_RefAdd = "参照增行";
  
  public final String BTN_FetchCost = "取成本价";
  
  public final String BTN_CardEdit = "卡片编辑";
  
  public final String BTN_ReRowNO = "重排行号";
  
  public final String BTN_ImportTaxCode = "导入税票号";
  
  public final String BTN_UnitePrint = "合并打印";  //add by QuSida 2010-8-6 10:04:59（顺德骏杰）

  private ButtonTree bt = null;

  // ------------------------UI
  public ButtonObject m_boBusiType = null ;
  // 新增
  public ButtonObject m_boAdd = null ;
//  保存
  public ButtonObject m_boSave = null ;

  // 维护
  // 修改，取消（原名放弃），删除（原名作废），放弃转单，合并开票，放弃合并
  // public ButtonObject m_boBill
  public ButtonObject m_boMaintain = null ;
  public ButtonObject m_boModify = null ;
  public ButtonObject m_boCancel = null ;
  public ButtonObject m_boBlankOut = null ;
  public ButtonObject m_boCancelTransfer =null ;
  public ButtonObject m_boUnite =null;
  public ButtonObject m_boUniteCancel = null ;

  // 行操作
  // 增行，删行，插入行（增加），复制行，粘贴行 
  //v55 新加 卡片编辑、重排行号
  public ButtonObject m_boLineOper = null ;
  public ButtonObject m_boAddLine = null ;
  public ButtonObject m_boDelLine = null ;
  public ButtonObject m_boInsertLine = null ;
  public ButtonObject m_boCopyLine = null ;
  public ButtonObject m_boPasteLine = null ;
  public ButtonObject m_boPasteLineTail = null;
  public ButtonObject m_boCardEdit = null;
  public ButtonObject m_boReRowNO = null;
  //参照增行
  public ButtonObject m_boRefAdd = null;
  //取成本价
  public ButtonObject m_boFetchCost = null;
  
  // 审批
  public ButtonObject m_boApprove = null ;

  // 执行
  // 送审，弃审
  public ButtonObject m_boAction = null ;
  public ButtonObject m_boSendAudit = null ;
  public ButtonObject m_boUnApprove = null ;
  
//  查询
  public ButtonObject m_boQuery = null ;

  // 浏览
  // 刷新（增加），定位，首页，上页（原名上一页），下页（原名下一页），末页，全选，全消
  public ButtonObject m_boBrowse = null ;
  public ButtonObject m_boRefresh = null ;
  public ButtonObject m_boLocal = null ;
  public ButtonObject m_boFirst = null ;
  public ButtonObject m_boPrev = null ;
  public ButtonObject m_boNext = null ;
  public ButtonObject m_boLast = null ;
  public ButtonObject m_boSelectAll = null ;
  public ButtonObject m_boUnSelectAll = null ;
  
//  卡片显示/列表显示
  public ButtonObject m_boCard = null ;
  
  // 打印
  // 预览，打印，合并显示
  public ButtonObject m_boPrintManage =null ;
  public ButtonObject m_boPreview = null ;
  public ButtonObject m_boPrint = null ;
  public ButtonObject m_boBillCombin = null ;
  public ButtonObject m_boUnitePrint = null;//合并打印   add by QuSida 2010-8-6 10:06:41（顺德骏杰）
 
//出库汇总开票
  public ButtonObject m_boGather = null ;
  
  // 辅助功能
  // 生成对冲发票，传金税，文档管理
  public ButtonObject m_boAssistFunction =null ;
  public ButtonObject m_boOpposeAct = null ;
  public ButtonObject m_boSoTax = null ;
  public ButtonObject m_boDocument = null ;
  public ButtonObject m_boImportTaxCode = null;


  // 辅助查询
  // 联查，存量显示/隐藏（取代原可用量按钮，功能和订单做一致），审批流状态，
  // 客户信息，发票执行情况，客户信用，毛利预估，客户信息
  public ButtonObject m_boAssistant = null ;
  public ButtonObject m_boOrderQuery = null ;
  public ButtonObject m_boATP = null ;
  public ButtonObject m_boAuditFlowStatus = null ;
  public ButtonObject m_boCustInfo = null ;
  public ButtonObject m_boExecRpt = null ;
  public ButtonObject m_boCustCredit =null ;
  public ButtonObject m_boPrifit = null ;
  


  private ButtonObject[] aryButtonGroup = null;

  /**
   * 方法功能描述：返回当前选择的业务类型。
   * <b>参数说明</b>
   * @return
   * @author fengjb
   * @time 2009-8-13 下午06:20:45
   */
  public String getBusiType() {

    if (null != m_boBusiType.getChildButtonGroup()
    	&& m_boBusiType.getChildButtonGroup().length > 0) {

      for (ButtonObject chidbtn:m_boBusiType.getChildButtonGroup()) {
        if (chidbtn.isSelected()) 
          return chidbtn.getTag();
      }

      // 初始化时使用
      return m_boBusiType.getChildButtonGroup()[0].getTag();
    }
    else {
      return null;
    }
  }

  /**
   * 返回按钮组
   * <p>
   * <b>examples:</b>
   * <p>
   * 使用示例
   * <p>
   * <b>参数说明</b>
   * 
   * @return
   * <p>
   * @author wangyf
   * @time 2007-3-20 下午01:58:56
   */
  public ButtonObject[] getButtonArray() {
    return aryButtonGroup;
  }

  /**
   * 方法功能描述：根据单据状态设置按钮状态。
   * <b>参数说明</b>
   * @param listPanel
   * @author fengjb
   * @time 2009-8-13 下午06:23:18
   */
  public void setStateByBillStatus(SaleInvoiceListPanel listPanel) {
    int iState = listPanel.getBillStatus();
    switch (iState) {
      case BillStatus.FREE: {
        m_boBlankOut.setEnabled(true);
        m_boModify.setEnabled(true);
        m_boApprove.setEnabled(true);
        m_boUnApprove.setEnabled(false);
        m_boDocument.setEnabled(true);
        m_boOrderQuery.setEnabled(true);
        m_boOrderQuery.setEnabled(true);
        break;
      }
      case BillStatus.AUDIT: {
        m_boBlankOut.setEnabled(false);
        m_boApprove.setEnabled(false);
        m_boUnApprove.setEnabled(true);
        m_boModify.setEnabled(false);
        m_boDocument.setEnabled(true);
        m_boOrderQuery.setEnabled(true);
        m_boOrderQuery.setEnabled(false);
        break;
      }
      case BillStatus.BLANKOUT: {
        m_boModify.setEnabled(false);
        m_boAction.setEnabled(false);
        m_boAssistant.setEnabled(false);
        m_boPrint.setEnabled(false);
        m_boDocument.setEnabled(false);
        m_boOrderQuery.setEnabled(false);
        m_boOrderQuery.setEnabled(false);
        // 预览键置灰
        m_boPreview.setEnabled(false);
        // 审批流状态置灰
        m_boAuditFlowStatus.setEnabled(false);
        break;
      }
       // 添加“正在审批”状态
      case BillStatus.AUDITING: {
        m_boBlankOut.setEnabled(false);
        m_boApprove.setEnabled(true);
        m_boUnApprove.setEnabled(false);
        m_boSave.setEnabled(false);
        m_boModify.setEnabled(false);
        break;
      }
      // 添加“审批未通过”状态
      case BillStatus.NOPASS: {
        m_boBlankOut.setEnabled(true);
        m_boModify.setEnabled(true);
        m_boSave.setEnabled(false);
        m_boCancel.setEnabled(false);
        m_boApprove.setEnabled(true);
        m_boUnApprove.setEnabled(false);
        m_boDocument.setEnabled(true);
        m_boOrderQuery.setEnabled(true);
        m_boOrderQuery.setEnabled(true);
        break;
      }
    }

    int selectRow = listPanel.getHeadTable().getSelectedRow();
    int iOppStatus = 0;
    if (selectRow > -1) {
      // 单据状态
      if (listPanel.getHeadItem("fcounteractflag") != null
          && listPanel.getHeadBillModel().getValueAt(selectRow,
              "fcounteractflag") != null
          && listPanel.getHeadItem("fcounteractflag").converType(
              listPanel.getHeadBillModel().getValueAt(selectRow,
                  "fcounteractflag")) != null) {
        iOppStatus = Integer.parseInt(listPanel.getHeadItem("fcounteractflag")
            .converType(
                listPanel.getHeadBillModel().getValueAt(selectRow,
                    "fcounteractflag")).toString());
      }
    }
    // 审批后已对冲单据，不能弃审，修订
    if (iState == BillStatus.AUDIT && iOppStatus == 1) {
      m_boUnApprove.setEnabled(false);
    }
    // 审批且对冲标记为正常，则单据能对冲
    if (iState == BillStatus.AUDIT && iOppStatus == 0) {
      m_boOpposeAct.setEnabled(true);
    }
    else {
      m_boOpposeAct.setEnabled(false);
    }

  }

  /**
   * SaleInvoiceBtn 的构造子
   */
  public SaleInvoiceBtn() {
    // 初始化按钮树
    try {
      bt = new ButtonTree(SaleInvoiceTools.getNodeCode());
    }
    catch (BusinessException e) {
      SCMEnv.out(e);
      return;
    }
    
    // 业务类型
    m_boBusiType = bt.getButton(ScmButtonConst.BTN_BUSINESS_TYPE);
    // 新增
    m_boAdd = bt.getButton(ScmButtonConst.BTN_ADD);
    PfUtilClient.retBusiAddBtn(m_boBusiType, m_boAdd, 
    		SaleInvoiceTools.getLoginPk_Corp(), SaleBillType.SaleInvoice);
    //业务类型按钮设置
    m_boBusiType.getChildButtonGroup()[0].setSelected(true);
    m_boBusiType.setCheckboxGroup(true);
    // 参照增行
    m_boRefAdd = bt.getButton(BTN_RefAdd);
    
    m_boRefAdd.removeAllChildren();
    for(ButtonObject childbtn:m_boAdd.getChildButtonGroup()){
        ButtonObject newbtn = new  ButtonObject
        (childbtn.getName(), childbtn.getHint(),childbtn.getCode());
        newbtn.setTag(childbtn.getTag());
        newbtn.setPowerContrl(false);
        m_boRefAdd.addChildButton(newbtn);
      }

    //取成本价
    m_boFetchCost = bt.getButton(BTN_FetchCost);
    
    // 保存
    m_boSave = bt.getButton(ScmButtonConst.BTN_SAVE); // 保存

    // 维护
    // 修改，取消，删除，放弃转单，合并开票，放弃合并
    m_boMaintain = bt.getButton(ScmButtonConst.BTN_BILL); // 修改
    m_boModify = bt.getButton(ScmButtonConst.BTN_BILL_EDIT); // 修改
    m_boCancel = bt.getButton(ScmButtonConst.BTN_BILL_CANCEL);// 取消
    m_boBlankOut = bt.getButton(ScmButtonConst.BTN_BILL_DELETE);
    m_boCancelTransfer = bt.getButton(ScmButtonConst.BTN_REF_CANCEL);
    m_boUnite = bt.getButton(BTN_UniteInvoice);
    m_boUniteCancel = bt.getButton(BTN_UniteCancel);// 放弃合并

    // 行操作
    // 增行，删行，插入行，复制行，粘贴行，卡片编辑，重排行号
    m_boLineOper = bt.getButton(ScmButtonConst.BTN_LINE);// 行操作
    m_boAddLine = bt.getButton(ScmButtonConst.BTN_LINE_ADD); // 增加行
    m_boDelLine = bt.getButton(ScmButtonConst.BTN_LINE_DELETE); // 删除行
    m_boInsertLine = bt.getButton(ScmButtonConst.BTN_LINE_INSERT); 
    m_boCopyLine = bt.getButton(ScmButtonConst.BTN_LINE_COPY);
    m_boPasteLine = bt.getButton(ScmButtonConst.BTN_LINE_PASTE);
    m_boPasteLineTail = bt.getButton(ScmButtonConst.BTN_LINE_PASTE_TAIL);
    m_boCardEdit = bt.getButton(BTN_CardEdit);
    m_boReRowNO = bt.getButton(BTN_ReRowNO);

    // 审批
    m_boApprove = bt.getButton(ScmButtonConst.BTN_AUDIT); // 审批

    // 执行
    // 送审，弃审
    m_boAction = bt.getButton(ScmButtonConst.BTN_EXECUTE);// 执行
    m_boSendAudit = bt.getButton(ScmButtonConst.BTN_EXECUTE_AUDIT);// 送审
    m_boUnApprove = bt.getButton(ScmButtonConst.BTN_EXECUTE_AUDIT_CANCEL); // 弃审
    
    // 查询
    m_boQuery = bt.getButton(ScmButtonConst.BTN_QUERY);

    // 浏览
    // 刷新（增加），定位，首页，上页（原名上一页），下页（原名下一页），末页，全选，全消
    m_boBrowse = bt.getButton(ScmButtonConst.BTN_BROWSE);
    m_boRefresh = bt.getButton(ScmButtonConst.BTN_BROWSE_REFRESH);
    m_boLocal = bt.getButton(ScmButtonConst.BTN_BROWSE_LOCATE);
    m_boFirst = bt.getButton(ScmButtonConst.BTN_BROWSE_TOP);
    m_boPrev = bt.getButton(ScmButtonConst.BTN_BROWSE_PREVIOUS);
    m_boNext = bt.getButton(ScmButtonConst.BTN_BROWSE_NEXT);
    m_boLast = bt.getButton(ScmButtonConst.BTN_BROWSE_BOTTOM);
    m_boSelectAll = bt.getButton(ScmButtonConst.BTN_BROWSE_SELECT_ALL);
    m_boUnSelectAll = bt.getButton(ScmButtonConst.BTN_BROWSE_SELECT_NONE);
    
    // 卡片显示/列表显示
    m_boCard = bt.getButton(ScmButtonConst.BTN_SWITCH);
    
    // 打印
    // 预览，打印，合并显示
    m_boPrintManage = bt.getButton(ScmButtonConst.BTN_PRINT);
    m_boPreview = bt.getButton(ScmButtonConst.BTN_PRINT_PREVIEW);
    m_boPrint = bt.getButton(ScmButtonConst.BTN_PRINT_PRINT);
    m_boBillCombin = bt.getButton(ScmButtonConst.BTN_PRINT_DISTINCT);
    m_boUnitePrint = new ButtonObject(BTN_UnitePrint,BTN_UnitePrint,BTN_UnitePrint);//合并打印  add by QuSida 2010-8-6 10:09:58 （顺德骏杰）
   
    // 出库汇总开票
    m_boGather = bt.getButton(BTN_Gathe);
    
    // 辅助功能
    // 生成对冲发票，传金税，文档管理
    m_boAssistFunction = bt.getButton(ScmButtonConst.BTN_ASSIST_FUNC);// 辅助功能
    m_boOpposeAct = bt.getButton(BTN_OpposeAct);
    m_boSoTax = bt.getButton(BTN_SoTax);
    m_boDocument = bt.getButton(ScmButtonConst.BTN_ASSIST_FUNC_DOCUMENT);
    m_boImportTaxCode = bt.getButton(BTN_ImportTaxCode);

    // 辅助查询
    // 联查，存量显示/隐藏（取代原可用量按钮，功能和订单做一致），审批流状态，
    // 客户信息，发票执行情况，客户信用，毛利预估，客户信息
    m_boAssistant = bt.getButton(ScmButtonConst.BTN_ASSIST_QUERY);
    m_boOrderQuery = bt.getButton(ScmButtonConst.BTN_ASSIST_QUERY_RELATED);
    
    m_boATP = bt.getButton(BTN_ATP);// 可用量
    m_boATP.setVisible(false);
    
    m_boAuditFlowStatus = bt.getButton(ScmButtonConst.BTN_ASSIST_QUERY_WORKFLOW);
    m_boCustInfo = bt.getButton(BTN_CUSTINFO);;
    m_boExecRpt = bt.getButton(BTN_ExecRpt);
    m_boCustCredit = bt.getButton(BTN_CUSTCREDIT);
    m_boPrifit = bt.getButton(BTN_PRIFIT);
    {
      m_boATP.setTag(ISaleInvoiceAction.Atp);
      m_boCustInfo.setTag(ISaleInvoiceAction.CustInfo);
      m_boCustCredit.setTag(ISaleInvoiceAction.CustCredit);
      m_boPrifit.setTag(ISaleInvoiceAction.Prifit);
      m_boSoTax.setTag(ISaleInvoiceAction.Tax);
      m_boApprove.setTag(ISaleInvoiceAction.Approve);
      m_boBlankOut.setTag(ISaleInvoiceAction.BlankOut);
      m_boUnApprove.setTag(ISaleInvoiceAction.UnApprove);
      m_boUnite.setTag(ISaleInvoiceAction.Unite);
      m_boUniteCancel.setTag(ISaleInvoiceAction.UnUnite);
    }
    aryButtonGroup = bt.getButtonArray();
 
  }

  /**
   * 选择的业务类型变化时，修改“新增”“参照增行”按钮显示
   */
  public void changeButtonsWhenBusiTypeSelected(ButtonObject bo) {
    // 设置业务类型
    m_boBusiType.setTag(bo.getTag());

    // 新增
    PfUtilClient.retAddBtn(m_boAdd, SaleInvoiceTools.getLoginPk_Corp(),
        SaleBillType.SaleInvoice, bo);
    //参照增行
    PfUtilClient.retAddBtn(m_boRefAdd, SaleInvoiceTools.getLoginPk_Corp(), 
    	SaleBillType.SaleInvoice, bo);
    
//     m_boRefAdd.removeAllChildren();
//    for(ButtonObject childbtn:m_boAdd.getChildButtonGroup()){
//        ButtonObject newbtn = new  ButtonObject
//        (childbtn.getName(), childbtn.getHint(), childbtn.getPower(), childbtn.getCode());
//        newbtn.setTag(childbtn.getTag());
//        newbtn.setPowerContrl(false);
//    	m_boRefAdd.addChildButton(newbtn);
//    }
    //所选业务类型在流程配置中销售出库单配置了自动计入发出商品组件，出库汇总开票按钮才可用
    boolean isBizAutoRegister = false;
    try{
      isBizAutoRegister = SaleinvoiceBO_Client.isBizAutoRegister(getBusiType());
    }catch(Exception e){
      nc.vo.scm.pub.SCMEnv.out(e);
    }
    if(isBizAutoRegister){
      m_boAdd.addChildButton(m_boGather);
    }
  }
  /**
   * 方法功能描述：审批流相关按钮。
   * <b>参数说明</b>
   * @return
   * @author fengjb
   * @time 2009-8-13 下午06:28:59
   */
  public ButtonObject[] getApproveActionButtonAry() {

    return new ButtonObject[] {
        m_boDocument, m_boAuditFlowStatus, // 审批流状态
        m_boApprove, m_boOrderQuery

    };
  }

}