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
 * �����ߣ�������
 * �������ڣ�2001-04-20

 * ���ܣ���̬ת���� ����


 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
 */
public class ClientUI extends SpecialBillBaseUI {
 
	
	//2010-11-08 MeiChao ���--����¼�밴ť.Begin
	
	private UFDouble number = null;//����ҳǩ�µ�����

	
	//��ʽ������������ add by ������ 2010-10-15
	private UFDouble pmny = null;
	
	private ButtonObject expenseInput;
	private ButtonObject getButtonExpenseInput(){
		if(this.expenseInput==null){
			this.expenseInput=new ButtonObject("����¼��","����¼��","����¼��");
		}
		return this.expenseInput;
	}
	//2010-11-08 MeiChao ��� End

	
	
	
	
  public final ICComboxItem[] comInvsetparttype = new ICComboxItem[]{
    new ICComboxItem(new Integer(BillRowType.beforeConvert),nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec","UPP4008spec-000142")),
    new ICComboxItem(new Integer(BillRowType.afterConvert),nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec","UPP4008spec-000143"))
  };


/**
 * ClientUI ������ע�⡣
 */
public ClientUI() {
  super();
  initialize();
}

/**
  * �����ߣ�������
  * ���ܣ������������޸�ʱ
  * ������e���ݱ༭�¼�
  * ���أ�
  * ���⣺
  * ���ڣ�(2001-5-8 19:08:05)
  * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
  */
public void afterEdit(nc.ui.pub.bill.BillEditEvent e) {

  //�Զ�����VO������
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
   * ��ʼ���ࡣ
   */
/* ���棺�˷������������ɡ� */
protected void initialize() {
	
	//�ڸ����ʼ������ִ��ǰ,������¼�밴ť��ӵ���ť����.
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
 * ��ʼ���ࡣ
 */
/* ���棺�˷������������ɡ� */
protected void initOther() {
  GenMethod.initComboBox(getBillCardPanel().getBodyItem("invsetparttype"), this.comInvsetparttype, false);
  GenMethod.initComboBox(getBillListPanel().getBodyItem("invsetparttype"), this.comInvsetparttype, false);
}




/**
 * onButtonClicked ����ע�⡣
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
  //2010-11-08 MeiChao begin ��ӶԷ���¼�밴ť���¼���Ӧ������
  else if (bo == this.getButtonExpenseInput())
    this.onBoExpenseInput();
  //2010-11-08 MeiChao end ��ӶԷ���¼�밴ť���¼���Ӧ������
  else
    super.onButtonClicked(bo);

}

/** ���ñ���ť״̬
  * �˴����뷽��˵����
  * �������ڣ�(2001-4-30 13:58:35)
  */
protected void setButtonState() {
	//2010-11-08 MeiChao ���÷���¼�밴ť��״̬
	this.setExpenseInputButtonStat(m_iMode);
	
	
  switch (m_iMode) {
    case BillMode.New : //����

      //showHintMessage("����  ����Ctrl����ѡ��������ݽ���ɾ��...");
      showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON","UPPSCMCommon-000287")/*@res "����"*/);

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
    case BillMode.Update : //�޸�

      showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UC001-0000045")/*@res "�޸�"*/);

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
    case BillMode.Browse : //���

      showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UC001-0000021")/*@res "���"*/);

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
    case BillMode.List : //�б�״̬

    	//modified by liuzy 2008-05-20 û��ʵ�ֵĹ��ܸ�����ʾ���û���ע��֮��
//      showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec","UPP4008spec-000130")/*@res "�б�  ����Ctrl����ѡ����ŵ��ݽ���ɾ��..."*/);

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
      ////��ֹ�޸ĺ�ɾ��
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
  //ʹ������Ч
  if (m_aryButtonGroup != null) {
    updateButtons();
  }
}


/**
   * ����
   * �������ڣ�(2001-4-18 19:45:17)
   */
public void onAdd() { //finished
  super.onAdd();
  initShowStatus();
}

  /**
  * ����
  * �������ڣ�(2001-4-18 19:46:27)
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
   * �����ߣ�yb
   * ���ܣ�������
   * ������
   * ���أ�
   * ���⣺
   * ���ڣ�(2001-5-16 ���� 5:58)
   * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
   */
  public void onInsertRow() { 
   super.onInsertRow();
   int irow = getBillCardPanel().getBillTable().getSelectedRow();
   int iStatus=BillRowType.afterConvert;
   
   setShowStatus(irow,iStatus);
  }





/**
  * �����ߣ�������
  * ���ܣ�������
  * ������
  * ���أ�
  * ���⣺
  * ���ڣ�(2001-5-10 ���� 2:50)
  * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
  */
public void onCopyRow() { //finished
//  int[] iaRow = getBillCardPanel().getBillTable().getSelectedRows();
//  for (int i = 0; i < iaRow.length; i++) {
//    if (iaRow[i] == 0) {
//      showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec","UPP4008spec-000131")/*@res "��һ���ǡ�ת��ǰ�����������ơ�"*/);
//      return;
//    }
//  }

  super.onCopyRow();

}



  /**
    * ɾ��
    * �������ڣ�(2001-4-18 19:46:41)
    */
  public void onDeleteRow() {
//    if(getBillCardPanel().getBillTable().getSelectedRow()==0
//      ||getBillCardPanel().getBillTable().getSelectedRow()==1){
//      showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec","UPP4008spec-000132")/*@res "���в���ɾ����"*/);
//      return;
//
//    }
    getBillCardPanel().delLine();


  }




/**
  * �����ߣ�������
  * ���ܣ�ճ����
  * ������
  * ���أ�
  * ���⣺
  * ���ڣ�(2001-5-10 ���� 4:15)
  * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
  */
public void onPasteRow() {
  //finished added by zhx bill row no
//  int iRowCount = getBillCardPanel().getBodyPanel().getTableModel().getRowCount();
//  int row = getBillCardPanel().getBillTable().getSelectedRow();
//  if (row <= 0) {
//    showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec","UPP4008spec-000133")/*@res "�����ڴ���ճ������ѡ��������!"*/);
//  } else {
//    super.onPasteRow();
//  }
  super.onPasteRow();
}





/**
  * ����
  * �������ڣ�(2001-4-18 19:47:08)
  */
public boolean onSave() {
  try {
    if (m_iLastSelListHeadRow < 0) { //δѡ���κα�ֱ������ʱ�����߲�ѯ���Ϊ��ʱ���ᷢ��
      m_iLastSelListHeadRow = 0; //���ѡ�е��б��ͷ��
      m_iTotalListHeadNum = 0; //�б��ͷĿǰ���ڵ�����
    }

    /**
     * 2010-11-08 MeiChao
     * ��ȡ��Ҫ����ķ�����ϢVO
     */
    //��ȡ��Ҫ����ķ�����Ϣ
    InformationCostVO[] expenseVOs=(InformationCostVO[])this.getBillCardPanel().getBillModel("jj_scm_informationcost").getBodyValueVOs(InformationCostVO.class.getName());
    
    
    //�������������
    getBillCardPanel().tableStopCellEditing();
    getBillCardPanel().stopEditing();

    //�˵����еĿ���
    filterNullLine();
    if (getBillCardPanel().getRowCount() == 0) {
      showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec","UPP4008spec-000134")/*@res "�����ݣ�����������..."*/);
      initShowStatus();
      return false;
    }
    //added by zhx 030626 ����кŵĺϷ���; �÷���Ӧ���ڹ��˿��еĺ��档
    if (!nc
      .ui
      .scm
      .pub
      .report
      .BillRowNo
      .verifyRowNosCorrect(getBillCardPanel(), m_sBillRowNo)) {
      return false;
    }

    //����̬ת������ɾ���ڶ������¸���
    if (getBillCardPanel().getRowCount() < 2) {
      showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec","UPP4008spec-000135")/*@res "�����в���ȷ������������..."*/);
      return false;

    }

    ////���뵥�ݺ�
    //if (BillMode.New == m_iMode) {
      //if (!nc
        //.ui
        //.ic
        //.pub
        //.exp
        //.GeneralMethod
        //.setBillCode(m_voBill, m_sBillTypeCode, getBillCardPanel())) {
        //nc.ui.pub.beans.MessageDialog.showErrorDlg(this, "����", "��õ��ݺ�ʧ�ܣ�");
        //return;
      //}
    //}

    SpecialBillVO voTempBill = getBillVO();
    //ͬ�����VO
    //voTempBill--->>>m_voBill
    m_voBill.setIDItems(voTempBill);
    // �޸��ˣ������� ����02:01:32_2009-10-15 �޸�ԭ��:�����滺��VO�ĵ�������
    m_voBill.getParentVO().setAttributeValue("cbilltypecode", m_sBillTypeCode);
    //VOУ��
    if (!checkVO()) {
      return false;
    }
    String sHPK = null; //bill pk
    int iRowCount = getBillCardPanel().getRowCount();
    SpecialBillItemVO[] voaMyTempItem = null;
	// �޸Ļ��߱���󷵻ص�СVO
	SMSpecialBillVO voSM = null;
    if (BillMode.New == m_iMode) {
      //����
      //��m_shvoBillSpecialHVO��д��FreeVO�����ӵ�ֵ
      voTempBill.setChildrenVO((SpecialBillItemVO[]) getChangedItemVOs());
      //д��HVO��BillTypeCode������
      voTempBill.getParentVO().setAttributeValue("cbilltypecode", m_sBillTypeCode);
   
      //�õ���ǰ��ItemVO
      voaMyTempItem = (SpecialBillItemVO[]) voTempBill.getChildrenVO();
      //��ʱHVO[]

      //д��,������PKs
      //���õ����к�zhx 0630:
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
      voTempBill.getHeaderVO().setCoperatoridnow(m_sUserID);//��ǰ����Ա
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
      } //��ʾ��ʾ��Ϣ
      if (alsPrimaryKey.get(0) != null)
        showErrorMessage((String) alsPrimaryKey.get(0));
      ArrayList alMyPK = (ArrayList) alsPrimaryKey.get(1);
      sHPK = alMyPK.get(0).toString();
		voSM = (SMSpecialBillVO) alsPrimaryKey
		.get(alsPrimaryKey.size() - 1);
  /*    //д��HHeaderVO��PK
      voTempBill.getParentVO().setPrimaryKey(sHPK);
      //m_voBill.getParentVO().setPrimaryKey(sHPK);
      //д��HItemVO��PK���Ӧ��ͷPK
      for (int i = 0; i < voaMyTempItem.length; i++) {
        voaMyTempItem[i].setPrimaryKey(alMyPK.get(i + 1).toString());
        voaMyTempItem[i].setAttributeValue("cspecialhid", sHPK);
      }
      voTempBill.setChildrenVO(voaMyTempItem);
      nc.ui.ic.pub.tools.GenMethod.fillVOValuesBy(new SpecialBillHeaderVO[]{voTempBill.getHeaderVO()},
      		new String[]{"tmaketime","tlastmoditime"}, new int[]{SmartFieldMeta.JAVATYPE_STRING,SmartFieldMeta.JAVATYPE_STRING},
      		IItemKey.cspecialhid, IItemKey.ic_special_h,
      		new String[]{IItemKey.tmaketime, IItemKey.tlastmoditime}, IItemKey.cspecialhid, null);
      //ͬ�����VO
      m_voBill.setIDItems(voTempBill);*/
      //����HVO
      m_iLastSelListHeadRow = m_iTotalListHeadNum;
      addBillVO();
      /**
       * 2010-11-08 MeiChao
       * �������������,�������ݿ��в����·�����Ϣ.
       */
      //2010-11-08 MeiChao begin
      //������д�������ϢVO��
				if (expenseVOs != null && expenseVOs.length > 0) {
					for (int i = 0; i < expenseVOs.length; i++) {
						expenseVOs[i].setCbillid(sHPK);//����������������
						expenseVOs[i].setVdef10("4N");//����������������
					}
				}
      JJIcScmPubHelper expenseManager=new JJIcScmPubHelper();
      expenseManager.insertSmartVOs(expenseVOs);
      //2010-11-08 MeiChao end
      
      
    } else if (BillMode.Update == m_iMode) { //�޸�
      //�ӽ����л����Ҫ������
      voTempBill = getUpdatedBillVO();
      if (null == voTempBill) {
        return false;
      } //��m_shvoBillSpecialHVO��д��FreeVO�����ӵ�ֵ
      voTempBill.setChildrenVO((SpecialBillItemVO[]) getChangedItemVOs());
      //�õ���ǰ��ItemVO
      voaMyTempItem = (SpecialBillItemVO[]) voTempBill.getChildrenVO();
      //д��,������PKs
      //���õ����к�zhx 0630:
      if (iRowCount > 0 && voTempBill.getChildrenVO() != null) {
        if (getBillCardPanel().getBodyItem(m_sBillRowNo) != null)
          for (int i = 0; i < iRowCount; i++) {
            voTempBill.setItemValue(
              i,
              m_sBillRowNo,
              getBillCardPanel().getBodyValueAt(i, m_sBillRowNo));

          }
      }
      voTempBill.getHeaderVO().setCoperatoridnow(m_sUserID);//��ǰ����Ա
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
      } //��ʾ��ʾ��Ϣ
      if (alsPrimaryKey.get(0) != null)
        showErrorMessage((String) alsPrimaryKey.get(0));
      ArrayList alMyPK = (ArrayList) alsPrimaryKey.get(1);
      sHPK = voTempBill.getParentVO().getPrimaryKey();
		voSM = (SMSpecialBillVO) alsPrimaryKey
		.get(alsPrimaryKey.size() - 1);
      //д��HItemVO��PK���Ӧ��ͷPK,ɾȥ�����ItemVO
      ArrayList alItemVO = new ArrayList();
      //����ɱ�����û�иı��ItemVO
      //��Ӧƽ̨���޸�,���Ƿ��ص�һ���Ǳ�ͷ��PK   2001/09/26
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
              SCMEnv.out("����ʱ�����ж�Ӧ�������������Ա���...");
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
      //ͬ�����VO
      m_voBill.setIDItems(voTempBill);
      //�޸�HVO
      m_alListData.set(m_iLastSelListHeadRow, m_voBill.clone());*/
      /**
       * 2010-11-08 MeiChao
       * ������޸ĵ���,��ô�޸ķ�����Ϣ
       */
      //2010-11-08 MeiChao ���·�����Ϣ�������ϵ���id
      for (int i = 0; i < expenseVOs.length; i++) {
					if (expenseVOs[i].getCbillid() == null) {
						expenseVOs[i].setCbillid(voTempBill.getHeaderVO()
								.getPrimaryKey());//������������id
						expenseVOs[i].setVdef10("4N");//����������������
					}
				}
      JJIcScmPubHelper expenseManager=new JJIcScmPubHelper();
      expenseManager.updateSmartVOs(expenseVOs, voTempBill.getHeaderVO()
								.getPrimaryKey());
      
    }
    //set ui pk below,so put it before freshts.
    //switchListToBill();
    //fresh timestamp
	// ����̨��Ϣ���µ�����
	freshVOBySmallVO(voSM);
/*    if (sHPK != null) {
      ArrayList alLastTs = qryLastTs(sHPK);
      freshTs(alLastTs);
    }*/
    //����HVO
    m_iMode = BillMode.Browse;
    m_iFirstSelListHeadRow = -1;
    setButtonState();
    setBillState();
    /**
     * 2010-11-08 MeiChao
     * ������VO��Ϣ��д��������(��ʱ����Ҫ)
     */
    showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec","UPP4008spec-000136")/*@res "����ɹ���"*/);
    return true;
  } catch (Exception e) {
    showErrorMessage(e.getMessage());
    showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec","UPP4008spec-000137")/*@res "δ����ɣ����������ԣ�"*/);
    handleException(e);
    return false;
  }
}

/**
   * �����ߣ�������
   * ���ܣ���ʼ����ť
   * ������
   * ���أ�
   * ���⣺
   * ���ڣ�(2001-5-15 ���� 3:12)
   * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
   */
protected void initButtons() {
  m_boAdd = new ButtonObject(nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UC001-0000002")/*@res "����"*/, nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec","UPPSCMCommon-000308")/*@res "���ӵ���"*/, 0,"����"); /*-=notranslate=-*/
  m_boChange = new ButtonObject(nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UC001-0000045")/*@res "�޸�"*/, nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec","UPPSCMCommon-000291")/*@res "�޸ĵ���"*/, 0,"�޸�");  /*-=notranslate=-*/
  m_boDelete = new ButtonObject(nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UC001-0000039")/*@res "ɾ��"*/, nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec","UPP4008spec-000504")/*@res "ɾ������"*/, 0,"ɾ��"); /*-=notranslate=-*/
  m_boCopyBill = new ButtonObject(nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UC001-0000043")/*@res "����"*/, nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec","UPP4008spec-000505")/*@res "���Ƶ���"*/, 0,"����"); /*-=notranslate=-*/
  m_boSave = new ButtonObject(nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UC001-0000001")/*@res "����"*/, nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UC001-0000001")/*@res "����"*/, 0,"����");  /*-=notranslate=-*/
  m_boCancel = new ButtonObject(nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UC001-0000008")/*@res "ȡ��"*/, nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UC001-0000008")/*@res "ȡ��"*/, 0,"ȡ��");  /*-=notranslate=-*/

  m_boAddRow = new ButtonObject(nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UC001-0000012")/*@res "����"*/, nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UC001-0000012")/*@res "����"*/, 0,"����");  /*-=notranslate=-*/
  m_boDeleteRow = new ButtonObject(nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UC001-0000013")/*@res "ɾ��"*/, nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UC001-0000013")/*@res "ɾ��"*/, 0,"ɾ��"); /*-=notranslate=-*/
  m_boInsertRow = new ButtonObject(nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UC001-0000016")/*@res "������"*/, nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UC001-0000016")/*@res "������"*/, 0,"������");  /*-=notranslate=-*/
  m_boCopyRow = new ButtonObject(nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UC001-0000014")/*@res "������"*/, nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UC001-0000014")/*@res "������"*/, 0,"������");  /*-=notranslate=-*/
  m_boPasteRow = new ButtonObject(nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UC001-0000015")/*@res "ճ����"*/, nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UC001-0000015")/*@res "ճ����"*/, 0,"ճ����"); /*-=notranslate=-*/
	m_boPasteRowTail = new ButtonObject(nc.ui.ml.NCLangRes.getInstance()
			.getStrByID("4008bill", "UPP4008bill-000556")/* @res "ճ���е���β" */,
			nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill",
					"UPP4008bill-000556")/* @res "ճ���е���β" */, 0, "ճ���е���β"); /*-=notranslate=-*/
	m_boNewRowNo = new ButtonObject(nc.ui.ml.NCLangRes.getInstance()
			.getStrByID("4008bill", "UPP4008bill-000551")/* @res "�����к�" */,
			nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill",
					"UPP4008bill-000551")/* @res "�����к�" */, 0, "�����к�"); /*-=notranslate=-*/
	
  m_boAuditBill = new ButtonObject(nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UC001-0000027")/*@res "����"*/, nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UC001-0000027")/*@res "����"*/, 0,"����"); /*-=notranslate=-*/
  m_boCancelAudit = new ButtonObject(nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UC001-0000028")/*@res "����"*/, nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UC001-0000028")/*@res "����"*/, 0,"����"); /*-=notranslate=-*/
  m_boQuery = new ButtonObject(nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UC001-0000006")/*@res "��ѯ"*/, nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UC001-0000006")/*@res "��ѯ"*/, 0,"��ѯ"); /*-=notranslate=-*/
  m_boLocate = new ButtonObject(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec","UPPSCMCommon-000089")/*@res "��λ"*/, nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec","UPPSCMCommon-000089")/*@res "��λ"*/, 0,"��λ");  /*-=notranslate=-*/
  m_PrintMng = new ButtonObject(nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UC001-0000007")/*@res "��ӡ"*/, nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UC001-0000007")/*@res "��ӡ"*/, 0,"��ӡ����");  /*-=notranslate=-*/
  m_boPrint = new ButtonObject(nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UC001-0000007")/*@res "��ӡ"*/, nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UC001-0000007")/*@res "��ӡ"*/, 0,"��ӡ"); /*-=notranslate=-*/
  m_boPreview = new ButtonObject(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec","UPPSCMCommon-000305")/*@res "Ԥ��"*/, nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec","UPPSCMCommon-000305")/*@res "Ԥ��"*/, 0,"Ԥ��"); /*-=notranslate=-*/
  {
    m_PrintMng.addChildButton(m_boPrint);
    m_PrintMng.addChildButton(m_boPreview);
    }
  m_boList = new ButtonObject(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec","UPPSCMCommon-000186")/*@res "�л�"*/, nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec","UPPSCMCommon-000186")/*@res "�л�"*/, 0,"�л�");  /*-=notranslate=-*/

  m_boJointCheck = new ButtonObject(nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON","UPPSCMCommon-000145")/*@res "����"*/, nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON","UPPSCMCommon-000145")/*@res "����"*/, 0,"����");  /*-=notranslate=-*/

  m_billMng = new ButtonObject(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec","UPT40080816-000037")/*@res "����ά��"*/, nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec","UPPSCMCommon-000074")/*@res "����ά������"*/, 0,"����ά��");  /*-=notranslate=-*/
  m_billRowMng = new ButtonObject(nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UC001-0000011")/*@res "�в���"*/, nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UC001-0000011")/*@res "�в���"*/, 0,"�в���"); /*-=notranslate=-*/

  m_boRowQuyQty = new ButtonObject(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec","UPPSCMCommon-000359")/*@res "������ѯ"*/, nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec","UPPSCMCommon-000359")/*@res "������ѯ"*/, 0,"������ѯ"); /*-=notranslate=-*/

	// ���·�ҳ�Ŀ���
	m_pageBtn = new PageCtrlBtn(this);
	m_boBrowse = new ButtonObject(nc.ui.ml.NCLangRes.getInstance()
			.getStrByID("common", "UC001-0000021")/* @res "���" */,
			nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
					"UC001-0000021")/* @res "���" */, 0, "���"); /*-=notranslate=-*/
  
  m_boLineCardEdit = new ButtonObject(nc.ui.ml.NCLangRes.getInstance()
      .getStrByID("common", "SCMCOMMONIC55YB002")/* @res "��Ƭ�༭" */,
      nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
          "SCMCOMMONIC55YB002")/* @res "��Ƭ�༭" */, 0, "��Ƭ�༭"); /*-=notranslate=-*/
	
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
      this.getButtonExpenseInput()//2010-11-08 MeiChao ������¼�밴ť���뵽��ť����.
  };
}

