package nc.bs.ps.settle;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import nc.bs.bd.b21.BusinessCurrencyRateUtil;
import nc.bs.framework.common.NCLocator;
import nc.bs.ml.NCLangResOnserver;
import nc.bs.ps.estimate.EstimateDMO;
import nc.bs.pu.pub.BsPuTool;
import nc.bs.pu.pub.PubDMO;
import nc.bs.pu.pub.PubImpl;
import nc.itf.ps.settle.ISettle;
import nc.itf.uap.busibean.ISysInitQry;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.pi.InvoiceHeaderVO;
import nc.vo.pi.InvoiceItemVO;
import nc.vo.pi.InvoiceVO;
import nc.vo.ps.estimate.EstimateVO;
import nc.vo.ps.estimate.GeneralBb3VO;
import nc.vo.ps.estimate.GeneralHHeaderVO;
import nc.vo.ps.estimate.GeneralHItemVO;
import nc.vo.ps.settle.IinvoiceVO;
import nc.vo.ps.settle.SettlebillHeaderVO;
import nc.vo.ps.settle.SettlebillItemVO;
import nc.vo.ps.settle.SettlebillVO;
import nc.vo.ps.settle.StockVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.constant.ScmConst;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.scm.pu.RelationsCalVO;
import nc.vo.scm.pub.SCMEnv;

/**
 * �˴���������˵���� �汾��: ��������:��ⵥ���ɷ�Ʊʱ����,�÷�����BO��Ӧ�� ����: ��������: �޸���: �޸�����: �޸�ԭ��:
 */
public class StockToInvoiceSettle {
  // �ͻ��˲���
  private String m_accountYear = null;

  private UFDate m_currentDate = null;

  private String m_busitypeID = null;

  private String m_operatorID = null;

  private String m_unitCode = null;

  private String m_settlebillCode = null;

  // ����
  private IinvoiceVO m_invoiceVOs[] = null;

  private StockVO m_stockVOs[] = null;

  private SettlebillItemVO m_settleVOs[] = null;

  // ���㵥��ͷ����+��������
  private Vector m_vKey = null;

  private int m_nNumDigit = 2;

  private int m_nPriceDigit = 2;

  private int m_nMnyDigit = 2;

  // �Զ��ݹ�����PO50
  private UFBoolean m_bIc2PiSettle = new UFBoolean(false);

  // ��Ҫ�ݹ�����ⵥ
  private StockVO m_zgStockVOs[] = null;

  // ��Ҫ�ݹ�����ⵥ��Ӧ���ݹ�VO
  private EstimateVO[] m_zgVOs = null;

  // �ݹ�Ӧ������PO51
  private UFBoolean m_bZGYF = new UFBoolean(false);

  // ��������Ƿ����ɱ�
  private UFBoolean m_bWasteCosting = new UFBoolean(true);

  /**
   * �˴����뷽��˵���� ��������:������ �������:�ͻ��ϲ��������Է�Ʊģ�飩 ����ֵ: �쳣����:
   */
  public StockToInvoiceSettle(
      String accountYear, UFDate currentDate, String busitypeID,
      String operatorID, String unitCode, String vSettlebillcode) {
    super();

    m_accountYear = accountYear;
    m_currentDate = currentDate;
    m_busitypeID = busitypeID;
    m_operatorID = operatorID;
    m_unitCode = unitCode;
    m_settlebillCode = vSettlebillcode;

    try {
      PubImpl pubBO = new PubImpl();

      // ��þ���
      int n[] = pubBO.getDigitBatch(m_unitCode, new String[] {
          "BD501", "BD505", "BD301"
      });
      if (n != null) {
        m_nNumDigit = n[0];
        m_nPriceDigit = n[1];
        m_nMnyDigit = n[2];
      }

      // ��ò���PO50,PO51
      ISysInitQry myService = (ISysInitQry) nc.bs.framework.common.NCLocator
          .getInstance().lookup(ISysInitQry.class.getName());
      Hashtable t = myService.queryBatchParaValues(m_unitCode, new String[] {
          "PO51", "PO52", "PO75"
      });
      if (t != null && t.size() > 0) {
        Object oTemp = null;
        if (t.get("PO51") != null) {
          oTemp = t.get("PO51");
          if (oTemp != null && oTemp.toString().trim().length() > 0)
            m_bIc2PiSettle = new UFBoolean(oTemp.toString());
        }
        if (t.get("PO52") != null) {
          oTemp = t.get("PO52");
          if (oTemp != null && oTemp.toString().trim().length() > 0)
            m_bZGYF = new UFBoolean(oTemp.toString());
        }
        if (t.get("PO75") != null) {
          oTemp = t.get("PO75");
          if (oTemp != null && oTemp.toString().trim().length() > 0)
            m_bWasteCosting = new UFBoolean(oTemp.toString());
        }
      }
    }
    catch (Exception e) {
      SCMEnv.out(e);
    }
  }

