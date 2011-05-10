package nc.ui.ic.ic233;


import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Hashtable;

import nc.ui.ic.jj.JJIcScmPubHelper;
import nc.ui.ic.jjpanel.InfoCostPanel;
import nc.ui.ic.pub.ICComboxItem;
import nc.ui.ic.pub.PageCtrlBtn;
import nc.ui.ic.pub.bill.QueryDlgHelpForSpec;
import nc.ui.ic.pub.bill.SpecialBillBaseUI;
import nc.ui.ic.pub.bill.SpecialBillHelper;
import nc.ui.ic.pub.bill.query.QueryConditionDlgForBill;
import nc.ui.ic.pub.tools.GenMethod;
import nc.ui.scm.pub.report.BillRowNo;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.beans.UIMenuItem;
import nc.ui.pub.beans.UITextField;
import nc.ui.pub.bill.BillCellEditor;
import nc.vo.ic.jjvo.InformationCostVO;
import nc.vo.ic.pub.BillRowType;
import nc.vo.ic.pub.BillTypeConst;
import nc.vo.ic.pub.SmartVOUtilExt;
import nc.vo.ic.pub.bill.GeneralBillHeaderVO;
import nc.vo.ic.pub.bill.GeneralBillItemVO;
import nc.vo.ic.pub.bill.GeneralBillVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.ic.bill.InvVO;
import nc.vo.ic.pub.bill.IItemKey;
import nc.vo.ic.pub.bill.QryInfoConst;
import nc.vo.ic.pub.bill.SpecialBillHeaderVO;
import nc.vo.ic.pub.bill.SpecialBillItemVO;
import nc.vo.ic.pub.bill.SpecialBillVO;
import nc.vo.ic.pub.check.CheckTools;
import nc.vo.ic.pub.lang.ResBase;
import nc.vo.ic.pub.smallbill.SMSpecialBillVO;
import nc.vo.scm.ic.bill.WhVO;
import nc.vo.scm.pub.SCMEnv;
import nc.vo.scm.pub.smart.SmartFieldMeta;
import nc.vo.scm.pub.vosplit.SplitBillVOs;
import nc.vo.scm.constant.ic.BillMode;
import nc.vo.scm.constant.ic.InOutFlag;

/*
 * 创建者：仲瑞庆
 * 创建日期：2001-04-20

 * 功能：形态转换单 界面


 * 修改日期，修改人，修改原因，注释标志：
 */
public class ClientUI extends SpecialBillBaseUI {
 
	
	//2010-11-08 MeiChao 添加--费用录入按钮.Begin
	
	private UFDouble number = null;//费用页签下的数量

	
	//拉式生产的最初金额 add by 付世超 2010-10-15
	private UFDouble pmny = null;
	
	private ButtonObject expenseInput;
	private ButtonObject getButtonExpenseInput(){
		if(this.expenseInput==null){
			this.expenseInput=new ButtonObject("费用录入","费用录入","费用录入");
		}
		return this.expenseInput;
	}
	//2010-11-08 MeiChao 添加 End

	
	
	
	
