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
  
  //Ϊ���Ч��,��������
  private SysInitVO pareVO = null;
  
  //���汾λ�� by zhaoyha at 2009.6.16
  private String cCurrencyID = PiPqPublicUIClass.getNativeCurrencyID();
  //InvoiceTempDataVO-������ʱ����  VO��������ڷ�����������һ��Ԫ����by zhaoyha at 2009.8
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
      //ȡ��Ʊ�۸���Դ
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
   * ���� SimpBillRefListPanel ����ֵ��
   * 
   * @return SimpBillRefListPanel
   */
  /* ���棺�˷������������ɡ� */
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
    	      }, "cproviderid");// ���ݿ��̹�����IDȡ���̻�������ID
    	      ClientCacheHelper.getColValue(srcHeadVOs, new String[] {
    	        "cprovidername"
    	      }, "bd_cubasdoc", "pk_cubasdoc", new String[] {
    	        "pk_cubasdoc"
    	      }, "pk_cubasdoc");// ���ݿ��̻�������IDȡ��������
      super.setSourceVOToUI(srcHeadVOs, srcBodyVOs);
      // ���û�����
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
        //�ڳ�(4T)��ģ��nodecode(Ӧ����pub_systemplate�е�nodekey)      
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
      }, "cproviderid");// ���ݿ��̹�����IDȡ���̻�������ID
      ClientCacheHelper.getColValue(srcHeadVOs, new String[] {
        "cprovidername"
      }, "bd_cubasdoc", "pk_cubasdoc", new String[] {
        "pk_cubasdoc"
      }, "pk_cubasdoc");// ���ݿ��̻�������IDȡ��������
      super.setSourceVOToUI(srcHeadVOs, srcBodyVOs);

      // ����������
