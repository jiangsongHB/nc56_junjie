package nc.ui.rc.receive;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Vector;

import javax.swing.JComponent;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ListSelectionListener;

import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.itf.po.outer.IQueryForIc;
import nc.itf.rc.receive.IArriveorder;
import nc.itf.rc.receive.IQueryForInitUI;
import nc.itf.uap.IUAPQueryBS;
import nc.itf.uap.IVOPersistence;
import nc.itf.uap.pf.IPFWorkflowQry;
import nc.itf.uap.rbac.IUserManageQuery;
import nc.itf.uap.sf.ICreateCorpQueryService;
import nc.itf.uap.workshop.plugins.formdev.IUAPBillService;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.jdbc.framework.processor.MapProcessor;
import nc.jdbc.framework.processor.ResultSetProcessor;
import nc.ui.bd.ref.IBusiType;
import nc.ui.ic.pub.lot.LotNumbRefPane;
import nc.ui.ml.NCLangRes;
import nc.ui.pf.query.ICheckRetVO;
import nc.ui.po.OrderHelper;
import nc.ui.po.pub.PoPublicUIClass;
import nc.ui.po.pub.PoToBackRcQueDLG;
import nc.ui.po.ref.OtherRefModel;
import nc.ui.pr.pray.IButtonConstPr;
import nc.ui.pu.jj.JJPuScmPubHelper;
import nc.ui.pu.jjpanel.InfoCostPanel;
import nc.ui.pu.pub.ATPForOneInvMulCorpUI;
import nc.ui.pu.pub.CheckISSellProxyHelper;
import nc.ui.pu.pub.PuGetUIValueTool;
import nc.ui.pu.pub.PuProjectPhaseRefModel;
import nc.ui.pu.pub.PuTool;
import nc.ui.pu.pub.PubHelper;
import nc.ui.pu.pub.SerialAllocationDlg;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.FramePanel;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UIMenuItem;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.beans.UITextField;
import nc.ui.pub.bill.BillActionListener;
import nc.ui.pub.bill.BillBodyMenuListener;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.bill.BillData;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillEditListener;
import nc.ui.pub.bill.BillEditListener2;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillListData;
import nc.ui.pub.bill.BillListPanel;
import nc.ui.pub.bill.BillModel;
import nc.ui.pub.bill.BillScrollPane;
import nc.ui.pub.bill.BillTableMouseListener;
import nc.ui.pub.bill.BillUIUtil;
import nc.ui.pub.bill.IBillData;
import nc.ui.pub.bill.IBillModelSortPrepareListener;
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
import nc.ui.pub.pf.PfUtilClient;
import nc.ui.pub.workflownote.FlowStateDlg;
import nc.ui.rc.pub.BackReasonRefModel;
import nc.ui.rc.pub.CPurchseMethods;
import nc.ui.rc.pub.InvRefModelForRepl;
import nc.ui.rc.pub.LocateDlg;
import nc.ui.rc.pub.PurchasePrintDS;
import nc.ui.scm.ic.freeitem.FreeItemRefPane;
import nc.ui.scm.ic.setpart.SetPartDlg;
import nc.ui.scm.inv.InvTool;
import nc.ui.scm.print.BillPrintTool;
import nc.ui.scm.print.SplitParams;
import nc.ui.scm.print.SplitPrintParamDlg;
import nc.ui.scm.pu.ParaVOForBatch;
import nc.ui.scm.pub.BillTools;
import nc.ui.scm.pub.BusiBillManageTool;
import nc.ui.scm.pub.CacheTool;
import nc.ui.scm.pub.CollectSettingDlg;
import nc.ui.scm.pub.bill.ButtonTree;
import nc.ui.scm.pub.bill.IBillExtendFun;
import nc.ui.scm.pub.def.DefSetTool;
import nc.ui.scm.pub.panel.RelationsCal;
import nc.ui.scm.pub.print.ISetBillVO;
import nc.ui.scm.pub.print.ScmPrintTool;
import nc.ui.scm.pub.report.BillRowNo;
import nc.ui.scm.pub.sourceref.SourceRefDlg;
import nc.ui.scm.ref.WarehouseRefModel;
import nc.vo.bd.b06.PsndocVO;
import nc.vo.bd.def.DefVO;
import nc.vo.pf.change.PfUtilBaseTools;
import nc.vo.po.OrderVO;
import nc.vo.po.pub.Operlog;
import nc.vo.po.pub.OrderPubVO;
import nc.vo.po.pub.SerialUiVO;
import nc.vo.pu.exception.RwtRcToPoException;
import nc.vo.pu.exception.RwtRcToScException;
import nc.vo.pu.pub.InitValue;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.CommonConstant;
import nc.vo.pub.ProductCode;
import nc.vo.pub.VOStatus;
import nc.vo.pub.ValidationException;
import nc.vo.pub.bill.BillTempletVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.lang.UFTime;
import nc.vo.pub.query.ConditionVO;
import nc.vo.rc.receive.ArrivePubVO;
import nc.vo.rc.receive.ArriveorderHeaderVO;
import nc.vo.rc.receive.ArriveorderItemVO;
import nc.vo.rc.receive.ArriveorderVO;
import nc.vo.scm.constant.ScmConst;
import nc.vo.scm.datapower.BtnPowerVO;
import nc.vo.scm.ic.bill.FreeVO;
import nc.vo.pu.jjvo.InformationCostVO;
import nc.vo.scm.pu.BillStatus;
import nc.vo.scm.pu.BillTypeConst;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.scm.pub.SCMEnv;
import nc.vo.scm.pub.session.ClientLink;
import nc.vo.scm.service.ServcallVO;
import nc.vo.uap.pf.PFBusinessException;


/**
 * @����������������ά��
 * @���ߣ���־ƽ
 * @�������ڣ�(2001-5-24 9:42:56)
 */
public class ArriveUI
  extends nc.ui.pub.ToftPanel
  implements BillEditListener,  BillTableMouseListener, ActionListener, 
  BillEditListener2, ListSelectionListener, BillBodyMenuListener, IBillModelSortPrepareListener,
  ISetBillVO, IBillExtendFun, ICheckRetVO, IBillRelaSortListener2, ILinkMaintain,//�����޸�
  ILinkAdd,//�������� 
  ILinkApprove,//������
  ILinkQuery,//������
  BillActionListener
  {
	private InformationCostVO[] vos = null;
	private ButtonObject boInfoCost ;//����¼�밴ť  add by QuSida 2010-8-28 (��ɽ����)
	private ButtonObject[] extendBtns ; //���ο�����ť����  add by QuSida 2010-8-28 (��ɽ����)
	private UFDouble arrnumber ;//ʵ�ʵ�������
	private UFDouble arrnum ;//�ۼƵ�������
//	private UFDouble arrmny;//�ۼƵ������
	
    //ȡϵͳ������
    private int m_iPowerAssNum = 2;
    private int m_iPowerConvertRate= 2;
    private int m_iPowerNum = 2;
    private int m_iPowerMoney = 2;
    private int m_iPowerPrice = 2;
    //AIM�Ƿ�����
    private boolean m_bAIMEnabled = false;
    //QC�Ƿ�����
    private boolean m_bQcEnabled = false;
    //��ť��ʵ��,since v51
    private ButtonTree m_btnTree = null;  
    
    //	�����������
    class IBillRelaSortListener2Body implements IBillRelaSortListener2 {
        public Object[] getRelaSortObjectArray() {
            return ArriveUI.this.getRelaSortObjectArrayBody();
        }
    }
    private boolean isRevise = false;
    private nc.itf.uap.pf.IPFWorkflowQry ipflowqry = ((IPFWorkflowQry) NCLocator.getInstance().lookup(IPFWorkflowQry.class.getName()));

    //since v55,�����к�
    UIMenuItem m_miReSortRowNo = null;
    UIMenuItem m_miCardEdit = null;
    private ButtonObject m_btnReSortRowNo = null;
    private ButtonObject m_btnCardEdit = null;
    //since v55,�����кż���
    class IMenuItemListener implements ActionListener{
        public void actionPerformed(ActionEvent event){
            ArriveUI.this.onMenuItemClick(event);
        };
    }  
  // ���кŶԻ���
  private SerialAllocationDlg m_dlgSerialAllocation = null;
  // ���浱ǰ�������޸ĵĵ��ݵ����кŷ�������
  public HashMap m_alSerialData =  new HashMap();
  // ���浱ǰ�������޸ĵĵ��ݵ����кŷ�������
  public HashMap m_alSerialDataCancel =  new HashMap();

  //�б��Ƿ���ع�
  private boolean m_bLoaded = false;
  
  //�Ƿ���Ҫ������Դ�����кŵ���Ϣ
  //private boolean m_bNeedLoadSourceInfo = true;
  
  private boolean m_bLoadedCard = false;
  //�򲻿��˽ڵ��ԭ��
  private String m_strNoOpenReasonMsg = null;
  //
  private boolean m_bQueriedFlag = false;
  
    //������������ȷ�������⣺
    class MyBillData implements IBillData{
    public void prepareBillData(nc.ui.pub.bill.BillData bd) {
        ArriveUI.this.initBillBeforeLoad(bd);
    }
  }
  /*���б��建��*/
  private Hashtable hBodyItem = new Hashtable();
  //�Ƿ�ı���ҵ������
  boolean isChangeBusitype = true;
  /**����ʱ���⴦����*/
  private boolean isFrmList = false;
  /**��ŵ�ǰ��ͷ��Ӧ�ı���*/
  private ArriveorderItemVO[] items = null;

  //��λ�Ի���
  private LocateDlg locatedlg = null;
  //������Ƭ
  private BillCardPanel m_billPanel = null;
  //������ѯ���
  private ArriveorderVO[] m_arriveVOs = null;
  //�����б�
  private BillListPanel m_arrListPanel = null;
    //������ѯ�Ի���
  ATPForOneInvMulCorpUI m_atpDlg = null;
  /*�ɹ��˻���ѯ��*/
  private PoToBackRcQueDLG m_backQuePoDlg = null;
  /*ί���˻���ѯ��*/
  private RcToScQueDLG m_backQueScDlg = null;
  /*�˻����ղɹ�����ѡ�����*/
  private ArrFrmOrdUI m_backRefUIPo = null;
  /*�˻�����ί�ⶩ��ѡ�����*/
  private ArrFrmOrdUI m_backRefUISc = null;
  
  //���﷭�빤��
  private NCLangRes m_lanResTool = NCLangRes.getInstance();
  
  /*��Ƭ��ť����*/
  private ButtonObject m_btnCheck = null;
  //ҵ������
  private ButtonObject m_btnBusiTypes = null;
  //������
  private ButtonObject m_btnAdds = null;
  //�˻���
  private ButtonObject m_btnBackPo = null;
  private ButtonObject m_btnBackSc = null;
  private ButtonObject m_btnBacks = null;
  //����ά����
  private ButtonObject m_btnModify = null;
  private ButtonObject m_btnSave = null;
  private ButtonObject m_btnCancel = null;
  private ButtonObject m_btnDiscard = null;
  private ButtonObject m_btnSendAudit = null;  
  private ButtonObject m_btnMaintains = null;
  //�в�����
  private ButtonObject m_btnDelLine = null;
  private ButtonObject m_btnCpyLine = null;
  private ButtonObject m_btnPstLine = null;
  //since v55, ճ���е���β
  private ButtonObject m_btnPstLineTail = null;
  private ButtonObject m_btnLines = null;
  //���������
  private ButtonObject m_btnBrowses = null;
  private ButtonObject m_btnQuery = null;
  private ButtonObject m_btnFirst = null;
  private ButtonObject m_btnPrev = null;
  private ButtonObject m_btnNext = null;
  private ButtonObject m_btnLast = null;
  private ButtonObject m_btnLocate = null;
  private ButtonObject m_btnRefresh = null;
  //�л�
  private ButtonObject m_btnList = null;
  //ִ����(��������������Ϣ���Ĺ���)
  private ButtonObject m_btnActions = null;
  //������
  private ButtonObject m_btnOthers = null;
  public  ButtonObject m_btnCombin = null;
  private ButtonObject m_btnPrints = null;
  private ButtonObject m_btnPrint = null;
  private ButtonObject m_btnPrintPreview = null;
  private ButtonObject m_btnUsable = null;
  private ButtonObject m_btnQueryBOM = null;
  private ButtonObject m_btnQuickReceive = null;
  protected ButtonObject m_btnDocument = null;
  protected ButtonObject m_btnLookSrcBill = null;
  private ButtonObject m_btnQueryForAudit = null;
  private ButtonObject m_btnCreateCard = null;
  private ButtonObject m_btnDeleteCard = null;
//  private ButtonObject m_btnSerialNO = null;


  /*��Ƭ��ť�˵�*/
  private ButtonObject m_aryArrCardButtons[] = null;
  
  /*�б�ť����*/
  private ButtonObject m_btnSelectAll = null;
  private ButtonObject m_btnSelectNo = null;
  private ButtonObject m_btnModifyList = null;
  private ButtonObject m_btnDiscardList = null;
  private ButtonObject m_btnQueryList = null;
  private ButtonObject m_btnCard = null;
  private ButtonObject m_btnEndCreate = null;
  private ButtonObject m_btnRefreshList = null;
  
  //������
  private ButtonObject m_btnUsableList = null;
  protected ButtonObject m_btnDocumentList = null;
  private ButtonObject m_btnQueryBOMList = null;
  private ButtonObject m_btnPrintPreviewList = null;
  private ButtonObject m_btnSplitPrint = null;
  private ButtonObject m_btnPrintList = null;
  private ButtonObject m_btnOthersList = null;

  /*�б�ť��*/
  private ButtonObject m_aryArrListButtons[] = null;
  /*��Ϣ���İ�ť��*/
  private ButtonObject m_btnAudit = null;
  private ButtonObject m_btnUnAudit = null;
  private ButtonObject m_btnOthersMsgCenter = null;
  private ButtonObject m_btnActionMsgCenter = null;
  private ButtonObject m_aryMsgCenter[] = null;
  
  //������ѯ�����Ի���
  private RcQueDlg m_dlgArrQueryCondition = null;
  /*�����ջ��Ի���*/
  private QueryOrdDlg m_dlgQuickArr = null;
  //���浥����ID��Ӧ�Ĵ������ID
  private Hashtable m_hBillIDsForCmangids = new Hashtable();
  /*����ʱ������ڴ�����*/
  private HashMap m_hTS = null;
  ////�����Ƿ񸨼��������־
  //Hashtable m_hIsAssMana = new Hashtable();
  //boolean isFlagsCache = false;
  ////���滻���ʡ��Ƿ�̶�������
  //Hashtable m_hConvertIsfixed = new Hashtable();
  //���汣��������
  private Hashtable m_hValiddays = new Hashtable();
  //������ǰ��
  private int m_iArrCurrRow = 0;
  /*��¼:ת��ǰ�û���ʾ���ݻ���λ��*/
  private int m_OldCardVOPos = 0;
  /*��¼:ת����ǰ�û��������ݳ���*/
  private int m_OldVOsLen = 0;
  /*���Ų���*/
  private UIRefPane m_PnlLotRef = null;
  //������ӡ�����б��ͷ�����к�
  protected ArrayList listSelectBillsPos = null;
  /*������ʽ����VOs*/
  private ArriveorderVO[] m_pushSaveVOs = null;
  //����״̬:��ʼ������������������޸ģ������б�ת���б�ת���޸ģ���Ϣ����
  private String m_strState = "��ʼ��";
  /*֧��ת������治�����ʾ���������*/
  /*��¼:ת���󻺴������е�����VO[],��ʼֵΪת��ǰ�û��������,�û�����ɹ�һ�ŵ���,����������һ�ŵ���*/
  private ArriveorderVO[] m_VOsAll = null;
  /**�ؼ��ֶ�Ӧ�ļ��㹫ʽ��ĳ��� ( �μ� RelationsCal )*/
  private int[] m_iDescriptions = new int[] {
      RelationsCal.IS_FIXED_CONVERT, RelationsCal.CONVERT_RATE, RelationsCal.NUM_ASSIST,
      RelationsCal.NUM, RelationsCal.NET_PRICE_ORIGINAL, RelationsCal.MONEY_ORIGINAL };
  //
  private ScmPrintTool printList = null;
  private ScmPrintTool printCard = null;
  
  //ҵ������(���˶��������˵�����ʱ���õ�)
  private UIRefPane m_refBusi = null;
  /**��ʽ�����õ�����*/
  /**���ü��㹫ʽ�ؼ����б�(��������ʱҲҪ���������йؼ���)*/
  private String[] m_saKey = new String[] { "Y", "convertrate", "nassistnum", "narrvnum", "nprice", "nmoney" };
  //��¼�����ɾ����
  private Vector v_DeletedItems = new Vector();
  //��¼δ����ֵ�ɾ����
  private Vector vDelNoSplitted = new Vector();
  //��ǰ��¼����Ա��Ȩ�޵Ĺ�˾[]
  private String[] saPkCorp = null;

  //�Ƿ����Ƶ���
  private boolean m_bSaveMaker = true;
  //��Ϣ���ĵ���ID
  private String m_strBillId = null;
  
  //�����ջ������г����쳣��־
  private boolean m_bQuickException = false;  
/**
 * ��ȡ�Ƿ�����ջ������г����쳣
 */
  public boolean isQuickException(){
    return m_bQuickException;
  }
/**
 * �����Ƿ�����ջ������г����쳣
 */
  public void setQuickExceptionFlag(boolean newVal){
    m_bQuickException = newVal;
  }


/**
 * ArriveUI ������ע�⡣
 */
public ArriveUI() {
  super();
  initialize();
}

/**
 * ArriveUI ������ע�⡣
 */
public ArriveUI(
  String pk_corp,
  String billType,
  String businessType,
  String operator,
  String billID) {

  super();
  //setCauditid(billID);
  initialize();
  ArriveorderVO vo = null;
  try {
    vo = ArriveorderHelper.findByPrimaryKey(billID);
    if (vo != null) {
      setCacheVOs(new ArriveorderVO[] { vo });
      setDispIndex(0);
      loadDataToCard();
    }
  } catch (Exception e) {
    SCMEnv.out(e);
  }
}

//For V56
public ArriveUI(FramePanel fp) {
  super();
  setFrame(fp);
  initialize();
}

/**
 * ��������������հ�ť�¼�
 * ���������������bodyRowChange()��֤
 * ���û���������������bodyRowChange()���������ص������ť
 * �������ڣ�(2001-10-20 11:25:46)
 * @param e java.awt.event.ActionEvent
 */
public void actionPerformed(java.awt.event.ActionEvent e) {

  //if (getArrBillCardPanel().getBodyItem("vfree0") != null) {
    //if (e.getSource() == ((FreeItemRefPane) getArrBillCardPanel().getBodyItem("vfree0").getComponent()).getUIButton()) {
      //PuTool.actionPerformedFree(
        //getArrBillCardPanel(),
        //e,
        //new String[] { "cmangid", "cinventorycode", "cinventoryname", "cinventoryspec", "cinventorytype" },
        //new String[] { "vfree0", "vfree1", "vfree2", "vfree3", "vfree4", "vfree5" });
    //}
  //}
}
/**
 * �༭���¼�--��ͷ�����֯
 * @param e
 */
private void afterEditWhenHeadStorOrg(BillEditEvent e){
  int iSize = getBillCardPanel().getRowCount();
  if(iSize <= 0){
    return;
  }
  for(int i=0 ;i < iSize ; i++){
    getBillCardPanel().setBodyValueAt(null,i,"cwarehouseid");
    getBillCardPanel().setBodyValueAt(null,i,"cwarehousename");
    getBillCardPanel().getBillModel().setRowState(i, BillModel.MODIFICATION);
  }
}
/**
 * @���ܣ��༭���¼� --> ĳ���Ըı�󴥷����߼�
 */
public void afterEdit(BillEditEvent e) {
  if (getBillCardPanel().getBillTable().getEditingRow() >= 0) {
    getBillCardPanel().getBillTable().editingStopped(new ChangeEvent(getBillCardPanel().getBillTable()));
  }
  BillModel bm = getBillCardPanel().getBillModel();
  if(e.getKey().equals("cstoreorganization")){
    afterEditWhenHeadStorOrg(e);
  }
  //�����к�
  else if (e.getKey().equals("crowno")) {
    BillRowNo.afterEditWhenRowNo(getBillCardPanel(), e, nc.vo.scm.pu.BillTypeConst.PO_ARRIVE);
  } else if (
    //����
    (e.getKey().equals("convertrate")
      || e.getKey().equals("nassistnum")
      || e.getKey().equals("narrvnum")
      || e.getKey().equals("nprice")
      || e.getKey().equals("nmoney")
      || e.getKey().equals("nelignum")
      || e.getKey().equals("nnotelignum"))) {
    //��ʽ����
    afterEditWhenNum(e);
    //����������Ϊ�෴����ʱ�Ĵ���
    if (e.getKey().equals("narrvnum") || e.getKey().equals("nassistnum"))
      afterSignChged(e);
    //������Ʒ
    afterEditWhenBodyLargessNums(e.getRow());
  } else if (e.getKey().equals("npresentnum")|| e.getKey().equals("npresentassistnum") || e.getKey().equals("nwastnum") || e.getKey().equals("nwastassistnum")) {
	  //��Ʒ��������Ʒ��������;��������;�ĸ���������
	  afterEditWhenPresentnum(e);
  } else if (e.getKey().equals("cassistunitname")) {
    //������
    afterEditWhenAssistUnit(e);
  } else if (e.getKey().equals("cinventorycode")) {
    //���
    afterEditWhenInv(e);
  } else if (e.getKey().equals("cemployeeid")) {
    //�ɹ�Ա
    afterEditWhenHeadEmployee(e);
  } else if (e.getKey().equals("vproducenum")) {
    //����
    afterEditWhenProdNum(e);
  } else if (e.getKey().equals("dproducedate")) {
    //��������
    afterEditWhenProdDate(e);
  } else if (e.getKey().equals("ivalidday")) {
    //����������
    afterEditWhenValidDays(e);
  } else if (e.getKey().equals("vmemo") && e.getPos() == 1) {
    if (getBillCardPanel().getBodyItem("vmemo") != null) {
      //���屸ע
      UIRefPane refBodyVmemo = (UIRefPane) getBillCardPanel().getBodyItem("vmemo").getComponent();
      bm.setValueAt(refBodyVmemo.getUITextField().getText(), e.getRow(), "vmemo");
    }
  } else if (e.getKey().equals("vbackreasonb") && e.getPos() == 1) {
    if (getBillCardPanel().getBodyItem("vbackreasonb") != null) {
      //�����˻�����
      UIRefPane refBodyReason = (UIRefPane) getBillCardPanel().getBodyItem("vbackreasonb").getComponent();
      bm.setValueAt(refBodyReason.getUITextField().getText(), e.getRow(), "vbackreasonb");
    }
  } else if (e.getKey().equals("vfree0")) {
    //������
    afterEditFree(e);
  } else if (e.getKey().equals("cwarehousename")) {
    //�ֿ�
    afterEditWhenWareHouse(e);
  } else if (e.getKey().equals("cproject")) {
    //��Ŀ
    afterEditWhenProject(e);
  //��Դ����������Ʒ�У���Ʒ��־���ܸ���
  } else if(e.getKey().equals("blargess")){
    afterEditWhenBodyLargessNums(e.getRow());
  }
  //�Զ�����PK����
  if(e.getPos() == 1)
    setBodyDefPK(e);
  else
    setHeadDefPK(e);
}
private void afterEditWhenPresentnum(BillEditEvent e) {
	UFDouble convert = new UFDouble(0);
	//���ID
	String sBaseID =(String) getBillCardPanel().getBillModel().getValueAt(e.getRow(), "cbaseid");
	//����������
	String sCassId =(String) getBillCardPanel().getBillModel().getValueAt(e.getRow(),"cassistunit");
	if (sCassId == null || sCassId.trim().equals("")) {
		UIRefPane refAss =(UIRefPane) getBillCardPanel().getBodyItem("cassistunitname").getComponent();
		sCassId = refAss.getRefPK();
		String sCassName = refAss.getRefName();
		getBillCardPanel().getBillModel().setValueAt(sCassId,e.getRow(),"cassistunit");
		getBillCardPanel().getBillModel().setValueAt(sCassName,e.getRow(),"cassistunitname");
	}
	//��ȡ������
	convert = nc.ui.pu.pub.PuTool.getInvConvRateValue(sBaseID, sCassId);
	getBillCardPanel().getBillModel().setValueAt(convert,e.getRow(),"convertrate");

	UFDouble aa = new UFDouble(0);
	UFDouble bb = new UFDouble(0);

	if (e.getKey().equals("npresentnum")){
		aa = getBillCardPanel().getBodyValueAt(e.getRow(),"npresentnum")==null?new UFDouble(0):(UFDouble)getBillCardPanel().getBodyValueAt(e.getRow(),"npresentnum");//ԭ��˰��
		getBillCardPanel().getBillModel().setValueAt(aa.div(convert),e.getRow(),"npresentassistnum");
	}else if(e.getKey().equals("npresentassistnum")){
		bb = getBillCardPanel().getBodyValueAt(e.getRow(),"npresentassistnum")==null?new UFDouble(0):(UFDouble)getBillCardPanel().getBodyValueAt(e.getRow(),"npresentassistnum");//ԭ����˰���
		getBillCardPanel().getBillModel().setValueAt(bb.multiply(convert),e.getRow(),"npresentnum");
	}else if(e.getKey().equals("nwastnum")){
		bb = getBillCardPanel().getBodyValueAt(e.getRow(),"nwastnum")==null?new UFDouble(0):(UFDouble)getBillCardPanel().getBodyValueAt(e.getRow(),"nwastnum");//ԭ����˰���
		getBillCardPanel().getBillModel().setValueAt(bb.div(convert),e.getRow(),"nwastassistnum");
	}else if(e.getKey().equals("nwastassistnum")){
		bb = getBillCardPanel().getBodyValueAt(e.getRow(),"nwastassistnum")==null?new UFDouble(0):(UFDouble)getBillCardPanel().getBodyValueAt(e.getRow(),"nwastassistnum");//ԭ����˰���
		getBillCardPanel().getBillModel().setValueAt(bb.multiply(convert),e.getRow(),"nwastnum");
	}

}

/**
 * ��������ֶμ���Ʒ��־�ֶα༭���¼�����
 * @param e
 * @since v50
 * @author czp 
 * @date 2006-10-09
 */
private void afterEditWhenBodyLargessNums(int iRow){

  UFBoolean bLargessUpRow = (UFBoolean) PuPubVO.getUFBoolean_NullAs(getBillCardPanel().getBillModel().getValueAt(iRow,"blargessuprow"), UFBoolean.FALSE);
  if(bLargessUpRow.booleanValue()){
    getBillCardPanel().getBillModel().setValueAt(new UFBoolean(true),iRow,"blargess");
  }
  UFBoolean bLargess = (UFBoolean) PuPubVO.getUFBoolean_NullAs(getBillCardPanel().getBillModel().getValueAt(iRow,"blargess"), UFBoolean.FALSE);
  if(bLargess.booleanValue()){
    Object oTemp = getBillCardPanel().getBillModel().getValueAt(iRow,"narrvnum");
    getBillCardPanel().getBillModel().setValueAt(oTemp,iRow,"npresentnum");
    oTemp = getBillCardPanel().getBillModel().getValueAt(iRow,"nassistnum");
    getBillCardPanel().getBillModel().setValueAt(oTemp,iRow,"npresentassistnum");     
  }else{
    getBillCardPanel().getBillModel().setValueAt(null,iRow,"npresentnum");
    getBillCardPanel().getBillModel().setValueAt(null,iRow,"npresentassistnum");            
  } 
}
/**
 * ������༭�¼�
 * �������ڣ�(2001-11-28 12:13:08)
 * @param e nc.ui.pub.bill.BillEditEvent
 */
private void afterEditFree(BillEditEvent e) {
  if (e.getPos() != 1 || !e.getKey().equals("vfree0"))
    return;
  if (getBillCardPanel().getBodyItem("vfree0") != null) {
    FreeVO freeVO = ((FreeItemRefPane) getBillCardPanel().getBodyItem("vfree0").getComponent()).getFreeVO();
    if (freeVO == null) {
      for (int i = 0; i < 5; i++) {
        String str = "vfree" + new Integer(i + 1).toString();
        getBillCardPanel().setBodyValueAt(null, e.getRow(), str);
      }
    } else {
      for (int i = 0; i < 5; i++) {
        String strName = "vfreename" + new Integer(i + 1).toString();
        if (freeVO.getAttributeValue(strName) != null) {
          String str = "vfree" + new Integer(i + 1).toString();
          Object ob = freeVO.getAttributeValue(str);
          getBillCardPanel().setBodyValueAt(ob, e.getRow(), str);
        }
      }
    }
  }
}

/**
 * �������༭�¼�
 * ����
    # �����������༭ʱ�������� ==>> ������       |
    # ѡȡ�ļ���ID��������ID���任����Ϊ1���̶�������    |
    # �ɡ������ʡ�������ʽ����               > ����ɾ�����ϸ������Ƿ�ɱ༭
    # ͬ�����µ���ģ���еĻ����ʺ��Ƿ�̶�����������   |
    # ���»����������пɱ༭��
 * @param e nc.ui.pub.bill.BillEditEvent
 */
private void afterEditWhenAssistUnit(BillEditEvent e) {
  //�Ƿ�̶��仯�ʸı�ʱҪͬ�����ģ�strKeys[0]��ֵ
  UFBoolean isfixed = new UFBoolean(true);
  UFDouble convert = new UFDouble(0);
  //���ID
  String sBaseID =
    (String) getBillCardPanel().getBillModel().getValueAt(e.getRow(), "cbaseid");
  //����������
  String sCassId =
    (String) getBillCardPanel().getBillModel().getValueAt(
      e.getRow(),
      "cassistunit");
  if (sCassId == null || sCassId.trim().equals("")) {
    UIRefPane refAss =
      (UIRefPane) getBillCardPanel().getBodyItem("cassistunitname").getComponent();
    sCassId = refAss.getRefPK();
    String sCassName = refAss.getRefName();
    getBillCardPanel().getBillModel().setValueAt(
      sCassId,
      e.getRow(),
      "cassistunit");
    getBillCardPanel().getBillModel().setValueAt(
      sCassName,
      e.getRow(),
      "cassistunitname");
  }
  //��ȡ������
  convert = nc.ui.pu.pub.PuTool.getInvConvRateValue(sBaseID, sCassId);
  //�Ƿ�̶�������
  isfixed = new UFBoolean(PuTool.isFixedConvertRate(sBaseID, sCassId));
  //�����������༭
  if (e.getKey().equals("cassistunitname")) { //���ø���������
    setRefPaneAssistunit(e.getRow());
    //���ø���Ϣ
    setAssisUnitEditState2(e);
    convert = nc.ui.pu.pub.PuTool.getInvConvRateValue(sBaseID, sCassId);
    getBillCardPanel().getBillModel().setValueAt(
      convert,
      e.getRow(),
      "convertrate");
    isfixed = new UFBoolean(PuTool.isFixedConvertRate(sBaseID, sCassId));
    if (isfixed.booleanValue())
      m_saKey[0] = "Y";
    else
      m_saKey[0] = "N";
    //�û������������㣺�������������������ϸ����������ϸ����������ۣ����
    e.setKey("convertrate");
    RelationsCal.calculate(getBillCardPanel(), e, getBillCardPanel().getBillModel(),
      "convertrate",
      m_iDescriptions,
      m_saKey, ArriveorderItemVO.class.getName());
  }
}
/**
 * ��ͷ�༭���¼�-�ɹ�Ա
 * @param e
 */
private void afterEditWhenHeadEmployee(BillEditEvent e){

  Logger.info("����afterEditWhenHeadEmployee()");/*-=notranslate=-*/
  
    String sPsnId = ((UIRefPane) getBillCardPanel().getHeadItem("cemployeeid").getComponent()).getRefPK();
    
  Logger.info("sPsnId = ((UIRefPane) getBillCardPanel().getHeadItem(��cemployeeid��).getComponent()).getRefPK()="+sPsnId);/*-=notranslate=-*/
    
  if (PuPubVO.getString_TrimZeroLenAsNull(sPsnId) == null) {
    
    Logger.info("PuPubVO.getString_TrimZeroLenAsNull(sPsnId) == null ��true");/*-=notranslate=-*/
        
    return;
    }
    //��ҵ��Ա����Ĭ�ϲ���
    UIRefPane ref = (UIRefPane) (getBillCardPanel().getHeadItem("cemployeeid").getComponent());
  
    Logger.info("getBillCardPanel().getHeadItem(��cemployeeid��).getComponent()��"+ref.toString());/*-=notranslate=-*/
    
    //ҵ��Ա��������
    Object sDeptId = ref.getRefModel().getValue("bd_psndoc.pk_deptdoc");
  
    Logger.info("ref.getRefModel().getValue(��bd_psndoc.pk_deptdoc��)"+sDeptId);/*-=notranslate=-*/
    
    getBillCardPanel().getHeadItem("cdeptid").setValue(sDeptId);
  
    Logger.info("�ӷ���afterEditWhenHeadEmployee()��������");/*-=notranslate=-*/
}

/**
 * ����༭�¼�
 * �������ڣ�(2001-11-28 12:13:08)
 * @param e nc.ui.pub.bill.BillEditEvent
 */
private void afterEditWhenInv(BillEditEvent e) {
  //�ı���ʱ,���������,������,����,����,���ȵ������Ϣ
  getBillCardPanel().setBodyValueAt(null, e.getRow(), "narrvnum");
  getBillCardPanel().setBodyValueAt(null, e.getRow(), "nassistnum");
  getBillCardPanel().setBodyValueAt(null, e.getRow(), "nprice");
  getBillCardPanel().setBodyValueAt(null, e.getRow(), "nmoney");
  getBillCardPanel().setBodyValueAt(null, e.getRow(), "nelignum");
  getBillCardPanel().setBodyValueAt(null, e.getRow(), "nnotelignum");
  getBillCardPanel().setBodyValueAt(null, e.getRow(), "vfree0");
  getBillCardPanel().setBodyValueAt(null, e.getRow(), "vfree1");
  getBillCardPanel().setBodyValueAt(null, e.getRow(), "vfree2");
  getBillCardPanel().setBodyValueAt(null, e.getRow(), "vfree3");
  getBillCardPanel().setBodyValueAt(null, e.getRow(), "vfree4");
  getBillCardPanel().setBodyValueAt(null, e.getRow(), "vfree5");
  getBillCardPanel().setBodyValueAt(null, e.getRow(), "dproducedate");
  getBillCardPanel().setBodyValueAt(null, e.getRow(), "ivalidday");
  getBillCardPanel().setBodyValueAt(null, e.getRow(), "dvaliddate");
  getBillCardPanel().setBodyValueAt(null, e.getRow(), "npresentnum");
  getBillCardPanel().setBodyValueAt(null, e.getRow(), "nwastnum");
  getBillCardPanel().setBodyValueAt(null, e.getRow(), "vproducenum");
  String[] aryAssistunit =
    new String[] {
      "cassistunit->getColValue(bd_invbasdoc,pk_measdoc2,pk_invbasdoc,cbaseid)",
      "cassistunitname->getColValue(bd_measdoc,measname,pk_measdoc,cassistunit)" };
  getBillCardPanel().getBillModel().execFormulas(e.getRow(), aryAssistunit);
  //����������
  setAssisUnitEditState2(e);
  
  //������κŹ�����
  String strCmangid = (String) getBillCardPanel().getBillModel().getValueAt(e.getRow(), "cmangid");
  if (PuTool.isBatchManaged(strCmangid))
    getBillCardPanel().setCellEditable(e.getRow(), "vproducenum", getBillCardPanel().getBodyItem("vproducenum").isEdit());
  else
    getBillCardPanel().setCellEditable(e.getRow(), "vproducenum", false);
  
  String strCbaseid = (String) getBillListPanel().getBodyBillModel().getValueAt(e.getRow(), "cbaseid");
  String strCassid = (String) getBillListPanel().getBodyBillModel().getValueAt(e.getRow(), "cassistunit");

  // �Ƿ�̶�������
  if(PuTool.isFixedConvertRate(strCbaseid, strCassid)){
    getBillListPanel().getBodyBillModel().setValueAt(new UFBoolean(true), e.getRow(), "isfixedrate");
  }


}

/**
 * �����༭�¼�
 * �������ڣ�(2001-11-28 12:13:08)
 * @param e nc.ui.pub.bill.BillEditEvent
 */
private void afterEditWhenNum(BillEditEvent e) {
  if(e.getKey().equals("narrvnum")){
    BillItem item = getBillCardPanel().getHeadItem("bisback");
    UFBoolean bBack = new UFBoolean(false);
    if(item != null) bBack = new UFBoolean(item.getValue());
    if(bBack.booleanValue()){
      Object oTemp = getBillCardPanel().getBodyValueAt(e.getRow(),"narrvnum");
      if(oTemp != null){
        UFDouble d = new UFDouble(oTemp.toString());
        if(d.doubleValue() > 0){
          getBillCardPanel().setBodyValueAt(e.getOldValue(), e.getRow(), "narrvnum");
          MessageDialog.showErrorDlg(this,m_lanResTool.getStrByID("SCMCOMMON","UPPSCMCommon-000059")/*@res "����"*/,m_lanResTool.getStrByID("40040301","UPP40040301-000275")/*@res"�˻�����������Ϊ��!"*/);
          //2010-10-13 MeiChao (��ɽ-����) ----begin  
          //�޸ĵ�������ʵ���������޸�ʱ.�����Ƿ�̶�������,�޸Ķ�Ӧʵ��������,(��ǰBUGΪʵ�������޸�,��Ӧʵ�����������仯)
          //2010-10-13 MeiChao (��ɽ-����) ----end  
          
          return;
        }
      }
    }
    //add by QuSida 2010-9-2 (��ɽ����)  --- begin
    //function ���ѵ��������޸ĺ�ʱ���·�����Ϣ�е�����
    int temp = getBillCardPanel().getBillModel("table").getRowCount();
//  UFDouble taxmny = null;
    UFDouble mny = null; 
    arrnumber = new UFDouble(0.0);
    for (int i = 0; i < temp; i++) {
    	arrnumber = arrnumber.add(new UFDouble((getBillCardPanel().getBodyValueAt(i,"narrvnum")==null?0:getBillCardPanel().getBodyValueAt(i,"narrvnum")).toString()));    
	}
    temp = getBillCardPanel().getBillModel("jj_scm_informationcost").getRowCount();
    //����ۼƵ�������
//    IUAPQueryBS query = NCLocator.getInstance().lookup(IUAPQueryBS.class);
//    String sql = "select sum(naccumarrvnum) from po_order_b where dr = 0 and corderid = '"+vos[0].getCbillid()+"'";
//    Object o = null;
//    try {
//		 o = query.executeQuery(sql, new ColumnProcessor());
//	} catch (BusinessException e1) {
//		// TODO Auto-generated catch block
//		e1.printStackTrace();
//		return;
//	}
	UFDouble arrmny = null;
	int length = 0;
//	if(vos!=null) length = vos.length;
    for (int i = length; i < temp; i++) {
    	Boolean ismny = (Boolean)getBillCardPanel().getBillModel("jj_scm_informationcost").getValueAt(i, "ismny");
    	
    	getBillCardPanel().getBillModel("jj_scm_informationcost").setValueAt(arrnumber, i, "nnumber");
    	

    	if(ismny == null || !ismny){
    	mny = new UFDouble(getBillCardPanel().getBillModel("jj_scm_informationcost").getValueAt(i, "noriginalcurprice").toString()).multiply(arrnumber);
    	//�ۼƵ������
    	arrmny  = new UFDouble(getBillCardPanel().getBillModel("jj_scm_informationcost").getValueAt(i, "noriginalcurprice").toString()).multiply(arrnumber.add(arrnum));
        
//   	taxmny = new UFDouble(getBillCardPanel().getBillModel("jj_scm_informationcost").getValueAt(i, "noriginalcurtaxprice").toString()).multiply(arrnumber);
    	
//    	getBillCardPanel().getBillModel("jj_scm_informationcost").setValueAt(taxmny, i, "noriginalcursummny");
    	getBillCardPanel().getBillModel("jj_scm_informationcost").setValueAt(mny, i, "noriginalcurmny");
    	
//   	getBillCardPanel().getBillModel("jj_scm_informationcost").setValueAt(taxmny, i, "ninvoriginalcursummny");
    	getBillCardPanel().getBillModel("jj_scm_informationcost").setValueAt(arrmny, i, "ninvoriginalcurmny");
    	}
    	else{
    	UFDouble price =  new UFDouble(getBillCardPanel().getBillModel("jj_scm_informationcost").getValueAt(i, "noriginalcurmny").toString()).div(arrnumber);
    		getBillCardPanel().getBillModel("jj_scm_informationcost").setValueAt(price, i, "noriginalcurprice");	
    
    	}
    	
    	//�޸Ļ���VO
//    	if(vos!=null&&i<vos.length){
//    		
////    		vos[i].setAttributeValue("noriginalcursummny", taxmny);
//    		vos[i].setAttributeValue("noriginalcurmny", mny);
////   		vos[i].setAttributeValue("ninvoriginalcursummny", taxmny);
//    		vos[i].setAttributeValue("ninvoriginalcurmny", arrmny);
//    		vos[i].setAttributeValue("nnumber", arrnumber);
//    	}
    	
        
    }
    //add by QuSida 2010-9-2 (��ɽ����)  --- end
    
  }
  
  UFBoolean isfixed = new UFBoolean(true);
  //���ID
  String sBaseID =
    (String) getBillCardPanel().getBillModel().getValueAt(e.getRow(), "cbaseid");
  //����������
  String sCassId =
    (String) getBillCardPanel().getBillModel().getValueAt(
      e.getRow(),
      "cassistunit");
  if (sCassId == null || sCassId.trim().equals("")) {
    UIRefPane refAss =
      (UIRefPane) getBillCardPanel().getBodyItem("cassistunitname").getComponent();
    sCassId = refAss.getRefPK();
    String sCassName = refAss.getRefName();
    getBillCardPanel().getBillModel().setValueAt(
      sCassId,
      e.getRow(),
      "cassistunit");
    getBillCardPanel().getBillModel().setValueAt(
      sCassName,
      e.getRow(),
      "cassistunitname");
  }
  //��ȡ������
  //convert = nc.ui.pu.pub.PuTool.getInvConvRateValue(sBaseID, sCassId);
  //�Ƿ�̶�������
  isfixed = new UFBoolean(PuTool.isFixedConvertRate(sBaseID, sCassId));
  
  //�Զ����㣺�����������������������ʣ��ϸ����������ϸ����������ۣ����
  if ((e.getKey().equals("convertrate")
    || e.getKey().equals("nassistnum")
    || e.getKey().equals("narrvnum")
    || e.getKey().equals("nprice")
    || e.getKey().equals("nmoney")
    || e.getKey().equals("nelignum")
    || e.getKey().equals("nnotelignum"))) {
    //������ݺϷ��ԣ����Ϸ��ָ�ԭֵ����
    String strErr = getErrMsg(e);
    if (strErr != null) {
      MessageDialog.showErrorDlg(this, m_lanResTool.getStrByID("40040301","UPP40040301-000085")/*@res "���ݴ���"*/, m_lanResTool.getStrByID("40040301","UPP40040301-000086")/*@res "�������ݴ���\n"*/ + strErr);
      getBillCardPanel().getBillModel().setValueAt(
        e.getOldValue(),
        e.getRow(),
        e.getKey());
      return;
    }
    if (isfixed.booleanValue())
      m_saKey[0] = "Y";
    else
      m_saKey[0] = "N";
    RelationsCal.calculate(getBillCardPanel(), e, getBillCardPanel().getBillModel(),m_iDescriptions, m_saKey, ArriveorderItemVO.class.getName());
    
    //ֻ�������͸������༭�������������
    if (e.getKey().equals("nassistnum") || e.getKey().equals("narrvnum"))
      setEditAndDirect(e);
  }
}

/**
 * �������ڱ༭�¼�
 * ������������ + ���������� = ʧЧ����(���ɱ༭)
 *   ע���������ڻ���������һ��Ϊ����ʧЧ����Ϊ��
 */
private void afterEditWhenProdDate(BillEditEvent e) {
  BillModel bm = getBillCardPanel().getBillModel();
  /**��ȡ��ǰ�༭������VO -- item ,ע�⣺�����ڳ�ʼ��ʱ�ɷ������˴��ݹ�������Ϊ�����Ƕ���ת��ĵ���*/
  ArriveorderItemVO item =
    (ArriveorderItemVO) bm.getBodyValueRowVO(
      e.getRow(),
      ArriveorderItemVO.class.getName());
  Object dproducedate = bm.getValueAt(e.getRow(), "dproducedate");
  if (dproducedate == null
    || dproducedate.toString().trim().equals("")
    || item.getIvalidday() == null
    || item.getIvalidday().toString().trim().equals("")) {
    item.setDvaliddate(null);
    bm.setValueAt(null, e.getRow(), "dvaliddate");
  } else {
    UFDate dvaliddate =
      item.getDproducedate().getDateAfter(item.getIvalidday().intValue());
    //ʧЧ����(���ɱ༭)
    bm.setValueAt(dvaliddate, e.getRow(), "dvaliddate");
    item.setDvaliddate(dvaliddate);
  }
}

/**
 * ���ű༭�¼�
 */
private void afterEditWhenProdNum(BillEditEvent e) {
  try {
	  
	  if (m_PnlLotRef == null) {
		  return;
	  }
    int iRow = e.getRow();
	  BillModel bm = getBillCardPanel().getBillModel();
	  Object vproducenum = bm.getValueAt(iRow, "vproducenum");
	  if (vproducenum == null || vproducenum.toString().trim().equals("")) {
		  bm.setValueAt(null, iRow, "dproducedate");
		  bm.setValueAt(null, iRow, "dvaliddate");
		  return;
	  }
//	  UFDate dateValid = m_PnlLotRef.getRefInvalideDate();
	  UFDate dateValid = new UFDate(System.currentTimeMillis());

	  //Object dproducedate = bm.getValueAt(e.getRow(), "dproducedate");
	  Object ivalidday = bm.getValueAt(iRow, "ivalidday");
	  int iDays = 0;
	  if (ivalidday == null || ivalidday.toString().trim().equals("")) {
		  iDays = 0;
	  } else {
		  iDays = Integer.parseInt(ivalidday.toString().trim());
	  }
	  //�������ڣ�ʧЧ���ڣ�����������
	  UFDate dateProduce = dateValid.getDateBefore(iDays);
	  bm.setValueAt(dateProduce, iRow, "dproducedate");
	  bm.setValueAt(dateValid, iRow, "dvaliddate");

    //���ÿ��ӿڣ���ȡ���ε�����Ϣ
    RcTool.setBatchCodeInfo(getBillCardPanel(), e
        .getRow(), (String) getBillCardPanel().getBodyValueAt(iRow,
            "cmangid"), (String) getBillCardPanel().getBodyValueAt(iRow,
            "cbaseid"), (String) getBillCardPanel().getBodyValueAt(iRow,
            "vproducenum"), getCorpPrimaryKey());
    
  } catch (Exception ex) {
    SCMEnv.out("���ű༭������������ڳ����쳣����ϸ��Ϣ���£�");
    reportException(ex);
    showHintMessage("���ű༭������������ڳ����쳣����ϸ��Ϣ���£�" + ex);
  }


}

/**
 * ��Ŀ�༭�¼�
 * @param e nc.ui.pub.bill.BillEditEvent
 */
private void afterEditWhenProject(BillEditEvent e) {
  int row = e.getRow();
  //��Ŀ
  Object o = getBillCardPanel().getBillModel().getValueAt(row, "cprojectid");
  if (o != null && o.toString().trim().length() > 0) {
    String cprojectid = o.toString();
    PuProjectPhaseRefModel refjobphase = new PuProjectPhaseRefModel(getCorpPrimaryKey(), cprojectid);
    ((UIRefPane) (getBillCardPanel().getBodyItem("cprojectphase").getComponent())).setIsCustomDefined(true);
    ((UIRefPane) (getBillCardPanel().getBodyItem("cprojectphase").getComponent())).setRefModel(refjobphase);
    //������Ŀ�׶β��ɱ༭
    getBillCardPanel().setCellEditable(row, "cprojectphase", getBillCardPanel().getBodyItem("cprojectphase").isEdit());
  } else {
    getBillCardPanel().getBillModel().setValueAt(null, row, "cprojectphase");
    getBillCardPanel().getBillModel().setValueAt(null, row, "cprojectphasebaseid");
    getBillCardPanel().getBillModel().setValueAt(null, row, "cprojectphaseid");
    //������Ŀ�׶β��ɱ༭
    getBillCardPanel().setCellEditable(row, "cprojectphase", false);
  }
}

/**
 * �����������༭�¼�
 * ������������ + ���������� = ʧЧ����(���ɱ༭)
 *   ע���������ڻ���������һ��Ϊ����ʧЧ����Ϊ��
 */
private void afterEditWhenValidDays(BillEditEvent e) {
  BillModel bm = getBillCardPanel().getBillModel();
  /**��ȡ��ǰ�༭������VO -- item ,ע�⣺�����ڳ�ʼ��ʱ�ɷ������˴��ݹ�������Ϊ�����Ƕ���ת��ĵ���*/
  ArriveorderItemVO item =
    (ArriveorderItemVO) bm.getBodyValueRowVO(
      e.getRow(),
      ArriveorderItemVO.class.getName());
  //����������
  Object ivalidday = bm.getValueAt(e.getRow(), "ivalidday");
  if (item.getDproducedate() == null
    || item.getDproducedate().toString().trim().equals("")
    || ivalidday == null
    || ivalidday.toString().trim().equals("")) {
    item.setDvaliddate(null);
    bm.setValueAt(null, e.getRow(), "dvaliddate");
  } else {
    UFDate dvaliddate =
      item.getDproducedate().getDateAfter(item.getIvalidday().intValue());
    //ʧЧ����(���ɱ༭)
    bm.setValueAt(dvaliddate, e.getRow(), "dvaliddate");
    item.setDvaliddate(dvaliddate);
  }
}

/**
 * �ֿ�༭�¼�
 * �������ڣ�(2001-11-28 12:13:08)
 * @param e nc.ui.pub.bill.BillEditEvent
 */
private void afterEditWhenWareHouse(BillEditEvent e) {
  //BillModel bm = getArrBillCardPanel().getBillModel();
  ////���²ֿ��Ƿ��λ������
  //String cwarehouseid = (String) bm.getValueAt(e.getRow(), "cwarehouseid");
  //UFBoolean isAllot = null;
  //if (cwarehouseid != null && !cwarehouseid.trim().equals("")) { //�ı��λ����ģ��
    //UIRefPane refCargDoc =
      //(UIRefPane) getArrBillCardPanel().getBodyItem("cstorename").getComponent();
    //refCargDoc.getRefModel().addWherePart(
      //"and bd_cargdoc.pk_stordoc = '"
        //+ cwarehouseid
        //+ "' and  UPPER(bd_cargdoc.sealflag) <> 'Y' ");
    //if (m_hIsAllot == null)
      //m_hIsAllot = new Hashtable();
    //if (!m_hIsAllot.containsKey(cwarehouseid)) {
      //try {
        //ArrayList ary = ArriveorderBO_Client.getStorFlags(cwarehouseid);
        //m_hIsAllot.put(cwarehouseid, ary);
        //isAllot = (UFBoolean) ary.get(0);
        ////if (isAllot.booleanValue()) {
          ////m_btnAllotCarg.setEnabled(true);
          ////updateButton(m_btnAllotCarg);
        ////} else {
          ////m_btnAllotCarg.setEnabled(false);
          ////updateButton(m_btnAllotCarg);
        ////}
      //} catch (Exception exx) {
        //reportException(exx);
        //SCMEnv.out("afterEdit()");
      //}
    //} else {
      //isAllot = (UFBoolean) ((ArrayList) m_hIsAllot.get(cwarehouseid)).get(0);
      //if (isAllot.booleanValue()) {
        //m_btnAllotCarg.setEnabled(true);
        //updateButton(m_btnAllotCarg);
      //} else {
        //m_btnAllotCarg.setEnabled(false);
        //updateButton(m_btnAllotCarg);
      //}
    //}
  //} else {
    //m_btnAllotCarg.setEnabled(false);
    //updateButton(m_btnAllotCarg);
  //}
}

/**
 * ���������෴�༭�¼�
 * �������ڣ�(2001-11-28 12:13:08)
 * @param e nc.ui.pub.bill.BillEditEvent
 */
private void afterSignChged(BillEditEvent e) {
  UFDouble ufdOld = new UFDouble(e.getOldValue().toString().trim());
  UFDouble ufdNew = new UFDouble(e.getValue().toString().trim());
  if (ufdOld.multiply(ufdNew).doubleValue() < 0) {
    getBillCardPanel().setBodyValueAt(null, e.getRow(), "npresentnum");
    getBillCardPanel().setBodyValueAt(null, e.getRow(), "nwastnum");
  }
  return;
}

/**
 * @���ܣ����뵱ǰ�����ת�뵽��������¼���е��ݵĻ�����
 */
private void appArriveorderVOSaved(ArriveorderVO voSaved) {
  if (voSaved == null)
    return;
  /*���´�������� m_VOsAll*/
  if (m_VOsAll == null) {
    m_VOsAll = new ArriveorderVO[] { voSaved };
    return;
  }
  ArriveorderVO[] saVOTmp = new ArriveorderVO[m_VOsAll.length + 1];
  for (int i = 0; i < m_VOsAll.length; i++) {
    saVOTmp[i] = m_VOsAll[i];
  }
  saVOTmp[saVOTmp.length - 1] = voSaved;
  m_VOsAll = saVOTmp;
}

/**
 * �༭ǰ����
 */
public boolean beforeEdit(nc.ui.pub.bill.BillEditEvent e) {
  if (e.getKey().equals("vfree0")) {
    //�������
    boolean bCanEdit = PuTool.beforeEditInvBillBodyFree(
      getBillCardPanel(),
      e,
      new String[] {
        "cmangid",
        "cinventorycode",
        "cinventoryname",
        "cinventoryspec",
        "cinventorytype" },
      new String[] { "vfree0", "vfree1", "vfree2", "vfree3", "vfree4", "vfree5" });
    return  bCanEdit;
  }
    else if (e.getKey().equals("narrvnum") || e.getKey().equals("vfree0") || e.getKey().equals("vfree1") || e.getKey().equals("vfree2") || e.getKey().equals("vfree3") || e.getKey().equals("vfree4") || e.getKey().equals("vfree5")) {
      //��������ܱ༭
      UFDouble nArr = PuPubVO.getUFDouble_NullAsZero(getBillCardPanel().getBillModel().getValueAt(e.getRow(), "narrvnum"));
      UFDouble nAcc = PuPubVO.getUFDouble_NullAsZero(getBillCardPanel().getBillModel().getValueAt(e.getRow(), "naccumchecknum"));
      if (nArr == null || nArr.doubleValue() < 0)// �������������ͬ����족��������Ϊ������
        return true;
      if (!(nAcc == null || nAcc.doubleValue() == 0))// �Ƿ���ڡ�����족���������
        return false;
  } else if (e.getKey().equalsIgnoreCase("vproducenum")) {
    //���κŴ���
    return beforeEditProdNum(e);
    //return true;
  } else if (e.getKey().equals("cprojectphase")) {
    Object oTmp =
      getBillCardPanel().getBillModel().getValueAt(e.getRow(), "cproject");
    if (oTmp == null || oTmp.toString().trim().equals(""))
      return false;
  }
    //�ֿ�
    else if (e.getKey().equals("cwarehousename")) {
        ((UIRefPane) getBillCardPanel().getBodyItem("cwarehousename").getComponent()).setPk_corp(getCorpPrimaryKey());
        PuTool.restrictWarehouseRefByStoreOrg(getBillCardPanel(), 
            getCorpPrimaryKey(),
        getBillCardPanel().getHeadItem("cstoreorganization").getValue(),
        "cwarehousename");

        UIRefPane refStore = (UIRefPane) getBillCardPanel().getBodyItem("cwarehousename").getComponent();
        UIRefPane refpane = (UIRefPane)getBillCardPanel().getHeadItem("cbiztype").getComponent();
        String sBiztype = refpane.getRefPK();
        WarehouseRefModel model = (WarehouseRefModel)refStore.getRefModel();
        Object[] oRule = null;
        try{
        oRule =  (Object[])CacheTool.getCellValue("bd_busitype","pk_busitype","verifyrule" , sBiztype );
        }catch(Exception ee){
         SCMEnv.out(ee);
        }
        model.setM_pk_corp(getClientEnvironment().getCorporation().getPrimaryKey());
        model.setPk_calbody(getBillCardPanel().getHeadItem("cstoreorganization").getValue());
        //ҵ������ֱ��
        if(oRule!=null&&oRule.length>0&&"Z".equalsIgnoreCase(oRule[0].toString())){
//         refStore.setRefModel(new WarehouseRefModel(getClientEnvironment().getCorporation().getPrimaryKey(),getBillCardPanel().getHeadItem("cstoreorganization").getValue(),true));
        	model.setBDirect(true);
        }else{
//         refStore.setRefModel(new WarehouseRefModel(getClientEnvironment().getCorporation().getPrimaryKey(),getBillCardPanel().getHeadItem("cstoreorganization").getValue(),false));
        	model.setBDirect(false);
        }
      
    }
    //�����������
    else if (e.getKey().equalsIgnoreCase("cinventorycode")){ 
    return beforeEditInv(e);
    }
  //��Ŀ�׶�
    else if(e.getKey().equalsIgnoreCase("cprojectphase")){
      return beforeEditProjectPhase(e);
    }
  	//������
    else if(e.getKey().equalsIgnoreCase("convertrate")){

    	  //���ID
    	  String sBaseID =
    	    (String) getBillCardPanel().getBillModel().getValueAt(e.getRow(), "cbaseid");
    	  //����������
    	  String sCassId =
    	    (String) getBillCardPanel().getBillModel().getValueAt(
    	      e.getRow(),
    	      "cassistunit");
    	  if (sCassId == null || sCassId.trim().equals("")) {
    	    UIRefPane refAss =
    	      (UIRefPane) getBillCardPanel().getBodyItem("cassistunitname").getComponent();
    	    sCassId = refAss.getRefPK();
    	    String sCassName = refAss.getRefName();
    	    getBillCardPanel().getBillModel().setValueAt(
    	      sCassId,
    	      e.getRow(),
    	      "cassistunit");
    	    getBillCardPanel().getBillModel().setValueAt(
    	      sCassName,
    	      e.getRow(),
    	      "cassistunitname");
    	  }
    	  //�ǹ̶������ʲſ��Ա༭
    	  return !PuTool.isFixedConvertRate(sBaseID, sCassId) 
    	  	&& getBillCardPanel().getBodyItem("convertrate").isEdit();
    }
  return true;
}
private boolean beforeEditInv(BillEditEvent e){

  UFBoolean bLargessUpRow = PuPubVO.getUFBoolean_NullAs(getBillCardPanel().getBodyValueAt(e.getRow(),"blargessuprow"),UFBoolean.FALSE);
  UFBoolean bLargess = PuPubVO.getUFBoolean_NullAs(getBillCardPanel().getBodyValueAt(e.getRow(),"blargess"),UFBoolean.FALSE);
  BillModel bm = getBillCardPanel().getBillModel();
  String strBillLinKey = getStateStr().equals("ת���޸�") ? "corder_bid" : "carriveorder_bid";
  if (bm.getValueAt(e.getRow(), strBillLinKey) == null){
    SCMEnv.out("1-����������Դ�������ܻ�ȡ�������򶩵�ID��������༭������");
    return false;
  }
  if (m_hBillIDsForCmangids == null){
    SCMEnv.out("2-����������Դ�������ܻ�ȡ�������򶩵�ID��������༭������");
    return false;
  }
  String cmangid = (String) m_hBillIDsForCmangids.get(bm.getValueAt(e.getRow(), strBillLinKey));
  if (cmangid == null || cmangid.trim().equals("")){
    SCMEnv.out("3-����������Դ�������ܻ�ȡ�������򶩵�ID��������༭������");
    return false;
  }
  UIRefPane refCinventorycode = (UIRefPane) getBillCardPanel().getBodyItem("cinventorycode").getComponent();
//  refCinventorycode.setIsCustomDefined(true);
//  refCinventorycode.setRefType(IBusiType.GRID);
  InvRefModelForRepl refmodel = (InvRefModelForRepl)refCinventorycode.getRefModel();
  refmodel.setPk_invmandoc(cmangid);
  refmodel.setCmagidOrderRow(PuPubVO.getString_TrimZeroLenAsNull(getBillCardPanel().getBillModel().getValueAt(e.getRow(), "corder_bid")));
  if(!bLargessUpRow.booleanValue() && bLargess.booleanValue()){
    //�����в�����Ʒ�ҵ�����������Ʒ����������ݴ������ȡ���У�������������滻����
//    refmodel = new InvRefModelForRepl(cmangid,ClientEnvironment.getInstance().getCorporation().getPrimaryKey(),true);
	  refmodel.setBAllInv(true);
  }
  else{
    //�����������Ϊ����������滻��
//    refmodel = new InvRefModelForRepl(cmangid,ClientEnvironment.getInstance().getCorporation().getPrimaryKey(),false);
	  refmodel.setBAllInv(false);
  }
  refmodel.setMyWherePart();
//  refCinventorycode.setRefModel(refmodel);    
  return true;
}
/**
 * ���ܣ��༭���κ�ǰ�����κŲ��մ���
 * ������
 * ���أ�
 * ���⣺
 * ���ڣ�(2002-9-16 13:01:38)
 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
 * @return boolean
 * @param e nc.ui.pub.bill.BillEditEvent
 */
private boolean beforeEditProdNum(BillEditEvent e) {

    int iRow = e.getRow();
    ParaVOForBatch vo = new ParaVOForBatch();
    //����FieldName
    vo.setMangIdField("cmangid");
    vo.setInvCodeField("cinventorycode");
    vo.setInvNameField("cinventoryname");
    vo.setSpecificationField("cinventoryspec");
    vo.setInvTypeField("cinventorytype");
    vo.setMainMeasureNameField("cmainmeasname");
    vo.setAssistUnitIDField("cassistunit");
    vo.setIsAstMg(new UFBoolean(PuTool
            .isAssUnitManaged((String) getBillCardPanel().getBodyValueAt(iRow, "cbaseid"))));
    vo.setWarehouseIDField("cwarehouseid");
    vo.setFreePrefix("vfree");
    //���ÿ�Ƭģ��,��˾��
    vo.setCardPanel(getBillCardPanel());
    vo.setPk_corp(getCorpPrimaryKey());
    vo.setEvent(e);
    vo.setStoreorganization(getBillCardPanel().getHeadItem("cstoreorganization").getValue());

    try {
        m_PnlLotRef = nc.ui.pu.pub.PuTool.beforeEditWhenBodyBatch(vo,true);
    } catch (Exception exp) {
        PuTool.outException(this, exp);
    }
  BillModel bm = getBillCardPanel().getBillModel();
  String cmangid = (String) bm.getValueAt(e.getRow(), "cmangid");
  if (m_PnlLotRef == null || !PuTool.isBatchManaged(cmangid)){
    return false;
  }
    return true;
}
/**
 * �����޸��б任ʱ�������(�б任�¼�)
 */
private void bmrcSetForModify(BillEditEvent e) {
  BillModel bm = getBillCardPanel().getBillModel();
  Object obj = bm.getValueAt(e.getRow(), "naccumchecknum");
  Object objElg = bm.getValueAt(e.getRow(), "nelignum");
  Object objNotElg = bm.getValueAt(e.getRow(), "nnotelignum");
  /*���ɱ༭�߼�(������һ)��
   *1)��  �ʼ�����  != 0 ���� ���޸��С��������С�������С��ϸ������Ǹ������ϸ������Ǹ�
   *2)��  �ϸ�����  != 0 ���� ���޸��С��������С�������С��ϸ������Ǹ������ϸ������Ǹ�
   *3)�����ϸ�����  != 0 ���� ���޸��С��������С�������С��ϸ������Ǹ������ϸ������Ǹ�
  */
  if ((obj != null
    && !obj.toString().trim().equals("")
    && !(new UFDouble(obj.toString().trim()).compareTo(new UFDouble(0)) == 0)
    || objElg != null
    && !objElg.toString().trim().equals("")
    && !(new UFDouble(objElg.toString().trim()).compareTo(new UFDouble(0)) == 0)
    || objNotElg != null
    && !objNotElg.toString().trim().equals("")
    && !(new UFDouble(objNotElg.toString().trim()).compareTo(new UFDouble(0)) == 0))
    && bm.getRowState(e.getRow()) != BillModel.MODIFICATION
    && bm.getRowState(e.getRow()) != BillModel.ADD
    && !isCheckFree(e)
    && !((objElg != null && new UFDouble(objElg.toString().trim()).compareTo(new UFDouble(0)) < 0) || (objNotElg != null && new UFDouble(objNotElg.toString().trim()).compareTo(new UFDouble(0)) < 0))) {
    //��ť�߼�
    //m_btnDelLine.setEnabled(false);
    setBtnLines(false);
    //updateButton(m_btnDelLine);
    //m_btnAllotCarg.setEnabled(false);
    //updateButton(m_btnAllotCarg);
    //���пɱ༭������Ϊ���ɱ༭
    getBillCardPanel().setCellEditable(e.getRow(), "cinventorycode", false);
    getBillCardPanel().setCellEditable(e.getRow(), "convertrate", false);
    getBillCardPanel().setCellEditable(e.getRow(), "cassistunitname", false);
    getBillCardPanel().setCellEditable(e.getRow(), "nassistnum", false);
    getBillCardPanel().setCellEditable(e.getRow(), "narrvnum", false);
    getBillCardPanel().setCellEditable(e.getRow(), "nprice", false);
    getBillCardPanel().setCellEditable(e.getRow(), "nmoney", false);
    getBillCardPanel().setCellEditable(e.getRow(), "nelignum", false);
    getBillCardPanel().setCellEditable(e.getRow(), "npresentnum", false);
    getBillCardPanel().setCellEditable(e.getRow(), "nwastnum", false);
    getBillCardPanel().setCellEditable(e.getRow(), "cwarehousename", false);
    getBillCardPanel().setCellEditable(e.getRow(), "cstorename", false);
    getBillCardPanel().setCellEditable(e.getRow(), "vproducenum", false);
    getBillCardPanel().setCellEditable(e.getRow(), "dproducedate", false);
    getBillCardPanel().setCellEditable(e.getRow(), "ivalidday", false);
    getBillCardPanel().setCellEditable(e.getRow(), "vmemo", false);
    if (getBillCardPanel().getBodyItem("vbackreasonb") != null)
      getBillCardPanel().setCellEditable(e.getRow(), "vbackreasonb", false);
    getBillCardPanel().setCellEditable(e.getRow(), "vfree0", false);
    getBillCardPanel().setCellEditable(e.getRow(), "vdef1", false);
    getBillCardPanel().setCellEditable(e.getRow(), "vdef2", false);
    getBillCardPanel().setCellEditable(e.getRow(), "vdef3", false);
    getBillCardPanel().setCellEditable(e.getRow(), "vdef4", false);
    getBillCardPanel().setCellEditable(e.getRow(), "vdef5", false);
    getBillCardPanel().setCellEditable(e.getRow(), "vdef6", false);
    getBillCardPanel().setCellEditable(e.getRow(), "cproject", false);
    getBillCardPanel().setCellEditable(e.getRow(), "cprojectphase", false);
  } else {
    //û�б����찴ť�߼�
    //m_btnDelLine.setEnabled(true);
    setBtnLines(true);
    //updateButton(m_btnDelLine);
  }
}
/**
 * ���ܣ�������Ŀ�׶β���
 */
private boolean beforeEditProjectPhase(BillEditEvent e) {
  int row = e.getRow();
  if (row < 0){
    return false;
  }
  getBillCardPanel().stopEditing();
  //��Ŀ
  Object o = getBillCardPanel().getBillModel().getValueAt(row, "cprojectid");
  Object pk_corp =
    getBillCardPanel().getBillModel().getValueAt(row, "pk_corp");
  String cprojectid = null;
  //��Ŀ�׶β���
  if ((o != null) && (!o.toString().trim().equals(""))) {
    cprojectid = o.toString().trim();
    PuProjectPhaseRefModel refjobphase =
      new PuProjectPhaseRefModel(pk_corp.toString(), cprojectid);
    (
      (UIRefPane)
        (
          getBillCardPanel()
            .getBodyItem("cprojectphase")
            .getComponent()))
            .setIsCustomDefined(
      true);
    (
      (UIRefPane)
        (
          getBillCardPanel()
            .getBodyItem("cprojectphase")
            .getComponent()))
            .setRefModel(
      refjobphase);
    return true;
  } else {
    return false;
  }
}

/**
 * �ֿ��Ƿ���л�λ����(�б任�¼�)
 */
private void bmrcSetForWareAlot(BillEditEvent e) {
  /*
  BillModel bm = getArrBillCardPanel().getBillModel();
  String cwarehouseid = (String) bm.getValueAt(e.getRow(), "cwarehouseid");
  if (cwarehouseid == null || cwarehouseid.trim().equals("")) {
    m_btnAllotCarg.setEnabled(false);
    updateButton(m_btnAllotCarg);
  } else {
    ArrayList ary1 = (ArrayList) m_hIsAllot.get(cwarehouseid);
    UFBoolean isAllot = (UFBoolean) ary1.get(0);
    if (isAllot.booleanValue()) {
      m_btnAllotCarg.setEnabled(true);
      updateButton(m_btnAllotCarg);
    } else {
      m_btnAllotCarg.setEnabled(false);
      updateButton(m_btnAllotCarg);
    }
    //���˻�λ
    UIRefPane refCarg =
      (UIRefPane) getArrBillCardPanel().getBodyItem("cstorename").getComponent();
    refCarg.getRefModel().addWherePart(
      "and bd_cargdoc.pk_stordoc = '"
        + cwarehouseid
        + "' and  UPPER(bd_cargdoc.sealflag) <> 'Y' ");
  }
  */
}
/**
 * ���ܣ��иı�
 1.�����б�
 2.ת���б�
 3.�����޸ļ�ת���޸�
 */
public void bodyRowChange(BillEditEvent e) {
    if (getStateStr().equals("�����б�")) {
      bodyRowChangeLookList(e);
    }
    else
    if (getStateStr().equals("ת���޸�") || getStateStr().equals("�����޸�")) {
      bodyRowChangeEdit(e);
    }
}

/**
 * ���ܣ������иı�ʱ�Ĵ���(�����޸ļ�ת���޸�)
 */
private void bodyRowChangeEdit(BillEditEvent e) {
  /**���úϸ������༭������������������*/
  setEditAndDirect(e);
  /**�Ƿ���ʾ�����ť(�Ƿ����������)*/
  //bmrcSetForFree(e); V31 czp del ͳһ�ڱ༭ǰ����
  /**���ø�������Ϣ*/
  setAssisUnitEditState2(e);
  /**����滻��������*/
//  bmrcSetForInvRef(e);V5�ƶ����༭ǰ�¼�
  /**�ֿ⣺�Ƿ���л�λ����*/
  bmrcSetForWareAlot(e);
  /**�����޸�ʱ�������*/
  if (getStateStr().equals("�����޸�")) {
    bmrcSetForModify(e);
  }
  //��Ƭ�༭ʱ������ʱ��Ҫ�������¼�
  if(e.getSource().getClass().getName().equals("nc.ui.pub.bill.BillModelRowEditPanel")){
	  beforeEditInv(e);
  }
  /**����Ƿ����κŹ���*/
//  bmrcSetForProdNum(e);V5�ƶ����༭ǰ�¼�
  /**��Ŀ��������*/ 
//  bmrcSetForRefPaneProject(e);V5�ƶ����༭ǰ�¼�

  //�����˵��Ҽ�����Ȩ�޿���
//  rightButtonRightControl();
}
/**
 * ����б��б任ʱ����
 * �������ڣ�(2001-11-18 15:25:26)
 */
private void bodyRowChangeLookList(BillEditEvent e) {
  //ѡ�����߼�
  int iLen = getBillListPanel().getHeadTable().getRowCount();
  for (int i = 0; i < iLen; i++) {
    if (getBillListPanel().getHeadTable().isRowSelected(i)) {
      getBillListPanel().getHeadBillModel().setRowState(i, BillModel.SELECTED);
      setDispIndex(i);
      setListBodyData(i);
      setButtonsList();
      updateButtons();
    } else {
      getBillListPanel().getHeadBillModel().setRowState(i, BillModel.NORMAL);
    }
  }
}
/**
 * @���ܣ������޸����ݵĺϷ���
 * @���ߣ���־ƽ
 * �������ڣ�(2001-6-20 14:47:40)
 * @return boolean
 * @param newvo nc.vo.rc.receive.ArriveorderVO
 * @param oldvo nc.vo.rc.receive.ArriveorderVO
 */
private boolean checkModifyData(ArriveorderVO newvo, ArriveorderVO oldvo) {
  try {
    /**���������������ʱ����ϢΪ������*/
    BillModel bm = getBillCardPanel().getBillModel();
    Hashtable hErr = new Hashtable();
    String strInvBasId = null, strErr = null;
    Object objAssUnit = null, objAssNum = null, objExhRate = null;
    if (bm != null) {
      for (int i = 0; i < bm.getRowCount(); i++) {
        strErr = "";
        strInvBasId = (String) bm.getValueAt(i, "cbaseid");
        if (PuTool.isAssUnitManaged(strInvBasId)) {
          objAssUnit = bm.getValueAt(i, "cassistunitname");
          objAssNum = bm.getValueAt(i, "nassistnum");
          objExhRate = bm.getValueAt(i, "convertrate");
          if (objAssUnit == null || objAssUnit.toString().trim().equals(""))
            strErr += m_lanResTool.getStrByID("common","UC000-0003938")/*@res "����λ"*/;
          if (objAssNum == null || objAssNum.toString().trim().equals("")) {
            if (strErr.length() > 0)
              strErr += m_lanResTool.getStrByID("SCMCOMMON","UPPSCMCommon-000000")/*@res "��"*/;
            strErr += m_lanResTool.getStrByID("common","UC000-0003971")/*@res "������"*/;
          }
          if (objExhRate == null || objExhRate.toString().trim().equals("")) {
            if (strErr.length() > 0)
              strErr += m_lanResTool.getStrByID("SCMCOMMON","UPPSCMCommon-000000")/*@res "��"*/;
            strErr += m_lanResTool.getStrByID("common","UC000-0002161")/*@res "������"*/;
          }
          if (strErr.trim().length() > 0)
            hErr.put(new Integer(i + 1), strErr);
        }
      }
    }
    if (hErr.size() > 0) {
      Vector vTmp = new Vector();
      Enumeration keys = hErr.keys();
      Integer iKey = null;
      strErr = "";
      strErr += m_lanResTool.getStrByID("40040301","UPP40040301-000100")/*@res "�и���������Ĵ�����ֿ��\n"*/;
      while (keys.hasMoreElements()) {
        iKey = (Integer) keys.nextElement();
        vTmp.addElement(m_lanResTool.getStrByID("40040301","UPP40040301-000101")/*@res "���� "*/ + iKey + ": "  + hErr.get(iKey) + "\n");
      }
      for (int i = vTmp.size() - 1; i >= 0; i--) {
        strErr += vTmp.elementAt(i);
      }
      MessageDialog.showErrorDlg(this, m_lanResTool.getStrByID("SCMCOMMON","UPPSCMCommon-000059")/*@res "����"*/, strErr);
      return false;
    }
    ArriveorderHeaderVO head = (ArriveorderHeaderVO) newvo.getParentVO();
    //��������
    String arrdate = null;
    if (!(head.getAttributeValue("dreceivedate") == null
      || head.getAttributeValue("dreceivedate").toString().trim().equals(""))) {
      arrdate = head.getAttributeValue("dreceivedate").toString();
    }
    //��Ӧ��
    String vendor = (String) head.getAttributeValue("cvendormangid");
    //����
    //String dept = (String) head.getAttributeValue("cdeptid");
    //ҵ��Ա
    //String empl = (String) head.getAttributeValue("cemployeeid");
    //ҵ������
    String busi = (String) head.getAttributeValue("cbiztype");
    //�����֯
    String sStoreOrgId = head.getCstoreorganization();

    if (arrdate == null || arrdate.trim().equals("")) {
      showErrorMessage(m_lanResTool.getStrByID("40040301","UPP40040301-000102")/*@res "�������ڲ���Ϊ��"*/);
      return false;
    } else if (vendor == null || vendor.trim().equals("")) {
      showErrorMessage(m_lanResTool.getStrByID("40040301","UPP40040301-000103")/*@res "��Ӧ�̲���Ϊ��"*/);
      return false;
//    } else if (dept == null || dept.trim().equals("")) {
//      showErrorMessage(m_lanResTool.getStrByID("40040301","UPP40040301-000104")/*@res "���Ų���Ϊ��"*/);
//      return false;
//    } else if (empl == null || empl.trim().equals("")) {
//      showErrorMessage(m_lanResTool.getStrByID("40040301","UPP40040301-000105")/*@res "ҵ��Ա����Ϊ��"*/);
//      return false;
    } else if (busi == null || busi.trim().equals("")) {
      showErrorMessage(m_lanResTool.getStrByID("40040301","UPP40040301-000106")/*@res "ҵ�����Ͳ���Ϊ��"*/);
      return false;
    } else if (sStoreOrgId == null || sStoreOrgId.trim().equals("")) {
      showErrorMessage(m_lanResTool.getStrByID("40040301","UPP40040301-000107")/*@res "�����֯����Ϊ��"*/);
      return false;
    }

    //��������пմ������
    String lines = "";
    int line = 0;
    for (line = 0;
      line < getBillCardPanel().getBillModel().getRowCount();
      line++) {
      if (getBillCardPanel().getBillModel().getValueAt(line, "cinventorycode")
        == null
        || getBillCardPanel()
          .getBillModel()
          .getValueAt(line, "cinventorycode")
          .toString()
          .trim()
          .equals("")) {
        if (!lines.trim().equals("")) {
          lines += m_lanResTool.getStrByID("SCMCOMMON","UPPSCMCommon-000000")/*@res "��"*/;
        }
        lines += line + 1;
      }
    }
    if (!lines.trim().equals("")) {
      MessageDialog.showWarningDlg(
        this,
        m_lanResTool.getStrByID("SCMCOMMON","UPPSCMCommon-000270")/*@res "��ʾ"*/,
        CommonConstant.BEGIN_MARK
          + lines
          + CommonConstant.END_MARK
          + m_lanResTool.getStrByID("40040301","UPP40040301-000108")/*@res "�д�����벻��Ϊ�գ�������������"*/);
      return false;
    }
    //����������Ϊ�����
    lines = "";
    line = 0;
    for (line = 0;
      line < getBillCardPanel().getBillModel().getRowCount();
      line++) {
      if (getBillCardPanel().getBillModel().getValueAt(line, "narrvnum") == null
        || getBillCardPanel()
          .getBillModel()
          .getValueAt(line, "narrvnum")
          .toString()
          .trim()
          .equals("")
        || ((UFDouble) getBillCardPanel().getBillModel().getValueAt(line, "narrvnum"))
          .doubleValue()
          == 0) {
        if (!lines.trim().equals("")) {
          lines += m_lanResTool.getStrByID("SCMCOMMON","UPPSCMCommon-000000")/*@res "��"*/;
        }
        lines += line + 1;
      }
    }
    if (!lines.trim().equals("")) {
      MessageDialog.showWarningDlg(
        this,
        m_lanResTool.getStrByID("SCMCOMMON","UPPSCMCommon-000270")/*@res "��ʾ"*/,
        CommonConstant.BEGIN_MARK
          + lines
          + CommonConstant.END_MARK
          + m_lanResTool.getStrByID("40040301","UPP40040301-000087")/*@res "�д��������������Ϊ�㣬Ҳ����Ϊ�գ������뵽������"*/);
      return false;
    }
    //����������Ϊ������
    lines = "";
    line = 0;
    for (line = 0;
      line < getBillCardPanel().getBillModel().getRowCount();
      line++) {
      if (!(getBillCardPanel().getBillModel().getValueAt(line, "nprice") == null
        || getBillCardPanel()
          .getBillModel()
          .getValueAt(line, "nprice")
          .toString()
          .trim()
          .equals("")
        || ((UFDouble) getBillCardPanel().getBillModel().getValueAt(line, "nprice"))
          .doubleValue()
          == 0)) {
        //�ǿշ���ʱ���Ϊ��
        if (((UFDouble) getBillCardPanel()
          .getBillModel()
          .getValueAt(line, "nprice"))
          .doubleValue()
          < 0) {
          if (!lines.trim().equals("")) {
            lines += m_lanResTool.getStrByID("SCMCOMMON","UPPSCMCommon-000000")/*@res "��"*/;
          }
          lines += line + 1;
        }
      }
    }
    if (!lines.trim().equals("")) {
      MessageDialog.showWarningDlg(
        this,
        m_lanResTool.getStrByID("SCMCOMMON","UPPSCMCommon-000270")/*@res "��ʾ"*/,
        CommonConstant.BEGIN_MARK
          + lines
          + CommonConstant.END_MARK
          + m_lanResTool.getStrByID("40040301","UPP40040301-000109")/*@res "�д�������۸�С���㣬���������뵽���۸�"*/);
      return false;
    }
//    //|��Ʒ����|<=|��������|
//    UFDouble givenum = null;
//    UFDouble arrnum = null;
//    lines = "";
//    line = 0;
//    for (line = 0;
//      line < getArrBillCardPanel().getBillModel().getRowCount();
//      line++) {
//      if ((getArrBillCardPanel().getBillModel().getValueAt(line, "npresentnum")
//        == null
//        || getArrBillCardPanel()
//          .getBillModel()
//          .getValueAt(line, "npresentnum")
//          .toString()
//          .trim()
//          .equals("")
//        || ((UFDouble) getArrBillCardPanel()
//          .getBillModel()
//          .getValueAt(line, "npresentnum"))
//          .doubleValue()
//          == 0)) {
//        givenum = new UFDouble(0);
//      } else {
//        givenum =
//          (UFDouble) getArrBillCardPanel().getBillModel().getValueAt(line, "npresentnum");
//      }
//      if (getArrBillCardPanel().getBillModel().getValueAt(line, "narrvnum") == null
//        || getArrBillCardPanel()
//          .getBillModel()
//          .getValueAt(line, "narrvnum")
//          .toString()
//          .trim()
//          .equals("")
//        || ((UFDouble) getArrBillCardPanel().getBillModel().getValueAt(line, "narrvnum"))
//          .doubleValue()
//          == 0) {
//        arrnum = new UFDouble(0);
//      } else {
//        arrnum =
//          (UFDouble) getArrBillCardPanel().getBillModel().getValueAt(line, "narrvnum");
//      }
//      givenum = new UFDouble(Math.abs(givenum.doubleValue()));
//      arrnum = new UFDouble(Math.abs(arrnum.doubleValue()));
//      if (arrnum.compareTo(givenum) < 0) {
//        if (!lines.trim().equals("")) {
//          lines += "��";
//        }
//        lines += line + 1;
//      }
//    }
//    if (!lines.trim().equals("")) {
//      MessageDialog.showWarningDlg(
//        this,
//        "��ʾ",
//        "��������������Ʒ������"
//          + CommonConstant.BEGIN_MARK
//          + lines
//          + CommonConstant.END_MARK
//          + "�д����Ʒ�������ڵ����������������������뵽����������Ʒ����");
//      return false;
//    }
  } catch (Exception e) {
    reportException(e);
    return false;
  }
  return true;
}

/**
 * @���ܣ�ɾ����ǰ���ϵĵ�����(��Ƭ)
 * @���ߣ���־ƽ
 * �������ڣ�(2001-10-08 20:16:16)
 */
private void delArriveorderVODiscarded() {
  ArriveorderVO[] arrives = null;
  Vector v = new Vector();
  int delIndex = 0;
  try {
    for (int i = 0; i < getCacheVOs().length; i++){
      if (i == getDispIndex()) {
        delIndex = i;
      }
      v.add(i, getCacheVOs()[i]);
    }
    v.remove(delIndex);

    if (v.size() > 0) {
      arrives = new ArriveorderVO[v.size()];
      v.copyInto(arrives);
      setCacheVOs(arrives);
    }else{
      setCacheVOs(null);
    }
    //ת���޸Ļ��޸�������״̬�ڱ��浽������������ʾλ��
    if (getDispIndex() > 0) {
      setDispIndex(getDispIndex()-1);
    }else{
      setDispIndex(0);
    }

  } catch (Exception e) {
    SCMEnv.out(e);
  }
}

/**
 * @���ܣ�ɾ����ǰ�����ת�뵽����(��Ƭ)
 * @���ߣ���־ƽ
 * �������ڣ�(2001-7-31 20:11:16)
 * @param:<|>
 * @return:
 * @exception:
 * @see;
 * @since:
 *
 */
private void delArriveorderVOSaved() {
  ArriveorderVO[] arrives = null;
  Vector v = new Vector();
  int delIndex = 0;
  try {
    for (int i = 0; i < getCacheVOs().length; i++){
      if (i == getDispIndex()) {
        delIndex = i;
      }
      v.add(i, getCacheVOs()[i]);
    }
    v.remove(delIndex);

    if (v.size() > 0) {
      arrives = new ArriveorderVO[v.size()];
      v.copyInto(arrives);
      setCacheVOs(arrives);
    }else{
      setCacheVOs(null);
    }
    //ת���޸Ļ��޸�������״̬�ڱ��浽������������ʾλ��
    if (getDispIndex() > 0) {
      setDispIndex(getDispIndex()-1);
    }else{
      setDispIndex(0);
    }

  } catch (Exception e) {
    SCMEnv.out(e);
  }
}

/**
 * @���ܣ�ɾ����ǰ���ϵĵ�����(�б�)
 * @���ߣ���־ƽ
 * �������ڣ�(2001-10-09 20:10:16)
 */
private void delArriveorderVOsDiscarded(Vector v_removed) {
  ArriveorderVO[] arrives = null;
  Vector v_all = new Vector();
  try {
    for (int i = 0; i < getCacheVOs().length; i++){
      v_all.add(i, getCacheVOs()[i]);
    }
    for (int i = 0; i < v_removed.size(); i++){
      v_all.remove(v_removed.elementAt(i));
    }
    if (v_all.size() > 0) {
      arrives = new ArriveorderVO[v_all.size()];
      v_all.copyInto(arrives);
      setCacheVOs(arrives);
    }else{
      setCacheVOs(null);
    }

  } catch (Exception e) {
    reportException(e);
    MessageDialog.showErrorDlg(this,m_lanResTool.getStrByID("SCMCOMMON","UPPSCMCommon-000059")/*@res "����"*/,m_lanResTool.getStrByID("40040301","UPP40040301-000110")/*@res "ˢ����ʾǰ�˻���ʱ����"*/);
  }
}

/**
 * @���ܣ��л����б����(ά���޸�=>>�б�)
 * @���ߣ���־ƽ
 * �������ڣ�(2001-6-20 9:11:08)
 * @param:<|>
 * @return:
 * @exception:
 * @see;
 * @since:
 *
 */
private void displayArrBillListPanel() {
  
  //���ý���״̬
  setM_strState("�����б�");
  
  //��ʾ���ݵ��б����
  loadDataToList();
  
  //��ʾ�б����
  if(!m_bLoaded){
    add(getBillListPanel(), "Center");
    m_bLoaded = true;
  }
  getBillCardPanel().setVisible(false);
  getBillListPanel().setVisible(true);
  
  //Ĭ����ʾ��һ��
  if (getCacheVOs() != null && getCacheVOs().length > 0) {
    getBillListPanel().getHeadTable().setRowSelectionInterval(
      getDispIndex(),
      getDispIndex());
    getBillListPanel().getHeadBillModel().setRowState(
      getDispIndex(),
      BillModel.SELECTED);
    setListBodyData(getDispIndex());
  }
  setButtonsState();
  updateUI();
}

/**
 * @���ܣ��л����б����(ת���޸�=>>�б�)
 */
private void displayArrBillListPanelNew() {

  //���ý���״̬
  setM_strState("ת���б�");
  setButtonsState();
  //��ʾ���ݵ��б����
  loadDataToList();
  //��ʾ�б����
  if(!m_bLoaded){
    add(getBillListPanel(), "Center");
    m_bLoaded = true;
  }
  getBillListPanel().setVisible(true);
  getBillCardPanel().setVisible(false);
  //�б�״̬Ĭ����ʾ����
  setDispIndex(0);
  if (getCacheVOs() != null && getCacheVOs().length > 0) {
    getBillListPanel().getHeadTable().setRowSelectionInterval(getDispIndex(), getDispIndex());
    getBillListPanel().getHeadBillModel().setRowState(getDispIndex(), BillModel.SELECTED);
    setListBodyData(getDispIndex());
  }
  setButtonsListValueChangedNew(1);
  updateUI();
}

/**
 * ���ߣ���ά��
 * ���ܣ��˴����뷽��˵��
 * ������
 * ���أ�
 * ���⣺
 * ���ڣ�(2004-3-16 15:28:25)
 */
public String getAccYear() {
  return getClientEnvironment().getAccountYear();
}
/**
 * ��������:��õ�������Ƭ
 * ��ʼ������
  1.����������
  2.��ͷ��ע�趨��
    (1).���Զ��췵��ֵ��
    (2).����ֵ��Ϊ����
  3.��ͷ��ע�趨��
    (1).���Զ��췵��ֵ��
    (2).����ֵ��Ϊ����
  4.������>=0
  5.����>=0
  6.�ջ��ֿ���չ��˵���Ʒ��
  7.��λ�Ƿ��
  8.����ģ�������
  9.���ȴ���
 * 2002-08-07 wyf   �޸Ĳɹ����ż�ҵ��Ա�����ݿ����Ͳ�ƥ������ DB2
 * 2002-08-08 wyf   �޸�һ��SQL����
 */
public BillCardPanel getBillCardPanel() {
//  m_arrBillPanel = null;
  if (m_billPanel == null) {
    try {
      m_billPanel = new BillCardPanel();
      BillData bd = new BillData(getBillTempletVO());
      if(bd==null){
    	  m_billPanel.loadTemplet("40040301010000000000");
    	  bd=m_billPanel.getBillData();
      }
      
      initBillBeforeLoad(bd);
      m_billPanel.setBillData(bd);
      /*try {
        m_billPanel.loadTemplet(getModuleCode(), null, getOperatorId(), getCorpPrimaryKey(),new MyBillData());
      } catch (Exception ex) {
        reportException(ex);
        m_billPanel.loadTemplet("40040301010000000000");
      }*/      
      // ����ģ���б������ܲ˵���ʼ��
      m_billPanel.setBodyMenuShow(true);
      UIMenuItem[] miBody = m_billPanel.getBodyMenuItems();
      if (miBody != null && miBody.length >= 3) {
        miBody[0].setVisible(false);
        miBody[2].setVisible(false);
      }
      m_billPanel.addBodyMenuListener(this);
      //����ǧ��λ
      /*m_billPanel.setBodyShowThMark(true);ȡ��ǧ��λӲ����*/
      //�����ȼ�
      m_billPanel.hideBodyTableCol("squalitylevelname");
      //���鴦�����
      m_billPanel.hideBodyTableCol("cdealname");
      //�кŵ�����
      if (m_billPanel.getBodyItem("crowno") != null) {
        BillRowNo.loadRowNoItem(m_billPanel, "crowno");
      }
      //�����Զ�����
      nc.ui.scm.pub.def.DefSetTool.updateBillCardPanelUserDef(m_billPanel, getClientEnvironment().getCorporation().getPk_corp(),
          ScmConst.PO_Arrive, //�������� 
          "vdef", "vdef");
      //�Ӻϼ���
      m_billPanel.setTatolRowShow(true);
      //����ģ��༭������
      m_billPanel.addEditListener(this);
      //�༭ǰ����
      m_billPanel.addBodyEditListener2(this);
      //�����������
      m_billPanel.getBodyPanel().addTableSortListener();
      m_billPanel.getBillModel().setRowSort(true);
      //�����к��������
      m_billPanel.getBillModel().setSortPrepareListener(this) ;

      // ��Ƭ�����������
      m_billPanel.getBillModel().addSortRelaObjectListener2(new IBillRelaSortListener2Body());

      bd = m_billPanel.getBillData();
      DefVO[] defBody = DefSetTool.getBatchcodeDef(getClientEnvironment().getCorporation().getPk_corp());
      if(defBody!=null){
        bd.updateItemByDef(defBody, "bc_vdef", false);
      }
      m_billPanel.setBillData(bd);
      
  	  //V55��֧������ѡ��
  	  PuTool.setLineSelected(m_billPanel);
  	  m_billPanel.addActionListener(this);

      //since v56, ���ò��������ĵ���Ŀ(ԭ���ǣ�������Ҫ����Ӧ�á������Լ���������Ϊ Ŀǰ������������֧�ֲ�������)
      PuTool.setBatchModifyForbid(m_billPanel, new String[]{"vfree0"});
    
    } catch (java.lang.Throwable e) {
      SCMEnv.out("��ʼ������ģ��(��Ƭ)ʱ�����쳣��");
      SCMEnv.out(e);
    }
  }
  return m_billPanel;
}

/**
 * ��õ������б�
 *
 */
public BillListPanel getBillListPanel() {
  if (m_arrListPanel == null) {
    try {
      m_arrListPanel = new BillListPanel();
      BillListData bd = new BillListData(getBillTempletVO());
      if(bd==null){
    	  m_arrListPanel.loadTemplet("40040301010000000000");
      }else
      {
    	  m_arrListPanel.setListData(bd);
      }
      /*// ����ģ��
      try {
        m_arrListPanel.loadTemplet(getModuleCode(), null, getOperatorId(), getCorpPrimaryKey());
      } catch (Exception ex) {
        reportException(ex);
        m_arrListPanel.loadTemplet("40040301010000000000");
      }*/
      //����ǧ��λ
      /*m_arrListPanel.getParentListPanel().setShowThMark(true);
      m_arrListPanel.getChildListPanel().setShowThMark(true);*/

      //��ʼ���б���
      initListDecimal();

      if (m_arrListPanel.getHeadItem("cbiztype") != null)
        m_arrListPanel.hideHeadTableCol("cbiztype");
      if (m_arrListPanel.getHeadItem("cvendormangid") != null)
        m_arrListPanel.hideHeadTableCol("cvendormangid");
      if (m_arrListPanel.getHeadItem("cemployeeid") != null)
        m_arrListPanel.hideHeadTableCol("cemployeeid");
      if (m_arrListPanel.getHeadItem("cdeptid") != null)
        m_arrListPanel.hideHeadTableCol("cdeptid");
      if (m_arrListPanel.getHeadItem("ctransmodeid") != null)
        m_arrListPanel.hideHeadTableCol("ctransmodeid");
      if (m_arrListPanel.getHeadItem("creceivepsn") != null)
        m_arrListPanel.hideHeadTableCol("creceivepsn");
      if (m_arrListPanel.getHeadItem("cstoreorganization") != null)
        m_arrListPanel.hideHeadTableCol("cstoreorganization");
      if (m_arrListPanel.getHeadItem("coperator") != null)
        m_arrListPanel.hideHeadTableCol("coperator");
      if (m_arrListPanel.getHeadItem("cauditpsn") != null)
        m_arrListPanel.hideHeadTableCol("cauditpsn");

      //�����ȼ�
      if (m_arrListPanel.getBodyItem("squalitylevelname") != null)
        m_arrListPanel.hideBodyTableCol("squalitylevelname");
      //���鴦�����
      if (m_arrListPanel.getBodyItem("cdealname") != null)
        m_arrListPanel.hideBodyTableCol("cdealname");

      //�����б��Զ�����
      nc.ui.scm.pub.def.DefSetTool.updateBillListPanelUserDef(m_arrListPanel, getCorpPrimaryKey(),
          ScmConst.PO_Arrive, //�������� 
          "vdef", "vdef");
      m_arrListPanel.setListData(m_arrListPanel.getBillListData());
      //�����б�ϼ�
      m_arrListPanel.getChildListPanel().setTotalRowShow(true);

      //���ݱ༭����
      m_arrListPanel.addEditListener(this);
      m_arrListPanel.addMouseListener(this);
      //��ѡ����
      m_arrListPanel.getHeadTable().setCellSelectionEnabled(false);
      m_arrListPanel.getHeadTable().setSelectionMode(javax.swing.ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
      m_arrListPanel.getHeadTable().getSelectionModel().addListSelectionListener(this);
      //�к��������
      m_arrListPanel.getBodyBillModel().setSortPrepareListener(this) ;
      //�б������ֶ��������
      m_arrListPanel.getHeadBillModel().addSortRelaObjectListener2(this);

  	  //V55��֧������ѡ��
  	  PuTool.setLineSelectedList(m_arrListPanel);
    } catch (Exception e) {
      SCMEnv.out("��ʼ������ģ��(�б�)ʱ�����쳣��");
      reportException(e);
    }
  }
  return m_arrListPanel;
}

/**
 * @���ܣ����ص�����VO�����ͷVO����
 * @���ߣ�Administrator
 * �������ڣ�(2001-6-8 21:53:25)
 * @param:<|>
 * @return:
 * @exception:
 * @see;
 * @since:
 *
 * @return nc.vo.pp.ask.AskbillHeaderVO[]
 * @param askbillvos nc.vo.pp.ask.AskbillVO[]
 */
private ArriveorderHeaderVO[] getArriveHeaderVOs(ArriveorderVO[] arrivevos) {
  ArriveorderHeaderVO[] headers = null;
  if (arrivevos != null) {
    headers = new ArriveorderHeaderVO[arrivevos.length];
    for (int i = 0; i < arrivevos.length; i++){
      headers[i] = (ArriveorderHeaderVO)arrivevos[i].getParentVO();
    }
  }
  return headers;
}

/**
 * @���ܣ���ѯ�����õ���������VO
 */
private void getArriveVOsFromDB() {
  try {
    /*�û��Զ�������*/
    ConditionVO[] condsUserDef = getQueryConditionDlg().getConditionVO();
    
    /*�û���������*/
    ConditionVO[] condsNormal = getQueryConditionDlg().getNormalCondsVO();
    /*��Դ������Ϣ����*/
    String strUpSrcSQlPart = getQueryConditionDlg().getUpSrcPnl().getSubSQL();
    /*��ѯ���ݿ�*/
    setCacheVOs(
      ArriveorderHelper.queryAllArriveMy(
        condsUserDef,
        condsNormal,
        getCorpPrimaryKey(),
        getBusitype(),
        strUpSrcSQlPart, getClientEnvironment().getUser().getPrimaryKey()));
    /*û�в�ѯ������ʱ�Ĵ���*/
    if (getCacheVOs() == null || !(getCacheVOs().length > 0)) {
      MessageDialog.showWarningDlg(this, m_lanResTool.getStrByID("SCMCOMMON","UPPSCMCommon-000270")/*@res "��ʾ"*/, m_lanResTool.getStrByID("40040301","UPP40040301-000111")/*@res "û�з��������ļ�¼��"*/);
      if (getBillCardPanel().getBillData() != null) {
        getBillCardPanel().getBillData().clearViewData();
      }
      if (getBillListPanel().getHeadBillModel() != null) {
        getBillListPanel().getHeadBillModel().clearBodyData();
      }
      if (getBillListPanel().getBodyBillModel() != null) {
        getBillListPanel().getBodyBillModel().clearBodyData();
      }
      updateUI();
    }
    /*�Ѳ�ѯ�����ݻ���{����ɾ������,�����Ӱ�������}*/
    else {
      checkVprocessbatch(getCacheVOs());
      for (int i = 0; i < getCacheVOs().length; i++) {
        if (getCacheVOs()[i].getChildrenVO() != null
          && getCacheVOs()[i].getChildrenVO().length > 0) {
          //�����ݸ���Դ����
          String cupsourcebilltype =
            ((ArriveorderItemVO[]) getCacheVOs()[i].getChildrenVO())[0]
              .getCupsourcebilltype();
          ((ArriveorderVO) getCacheVOs()[i]).setUpBillType(cupsourcebilltype);
          //ˢ�±����ϣ����
          for (int j = 0; j < getCacheVOs()[i].getChildrenVO().length; j++) {
            if (getCacheVOs()[i].getChildrenVO()[j].getPrimaryKey() == null)
              continue;
            if (getCacheVOs()[i].getChildrenVO()[j] == null)
              continue;
            hBodyItem.put(
              getCacheVOs()[i].getChildrenVO()[j].getPrimaryKey(),
              getCacheVOs()[i].getChildrenVO()[j]);
          }
        }
      }
    }
  } catch (Exception e) {
    reportException(e);
    MessageDialog.showErrorDlg(this, m_lanResTool.getStrByID("40040301","UPP40040301-000099")/*@res "����"*/, e.getMessage());
  }
}

/**
 * ���ܣ���ȡ������ѯ�Ի���
 */
private ATPForOneInvMulCorpUI getAtpDlg() {
  if (null == m_atpDlg) {
    m_atpDlg = new ATPForOneInvMulCorpUI(this);
  }
  return m_atpDlg;
}

/**
 * @���ܣ��˻���ѯ�����Ի���-�ɹ�
 */
private PoToBackRcQueDLG getBackQueDlgPo() {
  if (m_backQuePoDlg == null) {
    m_backQuePoDlg = new PoToBackRcQueDLG(this, getCorpPrimaryKey());
  }
  return m_backQuePoDlg;
}

/**
 * @���ܣ��˻���ѯ�����Ի���-ί��
 */
private RcToScQueDLG getBackQueDlgSc() {
  if (m_backQueScDlg == null) {
    m_backQueScDlg =
      new RcToScQueDLG(
        this,
        getCorpPrimaryKey(),
        getOperatorId(),
        "40041015", //��ڵ㣺�������˻���ί�ⶩ��
        getBusitype(),
        BillTypeConst.PO_ARRIVE,
        BillTypeConst.SC_ORDER,
        null);
    m_backQueScDlg.addExtraDate("dorderdate", getClientEnvironment().getDate(), getClientEnvironment().getDate());
  }
  return m_backQueScDlg;
}

/**
 * @���ܣ���ȡ�˻����ս���-�ɹ�
 */
private ArrFrmOrdUI getBackRefUIPo() {
  if (m_backRefUIPo == null) {
    m_backRefUIPo =
      new ArrFrmOrdUI(
        "corderid",
        getCorpPrimaryKey(),
        getOperatorId(),
        "40041002",
        "1>0",
        BillTypeConst.PO_ORDER,
        getBusitype(),
        PoToBackRcQueDLG.class.getName(),
        BillTypeConst.PO_ARRIVE,
        this,
        true);
  }
  return m_backRefUIPo;
}

/**
 * @���ܣ���ȡ�˻����ս���-�ɹ�
 */
private ArrFrmOrdUI getBackRefUISc() {
  if (m_backRefUISc == null) {
    m_backRefUISc =
      new ArrFrmOrdUI(
        "corderid",
        getCorpPrimaryKey(),
        getOperatorId(),
        "40041003",
        "1>0",
        BillTypeConst.SC_ORDER,
        getBusitype(),
        ArrFrmOrdQueDLG.class.getName(),
        BillTypeConst.PO_ARRIVE,
        this,
        true);
  }
  return m_backRefUISc;
}
/**
 * @���ܣ���ȡҵ������
 * @���ߣ���־ƽ
 * �������ڣ�(2001-9-4 15:25:00)
 * @param:<|>
 * @return:
 * @exception:
 * @see;
 * @since:
 *
 * @return java.lang.String
 */
private String getBusitype() {
  if (m_refBusi != null){
    return m_refBusi.getRefPK();
  }else{
    return null;
  }
}

/**
 * ���ܣ���������༭ʱ�Ϸ���
     1. narrvnum != null && narrvnum != 0
     2. nassistnum != null && nassistnum != 0
     3.|nelignum| <= |narrvnum|
     4.|npresentnum| <= |narrvnum|
 * ����������ģ��༭�¼�
 * ���أ�����
 * ���⣺
 * ���ڣ�(2002-7-26 9:03:59)
 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
 * @return java.lang.String
 * @param e nc.ui.pub.bill.BillEditEvent
 */
public String getErrMsg(BillEditEvent e) {
  String strErr = null;
  Object oArr = null, oAss = null, oElg = null, oPrsnt = null;
  UFDouble uArr = null, uAss = null, uElg = null, uPrsnt = null;
  BillModel bm = getBillCardPanel().getBillModel();
  oArr = bm.getValueAt(e.getRow(), "narrvnum");
  oAss = bm.getValueAt(e.getRow(), "nassistnum");
  oElg = bm.getValueAt(e.getRow(), "nelignum");
  oPrsnt = bm.getValueAt(e.getRow(), "npresentnum");
  if (oArr != null && !oArr.toString().trim().equals(""))
    uArr = new UFDouble(oArr.toString().trim());
  if (oAss != null && !oAss.toString().trim().equals(""))
    uAss = new UFDouble(oAss.toString().trim());
  if (oElg != null && !oElg.toString().trim().equals(""))
    uElg = new UFDouble(oElg.toString().trim());
  if (oPrsnt != null && !oPrsnt.toString().trim().equals(""))
    uPrsnt = new UFDouble(oPrsnt.toString().trim());
  //�����༭ʱ
  if (e.getKey().equals("narrvnum")) {
    if (uArr == null)
      return m_lanResTool.getStrByID("40040301","UPP40040301-000090")/*@res "����Ϊ��"*/;
    if (uArr.doubleValue() == 0)
      return m_lanResTool.getStrByID("40040301","UPP40040301-000091")/*@res "����Ϊ��"*/;
  }
  //�������༭ʱ
  if (e.getKey().equals("nassistnum")) {
    if (uAss == null)
      return m_lanResTool.getStrByID("40040301","UPP40040301-000092")/*@res "������Ϊ��"*/;
    if (uAss.doubleValue() == 0)
      return m_lanResTool.getStrByID("40040301","UPP40040301-000093")/*@res "������Ϊ��"*/;
  }
  //�ϸ������༭ʱ
  if (e.getKey().equals("nelignum")) {
    if (uElg != null && uElg.abs().doubleValue() > uArr.abs().doubleValue()) {
      strErr = m_lanResTool.getStrByID("40040301","UPP40040301-000114")/*@res "�ϸ���������ֵ���ڵ�����������ֵ"*/;
    }
  }
  //��Ʒ�����༭ʱ
  if (e.getKey().equals("npresentnum")) {
    if (uPrsnt != null && uPrsnt.abs().doubleValue() > uArr.abs().doubleValue()) {
      strErr = m_lanResTool.getStrByID("40040301","UPP40040301-000094")/*@res "��Ʒ��������ֵ���ڵ�����������ֵ"*/;
    }
  }
  return strErr;
}

/**
 * ��ȡ���ڵ㹦�ܽڵ�ID
 * �������ڣ�(2001-10-20 17:29:24)
 * @return java.lang.String
 */
private String getFuncId() {
  String funId = null;
  //funId = this.getModuleCode();
  if (funId == null || funId.trim().equals("")) {
    funId = "40040301";
  }
  return funId;

}

/**
 * @���ܣ���ȡ��λ�Ի���
 * @���ߣ���־ƽ
 * �������ڣ�(2001-9-15 13:50:13)
 * @param:<|>
 * @return:
 * @exception:
 * @see;
 * @since:
 *
 * @return nc.ui.rc.receive.LocateDlg
 */
private LocateDlg getLocateDlg() {
  if (locatedlg == null) {
    locatedlg = new LocateDlg(this,(AggregatedValueObject[])getCacheVOs(),getDispIndex(),m_lanResTool.getStrByID("40040301","UPP40040301-000244")/*@res "��������λ"*/,m_lanResTool.getStrByID("40040301","UPP40040301-000245")/*@res "������"*/);
  }
  return locatedlg;
}

/**
 * @���ܣ����ص�����VO����
 * @���ߣ���־ƽ
 * �������ڣ�(2001-6-19 20:13:12)
 * @param:<|>
 * @return:
 * @exception:
 * @see;
 * @since:
 *
 * @return nc.vo.rc.receive.ArriveorderVO[]
 */
public nc.vo.rc.receive.ArriveorderVO[] getCacheVOs() {
  return m_arriveVOs;
}

/**
 * @���ܣ���ȡ��������ѯ��������Ի���
 */
private RcQueDlg getQueryConditionDlg() {
  if (m_dlgArrQueryCondition == null && isChangeBusitype) {
    m_dlgArrQueryCondition = new RcQueDlg(this, getBusitype(), getFuncId(), getOperatorId(), getCorpPrimaryKey());
    m_dlgArrQueryCondition.addExtraDate("po_arriveorder.dreceivedate", getClientEnvironment().getDate(), getClientEnvironment().getDate());
    isChangeBusitype = false;
  }
  return m_dlgArrQueryCondition;
}

/**
 * @���ܣ����ص�ǰ��ʾ��VOλ��
 * @���ߣ���־ƽ
 * �������ڣ�(2001-6-20 8:47:47)
 * @param:<|>
 * @return:
 * @exception:
 * @see;
 * @since:
 *
 * @return int
 */
private int getDispIndex() {
  return m_iArrCurrRow;
}

/**
 * @���ܣ����ص���״̬
 * @���ߣ���־ƽ
 * �������ڣ�(2001-6-19 20:18:22)
 * @param:<|>
 * @return:
 * @exception:
 * @see;
 * @since:
 *
 * @return java.lang.String
 */
private java.lang.String getStateStr() {
  return m_strState;
}
/**
 * @���ܣ����ز���ԱID
 * @���ߣ���־ƽ
 * �������ڣ�(2001-10-24 14:12:52)
 * @return java.lang.String
 */
public String getOperatorId() {
  String operatorid = getClientEnvironment().getUser().getPrimaryKey();
  if (operatorid == null || operatorid.trim().equals("") || operatorid.equals("88888888888888888888")) {
    operatorid = "10013488065564590288";
  }
  return operatorid;
}
/**
 * ���ߣ���ά��
 * ���ܣ��˴����뷽��˵��
 * ������
 * ���أ�
 * ���⣺
 * ���ڣ�(2004-3-16 15:34:30)
 * @return java.lang.String
 * @param userid java.lang.String
 */
public String getPsnIdByOperID(String userid) {
  if (userid == null)
    return null;
  if (userid.trim().equals(""))
    return null;
  userid = userid.trim();
  String psnid = null;
  try {
    psnid = ArriveorderHelper.getPkPsnByPkOper(userid);
  } catch (Exception e) {
    //reportException(e);
    SCMEnv.out("���ݲ���Ա������Ա����ʱ����");
    psnid = null;
  }
  return psnid;
}

/**
 * ���ߣ���ά��
 * ���ܣ��˴����뷽��˵��
 * ������
 * ���أ�
 * ���⣺
 * ���ڣ�(2004-3-15 13:34:10)
 */
private QueryOrdDlg getQuickArrDlg() {
  if(m_dlgQuickArr == null)
    m_dlgQuickArr = new QueryOrdDlg(this,this);
  return m_dlgQuickArr;
}

/**
 *  ���ܣ�1������δ�ı�ĵ���������VO
      2������ɾ���������Ϊ���建���ϣ����֮����
 *  ˵����Ϊ�˶������������������⴦�� czp 2002-11-13
 */
private ArriveorderVO getSaveVO(ArriveorderVO vo) {
  //���б�������
  Vector vAllBody = new Vector();
  //δ�ı�ı���VO
  ArriveorderItemVO[] voaUIAllBody =
    (ArriveorderItemVO[]) getBillCardPanel().getBillModel().getBodyValueVOs(
      ArriveorderItemVO.class.getName());
  if (voaUIAllBody == null || voaUIAllBody.length <= 0)
    return vo;
  //δ�ı�ı���VO
  int iNoChangeLen = voaUIAllBody.length;
  for (int i = 0; i < iNoChangeLen; i++) {
    if (voaUIAllBody[i].getStatus() == VOStatus.UNCHANGED) {
      vAllBody.addElement((ArriveorderItemVO) voaUIAllBody[i]);
    }
  }
  //�ı�ı���VO��ƴ�ӱ���������VO�ı���VO��
  int iChangeLen = -1;
  if (vo.getChildrenVO() != null && vo.getChildrenVO().length > 0)
    iChangeLen = vo.getChildrenVO().length;
  if (iChangeLen > 0) {
    for (int i = 0; i < iChangeLen; i++) {
      vAllBody.addElement((ArriveorderItemVO) vo.getChildrenVO()[i]);
    }
  }
  ArriveorderItemVO[] voaAllBody = new ArriveorderItemVO[vAllBody.size()];
  vAllBody.copyInto(voaAllBody);
  //����ɾ���������Ϊ���建���ϣ����֮����
  if (hBodyItem != null && hBodyItem.size() > 0) {
    if (voaAllBody != null && voaAllBody.length > 0) {
      for (int i = 0; i < voaAllBody.length; i++) {
        if (voaAllBody[i].getStatus() == VOStatus.DELETED) {
          if (voaAllBody[i].getPrimaryKey() == null)
            continue;
          if (hBodyItem.get(voaAllBody[i].getPrimaryKey()) == null)
            continue;
          voaAllBody[i] =
            (ArriveorderItemVO) hBodyItem.get(voaAllBody[i].getPrimaryKey());
          voaAllBody[i].setStatus(VOStatus.DELETED);
        }
      }
    }
  }
  vo.setChildrenVO(voaAllBody);
  return vo;
}

private ArrayList getSelectedBills() {

  Vector vAll = new Vector();
  ArriveorderVO[] allvos = null;
  //ȫ��ѡ��ѯ�۵�
  int iPos = 0;
  for (int i = 0; i < getBillListPanel().getHeadBillModel().getRowCount(); i++) {
    if (getBillListPanel().getHeadBillModel().getRowState(i) == BillModel.SELECTED) {
      iPos = i;
      iPos = nc.ui.pu.pub.PuTool.getIndexBeforeSort(getBillListPanel(), iPos);
      vAll.add(getCacheVOs()[iPos]);
    }
  }
  allvos = new ArriveorderVO[vAll.size()];
  vAll.copyInto(allvos);

  //��ѯδ��������ĵ�����
  try {
    allvos = RcTool.getRefreshedVOs(allvos);
  } catch (BusinessException b) {
    showHintMessage(m_lanResTool.getStrByID("40040301","UPP40040301-000116")/*@res "���ִ���:"*/ + b.getMessage());
  } catch (Exception e) {
    SCMEnv.out(e);
    showHintMessage(m_lanResTool.getStrByID("40040301","UPP40040301-000117")/*@res "����δ֪����"*/);
  }
  ArrayList aryRslt = new ArrayList();
  if (allvos != null) {
    for (int i = 0; i < allvos.length; i++) {
      aryRslt.add(allvos[i]);
    }
  }
  return aryRslt;
}

/**
 * ���ߣ���ά��
 * ���ܣ��ӿ�IBillModelSortPrepareListener ��ʵ�ַ���
 * ������String sItemKey   ITEMKEY
 * ���أ���
 * ���⣺��
 * ���ڣ�(2004-03-24  11:39:21)
 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
 */
public int getSortTypeByBillItemKey(String sItemKey) {

  if ("crowno".equals(sItemKey)) {
    return BillItem.DECIMAL;
  } else if("csourcebilllinecode".equals(sItemKey)) {
    return BillItem.DECIMAL;
  } else if("cancestorbillrowno".equals(sItemKey)) {
    return BillItem.DECIMAL;
  }

  return getBillCardPanel().getBillModel().getItemByKey(sItemKey).getDataType();
}

/**
 * ���ߣ���ά��
 * ���ܣ��˴����뷽��˵��
 * ������
 * ���أ�
 * ���⣺
 * ���ڣ�(2004-3-16 15:36:26)
 * @return nc.vo.pub.lang.UFDate
 */
public UFDate getSysDate() {
  return getClientEnvironment().getDate();
}

/**
 * ����ʵ�ָ÷���������ҵ�����ı��⡣
 * @version (00-6-6 13:33:25)
 *
 * @return java.lang.String
 */
public String getTitle() {
  String title = m_lanResTool.getStrByID("40040301","UPP40040301-000248")/*@res "������ά��"*/;
  if (m_billPanel != null)
    title = m_billPanel.getTitle();
  return title;
}
/**
 * V51�ع���Ҫ��ƥ��,��ťʵ����������
 * <p>
 * <b>examples:</b>
 * <p>
 * ʹ��ʾ��
 * <p>
 * <b>����˵��</b>
 * <p>
 * @author czp
 * @time 2007-3-13 ����01:15:06
 */
private void createBtnInstances(){

  //���к�
//  m_btnSerialNO=getBtnTree().getButton(IButtonConstRc.BTN_ASSIST_FUNC_SERIALNO);
  //�����ʲ���Ƭ
  m_btnCreateCard=getBtnTree().getButton(IButtonConstRc.BTN_ASSIST_FUNC_CRTCARD);
  //ɾ���ʲ���Ƭ
  m_btnDeleteCard=getBtnTree().getButton(IButtonConstRc.BTN_ASSIST_FUNC_DELCARD);
	
  //����
  m_btnCheck = getBtnTree().getButton(IButtonConstRc.BTN_CHECK);
  //ҵ������
  m_btnBusiTypes = getBtnTree().getButton(IButtonConstRc.BTN_BUSINESS_TYPE);
  //������
  m_btnAdds = getBtnTree().getButton(IButtonConstRc.BTN_ADD);
  //�˻���
  m_btnBackPo = getBtnTree().getButton(IButtonConstRc.BTN_BACK_PU);
  m_btnBackSc = getBtnTree().getButton(IButtonConstRc.BTN_BACK_SC);
  m_btnBacks = getBtnTree().getButton(IButtonConstRc.BTN_BACK);
  //����ά����
  m_btnModify = getBtnTree().getButton(IButtonConstRc.BTN_BILL_EDIT);
  m_btnSave = getBtnTree().getButton(IButtonConstRc.BTN_SAVE);
  m_btnCancel = getBtnTree().getButton(IButtonConstRc.BTN_BILL_CANCEL);
  m_btnDiscard = getBtnTree().getButton(IButtonConstRc.BTN_BILL_DELETE);
  m_btnSendAudit = getBtnTree().getButton(IButtonConstRc.BTN_EXECUTE_AUDIT);  
  m_btnMaintains = getBtnTree().getButton(IButtonConstRc.BTN_BILL);
  //�в�����
  m_btnDelLine = getBtnTree().getButton(IButtonConstRc.BTN_LINE_DELETE);
  m_btnCpyLine = getBtnTree().getButton(IButtonConstRc.BTN_LINE_COPY);
  m_btnPstLine = getBtnTree().getButton(IButtonConstRc.BTN_LINE_PASTE);
  m_btnPstLineTail = getBtnTree().getButton(IButtonConstRc.BTN_LINE_PASTE_TAIL);
  m_btnLines = getBtnTree().getButton(IButtonConstRc.BTN_LINE);  
  //֧�ֵ����˵��С������кš�����
  m_btnReSortRowNo = getBtnTree().getButton(IButtonConstPr.BTN_ADD_NEWROWNO);
  m_btnCardEdit = getBtnTree().getButton(IButtonConstPr.BTN_CARDEDIT);
  m_miReSortRowNo = BillTools.addBodyMenuItem(getBillCardPanel(), m_btnReSortRowNo, null);
  m_miCardEdit = BillTools.addBodyMenuItem(getBillCardPanel(), m_btnCardEdit, null);
  m_miReSortRowNo.addActionListener(new IMenuItemListener());
  m_miCardEdit.addActionListener(new IMenuItemListener());
  //���������
  m_btnQuery = getBtnTree().getButton(IButtonConstRc.BTN_QUERY);
  m_btnFirst = getBtnTree().getButton(IButtonConstRc.BTN_BROWSE_TOP);
  m_btnPrev = getBtnTree().getButton(IButtonConstRc.BTN_BROWSE_PREVIOUS);
  m_btnNext = getBtnTree().getButton(IButtonConstRc.BTN_BROWSE_NEXT);
  m_btnLast = getBtnTree().getButton(IButtonConstRc.BTN_BROWSE_BOTTOM);
  m_btnLocate = getBtnTree().getButton(IButtonConstRc.BTN_BROWSE_LOCATE);
  m_btnRefresh = getBtnTree().getButton(IButtonConstRc.BTN_BROWSE_REFRESH);
  m_btnBrowses = getBtnTree().getButton(IButtonConstRc.BTN_BROWSE);
  //�л�
  m_btnList = getBtnTree().getButton(IButtonConstRc.BTN_SWITCH);
  //ִ����(��������������Ϣ���Ĺ���)
  m_btnActions = getBtnTree().getButton(IButtonConstRc.BTN_EXECUTE);
  //������
  m_btnCombin = getBtnTree().getButton(IButtonConstRc.BTN_PRINT_DISTINCT);
  m_btnPrints = getBtnTree().getButton(IButtonConstRc.BTN_PRINT);
  m_btnPrint = getBtnTree().getButton(IButtonConstRc.BTN_PRINT_PRINT);
  m_btnPrintPreview = getBtnTree().getButton(IButtonConstRc.BTN_PRINT_PREVIEW);
  m_btnSplitPrint = getBtnTree().getButton(IButtonConstRc.BTN_SPLITPRINT);
  m_btnUsable = getBtnTree().getButton(IButtonConstRc.BTN_ASSIST_QUERY_ONHAND);
  m_btnQueryBOM = getBtnTree().getButton(IButtonConstRc.BTN_ASSIST_QUERY_SUITE);
  m_btnQuickReceive = getBtnTree().getButton(IButtonConstRc.BTN_ASSIST_FUNC_QUICK_RECEIVE);
  m_btnDocument = getBtnTree().getButton(IButtonConstRc.BTN_ASSIST_FUNC_DOCUMENT);
  m_btnLookSrcBill = getBtnTree().getButton(IButtonConstRc.BTN_ASSIST_QUERY_RELATED);
  m_btnQueryForAudit = getBtnTree().getButton(IButtonConstRc.BTN_ASSIST_QUERY_WORKFLOW);
  m_btnOthers = getBtnTree().getButton(IButtonConstRc.BTN_ASSIST_QUERY);
  /*
  { m_btnUsable, m_btnQueryBOM,m_btnQuickReceive, m_btnDocument, m_btnLookSrcBill, m_btnQueryForAudit, m_btnCombin, m_btnPrint, m_btnPrintPreview};
  */
  
  /*��Ƭ��ť�˵�*/

  m_aryArrCardButtons = getBtnTree().getButtonArray();
  
  /*�б�ť����*/
  
  m_btnSelectAll = getBtnTree().getButton(IButtonConstRc.BTN_BROWSE_SELECT_ALL);
  m_btnSelectNo = getBtnTree().getButton(IButtonConstRc.BTN_BROWSE_SELECT_NONE);
  m_btnModifyList = m_btnModify;
  m_btnDiscardList = m_btnDiscard;
  m_btnQueryList = m_btnQuery;
  m_btnCard = m_btnList;
  m_btnEndCreate = getBtnTree().getButton(IButtonConstRc.BTN_REF_CANCEL);
  m_btnRefreshList = m_btnRefresh;
  
  //������
  m_btnUsableList = m_btnUsable;
  m_btnDocumentList = m_btnDocument;
  m_btnQueryBOMList = m_btnQueryBOM;
  m_btnPrintPreviewList = m_btnPrintPreview;
  m_btnPrintList = m_btnPrint;
  m_btnOthersList = m_btnOthers;

  /*�б�ť��*/
  m_aryArrListButtons = m_aryArrCardButtons;
  
  /*��Ϣ���İ�ť��*/
  m_btnAudit = getBtnTree().getButton(IButtonConstRc.BTN_AUDIT);
  m_btnAudit.setTag("APPROVE");
  m_btnUnAudit = getBtnTree().getButton(IButtonConstRc.BTN_EXECUTE_AUDIT_CANCEL);
  m_btnUnAudit.setTag("UNAPPROVE");
  m_btnOthersMsgCenter = m_btnOthers;
  m_btnActionMsgCenter = m_btnActions;
  m_aryMsgCenter = new ButtonObject[]{ m_btnAudit, m_btnActionMsgCenter, m_btnOthersMsgCenter};
}
/**
 * ��ʼ����ť
 * �������ڣ�(01-2-26 13:29:17)
 */
private void setButtonsInit() {
  //���⹦��
  m_btnList.setName(m_lanResTool.getStrByID("SCMCOMMON", "UPPSCMCommon-000464")/*
       * @res
       * "�б���ʾ"
       */);
  //
  for (int i = 0; i < m_aryArrCardButtons.length; i++){
    if(PuTool.isExist(getExtendBtns(),m_aryArrCardButtons[i])){
      continue;
    }
    m_aryArrCardButtons[i].setEnabled(false);
  }
  //����ת��
  m_btnEndCreate.setVisible(false);
  //��������
  getBtnTree().getButton(IButtonConstRc.BTN_ASSIST_FUNC).setEnabled(true);
  
  getBtnTree().getButton(IButtonConstRc.BTN_PRINT).setEnabled(true);
  m_btnActions.setEnabled(true);
  m_btnAudit.setEnabled(false);
  m_btnUnAudit.setEnabled(false);
  //����ά��
  m_btnMaintains.setEnabled(true);
  m_btnModify.setEnabled(false);
  m_btnSave.setEnabled(false);
  m_btnCancel.setEnabled(false);
  m_btnDiscard.setEnabled(false);
  m_btnSelectAll.setEnabled(false);
  m_btnSendAudit.setEnabled(false);
  //���
  m_btnBrowses.setEnabled(true);
  m_btnQuery.setEnabled(true);
  m_btnFirst.setEnabled(false);
  m_btnPrev.setEnabled(false);
  m_btnNext.setEnabled(false);
  m_btnLast.setEnabled(false);
  m_btnSelectAll.setEnabled(false);
  m_btnSelectNo.setEnabled(false);
  //����
  m_btnOthers.setEnabled(true);
  m_btnRefresh.setEnabled(false);
  m_btnLocate.setEnabled(false);
  m_btnUsable.setEnabled(false);
  m_btnQueryBOM.setEnabled(false);
  m_btnQuickReceive.setEnabled(true);
  m_btnDocument.setEnabled(false);
  m_btnLookSrcBill.setEnabled(false);
  m_btnPrint.setEnabled(false);
  m_btnSplitPrint.setEnabled(false);
  m_btnCombin.setEnabled(false);
  m_btnPrintPreview.setEnabled(false);
  m_btnQueryForAudit.setEnabled(false);

  m_btnCheck.setEnabled(false);
  
  m_btnBusiTypes.setEnabled(true);
  m_btnAdds.setEnabled(true);

  m_btnList.setEnabled(true);

  m_btnBacks.setEnabled(true);
  m_btnBackPo.setEnabled(true);
  m_btnBackSc.setEnabled(true);
  
  m_btnCreateCard.setEnabled(false);
  m_btnDeleteCard.setEnabled(false);

  //
  updateButtonsAll();
}

/**
 * ���ܣ���ʼ������
 * ������
 * ���أ�
 * ���⣺
 * ���ڣ�(2002-8-21 10:01:19)
 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
 */
private void initDecimal() {
  //��������

  //����
  if (m_billPanel.getBodyItem("narrvnum") != null)
    m_billPanel.getBodyItem("narrvnum").setDecimalDigits(m_iPowerNum);
  if (m_billPanel.getBodyItem("nplanarrvnum") != null)
	  m_billPanel.getBodyItem("nplanarrvnum").setDecimalDigits(m_iPowerNum);
  if (m_billPanel.getBodyItem("nelignum") != null)
    m_billPanel.getBodyItem("nelignum").setDecimalDigits(m_iPowerNum);
  if (m_billPanel.getBodyItem("nnotelignum") != null)
    m_billPanel.getBodyItem("nnotelignum").setDecimalDigits(m_iPowerNum);
  if (m_billPanel.getBodyItem("npresentnum") != null)
    m_billPanel.getBodyItem("npresentnum").setDecimalDigits(m_iPowerNum);
  if (m_billPanel.getBodyItem("nwastnum") != null)
    m_billPanel.getBodyItem("nwastnum").setDecimalDigits(m_iPowerNum);
  if (m_billPanel.getBodyItem("nwastassistnum") != null)
	    m_billPanel.getBodyItem("nwastassistnum").setDecimalDigits(m_iPowerNum);
  //������
  if (m_billPanel.getBodyItem("nassistnum") != null)
    m_billPanel.getBodyItem("nassistnum").setDecimalDigits(m_iPowerAssNum);
  //������
  if (m_billPanel.getBodyItem("npresentassistnum") != null)
    m_billPanel.getBodyItem("npresentassistnum").setDecimalDigits(m_iPowerAssNum);
  //������
  if (m_billPanel.getBodyItem("convertrate") != null)
    m_billPanel.getBodyItem("convertrate").setDecimalDigits(m_iPowerConvertRate);
  //����
  if (m_billPanel.getBodyItem("nprice") != null)
    m_billPanel.getBodyItem("nprice").setDecimalDigits(m_iPowerPrice);
  //���
  if (m_billPanel.getBodyItem("nmoney") != null) {
    m_billPanel.getBodyItem("nmoney").setDecimalDigits(m_iPowerMoney);
  }

}

/**
 * ��ʼ����ť��
 * <p>
 * <b>examples:</b>
 * <p>
 * ʹ��ʾ��
 * <p>
 * <b>����˵��</b>
 * <p>
 * @author czp
 * @time 2007-3-19 ����09:26:36
 */
private void initButtons() {
  
  // V51�ع���Ҫ��ƥ��,��ťʵ��������

	createBtnInstances();
  /*�Ż�������
   * //ҵ�����Ͱ�ť���Ӳ˵�
  PfUtilClient.retBusinessBtn(m_btnBusiTypes, getCorpPrimaryKey(), BillTypeConst.PO_ARRIVE);
  
  //ҵ�����Ͱ�ť�򹳴���
  PuTool.initBusiAddBtns(m_btnBusiTypes, m_btnAdds, BillTypeConst.PO_ARRIVE, getCorpPrimaryKey());*/
  PfUtilClient.retBusiAddBtn(m_btnBusiTypes,m_btnAdds, getCorpPrimaryKey(), BillTypeConst.PO_ARRIVE);
  
  retInitBusiAddBtns();
  // ������չ��ť
  addExtendBtns();

  // ���ؿ�Ƭ��ť
  setButtons(m_btnTree.getButtonArray());
}

private void retInitBusiAddBtns(){
if (m_btnBusiTypes.getChildButtonGroup() != null
		&& m_btnBusiTypes.getChildButtonGroup().length > 0) {

	ButtonObject[] bo = m_btnBusiTypes.getChildButtonGroup();
	for (int i = 0; i < bo.length; i++) {
		bo[i].setName(bo[i].getName());
	}
	m_btnBusiTypes.setChildButtonGroup(bo);

	m_btnBusiTypes.setTag(m_btnBusiTypes.getChildButtonGroup()[0].getTag());
	m_btnBusiTypes.getChildButtonGroup()[0].setSelected(true);
	m_btnBusiTypes.setCheckboxGroup(true);

	bo = m_btnAdds.getChildButtonGroup();
	for (int i = 0; i < bo.length; i++) {
		bo[i].setName(bo[i].getName());
	}
	m_btnAdds.setChildButtonGroup(bo);
}
}
private void initInvRef(){
	  UIRefPane refCinventorycode = (UIRefPane) getBillCardPanel().getBodyItem("cinventorycode").getComponent();
	  refCinventorycode.setIsCustomDefined(true);
	  refCinventorycode.setRefType(IBusiType.GRID);
	  InvRefModelForRepl refmodel = new InvRefModelForRepl(null,ClientEnvironment.getInstance().getCorporation().getPrimaryKey(),true);
	  refCinventorycode.setRefModel(refmodel);
}

/**
 * ������չ��ť��
 * <p>
 * <b>examples:</b>
 * <p>
 * ʹ��ʾ��
 * <p>
 * <b>����˵��</b>
 * <p>
 * @author czp
 * @time 2007-3-19 ����09:23:34
 */
private void addExtendBtns() {
  ButtonObject[] btnsExtend = getExtendBtns();    
  if(btnsExtend == null || btnsExtend.length <= 0){
    return;
  }
  ButtonObject boExtTop = getBtnTree().getExtTopButton();
  getBtnTree().addMenu(boExtTop);
  int iLen = btnsExtend.length;
  try{
    for(int j=0; j<iLen;j++){
        getBtnTree().addChildMenu(boExtTop, btnsExtend[j]);
    }
  }catch(BusinessException be){
    showHintMessage(be.getMessage());
    return;
  }
}
/**
 * �漰Զ�̵��õĳ�ʼ��
 * <p>
 * <b>examples:</b>
 * <p>
 * ʹ��ʾ��
 * <p>
 * <b>����˵��</b>
 * <p>
 * @author czp
 * @time 2009-7-23 ����02:18:54
 */
private void initParaBetter(){
    //
    IQueryForInitUI srvInitQry = NCLocator.getInstance().lookup(IQueryForInitUI.class);
    
    //���ȣ������������������ۡ����
    InitValue initValue = null;    
    try{
        initValue = srvInitQry.qeryInitValue();   
    }catch(BusinessException e){
        showHintMessage(e.getMessage());
        return;
    }
    m_iPowerAssNum = initValue.m_iPowerAssNum;
    m_iPowerConvertRate = initValue.m_iPowerConvertRate;
    m_iPowerNum = initValue.m_iPowerNum;
    m_iPowerPrice = initValue.m_iPowerPrice;
    //���ҽ���
    m_iPowerMoney = initValue.m_iPowerNmoney;
    m_bSaveMaker = initValue.m_bSaveMaker;
    //��������ģ������
    m_bQcEnabled = initValue.m_bQcEnabled;
    m_bAIMEnabled = initValue.m_bAIMEnabled;
    //ButtonTree
    //m_btnTree = initValue.m_btnTree;
}
/**
 * ��������:�ڵ��ʼ��
 *
 * ��ȡҵ�����͡����ص���ģ�塢��ʼ����ť״̬

 */
private void initialize() {

  //��ʼ������
  //initPara();
  initParaBetter();
    
  //��ʼ����ť
  initButtons();  

  //���ð�ť״̬
  setButtonsState();
  
  //��ʾ����
  setLayout(new java.awt.BorderLayout());
  add(getBillCardPanel(), BorderLayout.CENTER);

  //��ʼ�����屸ע
  initVmemoBody();

  //��ʼ������
  initDecimal();

  //��ʼ��������״̬
  getBillCardPanel().setEnabled(false);
//  getBillListPanel().setListData(getBillListPanel().getBillListData());

}
/**
 * ���ܣ���ʼ�����屸ע
 */
private void initVmemoBody(){
  if (getBillCardPanel().getBodyItem("vmemo") != null) {
    UIRefPane nRefPanel = (UIRefPane) getBillCardPanel().getBodyItem("vmemo").getComponent();
    nRefPanel.setTable(getBillCardPanel().getBillTable());
    nRefPanel.getRefModel().setRefCodeField(nRefPanel.getRefModel().getRefNameField());
    nRefPanel.getRefModel().setBlurFields(new String[] { nRefPanel.getRefModel().getRefNameField()});
    nRefPanel.setAutoCheck(false);
  }
}
/**
 * ���ܣ���ʼ������
 * ������
 * ���أ�
 * ���⣺
 * ���ڣ�(2002-8-21 10:01:19)
 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
 */
private void initListDecimal() {
  //��������

  //����
  if (m_arrListPanel.getBodyItem("narrvnum") != null)
    m_arrListPanel.getBodyItem("narrvnum").setDecimalDigits(m_iPowerNum);
  if (m_arrListPanel.getBodyItem("nplanarrvnum") != null)
	  m_arrListPanel.getBodyItem("nplanarrvnum").setDecimalDigits(m_iPowerNum);
  if (m_arrListPanel.getBodyItem("nelignum") != null)
    m_arrListPanel.getBodyItem("nelignum").setDecimalDigits(m_iPowerNum);
  if (m_arrListPanel.getBodyItem("nnotelignum") != null)
    m_arrListPanel.getBodyItem("nnotelignum").setDecimalDigits(m_iPowerNum);
  if (m_arrListPanel.getBodyItem("npresentnum") != null)
    m_arrListPanel.getBodyItem("npresentnum").setDecimalDigits(m_iPowerNum);
  if (m_arrListPanel.getBodyItem("nwastnum") != null)
    m_arrListPanel.getBodyItem("nwastnum").setDecimalDigits(m_iPowerNum);
  //������
  if (m_arrListPanel.getBodyItem("nassistnum") != null)
    m_arrListPanel.getBodyItem("nassistnum").setDecimalDigits(m_iPowerAssNum);
  //������
  if (m_arrListPanel.getBodyItem("convertrate") != null)
    m_arrListPanel.getBodyItem("convertrate").setDecimalDigits(m_iPowerConvertRate);
  //����
  if (m_arrListPanel.getBodyItem("nprice") != null)
    m_arrListPanel.getBodyItem("nprice").setDecimalDigits(m_iPowerPrice);
  //���
  if (m_arrListPanel.getBodyItem("nmoney") != null)
    m_arrListPanel.getBodyItem("nmoney").setDecimalDigits(m_iPowerMoney);

}

/**
 * ��������:��ʼ������
 */
public void initPara() {
  try {
    ServcallVO[] scDisc = new ServcallVO[2];
    //��ʼ�����ȣ����������ۣ�
    scDisc[0] = new ServcallVO();
    scDisc[0].setBeanName("nc.itf.pu.pub.IPub");
    scDisc[0].setMethodName("getDigitBatch");
    scDisc[0].setParameter(new Object[] { getCorpPrimaryKey(), new String[] { "BD502", "BD503", "BD501", "BD505" }});
    scDisc[0].setParameterTypes(new Class[] { String.class, String[].class });

    scDisc[1] = new ServcallVO();
    scDisc[1].setBeanName("nc.itf.rc.receive.IArriveorder");
    scDisc[1].setMethodName("getCurrDecimal");
    scDisc[1].setParameter(new Object[] { getCorpPrimaryKey()});
    scDisc[1].setParameterTypes(new Class[] { String.class });
    
    //
    String strPara0 = SysInitBO_Client.getParaString(PoPublicUIClass.getLoginPk_corp(),"PO060");
    
    //��̨һ�ε���
    Object[] oParaValue = nc.ui.scm.service.LocalCallService.callService(scDisc);
    if (oParaValue != null && oParaValue.length == scDisc.length) {
          //���������ݾ���
      int[] iDigits = (int[]) oParaValue[0];
      if (iDigits != null && iDigits.length == 4) {
          m_iPowerAssNum = iDigits[0];
          m_iPowerConvertRate = iDigits[1];
          m_iPowerNum = iDigits[2];
          m_iPowerPrice = iDigits[3];
      }
      //���ҽ���
      m_iPowerMoney = ((Integer) oParaValue[1]).intValue();
      String s = strPara0;
      if(s != null) m_bSaveMaker = (new UFBoolean(s)).booleanValue();
    }
    //��������ģ������
    ICreateCorpQueryService tt = NCLocator.getInstance().lookup(ICreateCorpQueryService.class);
    m_bQcEnabled = tt.isEnabled(getCorpPrimaryKey(), ProductCode.PROD_QC);
    m_bAIMEnabled = tt.isEnabled(getCorpPrimaryKey(), "AIM");
  } catch (Exception e) {
      showHintMessage(e.getMessage());
      reportException(e);
  }

}

/**
 * ���ܣ���ʼ������
 * ������
 * ���أ�
 * ���⣺
 * ���ڣ�(2002-8-21 10:01:19)
 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
 */
private void initRefPane(BillData bd) {
  //������������������ͷ����������������

  //ҵ�����ͳ�ʼ��
  if (bd.getHeadItem("cbiztype") != null) {
    m_refBusi = (UIRefPane) bd.getHeadItem("cbiztype").getComponent();
    m_refBusi.setEnabled(false);
  }
  //����
  if (bd.getHeadItem("cdeptid") != null) {
    String sql = "and (bd_deptdoc.deptattr IN ('2','4'))";
    UIRefPane refDept = (UIRefPane) (bd.getHeadItem("cdeptid").getComponent());
    /*
    String sqltemp = refDept.getRefModel().getWherePart();
    if ((sqltemp != null) && (!(sqltemp.trim().equals("")))) {
      sql = sql + " and " + sqltemp;
    }
    */
    refDept.getRefModel().addWherePart(sql);
  }
  //ҵ��Ա
  if (bd.getHeadItem("cemployeeid") != null) {
//    String sql = "and (bd_psndoc.pk_deptdoc IN (SELECT pk_deptdoc FROM bd_deptdoc WHERE (deptattr IN ('2','4')) AND dr = 0))";
    UIRefPane refEmpl = (UIRefPane) (bd.getHeadItem("cemployeeid").getComponent());
    nc.ui.pu.pub.PurPsnRefModel refPsnModel = new nc.ui.pu.pub.PurPsnRefModel(getCorpPrimaryKey());
    refEmpl.setRefModel(refPsnModel);
    refEmpl.getRefModel().setHiddenFieldCode(new String[] { "bd_psndoc.pk_psndoc", "bd_psndoc.pk_deptdoc" });
    /*
    String sqltemp = refEmpl.getRefModel().getWherePart();
    if ((sqltemp != null) && (!(sqltemp.trim().equals("")))) {
      sql = sql + " and " + sqltemp;
    }
    */
//    refEmpl.getRefModel().addWherePart(sql);
  }
  //��ͷ�˻�����
  if (bd.getHeadItem("vbackreasonh") != null) {
    UIRefPane refPaneReason = (UIRefPane) bd.getHeadItem("vbackreasonh").getComponent();
    refPaneReason.setRefModel(new BackReasonRefModel());
    refPaneReason.setAutoCheck(false);
  }
  //��ͷ��ע
  if (bd.getHeadItem("vmemo") != null) {
    UIRefPane refVmemo = (UIRefPane) bd.getHeadItem("vmemo").getComponent();
    refVmemo.setRefNodeName("����ժҪ");
    refVmemo.getRefModel().setRefCodeField(refVmemo.getRefModel().getRefNameField());
    refVmemo.getRefModel().setBlurFields(new String[] { refVmemo.getRefModel().getRefNameField()});
    refVmemo.setAutoCheck(false);
  }

  //�����֯
  if (bd.getHeadItem("cstoreorganization") != null) {
    UIRefPane refPane = (UIRefPane) bd.getHeadItem("cstoreorganization").getComponent();
    refPane.getRefModel().addWherePart(" and (bd_calbody.property = 0 or bd_calbody.property = 1) ");
  }

  //�������������������壭��������������

  //������
  /*
  if (bd.getBodyItem("vfree0") != null && bd.getBodyItem("vfree0") != null) {
    FreeItemRefPane m_firpFreeItemRefPane = new FreeItemRefPane();
    m_firpFreeItemRefPane.setMaxLength(bd.getBodyItem("vfree0").getLength());
    //�Ӽ�����
    m_firpFreeItemRefPane.getUIButton().addActionListener(this);
    bd.getBodyItem("vfree0").setComponent(m_firpFreeItemRefPane);

  }
  */
  //�������κŲ���
  try {
      if (bd.getBodyItem("vproducenum") != null && bd.getBodyItem("vproducenum").isShow()) {
//      UIRefPane lotRef = (UIRefPane)InterServUI.getInterInstance(ProductCode.PROD_IC,InterRegister.IC0012);
      UIRefPane lotRef = (UIRefPane) new LotNumbRefPane();
      lotRef.setIsCustomDefined(true);
      lotRef.setMaxLength(bd.getBodyItem("vproducenum").getLength());
      bd.getBodyItem("vproducenum").setComponent(lotRef);
    }
    } catch (Exception e) {
        SCMEnv.out(e);
    }

//  //���屸ע����
//  if (bd.getBodyItem("vmemo") != null) {
//    UIRefPane nRefPanel = (UIRefPane) bd.getBodyItem("vmemo").getComponent();
//    nRefPanel.setTable(bd.getBillTable());
//    nRefPanel.getRefModel().setRefCodeField(nRefPanel.getRefModel().getRefNameField());
//    nRefPanel.getRefModel().setBlurFields(new String[] { nRefPanel.getRefModel().getRefNameField()});
//    nRefPanel.setAutoCheck(false);
//  }

  //��Ŀ����
  if (bd.getBodyItem("cproject") != null) {
    String sql = "(upper(isnull(bd_jobbasfil.sealflag,'N')) = 'N')";
    UIRefPane ref = (UIRefPane) (bd.getBodyItem("cproject").getComponent());
    String sqltemp = ref.getRefModel().getWherePart();
    if ((sqltemp != null) && (!(sqltemp.trim().equals("")))) {
      sql = sql + " and " + sqltemp;
    }
    ref.getRefModel().setWherePart(sql);
  }
  //�۸�Ǹ�
  if (bd.getBodyItem("nprice") != null) {
    UIRefPane refPrice = (UIRefPane) bd.getBodyItem("nprice").getComponent();
    refPrice.setMinValue(0);
    refPrice.setMaxValue(nc.vo.scm.field.pu.FieldMinMaxValue.MAX_NUM);
  }
  //�����ʷǸ�
  if (bd.getBodyItem("convertrate") != null) {
    UIRefPane refConvert = (UIRefPane) bd.getBodyItem("convertrate").getComponent();
    refConvert.setMinValue(0);
    refConvert.setMaxValue(nc.vo.scm.field.pu.FieldMinMaxValue.MAX_NUM);
  }
  //�����������Ǹ�
  if (bd.getBodyItem("ivalidday") != null) {
    UIRefPane refVld = (UIRefPane) bd.getBodyItem("ivalidday").getComponent();
    refVld.setMinValue(0);
    refVld.setMaxValue(nc.vo.scm.field.pu.FieldMinMaxValue.MAX_NUM);
  }

  //�����˻�����
  if (bd.getBodyItem("vbackreasonb") != null) {
    UIRefPane refPaneReason1 = new UIRefPane();
    refPaneReason1.setRefModel(new BackReasonRefModel());
    bd.getBodyItem("vbackreasonb").setComponent(refPaneReason1);
    refPaneReason1.getRefModel().setRefCodeField(refPaneReason1.getRefModel().getRefNameField());
    refPaneReason1.getRefModel().setBlurFields(new String[] { refPaneReason1.getRefModel().getRefNameField()});
    refPaneReason1.setAutoCheck(false);
    refPaneReason1.setReturnCode(true);
  }
  //�����˻����ɴ���
  //if (bd.getBodyItem("vbackreasonb") != null) {
    //UIRefPane refPanel = (UIRefPane) bd.getBodyItem("vbackreasonb").getComponent();
    //refPanel.setTable(bd.getBillTable());
    //refPanel.getRefModel().setRefCodeField(refPanel.getRefModel().getRefNameField());
    //refPanel.getRefModel().setBlurFields(new String[] { refPanel.getRefModel().getRefNameField()});
    //refPanel.setAutoCheck(false);
  //}

  //�ջ��ֿ�
  if (bd.getBodyItem("cwarehousename") != null) {
    UIRefPane refStore = (UIRefPane) bd.getBodyItem("cwarehousename").getComponent();
    refStore.setRefModel(new WarehouseRefModel(getCorpPrimaryKey()));
    refStore.getRefModel().addWherePart(" and UPPER(bd_stordoc.gubflag) <> 'Y' and UPPER(bd_stordoc.sealflag) <> 'Y' ");
  }
  //��λ�Ƿ��
  if (bd.getBodyItem("cstorename") != null) {
    UIRefPane refCarg = (UIRefPane) bd.getBodyItem("cstorename").getComponent();
    refCarg.getRefModel().addWherePart("and UPPER(bd_cargdoc.sealflag) <> 'Y' ");
  }
  //����������
  if (bd.getBodyItem("cassistunitname") != null) {
    ((UIRefPane) (bd.getBodyItem("cassistunitname").getComponent())).setIsCustomDefined(true);
    ((UIRefPane) (bd.getBodyItem("cassistunitname").getComponent())).setRefModel(new OtherRefModel("��������λ"));
    ((UIRefPane) (bd.getBodyItem("cassistunitname").getComponent())).setReturnCode(false);
    ((UIRefPane) (bd.getBodyItem("cassistunitname").getComponent())).setRefInputType(UIRefPane.REFINPUTTYPE_CODE);
    ((UIRefPane) (bd.getBodyItem("cassistunitname").getComponent())).setCacheEnabled(false);
  }

}

/**
 * �Ƿ��˻�����
 */
private boolean isBackBill() {
  if (!getBillCardPanel().isVisible()) {
    SCMEnv.out("1.bisback='N'");
    return false;
  }
  if (getBillCardPanel().getHeadItem("bisback") == null
    || getBillCardPanel().getHeadItem("bisback").getValue() == null
    || getBillCardPanel().getHeadItem("bisback").getValue().trim().equals("")) {
    SCMEnv.out("2.bisback='N'");
    return false;
  }
  if (new UFBoolean(getBillCardPanel().getHeadItem("bisback").getValue())
    .booleanValue()) {
    SCMEnv.out("3.bisback='Y'");
    return true;
  }
  SCMEnv.out("4.bisback='N'");
  return false;
}

/**
 * ���ܣ�(�������޸ĵı���)���ݵ���������ϵ�жϵ��������Ƿ����
 * ������
 * ���أ��㷨(�������޸ĵı���):
     naccumchecknum == null(0) && (nelignum + nnotelignum != null(0)) ���� true
 * ���⣺
 * ���ڣ�(2002-9-12 13:03:45)
 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
 * @return boolean
 * @param e nc.ui.pub.bill.BillEditEvent
 */
private boolean isCheckFree(BillEditEvent e) {
  //��ǰ���ݱ����ϣ��key = ��ID��
  Hashtable hRowIdBody = new Hashtable();
  for (int i = 0;
    i < getCacheVOs()[getDispIndex()].getChildrenVO().length;
    i++) {
    hRowIdBody.put(
      getCacheVOs()[getDispIndex()].getChildrenVO()[i].getAttributeValue(
        "carriveorder_bid"),
      getCacheVOs()[getDispIndex()].getChildrenVO()[i]);
  }
  String sRowId =
    (String) getBillCardPanel().getBillModel().getValueAt(
      e.getRow(),
      "carriveorder_bid");
  ArriveorderItemVO item = (ArriveorderItemVO) hRowIdBody.get(sRowId);
  UFDouble nAcc = null, nElg = null, nNotElg = null;
  nAcc =
    (item.getNaccumchecknum() == null
      || item.getNaccumchecknum().doubleValue() == 0)
      ? new UFDouble(0)
      : item.getNaccumchecknum();
  nElg =
    (item.getNelignum() == null || item.getNelignum().doubleValue() == 0)
      ? new UFDouble(0)
      : item.getNelignum();
  nNotElg =
    (item.getNnotelignum() == null || item.getNnotelignum().doubleValue() == 0)
      ? new UFDouble(0)
      : item.getNnotelignum();
  if (nAcc.doubleValue() == 0 && nElg.add(nNotElg).doubleValue() != 0) {
    return true;
  }
  return false;
}

/**
 * �Ƿ�ֻ���ڵ������˻�һ�ֵ���(�������ڽ���)
 */
private boolean isOnlyOneTypeBill() {
  BillModel bm = getBillListPanel().getHeadBillModel();
  int iRowCnt = bm.getRowCount();
  if (iRowCnt <= 0)
    return true;
  iRowCnt = getBillListPanel().getHeadTable().getSelectedRowCount();
  if (iRowCnt <= 1)
    return true;
  Object objTmp = null;
  Vector vJudge = new Vector();
  for (int i = 0; i < iRowCnt; i++) {
    if (bm.getRowState(i) == BillModel.SELECTED) {
      objTmp = bm.getValueAt(i, "bisback");
      if (vJudge.size() > 0 && !vJudge.contains(objTmp))
        return false;
      vJudge.addElement(objTmp);
    }
  }
  vJudge = null;
  return true;
}

/**
 * @���ܣ����ر�ͷ��������
 */
private void loadBDData() {
  /*��������*/
  String strFormula[] = new String[]{"vendor->getColValue(bd_cubasdoc,custshortname,pk_cubasdoc,cvendorbaseid)",
      "cdeptname->getColValue(bd_deptdoc,deptname,pk_deptdoc,cdeptid)",
      "cemployee->getColValue(bd_psndoc,psnname,pk_psndoc,cemployeeid)",
      "ctransmode->getColValue(bd_sendtype,sendname,pk_sendtype,ctransmodeid)",
      "creceivepsnlist->getColValue(bd_psndoc,psnname,pk_psndoc,creceivepsn)",
      "cstoreorganizationname->getColValue(bd_calbody,bodyname,pk_calbody,cstoreorganization)",
      "cbiztypename->getColValue(bd_busitype,businame,pk_busitype,cbiztype)"};
  String strVarValue = null, strValue = null;
  nc.ui.pub.formulaparse.FormulaParse parse = new nc.ui.pub.formulaparse.FormulaParse();
  Hashtable hData[] = new Hashtable[7];
  UIRefPane refpnl[] = new UIRefPane[7];
  
  for(int i = 0; i < 7; i++) hData[i] = new Hashtable();
  /*��Ӧ��*/
  if (getCacheVOs()[getDispIndex()] != null && getCacheVOs()[getDispIndex()].getParentVO() != null) {
    strVarValue = (String) getCacheVOs()[getDispIndex()].getParentVO().getAttributeValue("cvendorbaseid");
    refpnl[0] = (UIRefPane) getBillCardPanel().getHeadItem("cvendormangid").getComponent();
    strValue = refpnl[0].getUITextField().getText();
    if (strVarValue != null && (strValue == null || strValue.trim().equals(""))) {
      hData[0].put("cvendorbaseid", strVarValue);
    }
  }
  /*����*/
  strVarValue = getBillCardPanel().getHeadItem("cdeptid").getValue();
  refpnl[1] = (UIRefPane) getBillCardPanel().getHeadItem("cdeptid").getComponent();
  strValue = refpnl[1].getUITextField().getText();
  if (strVarValue != null && (strValue == null || strValue.trim().equals(""))) {
    hData[1].put("cdeptid", strVarValue);
  }
  /*ҵ��Ա*/
  strVarValue = getBillCardPanel().getHeadItem("cemployeeid").getValue();
  refpnl[2] = (UIRefPane) getBillCardPanel().getHeadItem("cemployeeid").getComponent();
  strValue = refpnl[2].getUITextField().getText();
  if (strVarValue != null && (strValue == null || strValue.trim().equals(""))) {
    hData[2].put("cemployeeid", strVarValue);
  }
  /*���˷�ʽ*/
  strVarValue = getBillCardPanel().getHeadItem("ctransmodeid").getValue();
  refpnl[3] = (UIRefPane) getBillCardPanel().getHeadItem("ctransmodeid").getComponent();
  strValue = refpnl[3].getUITextField().getText();
  if (strVarValue != null && (strValue == null || strValue.trim().equals(""))) {
    hData[3].put("ctransmodeid", strVarValue);
  }
  /*�ջ���*/
  strVarValue = getBillCardPanel().getHeadItem("creceivepsn").getValue();
  refpnl[4] = (UIRefPane) getBillCardPanel().getHeadItem("creceivepsn").getComponent();
  strValue = refpnl[4].getUITextField().getText();
  if (strVarValue != null && (strValue == null || strValue.trim().equals(""))) {
    hData[4].put("creceivepsn", strVarValue);
  }
  /*�����֯*/
  strVarValue = getBillCardPanel().getHeadItem("cstoreorganization").getValue();
  refpnl[5] = (UIRefPane) getBillCardPanel().getHeadItem("cstoreorganization").getComponent();
  strValue = refpnl[5].getUITextField().getText();
  if (strVarValue != null && (strValue == null || strValue.trim().equals(""))) {
    hData[5].put("cstoreorganization", strVarValue);
  }
  /*ҵ������*/
  strVarValue = getBillCardPanel().getHeadItem("cbiztype").getValue();
  refpnl[6] = (UIRefPane) getBillCardPanel().getHeadItem("cbiztype").getComponent();
  strValue = refpnl[6].getUITextField().getText();
  if (strVarValue != null && (strValue == null || strValue.trim().equals(""))) {
    hData[6].put("cbiztype", strVarValue);
  }
  
  Vector v1 = new Vector(), v2 = new Vector(), v3 = new Vector();
  for(int i = 0; i < 7; i++){
    if(hData[i].size() > 0){
      v1.addElement(strFormula[i]);
      v2.addElement(hData[i]);
      v3.addElement(new Integer(i));
    }
  }
  if(v1.size() > 0){
    strFormula = new String[v1.size()];
    v1.copyInto(strFormula);
    hData = new Hashtable[v2.size()];
    v2.copyInto(hData);
    
    parse.setExpressArray(strFormula);
    parse.setDataSArray(hData);
    String s[] = parse.getValueS();
    if(s != null && s.length == v1.size()){
      for(int i = 0; i < v1.size(); i++){
        int j = ((Integer)v3.elementAt(i)).intValue();
        if (s != null && j < s.length) {
          refpnl[j].getUITextField().setText(s[j]);
        }
      }
    }
  }
}



public void mouse_doubleclick(nc.ui.pub.bill.BillMouseEnent e) {
  if (e.getPos() == BillItem.HEAD) {
    if (getStateStr().equals("ת���б�")) {
    	InformationCostVO[] vos = null;
    	vos = (InformationCostVO[])getBillListPanel().getBodyBillModel("jj_scm_informationcost").getBodyValueVOs(InformationCostVO.class.getName());
      setM_strState("ת���޸�");
      onCardNew();
//  	String pk =	getBillCardPanel().getHeadItem("carriveorderid").getValue();
//	String sql = "cbillid = '"+pk+"' and dr = 0";
//	
//try {
//	 vos = (InformationCostVO[])JJPuScmPubHelper.querySmartVOs(InformationCostVO.class, null, sql);
//} catch (Exception e1) {
//	// TODO Auto-generated catch block
//	e1.printStackTrace();
//}
if(vos!=null&&vos.length!=0){
	getBillCardPanel().getBillModel("jj_scm_informationcost").setBodyDataVO(vos);
	getBillCardPanel().getBillModel("jj_scm_informationcost").execLoadFormula();
}else{
	//20101013-11-48  MeiChao ����Ϊ��ʱ,�����ʷ������Ϣ.
	getBillCardPanel().getBillModel("jj_scm_informationcost").setBodyDataVO(null);
}
    } else {
      //���û�е����壬����Ϊ����������
      ArriveorderItemVO[] items =
        (ArriveorderItemVO[]) getBillListPanel().getBodyBillModel().getBodyValueVOs(
          ArriveorderItemVO.class.getName());
      if (items == null || items.length <= 0)
        return;
      //
      isFrmList = true;
      setM_strState("�������");
      onCard();
    }
  }
}

/**
 * ����:ִ������
 */
private void onAudit(ButtonObject bo) {
  showHintMessage(m_lanResTool.getStrByID("40040301","UPP40040301-000118")/*@res "��������..."*/);
  ArriveorderVO vo = getCacheVOs()[getDispIndex()];  
  //���������˼��������ڹ�ϣ������ʧ��ʱ�õ�
  String strPsnOld = vo.getHeadVO().getCauditpsn();
  UFDate dateAuditOld = vo.getHeadVO().getDauditdate();
  //
  try {
    //���������ˡ���������
    if (vo == null || vo.getParentVO() == null)
      return;
    //V31SP1:�����������ڲ���С�ڵ�����������
//    String strErr = PuTool.getAuditLessThanMakeMsg(new ArriveorderVO[] { vo },"dreceivedate","varrordercode", getClientEnvironment().getDate(),ScmConst.PO_Arrive);
//    if (strErr != null) {
//      throw new BusinessException(strErr);
//    }
    vo.getParentVO().setAttributeValue("dauditdate", getClientEnvironment().getDate());
    vo.getParentVO().setAttributeValue("cauditpsn", getOperatorId());
    vo.getParentVO().setAttributeValue("cuserid", getOperatorId());

    
    HashMap<String, String> mapConvertrate = new HashMap<String, String>();
    String sRowId = "";
    for (int i = 0; i < getBillCardPanel().getBodyPanel().getTable().getRowCount(); i++) {
      sRowId = PuPubVO.getString_TrimZeroLenAsNull(getBillCardPanel().getBillModel().getValueAt(i, "carriveorder_bid"));
      if(sRowId!=null){
        mapConvertrate.put(sRowId, PuPubVO.getString_TrimZeroLenAsNull(getBillCardPanel().getBillModel().getValueAt(i, "convertrate")));
      }
    }
    for (int j = 0; j < vo.getChildrenVO().length; j++) {
      vo.getChildrenVO()[j].setAttributeValue("convertrate",new UFDouble(mapConvertrate.get(vo.getChildrenVO()[j].getPrimaryKey())));
    }

    /*����*/
    PfUtilClient.processBatchFlow(null, "APPROVE", ScmConst.PO_Arrive, getClientEnvironment().getDate().toString(), new ArriveorderVO[] { vo }, null);
    
    if (!PfUtilClient.isSuccess()) {
      //����ʧ�ܣ��ָ������˼���������
      vo.getHeadVO().setCauditpsn(strPsnOld);
      vo.getHeadVO().setDauditdate(dateAuditOld);
      //
      showHintMessage(m_lanResTool.getStrByID("40040301","UPP40040301-000119")/*@res "����δ�ɹ�"*/);
      return;
    }
    /*�����ɹ���ˢ��*/   
    refreshVoFieldsByKey(vo,vo.getParentVO().getPrimaryKey());
    //
    getCacheVOs()[getDispIndex()] = vo;
    /*���ص���*/
    try {
      /*loadDataToCard();*/
    	getBillCardPanel().setBillValueVO(getCacheVOs()[getDispIndex()]);
    	lightRefreshUI();
    	setHeadValueByKey("vmemo", (String)getCacheVOs()[getDispIndex()].getParentVO().getAttributeValue("vmemo"));
    	  //function:��ѯ��ط�����Ϣ
    	  String pk = (String)getCacheVOs()[getDispIndex()].getHeadVO().getAttributeValue("carriveorderid");
    		String sql = "cbillid = '"+pk+"' and dr = 0";
    		InformationCostVO[] vos = null;

    		 try {
    			vos = (InformationCostVO[])JJPuScmPubHelper.querySmartVOs(InformationCostVO.class, null, sql);
    		} catch (Exception e) {
    			// TODO Auto-generated catch block
    			SCMEnv.out(e);
    		}
    	if(vos!=null&&vos.length!=0){
     		 getBillCardPanel().getBillModel("jj_scm_informationcost").setBodyDataVO(vos);
      		 getBillCardPanel().getBillModel("jj_scm_informationcost").execLoadFormula();
      		 }else{
      			 //20101014-11:51 MeiChao ���������ϢΪ��,�����������Ϣҳǩ��ʷ����.
      			getBillCardPanel().getBillModel("jj_scm_informationcost").setBodyDataVO(null); 
      		 }
    } catch (Exception e) {
      showHintMessage(m_lanResTool.getStrByID("40040301","UPP40040301-000120")/*@res "�����ɹ�,�����ص���ʱ�����쳣,��ˢ�½����ٽ�����������"*/);
    }
    /*ˢ�°�ť״̬*/
    setButtonsState();
    //
    showHintMessage(m_lanResTool.getStrByID("SCMCOMMON","UPPSCMCommon-000236")/*@res "�����ɹ�"*/);
  } catch (Exception e) {
    //����ʧ�ܣ��ָ������˼���������
    vo.getHeadVO().setCauditpsn(strPsnOld);
    vo.getHeadVO().setDauditdate(dateAuditOld);
    //
    showHintMessage(m_lanResTool.getStrByID("40040301","UPP40040301-000121")/*@res "�����쳣,����ʧ��"*/);
    SCMEnv.out(e);
    if (e instanceof java.rmi.RemoteException || e instanceof BusinessException || e instanceof PFBusinessException || e instanceof java.lang.NullPointerException ) {
      MessageDialog.showErrorDlg(this, m_lanResTool.getStrByID("40040301","UPP40040301-000099")/*@res "����"*/, e.getMessage());
    }
  }
}
private void lightRefreshUI() {
	BillModel bm = getBillCardPanel().getBillModel();	
//    	�رպϼƿ���
	boolean  bOldNeedCalc = bm.isNeedCalculate() ; 
	bm.setNeedCalculate(false) ;
	//ִ�м��ع�ʽ    
	bm.execLoadFormula();   
	    //�򿪺ϼƿ���
	bm.setNeedCalculate(bOldNeedCalc) ;
	getBillCardPanel().updateValue();
	//��������Դ
	loadSourceInfo();
	
	getBillCardPanel().updateUI();
}


/**
 * @���ܣ��������˻�-�ɹ�
 */
private void onBackPo() {
  /*���ò�ѯ������*/
  getBackRefUIPo().setQueyDlg(getBackQueDlgPo());
  /*����onQuery(),���������ݵ����ս���*/
  int iType = getBackRefUIPo().onQuery();
  if (iType == UIDialog.ID_CANCEL || iType == UIDialog.ID_NO) {
    return;
  }
  /*��ʾ���ս���*/
  iType = getBackRefUIPo().showModal();
  if (iType == UIDialog.ID_CANCEL || iType == UIDialog.ID_NO){
    return;
  }
  /*������*/
  if (getBackRefUIPo().getRetVos() == null || getBackRefUIPo().getRetVos().length <= 0)
    return;
  onExitFrmOrd((ArriveorderVO[]) getBackRefUIPo().getRetVos());
}

/**
 * @���ܣ��������˻�-ί��
 */
private void onBackSc() {
  /*���ò�ѯ������*/
  getBackRefUISc().setQueyDlg(getBackQueDlgSc());
  /*����onQuery(),���������ݵ����ս���*/
  int iType = getBackRefUISc().onQuery();
  if (iType == UIDialog.ID_CANCEL || iType == UIDialog.ID_NO) {
    return;
  }
  /*��ʾ���ս���*/
  iType = getBackRefUISc().showModal();
  if (iType == UIDialog.ID_CANCEL || iType == UIDialog.ID_NO){
    return;
  }
  /*������*/
  if (getBackRefUISc().getRetVos() == null || getBackRefUISc().getRetVos().length <= 0)
    return;
  onExitFrmOrd((ArriveorderVO[]) getBackRefUISc().getRetVos());
}

/**
 * @���ܣ�ѡȡһ��ҵ�����ͺ���
 */
private void onBusi(ButtonObject bo) {
  showHintMessage(m_lanResTool.getStrByID("40040301","UPP40040301-000122")/*@res "���ڳ�ʼ��ҵ������:"*/ + bo.getHint() + "...");
  /*���¼���ҵ�����Ͱ�ť��*/
  PfUtilClient.retAddBtn(
    m_btnAdds,
    getCorpPrimaryKey(),
    nc.vo.scm.pu.BillTypeConst.PO_ARRIVE,
    bo);
 // setButtons(m_aryArrCardButtons);
  setButtons(m_btnTree.getButtonArray());//add by QuSida 2010-8-31 (��ɽ����) ��ΪsetButtons(m_aryArrCardButtons)�е�m_aryArrCardButtons���������ο����İ�ť,��m_btnTree.getButtonArray()����
  updateButton(boInfoCost);
  m_btnAdds.setEnabled(true);
  updateButton(m_btnAdds);
  /*ˢ�´���*/
  updateButtons();
  updateButtonsAll();
  updateUI();
  showHintMessage(m_lanResTool.getStrByID("40040301","UPP40040301-000123")/*@res "��ǰ����ҵ������:"*/ + bo.getHint());
}


/**
 * ����ʵ�ָ÷�������Ӧ��ť�¼���
 * @version (00-6-1 10:32:59)
 *
 * @param bo ButtonObject
 */
public void onButtonClicked(nc.ui.pub.ButtonObject bo) {
  //
  boolean bCardShowing = getBillCardPanel().isShowing();
  //
  if(bCardShowing){
    onButtonClickedCard(bo);
  }else{
    onButtonClickedList(bo);
  }
}
/**
 * �������������鹦��ʵ��(������������)��ע���޸Ĵ˷���Ҫ��Ӧ�޸�nc.ui.rc.check.CheckUI.onCheck()����
 * <p>
 * <b>examples:</b>
 * <p>
 * ʹ��ʾ��
 * <p>
 * <b>����˵��</b>
 * <p>
 * @author czp
 * @time 2007-3-21 ����07:20:31
 * @
 */
private void onCheck(){
  if(getCacheVOs() == null 
      || getCacheVOs().length == 0
      || getCacheVOs()[getDispIndex()] == null
      ){
    return;
  }
  nc.vo.scm.pu.Timer timer = new nc.vo.scm.pu.Timer();
  timer.start();
  Hashtable<String, UFBoolean> hStorByChk = new Hashtable<String, UFBoolean>();
  
  //������������ʱ��ѯ�Ƿ��������������������
  ArriveorderVO voCurr = getCacheVOs()[getDispIndex()];
  int iLen = voCurr.getBodyLen();
  String[] saBid = new String[iLen];
  for(int i=0; i<iLen; i++){
    saBid[i] = voCurr.getBodyVo()[i].getPrimaryKey();
  }
  ArrayList aryTmp = null;
  try{
    aryTmp = ArriveorderHelper.getStoreByChkArray(saBid);
    if (aryTmp != null && aryTmp.size() > 0) {
      for (int i = 0; i < iLen; i++) {
        if (aryTmp.get(i) != null)
          hStorByChk.put(saBid[i], (UFBoolean) aryTmp.get(i));
      }
    }
  }catch(Exception e){
    MessageDialog.showErrorDlg(this, m_lanResTool.getStrByID("SCMCOMMON","UPPSCMCommon-000270")/*@res "��ʾ"*/, e.getMessage());
  }
  String strErrInfo = getCacheVOs()[getDispIndex()].judgeCanCheck(m_bQcEnabled, hStorByChk);
  if(strErrInfo != null){
    MessageDialog.showErrorDlg(this, m_lanResTool.getStrByID("SCMCOMMON","UPPSCMCommon-000270")/*@res "��ʾ"*/, strErrInfo);
    return;
  }
  timer.addExecutePhase(">>>�Ƿ��������������������ArriveorderHelper.getStoreByChkArray()");
  //��֯��д����
  
  String carriveorder_bid = null;
  String carriveorderid = null;
  String carriveorder_bts = null;
  String carriveorderts = null;
  ArrayList aryRewriteNum = new ArrayList();
  Vector<String> vStrLineId = new Vector<String>();
  Vector<String> vStrHeadId = new Vector<String>();
  Vector<String> vStrLineTs = new Vector<String>();
  Vector<String> vStrHeadTs = new Vector<String>();
  Vector<String> vStrCreporterid = new Vector<String>();
  Vector<String> vStrDreportdate = new Vector<String>();

  int iCnt = voCurr.getBodyLen();
  for (int i = 0; i < iCnt; i++) {
    //��д�õ�����ID��������ID
    carriveorder_bid = voCurr.getBodyVo()[i].getCarriveorder_bid();
    carriveorderid = voCurr.getBodyVo()[i].getCarriveorderid();
    carriveorder_bts = voCurr.getBodyVo()[i].getTs();
    carriveorderts = voCurr.getHeadVO().getTs();
    //��֯��д����
    UFDouble[] rewriteNum = new UFDouble[3];
    rewriteNum[0] = new UFDouble(0.0);
    rewriteNum[1] = new UFDouble(0.0);
    //�ۼƼ�������
    rewriteNum[2] = voCurr.getBodyVo()[i].getNarrvnum();
    aryRewriteNum.add(rewriteNum);
    vStrLineId.addElement(carriveorder_bid);
    vStrLineTs.addElement(carriveorder_bts);
    vStrHeadId.addElement(carriveorderid);
    vStrHeadTs.addElement(carriveorderts);

    //�ӽ����ȡ������ID���������ڡ�������ID V502
    String creporterid = getBillCardPanel().getBillModel().getValueAt(i, "creporterid")==null?null:getBillCardPanel().getBillModel().getValueAt(i, "creporterid").toString();//������ID
    String dreportdate = getBillCardPanel().getBillModel().getValueAt(i, "dreportdate")==null?null:getBillCardPanel().getBillModel().getValueAt(i, "dreportdate").toString();//��������
    vStrCreporterid.addElement(creporterid);
    vStrDreportdate.addElement(dreportdate);

  }

  boolean isTsChanged = false;
  //��̨���ò���
  ArrayList listPara = new ArrayList();
  /*
   * ����˵����
   * 0-----�ʼ��Ƿ�����
   * 1..4--�����ʼ쵥������ArriveorderBO_Client.crtQcBills
   * 5-----�Ƿ��д�ۼ��ʼ�����
   * 6..15-��д�ۼ��ʼ�����������ArriveorderBO_Client.rewriteNaccumchecknumMy
   */
  listPara.add(0,new UFBoolean(m_bQcEnabled));
  //��½����
  UFDate dBusinessDate = getClientEnvironment().getDate();
  //������ʱ��
  UFDateTime dtServerDateTime = ClientEnvironment.getServerTime();
  //Ŀ�����
  UFDateTime dtDateTime = new UFDateTime(dBusinessDate,new UFTime(dtServerDateTime.getTime()));
  //��������ͬһ�����µĲ������󣬽��˷����Ƶ���̨
  listPara.add(1,null);
  listPara.add(2,voCurr.getBodyVo());
  listPara.add(3,getOperatorId());
  listPara.add(4,dtDateTime);
  //�Ƿ��д�ۼ��ʼ�����
  listPara.add(5,UFBoolean.TRUE);
  //��֯��TS����
  String[] saLineIds = new String[vStrLineId.size()];
  String[] saHeadIds = new String[vStrHeadId.size()];
  String[] saLineTss = new String[vStrLineTs.size()];
  String[] saHeadTss = new String[vStrHeadTs.size()];
  vStrLineId.copyInto(saLineIds);
  vStrHeadId.copyInto(saHeadIds);
  vStrLineTs.copyInto(saLineTss);
  vStrHeadTs.copyInto(saHeadTss);
  //
  listPara.add(6,getCorpPrimaryKey());
  listPara.add(7,UFBoolean.TRUE);//������������(������ʱ�����ܲ�����)
  listPara.add(8,saLineIds);
  listPara.add(9,saHeadIds);
  listPara.add(10,aryRewriteNum);
  listPara.add(11,getOperatorId());
  listPara.add(12,saLineTss);
  listPara.add(13,saHeadTss);
  listPara.add(14,new UFBoolean(voCurr.isCheckOver()));//�Ƿ�Ϊ�ظ�����(�����������)
  listPara.add(15,UFBoolean.FALSE);//�ع��󣬽�����ǰ��̨�����ϲ�Ϊһ�Σ�ʱ���û�б仯

  String[] arrStrCreporterid = new String[vStrCreporterid.size()];
  String[] arrStrSreportdate = new String[vStrDreportdate.size()];
  vStrCreporterid.copyInto(arrStrCreporterid);
  vStrDreportdate.copyInto(arrStrSreportdate);
  listPara.add(16,new UFBoolean(isTsChanged));//
  listPara.add(17,new UFBoolean(isTsChanged));//
  listPara.add(18,new UFBoolean(isTsChanged));//
  listPara.add(19,arrStrCreporterid);
  listPara.add(20,arrStrSreportdate);
  timer.addExecutePhase(">>>�����ʼ쵥ǰ��������");
  try {
    //�ع�����������ͬһ�������µĲ�������
    String sRet = ArriveorderHelper.crtQcAndRewriteNum(listPara);
    //
    if (sRet != null) {
      MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON","UPPSCMCommon-000059")/*@res "����"*/, nc.ui.ml.NCLangRes.getInstance().getStrByID("40040303","UPP40040303-000013")/*@res "���α���ʧ�ܣ�"*/ + sRet);
      showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("common","4004COMMON000000060")/*@res "����ʧ��"*/);
      return;
    }
    timer.addExecutePhase(">>>�����ʼ켰��дʱ��");
    //
    if (sRet == null) {
      showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("common","4004COMMON000000058")/*@res "�ͼ�ɹ�"*/);
    }
    /*�ͼ�ɹ���ˢ��*/   
    refreshVoFieldsByKey(voCurr,voCurr.getParentVO().getPrimaryKey());
    //������Ҫ���ۼƱ��������õ���ǰvo��
    for (int i = 0; i < voCurr.getBodyLen(); i++) {
    	voCurr.getBodyVo()[i].setNaccumchecknum(voCurr.getBodyVo()[i].getNarrvnum());
    }
    //
    getCacheVOs()[getDispIndex()] = voCurr;
    /*���ص���*/
    try {
      /*loadDataToCard();*/
    	getBillCardPanel().setBillValueVO(getCacheVOs()[getDispIndex()]);
    	setHeadValueByKey("vmemo", (String)getCacheVOs()[getDispIndex()].getParentVO().getAttributeValue("vmemo"));
    	lightRefreshUI();
    	refreshCardData();
    } catch (Exception e) {
      SCMEnv.out(e);
      showHintMessage(m_lanResTool.getStrByID("common","4004COMMON000000060")/*@res "����ʧ��"*/);
    }
    timer.addExecutePhase(">>>ˢ�½���ʱ��");
    /*ˢ�°�ť״̬*/
    setButtonsState();
    timer.addExecutePhase(">>>ˢ�°�ť״̬");
    //
    showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("common","4004COMMON000000058")/*@res "�ͼ�ɹ�"*/);
  } catch (BusinessException b) {
    MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON","UPPSCMCommon-000059")/*@res "����"*/, b.getMessage());
    showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("common","4004COMMON000000060")/*@res "����ʧ��"*/);
  } catch (Exception b) {
    MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON","UPPSCMCommon-000059")/*@res "����"*/, b.getMessage());
    showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("common","4004COMMON000000060")/*@res "����ʧ��"*/);
  }
  timer.showAllExecutePhase("�����ʼ����");
}



/**
 * ��Ƭ��ť�¼���Ӧ��
 * <p>
 * <b>examples:</b>
 * <p>
 * ʹ��ʾ��
 * <p>
 * <b>����˵��</b>
 * @param bo
 * <p>
 * @author czp
 * @time 2007-3-20 ����04:12:37
 */
private void onButtonClickedCard(ButtonObject bo){

   if (bo == m_btnReSortRowNo){
    onReSortRowNo();
  } else if (bo == m_btnSplitPrint) {
    //�ֵ���ӡ
    onSplitPrint();
  } else if (bo == m_btnCardEdit){
	  onCardEdit();
  }else if (bo == m_btnPstLineTail){
	    onPasteLineToTail();
  } else if (bo == m_btnCheck) {
    onCheck();
  } else if (bo.getParent() == m_btnBusiTypes) {
    //
    bo.setSelected(true);
    //
    isChangeBusitype = true;
    onBusi(bo);
  } else if (bo == m_btnDiscard){
    onDiscard();
  } else if (bo.getParent() == m_btnAdds) {
    //onAdd();
    int iIndexBillType = bo.getTag().indexOf(":");
    String strBillType = bo.getTag().substring(0, iIndexBillType);
    if (strBillType.equals(ScmConst.SC_Order)) {
      if (!nc.ui.sm.user.UserPowerUI.isEnabled(getCorpPrimaryKey(), "SC")) {
        MessageDialog.showErrorDlg(this, m_lanResTool.getStrByID("SCMCOMMON","UPPSCMCommon-000059")/*@res "����"*/, m_lanResTool.getStrByID("40040301","UPP40040301-000124")/*@res "ί�ⶩ��ģ��û�����ã�"*/);
        return;
      }
    } else if (!strBillType.equals("21")) {
      MessageDialog.showErrorDlg(this, m_lanResTool.getStrByID("SCMCOMMON","UPPSCMCommon-000059")/*@res "����"*/, m_lanResTool.getStrByID("40040301","UPP40040301-000125")/*@res "������ֻ���ɲɹ�������ί�ⶩ�����ɣ�"*/);
      return;
    }
//    PfUtilClient.childButtonClicked(bo, getCorpPrimaryKey(), getFuncId(), getOperatorId(), ScmConst.PO_Arrive, this);
//    if (PfUtilClient.isCloseOK()) {
//      ArriveorderVO[] retVOs = (ArriveorderVO[]) PfUtilClient.getRetVos();
//      onExitFrmOrd(retVOs);
//    }
    //n502�޸� ֧����ת����ʽ
   SourceRefDlg.childButtonClicked(bo, getCorpPrimaryKey(), getFuncId(), getOperatorId(), ScmConst.PO_Arrive, this);
    if (SourceRefDlg.isCloseOK()) {
    	 Object[] o1 = SourceRefDlg.getRetSrcVos();
    	 Object o2 = SourceRefDlg.getRetSrcVo();
      ArriveorderVO[] retVOs = (ArriveorderVO[]) SourceRefDlg.getRetsVos();
     
      
      //add by QuSida 2010-9-2 (��ɽ����) --- begin
      //function�� ���ݵ�����VO��ѯ�����ݶ�Ӧ�ķ�����Ϣ
      ArrayList<String> pkList = new ArrayList<String>();
      //��List�洢��Դ���ݵ�����
      for(int i = 0; i < retVOs.length ; i++){
    	  ArriveorderItemVO[] items = retVOs[i].getBodyVo();
    	  for (int j = 0; j < items.length; j++) {
			String srcPK = items[i].m_cupsourcebillid;
			//�ų������������ͬһ���ݶ�����List�д洢����ظ����������Ŀ���
			if(pkList.size() == 0 || !pkList.contains(srcPK)){
				pkList.add(srcPK);
			}
		}
      }
      //��ѯ��where����
      StringBuffer whereSql = new StringBuffer("11 = 11");
      if(pkList.size()>0&&pkList!=null){
    	  whereSql.append(" and");
      }
      for (int i = 0; i < pkList.size(); i++) {
    	  if(i == pkList.size()-1){
    		  whereSql.append(" cbillid = '"+pkList.get(i)+"'");
    	  }
    	  else {
    		  whereSql.append(" cbillid = '"+pkList.get(i)+"' or");
    	  }
  	}      
      String sql = "select nvl(sum(naccumarrvnum),0) from po_order_b where dr = 0 and corderid = '"+pkList.get(0)+"'";
      Object o = null;
           try {
        	    IUAPQueryBS query1 = NCLocator.getInstance().lookup(IUAPQueryBS.class);
                o = query1.executeQuery(sql, new ColumnProcessor());        		  		
               
              //�ڶ�������Ϊnull��ʾ��ѯȫ���ֶ�,����ѯ����洢���ڴ���
              vos =  (InformationCostVO[]) JJPuScmPubHelper.querySmartVOs(InformationCostVO.class, null, whereSql.toString());
        	   
		} catch (Exception e) {
			// TODO Auto-generated catch block
			SCMEnv.out(e);
			return;
		}
		 arrnum = new UFDouble(o.toString());
	 //add by QuSida 2010-9-2 (��ɽ����) --- end
      onExitFrmOrd(retVOs);
		CircularlyAccessibleValueObject[] a = retVOs[0].getChildrenVO();
	  // int temp = getBillCardPanel().getBillModel("table").getRowCount();
	   arrnumber = new UFDouble(0.0);
				for (int i = 0; i < a.length; i++) {
					arrnumber = arrnumber.add(new UFDouble(a[i].getAttributeValue("narrvnum").toString()));
				}

      for (int i = 0; i < vos.length; i++) {
//		vos[i].setAttributeValue("ninvoriginalcursummny", vos[i].getNoriginalcursummny());
		vos[i].setAttributeValue("ninvoriginalcurmny", vos[i].getNoriginalcurmny().add(arrnum.multiply(vos[i].getNoriginalcurprice())));
	}

      getBillListPanel().getBodyBillModel("jj_scm_informationcost").setBodyDataVO(vos); //add by QuSida 2010-9-2 (��ɽ����) ����ѯ�����ķ�����Ϣд��������
      getBillListPanel().getBodyBillModel("jj_scm_informationcost").execLoadFormula();
    }
  } else if (bo == m_btnBackPo) {
    onBackPo();
  } else if (bo == m_btnBackSc) {
    onBackSc();
  } else if (bo == m_btnLocate) {
    onLocate();
  } else if (bo == m_btnPrint) {
    //��ӡ�������ӡ����
    onCardPrint();
  } else if (bo == m_btnCombin) {
    //�ϲ���ʾ��ӡ
    onCombin();
  } else if (bo == m_btnPrintPreview) {
    //��ӡԤ���������ӡ����
    onCardPrintPreview();
  } else if (bo == m_btnList){
    onList();
  } else if (bo == m_btnModify){
    onModify();
    //�ù�굽��ͷ��һ���ɱ༭��Ŀ
    getBillCardPanel().transferFocusTo(BillCardPanel.HEAD);
  } else if (bo == m_btnDelLine){
    onDeleteLine();
  } else if (bo == m_btnCpyLine){
    onCopyLine();
  } else if (bo == m_btnPstLine){
    onPasteLine();
  } else if (bo == m_btnSave){
    onSave();
  } else if (bo == m_btnCancel){
    onCancel();
  } else if (bo == m_btnQuery){
    onQuery();
  } else if (bo == m_btnFirst){
    onFirst();
  } else if (bo == m_btnPrev){
    onPrevious();
  } else if (bo == m_btnNext){
    onNext();
  } else if (bo == m_btnLast){
    onLast();
  } else if (bo == m_btnRefresh){
    onRefresh();
  }
  /*����V5֧��������*******************************/
  else if(bo == m_btnSendAudit){
    onSendAudit();
  } else if(bo == m_btnAudit){
    onAudit(bo);
  } else if(bo == m_btnUnAudit){
    onUnAudit(bo);
  } else if(bo == m_btnQueryForAudit){
    onQueryForAudit();
  /*����V5֧��������******************************/
  } else if (bo == m_btnUsable) {
    onQueryInvOnHand();
  } else if (bo == m_btnQueryBOM) {
    onQueryBOM();
  } else if (bo == m_btnDocument){
    onDocument();
  } else if (bo == m_btnLookSrcBill){
    onLnkQuery();
  } else if(bo == m_btnQuickReceive){
    onQuickArr();
  } else if (bo == m_btnCreateCard){
	  onCreateCard();
  } else if (bo == m_btnDeleteCard){
	  onDeleteCard();
//  } else if (bo == m_btnSerialNO){
//	  onSNAssign();
  }
  //֧�ֲ�ҵ��������չ
  else{
    onExtendBtnsClick(bo);
  }
}

/**
 * �б�ť�¼���Ӧ��
 * <p>
 * <b>examples:</b>
 * <p>
 * ʹ��ʾ��
 * <p>
 * <b>����˵��</b>
 * @param bo
 * <p>
 * @author czp
 * @time 2007-3-20 ����04:13:02
 */
private void onButtonClickedList(ButtonObject bo){

  if (bo == m_btnPrintList) {
    //����ӡ
    onBatchPrint();
  } else if (bo == m_btnSplitPrint) {
    //�ֵ���ӡ
    onSplitPrint();
  } else if (bo == m_btnPrintPreviewList) {
    //����ӡԤ��
    onBatchPrintPreview();
  } else if (bo == m_btnDiscardList){
    onDiscardSelected();
  } else if (bo == m_btnCard) {
    onCard();
  } else if (bo == m_btnModifyList) {
    loadBatchDocInfo(true);
    if (getStateStr().equals("ת���б�")) {
      onCardNew();
      if(vos!=null&&vos.length!=0){
    	  getBillCardPanel().getBillData().setBodyValueVO("jj_scm_informationcost", vos);//add by QuSida 2010-9-2 (��ɽ����) ����ѯ�����ķ�����Ϣд��������
          getBillCardPanel().getBillModel("jj_scm_informationcost").execLoadFormula();
      }else{
			 //20101014-11:51 MeiChao ���������ϢΪ��,�����������Ϣҳǩ��ʷ����.
			getBillCardPanel().getBillModel("jj_scm_informationcost").setBodyDataVO(null); 
		 }
    } else {
      onModifyList();
      if(vos!=null&&vos.length!=0){
    	  getBillCardPanel().getBillData().setBodyValueVO("jj_scm_informationcost", vos);//add by QuSida 2010-9-2 (��ɽ����) ����ѯ�����ķ�����Ϣд��������

      }else{
			 //20101014-11:51 MeiChao ���������ϢΪ��,�����������Ϣҳǩ��ʷ����.
			getBillCardPanel().getBillModel("jj_scm_informationcost").setBodyDataVO(null); 
		 }
    }
    //�ù�굽��ͷ��һ���ɱ༭��Ŀ
    getBillCardPanel().transferFocusTo(BillCardPanel.HEAD);
  } else if (bo == m_btnEndCreate) {
    onEndCreate();
  } else if (bo == m_btnQueryList){
    onQuery();
  } else if (bo == m_btnSelectAll){
    onSelectAll();
  } else if (bo == m_btnSelectNo){
    onSelectNo();
  } else if (bo == m_btnRefreshList){
    onRefresh();
  } else if (bo == m_btnUsableList) {
    onQueryInvOnHand();
  } else if (bo == m_btnQueryBOMList) {
    onQueryBOM();
  } else if (bo == m_btnDocumentList){
    onDocument();
  } else if(bo == m_btnAudit){
    onAuditList(bo);
  } else if(bo == m_btnUnAudit){
    onUnAuditList(bo);
  } else{
    onExtendBtnsClick(bo);
  }
}
/**
 * �����������ܡ�
 * <p>
 * <b>examples:</b>
 * <p>
 * ʹ��ʾ��
 * <p>
 * <b>����˵��</b>
 * <p>
 * @author czp
 * @time 2007-3-27 ����01:17:02
 */
private void onAuditList(ButtonObject bo) {

  Vector<ArriveorderVO> vSubVos = new Vector<ArriveorderVO>();
  ArriveorderHeaderVO headVO = new ArriveorderHeaderVO();
  
  //���������˼��������ڹ�ϣ������ʧ��ʱ�õ�
  HashMap<String, ArrayList<Object>> mapAuditInfo = new HashMap<String, ArrayList<Object>>();
  ArrayList<Object> listAuditInfo = null;
  
  //�����Ź����Ļ�������
  int iRealPos = 0;
  for (int i = 0;
    i < getBillListPanel().getHeadBillModel().getRowCount();
    i++) {
    if (getBillListPanel().getHeadBillModel().getRowState(i) == BillModel.SELECTED) {
      iRealPos = i;
      iRealPos =
      nc.ui.pu.pub.PuTool.getIndexBeforeSort(getBillListPanel(), iRealPos);
      //�����ˡ���������
      headVO = (ArriveorderHeaderVO) getCacheVOs()[iRealPos].getParentVO();
      //����ʧ��ʱ�õ������������˼��������ڹ�ϣ��
      if(PuPubVO.getString_TrimZeroLenAsNull(headVO.getCauditpsn()) != null){
        listAuditInfo = new ArrayList<Object>();
        listAuditInfo.add(headVO.getCauditpsn());
        listAuditInfo.add(headVO.getDauditdate());
        mapAuditInfo.put(headVO.getPrimaryKey(),listAuditInfo);
      }
      //�������ǲ���Ա
      headVO.setCauditpsn(getOperatorId());
      headVO.setDauditdate(PoPublicUIClass.getLoginDate());
      //
      vSubVos.add(getCacheVOs()[iRealPos]);
    }
  }
  ArriveorderVO[] arrivevos = null;
  if (vSubVos.size() > 0) {
    arrivevos = new ArriveorderVO[vSubVos.size()];
    vSubVos.copyInto(arrivevos);
  }
  try {
    //���ò���Ա
    for (int i = 0; i < arrivevos.length; i++) {
      arrivevos[i].getParentVO().setAttributeValue("cuserid", getOperatorId());
    }
    boolean isSucc = false;
//    //V31SP1:�����������ڲ���С�ڵ����������� V5.02 �ع�
//    String strErr = PuTool.getAuditLessThanMakeMsg(arrivevos,"dreceivedate","varrordercode", ClientEnvironment.getInstance().getDate(),ScmConst.PO_Arrive);
//    if (strErr != null && strErr.trim().length() > 0) {
//      throw new BusinessException(strErr);
//    }
    //����ǰ�������
    ArriveorderHeaderVO[] heads = new ArriveorderHeaderVO[arrivevos.length];
    for (int i = 0; i < arrivevos.length; i++) {
      heads[i] = (ArriveorderHeaderVO) arrivevos[i].getParentVO();
    }
    //����δ������ĵ�����
    //arrivevos = ArriveorderBO_Client.getAllWithBody(heads);
    arrivevos = RcTool.getRefreshedVOs(arrivevos);
    //

    PfUtilClient.processBatchFlow(
        this,
        "APPROVE",
        ScmConst.PO_Arrive,
        ClientEnvironment.getInstance().getDate().toString(),
        arrivevos,
        null);

    isSucc = PfUtilClient.isSuccess();
    if (isSucc) {
      //ˢ��ǰ����ʾ����
      //displayOthersVOs(vSubVos);
      onRefresh();
/*      *//************��¼ҵ����־*************//*
      if(arrivevos!=null && arrivevos.length > 0){
    	  Operlog operlog=new Operlog();
    	  for (ArriveorderVO arriveorderVO : arrivevos) {
    		  arriveorderVO.getOperatelogVO().setOpratorname(nc.ui.pub.ClientEnvironment.getInstance().getUser().getUserName());
    		  arriveorderVO.getOperatelogVO().setCompanyname(nc.ui.pub.ClientEnvironment.getInstance().getCorporation().getUnitname());
    		  arriveorderVO.getOperatelogVO().setOperatorId(nc.ui.pub.ClientEnvironment.getInstance().getUser().getPrimaryKey());
    		  operlog.insertBusinessExceptionlog(arriveorderVO, "����", "����", nc.vo.scm.funcs.Businesslog.MSGMESSAGE, nc.vo.scm.pu.BillTypeConst.PO_ARRIVE,
						nc.ui.sm.cmenu.Desktop.getApplet().getParameter("USER_IP"));
    	  }
      }
      *//************��¼ҵ����־* end ************/
      showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("common","4004COMMON000000071")/*@res "��˳ɹ�"*/);
    } else {
      //���������˼���������
      if(mapAuditInfo.size() > 0){
        for (int i = 0; i < arrivevos.length; i++) {
          headVO = arrivevos[i].getHeadVO();
          if(!mapAuditInfo.containsKey(headVO.getPrimaryKey())){
            headVO.setCauditpsn(null);
            headVO.setDauditdate(null);
            continue;
          }          
          listAuditInfo = (ArrayList<Object>)mapAuditInfo.get(headVO.getPrimaryKey());
          headVO.setCauditpsn((String)listAuditInfo.get(0));
          headVO.setDauditdate((UFDate)listAuditInfo.get(1));
        }
      }
      showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("common","4004COMMON000000072")/*@res "���ʧ��"*/);
    }
  } catch (nc.vo.pub.BusinessException e) {
    //���������˼���������
    if(mapAuditInfo.size() > 0){
      for (int i = 0; i < arrivevos.length; i++) {
        headVO = arrivevos[i].getHeadVO();
        if(!mapAuditInfo.containsKey(headVO.getPrimaryKey())){
          headVO.setCauditpsn(null);
          headVO.setDauditdate(null);
          continue;
        }          
        listAuditInfo = (ArrayList<Object>)mapAuditInfo.get(headVO.getPrimaryKey());
        headVO.setCauditpsn((String)listAuditInfo.get(0));
        headVO.setDauditdate((UFDate)listAuditInfo.get(1));
      }
    }
    reportException(e);
    showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("common","4004COMMON000000072")/*@res "���ʧ��"*/);
    MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON","UPPSCMCommon-000422")/*@res "ҵ���쳣"*/, e.getMessage());
  } catch (Exception e) {
    //���������˼���������
    if(mapAuditInfo.size() > 0){
      for (int i = 0; i < arrivevos.length; i++) {
        headVO = arrivevos[i].getHeadVO();
        if(!mapAuditInfo.containsKey(headVO.getPrimaryKey())){
          headVO.setCauditpsn(null);
          headVO.setDauditdate(null);
          continue;
        }          
        listAuditInfo = (ArrayList<Object>)mapAuditInfo.get(headVO.getPrimaryKey());
        headVO.setCauditpsn((String)listAuditInfo.get(0));
        headVO.setDauditdate((UFDate)listAuditInfo.get(1));
      }
    }
    reportException(e);
    showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("common","4004COMMON000000072")/*@res "���ʧ��"*/);
    if (e instanceof java.rmi.RemoteException){
      MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON","UPPSCMCommon-000422")/*@res "ҵ���쳣"*/, e.getMessage());
    }
  }
}
/**
 * ��������
 * <p>
 * <b>examples:</b>
 * <p>
 * ʹ��ʾ��
 * <p>
 * <b>����˵��</b>
 * <p>
 * @author czp
 * @time 2007-3-27 ����01:30:13
 */
private void onUnAuditList(ButtonObject bo) {

  Vector<ArriveorderVO> vSubVos = new Vector<ArriveorderVO>();
  int rowCount = getBillListPanel().getHeadBillModel().getRowCount();
  BillModel bm = getBillListPanel().getHeadBillModel();
  ArriveorderVO vo = null;
  ArriveorderVO[] arrivevos = null;
  
  //���������˼��������ڹ�ϣ������ʧ��ʱ�õ�
  HashMap<String, ArrayList<Object>> mapAuditInfo = new HashMap<String, ArrayList<Object>>();
  ArrayList<Object> listAuditInfo = null;
  
  //�����Ź����Ļ�������
  int iRealPos = 0;
  for (int i = 0; i < rowCount; i++) {
    if (bm.getRowState(i) == BillModel.SELECTED) {
      iRealPos = i;
      iRealPos =
        nc.ui.pu.pub.PuTool.getIndexBeforeSort(getBillListPanel(), iRealPos);
      vo = getCacheVOs()[iRealPos];
      vSubVos.add(vo);
    }
  }
  ArriveorderHeaderVO headVO = null;
  if (vSubVos.size() > 0) {
    arrivevos = new ArriveorderVO[vSubVos.size()];
    vSubVos.copyInto(arrivevos);
    try {
      //���ò���Ա
      for (int i = 0; i < arrivevos.length; i++) {
        //����ʧ��ʱ�õ������������˼��������ڹ�ϣ��
        headVO = arrivevos[i].getHeadVO();
        if(PuPubVO.getString_TrimZeroLenAsNull(headVO.getCauditpsn()) != null){
          listAuditInfo = new ArrayList<Object>();
          listAuditInfo.add(headVO.getCauditpsn());
          listAuditInfo.add(headVO.getDauditdate());
          mapAuditInfo.put(headVO.getPrimaryKey(),listAuditInfo);
        }
        arrivevos[i].getParentVO().setAttributeValue("cuserid", getOperatorId());
      }
      //����ǰ�������
      ArriveorderHeaderVO[] heads = new ArriveorderHeaderVO[arrivevos.length];
      for (int i = 0; i < arrivevos.length; i++) {
        heads[i] = (ArriveorderHeaderVO) arrivevos[i].getParentVO();
      }
      //����δ������ĵ�����
      arrivevos = RcTool.getRefreshedVOs(arrivevos);
      //
      boolean isSucess = false;

      PfUtilClient.processBatch(
          this,
          "UNAPPROVE",
          ScmConst.PO_Arrive,
          ClientEnvironment.getInstance().getDate().toString(),
          arrivevos,
          null);

      isSucess = PfUtilClient.isSuccess();
      if (isSucess) {
        //ˢ��ǰ����ʾ����
        //displayOthersVOs(vSubVos);
    	  /************��¼ҵ����־*************/
    	  if(arrivevos!=null && arrivevos.length > 0){
    		  Operlog operlog=new Operlog();
    		  for (ArriveorderVO arriveorderVO : arrivevos) {
    			  arriveorderVO.getOperatelogVO().setOpratorname(nc.ui.pub.ClientEnvironment.getInstance().getUser().getUserName());
    			  arriveorderVO.getOperatelogVO().setCompanyname(nc.ui.pub.ClientEnvironment.getInstance().getCorporation().getUnitname());
    			  arriveorderVO.getOperatelogVO().setOperatorId(nc.ui.pub.ClientEnvironment.getInstance().getUser().getPrimaryKey());
    			  operlog.insertBusinessExceptionlog(arriveorderVO, "����", "����", nc.vo.scm.funcs.Businesslog.MSGMESSAGE, nc.vo.scm.pu.BillTypeConst.PO_ARRIVE,
  						nc.ui.sm.cmenu.Desktop.getApplet().getParameter("USER_IP"));
    		  }
    	  }
          /************��¼ҵ����־* end ************/
    	  onRefresh();
    	  setButtonsRevise();

        showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON","UPPSCMCommon-000184")/*@res "����ɹ�"*/);
      } else {
        //���������˼���������
        if(mapAuditInfo.size() > 0){
          for (int i = 0; i < arrivevos.length; i++) {
            headVO = arrivevos[i].getHeadVO();
            if(!mapAuditInfo.containsKey(headVO.getPrimaryKey())){
              headVO.setCauditpsn(null);
              headVO.setDauditdate(null);
              continue;
            }          
            listAuditInfo = (ArrayList<Object>)mapAuditInfo.get(headVO.getPrimaryKey());
            headVO.setCauditpsn((String)listAuditInfo.get(0));
            headVO.setDauditdate((UFDate)listAuditInfo.get(1));
          }
        }
        showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON","UPPSCMCommon-000185")/*@res "����ʧ��"*/);
      }
    } catch (nc.vo.pub.BusinessException e) {
      //���������˼���������
      if(mapAuditInfo.size() > 0){
        for (int i = 0; i < arrivevos.length; i++) {
          headVO = arrivevos[i].getHeadVO();
          if(!mapAuditInfo.containsKey(headVO.getPrimaryKey())){
            headVO.setCauditpsn(null);
            headVO.setDauditdate(null);
            continue;
          }          
          listAuditInfo = (ArrayList<Object>)mapAuditInfo.get(headVO.getPrimaryKey());
          headVO.setCauditpsn((String)listAuditInfo.get(0));
          headVO.setDauditdate((UFDate)listAuditInfo.get(1));
        }
      }
      reportException(e);
      showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON","UPPSCMCommon-000185")/*@res "����ʧ��"*/);
      MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON","UPPSCMCommon-000422")/*@res "ҵ���쳣"*/, e.getMessage());
    } catch (Exception ex) {
      //���������˼���������
      if(mapAuditInfo.size() > 0){
        for (int i = 0; i < arrivevos.length; i++) {
          headVO = arrivevos[i].getHeadVO();
          if(!mapAuditInfo.containsKey(headVO.getPrimaryKey())){
            headVO.setCauditpsn(null);
            headVO.setDauditdate(null);
            continue;
          }          
          listAuditInfo = (ArrayList<Object>)mapAuditInfo.get(headVO.getPrimaryKey());
          headVO.setCauditpsn((String)listAuditInfo.get(0));
          headVO.setDauditdate((UFDate)listAuditInfo.get(1));
        }
      }
      reportException(ex);
      showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON","UPPSCMCommon-000185")/*@res "����ʧ��"*/);
      if (ex instanceof java.rmi.RemoteException){
        MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON","UPPSCMCommon-000422")/*@res "ҵ���쳣"*/, nc.ui.ml.NCLangRes.getInstance().getStrByID("40040302","UPP40040302-000003")/*@res "������������������ʱ����:"*/ + ex.getMessage());
      }
    }
  }
}
/**
 * ��ʾ������ɺ�ĵ��ݡ�
 * <p>
 * <b>examples:</b>
 * <p>
 * ʹ��ʾ��
 * <p>
 * <b>����˵��</b>
 * @param subVOs
 * <p>
 * @author czp
 * @time 2007-3-27 ����01:21:47
 */
private void displayOthersVOs(Vector<ArriveorderVO> subVOs) {
  Vector<ArriveorderVO> allVOs = new Vector<ArriveorderVO>();
  Vector<ArriveorderVO> newVOs = new Vector<ArriveorderVO>();
  ArriveorderVO[] arrvos = null;
  for (int i = 0; i < getCacheVOs().length; i++) {
    allVOs.addElement(getCacheVOs()[i]);
  }
  for (int i = 0; i < allVOs.size(); i++) {
    if (!subVOs.contains(allVOs.elementAt(i))) {
      newVOs.addElement(allVOs.elementAt(i));
    }
  }
  if (newVOs.size() > 0) {
    arrvos = new ArriveorderVO[newVOs.size()];
    newVOs.copyInto(arrvos);
    setCacheVOs(arrvos);
  } else {
    setCacheVOs(null);
  }
  //��ʾ���ݡ�����ť״̬
  loadDataToList();
  //Ĭ����ʾ��һ��
  if (getCacheVOs() != null && getCacheVOs().length > 0) {
    onSelectNo();
    getBillListPanel().getHeadTable().setRowSelectionInterval(0, 0);
    getBillListPanel().getHeadBillModel().setRowState(0, BillModel.SELECTED);
    //���õ�ǰλ��
    setDispIndex(0);
  }
  //ˢ�°�ť�߼�
  setButtonsState();
}
/**
 * ����:���������޸�(���������ת����������Ĵ���)
 */
public void onCancel() {
  if (getStateStr().equals("ת���޸�")) {
    delArriveorderVOSaved();
    if (getCacheVOs() != null) {
      displayArrBillListPanelNew();
      showHintMessage(m_lanResTool.getStrByID("40040301","UPP40040301-000126")/*@res "�������ŵ���,����ת��"*/);
    } else {
      onEndCreate();
    }
    return;
  }
  onCard();
  showHintMessage(m_lanResTool.getStrByID("common","UCH008")/*@res "ȡ���ɹ�"*/);
}

/**
 * @���ܣ��������б���桰�л�����ť�¼�,�л����������������ת���޸ġ�״̬
 * @���ߣ���־ƽ
 * �������ڣ�(2001-6-20 8:10:39)
 * @param:<|>
 * @return:
 * @exception:
 * @see;
 * @since:
 *
 */
private void onCard() { 
  
  showHintMessage(m_lanResTool.getStrByID("40040301","UPP40040301-000128")/*@res "���ڼ�������..."*/);
    
  //��������
  int index = getBillListPanel().getBodyBillModel().getSortColumn();
  boolean bSortAsc = getBillListPanel().getBodyBillModel().isSortAscending();
  
  //����ı�˳���ͬ��
//  int iPos = getBillListPanel().getHeadTable().getSelectedRow();
//  setDispIndex(iPos);
  /*ת���б�*/
  if (getStateStr().equals("ת���б�")) {
    onCardNew();
    if(index >= 0){
      getBillCardPanel().getBillModel().sortByColumn(index,bSortAsc);
    }
    return;
  }
  /*��ת���б�*/
  setM_strState("�������");
  setButtonsState();
  /*
  if (m_arrListPanel != null) {
    remove(getBillListPanel());
  }
  setLayout(new java.awt.BorderLayout());
  add(getBillCardPanel(), java.awt.BorderLayout.CENTER);
  */
  getBillListPanel().setVisible(false);
  getBillCardPanel().setVisible(true);
  getBillCardPanel().setEnabled(false);
  //
  try {
    loadDataToCard();
    setButtonsState();
  } catch (Exception e) {
    SCMEnv.out("���ص���ʱ����");
    showHintMessage(m_lanResTool.getStrByID("40040301","UPP40040301-000129")/*@res "��������ʧ��"*/);
  }
  if(index >= 0){
    getBillCardPanel().getBillModel().sortByColumn(index,bSortAsc);
  }
  updateUI();
  //add by QuSida 2010-9-11 (��ɽ����) --- begin
  //function:��ѯ����صķ�����Ϣ
	String pk =	getBillCardPanel().getHeadItem("carriveorderid").getValue();
	String sql = "cbillid = '"+pk+"' and dr = 0";
	InformationCostVO[] vos = null;
try {
	 vos = (InformationCostVO[])JJPuScmPubHelper.querySmartVOs(InformationCostVO.class, null, sql);
} catch (Exception e) {
	// TODO Auto-generated catch block
	e.printStackTrace();
}
if(vos!=null&&vos.length!=0){
	getBillCardPanel().getBillModel("jj_scm_informationcost").setBodyDataVO(vos);
	getBillCardPanel().getBillModel("jj_scm_informationcost").execLoadFormula();
}else{
		 //20101014-11:51 MeiChao ���������ϢΪ��,�����������Ϣҳǩ��ʷ����.
		getBillCardPanel().getBillModel("jj_scm_informationcost").setBodyDataVO(null); 
	 }
//add by QuSida 2010-9-11 (��ɽ����) --- end
  showHintMessage(m_lanResTool.getStrByID("common","UCH021")/*@res "��Ƭ��ʾ"*/);
}

/**
 * @���ܣ�ת�뵽�����޸�
 */
private void onCardNew() {
  showHintMessage(m_lanResTool.getStrByID("40040301","UPP40040301-000131")/*@res "���ڼ��ص���..."*/);
  isFrmList = true;
  setM_strState("ת���޸�");
  getBillListPanel().setVisible(false);
  getBillCardPanel().setVisible(true);
  getBillCardPanel().setEnabled(true);
  if(!m_bLoadedCard){
    setLayout(new java.awt.BorderLayout());
    add(getBillCardPanel(), java.awt.BorderLayout.CENTER);
    m_bLoadedCard = true;
  }
  setButtonsState();

  //���˴������,��֤��ҵ�����������д���ҵ������ʱ�滻�����������д�������
    //ȡҵ������
    String cBizType = getBillCardPanel().getHeadItem("cbiztype").getValue();

    UFBoolean checker = new UFBoolean(false);
    try {
    loadDataToCard();
    //�˻�����(ͷ��)
    setBackReasonEditable();
    //��Դί����Ʒ�в�����༭
    restrictSCBlargess();
    //�������ص����к�
    BillRowNo.addNewRowNo(getBillCardPanel(), nc.vo.scm.pu.BillTypeConst.PO_ARRIVE, "crowno");

    checker = CheckISSellProxyHelper.CheckIsSellProxyType(cBizType);
        //���˴������
        if(checker.booleanValue()){
          String sql = " and (sellproxyflag = 'Y')";
          UIRefPane refCinventorycode = (UIRefPane) (getBillCardPanel().getBodyItem("cinventorycode").getComponent());
          refCinventorycode.getRefModel().addWherePart(sql);
        }
  } catch (Exception e) {
    SCMEnv.out("���ص���ʱ����");
    showHintMessage(m_lanResTool.getStrByID("40040301","UPP40040301-000132")/*@res "���ص���ʧ��"*/);
  }
  /**���ÿ����֯��ֿ�ƥ��*/
  setOrgWarhouse();
  
  //���ݲ���Ա���òɹ�Ա���ɹ�����
  String strUserId = getClientEnvironment().getUser().getPrimaryKey();
  if(PuPubVO.getString_TrimZeroLenAsNull(getBillCardPanel().getHeadItem("cemployeeid").getValueObject()) == null){
    IUserManageQuery iSrvUser = (IUserManageQuery) NCLocator.getInstance().lookup(IUserManageQuery.class.getName());
    PsndocVO voPsnDoc = null;
    try{
      voPsnDoc = iSrvUser.getPsndocByUserid(getCorpPrimaryKey(), strUserId);
    }catch(BusinessException be){
      SCMEnv.out(be);
    }
    if(voPsnDoc != null){
      UIRefPane refPanePrayPsn = (UIRefPane) getBillCardPanel().getHeadItem("cemployeeid").getComponent();
      refPanePrayPsn.setPK(voPsnDoc.getPrimaryKey());
      //�ɲɹ�Ա�����ɹ�����(����ɹ�������ֵʱ�Ŵ���)
      if(PuPubVO.getString_TrimZeroLenAsNull(getBillCardPanel().getHeadItem("cdeptid").getValueObject()) == null){
        afterEditWhenHeadEmployee(null);
      }
    }
  }
  
  //��ӡ���������޸�
  if (getBillCardPanel().getTailItem("iprintcount") != null)
    getBillCardPanel().getTailItem("iprintcount").setEnabled(false);    
  //�ۼ��������Ӧ���ɱ༭
  getBillCardPanel().getBodyItem("naccumwarehousenum").setEdit(false);  
  updateUI();

  //�����˵��Ҽ�����Ȩ�޿���
//  rightButtonRightControl();
  updateButtons();

  showHintMessage(m_lanResTool.getStrByID("common","4004COMMON000000030")/*@res "�����޸�"*/);
}

/**
 * �رմ��ڵĿͻ��˽ӿڡ����ڱ���������ɴ��ڹر�ǰ�Ĺ�����
 * @return boolean ����ֵΪtrue��ʾ�����ڹرգ�����ֵΪfalse��ʾ�������ڹرա�
 *
 * �������ڣ�(2001-8-8 13:52:37)
 */
public boolean onClosing() {
  if (getStateStr().equals("�����޸�") || getStateStr().equals("ת���޸�")) {
    int iRet = MessageDialog.showYesNoCancelDlg(this, 
        m_lanResTool.getStrByID("SCMCOMMON", "UPPSCMCommon-000270")/* "��ʾ"*/, 
        m_lanResTool.getStrByID("common","UCH001")/*@res "�Ƿ񱣴����޸ĵ����ݣ�"*/);
    //����ɹ�����˳�
    if (iRet == MessageDialog.ID_YES) {
      return onSave();
    }
    //�˳�
    else if(iRet == MessageDialog.ID_NO) {
      return true;
    }
    //ȡ���ر�
    else{
      return false;
    }
  }
  return true;
}

/**
 * ��������:�п���
 */
private void onCopyLine() {
	if (getCacheVOs()[getDispIndex()].isHaveCheckLine()) {
		setPopMenuBtnsEnable(false);
		return;
	}

  if (getBillCardPanel().getBodyPanel().getTable().getSelectedRows() != null
    && getBillCardPanel().getBodyPanel().getTable().getSelectedRows().length > 0) {
    getBillCardPanel().copyLine();
  }
  showHintMessage(m_lanResTool.getStrByID("common","UCH039")/*@res "�����гɹ�"*/);
}

/**
 * ��������:ɾ��
 */
private void onDeleteLine() {
  if (getCacheVOs()[getDispIndex()].isHaveCheckLine()) {
  	setPopMenuBtnsEnable(false);
  	return;
  }

  if (getBillCardPanel().getBodyPanel().getTable().getSelectedRows() == null
    || getBillCardPanel().getBodyPanel().getTable().getSelectedRows().length
      <= 0) {
    showHintMessage(m_lanResTool.getStrByID("40040301","UPP40040301-000136")/*@res "δѡȡ�У�ɾ����δ�ɹ�"*/);
    return;
  }
  int iSelRowCnt =
    getBillCardPanel().getBodyPanel().getTable().getSelectedRows().length;
  if (iSelRowCnt
    == getBillCardPanel().getBodyPanel().getTable().getRowCount()) {
    showHintMessage(m_lanResTool.getStrByID("40040301","UPP40040301-000137")/*@res "���嵥����������һ�в��ܱ��棬ɾ����δ�ɹ���"*/);
    return;
  }
  getBillCardPanel().delLine();
  showHintMessage(m_lanResTool.getStrByID("common","UCH037")/*@res "ɾ�гɹ�"*/);
}

/**
 * @���ܣ����ϵ��� ���̷��� deleteMy() + rewriteOnDiscardMy()
 * �����Ϲ����Ѿ�����Ƭд����ʱ�õ�����
 * @���ߣ���־ƽ
 * �������ڣ�(2001-6-20 10:40:17)
 * @param:<|>
 * @return:
 * @exception:
 * @see;
 * @since:
 *
 */
private void onDiscard() {
    int iRet = 
      MessageDialog.showYesNoDlg(this,m_lanResTool.getStrByID("SCMCOMMON","UPPSCMCommon-000219")/*@res "ȷ��"*/,nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UCH002")/*@res "�Ƿ�ȷ��Ҫɾ����"*/,UIDialog.ID_NO);
    if(iRet != UIDialog.ID_YES){
        return;
    }
  showHintMessage(m_lanResTool.getStrByID("40040301","UPP40040301-000139")/*@res "��������..."*/);
  ArriveorderVO arrivevo = new ArriveorderVO();
  arrivevo = getCacheVOs()[getDispIndex()];
  /*//�����Ƿ���ɾ����У��
  if (!arrivevo.isCanBeModified() || arrivevo.isHaveCheckLine()) {
	  MessageDialog.showWarningDlg(
		        this,
		        m_lanResTool.getStrByID("SCMCOMMON","UPPSCMCommon-000270")@res "��ʾ",
		        CommonConstant.BEGIN_MARK
		          + getDispIndex()
		          + CommonConstant.END_MARK
		          + m_lanResTool.getStrByID("40040301","UPP40040301-000141")@res " �е������Ѿ������������������Ѿ�����,���ŵ��ݲ��ᱻ����");
	  return;
		    
  }*/
  
  //���Ϻ����ʾλ�ô���
  int IndexLast = getCacheVOs().length - 1;
  int IndexCurr = getDispIndex();
  boolean isLast = false;
  if (IndexLast == IndexCurr) {
    isLast = true;
  }
  //����
  try {
    //������Ա
    arrivevo.setCoperatorid(getOperatorId());
    //Ϊ�ж��Ƿ���޸ġ����������˵���
    ((ArriveorderHeaderVO) arrivevo.getParentVO()).setCoperatoridnow(getOperatorId());
    //������Ҫ
    arrivevo.getParentVO().setAttributeValue("cuserid", getOperatorId());
    PfUtilClient.processBatch("DISCARD", ScmConst.PO_Arrive, ClientEnvironment.getInstance().getDate().toString(), new ArriveorderVO[] { arrivevo });
    boolean bIsSucc = PfUtilClient.isSuccess();
    //add by QuSida 2010-9-11 (��ɽ����)  --- begin
    //function:ɾ��������Ϣ
	InformationCostVO[] vos = (InformationCostVO[])getBillCardPanel().getBillModel("jj_scm_information").getBodyValueVOs(InformationCostVO.class.getName());
	if(vos!=null&&vos.length!=0){
		try {
			JJPuScmPubHelper.deleteSmartVOs(vos);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			SCMEnv.out(e);
		}
	}
	//add by QuSida 2010-9-11 (��ɽ����)  --- end
    //ˢ��ǰ�˻���
    if (bIsSucc) {
      delArriveorderVODiscarded();
      if (getCacheVOs() == null || !(getCacheVOs().length > 0)) {
        getBillCardPanel().addNew();
        setButtonsState();
      } else {
        getBillCardPanel().getBillData().clearViewData();
        updateUI();
        if (isLast) {
          setDispIndex(getCacheVOs().length - 1);
        } else {
          setDispIndex(IndexCurr);
        }
        onCard();
      }
    }
  } catch (BusinessException b) {
    MessageDialog.showErrorDlg(this, m_lanResTool.getStrByID("SCMCOMMON","UPPSCMCommon-000059")/*@res "����"*/, b.getMessage());
    showHintMessage(m_lanResTool.getStrByID("SCMCOMMON","UPPSCMCommon-000347")/*@res "����ʧ��"*/);
    return;
  } catch (Exception e) {
    reportException(e);
    if (e.getMessage() != null
      && (e.getMessage().indexOf(m_lanResTool.getStrByID("40040301","UPP40040301-000250")/*@res "����"*/) >= 0 || e.getMessage().indexOf(m_lanResTool.getStrByID("40040301","UPP40040301-000212")/*@res ""*/) >= 0 || e.getMessage().indexOf(m_lanResTool.getStrByID("40040301","UPT40040301-000025")/*@res "�˻�"*/) >= 0)
      || e.getMessage().indexOf(m_lanResTool.getStrByID("40040301","UPP40040301-000207")/*@res "�ջ�"*/) >= 0
      || e.getMessage().indexOf(m_lanResTool.getStrByID("40040301","UPP40040301-000251")/*@res "����"*/) >= 0
      || e.getMessage().indexOf(m_lanResTool.getStrByID("40040301","UPP40040301-000252")/*@res "�ݲ�"*/) >= 0
      || e.getMessage().indexOf(m_lanResTool.getStrByID("40040301","UPP40040301-000253")/*@res "��"*/) >= 0) {
      MessageDialog.showErrorDlg(this, m_lanResTool.getStrByID("SCMCOMMON","UPPSCMCommon-000059")/*@res "����"*/, e.getMessage());
    } else
      MessageDialog.showErrorDlg(this, m_lanResTool.getStrByID("SCMCOMMON","UPPSCMCommon-000059")/*@res "����"*/, m_lanResTool.getStrByID("40040301","UPP40040301-000140")/*@res "���ϵ���ʧ��"*/);
    showHintMessage(m_lanResTool.getStrByID("SCMCOMMON","UPPSCMCommon-000347")/*@res "����ʧ��"*/);
    return;
  }
  /************��¼ҵ����־*************//*
   * �������Ż�������־�����ŵ���̨
	Operlog operlog=new Operlog();
	arrivevo.getOperatelogVO().setOpratorname(nc.ui.pub.ClientEnvironment.getInstance().getUser().getUserName());
	arrivevo.getOperatelogVO().setCompanyname(nc.ui.pub.ClientEnvironment.getInstance().getCorporation().getUnitname());
	arrivevo.getOperatelogVO().setOperatorId(nc.ui.pub.ClientEnvironment.getInstance().getUser().getPrimaryKey());
	operlog.insertBusinessExceptionlog(arrivevo, "ɾ��", "ɾ��", nc.vo.scm.funcs.Businesslog.MSGMESSAGE, nc.vo.scm.pu.BillTypeConst.PO_ARRIVE,
			nc.ui.sm.cmenu.Desktop.getApplet().getParameter("USER_IP"));
	*//************��¼ҵ����־* end ************/
  showHintMessage(m_lanResTool.getStrByID("common","4004COMMON000000068")/*@res "���ϳɹ�"*/);
}

/**
 * @���ܣ������б�
 * @�������������޸���û�б��������;
 * @��������������
   1.���Ͽ������ϵĵ���
   2.�����������ϵĵ����к�
 * @���ߣ���־ƽ
 * �������ڣ�(2001-06-20 10:40:17)
 * �޸����ڣ�(2001-10-29 14:40:17)
 */
private void onDiscardSelected() {

    int iRet = 
      MessageDialog.showYesNoDlg(this,m_lanResTool.getStrByID("SCMCOMMON","UPPSCMCommon-000219")/*@res "ȷ��"*/,nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UCH002")/*@res "�Ƿ�ȷ��Ҫɾ����"*/,UIDialog.ID_NO);
    if(iRet != UIDialog.ID_YES){
        return;
    }
  showHintMessage(m_lanResTool.getStrByID("40040301","UPP40040301-000139")/*@res "��������..."*/);
  Vector v = new Vector();
  String lines = "";
  int i = 0, iRealPos = 0;
  boolean isSortFlag = false;
  if (getBillListPanel().getHeadBillModel().getSortIndex() != null) {
    isSortFlag = true;
  }
  int rowcount = getBillListPanel().getHeadBillModel().getRowCount();
  BillModel bm = getBillListPanel().getHeadBillModel();
  ArriveorderVO arrivevo = null;
  for (i = 0; i < rowcount; i++) {
    if (bm.getRowState(i) == BillModel.SELECTED) {
      iRealPos = i;
      if (isSortFlag) {
        iRealPos = getBillListPanel().getHeadBillModel().getSortIndex()[i];
      }
      //ѡ�еĵ���(������)
      arrivevo = getCacheVOs()[iRealPos];
      //������������������������
      if (!arrivevo.isCanBeDiscarded() || arrivevo.isHaveCheckLine()) {
        if (!lines.trim().equals("")) {
          lines += m_lanResTool.getStrByID("SCMCOMMON","UPPSCMCommon-000000")/*@res "��"*/;
        }
        lines += i + 1;
      } else {
        v.add(arrivevo);
      }
    }
  }
  if (!lines.trim().equals("")) {
    if (lines.length() == 1) {
      MessageDialog.showWarningDlg(
        this,
        m_lanResTool.getStrByID("SCMCOMMON","UPPSCMCommon-000270")/*@res "��ʾ"*/,
        CommonConstant.BEGIN_MARK
          + lines
          + CommonConstant.END_MARK
          + m_lanResTool.getStrByID("40040301","UPP40040301-000141")/*@res " �е������Ѿ������������������Ѿ�����,���ŵ��ݲ��ᱻ����"*/);
    } else {
      MessageDialog.showWarningDlg(
        this,
        m_lanResTool.getStrByID("SCMCOMMON","UPPSCMCommon-000270")/*@res "��ʾ"*/,
        CommonConstant.BEGIN_MARK
          + lines
          + CommonConstant.END_MARK
          + m_lanResTool.getStrByID("40040301","UPP40040301-000142")/*@res " �е������Ѿ������������������Ѿ�����,��Щ���ݲ��ᱻ����"*/);
    }
  }
  ArriveorderVO[] arrivevos = null;
  if (v.size() > 0) {
    arrivevos = new ArriveorderVO[v.size()];
    v.copyInto(arrivevos);
    try {
      //������Ա
      for (int j = 0; j < arrivevos.length; j++) {
        arrivevos[j].setCoperatorid(getOperatorId());
        //Ϊ�ж��Ƿ���޸ġ����������˵���
        ((ArriveorderHeaderVO) arrivevos[j].getParentVO()).setCoperatoridnow(getOperatorId());
        //������Ҫ
        arrivevos[j].getParentVO().setAttributeValue("cuserid", getOperatorId());
      }
      //���ر���
      arrivevos = RcTool.getRefreshedVOs(arrivevos);
      PfUtilClient.processBatch(
        "DISCARD",
        ScmConst.PO_Arrive,
        ClientEnvironment.getInstance().getDate().toString(),
        arrivevos);
      boolean bIsSucc = PfUtilClient.isSuccess();
      //ˢ��ǰ�˻���
      if (bIsSucc) {
        //ȫ������
        if (v.size() == getCacheVOs().length) {
          getBillListPanel().getBodyBillModel().clearBodyData();
          getBillListPanel().getHeadBillModel().clearBodyData();
          setCacheVOs(null);
          updateUI();
        } else {
          //ˢ����ʾ
          delArriveorderVOsDiscarded(v);
          getBillListPanel().getBodyBillModel().clearBodyData();
          getBillListPanel().getHeadBillModel().clearBodyData();
          setDispIndex(0);
          onList();
        }
      }
    } catch (BusinessException b) {
      MessageDialog.showErrorDlg(this, m_lanResTool.getStrByID("SCMCOMMON","UPPSCMCommon-000059")/*@res "����"*/, b.getMessage());
      showHintMessage(m_lanResTool.getStrByID("SCMCOMMON","UPPSCMCommon-000347")/*@res "����ʧ��"*/);
      return;
    } catch (Exception e) {
      reportException(e);
      MessageDialog.showErrorDlg(this, m_lanResTool.getStrByID("SCMCOMMON","UPPSCMCommon-000059")/*@res "����"*/, m_lanResTool.getStrByID("40040301","UPP40040301-000143")/*@res "���ϵ�����ʧ��"*/);
      showHintMessage(m_lanResTool.getStrByID("SCMCOMMON","UPPSCMCommon-000347")/*@res "����ʧ��"*/);
      return;
    }
    /************��¼ҵ����־*************/
    Operlog operlog=new Operlog();
    for (ArriveorderVO arriveorderVO : arrivevos) {
    	arriveorderVO.getOperatelogVO().setOpratorname(nc.ui.pub.ClientEnvironment.getInstance().getUser().getUserName());
    	arriveorderVO.getOperatelogVO().setCompanyname(nc.ui.pub.ClientEnvironment.getInstance().getCorporation().getUnitname());
    	arriveorderVO.getOperatelogVO().setOperatorId(nc.ui.pub.ClientEnvironment.getInstance().getUser().getPrimaryKey());
    	operlog.insertBusinessExceptionlog(arriveorderVO, "ɾ��", "ɾ��", nc.vo.scm.funcs.Businesslog.MSGMESSAGE, nc.vo.scm.pu.BillTypeConst.PO_ARRIVE,
				nc.ui.sm.cmenu.Desktop.getApplet().getParameter("USER_IP"));
    }
    /************��¼ҵ����־* end ************/
    showHintMessage(m_lanResTool.getStrByID("common","UCH006")/*@res "ɾ���ɹ�"*/);
  } else {
    onSelectNo();
    showHintMessage(m_lanResTool.getStrByID("40040301","UPP40040301-000144")/*@res "����ʧ��:��ѡ���ݾ���������������"*/);
  }
  //
  setButtonsList();
  //
  showHintMessage(m_lanResTool.getStrByID("common","4004COMMON000000068")/*@res "���ϳɹ�"*/);
}

/**
 * ���� ���Ĺܹ���
 * ���õ���״̬����������������������б�
 */
private void onDocument() {
  String[] strPks = null;
  String[] strCodes = null;
  if (!(getStateStr().equalsIgnoreCase("�������")
    || getStateStr().equalsIgnoreCase("�����б�")
    || getStateStr().equalsIgnoreCase("��Ϣ����"))) {
    showHintMessage(m_lanResTool.getStrByID("40040301","UPP40040301-000145")/*@res "��ȡ�������ݺ�,�ĵ������ܲ�����"*/);
  }
  HashMap mapBtnPowerVo = new HashMap();
  Integer iBillStatus = null;
  //��Ƭ
  if (getStateStr().equalsIgnoreCase("�������")
      || getStateStr().equalsIgnoreCase("��Ϣ����")) {
    if (getCacheVOs() != null
      && getCacheVOs().length > 0
      && getCacheVOs()[getDispIndex()] != null
      && getCacheVOs()[getDispIndex()].getParentVO() != null) {
      strPks =
        new String[] {
          (String) getCacheVOs()[getDispIndex()].getParentVO().getAttributeValue(
            "carriveorderid")};
      strCodes =
        new String[] {
          (String) getCacheVOs()[getDispIndex()].getParentVO().getAttributeValue(
            "varrordercode")};
      // �����ĵ������ɾ����ť�Ƿ����
      BtnPowerVO pVo = new BtnPowerVO(strCodes[0]);
      iBillStatus = PuPubVO.getInteger_NullAs(getCacheVOs()[getDispIndex()].getParentVO().getAttributeValue("ibillstatus"),new Integer(BillStatus.FREE));
      if (iBillStatus.intValue() == 2 || iBillStatus.intValue() == 3) {
        pVo.setFileDelEnable("false");
      }
      mapBtnPowerVo.put(strCodes[0], pVo);
    }
  }
  //�б�
  if (getStateStr().equalsIgnoreCase("�����б�")) {
    if (getCacheVOs() != null && getCacheVOs().length > 0) {
      ArriveorderHeaderVO[] headers = null;
      headers =
        (ArriveorderHeaderVO[]) getBillListPanel()
          .getHeadBillModel()
          .getBodySelectedVOs(ArriveorderHeaderVO.class.getName());
      if (headers == null || headers.length <= 0)
        return;
      strPks = new String[headers.length];
      strCodes = new String[headers.length];
      BtnPowerVO pVo = null;
      for (int i = 0; i < headers.length; i++) {
        strPks[i] = headers[i].getPrimaryKey();
        strCodes[i] = headers[i].getVarrordercode();
        // �����ĵ������ɾ����ť�Ƿ����
        pVo = new BtnPowerVO(strCodes[i]);
        iBillStatus = PuPubVO.getInteger_NullAs(headers[i]
            .getIbillstatus(), new Integer(0));
        if (iBillStatus.intValue() == 2 || iBillStatus.intValue() == 3) {
          pVo.setFileDelEnable("false");
        }
        mapBtnPowerVo.put(strCodes[i], pVo);
      }
    }
  }
  if (strPks == null || strPks.length <= 0)
    return;
  //�����ĵ�����Ի���
  nc.ui.scm.file.DocumentManager.showDM(this,ScmConst.PO_Arrive ,strPks, mapBtnPowerVo);
  showHintMessage(m_lanResTool.getStrByID("common","4004COMMON000000025")/*@res "�ĵ�����ɹ�"*/);
}

/**
 * @���ܣ�����ת��/ת������
 */
private void onEndCreate() {
  showHintMessage(m_lanResTool.getStrByID("40040301","UPP40040301-000146")/*@res "�����˳�ת��..."*/);
  /*���û���VO[]:����ת���ɹ���Ҫ����*/
  setCacheVOs(m_VOsAll);
  /*����б�����*/
  getBillListPanel().getBillListData().clearCopyData();
  /*������ʾλ��:��������ת��ɹ��ĵ�������ͬ����*/
  int iNewCnt = 0;
  if (getCacheVOs() != null && getCacheVOs().length > m_OldVOsLen) {
    iNewCnt = getCacheVOs().length - m_OldVOsLen;
    setDispIndex(getCacheVOs().length - 1);
  } else {
    setDispIndex(m_OldCardVOPos);
  }
  /*��Ƭ��������*/
  setM_strState("�������");
  /*���л�����Ƭʱ����ͬ�Ĵ���*/
  onCard();
  /*��ʼ��ת���ñ���*/
  m_VOsAll = null;
  m_OldCardVOPos = 0;
  m_OldVOsLen = 0;
  if (iNewCnt > 0) {
    showHintMessage(m_lanResTool.getStrByID("40040301","UPP40040301-000147")/*@res "ת������:���� "*/ + iNewCnt + m_lanResTool.getStrByID("40040301","UPP40040301-000148")/*@res " ���µ���"*/);
  } else {
    showHintMessage(m_lanResTool.getStrByID("40040301","UPP40040301-000149")/*@res "ת������:û���µ�������"*/);
  }
  return;
}
private void setBodyFreeVO(ArriveorderVO[] retVOs){
	nc.ui.scm.pub.FreeVOParse freeVOParse = new nc.ui.scm.pub.FreeVOParse();
	for(ArriveorderVO vo:retVOs){
		freeVOParse.setFreeVO(vo.getChildrenVO(), "vfree0", "vfree", "cbaseid", "cmangid", true);
	}
}
/**
 * @���ܣ��Ӷ������ɶԻ������˻�ʱ�Ĵ���
 */
private void onExitFrmOrd(ArriveorderVO[] retVOs) {
  /*�����ѡ������ȡ�����أ��� retVOs = null,��onButtonClicked()��֤*/
  if (retVOs != null && retVOs.length > 0) {
    //���вɹ��繫˾����µĹ�˾��IDת��
    try{
      RcTool.chgDataForArrvCorp(retVOs,getCorpPrimaryKey());
    }catch(BusinessException e){
      MessageDialog.showHintDlg(this, m_lanResTool.getStrByID("SCMCOMMON","UPPSCMCommon-000270")/*@res "��ʾ"*/, e.getMessage());
      return;
    }
    //
    showHintMessage(m_lanResTool.getStrByID("40040301","UPP40040301-000150")/*@res "�������б��������..."*/);
    /*����ת��ǰ����������Ϣ*/
    m_VOsAll = getCacheVOs();
    if (m_VOsAll != null && m_VOsAll.length > 0) {
      m_OldVOsLen = m_VOsAll.length;
    } else {
      m_OldVOsLen = 0;
      m_VOsAll = null;
    }
    m_OldCardVOPos = getDispIndex();
    setBodyFreeVO(retVOs);
    /*��ǰ�������������������Ϣ*/
    setCacheVOs(retVOs);
    //���ڻ���TS
    m_hTS = new HashMap();
    m_pushSaveVOs = null;
    setDispIndex(0);
    /*��ʾ����*/
    displayArrBillListPanelNew();
    String[] value = new String[]{String.valueOf(getCacheVOs().length)};
    showHintMessage(m_lanResTool.getStrByID("40040301","UPP40040301-000151",null,value)/*@res "�б�����������: ������"+ getCacheVOs().length + " �Ŵ����浥��"*/ );
  }
  this.repaint();
}

/**
 * ��������:��ҳ
 */
private void onFirst() {
  int iRollBack = getDispIndex();
  setDispIndex(0);
  try {
    loadDataToCard();
    setButtonsState();
    showHintMessage(m_lanResTool.getStrByID("common","4004COMMON000000026")/*@res "�ɹ���ʾ��ҳ"*/);
  } catch (Exception e) {
    SCMEnv.out("���ص���ʱ����");
    setDispIndex(iRollBack);
    showHintMessage(m_lanResTool.getStrByID("40040301","UPP40040301-000153")/*@res "��ʾ��һ�ŵ���ʧ��"*/);
  }
}

/**
 * ��������:ĩҳ
 */
private void onLast() {
  int iRollBack = getDispIndex();
  setDispIndex(getCacheVOs().length - 1);
  try {
    loadDataToCard();
    setButtonsState();
    showHintMessage(m_lanResTool.getStrByID("common","4004COMMON000000029")/*@res "�ɹ���ʾĩҳ"*/);
  } catch (Exception e) {
    SCMEnv.out("���ص���ʱ����");
    setDispIndex(iRollBack);
    showHintMessage(m_lanResTool.getStrByID("40040301","UPP40040301-000155")/*@res "��ʾ���һ�ŵ���ʧ��"*/);
  }
}
/**
 * @���ܣ��б�(��������״̬��ά���޸ĺ�ת���޸�)
 * @���ߣ���־ƽ
 * �������ڣ�(2001-5-24 9:19:15)
 * @param:<|>
 * @return:
 * @exception:
 * @see;
 * @since:
 *
 */
private void onList() {
  showHintMessage(m_lanResTool.getStrByID("40040301","UPP40040301-000156")/*@res "�������б��������"*/);
  //���״̬ͼƬ
  //V5 Del : setImageType(this.IMAGE_NULL);
  //��������
  int index = getBillCardPanel().getBillModel().getSortColumn();
  boolean bSortAsc = getBillCardPanel().getBillModel().isSortAscending();
  if (getStateStr().equals("ת���޸�")) {
    displayArrBillListPanelNew();
  } else {
    displayArrBillListPanel();
  }
  if(index >= 0){
    getBillListPanel().getBodyBillModel().sortByColumn(index,bSortAsc);
  }
  updateUI();
  showHintMessage(m_lanResTool.getStrByID("common","UCH022")/*@res "�б���ʾ"*/);
}

/**
 * ����������
 */
private void onLnkQuery() {
  ArriveorderVO vo = getCacheVOs()[getDispIndex()];
  if (vo == null || vo.getParentVO() == null)
    return;
  nc.ui.scm.sourcebill.SourceBillFlowDlg soureDlg =
    new nc.ui.scm.sourcebill.SourceBillFlowDlg(
      this,
      nc.vo.scm.pu.BillTypeConst.PO_ARRIVE,
      ((ArriveorderHeaderVO) vo.getParentVO()).getPrimaryKey(),
      null,
      getClientEnvironment().getUser().getPrimaryKey(),
      ((ArriveorderHeaderVO) vo.getParentVO()).getVarrordercode());
  soureDlg.showModal();
  showHintMessage(m_lanResTool.getStrByID("common","4004COMMON000000019")/*@res "����ɹ�"*/);
}

/**
 * @���ܣ���λ
 */
private void onLocate() {
  showHintMessage(m_lanResTool.getStrByID("40040301","UPP40040301-000157")/*@res "ѡ��λλ��..."*/);
  //ÿ�ε���ʱ���õ���������ʾ����
  //UILabel
  getLocateDlg().setCurrBillCount(getCacheVOs().length);
  getLocateDlg().setCurrBillIndex(getDispIndex() + 1);
  String txt = getLocateDlg().getUILabel_Locate().getText();
  getLocateDlg().getUILabel_Locate().setText(
    txt.substring(0, txt.indexOf("{"))
      + "{1-"
      + (getLocateDlg().getCurrBillCount())
      + "}");
  //UITextField
  getLocateDlg().getUITextField_Locate().setMaxValue(
    getLocateDlg().getCurrBillCount());
  getLocateDlg().getUITextField_Locate().setMinValue(1);
  getLocateDlg().getUITextField_Locate().setText(
    (new Integer(getLocateDlg().getCurrBillIndex())).toString());

  getLocateDlg().showModal();
  showHintMessage(m_lanResTool.getStrByID("40040301","UPP40040301-000131")/*@res "���ڼ��ص���..."*/);
  if (getLocateDlg().isCloseOK()) {
    int iRollBack = getDispIndex();
    int currIndex = getLocateDlg().getLocateIndex();
    setDispIndex(currIndex - 1);
    setButtonsState();
    try {
      loadDataToCard();
    } catch (Exception e) {
      SCMEnv.out("���ص���ʱ����");
      setDispIndex(iRollBack);
      showHintMessage(m_lanResTool.getStrByID("40040301","UPP40040301-000158")/*@res "��λʧ��"*/);
    }

    showHintMessage(m_lanResTool.getStrByID("common","4004COMMON000000035")/*@res "��λ�ɹ�"*/);
    updateUI();
  } else {
    showHintMessage(m_lanResTool.getStrByID("40040301","UPP40040301-000160")/*@res "ȡ����λ"*/);
  }
}

/**
 * ��������:�������˵�
 * �������ڣ�(2001-3-27 11:09:34)
 * @param e java.awt.event.ActionEvent
 */
public void onMenuItemClick(java.awt.event.ActionEvent event) {
  UIMenuItem menuItem = (UIMenuItem) event.getSource();
  if (menuItem.equals(getBillCardPanel().getCopyLineMenuItem())) {
    onCopyLine();
  } else if (menuItem.equals(getBillCardPanel().getDelLineMenuItem())) {
    onDeleteLine();
  } else if (menuItem.equals(getBillCardPanel().getPasteLineMenuItem())) {
    onPasteLine();
  }else if (menuItem.equals(getBillCardPanel().getPasteLineToTailMenuItem())) {
    onPasteLineToTail();
  }
  //�����к�
  else if(menuItem.equals(m_miReSortRowNo)){
    onReSortRowNo();
  }
  else if(menuItem.equals(m_miCardEdit)){
	  onCardEdit();
  }
}
/*
 * �����к�
 */
private void onReSortRowNo(){
	if (getCacheVOs()[getDispIndex()].isHaveCheckLine()) {
		setPopMenuBtnsEnable(false);
		return;
	}

  PuTool.resortRowNo(getBillCardPanel(), ScmConst.PO_Arrive, "crowno");
  showHintMessage(NCLangRes.getInstance().getStrByID("common","SCMCOMMON000000284")/*@res "�����кųɹ�"*/);
}
private void onCardEdit(){
	if (getCacheVOs()[getDispIndex()].isHaveCheckLine()) {
		setPopMenuBtnsEnable(false);
		return;
	}

	//������ע������������������в����ڵġ���Чֵ�����ֶΣ���Ƭ�༭ʱ��������û�¼�����Чֵ��
	//UAP55 (2008.8.22)�����������
	//����취���Ƚ�item����������ΪString��������Ƭ�оͻ����ֵ�������ٸĻ�ԭ��ģ���ж���ģ�����
	BillItem memoItem=getBillCardPanel().getBodyItem("vmemo");
	int orgDataType=BillItem.STRING;
	if(memoItem!=null){
		orgDataType=memoItem.getDataType();
		memoItem.setDataType(BillItem.STRING);
	}
	//�����Ƭ�༭���棬����beforeedit�¼���ʽ�������ã��ж����ι���
	if(getBillCardPanel().getBodyItem("vproducenum").isShow()){
	for(int i=0; i<getBillCardPanel().getRowCount(); i++){		
		if(beforeEditProdNum(new nc.ui.pub.bill.BillEditEvent(getBillCardPanel().getBodyItem("vproducenum")
				,null,null,"vproducenum",i,1))){
			getBillCardPanel().getBodyItem("vproducenum").setEnabled(true);
		}
		else{
			getBillCardPanel().getBodyItem("vproducenum").setEnabled(false);
		}
		}
	}
	//ȥ����Ƭ�༭���������/ɾ�а�ť
	boolean oldRowEditState = getBillCardPanel().getBillModel().getRowEditState();
	getBillCardPanel().getBillModel().setRowEditState(true);
	//������Ƭ�༭
	getBillCardPanel().startRowCardEdit();
	//�Ļ�item��ԭʼ��ģ���ж���ģ���������
	if(memoItem!=null) memoItem.setDataType(orgDataType);
	
	
	getBillCardPanel().getBillModel().setRowEditState(oldRowEditState);
	
}
/**
 * ��Դί��ĵ�������Ʒ�в�����༭
 *
 */
private void restrictSCBlargess(){
if(getBillCardPanel().getBillModel().getItemByKey("blargess")!=null){
	if(isFromSC()){
		getBillCardPanel().getBodyItem("blargess").setEnabled(false);
	}else{
		getBillCardPanel().getBodyItem("blargess").setEnabled(getBillCardPanel().getBodyItem("blargess").isEdit());

	}
}
	
}

/**
 * ��������:����
 */
private void onModify() {

  setM_strState(OrderPubVO.RC_ARRIVEMODIFY_STATE);

  int iCurSelectedRow = getBillCardPanel().getBillTable().getSelectedRow();
  getBillCardPanel().updateValue();
  if (iCurSelectedRow >= 0)
    getBillCardPanel().getBillTable().setRowSelectionInterval(iCurSelectedRow, iCurSelectedRow);
  //add by ������ 2010-10-12 (��ɽ����) begin
  getBillCardPanel().getBodyTabbedPane().setSelectedIndex(0);//���ñ���ҳǩ��ʾΪ��ҳǩ!
  //add by ������ 2010-10-12 (��ɽ����) end
  //��ʼ��ɾ������(��λ����ʱ�õ�)
  v_DeletedItems = new Vector();

  //���Ƿ������ɵ�������Ϊ new UFBoolean(false)
  if (getCacheVOs() != null && getCacheVOs().length > 0) {
    for (int i = 0; i < (getCacheVOs()[getDispIndex()].getChildrenVO().length); i++) {
      ((ArriveorderItemVO) ((ArriveorderVO) getCacheVOs()[getDispIndex()]).getChildrenVO()[i]).setIssplit(new UFBoolean(false));
      getBillCardPanel().getBillModel().setValueAt(new UFBoolean(false), i, "issplit");
    }
  }
  getBillCardPanel().setEnabled(true);
  setAuditInfo();
  //ҵ�����Ͳ����޸�
  if (getBillCardPanel().getHeadItem("cbiztype") != null)
    getBillCardPanel().getHeadItem("cbiztype").setEnabled(false);
//  //���ݺŲ����޸�
//  if (getBillCardPanel().getHeadItem("varrordercode") != null)
//    getBillCardPanel().getHeadItem("varrordercode").setEnabled(false);
  //�Ƿ��˻������޸�
  if (getBillCardPanel().getHeadItem("bisback") != null)
    getBillCardPanel().getHeadItem("bisback").setEnabled(false);
  //��ӡ���������޸�
  if (getBillCardPanel().getTailItem("iprintcount") != null)
    getBillCardPanel().getTailItem("iprintcount").setEnabled(false);  
  //�ۼ��������Ӧ���ɱ༭
  getBillCardPanel().getBodyItem("naccumwarehousenum").setEdit(false);  
  //�˻�����(ͷ��)
  setBackReasonEditable();
  //��Դί����Ʒ���ɱ༭
  restrictSCBlargess();
  //���ݲ���Ա���ö�Ӧ�ɹ�Ա������
  setDefaultValueByUser();
  
  //�����֯���Ʋֿ�
  if (getStateStr().equals(OrderPubVO.RC_ARRIVEMODIFY_STATE) || getStateStr().equals(OrderPubVO.RC_OUTMODIFY_STATE)) {
    setOrgWarhouse();
  }
  setButtonsState();
  updateButtons();

  //���˴������,��֤��ҵ�����������д���ҵ������ʱ�滻�����������д�������
    //ȡҵ������
    String cBizType = getBillCardPanel().getHeadItem("cbiztype").getValue();
    UFBoolean checker = new UFBoolean(false);
    try {
      checker = CheckISSellProxyHelper.CheckIsSellProxyType(cBizType);
        //���˴������
        if(checker.booleanValue()){
          String sql = " and (sellproxyflag = 'Y')";
          UIRefPane refCinventorycode = (UIRefPane) (getBillCardPanel().getBodyItem("cinventorycode").getComponent());
          refCinventorycode.getRefModel().addWherePart(sql);
       }
  } catch (Exception e) {
    SCMEnv.out(e);
  }

  showHintMessage(m_lanResTool.getStrByID("common","4004COMMON000000030")/*@res "�����޸�"*/);
}
/**
 * ���ݲ���Ա���òɹ�Ա���ɹ����š�
 * <p>
 * <b>examples:</b>
 * <p>
 * ʹ��ʾ��
 * <p>
 * <b>����˵��</b>
 * <p>
 * @author czp
 * @time 2007-3-26 ����04:49:35
 */
private void setDefaultValueByUser(){
  //ȡ����Ա��Ӧҵ��Ա�����òɹ�Ա(�ɹ�Ա��ֵʱ������)
  if(PuPubVO.getString_TrimZeroLenAsNull(getBillCardPanel().getHeadItem("cemployeeid").getValueObject()) == null){
    IUserManageQuery iSrvUser = (IUserManageQuery) NCLocator.getInstance().lookup(IUserManageQuery.class.getName());
    PsndocVO voPsnDoc = null;
    try{
      voPsnDoc = iSrvUser.getPsndocByUserid(getBillCardPanel().getCorp(), PoPublicUIClass.getLoginUser());
    }catch(BusinessException be){
      SCMEnv.out(be);
    }
    if(voPsnDoc != null){
      UIRefPane refPanePrayPsn = (UIRefPane) getBillCardPanel().getHeadItem("cemployeeid").getComponent();
      refPanePrayPsn.setPK(voPsnDoc.getPrimaryKey());
      //�ɲɹ�Ա�����ɹ�����(����ɹ�Ա������ֵʱ�Ŵ���)
      if(PuPubVO.getString_TrimZeroLenAsNull(getBillCardPanel().getHeadItem("cdeptid").getValueObject()) == null){
        afterEditWhenHeadEmployee(null);
      }
    }
  }
}
/**
 * ��������:�����˵��Ҽ�����Ȩ�޿���
 */
private void rightButtonRightControl(){
  //  û�з����в���Ȩ��
    if(m_btnLines == null || m_btnLines.getChildCount() == 0){
      getBillCardPanel().getCopyLineMenuItem().setEnabled(false);
      getBillCardPanel().getDelLineMenuItem().setEnabled(false);
      getBillCardPanel().getPasteLineMenuItem().setEnabled(false);
      getBillCardPanel().getPasteLineToTailMenuItem().setEnabled(false);
      m_miReSortRowNo.setEnabled(false);
    }
    //�����в���Ȩ��
    else{
      getBillCardPanel().getCopyLineMenuItem().setEnabled(m_btnCpyLine.isPower() && m_btnCpyLine.isEnabled());
      getBillCardPanel().getDelLineMenuItem().setEnabled(m_btnDelLine.isPower() && m_btnDelLine.isEnabled());
      getBillCardPanel().getPasteLineMenuItem().setEnabled(m_btnPstLine.isPower() && m_btnPstLine.isEnabled());
      //ճ������β��ճ���������߼���ͬ
      getBillCardPanel().getPasteLineToTailMenuItem().setEnabled(m_btnPstLine.isPower() && m_btnPstLine.isEnabled());
      m_miReSortRowNo.setEnabled(m_btnReSortRowNo.isPower() && m_btnReSortRowNo.isEnabled());      
     }
}

/**
 * @���ܣ��б�״̬�µ��޸�
 * @���ߣ���־ƽ
 * �������ڣ�(2001-9-13 20:02:06)
 * @param:<|>
 * @return:
 * @exception:
 * @see;
 * @since:
 *
 */
private void onModifyList() {
  showHintMessage(m_lanResTool.getStrByID("40040301","UPP40040301-000161")/*@res "�����л�����Ƭ..."*/);
  isFrmList = true;
  onCard();
  onModify();
  showHintMessage(m_lanResTool.getStrByID("common","4004COMMON000000030")/*@res "�����޸�"*/);
}
/**
 * ��������:��ҳ
 */
private void onNext() {
  int iRollBack = getDispIndex();
  setDispIndex(getDispIndex() + 1);
  try {
    loadDataToCard();
    setButtonsState();
    showHintMessage(m_lanResTool.getStrByID("common","4004COMMON000000028")/*@res "�ɹ���ʾ��һҳ"*/);
  } catch (Exception e) {
    SCMEnv.out("���ص���ʱ����");
    setDispIndex(iRollBack);
    showHintMessage(m_lanResTool.getStrByID("40040301","UPP40040301-000163")/*@res "��ʾ��һ�ŵ���ʧ��"*/);
  }
}

/**
 * ��������:ճ����
 */
private void onPasteLine() {
  int iOldRowCnt = getBillCardPanel().getBillModel().getRowCount();
  if (getCacheVOs()[getDispIndex()].isHaveCheckLine()) {
	  setPopMenuBtnsEnable(false);
	  return;
  }

  try {
    getBillCardPanel().pasteLine();
  } catch (Exception e) {
    SCMEnv.out("ճ����ʱ����" + e.getMessage());
  }
  int iNewRowCnt = getBillCardPanel().getBillModel().getRowCount();
  if (iOldRowCnt > 0 && iNewRowCnt > 0 && iOldRowCnt == iNewRowCnt) {
    showHintMessage(m_lanResTool.getStrByID("40040301","UPP40040301-000164")/*@res "ճ��δ�ɹ�,����ԭ��û�п������ݻ�δȷ��Ҫճ����λ��"*/);
  } else if (iOldRowCnt <= 0 || iNewRowCnt <= 0) {
    showHintMessage("");
  } else {
    showHintMessage(m_lanResTool.getStrByID("common","UCH040")/*@res "ճ���гɹ�"*/);
    //�����к�
    BillRowNo.pasteLineRowNo(getBillCardPanel(),nc.vo.scm.pu.BillTypeConst.PO_ARRIVE,"crowno",iNewRowCnt - iOldRowCnt);
//    //
//    int iEndRow = getBillCardPanel().getBillTable().getSelectedRow() - 1;
//    int iBeginRow = iEndRow - (iNewRowCnt-iOldRowCnt) + 1;
//    for(int i=iBeginRow; i<=iEndRow;i++){
//      getBillCardPanel().getBillModel().setRowState(i, BillModel.ADD);
//    }
  }
}

/**
 * ��������:ճ���е���β
 */
private void onPasteLineToTail() {
  int iOldRowCnt = getBillCardPanel().getBillModel().getRowCount();
  if (getCacheVOs()[getDispIndex()].isHaveCheckLine()) {
	  setPopMenuBtnsEnable(false);
	  return;
  }

  try {
    getBillCardPanel().pasteLineToTail();
  } catch (Exception e) {
    SCMEnv.out("ճ���е���βʱ����" + e.getMessage());
  }
  int iNewRowCnt = getBillCardPanel().getBillModel().getRowCount();
  if (iOldRowCnt > 0 && iNewRowCnt > 0 && iOldRowCnt == iNewRowCnt) {
    showHintMessage(m_lanResTool.getStrByID("SCMCOMMON","UPPSCMCommon-000424")/*@res "ճ���е���βδ�ɹ�,����ԭ��û�п������ݻ�δȷ��Ҫճ����λ��"*/);
  } else if (iOldRowCnt <= 0 || iNewRowCnt <= 0) {
    showHintMessage("");
  } else {
    showHintMessage(m_lanResTool.getStrByID("common","UCH040")/*@res "ճ���гɹ�"*/);
    //�����к�
    BillRowNo.addLineRowNos(getBillCardPanel(),nc.vo.scm.pu.BillTypeConst.PO_ARRIVE,"crowno",iNewRowCnt - iOldRowCnt);
    //
//    int iEndRow = getBillCardPanel().getRowCount() - 1;
//    int iBeginRow = iEndRow - (iNewRowCnt-iOldRowCnt) + 1;
//    for(int i=iBeginRow; i<=iEndRow;i++){
//      getBillCardPanel().getBillModel().setRowState(i, BillModel.ADD);
//    }
  }
}

/**
 * ��������:��ҳ
 */
private void onPrevious() {
  int iRollBack = getDispIndex();
  setDispIndex(getDispIndex() - 1);
  try {
    loadDataToCard();
    setButtonsState();
    showHintMessage(m_lanResTool.getStrByID("common","4004COMMON000000027")/*@res "�ɹ���ʾ��һҳ"*/);
  } catch (Exception e) {
    SCMEnv.out("���ص���ʱ����");
    setDispIndex(iRollBack);
    showHintMessage(m_lanResTool.getStrByID("40040301","UPP40040301-000167")/*@res "��ʾ��һ�ŵ���ʧ��"*/);
  }
}

/**
 * @���ܣ�������ѯ
 * @���ߣ���־ƽ
 * @������(2001-7-18 12:41:25)
 */
private void onQuery() {
  /**/
  m_hTS = null;
  int iRetType = getQueryConditionDlg().showModal();
  
  
  if (iRetType == UIDialog.ID_OK) {
    m_bQueriedFlag = true;
    showHintMessage(m_lanResTool.getStrByID("40040301","UPP40040301-000168")/*@res "���ڲ�ѯ����..."*/);
    getArriveVOsFromDB();
    setDispIndex(0);
    if (getStateStr().equals("�����б�")) {
      onList();
    } else {
      isFrmList = false;
      onCard();
    }
    if (getCacheVOs() == null || getCacheVOs().length <= 0) {
      showHintMessage(m_lanResTool.getStrByID("40040301","UPP40040301-000169")/*@res "��ѯ���:û�з��������ĵ���"*/);
    } else {
      String[] value = new String[]{String.valueOf(getCacheVOs().length)};
      showHintMessage(m_lanResTool.getStrByID("40040301","UPP40040301-000170",null,value)/*@res "��ѯ���:��ѯ��"+ getCacheVOs().length + "�ŵ���"*/ );
    }
    
    if (m_dlgSerialAllocation != null) {
    	setM_alSerialData(null,null);
    }
    setButtonsRevise();
    showHintMessage(m_lanResTool.getStrByID("common","UCH009")/*@res "��ѯ���"*/);
  }
}

/**
 * ���ߣ���ά��
 * ���ܣ��˴����뷽��˵��
 * ������
 * ���أ�
 * ���⣺
 * ���ڣ�(2004-3-8 10:35:43)
 */
private void onQueryBOM() {
  String sState = getStateStr();
  String sCmangId = null;
  int iPos;
  ArriveorderItemVO itemVO = null;
  if (sState != null && (sState.equals("ת���б�") || sState.equals("�����б�"))) {
      if(getBillListPanel().getBodyTable().getRowCount() == 0)
        return;
      iPos = getBillListPanel().getBodyTable().getSelectedRow();
    if (iPos == -1) {
      MessageDialog.showHintDlg(this, m_lanResTool.getStrByID("SCMCOMMON","UPPSCMCommon-000270")/*@res "��ʾ"*/, m_lanResTool.getStrByID("SCMCOMMON","UPPSCMCommon-000364")/*@res "��ѡ���д������!"*/);
      return;
    }
    itemVO = (ArriveorderItemVO)getBillListPanel().getBodyBillModel().getBodyValueRowVO(iPos,ArriveorderItemVO.class.getName());
    sCmangId = itemVO.getCmangid();
  } else {
    if (getBillCardPanel().getRowCount() == 0) {
      return;
    }
    iPos = getBillCardPanel().getBillTable().getSelectedRow();
    if (iPos == -1) {
      MessageDialog.showHintDlg(this, m_lanResTool.getStrByID("SCMCOMMON","UPPSCMCommon-000270")/*@res "��ʾ"*/, m_lanResTool.getStrByID("SCMCOMMON","UPPSCMCommon-000364")/*@res "��ѡ���д������!"*/);
      return;
    }
    itemVO = (ArriveorderItemVO) getCacheVOs()[getDispIndex()].getChildrenVO()[iPos];
    sCmangId = itemVO.getCmangid();
  }
  if (PuPubVO.getString_TrimZeroLenAsNull(sCmangId) == null) {
    MessageDialog.showHintDlg(this, m_lanResTool.getStrByID("SCMCOMMON","UPPSCMCommon-000270")/*@res "��ʾ"*/, m_lanResTool.getStrByID("SCMCOMMON","UPPSCMCommon-000364")/*@res "��ѡ���д������!"*/);
    return;
  }
  SetPartDlg dlg = new SetPartDlg(this);
  dlg.setParam(getCorpPrimaryKey(), sCmangId);
  dlg.showSetpartDlg();
  showHintMessage(m_lanResTool.getStrByID("common","UCH009")/*@res "��ѯ���"*/);
}

/**
 * ���ܣ�������ѯ
 * ������(2002-10-31 19:45:39)
 * �޸ģ�2003-04-21/czp/ͳһ�����۶Ի���
 * ����״̬:��ʼ������������������޸ģ������б�ת���б�ת���޸�
 */
private void onQueryInvOnHand() {
  ArriveorderVO voPara = null;
  ArriveorderItemVO item = null;
  ArriveorderItemVO[] items = null;
  /*��Ƭ*/
  if (getStateStr().equals("�������") || getStateStr().equals("�����޸�") || getStateStr().equals("ת���޸�")) {
    voPara = (ArriveorderVO) getBillCardPanel().getBillValueVO(ArriveorderVO.class.getName(), ArriveorderHeaderVO.class.getName(), ArriveorderItemVO.class.getName());
    if (voPara == null) {
      showHintMessage(m_lanResTool.getStrByID("40040301","UPP40040301-000171")/*@res "δѡȡ����,���ܲ�ѯ����"*/);
      return;
    }
    /*������Ϣ�����Լ��*/
    int[] iSelRows = getBillCardPanel().getBillTable().getSelectedRows();
    if (iSelRows != null && iSelRows.length > 0) {
      /*�õ��û�ѡȡ�ĵ�һ��*/
      item = (ArriveorderItemVO) getBillCardPanel().getBillModel().getBodyValueRowVO(iSelRows[0], ArriveorderItemVO.class.getName());
    } else {
      /*�û�δѡ��ʱ��ȡ���ݵ�һ��*/
      items = (ArriveorderItemVO[]) getBillCardPanel().getBillModel().getBodyValueVOs(ArriveorderItemVO.class.getName());
      if (items == null || items.length <= 0) {
        showHintMessage(m_lanResTool.getStrByID("40040301","UPP40040301-000172")/*@res "��˾�����������������Ϣ������,���ܲ�ѯ����"*/);
        return;
      }
      item = items[0];
    }
    /*�ƻ�ִ������=��������*/
    item.setArrvdate((UFDate) voPara.getParentVO().getAttributeValue("dreceivedate"));
    /*��Ϣ�����Լ��*/
    if (voPara.getParentVO().getAttributeValue("pk_corp") == null
      || voPara.getParentVO().getAttributeValue("pk_corp").toString().trim().equals("")
      || item.getAttributeValue("cinventoryid") == null
      || item.getAttributeValue("cinventoryid").toString().trim().equals("")
      || item.getAttributeValue("arrvdate") == null
      || item.getAttributeValue("arrvdate").toString().trim().equals("")) {
      showHintMessage(m_lanResTool.getStrByID("40040301","UPP40040301-000172")/*@res "��˾�����������������Ϣ������,���ܲ�ѯ����"*/);
      return;
    }
    /*�����VO��ʼ�������ô�����ѯ�Ի���*/
    voPara.setChildrenVO(new ArriveorderItemVO[] { item });
    if (saPkCorp == null) {
      try {
        IUserManageQuery myService = (IUserManageQuery)nc.bs.framework.common.NCLocator.getInstance().lookup(IUserManageQuery.class.getName());
        nc.vo.bd.CorpVO[] vos =
          myService.queryAllCorpsByUserPK(getClientEnvironment().getUser().getPrimaryKey());
        if (vos == null || vos.length == 0){
                  SCMEnv.out("δ��ѯ����Ȩ�޹�˾��ֱ�ӷ���!");
                  return;
        }
        final int iLen = vos.length;
        saPkCorp = new String[iLen];
        for (int i = 0; i < iLen; i++){
          saPkCorp[i] = vos[i].getPrimaryKey();
        }
      } catch (Exception e) {
        MessageDialog.showErrorDlg(this, m_lanResTool.getStrByID("40040301","UPP40040301-000173")/*@res "��ȡ��Ȩ�޹�˾�쳣"*/, m_lanResTool.getStrByID("40040301","UPP40040301-000174")/*@res "��ȡ��Ȩ�޹�˾ʱ�����쳣(��ϸ��Ϣ�μ�����̨��־)!"*/);
        SCMEnv.out(e);
        return;
      }
    }
    getAtpDlg().setPkCorps(saPkCorp);
    getAtpDlg().initData(voPara);
    getAtpDlg().showModal();
  }
  /*�б�*/
  else if (getStateStr().equals("�����б�") || getStateStr().equals("ת���б�")) {
    /*��ͷ��Ϣ�����Լ��*/
    ArriveorderHeaderVO head = null;
    if (getBillListPanel().getHeadBillModel().getBodySelectedVOs(ArriveorderHeaderVO.class.getName()) == null
      || getBillListPanel().getHeadBillModel().getBodySelectedVOs(ArriveorderHeaderVO.class.getName()).length <= 0) {
      showHintMessage(m_lanResTool.getStrByID("40040301","UPP40040301-000171")/*@res "δѡȡ����,���ܲ�ѯ����"*/);
      return;
    }
    head = (ArriveorderHeaderVO) getBillListPanel().getHeadBillModel().getBodySelectedVOs(ArriveorderHeaderVO.class.getName())[0];
    if (head == null || head.getAttributeValue("pk_corp") == null || head.getAttributeValue("pk_corp").toString().trim().equals("")) {
      showHintMessage(m_lanResTool.getStrByID("40040301","UPP40040301-000175")/*@res "δ��ȷ��˾,���ܲ�ѯ����"*/);
      return;
    }
    /*������Ϣ�����Լ��*/
    int[] iSelRows = getBillListPanel().getBodyTable().getSelectedRows();
    if (iSelRows != null && iSelRows.length > 0) {
      /*�õ��û�ѡȡ�ĵ�һ��*/
      item = (ArriveorderItemVO) getBillListPanel().getBodyBillModel().getBodyValueRowVO(iSelRows[0], ArriveorderItemVO.class.getName());
    } else {
      /*�û�δѡ��ʱ��ȡ���ݵ�һ��*/
      items = (ArriveorderItemVO[]) getBillListPanel().getBodyBillModel().getBodyValueVOs(ArriveorderItemVO.class.getName());
      if (items == null || items.length <= 0) {
        showHintMessage(m_lanResTool.getStrByID("40040301","UPP40040301-000172")/*@res "��˾�����������������Ϣ������,���ܲ�ѯ����"*/);
        return;
      }
      item = items[0];
    }
    /*�ƻ�ִ������=��������*/
    item.setArrvdate(head.getDreceivedate());
    /*��Ϣ�����Լ��*/
    if (item.getAttributeValue("cinventoryid") == null
      || item.getAttributeValue("cinventoryid").toString().trim().equals("")
      || item.getAttributeValue("arrvdate") == null
      || item.getAttributeValue("arrvdate").toString().trim().equals("")) {
      showHintMessage(m_lanResTool.getStrByID("40040301","UPP40040301-000172")/*@res "��˾�����������������Ϣ������,���ܲ�ѯ����"*/);
      return;
    }
    /*�����VO��ʼ�������ô�����ѯ�Ի���*/
    voPara = new ArriveorderVO();
    voPara.setParentVO(head);
    voPara.setChildrenVO(new ArriveorderItemVO[] { item });
    if (saPkCorp == null) {
      try {
        IUserManageQuery myService = (IUserManageQuery)nc.bs.framework.common.NCLocator.getInstance().lookup(IUserManageQuery.class.getName());
        nc.vo.bd.CorpVO[] vos =
          myService.queryAllCorpsByUserPK(getClientEnvironment().getUser().getPrimaryKey());
        if (vos == null || vos.length == 0){
                  SCMEnv.out("δ��ѯ����Ȩ�޹�˾��ֱ�ӷ���!");
                  return;
        }
        final int iLen = vos.length;
        saPkCorp = new String[iLen];
        for (int i = 0; i < iLen; i++){
          saPkCorp[i] = vos[i].getPrimaryKey();
        }
      } catch (Exception e) {
        MessageDialog.showErrorDlg(this, m_lanResTool.getStrByID("40040301","UPP40040301-000173")/*@res "��ȡ��Ȩ�޹�˾�쳣"*/, m_lanResTool.getStrByID("40040301","UPP40040301-000174")/*@res "��ȡ��Ȩ�޹�˾ʱ�����쳣(��ϸ��Ϣ�μ�����̨��־)!"*/);
        SCMEnv.out(e);
        return;
      }
    }
    getAtpDlg().setPkCorps(saPkCorp);
    getAtpDlg().initData(voPara);
    getAtpDlg().showModal();
  }
  showHintMessage(m_lanResTool.getStrByID("common","4004COMMON000000032")/*@res "������ѯ���"*/);
}

/**
 * ���ߣ���ά��
 * ���ܣ������ջ�
 * ������
 * ���أ�
 * ���⣺
 * ���ڣ�(2004-3-15 10:20:43)
 */
private void onQuickArr() {
  int iRetType = UIDialog.ID_OK;
  do{
    getQuickArrDlg().setCheckBoxSel(false);
    getQuickArrDlg().setBillCodeValue("");
    getQuickArrDlg().showModal();
    iRetType = getQuickArrDlg().getResult();
    if (iRetType == UIDialog.ID_OK){
      String sBillCode = getQuickArrDlg().getBillCodeValue();
      if (sBillCode == null || sBillCode.trim().length() == 0) {
        showWarningMessage(m_lanResTool.getStrByID("40040301","UPP40040301-000176")/*@res "���ݺ�Ϊ�գ������뵥�ݺ�!"*/);
      }else iRetType = UIDialog.ID_NO;
    }else return;
  }while(iRetType == UIDialog.ID_OK);
  
  //V35BUG:
  if(isQuickException()){
    setQuickExceptionFlag(false);
    return;
  }

  try {
    ArriveorderVO[] retVOs = (ArriveorderVO[]) getQuickArrDlg().getRetVos();
    if (retVOs == null || retVOs.length == 0 || retVOs[0] == null) {
      if(getQuickArrDlg().getErrorMsg() != null){
        showWarningMessage(getQuickArrDlg().getErrorMsg());
      }else{
        showWarningMessage(m_lanResTool.getStrByID("40040301","UPP40040301-000177")/*@res "û�з��������ĵ��ݣ�"*/);
      }
      return;
    }
    //�����Ƿ񱣴�ǰ���
    if (getQuickArrDlg().isLookBefSave()) {
      onExitFrmOrd(retVOs);
      //��ʽ���浽����
    } else {
      //since v502 , ���벿�ַǿ���Ŀ���
      verifyNotNullFields(retVOs, getQuickArrDlg().getBillCodeValue());
      //
      m_hTS = new HashMap();
      m_pushSaveVOs = retVOs;
      for (int i = 0; i < retVOs.length; i++) {
        ArriveorderVO saveVO = retVOs[i];

        //���̱��淽���Ĳ���

        //aryPara0 : 0����˾������1���޸�ǰ�ĵ�������2����ǰ���ݱ༭״̬��3.�޸ĺ��VO������VO��
        ArrayList aryPara = new ArrayList(2);
        ArrayList aryPara0 = new ArrayList();

        aryPara0.add(getCorpPrimaryKey());
        aryPara0.add(saveVO);
        aryPara0.add("insert");
        aryPara0.add(saveVO);

        aryPara.add(aryPara0);
        aryPara.add(null);
        aryPara.add(new Integer(0));
        aryPara.add(new String("cvendormangid"));

        //���ñ�ͷ����״̬(����)
         ((ArriveorderHeaderVO) saveVO.getParentVO()).setStatus(VOStatus.NEW);
        for (int j = 0; j < saveVO.getChildrenVO().length; j++) {
          ((ArriveorderItemVO[]) saveVO.getChildrenVO())[j].setStatus(VOStatus.NEW);
        }
        saveVO.setOprType(VOStatus.NEW);
        //������Ա
        saveVO.setCoperatorid(getOperatorId());
        //�жϲ����Ĺ��÷��� PubDMO.checkVoNoChanged() ��Ҫ
        saveVO.getParentVO().setAttributeValue("cuserid", getOperatorId());

        ArrayList aryRet = (ArrayList) PfUtilClient.processAction("SAVEBASE", ScmConst.PO_Arrive, ClientEnvironment.getInstance().getDate().toString(), saveVO, aryPara);

        // ��������ˢ�»���VO��TS
        refreshVOTs((ArriveorderVO) aryRet.get(1));
        String strRetKey = (aryRet == null) ? null : (String) aryRet.get(0);
        boolean bIsSucc = PfUtilClient.isSuccess();
        //�������
        if (strRetKey != null && bIsSucc) {
          /*ˢ�±���ɹ�����*/
          /*�ű� N_23_SAVEBASE ���趨��VO*/
          ArriveorderVO voTmp = (ArriveorderVO) aryRet.get(1);
          if (voTmp == null)
            throw new BusinessException(m_lanResTool.getStrByID("40040301","UPP40040301-000178")/*@res "�������ݳɹ�����ˢ������ʱ�������Ժ����ԣ�"*/);
          /*���ӱ���ɹ����ݵ�����ʾ���ݻ���ĩβ*/
          appArriveorderVOSaved(voTmp);
        }
      }
      onEndCreate();
      m_pushSaveVOs = null;
      showHintMessage(m_lanResTool.getStrByID("40040301","UPP40040301-000179")/*@res "����ɹ�,ת������"*/);
    }
    showHintMessage(m_lanResTool.getStrByID("common","4004COMMON000000036")/*@res "�����ջ����"*/);
  } catch (Exception e) {
    reportException(e);
    showHintMessage(e.getMessage());
  }

}
/**
 * ����ͷ�������Ƿ�ǿգ������֯�ȡ�
 * <p>
 * <b>examples:</b>
 * <p>
 * ʹ��ʾ��
 * <p>
 * <b>����˵��</b>
 * @param voaArr ������
 * <p>
 * @author czp
 * @time 2007-10-16 ����10:24:33
 */
private void verifyNotNullFields(ArriveorderVO[] voaArr, String strOrderBillCode) throws Exception{  
  ArrayList<String> listOrderBid = new ArrayList<String>();
  ArriveorderHeaderVO head = null;
  ArriveorderItemVO[] items = null;
  for(ArriveorderVO voArrCurr : voaArr){
    head = voArrCurr.getHeadVO();
    if(PuPubVO.getString_TrimZeroLenAsNull(head.getCstoreorganization()) == null){
      items = voArrCurr.getBodyVo();
      for(ArriveorderItemVO item : items){
        listOrderBid.add(item.getCorder_bid());
      }
    }
  }
  if(listOrderBid.size() > 0){
    Object[][] oaRet = PubHelper.queryArrayValue("po_order_b", 
        "corder_bid", new String[]{"crowno"}, 
        listOrderBid.toArray(new String[listOrderBid.size()]));
    String strErrRowNo = "";
    for(int i=0; i<oaRet.length; i++){
      strErrRowNo += oaRet[i][0];
      strErrRowNo += ",";
    }
    strErrRowNo = strErrRowNo.substring(0,strErrRowNo.length()-1);
    //
    throw new Exception(NCLangRes.getInstance().getStrByID(
        "40040301", "UPP40040301-000288", null, new String[]{strOrderBillCode, strErrRowNo})
        /*"�����ջ�ʧ�ܡ�ԭ�򣬶�����Ϊ[{0}]�Ķ������ж����к��ջ������֯Ϊ�գ�{1}"*/);
  }
  
  
  BillItem[] headItems = getBillCardPanel().getHeadShowItems();

  //��÷ǿյ���ͷItem��Key
  String[] headNotNullKeys = null;
  Vector v1 = new Vector();
  for (int i = 0; headItems != null && i < headItems.length; i++) {
    if (headItems[i].isNull()) {
      v1.add(headItems[i].getKey());
    }
  }
  if (v1.size() > 0) {
    headNotNullKeys = new String[v1.size()];
    v1.copyInto(headNotNullKeys);
  }

  //��֤����ͷ�ǿ����ֵ�Ƿ�Ϊ��
  String hErrorMessage = "";
  String value = null;
  for (int i = 0; headNotNullKeys != null && i < headNotNullKeys.length; i++) {
    for(ArriveorderVO voArrCurr : voaArr){
      value = PuPubVO.getString_TrimZeroLenAsNull(voArrCurr.getHeadVO().getAttributeValue(headNotNullKeys[i]));
      String name = getBillCardPanel().getHeadItem(headNotNullKeys[i]).getName();
      if (value == null || value.trim().equals("")) {
        hErrorMessage += "[" + name + "],";
      }
    }
  }
  if (!hErrorMessage.equals(""))
    hErrorMessage = hErrorMessage.substring(0, hErrorMessage.length() - 1);


  //�����ʾ��Ϣ��Ϊ�����׳��쳣.
  if (!hErrorMessage.equals("")) {
    String errorMessage = "�����ջ�ʧ��:"+ nc.ui.ml.NCLangRes.getInstance().getStrByID("scmpub","UPPscmpub-000444")/*@res "�������Ե�ֵ����Ϊ��:"*/;
    if (!hErrorMessage.equals(""))
      errorMessage += "\n" + nc.ui.ml.NCLangRes.getInstance().getStrByID("scmpub","UPPscmpub-000445")/*@res "\n��ͷ:"*/ + hErrorMessage;

    throw new BusinessException(errorMessage);
  }
}
/**
 * @���ܣ�ˢ�����ݣ���Ƭ�б���棩
 * @���ߣ���־ƽ
 * �������ڣ�(2001-6-20 13:35:04)
 * @param:<|>
 * @return:
 * @exception:
 * @see;
 * @since:
 *
 */
private void onRefresh() {
  showHintMessage(m_lanResTool.getStrByID("40040301","UPP40040301-000168")/*@res "���ڲ�ѯ����..."*/);
  //��������
  getArriveVOsFromDB();
  /*��ʼ���������λ��(���ǵ�ɾ������)*/
  setDispIndex(0);
  //��ʾ���ݡ�����ť״̬
  if (getStateStr().equals("�������") || getStateStr().equals("��ʼ��")) {
    try {
      loadDataToCard();
    } catch (Exception e) {
      SCMEnv.out("���ص���ʱ����");
    }
  } else if (getStateStr().equals("�����б�")) {
    loadDataToList();
    //Ĭ����ʾ��һ��
    if (getCacheVOs() != null && getCacheVOs().length > 0) {
      getBillListPanel().getHeadTable().setRowSelectionInterval(
        getDispIndex(),
        getDispIndex());
      getBillListPanel().getHeadBillModel().setRowState(
        getDispIndex(),
        BillModel.SELECTED);
      setListBodyData(getDispIndex());
    }
  }
  //
  setButtonsState();
  //
  if (getCacheVOs() == null || getCacheVOs().length <= 0) {
    showHintMessage(m_lanResTool.getStrByID("40040301","UPP40040301-000169")/*@res "��ѯ���:û�з��������ĵ���"*/);
  } else {
    showHintMessage(m_lanResTool.getStrByID("40040301","UPP40040301-000170")/*@res "��ѯ���:��ѯ��"*/ + getCacheVOs().length + m_lanResTool.getStrByID("40040301","UPP40040301-000180")/*@res "�ŵ���"*/);
  }

  if (m_dlgSerialAllocation != null) {
	  	setM_alSerialData(null,null);
	  }

  showHintMessage(m_lanResTool.getStrByID("common","UCH007")/*@res "ˢ�³ɹ�"*/);
}

/**
 * ����˴ε������������۾��붩��ƥ�䣬����ö������ҽ��(������ί��)��
 * <p>
 * <b>examples:</b>
 * <p>
 * ʹ��ʾ��
 * <p>
 * <b>����˵��</b>
 * <p>
 * @author czp
 * @time 2007-5-11 ����02:28:42
 */
private void dealNmoneyBeforeSave(){
  ArrayList<String> listOrderBid = new ArrayList<String>();
  ArrayList<UFDouble> listNum = new ArrayList<UFDouble>();
  ArrayList<UFDouble> listPrice = new ArrayList<UFDouble>();
  int iLen = getBillCardPanel().getRowCount();  
  String strOrderBid = null;
  UFDouble ufdNum = null;
  UFDouble ufdPrice = null;
  BillModel bm = getBillCardPanel().getBillModel();
  for(int i=0; i<iLen; i++){
    strOrderBid = PuPubVO.getString_TrimZeroLenAsNull(bm.getValueAt(i, "corder_bid"));
    if(strOrderBid == null){
      continue;
    }
    ufdNum = PuPubVO.getUFDouble_NullAsZero(bm.getValueAt(i, "narrvnum"));
    if(ufdNum == null){
      continue;
    }
    ufdPrice = PuPubVO.getUFDouble_NullAsZero(bm.getValueAt(i, "nprice"));
    if(ufdPrice == null){
      continue;
    }
    //һ�ж���ֻ�践��һ������
    if(listOrderBid.contains(strOrderBid)){
      continue;
    }
    listOrderBid.add(strOrderBid);
    listNum.add(ufdNum);
    listPrice.add(ufdPrice);
  }
  if(listOrderBid.size() == 0){
    return;
  }
  int iSize = listOrderBid.size(); 
  String[] saBid = (String[]) listOrderBid.toArray(new String[iSize]);
  UFDouble[] uaNum = (UFDouble[]) listNum.toArray(new UFDouble[iSize]);
  UFDouble[] uaNprice = (UFDouble[]) listPrice.toArray(new UFDouble[iSize]);
  IQueryForIc srvQueryPrice = (IQueryForIc) NCLocator.getInstance().lookup(IQueryForIc.class.getName());
  UFDouble[] uaOrderNmoney = null;
  try{
    uaOrderNmoney = srvQueryPrice.queryForIcPrice(getCorpPrimaryKey(), saBid, uaNum, uaNprice);
  }catch(BusinessException e){
    SCMEnv.out(e.getMessage());
  }
  if(uaOrderNmoney == null){
    return;
  }
  //��ϣ����
  HashMap<String, UFDouble> mapOrderNmoney = new HashMap<String, UFDouble>();
  for(int i=0; i<iSize; i++){
    if(uaOrderNmoney[i] == null){
      continue;
    }
    mapOrderNmoney.put(saBid[i], uaOrderNmoney[i]);
  }
  //��д������ģ��
  for(int i=0; i<iLen; i++){
    strOrderBid = PuPubVO.getString_TrimZeroLenAsNull(bm.getValueAt(i, "corder_bid"));
    if(strOrderBid == null){
      continue;
    }
    ufdNum = PuPubVO.getUFDouble_NullAsZero(bm.getValueAt(i, "narrvnum"));
    if(ufdNum == null){
      continue;
    }
    ufdPrice = PuPubVO.getUFDouble_NullAsZero(bm.getValueAt(i, "nprice"));
    if(ufdPrice == null){
      continue;
    }
    if(!mapOrderNmoney.containsKey(strOrderBid)){
      continue;
    }
    bm.setValueAt(mapOrderNmoney.get(strOrderBid), i, "nmoney");
    //
    if(bm.getRowState(i) == BillModel.UNSTATE){
      bm.setRowState(i, BillModel.MODIFICATION);
    }
  }
}
/**
 * @���ܣ������޸Ľ��
 */
private boolean onSave() {
	getBillCardPanel().getBodyTabbedPane().setSelectedIndex(0);
  nc.vo.scm.pu.Timer timer= new nc.vo.scm.pu.Timer();
  timer.start("�ɹ������������onSave��ʼ");

  showHintMessage(m_lanResTool.getStrByID("40040301","UPP40040301-000181")/*@res "���ڱ��浥��..."*/);
  //��ֹ�༭
  getBillCardPanel().stopEditing();
  
  // ���Ӷ�У�鹫ʽ��֧��,������ʾ��UAP���� since v501
  if ( ! getBillCardPanel().getBillData().execValidateFormulas()){
      return false;
  }  
  //since v51 : ����ǰ����:����˴ε������������۾��붩��ƥ�䣬����ö������ҽ��
  //dealNmoneyBeforeSave();
  
  //���ڱ����VO
  ArriveorderVO saveVO = null;
  //����ģ������ʾ��VO(ת���޸ļ������޸ĵĽ��)
  ArriveorderVO newvo = (ArriveorderVO) getBillCardPanel().getBillValueVO(ArriveorderVO.class.getName(), ArriveorderHeaderVO.class.getName(), ArriveorderItemVO.class.getName());
  ArriveorderVO oldvo = (ArriveorderVO) getCacheVOs()[getDispIndex()];
  ((ArriveorderHeaderVO) newvo.getParentVO()).setPk_corp(getClientEnvironment().getCorporation().getPrimaryKey());
  ((ArriveorderHeaderVO) oldvo.getParentVO()).setPk_corp(getClientEnvironment().getCorporation().getPrimaryKey());

  if(!m_bSaveMaker && !isRevise) ((ArriveorderHeaderVO) newvo.getParentVO()).setCoperator(getClientEnvironment().getUser().getPrimaryKey());

  //�������
  if(!chechDataBeforeSave(newvo,oldvo))
    return false;
  
  //���̱��淽���Ĳ���
  //aryPara0 : 0����˾������1���޸�ǰ�ĵ�������2����ǰ���ݱ༭״̬��3.�޸ĺ��VO������VO��
  //aryPara1 : 0���Ƿ����û�������1���ϴβ���ʱ�Ķ�����ID����������

  ArrayList aryPara = new ArrayList(2);
  ArrayList aryPara0 = new ArrayList();
  
  aryPara0.add(getCorpPrimaryKey());
  aryPara0.add(null);
  aryPara0.add(getStateStr().equals(OrderPubVO.RC_OUTMODIFY_STATE/*ת���޸�*/) ? "insert" : "update");
  //������������*(-1) �����̨��Ϊ���������� power
  newvo.setDigitsNumPower(CPurchseMethods.getMeasDecimal(getCorpPrimaryKey()) * (-1));
  aryPara0.add(null);
  aryPara.add(aryPara0);
  aryPara.add(null);
  aryPara.add(new Integer(0));
  aryPara.add(new String("cvendormangid"));
  InformationCostVO[] infoCostVOs = (InformationCostVO[])getBillCardPanel().getBillData().getBodyValueVOs("jj_scm_informationcost", InformationCostVO.class.getName());//add by QuSida �õ�������ϢVO
  
  if (getStateStr().equals(OrderPubVO.RC_ARRIVEMODIFY_STATE/*�����޸�*/)) {
    //�����޸ı����VO
    saveVO = (ArriveorderVO) getBillCardPanel().getBillValueChangeVO(ArriveorderVO.class.getName(), ArriveorderHeaderVO.class.getName(), ArriveorderItemVO.class.getName());
    if(!m_bSaveMaker && !isRevise) ((ArriveorderHeaderVO) saveVO.getParentVO()).setCoperator(getClientEnvironment().getUser().getPrimaryKey());

    //������޸�״̬�£�ת��״̬���ã�
    //����б������λ���� �� ��λ��������д���δ�������ɾ������,��ӻ�����һ��������˴���(���ǽ��������ݿ���ɾ��)
    if (vDelNoSplitted.size() > 0) {
      for (int i = 0; i < vDelNoSplitted.size(); i++) {
        v_DeletedItems.addElement(vDelNoSplitted.elementAt(i));
      }
    }
    if (v_DeletedItems.size() > 0) {
      ArriveorderItemVO[] allItems = new ArriveorderItemVO[v_DeletedItems.size() + saveVO.getChildrenVO().length];
      if (v_DeletedItems.size() > 0) {
        for (int i = 0; i < saveVO.getChildrenVO().length; i++) {
          v_DeletedItems.addElement(((ArriveorderItemVO[]) saveVO.getChildrenVO())[i]);
        }
        v_DeletedItems.copyInto(allItems);
      }
      saveVO.setChildrenVO(allItems);
    }
    saveVO.setOprType(VOStatus.UPDATED);
    saveVO.setUpBillType(((ArriveorderItemVO) newvo.getChildrenVO()[0]).getCupsourcebilltype());
    //������Ա
    if(!isRevise){
    	saveVO.setCoperatorid(getOperatorId());
    }
    //������Ҫ
    saveVO.getParentVO().setAttributeValue("cuserid", getOperatorId());
    //��������״̬
    if (!isRevise // ������޸ı��棬״̬���䣬Ӧ�����������У�billstatus = 2
            && (saveVO.getHeadVO().getIbillstatus() == null || saveVO.getHeadVO().getIbillstatus().intValue() != 2)) {
    	saveVO.getHeadVO().setIbillstatus(new Integer(0));
    }
    //add by QuSida 2010-9-11 (��ɽ����) --- begin
    //function:�޸ķ�����Ϣ
        String cbillid = saveVO.getHeadVO().getPrimaryKey();
        if(infoCostVOs!=null&&infoCostVOs.length!=0){
        	for (int i = 0; i < infoCostVOs.length; i++) {
				infoCostVOs[i].setCbillid(cbillid);
			}
        	try {
    			JJPuScmPubHelper.updateSmartVOs(infoCostVOs,cbillid);
    		} catch (Exception e) {
    			// TODO Auto-generated catch block
    			SCMEnv.out(e);
    		}
        }
    	
    
    //add by QuSida 2010-9-11 (��ɽ����) --- end
  } else {
    //����������VO
    saveVO = newvo;
    //���ñ�ͷ����״̬(����)
     ((ArriveorderHeaderVO) saveVO.getParentVO()).setStatus(VOStatus.NEW);
    //�Ƶ���
     ((ArriveorderHeaderVO) saveVO.getParentVO()).setCoperator(getOperatorId());
    for (int i = 0; i < saveVO.getChildrenVO().length; i++) {
      ((ArriveorderItemVO[]) saveVO.getChildrenVO())[i].setStatus(VOStatus.NEW);
    }
    saveVO.setOprType(VOStatus.NEW);
    //���̻�д�ã��ϲ���Դ��������
    saveVO.setUpBillType(((ArriveorderItemVO) oldvo.getChildrenVO()[0]).getCupsourcebilltype());
    //������Ա
    saveVO.setCoperatorid(getOperatorId());
    //�жϲ����Ĺ��÷��� PubDMO.checkVoNoChanged() ��Ҫ
    saveVO.getParentVO().setAttributeValue("cuserid", getOperatorId());
  }
  //������Լ����--��ע
  UIRefPane nRefPanel = (UIRefPane) getBillCardPanel().getHeadItem("vmemo").getComponent();
  UITextField vMemoField = nRefPanel.getUITextField();
  String vmemo = vMemoField.getText();
  ((ArriveorderHeaderVO) saveVO.getParentVO()).setVmemo(vmemo);
  //������Լ����--�˻�����
  if (getBillCardPanel().getHeadItem("vbackreasonh") != null) {
    UIRefPane refPanel = (UIRefPane) getBillCardPanel().getHeadItem("vbackreasonh").getComponent();
    UITextField txtBack = refPanel.getUITextField();
    String strBack = txtBack.getText();
    ((ArriveorderHeaderVO) saveVO.getParentVO()).setVbackreasonh(strBack);
  }
  timer.addExecutePhase(m_lanResTool.getStrByID("40040301","UPP40040301-000254")/*@res "����ǰ��׼������"*/);
  //Ϊ�ж��Ƿ���޸ġ����������˵���
  ((ArriveorderHeaderVO) saveVO.getParentVO()).setCoperatoridnow(getOperatorId());
  String strRetKey = null;
  boolean isCycle = true;
  //�޸�ʱ��ƴ��δ�ı�ı���VO(˵���μ�getSaveVO()����)
  if (getStateStr().equals("�����޸�")) {
    saveVO = getSaveVO(saveVO);
    timer.addExecutePhase("getSaveVO");
  }
  //�Ƿ���Ҫ���˵��ݺ�:�������ֹ�¼�뵥�ݺ�
  if (getStateStr().equals("ת���޸�")) {
    if (saveVO.getParentVO() != null && saveVO.getParentVO().getAttributeValue("varrordercode") != null && saveVO.getParentVO().getAttributeValue("varrordercode").toString().trim().length() > 0) {
    }
  }
  doCycle : while (isCycle) {
    isCycle = false;
    try {
      setReWriteData(saveVO,newvo,oldvo);
   
      ArrayList aryRet = (ArrayList) PfUtilClient.processAction("SAVEBASE", ScmConst.PO_Arrive, ClientEnvironment.getInstance().getDate().toString(), saveVO, aryPara);
      
    //add by QuSida 2010-8-31 (��ɽ����)  --- begin
	//function:����������ɹ���,��������ϢVO���浽���ݿ���	
      if(infoCostVOs!=null&&infoCostVOs.length!=0){

			//��������������vo������
    	  String pk_bill = (String)aryRet.get(0);
		//String pk_bill = saveVO.getParentVO().getPrimaryKey();
			for (int i = 0; i < infoCostVOs.length; i++) {
				infoCostVOs[i].setCbillid(pk_bill);
			}
		JJPuScmPubHelper.updateSmartVOs(infoCostVOs,pk_bill);
		}
    //add by QuSida 2010-8-31 (��ɽ����)  --- end
      timer.addExecutePhase("ִ��SAVE�ű�");
      // ��������ˢ�»���VO��TS
      refreshVOTs((ArriveorderVO)aryRet.get(1));
      timer.addExecutePhase("ˢ�»���VO��TS");
      strRetKey = aryRet == null ? null : (String) aryRet.get(0);
      boolean bIsSucc = PfUtilClient.isSuccess();
      //�������
      if (getStateStr().equals("�����޸�")) {
        if (strRetKey != null && bIsSucc) {
          setM_strState("�������");
          //�ű����趨��VO
          getCacheVOs()[getDispIndex()] = (ArriveorderVO) aryRet.get(1);
          if (getCacheVOs()[getDispIndex()] == null)
            throw new BusinessException(m_lanResTool.getStrByID("40040301","UPP40040301-000178")/*@res "�������ݳɹ�����ˢ������ʱ�������Ժ����ԣ�"*/);
          //ArriveorderBO_Client.findByPrimaryKey(oldvo.getParentVO().getPrimaryKey());
          //˳�����û�
          setButtonsState();
          checkVprocessbatch(new ArriveorderVO[]{getCacheVOs()[getDispIndex()]});
          loadDataToCard();
          getBillCardPanel().setEnabled(false);
          showHintMessage(m_lanResTool.getStrByID("SCMCOMMON","UPPSCMCommon-000006")/*@res "����ɹ�"*/);
          updateUI();
        }
      } else if (getStateStr().equals("ת���޸�")) {
        if (strRetKey != null && bIsSucc) {
          //�ӵ�ǰδ���浥����ɾ������ɹ�����
          delArriveorderVOSaved();
          //�Ѹ���ts��VO
          ArriveorderVO voTmp = (ArriveorderVO) aryRet.get(1);
          if (voTmp == null)
            throw new BusinessException(m_lanResTool.getStrByID("40040301","UPP40040301-000178")/*@res "�������ݳɹ�����ˢ������ʱ�������Ժ����ԣ�"*/);
          
//          refreshVoFieldsByKey(voTmp,strRetKey);
          checkVprocessbatch(new ArriveorderVO[]{voTmp});
          //���ӱ���ɹ����ݵ�����ʾ���ݻ���ĩβ
          appArriveorderVOSaved(voTmp);
          //����ת���Ƿ��������ͬ����
          if (getCacheVOs() != null) {
            displayArrBillListPanelNew();
            showHintMessage(m_lanResTool.getStrByID("40040301","UPP40040301-000182")/*@res "����ɹ�,����ת��"*/);
          } else {
            onEndCreate();
            showHintMessage(m_lanResTool.getStrByID("40040301","UPP40040301-000179")/*@res "����ɹ�,ת������"*/);
          }
        }
      }
      //ArriveorderVO voTmp = (ArriveorderVO) aryRet.get(1);
      /************��¼ҵ����־*************//*
       * �������Ż�������־�����ŵ���̨
		Operlog operlog=new Operlog();
		voTmp.getOperatelogVO().setOpratorname(nc.ui.pub.ClientEnvironment.getInstance().getUser().getUserName());
		voTmp.getOperatelogVO().setCompanyname(nc.ui.pub.ClientEnvironment.getInstance().getCorporation().getUnitname());
		voTmp.getOperatelogVO().setOperatorId(nc.ui.pub.ClientEnvironment.getInstance().getUser().getPrimaryKey());
		operlog.insertBusinessExceptionlog(voTmp, "����", "����", nc.vo.scm.funcs.Businesslog.MSGMESSAGE, nc.vo.scm.pu.BillTypeConst.PO_ARRIVE,
				nc.ui.sm.cmenu.Desktop.getApplet().getParameter("USER_IP"));
		*//************��¼ҵ����־* end ************/
      timer.addExecutePhase("�������");
      setButtonsRevise();
      
      //��д
//      getBillCardPanel().getBillData().setBodyValueVO("jj_scm_informationcost", infoCostVOs);
      getBillCardPanel().getBillModel("jj_scm_informationcost").setBodyDataVO(infoCostVOs);
      
      showHintMessage(m_lanResTool.getStrByID("common","UCH005")/*@res "����ɹ�"*/);
    } catch (Exception e) {
//      //���˵��ݺ�
//      if (isBackCode) {
//        SCMEnv.out("���˵��ݺſ�ʼ[ArriveUI]...");
//        try {
//          PubHelper.returnBillCode(newvo);
//        } catch (Exception ex) {
//          SCMEnv.out("���˵��ݺ��쳣����[ArriveUI]");
//        }
//        SCMEnv.out("���˵��ݺ���������[ArriveUI]");
//      }
      //�����д�ɹ��������ݲ���ʾ���
      if (e instanceof RwtRcToPoException) {
        //�빺���ۼ�����������ʾ
        int iRet = showYesNoMessage(e.getMessage()) ;
        if (iRet==MessageDialog.ID_YES) {
          //����ѭ��
          isCycle = true ;
          //�Ƿ��û�ȷ��
          saveVO.setUserConfirm(true);
        } else {
          return false;
        }
      }
      //�����дί�ⶩ�����ݲ���ʾ���
      else if (e instanceof RwtRcToScException) {
        //�����ۼ�����������ʾ
        int iRet = showYesNoMessage(e.getMessage()) ;
        if (iRet==MessageDialog.ID_YES) {
          //����ѭ��
          isCycle = true ;
          //�Ƿ��û�ȷ��
          saveVO.setUserConfirm(true);
        } else {
          return false;
        }
      }else if (e instanceof BusinessException || e instanceof java.rmi.RemoteException || e instanceof ValidationException) {
        MessageDialog.showErrorDlg(this, m_lanResTool.getStrByID("SCMCOMMON","UPPSCMCommon-000059")/*@res "����"*/, e.getMessage());
      } else if (
        e.getMessage() != null
          && (e.getMessage().indexOf(m_lanResTool.getStrByID("40040301","UPP40040301-000250")/*@res "����"*/) >= 0
            || e.getMessage().indexOf(m_lanResTool.getStrByID("40040301","UPP40040301-000212")/*@res "����"*/) >= 0
            || e.getMessage().indexOf(m_lanResTool.getStrByID("40040301","UPT40040301-000025")/*@res "�˻�"*/) >= 0
            || e.getMessage().indexOf(m_lanResTool.getStrByID("40040301","UPP40040301-000207")/*@res "�ջ�"*/) >= 0
            || e.getMessage().indexOf(m_lanResTool.getStrByID("40040301","UPP40040301-000251")/*@res "����"*/) >= 0
            || e.getMessage().indexOf(m_lanResTool.getStrByID("40040301","UPP40040301-000252")/*@res "�ݲ�"*/) >= 0
            || e.getMessage().indexOf(m_lanResTool.getStrByID("40040301","UPP40040301-000253")/*@res "��"*/) >= 0
            || e.getMessage().indexOf("BusinessException") >= 0
            || e.getMessage().indexOf("RemoteException") >= 0)) {
        MessageDialog.showErrorDlg(this, m_lanResTool.getStrByID("SCMCOMMON","UPPSCMCommon-000059")/*@res "����"*/, e.getMessage());
      } else{
        MessageDialog.showErrorDlg(this, m_lanResTool.getStrByID("SCMCOMMON","UPPSCMCommon-000059")/*@res "����"*/, m_lanResTool.getStrByID("40040301","UPP40040301-000183")/*@res "ϵͳ�쳣������ʧ��"*/);
      }
      showHintMessage(m_lanResTool.getStrByID("SCMCOMMON","UPPSCMCommon-000010")/*@res "����ʧ��"*/);
    }
  }
  timer.showAllExecutePhase("�ɹ������������onSave��������") ;
  
  return true;
}
/**
 * ˢ�±���ɹ�����(��Ϊ���漰������������Ҫ��Ҫˢ���������ں�������,����״̬��TS���Ƶ�ʱ�䣬����ʱ�䣬����޸�ʱ�䣬����ʱ���)
 * @param vo
 * @param strKey
 * @author czp
 * @date 2006-05-18
 */
private void refreshVoFieldsByKey(ArriveorderVO vo,String strKey) throws Exception{
  //
  ArrayList arrRet = ArriveorderHelper.queryForSaveAudit(strKey);
  ((ArriveorderHeaderVO)vo.getParentVO()).setDauditdate((UFDate)arrRet.get(0));
  ((ArriveorderHeaderVO)vo.getParentVO()).setCauditpsn((String)arrRet.get(1));
  ((ArriveorderHeaderVO)vo.getParentVO()).setIbillstatus((Integer)arrRet.get(2));
  ((ArriveorderHeaderVO)vo.getParentVO()).setTs((String)arrRet.get(3));
  ((ArriveorderHeaderVO)vo.getParentVO()).setTmaketime((UFDateTime) arrRet.get(4));
  ((ArriveorderHeaderVO)vo.getParentVO()).setTaudittime((UFDateTime) arrRet.get(5));
  ((ArriveorderHeaderVO)vo.getParentVO()).setTlastmaketime((UFDateTime) arrRet.get(6));
  
  ArriveorderItemVO[] itemVOS = (ArriveorderItemVO[])vo.getChildrenVO();
  Map btsHP = (HashMap)arrRet.get(7);
  for(ArriveorderItemVO item:itemVOS){
	  if(btsHP.get(item.getPrimaryKey())!=null)
		  item.setTs(btsHP.get(item.getPrimaryKey()).toString());
  }
}
/**
 * @���ܣ�ѡ�����е�����
 * @���ߣ���־ƽ
 * �������ڣ�(2001-6-8 14:21:35)
 * @param:<|>
 * @return:
 * @exception:
 * @see;
 * @since:
 *
 */
private void onSelectAll() {
  int iLen = getBillListPanel().getHeadBillModel().getRowCount();
  getBillListPanel().getHeadTable().setRowSelectionInterval(0, iLen - 1);
  for (int i = 0; i < iLen; i++) {
    getBillListPanel().getHeadBillModel().setRowState(i, BillModel.SELECTED);
  }
  //���ð�ť״̬
  setButtonsList();
  //
  showHintMessage(m_lanResTool.getStrByID("common","4004COMMON000000033")/*@res "ȫ��ѡ�гɹ�"*/);
}

/**
 * @���ܣ�ȡ������ѡ���ĵ���������
 * @���ߣ���־ƽ
 * �������ڣ�(2001-6-8 14:22:12)
 * @param:<|>
 * @return:
 * @exception:
 * @see;
 * @since:
 *
 */
private void onSelectNo() {
  int iLen = getBillListPanel().getHeadBillModel().getRowCount();
  getBillListPanel().getHeadTable().removeRowSelectionInterval(0, iLen - 1);
  for (int i = 0; i < iLen; i++) {
    getBillListPanel().getHeadBillModel().setRowState(i, BillModel.NORMAL);
  }
  //���ð�ť״̬
  setButtonsList();
  //
  showHintMessage(m_lanResTool.getStrByID("common","4004COMMON000000034")/*@res "ȫ��ȡ���ɹ�"*/);
}

/**
 * ����:ִ������
 */
private void onUnAudit(ButtonObject bo) {
  showHintMessage(m_lanResTool.getStrByID("40040301","UPP40040301-000186")/*@res "��������..."*/);
  ArriveorderVO vo = getCacheVOs()[getDispIndex()];  
  //���õ�ǰ����Ա
  vo.getHeadVO().setCoperatoridnow(nc.ui.pub.ClientEnvironment.getInstance().getUser().getPrimaryKey());
  //���������˼��������ڹ�ϣ������ʧ��ʱ�õ�
  String strPsnOld = vo.getHeadVO().getCauditpsn();
  //����ǰ������
  vo.getHeadVO().setCauditpsnold(strPsnOld);
  UFDate dateAuditOld = vo.getHeadVO().getDauditdate();
  //
  try {
    //����
    PfUtilClient.processBatchFlow(
      null,
      "UNAPPROVE"+ getClientEnvironment().getUser().getPrimaryKey(),
      ScmConst.PO_Arrive,
      getClientEnvironment().getDate().toString(),
      new ArriveorderVO[] { vo },
      null);
    
    if (!PfUtilClient.isSuccess()) {
      //����ʧ�ܣ��ָ������˼���������
      vo.getHeadVO().setCauditpsn(strPsnOld);
      vo.getHeadVO().setDauditdate(dateAuditOld);
      //
      showHintMessage(m_lanResTool.getStrByID("40040301","UPP40040301-000187")/*@res "����ʧ��(ƽ̨���ó����쳣)"*/);
      return;
    }
    //����ɹ���ˢ��
    refreshVoFieldsByKey(vo,vo.getParentVO().getPrimaryKey());

    getCacheVOs()[getDispIndex()] = vo;
    //���ص���
    try {
    	getBillCardPanel().setBillValueVO(getCacheVOs()[getDispIndex()]);
    	lightRefreshUI();
    	setHeadValueByKey("vmemo", (String)getCacheVOs()[getDispIndex()].getParentVO().getAttributeValue("vmemo"));
  	  //function:��ѯ��ط�����Ϣ
  	  String pk = (String)getCacheVOs()[getDispIndex()].getHeadVO().getAttributeValue("carriveorderid");
  		String sql = "cbillid = '"+pk+"' and dr = 0";
  		InformationCostVO[] vos = null;

  		 try {
  			vos = (InformationCostVO[])JJPuScmPubHelper.querySmartVOs(InformationCostVO.class, null, sql);
  		} catch (Exception e) {
  			// TODO Auto-generated catch block
  			SCMEnv.out(e);
  		}
  	if(vos!=null&&vos.length!=0){
  		 getBillCardPanel().getBillModel("jj_scm_informationcost").setBodyDataVO(vos);
  		 getBillCardPanel().getBillModel("jj_scm_informationcost").execLoadFormula();
  	}else{
			 //20101014-11:51 MeiChao ���������ϢΪ��,�����������Ϣҳǩ��ʷ����.
			getBillCardPanel().getBillModel("jj_scm_informationcost").setBodyDataVO(null); 
		 }
    } catch (Exception e) {
      showHintMessage(m_lanResTool.getStrByID("40040301","UPP40040301-000188")/*@res "����ɹ�,�����ص���ʱ�����쳣,��ˢ�½����ٽ�����������"*/);
    }
    //ˢ�°�ť״̬
    setButtonsState();
    setButtonsRevise();

    //
    showHintMessage(m_lanResTool.getStrByID("common","UCH011")/*@res "����ɹ�"*/);
  } catch (Exception e) {
    //����ʧ�ܣ��ָ������˼���������
    vo.getHeadVO().setCauditpsn(strPsnOld);
    vo.getHeadVO().setDauditdate(dateAuditOld);
    //
    showHintMessage(m_lanResTool.getStrByID("40040301","UPP40040301-000189")/*@res "�����쳣,����ʧ��"*/);
    SCMEnv.out(e);
    if (e instanceof java.rmi.RemoteException || e instanceof BusinessException){
      MessageDialog.showErrorDlg(this,m_lanResTool.getStrByID("40040301","UPP40040301-000099")/*@res "����"*/,e.getMessage());
    }
  }
}

/**
 * ���ߣ���ά��
 * ���ܣ�ˢ�±����л���TS�����TS�����ڴ�����
 * ������
 * ���أ�
 * ���⣺
 * ���ڣ�(2004-4-1 10:55:04)
 */
private void refreshVOTs(ArriveorderVO vo) {
  if (m_hTS == null)
    return;
  if (vo == null)
    return;
  ArriveorderItemVO[] items = (ArriveorderItemVO[]) vo.getChildrenVO();
  if (items == null || items.length == 0)
    return;
  String sUpSourceBillType = items[0].getCupsourcebilltype();
  if (sUpSourceBillType == null || sUpSourceBillType.trim().length() == 0)
    return;
  int size = items.length;
  //ˢ�»���VO�ı���TS,������
  try {
    if(items[0].getCupsourcebilltype().equals("21")){
      String[] sID = new String[size];
      for(int i = 0;i < size;i++){
        sID[i] = items[i].getCorder_bid();
      }
      //ˢ��TS
      HashMap hTs = ArriveorderHelper.queryNewTs(sID);
      for(int i = 0;i < size;i++){
        Object[] ob = (Object[])hTs.get(items[i].getCorder_bid());
        if(ob != null && ob[0] != null)
          items[i].setTsbup(ob[0].toString());
      }
    }
    ArriveorderVO[] vos = getCacheVOs();
    //������ʽ����
    if (m_pushSaveVOs != null && m_pushSaveVOs.length > 0)
      vos = m_pushSaveVOs;
    if (vos == null || vos.length == 0)
      return;

    String sUpSourceBTs = null;
    String sUpsourceRowid = null;

    //����TS����TS����
    for (int i = 0; i < size; i++) {
      sUpSourceBTs = items[i].getTsbup();
      sUpsourceRowid = items[i].getCupsourcebillrowid();
      String sTs = null;
      if (m_hTS.containsKey(sUpsourceRowid)) {
        sTs = (String) m_hTS.get(sUpsourceRowid);
        if (sTs != null && sTs.trim().length() > 0 && !sTs.equals(sUpSourceBTs)) {
          m_hTS.remove(sUpsourceRowid);
          m_hTS.put(sUpsourceRowid, sUpSourceBTs);
        }
      } else {
        m_hTS.put(sUpsourceRowid, sUpSourceBTs);
      }
    }

    //��TS����ȥ����VO����
    for (int i = 0; i < vos.length; i++) {
      ArriveorderItemVO[] itemVOs = (ArriveorderItemVO[]) vos[i].getChildrenVO();
      for (int j = 0; j < itemVOs.length; j++) {
        sUpSourceBTs = itemVOs[j].getTsbup();
        sUpsourceRowid = itemVOs[j].getCupsourcebillrowid();
        String sTs = (String) m_hTS.get(sUpsourceRowid);
        if (sTs != null && sTs.trim().length() > 0 && !sTs.equals(sUpSourceBTs)) {
          itemVOs[j].setTsbup(sTs);
        }
      }
    }
  } catch (Exception e) {
    reportException(e);
  }
}

public void setBillVO(nc.vo.pub.AggregatedValueObject vo){
  UIRefPane refPane = null;
  BillModel bm = getBillCardPanel().getBillModel();
  ArriveorderVO VO = (ArriveorderVO) vo;
  
  //����VO����Ƭ����ģ��
  getBillCardPanel().setBillValueVO(VO);
  //����������ݱ�����(DR!=0)�򶳽�ʱ������ȷ��ʾ��������
  loadBDData();
  //������ģ���Զ�����
  ArriveorderHeaderVO voHead = (ArriveorderHeaderVO) VO.getParentVO();
  String[] saKey = new String[] { "vdef1", "vdef2", "vdef3", "vdef4", "vdef5", "vdef6", "vdef7", "vdef8", "vdef9", "vdef10","vdef11", "vdef12", "vdef13", "vdef14", "vdef15", "vdef16", "vdef17", "vdef18", "vdef19", "vdef20" };
  int iLen = saKey.length;
  for (int i = 0; i < iLen; i++) {
    voHead.setAttributeValue(saKey[i],getBillCardPanel().getHeadItem(saKey[i]).getValueObject());
  }
    //�رպϼƿ���
  boolean  bOldNeedCalc = bm.isNeedCalculate() ; 
  bm.setNeedCalculate(false) ;
  //ִ�м��ع�ʽ    
  bm.execLoadFormula();   
    //�򿪺ϼƿ���
  bm.setNeedCalculate(bOldNeedCalc) ;
  getBillCardPanel().updateValue();
  
  
  //��ʾ��ͷ��ע
  setHeadValueByKey("vmemo",(String) VO.getParentVO().getAttributeValue("vmemo"));
  
  //��ʾ��ͷ�˻�����
  if (getBillCardPanel().getHeadItem("vbackreasonh") != null) {
	  setHeadValueByKey("vbackreasonh",(String) VO.getParentVO().getAttributeValue("vbackreasonh"));
  }
  //�����滻�����չ����Ӳ��� {�������л򶩵���ID = cmangid }
  Vector vCmangids = new Vector();
  String strCmangid = null;
  m_hBillIDsForCmangids = new Hashtable();
  String strKey = (getStateStr().equals("ת���޸�")) ? "corder_bid" : "carriveorder_bid";
  for (int i = 0; i < bm.getRowCount(); i++) {
    strCmangid = (String) bm.getValueAt(i, "cmangid");
    if (strCmangid != null && strCmangid.trim().length() > 0 && !vCmangids.contains(strCmangid))
      vCmangids.addElement(strCmangid);
    if (bm.getValueAt(i, strKey) == null)
      continue;
    if (!m_hBillIDsForCmangids.containsKey(bm.getValueAt(i, strKey)))
      m_hBillIDsForCmangids.put(bm.getValueAt(i, strKey), bm.getValueAt(i, "cmangid"));
  }
  //���д�����屸ע������
  String strCmain = null;
  String strCbaseid = null;
  String strCassid = null;
  Object oNarrvnum = null;
  Object oNassinum = null;
  UFDouble ufdNarrvnum = null;
  UFDouble ufdNassinum = null;
  Object oValue = null;
  for (int i = 0; i < bm.getRowCount(); i++) {
    //���屸ע��ʼ��:�����ܴ���afterEdit()
    if (bm.getValueAt(i, "vmemo") == null) {
      bm.setValueAt("", i, "vmemo");
    }
    //�����˻����ɳ�ʼ��:�����ܴ���afterEdit()
    if (getBillCardPanel().getBodyItem("vbackreasonb") != null) {
      if (bm.getValueAt(i, "vbackreasonb") == null) {
        bm.setValueAt("", i, "vbackreasonb");
      }
    }
    //�Ƿ������ɵ���--��δ��
    bm.setValueAt(new UFBoolean(false), i, "issplit");

    strCbaseid = (String) bm.getValueAt(i, "cbaseid");
    strCmangid = (String) bm.getValueAt(i, "cmangid");
    strCassid = (String) bm.getValueAt(i, "cassistunit");
    strCmain = (String) bm.getValueAt(i, "cmainmeasid");
    //�Ƿ񸨼�������
    UFBoolean bIsAssMana = new UFBoolean(PuTool.isAssUnitManaged(strCbaseid));
    if (bIsAssMana.booleanValue()) {
      if (strCassid == null || strCassid.trim().equals("")) {
        MessageDialog.showErrorDlg(this, m_lanResTool.getStrByID("SCMCOMMON","UPPSCMCommon-000059")/*@res "����"*/, m_lanResTool.getStrByID("40040301","UPP40040301-000190")/*@res "�и���������Ĵ���д��ڿո�������"*/);
        return;
      }
      //���û�����
      UFDouble convert = PuTool.getInvConvRateValue(strCbaseid, strCassid);
      bm.setValueAt(convert, i, "convertrate");
      //����������ͬ��������Ϊ 1.0
      if (strCmain != null && strCmain.equals(strCassid)) {
        bm.setValueAt(new UFDouble(1.0), i, "convertrate");
      }
      //�ǹ̶������ʣ�������=������/������
      if (!PuTool.isFixedConvertRate(strCbaseid, strCassid)) {
        oNarrvnum = bm.getValueAt(i, "narrvnum");
        oNassinum = bm.getValueAt(i, "nassistnum");
        if (!(oNarrvnum == null || oNarrvnum.toString().trim().equals("")) && !(oNassinum == null || oNassinum.toString().trim().equals(""))) {
          ufdNarrvnum = new UFDouble(oNarrvnum.toString().trim());
          ufdNassinum = new UFDouble(oNassinum.toString().trim());
          oValue = ufdNassinum == new UFDouble(0) ? null : ufdNarrvnum.div(ufdNassinum);
        } else
          oValue = null;
        bm.setValueAt(oValue, i, "convertrate");
      }
    } else {
      bm.setValueAt(null, i, "convertrate");
    }
  }
  PuTool.loadSourceInfoAll(getBillCardPanel(),BillTypeConst.PO_ARRIVE);
  initInvRef();

}
/**
 * @���ܣ���Ƭ��ʾ����
 */
private void loadDataToCard() throws Exception {
  UIRefPane refPane = null;
  BillModel bm = getBillCardPanel().getBillModel();
  if (getCacheVOs() != null) {    
    //���б���Ƭ�л�ʱ��������
    if (isFrmList) {
      isFrmList = false;
      //��ȡ������ʵ��λ��
      int iShowPos = getBillListPanel().getHeadTable().getSelectedRow();
      if (iShowPos >= 0) {
        iShowPos = PuTool.getIndexBeforeSort(getBillListPanel(), iShowPos);
        setDispIndex(iShowPos);
      }
    }
    //��������ʱ���ش���ˢ�±���,V5,�������޸�Ҫˢ�£���Ϊ�����Ƶ�ʱ�䡢����޸�ʱ�䡢����ʱ��
    if (!getStateStr().equals("ת���޸�")) {
      //��ȡ����(δ�����ع���ˢ��)
      try {
        getCacheVOs()[getDispIndex()] = RcTool.getRefreshedVO(getCacheVOs()[getDispIndex()]);
      } catch (Exception be) {
        if (be instanceof BusinessException)
          MessageDialog.showErrorDlg(this, m_lanResTool.getStrByID("SCMCOMMON","UPPSCMCommon-000422")/*@res "ҵ���쳣"*/, be.getMessage());
        throw be;
      }
    } 
    //������ʽ������ⵥʱ����������쳣��Ҫ�������˺����������ÿ�
    ArriveorderHeaderVO ArrBillHeadVO = (ArriveorderHeaderVO)getCacheVOs()[getDispIndex()].getParentVO();
    if(ArrBillHeadVO.getIbillstatus().intValue() == 0){
      ArrBillHeadVO.setCauditpsn(null);
      ArrBillHeadVO.setDauditdate(null);
    }
    //���ɹ���˾��ʼ���Ĳ��գ�{ҵ��Ա�����š���Ӧ��}
    String strPurCorp = ArrBillHeadVO.getPk_purcorp();    
    ((UIRefPane)getBillCardPanel().getHeadItem("cemployeeid").getComponent()).getRefModel().setPk_corp(strPurCorp);
    ((UIRefPane)getBillCardPanel().getHeadItem("cdeptid").getComponent()).getRefModel().setPk_corp(strPurCorp);
    
    /* V50 ����ǰ�� 2006-11-24 : Wangyf&Xy&Xhq&Czp,ע�͵�,���ջ���˾����Ӧ�̲���
    ((UIRefPane)getBillCardPanel().getHeadItem("cvendormangid").getComponent()).getRefModel().setPk_corp(strPurCorp);
    */
    
    //ִ�����ε�����ʽ,�������ε�����Ϣ
    RcTool.execFormulaForBatchCode(((ArriveorderVO)getCacheVOs()[getDispIndex()]).getBodyVo());

    //����VO����Ƭ����ģ��
    getBillCardPanel().setBillValueVO(getCacheVOs()[getDispIndex()]);
    //����������ݱ�����(DR!=0)�򶳽�ʱ������ȷ��ʾ��������
    loadBDData();
    //������ģ���Զ�����
    ArriveorderHeaderVO voHead = (ArriveorderHeaderVO) getCacheVOs()[getDispIndex()].getParentVO();
    String[] saKey = new String[] { "vdef1", "vdef2", "vdef3", "vdef4", "vdef5", "vdef6", "vdef7", "vdef8", "vdef9", "vdef10","vdef11", "vdef12", "vdef13", "vdef14", "vdef15", "vdef16", "vdef17", "vdef18", "vdef19", "vdef20" };
    int iLen = saKey.length;
    for (int i = 0; i < iLen; i++) {
      voHead.setAttributeValue(saKey[i],getBillCardPanel().getHeadItem(saKey[i]).getValue());
    }
        //�رպϼƿ���
    boolean  bOldNeedCalc = bm.isNeedCalculate() ; 
    bm.setNeedCalculate(false) ;
    //ִ�м��ع�ʽ    
    bm.execLoadFormula();   
        //�򿪺ϼƿ���
    bm.setNeedCalculate(bOldNeedCalc) ;
    getBillCardPanel().updateValue();
    
    //��ʾ��ͷ��ע
    refPane = (UIRefPane) getBillCardPanel().getHeadItem("vmemo").getComponent();
    refPane.setValue((String) getCacheVOs()[getDispIndex()].getParentVO().getAttributeValue("vmemo"));
    //��ʾ��ͷ�˻�����
    if (getBillCardPanel().getHeadItem("vbackreasonh") != null) {
      refPane = (UIRefPane) getBillCardPanel().getHeadItem("vbackreasonh").getComponent();
      refPane.setValue((String) getCacheVOs()[getDispIndex()].getParentVO().getAttributeValue("vbackreasonh"));
//      if (getBillCardPanel().getHeadItem("bisback") != null){
//    	  boolean bIsEdit = getStateStr().equals("�����޸�") || getStateStr().equals("ת���޸�");
//    	  getBillCardPanel().getHeadItem("bisback").setEnabled(isBackBill() && bIsEdit);
//      }

    }
    //�����滻�����չ����Ӳ��� {�������л򶩵���ID = cmangid }
    Vector vCmangids = new Vector();
    String strCmangid = null;
    m_hBillIDsForCmangids = new Hashtable();
    String strKey = (getStateStr().equals("ת���޸�")) ? "corder_bid" : "carriveorder_bid";
    for (int i = 0; i < bm.getRowCount(); i++) {
      strCmangid = (String) bm.getValueAt(i, "cmangid");
      if (strCmangid != null && strCmangid.trim().length() > 0 && !vCmangids.contains(strCmangid))
        vCmangids.addElement(strCmangid);
      if (bm.getValueAt(i, strKey) == null)
        continue;
      if (!m_hBillIDsForCmangids.containsKey(bm.getValueAt(i, strKey)))
        m_hBillIDsForCmangids.put(bm.getValueAt(i, strKey), bm.getValueAt(i, "cmangid"));
    }
    //���д�����屸ע������
    String strCmain = null;
    String strCbaseid = null;
    String strCassid = null;
    Object oNarrvnum = null;
    Object oNassinum = null;
    UFDouble ufdNarrvnum = null;
    UFDouble ufdNassinum = null;
    Object oValue = null;
    
    UFDate dvaliddate = null;
    int intValiddays = 0;
    
    if(!refreshCardData())
    	return;
    //����޸ģ����ù��ܰ�ť�Ƿ���Ч
    if (!getStateStr().equals("ת���޸�")) {
      getBillCardPanel().getHeadItem("varrordercode").setEnabled(false);
      if (getStateStr().equals("�������")) {
        setBtnLines(false);
      }
    }
    //ת���޸ģ����õ��ݺš��ñ���������
    if (getStateStr().equals("ת���޸�")) {
      getBillCardPanel().getHeadItem("varrordercode").setValue(null);
      getBillCardPanel().getHeadItem("varrordercode").setEnabled(getBillCardPanel().getHeadItem("varrordercode").isEdit());
      //���汣��������
      String[] arrStrCmangids = null;
      if (vCmangids.size() > 0) {
        arrStrCmangids = new String[vCmangids.size()];
        vCmangids.copyInto(arrStrCmangids);
      }
      Object qualitydaynum=null;
      Object qualityperiodunit=null;
      Object[][] retOb = null;
      
      for (int i = 0; i < bm.getRowCount(); i++) {
        strCmangid = (String) bm.getValueAt(i, "cmangid");
        try{
          retOb = CacheTool.getMultiColValue("bd_invmandoc", "pk_invmandoc", new String[] { "qualitydaynum", "qualityperiodunit" }, new String[] { strCmangid });
          if (retOb != null && retOb.length > 0 && retOb[0] != null && retOb[0].length > 1) {
              qualitydaynum=retOb[0][0];
              qualityperiodunit=retOb[0][1];
          }
          intValiddays = PuTool.calcQualityDay(PuPubVO.getInteger_NullAs(qualitydaynum, 0),PuPubVO.getInteger_NullAs(qualityperiodunit, 0));
          bm.setValueAt(intValiddays, i, "ivalidday");
          dvaliddate = (UFDate) getBillCardPanel().getBillModel().getValueAt(i, "dvaliddate");
          if(dvaliddate!=null){
            bm.setValueAt(dvaliddate.getDateBefore(intValiddays), i, "dproducedate");
          }
        }catch(Exception ee){
         SCMEnv.out(ee);
        }

      }
    }
    //����״̬ͼƬ
    try {
      getBillCardPanel().update(getGraphics());
    } catch (Exception e) {
      SCMEnv.out("����ͼƬʱ����(��Ӱ��ҵ�����)");
    }
    
    loadSourceInfo();
    
  } else {
    if (getBillCardPanel().getBillData() != null) {
      //V5 Del : setImageType(this.IMAGE_NULL);
      getBillCardPanel().getBillData().clearViewData();
      getBillCardPanel().updateUI();
    }
  }
  //������������ҵ����������ֵ
  if (getBusitype() != null) {
    m_refBusi.setPK(getBusitype());
  }
  //��ֵ���ֶ�������������
  setNumFieldsNeg(isBackBill()); //isBackBill()�����ڵ���ģ����ֵ֮��
  //�˻�����(ͷ��)
  boolean bIsEdit = getStateStr().equals("�����޸�") || getStateStr().equals("ת���޸�");
  if(bIsEdit){
    if (getBillCardPanel().getHeaderPanel("vbackreasonh") != null) {
      getBillCardPanel().getHeadItem("vbackreasonh").setEnabled(isBackBill() && getBillCardPanel().getHeadItem("vbackreasonh").isEdit());
      getBillCardPanel().getHeadItem("vbackreasonh").setEdit(isBackBill() && getBillCardPanel().getHeadItem("vbackreasonh").isEdit());
    }
    if (getBillCardPanel().getBodyItem("vbackreasonb") != null)
      getBillCardPanel().getBodyItem("vbackreasonb").setEdit(isBackBill() && getBillCardPanel().getBodyItem("vbackreasonb").isEdit());
  }
  initInvRef();
}
private boolean refreshCardData() {
	String strCmangid;
	String strCmain;
	String strCbaseid;
	String strCassid;
	Object oNarrvnum;
	Object oNassinum;
	UFDouble ufdNarrvnum;
	UFDouble ufdNassinum;
	Object oValue;
	UFDate dvaliddate;
	int intValiddays;
	BillModel bm = getBillCardPanel().getBillModel();
	for (int i = 0; i < bm.getRowCount(); i++) {
      //���屸ע��ʼ��:�����ܴ���afterEdit()
      if (bm.getValueAt(i, "vmemo") == null) {
        bm.setValueAt("", i, "vmemo");
      }
      //�����˻����ɳ�ʼ��:�����ܴ���afterEdit()
      if (getBillCardPanel().getBodyItem("vbackreasonb") != null) {
        if (bm.getValueAt(i, "vbackreasonb") == null) {
          bm.setValueAt("", i, "vbackreasonb");
        }
      }
      //�Ƿ������ɵ���--��δ��
      bm.setValueAt(new UFBoolean(false), i, "issplit");

      strCbaseid = (String) bm.getValueAt(i, "cbaseid");
      strCmangid = (String) bm.getValueAt(i, "cmangid");
      strCassid = (String) bm.getValueAt(i, "cassistunit");
      strCmain = (String) bm.getValueAt(i, "cmainmeasid");
      //�Ƿ񸨼�������
      UFBoolean bIsAssMana = new UFBoolean(PuTool.isAssUnitManaged(strCbaseid));
      if (bIsAssMana.booleanValue()) {
        if (strCassid == null || strCassid.trim().equals("")) {
          MessageDialog.showErrorDlg(this, m_lanResTool.getStrByID("SCMCOMMON","UPPSCMCommon-000059")/*@res "����"*/, m_lanResTool.getStrByID("40040301","UPP40040301-000190")/*@res "�и���������Ĵ���д��ڿո�������"*/);
          return false;
        }
        //���û�����
        UFDouble convert = PuTool.getInvConvRateValue(strCbaseid, strCassid);
        bm.setValueAt(convert, i, "convertrate");
        //����������ͬ��������Ϊ 1.0
        if (strCmain != null && strCmain.equals(strCassid)) {
          bm.setValueAt(new UFDouble(1.0), i, "convertrate");
        }
        //�ǹ̶������ʣ�������=������/������
        if (!PuTool.isFixedConvertRate(strCbaseid, strCassid)) {
          oNarrvnum = bm.getValueAt(i, "narrvnum");
          oNassinum = bm.getValueAt(i, "nassistnum");
          if (!(oNarrvnum == null || oNarrvnum.toString().trim().equals("")) && !(oNassinum == null || oNassinum.toString().trim().equals(""))) {
            ufdNarrvnum = new UFDouble(oNarrvnum.toString().trim());
            ufdNassinum = new UFDouble(oNassinum.toString().trim());
            oValue = ufdNassinum == new UFDouble(0) ? null : ufdNarrvnum.div(ufdNassinum);
          } else{
            oValue = null;
          }
          bm.setValueAt(oValue, i, "convertrate");
        }else
        	bm.setValueAt(new UFBoolean(true), i, "isfixedrate");
      } else {
    	bm.setValueAt(new UFBoolean(true), i, "isfixedrate");
        bm.setValueAt(null, i, "convertrate");
      }
      
      intValiddays = PuPubVO.getInteger_NullAs(getBillCardPanel().getBillModel().getValueAt(i, "ivalidday") ,new Integer(BillStatus.FREE));
      dvaliddate = (UFDate) getBillCardPanel().getBillModel().getValueAt(i, "dvaliddate");
      if(dvaliddate!=null){
        bm.setValueAt(dvaliddate.getDateBefore(intValiddays), i, "dproducedate");
      }

    }
	return true;
}

/**
 * ���ر�����Դ��Ϣ
 *
 */
private void loadSourceInfo() {
	if (getBillCardPanel().getBillModel().getItemByKey("csourcebillcode").isShow()) {
	    //������Դ��Ϣ
	    PuTool.loadSourceInfoAll(getBillCardPanel(),BillTypeConst.PO_ARRIVE);
    }
}

/**
 * @���ܣ���ѯ�۵��б����д����
 * @���ߣ���־ƽ
 * �������ڣ�(2001-6-7 17:25:38)
 */
private void loadDataToList() {

  if (getCacheVOs() != null) {
    getBillListPanel().getBodyBillModel().clearBodyData();
    ArriveorderHeaderVO[] headers = null;
    headers = getArriveHeaderVOs(getCacheVOs());
    getBillListPanel().setHeaderValueVO(headers);
    //��ʾ��ͷ��ע���˻�����
    for (int i = 0; i < headers.length; i++) {
      getBillListPanel().getHeadBillModel().setValueAt(headers[i].getVmemo(), i, "vmemo");
      getBillListPanel().getHeadBillModel().setValueAt(headers[i].getVbackreasonh(), i, "vbackreasonh");
    }
    getBillListPanel().getHeadBillModel().execLoadFormula();

    //ִ�����ε�����ʽ,�������ε�����Ϣ
    if ((null != ((ArriveorderVO) getCacheVOs()[getDispIndex()]).getBodyVo())
          && ((ArriveorderVO) getCacheVOs()[getDispIndex()]).getBodyVo().length > 0) {
        RcTool
            .execFormulaForBatchCode(((ArriveorderVO) getCacheVOs()[getDispIndex()])
                .getBodyVo());
        }

  } else {
    if (getBillListPanel().getHeadBillModel() != null) {
      getBillListPanel().getHeadBillModel().clearBodyData();
    }
    if (getBillListPanel().getBodyBillModel() != null) {
      getBillListPanel().getBodyBillModel().clearBodyData();
    }
  }
}

private void loadBatchDocInfo(boolean isList){
  String[] saBaseId = null;
  String[] saAssistUnit = null;
  String[] saMangId = null;

  if(isList){
    saBaseId = (String[]) PuGetUIValueTool.getArray(getBillListPanel().getBodyBillModel(), "cbaseid", String.class, 0, getBillListPanel().getBodyBillModel().getRowCount());
    saAssistUnit = (String[]) PuGetUIValueTool.getArray(getBillListPanel().getBodyBillModel(), "cassistunit", String.class, 0, getBillListPanel().getBodyBillModel().getRowCount());
    saMangId = (String[]) PuGetUIValueTool.getArrayNotNull(getBillListPanel().getBodyBillModel(), "cmangid", String.class, 0, getBillListPanel().getBodyBillModel().getRowCount());
  }else{
    saBaseId = (String[]) PuGetUIValueTool.getArray(getBillCardPanel().getBillModel(), "cbaseid", String.class, 0, getBillCardPanel().getRowCount());
    saAssistUnit = (String[]) PuGetUIValueTool.getArray(getBillCardPanel().getBillModel(), "cassistunit", String.class, 0, getBillCardPanel().getRowCount());
    saMangId = (String[]) PuGetUIValueTool.getArrayNotNull(getBillCardPanel().getBillModel(), "cmangid", String.class, 0, getBillCardPanel().getRowCount());
  }
    
  // �Ƿ����κŹ�����������
  PuTool.loadBatchProdNumMngt(saMangId);

  // ��������
  PuTool.loadBatchInvConvRateInfo(saBaseId, saAssistUnit);

  // ��������
  PuTool.loadBatchAssistManaged(saBaseId);

}

/**
 *  ��������:�б仯��༭����
   1�����ø��������գ�
   2�����û����ʣ�
   //3�������Ƿ�̶������ʣ�
   4�����Ƹ�����������Ϣ�ı༭״̬
 */
private void setAssisUnitEditState2(BillEditEvent event) {
  
  if(event.getRow()<0){
    return;
  }
  loadBatchDocInfo(false);
  //�Ƿ���и���������
  String strCbaseid = (String) getBillCardPanel().getBillModel().getValueAt(event.getRow(), "cbaseid");

  if (strCbaseid != null && !strCbaseid.trim().equals("") && nc.ui.pu.pub.PuTool.isAssUnitManaged(strCbaseid)) {
    // ���ø���������
    setRefPaneAssistunit(event.getRow());
    //���ÿɱ༭��
    getBillCardPanel().setCellEditable(event.getRow(), "convertrate", getBillCardPanel().getBodyItem("convertrate").isEdit());
    getBillCardPanel().setCellEditable(event.getRow(), "nassistnum", getBillCardPanel().getBodyItem("nassistnum").isEdit());
    getBillCardPanel().setCellEditable(event.getRow(), "cassistunitname", getBillCardPanel().getBodyItem("cassistunitname").isEdit());
    String cassistunit =
      (String) getBillCardPanel().getBillModel().getValueAt(
        event.getRow(),
        "cassistunit");
    //������Ϊ��,���������ɱ༭
    if (cassistunit == null || cassistunit.trim().equals("")) {
      getBillCardPanel().setCellEditable(event.getRow(), "cassistunitname", getBillCardPanel().getBodyItem("cassistunitname").isEdit());
      getBillCardPanel().setCellEditable(event.getRow(), "nassistnum", getBillCardPanel().getBodyItem("nassistnum").isEdit());
      getBillCardPanel().setCellEditable(event.getRow(), "nmoney", false);
      getBillCardPanel().setCellEditable(event.getRow(), "narrvnum", false);
      getBillCardPanel().setCellEditable(event.getRow(), "nelignum", false);
      getBillCardPanel().setCellEditable(event.getRow(), "npresentnum", false);
      getBillCardPanel().setCellEditable(event.getRow(), "nwastnum", false);
      getBillCardPanel().setCellEditable(event.getRow(), "convertrate", false);
    } else { //��������Ϊ��
      //���û�����
      UFDouble ufdConv =
        nc.ui.pu.pub.PuTool.getInvConvRateValue(strCbaseid, cassistunit);
      Object oTmp =
        getBillCardPanel().getBillModel().getValueAt(event.getRow(), "convertrate");
      if (oTmp == null || oTmp.toString().trim().equals("")) {
        getBillCardPanel().getBillModel().setValueAt(
          ufdConv,
          event.getRow(),
          "convertrate");
      }
      //���ÿɱ༭��
      getBillCardPanel().setCellEditable(event.getRow(), "cassistunitname", getBillCardPanel().getBodyItem("cassistunitname").isEdit());
      getBillCardPanel().setCellEditable(event.getRow(), "nassistnum", getBillCardPanel().getBodyItem("nassistnum").isEdit());
      getBillCardPanel().setCellEditable(event.getRow(), "nmoney", getBillCardPanel().getBodyItem("nmoney").isEdit());
      getBillCardPanel().setCellEditable(event.getRow(), "narrvnum", getBillCardPanel().getBodyItem("narrvnum").isEdit());
      //���������������
      /* delete 2003-10-22
      if (!bIsNegative) {
        getArrBillCardPanel().setCellEditable(event.getRow(), "nelignum", false);
      } else {
        getArrBillCardPanel().setCellEditable(event.getRow(), "nelignum", true);
      }
      */
      getBillCardPanel().setCellEditable(event.getRow(), "npresentnum", getBillCardPanel().getBodyItem("npresentnum").isEdit());
      getBillCardPanel().setCellEditable(event.getRow(), "nwastnum", getBillCardPanel().getBodyItem("nwastnum").isEdit());
      getBillCardPanel().setCellEditable(event.getRow(), "convertrate", getBillCardPanel().getBodyItem("convertrate").isEdit());
      //����������ǹ̶�������
      if (nc.ui.pu.pub.PuTool.isFixedConvertRate(strCbaseid, cassistunit)) {
        getBillCardPanel().setCellEditable(event.getRow(), "convertrate", false);
      }
      //���������������ͬ,�����ʲ��ɱ༭
      String ass =
        (String) getBillCardPanel().getBillModel().getValueAt(
          event.getRow(),
          "cassistunitname");
      String main =
        (String) getBillCardPanel().getBillModel().getValueAt(
          event.getRow(),
          "cmainmeasname");
      if (ass != null && ass.equals(main)) {
        getBillCardPanel().getBillModel().setValueAt(
          new UFDouble(1),
          event.getRow(),
          "convertrate");
        getBillCardPanel().setCellEditable(event.getRow(), "convertrate", false);

      }
    }
  } else {
    //û�и���������ʱ������ϢΪ��(ģ���в��ָ���ϢҪ����ʱ�����û����ɼ�)
    getBillCardPanel().setCellEditable(event.getRow(), "convertrate", false);
    getBillCardPanel().setCellEditable(event.getRow(), "nassistnum", false);
    getBillCardPanel().setCellEditable(event.getRow(), "cassistunitname", false);
    getBillCardPanel().getBillModel().setValueAt(
      null,
      event.getRow(),
      "convertrate");
    getBillCardPanel().getBillModel().setValueAt(
      null,
      event.getRow(),
      "nassistnum");
    getBillCardPanel().getBillModel().setValueAt(
      null,
      event.getRow(),
      "cassistunitname");
    getBillCardPanel().getBillModel().setValueAt(
      null,
      event.getRow(),
      "nassistnum");
    getBillCardPanel().getBillModel().setValueAt(
      null,
      event.getRow(),
      "cassistunit");
  }
}

/**
 * ��������:�����˻������Ƿ�ɱ༭
 */
private void setBackReasonEditable() {
  if (isBackBill()) {
    if (getBillCardPanel().getHeadItem("vbackreasonh") != null) {
      getBillCardPanel().getHeadItem("vbackreasonh").setEnabled(getBillCardPanel().getHeadItem("vbackreasonh").isEdit());
      getBillCardPanel().getHeadItem("vbackreasonh").setEdit(getBillCardPanel().getHeadItem("vbackreasonh").isEdit());
    }
    if (getBillCardPanel().getBodyItem("vbackreasonb") != null) {
      getBillCardPanel().getBodyItem("vbackreasonb").setEnabled(getBillCardPanel().getBodyItem("vbackreasonb").isEdit());
      getBillCardPanel().getBodyItem("vbackreasonb").setEdit(getBillCardPanel().getBodyItem("vbackreasonb").isEdit());
    }
  } else {
    if (getBillCardPanel().getHeadItem("vbackreasonh") != null)
      getBillCardPanel().getHeadItem("vbackreasonh").setEnabled(false);
    if (getBillCardPanel().getBodyItem("vbackreasonb") != null) {
      getBillCardPanel().getBodyItem("vbackreasonb").setEnabled(false);
      getBillCardPanel().getBodyItem("vbackreasonb").setEdit(false);
    }
  }
}

/**
 * �����в����Ƿ����
 */
private void setBtnLines(boolean isEnable) {

  m_btnLines.setEnabled(isEnable);
  int iLne = m_btnLines.getChildCount();
  for (int i = 0; i < iLne; i++) {
    ((ButtonObject)m_btnLines.getChildren().elementAt(i)).setEnabled(isEnable);
    updateButton((ButtonObject)m_btnLines.getChildren().elementAt(i));
  }
  UIMenuItem[] menuitems = getBillCardPanel().getBodyMenuItems();
  if (menuitems != null && menuitems.length > 0) {
    for (int i = 0; i < menuitems.length; i++) {
      menuitems[i].setEnabled(isEnable);
    }
  }
  //since v502
  rightButtonRightControl();
}

/**
 * @���ܣ����õ����б�״̬�µİ�ť
 * @���ߣ���־ƽ
 * �������ڣ�(2001-6-20 7:58:28)
 */
private void setButtonsList() {
 
  //�б�����
  m_btnCard.setName(m_lanResTool.getStrByID("SCMCOMMON", "UPPSCMCommon-000463")/*
       * @res
       * "��Ƭ��ʾ"
       */);
  
  /*����ת��*/
  m_btnEndCreate.setEnabled(false);
  m_btnEndCreate.setVisible(false);
  //ҵ������
  m_btnBusiTypes.setEnabled(false);
  m_btnAdds.setEnabled(false);
  m_btnSave.setEnabled(false);
  m_btnBacks.setEnabled(false);
  m_btnCancel.setEnabled(false);
  m_btnLines.setEnabled(false);
  m_btnRefresh.setEnabled(m_bQueriedFlag);
  m_btnLocate.setEnabled(false);
  m_btnFirst.setEnabled(false);
  m_btnPrev.setEnabled(false);
  m_btnNext.setEnabled(false);
  m_btnLast.setEnabled(false);
  m_btnCombin.setEnabled(false);
  m_btnCheck.setEnabled(false);
  m_btnQueryForAudit.setEnabled(false);
  m_btnSendAudit.setEnabled(false);
  m_btnLookSrcBill.setEnabled(false);
  m_btnQuickReceive.setEnabled(false);
  
  m_btnMaintains.setEnabled(true);
  m_btnBrowses.setEnabled(true);
  m_btnPrints.setEnabled(true);
    
  /*������ѯ�����׼���ѯ���ĵ�����Ԥ������ӡ���޸ġ����ϡ�����������ȫѡ��ȫ������Ƭ��ʾ/�б���ʾ*/
  
  int iDataCnt = getCacheVOs() == null ? 0 : getCacheVOs().length;
  int iSeltCnt = getBillListPanel().getHeadTable().getSelectedRowCount();
  
  //����������
  if(iDataCnt == 0){
    m_btnUsable.setEnabled(false);
    m_btnQueryBOM.setEnabled(false);
    m_btnDocument.setEnabled(false);
    m_btnPrint.setEnabled(false);
    m_btnSplitPrint.setEnabled(false);
    m_btnPrintPreview.setEnabled(false);
    m_btnModify.setEnabled(false);
    m_btnDiscard.setEnabled(false);
    m_btnAudit.setEnabled(false);
    m_btnUnAudit.setEnabled(false);
    m_btnSelectAll.setEnabled(false);
    m_btnSelectNo.setEnabled(false);
    m_btnCard.setEnabled(true);
    //
    updateButtonsAll();
    return;
  }
  //δѡ������
  if(iSeltCnt == 0){
    m_btnUsable.setEnabled(false);
    m_btnQueryBOM.setEnabled(false);
    m_btnDocument.setEnabled(false);
    m_btnPrint.setEnabled(false);
    m_btnSplitPrint.setEnabled(false);
    m_btnPrintPreview.setEnabled(false);
    m_btnModify.setEnabled(false);
    m_btnDiscard.setEnabled(false);
    m_btnAudit.setEnabled(false);
    m_btnUnAudit.setEnabled(false);
    m_btnSelectAll.setEnabled(false);
    m_btnSelectNo.setEnabled(false);
    m_btnCard.setEnabled(false);
    //
    updateButtonsAll();
    return;
  }
  
  /*������ѯ�����׼���ѯ���ĵ�����Ԥ������ӡ���޸ġ����ϡ�����������ȫѡ��ȫ������Ƭ��ʾ/�б���ʾ*/
  boolean bOnlyOneSelected = (iSeltCnt == 1);
  boolean bAllSelected = (iSeltCnt == iDataCnt);
  
  //
  m_btnUsable.setEnabled(bOnlyOneSelected);
  m_btnQueryBOM.setEnabled(bOnlyOneSelected);
  m_btnCard.setEnabled(bOnlyOneSelected);
  if(bOnlyOneSelected){
    m_btnModify.setEnabled(getCacheVOs()[getDispIndex()].isCanBeModified());
    m_btnDiscard.setEnabled(getCacheVOs()[getDispIndex()].isCanBeDiscarded());
    m_btnAudit.setEnabled(getCacheVOs()[getDispIndex()].isCanAudit());
    m_btnUnAudit.setEnabled(getCacheVOs()[getDispIndex()].isCanUnAudit());
  }else{
    m_btnModify.setEnabled(false);
    m_btnDiscard.setEnabled(true);
    m_btnAudit.setEnabled(true);
    m_btnUnAudit.setEnabled(true);
    
    ArrayList a = getSelectedBills();
    for (int i = 0; i < a.size(); i++) {
      ArriveorderVO aa = (ArriveorderVO) a.get(i);
      if (aa != null && aa.getHeadVO() != null) {
        if (3 == aa.getHeadVO().getIbillstatus().intValue()) {
          m_btnAudit.setEnabled(false);
          break;
        }else if (0 == aa.getHeadVO().getIbillstatus().intValue()) {
          m_btnUnAudit.setEnabled(false);
          break;
        }
      }
    }
  }
  m_btnDocument.setEnabled(true);
  m_btnPrint.setEnabled(true);
  m_btnSplitPrint.setEnabled(true);
  m_btnPrintPreview.setEnabled(true);
  m_btnSelectNo.setEnabled(true);
  m_btnSelectAll.setEnabled(!bAllSelected);
  
  m_btnCreateCard.setEnabled(false);
  m_btnDeleteCard.setEnabled(false);

  //
  updateButtonsAll();

}

/**
 * @���ܣ��������ɵĵ������б�(δ����ʱ)��ť�߼�
 */
private void setButtonsListNew() {
  //
  for (int i = 0; i < m_aryArrListButtons.length; i++) {
    m_aryArrListButtons[i].setEnabled(false);
  }
  m_btnCancel.setEnabled(false);
  /*ֻ�С��л�����������ת������ť����*/
  m_btnModifyList.setEnabled(true);
  m_btnEndCreate.setVisible(true);
  m_btnEndCreate.setEnabled(true);
  //
  updateButtonsAll();
}
/**
 * ��Ϣ���İ�ť�߼�
 *
 */
private void setButtonsMsgCenter(){

  //����
  m_btnAudit.setEnabled(true);
  updateButton(m_btnAudit);
  //����
  m_btnUnAudit.setEnabled(true);  
  updateButton(m_btnUnAudit);
  //״̬��ѯ
  m_btnQueryForAudit.setEnabled(true);
  updateButton(m_btnQueryForAudit);
  //�ĵ�����
  m_btnDocument.setEnabled(true);
  updateButton(m_btnDocument);
  //������
  m_btnLookSrcBill.setEnabled(true);
  updateButton(m_btnLookSrcBill);
}

/**
 * @���ܣ��������״̬�°�ť����
 */
private void setButtonsCard() {
  //
  setButtonsInit();
  
  if (getCacheVOs() != null && getCacheVOs().length >= 1) {
    //��ӡ����λ��ˢ�¡��޸ġ����ϡ�����
    m_btnPrint.setEnabled(true);
    m_btnSplitPrint.setEnabled(true);
    m_btnCombin.setEnabled(true);
    m_btnPrintPreview.setEnabled(true);
    m_btnLocate.setEnabled(true);
    m_btnRefresh.setEnabled(m_bQueriedFlag);
    m_btnRefreshList.setEnabled(m_bQueriedFlag);
    //�޸�
    if (getCacheVOs()[getDispIndex()].isCanBeModified()) {
      m_btnModify.setEnabled(true);
    } else {
      m_btnModify.setEnabled(false);
    }
    //����
    if (getCacheVOs()[getDispIndex()].isCanBeDiscarded()) {
      if (getCacheVOs()[getDispIndex()].isHaveCheckLine()) {
        m_btnDiscard.setEnabled(false);
      } else {
        m_btnDiscard.setEnabled(true);
      }
    } else {
      m_btnDiscard.setEnabled(false);
    }
    //����
    m_btnOthers.setEnabled(true);
    //����ά��
    m_btnMaintains.setEnabled(true);
    //���
    m_btnBrowses.setEnabled(true);
    //������ѯ
    m_btnUsable.setEnabled(true);
    //���׼�
    m_btnQueryBOM.setEnabled(true);
    //�в���
    setBtnLines(false);
    //�ĵ�����
    m_btnDocument.setEnabled(true);
    //״̬��ѯ
    m_btnQueryForAudit.setEnabled(true);
    //������
    m_btnLookSrcBill.setEnabled(true);
    //�����ջ�
    m_btnQuickReceive.setEnabled(true);
    //������ĩ���߼�
    if (getCacheVOs().length == 1) {
      m_btnFirst.setEnabled(false);
      m_btnPrev.setEnabled(false);
      m_btnNext.setEnabled(false);
      m_btnLast.setEnabled(false);
    } else if (getDispIndex() != getCacheVOs().length - 1 && getDispIndex() != 0) {
      m_btnFirst.setEnabled(true);
      m_btnPrev.setEnabled(true);
      m_btnNext.setEnabled(true);
      m_btnLast.setEnabled(true);
    } else if (getDispIndex() == 0) {
      m_btnFirst.setEnabled(false);
      m_btnPrev.setEnabled(false);
      m_btnNext.setEnabled(true);
      m_btnLast.setEnabled(true);
    } else {
      m_btnFirst.setEnabled(true);
      m_btnPrev.setEnabled(true);
      m_btnNext.setEnabled(false);
      m_btnLast.setEnabled(false);
    }
    //����ť
    m_btnSendAudit.setEnabled(isNeedSendAudit(getCacheVOs()[getDispIndex()]));
    //
    m_btnAudit.setEnabled(getCacheVOs()[getDispIndex()].isCanAudit());
    m_btnUnAudit.setEnabled(getCacheVOs()[getDispIndex()].isCanUnAudit());
    
    //��֧����������δ�����������Ϊά��û��¼��ϸ񡢲��ϸ������Ĺ���
    m_btnCheck.setEnabled(m_bQcEnabled);
    
  }
  //��ѯˢ��
  m_btnQuery.setEnabled(true);
  m_btnRefresh.setEnabled(m_bQueriedFlag);
  //
  updateButtonsAll();
}

/**
 * @���ܣ��޸İ�ť�߼�
 * @���ߣ���־ƽ
 * �������ڣ�(2001-6-20 13:39:39)
 * @param:<|>
 * @return:
 * @exception:
 * @see;
 * @since:
 *
 */
private void setButtonsModify() {
  //
  m_btnEndCreate.setVisible(false);
  //
  for (int i = 0; i < m_aryArrCardButtons.length; i++) {
    if(PuTool.isExist(getExtendBtns(),m_aryArrCardButtons[i])){
      continue;
    }
    m_aryArrCardButtons[i].setEnabled(false);
  }
  int iLen = m_btnOthers.getChildCount();
  for (int i = 0; i < iLen; i++) {
    ((ButtonObject)m_btnOthers.getChildren().elementAt(i)).setEnabled(false);
  }
  iLen = m_btnBacks.getChildCount();
  for (int i = 0; i < iLen; i++) {
    ((ButtonObject)m_btnBacks.getChildren().elementAt(i)).setEnabled(false);
  }
  //����
  m_btnOthers.setEnabled(true);
  //�����ջ�
  m_btnQuickReceive.setEnabled(false);
  //������ѯ
  m_btnUsable.setEnabled(true);
  //���׼�
  m_btnQueryBOM.setEnabled(true);
  m_btnRefresh.setEnabled(false);
  m_btnLocate.setEnabled(false);
  m_btnDocument.setEnabled(false);
  m_btnLookSrcBill.setEnabled(false);

  //����ά��
  m_btnMaintains.setEnabled(true);
  m_btnSave.setEnabled(true);
  m_btnCancel.setEnabled(true);
  m_btnModify.setEnabled(false);
  m_btnDiscard.setEnabled(false);
  //���
  m_btnBrowses.setEnabled(true);
  m_btnQuery.setEnabled(false);
  m_btnFirst.setEnabled(false);
  m_btnPrev.setEnabled(false);
  m_btnNext.setEnabled(false);
  m_btnLast.setEnabled(false);
  m_btnUnAudit.setEnabled(false);

  for (int iRow = 0; iRow < getBillCardPanel().getBillModel().getRowCount();iRow++) {
    for (int i = 0; i < RcTool.sTargetFields.length;i++) {
      getBillCardPanel().setCellEditable(iRow, RcTool.sTargetFields[i], false);//�ɹ�������Ŀǰ���ṩ���������ε�������Ϣ�ڵ������Ͻ����޸ĵĹ��� V502
    }
  }

  //ִ�а�ť��
  m_btnActions.setEnabled(true);
  //����ť
  m_btnSendAudit.setEnabled(isNeedSendAudit(getCacheVOs()[getDispIndex()]));
  
  setBtnLines(true);

  /** �����Ҽ��˵��밴ť�顰�в�����Ȩ����ͬ */
  setPopMenuBtnsEnable();
  //
  updateButtonsAll();
}

/**
 * @���ܣ��������ɵĵ�������Ƭ���水ť�߼�
 * @���ߣ���־ƽ
 * �������ڣ�(2001-7-31 18:42:07)
 * @param:<|>
 * @return:
 * @exception:
 * @see;
 * @since:
 *
 */
private void setButtonsModifyNew() {

  setButtonsModify();

  m_btnList.setEnabled(false);
  updateButton(m_btnList);
  
}
/**
 * ���ð�ť״̬
 * �������ڣ�(2001-3-17 9:00:09)
 */
private void setButtonsState() {
  
  int iVal = -999;//֧�ֲ�ҵ��������չ

  if (getStateStr().equals("��ʼ��")) {
    setButtonsInit();
    iVal = 0;
  }
  else if (getStateStr().equals("�������")) {
    setButtonsCard();
    iVal = 1;
  }
  else if (getStateStr().equals("�����޸�")) {
    setButtonsModify();
    iVal = 2;
  }
  else if (getStateStr().equals("�����б�")) {
    setButtonsList();
    iVal = 3;
  }
  else if (getStateStr().equals("ת���б�")) {
    setButtonsListNew();
    iVal = 4;
  }
  else if (getStateStr().equals("ת���޸�")) {
    setButtonsModifyNew();
    iVal = 5;
  }
  else if (getStateStr().equals("��Ϣ����")) {
    setButtonsMsgCenter();
    iVal = 6;
  }
  for (int i = 0; i < getBillCardPanel().getBillModel().getRowCount(); i++) {
    getBillCardPanel().setCellEditable(i, "creporterid", false);// ������ID
    getBillCardPanel().setCellEditable(i, "creportername", false);// ����������
    getBillCardPanel().setCellEditable(i, "dreportdate", false);// ��������
  }
  setBtnStatusSN();
  setExtendBtnsStat(iVal);
}

/**
 * ��������:�ı���水ť״̬
 */
private void updateButtonsAll() {
  int iLen = getBtnTree().getButtonArray().length;
  for (int i = 0; i < iLen; i++) {
    update(getBtnTree().getButtonArray()[i]);
  }
}

/**
 * �������ڣ� 2005-9-20 ���������� ���°�ť״̬���ݹ鷽ʽ��
 */
private void update(ButtonObject bo) {
  updateButton(bo);
  if (bo.getChildCount() > 0) {
    for (int i = 0, len = bo.getChildCount(); i < len; i++)
      update(bo.getChildButtonGroup()[i]);
  }
}
/**
 * ��ȡ��ť������Ψһʵ����
 * <p>
 * <b>examples:</b>
 * <p>
 * ʹ��ʾ��
 * <p>
 * <b>����˵��</b>
 * @return
 * <p>
 * @author czp
 * @time 2007-3-13 ����01:16:48
 */
protected ButtonTree getBtnTree(){//��ΪgetExtendBtns()����������ڲ����ˣ�Ϊ���ο����ſ�Ϊprotected
  if(m_btnTree == null){
    try {
      m_btnTree = new ButtonTree("40040301"); 
    }
    catch (BusinessException be) {
      showHintMessage(be.getMessage());
      return null;
    }
  }
  return m_btnTree;
}
/**
 * ���ܣ��ڱ༭����������������:
   1.���Ϊ�ջ��ߴ���0���ϸ����������ϸ��������ɱ༭����Ʒ������;�����������ͬ����
   2.���С��0���ϸ������ɱ༭����Ʒ������;�����������ϸ񡢲��ϸ�ͬ����
 * @param e nc.ui.pub.bill.BillEditEvent
 */
private void setEditAndDirect(BillEditEvent e) {
  boolean bIsNegative = false;
  if (getBillCardPanel().getBodyValueAt(e.getRow(), "narrvnum") == null
    || getBillCardPanel().getBodyValueAt(e.getRow(), "narrvnum").toString().equals(
      "")
    || (new UFDouble(getBillCardPanel()
      .getBodyValueAt(e.getRow(), "narrvnum")
      .toString()))
      .doubleValue()
      >= 0.0) {
    bIsNegative = false;
  } else {
    bIsNegative = true;
  }
  //#������
  if (!bIsNegative) {
    //�ϸ��������ɱ༭
    /* delete 2003-10-22
    getArrBillCardPanel().setCellEditable(e.getRow(), "nelignum", false);
    */
    //�ϸ������뵽������ͬ��
    ((UIRefPane) getBillCardPanel().getBodyItem("nelignum").getComponent())
    .getUITextField()
    .setMaxValue(nc.vo.scm.field.pu.FieldMinMaxValue.MAX_NUM);
    ((UIRefPane) getBillCardPanel().getBodyItem("nelignum").getComponent())
    .getUITextField()
    .setMinValue(0.0);

    //��Ʒͬ��
    ((UIRefPane) getBillCardPanel().getBodyItem("npresentnum").getComponent())
    .getUITextField()
    .setMinValue(0.0);

    ((UIRefPane) getBillCardPanel().getBodyItem("npresentnum").getComponent())
    .getUITextField()
    .setMaxValue(nc.vo.scm.field.pu.FieldMinMaxValue.MAX_NUM);
    ////;��ͬ��
    ((UIRefPane) getBillCardPanel().getBodyItem("nwastnum").getComponent())
    .getUITextField()
    .setMinValue(0.0);

    ((UIRefPane) getBillCardPanel().getBodyItem("nwastnum").getComponent())
    .getUITextField()
    .setMaxValue(nc.vo.scm.field.pu.FieldMinMaxValue.MAX_NUM);
    //���ͬ��
    ((UIRefPane) getBillCardPanel().getBodyItem("nmoney").getComponent())
    .getUITextField()
    .setMinValue(0.0);

    ((UIRefPane) getBillCardPanel().getBodyItem("nmoney").getComponent())
    .getUITextField()
    .setMaxValue(nc.vo.scm.field.pu.FieldMinMaxValue.MAX_NUM);
  }
  //#������
  else {
    //�ϸ������ɱ༭
    /* delete 2003-10-22
    getArrBillCardPanel().setCellEditable(e.getRow(), "nelignum", true);
    */
    //�ϸ������뵽������ͬ��
    ((UIRefPane) getBillCardPanel().getBodyItem("nelignum").getComponent())
    .getUITextField()
    .setMaxValue(0.0);
    ((UIRefPane) getBillCardPanel().getBodyItem("nelignum").getComponent())
    .getUITextField()
    .setMinValue(nc.vo.scm.field.pu.FieldMinMaxValue.MIN_NUM);
    //��Ʒͬ��
    ((UIRefPane) getBillCardPanel().getBodyItem("npresentnum").getComponent())
    .getUITextField()
    .setMaxValue(0.0);
    ((UIRefPane) getBillCardPanel().getBodyItem("npresentnum").getComponent())
    .getUITextField()
    .setMinValue(nc.vo.scm.field.pu.FieldMinMaxValue.MIN_NUM);
    //;��ͬ��
    ((UIRefPane) getBillCardPanel().getBodyItem("nwastnum").getComponent())
      .getUITextField()
      .setMaxValue(0.0);
    ((UIRefPane) getBillCardPanel().getBodyItem("nwastnum").getComponent())
      .getUITextField()
      .setMinValue(nc.vo.scm.field.pu.FieldMinMaxValue.MIN_NUM);
    //���ͬ��
    ((UIRefPane) getBillCardPanel().getBodyItem("nmoney").getComponent())
    .getUITextField()
    .setMaxValue(0.0);
    ((UIRefPane) getBillCardPanel().getBodyItem("nmoney").getComponent())
    .getUITextField()
    .setMinValue(nc.vo.scm.field.pu.FieldMinMaxValue.MIN_NUM);
  }
}

/**
 * @���ܣ��������б�ͷ�任ʱ��������д����Ӧ�ӱ�����
 * @���ߣ���־ƽ
 * �������ڣ�(2001-6-8 16:41:42)
 * �޸ģ�Ϊ���Ч�ʣ�Ҫ���Ӷ�ָ����ͷ���ر���Ĳ��� 0530
 * @param row0 int
 */
private boolean setListBodyData(int row0) {
  boolean isErr = false;
  if (!getStateStr().equals("ת���б�")) {
    items = null;
    try {
      getCacheVOs()[row0] = RcTool.getRefreshedVO(getCacheVOs()[row0]);
      if (getCacheVOs()[row0] != null
        && getCacheVOs()[row0].getChildrenVO() != null
        && getCacheVOs()[row0].getChildrenVO().length > 0) {
        items = (ArriveorderItemVO[]) getCacheVOs()[row0].getChildrenVO();
      }
    } catch (Exception be) {
      getBillListPanel().getBodyBillModel().clearBodyData();
      if (be instanceof BusinessException) {
        MessageDialog.showErrorDlg(this, m_lanResTool.getStrByID("SCMCOMMON","UPPSCMCommon-000422")/*@res "ҵ���쳣"*/, be.getMessage());
      }
      return true;
    }
    getBillListPanel().setBodyValueVO(items);
  } else {
    getBillListPanel().setBodyValueVO(getCacheVOs()[getDispIndex()].getChildrenVO());
  }
  getBillListPanel().getBodyBillModel().execLoadFormula();
  BillModel bm = getBillListPanel().getBodyBillModel();
  //���д��� ------------------------------------------------ ��ʼ
  String strCbaseid = null;
  String strCmain = null;
  String strCassid = null;
  Object oNarrvnum = null;
  Object oNassinum = null;
  UFDouble ufdNarrvnum = null;
  UFDouble ufdNassinum = null;
  Object oValue = null;
  
  
  UFDate dvaliddate = null;
  int intValiddays = 0;

  for (int i = 0; i < bm.getRowCount(); i++) {
    //���屸ע��ʼ��
    if (bm.getValueAt(i, "vmemo") == null) {
      bm.setValueAt("", i, "vmemo");
    }
    strCbaseid = (String) bm.getValueAt(i, "cbaseid");
    //strCmangid = (String) bm.getValueAt(i, "cmangid");
    strCassid = (String) bm.getValueAt(i, "cassistunit");
    strCmain = (String) bm.getValueAt(i, "cmainmeasid");
    //�Ƿ񸨼�������
    UFBoolean bIsAssMana = new UFBoolean(PuTool.isAssUnitManaged(strCbaseid));
    if (bIsAssMana.booleanValue()) {
      if (strCassid == null || strCassid.trim().equals("")) {
        MessageDialog.showErrorDlg(this, m_lanResTool.getStrByID("SCMCOMMON","UPPSCMCommon-000059")/*@res "����"*/, m_lanResTool.getStrByID("40040301","UPP40040301-000190")/*@res "�и���������Ĵ���д��ڿո�������"*/);
        return true;
      }
      //���û�����
      UFDouble convert = PuTool.getInvConvRateValue(strCbaseid, strCassid);
      bm.setValueAt(convert, i, "convertrate");
      //�������������ͬ
      if (strCmain != null && strCmain.equals(strCassid)) {
        bm.setValueAt(new UFDouble(1.0), i, "convertrate");
      }
      //������ǹ̶������ʣ�����������/������ȡ�û�����
      if (!PuTool.isFixedConvertRate(strCbaseid, strCassid)) {
        oNarrvnum = bm.getValueAt(i, "narrvnum");
        oNassinum = bm.getValueAt(i, "nassistnum");
        if (!(oNarrvnum == null || oNarrvnum.toString().trim().equals(""))
          && !(oNassinum == null || oNassinum.toString().trim().equals(""))) {
          ufdNarrvnum = new UFDouble(oNarrvnum.toString().trim());
          ufdNassinum = new UFDouble(oNassinum.toString().trim());
          oValue = ufdNassinum == new UFDouble(0) ? null : ufdNarrvnum.div(ufdNassinum);
        } else
          oValue = null;
        bm.setValueAt(oValue, i, "convertrate");
      }
    } else {
      bm.setValueAt(null, i, "convertrate");
      bm.setValueAt(null, i, "isfixedrate");
    }
    // �Ƿ�̶�������
    if(PuTool.isFixedConvertRate(strCbaseid, strCassid)){
      bm.setValueAt(new UFBoolean(true), i, "isfixedrate");
    }
    
    intValiddays = PuPubVO.getInteger_NullAs(getBillCardPanel().getBillModel().getValueAt(i, "ivalidday") ,new Integer(BillStatus.FREE));
    dvaliddate = (UFDate) getBillCardPanel().getBillModel().getValueAt(i, "dvaliddate");
    if(dvaliddate!=null){
      bm.setValueAt(dvaliddate.getDateBefore(intValiddays), i, "dproducedate");
    }

  }
  checkVprocessbatch(getCacheVOs());
  //���д��� -------------------------------------------------- ����

  //������Դ��Ϣ��Դͷ��Ϣ
  PuTool.loadSourceInfoAll(getBillListPanel(),BillTypeConst.PO_ARRIVE);
  //add by QuSida 2010-9-11 (��ɽ����)  --- begin
  //function:��ѯ��ط�����Ϣ
  String pk = (String)getCacheVOs()[row0].getHeadVO().getAttributeValue("carriveorderid");
	String sql = "cbillid = '"+pk+"' and dr = 0";
	InformationCostVO[] vos = null;

	 try {
		vos = (InformationCostVO[])JJPuScmPubHelper.querySmartVOs(InformationCostVO.class, null, sql);
	} catch (Exception e) {
		// TODO Auto-generated catch block
		SCMEnv.out(e);
	}
if(vos!=null&&vos.length!=0){
	
	 getBillListPanel().getBodyBillModel("jj_scm_informationcost").setBodyDataVO(vos);
	 getBillListPanel().getBodyBillModel().execLoadFormula();//20101010-Meichao ִ�б������м��ع�ʽ
//	 getBillListPanel().getBodyBillModel("jj_scm_informationcost").execLoadFormula();
}else{
	//20101010 2204 Meichao������else�ж�: ��������ϢΪ��ʱ.���֮ǰ�ķ���ҳǩ����.
	getBillListPanel().getBodyBillModel("jj_scm_informationcost").setBodyDataVO(null);
}
//add by QuSida 2010-9-11 (��ɽ����)  --- end
  return isErr;
}

/**
 * ���ܣ��б���ʱ�����������б�ť�߼�����
 * ���ߣ���־ƽ
 * ���ڣ�(2003-2-24 17:02:24)
 */
private void setButtonsListWhenErr() {
  //
  for (int i = 0; i < m_aryArrListButtons.length; i++) {
    m_aryArrListButtons[i].setEnabled(false);
  }
  //
  m_btnQuery.setEnabled(true);
  m_btnBrowses.setEnabled(true);
  m_btnRefresh.setEnabled(true);
  //
  updateButtonsAll();
}

/**
 * @���ܣ����õ�����������
 * @���ߣ���־ƽ
 * �������ڣ�(2001-6-19 20:13:12)
 * @param:<|>
 * @return:
 * @exception:
 * @see;
 * @since:
 *
 * @param newM_arriveVOs nc.vo.rc.receive.ArriveorderVO[]
 */
public void setCacheVOs(nc.vo.rc.receive.ArriveorderVO[] newM_arriveVOs) {
  m_arriveVOs = newM_arriveVOs;
}

/**
 * @���ܣ����õ�ǰ��ʾ������
 * @���ߣ���־ƽ
 * �������ڣ�(2001-6-20 8:47:47)
 * @param:<|>
 * @return:
 * @exception:
 * @see;
 * @since:
 *
 * @param newM_iArrCurrRow int
 */
private void setDispIndex(int newM_iArrCurrRow) {
  m_iArrCurrRow = newM_iArrCurrRow;
}
/**
 * @���ܣ����õ�ǰ����ά��״̬
 * @���ߣ���־ƽ
 * �������ڣ�(2001-6-19 20:18:22)
 * @param:<|>
 * @return:
 * @exception:
 * @see;
 * @since:
 *
 * @param newM_strState java.lang.String
 */
private void setM_strState(java.lang.String newM_strState) {
  m_strState = newM_strState;
}


/**
 * ��������:������ֵ�����ֶ�ȡֵ��Χ
 *      /
 *      | false: ����ȡֵ
 * isBack = |
 *      | true : ��
 *      \
 */
private void setNumFieldsNeg(boolean isBack) {
  double iMin = nc.vo.scm.field.pu.FieldMinMaxValue.MIN_NUM;
  double iMax = nc.vo.scm.field.pu.FieldMinMaxValue.MAX_NUM;
  if (isBack) {
    iMax = 0;
  }
  //�����ջ�
  UIRefPane refNarrvnum = (UIRefPane) getBillCardPanel().getBodyItem("narrvnum").getComponent();
  refNarrvnum.setMinValue(iMin);
  refNarrvnum.setMaxValue(iMax);
  //������
  UIRefPane refNassistnum = (UIRefPane) getBillCardPanel().getBodyItem("nassistnum").getComponent();
  refNassistnum.setMinValue(iMin);
  refNassistnum.setMaxValue(iMax);
  refNarrvnum.setMinValue(iMin);
  refNarrvnum.setMaxValue(iMax);
  refNarrvnum.setMinValue(iMin);
  refNarrvnum.setMaxValue(iMax);
  refNarrvnum.setMinValue(iMin);
  refNarrvnum.setMaxValue(iMax);
}

/**
 * ���ܣ�����ת��ʱ�л����޸ġ����ϡ�ȫѡ��ȫ�����ĵ�����ť��ʾ�߼�����ǰ���ݶ�λ
 * ��ť�߼�
 *   @�л�������ֻ��һ��ѡ��ʱ��Ч
 *   @�޸ģ���Ч
 *   @��ӡ����Ч
 *   @���ϣ���Ч
 *   @ȫѡ����Ч
 *   @ȫ������Ч
 *   @�ĵ�������Ч
 *   @������ѯ����Ч
 *   @����ת������Ч
 */
private void setButtonsListValueChangedNew(int cnt) {
  for (int i = 0; i < m_aryArrListButtons.length; i++) {
    m_aryArrListButtons[i].setEnabled(false);
  }
  m_btnMaintains.setEnabled(true);
  //
  m_btnDiscard.setEnabled(false);
  //
  int iLen = m_btnOthers.getChildCount();
  for (int i = 0; i < iLen; i++) {
    ((ButtonObject)m_btnOthers.getChildren().elementAt(i)).setEnabled(false);
  }
  m_btnOthersList.setEnabled(true);
  /*������ת��������*/
  m_btnEndCreate.setVisible(true);
  m_btnEndCreate.setEnabled(true);
  m_btnUsableList.setEnabled(true);
  m_btnQueryBOMList.setEnabled(true);
  if (cnt == 1) {
    m_btnModifyList.setEnabled(true);
  }
  //
  updateButtonsAll();
}

/**
 * @���ܣ����ÿ����֯��ֿ�ƥ��
 */
private void setOrgWarhouse() {
  UIRefPane ref =
    (UIRefPane) getBillCardPanel()
      .getHeadItem("cstoreorganization")
      .getComponent();
  String sPkCalBody = ref.getRefPK();
  PuTool.restrictWarehouseRefByStoreOrg(
    getBillCardPanel(),
    getCorpPrimaryKey(),
    sPkCalBody,
    "cwarehousename");
}

private void setRefPaneAssistunit(int row) {
  //�������ID��������ID
  Object cbaseid = getBillCardPanel().getBillModel().getValueAt(row, "cbaseid");
  //���ø�������λ����
  UIRefPane ref =
    (UIRefPane) getBillCardPanel().getBodyItem("cassistunitname").getComponent();
  String wherePart = "bd_convert.pk_invbasdoc='" + cbaseid + "' ";
  ref.setWhereString(wherePart);
  String unionPart = " union all \n";
  unionPart
    += "(select bd_measdoc.shortname,bd_measdoc.measname,bd_invbasdoc.pk_measdoc \n";
  unionPart += "from bd_invbasdoc \n";
  unionPart += "left join bd_measdoc  \n";
  unionPart += "on bd_invbasdoc.pk_measdoc=bd_measdoc.pk_measdoc \n";
  unionPart += "where bd_invbasdoc.pk_invbasdoc='" + cbaseid + "') \n";
  ref.getRefModel().setGroupPart(unionPart);
}
/**
 * ������ť
 * �������ڣ�(2001-3-17 9:00:09)
 */
private void updateButtonsMy() {
  if (getStateStr().equals("�������б�"))
    setButtons(m_aryArrListButtons);
  else
    for (int i = 0; i < m_aryArrCardButtons.length; i++){
      updateButton(m_aryArrCardButtons[i]);
    }
}

/**
  * Called whenever the value of the selection changes.
  * @param e the event that characterizes the change.
  */
public void valueChanged(javax.swing.event.ListSelectionEvent e) {
  boolean isErr = false;
  if (!e.getValueIsAdjusting())
    return;
  int m_nFirstSelectedIndex = -1;
  //ѡ������
  int iSelCnt = 0;
  //������Ϊδѡ��
  int iCount = getBillListPanel().getHeadTable().getRowCount();
  for (int i = 0; i < iCount; i++) {
    getBillListPanel().getHeadBillModel().setRowState(i, BillModel.NORMAL);
  }
  //�õ���ѡ�е���
  int[] iaSelectedRow = getBillListPanel().getHeadTable().getSelectedRows();
  if (iaSelectedRow == null || iaSelectedRow.length == 0) {
    m_nFirstSelectedIndex = -1;
  } else {
    iSelCnt = iaSelectedRow.length;
    //m_nFirstSelectedIndex = iaSelectedRow[0];
    //ѡ�е��б�ʾΪ�򣪺�
    for (int i = 0; i < iSelCnt; i++) {
      getBillListPanel().getHeadBillModel().setRowState(
        iaSelectedRow[i],
        BillModel.SELECTED);
    }
  }
  if (iSelCnt == 1 && iaSelectedRow != null && iaSelectedRow.length > 0) {
    m_nFirstSelectedIndex = iaSelectedRow[0];
  }
  if (m_nFirstSelectedIndex < 0) {
    getBillListPanel().setBodyValueVO(null);
  } else {
    int nCurIndex =
      nc.ui.pu.pub.PuTool.getIndexBeforeSort(
        getBillListPanel(),
        m_nFirstSelectedIndex);
    setDispIndex(nCurIndex);
    isErr = setListBodyData(nCurIndex);
    //ˢ��
    getBillListPanel().getBodyTabbedPane().setSelectedIndex(0);//20101010-Meichao �޸Ľ���ҳǩΪ�ӱ�ҳǩ.�Ӷ�ʹ��ʽִ�гɹ���ʾ.
    getBillListPanel().getBodyBillModel("jj_scm_informationcost").execLoadFormula();
    getBillListPanel().getBodyTable().updateUI();
    
  }
  //��ť�߼�
  if ("ת���б�".equals(getStateStr())) {
    setButtonsListValueChangedNew(iSelCnt);
  } else {
    setButtonsList();
  }
  //�������ҵ���쳣���������ù��ܰ�ť
  if (isErr){
    setButtonsListWhenErr();
  }
  updateButtons();
}
/**
 * ��������:�Զ������PK(����)
 */
private void setBodyDefPK(BillEditEvent event) {
  if (event.getKey().equals("vdef1")) {
    DefSetTool.afterEditBody(getBillCardPanel().getBillModel(), event
        .getRow(), "vdef1", "pk_defdoc1");
  } else if (event.getKey().equals("vdef2")) {
    DefSetTool.afterEditBody(getBillCardPanel().getBillModel(), event
        .getRow(), "vdef2", "pk_defdoc2");
  } else if (event.getKey().equals("vdef3")) {
    DefSetTool.afterEditBody(getBillCardPanel().getBillModel(), event
        .getRow(), "vdef3", "pk_defdoc3");
  } else if (event.getKey().equals("vdef4")) {
    DefSetTool.afterEditBody(getBillCardPanel().getBillModel(), event
        .getRow(), "vdef4", "pk_defdoc4");
  } else if (event.getKey().equals("vdef5")) {
    DefSetTool.afterEditBody(getBillCardPanel().getBillModel(), event
        .getRow(), "vdef5", "pk_defdoc5");
  } else if (event.getKey().equals("vdef6")) {
    DefSetTool.afterEditBody(getBillCardPanel().getBillModel(), event
        .getRow(), "vdef6", "pk_defdoc6");
  } else if (event.getKey().equals("vdef7")) {
    DefSetTool.afterEditBody(getBillCardPanel().getBillModel(), event
        .getRow(), "vdef7", "pk_defdoc7");
  } else if (event.getKey().equals("vdef8")) {
    DefSetTool.afterEditBody(getBillCardPanel().getBillModel(), event
        .getRow(), "vdef8", "pk_defdoc8");
  } else if (event.getKey().equals("vdef9")) {
    DefSetTool.afterEditBody(getBillCardPanel().getBillModel(), event
        .getRow(), "vdef9", "pk_defdoc9");
  } else if (event.getKey().equals("vdef10")) {
    DefSetTool.afterEditBody(getBillCardPanel().getBillModel(), event
        .getRow(), "vdef10", "pk_defdoc10");
  } else if (event.getKey().equals("vdef11")) {
    DefSetTool.afterEditBody(getBillCardPanel().getBillModel(), event
        .getRow(), "vdef11", "pk_defdoc11");
  } else if (event.getKey().equals("vdef12")) {
    DefSetTool.afterEditBody(getBillCardPanel().getBillModel(), event
        .getRow(), "vdef12", "pk_defdoc12");
  } else if (event.getKey().equals("vdef13")) {
    DefSetTool.afterEditBody(getBillCardPanel().getBillModel(), event
        .getRow(), "vdef13", "pk_defdoc13");
  } else if (event.getKey().equals("vdef14")) {
    DefSetTool.afterEditBody(getBillCardPanel().getBillModel(), event
        .getRow(), "vdef14", "pk_defdoc14");
  } else if (event.getKey().equals("vdef15")) {
    DefSetTool.afterEditBody(getBillCardPanel().getBillModel(), event
        .getRow(), "vdef15", "pk_defdoc15");
  } else if (event.getKey().equals("vdef16")) {
    DefSetTool.afterEditBody(getBillCardPanel().getBillModel(), event
        .getRow(), "vdef16", "pk_defdoc16");
  } else if (event.getKey().equals("vdef17")) {
    DefSetTool.afterEditBody(getBillCardPanel().getBillModel(), event
        .getRow(), "vdef17", "pk_defdoc17");
  } else if (event.getKey().equals("vdef18")) {
    DefSetTool.afterEditBody(getBillCardPanel().getBillModel(), event
        .getRow(), "vdef18", "pk_defdoc18");
  } else if (event.getKey().equals("vdef19")) {
    DefSetTool.afterEditBody(getBillCardPanel().getBillModel(), event
        .getRow(), "vdef19", "pk_defdoc19");
  } else if (event.getKey().equals("vdef20")) {
    DefSetTool.afterEditBody(getBillCardPanel().getBillModel(), event
        .getRow(), "vdef20", "pk_defdoc20");
  }
}

/**
 * ��������:�Զ������PK(��ͷ)
 */
private void setHeadDefPK(BillEditEvent event) {
  if (event.getKey().equals("vdef1")) {
    DefSetTool.afterEditHead(getBillCardPanel().getBillData(), "vdef1",
        "pk_defdoc1");
  } else if (event.getKey().equals("vdef2")) {
    DefSetTool.afterEditHead(getBillCardPanel().getBillData(), "vdef2",
        "pk_defdoc2");
  } else if (event.getKey().equals("vdef3")) {
    DefSetTool.afterEditHead(getBillCardPanel().getBillData(), "vdef3",
        "pk_defdoc3");
  } else if (event.getKey().equals("vdef4")) {
    DefSetTool.afterEditHead(getBillCardPanel().getBillData(), "vdef4",
        "pk_defdoc4");
  } else if (event.getKey().equals("vdef5")) {
    DefSetTool.afterEditHead(getBillCardPanel().getBillData(), "vdef5",
        "pk_defdoc5");
  } else if (event.getKey().equals("vdef6")) {
    DefSetTool.afterEditHead(getBillCardPanel().getBillData(), "vdef6",
        "pk_defdoc6");
  } else if (event.getKey().equals("vdef7")) {
    DefSetTool.afterEditHead(getBillCardPanel().getBillData(), "vdef7",
        "pk_defdoc7");
  } else if (event.getKey().equals("vdef8")) {
    DefSetTool.afterEditHead(getBillCardPanel().getBillData(), "vdef8",
        "pk_defdoc8");
  } else if (event.getKey().equals("vdef9")) {
    DefSetTool.afterEditHead(getBillCardPanel().getBillData(), "vdef9",
        "pk_defdoc9");
  } else if (event.getKey().equals("vdef10")) {
    DefSetTool.afterEditHead(getBillCardPanel().getBillData(), "vdef10",
        "pk_defdoc10");
  } else if (event.getKey().equals("vdef11")) {
    DefSetTool.afterEditHead(getBillCardPanel().getBillData(), "vdef11",
        "pk_defdoc11");
  } else if (event.getKey().equals("vdef12")) {
    DefSetTool.afterEditHead(getBillCardPanel().getBillData(), "vdef12",
        "pk_defdoc12");
  } else if (event.getKey().equals("vdef13")) {
    DefSetTool.afterEditHead(getBillCardPanel().getBillData(), "vdef13",
        "pk_defdoc13");
  } else if (event.getKey().equals("vdef14")) {
    DefSetTool.afterEditHead(getBillCardPanel().getBillData(), "vdef14",
        "pk_defdoc14");
  } else if (event.getKey().equals("vdef15")) {
    DefSetTool.afterEditHead(getBillCardPanel().getBillData(), "vdef15",
        "pk_defdoc15");
  } else if (event.getKey().equals("vdef16")) {
    DefSetTool.afterEditHead(getBillCardPanel().getBillData(), "vdef16",
        "pk_defdoc16");
  } else if (event.getKey().equals("vdef17")) {
    DefSetTool.afterEditHead(getBillCardPanel().getBillData(), "vdef17",
        "pk_defdoc17");
  } else if (event.getKey().equals("vdef18")) {
    DefSetTool.afterEditHead(getBillCardPanel().getBillData(), "vdef18",
        "pk_defdoc18");
  } else if (event.getKey().equals("vdef19")) {
    DefSetTool.afterEditHead(getBillCardPanel().getBillData(), "vdef19",
        "pk_defdoc19");
  } else if (event.getKey().equals("vdef20")) {
    DefSetTool.afterEditHead(getBillCardPanel().getBillData(), "vdef20",
        "pk_defdoc20");
  }
}
private boolean chechDataBeforeSave(ArriveorderVO newvo,ArriveorderVO oldvo){
  nc.vo.scm.pu.Timer timer= new nc.vo.scm.pu.Timer();
  timer.start("�ɹ��������������chechDataBeforeSave��ʼ");

  int nError = -1;
  //��鵥���к�
  if (!BillRowNo.verifyRowNosCorrect(getBillCardPanel(), "crowno")) {
    showHintMessage(m_lanResTool.getStrByID("SCMCOMMON","UPPSCMCommon-000010")/*@res "����ʧ��"*/);
    return false;
  }
  timer.addExecutePhase("��鵥���к�verifyRowNosCorrect");
  //��鵥��ģ��ǿ���
  try {
    nc.ui.pu.pub.PuTool.validateNotNullField(getBillCardPanel());
  } catch (Exception e) {
    showHintMessage(m_lanResTool.getStrByID("40040301","UPP40040301-000191")/*@res "����ʧ��:������Ŀ���ڿ���"*/);
    MessageDialog.showWarningDlg(this, m_lanResTool.getStrByID("40040301","UPP40040301-000192")/*@res "����ģ��ǿ�����"*/, e.getMessage());
    return false;
  }
  timer.addExecutePhase(m_lanResTool.getStrByID("40040301","UPP40040301-000193")/*@res "��鵥��ģ��ǿ���validateNotNullField"*/);
  try {
    //���������������Ϸ���
    ArriveorderItemVO bodyVO[] = (ArriveorderItemVO[]) newvo.getChildrenVO();
    ArriveorderHeaderVO headVO = (ArriveorderHeaderVO) newvo.getParentVO();
    for (nError = 0; nError < bodyVO.length; nError++){
      if(headVO.getBisback().booleanValue() && bodyVO[nError].getNarrvnum() != null && bodyVO[nError].getNarrvnum().doubleValue() > 0) throw new ValidationException(m_lanResTool.getStrByID("40040301","UPP40040301-000275")/*@res"�˻�����������Ϊ��!"*/);
      bodyVO[nError].validate();
    }
    //���¼�����������Ϸ���
    if (!checkModifyData(newvo, oldvo)) {
      showHintMessage(m_lanResTool.getStrByID("SCMCOMMON","UPPSCMCommon-000010")/*@res "����ʧ��"*/);
      return false;
    }
    //���¼�������Ƿ񳬳����ݿ�����ɷ�Χ
    if (!nc.vo.scm.field.pu.FieldDBValidate.validate((ArriveorderItemVO[]) newvo.getChildrenVO())) {
      showHintMessage(m_lanResTool.getStrByID("40040301","UPP40040301-000194")/*@res "���ڲ������ݳ������ݿ�����ɷ�Χ,����"*/);
      return false;
    }
  } catch (ValidationException e) {
    String[] value = new String[]{String.valueOf(nError + 1),e.getMessage()};
    MessageDialog.showErrorDlg(this, m_lanResTool.getStrByID("40040301","UPP40040301-000195")/*@res "�Ϸ��Լ��"*/, m_lanResTool.getStrByID("40040301","UPP40040301-000196",null,value)/*@res "������"*/+  e.getMessage() );
    showHintMessage(m_lanResTool.getStrByID("SCMCOMMON","UPPSCMCommon-000010")/*@res "����ʧ��"*/);
    return false;
  }
  timer.addExecutePhase("���ݼ��Ϸ���");
  timer.showAllExecutePhase("�ɹ��������������chechDataBeforeSave����");

  return true;
}

/**
 * ���ߣ���ά��
 * ���ܣ������ӡ����
 * ��������
 * ���أ���
 * ���⣺��
 * ���ڣ�(2004-12-15 11:39:21)
 * @throws BusinessException 
 * @throws ClassNotFoundException 
 * @throws IllegalAccessException 
 * @throws InstantiationException 
 */
private void onCardPrint() {
  ArriveorderVO vo = (ArriveorderVO) getCacheVOs()[getDispIndex()];
  ArrayList aryRslt = new ArrayList();
  aryRslt.add(vo);
  try {
    if (printCard == null){
        //Ŀǰ�����Ͼ������벹����
      if(nc.vo.scm.pub.CustomerConfigVO.getCustomerName().equalsIgnoreCase(nc.vo.scm.pub.CustomerConfigVO.NAME_NANJINGPUZHEN)){       
          PurchasePrintDS printData = new PurchasePrintDS(getModuleCode(),getBillCardPanel());      
//        printCard = new ScmPrintTool(getArrBillCardPanel(),printData,aryRslt);
        printCard = new ScmPrintTool(this,getBillCardPanel(),printData,aryRslt,getModuleCode());
      }else{
          printCard = new ScmPrintTool(this,getBillCardPanel(),aryRslt,getModuleCode());
      }
    } else{
      printCard.setData(aryRslt);     
    }
    printCard.onCardPrint(getBillCardPanel(),getBillListPanel(),ScmConst.PO_Arrive);
    if(PuPubVO.getString_TrimZeroLenAsNull(printCard.getPrintMessage()) != null){
      MessageDialog.showHintDlg(this,m_lanResTool.getStrByID("SCMCOMMON","UPPSCMCommon-000270")/*@res "��ʾ"*/,printCard.getPrintMessage());
    }
  } catch (Exception e1) {
    SCMEnv.out(e1);
  }
}
/**
 * ���ߣ���ά��
 * ���ܣ������ӡ����
 * ��������
 * ���أ���
 * ���⣺��
 * ���ڣ�(2004-12-15 11:39:21)
 */
  private void onCardPrintPreview() {
    
    if(getCacheVOs() == null || getCacheVOs().length == 0){
      return;
    }
    ArriveorderVO vo = (ArriveorderVO) getCacheVOs()[getDispIndex()];
    ArrayList aryRslt = new ArrayList();
    aryRslt.add(vo);
    try {
      if (printCard == null){
          //Ŀǰ�����Ͼ������벹����
        if(nc.vo.scm.pub.CustomerConfigVO.getCustomerName().equalsIgnoreCase(nc.vo.scm.pub.CustomerConfigVO.NAME_NANJINGPUZHEN)){       
            PurchasePrintDS printData = new PurchasePrintDS(getModuleCode(),getBillCardPanel());      
          printCard = new ScmPrintTool(getBillCardPanel(),printData,aryRslt,getModuleCode());
        }else{
            printCard = new ScmPrintTool(this,getBillCardPanel(),aryRslt,getModuleCode());
        }
      } else{
        printCard.setData(aryRslt);     
      }
      printCard.onCardPrintPreview(getBillCardPanel(),getBillListPanel(),ScmConst.PO_Arrive);
      try{
    	  //loadDataToCard();
      }catch(Exception e){
    	  SCMEnv.out("���ص���������ʱ����");/*-=notranslate=-*/
    	  SCMEnv.out(e);
      } 

      if(PuPubVO.getString_TrimZeroLenAsNull(printCard.getPrintMessage()) != null){
        //MessageDialog.showHintDlg(this,m_lanResTool.getStrByID("SCMCOMMON","UPPSCMCommon-000270")/*@res "��ʾ"*/,printCard.getPrintMessage());
    	showHintMessage(printCard.getPrintMessage());
      }
      //��ʾ��ͷ��ע
      setHeadValueByKey("vmemo",(String) vo.getParentVO().getAttributeValue("vmemo"));
    } catch (Exception e1) {
      SCMEnv.out(e1);
    }
}
  /**
   * �����ͷ��ע��ʾ����
   * @param value
   */
  private void setHeadValueByKey(String key,String value){
	  if(getBillCardPanel().getHeadItem(key)!=null &&
			  getBillCardPanel().getHeadItem(key).getComponent() instanceof UIRefPane){
		  UIRefPane refPane = (UIRefPane) getBillCardPanel().getHeadItem(key).getComponent();
	      refPane.setValue(value);
	  }else
		  getBillCardPanel().getHeadItem(key).setValue(value);
  }
/**
 * ���ߣ���ά��
 * ���ܣ�����ӡ
 * ��������
 * ���أ���
 * ���⣺��
 * ���ڣ�(2004-12-15 11:39:21)
 */
private void onBatchPrint() {
  if (printList == null){
    printList = new ScmPrintTool(this,getBillCardPanel(),getSelectedBills(),getModuleCode());
  } else{
    try {
      printList.setData(getSelectedBills());
    } catch (BusinessException e1) {
      MessageDialog.showHintDlg(this,m_lanResTool.getStrByID("SCMCOMMON","UPPSCMCommon-000270")/*@res "��ʾ"*/,e1.getMessage());
    }
  }
  try {
    printList.onBatchPrint(getBillListPanel(),getBillCardPanel(),ScmConst.PO_Arrive);
    if(PuPubVO.getString_TrimZeroLenAsNull(printList.getPrintMessage()) != null){
    	this.showHintMessage(printList.getPrintMessage());
    }
  } catch (BusinessException e) {
    SCMEnv.out(e.getMessage());
  }
}
private void onSplitPrint() {

	if (getBillListPanel().getHeadTable().getRowCount() > 0) {
		int rowSelected = getBillListPanel().getHeadTable().getSelectedRowCount();
		if (rowSelected == 0 || rowSelected > 1) {
			showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("401201", "UPP401201-000045")/*@res "��ѡ��һ�ŵ���"*/);
			return;
		}
	}

	if (getCacheVOs() == null || getCacheVOs().length == 0) {
		return;
	}
	SplitPrintParamDlg s=new SplitPrintParamDlg(this,initSplitParams());
	s.showModal();
//	������ӡ����
	SplitParams[] paramvos = null;

	if(s.isCloseByBtnOK()){

		ArrayList<AggregatedValueObject> prlistvo = new ArrayList<AggregatedValueObject>();
		ArriveorderVO vo = getCacheVOs()[getDispIndex()];
		if(vo == null || vo.getBodyLen() < 1){
			return;
		}
		ArriveorderVO newvo = null;
		//�ӽ����ϻ�ȡ���ݡ�����VO��"�������"��"�������"���ֶ���ֵ
		if(getBillCardPanel().isShowing()){
			newvo = (ArriveorderVO) getBillCardPanel().getBillValueVO(ArriveorderVO.class.getName(), ArriveorderHeaderVO.class.getName(), ArriveorderItemVO.class.getName());
		}else{
			int selrow = getBillListPanel().getHeadTable().getSelectedRow();
			newvo = (ArriveorderVO) getBillListPanel().getBillValueVO(selrow, ArriveorderVO.class.getName(),ArriveorderHeaderVO.class.getName(), ArriveorderItemVO.class.getName());
			ArriveorderItemVO[] items = (ArriveorderItemVO[]) getBillListPanel().getBodyBillModel().getBodyValueVOs(ArriveorderItemVO.class.getName());
			newvo.setChildrenVO(items);
		}
		prlistvo.add(newvo);

		paramvos = s.getSplitParams();

		BillPrintTool printTool = null;
		try {
			printTool = new BillPrintTool(getFuncId(), prlistvo,getBillCardPanel().getBillData(), null, getCorpPrimaryKey(),getOperatorId(), "varrordercode", "carriveorderid");
			printTool.onBatchSplitPrintPreview(getBillListPanel(),BillTypeConst.PO_ARRIVE, paramvos);
		}
		catch (BusinessException e) {
			MessageDialog.showHintDlg(this, m_lanResTool.getStrByID("SCMCOMMON","UPPSCMCommon-000270")/* @res "��ʾ" */, e.getMessage());
		}
		catch (InstantiationException e) {
			MessageDialog.showHintDlg(this, m_lanResTool.getStrByID("SCMCOMMON","UPPSCMCommon-000270")/* @res "��ʾ" */, e.getMessage());
		}
		catch (IllegalAccessException e) {
			MessageDialog.showHintDlg(this, m_lanResTool.getStrByID("SCMCOMMON","UPPSCMCommon-000270")/* @res "��ʾ" */, e.getMessage());
		}
		catch (InterruptedException e) {
			MessageDialog.showHintDlg(this, m_lanResTool.getStrByID("SCMCOMMON","UPPSCMCommon-000270")/* @res "��ʾ" */, e.getMessage());
		}
		catch (ClassNotFoundException e) {
			MessageDialog.showHintDlg(this, m_lanResTool.getStrByID("SCMCOMMON","UPPSCMCommon-000270")/*@res "��ʾ"*/, e.getMessage());
		}
	}
}
/**
 * ���ߣ���ά��
 * ���ܣ�����ӡ
 * ��������
 * ���أ���
 * ���⣺��
 * ���ڣ�(2004-12-15 11:39:21)
 */
private void onBatchPrintPreview() {
  if (printList == null){
    printList = new ScmPrintTool(this,getBillCardPanel(),getSelectedBills(),getModuleCode());
  } else{
    try {
      printList.setData(getSelectedBills());
    } catch (BusinessException e1) {
      MessageDialog.showHintDlg(this,m_lanResTool.getStrByID("SCMCOMMON","UPPSCMCommon-000270")/*@res "��ʾ"*/,e1.getMessage());
    }
  }
  try {
    printList.onBatchPrintPreview(getBillListPanel(),getBillCardPanel(),ScmConst.PO_Arrive);
    if(PuPubVO.getString_TrimZeroLenAsNull(printList.getPrintMessage()) != null){
    	this.showHintMessage(printList.getPrintMessage());
       //MessageDialog.showHintDlg(this,m_lanResTool.getStrByID("SCMCOMMON","UPPSCMCommon-000270")/*@res "��ʾ"*/,printList.getPrintMessage());
    }
  } catch (BusinessException e) {
  }
}
/**
 * ��������:���ص���ģ��֮ǰ�ĳ�ʼ��
 */

private void initBillBeforeLoad(BillData bd) {

  //---------����ģ�����ǰ�ĳ�ʼ����������������
  if (bd != null && bd.getBodyItem("vfree0") != null && bd.getBodyItem("vfree0") != null) {
    FreeItemRefPane m_firpFreeItemRefPane = new FreeItemRefPane();
    m_firpFreeItemRefPane.setMaxLength(bd.getBodyItem("vfree0").getLength());
    //�Ӽ�����
    m_firpFreeItemRefPane.getUIButton().addActionListener(this);
    bd.getBodyItem("vfree0").setComponent(m_firpFreeItemRefPane);
  }
  //��ʼ������
  initRefPane(bd);

  //��ʼ��ComboBox
//  initComboBox(bd);
  //��ʼ������
//  initDecimal(bd);

}

/**
 * ���ο���������չ��ť��Ҫ����ο��������������ʵ��
 * 
 * @see nc.ui.scm.pub.bill.IBillExtendFun#getExtendBtns()
 */
public ButtonObject[] getExtendBtns() {
	if(extendBtns == null || extendBtns.length == 0){
		//�������¼�밴ť add by QuSida 2010-8-10  ����ɽ���ܣ�
		extendBtns = new ButtonObject[]{getBoInfoCost()};
	return extendBtns;}
	else return extendBtns;
}

/**
 * ������ο�����ť�����Ӧ����Ҫ����ο��������������ʵ��
 * 
 * @see nc.ui.scm.pub.bill.IBillExtendFun#onExtendBtnsClick(nc.ui.pub.ButtonObject)
 */
public void onExtendBtnsClick(ButtonObject bo) {
	 if(bo == getBoInfoCost()){
	    	this.onBoInfoCost();
	    }
}
/**
 * ���ο���״̬��ԭ�н���״̬����󶨣�Ҫ����ο��������������ʵ��
 *
 *  ״̬��ֵ���ձ�
 * 
 *  0����ʼ��
 *  1���������
 *  2�������޸�
 *  3�������б�
 *  4��ת���б�
 *  5��ת���޸�
 */
public void setExtendBtnsStat(int iState) {

	//���÷���¼�밴ť��״̬  add by QuSida 2010-8-29 (��ɽ����)
	if(iState==0){
		getBoInfoCost().setEnabled(false);
		updateButton(getBoInfoCost());
	}
	else if(iState==1){
		getBoInfoCost().setEnabled(false);
		updateButton(getBoInfoCost());
	}
	else if(iState==2){
		getBoInfoCost().setEnabled(true);
		updateButton(getBoInfoCost());
	}
	else if(iState==3){
		getBoInfoCost().setEnabled(false);
		updateButton(getBoInfoCost());
	}
	else if(iState==4){
		getBoInfoCost().setEnabled(false);
		updateButton(getBoInfoCost());
	}
	else if(iState==5){
		getBoInfoCost().setEnabled(true);
		updateButton(getBoInfoCost());
	}
	else if(iState==6){
		getBoInfoCost().setEnabled(false);
		updateButton(getBoInfoCost());
	}
		

}
/**
 *  ���󵽻���
 * <p>
 * <strong>����ģ�飺</strong>�ɹ�����
 * <p>
 * <strong>����޸��ˣ�</strong>czp
 * <p>
 * <strong>����޸����ڣ�</strong>2006-02-09
 * <p>
 * <strong>����������</strong>
 * <p>
 * @param    ��
 * @return   ��
 * @throws   ��
 * @since    NC50
 * @see      
 */
private void onSendAudit(){

  // �༭״̬���󣽡����桱��������
  if (getStateStr().equals("ת���޸�") || getStateStr().equals("�����޸�")) {
    onSave();
  }
  //�ò������Ƿ�����������
  if(getCacheVOs() == null || getDispIndex() < 0)
    return;
  ArriveorderVO vo = getCacheVOs()[getDispIndex()];
  
  if(isNeedSendAudit(getCacheVOs()[getDispIndex()])){
    try {
      HashMap<String, String> hmPfExParams = new HashMap<String, String>();
      hmPfExParams.put(PfUtilBaseTools.PARAM_RELOAD_VO, PfUtilBaseTools.PARAM_RELOAD_VO);
      PfUtilClient.processAction("SAVE",BillTypeConst.PO_ARRIVE, ClientEnvironment.getInstance().getDate().toString(), vo);
      
      /*�����ɹ���ˢ��*/   
      refreshVoFieldsByKey(vo,vo.getParentVO().getPrimaryKey());
      //
      getCacheVOs()[getDispIndex()] = vo;
      /*���ص���:��Ч���Ż��ռ�*/
      loadDataToCard();
      /*ˢ�°�ť״̬*/
      setButtonsState();
    } catch (Exception e) {
      SCMEnv.out("����������ʧ�ܣ�");
      SCMEnv.out(e);
      if (e instanceof BusinessException || e instanceof RuntimeException) {
        MessageDialog.showErrorDlg(
            this, 
            m_lanResTool.getStrByID("SCMCOMMON", "UPPSCMCommon-000270")/*@res "��ʾ"*/, 
            e.getMessage());
      } else {
        MessageDialog.showErrorDlg(
            this, 
            m_lanResTool.getStrByID("SCMCOMMON", "UPPSCMCommon-000270")/*@res "��ʾ"*/, 
            m_lanResTool.getStrByID("SCMCOMMON", "UPPSCMCommon-000408")/*@res"����ʧ�ܣ�"*/);
      }
    }
  }
  showHintMessage(m_lanResTool.getStrByID("common","UCH023")/*@res "������"*/);
}
/**
 *  �жϵ������Ƿ��б�Ҫ����
 * <p>
 * <strong>����ģ�飺</strong>�ɹ�����
 * <p>
 * <strong>����޸��ˣ�</strong>czp
 * <p>
 * <strong>����޸����ڣ�</strong>2006-02-09
 * <p>
 * <strong>����������</strong>Ҫ��ͬʱ���㣬
 * <p>
 * 1)������״̬Ϊ�����ɡ�(Ŀǰ���빺�����ɹ���������һ�£�������ͨ����Ҫ[�޸�]-[����]����������״̬��Ϊ���ɣ��ſ�����)
 * <p>
 * 2)��������������
 * <p>
 * @param    ��
 * @return   ��
 * @throws   ��
 * @since    NC50
 * @see      
 */
private boolean isNeedSendAudit(ArriveorderVO vo){

  boolean bRet = false;
  if(vo == null || vo.getHeadVO() == null)
    return false;
  String billid = vo.getHeadVO().getCarriveorderid();
  String busiType = vo.getHeadVO().getCbiztype();
  boolean isNeedSendToAuditQ = BusiBillManageTool.isNeedSendToAudit(BillTypeConst.PO_ARRIVE,getCorpPrimaryKey(), busiType, billid, getClientEnvironment().getUser().getPrimaryKey());
  bRet = (isNeedSendToAuditQ
      && vo.getHeadVO().getIbillstatus() != null
      && (vo.getHeadVO().getIbillstatus().intValue() == 0 || vo.getHeadVO().getIbillstatus().intValue() == 2 || vo.getHeadVO().getIbillstatus().intValue() == 4));
  return bRet;
}

/**
 *  ��ȡ��ǰVO����Ϣ������
 * <p>
 * <strong>����ģ�飺</strong>�ɹ�����
 * <p>
 * <strong>����޸��ˣ�</strong>czp
 * <p>
 * <strong>����޸����ڣ�</strong>2006-02-10
 * <p>
 * <strong>����������</strong>
 * <p>
 * @param    ��
 * @return   ��Ϣ������ʾ��ҵ�񵥾�VO
 * @throws   ��
 * @since    NC50
 * @see      
 */
public AggregatedValueObject getVo() throws Exception {
  ArriveorderVO vo = null;
  if(getCacheVOs() != null && getCacheVOs().length == 1 && getCacheVOs()[0] != null){
    SCMEnv.out("��������ֵ���������²�ѯ!");
    return getCacheVOs()[0];
  }
  try{
    vo = ArriveorderHelper.findByPrimaryKey(m_strBillId);
  }catch(Exception e){
    PuTool.outException(this,e);
  }
  return vo;
}
/**
 * ��ѯ��ǰ��������״̬
 */
private void onQueryForAudit() {
  if (getCacheVOs() != null 
      && getCacheVOs().length > 0 
      && getCacheVOs()[0] != null 
      && getCacheVOs()[0].getHeadVO() != null) {
    FlowStateDlg approvestatedlg = new FlowStateDlg(
        this,
        BillTypeConst.PO_ARRIVE,
        getCacheVOs()[0].getHeadVO().getCbiztype(),
        getCacheVOs()[0].getHeadVO().getPrimaryKey());
    approvestatedlg.showModal();
  }
  showHintMessage(m_lanResTool.getStrByID("common","UCH035")/*@res "����״̬��ѯ�ɹ�"*/);
}
/**
 * <p>���򷽷�������Ҫ����Ļ���VO����
 * @since V50
 */
public Object[] getRelaSortObjectArray() {
  return getCacheVOs();
}

/**
 * ��������ӿڷ���ʵ�� -- ά��
 **/
public void doMaintainAction(ILinkMaintainData maintaindata) {
  SCMEnv.out("����ά���ӿ�...");

  if(maintaindata == null || maintaindata.getBillID() == null){
    SCMEnv.out("msgVo Ϊ�գ�ֱ�ӷ���!");
    SCMEnv.out("****************�����ǵ��ö�ջ���Ǵ���****************");
    SCMEnv.out(new Exception());
    SCMEnv.out("****************�����ǵ��ö�ջ���Ǵ���****************");
    return;
  }
  //���ؿ�Ƭ
  setLayout(new java.awt.BorderLayout());
  add(getBillCardPanel(),java.awt.BorderLayout.CENTER);
  //���ð�ť��
  setButtons(m_aryArrCardButtons);
  /*
  for(int i=0;i<m_aryMsgCenter.length;i++){
    m_aryMsgCenter[i].setEnabled(true);
  }
  */
  //��ѯ����������
  ArriveorderVO vo = null;
  //��¼����ID��getVo()��
  m_strBillId = maintaindata.getBillID();
  try{
    vo = ArriveorderHelper.findByPrimaryKey(m_strBillId);
  }catch(Exception e){
    PuTool.outException(this,e);
    return;
  }
  
  //�����ǰ��¼��˾���ǲ���Ա�Ƶ����ڹ�˾��������޲�����ť�����ṩ������ܣ�by chao , xy , 2006-11-07
  String strLoginCorpId = PoPublicUIClass.getLoginPk_corp();
  String strPrayCorpId = vo == null ? null : vo.getHeadVO().getPk_corp();
  boolean bSameCorpFlag = strLoginCorpId.equals(strPrayCorpId);
  
  if(vo == null){
    if(!bSameCorpFlag){
      setButtonsNull();
    }
    return;
  }
  setCacheVOs(new ArriveorderVO[]{vo});
  for (int i = 0; i < getCacheVOs().length; i++) {
    if (getCacheVOs()[i].getChildrenVO() != null
      && getCacheVOs()[i].getChildrenVO().length > 0) {
      //�����ݸ���Դ����
      String cupsourcebilltype =
        ((ArriveorderItemVO[]) getCacheVOs()[i].getChildrenVO())[0]
          .getCupsourcebilltype();
      ((ArriveorderVO) getCacheVOs()[i]).setUpBillType(cupsourcebilltype);
      //ˢ�±����ϣ����
      for (int j = 0; j < getCacheVOs()[i].getChildrenVO().length; j++) {
        try{
          if (getCacheVOs()[i].getChildrenVO()[j].getPrimaryKey() == null){
            continue;
          }
          if (getCacheVOs()[i].getChildrenVO()[j] == null){
            continue;
          }
          hBodyItem.put(
            getCacheVOs()[i].getChildrenVO()[j].getPrimaryKey(),
            getCacheVOs()[i].getChildrenVO()[j]);
        }catch(BusinessException e){
          PuTool.outException(this,e);
          return;
        }
      }
    }
  }
  setDispIndex(0);
  try{
    loadDataToCard();
  }catch(Exception e){
    SCMEnv.out("���ص���������ʱ����");/*-=notranslate=-*/
    SCMEnv.out(e);
  } 
  //����ͬһ��˾�����а�ť���ɼ�
  if(!bSameCorpFlag){
    setButtonsNull();
    return;
  }
  //�����ͬһ��˾ 
//  if(vo.isCanBeModified()){
//    onModify();
//  }else{
    setButtonsCard(); 
//  }
}
/**
 * ��������ӿڷ���ʵ�� -- ����
 **/
public void doAddAction(ILinkAddData adddata) {

  SCMEnv.out("���������ӿ�...");
  
  //Ĭ�ϴ˽ڵ�ɴ�
  m_strNoOpenReasonMsg = null;
  
  if(adddata == null){
    SCMEnv.out("ILinkAddData::adddata����Ϊ�գ�ֱ�ӷ���");/*-=notranslate=-*/
    return;
  }
  String strUpBillType = adddata.getSourceBillType();
  //����Ϊ�ɹ�����
  if(BillTypeConst.PO_ORDER.equals(strUpBillType)){
    OrderVO voOrder = null;
    try{
      voOrder = OrderHelper.queryForOrderBillLinkAdd(new ClientLink(ClientEnvironment.getInstance()),adddata.getSourceBillID());
    }catch(Exception e){      
      SCMEnv.out(e);
      return;
    }
    //�˽ڵ��Ƿ�ɴ�
    if(voOrder == null){
      MessageDialog.showHintDlg(this, 
          NCLangRes.getInstance().getStrByID("SCMCOMMON","UPPSCMCommon-000270")/* @res "��ʾ" */, 
          NCLangRes.getInstance().getStrByID("40040301","UPP40040301-000287")/* @res "�������ݲ������ɵ�����������ԭ��1������ҵ�������ߵ����ƻ�����δ���ɵ����ƻ���2�����ж����о�Ϊ�����ۿ����ԣ�3�������Ѿ���ȫ����"*/);
      return;
    }
    ArrFrmOrdUI billReferUI = new ArrFrmOrdUI(
        "corderid",
        ClientEnvironment.getInstance().getCorporation().getPrimaryKey(),
        ClientEnvironment.getInstance().getUser().getPrimaryKey(),
        "4004020201",
        "null1",
        ScmConst.PO_Order,
        null,
        "nc.ui.po.pub.PoToRcQueDLG",
        ScmConst.PO_Arrive,
        this,
        false,
        true);
    // ��������
    billReferUI.loadDataForMsgCenter(new OrderVO[]{voOrder});
    //
    billReferUI.showModal();
    //
    if (billReferUI.getResult() == UIDialog.ID_OK) {
      ArriveorderVO[] retVOs = (ArriveorderVO[]) billReferUI.getRetVos();
      onExitFrmOrd(retVOs);
    }else{
      SCMEnv.out("ȡ���������ɲ���");/*-=notranslate=-*/
      billReferUI.closeCancel();
      billReferUI.destroy();
    }
  }
  
}
/**
 * ���򿪸ýڵ��ǰ�����������ڴ���ֻ������ĳһ����ʱ�����ܴ򿪸ýڵ㡱 �������
 * ��Ҫ�жϡ�ֻ������ĳһ����ʱ�����ܴ򿪸ýڵ㡱�Ľڵ㣬��Ҫʵ�ֱ������� �ڷ����ڽ��������жϡ�
 * ������ݷ���ֵ������Ӧ�����������ֵΪһ���ǿ��ַ�������ô���಻��
 * �ýڵ㣬ֻ��һ���Ի�������ʾ���ص��ַ������������ֵΪnull����ô������Դ� �����ڵ�һ���򿪸ýڵ㡣
 * 
 * �������ڣ�(2002-3-11 10:39:16)
 * 
 * @return java.lang.String
 */
protected String checkPrerequisite() {
    return m_strNoOpenReasonMsg;
}
/**
 * ��������ӿڷ���ʵ�� -- ����
 **/
public void doApproveAction(ILinkApproveData approvedata) {
  SCMEnv.out("���������ӿ�...");
  isRevise = true;
  if(approvedata == null || approvedata.getBillID() == null){
    SCMEnv.out("msgVo Ϊ�գ�ֱ�ӷ���!");
    SCMEnv.out("****************�����ǵ��ö�ջ���Ǵ���****************");
    SCMEnv.out(new Exception());
    SCMEnv.out("****************�����ǵ��ö�ջ���Ǵ���****************");
    return;
  }
  //���ؿ�Ƭ
  setLayout(new java.awt.BorderLayout());
  add(getBillCardPanel(),java.awt.BorderLayout.CENTER);
  //��ѯ����������
  ArriveorderVO vo = null;
  //��¼����ID��getVo()��
  m_strBillId = approvedata.getBillID();
  try{
    vo = ArriveorderHelper.findByPrimaryKey(m_strBillId);
    if(vo == null) 
      return;
    setCacheVOs(new ArriveorderVO[]{vo});
    setDispIndex(0);
    loadDataToCard();
  }catch(Exception e){
    PuTool.outException(this,e);
  }
  getBillCardPanel().setEnabled(false);
  
  //��¼��˾�뵥��������˾�Ƿ���ͬ
  boolean bCorpSameFlag = getCorpPrimaryKey().equals(vo.getPk_corp()) ;
  //���ð�ť��
  if(bCorpSameFlag){
    setButtons(m_aryArrCardButtons);
    setM_strState("�������");
  }else{
    if(m_btnActionMsgCenter.getChildCount() == 0){
      m_btnActionMsgCenter.addChildButton(m_btnAudit);
      m_btnActionMsgCenter.addChildButton(m_btnUnAudit); 
    }
    if(m_btnOthersMsgCenter.getChildCount() == 0){
      m_btnOthersMsgCenter.addChildButton(m_btnQueryForAudit);
      m_btnOthersMsgCenter.addChildButton(m_btnDocument);
      m_btnOthersMsgCenter.addChildButton(m_btnLookSrcBill); 
    }
    setButtons(m_aryMsgCenter);
    //
    setM_strState("��Ϣ����");
  }
  //
  setButtonsState();
  setButtonsRevise();

}

/**
 * ����������������Ҫ�����������Ĺ��ܡ�
 * <p>
 * <b>examples:</b>
 * <p>
 * ʹ��ʾ��
 * <p>
 * <b>����˵��</b>
 * 
 * @param isCurAuditOPER
 *          �Ƿ�ǰ������
 *          <p>
 * @author donggq
 * @time 2008-10-24 ����10:26:37
 */
private void setButtonsRevise() {
  if (isRevise) {
    m_btnAdds.setEnabled(false);
    m_btnDiscard.setEnabled(false);
    m_btnDiscardList.setEnabled(false);
    updateButton(m_btnDiscard);
    updateButton(m_btnDiscardList);
    updateButton(m_btnAdds);
    updateButton(m_btnModify);
    updateButton(m_btnModifyList);
  }

  if (getCacheVOs() != null) {
    Integer iBillStatus = PuPubVO.getInteger_NullAs(getCacheVOs()[getDispIndex()].getParentVO().getAttributeValue("ibillstatus"), new Integer(BillStatus.FREE));
    String strPsnOld = (getCacheVOs()[getDispIndex()]).getHeadVO().getCauditpsn();
    if (iBillStatus.intValue() == 2 && PuPubVO.getString_TrimZeroLenAsNull(strPsnOld) == null) {
      m_btnModify.setEnabled(true);
      m_btnModifyList.setEnabled(true);
      updateButton(m_btnModify);
      updateButton(m_btnModifyList);
    }
  }
}

/**
 * ��������ӿڷ���ʵ�� -- ������ 
 **/
public void doQueryAction(ILinkQueryData querydata) {
  SCMEnv.out("����������ӿ�...");
  
  String billID = querydata.getBillID();
  
  
  //initialize();
  //
  setM_strState("�������");
  //
  //ArriveorderVO vo = null;
  
  try {
    /*vo = ArriveorderHelper.findByPrimaryKey(billID);
    if(vo == null){
      MessageDialog.showHintDlg(this, NCLangRes.getInstance().getStrByID("SCMCOMMON","UPPSCMCommon-000270") "��ʾ" , 
          m_lanResTool.getStrByID("common", "SCMCOMMON000000161") "û�в쿴���ݵ�Ȩ��" );
      return;
    }*/
    //
    String strPkCorp = querydata.getPkOrg()==null?getCorpPrimaryKey():querydata.getPkOrg();/*vo.getPk_corp();*/
    
    //���յ���������˾���ز�ѯģ�� 
    RcQueDlg queryDlg = new RcQueDlg(this, getBusitype(), getFuncId(), getOperatorId(), strPkCorp);//��ѯģ����û�й�˾ʱ��Ҫ�������⹫˾
    queryDlg.setDefaultValue("po_arriveorder.dreceivedate","po_arriveorder.dreceivedate","");
    queryDlg.initCorpsRefs();
    //���ù���������ȡ�ù�˾�п���Ȩ�޵ĵ�������VO����
    ConditionVO[] condsUserDef = queryDlg.getDataPowerConVOs(strPkCorp,RcQueDlg.REFKEYS);
    //��֯�ڶ��β�ѯ���ݣ�����Ȩ�޺͵���PK����
    ArriveorderVO[] voaRet = ArriveorderHelper.queryAllArriveMy(condsUserDef, 
        null,
        strPkCorp, 
        null, 
        "po_arriveorder.carriveorderid = '" + billID + "' ");
    if(voaRet == null || voaRet.length <= 0 || voaRet[0] == null){
      MessageDialog.showHintDlg(this, NCLangRes.getInstance().getStrByID("SCMCOMMON","UPPSCMCommon-000270")/* "��ʾ" */, 
          m_lanResTool.getStrByID("common", "SCMCOMMON000000161")/* "û�в쿴���ݵ�Ȩ��" */);
      setButtonsNull();
      return;
    }
    setCacheVOs(voaRet);
    setDispIndex(0);
    loadDataToCard();
  } catch (Exception e) {
    SCMEnv.out(e);
    MessageDialog.showErrorDlg(this, NCLangRes.getInstance().getStrByID("SCMCOMMON","UPPSCMCommon-000270")/* "��ʾ" */, 
        e.getMessage());
    setButtonsNull();
    return;
  }
  boolean bCorpSameFlag = getCorpPrimaryKey().equals(querydata.getPkOrg()/*vo.getPk_corp()*/) ;
  //���ð�ť��
  if(bCorpSameFlag){
    setButtons(m_aryArrCardButtons);
    setButtonsCard();
  }else{
    setButtonsNull();
  }
}
/**
 * ��յ�ǰ���水ť
 */
private void setButtonsNull(){
  ButtonObject[] objs = getButtons();
  int iLen = objs == null ? 0 : objs.length;
  for (int i = 0; i < iLen; i++) {
    if (objs[i] == null) {
      continue;
    }
    objs[i].setVisible(false);
    updateButton(objs[i]);
  }
}
/**
 * �ϲ���ʾ����ӡ����
 * 
 * @since v50
 */
private void onCombin() {
  CollectSettingDlg dlg = new CollectSettingDlg(this,
      NCLangRes.getInstance().getStrByID("common",
      "4004COMMON000000089")/*@res "�ϲ���ʾ"*/,
      ScmConst.PO_Arrive,
      "40040301",getCorpPrimaryKey(),
      ClientEnvironment.getInstance().getUser().getPrimaryKey(),
      ArriveorderVO.class.getName(),
      ArriveorderHeaderVO.class.getName(),
      ArriveorderItemVO.class.getName());
  //
  
  JComponent tempCom = null; 
  if(getBillCardPanel().getHeadItem("vmemo").getComponent() instanceof UIRefPane){
    tempCom = getBillCardPanel().getHeadItem("vmemo").getComponent(); 
    getBillCardPanel().getHeadItem("vmemo").setDataType(BillItem.STRING);
  } 

  dlg.initData(getBillCardPanel(),
  // �̶�������
      new String[] { "cinventorycode", "cinventoryname",
    "cinventoryspec", "cinventorytype", "prodarea" },
      // ȱʡ������
      null,
      // �����
      new String[] { "nmoney", "narrvnum","nelignum","nnotelignum","nwastnum" },
      // ��ƽ����
      null,
      // ���Ȩƽ����
      new String[] { "nprice" },
      // ������
      "narrvnum");
  dlg.showModal();

  if(tempCom != null){
      getBillCardPanel().getHeadItem("vmemo").setDataType(BillItem.UFREF);
      getBillCardPanel().getHeadItem("vmemo").setComponent(tempCom);
    }

  showHintMessage(m_lanResTool
      .getStrByID("common", "4004COMMON000000039")/* @res "�ϲ���ʾ���" */);
  try{
	    loadDataToCard();
	  }catch(Exception e){
	    SCMEnv.out("���ص���������ʱ����");/*-=notranslate=-*/
	    SCMEnv.out(e);
	  } 

}

/**
 * �����ʲ���Ƭ��ť״̬��
 * <p>
 * <b>examples:</b>
 * <p>
 * ʹ��ʾ��
 * <p>
 * <b>����˵��</b>
 * <p>
 * @author lixiaodong
 * @time 2008-5-12 ����02:48:14
 */
private void setBtnStatusSN() {
  if (getCacheVOs() == null || getCacheVOs().length <= 0) {
    return;
  }
  ArriveorderVO vo = getCacheVOs()[getDispIndex()];
  int row = -1;

  //ֻ�п�Ƭ�¿���
  int iBillStatus = PuPubVO.getInteger_NullAs(getCacheVOs()[getDispIndex()].getParentVO().getAttributeValue("ibillstatus"),new Integer(BillStatus.FREE));
  if (!m_bAIMEnabled || !getStateStr().equals("�������") || m_btnAudit.isEnabled() || ((ArriveorderVO) vo).isReturn() || iBillStatus != BillStatus.AUDITED) {
    m_btnCreateCard.setEnabled(false);
    m_btnDeleteCard.setEnabled(false);
    return;
  }

  String bfaflag = "false";
  int intCrtNum = 0;
  int intLen = vo.getBodyLen();
  for (int i = 0; i < intLen;i++){
    bfaflag=getBillCardPanel().getBillModel().getValueAt(i, "bfaflag")==null?"":getBillCardPanel().getBillModel().getValueAt(i, "bfaflag").toString();
    if("true".equalsIgnoreCase(bfaflag)){
      intCrtNum++;
    }
  }

  if(intCrtNum==0){//û�����ɿ�Ƭ
    m_btnCreateCard.setEnabled(true);
    m_btnDeleteCard.setEnabled(false);
  }else if(intCrtNum==intLen){//ȫ�����ɿ�Ƭ
    m_btnCreateCard.setEnabled(false);
    m_btnDeleteCard.setEnabled(true);
  }else{//�������ɿ�Ƭ
    m_btnCreateCard.setEnabled(true);
    m_btnDeleteCard.setEnabled(true);
  }

  updateButtonsAll();
}

/**
 * ���кŷ��䡣
 * <p>
 * <b>examples:</b>
 * <p>
 * ʹ��ʾ��
 * <p>
 * <b>����˵��</b>
 * @param
 * @return
 * @throws
 * <p>
 * @author lixiaodong
 * @time 2008-5-28 ����01:08:26
 */
public boolean onSNAssign() {
  showHintMessage(m_lanResTool.getStrByID("40040301","UPT40040301-000137")/*@res"���кŷ��俪ʼ!"*/);
  int row = -1;

  if (getCacheVOs() == null || getCacheVOs().length <= 0) {
    return false;
  }

  ArriveorderVO cardVos = null;
  if(cardVos==null){
    cardVos = (ArriveorderVO) getCacheVOs()[getDispIndex()].clone();
  }
  if (cardVos == null) {
    return false;
  }

  if(!cardVos.getHeadVO().isAuditted()){
    showHintMessage(m_lanResTool.getStrByID("40040301", "UPT40040301-000140")/* @res"����δ����!" */);
    return false;
  }
  
  if(isFromSC())
  {
	  String sTitle = m_lanResTool.getStrByID("SCMCOMMON", "UPPSCMCommon-000270")/* @res "��ʾ" */;
	  String sError = m_lanResTool.getStrByID("40040301", "UPT40040301-000157")/* @res"��֧����ԴΪί��ĵ����������ʲ���Ƭ" */;
      MessageDialog.showErrorDlg(this, sTitle, sError);
      return false;
  }
  
  String sTitle = m_lanResTool.getStrByID("SCMCOMMON", "UPPSCMCommon-000270")/* @res "��ʾ" */;
  String sError = m_lanResTool.getStrByID("40040301", "UPT40040301-000139")/* @res"û�пɷ������кŵ���!" */;  
  if(filterVos(cardVos)==null){
    MessageDialog.showErrorDlg(this, sTitle, sError);
    return false;
  }
  if(m_alSerialData !=null){
    setM_alSerialDataCancel((HashMap) m_alSerialData.clone());
  }
  HashMap RowIdAndWillstorenum = null;
  try {
      RowIdAndWillstorenum = loadBodyDataForCard(cardVos);
      if (RowIdAndWillstorenum == null || RowIdAndWillstorenum.size() < 1) {
        MessageDialog.showErrorDlg(this, sTitle, sError);
        return false;
      }
      else {
        ArriveorderItemVO[] itemVO = cardVos.getBodyVo();
        Vector vctItemVO = new Vector();
        int rowCount = RowIdAndWillstorenum.size();
        for (int j = 0; j < itemVO.length; j++) {
          if (RowIdAndWillstorenum.containsKey(itemVO[j].getPrimaryKey())) {
            vctItemVO.add(itemVO[j]);
          }
        }
        if (vctItemVO == null || vctItemVO.size() < 1) {
          MessageDialog.showErrorDlg(this, sTitle, sError);
          return false;
        }
        
        ArriveorderItemVO[] temp = new ArriveorderItemVO[vctItemVO.size()];
        vctItemVO.copyInto(temp);
        cardVos.setChildrenVO(temp);
        
      }
    }
  catch (BusinessException e) {
      MessageDialog.showErrorDlg(this, sTitle, e.getMessage());
    }
  SerialAllocationDlg temp = getSerialAllocationDlg(cardVos,RowIdAndWillstorenum);
  if (temp != null) {
      int iRetType = temp.showModal();
      if (iRetType == UIDialog.ID_OK) {
        showHintMessage(m_lanResTool.getStrByID("40040301", "UPT40040301-000138")/* @res"���кŷ������!" */);
        return true;
      }
    }
  return false;
}
/**
 * �Ƿ���Դί��
 * @return
 */
private boolean isFromSC() {
	return getBillCardPanel().getBillModel().getValueAt(0, "cupsourcebilltype")!=null &&
			  getBillCardPanel().getBillModel().getValueAt(0, "cupsourcebilltype").toString().equals(BillTypeConst.SC_ORDER);
}


/**
 * �õ��������еĿ����ɿ�Ƭ�����㷨ͬ�������
 * <p>
 * <b>examples:</b>
 * <p>
 * ʹ��ʾ��
 * <p>
 * <b>����˵��</b>
 * @param bID
 * @param bTS
 * @return
 * <p>
 * @author lixiaodong
 * @throws BusinessException 
 * @time 2008-6-21 ����11:19:42
 */
public HashMap loadBodyDataForCard(ArriveorderVO arriveorderVO) throws BusinessException {
  HashMap RowIdAndWillstorenum  = new HashMap();
  try {
    if (arriveorderVO == null) {
      return null;
    }

    // ��ѯ�������ݲ��ٲ�ѯ
    Object[] oaRet = null;
    if (arriveorderVO.getChildrenVO() != null) {

      /*����ArrayList�ṹ
       *   |-String,��˾����
       *   |-String,��ѯ������
       *   |-UFBoolean,���������Ƿ�����
       *   |-String,����ԱID
       */
      java.util.ArrayList listPara = new java.util.ArrayList();
      listPara.add(getCorpPrimaryKey());
      listPara.add("carriveorderid = '"+arriveorderVO.getHeadVO().getPrimaryKey()+"'");
      listPara.add(new UFBoolean(m_bQcEnabled));
      listPara.add(ClientEnvironment.getInstance().getUser().getPrimaryKey());

      oaRet = ArriveorderHelper.queryVOsForIc(listPara);
      if (oaRet != null && oaRet[0] != null && ((Object[]) oaRet[0]).length > 0 &&oaRet[1] != null && ((Object[]) oaRet[0]).length > 0 ) {
        nc.vo.rc.receive.ArriveorderItemVO[] tmpBodyVo = (ArriveorderItemVO[]) oaRet[1];
        if (tmpBodyVo != null) {
          for (int i = 0; i < tmpBodyVo.length; i++) {
            if(PuPubVO.getUFDouble_ZeroAsNull(tmpBodyVo[i].getNwillstorenum()) != null){
            	if(RowIdAndWillstorenum.containsKey(tmpBodyVo[i].getPrimaryKey())){
            		UFDouble oldValue = (UFDouble)RowIdAndWillstorenum.get(tmpBodyVo[i].getPrimaryKey());
            		RowIdAndWillstorenum.put(tmpBodyVo[i].getPrimaryKey(), 
            				oldValue.add(tmpBodyVo[i].getNwillstorenum()));
            	}else
            		RowIdAndWillstorenum.put(tmpBodyVo[i].getPrimaryKey(), tmpBodyVo[i].getNwillstorenum());
            }
          }
        }else{
          return null;
        }
      }else{
        return null;
      }

    }
  } catch (Exception e) {
    SCMEnv.out(e);
    throw new BusinessException(e.getMessage());
  }
  return RowIdAndWillstorenum;
}

private SerialAllocationDlg getSerialAllocationDlg(nc.vo.rc.receive.ArriveorderVO vo,HashMap RowIdAndWillstorenum) {
  try {
    m_dlgSerialAllocation = new SerialAllocationDlg(this,vo,getOperatorId(), getCorpPrimaryKey(),new MyBillData(),m_bQcEnabled,RowIdAndWillstorenum);
    m_dlgSerialAllocation.setName("SerialAllocationDlg");
  } catch (java.lang.Throwable ivjExc) {
    nc.vo.scm.pub.SCMEnv.error(ivjExc);
    showWarningMessage(ivjExc.getMessage());
    return null;
  }
  return m_dlgSerialAllocation;
}

/**
 * ���˵������кŹ����������ʲ�����û���ɿ�Ƭ�ĵ������С�
 * <p>
 * <b>examples:</b>
 * <p>
 * ʹ��ʾ��
 * <p>
 * <b>����˵��</b>
 * @param vo �������ۺ�VO
 * @return ���˺󵽻����ۺ�VO
 * <p>
 * @author lixiaodong
 * @time 2008-6-28 ����01:21:04
 */
private ArriveorderVO filterVos(ArriveorderVO vo) {
  if (vo ==null||!vo.getHeadVO().isAuditted()||vo.getBodyLen() < 1) {
    return null;
  }
  ArriveorderItemVO[] itemVO = vo.getBodyVo();
  Vector vctItemVO  = new Vector();

  String bfaflag = "";//�Ƿ������ɿ�Ƭ
  String isserialmanaflag  = "";//���кŹ�����
  String iscapitalstor  = "";//�Ƿ��ʲ���
  String rowID  = "";//��ID
  String naccumwarehousenum  = "";//�ۼ��������
  String rowNO  = "";//�к�
  String cinventoryname  = "";
  String err  = "";

  int rowCount = getBillCardPanel().getBillModel().getRowCount();
  if(rowCount < 1){
    return null;
  }
  
  for (int i = 0; i < itemVO.length; i++) {
    for (int j = 0; j < rowCount; j++) {
      bfaflag=getBillCardPanel().getBillModel().getValueAt(j, "bfaflag")==null?"":getBillCardPanel().getBillModel().getValueAt(j, "bfaflag").toString();
      isserialmanaflag = getBillCardPanel().getBillModel().getValueAt(j, "isserialmanaflag")==null?"":getBillCardPanel().getBillModel().getValueAt(j, "isserialmanaflag").toString();
      iscapitalstor = PuPubVO.getString_TrimZeroLenAsNull(getBillCardPanel().getBillModel().getValueAt(j, "iscapitalstor"))==null?"":getBillCardPanel().getBillModel().getValueAt(j, "iscapitalstor").toString();
      rowID = getBillCardPanel().getBillModel().getValueAt(j, "carriveorder_bid")==null?"":getBillCardPanel().getBillModel().getValueAt(j, "carriveorder_bid").toString();
      naccumwarehousenum = getBillCardPanel().getBillModel().getValueAt(j, "naccumwarehousenum")==null?"":getBillCardPanel().getBillModel().getValueAt(j, "naccumwarehousenum").toString();
      rowNO = PuPubVO.getString_TrimZeroLenAsNull(getBillCardPanel().getBillModel().getValueAt(j,"crowno"));
      cinventoryname = PuPubVO.getString_TrimZeroLenAsNull(getBillCardPanel().getBillModel().getValueAt(j,"cinventoryname"));
      err  = "��" + rowNO + "��," + cinventoryname + ":";
      
      if(rowID.equalsIgnoreCase(itemVO[i].getPrimaryKey())){
        if ((PuPubVO.getString_TrimZeroLenAsNull(iscapitalstor) == null || "true".equalsIgnoreCase(iscapitalstor))) {
          if ("true".equalsIgnoreCase(isserialmanaflag)) {
            if(!"true".equalsIgnoreCase(bfaflag)){
              if (InvTool.isAsset((String) getBillCardPanel().getBillModel().getValueAt(j, "cbaseid"))) {// �ʲ�����
                if (!(new UFDouble(naccumwarehousenum.toString().trim()).compareTo(new UFDouble(0)) > 0)) {// �ۼ��������������
                  vctItemVO.add(itemVO[i]);
                }else{
                  SCMEnv.out(err + " �ۼ��������������" + "\n");  
                }
              }else{
                SCMEnv.out(err + " ���ʲ�����" + "\n");
              }
            }else{
              SCMEnv.out(err + " �����ɿ�Ƭ" + "\n");
            }
          }else{
            SCMEnv.out(err + " �����кŹ�����" + "\n");
          }
        }else{
          SCMEnv.out(err + " ���ʲ���" + "\n");
        }
      }
    }
  }
  if(vctItemVO==null||vctItemVO.size()<1){
    return null;
  }

  ArriveorderItemVO[] temp = new ArriveorderItemVO[vctItemVO.size()];
  vctItemVO.copyInto(temp);
  vo.setChildrenVO(temp);
  return vo;
}

/**
 * ��֯���������ݣ����ú�̨�������������ʲ���Ƭ��
 * <p>
 * <b>examples:</b>
 * <p>
 * ʹ��ʾ��
 * <p>
 * <b>����˵��</b>
 * <p>
 * @author lixiaodong
 * @time 2008-5-21 ����02:23:31
 */
private void onCreateCard() {
  if (!m_bAIMEnabled) {
    MessageDialog.showErrorDlg(this, m_lanResTool.getStrByID("SCMCOMMON","UPPSCMCommon-000059")/*@res "����"*/, m_lanResTool.getStrByID("40040301","UPT40040301-000141")/*@res "�ʲ�ģ��δ���ã��������ɿ�Ƭ!"*/);
    return;
  }

  if (!onSNAssign()) {
    showHintMessage("");
    return;
  }

  try {

    String sRowId = "";// ��ǰѡ����ID
    String sOrderRowId = "";// ��ǰѡ���ж�Ӧ�Ķ�����ID
    Vector vctRowId = new Vector();
    Vector vctOrderRowId = new Vector();
    Vector vctPlanarrvdates = new Vector();
    nc.vo.po.pub.SerialUiVO serialUiVO=new nc.vo.po.pub.SerialUiVO();
    ArriveorderItemVO item = null;
    BillModel bm = getBillCardPanel().getBillModel();
    int iRowCnt =getBillCardPanel().getBodyPanel().getTable().getRowCount();
    if(iRowCnt<1){
      showHintMessage("");
      return;
    }


    //�õ���д�����кŵ�δ���ɿ�Ƭ����
    SerialUiVO[] serial = null;

    StringBuffer rowno=new StringBuffer();
    int noserial=0;
    for (int i = 0; i < iRowCnt; i++) {
      sRowId = (String) getBillCardPanel().getBillModel().getValueAt(i, "carriveorder_bid");
      sOrderRowId = (String) getBillCardPanel().getBillModel().getValueAt(i, "corder_bid");
      serial = (SerialUiVO[]) getM_alSerialData(sRowId, null).get(sRowId);
      if (getM_alSerialData(sRowId, null) == null || getM_alSerialData(sRowId, null).size() < 1 || serial == null) {
        rowno.append((String) getBillCardPanel().getBillModel().getValueAt(i, "crowno") + ",");
        noserial++;
        continue;
      }
      if ("true".equalsIgnoreCase(getBillCardPanel().getBillModel().getValueAt(i, "bfaflag")==null?"":getBillCardPanel().getBillModel().getValueAt(i, "bfaflag").toString())){
        continue;
      }

      if (serial != null && serial.length>0) {
        String[] serialNo=serial[0].getSerialNo(serial);
        if(serialNo==null||serialNo.length<1){//�ӻ�����ȡ���к�
          rowno.append((String) getBillCardPanel().getBillModel().getValueAt(i, "crowno") + ",");
          noserial++;
          continue;
        }
      }

      String bfaflag = "";
      for (int ii = 0; ii < getCacheVOs()[getDispIndex()].getBodyLen();ii++){
        bfaflag=getBillCardPanel().getBillModel().getValueAt(ii, "bfaflag")==null?"":getBillCardPanel().getBillModel().getValueAt(ii, "bfaflag").toString();
        if((!"true".equalsIgnoreCase(bfaflag))&&((String) getBillCardPanel().getBillModel().getValueAt(i, "carriveorder_bid")).equalsIgnoreCase(getCacheVOs()[getDispIndex()].getBodyVo()[ii].getPrimaryKey())){
          String[] temp=serialUiVO.getSerialNo(getCacheVOs()[getDispIndex()].getBodyVo()[ii].getSerialUiVO());

          if(temp==null||temp.length<1){//�ӻ�����ȡ���к�
            temp=serialUiVO.getSerialNo((SerialUiVO[]) getM_alSerialData(sRowId, null).get(getCacheVOs()[getDispIndex()].getBodyVo()[ii].getPrimaryKey()));
          }

          if(temp!=null&&temp.length>0){
            if (!vctRowId.contains(sRowId)) {
              vctRowId.add(sRowId);
              vctPlanarrvdates.add(getBillCardPanel().getHeadItem("dreceivedate").getValue());
              vctOrderRowId.add(sOrderRowId);
            }
          }
        }
      }
    }
    if (PuPubVO.getString_TrimZeroLenAsNull(rowno) != null && noserial==iRowCnt) {
      showHintMessage(m_lanResTool.getStrByID("40040301","UPT40040301-000136")/*@res"δ��д���кŵ��к�:"*/ + rowno.toString().substring(0, rowno.toString().length()-1));
      return;
    }

    if (vctRowId == null || vctRowId.size() < 1 || vctPlanarrvdates == null || vctPlanarrvdates.size() < 1 || vctRowId.size() != vctPlanarrvdates.size()) {
      showHintMessage(m_lanResTool.getStrByID("40040301","UPT40040301-000135")/*@res"û�п����ɿ�Ƭ�ĵ�������!"*/);
      return;
    }

    int iSize = getBillCardPanel().getRowCount();
    if(iSize <= 0){
      showHintMessage("");
      return;
    }
    ArriveorderItemVO[] arriveorderItemVO = null;
    Vector vctItemVO=new Vector();
    for(int i=0 ;i < iSize ; i++){
      item = (ArriveorderItemVO) bm.getBodyValueRowVO(i,ArriveorderItemVO.class.getName());
      sRowId = (String) getBillCardPanel().getBillModel().getValueAt(i, "carriveorder_bid");// ��ID
      if ((SerialUiVO[]) getM_alSerialData(sRowId, null).get(sRowId) != null
          && ((SerialUiVO[]) getM_alSerialData(sRowId, null).get(sRowId)).length > 0
          && ((SerialUiVO[]) getM_alSerialData(sRowId, null).get(sRowId))[0].getVstartsn() != null
          && vctRowId.contains(sRowId)) {
        item.setSerialUiVO((SerialUiVO[]) getM_alSerialData(sRowId, null).get(sRowId));
        vctItemVO.add(item);
      }
    }

    if(vctItemVO==null||vctItemVO.size()<1){
      showHintMessage(m_lanResTool.getStrByID("40040301","UPT40040301-000135")/*@res"û�п����ɿ�Ƭ�ĵ�������!"*/);
      return;
    }

    ArriveorderVO vo = new ArriveorderVO();
    arriveorderItemVO = new ArriveorderItemVO[vctItemVO.size()];
    vctItemVO.copyInto(arriveorderItemVO);
    vo.setParentVO(getCacheVOs()[getDispIndex()].getHeadVO());
    vo.setChildrenVO(arriveorderItemVO);


    ArrayList arr=new ArrayList();
    arr.add(vo);
    arr.add(getOperatorId());//��ǰ��¼���û���ID
    arr.add(getSysDate());//��ǰ��¼����
    arr.add(PoPublicUIClass.getLoginPk_corp());//��ǰ��¼��˾

    // ���ú�̨�������������ʲ���Ƭ
    IArriveorder bo2 = (IArriveorder) nc.bs.framework.common.NCLocator.getInstance().lookup(IArriveorder.class.getName());
    bo2.onCreateCard(arr);


    // ����ˢ�½��棬����"bfaflag"��"TS"�ȱ�������
    String billID = vo.getHeadVO().getPrimaryKey();
    getCacheVOs()[getDispIndex()] = ArriveorderHelper.findByPrimaryKey(billID);
    /*���ص���*/
    try {
      loadDataToCard();
      refreshCardData();
    } catch (Exception e) {
      showHintMessage("�ɹ�,�����ص���ʱ�����쳣,��ˢ�½����ٽ�����������");
    }
    /*ˢ�°�ť״̬*/
    setButtonsState();


    showHintMessage(m_lanResTool.getStrByID("40040301","UPT40040301-000134")/*@res"���ɿ�Ƭ�ɹ�!"*/); 
  } catch (BusinessException e) {
    SCMEnv.out(e.getMessage());
    showHintMessage(e.getMessage());
    MessageDialog.showErrorDlg(this, m_lanResTool.getStrByID("SCMCOMMON","UPPSCMCommon-000059")/*@res "����"*/, e.getMessage());
  } catch (Exception e) {
    SCMEnv.out(e);
    showHintMessage(e.getMessage());
    MessageDialog.showErrorDlg(this, m_lanResTool.getStrByID("SCMCOMMON","UPPSCMCommon-000059")/*@res "����"*/, e.getMessage());
  }
}

/**
 * ��֯���������ݣ�ɾ�����������ɵ��ʲ���Ƭ��
 * <p>
 * <b>examples:</b>
 * <p>
 * ʹ��ʾ��
 * <p>
 * <b>����˵��</b>
 * <p>
 * @author lixiaodong
 * @time 2008-5-21 ����02:29:42
 */
private void onDeleteCard() {
  try {

    int iSize = getBillCardPanel().getRowCount();
    if(iSize <= 0){
      return;
    }
    if (getCacheVOs() == null || getCacheVOs().length <= 0) {
      showHintMessage(m_lanResTool.getStrByID("40040301","UPT40040301-000133")/*@res"û���������ʲ���Ƭ����!"*/);
      return;
    }

    ArriveorderVO m_Vos = getCacheVOs()[getDispIndex()];
    if (m_Vos == null) {
      showHintMessage(m_lanResTool.getStrByID("40040301","UPT40040301-000133")/*@res"û���������ʲ���Ƭ����!"*/);
      return;
    }
    if(m_Vos.getBodyLen()<1){
      showHintMessage(m_lanResTool.getStrByID("40040301","UPT40040301-000133")/*@res"û���������ʲ���Ƭ����!"*/);
      return;
    }
    ArriveorderHeaderVO head = null;
    ArriveorderItemVO[] items = null;

    items = m_Vos.getBodyVo();
    head=m_Vos.getHeadVO();
    Vector v =  new Vector();// ���������ɿ�Ƭ�ĵ�������
    Vector rowID =  new Vector();// ���������ɿ�Ƭ�ĵ�������ID
    for (int i = 0; i < iSize; i++) {
      if (items[i].getBfaflag() == null || !items[i].getBfaflag().booleanValue()) {// ��ǰѡ�����Ƿ������ʲ�Ƭ��־
      } else {
        v.add(items[i]);
        rowID.add(items[i].getPrimaryKey());
      }
    }
    if(v==null||v.size()<1){
      showHintMessage(m_lanResTool.getStrByID("40040301","UPT40040301-000133")/*@res"û���������ʲ���Ƭ����!"*/);
      return;
    }

    //��֯�µĵ������ۺ�VO������̨
    ArriveorderVO vo = new ArriveorderVO();
    ArriveorderItemVO[] arriveorderItemVO = new ArriveorderItemVO[v.size()];
    v.copyInto(arriveorderItemVO);
    vo.setParentVO(getCacheVOs()[getDispIndex()].getHeadVO());
    vo.setChildrenVO(arriveorderItemVO);


    // ���ù̶��ʲ��Ľӿڣ�ɾ�����������ɵ��ʲ���Ƭ��ɾ�����ο�浥�ݡ�
    ArrayList arr=new ArrayList();
    arr.add(vo);
    arr.add(getOperatorId());//��ǰ��¼���û���ID
    arr.add(getSysDate());//��ǰ��¼����

    IArriveorder bo2 = (IArriveorder) nc.bs.framework.common.NCLocator.getInstance().lookup(IArriveorder.class.getName());
    bo2.onDeleteCard(arr);


    // ����ˢ�½��棬����"bfaflag"��"TS"�ȱ�������
    String billID = vo.getHeadVO().getPrimaryKey();
    getCacheVOs()[getDispIndex()] = ArriveorderHelper.findByPrimaryKey(billID);

    /*���ص���*/
    loadDataToCard();
    /*ˢ�°�ť״̬*/
    setButtonsState();

    showHintMessage(m_lanResTool.getStrByID("common","UCH006")/*@res "ɾ���ɹ�"*/);
  } catch (Exception e) {
    showHintMessage(e.getMessage());
  }
}

public void setM_alSerialData(String sRowId, SerialUiVO[] temp) {
  if(sRowId == null){
    m_alSerialData = null;
  }
  if (m_alSerialData != null) {
    if (m_alSerialData.containsKey(sRowId)) {
      m_alSerialData.remove(sRowId);
    }
    m_alSerialData.put(sRowId, temp);
  }
}

public HashMap getM_alSerialData(String sRowId, UFDouble willstorenum) {
  if (m_alSerialData == null || m_alSerialData.size() < 1) {
    SerialUiVO[] temp = new SerialUiVO[m_billPanel.getBillModel().getRowCount()];
    temp[0] = new SerialUiVO();
    temp[0].setNqty(willstorenum == null ? 0 : PuTool.getQty(willstorenum.doubleValue()));

    m_alSerialData = new HashMap();
    if (!m_alSerialData.containsKey(sRowId)) {
      m_alSerialData.put(sRowId, temp);
    }
  }
  return m_alSerialData;
}

/**
 * ����������������ʼ����ӡ������
 * <p>
 * <b>examples:</b>
 * <p>
 * ʹ��ʾ��
 * <p>
 * <b>����˵��</b>
 * @return
 * <p>
 * @author lixiaodong
 * @time 2008-8-4 ����11:38:49
 */
private SplitParams[] initSplitParams() {
  //������ӡ����
  SplitParams[] paramvos = null;

  if (paramvos == null || paramvos.length < 1) {
    paramvos = new SplitParams[2];
    paramvos[0] = new SplitParams("cwarehouseid", NCLangRes.getInstance().getStrByID("common", "UC000-0002236"), 0, null, true);// UC000-0002236=�ջ��ֿ�
    paramvos[1] = new SplitParams("cinvclid", NCLangRes.getInstance().getStrByID("common", "UC000-0001443"), 0, null, true);// UC000-0001443=�������
  }

//  // �ӽ����ȡ�����������
//  int iLen = vo.getBodyLen();
//  String[] saBid = new String[iLen];
//  String cinvclid = null;
//  String cbid = null;
//  for (int i = 0; i < getBillCardPanel().getBillModel().getRowCount(); i++) {
//    cinvclid = getBillCardPanel().getBillModel().getValueAt(i, "cinvclid") == null ? null : getBillCardPanel().getBillModel().getValueAt(i, "cinvclid").toString();
//    cbid = getBillCardPanel().getBillModel().getValueAt(i, "carriveorder_bid") == null ? null : getBillCardPanel().getBillModel().getValueAt(i, "carriveorder_bid").toString();
//    for (int ii = 0; ii < iLen; ii++) {
//      saBid[ii] = vo.getBodyVo()[ii].getPrimaryKey();
//      if (PuPubVO.getString_TrimZeroLenAsNull(saBid[ii]) != null && PuPubVO.getString_TrimZeroLenAsNull(saBid[ii]).equalsIgnoreCase(cbid)) {
//        vo.getBodyVo()[ii].setAttributeValue("cinvclid", cinvclid);
//      }
//    }
//  }
//  if (vo != null) {
//    setCacheVOs(new ArriveorderVO[] {vo});
//  }
  return paramvos;
}
public boolean onEditAction(int action) {
	if(action == BillScrollPane.ADDLINE){
		return false;
	}
	return true;
}
/*
 * ���õ��ݿ�Ƭ�Ҽ��˵��в����밴ť�顰�в�����Ȩ����ͬ
 */
private void setPopMenuBtnsEnable(boolean b) {
	m_btnDelLine.setEnabled(false);
	m_btnCpyLine.setEnabled(false);
	m_btnPstLine.setEnabled(false);
	m_btnPstLineTail.setEnabled(false);
	m_miReSortRowNo.setEnabled(false);
	m_btnReSortRowNo.setEnabled(false);
	m_btnCardEdit.setEnabled(false);
	m_miCardEdit.setEnabled(false);

	updateButtonsAll();


	// û�з����в���Ȩ��
	if (m_btnLines == null || m_btnLines.getChildCount() == 0) {
		getBillCardPanel().getBodyPanel().getMiCopyLine().setEnabled(false);
		getBillCardPanel().getBodyPanel().getMiDelLine().setEnabled(false);
		getBillCardPanel().getBodyPanel().getMiPasteLine().setEnabled(false);
		getBillCardPanel().getBodyPanel().getMiPasteLineToTail().setEnabled(false);
		m_miReSortRowNo.setEnabled(false);
		m_miCardEdit.setEnabled(false);
	}
	// �����в���Ȩ��
	else {
		getBillCardPanel().getBodyPanel().getMiCopyLine().setEnabled(b);
		getBillCardPanel().getBodyPanel().getMiDelLine().setEnabled(b);
		getBillCardPanel().getBodyPanel().getMiPasteLine().setEnabled(b);
		// ճ������β��ճ���������߼���ͬ
		getBillCardPanel().getBodyPanel().getMiPasteLineToTail().setEnabled(b);
		//
		m_miReSortRowNo.setEnabled(b);
		m_btnCardEdit.setEnabled(b);

	}

}

private void setPopMenuBtnsEnable() {
  
  // û�з����в���Ȩ��
  if (m_btnLines == null || m_btnLines.getChildCount() == 0) {
    getBillCardPanel().getBodyPanel().getMiCopyLine().setEnabled(false);
    getBillCardPanel().getBodyPanel().getMiDelLine().setEnabled(false);
    getBillCardPanel().getBodyPanel().getMiPasteLine().setEnabled(false);
    getBillCardPanel().getBodyPanel().getMiPasteLineToTail().setEnabled(false);
    m_miReSortRowNo.setEnabled(false);
    m_miCardEdit.setEnabled(false);
  }
  // �����в���Ȩ��
  else {
    getBillCardPanel().getBodyPanel().getMiCopyLine().setEnabled(isPowerBtn(m_btnCpyLine.getCode()));
    getBillCardPanel().getBodyPanel().getMiDelLine().setEnabled(isPowerBtn(m_btnDelLine.getCode()));
    getBillCardPanel().getBodyPanel().getMiPasteLine().setEnabled(isPowerBtn(m_btnPstLine.getCode()));
    // ճ������β��ճ���������߼���ͬ
    getBillCardPanel().getBodyPanel().getMiPasteLineToTail().setEnabled(isPowerBtn(m_btnPstLineTail.getCode()));
    //
    m_miReSortRowNo.setEnabled(isPowerBtn(m_btnReSortRowNo.getCode()));
    m_btnCardEdit.setEnabled(isPowerBtn(m_btnCardEdit.getCode()));

  }
}

/**
 * ������ѯ�Ի������û�ѯ�ʲ�ѯ������
 * ����û��ڶԻ������ˡ�ȷ��������ô����true,���򷵻�false
 * ��ѯ����ͨ�������StringBuffer���ظ�������
 *
 * @param sqlWhereBuf
 *            �����ѯ������StringBuffer
 * @return �û�ѡȷ������true���򷵻�false
 */
protected boolean askForQueryCondition(StringBuffer sqlWhereBuf)
    throws Exception {

  // para sqlWhereBuf can not be null
  if (sqlWhereBuf == null)
    throw new IllegalArgumentException("askForQueryCondition().sqlWhereBuf can't be null!");

  String strWhere = getQueryConditionDlg().getWhereSQL();//getWhereSql();
  if (PuPubVO.getString_TrimZeroLenAsNull(strWhere) != null){
    strWhere = "(" + strWhere + ") and po_arriveorder.dr=0 ";
  }else{
    strWhere = " and po_arriveorder.dr=0 ";
  }

  sqlWhereBuf.append(strWhere);
  return true;
}
public HashMap getM_alSerialDataCancel() {
  return m_alSerialDataCancel;
}
public void setM_alSerialDataCancel(HashMap serialDataCancel) {
  m_alSerialDataCancel = serialDataCancel;
}

public void checkVprocessbatch(nc.vo.rc.receive.ArriveorderVO[] VOs) {
  try {
    if (VOs == null || VOs.length < 1) {
      return;
    }

    for (int i = 0; i < VOs.length; i++) {
      if(VOs[i].getChildrenVO() == null){
        continue;
      }
      int rownum = VOs[i].getChildrenVO().length;
      boolean flag = true;
      ArriveorderItemVO itemVO = null;
      String cmangid = "";
      String sBaseID = "";
      String sCassId = "";
      Object[] o = null;
      HashMap<String, String> mapMangID = new HashMap<String, String>();
      
      for (int j = 0; j < rownum; j++) {
        itemVO = (ArriveorderItemVO) VOs[i].getChildrenVO()[j];
        cmangid = PuPubVO.getString_TrimZeroLenAsNull(itemVO.getCmangid());
        sBaseID = PuPubVO.getString_TrimZeroLenAsNull(itemVO.getCbaseid());
        sCassId = PuPubVO.getString_TrimZeroLenAsNull(itemVO.getCassistunit());
        if (cmangid != null) {
          if(mapMangID.containsKey(cmangid)){
            if("Y".equalsIgnoreCase(PuPubVO.getString_TrimZeroLenAsNull(mapMangID.get(cmangid)))){
              itemVO.setIswholemanaflag(UFBoolean.TRUE);
            }
          }else{
            o = (Object[]) CacheTool.getCellValue("bd_invmandoc","pk_invmandoc", "wholemanaflag", cmangid);
            if (o != null && o.length > 0 && o[0] != null && o[0].toString().equals("Y")) {
              mapMangID.put(cmangid, o[0].toString());
              itemVO.setIswholemanaflag(UFBoolean.TRUE);
            }else{
              mapMangID.put(cmangid, null);
            }
          }
          // �Ƿ�̶�������
          if(PuTool.isFixedConvertRate(sBaseID, sCassId)){
            itemVO.setIsfixedrate(UFBoolean.TRUE);
          }

        }
      }
    }
  }
  catch (BusinessException be) {
    SCMEnv.out(be.getMessage());
  }

}
private void setReWriteData(ArriveorderVO savevo,ArriveorderVO newvo,ArriveorderVO oldvo) {
  try {
    savevo.setH_arrNumNew(ArrivePubVO.getAccArrNum(newvo));
    savevo.setH_arrNumOld(ArrivePubVO.getAccArrNum(oldvo));
    savevo.setH_wasNumNew(ArrivePubVO.getAccWasNum(newvo));
    savevo.setH_wasNumOld(ArrivePubVO.getAccWasNum(oldvo));
    savevo.setH_givNumNew(ArrivePubVO.getAccGivNum(newvo));
    savevo.setH_givNumOld(ArrivePubVO.getAccGivNum(oldvo));
    savevo.setH_accArrNumBB1New(ArrivePubVO.getAccArrNumBB1(newvo));
    savevo.setH_accArrNumBB1Old(ArrivePubVO.getAccArrNumBB1(oldvo));
  }
  catch (BusinessException e) {
    nc.vo.scm.pub.SCMEnv.out(e);
  }
}
/**
 * <p>
 * ���򷽷�������Ҫ����ĵ�ǰVO����VO����
 * 
 * @since V50
 */
public Object[] getRelaSortObjectArrayBody() {
	if (getCacheVOs() != null && getCacheVOs().length > 0 && getDispIndex() >= 0 && getCacheVOs()[getDispIndex()] != null
			&& getCacheVOs()[getDispIndex()].getChildrenVO() != null) {
		return getCacheVOs()[getDispIndex()].getChildrenVO();
	}
	return null;
}
/**
 *���󷵻ص��Ƶ���ʱ����Ҫ�ڴ���������ˡ��������ڡ�����ʱ��
 */
private void setAuditInfo() {
	Integer iVoStatus = BillStatus.FREE;
	if(getBillCardPanel().getHeadItem("ibillstatus") != null){
		iVoStatus = PuPubVO.getInteger_NullAs(getBillCardPanel().getHeadItem("ibillstatus").getValueObject(), BillStatus.FREE);
	}else if (getBillCardPanel().getTailItem("ibillstatus") != null){
		iVoStatus = PuPubVO.getInteger_NullAs(getBillCardPanel().getTailItem("ibillstatus").getValueObject(), BillStatus.FREE);
	}
	if(BillStatus.AUDITFAIL.intValue() == iVoStatus.intValue()){
		if (getBillCardPanel().getTailItem("cauditpsn") != null){
			getBillCardPanel().getTailItem("cauditpsn").setValue(null);
		}else if(getBillCardPanel().getHeadItem("cauditpsn") != null){
			getBillCardPanel().getHeadItem("cauditpsn").setValue(null);
		}
		if (getBillCardPanel().getTailItem("dauditdate") != null){
			getBillCardPanel().getTailItem("dauditdate").setValue(null);
		}else if(getBillCardPanel().getHeadItem("dauditdate") != null){
			getBillCardPanel().getHeadItem("dauditdate").setValue(null);
		}
		if (getBillCardPanel().getTailItem("taudittime") != null){
			getBillCardPanel().getTailItem("taudittime").setValue(null);
		}else if(getBillCardPanel().getHeadItem("taudittime") != null){
			getBillCardPanel().getHeadItem("taudittime").setValue(null);
		}
	}
}
private BillTempletVO m_billTempletVO = null;
/**
 * ��ȡ�ýڵ��ģ������
 */
private BillTempletVO getBillTempletVO(){
	if(m_billTempletVO==null){
		m_billTempletVO = BillUIUtil.getDefaultTempletStatic(BillTypeConst.PO_ARRIVE, null, 
				getOperatorId(), getCorpPrimaryKey(),null ,null);	
		// m_billPanel.loadTemplet(getModuleCode(), null, getOperatorId(), getCorpPrimaryKey(),new MyBillData());
	}
	return m_billTempletVO;
}
/**
 * @function �õ�����¼�밴ť
 *
 * @author QuSida
 *
 * @return ButtonObject
 *
 * @date 2010-8-28 ����10:03:53
 */
private ButtonObject getBoInfoCost(){
	if(boInfoCost == null){
		//����¼�밴ť add by QuSida 2010-8-10  ����ɽ���ܣ�
		boInfoCost = new ButtonObject("����¼��","����¼��","����¼��");
		return boInfoCost;
	}
	else return boInfoCost;
}
/**
 * @function ������Ϣ¼�빦��
 *
 * @author QuSida
 * 
 * @return void
 *
 * @date 2010-8-28 ����10:03:17
 */
private void onBoInfoCost() {
	InformationCostVO[] ivos = (InformationCostVO[] )getBillCardPanel().getBillModel("jj_scm_informationcost").getBodyValueVOs(InformationCostVO.class.getName());
	ArrayList voList = new ArrayList();
	for (int i = 0; i < ivos.length; i++) {
		//modify by ������ 2010-10-12 ԭ�ж���������  
		if(ivos[i] != null){
//		if(ivos[i].getPrimaryKey() == null||ivos[i].getPrimaryKey().length() == 0){
			voList.add(ivos[i]);
		}
	}
	InfoCostPanel c = null;
	if(voList.size()!=0&&voList!=null){
		InformationCostVO[]  ivos1 = new InformationCostVO[voList.size()]; 
	voList.toArray(ivos1);
	c = new InfoCostPanel(getBillCardPanel(),ivos1);
//	if(ivos == null || ivos.length == 0)
			
		}
	else 
		
	c = new InfoCostPanel(getBillCardPanel());
	// �򿪷���¼�����
	c.showModal();
//	if(c.showModal()==UIDialog.ID_OK){//showModalΪ��ʾDialog,����÷�������ֵΪ1,��ô��ʾ�û�����ȷ��.
//		20101010 15:26 ÷��.���
//	}
	// ������¼�����ر�ʱ,��¼������ݴ�ŵ�������Ϣҳǩ��
	if (c.isCloseOK()) {
		InformationCostVO[] infoCostVOs = c.getInfoCostVOs();
		if (infoCostVOs != null && infoCostVOs.length != 0){
			// ������¼������vo���鲻Ϊ��ʱ,��vo�浽����¼��ҳǩ��
			if(infoCostVOs.length!=0&&infoCostVOs!=null){
				
				   int temp = getBillCardPanel().getBillModel("table").getRowCount();
				   UFDouble arrmny = null;
				   UFDouble mny = null;
				    arrnumber = new UFDouble(0.0);
			    for (int i = 0; i < temp; i++) {
			    	arrnumber = arrnumber.add(new UFDouble(getBillCardPanel().getBillModel("table").getValueAt(i,"narrvnum").toString()));    			    	  
				}

				for (int i = 0; i < infoCostVOs.length; i++) {
					infoCostVOs[i].setNnumber(arrnumber);
					UFBoolean ismny = (UFBoolean)infoCostVOs[i].getAttributeValue("ismny");
					if(ismny == null || !ismny.booleanValue()){
				//add by  ������ 2010-10-12 begin
						if(arrnum == null){//�����ۼƴ������
							arrnum = new UFDouble(0.0);
						}
				//add by  ������ 2010-10-12 end
			    	mny = new UFDouble(infoCostVOs[i].getAttributeValue("noriginalcurprice").toString()).multiply(arrnumber);
			    	arrmny  = new UFDouble(infoCostVOs[i].getAttributeValue("noriginalcurprice").toString()).multiply(arrnumber.add(arrnum));
//			    	taxmny = new UFDouble(infoCostVOs[i].getAttributeValue("noriginalcurtaxprice").toString()).multiply(arrnumber);
//			    	infoCostVOs[i].setAttributeValue("noriginalcursummny", taxmny);
			    	infoCostVOs[i].setAttributeValue("noriginalcurmny", mny);
//			    	infoCostVOs[i].setAttributeValue("ninvoriginalcursummny", taxmny);
			    	infoCostVOs[i].setAttributeValue("ninvoriginalcurmny", arrmny);	
					}else{
						infoCostVOs[i].setAttributeValue("noriginalcurprice", infoCostVOs[i].getNoriginalcurmny().div(arrnumber));	
						infoCostVOs[i].setAttributeValue("ninvoriginalcurmny", infoCostVOs[i].getNoriginalcurmny());	
					}
				}
				// by ������ 2010-10-12 ע�͵�����	
//				InformationCostVO[] newVOs = new InformationCostVO[vos.length+infoCostVOs.length];
//				if(vos.length != 0 && vos != null){
//					//int temp = vos.length>infoCostVOs.length?vos.length:infoCostVOs.length;
//					
//					System.arraycopy(vos, 0, newVOs, 0, vos.length);
//					
//					System.arraycopy(infoCostVOs, 0, newVOs, vos.length, infoCostVOs.length);
//					
//					
//						getBillCardPanel().getBillData().setBodyValueVO(
//								"jj_scm_informationcost", newVOs);
//					//	getBillCardPanel().getBillModel("jj_scm_informationcost").execLoadFormula();
//				}
//				else 
				getBillCardPanel().getBillData().setBodyValueVO(
						"jj_scm_informationcost", infoCostVOs);
				getBillCardPanel().getBillModel("jj_scm_informationcost").execLoadFormula();
			}		
		}
	}
}
private void costInfoDistribute(InformationCostVO[] infoCostVOs,OrderVO orderVO){
//	if(infoCostVOs==null||infoCostVOs.length==0||orderVO==null){
//		SCMEnv.out("Warning:Params is null!");
//		return;
//	}
//	UFDouble number = new UFDouble(0.0);
//	UFDouble laborSummy = new UFDouble(0.0);
//	for (int i = 0; i < infoCostVOs.length; i++) {
//		if(infoCostVOs[i].getNoriginalcursummny()!=null)
//		laborSummy = laborSummy.add(infoCostVOs[i].getNoriginalcursummny());
//	}
//	OrderItemVO[] childVOs = (OrderItemVO[])orderVO.getChildrenVO();
//	if(childVOs.length == 0||childVOs == null){
//		SCMEnv.out("Warning:OrderItemVO[] is null!");
//		return ;
//	}
//	for (int i = 0; i < childVOs.length; i++) {
//          if(childVOs[i].getNordernum()!=null)
//        	  number = number.add(childVOs[i].getNordernum());
//	}
//	UFDouble templaborSummy = new UFDouble(0.0);
//	for (int i = 0; i < childVOs.length; i++) {
//		  if(i!=childVOs.length-1){
//		UFDouble param1 = childVOs[i].getNordernum().div(
//				number).multiply(laborSummy);        
//        	childVOs[i].setNoriginaltaxpricemny(childVOs[i].getNoriginaltaxpricemny().add(param1));
//        	childVOs[i].setNorgtaxprice(childVOs[i].getNoriginaltaxpricemny().div(childVOs[i].getNordernum()));
//        	templaborSummy = templaborSummy.add(param1);
//		  }else {
//				UFDouble param1 = laborSummy.sub(templaborSummy); 
//				childVOs[i].setNoriginaltaxpricemny(childVOs[i].getNoriginaltaxpricemny().add(param1));
//            	childVOs[i].setNorgtaxprice(childVOs[i].getNoriginaltaxpricemny().div(childVOs[i].getNordernum()));
//		  }
//	}
}
   

}