  public final ICComboxItem[] comInvsetparttype = new ICComboxItem[]{
    new ICComboxItem(new Integer(BillRowType.beforeConvert),nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec","UPP4008spec-000142")),
    new ICComboxItem(new Integer(BillRowType.afterConvert),nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec","UPP4008spec-000143"))
  };


/**
 * ClientUI 构造子注解。
 */
public ClientUI() {
  super();
  initialize();
}

/**
  * 创建者：仲瑞庆
  * 功能：当发生数据修改时
  * 参数：e单据编辑事件
  * 返回：
  * 例外：
  * 日期：(2001-5-8 19:08:05)
  * 修改日期，修改人，修改原因，注释标志：
  */
public void afterEdit(nc.ui.pub.bill.BillEditEvent e) {

  //自动更新VO中数据
  //********************************************************************//

  String sColItemKey = e.getKey().trim();
  int row = e.getRow();

  if (sColItemKey.equals("cwarehousename")) {
    afterBodyWhEdit(e);
  } else if (sColItemKey.equals("cinventorycode")) {
      afterInvMutiEdit(e);
  } else if (sColItemKey.equals(m_sNumItemKey)) {
    calculateByHsl(row, m_sNumItemKey, m_sAstItemKey, 0);
  } else if (sColItemKey.equals(m_sAstItemKey)) {
    calculateByHsl(row, m_sNumItemKey, m_sAstItemKey, 1);
  } else if (sColItemKey.equals("invsetparttype")) {
    Integer status = (Integer)GenMethod.getICCItemValueByName(
        this.comInvsetparttype, (String)getBillCardPanel().getBodyValueAt(e.getRow(), "invsetparttype"));
    if(status!=null)
      setShowStatus(e.getRow(), status);
    
    
  } else
    super.afterEdit(e);

}


/**
   * 初始化类。
   */
/* 警告：此方法将重新生成。 */
protected void initialize() {
	
	//在父类初始化方法执行前,将费用录入按钮添加到按钮树中.
	//this.getButtonManager().getButtonTree().addMenu(this.getButtonExpenseInput());
	
	
	
  try {
    initVariable();
    super.initialize();

    setSortEnable(true, false, false);
    setSortEnable(false, false, false);
    initOther();
  } catch (java.lang.Throwable ivjExc) {
    handleException(ivjExc);
  }
}

/**
 * 初始化类。
 */
/* 警告：此方法将重新生成。 */
protected void initOther() {
  GenMethod.initComboBox(getBillCardPanel().getBodyItem("invsetparttype"), this.comInvsetparttype, false);
  GenMethod.initComboBox(getBillListPanel().getBodyItem("invsetparttype"), this.comInvsetparttype, false);
}




/**
 * onButtonClicked 方法注解。
 */
public void onButtonClicked(nc.ui.pub.ButtonObject bo) { //finished
  showHintMessage(bo.getName());
  if (bo == m_boAdd)
    onAdd();
  else if (bo == m_boAuditBill)
    onAuditBill();
  else if (bo == m_boCancelAudit)
    onCancelAudit();
  else if (bo == m_boSave)
    onSave();
  //2010-11-08 MeiChao begin 添加对费用录入按钮的事件相应处理方法
  else if (bo == this.getButtonExpenseInput())
    this.onBoExpenseInput();
  //2010-11-08 MeiChao end 添加对费用录入按钮的事件相应处理方法
  else
    super.onButtonClicked(bo);

}

/** 设置表单按钮状态
  * 此处插入方法说明。
  * 创建日期：(2001-4-30 13:58:35)
  */
protected void setButtonState() {
	//2010-11-08 MeiChao 设置费用录入按钮的状态
	this.setExpenseInputButtonStat(m_iMode);
	
	
  switch (m_iMode) {
    case BillMode.New : //新增

      //showHintMessage("新增  按下Ctrl键可选择多行数据进行删除...");
      showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON","UPPSCMCommon-000287")/*@res "新增"*/);

      m_billMng.setEnabled(true);
      m_boAdd.setEnabled(false);
      m_boChange.setEnabled(false);
      m_boDelete.setEnabled(false);

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

      m_boQuery.setEnabled(false);
      m_boLocate.setEnabled(false);
      m_boPrint.setEnabled(false);
      m_boList.setEnabled(false);

      m_boRowQuyQty.setEnabled(true);
      m_boPreview.setEnabled(false);
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

      m_boQuery.setEnabled(false);
      m_boLocate.setEnabled(false);
      m_boPrint.setEnabled(false);
      m_boList.setEnabled(false);

      m_boRowQuyQty.setEnabled(true);
      m_boPreview.setEnabled(false);
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
      m_boCopyBill.setEnabled(m_iTotalListHeadNum > 0);
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
        m_boCancelAudit.setEnabled(false);
      }

      m_boQuery.setEnabled(true);
      m_boLocate.setEnabled((m_iTotalListHeadNum > 0));
      m_boPrint.setEnabled(true);
      m_boList.setEnabled(true);

      m_boRowQuyQty.setEnabled(true);
      m_boPreview.setEnabled(true);
      if (null != m_alListData)
    	  setPageBtnStatus(m_alListData.size(), m_iLastSelListHeadRow);
		if (null != m_pageBtn)
			m_pageBtn.setPageBtnVisible(true);
      break;
    case BillMode.List : //列表状态

    	//modified by liuzy 2008-05-20 没有实现的功能干嘛显示给用户？注释之！
//      showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec","UPP4008spec-000130")/*@res "列表  按下Ctrl键可选择多张单据进行删除..."*/);

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
      ////禁止修改和删除
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

      m_boQuery.setEnabled(true);
      m_boLocate.setEnabled(true);
      m_boPrint.setEnabled(true);
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


/**
   * 新增
   * 创建日期：(2001-4-18 19:45:17)
   */
public void onAdd() { //finished
  super.onAdd();
  initShowStatus();
}

  /**
  * 增行
  * 创建日期：(2001-4-18 19:46:27)
  */
  public void onAddRow() { //finished
    super.onAddRow();
    int iRow = getBillCardPanel().getRowCount() - 1;
    int iStatus=BillRowType.afterConvert;
    
    if (iRow>0)
      iStatus=BillRowType.afterConvert;
    else
        iStatus=BillRowType.beforeConvert;

    setShowStatus(iRow,iStatus);
  }


  /**
   * 创建者：yb
   * 功能：插入行
   * 参数：
   * 返回：
   * 例外：
   * 日期：(2001-5-16 下午 5:58)
   * 修改日期，修改人，修改原因，注释标志：
   */
  public void onInsertRow() { 
   super.onInsertRow();
   int irow = getBillCardPanel().getBillTable().getSelectedRow();
   int iStatus=BillRowType.afterConvert;
   
   setShowStatus(irow,iStatus);
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
//  int[] iaRow = getBillCardPanel().getBillTable().getSelectedRows();
//  for (int i = 0; i < iaRow.length; i++) {
//    if (iaRow[i] == 0) {
//      showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec","UPP4008spec-000131")/*@res "第一行是“转换前”，不允许复制。"*/);
//      return;
//    }
//  }

  super.onCopyRow();

}



  /**
    * 删行
    * 创建日期：(2001-4-18 19:46:41)
    */
  public void onDeleteRow() {
//    if(getBillCardPanel().getBillTable().getSelectedRow()==0
//      ||getBillCardPanel().getBillTable().getSelectedRow()==1){
//      showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec","UPP4008spec-000132")/*@res "该行不能删除！"*/);
//      return;
//
//    }
    getBillCardPanel().delLine();


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
  //finished added by zhx bill row no
//  int iRowCount = getBillCardPanel().getBodyPanel().getTableModel().getRowCount();
//  int row = getBillCardPanel().getBillTable().getSelectedRow();
//  if (row <= 0) {
//    showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec","UPP4008spec-000133")/*@res "不能在此行粘贴，请选择其他行!"*/);
//  } else {
//    super.onPasteRow();
//  }
  super.onPasteRow();
}





/**
  * 保存
  * 创建日期：(2001-4-18 19:47:08)
  */
public boolean onSave() {
  try {
    if (m_iLastSelListHeadRow < 0) { //未选择任何表，直接新增时；或者查询结果为空时；会发生
      m_iLastSelListHeadRow = 0; //最后选中的列表表头行
      m_iTotalListHeadNum = 0; //列表表头目前存在的行数
    }

    /**
     * 2010-11-08 MeiChao
     * 获取需要保存的费用信息VO
     */
    //获取需要保存的费用信息
    InformationCostVO[] expenseVOs=(InformationCostVO[])this.getBillCardPanel().getBillModel("jj_scm_informationcost").getBodyValueVOs(InformationCostVO.class.getName());
    
    
    //填入界面中数据
    getBillCardPanel().tableStopCellEditing();
    getBillCardPanel().stopEditing();

    //滤掉所有的空行
    filterNullLine();
    if (getBillCardPanel().getRowCount() == 0) {
      showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec","UPP4008spec-000134")/*@res "无数据，请重新输入..."*/);
      initShowStatus();
      return false;
    }
    //added by zhx 030626 检查行号的合法性; 该方法应放在过滤空行的后面。
    if (!nc
      .ui
      .scm
      .pub
      .report
      .BillRowNo
      .verifyRowNosCorrect(getBillCardPanel(), m_sBillRowNo)) {
      return false;
    }

    //对形态转换单，删除第二行以下各行
    if (getBillCardPanel().getRowCount() < 2) {
      showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec","UPP4008spec-000135")/*@res "数据行不正确，请重新输入..."*/);
      return false;

    }

    ////加入单据号
    //if (BillMode.New == m_iMode) {
      //if (!nc
        //.ui
        //.ic
        //.pub
        //.exp
        //.GeneralMethod
        //.setBillCode(m_voBill, m_sBillTypeCode, getBillCardPanel())) {
        //nc.ui.pub.beans.MessageDialog.showErrorDlg(this, "错误", "获得单据号失败！");
        //return;
      //}
    //}

    SpecialBillVO voTempBill = getBillVO();
    //同步最大化VO
    //voTempBill--->>>m_voBill
    m_voBill.setIDItems(voTempBill);
    // 修改人：陈倪娜 下午02:01:32_2009-10-15 修改原因:填充界面缓存VO的单据类型
    m_voBill.getParentVO().setAttributeValue("cbilltypecode", m_sBillTypeCode);
    //VO校验
    if (!checkVO()) {
      return false;
    }
    String sHPK = null; //bill pk
    int iRowCount = getBillCardPanel().getRowCount();
    SpecialBillItemVO[] voaMyTempItem = null;
	// 修改或者保存后返回的小VO
	SMSpecialBillVO voSM = null;
    if (BillMode.New == m_iMode) {
      //新增
      //向m_shvoBillSpecialHVO中写入FreeVO中增加的值
      voTempBill.setChildrenVO((SpecialBillItemVO[]) getChangedItemVOs());
      //写入HVO中BillTypeCode表单类型
      voTempBill.getParentVO().setAttributeValue("cbilltypecode", m_sBillTypeCode);
   
      //得到当前的ItemVO
      voaMyTempItem = (SpecialBillItemVO[]) voTempBill.getChildrenVO();
      //临时HVO[]

      //写入,并返回PKs
      //重置单据行号zhx 0630:
      if (iRowCount > 0 && voTempBill.getChildrenVO() != null) {
        if (getBillCardPanel().getBodyItem(m_sBillRowNo) != null)
          for (int i = 0; i < iRowCount; i++) {

            voTempBill.setItemValue(
              i,
              m_sBillRowNo,
              getBillCardPanel().getBodyValueAt(i, m_sBillRowNo));

          }
      }
      if(m_sCorpID.equals(voTempBill.getVBillCode()))
        voTempBill.setVBillCode(null);
      voTempBill.getHeaderVO().setCoperatoridnow(m_sUserID);//当前操作员
      ArrayList alsPrimaryKey =
        (ArrayList) nc.ui.pub.pf.PfUtilClient.processAction(
          "WRITE",
          m_sBillTypeCode,
          m_sLogDate,
          voTempBill);
      voTempBill.setVBillCode(m_sCorpID);
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
  /*    //写入HHeaderVO的PK
      voTempBill.getParentVO().setPrimaryKey(sHPK);
      //m_voBill.getParentVO().setPrimaryKey(sHPK);
      //写入HItemVO的PK与对应表头PK
      for (int i = 0; i < voaMyTempItem.length; i++) {
        voaMyTempItem[i].setPrimaryKey(alMyPK.get(i + 1).toString());
        voaMyTempItem[i].setAttributeValue("cspecialhid", sHPK);
      }
      voTempBill.setChildrenVO(voaMyTempItem);
      nc.ui.ic.pub.tools.GenMethod.fillVOValuesBy(new SpecialBillHeaderVO[]{voTempBill.getHeaderVO()},
      		new String[]{"tmaketime","tlastmoditime"}, new int[]{SmartFieldMeta.JAVATYPE_STRING,SmartFieldMeta.JAVATYPE_STRING},
      		IItemKey.cspecialhid, IItemKey.ic_special_h,
      		new String[]{IItemKey.tmaketime, IItemKey.tlastmoditime}, IItemKey.cspecialhid, null);
      //同步最大化VO
      m_voBill.setIDItems(voTempBill);*/
      //增加HVO
      m_iLastSelListHeadRow = m_iTotalListHeadNum;
      addBillVO();
      /**
       * 2010-11-08 MeiChao
       * 如果是新增单据,则向数据库中插入新费用信息.
       */
      //2010-11-08 MeiChao begin
      //将主键写入费用信息VO中
				if (expenseVOs != null && expenseVOs.length > 0) {
					for (int i = 0; i < expenseVOs.length; i++) {
						expenseVOs[i].setCbillid(sHPK);//设置所属单据主键
						expenseVOs[i].setVdef10("4N");//设置所属单据类型
					}
				}
      JJIcScmPubHelper expenseManager=new JJIcScmPubHelper();
      expenseManager.insertSmartVOs(expenseVOs);
      //2010-11-08 MeiChao end
      
      
    } else if (BillMode.Update == m_iMode) { //修改
      //从界面中获得需要的数据
      voTempBill = getUpdatedBillVO();
      if (null == voTempBill) {
        return false;
      } //向m_shvoBillSpecialHVO中写入FreeVO中增加的值
      voTempBill.setChildrenVO((SpecialBillItemVO[]) getChangedItemVOs());
      //得到当前的ItemVO
      voaMyTempItem = (SpecialBillItemVO[]) voTempBill.getChildrenVO();
      //写入,并返回PKs
      //重置单据行号zhx 0630:
      if (iRowCount > 0 && voTempBill.getChildrenVO() != null) {
        if (getBillCardPanel().getBodyItem(m_sBillRowNo) != null)
          for (int i = 0; i < iRowCount; i++) {
            voTempBill.setItemValue(
              i,
              m_sBillRowNo,
              getBillCardPanel().getBodyValueAt(i, m_sBillRowNo));

          }
      }
      voTempBill.getHeaderVO().setCoperatoridnow(m_sUserID);//当前操作员
      ArrayList alsPrimaryKey =
        (ArrayList) nc.ui.pub.pf.PfUtilClient.processAction(
          "WRITE",
          m_sBillTypeCode,
          m_sLogDate,
          voTempBill,
          ((SpecialBillVO) m_alListData.get(m_iLastSelListHeadRow)).clone());
      if (alsPrimaryKey == null || alsPrimaryKey.size() < 2) {
        nc.vo.scm.pub.SCMEnv.out("return data error.");
        return true;
      } //显示提示信息
      if (alsPrimaryKey.get(0) != null)
        showErrorMessage((String) alsPrimaryKey.get(0));
      ArrayList alMyPK = (ArrayList) alsPrimaryKey.get(1);
      sHPK = voTempBill.getParentVO().getPrimaryKey();
		voSM = (SMSpecialBillVO) alsPrimaryKey
		.get(alsPrimaryKey.size() - 1);
      //写入HItemVO的PK与对应表头PK,删去多余的ItemVO
      ArrayList alItemVO = new ArrayList();
      //加入旧表体中没有改变的ItemVO
      //适应平台的修改,总是返回第一个是表头的PK   2001/09/26
      //changed to start from 1
      int iItemNumb = 1;
      for (int i = 0; i < voaMyTempItem.length; i++) {
        switch (voaMyTempItem[i].getStatus()) {
          case nc.vo.pub.VOStatus.UNCHANGED :
            //switch (getBillCardPanel().getBillModel().getRowState(i)) {
            //case getBillCardPanel().getBillModel().NORMAL :
            alItemVO.add(voaMyTempItem[i]);
            break;
          case nc.vo.pub.VOStatus.NEW :
            if (iItemNumb < alMyPK.size()) {
              voaMyTempItem[i].setPrimaryKey(alMyPK.get(iItemNumb).toString());
              iItemNumb++;
              voaMyTempItem[i].setAttributeValue("cspecialhid", sHPK);
              alItemVO.add(voaMyTempItem[i]);
            } else {
              SCMEnv.out("保存时出现行对应不上现象，请程序员检查...");
            }
            break;
          case nc.vo.pub.VOStatus.UPDATED :
            alItemVO.add(voaMyTempItem[i]);
        }
      }

 /*     voaMyTempItem = null;
      voaMyTempItem = new SpecialBillItemVO[alItemVO.size()];
      for (int i = 0; i < alItemVO.size(); i++) {
        voaMyTempItem[i] = (SpecialBillItemVO) alItemVO.get(i);
      }
      voTempBill.setChildrenVO(voaMyTempItem);
      
      nc.ui.ic.pub.tools.GenMethod.fillVOValuesBy(new SpecialBillHeaderVO[]{voTempBill.getHeaderVO()},
      		new String[]{"tlastmoditime"}, new int[]{SmartFieldMeta.JAVATYPE_STRING},
      		IItemKey.cspecialhid, IItemKey.ic_special_h,
      		new String[]{IItemKey.tlastmoditime}, IItemKey.cspecialhid, null);
      //同步最大化VO
      m_voBill.setIDItems(voTempBill);
      //修改HVO
      m_alListData.set(m_iLastSelListHeadRow, m_voBill.clone());*/
      /**
       * 2010-11-08 MeiChao
       * 如果是修改单据,那么修改费用信息
       */
      //2010-11-08 MeiChao 将新费用信息中设置上单据id
      for (int i = 0; i < expenseVOs.length; i++) {
					if (expenseVOs[i].getCbillid() == null) {
						expenseVOs[i].setCbillid(voTempBill.getHeaderVO()
								.getPrimaryKey());//设置所属单据id
						expenseVOs[i].setVdef10("4N");//设置所属单据类型
					}
				}
      JJIcScmPubHelper expenseManager=new JJIcScmPubHelper();
      expenseManager.updateSmartVOs(expenseVOs, voTempBill.getHeaderVO()
								.getPrimaryKey());
      
    }
    //set ui pk below,so put it before freshts.
    //switchListToBill();
    //fresh timestamp
	// 将后台信息更新到界面
	freshVOBySmallVO(voSM);
/*    if (sHPK != null) {
      ArrayList alLastTs = qryLastTs(sHPK);
      freshTs(alLastTs);
    }*/
    //重显HVO
    m_iMode = BillMode.Browse;
    m_iFirstSelListHeadRow = -1;
    setButtonState();
    setBillState();
    /**
     * 2010-11-08 MeiChao
     * 将费用VO信息重写至界面中(暂时不需要)
     */
    showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec","UPP4008spec-000136")/*@res "保存成功！"*/);
    return true;
  } catch (Exception e) {
    showErrorMessage(e.getMessage());
    showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec","UPP4008spec-000137")/*@res "未能完成，请再做尝试！"*/);
    handleException(e);
    return false;
  }
}

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
  m_boAdd = new ButtonObject(nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UC001-0000002")/*@res "增加"*/, nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec","UPPSCMCommon-000308")/*@res "增加单据"*/, 0,"增加"); /*-=notranslate=-*/
  m_boChange = new ButtonObject(nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UC001-0000045")/*@res "修改"*/, nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec","UPPSCMCommon-000291")/*@res "修改单据"*/, 0,"修改");  /*-=notranslate=-*/
  m_boDelete = new ButtonObject(nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UC001-0000039")/*@res "删除"*/, nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec","UPP4008spec-000504")/*@res "删除单据"*/, 0,"删除"); /*-=notranslate=-*/
  m_boCopyBill = new ButtonObject(nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UC001-0000043")/*@res "复制"*/, nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec","UPP4008spec-000505")/*@res "复制单据"*/, 0,"复制"); /*-=notranslate=-*/
  m_boSave = new ButtonObject(nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UC001-0000001")/*@res "保存"*/, nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UC001-0000001")/*@res "保存"*/, 0,"保存");  /*-=notranslate=-*/
  m_boCancel = new ButtonObject(nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UC001-0000008")/*@res "取消"*/, nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UC001-0000008")/*@res "取消"*/, 0,"取消");  /*-=notranslate=-*/

  m_boAddRow = new ButtonObject(nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UC001-0000012")/*@res "增行"*/, nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UC001-0000012")/*@res "增行"*/, 0,"增行");  /*-=notranslate=-*/
  m_boDeleteRow = new ButtonObject(nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UC001-0000013")/*@res "删行"*/, nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UC001-0000013")/*@res "删行"*/, 0,"删行"); /*-=notranslate=-*/
  m_boInsertRow = new ButtonObject(nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UC001-0000016")/*@res "插入行"*/, nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UC001-0000016")/*@res "插入行"*/, 0,"插入行");  /*-=notranslate=-*/
  m_boCopyRow = new ButtonObject(nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UC001-0000014")/*@res "复制行"*/, nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UC001-0000014")/*@res "复制行"*/, 0,"复制行");  /*-=notranslate=-*/
  m_boPasteRow = new ButtonObject(nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UC001-0000015")/*@res "粘贴行"*/, nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UC001-0000015")/*@res "粘贴行"*/, 0,"粘贴行"); /*-=notranslate=-*/
	m_boPasteRowTail = new ButtonObject(nc.ui.ml.NCLangRes.getInstance()
			.getStrByID("4008bill", "UPP4008bill-000556")/* @res "粘贴行到表尾" */,
			nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill",
					"UPP4008bill-000556")/* @res "粘贴行到表尾" */, 0, "粘贴行到表尾"); /*-=notranslate=-*/
	m_boNewRowNo = new ButtonObject(nc.ui.ml.NCLangRes.getInstance()
			.getStrByID("4008bill", "UPP4008bill-000551")/* @res "重排行号" */,
			nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill",
					"UPP4008bill-000551")/* @res "重排行号" */, 0, "重排行号"); /*-=notranslate=-*/
	
  m_boAuditBill = new ButtonObject(nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UC001-0000027")/*@res "审批"*/, nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UC001-0000027")/*@res "审批"*/, 0,"审批"); /*-=notranslate=-*/
  m_boCancelAudit = new ButtonObject(nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UC001-0000028")/*@res "弃审"*/, nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UC001-0000028")/*@res "弃审"*/, 0,"弃审"); /*-=notranslate=-*/
  m_boQuery = new ButtonObject(nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UC001-0000006")/*@res "查询"*/, nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UC001-0000006")/*@res "查询"*/, 0,"查询"); /*-=notranslate=-*/
  m_boLocate = new ButtonObject(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec","UPPSCMCommon-000089")/*@res "定位"*/, nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec","UPPSCMCommon-000089")/*@res "定位"*/, 0,"定位");  /*-=notranslate=-*/
  m_PrintMng = new ButtonObject(nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UC001-0000007")/*@res "打印"*/, nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UC001-0000007")/*@res "打印"*/, 0,"打印管理");  /*-=notranslate=-*/
  m_boPrint = new ButtonObject(nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UC001-0000007")/*@res "打印"*/, nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UC001-0000007")/*@res "打印"*/, 0,"打印"); /*-=notranslate=-*/
  m_boPreview = new ButtonObject(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec","UPPSCMCommon-000305")/*@res "预览"*/, nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec","UPPSCMCommon-000305")/*@res "预览"*/, 0,"预览"); /*-=notranslate=-*/
  {
    m_PrintMng.addChildButton(m_boPrint);
    m_PrintMng.addChildButton(m_boPreview);
    }
  m_boList = new ButtonObject(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec","UPPSCMCommon-000186")/*@res "切换"*/, nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec","UPPSCMCommon-000186")/*@res "切换"*/, 0,"切换");  /*-=notranslate=-*/

  m_boJointCheck = new ButtonObject(nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON","UPPSCMCommon-000145")/*@res "联查"*/, nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON","UPPSCMCommon-000145")/*@res "联查"*/, 0,"联查");  /*-=notranslate=-*/

  m_billMng = new ButtonObject(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec","UPT40080816-000037")/*@res "单据维护"*/, nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec","UPPSCMCommon-000074")/*@res "单据维护操作"*/, 0,"单据维护");  /*-=notranslate=-*/
  m_billRowMng = new ButtonObject(nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UC001-0000011")/*@res "行操作"*/, nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UC001-0000011")/*@res "行操作"*/, 0,"行操作"); /*-=notranslate=-*/

  m_boRowQuyQty = new ButtonObject(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec","UPPSCMCommon-000359")/*@res "存量查询"*/, nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec","UPPSCMCommon-000359")/*@res "存量查询"*/, 0,"存量查询"); /*-=notranslate=-*/

	// 上下翻页的控制
	m_pageBtn = new PageCtrlBtn(this);
	m_boBrowse = new ButtonObject(nc.ui.ml.NCLangRes.getInstance()
			.getStrByID("common", "UC001-0000021")/* @res "浏览" */,
			nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
					"UC001-0000021")/* @res "浏览" */, 0, "浏览"); /*-=notranslate=-*/
  
  m_boLineCardEdit = new ButtonObject(nc.ui.ml.NCLangRes.getInstance()
      .getStrByID("common", "SCMCOMMONIC55YB002")/* @res "卡片编辑" */,
      nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
          "SCMCOMMONIC55YB002")/* @res "卡片编辑" */, 0, "卡片编辑"); /*-=notranslate=-*/
	
  m_billMng.addChildButton(m_boAdd);
  m_billMng.addChildButton(m_boDelete);
  m_billMng.addChildButton(m_boChange);
  m_billMng.addChildButton(m_boCopyBill);
  m_billMng.addChildButton(m_boSave);
  m_billMng.addChildButton(m_boCancel);

  m_billRowMng.addChildButton(m_boAddRow);
  m_billRowMng.addChildButton(m_boDeleteRow);
  m_billRowMng.addChildButton(m_boInsertRow);
  m_billRowMng.addChildButton(m_boCopyRow);
  m_billRowMng.addChildButton(m_boPasteRow);
	m_billRowMng.addChildButton(m_boPasteRowTail);
	m_billRowMng.addChildButton(m_boNewRowNo);
  m_billRowMng.addChildButton(m_boLineCardEdit);
  
	m_boBrowse.addChildButton(m_pageBtn.getFirst());
	m_boBrowse.addChildButton(m_pageBtn.getPre());
	m_boBrowse.addChildButton(m_pageBtn.getNext());
	m_boBrowse.addChildButton(m_pageBtn.getLast());

  m_aryButtonGroup = new ButtonObject[] { m_billMng,
    m_billRowMng,
    m_boAuditBill,
      m_boCancelAudit,
      m_boQuery,m_boBrowse,
      m_boJointCheck,
      m_boRowQuyQty,
      m_boLocate,
      m_PrintMng,
      m_boList,
      this.getButtonExpenseInput()//2010-11-08 MeiChao 将费用录入按钮加入到按钮组中.
  };
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
  //常量
  /**组装*/
  m_sBillTypeCode = nc.vo.ic.pub.BillTypeConst.m_transform;
  m_sBillCode = "IC_BILL_TEMPLET_004N";
  m_sPNodeCode = "40081008";
  //sSpecialHBO_Client = "nc.ui.ic.ic233.SpecialHBO_Client";
}

