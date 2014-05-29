package nc.ui.ps.estimate;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.JComponent;
import javax.swing.ListSelectionModel;

import nc.itf.scm.cenpur.service.ChgDataUtil;
import nc.ui.bd.ref.AbstractRefModel;
import nc.ui.ic.pub.lot.LotNumbRefPane;
import nc.ui.ic.service.IICPub_LotNumbRefPane;
import nc.ui.ml.NCLangRes;
import nc.ui.po.pub.PoQueryCondition;
import nc.ui.ps.pub.SpeBiztypeDef;
import nc.ui.ps.pub.WarehouseRef;
import nc.ui.pu.pub.POPubSetUI2;
import nc.ui.pu.pub.PuTool;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIButton;
import nc.ui.pub.beans.UIComboBox;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UILabel;
import nc.ui.pub.beans.UIRadioButton;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.beans.UITable;
import nc.ui.pub.beans.UITextField;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.bill.BillData;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillEditListener;
import nc.ui.pub.bill.BillEditListener2;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillListData;
import nc.ui.pub.bill.BillListPanel;
import nc.ui.pub.bill.BillModel;
import nc.ui.pub.bill.BillMouseEnent;
import nc.ui.pub.bill.BillTableMouseListener;
import nc.ui.pub.bill.DefaultCurrTypeBizDecimalListener;
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
import nc.ui.pub.print.IDataSource;
import nc.ui.scm.ic.freeitem.DefHelper;
import nc.ui.scm.ic.freeitem.FreeItemRefPane;
import nc.ui.scm.pub.def.DefSetTool;
import nc.ui.scm.pub.print.ISetBillVO;
import nc.ui.scm.pub.print.ScmPrintTool;
import nc.ui.scm.pub.report.BillRowNo;
import nc.vo.ps.estimate.EstimateVO;
import nc.vo.ps.estimate.GeneralBb3VO;
import nc.vo.ps.estimate.GeneralHHeaderVO;
import nc.vo.ps.estimate.GeneralHItemVO;
import nc.vo.ps.estimate.GeneralHVO;
import nc.vo.ps.settle.OorderVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.ValidationException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.para.SysInitVO;
import nc.vo.pub.query.ConditionVO;
import nc.vo.querytemplate.TemplateInfo;
import nc.vo.scm.cenpur.service.ChgDocPkVO;
import nc.vo.scm.cenpur.service.ChgPriceMnyVO;
import nc.vo.scm.constant.ScmConst;
import nc.vo.scm.ic.bill.FreeVO;
import nc.vo.scm.ic.bill.InvVO;
import nc.vo.scm.ic.bill.WhVO;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.scm.pub.SCMEnv;
import nc.vo.scm.pub.excp.RwtIcToPoException;
import nc.vo.scm.service.ServcallVO;

/**
 * 功能描述:期初暂估入库单 作者: 创建日期: 修改：2004-09-10 袁野
 * 
 * @modify: since v502, 支持分收集结模式下的期初订单生成期初暂估入库单，但此入库单归属采购公司
 */
public class InitialUI extends nc.ui.pub.ToftPanel implements ActionListener,
    BillEditListener, BillEditListener2, BillTableMouseListener, IDataSource,
    javax.swing.event.ListSelectionListener, IBillRelaSortListener2,
    ILinkMaintain,// 关联修改
    ILinkAdd,// 关联新增
    ILinkApprove,// 审批流
    ILinkQuery,// 逐级联查
    ISetBillVO

{
  // since v502, 支持订单数据排序
  class IBillRelaSortListener2Impl implements IBillRelaSortListener2 {

    public Object[] getRelaSortObjectArray() {
      return m_orderVOs;
    }

  }

  // since v53,支持列表联查
  ButtonObject m_btnListLinkQuery = new ButtonObject(NCLangRes.getInstance()
      .getStrByID("scmcommon", "UPPSCMCommon-000145")/*
                                                       * @res "联查"
                                                       */, NCLangRes.getInstance().getStrByID("scmcommon", "UPPSCMCommon-000145")/*
                                                                               * @res
                                                                               * "联查"
                                                                               */, "联查");

  // 界面控制按钮
  private ButtonObject m_buttons1[] = null;

  private ButtonObject m_buttons2[] = null;

  private ButtonObject m_buttons3[] = null;

  // 按钮状态：0 正常；1 置灰；2 不可视
  private int m_nButtonState[] = null;

  // 界面状态
  private int m_nUIState = 1;

  // 单据
  private BillCardPanel m_billPanel1 = null;

  private BillCardPanel m_billPanel2 = null;

  private BillListPanel m_listPanel = null;

  // 单位编码，系统应提供方法获取
  private String m_sUnitCode = getCorpPrimaryKey();

  // 缓存
  private int m_nPresentRecord = 0;

  // 期初入库单
  private GeneralHVO m_VOs[] = null;

  private OorderVO m_orderVOs[] = null;

  // 分单方式
  private int m_nMode = 0;

  private UIDialog m_dialog = null;

  private UIRadioButton m_rb1 = null;

  private UIRadioButton m_rb2 = null;

  private UIRadioButton m_rb3 = null;

  private UIRadioButton m_rb4 = null;

  private UIButton m_bnOK = null;

  private UIButton m_bnCancel = null;

  // 是否增加期初暂估入库单
  private boolean m_bAdd = false;

  // 期初暂估入库单子表及子子表是否查询
  // private UFBoolean m_bBodyQuery[] = null;
  // 是否订单生成入库单
  private boolean m_bGenerate = false;

  // 库存是否启用
  private boolean m_bICStartUp = false;

  // 已加锁
  private boolean m_bSettling = false;

  // 只用于设置按钮 {this.setButtons()}
  private ButtonObject m_buttons11[] = null;

  private ButtonObject m_buttons31[] = null;

  // 查询条件
  private InitialUIQryDlg m_condClient1 = null;

  private PoQueryCondition m_condClient2 = null;
  
  //新查询模板
  //V55 by zhaoyha at 2008.10.15
  private InitialQueryOrderDlg m_condQrderDlg = null;

  // 存货属性解析
  private nc.ui.scm.pub.InvoInfoBYFormula m_formula = new nc.ui.scm.pub.InvoInfoBYFormula();

  // 自由项
  private FreeItemRefPane m_freeItem = null;

  // 订单生成的期初入库单
  private GeneralHVO m_gVOs[] = null;

  private SysInitVO m_initVO = null;

  // 采购金额小数位(本位币小数位)
  private int m_moneyDecimal = 2;

  private int m_nRestoreState[] = null;

  // 采购单价小数位
  private int m_priceDecimal = 2;

  // 列表打印
  private ScmPrintTool m_printList = null;

  private ScmPrintTool m_printCard = null;

  private UIRadioButton m_rb5 = null;

  // 来源是订单的入库单的业务类型集合
  private String m_sBiztypeID[] = null;

  // 部门ID缓存
  private String m_sDeptID = null;

  // 暂估单价来源
  private String m_sEstPriceSource = null;

  // 主计量小数位
  private int m_unitDecimal = 2;

  // 辅计量小数位
  private int m_unitDecimalAssist = 2;

  // 系统初始化参数 "BD501","BD505","BD301"
  private int measure[] = null;

  // 采购系统启用日期
  private UFDate m_dSystem = null;

  // 是否暂估应付
  private UFBoolean m_bZGYF = new UFBoolean(false);

  // zhf add
  private POPubSetUI2 m_cardPoPubSetUI2 = null;

  // 本币币种
  private String m_sCurrTypeID = null;

  // end

  /**
   * OrderUI 构造子注解。
   */
  public InitialUI() {
    super();
    init();
  }

  /**
   * 此处插入方法说明。 功能描述: 输入参数: 返回值: 异常处理:
   * 
   * @param event
   *          nc.ui.pub.beans.UIDialogEvent
   */
  public void actionPerformed(ActionEvent event) {
    UIButton button = (UIButton) event.getSource();

    if (button == m_bnOK) {
      if (m_rb1.isSelected()) {
        m_nMode = 0;
        m_initVO.setValue("供应商");
      }
      if (m_rb2.isSelected()) {
        m_nMode = 1;
        m_initVO.setValue("存货+供应商");
      }
      if (m_rb3.isSelected()) {
        m_nMode = 2;
        m_initVO.setValue("订单");
      }
      if (m_rb4.isSelected()) {
        m_nMode = 3;
        m_initVO.setValue("仓库+供应商");
      }
      if (m_rb5.isSelected()) {
        m_nMode = 4;
        m_initVO.setValue("订单+仓库");
      }

      m_dialog.closeOK();
    }
    else {
      m_dialog.closeCancel();
      return;
    }

    // 重新设置系统的分单方式
    SysInitVO tempVOs[] = new SysInitVO[1];
    tempVOs[0] = new SysInitVO();
    tempVOs[0] = (SysInitVO) m_initVO.clone();
    tempVOs[0].setPk_corpid(m_sUnitCode);
    try {
      SysInitBO_Client.saveSysInitVOs(tempVOs);
    }
    catch (Exception e) {
      MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes.getInstance()
          .getStrByID("40040503", "UPP40040503-000040")/* @res "重新设置分单方式" */,
          e.toString());
      return;
    }
    return;
  }

  private POPubSetUI2 getCardPoPubSetUI2() {
    if (m_cardPoPubSetUI2 == null) {
      m_cardPoPubSetUI2 = new POPubSetUI2();
    }
    return m_cardPoPubSetUI2;
  }

  /**
   * 为表体数据设置汇率精度 原币金额业务精度
   * 
   * @author zhanghongfang
   * @time 2008-7-29 下午07:03:39
   */

  private void setBodyDigits() {
    OorderVO[] VOs = null;
    VOs = (OorderVO[]) getBillCardPanel2().getBillData().getBodyValueVOs(
        "nc.vo.ps.settle.OorderVO");
    int row = 0;

    ArrayList<String> currIdList = new ArrayList<String>();
    String sCurrId = null;

    for (OorderVO vo : VOs) {
      sCurrId = PuPubVO.getString_TrimZeroLenAsNull(vo.getCurrencytypeid());
      if (sCurrId == null) {
        continue;
      }
      if (!currIdList.contains(sCurrId)
          && !sCurrId.equalsIgnoreCase(m_sCurrTypeID)) {
        currIdList.add(sCurrId);
      }
    }
    // 获取币种对应业务精度
    HashMap<String, Integer> mnyDigitMap = new HashMap<String, Integer>();
    if (currIdList != null && currIdList.size() > 0) {
      String[] currIds = currIdList.toArray(new String[0]);
      int[] iMnyDigits = getCardPoPubSetUI2().getMoneyDigitByCurr_Busi_Batch(
          currIds);
      if (currIds.length != iMnyDigits.length)
        return;
      for (int i = 0; i < currIds.length; i++) {
        mnyDigitMap.put(currIds[i], new Integer(iMnyDigits[i]));
      }
    }

    for (OorderVO vo : VOs) {
      sCurrId = vo.getCurrencytypeid() == null ? "" : vo.getCurrencytypeid()
          .trim();
      int iaExchRateDigit = getDigits_ExchangeRate(sCurrId) == null ? 5
          : getDigits_ExchangeRate(sCurrId)[0];
      getBillCardPanel2().getBillModel().getItemByKey("nexchangeotobrate")
          .setDecimalDigits(iaExchRateDigit);
      getBillCardPanel2().getBillModel().setValueAt(
          m_orderVOs[row].getNexchangeotobrate(), row, "nexchangeotobrate");

      int iMnyDigit = PuPubVO.getInteger_NullAs(
          mnyDigitMap.get(vo.getCurrencytypeid()), new Integer(m_moneyDecimal))
          .intValue();

      getBillCardPanel2().getBillModel().getItemByKey("noriginaltaxpricemny")
          .setDecimalDigits(iMnyDigit);

      getBillCardPanel2().getBillModel().getItemByKey("noriginalcurmny")
          .setDecimalDigits(iMnyDigit);
      getBillCardPanel2().getBillModel().getItemByKey("norgtaxmoney")
          .setDecimalDigits(iMnyDigit);

      getBillCardPanel2().getBillModel().setValueAt(
          m_orderVOs[row].getNoriginaltaxpricemny(), row,
          "noriginaltaxpricemny");
      getBillCardPanel2().getBillModel().setValueAt(
          m_orderVOs[row].getNoriginalcurmny(), row, "noriginalcurmny");
      getBillCardPanel2().getBillModel().setValueAt(
          m_orderVOs[row].getNorgtaxmoney(), row, "norgtaxmoney");

      row++;
    }
  }

  /**
   * 根据币种设置实时折本汇率 zhf v55
   */
  private void setExchangeRate(int row, String sCurrId) {
    // 首先设置显示精度
    int[] iaExchRateDigit = getCardPoPubSetUI2().getBothExchRateDigit(
        getCorpPrimaryKey(), sCurrId);
    int iMnyDigit = getCardPoPubSetUI2().getMoneyDigitByCurr_Busi(sCurrId);

    getBillCardPanel2().getBodyItem("nexchangeotobrate").setDecimalDigits(
        iaExchRateDigit[0]);
 
    // 设置值
    UFDouble[] daRate = getCardPoPubSetUI2().getBothExchRateValue(
        getCorpPrimaryKey(), sCurrId,
        nc.ui.po.pub.PoPublicUIClass.getLoginDate());// 当前日期取到实时汇率
    getBillCardPanel2().setBodyValueAt(daRate[0], row, "nexchangeotobrate");

    getBillCardPanel2().getBillModel().getItemByKey("noriginaltaxpricemny")
        .setDecimalDigits(iMnyDigit);
    getBillCardPanel2().getBillModel().getItemByKey("noriginalcurmny")
        .setDecimalDigits(iMnyDigit);
    getBillCardPanel2().getBillModel().getItemByKey("norgtaxmoney")
        .setDecimalDigits(iMnyDigit);

    // 重新赋值 精度生效
    getBillCardPanel2().getBillModel().setValueAt(
        getBillCardPanel2().getBodyValueAt(row, "noriginaltaxpricemny"), row,
        "noriginaltaxpricemny");
    getBillCardPanel2().getBillModel().setValueAt(
        getBillCardPanel2().getBodyValueAt(row, "noriginalcurmny"), row,
        "noriginalcurmny");
    getBillCardPanel2().getBillModel().setValueAt(
        getBillCardPanel2().getBodyValueAt(row, "norgtaxmoney"), row,
        "norgtaxmoney");
  }

  /**
   * 获得指定币种的汇率精度（折本+折辅） zhf v55
   */
  private int[] getDigits_ExchangeRate(String sCurrId) {
    int[] iaExchRateDigit = null;
    // 得到折本、折辅汇率精度
    iaExchRateDigit = getCardPoPubSetUI2().getBothExchRateDigit(
        getCorpPrimaryKey(), sCurrId);
    return iaExchRateDigit;
  }

  // /**
  // * @作者 zhf
  // * @创建时间：2008-6-17下午01:56:12
  // * @param iRow
  // * @return void
  // * @说明：(for v5.5)设置表体行汇率精度
  // * @修改者：
  // * @修改时间：
  // * @修改说明：
  // */
  //
  // protected void setRowDigits_ExchangeRate(int iRow) {
  // // 取得币种
  // String sCurrId = (String) getBillCardPanel2().getBillModel().getValueAt(
  // iRow, "currencytypeid");
  // int[] iaExchRateDigit = getDigits_ExchangeRate(sCurrId);
  // // 得到折本、折辅汇率精度
  // if (iaExchRateDigit == null || iaExchRateDigit.length == 0) {
  // getBillCardPanel2().getBillModel().getItemByKey("nexchangeotobrate")
  // .setDecimalDigits(2);
  // }
  // else {
  // getBillCardPanel2().getBillModel().getItemByKey("nexchangeotobrate")
  // .setDecimalDigits(iaExchRateDigit[0]);
  // }
  // }

  /**
   * 编辑折本汇率后处理 zhf
   */
  private void afterEditWhenNexchRate(int row) {

    // 折本汇率
    Object bRate = getBillCardPanel2().getBodyValueAt(row, "nexchangeotobrate");
    if (bRate == null || bRate.toString().trim().length() == 0) {
      return;
    }
    UFDouble ufBRate = new UFDouble(bRate.toString());
    EstimateUI.setLocalPriceMnyFrmOrg(getBillCardPanel2(), getCorpPrimaryKey(),
        row, ufBRate, m_priceDecimal,m_moneyDecimal, "ngaugenum");
  }

  /**
   * 修改原币币种修改后联动运算 zhf
   */
  private void afterEditWhenCurrency(int row) throws Exception {

    Object oCurrId = getBillCardPanel2().getBodyValueAt(row, "currencytypeid");
    if (PuPubVO.getString_TrimZeroLenAsNull(oCurrId) == null) {
      MessageDialog.showWarningDlg(this, NCLangRes.getInstance().getStrByID(
          "40040503", "UPP40040503-000033"/* @res "暂估处理 */), NCLangRes
          .getInstance().getStrByID("4004050301", "UPP4004050301-000011")/*
                                                                           * @res
                                                                           * 原币币种不能为空，请录入原币币种
                                                                           */);
      return;
    }
    String sCurrId = oCurrId.toString().trim();

    setExchangeRate(row, sCurrId);
    if (sCurrId.equalsIgnoreCase(m_sCurrTypeID))// 判断当前币种是否公司本位币
      // 设置折本汇率编辑属性
      getBillCardPanel2().getBillModel().setCellEditable(row,
          "nexchangeotobrate", false);
    else
      getBillCardPanel2().getBillModel().setCellEditable(row,
          "nexchangeotobrate", true);

    // 折本汇率
    Object bRate = getBillCardPanel2().getBodyValueAt(row, "nexchangeotobrate");
    if (bRate == null || bRate.toString().trim().length() == 0) {
      return;
    }
    UFDouble ufBRate = new UFDouble(bRate.toString());
    EstimateUI.setLocalPriceMnyFrmOrg(getBillCardPanel2(), getCorpPrimaryKey(),
        row, ufBRate, m_priceDecimal, m_moneyDecimal, "ngaugenum");
  }

  private void updateOorderVO(Integer[] rows) {
    OorderVO[] vos = (OorderVO[]) getBillCardPanel2().getBillModel()
        .getBodyValueVOs("nc.vo.ps.settle.OorderVO");
    for (int i = 0; i < rows.length; i++) {
      m_orderVOs[rows[i].intValue()].setNoriginalcurmny(vos[rows[i].intValue()]
          .getNoriginalcurmny());
      m_orderVOs[rows[i].intValue()].setNorgtaxmoney(vos[rows[i].intValue()]
          .getNorgtaxmoney());
      m_orderVOs[rows[i].intValue()].setNoriginaltaxpricemny(vos[rows[i]
          .intValue()].getNoriginaltaxpricemny());

      m_orderVOs[rows[i].intValue()].setNorgnettaxprice(vos[rows[i].intValue()]
          .getNorgnettaxprice());
      m_orderVOs[rows[i].intValue()].setNoriginalnetprice(vos[rows[i]
          .intValue()].getNoriginalnetprice());

      m_orderVOs[rows[i].intValue()].setNtaxmoney(vos[rows[i].intValue()]
          .getNtaxmoney());
      m_orderVOs[rows[i].intValue()].setNmoney(vos[rows[i].intValue()]
          .getNmoney());
      m_orderVOs[rows[i].intValue()].setNtotalmoney(vos[rows[i].intValue()]
          .getNtotalmoney());

      m_orderVOs[rows[i].intValue()].setNprice(vos[rows[i].intValue()]
          .getNprice());
      m_orderVOs[rows[i].intValue()].setNtaxprice(vos[rows[i].intValue()]
          .getNtaxprice());

      m_orderVOs[rows[i].intValue()].setCurrencytypeid(vos[rows[i].intValue()]
          .getCurrencytypeid());
      //再从模板上取一次汇率,否则精度可能存在问题
      m_orderVOs[rows[i].intValue()].setNexchangeotobrate((UFDouble)getBillCardPanel2().
          getBodyValueAt(i, "nexchangeotobrate"));
      // by zhaoyha at 2008.10.29
      m_orderVOs[rows[i].intValue()].setNzgyfnotaxmoney(vos[rows[i].intValue()].getNzgyfnotaxmoney());
      m_orderVOs[rows[i].intValue()].setNzgyfnotaxprice(vos[rows[i].intValue()].getNzgyfnotaxprice());
      // by zhaoyha end
    }
  }

  /**
   * 此处插入方法说明。 功能描述: 输入参数: 返回值: 异常处理:
   */
  public void afterEdit(BillEditEvent event) {
    // 暂估界面(选择订单界面)编辑事件

    if (getBillCardPanel2().isShowing()) {

      String strItemKey = event.getKey();
      int row = event.getRow();

      // 修改币种
      if (strItemKey.equals("currencytypename")) {
        try {
          afterEditWhenCurrency(row);
          // // 把界面变换后的数据更新到缓存
          // updateOorderVO(row);
          return;
        }
        catch (Exception e) {
          SCMEnv.out(e);
        }
      }
      if (strItemKey.equals("nexchangeotobrate")) {// 修改汇率
        afterEditWhenNexchRate(row);
        // // 把界面变换后的数据更新到缓存
        // updateOorderVO(row);
        return;
      }
      String strDisCntName = "应税内含";/*-=notranslate=-*/
      if (m_orderVOs[row].getIdiscounttaxtype().intValue() == 1)
        strDisCntName = "应税外加";
      if (m_orderVOs[row].getIdiscounttaxtype().intValue() == 2)
        strDisCntName = "不计税";
      //
      // 修改本币后联动计算
      UFDouble ufRate = getBillCardPanel2().getBodyValueAt(row,
          "nexchangeotobrate") == null ? new UFDouble(1.0)
          : (UFDouble) getBillCardPanel2().getBodyValueAt(row,
              "nexchangeotobrate");
      if ("nprice".equalsIgnoreCase(strItemKey)
          || "nmoney".equalsIgnoreCase(strItemKey)
          || "ntaxprice".equalsIgnoreCase(strItemKey)
          || "ntotalmoney".equalsIgnoreCase(strItemKey)
          || "ngaugenum".equalsIgnoreCase(strItemKey)) {
              
        EstimateUI.computeBodyData(m_orderVOs, strDisCntName,
            getBillCardPanel2(), event, this);
        // 本币值结算后联动计算原币数据
        String sCurrId = PuPubVO
            .getString_TrimZeroLenAsNull(getBillCardPanel2().getBodyValueAt(
                row, "currencytypeid"));
        //修改暂估数量后，原币也要重新计算，但不向暂估成本本币折 V55 by zhaoyha at 2008.9.27
        if("ngaugenum".equalsIgnoreCase(strItemKey)){
          EstimateUI.computeOrgBodyData(m_orderVOs, strDisCntName,
              getBillCardPanel2(), event, this);
          EstimateUI.setYFLocalPriceMnyFrmOrg(getBillCardPanel2(),
              getCorpPrimaryKey(), row, ufRate, m_priceDecimal, m_moneyDecimal,
              "ngaugenum");
        }
        return;
      }
      //修改原币后信息后,联动计算,并向本币折算
      else if("noriginalnetprice".equalsIgnoreCase(strItemKey) || "noriginalcurmny".equalsIgnoreCase(strItemKey) 
          || "norgnettaxprice".equalsIgnoreCase(strItemKey) || "noriginaltaxpricemny".equalsIgnoreCase(strItemKey)){
        // 修改原币后联动计算
        EstimateUI.computeOrgBodyData(m_orderVOs, strDisCntName,
            getBillCardPanel2(), event, this);
        // 原币值计算后联动计算本币数据
        EstimateUI.setLocalPriceMnyFrmOrg(getBillCardPanel2(),
            getCorpPrimaryKey(), row, ufRate, m_priceDecimal, m_moneyDecimal,
            "ngaugenum");
      }
      //修改税率后,暂估成本和暂估应付的本币都要计算,但暂估应付的原币不向暂估成本的本币折算
      else if("ntaxrate".equalsIgnoreCase(strItemKey)){
        // 暂估成本联动计算
        EstimateUI.computeBodyData(m_orderVOs, strDisCntName,
            getBillCardPanel2(), event, this);
        // 暂估应付原币联动计算
        EstimateUI.computeOrgBodyData(m_orderVOs, strDisCntName,
            getBillCardPanel2(), event, this);
        // 暂估应付原币向暂估应付本币折算
        EstimateUI.setYFLocalPriceMnyFrmOrg(getBillCardPanel2(),
            getCorpPrimaryKey(), row, ufRate, m_priceDecimal, m_moneyDecimal,
            "ngaugenum");
       }
      return;
    }
    // 入库单界面编辑事件
    if (m_nUIState == 1 && event.getKey().trim().equals("cdptid")) {
      int nRow = getBillCardPanel1().getRowCount();
      GeneralHVO VO = new GeneralHVO(nRow);
      getBillCardPanel1().getBillValueVO(VO);

      String sKey = VO.getHeadVO().getCdptid();
      UIRefPane nRefPanel = (UIRefPane) getBillCardPanel1().getHeadItem(
          "cbizid").getComponent();
      String sWhere = " bd_psndoc.pk_deptdoc = '" + sKey + "'";

      if (sKey != null && sKey.length() > 0) {
        nRefPanel.setWhereString(sWhere);

        if (!sKey.equals(m_sDeptID)) {
          // 如果业务员不属于部门,清除业务员
          UIRefPane nRefPanel0 = (UIRefPane) getBillCardPanel1().getHeadItem(
              "cbizid").getComponent();
          nRefPanel0.setValue(null);
          nRefPanel0.setPK(null);
        }
      }
      else {
        nRefPanel.setWhereString("bd_deptdoc.pk_corp = '" + m_sUnitCode
            + "' and bd_deptdoc.pk_deptdoc = bd_psndoc.pk_deptdoc");
      }
    }
    else if (m_nUIState == 1 && event.getKey().trim().equals("cbizid")) {
      
      String sPsnID = ((UIRefPane) getBillCardPanel1().getHeadItem("cbizid").getComponent()).getRefPK();
      if (sPsnID != null && sPsnID.length() > 0) {
        try {
          String sDeptName = EstimateHelper.getRefDeptName(sPsnID);
          UIRefPane nRefPanel = (UIRefPane) getBillCardPanel1().getHeadItem(
              "cdptid").getComponent();
          nRefPanel.setValue(sDeptName);
          nRefPanel.setPK(EstimateHelper.getRefDeptKey(sPsnID));

          m_sDeptID = nRefPanel.getRefPK();
        }
        catch (Exception e) {
          SCMEnv.out(e);
          MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes.getInstance()
              .getStrByID("40040503", "UPP40040503-000041")/* @res "设置部门" */,
              e.getMessage());
          return;
        }
      }
    }
    else if (m_nUIState == 1 && event.getKey().trim().equals("dbilldate")) {
      UIRefPane nRefPanel = (UIRefPane) getBillCardPanel1().getHeadItem(
          "dbilldate").getComponent();
      if (nRefPanel.getText().length() == 0) {
        MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes.getInstance()
            .getStrByID("40040503", "UPP40040503-000042")/* @res "修改入库日期" */,
            nc.ui.ml.NCLangRes.getInstance().getStrByID("40040503",
                "UPP40040503-000043")/* @res "入库日期不能为空！" */);
        nRefPanel.setValue(m_dSystem.toString());
        return;
      }

      UFDate dBillDate = new UFDate(nRefPanel.getText());
      if (dBillDate.compareTo(m_dSystem) > 0) {
        MessageDialog.showErrorDlg(this, NCLangRes.getInstance().getStrByID(
            "40040503", "UPP40040503-000042")/* @res "修改入库日期" */, NCLangRes
            .getInstance().getStrByID("40040503", "UPP40040503-000044", null,
                new String[] {
                    m_dSystem.toString(), m_dSystem.getDateAfter(1).toString()
                })
        /* @res "期初暂估入库单的入库日期不能晚于{0},系统启用日期{1}" */);
        nRefPanel.setValue(m_dSystem.toString());
        return;
      }
      // 按表头单据日期重新设置表体的业务日期及暂估日期，否则暂估月统计报表不正确，FENGPING，2008-01-08
      else {
        int iRowCnt = getBillCardPanel1().getRowCount();
        for (int i = 0; i < iRowCnt; i++) {
          getBillCardPanel1().getBillModel().setValueAt(dBillDate, i,
              "dbizdate");
          getBillCardPanel1().getBillModel()
              .setValueAt(dBillDate, i, "dzgdate");
          getBillCardPanel1().getBillModel().setRowState(i,
              BillModel.MODIFICATION);
        }
      }
    }
    else if (m_nUIState == 1
        && (event.getKey().trim().equals("ninnum")
            || event.getKey().trim().equals("nprice") || event.getKey().trim()
            .equals("nmny"))) {
      computeBBodyData(event);

    }
    else if (m_nUIState == 1 && event.getKey().trim().equals("vfree")) {
      // 设置自由项的值
      FreeVO freeVO = m_freeItem.getFreeVO();
      getBillCardPanel1().setBodyValueAt(freeVO.getVfree1(), event.getRow(),
          "vfree1");
      getBillCardPanel1().setBodyValueAt(freeVO.getVfree2(), event.getRow(),
          "vfree2");
      getBillCardPanel1().setBodyValueAt(freeVO.getVfree3(), event.getRow(),
          "vfree3");
      getBillCardPanel1().setBodyValueAt(freeVO.getVfree4(), event.getRow(),
          "vfree4");
      getBillCardPanel1().setBodyValueAt(freeVO.getVfree5(), event.getRow(),
          "vfree5");

    }
    else if (m_nUIState == 1 && event.getKey().trim().endsWith("crowno")) {
      // 行号
      nc.ui.scm.pub.report.BillRowNo.afterEditWhenRowNo(getBillCardPanel1(),
          event, "4T");

    }
    else if (m_nUIState == 1 && event.getKey().trim().endsWith("cwarehouseid")) {
      // 仓库
      setCalbodyValue();
    }
    // since v53重构，暂估数量、单价、金额调整到暂估界面(选择订单界面)编辑
    if (m_nUIState == 2) {
      // computeBodyData(event);
    }
    // since v53,支持单据模板自定义项维护
    if (getBillCardPanel1().isShowing()
        && event.getKey().startsWith("vuserdef")) {
      if (event.getPos() == BillItem.HEAD) {
        DefSetTool.afterEditHead(getBillCardPanel1().getBillData(), event
            .getKey(), "pk_defdoc"
            + event.getKey().substring("vuserdef".length(),
                event.getKey().length()));
      }
      else if (event.getPos() == BillItem.BODY) {
        DefSetTool.afterEditBody(getBillCardPanel1().getBillModel(), event
            .getRow(), event.getKey(), "pk_defdoc"
            + event.getKey().substring("vuserdef".length(),
                event.getKey().length()));
      }
    }
  }

  /**
   * 是否未查询过表体
   * <p>
   * <b>examples:</b>
   * <p>
   * 使用示例
   * <p>
   * <b>参数说明</b>
   * 
   * @param voCur
   *          <p>
   * @author czp
   * @time 2007-7-16 下午01:27:18
   */
  private boolean isNoBodys(GeneralHVO voCur) {
    boolean bNoLoaded = true;
    try {
      bNoLoaded = (voCur == null || voCur.getChildrenVO() == null
          || voCur.getChildrenVO().length == 0 || PuPubVO
          .getString_TrimZeroLenAsNull(voCur.getChildrenVO()[0].getPrimaryKey()) == null);
    }
    catch (BusinessException e) {
      // 出现异常时，无论如何重新查询
      SCMEnv.out(e);
    }
    return bNoLoaded;
  }

  /**
   * 此处插入方法说明。 功能描述: 输入参数: 返回值: 异常处理:
   */
  public void bodyRowChange(BillEditEvent event) {
    if ((UITable) event.getSource() == getBillListPanel().getHeadTable()) {
      // 入库单列表
      m_nPresentRecord = event.getRow();

      if (m_nPresentRecord >= 0) {
        // 获得该张单据的表体,sice v502 重构
        // if((!m_bGenerate) && m_bBodyQuery != null &&
        // m_bBodyQuery.length > 0 &&
        // (!m_bBodyQuery[m_nPresentRecord].booleanValue())){
        if (!m_bGenerate && isNoBodys(m_VOs[m_nPresentRecord])) {
          try {
            ArrayList list = EstimateHelper.queryInitialBody(
                m_VOs[m_nPresentRecord].getHeadVO().getCgeneralhid(),
                m_VOs[m_nPresentRecord].getHeadVO().getTs());
            if (list == null || list.size() == 0)
              throw new BusinessException(nc.ui.ml.NCLangRes.getInstance()
                  .getStrByID("4004050302", "UPP4004050302-000004")/*
                                                                     * @res
                                                                     * "发生并发操作，请刷新界面后再试！"
                                                                     */);
            GeneralHItemVO tempBodyVO[] = (GeneralHItemVO[]) list.get(0);
            GeneralBb3VO tempBb3VO[] = (GeneralBb3VO[]) list.get(1);
            m_VOs[m_nPresentRecord].setChildrenVO(tempBodyVO);
            m_VOs[m_nPresentRecord].setGrandVO(tempBb3VO);
          }
          catch (BusinessException e) {
            SCMEnv.out(e);
            MessageDialog.showHintDlg(this, nc.ui.ml.NCLangRes.getInstance()
                .getStrByID("40040503", "UPP40040503-000045")/*
                                                               * @res "并发操作"
                                                               */, e.getMessage());
            return;
          }
          catch (Exception e) {
            SCMEnv.out(e);
          }
          // m_bBodyQuery[m_nPresentRecord] = new UFBoolean(true);
        }

        GeneralHItemVO bodyVO[] = m_VOs[m_nPresentRecord].getBodyVO();
        getBillListPanel().getBodyBillModel().setBodyDataVO(bodyVO);
        getBillListPanel().getBodyBillModel().execLoadFormula();
        getBillListPanel().getBodyBillModel().updateValue();
        // 显示来源单据信息
        nc.ui.pu.pub.PuTool.loadSourceBillData(getBillListPanel(), "4T");

        // 设置库存组织
        Object o = getBillListPanel().getHeadBillModel().getValueAt(
            event.getRow(), "cstoreorganization");
        if (o != null)
          m_VOs[m_nPresentRecord].getHeadVO().setCstoreorganization(
              o.toString());
        GeneralHItemVO tempbodyVO[] = m_VOs[m_nPresentRecord].getBodyVO();
        // 设置来源单据类型名称
        for (int j = 0; j < tempbodyVO.length; j++) {
          Object oTemp = getBillListPanel().getBodyBillModel().getValueAt(j,
              "csourcebillname");
          if (oTemp != null)
            tempbodyVO[j].setCsourcebillname(oTemp.toString());
          oTemp = getBillListPanel().getBodyBillModel().getValueAt(j,
              "csourcebillcode");
          if (oTemp != null)
            tempbodyVO[j].setCsourcebillcode(oTemp.toString());
        }
        //

      }
    }
  }

  /**
   * 此处插入方法说明。 功能描述:改变界面按钮状态 输入参数: 返回值: 异常处理:
   */
  private void changeButtonState() {
    if (m_nUIState == 1) {
      for (int i = 0; i < m_nButtonState.length; i++) {
        if (m_nButtonState[i] == 0) {
          m_buttons1[i].setVisible(true);
          m_buttons1[i].setEnabled(true);
        }
        if (m_nButtonState[i] == 1) {
          m_buttons1[i].setVisible(true);
          m_buttons1[i].setEnabled(false);
        }
        if (m_nButtonState[i] == 2) {
          m_buttons1[i].setVisible(false);
        }

        this.updateButton(m_buttons1[i]);
      }
    }

    else if (m_nUIState == 2) {
      for (int i = 0; i < m_nButtonState.length; i++) {
        if (m_nButtonState[i] == 0) {
          m_buttons2[i].setVisible(true);
          m_buttons2[i].setEnabled(true);
        }
        if (m_nButtonState[i] == 1) {
          m_buttons2[i].setVisible(true);
          m_buttons2[i].setEnabled(false);
        }
        if (m_nButtonState[i] == 2) {
          m_buttons2[i].setVisible(false);
        }

        this.updateButton(m_buttons2[i]);
      }
    }

    else if (m_nUIState == 3) {
      for (int i = 0; i < m_nButtonState.length; i++) {
        if (m_nButtonState[i] == 0) {
          m_buttons3[i].setVisible(true);
          m_buttons3[i].setEnabled(true);
        }
        if (m_nButtonState[i] == 1) {
          m_buttons3[i].setVisible(true);
          m_buttons3[i].setEnabled(false);
        }
        if (m_nButtonState[i] == 2) {
          m_buttons3[i].setVisible(false);
        }

        this.updateButton(m_buttons3[i]);
      }
    }
  }

  /**
   * 期初入库单,卡片模板
   */
  private BillCardPanel getBillCardPanel1() {
    if (m_billPanel1 == null) {
      try {
        m_billPanel1 = new BillCardPanel();
        //
        m_billPanel1.loadTemplet("4T", null, getClientEnvironment().getUser()
            .getPrimaryKey(), getClientEnvironment().getCorporation()
            .getPk_corp());

        // 加载模板
        // BillData bd = new
        // BillData(m_billPanel1.getTempletData("40040503020100000000"));
        BillData bd = m_billPanel1.getBillData();

        bd = initDecimal1(bd);

        // 批次号参照
        LotNumbRefPane lotRef = new LotNumbRefPane();
        if (lotRef != null) {
          lotRef.setMaxLength(bd.getBodyItem("vbatchcode").getLength());
        }
        bd.getBodyItem("vbatchcode").setComponent((JComponent) lotRef);

        // 自由项
        m_freeItem = new FreeItemRefPane();
        m_freeItem.setMaxLength(bd.getBodyItem("vfree").getLength());
        bd.getBodyItem("vfree").setComponent(m_freeItem);

        m_billPanel1.setBillData(bd);
        m_billPanel1.setTatolRowShow(true);
        //m_billPanel1.setShowThMark(true);

        // 增加单据编辑监听
        m_billPanel1.addEditListener(this);
        m_billPanel1.addBodyEditListener2(this);
        m_billPanel1.setBodyMenuShow(false);

        // 初始化行号
        nc.ui.scm.pub.report.BillRowNo.loadRowNoItem(m_billPanel1, "crowno");

        // 修改自定义项
        DefSetTool.updateBillCardPanelUserDef(m_billPanel1,
            getClientEnvironment().getCorporation().getPk_corp(), "4T", // 单据类型
            "vuserdef", "vuserdef");
        // 为使自定义项名称显示在模板上,这里重新设定一下数据
        m_billPanel1.setBillData(m_billPanel1.getBillData());
        // V55选中模式
        PuTool.setLineSelected(m_billPanel1);
      }
      catch (java.lang.Throwable ivjExc) {
        SCMEnv.out(ivjExc);
        MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes.getInstance()
            .getStrByID("40040503", "UPP40040503-000024")/* @res "加载模板" */,
            nc.ui.ml.NCLangRes.getInstance().getStrByID("40040503",
                "UPP40040503-000025")/* @res "模板不存在!" */);
        return null;
      }
    }
    return m_billPanel1;
  }

  /**
   * 订单卡片模板--选择要生成期初暂估入库单的采购订单
   */
  private BillCardPanel getBillCardPanel2() {
    if (m_billPanel2 == null) {
      try {
        m_billPanel2 = new BillCardPanel();

        // 加载模板
        BillData bd = new BillData(m_billPanel2
            .getTempletData("40040503020200000000"));

        // m_billPanel2.loadTemplet("40040503020200000000");

        //若未暂估应付则不显示暂估应付页签
        if(!m_bZGYF.booleanValue())
          bd.removeTabItems(BillData.BODY, "zgyf_table");
        
        bd = initDecimal2(bd);

        m_billPanel2.setBillData(bd);

        // 增加单据编辑监听
        //m_billPanel2.setShowThMark(true);
        m_billPanel2.setTatolRowShow(true);
        m_billPanel2.setTatolRowShow("zgyf_table", true);
        m_billPanel2.addEditListener(this);
        m_billPanel2.addBodyEditListener2(this);
        //屏蔽所有页签的右键菜单 V55 by zhaoyha at 2008.9.26 
        for(String strTableCode : m_billPanel2.getBillData().getTableCodes(BillItem.BODY)){
          m_billPanel2.setBodyMenuShow(strTableCode,false);
        }   

        // 表体选择监听
        m_billPanel2.getBillTable().setCellSelectionEnabled(false);
        m_billPanel2.getBillTable().setSelectionMode(
            javax.swing.ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        m_billPanel2.getBillTable().getSelectionModel()
            .addListSelectionListener(this);
        // m_billPanel2.setBillBeforeEditListenerHeadTail(this);
        m_billPanel2.getBillModel().addSortRelaObjectListener2(
            new IBillRelaSortListener2Impl());

        // 扣税类别 0应税内含 1应税外加 2不计税
        UIComboBox comItem = (UIComboBox) m_billPanel2.getBodyItem(
            "idiscounttaxtype").getComponent();
        m_billPanel2.getBodyItem("idiscounttaxtype").setWithIndex(true);
        comItem.addItem(nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401",
            "UPP40040401-000105")/* @res "应税内含" */);
        comItem.addItem(nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401",
            "UPP40040401-000106")/* @res "应税外加" */);
        comItem.addItem(nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401",
            "UPP40040401-000107")/* @res "不计税" */);
        //
        m_billPanel2.setBodyMultiSelect(true);
        //
        m_billPanel2.getBillTable().setRowSelectionAllowed(true);
        m_billPanel2.getBillTable().setColumnSelectionAllowed(false);
        m_billPanel2.getBillTable().setSelectionMode(
            javax.swing.ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        new DefaultCurrTypeBizDecimalListener(m_billPanel2.getBillModel(),
            "currencytypeid", new String[] {
                "noriginaltaxpricemny", "noriginalcurmny", "norgtaxmoney"
            });
        //
        PuTool.setLineSelected(m_billPanel2);
      }
      catch (java.lang.Throwable ivjExc) {
        SCMEnv.out(ivjExc);
        MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes.getInstance()
            .getStrByID("40040503", "UPP40040503-000024")/* @res "加载模板" */,
            nc.ui.ml.NCLangRes.getInstance().getStrByID("40040503",
                "UPP40040503-000025")/* @res "模板不存在!" */);
        return null;

      }
    }
    return m_billPanel2;
  }

  /**
   * 期初暂估入库单，列表界面
   */
  private BillListPanel getBillListPanel() {
    if (m_listPanel == null) {
      try {
        m_listPanel = new BillListPanel(false);
        m_listPanel.setName("ListPanel");
        try {
          m_listPanel.loadTemplet("4T", null, getClientEnvironment().getUser()
              .getPrimaryKey(), getClientEnvironment().getCorporation()
              .getPk_corp());
        }
        catch (Exception e) {
          SCMEnv.out(e);
          m_listPanel.loadTemplet("40040503020100000000");
        }
        BillListData bd = m_listPanel.getBillListData();
        bd = initListDecimal(bd);
        m_listPanel.setListData(bd);
        //
        DefSetTool.updateBillListPanelUserDef(m_listPanel,
            getClientEnvironment().getCorporation().getPk_corp(), "4T", // 单据类型
            "vuserdef", "vuserdef");
        //
        //m_listPanel.getParentListPanel().setShowThMark(true);
        //m_listPanel.getChildListPanel().setShowThMark(true);
        m_listPanel.getParentListPanel().setTotalRowShow(true);
        m_listPanel.getChildListPanel().setTotalRowShow(true);
        m_listPanel.addMouseListener(this);
        m_listPanel.addEditListener(this);
        // 表头选择监听
        m_listPanel.getHeadBillModel().addSortRelaObjectListener2(this);
        m_listPanel.getHeadTable().setCellSelectionEnabled(false);
        //
        m_listPanel.getHeadTable().setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        // 为使自定义项名称显示在模板上,这里重新设定一下数据[1]
        m_listPanel.setListData(m_listPanel.getBillListData());
        // V55选中模式
        PuTool.setLineSelectedList(m_listPanel);
        //监听器必须加在[1]后面
        m_listPanel.getHeadTable().getSelectionModel().addListSelectionListener(this);
        //
        m_listPanel.updateUI();
      }
      catch (java.lang.Throwable e) {
        SCMEnv.out(e);
        MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes.getInstance()
            .getStrByID("40040503", "UPP40040503-000024")/* @res "加载模板" */,
            nc.ui.ml.NCLangRes.getInstance().getStrByID("40040503",
                "UPP40040503-000025")/* @res "模板不存在!" */);
        return null;
      }
    }
    return m_listPanel;
  }

  /**
   * 子类实现该方法，返回业务界面的标题。
   * 
   * @version (00-6-6 13:33:25)
   * @return java.lang.String
   */
  public String getTitle() {
    return nc.ui.ml.NCLangRes.getInstance().getStrByID("4004050302",
        "UPP4004050302-000005")/* @res "维护期初暂估" */;
  }

  /**
   * 功能描述:初始化 修改：2004-09-10 袁野
   */
  public void init() {
    initpara();
    // 显示按钮
    m_buttons1 = new ButtonObject[16];
    m_buttons1[0] = new ButtonObject(nc.ui.ml.NCLangRes.getInstance()
        .getStrByID("common", "UC001-0000002")/*
                                               * @res "增加"
                                               */, nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UC001-0000002")/*
                                                                                 * @res
                                                                                 * "增加"
                                                                                 */, "增加");
    m_buttons1[1] = new ButtonObject(nc.ui.ml.NCLangRes.getInstance()
        .getStrByID("common", "UC001-0000045")/*
                                               * @res "修改"
                                               */, nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UC001-0000045")/*
                                                                                 * @res
                                                                                 * "修改"
                                                                                 */, "修改");
    m_buttons1[2] = new ButtonObject(nc.ui.ml.NCLangRes.getInstance()
        .getStrByID("common", "UC001-0000001")/*
                                               * @res "保存"
                                               */, nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UC001-0000001")/*
                                                                                 * @res
                                                                                 * "保存"
                                                                                 */, "保存");
    m_buttons1[3] = new ButtonObject(nc.ui.ml.NCLangRes.getInstance()
        .getStrByID("common", "UC001-0000013")/*
                                               * @res "删行"
                                               */, nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UC001-0000013")/*
                                                                                 * @res
                                                                                 * "删行"
                                                                                 */, "删行");
    m_buttons1[4] = new ButtonObject(nc.ui.ml.NCLangRes.getInstance()
        .getStrByID("common", "UC001-0000008")/*
                                               * @res "取消"
                                               */, nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UC001-0000008")/*
                                                                                 * @res
                                                                                 * "取消"
                                                                                 */, "取消");
    m_buttons1[5] = new ButtonObject(nc.ui.ml.NCLangRes.getInstance()
        .getStrByID("common", "UC001-0000005")/*
                                               * @res "作废"
                                               */, nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UC001-0000005")/*
                                                                                 * @res
                                                                                 * "作废"
                                                                                 */, "删除");
    m_buttons1[6] = new ButtonObject(nc.ui.ml.NCLangRes.getInstance()
        .getStrByID("common", "UC001-0000006")/*
                                               * @res "查询"
                                               */, nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UC001-0000006")/*
                                                                                 * @res
                                                                                 * "查询"
                                                                                 */, "查询");
    m_buttons1[7] = new ButtonObject(nc.ui.ml.NCLangRes.getInstance()
        .getStrByID("common", "UCH031")/*
                                         * @res "首页"
                                         */, nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UCH031")/*
                                                                         * @res
                                                                         * "首页"
                                                                         */, "首页");
    m_buttons1[8] = new ButtonObject(nc.ui.ml.NCLangRes.getInstance()
        .getStrByID("common", "UCH033")/*
                                         * @res "上一页"
                                         */, nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UCH033")/*
                                                                         * @res
                                                                         * "上一页"
                                                                         */, "上一页");
    m_buttons1[9] = new ButtonObject(nc.ui.ml.NCLangRes.getInstance()
        .getStrByID("common", "UCH034")/*
                                         * @res "下一页"
                                         */, nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UCH034")/*
                                                                         * @res
                                                                         * "下一页"
                                                                         */, "下一页");
    m_buttons1[10] = new ButtonObject(nc.ui.ml.NCLangRes.getInstance()
        .getStrByID("common", "UCH032")/*
                                         * @res "末页"
                                         */, nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UCH032")/*
                                                                         * @res
                                                                         * "末页"
                                                                         */, "末页");
    m_buttons1[11] = new ButtonObject(nc.ui.ml.NCLangRes.getInstance()
        .getStrByID("scmcommon", "UPPSCMCommon-000464")/* @res "列表显示" */,
        nc.ui.ml.NCLangRes.getInstance().getStrByID("scmcommon",
            "UPPSCMCommon-000464")/* @res "列表显示" */, "列表显示");
    m_buttons1[12] = new ButtonObject(nc.ui.ml.NCLangRes.getInstance()
        .getStrByID("common", "UC001-0000007")/*
                                               * @res "打印"
                                               */, nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UC001-0000007")/*
                                                                                 * @res
                                                                                 * "打印"
                                                                                 */, 2, "打印"); /*-=notranslate=-*/
    m_buttons1[13] = new ButtonObject(nc.ui.ml.NCLangRes.getInstance()
        .getStrByID("4004050302", "UPT4004050302-000025")/* @res "打印预览" */,
        nc.ui.ml.NCLangRes.getInstance().getStrByID("4004050302",
            "UPT4004050302-000025")/* @res "打印预览" */, 2, "打印预览"); /*-=notranslate=-*/
    m_buttons1[14] = new ButtonObject(nc.ui.ml.NCLangRes.getInstance()
        .getStrByID("4004050302", "UPT4004050302-000034")/* @res "文档管理" */,
        nc.ui.ml.NCLangRes.getInstance().getStrByID("4004050302",
            "UPT4004050302-000034")/* @res "文档管理" */, "文档管理");
    m_buttons1[15] = new ButtonObject(nc.ui.ml.NCLangRes.getInstance()
        .getStrByID("scmcommon", "UPPSCMCommon-000145")/* @res "联查" */,
        nc.ui.ml.NCLangRes.getInstance().getStrByID("scmcommon",
            "UPPSCMCommon-000145")/* @res "联查" */, "联查");
    // 单据维护
    ButtonObject btnOprCombin = new ButtonObject(
        nc.ui.ml.NCLangRes.getInstance().getStrByID("4004050302",
            "UPT4004050302-000026")/* @res "单据维护" */,
        nc.ui.ml.NCLangRes.getInstance().getStrByID("4004050302",
            "UPT4004050302-000026")/* @res "单据维护" */, "单据维护");
    btnOprCombin.setChildButtonGroup(new ButtonObject[] {
        m_buttons1[1], m_buttons1[2], m_buttons1[3], m_buttons1[4],
        m_buttons1[5]
    });
    // 浏览
    ButtonObject btnLookCombin = new ButtonObject(nc.ui.ml.NCLangRes
        .getInstance().getStrByID("common", "UC001-0000021")/*
                                                             * @res "浏览"
                                                             */, nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UC001-0000021")/*
                                                                                 * @res
                                                                                 * "浏览"
                                                                                 */, "浏览");
    btnLookCombin.setChildButtonGroup(new ButtonObject[] {
        m_buttons1[6], m_buttons1[7], m_buttons1[8], m_buttons1[9],
        m_buttons1[10]
    });
    // 辅助
    ButtonObject btnOtherCombin11 = new ButtonObject(nc.ui.ml.NCLangRes
        .getInstance().getStrByID("common", "UC001-0000036")/*
                                                             * @res "辅助"
                                                             */, nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UC001-0000036")/*
                                                                                 * @res
                                                                                 * "辅助"
                                                                                 */, "辅助");
    btnOtherCombin11.setChildButtonGroup(new ButtonObject[] {
        m_buttons1[14], m_buttons1[15], m_buttons1[12], m_buttons1[13]
    });
    m_buttons11 = new ButtonObject[] {
        m_buttons1[0], btnOprCombin, btnLookCombin, m_buttons1[11],
        btnOtherCombin11
    };
    //
    m_buttons3 = new ButtonObject[8];
    m_buttons3[0] = new ButtonObject(nc.ui.ml.NCLangRes.getInstance()
        .getStrByID("common", "UC001-0000041")/*
                                               * @res "全选"
                                               */, nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UC001-0000041")/*
                                                                                 * @res
                                                                                 * "全选"
                                                                                 */, "全选");
    m_buttons3[1] = new ButtonObject(nc.ui.ml.NCLangRes.getInstance()
        .getStrByID("common", "UC001-0000042")/*
                                               * @res "全消"
                                               */, nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UC001-0000042")/*
                                                                                 * @res
                                                                                 * "全消"
                                                                                 */, "全消");
    m_buttons3[2] = new ButtonObject(nc.ui.ml.NCLangRes.getInstance()
        .getStrByID("common", "UC001-0000005")/*
                                               * @res "作废"
                                               */, nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UC001-0000005")/*
                                                                                 * @res
                                                                                 * "作废"
                                                                                 */, "作废");
    m_buttons3[3] = new ButtonObject(nc.ui.ml.NCLangRes.getInstance()
        .getStrByID("common", "UC001-0000006")/*
                                               * @res "查询"
                                               */, nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UC001-0000006")/*
                                                                                 * @res
                                                                                 * "查询"
                                                                                 */, "查询");
    m_buttons3[4] = new ButtonObject(nc.ui.ml.NCLangRes.getInstance()
        .getStrByID("scmcommon", "UPPSCMCommon-000463")/* @res "卡片显示" */,
        nc.ui.ml.NCLangRes.getInstance().getStrByID("scmcommon",
            "UPPSCMCommon-000463")/* @res "卡片显示" */, "卡片显示");
    m_buttons3[5] = new ButtonObject(nc.ui.ml.NCLangRes.getInstance()
        .getStrByID("4004050302", "UPT4004050302-000034")/* @res "文档管理" */,
        nc.ui.ml.NCLangRes.getInstance().getStrByID("4004050302",
            "UPT4004050302-000034")/* @res "文档管理" */, "文档管理");
    m_buttons3[6] = new ButtonObject(nc.ui.ml.NCLangRes.getInstance()
        .getStrByID("common", "UC001-0000007")/*
                                               * @res "打印"
                                               */, nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UC001-0000007")/*
                                                                                 * @res
                                                                                 * "打印"
                                                                                 */, 2, "打印"); /*-=notranslate=-*/
    m_buttons3[7] = new ButtonObject(nc.ui.ml.NCLangRes.getInstance()
        .getStrByID("4004050302", "UPT4004050302-000025")/* @res "打印预览" */,
        nc.ui.ml.NCLangRes.getInstance().getStrByID("4004050302",
            "UPT4004050302-000025")/* @res "打印预览" */, 2, "打印预览"); /*-=notranslate=-*/
    // 辅助
    ButtonObject btnOtherCombin31 = new ButtonObject(nc.ui.ml.NCLangRes
        .getInstance().getStrByID("common", "UC001-0000036")/*
                                                             * @res "辅助"
                                                             */, nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UC001-0000036")/*
                                                                                 * @res
                                                                                 * "辅助"
                                                                                 */, "辅助");
    btnOtherCombin31.setChildButtonGroup(new ButtonObject[] {
        m_buttons3[5], m_buttons3[6], m_buttons3[7]
    });

    /* since v53,支持列表联查 */
    m_buttons31 = new ButtonObject[] {
        m_buttons3[0], m_buttons3[1], m_buttons3[2], m_buttons3[3],
        m_buttons3[4], m_btnListLinkQuery, btnOtherCombin31
    };
    //
    m_buttons2 = new ButtonObject[6];
    m_buttons2[0] = new ButtonObject(nc.ui.ml.NCLangRes.getInstance()
        .getStrByID("common", "UC001-0000041")/*
                                               * @res "全选"
                                               */, nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UC001-0000041")/*
                                                                                 * @res
                                                                                 * "全选"
                                                                                 */, "全选");
    m_buttons2[1] = new ButtonObject(nc.ui.ml.NCLangRes.getInstance()
        .getStrByID("common", "UC001-0000042")/*
                                               * @res "全消"
                                               */, nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UC001-0000042")/*
                                                                                 * @res
                                                                                 * "全消"
                                                                                 */, "全消");
    m_buttons2[2] = new ButtonObject(nc.ui.ml.NCLangRes.getInstance()
        .getStrByID("4004050302", "UPT4004050302-000032")/* @res "分单方式" */,
        nc.ui.ml.NCLangRes.getInstance().getStrByID("4004050302",
            "UPT4004050302-000032")/* @res "分单方式" */, "分单方式");
    m_buttons2[3] = new ButtonObject(nc.ui.ml.NCLangRes.getInstance()
        .getStrByID("4004050302", "UPT4004050302-000033")/* @res "暂估" */,
        nc.ui.ml.NCLangRes.getInstance().getStrByID("4004050302",
            "UPT4004050302-000033")/* @res "暂估" */, "暂估");
    m_buttons2[4] = new ButtonObject(nc.ui.ml.NCLangRes.getInstance()
        .getStrByID("common", "UC001-0000006")/*
                                               * @res "查询"
                                               */, nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UC001-0000006")/*
                                                                                 * @res
                                                                                 * "查询"
                                                                                 */, "查询");
    m_buttons2[5] = new ButtonObject(nc.ui.ml.NCLangRes.getInstance()
        .getStrByID("common", "UC001-0000038")/*
                                               * @res "返回"
                                               */, nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UC001-0000038")/*
                                                                                 * @res
                                                                                 * "返回"
                                                                                 */, "返回");

    this.setButtons(m_buttons11);

    // 显示单据
    setLayout(new java.awt.BorderLayout());
    add(getBillCardPanel1(), java.awt.BorderLayout.CENTER);
    getBillCardPanel1().setEnabled(false);

    initConfigure();

    // 初始按钮状态
    m_nButtonState = new int[m_buttons1.length];
    m_nRestoreState = new int[m_buttons1.length];

    // 设置除增加,打印，查询和列表外的所有按钮为灰
    for (int i = 0; i < 16; i++) {
      m_nButtonState[i] = 1;
    }
    m_nButtonState[0] = 0;
    m_nButtonState[6] = 0;
    m_nButtonState[11] = 0;
    m_nButtonState[12] = 0;
    m_nButtonState[13] = 0;

    changeButtonState();

  }

  /**
   * 此处插入方法说明。 功能描述: 输入参数: 返回值: 异常处理:
   */
  public void mouse_doubleclick(BillMouseEnent event) {
    if (event.getPos() == BillItem.HEAD) {
      m_nPresentRecord = event.getRow();

      // 列表可能已排序，获得排序前的缓存记录指针
      m_nPresentRecord = nc.ui.pu.pub.PuTool.getIndexBeforeSort(m_listPanel,
          m_nPresentRecord);

      if (m_nPresentRecord >= 0) {
        this.remove(getBillListPanel());
        getBillCardPanel1().setVisible(true);
        this.setButtons(m_buttons11);
        m_nButtonState = new int[m_buttons1.length];

        if (m_bGenerate && 
            PuPubVO.getString_TrimZeroLenAsNull(m_VOs[m_nPresentRecord].getHeadVO().getCgeneralhid())==null) {
          getBillCardPanel1().setEnabled(false);
          m_bAdd = true;
          // 设置业务员的部门属性
          String sKey = m_VOs[m_nPresentRecord].getHeadVO().getCdptid();
          UIRefPane nRefPanel = (UIRefPane) getBillCardPanel1().getHeadItem(
              "cbizid").getComponent();
          String sWhere = " bd_psndoc.pk_deptdoc = '" + sKey + "'";
          if (sKey != null && sKey.length() > 0)
            nRefPanel.setWhereString(sWhere);
          else
            nRefPanel.setWhereString("bd_deptdoc.pk_corp = '" + m_sUnitCode
                + "' and bd_deptdoc.pk_deptdoc = bd_psndoc.pk_deptdoc");

          // 设置除保存，删行, 打印，放弃外所有按钮为灰
          for (int i = 0; i < 16; i++) {
            m_nButtonState[i] = 1;
          }
          m_nButtonState[2] = 0;
          m_nButtonState[3] = 0;
          m_nButtonState[4] = 0;
          m_nButtonState[12] = 0;
          m_nButtonState[13] = 0;
        }
        else {
          // 设置除保存,取消,删行外所有按钮为正常
          for (int i = 0; i < 16; i++) {
            m_nButtonState[i] = 0;
          }
          for (int i = 2; i < 5; i++)
            m_nButtonState[i] = 1;
          if (m_nPresentRecord == 0) {
            m_nButtonState[7] = 1;
            m_nButtonState[8] = 1;
          }
          if (m_nPresentRecord == m_VOs.length - 1) {
            m_nButtonState[9] = 1;
            m_nButtonState[10] = 1;
          }
          if (m_VOs.length == 1) {
            for (int i = 7; i < 11; i++)
              m_nButtonState[i] = 1;
          }
          // 暂估应付时,不可以修改，也就不可以行操作
          if (m_bZGYF.booleanValue()) {
            m_nButtonState[1] = 1;
          }
        }
        m_nUIState = 1;
        changeButtonState();

        // 显示卡片单据
        getBillCardPanel1().setBillValueVO(m_VOs[m_nPresentRecord]);
        getBillCardPanel1().getBillModel().execLoadFormula();
        // 显示来源单据信息
        nc.ui.pu.pub.PuTool.loadSourceBillData(getBillCardPanel1(), "4T");
        getBillCardPanel1().updateValue();
        getBillCardPanel1().updateUI();

        if (m_bGenerate){
          if(PuPubVO.getString_TrimZeroLenAsNull(m_VOs[m_nPresentRecord].getHeadVO().getCgeneralhid())!=null){
            getBillCardPanel1().setEnabled(false);
          }
          else 
            setPartNoEditable();
        }
        // 显示备注
        UIRefPane nRefPanel1 = (UIRefPane) getBillCardPanel1().getHeadItem(
            "vnote").getComponent();
        nRefPanel1.setValue(m_VOs[m_nPresentRecord].getHeadVO().getVnote());

        // 显示库存组织
        setCalbodyValue();

        if (m_bGenerate) {
          // 聚集在入库单号
          getBillCardPanel1().transferFocusTo(BillCardPanel.HEAD);
        }

      }
    }
  }

  /**
   * 子类实现该方法，响应按钮事件。
   * 
   * @version (00-6-1 10:32:59)
   * @param bo
   *          ButtonObject
   */
  public void onButtonClicked(nc.ui.pub.ButtonObject bo) {
    if (bo == m_buttons1[0]) {
      showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
          "UCH028")/* @res "正在增加" */);
      onAppend();
    }

    if (bo == m_buttons1[1]) {
      showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
          "UCH027")/* @res "正在修改" */);
      onModify();
    }

    if (bo == m_buttons1[2]) {
      onSave();
      showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
          "UCH005")/* @res "保存成功" */);
    }

    if (bo == m_buttons1[3]) {
      onDeleteLine();
      showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
          "UCH037")/* @res "删行成功" */);
    }

    if (bo == m_buttons1[4]) {
      onCancelling();
      showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
          "UCH008")/* @res "取消成功" */);
    }

    if (bo == m_buttons1[5]) {
      onDiscard();
    }

    if (bo == m_buttons1[6]) {
      onCardQuery();
      showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
          "UCH009")/* @res "查询完成" */);
    }

    if (bo == m_buttons1[7])
      onFirst();

    if (bo == m_buttons1[8])
      onPrevious();

    if (bo == m_buttons1[9])
      onNext();

    if (bo == m_buttons1[10])
      onLast();

    if (bo == m_buttons1[11])
      onList();

    if (bo == m_buttons1[12]) {
      onPrint();
      showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
          "UCH041")/* @res "打印成功" */);
    }

    if (bo == m_buttons1[13]) {
      onPreview();
      showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
          "4004COMMON000000021")/* @res "打印预览完成" */);
    }

    if (bo == m_buttons1[14]) {
      onFileManage();
      showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
          "4004COMMON000000025")/* @res "文档管理成功" */);
    }

    if (bo == m_buttons1[15] || bo == m_btnListLinkQuery) {
      onBillRelateQuery();
      showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
          "4004COMMON000000019")/* @res "联查成功" */);
    }

    if (bo == m_buttons3[0]) {
      onSelectAll();
      showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
          "4004COMMON000000033")/* @res "全选成功" */);
    }

    if (bo == m_buttons3[1]) {
      onSelectNo();
      showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
          "4004COMMON000000034")/* @res "全消成功" */);
    }

    if (bo == m_buttons3[2]) {
      onDiscard();
    }

    if (bo == m_buttons3[3]) {
      onQueryStock();
      showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
          "UCH009")/* @res "查询完成" */);
    }

    if (bo == m_buttons3[4])
      onSwitch();

    if (bo == m_buttons3[5]) {
      onFileManage();
      showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
          "4004COMMON000000025")/* @res "文档管理成功" */);
    }

    if (bo == m_buttons3[6]) {
      onListPrint();
      showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
          "UCH041")/* @res "打印成功" */);
    }

    if (bo == m_buttons3[7]) {
      onListPreview();
      showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
          "4004COMMON000000021")/* @res "打印预览完成" */);
    }

    if (bo == m_buttons2[0]) {
      onSelectAll();
      showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
          "4004COMMON000000033")/* @res "全选成功" */);
    }

    if (bo == m_buttons2[1]) {
      onSelectNo();
      showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
          "4004COMMON000000034")/* @res "全消成功" */);
    }

    if (bo == m_buttons2[2])
      onSelectMode();

    if (bo == m_buttons2[3])
      onEstimate();

    if (bo == m_buttons2[4]) {
      onQueryOrder();
      showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
          "UCH009")/* @res "查询完成" */);
    }

    if (bo == m_buttons2[5]) {
      onReturn();
      showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
          "4004COMMON000000053")/* @res "返回成功" */);
    }

  }

  /**
   * 此处插入方法说明。 功能描述: 输入参数: 返回值: 异常处理: 日期:
   */
  public boolean beforeEdit(BillEditEvent event) {
    int row=event.getRow();
    String key=event.getKey();
    //如果是暂估维护界面
    if (getBillCardPanel2().isShowing()) {
       if("nexchangeotobrate".equalsIgnoreCase(key)){
          // 必须调用!!!!!!!!
          getBillCardPanel2().stopEditing();
          // 得到币种
          String sCurrId = (String) getBillCardPanel2().getBodyValueAt(row,
                  "currencytypeid");
          getBillCardPanel2().getBodyItem("nexchangeotobrate")
                  .setDecimalDigits(getDigits_ExchangeRate(sCurrId)[0]);
        }
       
       return true;
    }
   
    //如果是入库单维护界面
    if (event.getPos() == BillItem.BODY) {

      if ("vbatchcode".equalsIgnoreCase(key)) {
        beforeEditWhenBodyBatch(getBillCardPanel1(), m_sUnitCode, event,
            new String[] {
                "cinventoryid", "cinventorycode", "cinventoryname",
                "cinventoryspec", "cinventorytype", "cinventoryunit", null,
                null, "cwarehouseid"
            }, "vfree");
      }
      else if ("vfree".equalsIgnoreCase(key)) {
        // 用户模板定义可修改情况下的处理
        return PuTool.beforeEditInvBillBodyFree(getBillCardPanel1(), event,
            new String[] {
                "cinventoryid", "cinventorycode", "cinventoryname",
                "cinventoryspec", "cinventorytype"
            }, new String[] {
                "vfree", "vfree1", "vfree2", "vfree3", "vfree4", "vfree5"
            });
        // // 设置自由项自定义参照
        // InvVO invVO = new InvVO();
        // String sBaseId = (String) getBillCardPanel1().getBodyValueAt(
        // event.getRow(), "cbaseid");
        // if (sBaseId == null)
        // return true;
        //
        // // 获得存货编码，存货名称，存货规格，存货型号及存货基础ID
        // invVO.setCinventorycode((String) getBillCardPanel1()
        // .getBodyValueAt(event.getRow(), "cinventorycode"));
        // invVO.setInvname((String) getBillCardPanel1().getBodyValueAt(
        // event.getRow(), "cinventoryname"));
        // invVO.setInvspec((String) getBillCardPanel1().getBodyValueAt(
        // event.getRow(), "cinventoryspec"));
        // invVO.setInvtype((String) getBillCardPanel1().getBodyValueAt(
        // event.getRow(), "cinventorytype"));
        // invVO.setCinventoryid((String) getBillCardPanel1()
        // .getBodyValueAt(event.getRow(), "cbaseid"));
        // try {
        // FreeVO freeVO = nc.ui.pr.pray.PraybillHelper
        // .queryVOForFreeItem(invVO.getCinventoryid());
        // invVO.setFreeItemVO(freeVO);
        // } catch (Exception e) {
        // SCMEnv.out(e);
        // MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes
        // .getInstance().getStrByID("40040503",
        // "UPP40040503-000047")/*
        // * @res "设置自由项自定义参照"
        // */, e.getMessage());
        // }
        //
        // invVO.setIsFreeItemMgt(new Integer(1));
        // m_freeItem.setFreeItemParam(invVO);
      }
    }
    return true;
  }

  /**
   * 此处插入方法说明。 功能描述: 输入参数: BillCardPanle panle 单据模版 String pk_corp 公司主键
   * BillEditEvent e 事件 String[] invFieldNames
   * 存货信息字段名称（存货管理ID/编码/名称/规格/型号/主计量单位名称/辅计量单位ID/是否辅计量管理） + 仓库字段名称（仓库ID） String
   * freePrefix 自由项信息字段前缀 返回值: 异常处理: 日期:
   */
  private void beforeEditWhenBodyBatch(BillCardPanel panel, String pk_corp,
      BillEditEvent event, String[] invFieldNames, String freePrefix) {

    // 合法性判断
    if (panel == null || pk_corp == null || pk_corp.length() <= 0
        || invFieldNames == null || invFieldNames.length != 9
        || freePrefix == null || freePrefix.length() <= 0) {
      SCMEnv.out("传入参数不完整！");
    }

    panel.stopEditing();
    int row = event.getRow();
    Object omangid = panel.getBodyValueAt(row, invFieldNames[0]);
    if (omangid == null || omangid.toString().length() <= 0)
      return;
    String cmangid = omangid.toString();

    InvVO invVO = new InvVO();

    // 组织仓库信息
    Object owhid = panel.getHeadItem(invFieldNames[8]).getValue();
    WhVO whvo = new WhVO();
    if (owhid != null && owhid.toString().length() > 0) {
      String cwhid = owhid.toString();
      UIRefPane refPane = new UIRefPane();
      refPane.setRefNodeName("仓库档案");
      refPane.setValue(cwhid);
      refPane.setPK(cwhid);
      // refPane.getRefModel().reloadData();
      whvo.setCwarehouseid(cwhid);
      whvo.setCwarehousecode(refPane.getRefCode());
      whvo.setCwarehousename(refPane.getRefName());
      whvo.setIsWasteWh(new Integer(0));
      whvo.setPk_corp(pk_corp);
    }

    // 组织存货信息
    ArrayList listI = new ArrayList();
    listI.add(cmangid);
    ArrayList retList = null;
    try {
      ArrayList allList = new ArrayList();
      allList.add(listI);
      retList = DefHelper.queryFreeVOByInvIDsGroupByBills(allList);
    }
    catch (Exception exception) {
      SCMEnv.out(exception);
    }

    if (retList != null && retList.size() > 0) {
      ArrayList freeList = (ArrayList) (retList.get(0));
      FreeVO freeVO = (FreeVO) freeList.get(0);

      // 是否自由项管理
      if (freeVO == null
          || ((freeVO.getVfreename1() == null || freeVO.getVfreename1()
              .length() < 1)
              && (freeVO.getVfreename2() == null || freeVO.getVfreename2()
                  .length() < 1)
              && (freeVO.getVfreename3() == null || freeVO.getVfreename3()
                  .length() < 1)
              && (freeVO.getVfreename4() == null || freeVO.getVfreename4()
                  .length() < 1) && (freeVO.getVfreename5() == null || freeVO
              .getVfreename5().length() < 1))) {

        // 非自由项管理
        invVO.setIsFreeItemMgt(new Integer(0));
      }
      else {
        // 自由项管理
        invVO.setIsFreeItemMgt(new Integer(1));
        // 自由项
        freeVO.setCinventoryid(cmangid);
        for (int i = 1; i < 6; i++) {
          Object oTemp = panel.getBodyValueAt(row, freePrefix + i);
          freeVO.setAttributeValue("vfree" + i, oTemp == null ? null : oTemp
              .toString());
        }
        // 设置FreeVO
        invVO.setFreeItemVO(freeVO);

      }
    }
    else {
      // 非自由项管理
      invVO.setIsFreeItemMgt(new Integer(0));
    }
    // 存货管理ID
    invVO.setCinventoryid(cmangid);

    // 存货编码
    Object strTemp = panel.getBodyValueAt(row, invFieldNames[1]);
    invVO.setCinventorycode(strTemp == null ? null : strTemp.toString());
    // 存货名称
    strTemp = panel.getBodyValueAt(row, invFieldNames[2]);
    invVO.setInvname(strTemp == null ? null : strTemp.toString());
    // 规格
    strTemp = panel.getBodyValueAt(row, invFieldNames[3]);
    invVO.setInvspec(strTemp == null ? null : strTemp.toString());
    // 型号
    strTemp = panel.getBodyValueAt(row, invFieldNames[4]);
    invVO.setInvtype(strTemp == null ? null : strTemp.toString());
    // 主计量单位名称
    strTemp = panel.getBodyValueAt(row, invFieldNames[5]);
    invVO.setMeasdocname(strTemp == null ? null : strTemp.toString());
    // 是否辅计量管理
    // strTemp = panel.getBodyValueAt(row, invFieldNames[7]);
    // if(strTemp != null){
    // strTemp = (strTemp.toString().equalsIgnoreCase("Y") ? new Integer(1):
    // new Integer(0));
    // }
    // else strTemp = new Integer(0);
    // invVO.setIsAstUOMmgt((Integer)strTemp);
    // 辅计量单位ID
    // strTemp = panel.getBodyValueAt(row, invFieldNames[6]);
    // invVO.setCastunitid(strTemp == null ? null : strTemp.toString()) ;

    // nc.ui.ic.pub.lot.LotNumbRefPane refPane =
    // (nc.ui.ic.pub.lot.LotNumbRefPane)panel.getBodyItem("vbatchcode").getComponent();
    IICPub_LotNumbRefPane refPane = (IICPub_LotNumbRefPane) panel.getBodyItem(
        "vbatchcode").getComponent();
    refPane.setParameter(whvo, invVO);

  }

  /**
   * 此处插入方法说明。 功能描述:检查批次号是否输入 输入参数: 返回值: 异常处理:
   * 
   * @return boolean
   * @param VO
   *          nc.vo.pr.pray.PraybillVO
   */
  private String checkBatchCode() {
    String sErr = "";
    int nRow = getBillCardPanel1().getRowCount();
    int nCol = getBillCardPanel1().getBodyColByKey("vbatchcode");

    for (int i = 0; i < nRow; i++) {
      if (getBillCardPanel1().getBillModel().isCellEditable(i, nCol)) {
        Object oTemp = getBillCardPanel1().getBodyValueAt(i, "vbatchcode");
        if (oTemp == null || oTemp.toString().trim().length() == 0) {
          String[] value = new String[] {
            String.valueOf(i + 1)
          };
          sErr += nc.ui.ml.NCLangRes.getInstance().getStrByID("40040503",
              "UPP40040503-000048", null, value)/*
                                                 * @res "行" +
                                                 * CommonConstant.BEGIN_MARK +
                                                 * (i + 1) +
                                                 * CommonConstant.END_MARK +
                                                 * "未录入批次号！\n"
                                                 */;
        }
      }
    }

    return sErr;
  }

  /**
   * 此处插入方法说明。 功能描述:检查入库单号是否重复 输入参数: 返回值: 异常处理:
   * 
   * @return boolean
   * @param VO
   *          nc.vo.pr.pray.PraybillVO
   *
   *@modified by zhaoyha at 2009.9 移动到后台检查
   *@deprecated
   */
  private boolean checkBeforeSave(GeneralHVO VO, boolean bAdd) {
    boolean b = false;

    try {
      if (bAdd)
        b = EstimateHelper.isCodeDuplicate(m_sUnitCode, VO.getHeadVO()
            .getVbillcode(), null);
      else
        b = EstimateHelper.isCodeDuplicate(m_sUnitCode, VO.getHeadVO()
            .getVbillcode(), m_VOs[m_nPresentRecord].getHeadVO()
            .getCgeneralhid().trim());
    }
    catch (Exception e) {
      MessageDialog.showErrorDlg(this,
          nc.ui.ml.NCLangRes.getInstance().getStrByID("40040503",
              "UPP40040503-000049")/* @res "检查入库单号是否重复" */, e.toString());
      SCMEnv.out(e);
      return true;
    }

    return b;
  }

  /**
   * 此处插入方法说明。 功能描述:检查自由项是否输入 输入参数: 返回值: 异常处理:
   * 
   * @return boolean
   * @param VO
   *          nc.vo.pr.pray.PraybillVO
   */
  private String checkFreeItem() {
    String sErr = "";
    int nRow = getBillCardPanel1().getRowCount();
    int nCol = getBillCardPanel1().getBodyColByKey("vfree");

    for (int i = 0; i < nRow; i++) {
      if (getBillCardPanel1().getBillModel().isCellEditable(i, nCol)) {
        Object oTemp = getBillCardPanel1().getBodyValueAt(i, "vfree");
        if (oTemp == null || oTemp.toString().trim().length() == 0) {
          String[] value = new String[] {
            String.valueOf(i + 1)
          };
          sErr += nc.ui.ml.NCLangRes.getInstance().getStrByID("40040503",
              "UPP40040503-000050", null, value)/*
                                                 * @res "行" +
                                                 * CommonConstant.BEGIN_MARK +
                                                 * (i + 1) +
                                                 * CommonConstant.END_MARK +
                                                 * "未录入自由项！\n"
                                                 */;
        }
      }
    }

    return sErr;
  }

  /**
   * 此处插入方法说明。 功能描述:卡片界面，修改数量时,金额自动变化;修改单价时，金额自动变化；修改金额时，单价自动变化 输入参数: 返回值: 异常处理:
   */
  private void computeBBodyData(BillEditEvent event) {
    // 需要计算的表体行
    int n = event.getRow();

    UFDouble nInNum = null;
    Object oTemp = getBillCardPanel1().getBodyValueAt(n, "ninnum");
    if (oTemp != null && oTemp.toString().length() > 0)
      nInNum = (UFDouble) oTemp;
    UFDouble nPrice = null;
    oTemp = getBillCardPanel1().getBodyValueAt(n, "nprice");
    if (oTemp != null && oTemp.toString().length() > 0)
      nPrice = (UFDouble) oTemp;
    UFDouble nMny = null;
    oTemp = getBillCardPanel1().getBodyValueAt(n, "nmny");
    if (oTemp != null && oTemp.toString().length() > 0)
      nMny = (UFDouble) oTemp;

    if (event.getKey().trim().equals("ninnum")) {
      // 数量变化，金额自动变化
      if (nInNum != null) {
        UFDouble ninnum = m_VOs[m_nPresentRecord].getBodyVO()[n].getNinnum();
        if (ninnum != null && nInNum.doubleValue() * ninnum.doubleValue() < 0.0) {
          MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes.getInstance()
              .getStrByID("40040503", "UPP40040503-000051")/*
                                                             * @res "修改数量"
                                                             */, nc.ui.ml.NCLangRes.getInstance().getStrByID("40040503",
              "UPP40040503-000052")/*
                                     * @res "修改数量不能改变符号！"
                                     */);
          getBillCardPanel1().setBodyValueAt(ninnum, n, "ninnum");
          return;
        }
        if (nInNum.doubleValue() == 0.0) {
          MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes.getInstance()
              .getStrByID("40040503", "UPP40040503-000051")/*
                                                             * @res "修改数量"
                                                             */, nc.ui.ml.NCLangRes.getInstance().getStrByID("40040503",
              "UPP40040503-000053")/*
                                     * @res "暂估数量不能为零！"
                                     */);
          getBillCardPanel1().setBodyValueAt(ninnum, n, "ninnum");
          return;
        }

        if (nPrice != null && nPrice.doubleValue() != 0.0) {
          double d = nPrice.doubleValue() * nInNum.doubleValue();
          getBillCardPanel1().setBodyValueAt(new UFDouble(d), n, "nmny");
        }
      }
      else
        getBillCardPanel1().setBodyValueAt(null, n, "nmny");

    }
    else if (event.getKey().trim().equals("nprice")) {
      // 单价变化，金额自动变化
      if (nPrice != null) {
        if (nInNum != null) {
          double d = nInNum.doubleValue() * nPrice.doubleValue();
          getBillCardPanel1().setBodyValueAt(new UFDouble(d), n, "nmny");
        }
      }
      else
        getBillCardPanel1().setBodyValueAt(null, n, "nmny");

    }
    else if (event.getKey().trim().equals("nmny")) {
      // 金额变化，单价自动变化
      if (nMny != null) {
        UFDouble nmny = m_VOs[m_nPresentRecord].getBodyVO()[n].getNmny();
        if (nmny != null && nInNum != null
            && nMny.doubleValue() * nInNum.doubleValue() < 0.0) {
          MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes.getInstance()
              .getStrByID("40040503", "UPP40040503-000022")/*
                                                             * @res "修改金额"
                                                             */, nc.ui.ml.NCLangRes.getInstance().getStrByID("40040503",
              "UPP40040503-000023")/*
                                     * @res "修改金额不能改变符号！"
                                     */);
          getBillCardPanel1().setBodyValueAt(nmny, n, "nmny");
          return;
        }

        if (nInNum != null && nInNum.doubleValue() != 0.0) {
          double d = nMny.doubleValue() / nInNum.doubleValue();
          getBillCardPanel1().setBodyValueAt(new UFDouble(d), n, "nprice");
        }
      }
      else
        getBillCardPanel1().setBodyValueAt(null, n, "nprice");
    }
  }

  /**
   * 此处插入方法说明。 功能描述:订单查询界面，修改暂估数量时，暂估金额自动变化； 修改暂估单价时，暂估金额自动变化；修改暂估金额，暂估单价自动变化
   * 输入参数: 返回值: 异常处理:
   */
  private void computeBodyData(BillEditEvent event) {
    // 需要计算的表体行
    int n = event.getRow();

    UFDouble nGaugeNum = null;
    Object oTemp = getBillCardPanel2().getBodyValueAt(n, "ngaugenum");
    if (oTemp != null && oTemp.toString().length() > 0)
      nGaugeNum = (UFDouble) oTemp;
    UFDouble nGaugePrice = null;
    oTemp = getBillCardPanel2().getBodyValueAt(n, "nprice");
    if (oTemp != null && oTemp.toString().length() > 0)
      nGaugePrice = (UFDouble) oTemp;
    UFDouble nGaugeMny = null;
    oTemp = getBillCardPanel2().getBodyValueAt(n, "nmoney");
    if (oTemp != null && oTemp.toString().length() > 0)
      nGaugeMny = (UFDouble) oTemp;

    if (event.getKey().trim().equals("ngaugenum")) {
      // 暂估数量变化，金额自动变化
      if (nGaugeNum != null && nGaugeNum.toString().length() > 0) {
        if (m_orderVOs[n].getNgaugenum() != null
            && nGaugeNum.doubleValue()
                * m_orderVOs[n].getNgaugenum().doubleValue() < 0.0) {
          MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes.getInstance()
              .getStrByID("40040503", "UPP40040503-000054")/*
                                                             * @res "修改暂估数量"
                                                             */, nc.ui.ml.NCLangRes.getInstance().getStrByID("40040503",
              "UPP40040503-000055")/*
                                     * @res "修改暂估数量不能改变符号！"
                                     */);
          getBillCardPanel2().setBodyValueAt(m_orderVOs[n].getNgaugenum(), n,
              "ngaugenum");
          return;
        }
        if (nGaugeNum.doubleValue() == 0.0) {
          MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes.getInstance()
              .getStrByID("40040503", "UPP40040503-000054")/*
                                                             * @res "修改暂估数量"
                                                             */, nc.ui.ml.NCLangRes.getInstance().getStrByID("40040503",
              "UPP40040503-000056")/*
                                     * @res "修改暂估数量不能等于零！"
                                     */);
          getBillCardPanel2().setBodyValueAt(m_orderVOs[n].getNgaugenum(), n,
              "ngaugenum");
          return;
        }
        if (nGaugePrice != null) {
          double d = nGaugeNum.doubleValue() * nGaugePrice.doubleValue();
          getBillCardPanel2().setBodyValueAt(new UFDouble(d), n, "nmoney");
        }
      }
      else
        getBillCardPanel2().setBodyValueAt(null, n, "nmoney");

      m_orderVOs[n].setNgaugenum(nGaugeNum);
      m_orderVOs[n].setNmoney((UFDouble) getBillCardPanel2().getBodyValueAt(n,
          "nmoney"));
    }
    else if (event.getKey().trim().equals("nprice")) {
      // 暂估单价变化，金额自动变化
      if (nGaugePrice != null && nGaugePrice.toString().length() > 0) {
        if (nGaugeNum != null) {
          double d = nGaugeNum.doubleValue() * nGaugePrice.doubleValue();
          getBillCardPanel2().setBodyValueAt(new UFDouble(d), n, "nmoney");
        }
      }
      else
        getBillCardPanel2().setBodyValueAt(null, n, "nmoney");

      m_orderVOs[n].setNprice(nGaugePrice);
      m_orderVOs[n].setNmoney((UFDouble) getBillCardPanel2().getBodyValueAt(n,
          "nmoney"));
    }
    else if (event.getKey().trim().equals("nmoney")) {
      // 暂估金额变化，单价自动变化
      if (nGaugeMny != null && nGaugeMny.toString().length() > 0) {
        if (nGaugeNum != null
            && nGaugeMny.doubleValue() * nGaugeNum.doubleValue() < 0.0) {
          MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes.getInstance()
              .getStrByID("40040503", "UPP40040503-000057")/*
                                                             * @res "修改暂估金额"
                                                             */, nc.ui.ml.NCLangRes.getInstance().getStrByID("40040503",
              "UPP40040503-000058")/*
                                     * @res "修改暂估金额不能改变符号！"
                                     */);
          getBillCardPanel2().setBodyValueAt(m_orderVOs[n].getNmoney(), n,
              "nmoney");
          return;
        }
        if (nGaugeNum != null && nGaugeNum.doubleValue() != 0.0) {
          double d = nGaugeMny.doubleValue() / nGaugeNum.doubleValue();
          getBillCardPanel2().setBodyValueAt(new UFDouble(d), n, "nprice");
        }
      }
      else
        getBillCardPanel2().setBodyValueAt(null, n, "nprice");

      m_orderVOs[n].setNmoney(nGaugeMny);
      m_orderVOs[n].setNprice((UFDouble) getBillCardPanel2().getBodyValueAt(n,
          "nprice"));

    }
  }

  /**
   * 此处插入方法说明。 功能描述:根据订单，按存货编码+供应商生成入库单 注意：库存组织、业务员、部门、业务类型、采购公司是系统缺省的分单条件。
   * 输入参数: 返回值:入库单VO 异常处理:
   */
  private GeneralHVO[] doGenerationByInv(OorderVO tempVOs[], Vector vMixture) {
    if (tempVOs == null || tempVOs.length == 0)
      return null;
    // 优化效率，提供批次查询收发方式
    String sRSMode[] = new String[tempVOs.length];
    for (int i = 0; i < tempVOs.length; i++)
      sRSMode[i] = tempVOs[i].getCbiztype();
    try {
      sRSMode = EstimateHelper.getRSModeBatch(sRSMode);
    }
    catch (Exception e) {
      SCMEnv.out(e);
      MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes.getInstance()
          .getStrByID("40040503", "UPP40040503-000059")/* @res "获得收发方式" */, e
          .getMessage());
      return null;
    }
    //

    Vector vv = new Vector();
    String sTemp = null, s1 = null, s2 = null, s3 = null, s4 = null, s5 = null, s6 = null, s7 = null, sID = null, s8 = null;
    boolean b = false;

    for (int i = 0; i < vMixture.size(); i++) {
      GeneralHHeaderVO headVO = new GeneralHHeaderVO();
      Vector v = new Vector();
      Vector v0 = new Vector();

      sTemp = (String) vMixture.elementAt(i);
      b = false;
      for (int j = 0; j < tempVOs.length; j++) {
        s1 = tempVOs[j].getCmangid();
        s2 = tempVOs[j].getCvendormangid();
        s3 = tempVOs[j].getCstoreorganization();
        s4 = tempVOs[j].getCemployeeid();
        s5 = tempVOs[j].getCdeptid();
        s6 = tempVOs[j].getCbiztype();
        s7 = tempVOs[j].getPk_corp();
        s8 = tempVOs[j].getPk_arrvcorp();
        if (s1 == null || s1.trim().length() == 0)
          s1 = "null";
        if (s2 == null || s2.trim().length() == 0)
          s2 = "null";
        if (s3 == null || s3.trim().length() == 0)
          s3 = "null";
        if (s4 == null || s4.trim().length() == 0)
          s4 = "null";
        if (s5 == null || s5.trim().length() == 0)
          s5 = "null";
        if (s6 == null || s6.trim().length() == 0)
          s6 = "null";
        if (s7 == null || s7.trim().length() == 0)
          s7 = "null";
        if (s8 == null || s8.trim().length() == 0)
          s8 = "null";
        sID = s1 + s2 + s3 + s4 + s5 + s6 + s7 + s8;
        if (sTemp.equals(sID)) {
          if (!b) {
            // 生成入库单表头
            headVO.setDbilldate(m_dSystem);
            headVO.setCbiztype(tempVOs[j].getCbiztype());
            headVO.setCproviderid(tempVOs[j].getCvendormangid());
            headVO.setPk_cubasdoc(tempVOs[j].getCvendorbaseid());
            headVO.setCdptid(tempVOs[j].getCdeptid());
            headVO.setCbizid(tempVOs[j].getCemployeeid());
            headVO.setPk_corp(tempVOs[j].getPk_arrvcorp());// 收货公司
            headVO.setPk_purcorp(tempVOs[j].getPk_corp());// 采购公司
            headVO.setCstoreorganization(tempVOs[j].getPk_arrvstoorg());// 收货库存组织
            headVO.setCwarehouseid(tempVOs[j].getCwarehouseid());
            headVO.setCbilltypecode("4T");
            headVO.setCregister(getClientEnvironment().getUser()
                .getPrimaryKey());
            headVO.setCdispatcherid(sRSMode[j]);
            headVO.setCoperatorid(getClientEnvironment().getUser()
                .getPrimaryKey());

            headVO.setVuserdef1(tempVOs[j].getHvdef1());
            headVO.setVuserdef2(tempVOs[j].getHvdef2());
            headVO.setVuserdef3(tempVOs[j].getHvdef3());
            headVO.setVuserdef4(tempVOs[j].getHvdef4());
            headVO.setVuserdef5(tempVOs[j].getHvdef5());
            headVO.setVuserdef6(tempVOs[j].getHvdef6());
            headVO.setVuserdef7(tempVOs[j].getHvdef7());
            headVO.setVuserdef8(tempVOs[j].getHvdef8());
            headVO.setVuserdef9(tempVOs[j].getHvdef9());
            headVO.setVuserdef10(tempVOs[j].getHvdef10());
            headVO.setVuserdef11(tempVOs[j].getHvdef11());
            headVO.setVuserdef12(tempVOs[j].getHvdef12());
            headVO.setVuserdef13(tempVOs[j].getHvdef13());
            headVO.setVuserdef14(tempVOs[j].getHvdef14());
            headVO.setVuserdef15(tempVOs[j].getHvdef15());
            headVO.setVuserdef16(tempVOs[j].getHvdef16());
            headVO.setVuserdef17(tempVOs[j].getHvdef17());
            headVO.setVuserdef18(tempVOs[j].getHvdef18());
            headVO.setVuserdef19(tempVOs[j].getHvdef19());
            headVO.setVuserdef20(tempVOs[j].getHvdef20());

            headVO.setPk_defdoc1(tempVOs[j].getHpkdef1());
            headVO.setPk_defdoc2(tempVOs[j].getHpkdef2());
            headVO.setPk_defdoc3(tempVOs[j].getHpkdef3());
            headVO.setPk_defdoc4(tempVOs[j].getHpkdef4());
            headVO.setPk_defdoc5(tempVOs[j].getHpkdef5());
            headVO.setPk_defdoc6(tempVOs[j].getHpkdef6());
            headVO.setPk_defdoc7(tempVOs[j].getHpkdef7());
            headVO.setPk_defdoc8(tempVOs[j].getHpkdef8());
            headVO.setPk_defdoc9(tempVOs[j].getHpkdef9());
            headVO.setPk_defdoc10(tempVOs[j].getHpkdef10());
            headVO.setPk_defdoc11(tempVOs[j].getHpkdef11());
            headVO.setPk_defdoc12(tempVOs[j].getHpkdef12());
            headVO.setPk_defdoc13(tempVOs[j].getHpkdef13());
            headVO.setPk_defdoc14(tempVOs[j].getHpkdef14());
            headVO.setPk_defdoc15(tempVOs[j].getHpkdef15());
            headVO.setPk_defdoc16(tempVOs[j].getHpkdef16());
            headVO.setPk_defdoc17(tempVOs[j].getHpkdef17());
            headVO.setPk_defdoc18(tempVOs[j].getHpkdef18());
            headVO.setPk_defdoc19(tempVOs[j].getHpkdef19());
            headVO.setPk_defdoc20(tempVOs[j].getHpkdef20());
            headVO.setVnote(tempVOs[j].getVmemoHead());
          }

          // 生成入库单表体
          GeneralHItemVO bodyVO = new GeneralHItemVO();
          // since v502
          bodyVO.setBLargess(tempVOs[j].getBLargess());
          //
          bodyVO.setCsourcebillhid(tempVOs[j].getCorderid());
          bodyVO.setCsourcebillbid(tempVOs[j].getCorder_bid());
          bodyVO.setVsourcebillcode(tempVOs[j].getVordercode());
          bodyVO.setCsourcetype(ScmConst.PO_Order);
          bodyVO.setBzgflag(new UFBoolean(true));
          bodyVO.setIsok(new UFBoolean(false));
          bodyVO.setCinventoryid(tempVOs[j].getCmangid());
          bodyVO.setNinnum(tempVOs[j].getNgaugenum());
          bodyVO.setNprice(tempVOs[j].getNprice());
          bodyVO.setNmny(tempVOs[j].getNmoney());

          bodyVO.setVfree1(tempVOs[j].getVfree1());
          bodyVO.setVfree2(tempVOs[j].getVfree2());
          bodyVO.setVfree3(tempVOs[j].getVfree3());
          bodyVO.setVfree4(tempVOs[j].getVfree4());
          bodyVO.setVfree5(tempVOs[j].getVfree5());

          bodyVO.setCprojectid(tempVOs[j].getCprojectid());
          bodyVO.setCprojectphaseid(tempVOs[j].getCprojectphaseid());

          bodyVO.setCfirstbillhid(tempVOs[j].getCorderid());
          bodyVO.setCfirstbillbid(tempVOs[j].getCorder_bid());
          bodyVO.setCfirsttype(ScmConst.PO_Order);
          // 批次号
          bodyVO.setVbatchcode(tempVOs[j].getVbatchcode());
          bodyVO.setFlargess(new UFBoolean(false));
          // 到货计划行ID
          bodyVO.setCorder_bb1id(tempVOs[j].getCorder_bb1id());
          bodyVO.setVuserdef1(tempVOs[j].getBvdef1());
          bodyVO.setVuserdef2(tempVOs[j].getBvdef2());
          bodyVO.setVuserdef3(tempVOs[j].getBvdef3());
          bodyVO.setVuserdef4(tempVOs[j].getBvdef4());
          bodyVO.setVuserdef5(tempVOs[j].getBvdef5());
          bodyVO.setVuserdef6(tempVOs[j].getBvdef6());
          bodyVO.setVuserdef7(tempVOs[j].getBvdef7());
          bodyVO.setVuserdef8(tempVOs[j].getBvdef8());
          bodyVO.setVuserdef9(tempVOs[j].getBvdef9());
          bodyVO.setVuserdef10(tempVOs[j].getBvdef10());
          bodyVO.setVuserdef11(tempVOs[j].getBvdef11());
          bodyVO.setVuserdef12(tempVOs[j].getBvdef12());
          bodyVO.setVuserdef13(tempVOs[j].getBvdef13());
          bodyVO.setVuserdef14(tempVOs[j].getBvdef14());
          bodyVO.setVuserdef15(tempVOs[j].getBvdef15());
          bodyVO.setVuserdef16(tempVOs[j].getBvdef16());
          bodyVO.setVuserdef17(tempVOs[j].getBvdef17());
          bodyVO.setVuserdef18(tempVOs[j].getBvdef18());
          bodyVO.setVuserdef19(tempVOs[j].getBvdef19());
          bodyVO.setVuserdef20(tempVOs[j].getBvdef20());

          bodyVO.setPk_defdoc1(tempVOs[j].getBpkdef1());
          bodyVO.setPk_defdoc2(tempVOs[j].getBpkdef2());
          bodyVO.setPk_defdoc3(tempVOs[j].getBpkdef3());
          bodyVO.setPk_defdoc4(tempVOs[j].getBpkdef4());
          bodyVO.setPk_defdoc5(tempVOs[j].getBpkdef5());
          bodyVO.setPk_defdoc6(tempVOs[j].getBpkdef6());
          bodyVO.setPk_defdoc7(tempVOs[j].getBpkdef7());
          bodyVO.setPk_defdoc8(tempVOs[j].getBpkdef8());
          bodyVO.setPk_defdoc9(tempVOs[j].getBpkdef9());
          bodyVO.setPk_defdoc10(tempVOs[j].getBpkdef10());
          bodyVO.setPk_defdoc11(tempVOs[j].getBpkdef11());
          bodyVO.setPk_defdoc12(tempVOs[j].getBpkdef12());
          bodyVO.setPk_defdoc13(tempVOs[j].getBpkdef13());
          bodyVO.setPk_defdoc14(tempVOs[j].getBpkdef14());
          bodyVO.setPk_defdoc15(tempVOs[j].getBpkdef15());
          bodyVO.setPk_defdoc16(tempVOs[j].getBpkdef16());
          bodyVO.setPk_defdoc17(tempVOs[j].getBpkdef17());
          bodyVO.setPk_defdoc18(tempVOs[j].getBpkdef18());
          bodyVO.setPk_defdoc19(tempVOs[j].getBpkdef19());
          bodyVO.setPk_defdoc20(tempVOs[j].getBpkdef20());
          bodyVO.setVnote(tempVOs[j].getVmemoBody());

          // 暂估日期
          // bodyVO.setDzgdate(m_dSystem);
          // bodyVO.setDbizdate(m_dSystem);

          bodyVO.setDzgdate(headVO.getDbilldate());
          bodyVO.setDbizdate(headVO.getDbilldate());

          //
          bodyVO.setPk_reqcorp(tempVOs[j].getPk_reqcorp());// 需求公司
          bodyVO.setPk_invoicecorp(tempVOs[j].getPk_invoicecorp());// 收票公司
          bodyVO.setPk_reqstoorg(tempVOs[j].getPk_reqstoorg());// 需求库存组织
          bodyVO.setPk_creqwareid(tempVOs[j].getPk_creqwareid());// 需求仓库
          bodyVO.setPk_corp(headVO.getPk_corp());

          bodyVO.setCbaseid(tempVOs[j].getCbaseid());
          bodyVO.setCvendorid(tempVOs[j].getCvendormangid());

          // 生成入库单子子表
          GeneralBb3VO bb3VO = new GeneralBb3VO();
          bb3VO.setNaccountnum1(new UFDouble(0.0));
          bb3VO.setNaccountmny(new UFDouble(0.0));
          bb3VO.setNpprice(tempVOs[j].getNprice());
          bb3VO.setNpmoney(tempVOs[j].getNmoney());

          b = true;
          v.addElement(bodyVO);
          v0.addElement(bb3VO);
        }
      }

      // 生成入库单
      if (v.size() > 0) {
        GeneralHItemVO bodyVOs[] = new GeneralHItemVO[v.size()];
        v.copyInto(bodyVOs);

        GeneralBb3VO bb3VOs[] = new GeneralBb3VO[v0.size()];
        v0.copyInto(bb3VOs);

        GeneralHVO vo = new GeneralHVO(v.size());
        vo.setParentVO(headVO);
        vo.setChildrenVO(bodyVOs);
        vo.setGrandVO(bb3VOs);

        vv.addElement(vo);
      }

    }

    // 返回
    if (vv.size() > 0) {
      GeneralHVO returnVOs[] = new GeneralHVO[vv.size()];
      vv.copyInto(returnVOs);
      return returnVOs;
    }

    return null;
  }

  /**
   * 此处插入方法说明。 功能描述:根据订单，按仓库ID+订单号生成入库单 注意：库存组织、业务员、部门、业务类型、采购公司是系统缺省的分单条件。
   * 输入参数: 返回值:入库单VO 异常处理:
   */
  private GeneralHVO[] doGenerationByMixture(OorderVO tempVOs[], Vector vMixture) {
    if (tempVOs == null || tempVOs.length == 0)
      return null;
    // 优化效率，提供批次查询收发方式
    String sRSMode[] = new String[tempVOs.length];
    for (int i = 0; i < tempVOs.length; i++)
      sRSMode[i] = tempVOs[i].getCbiztype();
    try {
      sRSMode = EstimateHelper.getRSModeBatch(sRSMode);
    }
    catch (Exception e) {
      SCMEnv.out(e);
      MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes.getInstance()
          .getStrByID("40040503", "UPP40040503-000059")/* @res "获得收发方式" */, e
          .getMessage());
      return null;
    }
    //

    Vector vv = new Vector();
    String sTemp = null, s1 = null, s2 = null, s3 = null, s4 = null, s5 = null, s6 = null, s7 = null, sID = null, s8 = null;
    boolean b = false;

    for (int i = 0; i < vMixture.size(); i++) {
      GeneralHHeaderVO headVO = new GeneralHHeaderVO();
      Vector v = new Vector();
      Vector v0 = new Vector();

      sTemp = (String) vMixture.elementAt(i);
      b = false;
      for (int j = 0; j < tempVOs.length; j++) {
        s1 = tempVOs[j].getCwarehouseid();
        s2 = tempVOs[j].getVordercode();
        s3 = tempVOs[j].getCstoreorganization();
        s4 = tempVOs[j].getCemployeeid();
        s5 = tempVOs[j].getCdeptid();
        s6 = tempVOs[j].getCbiztype();
        s7 = tempVOs[j].getPk_corp();
        s8 = tempVOs[j].getPk_arrvcorp();
        if (s1 == null || s1.trim().length() == 0)
          s1 = "null";
        if (s2 == null || s2.trim().length() == 0)
          s2 = "null";
        if (s3 == null || s3.trim().length() == 0)
          s3 = "null";
        if (s4 == null || s4.trim().length() == 0)
          s4 = "null";
        if (s5 == null || s5.trim().length() == 0)
          s5 = "null";
        if (s6 == null || s6.trim().length() == 0)
          s6 = "null";
        if (s7 == null || s7.trim().length() == 0)
          s7 = "null";
        if (s8 == null || s8.trim().length() == 0)
          s8 = "null";
        sID = s1 + s2 + s3 + s4 + s5 + s6 + s7 + s8;
        if (sTemp.equals(sID)) {
          if (!b) {
            // 生成入库单表头
            headVO.setDbilldate(m_dSystem);
            headVO.setCbiztype(tempVOs[j].getCbiztype());
            headVO.setCproviderid(tempVOs[j].getCvendormangid());
            headVO.setPk_cubasdoc(tempVOs[j].getCvendorbaseid());
            headVO.setCdptid(tempVOs[j].getCdeptid());
            headVO.setCbizid(tempVOs[j].getCemployeeid());
            headVO.setPk_corp(tempVOs[j].getPk_arrvcorp());// 收货公司
            headVO.setPk_purcorp(tempVOs[j].getPk_corp());// 采购公司
            headVO.setCstoreorganization(tempVOs[j].getPk_arrvstoorg());// 收货库存组织
            headVO.setCwarehouseid(tempVOs[j].getCwarehouseid());
            headVO.setCbilltypecode("4T");
            headVO.setCregister(getClientEnvironment().getUser()
                .getPrimaryKey());
            headVO.setCdispatcherid(sRSMode[j]);
            headVO.setCoperatorid(getClientEnvironment().getUser()
                .getPrimaryKey());
            
            headVO.setVuserdef1(tempVOs[j].getHvdef1());
            headVO.setVuserdef2(tempVOs[j].getHvdef2());
            headVO.setVuserdef3(tempVOs[j].getHvdef3());
            headVO.setVuserdef4(tempVOs[j].getHvdef4());
            headVO.setVuserdef5(tempVOs[j].getHvdef5());
            headVO.setVuserdef6(tempVOs[j].getHvdef6());
            headVO.setVuserdef7(tempVOs[j].getHvdef7());
            headVO.setVuserdef8(tempVOs[j].getHvdef8());
            headVO.setVuserdef9(tempVOs[j].getHvdef9());
            headVO.setVuserdef10(tempVOs[j].getHvdef10());
            headVO.setVuserdef11(tempVOs[j].getHvdef11());
            headVO.setVuserdef12(tempVOs[j].getHvdef12());
            headVO.setVuserdef13(tempVOs[j].getHvdef13());
            headVO.setVuserdef14(tempVOs[j].getHvdef14());
            headVO.setVuserdef15(tempVOs[j].getHvdef15());
            headVO.setVuserdef16(tempVOs[j].getHvdef16());
            headVO.setVuserdef17(tempVOs[j].getHvdef17());
            headVO.setVuserdef18(tempVOs[j].getHvdef18());
            headVO.setVuserdef19(tempVOs[j].getHvdef19());
            headVO.setVuserdef20(tempVOs[j].getHvdef20());

            headVO.setPk_defdoc1(tempVOs[j].getHpkdef1());
            headVO.setPk_defdoc2(tempVOs[j].getHpkdef2());
            headVO.setPk_defdoc3(tempVOs[j].getHpkdef3());
            headVO.setPk_defdoc4(tempVOs[j].getHpkdef4());
            headVO.setPk_defdoc5(tempVOs[j].getHpkdef5());
            headVO.setPk_defdoc6(tempVOs[j].getHpkdef6());
            headVO.setPk_defdoc7(tempVOs[j].getHpkdef7());
            headVO.setPk_defdoc8(tempVOs[j].getHpkdef8());
            headVO.setPk_defdoc9(tempVOs[j].getHpkdef9());
            headVO.setPk_defdoc10(tempVOs[j].getHpkdef10());
            headVO.setPk_defdoc11(tempVOs[j].getHpkdef11());
            headVO.setPk_defdoc12(tempVOs[j].getHpkdef12());
            headVO.setPk_defdoc13(tempVOs[j].getHpkdef13());
            headVO.setPk_defdoc14(tempVOs[j].getHpkdef14());
            headVO.setPk_defdoc15(tempVOs[j].getHpkdef15());
            headVO.setPk_defdoc16(tempVOs[j].getHpkdef16());
            headVO.setPk_defdoc17(tempVOs[j].getHpkdef17());
            headVO.setPk_defdoc18(tempVOs[j].getHpkdef18());
            headVO.setPk_defdoc19(tempVOs[j].getHpkdef19());
            headVO.setPk_defdoc20(tempVOs[j].getHpkdef20());
            headVO.setVnote(tempVOs[j].getVmemoHead());
          }

          // 生成入库单表体
          GeneralHItemVO bodyVO = new GeneralHItemVO();
          // since v502
          bodyVO.setBLargess(tempVOs[j].getBLargess());
          //
          bodyVO.setCsourcebillhid(tempVOs[j].getCorderid());
          bodyVO.setCsourcebillbid(tempVOs[j].getCorder_bid());
          bodyVO.setVsourcebillcode(tempVOs[j].getVordercode());
          bodyVO.setCsourcetype(ScmConst.PO_Order);
          bodyVO.setBzgflag(new UFBoolean(true));
          bodyVO.setIsok(new UFBoolean(false));
          bodyVO.setCinventoryid(tempVOs[j].getCmangid());
          bodyVO.setNinnum(tempVOs[j].getNgaugenum());
          bodyVO.setNprice(tempVOs[j].getNprice());
          bodyVO.setNmny(tempVOs[j].getNmoney());

          bodyVO.setVfree1(tempVOs[j].getVfree1());
          bodyVO.setVfree2(tempVOs[j].getVfree2());
          bodyVO.setVfree3(tempVOs[j].getVfree3());
          bodyVO.setVfree4(tempVOs[j].getVfree4());
          bodyVO.setVfree5(tempVOs[j].getVfree5());
          bodyVO.setCprojectid(tempVOs[j].getCprojectid());
          bodyVO.setCprojectphaseid(tempVOs[j].getCprojectphaseid());

          bodyVO.setCfirstbillhid(tempVOs[j].getCorderid());
          bodyVO.setCfirstbillbid(tempVOs[j].getCorder_bid());
          bodyVO.setCfirsttype(ScmConst.PO_Order);
          // 批次号
          bodyVO.setVbatchcode(tempVOs[j].getVbatchcode());
          bodyVO.setFlargess(new UFBoolean(false));
          // 暂估日期
          // bodyVO.setDzgdate(m_dSystem);
          // bodyVO.setDbizdate(m_dSystem);

          bodyVO.setDzgdate(headVO.getDbilldate());
          bodyVO.setDbizdate(headVO.getDbilldate());

          bodyVO.setPk_reqcorp(tempVOs[j].getPk_reqcorp());// 需求公司
          bodyVO.setPk_invoicecorp(tempVOs[j].getPk_invoicecorp());// 收票公司
          bodyVO.setPk_reqstoorg(tempVOs[j].getPk_reqstoorg());// 需求库存组织
          bodyVO.setPk_creqwareid(tempVOs[j].getPk_creqwareid());// 需求仓库
          bodyVO.setPk_corp(headVO.getPk_corp());

          bodyVO.setCbaseid(tempVOs[j].getCbaseid());
          bodyVO.setCvendorid(tempVOs[j].getCvendormangid());

          // 到货计划行ID
          bodyVO.setCorder_bb1id(tempVOs[j].getCorder_bb1id());
          bodyVO.setVuserdef1(tempVOs[j].getBvdef1());
          bodyVO.setVuserdef2(tempVOs[j].getBvdef2());
          bodyVO.setVuserdef3(tempVOs[j].getBvdef3());
          bodyVO.setVuserdef4(tempVOs[j].getBvdef4());
          bodyVO.setVuserdef5(tempVOs[j].getBvdef5());
          bodyVO.setVuserdef6(tempVOs[j].getBvdef6());
          bodyVO.setVuserdef7(tempVOs[j].getBvdef7());
          bodyVO.setVuserdef8(tempVOs[j].getBvdef8());
          bodyVO.setVuserdef9(tempVOs[j].getBvdef9());
          bodyVO.setVuserdef10(tempVOs[j].getBvdef10());
          bodyVO.setVuserdef11(tempVOs[j].getBvdef11());
          bodyVO.setVuserdef12(tempVOs[j].getBvdef12());
          bodyVO.setVuserdef13(tempVOs[j].getBvdef13());
          bodyVO.setVuserdef14(tempVOs[j].getBvdef14());
          bodyVO.setVuserdef15(tempVOs[j].getBvdef15());
          bodyVO.setVuserdef16(tempVOs[j].getBvdef16());
          bodyVO.setVuserdef17(tempVOs[j].getBvdef17());
          bodyVO.setVuserdef18(tempVOs[j].getBvdef18());
          bodyVO.setVuserdef19(tempVOs[j].getBvdef19());
          bodyVO.setVuserdef20(tempVOs[j].getBvdef20());

          bodyVO.setPk_defdoc1(tempVOs[j].getBpkdef1());
          bodyVO.setPk_defdoc2(tempVOs[j].getBpkdef2());
          bodyVO.setPk_defdoc3(tempVOs[j].getBpkdef3());
          bodyVO.setPk_defdoc4(tempVOs[j].getBpkdef4());
          bodyVO.setPk_defdoc5(tempVOs[j].getBpkdef5());
          bodyVO.setPk_defdoc6(tempVOs[j].getBpkdef6());
          bodyVO.setPk_defdoc7(tempVOs[j].getBpkdef7());
          bodyVO.setPk_defdoc8(tempVOs[j].getBpkdef8());
          bodyVO.setPk_defdoc9(tempVOs[j].getBpkdef9());
          bodyVO.setPk_defdoc10(tempVOs[j].getBpkdef10());
          bodyVO.setPk_defdoc11(tempVOs[j].getBpkdef11());
          bodyVO.setPk_defdoc12(tempVOs[j].getBpkdef12());
          bodyVO.setPk_defdoc13(tempVOs[j].getBpkdef13());
          bodyVO.setPk_defdoc14(tempVOs[j].getBpkdef14());
          bodyVO.setPk_defdoc15(tempVOs[j].getBpkdef15());
          bodyVO.setPk_defdoc16(tempVOs[j].getBpkdef16());
          bodyVO.setPk_defdoc17(tempVOs[j].getBpkdef17());
          bodyVO.setPk_defdoc18(tempVOs[j].getBpkdef18());
          bodyVO.setPk_defdoc19(tempVOs[j].getBpkdef19());
          bodyVO.setPk_defdoc20(tempVOs[j].getBpkdef20());
          bodyVO.setVnote(tempVOs[j].getVmemoBody());

          // 生成入库单子子表
          GeneralBb3VO bb3VO = new GeneralBb3VO();
          bb3VO.setNaccountnum1(new UFDouble(0.0));
          bb3VO.setNaccountmny(new UFDouble(0.0));
          bb3VO.setNpprice(tempVOs[j].getNprice());
          bb3VO.setNpmoney(tempVOs[j].getNmoney());

          b = true;
          v.addElement(bodyVO);
          v0.addElement(bb3VO);
        }
      }

      // 生成入库单
      if (v.size() > 0) {
        GeneralHItemVO bodyVOs[] = new GeneralHItemVO[v.size()];
        v.copyInto(bodyVOs);

        GeneralBb3VO bb3VOs[] = new GeneralBb3VO[v0.size()];
        v0.copyInto(bb3VOs);

        GeneralHVO vo = new GeneralHVO(v.size());
        vo.setParentVO(headVO);
        vo.setChildrenVO(bodyVOs);
        vo.setGrandVO(bb3VOs);

        vv.addElement(vo);
      }

    }

    // 返回
    if (vv.size() > 0) {
      GeneralHVO returnVOs[] = new GeneralHVO[vv.size()];
      vv.copyInto(returnVOs);
      return returnVOs;
    }

    return null;
  }

  /**
   * 此处插入方法说明。 功能描述:根据订单，按订单号生成入库单 注意：库存组织、业务员、部门、业务类型、采购公司是系统缺省的分单条件。 输入参数:
   * 返回值:入库单VO 异常处理:
   */
  private GeneralHVO[] doGenerationByOrder(OorderVO tempVOs[], Vector vMixture) {
    if (tempVOs == null || tempVOs.length == 0)
      return null;
    // 优化效率，提供批次查询收发方式
    String sRSMode[] = new String[tempVOs.length];
    for (int i = 0; i < tempVOs.length; i++)
      sRSMode[i] = tempVOs[i].getCbiztype();
    try {
      sRSMode = EstimateHelper.getRSModeBatch(sRSMode);
    }
    catch (Exception e) {
      SCMEnv.out(e);
      MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes.getInstance()
          .getStrByID("40040503", "UPP40040503-000059")/* @res "获得收发方式" */, e
          .getMessage());
      return null;
    }
    //
    Vector vv = new Vector();
    String sTemp = null, s1 = null, s2 = null, s3 = null, s4 = null, s5 = null, s6 = null, sID = null, s7 = null;
    boolean b = false;

    for (int i = 0; i < vMixture.size(); i++) {
      GeneralHHeaderVO headVO = new GeneralHHeaderVO();
      Vector v = new Vector();
      Vector v0 = new Vector();

      sTemp = (String) vMixture.elementAt(i);
      b = false;
      for (int j = 0; j < tempVOs.length; j++) {
        s1 = tempVOs[j].getVordercode();
        s2 = tempVOs[j].getCstoreorganization();
        s3 = tempVOs[j].getCemployeeid();
        s4 = tempVOs[j].getCdeptid();
        s5 = tempVOs[j].getCbiztype();
        s6 = tempVOs[j].getPk_corp();
        s7 = tempVOs[j].getPk_arrvcorp();
        if (s1 == null || s1.trim().length() == 0)
          s1 = "null";
        if (s2 == null || s2.trim().length() == 0)
          s2 = "null";
        if (s3 == null || s3.trim().length() == 0)
          s3 = "null";
        if (s4 == null || s4.trim().length() == 0)
          s4 = "null";
        if (s5 == null || s5.trim().length() == 0)
          s5 = "null";
        if (s6 == null || s6.trim().length() == 0)
          s6 = "null";
        if (s7 == null || s7.trim().length() == 0)
          s7 = "null";
        sID = s1 + s2 + s3 + s4 + s5 + s6 + s7;
        if (sTemp.equals(sID)) {
          if (!b) {
            // 生成入库单表头
            headVO.setDbilldate(m_dSystem);
            headVO.setCbiztype(tempVOs[j].getCbiztype());
            headVO.setCproviderid(tempVOs[j].getCvendormangid());
            headVO.setPk_cubasdoc(tempVOs[j].getCvendorbaseid());
            headVO.setCdptid(tempVOs[j].getCdeptid());
            headVO.setCbizid(tempVOs[j].getCemployeeid());
            headVO.setPk_corp(tempVOs[j].getPk_arrvcorp());// 收货公司
            headVO.setPk_purcorp(tempVOs[j].getPk_corp());// 采购公司
            headVO.setCstoreorganization(tempVOs[j].getPk_arrvstoorg());// 收货库存组织
            headVO.setCwarehouseid(tempVOs[j].getCwarehouseid());
            headVO.setCbilltypecode("4T");
            headVO.setCregister(getClientEnvironment().getUser()
                .getPrimaryKey());
            headVO.setCdispatcherid(sRSMode[j]);
            headVO.setCoperatorid(getClientEnvironment().getUser()
                .getPrimaryKey());

//            headVO.setVuserdef1(tempVOs[j].getHvdef1());
//            headVO.setVuserdef2(tempVOs[j].getHvdef2());
//            headVO.setVuserdef3(tempVOs[j].getHvdef3());
//            headVO.setVuserdef4(tempVOs[j].getHvdef4());
//            headVO.setVuserdef5(tempVOs[j].getHvdef5());
//            headVO.setVuserdef6(tempVOs[j].getHvdef6());
//            headVO.setVuserdef7(tempVOs[j].getHvdef7());
//            headVO.setVuserdef8(tempVOs[j].getHvdef8());
//            headVO.setVuserdef9(tempVOs[j].getHvdef9());
//            headVO.setVuserdef10(tempVOs[j].getHvdef10());
//            headVO.setVuserdef11(tempVOs[j].getHvdef11());
//            headVO.setVuserdef12(tempVOs[j].getHvdef12());
//            headVO.setVuserdef13(tempVOs[j].getHvdef13());
//            headVO.setVuserdef14(tempVOs[j].getHvdef14());
//            headVO.setVuserdef15(tempVOs[j].getHvdef15());
//            headVO.setVuserdef16(tempVOs[j].getHvdef16());
//            headVO.setVuserdef17(tempVOs[j].getHvdef17());
//            headVO.setVuserdef18(tempVOs[j].getHvdef18());
//            headVO.setVuserdef19(tempVOs[j].getHvdef19());
//            headVO.setVuserdef20(tempVOs[j].getHvdef20());
//
//            headVO.setPk_defdoc1(tempVOs[j].getHpkdef1());
//            headVO.setPk_defdoc2(tempVOs[j].getHpkdef2());
//            headVO.setPk_defdoc3(tempVOs[j].getHpkdef3());
//            headVO.setPk_defdoc4(tempVOs[j].getHpkdef4());
//            headVO.setPk_defdoc5(tempVOs[j].getHpkdef5());
//            headVO.setPk_defdoc6(tempVOs[j].getHpkdef6());
//            headVO.setPk_defdoc7(tempVOs[j].getHpkdef7());
//            headVO.setPk_defdoc8(tempVOs[j].getHpkdef8());
//            headVO.setPk_defdoc9(tempVOs[j].getHpkdef9());
//            headVO.setPk_defdoc10(tempVOs[j].getHpkdef10());
//            headVO.setPk_defdoc11(tempVOs[j].getHpkdef11());
//            headVO.setPk_defdoc12(tempVOs[j].getHpkdef12());
//            headVO.setPk_defdoc13(tempVOs[j].getHpkdef13());
//            headVO.setPk_defdoc14(tempVOs[j].getHpkdef14());
//            headVO.setPk_defdoc15(tempVOs[j].getHpkdef15());
//            headVO.setPk_defdoc16(tempVOs[j].getHpkdef16());
//            headVO.setPk_defdoc17(tempVOs[j].getHpkdef17());
//            headVO.setPk_defdoc18(tempVOs[j].getHpkdef18());
//            headVO.setPk_defdoc19(tempVOs[j].getHpkdef19());
//            headVO.setPk_defdoc20(tempVOs[j].getHpkdef20());
            headVO.setVnote(tempVOs[j].getVmemoHead());
          }

          // 生成入库单表体
          GeneralHItemVO bodyVO = new GeneralHItemVO();
          // since v502
          bodyVO.setBLargess(tempVOs[j].getBLargess());
          //
          bodyVO.setCsourcebillhid(tempVOs[j].getCorderid());
          bodyVO.setCsourcebillbid(tempVOs[j].getCorder_bid());
          bodyVO.setVsourcebillcode(tempVOs[j].getVordercode());
          bodyVO.setCsourcetype(ScmConst.PO_Order);
          bodyVO.setBzgflag(new UFBoolean(true));
          bodyVO.setIsok(new UFBoolean(false));
          bodyVO.setCinventoryid(tempVOs[j].getCmangid());
          bodyVO.setNinnum(tempVOs[j].getNgaugenum());
          bodyVO.setNprice(tempVOs[j].getNprice());
          bodyVO.setNmny(tempVOs[j].getNmoney());

          bodyVO.setVfree1(tempVOs[j].getVfree1());
          bodyVO.setVfree2(tempVOs[j].getVfree2());
          bodyVO.setVfree3(tempVOs[j].getVfree3());
          bodyVO.setVfree4(tempVOs[j].getVfree4());
          bodyVO.setVfree5(tempVOs[j].getVfree5());
          bodyVO.setCprojectid(tempVOs[j].getCprojectid());
          bodyVO.setCprojectphaseid(tempVOs[j].getCprojectphaseid());

          bodyVO.setCfirstbillhid(tempVOs[j].getCorderid());
          bodyVO.setCfirstbillbid(tempVOs[j].getCorder_bid());
          bodyVO.setCfirsttype(ScmConst.PO_Order);
          // 批次号
          bodyVO.setVbatchcode(tempVOs[j].getVbatchcode());
          bodyVO.setFlargess(new UFBoolean(false));
          // 暂估日期
          // bodyVO.setDzgdate(m_dSystem);
          // bodyVO.setDbizdate(m_dSystem);

          bodyVO.setDzgdate(headVO.getDbilldate());
          bodyVO.setDbizdate(headVO.getDbilldate());

          bodyVO.setPk_reqcorp(tempVOs[j].getPk_reqcorp());// 需求公司
          bodyVO.setPk_invoicecorp(tempVOs[j].getPk_invoicecorp());// 收票公司
          bodyVO.setPk_reqstoorg(tempVOs[j].getPk_reqstoorg());// 需求库存组织
          bodyVO.setPk_creqwareid(tempVOs[j].getPk_creqwareid());// 需求仓库
          bodyVO.setPk_corp(headVO.getPk_corp());

          bodyVO.setCbaseid(tempVOs[j].getCbaseid());
          bodyVO.setCvendorid(tempVOs[j].getCvendormangid());

          // 到货计划行ID
          bodyVO.setCorder_bb1id(tempVOs[j].getCorder_bb1id());
//          bodyVO.setVuserdef1(tempVOs[j].getBvdef1());
//          bodyVO.setVuserdef2(tempVOs[j].getBvdef2());
//          bodyVO.setVuserdef3(tempVOs[j].getBvdef3());
//          bodyVO.setVuserdef4(tempVOs[j].getBvdef4());
//          bodyVO.setVuserdef5(tempVOs[j].getBvdef5());
//          bodyVO.setVuserdef6(tempVOs[j].getBvdef6());
//          bodyVO.setVuserdef7(tempVOs[j].getBvdef7());
//          bodyVO.setVuserdef8(tempVOs[j].getBvdef8());
//          bodyVO.setVuserdef9(tempVOs[j].getBvdef9());
//          bodyVO.setVuserdef10(tempVOs[j].getBvdef10());
//          bodyVO.setVuserdef11(tempVOs[j].getBvdef11());
//          bodyVO.setVuserdef12(tempVOs[j].getBvdef12());
//          bodyVO.setVuserdef13(tempVOs[j].getBvdef13());
//          bodyVO.setVuserdef14(tempVOs[j].getBvdef14());
//          bodyVO.setVuserdef15(tempVOs[j].getBvdef15());
//          bodyVO.setVuserdef16(tempVOs[j].getBvdef16());
//          bodyVO.setVuserdef17(tempVOs[j].getBvdef17());
//          bodyVO.setVuserdef18(tempVOs[j].getBvdef18());
//          bodyVO.setVuserdef19(tempVOs[j].getBvdef19());
//          bodyVO.setVuserdef20(tempVOs[j].getBvdef20());
//
//          bodyVO.setPk_defdoc1(tempVOs[j].getBpkdef1());
//          bodyVO.setPk_defdoc2(tempVOs[j].getBpkdef2());
//          bodyVO.setPk_defdoc3(tempVOs[j].getBpkdef3());
//          bodyVO.setPk_defdoc4(tempVOs[j].getBpkdef4());
//          bodyVO.setPk_defdoc5(tempVOs[j].getBpkdef5());
//          bodyVO.setPk_defdoc6(tempVOs[j].getBpkdef6());
//          bodyVO.setPk_defdoc7(tempVOs[j].getBpkdef7());
//          bodyVO.setPk_defdoc8(tempVOs[j].getBpkdef8());
//          bodyVO.setPk_defdoc9(tempVOs[j].getBpkdef9());
//          bodyVO.setPk_defdoc10(tempVOs[j].getBpkdef10());
//          bodyVO.setPk_defdoc11(tempVOs[j].getBpkdef11());
//          bodyVO.setPk_defdoc12(tempVOs[j].getBpkdef12());
//          bodyVO.setPk_defdoc13(tempVOs[j].getBpkdef13());
//          bodyVO.setPk_defdoc14(tempVOs[j].getBpkdef14());
//          bodyVO.setPk_defdoc15(tempVOs[j].getBpkdef15());
//          bodyVO.setPk_defdoc16(tempVOs[j].getBpkdef16());
//          bodyVO.setPk_defdoc17(tempVOs[j].getBpkdef17());
//          bodyVO.setPk_defdoc18(tempVOs[j].getBpkdef18());
//          bodyVO.setPk_defdoc19(tempVOs[j].getBpkdef19());
//          bodyVO.setPk_defdoc20(tempVOs[j].getBpkdef20());
          bodyVO.setVnote(tempVOs[j].getVmemoBody());

          // 生成入库单子子表
          GeneralBb3VO bb3VO = new GeneralBb3VO();
          bb3VO.setNaccountnum1(new UFDouble(0.0));
          bb3VO.setNaccountmny(new UFDouble(0.0));
          bb3VO.setNpprice(tempVOs[j].getNprice());
          bb3VO.setNpmoney(tempVOs[j].getNmoney());

          b = true;
          v.addElement(bodyVO);
          v0.addElement(bb3VO);
        }
      }

      // 生成入库单
      if (v.size() > 0) {
        GeneralHItemVO bodyVOs[] = new GeneralHItemVO[v.size()];
        v.copyInto(bodyVOs);

        GeneralBb3VO bb3VOs[] = new GeneralBb3VO[v0.size()];
        v0.copyInto(bb3VOs);

        GeneralHVO vo = new GeneralHVO(v.size());
        vo.setParentVO(headVO);
        vo.setChildrenVO(bodyVOs);
        vo.setGrandVO(bb3VOs);

        vv.addElement(vo);
      }

    }

    // 返回
    if (vv.size() > 0) {
      GeneralHVO returnVOs[] = new GeneralHVO[vv.size()];
      vv.copyInto(returnVOs);
      return returnVOs;
    }

    return null;
  }

  /**
   * 此处插入方法说明。 功能描述:根据订单，按供应商编码生成入库单 注意：库存组织、业务员、部门、业务类型、采购公司是系统缺省的分单条件。 输入参数:
   * 返回值:入库单VO 异常处理:
   */
  /**
   * 根据订单，按供应商编码生成入库单
   * <p>
   * 注意：库存组织、业务员、部门、业务类型、采购公司是系统缺省的分单条件 <b>examples:</b>
   * <p>
   * 使用示例
   * <p>
   * <b>参数说明</b>
   * 
   * @param tempVOs
   * @param vMixture
   * @return
   *          <p>
   * @author xhq
   * @modify 2007-8-22 上午11:01:25, by czp, 增加默认分单条件，收货公司
   */
  private GeneralHVO[] doGenerationByVendor(OorderVO tempVOs[], Vector vMixture) {
    if (tempVOs == null || tempVOs.length == 0)
      return null;
    // 优化效率，提供批次查询收发方式
    String sRSMode[] = new String[tempVOs.length];
    for (int i = 0; i < tempVOs.length; i++)
      sRSMode[i] = tempVOs[i].getCbiztype();
    try {
      sRSMode = EstimateHelper.getRSModeBatch(sRSMode);
    }
    catch (Exception e) {
      SCMEnv.out(e);
      MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes.getInstance()
          .getStrByID("40040503", "UPP40040503-000059")/* @res "获得收发方式" */, e
          .getMessage());
      return null;
    }
    //
    Vector vv = new Vector();
    String sTemp = null, s1 = null, s2 = null, s3 = null, s4 = null, s5 = null, s6 = null, sID = null, s7 = null;
    boolean b = false;

    for (int i = 0; i < vMixture.size(); i++) {
      GeneralHHeaderVO headVO = new GeneralHHeaderVO();
      Vector v = new Vector();
      Vector v0 = new Vector();

      sTemp = (String) vMixture.elementAt(i);
      b = false;
      for (int j = 0; j < tempVOs.length; j++) {
        s1 = tempVOs[j].getCvendormangid();
        s2 = tempVOs[j].getCstoreorganization();
        s3 = tempVOs[j].getCemployeeid();
        s4 = tempVOs[j].getCdeptid();
        s5 = tempVOs[j].getCbiztype();
        s6 = tempVOs[j].getPk_corp();
        s7 = tempVOs[j].getPk_arrvcorp();
        if (s1 == null || s1.trim().length() == 0)
          s1 = "null";
        if (s2 == null || s2.trim().length() == 0)
          s2 = "null";
        if (s3 == null || s3.trim().length() == 0)
          s3 = "null";
        if (s4 == null || s4.trim().length() == 0)
          s4 = "null";
        if (s5 == null || s5.trim().length() == 0)
          s5 = "null";
        if (s6 == null || s6.trim().length() == 0)
          s6 = "null";
        if (s7 == null || s7.trim().length() == 0)
          s7 = "null";
        sID = s1 + s2 + s3 + s4 + s5 + s6 + s7;
        if (sTemp.equals(sID)) {
          if (!b) {
            // 生成入库单表头
            headVO.setDbilldate(m_dSystem);
            headVO.setCbiztype(tempVOs[j].getCbiztype());
            headVO.setCproviderid(tempVOs[j].getCvendormangid());
            headVO.setPk_cubasdoc(tempVOs[j].getCvendorbaseid());
            headVO.setCdptid(tempVOs[j].getCdeptid());
            headVO.setCbizid(tempVOs[j].getCemployeeid());
            headVO.setPk_corp(tempVOs[j].getPk_arrvcorp());// 收货公司
            headVO.setPk_purcorp(tempVOs[j].getPk_corp());// 采购公司
            headVO.setCstoreorganization(tempVOs[j].getPk_arrvstoorg());// 收货库存组织
            headVO.setCwarehouseid(tempVOs[j].getCwarehouseid());
            headVO.setCbilltypecode("4T");
            headVO.setCregister(getClientEnvironment().getUser()
                .getPrimaryKey());
            headVO.setCdispatcherid(sRSMode[j]);
            headVO.setCoperatorid(getClientEnvironment().getUser()
                .getPrimaryKey());

//            headVO.setVuserdef1(tempVOs[j].getHvdef1());
//            headVO.setVuserdef2(tempVOs[j].getHvdef2());
//            headVO.setVuserdef3(tempVOs[j].getHvdef3());
//            headVO.setVuserdef4(tempVOs[j].getHvdef4());
//            headVO.setVuserdef5(tempVOs[j].getHvdef5());
//            headVO.setVuserdef6(tempVOs[j].getHvdef6());
//            headVO.setVuserdef7(tempVOs[j].getHvdef7());
//            headVO.setVuserdef8(tempVOs[j].getHvdef8());
//            headVO.setVuserdef9(tempVOs[j].getHvdef9());
//            headVO.setVuserdef10(tempVOs[j].getHvdef10());
//            headVO.setVuserdef11(tempVOs[j].getHvdef11());
//            headVO.setVuserdef12(tempVOs[j].getHvdef12());
//            headVO.setVuserdef13(tempVOs[j].getHvdef13());
//            headVO.setVuserdef14(tempVOs[j].getHvdef14());
//            headVO.setVuserdef15(tempVOs[j].getHvdef15());
//            headVO.setVuserdef16(tempVOs[j].getHvdef16());
//            headVO.setVuserdef17(tempVOs[j].getHvdef17());
//            headVO.setVuserdef18(tempVOs[j].getHvdef18());
//            headVO.setVuserdef19(tempVOs[j].getHvdef19());
//            headVO.setVuserdef20(tempVOs[j].getHvdef20());
//
//            headVO.setPk_defdoc1(tempVOs[j].getHpkdef1());
//            headVO.setPk_defdoc2(tempVOs[j].getHpkdef2());
//            headVO.setPk_defdoc3(tempVOs[j].getHpkdef3());
//            headVO.setPk_defdoc4(tempVOs[j].getHpkdef4());
//            headVO.setPk_defdoc5(tempVOs[j].getHpkdef5());
//            headVO.setPk_defdoc6(tempVOs[j].getHpkdef6());
//            headVO.setPk_defdoc7(tempVOs[j].getHpkdef7());
//            headVO.setPk_defdoc8(tempVOs[j].getHpkdef8());
//            headVO.setPk_defdoc9(tempVOs[j].getHpkdef9());
//            headVO.setPk_defdoc10(tempVOs[j].getHpkdef10());
//            headVO.setPk_defdoc11(tempVOs[j].getHpkdef11());
//            headVO.setPk_defdoc12(tempVOs[j].getHpkdef12());
//            headVO.setPk_defdoc13(tempVOs[j].getHpkdef13());
//            headVO.setPk_defdoc14(tempVOs[j].getHpkdef14());
//            headVO.setPk_defdoc15(tempVOs[j].getHpkdef15());
//            headVO.setPk_defdoc16(tempVOs[j].getHpkdef16());
//            headVO.setPk_defdoc17(tempVOs[j].getHpkdef17());
//            headVO.setPk_defdoc18(tempVOs[j].getHpkdef18());
//            headVO.setPk_defdoc19(tempVOs[j].getHpkdef19());
//            headVO.setPk_defdoc20(tempVOs[j].getHpkdef20());
            headVO.setVnote(tempVOs[j].getVmemoHead());
          }

          // 生成入库单表体
          GeneralHItemVO bodyVO = new GeneralHItemVO();
          // since v502
          bodyVO.setBLargess(tempVOs[j].getBLargess());
          //
          bodyVO.setCsourcebillhid(tempVOs[j].getCorderid());
          bodyVO.setCsourcebillbid(tempVOs[j].getCorder_bid());
          bodyVO.setVsourcebillcode(tempVOs[j].getVordercode());
          bodyVO.setCsourcetype(ScmConst.PO_Order);
          bodyVO.setBzgflag(new UFBoolean(true));
          bodyVO.setIsok(new UFBoolean(false));
          bodyVO.setCinventoryid(tempVOs[j].getCmangid());
          bodyVO.setNinnum(tempVOs[j].getNgaugenum());
          bodyVO.setNprice(tempVOs[j].getNprice());
          bodyVO.setNmny(tempVOs[j].getNmoney());

          bodyVO.setVfree1(tempVOs[j].getVfree1());
          bodyVO.setVfree2(tempVOs[j].getVfree2());
          bodyVO.setVfree3(tempVOs[j].getVfree3());
          bodyVO.setVfree4(tempVOs[j].getVfree4());
          bodyVO.setVfree5(tempVOs[j].getVfree5());
          bodyVO.setCprojectid(tempVOs[j].getCprojectid());
          bodyVO.setCprojectphaseid(tempVOs[j].getCprojectphaseid());

          bodyVO.setCfirstbillhid(tempVOs[j].getCorderid());
          bodyVO.setCfirstbillbid(tempVOs[j].getCorder_bid());
          bodyVO.setCfirsttype(ScmConst.PO_Order);
          // 批次号
          bodyVO.setVbatchcode(tempVOs[j].getVbatchcode());
          bodyVO.setFlargess(new UFBoolean(false));
          // 暂估日期
          // bodyVO.setDzgdate(m_dSystem);
          // bodyVO.setDbizdate(m_dSystem);

          bodyVO.setDzgdate(headVO.getDbilldate());
          bodyVO.setDbizdate(headVO.getDbilldate());

          bodyVO.setPk_reqcorp(tempVOs[j].getPk_reqcorp());// 需求公司
          bodyVO.setPk_invoicecorp(tempVOs[j].getPk_invoicecorp());// 收票公司
          bodyVO.setPk_reqstoorg(tempVOs[j].getPk_reqstoorg());// 需求库存组织
          bodyVO.setPk_creqwareid(tempVOs[j].getPk_creqwareid());// 需求仓库
          bodyVO.setPk_corp(headVO.getPk_corp());

          bodyVO.setCbaseid(tempVOs[j].getCbaseid());
          bodyVO.setCvendorid(tempVOs[j].getCvendormangid());
          
          bodyVO.setPk_cubasdoc(tempVOs[j].getCvendorbaseid());  //wanglei 2014-05-28

          // 到货计划行ID
          bodyVO.setCorder_bb1id(tempVOs[j].getCorder_bb1id());
//          bodyVO.setVuserdef1(tempVOs[j].getBvdef1());
//          bodyVO.setVuserdef2(tempVOs[j].getBvdef2());
//          bodyVO.setVuserdef3(tempVOs[j].getBvdef3());
//          bodyVO.setVuserdef4(tempVOs[j].getBvdef4());
//          bodyVO.setVuserdef5(tempVOs[j].getBvdef5());
//          bodyVO.setVuserdef6(tempVOs[j].getBvdef6());
//          bodyVO.setVuserdef7(tempVOs[j].getBvdef7());
//          bodyVO.setVuserdef8(tempVOs[j].getBvdef8());
//          bodyVO.setVuserdef9(tempVOs[j].getBvdef9());
//          bodyVO.setVuserdef10(tempVOs[j].getBvdef10());
//          bodyVO.setVuserdef11(tempVOs[j].getBvdef11());
//          bodyVO.setVuserdef12(tempVOs[j].getBvdef12());
//          bodyVO.setVuserdef13(tempVOs[j].getBvdef13());
//          bodyVO.setVuserdef14(tempVOs[j].getBvdef14());
//          bodyVO.setVuserdef15(tempVOs[j].getBvdef15());
//          bodyVO.setVuserdef16(tempVOs[j].getBvdef16());
//          bodyVO.setVuserdef17(tempVOs[j].getBvdef17());
//          bodyVO.setVuserdef18(tempVOs[j].getBvdef18());
//          bodyVO.setVuserdef19(tempVOs[j].getBvdef19());
//          bodyVO.setVuserdef20(tempVOs[j].getBvdef20());
//
//          bodyVO.setPk_defdoc1(tempVOs[j].getBpkdef1());
//          bodyVO.setPk_defdoc2(tempVOs[j].getBpkdef2());
//          bodyVO.setPk_defdoc3(tempVOs[j].getBpkdef3());
//          bodyVO.setPk_defdoc4(tempVOs[j].getBpkdef4());
//          bodyVO.setPk_defdoc5(tempVOs[j].getBpkdef5());
//          bodyVO.setPk_defdoc6(tempVOs[j].getBpkdef6());
//          bodyVO.setPk_defdoc7(tempVOs[j].getBpkdef7());
//          bodyVO.setPk_defdoc8(tempVOs[j].getBpkdef8());
//          bodyVO.setPk_defdoc9(tempVOs[j].getBpkdef9());
//          bodyVO.setPk_defdoc10(tempVOs[j].getBpkdef10());
//          bodyVO.setPk_defdoc11(tempVOs[j].getBpkdef11());
//          bodyVO.setPk_defdoc12(tempVOs[j].getBpkdef12());
//          bodyVO.setPk_defdoc13(tempVOs[j].getBpkdef13());
//          bodyVO.setPk_defdoc14(tempVOs[j].getBpkdef14());
//          bodyVO.setPk_defdoc15(tempVOs[j].getBpkdef15());
//          bodyVO.setPk_defdoc16(tempVOs[j].getBpkdef16());
//          bodyVO.setPk_defdoc17(tempVOs[j].getBpkdef17());
//          bodyVO.setPk_defdoc18(tempVOs[j].getBpkdef18());
//          bodyVO.setPk_defdoc19(tempVOs[j].getBpkdef19());
//          bodyVO.setPk_defdoc20(tempVOs[j].getBpkdef20());
          bodyVO.setVnote(tempVOs[j].getVmemoBody());

          // 生成入库单子子表
          GeneralBb3VO bb3VO = new GeneralBb3VO();
          bb3VO.setNaccountnum1(new UFDouble(0.0));
          bb3VO.setNaccountmny(new UFDouble(0.0));
          bb3VO.setNpprice(tempVOs[j].getNprice());
          bb3VO.setNpmoney(tempVOs[j].getNmoney());

          b = true;
          v.addElement(bodyVO);
          v0.addElement(bb3VO);
        }
      }

      // 生成入库单
      if (v.size() > 0) {
        GeneralHItemVO bodyVOs[] = new GeneralHItemVO[v.size()];
        v.copyInto(bodyVOs);

        GeneralBb3VO bb3VOs[] = new GeneralBb3VO[v0.size()];
        v0.copyInto(bb3VOs);

        GeneralHVO vo = new GeneralHVO(v.size());
        vo.setParentVO(headVO);
        vo.setChildrenVO(bodyVOs);
        vo.setGrandVO(bb3VOs);

        vv.addElement(vo);
      }

    }

    // 返回
    if (vv.size() > 0) {
      GeneralHVO returnVOs[] = new GeneralHVO[vv.size()];
      vv.copyInto(returnVOs);
      return returnVOs;
    }

    return null;
  }

  /**
   * 此处插入方法说明。 功能描述:根据订单，按仓库ID+供应商ID生成入库单 注意：库存组织、业务员、部门、业务类型、采购公司是系统缺省的分单条件。
   * 输入参数: 返回值:入库单VO 异常处理:
   */
  private GeneralHVO[] doGenerationByWarehouse(OorderVO tempVOs[],
      Vector vMixture) {
    if (tempVOs == null || tempVOs.length == 0)
      return null;
    // 优化效率，提供批次查询收发方式
    String sRSMode[] = new String[tempVOs.length];
    for (int i = 0; i < tempVOs.length; i++)
      sRSMode[i] = tempVOs[i].getCbiztype();
    try {
      sRSMode = EstimateHelper.getRSModeBatch(sRSMode);
    }
    catch (Exception e) {
      SCMEnv.out(e);
      MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes.getInstance()
          .getStrByID("40040503", "UPP40040503-000059")/* @res "获得收发方式" */, e
          .getMessage());
      return null;
    }
    //
    Vector vv = new Vector();
    String sTemp = null, s1 = null, s2 = null, s3 = null, s4 = null, s5 = null, s6 = null, s7 = null, sID = null, s8;
    boolean b = false;

    for (int i = 0; i < vMixture.size(); i++) {
      GeneralHHeaderVO headVO = new GeneralHHeaderVO();
      Vector v = new Vector();
      Vector v0 = new Vector();

      sTemp = (String) vMixture.elementAt(i);
      b = false;
      for (int j = 0; j < tempVOs.length; j++) {
        s1 = tempVOs[j].getCwarehouseid();
        s2 = tempVOs[j].getCvendormangid();
        s3 = tempVOs[j].getCstoreorganization();
        s4 = tempVOs[j].getCemployeeid();
        s5 = tempVOs[j].getCdeptid();
        s6 = tempVOs[j].getCbiztype();
        s7 = tempVOs[j].getPk_corp();
        s8 = tempVOs[j].getPk_arrvcorp();
        if (s1 == null || s1.trim().length() == 0)
          s1 = "null";
        if (s2 == null || s2.trim().length() == 0)
          s2 = "null";
        if (s3 == null || s3.trim().length() == 0)
          s3 = "null";
        if (s4 == null || s4.trim().length() == 0)
          s4 = "null";
        if (s5 == null || s5.trim().length() == 0)
          s5 = "null";
        if (s6 == null || s6.trim().length() == 0)
          s6 = "null";
        if (s7 == null || s7.trim().length() == 0)
          s7 = "null";
        if (s8 == null || s8.trim().length() == 0)
          s8 = "null";
        sID = s1 + s2 + s3 + s4 + s5 + s6 + s7 + s8;
        if (sTemp.equals(sID)) {
          if (!b) {
            // 生成入库单表头
            headVO.setDbilldate(m_dSystem);
            headVO.setCbiztype(tempVOs[j].getCbiztype());
            headVO.setCproviderid(tempVOs[j].getCvendormangid());
            headVO.setPk_cubasdoc(tempVOs[j].getCvendorbaseid());
            headVO.setCdptid(tempVOs[j].getCdeptid());
            headVO.setCbizid(tempVOs[j].getCemployeeid());
            headVO.setPk_corp(tempVOs[j].getPk_arrvcorp());// 收货公司
            headVO.setPk_purcorp(tempVOs[j].getPk_corp());// 采购公司
            headVO.setCstoreorganization(tempVOs[j].getPk_arrvstoorg());// 收货库存组织
            headVO.setCwarehouseid(tempVOs[j].getCwarehouseid());
            headVO.setCbilltypecode("4T");
            headVO.setCregister(getClientEnvironment().getUser()
                .getPrimaryKey());
            headVO.setCdispatcherid(sRSMode[j]);
            headVO.setCoperatorid(getClientEnvironment().getUser()
                .getPrimaryKey());

//            headVO.setVuserdef1(tempVOs[j].getHvdef1());
//            headVO.setVuserdef2(tempVOs[j].getHvdef2());
//            headVO.setVuserdef3(tempVOs[j].getHvdef3());
//            headVO.setVuserdef4(tempVOs[j].getHvdef4());
//            headVO.setVuserdef5(tempVOs[j].getHvdef5());
//            headVO.setVuserdef6(tempVOs[j].getHvdef6());
//            headVO.setVuserdef7(tempVOs[j].getHvdef7());
//            headVO.setVuserdef8(tempVOs[j].getHvdef8());
//            headVO.setVuserdef9(tempVOs[j].getHvdef9());
//            headVO.setVuserdef10(tempVOs[j].getHvdef10());
//            headVO.setVuserdef11(tempVOs[j].getHvdef11());
//            headVO.setVuserdef12(tempVOs[j].getHvdef12());
//            headVO.setVuserdef13(tempVOs[j].getHvdef13());
//            headVO.setVuserdef14(tempVOs[j].getHvdef14());
//            headVO.setVuserdef15(tempVOs[j].getHvdef15());
//            headVO.setVuserdef16(tempVOs[j].getHvdef16());
//            headVO.setVuserdef17(tempVOs[j].getHvdef17());
//            headVO.setVuserdef18(tempVOs[j].getHvdef18());
//            headVO.setVuserdef19(tempVOs[j].getHvdef19());
//            headVO.setVuserdef20(tempVOs[j].getHvdef20());
//
//            headVO.setPk_defdoc1(tempVOs[j].getHpkdef1());
//            headVO.setPk_defdoc2(tempVOs[j].getHpkdef2());
//            headVO.setPk_defdoc3(tempVOs[j].getHpkdef3());
//            headVO.setPk_defdoc4(tempVOs[j].getHpkdef4());
//            headVO.setPk_defdoc5(tempVOs[j].getHpkdef5());
//            headVO.setPk_defdoc6(tempVOs[j].getHpkdef6());
//            headVO.setPk_defdoc7(tempVOs[j].getHpkdef7());
//            headVO.setPk_defdoc8(tempVOs[j].getHpkdef8());
//            headVO.setPk_defdoc9(tempVOs[j].getHpkdef9());
//            headVO.setPk_defdoc10(tempVOs[j].getHpkdef10());
//            headVO.setPk_defdoc11(tempVOs[j].getHpkdef11());
//            headVO.setPk_defdoc12(tempVOs[j].getHpkdef12());
//            headVO.setPk_defdoc13(tempVOs[j].getHpkdef13());
//            headVO.setPk_defdoc14(tempVOs[j].getHpkdef14());
//            headVO.setPk_defdoc15(tempVOs[j].getHpkdef15());
//            headVO.setPk_defdoc16(tempVOs[j].getHpkdef16());
//            headVO.setPk_defdoc17(tempVOs[j].getHpkdef17());
//            headVO.setPk_defdoc18(tempVOs[j].getHpkdef18());
//            headVO.setPk_defdoc19(tempVOs[j].getHpkdef19());
//            headVO.setPk_defdoc20(tempVOs[j].getHpkdef20());
            headVO.setVnote(tempVOs[j].getVmemoHead());
          }

          // 生成入库单表体
          GeneralHItemVO bodyVO = new GeneralHItemVO();
          // since v502
          bodyVO.setBLargess(tempVOs[j].getBLargess());
          //
          bodyVO.setCsourcebillhid(tempVOs[j].getCorderid());
          bodyVO.setCsourcebillbid(tempVOs[j].getCorder_bid());
          bodyVO.setVsourcebillcode(tempVOs[j].getVordercode());
          bodyVO.setCsourcetype(ScmConst.PO_Order);
          bodyVO.setBzgflag(new UFBoolean(true));
          bodyVO.setIsok(new UFBoolean(false));
          bodyVO.setCinventoryid(tempVOs[j].getCmangid());
          bodyVO.setNinnum(tempVOs[j].getNgaugenum());
          bodyVO.setNprice(tempVOs[j].getNprice());
          bodyVO.setNmny(tempVOs[j].getNmoney());

          bodyVO.setVfree1(tempVOs[j].getVfree1());
          bodyVO.setVfree2(tempVOs[j].getVfree2());
          bodyVO.setVfree3(tempVOs[j].getVfree3());
          bodyVO.setVfree4(tempVOs[j].getVfree4());
          bodyVO.setVfree5(tempVOs[j].getVfree5());
          bodyVO.setCprojectid(tempVOs[j].getCprojectid());
          bodyVO.setCprojectphaseid(tempVOs[j].getCprojectphaseid());

          bodyVO.setCfirstbillhid(tempVOs[j].getCorderid());
          bodyVO.setCfirstbillbid(tempVOs[j].getCorder_bid());
          bodyVO.setCfirsttype(ScmConst.PO_Order);
          // 批次号
          bodyVO.setVbatchcode(tempVOs[j].getVbatchcode());
          bodyVO.setFlargess(new UFBoolean(false));
          // 暂估日期
          // bodyVO.setDzgdate(m_dSystem);
          // bodyVO.setDbizdate(m_dSystem);

          bodyVO.setDzgdate(headVO.getDbilldate());
          bodyVO.setDbizdate(headVO.getDbilldate());

          bodyVO.setPk_reqcorp(tempVOs[j].getPk_reqcorp());// 需求公司
          bodyVO.setPk_invoicecorp(tempVOs[j].getPk_invoicecorp());// 收票公司
          bodyVO.setPk_reqstoorg(tempVOs[j].getPk_reqstoorg());// 需求库存组织
          bodyVO.setPk_creqwareid(tempVOs[j].getPk_creqwareid());// 需求仓库
          bodyVO.setPk_corp(headVO.getPk_corp());

          bodyVO.setCbaseid(tempVOs[j].getCbaseid());
          bodyVO.setCvendorid(tempVOs[j].getCvendormangid());

          // 到货计划行ID
          bodyVO.setCorder_bb1id(tempVOs[j].getCorder_bb1id());
//          bodyVO.setVuserdef1(tempVOs[j].getBvdef1());
//          bodyVO.setVuserdef2(tempVOs[j].getBvdef2());
//          bodyVO.setVuserdef3(tempVOs[j].getBvdef3());
//          bodyVO.setVuserdef4(tempVOs[j].getBvdef4());
//          bodyVO.setVuserdef5(tempVOs[j].getBvdef5());
//          bodyVO.setVuserdef6(tempVOs[j].getBvdef6());
//          bodyVO.setVuserdef7(tempVOs[j].getBvdef7());
//          bodyVO.setVuserdef8(tempVOs[j].getBvdef8());
//          bodyVO.setVuserdef9(tempVOs[j].getBvdef9());
//          bodyVO.setVuserdef10(tempVOs[j].getBvdef10());
//          bodyVO.setVuserdef11(tempVOs[j].getBvdef11());
//          bodyVO.setVuserdef12(tempVOs[j].getBvdef12());
//          bodyVO.setVuserdef13(tempVOs[j].getBvdef13());
//          bodyVO.setVuserdef14(tempVOs[j].getBvdef14());
//          bodyVO.setVuserdef15(tempVOs[j].getBvdef15());
//          bodyVO.setVuserdef16(tempVOs[j].getBvdef16());
//          bodyVO.setVuserdef17(tempVOs[j].getBvdef17());
//          bodyVO.setVuserdef18(tempVOs[j].getBvdef18());
//          bodyVO.setVuserdef19(tempVOs[j].getBvdef19());
//          bodyVO.setVuserdef20(tempVOs[j].getBvdef20());
//
//          bodyVO.setPk_defdoc1(tempVOs[j].getBpkdef1());
//          bodyVO.setPk_defdoc2(tempVOs[j].getBpkdef2());
//          bodyVO.setPk_defdoc3(tempVOs[j].getBpkdef3());
//          bodyVO.setPk_defdoc4(tempVOs[j].getBpkdef4());
//          bodyVO.setPk_defdoc5(tempVOs[j].getBpkdef5());
//          bodyVO.setPk_defdoc6(tempVOs[j].getBpkdef6());
//          bodyVO.setPk_defdoc7(tempVOs[j].getBpkdef7());
//          bodyVO.setPk_defdoc8(tempVOs[j].getBpkdef8());
//          bodyVO.setPk_defdoc9(tempVOs[j].getBpkdef9());
//          bodyVO.setPk_defdoc10(tempVOs[j].getBpkdef10());
//          bodyVO.setPk_defdoc11(tempVOs[j].getBpkdef11());
//          bodyVO.setPk_defdoc12(tempVOs[j].getBpkdef12());
//          bodyVO.setPk_defdoc13(tempVOs[j].getBpkdef13());
//          bodyVO.setPk_defdoc14(tempVOs[j].getBpkdef14());
//          bodyVO.setPk_defdoc15(tempVOs[j].getBpkdef15());
//          bodyVO.setPk_defdoc16(tempVOs[j].getBpkdef16());
//          bodyVO.setPk_defdoc17(tempVOs[j].getBpkdef17());
//          bodyVO.setPk_defdoc18(tempVOs[j].getBpkdef18());
//          bodyVO.setPk_defdoc19(tempVOs[j].getBpkdef19());
//          bodyVO.setPk_defdoc20(tempVOs[j].getBpkdef20());
          bodyVO.setVnote(tempVOs[j].getVmemoBody());       

          // 生成入库单子子表
          GeneralBb3VO bb3VO = new GeneralBb3VO();
          bb3VO.setNaccountnum1(new UFDouble(0.0));
          bb3VO.setNaccountmny(new UFDouble(0.0));
          bb3VO.setNpprice(tempVOs[j].getNprice());
          bb3VO.setNpmoney(tempVOs[j].getNmoney());

          b = true;
          v.addElement(bodyVO);
          v0.addElement(bb3VO);
        }
      }

      // 生成入库单
      if (v.size() > 0) {
        GeneralHItemVO bodyVOs[] = new GeneralHItemVO[v.size()];
        v.copyInto(bodyVOs);

        GeneralBb3VO bb3VOs[] = new GeneralBb3VO[v0.size()];
        v0.copyInto(bb3VOs);

        GeneralHVO vo = new GeneralHVO(v.size());
        vo.setParentVO(headVO);
        vo.setChildrenVO(bodyVOs);
        vo.setGrandVO(bb3VOs);

        vv.addElement(vo);
      }

    }

    // 返回
    if (vv.size() > 0) {
      GeneralHVO returnVOs[] = new GeneralHVO[vv.size()];
      vv.copyInto(returnVOs);
      return returnVOs;
    }

    return null;
  }

  /**
   * 此处插入方法说明。 功能描述:保存后刷新界面 输入参数: 返回值: 异常处理:
   */
  private void doRefresh(int n) {
    // 显示列表
    getBillCardPanel1().setVisible(false);

    add(getBillListPanel(), java.awt.BorderLayout.CENTER);
    this.setButtons(m_buttons31);
    m_nButtonState = new int[m_buttons3.length];

    // 保存后的暂估入库单不再显示
    Vector v1 = new Vector();
    for (int i = 0; i < m_VOs.length; i++) {
      //if (i != n)
        v1.addElement(m_VOs[i]);
    }
    if (v1.size() == 0) {
      // 所有新生成的暂估入库单已保存完毕
      m_bAdd = false;
      m_bGenerate = false;
      m_VOs = null;
      getBillListPanel().getHeadBillModel().clearBodyData();
      getBillListPanel().getBodyBillModel().clearBodyData();
      getBillListPanel().updateUI();
      //
      setListBtnsStateEditable();
      return;
    }

    // 部分新生成的暂估入库单已保存完毕
    m_VOs = new GeneralHVO[v1.size()];
    v1.copyInto(m_VOs);

    Vector v2 = new Vector();
    for (int i = 0; i < m_VOs.length; i++)
      v2.addElement(m_VOs[i].getHeadVO());
    GeneralHHeaderVO head[] = new GeneralHHeaderVO[v2.size()];
    v2.copyInto(head);

    GeneralHItemVO body[] = m_VOs[0].getBodyVO();
    // 隐藏cbiztype,cproviderid,cdptid,cbizid,cwarehouseid,cwhsmanagerid
    getBillListPanel().hideHeadTableCol("cbiztype");
    getBillListPanel().hideHeadTableCol("cproviderid");
    getBillListPanel().hideHeadTableCol("cdptid");
    getBillListPanel().hideHeadTableCol("cbizid");
    getBillListPanel().hideHeadTableCol("cwarehouseid");
    getBillListPanel().hideHeadTableCol("cwhsmanagerid");

    getBillListPanel().getHeadBillModel().setBodyDataVO(head);
    getBillListPanel().getHeadBillModel().execLoadFormula();
    getBillListPanel().getBodyBillModel().setBodyDataVO(body);
    getBillListPanel().getBodyBillModel().execLoadFormula();
    getBillListPanel().getHeadBillModel().updateValue();
    getBillListPanel().getBodyBillModel().updateValue();
    getBillListPanel().updateUI();

    // 显示备注
    for (int i = 0; i < head.length; i++) {
      getBillListPanel().getHeadBillModel().setValueAt(head[i].getVnote(), i,
          "vnote");
    }
    //
    setListBtnsStateEditable();
  }

  /**
   * 此处插入方法说明。 功能描述:根据订单生成入库单。按选定的分单方式进行。 注意：库存组织、业务员、部门、业务类型、采购公司是系统缺省的分单条件。
   * 输入参数:订单VO 返回值: 异常处理:
   */
  private void generateStock(OorderVO tempVOs[]) {
    try {
      for (int i = 0; i < tempVOs.length; i++)
        tempVOs[i].validate();
    }
    catch (Exception e) {
      SCMEnv.out(e);
      MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes.getInstance()
          .getStrByID("40040503", "UPP40040503-000060")/* @res "订单检查" */, e
          .getMessage());
      return;
    }

    GeneralHVO vo[] = null;

    if (m_nMode == 0) {
      // 按供应商
      // 供应商编码+库存组织+业务员+部门+业务类型+采购公司唯一组合
      Vector vMixture = new Vector();
      String sID = null, s1 = null, s2 = null, s3 = null, s4 = null, s5 = null, s6 = null, s7 = null;
      for (int i = 0; i < tempVOs.length; i++) {
        s1 = tempVOs[i].getCvendormangid();
        s2 = tempVOs[i].getCstoreorganization();
        s3 = tempVOs[i].getCemployeeid();
        s4 = tempVOs[i].getCdeptid();
        s5 = tempVOs[i].getCbiztype();
        s6 = tempVOs[i].getPk_corp();
        s7 = tempVOs[i].getPk_arrvcorp();
        if (s1 == null || s1.trim().length() == 0)
          s1 = "null";
        if (s2 == null || s2.trim().length() == 0)
          s2 = "null";
        if (s3 == null || s3.trim().length() == 0)
          s3 = "null";
        if (s4 == null || s4.trim().length() == 0)
          s4 = "null";
        if (s5 == null || s5.trim().length() == 0)
          s5 = "null";
        if (s6 == null || s6.trim().length() == 0)
          s6 = "null";
        if (s7 == null || s7.trim().length() == 0)
          s7 = "null";
        sID = s1 + s2 + s3 + s4 + s5 + s6 + s7;
        if (!vMixture.contains(sID))
          vMixture.addElement(sID);
      }
      vo = doGenerationByVendor(tempVOs, vMixture);

    }
    else if (m_nMode == 1) {
      // 按存货+供应商
      // 存货编码+供应商ID+库存组织+业务员+部门+业务类型+采购公司唯一组合
      Vector vMixture = new Vector();
      String sID = null, s1 = null, s2 = null, s3 = null, s4 = null, s5 = null, s6 = null, s7 = null, s8 = null;
      for (int i = 0; i < tempVOs.length; i++) {
        s1 = tempVOs[i].getCmangid();
        s2 = tempVOs[i].getCvendormangid();
        s3 = tempVOs[i].getCstoreorganization();
        s4 = tempVOs[i].getCemployeeid();
        s5 = tempVOs[i].getCdeptid();
        s6 = tempVOs[i].getCbiztype();
        s7 = tempVOs[i].getPk_corp();
        s8 = tempVOs[i].getPk_arrvcorp();
        if (s1 == null || s1.trim().length() == 0)
          s1 = "null";
        if (s2 == null || s2.trim().length() == 0)
          s2 = "null";
        if (s3 == null || s3.trim().length() == 0)
          s3 = "null";
        if (s4 == null || s4.trim().length() == 0)
          s4 = "null";
        if (s5 == null || s5.trim().length() == 0)
          s5 = "null";
        if (s6 == null || s6.trim().length() == 0)
          s6 = "null";
        if (s7 == null || s7.trim().length() == 0)
          s7 = "null";
        if (s8 == null || s8.trim().length() == 0)
          s8 = "null";
        sID = s1 + s2 + s3 + s4 + s5 + s6 + s7 + s8;
        if (!vMixture.contains(sID))
          vMixture.addElement(sID);
      }
      vo = doGenerationByInv(tempVOs, vMixture);

    }
    else if (m_nMode == 2) {
      // 按订单号
      // 订单号+库存组织+业务员+部门+业务类型+采购公司唯一组合
      Vector vMixture = new Vector();
      String sID = null, s1 = null, s2 = null, s3 = null, s4 = null, s5 = null, s6 = null, s7 = null;
      for (int i = 0; i < tempVOs.length; i++) {
        s1 = tempVOs[i].getVordercode();
        s2 = tempVOs[i].getCstoreorganization();
        s3 = tempVOs[i].getCemployeeid();
        s4 = tempVOs[i].getCdeptid();
        s5 = tempVOs[i].getCbiztype();
        s6 = tempVOs[i].getPk_corp();
        s7 = tempVOs[i].getPk_arrvcorp();
        if (s1 == null || s1.trim().length() == 0)
          s1 = "null";
        if (s2 == null || s2.trim().length() == 0)
          s2 = "null";
        if (s3 == null || s3.trim().length() == 0)
          s3 = "null";
        if (s4 == null || s4.trim().length() == 0)
          s4 = "null";
        if (s5 == null || s5.trim().length() == 0)
          s5 = "null";
        if (s6 == null || s6.trim().length() == 0)
          s6 = "null";
        if (s7 == null || s7.trim().length() == 0)
          s7 = "null";
        sID = s1 + s2 + s3 + s4 + s5 + s6 + s7;
        if (!vMixture.contains(sID))
          vMixture.addElement(sID);
      }
      vo = doGenerationByOrder(tempVOs, vMixture);

    }
    else if (m_nMode == 3) {
      // 按仓库+供应商
      // 仓库ID+供应商ID+库存组织+业务员+部门+业务类型+采购公司唯一组合
      Vector vMixture = new Vector();
      String sID = null, s1 = null, s2 = null, s3 = null, s4 = null, s5 = null, s6 = null, s7 = null, s8 = null;
      for (int i = 0; i < tempVOs.length; i++) {
        s1 = tempVOs[i].getCwarehouseid();
        s2 = tempVOs[i].getCvendormangid();
        s3 = tempVOs[i].getCstoreorganization();
        s4 = tempVOs[i].getCemployeeid();
        s5 = tempVOs[i].getCdeptid();
        s6 = tempVOs[i].getCbiztype();
        s7 = tempVOs[i].getPk_corp();
        s8 = tempVOs[i].getPk_arrvcorp();
        if (s1 == null || s1.trim().length() == 0)
          s1 = "null";
        if (s2 == null || s2.trim().length() == 0)
          s2 = "null";
        if (s3 == null || s3.trim().length() == 0)
          s3 = "null";
        if (s4 == null || s4.trim().length() == 0)
          s4 = "null";
        if (s5 == null || s5.trim().length() == 0)
          s5 = "null";
        if (s6 == null || s6.trim().length() == 0)
          s6 = "null";
        if (s7 == null || s7.trim().length() == 0)
          s7 = "null";
        if (s8 == null || s8.trim().length() == 0)
          s8 = "null";
        sID = s1 + s2 + s3 + s4 + s5 + s6 + s7 + s8;
        if (!vMixture.contains(sID))
          vMixture.addElement(sID);
      }
      vo = doGenerationByWarehouse(tempVOs, vMixture);

    }
    else if (m_nMode == 4) {
      // 按订单号+仓库
      // 订单号+仓库ID+库存组织+业务员+部门+业务类型+采购公司唯一组合
      Vector vMixture = new Vector();
      String sID = null, s1 = null, s2 = null, s3 = null, s4 = null, s5 = null, s6 = null, s7 = null, s8 = null;
      for (int i = 0; i < tempVOs.length; i++) {
        s1 = tempVOs[i].getCwarehouseid();
        s2 = tempVOs[i].getVordercode();
        s3 = tempVOs[i].getCstoreorganization();
        s4 = tempVOs[i].getCemployeeid();
        s5 = tempVOs[i].getCdeptid();
        s6 = tempVOs[i].getCbiztype();
        s7 = tempVOs[i].getPk_corp();
        s8 = tempVOs[i].getPk_arrvcorp();
        if (s1 == null || s1.trim().length() == 0)
          s1 = "null";
        if (s2 == null || s2.trim().length() == 0)
          s2 = "null";
        if (s3 == null || s3.trim().length() == 0)
          s3 = "null";
        if (s4 == null || s4.trim().length() == 0)
          s4 = "null";
        if (s5 == null || s5.trim().length() == 0)
          s5 = "null";
        if (s6 == null || s6.trim().length() == 0)
          s6 = "null";
        if (s7 == null || s7.trim().length() == 0)
          s7 = "null";
        if (s8 == null || s8.trim().length() == 0)
          s8 = "null";
        sID = s1 + s2 + s3 + s4 + s5 + s6 + s7 + s8;
        if (!vMixture.contains(sID))
          vMixture.addElement(sID);
      }
      vo = doGenerationByMixture(tempVOs, vMixture);
    }

    // 生成的入库单加入到缓存中
    if (vo == null || vo.length == 0)
      return;

    // 自由项
    try {
      vo = switchVOForDifferentCorp(vo);
      if (vo == null || vo.length == 0)
        return;

    }
    catch (Exception e) {
      SCMEnv.out(e);
      MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes.getInstance()
          .getStrByID("40040503", "UPP40040503-000061")/* @res "获取自由项信息" */, e
          .getMessage());
    }
    //
    try {
      String vfree[][] = EstimateHelper.getFreeItem0(vo);
      if (vfree != null && vfree.length > 0) {
        for (int i = 0; i < vo.length; i++) {
          GeneralHItemVO itemVO[] = vo[i].getBodyVO();
          if (itemVO != null && itemVO.length > 0) {
            for (int j = 0; j < itemVO.length; j++) {
              if (vfree[i] != null) {
                itemVO[j].setVfree(vfree[i][j]);
              }
            }
          }
        }
      }
    }
    catch (Exception e) {
      SCMEnv.out(e);
      MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes.getInstance()
          .getStrByID("40040503", "UPP40040503-000061")/* @res "获取自由项信息" */, e
          .getMessage());
    }
    //

    Vector v = new Vector();
    // if(m_gVOs!=null && m_gVOs.length>0){
    // for(int i=0;i<m_gVOs.length;i++) v.addElement(m_gVOs[i]);
    // }
    for (int i = 0; i < vo.length; i++)
      v.addElement(vo[i]);

    m_gVOs = new GeneralHVO[v.size()];
    v.copyInto(m_gVOs);

    m_bGenerate = true;
  }

  /**
   * 此处插入方法说明。 功能描述:获得要打印的字段主键 输入参数: 返回值: 异常处理:
   */
  public String[] getAllDataItemExpress() {
    BillItem headItems[] = getBillCardPanel1().getHeadShowItems();
    BillItem bodyItems[] = getBillCardPanel1().getBodyShowItems();

    Vector v = new Vector();
    for (int i = 0; i < headItems.length; i++)
      v.addElement(headItems[i].getKey());
    for (int i = 0; i < bodyItems.length; i++)
      v.addElement(bodyItems[i].getKey());

    if (v.size() > 0) {
      String sKey[] = new String[v.size()];
      v.copyInto(sKey);
      return sKey;
    }
    return null;
  }

  /**
   * 此处插入方法说明。 功能描述:获得要打印的字段名称 输入参数: 返回值: 异常处理:
   */
  public String[] getAllDataItemNames() {
    BillItem headItems[] = getBillCardPanel1().getHeadShowItems();
    BillItem bodyItems[] = getBillCardPanel1().getBodyShowItems();

    Vector v = new Vector();
    for (int i = 0; i < headItems.length; i++)
      v.addElement(headItems[i].getName());
    for (int i = 0; i < bodyItems.length; i++)
      v.addElement(bodyItems[i].getName());

    if (v.size() > 0) {
      String sName[] = new String[v.size()];
      v.copyInto(sName);
      return sName;
    }
    return null;
  }

  /**
   * 此处插入方法说明。 功能描述: 输入参数: 返回值: 异常处理:
   */
  public String[] getDependentItemExpressByExpress(String itemName) {
    return null;
  }

  /**
   * 此处插入方法说明。 功能描述:获得要打印的字段的值 输入参数:主键 返回值: 异常处理:
   */
  public String[] getItemValuesByExpress(String itemKey) {
    BillItem headItem[] = getBillCardPanel1().getHeadShowItems();
    BillItem bodyItem[] = getBillCardPanel1().getBodyShowItems();

    itemKey = itemKey.trim();
    int nPos = -1;
    for (int i = 0; i < headItem.length; i++) {
      String s = headItem[i].getKey().trim();
      if (itemKey.equals("h_" + s)) {
        nPos = 0;
        break;
      }
    }

    if (nPos == -1) {
      for (int i = 0; i < bodyItem.length; i++) {
        String s = bodyItem[i].getKey().trim();
        if (itemKey.equals(s)) {
          nPos = 1;
          break;
        }
      }
    }

    // 卡片
    int nRow = getBillCardPanel1().getRowCount();
    if (nPos == 0) {
      // 表头
      Object o = null;
      if (itemKey.equals("h_vbillcode") || itemKey.equals("h_dbilldate")) {
        GeneralHVO VO = new GeneralHVO(nRow);
        getBillCardPanel1().getBillValueVO(VO);
        GeneralHHeaderVO headVO = VO.getHeadVO();
        o = headVO.getAttributeValue(itemKey.substring(2));
      }
      else {
        UIRefPane nRefPanel = (UIRefPane) getBillCardPanel1().getHeadItem(
            itemKey.substring(2)).getComponent();
        o = nRefPanel.getRefName();
      }

      String sValues[] = new String[1];
      if (o != null)
        sValues[0] = o.toString();
      else
        sValues[0] = "";
      return sValues;
    }
    else {
      // 表体
      // 处理合计行
      int nTotal = 0;
      if (getBillCardPanel1().getBodyPanel().isTatolRow())
        nTotal = 1;
      String sValues[] = new String[nRow + nTotal];
      for (int i = 0; i < nRow; i++) {
        Object o = getBillCardPanel1().getBodyValueAt(i, itemKey);
        if (o != null)
          sValues[i] = o.toString();
        else
          sValues[0] = "";
      }
      if (nTotal > 0) {
        Object o = getBillCardPanel1().getTotalTableModel().getValueAt(0,
            getBillCardPanel1().getBillModel().getBodyColByKey(itemKey));
        if (o != null && o.toString().trim().length() > 0)
          sValues[nRow] = o.toString();
        else
          sValues[nRow] = "";
      }

      return sValues;
    }
  }

  /**
   * 功能: 获得期初暂估入库单的增加值{增加值=修改后的值-修改前的值，新增情况“修改前的值=0”} 返回：ArrayList{boolean[]
   * 是否到货计划[]；String[] 订单行(到货计划)ID[]；UFDouble[] 订单行ID对应的入库数量变化量} 作者：熊海情 修改：晁志平
   * FOR V30 增加到货计划处理
   */
  private ArrayList getModifiedOrderStockNum() {
    // 旧VO
    GeneralHItemVO itemVO1[] = m_VOs[m_nPresentRecord].getBodyVO();
    int iLen = itemVO1.length;
    boolean[] isPlanLines = new boolean[iLen];
    String[] saKey = new String[iLen];
    UFDouble[] uaNumAdded = new UFDouble[iLen];
    // 新VO
    int nRow = getBillCardPanel1().getRowCount();
    GeneralHVO vo = new GeneralHVO(nRow);
    getBillCardPanel1().getBillValueVO(vo);
    GeneralHItemVO itemVO2[] = vo.getBodyVO();
    int iLenNew = itemVO2.length;

    if (!m_bAdd) {
      saKey = new String[iLen];
      isPlanLines = new boolean[iLen];
      uaNumAdded = new UFDouble[iLen];
      Hashtable hNewNum = new Hashtable();
      for (int i = 0; i < iLenNew; i++) {
        hNewNum.put(itemVO2[i].getCgeneralbid(), itemVO2[i].getNinnum());
      }
      String strOldKey = null;
      UFDouble uNewNum = null;
      for (int i = 0; i < iLen; i++) {
        isPlanLines[i] = (itemVO1[i].getCorder_bb1id() != null && itemVO1[i]
            .getCorder_bb1id().trim().length() > 0);
        strOldKey = itemVO1[i].getCgeneralbid();
        if (isPlanLines[i]) {
          saKey[i] = itemVO1[i].getCorder_bb1id();
        }
        else {
          saKey[i] = itemVO1[i].getCsourcebillbid();
        }
        uNewNum = (UFDouble) hNewNum.get(strOldKey);
        if (!hNewNum.containsKey(strOldKey) || uNewNum == null) {
          uaNumAdded[i] = itemVO1[i].getNinnum().multiply(-1.0);
          continue;
        }
        uaNumAdded[i] = uNewNum.sub(itemVO1[i].getNinnum());
      }
    }
    else {
      saKey = new String[iLenNew];
      isPlanLines = new boolean[iLenNew];
      uaNumAdded = new UFDouble[iLenNew];
      for (int i = 0; i < iLenNew; i++) {
        isPlanLines[i] = (itemVO2[i].getCorder_bb1id() != null && itemVO2[i]
            .getCorder_bb1id().trim().length() > 0);
        saKey[i] = isPlanLines[i] ? itemVO2[i].getCorder_bb1id() : itemVO2[i]
            .getCsourcebillbid();
        uaNumAdded[i] = itemVO2[i].getNinnum();
      }
    }

    ArrayList list = new ArrayList();
    list.add(isPlanLines);
    list.add(saKey);
    list.add(uaNumAdded);

    return list;
  }

  /**
   * 此处插入方法说明。 功能描述:获得打印模板名称 输入参数: 返回值: 异常处理:
   */
  public String getModuleName() {
    return "4004050302";
  }

  /**
   * 功能描述:初始化 修改：2004-09-10 袁野
   */
  private void initConfigure() {

    if (m_initVO != null) {
      String sMode = m_initVO.getValue().trim();
      if (sMode.equals("供应商"))
        m_nMode = 0;
      else if (sMode.equals("存货+供应商"))
        m_nMode = 1;
      else if (sMode.equals("订单"))
        m_nMode = 2;
      else if (sMode.equals("仓库+供应商"))
        m_nMode = 3;
      else
        m_nMode = 4;
    }

    // 仓库
    UIRefPane nRefPanel1 = (UIRefPane) getBillCardPanel1().getHeadItem(
        "cwarehouseid").getComponent();
    nRefPanel1.setIsCustomDefined(true);
    nRefPanel1.setRefModel(new nc.ui.ps.pub.WarehouseRef(m_sUnitCode));
    AbstractRefModel refModel = nRefPanel1.getRefModel();
    ((WarehouseRef) refModel).setVMIFlag(true);
    // 部门
    UIRefPane nRefPanel2 = (UIRefPane) getBillCardPanel1()
        .getHeadItem("cdptid").getComponent();
    String strWherePart = " AND (deptattr IN ('2','4')) ";
    nRefPanel2.getRefModel().addWherePart(strWherePart);

    return;
  }

  /**
   * 此处插入方法说明。 功能描述:初始化小数位 修改：2004-09-10 袁野
   */
  private BillData initDecimal1(BillData bd) {
    // 获得系统初始化参数
    // int measure[] = nc.ui.pu.pub.PuTool.getDigitBatch(m_sUnitCode, new
    // String[]{"BD501","BD505","BD301"});

    if (measure == null || measure.length == 0) {
      MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes.getInstance()
          .getStrByID("40040503", "UPP40040503-000026")/* @res "获得系统初始化参数" */,
          nc.ui.ml.NCLangRes.getInstance().getStrByID("40040503",
              "UPP40040503-000027")/* @res "无法获得系统初始化参数！" */);
      return null;
    }

    // 获得主计量小数位
    m_unitDecimal = measure[0];

    // 获得采购单价小数位
    m_priceDecimal = measure[1];

    // 获得采购金额小数位
    m_moneyDecimal = measure[2];

    // 获得辅计量小数位
    m_unitDecimalAssist = measure[3];

    // 设置系统初始化参数
    int nMeasDecimal = m_unitDecimal;
    int nPriceDecimal = m_priceDecimal;
    int nMoneyDecimal = m_moneyDecimal;

    // 获得单据元素对应的控件,并修改控件的属性
    if (bd.getBodyItem("ninnum") != null) {
      bd.getBodyItem("ninnum").setDecimalDigits(nMeasDecimal);
    }
    if (bd.getBodyItem("nshouldoutnum") != null) {
      bd.getBodyItem("nshouldoutnum").setDecimalDigits(nMeasDecimal);
    }
    if (bd.getBodyItem("noutnum") != null) {
      bd.getBodyItem("noutnum").setDecimalDigits(nMeasDecimal);
    }
    if (bd.getBodyItem("nshouldinnum") != null) {
      bd.getBodyItem("nshouldinnum").setDecimalDigits(nMeasDecimal);
    }
    if (bd.getBodyItem("ntranoutnum") != null) {
      bd.getBodyItem("ntranoutnum").setDecimalDigits(nMeasDecimal);
    }
    if (bd.getBodyItem("nshouldoutassistnum") != null) {
      bd.getBodyItem("nshouldoutassistnum").setDecimalDigits(
          m_unitDecimalAssist);
    }
    if (bd.getBodyItem("noutassistnum") != null) {
      bd.getBodyItem("noutassistnum").setDecimalDigits(m_unitDecimalAssist);
    }
    if (bd.getBodyItem("nneedinassistnum") != null) {
      bd.getBodyItem("nneedinassistnum").setDecimalDigits(m_unitDecimalAssist);
    }
    if (bd.getBodyItem("ninassistnum") != null) {
      bd.getBodyItem("ninassistnum").setDecimalDigits(m_unitDecimalAssist);
    }
    if (bd.getBodyItem("nprice") != null) {
      bd.getBodyItem("nprice").setDecimalDigits(nPriceDecimal);
    }
    if (bd.getBodyItem("nmny") != null) {
      bd.getBodyItem("nmny").setDecimalDigits(nMoneyDecimal);
    }
    if (bd.getBodyItem("nplannedmny") != null) {
      bd.getBodyItem("nplannedmny").setDecimalDigits(nMoneyDecimal);
    }

    return bd;
  }

  /**
   * 此处插入方法说明。 功能描述:初始化小数位 输入参数: 返回值: 异常处理:
   */
  private BillData initDecimal2(BillData bd) {

    // 设置系统初始化参数
    int nMeasDecimal = m_unitDecimal;
    int nPriceDecimal = m_priceDecimal;
    int nMoneyDecimal = m_moneyDecimal;

    // 获得单据元素对应的控件,并修改控件的属性
    if (bd.getBodyItem("nordernum") != null) {
      bd.getBodyItem("nordernum").setDecimalDigits(nMeasDecimal);
    }
    if (bd.getBodyItem("ngaugenum") != null) {
      bd.getBodyItem("ngaugenum").setDecimalDigits(nMeasDecimal);
    }
    if (bd.getBodyItem("nprice") != null) {
      bd.getBodyItem("nprice").setDecimalDigits(nPriceDecimal);
    }
    if (bd.getBodyItem("ntaxprice") != null) {
      bd.getBodyItem("ntaxprice").setDecimalDigits(nPriceDecimal);
    }
    if (bd.getBodyItem("nmoney") != null) {
      bd.getBodyItem("nmoney").setDecimalDigits(nMoneyDecimal);
    }
    if (bd.getBodyItem("ntaxmoney") != null) {
      bd.getBodyItem("ntaxmoney").setDecimalDigits(nMoneyDecimal);
    }
    if (bd.getBodyItem("ntotalmoney") != null) {
      bd.getBodyItem("ntotalmoney").setDecimalDigits(nMoneyDecimal);
    }
    //

    // 暂估应付支持外币 zhf 2008-06-27
    BillItem item1 = bd.getBodyItem("noriginalnetprice");
    item1.setDecimalDigits(nPriceDecimal);

    item1 = bd.getBodyItem("norgnettaxprice");
    item1.setDecimalDigits(nPriceDecimal);

    BillItem item2 = bd.getBodyItem("noriginalcurmny");
    item2.setDecimalDigits(nMoneyDecimal);

    item2 = bd.getBodyItem("noriginaltaxpricemny");
    item2.setDecimalDigits(nMoneyDecimal);
    // end

    // ------ by zhaoyha at 2008.10.23 -----------------------
    //支持暂估应付页签的本币
    BillItem item3 = bd.getBodyItem("nzgyfnotaxprice"); //应付本币无税单价
    if (item3 != null) {
      item3.setDecimalDigits(nPriceDecimal);
    }
    item3=bd.getBodyItem("nzgyfnotaxmoney"); //应付本币无税金额
    if (item3 != null) {
      item3.setDecimalDigits(nMoneyDecimal);
    }
    item3 = bd.getBodyItem("nzgyfprice"); //应付本币含税单价
    if (item3 != null) {
      item3.setDecimalDigits(nPriceDecimal);
    }
    item3=bd.getBodyItem("nzgyfmoney"); //应付本币含税金额
    if (item3 != null) {
      item3.setDecimalDigits(nMoneyDecimal);
    }
    
    return bd;
  }

  /**
   * 此处插入方法说明。 功能描述:初始化小数位 输入参数: 返回值: 异常处理:
   */
  private BillListData initListDecimal(BillListData bd) {
    // 设置系统初始化参数
    int nMeasDecimal = m_unitDecimal;
    int nPriceDecimal = m_priceDecimal;
    int nMoneyDecimal = m_moneyDecimal;

    // 获得单据元素对应的控件,并修改控件的属性
    BillItem item1 = bd.getBodyItem("ninnum");
    item1.setDecimalDigits(nMeasDecimal);

    BillItem item2 = bd.getBodyItem("nprice");
    item2.setDecimalDigits(nPriceDecimal);

    BillItem item3 = bd.getBodyItem("nmny");
    item3.setDecimalDigits(nMoneyDecimal);

    return bd;
  }

  /**
   * 为了减少初始化时前后台交互的次数，一次性获取多个系统参数 作者:袁野 日期：2004-09-09
   */
  public void initpara() {
    try {

      Object[] objs = null;
      ServcallVO[] scds = new ServcallVO[3];

      // 获得系统初始化参数
      scds[0] = new ServcallVO();
      scds[0].setBeanName("nc.itf.pu.pub.IPub");
      scds[0].setMethodName("getDigitBatch");
      scds[0].setParameter(new Object[] {
          m_sUnitCode, new String[] {
              "BD501", "BD505", "BD301", "BD502"
          }
      });
      scds[0].setParameterTypes(new Class[] {
          String.class, String[].class
      });

      // 库存是否启用
      scds[1] = new ServcallVO();
      scds[1].setBeanName("nc.itf.pu.pub.IPub");
      scds[1].setMethodName("isEnabled");
      scds[1].setParameter(new Object[] {
          m_sUnitCode, "IC"
      });
      scds[1].setParameterTypes(new Class[] {
          String.class, String.class
      });

      // 获得系统启用日期
      scds[2] = new ServcallVO();
      scds[2].setBeanName("nc.itf.uap.sf.ICreateCorpQueryService");
      scds[2].setMethodName("queryEnabledPeriod");
      scds[2].setParameter(new Object[] {
          m_sUnitCode, "PO"
      });
      scds[2].setParameterTypes(new Class[] {
          String.class, String.class
      });
      //
      objs = nc.ui.scm.service.LocalCallService.callService(scds);
      //
      measure = (int[]) objs[0];// 获得系统初始化参数
      m_bICStartUp = ((UFBoolean) objs[1]).booleanValue();// 库存是否启用

      /*
       * 系统启用日期,字符串数组，长度为5，各元素如下：<br> <code>0</code> - 启用年；<br> <code>1</code> -
       * 启用月；<br> <code>2</code> - 时间戳(char 19)；<br> <code>3</code> -
       * 对应自然年月的首天；<br> <code>4</code> - 对应自然年月的末天。
       */
      String[] sysDate = (String[]) objs[2];
      m_dSystem = PuPubVO.getUFDate(sysDate[3]);
      // 系统启用日期向后退一天是期初单据允许的最大单据日期
      m_dSystem = m_dSystem.getDateBefore(1);
      // 暂估应付
      m_bZGYF = SysInitBO_Client.getParaBoolean(m_sUnitCode, "PO52");

      // 获得系统设置的分单方式
      m_initVO = SysInitBO_Client.queryByParaCode(m_sUnitCode, "PO21");

      // 获得系统设置的暂估方式和差异转入方式
      Hashtable t = SysInitBO_Client.queryBatchParaValues(m_sUnitCode,
          new String[] {
              "PO27", "BD301"
          });
      if (t == null || t.size() == 0)
        return;

      if (t.get("PO27") != null)
        m_sEstPriceSource = t.get("PO27").toString().trim();
      if (t.get("BD301") != null) {
        Object temp = t.get("BD301");
        m_sCurrTypeID = temp.toString();
      }
      else {
        SCMEnv.out("未获取初始化参数：本位币[BD301], 请检查...");
      }

    }
    catch (Exception e) {
      SCMEnv.out(e);
      MessageDialog.showErrorDlg(this,
          nc.ui.ml.NCLangRes.getInstance().getStrByID("40040503",
              "UPP40040503-000028")/* @res "获取系统初始化参数出错" */, e.getMessage());
      return;
    }
  }

  /**
   * 查询模版初始化 作者:袁野 日期：2004-09-10
   */
  public InitialUIQryDlg initQueryModel1(InitialUIQryDlg condClient,
      String pk_corp, boolean bLinkQuery) {

    // 初始化查询模板
    condClient = new InitialUIQryDlg(this);
    condClient.setTitle(nc.ui.ml.NCLangRes.getInstance().getStrByID(
        "4004050302", "UPP4004050302-000006")/* @res "期初暂估查询" */);
    condClient.setTempletID(pk_corp, "400405020201", getClientEnvironment()
        .getUser().getPrimaryKey(), null, "40040502001");

    condClient.setValueRef("dbilldate", "日历");
    condClient.setValueRef("cinvclassid", "存货分类");
    condClient.setValueRef("coperatorid", "操作员");
    condClient.setValueRef("cwarehouseid", "仓库档案");

    UIRefPane vendorRef = new UIRefPane();
    vendorRef.setRefNodeName("供应商档案");
    condClient.setValueRef("cvendorbaseid", vendorRef);

    UIRefPane inventoryRef = new UIRefPane();
    inventoryRef.setRefNodeName("存货档案");
    condClient.setValueRef("cbaseid", inventoryRef);
    /*
     * since v502,隐藏收货公司查询条件，因为, 分收集结模式下，入库单所属公司也记为采购公司，
     * 这样，期初暂估入库单实际上是单个公司（采购公司）单据，用采购订单的收货公司是查询不到的。
     */
    // UIRefPane corpPane = new UIRefPane();
    // corpPane.setRefNodeName("公司目录");
    // condClient.setValueRef("pk_stockcorp",corpPane);
    //    
    if (!bLinkQuery) {
      condClient.setDefaultValue("dbilldate", "dbilldate",
          getClientEnvironment().getDate().toString());
      condClient.setIsWarningWithNoInput(true);

      // 加载自定义项名称
      nc.ui.scm.pub.def.DefSetTool.updateQueryConditionClientUserDef(
          condClient, pk_corp, "icbill", "vuserdef", "ic_general_b.vuserdef");

      /* 封存的基础数据能被参照 */
      condClient.setSealedDataShow(true);
      condClient.setShowPrintStatusPanel(true);
    }

    // 数据权限控制
    condClient.setRefsDataPowerConVOs(nc.ui.pub.ClientEnvironment.getInstance()
        .getUser().getPrimaryKey(), new String[] {
      nc.ui.pub.ClientEnvironment.getInstance().getCorporation()
          .getPrimaryKey()
    }, new String[] {
        "供应商档案", "仓库档案", "存货档案", "存货分类"
    }, new String[] {
        "cvendorbaseid", "cwarehouseid", "cbaseid", "cinvclassid"
    }, new int[] {
        0, 2, 0, 0
    });

    return condClient;
  }

  /**
   * 查询模版初始化 作者:袁野 日期：2004-09-09
   */
  public void initQueryModel2() {
    if (m_condClient2 == null) {
      m_condClient2 = new PoQueryCondition(this, nc.ui.ml.NCLangRes
          .getInstance().getStrByID("4004050302", "UPP4004050302-000006")/*
                                                                           * @res
                                                                           * "期初暂估查询"
                                                                           */);
      m_condClient2.setTempletID(m_sUnitCode, "4004050302",
          getClientEnvironment().getUser().getPrimaryKey(), null, "PS04");

      m_condClient2.setValueRef("dorderdate", "日历");
      m_condClient2.setValueRef("cinvclassid", "存货分类");

      UIRefPane vendorRef = new UIRefPane();
      vendorRef.setRefNodeName("供应商档案");
      vendorRef.getRefModel().addWherePart(" and frozenflag = 'N' ");
      m_condClient2.setValueRef("cvendorbaseid", vendorRef);

      UIRefPane inventoryRef = new UIRefPane();
      inventoryRef.setRefNodeName("存货档案");
      inventoryRef.getRefModel().addWherePart(
          " and upper(bd_invmandoc.sealflag) = 'N' ");
      m_condClient2.setValueRef("cbaseid", inventoryRef);

      m_condClient2.setDefaultValue("dorderdate", "dorderdate",
          getClientEnvironment().getDate().toString());

      m_condClient2.setIsWarningWithNoInput(true);
      /* 封存的基础数据能被参照 */
      m_condClient2.setSealedDataShow(true);

      // 数据权限控制
      m_condClient2.setRefsDataPowerConVOs(nc.ui.pub.ClientEnvironment
          .getInstance().getUser().getPrimaryKey(), new String[] {
        nc.ui.pub.ClientEnvironment.getInstance().getCorporation()
            .getPrimaryKey()
      }, new String[] {
          "供应商档案", "存货档案", "存货分类"
      }, new String[] {
          "cvendorbaseid", "cbaseid", "cinvclassid"
      }, new int[] {
          0, 0, 0
      });

    }
  }

  /**
   * 此处插入方法说明。 功能描述:获得要打印的字段是否为数字 输入参数:主键 返回值: 异常处理:
   */
  public boolean isNumber(String itemKey) {
    itemKey = itemKey.trim();
    itemKey = itemKey.substring(0, 1);
    if (itemKey.equals("n"))
      return true;
    else
      return false;
  }

  /**
   * 此处插入方法说明。 功能描述:判断期初暂估入库单是否已经结算（包括全部结算和部分结算） 输入参数: 返回值: 异常处理:
   */
  private boolean isSettled(GeneralHVO VO) {
    boolean b = false;
    GeneralBb3VO bb3VO[] = VO.getGrandVO();

    for (int i = 0; i < bb3VO.length; i++) {
      UFDouble nAccumNum = bb3VO[i].getNaccountnum1();
      if (nAccumNum != null && Math.abs(nAccumNum.doubleValue()) > 0.0) {
        b = true;
        break;
      }
    }
    return b;
  }

  /**
   * 功能描述:增加(订单生成入库单)
   */
  public void onAppend() {
    this.showHintMessage("");
    // 查询来源是订单的入库单的业务类型
    try {
      m_sBiztypeID = EstimateHelper.querySpeBiztypeID(m_sUnitCode);
    }
    catch (Exception e) {
      MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes.getInstance()
          .getStrByID("40040503", "UPP40040503-000063")/*
                                                         * @res
                                                         * "查询来源是订单的入库单的业务类型"
                                                         */, e.getMessage());
      return;
    }
    if (m_sBiztypeID == null || m_sBiztypeID.length == 0) {
      MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes.getInstance()
          .getStrByID("40040503", "UPP40040503-000063")/*
                                                         * @res
                                                         * "查询来源是订单的入库单的业务类型"
                                                         */, nc.ui.ml.NCLangRes.getInstance().getStrByID("40040503",
          "UPP40040503-000064")/*
                                 * @res "来源是订单的入库单的业务类型不存在，不能通过订单增加期初暂估入库单！"
                                 */);
      return;
    }

    getBillCardPanel1().setVisible(false);
    setLayout(new java.awt.BorderLayout());
    add(getBillCardPanel2(), java.awt.BorderLayout.CENTER);
    getBillCardPanel2().getBillData().clearViewData();
    getBillCardPanel2().setEnabled(false);
    getBillCardPanel2().setVisible(true);
    this.setButtons(m_buttons2);
    m_nButtonState = new int[m_buttons2.length];

    // 设置除查询外的所有按钮为灰
    for (int i = 0; i < 5; i++) {
      m_nButtonState[i] = 1;
    }
    m_nButtonState[4] = 0;
    m_nUIState = 2;
    changeButtonState();

    m_bAdd = true;

    // 自动弹出订单查询对话框
    onQueryOrder();
  }

  /**
   * 此处插入方法说明。 功能描述: 输入参数: 返回值: 异常处理: 日期:
   */
  private void onBillRelateQuery() {
    if (m_VOs == null || m_VOs.length == 0)
      return;

    GeneralHVO VO = m_VOs[m_nPresentRecord];
    nc.ui.scm.sourcebill.SourceBillFlowDlg soureDlg = new nc.ui.scm.sourcebill.SourceBillFlowDlg(
        this, "4T", /* 当前单据类型 */
        VO.getHeadVO().getCgeneralhid(), /* 当前单据ID */
        null, /* 当前业务类型 */
        getClientEnvironment().getUser().getPrimaryKey(), /* 当前用户ID */
        VO.getHeadVO().getVbillcode()); /* 单据编码 */
        
    soureDlg.showModal();
  }

  /**
   * 此处插入方法说明。 功能描述:取消 输入参数: 返回值: 异常处理:
   */
  public void onCancelling() {
    if (m_bGenerate) {
      // 返回列表
      getBillCardPanel1().setVisible(false);

      add(getBillListPanel(), java.awt.BorderLayout.CENTER);
      this.setButtons(m_buttons31);
      m_nButtonState = new int[m_buttons3.length];

      // 隐藏cbiztype,cproviderid,cdptid,cbizid,cwarehouseid,cwhsmanagerid
      getBillListPanel().hideHeadTableCol("cbiztype");
      getBillListPanel().hideHeadTableCol("cproviderid");
      getBillListPanel().hideHeadTableCol("cdptid");
      getBillListPanel().hideHeadTableCol("cbizid");
      getBillListPanel().hideHeadTableCol("cwarehouseid");
      getBillListPanel().hideHeadTableCol("cwhsmanagerid");

      // 未保存的暂估入库单不再显示
      Vector v1 = new Vector();
      for (int i = 0; i < m_VOs.length; i++) {
        if (i != m_nPresentRecord || 
            PuPubVO.getString_TrimZeroLenAsNull(m_VOs[m_nPresentRecord].getHeadVO().getCgeneralhid())!=null)
          v1.addElement(m_VOs[i]);
      }
      
      if (v1 == null || v1.size() == 0) {
        // 不存在暂估入库单
        getBillListPanel().getHeadBillModel().clearBodyData();
        getBillListPanel().getBodyBillModel().clearBodyData();
        getBillListPanel().updateUI();

        // 除查询,切换外其他按钮为灰
        for (int i = 0; i < 6; i++)
          m_nButtonState[i] = 1;
        m_nButtonState[3] = 0;
        m_nButtonState[4] = 0;
        m_nUIState = 3;
        changeButtonState();
        //将缓存VO清空
        m_VOs=null;
        m_bGenerate=false;
        m_bAdd=false;
        m_bSettling=false;
        getBillCardPanel1().setEnabled(false);
        return;
      }

      // 存在暂估入库单
      m_VOs = new GeneralHVO[v1.size()];
      v1.copyInto(m_VOs);

      Vector v2 = new Vector();
      for (int i = 0; i < m_VOs.length; i++)
        v2.addElement(m_VOs[i].getHeadVO());
      GeneralHHeaderVO head[] = new GeneralHHeaderVO[v2.size()];
      v2.copyInto(head);

      GeneralHItemVO body[] = m_VOs[0].getBodyVO();

      getBillListPanel().getHeadBillModel().setBodyDataVO(head);
      getBillListPanel().getHeadBillModel().execLoadFormula();
      getBillListPanel().getBodyBillModel().setBodyDataVO(body);
      getBillListPanel().getBodyBillModel().execLoadFormula();
      getBillListPanel().getHeadBillModel().updateValue();
      getBillListPanel().getBodyBillModel().updateValue();
      getBillListPanel().updateUI();
      //

      // 显示备注
      for (int i = 0; i < head.length; i++) {
        getBillListPanel().getHeadBillModel().setValueAt(head[i].getVnote(), i,
            "vnote");
      }
      //
      setListBtnsStateEditable();
      // // 除全消,作废,切换外其他按钮为正常
      // for (int i = 0; i < 8; i++) {
      // m_nButtonState[i] = 0;
      // }
      // m_nButtonState[1] = 1;
      // m_nButtonState[2] = 1;
      // m_nButtonState[4] = 1;
      // m_nButtonState[5] = 1;
      // m_nButtonState[6] = 1;
      // m_nButtonState[7] = 1;
      //
      // m_nUIState = 3;
      // changeButtonState();

    }
    else {
      m_bAdd = false;
      getBillCardPanel1().setEnabled(false);
      getBillCardPanel1().setBillValueVO(m_VOs[m_nPresentRecord]);
      getBillCardPanel1().getBillModel().execLoadFormula();
      getBillCardPanel1().updateValue();
      getBillCardPanel1().updateUI();

      // 显示备注
      UIRefPane nRefPanel1 = (UIRefPane) getBillCardPanel1().getHeadItem(
          "vnote").getComponent();
      nRefPanel1.setValue(m_VOs[m_nPresentRecord].getHeadVO().getVnote());
      // 显示来源单据信息
      nc.ui.pu.pub.PuTool.loadSourceBillData(getBillCardPanel1(), "4T");
      // 恢复按钮状态
      for (int i = 0; i < m_nButtonState.length; i++)
        m_nButtonState[i] = m_nRestoreState[i];
      // 暂估应付时,不可以修改，也就不可以行操作
      if (m_bZGYF.booleanValue()) {
        m_nButtonState[1] = 1;
      }
      m_nUIState = 1;
      changeButtonState();
    }
  }

  /**
   * 此处插入方法说明。 功能描述:期初暂估入库单卡片查询 输入参数: 返回值: 异常处理:
   */
  public void onCardQuery() {
    this.showHintMessage("");

    if (!m_bICStartUp) {
      // 库存未启用
      MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes.getInstance()
          .getStrByID("40040503", "UPP40040503-000065")/* @res "期初暂估入库单查询" */,
          nc.ui.ml.NCLangRes.getInstance().getStrByID("40040503",
              "UPP40040503-000036")/* @res "库存未启用，不存在入库单！" */);
      return;
    }

    if (m_condClient1 == null)
      m_condClient1 = initQueryModel1(m_condClient1, m_sUnitCode, false);

    m_condClient1.hideNormal();
    m_condClient1.showModal();

    if (m_condClient1.isCloseOK()) {
      // 获取期初暂估入库单查询条件
      ConditionVO conditionVO[] = m_condClient1.getConditionVO();

      // 查询
      try {
        long tTime = System.currentTimeMillis();
        m_bGenerate = false;
        m_VOs = EstimateHelper.queryStockForEstimate(m_sUnitCode, conditionVO);
        if (m_VOs == null || m_VOs.length == 0) {
          // m_bBodyQuery = null;
          MessageDialog.showHintDlg(this, nc.ui.ml.NCLangRes.getInstance()
              .getStrByID("40040503", "UPP40040503-000065")/*
                                                             * @res "期初暂估入库单查询"
                                                             */, nc.ui.ml.NCLangRes.getInstance().getStrByID("40040503",
              "UPP40040503-000066")/*
                                     * @res "没有符合条件的期初暂估入库单！"
                                     */);

          // 清空数据
          getBillCardPanel1().getBillData().clearViewData();
          getBillCardPanel1().updateValue();
          getBillCardPanel1().updateUI();
          // 设置除增加,打印，查询和列表外的所有按钮为灰
          for (int i = 0; i < 16; i++) {
            m_nButtonState[i] = 1;
          }
          m_nButtonState[0] = 0;
          m_nButtonState[6] = 0;
          m_nButtonState[11] = 0;
          m_nButtonState[12] = 0;
          m_nButtonState[13] = 0;

          m_nUIState = 1;
          changeButtonState();
          return;
        }
        tTime = System.currentTimeMillis() - tTime;
        SCMEnv.out("期初暂估入库单查询时间：" + tTime + " ms!");

        // 显示查询结果
        m_nPresentRecord = 0;

        // m_bBodyQuery = new UFBoolean[m_VOs.length];
        // for(int i = 0; i < m_VOs.length; i++){
        // if(i == 0) m_bBodyQuery[i] = new UFBoolean(true);
        // else m_bBodyQuery[i] = new UFBoolean(false);
        // }

        getBillCardPanel1().setBillValueVO(m_VOs[0]);
        getBillCardPanel1().getBillModel().execLoadFormula();
        
        // 设置来源单据号
        nc.ui.pu.pub.PuTool.loadSourceBillData(getBillCardPanel1(), "4T");
        getBillCardPanel1().updateValue();
        getBillCardPanel1().updateUI();
        // 显示备注
        UIRefPane nRefPanel1 = (UIRefPane) getBillCardPanel1().getHeadItem(
            "vnote").getComponent();
        // 防止不可编辑时,鼠标点击后就消失
        nRefPanel1.setAutoCheck(false);
        nRefPanel1.setValue(m_VOs[0].getHeadVO().getVnote());
        
        // 显示库存组织
        setCalbodyValue();

        // 设置除保存,取消,删行外所有按钮为正常
        for (int i = 0; i < 16; i++) {
          m_nButtonState[i] = 0;
        }
        for (int i = 2; i < 5; i++)
          m_nButtonState[i] = 1;
        if (m_nPresentRecord == 0) {
          for (int i = 7; i < 9; i++)
            m_nButtonState[i] = 1;
        }
        if (m_nPresentRecord == m_VOs.length - 1) {
          for (int i = 9; i < 11; i++)
            m_nButtonState[i] = 1;
        }
        if (m_VOs.length == 1) {
          for (int i = 7; i < 11; i++)
            m_nButtonState[i] = 1;
        }
        // 暂估应付时,不可以修改，也就不可以行操作
        if (m_bZGYF.booleanValue()) {
          m_nButtonState[1] = 1;
        }
        m_nUIState = 1;
        changeButtonState();
      }
      catch (java.sql.SQLException e) {
        MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes.getInstance()
            .getStrByID("40040503", "UPP40040503-000065")/*
                                                           * @res "期初暂估入库单查询"
                                                           */, nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON",
            "UPPSCMCommon-000412")/*
                                   * @res "SQL语句错误！"
                                   */);
        SCMEnv.out(e);
        return;
      }
      catch (ArrayIndexOutOfBoundsException e) {
        MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes.getInstance()
            .getStrByID("40040503", "UPP40040503-000065")/*
                                                           * @res "期初暂估入库单查询"
                                                           */, nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON",
            "UPPSCMCommon-000426")/*
                                   * @res "数组越界错误！"
                                   */);
        SCMEnv.out(e);
        return;
      }
      catch (NullPointerException e) {
        MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes.getInstance()
            .getStrByID("40040503", "UPP40040503-000065")/*
                                                           * @res "期初暂估入库单查询"
                                                           */, nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON",
            "UPPSCMCommon-000427")/*
                                   * @res "空指针错误！"
                                   */);
        SCMEnv.out(e);
        return;
      }
      catch (Exception e) {
        MessageDialog.showErrorDlg(this,
            nc.ui.ml.NCLangRes.getInstance().getStrByID("40040503",
                "UPP40040503-000065")/* @res "期初暂估入库单查询" */, e.getMessage());
        SCMEnv.out(e);
        return;
      }
    }
  }

  /**
   * 此处插入方法说明。 功能描述:退出系统 输入参数: 返回值: 异常处理:
   * 
   * @return boolean
   */
  public boolean onClosing() {
    if (m_bSettling) {
      // 数据已加锁
      int nReturn = MessageDialog.showYesNoCancelDlg(this,
          nc.ui.ml.NCLangRes.getInstance().getStrByID("4004050302",
              "UPP4004050302-000008")/* @res "退出期初暂估处理" */, nc.ui.ml.NCLangRes
              .getInstance().getStrByID("common", "UCH001")/*
                                                             * @res
                                                             * "是否保存已修改的数据？"
                                                             */);
      if (nReturn == MessageDialog.ID_YES) {
        boolean bExistSucc = onSave();
        if (m_bSettling) {
          m_bSettling = false;
        }
        return bExistSucc;
      }
      else if (nReturn == MessageDialog.ID_NO) {
        m_bSettling = false;
        return true;
      }
      else {
        return false;
      }
    }
    m_bSettling = false;
    return true;
  }

  /**
   * 此处插入方法说明。 功能描述:删行 输入参数: 返回值: 异常处理:
   */
  public void onDeleteLine() {
    if (!getBillCardPanel1().delLine()) {
      MessageDialog.showHintDlg(this, nc.ui.ml.NCLangRes.getInstance()
          .getStrByID("common", "UC001-0000013")/* @res "删行" */,
          nc.ui.ml.NCLangRes.getInstance().getStrByID("40040503",
              "UPP40040503-000067")/* @res "没有选中期初暂估行！" */);
    }

    // 保存按钮状态
    // for(int i=0;i<m_nButtonState.length;i++)
    // m_nRestoreState[i]=m_nButtonState[i];
  }

  /**
   * 功能描述:期初暂估作废 参数： 返回： 作者：熊海情 创建：2001-6-22 14:41:50 修改：晁志平 FOR V30
   */
  public void onDiscard() {
    this.showHintMessage("");
    Vector vecNoSelected = new Vector();
    Integer nSelected[] = null;
    boolean b = getBillCardPanel1().isShowing();
    if (!b) {
      Vector v = new Vector();
      int nRow = getBillListPanel().getHeadBillModel().getRowCount();
      for (int i = 0; i < nRow; i++) {
        int nStatus = getBillListPanel().getHeadBillModel().getRowState(i);
        if (nStatus == BillModel.SELECTED)
          v.addElement(new Integer(i));
        else
          vecNoSelected.addElement(new Integer(i));
      }
      nSelected = new Integer[v.size()];
      v.copyInto(nSelected);
    }
    else {
      nSelected = new Integer[1];
      nSelected[0] = new Integer(m_nPresentRecord);
    }

    if (nSelected == null || nSelected.length == 0) {
      MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes.getInstance()
          .getStrByID("40040503", "UPP40040503-000068")/* @res "期初暂估作废" */,
          nc.ui.ml.NCLangRes.getInstance().getStrByID("40040503",
              "UPP40040503-000069")/* @res "未选中期初暂估入库单！" */);
      return;
    }

    if (MessageDialog.showYesNoDlg(this, nc.ui.ml.NCLangRes.getInstance()
        .getStrByID("4004050302", "UPP4004050302-000010")/*
                                                           * @res "期初暂估作废"
                                                           */, nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UCH002")/*
                                                                         * @res
                                                                         * "确定作废期初暂估？"
                                                                         */) != MessageDialog.ID_YES)
      return;

    try {
      // 获得单据表体
      // if (m_bBodyQuery != null && m_bBodyQuery.length > 0) {
      int iCnt = nSelected.length;
      Vector v1 = new Vector();
      Vector v2 = new Vector();
      for (int i = 0; i < iCnt; i++) {
        // int nStatus =
        // getBillListPanel().getHeadBillModel().getRowState(i);
        if (isNoBodys(m_VOs[nSelected[i].intValue()])) {
          v1.addElement(m_VOs[nSelected[i].intValue()].getHeadVO()
              .getCgeneralhid());
          v2.addElement(m_VOs[nSelected[i].intValue()].getHeadVO().getTs());
        }
      }
      if (!m_bGenerate && v1.size() > 0 && v2.size() > 0) {
        String headKey[] = new String[v1.size()];
        v1.copyInto(headKey);
        String ts[] = new String[v2.size()];
        v2.copyInto(ts);

        ArrayList list = EstimateHelper.queryInitialBodyBatch(headKey, ts);
        if (list == null || list.size() == 0)
          throw new BusinessException(nc.ui.ml.NCLangRes.getInstance()
              .getStrByID("40040503", "UPP40040503-000070")/*
                                                             * @res
                                                             * "发生并发操作，请刷新界面后再试！"
                                                             */);
        int j = 0;
        for (int i = 0; i < iCnt; i++) {
          // int nStatus =
          // getBillListPanel().getHeadBillModel().getRowState(i);
          if (isNoBodys(m_VOs[nSelected[i].intValue()])) {
            ArrayList list0 = (ArrayList) list.get(j);
            GeneralHItemVO tempBodyVO[] = (GeneralHItemVO[]) list0.get(0);
            GeneralBb3VO tempBb3VO[] = (GeneralBb3VO[]) list0.get(1);
            m_VOs[nSelected[i].intValue()].setChildrenVO(tempBodyVO);
            m_VOs[nSelected[i].intValue()].setGrandVO(tempBb3VO);
            // m_bBodyQuery[i] = new UFBoolean(true);
            j++;
          }
        }
      }
      // }
    }
    catch (BusinessException e) {
      SCMEnv.out(e);
      MessageDialog.showHintDlg(this, nc.ui.ml.NCLangRes.getInstance()
          .getStrByID("40040503", "UPP40040503-000045")/* @res "并发操作" */, e
          .getMessage());
      return;
    }
    catch (Exception e) {
      SCMEnv.out(e);
    }

    long tTime = System.currentTimeMillis();
    Vector vTemp = new Vector();
    StringBuffer sMessage = new StringBuffer();
    for (int i = 0; i < nSelected.length; i++) {
      GeneralHVO vo = m_VOs[nSelected[i].intValue()];
      vTemp.addElement(vo);

      // int nBillStatus=vo.getHeadVO().getDr().intValue();
      // if(nBillStatus>0) sMessage.append(vo.getHeadVO().getVbillcode()+"
      // 号期初暂估入库单已经作废！\n");
      // if(isSettled(vo)) sMessage.append(vo.getHeadVO().getVbillcode()+"
      // 号期初暂估入库单已经结算！\n");
    }

    // 如果单据已经作废，不能作废
    if (sMessage.length() > 0) {
      MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes.getInstance()
          .getStrByID("40040503", "UPP40040503-000068")/* @res "期初暂估作废" */,
          sMessage.toString());
      return;
    }

    GeneralHVO VOs[] = new GeneralHVO[vTemp.size()];
    vTemp.copyInto(VOs);

    try {
      java.util.ArrayList listPara = new java.util.ArrayList();
      listPara.add(VOs);
      listPara.add(getClientEnvironment().getUser().getPrimaryKey());
      listPara.add(getCorpPrimaryKey());

      EstimateHelper.discard(listPara);

      // 作废成功，修改单据状态
      for (int i = 0; i < nSelected.length; i++) {
        m_VOs[nSelected[i].intValue()].getHeadVO().setDr(new Integer(1));
        GeneralHItemVO bodyVO[] = m_VOs[nSelected[i].intValue()].getBodyVO();
        for (int j = 0; j < bodyVO.length; j++)
          bodyVO[j].setDr(new Integer(1));
      }
    }
    catch (nc.vo.pub.BusinessException e) {
      SCMEnv.out(e);
      MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes.getInstance()
          .getStrByID("40040503", "UPP40040503-000068")/* @res "期初暂估作废" */, e
          .getMessage());
      return;
    }
    catch (Exception e) {
      SCMEnv.out(e);
      MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes.getInstance()
          .getStrByID("40040503", "UPP40040503-000068")/* @res "期初暂估作废" */, e
          .getMessage());
      return;
    }

    tTime = System.currentTimeMillis() - tTime;
    SCMEnv.out("期初暂估作废时间：" + tTime + " ms!");
    this.showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
        "40040503", "UPP40040503-000071")/* @res "作废成功！" */);

    // 作废后的期初暂估入库单不再显示在界面
    if (!b) {
      // 列表
      Vector v0 = new Vector();
      Vector vTemp1 = new Vector();
      for (int i = 0; i < vecNoSelected.size(); i++) {
        int n = ((Integer) vecNoSelected.elementAt(i)).intValue();
        v0.addElement(m_VOs[n]);
        // vTemp1.addElement(m_bBodyQuery[n]);
      }

      if (v0 == null || v0.size() == 0) {
        // 所有单据已作废完毕
        m_VOs = null;
        // m_bBodyQuery = null;
        getBillListPanel().getHeadBillModel().clearBodyData();
        getBillListPanel().getBodyBillModel().clearBodyData();
        getBillListPanel().updateUI();
        // 除查询,切换外其它为灰
        for (int i = 0; i < 8; i++)
          m_nButtonState[i] = 1;
        for (int i = 3; i < 5; i++)
          m_nButtonState[i] = 0;
        m_nUIState = 3;
        changeButtonState();
        return;
      }

      // 部分单据已作废完毕
      m_VOs = new GeneralHVO[v0.size()];
      v0.copyInto(m_VOs);
      // m_bBodyQuery = new UFBoolean[vTemp1.size()];
      // vTemp1.copyInto(m_bBodyQuery);

      //
      // 获得该张单据的表体
      // if (m_bBodyQuery != null && m_bBodyQuery.length > 0 &&
      // (!m_bBodyQuery[0].booleanValue())) {
      if (isNoBodys(m_VOs[0])) {
        try {
          ArrayList list = EstimateHelper.queryInitialBody(m_VOs[0].getHeadVO()
              .getCgeneralhid(), m_VOs[0].getHeadVO().getTs());
          if (list == null || list.size() == 0)
            throw new BusinessException(nc.ui.ml.NCLangRes.getInstance()
                .getStrByID("40040503", "UPP40040503-000070")/*
                                                               * @res
                                                               * "发生并发操作，请刷新界面后再试！"
                                                               */);
          GeneralHItemVO tempBodyVO[] = (GeneralHItemVO[]) list.get(0);
          GeneralBb3VO tempBb3VO[] = (GeneralBb3VO[]) list.get(1);
          m_VOs[0].setChildrenVO(tempBodyVO);
          m_VOs[0].setGrandVO(tempBb3VO);
        }
        catch (BusinessException e) {
          SCMEnv.out(e);
          MessageDialog.showHintDlg(this, nc.ui.ml.NCLangRes.getInstance()
              .getStrByID("40040503", "UPP40040503-000045")/* @res "并发操作" */,
              e.getMessage());
          return;
        }
        catch (Exception e) {
          SCMEnv.out(e);
        }
        // m_bBodyQuery[0] = new UFBoolean(true);
      }

      Vector v1 = new Vector();
      for (int i = 0; i < m_VOs.length; i++)
        v1.addElement(m_VOs[i].getHeadVO());
      GeneralHHeaderVO head[] = new GeneralHHeaderVO[v1.size()];
      v1.copyInto(head);

      GeneralHItemVO body[] = m_VOs[0].getBodyVO();

      getBillListPanel().getHeadBillModel().setBodyDataVO(head);
      getBillListPanel().getHeadBillModel().execLoadFormula();
      getBillListPanel().getBodyBillModel().setBodyDataVO(body);
      getBillListPanel().getBodyBillModel().execLoadFormula();
      getBillListPanel().getHeadBillModel().updateValue();
      getBillListPanel().getBodyBillModel().updateValue();
      getBillListPanel().updateUI();

      // 显示备注
      for (int i = 0; i < head.length; i++) {
        getBillListPanel().getHeadBillModel().setValueAt(head[i].getVnote(), i,
            "vnote");
      }

      // 除全消,作废和切换外其它正常
      for (int i = 0; i < 8; i++)
        m_nButtonState[i] = 0;
      m_nButtonState[1] = 1;
      m_nButtonState[2] = 1;
      m_nButtonState[4] = 1;
      m_nButtonState[6] = 1;
      m_nButtonState[7] = 1;

      m_nUIState = 3;
      changeButtonState();
    }
    else {
      // 卡片
      Vector v0 = new Vector();
      Vector vTemp1 = new Vector();
      for (int i = 0; i < m_VOs.length; i++) {
        if (i != m_nPresentRecord) {
          v0.addElement(m_VOs[i]);
          // vTemp1.addElement(m_bBodyQuery[i]);
        }
      }
      if (v0.size() > 0) {
        // 部分单据已作废完毕
        if (m_nPresentRecord == m_VOs.length - 1)
          m_nPresentRecord--;
        m_VOs = new GeneralHVO[v0.size()];
        v0.copyInto(m_VOs);
        // m_bBodyQuery = new UFBoolean[vTemp1.size()];
        // vTemp1.copyInto(m_bBodyQuery);

        // 获得该张单据的表体
        // if (m_bBodyQuery != null && m_bBodyQuery.length > 0 &&
        // (!m_bBodyQuery[m_nPresentRecord].booleanValue())) {
        if (!m_bGenerate && isNoBodys(m_VOs[m_nPresentRecord])) {
          // m_bBodyQuery[m_nPresentRecord] = new UFBoolean(true);
          try {
            ArrayList list = EstimateHelper.queryInitialBody(
                m_VOs[m_nPresentRecord].getHeadVO().getCgeneralhid(),
                m_VOs[m_nPresentRecord].getHeadVO().getTs());
            if (list == null || list.size() == 0)
              throw new BusinessException(nc.ui.ml.NCLangRes.getInstance()
                  .getStrByID("40040503", "UPP40040503-000070")/*
                                                                 * @res
                                                                 * "发生并发操作，请刷新界面后再试！"
                                                                 */);
            GeneralHItemVO tempBodyVO[] = (GeneralHItemVO[]) list.get(0);
            GeneralBb3VO tempBb3VO[] = (GeneralBb3VO[]) list.get(1);
            m_VOs[m_nPresentRecord].setChildrenVO(tempBodyVO);
            m_VOs[m_nPresentRecord].setGrandVO(tempBb3VO);
          }
          catch (BusinessException e) {
            SCMEnv.out(e);
            MessageDialog.showHintDlg(this, nc.ui.ml.NCLangRes.getInstance()
                .getStrByID("40040503", "UPP40040503-000045")/*
                                                               * @res "并发操作"
                                                               */, e.getMessage());
            return;
          }
          catch (Exception e) {
            SCMEnv.out(e);
          }
        }

        getBillCardPanel1().setBillValueVO(m_VOs[m_nPresentRecord]);
        getBillCardPanel1().getBillModel().execLoadFormula();
        // 显示备注
        UIRefPane nRefPanel1 = (UIRefPane) getBillCardPanel1().getHeadItem(
            "vnote").getComponent();
        nRefPanel1.setValue(m_VOs[m_nPresentRecord].getHeadVO().getVnote());

        // 设置除保存,取消,删行外所有按钮为正常
        for (int i = 0; i < 16; i++) {
          m_nButtonState[i] = 0;
        }
        for (int i = 2; i < 5; i++)
          m_nButtonState[i] = 1;
        if (m_nPresentRecord == 0) {
          m_nButtonState[7] = 1;
          m_nButtonState[8] = 1;
        }
        if (m_nPresentRecord == m_VOs.length - 1) {
          m_nButtonState[9] = 1;
          m_nButtonState[10] = 1;
        }
        if (m_VOs.length == 1) {
          for (int i = 7; i < 11; i++)
            m_nButtonState[i] = 1;
        }
        // 暂估应付时,不可以修改，也就不可以行操作
        if (m_bZGYF.booleanValue()) {
          m_nButtonState[1] = 1;
        }
        m_nUIState = 1;
        changeButtonState();
      }
      else {
        // 所有单据已作废完毕
        m_VOs = null;
        // m_bBodyQuery = null;
        getBillCardPanel1().getBillData().clearViewData();
        getBillCardPanel1().updateUI();
        // 除增加,打印，查询和列表外的所有按钮为灰
        for (int i = 0; i < 16; i++)
          m_nButtonState[i] = 1;
        m_nButtonState[0] = 0;
        m_nButtonState[6] = 0;
        m_nButtonState[11] = 0;
        m_nButtonState[12] = 0;
        m_nButtonState[13] = 0;
        changeButtonState();
        return;
      }

      getBillCardPanel1().updateValue();
      getBillCardPanel1().updateUI();
      if (m_bGenerate) {
        doRefresh(m_nPresentRecord);
      }
    }
  }

  /**
   * 此处插入方法说明。 功能描述:暂估 输入参数: 返回值: 异常处理:
   */
  public void onEstimate() {
    // 根据订单生成入库单
    Integer nSelected[] = null;
    Vector v = new Vector();
    int nRow = getBillCardPanel2().getRowCount();
    for (int i = 0; i < nRow; i++) {
      int nStatus = getBillCardPanel2().getBillModel().getRowState(i);
      if (nStatus == BillModel.SELECTED)
        v.addElement(new Integer(i));
    }

    if (v.size() > 0) {
      nSelected = new Integer[v.size()];
      v.copyInto(nSelected);
    }
    else {
      MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes.getInstance()
          .getStrByID("40040503", "UPP40040503-000072")/* @res "订单生成入库单" */,
          nc.ui.ml.NCLangRes.getInstance().getStrByID("40040503",
              "UPP40040503-000073")/* @res "未选中订单行！" */);
      return;
    }

    // zhf
    updateOorderVO(nSelected);
    // end

    Vector vv = new Vector();
    // // for (int i = 0; i < nSelected.length; i++)
    // // vv.addElement(m_orderVOs[nSelected[i].intValue()]);
    // // m_orderVOs = new OorderVO[vv.size()];
    // // vv.copyInto(m_orderVOs);
    //
    String strErrMnyLines = "";

    for (int i = 0; i < nSelected.length; i++) {
      OorderVO vo = m_orderVOs[nSelected[i].intValue()];
      UFDouble nMoney = vo.getNmoney();
      if (nMoney == null || nMoney.doubleValue() == 0.0) {
        strErrMnyLines += (nSelected[i].intValue() + 1) + "、";
      }
      vv.add(vo);
    }
    // 去掉最后一个“、”号
    if (strErrMnyLines.length() > 0) {
      strErrMnyLines = strErrMnyLines.substring(0, strErrMnyLines.length() - 1);
    }

    // 如果入库单的入库数量为空或为零，入库单不能进行暂估处理
    if (strErrMnyLines.length() > 0) {
      MessageDialog.showErrorDlg(this, NCLangRes.getInstance().getStrByID(
          "40040503", "UPP40040503-000033"/* @res "暂估处理 */), NCLangRes
          .getInstance().getStrByID("40040503", "UPP40040503-000032"
          /* @res "下列入库单行的暂估金额为0，不能暂估。行序号为：{0}" */, null, new String[] {
            strErrMnyLines
          }));
      return;
    }
    //
    m_bSettling = true;

    OorderVO[] tempOrderVOs = new OorderVO[vv.size()];
    if (vv != null && vv.size() > 0)
      vv.copyInto(tempOrderVOs);
    generateStock(tempOrderVOs);

    // 暂估结束，返回列表单据，列表显示所有新生成的入库单
    this.remove(getBillCardPanel2());
    setLayout(new java.awt.BorderLayout());
    add(getBillListPanel(), java.awt.BorderLayout.CENTER);
    this.setButtons(m_buttons31);
    m_nButtonState = new int[m_buttons3.length];

    // 隐藏cbiztype,cproviderid,cdptid,cbizid,cwarehouseid,cwhsmanagerid,cdispatcherid
    getBillListPanel().hideHeadTableCol("cbiztype");
    getBillListPanel().hideHeadTableCol("cproviderid");
    getBillListPanel().hideHeadTableCol("cdptid");
    getBillListPanel().hideHeadTableCol("cbizid");
    getBillListPanel().hideHeadTableCol("cwarehouseid");
    getBillListPanel().hideHeadTableCol("cwhsmanagerid");
    getBillListPanel().hideHeadTableCol("cstoreorganization");
    getBillListPanel().hideHeadTableCol("pk_purcorp");
    getBillListPanel().hideHeadTableCol("cdispatcherid");

    // // 没有生成期初暂估入库单
    if (m_gVOs == null || m_gVOs.length == 0) {
      // // 设置除查询和切换外所有按钮为灰
      // for (int i = 0; i < 8; i++) {
      // m_nButtonState[i] = 1;
      // }
      // m_nButtonState[3] = 0;
      // m_nButtonState[4] = 0;
      // m_nUIState = 3;
      // changeButtonState();
      //
      setListBtnsStateEditable();
      return;
    }

    // 生成期初暂估入库单
    // 新生成的暂估入库单增加行号
    BillRowNo.setVOsRowNoByRule(m_gVOs, "4T", "crowno");

    // 缓存中交换原来的入库单和新生成的入库单，以便编辑新生成的入库单
    Vector v1 = new Vector();
    if (m_VOs != null && m_VOs.length > 0) {
      for (int i = 0; i < m_VOs.length; i++)
        v1.addElement(m_VOs[i]);
    }
    Vector v2 = new Vector();
    for (int i = 0; i < m_gVOs.length; i++)
      v2.addElement(m_gVOs[i]);

    m_VOs = new GeneralHVO[v2.size()];
    v2.copyInto(m_VOs);
    m_gVOs = new GeneralHVO[v1.size()];
    v1.copyInto(m_gVOs);
    m_nPresentRecord = 0;

    // 显示列表
    Vector v0 = new Vector();
    for (int i = 0; i < m_VOs.length; i++)
      v0.addElement(m_VOs[i].getHeadVO());
    GeneralHHeaderVO head[] = new GeneralHHeaderVO[v0.size()];
    v0.copyInto(head);

    GeneralHItemVO body[] = m_VOs[0].getBodyVO();

    m_nUIState = 3;

    getBillListPanel().getHeadBillModel().setBodyDataVO(head);
    getBillListPanel().getHeadBillModel().execLoadFormula();
    getBillListPanel().getBodyBillModel().setBodyDataVO(body);
    getBillListPanel().getBodyBillModel().execLoadFormula();
    getBillListPanel().getHeadBillModel().updateValue();
    getBillListPanel().getBodyBillModel().updateValue();
    getBillListPanel().updateUI();
    // 显示来源单据信息
    nc.ui.pu.pub.PuTool.loadSourceBillData(getBillListPanel(), "4T");
    //默认选择第一行
    getBillListPanel().getHeadBillModel().setRowState(0, BillModel.SELECTED);
    //
    setListBtnsStateEditable();
  }

  /**
   * 此处插入方法说明。 功能描述:文件管理 输入参数: 返回值: 异常处理: 日期:2003/03/07
   */
  private void onFileManage() {
    Integer nSelected[] = null;
    boolean b = getBillCardPanel1().isVisible();
    if (!b) {
      Vector v = new Vector();
      int nRow = getBillListPanel().getHeadBillModel().getRowCount();
      for (int i = 0; i < nRow; i++) {
        int nStatus = getBillListPanel().getHeadBillModel().getRowState(i);
        if (nStatus == BillModel.SELECTED)
          v.addElement(new Integer(i));
      }
      nSelected = new Integer[v.size()];
      v.copyInto(nSelected);
    }
    else {
      nSelected = new Integer[1];
      nSelected[0] = new Integer(m_nPresentRecord);
    }

    if (nSelected == null || nSelected.length == 0)
      return;

    String sBillID[] = new String[nSelected.length];
    String sBillCode[] = new String[nSelected.length];
    for (int i = 0; i < sBillID.length; i++) {
      int j = nSelected[i].intValue();
      if (j >= 0 && j < m_VOs.length) {
        sBillID[i] = m_VOs[j].getHeadVO().getCgeneralhid();
        sBillCode[i] = m_VOs[j].getHeadVO().getVbillcode();
      }
    }

    if (sBillID != null && sBillID.length > 0) {
      nc.ui.scm.file.DocumentManager.showDM(this,"4T", sBillID);
    }
  }

  /**
   * 此处插入方法说明。 功能描述:首页 输入参数: 返回值: 异常处理:
   */
  public void onFirst() {
    this.showHintMessage("");
    //getBillCardPanel1().getBillModel().clearBodyData();
    //m_nPresentRecord = 0;
    m_nButtonState[7] = 1;
    m_nButtonState[8] = 1;
    m_nButtonState[9] = 0;
    m_nButtonState[10] = 0;
    int tempInx=m_nPresentRecord;
    int firstInx=0;
    while(--tempInx>=0){
        if(PuPubVO.getString_TrimZeroLenAsNull(m_VOs[tempInx].getHeadVO().getCgeneralhid())!=null)
          firstInx=tempInx;
    }
    if(tempInx>=0)
      m_nPresentRecord=firstInx;
    else
      return;
    changeButtonState();
    getBillCardPanel1().getBillModel().clearBodyData();


    // 获得该张单据的表体
    // if(m_bBodyQuery != null && m_bBodyQuery.length > 0 &&
    // (!m_bBodyQuery[m_nPresentRecord].booleanValue())){
    if (isNoBodys(m_VOs[m_nPresentRecord])) {
      try {
        ArrayList list = EstimateHelper.queryInitialBody(
            m_VOs[m_nPresentRecord].getHeadVO().getCgeneralhid(),
            m_VOs[m_nPresentRecord].getHeadVO().getTs());
        if (list == null || list.size() == 0)
          throw new BusinessException(nc.ui.ml.NCLangRes.getInstance()
              .getStrByID("4004050302", "UPP4004050302-000004")/*
                                                                 * @res
                                                                 * "发生并发操作，请刷新界面后再试！"
                                                                 */);
        GeneralHItemVO tempBodyVO[] = (GeneralHItemVO[]) list.get(0);
        GeneralBb3VO tempBb3VO[] = (GeneralBb3VO[]) list.get(1);
        m_VOs[m_nPresentRecord].setChildrenVO(tempBodyVO);
        m_VOs[m_nPresentRecord].setGrandVO(tempBb3VO);
      }
      catch (BusinessException e) {
        SCMEnv.out(e);
        MessageDialog.showHintDlg(this, nc.ui.ml.NCLangRes.getInstance()
            .getStrByID("40040503", "UPP40040503-000045")/* @res "并发操作" */, e
            .getMessage());
        return;
      }
      catch (Exception e) {
        SCMEnv.out(e);
      }
      // m_bBodyQuery[m_nPresentRecord] = new UFBoolean(true);
    }

    getBillCardPanel1().setBillValueVO(m_VOs[0]);
    getBillCardPanel1().getBillModel().execLoadFormula();
    getBillCardPanel1().updateValue();
    getBillCardPanel1().updateUI();
    // 显示来源单据信息
    nc.ui.pu.pub.PuTool.loadSourceBillData(getBillCardPanel1(), "4T");

    // 显示备注
    UIRefPane nRefPanel1 = (UIRefPane) getBillCardPanel1().getHeadItem("vnote")
        .getComponent();
    nRefPanel1.setValue(m_VOs[m_nPresentRecord].getHeadVO().getVnote());

    // 显示库存组织
    setCalbodyValue();

  }

  /**
   * 此处插入方法说明。 功能描述:末页 输入参数: 返回值: 异常处理:
   */
  public void onLast() {
    this.showHintMessage("");
    //getBillCardPanel1().getBillModel().clearBodyData();
    //m_nPresentRecord = m_VOs.length - 1;
    m_nButtonState[7] = 0;
    m_nButtonState[8] = 0;
    m_nButtonState[9] = 1;
    m_nButtonState[10] = 1;

    int tempInx=m_nPresentRecord;
    int lastInx=m_VOs.length;
    while(++tempInx<m_VOs.length){ 
        if(PuPubVO.getString_TrimZeroLenAsNull(m_VOs[tempInx].getHeadVO().getCgeneralhid())!=null)
          lastInx=tempInx;
    }
    if(tempInx<m_VOs.length)
      m_nPresentRecord=lastInx;
    else
      return;
    
    changeButtonState();
    getBillCardPanel1().getBillModel().clearBodyData();


    // 获得该张单据的表体
    // if(m_bBodyQuery != null && m_bBodyQuery.length > 0 &&
    // (!m_bBodyQuery[m_nPresentRecord].booleanValue())){
    if (isNoBodys(m_VOs[m_nPresentRecord])) {
      try {
        ArrayList list = EstimateHelper.queryInitialBody(
            m_VOs[m_nPresentRecord].getHeadVO().getCgeneralhid(),
            m_VOs[m_nPresentRecord].getHeadVO().getTs());
        if (list == null || list.size() == 0)
          throw new BusinessException(nc.ui.ml.NCLangRes.getInstance()
              .getStrByID("4004050302", "UPP4004050302-000004")/*
                                                                 * @res
                                                                 * "发生并发操作，请刷新界面后再试！"
                                                                 */);
        GeneralHItemVO tempBodyVO[] = (GeneralHItemVO[]) list.get(0);
        GeneralBb3VO tempBb3VO[] = (GeneralBb3VO[]) list.get(1);
        m_VOs[m_nPresentRecord].setChildrenVO(tempBodyVO);
        m_VOs[m_nPresentRecord].setGrandVO(tempBb3VO);
      }
      catch (BusinessException e) {
        SCMEnv.out(e);
        MessageDialog.showHintDlg(this, nc.ui.ml.NCLangRes.getInstance()
            .getStrByID("40040503", "UPP40040503-000045")/* @res "并发操作" */, e
            .getMessage());
        return;
      }
      catch (Exception e) {
        SCMEnv.out(e);
      }
      // m_bBodyQuery[m_nPresentRecord] = new UFBoolean(true);
    }

    getBillCardPanel1().setBillValueVO(m_VOs[m_nPresentRecord]);
    getBillCardPanel1().getBillModel().execLoadFormula();
    getBillCardPanel1().updateValue();
    getBillCardPanel1().updateUI();
    // 显示来源单据信息
    nc.ui.pu.pub.PuTool.loadSourceBillData(getBillCardPanel1(), "4T");

    // 显示备注
    UIRefPane nRefPanel1 = (UIRefPane) getBillCardPanel1().getHeadItem("vnote")
        .getComponent();
    nRefPanel1.setValue(m_VOs[m_nPresentRecord].getHeadVO().getVnote());

    // 显示库存组织
    setCalbodyValue();

  }

  /**
   * 此处插入方法说明。 功能描述:卡片到列表 输入参数: 返回值: 异常处理:
   */
  public void onList() {
    this.showHintMessage("");
    getBillCardPanel1().setVisible(false);
    getBillCardPanel2().setVisible(false);
    setLayout(new java.awt.BorderLayout());
    add(getBillListPanel(), java.awt.BorderLayout.CENTER);
    this.setButtons(m_buttons31);
    m_nButtonState = new int[m_buttons3.length];

    // 隐藏cbiztype,cproviderid,cdptid,cbizid,cwarehouseid,cwhsmanagerid,cdispatcherid
    getBillListPanel().hideHeadTableCol("cbiztype");
    getBillListPanel().hideHeadTableCol("cproviderid");
    getBillListPanel().hideHeadTableCol("cdptid");
    getBillListPanel().hideHeadTableCol("cbizid");
    getBillListPanel().hideHeadTableCol("cwarehouseid");
    getBillListPanel().hideHeadTableCol("cwhsmanagerid");
    getBillListPanel().hideHeadTableCol("cstoreorganization");
    getBillListPanel().hideHeadTableCol("pk_purcorp");
    getBillListPanel().hideHeadTableCol("cdispatcherid");

    if (m_VOs != null && m_VOs.length > 0) {
      // 显示卡片状态的当前单据
      Vector v1 = new Vector();
      for (int i = 0; i < m_VOs.length; i++)
        v1.addElement(m_VOs[i].getHeadVO());
      GeneralHHeaderVO head[] = new GeneralHHeaderVO[v1.size()];
      v1.copyInto(head);

      GeneralHItemVO body[] = m_VOs[m_nPresentRecord].getBodyVO();

      getBillListPanel().getHeadBillModel().setBodyDataVO(head);
      getBillListPanel().getHeadBillModel().execLoadFormula();
      getBillListPanel().getBodyBillModel().setBodyDataVO(body);
      getBillListPanel().getBodyBillModel().execLoadFormula();
      getBillListPanel().getHeadBillModel().updateValue();
      getBillListPanel().getBodyBillModel().updateValue();
      // 显示来源单据信息
      nc.ui.pu.pub.PuTool.loadSourceBillData(getBillListPanel(), "4T");
      getBillListPanel().updateUI();

      getBillListPanel().getHeadBillModel().setRowState(m_nPresentRecord,
          BillModel.SELECTED);

      // 显示备注
      for (int i = 0; i < head.length; i++) {
        getBillListPanel().getHeadBillModel().setValueAt(head[i].getVnote(), i,
            "vnote");
      }

      // 设置所有按钮为正常
      for (int i = 0; i < 8; i++) {
        m_nButtonState[i] = 0;
      }
    }
    else {
      // 设置除查询,切换外的所有按钮为灰
      getBillListPanel().getHeadBillModel().clearBodyData();
      getBillListPanel().getBodyBillModel().clearBodyData();
      getBillListPanel().updateUI();

      for (int i = 0; i < 8; i++) {
        m_nButtonState[i] = 1;
      }
      m_nButtonState[3] = 0;
      m_nButtonState[4] = 0;
    }

    m_nUIState = 3;
    changeButtonState();

    // since v53
    if (m_bGenerate) {
      setListBtnsStateEditable();
    }
    else {
      setListBtnsState();
    }
  }

  /**
   * 此处插入方法说明。 功能描述:列表打印 输入参数: 返回值: 异常处理: 2003/11/04
   */
  private void onListPreview() {
    //
    this.showHintMessage("");
    Vector vv = new Vector();
    Integer nSelected[] = null;
    Vector v = new Vector();
    int nRow = getBillListPanel().getHeadBillModel().getRowCount();
    for (int i = 0; i < nRow; i++) {
      //
      Object oTemp = getBillListPanel().getHeadBillModel().getValueAt(i,
          "cstoreorganization");
      if (oTemp != null)
        m_VOs[i].getHeadVO().setCstoreorganization(oTemp.toString());
      //
      int nStatus = getBillListPanel().getHeadBillModel().getRowState(i);
      if (nStatus == BillModel.SELECTED )
        v.addElement(new Integer(i));
      else
        vv.addElement(new Integer(i));
    }
    nSelected = new Integer[v.size()];
    v.copyInto(nSelected);

    if (nSelected == null || nSelected.length == 0) {
      MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes.getInstance()
          .getStrByID("40040503", "UPP40040503-000074")/* @res "批次打印期初暂估" */,
          nc.ui.ml.NCLangRes.getInstance().getStrByID("40040503",
              "UPP40040503-000069")/* @res "未选中期初暂估入库单！" */);
      return;
    }
    //

    try {
      // 获得单据表体
      // if(m_bBodyQuery != null && m_bBodyQuery.length > 0){
      Vector v1 = new Vector();
      Vector v2 = new Vector();
      for (int i = 0; i < nSelected.length; i++) {
        // int
        // nStatus=getBillListPanel().getHeadBillModel().getRowState(i);
        if (isNoBodys(m_VOs[nSelected[i].intValue()])) {
          v1.addElement(m_VOs[nSelected[i].intValue()].getHeadVO()
              .getCgeneralhid());
          v2.addElement(m_VOs[nSelected[i].intValue()].getHeadVO().getTs());
        }
      }
      if (v1.size() > 0 && v2.size() > 0) {
        String headKey[] = new String[v1.size()];
        v1.copyInto(headKey);
        String ts[] = new String[v2.size()];
        v2.copyInto(ts);

        ArrayList list = EstimateHelper.queryInitialBodyBatch(headKey, ts);
        if (list == null || list.size() == 0)
          throw new BusinessException(nc.ui.ml.NCLangRes.getInstance()
              .getStrByID("4004050302", "UPP4004050302-000004")/*
                                                                 * @res
                                                                 * "发生并发操作，请刷新界面后再试！"
                                                                 */);
        int j = 0;
        for (int i = 0; i < nSelected.length; i++) {
          // int
          // nStatus=getBillListPanel().getHeadBillModel().getRowState(i);
          if (isNoBodys(m_VOs[nSelected[i].intValue()])) {
            ArrayList list0 = (ArrayList) list.get(j);
            GeneralHItemVO tempBodyVO[] = (GeneralHItemVO[]) list0.get(0);
            GeneralBb3VO tempBb3VO[] = (GeneralBb3VO[]) list0.get(1);
            m_VOs[nSelected[i].intValue()].setChildrenVO(tempBodyVO);
            m_VOs[nSelected[i].intValue()].setGrandVO(tempBb3VO);
            // m_bBodyQuery[i] = new UFBoolean(true);
            j++;
          }
        }
      }
      // }
    }
    catch (BusinessException e) {
      SCMEnv.out(e);
      MessageDialog.showHintDlg(this, nc.ui.ml.NCLangRes.getInstance()
          .getStrByID("40040503", "UPP40040503-000045")/* @res "并发操作" */, e
          .getMessage());
      return;
    }
    catch (Exception e) {
      SCMEnv.out(e);
      return;
    }

    java.util.ArrayList list = new java.util.ArrayList();
    for (int i = 0; i < nSelected.length; i++) {
      GeneralHVO vo = m_VOs[nSelected[i].intValue()];
      list.add(vo);
    }

    if (list.size() > 0) {
      // if(m_printList == null){
      // m_printList = new ScmPrintTool(getBillCardPanel1());
      // }
      // m_printList.setData(list);
      // m_printList.preview();

      if (m_printList == null) {
        m_printList = new ScmPrintTool(this, getBillCardPanel1(), list,
            getModuleCode());
      }
      else {
        try {
          m_printList.setData(list);
        }
        catch (Exception e1) {
          SCMEnv.out(e1);
          return;
        }
      }
      try {
        m_printList.onBatchPrintPreview(getBillListPanel(),
            getBillCardPanel1(), "4T");
        if (PuPubVO.getString_TrimZeroLenAsNull(m_printList.getPrintMessage()) != null) {
          MessageDialog.showHintDlg(this, nc.ui.ml.NCLangRes.getInstance()
              .getStrByID("SCMCOMMON", "UPPSCMCommon-000270")/* @res "提示" */,
              m_printList.getPrintMessage());
        }
      }
      catch (Exception e) {
        SCMEnv.out(e);
      }
    }
  }

  /**
   * 此处插入方法说明。 功能描述:列表打印 输入参数: 返回值: 异常处理: 2003/11/04
   */
  private void onListPrint() {
    //
    this.showHintMessage("");
    Vector vv = new Vector();
    Integer nSelected[] = null;
    Vector v = new Vector();
    int nRow = getBillListPanel().getHeadBillModel().getRowCount();
    for (int i = 0; i < nRow; i++) {
      //
      Object oTemp = getBillListPanel().getHeadBillModel().getValueAt(i,
          "cstoreorganization");
      if (oTemp != null)
        m_VOs[i].getHeadVO().setCstoreorganization(oTemp.toString());
      //
      int nStatus = getBillListPanel().getHeadBillModel().getRowState(i);
      if (nStatus == BillModel.SELECTED)
        v.addElement(new Integer(i));
      else
        vv.addElement(new Integer(i));
    }
    nSelected = new Integer[v.size()];
    v.copyInto(nSelected);

    if (nSelected == null || nSelected.length == 0) {
      MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes.getInstance()
          .getStrByID("40040503", "UPP40040503-000074")/* @res "批次打印期初暂估" */,
          nc.ui.ml.NCLangRes.getInstance().getStrByID("40040503",
              "UPP40040503-000069")/* @res "未选中期初暂估入库单！" */);
      return;
    }
    //

    try {
      // 获得单据表体
      // if(m_bBodyQuery != null && m_bBodyQuery.length > 0){
      Vector v1 = new Vector();
      Vector v2 = new Vector();
      for (int i = 0; i < nSelected.length; i++) {
        // int
        // nStatus=getBillListPanel().getHeadBillModel().getRowState(i);
        if (isNoBodys(m_VOs[nSelected[i].intValue()])) {
          v1.addElement(m_VOs[nSelected[i].intValue()].getHeadVO()
              .getCgeneralhid());
          v2.addElement(m_VOs[nSelected[i].intValue()].getHeadVO().getTs());
        }
      }
      if (v1.size() > 0 && v2.size() > 0) {
        String headKey[] = new String[v1.size()];
        v1.copyInto(headKey);
        String ts[] = new String[v2.size()];
        v2.copyInto(ts);

        ArrayList list = EstimateHelper.queryInitialBodyBatch(headKey, ts);
        if (list == null || list.size() == 0)
          throw new BusinessException(nc.ui.ml.NCLangRes.getInstance()
              .getStrByID("4004050302", "UPP4004050302-000004")/*
                                                                 * @res
                                                                 * "发生并发操作，请刷新界面后再试！"
                                                                 */);
        int j = 0;
        for (int i = 0; i < nSelected.length; i++) {
          // int
          // nStatus=getBillListPanel().getHeadBillModel().getRowState(i);
          if (isNoBodys(m_VOs[nSelected[i].intValue()])) {
            ArrayList list0 = (ArrayList) list.get(j);
            GeneralHItemVO tempBodyVO[] = (GeneralHItemVO[]) list0.get(0);
            GeneralBb3VO tempBb3VO[] = (GeneralBb3VO[]) list0.get(1);
            m_VOs[nSelected[i].intValue()].setChildrenVO(tempBodyVO);
            m_VOs[nSelected[i].intValue()].setGrandVO(tempBb3VO);
            // m_bBodyQuery[i] = new UFBoolean(true);
            j++;
          }
        }
      }
      // }
    }
    catch (BusinessException e) {
      SCMEnv.out(e);
      MessageDialog.showHintDlg(this, nc.ui.ml.NCLangRes.getInstance()
          .getStrByID("40040503", "UPP40040503-000045")/* @res "并发操作" */, e
          .getMessage());
      return;
    }
    catch (Exception e) {
      SCMEnv.out(e);
      return;
    }

    java.util.ArrayList list = new java.util.ArrayList();
    for (int i = 0; i < nSelected.length; i++) {
      GeneralHVO vo = m_VOs[nSelected[i].intValue()];
      list.add(vo);
    }

    if (list.size() > 0) {
      // if(m_printList == null){
      // m_printList = new ScmPrintTool(getBillCardPanel1());
      // }
      // m_printList.setData(list);
      // m_printList.print();

      if (m_printList == null) {
        m_printList = new ScmPrintTool(null, getBillCardPanel1(), list,
            getModuleCode());
      }
      else {
        try {
          m_printList.setData(list);
        }
        catch (Exception e1) {
          SCMEnv.out(e1);
          return;
        }
      }
      try {
        m_printList.onBatchPrint(getBillListPanel(), getBillCardPanel1(), "4T");
        if (PuPubVO.getString_TrimZeroLenAsNull(m_printList.getPrintMessage()) != null) {
          MessageDialog.showHintDlg(this, nc.ui.ml.NCLangRes.getInstance()
              .getStrByID("SCMCOMMON", "UPPSCMCommon-000270")/* @res "提示" */,
              m_printList.getPrintMessage());
        }
      }
      catch (Exception e) {
        SCMEnv.out(e);
      }
    }
  }

  /**
   * 此处插入方法说明。 功能描述:修改 输入参数: 返回值: 异常处理:
   */
  public void onModify() {
    this.showHintMessage("");
    if (isSettled(m_VOs[m_nPresentRecord])) {
      MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes.getInstance()
          .getStrByID("40040503", "UPP40040503-000075")/* @res "修改期初暂估入库单" */,
          nc.ui.ml.NCLangRes.getInstance().getStrByID("40040503",
              "UPP40040503-000076")/* @res "入库单已经结算，不能修改！" */);
      return;
    }

    getBillCardPanel1().setEnabled(false);
    setPartNoEditable();

    // 入库单号不可编辑
    BillItem item = getBillCardPanel1().getHeadItem("vbillcode");
    item.setEnabled(false);

    m_bAdd = false;
    // 保存按钮状态
    for (int i = 0; i < m_nButtonState.length; i++)
      m_nRestoreState[i] = m_nButtonState[i];

    // 设置除保存，删行,放弃,打印外所有按钮为灰
    for (int i = 0; i < 16; i++) {
      m_nButtonState[i] = 1;
    }
    for (int i = 2; i < 5; i++)
      m_nButtonState[i] = 0;
    m_nButtonState[12] = 0;
    m_nButtonState[13] = 0;
    m_nUIState = 1;
    changeButtonState();
  }

  /**
   * 此处插入方法说明。 功能描述:下页 输入参数: 返回值: 异常处理:
   */
  public void onNext() {
    this.showHintMessage("");

    if (m_nPresentRecord == m_VOs.length - 2) {
      m_nButtonState[9] = 1;
      m_nButtonState[10] = 1;
    }
    m_nButtonState[7] = 0;
    m_nButtonState[8] = 0;
    //m_nPresentRecord++;
    int tempInx=m_nPresentRecord;
    while(++tempInx<m_VOs.length &&
        PuPubVO.getString_TrimZeroLenAsNull(m_VOs[tempInx].getHeadVO().getCgeneralhid())==null);
    if(tempInx<m_VOs.length)
      m_nPresentRecord=tempInx;
    else
      return;
    
    changeButtonState();
    getBillCardPanel1().getBillModel().clearBodyData();

    // 获得该张单据的表体
    // if(m_bBodyQuery != null && m_bBodyQuery.length > 0 &&
    // (!m_bBodyQuery[m_nPresentRecord].booleanValue())){
    if (isNoBodys(m_VOs[m_nPresentRecord])) {
      try {
        ArrayList list = EstimateHelper.queryInitialBody(
            m_VOs[m_nPresentRecord].getHeadVO().getCgeneralhid(),
            m_VOs[m_nPresentRecord].getHeadVO().getTs());
        if (list == null || list.size() == 0)
          throw new BusinessException(nc.ui.ml.NCLangRes.getInstance()
              .getStrByID("4004050302", "UPP4004050302-000004")/*
                                                                 * @res
                                                                 * "发生并发操作，请刷新界面后再试！"
                                                                 */);
        GeneralHItemVO tempBodyVO[] = (GeneralHItemVO[]) list.get(0);
        GeneralBb3VO tempBb3VO[] = (GeneralBb3VO[]) list.get(1);
        m_VOs[m_nPresentRecord].setChildrenVO(tempBodyVO);
        m_VOs[m_nPresentRecord].setGrandVO(tempBb3VO);
      }
      catch (BusinessException e) {
        SCMEnv.out(e);
        MessageDialog.showHintDlg(this, nc.ui.ml.NCLangRes.getInstance()
            .getStrByID("40040503", "UPP40040503-000045")/* @res "并发操作" */, e
            .getMessage());
        return;
      }
      catch (Exception e) {
        SCMEnv.out(e);
      }
      // m_bBodyQuery[m_nPresentRecord] = new UFBoolean(true);
    }

    getBillCardPanel1().setBillValueVO(m_VOs[m_nPresentRecord]);
    getBillCardPanel1().getBillModel().execLoadFormula();
    getBillCardPanel1().updateValue();
    getBillCardPanel1().updateUI();
    // 显示来源单据信息
    nc.ui.pu.pub.PuTool.loadSourceBillData(getBillCardPanel1(), "4T");

    // 显示备注
    UIRefPane nRefPanel1 = (UIRefPane) getBillCardPanel1().getHeadItem("vnote")
        .getComponent();
    nRefPanel1.setValue(m_VOs[m_nPresentRecord].getHeadVO().getVnote());

    // 显示库存组织
    setCalbodyValue();

  }

  /**
   * 此处插入方法说明。 功能描述:打印 输入参数: 返回值: 异常处理:
   */
  public void onPreview() {
    this.showHintMessage("");

    // PrintEntry print = new PrintEntry(null,null);
    // print.setTemplateID(m_sUnitCode,"4004050302",getClientEnvironment().getUser().getPrimaryKey(),null);
    // int ret = print.selectTemplate() ;
    // if(ret>0){
    // print.setDataSource(this);
    // print.preview() ;
    // }

    java.util.ArrayList list = new java.util.ArrayList();
    if (m_VOs == null || m_VOs.length == 0)// || m_bGenerate)
      return;
    list.add(m_VOs[m_nPresentRecord]);
    if (m_printCard == null) {
      m_printCard = new ScmPrintTool(this, getBillCardPanel1(), list,
          getModuleCode());
    }
    else {
      try {
        m_printCard.setData(list);
      }
      catch (Exception e1) {
        SCMEnv.out(e1);
        return;
      }
    }

    try {
      m_printCard.onCardPrintPreview(getBillCardPanel1(), getBillListPanel(),
          "4T");
      if (PuPubVO.getString_TrimZeroLenAsNull(m_printCard.getPrintMessage()) != null) {
        MessageDialog.showHintDlg(this, NCLangRes.getInstance().getStrByID(
            "SCMCOMMON", "UPPSCMCommon-000270")
        /* @res "提示" */, m_printCard.getPrintMessage());
      }
    }
    catch (Exception e) {
      SCMEnv.out(e);
    }

  }

  /**
   * 此处插入方法说明。 功能描述:上页 输入参数: 返回值: 异常处理:
   */
  public void onPrevious() {
    this.showHintMessage("");
    //getBillCardPanel1().getBillModel().clearBodyData();
    if (m_nPresentRecord == 1) {
      m_nButtonState[7] = 1;
      m_nButtonState[8] = 1;
    }
    m_nButtonState[9] = 0;
    m_nButtonState[10] = 0;
    //m_nPresentRecord--;
    int tempInx=m_nPresentRecord;
    while(--tempInx>=0 &&
        PuPubVO.getString_TrimZeroLenAsNull(m_VOs[tempInx].getHeadVO().getCgeneralhid())==null);
    if(tempInx>=0)
      m_nPresentRecord=tempInx;
    else
      return;
    changeButtonState();
    getBillCardPanel1().getBillModel().clearBodyData();


    // 获得该张单据的表体
    // if(m_bBodyQuery != null && m_bBodyQuery.length > 0 &&
    // (!m_bBodyQuery[m_nPresentRecord].booleanValue())){
    if (isNoBodys(m_VOs[m_nPresentRecord])) {
      try {
        ArrayList list = EstimateHelper.queryInitialBody(
            m_VOs[m_nPresentRecord].getHeadVO().getCgeneralhid(),
            m_VOs[m_nPresentRecord].getHeadVO().getTs());
        if (list == null || list.size() == 0)
          throw new BusinessException(nc.ui.ml.NCLangRes.getInstance()
              .getStrByID("4004050302", "UPP4004050302-000004")/*
                                                                 * @res
                                                                 * "发生并发操作，请刷新界面后再试！"
                                                                 */);
        GeneralHItemVO tempBodyVO[] = (GeneralHItemVO[]) list.get(0);
        GeneralBb3VO tempBb3VO[] = (GeneralBb3VO[]) list.get(1);
        m_VOs[m_nPresentRecord].setChildrenVO(tempBodyVO);
        m_VOs[m_nPresentRecord].setGrandVO(tempBb3VO);
      }
      catch (BusinessException e) {
        SCMEnv.out(e);
        MessageDialog.showHintDlg(this, nc.ui.ml.NCLangRes.getInstance()
            .getStrByID("40040503", "UPP40040503-000045")/* @res "并发操作" */, e
            .getMessage());
        return;
      }
      catch (Exception e) {
        SCMEnv.out(e);
      }
      // m_bBodyQuery[m_nPresentRecord] = new UFBoolean(true);
    }

    getBillCardPanel1().setBillValueVO(m_VOs[m_nPresentRecord]);
    getBillCardPanel1().getBillModel().execLoadFormula();
    // 显示来源单据信息
    nc.ui.pu.pub.PuTool.loadSourceBillData(getBillCardPanel1(), "4T");
    getBillCardPanel1().updateValue();
    getBillCardPanel1().updateUI();

    // 显示备注
    UIRefPane nRefPanel1 = (UIRefPane) getBillCardPanel1().getHeadItem("vnote")
        .getComponent();
    nRefPanel1.setValue(m_VOs[m_nPresentRecord].getHeadVO().getVnote());

    // 显示库存组织
    setCalbodyValue();

  }

  /**
   * 此处插入方法说明。 功能描述:打印 输入参数: 返回值: 异常处理:
   */
  public void onPrint() {
    this.showHintMessage("");

    // PrintEntry print = new PrintEntry(null,null);
    // print.setTemplateID(m_sUnitCode,"4004050302",getClientEnvironment().getUser().getPrimaryKey(),null);
    // //print.print();
    // int ret = print.selectTemplate() ;
    // if(ret>0){
    // print.setDataSource(this);
    // print.print() ;
    // }

    java.util.ArrayList list = new java.util.ArrayList();
    if (m_VOs == null || m_VOs.length == 0)// || m_bGenerate)
      return;
    list.add(m_VOs[m_nPresentRecord]);
    if (m_printCard == null) {
      m_printCard = new ScmPrintTool(getBillCardPanel1(), list, getModuleCode());
    }
    else {
      try {
        m_printCard.setData(list);
      }
      catch (Exception e1) {
        SCMEnv.out(e1);
        return;
      }
    }

    try {
      m_printCard.onCardPrint(getBillCardPanel1(), getBillListPanel(), "4T");
      if (PuPubVO.getString_TrimZeroLenAsNull(m_printCard.getPrintMessage()) != null) {
        MessageDialog.showHintDlg(this, NCLangRes.getInstance().getStrByID(
            "SCMCOMMON", "UPPSCMCommon-000270")/* @res "提示" */, m_printCard
            .getPrintMessage());
      }
    }
    catch (Exception e) {
      SCMEnv.out(e);
    }
    
  }

  /**
   * 功能描述:期初暂估入库单列表查询 修改：2004-09-10 袁野
   * 
   * @since v53, 2008-04-17, 支持自定义项， czp
   */
  public void onQueryStock() {
    if (!m_bICStartUp) {
      // 库存未启用
      MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes.getInstance()
          .getStrByID("40040503", "UPP40040503-000065")/* @res "期初暂估入库单查询" */,
          nc.ui.ml.NCLangRes.getInstance().getStrByID("40040503",
          "UPP40040503-000036")/* @res "库存未启用，不存在入库单！" */);
      return;
    }

    if (m_condClient1 == null) {
      m_condClient1 = initQueryModel1(m_condClient1, m_sUnitCode, false);
    }

    m_condClient1.hideNormal();
    m_condClient1.showModal();

    if (m_condClient1.isCloseOK()) {
      // 获取期初暂估入库单查询条件
      ConditionVO conditionVO[] = m_condClient1.getConditionVO();

      // 查询
      try {
        long tTime = System.currentTimeMillis();
        m_bGenerate = false;
        m_VOs = EstimateHelper.queryStockForEstimate(m_sUnitCode, conditionVO);
        if (m_VOs == null || m_VOs.length == 0) {
          // m_bBodyQuery = null;
          MessageDialog.showHintDlg(this, nc.ui.ml.NCLangRes.getInstance()
              .getStrByID("40040503", "UPP40040503-000065")/*
               * @res "期初暂估入库单查询"
               */, nc.ui.ml.NCLangRes.getInstance().getStrByID("40040503",
               "UPP40040503-000066")/*
                * @res "没有符合条件的期初暂估入库单！"
                */);

          // 清空数据
          getBillListPanel().getHeadBillModel().clearBodyData();
          getBillListPanel().getBodyBillModel().clearBodyData();
          getBillListPanel().updateUI();
          // 设置按钮状态：除查询,切换外全部为灰
          for (int i = 0; i < 8; i++) {
            m_nButtonState[i] = 1;
          }
          m_nButtonState[3] = 0;
          m_nButtonState[4] = 0;
          m_nUIState = 3;
          changeButtonState();
          return;
        }
        tTime = System.currentTimeMillis() - tTime;
        SCMEnv.out("期初暂估入库单查询时间：" + tTime + " ms!");

        // 显示查询结果
        // m_bBodyQuery = new UFBoolean[m_VOs.length];
        // for(int i = 0; i < m_VOs.length; i++){
        // if(i == 0) m_bBodyQuery[i] = new UFBoolean(true);
        // else m_bBodyQuery[i] = new UFBoolean(false);
        // }

        Vector v = new Vector();
        for (int i = 0; i < m_VOs.length; i++)
          v.addElement(m_VOs[i].getHeadVO());
        GeneralHHeaderVO head[] = new GeneralHHeaderVO[v.size()];
        v.copyInto(head);

        GeneralHItemVO body[] = m_VOs[0].getBodyVO();

        getBillListPanel().getHeadBillModel().setBodyDataVO(head);
        getBillListPanel().getHeadBillModel().execLoadFormula();
        getBillListPanel().getBodyBillModel().setBodyDataVO(body);
        getBillListPanel().getBodyBillModel().execLoadFormula();
        getBillListPanel().getHeadBillModel().updateValue();
        getBillListPanel().getBodyBillModel().updateValue();
        nc.ui.pu.pub.PuTool.loadSourceBillData(getBillListPanel(), "4T");
        getBillListPanel().updateUI();

        // 显示备注
        for (int i = 0; i < head.length; i++) {
          getBillListPanel().getHeadBillModel().setValueAt(head[i].getVnote(),
              i, "vnote");
        }       
        setListBtnsState();
//        // 设置按钮状态：除全消,作废,切换外全部为正常
//        for (int i = 0; i < 8; i++) {
//          m_nButtonState[i] = 1;
//        }
//        m_nButtonState[3] = 0;
//        m_nButtonState[4] = 0;
//        m_nButtonState[4] = 1;
//        m_nButtonState[6] = 1;
//        m_nButtonState[7] = 1;
//
//        m_nUIState = 3;
//        changeButtonState();
      }
      catch (java.sql.SQLException e) {
        MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes.getInstance()
            .getStrByID("40040503", "UPP40040503-000065")/*
             * @res "期初暂估入库单查询"
             */, nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON",
             "UPPSCMCommon-000412")/*
              * @res "SQL语句错误！"
              */);
        SCMEnv.out(e);
        return;
      }
      catch (ArrayIndexOutOfBoundsException e) {
        MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes.getInstance()
            .getStrByID("40040503", "UPP40040503-000065")/*
             * @res "期初暂估入库单查询"
             */, nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON",
             "UPPSCMCommon-000426")/*
              * @res "数组越界错误！"
              */);
        SCMEnv.out(e);
        return;
      }
      catch (NullPointerException e) {
        MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes.getInstance()
            .getStrByID("40040503", "UPP40040503-000065")/*
             * @res "期初暂估入库单查询"
             */, nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON",
             "UPPSCMCommon-000427")/*
              * @res "空指针错误！"
              */);
        SCMEnv.out(e);
        return;
      }
      catch (Exception e) {
        MessageDialog.showErrorDlg(this,
            nc.ui.ml.NCLangRes.getInstance().getStrByID("40040503",
            "UPP40040503-000065")/* @res "期初暂估入库单查询" */, e.getMessage());
        SCMEnv.out(e);
        return;
      }
    }
  }
  
  /**
   * 此处插入方法说明。 功能描述:订单查询 输入参数: 返回值: 异常处理:

   */
  public void onQueryOrder() {
    if (!m_bICStartUp) {
      // 库存未启用
      MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes.getInstance()
          .getStrByID("40040503", "UPP40040503-000077")/*
                                                         * @res "订单查询"
                                                         */, nc.ui.ml.NCLangRes.getInstance().getStrByID("40040503",
          "UPP40040503-000078")/*
                                 * @res "库存未启用，不能通过采购订单生成期初暂估入库单！"
                                 */);
      return;
    }

    initQueryModel2();

    UIRefPane biztypeRef = new UIRefPane();
    biztypeRef.setIsCustomDefined(true);
    biztypeRef.setRefModel(new nc.ui.ps.pub.SpeBiztypeDef(m_sBiztypeID));
    m_condClient2.setValueRef("cbiztype", biztypeRef);

    m_condClient2.hideNormal();
    m_condClient2.hideCorp();
    m_condClient2.showModal();

    if (m_condClient2.isCloseOK()) {
      // 获取订单查询条件
      ConditionVO conditionVO[] = m_condClient2.getConditionVO();

      // 查询
      try {
        long tTime = System.currentTimeMillis();
        m_orderVOs = EstimateHelper.queryOrder(m_sUnitCode, conditionVO,
            m_sBiztypeID, m_sEstPriceSource);
        if (m_orderVOs == null || m_orderVOs.length == 0) {
          MessageDialog.showHintDlg(this, nc.ui.ml.NCLangRes.getInstance()
              .getStrByID("40040503", "UPP40040503-000077")/*
                                                             * @res "订单查询"
                                                             */, nc.ui.ml.NCLangRes.getInstance().getStrByID("40040503",
              "UPP40040503-000079")/*
                                     * @res "没有符合条件的订单！"
                                     */);

          // 清空数据
          getBillCardPanel2().getBillModel().clearBodyData();
          getBillCardPanel2().updateUI();
          // 设置除查询,返回外的所有按钮为灰
          for (int i = 0; i < 6; i++) {
            m_nButtonState[i] = 1;
          }
          m_nButtonState[4] = 0;
          m_nButtonState[5] = 0;
          m_nUIState = 2;
          changeButtonState();
          return;
        }
        tTime = System.currentTimeMillis() - tTime;
        SCMEnv.out("订单查询时间：" + tTime + " ms!");

        // 如果是分收集结，则将收货公司设置为登录公司,since v502
        for (OorderVO curVo : m_orderVOs) {
          if (EstimateVO.PURCHASE.equals(EstimateVO.getEstType(curVo
              .getPk_corp(), curVo.getPk_arrvcorp(), curVo.getPk_invoicecorp(),
              m_sUnitCode))) {
            curVo.setPk_arrvcorp(m_sUnitCode);
          }
        }

        // 显示查询结果
        getBillCardPanel2().getBillData().setBodyValueVO(m_orderVOs);
        getBillCardPanel2().getBillModel().execLoadFormula();
        getBillCardPanel2().updateValue();
        getBillCardPanel2().updateUI();

        // zhf 设置折本汇率
        setBodyDigits();

        // setAddPartNoEditable();
        getBillCardPanel2().setEnabled(true);

        // 币种为本币折本汇率不可修改 zhf add
        int rows = getBillCardPanel2().getBillModel().getRowCount();
        for (int i = 0; i < rows; i++) {
          Object curTypeId = getBillCardPanel2().getBodyValueAt(i,
              "currencytypeid");
          if (curTypeId == null)
            continue;

          if (curTypeId.toString().equalsIgnoreCase(m_sCurrTypeID))// 判断当前币种是否公司本位币
            // 设置折本汇率编辑属性
            getBillCardPanel2().getBillModel().setCellEditable(i,
                "nexchangeotobrate", false);
          else
            getBillCardPanel2().getBillModel().setCellEditable(i,
                "nexchangeotobrate", true);
        }
        // zhf end

        // 设置按钮状态：除全消,暂估外其它为正常
        for (int i = 0; i < 6; i++) {
          m_nButtonState[i] = 0;
        }
        m_nButtonState[1] = 1;
        m_nButtonState[3] = 1;
        m_nUIState = 2;
        changeButtonState();
      }
      catch (java.sql.SQLException e) {
        MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes.getInstance()
            .getStrByID("40040503", "UPP40040503-000077")/*
                                                           * @res "订单查询"
                                                           */, nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON",
            "UPPSCMCommon-000412")/*
                                   * @res "SQL语句错误！"
                                   */);
        SCMEnv.out(e);
        return;
      }
      catch (ArrayIndexOutOfBoundsException e) {
        MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes.getInstance()
            .getStrByID("40040503", "UPP40040503-000077")/*
                                                           * @res "订单查询"
                                                           */, nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON",
            "UPPSCMCommon-000426")/*
                                   * @res "数组越界错误！"
                                   */);
        SCMEnv.out(e);
        return;
      }
      catch (NullPointerException e) {
        MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes.getInstance()
            .getStrByID("40040503", "UPP40040503-000077")/*
                                                           * @res "订单查询"
                                                           */, nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON",
            "UPPSCMCommon-000427")/*
                                   * @res "空指针错误！"
                                   */);
        SCMEnv.out(e);
        return;
      }
      catch (Exception e) {
        MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes.getInstance()
            .getStrByID("40040503", "UPP40040503-000077")/* @res "订单查询" */, e
            .getMessage());
        SCMEnv.out(e);
        return;
      }
    }
  }
  
  
  /**
   * 此处插入方法说明。 功能描述:订单查询 输入参数: 返回值: 异常处理:
   * modify by zhaoyha at 2008.10.15 使用新查询模板
   */
  public void onQueryOrderForV55() {
    if (!m_bICStartUp) {
      // 库存未启用
      MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes.getInstance()
          .getStrByID("40040503", "UPP40040503-000077")/*
           * @res "订单查询"
           */, nc.ui.ml.NCLangRes.getInstance().getStrByID("40040503",
           "UPP40040503-000078")/*
            * @res "库存未启用，不能通过采购订单生成期初暂估入库单！"
            */);
      return;
    }

    getNewQueryDlg().showModal();
    if(getNewQueryDlg().isCloseOK()){

      // 查询
      try {
        //业务类型权限控制
        String sInit = " and po_order.cbiztype in (";
        String s=sInit;
        for (int i = 0; i < m_sBiztypeID.length; i++) {
          if (i != m_sBiztypeID.length - 1)
            s += "'" + m_sBiztypeID[i] + "',";
          else
            s += "'" + m_sBiztypeID[i] + "')";
        }
        //得到查询对话框中的条件
        String wherePart=getNewQueryDlg().getQueryWhereSql();
        if(!sInit.equals(s)) wherePart+=s;
        long tTime = System.currentTimeMillis();

        m_orderVOs = EstimateHelper.queryOrder(m_sUnitCode, wherePart);
        if (m_orderVOs == null || m_orderVOs.length == 0) {
          MessageDialog.showHintDlg(this, nc.ui.ml.NCLangRes.getInstance()
              .getStrByID("40040503", "UPP40040503-000077")/*
               * @res "订单查询"
               */, nc.ui.ml.NCLangRes.getInstance().getStrByID("40040503",
               "UPP40040503-000079")/*
                * @res "没有符合条件的订单！"
                */);

          // 清空数据
          getBillCardPanel2().getBillModel().clearBodyData();
          getBillCardPanel2().updateUI();
          // 设置除查询,返回外的所有按钮为灰
          for (int i = 0; i < 6; i++) {
            m_nButtonState[i] = 1;
          }
          m_nButtonState[4] = 0;
          m_nButtonState[5] = 0;
          m_nUIState = 2;
          changeButtonState();
          return;
        }
        tTime = System.currentTimeMillis() - tTime;
        SCMEnv.out("订单查询时间：" + tTime + " ms!");

        // 如果是分收集结，则将收货公司设置为登录公司,since v502
        for (OorderVO curVo : m_orderVOs) {
          if (EstimateVO.PURCHASE.equals(EstimateVO.getEstType(curVo
              .getPk_corp(), curVo.getPk_arrvcorp(), curVo.getPk_invoicecorp(),
              m_sUnitCode))) {
            curVo.setPk_arrvcorp(m_sUnitCode);
          }
        }

        // 显示查询结果
        getBillCardPanel2().getBillData().setBodyValueVO(m_orderVOs);
        getBillCardPanel2().getBillModel().execLoadFormula();
        getBillCardPanel2().updateValue();
        getBillCardPanel2().updateUI();

        // zhf 设置折本汇率
        setBodyDigits();

        // setAddPartNoEditable();
        getBillCardPanel2().setEnabled(true);

        // 币种为本币折本汇率不可修改 zhf add
        int rows = getBillCardPanel2().getBillModel().getRowCount();
        for (int i = 0; i < rows; i++) {
          Object curTypeId = getBillCardPanel2().getBodyValueAt(i,
              "currencytypeid");
          if (curTypeId == null)
            continue;

          if (curTypeId.toString().equalsIgnoreCase(m_sCurrTypeID))// 判断当前币种是否公司本位币
            // 设置折本汇率编辑属性
            getBillCardPanel2().getBillModel().setCellEditable(i,
                "nexchangeotobrate", false);
          else
            getBillCardPanel2().getBillModel().setCellEditable(i,
                "nexchangeotobrate", true);
        }
        // zhf end

        // 设置按钮状态：除全消,暂估外其它为正常
        for (int i = 0; i < 6; i++) {
          m_nButtonState[i] = 0;
        }
        m_nButtonState[1] = 1;
        m_nButtonState[3] = 1;
        m_nUIState = 2;
        changeButtonState();
      }
      catch (ArrayIndexOutOfBoundsException e) {
        MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes.getInstance()
            .getStrByID("40040503", "UPP40040503-000077")/*
             * @res "订单查询"
             */, nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON",
             "UPPSCMCommon-000426")/*
              * @res "数组越界错误！"
              */);
        SCMEnv.out(e);
        return;
      }
      catch (NullPointerException e) {
        MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes.getInstance()
            .getStrByID("40040503", "UPP40040503-000077")/*
             * @res "订单查询"
             */, nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON",
             "UPPSCMCommon-000427")/*
              * @res "空指针错误！"
              */);
        SCMEnv.out(e);
        return;
      }
      catch (Exception e) {
        MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes.getInstance()
            .getStrByID("40040503", "UPP40040503-000077")/* @res "订单查询" */, e
            .getMessage());
        SCMEnv.out(e);
        return;
      }
    }
  }

  /**
   * 功能描述:保存
   */
  public boolean onSave() {
    getBillCardPanel1().stopEditing();

    // updateOorderVO();// zhf add

    ArrayList lModify = null;

    int nRow = getBillCardPanel1().getRowCount();
    GeneralHVO VO = new GeneralHVO(nRow);
    getBillCardPanel1().getBillValueVO(VO);

    GeneralHHeaderVO hheadVO = VO.getHeadVO();

    GeneralHItemVO bbodyVO[] = VO.getBodyVO();
    if (bbodyVO == null || bbodyVO.length == 0) {
      MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes.getInstance()
          .getStrByID("40040503", "UPP40040503-000082")/* @res "合法性检查" */,
          nc.ui.ml.NCLangRes.getInstance().getStrByID("40040503",
              "UPP40040503-000093")/* @res"表体不能为空！" */);
      return false;
    }
    //
    GeneralHItemVO tempVO[] = m_VOs[m_nPresentRecord].getBodyVO();
    for (int i = 0; i < bbodyVO.length; i++) {
      // 单据模板中没有ccfirsttype,cfirstbillhid,cfirstbillbid
      // 因此保存时需要设置上述3个字段的值及表头,表体自定义项的值
      bbodyVO[i].setCfirsttype(tempVO[i].getCfirsttype());
      bbodyVO[i].setVfirstbillcode(tempVO[i].getVsourcebillcode());// since
      // v53,
      // 向收付传递
      bbodyVO[i].setCfirstbillhid(tempVO[i].getCfirstbillhid());
      bbodyVO[i].setCfirstbillbid(tempVO[i].getCfirstbillbid());
      // since v502
      bbodyVO[i].setBLargess(tempVO[i].getBLargess());
      bbodyVO[i].setCvendorid(hheadVO.getCproviderid());
    }
    // 合法性检查
    int nError = -1;
    try {
      VO.getHeadVO().validate();
      GeneralHItemVO bodyVO[] = VO.getBodyVO();
      for (nError = 0; nError < bodyVO.length; nError++)
        bodyVO[nError].validate();
    }
    catch (ValidationException e) {
      String sError = "";
      if (nError == -1) {
        sError = e.getMessage();
      }
      else {
        String[] value = new String[] {
          String.valueOf(nError + 1)
        };
        sError = nc.ui.ml.NCLangRes.getInstance().getStrByID("40040503",
            "UPP40040503-000081", null, value)/*
                                               * @res "表体行" +
                                               * CommonConstant.BEGIN_MARK +
                                               * (nError + 1) +
                                               * CommonConstant.END_MARK +
                                               * "下列字段不能为空：\n"
                                               */
            + e.getMessage();
      }
      MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes.getInstance()
          .getStrByID("40040503", "UPP40040503-000082")/* @res "合法性检查" */,
          sError);
      return false;
    }
    catch (Exception e) {
      SCMEnv.out(e);
      MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes.getInstance()
          .getStrByID("40040503", "UPP40040503-000082")/* @res "合法性检查" */, e
          .getMessage());
      return false;
    }

    // 行号检查
    if (!nc.ui.scm.pub.report.BillRowNo.verifyRowNosCorrect(
        getBillCardPanel1(), "crowno"))
      return false;

    // 自由项、批次号检查
    String sError = checkFreeItem() + checkBatchCode();
    if (sError != null && sError.trim().length() > 0) {
      MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes.getInstance()
          .getStrByID("40040503", "UPP40040503-000082")/* @res "合法性检查" */,
          sError);
      return false;
    }

//    if (checkBeforeSave(VO, m_bAdd)) {
//      MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes.getInstance()
//          .getStrByID("40040503", "UPP40040503-000082")/* @res "合法性检查" */,
//          nc.ui.ml.NCLangRes.getInstance().getStrByID("40040503",
//              "UPP40040503-000083")/* @res "入库单号重复，不能保存！" */);
//      return false;
//    }

    if (!m_bAdd) {
      // 非增加入库单
      GeneralHHeaderVO headVO = VO.getHeadVO();
      // 表头设置
      headVO.setPk_corp(m_sUnitCode);
      headVO.setFbillflag(m_VOs[m_nPresentRecord].getHeadVO().getFbillflag());
      headVO.setPrimaryKey((String) m_VOs[m_nPresentRecord].getHeadVO()
          .getAttributeValue("cgeneralhid"));
      headVO.setHeadEditStatus(2);

      UIRefPane nRefPanel = (UIRefPane) getBillCardPanel1()
          .getHeadItem("vnote").getComponent();
      UITextField vMemoField = nRefPanel.getUITextField();
      headVO.setVnote(vMemoField.getText());

      GeneralHItemVO bodyVO[] = VO.getBodyVO();
      for (int i = 0; i < bodyVO.length; i++) {
        bodyVO[i].setBodyEditStatus(getBillCardPanel1().getBillData()
            .getBillModel().getRowState(i));
        // bodyVO[i].setDzgdate(m_dSystem);

        bodyVO[i].setDbizdate(headVO.getDbilldate());
        bodyVO[i].setDzgdate(headVO.getDbilldate());

        bodyVO[i].setPk_corp(m_sUnitCode);
      }

      // 复制表体及子表
      Vector vData = new Vector();
      // Vector vDataData = new Vector();//
      for (int i = 0; i < bodyVO.length; i++) {
        vData.addElement(bodyVO[i]);
        // vDataData.addElement(bb3VO[i]);//
      }

      // 复制删除的表体
      Vector vDelData = getBillCardPanel1().getBillModel().getDeleteRow();
      GeneralHItemVO bVO[] = m_VOs[m_nPresentRecord].getBodyVO();
      if (vDelData != null) {
        for (int i = 0; i < vDelData.size(); i++) {
          Vector vTemp = (Vector) vDelData.elementAt(i);
          GeneralHItemVO bvoTemp = new GeneralHItemVO();
          bvoTemp.setCgeneralbid((String) vTemp.elementAt(1));
          for (int j = 0; j < bVO.length; j++) {
            if (bvoTemp.getCgeneralbid().equals(bVO[j].getCgeneralbid())) {
              bvoTemp = (GeneralHItemVO) bVO[j].clone();
              vData.addElement(bvoTemp);
              break;
            }
          }
        }
      }

      GeneralHItemVO v[] = new GeneralHItemVO[vData.size()];
      vData.copyInto(v);

      Vector vTemp = new Vector();
      GeneralBb3VO bb3VOs[] = m_VOs[m_nPresentRecord].getGrandVO();
      for (int i = 0; i < v.length; i++) {
        String s1 = v[i].getCgeneralbid().trim();
        for (int j = 0; j < bb3VOs.length; j++) {
          String s2 = bb3VOs[j].getCgeneralbid().trim();
          if (s1.equals(s2)) {
            bb3VOs[j].setNpprice(v[i].getNprice());
            bb3VOs[j].setNpmoney(v[i].getNmny());
            vTemp.addElement(bb3VOs[j]);
            break;
          }
        }
      }
      GeneralBb3VO vv[] = new GeneralBb3VO[vTemp.size()];
      vTemp.copyInto(vv);

      for (int i = vData.size() - 1; i >= nRow; i--) {
        // 设置删除状态
        v[i].setBodyEditStatus(3);
      }
      // 包括删除的所有表体设置为当前的表体
      VO.setChildrenVO(v);
      VO.setGrandVO(vv);

      // 期初暂估入库单修改时，获得入库单行修改的暂估数量的修改值=修改后的值-修改前的值
      lModify = getModifiedOrderStockNum();

      // 设置dr及ts
      VO.getHeadVO().setDr(m_VOs[m_nPresentRecord].getHeadVO().getDr());
      VO.getHeadVO().setTs(m_VOs[m_nPresentRecord].getHeadVO().getTs());
      GeneralHItemVO temp1VO[] = VO.getBodyVO();
      GeneralBb3VO bb31VO[] = VO.getGrandVO();
      GeneralHItemVO temp2VO[] = m_VOs[m_nPresentRecord].getBodyVO();
      GeneralBb3VO bb32VO[] = m_VOs[m_nPresentRecord].getGrandVO();
      for (int i = 0; i < temp1VO.length; i++) {
        String s1 = temp1VO[i].getCgeneralbid();
        if (s1 == null || s1.trim().length() == 0)
          continue;
        for (int j = 0; j < temp2VO.length; j++) {
          String s2 = temp2VO[j].getCgeneralbid().trim();
          if (s1.equals(s2)) {
            temp1VO[i].setDr(temp2VO[j].getDr());
            temp1VO[i].setTs(temp2VO[j].getTs());
            bb31VO[i].setDr(bb32VO[j].getDr());
            bb31VO[i].setTs(bb32VO[j].getTs());
            break;
          }
        }
      }
    }
    else {
      // 增加入库单
      GeneralHHeaderVO headVO = VO.getHeadVO();
      headVO.setFbillflag(new Integer(3));

      GeneralHItemVO bodyVO[] = VO.getBodyVO();
      if (bodyVO != null && bodyVO.length > 0) {
        for (int i = 0; i < bodyVO.length; i++) {
          // bodyVO[i].setDzgdate(m_dSystem);
          bodyVO[i].setDbizdate(headVO.getDbilldate());
          bodyVO[i].setDzgdate(headVO.getDbilldate());
          bodyVO[i].setPk_corp(headVO.getPk_corp());
        }
      }

      try {
        if (headVO.getCoperatorid() == null)
          headVO.setCoperatorid(EstimateHelper
              .getRefOperatorKey(getClientEnvironment().getUser()
                  .getPrimaryKey()));
        if (headVO.getCdptid() == null)
          headVO.setCdptid(EstimateHelper
              .getRefDeptKey(headVO.getCoperatorid()));
      }
      catch (Exception e) {
        SCMEnv.out(e);
        MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes.getInstance()
            .getStrByID("SCMCOMMON", "UPPSCMCommon-000355")/* @res "表头设置" */,
            e.getMessage());
        return false;
      }

      UIRefPane nRefPanel = (UIRefPane) getBillCardPanel1()
          .getHeadItem("vnote").getComponent();
      UITextField vMemoField = nRefPanel.getUITextField();
      headVO.setVnote(vMemoField.getText());

      // 订单生成期初暂估入库单时，获得入库单行的暂估数量,修改订单的累计入库数量
      lModify = getModifiedOrderStockNum();
    }

    boolean bContinue = true;
    while (bContinue) {
      try {
        // 保存
        long tTime = System.currentTimeMillis();
        // 获得入库单VO的来源订单VO
        Vector vv = new Vector();
        GeneralHItemVO itemVO[] = VO.getBodyVO();
        if (m_orderVOs != null && m_orderVOs.length > 0) {
          for (int i = 0; i < m_orderVOs.length; i++) {
            String s1 = m_orderVOs[i].getCorder_bid().trim();
            for (int j = 0; j < itemVO.length; j++) {
              String s2 = itemVO[j].getCsourcebillbid().trim();
              if (s1.equals(s2)) {
                vv.addElement(m_orderVOs[i]);
                break;
              }
            }
          }
        }
        OorderVO orderVOs[] = null;
        if (vv.size() > 0) {
          orderVOs = new OorderVO[vv.size()];
          vv.copyInto(orderVOs);
        }
        //

        lModify.add(getCorpPrimaryKey());

        ArrayList list = EstimateHelper.doSave(VO, m_bAdd, lModify,
            getClientEnvironment().getUser().getPrimaryKey(), orderVOs);

        ArrayList list1 = null;
        ArrayList list2 = null;
        if (list != null && list.size() > 0) {
          list1 = (ArrayList) list.get(0);
          list2 = (ArrayList) list.get(1);
        }

        tTime = System.currentTimeMillis() - tTime;
        SCMEnv.out("保存时间：" + tTime + " ms!");

        // 保存成功，重新设置表体（去掉删除的表体）
        GeneralHItemVO bodyVO[] = VO.getBodyVO();
        GeneralBb3VO bb3VO[] = VO.getGrandVO();
        Vector temp = new Vector();
        Vector tempp = new Vector();
        for (int i = 0; i < bodyVO.length; i++) {
          if (bodyVO[i].getBodyEditStatus() != 3) {
            temp.addElement(bodyVO[i]);
            tempp.addElement(bb3VO[i]);
          }
        }
        GeneralHItemVO tempBVO[] = new GeneralHItemVO[temp.size()];
        temp.copyInto(tempBVO);
        VO.setChildrenVO(tempBVO);
        GeneralBb3VO tempBb3VO[] = new GeneralBb3VO[tempp.size()];
        tempp.copyInto(tempBb3VO);
        VO.setGrandVO(tempBb3VO);

        // 设置除保存,取消,删行外所有按钮为正常
        getBillCardPanel1().setEnabled(false);
        for (int i = 0; i < 16; i++) {
          m_nButtonState[i] = 0;
        }
        for (int i = 2; i < 5; i++)
          m_nButtonState[i] = 1;
        // 若处于增加，则作废为灰
        if (m_bGenerate)
          m_nButtonState[5] = 1;

        // 设置新增单据的子表和子子表查询为TRUE
        // if (m_bAdd) {
        // Vector vTemp1 = new Vector();
        // if (m_bBodyQuery != null && m_bBodyQuery.length > 0) {
        // for (int i = 0; i < m_bBodyQuery.length; i++)
        // vTemp1.addElement(m_bBodyQuery[i]);
        // }
        // vTemp1.addElement(new UFBoolean(true));
        // m_bBodyQuery = new UFBoolean[vTemp1.size()];
        // vTemp1.copyInto(m_bBodyQuery);
        // }

        // 增加的表头及增行表体设置主键
        if (m_bAdd) {
          if (list1 != null && list1.size() > 0) {
            // 新增的入库单行获得主键值
            VO.getHeadVO().setAttributeValue("cgeneralhid", list1.get(0));
            int j = 1;
            for (int i = 0; i < nRow; i++) {
              tempBVO[i].setAttributeValue("cgeneralhid", list1.get(0));
              tempBVO[i].setAttributeValue("cgeneralbid", list1.get(j));
              tempBb3VO[i].setAttributeValue("cgeneralhid", list1.get(0));
              tempBb3VO[i].setAttributeValue("cgeneralbid", list1.get(j));
              tempBb3VO[i].setAttributeValue("cgeneralbb3", list1.get(j+nRow));
              j++;
            }

            // 新增的入库单获得单据号
            VO.getHeadVO().setAttributeValue("vbillcode",
                list1.get(list1.size() - 1));
            //
          }

          if (list2 != null && list2.size() > 0) {
            // 新增的入库单头及新增的入库单体及子表获得时间戳
            VO.getHeadVO().setTs((String) list2.get(0));
            int j = 1;
            for (int i = 0; i < nRow; i++) {
              tempBVO[i].setTs((String) list2.get(j));
              j++;
              tempBb3VO[i].setTs((String) list2.get(j));
              j++;
            }
          }
          //

          Vector vTemp = new Vector();
          if (m_VOs != null) {
            // 已经存在入库单
            if (!m_bGenerate) {
              for (int i = 0; i < m_VOs.length; i++) {
                vTemp.addElement(m_VOs[i]);
              }
              vTemp.addElement(VO);
              m_VOs = new GeneralHVO[vTemp.size()];
              vTemp.copyInto(m_VOs);
              m_nPresentRecord = m_VOs.length - 1;
            }
            else
              m_VOs[m_nPresentRecord] = VO;
          }
          else {
            // 不存在入库单
            m_VOs = new GeneralHVO[1];
            m_VOs[0] = VO;
            m_nPresentRecord = 0;
          }
        }
        else {
          // 新增的入库单行获得主键值
          if (list1 != null && list1.size() > 0) {
            int j = 1;
            for (int i = 0; i < nRow; i++) {
              if (tempBVO[i].getBodyEditStatus() == 1) {
                tempBVO[i].setAttributeValue("cgeneralhid", list1.get(0));
                tempBVO[i].setAttributeValue("cgeneralbid", list1.get(j));
                tempBb3VO[i].setAttributeValue("cgeneralhid", list1.get(0));
                tempBb3VO[i].setAttributeValue("cgeneralbid", list1.get(j));
                j++;
                tempBVO[i].setBodyEditStatus(0);
              }
            }
          }

          // 入库单头及更新的入库单体及子表获得时间戳
          if (list2 != null && list2.size() > 0) {
            VO.getHeadVO().setTs((String) list2.get(0));
            int j = 1;
            for (int i = 0; i < nRow; i++) {
              if (tempBVO[i].getBodyEditStatus() == 2) {
                tempBVO[i].setTs((String) list2.get(j));
                j++;
                tempBb3VO[i].setTs((String) list2.get(j));
                j++;
                tempBVO[i].setBodyEditStatus(0);
              }
            }
          }
          //

          m_VOs[m_nPresentRecord] = VO;
        }

        // 单据重新装载数据，并更新界面
        getBillCardPanel1().setBillValueVO(m_VOs[m_nPresentRecord]);
        getBillCardPanel1().getBillModel().execLoadFormula();
        // 显示来源单据信息
        nc.ui.pu.pub.PuTool.loadSourceBillData(getBillCardPanel1(), "4T");
        getBillCardPanel1().updateValue();
        if (!m_bAdd) {
        }

        this.showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
            "40040503", "UPP40040503-000084")/*
                                               * @res "保存成功！"
                                               */);
        if (m_nPresentRecord == 0) {
          m_nButtonState[7] = 1;
          m_nButtonState[8] = 1;
        }
        if (m_nPresentRecord == m_VOs.length - 1) {
          m_nButtonState[9] = 1;
          m_nButtonState[10] = 1;
        }
        if (m_VOs.length == 1) {
          for (int i = 7; i < 11; i++)
            m_nButtonState[i] = 1;
        }
        // 暂估应付时,不可以修改，也就不可以行操作
        if (m_bZGYF.booleanValue()) {
          m_nButtonState[1] = 1;
        }
        m_nUIState = 1;
        changeButtonState();

        // 显示备注
        UIRefPane nRefPanel = (UIRefPane) getBillCardPanel1().getHeadItem(
            "vnote").getComponent();
        nRefPanel.setValue(m_VOs[m_nPresentRecord].getHeadVO().getVnote());

        // 显示列表界面且保存后的期初暂估入库单不再显示在界面
        if (m_bGenerate) {
          doRefresh(m_nPresentRecord);
        }

        bContinue = false;
      }
      catch (java.sql.SQLException e) {
        MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes.getInstance()
            .getStrByID("40040503", "UPP40040503-000085")/* @res "入库单保存" */, e
            .getMessage());
        SCMEnv.out(e);
        return false;
      }
      catch (ArrayIndexOutOfBoundsException e) {
        MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes.getInstance()
            .getStrByID("40040503", "UPP40040503-000085")/*
                                                           * @res "入库单保存"
                                                           */, nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON",
            "UPPSCMCommon-000426")/*
                                   * @res "数组越界错误！"
                                   */);
        SCMEnv.out(e);
        return false;
      }
      catch (NullPointerException e) {
        MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes.getInstance()
            .getStrByID("40040503", "UPP40040503-000085")/*
                                                           * @res "入库单保存"
                                                           */, nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON",
            "UPPSCMCommon-000427")/*
                                   * @res "空指针错误！"
                                   */);
        SCMEnv.out(e);
        return false;
      }
      // since v502, 支持提示处理
      catch (RwtIcToPoException e) {
        // 入库单累计数量超出提示
        int iRet = showYesNoMessage(e.getMessage());
        if (iRet == MessageDialog.ID_YES) {
          // 继续循环
          bContinue = true;
          // 用户确认过，可以保存
          VO.setUserConfirmed(true);
        }
        else {
          return false;
        }
      }
      catch (nc.vo.pub.BusinessException e) {
        MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes.getInstance()
            .getStrByID("40040503", "UPP40040503-000085")/* @res "入库单保存" */, e
            .getMessage());
        SCMEnv.out(e);
        return false;
      }
      catch (Exception e) {
        MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes.getInstance()
            .getStrByID("40040503", "UPP40040503-000085")/* @res "入库单保存" */, e
            .getMessage());
        SCMEnv.out(e);
        return false;
      }
    }
    //置是否处理中标志
    m_bSettling = false;
    return true;
  }

  /**
   * 此处插入方法说明。 功能描述:全选 输入参数: 返回值: 异常处理:
   */
  public void onSelectAll() {
    if (m_nUIState == 2) {
      // 显示订单查询列表
      int nRow = getBillCardPanel2().getBillModel().getRowCount();
      //
      for (int i = 0; i < nRow; i++)
        getBillCardPanel2().getBillModel().setRowState(i, BillModel.SELECTED);

      getBillCardPanel2().updateUI();
      // 除全选外其他按钮正常
      for (int i = 0; i < 5; i++)
        m_nButtonState[i] = 0;
      m_nButtonState[0] = 1;
      m_nUIState = 2;
      changeButtonState();
    }

    if (m_nUIState == 3) {
      // 显示入库单列表
      int nRow = getBillListPanel().getHeadBillModel().getRowCount();
      //
      getBillListPanel().getHeadTable().setRowSelectionInterval(0, nRow - 1);
      //
      for (int i = 0; i < nRow; i++) {
        getBillListPanel().getHeadBillModel()
            .setRowState(i, BillModel.SELECTED);
      }
      getBillListPanel().updateUI();
      if (!m_bAdd) {
        // 非订单生成采购入库单，除全选,切换外其他按钮正常
        for (int i = 0; i < 8; i++)
          m_nButtonState[i] = 0;
        m_nButtonState[0] = 1;
      }
      else {
        // 订单生成采购入库单，除全消，打印外其他为灰
        for (int i = 0; i < 8; i++)
          m_nButtonState[i] = 1;
        m_nButtonState[1] = 0;
      }
      if (nRow != 1)
        m_nButtonState[4] = 1;
      else
        m_nButtonState[4] = 0;
      m_nUIState = 3;
      changeButtonState();
    }
  }

  /**
   * 此处插入方法说明。 功能描述:选择分单方式 输入参数: 返回值: 异常处理:
   */
  public void onSelectMode() {
    if (m_dialog == null) {
      m_dialog = new UIDialog(this);
      m_dialog.setTitle(nc.ui.ml.NCLangRes.getInstance().getStrByID(
          "4004050302", "UPP4004050302-000012")/* @res "选择分单方式" */);

      m_rb1 = new UIRadioButton();
      m_rb1.setEnabled(true);
      UILabel label1 = new UILabel(nc.ui.ml.NCLangRes.getInstance().getStrByID(
          "4004050302", "UPP4004050302-000013")/*
                                                 * @res "按供应商"
                                                 */);
      m_rb1.setBounds(80, 10, 16, 16);
      label1.setBounds(96, 7, 100, 25);

      m_rb2 = new UIRadioButton();
      m_rb2.setEnabled(true);
      UILabel label2 = new UILabel(nc.ui.ml.NCLangRes.getInstance().getStrByID(
          "4004050302", "UPP4004050302-000014")/*
                                                 * @res "按存货+供应商"
                                                 */);
      m_rb2.setBounds(80, 45, 16, 16);
      label2.setBounds(96, 42, 100, 25);

      m_rb3 = new UIRadioButton();
      m_rb3.setEnabled(true);
      UILabel label3 = new UILabel(nc.ui.ml.NCLangRes.getInstance().getStrByID(
          "4004050302", "UPP4004050302-000015")/*
                                                 * @res "按订单"
                                                 */);
      m_rb3.setBounds(80, 80, 16, 16);
      label3.setBounds(96, 77, 100, 25);

      m_rb4 = new UIRadioButton();
      m_rb4.setEnabled(true);
      UILabel label4 = new UILabel(nc.ui.ml.NCLangRes.getInstance().getStrByID(
          "4004050302", "UPP4004050302-000016")/*
                                                 * @res "按仓库+供应商"
                                                 */);
      m_rb4.setBounds(80, 115, 16, 16);
      label4.setBounds(96, 112, 100, 25);

      m_rb5 = new UIRadioButton();
      m_rb5.setEnabled(true);
      UILabel label5 = new UILabel(nc.ui.ml.NCLangRes.getInstance().getStrByID(
          "4004050302", "UPP4004050302-000017")/*
                                                 * @res "按订单+仓库"
                                                 */);
      m_rb5.setBounds(80, 150, 16, 16);
      label5.setBounds(96, 147, 100, 25);

      // 设置初始值
      switch (m_nMode) {
        case 0:
          m_rb1.setSelected(true);
        case 1:
          m_rb2.setSelected(true);
        case 2:
          m_rb3.setSelected(true);
        case 3:
          m_rb4.setSelected(true);
        case 4:
          m_rb5.setSelected(true);
      }

      javax.swing.ButtonGroup group = new javax.swing.ButtonGroup();
      group.add(m_rb1);
      group.add(m_rb2);
      group.add(m_rb3);
      group.add(m_rb4);
      group.add(m_rb5);

      m_bnOK = new UIButton();
      m_bnOK.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
          "UC001-0000044")/* @res "确定" */);
      m_bnOK.addActionListener(this);
      m_bnCancel = new UIButton();
      m_bnCancel.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
          "UC001-0000008")/* @res "取消" */);
      m_bnCancel.addActionListener(this);
      m_bnOK.setBounds(100, 180, 70, 22);
      m_bnCancel.setBounds(250, 180, 70, 22);

      m_dialog.getContentPane().setLayout(null);
      m_dialog.getContentPane().add(m_rb1);
      m_dialog.getContentPane().add(m_rb2);
      m_dialog.getContentPane().add(m_rb3);
      m_dialog.getContentPane().add(m_rb4);
      m_dialog.getContentPane().add(m_rb5);
      m_dialog.getContentPane().add(label1);
      m_dialog.getContentPane().add(label2);
      m_dialog.getContentPane().add(label3);
      m_dialog.getContentPane().add(label4);
      m_dialog.getContentPane().add(label5);
      m_dialog.getContentPane().add(m_bnOK);
      m_dialog.getContentPane().add(m_bnCancel);
    }

    m_dialog.showModal();
  }

  /**
   * 此处插入方法说明。 功能描述:全消 输入参数: 返回值: 异常处理:
   */
  public void onSelectNo() {
    if (m_nUIState == 2) {
      // 显示订单查询列表
      int nRow = getBillCardPanel2().getBillModel().getRowCount();
      if (nRow <= 0) {
        showHintMessage(NCLangRes.getInstance().getStrByID("40040101",
            "UPP40040101-000541")/* @res "无数据选中" */);
        return;
      }
      getBillCardPanel2().getBillTable()
          .removeRowSelectionInterval(0, nRow - 1);
      //
      for (int i = 0; i < nRow; i++)
        getBillCardPanel2().getBillModel().setRowState(i, BillModel.UNSTATE);

      nRow = getBillCardPanel2().getBillModel().getRowCount();
      for (int i = 0; i < nRow; i++)
        getBillCardPanel2().getBillModel().setRowState(i, BillModel.UNSTATE);

      getBillCardPanel2().updateUI();
      // 除全消,分单,暂估外所有正常
      for (int i = 0; i < 5; i++)
        m_nButtonState[i] = 0;
      m_nButtonState[1] = 1;
      m_nButtonState[2] = 1;
      m_nButtonState[3] = 1;
      m_nUIState = 2;
      changeButtonState();
    }

    if (m_nUIState == 3) {
      int nRow = getBillListPanel().getHeadBillModel().getRowCount();
      getBillListPanel().getHeadTable().removeRowSelectionInterval(0, nRow - 1);
      // 显示入库单列表
      for (int i = 0; i < nRow; i++)
        getBillListPanel().getHeadBillModel().setRowState(i, BillModel.UNSTATE);

      nRow = getBillListPanel().getHeadBillModel().getRowCount();
      for (int i = 0; i < nRow; i++)
        getBillListPanel().getHeadBillModel().setRowState(i, BillModel.UNSTATE);

      getBillListPanel().updateUI();
      if (!m_bAdd) {
        // 非订单生成入库单时，除全选,查询外其他按钮为灰
        for (int i = 0; i < 8; i++)
          m_nButtonState[i] = 1;
        m_nButtonState[0] = 0;
        m_nButtonState[3] = 0;
      }
      else {
        // 订单生成入库单时，除全选外其他按钮为灰
        for (int i = 0; i < 8; i++)
          m_nButtonState[i] = 1;
        m_nButtonState[0] = 0;
      }
      m_nUIState = 3;
      changeButtonState();
    }
  }

  /**
   * 此处插入方法说明。 功能描述:列表转换到卡片 输入参数: 返回值: 异常处理:
   */
  public void onSwitch() {
    // 列表可能已排序，获得排序前的缓存记录指针
    m_nPresentRecord = nc.ui.pu.pub.PuTool.getIndexBeforeSort(m_listPanel,
        m_nPresentRecord);

    this.remove(getBillListPanel());
    getBillCardPanel1().setVisible(true);
    this.setButtons(m_buttons11);
    m_nButtonState = new int[m_buttons1.length];

    // 不存在期初入库单
    if (getBillListPanel().getHeadBillModel().getRowCount() == 0) {
      getBillCardPanel1().getBillData().clearViewData();
      getBillCardPanel1().updateValue();
      getBillCardPanel1().updateUI();
      getBillCardPanel1().setEnabled(false);
      if (m_bGenerate) {
        m_bAdd = false;
      }

      // 设置除增加,打印，查询和列表外的所有按钮为灰
      for (int i = 0; i < 16; i++) {
        m_nButtonState[i] = 1;
      }
      m_nButtonState[0] = 0;
      m_nButtonState[6] = 0;
      m_nButtonState[11] = 0;
      m_nButtonState[12] = 0;
      m_nButtonState[13] = 0;
      m_nUIState = 1;
      changeButtonState();
      return;
    }

    // 存在期初入库单
    if(m_nPresentRecord<0){
      for (int i = 0; i < getBillListPanel().getHeadBillModel().getRowCount(); i++) {
        if (getBillListPanel().getHeadBillModel().getRowState(i) == BillModel.SELECTED) {
          m_nPresentRecord = i;
          break;
        }
      }
    }

    if (m_nPresentRecord >= 0) {
      if (m_bGenerate && 
          PuPubVO.getString_TrimZeroLenAsNull(m_VOs[m_nPresentRecord].getHeadVO().getCgeneralhid())==null) {
        getBillCardPanel1().setEnabled(false);
        m_bAdd = true;
        // 设置业务员的部门属性
        String sKey = m_VOs[m_nPresentRecord].getHeadVO().getCdptid();
        UIRefPane nRefPanel = (UIRefPane) getBillCardPanel1().getHeadItem(
            "cbizid").getComponent();
        String sWhere = " bd_psndoc.pk_deptdoc = '" + sKey + "'";
        if (sKey != null && sKey.length() > 0)
          nRefPanel.setWhereString(sWhere);
        else
          nRefPanel.setWhereString("bd_deptdoc.pk_corp = '" + m_sUnitCode
              + "' and bd_deptdoc.pk_deptdoc = bd_psndoc.pk_deptdoc");

        // 设置除保存，删行, 打印，放弃外所有按钮为灰
        for (int i = 0; i < 16; i++) {
          m_nButtonState[i] = 1;
        }
        m_nButtonState[2] = 0;
        m_nButtonState[3] = 0;
        m_nButtonState[4] = 0;
        m_nButtonState[12] = 0;
        m_nButtonState[13] = 0;
      }
      else {
        // 设置除保存,取消,删行外所有按钮为正常
        for (int i = 0; i < 16; i++) {
          m_nButtonState[i] = 0;
        }
        for (int i = 2; i < 5; i++)
          m_nButtonState[i] = 1;
        if (m_nPresentRecord == 0) {
          for (int i = 7; i < 9; i++)
            m_nButtonState[i] = 1;
        }
        if (m_nPresentRecord == m_VOs.length - 1) {
          for (int i = 9; i < 11; i++)
            m_nButtonState[i] = 1;
        }
        if (m_VOs.length == 1) {
          for (int i = 7; i < 11; i++)
            m_nButtonState[i] = 1;
        }
        // 暂估应付时,不可以修改，也就不可以行操作
        if (m_bZGYF.booleanValue()) {
          m_nButtonState[1] = 1;
        }
      }
      m_nUIState = 1;
      changeButtonState();

      // 显示卡片单据
      getBillCardPanel1().setBillValueVO(m_VOs[m_nPresentRecord]);
      getBillCardPanel1().getBillModel().execLoadFormula();
      // 显示来源单据信息
      nc.ui.pu.pub.PuTool.loadSourceBillData(getBillCardPanel1(), "4T");
      getBillCardPanel1().updateValue();
      getBillCardPanel1().updateUI();

      if (m_bGenerate){
          if(PuPubVO.getString_TrimZeroLenAsNull(m_VOs[m_nPresentRecord].getHeadVO().getCgeneralhid())!=null){
            getBillCardPanel1().setEnabled(false);
          }
          else 
            setPartNoEditable();
      }
      // 显示备注
      UIRefPane nRefPanel1 = (UIRefPane) getBillCardPanel1().getHeadItem(
          "vnote").getComponent();
      nRefPanel1.setValue(m_VOs[m_nPresentRecord].getHeadVO().getVnote());

      // 显示库存组织
      setCalbodyValue();

      if (m_bGenerate) {
        // 聚集在入库单号
        getBillCardPanel1().transferFocusTo(BillCardPanel.HEAD);
      }

    }
  }

  /**
   * 此处插入方法说明。 功能描述:增加状态查询完毕后，设置表体除暂估数量，暂估单价,暂估金额外均不可编辑 输入参数: 返回值: 异常处理:
   */
  private void setAddPartNoEditable() {
    BillItem items[] = getBillCardPanel2().getBodyItems();
    for (int i = 0; i < items.length; i++)
      items[i].setEnabled(false);

    for (int i = 0; i < items.length; i++) {
      String sKey = items[i].getKey().trim();
      if (sKey.equals("ngaugenum"))
        items[i].setEnabled(true);
      if (sKey.equals("nprice"))
        items[i].setEnabled(true);
      if (sKey.equals("nmoney"))
        items[i].setEnabled(true);
    }

    UIRefPane nRefPanel1 = (UIRefPane) getBillCardPanel2().getBodyItem(
        "ngaugenum").getComponent();
    UITextField nStockNumUI = (UITextField) nRefPanel1.getUITextField();
    // nStockNumUI.setMaxLength(13);
    nStockNumUI.setMaxLength(16);

    UIRefPane nRefPanel2 = (UIRefPane) getBillCardPanel2()
        .getBodyItem("nprice").getComponent();
    UITextField nPriceUI = (UITextField) nRefPanel2.getUITextField();
    // nPriceUI.setMaxLength(9);
    nPriceUI.setMaxLength(16);
    nPriceUI.setDelStr("-");

    UIRefPane nRefPanel3 = (UIRefPane) getBillCardPanel2()
        .getBodyItem("nmoney").getComponent();
    UITextField nMoneyUI = (UITextField) nRefPanel3.getUITextField();
    // nMoneyUI.setMaxLength(9);
    nMoneyUI.setMaxLength(16);

  }

  /**
   * 此处插入方法说明。 创建日期：(2003-11-18 16:57:10)
   */
  private void setCalbodyValue() {
    UIRefPane nRefPanel = (UIRefPane) getBillCardPanel1().getHeadItem(
        "cwarehouseid").getComponent();
    String sWarehouseid = nRefPanel.getRefPK();
    ((UIRefPane) getBillCardPanel1().getHeadItem("cwarehouse").getComponent())
        .setValue(sWarehouseid);
    int nRow = getBillCardPanel1().getRowCount();
    if (sWarehouseid == null || sWarehouseid.trim().length() == 0) {
      // nRefPanel =
      // (UIRefPane)getBillCardPanel1().getHeadItem("cstoreorganization").getComponent();
      // nRefPanel.setValue(null);
      // nRefPanel.setPK(null);
      //    
      for (int i = 0; i < nRow; i++) {
        getBillCardPanel1().setBodyValueAt(null, i, "pk_reqstoorg");
        getBillCardPanel1().setBodyValueAt(null, i, "pk_creqwareid");
      }
    }
    else {
      
      Object oTemp = getBillCardPanel1().execHeadFormula(
          "cstoreorganization->getColValue(bd_stordoc,pk_calbody,pk_stordoc,cwarehouse)");
      if (oTemp != null && oTemp.toString().trim().length() > 0) {
        nRefPanel = (UIRefPane) getBillCardPanel1().getHeadItem(
            "cstoreorganization").getComponent();
        nRefPanel.setPK(oTemp.toString());
      }
      for (int i = 0; i < nRow; i++) {
        getBillCardPanel1().setBodyValueAt(oTemp.toString(), i, "pk_reqstoorg");
        getBillCardPanel1().setBodyValueAt(sWarehouseid, i, "pk_creqwareid");
      }
    }

  }

  /**
   * @since v53, 支持自定义项可编辑，支持可配置模板，czp, 2008-04-17
   *        <p>
   *        注意，调整后详细说明：
   *        <p>
   *        A、保留原来(V502及以前)是否可编辑的逻辑，
   *        <p>
   *        1、表头：业务类型、库存组织不可编辑，其它项目可编辑
   *        <p>
   *        2、表体：自由项、批次号、行号外均可编辑，其它项目不可编辑
   *        <p>
   *        B、V53新增的逻辑，
   *        <p>
   *        1、表头：增加了表头自定义项1-20可配置成可编辑
   *        <p>
   *        2、表体：
   *        <p>
   *        B-2-1)、增加了表体自定义项1-20可配置成可编辑
   *        <p>
   *        B-2-2)、数量、单价、金额在暂估时确认，不允许修改，要修改需要删除期初暂估入库单重新暂估(与手工暂估处理一致)
   */
  private void setPartNoEditable() {
    getBillCardPanel1().setEnabled(true);
    BillItem items[] = getBillCardPanel1().getHeadItems();
    for (int i = 0; i < items.length; i++) {
      String sKey = items[i].getKey().trim();
      if (sKey.equals("cbiztype") || sKey.equals("cstoreorganization"))
        items[i].setEnabled(false);
    }
    // since V53,数量、单价、金额在暂估时确认，不允许修改，要修改需要删除期初暂估入库单重新暂估(与手工暂估处理一致)
    items = getBillCardPanel1().getBodyItems();
    for (int i = 0; i < items.length; i++) {
      String sKey = items[i].getKey().trim();
      if (sKey.equals("vfree"))
        items[i].setEnabled(items[i].isEdit());
      else if (sKey.equals("vbatchcode"))
        items[i].setEnabled(items[i].isEdit());
      else if (sKey.endsWith("crowno"))
        items[i].setEnabled(items[i].isEdit());
      else if (sKey.equals("ninnum"))
        items[i].setEnabled(false);
      else if (sKey.equals("nprice"))
        items[i].setEnabled(false);
      else if (sKey.equals("nmny"))
        items[i].setEnabled(false);
      else if (sKey.startsWith("vuserdef"))
        items[i].setEnabled(items[i].isEdit());
      else
        items[i].setEnabled(false);
    }

    // 币种为人民币折本汇率不可修改 zhf add
    int rows = getBillCardPanel2().getBillModel().getRowCount();
    for (int i = 0; i < rows; i++) {
      Object curTypeId = getBillCardPanel2()
          .getBodyValueAt(i, "currencytypeid");
      if (curTypeId == null)
        continue;

      if (curTypeId.toString().equalsIgnoreCase(m_sCurrTypeID))// 判断当前币种是否公司本位币
        // 设置折本汇率编辑属性
        getBillCardPanel2().getBillModel().setCellEditable(i,
            "nexchangeotobrate", false);
      else
        getBillCardPanel2().getBillModel().setCellEditable(i,
            "nexchangeotobrate", true);
    }
    // zhf end

    //
    UIRefPane nRefPanel1 = (UIRefPane) getBillCardPanel1()
        .getBodyItem("ninnum").getComponent();
    UITextField nStockNumUI = (UITextField) nRefPanel1.getUITextField();
    // nStockNumUI.setMaxLength(13);
    nStockNumUI.setMaxLength(16);

    UIRefPane nRefPanel2 = (UIRefPane) getBillCardPanel1()
        .getBodyItem("nprice").getComponent();
    UITextField nPriceUI = (UITextField) nRefPanel2.getUITextField();
    // nPriceUI.setMaxLength(9);
    nPriceUI.setMaxLength(16);
    nPriceUI.setDelStr("-");

    UIRefPane nRefPanel3 = (UIRefPane) getBillCardPanel1().getBodyItem("nmny")
        .getComponent();
    UITextField nMoneyUI = (UITextField) nRefPanel3.getUITextField();
    // nMoneyUI.setMaxLength(9);
    nMoneyUI.setMaxLength(16);

    UIRefPane nRefPanel4 = (UIRefPane) getBillCardPanel1().getBodyItem(
        "cinventorycode").getComponent();
    String sWhere = " upper(bd_invmandoc.sealflag) = 'N' and bd_invmandoc.pk_invbasdoc = bd_invbasdoc.pk_invbasdoc and bd_invmandoc.pk_corp = '"
        + m_sUnitCode + "'";
    nRefPanel4.setWhereString(sWhere);

    UIRefPane nRefPanel5 = (UIRefPane) getBillCardPanel1().getHeadItem("vnote")
        .getComponent();
    nRefPanel5.setReturnCode(false);
    nRefPanel5.setAutoCheck(false);

    UIRefPane nRefPanel6 = (UIRefPane) getBillCardPanel1().getHeadItem(
        "cwarehouseid").getComponent();
    nRefPanel6.setWhereString(" bd_stordoc.pk_corp = '" + m_sUnitCode
        + "' and upper(gubflag) = 'N'");

    UIRefPane nRefPanel7 = (UIRefPane) getBillCardPanel1().getHeadItem(
        "cproviderid").getComponent();
    nRefPanel7
        .setWhereString(" bd_cumandoc.pk_corp = '"
            + m_sUnitCode
            + "' and upper(frozenflag) = 'N' and (custflag = '1' or custflag = '3') and bd_cumandoc.pk_cubasdoc = bd_cubasdoc.pk_cubasdoc");

    // 批次获得存货属性:自由项、是否批次管理
    int nRow = m_VOs[m_nPresentRecord].getBodyVO().length;
    InvVO invVO[] = new InvVO[nRow];
    for (int i = 0; i < nRow; i++) {
      invVO[i] = new InvVO();
      invVO[i].setCinventoryid(m_VOs[m_nPresentRecord].getBodyVO()[i]
          .getCinventoryid());
    }
    invVO = m_formula.getQuryInvVOs(invVO, false, false, 2);
    for (int i = 0; i < nRow; i++) {
      Integer j = invVO[i].getIsFreeItemMgt();
      if (j != null) {
        if (j.intValue() != 1)
          getBillCardPanel1().setCellEditable(i, "vfree", false);
        else
          getBillCardPanel1().setCellEditable(i, "vfree", true);
      }
      else {
        getBillCardPanel1().setCellEditable(i, "vfree", false);
      }
      j = invVO[i].getIsLotMgt();
      if (j != null) {
        if (j.intValue() != 1)
          getBillCardPanel1().setCellEditable(i, "vbatchcode", false);
        else
          getBillCardPanel1().setCellEditable(i, "vbatchcode", true);
      }
      else {
        getBillCardPanel1().setCellEditable(i, "vbatchcode", false);
      }
    }
    //
  }

  /*
   * 订单查询结果界面点击"返回按钮"处理
   */
  private void onReturn() {
    getBillCardPanel2().setVisible(false);
    getBillCardPanel1().setVisible(true);
    this.setButtons(m_buttons11);
    m_nButtonState = new int[m_buttons1.length];

    // 不存在期初入库单
    if (getBillListPanel().getHeadBillModel().getRowCount() == 0) {
      getBillCardPanel1().getBillData().clearViewData();
      getBillCardPanel1().updateValue();
      getBillCardPanel1().updateUI();
      getBillCardPanel1().setEnabled(false);
      if (m_bGenerate) {
        m_bAdd = false;
      }

      // 设置除增加,打印，查询和列表外的所有按钮为灰
      for (int i = 0; i < 16; i++) {
        m_nButtonState[i] = 1;
      }
      m_nButtonState[0] = 0;
      m_nButtonState[6] = 0;
      m_nButtonState[11] = 0;
      m_nButtonState[12] = 0;
      m_nButtonState[13] = 0;
      m_nUIState = 1;
      changeButtonState();
      return;
    }

    // 存在期初入库单
    for (int i = 0; i < getBillListPanel().getHeadBillModel().getRowCount(); i++) {
      if (getBillListPanel().getHeadBillModel().getRowState(i) == BillModel.SELECTED) {
        m_nPresentRecord = i;
        break;
      }
    }

    if (m_nPresentRecord >= 0) {
      if (m_bGenerate) {
        getBillCardPanel1().setEnabled(false);
        m_bAdd = true;
        // 设置业务员的部门属性
        String sKey = m_VOs[m_nPresentRecord].getHeadVO().getCdptid();
        UIRefPane nRefPanel = (UIRefPane) getBillCardPanel1().getHeadItem(
            "cbizid").getComponent();
        String sWhere = " bd_psndoc.pk_deptdoc = '" + sKey + "'";
        if (sKey != null && sKey.length() > 0)
          nRefPanel.setWhereString(sWhere);
        else
          nRefPanel.setWhereString("bd_deptdoc.pk_corp = '" + m_sUnitCode
              + "' and bd_deptdoc.pk_deptdoc = bd_psndoc.pk_deptdoc");

        // 设置除保存，打印，放弃外所有按钮为灰
        for (int i = 0; i < 16; i++) {
          m_nButtonState[i] = 1;
        }
        m_nButtonState[2] = 0;
        m_nButtonState[4] = 0;
        m_nButtonState[12] = 0;
        m_nButtonState[13] = 0;
      }
      else {
        // 设置除保存,取消,删行外所有按钮为正常
        for (int i = 0; i < 16; i++) {
          m_nButtonState[i] = 0;
        }
        for (int i = 2; i < 5; i++)
          m_nButtonState[i] = 1;
        if (m_nPresentRecord == 0) {
          for (int i = 7; i < 9; i++)
            m_nButtonState[i] = 1;
        }
        if (m_nPresentRecord == m_VOs.length - 1) {
          for (int i = 9; i < 11; i++)
            m_nButtonState[i] = 1;
        }
        if (m_VOs.length == 1) {
          for (int i = 7; i < 11; i++)
            m_nButtonState[i] = 1;
        }
        // 暂估应付时,不可以修改，也就不可以行操作
        if (m_bZGYF.booleanValue()) {
          m_nButtonState[1] = 1;
        }
      }
      m_nUIState = 1;
      changeButtonState();

      // 显示卡片单据
      getBillCardPanel1().setBillValueVO(m_VOs[m_nPresentRecord]);
      getBillCardPanel1().getBillModel().execLoadFormula();
      // 显示来源单据信息
      nc.ui.pu.pub.PuTool.loadSourceBillData(getBillCardPanel1(), "4T");
      getBillCardPanel1().updateValue();
      getBillCardPanel1().updateUI();

      if (m_bGenerate)
        setPartNoEditable();
      // 显示备注
      UIRefPane nRefPanel1 = (UIRefPane) getBillCardPanel1().getHeadItem(
          "vnote").getComponent();
      nRefPanel1.setValue(m_VOs[m_nPresentRecord].getHeadVO().getVnote());

      // 显示库存组织
      setCalbodyValue();

      if (m_bGenerate) {
        // 聚集在入库单号
        getBillCardPanel1().transferFocusTo(BillCardPanel.HEAD);
      }

    }
  }

  /**
   * 此处插入方法说明。 功能描述:处理列表选择 输入参数: 返回值: 异常处理: 日期:2002/09/26
   */
  public void valueChanged(javax.swing.event.ListSelectionEvent event) {
    if ((ListSelectionModel) event.getSource() == getBillCardPanel2()
        .getBillTable().getSelectionModel()) {
      int nRow = getBillCardPanel2().getBillModel().getRowCount();

      // // 表体所有行恢复正常（不选择）
      // for (int i = 0; i < nRow; i++) {
      // getBillCardPanel2().getBillModel().setRowState(i,
      // BillModel.UNSTATE);
      // }

      // 获得表体选择行数
      int nSelected[] = getBillCardPanel2().getBillTable().getSelectedRows();
      if (nSelected != null && nSelected.length > 0) {
        for (int i = 0; i < nSelected.length; i++) {
          int j = nSelected[i];
          if (j < nRow)
            getBillCardPanel2().getBillModel().setRowState(j,
                BillModel.SELECTED);
        }
      }

      int m = 0;
      for (int i = 0; i < getBillCardPanel2().getBillModel().getRowCount(); i++) {
        if (getBillCardPanel2().getBillModel().getRowState(i) == BillModel.SELECTED)
          m++;
      }
      if (m > 0) {
        // 全部正常
        m_nButtonState[0] = 0;
        m_nButtonState[1] = 0;
        m_nButtonState[2] = 0;
        m_nButtonState[3] = 0;
        m_nButtonState[4] = 0;
      }
      else {
        // 全消,暂估为灰
        m_nButtonState[1] = 1;
        m_nButtonState[3] = 1;
      }
      changeButtonState();

    }
    else if ((ListSelectionModel) event.getSource() == getBillListPanel()
        .getHeadTable().getSelectionModel()) {
      int nRow = getBillListPanel().getHeadBillModel().getRowCount();

      // 表头所有行恢复正常（不选择）
      for (int i = 0; i < nRow; i++) {
        getBillListPanel().getHeadBillModel().setRowState(i, BillModel.UNSTATE);
      }

      // 获得表头选择行数
      int nSelected[] = getBillListPanel().getHeadTable().getSelectedRows();
      if (nSelected != null && nSelected.length > 0) {
        for (int i = 0; i < nSelected.length; i++) {
          int j = nSelected[i];
          if (j < nRow)
            getBillListPanel().getHeadBillModel().setRowState(j,
                BillModel.SELECTED);
        }
      }
      // since v53
      setListBtnsState();
      //
    }
  }

  /*
   * since v53, 列表按钮显示逻辑统一算法--暂估后的列表选择状态
   */
  private void setListBtnsStateEditable() {
    int nRow = getBillListPanel().getHeadBillModel().getRowCount();
    int n = 0;
    for (int i = 0; i < getBillListPanel().getHeadBillModel().getRowCount(); i++) {
      if (getBillListPanel().getHeadBillModel().getRowState(i) == BillModel.SELECTED)
        n++;
    }
    // m_buttons3[4] = "卡片显示";
//    if (n == 1 || nRow == 0) {
      m_nButtonState[4] = 0;
//   }
//    else {
//      m_nButtonState[4] = 1;
//    }
    // m_buttons3[3] = "查询";
    // m_buttons3[0] = "全选";
    // m_buttons3[1] = "全消";
    // m_buttons3[2] = "作废";
    // m_buttons3[5] = "文档管理";
    // m_buttons3[6] = "打印";
    // m_buttons3[7] = "打印预览";
    m_nButtonState[3] = 1;
    m_nButtonState[0] = 1;
    m_nButtonState[1] = 1;
    m_nButtonState[2] = 1;
    m_nButtonState[5] = 1;
    m_nButtonState[6] = 1;
    m_nButtonState[7] = 1;
    //
    changeButtonState();
    //
    m_btnListLinkQuery.setEnabled(false);
    updateButton(m_btnListLinkQuery);
    //
  }

  /*
   * since v53, 列表按钮显示逻辑统一算法
   */
  private void setListBtnsState() {
    // 增加期初采购入库
    if (m_bAdd) {
      setListBtnsStateEditable();
      return;
    }
    // 浏览期初采购入库
    int nRow = getBillListPanel().getHeadBillModel().getRowCount();
    int n = 0;
    for (int i = 0; i < getBillListPanel().getHeadBillModel().getRowCount(); i++) {
      if (getBillListPanel().getHeadBillModel().getRowState(i) == BillModel.SELECTED)
        n++;
    }
    // m_buttons3[3] = "查询";
    m_nButtonState[3] = 0;
    // m_buttons3[4] = "卡片显示";
    if (n == 1 || nRow == 0) {
      m_nButtonState[4] = 0;
    }
    else {
      m_nButtonState[4] = 1;
    }
    // m_buttons3[0] = "全选";
    if (nRow==0 || nRow > 0 && n == nRow) {
      m_nButtonState[0] = 1;
    }
    else {
      m_nButtonState[0] = 0;
    }
    // m_buttons3[1] = "全消";
    // m_buttons3[2] = "作废";
    // m_buttons3[5] = "文档管理";
    // m_buttons3[6] = "打印";
    // m_buttons3[7] = "打印预览";
    if (nRow > 0 && n != 0) {
      m_nButtonState[1] = 0;
      m_nButtonState[2] = 0;
      m_nButtonState[5] = 0;
      m_nButtonState[6] = 0;
      m_nButtonState[7] = 0;
    }
    else {
      m_nButtonState[1] = 1;
      m_nButtonState[2] = 1;
      m_nButtonState[5] = 1;
      m_nButtonState[6] = 1;
      m_nButtonState[7] = 1;
    }
    //
    changeButtonState();
    //
    m_btnListLinkQuery.setEnabled(nRow > 0 && m_buttons3[4].isEnabled());
    updateButton(m_btnListLinkQuery);
    //

  }

  /*
   * 采购公司和收货公司不同,需要转换: 存货, 供应商, 单价, 金额 2006-07-20 xhq
   */
  private GeneralHVO[] switchVOForDifferentCorp(GeneralHVO billVO[])
      throws Exception {
    Vector v1 = new Vector(), v2 = new Vector();
    for (int i = 0; i < billVO.length; i++) {
      GeneralHHeaderVO headVO = billVO[i].getHeadVO();
      if (!headVO.getPk_corp().equals(headVO.getPk_purcorp()))
        v1.addElement(billVO[i]);
      else
        v2.addElement(billVO[i]);
    }

    // 1.转换供应商管理ID
    Vector v3 = new Vector();
    for (int i = 0; i < v1.size(); i++) {
      GeneralHVO VO = (GeneralHVO) v1.elementAt(i);
      GeneralHHeaderVO headVO = VO.getHeadVO();
      if (headVO.getCproviderid() == null
          || headVO.getCproviderid().trim().length() == 0)
        continue;

      ChgDocPkVO switchVO = new ChgDocPkVO();
      switchVO.setDstCorpId(headVO.getPk_corp());
      switchVO.setSrcCorpId(headVO.getPk_purcorp());
      switchVO.setSrcManId(headVO.getCproviderid());
      v3.addElement(switchVO);
    }
    if (v3.size() > 0) {
      ChgDocPkVO switchVO[] = new ChgDocPkVO[v3.size()];
      v3.copyInto(switchVO);
      switchVO = ChgDataUtil.chgPkCuByCorp(switchVO);
      int j = 0;
      for (int i = 0; i < v1.size(); i++) {
        GeneralHVO VO = (GeneralHVO) v1.elementAt(i);
        GeneralHHeaderVO headVO = VO.getHeadVO();
        GeneralHItemVO bodyVO[] = VO.getBodyVO();
        if (headVO.getCproviderid() == null
            || headVO.getCproviderid().trim().length() == 0)
          continue;
        headVO.setCproviderid(switchVO[j].getDstManId());
        for (int k = 0; k < bodyVO.length; k++)
          bodyVO[k].setCvendorid(switchVO[j].getDstManId());
        j++;
      }
    }

    // 2.转换存货管理ID
    v3 = new Vector();
    // 3.转换单价,金额
    Vector v4 = new Vector(), v5 = new Vector();
    Hashtable tCurrTypeID = new Hashtable();// 公司本币
    for (int i = 0; i < v1.size(); i++) {
      GeneralHVO VO = (GeneralHVO) v1.elementAt(i);
      GeneralHHeaderVO headVO = VO.getHeadVO();
      GeneralHItemVO bodyVO[] = VO.getBodyVO();
      for (int j = 0; j < bodyVO.length; j++) {
        ChgDocPkVO switchVO = new ChgDocPkVO();
        switchVO.setDstCorpId(headVO.getPk_corp());
        switchVO.setSrcCorpId(headVO.getPk_purcorp());
        switchVO.setSrcBasId(bodyVO[j].getCbaseid());
        v3.addElement(switchVO);

        ChgPriceMnyVO switchVOs1 = new ChgPriceMnyVO();
        switchVOs1.setSrcCorpId(headVO.getPk_purcorp());
        Object oTemp = tCurrTypeID.get(headVO.getPk_purcorp());
        if (oTemp == null) {
          oTemp = SysInitBO_Client.getParaString(headVO.getPk_purcorp(),
              "BD301");
          tCurrTypeID.put(headVO.getPk_purcorp(), oTemp);
        }
        switchVOs1.setSrcCurrId(oTemp.toString());
        switchVOs1.setSrcVal(bodyVO[j].getNprice());
        switchVOs1.setDstCorpId(headVO.getPk_corp());
        switchVOs1.setDRateDate(getClientEnvironment().getDate());
        oTemp = tCurrTypeID.get(headVO.getPk_corp());
        if (oTemp == null) {
          oTemp = SysInitBO_Client.getParaString(headVO.getPk_corp(), "BD301");
          tCurrTypeID.put(headVO.getPk_corp(), oTemp);
        }
        switchVOs1.setDstCurrId(oTemp.toString());
        v4.addElement(switchVOs1);

        ChgPriceMnyVO switchVOs2 = new ChgPriceMnyVO();
        switchVOs2.setSrcCorpId(headVO.getPk_purcorp());
        oTemp = tCurrTypeID.get(headVO.getPk_purcorp());
        if (oTemp == null) {
          oTemp = SysInitBO_Client.getParaString(headVO.getPk_purcorp(),
              "BD301");
          tCurrTypeID.put(headVO.getPk_purcorp(), oTemp);
        }
        switchVOs2.setSrcCurrId(oTemp.toString());
        switchVOs2.setSrcVal(bodyVO[j].getNmny());
        switchVOs2.setDstCorpId(headVO.getPk_corp());
        oTemp = tCurrTypeID.get(headVO.getPk_corp());
        if (oTemp == null) {
          oTemp = SysInitBO_Client.getParaString(headVO.getPk_corp(), "BD301");
          tCurrTypeID.put(headVO.getPk_corp(), oTemp);
        }
        switchVOs2.setDstCurrId(oTemp.toString());
        switchVOs2.setDRateDate(getClientEnvironment().getDate());
        v5.addElement(switchVOs2);
      }
    }

    if (v3.size() == 0) {
      GeneralHVO VO[] = new GeneralHVO[v2.size()];
      v2.copyInto(VO);
      return VO;
    }
    ChgDocPkVO switchVO[] = new ChgDocPkVO[v3.size()];
    v3.copyInto(switchVO);
    switchVO = ChgDataUtil.chgPkInvByCorpBase(switchVO);

    if (v4.size() == 0 || v5.size() == 0) {
      GeneralHVO VO[] = new GeneralHVO[v2.size()];
      v2.copyInto(VO);
      return VO;
    }
    ChgPriceMnyVO switchVO1[] = new ChgPriceMnyVO[v4.size()];
    v4.copyInto(switchVO1);
    ChgPriceMnyVO switchVO2[] = new ChgPriceMnyVO[v5.size()];
    v5.copyInto(switchVO2);
    switchVO1 = ChgDataUtil.chgPriceByCorp(switchVO1);
    switchVO2 = ChgDataUtil.chgPriceByCorp(switchVO2);

    for (int i = 0; i < v1.size(); i++) {
      GeneralHVO VO = (GeneralHVO) v1.elementAt(i);
      GeneralHItemVO bodyVO[] = VO.getBodyVO();
      GeneralBb3VO bb3VO[] = VO.getGrandVO();
      for (int j = 0; j < bodyVO.length; j++) {
        bodyVO[j].setCinventoryid(switchVO[j].getDstManId());
        bodyVO[j].setNprice(switchVO1[j].getDstVal());
        bodyVO[j].setNmny(switchVO2[j].getDstVal());

        bb3VO[j].setNpprice(bodyVO[j].getNprice());
        bb3VO[j].setNpmoney(bodyVO[j].getNmny());
      }
    }

    // 返回
    v3 = new Vector();
    for (int i = 0; i < v1.size(); i++)
      v3.addElement(v1.elementAt(i));
    for (int i = 0; i < v2.size(); i++)
      v3.addElement(v2.elementAt(i));
    GeneralHVO VO[] = new GeneralHVO[v3.size()];
    v3.copyInto(VO);

    return VO;
  }

  public Object[] getRelaSortObjectArray() {
    // TODO 自动生成方法存根
    return m_VOs;
  }

  public void doMaintainAction(ILinkMaintainData maintaindata) {
    // TODO 自动生成方法存根

  }

  public void doAddAction(ILinkAddData adddata) {
    // TODO 自动生成方法存根

  }

  public void doApproveAction(ILinkApproveData approvedata) {
    // TODO 自动生成方法存根

  }

  public void doQueryAction(ILinkQueryData querydata) {
    // TODO 自动生成方法存根
    init();

    GeneralHVO tempVO = null;
    try {
      tempVO = EstimateHelper.queryStockByHeadKey(null, querydata.getBillID());
    }
    catch (Exception e) {
      SCMEnv.out(e);
      MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes.getInstance()
          .getStrByID("40040503", "UPP40040503-000046")/* @res "单据逐级联查" */, e
          .getMessage());
      return;
    }
    if (tempVO == null)
      return;

    String pk_corp = tempVO.getHeadVO().getPk_corp();
    InitialUIQryDlg dlg = null;
    dlg = initQueryModel1(dlg, pk_corp, true);
    ConditionVO conditionVO[] = dlg.getConditionVO();

    Vector vTemp = new Vector();
    ConditionVO condVO = new ConditionVO();
    condVO.setFieldCode("cgeneralhid");
    condVO.setOperaCode("=");
    condVO.setValue(tempVO.getHeadVO().getCgeneralhid());
    vTemp.addElement(condVO);
    if (conditionVO != null && conditionVO.length > 0) {
      for (int i = 0; i < conditionVO.length; i++)
        vTemp.addElement(conditionVO[i]);
    }
    conditionVO = new ConditionVO[vTemp.size()];
    vTemp.copyInto(conditionVO);

    try {
      GeneralHVO VOs[] = EstimateHelper.queryStockForEstimate(pk_corp,
          conditionVO);
      if (VOs != null && VOs.length > 0)
        tempVO = VOs[0];
      else
        tempVO = null;
    }
    catch (Exception e) {
      SCMEnv.out(e);
      MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes.getInstance()
          .getStrByID("40040503", "UPP40040503-000046")/* @res "单据逐级联查" */, e
          .getMessage());
      return;
    }

    getBillCardPanel1().setBillValueVO(tempVO);
    getBillCardPanel1().getBillModel().execLoadFormula();
    // 设置来源单据号
    nc.ui.pu.pub.PuTool.loadSourceBillData(getBillCardPanel1(), "4T");
    getBillCardPanel1().updateValue();
    getBillCardPanel1().updateUI();
    // 显示备注
    UIRefPane nRefPanel1 = (UIRefPane) getBillCardPanel1().getHeadItem("vnote")
        .getComponent();
    nRefPanel1.setValue(tempVO.getHeadVO().getVnote());

    // 显示库存组织
    setCalbodyValue();

    if (tempVO != null){
      m_VOs = new GeneralHVO[] {
        tempVO
      };
      //联查按钮也可用
      m_nButtonState[15]=0;
      changeButtonState();
    }
    else
      m_VOs = null;
  }

  public void setBillVO(AggregatedValueObject vo) {
    // TODO 自动生成方法存根
    if (vo == null)
      return;

    GeneralHVO VO = (GeneralHVO) vo;
    getBillCardPanel1().setBillValueVO(VO);
    getBillCardPanel1().getBillModel().execLoadFormula();
    // 设置来源单据号
    nc.ui.pu.pub.PuTool.loadSourceBillData(getBillCardPanel1(), "4T");
    getBillCardPanel1().updateValue();
    getBillCardPanel1().updateUI();
    // 显示备注
    UIRefPane nRefPanel1 = (UIRefPane) getBillCardPanel1().getHeadItem("vnote")
        .getComponent();
    nRefPanel1.setValue(VO.getHeadVO().getVnote());
    // 显示库存组织
    setCalbodyValue();

  }
  
  /**
   * 
   * 方法功能描述：
   * <p>初始化新查询对话框的信息。
   * <p>
   * <b>examples:</b>
   * <p>
   * 使用示例
   * <p>
   * <b>参数说明</b>
   * <p>
   * @author zhaoyha
   * @time 2008-10-15 下午07:57:41
   * @since 5.5
   */
  protected InitialQueryOrderDlg getNewQueryDlg(){
    
    if(m_condQrderDlg==null ){

      TemplateInfo tempinfo=new TemplateInfo();
      tempinfo.setPk_Org(m_sUnitCode);
      tempinfo.setCurrentCorpPk(m_sUnitCode);
      tempinfo.setFunNode(getModuleCode());
      tempinfo.setUserid(ClientEnvironment.getInstance().getUser().getPrimaryKey());

     //生成新的查询对话框
      m_condQrderDlg=new InitialQueryOrderDlg(this,tempinfo,
          NCLangRes.getInstance().getStrByID("4004050302", "UPP4004050302-000006")
          /*
           * @res
           * "期初暂估查询"
           */);
      //设置自定义参照
      m_condQrderDlg.setRefModel("po_order.cbiztype", SpeBiztypeDef.class, new Object[]{m_sBiztypeID});
    }
    return m_condQrderDlg;
  }
  
  private int nextBill(){
    int tempInx=m_nPresentRecord;
    while(++tempInx<m_VOs.length &&
        PuPubVO.getString_TrimZeroLenAsNull(m_VOs[tempInx].getHeadVO().getCgeneralhid())==null);
    if(tempInx<m_VOs.length)
      return tempInx;
    else
      return -1;
  }
  
  private int previousBill(){
    int tempInx=m_nPresentRecord;
    while(--tempInx>=0 &&
        PuPubVO.getString_TrimZeroLenAsNull(m_VOs[tempInx].getHeadVO().getCgeneralhid())==null);
    if(tempInx>=0)
      return tempInx;
    else
      return -1;
  }
  
  
}