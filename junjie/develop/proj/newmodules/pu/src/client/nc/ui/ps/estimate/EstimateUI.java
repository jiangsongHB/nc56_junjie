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
 * ��������:�ݹ�UI
 * <p>
 * ����:�ܺ���
 * <p>
 * ��������:2001��02��14
 * <p>
 * �޸ģ�2004-09-10 ԬҰ
 * <p>
 * �޸ģ�2007-07-09 ��־ƽ for V502
 * <p>
 * �޸ģ�2008-06-17 �ź췼�������� for V55
 */
public class EstimateUI extends nc.ui.pub.ToftPanel implements BillEditListener,
    javax.swing.event.ListSelectionListener, BillEditListener2, IDataSource, IBillRelaSortListener2, MouseListener,
    IBillModelRowStateChangeEventListener {
	
  //2010-10-17 �Ƿ�����ݹ�--MeiChao ���ע��
  private boolean feeFlag = false; 
  // ������ư�ť
  private ButtonObject m_buttons[] = null;

  // ��ť״̬��0 ������1 �ûң�2 ������
  private int m_nButtonState[] = null;

  // ����
  private BillCardPanel m_billPanel = null;

  // ��ѯģ��������ѯ����
  private UIRadioButton m_rbEst = null;

  private UIRadioButton m_rbUnEst = null;

  // ��λ���룬ϵͳӦ�ṩ������ȡ
  private String m_sUnitCode = getCorpPrimaryKey();

  // ��ѯ����,since v502 �޸ģ�֧���ջ���˾�����������ֻ����һ��
  // private PoQueryCondition m_condClient=null;
  private EstimateQueryCondition m_condClient = null;

  // �²�ѯ�Ի���,since 5.5 by zhaoyha at 2008.10.5
  private EstimateQueryDlg m_condDlg = null;

  // ����
  private EstimateVO m_estimate1VOs[] = null;

  private EstimateVO m_estimate2VOs[] = null;

  private String m_sZG = "N";

  // �ݹ���ʽ
  private String m_sEstimateMode = null;

  // ����ת�뷽ʽ
  private String m_sDiffMode = null;

  // �ݹ�������Դ
  private String m_sEstPriceSource = null;

  // ����Ƿ�����
  private boolean m_bICStartUp = false;

  // ϵͳ��ʼ������ "BD501","BD505","BD301"
  private int m_measure[] = null;

  // �Ƿ��ݹ�Ӧ��
  private UFBoolean m_bZGYF = new UFBoolean(false);

  // ���ұ���
  private String m_sCurrTypeID = null;

  // zhf add
  private POPubSetUI2 m_cardPoPubSetUI2 = null;

  /**
   * Estimate ������ע�⡣
   */
  public EstimateUI() {
    super();
    init();
  }

  /**
   * �����������༭���¼����� ������ ���أ� ���ߣ��ܺ��� ������2001-6-22 14:41:50 �޸ģ���־ƽ FOR V30 �޸ģ���־ƽ FOR
   * V53 �޸ģ��ź췼 FOR v55
   */
  public void afterEdit(BillEditEvent event) {
    int row = event.getRow();
    String key = event.getKey().trim();
    // �޸ı���
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
    // �޸��۱�����
    if ("nexchangeotobrate".equalsIgnoreCase(key)) {
      afterEditWhenNexchRate(row);
      return;
    }

    String strDisCntName = "Ӧ˰�ں�";/*-=notranslate=-*/
    if (m_estimate1VOs[row].getIdiscounttaxtype().intValue() == 1)
      strDisCntName = "Ӧ˰���";
    if (m_estimate1VOs[row].getIdiscounttaxtype().intValue() == 2)
      strDisCntName = "����˰";

    // �޸ı��Һ���������
    if ("nprice".equalsIgnoreCase(key) || "nmoney".equalsIgnoreCase(key) || "ntaxprice".equalsIgnoreCase(key)
        || "ntotalmoney".equalsIgnoreCase(key)) {
      computeBodyData(m_estimate1VOs, strDisCntName, getBillCardPanel(), event, this);
      // ����ֵ�������������ԭ������
      // v55 by zhaoyha at 2008.9.22
      // ������Ҫ�󱾱Ҳ���ԭ������
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
    // �޸�ԭ�Һ���Ϣ��,��������,���򱾱�����
    else if ("noriginalnetprice".equalsIgnoreCase(key) || "noriginalcurmny".equalsIgnoreCase(key)
        || "norgnettaxprice".equalsIgnoreCase(key) || "noriginaltaxpricemny".equalsIgnoreCase(key)) {
      // �޸�ԭ�Һ���������
      computeOrgBodyData(m_estimate1VOs, strDisCntName, getBillCardPanel(), event, this);
      // ԭ��ֵ������������㱾������
      setLocalPriceMnyFrmOrg(getBillCardPanel(), m_sUnitCode, row, (UFDouble) getBillCardPanel().getBodyValueAt(row,
          "nexchangeotobrate"), m_measure[1], m_measure[2], "ninnum");
    }
    // �޸�˰�ʺ�,�ݹ��ɱ����ݹ�Ӧ���ı��Ҷ�Ҫ����,���ݹ�Ӧ����ԭ�Ҳ����ݹ��ɱ��ı�������
    else if ("ntaxrate".equalsIgnoreCase(key)) {
      // �ݹ��ɱ���������
      computeBodyData(m_estimate1VOs, strDisCntName, getBillCardPanel(), event, this);
      // �ݹ�Ӧ��ԭ����������
      computeOrgBodyData(m_estimate1VOs, strDisCntName, getBillCardPanel(), event, this);
      // �ݹ�Ӧ��ԭ�����ݹ�Ӧ����������
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
//            //������UITextField�����ֵ(>=0��<=0) by zhaoyha
//            beforeEdit(newEvent);
//            getBillCardPanel().setBodyValueAt(oldFeeMny, curRow, "nfeemny");
//          }
//        });
//      }
//    }    
  }

  /**
   * �༭�۱����ʺ���
   * <p>
   * <b>examples:</b>
   * <p>
   * ʹ��ʾ��
   * <p>
   * <b>����˵��</b>
   * 
   * @param row
   *          <p>
   * @author zhanghongfang
   * @time 2008-7-28 ����03:03:26
   */

  private void afterEditWhenNexchRate(int row) {

    // �۱�����
    Object bRate = getBillCardPanel().getBodyValueAt(row, "nexchangeotobrate");
    if (bRate == null || bRate.toString().trim().length() == 0) {
      return;
    }
    UFDouble ufBRate = new UFDouble(bRate.toString());

    // m_estimate1VOs[row].setNexchangeotobrate(ufBRate);

    setLocalPriceMnyFrmOrg(getBillCardPanel(), m_sUnitCode, row, ufBRate, m_measure[1], m_measure[2], "ninnum");

  }

  /**
   * ���»�������
   * <p>
   * <b>examples:</b>
   * <p>
   * ʹ��ʾ��
   * <p>
   * <b>����˵��</b>
   * 
   * @param rows
   *          <p>
   * @author zhanghongfang
   * @time 2008-7-28 ����03:03:37
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

      // �ѱ��ֺͻ��ʵı䶯���µ�����
      m_estimate1VOs[rows[i].intValue()].setCurrencytypeid(VOs[rows[i].intValue()].getCurrencytypeid());
      //�۱����� ֱ�Ӵӽ�����ȡ
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
  // * @���� zhf
  // * @����ʱ�䣺2008-6-17����01:56:12
  // * @param iRow
  // * @return void
  // * @˵����(for v5.5)���ñ����л��ʾ���
  // * @�޸��ߣ�
  // * @�޸�ʱ�䣺
  // * @�޸�˵����
  // */
  //
  // protected void setRowDigits_ExchangeRate(int iRow) {
  // //ȡ�ñ���
  // String sCurrId=(String)getBillCardPanel().getBillModel().getValueAt(iRow,
  // "currencytypeid");
  // int[] iaExchRateDigit = getDigits_ExchangeRate(sCurrId);
  // //�õ��۱����۸����ʾ���
  // if(iaExchRateDigit == null || iaExchRateDigit.length == 0){
  // getBillCardPanel().getBillModel().getItemByKey("nexchangeotobrate").setDecimalDigits(2);
  // }else{
  // getBillCardPanel().getBillModel().getItemByKey("nexchangeotobrate").setDecimalDigits(iaExchRateDigit[0]);
  // }
  // }
  /**
   * ���ݱ�������ʵʱ�۱����ʾ��ȣ�ԭ�ҽ��ҵ�񾫶�
   */
  private void setExchangeRateMnyDigit(int row, String sCurrId) {

    // ����������ʾ����
    int[] iaExchRateDigit = getPOPubSetUI2().getBothExchRateDigit(m_sUnitCode, sCurrId);
    int iMnyDigit = getPOPubSetUI2().getMoneyDigitByCurr_Busi(sCurrId);
    getBillCardPanel().getBillModel().getItemByKey("nexchangeotobrate").setDecimalDigits(iaExchRateDigit[0]);
    // ����ֵ
    UFDouble[] daRate = getPOPubSetUI2().getBothExchRateValue(m_sUnitCode, sCurrId,
        nc.ui.po.pub.PoPublicUIClass.getLoginDate());// ��ǰ����ȡ��ʵʱ����
    getBillCardPanel().setBodyValueAt(daRate[0], row, "nexchangeotobrate");
    // �����޸ı�־
    // getBillCardPanel().getBillModel().setRowState(row,
    // BillModel.MODIFICATION);

    getBillCardPanel().getBillModel().getItemByKey("noriginaltaxpricemny").setDecimalDigits(iMnyDigit);
    getBillCardPanel().getBillModel().getItemByKey("noriginalcurmny").setDecimalDigits(iMnyDigit);
    getBillCardPanel().getBillModel().getItemByKey("norgtaxmoney").setDecimalDigits(iMnyDigit);

    // ���¸�ֵ ������Ч
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
          .getStrByID("40040503", "UPP40040503-000033"/* @res "�ݹ����� */), NCLangRes.getInstance().getStrByID(
          "4004050301", "UPP4004050301-000011")/*
                                                 * @res ԭ�ұ��ֲ���Ϊ�գ���¼��ԭ�ұ���
                                                 */);
      return;
    }
    String sCurrId = oCurrId.toString().trim();

    // setRowDigits_ExchangeRate(row);
    setExchangeRateMnyDigit(row, sCurrId);
    if (sCurrId.equalsIgnoreCase(m_sCurrTypeID))// �жϵ�ǰ�����Ƿ�˾��λ�� �����۱����ʱ༭����
      getBillCardPanel().getBillModel().setCellEditable(row, "nexchangeotobrate", false);
    else
      getBillCardPanel().getBillModel().setCellEditable(row, "nexchangeotobrate", true);
    // �۱�����
    Object bRate = getBillCardPanel().getBodyValueAt(row, "nexchangeotobrate");
    if (bRate == null || bRate.toString().trim().length() == 0) {
      return;
    }
    UFDouble ufBRate = new UFDouble(bRate.toString());
    setLocalPriceMnyFrmOrg(getBillCardPanel(), m_sUnitCode, row, ufBRate, m_measure[1], m_measure[2], "ninnum");
  }

  /**
   * ����������������ԭ�Ҽ۸�����Ϣ���㱾�Ҽ۸�����Ϣ �����ݹ�������ڳ��ݹ�ά��
   * <p>
   * <b>examples:</b>
   * <p>
   * ʹ��ʾ��
   * <p>
   * <p>
   * �޸� by zhaoyha at 2008.9.22
   * <p>
   * �ݹ��ɱ���Ӧ���ֿ�������ص��ֶν����˵���
   * <p>
   * ԭ�Ҽ����ݹ��ɱ�Ҳ���ݹ�Ӧ���ı�������
   * <p>
   * ֻ���ݹ�Ӧ������μ�{@link #setYFLocalPriceMnyFrmOrg(BillCardPanel, String, int, UFDouble, int, int, String)}
   * <p>
   * <b>����˵��</b>
   * 
   * @param cardPanel
   * @param corpPk
   * @param row
   * @param nexchrate
   * @param nPriceDecimal
   * @param numfiled
   *          �ݹ�������Ŀ���� ��ͨ�ݹ� "ninnum" , �ڳ��ݹ�"ngaugenum"
   *          <p>
   * @author zhanghongfang
   * @time 2008-8-6 ����09:51:31
   */
  public static void setLocalPriceMnyFrmOrg(BillCardPanel cardPanel, String corpPk, int row, UFDouble nexchrate,
      int nPriceDecimal, int nMnyDecimal, String numfiled) {

    // ԭ�ҽ��
    UFDouble ufNoriginalcurmny = PuPubVO.getUFDouble_NullAsZero(cardPanel.getBodyValueAt(row, "noriginalcurmny"));
    UFDouble ufMoney = null;
    // ԭ��˰��
    // UFDouble ufNoriginaltaxmny = PuPubVO.getUFDouble_NullAsZero(cardPanel
    // .getBodyValueAt(row, "norgtaxmoney"));
    UFDouble ufTaxMny = null;
    // ԭ�Ҽ�˰�ϼ�
    UFDouble ufNoriginalsummny = PuPubVO.getUFDouble_NullAsZero(cardPanel.getBodyValueAt(row, "noriginaltaxpricemny"));
    UFDouble ufSumMny = null;

    UFDouble ufdNum = PuPubVO.getUFDouble_NullAsZero(cardPanel.getBodyValueAt(row, numfiled));
    BusinessCurrencyRateUtil curUtil = POPubSetUI.getCurrArith_Busi(corpPk);

    Object oCurrid = cardPanel.getBodyValueAt(row, "currencytypeid");

    try {
      String localCuid = PiPqPublicUIClass.getNativeCurrencyID();
      //���ԭ��==����
      if(localCuid.equals(oCurrid)){
        //�����ݹ�Ӧ������
        cardPanel.setBodyValueAt(cardPanel.getBodyValueAt(row, "noriginaltaxpricemny"), row, "nzgyfmoney");
        cardPanel.setBodyValueAt(cardPanel.getBodyValueAt(row, "norgnettaxprice"), row, "nzgyfprice");
        cardPanel.setBodyValueAt(cardPanel.getBodyValueAt(row, "noriginalcurmny"), row, "nzgyfnotaxmoney");
        cardPanel.setBodyValueAt(cardPanel.getBodyValueAt(row, "noriginalnetprice"), row, "nzgyfnotaxprice");
        cardPanel.setBodyValueAt(cardPanel.getBodyValueAt(row, "norgtaxmoney"), row, "nzgyftaxmoney");
        //�����ݹ��ɱ�����
        cardPanel.setBodyValueAt(cardPanel.getBodyValueAt(row, "noriginaltaxpricemny"), row, "ntotalmoney");
        cardPanel.setBodyValueAt(cardPanel.getBodyValueAt(row, "norgnettaxprice"), row, "ntaxprice");
        cardPanel.setBodyValueAt(cardPanel.getBodyValueAt(row, "noriginalcurmny"), row, "nmoney");
        cardPanel.setBodyValueAt(cardPanel.getBodyValueAt(row, "noriginalnetprice"), row, "nprice");
        cardPanel.setBodyValueAt(cardPanel.getBodyValueAt(row, "norgtaxmoney"), row, "ntaxmoney");
        return;
      }
      
      // ԭ�ҽ��
      if (oCurrid == null) {
        ufMoney = null;
      }
      else {
        ufMoney = curUtil
            .getAmountByOpp(oCurrid.toString(), localCuid, ufNoriginalcurmny, nexchrate, null, nMnyDecimal);
      }
      cardPanel.setBodyValueAt(ufMoney, row, "nmoney");
      // ��˰���
      cardPanel.setBodyValueAt(ufMoney, row, "nzgyfnotaxmoney");
      // ԭ��˰��
      // if (oCurrid == null) {
      // ufTaxMny = null;
      // }
      // else {

      // }

      // ԭ�Ҽ�˰�ϼ�
      if (oCurrid == null) {
        ufSumMny = null;
      }
      else {
        ufSumMny = curUtil.getAmountByOpp(oCurrid.toString(), localCuid, ufNoriginalsummny, nexchrate, null,
            nMnyDecimal);
      }
      // ��˰�ϼ�
      cardPanel.setBodyValueAt(ufSumMny, row, "ntotalmoney");
      cardPanel.setBodyValueAt(ufSumMny, row, "nzgyfmoney");
      ufTaxMny = ufSumMny.sub(ufMoney);

      // ˰��
      cardPanel.setBodyValueAt(ufTaxMny, row, "ntaxmoney");
      cardPanel.setBodyValueAt(ufTaxMny, row, "nzgyftaxmoney");
      // ����
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
   * ��������������
   * <p>
   * ��ԭ�Ҽ۸�����Ϣ���㱾�Ҽ۸�����Ϣ �����ݹ�������ڳ��ݹ�ά��
   * <p>
   * ֻ���ݹ�Ӧ��ҳǩ�ϵı�������
   * <p>
   * <b>examples:</b>
   * <p>
   * ʹ��ʾ�� <b>����˵��</b>
   * 
   * @param cardPanel
   * @param corpPk
   * @param row
   * @param nexchrate
   * @param nPriceDecimal
   * @param numfiled
   *          �ݹ�������Ŀ���� ��ͨ�ݹ� "ninnum" , �ڳ��ݹ�"ngaugenum"
   *          <p>
   * @author zhaoyha
   * @time 2008-9-27 ����09:51:31
   */
  public static void setYFLocalPriceMnyFrmOrg(BillCardPanel cardPanel, String corpPk, int row, UFDouble nexchrate,
      int nPriceDecimal, int nMnyDecimal, String numfiled) {

    // ԭ�ҽ��
    UFDouble ufNoriginalcurmny = PuPubVO.getUFDouble_NullAsZero(cardPanel.getBodyValueAt(row, "noriginalcurmny"));
    UFDouble ufMoney = null;
    UFDouble ufTaxMny = null;
    // ԭ�Ҽ�˰�ϼ�
    UFDouble ufNoriginalsummny = PuPubVO.getUFDouble_NullAsZero(cardPanel.getBodyValueAt(row, "noriginaltaxpricemny"));
    UFDouble ufSumMny = null;

    UFDouble ufdNum = PuPubVO.getUFDouble_NullAsZero(cardPanel.getBodyValueAt(row, numfiled));
    BusinessCurrencyRateUtil curUtil = POPubSetUI.getCurrArith_Busi(corpPk);

    Object oCurrid = cardPanel.getBodyValueAt(row, "currencytypeid");

    try {
      String localCuid = PiPqPublicUIClass.getNativeCurrencyID();
      //���ԭ��==����
      if(localCuid.equals(oCurrid)){
        cardPanel.setBodyValueAt(cardPanel.getBodyValueAt(row, "noriginaltaxpricemny"), row, "nzgyfmoney");
        cardPanel.setBodyValueAt(cardPanel.getBodyValueAt(row, "norgnettaxprice"), row, "nzgyfprice");
        cardPanel.setBodyValueAt(cardPanel.getBodyValueAt(row, "noriginalcurmny"), row, "nzgyfnotaxmoney");
        cardPanel.setBodyValueAt(cardPanel.getBodyValueAt(row, "noriginalnetprice"), row, "nzgyfnotaxprice");
        cardPanel.setBodyValueAt(cardPanel.getBodyValueAt(row, "norgtaxmoney"), row, "nzgyftaxmoney");
        return;
      }
      
      // ԭ�ҽ��
      if (oCurrid == null) {
        ufMoney = null;
      }
      
      else {
        ufMoney = curUtil
            .getAmountByOpp(oCurrid.toString(), localCuid, ufNoriginalcurmny, nexchrate, null, nMnyDecimal);
      }
      // ��˰���
      cardPanel.setBodyValueAt(ufMoney, row, "nzgyfnotaxmoney");

      // ԭ�Ҽ�˰�ϼ�
      if (oCurrid == null) {
        ufSumMny = null;
      }
      else {
        ufSumMny = curUtil.getAmountByOpp(oCurrid.toString(), localCuid, ufNoriginalsummny, nexchrate, null,
            nMnyDecimal);
      }
      // ��˰�ϼ�
      cardPanel.setBodyValueAt(ufSumMny, row, "nzgyfmoney");
      ufTaxMny = ufSumMny.sub(ufMoney);

      // ˰��
      cardPanel.setBodyValueAt(ufTaxMny, row, "nzgyftaxmoney");
      // ����
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
   * ���������������ɱ��Ҽ۸�����Ϣ����ԭ�Ҽ۸�����Ϣ �����ݹ�������ڳ��ݹ�ά��
   * <p>
   * <b>examples:</b>
   * <p>
   * ʹ��ʾ��
   * <p>
   * <b>����˵��</b>
   * 
   * @param cardPanel
   * @param corpPk
   * @param row
   * @param nexchrate
   * @param nPriceDecimal
   * @param numfiled
   *          �ݹ�������Ŀ���� ��ͨ�ݹ� "ninnum" , �ڳ��ݹ�"ngaugenum"
   *          <p>
   * @author zhanghongfang
   * @time 2008-8-6 ����09:51:31
   */
  public static void setOrgPriceMnyFrmLocal(BillCardPanel cardPanel, String corpPk, int row, UFDouble nexchrate,
      int nPriceDecimal, int nMnyDecimal, String numfiled, String sCurrid) {

    // ���
    UFDouble ufNoriginalcurmny = PuPubVO.getUFDouble_NullAsZero(cardPanel.getBodyValueAt(row, "nmoney"));
    UFDouble ufMoney = null;
    // ˰��
    // UFDouble ufNoriginaltaxmny = PuPubVO.getUFDouble_NullAsZero(cardPanel
    // .getBodyValueAt(row, "ntaxmoney"));
    UFDouble ufTaxMny = null;
    // ��˰�ϼ�
    UFDouble ufNoriginalsummny = PuPubVO.getUFDouble_NullAsZero(cardPanel.getBodyValueAt(row, "ntotalmoney"));
    UFDouble ufSumMny = null;

    UFDouble ufdNum = PuPubVO.getUFDouble_NullAsZero(cardPanel.getBodyValueAt(row, numfiled));
    BusinessCurrencyRateUtil curUtil = POPubSetUI.getCurrArith_Busi(corpPk);

    try {
      String localCuid = PiPqPublicUIClass.getNativeCurrencyID();
      // ԭ�ҽ��
      if (sCurrid == null) {
        ufMoney = null;
      }
      else {
        ufMoney = curUtil.getAmountByOpp(localCuid, sCurrid, ufNoriginalcurmny, nexchrate, null);
      }
      cardPanel.setBodyValueAt(ufMoney, row, "noriginalcurmny");
      // ԭ��˰��
      // if (sCurrid == null) {
      // ufTaxMny = null;
      // }
      // else {

      // }
      cardPanel.setBodyValueAt(ufTaxMny, row, "norgtaxmoney");
      // ԭ�Ҽ�˰�ϼ�
      if (sCurrid == null) {
        ufSumMny = null;
      }
      else {
        ufSumMny = curUtil.getAmountByOpp(localCuid, sCurrid, ufNoriginalsummny, nexchrate, null);
      }
      cardPanel.setBodyValueAt(ufSumMny, row, "noriginaltaxpricemny");

      ufTaxMny = ufSumMny.sub(ufMoney);

      // ����
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
   * �����������б任�¼����� ������ ���أ� ���ߣ��ܺ��� ������2001-6-22 14:41:50 �޸ģ���־ƽ FOR V30
   */
  public void bodyRowChange(BillEditEvent event) {
  }

  /**
   * �����������ı���水ť״̬ ������ ���أ� ���ߣ��ܺ��� ������2001-6-22 14:41:50 �޸ģ���־ƽ FOR V30
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
    // ��ǰѡ��������
    int iSelectedCnt = 0;
    int iRowCnt = getBillCardPanel().getRowCount();
    for (int i = 0; i < iRowCnt; i++) {
      if (getBillCardPanel().getBillModel().getRowState(i) == BillModel.SELECTED) {
        iSelectedCnt++;
      }
    }
    // �����żۼ���
    m_buttons[7].setEnabled(m_sZG.equals("N") && iRowCnt > 0 && iSelectedCnt == 1);
    this.updateButton(m_buttons[7]);

    // ���鰴ť�߼�
    int iSelectedCntTable = getBillCardPanel().getBillTable().getSelectedRowCount();
    m_buttons[8].setEnabled(iRowCnt > 0 && iSelectedCntTable >= 1);
    this.updateButton(m_buttons[8]);
  }

  /**
   * ������������ѯģ�����Ӳ�ѯ���� ������ ���أ� ���ߣ��ܺ��� ������2001-6-22 14:41:50 �޸ģ���־ƽ FOR V30
   */
  private void changeQueryModelLayout() {
    if (m_rbEst != null && m_rbUnEst != null)
      return;

    UILabel label1 = new UILabel(nc.ui.ml.NCLangRes.getInstance().getStrByID("4004050301", "UPP4004050301-000000")/*
                                                                                                                   * @res
                                                                                                                   * "ѡ����ⵥ��"
                                                                                                                   */);
    label1.setBounds(30, 65, 100, 25);

    m_rbEst = new UIRadioButton();
    m_rbEst.setBounds(130, 65, 16, 16);
    m_rbEst.setSelected(true);
    UILabel label2 = new UILabel(nc.ui.ml.NCLangRes.getInstance().getStrByID("4004050301", "UPP4004050301-000001")/*
                                                                                                                   * @res
                                                                                                                   * "δ�ݹ�"
                                                                                                                   */);
    label2.setBounds(146, 65, 60, 25);

    m_rbUnEst = new UIRadioButton();
    m_rbUnEst.setBounds(130, 95, 16, 16);
    UILabel label3 = new UILabel(nc.ui.ml.NCLangRes.getInstance().getStrByID("4004050301", "UPP4004050301-000002")/*
                                                                                                                   * @res
                                                                                                                   * "���ݹ�"
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
   * �����������޸ĵ���,���ȵ���������{ע�⣺���������������ֹ��ݹ�&�ڳ��ݹ����ݹ�������������!}
   * <p>
   * ������
   * <p>
   * ���أ�
   * <p>
   * ���ߣ��ܺ���
   * <p>
   * ������2001-6-22 14:41:50
   * <p>
   * �޸ģ���־ƽ FOR V30
   * <p>
   * �޸ģ���־ƽ FOR V53
   * <p>
   * �޸ģ�Ϊ֧���ڳ��ݹ�Ӧ��������������Ϊ��̬������
   * <p>
   * ����0��CircularlyAccessibleValueObject[] voa, Ҫ���������VO[]
   * <p>
   * ����1��String strDisCntName = "Ӧ˰�ں�";
   * <p>
   * if(m_estimate1VOs[iRowPos].getIdiscounttaxtype().intValue() == 1) s =
   * "Ӧ˰���";
   * <p>
   * if(m_estimate1VOs[iRowPos].getIdiscounttaxtype().intValue() == 2) s =
   * "����˰";
   * <p>
   * ����2��BillCardPanel bcp: getBillCardPanel()
   * <p>
   * ����3��BillEditEvent event
   * 
   * @�޸��� zhf
   * @�޸�ʱ�� 2008-06-27
   * @�޸�˵�� ֧���ݹ�ʱ�ݹ�Ӧ��֧����� ��������Ϣ��Ϊ���ɱ༭ �༭ԭ�Һ�������� �����㹫ʽ��Ϊԭ�Ҽ��������㣩
   */
  public static void computeOrgBodyData(CircularlyAccessibleValueObject[] voa, String strDisCntName, BillCardPanel bcp,
      BillEditEvent event, ToftPanel panelThis) {

    int iRowPos = event.getRow();
    String key = event.getKey().trim();
    if (iRowPos < 0 || voa == null || voa.length == 0) {
      return;
    }
    // ע�⣺���������������ֹ��ݹ�&�ڳ��ݹ����ݹ�������������!
    if (!(voa instanceof EstimateVO[]) && !(voa instanceof OorderVO[])) {
      MessageDialog.showErrorDlg(panelThis, "��ʾ", "��֧�ֵĵ���!");/*-=notranslate=-*/
      return;
    }
    // ���ֵ�����
    boolean bEstFlag = (voa instanceof EstimateVO[]);
    // ���
    Object oMoney = bcp.getBillModel().getValueAt(iRowPos, "noriginalcurmny");
    UFDouble ufdMoney = PuPubVO.getUFDouble_NullAsZero(oMoney);
    // ��˰�ϼ�
    Object oTotalMoney = bcp.getBillModel().getValueAt(iRowPos, "noriginaltaxpricemny");
    UFDouble ufdTotalMoney = PuPubVO.getUFDouble_NullAsZero(oTotalMoney);

    // ���ù����㷨�����־����ԭ�Ҽ��㻹�Ǳ��Ҽ��㣩
    bcp.getBillModel().setValueAt(new UFBoolean(true), iRowPos, "bisOriCal");

    // �����㷨����������
    int[] descriptions = new int[] {
        RelationsCal.DISCOUNT_TAX_TYPE_NAME, // ��˰�����(Ӧ˰�ں���Ӧ˰��ӣ�����˰)
        RelationsCal.DISCOUNT_TAX_TYPE_KEY,// ��˰���
        RelationsCal.NUM,// ������
        RelationsCal.NET_PRICE_ORIGINAL,// ������
        RelationsCal.MONEY_ORIGINAL,// ���
        RelationsCal.NET_TAXPRICE_ORIGINAL,// ����˰����
        RelationsCal.SUMMNY_ORIGINAL, // ��˰�ϼ� --ԭ��
        RelationsCal.TAXRATE, // ˰��
        RelationsCal.DISCOUNT_RATE,// ����
        RelationsCal.PRICE_ORIGINAL,// ����
        RelationsCal.TAXPRICE_ORIGINAL,// ��˰����
        RelationsCal.TAX_ORIGINAL
    // ˰��
    };
    // ����itemkey
    String strNumKey = "ngaugenum";
    if (bEstFlag) {
      strNumKey = "ninnum";
    }
    // ���������itemkey(����������Ӧ)
    String[] keys = new String[] {
        strDisCntName, "idiscounttaxtype", strNumKey, "noriginalnetprice", "noriginalcurmny", "norgnettaxprice",
        "noriginaltaxpricemny", "ntaxrate", "ndiscountrate", "nnetprice", "nnettaxprice", "norgtaxmoney"
    };
    // ����һ���Լ��
    if (key != null && "noriginalcurmny".equalsIgnoreCase(key.trim())) {
      if (bEstFlag) {
        if (((EstimateVO[]) voa)[iRowPos].getNinnum() != null
            && ufdMoney.multiply(((EstimateVO[]) voa)[iRowPos].getNinnum()).doubleValue() < 0.0) {
          MessageDialog.showErrorDlg(panelThis, nc.ui.ml.NCLangRes.getInstance().getStrByID("40040503",
              "UPP40040503-000022")/* @res "�޸Ľ��" */, nc.ui.ml.NCLangRes.getInstance().getStrByID("40040503",
              "UPP40040503-000023")/* @res "�޸Ľ��ܸı���ţ�" */);
          bcp.setBodyValueAt(event.getOldValue(), iRowPos, "noriginalcurmny");
          return;
        }
      }
      else {
        if (((OorderVO[]) voa)[iRowPos].getNgaugenum() != null
            && ufdMoney.multiply(((OorderVO[]) voa)[iRowPos].getNgaugenum()).doubleValue() < 0.0) {
          MessageDialog.showErrorDlg(panelThis, nc.ui.ml.NCLangRes.getInstance().getStrByID("40040503",
              "UPP40040503-000022")/* @res "�޸Ľ��" */, nc.ui.ml.NCLangRes.getInstance().getStrByID("40040503",
              "UPP40040503-000023")/* @res "�޸Ľ��ܸı���ţ�" */);
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
              "UPP40040503-000022")/* @res "�޸Ľ��" */, nc.ui.ml.NCLangRes.getInstance().getStrByID("40040503",
              "UPP40040503-000023")/* @res "�޸Ľ��ܸı���ţ�" */);
          bcp.setBodyValueAt(event.getOldValue(), iRowPos, "noriginaltaxpricemny");
          return;
        }
      }
      else {
        if (((OorderVO[]) voa)[iRowPos].getNgaugenum() != null
            && ufdTotalMoney.multiply(((OorderVO[]) voa)[iRowPos].getNgaugenum()).doubleValue() < 0.0) {
          MessageDialog.showErrorDlg(panelThis, nc.ui.ml.NCLangRes.getInstance().getStrByID("40040503",
              "UPP40040503-000022")/* @res "�޸Ľ��" */, nc.ui.ml.NCLangRes.getInstance().getStrByID("40040503",
              "UPP40040503-000023")/* @res "�޸Ľ��ܸı���ţ�" */);
          bcp.setBodyValueAt(event.getOldValue(), iRowPos, "noriginaltaxpricemny");
          return;
        }
      }
    }
    // ���ù����㷨����׼����˰��Ϊ��ʱ����Ϊ0
    Object oTaxRate = bcp.getBillModel().getValueAt(iRowPos, "ntaxrate");
    if (oTaxRate == null || oTaxRate.toString().trim().equals("")) {
      bcp.getBillModel().setValueAt(new UFDouble(0.0), iRowPos, "ntaxrate");
    }
    // ���ù����㷨����
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
    // ���������null�����0
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
   * �����������޸ĵ���,���ȵ���������{ע�⣺���������������ֹ��ݹ�&�ڳ��ݹ����ݹ�������������!}
   * <p>
   * ������
   * <p>
   * ���أ�
   * <p>
   * ���ߣ��ܺ���
   * <p>
   * ������2001-6-22 14:41:50
   * <p>
   * �޸ģ���־ƽ FOR V30
   * <p>
   * �޸ģ���־ƽ FOR V53
   * <p>
   * �޸ģ�Ϊ֧���ڳ��ݹ�Ӧ��������������Ϊ��̬������
   * <p>
   * ����0��CircularlyAccessibleValueObject[] voa, Ҫ���������VO[]
   * <p>
   * ����1��String strDisCntName = "Ӧ˰�ں�";
   * <p>
   * if(m_estimate1VOs[iRowPos].getIdiscounttaxtype().intValue() == 1) s =
   * "Ӧ˰���";
   * <p>
   * if(m_estimate1VOs[iRowPos].getIdiscounttaxtype().intValue() == 2) s =
   * "����˰";
   * <p>
   * ����2��BillCardPanel bcp: getBillCardPanel()
   * <p>
   * ����3��BillEditEvent event
   */
  public static void computeBodyData(CircularlyAccessibleValueObject[] voa, String strDisCntName, BillCardPanel bcp,
      BillEditEvent event, ToftPanel panelThis) {

    int iRowPos = event.getRow();
    if (iRowPos < 0 || voa == null || voa.length == 0) {
      return;
    }
    // ע�⣺���������������ֹ��ݹ�&�ڳ��ݹ����ݹ�������������!
    if (!(voa instanceof EstimateVO[]) && !(voa instanceof OorderVO[])) {
      MessageDialog.showErrorDlg(panelThis, "��ʾ", "��֧�ֵĵ���!");/*-=notranslate=-*/
      return;
    }
    // ���ֵ�����
    boolean bEstFlag = (voa instanceof EstimateVO[]);
    // ���
    Object oMoney = bcp.getBillModel().getValueAt(iRowPos, "nmoney");
    UFDouble ufdMoney = PuPubVO.getUFDouble_NullAsZero(oMoney);
    // ��˰�ϼ�
    Object oTotalMoney = bcp.getBillModel().getValueAt(iRowPos, "ntotalmoney");
    UFDouble ufdTotalMoney = PuPubVO.getUFDouble_NullAsZero(oTotalMoney);

    // ���ù����㷨�����־����ԭ�Ҽ��㻹�Ǳ��Ҽ��㣩
    bcp.getBillModel().setValueAt(new UFBoolean(false), iRowPos, "bisOriCal");

    // �����㷨����������
    int[] descriptions = new int[] {
        RelationsCal.DISCOUNT_TAX_TYPE_NAME, // ��˰�����(Ӧ˰�ں���Ӧ˰��ӣ�����˰)
        RelationsCal.DISCOUNT_TAX_TYPE_KEY,// ��˰���
        RelationsCal.NUM,// ������
        RelationsCal.NET_PRICE_ORIGINAL,// ������
        RelationsCal.MONEY_ORIGINAL,// ���
        RelationsCal.NET_TAXPRICE_ORIGINAL,// ����˰����
        RelationsCal.SUMMNY_ORIGINAL, // ��˰�ϼ� --ԭ��
        RelationsCal.TAXRATE, // ˰��
        RelationsCal.DISCOUNT_RATE,// ����
        RelationsCal.PRICE_ORIGINAL,// ����
        RelationsCal.TAXPRICE_ORIGINAL,// ��˰����
        RelationsCal.TAX_ORIGINAL
    // ˰��
    };
    // ����itemkey
    String strNumKey = "ngaugenum";
    if (bEstFlag) {
      strNumKey = "ninnum";
    }
    // ���������itemkey(����������Ӧ)
    String[] keys = new String[] {
        strDisCntName, "idiscounttaxtype", strNumKey, "nprice", "nmoney", "ntaxprice", "ntotalmoney", "ntaxrate",
        "ndiscountrate", "nnetprice", "nnettaxprice", "ntaxmoney"
    };
    // ����һ���Լ��
    if (event.getKey() != null && "nmoney".equalsIgnoreCase(event.getKey().trim())) {
      if (bEstFlag) {
        if (((EstimateVO[]) voa)[iRowPos].getNinnum() != null
            && ufdMoney.multiply(((EstimateVO[]) voa)[iRowPos].getNinnum()).doubleValue() < 0.0) {
          MessageDialog.showErrorDlg(panelThis, nc.ui.ml.NCLangRes.getInstance().getStrByID("40040503",
              "UPP40040503-000022")/* @res "�޸Ľ��" */, nc.ui.ml.NCLangRes.getInstance().getStrByID("40040503",
              "UPP40040503-000023")/* @res "�޸Ľ��ܸı���ţ�" */);
          bcp.setBodyValueAt(event.getOldValue(), iRowPos, "nmoney");
          return;
        }
      }
      else {
        if (((OorderVO[]) voa)[iRowPos].getNgaugenum() != null
            && ufdMoney.multiply(((OorderVO[]) voa)[iRowPos].getNgaugenum()).doubleValue() < 0.0) {
          MessageDialog.showErrorDlg(panelThis, nc.ui.ml.NCLangRes.getInstance().getStrByID("40040503",
              "UPP40040503-000022")/* @res "�޸Ľ��" */, nc.ui.ml.NCLangRes.getInstance().getStrByID("40040503",
              "UPP40040503-000023")/* @res "�޸Ľ��ܸı���ţ�" */);
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
              "UPP40040503-000022")/* @res "�޸Ľ��" */, nc.ui.ml.NCLangRes.getInstance().getStrByID("40040503",
              "UPP40040503-000023")/* @res "�޸Ľ��ܸı���ţ�" */);
          bcp.setBodyValueAt(event.getOldValue(), iRowPos, "ntotalmoney");
          return;
        }
      }
      else {
        if (((OorderVO[]) voa)[iRowPos].getNgaugenum() != null
            && ufdTotalMoney.multiply(((OorderVO[]) voa)[iRowPos].getNgaugenum()).doubleValue() < 0.0) {
          MessageDialog.showErrorDlg(panelThis, nc.ui.ml.NCLangRes.getInstance().getStrByID("40040503",
              "UPP40040503-000022")/* @res "�޸Ľ��" */, nc.ui.ml.NCLangRes.getInstance().getStrByID("40040503",
              "UPP40040503-000023")/* @res "�޸Ľ��ܸı���ţ�" */);
          bcp.setBodyValueAt(event.getOldValue(), iRowPos, "ntotalmoney");
          return;
        }
      }
    }
    // ���ù����㷨����׼����˰��Ϊ��ʱ����Ϊ0
    Object oTaxRate = bcp.getBillModel().getValueAt(iRowPos, "ntaxrate");
    if (oTaxRate == null || oTaxRate.toString().trim().equals("")) {
      bcp.getBillModel().setValueAt(new UFDouble(0.0), iRowPos, "ntaxrate");
    }
    // ���ù����㷨����
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
    // ���������null�����0
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
   * �������������Ҫ��ӡ���ֶ����� ������ ���أ� ���ߣ��ܺ��� ������2001-6-22 14:41:50 �޸ģ���־ƽ FOR V30
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
   * �������������Ҫ��ӡ���ֶ����� ������ ���أ� ���ߣ��ܺ��� ������2001-6-22 14:41:50 �޸ģ���־ƽ FOR V30
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
   * ������������õ��ݿ�Ƭģ��ؼ� ������ ���أ� ���ߣ��ܺ��� ������2001-6-22 14:41:50 �޸ģ���־ƽ FOR V30
   */
  private BillCardPanel getBillCardPanel() {
    if (m_billPanel == null) {
      try {
        m_billPanel = new BillCardPanel();
        // user code begin {1}
        // ����ģ��
        BillData bd = new BillData(m_billPanel.getTempletData("40040503010000000000"));

        // m_billPanel.loadTemplet("40040503010000000000");
        
        //��δ�ݹ�Ӧ������ʾ�ݹ�Ӧ��ҳǩ
        if(!m_bZGYF.booleanValue())
          bd.removeTabItems(BillData.BODY, "zgyf_table");
        
        bd = initDecimal(bd);

        m_billPanel.setBillData(bd);
        //m_billPanel.setShowThMark(true);
        m_billPanel.setTatolRowShow(true);
        m_billPanel.setTatolRowShow("zgyf_table",true);

        // ���ӵ��ݱ༭����
        m_billPanel.addEditListener(this);
        m_billPanel.addBodyEditListener2(this);
        // ��������ҳǩ���Ҽ��˵� V55 by zhaoyha at 2008.9.25
        for (String strTableCode : m_billPanel.getBillData().getTableCodes(BillItem.BODY)) {
          m_billPanel.setBodyMenuShow(strTableCode, false);
        }

        m_billPanel.getBillTable().getSelectionModel().addListSelectionListener(this);

        // since v53, ��Ӧ���ѡ�кż���
        m_billPanel.getBodyPanel().getRowNOTable().setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        m_billPanel.getBodyPanel().getRowNOTable().getSelectionModel().addListSelectionListener(this);
        //
        m_billPanel.getBodyPanel().getRowNOTable().addMouseListener(this);
        m_billPanel.getBillModel().addSortRelaObjectListener2(this);

        // ����ѡ��ģʽ V55 by zhaoyha at 2008.9.25
        PuTool.setLineSelected(m_billPanel);
        m_billPanel.setBodyMultiSelect(true);

        // since v55, ѡ�и�ѡ���¼�����
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
                                                                                           * "����ģ��"
                                                                                           */,
            nc.ui.ml.NCLangRes.getInstance().getStrByID("40040503", "UPP40040503-000025")/*
                                                                                           * @res
                                                                                           * "ģ�岻����!"
                                                                                           */);
        return null;

      }
    }
    return m_billPanel;
  }

  /**
   * ���������� ������ ���أ� ���ߣ��ܺ��� ������2001-6-22 14:41:50 �޸ģ���־ƽ FOR V30
   */
  public String[] getDependentItemExpressByExpress(String itemName) {
    return null;
  }

  /**
   * �������������Ҫ��ӡ���ֶε�ֵ ������ ���أ� ���ߣ��ܺ��� ������2001-6-22 14:41:50 �޸ģ���־ƽ FOR V30
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
   * ������������ô�ӡģ������ ������ ���أ� ���ߣ��ܺ��� ������2001-6-22 14:41:50 �޸ģ���־ƽ FOR V30
   */
  public String getModuleName() {
    return "4004050301";
  }

  /**
   * ����ʵ�ָ÷���������ҵ�����ı��⡣
   * 
   * @version (00-6-6 13:33:25)
   * @return java.lang.String
   */
  public String getTitle() {
    return nc.ui.ml.NCLangRes.getInstance().getStrByID("4004050301", "UPT4004050301-000002")/*
                                                                                             * @res
                                                                                             * "�ݹ�����"
                                                                                             */;
  }

  /**
   * ������������ʼ�� ������ ���أ� ���ߣ��ܺ��� ������2001-6-22 14:41:50 �޸ģ�2004-09-10 ԬҰ
   */
  public void init() {
    String strErrInf = initpara();
    if (strErrInf != null) {
      MessageDialog.showErrorDlg(this,
          nc.ui.ml.NCLangRes.getInstance().getStrByID("40040503", "UPP40040503-000028")/*
                                                                                         * @res
                                                                                         * "��ȡϵͳ��ʼ����������"
                                                                                         */,
          strErrInf);
      return;
    }
    // ��ʾ��ť
    m_buttons = new ButtonObject[10];  //����ť���飬���ӷ��÷�̯��ť add by QuSida 2010-9-15 (��ɽ����)
    m_buttons[0] = new ButtonObject(nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UC001-0000041")/*
                                                                                                           * @res
                                                                                                           * "ȫѡ"
                                                                                                           */, nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UC001-0000041")/*
                                                                                 * @res
                                                                                 * "ȫѡ"
                                                                                 */, 2, "ȫѡ");
    m_buttons[1] = new ButtonObject(nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UC001-0000042")/*
                                                                                                           * @res
                                                                                                           * "ȫ��"
                                                                                                           */, nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UC001-0000042")/*
                                                                                 * @res
                                                                                 * "ȫ��"
                                                                                 */, 2, "ȫ��");
    m_buttons[2] = new ButtonObject(
        nc.ui.ml.NCLangRes.getInstance().getStrByID("4004050301", "UPT4004050301-000003")/*
                                                                                           * @res
                                                                                           * "�ݹ�"
                                                                                           */,
        nc.ui.ml.NCLangRes.getInstance().getStrByID("4004050301", "UPT4004050301-000003")/*
                                                                                           * @res
                                                                                           * "�ݹ�"
                                                                                           */, 2, "�ݹ�");
    m_buttons[3] = new ButtonObject(
        nc.ui.ml.NCLangRes.getInstance().getStrByID("4004050301", "UPT4004050301-000004")/*
                                                                                           * @res
                                                                                           * "����"
                                                                                           */,
        nc.ui.ml.NCLangRes.getInstance().getStrByID("4004050301", "UPT4004050301-000004")/*
                                                                                           * @res
                                                                                           * "����"
                                                                                           */, 2, "����");
    m_buttons[4] = new ButtonObject(nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UC001-0000006")/*
                                                                                                           * @res
                                                                                                           * "��ѯ"
                                                                                                           */, nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UC001-0000006")/*
                                                                                 * @res
                                                                                 * "��ѯ"
                                                                                 */, 2, "��ѯ");
    m_buttons[5] = new ButtonObject(
        nc.ui.ml.NCLangRes.getInstance().getStrByID("4004050301", "UPT4004050301-000005")/*
                                                                                           * @res
                                                                                           * "��ӡԤ��"
                                                                                           */,
        nc.ui.ml.NCLangRes.getInstance().getStrByID("4004050301", "UPT4004050301-000005")/*
                                                                                           * @res
                                                                                           * "��ӡԤ��"
                                                                                           */, 2, "��ӡԤ��");
    m_buttons[6] = new ButtonObject(nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UC001-0000007")/*
                                                                                                           * @res
                                                                                                           * "��ӡ"
                                                                                                           */, nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UC001-0000007")/*
                                                                                 * @res
                                                                                 * "��ӡ"
                                                                                 */, 2, "��ӡ");
    m_buttons[7] = new ButtonObject(
        nc.ui.ml.NCLangRes.getInstance().getStrByID("4004050301", "UPT4004050301-000007")/*
                                                                                           * @res
                                                                                           * "�����żۼ���"
                                                                                           */,
        nc.ui.ml.NCLangRes.getInstance().getStrByID("4004050301", "UPT4004050301-000007")/*
                                                                                           * @res
                                                                                           * "�����żۼ���"
                                                                                           */, 2,
        "�����żۼ���");
    m_buttons[8] = new ButtonObject(
        nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "SCMCOMMON000000019")/* @res"����" */, nc.ui.ml.NCLangRes
            .getInstance().getStrByID("common", "SCMCOMMON000000019")/* @res"����" */, 2, "����"); /*-=notranslate=-*/
    
    m_buttons[9] = new ButtonObject("�����ݹ�","�����ݹ�","�����ݹ�");//�����ݹ���ť add by QuSida (��ɽ����) 2010-9-15
    
    this.setButtons(m_buttons);
    
   

    // ��ʾ����
    setLayout(new java.awt.BorderLayout());
    add(getBillCardPanel(), java.awt.BorderLayout.CENTER);
    getBillCardPanel().setEnabled(false);

    // ��˰��� 0Ӧ˰�ں� 1Ӧ˰��� 2����˰
    UIComboBox comItem = (UIComboBox) getBillCardPanel().getBodyItem("idiscounttaxtype").getComponent();
    getBillCardPanel().getBodyItem("idiscounttaxtype").setWithIndex(true);
    comItem.addItem(nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPP40040401-000105")/*
                                                                                                   * @res
                                                                                                   * "Ӧ˰�ں�"
                                                                                                   */);
    comItem.addItem(nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPP40040401-000106")/*
                                                                                                   * @res
                                                                                                   * "Ӧ˰���"
                                                                                                   */);
    comItem.addItem(nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPP40040401-000107")/*
                                                                                                   * @res
                                                                                                   * "����˰"
                                                                                                   */);
    comItem.setTranslate(true);

    // ��ʼ��ť״̬
    m_nButtonState = new int[m_buttons.length];

    // ���ó���ѯ������а�ťΪ��
    for (int i = 0; i < 5; i++) {
      m_nButtonState[i] = 1;
    }
    m_nButtonState[4] = 0;
    m_nButtonState[5] = 0;
    m_nButtonState[6] = 0;
    m_nButtonState[7] = 0;
    m_nButtonState[9] = 0; //�����ݹ�
    changeButtonState();

  }

  // /**
  // *
  // * @���� zhf
  // * @����ʱ�䣺2008-7-4����09:21:42
  // * @return void
  // * @˵����(for v5.5)
  // * @�޸��ߣ�
  // * @�޸�ʱ�䣺
  // * @�޸�˵����
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
   * ��������:��ʼ��С��λ ������ ���أ� ���ߣ��ܺ��� ������2001-6-22 14:41:50 �޸ģ�2004-09-10 ԬҰ
   */
  private BillData initDecimal(BillData bd) {
    // ���ϵͳ��ʼ������

    if (m_measure == null || m_measure.length == 0) {
      MessageDialog.showErrorDlg(this,
          nc.ui.ml.NCLangRes.getInstance().getStrByID("40040503", "UPP40040503-000026")/*
                                                                                         * @res
                                                                                         * "���ϵͳ��ʼ������"
                                                                                         */,
          NCLangRes.getInstance().getStrByID("common", "MT7", null, new String[] {
            "��λ��"
          }));/* MT7=��ȡ{0}ʧ�� */
      ;
      return null;
    }

    // ���ϵͳ��ʼ������
    // ���������С��λ
    int nMeasDecimal = m_measure[0];
    // ��òɹ�����С��λ
    int nPriceDecimal = m_measure[1];
    // ��òɹ����С��λ
    int nMoneyDecimal = m_measure[2];

    // ��õ���Ԫ�ض�Ӧ�Ŀؼ�,���޸Ŀؼ�������
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

    // zhf add for ha 2008-05-16 ֧�����
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

    // ֧�ַ����ݹ�------���ý�� zhf 2008/06/24
    item7 = bd.getBodyItem("nfeemny");
    if (item7 != null) {
      item7.setDecimalDigits(nMoneyDecimal);
    }

    // ------ by zhaoyha at 2008.10.23 -----------------------
    // ֧���ݹ�Ӧ��ҳǩ�ı���
    BillItem item8 = bd.getBodyItem("nzgyfnotaxprice"); // Ӧ��������˰����
    if (item8 != null) {
      item8.setDecimalDigits(nPriceDecimal);
    }
    item8 = bd.getBodyItem("nzgyfnotaxmoney"); // Ӧ��������˰���
    if (item8 != null) {
      item8.setDecimalDigits(nMoneyDecimal);
    }
    item8 = bd.getBodyItem("nzgyfprice"); // Ӧ�����Һ�˰����
    if (item8 != null) {
      item8.setDecimalDigits(nPriceDecimal);
    }
    item8 = bd.getBodyItem("nzgyfmoney"); // Ӧ�����Һ�˰���
    if (item8 != null) {
      item8.setDecimalDigits(nMoneyDecimal);
    }

    return bd;
  }

  /**
   * Ϊ�˼��ٳ�ʼ��ʱǰ��̨�����Ĵ�����һ���Ի�ȡ���ϵͳ���� ����:ԬҰ ���ڣ�2004-09-09
   */
  public String initpara() {
    try {
      Object[] objs = null;
      ServcallVO[] scds = new ServcallVO[2];

      // ���ϵͳ��ʼ������
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

      // ����Ƿ�����
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

      // �������۾���
      m_measure = (int[]) objs[0];
      if (m_measure == null) {
        SCMEnv.out("δ��ȡ��ʼ������������С��λ[BD501]���ɹ�/���۵���С��λ[BD505], ����...");
        return "δ��ȡ��ʼ������������С��λ[BD501]���ɹ�/���۵���С��λ[BD505], ����...";
      }
      // ����Ƿ�����
      if (objs[1] == null) {
        SCMEnv.out("δ��ȡ��ʼ������������Ƿ�����, ����...");
        return "δ��ȡ��ʼ������������Ƿ�����, ����...";
      }
      m_bICStartUp = ((UFBoolean) objs[1]).booleanValue();

      // ���ϵͳ���õ��ݹ���ʽ�Ͳ���ת�뷽ʽ
      Hashtable hTemp = SysInitBO_Client.queryBatchParaValues(m_sUnitCode, new String[] {
          "PO12", "PO13", "PO27", "PO52", "BD301"
      });
      if (hTemp == null || hTemp.size() == 0) {
        SCMEnv.out("δ��ȡ��ʼ������PO12,PO13,PO27,PO52,BD301 ����...");
        return "δ��ȡ��ʼ������PO12,PO13,PO27,PO52,BD301 ����...";
      }
      if (hTemp.get("PO12") == null) {
        SCMEnv.out("δ��ȡ��ʼ�������ݹ�����ʽ[PO12], ����...");
        return "δ��ȡ��ʼ�������ݹ�����ʽ[PO12], ����...";
      }
      else {
        Object temp = hTemp.get("PO12");
        m_sEstimateMode = temp.toString();
      }

      if (hTemp.get("PO13") == null) {
        SCMEnv.out("δ��ȡ��ʼ����������ת�뷽ʽ[PO13], ����...");
        return "δ��ȡ��ʼ����������ת�뷽ʽ[PO13], ����...";
      }
      else {
        Object temp = hTemp.get("PO12");
        m_sDiffMode = temp.toString();
      }

      if (hTemp.get("PO27") == null) {
        SCMEnv.out("δ��ȡ��ʼ�������ݹ�������Դ[PO27], ����...");
        return "δ��ȡ��ʼ�������ݹ�������Դ[PO27], ����...";
      }
      else {
        Object temp = hTemp.get("PO27");
        m_sEstPriceSource = temp.toString();
      }

      if (hTemp.get("PO52") == null) {
        SCMEnv.out("δ��ȡ��ʼ������:��ⵥ�ݹ�ʱ�Ƿ��ݹ�Ӧ��[PO52], ����...");
        return "δ��ȡ��ʼ������:��ⵥ�ݹ�ʱ�Ƿ��ݹ�Ӧ��[PO52], ����...";
      }
      else {
        Object temp = hTemp.get("PO52");
        m_bZGYF = new UFBoolean(temp.toString());
      }

      if (hTemp.get("BD301") == null) {
        SCMEnv.out("δ��ȡ��ʼ����������λ��[BD301], ����...");
        return "δ��ȡ��ʼ����������λ��[BD301], ����...";
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
   * ��ѯģ���ʼ�� ����:ԬҰ ���ڣ�2004-09-10
   */
  public void initQueryModel() {
    if (m_condClient == null) {
      // ��ʼ����ѯģ��
      m_condClient = new EstimateQueryCondition(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("4004050301",
          "UPP4004050301-000004")/*
                                   * @res "�ݹ���ⵥ��ѯ"
                                   */);
      m_condClient.setTempletID(m_sUnitCode, "4004050301", getClientEnvironment().getUser().getPrimaryKey(), null,
          "PS03");

      changeQueryModelLayout();
      m_condClient.setValueRef("dbilldate", "����");
      m_condClient.setValueRef("cbillmaker", "����Ա");

      // ��������
      UIRefPane deptRef = new UIRefPane();
      deptRef.setRefNodeName("���ŵ���");
      m_condClient.setValueRef("cdeptid", deptRef);
      // ��Ա����
      UIRefPane psnRef = new UIRefPane();
      psnRef.setRefNodeName("��Ա����");
      m_condClient.setValueRef("coperator", psnRef);

      UIRefPane vendorRef = new UIRefPane();
      vendorRef.setRefNodeName("��Ӧ�̵���");
      vendorRef.getRefModel().addWherePart(" and frozenflag = 'N'");
      m_condClient.setValueRef("cvendorbaseid", vendorRef);

      UIRefPane biztypeRef = new UIRefPane();
      biztypeRef.setRefNodeName("ҵ������");
      m_condClient.setValueRef("cbiztype", biztypeRef);

      m_condClient.setDefaultValue("dbilldate", "dbilldate", getClientEnvironment().getDate().toString());

      // �����Զ���������
      nc.ui.scm.pub.def.DefSetTool.updateQueryConditionClientUserDef(m_condClient, m_sUnitCode, "icbill", "vuserdef",
          "vuserdef");
      m_condClient.setIsWarningWithNoInput(true);
      /* ���Ļ��������ܱ����� */
      m_condClient.setSealedDataShow(true);

      // ����Ȩ�޿���
      m_condClient.setCorpRefs("B.pk_invoicecorp", new String[] {
          "cvendorbaseid", "cdeptid", "coperator", "invcode", "cwarehouseid", "cprojectid"
      });
//      m_condClient.setRefsDataPowerConVOs(nc.ui.pub.ClientEnvironment.getInstance().getUser().getPrimaryKey(),
//          new String[] {
//            nc.ui.pub.ClientEnvironment.getInstance().getCorporation().getPrimaryKey()
//          }, new String[] {
//              "��Ӧ�̵���", "���ŵ���", "��Ա����", "�������", "�ֿ⵵��", "��Ŀ������"
//          }, new String[] {
//              "cvendorbaseid", "cdeptid", "coperator", "invcode", "cwarehouseid", "cprojectid"
//          }, new int[] {
//              0, 0, 0, 0, 0, 0
//          });
    }
  }

  /**
   * ��������:���Ҫ��ӡ���ֶ��Ƿ�Ϊ���� ������ ���أ� ���ߣ��ܺ��� ������2001-6-22 14:41:50 �޸ģ���־ƽ FOR V30
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
   * ����ʵ�ָ÷�������Ӧ��ť�¼���
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
                                                                                                   * "ȫѡ�ɹ�"
                                                                                                   */);
    }
    else if (bo == m_buttons[1]) {
      onSelectNo();
      showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "4004COMMON000000034")/*
                                                                                                   * @res
                                                                                                   * "ȫ���ɹ�"
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
                                                                                       * "��ѯ���"
                                                                                       */);
    }
    else if (bo == m_buttons[5]) {
      onPreview();
      showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "4004COMMON000000021")/*
                                                                                                   * @res
                                                                                                   * "��ӡԤ�����"
                                                                                                   */);
    }
    else if (bo == m_buttons[6]) {
      onPrint();
      showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UCH041")/*
                                                                                       * @res
                                                                                       * "��ӡ�ɹ�"
                                                                                       */);
    }
    else if (bo == m_buttons[7]) {
      onHQHP();
    }
    else if (bo == m_buttons[8]) {
      onLinkQuery();
      showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "4004COMMON000000019")/*
                                                                                                   * @res
                                                                                                   * "����ɹ�"
                                                                                                   */);
    }
    else if(bo == m_buttons[9]){
    	onCostDistribute();
    	 showHintMessage("�����ݹ�");
    }
  }

  /**
   * ����������
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
   * ��������:�ݹ� ������ ���أ� ���ߣ��ܺ��� ������2001-6-22 14:41:50 �޸ģ���־ƽ FOR V30
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
                                                                                         * "��ⵥ�ݹ�"
                                                                                         */,
          nc.ui.ml.NCLangRes.getInstance().getStrByID("40040503", "UPP40040503-000031")/*
                                                                                         * @res
                                                                                         * "δѡ����ⵥ��"
                                                                                         */);
      return;
    }

    // ���¾���
    updateEstimate1VO(nSelected);

    Vector<EstimateVO> vTemp = new Vector<EstimateVO>();
    for (int i = 0; i < nSelected.length; i++)
      vTemp.addElement(m_estimate1VOs[nSelected[i].intValue()]);
    EstimateVO VOs[] = new EstimateVO[vTemp.size()];
    vTemp.copyInto(VOs);
    //ͳһ������ǰ��� by zhaoyha at 2009.9
    if(!checkBeforeSave(VOs, nSelected)) return;

    try {
      timer.addExecutePhase("���ݿ����$$$$ǰ$$$����ʱ��#######");

      java.util.ArrayList paraList = new java.util.ArrayList();
      // paraList.add(getClientEnvironment().getUser().getPrimaryKey());
      // paraList.add(getClientEnvironment().getDate());
      paraList.add(new ClientLink(getClientEnvironment()));
      paraList.add(m_sEstimateMode);
      paraList.add(m_sDiffMode);
      paraList.add(m_bZGYF);//����˾������PO52: �����ݹ��Ƿ��ݹ�Ӧ�� ��������б�.
      paraList.add(m_sCurrTypeID);
      if(feeFlag){
    	  EstimateHelper.feeEstimate(VOs, paraList);
      }
      else 
    	  EstimateHelper.estimate(VOs, paraList);

      timer.addExecutePhase("���ݿ����$$$$����$$$����ʱ��#######");
      

    }
    catch (java.sql.SQLException e) {
      MessageDialog.showErrorDlg(this,
          nc.ui.ml.NCLangRes.getInstance().getStrByID("40040503", "UPP40040503-000030")/*
                                                                                         * @res
                                                                                         * "��ⵥ�ݹ�"
                                                                                         */,
          nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000412")/*
                                                                                           * @res
                                                                                           * "SQL������"
                                                                                           */);
      SCMEnv.out(e);
      return;
    }
    catch (ArrayIndexOutOfBoundsException e) {
      MessageDialog.showErrorDlg(this,
          nc.ui.ml.NCLangRes.getInstance().getStrByID("40040503", "UPP40040503-000030")/*
                                                                                         * @res
                                                                                         * "��ⵥ�ݹ�"
                                                                                         */,
          nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000426")/*
                                                                                           * @res
                                                                                           * "����Խ�����"
                                                                                           */);
      SCMEnv.out(e);
      return;
    }
    catch (NullPointerException e) {
      MessageDialog.showErrorDlg(this,
          nc.ui.ml.NCLangRes.getInstance().getStrByID("40040503", "UPP40040503-000030")/*
                                                                                         * @res
                                                                                         * "��ⵥ�ݹ�"
                                                                                         */,
          nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000427")/*
                                                                                           * @res
                                                                                           * "��ָ�����"
                                                                                           */);
      SCMEnv.out(e);
      return;
    }
    catch (nc.vo.pub.BusinessException e) {
      MessageDialog.showErrorDlg(this,
          nc.ui.ml.NCLangRes.getInstance().getStrByID("40040503", "UPP40040503-000030")/*
                                                                                         * @res
                                                                                         * "��ⵥ�ݹ�"
                                                                                         */, e
              .getMessage());
      SCMEnv.out(e);
      return;
    }
    catch (Exception e) {
      MessageDialog.showErrorDlg(this,
          nc.ui.ml.NCLangRes.getInstance().getStrByID("40040503", "UPP40040503-000030")/*
                                                                                         * @res
                                                                                         * "��ⵥ�ݹ�"
                                                                                         */, e
              .getMessage());
      SCMEnv.out(e);
      return;
    }
    this
        .showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("40040503", "UPP40040503-000034")/*
                                                                                                       * @res
                                                                                                       * "��ⵥ�ݹ��ɹ���"
                                                                                                       */);

    // �����ݹ���ʽ��������������
    // if(m_sEstimateMode.equals("��������")){}
    // else if(m_sEstimateMode.equals("�����س�")){}

    // �ݹ������ⵥ������ʾ�ڽ���
    if (vv == null || vv.size() == 0) {
      // ������ⵥ���ݹ����
      getBillCardPanel().getBillData().clearViewData();
      getBillCardPanel().updateUI();
      // ����ѯ������Ϊ��
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

    // ���ð�ť״̬�����ݹ������ݣ�ȫ����ȫ��Ϊ����
    for (int i = 0; i < 5; i++) {
      m_nButtonState[i] = 0;
    }
    m_nButtonState[1] = 1;
    m_nButtonState[2] = 1;
    m_nButtonState[3] = 1;
    m_nButtonState[7] = 0;
    changeButtonState();
    timer.addExecutePhase("���ݿ����$$$$��$$$����ʱ��#######");
    //
    timer.showAllExecutePhase("�ݹ�����UIʱ��ֲ���");
    feeFlag = false;
  }

  /**
   * ��������:��ӡԤ�� ������ ���أ� ���ߣ��ܺ��� ������2004-3-24 14:41:50 �޸ģ���־ƽ FOR V30
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
   * ��������:��ӡ ������ ���أ� ���ߣ��ܺ��� ������2004-3-24 14:41:50 �޸ģ���־ƽ FOR V30
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
   * �����żۼ���
   */
  private void onHQHP() {

    if (m_sZG.equals("N") && m_estimate1VOs != null && m_estimate1VOs.length > 0) {
      int nSelected[] = getBillCardPanel().getBillTable().getSelectedRows();
      if (nSelected != null && nSelected.length > 0) {
        // ���������ż��ṩ�Ľӿ�,��ȡ��˰����
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

        // ��ȡ��˰����,�Զ���������������Ŀ
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
              SCMEnv.out("�����ż�δ��ȫȡ�ü۸���Ϣ(���ۡ���˰����)");/*-=notranslate=-*/
            }
          }
        }
      }
    }
  }
  
  /**
   * ��������:��ⵥ��ѯ ������ ���أ� ���ߣ��ܺ��� ������2004-3-24 14:41:50 �޸ģ�2004-09-10 ԬҰ
   * �޸ģ�2008-06-17 ���t�� for v55
   */
  public void onQuery() {

    if (!m_bICStartUp) {
      // ���δ����
      MessageDialog.showErrorDlg(this, 
          NCLangRes.getInstance().getStrByID("40040503", "UPP40040503-000035")/* @res "��ⵥ��ѯ" */,
          NCLangRes.getInstance().getStrByID("40040503","UPP40040503-000036")/* @res "���δ���ã���������ⵥ��" */);
      return;
    }

    initQueryModel();

    m_condClient.hideCorp();
    m_condClient.hideUnitButton();
    m_condClient.showModal();
    Timer debugTime = new Timer();
    debugTime.start();
    if (m_condClient.isCloseOK()) {
      // ��ȡ��ⵥ��ѯ����
      ConditionVO conditionVO[] = m_condClient.getConditionVO();

      // ���ð�ť״̬�����ݹ������ݣ�ȫ����ȫ��Ϊ����
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
      debugTime.addExecutePhase("��ȡ��ⵥ��ѯ����");
      
      // ��ѯ
      try {
        if (m_sZG.toUpperCase().equals("N")) {
          m_estimate1VOs = EstimateHelper.queryEstimate(m_sUnitCode, conditionVO, m_sZG, m_sEstPriceSource);
          if (m_estimate1VOs == null || m_estimate1VOs.length == 0) {
            MessageDialog.showHintDlg(this,NCLangRes.getInstance().getStrByID("40040503", "UPP40040503-000035")/* @res "��ⵥ��ѯ" */,
                NCLangRes.getInstance().getStrByID("40040503", "UPP40040503-000037")/* @res "û�з�����������ⵥ��" */);

            // �������
            getBillCardPanel().getBillModel().clearBodyData();
            getBillCardPanel().updateUI();
            // ȫѡΪ��
            m_nButtonState[0] = 1;
            changeButtonState();
            return;
          }
          debugTime.addExecutePhase("��ѯ��ⵥ-δ�ݹ�");
        }
        else {
          m_estimate2VOs = EstimateHelper.queryEstimate(m_sUnitCode, conditionVO, m_sZG, m_sEstPriceSource);
          if (m_estimate2VOs == null || m_estimate2VOs.length == 0) {
            MessageDialog.showHintDlg(this,
                NCLangRes.getInstance().getStrByID("40040503","UPP40040503-000035")/* @res "��ⵥ��ѯ" */,
                NCLangRes.getInstance().getStrByID("40040503","UPP40040503-000037")/* @res "û�з�����������ⵥ��" */);
            // �������
            getBillCardPanel().getBillModel().clearBodyData();
            getBillCardPanel().updateUI();
            // ȫѡΪ��
            m_nButtonState[0] = 1;
            changeButtonState();
            return;
          }
          debugTime.addExecutePhase("��ѯ��ⵥ-���ݹ�");
        }

        // ����������
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
        debugTime.addExecutePhase("����������");

        // ����˰�ʺͼ�˰�ϼ�, ���㺬˰���ۺͼ�˰�ϼ�
        vTemp = new Vector<EstimateVO>();
        if ("N".equalsIgnoreCase(m_sZG)) {
          for (int i = 0; i < m_estimate1VOs.length; i++) {
            m_estimate1VOs[i].setNdiscountrate(new UFDouble(100));
          }
        }
        else {
          for (int i = 0; i < m_estimate2VOs.length; i++) {
            // �㰲��Ŀ���⣬��ѯ�Ѿ��ݹ���������δ��ʾ˰��
            m_estimate2VOs[i].setAttributeValue("ntaxmoney", 
                PuPubVO.getUFDouble_NullAsZero(m_estimate2VOs[i].getNtotalmoney()).sub(PuPubVO.getUFDouble_NullAsZero(m_estimate2VOs[i].getNmoney())));
          }
        }
        debugTime.addExecutePhase("����˰�ʺͼ�˰�ϼ�");
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
        debugTime.addExecutePhase("���ݿ�Ƭ�������ݣ�getBillCardPanel().getBillData().setBodyValueVO()");
        //
        getBillCardPanel().getBillModel().execLoadFormula();
        debugTime.addExecutePhase("���ݿ�Ƭִ�й�ʽ��getBillCardPanel().getBillModel().execLoadFormula()");
        
        getBillCardPanel().updateValue();
        getBillCardPanel().updateUI();
        changeButtonState();
        boolean bSetFlag = m_sZG.toUpperCase().equals("N");
        getBillCardPanel().setEnabled(bSetFlag);
        debugTime.addExecutePhase("��������");
        
        if (bSetFlag) setPartEditable();
        debugTime.addExecutePhase("setPartEditable()");
        // ���������û���
        setBodyDigits();
        debugTime.addExecutePhase("setBodyDigits()");
        if (!bSetFlag) {
          // �Ѿ��ݹ�,���¼���
          getBillCardPanel().getBillModel().execFormulas(new String[] {"norgtaxmoney->noriginaltaxpricemny-noriginalcurmny"});
          for(int i=0;i<getBillCardPanel().getRowCount();++i)
            setYFLocalPriceMnyFrmOrg(getBillCardPanel(), m_sUnitCode, i, 
                PuPubVO.getUFDouble_NullAsZero(getBillCardPanel().getBodyValueAt(i,"nexchangeotobrate"))
                , m_measure[1], m_measure[2], "ninnum");
        }

        debugTime.addExecutePhase("getBillCardPanel().getBillModel().execFormulas()");
        //���ʱ��ֲ�
        debugTime.showAllExecutePhase("�ɹ��ݹ���ѯ��ⵥ��ʱ�䣺");
      }
      catch (java.sql.SQLException e) {
        MessageDialog.showErrorDlg(this, NCLangRes.getInstance().getStrByID("40040503", "UPP40040503-000035")/* @res "��ⵥ��ѯ" */,
            NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000412")/* @res "SQL������" */);
        SCMEnv.out(e);
        return;
      }
      catch (ArrayIndexOutOfBoundsException e) {
        MessageDialog.showErrorDlg(this, NCLangRes.getInstance().getStrByID("40040503", "UPP40040503-000035")/* @res "��ⵥ��ѯ" */,
            NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000426")/* @res "����Խ�����" */);
        SCMEnv.out(e);
        return;
      }
      catch (NullPointerException e) {
        MessageDialog.showErrorDlg(this, NCLangRes.getInstance().getStrByID("40040503", "UPP40040503-000035")/* @res "��ⵥ��ѯ" */,
            NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000427")/* @res "��ָ�����" */);
        SCMEnv.out(e);
        return;
      }
      catch (Exception e) {
        MessageDialog.showErrorDlg(this, NCLangRes.getInstance().getStrByID("40040503", "UPP40040503-000035")/* @res "��ⵥ��ѯ" */, e.getMessage());
        SCMEnv.out(e);
        return;
      }
    }
  }


  /**
   * ��������:��ⵥ��ѯ ������ ���أ� ���ߣ��ܺ��� ������2004-3-24 14:41:50 �޸ģ�2004-09-10 ԬҰ
   * <p>
   * �޸ģ�2008-06-17 ���t�� for v55
   * <p>
   * �޸ģ�2088-10-12 zhaoyha for v55
   * <P>
   * ˵����ʹ���µĲ�ѯģ��
   * �������V55�²�ѯģ
   */
  public void onQueryForV55() {

    if (!m_bICStartUp) {
      // ���δ����
      MessageDialog.showErrorDlg(this,
          nc.ui.ml.NCLangRes.getInstance().getStrByID("40040503", "UPP40040503-000035")/*
                                                                                         * @res
                                                                                         * "��ⵥ��ѯ"
                                                                                         */,
          nc.ui.ml.NCLangRes.getInstance().getStrByID("40040503", "UPP40040503-000036")/*
                                                                                         * @res
                                                                                         * "���δ���ã���������ⵥ��"
                                                                                         */);
      return;
    }
    if (getNewQueryDlg().showModal() == QueryConditionDLG.ID_OK) {
      String sqlWherePart = getNewQueryDlg().getQueryWhereSql();

      // ���ð�ť״̬�����ݹ������ݣ�ȫ����ȫ��Ϊ����
      for (int i = 0; i < 5; i++) {
        m_nButtonState[i] = 0;
      }

      m_nButtonState[1] = 1;
      m_nButtonState[2] = 1;
      m_nButtonState[3] = 1;

      m_sZG = getNewQueryDlg().getQueryEstimateFlag();

      // ��ѯ
      try {
        long tTime = System.currentTimeMillis();

        if (m_sZG.toUpperCase().equals("N")) {
          m_estimate1VOs = EstimateHelper.queryEstimate(m_sUnitCode, sqlWherePart, m_sZG);
          if (m_estimate1VOs == null || m_estimate1VOs.length == 0) {
            MessageDialog.showHintDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("40040503",
                "UPP40040503-000035")/* @res "��ⵥ��ѯ" */, nc.ui.ml.NCLangRes.getInstance().getStrByID("40040503",
                "UPP40040503-000037")/* @res "û�з�����������ⵥ��" */);

            // �������
            getBillCardPanel().getBillModel().clearBodyData();
            getBillCardPanel().updateUI();
            // ȫѡΪ��
            m_nButtonState[0] = 1;
            changeButtonState();
            return;
          }
          tTime = System.currentTimeMillis() - tTime;
          SCMEnv.out("��ⵥ��ѯʱ�䣺" + tTime + " ms!");

        }
        else {
          m_estimate2VOs = EstimateHelper.queryEstimate(m_sUnitCode, sqlWherePart, m_sZG);
          if (m_estimate2VOs == null || m_estimate2VOs.length == 0) {
            MessageDialog.showHintDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("40040503",
                "UPP40040503-000035")/* @res "��ⵥ��ѯ" */, nc.ui.ml.NCLangRes.getInstance().getStrByID("40040503",
                "UPP40040503-000037")/* @res "û�з�����������ⵥ��" */);

            // �������
            getBillCardPanel().getBillModel().clearBodyData();
            getBillCardPanel().updateUI();
            // ȫѡΪ��
            m_nButtonState[0] = 1;
            changeButtonState();
            return;
          }
          tTime = System.currentTimeMillis() - tTime;
          SCMEnv.out("��ⵥ��ѯʱ�䣺" + tTime + " ms!");
        }

        // ����������
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

        // ����˰�ʺͼ�˰�ϼ�, ���㺬˰���ۺͼ�˰�ϼ�
        // �����ⵥ����Դ�ڶ���,ȡ�����Ӧ��˰Ŀ˰��,��˰���ΪӦ˰���
        vTemp = new Vector();
        if ("N".equalsIgnoreCase(m_sZG)) {
          for (int i = 0; i < m_estimate1VOs.length; i++) {
            m_estimate1VOs[i].setNdiscountrate(new UFDouble(100));
          }
        }
        else {
          for (int i = 0; i < m_estimate2VOs.length; i++) {
            // �㰲��Ŀ���⣬��ѯ�Ѿ��ݹ���������δ��ʾ˰��
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
        // ���������û���
        setBodyDigits();
        if (!bSetFlag) {
          getBillCardPanel().getBillModel().execFormulas(new String[] {
            "norgtaxmoney->noriginaltaxpricemny-noriginalcurmny"
          });
          // for (int i = 0; i < m_estimate2VOs.length; i++) {// ֧���û��༭˰��
          // // ��ѯ���ݹ�ʱ����˰�� zhf
          // // add for v55
          // if (m_estimate2VOs[i].getIdiscounttaxtype().intValue() == 0) {//
          // Ӧ˰�ں�
          // getBillCardPanel().getBillModel().execFormulas(i, new String[] {
          // "ntaxrate->(1-noriginalnetprice/norgnettaxprice)*100"
          // });
          // }
          // else if (m_estimate2VOs[i].getIdiscounttaxtype().intValue() == 1)
          // {// Ӧ˰���
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
                                                                                           * "��ⵥ��ѯ"
                                                                                           */,
            nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000412")/*
                                                                                             * @res
                                                                                             * "SQL������"
                                                                                             */);
        SCMEnv.out(e);
        return;
      }
      catch (ArrayIndexOutOfBoundsException e) {
        MessageDialog.showErrorDlg(this,
            nc.ui.ml.NCLangRes.getInstance().getStrByID("40040503", "UPP40040503-000035")/*
                                                                                           * @res
                                                                                           * "��ⵥ��ѯ"
                                                                                           */,
            nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000426")/*
                                                                                             * @res
                                                                                             * "����Խ�����"
                                                                                             */);
        SCMEnv.out(e);
        return;
      }
      catch (NullPointerException e) {
        MessageDialog.showErrorDlg(this,
            nc.ui.ml.NCLangRes.getInstance().getStrByID("40040503", "UPP40040503-000035")/*
                                                                                           * @res
                                                                                           * "��ⵥ��ѯ"
                                                                                           */,
            nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000427")/*
                                                                                             * @res
                                                                                             * "��ָ�����"
                                                                                             */);
        SCMEnv.out(e);
        return;
      }
      catch (Exception e) {
        MessageDialog.showErrorDlg(this,
            nc.ui.ml.NCLangRes.getInstance().getStrByID("40040503", "UPP40040503-000035")/*
                                                                                           * @res
                                                                                           * "��ⵥ��ѯ"
                                                                                           */, e
                .getMessage());
        SCMEnv.out(e);
        return;
      }
    }
  }

  // /*
  // * �������ݹ��Ĳɹ���ⵥ�ĺ�˰���ۺͼ�˰�ϼ� czp, 2007-08-15, �ع���VO�������̨Ӧ������
  // */
  // private EstimateVO[] calculateTaxPriceForEstimateVO(EstimateVO VO[]) {
  // int nPricePolicy = PuTool.getPricePriorPolicy(m_sUnitCode);
  // EstimateVO.calculateTaxPriceForEstimateVO(VO, nPricePolicy);
  // return VO;
  // }

  /**
   * ��������:ȫѡ ������ ���أ� ���ߣ��ܺ��� ������2004-3-24 14:41:50 �޸ģ���־ƽ FOR V30
   */
  public void onSelectAll() {
    Timer timeTrace = new Timer();
    timeTrace.start();
    int nRow = getBillCardPanel().getBillModel().getRowCount();
    if (nRow <= 0) {
      showHintMessage(NCLangRes.getInstance().getStrByID("40040101", "UPP40040101-000541")/*@res"������ѡ��"*/);
      return;
    }
    timeTrace.addExecutePhase("getBillCardPanel().getBillModel().getRowCount()="+getBillCardPanel().getBillModel().getRowCount());
    //Ϊ���Ч������,ȥ����ɫ
    //getBillCardPanel().getBillTable().setRowSelectionInterval(0, nRow - 1);

    //�ر��¼�����
    getBillCardPanel().getBillModel().removeRowStateChangeEventListener();
    
  // �رպϼƿ���
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
  // �򿪺ϼƿ���
    getBillCardPanel().getBillModel().setNeedCalculate(bOldNeedCalc);
    
    getBillCardPanel().updateUI();
    timeTrace.addExecutePhase("getBillCardPanel().updateUI()");
    //
    setBtnsStates();
    timeTrace.addExecutePhase("setBtnsStates()");
    //
    timeTrace.showAllExecutePhase("onSelectAll()");
    //���¼�����
    getBillCardPanel().getBillModel().addRowStateChangeEventListener(this);
  }

  /**
   * ��������:ȫ�� ������ ���أ� ���ߣ��ܺ��� ������2004-3-24 14:41:50 �޸ģ���־ƽ FOR V30
   */
  public void onSelectNo() {
      int nRow = getBillCardPanel().getBillModel().getRowCount();
      if (nRow <= 0) {
        showHintMessage(NCLangRes.getInstance().getStrByID("40040101", "UPP40040101-000541")/*@res"������ѡ��"*/);
        return;
      }
    //�ر��¼�����
    getBillCardPanel().getBillModel().removeRowStateChangeEventListener();
    // �رպϼƿ���
    boolean bOldNeedCalc = getBillCardPanel().getBillModel().isNeedCalculate();
    getBillCardPanel().getBillModel().setNeedCalculate(false);
    //
      getBillCardPanel().getBillTable().removeRowSelectionInterval(0, nRow - 1);
      //
      for (int i = 0; i < nRow; i++) {
        getBillCardPanel().getBillModel().setRowState(i, BillModel.UNSTATE);
      }
      getBillCardPanel().getBillModel().reCalcurateAll();
    // �򿪺ϼƿ���
      getBillCardPanel().getBillModel().setNeedCalculate(bOldNeedCalc);
      //
      getBillCardPanel().updateUI();
      //
      setBtnsStates();
      //���¼�����
      getBillCardPanel().getBillModel().addRowStateChangeEventListener(this);     
    }

  /**
   * ��������:ȡ���ݹ� ������ ���أ� ���ߣ��ܺ��� ������2004-3-24 14:41:50 �޸ģ���־ƽ FOR V30
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
                                                                                         * "��ⵥȡ���ݹ�"
                                                                                         */,
          nc.ui.ml.NCLangRes.getInstance().getStrByID("40040503", "UPP40040503-000031")/*
                                                                                         * @res
                                                                                         * "δѡ����ⵥ��"
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

    timerDebug.addExecutePhase("��֯����");

    try {
      EstimateHelper.antiEstimate(VOs, new ClientLink(getClientEnvironment()));
    }
    catch (java.sql.SQLException e) {
      MessageDialog.showErrorDlg(this,
          nc.ui.ml.NCLangRes.getInstance().getStrByID("40040503", "UPP40040503-000038")/*
                                                                                         * @res
                                                                                         * "��ⵥȡ���ݹ�"
                                                                                         */,
          nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000412")/*
                                                                                           * @res
                                                                                           * "SQL������"
                                                                                           */);
      SCMEnv.out(e);
      return;
    }
    catch (ArrayIndexOutOfBoundsException e) {
      MessageDialog.showErrorDlg(this,
          nc.ui.ml.NCLangRes.getInstance().getStrByID("40040503", "UPP40040503-000038")/*
                                                                                         * @res
                                                                                         * "��ⵥȡ���ݹ�"
                                                                                         */,
          nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000426")/*
                                                                                           * @res
                                                                                           * "����Խ�����"
                                                                                           */);
      SCMEnv.out(e);
      return;
    }
    catch (NullPointerException e) {
      MessageDialog.showErrorDlg(this,
          nc.ui.ml.NCLangRes.getInstance().getStrByID("40040503", "UPP40040503-000038")/*
                                                                                         * @res
                                                                                         * "��ⵥȡ���ݹ�"
                                                                                         */,
          nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000427")/*
                                                                                           * @res
                                                                                           * "��ָ�����"
                                                                                           */);
      SCMEnv.out(e);
      return;
    }
    catch (nc.vo.pub.BusinessException e) {
      MessageDialog.showErrorDlg(this,
          nc.ui.ml.NCLangRes.getInstance().getStrByID("40040503", "UPP40040503-000038")/*
                                                                                         * @res
                                                                                         * "��ⵥȡ���ݹ�"
                                                                                         */, e
              .getMessage());
      SCMEnv.out(e);
      return;
    }
    catch (Exception e) {
      MessageDialog.showErrorDlg(this,
          nc.ui.ml.NCLangRes.getInstance().getStrByID("40040503", "UPP40040503-000038")/*
                                                                                         * @res
                                                                                         * "��ⵥȡ���ݹ�"
                                                                                         */, e
              .getMessage());
      SCMEnv.out(e);
      return;
    }
    timerDebug.addExecutePhase("Զ�̵��� antiEstimate(EstimateVO[],String)");

    this
        .showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("40040503", "UPP40040503-000039")/*
                                                                                                       * @res
                                                                                                       * "ȡ���ݹ��ɹ���"
                                                                                                       */);

    // ȡ���ݹ������ⵥ������ʾ�ڽ���
    if (vv == null || vv.size() == 0) {
      // ������ⵥ��ȡ���ݹ����
      getBillCardPanel().getBillData().clearViewData();
      getBillCardPanel().updateUI();
      // ����ѯ������Ϊ��
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

    // ���ð�ť״̬�����ݹ������ݣ�ȫ����ȫ��Ϊ����
    for (int i = 0; i < 5; i++) {
      m_nButtonState[i] = 0;
    }
    m_nButtonState[1] = 1;
    m_nButtonState[2] = 1;
    m_nButtonState[3] = 1;
    m_nButtonState[7] = 1;
    changeButtonState();

    timerDebug.addExecutePhase("ȡ���ݹ�����(������ʾ���ݼ���ť�߼�)");

    timerDebug.showAllExecutePhase("ȡ���ݹ�UIʱ��ֲ���");
  }

  /**
   * ��������:�ݹ�ʱ,����˰����, ��˰���, ��˰����, ��˰�ϼƺ�˰�ʿɱ༭��, ��������������ɱ༭, ������ ���أ� ���ߣ��ܺ���
   * ������2004-3-24 14:41:50 �޸ģ���־ƽ FOR V30
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
    // ����Ϊ������۱����ʲ����޸�
    int rows = getBillCardPanel().getBillModel().getRowCount();
    for (int i = 0; i < rows; i++) {
      Object curTypeId = getBillCardPanel().getBodyValueAt(i, "currencytypeid");
      if (curTypeId == null)
        continue;

      if (curTypeId.toString().equalsIgnoreCase(m_sCurrTypeID))// �жϵ�ǰ�����Ƿ�˾��λ��
        // �����۱����ʱ༭����
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
   * ��������:�����б�ѡ�� ������ ���أ� ���ߣ��ܺ��� ������2002-9-26 14:41:50 �޸ģ���־ƽ FOR V30
   */
  public void valueChanged(javax.swing.event.ListSelectionEvent event) {
    if ((ListSelectionModel) event.getSource() == getBillCardPanel().getBillTable().getSelectionModel()
        || (ListSelectionModel) event.getSource() == getBillCardPanel().getBodyPanel().getRowNOTable()
            .getSelectionModel()) {
      // ���������лָ���������ѡ��
      int nSelected[] = getBillCardPanel().getBillTable().getSelectedRows();
      // ��ñ���ѡ������
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
   * ���ð�ť״̬
   * <p>
   * 
   * @author czp
   * @time 2008-5-15 ����02:29:30
   */
  private void setBtnsStates() {
    //
    int nRow = getBillCardPanel().getBillModel().getRowCount();
    // ���ð�ť״̬��ȫ��Ϊ����
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
    // ѡ����������0��ȫ������������Ϊ��
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
      // �������!!!!!!!!
      getBillCardPanel().stopEditing();
      //
      String sCurrId = (String) getBillCardPanel().getBodyValueAt(e.getRow(), "currencytypeid");
      getBillCardPanel().getBodyItem("nexchangeotobrate").setDecimalDigits(
          m_cardPoPubSetUI2.getBothExchRateDigit(m_sUnitCode, sCurrId)[0]);
    }
    return true;
  }

  public Object[] getRelaSortObjectArray() {
    // TODO �Զ����ɷ������
    if (m_sZG.equals("N"))
      return m_estimate1VOs;
    return m_estimate2VOs;
  }

  public void mouseClicked(MouseEvent e) {
    // TODO �Զ����ɷ������

  }

  public void mouseEntered(MouseEvent e) {
    // TODO �Զ����ɷ������

  }

  public void mouseExited(MouseEvent e) {
    // TODO �Զ����ɷ������

  }

  public void mousePressed(MouseEvent e) {
    // TODO �Զ����ɷ������

  }

  public void mouseReleased(MouseEvent e) {
    if (e.getModifiers() == MouseEvent.MOUSE_RELEASED) {
      setBtnsStates();
    }

  }

  /**
   * zhf Ϊ�����������þ��� �ͱ������ ���� ԭ�ҽ��ҵ�񾫶�
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

    // ��ȡ���ֶ�Ӧҵ�񾫶�
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
   * zhf ���ָ�����ֵĻ��ʾ��ȣ��۱�+�۸���
   */
  private int[] getDigits_ExchangeRate(String sCurrId) {
    if (PuPubVO.getString_TrimZeroLenAsNull(sCurrId) == null)
      return null;
    int[] iaExchRateDigit = null;
    // �õ��۱����۸����ʾ���
    iaExchRateDigit = getPOPubSetUI2().getBothExchRateDigit(m_sUnitCode, sCurrId);
    return iaExchRateDigit;
  }

  /*
   * since v55, ��һ��ѡ�и�ѡ���¼�����
   */
  public void valueChanged(RowStateChangeEvent event) {
    //
    setBtnsStates();
  }

  /**
   * ��������������
   * <p>
   * ��ʼ���²�ѯ�Ի������Ϣ��
   * <p>
   * <b>examples:</b>
   * <p>
   * ʹ��ʾ��
   * <p>
   * <b>����˵��</b>
   * <p>
   * 
   * @author zhaoyha
   * @time 2008-10-5 ����07:57:41
   * @since 5.5
   */
  protected EstimateQueryDlg getNewQueryDlg() {

    if (m_condDlg == null) {

      TemplateInfo tempinfo = new TemplateInfo();
      tempinfo.setPk_Org(m_sUnitCode);
      tempinfo.setCurrentCorpPk(m_sUnitCode);
      tempinfo.setFunNode(getModuleCode());
      tempinfo.setUserid(ClientEnvironment.getInstance().getUser().getPrimaryKey());

      // �����µĲ�ѯ�Ի���
      m_condDlg = new EstimateQueryDlg(this, tempinfo, NCLangRes.getInstance().getStrByID("4004050301",
          "UPP4004050301-000004")/* @res "�ݹ���ⵥ��ѯ" */);
    }
    return m_condDlg;
  }
  
  /**
   * 
   * �����������������ݹ�VO������ǰ��顣
   * <p>
   * <b>examples:</b>
   * <p>
   * ʹ��ʾ��
   * <p>
   * <b>����˵��</b>
   * @param vos ѡ����ݹ�VO����
   * @param rownum ѡ����к����� �� vos��Ӧ
   * @return
   * <p>
   * @author zhaoyha
   * @time 2009-9-17 ����11:14:01
   */
  private boolean checkBeforeSave(EstimateVO[] vos,Integer[] rownum){
    StringBuilder errMsg=new StringBuilder();
    String lineSeparator=System.getProperty("line.separator");
    for (int i = 0; i < vos.length; i++) {
      StringBuilder rowErrMsg=new StringBuilder();
      if(UFDouble.ZERO_DBL.equals(PuPubVO.getUFDouble_NullAsZero(vos[i].getNmoney())))    //�ݹ����
        rowErrMsg.append("\t").append(NCLangRes.getInstance().getStrByID("40040503", "EstimateUI-000000")/*�ݹ����Ϊ��*/);
      if(UFDouble.ZERO_DBL.equals(PuPubVO.getUFDouble_NullAsZero(vos[i].getNfeemny()))    //���ñ���ͷ��ý��
              && !StringUtil.isEmptyWithTrim(vos[i].getCfeeid())
          || !UFDouble.ZERO_DBL.equals(PuPubVO.getUFDouble_NullAsZero(vos[i].getNfeemny()))
              && StringUtil.isEmptyWithTrim(vos[i].getCfeeid())) 
        rowErrMsg.append(lineSeparator).append("\t").append(NCLangRes.getInstance().getStrByID("40040503", "EstimateUI-000001")/*�ݹ����ñ������δ¼��*/);
      if(!UFDouble.ZERO_DBL.equals(PuPubVO.getUFDouble_NullAsZero(vos[i].getNfeemny())) //�ݹ�������������������
          && vos[i].getNfeemny().doubleValue()*vos[i].getNinnum().doubleValue()<0)
        rowErrMsg.append(lineSeparator).append("\t").append(NCLangRes.getInstance().getStrByID("40040503", "EstimateUI-000002")/*�ݹ����������������Ų�һ��*/);
      try {vos[i].validate();}catch (ValidationException e) {                           //�ֶ����ֵ��Сֵ
        rowErrMsg.append(lineSeparator).append("\t").append(e.getMessage()); }
      if(rowErrMsg.length()>0)
        errMsg.append(lineSeparator).append(NCLangRes.getInstance().getStrByID("40040503", "EstimateUI-000003",
                null,new String[]{(PuPubVO.getInteger_NullAs(rownum[i], new Integer(0)).intValue() + 1) + ""})/*��{0}�д������´���*/).append(lineSeparator).append(rowErrMsg);
    }
    if(errMsg.length()>0){
      String showMsg=errMsg.toString().replaceFirst(lineSeparator, "").replaceAll(lineSeparator+lineSeparator,lineSeparator);
      MessageDialog.showErrorDlg(this, NCLangRes.getInstance().getStrByID("40040503", "UPP40040503-000033"/* @res "�ݹ����� */), 
          showMsg);
      return false;
    }
    return true;
  }
  
  
  
  
  /**
   * 2010-10-17 MeiChao �ݹ������з�̯���õķ���.
   * �������л���˵��:	��ѡ������,��Ĭ�ϲ������ж�Ӧ�����ŵ������д����.
   * ֧��ͬʱ�������ŵ���.
   */
  private void onCostDistribute(){
	  /**
	   * ��һ��,��ȡҳ�������ݹ������.�Լ�����������ԭʼ����.
	   */
	  //���������ݹ�����
	  EstimateVO[] allEstimateVOs=(EstimateVO[])getBillCardPanel().getBillModel().getBodyValueVOs(EstimateVO.class.getName());
	  //���»�ȡһ�������ݹ�����(����ԭ֮��)
	  EstimateVO[] allEstimateVOsBackup=(EstimateVO[])getBillCardPanel().getBillModel().getBodyValueVOs(EstimateVO.class.getName());
	  //��ȡ��ѡ����ݹ�����
      Integer[] electedRowNOs = null;
      Vector select = new Vector();
      Vector unselect = new Vector();
      int nRow = getBillCardPanel().getRowCount();//�������
      for (int i = 0; i < nRow; i++) {
    	//��ȡ��ǰ��״̬
        int nStatus = getBillCardPanel().getBillModel().getRowState(i);
        if (nStatus == BillModel.SELECTED)
        	select.addElement(new Integer(i));//�������Ϊѡ��״̬,������к���ѡ�񼯺�.
        else
        	unselect.addElement(new Integer(i));//��֮,������к���δѡ�񼯺�.
      }
      electedRowNOs = new Integer[select.size()];
      select.copyInto(electedRowNOs);//��ѡ���кż������������.
	  //��ѡ�����������Ϊ���ж�
      if(electedRowNOs==null||electedRowNOs.length==0){
    	  MessageDialog.showErrorDlg(this, "����", "�Բ���,�빴ѡҪ����������.");
    	  return;
      }
      /**
       * �ڶ���,��ȡѡ�е���,����ѡ�ж�Ӧ�ĵ���.ѡ��һ�д��Ĭ��ѡ�ж�Ӧ��������.
       * ע��,����Ϊ�������.
       */
      //ѡ�е���PK����
      String[] selectedPKs=null;
      //ʹ�ü���Set,�����PK,���Զ������ظ�.
      Set selectPKSet = new TreeSet();
	  //��ѡ�����б���������pk��Set����.
      for(int i=0;i<electedRowNOs.length;i++){
    	  selectPKSet.add(allEstimateVOs[electedRowNOs[i]].getCgeneralhid());
      }
      selectedPKs=new String[selectPKSet.size()];
      selectedPKs=(String[])selectPKSet.toArray(selectedPKs);//��Set�е�PK�����������.
	  //��֤������
      if(selectedPKs==null||selectedPKs.length==0||selectedPKs[0]==null){//�������ʹ����������һ���ж�,��Ϊ��ʱ��������Ҫ��1�����ݱ�ѡ��.
    	  MessageDialog.showErrorDlg(this, "����", "�޷���ȡѡ���еĵ���PK!������̨�鿴.");
    	  return;
      }
      //**��ʱselectedPKs�ĳ��ȱ�Ϊ��ѡ��ĵ�������,�����ᵽ��һ������<<ѡ�е���>>,ע�����.**
      /**
       * ��ʼ��ȡ����ѡ�е��ݵ�����,�����������,���ý��,����PK��,�����ݲ�ͬ���ڲ�ͬ��Map������
       * ����ЩMap,�ִ����һ��ArrayList��...
       * ����������һ����װ�������ݵĹ���.
       */
      List selectedAllBillData=new ArrayList();
	  for(int i=0;i<selectedPKs.length;i++){
		  Map selectedBillData=new HashMap();//������ϢMap
		  List selectedBodys=new ArrayList();//��������ϢList
		  Double selectedTotalQuantity=0.0;//��ѡ��������������
		  List rowno=new ArrayList();//��ѡ�����������ڵ��к�
		  selectedBillData.put("pk", selectedPKs[i].toString());//������PK���뵥����ϢMap��,���Ա�ʶ��ͬ����.
		  //�����������д����
		  for(int j=0;j<allEstimateVOs.length;j++){
			  //��������PK�뵱ǰselectedPKs[i]��ָPKһ��,˵���ô�����ڵ�ǰ��ĳ<<ѡ�е���>>
			  if(allEstimateVOs[j].getCgeneralhid()==selectedPKs[i].toString()){
				  //���������кŷ���rowno��.
				  rowno.add(j);
				  //����ӵ���������ϢList��
				  selectedBodys.add(allEstimateVOs[j]);
				  //�ۼ�����������
				  selectedTotalQuantity+=allEstimateVOs[j].getNinnum().toDouble();
			  }
		  }
		  //��������,������������ϢList���뵽������ϢMap��,���һ�������ĵ���
		  selectedBillData.put("rowno", rowno);//�����к���
		  selectedBillData.put("body", selectedBodys);//��������
		  selectedBillData.put("number", selectedTotalQuantity);//����������
		  //���õ��ݼ��뵽selectedBillData��
		  selectedAllBillData.add(selectedBillData);
	  }
	  if(selectedAllBillData==null||selectedAllBillData.size()==0){
		  MessageDialog.showErrorDlg(this, "���ش���", "ϵͳ�ڷ�װѡ�е���ʱ����һ������Ԥ������������,����UFIDA����ʦ��ϵ.");
	  }
	  /**
	   * �������ݷ�װ���,��ʼ��ȡ���ݶ�Ӧ������Ϣ.
	   */
	  //������ѡ���ݶ�Ӧ�ķ�����Ϣ.
	  List<String> haveexpense=new ArrayList<String>();//�з��õĵ���
	  List<String> havenotexpense=new ArrayList<String>();//û�з��õĵ���
	  List<String> unknowexpense=new ArrayList<String>();//��ȡ����ʧ�ܵĵ���
	  for(int i=0;i<selectedAllBillData.size();i++){
		  Double selectedtotalamount=0.0;//��ѡ�������ܽ��

		  Map selectedBill=(HashMap)selectedAllBillData.get(i);//��ȡ��ǰ����Map����
		  String cbillid=selectedBill.get("pk").toString();//��ȡ��ǰ���ݶ�Ӧ��ⵥPK
		  StringBuffer sql = new StringBuffer(" dr = 0 ");//������ⵥPKƴsql����where�־�
			Vector v = new Vector();
			ArrayList list = new ArrayList();
			v.addElement(cbillid);
			sql.append(" and cbillid = '" + cbillid + "' ");
			InformationCostVO[] expenseinformation = null;
		 try{
			 //��ѯ��Ӧ��ⵥ�ķ�����Ϣ.
			 expenseinformation = (InformationCostVO[])JJPuScmPubHelper.querySmartVOs(InformationCostVO.class, null, sql.toString());
		 } catch(Exception e){
			 SCMEnv.out(e);
			 showErrorMessage("��ȡ������Ϣʧ��!");
			 unknowexpense.add(cbillid);//������ݿ����ʧ��,�򽫸���ⵥpk����unknowexpense
			 continue;//����ִ�б���,��ֹ��;����,Ӱ����������.
		 }
		 if(expenseinformation == null || expenseinformation.length == 0){
			 SCMEnv.out("����:"+cbillid+"�޷�����Ϣ.");
			 havenotexpense.add(cbillid);//����鲻������ⵥ��Ӧ������Ϣ,�򽫸õ�pk����havenotexpense
		 }else if(expenseinformation!=null&&expenseinformation.length>0){
			 //��ɹ����������Ϣ,��������������ý���ۼ���selectedtotalamount��.
			 for(int k=0;k<expenseinformation.length;k++){
				 selectedtotalamount+=expenseinformation[k].getNoriginalcurmny().toDouble();
			 }
			 haveexpense.add(cbillid);
			 selectedBill.put("expenseamount", selectedtotalamount);//�����ݷ����ܽ�����õ���map��.
		 }
	  }
	  //***������Ϣ��ȡ���,��ʼ��֤������Ϣ****
	  if(haveexpense.size()!=0){
		  /**
		   * ����з�����Ϣ�ĵ�������ѡ�е��������,��ֱ�ӽ��з�̯����,������ʾ��ʾ��Ϣ.
		   * ��ʼ��̯���ò���...
		   */
		  //��ʼ���б�����̯!
		  for(int i=0;i<selectedAllBillData.size();i++){//Ҫע��,�˴���Ȼ������ѡ�е��ݽ��б���..
			  for(int j=0;j<haveexpense.size();j++){//�ڴ�ѭ���н��з�����Ϣ��PKȡ����֮��֤,���,���ʾ���Խ��з�̯.
				  if(((HashMap)selectedAllBillData.get(i)).get("pk").toString().equals(haveexpense.get(j).toString())){
					 List rowno=(List)((HashMap)selectedAllBillData.get(i)).get("rowno");//�к�
					 List selectedBodys=(List)((HashMap)selectedAllBillData.get(i)).get("body");//������
					 Double expenseamount=(Double)((HashMap)selectedAllBillData.get(i)).get("expenseamount");//�����ܶ�
					 Double number=(Double)((HashMap)selectedAllBillData.get(i)).get("number");//��ǰ���ݴ������.
					 if(rowno.size()==selectedBodys.size()){//�к�List��������List�������,�����˵��֮ǰ���ݴ���������.�������Ǵ����..
						 for(int k=0;k<rowno.size();k++){//��ʼ���б�����̯
							 //��ǰ�����к�
							 Integer nowRowNO=Integer.valueOf(rowno.get(k).toString());
							 //��ǰ�����д������
							 Double nownumber=((EstimateVO)selectedBodys.get(k)).getNinnum().toDouble();
							 //��ǰ��Ӧ��̯�ķ��ý��.
							 Double nowexpense=expenseamount*nownumber/number;
							 //���ý��д��ҳ��VO�����ж�Ӧ����еķ��ý������
							 allEstimateVOs[nowRowNO].setNfeemny(new UFDouble(nowexpense));
						 }
					 }
				  }
			  }
		  }
		 //��̯ѭ����������Ժ�,���½���.
		 this.getBillCardPanel().getBillModel().setBodyDataVO(allEstimateVOs);
		 this.getBillCardPanel().getBillModel().execLoadFormula();
		 feeFlag = true;//�Ƿ���÷�̯��־�趨Ϊtrue
		 this.getBillCardPanel().updateUI();
		 if(haveexpense.size()!=0&&haveexpense.size()==selectedAllBillData.size()){
			 //����з��õĵ�������ѡ�񵥾������,����гɹ���ʾ.
		 MessageDialog.showHintDlg(this, "��ʾ", "�ɹ���̯����,�����ݹ���ť�����ݹ�����.");
		 }
		 /**
		  * ���ɹ���̯�����д��������Ϊѡ��״̬
		  */
		 for(int x=0;x<haveexpense.size();x++){//ʹ��haveexpense��������ѭ��,��Ϊֻ���óɹ���̯�Ĵ��
	      String thisICBillPK=haveexpense.get(x).toString();
			 for (int y = 0; y < nRow; y++) {
	    	  if(allEstimateVOs[y].getCgeneralhid()==thisICBillPK){
	    		  //����������е���ⵥPK��ɹ���̯����ⵥPKһ��.
	    	      this.getBillCardPanel().getBillModel().setRowState(y, BillModel.SELECTED);
	    	  }
	    	  }
		 }
		 this.getBillCardPanel().updateUI();//���½���!!!!!!
	  }
	  
	  	/**
	  	 * ��������,��Ҫ�Ƿ�̯ʱ�����������ʾ.
	  	 */
	      if(unknowexpense.size()==selectedAllBillData.size()){
		  //����޷���ȡ����ѡ�������
		  MessageDialog.showErrorDlg(this, "����", "�޷���ȡ��ѡ���ݵķ�����Ϣ,����������������,���뼼��Ա��ϵ.");
		  return;
		  }else if(unknowexpense.size()>0&&unknowexpense.size()<selectedAllBillData.size()){
			  //��������޷���ȡ���õ���,��ô..
			  String unknowexpensePKs="";
			  for(int i=0;i<unknowexpense.size();i++){
				  unknowexpensePKs+=";";
				  unknowexpensePKs+=unknowexpense.get(i).toString();
			  }
			  MessageDialog.showHintDlg(this, "����", "�ɹ����в��ֵ��ݵķ��÷�̯,�����µ����޷���ȡ����:"+unknowexpensePKs);
		  }
		  
		  if(havenotexpense.size()==selectedAllBillData.size()){
			  //����޷��õ�������ѡ�������..��ô..
		 	 MessageDialog.showHintDlg(this, "��ʾ", "��ѡ��ĵ����޷�����Ϣ,������з��÷�̯����!");
		 	 return;
		  }else if(havenotexpense.size()>0&&havenotexpense.size()<selectedAllBillData.size()){
			  //��������޷��õĵ���..��ô...
			  String havenotexpensePKs="";
			  for(int i=0;i<havenotexpense.size();i++){
				  havenotexpensePKs+=";";
				  havenotexpensePKs+=havenotexpense.get(i).toString();
			  }
			  MessageDialog.showHintDlg(this, "����", "�ɹ����в��ֵ��ݵķ��÷�̯,�����µ����޷�����Ϣ:"+havenotexpensePKs);
		  }
	  
	  /**
	   * ����,���ݷ�̯�������!
	   */
	  
//	  EstimateVO[] emvos = (EstimateVO[])getBillCardPanel().getBillData().getBodyValueChangeVOs(EstimateVO.class.getName());
//	  ArrayList rowList = new ArrayList();
//	  for (int h = 0; h < getBillCardPanel().getBillModel().getRowCount(); h++) {
//		if(getBillCardPanel().getBillModel().getRowState(h) == 4){
//			rowList.add(h);
//		}
//	}
//	  if(emvos == null || emvos.length == 0){
//		  showErrorMessage("û��Ҫ���з��÷�̯������!");
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
//		 showErrorMessage("��ȡ������Ϣʧ��!");
//	 }
//	 if(infovos == null || infovos.length == 0){
//		 SCMEnv.out("-------�޷�����Ϣ,�����з��÷�̯--------");
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
//	 feeFlag = true;//��̯���óɹ���,���Ƿ�����ݹ�����趨Ϊtrue.
//	 SCMEnv.out("-------��̯���óɹ�--------");  
  }
}