/**
 * 创建者：yangb
 * 功能：根据形态转换得到其他出入库单据VO
 * 参数：
 * 返回：
 * 例外：
 * 日期：(2001-5-10 下午 2:54)
 * 修改日期，修改人，修改原因，注释标志：
 * 
 * 本版需要多对转换关系存在于一张形态转换单，其他出入库必须纪录来自同一行转换前PK。
 * 考虑将形态转换的转换前行PK纪录到源头cfirstbillbid上，凡是来源于相同转换前的行，
 * 其他出入库单的cfirstbillbid都记录相同。
 */
private ArrayList<GeneralBillVO[]> getGeneralBillVOs(SpecialBillVO spvo) {
  if(spvo==null)
    return null;
  SpecialBillHeaderVO spheadvos = spvo.getHeaderVO();
  SpecialBillItemVO[] spbodyvos = spvo.getItemVOs();
  if(spbodyvos==null || spbodyvos.length<=0)
    return null;
  final String  ctempparentid = "ctempparentid";
  ArrayList<SpecialBillItemVO> list4i = new ArrayList<SpecialBillItemVO>(spbodyvos.length);
  ArrayList<SpecialBillItemVO> list4a = new ArrayList<SpecialBillItemVO>(spbodyvos.length);
  SpecialBillItemVO beforeitemvo = null;
  //遍历形态转换单表体，将转换前行PK 纪录到转换后的字段ctempparentid,然后做VO对照
  for(int i=0;i<spbodyvos.length;i++){
    
    if(spbodyvos[i].getFbillrowflag()!=null && spbodyvos[i].getFbillrowflag()==BillRowType.beforeConvert){
      list4i.add(spbodyvos[i]);
      beforeitemvo = spbodyvos[i];
    }else{
//      if(beforeitemvo==null){
//          showErrorMessage("在类型为[转换后]的行前面必须至少有一个[转换前]的行");
//          return null;
//      }
      spbodyvos[i].setAttributeValue(ctempparentid, beforeitemvo.getCspecialbid());
      list4a.add(spbodyvos[i]);
    }
  }
  //分单
  SpecialBillVO tempVO = new SpecialBillVO();
  tempVO.setParentVO(spheadvos);
  tempVO.setChildrenVO(list4i.toArray(new SpecialBillItemVO[list4i.size()]));
  SpecialBillVO[] spvos4i = (SpecialBillVO[])SplitBillVOs.getSplitVO(SpecialBillVO.class.getName(),SpecialBillHeaderVO.class.getName(), 
      SpecialBillItemVO.class.getName(), tempVO , null, new String[]{"cwarehouseid"});
  
  tempVO = new SpecialBillVO();
  tempVO.setParentVO(spheadvos);
  tempVO.setChildrenVO(list4a.toArray(new SpecialBillItemVO[list4a.size()]));
  SpecialBillVO[] spvos4a = (SpecialBillVO[])SplitBillVOs.getSplitVO(SpecialBillVO.class.getName(),SpecialBillHeaderVO.class.getName(), 
      SpecialBillItemVO.class.getName(), tempVO , null, new String[]{"cwarehouseid"});
  
  
  //交换 处理
  GeneralBillVO[] vos4i = pfVOConvert(spvos4i,BillTypeConst.m_transform,BillTypeConst.m_otherOut);
  for(GeneralBillVO gvo : vos4i){
    BillRowNo.setVORowNoByRule(gvo,BillTypeConst.m_otherOut, IItemKey.CROWNO);
    setStatus(gvo);
  }
  
  GeneralBillVO[] vos4a = pfVOConvert(spvos4a,BillTypeConst.m_transform,BillTypeConst.m_otherIn);
  for(GeneralBillVO gvo : vos4a){
    BillRowNo.setVORowNoByRule(gvo,BillTypeConst.m_otherIn, IItemKey.CROWNO);
    setStatus(gvo);
  }
  
  ArrayList<GeneralBillVO[]> retlist = new ArrayList<GeneralBillVO[]>(2);
  retlist.add(vos4i);
  retlist.add(vos4a);
  return retlist;
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
  if (m_alListData == null
    || m_alListData.size() < m_iLastSelListHeadRow
    || m_iLastSelListHeadRow < 0) {

    return;
  }
  
  SpecialBillVO voTempBill =
    (SpecialBillVO) m_alListData.get(m_iLastSelListHeadRow);
  
  if (null == voTempBill.getItemVOs()[0].getPk_calbody()){
	  String[] sWhids = new String[voTempBill.getItemVOs().length];
	  for(int i = 0 ; i < voTempBill.getItemVOs().length ; i++)
		  sWhids[i] = voTempBill.getItemVOs()[i].getCwarehouseid();
	  String[] sPkcalbodyids = GenMethod.getsCalbodyidByWhid(sWhids);
	  for(int i = 0 ; i < voTempBill.getItemVOs().length ; i++)
		  voTempBill.getItemVOs()[i].setPk_calbody(sPkcalbodyids[i]);
  }
  ArrayList<GeneralBillVO[]> retvolist = getGeneralBillVOs(voTempBill);
  if(retvolist==null || retvolist.size()<=0)
    return ;
  
  GeneralBillVO[] gbvoOut = retvolist.get(0);
  GeneralBillVO[] gbvoIn = retvolist.get(1);
  
  ArrayList<GeneralBillVO> alOutGeneralVO = new ArrayList<GeneralBillVO>(gbvoOut.length);
  alOutGeneralVO.addAll(Arrays.asList(gbvoOut));
  ArrayList<GeneralBillVO> alInGeneralVO = new ArrayList<GeneralBillVO>(gbvoIn.length);
  alInGeneralVO.addAll(Arrays.asList(gbvoIn));
 

//  //置出库VO
//  SpecialBillVO tempVO = new SpecialBillVO();
//  SpecialBillItemVO[] tempItemVO = new SpecialBillItemVO[1];
//  tempVO.setParentVO(voTempBill.getHeaderVO());
//  tempItemVO[0] = voTempBill.getItemVOs()[0];
//  tempVO.setChildrenVO(tempItemVO);
//  //GeneralBillVO gbvoOut = new GeneralBillVO();
//  //gbvoOut = changeFromSpecialVOtoGeneralVO(tempVO, InOutFlag.OUT);
//  GeneralBillVO[] gbvoOut = pfVOConvert(new SpecialBillVO[]{tempVO},"4N","4I");
//  setRowNo(gbvoOut[0]);
//  setStatus(gbvoOut[0]);
//  alOutGeneralVO.add(gbvoOut[0]);
//  //置入库VO
//  tempVO = new SpecialBillVO();
//  tempVO.setParentVO(voTempBill.getHeaderVO());
//  Hashtable htByWh = new Hashtable();
//  for (int i = 1; i < voTempBill.getChildrenVO().length; i++) {
//    SpecialBillItemVO sbivonow = voTempBill.getItemVOs()[i];
//    String sWh = sbivonow.getCwarehouseid().trim();
//    if (!htByWh.containsKey(sWh)) {
//      tempItemVO = new SpecialBillItemVO[1];
//      tempItemVO[0] = sbivonow;
//      htByWh.put(sWh, tempItemVO);
//    } else {
//      SpecialBillItemVO[] oldVOs = (SpecialBillItemVO[]) htByWh.get(sWh);
//      tempItemVO = new SpecialBillItemVO[oldVOs.length + 1];
//      for (int j = 0; j < tempItemVO.length - 1; j++) {
//        tempItemVO[j] = oldVOs[j];
//      }
//      tempItemVO[oldVOs.length] = sbivonow;
//      htByWh.put(sWh, tempItemVO);
//    }
//  }
//  java.util.Enumeration enumKey = htByWh.keys();
//  Object obj;
//  while (enumKey.hasMoreElements()) {
//    obj = enumKey.nextElement();
//    tempItemVO = null;
//    tempItemVO = (SpecialBillItemVO[]) htByWh.get(obj);
//    tempVO.setChildrenVO(tempItemVO);
//    //GeneralBillVO gbvoIn = new GeneralBillVO();
//    //gbvoIn = changeFromSpecialVOtoGeneralVO(tempVO, InOutFlag.IN);
//    GeneralBillVO[] gbvoIn = pfVOConvert(new SpecialBillVO[]{tempVO},"4N","4A");
//    setRowNo(gbvoIn[0]);
//    setStatus(gbvoIn[0]);
//    alInGeneralVO.add(gbvoIn[0]);
//  }

  getAuditDlg().setVO(
    voTempBill,
    alInGeneralVO,
    alOutGeneralVO,
    m_sBillTypeCode,
    m_voBill.getPrimaryKey().trim(),
    m_sCorpID,
    m_sUserID);

  //不允许条码扫描超过实发数量 hanwei 2003-11-4
  //下面两项参数必须同时加入
  //设置调整的其他入和出不能超应发数量 add by hanwei 2004-6-5
  //在单据界面上扫描控制，不能超过应发数量 nc.ui.ic.pub.bill.GeneralBillClientUI.scanfixline_fix(BarCodeParseVO [], int, int, boolean)
  getAuditDlg().getChldClientUIIn().getBarcodeCtrl().setOverShouldNum(false);
  getAuditDlg().getChldClientUIOut().getBarcodeCtrl().setOverShouldNum(false);
  //在条码编辑框上扫描控制，不能超过实发数量
  getAuditDlg().getChldClientUIIn().getBarcodeCtrl().setModifyBillUINum(false);
  getAuditDlg().getChldClientUIOut().getBarcodeCtrl().setModifyBillUINum(false);

  getAuditDlg().setName("BillDlg");
  getAuditDlg().showModal();
  //if (ret == nc.ui.pub.beans.UIDialog.ID_OK) {
  if (getAuditDlg().isOK()) {
    try { //更新表尾
      setAuditBillFlag();
      filterNullLine();

      String sHPK=m_voBill.getPrimaryKey().trim();
      if (sHPK != null) {
      ArrayList alLastTs = qryLastTs(sHPK);
      freshTs(alLastTs);
      }

      m_alListData.set(m_iLastSelListHeadRow, m_voBill.clone());

      m_iFirstSelListHeadRow = -1;
      //switchListToBill();

      setButtonState();
      setBillState();
      showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
      "UC001-0000027")+ResBase.getSuccess()/* @res "审批成功" */);
      
    } catch (Exception e) {
      handleException(e);
      nc.ui.pub.beans.MessageDialog.showErrorDlg(this, null, e.getMessage());
    }
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
  
  //转换前与转换后行不能完全相同
  //应当已进行了非空校验
  SpecialBillItemVO[] bodyvos = m_voBill.getItemVOs();
  if(bodyvos==null || bodyvos.length<0 || bodyvos.length<2){
    showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
        "4008other", "UPP4008other-000510"));/* @res "表体行数必须大于1";*/
    return false;
  }
  Integer iFbillrowflag = null;
  String key = null;
  SpecialBillItemVO convBeforevo = null; 
  String convBeforeKey = null;
  int convBeforePos = -1;
  String[] fields = null;
  Integer isSupplierstock = null;//转换前行是否供应商管理存货
  for(int i=0;i<bodyvos.length;i++){
    if(nc.vo.ic.pub.GenMethod.isSEmptyOrNull(bodyvos[i].getCinventoryid())){
      showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
          "4008other", "UPP4008other-000511")); //"存货不能为空!"
      return false;
    }
    iFbillrowflag = bodyvos[i].getFbillrowflag();
    if(iFbillrowflag==null){
      showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
          "4008other", "UPP4008other-000512")); //"类型不能为空!"
      return false;
    }
    
    if(null != bodyvos[i].getM_Issupplierstock() && bodyvos[i].getM_Issupplierstock().intValue() == 1
    		&& (null == bodyvos[i].getCvendorid()||"".equals(bodyvos[i].getCvendorid()))){
        showErrorMessage(bodyvos[i].getCrowno()+nc.ui.ml.NCLangRes.getInstance().getStrByID(
                "4008other", "UPP4008other-000536")); //"行供应商不能为空!"
        return false;
      }
        
    if(iFbillrowflag.intValue()==BillRowType.afterConvert){
    	
//      if(convBeforePos<0){
//        showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
//            "4008other", "UPP4008other-000513")); //"在类型为[转换后]的行前面必须至少有一个[转换前]的行";
//        return false;
//      }
    

      key = SmartVOUtilExt.getKeysString(bodyvos[i], fields, null);
    //add by ouyangzhb 2011-05-10问题号：0000203: 要求修改形态转换单保存条件
//
//      if (key.equals(convBeforeKey)) {
//        showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec","UPP4008spec-000140")/*@res "转换前与转换后的 存货编码、辅计量单位、自由项、批次 至少有一项应不同..."*/);
//        //更改颜色为正常颜色
///*        SetColor.SetTableColor(
//          getBillCardPanel().getBillModel(),
//          getBillCardPanel().getBillTable(),
//          getBillCardPanel(),
//          new ArrayList(),
//          m_cNormalColor,
//          m_cNormalColor,
//          m_bExchangeColor,
//          m_bLocateErrorColor,
//          "");*/
//        nc.ui.ic.pub.tools.GenMethod.reSetRowColorWhenNOException(getBillCardPanel());
//        return false;
//      }
  	//add by ouyangzhb 2011-05-10 问题号：0000203: 要求修改形态转换单保存条件 end
      
    }else {
    	isSupplierstock = bodyvos[i].getM_Issupplierstock();
    	if(null != isSupplierstock && isSupplierstock.intValue() == 1)
      	  fields = new String[]{"cinventoryid","castunitid","vbatchcode","vfree0","cvendorid"};
        else
      	  fields = new String[]{"cinventoryid","castunitid","vbatchcode","vfree0"};
      if(i>0 && convBeforePos>=0){
        if(convBeforePos==i-1 || i==bodyvos.length-1){
          showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
              "4008other", "UPP4008other-000514"));//"在类型为[转换前]的行后面必须至少有一个[转换后]的行");
          return false;
        }
      }
      convBeforevo = bodyvos[i];
      convBeforePos = i;
      convBeforeKey = SmartVOUtilExt.getKeysString(convBeforevo, fields, null);
    }
    
  }
    
  