/**
   * �����ߣ�������
   * ���ܣ���ʼ������
   * ������
   * ���أ�
   * ���⣺
   * ���ڣ�(2001-5-24 ���� 6:27)
   * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
   */
protected void initVariable() {
  //����
  /**��װ*/
  m_sBillTypeCode = nc.vo.ic.pub.BillTypeConst.m_transform;
  m_sBillCode = "IC_BILL_TEMPLET_004N";
  m_sPNodeCode = "40081008";
  //sSpecialHBO_Client = "nc.ui.ic.ic233.SpecialHBO_Client";
}

/**
 * �����ߣ�yangb
 * ���ܣ�������̬ת���õ���������ⵥ��VO
 * ������
 * ���أ�
 * ���⣺
 * ���ڣ�(2001-5-10 ���� 2:54)
 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
 * 
 * ������Ҫ���ת����ϵ������һ����̬ת�������������������¼����ͬһ��ת��ǰPK��
 * ���ǽ���̬ת����ת��ǰ��PK��¼��Դͷcfirstbillbid�ϣ�������Դ����ͬת��ǰ���У�
 * ��������ⵥ��cfirstbillbid����¼��ͬ��
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
  //������̬ת�������壬��ת��ǰ��PK ��¼��ת������ֶ�ctempparentid,Ȼ����VO����
  for(int i=0;i<spbodyvos.length;i++){
    
    if(spbodyvos[i].getFbillrowflag()!=null && spbodyvos[i].getFbillrowflag()==BillRowType.beforeConvert){
      list4i.add(spbodyvos[i]);
      beforeitemvo = spbodyvos[i];
    }else{
//      if(beforeitemvo==null){
//          showErrorMessage("������Ϊ[ת����]����ǰ�����������һ��[ת��ǰ]����");
//          return null;
//      }
      spbodyvos[i].setAttributeValue(ctempparentid, beforeitemvo.getCspecialbid());
      list4a.add(spbodyvos[i]);
    }
  }
  //�ֵ�
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
  
  
  //���� ����
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
  * �����ߣ�������
  * ���ܣ�ִ������
  * ������
  * ���أ�
  * ���⣺
  * ���ڣ�(2001-5-10 ���� 2:54)
  * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
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
 

//  //�ó���VO
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
//  //�����VO
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

  //����������ɨ�賬��ʵ������ hanwei 2003-11-4
  //���������������ͬʱ����
  //���õ�����������ͳ����ܳ�Ӧ������ add by hanwei 2004-6-5
  //�ڵ��ݽ�����ɨ����ƣ����ܳ���Ӧ������ nc.ui.ic.pub.bill.GeneralBillClientUI.scanfixline_fix(BarCodeParseVO [], int, int, boolean)
  getAuditDlg().getChldClientUIIn().getBarcodeCtrl().setOverShouldNum(false);
  getAuditDlg().getChldClientUIOut().getBarcodeCtrl().setOverShouldNum(false);
  //������༭����ɨ����ƣ����ܳ���ʵ������
  getAuditDlg().getChldClientUIIn().getBarcodeCtrl().setModifyBillUINum(false);
  getAuditDlg().getChldClientUIOut().getBarcodeCtrl().setModifyBillUINum(false);

  getAuditDlg().setName("BillDlg");
  getAuditDlg().showModal();
  //if (ret == nc.ui.pub.beans.UIDialog.ID_OK) {
  if (getAuditDlg().isOK()) {
    try { //���±�β
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
      "UC001-0000027")+ResBase.getSuccess()/* @res "�����ɹ�" */);
      
    } catch (Exception e) {
      handleException(e);
      nc.ui.pub.beans.MessageDialog.showErrorDlg(this, null, e.getMessage());
    }
  }
}


