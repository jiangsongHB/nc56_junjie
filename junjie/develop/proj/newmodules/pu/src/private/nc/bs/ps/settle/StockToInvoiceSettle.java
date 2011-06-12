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
 * 此处插入类型说明。 版本号: 功能描述:入库单生成发票时结算,该方法在BO层应用 作者: 创建日期: 修改人: 修改日期: 修改原因:
 */
public class StockToInvoiceSettle {
  // 客户端参数
  private String m_accountYear = null;

  private UFDate m_currentDate = null;

  private String m_busitypeID = null;

  private String m_operatorID = null;

  private String m_unitCode = null;

  private String m_settlebillCode = null;

  // 缓存
  private IinvoiceVO m_invoiceVOs[] = null;

  private StockVO m_stockVOs[] = null;

  private SettlebillItemVO m_settleVOs[] = null;

  // 结算单表头主键+表体主键
  private Vector m_vKey = null;

  private int m_nNumDigit = 2;

  private int m_nPriceDigit = 2;

  private int m_nMnyDigit = 2;

  // 自动暂估参数PO50
  private UFBoolean m_bIc2PiSettle = new UFBoolean(false);

  // 需要暂估的入库单
  private StockVO m_zgStockVOs[] = null;

  // 需要暂估的入库单对应的暂估VO
  private EstimateVO[] m_zgVOs = null;

  // 暂估应付参数PO51
  private UFBoolean m_bZGYF = new UFBoolean(false);

  // 合理损耗是否进入成本
  private UFBoolean m_bWasteCosting = new UFBoolean(true);

  /**
   * 此处插入方法说明。 功能描述:构造子 输入参数:客户断参数（来自发票模块） 返回值: 异常处理:
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

      // 获得精度
      int n[] = pubBO.getDigitBatch(m_unitCode, new String[] {
          "BD501", "BD505", "BD301"
      });
      if (n != null) {
        m_nNumDigit = n[0];
        m_nPriceDigit = n[1];
        m_nMnyDigit = n[2];
      }

      // 获得参数PO50,PO51
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
   * 此处插入方法说明。 功能描述:入库单生成发票结算 输入参数:发票VO，入库单VO 返回值:成功，返回入库单VO集合及结算单VO，否则，返回空
   * 异常处理:
   * 
   * @修改人 张红芳
   * @修改说明 部分结算自动暂估时 暂估价取结算价 （自动结算取发票上的单价）
   */
  public Vector doBalance(InvoiceVO invoiceVO,
      GeneralHHeaderVO generalHHeaderVOs[], GeneralHItemVO generalHItemVOs[],
      GeneralBb3VO generalBb3VOs[], String strOprType) throws BusinessException {
    // 将标准入库单VO转换为方便结算的StockVO
    initStock(generalHHeaderVOs, generalHItemVOs, generalBb3VOs);

    // 将标准发票VO转换为方便结算的IinvoiceVO
    initInvoice(invoiceVO);

    if (m_stockVOs == null) {
      throw new BusinessException(NCLangResOnserver.getInstance().getStrByID(
          "40040503", "UPP40040503-000097"
      /* @res"根据入库单开票结算-入库单行已经全部结算完毕" */));
    }

    if (m_invoiceVOs == null) {
      throw new BusinessException(NCLangResOnserver.getInstance().getStrByID(
          "40040503", "UPP40040503-000098"
      /* @res"根据入库单开票结算-发票行已经全部结算完毕" */));
    }

    if (m_invoiceVOs.length != m_stockVOs.length) {
      throw new BusinessException(NCLangResOnserver.getInstance().getStrByID(
          "40040503", "UPP40040503-000099"
      /* @res"根据入库单开票结算-入库单行,发票行行数不一致" */));
    }

    // since v55 为发票设置和其结算的入库单行主键
    HashMap<String, String> invStockMap = new HashMap<String, String>();
    for (int i = 0; i < m_invoiceVOs.length; i++) {
      if (!invStockMap.containsKey(m_stockVOs[i].getCgeneralbid())) {
        invStockMap.put(m_stockVOs[i].getCgeneralbid().trim(), m_invoiceVOs[i]
            .getCinvoice_bid());
      }
    }
    // end

    // 参与暂估的入库单行除未暂估,未结算过外,还要求:入库数量>发票数量(入库单部分结算)
    String pk_arrcorp = generalHHeaderVOs[0].getPk_corp();
    String pk_invoicecorp = invoiceVO.getHeadVO().getPk_corp();
    String pk_purcorp = invoiceVO.getHeadVO().getPk_purcorp();

    /*
     * 考虑：A、联通华盛项目没有要求 B、稳定性 C、工期，暂时与V5011处理一致，仍不支持(2007-07-11-czp-注) 如果支持，需要调整：
     * 1)、调整转换VO算法，暂估VO要记录各个公司(采购、收票、收货公司)； 2)、取价算法(将设置价格操作放到转换VO之后，可共用暂估时的算法)；
     * 3)、计算金额时的精度要用收票公司(采购公司)的，而不再是用收货公司的精度
     */
    if (pk_purcorp.equals(pk_invoicecorp) && !pk_purcorp.equals(pk_arrcorp)) {
      // 分收集结模式, 不支持暂估
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
      
      // 暂估取价，取发票价 zhf for v55-------------begain--------------------------     

      InvoiceItemVO[] invItemVos = invoiceVO.getBodyVO();
      //得到发票上来源入库单行对应的第一发票行(有可能对应多行)信息  by zhaoyha at 2009.9
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
            RelationsCalVO.PRICE_PRIOR_TO_TAXPRICE);//因为暂估用于向存货核算传,所以取"无税优先"计算
        localCurId = PubDMO.getLocalCurrId(pk_invoicecorp);
        estDMO = new EstimateDMO();
      }
      catch (Exception e1) {
        PubDMO.throwBusinessException(e1);
      }
      for (int i = 0; i < m_zgVOs.length; i++) {
        //---- by zhaoyha at 2008.10.10  修改原币向本币折算算法 ------------
        estDMO.setYFLocalPriceMnyFrmOrg(m_zgVOs[i], pk_invoicecorp, localCurId,
            dmoCurrArith, m_nPriceDigit, m_nMnyDigit);
        estDMO.setLocalPriceMnyFrmOrg(m_zgVOs[i], pk_invoicecorp, localCurId, 
            dmoCurrArith, m_nPriceDigit, m_nMnyDigit);
        //---- by zhaoyha end ---------------------------------------------
      }

//    -----------------------------end-----------------------------------------------------------