//  if (((Integer) m_voBill.getItemValue(0, "fbillrowflag")).intValue()
//    != BillRowType.beforeConvert) {
//    showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec","UPP4008spec-000139")/*@res "第一行必须输入转换前存货！"*/);
//    return false;
//  }
//
//  String key0 =
//    (m_voBill.getItemValue(0, "cinventoryid") == null
//      ? ""
//      : (m_voBill.getItemValue(0, "cinventoryid").toString().trim()))
//      + (m_voBill.getItemValue(0, "castunitid") == null
//        ? ""
//        : (m_voBill.getItemValue(0, "castunitid").toString().trim()))
//      + (m_voBill.getItemValue(0, "vbatchcode") == null
//        ? ""
//        : (m_voBill.getItemValue(0, "vbatchcode").toString().trim()))
//      + (m_voBill.getItemValue(0, "vfree0") == null
//        ? ""
//        : (m_voBill.getItemValue(0, "vfree0").toString().trim()));
//
//  String key = null;
//
//  for (int i = 1; i < m_voBill.getItemCount(); i++) {
//    key =
//      (m_voBill.getItemValue(i, "cinventoryid") == null
//        ? ""
//        : (m_voBill.getItemValue(i, "cinventoryid").toString().trim()))
//        + (m_voBill.getItemValue(i, "castunitid") == null
//          ? ""
//          : (m_voBill.getItemValue(i, "castunitid").toString().trim()))
//        + (m_voBill.getItemValue(i, "vbatchcode") == null
//          ? ""
//          : (m_voBill.getItemValue(i, "vbatchcode").toString().trim()))
//        + (m_voBill.getItemValue(i, "vfree0") == null
//          ? ""
//          : (m_voBill.getItemValue(i, "vfree0").toString().trim()));
//    if (key0.equals(key)) {
//      showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec","UPP4008spec-000140")/*@res "转换前与转换后的 存货编码、辅计量单位、自由项、批次 至少有一项应不同..."*/);
//      //更改颜色为正常颜色
//      SetColor.SetTableColor(
//        getBillCardPanel().getBillModel(),
//        getBillCardPanel().getBillTable(),
//        getBillCardPanel(),
//        new ArrayList(),
//        m_cNormalColor,
//        m_cNormalColor,
//        m_bExchangeColor,
//        m_bLocateErrorColor,
//        "");
//      return false;
//    }
//  }
  return super.checkVO();
}



