/**
  * �����ߣ�������
  * ���ܣ�VOУ��
  * ������
  * ���أ�
  * ���⣺
  * ���ڣ�(2001-5-24 ���� 5:17)
  * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
  */
protected boolean checkVO() {
  
  //ת��ǰ��ת�����в�����ȫ��ͬ
  //Ӧ���ѽ����˷ǿ�У��
  SpecialBillItemVO[] bodyvos = m_voBill.getItemVOs();
  if(bodyvos==null || bodyvos.length<0 || bodyvos.length<2){
    showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
        "4008other", "UPP4008other-000510"));/* @res "���������������1";*/
    return false;
  }
  Integer iFbillrowflag = null;
  String key = null;
  SpecialBillItemVO convBeforevo = null; 
  String convBeforeKey = null;
  int convBeforePos = -1;
  String[] fields = null;
  Integer isSupplierstock = null;//ת��ǰ���Ƿ�Ӧ�̹�����
  for(int i=0;i<bodyvos.length;i++){
    if(nc.vo.ic.pub.GenMethod.isSEmptyOrNull(bodyvos[i].getCinventoryid())){
      showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
          "4008other", "UPP4008other-000511")); //"�������Ϊ��!"
      return false;
    }
    iFbillrowflag = bodyvos[i].getFbillrowflag();
    if(iFbillrowflag==null){
      showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
          "4008other", "UPP4008other-000512")); //"���Ͳ���Ϊ��!"
      return false;
    }
    
    if(null != bodyvos[i].getM_Issupplierstock() && bodyvos[i].getM_Issupplierstock().intValue() == 1
    		&& (null == bodyvos[i].getCvendorid()||"".equals(bodyvos[i].getCvendorid()))){
        showErrorMessage(bodyvos[i].getCrowno()+nc.ui.ml.NCLangRes.getInstance().getStrByID(
                "4008other", "UPP4008other-000536")); //"�й�Ӧ�̲���Ϊ��!"
        return false;
      }
        
    if(iFbillrowflag.intValue()==BillRowType.afterConvert){
    	
//      if(convBeforePos<0){
//        showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
//            "4008other", "UPP4008other-000513")); //"������Ϊ[ת����]����ǰ�����������һ��[ת��ǰ]����";
//        return false;
//      }
    

      key = SmartVOUtilExt.getKeysString(bodyvos[i], fields, null);
    //add by ouyangzhb 2011-05-10����ţ�0000203: Ҫ���޸���̬ת������������
//
//      if (key.equals(convBeforeKey)) {
//        showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec","UPP4008spec-000140")/*@res "ת��ǰ��ת����� ������롢��������λ����������� ������һ��Ӧ��ͬ..."*/);
//        //������ɫΪ������ɫ
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
  	//add by ouyangzhb 2011-05-10 ����ţ�0000203: Ҫ���޸���̬ת������������ end
      
    }else {
    	isSupplierstock = bodyvos[i].getM_Issupplierstock();
    	if(null != isSupplierstock && isSupplierstock.intValue() == 1)
      	  fields = new String[]{"cinventoryid","castunitid","vbatchcode","vfree0","cvendorid"};
        else
      	  fields = new String[]{"cinventoryid","castunitid","vbatchcode","vfree0"};
      if(i>0 && convBeforePos>=0){
        if(convBeforePos==i-1 || i==bodyvos.length-1){
          showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
              "4008other", "UPP4008other-000514"));//"������Ϊ[ת��ǰ]���к������������һ��[ת����]����");
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
//    showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec","UPP4008spec-000139")/*@res "��һ�б�������ת��ǰ�����"*/);
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
//      showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec","UPP4008spec-000140")/*@res "ת��ǰ��ת����� ������롢��������λ����������� ������һ��Ӧ��ͬ..."*/);
//      //������ɫΪ������ɫ
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
  * �����ߣ�������
  * ���ܣ�����ֿ�ı��¼�����
  * ������e���ݱ༭�¼�
  * ���أ�
  * ���⣺
  * ���ڣ�(2001-5-8 19:08:05)
  * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
  *
  *
  *
  *
  */
public void afterBodyWhEdit(nc.ui.pub.bill.BillEditEvent e) {
  //�ֿ�
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

    //�������������б���ʽ����ʾ��
    nc.vo.scm.ic.bill.WhVO voWh=
      (nc.vo.scm.ic.bill.WhVO) SpecialBillHelper.queryInfo(new Integer(1), sID);

    if (m_voBill != null) {
      m_voBill.setBodyWh(e.getRow(), voWh);
      //���β�ִ���
      //m_voBill.clearInvQtyInfo();
      //������/������
      //String[] sIKs= getClearIDs(1, "cwarehousename");
      //int iRowCount= getBillCardPanel().getRowCount();
      //for (int row= 0; row < iRowCount; row++)
        //clearRowData(row, sIKs);
      //ˢ���ִ�����ʾ
      //setTailValue(0);

      //���������������
      //for (int i= 0; i < getBillCardPanel().getRowCount(); i++) {
      //clearRowData(i);
      //}
      //���µ���Ϣ��Ҫ�Ż���������κ�δ��ʾ����������ʾ��
      //showHintMessage("�ֿ��޸ģ�������ȷ����������Ρ�������");
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
 * �˴����뷽��˵����
 * �����ߣ�������
 * ���ܣ����κŸ����¼�
 * ������
 * ���أ�
 * ���⣺
 * ���ڣ�(2001-6-20 21:43:07)
 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
 *
 * @param param nc.ui.pub.bill.BillEditEvent
 * 
 */
public void afterLotEdit(nc.ui.pub.bill.BillEditEvent e) {
	//�޸��ˣ������� �޸����ڣ�2007-8-30����10:48:56 �޸�ԭ��ת����ҲҪ������
 // if (e.getRow() == 0) {//ת��ǰ��Ҫ��������Ƿ���ȷ
    String s = e.getKey();
//    WhVO whvo = m_voBill.getBodyWh(e.getRow());
    if (e.getRow() == 0) //ת��ǰ��Ҫ��������Ƿ���ȷ
    getLotRefbyHand(s);
    pickupLotRef(s);
    //ͬ���ı�m_voBill����pickupValuefromLotNumbRef() �����С�
  //}
}









/**
   * �����ߣ�������
   * ���ܣ��б༭���
   * ������
   * ���أ�
   * ���⣺
   * ���ڣ�(2001-5-24 ���� 9:38)
   * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
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

  //����㲻Ϊ�ֿ����ʱ���������޴�����ֹ��������ֵ
  if ((!sItemKey.equals("cinventorycode") && !sItemKey.equals("cwarehousename"))
    && (null == m_voBill.getItemValue(iRow, "cinventoryid")
      || bInventoryIdIsZero == true)) {
    bi.setEnabled(false);
    return;
  } else {
    bi.setEnabled(bi.isEdit());
  }

  //���ø���ķ�����
  super.colEditableSet(sItemKey, iRow);
  //���Ի�����

  if (sItemKey.equals("vbatchcode")) {
    if ((null != m_voBill.getItemValue(iRow, "isLotMgt"))
      && (Integer.valueOf(m_voBill.getItemValue(iRow, "isLotMgt").toString()).intValue()
        != 0)) {
      bi.setEnabled(true);
      if ((iRow == 0)) { //��һ�У�ת��ǰ,���⣬��Ҫѡ�����κš�
        String ColName =
          getBillCardPanel().getBillData().getBodyItem("vbatchcode").getName();
        getBillCardPanel().getBodyPanel().getTable().getColumn(ColName).setCellEditor(
          new BillCellEditor(getLotNumbRefPane()));
      } else { //ת������⣬��Ҫ¼�����κš�
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
      if ((iRow == 0)) { //��һ�У�ת��ǰ�����⣬���δ���ʧЧ���ڡ�
        bi.setEnabled(false);
      } else { //ת������Ҫ¼��
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
      if (iRow == 0) { //��һ�У�ת��ǰ�����⣬���δ����������ڡ�
        bi.setEnabled(false);
      } else { //ת������Ҫ¼��
        bi.setEnabled(true);
      }
    } else {
      bi.setEnabled(false);
    }
  }
}







































/**
   * �����ߣ�������
   * ���ܣ�����¼�����
   * ������e���ݱ༭�¼�
   * ���أ�
   * ���⣺
   * ���ڣ�(2001-5-8 19:08:05)
   * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
   *
   *
   *
   *
   */
public void afterInvEdit(Object value,int row) {


  //nc.vo.scm.pub.SCMEnv.out("inv chg");
  try {
    setTailValue(null);
    //����������������������,��ȥ����β��ʾ
    if ((value == null) || (value.toString().trim().length() == 0)) {
      clearRowData(row);
      //��β

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
      //����
      setBodyInvValue(row, voInv);
      //��β
      //setTailValue(voInv);
      //������
      String[] sIKs = getClearIDs(1, "cinventorycode");
      //���ƻ������Զ�����
      clearRowData(row, sIKs);
      //���µ���Ϣ��Ҫ�Ż���������κ�δ��ʾ����������ʾ��
      showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec","UPP4008spec-000141")/*@res "����޸ģ�������ȷ����������Ρ�������"*/);
    }
  } catch (Exception e2) {
    nc.vo.scm.pub.SCMEnv.error(e2);
  }
}

/**
* �����ߣ�������
* ���ܣ�����ת��,��ʼ������״̬
* ������
* ���أ�
* ���⣺
* ���ڣ�(2001-7-4 ���� 5:48)
* �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
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
 * �����ߣ�������
 * ���ܣ��ɴ���ĵ������͡��ֶΣ���õ����ֶθı��Ӧ�ı�������ֶ��б�
 * ������iBillFlag �������ͣ���Ϊ��ͨ���ݣ�����0����Ϊ���ⵥ�ݣ�����1
        ����
        ���          cinventorycode��
        ����ֿ�      cwarehousename��
        ������       vfree0��
        ��ͷ����ֿ�  coutwarehouseid��
        ��ͷ�ֿ�      cwarehouseid
 * ���أ�
 * ���⣺
 * ���ڣ�(2001-7-18 ���� 9:20)
 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
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
    //���
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
    //���ⵥ�ı������ڲֿ�
    sReturnString = new String[6];
    sReturnString[0] = "vbatchcode";
    sReturnString[2] = m_sNumItemKey;
    sReturnString[3] = m_sAstItemKey;
    sReturnString[4] = "scrq";
    sReturnString[5] = "dvalidate";
    //showWarningMessage("������ȷ�����κţ�");
    return null;
  } else if (sWhatChange.equals("vfree0")) {
    //������
    sReturnString = new String[3];
    sReturnString[0] = "vbatchcode";
    sReturnString[1] = "scrq";
    sReturnString[2] = "dvalidate";
    //showWarningMessage("������ȷ�����κţ�");
    //�޸��ˣ������� �޸����ڣ�2007-9-3����01:16:04 �޸�ԭ��
    //return null;
  } else if (sWhatChange.equals("coutwarehouseid")) {
    sReturnString = new String[5];
    sReturnString[0] = "vbatchcode";
    sReturnString[1] = m_sNumItemKey;
    sReturnString[2] = m_sAstItemKey;
    sReturnString[3] = "scrq";
    sReturnString[4] = "dvalidate";
    //showWarningMessage("������ȷ�����κţ�");
    return null;
  } else if ((sWhatChange.equals("cwarehouseid")) && (iBillFlag == 0)) {
    sReturnString = new String[5];
    sReturnString[0] = "vbatchcode";
    sReturnString[1] = m_sNumItemKey;
    sReturnString[2] = m_sAstItemKey;
    sReturnString[3] = "scrq";
    sReturnString[4] = "dvalidate";
    //showWarningMessage("������ȷ�����κţ�");
    return null;
  }
  return sReturnString;
}*/

/**
 * ClientUI ������ע�⡣
 * nc 2.2 �ṩ�ĵ������鹦�ܹ����ӡ�
 *
 */
public ClientUI(
  String pk_corp,
  String billType,
  String businessType,
  String operator,
  String billID) {
  super();
  //��ʼ������
  initialize(pk_corp, operator, "jc", businessType, "0001", "2003-04-17");
  //�鵥��
  SpecialBillVO voBill =
    qryBill(pk_corp, billType, businessType, operator, billID);
  if (voBill == null)
    nc.ui.pub.beans.MessageDialog.showHintDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec","UPPSCMCommon-000270")/*@res "��ʾ"*/, nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec","UPP4008spec-000121")/*@res "û�з��ϲ�ѯ�����ĵ��ݣ�"*/);
  else //��ʾ����
    setBillValueVO(voBill);

}

/**
 * �༭ǰ����
 * �������ڣ�(2001-3-23 2:02:27)
 * @param e ufbill.BillEditEvent
 */
public boolean beforeEdit(nc.ui.pub.bill.BillEditEvent e) {
  nc.vo.scm.pub.ctrl.GenMsgCtrl.printHint("beforeEdit:" + e.getKey());
  String sItemKey = e.getKey();
  nc.ui.pub.bill.BillItem bi = getBillCardPanel().getBillData().getBodyItem(sItemKey);

  boolean isEditable = true;
  int iRow = e.getRow();

  //���ø���ķ�����
  isEditable = super.beforeEdit(e);

  if (isEditable) {

    //���Ի�����

    //zhy2007-04-12���ⵥ�ݵ����κŵ�����ʾҲ����������ε���
    if (sItemKey.equals("vbatchcode")) {
      String ColName = getBillCardPanel().getBillData().getBodyItem("vbatchcode").getName();
      getBillCardPanel().getBodyPanel().getTable().getColumn(ColName).setCellEditor(
        new BillCellEditor(getLotNumbRefPane()));
      WhVO wvo = m_voBill.getBodyWh(iRow);
      getLotNumbRefPane().setParameter(wvo, m_voBill.getItemInv(iRow));
    }else
//    if (sItemKey.equals("vbatchcode")) {
//
//      if ((iRow == 0)) { //��һ�У�ת��ǰ,���⣬��Ҫѡ�����κš�
//        String ColName = getBillCardPanel().getBillData().getBodyItem("vbatchcode").getName();
//        getBillCardPanel().getBodyPanel().getTable().getColumn(ColName).setCellEditor(
//          new BillCellEditor(getLotNumbRefPane()));
//        WhVO wvo = m_voBill.getBodyWh(0);
//        getLotNumbRefPane().setParameter(wvo, m_voBill.getItemInv(0));
//      } else { //ת������⣬��Ҫ¼�����κš�
//        UITextField uft=new UITextField();
//        uft.setMaxLength(getBillCardPanel().getBodyItem("vbatchcode").getLength());
//        String ColName = getBillCardPanel().getBillData().getBodyItem("vbatchcode").getName();
//        getBillCardPanel().getBodyPanel().getTable().getColumn(ColName).setCellEditor(
//          new BillCellEditor(uft));
//      }
//
//    } else 
      if (sItemKey.equals("dvalidate") || sItemKey.equals("scrq")) {

      if ((iRow == 0)) { //��һ�У�ת��ǰ�����⣬���δ���ʧЧ���ڡ�
        bi.setEnabled(false);
      }

    }
  }

  bi.setEnabled(isEditable);
  return isEditable;

}

/**
 * �����ߣ�������
 * ����: ת��Ϊ����ⵥ��ͷ
 * ������
 * ���أ�
 * ���⣺
 * ���ڣ�(2001-8-16 12:53:03)
 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
 * @param gvo nc.vo.ic.pub.bill.GeneralBillVO
 * @param svo nc.vo.ic.pub.bill.SpecialBillVO
 * @param iInOutFlag int
 */
protected GeneralBillHeaderVO changeFromSpecialVOtoGeneralVOAboutHeader(
  GeneralBillVO gvo,
  SpecialBillVO svo,
  int iInOutFlag) {

  //ȡ�����һ�еĲֿ����ԣ�ǰ���Ǵ�������ⵥ����ֻ��һ���ֿ�
  gvo.setWh(svo.getBodyWh(0));

  GeneralBillHeaderVO voBillHeader =
    super.changeFromSpecialVOtoGeneralVOAboutHeader(gvo, svo, iInOutFlag);

  //���Ի�����
  SpecialBillHeaderVO voSpHeader = svo.getHeaderVO();
  voBillHeader.setCdptid(voSpHeader.getCoutdeptid());
  voBillHeader.setCdptname(voSpHeader.getCoutdeptname());
  voBillHeader.setCbizid(voSpHeader.getCoutbsor());
  voBillHeader.setCbizname(voSpHeader.getCoutbsorname());

  return voBillHeader;
}

/**
 * �����ߣ�������
 * ���ܣ�
 * ������
 * ���أ�
 * ���⣺
 * ���ڣ�(2001-8-16 12:53:03)
 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
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
        getQueryDlg().setCombox("qbillstatus", new String[][] { { "1", nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec","UPP4008spec-000128")/*@res "�Ƶ�"*/ }, {
          "0", nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UC001-0000027")/*@res "����"*/ }, {
          "2", nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec","UPPSCMCommon-000217")/*@res "ȫ��"*/ }
        });
        
      }
    };
  }
  return this.m_queryHelp;
}