  /**
   * �˴����뷽��˵���� ��������:��ⵥ���ɷ�Ʊ���� �������:��ƱVO����ⵥVO ����ֵ:�ɹ���������ⵥVO���ϼ����㵥VO�����򣬷��ؿ�
   * �쳣����:
   * 
   * @�޸��� �ź췼
   * @�޸�˵�� ���ֽ����Զ��ݹ�ʱ �ݹ���ȡ����� ���Զ�����ȡ��Ʊ�ϵĵ��ۣ�
   */
  public Vector doBalance(InvoiceVO invoiceVO,
      GeneralHHeaderVO generalHHeaderVOs[], GeneralHItemVO generalHItemVOs[],
      GeneralBb3VO generalBb3VOs[], String strOprType) throws BusinessException {
    // ����׼��ⵥVOת��Ϊ��������StockVO
    initStock(generalHHeaderVOs, generalHItemVOs, generalBb3VOs);

    // ����׼��ƱVOת��Ϊ��������IinvoiceVO
    initInvoice(invoiceVO);

    if (m_stockVOs == null) {
      throw new BusinessException(NCLangResOnserver.getInstance().getStrByID(
          "40040503", "UPP40040503-000097"
      /* @res"������ⵥ��Ʊ����-��ⵥ���Ѿ�ȫ���������" */));
    }

    if (m_invoiceVOs == null) {
      throw new BusinessException(NCLangResOnserver.getInstance().getStrByID(
          "40040503", "UPP40040503-000098"
      /* @res"������ⵥ��Ʊ����-��Ʊ���Ѿ�ȫ���������" */));
    }

    if (m_invoiceVOs.length != m_stockVOs.length) {
      throw new BusinessException(NCLangResOnserver.getInstance().getStrByID(
          "40040503", "UPP40040503-000099"
      /* @res"������ⵥ��Ʊ����-��ⵥ��,��Ʊ��������һ��" */));
    }

    // since v55 Ϊ��Ʊ���ú���������ⵥ������
    HashMap<String, String> invStockMap = new HashMap<String, String>();
    for (int i = 0; i < m_invoiceVOs.length; i++) {
      if (!invStockMap.containsKey(m_stockVOs[i].getCgeneralbid())) {
        invStockMap.put(m_stockVOs[i].getCgeneralbid().trim(), m_invoiceVOs[i]
            .getCinvoice_bid());
      }
    }
    // end

    // �����ݹ�����ⵥ�г�δ�ݹ�,δ�������,��Ҫ��:�������>��Ʊ����(��ⵥ���ֽ���)
    String pk_arrcorp = generalHHeaderVOs[0].getPk_corp();
    String pk_invoicecorp = invoiceVO.getHeadVO().getPk_corp();
    String pk_purcorp = invoiceVO.getHeadVO().getPk_purcorp();

    /*
     * ���ǣ�A����ͨ��ʢ��Ŀû��Ҫ�� B���ȶ��� C�����ڣ���ʱ��V5011����һ�£��Բ�֧��(2007-07-11-czp-ע) ���֧�֣���Ҫ������
     * 1)������ת��VO�㷨���ݹ�VOҪ��¼������˾(�ɹ�����Ʊ���ջ���˾)�� 2)��ȡ���㷨(�����ü۸�����ŵ�ת��VO֮�󣬿ɹ����ݹ�ʱ���㷨)��
     * 3)��������ʱ�ľ���Ҫ����Ʊ��˾(�ɹ���˾)�ģ������������ջ���˾�ľ���
     */
    if (pk_purcorp.equals(pk_invoicecorp) && !pk_purcorp.equals(pk_arrcorp)) {
      // ���ռ���ģʽ, ��֧���ݹ�
      m_zgStockVOs = null;
      m_zgVOs = null;
    }
    //
    BusinessCurrencyRateUtil dmoCurrArith = null;
    try{
      dmoCurrArith = new BusinessCurrencyRateUtil(pk_invoicecorp);
    }catch(Exception e){
      PubDMO.throwBusinessException(e);
    }
    Map<String,UFDouble> stockInvNumMap=allStockNumAtInv(m_invoiceVOs);
    if (m_zgStockVOs != null && m_zgStockVOs.length > 0) {
      Vector vTemp = new Vector();
      Vector<EstimateVO> vEstVos = new Vector<EstimateVO>();
      for (int i = 0; i < m_zgStockVOs.length; i++) {
        String s1 = m_zgStockVOs[i].getCgeneralbid();
        for (int j = 0; j < m_invoiceVOs.length; j++) {
          String s2 = m_invoiceVOs[j].getCupsourcebillrowid();
          if (s2 == null)
            continue;
          if (s1.equals(s2)
              && Math.abs(m_zgStockVOs[i].getNinnum().doubleValue()) >
                 Math.abs(m_invoiceVOs[j].getNinvoicenum().doubleValue())
                 && !vTemp.contains(m_zgStockVOs[i]) 
                 && !m_zgStockVOs[i].getNinnum().equals(stockInvNumMap.get(s1))) {
            vTemp.addElement(m_zgStockVOs[i]);
            vEstVos.addElement(m_zgVOs[i]);
            break;
          }
        }
      }
      m_zgStockVOs = new StockVO[vTemp.size()];
      vTemp.copyInto(m_zgStockVOs);
      m_zgVOs = new EstimateVO[vTemp.size()];
      vEstVos.copyInto(m_zgVOs);
      
      // �ݹ�ȡ�ۣ�ȡ��Ʊ�� zhf for v55-------------begain--------------------------     

      InvoiceItemVO[] invItemVos = invoiceVO.getBodyVO();
      //�õ���Ʊ����Դ��ⵥ�ж�Ӧ�ĵ�һ��Ʊ��(�п��ܶ�Ӧ����)��Ϣ  by zhaoyha at 2009.9
      Map<String,Object[]> invItemInfo=stockInvoiceInfo(invItemVos);
      for (int i = 0; i < m_zgVOs.length; i++) {
        for (int j = 0; j < invItemVos.length; j++) {
          if (invItemVos[j].getCinvoice_bid().trim().equals(
                  invStockMap.get(m_zgVOs[i].getCgeneralbid()))) {
              Object[] info=invItemInfo.get(m_zgVOs[i].getCgeneralbid());
              m_zgVOs[i].setCurrencytypeid((String) info[0]);
                      //invItemVos[j].getCcurrencytypeid().trim());
              m_zgVOs[i].setNexchangeotobrate((UFDouble) info[1]);//invItemVos[j].getNexchangeotobrate());
              m_zgVOs[i].setNoriginalnetprice((UFDouble) info[2]);//invItemVos[j].getNoriginalcurprice());
              m_zgVOs[i].setNorgnettaxprice((UFDouble) info[3]);//invItemVos[j].getNorgnettaxprice());
          }
        }
      }
      
      EstimateDMO estDMO = null;
      String localCurId = null;

      try {
        EstimateVO.calculateOrgTaxPriceForEstimateVO(m_zgVOs,  
            RelationsCalVO.PRICE_PRIOR_TO_TAXPRICE);//��Ϊ�ݹ������������㴫,����ȡ"��˰����"����
        localCurId = PubDMO.getLocalCurrId(pk_invoicecorp);
        estDMO = new EstimateDMO();
      }
      catch (Exception e1) {
        PubDMO.throwBusinessException(e1);
      }
      for (int i = 0; i < m_zgVOs.length; i++) {
        //---- by zhaoyha at 2008.10.10  �޸�ԭ���򱾱������㷨 ------------
        estDMO.setYFLocalPriceMnyFrmOrg(m_zgVOs[i], pk_invoicecorp, localCurId,
            dmoCurrArith, m_nPriceDigit, m_nMnyDigit);
        estDMO.setLocalPriceMnyFrmOrg(m_zgVOs[i], pk_invoicecorp, localCurId, 
            dmoCurrArith, m_nPriceDigit, m_nMnyDigit);
        //---- by zhaoyha end ---------------------------------------------
      }

//    -----------------------------end-----------------------------------------------------------

      // �����ݹ���Ǽ��ݹ�����
      for (int i = 0; i < m_zgStockVOs.length; i++) {
        m_zgStockVOs[i].setBzgflag(new UFBoolean(true));
        m_zgStockVOs[i].setDzgdate(m_currentDate);

        // �����ݹ���
        m_zgStockVOs[i].setNprice(m_zgVOs[i].getNprice());
        m_zgStockVOs[i].setNtaxprice(m_zgVOs[i].getNtaxprice());

        m_zgStockVOs[i].setNmoney(m_zgVOs[i].getNmoney());
        m_zgStockVOs[i].setNtotalmoney(m_zgVOs[i].getNtotalmoney());

      }
      ISettle myService = (ISettle) NCLocator.getInstance().lookup(
          ISettle.class.getName());
      // ��ȡ�ݹ�VO����Ʊ��˾
      if (m_zgVOs != null && m_zgVOs.length > 0) {          //���˵���Դ��ͬһ��ⵥ�е�VO by zhaoyha at 2009.9
        Object[] filterVos=filterDupStock(m_zgStockVOs, m_zgVOs);
        myService.estimateForPartSettle((StockVO[])filterVos[0],
                (EstimateVO[])filterVos[1], m_operatorID,
                m_currentDate, m_bZGYF, m_zgVOs[0].getPk_invoicecorp());
        }
    
    }    

     //��������Ŀ������ȡ�����ϵĵ��۲������ݻ���ת��������Ϊ��ʱ�����ҽ��/��������-----zhf ���� ��������
     //BusinessCurrencyRateUtil dmoCurrArith = null;
     //try {
       //dmoCurrArith = new BusinessCurrencyRateUtil(m_unitCode);
     //}
     //catch (Exception e) {
       //PubDMO.throwBusinessException(e);
     //}
    // �Ƿ������Һ���
    // boolean bBlnLocalFrac = false;

    // ���۾���
    // ISysInitQry myService = (ISysInitQry) nc.bs.framework.common.NCLocator
    // .getInstance().lookup(ISysInitQry.class.getName());
    // int nPriceDecimal = myService.getParaInt(m_unitCode, "BD505");

    // ���ñ���
    Vector v = new Vector();
    SettlebillItemVO body[] = new SettlebillItemVO[m_stockVOs.length];
    UFDouble ufdPrice = null;
    for (int i = 0; i < body.length; i++) {
      body[i] = new SettlebillItemVO();
      body[i].setPk_corp(m_unitCode);
      body[i].setPk_stockcorp(m_stockVOs[i].getPk_stockcorp());
      body[i].setCinvoice_bid(m_invoiceVOs[i].getCinvoice_bid());
      body[i].setCinvoiceid(m_invoiceVOs[i].getCinvoiceid());
      body[i].setCstockrow(m_stockVOs[i].getCgeneralbid());
      body[i].setCstockid(m_stockVOs[i].getCgeneralhid());
      body[i].setVstockbilltype(m_stockVOs[i].getBillTypeNullAs45());
      body[i].setCmangid(m_invoiceVOs[i].getCmangid());
      body[i].setCbaseid(m_invoiceVOs[i].getCbaseid());
      // �������
      body[i].setNreasonalwastnum(m_invoiceVOs[i].getNreasonwastenum());

      // ��������Ϊ��Ʊδ��������-�����������
      body[i].setNsettlenum(m_invoiceVOs[i].getNnosettlenum());
      if (m_invoiceVOs[i].getNnosettlenum() != null
          && m_invoiceVOs[i].getNreasonwastenum() != null) {
        body[i].setNsettlenum(m_invoiceVOs[i].getNnosettlenum().sub(
            m_invoiceVOs[i].getNreasonwastenum()));
        body[i].setNsettlenum(new UFDouble(PubDMO.getRoundDouble(m_nNumDigit,
            body[i].getNsettlenum().doubleValue())));
      }

      // ��������δ��������ͬ�����ҵ��۷ǿգ�����ʱ����������
      if (m_invoiceVOs[i].getNnosettlemny() != null
          && m_invoiceVOs[i].getNnosettlemny().equals(
              m_invoiceVOs[i].getNmoney())) {
        ufdPrice = PuPubVO.getUFDouble_ZeroAsNull(m_invoiceVOs[i]
            .getNoriginalcurprice());
      }
      else {
        ufdPrice = null;
      }
      ufdPrice = IinvoiceVO.getNpriceArith(m_invoiceVOs[i].getNmoney(),
          m_invoiceVOs[i].getNinvoicenum(), ufdPrice, m_nPriceDigit,
          m_invoiceVOs[i].getNexchangeotobrate(), m_invoiceVOs[i]
              .getCcurrencytypeid(), dmoCurrArith, m_unitCode);
      body[i].setNprice(ufdPrice);

      // ���������=��Ʊ������=��Ʊ���ҵ���*(��Ʊ����)
      UFDouble d = body[i].getNsettlenum();
      if (d != null) {
        if (d.equals(m_invoiceVOs[i].getNnosettlenum()
            .sub(PuPubVO.getUFDouble_NullAsZero(m_invoiceVOs[i].getNreasonwastenum())))) {
          // ������ν�������=��Ʊδ��������,������=��Ʊδ������;����,������=��Ʊ����*��Ʊ����
          // Ӧ���Ǳ��ν�������=��Ʊδ��������-�����������,������=��Ʊδ������ by zhaoyha at 2009.7.16
          body[i].setNmoney(m_invoiceVOs[i].getNnosettlemny());
        }
        else if (m_invoiceVOs[i].getNprice() != null) { //�����֧�ڷ�Ʊ�Զ����㲻���߽���
          body[i].setNmoney(body[i].getNprice().multiply(d, m_nMnyDigit));
        }

        // ������ĵ���Ϊ��Ʊ����,������Ľ��=�����������*������ĵ���
        if (!m_bWasteCosting.booleanValue()) {
          body[i].setNreasonalwastprice(body[i].getNprice());
          body[i].setNreasonalwastmny(PuPubVO.getUFDouble_NullAsZero(
              body[i].getNreasonalwastnum()).multiply(
              PuPubVO.getUFDouble_NullAsZero(body[i].getNreasonalwastprice()),
              BsPuTool.getCCurrDecimal(null)));
          body[i].setNmoney(PuPubVO.getUFDouble_NullAsZero(body[i].getNmoney())
              .sub(body[i].getNreasonalwastmny()));
        }

        body[i].setNmoney(new UFDouble(PubDMO.getRoundDouble(m_nMnyDigit,
            PuPubVO.getUFDouble_NullAsZero(body[i].getNmoney()).doubleValue())));
        if (body[i].getNreasonalwastmny() != null) {
          body[i].setNreasonalwastmny(new UFDouble(PubDMO.getRoundDouble(
              m_nMnyDigit, body[i].getNreasonalwastmny().doubleValue())));
        }
      }

      // �����ݹ����
      if (body[i].getNsettlenum() != null) {
        if (m_stockVOs[i].getNnosettlenum().equals(body[i].getNsettlenum())) {
          // ������ν�������=��ⵥδ��������,�ݹ����=��ⵥδ������;����,�ݹ����=�ݹ�����*��������
          body[i].setNgaugemny(m_stockVOs[i].getNnosettlemny());
        }
        else if (m_stockVOs[i].getNprice() != null) {
          body[i].setNgaugemny(body[i].getNsettlenum().multiply(
              m_stockVOs[i].getNprice(), m_nMnyDigit));
        }
        body[i].setNgaugemny(new UFDouble(PubDMO.getRoundDouble(m_nMnyDigit,
            body[i].getNgaugemny().doubleValue())));
      }

      // �����ⵥ��
      body[i].setVbillcode(m_stockVOs[i].getVbillcode());
      // ��÷�Ʊ��
      body[i].setVinvoicecode(m_invoiceVOs[i].getVinvoicecode());

      v.addElement(body[i]);
    }

    if (v.size() == 0)
      return null;
    m_settleVOs = new SettlebillItemVO[v.size()];
    v.copyInto(m_settleVOs);

    // �޸���ⵥ�ͷ�Ʊ���ݵ��ۼƽ����������ۼƽ�����
    SettlebillVO settlebillVO = null;
    try {
      settlebillVO = doModification(strOprType);
    }
    catch (Exception e) {
      PubDMO.throwBusinessException(e);
    }
    if (settlebillVO != null) {
      Vector vReturn = new Vector();
      vReturn.addElement(m_stockVOs);
      vReturn.addElement(settlebillVO);
      return vReturn;
    }
    else
      return null;
  }