      // 设置暂估标记及暂估日期
      for (int i = 0; i < m_zgStockVOs.length; i++) {
        m_zgStockVOs[i].setBzgflag(new UFBoolean(true));
        m_zgStockVOs[i].setDzgdate(m_currentDate);

        // 设置暂估价
        m_zgStockVOs[i].setNprice(m_zgVOs[i].getNprice());
        m_zgStockVOs[i].setNtaxprice(m_zgVOs[i].getNtaxprice());

        m_zgStockVOs[i].setNmoney(m_zgVOs[i].getNmoney());
        m_zgStockVOs[i].setNtotalmoney(m_zgVOs[i].getNtotalmoney());

      }
      ISettle myService = (ISettle) NCLocator.getInstance().lookup(
          ISettle.class.getName());
      // 暂取暂估VO的收票公司
      if (m_zgVOs != null && m_zgVOs.length > 0) {          //过滤掉来源于同一入库单行的VO by zhaoyha at 2009.9
        Object[] filterVos=filterDupStock(m_zgStockVOs, m_zgVOs);
        myService.estimateForPartSettle((StockVO[])filterVos[0],
                (EstimateVO[])filterVos[1], m_operatorID,
                m_currentDate, m_bZGYF, m_zgVOs[0].getPk_invoicecorp());
        }
    
    }    

     //按龙旗项目调整，取单据上的单价并按单据汇率转换，单价为空时按本币金额/除以数量-----zhf 调整 定义上移
     //BusinessCurrencyRateUtil dmoCurrArith = null;
     //try {
       //dmoCurrArith = new BusinessCurrencyRateUtil(m_unitCode);
     //}
     //catch (Exception e) {
       //PubDMO.throwBusinessException(e);
     //}
    // 是否主辅币核算
    // boolean bBlnLocalFrac = false;

    // 单价精度
    // ISysInitQry myService = (ISysInitQry) nc.bs.framework.common.NCLocator
    // .getInstance().lookup(ISysInitQry.class.getName());
    // int nPriceDecimal = myService.getParaInt(m_unitCode, "BD505");

    // 设置表体
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
      // 合理损耗
      body[i].setNreasonalwastnum(m_invoiceVOs[i].getNreasonwastenum());

      // 结算数量为发票未结算数量-合理损耗数量
      body[i].setNsettlenum(m_invoiceVOs[i].getNnosettlenum());
      if (m_invoiceVOs[i].getNnosettlenum() != null
          && m_invoiceVOs[i].getNreasonwastenum() != null) {
        body[i].setNsettlenum(m_invoiceVOs[i].getNnosettlenum().sub(
            m_invoiceVOs[i].getNreasonwastenum()));
        body[i].setNsettlenum(new UFDouble(PubDMO.getRoundDouble(m_nNumDigit,
            body[i].getNsettlenum().doubleValue())));
      }

      // 如果金额与未结算金额相同，而且单价非空，非零时按单价折算
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

      // 计算结算金额=发票结算金额=发票本币单价*(发票数量)
      UFDouble d = body[i].getNsettlenum();
      if (d != null) {
        if (d.equals(m_invoiceVOs[i].getNnosettlenum()
            .sub(PuPubVO.getUFDouble_NullAsZero(m_invoiceVOs[i].getNreasonwastenum())))) {
          // 如果本次结算数量=发票未结算数量,结算金额=发票未结算金额;否则,结算金额=发票单价*发票数量
          // 应该是本次结算数量=发票未结算数量-合理损耗数量,结算金额=发票未结算金额 by zhaoyha at 2009.7.16
          body[i].setNmoney(m_invoiceVOs[i].getNnosettlemny());
        }
        else if (m_invoiceVOs[i].getNprice() != null) { //这个分支在发票自动结算不会走进来
          body[i].setNmoney(body[i].getNprice().multiply(d, m_nMnyDigit));
        }

        // 合理损耗单价为发票单价,合理损耗金额=合理损耗数量*合理损耗单价
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

      // 计算暂估金额
      if (body[i].getNsettlenum() != null) {
        if (m_stockVOs[i].getNnosettlenum().equals(body[i].getNsettlenum())) {
          // 如果本次结算数量=入库单未结算数量,暂估金额=入库单未结算金额;否则,暂估金额=暂估单价*结算数量
          body[i].setNgaugemny(m_stockVOs[i].getNnosettlemny());
        }
        else if (m_stockVOs[i].getNprice() != null) {
          body[i].setNgaugemny(body[i].getNsettlenum().multiply(
              m_stockVOs[i].getNprice(), m_nMnyDigit));
        }
        body[i].setNgaugemny(new UFDouble(PubDMO.getRoundDouble(m_nMnyDigit,
            body[i].getNgaugemny().doubleValue())));
      }

      // 获得入库单号
      body[i].setVbillcode(m_stockVOs[i].getVbillcode());
      // 获得发票号
      body[i].setVinvoicecode(m_invoiceVOs[i].getVinvoicecode());

      v.addElement(body[i]);
    }

    if (v.size() == 0)
      return null;
    m_settleVOs = new SettlebillItemVO[v.size()];
    v.copyInto(m_settleVOs);

    // 修改入库单和发票数据的累计结算数量和累计结算金额
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
   * 此处插入方法说明。 功能描述:结算完毕，修改入库单和发票 输入参数: 返回值: 异常处理:
   */
  private SettlebillVO doModification(String strOprType) throws BusinessException {
//    for (int i = 0; i < m_invoiceVOs.length; i++) {
//      m_invoiceVOs[i].setNaccumsettlenum(m_invoiceVOs[i].getNinvoicenum());
//      m_invoiceVOs[i].setNaccumsettlemny(m_invoiceVOs[i].getNmoney());
//      double d = m_stockVOs[i].getNaccumsettlenum().doubleValue();
//      d += m_settleVOs[i].getNsettlenum().doubleValue();
//      // 比较入库单的累计结算数量和实收数量
//      if (Math.abs(d) > Math.abs(m_stockVOs[i].getNinnum().doubleValue())) {
//        throw new BusinessException(NCLangResOnserver.getInstance().getStrByID(
//            "40040502", "UPP40040502-000047")
//        /* @res* "累计结算数量大于实收数量!" */);
//      }
//      m_stockVOs[i].setNaccumsettlenum(new UFDouble(d));
//
//      d = m_stockVOs[i].getNaccumsettlemny().doubleValue();
//      // 获得暂估金额
//      if (m_settleVOs[i].getNgaugemny() != null)
//        d += m_settleVOs[i].getNgaugemny().doubleValue();
//      // 设置入库单的累计结算金额
//      m_stockVOs[i].setNaccumsettlemny(new UFDouble(d).add(new UFDouble(0.0),
//          BsPuTool.getCCurrDecimal(null)));
//    }
    
    //处理入库单，结算单，发票的结算信息  by zhaoyha at 2009.9
    handleSettleNumValue();
    // 设置表头
    SettlebillHeaderVO head = new SettlebillHeaderVO();
    head.setPk_corp(m_unitCode);
    head.setCaccountyear(m_accountYear);
    head.setDsettledate(m_currentDate);
    head.setIbillstatus(new Integer(0));
    head.setCbilltype(ScmConst.PO_SettleBill);
    head.setCoperator(m_operatorID);
    head.setTmaketime((new UFDateTime(new Date())).toString());
    // 生成结算单号
    head.setVsettlebillcode(m_settlebillCode);

    SettlebillVO settlebillVO = new SettlebillVO(m_settleVOs.length);
    settlebillVO.setParentVO(head);
    settlebillVO.setChildrenVO(m_settleVOs);

    // 插入结算单，更新入库单，发票
    long tTime = System.currentTimeMillis();
    try {
      SettleDMO dmo = new SettleDMO();
      m_vKey = dmo.doStockToInvoiceBalance(m_vKey, settlebillVO, m_stockVOs,
          m_invoiceVOs, m_unitCode, m_busitypeID, m_operatorID, strOprType);
      tTime = System.currentTimeMillis() - tTime;
      SCMEnv.out("本次结算时间：" + tTime + " ms!");
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
   * 此处插入方法说明。 功能描述:将标准发票VO转换为方便结算的IinvoiceVO 输入参数:发票VO（来自发票模块） 返回值: 异常处理:
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

      // 发票有可能已部分结算
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

      // sinc v502, 单价统一在外边处理
      // //支持精度
      // if(items[i].getNinvoicenum() != null && items[i].getNmoney() !=
      // null && Math.abs(items[i].getNinvoicenum().doubleValue()) > 0){
      // vo.setNprice(items[i].getNmoney().div(items[i].getNinvoicenum(),
      // iPriceDigit));
      // }
      // since v502,
      vo.setNoriginalcurprice(items[i].getNoriginalcurprice());
      vo.setNexchangeotobrate(items[i].getNexchangeotobrate());
      vo.setCcurrencytypeid(items[i].getCcurrencytypeid());

      /* 如果结算完毕，则不再结算 CZP 2004-06-14 ADD */
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
   * 此处插入方法说明。 功能描述:将标准入库单VO转换为方便结算的StockVO 输入参数:入库单VO，入库单子子表VO（来自发票模块） 返回值:
   * 异常处理:
   */
  private void initStock(GeneralHHeaderVO generalHHeaderVOs[],
      GeneralHItemVO generalHItemVOs[], GeneralBb3VO generalBb3VOs[])
      throws BusinessException {
    // 入库单子子表与子表的匹配
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

    // 转换
    Vector v = new Vector();
    vTemp = new Vector();// 待暂估
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

      // 20071019取收发类别
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

      // 暂估单价
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
      // v5支持集中采购调整
      // 查询入库单调整，收货公司与当前登录公司不一致，则返回前增加处理:
      try {
        new SettleDMO().preDealStockVO(m_unitCode,
        		m_stockVOs);
      } catch (Exception e) {
        SCMEnv.out(e);
        m_stockVOs = null;
      }
    }

    if (m_stockVOs != null && m_stockVOs.length > 0) {
      // 转换VO,并设置单价、金额信息
      ISettle myService = (ISettle) nc.bs.framework.common.NCLocator
          .getInstance().lookup(ISettle.class.getName());
      EstimateVO[] estVOs = myService.switchVOForZGYF(m_stockVOs, m_bZGYF);
      if (estVOs == null || estVOs.length != m_stockVOs.length) {
        throw new BusinessException("程序逻辑异常(此信息供程序员参考)");/*-=notranslate=-*/
      }
      //
      Vector<EstimateVO> vEstVos = new Vector<EstimateVO>();
      for (int i = 0; i < m_stockVOs.length; i++) {
        // 如果是暂估过，则不必设置暂估价，since V53
        if (!m_stockVOs[i].getBzgflag().booleanValue()) {
          // 设置暂估单价到StockVO
          m_stockVOs[i].setNprice(estVOs[i].getNprice());
          m_stockVOs[i].setNmoney(estVOs[i].getNmoney());
          m_stockVOs[i].setNtaxprice(estVOs[i].getNtaxprice());
          m_stockVOs[i].setNtotalmoney(estVOs[i].getNtotalmoney());
        }
        // PO51参数为"是", 参与暂估的入库单基本要求: 未暂估, 未结算
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
   * 方法功能描述：得到发票上同一入库单行所有开票数量(考虑了合理损耗)。
   * <p>
   * 调整暂估stockVos by zhaoyha at 2009.9
   * 同一张发票多行来源于一行入库单，发票合计数量==入库数量，应该不算部分结算
   * <p>
   * 使用示例
   * <p>
   * <b>参数说明</b>
   * @param vos
   * @return Map(入库单行ID，发票上所有对应此入库单的开票数量)
   * <p>
   * @author zhaoyha
   * @time 2009-9-4 上午11:01:51
   */
  private Map<String,UFDouble> allStockNumAtInv(IinvoiceVO[] vos){
      Map<String,UFDouble> mapBID2Num=new HashMap<String, UFDouble>();
      for(IinvoiceVO vo:vos){
          if(StringUtil.isEmptyWithTrim(vo.getCupsourcebillrowid())) continue;
          UFDouble num=mapBID2Num.containsKey(vo.getCupsourcebillrowid())?
                  mapBID2Num.get(vo.getCupsourcebillrowid()):UFDouble.ZERO_DBL;
                  //总计数量=sum(发票数量-合理损耗数量)
          num=num.add(vo.getNinvoicenum().sub(PuPubVO.getUFDouble_NullAsZero(vo.getNreasonwastenum())));
          mapBID2Num.put(vo.getCupsourcebillrowid(), num);
      }
      return mapBID2Num;
  }
  
  /**
   * 
   * 方法功能描述：得到来源于入库单行的发票行信息。
   * <p>
   * <b>examples:</b>
   * <p>
   * 使用示例
   * <p>
   * <b>参数说明</b>
   * @param vos
   * @return Map(入库单行ID，发票上币种、折本汇率、原币无税单价、含税单价信息)
   * <p>
   * @author zhaoyha
   * @time 2009-9-7 上午11:03:57
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
   * 方法功能描述：设置入库单，结算单，发票的结算信息。
   * <p>
   * <b>examples:</b>
   * <p>
   * 使用示例
   * <p>
   * <b>参数说明</b>
   * @param vos
   * <p>
   * @author zhaoyha
 * @throws BusinessException 
   * @time 2009-9-4 下午01:40:13
   */
  private void handleSettleNumValue() throws BusinessException{
      //倒挤处理(对结算单VO)
      Map<String,UFDouble[]> settleNumMny=handleLastRemnant(m_invoiceVOs, m_stockVOs, m_settleVOs);
      for (int i = 0; i < m_invoiceVOs.length; i++) {
          m_invoiceVOs[i].setNaccumsettlenum(m_invoiceVOs[i].getNinvoicenum());
          m_invoiceVOs[i].setNaccumsettlemny(m_invoiceVOs[i].getNmoney());
          UFDouble d = PuPubVO.getUFDouble_NullAsZero(m_stockVOs[i].getNaccumsettlenum());
          d=d.add(settleNumMny.get(m_stockVOs[i].getCgeneralbid())[0]);
          // 比较入库单的累计结算数量和实收数量
          if (d.abs().compareTo(m_stockVOs[i].getNinnum().abs())>0) {
              throw new BusinessException(NCLangResOnserver.getInstance().getStrByID(
                      "40040502", "UPP40040502-000047")
              /* @res* "累计结算数量大于实收数量!" */);
          }
          m_stockVOs[i].setNaccumsettlenum(d);
          d=PuPubVO.getUFDouble_NullAsZero(m_stockVOs[i].getNaccumsettlemny());
          d=d.add(settleNumMny.get(m_stockVOs[i].getCgeneralbid())[1],BsPuTool.getCCurrDecimal(null));
          // 设置入库单的累计结算金额
          m_stockVOs[i].setNaccumsettlemny(d);
      }
  }
  
  /**
   * 
   * 方法功能描述：汇总同一入库单行的总计结算信息。
   * <p>
   * <b>examples:</b>
   * <p>
   * 使用示例
   * <p>
   * <b>参数说明</b>
   * @param vos
   * @return Map(入库单行ID，本次总计结算数量、金额)
   * <p>
   * @author zhaoyha
   * @time 2009-9-7 下午01:32:14
   */
  private Map<String,UFDouble[]> sumSettleNumValue(SettlebillItemVO[] vos){
      Map<String,UFDouble[]> mapBID2SettleNumValue=new HashMap<String, UFDouble[]>();
      for(SettlebillItemVO vo:vos){
          UFDouble[] settleNumValue=mapBID2SettleNumValue.containsKey(vo.getCstockrow())?
                  mapBID2SettleNumValue.get(vo.getCstockrow()):new UFDouble[]{UFDouble.ZERO_DBL,UFDouble.ZERO_DBL};
          //累计结算数量
          settleNumValue[0]=settleNumValue[0].add(vo.getNsettlenum());
          //累计结算金额
          settleNumValue[1]=settleNumValue[1].add(PuPubVO.getUFDouble_NullAsZero(vo.getNgaugemny()));
          mapBID2SettleNumValue.put(vo.getCstockrow(), settleNumValue);
      }
      return mapBID2SettleNumValue;
  }
  
  /**
   * 
   * 方法功能描述：得到入库单行的累计未结算数量和金额 - 不包含本次。
   * <p>
   * <b>examples:</b>
   * <p>
   * 使用示例
   * <p>
   * <b>参数说明</b>
   * @param vos Map(入库单行ID，累计结算数量和金额)
   * @return
   * <p>
   * @author zhaoyha
   * @time 2009-9-7 下午02:25:34
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
   * 方法功能描述：对入库单的回冲暂估金额(累计结算金额)进行倒挤。
   * <p>
   * <b>examples:</b>
   * <p>
   * 使用示例
   * <p>
   * <b>参数说明</b>
   * @param invVos
   * @param stockVos
   * @param settleVos
   * @return Map(入库单行ID，本次累计结算数量，金额(含倒挤))
   * <p>
   * @author zhaoyha
   * @time 2009-9-7 下午03:02:20
   */
  private  Map<String,UFDouble[]> handleLastRemnant(IinvoiceVO invVos[],StockVO[] stockVos,SettlebillItemVO[] settleVos){
      //入库单总计开票信息(在本张发票上)
      Map<String,UFDouble> allStockNum=allStockNumAtInv(invVos);
      //入库单累计未结算信息(不包含本次)
      Map<String,UFDouble[]> accSettleValue=stockAccSettleNumValue(stockVos);
      //入库单本次结算信息
      Map<String,UFDouble[]> settleNumMny=sumSettleNumValue(settleVos);
      //得到需要倒挤的入库单行ID
      Set<String> stockID=new HashSet<String>();
      for(StockVO vo:stockVos) if(allStockNum.get(vo.getCgeneralbid()).equals(
              accSettleValue.get(vo.getCgeneralbid())[0]))
          stockID.add(vo.getCgeneralbid());
      //保存已经倒挤处理的入库单行ID
      Set<String> handledStockID=new HashSet<String>();
      //只对最后结算单行倒挤
      for (int i = settleVos.length-1; i >= 0; i--)
          if(stockID.contains(settleVos[i].getCstockrow())
                  && !handledStockID.contains(settleVos[i].getCstockrow())){
              //需要倒挤的金额=(累计未结算金额-本次总计结算金额);本次新的总计结算金额=本次总计结算金额+需要倒挤的金额
              UFDouble d=accSettleValue.get(settleVos[i].getCstockrow())[1];
              d=d.sub(settleNumMny.get(settleVos[i].getCstockrow())[1]);
              settleVos[i].setNgaugemny(PuPubVO.getUFDouble_NullAsZero(settleVos[i].getNgaugemny()).add(d));
              //保存新的结算金额
              settleNumMny.get(settleVos[i].getCstockrow())[1]=settleNumMny.get(settleVos[i].getCstockrow())[1].add(d);
              handledStockID.add(settleVos[i].getCstockrow());
          }
      return settleNumMny;
  }
  
  /**
   * 
   * 方法功能描述：过滤掉来源于同一入入库单的StockVO和暂估VO，避免部分结算自动暂估时出错。
   * <p>
   * <b>examples:</b>
   * <p>
   * 使用示例
   * <p>
   * <b>参数说明</b>
   * @param stockVos
   * @param estVos
   * @return Object[0] 过滤后的StockVO[],过滤后的EstimateVO[]
   * <p>
   * @author zhaoyha
   * @time 2009-9-7 上午11:00:40
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