/**
  * 创建者：仲瑞庆
  * 功能：表体仓库改变事件处理
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
public void afterBodyWhEdit(nc.ui.pub.bill.BillEditEvent e) {
  //仓库
  try {
    String sName=
      ((nc.ui.pub.beans.UIRefPane) getBillCardPanel()
        .getBodyItem("cwarehousename")
        .getComponent())
        .getRefName();
    String sID=
      ((nc.ui.pub.beans.UIRefPane) getBillCardPanel()
        .getBodyItem("cwarehousename")
        .getComponent())
        .getRefPK();

    //保存名称以在列表形式下显示。
    nc.vo.scm.ic.bill.WhVO voWh=
      (nc.vo.scm.ic.bill.WhVO) SpecialBillHelper.queryInfo(new Integer(1), sID);

    if (m_voBill != null) {
      m_voBill.setBodyWh(e.getRow(), voWh);
      //清表尾现存量
      //m_voBill.clearInvQtyInfo();
      //清批次/自由项
      //String[] sIKs= getClearIDs(1, "cwarehousename");
      //int iRowCount= getBillCardPanel().getRowCount();
      //for (int row= 0; row < iRowCount; row++)
        //clearRowData(row, sIKs);
      //刷新现存量显示
      //setTailValue(0);

      //清表体行所有数据
      //for (int i= 0; i < getBillCardPanel().getRowCount(); i++) {
      //clearRowData(i);
      //}
      //以下的信息需要优化，如果批次号未显示，则无需显示。
      //showHintMessage("仓库修改，请重新确认自由项、批次、数量。");
    }

    if (m_voBill != null) {
      m_voBill.setItemValue(e.getRow(), "cwarehousename", sName);
      m_voBill.setItemValue(e.getRow(), "cwarehouseid", sID);
    }
    getBillCardPanel().setBodyValueAt(sName, e.getRow(), "cwarehousename");
    getBillCardPanel().setBodyValueAt(sID, e.getRow(), "cwarehouseid");
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
 * 
 */