  /**
   * �˴����뷽��˵���� ��������:������ϣ��޸���ⵥ�ͷ�Ʊ �������: ����ֵ: �쳣����:
   */
  private SettlebillVO doModification(String strOprType) throws BusinessException {
//    for (int i = 0; i < m_invoiceVOs.length; i++) {
//      m_invoiceVOs[i].setNaccumsettlenum(m_invoiceVOs[i].getNinvoicenum());
//      m_invoiceVOs[i].setNaccumsettlemny(m_invoiceVOs[i].getNmoney());
//      double d = m_stockVOs[i].getNaccumsettlenum().doubleValue();
//      d += m_settleVOs[i].getNsettlenum().doubleValue();
//      // �Ƚ���ⵥ���ۼƽ���������ʵ������
//      if (Math.abs(d) > Math.abs(m_stockVOs[i].getNinnum().doubleValue())) {
//        throw new BusinessException(NCLangResOnserver.getInstance().getStrByID(
//            "40040502", "UPP40040502-000047")
//        /* @res* "�ۼƽ�����������ʵ������!" */);
//      }
//      m_stockVOs[i].setNaccumsettlenum(new UFDouble(d));
//
//      d = m_stockVOs[i].getNaccumsettlemny().doubleValue();
//      // ����ݹ����
//      if (m_settleVOs[i].getNgaugemny() != null)
//        d += m_settleVOs[i].getNgaugemny().doubleValue();
//      // ������ⵥ���ۼƽ�����
//      m_stockVOs[i].setNaccumsettlemny(new UFDouble(d).add(new UFDouble(0.0),
//          BsPuTool.getCCurrDecimal(null)));
//    }
    
    //������ⵥ�����㵥����Ʊ�Ľ�����Ϣ  by zhaoyha at 2009.9
    handleSettleNumValue();
    // ���ñ�ͷ
    SettlebillHeaderVO head = new SettlebillHeaderVO();
    head.setPk_corp(m_unitCode);
    head.setCaccountyear(m_accountYear);
    head.setDsettledate(m_currentDate);
    head.setIbillstatus(new Integer(0));
    head.setCbilltype(ScmConst.PO_SettleBill);
    head.setCoperator(m_operatorID);
    head.setTmaketime((new UFDateTime(new Date())).toString());
    // ���ɽ��㵥��
    head.setVsettlebillcode(m_settlebillCode);

    SettlebillVO settlebillVO = new SettlebillVO(m_settleVOs.length);
    settlebillVO.setParentVO(head);
    settlebillVO.setChildrenVO(m_settleVOs);

    // ������㵥��������ⵥ����Ʊ
    long tTime = System.currentTimeMillis();
    try {
      SettleDMO dmo = new SettleDMO();
      m_vKey = dmo.doStockToInvoiceBalance(m_vKey, settlebillVO, m_stockVOs,
          m_invoiceVOs, m_unitCode, m_busitypeID, m_operatorID, strOprType);
      tTime = System.currentTimeMillis() - tTime;
      SCMEnv.out("���ν���ʱ�䣺" + tTime + " ms!");
    }
    catch (Exception e) {
      PubDMO.throwBusinessException(e);
    }

    if (m_vKey != null && m_vKey.size() > 1) {
      String headKey = (String) m_vKey.elementAt(0);
      for (int i = 1; i < m_vKey.size(); i++) {
        String bodyKey = (String) m_vKey.elementAt(i);
        m_settleVOs[i - 1].setCsettlebill_bid(bodyKey);
        m_settleVOs[i - 1].setCsettlebillid(headKey);
      }
    }
    return settlebillVO;
  }

