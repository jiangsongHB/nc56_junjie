package nc.ui.sourceref.pi;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import nc.bs.bd.b21.CurrencyRateUtil;
import nc.itf.scm.cenpur.service.ChgDataUtil;
import nc.ui.bd.b21.CurrParamQuery;
import nc.ui.pi.InvoiceHelper;
import nc.ui.pi.invoice.StoClassifyBillDlg;
import nc.ui.pi.pub.PiPqPublicUIClass;
import nc.ui.po.pub.PoPublicUIClass;
import nc.ui.pu.pub.POPubSetUI;
import nc.ui.pu.pub.PuTool;
import nc.ui.pu.pub.PubHelper;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.para.SysInitBO_Client;
import nc.ui.pub.query.QueryConditionClient;
import nc.ui.scm.pub.BusiBillManageTool;
import nc.ui.scm.pub.FreeVOParse;
import nc.ui.scm.pub.billutil.ClientCacheHelper;
import nc.ui.scm.pub.cache.CacheTool;
import nc.ui.scm.pub.panel.RelationsCal;
import nc.ui.scm.pub.sourceref.BillRefListPanel;
import nc.ui.scm.pub.sourceref.SimpBillRefListPanel;
import nc.ui.scm.pub.sourceref.SourceRefDlg;
import nc.ui.scm.sourcebill.SourceBillInfoFinder;
import nc.vo.ic.pub.bill.GeneralBillHeaderVO;
import nc.vo.ic.pub.bill.GeneralBillItemVO;
import nc.vo.ic.pub.bill.GeneralBillVO;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.pi.InvoiceHeaderVO;
import nc.vo.pi.InvoiceItemVO;
import nc.vo.pi.InvoiceTempDataVO;
import nc.vo.pi.InvoiceVO;
import nc.vo.pi.NormalCondVO;
import nc.vo.po.OrderItemVO;
import nc.vo.pps.PricParaVO;
import nc.vo.ps.estimate.GeneralBb3VO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.para.SysInitVO;
import nc.vo.pub.query.ConditionVO;
import nc.vo.scm.cenpur.service.ChgDocPkVO;
import nc.vo.scm.pu.BillTypeConst;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.scm.pu.RelationsCalVO;
import nc.vo.scm.pub.SCMEnv;
import nc.vo.scm.pub.log.ScmTimeLog;
import nc.vo.scm.relacal.SCMRelationsCal;

public class SourceRefDlgStoreToPi extends SourceRefDlg {
  String sCorp = null;

  private StoClassifyBillDlg m_StoClassifyBillDlg;

  private SysInitVO m_sysInitVO = null;

  private String m_strDisType;

  private Hashtable m_hCurrTypeID = null;

  private GeneralBillVO[] m_GeneralHVOs;

  private HashMap hWareHouseId = new HashMap();

  private OrderItemVO[] m_OrderItemVOs;

  private Integer[][] m_DistributedRows;

  private Hashtable m_hasUpSourceTS = null;

  private Hashtable m_hasOrderInfo = null;

  private GeneralBb3VO[] bb3VOs;

  private String m_sCurrTypeID = null;

  String sBusinessType = null;

  String sBilltype = null;

  private nc.vo.pi.InvoiceVO[] invoiceVO = null;

  SourceBillInfoFinder finder = new SourceBillInfoFinder();
  
  //为提高效率,缓存起来
  private SysInitVO pareVO = null;
  
  //缓存本位币 by zhaoyha at 2009.6.16
  private String cCurrencyID = PiPqPublicUIClass.getNativeCurrencyID();
  //InvoiceTempDataVO-保存临时数据  VO交换后放在返回数组的最后一个元素中by zhaoyha at 2009.8
  private InvoiceTempDataVO tmpDataVo;
  


  public SourceRefDlgStoreToPi(
      String pkField, String pkCorp, String operator, String funNode, String queryWhere, String billType,
      String businessType, String templateId, String currentBillType, Container parent) {
    super(pkField, pkCorp, operator, funNode, queryWhere, billType, businessType, templateId, currentBillType, parent);
    sCorp = pkCorp;
    sBusinessType = businessType;
    sBilltype = billType;
    try {
      m_sCurrTypeID = SysInitBO_Client.getPkValue(ClientEnvironment.getInstance().getCorporation().getPrimaryKey(),
          "BD301");
      //取发票价格来源
      pareVO = nc.ui.pub.para.SysInitBO_Client.queryByParaCode(getPkCorp(),"PO82");
    }
    catch (Exception e) {
      SCMEnv.out(e);
      return;
    }
  }

  protected BillRefListPanel createDoubleTableListPanel() {
    return new BillRefListPanelStoreToPi(getBusinessType(), getBillType(), getCurrentBillType(), getPkCorp());
  }

  /**
   * 返回 SimpBillRefListPanel 特性值。
   * 
   * @return SimpBillRefListPanel
   */
  /* 警告：此方法将重新生成。 */
  protected SimpBillRefListPanel createOneTableListPanel() {
    return new SimpBillRefListPanelStoreToPi(getBusinessType(), getBillType(), getCurrentBillType(), getPkCorp());
  }

  class SimpBillRefListPanelStoreToPi extends SimpBillRefListPanel {
    public SimpBillRefListPanelStoreToPi(
        String biztype, String sourcetype, String targettype, String pk_corp) {
      super(biztype, sourcetype, targettype, pk_corp);

    }

    private void setMnyDigit(String strCorp) {
      int nDigit = new Integer(POPubSetUI.getMoneyDigitByCurr_Busi(strCorp));
      int nPriceDigit = new POPubSetUI(strCorp).getPriceDecimal();
      int[] iaDigits = PoPublicUIClass.getShowDigits(strCorp);
      getSourceBodyItem("ninnum").setDecimalDigits(iaDigits[0]);
      getSourceBodyItem("nshouldinnum").setDecimalDigits(iaDigits[0]);
      getSourceBodyItem("ninassistnum").setDecimalDigits(iaDigits[1]);
      getSourceBodyItem("nneedinassistnum").setDecimalDigits(iaDigits[1]);
      getSourceBodyItem("nprice").setDecimalDigits(nPriceDigit);
      getSourceBodyItem("nmny").setDecimalDigits(nDigit);
    }

    public void setSourceVOToUI(CircularlyAccessibleValueObject[] srcHeadVOs,
        CircularlyAccessibleValueObject[] srcBodyVOs) throws BusinessException {

      setMnyDigit(sCorp);
      if ("4T".equals(sBilltype)) {
        loadSourceInfoAll(srcBodyVOs);
      }
      ClientCacheHelper.getColValue(srcHeadVOs, new String[] {
    	        "pk_cubasdoc"
    	      }, "bd_cumandoc", "pk_cumandoc", new String[] {
    	        "pk_cubasdoc"
    	      }, "cproviderid");// 根据客商管理档案ID取客商基本档案ID
    	      ClientCacheHelper.getColValue(srcHeadVOs, new String[] {
    	        "cprovidername"
    	      }, "bd_cubasdoc", "pk_cubasdoc", new String[] {
    	        "pk_cubasdoc"
    	      }, "pk_cubasdoc");// 根据客商基本档案ID取客商名称
      super.setSourceVOToUI(srcHeadVOs, srcBodyVOs);
      // 设置换算率
      PuTool.setBillModelConvertRateForConvertBill(getHeadBillModel(), new String[] {
          "cbaseid", "cassistunit", "ninnum", "nassistnum", "nexchangerate"
      });
    }

    private void loadSourceInfoAll(CircularlyAccessibleValueObject[] srcBodyVOs) {
      Vector<String> vecHids = new Vector<String>();
      Vector<String> vecbids = new Vector<String>();
      Vector<String> vecType = new Vector<String>();
      String[] sHids = null;
      String[] sbids = null;
      String[] sType = null;
      for (int i = 0; i < srcBodyVOs.length; i++) {
        vecHids.add(((GeneralBillItemVO) srcBodyVOs[i]).getCsourcebillhid());
        vecbids.add(((GeneralBillItemVO) srcBodyVOs[i]).getCsourcebillbid());
        vecType.add("21");
      }
      if (vecHids.size() > 0) {
        sHids = new String[vecHids.size()];
        vecHids.copyInto(sHids);
      }
      if (vecbids.size() > 0) {
        sbids = new String[vecbids.size()];
        vecbids.copyInto(sbids);
      }

      Hashtable table = finder.getSourceBillInfo1(vecType.toArray(new String[0]), sHids, sbids);
      String silltypename = BillTypeConst.getBillTypeName("21");
      for (int i = 0; i < srcBodyVOs.length; i++) {
        Object[] sourceInfo = (Object[]) table.get(sHids[i] + sbids[i]);
        ((GeneralBillItemVO) srcBodyVOs[i]).setAttributeValue("csourcebillname", silltypename);
        ((GeneralBillItemVO) srcBodyVOs[i]).setAttributeValue("csourcebillcode", sourceInfo[1]);
        ((GeneralBillItemVO) srcBodyVOs[i]).setAttributeValue("csourcebillrowno", sourceInfo[2]);
      }
    }
    public String getRefNodeCode(){
      if("4T".equals(sBilltype))
        //期初(4T)的模板nodecode(应该是pub_systemplate中的nodekey)      
        return "4004050302Z0";
      else if("47".equals(sBilltype) || "45".equals(sBilltype))
        return "REFTEMPLET";
      else
        return null;
    }
    
  }

  class BillRefListPanelStoreToPi extends BillRefListPanel {
    public BillRefListPanelStoreToPi(
        String biztype, String sourcetype, String targettype, String pk_corp) {
      super(biztype, sourcetype, targettype, pk_corp);

    }

    private void setMnyDigit(String strCorp) {
      int nDigit = new Integer(POPubSetUI.getMoneyDigitByCurr_Busi(strCorp));
      int nPriceDigit = new POPubSetUI(strCorp).getPriceDecimal();
      int[] iaDigits = PoPublicUIClass.getShowDigits(strCorp);
      getBodyItem("ninnum").setDecimalDigits(iaDigits[0]);
      getBodyItem("nshouldinnum").setDecimalDigits(iaDigits[0]);
      getBodyItem("ninassistnum").setDecimalDigits(iaDigits[1]);
      getBodyItem("nneedinassistnum").setDecimalDigits(iaDigits[1]);
      getBodyItem("nprice").setDecimalDigits(nPriceDigit);
      getBodyItem("nmny").setDecimalDigits(nDigit);
    }