public void afterLotEdit(nc.ui.pub.bill.BillEditEvent e) {
	//修改人：刘家清 修改日期：2007-8-30上午10:48:56 修改原因：转换后也要处理了
 // if (e.getRow() == 0) {//转换前需要检查批次是否正确
    String s = e.getKey();
//    WhVO whvo = m_voBill.getBodyWh(e.getRow());
    if (e.getRow() == 0) //转换前需要检查批次是否正确
    getLotRefbyHand(s);
    pickupLotRef(s);
    //同步改变m_voBill并入pickupValuefromLotNumbRef() 方法中。
  //}
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
  boolean bInventoryIdIsZero = false;
  if (iRow > 1
      && !sItemKey.equals("cinventorycode")
      && !sItemKey.equals("cwarehousename")
      && (getBillCardPanel().getBodyValueAt(iRow, "cinventoryid") == null)
      //|| (getBillCardPanel().getBodyValueAt(iRow, "cinventoryid").toString().trim().length() == 0)) {
      ||(bInventoryIdIsZero == true)) {
    bi.setEnabled(false);
    return;
  } else {
    bi.setEnabled(bi.isEdit());
  }


  if (m_voBill.getItemValue(iRow, "cinventoryid") != null
    && m_voBill.getItemValue(iRow, "cinventoryid").toString().trim().length() == 0)
    bInventoryIdIsZero = true;

  //当入点不为仓库或存货时，表体行无存货则禁止输入其他值
  if ((!sItemKey.equals("cinventorycode") && !sItemKey.equals("cwarehousename"))
    && (null == m_voBill.getItemValue(iRow, "cinventoryid")
      || bInventoryIdIsZero == true)) {
    bi.setEnabled(false);
    return;
  } else {
    bi.setEnabled(bi.isEdit());
  }

  //调用父类的方法。
  super.colEditableSet(sItemKey, iRow);
  //个性化设置

  if (sItemKey.equals("vbatchcode")) {
    if ((null != m_voBill.getItemValue(iRow, "isLotMgt"))
      && (Integer.valueOf(m_voBill.getItemValue(iRow, "isLotMgt").toString()).intValue()
        != 0)) {
      bi.setEnabled(true);
      if ((iRow == 0)) { //第一行，转换前,出库，需要选择批次号。
        String ColName =
          getBillCardPanel().getBillData().getBodyItem("vbatchcode").getName();
        getBillCardPanel().getBodyPanel().getTable().getColumn(ColName).setCellEditor(
          new BillCellEditor(getLotNumbRefPane()));
      } else { //转换后，入库，需要录入批次号。
        String ColName =
          getBillCardPanel().getBillData().getBodyItem("vbatchcode").getName();
        getBillCardPanel().getBodyPanel().getTable().getColumn(ColName).setCellEditor(
          new BillCellEditor(new UITextField()));

      }
    } else {
      bi.setEnabled(false);
    }
  } else if (sItemKey.equals("dvalidate")) {
    if ((null != m_voBill.getItemValue(iRow, "isValidateMgt"))
      && (Integer
        .valueOf(m_voBill.getItemValue(iRow, "isValidateMgt").toString())
        .intValue()
        != 0)) {
      if ((iRow == 0)) { //第一行，转换前，出库，批次带出失效日期。
        bi.setEnabled(false);
      } else { //转换后，需要录入
        bi.setEnabled(true);
      }
    } else {
      bi.setEnabled(false);
    }
  } else if (sItemKey.equals("scrq")) {
    if ((null != m_voBill.getItemValue(iRow, "isValidateMgt"))
      && (Integer
        .valueOf(m_voBill.getItemValue(iRow, "isValidateMgt").toString())
        .intValue()
        != 0)) {
      if (iRow == 0) { //第一行，转换前，出库，批次带出生产日期。
        bi.setEnabled(false);
      } else { //转换后，需要录入
        bi.setEnabled(true);
      }
    } else {
      bi.setEnabled(false);
    }
  }
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
public void afterInvEdit(Object value,int row) {


  //nc.vo.scm.pub.SCMEnv.out("inv chg");
  try {
    setTailValue(null);
    //如果清除存货编码则清掉此行,并去掉表尾显示
    if ((value == null) || (value.toString().trim().length() == 0)) {
      clearRowData(row);
      //表尾

    } else {
      String sTempID1 =(String)value;
        //((nc.ui.pub.beans.UIRefPane) getBillCardPanel()
          //.getBodyItem("cinventorycode")
          //.getComponent())
          //.getRefPK();
      String sTempID2 = null;
      if (getBillCardPanel().getHeadItem("coutwarehouseid") != null)
        sTempID2 = getBillCardPanel().getHeadItem("coutwarehouseid").getValue();
      ArrayList alIDs = new ArrayList();
      alIDs.add(sTempID2);
      alIDs.add(sTempID1);
      alIDs.add(m_sUserID);
      alIDs.add(m_sCorpID);

      InvVO voInv =
        (InvVO)SpecialBillHelper.queryInfo(new Integer(QryInfoConst.INV), alIDs);
      
      Integer fbillrowflag = CheckTools.toInteger(getBillCardPanel().getBodyValueAt(row, "fbillrowflag"));
      
      if(fbillrowflag!=null)
        voInv.setAttributeValue("fbillrowflag", fbillrowflag);
      else
        voInv.setAttributeValue("fbillrowflag", new Integer(BillRowType.afterConvert));
      
//      if (row == 0) {
//        voInv.setAttributeValue("fbillrowflag", new Integer(BillRowType.beforeConvert));
//        //invsetparttype
//      } else if (row == 1) {
//        voInv.setAttributeValue("fbillrowflag", new Integer(BillRowType.afterConvert));
//      }
        
      m_voBill.setItemInv(row, voInv);
      //表体
      setBodyInvValue(row, voInv);
      //表尾
      //setTailValue(voInv);
      //清批次
      String[] sIKs = getClearIDs(1, "cinventorycode");
      //金额，计划金额会自动清吗？
      clearRowData(row, sIKs);
      //以下的信息需要优化，如果批次号未显示，则无需显示。
      showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec","UPP4008spec-000141")/*@res "存货修改，请重新确认自由项、批次、数量。"*/);
    }
  } catch (Exception e2) {
    nc.vo.scm.pub.SCMEnv.error(e2);
  }
}

/**
* 创建者：仲瑞庆
* 功能：对形转单,初始化新增状态
* 参数：
* 返回：
* 例外：
* 日期：(2001-7-4 下午 5:48)
* 修改日期，修改人，修改原因，注释标志：
*/
protected void initShowStatus() {

  if (getBillCardPanel().getRowCount() > 0) {
    setShowStatus(0, BillRowType.beforeConvert);

    for (int i = 1; i < m_voBill.getItemCount(); i++) {
      setShowStatus(i, BillRowType.afterConvert);

    }
  }
}

/**
 * 创建者：仲瑞庆
 * 功能：由传入的单据类型、字段，获得当该字段改变后，应改变的其他字段列表
 * 参数：iBillFlag 单据类型，当为普通单据，传入0，当为特殊单据，传入1
        已有
        存货          cinventorycode，
        表体仓库      cwarehousename，
        自由项       vfree0，
        表头出库仓库  coutwarehouseid，
        表头仓库      cwarehouseid
 * 返回：
 * 例外：
 * 日期：(2001-7-18 上午 9:20)
 * 修改日期，修改人，修改原因，注释标志：
 * @return java.lang.String[]
 * @param sWhatChange java.lang.String
 * 
 */
/*protected String[] getClearIDs(int iBillFlag, String sWhatChange) {
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
    sReturnString = new String[3];
    sReturnString[0] = "vbatchcode";
    sReturnString[1] = "scrq";
    sReturnString[2] = "dvalidate";
    //showWarningMessage("请重新确认批次号！");
    //修改人：刘家清 修改日期：2007-9-3下午01:16:04 修改原因：
    //return null;
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
}*/

/**
 * ClientUI 构造子注解。
 * nc 2.2 提供的单据联查功能构造子。
 *
 */
public ClientUI(
  String pk_corp,
  String billType,
  String businessType,
  String operator,
  String billID) {
  super();
  //初始化界面
  initialize(pk_corp, operator, "jc", businessType, "0001", "2003-04-17");
  //查单据
  SpecialBillVO voBill =
    qryBill(pk_corp, billType, businessType, operator, billID);
  if (voBill == null)
    nc.ui.pub.beans.MessageDialog.showHintDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec","UPPSCMCommon-000270")/*@res "提示"*/, nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec","UPP4008spec-000121")/*@res "没有符合查询条件的单据！"*/);
  else //显示单据
    setBillValueVO(voBill);

}