  /**
   * �˴����뷽��˵���� ��������:����׼��ƱVOת��Ϊ��������IinvoiceVO �������:��ƱVO�����Է�Ʊģ�飩 ����ֵ: �쳣����:
   */
  private void initInvoice(InvoiceVO invoiceVO) throws BusinessException {
    Vector v = new Vector();
    //
    InvoiceHeaderVO header = invoiceVO.getHeadVO();
    InvoiceItemVO items[] = invoiceVO.getBodyVO();
    for (int i = 0; i < items.length; i++) {
      IinvoiceVO vo = new IinvoiceVO();
      vo.setCdeptid(header.getCdeptid());
      vo.setCoperatorid(header.getCoperator());
      vo.setCvendormangid(header.getCvendormangid());
      vo.setCvendorbaseid(header.getCvendorbaseid());
      vo.setPk_corp(header.getPk_corp());
      vo.setVinvoicecode(header.getVinvoicecode());
      vo.setCinvoice_bid(items[i].getCinvoice_bid());
      vo.setCinvoiceid(items[i].getCinvoiceid());
      vo.setCmangid(items[i].getCmangid());
      vo.setCbaseid(items[i].getCbaseid());

      vo.setCupsourcebillid(items[i].getCupsourcebillid());
      vo.setCupsourcebillrowid(items[i].getCupsourcebillrowid());
      vo.setCupsourcebilltype(items[i].getCupsourcebilltype());
      vo.setCsourcebillhid(items[i].getCsourcebillid());
      vo.setCsourcebillbid(items[i].getCsourcebillrowid());
      vo.setCsourcebilltype(items[i].getCsourcebilltype());

      UFDouble d = items[i].getNinvoicenum();
      if (d == null || d.toString().trim().length() == 0)
        d = new UFDouble(0.0);
      vo.setNinvoicenum(d);
      d = items[i].getNmoney();
      if (d == null || d.toString().trim().length() == 0)
        d = new UFDouble(0.0);
      vo.setNmoney(d);

      // ��Ʊ�п����Ѳ��ֽ���
      UFDouble dd = items[i].getNaccumsettnum();
      if (dd == null || dd.toString().trim().length() == 0)
        dd = new UFDouble(0);
      vo.setNaccumsettlenum(dd);
      vo.setNnosettlenum(new UFDouble(vo.getNinvoicenum().doubleValue()
          - vo.getNaccumsettlenum().doubleValue()));
      dd = items[i].getNaccumsettmny();
      if (dd == null || dd.toString().trim().length() == 0)
        dd = new UFDouble(0);
      vo.setNaccumsettlemny(dd);
      // vo.setNnosettlemny(new UFDouble(vo.getNmoney().doubleValue()
      // - vo.getNaccumsettlemny().doubleValue()));
      vo.setNnosettlemny(vo.getNmoney().sub(vo.getNaccumsettlemny(),
          BsPuTool.getCCurrDecimal(null)));
      vo.setNreasonwastenum(items[i].getNreasonwastenum());

      // sinc v502, ����ͳһ����ߴ���
      // //֧�־���
      // if(items[i].getNinvoicenum() != null && items[i].getNmoney() !=
      // null && Math.abs(items[i].getNinvoicenum().doubleValue()) > 0){
      // vo.setNprice(items[i].getNmoney().div(items[i].getNinvoicenum(),
      // iPriceDigit));
      // }
      // since v502,
      vo.setNoriginalcurprice(items[i].getNoriginalcurprice());
      vo.setNexchangeotobrate(items[i].getNexchangeotobrate());
      vo.setCcurrencytypeid(items[i].getCcurrencytypeid());

      /* ���������ϣ����ٽ��� CZP 2004-06-14 ADD */
      if (Math.abs(vo.getNnosettlemny().doubleValue()) > 0) {
        v.addElement(vo);
      }
    }
    if (v.size() > 0) {
      m_invoiceVOs = new IinvoiceVO[v.size()];
      v.copyInto(m_invoiceVOs);
    }
    else {
      m_invoiceVOs = null;
    }
  }