    public void setSourceVOToUI(CircularlyAccessibleValueObject[] srcHeadVOs,
        CircularlyAccessibleValueObject[] srcBodyVOs) throws BusinessException {

      setMnyDigit(sCorp);
      if ("4T".equals(sBilltype)) {
        loadSourceInfoAll(srcBodyVOs);
      }
      ClientCacheHelper.getColValue(srcHeadVOs, new String[] {
        "pk_cubasdoc"
      }, "bd_cumandoc", "pk_cumandoc", new String[] {
        "pk_cubasdoc"
      }, "cproviderid");// 根据客商管理档案ID取客商基本档案ID
      ClientCacheHelper.getColValue(srcHeadVOs, new String[] {
        "cprovidername"
      }, "bd_cubasdoc", "pk_cubasdoc", new String[] {
        "pk_cubasdoc"
      }, "pk_cubasdoc");// 根据客商基本档案ID取客商名称
      super.setSourceVOToUI(srcHeadVOs, srcBodyVOs);

      // 设置自由项
//      PoPublicUIClass.setFreeColValueForConvertBill(getBodyBillModel(), "vfree");
      // 设置换算率
      PuTool.setBillModelConvertRateForConvertBill(getBodyBillModel(), new String[] {
          "cbaseid", "cassistunit", "ninnum", "nassistnum", "nexchangerate"
      });

    }

    private void loadSourceInfoAll(CircularlyAccessibleValueObject[] srcBodyVOs) {
      Vector<String> vecHids = new Vector<String>();
      Vector<String> vecbids = new Vector<String>();
      Vector<String> vecType = new Vector<String>();
      String[] sHids = null;
      String[] sbids = null;
      String[] sType = null;
      for (int i = 0; i < srcBodyVOs.length; i++) {
        vecHids.add(((GeneralBillItemVO) srcBodyVOs[i]).getCsourcebillhid());
        vecbids.add(((GeneralBillItemVO) srcBodyVOs[i]).getCsourcebillbid());
        vecType.add("21");
      }
      if (vecHids.size() > 0) {
        sHids = new String[vecHids.size()];
        vecHids.copyInto(sHids);
      }
      if (vecbids.size() > 0) {
        sbids = new String[vecbids.size()];
        vecbids.copyInto(sbids);
      }

      Hashtable table = finder.getSourceBillInfo1(vecType.toArray(new String[0]), sHids, sbids);
      String silltypename = BillTypeConst.getBillTypeName("21");
      for (int i = 0; i < srcBodyVOs.length; i++) {
        Object[] sourceInfo = (Object[]) table.get(sHids[i] + sbids[i]);
        ((GeneralBillItemVO) srcBodyVOs[i]).setAttributeValue("csourcebillname", silltypename);
        ((GeneralBillItemVO) srcBodyVOs[i]).setAttributeValue("csourcebillcode", sourceInfo[1]);
        ((GeneralBillItemVO) srcBodyVOs[i]).setAttributeValue("csourcebillrowno", sourceInfo[2]);
      }
    }

    public synchronized void headRowChange(int iNewRow) {
      super.headRowChange(iNewRow);
      // 设置换算率
      PuTool.setBillModelConvertRate(getBodyBillModel(), new String[] {
          "cbaseid", "cassistunit", "ninnum", "nassistnum", "nexchangerate"
      });
      // 设置自由项
      PoPublicUIClass.setFreeColValueForConvertBill(getBodyBillModel(), "vfree");
      // nc.ui.scm.sourcebill.SourceBillTool.loadSourceInfoAll(
      // getBodyBillModel(), sBilltype);
    }
    
    public String getRefNodeCode(){
      if("4T".equals(sBilltype))
        //期初(4T)的模板nodecode(应该是pub_systemplate中的nodekey)      
        return "4004050302Z0";
      else if("47".equals(sBilltype) || "45".equals(sBilltype))
        return "REFTEMPLET";
      else
        return null;
    }
  }

  /**
   * 分单按钮
   * 
   * @see nc.ui.rc.receive.OrdToArrRedunSourceDlg#getPanlCmd()
   */
  protected nc.ui.pub.beans.UIPanel getPanlCmd() {
    if (panlCmd == null) {
      try {
        panlCmd = new nc.ui.pub.beans.UIPanel();
        panlCmd.setName("PanlCmd");
        panlCmd.setPreferredSize(new java.awt.Dimension(0, 40));
        panlCmd.setLayout(new java.awt.FlowLayout());
        panlCmd.add(getbtnOk(), getbtnOk().getName());
        panlCmd.add(getbtnCancel(), getbtnCancel().getName());

        panlCmd.add(getbtnQuery(), getbtnQuery().getName());
        panlCmd.add(getbtnRefQry(), getbtnRefQry().getName());

        panlCmd.add(getbtnSplitMode(), getbtnSplitMode().getName());

        panlCmd.add(getbtnQuery(), getbtnQuery().getName());
        setSplitMethod();
      }
      catch (java.lang.Throwable ex) {
        handleException(ex);
      }
    }
    return panlCmd;
  }

  public void actionPerformed(ActionEvent e) {
    if (e.getSource() == getbtnSplitMode()) {
      onDisType();
    }
    super.actionPerformed(e);
  }

  private void onDisType() {

    getDisTypeDlg().showModal();

    if (getDisTypeDlg().isCloseOK()) {
      setDisType(getDisTypeDlg().getDisType());
      if (m_sysInitVO != null) {
        m_sysInitVO.setValue(getDisType());
        m_sysInitVO.setPk_corpid(ClientEnvironment.getInstance().getCorporation().getPrimaryKey());
        try {
          SysInitBO_Client.saveSysInitVOs(new SysInitVO[] {
            m_sysInitVO
          });
        }
        catch (Exception e) {
          SCMEnv.out(e);
          /** <needn't>不影响流程 */
        }
      }
    }
  }

  private StoClassifyBillDlg getDisTypeDlg() {
    if (m_StoClassifyBillDlg == null) {
      m_StoClassifyBillDlg = new StoClassifyBillDlg(this, getDisType());
    }
    return m_StoClassifyBillDlg;
  }

  public void setDisType(String strNew) {
    m_strDisType = strNew;
  }

  public String getDisType() {
    return m_strDisType;
  }

