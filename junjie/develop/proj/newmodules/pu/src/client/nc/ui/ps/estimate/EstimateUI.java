package nc.ui.ps.estimate;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.Vector;

import javax.swing.ListSelectionModel;

import nc.bs.bd.b21.BusinessCurrencyRateUtil;
import nc.ui.ml.NCLangRes;
import nc.ui.pi.pub.PiPqPublicUIClass;
import nc.ui.pps.PricStlHelper;
import nc.ui.pu.pub.POPubSetUI;
import nc.ui.pu.pub.POPubSetUI2;
import nc.ui.pu.pub.PuTool;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.ToftPanel;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIComboBox;
import nc.ui.pub.beans.UILabel;
import nc.ui.pub.beans.UIRadioButton;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.beans.UITextField;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.bill.BillData;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillEditListener;
import nc.ui.pub.bill.BillEditListener2;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillModel;
import nc.ui.pub.bill.DefaultCurrTypeBizDecimalListener;
import nc.ui.pub.bill.IBillModelRowStateChangeEventListener;
import nc.ui.pub.bill.IBillRelaSortListener2;
import nc.ui.pub.bill.RowStateChangeEvent;
import nc.ui.pub.para.SysInitBO_Client;
import nc.ui.pub.print.IDataSource;
import nc.ui.pub.print.PrintEntry;
import nc.ui.querytemplate.QueryConditionDLG;
import nc.ui.pu.jj.JJPuScmPubHelper;
import nc.ui.scm.pub.panel.RelationsCal;
import nc.ui.scm.sourcebill.SourceBillFlowDlg;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.pps.PricParaVO;
import nc.vo.ps.estimate.EstimateVO;
import nc.vo.ps.settle.OorderVO;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.ValidationException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.query.ConditionVO;
import nc.vo.querytemplate.TemplateInfo;
import nc.vo.pu.jjvo.InformationCostVO;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.scm.pu.Timer;
import nc.vo.scm.pub.SCMEnv;
import nc.vo.scm.pub.session.ClientLink;
import nc.vo.scm.service.ServcallVO;

/**
 * <p>
 * 功能描述:暂估UI
 * <p>
 * 作者:熊海情
 * <p>
 * 创建日期:2001－02－14
 * <p>
 * 修改：2004-09-10 袁野
 * <p>
 * 修改：2007-07-09 晁志平 for V502
 * <p>
 * 修改：2008-06-17 张红芳，赵玉行 for V55
 */