  /**
   * �˴����뷽��˵���� ��������:����׼��ⵥVOת��Ϊ��������StockVO �������:��ⵥVO����ⵥ���ӱ�VO�����Է�Ʊģ�飩 ����ֵ:
   * �쳣����:
   */
  private void initStock(GeneralHHeaderVO generalHHeaderVOs[],
      GeneralHItemVO generalHItemVOs[], GeneralBb3VO generalBb3VOs[])
      throws BusinessException {
    // ��ⵥ���ӱ����ӱ��ƥ��
    Vector vTemp = new Vector();
    for (int i = 0; i < generalHItemVOs.length; i++) {
      String s1 = generalHItemVOs[i].getCgeneralhid().trim();
      String s2 = generalHItemVOs[i].getCgeneralbid().trim();
      for (int j = 0; j < generalBb3VOs.length; j++) {
        String ss1 = generalBb3VOs[j].getCgeneralhid().trim();
        String ss2 = generalBb3VOs[j].getCgeneralbid().trim();
        if (s1.equals(ss1) && s2.equals(ss2)) {
          vTemp.addElement(generalBb3VOs[j]);
          break;
        }
      }
    }
    GeneralBb3VO items_b[] = new GeneralBb3VO[vTemp.size()];
    vTemp.copyInto(items_b);

    // ת��
    Vector v = new Vector();
    vTemp = new Vector();// ���ݹ�
    for (int i = 0; i < generalHHeaderVOs.length; i++) {
      StockVO vo = new StockVO();
      // since v502
      vo.setCastunitid(generalHItemVOs[i].getCastunitid());
      vo.setHsl(generalHItemVOs[i].getHsl());
      //
      vo.setCbiztype(generalHHeaderVOs[i].getCbiztype());
      vo.setCdeptid(generalHHeaderVOs[i].getCdptid());
      vo.setCgeneralbid(generalHItemVOs[i].getCgeneralbid());
      vo.setCgeneralhid(generalHHeaderVOs[i].getCgeneralhid());
      vo.setCmangid(generalHItemVOs[i].getCinventoryid());
      vo.setCbaseid(generalHItemVOs[i].getCbaseid());
      vo.setCoperatorid(generalHHeaderVOs[i].getCoperatorid());
      vo.setCprovidermangid(generalHHeaderVOs[i].getCproviderid());
      vo.setVbatchcode(generalHItemVOs[i].getVbatchcode());

      vo.setCsourcebillbid(generalHItemVOs[i].getCsourcebillbid());
      vo.setCsourcebillhid(generalHItemVOs[i].getCsourcebillhid());
      vo.setCsourcebilltype(generalHItemVOs[i].getCsourcetype());
      vo.setCfirstbillbid(generalHItemVOs[i].getCfirstbillbid());
      vo.setCfirstbillhid(generalHItemVOs[i].getCfirstbillhid());
      vo.setCfirsttype(generalHItemVOs[i].getCfirsttype());
      vo.setPk_corp(generalHItemVOs[i].getPk_invoicecorp());
      vo.setPk_stockcorp(generalHHeaderVOs[i].getPk_corp());
      vo.setPk_purcorp(generalHHeaderVOs[i].getPk_purcorp());

      vo.setCbilltype(generalHHeaderVOs[i].getCbilltypecode());
      vo.setCwarehouseid(generalHHeaderVOs[i].getCwarehouseid());
      vo.setCstoreorganization(generalHHeaderVOs[i].getCstoreorganization());

      vo.setCbaseid(generalHItemVOs[i].getCbaseid());
      vo.setDbizdate(generalHItemVOs[i].getDbizdate());
      vo.setCbizid(generalHHeaderVOs[i].getCbizid());

      // 20071019ȡ�շ����
      vo.setCdispatcherid(generalHHeaderVOs[i].getCdispatcherid());

      UFDouble d = items_b[i].getNaccountnum1();
      if (d == null || d.toString().length() == 0)
        d = new UFDouble(0.0);
      vo.setNaccumsettlenum(d);
      d = items_b[i].getNaccountmny();
      if (d == null || d.toString().length() == 0)
        d = new UFDouble(0.0);
      vo.setNaccumsettlemny(d);

      d = generalHItemVOs[i].getNinnum();
      if (d == null || d.toString().length() == 0)
        d = new UFDouble(0.0);
      vo.setNinnum(d);
      d = items_b[i].getNpmoney();
      if (d == null || d.toString().length() == 0)
        d = new UFDouble(0.0);
      vo.setNmoney(d);
      //
      vo.setNnosettlenum(new UFDouble(vo.getNinnum().doubleValue()
          - vo.getNaccumsettlenum().doubleValue()));
      vo.setNnosettlemny(new UFDouble(vo.getNmoney().doubleValue()
          - vo.getNaccumsettlemny().doubleValue()));
      vo.setPk_corp(generalHHeaderVOs[i].getPk_corp());
      vo.setVbillcode(generalHHeaderVOs[i].getVbillcode());
      vo.setVsourcebillcode(generalHItemVOs[i].getVsourcebillcode());

      vo.setCprojectid(generalHItemVOs[i].getCprojectid());
      vo.setCprojectphaseid(generalHItemVOs[i].getCprojectphaseid());
      vo.setVfirstbillcode(generalHItemVOs[i].getVfirstbillcode());

      // �ݹ�����
      d = items_b[i].getNpprice();
      if (d == null || d.toString().length() == 0)
        d = new UFDouble(0.0);
      vo.setNprice(d);
      // since v53,
      vo.setNtaxprice(items_b[i].getNzgyfprice());
      vo.setNtotalmoney(items_b[i].getNzgyfmoney());
      //
      vo.setVuserdefh1(generalHHeaderVOs[i].getVuserdef1());
      vo.setVuserdefh2(generalHHeaderVOs[i].getVuserdef2());
      vo.setVuserdefh3(generalHHeaderVOs[i].getVuserdef3());
      vo.setVuserdefh4(generalHHeaderVOs[i].getVuserdef4());
      vo.setVuserdefh5(generalHHeaderVOs[i].getVuserdef5());
      vo.setVuserdefh6(generalHHeaderVOs[i].getVuserdef6());
      vo.setVuserdefh7(generalHHeaderVOs[i].getVuserdef7());
      vo.setVuserdefh8(generalHHeaderVOs[i].getVuserdef8());
      vo.setVuserdefh9(generalHHeaderVOs[i].getVuserdef9());
      vo.setVuserdefh10(generalHHeaderVOs[i].getVuserdef10());

      vo.setVuserdefh11(generalHHeaderVOs[i].getVuserdef11());
      vo.setVuserdefh12(generalHHeaderVOs[i].getVuserdef12());
      vo.setVuserdefh13(generalHHeaderVOs[i].getVuserdef13());
      vo.setVuserdefh14(generalHHeaderVOs[i].getVuserdef14());
      vo.setVuserdefh15(generalHHeaderVOs[i].getVuserdef15());
      vo.setVuserdefh16(generalHHeaderVOs[i].getVuserdef16());
      vo.setVuserdefh17(generalHHeaderVOs[i].getVuserdef17());
      vo.setVuserdefh18(generalHHeaderVOs[i].getVuserdef18());
      vo.setVuserdefh19(generalHHeaderVOs[i].getVuserdef19());
      vo.setVuserdefh20(generalHHeaderVOs[i].getVuserdef20());

      vo.setPk_defdoch1(generalHHeaderVOs[i].getPk_defdoc1());
      vo.setPk_defdoch2(generalHHeaderVOs[i].getPk_defdoc2());
      vo.setPk_defdoch3(generalHHeaderVOs[i].getPk_defdoc3());
      vo.setPk_defdoch4(generalHHeaderVOs[i].getPk_defdoc4());
      vo.setPk_defdoch5(generalHHeaderVOs[i].getPk_defdoc5());
      vo.setPk_defdoch6(generalHHeaderVOs[i].getPk_defdoc6());
      vo.setPk_defdoch7(generalHHeaderVOs[i].getPk_defdoc7());
      vo.setPk_defdoch8(generalHHeaderVOs[i].getPk_defdoc8());
      vo.setPk_defdoch9(generalHHeaderVOs[i].getPk_defdoc9());
      vo.setPk_defdoch10(generalHHeaderVOs[i].getPk_defdoc10());

      vo.setPk_defdoch11(generalHHeaderVOs[i].getPk_defdoc11());
      vo.setPk_defdoch12(generalHHeaderVOs[i].getPk_defdoc12());
      vo.setPk_defdoch13(generalHHeaderVOs[i].getPk_defdoc13());
      vo.setPk_defdoch14(generalHHeaderVOs[i].getPk_defdoc14());
      vo.setPk_defdoch15(generalHHeaderVOs[i].getPk_defdoc15());
      vo.setPk_defdoch16(generalHHeaderVOs[i].getPk_defdoc16());
      vo.setPk_defdoch17(generalHHeaderVOs[i].getPk_defdoc17());
      vo.setPk_defdoch18(generalHHeaderVOs[i].getPk_defdoc18());
      vo.setPk_defdoch19(generalHHeaderVOs[i].getPk_defdoc19());
      vo.setPk_defdoch20(generalHHeaderVOs[i].getPk_defdoc20());

      vo.setVuserdefb1(generalHItemVOs[i].getVuserdef1());
      vo.setVuserdefb2(generalHItemVOs[i].getVuserdef2());
      vo.setVuserdefb3(generalHItemVOs[i].getVuserdef3());
      vo.setVuserdefb4(generalHItemVOs[i].getVuserdef4());
      vo.setVuserdefb5(generalHItemVOs[i].getVuserdef5());
      vo.setVuserdefb6(generalHItemVOs[i].getVuserdef6());
      vo.setVuserdefb7(generalHItemVOs[i].getVuserdef7());
      vo.setVuserdefb8(generalHItemVOs[i].getVuserdef8());
      vo.setVuserdefb9(generalHItemVOs[i].getVuserdef9());
      vo.setVuserdefb10(generalHItemVOs[i].getVuserdef10());

      vo.setVuserdefb11(generalHItemVOs[i].getVuserdef11());
      vo.setVuserdefb12(generalHItemVOs[i].getVuserdef12());
      vo.setVuserdefb13(generalHItemVOs[i].getVuserdef13());
      vo.setVuserdefb14(generalHItemVOs[i].getVuserdef14());
      vo.setVuserdefb15(generalHItemVOs[i].getVuserdef15());
      vo.setVuserdefb16(generalHItemVOs[i].getVuserdef16());
      vo.setVuserdefb17(generalHItemVOs[i].getVuserdef17());
      vo.setVuserdefb18(generalHItemVOs[i].getVuserdef18());
      vo.setVuserdefb19(generalHItemVOs[i].getVuserdef19());
      vo.setVuserdefb20(generalHItemVOs[i].getVuserdef20());

      vo.setPk_defdocb1(generalHItemVOs[i].getPk_defdoc1());
      vo.setPk_defdocb2(generalHItemVOs[i].getPk_defdoc2());
      vo.setPk_defdocb3(generalHItemVOs[i].getPk_defdoc3());
      vo.setPk_defdocb4(generalHItemVOs[i].getPk_defdoc4());
      vo.setPk_defdocb5(generalHItemVOs[i].getPk_defdoc5());
      vo.setPk_defdocb6(generalHItemVOs[i].getPk_defdoc6());
      vo.setPk_defdocb7(generalHItemVOs[i].getPk_defdoc7());
      vo.setPk_defdocb8(generalHItemVOs[i].getPk_defdoc8());
      vo.setPk_defdocb9(generalHItemVOs[i].getPk_defdoc9());
      vo.setPk_defdocb10(generalHItemVOs[i].getPk_defdoc10());

      vo.setPk_defdocb11(generalHItemVOs[i].getPk_defdoc11());
      vo.setPk_defdocb12(generalHItemVOs[i].getPk_defdoc12());
      vo.setPk_defdocb13(generalHItemVOs[i].getPk_defdoc13());
      vo.setPk_defdocb14(generalHItemVOs[i].getPk_defdoc14());
      vo.setPk_defdocb15(generalHItemVOs[i].getPk_defdoc15());
      vo.setPk_defdocb16(generalHItemVOs[i].getPk_defdoc16());
      vo.setPk_defdocb17(generalHItemVOs[i].getPk_defdoc17());
      vo.setPk_defdocb18(generalHItemVOs[i].getPk_defdoc18());
      vo.setPk_defdocb19(generalHItemVOs[i].getPk_defdoc19());
      vo.setPk_defdocb20(generalHItemVOs[i].getPk_defdoc20());

      if (generalHItemVOs[i].getBzgflag() == null)
        vo.setBzgflag(new UFBoolean(false));
      else
        vo.setBzgflag(generalHItemVOs[i].getBzgflag());
      if (generalHItemVOs[i].getBzgyfflag() == null)
        vo.setBzgyfflag(new UFBoolean(false));
      else
        vo.setBzgyfflag(generalHItemVOs[i].getBzgyfflag());

      if (Math.abs(vo.getNnosettlenum().doubleValue()) > 0) {
        v.addElement(vo);
      }
    }

    m_stockVOs = new StockVO[v.size()];
    v.copyInto(m_stockVOs);

    if (m_stockVOs != null && m_stockVOs.length > 0) {
      // v5֧�ּ��вɹ�����
      // ��ѯ��ⵥ�������ջ���˾�뵱ǰ��¼��˾��һ�£��򷵻�ǰ���Ӵ���:
      try {
        new SettleDMO().preDealStockVO(m_unitCode,
        		m_stockVOs);
      } catch (Exception e) {
        SCMEnv.out(e);
        m_stockVOs = null;
      }
    }

    if (m_stockVOs != null && m_stockVOs.length > 0) {
      // ת��VO,�����õ��ۡ������Ϣ
      ISettle myService = (ISettle) nc.bs.framework.common.NCLocator
          .getInstance().lookup(ISettle.class.getName());
      EstimateVO[] estVOs = myService.switchVOForZGYF(m_stockVOs, m_bZGYF);
      if (estVOs == null || estVOs.length != m_stockVOs.length) {
        throw new BusinessException("�����߼��쳣(����Ϣ������Ա�ο�)");/*-=notranslate=-*/
      }
      //
      Vector<EstimateVO> vEstVos = new Vector<EstimateVO>();
      for (int i = 0; i < m_stockVOs.length; i++) {
        // ������ݹ������򲻱������ݹ��ۣ�since V53
        if (!m_stockVOs[i].getBzgflag().booleanValue()) {
          // �����ݹ����۵�StockVO
          m_stockVOs[i].setNprice(estVOs[i].getNprice());
          m_stockVOs[i].setNmoney(estVOs[i].getNmoney());
          m_stockVOs[i].setNtaxprice(estVOs[i].getNtaxprice());
          m_stockVOs[i].setNtotalmoney(estVOs[i].getNtotalmoney());
        }
        // PO51����Ϊ"��", �����ݹ�����ⵥ����Ҫ��: δ�ݹ�, δ����
        if (Math.abs(m_stockVOs[i].getNnosettlenum().doubleValue()) > 0) {
          if (m_bIc2PiSettle.booleanValue()
              && !m_stockVOs[i].getBzgflag().booleanValue()
              && m_stockVOs[i].getNaccumsettlenum().doubleValue() == 0) {
            vTemp.addElement(m_stockVOs[i]);
            vEstVos.addElement(estVOs[i]);
          }
        }
      }
      if (vTemp.size() > 0) {
        m_zgStockVOs = new StockVO[vTemp.size()];
        vTemp.copyInto(m_zgStockVOs);
        m_zgVOs = new EstimateVO[vTemp.size()];
        vEstVos.copyInto(m_zgVOs);
      }
    }
  }
  