//      PoPublicUIClass.setFreeColValueForConvertBill(getBodyBillModel(), "vfree");
      // ���û�����
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
      // ���û�����
      PuTool.setBillModelConvertRate(getBodyBillModel(), new String[] {
          "cbaseid", "cassistunit", "ninnum", "nassistnum", "nexchangerate"
      });
      // ����������
      PoPublicUIClass.setFreeColValueForConvertBill(getBodyBillModel(), "vfree");
      // nc.ui.scm.sourcebill.SourceBillTool.loadSourceInfoAll(
      // getBodyBillModel(), sBilltype);
    }
    
    public String getRefNodeCode(){
      if("4T".equals(sBilltype))
        //�ڳ�(4T)��ģ��nodecode(Ӧ����pub_systemplate�е�nodekey)      
        return "4004050302Z0";
      else if("47".equals(sBilltype) || "45".equals(sBilltype))
        return "REFTEMPLET";
      else
        return null;
    }
  }

  /**
   * �ֵ���ť
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
          /** <needn't>��Ӱ������ */
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
    // �ֵ���ʽ
    String strDis = "��Ӧ��";
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
                                                                                                                     * "��ʾ"
                                                                                                                     */, nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPP40040401-000006")/*
                                                                                         * @res
                                                                                         * "ϵͳ���ϣ�ȡ������ⵥת��Ʊ�ķֵ���ʽ��Ĭ��Ϊ����Ӧ�̣�"
                                                                                         */);
      strDis = "��Ӧ��";
    }
    setDisType(strDis);
  }

  /**
   * ���ܣ��ֵ����� ������ ���أ� ���⣺ ���ڣ�(2004-3-16 8:59:25)
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
    // ���ֵ���ʽ�õ����з�Ʊ
    Hashtable stoTable = new Hashtable();
    Hashtable rowsTable = new Hashtable();
    HashMap hFlagMap = new HashMap();
    for (int i = 0; i < vos.length; i++) {
      GeneralBillHeaderVO headVO = (GeneralBillHeaderVO) vos[i].getParentVO();
      GeneralBillItemVO[] itemVOs = (GeneralBillItemVO[]) vos[i].getChildrenVO();
      Object s = headVO.getAttributeValue("pk_purcorp");
      s = itemVOs[0].getAttributeValue("pk_invoicecorp");

      // ��ȡ��Ӧ�̻���ID
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
       
        // ��ȡ��Ӧ�̻���ID
        //Ӧ�ð���Դ�����ķ�Ʊ������Ӧ�̣��ֵ�(��̨��ѯ��ⵥʱ�Ѿ�����������)
        //by zhaoyha at 2009.10.26(NCdp201040662)
        String pk_cubasdoc = itemVOs[j].getPk_cubasdoc();
//        Object oTemp = null;
//        try {
//          oTemp = CacheTool.getCellValue("bd_cumandoc", "pk_cumandoc", "pk_cubasdoc", itemVOs[j].getCvendorid());
//        }
//        catch (Exception e) {
//          SCMEnv.out("��ǰ̨�����ѯ��Ӧ�̻���ID�����ڷ�Ʊ�ֵ�����");
//          SCMEnv.out(e);
//        }
//        if (oTemp != null && ((Object[]) oTemp).length >= 1) {
//          if (((Object[]) oTemp)[0] != null && ((Object[]) oTemp)[0].toString().trim().length() > 0)
//            pk_cubasdoc = ((Object[]) oTemp)[0].toString();
//        }
        // ����Ӧ�̻���ID�ֵ�

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
        // ���ݷֵ���ʽ�õ�KEY����
        if (getDisType().trim().equals("���+��Ӧ��")) { // ��Ӧ��+���
          curKey += itemVOs[j].getCinvbasid()!=null? itemVOs[j].getCinvbasid():itemVOs[j].getCinventoryid();
        }
        else if (getDisType().trim().equals("����")) { // ����
          if (getM_OrderItemVOs() != null && getM_OrderItemVOs()[j] != null) {
            curKey += getM_OrderItemVOs()[j].getCorderid();
          }
          else {
            curKey += "NULL";
          }
        }
        else if (getDisType().trim().equals("��ⵥ")) { // ��ⵥ
          curKey += headVO.getPrimaryKey();
        }
        // ���뵽HASH����
        if (!stoTable.containsKey(curKey)) {
          Vector vec = new Vector();
          vec.addElement(headVO);
          vec.addElement(itemVOs[j]);
          stoTable.put(curKey, vec);
          // �б�
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
          // �б�
          Vector indexVEC = (Vector) rowsTable.get(curKey);
          indexVEC.addElement(new Integer(j));
          /*
           * ������ⵥ�����ɷ�Ʊʱ�����ѡ����ⵥ�Ĳ��š�ҵ��Ա������Э����ͬ����Я������Ʊ�ϣ�
           * �������ͬ����Ʊ�ϵĲ��š�ҵ��Ա������Э��Ϊ�գ����û��ֹ����롣
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
                                                                                                                     * "��ʾ"
                                                                                                                     */, nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPP40040401-000004")/*
                                                                                         * @res
                                                                                         * "����ѡ����Ҫ���ɷ�Ʊ����ⵥ��"
                                                                                         */);
      return null;
    }
    // �ӹ�ϣ����ȡ������VO
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

        // �б�
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
   * �˴����뷽��˵���� �������ڣ�(2001-9-17 13:54:00)
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
                                                                                                       * "��ѡ�񵥾�"
                                                                                                       */);
      return;
    }
    retBillVos = getDistributedVOs(retBillVos);
    ScmTimeLog timer =new ScmTimeLog();
    timer.start("��ʼ�Ӷ���VO���ɷ�ƱVO");
    retBillVos = generateInvoiceVOsNew(retBillVos);
    timer.showTime("��ɴӶ���VO���ɷ�ƱVO");
	//
	super.setRetsVos(retBillVos);
    //
    this.getAlignmentX();
    this.closeOK();
  }

  public AggregatedValueObject[] generateInvoiceVOsNew(AggregatedValueObject[] vos) {
    //Hashtable hFreeCust = new Hashtable();// key: ����ID, value: ɢ��ID
    //Map<String,String> bankInfo = new HashMap<String,String>();

    //�Ƿ���Ҫ���òɹ�Ĭ�ϸ���λ
    boolean isTransPUAssUnit = false;
    String para = null;
    try{
    	para = nc.ui.pub.para.SysInitBO_Client.getParaString(getPkCorp(),"PO08");
    }catch(BusinessException e){
    	SCMEnv.error(e);
    }
    if (para != null && "��".equals(para.trim())){
    	isTransPUAssUnit = true;
    }
    //Ч���Ż�����VO�������� by zhaoyha at 2009.8
    // /������ⵥ���ɵķ�Ʊ�еġ���Ӧ�̡�ȡ��ⵥ��Դ�����ġ���Ʊ����������Ʊ����Ϊ��ʱ��ȡ�����ϵġ���Ӧ�̡�
//    try {
//      for (int i = 0; i < vos.length; i++) {
//        GeneralBillItemVO[] itemVOs = (GeneralBillItemVO[]) vos[i].getChildrenVO();
//        if (itemVOs != null
//            && itemVOs.length > 0
//            && itemVOs[0].getCfirsttype() != null
//            && (itemVOs[0].getCfirsttype().equals(ScmConst.PO_Order) || itemVOs[0].getCfirsttype().equals(
//                ScmConst.SC_Order))) {// �����ⵥ��Դ�ڲɹ�����
//          String strOrderHid = itemVOs[0].getCfirstbillhid();// ����hid
//          if (strOrderHid != null && strOrderHid.trim().length() > 0) {
//            // ��ѯ�������ϵķ�Ʊ����vendormangid
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
//                  String strGivInvoice = oa2Ret[0][0].toString().trim();// �����ϵķ�Ʊ��
//                  if (strGivInvoice != null && strGivInvoice.trim().length() > 0) {
//                    ((GeneralBillHeaderVO) vos[i].getParentVO()).setCproviderid(strGivInvoice);
//                  }
//                }
//                else if (oa2Ret[0][1] != null) {// ����Ʊ����Ϊ��ʱ��ȡ�����ϵġ���Ӧ�̡�
//                  String strVendormangid = oa2Ret[0][1].toString().trim();//
//                  if (strVendormangid != null && strVendormangid.trim().length() > 0) {
//                    ((GeneralBillHeaderVO) vos[i].getParentVO()).setCproviderid(strVendormangid);
//                  }
//                }
//                
//                //�������� 
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
//      // MessageDialog.showHintDlg(this,"��ʾ","����ת��ʧ�ܣ�");
//      return null;
//    }
    // ����VOת��
    InvoiceVO[] retInvVOs = new InvoiceVO[vos.length];
    try {
      //Ч���Ż������ؽ�����һ��ΪInvoiceTempDataVO-������ʱ���� by zhaoyha at 2009.8
      AggregatedValueObject[] retVos = BusiBillManageTool.runChangeDataAry(vos, getBillType(), getCurrentBillType());
      for(int i=0;i<retInvVOs.length;++i)
        retInvVOs[i]=(InvoiceVO) retVos[i];
      //������Դ��ⵥ��ͷTS��Ϣ  by zhaoyha
      //PiTools.setUpSourceHTs(retInvVOs, getbillListPanel().getSelectedSourceVOs());
      if(retVos.length>retInvVOs.length)
        setTmpDataVo((InvoiceTempDataVO) retVos[retInvVOs.length]);

      // ��һЩ��������
      if (!processAfterChange((GeneralBillVO[]) vos, retInvVOs)) {
        MessageDialog.showWarningDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401",
            "UPPSCMCommon-000132")/* @res "����" */, nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401",
            "UPP40040401-000005")/* @res "���ڷ�Ʊ��������Դ����������������ͬ�ķ�Ʊ�У����飡" */);
        return null;
      }

    }
    catch (Exception e) {
      SCMEnv.out(e);
      MessageDialog.showHintDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPPSCMCommon-000270")/*
                                                                                                                     * @res
                                                                                                                     * "��ʾ"
                                                                                                                     */, e.getMessage());
      return null;
    }

    if (retInvVOs != null && retInvVOs.length > 0) {
      int retInvVOsLength = retInvVOs.length;
      Vector vTemp = new Vector();
      for (int i = 0; i < retInvVOsLength; i++) {
        // ����������
        InvoiceItemVO bodyVO[] = retInvVOs[i].getBodyVO();
        String corderid = null;
        for (int j = 0; j < bodyVO.length; j++) {
          if (bodyVO[j].getCorderid() != null) {
            corderid = bodyVO[j].getCorderid();
            break;
          }
        }
        //Ч���Ż�����VO�������� by zhaoyha at 2009.8
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
          //������ڳ���ⵥ����Ʊ����Ҫ����Ĭ�ϸ���λ modify by donggq 
          //by zhaoyha 2009.3.2��������NCdp200735769,45��ⵥҲҪת��,�����ڳ�
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

    // //���÷���ֵ    
    return retInvVOs;
  }

  private boolean processAfterChange(GeneralBillVO[] voaStock, InvoiceVO[] voaInvoice) throws Exception {
    if (voaInvoice == null)
      return true;
    try {
      // ��Դ����ⵥ, �Ӽ۸���㵥ȡ��
      //Ч���Ż�����VO�������� by zhaoyha at 2009.8
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
        // ��Դ
        voaInvoice[i].setSource(InvoiceVO.FROM_STO);
        // ɾ�����
        voaInvoice[i].getHeadVO().setDr(new Integer(0));
        // ��Ʊ����,��Ʊ����,��Ʊ���� �ڳ���־,�Ƶ���
        voaInvoice[i].getHeadVO().setDinvoicedate(ClientEnvironment.getInstance().getDate());
        voaInvoice[i].getHeadVO().setDarrivedate(ClientEnvironment.getInstance().getDate());
        voaInvoice[i].getHeadVO().setIbillstatus(new Integer(0));
        voaInvoice[i].getHeadVO().setFinitflag(new Integer(0));
        voaInvoice[i].getHeadVO().setIinvoicetype(new Integer(0));
        voaInvoice[i].getHeadVO().setIdiscounttaxtype(new Integer(1));
        // ������
        String strOperPk = nc.ui.pub.ClientEnvironment.getInstance().getUser().getPrimaryKey();
        voaInvoice[i].getHeadVO().setCoperator(strOperPk);
        // ����˰�ʡ�����
        voaInvoice[i].getHeadVO().setIdiscounttaxtype(new Integer(0));
        voaInvoice[i].getHeadVO().setNtaxrate(null);

        if (voaInvoice[i].getHeadVO().getCvendormangid() != null
            && voaInvoice[i].getHeadVO().getCvendorbaseid() == null) {
          // ��ȡ��Ӧ�̻���ID
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

        // �ɹ���˾
        if (voaInvoice[i].getHeadVO().getPk_purcorp() == null)
          voaInvoice[i].getHeadVO().setPk_purcorp(ClientEnvironment.getInstance().getCorporation().getPrimaryKey());

        InvoiceItemVO[] voaInvItem = voaInvoice[i].getBodyVO();
        HashMap hData = new HashMap();
        for(int j = 0; j < voaInvItem.length; j++){
        	hData.put(voaInvItem[j].getCbaseid(),"");
        }
        PuTool.loadBatchTaxrate((String[]) hData.keySet().toArray(new String[0]), voaInvItem[0].getPk_corp());
        for (int j = 0; j < voaInvItem.length; j++) {
          // ��Ӧ���к�
          int index = getDistributedRows()[i][j].intValue();
          // ��Ʊ����ͬ��������
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
          // //����۸���㵥����,�����¼���
          // getbillListPanel().getHeadBillModel().setValueAt(tHQHP.get(voaInvItem[j].getCupsourcebillrowid()),
          // j, "norgnettaxprice");
          // BillEditEvent event = new
          // BillEditEvent(getbillListPanel().getHeadTable(),tHQHP.get(voaInvItem[j].getCupsourcebillrowid()),"norgnettaxprice",j,1);//ԭ�Ҿ���˰����
          // afterEditRelations(event);
          // }
          // �����˰���˰�ϼ�
          // voaInvItem[j].setNoriginalcurmny(voaInvItem[j].getNinvoicenum().multiply(voaInvItem[j].getNoriginalnetprice()));
          // voaInvItem[j].setNoriginalsummny(voaInvItem[j].getNinvoicenum().multiply(voaInvItem[j].getNorgnettaxprice()));
          // voaInvItem[j].setNoriginaltaxmny(voaInvItem[j].getNoriginalsummny().sub(voaInvItem[j].getNoriginalcurmny()));
          // ��ο�Ʊ����
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
          // max=MAX(�ѿ�Ʊ����-�ۼ��������,�ѽ�������)
          double max = 0.0;
          if (dTemp > dTemp1) {
            max = dTemp;
          }
          else {
            max = dTemp1;
          }
          //��Դͷ�������÷�Ʊ�е�һЩ��Ϣ
          setInfoFromOrder(voaInvItem[j]);
          UFDouble dInvNum = PuPubVO.getUFDouble_NullAsZero(voaStock[i].getItemVOs()[j].getNinnum()).sub(max);

          voaInvItem[j].setNinvoicenum(dInvNum);
          //�н��㵥��Ҫ�������ż۵ĵ���
          //�뷢Ʊά������������ż۰�ť��һ�� by zhaoyha at 2009.6.16
          if(cCurrencyID.equals(voaInvItem[j].getCcurrencytypeid()) && //Ҫ�����һ��
              PuPubVO.getUFDouble_ZeroAsNull(tHQHP.get(voaInvItem[j].getCupsourcebillrowid())) != null){
      		voaInvItem[j].setNorgnettaxprice((UFDouble)tHQHP.get(voaInvItem[j].getCupsourcebillrowid()));
      		calculate(voaInvItem[j], "norgnettaxprice");
      	  }else
          // ���÷�Ʊ���ۣ������оʹӶ���ȡ������û�оʹ���ⵥȡ
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
              //�����ⵥ�����ȣ�������Ϊ��ⵥ��
            	voaInvItem[j].setCcurrencytypeid(body.getCcurrencytypeid());
              if(isStorPre() && PuPubVO.getUFDouble_ZeroAsNull(voaStock[i].getItemVOs()[j].getNprice()) != null){
            	  voaInvItem[j].setNoriginalcurprice(voaStock[i].getItemVOs()[j].getNprice());
            	  voaInvItem[j].setNoriginalcurmny(voaStock[i].getItemVOs()[j].getNmny());
            	  exchangeStockToInvoicePrice(voaInvItem[j], voaStock[i].getPk_corp());
//            	  voaInvItem[j].setNorgnettaxprice(voaStock[i].getItemVOs()[j].getNtaxprice());
              }else{
            	  voaInvItem[j].setNoriginalcurprice(body.getNoriginalnetprice());// ��ƱҪȡ������˰����
            	  voaInvItem[j].setNorgnettaxprice(body.getNorgnettaxprice());
              }
              // voaInvItem[j].setNoriginalcurprice(m_OrderItemVOs[j].getNoriginalcurprice());
              // ����β�һ������ο�Ʊ���߶�����һ�ο�Ʊ���ȡ��
              // �ȽϷ�Ʊ�к���ⵥ������
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
                // �ȽϷ�Ʊ�кͶ�Ӧ����������
            	if (voaInvItem[j].getNinvoicenum().compareTo(body.getNordernum()) == 0
            			&& !(isStorPre() && PuPubVO.getUFDouble_ZeroAsNull(voaStock[i].getItemVOs()[j].getNprice()) != null)) {
                  voaInvItem[j].setNoriginalcurmny(body.getNoriginalcurmny());
                  voaInvItem[j].setNtaxrate(body.getNtaxrate());
                  voaInvItem[j].setNoriginaltaxmny(body.getNoriginaltaxmny());
                  voaInvItem[j].setNoriginalsummny(body.getNoriginaltaxpricemny());
                }
                else {
                  voaInvItem[j].setNtaxrate(body.getNtaxrate());// ˰��
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

          // ������ⵥ���ɲɹ���ƱʱӦ�������֡�˰��
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

          // //��Ʊ����
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
          // //��˰���
          // Object ob = getbillListPanel().getHeadBillModel().getValueAt(index,
          // "idiscounttaxtype");
          // if (ob != null) {
          // voaInvItem[j].setIdiscounttaxtype((Integer)
          // getbillListPanel().getBodyItem("idiscounttaxtype").converType(ob));
          // } else {
          // voaInvItem[j].setIdiscounttaxtype(new Integer(1));
          // }

          // ɾ�����
          voaInvItem[j].setDr(new Integer(0));
          // �۱����۸�����
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
          // ���ڶ���ɹ���ⵥ����ͬһ��Ʊ�ֿ���Ϣ��ʧ�Ĵ���
          if (sCupsourceBillId != null && sCupsourceBillId.length() > 0
              && voaStock[i].getHeaderVO().getPk_corp().equals(voaInvoice[i].getHeadVO().getPk_corp())) {
            String sWareHouseId = (String) hWareHouseId.get(sCupsourceBillId);
            if (sWareHouseId != null && sWareHouseId.trim().length() > 0)
              voaInvItem[j].setCwarehouseid(sWareHouseId);
          }
          else {
            voaInvItem[j].setCwarehouseid(null);
          }

          // ����Դͷ��Ϣ(������ⵥ)
          if (sCupsourceBillId != null && sCupsourceBillId.length() > 0
              && (voaInvItem[j].getCsourcebillid() == null || voaInvItem[j].getCsourcebillid().length() == 0)) {
            voaInvItem[j].setCsourcebillid(sCupsourceBillId);
            voaInvItem[j].setCsourcebillrowid(voaInvItem[j].getCupsourcebillrowid());
            voaInvItem[j].setCsourcebilltype(voaInvItem[j].getCupsourcebilltype());
          }
        }
      }

      // ��Ʊ��˾���ջ���˾��ͬ,�������ID��Ҫת��Ϊ��Ʊ��˾����ID
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

      // ��Ʊ��˾�Ͳɹ���˾��ͬ,��Ӧ�̹���ID��Ҫת��Ϊ��Ʊ��˾����ID
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
        //Ч���Ż�����VO�������� by zhaoyha at 2009.8
        Map<String, InvoiceItemVO[]> hData = getTmpDataVo().getSrcOrderPreGenInvInfo(); 
          //InvoiceHelper.queryInvoiceBodysStore(strGeneralbid);

        for (int i = 0; i < voaStock.length; i++) {
          // ��ⵥ����
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
            	// �����Ʊ�۸�ͬ�ڶ����۸��򲻽����ݲ��
            	if(voaInvoice[i].getBodyVO()[j].getNoriginalcurprice().compareTo(orderItemVOtemp.getNoriginalnetprice()) != 0){
            		voaBVOs[j] = null;
            	}else{
            		voaBVOs[j] = orderItemVOtemp;
            	}
            }
            else {
//            	OrderItemVO orderItemVOtemp =  (OrderItemVO) hOrderData.get(((GeneralBillItemVO) voaStock[i].getChildrenVO()[j])
//                        .getCfirstbillbid());
//            	// �����Ʊ�۸�ͬ�ڶ����۸��򲻽����ݲ��
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
      // "��ʾ"*/, strErrMsg);
      throw e;
    }
    return true;

  }

  /**
   * 
   * �����������������÷�Ʊ�еĶ�����Ϣ��
   * <p>
   * <b>examples:</b>
   * <p>
   * ʹ��ʾ��
   * <p>
   * <b>����˵��</b>
   * @param invcItem
   * <p>
   * @author zhaoyha
   * @time 2009-6-16 ����02:06:09
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
//      // ���
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
//      // Ӧ˰�ں�
//      if (strDisTaxName == 0) {
//        if (dTaxrate.doubleValue() == 100.0) {
//          dTax = dMoney;
//          dMoney = new UFDouble(0.0);
//          voaBody.setNoriginalnetprice(dMoney);
//        }
//        else {
//          dTax = dMoney.multiply(dTaxrate).div(new UFDouble(1.0).sub(dTaxrate));
//        }
//        // Ӧ˰���
//      }
//      else if (strDisTaxName == 1) {
//        dTax = dMoney.multiply(dTaxrate);
//        // ����˰
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
//      // �۶��ϼ�
//      UFDouble uNoriginaltaxpricemny = voaBody.getNinvoicenum().multiply(dNorgnettaxprice);
//      UFDouble dTaxrate = voaBody.getNtaxrate();
//      if (dTaxrate == null) {
//        dTaxrate = nc.vo.scm.pu.VariableConst.ZERO;
//      }
//      dTaxrate = dTaxrate.div(100.0);
//      int strDisTaxName = voaBody.getIdiscounttaxtype();
//      UFDouble dTax = null;
//      // Ӧ˰�ں�
//      if (strDisTaxName == 1) {
//        dTax = uNoriginaltaxpricemny.multiply(dTaxrate.div(new UFDouble(1.0).add(dTaxrate)));
//        // Ӧ˰���
//      }
//      else if (strDisTaxName == 0) {
//        dTax = uNoriginaltaxpricemny.multiply(dTaxrate);
//        // ����˰
//      }
//      else if (strDisTaxName == 2) {
//        dTax = nc.vo.scm.pu.VariableConst.ZERO;
//      }
//      else {
//        dTax = nc.vo.scm.pu.VariableConst.ZERO;
//      }
//      voaBody.setNoriginalsummny(uNoriginaltaxpricemny);
//      voaBody.setNoriginaltaxmny(dTax);
//      // ���
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
      // ����Ȩ�޿���
      ConditionVO tempVO = new ConditionVO();
      tempVO.setFieldName("����Ա");
      tempVO.setFieldCode("");
      tempVO.setOperaCode("");
      tempVO.setValue(nc.ui.pub.ClientEnvironment.getInstance().getUser().getPrimaryKey());
      v.addElement(tempVO);
      tempVO = new ConditionVO();
      tempVO.setFieldName("��˾");
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

      // �����׷�ӻ���
      if (invoiceVO != null && invoiceVO.length > 0) {
        Set<String> setGeneralBillItemID = new HashSet<String>();
        // ͳ�Ʒ�Ʊ���������е���ⵥ��ID:nc.vo.pi.InvoiceItemVO.m_cupsourcebillrowid
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
        // ѭ�����������ⵥ���뷢Ʊ�����ϵĵ��ݱȽϣ����еĲ���ʾ
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
        // û��׷�ӻ���
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
      // ����������
      
      (new FreeVOParse()).setFreeVO(itemVOsTForPo, "vfree0", "vfree","freevo",
				"pk_invbasdoc", "cinventoryid", true);
      
//      +
      result[0] = headVOsForPo;
      result[1] = itemVOsTForPo;
    }
    catch (Exception e) {
      nc.vo.scm.pub.SCMEnv.out("���ݼ���ʧ�ܣ�");
      nc.vo.scm.pub.SCMEnv.out(e);
      showErroMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000255")/*
                                                                                                       * @res
                                                                                                       * "���ݼ���ʧ��"
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
      nc.vo.scm.pub.SCMEnv.out("���ݼ���ʧ�ܣ�");
      nc.vo.scm.pub.SCMEnv.out(e);
      showErroMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000255")/*
                                                                                                       * @res
                                                                                                       * "���ݼ���ʧ��"
                                                                                                       */);
      return;
    }
    isUpdateUIDataWhenSwitch = true;
  }

  public QueryConditionClient QueryConditionClient() {
    return ((BillReferQueryProxyStoreToPi) super.getQueyDlg()).createOldQryDlg();
  }

  private NormalCondVO[] getNormalCondVOs() {
    // ��Ʊ���ڣ���Ӧ�̣���������ţ�ҵ��Ա,������࣬��������
    // �Ƿ��ڳ����Ƿ��������Ƿ����

    // ��ѯ����VO
    NormalCondVO[] vos = new NormalCondVO[3];
    vos[0] = new NormalCondVO("��˾", getPkCorp());
    vos[1] = new NormalCondVO("ҵ������", sBusinessType);
    vos[2] = new NormalCondVO("��������", sBilltype);

    return vos;
  }

  public nc.vo.pi.InvoiceVO[] getInvoiceVO() {
    return invoiceVO;
  }
  /**
   * 
   * ����PO83�У��Ƿ���ⵥ�۸������ڶ����۸�
   * <p>
   * <b>examples:</b>
   * <p>
   * ʹ��ʾ��
   * <p>
   * <b>����˵��</b>
   * @return ��ⵥ�����ڶ��� true ��������������ⵥ  false
   * <p>
   * @author donggq
   * @time 2008-11-10 ����10:24:54
   */
  public boolean isStorPre(){
	  if (pareVO == null)
		  return false;
	  String initPare = pareVO.getValue();
	  // "8" ��ⵥ��
	  // "6" ������
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
   * ���¼��㣬���ù�Ӧ����������
   * <p>
   * <b>examples:</b>
   * <p>
   * ʹ��ʾ��
   * <p>
   * <b>����˵��</b>
   * @param bodyVO
   * @param changedKey
   * <p>
   * @author donggq
   * @time 2008-11-19 ����02:32:26
   */
  private void calculate(InvoiceItemVO bodyVO,String changedKey){
	    // ��˰��� 0Ӧ˰�ں� 1Ӧ˰��� 2����˰
	    String s = "Ӧ˰�ں�";

	    if (bodyVO != null ) {
	      if (bodyVO.getIdiscounttaxtype().intValue() == 1)
	        s = "Ӧ˰���";
	      else if (bodyVO.getIdiscounttaxtype().intValue() == 2)
	        s = "����˰";
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
	            PuTool.getPricePriorPolicy(bodyVO.getPk_corp()), 7//�����ۿ�
	    }, changedKey, descriptions, keys);
  }
  /**
   * �Ƿ���ʾĿ�굥�ݲ������.
   * ��������:(2001-3-23 2:02:27)
   * @param e ufbill.BillEditEvent
   */
  protected boolean isShowInvHandPane(){
    return false;
  }
  
  /**
   * 
   * ������������������ⵥ��(�Ѿ����õ���Ʊ����˰ԭ�Ҽ���)ת��Ϊ��Ʊ���ּ۸�
   * <p>
   * <b>examples:</b>
   * <p>
   * ʹ��ʾ��
   * <p>
   * <b>����˵��</b>
   * @param invItemVo �Ѿ�����δת��ǰ����ⵥ�۵ķ�ƱVO
   * @param stockPk_corp ��ⵥ���ڹ�˾
   * @throws BusinessException
   * <p>
   * @author zhaoyha
   * @time 2009-2-11 ����11:38:44
   */
  public void exchangeStockToInvoicePrice(InvoiceItemVO invItemVo,String stockPk_corp) throws BusinessException{
    String currentPk_corp=invItemVo.getPk_corp();
    //ȡ����:��Ʊ����---->��ⵥ��˾��λ��
    UFDouble exchgRate = new CurrencyRateUtil(currentPk_corp)
      .getRate(invItemVo.getCcurrencytypeid()==null?CurrParamQuery.getInstance().getLocalCurrPK(invItemVo.getPk_corp()):invItemVo.getCcurrencytypeid(),CurrParamQuery.getInstance().getLocalCurrPK(stockPk_corp)
          ,nc.ui.po.pub.PoPublicUIClass.getLoginDate().toString());
    //�ӱ�����ԭ������,Ҫ�������ȴ���
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
    //������ת����ɵļ۸�
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
   * @param tmpDataVo Ҫ���õ� tmpDataVo
   */
  public void setTmpDataVo(InvoiceTempDataVO tmpDataVo) {
    this.tmpDataVo = tmpDataVo;
  }

}