public class EstimateUI extends nc.ui.pub.ToftPanel implements BillEditListener,
    javax.swing.event.ListSelectionListener, BillEditListener2, IDataSource, IBillRelaSortListener2, MouseListener,
    IBillModelRowStateChangeEventListener {
	
  //2010-10-17 是否费用暂估--MeiChao 添加注释
  private boolean feeFlag = false; 
  // 界面控制按钮
  private ButtonObject m_buttons[] = null;

  // 按钮状态：0 正常；1 置灰；2 不可视
  private int m_nButtonState[] = null;

  // 单据
  private BillCardPanel m_billPanel = null;

  // 查询模板新增查询条件
  private UIRadioButton m_rbEst = null;

  private UIRadioButton m_rbUnEst = null;

  // 单位编码，系统应提供方法获取
  private String m_sUnitCode = getCorpPrimaryKey();

  // 查询条件,since v502 修改，支持收货公司条件，并检查只输入一个
  // private PoQueryCondition m_condClient=null;
  private EstimateQueryCondition m_condClient = null;

  // 新查询对话框,since 5.5 by zhaoyha at 2008.10.5
  private EstimateQueryDlg m_condDlg = null;

  // 缓存
  private EstimateVO m_estimate1VOs[] = null;

  private EstimateVO m_estimate2VOs[] = null;

  private String m_sZG = "N";

  // 暂估方式
  private String m_sEstimateMode = null;

  // 差异转入方式
  private String m_sDiffMode = null;

  // 暂估单价来源
  private String m_sEstPriceSource = null;

  // 库存是否启用
  private boolean m_bICStartUp = false;

  // 系统初始化参数 "BD501","BD505","BD301"
  private int m_measure[] = null;

  // 是否暂估应付
  private UFBoolean m_bZGYF = new UFBoolean(false);

  // 本币币种
  private String m_sCurrTypeID = null;

  // zhf add
  private POPubSetUI2 m_cardPoPubSetUI2 = null;

  /**
   * Estimate 构造子注解。
   */
  public EstimateUI() {
    super();
    init();
  }

  /**
   * 功能描述：编辑后事件处理 参数： 返回： 作者：熊海情 创建：2001-6-22 14:41:50 修改：晁志平 FOR V30 修改：晁志平 FOR
   * V53 修改：张红芳 FOR v55
   */
  public void afterEdit(BillEditEvent event) {
    int row = event.getRow();
    String key = event.getKey().trim();
    // 修改币种
    if ("currencytypename".equalsIgnoreCase(key)) {
      try {
        afterEditWhenCurrency(row);
        return;
      }
      catch (Exception e) {
        SCMEnv.out(e);
        return;
      }
    }
    // 修改折本汇率
    if ("nexchangeotobrate".equalsIgnoreCase(key)) {
      afterEditWhenNexchRate(row);
      return;
    }

    String strDisCntName = "应税内含";/*-=notranslate=-*/
    if (m_estimate1VOs[row].getIdiscounttaxtype().intValue() == 1)
      strDisCntName = "应税外加";
    if (m_estimate1VOs[row].getIdiscounttaxtype().intValue() == 2)
      strDisCntName = "不计税";

    // 修改本币后联动计算
    if ("nprice".equalsIgnoreCase(key) || "nmoney".equalsIgnoreCase(key) || "ntaxprice".equalsIgnoreCase(key)
        || "ntotalmoney".equalsIgnoreCase(key)) {
      computeBodyData(m_estimate1VOs, strDisCntName, getBillCardPanel(), event, this);
      // 本币值结算后联动计算原币数据
      // v55 by zhaoyha at 2008.9.22
      // 新需求，要求本币不让原币折算
      // UFDouble nExrate = PuPubVO.getUFDouble_NullAsZero(getBillCardPanel()
      // .getBodyValueAt(row, "nexchangeotobrate"));
      // if (nExrate.doubleValue() == 0)
      // return;
      // String sCurrId = PuPubVO.getString_TrimZeroLenAsNull(getBillCardPanel()
      // .getBodyValueAt(row, "currencytypeid"));
      // nExrate = new UFDouble(1 / nExrate.doubleValue(), getPOPubSetUI2()
      // .getBothExchRateDigit(m_sUnitCode, sCurrId)[0]);
      // setOrgPriceMnyFrmLocal(getBillCardPanel(), m_sUnitCode, row, nExrate,
      // m_measure[1], getPOPubSetUI2().getMoneyDigitByCurr_Busi(sCurrId),
      // "ninnum", sCurrId);
      return;
    }
    // 修改原币后信息后,联动计算,并向本币折算
    else if ("noriginalnetprice".equalsIgnoreCase(key) || "noriginalcurmny".equalsIgnoreCase(key)
        || "norgnettaxprice".equalsIgnoreCase(key) || "noriginaltaxpricemny".equalsIgnoreCase(key)) {
      // 修改原币后联动计算
      computeOrgBodyData(m_estimate1VOs, strDisCntName, getBillCardPanel(), event, this);
      // 原币值结算后联动计算本币数据
      setLocalPriceMnyFrmOrg(getBillCardPanel(), m_sUnitCode, row, (UFDouble) getBillCardPanel().getBodyValueAt(row,
          "nexchangeotobrate"), m_measure[1], m_measure[2], "ninnum");
    }
    // 修改税率后,暂估成本和暂估应付的本币都要计算,但暂估应付的原币不向暂估成本的本币折算
    else if ("ntaxrate".equalsIgnoreCase(key)) {
      // 暂估成本联动计算
      computeBodyData(m_estimate1VOs, strDisCntName, getBillCardPanel(), event, this);
      // 暂估应付原币联动计算
      computeOrgBodyData(m_estimate1VOs, strDisCntName, getBillCardPanel(), event, this);
      // 暂估应付原币向暂估应付本币折算
      setYFLocalPriceMnyFrmOrg(getBillCardPanel(), m_sUnitCode, row, (UFDouble) getBillCardPanel().getBodyValueAt(row,
          "nexchangeotobrate"), m_measure[1], m_measure[2], "ninnum");
    }
//    else if ("nfeemny".equalsIgnoreCase(key)){
//      UFDouble ufNum = PuPubVO.getUFDouble_NullAsZero(getBillCardPanel().getBillModel().getValueAt(event.getRow(), "ninnum"));
//      UFDouble newFeeMny=PuPubVO.getUFDouble_NullAsZero(event.getValue());
//      final UFDouble oldFeeMny=(UFDouble)event.getOldValue();
//      final int curRow=event.getRow();
//      final BillEditEvent newEvent=new BillEditEvent(event.getSource(),oldFeeMny,null,key,curRow,event.getPos());
//      if (ufNum.doubleValue() * newFeeMny.doubleValue() < 0) {
//        SwingUtilities.invokeLater(new Runnable(){
//          public void run() {
//            //先设置UITextField允许的值(>=0或<=0) by zhaoyha
//            beforeEdit(newEvent);
//            getBillCardPanel().setBodyValueAt(oldFeeMny, curRow, "nfeemny");
//          }
//        });
//      }
//    }    
  }

  /**
   * 编辑折本汇率后处理
   * <p>
   * <b>examples:</b>
   * <p>
   * 使用示例
   * <p>
   * <b>参数说明</b>
   * 
   * @param row
   *          <p>
   * @author zhanghongfang
   * @time 2008-7-28 下午03:03:26
   */

  private void afterEditWhenNexchRate(int row) {

    // 折本汇率
    Object bRate = getBillCardPanel().getBodyValueAt(row, "nexchangeotobrate");
    if (bRate == null || bRate.toString().trim().length() == 0) {
      return;
    }
    UFDouble ufBRate = new UFDouble(bRate.toString());

    // m_estimate1VOs[row].setNexchangeotobrate(ufBRate);

    setLocalPriceMnyFrmOrg(getBillCardPanel(), m_sUnitCode, row, ufBRate, m_measure[1], m_measure[2], "ninnum");

  }

  /**
   * 更新缓存数据
   * <p>
   * <b>examples:</b>
   * <p>
   * 使用示例
   * <p>
   * <b>参数说明</b>
   * 
   * @param rows
   *          <p>
   * @author zhanghongfang
   * @time 2008-7-28 下午03:03:37
   */

  private void updateEstimate1VO(Integer[] rows) {
    EstimateVO[] VOs = (EstimateVO[]) getBillCardPanel().getBillData().getBodyValueVOs("nc.vo.ps.estimate.EstimateVO");
    for (int i = 0; i < rows.length; i++) {
      m_estimate1VOs[rows[i].intValue()].setNoriginalcurmny(VOs[rows[i].intValue()].getNoriginalcurmny());
      m_estimate1VOs[rows[i].intValue()].setNorgtaxmoney(VOs[rows[i].intValue()].getNorgtaxmoney());
      m_estimate1VOs[rows[i].intValue()].setNoriginaltaxpricemny(VOs[rows[i].intValue()].getNoriginaltaxpricemny());

      m_estimate1VOs[rows[i].intValue()].setNorgnettaxprice(VOs[rows[i].intValue()].getNorgnettaxprice());
      m_estimate1VOs[rows[i].intValue()].setNoriginalnetprice(VOs[rows[i].intValue()].getNoriginalnetprice());

      m_estimate1VOs[rows[i].intValue()].setNtaxmoney(VOs[rows[i].intValue()].getNtaxmoney());
      m_estimate1VOs[rows[i].intValue()].setNmoney(VOs[rows[i].intValue()].getNmoney());
      m_estimate1VOs[rows[i].intValue()].setNtotalmoney(VOs[rows[i].intValue()].getNtotalmoney());

      m_estimate1VOs[rows[i].intValue()].setNprice(VOs[rows[i].intValue()].getNprice());
      m_estimate1VOs[rows[i].intValue()].setNtaxprice(VOs[rows[i].intValue()].getNtaxprice());

      m_estimate1VOs[rows[i].intValue()].setCfeeid(VOs[rows[i].intValue()].getCfeeid());
      m_estimate1VOs[rows[i].intValue()].setNfeemny(VOs[rows[i].intValue()].getNfeemny());

      // 把币种和汇率的变动更新到缓存
      m_estimate1VOs[rows[i].intValue()].setCurrencytypeid(VOs[rows[i].intValue()].getCurrencytypeid());
      //折本汇率 直接从界面上取
      Object bRate = getBillCardPanel().getBodyValueAt(rows[i].intValue(), "nexchangeotobrate");
      if (bRate != null && bRate.toString().trim().length() >0 ) {
    	  UFDouble ufBRate = new UFDouble(bRate.toString());          
          m_estimate1VOs[rows[i].intValue()].setNexchangeotobrate(ufBRate);
      }
      
      // ---- v55 by zhaoyha at 2008.9.22 -------------
      m_estimate1VOs[rows[i].intValue()].setNzgyfnotaxmoney(VOs[rows[i].intValue()].getNzgyfnotaxmoney());
      m_estimate1VOs[rows[i].intValue()].setNzgyfnotaxprice(VOs[rows[i].intValue()].getNzgyfnotaxprice());
      // ---- v55 by zhaoyha end -----------------------
    }
  }

  private POPubSetUI2 getPOPubSetUI2() {
    if (m_cardPoPubSetUI2 == null) {
      m_cardPoPubSetUI2 = new POPubSetUI2();
    }
    return m_cardPoPubSetUI2;
  }

  // /**
  // *
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
  // //取得币种
  // String sCurrId=(String)getBillCardPanel().getBillModel().getValueAt(iRow,
  // "currencytypeid");
  // int[] iaExchRateDigit = getDigits_ExchangeRate(sCurrId);
  // //得到折本、折辅汇率精度
  // if(iaExchRateDigit == null || iaExchRateDigit.length == 0){
  // getBillCardPanel().getBillModel().getItemByKey("nexchangeotobrate").setDecimalDigits(2);
  // }else{
  // getBillCardPanel().getBillModel().getItemByKey("nexchangeotobrate").setDecimalDigits(iaExchRateDigit[0]);
  // }
  // }
  /**
   * 根据币种设置实时折本汇率精度，原币金额业务精度
   */
  private void setExchangeRateMnyDigit(int row, String sCurrId) {

    // 首先设置显示精度
    int[] iaExchRateDigit = getPOPubSetUI2().getBothExchRateDigit(m_sUnitCode, sCurrId);
    int iMnyDigit = getPOPubSetUI2().getMoneyDigitByCurr_Busi(sCurrId);
    getBillCardPanel().getBillModel().getItemByKey("nexchangeotobrate").setDecimalDigits(iaExchRateDigit[0]);
    // 设置值
    UFDouble[] daRate = getPOPubSetUI2().getBothExchRateValue(m_sUnitCode, sCurrId,
        nc.ui.po.pub.PoPublicUIClass.getLoginDate());// 当前日期取到实时汇率
    getBillCardPanel().setBodyValueAt(daRate[0], row, "nexchangeotobrate");
    // 设置修改标志
    // getBillCardPanel().getBillModel().setRowState(row,
    // BillModel.MODIFICATION);

    getBillCardPanel().getBillModel().getItemByKey("noriginaltaxpricemny").setDecimalDigits(iMnyDigit);
    getBillCardPanel().getBillModel().getItemByKey("noriginalcurmny").setDecimalDigits(iMnyDigit);
    getBillCardPanel().getBillModel().getItemByKey("norgtaxmoney").setDecimalDigits(iMnyDigit);

    // 重新赋值 精度生效
    getBillCardPanel().getBillModel().setValueAt(getBillCardPanel().getBodyValueAt(row, "noriginaltaxpricemny"), row,
        "noriginaltaxpricemny");
    getBillCardPanel().getBillModel().setValueAt(getBillCardPanel().getBodyValueAt(row, "noriginalcurmny"), row,
        "noriginalcurmny");
    getBillCardPanel().getBillModel().setValueAt(getBillCardPanel().getBodyValueAt(row, "norgtaxmoney"), row,
        "norgtaxmoney");
  }

  private void afterEditWhenCurrency(int row) throws Exception {

    Object oCurrId = getBillCardPanel().getBodyValueAt(row, "currencytypeid");
    if (PuPubVO.getString_TrimZeroLenAsNull(oCurrId) == null) {
      MessageDialog.showWarningDlg(this, NCLangRes.getInstance()
          .getStrByID("40040503", "UPP40040503-000033"/* @res "暂估处理 */), NCLangRes.getInstance().getStrByID(
          "4004050301", "UPP4004050301-000011")/*
                                                 * @res 原币币种不能为空，请录入原币币种
                                                 */);
      return;
    }
    String sCurrId = oCurrId.toString().trim();

    // setRowDigits_ExchangeRate(row);
    setExchangeRateMnyDigit(row, sCurrId);
    if (sCurrId.equalsIgnoreCase(m_sCurrTypeID))// 判断当前币种是否公司本位币 设置折本汇率编辑属性
      getBillCardPanel().getBillModel().setCellEditable(row, "nexchangeotobrate", false);
    else
      getBillCardPanel().getBillModel().setCellEditable(row, "nexchangeotobrate", true);
    // 折本汇率
    Object bRate = getBillCardPanel().getBodyValueAt(row, "nexchangeotobrate");
    if (bRate == null || bRate.toString().trim().length() == 0) {
      return;
    }
    UFDouble ufBRate = new UFDouble(bRate.toString());
    setLocalPriceMnyFrmOrg(getBillCardPanel(), m_sUnitCode, row, ufBRate, m_measure[1], m_measure[2], "ninnum");
  }

  /**
   * 方法功能描述：由原币价格金额信息折算本币价格金额信息 用于暂估处理和期初暂估维护
   * <p>
   * <b>examples:</b>
   * <p>
   * 使用示例
   * <p>
   * <p>
   * 修改 by zhaoyha at 2008.9.22
   * <p>
   * 暂估成本和应付分开，对相关的字段进行了调整
   * <p>
   * 原币既向暂估成本也向暂估应付的本币折算
   * <p>
   * 只向暂估应付折请参见{@link #setYFLocalPriceMnyFrmOrg(BillCardPanel, String, int, UFDouble, int, int, String)}
   * <p>
   * <b>参数说明</b>
   * 
   * @param cardPanel
   * @param corpPk
   * @param row
   * @param nexchrate
   * @param nPriceDecimal
   * @param numfiled
   *          暂估数量项目主键 普通暂估 "ninnum" , 期初暂估"ngaugenum"
   *          <p>
   * @author zhanghongfang
   * @time 2008-8-6 上午09:51:31
   */
  public static void setLocalPriceMnyFrmOrg(BillCardPanel cardPanel, String corpPk, int row, UFDouble nexchrate,
      int nPriceDecimal, int nMnyDecimal, String numfiled) {

    // 原币金额
    UFDouble ufNoriginalcurmny = PuPubVO.getUFDouble_NullAsZero(cardPanel.getBodyValueAt(row, "noriginalcurmny"));
    UFDouble ufMoney = null;
    // 原币税额
    // UFDouble ufNoriginaltaxmny = PuPubVO.getUFDouble_NullAsZero(cardPanel
    // .getBodyValueAt(row, "norgtaxmoney"));
    UFDouble ufTaxMny = null;
    // 原币价税合计
    UFDouble ufNoriginalsummny = PuPubVO.getUFDouble_NullAsZero(cardPanel.getBodyValueAt(row, "noriginaltaxpricemny"));
    UFDouble ufSumMny = null;

    UFDouble ufdNum = PuPubVO.getUFDouble_NullAsZero(cardPanel.getBodyValueAt(row, numfiled));
    BusinessCurrencyRateUtil curUtil = POPubSetUI.getCurrArith_Busi(corpPk);

    Object oCurrid = cardPanel.getBodyValueAt(row, "currencytypeid");

    try {
      String localCuid = PiPqPublicUIClass.getNativeCurrencyID();
      //如果原币==本币
      if(localCuid.equals(oCurrid)){
        //带到暂估应付本币
        cardPanel.setBodyValueAt(cardPanel.getBodyValueAt(row, "noriginaltaxpricemny"), row, "nzgyfmoney");
        cardPanel.setBodyValueAt(cardPanel.getBodyValueAt(row, "norgnettaxprice"), row, "nzgyfprice");
        cardPanel.setBodyValueAt(cardPanel.getBodyValueAt(row, "noriginalcurmny"), row, "nzgyfnotaxmoney");
        cardPanel.setBodyValueAt(cardPanel.getBodyValueAt(row, "noriginalnetprice"), row, "nzgyfnotaxprice");
        cardPanel.setBodyValueAt(cardPanel.getBodyValueAt(row, "norgtaxmoney"), row, "nzgyftaxmoney");
        //带到暂估成本本币
        cardPanel.setBodyValueAt(cardPanel.getBodyValueAt(row, "noriginaltaxpricemny"), row, "ntotalmoney");
        cardPanel.setBodyValueAt(cardPanel.getBodyValueAt(row, "norgnettaxprice"), row, "ntaxprice");
        cardPanel.setBodyValueAt(cardPanel.getBodyValueAt(row, "noriginalcurmny"), row, "nmoney");
        cardPanel.setBodyValueAt(cardPanel.getBodyValueAt(row, "noriginalnetprice"), row, "nprice");
        cardPanel.setBodyValueAt(cardPanel.getBodyValueAt(row, "norgtaxmoney"), row, "ntaxmoney");
        return;
      }
      
      // 原币金额
      if (oCurrid == null) {
        ufMoney = null;
      }
      else {
        ufMoney = curUtil
            .getAmountByOpp(oCurrid.toString(), localCuid, ufNoriginalcurmny, nexchrate, null, nMnyDecimal);
      }
      cardPanel.setBodyValueAt(ufMoney, row, "nmoney");
      // 无税金额
      cardPanel.setBodyValueAt(ufMoney, row, "nzgyfnotaxmoney");
      // 原币税额
      // if (oCurrid == null) {
      // ufTaxMny = null;
      // }
      // else {

      // }

      // 原币价税合计
      if (oCurrid == null) {
        ufSumMny = null;
      }
      else {
        ufSumMny = curUtil.getAmountByOpp(oCurrid.toString(), localCuid, ufNoriginalsummny, nexchrate, null,
            nMnyDecimal);
      }
      // 价税合计
      cardPanel.setBodyValueAt(ufSumMny, row, "ntotalmoney");
      cardPanel.setBodyValueAt(ufSumMny, row, "nzgyfmoney");
      ufTaxMny = ufSumMny.sub(ufMoney);

      // 税金
      cardPanel.setBodyValueAt(ufTaxMny, row, "ntaxmoney");
      cardPanel.setBodyValueAt(ufTaxMny, row, "nzgyftaxmoney");
      // 单价
      if (ufdNum.doubleValue() != 0) {
        UFDouble nPrice = ufMoney.div(ufdNum);
        nPrice = nPrice.add(new UFDouble(0.0), nPriceDecimal);
        UFDouble nTaxPrice = ufSumMny.div(ufdNum);
        nTaxPrice = nTaxPrice.add(new UFDouble(0.0), nPriceDecimal);

        cardPanel.setBodyValueAt(nPrice, row, "nprice");
        cardPanel.setBodyValueAt(nPrice, row, "nzgyfnotaxprice");
        cardPanel.setBodyValueAt(nTaxPrice, row, "ntaxprice");
        cardPanel.setBodyValueAt(nTaxPrice, row, "nzgyfprice");
      }
      else {
        cardPanel.setBodyValueAt(new UFDouble(0.0), row, "nprice");
        cardPanel.setBodyValueAt(new UFDouble(0.0), row, "nzgyfnotaxprice");
        cardPanel.setBodyValueAt(new UFDouble(0.0), row, "ntaxprice");
        cardPanel.setBodyValueAt(new UFDouble(0.0), row, "nzgyfprice");
      }
    }
    catch (Exception e) {
      SCMEnv.out(e);
      return;
    }
  }

  /**
   * 方法功能描述：
   * <p>
   * 由原币价格金额信息折算本币价格金额信息 用于暂估处理和期初暂估维护
   * <p>
   * 只向暂估应付页签上的本币折算
   * <p>
   * <b>examples:</b>
   * <p>
   * 使用示例 <b>参数说明</b>
   * 
   * @param cardPanel
   * @param corpPk
   * @param row
   * @param nexchrate
   * @param nPriceDecimal
   * @param numfiled
   *          暂估数量项目主键 普通暂估 "ninnum" , 期初暂估"ngaugenum"
   *          <p>
   * @author zhaoyha
   * @time 2008-9-27 上午09:51:31
   */
  public static void setYFLocalPriceMnyFrmOrg(BillCardPanel cardPanel, String corpPk, int row, UFDouble nexchrate,
      int nPriceDecimal, int nMnyDecimal, String numfiled) {

    // 原币金额
    UFDouble ufNoriginalcurmny = PuPubVO.getUFDouble_NullAsZero(cardPanel.getBodyValueAt(row, "noriginalcurmny"));
    UFDouble ufMoney = null;
    UFDouble ufTaxMny = null;
    // 原币价税合计
    UFDouble ufNoriginalsummny = PuPubVO.getUFDouble_NullAsZero(cardPanel.getBodyValueAt(row, "noriginaltaxpricemny"));
    UFDouble ufSumMny = null;

    UFDouble ufdNum = PuPubVO.getUFDouble_NullAsZero(cardPanel.getBodyValueAt(row, numfiled));
    BusinessCurrencyRateUtil curUtil = POPubSetUI.getCurrArith_Busi(corpPk);

    Object oCurrid = cardPanel.getBodyValueAt(row, "currencytypeid");

    try {
      String localCuid = PiPqPublicUIClass.getNativeCurrencyID();
      //如果原币==本币
      if(localCuid.equals(oCurrid)){
        cardPanel.setBodyValueAt(cardPanel.getBodyValueAt(row, "noriginaltaxpricemny"), row, "nzgyfmoney");
        cardPanel.setBodyValueAt(cardPanel.getBodyValueAt(row, "norgnettaxprice"), row, "nzgyfprice");
        cardPanel.setBodyValueAt(cardPanel.getBodyValueAt(row, "noriginalcurmny"), row, "nzgyfnotaxmoney");
        cardPanel.setBodyValueAt(cardPanel.getBodyValueAt(row, "noriginalnetprice"), row, "nzgyfnotaxprice");
        cardPanel.setBodyValueAt(cardPanel.getBodyValueAt(row, "norgtaxmoney"), row, "nzgyftaxmoney");
        return;
      }
      
      // 原币金额
      if (oCurrid == null) {
        ufMoney = null;
      }
      
      else {
        ufMoney = curUtil
            .getAmountByOpp(oCurrid.toString(), localCuid, ufNoriginalcurmny, nexchrate, null, nMnyDecimal);
      }
      // 无税金额
      cardPanel.setBodyValueAt(ufMoney, row, "nzgyfnotaxmoney");

      // 原币价税合计
      if (oCurrid == null) {
        ufSumMny = null;
      }
      else {
        ufSumMny = curUtil.getAmountByOpp(oCurrid.toString(), localCuid, ufNoriginalsummny, nexchrate, null,
            nMnyDecimal);
      }
      // 价税合计
      cardPanel.setBodyValueAt(ufSumMny, row, "nzgyfmoney");
      ufTaxMny = ufSumMny.sub(ufMoney);

      // 税金
      cardPanel.setBodyValueAt(ufTaxMny, row, "nzgyftaxmoney");
      // 单价
      if (ufdNum.doubleValue() != 0) {
        UFDouble nPrice = ufMoney.div(ufdNum);
        nPrice = nPrice.add(new UFDouble(0.0), nPriceDecimal);
        UFDouble nTaxPrice = ufSumMny.div(ufdNum);
        nTaxPrice = nTaxPrice.add(new UFDouble(0.0), nPriceDecimal);
        cardPanel.setBodyValueAt(nPrice, row, "nzgyfnotaxprice");
        cardPanel.setBodyValueAt(nTaxPrice, row, "nzgyfprice");
      }
      else {
        cardPanel.setBodyValueAt(new UFDouble(0.0), row, "nzgyfnotaxprice");
        cardPanel.setBodyValueAt(new UFDouble(0.0), row, "nzgyfprice");
      }
    }
    catch (Exception e) {
      SCMEnv.out(e);
      return;
    }
  }
  

  /**
   * 方法功能描述：由本币价格金额信息折算原币价格金额信息 用于暂估处理和期初暂估维护
   * <p>
   * <b>examples:</b>
   * <p>
   * 使用示例
   * <p>
   * <b>参数说明</b>
   * 
   * @param cardPanel
   * @param corpPk
   * @param row
   * @param nexchrate
   * @param nPriceDecimal
   * @param numfiled
   *          暂估数量项目主键 普通暂估 "ninnum" , 期初暂估"ngaugenum"
   *          <p>
   * @author zhanghongfang
   * @time 2008-8-6 上午09:51:31
   */
  public static void setOrgPriceMnyFrmLocal(BillCardPanel cardPanel, String corpPk, int row, UFDouble nexchrate,
      int nPriceDecimal, int nMnyDecimal, String numfiled, String sCurrid) {

    // 金额
    UFDouble ufNoriginalcurmny = PuPubVO.getUFDouble_NullAsZero(cardPanel.getBodyValueAt(row, "nmoney"));
    UFDouble ufMoney = null;
    // 税额
    // UFDouble ufNoriginaltaxmny = PuPubVO.getUFDouble_NullAsZero(cardPanel
    // .getBodyValueAt(row, "ntaxmoney"));
    UFDouble ufTaxMny = null;
    // 价税合计
    UFDouble ufNoriginalsummny = PuPubVO.getUFDouble_NullAsZero(cardPanel.getBodyValueAt(row, "ntotalmoney"));
    UFDouble ufSumMny = null;

    UFDouble ufdNum = PuPubVO.getUFDouble_NullAsZero(cardPanel.getBodyValueAt(row, numfiled));
    BusinessCurrencyRateUtil curUtil = POPubSetUI.getCurrArith_Busi(corpPk);

    try {
      String localCuid = PiPqPublicUIClass.getNativeCurrencyID();
      // 原币金额
      if (sCurrid == null) {
        ufMoney = null;
      }
      else {
        ufMoney = curUtil.getAmountByOpp(localCuid, sCurrid, ufNoriginalcurmny, nexchrate, null);
      }
      cardPanel.setBodyValueAt(ufMoney, row, "noriginalcurmny");
      // 原币税额
      // if (sCurrid == null) {
      // ufTaxMny = null;
      // }
      // else {

      // }
      cardPanel.setBodyValueAt(ufTaxMny, row, "norgtaxmoney");
      // 原币价税合计
      if (sCurrid == null) {
        ufSumMny = null;
      }
      else {
        ufSumMny = curUtil.getAmountByOpp(localCuid, sCurrid, ufNoriginalsummny, nexchrate, null);
      }
      cardPanel.setBodyValueAt(ufSumMny, row, "noriginaltaxpricemny");

      ufTaxMny = ufSumMny.sub(ufMoney);

      // 单价
      if (ufdNum.doubleValue() != 0) {
        UFDouble nPrice = ufMoney.div(ufdNum);
        nPrice = nPrice.add(new UFDouble(0.0), nPriceDecimal);
        UFDouble nTaxPrice = ufSumMny.div(ufdNum);
        nTaxPrice = nTaxPrice.add(new UFDouble(0.0), nPriceDecimal);

        cardPanel.setBodyValueAt(nPrice, row, "noriginalnetprice");
        cardPanel.setBodyValueAt(nTaxPrice, row, "norgnettaxprice");
      }
      else {
        cardPanel.setBodyValueAt(new UFDouble(0.0), row, "noriginalnetprice");
        cardPanel.setBodyValueAt(new UFDouble(0.0), row, "norgnettaxprice");
      }
    }
    catch (Exception e) {
      SCMEnv.out(e);
      return;
    }
  }

  /**
   * 功能描述：行变换事件处理 参数： 返回： 作者：熊海情 创建：2001-6-22 14:41:50 修改：晁志平 FOR V30
   */
  public void bodyRowChange(BillEditEvent event) {
  }

  /**
   * 功能描述：改变界面按钮状态 参数： 返回： 作者：熊海情 创建：2001-6-22 14:41:50 修改：晁志平 FOR V30
   */
  private void changeButtonState() {
    for (int i = 0; i < m_nButtonState.length; i++) {
      if (m_nButtonState[i] == 0) {
        m_buttons[i].setVisible(true);
        m_buttons[i].setEnabled(true);
      }
      else if (m_nButtonState[i] == 1) {
        m_buttons[i].setVisible(true);
        m_buttons[i].setEnabled(false);
      }
      else if (m_nButtonState[i] == 2) {
        m_buttons[i].setVisible(false);
      }
      this.updateButton(m_buttons[i]);
    }
    // 当前选中行数量
    int iSelectedCnt = 0;
    int iRowCnt = getBillCardPanel().getRowCount();
    for (int i = 0; i < iRowCnt; i++) {
      if (getBillCardPanel().getBillModel().getRowState(i) == BillModel.SELECTED) {
        iSelectedCnt++;
      }
    }
    // 优质优价计算
    m_buttons[7].setEnabled(m_sZG.equals("N") && iRowCnt > 0 && iSelectedCnt == 1);
    this.updateButton(m_buttons[7]);

    // 联查按钮逻辑
    int iSelectedCntTable = getBillCardPanel().getBillTable().getSelectedRowCount();
    m_buttons[8].setEnabled(iRowCnt > 0 && iSelectedCntTable >= 1);
    this.updateButton(m_buttons[8]);
  }

  /**
   * 功能描述：查询模板增加查询条件 参数： 返回： 作者：熊海情 创建：2001-6-22 14:41:50 修改：晁志平 FOR V30
   */
  private void changeQueryModelLayout() {
    if (m_rbEst != null && m_rbUnEst != null)
      return;

    UILabel label1 = new UILabel(nc.ui.ml.NCLangRes.getInstance().getStrByID("4004050301", "UPP4004050301-000000")/*
                                                                                                                   * @res
                                                                                                                   * "选择入库单："
                                                                                                                   */);
    label1.setBounds(30, 65, 100, 25);

    m_rbEst = new UIRadioButton();
    m_rbEst.setBounds(130, 65, 16, 16);
    m_rbEst.setSelected(true);
    UILabel label2 = new UILabel(nc.ui.ml.NCLangRes.getInstance().getStrByID("4004050301", "UPP4004050301-000001")/*
                                                                                                                   * @res
                                                                                                                   * "未暂估"
                                                                                                                   */);
    label2.setBounds(146, 65, 60, 25);

    m_rbUnEst = new UIRadioButton();
    m_rbUnEst.setBounds(130, 95, 16, 16);
    UILabel label3 = new UILabel(nc.ui.ml.NCLangRes.getInstance().getStrByID("4004050301", "UPP4004050301-000002")/*
                                                                                                                   * @res
                                                                                                                   * "已暂估"
                                                                                                                   */);
    label3.setBounds(146, 95, 60, 25);

    javax.swing.ButtonGroup buttonGroup = new javax.swing.ButtonGroup();
    buttonGroup.add(m_rbEst);
    buttonGroup.add(m_rbUnEst);

    m_condClient.getUIPanelNormal().add(label1);
    m_condClient.getUIPanelNormal().add(label2);
    m_condClient.getUIPanelNormal().add(label3);
    m_condClient.getUIPanelNormal().add(m_rbEst);
    m_condClient.getUIPanelNormal().add(m_rbUnEst);
  }

  /**
   * 功能描述：修改单价,金额等的联运运算{注意：本方法仅适用于手工暂估&期初暂估的暂估界面联运运算!}
   * <p>
   * 参数：
   * <p>
   * 返回：
   * <p>
   * 作者：熊海情
   * <p>
   * 创建：2001-6-22 14:41:50
   * <p>
   * 修改：晁志平 FOR V30
   * <p>
   * 修改：晁志平 FOR V53
   * <p>
   * 修改：为支持期初暂估应付，本方法调整为静态方法，
   * <p>
   * 参数0：CircularlyAccessibleValueObject[] voa, 要参与运算的VO[]
   * <p>
   * 参数1：String strDisCntName = "应税内含";
   * <p>
   * if(m_estimate1VOs[iRowPos].getIdiscounttaxtype().intValue() == 1) s =
   * "应税外加";
   * <p>
   * if(m_estimate1VOs[iRowPos].getIdiscounttaxtype().intValue() == 2) s =
   * "不计税";
   * <p>
   * 参数2：BillCardPanel bcp: getBillCardPanel()
   * <p>
   * 参数3：BillEditEvent event
   * 
   * @修改人 zhf
   * @修改时间 2008-06-27
   * @修改说明 支持暂估时暂估应付支持外币 （本币信息置为不可编辑 编辑原币后带出本币 将计算公式改为原币间联动计算）
   */
  public static void computeOrgBodyData(CircularlyAccessibleValueObject[] voa, String strDisCntName, BillCardPanel bcp,
      BillEditEvent event, ToftPanel panelThis) {

    int iRowPos = event.getRow();
    String key = event.getKey().trim();
    if (iRowPos < 0 || voa == null || voa.length == 0) {
      return;
    }
    // 注意：本方法仅适用于手工暂估&期初暂估的暂估界面联运运算!
    if (!(voa instanceof EstimateVO[]) && !(voa instanceof OorderVO[])) {
      MessageDialog.showErrorDlg(panelThis, "提示", "不支持的调用!");/*-=notranslate=-*/
      return;
    }
    // 区分调用者
    boolean bEstFlag = (voa instanceof EstimateVO[]);
    // 金额
    Object oMoney = bcp.getBillModel().getValueAt(iRowPos, "noriginalcurmny");
    UFDouble ufdMoney = PuPubVO.getUFDouble_NullAsZero(oMoney);
    // 价税合计
    Object oTotalMoney = bcp.getBillModel().getValueAt(iRowPos, "noriginaltaxpricemny");
    UFDouble ufdTotalMoney = PuPubVO.getUFDouble_NullAsZero(oTotalMoney);

    // 设置公共算法计算标志（是原币计算还是本币计算）
    bcp.getBillModel().setValueAt(new UFBoolean(true), iRowPos, "bisOriCal");

    // 公共算法描述符定义
    int[] descriptions = new int[] {
        RelationsCal.DISCOUNT_TAX_TYPE_NAME, // 扣税类别名(应税内含，应税外加，不计税)
        RelationsCal.DISCOUNT_TAX_TYPE_KEY,// 扣税类别
        RelationsCal.NUM,// 主数量
        RelationsCal.NET_PRICE_ORIGINAL,// 净单价
        RelationsCal.MONEY_ORIGINAL,// 金额
        RelationsCal.NET_TAXPRICE_ORIGINAL,// 净含税单价
        RelationsCal.SUMMNY_ORIGINAL, // 价税合计 --原币
        RelationsCal.TAXRATE, // 税率
        RelationsCal.DISCOUNT_RATE,// 扣率
        RelationsCal.PRICE_ORIGINAL,// 单价
        RelationsCal.TAXPRICE_ORIGINAL,// 含税单价
        RelationsCal.TAX_ORIGINAL
    // 税额
    };
    // 数量itemkey
    String strNumKey = "ngaugenum";
    if (bEstFlag) {
      strNumKey = "ninnum";
    }
    // 参与运算的itemkey(与描述符对应)
    String[] keys = new String[] {
        strDisCntName, "idiscounttaxtype", strNumKey, "noriginalnetprice", "noriginalcurmny", "norgnettaxprice",
        "noriginaltaxpricemny", "ntaxrate", "ndiscountrate", "nnetprice", "nnettaxprice", "norgtaxmoney"
    };
    // 符号一致性检查
    if (key != null && "noriginalcurmny".equalsIgnoreCase(key.trim())) {
      if (bEstFlag) {
        if (((EstimateVO[]) voa)[iRowPos].getNinnum() != null
            && ufdMoney.multiply(((EstimateVO[]) voa)[iRowPos].getNinnum()).doubleValue() < 0.0) {
          MessageDialog.showErrorDlg(panelThis, nc.ui.ml.NCLangRes.getInstance().getStrByID("40040503",
              "UPP40040503-000022")/* @res "修改金额" */, nc.ui.ml.NCLangRes.getInstance().getStrByID("40040503",
              "UPP40040503-000023")/* @res "修改金额不能改变符号！" */);
          bcp.setBodyValueAt(event.getOldValue(), iRowPos, "noriginalcurmny");
          return;
        }
      }
      else {
        if (((OorderVO[]) voa)[iRowPos].getNgaugenum() != null
            && ufdMoney.multiply(((OorderVO[]) voa)[iRowPos].getNgaugenum()).doubleValue() < 0.0) {
          MessageDialog.showErrorDlg(panelThis, nc.ui.ml.NCLangRes.getInstance().getStrByID("40040503",
              "UPP40040503-000022")/* @res "修改金额" */, nc.ui.ml.NCLangRes.getInstance().getStrByID("40040503",
              "UPP40040503-000023")/* @res "修改金额不能改变符号！" */);
          bcp.setBodyValueAt(event.getOldValue(), iRowPos, "noriginalcurmny");
          return;
        }
      }
    }
    else if (key != null && "noriginaltaxpricemny".equalsIgnoreCase(key.trim())) {
      if (bEstFlag) {
        if (((EstimateVO[]) voa)[iRowPos].getNinnum() != null
            && ufdTotalMoney.multiply(((EstimateVO[]) voa)[iRowPos].getNinnum()).doubleValue() < 0.0) {
          MessageDialog.showErrorDlg(panelThis, nc.ui.ml.NCLangRes.getInstance().getStrByID("40040503",
              "UPP40040503-000022")/* @res "修改金额" */, nc.ui.ml.NCLangRes.getInstance().getStrByID("40040503",
              "UPP40040503-000023")/* @res "修改金额不能改变符号！" */);
          bcp.setBodyValueAt(event.getOldValue(), iRowPos, "noriginaltaxpricemny");
          return;
        }
      }
      else {
        if (((OorderVO[]) voa)[iRowPos].getNgaugenum() != null
            && ufdTotalMoney.multiply(((OorderVO[]) voa)[iRowPos].getNgaugenum()).doubleValue() < 0.0) {
          MessageDialog.showErrorDlg(panelThis, nc.ui.ml.NCLangRes.getInstance().getStrByID("40040503",
              "UPP40040503-000022")/* @res "修改金额" */, nc.ui.ml.NCLangRes.getInstance().getStrByID("40040503",
              "UPP40040503-000023")/* @res "修改金额不能改变符号！" */);
          bcp.setBodyValueAt(event.getOldValue(), iRowPos, "noriginaltaxpricemny");
          return;
        }
      }
    }
    // 调用公用算法计算准备：税率为空时设置为0
    Object oTaxRate = bcp.getBillModel().getValueAt(iRowPos, "ntaxrate");
    if (oTaxRate == null || oTaxRate.toString().trim().equals("")) {
      bcp.getBillModel().setValueAt(new UFDouble(0.0), iRowPos, "ntaxrate");
    }
    // 调用公用算法计算
    if (bEstFlag) {
      RelationsCal.calculate(bcp,event, bcp.getBillModel(), new int[] {
        PuTool.getPricePriorPolicy(ClientEnvironment.getInstance().getCorporation().getPk_corp())
      }, descriptions, keys, EstimateVO.class.getName());
    }
    else {
      RelationsCal.calculate(bcp,event, bcp.getBillModel(), new int[] {
        PuTool.getPricePriorPolicy(ClientEnvironment.getInstance().getCorporation().getPk_corp())
      }, descriptions, keys, OorderVO.class.getName());
    }
    // 计算后处理，将null处理成0
    Object oGaugenum = bcp.getBillModel().getValueAt(iRowPos, "ngaugenum");
    voa[iRowPos].setAttributeValue("ngaugenum", PuPubVO.getUFDouble_NullAsZero(oGaugenum));
    Object oPrice = bcp.getBillModel().getValueAt(iRowPos, "noriginalnetprice");
    voa[iRowPos].setAttributeValue("noriginalnetprice", PuPubVO.getUFDouble_NullAsZero(oPrice));
    oMoney = bcp.getBillModel().getValueAt(iRowPos, "noriginalcurmny");
    voa[iRowPos].setAttributeValue("noriginalcurmny", PuPubVO.getUFDouble_NullAsZero(oMoney));
    Object oTaxPrice = bcp.getBillModel().getValueAt(iRowPos, "norgnettaxprice");
    voa[iRowPos].setAttributeValue("norgnettaxprice", PuPubVO.getUFDouble_NullAsZero(oTaxPrice));
    oTaxRate = bcp.getBillModel().getValueAt(iRowPos, "ntaxrate");
    voa[iRowPos].setAttributeValue("ntaxrate", PuPubVO.getUFDouble_NullAsZero(oTaxRate));
    oTotalMoney = bcp.getBillModel().getValueAt(iRowPos, "noriginaltaxpricemny");
    voa[iRowPos].setAttributeValue("noriginaltaxpricemny", PuPubVO.getUFDouble_NullAsZero(oTotalMoney));
    //
    return;
  }

  /**
   * 功能描述：修改单价,金额等的联运运算{注意：本方法仅适用于手工暂估&期初暂估的暂估界面联运运算!}
   * <p>
   * 参数：
   * <p>
   * 返回：
   * <p>
   * 作者：熊海情
   * <p>
   * 创建：2001-6-22 14:41:50
   * <p>
   * 修改：晁志平 FOR V30
   * <p>
   * 修改：晁志平 FOR V53
   * <p>
   * 修改：为支持期初暂估应付，本方法调整为静态方法，
   * <p>
   * 参数0：CircularlyAccessibleValueObject[] voa, 要参与运算的VO[]
   * <p>
   * 参数1：String strDisCntName = "应税内含";
   * <p>
   * if(m_estimate1VOs[iRowPos].getIdiscounttaxtype().intValue() == 1) s =
   * "应税外加";
   * <p>
   * if(m_estimate1VOs[iRowPos].getIdiscounttaxtype().intValue() == 2) s =
   * "不计税";
   * <p>
   * 参数2：BillCardPanel bcp: getBillCardPanel()
   * <p>
   * 参数3：BillEditEvent event
   */
  public static void computeBodyData(CircularlyAccessibleValueObject[] voa, String strDisCntName, BillCardPanel bcp,
      BillEditEvent event, ToftPanel panelThis) {

    int iRowPos = event.getRow();
    if (iRowPos < 0 || voa == null || voa.length == 0) {
      return;
    }
    // 注意：本方法仅适用于手工暂估&期初暂估的暂估界面联运运算!
    if (!(voa instanceof EstimateVO[]) && !(voa instanceof OorderVO[])) {
      MessageDialog.showErrorDlg(panelThis, "提示", "不支持的调用!");/*-=notranslate=-*/
      return;
    }
    // 区分调用者
    boolean bEstFlag = (voa instanceof EstimateVO[]);
    // 金额
    Object oMoney = bcp.getBillModel().getValueAt(iRowPos, "nmoney");
    UFDouble ufdMoney = PuPubVO.getUFDouble_NullAsZero(oMoney);
    // 价税合计
    Object oTotalMoney = bcp.getBillModel().getValueAt(iRowPos, "ntotalmoney");
    UFDouble ufdTotalMoney = PuPubVO.getUFDouble_NullAsZero(oTotalMoney);

    // 设置公共算法计算标志（是原币计算还是本币计算）
    bcp.getBillModel().setValueAt(new UFBoolean(false), iRowPos, "bisOriCal");

    // 公共算法描述符定义
    int[] descriptions = new int[] {
        RelationsCal.DISCOUNT_TAX_TYPE_NAME, // 扣税类别名(应税内含，应税外加，不计税)
        RelationsCal.DISCOUNT_TAX_TYPE_KEY,// 扣税类别
        RelationsCal.NUM,// 主数量
        RelationsCal.NET_PRICE_ORIGINAL,// 净单价
        RelationsCal.MONEY_ORIGINAL,// 金额
        RelationsCal.NET_TAXPRICE_ORIGINAL,// 净含税单价
        RelationsCal.SUMMNY_ORIGINAL, // 价税合计 --原币
        RelationsCal.TAXRATE, // 税率
        RelationsCal.DISCOUNT_RATE,// 扣率
        RelationsCal.PRICE_ORIGINAL,// 单价
        RelationsCal.TAXPRICE_ORIGINAL,// 含税单价
        RelationsCal.TAX_ORIGINAL
    // 税额
    };
    // 数量itemkey
    String strNumKey = "ngaugenum";
    if (bEstFlag) {
      strNumKey = "ninnum";
    }
    // 参与运算的itemkey(与描述符对应)
    String[] keys = new String[] {
        strDisCntName, "idiscounttaxtype", strNumKey, "nprice", "nmoney", "ntaxprice", "ntotalmoney", "ntaxrate",
        "ndiscountrate", "nnetprice", "nnettaxprice", "ntaxmoney"
    };
    // 符号一致性检查
    if (event.getKey() != null && "nmoney".equalsIgnoreCase(event.getKey().trim())) {
      if (bEstFlag) {
        if (((EstimateVO[]) voa)[iRowPos].getNinnum() != null
            && ufdMoney.multiply(((EstimateVO[]) voa)[iRowPos].getNinnum()).doubleValue() < 0.0) {
          MessageDialog.showErrorDlg(panelThis, nc.ui.ml.NCLangRes.getInstance().getStrByID("40040503",
              "UPP40040503-000022")/* @res "修改金额" */, nc.ui.ml.NCLangRes.getInstance().getStrByID("40040503",
              "UPP40040503-000023")/* @res "修改金额不能改变符号！" */);
          bcp.setBodyValueAt(event.getOldValue(), iRowPos, "nmoney");
          return;
        }
      }
      else {
        if (((OorderVO[]) voa)[iRowPos].getNgaugenum() != null
            && ufdMoney.multiply(((OorderVO[]) voa)[iRowPos].getNgaugenum()).doubleValue() < 0.0) {
          MessageDialog.showErrorDlg(panelThis, nc.ui.ml.NCLangRes.getInstance().getStrByID("40040503",
              "UPP40040503-000022")/* @res "修改金额" */, nc.ui.ml.NCLangRes.getInstance().getStrByID("40040503",
              "UPP40040503-000023")/* @res "修改金额不能改变符号！" */);
          bcp.setBodyValueAt(event.getOldValue(), iRowPos, "nmoney");
          return;
        }
      }
    }
    else if (event.getKey() != null && "ntotalmoney".equalsIgnoreCase(event.getKey().trim())) {
      if (bEstFlag) {
        if (((EstimateVO[]) voa)[iRowPos].getNinnum() != null
            && ufdTotalMoney.multiply(((EstimateVO[]) voa)[iRowPos].getNinnum()).doubleValue() < 0.0) {
          MessageDialog.showErrorDlg(panelThis, nc.ui.ml.NCLangRes.getInstance().getStrByID("40040503",
              "UPP40040503-000022")/* @res "修改金额" */, nc.ui.ml.NCLangRes.getInstance().getStrByID("40040503",
              "UPP40040503-000023")/* @res "修改金额不能改变符号！" */);
          bcp.setBodyValueAt(event.getOldValue(), iRowPos, "ntotalmoney");
          return;
        }
      }
      else {
        if (((OorderVO[]) voa)[iRowPos].getNgaugenum() != null
            && ufdTotalMoney.multiply(((OorderVO[]) voa)[iRowPos].getNgaugenum()).doubleValue() < 0.0) {
          MessageDialog.showErrorDlg(panelThis, nc.ui.ml.NCLangRes.getInstance().getStrByID("40040503",
              "UPP40040503-000022")/* @res "修改金额" */, nc.ui.ml.NCLangRes.getInstance().getStrByID("40040503",
              "UPP40040503-000023")/* @res "修改金额不能改变符号！" */);
          bcp.setBodyValueAt(event.getOldValue(), iRowPos, "ntotalmoney");
          return;
        }
      }
    }
    // 调用公用算法计算准备：税率为空时设置为0
    Object oTaxRate = bcp.getBillModel().getValueAt(iRowPos, "ntaxrate");
    if (oTaxRate == null || oTaxRate.toString().trim().equals("")) {
      bcp.getBillModel().setValueAt(new UFDouble(0.0), iRowPos, "ntaxrate");
    }
    // 调用公用算法计算
    if (bEstFlag) {
      RelationsCal.calculate(bcp,event, bcp.getBillModel(), new int[] {
        PuTool.getPricePriorPolicy(ClientEnvironment.getInstance().getCorporation().getPk_corp())
      }, descriptions, keys, EstimateVO.class.getName());
    }
    else {
      RelationsCal.calculate(bcp,event, bcp.getBillModel(), new int[] {
        PuTool.getPricePriorPolicy(ClientEnvironment.getInstance().getCorporation().getPk_corp())
      }, descriptions, keys, OorderVO.class.getName());
    }
    // 计算后处理，将null处理成0
    Object oGaugenum = bcp.getBillModel().getValueAt(iRowPos, "ngaugenum");
    voa[iRowPos].setAttributeValue("ngaugenum", PuPubVO.getUFDouble_NullAsZero(oGaugenum));
    Object oPrice = bcp.getBillModel().getValueAt(iRowPos, "nprice");
    voa[iRowPos].setAttributeValue("nprice", PuPubVO.getUFDouble_NullAsZero(oPrice));
    oMoney = bcp.getBillModel().getValueAt(iRowPos, "nmoney");
    voa[iRowPos].setAttributeValue("nmoney", PuPubVO.getUFDouble_NullAsZero(oMoney));
    Object oTaxPrice = bcp.getBillModel().getValueAt(iRowPos, "ntaxprice");
    voa[iRowPos].setAttributeValue("ntaxprice", PuPubVO.getUFDouble_NullAsZero(oTaxPrice));
    oTaxRate = bcp.getBillModel().getValueAt(iRowPos, "ntaxrate");
    voa[iRowPos].setAttributeValue("ntaxrate", PuPubVO.getUFDouble_NullAsZero(oTaxRate));
    oTotalMoney = bcp.getBillModel().getValueAt(iRowPos, "ntotalmoney");
    voa[iRowPos].setAttributeValue("ntotalmoney", PuPubVO.getUFDouble_NullAsZero(oTotalMoney));
    //
    return;
  }

  /**
   * 功能描述：获得要打印的字段主键 参数： 返回： 作者：熊海情 创建：2001-6-22 14:41:50 修改：晁志平 FOR V30
   */
  public String[] getAllDataItemExpress() {
    BillItem bodyItems[] = getBillCardPanel().getBodyShowItems();

    Vector v = new Vector();
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
   * 功能描述：获得要打印的字段名称 参数： 返回： 作者：熊海情 创建：2001-6-22 14:41:50 修改：晁志平 FOR V30
   */
  public String[] getAllDataItemNames() {
    BillItem bodyItems[] = getBillCardPanel().getBodyShowItems();

    Vector v = new Vector();
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
   * 功能描述：获得单据卡片模板控件 参数： 返回： 作者：熊海情 创建：2001-6-22 14:41:50 修改：晁志平 FOR V30
   */
  private BillCardPanel getBillCardPanel() {
    if (m_billPanel == null) {
      try {
        m_billPanel = new BillCardPanel();
        // user code begin {1}
        // 加载模板
        BillData bd = new BillData(m_billPanel.getTempletData("40040503010000000000"));

        // m_billPanel.loadTemplet("40040503010000000000");
        
        //若未暂估应付则不显示暂估应付页签
        if(!m_bZGYF.booleanValue())
          bd.removeTabItems(BillData.BODY, "zgyf_table");
        
        bd = initDecimal(bd);

        m_billPanel.setBillData(bd);
        //m_billPanel.setShowThMark(true);
        m_billPanel.setTatolRowShow(true);
        m_billPanel.setTatolRowShow("zgyf_table",true);

        // 增加单据编辑监听
        m_billPanel.addEditListener(this);
        m_billPanel.addBodyEditListener2(this);
        // 屏蔽所有页签的右键菜单 V55 by zhaoyha at 2008.9.25
        for (String strTableCode : m_billPanel.getBillData().getTableCodes(BillItem.BODY)) {
          m_billPanel.setBodyMenuShow(strTableCode, false);
        }

        m_billPanel.getBillTable().getSelectionModel().addListSelectionListener(this);

        // since v53, 响应点击选中号监听
        m_billPanel.getBodyPanel().getRowNOTable().setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        m_billPanel.getBodyPanel().getRowNOTable().getSelectionModel().addListSelectionListener(this);
        //
        m_billPanel.getBodyPanel().getRowNOTable().addMouseListener(this);
        m_billPanel.getBillModel().addSortRelaObjectListener2(this);

        // 调整选中模式 V55 by zhaoyha at 2008.9.25
        PuTool.setLineSelected(m_billPanel);
        m_billPanel.setBodyMultiSelect(true);

        // since v55, 选中复选框事件监听
        m_billPanel.getBillModel().addRowStateChangeEventListener(this);

        new DefaultCurrTypeBizDecimalListener(m_billPanel.getBillModel(), "currencytypeid", new String[] {
            "noriginaltaxpricemny", "noriginalcurmny", "norgtaxmoney"
        });
      }
      catch (java.lang.Throwable exception) {
        SCMEnv.out(exception);
        MessageDialog.showErrorDlg(this,
            nc.ui.ml.NCLangRes.getInstance().getStrByID("40040503", "UPP40040503-000024")/*
                                                                                           * @res
                                                                                           * "加载模板"
                                                                                           */,
            nc.ui.ml.NCLangRes.getInstance().getStrByID("40040503", "UPP40040503-000025")/*
                                                                                           * @res
                                                                                           * "模板不存在!"
                                                                                           */);
        return null;

      }
    }
    return m_billPanel;
  }

  /**
   * 功能描述： 参数： 返回： 作者：熊海情 创建：2001-6-22 14:41:50 修改：晁志平 FOR V30
   */
  public String[] getDependentItemExpressByExpress(String itemName) {
    return null;
  }

  /**
   * 功能描述：获得要打印的字段的值 参数： 返回： 作者：熊海情 创建：2001-6-22 14:41:50 修改：晁志平 FOR V30
   */
  public String[] getItemValuesByExpress(String itemKey) {
    itemKey = itemKey.trim();

    if (m_sZG.equals("N")) {
      if (m_estimate1VOs != null && m_estimate1VOs.length > 0) {
        String sValues[] = new String[m_estimate1VOs.length];
        for (int i = 0; i < m_estimate1VOs.length; i++) {
          Object o = getBillCardPanel().getBodyValueAt(i, itemKey);
          if (o != null)
            sValues[i] = o.toString();
          else
            sValues[i] = "";
        }
        return sValues;
      }

    }
    else {
      if (m_estimate2VOs != null && m_estimate2VOs.length > 0) {
        String sValues[] = new String[m_estimate2VOs.length];
        for (int i = 0; i < m_estimate2VOs.length; i++) {
          Object o = getBillCardPanel().getBodyValueAt(i, itemKey);
          if (o != null)
            sValues[i] = o.toString();
          else
            sValues[i] = "";
        }
        return sValues;
      }
    }

    return null;
  }

  /**
   * 功能描述：获得打印模板名称 参数： 返回： 作者：熊海情 创建：2001-6-22 14:41:50 修改：晁志平 FOR V30
   */
  public String getModuleName() {
    return "4004050301";
  }

  /**
   * 子类实现该方法，返回业务界面的标题。
   * 
   * @version (00-6-6 13:33:25)
   * @return java.lang.String
   */
  public String getTitle() {
    return nc.ui.ml.NCLangRes.getInstance().getStrByID("4004050301", "UPT4004050301-000002")/*
                                                                                             * @res
                                                                                             * "暂估处理"
                                                                                             */;
  }

  /**
   * 功能描述：初始化 参数： 返回： 作者：熊海情 创建：2001-6-22 14:41:50 修改：2004-09-10 袁野
   */
  public void init() {
    String strErrInf = initpara();
    if (strErrInf != null) {
      MessageDialog.showErrorDlg(this,
          nc.ui.ml.NCLangRes.getInstance().getStrByID("40040503", "UPP40040503-000028")/*
                                                                                         * @res
                                                                                         * "获取系统初始化参数出错"
                                                                                         */,
          strErrInf);
      return;
    }
    // 显示按钮
    m_buttons = new ButtonObject[10];  //扩大按钮数组，增加费用分摊按钮 add by QuSida 2010-9-15 (佛山骏杰)
    m_buttons[0] = new ButtonObject(nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UC001-0000041")/*
                                                                                                           * @res
                                                                                                           * "全选"
                                                                                                           */, nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UC001-0000041")/*
                                                                                 * @res
                                                                                 * "全选"
                                                                                 */, 2, "全选");
    m_buttons[1] = new ButtonObject(nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UC001-0000042")/*
                                                                                                           * @res
                                                                                                           * "全消"
                                                                                                           */, nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UC001-0000042")/*
                                                                                 * @res
                                                                                 * "全消"
                                                                                 */, 2, "全消");
    m_buttons[2] = new ButtonObject(
        nc.ui.ml.NCLangRes.getInstance().getStrByID("4004050301", "UPT4004050301-000003")/*
                                                                                           * @res
                                                                                           * "暂估"
                                                                                           */,
        nc.ui.ml.NCLangRes.getInstance().getStrByID("4004050301", "UPT4004050301-000003")/*
                                                                                           * @res
                                                                                           * "暂估"
                                                                                           */, 2, "暂估");
    m_buttons[3] = new ButtonObject(
        nc.ui.ml.NCLangRes.getInstance().getStrByID("4004050301", "UPT4004050301-000004")/*
                                                                                           * @res
                                                                                           * "反暂"
                                                                                           */,
        nc.ui.ml.NCLangRes.getInstance().getStrByID("4004050301", "UPT4004050301-000004")/*
                                                                                           * @res
                                                                                           * "反暂"
                                                                                           */, 2, "反暂");
    m_buttons[4] = new ButtonObject(nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UC001-0000006")/*
                                                                                                           * @res
                                                                                                           * "查询"
                                                                                                           */, nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UC001-0000006")/*
                                                                                 * @res
                                                                                 * "查询"
                                                                                 */, 2, "查询");
    m_buttons[5] = new ButtonObject(
        nc.ui.ml.NCLangRes.getInstance().getStrByID("4004050301", "UPT4004050301-000005")/*
                                                                                           * @res
                                                                                           * "打印预览"
                                                                                           */,
        nc.ui.ml.NCLangRes.getInstance().getStrByID("4004050301", "UPT4004050301-000005")/*
                                                                                           * @res
                                                                                           * "打印预览"
                                                                                           */, 2, "打印预览");
    m_buttons[6] = new ButtonObject(nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UC001-0000007")/*
                                                                                                           * @res
                                                                                                           * "打印"
                                                                                                           */, nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UC001-0000007")/*
                                                                                 * @res
                                                                                 * "打印"
                                                                                 */, 2, "打印");
    m_buttons[7] = new ButtonObject(
        nc.ui.ml.NCLangRes.getInstance().getStrByID("4004050301", "UPT4004050301-000007")/*
                                                                                           * @res
                                                                                           * "优质优价计算"
                                                                                           */,
        nc.ui.ml.NCLangRes.getInstance().getStrByID("4004050301", "UPT4004050301-000007")/*
                                                                                           * @res
                                                                                           * "优质优价计算"
                                                                                           */, 2,
        "优质优价计算");
    m_buttons[8] = new ButtonObject(
        nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "SCMCOMMON000000019")/* @res"联查" */, nc.ui.ml.NCLangRes
            .getInstance().getStrByID("common", "SCMCOMMON000000019")/* @res"联查" */, 2, "联查"); /*-=notranslate=-*/
    
    m_buttons[9] = new ButtonObject("费用暂估","费用暂估","费用暂估");//费用暂估按钮 add by QuSida (佛山骏杰) 2010-9-15
    
    this.setButtons(m_buttons);
    
   

    // 显示单据
    setLayout(new java.awt.BorderLayout());
    add(getBillCardPanel(), java.awt.BorderLayout.CENTER);
    getBillCardPanel().setEnabled(false);

    // 扣税类别 0应税内含 1应税外加 2不计税
    UIComboBox comItem = (UIComboBox) getBillCardPanel().getBodyItem("idiscounttaxtype").getComponent();
    getBillCardPanel().getBodyItem("idiscounttaxtype").setWithIndex(true);
    comItem.addItem(nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPP40040401-000105")/*
                                                                                                   * @res
                                                                                                   * "应税内含"
                                                                                                   */);
    comItem.addItem(nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPP40040401-000106")/*
                                                                                                   * @res
                                                                                                   * "应税外加"
                                                                                                   */);
    comItem.addItem(nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPP40040401-000107")/*
                                                                                                   * @res
                                                                                                   * "不计税"
                                                                                                   */);
    comItem.setTranslate(true);

    // 初始按钮状态
    m_nButtonState = new int[m_buttons.length];

    // 设置除查询外的所有按钮为灰
    for (int i = 0; i < 5; i++) {
      m_nButtonState[i] = 1;
    }
    m_nButtonState[4] = 0;
    m_nButtonState[5] = 0;
    m_nButtonState[6] = 0;
    m_nButtonState[7] = 0;
    m_nButtonState[9] = 0; //费用暂估
    changeButtonState();

  }

  // /**
  // *
  // * @作者 zhf
  // * @创建时间：2008-7-4上午09:21:42
  // * @return void
  // * @说明：(for v5.5)
  // * @修改者：
  // * @修改时间：
  // * @修改说明：
  // */
  //
  // public void initRefPane(){
  // UIRefPane refpane = (UIRefPane)
  // getBillCardPanel().getBodyItem("cfycode").getComponent();
  // refpane.setReturnCode(false);
  //  
  // refpane.setWhereString(" B.pk_corp = '"+m_sUnitCode.trim()+"'
  // A.pk_invbasdoc = B.pk_invbasdoc and B.pk_invbasdoc=C.cbaseid " +
  // "and laborflag='Y' and B.sealflag = 'N' and isnull(C.dr,0)=0 and
  // isnull(A.dr,0)=0 and isnull(B.dr,0)=0 ");
  // // new PubDMO().queryHtResultFromAnyTable(sTable, sFieldName, saFields,
  // saId)
  // }
  /**
   * 功能描述:初始化小数位 参数： 返回： 作者：熊海情 创建：2001-6-22 14:41:50 修改：2004-09-10 袁野
   */
  private BillData initDecimal(BillData bd) {
    // 获得系统初始化参数

    if (m_measure == null || m_measure.length == 0) {
      MessageDialog.showErrorDlg(this,
          nc.ui.ml.NCLangRes.getInstance().getStrByID("40040503", "UPP40040503-000026")/*
                                                                                         * @res
                                                                                         * "获得系统初始化参数"
                                                                                         */,
          NCLangRes.getInstance().getStrByID("common", "MT7", null, new String[] {
            "本位币"
          }));/* MT7=获取{0}失败 */
      ;
      return null;
    }

    // 获得系统初始化参数
    // 获得主计量小数位
    int nMeasDecimal = m_measure[0];
    // 获得采购单价小数位
    int nPriceDecimal = m_measure[1];
    // 获得采购金额小数位
    int nMoneyDecimal = m_measure[2];

    // 获得单据元素对应的控件,并修改控件的属性
    BillItem item1 = bd.getBodyItem("nprice");
    item1.setDecimalDigits(nPriceDecimal);

    item1 = bd.getBodyItem("ntaxprice");
    item1.setDecimalDigits(nPriceDecimal);

    BillItem item2 = bd.getBodyItem("ninnum");
    item2.setDecimalDigits(nMeasDecimal);

    BillItem item3 = bd.getBodyItem("nsettlenum");
    item3.setDecimalDigits(nMeasDecimal);

    BillItem item4 = bd.getBodyItem("nmoney");
    item4.setDecimalDigits(nMoneyDecimal);

    BillItem item5 = bd.getBodyItem("nsettlemny");
    item5.setDecimalDigits(nMoneyDecimal);

    item5 = bd.getBodyItem("ntotalmoney");
    item5.setDecimalDigits(nMoneyDecimal);

    item5 = bd.getBodyItem("ntaxmoney");
    item5.setDecimalDigits(nMoneyDecimal);

    // zhf add for ha 2008-05-16 支持外币
    BillItem item6 = bd.getBodyItem("noriginalnetprice");
    if (item6 != null) {
      item6.setDecimalDigits(nPriceDecimal);
    }

    item6 = bd.getBodyItem("norgnettaxprice");
    if (item6 != null) {
      item6.setDecimalDigits(nPriceDecimal);
    }

    BillItem item7 = bd.getBodyItem("noriginalcurmny");
    if (item7 != null) {
      item7.setDecimalDigits(nMoneyDecimal);
    }

    item7 = bd.getBodyItem("noriginaltaxpricemny");
    if (item7 != null) {
      item7.setDecimalDigits(nMoneyDecimal);
    }

    // 支持费用暂估------费用金额 zhf 2008/06/24
    item7 = bd.getBodyItem("nfeemny");
    if (item7 != null) {
      item7.setDecimalDigits(nMoneyDecimal);
    }

    // ------ by zhaoyha at 2008.10.23 -----------------------
    // 支持暂估应付页签的本币
    BillItem item8 = bd.getBodyItem("nzgyfnotaxprice"); // 应付本币无税单价
    if (item8 != null) {
      item8.setDecimalDigits(nPriceDecimal);
    }
    item8 = bd.getBodyItem("nzgyfnotaxmoney"); // 应付本币无税金额
    if (item8 != null) {
      item8.setDecimalDigits(nMoneyDecimal);
    }
    item8 = bd.getBodyItem("nzgyfprice"); // 应付本币含税单价
    if (item8 != null) {
      item8.setDecimalDigits(nPriceDecimal);
    }
    item8 = bd.getBodyItem("nzgyfmoney"); // 应付本币含税金额
    if (item8 != null) {
      item8.setDecimalDigits(nMoneyDecimal);
    }

    return bd;
  }

  /**
   * 为了减少初始化时前后台交互的次数，一次性获取多个系统参数 作者:袁野 日期：2004-09-09
   */
  public String initpara() {
    try {
      Object[] objs = null;
      ServcallVO[] scds = new ServcallVO[2];

      // 获得系统初始化参数
      scds[0] = new ServcallVO();
      scds[0].setBeanName("nc.itf.pu.pub.IPub");
      scds[0].setMethodName("getDigitBatch");
      scds[0].setParameter(new Object[] {
          m_sUnitCode, new String[] {
              "BD501", "BD505", "BD301"
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

      objs = nc.ui.scm.service.LocalCallService.callService(scds);

      // 数量单价精度
      m_measure = (int[]) objs[0];
      if (m_measure == null) {
        SCMEnv.out("未获取初始化参数：数量小数位[BD501]、采购/销售单价小数位[BD505], 请检查...");
        return "未获取初始化参数：数量小数位[BD501]、采购/销售单价小数位[BD505], 请检查...";
      }
      // 库存是否启用
      if (objs[1] == null) {
        SCMEnv.out("未获取初始化参数：库存是否启用, 请检查...");
        return "未获取初始化参数：库存是否启用, 请检查...";
      }
      m_bICStartUp = ((UFBoolean) objs[1]).booleanValue();

      // 获得系统设置的暂估方式和差异转入方式
      Hashtable hTemp = SysInitBO_Client.queryBatchParaValues(m_sUnitCode, new String[] {
          "PO12", "PO13", "PO27", "PO52", "BD301"
      });
      if (hTemp == null || hTemp.size() == 0) {
        SCMEnv.out("未获取初始化参数PO12,PO13,PO27,PO52,BD301 请检查...");
        return "未获取初始化参数PO12,PO13,PO27,PO52,BD301 请检查...";
      }
      if (hTemp.get("PO12") == null) {
        SCMEnv.out("未获取初始化参数暂估处理方式[PO12], 请检查...");
        return "未获取初始化参数暂估处理方式[PO12], 请检查...";
      }
      else {
        Object temp = hTemp.get("PO12");
        m_sEstimateMode = temp.toString();
      }

      if (hTemp.get("PO13") == null) {
        SCMEnv.out("未获取初始化参数差异转入方式[PO13], 请检查...");
        return "未获取初始化参数差异转入方式[PO13], 请检查...";
      }
      else {
        Object temp = hTemp.get("PO12");
        m_sDiffMode = temp.toString();
      }

      if (hTemp.get("PO27") == null) {
        SCMEnv.out("未获取初始化参数暂估单价来源[PO27], 请检查...");
        return "未获取初始化参数暂估单价来源[PO27], 请检查...";
      }
      else {
        Object temp = hTemp.get("PO27");
        m_sEstPriceSource = temp.toString();
      }

      if (hTemp.get("PO52") == null) {
        SCMEnv.out("未获取初始化参数:入库单暂估时是否暂估应付[PO52], 请检查...");
        return "未获取初始化参数:入库单暂估时是否暂估应付[PO52], 请检查...";
      }
      else {
        Object temp = hTemp.get("PO52");
        m_bZGYF = new UFBoolean(temp.toString());
      }

      if (hTemp.get("BD301") == null) {
        SCMEnv.out("未获取初始化参数：本位币[BD301], 请检查...");
        return "未获取初始化参数：本位币[BD301], 请检查...";
      }
      else {
        Object temp = hTemp.get("BD301");
        m_sCurrTypeID = temp.toString();
      }
    }
    catch (Exception e) {
      SCMEnv.out(e);
      return e.getMessage();
    }
    return null;
  }

  /**
   * 查询模版初始化 作者:袁野 日期：2004-09-10
   */
  public void initQueryModel() {
    if (m_condClient == null) {
      // 初始化查询模板
      m_condClient = new EstimateQueryCondition(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("4004050301",
          "UPP4004050301-000004")/*
                                   * @res "暂估入库单查询"
                                   */);
      m_condClient.setTempletID(m_sUnitCode, "4004050301", getClientEnvironment().getUser().getPrimaryKey(), null,
          "PS03");

      changeQueryModelLayout();
      m_condClient.setValueRef("dbilldate", "日历");
      m_condClient.setValueRef("cbillmaker", "操作员");

      // 部门名称
      UIRefPane deptRef = new UIRefPane();
      deptRef.setRefNodeName("部门档案");
      m_condClient.setValueRef("cdeptid", deptRef);
      // 人员名称
      UIRefPane psnRef = new UIRefPane();
      psnRef.setRefNodeName("人员档案");
      m_condClient.setValueRef("coperator", psnRef);

      UIRefPane vendorRef = new UIRefPane();
      vendorRef.setRefNodeName("供应商档案");
      vendorRef.getRefModel().addWherePart(" and frozenflag = 'N'");
      m_condClient.setValueRef("cvendorbaseid", vendorRef);

      UIRefPane biztypeRef = new UIRefPane();
      biztypeRef.setRefNodeName("业务类型");
      m_condClient.setValueRef("cbiztype", biztypeRef);

      m_condClient.setDefaultValue("dbilldate", "dbilldate", getClientEnvironment().getDate().toString());

      // 加载自定义项名称
      nc.ui.scm.pub.def.DefSetTool.updateQueryConditionClientUserDef(m_condClient, m_sUnitCode, "icbill", "vuserdef",
          "vuserdef");
      m_condClient.setIsWarningWithNoInput(true);
      /* 封存的基础数据能被参照 */
      m_condClient.setSealedDataShow(true);

      // 数据权限控制
      m_condClient.setCorpRefs("B.pk_invoicecorp", new String[] {
          "cvendorbaseid", "cdeptid", "coperator", "invcode", "cwarehouseid", "cprojectid"
      });
//      m_condClient.setRefsDataPowerConVOs(nc.ui.pub.ClientEnvironment.getInstance().getUser().getPrimaryKey(),
//          new String[] {
//            nc.ui.pub.ClientEnvironment.getInstance().getCorporation().getPrimaryKey()
//          }, new String[] {
//              "供应商档案", "部门档案", "人员档案", "存货档案", "仓库档案", "项目管理档案"
//          }, new String[] {
//              "cvendorbaseid", "cdeptid", "coperator", "invcode", "cwarehouseid", "cprojectid"
//          }, new int[] {
//              0, 0, 0, 0, 0, 0
//          });
    }
  }

  /**
   * 功能描述:获得要打印的字段是否为数字 参数： 返回： 作者：熊海情 创建：2001-6-22 14:41:50 修改：晁志平 FOR V30
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
   * 子类实现该方法，响应按钮事件。
   * 
   * @version (00-6-1 10:32:59)
   * @param bo
   *          ButtonObject
   */
  public void onButtonClicked(nc.ui.pub.ButtonObject bo) {
    if (bo == m_buttons[0]) {
      onSelectAll();
      showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "4004COMMON000000033")/*
                                                                                                   * @res
                                                                                                   * "全选成功"
                                                                                                   */);
    }
    else if (bo == m_buttons[1]) {
      onSelectNo();
      showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "4004COMMON000000034")/*
                                                                                                   * @res
                                                                                                   * "全消成功"
                                                                                                   */);
    }
    else if (bo == m_buttons[2]) {
      onEstimate();
    }
    else if (bo == m_buttons[3]) {
      onUnEstimate();
    }
    else if (bo == m_buttons[4]) {
      onQuery();
      showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UCH009")/*
                                                                                       * @res
                                                                                       * "查询完成"
                                                                                       */);
    }
    else if (bo == m_buttons[5]) {
      onPreview();
      showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "4004COMMON000000021")/*
                                                                                                   * @res
                                                                                                   * "打印预览完成"
                                                                                                   */);
    }
    else if (bo == m_buttons[6]) {
      onPrint();
      showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UCH041")/*
                                                                                       * @res
                                                                                       * "打印成功"
                                                                                       */);
    }
    else if (bo == m_buttons[7]) {
      onHQHP();
    }
    else if (bo == m_buttons[8]) {
      onLinkQuery();
      showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "4004COMMON000000019")/*
                                                                                                   * @res
                                                                                                   * "联查成功"
                                                                                                   */);
    }
    else if(bo == m_buttons[9]){
    	onCostDistribute();
    	 showHintMessage("费用暂估");
    }
  }

  /**
   * 单据逐级联查
   */
  private void onLinkQuery() {

    String strBillType = null;
    String strBillHid = null;
    String strBillCode = null;

    int[] iaSelectedRow = getBillCardPanel().getBillTable().getSelectedRows();

    if (iaSelectedRow != null && iaSelectedRow.length > 0) {
      strBillType = ((EstimateVO[]) getRelaSortObjectArray())[iaSelectedRow[0]].getCbilltypecode();
      strBillHid = ((EstimateVO[]) getRelaSortObjectArray())[iaSelectedRow[0]].getCgeneralhid();
      strBillCode = ((EstimateVO[]) getRelaSortObjectArray())[iaSelectedRow[0]].getVbillcode();
    }
    if (strBillHid == null) {
      return;
    }
    SourceBillFlowDlg soureDlg = new SourceBillFlowDlg(this, strBillType, strBillHid, null, getClientEnvironment()
        .getUser().getPrimaryKey(), strBillCode);
    soureDlg.showModal();
  }

  /**
   * 功能描述:暂估 参数： 返回： 作者：熊海情 创建：2001-6-22 14:41:50 修改：晁志平 FOR V30
   */
  public void onEstimate() {

    nc.vo.scm.pu.Timer timer = new nc.vo.scm.pu.Timer();
    timer.start();

    getBillCardPanel().stopEditing();// zhf add

    Integer nSelected[] = null;
    Vector v = new Vector();
    Vector vv = new Vector();
    int nRow = getBillCardPanel().getRowCount();
    for (int i = 0; i < nRow; i++) {
      int nStatus = getBillCardPanel().getBillModel().getRowState(i);
      if (nStatus == BillModel.SELECTED)
        v.addElement(new Integer(i));
      else
        vv.addElement(new Integer(i));
    }
    nSelected = new Integer[v.size()];
    v.copyInto(nSelected);

    if (nSelected == null || nSelected.length == 0) {
      MessageDialog.showErrorDlg(this,
          nc.ui.ml.NCLangRes.getInstance().getStrByID("40040503", "UPP40040503-000030")/*
                                                                                         * @res
                                                                                         * "入库单暂估"
                                                                                         */,
          nc.ui.ml.NCLangRes.getInstance().getStrByID("40040503", "UPP40040503-000031")/*
                                                                                         * @res
                                                                                         * "未选中入库单！"
                                                                                         */);
      return;
    }

    // 更新緩存
    updateEstimate1VO(nSelected);

    Vector<EstimateVO> vTemp = new Vector<EstimateVO>();
    for (int i = 0; i < nSelected.length; i++)
      vTemp.addElement(m_estimate1VOs[nSelected[i].intValue()]);
    EstimateVO VOs[] = new EstimateVO[vTemp.size()];
    vTemp.copyInto(VOs);
    //统一做保存前检查 by zhaoyha at 2009.9
    if(!checkBeforeSave(VOs, nSelected)) return;

    try {
      timer.addExecutePhase("数据库更新$$$$前$$$处理时间#######");

      java.util.ArrayList paraList = new java.util.ArrayList();
      // paraList.add(getClientEnvironment().getUser().getPrimaryKey());
      // paraList.add(getClientEnvironment().getDate());
      paraList.add(new ClientLink(getClientEnvironment()));
      paraList.add(m_sEstimateMode);
      paraList.add(m_sDiffMode);
      paraList.add(m_bZGYF);//将公司级参数PO52: 汇总暂估是否暂估应付 加入参数列表.
      paraList.add(m_sCurrTypeID);
      if(feeFlag){
    	  EstimateHelper.feeEstimate(VOs, paraList);
      }
      else 
    	  EstimateHelper.estimate(VOs, paraList);

      timer.addExecutePhase("数据库更新$$$$操作$$$处理时间#######");
      

    }
    catch (java.sql.SQLException e) {
      MessageDialog.showErrorDlg(this,
          nc.ui.ml.NCLangRes.getInstance().getStrByID("40040503", "UPP40040503-000030")/*
                                                                                         * @res
                                                                                         * "入库单暂估"
                                                                                         */,
          nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000412")/*
                                                                                           * @res
                                                                                           * "SQL语句错误！"
                                                                                           */);
      SCMEnv.out(e);
      return;
    }
    catch (ArrayIndexOutOfBoundsException e) {
      MessageDialog.showErrorDlg(this,
          nc.ui.ml.NCLangRes.getInstance().getStrByID("40040503", "UPP40040503-000030")/*
                                                                                         * @res
                                                                                         * "入库单暂估"
                                                                                         */,
          nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000426")/*
                                                                                           * @res
                                                                                           * "数组越界错误！"
                                                                                           */);
      SCMEnv.out(e);
      return;
    }
    catch (NullPointerException e) {
      MessageDialog.showErrorDlg(this,
          nc.ui.ml.NCLangRes.getInstance().getStrByID("40040503", "UPP40040503-000030")/*
                                                                                         * @res
                                                                                         * "入库单暂估"
                                                                                         */,
          nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000427")/*
                                                                                           * @res
                                                                                           * "空指针错误！"
                                                                                           */);
      SCMEnv.out(e);
      return;
    }
    catch (nc.vo.pub.BusinessException e) {
      MessageDialog.showErrorDlg(this,
          nc.ui.ml.NCLangRes.getInstance().getStrByID("40040503", "UPP40040503-000030")/*
                                                                                         * @res
                                                                                         * "入库单暂估"
                                                                                         */, e
              .getMessage());
      SCMEnv.out(e);
      return;
    }
    catch (Exception e) {
      MessageDialog.showErrorDlg(this,
          nc.ui.ml.NCLangRes.getInstance().getStrByID("40040503", "UPP40040503-000030")/*
                                                                                         * @res
                                                                                         * "入库单暂估"
                                                                                         */, e
              .getMessage());
      SCMEnv.out(e);
      return;
    }
    this
        .showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("40040503", "UPP40040503-000034")/*
                                                                                                       * @res
                                                                                                       * "入库单暂估成功！"
                                                                                                       */);

    // 根据暂估方式，向存货传送数据
    // if(m_sEstimateMode.equals("单到补差")){}
    // else if(m_sEstimateMode.equals("单到回冲")){}

    // 暂估后的入库单不再显示在界面
    if (vv == null || vv.size() == 0) {
      // 所有入库单已暂估完毕
      getBillCardPanel().getBillData().clearViewData();
      getBillCardPanel().updateUI();
      // 除查询外所有为灰
      for (int i = 0; i < 5; i++)
        m_nButtonState[i] = 1;
      m_nButtonState[4] = 0;
      changeButtonState();
      return;
    }

    Vector v0 = new Vector();
    for (int i = 0; i < vv.size(); i++) {
      int n = ((Integer) vv.elementAt(i)).intValue();
      v0.addElement(m_estimate1VOs[n]);
    }
    m_estimate1VOs = new EstimateVO[v0.size()];
    v0.copyInto(m_estimate1VOs);

    getBillCardPanel().getBillModel().setBodyDataVO(m_estimate1VOs);
    getBillCardPanel().getBillModel().execLoadFormula();
    getBillCardPanel().getBillModel().updateValue();
    getBillCardPanel().updateUI();

    // 设置按钮状态：除暂估，反暂，全消外全部为正常
    for (int i = 0; i < 5; i++) {
      m_nButtonState[i] = 0;
    }
    m_nButtonState[1] = 1;
    m_nButtonState[2] = 1;
    m_nButtonState[3] = 1;
    m_nButtonState[7] = 0;
    changeButtonState();
    timer.addExecutePhase("数据库更新$$$$后$$$处理时间#######");
    //
    timer.showAllExecutePhase("暂估处理UI时间分布：");
    feeFlag = false;
  }

  /**
   * 功能描述:打印预览 参数： 返回： 作者：熊海情 创建：2004-3-24 14:41:50 修改：晁志平 FOR V30
   */
  private void onPreview() {
    PrintEntry print = new PrintEntry(null, null);

    print.setTemplateID(m_sUnitCode, "4004050301", getClientEnvironment().getUser().getPrimaryKey(), null);
    int ret = print.selectTemplate();
    if (ret > 0) {
      print.setDataSource(this);
      print.preview();
    }
  }

  /**
   * 功能描述:打印 参数： 返回： 作者：熊海情 创建：2004-3-24 14:41:50 修改：晁志平 FOR V30
   */
  private void onPrint() {
    PrintEntry print = new PrintEntry(null, null);

    print.setTemplateID(m_sUnitCode, "4004050301", getClientEnvironment().getUser().getPrimaryKey(), null);
    int ret = print.selectTemplate();
    if (ret > 0) {
      print.setDataSource(this);
      print.print();
    }
  }

  /*
   * 优质优价计算
   */
  private void onHQHP() {

    if (m_sZG.equals("N") && m_estimate1VOs != null && m_estimate1VOs.length > 0) {
      int nSelected[] = getBillCardPanel().getBillTable().getSelectedRows();
      if (nSelected != null && nSelected.length > 0) {
        // 调用优质优价提供的接口,获取无税单价
        Vector vTemp = new Vector();
        for (int i = 0; i < nSelected.length; i++) {
          int j = nSelected[i];
          PricParaVO tempVO = new PricParaVO();
          tempVO.setCgeneralbid(m_estimate1VOs[j].getCgeneralbid());
          vTemp.addElement(tempVO);
        }
        PricParaVO VOs[] = new PricParaVO[vTemp.size()];
        vTemp.copyInto(VOs);
        try {
          VOs = PricStlHelper.queryPricStlPrices(VOs);
        }
        catch (Exception e) {
          SCMEnv.out(e);
        }

        // 获取无税单价,自动计算其它数据项目
        if (VOs != null && VOs.length > 0) {
          for (int i = 0; i < nSelected.length; i++) {
            int j = nSelected[i];
            if (VOs[i].getNprice() != null && VOs[i].getNtaxprice() != null) {
              /*
               * getBillCardPanel().setBodyValueAt(VOs[i].getNprice(),j,"nprice");
               * BillEditEvent event = new
               * BillEditEvent(getBillCardPanel().getBillTable(),VOs[i].getNprice(),"nprice",j);
               * computeBodyData(event); ntaxpricemny->ninnum*ntaxprice
               */
              m_estimate1VOs[j].setNprice(VOs[i].getNprice().add(new UFDouble(0.0), m_measure[1]));
              getBillCardPanel().getBillModel().setValueAt(m_estimate1VOs[j].getNprice(), j, "nprice");

              m_estimate1VOs[j].setNmoney(VOs[i].getNprice().multiply(m_estimate1VOs[j].getNinnum(), m_measure[2]));
              getBillCardPanel().getBillModel().setValueAt(m_estimate1VOs[j].getNmoney(), j, "nmoney");

              m_estimate1VOs[j].setNtaxprice(VOs[i].getNtaxprice().add(new UFDouble(0.0), m_measure[1]));
              getBillCardPanel().getBillModel().setValueAt(m_estimate1VOs[j].getNtaxprice(), j, "ntaxprice");

              m_estimate1VOs[j].setNtotalmoney(VOs[i].getNtaxprice().multiply(m_estimate1VOs[j].getNinnum(),
                  m_measure[2]));
              getBillCardPanel().getBillModel().setValueAt(m_estimate1VOs[j].getNtotalmoney(), j, "ntotalmoney");
            }
            else {
              SCMEnv.out("优质优价未完全取得价格信息(单价、含税单价)");/*-=notranslate=-*/
            }
          }
        }
      }
    }
  }
  
  /**
   * 功能描述:入库单查询 参数： 返回： 作者：熊海情 创建：2004-3-24 14:41:50 修改：2004-09-10 袁野
   * 修改：2008-06-17 張紅芳 for v55
   */
  public void onQuery() {

    if (!m_bICStartUp) {
      // 库存未启用
      MessageDialog.showErrorDlg(this, 
          NCLangRes.getInstance().getStrByID("40040503", "UPP40040503-000035")/* @res "入库单查询" */,
          NCLangRes.getInstance().getStrByID("40040503","UPP40040503-000036")/* @res "库存未启用，不存在入库单！" */);
      return;
    }

    initQueryModel();

    m_condClient.hideCorp();
    m_condClient.hideUnitButton();
    m_condClient.showModal();
    Timer debugTime = new Timer();
    debugTime.start();
    if (m_condClient.isCloseOK()) {
      // 获取入库单查询条件
      ConditionVO conditionVO[] = m_condClient.getConditionVO();

      // 设置按钮状态：除暂估，反暂，全消外全部为正常
      for (int i = 0; i < 5; i++) {
        m_nButtonState[i] = 0;
      }
      m_nButtonState[1] = 1;
      m_nButtonState[2] = 1;
      m_nButtonState[3] = 1;

      if (m_rbEst.isSelected()) {
        m_sZG = "N";
      }
      else {
        m_sZG = "Y";
      }
      debugTime.addExecutePhase("获取入库单查询条件");
      
      // 查询
      try {
        if (m_sZG.toUpperCase().equals("N")) {
          m_estimate1VOs = EstimateHelper.queryEstimate(m_sUnitCode, conditionVO, m_sZG, m_sEstPriceSource);
          if (m_estimate1VOs == null || m_estimate1VOs.length == 0) {
            MessageDialog.showHintDlg(this,NCLangRes.getInstance().getStrByID("40040503", "UPP40040503-000035")/* @res "入库单查询" */,
                NCLangRes.getInstance().getStrByID("40040503", "UPP40040503-000037")/* @res "没有符合条件的入库单！" */);

            // 清空数据
            getBillCardPanel().getBillModel().clearBodyData();
            getBillCardPanel().updateUI();
            // 全选为空
            m_nButtonState[0] = 1;
            changeButtonState();
            return;
          }
          debugTime.addExecutePhase("查询入库单-未暂估");
        }
        else {
          m_estimate2VOs = EstimateHelper.queryEstimate(m_sUnitCode, conditionVO, m_sZG, m_sEstPriceSource);
          if (m_estimate2VOs == null || m_estimate2VOs.length == 0) {
            MessageDialog.showHintDlg(this,
                NCLangRes.getInstance().getStrByID("40040503","UPP40040503-000035")/* @res "入库单查询" */,
                NCLangRes.getInstance().getStrByID("40040503","UPP40040503-000037")/* @res "没有符合条件的入库单！" */);
            // 清空数据
            getBillCardPanel().getBillModel().clearBodyData();
            getBillCardPanel().updateUI();
            // 全选为空
            m_nButtonState[0] = 1;
            changeButtonState();
            return;
          }
          debugTime.addExecutePhase("查询入库单-已暂估");
        }

        // 加载自由项
        nc.ui.scm.pub.FreeVOParse freeParse = new nc.ui.scm.pub.FreeVOParse();
        Vector<EstimateVO> vTemp = new Vector<EstimateVO>();
        if (m_sZG.toUpperCase().equals("N")) {
          for (int i = 0; i < m_estimate1VOs.length; i++) {
            if (m_estimate1VOs[i].getVfree1() != null || m_estimate1VOs[i].getVfree2() != null || m_estimate1VOs[i].getVfree3() != null || m_estimate1VOs[i].getVfree4() != null || m_estimate1VOs[i].getVfree5() != null) {
              vTemp.addElement(m_estimate1VOs[i]);
            }
          }
          if (vTemp.size() > 0) {
            EstimateVO tempVO[] = new EstimateVO[vTemp.size()];
            vTemp.copyInto(tempVO);
            freeParse.setFreeVO(tempVO, "vfree", "vfree", null, "cmangid",false);
          }
        }
        else {
          for (int i = 0; i < m_estimate2VOs.length; i++) {
            if (m_estimate2VOs[i].getVfree1() != null || m_estimate2VOs[i].getVfree2() != null || m_estimate2VOs[i].getVfree3() != null || m_estimate2VOs[i].getVfree4() != null || m_estimate2VOs[i].getVfree5() != null) {
              vTemp.addElement(m_estimate2VOs[i]);
            }
          }
          if (vTemp.size() > 0) {
            EstimateVO tempVO[] = new EstimateVO[vTemp.size()];
            vTemp.copyInto(tempVO);
            freeParse.setFreeVO(tempVO, "vfree", "vfree", null, "cmangid", false);
          }
        }
        debugTime.addExecutePhase("加载自由项");

        // 设置税率和价税合计, 计算含税单价和价税合计
        vTemp = new Vector<EstimateVO>();
        if ("N".equalsIgnoreCase(m_sZG)) {
          for (int i = 0; i < m_estimate1VOs.length; i++) {
            m_estimate1VOs[i].setNdiscountrate(new UFDouble(100));
          }
        }
        else {
          for (int i = 0; i < m_estimate2VOs.length; i++) {
            // 恒安项目问题，查询已经暂估过的数据未显示税额
            m_estimate2VOs[i].setAttributeValue("ntaxmoney", 
                PuPubVO.getUFDouble_NullAsZero(m_estimate2VOs[i].getNtotalmoney()).sub(PuPubVO.getUFDouble_NullAsZero(m_estimate2VOs[i].getNmoney())));
          }
        }
        debugTime.addExecutePhase("设置税率和价税合计");
        //
        getBillCardPanel().getBillModel().clearBodyData();
        debugTime.addExecutePhase("clearBodyData()");
        //
        if (m_sZG.toUpperCase().equals("N")){
          getBillCardPanel().getBillData().setBodyValueVO(m_estimate1VOs);
        }
        else{
          getBillCardPanel().getBillData().setBodyValueVO(m_estimate2VOs);
        }
        debugTime.addExecutePhase("单据卡片设置数据：getBillCardPanel().getBillData().setBodyValueVO()");
        //
        getBillCardPanel().getBillModel().execLoadFormula();
        debugTime.addExecutePhase("单据卡片执行公式：getBillCardPanel().getBillModel().execLoadFormula()");
        
        getBillCardPanel().updateValue();
        getBillCardPanel().updateUI();
        changeButtonState();
        boolean bSetFlag = m_sZG.toUpperCase().equals("N");
        getBillCardPanel().setEnabled(bSetFlag);
        debugTime.addExecutePhase("其它处理");
        
        if (bSetFlag) setPartEditable();
        debugTime.addExecutePhase("setPartEditable()");
        // 按精度设置汇率
        setBodyDigits();
        debugTime.addExecutePhase("setBodyDigits()");
        if (!bSetFlag) {
          // 已经暂估,重新计算
          getBillCardPanel().getBillModel().execFormulas(new String[] {"norgtaxmoney->noriginaltaxpricemny-noriginalcurmny"});
          for(int i=0;i<getBillCardPanel().getRowCount();++i)
            setYFLocalPriceMnyFrmOrg(getBillCardPanel(), m_sUnitCode, i, 
                PuPubVO.getUFDouble_NullAsZero(getBillCardPanel().getBodyValueAt(i,"nexchangeotobrate"))
                , m_measure[1], m_measure[2], "ninnum");
        }

        debugTime.addExecutePhase("getBillCardPanel().getBillModel().execFormulas()");
        //输出时间分布
        debugTime.showAllExecutePhase("采购暂估查询入库单总时间：");
      }
      catch (java.sql.SQLException e) {
        MessageDialog.showErrorDlg(this, NCLangRes.getInstance().getStrByID("40040503", "UPP40040503-000035")/* @res "入库单查询" */,
            NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000412")/* @res "SQL语句错误！" */);
        SCMEnv.out(e);
        return;
      }
      catch (ArrayIndexOutOfBoundsException e) {
        MessageDialog.showErrorDlg(this, NCLangRes.getInstance().getStrByID("40040503", "UPP40040503-000035")/* @res "入库单查询" */,
            NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000426")/* @res "数组越界错误！" */);
        SCMEnv.out(e);
        return;
      }
      catch (NullPointerException e) {
        MessageDialog.showErrorDlg(this, NCLangRes.getInstance().getStrByID("40040503", "UPP40040503-000035")/* @res "入库单查询" */,
            NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000427")/* @res "空指针错误！" */);
        SCMEnv.out(e);
        return;
      }
      catch (Exception e) {
        MessageDialog.showErrorDlg(this, NCLangRes.getInstance().getStrByID("40040503", "UPP40040503-000035")/* @res "入库单查询" */, e.getMessage());
        SCMEnv.out(e);
        return;
      }
    }
  }


  /**
   * 功能描述:入库单查询 参数： 返回： 作者：熊海情 创建：2004-3-24 14:41:50 修改：2004-09-10 袁野
   * <p>
   * 修改：2008-06-17 張紅芳 for v55
   * <p>
   * 修改：2088-10-12 zhaoyha for v55
   * <P>
   * 说明：使用新的查询模板
   * 这个用于V55新查询模
   */
  public void onQueryForV55() {

    if (!m_bICStartUp) {
      // 库存未启用
      MessageDialog.showErrorDlg(this,
          nc.ui.ml.NCLangRes.getInstance().getStrByID("40040503", "UPP40040503-000035")/*
                                                                                         * @res
                                                                                         * "入库单查询"
                                                                                         */,
          nc.ui.ml.NCLangRes.getInstance().getStrByID("40040503", "UPP40040503-000036")/*
                                                                                         * @res
                                                                                         * "库存未启用，不存在入库单！"
                                                                                         */);
      return;
    }
    if (getNewQueryDlg().showModal() == QueryConditionDLG.ID_OK) {
      String sqlWherePart = getNewQueryDlg().getQueryWhereSql();

      // 设置按钮状态：除暂估，反暂，全消外全部为正常
      for (int i = 0; i < 5; i++) {
        m_nButtonState[i] = 0;
      }

      m_nButtonState[1] = 1;
      m_nButtonState[2] = 1;
      m_nButtonState[3] = 1;

      m_sZG = getNewQueryDlg().getQueryEstimateFlag();

      // 查询
      try {
        long tTime = System.currentTimeMillis();

        if (m_sZG.toUpperCase().equals("N")) {
          m_estimate1VOs = EstimateHelper.queryEstimate(m_sUnitCode, sqlWherePart, m_sZG);
          if (m_estimate1VOs == null || m_estimate1VOs.length == 0) {
            MessageDialog.showHintDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("40040503",
                "UPP40040503-000035")/* @res "入库单查询" */, nc.ui.ml.NCLangRes.getInstance().getStrByID("40040503",
                "UPP40040503-000037")/* @res "没有符合条件的入库单！" */);

            // 清空数据
            getBillCardPanel().getBillModel().clearBodyData();
            getBillCardPanel().updateUI();
            // 全选为空
            m_nButtonState[0] = 1;
            changeButtonState();
            return;
          }
          tTime = System.currentTimeMillis() - tTime;
          SCMEnv.out("入库单查询时间：" + tTime + " ms!");

        }
        else {
          m_estimate2VOs = EstimateHelper.queryEstimate(m_sUnitCode, sqlWherePart, m_sZG);
          if (m_estimate2VOs == null || m_estimate2VOs.length == 0) {
            MessageDialog.showHintDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("40040503",
                "UPP40040503-000035")/* @res "入库单查询" */, nc.ui.ml.NCLangRes.getInstance().getStrByID("40040503",
                "UPP40040503-000037")/* @res "没有符合条件的入库单！" */);

            // 清空数据
            getBillCardPanel().getBillModel().clearBodyData();
            getBillCardPanel().updateUI();
            // 全选为空
            m_nButtonState[0] = 1;
            changeButtonState();
            return;
          }
          tTime = System.currentTimeMillis() - tTime;
          SCMEnv.out("入库单查询时间：" + tTime + " ms!");
        }

        // 加载自由项
        nc.ui.scm.pub.FreeVOParse freeParse = new nc.ui.scm.pub.FreeVOParse();
        Vector vTemp = new Vector();
        if (m_sZG.toUpperCase().equals("N")) {
          for (int i = 0; i < m_estimate1VOs.length; i++) {
            if (m_estimate1VOs[i].getVfree1() != null || m_estimate1VOs[i].getVfree2() != null
                || m_estimate1VOs[i].getVfree3() != null || m_estimate1VOs[i].getVfree4() != null
                || m_estimate1VOs[i].getVfree5() != null) {
              vTemp.addElement(m_estimate1VOs[i]);
            }
          }
          if (vTemp.size() > 0) {
            EstimateVO tempVO[] = new EstimateVO[vTemp.size()];
            vTemp.copyInto(tempVO);
            freeParse.setFreeVO(tempVO, "vfree", "vfree", null, "cmangid", false);
          }
        }
        else {
          for (int i = 0; i < m_estimate2VOs.length; i++) {
            if (m_estimate2VOs[i].getVfree1() != null || m_estimate2VOs[i].getVfree2() != null
                || m_estimate2VOs[i].getVfree3() != null || m_estimate2VOs[i].getVfree4() != null
                || m_estimate2VOs[i].getVfree5() != null) {
              vTemp.addElement(m_estimate2VOs[i]);
            }
          }
          if (vTemp.size() > 0) {
            EstimateVO tempVO[] = new EstimateVO[vTemp.size()];
            vTemp.copyInto(tempVO);
            freeParse.setFreeVO(tempVO, "vfree", "vfree", null, "cmangid", false);
          }
        }

        // 设置税率和价税合计, 计算含税单价和价税合计
        // 如果入库单非来源于订单,取存货对应的税目税率,扣税类别为应税外加
        vTemp = new Vector();
        if ("N".equalsIgnoreCase(m_sZG)) {
          for (int i = 0; i < m_estimate1VOs.length; i++) {
            m_estimate1VOs[i].setNdiscountrate(new UFDouble(100));
          }
        }
        else {
          for (int i = 0; i < m_estimate2VOs.length; i++) {
            // 恒安项目问题，查询已经暂估过的数据未显示税额
            m_estimate2VOs[i].setAttributeValue("ntaxmoney", PuPubVO.getUFDouble_NullAsZero(
                m_estimate2VOs[i].getNtotalmoney()).sub(PuPubVO.getUFDouble_NullAsZero(m_estimate2VOs[i].getNmoney())));
          }
        }

        getBillCardPanel().getBillModel().clearBodyData();
        if (m_sZG.toUpperCase().equals("N")) {
          getBillCardPanel().getBillData().setBodyValueVO(m_estimate1VOs);
        }
        else {
          getBillCardPanel().getBillData().setBodyValueVO(m_estimate2VOs);
        }

        getBillCardPanel().getBillModel().execLoadFormula();
        getBillCardPanel().updateValue();
        getBillCardPanel().updateUI();

        changeButtonState();

        boolean bSetFlag = m_sZG.toUpperCase().equals("N");

        getBillCardPanel().setEnabled(bSetFlag);

        if (bSetFlag)
          setPartEditable();
        // 按精度设置汇率
        setBodyDigits();
        if (!bSetFlag) {
          getBillCardPanel().getBillModel().execFormulas(new String[] {
            "norgtaxmoney->noriginaltaxpricemny-noriginalcurmny"
          });
          // for (int i = 0; i < m_estimate2VOs.length; i++) {// 支持用户编辑税率
          // // 查询已暂估时反算税率 zhf
          // // add for v55
          // if (m_estimate2VOs[i].getIdiscounttaxtype().intValue() == 0) {//
          // 应税内含
          // getBillCardPanel().getBillModel().execFormulas(i, new String[] {
          // "ntaxrate->(1-noriginalnetprice/norgnettaxprice)*100"
          // });
          // }
          // else if (m_estimate2VOs[i].getIdiscounttaxtype().intValue() == 1)
          // {// 应税外加
          // getBillCardPanel().getBillModel().execFormulas(i, new String[] {
          // "ntaxrate->(norgnettaxprice/noriginalnetprice-1)*100"
          // });
          // }
          // }
        }

      }
      catch (java.sql.SQLException e) {
        MessageDialog.showErrorDlg(this,
            nc.ui.ml.NCLangRes.getInstance().getStrByID("40040503", "UPP40040503-000035")/*
                                                                                           * @res
                                                                                           * "入库单查询"
                                                                                           */,
            nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000412")/*
                                                                                             * @res
                                                                                             * "SQL语句错误！"
                                                                                             */);
        SCMEnv.out(e);
        return;
      }
      catch (ArrayIndexOutOfBoundsException e) {
        MessageDialog.showErrorDlg(this,
            nc.ui.ml.NCLangRes.getInstance().getStrByID("40040503", "UPP40040503-000035")/*
                                                                                           * @res
                                                                                           * "入库单查询"
                                                                                           */,
            nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000426")/*
                                                                                             * @res
                                                                                             * "数组越界错误！"
                                                                                             */);
        SCMEnv.out(e);
        return;
      }
      catch (NullPointerException e) {
        MessageDialog.showErrorDlg(this,
            nc.ui.ml.NCLangRes.getInstance().getStrByID("40040503", "UPP40040503-000035")/*
                                                                                           * @res
                                                                                           * "入库单查询"
                                                                                           */,
            nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000427")/*
                                                                                             * @res
                                                                                             * "空指针错误！"
                                                                                             */);
        SCMEnv.out(e);
        return;
      }
      catch (Exception e) {
        MessageDialog.showErrorDlg(this,
            nc.ui.ml.NCLangRes.getInstance().getStrByID("40040503", "UPP40040503-000035")/*
                                                                                           * @res
                                                                                           * "入库单查询"
                                                                                           */, e
                .getMessage());
        SCMEnv.out(e);
        return;
      }
    }
  }

  // /*
  // * 计算需暂估的采购入库单的含税单价和价税合计 czp, 2007-08-15, 重构到VO，满足后台应用需求
  // */
  // private EstimateVO[] calculateTaxPriceForEstimateVO(EstimateVO VO[]) {
  // int nPricePolicy = PuTool.getPricePriorPolicy(m_sUnitCode);
  // EstimateVO.calculateTaxPriceForEstimateVO(VO, nPricePolicy);
  // return VO;
  // }

  /**
   * 功能描述:全选 参数： 返回： 作者：熊海情 创建：2004-3-24 14:41:50 修改：晁志平 FOR V30
   */
  public void onSelectAll() {
    Timer timeTrace = new Timer();
    timeTrace.start();
    int nRow = getBillCardPanel().getBillModel().getRowCount();
    if (nRow <= 0) {
      showHintMessage(NCLangRes.getInstance().getStrByID("40040101", "UPP40040101-000541")/*@res"无数据选中"*/);
      return;
    }
    timeTrace.addExecutePhase("getBillCardPanel().getBillModel().getRowCount()="+getBillCardPanel().getBillModel().getRowCount());
    //为解决效率问题,去掉着色
    //getBillCardPanel().getBillTable().setRowSelectionInterval(0, nRow - 1);

    //关闭事件触发
    getBillCardPanel().getBillModel().removeRowStateChangeEventListener();
    
  // 关闭合计开关
  boolean bOldNeedCalc = getBillCardPanel().getBillModel().isNeedCalculate();
  getBillCardPanel().getBillModel().setNeedCalculate(false);
  //
    for (int i = 0; i < nRow; i++) {
      getBillCardPanel().getBillModel().setRowState(i, BillModel.SELECTED);
    }
    timeTrace.addExecutePhase("getBillCardPanel().getBillModel().setRowState(i, BillModel.SELECTED)");
    //
    getBillCardPanel().getBillModel().reCalcurateAll();
    timeTrace.addExecutePhase("getBillCardPanel().getBillModel().reCalcurateAll()");
  // 打开合计开关
    getBillCardPanel().getBillModel().setNeedCalculate(bOldNeedCalc);
    
    getBillCardPanel().updateUI();
    timeTrace.addExecutePhase("getBillCardPanel().updateUI()");
    //
    setBtnsStates();
    timeTrace.addExecutePhase("setBtnsStates()");
    //
    timeTrace.showAllExecutePhase("onSelectAll()");
    //打开事件触发
    getBillCardPanel().getBillModel().addRowStateChangeEventListener(this);
  }

  /**
   * 功能描述:全消 参数： 返回： 作者：熊海情 创建：2004-3-24 14:41:50 修改：晁志平 FOR V30
   */
  public void onSelectNo() {
      int nRow = getBillCardPanel().getBillModel().getRowCount();
      if (nRow <= 0) {
        showHintMessage(NCLangRes.getInstance().getStrByID("40040101", "UPP40040101-000541")/*@res"无数据选中"*/);
        return;
      }
    //关闭事件触发
    getBillCardPanel().getBillModel().removeRowStateChangeEventListener();
    // 关闭合计开关
    boolean bOldNeedCalc = getBillCardPanel().getBillModel().isNeedCalculate();
    getBillCardPanel().getBillModel().setNeedCalculate(false);
    //
      getBillCardPanel().getBillTable().removeRowSelectionInterval(0, nRow - 1);
      //
      for (int i = 0; i < nRow; i++) {
        getBillCardPanel().getBillModel().setRowState(i, BillModel.UNSTATE);
      }
      getBillCardPanel().getBillModel().reCalcurateAll();
    // 打开合计开关
      getBillCardPanel().getBillModel().setNeedCalculate(bOldNeedCalc);
      //
      getBillCardPanel().updateUI();
      //
      setBtnsStates();
      //打开事件触发
      getBillCardPanel().getBillModel().addRowStateChangeEventListener(this);     
    }

  /**
   * 功能描述:取消暂估 参数： 返回： 作者：熊海情 创建：2004-3-24 14:41:50 修改：晁志平 FOR V30
   */
  public void onUnEstimate() {

    nc.vo.scm.pu.Timer timerDebug = new nc.vo.scm.pu.Timer();
    timerDebug.start();

    Integer nSelected[] = null;
    Vector v = new Vector();
    Vector vv = new Vector();
    int nRow = getBillCardPanel().getRowCount();
    for (int i = 0; i < nRow; i++) {
      int nStatus = getBillCardPanel().getBillModel().getRowState(i);
      if (nStatus == BillModel.SELECTED)
        v.addElement(new Integer(i));
      else
        vv.addElement(new Integer(i));
    }
    nSelected = new Integer[v.size()];
    v.copyInto(nSelected);

    if (nSelected == null || nSelected.length == 0) {
      MessageDialog.showErrorDlg(this,
          nc.ui.ml.NCLangRes.getInstance().getStrByID("40040503", "UPP40040503-000038")/*
                                                                                         * @res
                                                                                         * "入库单取消暂估"
                                                                                         */,
          nc.ui.ml.NCLangRes.getInstance().getStrByID("40040503", "UPP40040503-000031")/*
                                                                                         * @res
                                                                                         * "未选中入库单！"
                                                                                         */);
      return;
    }
    Vector vTemp = new Vector();
    for (int i = 0; i < nSelected.length; i++) {
      EstimateVO vo = m_estimate2VOs[nSelected[i].intValue()];
      vTemp.addElement(vo);
    }
    EstimateVO VOs[] = new EstimateVO[vTemp.size()];
    vTemp.copyInto(VOs);

    timerDebug.addExecutePhase("组织数据");

    try {
      EstimateHelper.antiEstimate(VOs, new ClientLink(getClientEnvironment()));
    }
    catch (java.sql.SQLException e) {
      MessageDialog.showErrorDlg(this,
          nc.ui.ml.NCLangRes.getInstance().getStrByID("40040503", "UPP40040503-000038")/*
                                                                                         * @res
                                                                                         * "入库单取消暂估"
                                                                                         */,
          nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000412")/*
                                                                                           * @res
                                                                                           * "SQL语句错误！"
                                                                                           */);
      SCMEnv.out(e);
      return;
    }
    catch (ArrayIndexOutOfBoundsException e) {
      MessageDialog.showErrorDlg(this,
          nc.ui.ml.NCLangRes.getInstance().getStrByID("40040503", "UPP40040503-000038")/*
                                                                                         * @res
                                                                                         * "入库单取消暂估"
                                                                                         */,
          nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000426")/*
                                                                                           * @res
                                                                                           * "数组越界错误！"
                                                                                           */);
      SCMEnv.out(e);
      return;
    }
    catch (NullPointerException e) {
      MessageDialog.showErrorDlg(this,
          nc.ui.ml.NCLangRes.getInstance().getStrByID("40040503", "UPP40040503-000038")/*
                                                                                         * @res
                                                                                         * "入库单取消暂估"
                                                                                         */,
          nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000427")/*
                                                                                           * @res
                                                                                           * "空指针错误！"
                                                                                           */);
      SCMEnv.out(e);
      return;
    }
    catch (nc.vo.pub.BusinessException e) {
      MessageDialog.showErrorDlg(this,
          nc.ui.ml.NCLangRes.getInstance().getStrByID("40040503", "UPP40040503-000038")/*
                                                                                         * @res
                                                                                         * "入库单取消暂估"
                                                                                         */, e
              .getMessage());
      SCMEnv.out(e);
      return;
    }
    catch (Exception e) {
      MessageDialog.showErrorDlg(this,
          nc.ui.ml.NCLangRes.getInstance().getStrByID("40040503", "UPP40040503-000038")/*
                                                                                         * @res
                                                                                         * "入库单取消暂估"
                                                                                         */, e
              .getMessage());
      SCMEnv.out(e);
      return;
    }
    timerDebug.addExecutePhase("远程调用 antiEstimate(EstimateVO[],String)");

    this
        .showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("40040503", "UPP40040503-000039")/*
                                                                                                       * @res
                                                                                                       * "取消暂估成功！"
                                                                                                       */);

    // 取消暂估后的入库单不再显示在界面
    if (vv == null || vv.size() == 0) {
      // 所有入库单已取消暂估完毕
      getBillCardPanel().getBillData().clearViewData();
      getBillCardPanel().updateUI();
      // 除查询外所有为灰
      for (int i = 0; i < 5; i++)
        m_nButtonState[i] = 1;
      m_nButtonState[4] = 0;
      changeButtonState();
      return;
    }

    Vector v0 = new Vector();
    for (int i = 0; i < vv.size(); i++) {
      int n = ((Integer) vv.elementAt(i)).intValue();
      v0.addElement(m_estimate2VOs[n]);
    }
    m_estimate2VOs = new EstimateVO[v0.size()];
    v0.copyInto(m_estimate2VOs);

    getBillCardPanel().getBillModel().setBodyDataVO(m_estimate2VOs);
    getBillCardPanel().getBillModel().execLoadFormula();
    getBillCardPanel().getBillModel().updateValue();
    getBillCardPanel().updateUI();

    // 设置按钮状态：除暂估，反暂，全消外全部为正常
    for (int i = 0; i < 5; i++) {
      m_nButtonState[i] = 0;
    }
    m_nButtonState[1] = 1;
    m_nButtonState[2] = 1;
    m_nButtonState[3] = 1;
    m_nButtonState[7] = 1;
    changeButtonState();

    timerDebug.addExecutePhase("取消暂估后处理(不再显示单据及按钮逻辑)");

    timerDebug.showAllExecutePhase("取消暂估UI时间分布：");
  }

  /**
   * 功能描述:暂估时,除无税单价, 无税金额, 含税单价, 价税合计和税率可编辑外, 其余数据项均不可编辑, 参数： 返回： 作者：熊海情
   * 创建：2004-3-24 14:41:50 修改：晁志平 FOR V30
   */
  private void setPartEditable() {
    //
    BillItem items[] = getBillCardPanel().getBodyShowItems();
    for (int i = 0; i < items.length; i++) {
      items[i].setEnabled("noriginalnetprice".equals(items[i].getKey().trim())
          || "norgnettaxprice".equals(items[i].getKey().trim())
          || "noriginaltaxpricemny".equals(items[i].getKey().trim())
          || "noriginalcurmny".equals(items[i].getKey().trim()) || "currencytypename".equals(items[i].getKey().trim())
          || "nexchangeotobrate".equals(items[i].getKey().trim()) || "cfycode".equals(items[i].getKey().trim())
          || "nfeemny".equals(items[i].getKey().trim()) || "ntaxrate".equals(items[i].getKey().trim())
          || "nprice".equals(items[i].getKey().trim()) || "nmoney".equals(items[i].getKey().trim())
          || "ntotalmoney".equals(items[i].getKey().trim()) || "ntaxprice".equals(items[i].getKey().trim()));
      items[i].setEdit("noriginalnetprice".equals(items[i].getKey().trim())
          || "norgnettaxprice".equals(items[i].getKey().trim())
          || "noriginaltaxpricemny".equals(items[i].getKey().trim())
          || "noriginalcurmny".equals(items[i].getKey().trim()) || "currencytypename".equals(items[i].getKey().trim())
          || "nexchangeotobrate".equals(items[i].getKey().trim()) || "cfycode".equals(items[i].getKey().trim())
          || "nfeemny".equals(items[i].getKey().trim()) || "ntaxrate".equals(items[i].getKey().trim())
          || "nprice".equals(items[i].getKey().trim()) || "nmoney".equals(items[i].getKey().trim())
          || "ntotalmoney".equals(items[i].getKey().trim()) || "ntaxprice".equals(items[i].getKey().trim()));

    }
    // 币种为人民币折本汇率不可修改
    int rows = getBillCardPanel().getBillModel().getRowCount();
    for (int i = 0; i < rows; i++) {
      Object curTypeId = getBillCardPanel().getBodyValueAt(i, "currencytypeid");
      if (curTypeId == null)
        continue;

      if (curTypeId.toString().equalsIgnoreCase(m_sCurrTypeID))// 判断当前币种是否公司本位币
        // 设置折本汇率编辑属性
        getBillCardPanel().getBillModel().setCellEditable(i, "nexchangeotobrate", false);
      else
        getBillCardPanel().getBillModel().setCellEditable(i, "nexchangeotobrate", true);
    }
    //
    UIRefPane nRefPanel = (UIRefPane) getBillCardPanel().getBodyItem("nprice").getComponent();
    UITextField nPriceUI = (UITextField) nRefPanel.getUITextField();
    nPriceUI.setMaxLength(16);
    nPriceUI.setDelStr("-");

    nRefPanel = (UIRefPane) getBillCardPanel().getBodyItem("ntaxprice").getComponent();
    nPriceUI = (UITextField) nRefPanel.getUITextField();
    nPriceUI.setMaxLength(16);
    nPriceUI.setDelStr("-");

  }

  /**
   * 功能描述:处理列表选择 参数： 返回： 作者：熊海情 创建：2002-9-26 14:41:50 修改：晁志平 FOR V30
   */
  public void valueChanged(javax.swing.event.ListSelectionEvent event) {
    if ((ListSelectionModel) event.getSource() == getBillCardPanel().getBillTable().getSelectionModel()
        || (ListSelectionModel) event.getSource() == getBillCardPanel().getBodyPanel().getRowNOTable()
            .getSelectionModel()) {
      // 表体所有行恢复正常（不选择）
      int nSelected[] = getBillCardPanel().getBillTable().getSelectedRows();
      // 获得表体选择行数
      if (nSelected != null && nSelected.length > 0) {
        for (int i = 0; i < nSelected.length; i++) {
          int j = nSelected[i];
          getBillCardPanel().getBillModel().setRowState(j, BillModel.SELECTED);
        }
      }
      //
      setBtnsStates();
    }
  }

  /**
   * 设置按钮状态
   * <p>
   * 
   * @author czp
   * @time 2008-5-15 下午02:29:30
   */
  private void setBtnsStates() {
    //
    int nRow = getBillCardPanel().getBillModel().getRowCount();
    // 设置按钮状态：全部为正常
    for (int i = 0; i < 5; i++) {
      m_nButtonState[i] = 0;
    }
    if (m_sZG.toUpperCase().equals("N")) {
      m_nButtonState[3] = 1;
      m_nButtonState[7] = 0;
      
    }
    else {
      m_nButtonState[2] = 1;
      m_nButtonState[7] = 1;
    }

    int m = 0;
    for (int i = 0; i < getBillCardPanel().getBillModel().getRowCount(); i++) {
      if (getBillCardPanel().getBillModel().getRowState(i) == BillModel.SELECTED)
        m++;
    }
    // 选中行数大于0，全消正常，否则为灰
    if (m > 0) {
      m_nButtonState[1] = 0;
      m_nButtonState[7] = 0;
      if (m == nRow)
        m_nButtonState[0] = 1;
      else
        m_nButtonState[0] = 0;
    }
    else {
      m_nButtonState[0] = 0;
      m_nButtonState[1] = 1;
      m_nButtonState[7] = 1;
      if (m_sZG.toUpperCase().equals("N"))
        m_nButtonState[2] = 1;
      else
        m_nButtonState[3] = 1;
    }
    changeButtonState();
  }

  public boolean beforeEdit(BillEditEvent e) {
    String key = e.getKey();
    if ("ntaxrate".equalsIgnoreCase(key)) {
      Object oTaxRate = getBillCardPanel().getBillModel().getValueAt(e.getRow(), "ntaxrate");
      if (oTaxRate == null || oTaxRate.toString().trim().length() == 0) {
        getBillCardPanel().getBillModel().setValueAt(new UFDouble(0), e.getRow(), "ntaxrate");
      }
    }
//    if ("nfeemny".equalsIgnoreCase(key)) {
//      Object ufNum = getBillCardPanel().getBillModel().getValueAt(e.getRow(), "ninnum");
//      if (ufNum != null && ufNum.toString().trim().length() != 0) {
//        UIRefPane ref = (UIRefPane) getBillCardPanel().getBillModel().getItemByKey("nfeemny").getComponent();
//        //ref.setTextType(UITextType.TextDbl);
//        if (((UFDouble) ufNum).doubleValue() < 0) {
//          ref.setMaxValue(0);
//          ref.setMinValue(-999999);
//        }
//        else {
//          ref.setMaxValue(999999);
//          ref.setMinValue(0);
//        }
//      }
//    }
    if ("nexchangeotobrate".equalsIgnoreCase(key)) {
      // 必须调用!!!!!!!!
      getBillCardPanel().stopEditing();
      //
      String sCurrId = (String) getBillCardPanel().getBodyValueAt(e.getRow(), "currencytypeid");
      getBillCardPanel().getBodyItem("nexchangeotobrate").setDecimalDigits(
          m_cardPoPubSetUI2.getBothExchRateDigit(m_sUnitCode, sCurrId)[0]);
    }
    return true;
  }

  public Object[] getRelaSortObjectArray() {
    // TODO 自动生成方法存根
    if (m_sZG.equals("N"))
      return m_estimate1VOs;
    return m_estimate2VOs;
  }

  public void mouseClicked(MouseEvent e) {
    // TODO 自动生成方法存根

  }

  public void mouseEntered(MouseEvent e) {
    // TODO 自动生成方法存根

  }

  public void mouseExited(MouseEvent e) {
    // TODO 自动生成方法存根

  }

  public void mousePressed(MouseEvent e) {
    // TODO 自动生成方法存根

  }

  public void mouseReleased(MouseEvent e) {
    if (e.getModifiers() == MouseEvent.MOUSE_RELEASED) {
      setBtnsStates();
    }

  }

  /**
   * zhf 为表体数据设置精度 和币种相关 汇率 原币金额业务精度
   */
  private void setBodyDigits() {
    EstimateVO[] VOs = null;
    VOs = (EstimateVO[]) getBillCardPanel().getBillData().getBodyValueVOs("nc.vo.ps.estimate.EstimateVO");
    int row = 0;
    ArrayList<String> currIdList = new ArrayList<String>();
    String sCurrId = null;

    for (EstimateVO vo : VOs) {
      sCurrId = PuPubVO.getString_TrimZeroLenAsNull(vo.getCurrencytypeid());
      if (sCurrId == null) {
        continue;
      }
      if (!currIdList.contains(sCurrId) && !sCurrId.equalsIgnoreCase(m_sCurrTypeID)) {
        currIdList.add(sCurrId);
      }
    }

    // 获取币种对应业务精度
    HashMap<String, Integer> mnyDigitMap = new HashMap<String, Integer>();
    if (currIdList != null && currIdList.size() > 0) {
      String[] currIds = currIdList.toArray(new String[0]);
      int[] iMnyDigits = getPOPubSetUI2().getMoneyDigitByCurr_Busi_Batch(currIds);
      if (currIds.length != iMnyDigits.length)
        return;
      for (int i = 0; i < currIds.length; i++) {
        mnyDigitMap.put(currIds[i], new Integer(iMnyDigits[i]));
      }
    }

    // int row = 0;
    for (EstimateVO vo : VOs) {
      sCurrId = PuPubVO.getString_TrimZeroLenAsNull(vo.getCurrencytypeid());
      int iaExchRateDigit = getDigits_ExchangeRate(sCurrId) == null ? 5 : getDigits_ExchangeRate(sCurrId)[0];
      getBillCardPanel().getBillModel().getItemByKey("nexchangeotobrate").setDecimalDigits(iaExchRateDigit);

      int iMnyDigit = PuPubVO.getInteger_NullAs(mnyDigitMap.get(vo.getCurrencytypeid()), new Integer(m_measure[2]))
          .intValue();
      getBillCardPanel().getBillModel().getItemByKey("noriginaltaxpricemny").setDecimalDigits(iMnyDigit);

      getBillCardPanel().getBillModel().getItemByKey("noriginalcurmny").setDecimalDigits(iMnyDigit);
      getBillCardPanel().getBillModel().getItemByKey("norgtaxmoney").setDecimalDigits(iMnyDigit);
      if (m_sZG.toUpperCase().equals("N")) {
        getBillCardPanel().getBillModel().setValueAt(m_estimate1VOs[row].getNexchangeotobrate(), row,
            "nexchangeotobrate");

        getBillCardPanel().getBillModel().setValueAt(m_estimate1VOs[row].getNoriginaltaxpricemny(), row,
            "noriginaltaxpricemny");
        getBillCardPanel().getBillModel().setValueAt(m_estimate1VOs[row].getNoriginalcurmny(), row, "noriginalcurmny");
        getBillCardPanel().getBillModel().setValueAt(m_estimate1VOs[row].getNorgtaxmoney(), row, "norgtaxmoney");
      }

      else {
        getBillCardPanel().getBillModel().setValueAt(m_estimate2VOs[row].getNexchangeotobrate(), row,
            "nexchangeotobrate");

        getBillCardPanel().getBillModel().setValueAt(m_estimate2VOs[row].getNoriginaltaxpricemny(), row,
            "noriginaltaxpricemny");
        getBillCardPanel().getBillModel().setValueAt(m_estimate2VOs[row].getNoriginalcurmny(), row, "noriginalcurmny");
        getBillCardPanel().getBillModel().setValueAt(m_estimate2VOs[row].getNorgtaxmoney(), row, "norgtaxmoney");
      }
      row++;
    }
  }

  /**
   * zhf 获得指定币种的汇率精度（折本+折辅）
   */
  private int[] getDigits_ExchangeRate(String sCurrId) {
    if (PuPubVO.getString_TrimZeroLenAsNull(sCurrId) == null)
      return null;
    int[] iaExchRateDigit = null;
    // 得到折本、折辅汇率精度
    iaExchRateDigit = getPOPubSetUI2().getBothExchRateDigit(m_sUnitCode, sCurrId);
    return iaExchRateDigit;
  }

  /*
   * since v55, 第一列选中复选框事件处理
   */
  public void valueChanged(RowStateChangeEvent event) {
    //
    setBtnsStates();
  }

  /**
   * 方法功能描述：
   * <p>
   * 初始化新查询对话框的信息。
   * <p>
   * <b>examples:</b>
   * <p>
   * 使用示例
   * <p>
   * <b>参数说明</b>
   * <p>
   * 
   * @author zhaoyha
   * @time 2008-10-5 下午07:57:41
   * @since 5.5
   */
  protected EstimateQueryDlg getNewQueryDlg() {

    if (m_condDlg == null) {

      TemplateInfo tempinfo = new TemplateInfo();
      tempinfo.setPk_Org(m_sUnitCode);
      tempinfo.setCurrentCorpPk(m_sUnitCode);
      tempinfo.setFunNode(getModuleCode());
      tempinfo.setUserid(ClientEnvironment.getInstance().getUser().getPrimaryKey());

      // 生成新的查询对话框
      m_condDlg = new EstimateQueryDlg(this, tempinfo, NCLangRes.getInstance().getStrByID("4004050301",
          "UPP4004050301-000004")/* @res "暂估入库单查询" */);
    }
    return m_condDlg;
  }
  
  /**
   * 
   * 方法功能描述：对暂估VO做保存前检查。
   * <p>
   * <b>examples:</b>
   * <p>
   * 使用示例
   * <p>
   * <b>参数说明</b>
   * @param vos 选择的暂估VO数组
   * @param rownum 选择的行号数组 与 vos对应
   * @return
   * <p>
   * @author zhaoyha
   * @time 2009-9-17 上午11:14:01
   */
  private boolean checkBeforeSave(EstimateVO[] vos,Integer[] rownum){
    StringBuilder errMsg=new StringBuilder();
    String lineSeparator=System.getProperty("line.separator");
    for (int i = 0; i < vos.length; i++) {
      StringBuilder rowErrMsg=new StringBuilder();
      if(UFDouble.ZERO_DBL.equals(PuPubVO.getUFDouble_NullAsZero(vos[i].getNmoney())))    //暂估金额
        rowErrMsg.append("\t").append(NCLangRes.getInstance().getStrByID("40040503", "EstimateUI-000000")/*暂估金额为零*/);
      if(UFDouble.ZERO_DBL.equals(PuPubVO.getUFDouble_NullAsZero(vos[i].getNfeemny()))    //费用编码和费用金额
              && !StringUtil.isEmptyWithTrim(vos[i].getCfeeid())
          || !UFDouble.ZERO_DBL.equals(PuPubVO.getUFDouble_NullAsZero(vos[i].getNfeemny()))
              && StringUtil.isEmptyWithTrim(vos[i].getCfeeid())) 
        rowErrMsg.append(lineSeparator).append("\t").append(NCLangRes.getInstance().getStrByID("40040503", "EstimateUI-000001")/*暂估费用编码或金额未录入*/);
      if(!UFDouble.ZERO_DBL.equals(PuPubVO.getUFDouble_NullAsZero(vos[i].getNfeemny())) //暂估费用与数量正负符号
          && vos[i].getNfeemny().doubleValue()*vos[i].getNinnum().doubleValue()<0)
        rowErrMsg.append(lineSeparator).append("\t").append(NCLangRes.getInstance().getStrByID("40040503", "EstimateUI-000002")/*暂估费用与数量正负号不一致*/);
      try {vos[i].validate();}catch (ValidationException e) {                           //字段最大值最小值
        rowErrMsg.append(lineSeparator).append("\t").append(e.getMessage()); }
      if(rowErrMsg.length()>0)
        errMsg.append(lineSeparator).append(NCLangRes.getInstance().getStrByID("40040503", "EstimateUI-000003",
                null,new String[]{(PuPubVO.getInteger_NullAs(rownum[i], new Integer(0)).intValue() + 1) + ""})/*第{0}行存在以下错误：*/).append(lineSeparator).append(rowErrMsg);
    }
    if(errMsg.length()>0){
      String showMsg=errMsg.toString().replaceFirst(lineSeparator, "").replaceAll(lineSeparator+lineSeparator,lineSeparator);
      MessageDialog.showErrorDlg(this, NCLangRes.getInstance().getStrByID("40040503", "UPP40040503-000033"/* @res "暂估处理 */), 
          showMsg);
      return false;
    }
    return true;
  }
  
  
  
  
  /**
   * 2010-10-17 MeiChao 暂估处理中分摊费用的方法.
   * 方法运行机制说明:	勾选任意行,则默认操作此行对应的整张单据所有存货行.
   * 支持同时操作多张单据.
   */
  private void onCostDistribute(){
	  /**
	   * 第一步,获取页面所有暂估存货项.以及其他待处理原始数据.
	   */
	  //表体所有暂估数据
	  EstimateVO[] allEstimateVOs=(EstimateVO[])getBillCardPanel().getBillModel().getBodyValueVOs(EstimateVO.class.getName());
	  //重新获取一次所有暂估数据(供还原之用)
	  EstimateVO[] allEstimateVOsBackup=(EstimateVO[])getBillCardPanel().getBillModel().getBodyValueVOs(EstimateVO.class.getName());
	  //获取所选择的暂估数据
      Integer[] electedRowNOs = null;
      Vector select = new Vector();
      Vector unselect = new Vector();
      int nRow = getBillCardPanel().getRowCount();//获得行数
      for (int i = 0; i < nRow; i++) {
    	//获取当前行状态
        int nStatus = getBillCardPanel().getBillModel().getRowState(i);
        if (nStatus == BillModel.SELECTED)
        	select.addElement(new Integer(i));//如果该行为选中状态,则添加行号至选择集合.
        else
        	unselect.addElement(new Integer(i));//反之,则添加行号至未选择集合.
      }
      electedRowNOs = new Integer[select.size()];
      select.copyInto(electedRowNOs);//将选中行号集合填充入数组.
	  //对选中行数组进行为空判断
      if(electedRowNOs==null||electedRowNOs.length==0){
    	  MessageDialog.showErrorDlg(this, "错误", "对不起,请勾选要操作的数据.");
    	  return;
      }
      /**
       * 第二步,获取选中单据,即所选行对应的单据.选中一行存货默认选中对应单据整单.
       * 注意,可能为多个单据.
       */
      //选中单据PK数组
      String[] selectedPKs=null;
      //使用集合Set,来存放PK,可自动过滤重复.
      Set selectPKSet = new TreeSet();
	  //从选中行中遍历出单据pk的Set集合.
      for(int i=0;i<electedRowNOs.length;i++){
    	  selectPKSet.add(allEstimateVOs[electedRowNOs[i]].getCgeneralhid());
      }
      selectedPKs=new String[selectPKSet.size()];
      selectedPKs=(String[])selectPKSet.toArray(selectedPKs);//将Set中的PK填充入数组中.
	  //验证该数组
      if(selectedPKs==null||selectedPKs.length==0||selectedPKs[0]==null){//这里可以使用数组对其第一项判断,因为此时必须最少要有1个单据被选中.
    	  MessageDialog.showErrorDlg(this, "错误", "无法获取选中行的单据PK!请至后台查看.");
    	  return;
      }
      //**此时selectedPKs的长度便为所选择的单据数量,这里提到的一个概念<<选中单据>>,注意理解.**
      /**
       * 开始获取所有选中单据的数据,包括存货数量,费用金额,单据PK等,按单据不同存在不同的Map集合中
       * 而这些Map,又存放于一个ArrayList中...
       * 本步骤属于一个封装界面数据的过程.
       */
      List selectedAllBillData=new ArrayList();
	  for(int i=0;i<selectedPKs.length;i++){
		  Map selectedBillData=new HashMap();//单据信息Map
		  List selectedBodys=new ArrayList();//单据体信息List
		  Double selectedTotalQuantity=0.0;//所选单据体存货总数量
		  List rowno=new ArrayList();//所选单据体存货所在的行号
		  selectedBillData.put("pk", selectedPKs[i].toString());//将单据PK存入单据信息Map中,用以标识不同单据.
		  //遍历界面所有存货行
		  for(int j=0;j<allEstimateVOs.length;j++){
			  //如果存货行PK与当前selectedPKs[i]所指PK一致,说明该存货属于当前的某<<选中单据>>
			  if(allEstimateVOs[j].getCgeneralhid()==selectedPKs[i].toString()){
				  //将其所属行号放入rowno中.
				  rowno.add(j);
				  //将其加到单据体信息List中
				  selectedBodys.add(allEstimateVOs[j]);
				  //累加其存货总数量
				  selectedTotalQuantity+=allEstimateVOs[j].getNinnum().toDouble();
			  }
		  }
		  //遍历过后,将单据体存货信息List加入到单据信息Map中,组成一个完整的单据
		  selectedBillData.put("rowno", rowno);//表体行号组
		  selectedBillData.put("body", selectedBodys);//表体存货组
		  selectedBillData.put("number", selectedTotalQuantity);//表体存货总数
		  //将该单据加入到selectedBillData中
		  selectedAllBillData.add(selectedBillData);
	  }
	  if(selectedAllBillData==null||selectedAllBillData.size()==0){
		  MessageDialog.showErrorDlg(this, "严重错误", "系统在封装选中单据时产生一个不可预见的致命错误,请与UFIDA工程师联系.");
	  }
	  /**
	   * 界面数据封装完毕,开始读取单据对应费用信息.
	   */
	  //遍历所选单据对应的费用信息.
	  List<String> haveexpense=new ArrayList<String>();//有费用的单据
	  List<String> havenotexpense=new ArrayList<String>();//没有费用的单据
	  List<String> unknowexpense=new ArrayList<String>();//获取费用失败的单据
	  for(int i=0;i<selectedAllBillData.size();i++){
		  Double selectedtotalamount=0.0;//所选单据体总金额

		  Map selectedBill=(HashMap)selectedAllBillData.get(i);//获取当前单据Map对象
		  String cbillid=selectedBill.get("pk").toString();//获取当前单据对应入库单PK
		  StringBuffer sql = new StringBuffer(" dr = 0 ");//根据入库单PK拼sql语句的where字句
			Vector v = new Vector();
			ArrayList list = new ArrayList();
			v.addElement(cbillid);
			sql.append(" and cbillid = '" + cbillid + "' ");
			InformationCostVO[] expenseinformation = null;
		 try{
			 //查询对应入库单的费用信息.
			 expenseinformation = (InformationCostVO[])JJPuScmPubHelper.querySmartVOs(InformationCostVO.class, null, sql.toString());
		 } catch(Exception e){
			 SCMEnv.out(e);
			 showErrorMessage("获取费用信息失败!");
			 unknowexpense.add(cbillid);//如果数据库访问失败,则将该入库单pk存入unknowexpense
			 continue;//继续执行遍历,防止中途出错,影响后面的数据.
		 }
		 if(expenseinformation == null || expenseinformation.length == 0){
			 SCMEnv.out("单据:"+cbillid+"无费用信息.");
			 havenotexpense.add(cbillid);//如果查不到该入库单对应费用信息,则将该单pk存入havenotexpense
		 }else if(expenseinformation!=null&&expenseinformation.length>0){
			 //如成功查出费用信息,则将其遍历并将费用金额累加至selectedtotalamount中.
			 for(int k=0;k<expenseinformation.length;k++){
				 selectedtotalamount+=expenseinformation[k].getNoriginalcurmny().toDouble();
			 }
			 haveexpense.add(cbillid);
			 selectedBill.put("expenseamount", selectedtotalamount);//将单据费用总金额存入该单据map中.
		 }
	  }
	  //***费用信息读取完毕,开始验证费用信息****
	  if(haveexpense.size()!=0){
		  /**
		   * 如果有费用信息的单据数与选中单据数相等,则直接进行分摊操作,无需显示提示信息.
		   * 开始分摊费用操作...
		   */
		  //开始进行遍历分摊!
		  for(int i=0;i<selectedAllBillData.size();i++){//要注意,此处仍然对整个选中单据进行遍历..
			  for(int j=0;j<haveexpense.size();j++){//在此循环中将有费用信息的PK取出与之验证,相符,则表示可以进行分摊.
				  if(((HashMap)selectedAllBillData.get(i)).get("pk").toString().equals(haveexpense.get(j).toString())){
					 List rowno=(List)((HashMap)selectedAllBillData.get(i)).get("rowno");//行号
					 List selectedBodys=(List)((HashMap)selectedAllBillData.get(i)).get("body");//表体存货
					 Double expenseamount=(Double)((HashMap)selectedAllBillData.get(i)).get("expenseamount");//费用总额
					 Double number=(Double)((HashMap)selectedAllBillData.get(i)).get("number");//当前单据存货总数.
					 if(rowno.size()==selectedBodys.size()){//行号List必须与存货List长度相等,否则便说明之前数据处理有问题.处理结果是错误的..
						 for(int k=0;k<rowno.size();k++){//开始进行遍历分摊
							 //当前处理行号
							 Integer nowRowNO=Integer.valueOf(rowno.get(k).toString());
							 //当前处理行存货数量
							 Double nownumber=((EstimateVO)selectedBodys.get(k)).getNinnum().toDouble();
							 //当前行应分摊的费用金额.
							 Double nowexpense=expenseamount*nownumber/number;
							 //将该金额写入页面VO数组中对应存货行的费用金额项中
							 allEstimateVOs[nowRowNO].setNfeemny(new UFDouble(nowexpense));
						 }
					 }
				  }
			  }
		  }
		 //分摊循环处理完毕以后,更新界面.
		 this.getBillCardPanel().getBillModel().setBodyDataVO(allEstimateVOs);
		 this.getBillCardPanel().getBillModel().execLoadFormula();
		 feeFlag = true;//是否费用分摊标志设定为true
		 this.getBillCardPanel().updateUI();
		 if(haveexpense.size()!=0&&haveexpense.size()==selectedAllBillData.size()){
			 //如果有费用的单据数与选择单据数相等,则进行成功提示.
		 MessageDialog.showHintDlg(this, "提示", "成功分摊费用,请点击暂估按钮进行暂估处理.");
		 }
		 /**
		  * 将成功分摊的所有存货行设置为选中状态
		  */
		 for(int x=0;x<haveexpense.size();x++){//使用haveexpense来控制外循环,因为只设置成功分摊的存货
	      String thisICBillPK=haveexpense.get(x).toString();
			 for (int y = 0; y < nRow; y++) {
	    	  if(allEstimateVOs[y].getCgeneralhid()==thisICBillPK){
	    		  //如果表体存货中的入库单PK与成功分摊的入库单PK一致.
	    	      this.getBillCardPanel().getBillModel().setRowState(y, BillModel.SELECTED);
	    	  }
	    	  }
		 }
		 this.getBillCardPanel().updateUI();//更新界面!!!!!!
	  }
	  
	  	/**
	  	 * 后续处理,主要是分摊时的特殊情况提示.
	  	 */
	      if(unknowexpense.size()==selectedAllBillData.size()){
		  //如果无法获取数与选中数相等
		  MessageDialog.showErrorDlg(this, "错误", "无法获取所选单据的费用信息,请检查您的网络连接,或与技术员联系.");
		  return;
		  }else if(unknowexpense.size()>0&&unknowexpense.size()<selectedAllBillData.size()){
			  //如果存在无法获取费用单据,那么..
			  String unknowexpensePKs="";
			  for(int i=0;i<unknowexpense.size();i++){
				  unknowexpensePKs+=";";
				  unknowexpensePKs+=unknowexpense.get(i).toString();
			  }
			  MessageDialog.showHintDlg(this, "警告", "成功进行部分单据的费用分摊,但以下单据无法获取费用:"+unknowexpensePKs);
		  }
		  
		  if(havenotexpense.size()==selectedAllBillData.size()){
			  //如果无费用单据数与选中树相等..那么..
		 	 MessageDialog.showHintDlg(this, "提示", "所选择的单据无费用信息,无需进行费用分摊操作!");
		 	 return;
		  }else if(havenotexpense.size()>0&&havenotexpense.size()<selectedAllBillData.size()){
			  //如果存在无费用的单据..那么...
			  String havenotexpensePKs="";
			  for(int i=0;i<havenotexpense.size();i++){
				  havenotexpensePKs+=";";
				  havenotexpensePKs+=havenotexpense.get(i).toString();
			  }
			  MessageDialog.showHintDlg(this, "警告", "成功进行部分单据的费用分摊,但以下单据无费用信息:"+havenotexpensePKs);
		  }
	  
	  /**
	   * 至此,数据分摊处理完毕!
	   */
	  
//	  EstimateVO[] emvos = (EstimateVO[])getBillCardPanel().getBillData().getBodyValueChangeVOs(EstimateVO.class.getName());
//	  ArrayList rowList = new ArrayList();
//	  for (int h = 0; h < getBillCardPanel().getBillModel().getRowCount(); h++) {
//		if(getBillCardPanel().getBillModel().getRowState(h) == 4){
//			rowList.add(h);
//		}
//	}
//	  if(emvos == null || emvos.length == 0){
//		  showErrorMessage("没有要进行费用分摊的数据!");
//		  return;
//	  }
//	  StringBuffer sql = new StringBuffer(" dr = 0 ");
//		Vector v = new Vector();
//		ArrayList list = new ArrayList();
//		for (int i = 0; i < emvos.length; i++) {
//			String cbillid = emvos[i].getCgeneralhid();
//			if (i == 0) {
//				v.addElement(cbillid);
//				sql.append(" and cbillid = '" + cbillid + "' ");
//			} else {
//				if (!v.contains(cbillid)) {
//					v.addElement(cbillid);
//					sql.append(" or cbillid = '" + cbillid + "' ");
//				}
//			}
//		}
//		InformationCostVO[] infovos = null;
//	 try{
//		 infovos = (InformationCostVO[])JJPuScmPubHelper.querySmartVOs(InformationCostVO.class, null, sql.toString());
//	 } catch(Exception e){
//		 SCMEnv.out(e);
//		 showErrorMessage("获取费用信息失败!");
//	 }
//	 if(infovos == null || infovos.length == 0){
//		 SCMEnv.out("-------无费用信息,不进行费用分摊--------");
//		 return;
//	 }
//	 for (int i = 0; i < v.size(); i++) {
//			UFDouble mny = new UFDouble().ZERO_DBL;
//			UFDouble number = new UFDouble().ZERO_DBL;
//			ArrayList<EstimateVO> emvoList = new ArrayList<EstimateVO>();
//			for (int j = 0; j < infovos.length; j++) {
//				if (infovos[j].getCbillid().equals(v.get(i))) {
//					mny = mny.add(infovos[j].getNoriginalcurmny());
//		//			String costunit = infovos[j].getCcostunitid();
//					number = infovos[j].getNnumber();
//				}
//			}
//			for (int k = 0; k < emvos.length; k++) {
//				if (emvos[k].getCgeneralhid().equals(v.get(i))) {
////					number = number.add(emvos[k].getNinnum());
//					
//					emvoList.add(emvos[k]);
//				}
//			}
//			if(emvoList!=null&&emvoList.size()!=0){
//				UFDouble mny1 = mny;
//			  for (int l = 0; l < emvoList.size(); l++) {
////				  if(l == emvoList.size()-1){
////					  emvoList.get(l).setNfeemny(mny1); 
////				  }
////				  else{
//					  UFDouble innum = emvoList.get(l).getNinnum();
//				 
//				UFDouble freemny = mny.multiply(innum.div(number));
//				emvoList.get(l).setNfeemny(freemny);
//				mny1 = mny1.sub(freemny);
////				  }
//			}	
//			}
//	}
//	 int n = 0;
//	 for (int m = 0; m < rowList.size(); m++) {
//        int rowNO = (Integer)rowList.get(m);
//        getBillCardPanel().getBillModel().setBodyRowVO(emvos[n], m);
//        n++;
//		}
////	 getBillCardPanel().getBillData().setBodyValueVO(emvos);
//	 getBillCardPanel().getBillModel().execLoadFormula();
//	 feeFlag = true;//分摊费用成功后,将是否费用暂估标记设定为true.
//	 SCMEnv.out("-------分摊费用成功--------");  
  }
}