  /**
   * 
   * ���������������õ���Ʊ��ͬһ��ⵥ�����п�Ʊ����(�����˺������)��
   * <p>
   * �����ݹ�stockVos by zhaoyha at 2009.9
   * ͬһ�ŷ�Ʊ������Դ��һ����ⵥ����Ʊ�ϼ�����==���������Ӧ�ò��㲿�ֽ���
   * <p>
   * ʹ��ʾ��
   * <p>
   * <b>����˵��</b>
   * @param vos
   * @return Map(��ⵥ��ID����Ʊ�����ж�Ӧ����ⵥ�Ŀ�Ʊ����)
   * <p>
   * @author zhaoyha
   * @time 2009-9-4 ����11:01:51
   */
  private Map<String,UFDouble> allStockNumAtInv(IinvoiceVO[] vos){
      Map<String,UFDouble> mapBID2Num=new HashMap<String, UFDouble>();
      for(IinvoiceVO vo:vos){
          if(StringUtil.isEmptyWithTrim(vo.getCupsourcebillrowid())) continue;
          UFDouble num=mapBID2Num.containsKey(vo.getCupsourcebillrowid())?
                  mapBID2Num.get(vo.getCupsourcebillrowid()):UFDouble.ZERO_DBL;
                  //�ܼ�����=sum(��Ʊ����-�����������)
          num=num.add(vo.getNinvoicenum().sub(PuPubVO.getUFDouble_NullAsZero(vo.getNreasonwastenum())));
          mapBID2Num.put(vo.getCupsourcebillrowid(), num);
      }
      return mapBID2Num;
  }
  