  public void setSplitMethod() {
    // 分单方式
    String strDis = "供应商";
    try {
      m_sysInitVO = nc.ui.pub.para.SysInitBO_Client.queryByParaCode(ClientEnvironment.getInstance().getCorporation()
          .getPrimaryKey(), "PO19");
      if (m_sysInitVO != null)
        strDis = m_sysInitVO.getValue();
    }
    catch (Exception e) {
      SCMEnv.out(e);
      MessageDialog.showHintDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPPSCMCommon-000270")/*
                                                                                                                     * @res
                                                                                                                     * "提示"
                                                                                                                     */, nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPP40040401-000006")/*
                                                                                         * @res
                                                                                         * "系统故障，取不到入库单转发票的分单方式，默认为按供应商！"
                                                                                         */);
      strDis = "供应商";
    }
    setDisType(strDis);
  }

  /**
   * 功能：分单处理 参数： 返回： 例外： 日期：(2004-3-16 8:59:25)
   */
  public AggregatedValueObject[] getDistributedVOs(AggregatedValueObject[] vos) {
    m_hCurrTypeID = new Hashtable();
    Vector vTemp = new Vector();
    for (int i = 0; i < vos.length; i++) {
      if (((GeneralBillVO) vos[i]).getHeaderVO().getCproviderid() != null
          && !vTemp.contains(((GeneralBillVO) vos[i]).getHeaderVO().getCproviderid()))
        vTemp.addElement(((GeneralBillVO) vos[i]).getHeaderVO().getCproviderid());
    }
    m_GeneralHVOs = (GeneralBillVO[]) vos;
    if (vTemp.size() > 0) {
      String sTemp[] = new String[vTemp.size()];
      vTemp.copyInto(sTemp);
      try {
        Object oTemp = CacheTool.getColumnValue("bd_cumandoc", "pk_cumandoc", "pk_currtype1", sTemp);
        if (oTemp != null) {
          Object o[] = (Object[]) oTemp;
          if (o != null && o.length > 0) {
            for (int i = 0; i < o.length; i++) {
              if (o[i] != null)
                m_hCurrTypeID.put(sTemp[i], o[i]);
            }
          }
        }
      }
      catch (Exception e) {
        SCMEnv.out(e);
        return null;
      }
    }
    cacheWareHouse((GeneralBillVO[]) vos);
    if (vos == null)
      return vos;
    // 按分单方式得到所有发票
    Hashtable stoTable = new Hashtable();
    Hashtable rowsTable = new Hashtable();
    HashMap hFlagMap = new HashMap();
    for (int i = 0; i < vos.length; i++) {
      GeneralBillHeaderVO headVO = (GeneralBillHeaderVO) vos[i].getParentVO();
      GeneralBillItemVO[] itemVOs = (GeneralBillItemVO[]) vos[i].getChildrenVO();
      Object s = headVO.getAttributeValue("pk_purcorp");
      s = itemVOs[0].getAttributeValue("pk_invoicecorp");

      // 获取供应商基础ID
      //String pk_cubasdoc = null;
//      Object oTemp = null;
//      try {
//        oTemp = CacheTool.getCellValue("bd_cumandoc", "pk_cumandoc", "pk_cubasdoc", headVO.getCproviderid());
//      }
//      catch (Exception e) {
//        SCMEnv.out(e);
//        return null;
//      }
//      if (oTemp != null && ((Object[]) oTemp).length >= 1) {
//        if (((Object[]) oTemp)[0] != null && ((Object[]) oTemp)[0].toString().trim().length() > 0)
//          pk_cubasdoc = ((Object[]) oTemp)[0].toString();
//      }

      for (int j = 0; j < itemVOs.length; j++) {
       
        // 获取供应商基础ID
        //应该按来源订单的发票方（或供应商）分单(后台查询入库单时已经做到表体中)
        //by zhaoyha at 2009.10.26(NCdp201040662)
        String pk_cubasdoc = itemVOs[j].getPk_cubasdoc();
//        Object oTemp = null;
//        try {
//          oTemp = CacheTool.getCellValue("bd_cumandoc", "pk_cumandoc", "pk_cubasdoc", itemVOs[j].getCvendorid());
//        }
//        catch (Exception e) {
//          SCMEnv.out("从前台缓存查询供应商基本ID（用于发票分单）：");
//          SCMEnv.out(e);
//        }
//        if (oTemp != null && ((Object[]) oTemp).length >= 1) {
//          if (((Object[]) oTemp)[0] != null && ((Object[]) oTemp)[0].toString().trim().length() > 0)
//            pk_cubasdoc = ((Object[]) oTemp)[0].toString();
//        }
        // 按供应商基础ID分单

        String curKey = ((headVO.getCbiztypeid() == null || headVO.getCbiztypeid().trim().length() == 0) ? "NULL"
            : headVO.getCbiztypeid())
            // + ((headVO.getCproviderid() == null ||
            // headVO.getCproviderid().trim().length() == 0) ? "NULL" :
            // headVO.getCproviderid());
            + ((pk_cubasdoc == null || pk_cubasdoc.trim().length() == 0) ? "NULL" : pk_cubasdoc);

        String deptKey = ((headVO.getCdptid() == null || headVO.getCdptid().trim().length() == 0) ? "NULL" : headVO
            .getCdptid());
        String sStoreOrgId = ((headVO.getPk_calbody() == null || headVO.getPk_calbody().trim().length() == 0) ? "NULL"
            : headVO.getPk_calbody());
        // 根据分单方式得到KEY依据
        if (getDisType().trim().equals("存货+供应商")) { // 供应商+存货
          curKey += itemVOs[j].getCinvbasid()!=null? itemVOs[j].getCinvbasid():itemVOs[j].getCinventoryid();
        }
        else if (getDisType().trim().equals("订单")) { // 订单
          if (getM_OrderItemVOs() != null && getM_OrderItemVOs()[j] != null) {
            curKey += getM_OrderItemVOs()[j].getCorderid();
          }
          else {
            curKey += "NULL";
          }
        }
        else if (getDisType().trim().equals("入库单")) { // 入库单
          curKey += headVO.getPrimaryKey();
        }
        // 加入到HASH表中
        if (!stoTable.containsKey(curKey)) {
          Vector vec = new Vector();
          vec.addElement(headVO);
          vec.addElement(itemVOs[j]);
          stoTable.put(curKey, vec);
          // 行表
          Vector indexVEC = new Vector();
          indexVEC.addElement(new Integer(j));
          rowsTable.put(curKey, indexVEC);

          Vector vFlag = new Vector();
          vFlag.addElement(deptKey);
          vFlag.addElement(sStoreOrgId);

          hFlagMap.put(curKey, vFlag);
        }
        else {
          Vector vec = (Vector) stoTable.get(curKey);
          vec.addElement(itemVOs[j]);
          // 行表
          Vector indexVEC = (Vector) rowsTable.get(curKey);
          indexVEC.addElement(new Integer(j));
          /*
           * 参照入库单单生成发票时，如果选中入库单的部门、业务员、付款协议相同，则携带到发票上，
           * 如果不相同，则发票上的部门、业务员、付款协议为空，由用户手工输入。
           */
          Vector vFlag = (Vector) hFlagMap.get(curKey);
          if (!((String) vFlag.get(0)).equals(deptKey))
            ((GeneralBillHeaderVO) vec.get(0)).setCdptid(null);
          if (!((String) vFlag.get(1)).equals(sStoreOrgId))
            ((GeneralBillHeaderVO) vec.get(0)).setPk_calbody(null);
        }
      }
    }

    if (stoTable.size() == 0) {
      MessageDialog.showHintDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPPSCMCommon-000270")/*
                                                                                                                     * @res
                                                                                                                     * "提示"
                                                                                                                     */, nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPP40040401-000004")/*
                                                                                         * @res
                                                                                         * "请先选择需要生成发票的入库单！"
                                                                                         */);
      return null;
    }
    // 从哈希表中取出所有VO
    GeneralBillVO[] allVOs = null;
    Integer[][] allRows = null;
    if (stoTable.size() > 0) {
      allVOs = new GeneralBillVO[stoTable.size()];
      allRows = new Integer[stoTable.size()][];
      Enumeration elems = stoTable.keys();
      int i = 0;
      while (elems.hasMoreElements()) {
        Object curKey = elems.nextElement();
        Vector vec = (Vector) stoTable.get(curKey);
        int len = vec.size() - 1;

        GeneralBillHeaderVO headVO = (GeneralBillHeaderVO) vec.elementAt(0);
        GeneralBillItemVO[] itemVOs = new GeneralBillItemVO[len];
        vec.removeElementAt(0);
        vec.copyInto(itemVOs);

        allVOs[i] = new GeneralBillVO();
        allVOs[i].setParentVO(headVO);
        allVOs[i].setChildrenVO(itemVOs);

        // 行表
        vec = (Vector) rowsTable.get(curKey);
        allRows[i] = new Integer[vec.size()];
        vec.copyInto(allRows[i]);

        i++;
      }
    }

    setDistributedRows(allRows);

    // //Add by xhq 2004/06/14
    setUpSourceTS();
    setOrderInfo();
    // //Add by xhq 2004/06/14

    return allVOs;
  }

  private void cacheWareHouse(GeneralBillVO[] billVOs) {
    if (billVOs == null || billVOs.length == 0)
      return;
    int size = billVOs.length;
    for (int i = 0; i < size; i++) {
      if (billVOs[i] == null)
        continue;
      GeneralBillHeaderVO hVO = (GeneralBillHeaderVO) billVOs[i].getParentVO();
      String sGeneralhid = hVO.getCgeneralhid();
      String sWareHouseId = hVO.getCwarehouseid();
      if (sGeneralhid != null && sWareHouseId != null)
        hWareHouseId.put(sGeneralhid, sWareHouseId);
    }

  }

  public OrderItemVO[] getM_OrderItemVOs() {
    return m_OrderItemVOs;
  }

  public void setM_OrderItemVOs(OrderItemVO[] orderItemVOs) {
    m_OrderItemVOs = orderItemVOs;
  }

  private void setDistributedRows(java.lang.Integer[][] newDistributedRows) {
    m_DistributedRows = newDistributedRows;
  }

  /**
   * 此处插入方法说明。 创建日期：(2001-9-17 13:54:00)
   * 
   * @return java.lang.Integer[][]
   */
  private java.lang.Integer[][] getDistributedRows() {
    return m_DistributedRows;
  }

  private void setUpSourceTS() {
    if (m_GeneralHVOs == null || m_GeneralHVOs.length == 0) {
      m_hasUpSourceTS = null;
      return;
    }

    m_hasUpSourceTS = new Hashtable();
    for (int i = 0; i < m_GeneralHVOs.length; i++) {
      GeneralBillHeaderVO headVO = (GeneralBillHeaderVO) m_GeneralHVOs[i].getParentVO();
      GeneralBillItemVO bodyVO[] = (GeneralBillItemVO[]) m_GeneralHVOs[i].getChildrenVO();
      if (bodyVO == null || bodyVO.length == 0)
        continue;
      for (int j = 0; j < bodyVO.length; j++)
        m_hasUpSourceTS.put(bodyVO[j].getCgeneralbid(), new String[] {
            headVO.getTs(), bodyVO[j].getTs()
        });
    }
    if (m_hasUpSourceTS.size() == 0)
      m_hasUpSourceTS = null;
  }

  private void setOrderInfo() {
    if (m_OrderItemVOs == null || m_OrderItemVOs.length == 0) {
      m_hasOrderInfo = null;
      return;
    }

    m_hasOrderInfo = new Hashtable();
    for (int i = 0; i < m_GeneralHVOs.length; i++) {
      GeneralBillItemVO bodyVO[] = (GeneralBillItemVO[]) m_GeneralHVOs[i].getChildrenVO();
      if (bodyVO == null || bodyVO.length == 0)
        continue;
      for (int j = 0; j < bodyVO.length; j++) {
        String s1 = bodyVO[j].getCfirstbillbid();
        if (s1 == null || s1.trim().length() == 0)
          continue;
        for (int k = 0; k < m_OrderItemVOs.length; k++) {
          if (m_OrderItemVOs[k] == null)
            continue;
          String s2 = m_OrderItemVOs[k].getCorder_bid();
          String s3 = m_OrderItemVOs[k].getCorderid();
          String s4 = m_OrderItemVOs[k].getCcurrencytypeid();
          if (s1.equals(s2)) {
            m_hasOrderInfo.put(bodyVO[j].getCgeneralbid(), new String[] {
                s3, s2, s4
            });
            break;
          }
        }
      }
    }
    if (m_hasOrderInfo.size() == 0)
      m_hasOrderInfo = null;
  }

  protected void onOk() {
    try {

      retBillVos = getbillListPanel().getSelectedSourceVOs();

    }
    catch (BusinessException e) {
      showErroMessage(e.getMessage());
      return;
    }

    if (retBillVos == null || retBillVos.length <= 0) {
      showErroMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000199")/*
                                                                                                       * @res
                                                                                                       * "请选择单据"
                                                                                                       */);
      return;
    }
    retBillVos = getDistributedVOs(retBillVos);
    ScmTimeLog timer =new ScmTimeLog();
    timer.start("开始从订单VO生成发票VO");
    retBillVos = generateInvoiceVOsNew(retBillVos);
    timer.showTime("完成从订单VO生成发票VO");
	//
	super.setRetsVos(retBillVos);
    //
    this.getAlignmentX();
    this.closeOK();
  }

  public AggregatedValueObject[] generateInvoiceVOsNew(AggregatedValueObject[] vos) {
    //Hashtable hFreeCust = new Hashtable();// key: 订单ID, value: 散户ID
    //Map<String,String> bankInfo = new HashMap<String,String>();

    //是否需要设置采购默认辅单位
    boolean isTransPUAssUnit = false;
    String para = null;
    try{
    	para = nc.ui.pub.para.SysInitBO_Client.getParaString(getPkCorp(),"PO08");
    }catch(BusinessException e){
    	SCMEnv.error(e);
    }
    if (para != null && "是".equals(para.trim())){
    	isTransPUAssUnit = true;
    }
    //效率优化，走VO交换后处理 by zhaoyha at 2009.8
    // /参照入库单生成的发票中的“供应商”取入库单来源订单的“发票方”，“发票方”为空时，取订单上的“供应商”
//    try {
//      for (int i = 0; i < vos.length; i++) {
//        GeneralBillItemVO[] itemVOs = (GeneralBillItemVO[]) vos[i].getChildrenVO();
//        if (itemVOs != null
//            && itemVOs.length > 0
//            && itemVOs[0].getCfirsttype() != null
//            && (itemVOs[0].getCfirsttype().equals(ScmConst.PO_Order) || itemVOs[0].getCfirsttype().equals(
//                ScmConst.SC_Order))) {// 如果入库单来源于采购订单
//          String strOrderHid = itemVOs[0].getCfirstbillhid();// 订单hid
//          if (strOrderHid != null && strOrderHid.trim().length() > 0) {
//            // 查询出订单上的发票方和vendormangid
//            Object[][] oa2Ret = null;
//            if (itemVOs[0].getCfirsttype().equals(ScmConst.PO_Order))
//              oa2Ret = (Object[][]) PubHelper.queryResultsFromAnyTable("po_order", new String[] {
//                  "cgiveinvoicevendor", "cvendormangid", "cfreecustid","caccountbankid"
//              }, " corderid = '" + strOrderHid + "' and dr = 0 ");
//            else
//              oa2Ret = (Object[][]) PubHelper.queryResultsFromAnyTable("sc_order", new String[] {
//                  "cgiveinvoicevendor", "cvendormangid","","caccountbankid"
//              }, " corderid = '" + strOrderHid + "' and dr = 0 ");
//            if (oa2Ret != null && oa2Ret.length > 0) {
//              if (oa2Ret[0] != null) {
//
//                if (itemVOs[0].getCfirsttype().equals(ScmConst.PO_Order) && oa2Ret[0][2] != null)
//                  hFreeCust.put(strOrderHid, oa2Ret[0][2]);
//
//                if (oa2Ret[0][0] != null) {
//                  String strGivInvoice = oa2Ret[0][0].toString().trim();// 订单上的发票方
//                  if (strGivInvoice != null && strGivInvoice.trim().length() > 0) {
//                    ((GeneralBillHeaderVO) vos[i].getParentVO()).setCproviderid(strGivInvoice);
//                  }
//                }
//                else if (oa2Ret[0][1] != null) {// “发票方”为空时，取订单上的“供应商”
//                  String strVendormangid = oa2Ret[0][1].toString().trim();//
//                  if (strVendormangid != null && strVendormangid.trim().length() > 0) {
//                    ((GeneralBillHeaderVO) vos[i].getParentVO()).setCproviderid(strVendormangid);
//                  }
//                }
//                
//                //开户银行 
//                if (oa2Ret[0][3] != null)
//                  bankInfo.put(strOrderHid, (String)oa2Ret[0][3]);
//              }
//            }
//          }
//        }
//      }
//    }
//    catch (Exception e) {
//      SCMEnv.out(e);
//      // MessageDialog.showHintDlg(this,"提示","单据转换失败！");
//      return null;
//    }
    // 进行VO转换
    InvoiceVO[] retInvVOs = new InvoiceVO[vos.length];
    try {
      //效率优化，返回结果最后一个为InvoiceTempDataVO-保存临时数据 by zhaoyha at 2009.8
      AggregatedValueObject[] retVos = BusiBillManageTool.runChangeDataAry(vos, getBillType(), getCurrentBillType());
      for(int i=0;i<retInvVOs.length;++i)
        retInvVOs[i]=(InvoiceVO) retVos[i];
      //设置来源入库单的头TS信息  by zhaoyha
      //PiTools.setUpSourceHTs(retInvVOs, getbillListPanel().getSelectedSourceVOs());
      if(retVos.length>retInvVOs.length)
        setTmpDataVo((InvoiceTempDataVO) retVos[retInvVOs.length]);

      // 做一些后续处理
      if (!processAfterChange((GeneralBillVO[]) vos, retInvVOs)) {
        MessageDialog.showWarningDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401",
            "UPPSCMCommon-000132")/* @res "警告" */, nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401",
            "UPP40040401-000005")/* @res "存在发票数量和来源单据数量正负不相同的发票行，请检查！" */);
        return null;
      }

    }
    catch (Exception e) {
      SCMEnv.out(e);
      MessageDialog.showHintDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPPSCMCommon-000270")/*
                                                                                                                     * @res
                                                                                                                     * "提示"
                                                                                                                     */, e.getMessage());
      return null;
    }

    if (retInvVOs != null && retInvVOs.length > 0) {
      int retInvVOsLength = retInvVOs.length;
      Vector vTemp = new Vector();
      for (int i = 0; i < retInvVOsLength; i++) {
        // 设置自由项
        InvoiceItemVO bodyVO[] = retInvVOs[i].getBodyVO();
        String corderid = null;
        for (int j = 0; j < bodyVO.length; j++) {
          if (bodyVO[j].getCorderid() != null) {
            corderid = bodyVO[j].getCorderid();
            break;
          }
        }
        //效率优化，走VO交换后处理 by zhaoyha at 2009.8
//        if (corderid != null) {
//          Object otemp = hFreeCust.get(corderid);
//          if (otemp != null)
//            retInvVOs[i].getHeadVO().setCfreecustid(otemp.toString());
//          otemp=bankInfo.get(corderid);
//          if (otemp != null)
//            retInvVOs[i].getHeadVO().setCaccountbankid(otemp.toString());
//        }

        for (int j = 0; j < bodyVO.length; j++) {
          if (bodyVO[j].getVfree1() != null || bodyVO[j].getVfree2() != null || bodyVO[j].getVfree3() != null
              || bodyVO[j].getVfree4() != null || bodyVO[j].getVfree5() != null)
            vTemp.addElement(bodyVO[j]);
          //如果是期初入库单到发票，需要设置默认辅单位 modify by donggq 
          //by zhaoyha 2009.3.2根据问题NCdp200735769,45入库单也要转换,不光期初
          if (isTransPUAssUnit){
        	  bodyVO[j].setCbaseid(PuTool.getInvBasID(bodyVO[j].getCmangid()));
        	  if(PuTool.isAssUnitManaged(bodyVO[j].getCbaseid())){
  				String cassistunit = PuTool.getDefaultPUAssUnit(bodyVO[j].getCbaseid());
  				if(PuPubVO.getString_TrimZeroLenAsNull(cassistunit) != null
  						&& PuPubVO.getUFDouble_ZeroAsNull(bodyVO[j].getNinvoicenum()) != null){
  							bodyVO[j].setCassistunit(cassistunit);
	    				UFDouble ufdConv = PuTool.getInvConvRateValue(bodyVO[j].getCbaseid(), cassistunit);
	    				if(ufdConv != null && ufdConv.doubleValue() != 0.0){
	    					bodyVO[j].setNexchangerate(ufdConv);
	    					bodyVO[j].setNassistnum(bodyVO[j].getNinvoicenum().div(ufdConv));
	    				}
  				}
  			}
          }
        }
      }
      if (vTemp.size() > 0) {
        InvoiceItemVO tempbodyVO[] = new InvoiceItemVO[vTemp.size()];
        vTemp.copyInto(tempbodyVO);
        new nc.ui.scm.pub.FreeVOParse().setFreeVO(tempbodyVO, "vfree0", "vfree", null, "cmangid", false);
      }
    }

    // //设置返回值    
    return retInvVOs;
  }

  private boolean processAfterChange(GeneralBillVO[] voaStock, InvoiceVO[] voaInvoice) throws Exception {
    if (voaInvoice == null)
      return true;
    try {
      // 来源于入库单, 从价格结算单取价
      //效率优化，走VO交换后处理 by zhaoyha at 2009.8
      PricParaVO VOs[]=getTmpDataVo().getPriceStl();
//      Vector vTemp = new Vector();
//      for (int i = 0; i < voaInvoice.length; i++) {
//        InvoiceItemVO bodyVO[] = voaInvoice[i].getBodyVO();
//        for (int j = 0; j < bodyVO.length; j++) {
//          PricParaVO tempVO = new PricParaVO();
//          tempVO.setCgeneralbid(bodyVO[j].getCupsourcebillrowid());
//
//          vTemp.addElement(tempVO);
//        }
//      }
//      PricParaVO VOs[] = new PricParaVO[vTemp.size()];
//      vTemp.copyInto(VOs);

      //VOs = PricStlHelper.queryPricStlPrices(VOs);
      
      Hashtable tHQHP = new Hashtable();
      if (VOs != null && VOs.length > 0) {
        for (int i = 0; i < VOs.length; i++) {
          if (VOs[i].getNtaxprice() != null)
            tHQHP.put(VOs[i].getCgeneralbid(), VOs[i].getNtaxprice());
        }
      }

      for (int i = 0; i < voaInvoice.length; i++) {
        // 来源
        voaInvoice[i].setSource(InvoiceVO.FROM_STO);
        // 删除标记
        voaInvoice[i].getHeadVO().setDr(new Integer(0));
        // 发票日期,收票日期,发票类型 期初标志,制单人
        voaInvoice[i].getHeadVO().setDinvoicedate(ClientEnvironment.getInstance().getDate());
        voaInvoice[i].getHeadVO().setDarrivedate(ClientEnvironment.getInstance().getDate());
        voaInvoice[i].getHeadVO().setIbillstatus(new Integer(0));
        voaInvoice[i].getHeadVO().setFinitflag(new Integer(0));
        voaInvoice[i].getHeadVO().setIinvoicetype(new Integer(0));
        voaInvoice[i].getHeadVO().setIdiscounttaxtype(new Integer(1));
        // 操作人
        String strOperPk = nc.ui.pub.ClientEnvironment.getInstance().getUser().getPrimaryKey();
        voaInvoice[i].getHeadVO().setCoperator(strOperPk);
        // 表体税率、汇率
        voaInvoice[i].getHeadVO().setIdiscounttaxtype(new Integer(0));
        voaInvoice[i].getHeadVO().setNtaxrate(null);

        if (voaInvoice[i].getHeadVO().getCvendormangid() != null
            && voaInvoice[i].getHeadVO().getCvendorbaseid() == null) {
          // 获取供应商基础ID
          String pk_cubasdoc = null;
          Object oTemp = null;
          try {
            oTemp = CacheTool.getCellValue("bd_cumandoc", "pk_cumandoc", "pk_cubasdoc", voaInvoice[i].getHeadVO()
                .getCvendormangid());
          }
          catch (Exception e) {
            SCMEnv.out(e);
          }
          if (oTemp != null && ((Object[]) oTemp).length >= 1) {
            if (((Object[]) oTemp)[0] != null && ((Object[]) oTemp)[0].toString().trim().length() > 0)
              pk_cubasdoc = ((Object[]) oTemp)[0].toString();
          }
          voaInvoice[i].getHeadVO().setCvendorbaseid(pk_cubasdoc);
        }

        // 采购公司
        if (voaInvoice[i].getHeadVO().getPk_purcorp() == null)
          voaInvoice[i].getHeadVO().setPk_purcorp(ClientEnvironment.getInstance().getCorporation().getPrimaryKey());

        InvoiceItemVO[] voaInvItem = voaInvoice[i].getBodyVO();
        HashMap hData = new HashMap();
        for(int j = 0; j < voaInvItem.length; j++){
        	hData.put(voaInvItem[j].getCbaseid(),"");
        }
        PuTool.loadBatchTaxrate((String[]) hData.keySet().toArray(new String[0]), voaInvItem[0].getPk_corp());
        for (int j = 0; j < voaInvItem.length; j++) {
          // 对应的行号
          int index = getDistributedRows()[i][j].intValue();
          // 发票数量同正负控制
          // UFDouble dOleInvoiceNum =
          // (UFDouble)hInvoiceNum.get(((GeneralBillItemVO[])voaStock[i].getChildrenVO())[j].getCgeneralbid());
          // UFDouble dNowInvoiceNum = (UFDouble)
          // getbillListPanel().getHeadBillModel().getValueAt(index,
          // "ninvoicenum");
          // if(dOleInvoiceNum != null && dNowInvoiceNum != null &&
          // dOleInvoiceNum.doubleValue() * dNowInvoiceNum.doubleValue() < 0){
          // return false;
          // }

          // if(voaInvItem[j].getCupsourcebillrowid() != null &&
          // tHQHP.get(voaInvItem[j].getCupsourcebillrowid()) != null){
          // //如果价格结算单存在,则重新计算
          // getbillListPanel().getHeadBillModel().setValueAt(tHQHP.get(voaInvItem[j].getCupsourcebillrowid()),
          // j, "norgnettaxprice");
          // BillEditEvent event = new
          // BillEditEvent(getbillListPanel().getHeadTable(),tHQHP.get(voaInvItem[j].getCupsourcebillrowid()),"norgnettaxprice",j,1);//原币净含税单价
          // afterEditRelations(event);
          // }
          // 重算金额，税额，价税合计
          // voaInvItem[j].setNoriginalcurmny(voaInvItem[j].getNinvoicenum().multiply(voaInvItem[j].getNoriginalnetprice()));
          // voaInvItem[j].setNoriginalsummny(voaInvItem[j].getNinvoicenum().multiply(voaInvItem[j].getNorgnettaxprice()));
          // voaInvItem[j].setNoriginaltaxmny(voaInvItem[j].getNoriginalsummny().sub(voaInvItem[j].getNoriginalcurmny()));
          // 多次开票数量
          GeneralBb3VO bvo = null;
          for (int k = 0; k < bb3VOs.length; k++) {
            if (bb3VOs[k].getCgeneralbid().equals(voaStock[i].getItemVOs()[j].getCgeneralbid())) {
              bvo = bb3VOs[k];
              break;
            }
          }
          double dTemp = 0.0;
          if (bvo != null) {
            if (bvo.getNsignnum() != null) {
              dTemp = bvo.getNsignnum().doubleValue();
            }
            if (bvo.getNaccountwastenum() != null) {
              dTemp -= bvo.getNaccountwastenum().doubleValue();
            }
          }
          double dTemp1 = 0.0;
          if (bvo != null) {
            if (bvo.getNaccountnum1() != null) {
              dTemp1 = bvo.getNaccountnum1().doubleValue();
            }
          }
          // max=MAX(已开票数量-累计损耗数量,已结算数量)
          double max = 0.0;
          if (dTemp > dTemp1) {
            max = dTemp;
          }
          else {
            max = dTemp1;
          }
          //从源头订单设置发票行的一些信息
          setInfoFromOrder(voaInvItem[j]);
          UFDouble dInvNum = PuPubVO.getUFDouble_NullAsZero(voaStock[i].getItemVOs()[j].getNinnum()).sub(max);

          voaInvItem[j].setNinvoicenum(dInvNum);
          //有结算单后，要用优质优价的单价
          //与发票维护界面的优质优价按钮做一致 by zhaoyha at 2009.6.16
          if(cCurrencyID.equals(voaInvItem[j].getCcurrencytypeid()) && //要求币种一致
              PuPubVO.getUFDouble_ZeroAsNull(tHQHP.get(voaInvItem[j].getCupsourcebillrowid())) != null){
      		voaInvItem[j].setNorgnettaxprice((UFDouble)tHQHP.get(voaInvItem[j].getCupsourcebillrowid()));
      		calculate(voaInvItem[j], "norgnettaxprice");
      	  }else
          // 设置发票单价，订单有就从订单取，订单没有就从入库单取
          if (m_OrderItemVOs != null ) {
            OrderItemVO body = null;
            for (int k = 0; k < m_OrderItemVOs.length; k++) {
              if (m_OrderItemVOs[k] != null
                  && m_OrderItemVOs[k].getCorder_bid().equals(voaInvItem[j].getCsourcebillrowid())) {
                body = m_OrderItemVOs[k];
                break;
              }
            }
            if (body != null) {
              //如果入库单价优先，则设置为入库单价
            	voaInvItem[j].setCcurrencytypeid(body.getCcurrencytypeid());
              if(isStorPre() && PuPubVO.getUFDouble_ZeroAsNull(voaStock[i].getItemVOs()[j].getNprice()) != null){
            	  voaInvItem[j].setNoriginalcurprice(voaStock[i].getItemVOs()[j].getNprice());
            	  voaInvItem[j].setNoriginalcurmny(voaStock[i].getItemVOs()[j].getNmny());
            	  exchangeStockToInvoicePrice(voaInvItem[j], voaStock[i].getPk_corp());
//            	  voaInvItem[j].setNorgnettaxprice(voaStock[i].getItemVOs()[j].getNtaxprice());
              }else{
            	  voaInvItem[j].setNoriginalcurprice(body.getNoriginalnetprice());// 发票要取订单无税净价
            	  voaInvItem[j].setNorgnettaxprice(body.getNorgnettaxprice());
              }
              // voaInvItem[j].setNoriginalcurprice(m_OrderItemVOs[j].getNoriginalcurprice());
              // 处理尾差，一次入库多次开票或者多次入库一次开票情况取价
              // 比较发票行和入库单行数量
              if (voaInvItem[j].getNinvoicenum().compareTo(
                  PuPubVO.getUFDouble_NullAsZero(voaStock[i].getItemVOs()[j].getNinnum())) != 0) {
                voaInvItem[j].setNoriginalcurmny(voaInvItem[j].getNinvoicenum()
                    .multiply(
                    		voaInvItem[j].getNoriginalcurprice() == null ? nc.vo.scm.pu.VariableConst.ZERO : voaInvItem[j].getNoriginalcurprice()));
                voaInvItem[j].setNtaxrate(body.getNtaxrate());
//                voaInvItem[j].setNoriginaltaxmny(voaInvItem[j].getNinvoicenum().multiply(voaInvItem[j].getNoriginalcurprice())
//                    .sub(
//                        voaInvItem[j].getNinvoicenum().multiply(
//                        		voaInvItem[j].getNoriginalcurprice() == null ? nc.vo.scm.pu.VariableConst.ZERO : voaInvItem[j].getNoriginalcurprice())));
                voaInvItem[j].setNoriginaltaxmny(voaInvItem[j].getNinvoicenum().multiply(voaInvItem[j].getNoriginalcurprice())
                    .multiply(body.getNtaxrate()).div(new UFDouble(100.00)));
                voaInvItem[j].setNoriginalsummny(voaInvItem[j].getNoriginalcurmny() == null
                    && voaInvItem[j].getNoriginaltaxmny() == null ? null : voaInvItem[j].getNoriginalcurmny().add(
                    voaInvItem[j].getNoriginaltaxmny()));
              }
              else {
                // 比较发票行和对应订单行数量
            	if (voaInvItem[j].getNinvoicenum().compareTo(body.getNordernum()) == 0
            			&& !(isStorPre() && PuPubVO.getUFDouble_ZeroAsNull(voaStock[i].getItemVOs()[j].getNprice()) != null)) {
                  voaInvItem[j].setNoriginalcurmny(body.getNoriginalcurmny());
                  voaInvItem[j].setNtaxrate(body.getNtaxrate());
                  voaInvItem[j].setNoriginaltaxmny(body.getNoriginaltaxmny());
                  voaInvItem[j].setNoriginalsummny(body.getNoriginaltaxpricemny());
                }
                else {
                  voaInvItem[j].setNtaxrate(body.getNtaxrate());// 税率
//                  calInitRelations(voaInvItem[j]);
                  if(PuPubVO.getUFDouble_ZeroAsNull(voaInvItem[j].getNorgnettaxprice()) != null
                		  && PuTool.getPricePriorPolicy(voaInvItem[j].getPk_corp()) == RelationsCalVO.TAXPRICE_PRIOR_TO_PRICE){
                	  calculate(voaInvItem[j], "norgnettaxprice");//noriginalcurprice
                  }else {
                	  calculate(voaInvItem[j], "noriginalcurprice");
                  }
                }
              }
            }else{
            	if(isStorPre() ){
            		voaInvItem[j].setNoriginalcurprice(voaStock[i].getItemVOs()[j].getNprice());
            		voaInvItem[j].setNoriginalcurmny(voaStock[i].getItemVOs()[j].getNmny());
            		exchangeStockToInvoicePrice(voaInvItem[j], voaStock[i].getPk_corp());
            		voaInvItem[j].setNtaxrate(PuTool.getInvTaxRate(voaInvItem[j].getCbaseid()));
            		calculate(voaInvItem[j], "noriginalcurprice");
            	}
            }

          }
          else {
            voaInvItem[j].setNoriginalcurprice(voaStock[i].getItemVOs()[j].getNprice());
            voaInvItem[j].setNoriginalcurmny(voaStock[i].getItemVOs()[j].getNmny());
          }

          if (voaInvItem[j].getNinvoicenum().compareTo(
              PuPubVO.getUFDouble_NullAsZero(voaStock[i].getItemVOs()[j].getNinnum())) != 0) {
//            calInitRelations(voaInvItem[j]);
        	  calculate(voaInvItem[j], "ninvoicenum");
          }
//          if (m_hasOrderInfo != null && m_hasOrderInfo.size() > 0 && voaInvItem[j].getCupsourcebillrowid() != null
//              && voaInvItem[j].getCupsourcebillrowid().trim().length() > 0) {
//            Object oTemp = m_hasOrderInfo.get(voaInvItem[j].getCupsourcebillrowid());
//            if (oTemp != null) {
//              String sInfo[] = (String[]) oTemp;
//              voaInvItem[j].setCorderid(sInfo[0]);
//              voaInvItem[j].setCorder_bid(sInfo[1]);
//              voaInvItem[j].setCcurrencytypeid(sInfo[2]);
//            }
//          }

          // 自制入库单生成采购发票时应带出币种、税率
          if (voaInvItem[j].getCcurrencytypeid() == null) {
            Object lObj_VendorMangid = getbillListPanel().getSourceBodyValueAt(index, "cvendormangid");
            if (lObj_VendorMangid != null && lObj_VendorMangid.toString().trim().length() > 0) {
              Object oTemp = m_hCurrTypeID.get(lObj_VendorMangid);
              if (oTemp != null) {
                voaInvItem[j].setCcurrencytypeid(oTemp.toString().trim());
              }
              else {
                voaInvItem[j].setCcurrencytypeid(m_sCurrTypeID);
              }
            }
            else {
              voaInvItem[j].setCcurrencytypeid(m_sCurrTypeID);
            }
          }

          // //发票数量
          // voaInvItem[j].setNinvoicenum(dNowInvoiceNum);
          // voaInvItem[j].setNoriginalcurprice((UFDouble)
          // getbillListPanel().getHeadBillModel().getValueAt(index,
          // "noriginalcurprice"));
          // voaInvItem[j].setNoriginalcurmny((UFDouble)
          // getbillListPanel().getHeadBillModel().getValueAt(index,
          // "noriginalcurmny"));
          // voaInvItem[j].setNtaxrate((UFDouble)
          // getbillListPanel().getHeadBillModel().getValueAt(index,
          // "ntaxrate"));
          //			  								
          // voaInvItem[j].setNoriginaltaxmny((UFDouble)
          // getbillListPanel().getHeadBillModel().getValueAt(index,
          // "noriginaltaxmny"));
          // voaInvItem[j].setNoriginalsummny((UFDouble)
          // getbillListPanel().getHeadBillModel().getValueAt(index,
          // "noriginalsummny"));
          //			  	
          // voaInvItem[j].setNorgnettaxprice((UFDouble)
          // getbillListPanel().getHeadBillModel().getValueAt(index,
          // "norgnettaxprice"));
          // voaInvItem[j].setNreasonwastenum((UFDouble)
          // getbillListPanel().getHeadBillModel().getValueAt(index,
          // "nreasonwastenum"));
          //			  	
          // //扣税类别
          // Object ob = getbillListPanel().getHeadBillModel().getValueAt(index,
          // "idiscounttaxtype");
          // if (ob != null) {
          // voaInvItem[j].setIdiscounttaxtype((Integer)
          // getbillListPanel().getBodyItem("idiscounttaxtype").converType(ob));
          // } else {
          // voaInvItem[j].setIdiscounttaxtype(new Integer(1));
          // }

          // 删除标记
          voaInvItem[j].setDr(new Integer(0));
          // 折本及折辅江率
          voaInvItem[j].setNexchangeotobrate(null);

          GeneralBillItemVO[] genItemVOs = (GeneralBillItemVO[]) voaStock[i].getChildrenVO();

          if (m_hasUpSourceTS != null && m_hasUpSourceTS.size() > 0 && voaInvItem[j].getCupsourcebillrowid() != null
              && voaInvItem[j].getCupsourcebillrowid().trim().length() > 0) {
            Object oTemp = m_hasUpSourceTS.get(voaInvItem[j].getCupsourcebillrowid());
            if (oTemp != null) {
              String ts[] = (String[]) oTemp;
              voaInvItem[j].setCupsourcehts(ts[0]);
              voaInvItem[j].setCupsourcebts(ts[1]);
            }
          }

          voaInvItem[j].setVmemo((String) genItemVOs[j].getAttributeValue("vnotebody"));

          String sCupsourceBillId = voaInvItem[j].getCupsourcebillid();
          // 关于多个采购入库单生成同一发票仓库信息丢失的处理
          if (sCupsourceBillId != null && sCupsourceBillId.length() > 0
              && voaStock[i].getHeaderVO().getPk_corp().equals(voaInvoice[i].getHeadVO().getPk_corp())) {
            String sWareHouseId = (String) hWareHouseId.get(sCupsourceBillId);
            if (sWareHouseId != null && sWareHouseId.trim().length() > 0)
              voaInvItem[j].setCwarehouseid(sWareHouseId);
          }
          else {
            voaInvItem[j].setCwarehouseid(null);
          }

          // 处理源头信息(自制入库单)
          if (sCupsourceBillId != null && sCupsourceBillId.length() > 0
              && (voaInvItem[j].getCsourcebillid() == null || voaInvItem[j].getCsourcebillid().length() == 0)) {
            voaInvItem[j].setCsourcebillid(sCupsourceBillId);
            voaInvItem[j].setCsourcebillrowid(voaInvItem[j].getCupsourcebillrowid());
            voaInvItem[j].setCsourcebilltype(voaInvItem[j].getCupsourcebilltype());
          }
        }
      }

      // 收票公司和收货公司不同,存货管理ID需要转换为收票公司管理ID
      String pk_invcorp = voaInvoice[0].getHeadVO().getPk_corp();
      for (int i = 0; i < voaInvoice.length; i++) {
        GeneralBillItemVO itemVO[] = voaStock[i].getItemVOs();
        for (int j = 0; j < itemVO.length; j++) {
          if (itemVO[j].getAttributeValue("pk_corp") == null) {
            continue;
          }
          if (!itemVO[j].getAttributeValue("pk_corp").equals(pk_invcorp)) {
            voaInvoice[i].getHeadVO().setCstoreorganization(null);
            break;
          }
        }
      }
      Vector v = new Vector(), vv = new Vector();
      for (int i = 0; i < voaStock.length; i++) {
        GeneralBillItemVO itemVO[] = voaStock[i].getItemVOs();
        for (int j = 0; j < itemVO.length; j++) {
          if (voaStock[i].getHeaderVO().getAttributeValue("pk_corp") == null) {
            continue;
          }
          if (!voaStock[i].getHeaderVO().getAttributeValue("pk_corp").equals(pk_invcorp) && !v.contains(itemVO[j].getCinvbasid())) {
            v.addElement(itemVO[j].getCinvbasid());
            vv.addElement(voaStock[i].getHeaderVO().getAttributeValue("pk_corp"));
          }
        }
      }
      HashMap<String, String> hmInventory = new HashMap<String, String>();
      ChgDocPkVO chgInvVO[] = null;
      if (v.size() > 0) {
        chgInvVO = new ChgDocPkVO[v.size()];
        for (int i = 0; i < v.size(); i++) {
          chgInvVO[i] = new ChgDocPkVO();
          chgInvVO[i].setSrcBasId((String) v.elementAt(i));
          chgInvVO[i].setSrcCorpId((String) vv.elementAt(i));
          chgInvVO[i].setDstCorpId(pk_invcorp);
        }
//        chgInvVO = ChgDataUtil.chgPkInvByCorpBase(chgInvVO);
        hmInventory = PubHelper.getInvManByDstCorpAndBasDoc(chgInvVO);
      }

      for (int i = 0; i < voaInvoice.length; i++) {
        InvoiceItemVO itemVO[] = voaInvoice[i].getBodyVO();
        for (int j = 0; j < itemVO.length; j++) {
          if (chgInvVO != null && chgInvVO.length > 0) {
            for (int k = 0; k < chgInvVO.length; k++) {
              if (itemVO[j].getCbaseid().equals(chgInvVO[k].getSrcBasId())) {
                itemVO[j].setCmangid(hmInventory.get(chgInvVO[k].getDstCorpId() + chgInvVO[k].getSrcBasId()));
                break;
              }
            }
          }
        }
      }

      // 收票公司和采购公司不同,供应商管理ID需要转换为收票公司管理ID
      v = new Vector();
      vv = new Vector();
      for (int i = 0; i < voaStock.length; i++) {
        GeneralBillHeaderVO headVO = voaStock[i].getHeaderVO();
        InvoiceHeaderVO invHeadVO = voaInvoice[i].getHeadVO();
        if (headVO.getAttributeValue("pk_purcorp") == null)
          continue;
        if (!pk_invcorp.equals(headVO.getAttributeValue("pk_purcorp")) && 
            !StringUtil.isEmptyWithTrim(invHeadVO.getCvendormangid())
            && !v.contains(invHeadVO.getCvendormangid())) {
          v.addElement(invHeadVO.getCvendormangid());
          vv.addElement(headVO.getAttributeValue("pk_purcorp"));
        }
      }
      ChgDocPkVO chgCuVO[] = null;
      if (v.size() > 0) {
        chgCuVO = new ChgDocPkVO[v.size()];
        for (int i = 0; i < v.size(); i++) {
          chgCuVO[i] = new ChgDocPkVO();
          chgCuVO[i].setSrcManId((String) v.elementAt(i));
          chgCuVO[i].setSrcCorpId((String) vv.elementAt(i));
          chgCuVO[i].setDstCorpId(pk_invcorp);
        }
        chgCuVO = ChgDataUtil.chgPkCuByCorp(chgCuVO);
      }

      for (int i = 0; i < voaInvoice.length; i++) {
        InvoiceHeaderVO headVO = voaInvoice[i].getHeadVO();
        if (chgCuVO != null && chgCuVO.length > 0) {
          for (int k = 0; k < chgCuVO.length; k++) {
            if (headVO.getCvendormangid() != null && chgCuVO[k].getSrcManId() != null
                && headVO.getCvendormangid().equals(chgCuVO[k].getSrcManId())) {
              headVO.setCvendorbaseid(chgCuVO[k].getSrcBasId());
              headVO.setCvendormangid(chgCuVO[k].getDstManId());
            }
          }
        }
      }
      HashMap hOrderData = new HashMap();
      for (int i = 0; i < m_OrderItemVOs.length; i++) {
        if (m_OrderItemVOs != null && m_OrderItemVOs[i] != null) {
          hOrderData.put(m_OrderItemVOs[i].getCorder_bid(), m_OrderItemVOs[i]);
        }
      }
      if (voaStock != null && voaStock.length != 0) {
        String strCorp = voaInvoice[0].getBodyVO()[0].getPk_corp();
        int iPricePriorPolicy = PuTool.getPricePriorPolicy(strCorp) == 6 ? 1 : 0;
        Vector<String> vecString = new Vector<String>();
        for (int i = 0; i < voaStock.length; i++) {
          GeneralBillItemVO[] voBody = (GeneralBillItemVO[]) voaStock[i].getChildrenVO();
          for (int j = 0; j < voBody.length; j++) {
            vecString.add(voBody[j].getCfirstbillbid());
          }
        }
        String[] strGeneralbid = null;
        if (vecString.size() > 0) {
          strGeneralbid = new String[vecString.size()];
          vecString.copyInto(strGeneralbid);
        }
        //效率优化，走VO交换后处理 by zhaoyha at 2009.8
        Map<String, InvoiceItemVO[]> hData = getTmpDataVo().getSrcOrderPreGenInvInfo(); 
          //InvoiceHelper.queryInvoiceBodysStore(strGeneralbid);

        for (int i = 0; i < voaStock.length; i++) {
          // 入库单自制
          if (((GeneralBillItemVO) voaStock[i].getChildrenVO()[0]).getCfirstbillhid() == null) {
            continue;
          }

          GeneralBillItemVO[] voBody = (GeneralBillItemVO[]) voaStock[i].getChildrenVO();
          java.util.ArrayList aList = new java.util.ArrayList();
          for (int j = 0; j < voBody.length; j++) {
            if (hData != null && hData.size() > 0) {
              InvoiceItemVO[] items = hData.get(voBody[j].getCfirstbillbid());
              aList.add(items);
            }
          }
          OrderItemVO[] voaBVOs = new OrderItemVO[voaStock[i].getChildrenVO().length];

          for (int j = 0; j < voaStock[i].getChildrenVO().length; j++) {
            if (hOrderData.get(((GeneralBillItemVO) voaStock[i].getChildrenVO()[j]).getCsourcebillbid()) != null) {
            	OrderItemVO orderItemVOtemp =  (OrderItemVO) hOrderData.get(((GeneralBillItemVO) voaStock[i].getChildrenVO()[j])
                        .getCsourcebillbid());
            	// 如果发票价格不同于订单价格，则不进行容差处理
            	if(voaInvoice[i].getBodyVO()[j].getNoriginalcurprice().compareTo(orderItemVOtemp.getNoriginalnetprice()) != 0){
            		voaBVOs[j] = null;
            	}else{
            		voaBVOs[j] = orderItemVOtemp;
            	}
            }
            else {
//            	OrderItemVO orderItemVOtemp =  (OrderItemVO) hOrderData.get(((GeneralBillItemVO) voaStock[i].getChildrenVO()[j])
//                        .getCfirstbillbid());
//            	// 如果发票价格不同于订单价格，则不进行容差处理
//            	if(voaInvoice[i].getBodyVO()[j].getNoriginalcurprice().compareTo(orderItemVOtemp.getNoriginalnetprice()) != 0){
            		voaBVOs[j] = null;
//            	}else{
//            		voaBVOs[j] = orderItemVOtemp;
//            	}
//              voaBVOs[j] = (OrderItemVO) hOrderData.get(((GeneralBillItemVO) voaStock[i].getChildrenVO()[j])
//                  .getCfirstbillbid());
            }
          }

          if (voaBVOs != null && voaBVOs[0] != null && aList.size()>0) {//&& voaInvoice[i].getBodyVO()
            nc.vo.transfer.UpToDownEqualValueTool.setValueEqualToUpBillForStore("21", "25", iPricePriorPolicy, voaBVOs,
                aList, voaInvoice[i].getBodyVO());
          }

          if (PuTool.getPricePriorPolicy(strCorp) == nc.vo.scm.pu.RelationsCalVO.PRICE_PRIOR_TO_TAXPRICE) {
            for (int j = 0; j < voaInvoice[i].getBodyVO().length; j++) {
              voaInvoice[i].getBodyVO()[j].setNoriginalsummny(PuPubVO.getUFDouble_NullAsZero(
                  voaInvoice[i].getBodyVO()[j].getNoriginalcurmny()).add(
                  voaInvoice[i].getBodyVO()[j].getNoriginaltaxmny() == null ? nc.vo.scm.pu.VariableConst.ZERO
                      : voaInvoice[i].getBodyVO()[j].getNoriginaltaxmny()));
            }
          }
          else {
            for (int j = 0; j < voaInvoice[i].getBodyVO().length; j++) {
              voaInvoice[i].getBodyVO()[j].setNoriginalcurmny(PuPubVO.getUFDouble_NullAsZero(
                  voaInvoice[i].getBodyVO()[j].getNoriginalsummny()).sub(
                  voaInvoice[i].getBodyVO()[j].getNoriginaltaxmny() == null ? nc.vo.scm.pu.VariableConst.ZERO
                      : voaInvoice[i].getBodyVO()[j].getNoriginaltaxmny()));
            }
          }
        }
      }

    }
    catch (Exception e) {
      // String strErrMsg = e.getMessage();
      // MessageDialog.showHintDlg(this,
      // nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON","UPPSCMCommon-000270")/*@res
      // "提示"*/, strErrMsg);
      throw e;
    }
    return true;

  }

  /**
   * 
   * 方法功能描述：设置发票行的订单信息。
   * <p>
   * <b>examples:</b>
   * <p>
   * 使用示例
   * <p>
   * <b>参数说明</b>
   * @param invcItem
   * <p>
   * @author zhaoyha
   * @time 2009-6-16 下午02:06:09
   */
  private void setInfoFromOrder(InvoiceItemVO invcItem) {
    if (m_hasOrderInfo != null && m_hasOrderInfo.size() > 0 && invcItem.getCupsourcebillrowid() != null
        && invcItem.getCupsourcebillrowid().trim().length() > 0) {
      Object oTemp = m_hasOrderInfo.get(invcItem.getCupsourcebillrowid());
      if (oTemp != null) {
        String sInfo[] = (String[]) oTemp;
        invcItem.setCorderid(sInfo[0]);
        invcItem.setCorder_bid(sInfo[1]);
        invcItem.setCcurrencytypeid(sInfo[2]);
      }
    }
  }

  public GeneralBb3VO[] getBb3VOs() {
    return bb3VOs;
  }

  public void setBb3VOs(GeneralBb3VO[] bb3VOs) {
    this.bb3VOs = bb3VOs;
  }