/**
 * ���� QueryConditionClient1 ����ֵ��
 * @return nc.ui.pub.query.QueryConditionClient
 */
//protected QueryConditionDlgForBill getConditionDlg() {
//  if (ivjQueryConditionDlg == null) {
//    ivjQueryConditionDlg= new QueryConditionDlgForBill(this);
//    //ivjQueryConditionDlg.setDefaultCloseOperation(ivjQueryConditionDlg.HIDE_ON_CLOSE);
//    //getConditionDlg().setTempletID(m_sCorpID, m_sPNodeCode, m_sUserID, null);
//    ivjQueryConditionDlg.setTempletID(m_sCorpID, m_sPNodeCode, m_sUserID, null);
//
//    //����Ϊ�Բ��յĳ�ʼ��
//    ivjQueryConditionDlg.initQueryDlgRef();
//    //����Ϊ�Թ�˾���յĳ�ʼ��
//    ArrayList alCorpIDs= new ArrayList();
//    try {
//      alCorpIDs=
//        (ArrayList) SpecialBillHelper.queryInfo(
//          new Integer(QryInfoConst.USER_CORP),
//          m_sUserID);
//    } catch (Exception e) {
//      nc.vo.scm.pub.SCMEnv.error(e);
//    }
//    //���õ�������Ϊ��ǰ��¼����
////    ivjQueryConditionDlg.setInitDate("dbilldate", m_sLogDate);
//  //modified by liuzy 2008-04-01 v5.03���󣺿�浥�ݲ�ѯ������ֹ����
//	ivjQueryConditionDlg.setInitDate("dbilldate.from", m_sLogDate);
//	ivjQueryConditionDlg.setInitDate("dbilldate.end", m_sLogDate);
//    ivjQueryConditionDlg.initCorpRef("pk_corp", m_sCorpID, alCorpIDs);
//
//    ivjQueryConditionDlg.setCombox("qbillstatus", new String[][] { { "1", nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec","UPP4008spec-000128")/*@res "�Ƶ�"*/ }, {
//        "0", nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UC001-0000027")/*@res "����"*/ }, {
//        "2", nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec","UPPSCMCommon-000217")/*@res "ȫ��"*/ }
//    });
//
//  }
//  return ivjQueryConditionDlg;
//}