  /**
   * 
   * ���������������õ���Դ����ⵥ�еķ�Ʊ����Ϣ��
   * <p>
   * <b>examples:</b>
   * <p>
   * ʹ��ʾ��
   * <p>
   * <b>����˵��</b>
   * @param vos
   * @return Map(��ⵥ��ID����Ʊ�ϱ��֡��۱����ʡ�ԭ����˰���ۡ���˰������Ϣ)
   * <p>
   * @author zhaoyha
   * @time 2009-9-7 ����11:03:57
   */
  private Map<String,Object[]> stockInvoiceInfo(InvoiceItemVO[] vos){
      Map<String,Object[]> mapBID2Price=new HashMap<String,Object[]>();
      for(InvoiceItemVO vo:vos){
          if(StringUtil.isEmptyWithTrim(vo.getCupsourcebillrowid())) continue;
          if(!mapBID2Price.containsKey(vo.getCupsourcebillrowid()))
              mapBID2Price.put(vo.getCupsourcebillrowid(), 
              new Object[]{vo.getCcurrencytypeid(),vo.getNexchangeotobrate(),
                  vo.getNoriginalcurprice(),vo.getNorgnettaxprice()});
      }
      return mapBID2Price;
  }
  
  
  /**
   * 
   * ��������������������ⵥ�����㵥����Ʊ�Ľ�����Ϣ��
   * <p>
   * <b>examples:</b>
   * <p>
   * ʹ��ʾ��
   * <p>
   * <b>����˵��</b>
   * @param vos
   * <p>
   * @author zhaoyha
 * @throws BusinessException 
   * @time 2009-9-4 ����01:40:13
   */
  private void handleSettleNumValue() throws BusinessException{
      //��������(�Խ��㵥VO)
      Map<String,UFDouble[]> settleNumMny=handleLastRemnant(m_invoiceVOs, m_stockVOs, m_settleVOs);
      for (int i = 0; i < m_invoiceVOs.length; i++) {
          m_invoiceVOs[i].setNaccumsettlenum(m_invoiceVOs[i].getNinvoicenum());
          m_invoiceVOs[i].setNaccumsettlemny(m_invoiceVOs[i].getNmoney());
          UFDouble d = PuPubVO.getUFDouble_NullAsZero(m_stockVOs[i].getNaccumsettlenum());
          d=d.add(settleNumMny.get(m_stockVOs[i].getCgeneralbid())[0]);
          // �Ƚ���ⵥ���ۼƽ���������ʵ������
          if (d.abs().compareTo(m_stockVOs[i].getNinnum().abs())>0) {
              throw new BusinessException(NCLangResOnserver.getInstance().getStrByID(
                      "40040502", "UPP40040502-000047")
              /* @res* "�ۼƽ�����������ʵ������!" */);
          }
          m_stockVOs[i].setNaccumsettlenum(d);
          d=PuPubVO.getUFDouble_NullAsZero(m_stockVOs[i].getNaccumsettlemny());
          d=d.add(settleNumMny.get(m_stockVOs[i].getCgeneralbid())[1],BsPuTool.getCCurrDecimal(null));
          // ������ⵥ���ۼƽ�����
          m_stockVOs[i].setNaccumsettlemny(d);
      }
  }
  