//  private void calInitRelations(InvoiceItemVO voaBody) {
//    if (voaBody == null) {
//      return;
//    }
//    String strCorp = voaBody.getPk_corp();
//    if (PuTool.getPricePriorPolicy(strCorp) == nc.vo.scm.pu.RelationsCalVO.PRICE_PRIOR_TO_TAXPRICE) {
//      // 金额
//      UFDouble dMoney = voaBody.getNinvoicenum().multiply(
//          voaBody.getNoriginalcurprice() == null ? nc.vo.scm.pu.VariableConst.ZERO : voaBody.getNoriginalcurprice());
//      UFDouble dTaxrate = voaBody.getNtaxrate();
//      if (dTaxrate == null) {
//        dTaxrate = nc.vo.scm.pu.VariableConst.ZERO;
//      }
//      dTaxrate = dTaxrate.div(100.0);
//      // 
//      voaBody.setNorgnettaxprice(voaBody.getNoriginalcurprice().multiply(new UFDouble(1.0).add(dTaxrate)));
//      int strDisTaxName = voaBody.getIdiscounttaxtype();
//      UFDouble dTax = null;
//      // 应税内含
//      if (strDisTaxName == 0) {
//        if (dTaxrate.doubleValue() == 100.0) {
//          dTax = dMoney;
//          dMoney = new UFDouble(0.0);
//          voaBody.setNoriginalnetprice(dMoney);
//        }
//        else {
//          dTax = dMoney.multiply(dTaxrate).div(new UFDouble(1.0).sub(dTaxrate));
//        }
//        // 应税外加
//      }
//      else if (strDisTaxName == 1) {
//        dTax = dMoney.multiply(dTaxrate);
//        // 不计税
//      }
//      else if (strDisTaxName == 2) {
//        dTax = nc.vo.scm.pu.VariableConst.ZERO;
//      }
//      else {
//        dTax = nc.vo.scm.pu.VariableConst.ZERO;
//      }
//      UFDouble dSummny = dMoney.add(dTax);
//      voaBody.setNoriginalcurmny(dMoney);
//      voaBody.setNoriginaltaxmny(dTax);
//      voaBody.setNoriginalsummny(dSummny);
//    }
//    else {
//      UFDouble dNorgnettaxprice = voaBody.getNorgnettaxprice();
//      if (dNorgnettaxprice == null)
//        dNorgnettaxprice = VariableConst.ZERO;
//
//      // 价稅合计
//      UFDouble uNoriginaltaxpricemny = voaBody.getNinvoicenum().multiply(dNorgnettaxprice);
//      UFDouble dTaxrate = voaBody.getNtaxrate();
//      if (dTaxrate == null) {
//        dTaxrate = nc.vo.scm.pu.VariableConst.ZERO;
//      }
//      dTaxrate = dTaxrate.div(100.0);
//      int strDisTaxName = voaBody.getIdiscounttaxtype();
//      UFDouble dTax = null;
//      // 应税内含
//      if (strDisTaxName == 1) {
//        dTax = uNoriginaltaxpricemny.multiply(dTaxrate.div(new UFDouble(1.0).add(dTaxrate)));
//        // 应税外加
//      }
//      else if (strDisTaxName == 0) {
//        dTax = uNoriginaltaxpricemny.multiply(dTaxrate);
//        // 不计税
//      }
//      else if (strDisTaxName == 2) {
//        dTax = nc.vo.scm.pu.VariableConst.ZERO;
//      }
//      else {
//        dTax = nc.vo.scm.pu.VariableConst.ZERO;
//      }
//      voaBody.setNoriginalsummny(uNoriginaltaxpricemny);
//      voaBody.setNoriginaltaxmny(dTax);
//      // 金额
//      UFDouble dMoney = uNoriginaltaxpricemny.sub(dTax);
//      voaBody.setNoriginalcurmny(dMoney);
//    }
//    return;
//  }

  public void loadHeadData() {
    Object[] result = new Object[3];
    try {
		String strReplacedWhere = null;
		if(getBillType().equals(nc.vo.scm.pu.BillTypeConst.STORE_SC)){
			strReplacedWhere = ((nc.ui.pi.invoice.InvFrmStoQueDlg) QueryConditionClient()).getScStoWhereSQL(
					getNormalCondVOs(), ((nc.ui.pi.invoice.InvFrmStoQueDlg) QueryConditionClient()).getConditionVO());
		}else{
			strReplacedWhere = ((nc.ui.pi.invoice.InvFrmStoQueDlg) QueryConditionClient()).getPoStoWhereSQL(
					getNormalCondVOs(), ((nc.ui.pi.invoice.InvFrmStoQueDlg) QueryConditionClient()).getConditionVO());
		}      if (strReplacedWhere.equals("null")) {
        strReplacedWhere = null;
      }
      ConditionVO[] powerVOs = null;
      Vector v = new Vector();
      // 数据权限控制
      ConditionVO tempVO = new ConditionVO();
      tempVO.setFieldName("操作员");
      tempVO.setFieldCode("");
      tempVO.setOperaCode("");
      tempVO.setValue(nc.ui.pub.ClientEnvironment.getInstance().getUser().getPrimaryKey());
      v.addElement(tempVO);
      tempVO = new ConditionVO();
      tempVO.setFieldName("公司");
      tempVO.setFieldCode("");
      tempVO.setOperaCode("");
      tempVO.setValue(nc.ui.pub.ClientEnvironment.getInstance().getCorporation().getPrimaryKey());
      v.addElement(tempVO);

      powerVOs = new ConditionVO[v.size()];
      v.copyInto(powerVOs);

      Object[] ob = null;
      if(getBillType().equals(nc.vo.scm.pu.BillTypeConst.STORE_SC)){
    	  ob = InvoiceHelper.queryGenenelVOsByFromWhere(nc.vo.scm.pu.BillTypeConst.STORE_SC, strReplacedWhere, powerVOs);
      }else{
    	  ob = InvoiceHelper.queryGenenelVOsByFromWhere(nc.vo.scm.pu.BillTypeConst.STORE_PO, strReplacedWhere, powerVOs);
      }
      
      if (ob == null) {
        getbillListPanel().clearUIData();
        return;
      }
      invoiceVO = ((nc.ui.pi.invoice.InvFrmStoQueDlg) QueryConditionClient()).getInvoiceVO();
      Vector<GeneralBillVO> rightVO = new Vector<GeneralBillVO>();

      setM_OrderItemVOs((OrderItemVO[]) ob[4]);
      setBb3VOs((GeneralBb3VO[]) ob[1]);
      GeneralBillVO[] hVOs = (GeneralBillVO[]) ob[0];
      if (hVOs == null || hVOs.length < 1) {
        getbillListPanel().clearUIData();
        return;
      }

      Vector<GeneralBillHeaderVO> headVOs = new Vector<GeneralBillHeaderVO>();
      GeneralBillHeaderVO[] headVOsForPo = null;
      GeneralBillItemVO[] itemVOsTForPo = null;
      Vector<GeneralBillItemVO> itemVOs = new Vector<GeneralBillItemVO>();

      // 如果有追加缓存
      if (invoiceVO != null && invoiceVO.length > 0) {
        Set<String> setGeneralBillItemID = new HashSet<String>();
        // 统计发票界面上已有的入库单行ID:nc.vo.pi.InvoiceItemVO.m_cupsourcebillrowid
        for (int ii = 0; ii < invoiceVO.length; ii++) {
          if (invoiceVO[ii].getChildrenVO() != null && invoiceVO[ii].getChildrenVO().length > 0) {
            for (int j = 0; j < invoiceVO[ii].getChildrenVO().length; j++) {
              if (PuPubVO.getString_TrimZeroLenAsNull(((InvoiceItemVO) invoiceVO[ii].getChildrenVO()[j])
                  .getCupsourcebillrowid()) != null
                  && PuPubVO.getString_TrimZeroLenAsNull(((InvoiceItemVO) invoiceVO[ii].getChildrenVO()[j])
                      .getPrimaryKey()) == null) {
                setGeneralBillItemID.add(((InvoiceItemVO) invoiceVO[ii].getChildrenVO()[j])
                    .getCupsourcebillrowid().toString());
//                put(String.valueOf(j), ((InvoiceItemVO) invoiceVO[ii].getChildrenVO()[j])
//                    .getCupsourcebillrowid().toString());
              }
            }
          }
        }
        // 循环查出来的入库单，与发票界面上的单据比较，已有的不显示
        for (int ii = 0; ii < hVOs.length; ii++) {
          Vector<GeneralBillItemVO> rightItem = new Vector<GeneralBillItemVO>();
          for (int j = 0; j < hVOs[ii].getChildrenVO().length; j++) {
            if (!setGeneralBillItemID.contains((((nc.vo.ic.pub.bill.GeneralBillItemVO) hVOs[ii].getChildrenVO()[j])
                .getCgeneralbid()))) {
              rightItem.add((GeneralBillItemVO) hVOs[ii].getChildrenVO()[j]);
            }
          }
          if (rightItem.size() > 0) {
            GeneralBillItemVO[] item = new GeneralBillItemVO[rightItem.size()];
            rightItem.copyInto(item);
            GeneralBillVO vo = new GeneralBillVO();
            vo.setParentVO(hVOs[ii].getParentVO());
            vo.setChildrenVO(item);
            rightVO.add(vo);
          }
        }
        if (rightVO.size() > 0) {
          GeneralBillVO[] vos = new GeneralBillVO[rightVO.size()];
          rightVO.copyInto(vos);
          for (int ii = 0; ii < vos.length; ii++) {
            headVOs.add((GeneralBillHeaderVO) vos[ii].getParentVO());
            for (int j = 0; j < vos[ii].getChildrenVO().length; j++) {
//            	((GeneralBillItemVO) vos[ii].getChildrenVO()[j]).setVfree0((String)((GeneralBillItemVO) vos[ii].getChildrenVO()[j]).getAttributeValue("vfree0"));
              itemVOs.add((GeneralBillItemVO) vos[ii].getChildrenVO()[j]);
            }
          }
        }
        // 没有追加缓存
      }
      else {
        for (int i = 0; i < hVOs.length; i++) {
          headVOs.add((GeneralBillHeaderVO) hVOs[i].getParentVO());
          for (int j = 0; j < hVOs[i].getChildrenVO().length; j++) {
//        	((GeneralBillItemVO) hVOs[i].getChildrenVO()[j]).setVfree0((String)((GeneralBillItemVO) hVOs[i].getChildrenVO()[j]).getAttributeValue("vfree0"));
            itemVOs.add((GeneralBillItemVO) hVOs[i].getChildrenVO()[j]);
          }
        }

      }

      if (headVOs.size() > 0) {
        headVOsForPo = new GeneralBillHeaderVO[headVOs.size()];
        headVOs.copyInto(headVOsForPo);
      }
      if (itemVOs.size() > 0) {
        itemVOsTForPo = new GeneralBillItemVO[itemVOs.size()];
        itemVOs.copyInto(itemVOsTForPo);
      }
      // 计算自由项
      
      (new FreeVOParse()).setFreeVO(itemVOsTForPo, "vfree0", "vfree","freevo",
				"pk_invbasdoc", "cinventoryid", true);
      
//      +
      result[0] = headVOsForPo;
      result[1] = itemVOsTForPo;
    }
    catch (Exception e) {
      nc.vo.scm.pub.SCMEnv.out("数据加载失败！");
      nc.vo.scm.pub.SCMEnv.out(e);
      showErroMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000255")/*
                                                                                                       * @res
                                                                                                       * "数据加载失败"
                                                                                                       */);
      return;
    }
    try {
      if (result != null && result[0] != null && ((Object[]) result[0]).length > 0) {

        getbillListPanel().setSourceVOToUI((CircularlyAccessibleValueObject[]) result[0],
            (CircularlyAccessibleValueObject[]) result[1]);
        if (result.length >= 3 && result[2] != null && result[2].toString().trim().length() > 0)
          showHintMessage(result[3].toString());
      }
      else {
        getbillListPanel().clearUIData();
      }
    }
    catch (Exception e) {
      nc.vo.scm.pub.SCMEnv.out("数据加载失败！");
      nc.vo.scm.pub.SCMEnv.out(e);
      showErroMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000255")/*
                                                                                                       * @res
                                                                                                       * "数据加载失败"
                                                                                                       */);
      return;
    }
    isUpdateUIDataWhenSwitch = true;
  }

  public QueryConditionClient QueryConditionClient() {
    return ((BillReferQueryProxyStoreToPi) super.getQueyDlg()).createOldQryDlg();
  }

  private NormalCondVO[] getNormalCondVOs() {
    // 发票日期，供应商，存货，部门，业务员,存货分类，地区分类
    // 是否期初，是否审批，是否费用

    // 查询条件VO
    NormalCondVO[] vos = new NormalCondVO[3];
    vos[0] = new NormalCondVO("公司", getPkCorp());
    vos[1] = new NormalCondVO("业务类型", sBusinessType);
    vos[2] = new NormalCondVO("单据类型", sBilltype);

    return vos;
  }

  public nc.vo.pi.InvoiceVO[] getInvoiceVO() {
    return invoiceVO;
  }
  /**
   * 
   * 参数PO83中，是否入库单价格优先于订单价格
   * <p>
   * <b>examples:</b>
   * <p>
   * 使用示例
   * <p>
   * <b>参数说明</b>
   * @return 入库单优先于订单 true ；订单优先于入库单  false
   * <p>
   * @author donggq
   * @time 2008-11-10 上午10:24:54
   */
  public boolean isStorPre(){
	  if (pareVO == null)
		  return false;
	  String initPare = pareVO.getValue();
	  // "8" 入库单价
	  // "6" 订单价
	  if(initPare != null 
			  && initPare.indexOf("8") < initPare.indexOf("6") 
			  && initPare.indexOf("8") != -1)
		  return true;
	  else if (initPare != null 
			  && initPare.indexOf("6") == -1
			  && initPare.indexOf("8") != -1)
		  return true;
	  return false;}
  /**
   * 
   * 重新计算，调用供应链公共方法
   * <p>
   * <b>examples:</b>
   * <p>
   * 使用示例
   * <p>
   * <b>参数说明</b>
   * @param bodyVO
   * @param changedKey
   * <p>
   * @author donggq
   * @time 2008-11-19 下午02:32:26
   */
  private void calculate(InvoiceItemVO bodyVO,String changedKey){
	    // 扣税类别 0应税内含 1应税外加 2不计税
	    String s = "应税内含";

	    if (bodyVO != null ) {
	      if (bodyVO.getIdiscounttaxtype().intValue() == 1)
	        s = "应税外加";
	      else if (bodyVO.getIdiscounttaxtype().intValue() == 2)
	        s = "不计税";
	    }
	    String[] keys = new String[] {
	            s, "idiscounttaxtype", "ninvoicenum", "noriginalcurprice", "norgnettaxprice", "ntaxrate", "noriginalcurmny",
	            "noriginaltaxmny", "noriginalsummny"
	        };
	    int[] descriptions = new int[] {
	            RelationsCal.DISCOUNT_TAX_TYPE_NAME, RelationsCal.DISCOUNT_TAX_TYPE_KEY, RelationsCal.NUM,
	            RelationsCal.NET_PRICE_ORIGINAL, RelationsCal.NET_TAXPRICE_ORIGINAL, RelationsCal.TAXRATE,
	            RelationsCal.MONEY_ORIGINAL, RelationsCal.TAX_ORIGINAL, RelationsCal.SUMMNY_ORIGINAL
	        };
	    SCMRelationsCal.calculate(bodyVO, new int[] {
	            PuTool.getPricePriorPolicy(bodyVO.getPk_corp()), 7//调整折扣
	    }, changedKey, descriptions, keys);
  }
  /**
   * 是否显示目标单据操作面板.
   * 创建日期:(2001-3-23 2:02:27)
   * @param e ufbill.BillEditEvent
   */
  protected boolean isShowInvHandPane(){
    return false;
  }
  
  /**
   * 
   * 方法功能描述：将入库单价(已经设置到发票的无税原币价上)转换为发票币种价格。
   * <p>
   * <b>examples:</b>
   * <p>
   * 使用示例
   * <p>
   * <b>参数说明</b>
   * @param invItemVo 已经设置未转换前的入库单价的发票VO
   * @param stockPk_corp 入库单所在公司
   * @throws BusinessException
   * <p>
   * @author zhaoyha
   * @time 2009-2-11 上午11:38:44
   */
  public void exchangeStockToInvoicePrice(InvoiceItemVO invItemVo,String stockPk_corp) throws BusinessException{
    String currentPk_corp=invItemVo.getPk_corp();
    //取汇率:发票币种---->入库单公司本位币
    UFDouble exchgRate = new CurrencyRateUtil(currentPk_corp)
      .getRate(invItemVo.getCcurrencytypeid()==null?CurrParamQuery.getInstance().getLocalCurrPK(invItemVo.getPk_corp()):invItemVo.getCcurrencytypeid(),CurrParamQuery.getInstance().getLocalCurrPK(stockPk_corp)
          ,nc.ui.po.pub.PoPublicUIClass.getLoginDate().toString());
    //从本币向原种折算,要将汇率先处理
    if(exchgRate==null || exchgRate.doubleValue()==0) exchgRate=new UFDouble(0);
    else exchgRate=new UFDouble(1.0).div(exchgRate);
    
    UFDouble changedPrice = POPubSetUI.getCurrArith_Busi(currentPk_corp).getAmountByOpp(
        invItemVo.getCcurrencytypeid()==null?CurrParamQuery.getInstance().getLocalCurrPK(invItemVo.getPk_corp()):invItemVo.getCcurrencytypeid()
        		,CurrParamQuery.getInstance().getLocalCurrPK(stockPk_corp),
        invItemVo.getNoriginalcurprice(),exchgRate,nc.ui.po.pub.PoPublicUIClass.getLoginDate().toString(),
        new POPubSetUI(currentPk_corp).getPriceDecimal());
    UFDouble changeMoney = POPubSetUI.getCurrArith_Busi(currentPk_corp).getAmountByOpp(
        invItemVo.getCcurrencytypeid()==null?CurrParamQuery.getInstance().getLocalCurrPK(invItemVo.getPk_corp()):invItemVo.getCcurrencytypeid()
        		,CurrParamQuery.getInstance().getLocalCurrPK(stockPk_corp),
        invItemVo.getNoriginalcurmny(),exchgRate,nc.ui.po.pub.PoPublicUIClass.getLoginDate().toString(),
        POPubSetUI.getMoneyDigitByCurr_Busi(currentPk_corp));
    //设置上转换完成的价格
    invItemVo.setNoriginalcurprice(changedPrice);
    invItemVo.setNoriginalcurmny(changeMoney);
  }

  /**
   * @return tmpDataVo
   */
  public InvoiceTempDataVO getTmpDataVo() {
    return tmpDataVo;
  }

  /**
   * @param tmpDataVo 要设置的 tmpDataVo
   */
  public void setTmpDataVo(InvoiceTempDataVO tmpDataVo) {
    this.tmpDataVo = tmpDataVo;
  }

}