/**
 * �򵥳�ʼ���ࡣ����������������������õĲ���Ա����˾�ȡ�
 */
/* ���棺�˷������������ɡ� */
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
	// �޸��ˣ������� �޸����ڣ�2007-12-26����11:05:02 �޸�ԭ���Ҽ�����"�����к�"
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
 * �
 * ���ܣ�
 * ������
 * ���أ�
 * ���⣺
 * ���ڣ�(2005-1-14 15:13:27)
 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
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
 * �����ߣ����˾�
 * ���ܣ���ѯָ���ĵ��ݡ�
 * ������
billType, ��ǰ��������
billID, ��ǰ����ID
businessType, ��ǰҵ������
operator, ��ǰ�û�ID
pk_corp, ��ǰ��˾ID

* ���� ������vo
* ���� ��
* ���� �� (2001 - 5 - 9 9 : 23 : 32)
* �޸����� �� �޸��� �� �޸�ԭ�� �� ע�ͱ�־ ��
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
  //�ñ�ͷ�ĳ��׼�
  if (voResult != null
    && voResult.getHeaderVO() != null
    && voResult.getItemVOs() != null) {
    //�õ���һ�ŵ��ݱ���
//    SpecialBillHeaderVO voHead = voResult.getHeaderVO();
    SpecialBillItemVO[] voItems = voResult.getItemVOs();

    if (voItems != null && voItems.length > 0) {
      int iLen = voItems.length;
      for (int i = 0; i < iLen; i++) {
        if (voItems[i].getFbillrowflag() != null) {
          if (voItems[i].getFbillrowflag().intValue() == BillRowType.beforeConvert)
            voItems[i].setInvsetparttype(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec","UPP4008spec-000142")/*@res "ת��ǰ"*/);
          else
            voItems[i].setInvsetparttype(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec","UPP4008spec-000143")/*@res "ת����"*/);
        }
      }
    }

  }
  return voResult;
}

/**
 * �˴����뷽��˵����
   ������ն�ѡ�ý���
   ����̬ת��������������
 * �������ڣ�(2003-11-11 20:48:18)
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
    //����
    setBodyInvValue(iCurRow, invVOs[i]);
    //��β
    //setTailValue(invVOs[i]);
    //������
    String[] sIKs = getClearIDs(1, "cinventorycode");
    //���ƻ������Զ�����
    clearRowData(iCurRow, sIKs);
  }

  getBillCardPanel().getBillModel().setNeedCalculate(true);
  getBillCardPanel().getBillModel().reCalcurateAll();

}

/**
 * ������
 * ���ܣ�
 * ������
 * ���أ�
 * ���⣺
 * ���ڣ�(2005-1-18 14:45:22)
 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
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
* �����ߣ�������
* ���ܣ�����ת��,��ʼ������״̬
* ������
* ���أ�
* ���⣺
* ���ڣ�(2001-7-4 ���� 5:48)
* �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
*/
protected void setShowStatus(int irow, int status) {

  if (m_voBill != null
    && m_voBill.getChildrenVO() != null
    && irow < m_voBill.getChildrenVO().length) {
    m_voBill.setItemValue(irow, "fbillrowflag", new Integer(status));
    getBillCardPanel().setBodyValueAt(new Integer(status), irow, "fbillrowflag");
    if (status == BillRowType.beforeConvert) {
      m_voBill.setItemValue(irow, "invsetparttype", nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec","UPP4008spec-000142")/*@res "ת��ǰ"*/);
      getBillCardPanel().setBodyValueAt(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec","UPP4008spec-000142")/*@res "ת��ǰ"*/, irow, "invsetparttype");
    } else {
      m_voBill.setItemValue(irow, "invsetparttype", nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec","UPP4008spec-000143")/*@res "ת����"*/);
      getBillCardPanel().setBodyValueAt(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec","UPP4008spec-000143")/*@res "ת����"*/, irow, "invsetparttype");
    }

  }
}

/**
 * ������
 * ���ܣ�
 * ������
 * ���أ�
 * ���⣺
 * ���ڣ�(2005-1-22 18:01:56)
 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
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
 *����¼�밴ť���¼���Ӧ����
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
	// �򿪷���¼�����
	c.showModal();
	// ������¼�����ر�ʱ,��¼������ݴ�ŵ�������Ϣҳǩ��
	if (c.isCloseOK()) {
		InformationCostVO[] infoCostVOs = c.getInfoCostVOs();
		if (infoCostVOs != null && infoCostVOs.length != 0){
			// ������¼������vo���鲻Ϊ��ʱ,��vo�浽����¼��ҳǩ��
					getBillCardPanel().getBillData().setBodyValueVO(
						"jj_scm_informationcost", infoCostVOs);
				getBillCardPanel().getBillModel("jj_scm_informationcost").execLoadFormula();
			}	
		
	}
}
/**
 * 
 * (���÷���¼�밴ť�Ŀɼ�/����״̬)
 *  MeiChao  ����ע��  2010-11-08  
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