  /**
   * 
   * ������������������ͬһ��ⵥ�е��ܼƽ�����Ϣ��
   * <p>
   * <b>examples:</b>
   * <p>
   * ʹ��ʾ��
   * <p>
   * <b>����˵��</b>
   * @param vos
   * @return Map(��ⵥ��ID�������ܼƽ������������)
   * <p>
   * @author zhaoyha
   * @time 2009-9-7 ����01:32:14
   */
  private Map<String,UFDouble[]> sumSettleNumValue(SettlebillItemVO[] vos){
      Map<String,UFDouble[]> mapBID2SettleNumValue=new HashMap<String, UFDouble[]>();
      for(SettlebillItemVO vo:vos){
          UFDouble[] settleNumValue=mapBID2SettleNumValue.containsKey(vo.getCstockrow())?
                  mapBID2SettleNumValue.get(vo.getCstockrow()):new UFDouble[]{UFDouble.ZERO_DBL,UFDouble.ZERO_DBL};
          //�ۼƽ�������
          settleNumValue[0]=settleNumValue[0].add(vo.getNsettlenum());
          //�ۼƽ�����
          settleNumValue[1]=settleNumValue[1].add(PuPubVO.getUFDouble_NullAsZero(vo.getNgaugemny()));
          mapBID2SettleNumValue.put(vo.getCstockrow(), settleNumValue);
      }
      return mapBID2SettleNumValue;
  }
  
  /**
   * 
   * ���������������õ���ⵥ�е��ۼ�δ���������ͽ�� - ���������Ρ�
   * <p>
   * <b>examples:</b>
   * <p>
   * ʹ��ʾ��
   * <p>
   * <b>����˵��</b>
   * @param vos Map(��ⵥ��ID���ۼƽ��������ͽ��)
   * @return
   * <p>
   * @author zhaoyha
   * @time 2009-9-7 ����02:25:34
   */
  private Map<String,UFDouble[]> stockAccSettleNumValue(StockVO[] vos){
      Map<String,UFDouble[]> mapBID2AccSettleValue=new HashMap<String, UFDouble[]>();
      for(StockVO vo:vos)
          if(!mapBID2AccSettleValue.containsKey(vo.getCgeneralbid()))
              mapBID2AccSettleValue.put(vo.getCgeneralbid(), new UFDouble[]{
                  vo.getNnosettlenum(),vo.getNnosettlemny()});
      return mapBID2AccSettleValue;
  }
  
  /**
   * 
   * ������������������ⵥ�Ļس��ݹ����(�ۼƽ�����)���е�����
   * <p>
   * <b>examples:</b>
   * <p>
   * ʹ��ʾ��
   * <p>
   * <b>����˵��</b>
   * @param invVos
   * @param stockVos
   * @param settleVos
   * @return Map(��ⵥ��ID�������ۼƽ������������(������))
   * <p>
   * @author zhaoyha
   * @time 2009-9-7 ����03:02:20
   */
  private  Map<String,UFDouble[]> handleLastRemnant(IinvoiceVO invVos[],StockVO[] stockVos,SettlebillItemVO[] settleVos){
      //��ⵥ�ܼƿ�Ʊ��Ϣ(�ڱ��ŷ�Ʊ��)
      Map<String,UFDouble> allStockNum=allStockNumAtInv(invVos);
      //��ⵥ�ۼ�δ������Ϣ(����������)
      Map<String,UFDouble[]> accSettleValue=stockAccSettleNumValue(stockVos);
      //��ⵥ���ν�����Ϣ
      Map<String,UFDouble[]> settleNumMny=sumSettleNumValue(settleVos);
      //�õ���Ҫ��������ⵥ��ID
      Set<String> stockID=new HashSet<String>();
      for(StockVO vo:stockVos) if(allStockNum.get(vo.getCgeneralbid()).equals(
              accSettleValue.get(vo.getCgeneralbid())[0]))
          stockID.add(vo.getCgeneralbid());
      //�����Ѿ������������ⵥ��ID
      Set<String> handledStockID=new HashSet<String>();
      //ֻ�������㵥�е���
      for (int i = settleVos.length-1; i >= 0; i--)
          if(stockID.contains(settleVos[i].getCstockrow())
                  && !handledStockID.contains(settleVos[i].getCstockrow())){
              //��Ҫ�����Ľ��=(�ۼ�δ������-�����ܼƽ�����);�����µ��ܼƽ�����=�����ܼƽ�����+��Ҫ�����Ľ��
              UFDouble d=accSettleValue.get(settleVos[i].getCstockrow())[1];
              d=d.sub(settleNumMny.get(settleVos[i].getCstockrow())[1]);
              settleVos[i].setNgaugemny(PuPubVO.getUFDouble_NullAsZero(settleVos[i].getNgaugemny()).add(d));
              //�����µĽ�����
              settleNumMny.get(settleVos[i].getCstockrow())[1]=settleNumMny.get(settleVos[i].getCstockrow())[1].add(d);
              handledStockID.add(settleVos[i].getCstockrow());
          }
      return settleNumMny;
  }
  
  /**
   * 
   * �����������������˵���Դ��ͬһ����ⵥ��StockVO���ݹ�VO�����ⲿ�ֽ����Զ��ݹ�ʱ����
   * <p>
   * <b>examples:</b>
   * <p>
   * ʹ��ʾ��
   * <p>
   * <b>����˵��</b>
   * @param stockVos
   * @param estVos
   * @return Object[0] ���˺��StockVO[],���˺��EstimateVO[]
   * <p>
   * @author zhaoyha
   * @time 2009-9-7 ����11:00:40
   */
  private Object[] filterDupStock(StockVO[] stockVos,EstimateVO[] estVos){
      List<StockVO> newStockVos=new ArrayList<StockVO>();
      List<EstimateVO> newEstVos=new ArrayList<EstimateVO>();
      Set<String> stockRowID=new HashSet<String>();
      for(StockVO vo:stockVos)
          if(!stockRowID.contains(vo.getCgeneralbid())) {
              newStockVos.add(vo);
              stockRowID.add(vo.getCgeneralbid());
          }
      stockRowID.clear();
      for(EstimateVO vo:estVos)
          if(!stockRowID.contains(vo.getCgeneralbid())){
              newEstVos.add(vo);
              stockRowID.add(vo.getCgeneralbid());
          }
      return new Object[]{newStockVos.toArray(new StockVO[newStockVos.size()]),
              newEstVos.toArray(new EstimateVO[newEstVos.size()])};
  }
  
}