/**
 * 编辑前处理。
 * 创建日期：(2001-3-23 2:02:27)
 * @param e ufbill.BillEditEvent
 */
public boolean beforeEdit(nc.ui.pub.bill.BillEditEvent e) {
  nc.vo.scm.pub.ctrl.GenMsgCtrl.printHint("beforeEdit:" + e.getKey());
  String sItemKey = e.getKey();
  nc.ui.pub.bill.BillItem bi = getBillCardPanel().getBillData().getBodyItem(sItemKey);

  boolean isEditable = true;
  int iRow = e.getRow();

  //调用父类的方法。
  isEditable = super.beforeEdit(e);

  if (isEditable) {

    //个性化设置

    //zhy2007-04-12特殊单据的批次号档案显示也允许参照批次档案
    if (sItemKey.equals("vbatchcode")) {
      String ColName = getBillCardPanel().getBillData().getBodyItem("vbatchcode").getName();
      getBillCardPanel().getBodyPanel().getTable().getColumn(ColName).setCellEditor(
        new BillCellEditor(getLotNumbRefPane()));
      WhVO wvo = m_voBill.getBodyWh(iRow);
      getLotNumbRefPane().setParameter(wvo, m_voBill.getItemInv(iRow));
    }else
//    if (sItemKey.equals("vbatchcode")) {
//
//      if ((iRow == 0)) { //第一行，转换前,出库，需要选择批次号。
//        String ColName = getBillCardPanel().getBillData().getBodyItem("vbatchcode").getName();
//        getBillCardPanel().getBodyPanel().getTable().getColumn(ColName).setCellEditor(
//          new BillCellEditor(getLotNumbRefPane()));
//        WhVO wvo = m_voBill.getBodyWh(0);
//        getLotNumbRefPane().setParameter(wvo, m_voBill.getItemInv(0));
//      } else { //转换后，入库，需要录入批次号。
//        UITextField uft=new UITextField();
//        uft.setMaxLength(getBillCardPanel().getBodyItem("vbatchcode").getLength());
//        String ColName = getBillCardPanel().getBillData().getBodyItem("vbatchcode").getName();
//        getBillCardPanel().getBodyPanel().getTable().getColumn(ColName).setCellEditor(
//          new BillCellEditor(uft));
//      }
//
//    } else 
      if (sItemKey.equals("dvalidate") || sItemKey.equals("scrq")) {

      if ((iRow == 0)) { //第一行，转换前，出库，批次带出失效日期。
        bi.setEnabled(false);
      }

    }
  }

  bi.setEnabled(isEditable);
  return isEditable;

}

/**
 * 创建者：仲瑞庆
 * 功能: 转换为出入库单表头
 * 参数：
 * 返回：
 * 例外：
 * 日期：(2001-8-16 12:53:03)
 * 修改日期，修改人，修改原因，注释标志：
 * @param gvo nc.vo.ic.pub.bill.GeneralBillVO
 * @param svo nc.vo.ic.pub.bill.SpecialBillVO
 * @param iInOutFlag int
 */
protected GeneralBillHeaderVO changeFromSpecialVOtoGeneralVOAboutHeader(
  GeneralBillVO gvo,
  SpecialBillVO svo,
  int iInOutFlag) {

  //取表体第一行的仓库属性，前提是传入的特殊单表体只有一个仓库
  gvo.setWh(svo.getBodyWh(0));

  GeneralBillHeaderVO voBillHeader =
    super.changeFromSpecialVOtoGeneralVOAboutHeader(gvo, svo, iInOutFlag);

  //个性化处理
  SpecialBillHeaderVO voSpHeader = svo.getHeaderVO();
  voBillHeader.setCdptid(voSpHeader.getCoutdeptid());
  voBillHeader.setCdptname(voSpHeader.getCoutdeptname());
  voBillHeader.setCbizid(voSpHeader.getCoutbsor());
  voBillHeader.setCbizname(voSpHeader.getCoutbsorname());

  return voBillHeader;
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

//  GeneralBillItemVO voItem =
    super.changeFromSpecialVOtoGeneralVOAboutItem(gbvo, sbvo, iInOutFlag, j);

  if (iInOutFlag == InOutFlag.OUT) {
    gbvo.setItemValue(j, "nshouldoutnum", sbvo.getItemValue(j, "dshldtransnum"));
    gbvo.setItemValue(
      j,
      "nshouldoutassistnum",
      sbvo.getItemValue(j, "nshldtransastnum"));
    gbvo.setItemValue(j, "noutnum", sbvo.getItemValue(j, "dshldtransnum"));
    gbvo.setItemValue(j, "noutassistnum", sbvo.getItemValue(j, "nshldtransastnum"));
  } else {
    gbvo.setItemValue(j, "nshouldinnum", sbvo.getItemValue(j, "dshldtransnum"));
    gbvo.setItemValue(
      j,
      "nneedinassistnum",
      sbvo.getItemValue(j, "nshldtransastnum"));
    gbvo.setItemValue(j, "ninnum", sbvo.getItemValue(j, "dshldtransnum"));
    gbvo.setItemValue(j, "ninassistnum", sbvo.getItemValue(j, "nshldtransastnum"));
  }
  return (GeneralBillItemVO) gbvo.getItemVOs()[j];
}

protected QueryDlgHelpForSpec getQueryHelp() {
  if(this.m_queryHelp==null){
    this.m_queryHelp = new QueryDlgHelpForSpec(this){
      protected void init() {
        super.init();
        getQueryDlg().setLogFields(new String[]{
            "qbillstatus",
            "boutnumnull",
            "freplenishflag",
            "dbilldate.from",
            "dbilldate.end",
            "head.pk_calbody",
            "pk_calbody",
            "head.cwarehouseid",
            "cwarehouseid",
            "pk_corp",
            "head.pk_corp",
            "coutwarehouseid",
            "cinwarehouseid",
            "coutdeptid",
            "cindeptid"
        });
        getQueryDlg().setCombox("qbillstatus", new String[][] { { "1", nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec","UPP4008spec-000128")/*@res "制单"*/ }, {
          "0", nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UC001-0000027")/*@res "审批"*/ }, {
          "2", nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec","UPPSCMCommon-000217")/*@res "全部"*/ }
        });
        
      }
    };
  }
  return this.m_queryHelp;
}

/**
 * 返回 QueryConditionClient1 特性值。
 * @return nc.ui.pub.query.QueryConditionClient
 */
//protected QueryConditionDlgForBill getConditionDlg() {
//  if (ivjQueryConditionDlg == null) {
//    ivjQueryConditionDlg= new QueryConditionDlgForBill(this);
//    //ivjQueryConditionDlg.setDefaultCloseOperation(ivjQueryConditionDlg.HIDE_ON_CLOSE);
//    //getConditionDlg().setTempletID(m_sCorpID, m_sPNodeCode, m_sUserID, null);
//    ivjQueryConditionDlg.setTempletID(m_sCorpID, m_sPNodeCode, m_sUserID, null);
//
//    //以下为对参照的初始化
//    ivjQueryConditionDlg.initQueryDlgRef();
//    //以下为对公司参照的初始化
//    ArrayList alCorpIDs= new ArrayList();
//    try {
//      alCorpIDs=
//        (ArrayList) SpecialBillHelper.queryInfo(
//          new Integer(QryInfoConst.USER_CORP),
//          m_sUserID);
//    } catch (Exception e) {
//      nc.vo.scm.pub.SCMEnv.error(e);
//    }
//    //设置单据日期为当前登录日期
////    ivjQueryConditionDlg.setInitDate("dbilldate", m_sLogDate);
//  //modified by liuzy 2008-04-01 v5.03需求：库存单据查询增加起止日期
//	ivjQueryConditionDlg.setInitDate("dbilldate.from", m_sLogDate);
//	ivjQueryConditionDlg.setInitDate("dbilldate.end", m_sLogDate);
//    ivjQueryConditionDlg.initCorpRef("pk_corp", m_sCorpID, alCorpIDs);
//
//    ivjQueryConditionDlg.setCombox("qbillstatus", new String[][] { { "1", nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec","UPP4008spec-000128")/*@res "制单"*/ }, {
//        "0", nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UC001-0000027")/*@res "审批"*/ }, {
//        "2", nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec","UPPSCMCommon-000217")/*@res "全部"*/ }
//    });
//
//  }
//  return ivjQueryConditionDlg;
//}

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
    initVariable();
    super.initialize(
      pk_corp,
      sOperatorid,
      sOperatorname,
      sBiztypeid,
      sGroupid,
      sLogDate);
	// 修改人：刘家清 修改日期：2007-12-26上午11:05:02 修改原因：右键增加"重排行号"
	UIMenuItem[] oldUIMenuItems = getBillCardPanel().getBodyMenuItems();
	if (oldUIMenuItems.length > 0) {
		ArrayList<UIMenuItem> newMenuList = new ArrayList<UIMenuItem>();
		for (UIMenuItem oldUIMenuItem : oldUIMenuItems)
			newMenuList.add(oldUIMenuItem);
		newMenuList.add(getAddNewRowNoItem());
    newMenuList.add(getLineCardEditItem());
		getAddNewRowNoItem().removeActionListener(this);
		getAddNewRowNoItem().addActionListener(this);
    
    getLineCardEditItem().removeActionListener(this);
    getLineCardEditItem().addActionListener(this);
    
		UIMenuItem[] newUIMenuItems = new UIMenuItem[newMenuList.size()];
		m_Menu_AddNewRowNO_Index = newMenuList.size() - 1;
		newMenuList.toArray(newUIMenuItems);
		// getBillCardPanel().setBodyMenu(newUIMenuItems);
		getBillCardPanel().getBodyPanel().setMiBody(newUIMenuItems);
		getBillCardPanel().getBodyPanel().setBBodyMenuShow(true);
		getBillCardPanel().getBodyPanel().addTableBodyMenu();

	}
    setSortEnable(true, false, false);
    setSortEnable(false, false, false);
    initOther();
  } catch (java.lang.Throwable ivjExc) {
    handleException(ivjExc);
  }

}

/**
 * 李俊
 * 功能：
 * 参数：
 * 返回：
 * 例外：
 * 日期：(2005-1-14 15:13:27)
 * 修改日期，修改人，修改原因，注释标志：
 *
 * @return nc.vo.ic.pub.bill.GeneralBillVO[]
 * @param voSp nc.vo.ic.pub.bill.SpecialBillVO[]
 */
private GeneralBillVO[] pfVOConvert(
  SpecialBillVO[] voSp,
  String sSrcBillType,
  String sDesBillType) {
  GeneralBillVO[] gbvo = null;
  try {
    gbvo =
      (GeneralBillVO[]) nc.ui.pub.change.PfChangeBO_Client.pfChangeBillToBillArray(
        voSp,
        sSrcBillType,
        sDesBillType);
  } catch (Exception e) {
    nc.vo.scm.pub.SCMEnv.error(e);
  }
   return gbvo;
}

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

  SpecialBillVO voResult =
    super.qryBill(pk_corp, billType, businessType, operator, billID);
  //置表头的成套件
  if (voResult != null
    && voResult.getHeaderVO() != null
    && voResult.getItemVOs() != null) {
    //得到第一张单据表体
//    SpecialBillHeaderVO voHead = voResult.getHeaderVO();
    SpecialBillItemVO[] voItems = voResult.getItemVOs();

    if (voItems != null && voItems.length > 0) {
      int iLen = voItems.length;
      for (int i = 0; i < iLen; i++) {
        if (voItems[i].getFbillrowflag() != null) {
          if (voItems[i].getFbillrowflag().intValue() == BillRowType.beforeConvert)
            voItems[i].setInvsetparttype(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec","UPP4008spec-000142")/*@res "转换前"*/);
          else
            voItems[i].setInvsetparttype(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec","UPP4008spec-000143")/*@res "转换后"*/);
        }
      }
    }

  }
  return voResult;
}

/**
 * 此处插入方法说明。
   存货参照多选置界面
   供形态转换单等子类重载
 * 创建日期：(2003-11-11 20:48:18)
 * @param invVOs nc.vo.scm.ic.bill.InvVO[]
 */
public void setBodyInVO(InvVO[] invVOs, int iBeginRow) {

  int iCurRow = 0;
  Integer iStatus = new Integer(BillRowType.afterConvert);
//  Integer iBeforeConvert = new Integer(BillRowType.beforeConvert);
//  Integer iAfterConvert = new Integer(BillRowType.afterConvert);
  getBillCardPanel().getBillModel().setNeedCalculate(false);
  Object value = null;
  for (int i = 0; i < invVOs.length; i++) {
    iCurRow = iBeginRow + i;
//    if (iCurRow > 0)
//      iStatus = iAfterConvert;
//    else
//      iStatus = iBeforeConvert;
    
    value = getBillCardPanel().getBodyValueAt(iCurRow, "fbillrowflag");
    
    if(nc.vo.ic.pub.GenMethod.isOEmptyOrNull(value)){

      setShowStatus(iCurRow, iStatus.intValue());
  
      invVOs[i].setAttributeValue("fbillrowflag", iStatus);
    
    }else{
      
      iStatus = CheckTools.toInteger(value); //(Integer)GenMethod.getICCItemValueByName(this.comInvsetparttype,value.toString());
      
      setShowStatus(iCurRow, iStatus.intValue());
      
      invVOs[i].setAttributeValue("fbillrowflag", iStatus);
    }

    m_voBill.setItemInv(iCurRow, invVOs[i]);
    //表体
    setBodyInvValue(iCurRow, invVOs[i]);
    //表尾
    //setTailValue(invVOs[i]);
    //清批次
    String[] sIKs = getClearIDs(1, "cinventorycode");
    //金额，计划金额会自动清吗？
    clearRowData(iCurRow, sIKs);
  }

  getBillCardPanel().getBillModel().setNeedCalculate(true);
  getBillCardPanel().getBillModel().reCalcurateAll();

}

/**
 * 程起伍
 * 功能：
 * 参数：
 * 返回：
 * 例外：
 * 日期：(2005-1-18 14:45:22)
 * 修改日期，修改人，修改原因，注释标志：
 *
 * @param voBill nc.vo.ic.pub.bill.GeneralBillVO
 */
private void setRowNo(GeneralBillVO voBill) {

  if (voBill == null||voBill.getChildrenVO().length<=0) return;
  int len = voBill.getChildrenVO().length;
  for (int i=1;i<=len;i++){
    voBill.getChildrenVO()[i-1].setAttributeValue("crowno",(new Integer(i)).toString());
  }

}

/**
* 创建者：仲瑞庆
* 功能：对形转单,初始化新增状态
* 参数：
* 返回：
* 例外：
* 日期：(2001-7-4 下午 5:48)
* 修改日期，修改人，修改原因，注释标志：
*/
protected void setShowStatus(int irow, int status) {

  if (m_voBill != null
    && m_voBill.getChildrenVO() != null
    && irow < m_voBill.getChildrenVO().length) {
    m_voBill.setItemValue(irow, "fbillrowflag", new Integer(status));
    getBillCardPanel().setBodyValueAt(new Integer(status), irow, "fbillrowflag");
    if (status == BillRowType.beforeConvert) {
      m_voBill.setItemValue(irow, "invsetparttype", nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec","UPP4008spec-000142")/*@res "转换前"*/);
      getBillCardPanel().setBodyValueAt(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec","UPP4008spec-000142")/*@res "转换前"*/, irow, "invsetparttype");
    } else {
      m_voBill.setItemValue(irow, "invsetparttype", nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec","UPP4008spec-000143")/*@res "转换后"*/);
      getBillCardPanel().setBodyValueAt(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec","UPP4008spec-000143")/*@res "转换后"*/, irow, "invsetparttype");
    }

  }
}

/**
 * 程起伍
 * 功能：
 * 参数：
 * 返回：
 * 例外：
 * 日期：(2005-1-22 18:01:56)
 * 修改日期，修改人，修改原因，注释标志：
 *
 * @param voBill nc.vo.ic.pub.bill.GeneralBillVO
 */
public void setStatus(GeneralBillVO voBill) {

  if (voBill == null) return;
  voBill.setStatus(nc.vo.pub.VOStatus.NEW);
  for (int i=0;i<voBill.getChildrenVO().length;i++){
    voBill.getChildrenVO()[i].setStatus(nc.vo.pub.VOStatus.NEW);
  }

}

public boolean isCellEditable(boolean value, int row, String itemkey) {
  if (m_iMode == BillMode.Browse) return false;
  if("invsetparttype".equals(itemkey))
    return true;
  return super.isCellEditable(value, row, itemkey);
}



/**
 *费用录入按钮的事件响应方法
 * Meichao
 *2010-11-08 16:58
 */
private void onBoExpenseInput() {
	
	
	InformationCostVO[] vos = (InformationCostVO[] )getBillCardPanel().getBillModel("jj_scm_informationcost").getBodyValueVOs(InformationCostVO.class.getName());
	ArrayList voList = new ArrayList();
	for (int i = 0; i < vos.length; i++) {
		if(vos[i] != null){
			voList.add(vos[i]);
		}
	}
	InfoCostPanel c = null;
	if(voList.size()!=0&&voList!=null){
		InformationCostVO[] vos1 = new InformationCostVO[voList.size()];
		voList.toArray(vos1);
		c = new InfoCostPanel(getBillCardPanel(),vos1);
	}
	else 			
	c = new InfoCostPanel(getBillCardPanel());
	// 打开费用录入界面
	c.showModal();
	// 当费用录入界面关闭时,将录入的数据存放到费用信息页签上
	if (c.isCloseOK()) {
		InformationCostVO[] infoCostVOs = c.getInfoCostVOs();
		if (infoCostVOs != null && infoCostVOs.length != 0){
			// 当费用录入界面的vo数组不为空时,将vo存到费用录入页签上
					getBillCardPanel().getBillData().setBodyValueVO(
						"jj_scm_informationcost", infoCostVOs);
				getBillCardPanel().getBillModel("jj_scm_informationcost").execLoadFormula();
			}	
		
	}
}
/**
 * 
 * (设置费用录入按钮的可见/可用状态)
 *  MeiChao  补充注释  2010-11-08  
 * @see nc.ui.ic.pub.bill.GeneralBillClientUI#setExtendBtnsStat(int)
 * 
 */
	public void setExpenseInputButtonStat(int iState) {
		switch (iState) {
		case BillMode.New:
			getButtonExpenseInput().setEnabled(true);
			break;
		case BillMode.Update:
			getButtonExpenseInput().setEnabled(true);
			break;
		case BillMode.Browse:
			getButtonExpenseInput().setEnabled(false);
			break;
		default:
			getButtonExpenseInput().setEnabled(false);
			break;
		}
	}



}