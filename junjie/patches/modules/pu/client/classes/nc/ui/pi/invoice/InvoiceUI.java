package nc.ui.pi.invoice;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import nc.bs.bd.b21.BusinessCurrencyRateUtil;
import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.itf.pi.IDataPowerForInv;
import nc.itf.pi.IInvoiceD;
import nc.itf.scm.cenpur.service.TempTableUtil;
import nc.itf.uap.pf.IPFMetaModel;
import nc.ui.bd.b21.CurrParamQuery;
import nc.ui.bd.b39.PhaseRefModel;
import nc.ui.ml.NCLangRes;
import nc.ui.pf.query.ICheckRetVO;
import nc.ui.pi.InvoiceHelper;
import nc.ui.pi.pub.PiPqPublicUIClass;
import nc.ui.po.oper.IButtonConstPo;
import nc.ui.po.pub.PoPublicUIClass;
import nc.ui.po.ref.OtherRefModel;
import nc.ui.pps.PricStlHelper;
import nc.ui.pr.pray.IButtonConstPr;
import nc.ui.pu.pub.PIPluginUI;
import nc.ui.pu.pub.POPubSetUI;
import nc.ui.pu.pub.POPubSetUI2;
import nc.ui.pu.pub.PuGetUIValueTool;
import nc.ui.pu.pub.PuTool;
import nc.ui.pu.pub.PubHelper;
import nc.ui.pu.pub.PurPsnRefModel;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.FramePanel;
import nc.ui.pub.ToftPanel;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UICheckBox;
import nc.ui.pub.beans.UIComboBox;
import nc.ui.pub.beans.UIMenuItem;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.beans.UITextField;
import nc.ui.pub.bill.BillActionListener;
import nc.ui.pub.bill.BillBodyMenuListener;
import nc.ui.pub.bill.BillCardBeforeEditListener;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.bill.BillData;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillEditListener;
import nc.ui.pub.bill.BillEditListener2;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillItemEvent;
import nc.ui.pub.bill.BillModel;
import nc.ui.pub.bill.BillScrollPane;
import nc.ui.pub.bill.BillTableMouseListener;
import nc.ui.pub.bill.BillUIUtil;
import nc.ui.pub.bill.DefaultCurrTypeBizDecimalListener;
import nc.ui.pub.bill.DefaultCurrTypeDecimalAdapter;
import nc.ui.pub.bill.IBillData;
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
import nc.ui.scm.file.DocumentManager;
import nc.ui.scm.ic.freeitem.FreeItemRefPane;
import nc.ui.scm.plugin.InvokeEventProxy;
import nc.ui.scm.pub.BillTools;
import nc.ui.scm.pub.BusiBillManageTool;
import nc.ui.scm.pub.CacheTool;
import nc.ui.scm.pub.CollectSettingDlg;
import nc.ui.scm.pub.bill.ButtonTree;
import nc.ui.scm.pub.billutil.BillCardPanelHelper;
import nc.ui.scm.pub.billutil.ClientCacheHelper;
import nc.ui.scm.pub.def.DefSetTool;
import nc.ui.scm.pub.panel.RelationsCal;
import nc.ui.scm.pub.print.ISetBillVO;
import nc.ui.scm.pub.print.ScmPrintTool;
import nc.ui.scm.pub.query.SCMQueryConditionDlg;
import nc.ui.scm.pub.report.BillRowNo;
import nc.ui.scm.pub.sourceref.SourceRefDlg;
import nc.vo.bd.b06.PsndocVO;
import nc.vo.ic.pub.vmi.VmiSumHeaderVO;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.pi.ConstantVO;
import nc.vo.pi.HintMessageException;
import nc.vo.pi.InvoiceHeaderVO;
import nc.vo.pi.InvoiceItemVO;
import nc.vo.pi.InvoiceVO;
import nc.vo.pi.NormalCondVO;
import nc.vo.po.pub.Operlog;
import nc.vo.po.pub.OrderPubVO;
import nc.vo.pps.PricParaVO;
import nc.vo.pu.exception.RwtPiToPoException;
import nc.vo.pu.exception.RwtPiToScException;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CommonConstant;
import nc.vo.pub.VOStatus;
import nc.vo.pub.ValidationException;
import nc.vo.pub.bill.BillTempletVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.para.SysInitVO;
import nc.vo.pub.pfflow04.MessagedriveVO;
import nc.vo.pub.query.ConditionVO;
import nc.vo.scm.constant.ScmConst;
import nc.vo.scm.ic.bill.FreeVO;
import nc.vo.scm.ic.bill.InvVO;
import nc.vo.scm.pu.BillStatus;
import nc.vo.scm.pu.BillTypeConst;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.scm.pu.VariableConst;
import nc.vo.scm.pub.SCMEnv;
import nc.vo.scm.pub.session.ClientLink;
import nc.vo.scm.pub.smart.ObjectUtils;
import nc.vo.wfengine.engine.exception.EngineException;
import nc.vo.wfengine.engine.exception.TaskInvalidateException;

/**
 * ��Ʊά��UI ���ߣ���ӡ��
 * 
 * @version ����޸�����
 * @see ��Ҫ�μ���������
 * @since �Ӳ�Ʒ����һ���汾�����౻��ӽ���������ѡ��
 * <li><b>����V56���󣬷�Ʊ�������ӡ�������</b></li>
 */
public class InvoiceUI extends nc.ui.pub.ToftPanel implements BillEditListener, BillEditListener2,
    BillCardBeforeEditListener, BillTableMouseListener, BillBodyMenuListener, ICheckRetVO, ListSelectionListener,
    ISetBillVO, nc.ui.scm.pub.bill.IBillExtendFun, IBillRelaSortListener2, ILinkMaintain,// �����޸�
    ILinkAdd,// ��������
    ILinkApprove,// ������
    ILinkQuery,// ������
    BillActionListener {
  
  String sContinueBillTypeName = "";
  boolean feeFlag = false;//���÷�Ʊ��˱�־ add by QuSida (��ɽ����) 2010-9-26
  boolean bIswaitaudit = false;

  // {ҵ����������=�Ƿ�����������������Ӧ��}
  HashMap<String, UFBoolean> m_mapBusitypeAppriveDrive = null;

  // since v55,�����к�
  UIMenuItem m_miReSortRowNo = null;

  private ButtonObject m_btnReSortRowNo = null;

  UIMenuItem m_miCardEdit = null;

  private ButtonObject m_btnCardEdit = null;
  
  /**
   * ���ο������֧�� by zhaoyha at 2009.1.19
   */
  private InvokeEventProxy invokeEventProxy;
  
  /**
   * ���ο������֧�� by zhaoyha at 2009.1.19
   */
  private PIPluginUI pluginui;

  // since v55,�����кż���
  class IMenuItemListener implements ActionListener {
    public void actionPerformed(ActionEvent event) {
      InvoiceUI.this.onMenuItemClick(event);
    };
  }

  class MyBillData implements IBillData {
    public void prepareBillData(nc.ui.pub.bill.BillData bd) {
      InvoiceUI.this.initRefpane(bd);
    }
  }

  // ��ť��ʵ��,since v51
  private ButtonTree m_btnTree = null;

  private final static int INV_PANEL_CARD = 0;

  private final static int INV_PANEL_LIST = 1;

  // ĳҵ�������Ƿ������Ƶ���
  private boolean m_bCouldMaked;

  // �Ƿ��������й���ѯ,��ȷ��ˢ�°�ť�Ƿ����
  private boolean m_bEverQueryed = false;

  // �Ƿ������빺��(���������빺��)
  private boolean m_bAdd = false;

  // �Ƿ�������Ƶ���
  private boolean isAllowedModifyByOther = false;

  // ��Ʊ����ʱ�ԡ��ڼ족״̬���εĿ��Ʒ�ʽ:�����ơ���ʾ�����ƣ�Ĭ��Ϊ�����ơ�
  private String m_sAuditControlMode = null;

  private ButtonObject m_bizButton = null;// ��ǰҵ�����Ͱ�ť

  // ��Ϣ�����������水ť
  private ButtonObject m_btnAudit = new ButtonObject(nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
      "UC001-0000027")/* @res "����" */,
      nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UC001-0000027")/*
                                                                             * @res
                                                                             * "����"
                                                                             */, 2, "����"); /*-=notranslate=-*/

  private ButtonObject m_btnUnAudit = new ButtonObject(nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
      "UC001-0000028")/* @res "����" */,
      nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPP40040401-000149")/*
                                                                                     * @res
                                                                                     * "ִ���������"
                                                                                     */, 5, "����"); /*-=notranslate=-*/

  // ҵ�������鰴ť
  private ButtonObject m_btnInvBillBusiType = null;// ҵ������

  // �����鰴ť
  private ButtonObject m_btnInvBillNew = null;

  // �����鰴ť
  private ButtonObject m_btnInvBillSave = null;

  // ά���鰴ť
  private ButtonObject m_btnBillMaintain = null;// ά��

  private ButtonObject m_btnInvBillModify = null;// �޸�

  private ButtonObject m_btnInvBillCancel = null;// ȡ��

  private ButtonObject m_btnInvBillDiscard = null;// ɾ��

  private ButtonObject m_btnInvBillCopy = null;// ����

  private ButtonObject m_btnInvBillConversion = null;// ����ת��

  // �в����鰴ť
  private ButtonObject m_btnInvBillRowOperation = null;// �в���

  private ButtonObject m_btnInvBillAddRow = null;// ����

  private ButtonObject m_btnInvBillDeleteRow = null;// ɾ��

  private ButtonObject m_btnInvBillInsertRow = null;// ������

  private ButtonObject m_btnInvBillCopyRow = null;// ������

  private ButtonObject m_btnInvBillPasteRow = null;// ճ����

  private ButtonObject m_btnInvBillPasteRowTail = null;// ճ���е���β

  // ��������
  private ButtonObject btnBillAddContinue = null;

  // ִ���鰴ť
  private ButtonObject m_btnInvBillExcute = null;

  private ButtonObject m_btnSendAudit = null;// ����

  private ButtonObject m_btnInvBillAudit = null;// ���

  private ButtonObject m_btnInvBillUnAudit = null;// ����

  // ��ѯ�鰴ť
  private ButtonObject m_btnInvBillQuery = null;// ��ѯ

  // ����鰴ť
  private ButtonObject m_btnBillBrowsed = null;// ���

  private ButtonObject m_btnInvBillRefresh = null;// ˢ��

  private ButtonObject m_btnInvBillGoFirstOne = null;// ��ҳ

  private ButtonObject m_btnInvBillGoNextOne = null;// ��һҳ

  private ButtonObject m_btnInvBillGoPreviousOne = null;// ��һҳ

  private ButtonObject m_btnInvBillGoLastOne = null;// ĩҳ

  private ButtonObject m_btnInvSelectAll = null;// ȫѡ

  private ButtonObject m_btnInvDeselectAll = null;// ȫ��

  // ��Ƭ��ʾ/�б���ʾ(�л�)
  private ButtonObject m_btnInvShift = null;// ��Ƭ��ʾ/�б���ʾ

  // ��ӡ�����鰴ť
  private ButtonObject m_btnPrints = null;

  private ButtonObject m_btnInvBillPreview = null;// Ԥ��

  private ButtonObject m_btnInvBillPrint = null;// ��ӡ

  private ButtonObject btnBillCombin = null;// �ϲ���ʾ

  // ������ѯ�鰴ť
  private ButtonObject m_btnInvBillAssist = null;

  private ButtonObject m_btnLnkQuery = null;// ����

  private ButtonObject m_btnQueryForAudit = null;// ������״̬(״̬��ѯ)

  private ButtonObject m_btnHqhp = null;// �����ż�ȡ��

  // ���������鰴ť
  private ButtonObject m_btnOthersFuncs = null;

  private ButtonObject m_btnCrtAPBill = null;;// ��Ӧ��

  private ButtonObject m_btnDelAPBill = null;;// ȡ����Ӧ��

  private ButtonObject m_btnDocManage = null;;// �ĵ�����

  // ������������ID
  private String m_cauditid = null;

  // ������������ť��
  private ButtonObject m_btnAuditFlowAssist = null;// ����

  private ButtonObject[] aryForAudit = new ButtonObject[] {
      m_btnAudit, m_btnUnAudit, m_btnInvBillAssist, m_btnOthersFuncs
  };

  // ��Ƭ����,�б����,ģ������
  private InvBillPanel m_billPanel = null;

  private InvListPanel m_listPanel = null;

  // ��Ʊ��ѯ�Ի���
  private InvoiceUIQueDlg m_InvQueDlg = null;

  // �����е�VO���飬��ǰ��VOλ��
  private InvoiceVO[] m_InvVOs;

  // �Ƿ������Ƶ���
  private boolean m_isMakedBill = false;

  // ��Ƭ�����״̬:��ͨ���,����ת�����
  private int m_nBillBrowseState;

  private int m_nCurInvVOPos;

  // �б��״̬:��ͨ�б�,����ת���б�
  private int m_nCurOperState;

  // ///======����״̬: ���ݿ�Ƭ �����б�
  private int m_nCurPanelMode = 0;

  private int m_nListOperState;

  private int m_nLstInvVOPos;

  // ѡ�е�����Ŀ������ȷ����ť��״̬
  private int m_nSelectedRowCount;

  // ��һ�εı���С��λ
  private int m_oldCurrMoneyDigit = 2;

  // ������ӡ�����б��ͷ�����к�
  protected ArrayList listSelectBillsPos = null;

  // ����������ֶ���
  private final String m_sInvMngIDItemKey = "invcode";

  // //=======��Ʊά��
  // ��ǰҵ������,ԭ��ҵ������
  private String m_strCurBizeType;

  private String m_strBtnTag;// ����"ҵ������"

  // ״̬����ͷ
  private String m_strHeadHintText = 
//    nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPP40040401-000101")/*
//                                                                                                                   * @res
//                                                                                                                   * "δѡ��ҵ������"
//                                                                                                                   */
//      + "    " + 
      nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPP40040401-000026")/*
                                                                                               * @res
                                                                                               * "����״̬"
                                                                                               */
      + nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPP40040401-000102")/*
                                                                                       * @res
                                                                                       * "��"
                                                                                       */;

  // ������ӡ����
  private ScmPrintTool printList = null;

  private ScmPrintTool printCard = null;

  private final int STATE_BROWSE_NORMAL = 2;

  private final int STATE_EDIT = 1;

  // �û��ĵ�ǰ������״̬:��ʼ��,��ͨ���,����ת�����,�༭,��ͨ�б�,����ת���б�
  private final int STATE_INIT = 0;

  private final int STATE_LIST_FROM_BILLS = 5;

  private final int STATE_LIST_NORMAL = 4;

  private boolean m_bCopy = false;// ���ݸ���

  // ����ת��Ʊ���㷽ʽ
  private String m_sOrder2InvoiceSettleMode = null;

  // ���ת��Ʊ���㷽ʽ
  private String m_sStock2InvoiceSettleMode = null;

  // �Ƿ��ݹ�Ӧ��
  private String m_sZGYF = null;

  private Hashtable hBillStatusBeforeEdit = new Hashtable();

  private int cunpos = 0;

  // ���Ľ��� ���ںϼ�
  private int iMaxMnyDigit = 8;

  // ��Ʊ���ڵ�ǰ��
  private int currentPos = 0;

  private boolean splitFlag = false;

  // ������д���ҵ������
  Set<String> cBizTypeTable = new HashSet<String>();

  // ת��ʱ�б���VO�ڻ����е�λ��
  private String[] VOsPos = null;

  private POPubSetUI2 m_cardPoPubSetUI2 = null;

  // �ɽ����ഫ�ݹ����Ļ��ʾ������ù���ʵ��
  private POPubSetUI2 m_listPoPubSetUI2 = new POPubSetUI2();

  // �Ƿ�Ϊ�༭���ֺ�
  private int isEditCur = 0;

  // ת������ɹ���ķ�ƱVO
  private Vector m_vSavedVO = null;

  // ���,˰��,��˰�ϼ�����, ���þ���
  private String m_itemMny_fi[] = new String[] {
    "noriginalpaymentmny"
  };// ԭ���ۼƸ���

  // ����������, ���þ���
  private String m_itemMny_bu[] = new String[] {
      "noriginalcurmny", "noriginaltaxmny", "noriginalsummny"
  };// ԭ����˰��ԭ��˰�ԭ�Ҽ�˰�ϼ�

  // ���������ۿ�
  private int iChange = 7;
  
  //ҵ�������Ƿ񣬷�Ʊ�ж�������ⵥ������Դ������Ʊֻ�ܴӶ������շ�����
  private Map<String,UFBoolean> onlyRefFeeOrder = new HashMap<String,UFBoolean>();
  
  //���浱ǰ�û�ѡ��ķ�Ʊ����
  private int curInvType = 0;
  
  //���浥��ģ��VO���б�Ϳ�Ƭֻ����һ��
  private BillTempletVO billTempletVo = null;
  
//  private ButtonObject[] extendBtns = null;
//  private ButtonObject feeInvoice = null;
//  private ButtonObject[] btnss = null;
// 
//private ButtonObject[] getBtnss(){
//	if(btnss == null ){
//		 btnss = new ButtonObject[m_btnTree.getButtonArray().length+getExtendBtns().length];
//	      System.arraycopy(m_btnTree.getButtonArray(), 0, btnss, 0, m_btnTree.getButtonArray().length);
//	      System.arraycopy(getExtendBtns(), 0, btnss, m_btnTree.getButtonArray().length, getExtendBtns().length);
//	      return btnss;
//	}
//	return btnss;
//}
  /**
   * InvBillClientUI ������ע�⡣
   */
  public InvoiceUI() {
    super();
    initi();
  }

  //For V56
  public InvoiceUI(FramePanel fp) {
    super();
    setFrame(fp);
    initi();
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
   * 
   * @author lxd
   * @time 2007-3-15 ����02:23:07
   */
  private void createBtnInstances() {

    // ҵ�������鰴ť
    m_btnInvBillBusiType = getBtnTree().getButton(IButtonConstInv.BTN_BUSINESS_TYPE);// ҵ������

    // �����鰴ť
    m_btnInvBillNew = getBtnTree().getButton(IButtonConstInv.BTN_ADD);

    // �����鰴ť
    m_btnInvBillSave = getBtnTree().getButton(IButtonConstInv.BTN_SAVE);

    // ά���鰴ť
    m_btnBillMaintain = getBtnTree().getButton(IButtonConstInv.BTN_BILL);// ά��
    m_btnInvBillModify = getBtnTree().getButton(IButtonConstInv.BTN_BILL_EDIT);// �޸�
    m_btnInvBillCancel = getBtnTree().getButton(IButtonConstInv.BTN_BILL_CANCEL);// ȡ��
    m_btnInvBillDiscard = getBtnTree().getButton(IButtonConstInv.BTN_BILL_DELETE);// ɾ��
    m_btnInvBillCopy = getBtnTree().getButton(IButtonConstInv.BTN_BILL_COPY);// ����

    // �в����鰴ť
    m_btnInvBillRowOperation = getBtnTree().getButton(IButtonConstInv.BTN_LINE);// �в���
    m_btnInvBillAddRow = getBtnTree().getButton(IButtonConstInv.BTN_LINE_ADD);// ����
    m_btnInvBillDeleteRow = getBtnTree().getButton(IButtonConstInv.BTN_LINE_DELETE);// ɾ��
    m_btnInvBillInsertRow = getBtnTree().getButton(IButtonConstInv.BTN_LINE_INSERT);// ������
    m_btnInvBillCopyRow = getBtnTree().getButton(IButtonConstInv.BTN_LINE_COPY);// ������
    m_btnInvBillPasteRow = getBtnTree().getButton(IButtonConstInv.BTN_LINE_PASTE);// ճ����
    m_btnInvBillPasteRowTail = getBtnTree().getButton(IButtonConstInv.BTN_LINE_PASTE_TAIL);// ճ���е���β
    // ֧�ֵ����˵��С������кš�����
    m_btnReSortRowNo = getBtnTree().getButton(IButtonConstPr.BTN_ADD_NEWROWNO);
    m_btnCardEdit = getBtnTree().getButton(IButtonConstPr.BTN_CARDEDIT);
    m_miReSortRowNo = BillTools.addBodyMenuItem(getBillCardPanel(), m_btnReSortRowNo, null);
    m_miCardEdit = BillTools.addBodyMenuItem(getBillCardPanel(), m_btnCardEdit, null);
    if(m_miReSortRowNo != null){
      m_miReSortRowNo.addActionListener(new IMenuItemListener());
    }
    if(m_miCardEdit != null){
      m_miCardEdit.addActionListener(new IMenuItemListener());
    }
    //
    btnBillAddContinue = getBtnTree().getButton(IButtonConstPo.BTN_ADDCONTINUE);// ��������
    // ִ���鰴ť
    m_btnInvBillExcute = getBtnTree().getButton(IButtonConstInv.BTN_EXECUTE);
    m_btnSendAudit = getBtnTree().getButton(IButtonConstInv.BTN_EXECUTE_AUDIT);// ����
    m_btnInvBillAudit = getBtnTree().getButton(IButtonConstInv.BTN_AUDIT);// ���
    m_btnInvBillUnAudit = getBtnTree().getButton(IButtonConstInv.BTN_EXECUTE_AUDIT_CANCEL);// ����
    m_btnInvBillConversion = getBtnTree().getButton(IButtonConstInv.BTN_REF_CANCEL);// ����ת��

    // ��ѯ�鰴ť
    m_btnInvBillQuery = getBtnTree().getButton(IButtonConstInv.BTN_QUERY);// ��Ƭ��ѯ

    // ����鰴ť
    m_btnBillBrowsed = getBtnTree().getButton(IButtonConstInv.BTN_BROWSE);// ���
    m_btnInvBillRefresh = getBtnTree().getButton(IButtonConstInv.BTN_BROWSE_REFRESH);// ��Ƭˢ��
    m_btnInvBillGoFirstOne = getBtnTree().getButton(IButtonConstInv.BTN_BROWSE_TOP);// ��ҳ
    m_btnInvBillGoNextOne = getBtnTree().getButton(IButtonConstInv.BTN_BROWSE_NEXT);// ��һҳ
    m_btnInvBillGoPreviousOne = getBtnTree().getButton(IButtonConstInv.BTN_BROWSE_PREVIOUS);// ��һҳ
    m_btnInvBillGoLastOne = getBtnTree().getButton(IButtonConstInv.BTN_BROWSE_BOTTOM);// ĩҳ

    m_btnInvSelectAll = getBtnTree().getButton(IButtonConstInv.BTN_BROWSE_SELECT_ALL);// ȫѡ
    m_btnInvDeselectAll = getBtnTree().getButton(IButtonConstInv.BTN_BROWSE_SELECT_NONE);// ȫ��

    // ��Ƭ��ʾ/�б���ʾ(�л�)
    m_btnInvShift = getBtnTree().getButton(IButtonConstInv.BTN_SWITCH);// ��Ƭ��ʾ/�б���ʾ

    // ��ӡ�����鰴ť
    m_btnPrints = getBtnTree().getButton(IButtonConstInv.BTN_PRINT);// ��ӡ����
    m_btnInvBillPreview = getBtnTree().getButton(IButtonConstInv.BTN_PRINT_PREVIEW);// Ԥ��
    m_btnInvBillPrint = getBtnTree().getButton(IButtonConstInv.BTN_PRINT_PRINT);// ��ӡ
    btnBillCombin = getBtnTree().getButton(IButtonConstInv.BTN_PRINT_DISTINCT);// �ϲ���ʾ

    // ������ѯ�鰴ť
    m_btnInvBillAssist = getBtnTree().getButton(IButtonConstInv.BTN_ASSIST_QUERY);// ������ѯ
    m_btnLnkQuery = getBtnTree().getButton(IButtonConstInv.BTN_ASSIST_QUERY_RELATED);// ����
    m_btnQueryForAudit = getBtnTree().getButton(IButtonConstInv.BTN_ASSIST_QUERY_WORKFLOW);// ������״̬(״̬��ѯ)
    m_btnHqhp = getBtnTree().getButton(IButtonConstInv.BTN_HQHP);// �����ż�ȡ��

    // ���������鰴ť
    m_btnOthersFuncs = getBtnTree().getButton(IButtonConstInv.BTN_ASSIST_FUNC);
    m_btnCrtAPBill = getBtnTree().getButton(IButtonConstInv.BTN_CrtAPBill);
    m_btnDelAPBill = getBtnTree().getButton(IButtonConstInv.BTN_DelAPBill);
    m_btnDocManage = getBtnTree().getButton(IButtonConstInv.BTN_ASSIST_FUNC_DOCUMENT);// �ĵ�����
  }

  /**
   * ��ȡ��ť������Ψһʵ����
   * <p>
   * <b>examples:</b>
   * <p>
   * ʹ��ʾ��
   * <p>
   * <b>����˵��</b>
   * 
   * @return
   * <p>
   * @author lxd
   * @time 2007-3-15 ����05:04:22
   */
  private ButtonTree getBtnTree() {
    if (m_btnTree == null) {
      try {
        m_btnTree = new ButtonTree(getModuleCode());
//        ButtonObject[] btnsExtend = getExtendBtns();
//        
//            for(int j=0; j<btnsExtend.length;j++){
//                getBtnTree().addMenu(btnsExtend[j]);
//            }
//        
      }
      catch (BusinessException be) {
        showHintMessage(be.getMessage());
        return null;
      }
    }
    return m_btnTree;
  }

  /**
   * ����ID�õ���ʾ��״̬
   * 
   * @param
   * @return
   * @exception �쳣����
   * @see ��Ҫ�μ�����������
   * @since �������һ���汾���˷�������ӽ���������ѡ��
   */
  private void setMaxMnyDigit(int iMaxDigit) {
    // int iMaxDigit = nMaxDigit.intValue() ;
    // ������
    getBillCardPanel().getBodyItem("noriginalcurmny").setDecimalDigits(iMaxDigit);
    getBillCardPanel().getBodyItem("noriginaltaxmny").setDecimalDigits(iMaxDigit);
    getBillCardPanel().getBodyItem("noriginalsummny").setDecimalDigits(iMaxDigit);
    getBillCardPanel().getBodyItem("noriginalpaymentmny").setDecimalDigits(iMaxDigit);
    getBillCardPanel().getBodyItem("nmoney").setDecimalDigits(iMaxDigit);
    getBillCardPanel().getBodyItem("ntaxmny").setDecimalDigits(iMaxDigit);
    getBillCardPanel().getBodyItem("nsummny").setDecimalDigits(iMaxDigit);
    getBillCardPanel().getBodyItem("npaymentmny").setDecimalDigits(iMaxDigit);
    getBillCardPanel().getBodyItem("nexchangeotobrate").setDecimalDigits(iMaxDigit);
    try{
      int size = getBillCardPanel().getRowCount();
      int iCurMnyDigit = m_listPoPubSetUI2.getCCurrDecimal();
      for (int i = 0; i < size; i++) {
  
//        // ȡ�ñ���
//        String sCurrId = (String) getBillListPanel().getBodyBillModel().getValueAt(i, "ccurrencytypeid");
//        int[] iaExchRateDigit = null;
//        if(sCurrId == null){
//          sCurrId = CurrParamQuery.getInstance().getLocalCurrPK(getPk_corp());//����ID
//          BusinessCurrencyRateUtil  caCurCorp  = new BusinessCurrencyRateUtil(getPk_corp());
//        }
//        // �õ��۱����۸����ʾ���
//        iaExchRateDigit = m_listPoPubSetUI2.getBothExchRateDigit(getPk_corp(), sCurrId);
//        if (iaExchRateDigit == null || iaExchRateDigit.length == 0) {
//        }
//        else {
          getBillCardPanel().getBodyItem("nsummny").setDecimalDigits(iCurMnyDigit);// ���Ҽ�˰�ϼ�
          getBillCardPanel().getBodyItem("ntaxmny").setDecimalDigits(iCurMnyDigit);// ����˰��
          getBillCardPanel().getBodyItem("nmoney").setDecimalDigits(iCurMnyDigit);// ������˰���
          getBillCardPanel().getBodyItem("npaymentmny").setDecimalDigits(iCurMnyDigit);// �����ۼƸ���
//        }
      }
    }catch(Exception e){
      showErrorMessage(e.getMessage());
      SCMEnv.out(e);
    }
  }

  /**
   * ����ID�õ���ʾ��״̬
   * 
   * @param
   * @return
   * @exception �쳣����
   * @see ��Ҫ�μ�����������
   * @since �������һ���汾���˷�������ӽ���������ѡ��
   */
  private void setMaxMnyDigitList(int iMaxDigit) {
    // if(true) return;

    // int iMaxDigit = nMaxDigit.intValue() ;
    // ������
    getBillListPanel().getBodyItem("noriginalcurmny").setDecimalDigits(iMaxDigit);
    getBillListPanel().getBodyItem("noriginaltaxmny").setDecimalDigits(iMaxDigit);
    getBillListPanel().getBodyItem("noriginalsummny").setDecimalDigits(iMaxDigit);
    getBillListPanel().getBodyItem("noriginalpaymentmny").setDecimalDigits(iMaxDigit);
    getBillListPanel().getBodyItem("nmoney").setDecimalDigits(iMaxDigit);
    getBillListPanel().getBodyItem("ntaxmny").setDecimalDigits(iMaxDigit);
    getBillListPanel().getBodyItem("nsummny").setDecimalDigits(iMaxDigit);
    getBillListPanel().getBodyItem("npaymentmny").setDecimalDigits(iMaxDigit);
    getBillListPanel().getBodyItem("nexchangeotobrate").setDecimalDigits(iMaxDigit);

    int size = getBillCardPanel().getRowCount();
    for (int i = 0; i < size; i++) {

      int iCurMnyDigit = m_listPoPubSetUI2.getCCurrDecimal();

      getBillListPanel().getBodyItem("nsummny").setDecimalDigits(iCurMnyDigit);// ���Ҽ�˰�ϼ�
      getBillListPanel().getBodyItem("ntaxmny").setDecimalDigits(iCurMnyDigit);// ����˰��
      getBillListPanel().getBodyItem("nmoney").setDecimalDigits(iCurMnyDigit);// ������˰���
      getBillListPanel().getBodyItem("npaymentmny").setDecimalDigits(iCurMnyDigit);// �����ۼƸ���

    }

  }

  /**
   * ���ߣ���ӡ�� ���ܣ����һ�����յİ�ťʱ�����������ı仯�� ������ActionEvent e ��׽����ActionEvent�¼� ���أ��� ���⣺��
   * ���ڣ�(2002-3-13 11:39:21) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־�� 2002-03-20 wyf ���Ч�ʣ��ڸú����м���ɷ�֧
   * wyf add/modify/delete 2002-03-21 begin/end 2002-09-18 wyf ����Բֿ���յ���Ӧ
   */
  public void actionPerformed(ActionEvent e) {

    if (e.getSource() == ((UIRefPane) getBillCardPanel().getHeadItem("cemployeeid").getComponent()).getUIButton()) {
      actionPerformedEmployee(e);
    }
    // else if (e.getSource() == ((UIRefPane)
    // getInvBillPanel().getBodyItem("vfree0").getComponent()).getUIButton()) {
    // PuTool.actionPerformedFree(getInvBillPanel(), e, new String[] {
    // "cmangid", "invcode", "invname", "invspec", "invtype" }, new String[] {
    // "vfree0", "vfree1", "vfree2", "vfree3", "vfree4", "vfree5" });
    // }
    else if (e.getSource() == ((UIRefPane) getBillCardPanel().getBodyItem("cprojectphasename").getComponent())
        .getUIButton()) {
      actionPerformedProjectPhase(e);
    }
    else if (e.getSource() == ((UIRefPane) getBillCardPanel().getBodyItem("cwarehousename").getComponent())
        .getUIButton()) {
      PuTool.restrictWarehouseRefByStoreOrg(getBillCardPanel(), getPk_corp(), getBillCardPanel().getHeadItem(
          "cstoreorganization").getValue(), "cwarehousename");
    }

  }

  /**
   * ���ߣ���ӡ�� ���ܣ������ͷ��ҵ��Ա����ʱ�����̸��²��Ų��գ��޸�Ϊ��ҵ��Ա��Ӧ�Ĳ��� ������ActionEvent e
   * ��׽����ActionEvent�¼� ���أ��� ���⣺�� ���ڣ�(2002-3-13 11:39:21) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
   * 2002-03-21 wyf ���Ч�� wyf add/modify/delete 2002-03-21 begin/end 2008-10-23
   * wyf ҵ��Ա�����޸�Ϊ����״
   */
  private void actionPerformedEmployee(ActionEvent e) {
    // �õ���ĿID

    // ���øò��ŵ�Ĭ��ҵ��Ա, �ò��Ŷ�Ӧ��ҵ��Ա��̬����

    UIRefPane pane = (UIRefPane) getBillCardPanel().getHeadItem("cemployeeid").getComponent();
    String cemployeeid = pane.getRefPK();
    pane.setRefModel(new PurPsnRefModel(getPk_corp(), null));
    pane.setPK(cemployeeid);

  }

  /**
   * ���ߣ����� ���ܣ��޸ı�����ֺ��� ������BillEditEvent e ��׽����BillEditEvent�¼� ���أ��� ���⣺��
   * ���ڣ�(2002-3-13 11:39:21) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־�� 2002-05-15 wyf
   * ��ȡ���뵽��������setBodyCurrRelated()�� wyf add/modify/delete 2002-05-15 begin/end
   * 2002-07-12 wyf ԭ�ȱ��ֱ仯��ȡ��ΪĬ�ϼۣ���ȡΪ��ͬ�� 2002-10-18 wyf �޸�ǰ�������������¼����KEYΪ������
   * �޸ĺ�Ϊԭ�ҵ��ۣ��øı�Ϊ�������еĸı䶼��Ӱ��ƻ��������ڵ��޸�
   */
  private void afterEditWhenBodyCurrency(BillEditEvent e) {

    int iRow = e.getRow();
    // ���ñ����۱����۸����ʵľ��Ⱥ�ֵ��������ɱ༭��
    setExchangeRateBody(iRow, true, null);
    if (e.getValue() == null || e.getValue().toString().trim().equals("")) {
      return;
    }
    getBillCardPanel().setDefaultPrice(iRow, iRow, null);

    String sCurrTypeId = PuPubVO
        .getString_TrimZeroLenAsNull(getBillCardPanel().getBodyValueAt(iRow, "ccurrencytypeid"));
    if (sCurrTypeId == null) {
      return;
    }
    // ����ֵӦ���´���������ܱ���λ����ͬ�������¼���
    // ���޷��õ��ɱ���ID����˲��ܱ��־����Ƿ�ı䣬�����¼��㣬��Ȼ�п��ܵ������ݸı䡣

    UFDouble ninvoicenum = (UFDouble) getBillCardPanel().getBodyValueAt(iRow, "nexchangeotobrate");
    UFDouble ncurprice = (UFDouble) getBillCardPanel().getBodyValueAt(iRow, "noriginalcurprice");
    UFDouble summy = (UFDouble) getBillCardPanel().getBodyValueAt(iRow, "noriginalsummny");
    String sChangedKey = null;
    if (ninvoicenum != null && ninvoicenum.doubleValue() > 0.00 && ncurprice != null && ncurprice.doubleValue() > 0.00) {
      sChangedKey = "ninvoicenum";
    }
    else if ((summy != null && summy.doubleValue() > 0.00)) {
      sChangedKey = "noriginalsummny";
    }
    else {
      sChangedKey = "ninvoicenum";
    }
    afterEditWhenBodyRelationsCal(new BillEditEvent(getBillCardPanel().getBodyItem(sChangedKey), getBillCardPanel()
        .getBodyValueAt(iRow, sChangedKey), sChangedKey, iRow, BillItem.BODY));
    updateUI();
  }

  /**
   * ���ߣ����� ���ܣ��޸����������ۡ������� ������BillEditEvent e ��׽����BillEditEvent�¼� ���أ��� ���⣺��
   * ���ڣ�(2002-3-13 11:39:21) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־�� 2002-10-18 wyf
   * �޸�ǰ�������������¼����KEYΪ������ �޸ĺ�Ϊԭ�ҵ��ۣ��øı�Ϊ��Ӧ��ֻ�������ı��Ӱ��ƻ��������ڵ��޸� 2003-01-20 wyf
   * �޸�ǰ�������仯���¼ƻ��������ڱ仯�� �޸ĺ���������ɿջ��Ϊ��ֵ�����¼ƻ��������ڱ仯�����򲻱�
   */

  private void afterEditWhenBodyRelationsCal(BillEditEvent e) {

    int iRow = e.getRow();
    if ((!(e.getKey().equals("idiscounttaxtype"))) && (e.getValue() != null)
        && (!(e.getValue().toString().trim().equals("")))) {
      UFDouble ndata = new UFDouble(e.getValue().toString().trim());
      if (ndata.doubleValue() < 0) {
        if (e.getKey().equals("ntaxrate")) {
          MessageDialog.showWarningDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON",
              "UPPSCMCommon-000270")/* @res "��ʾ" */, nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401",
              "UPP40040401-000238")/* @res "˰�ʲ���С��0" */);
          getBillCardPanel().getBillModel().setValueAt(null, iRow, "ntaxrate");
          return;
        }
        else if (e.getKey().equals("noriginalcurprice")) {
          MessageDialog.showWarningDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON",
              "UPPSCMCommon-000270")/* @res "��ʾ" */, nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401",
              "UPP40040401-000239")/* @res "���۲���С��0" */);
          getBillCardPanel().getBillModel().setValueAt(null, iRow, "noriginalcurprice");
          return;
        }
      }
      if (e.getKey().equals("ndiscountrate")) {
        // if ((ndata.doubleValue() < 0) || (ndata.doubleValue() > 100))
        // {
        if (ndata.compareTo(VariableConst.ZERO) < 0) {
          // MessageDialog.showWarningDlg(this,"��ʾ","���ʱ������0С��100��");
          MessageDialog.showWarningDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON",
              "UPPSCMCommon-000270")/* @res "��ʾ" */, nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401",
              "UPP40040401-000240")/* @res "���ʱ������0" */);
          getBillCardPanel().getBillModel().setValueAt(null, iRow, "ndiscountrate");
          return;
        }
      }
    }

    // wyf 2002-10-21 add begin
    // ԭ�еĶ�������
    // Object oNum = null;
    // if (e.getKey().equals("ninvoicenum")) {
    // oNum = e.getOldValue();
    // } else {
    // oNum = getInvBillPanel().getBodyValueAt(iRow, "ninvoicenum");
    // }
    // UFDouble dOldNum = (oNum == null || oNum.toString().trim().equals("")) ?
    // VariableConst.ZERO
    // : new UFDouble(oNum.toString());
    // wyf 2002-10-21 add end

    // ����������ϵ
    // calRelation(e);
    afterEditInvBillRelations(e);

  }

  /**
   * ���ߣ���ӡ�� ���ܣ����ñ����۱����۸����ʵľ��Ⱥ�ֵ��������ɱ༭�� ������ int iRow boolean bResetExchValue
   * �Ƿ���Ҫ�������ñ����еĻ���ֵ ���أ��� ���⣺�� ���ڣ�(2002-5-13 11:39:21) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
   */
  private void setExchangeRateBody(int iRow, boolean bResetExchValue, InvoiceItemVO items) {

    String dInvoiceDate = getBillCardPanel().getHeadItem("dinvoicedate").getValue();
    String sCurrId = (String) getBillCardPanel().getBodyValueAt(iRow, "ccurrencytypeid");
    if (sCurrId == null || sCurrId.trim().length() == 0) {
      sCurrId = getBillCardPanel().getHeadItem("ccurrencytypeid").getValue();
    }
    // ����������ʾ����
    setRowDigits_ExchangeRate(getPk_corp(), iRow, getBillCardPanel().getBillModel(), m_cardPoPubSetUI2);
    UFDouble nexchangeotobrate = null;
    if (items != null) {
      nexchangeotobrate = items.getNexchangeotobrate();
    }
    else {
      nexchangeotobrate = PuPubVO.getUFDouble_NullAsZero(getBillCardPanel().getBodyValueAt(iRow, "nexchangeotobrate"));
    }
    // ����ֵ
    if (bResetExchValue && dInvoiceDate != null && dInvoiceDate.trim().length() > 0) {
      UFDouble[] daRate = null;
      String strCurrDate = dInvoiceDate;
      if (strCurrDate == null || strCurrDate.trim().length() == 0) {
        strCurrDate = PoPublicUIClass.getLoginDate() + "";
      }
      daRate = m_cardPoPubSetUI2.getBothExchRateValue(getPk_corp(), sCurrId, new UFDate(dInvoiceDate));
      // UFDouble nexchangeotobrate =
      // (UFDouble)getInvBillPanel().getBodyValueAt(iRow, "nexchangeotobrate");
      // ���ֱ༭�����Ĭ��ֵ
      if (isEditCur == 1) {
        getBillCardPanel().setBodyValueAt(daRate[0], iRow, "nexchangeotobrate");
      }
      else {
        if (nexchangeotobrate != null) {
          getBillCardPanel().setBodyValueAt(nexchangeotobrate, iRow, "nexchangeotobrate");
        }
        else {
          getBillCardPanel().setBodyValueAt(daRate[0], iRow, "nexchangeotobrate");
        }
      }
    }
    else if (nexchangeotobrate != null && nexchangeotobrate.doubleValue() > 0) {
      getBillCardPanel().setBodyValueAt(nexchangeotobrate, iRow, "nexchangeotobrate");
    }
    else if (nexchangeotobrate == null && dInvoiceDate != null && dInvoiceDate.trim().length() > 0) {
      UFDouble[] daRate = null;
      String strCurrDate = dInvoiceDate;
      if (strCurrDate == null || strCurrDate.trim().length() == 0) {
        strCurrDate = PoPublicUIClass.getLoginDate() + "";
      }
      daRate = m_cardPoPubSetUI2.getBothExchRateValue(getPk_corp(), sCurrId, new UFDate(dInvoiceDate));
      getBillCardPanel().setBodyValueAt(daRate[0], iRow, "nexchangeotobrate");
    }

    // ���ÿɱ༭��
    boolean[] baEditable = null;

    baEditable = m_cardPoPubSetUI2.getBothExchRateEditable(getPk_corp(), sCurrId);

    getBillCardPanel().getBillModel().setCellEditable(iRow, "nexchangeotobrate", baEditable[0]);

    // �����޸ı�־
    getBillCardPanel().getBillModel().setRowState(iRow, BillModel.MODIFICATION);
  }

  /**
   * ���ߣ����� ���ܣ������۱����۸�����С��λ ������String pk_corp ��˾ID int iflag ���أ��� ���⣺��
   * ���ڣ�(2002-3-13 11:39:21) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־�� 2002-05-20 wyf �޸�ԭ��ȡ��󾫶�Ϊȡ�����־���
   * wyf add/modify/delete 2002-05-20 begin/end
   */
  protected static void setRowDigits_ExchangeRate(String sPk_corp, int iRow, BillModel billModel, POPubSetUI2 setUi) {
    // ȡ�ñ���
    String sCurrId = (String) billModel.getValueAt(iRow, "ccurrencytypeid");
    int[] iaExchRateDigit = null;
    // �õ��۱����۸����ʾ���
    iaExchRateDigit = setUi.getBothExchRateDigit(sPk_corp, sCurrId);
    if (iaExchRateDigit == null || iaExchRateDigit.length == 0) {
      billModel.getItemByKey("nexchangeotobrate").setDecimalDigits(5);
    }
    else {
      billModel.getItemByKey("nexchangeotobrate").setDecimalDigits(iaExchRateDigit[0]);
    }
  }

  /**
   * ���ߣ���ӡ�� ���ܣ�����������Ŀ�׶β���ʱ�����̲�׽������Ŀ����ID�����õ�����Ŀ�׶εĲ��ա� ������ActionEvent e
   * ��׽����ActionEvent�¼� ���أ��� ���⣺�� ���ڣ�(2002-3-13 11:39:21) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
   * 2002-03-21 wyf ���Ч�� wyf add/modify/delete 2002-03-21 begin/end
   */
  private void actionPerformedProjectPhase(ActionEvent e) {

    int nRow = getBillCardPanel().getBillTable().getSelectedRow();
    // ��Ŀ����ID
    String strProjectMngPk = (String) getBillCardPanel().getBodyValueAt(nRow, "cprojectid");
    // ���ø�����ĿID����Ŀ�׶β���
    UIRefPane pane = ((UIRefPane) getBillCardPanel().getBodyItem("cprojectphasename").getComponent());
    // pane.setRefModel(new CTIPhaseRefModel(strProjectMngPk)) ;
    pane.setRefModel(new PhaseRefModel(strProjectMngPk));

  }

  /**
   * ���з��ϲ�ѯ��������ⵥ->��ƱVO
   * 
   * @param ����˵��
   * @return ����ֵ
   * @exception �쳣����
   * @see ��Ҫ�μ�����������
   * @since �������һ���汾���˷�������ӽ���
   * @return nc.vo.pi.InvoiceVO[]
   */
  private int addNewOneIntoInvVOs(InvoiceVO newVO) {

    if (getInvVOs() == null) {
      setInvVOs(new InvoiceVO[] {
        newVO
      });
      setCurVOPos(0);
      return 0;
    }

    InvoiceVO[] vos = new InvoiceVO[getInvVOs().length + 1];
    for (int i = 0; i < getInvVOs().length; i++) {
      vos[i] = getInvVOs()[i];
    }
    vos[vos.length - 1] = newVO;

    setInvVOs(vos);
    setCurVOPos(getInvVOs().length - 1);
    return getCurVOPos();
  }

  /**
   * ���ߣ���ӡ�� ���ܣ�����һ��仯���ʱ�����������ı仯 ������BillEditEvent e ��׽����BillEditEvent�¼� ���أ���
   * ���⣺�� ���ڣ�(2002-3-13 11:39:21) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־�� 2002-03-20 wyf
   * ���Ч�ʣ��ڸú����м���ɷ�֧ wyf add/modify/delete 2002-03-21 begin/end 2002-09-17 wyf
   * ����Կ����֯�ı�����κŵ���Ӧ
   */
  public void afterEdit(BillEditEvent e) {

    if (getCurPanelMode() == INV_PANEL_CARD) {
      // ��ͷ
      // �����Զ�����PK
      setHeadDefPK(e);
      if (e.getSource() == getBillCardPanel().getHeadItem("cvendormangid").getComponent()) {
        afterEditInvBillVendor(e);
      }
      else if (e.getSource() == getBillCardPanel().getHeadItem("cdeptid").getComponent()) {
        afterEditInvBillDept(e);
      }
      else if (e.getSource() == getBillCardPanel().getHeadItem("cemployeeid").getComponent()) {
        afterEditInvBillEmployee(e);
      }
      else if (e.getSource() == getBillCardPanel().getHeadItem("caccountbankid").getComponent()) {
        afterEditInvBillBank(e);
      }
      else if (e.getSource() == getBillCardPanel().getHeadItem("cfreecustid").getComponent()) {
        afterEditInvBillFreeCust(e);
      }
      else if (e.getSource() == getBillCardPanel().getHeadItem("ccurrencytypeid").getComponent()) {
        afterEditWhenHeadCurrency(e);
      }
      else if (e.getSource() == getBillCardPanel().getHeadItem("ntaxrate").getComponent()) {
        if (getBillCardPanel().getHeadItem("ntaxrate") != null) {
          getBillCardPanel().setHeadItem("ntaxrate", getBillCardPanel().getHeadItem("ntaxrate").getValue());
        }
      }
      else if (e.getSource() == getBillCardPanel().getHeadItem("darrivedate").getComponent()) {
        afterEditInvBillDArriveDate(e);
      }
      else if (e.getSource() == getBillCardPanel().getHeadItem("cstoreorganization").getComponent()) {
        PuTool.afterEditStoreOrgToWarehouse(getBillCardPanel(), e, getPk_corp(), getBillCardPanel().getHeadItem(
            "cstoreorganization").getValue(), "cwarehousename", new String[] {
          "cwarehouseid"
        });

        // �����֯������������еļƻ���
        int size = getBillCardPanel().getRowCount();
        String[] sMangIds = new String[size];

        // �����֯ID
        String sCstoreorganization = getBillCardPanel().getHeadItem("cstoreorganization").getValue();
        for (int i = 0; i < size; i++) {
          sMangIds[i] = (String) getBillCardPanel().getBillModel().getValueAt(i, "cmangid");
        }
        UFDouble[] uPrice = queryPlanPrices(sMangIds, sCstoreorganization);
        if (uPrice != null) {
          for (int i = 0; i < size; i++)
            getBillCardPanel().getBillModel().setValueAt(uPrice[i], i, "nplanprice");
        }
      }
      else if (e.getSource() == getBillCardPanel().getHeadItem("nexchangeotobrate").getComponent()) {
        // ���㱾������
        try {
          int size = getBillCardPanel().getRowCount();
          for (int i = 0; i < size; i++) {
            computeValueFrmOriginal(i);
          }
        }
        catch (Exception ex) {
          reportException(ex);
          PuTool.outException(this, ex);
          int size = getBillCardPanel().getRowCount();
          for (int i = 0; i < size; i++) {
            getBillCardPanel().setBodyValueAt(null, i, "nmoney");
            getBillCardPanel().setBodyValueAt(null, i, "ntaxmny");
            getBillCardPanel().setBodyValueAt(null, i, "nsummny");
          }
          return;
        }

      }
      // ��Ʊ�����޸ĺ�,���Ʋ������������ⷢƱ
      else if ("iinvoicetype".equals(e.getKey())){
        afterEditInvoicetype(e);
      }

      // ����
      else if (e.getPos() == 1) {
        if (e.getKey().equals("cprojectname")) {
          afterEditInvBillProject(e);
        }
        else if (e.getKey().equals("idiscounttaxtype")
            || e.getKey().equals("ninvoicenum")
            || e.getKey().equals("noriginalcurprice") // e.getKey().equals("noriginalcurpriceinctax")
            || e.getKey().equals("norgnettaxprice") || e.getKey().equals("ntaxrate")
            || e.getKey().equals("noriginalcurmny") || e.getKey().equals("noriginaltaxmny")
            || e.getKey().equals("noriginalsummny") || e.getKey().equals("ndiscountrate")
            || e.getKey().equals("npricediscountrate") || e.getKey().equals("nexchangeotobrate")) {
          // ���㱾������
          try {
            if (e.getKey().equals("ninvoicenum")) {
              aftereditWhenBodyInvoicenum(this, getBillCardPanel(), e);
            }
            afterEditInvBillRelations(e);
            computeValueFrmOriginal(e.getRow());
          }
          catch (Exception ex) {
            reportException(ex);
            PuTool.outException(this, ex);
            getBillCardPanel().setBodyValueAt(null, e.getRow(), "nmoney");
            getBillCardPanel().setBodyValueAt(null, e.getRow(), "ntaxmny");
            getBillCardPanel().setBodyValueAt(null, e.getRow(), "nsummny");
            return;
          }
        }
        else if (e.getKey().equals("vfree0")) {
          afterEditInvBillFree(e);
        }
        else if (e.getKey().equals("vmemo")) {
          afterEditInvBillBodyMemo(e);
        }
        else if (e.getKey().equals("invcode")) {
          afterEditInvBillInvcode(e);
        }
        // ��Ʊ֧�ֶ����
        else if (e.getKey().equals("currname")) {
          // ����
          isEditCur = 1;
          afterEditWhenBodyCurrency(e);
          isEditCur = 0;
        }
        // ������ ɭ��NC
        else if (e.getKey().trim().equals("cassistunitname")) {
          afterEditWhenBodyAssist(this, getBillCardPanel(), e);
        }
        // ������ ɭ��NC
        else if (e.getKey().trim().equals("nexchangerate")) {
          afterEditWhenBodyRate(this, getBillCardPanel(), e);
        }
        // ������ ɭ��NC
        else if (e.getKey().trim().equals("nassistnum")) {
          afterEditWhenBodyAssNum(this, getBillCardPanel(), e);
          
        }


        // �����Զ�����PK
        setBodyDefPK(e);
        return;
        
      }

      getBillCardPanel().execHeadEditFormulas();

    }
    if (e.getKey().equals("crowno")) {
      BillRowNo.afterEditWhenRowNo(getBillCardPanel(), e, ScmConst.PO_Invoice);
    }
    if (e.getKey().equals("vinvoicecode")) {
      // �嵥�ݺſո�
      if (PuPubVO.getString_TrimZeroLenAsNull(getBillCardPanel().getHeadItem("vinvoicecode").getValue()) != null) {
        getBillCardPanel().getHeadItem("vinvoicecode").setValue(
            getBillCardPanel().getHeadItem("vinvoicecode").getValue().toString().trim());
      }

    }
    
    /**
     * ���ο������֧�� by zhaoyha at 2009.2.16
     */
    getInvokeEventProxy().afterEdit(e);
    
  }

  /**
   * ���ߣ���ӡ�� ���ܣ���ͷ�������б仯�������ʺ���Ӧ�仯 ������BillEditEvent e ��׽����BillEditEvent�¼� ���أ���
   * ���⣺�� ���ڣ�(2002-3-13 11:39:21) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־�� 2002-03-20 wyf
   * ���Ч�ʣ��ڸú����м���ɷ�֧ wyf add/modify/delete 2002-03-21 begin/end
   */
  private void afterEditInvBillBank(BillEditEvent e) {
    UIRefPane pane = (UIRefPane) getBillCardPanel().getHeadItem("caccountbankid").getComponent();
    getBillCardPanel().setHeadItem("cvendoraccount", pane.getRefCode());

  }

  /**
   * ���ߣ���ӡ�� ���ܣ����屸ע�仯��ɺ󣬱�֤����ǲ��ս�����������գ�������ֹ����룬�����ֹ����� ������BillEditEvent e
   * ��׽����BillEditEvent�¼� ���أ��� ���⣺�� ���ڣ�(2002-3-13 11:39:21) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
   * 2002-03-20 wyf ���Ч�ʣ��ڸú����м���ɷ�֧ wyf add/modify/delete 2002-03-21 begin/end
   */
  private void afterEditInvBillBodyMemo(BillEditEvent e) {

    String str = ((UIRefPane) getBillCardPanel().getBodyItem("vmemo").getComponent()).getRefName();
    if (str != null && !str.trim().equals("")) {
      getBillCardPanel().setBodyValueAt(str, e.getRow(), "vmemo");
    }
    else {
      getBillCardPanel().setBodyValueAt(((UIRefPane) getBillCardPanel().getBodyItem("vmemo").getComponent()).getText(),
          e.getRow(), "vmemo");
    }
  }

  /**
   * ���ߣ����� ���ܣ��޸ı�ͷ���ֺ��� ������BillEditEvent e ��׽����BillEditEvent�¼� ���أ��� ���⣺��
   * ���ڣ�(2002-3-13 11:39:21) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־�� 2001-12-11 ljq
   * ���ݲ�ͬ�ı��֣����ñ�ͷ�������۸����ʡ��۱����ʵľ��� 2002-06-07 wyf �޸���״̬���Ա㱣��ʱ���� 2002-11-11 WYF
   * ���������޼۵Ĵ���
   */
  private void afterEditWhenHeadCurrency(BillEditEvent e) {

    // �رպϼƿ���
    boolean bOldNeedCalc = getBillCardPanel().getBillModel().isNeedCalculate();
    getBillCardPanel().getBillModel().setNeedCalculate(false);

    try {
      String sCurrId = getBillCardPanel().getHeadItem("ccurrencytypeid").getValue();
      String dInvoiceDate = getBillCardPanel().getHeadItem("dinvoicedate").getValue();
      //add by QuSida (��ɽ����) 2010-9-24  ��ֹ��Ʊ����Ϊ��ʱ��setExchangeRateHead(dInvoiceDate, sCurrId)��������
      if(dInvoiceDate == null||dInvoiceDate.equals("")){
    	  dInvoiceDate = ClientEnvironment.getInstance().getDate().toString();
      }

      // =================��ͷ
      // �����۱����۸�����
      setExchangeRateHead(dInvoiceDate, sCurrId);
      // �������¼���
      setCurrMoneyDigitAndReCalToBill(sCurrId);
      // =================����
      if (sCurrId == null || sCurrId.trim().equals("") || getBillCardPanel().getBillModel().getRowCount() == 0) {
     // �򿪺ϼƿ���
        getBillCardPanel().getBillModel().setNeedCalculate(bOldNeedCalc);
        return;
      }
      ArrayList listAllCurrId = new ArrayList();
      listAllCurrId.add(sCurrId);
      BusinessCurrencyRateUtil bca = new BusinessCurrencyRateUtil(getPk_corp());
      // ȡ��������PK
      if (CurrParamQuery.getInstance().getLocalCurrPK(getPk_corp()) != null
          && !listAllCurrId.contains(CurrParamQuery.getInstance().getLocalCurrPK(getPk_corp()))) {
        listAllCurrId.add(CurrParamQuery.getInstance().getLocalCurrPK(getPk_corp()));
      }

      // �ı����û�б��ֵ���
      String sBodyCurrId = null;
      int iLen = getBillCardPanel().getRowCount();
      for (int i = 0; i < iLen; i++) {
        sBodyCurrId = (String) getBillCardPanel().getBillModel().getValueAt(i, "ccurrencytypeid");
        if (sBodyCurrId == null || sBodyCurrId.equals("")) {
          getBillCardPanel().getBillModel().setValueAt(sCurrId, i, "ccurrencytypeid");
          // ��ȡ��������
          // execBodyFormula(i, "ccurrencytype");//�Ż� V31
          // ����������ر��ֵĸĶ�
          afterEditWhenBodyCurrency(new BillEditEvent(getBillCardPanel().getBodyItem("ccurrencytypeid").getComponent(),
              sCurrId, "ccurrencytypeid", i));
          // �����޸ı�־
          getBillCardPanel().getBillModel().setRowState(i, BillModel.MODIFICATION);
        }
      }
      getBillCardPanel().getBillModel().execEditFormulaByKey(-1, "currname");// �Ż�
                                                                              // V31

      // ���㱾������
      try {
        int size = getBillCardPanel().getRowCount();
        for (int i = 0; i < size; i++) {
          computeValueFrmOriginal(i);
        }
      }
      catch (Exception ex) {
        reportException(ex);
        PuTool.outException(this, ex);
        int size = getBillCardPanel().getRowCount();
        for (int i = 0; i < size; i++) {
          getBillCardPanel().setBodyValueAt(null, i, "nmoney");
          getBillCardPanel().setBodyValueAt(null, i, "ntaxmny");
          getBillCardPanel().setBodyValueAt(null, i, "nsummny");
        }
      }
    }
    catch (Exception exp) {
      PuTool.outException(this, exp);
      // �򿪺ϼƿ���
      getBillCardPanel().getBillModel().setNeedCalculate(bOldNeedCalc);
      return;
    }
    // �򿪺ϼƿ���
    getBillCardPanel().getBillModel().setNeedCalculate(bOldNeedCalc);
  }

  /**
   * ���ߣ���ӡ�� ���ܣ����ñ�ͷ���ּ����ʵĿɱ༭�� �������� ���أ��� ���⣺�� ���ڣ�(2002-5-13 11:39:21)
   * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
   */
  private void setExchangeRateHead(String dInvoicedate, String sCurrId) {

    sCurrId = (sCurrId == null || sCurrId.trim().length() == 0) ? null : sCurrId;
    // ����������ʾ����
    int[] iaExchRateDigit = m_cardPoPubSetUI2.getBothExchRateDigit(getPk_corp(), sCurrId);
    getBillCardPanel().getHeadItem("nexchangeotobrate").setDecimalDigits(iaExchRateDigit[0]);
    // ����ֵ
    UFDouble[] daRate = m_cardPoPubSetUI2.getBothExchRateValue(getPk_corp(), sCurrId, new UFDate(dInvoicedate));
    UFDouble temp = daRate[0] == null?UFDouble.ONE_DBL:daRate[0];
    getBillCardPanel().getHeadItem("nexchangeotobrate").setValue(temp);
    // �ɱ༭��
    boolean[] iaEditable = m_cardPoPubSetUI2.getBothExchRateEditable(getPk_corp(), sCurrId);
    getBillCardPanel().getHeadItem("nexchangeotobrate").setEnabled(iaEditable[0]);

  }

  /**
   * ���ߣ���ӡ�� ���ܣ���ͷƱ�����ڱ仯�󣬻�����Ӧ�仯�� ������BillEditEvent e ��׽����BillEditEvent�¼� ���أ���
   * ���⣺�� ���ڣ�(2002-3-13 11:39:21) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־�� 2002-03-20 wyf
   * ���Ч�ʣ��ڸú����м���ɷ�֧ wyf add/modify/delete 2002-03-21 begin/end
   */
  private void afterEditInvBillDArriveDate(BillEditEvent e) {
    String strCurr = getBillCardPanel().getHeadItem("ccurrencytypeid").getValue();
    setCurrRateToBillHead(strCurr, null);

  }

  /**
   * ���ߣ���ӡ�� ���ܣ���ͷ���ű仯��ҵ��Ա��Ӧ�仯 ������BillEditEvent e ��׽����BillEditEvent�¼� ���أ��� ���⣺��
   * ���ڣ�(2002-3-13 11:39:21) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־�� 2002-03-20 wyf ���Ч�ʣ��ڸú����м���ɷ�֧
   * wyf add/modify/delete 2002-03-21 begin/end
   */
  private void afterEditInvBillDept(BillEditEvent e) {

    UIRefPane pane = (UIRefPane) getBillCardPanel().getHeadItem("cdeptid").getComponent();
    if (pane.getRefPK() == null || pane.getRefPK().trim().length() < 1)
      return;
  }

  /**
   * ���ߣ���ӡ�� ���ܣ���ͷҵ��Ա�仯�󣬲�����Ӧ�仯 ������BillEditEvent e ��׽����BillEditEvent�¼� ���أ��� ���⣺��
   * ���ڣ�(2002-3-13 11:39:21) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־�� 2002-03-20 wyf ���Ч�ʣ��ڸú����м���ɷ�֧
   * wyf add/modify/delete 2002-03-21 begin/end
   */
  private void afterEditInvBillEmployee(BillEditEvent e) {

    // ���øò��ŵ�Ĭ��ҵ��Ա, �ò��Ŷ�Ӧ��ҵ��Ա��̬����
    String cemployeeid = ((UIRefPane) getBillCardPanel().getHeadItem("cemployeeid").getComponent()).getRefPK();
    if (cemployeeid == null || cemployeeid.trim().length() < 1)
      return;

    String newDeptid = (String) PiPqPublicUIClass.getAResultFromTable("bd_psndoc", "pk_deptdoc", "pk_psndoc",
        cemployeeid);

    UIRefPane pane = (UIRefPane) getBillCardPanel().getHeadItem("cdeptid").getComponent();
    pane.setPK(newDeptid);
  }

  /**
   * ���ߣ���ӡ�� ���ܣ�����������仯������������ֵ��Ӧ�ı� ������BillEditEvent e ��׽����BillEditEvent�¼� ���أ���
   * ���⣺�� ���ڣ�(2002-3-13 11:39:21) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־�� 2002-03-20 wyf
   * ���Ч�ʣ��ڸú����м���ɷ�֧ wyf add/modify/delete 2002-03-21 begin/end
   */
  private void afterEditInvBillFree(BillEditEvent e) {

    FreeVO freeVO = ((FreeItemRefPane) getBillCardPanel().getBodyItem("vfree0").getComponent()).getFreeVO();
    if (freeVO == null) {
      for (int i = 0; i < 5; i++) {
        String str = "vfree" + new Integer(i + 1).toString();
        getBillCardPanel().setBodyValueAt(null, e.getRow(), str);
      }
    }
    else {
      // ��Ʊ��������1��5 //getVfreeid7
      for (int i = 0; i < 5; i++) {
        // vfreename2
        String strName = "vfreename" + new Integer(i + 1).toString();
        if (freeVO.getAttributeValue(strName) != null) {
          String str = "vfree" + new Integer(i + 1).toString();
          Object ob = freeVO.getAttributeValue(str);
          getBillCardPanel().setBodyValueAt(ob, e.getRow(), str);
        }
      }
    }

    // ���⵱ǰֵ��������һ�����������
    InvVO invVO = new InvVO();
    ((FreeItemRefPane) getBillCardPanel().getBodyItem("vfree0").getComponent()).setFreeItemParam(invVO);
  }

  /**
   * ���ߣ���ӡ�� ���ܣ���ͷɢ���仯����Ӧ�绰�ȱ仯 ������BillEditEvent e ��׽����BillEditEvent�¼� ���أ��� ���⣺��
   * ���ڣ�(2002-3-13 11:39:21) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־�� 2002-03-20 wyf ���Ч�ʣ��ڸú����м���ɷ�֧
   * wyf add/modify/delete 2002-03-21 begin/end
   */
  private void afterEditInvBillFreeCust(BillEditEvent e) {

    String freeId = getBillCardPanel().getHeadItem("cfreecustid").getValue();
    setDefaultInfoForAFreeCust(freeId);
  }

  /**
   * ���ߣ���ӡ�� ���ܣ��������仯�������˰����Ӧ�ı� ������BillEditEvent e ��׽����BillEditEvent�¼� ���أ���
   * ���⣺�� ���ڣ�(2002-3-13 11:39:21) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־�� 2002-03-20 wyf
   * ���Ч�ʣ��ڸú����м���ɷ�֧ wyf add/modify/delete 2002-03-21 begin/end 2002-08-26 wyf
   * ˰�ʸı�󴥷�ֵ���¼����¼� 2002-09-18 wyf ����޸ĺ����κ���գ������ƿɱ༭��
   */
  private void afterEditInvBillInvcode(BillEditEvent e) {

    UIRefPane refpane = (UIRefPane) getBillCardPanel().getBodyItem("invcode").getComponent();
    Object[] saMangId = ((Object[]) refpane.getRefValues("bd_invmandoc.pk_invmandoc"));

    int nRow = getBillCardPanel().getRowCount();
    InvoiceVO VO = new InvoiceVO(nRow);
    getBillCardPanel().getBillValueVO(VO);

    // Ϊ��ѡ��׼��
    int iInsertLen = (saMangId == null ? 0 : saMangId.length - 1);
    int iBeginRow = e.getRow();
    if (iBeginRow == getBillCardPanel().getRowCount() - 1) {
      // ѡ�е����������һ��������
      for (int i = 0; i < iInsertLen; i++) {
        onActionAppendLine();
      }
    }
    else {
      // ����
      onActionInsertLines(iBeginRow, iBeginRow + 1, iInsertLen);
    }
    int iEndRow = iBeginRow + iInsertLen;

    // ������
    // ȡ��ͷ����
    String sCurrId = getBillCardPanel().getHeadItem("ccurrencytypeid").getValue();
    String sBodyCurrId = null;
    for (int i = iBeginRow; i <= iEndRow; i++) {
      getBillCardPanel().setBodyValueAt(null, i, "vfree0");
      getBillCardPanel().setBodyValueAt(null, i, "vfree1");
      getBillCardPanel().setBodyValueAt(null, i, "vfree2");
      getBillCardPanel().setBodyValueAt(null, i, "vfree3");
      getBillCardPanel().setBodyValueAt(null, i, "vfree4");
      getBillCardPanel().setBodyValueAt(null, i, "vfree5");
      // ���������б���Ĭ��Ϊ��ͷ����
      sBodyCurrId = (String) getBillCardPanel().getBillModel().getValueAt(i, "ccurrencytypeid");
      if (sBodyCurrId == null || sBodyCurrId.equals("")) {
        getBillCardPanel().getBillModel().setValueAt(sCurrId, i, "ccurrencytypeid");
        // ��ȡ��������
        // ����������ر��ֵĸĶ�
        afterEditWhenBodyCurrency(new BillEditEvent(getBillCardPanel().getBodyItem("ccurrencytypeid").getComponent(),
            sCurrId, "ccurrencytypeid", i));
        // �����޸ı�־
        getBillCardPanel().getBillModel().setRowState(i, BillModel.MODIFICATION);
        getBillCardPanel().getBillModel().getValueAt(i, "ccurrencytypeid");
      }
      // ���û�����
      setExchangeRateBody(i, true, null);

    }
    // ִ�б��幫ʽ��������
    setInvEditFormulaInfo(getBillCardPanel(), refpane, iBeginRow, iEndRow);
    getBillCardPanel().getBillModel().execEditFormulaByKey(-1, "currname");// �Ż�
                                                                            // V31
    // ���������Ĭ��˰��
    setRelated_Taxrate(iBeginRow, iEndRow);

    for (int i = iBeginRow; i <= iEndRow; i++) {
      // ���κŵĿɱ༭��
      String sMangId = PuPubVO.getString_TrimZeroLenAsNull(getBillCardPanel().getBodyValueAt(i, "cmangid"));
      if (sMangId == null || sMangId.trim().length() == 0) {
        getBillCardPanel().getBillModel().setCellEditable(i, "vproducenum", false);
      }
      else {
        getBillCardPanel().getBillModel().setCellEditable(i, "vproducenum", PuTool.isBatchManaged(sMangId));
      }
      // ���κ����
      getBillCardPanel().setBodyValueAt(null, i, "vproducenum");
    }

    // ��ȡ�ƻ��۸�
    String sCstoreorganization = getBillCardPanel().getHeadItem("cstoreorganization").getValue();
    int size = saMangId == null ? 0 : saMangId.length;
    String[] sMangIds = new String[size];
    for (int i = 0; i < size; i++) {
      sMangIds[i] = saMangId[i].toString();
    }
    UFDouble[] uPrice = queryPlanPrices(sMangIds, sCstoreorganization);
    if (uPrice != null) {
      for (int i = iBeginRow; i <= iEndRow; i++) {
        getBillCardPanel().getBillModel().setValueAt(uPrice[i - iBeginRow], i, "nplanprice");
      }
    }
    BillItem it = getBillCardPanel().getBodyItem(e.getKey());
    if (it.getEditFormulas() != null && it.getEditFormulas().length > 0) {
      getBillCardPanel().getBillModel().execFormulas(it.getEditFormulas(), iBeginRow, iEndRow);
    }
    //For V56 ������ô����Ϣ
    PuTool.loadFeeInventoryBatch(new InvoiceVO[]{getCurVOonCard()}, "cbaseid");
    getBillCardPanel().setDefaultPrice(iBeginRow, iEndRow, null);
    // ������
    setRelated_AssistUnit(iBeginRow, iEndRow);
  }

  /**
   * ���ߣ���ӡ�� ���ܣ�������Ŀ�仯����Ŀ�׶�ID��� ������BillEditEvent e ��׽����BillEditEvent�¼� ���أ��� ���⣺��
   * ���ڣ�(2002-3-13 11:39:21) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־�� 2002-03-20 wyf ���Ч�ʣ��ڸú����м���ɷ�֧
   * wyf add/modify/delete 2002-03-21 begin/end
   */
  private void afterEditInvBillProject(BillEditEvent e) {
    int n = e.getRow();
    if (n < 0)
      return;
    // �ж���Ŀ�Ƿ�Ϊ�ա���Ϊ�գ�����Ŀ�׶β��ɱ༭
    Object oTemp = getBillCardPanel().getBodyValueAt(n, "cprojectname");
    if (oTemp == null || oTemp.toString().length() == 0) {
      getBillCardPanel().getBillModel().setCellEditable(n, "cprojectphasename", false);
      getBillCardPanel().setBodyValueAt(null, n, "cprojectphaseid");
      getBillCardPanel().setBodyValueAt(null, n, "cprojectphasename");
    }
    else {
      getBillCardPanel().getBillModel().setCellEditable(n, "cprojectphasename", true);
    }
  }

  /**
   * ���ߣ���ӡ�� ���ܣ�������һ����ֵ�仯����������Ӧ�ı� ������BillEditEvent e ��׽����BillEditEvent�¼� ���أ���
   * ���⣺�� ���ڣ�(2002-3-13 11:39:21) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־�� 2002-03-20 wyf
   * ���Ч��afterEdit()����ɷ�֧ wyf add/modify/delete 2002-03-21 begin/end 2002-08-26
   * wyf ��˰���δѡ��ʱ�����м��� wyf add/modify/delete 2002-03-21 begin/end
   */
  public void afterEditInvBillRelations(BillEditEvent e) {
    int[] descriptions = new int[] {
        RelationsCal.DISCOUNT_TAX_TYPE_NAME, RelationsCal.DISCOUNT_TAX_TYPE_KEY, RelationsCal.NUM,
        RelationsCal.NET_PRICE_ORIGINAL, RelationsCal.NET_TAXPRICE_ORIGINAL, RelationsCal.TAXRATE,
        RelationsCal.MONEY_ORIGINAL, RelationsCal.TAX_ORIGINAL, RelationsCal.SUMMNY_ORIGINAL
    };
    // ��˰��� 0Ӧ˰�ں� 1Ӧ˰��� 2����˰
    String s=(String) getBillCardPanel().getBodyValueAt(e.getRow(), "idiscounttaxtype");
    if(StringUtil.isEmptyWithTrim(s)) s="Ӧ˰�ں�";
    
    String[] keys = new String[] {
        s, "idiscounttaxtype", "ninvoicenum", "noriginalcurprice", "norgnettaxprice", "ntaxrate", "noriginalcurmny",
        "noriginaltaxmny", "noriginalsummny"
    };
    // fangy*********************************
    RelationsCal.calculate(getBillCardPanel(), e, getBillCardPanel().getBillModel(),new int[] {
        PuTool.getPricePriorPolicy(this.getPk_corp()), iChange
    }, descriptions, keys, InvoiceItemVO.class.getName());
    // fangy*********************************
  }

  /**
   * ���ߣ���ӡ�� ���ܣ���ͷ��Ӧ�̱仯����Ӧ��������仯 ������BillEditEvent e ��׽����BillEditEvent�¼� ���أ���
   * ���⣺�� ���ڣ�(2002-3-13 11:39:21) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־�� 2002-03-20 wyf
   * ���Ч�ʣ��ڸú����м���ɷ�֧ wyf add/modify/delete 2002-03-21 begin/end
   */
  private void afterEditInvBillVendor(BillEditEvent e) {

    // added by fangy 2002-10-23 12:22 begin
    // ������յ����
    if (e.getValue() == null || e.getValue().equals("")) {
      // ����
      getBillCardPanel().setHeadItem("ccurrencytypeid", null);
      // �ո���Э��
      getBillCardPanel().setHeadItem("ctermprotocolid", null);
      // ���к��ʻ�
      getBillCardPanel().getHeadItem("caccountbankid").setValue(null);
      getBillCardPanel().getHeadItem("caccountbankid").setEnabled(false);
      getBillCardPanel().setHeadItem("cvendoraccount", null);
      getBillCardPanel().setHeadItem("cvendorphone", null);
      // ���ź�ҵ��Ա
      // getInvBillPanel().setHeadItem("cdeptid",null) ;
      // getInvBillPanel().setHeadItem("cemployeeid",null) ;
    }
    // added by fangy 2002-10-23 12:22 end
    else {
      // ��Ӧ��
      String cvendormangid = getBillCardPanel().getHeadItem("cvendormangid").getValue();
      // String cvendorbaseid =
      // getResultFromFormula("getColValue(bd_cumandoc,pk_cubasdoc,pk_cumandoc,cvendormangid)","cvendormangid",cvendormangid)
      // ;
      String cvendorbaseid = (String) PiPqPublicUIClass.getAResultFromTable("bd_cumandoc", "pk_cubasdoc",
          "pk_cumandoc", cvendormangid);
      getBillCardPanel().setHeadItem("cvendorbaseid", cvendorbaseid);

      // Ĭ�Ͻ��ױ���:ȡĬ�Ͻ��ױ���,���û��,�򲻱�
      // String strCurr =
      // getResultFromFormula("getColValue(bd_cumandoc,pk_currtype1,pk_cumandoc,cvendormangid)","cvendormangid",cvendormangid)
      // ;
      String strCurr = (String) PiPqPublicUIClass.getAResultFromTable("bd_cumandoc", "pk_currtype1", "pk_cumandoc",
          cvendormangid);
      if (strCurr != null && strCurr.trim().length() > 1) {
        getBillCardPanel().setHeadItem("ccurrencytypeid", strCurr);
        setCurrRateToBillHead(strCurr, null);
        setCurrMoneyDigitAndReCalToBill(strCurr);

        afterEditWhenHeadCurrency(e);
      }
      // �ո���Э��
      String payTermId = getBillCardPanel().getHeadItem("ctermprotocolid").getValue();
      if (payTermId == null || payTermId.trim().length() < 1) {
        // payTermId =
        // getResultFromFormula("getColValue(bd_cumandoc,pk_payterm,pk_cumandoc,cvendormangid)","cvendormangid",cvendormangid)
        // ;
        payTermId = (String) PiPqPublicUIClass.getAResultFromTable("bd_cumandoc", "pk_payterm", "pk_cumandoc",
            cvendormangid);
        getBillCardPanel().setHeadItem("ctermprotocolid", payTermId);
      }
      // �ո��λ
      String sPayUnit = getBillCardPanel().getHeadItem("cpayunit").getValue();
      // ��ԭ������Ŀ���⣺�޸ķ�Ʊ�ϵĹ�Ӧ��ΪB��λ������ǩ�ֺ�����Ӧ�����ϵĹ�Ӧ�̻�����A��λ
      // if (sPayUnit == null || sPayUnit.trim().length() == 0) {
      // sPayUnit = (String)
      // PiPqPublicUIClass.getAResultFromTable("bd_cumandoc", "pk_payterm",
      // "pk_cumandoc", cvendormangid);
      getBillCardPanel().setHeadItem("cpayunit", cvendormangid);
      // }

      // ====�Ƿ�ɢ��, �绰,��������,�ʺ�
      // String strFreeFlag =
      // getResultFromFormula("getColValue(bd_cubasdoc,freecustflag,pk_cubasdoc,cvendorbaseid)","cvendorbaseid",cvendorbaseid)
      // ;
      String strFreeFlag = (String) PiPqPublicUIClass.getAResultFromTable("bd_cubasdoc", "freecustflag", "pk_cubasdoc",
          cvendorbaseid);
      if (strFreeFlag.equals("N")) {
        // ɢ���û�,�����Ӧ��Ϣ
        getBillCardPanel().getHeadItem("cfreecustid").setEnabled(false);
        getBillCardPanel().getHeadItem("cfreecustid").setValue(null);
        ((UIRefPane) getBillCardPanel().getHeadItem("cfreecustid").getComponent()).getUITextField().setText(null);
        ((UIRefPane) getBillCardPanel().getHeadItem("caccountbankid").getComponent()).setButtonVisible(true);
        getBillCardPanel().getHeadItem("caccountbankid").setEnabled(true);

        // �ù�Ӧ�̶�Ӧ�Ŀ������ж�̬���ռ�Ĭ�Ͽ�������
        setDefaultBankAccountForAVendor(cvendorbaseid);
        // ��Ӧ�̵绰
        setDefaultPhoneForAVendor(cvendorbaseid);
      }
      else {
        // ɢ������
        getBillCardPanel().getHeadItem("cfreecustid").setEnabled(true);
        // �������в��ɱ༭,��ť���ɼ�
        ((UIRefPane) getBillCardPanel().getHeadItem("caccountbankid").getComponent()).setButtonVisible(false);
        getBillCardPanel().getHeadItem("caccountbankid").setEnabled(false);

        // Ӱ�����Ϣ
        getBillCardPanel().setHeadItem("cvendorphone", null);
        getBillCardPanel().setHeadItem("caccountbankid", null);
        ((UIRefPane) getBillCardPanel().getHeadItem("caccountbankid").getComponent()).getUITextField().setText(null);
        getBillCardPanel().setHeadItem("cvendoraccount", null);
      }
      // ���ܻ�Ӱ�쵽���ż�ҵ��ԱID
      if (getBillCardPanel().getHeadItem("cdeptid").getValue() == null) {
        // String deptID =
        // getResultFromFormula("getColValue(bd_cumandoc,pk_respdept1,pk_cumandoc,cvendormangid)","cvendormangid",cvendormangid)
        // ;
        String deptID = (String) PiPqPublicUIClass.getAResultFromTable("bd_cumandoc", "pk_respdept1", "pk_cumandoc",
            cvendormangid);
        getBillCardPanel().setHeadItem("cdeptid", deptID);
      }
      // Ĭ��ҵ��Ա
      if (getBillCardPanel().getHeadItem("cemployeeid").getValue() == null) {
        // String employeeID =
        // getResultFromFormula("getColValue(bd_cumandoc,pk_resppsn1,pk_cumandoc,cvendormangid)","cvendormangid",cvendormangid)
        // ;
        String employeeID = (String) PiPqPublicUIClass.getAResultFromTable("bd_cumandoc", "pk_resppsn1", "pk_cumandoc",
            cvendormangid);
        getBillCardPanel().setHeadItem("cemployeeid", employeeID);
        UIRefPane ref = (UIRefPane) (getBillCardPanel().getHeadItem("cemployeeid").getComponent());
        ref.setPK(employeeID);
      }
    }
  }

  /**
   * ���ߣ���ӡ�� ���ܣ��༭ǰ���� ������BillEditEvent e ��׽����BillEditEvent�¼� ���أ��� ���⣺true
   * ���ڣ�(2002-3-13 11:39:21) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־�� 2002-09-17 wyf ��������κŵĴ���
   */
  public boolean beforeEdit(BillEditEvent e) {
    /**
     * ���ο������֧�� by zhaoyha at 2009.2.16
     */
    boolean pluginRet=getInvokeEventProxy().beforeEdit(e);

    if (getCurPanelMode() == INV_PANEL_CARD) {
      if (e.getPos() == BillItem.BODY) {
        // ������
        if (e.getKey().equals("vfree0")) {
          return PuTool.beforeEditInvBillBodyFree(getBillCardPanel(), e, new String[] {
              "cmangid", "invcode", "invname", "invspec", "invtype"
          }, new String[] {
              "vfree0", "vfree1", "vfree2", "vfree3", "vfree4", "vfree5"
          });
        }
        // ��ע
        else if (e.getKey().equals("vmemo")) {
          return beforeEditInvBillBodyMemo(e);
        }
        // �۱�����
        else if (e.getKey().equals("nexchangeotobrate")) {
          // ���û�����
          setExchangeRateBody(e.getRow(), true, null);
        }
        // ����ԭ/��/���ҽ���
        else if (e.getKey().equals("noriginalcurmny") || e.getKey().equals("noriginaltaxmny")
            || e.getKey().equals("noriginalsummny") || e.getKey().equals("nmoney") || e.getKey().equals("ntaxmny")
            || e.getKey().equals("nsummny")) {
          // setRowDigits_Mny(getPk_corp(),
          // e.getRow(),getInvBillPanel().getBillModel(),m_cardPoPubSetUI2);
          try {
            computeValueFrmOriginal(e.getRow());
          }
          catch (Exception ex) {
            SCMEnv.out(e);
          }
        }
        // ���κ�
        else if (e.getKey().equals("vproducenum")) {
          PuTool.beforeEditWhenBodyBatch(getBillCardPanel(), getPk_corp(), e, new String[] {
              // �������ID�����롢���ơ�����ͺš���λ
              "cmangid", "invcode", "invname", "invspec", "invtype", "measname",
              // ��������λ���Ƿ񸨼�������
              // "cassistunit", "assistunitflag",
              null, null,
              // �ֿ�ID
              "cwarehouseid"
          }, "vfree");
        }
        // �ֿ�,�Ӳɹ���ⵥ���ɵķ�Ʊ�ֿⲻ�ܸ���
        else if (e.getKey().equals("cwarehousename")) {
          Object oCupsourceBillType = getBillCardPanel().getBodyValueAt(e.getRow(), "cupsourcebilltype");
          if (oCupsourceBillType != null && oCupsourceBillType.toString().trim().length() > 0) {
            if (oCupsourceBillType.toString().equals("45")) {
              showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPP40040401-000009")/*
                                                                                                             * @res
                                                                                                             * "�Ӳɹ���ⵥ���ɵķ�Ʊ�ֿⲻ�ܸ���"
                                                                                                             */);
              getBillCardPanel().getBodyItem("cwarehousename").setEnabled(false);
            }
          }
          else
            getBillCardPanel().getBodyItem("cwarehousename").setEnabled(true);
        }
        // ���
        else if (e.getKey().equals("invcode")) {

        }
        // ��Ŀ�׶�
        else if (e.getKey().equals("cprojectphasename")) {
          Object oTemp = getBillCardPanel().getBodyValueAt(e.getRow(), "cprojectid");
          if (oTemp == null || oTemp.toString().trim().length() == 0) {
            getBillCardPanel().setCellEditable(e.getRow(), "cprojectphasename", false);
          }
          else {
            getBillCardPanel().setCellEditable(e.getRow(), "cprojectphasename", true);
            UIRefPane refpane = (UIRefPane) getBillCardPanel().getBodyItem("cprojectphasename").getComponent();
            String s = refpane.getRefModel().getWherePart();
            s += " and bd_jobobjpha.pk_jobmngfil = '" + oTemp.toString() + "' ";
            refpane.setWhereString(s);
          }
        }
        // �������������ʡ��������������� ɭ��NC
        else if ("ninvoicenum".equals(e.getKey()) || "nmoney".equals(e.getKey()) || "nexchangerate".equals(e.getKey())
            || "nassistnum".equals(e.getKey()) || "cassistunitname".equals(e.getKey())) {
          beforeEditBodyAssistUnitNumber(getBillCardPanel(), e.getRow());          
        }
        // �ɹ���Ʊ�������Ļ��ܵ���Ʊ���ޱ���¼�뵥����ʾ��
        else if (e.getKey().equals("noriginalcurprice")) {
          if (PuPubVO.getString_TrimZeroLenAsNull(getBillCardPanel().getHeadItem("ccurrencytypeid").getValue()) == null) {
            showHintMessage(getHeadHintText()
                + nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPP40040401-000256")/* @res"���������!" */);
            return false;
          }
        }
        else if (e.getKey().startsWith("vdef")){
            if(getBillCardPanel().getBodyItem(e.getKey()).getComponent() instanceof UIRefPane){
              ((UIRefPane)getBillCardPanel().getBodyItem(e.getKey()).getComponent()).getUITextField().setEditable(true);
            }
         }

      }
    }
    return true;
  }

  /**
   * ���ߣ���ά�� ���ܣ���ͷ��β�༭ǰ���� ������BillItemEvent e ��׽����BillItemEvent�¼� ���أ��� ���⣺��
   * ���ڣ�(2002-7-22 11:39:21) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־
   */
  public boolean beforeEdit(BillItemEvent e) {
    //<p>���ο������֧�� by zhaoyha at 2009.2.16
    boolean pluginRet=getInvokeEventProxy().beforeEdit(e);

    // ��ͷ

    // ����Ϊ�ִ����Կ����֯
    if (e.getSource().equals(getBillCardPanel().getHeadItem("cstoreorganization"))) {
      PuTool.restrictStoreOrg(getBillCardPanel().getHeadItem("cstoreorganization").getComponent(), false);
    }

    // �ù�Ӧ�����ƿ�������
    if(e.getSource().equals(getBillCardPanel().getHeadItem("caccountbankid"))){
        String cvendormangid = getBillCardPanel().getHeadItem("cvendormangid").getValue();
      String cvendorbaseid = (String) PiPqPublicUIClass.getAResultFromTable("bd_cumandoc", "pk_cubasdoc", "pk_cumandoc", cvendormangid);
      if(cvendorbaseid != null){
        setDefaultBankAccountForAVendor(cvendorbaseid);
      }
    }
    //��Ʊ����
    if("iinvoicetype".equals(e.getItem().getKey())){
      setCurInvoiceType();
    }
    
    return true;
  }

  /**
   * �༭ǰ���� �������ڣ�(2001-3-23 2:02:27)
   * 
   * @param e
   *          ufbill.BillEditEvent
   */
  private boolean beforeEditInvBillBodyMemo(BillEditEvent e) {

    // �������:ֹͣ�༭!!!!!!
    getBillCardPanel().stopEditing();

    Object ob = getBillCardPanel().getBodyValueAt(e.getRow(), "vmemo");
    ((UIRefPane) getBillCardPanel().getBodyItem("vmemo").getComponent()).setText((String) ob);

    return true;
  }

  public void bodyRowChange(BillEditEvent e) {
    /**
     * ���ο������֧�� by zhaoyha at 2009.2.16
     */
    getInvokeEventProxy().bodyRowChange(e);
  }

  /**
   * ���ߣ���ӡ�� ���ܣ����㱾�Ҽ����ҵĽ�˰���˰�ϼƼ��ۼƸ��� ������InvoiceVO invVO ������ķ�ƱVO
   * ���أ�������ϣ�����true�� ������������籾��δ�趨�޷���������������false������ʾ�û� ���⣺�� ���ڣ�(2002-3-13
   * 11:39:21) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־�� 2002-11-01 wyf
   * �����û�δ������ʣ���Ĭ�ϰ�����������㣬����Ʊδ������ʵ�����
   */
  private boolean calNativeAndAssistCurrValue(InvoiceVO invVO) {

    // ����ȡƱ������
    UFDouble nmoney, nsummny;
    String pk_corp = invVO.getHeadVO().getPk_corp();
    try {
      if (CurrParamQuery.getInstance().getLocalCurrPK(pk_corp) == null) {
        MessageDialog.showErrorDlg(this,
            nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPPSCMCommon-000059")/*
                                                                                           * @res
                                                                                           * "����"
                                                                                           */,
            nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPP40040401-000010")/*
                                                                                           * @res
                                                                                           * "δ�ɹ�ָ�����ң������������"
                                                                                           */);
        return false;
      }

      // wyf 2002-11-01 add begin
      // ����������Ƿ��Ϊ��
      String sHint = PiPqPublicUIClass.checkBothExchRateNull(invVO);
      if (sHint != null) {
        MessageDialog.showErrorDlg(this,
            nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPPSCMCommon-000059")/*
                                                                                           * @res
                                                                                           * "����"
                                                                                           */, sHint);
        return false;
      }
      // wyf 2002-11-01 add end

      String strRateDate = invVO.getHeadVO().getDarrivedate().toString();
      // ����
      for (int i = 0; i < invVO.getBodyVO().length; i++) {
        InvoiceItemVO itemVO = invVO.getBodyVO()[i];

        nmoney = POPubSetUI.getCurrArith_Busi(pk_corp).getAmountByOpp(itemVO.getCcurrencytypeid(),
            CurrParamQuery.getInstance().getLocalCurrPK(pk_corp), itemVO.getNoriginalcurmny(),
            itemVO.getNexchangeotobrate(), strRateDate);
        nsummny = POPubSetUI.getCurrArith_Busi(pk_corp).getAmountByOpp(itemVO.getCcurrencytypeid(),
            CurrParamQuery.getInstance().getLocalCurrPK(pk_corp), itemVO.getNoriginalsummny(),
            itemVO.getNexchangeotobrate(), strRateDate);
        // ��ֵ
        invVO.getBodyVO()[i].setNmoney(nmoney);
        invVO.getBodyVO()[i].setNsummny(nsummny);
        if (nsummny != null && nmoney != null) {
          invVO.getBodyVO()[i].setNtaxmny(nsummny.sub(nmoney));
        }
        // �ۼƸ���
        nmoney = POPubSetUI.getCurrArith_Finance(pk_corp).getAmountByOpp(itemVO.getCcurrencytypeid(),
            CurrParamQuery.getInstance().getLocalCurrPK(pk_corp),// POPubSetUI.getCurrArith_Busi(pk_corp).getLocalCurrPK(),
            itemVO.getNoriginalpaymentmny(), itemVO.getNexchangeotobrate(), strRateDate);
        invVO.getBodyVO()[i].setNpaymentmny(nmoney);
      }
    }
    catch (Exception e) {
      SCMEnv.out(e);
      MessageDialog
          .showErrorDlg(this,
              nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPPSCMCommon-000059")/*
                                                                                             * @res
                                                                                             * "����"
                                                                                             */,
              nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPP40040401-000012")/*
                                                                                             * @res
                                                                                             * "��������������������\n
                                                                                             * 1���Ƿ�ϵͳ������\n
                                                                                             * 2���Ƿ�ɹ�ָ�����ң����ң���\n
                                                                                             * 3���Ƿ�ָ��Ʊ�����ڵĻ��ʡ�\n��ʾ����¼����ʡ�"
                                                                                             */);
      return false;
    }

    return true;

  }

  /**
   * //���㱾�ҽ�����˰����Ҽ�˰�ϼ� �������ڣ�(2004-3-23 19:16:20)
   */
  public void computeValueFrmOriginal(int row) throws Exception {

    // �۱�����
    Object bRate = getBillCardPanel().getBodyValueAt(row, "nexchangeotobrate");
    if (bRate == null || bRate.toString().trim().length() == 0) {
      bRate = getBillCardPanel().getHeadItem("nexchangeotobrate").getValue();
      getBillCardPanel().setBodyValueAt(bRate, row, "nexchangeotobrate");
    }
    UFDouble ufBRate = new UFDouble(bRate.toString());

    // �۸�����
    // ԭ�ҽ��
    UFDouble ufNoriginalcurmny = PuPubVO.getUFDouble_ZeroAsNull(getBillCardPanel().getBodyValueAt(row,
        "noriginalcurmny"));
    UFDouble ufMoney = null;
    // ԭ��˰��
    UFDouble ufNoriginaltaxmny = PuPubVO.getUFDouble_ZeroAsNull(getBillCardPanel().getBodyValueAt(row,
        "noriginaltaxmny"));
    UFDouble ufTaxMny = null;
    // ԭ�Ҽ�˰�ϼ�
    UFDouble ufNoriginalsummny = PuPubVO.getUFDouble_ZeroAsNull(getBillCardPanel().getBodyValueAt(row,
        "noriginalsummny"));
    UFDouble ufSumMny = null;

    if (getBillCardPanel().getBodyValueAt(row, "ccurrencytypeid") == null
        || getBillCardPanel().getBodyValueAt(row, "ccurrencytypeid").toString().trim().equals("")) {
      String strCurrTypeId = ((UIRefPane) getBillCardPanel().getHeadItem("ccurrencytypeid").getComponent()).getRefPK();
      getBillCardPanel().setBodyValueAt(strCurrTypeId, row, "ccurrencytypeid");
    }
    try {
      // ԭ�ҽ��
      if (ufNoriginalcurmny == null || getBillCardPanel().getBodyValueAt(row, "ccurrencytypeid") == null) {
        ufMoney = null;
      }
      else {
        ufMoney = POPubSetUI.getCurrArith_Busi(getPk_corp()).getAmountByOpp(
            getBillCardPanel().getBodyValueAt(row, "ccurrencytypeid").toString(),
            CurrParamQuery.getInstance().getLocalCurrPK(getPk_corp()), ufNoriginalcurmny, ufBRate,
            nc.ui.po.pub.PoPublicUIClass.getLoginDate().toString());
      }
      getBillCardPanel().setBodyValueAt(ufMoney, row, "nmoney");
      // ԭ��˰��
      if (ufNoriginaltaxmny == null || getBillCardPanel().getBodyValueAt(row, "ccurrencytypeid") == null) {
        ufTaxMny = null;
      }
      else {
        ufTaxMny = POPubSetUI.getCurrArith_Busi(getPk_corp()).getAmountByOpp(
            getBillCardPanel().getBodyValueAt(row, "ccurrencytypeid").toString(),
            CurrParamQuery.getInstance().getLocalCurrPK(getPk_corp()), ufNoriginaltaxmny, ufBRate,
            nc.ui.po.pub.PoPublicUIClass.getLoginDate().toString());
      }
      getBillCardPanel().setBodyValueAt(ufTaxMny, row, "ntaxmny");
      // ԭ�Ҽ�˰�ϼ�
      if (ufNoriginalsummny == null || getBillCardPanel().getBodyValueAt(row, "ccurrencytypeid") == null) {
        ufSumMny = null;
      }
      else {
        ufSumMny = POPubSetUI.getCurrArith_Busi(getPk_corp()).getAmountByOpp(
            getBillCardPanel().getBodyValueAt(row, "ccurrencytypeid").toString(),
            CurrParamQuery.getInstance().getLocalCurrPK(getPk_corp()), ufNoriginalsummny, ufBRate,
            nc.ui.po.pub.PoPublicUIClass.getLoginDate().toString());
      }
      getBillCardPanel().setBodyValueAt(ufSumMny, row, "nsummny");

    }
    catch (Exception ex) {
      reportException(ex);
      throw ex;
    }
  }

  /**
   * //���㱾�ҽ�����˰����Ҽ�˰�ϼ� �������ڣ�(2004-3-23 19:16:20)
   */
  private void computeValueFrmOtherBill(InvoiceVO vo) throws Exception {

    InvoiceItemVO[] items = vo.getBodyVO();
    try {
      for (int i = 0; i < items.length; i++) {
        InvoiceItemVO itemVO = items[i];
        // ԭ�ҽ��
        UFDouble ufNoriginalcurmny = PuPubVO.getUFDouble_ZeroAsNull(itemVO.getNoriginalcurmny());
        UFDouble ufMoney = null;
        // ԭ��˰��
        UFDouble ufNoriginaltaxmny = PuPubVO.getUFDouble_ZeroAsNull(itemVO.getNoriginaltaxmny());
        UFDouble ufTaxMny = null;
        // ԭ�Ҽ�˰�ϼ�
        UFDouble ufNoriginalsummny = PuPubVO.getUFDouble_ZeroAsNull(itemVO.getNoriginalsummny());
        UFDouble ufSumMny = null;
        String strCurrTypeId = itemVO.getCcurrencytypeid();

        UFDate dOrderDate = vo.getHeadVO().getDinvoicedate();
        // ����������ʾ����
        // setRowDigits_ExchangeRate(getPk_corp(), iRow,
        // getInvBillPanel().getBillModel(),m_cardPoPubSetUI2);
        // ֵ
        UFDouble ufBRate = null;
        UFDouble ufARate = null;
        if (dOrderDate != null) {
          UFDouble[] daRate = null;
          String strCurrDate = dOrderDate.toString();
          if (strCurrDate == null || strCurrDate.trim().length() == 0) {
            strCurrDate = PoPublicUIClass.getLoginDate() + "";
          }
          daRate = m_listPoPubSetUI2.getBothExchRateValue(getPk_corp(), strCurrTypeId, new UFDate(strCurrDate));
          // �۱�����
          ufBRate = daRate[0];
          itemVO.setNexchangeotobrate(ufBRate);
        }
        // ���ҽ��
        if (ufNoriginalcurmny == null) {
          ufMoney = null;
        }
        else {
          ufMoney = POPubSetUI.getCurrArith_Busi(getPk_corp()).getAmountByOpp(items[i].getCcurrencytypeid(),
              CurrParamQuery.getInstance().getLocalCurrPK(getPk_corp()), ufNoriginalcurmny, ufBRate,
              nc.ui.po.pub.PoPublicUIClass.getLoginDate().toString());
        }
        itemVO.setNmoney(ufMoney);
        // ����˰��
        if (ufNoriginaltaxmny == null) {
          ufTaxMny = null;
        }
        else {
          ufTaxMny = POPubSetUI.getCurrArith_Busi(getPk_corp()).getAmountByOpp(items[i].getCcurrencytypeid(),
              CurrParamQuery.getInstance().getLocalCurrPK(getPk_corp()), ufNoriginaltaxmny, ufBRate,
              nc.ui.po.pub.PoPublicUIClass.getLoginDate().toString());
        }
        itemVO.setNtaxmny(ufTaxMny);
        // ���Ҽ�˰�ϼ�
        if (ufNoriginalsummny == null) {
          ufSumMny = null;
        }
        else {
          ufSumMny = POPubSetUI.getCurrArith_Busi(getPk_corp()).getAmountByOpp(items[i].getCcurrencytypeid(),
              CurrParamQuery.getInstance().getLocalCurrPK(getPk_corp()), ufNoriginalsummny, ufBRate,
              nc.ui.po.pub.PoPublicUIClass.getLoginDate().toString());
        }
        itemVO.setNsummny(ufSumMny);
      }
    }
    catch (Exception ex) {
      reportException(ex);
      throw ex;
    }

  }

  /**
   * ���ߣ���ά�� ���ܣ���Ʊ���ƴ��� ������ ���أ� ���⣺ ���ڣ�(2004-6-14 14:58:38) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
   */
  private void dealWhenCopy() {
    // ��ʾ״̬ͼƬ
    // V5 Del : setImageType(this.IMAGE_NULL);

    // ��ձ�ͷ����
    getBillCardPanel().getHeadItem("vinvoicecode").setValue(null);
    //getBillCardPanel().getHeadItem("vinvoicecode").setEdit(true);
    getBillCardPanel().getTailItem("iprintcount").setValue(new Integer(0));    
    getBillCardPanel().getHeadItem("cinvoiceid").setValue(null);
    getBillCardPanel().getHeadItem("ibillstatus").setValue(new Integer(0));
    getBillCardPanel().getHeadItem("ts").setValue(null);
    getBillCardPanel().getHeadItem("dinvoicedate").setValue(nc.ui.pub.ClientEnvironment.getInstance().getDate());
    getBillCardPanel().getHeadItem("darrivedate").setValue(nc.ui.pub.ClientEnvironment.getInstance().getDate());

    // �����Ƶ���
    ((UIRefPane) getBillCardPanel().getTailItem("coperator").getComponent()).setPK(nc.ui.pub.ClientEnvironment
        .getInstance().getUser().getPrimaryKey());
    ((UIRefPane) getBillCardPanel().getTailItem("dauditdate").getComponent()).setValue(null);
    getBillCardPanel().getTailItem("cauditpsn").setValue(null);
    getBillCardPanel().getTailItem("tmaketime").setValue(null);
    getBillCardPanel().getTailItem("taudittime").setValue(null);
    getBillCardPanel().getTailItem("tlastmaketime").setValue(null);

    try {
      // ���˴������,��֤��ҵ�����������д���ҵ������ʱ�滻�����������д�������
      setInventoryRefFilter(getBillCardPanel().getHeadItem("cbiztype").getValue());
    }
    catch (Exception e) {
      SCMEnv.out(e);
    }
    // ��ձ�������
    BillModel bm = getBillCardPanel().getBillModel();
    //����czpҪ��,���������൱������,����������Ϣ by zhaoyha at 2009.1.8
    for (int i = 0; i < bm.getRowCount(); i++) {
      bm.setRowState(i, BillModel.ADD);
       bm.setValueAt(null, i, "corderid");
       bm.setValueAt(null, i, "corder_bid");
      bm.setValueAt(null, i, "naccumorignsettmny");
      bm.setValueAt(null, i, "naccumsettmny");
      bm.setValueAt(null, i, "naccumsettnum");
       bm.setValueAt(null, i, "cupsourcehts");
       bm.setValueAt(null, i, "cupsourcebts");
       bm.setValueAt(null, i, "csourcehts");
       bm.setValueAt(null, i, "csourcebts");
      bm.setValueAt(null, i, "npaymentmny");
      bm.setValueAt(null, i, "noriginalpaymentmny");
      bm.setValueAt(null, i, "cinvoice_bid");
      bm.setValueAt(null, i, "cinvoiceid");
       bm.setValueAt(null, i, "cupsourcebillrowid");
       bm.setValueAt(null, i, "cupsourcebilltype");
       bm.setValueAt(null, i, "cupsourcebillid");
       bm.setValueAt(null, i, "csourcebillid");
       bm.setValueAt(null, i, "csourcebillrowid");
       bm.setValueAt(null, i, "csourcebilltype");
       bm.setValueAt(null, i, "csourcebillname");
       bm.setValueAt(null, i, "csourcebillcode");
       bm.setValueAt(null, i, "csourcebillrowno");
       bm.setValueAt(null, i, "cancestorbillname");
       bm.setValueAt(null, i, "cancestorbillcode");
       bm.setValueAt(null, i, "cancestorbillrowno");
      bm.setValueAt(null, i, "ts");
    }
    getBillCardPanel().getBodyItem("invcode").setEnabled(true);
  }

  /**
   * ���ߣ���ӡ�� ���ܣ�ִ�п�Ƭ��ѯ,�ɿ�Ƭ��"��ѯ"��"ˢ��"���� ������InvQueDlg curDlg ��ѯ�Ի��� ���أ��� ���⣺��
   * ���ڣ�(2002-3-13 11:39:21) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־�� 2002-11-14 wyf �����LIKE����
   */
  private void execBillQuery(InvQueDlg curDlg) {

    showHintMessage(getHeadHintText() +nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401","UPP40040401-000013")/*@res "���ڲ�ѯ"*/ + CommonConstant.SPACE_MARK + CommonConstant.BEGIN_MARK + "......." + CommonConstant.END_MARK);

    NormalCondVO[] normalVOs = curDlg.getNormalCondVOs();
    ConditionVO[] definedVOs = curDlg.getConditionVO();

    //wyf 2002-11-14  add begin
    PiPqPublicUIClass.processLIKEInCondVOs(definedVOs);
    //wyf 2002-11-14  add end
    
    
    InvoiceVO[] VOs = null;
    try {
      VOs = InvoiceHelper.queryInvoiceVOsByCondsMy(getClientEnvironment().getUser().getPrimaryKey(), normalVOs, definedVOs);
    } catch (java.lang.Exception e) {
      SCMEnv.out(e);
      MessageDialog.showHintDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401","UPPSCMCommon-000270")/*@res "��ʾ"*/, nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401","UPP40040401-000014")/*@res "ϵͳ����!"*/);
      return;
    }

    // û�з��������ķ�Ʊ
    if (VOs == null) {
      setInvVOs(null);
      MessageDialog.showHintDlg(this,
          nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPPSCMCommon-000270")/*
                                                                                         * @res
                                                                                         * "��ʾ"
                                                                                         */,
          nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPP40040401-000015")/*
                                                                                         * @res
                                                                                         * "û�з��ϲ�ѯ�����ķ�Ʊ!"
                                                                                         */);
      getBillCardPanel().addNew();
      return;
    }

    // ���÷�ƱVO����
    for (int i = 0; i < VOs.length; i++) {
      VOs[i].setSource(InvoiceVO.FROM_QUERY);
    }
    setInvVOs(VOs);
    setCurVOPos(0);

    // ���ÿ�Ƭ���б����ʾ״̬
    setBillBrowseState(STATE_BROWSE_NORMAL);
    setListOperState(STATE_LIST_NORMAL);

    // ���ÿ�Ƭ��������
    setVOToBillPanel();
  }

  /**
   * ���ߣ���ά�� ���ܣ�����ͷ��ʽ���ݣ����ⲻ�ɲ��ճ������� ������ ���أ� ���⣺ ���ڣ�(2004-5-20 14:18:41)
   * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
   * 
   * @param vo
   *          nc.vo.pr.pray.PraybillVO
   */
  public void execHeadTailFormula(InvoiceVO vo) {
    if (vo == null) {
      return;
    }
    // ������ʽ
    InvoiceHeaderVO voHead = vo.getHeadVO();
    UIRefPane refpane = null;

    String[] saFormula = new String[] {
      "getColValue(bd_calbody,bodyname,pk_calbody,cstoreorganization)"
    };
    PuTool.getFormulaParse().setExpressArray(saFormula);

    int iFormulaLen = saFormula.length;
    for (int i = 0; i < iFormulaLen; i++) {
      PuTool.getFormulaParse().addVariable("cstoreorganization", voHead.getCstoreorganization());
    }

    String[][] saRet = PuTool.getFormulaParse().getValueSArray();

    refpane = (UIRefPane) getBillCardPanel().getHeadItem("cstoreorganization").getComponent();

    if (nc.vo.scm.pu.PuPubVO.getString_TrimZeroLenAsNull(refpane.getUITextField().getText()) == null) {
      if (saRet != null && saRet[0] != null)
        refpane.getUITextField().setText(saRet[0][0]);
    }
  }

  /**
   * ���ߣ���ӡ�� ���ܣ�ִ���б��ѯ,���б��"��ѯ"��"ˢ��"���� ������InvQueDlg curDlg ��ѯ�Ի��� ���أ��� ���⣺��
   * ���ڣ�(2002-3-13 11:39:21) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־�� 2002-11-14 wyf �����LIKE����
   */
  private void execListQuery(InvQueDlg curDlg) {

    showHintMessage(getHeadHintText() + nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401","UPP40040401-000013")/*@res "���ڲ�ѯ"*/ + "......." );

    NormalCondVO[] normalVOs = curDlg.getNormalCondVOs();
    ConditionVO[] definedVOs = curDlg.getConditionVO();
    //δ��ѯֱ�ӵ�ˢ��
    if(definedVOs == null){
      return ;
    }
    //wyf 2002-11-14  add begin
    PiPqPublicUIClass.processLIKEInCondVOs(definedVOs);
    //wyf 2002-11-14  add end

    
    InvoiceVO[] VOs = null;
    try {
      VOs = InvoiceHelper.queryInvoiceVOsByCondsMy(getClientEnvironment().getUser().getPrimaryKey(), normalVOs, definedVOs);
    }
    catch (java.lang.Exception e) {
      SCMEnv.out(e);
      MessageDialog.showHintDlg(this,
          nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPPSCMCommon-000270")/*
                                                                                         * @res
                                                                                         * "��ʾ"
                                                                                         */,
          nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPP40040401-000014")/*
                                                                                         * @res
                                                                                         * "ϵͳ����!"
                                                                                         */);
      return;
    }

    // û�з��������ķ�Ʊ
    if (VOs == null) {
      setInvVOs(null);
      MessageDialog.showHintDlg(this,
          nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPPSCMCommon-000270")/*
                                                                                         * @res
                                                                                         * "��ʾ"
                                                                                         */,
          nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPP40040401-000015")/*
                                                                                         * @res
                                                                                         * "û�з��ϲ�ѯ�����ķ�Ʊ!"
                                                                                         */);
      setVOsToListPanel();
      return;
    }

    // ���÷�ƱVO����
    for (int i = 0; i < VOs.length; i++) {
      VOs[i].setSource(InvoiceVO.FROM_QUERY);
    }
    setInvVOs(VOs);

    // ��ʾ�б�ǰ����VO,���Ծ�����ʽ�����ֵ��ֵ
    setCurVOPos(0);
    setVOsToListPanel();

    // ���ÿ�Ƭ���б����ʾ״̬
    setBillBrowseState(STATE_BROWSE_NORMAL);
    setListOperState(STATE_LIST_NORMAL);
    setCurOperState(getListOperState());
  }

  /**
   * �˴����뷽��˵���� ��������:���ǵ����� ���ߣ���ά�� �������: ����ֵ: �쳣����: ����:
   */
  private void filterNullLine() {
    // �����ֵ�ݴ�
    Object oTempValue = null;
    // ����model
    nc.ui.pub.bill.BillModel bmBill = getBillCardPanel().getBillModel();

    // ����кţ�Ч�ʸ�һЩ��
    int iInvCol = bmBill.getBodyColByKey(m_sInvMngIDItemKey);

    // �����д����
    if (bmBill != null && iInvCol >= 0 && iInvCol < bmBill.getColumnCount()) {
      // ����
      int iRowCount = getBillCardPanel().getRowCount();
      // �Ӻ���ǰɾ
      for (int line = iRowCount - 1; line >= 0; line--) {
        // ���δ��
        oTempValue = bmBill.getValueAt(line, iInvCol);
        if (oTempValue == null || oTempValue.toString().trim().length() == 0)
          // ɾ��
          getBillCardPanel().getBillModel().delLine(new int[] {
            line
          });
      }
    }
    if (bmBill.getRowCount() <= 0 && isCouldMaked())
      onAppendLine();
  }

  /**
   * �˴����뷽��˵���� �������ڣ�(2001-9-19 9:38:47)
   * 
   * @return java.lang.String
   */
  public int getBillBrowseState() {
    return m_nBillBrowseState;
  }

  /**
   * ����ʵ�ָ÷���������ҵ�����ı��⡣
   * 
   * @version (00-6-6 13:33:25)
   * @return java.lang.String
   */
  private String getCauditid() {

    return m_cauditid;
  }

  /**
   * �����Ĺ��ܡ���;�������Եĸ��ģ��Լ�����ִ��ǰ������״̬��
   * 
   * @param ����˵��
   * @return ����ֵ
   * @exception �쳣����
   * @see ��Ҫ�μ�����������
   * @since �������һ���汾���˷�������ӽ���
   * @return java.lang.String
   */
  private java.lang.String getCurBizeType() {
    return m_strCurBizeType;
  }

  /**
   * ����ԭ�Ҽ��㱾��
   */
  private String getCurOperator() {
    return nc.ui.pub.ClientEnvironment.getInstance().getUser().getPrimaryKey();
  }

  /**
   * �õ���ǰ����Ĳ���״̬
   * 
   * @param ����˵��
   * @return ����ֵ
   * @exception �쳣����
   * @see ��Ҫ�μ�����������
   * @since �������һ���汾���˷�������ӽ���
   * @return java.lang.String
   */
  public int getCurOperState() {
    return m_nCurOperState;
  }

  /**
   * �õ���ǰ����PANEL
   * 
   * @param ����˵��
   * @return ����ֵ
   * @exception �쳣����
   * @see ��Ҫ�μ�����������
   * @since �������һ���汾���˷�������ӽ���������ѡ��
   * @deprecated �÷����������һ���汾���Ѿ������������滻������ѡ��
   */
  private Component getCurPanel() {
    if (getCurPanelMode() == INV_PANEL_CARD)
      return getBillCardPanel();
    else if (getCurPanelMode() == INV_PANEL_LIST)
      return getBillListPanel();
    return null;

  }

  /**
   * �õ���ǰ�Ľ���PANEL������
   * 
   * @param ����˵��
   * @return ����ֵ
   * @exception �쳣����
   * @see ��Ҫ�μ�����������
   * @since �������һ���汾���˷�������ӽ���
   * @return int
   */
  private int getCurPanelMode() {
    return m_nCurPanelMode;
  }

  /**
   * �õ���ǰ��ʾ��VO��λ��
   * 
   * @param ����˵��
   * @return ����ֵ
   * @exception �쳣����
   * @see ��Ҫ�μ�����������
   * @since �������һ���汾���˷�������ӽ���
   * @return nc.vo.pi.InvoiceVO[]
   */
  private int getCurVOPos() {

    return m_nCurInvVOPos;
  }

  /**
   * ����ʵ�ָ÷���������ҵ�����ı��⡣
   * 
   * @version (00-6-6 13:33:25)
   * @return java.lang.String
   */
  private String getHeadHintText() {

    return m_strHeadHintText;
  }

  /**
   * �õ���Ƭ����
   * 
   * @param ����˵��
   * @return ����ֵ
   * @exception �쳣����
   * @see ��Ҫ�μ�����������
   * @since �������һ���汾���˷�������ӽ���������ѡ��
   */
  public InvBillPanel getBillCardPanel() {
    if (m_billPanel == null) {
      try {
        m_billPanel = new InvBillPanel(nc.ui.pub.ClientEnvironment.getInstance(), this);
      }
      catch (java.lang.Exception e) {
        SCMEnv.out(e);
        MessageDialog.showHintDlg(this,NCLangRes.getInstance().getStrByID("40040401", "UPPSCMCommon-000270")/*@res"��ʾ"*/, e.getMessage());
        return null;
      }
      
      // ��ͷ�༭ǰ�¼�����
      m_billPanel.setBillBeforeEditListenerHeadTail(this);
      
      // ���в�ͬ�������ý���
      new DefaultCurrTypeBizDecimalListener(m_billPanel.getBillModel(), "ccurrencytypeid", m_itemMny_bu);
      new DefaultCurrTypeDecimalAdapter(m_billPanel.getBillModel(), "ccurrencytypeid", m_itemMny_fi);
      // �����������
      m_billPanel.getBillModel().addSortRelaObjectListener2(new IBillRelaSortListener2(){
        public Object[] getRelaSortObjectArray() {
          return getCurrBodyVOs();
        }
      });

      //since v56, ���ò��������ĵ���Ŀ(ԭ���ǣ�������Ҫ����Ӧ�á������Լ���������Ϊ Ŀǰ������������֧�ֲ�������)
      PuTool.setBatchModifyForbid(m_billPanel, new String[]{"vfree0"});
    }
    
    return m_billPanel;
  }

  /**
   * ���з��ϲ�ѯ�����ķ�ƱHVO
   * 
   * @param ����˵��
   * @return ����ֵ
   * @exception �쳣����
   * @see ��Ҫ�μ�����������
   * @since �������һ���汾���˷�������ӽ���
   * @return nc.vo.pi.InvoiceVO[]
   */
  private nc.vo.pi.InvoiceHeaderVO[] getInvHVOs() {

    if (getInvVOs() == null)
      return null;

    Vector vec = new Vector();
    for (int i = 0; i < getInvVOs().length; i++) {
      vec.addElement(getInvVOs()[i].getHeadVO());
    }
    InvoiceHeaderVO[] hVO = new InvoiceHeaderVO[getInvVOs().length];
    vec.copyInto(hVO);

    return hVO;
  }

  /**
   * �õ���Ʊ�б�PANEL
   * 
   * @param ����˵��
   * @return ����ֵ
   * @exception �쳣����
   * @see ��Ҫ�μ�����������
   * @since �������һ���汾���˷�������ӽ���������ѡ��
   * @deprecated �÷����������һ���汾���Ѿ������������滻������ѡ��
   * @return nc.ui.pa.mi.InvListPanel
   */
  public InvListPanel getBillListPanel() {
    if (m_listPoPubSetUI2 == null) {
      m_listPoPubSetUI2 = new POPubSetUI2();
    }

    if (m_listPanel == null) {
      try {
        m_listPanel = new InvListPanel(nc.ui.pub.ClientEnvironment.getInstance(),this);

        m_listPanel.getHeadBillModel().addSortRelaObjectListener2(this);
      }
      catch (java.lang.Throwable ivjExc) {
        SCMEnv.out(ivjExc.toString());
        MessageDialog.showHintDlg(this,
            nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPP40040401-000016")/*
                                                                                           * @res
                                                                                           * "ϵͳ����"
                                                                                           */,
            nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPP40040401-000017")/*
                                                                                           * @res
                                                                                           * "�б�ģ�����ʧ�ܣ�"
                                                                                           */);
        return null;
      }
      m_listPanel.getHeadItem("cauditpsn").setShow(false);
      initListListener();

      new DefaultCurrTypeBizDecimalListener(m_listPanel.getBodyBillModel(), "ccurrencytypeid", m_itemMny_bu);
      new DefaultCurrTypeDecimalAdapter(m_listPanel.getBodyBillModel(), "ccurrencytypeid", m_itemMny_fi);

      // since v55, ֧������ѡ��
//      PuTool.setLineSelectedList(m_listPanel);
      // �����������
      m_listPanel.getBodyBillModel().addSortRelaObjectListener2(new IBillRelaSortListener2(){
        public Object[] getRelaSortObjectArray() {
          return getCurrBodyVOs();
          }
      });
    }

    return m_listPanel;
  }

  /**
   * ���з��ϲ�ѯ�����ķ�ƱVO
   * 
   * @param ����˵��
   * @return ����ֵ
   * @exception �쳣����
   * @see ��Ҫ�μ�����������
   * @since �������һ���汾���˷�������ӽ���
   * @return nc.vo.pi.InvoiceVO[]
   */
  private nc.vo.pi.InvoiceVO[] getInvVOs() {
    return m_InvVOs;
  }

  /**
   * V55 ��������ר�ã����淢ƱVO�����ǵ��༭ʱɾ��������ӽ������µõ�ITEMVO.
   * 
   * @param ����˵��
   * @return ����ֵ
   * @exception �쳣����
   * @see ��Ҫ�μ�����������
   * @since �������һ���汾���˷�������ӽ���
   * @return nc.vo.pi.InvoiceVO[]
   */
  public nc.vo.pi.InvoiceVO[] getInvVOsByContinue() {
    int bodyNum = getBillCardPanel().getRowCount();
    if (bodyNum < 1) {
      return null;
    }
    if (m_InvVOs == null) {
      return null;
    }
    InvoiceVO billVO = new InvoiceVO(bodyNum);
    getBillCardPanel().getBillValueVO(billVO);
    //�������û���,ֻ�ӽ�����ȡ���ܴ������ʵľ��Ȳ���ȷ
    // by zhaoyha at 2009.6.8
    InvoiceItemVO[] invoiceItemVO = billVO.getBodyVO();
    for (int i=0;i<invoiceItemVO.length;++i) {
    UFDouble nexchgRate=(UFDouble) getBillCardPanel().getBodyValueAt(i, "nexchangeotobrate");
    invoiceItemVO[i].setNexchangeotobrate(nexchgRate);
    
  }
    m_InvVOs[getCurVOPos()].setChildrenVO(invoiceItemVO);
    return m_InvVOs;
  }

  /**
   * �˴����뷽��˵���� �������ڣ�(2001-9-14 21:19:56)
   * 
   * @return java.lang.String
   */
  private int getListOperState() {
    return m_nListOperState;
  }

  /**
   * ����ģ����� �������ڣ�(2000-10-17 15:10:42)
   * 
   * @return java.lang.String
   */
  //For V56 by zhaoyha
//  public String getModuleCode() {
//    return ConstantVO.NODE_INV_MAINTAIN;
//  }

  /**
   * ��Ŀ�׶β���
   */
  private int getOldCurrMoneyDigit() {

    return m_oldCurrMoneyDigit;
  }

  /**
   * �˴����뷽��˵���� �������ڣ�(2001-9-14 12:03:06)
   * 
   * @return int[]
   */
  private String getPk_corp() {
    return nc.ui.pub.ClientEnvironment.getInstance().getCorporation().getPrimaryKey();
  }

  /**
   * ���ߣ���ӡ�� ���ܣ��õ���Ʊ�Ĳ�ѯ�����Ի��� �������� ���أ��� ���⣺�� ���ڣ�(2002-4-22 11:39:21)
   * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־�� 2002-06-07 wyf ȡĬ�ϲ�ѯģ��
   */
  public InvQueDlg getQueDlg() {

    if(m_InvQueDlg == null ){
      m_InvQueDlg = new InvoiceUIQueDlg(this,getModuleCode(),null,ScmConst.PO_Invoice, getCurOperator(),getPk_corp()) ;
      //����V56���󣬵������ڲ�ѯ���� by zhaoyha at 2009.9
      m_InvQueDlg.addCurToCurDate("po_invoice.dinvoicedate");
      //�����Զ���������
      nc.ui.scm.pub.def.DefSetTool.updateQueryConditionClientUserDef(m_InvQueDlg,
      //��ǰģ��
      getPk_corp(), //��˾����
      ScmConst.PO_Invoice,//��������
      "po_invoice.vdef", //����ģ���е���ͷ���Զ�����ǰ׺
      "po_invoice_b.vdef" //����ģ���е�������Զ�����ǰ׺
      );
    }

    return m_InvQueDlg ;
  }

  /**
   * ���ߣ���ӡ�� ���ܣ��ӽ���õ��豣���VO �������� ���أ��� ���⣺�� ���ڣ�(2001-5-13 11:39:21)
   * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־�� 2002-06-07 wyf ȥ�����ֲɹ�����ί�ⷢƱ�Ĵ��� 2002-11-12 wyf
   * ����Ե���ģ��ı������� 2002-11-18 wyf �޸��Ѵ��ڵĵ���ʱ���Ƶ��˲��޸�
   */
  private InvoiceVO getSavedInvVOFromBill() {

    // =======�õ������϶�ӦVO(billVO),���ڱ���ǰ��У��
    int bodyNum = getBillCardPanel().getRowCount();
    if (bodyNum < 1) {
      MessageDialog.showHintDlg(this,
          nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPPSCMCommon-000270")/*
                                                                                         * @res
                                                                                         * "��ʾ"
                                                                                         */,
          nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPP40040401-000018")/*
                                                                                         * @res
                                                                                         * "�����뷢Ʊ��ϸ��"
                                                                                         */);
      return null;
    }
    InvoiceVO billVO = new InvoiceVO(bodyNum);
    //���������۱����ʵ�ת����������ӽ���ȥ��ֵʱ����value�ľ��ȱ�С������
    if (getBillCardPanel().getBillData().getBodyItem("nexchangeotobrate") != null){
      BillItem exchangeItem = getBillCardPanel().getBillData().getBodyItem("nexchangeotobrate");
      exchangeItem.setConverter(new nc.ui.scm.pub.SCMUFDoubleConverter());
    }
    getBillCardPanel().getBillValueVO(billVO);

    // =======�õ��豣��ķ�Ʊ����������
    // ===��ͷ
    // ����ͷ�е�ĳЩ���Է������ĺ󣬿��ܵ�������Ϊ�գ�����Ϊ�丳ֵ
    InvoiceHeaderVO head = billVO.getHeadVO();
    // ҵ������
    if(PuPubVO.getString_TrimZeroLenAsNull(head.getCbiztype())==null)
      head.setCbiztype(getCurBizeType());
    // ������Դ
    if (getInvVOs() != null && getInvVOs().length > 0) {
      InvoiceVO vo = getInvVOs()[getCurVOPos()];
      billVO.setSource(vo.getSource());
    }

    // iinvoicetype
    UIComboBox comItem = (UIComboBox) getBillCardPanel().getHeadItem("iinvoicetype").getComponent();
    head.setIinvoicetype(new Integer(comItem.getSelectedIndex()));
    // finitflag
    UICheckBox initCheck = (UICheckBox) getBillCardPanel().getHeadItem("finitflag").getComponent();
    if (initCheck.isSelected()) {
      head.setFinitflag(new Integer(1));
    }
    else {
      head.setFinitflag(new Integer(0));
    }
    // cvendorbaseid
    if ((head.getCvendorbaseid() == null || head.getCvendorbaseid().trim().equals(""))
        && (head.getCvendormangid() != null && head.getCvendormangid().trim() != "")) {
      // String cvendorbaseid =
      // getResultFromFormula("getColValue(bd_cumandoc,pk_cubasdoc,pk_cumandoc,cvendormangid)","cvendormangid",head.getCvendormangid())
      // ;
      String cvendorbaseid = (String) PiPqPublicUIClass.getAResultFromTable("bd_cumandoc", "pk_cubasdoc",
          "pk_cumandoc", head.getCvendormangid());
      head.setCvendorbaseid(cvendorbaseid);
    }
    // cvendormaneid
    if ((head.getCvendormangid() == null || head.getCvendormangid().trim().equals(""))
        && (head.getCvendorbaseid() != null && head.getCvendorbaseid().trim() != "")) {
      // String cvendorbaseid =
      // getResultFromFormula("getColValue(bd_cumandoc,pk_cubasdoc,pk_cumandoc,cvendormangid)","cvendormangid",head.getCvendormangid())
      // ;
      String cvendormangeid = (String) PiPqPublicUIClass.getAResultFromTable("bd_cumandoc", "pk_cumandoc",
          "pk_cubasdoc", head.getCvendorbaseid());
      head.setCvendormangid(cvendormangeid);
    }
    // pk_corp caccountyear coperator ibillstatus dr cbilltype vmemo
    head.setPk_corp(nc.ui.pub.ClientEnvironment.getInstance().getCorporation().getPrimaryKey());
    head.setCaccountyear(nc.ui.pub.ClientEnvironment.getInstance().getAccountYear());

    // wyf 2002-11-18 modify begin
    // head.setCoperator(nc.ui.pub.ClientEnvironment.getInstance().getUser().getPrimaryKey())
    // ;
    if (head.getCinvoiceid() == null || head.getCinvoiceid().trim().equals("")) {
      head.setCoperator(nc.ui.pub.ClientEnvironment.getInstance().getUser().getPrimaryKey());
    }
    // wyf 2002-11-18 modify end
    // czp 2009-10-22 modify bgn
    if(head.getIbillstatus() == null || head.getIbillstatus().intValue() != 2){
        head.setIbillstatus(new Integer(0));
    }
    // czp 2009-10-22 modify end
    head.setDr(new Integer(0));
    // ������ί�⻹�ǲɹ�
    head.setCbilltype(nc.vo.scm.pu.BillTypeConst.PO_INVOICE);
    int iLen = billVO.getBodyVO().length;
    if(getBillCardPanel().getHeadItem("vmemo").getComponent() instanceof UIRefPane){
      UIRefPane nRefPanel = (UIRefPane) getBillCardPanel().getHeadItem("vmemo").getComponent();
      UITextField vMemoField = nRefPanel.getUITextField();
      head.setVmemo(vMemoField.getText());
    }

    // head.setVmemo(
    // ((UIRefPane)getInvBillPanel().getHeadItem("vmemo").getComponent()).getText()
    // ) ;

    // ===����
    for (int i = 0; i < iLen; i++) {
      // pk_corp dr
      billVO.getBodyVO()[i].setPk_corp(head.getPk_corp());
      billVO.getBodyVO()[i].setDr(head.getDr());
      // cbaseid
      if (billVO.getBodyVO()[i].getCbaseid() == null || billVO.getBodyVO()[i].getCbaseid().trim().equals("")) {
        String cbaseid = (String) PiPqPublicUIClass.getAResultFromTable("bd_invmandoc", "pk_invbasdoc", "pk_invmandoc",
            billVO.getBodyVO()[i].getCmangid());
        billVO.getBodyVO()[i].setCbaseid(cbaseid);
      }

      // ��ͷ���֣��۱����۸����ʴ�������
      // billVO.getBodyVO()[i].setCcurrencytypeid(head.getCcurrencytypeid()) ;
      // billVO.getBodyVO()[i].setNexchangeotobrate(head.getNexchangeotobrate())
      // ;
      // billVO.getBodyVO()[i].setNexchangeotoarate(head.getNexchangeotoarate())
      // ;
      // ���ԭ��ĳ������Ϊ��,����Ϊ0
      UFDouble dTemp = new UFDouble(0.0);
      if (billVO.getBodyVO()[i].getNtaxrate() == null)
        billVO.getBodyVO()[i].setNtaxrate(dTemp);
      if (billVO.getBodyVO()[i].getNoriginalcurmny() == null)
        billVO.getBodyVO()[i].setNoriginalcurmny(dTemp);
      if (billVO.getBodyVO()[i].getNoriginalcurprice() == null)
        billVO.getBodyVO()[i].setNoriginalcurprice(dTemp);
      if (billVO.getBodyVO()[i].getNoriginalpaymentmny() == null)
        billVO.getBodyVO()[i].setNoriginalpaymentmny(dTemp);
      if (billVO.getBodyVO()[i].getNoriginalsummny() == null)
        billVO.getBodyVO()[i].setNoriginalsummny(dTemp);
      if (billVO.getBodyVO()[i].getNoriginaltaxmny() == null)
        billVO.getBodyVO()[i].setNoriginaltaxmny(dTemp);
      setExchangeRateBody(i, true, null);
      // //�۱�����
      UFDouble nexchangeotobrate = billVO.getBodyVO()[i].getNexchangeotobrate();
      // ��˾��Ӧ�ı�����Ϣ
      String pk_corp = billVO.getHeadVO().getPk_corp();
      String[] value = new String[] {
        new Integer(i + 1).toString()
      };
      if (nexchangeotobrate == null
          || (nexchangeotobrate != null && (nexchangeotobrate.doubleValue() < 0 || nexchangeotobrate.doubleValue() == 0))) {
        // return "��ͷ��\n�۱����ʲ���Ϊ�գ�" ;
        MessageDialog.showWarningDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON",
            "UPPSCMCommon-000270")/* @res "��ʾ" */, nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401",
            "UPP40040401-000236", null, value)/* @res "������{0}\n�۱����ʲ���Ϊ��" */);
        return null;
      }
    }
    //�õ�һ�б������ͷ���ֻ���
    if(StringUtil.isEmptyWithTrim(head.getCcurrencytypeid())||
        UFDouble.ZERO_DBL.equals(PuPubVO.getUFDouble_NullAsZero(
            head.getNexchangeotobrate()))){
      head.setCcurrencytypeid(billVO.getBodyVO()[0].getCcurrencytypeid());
      head.setNexchangeotobrate(billVO.getBodyVO()[0].getNexchangeotobrate());
    }
    
      
    
    // wyf 2002-11-12 add begin
    // ������ı�������
    try {
      PuTool.validateNotNullField(getBillCardPanel());
    }
    catch (Exception e) {
      MessageDialog.showHintDlg(this,
          nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPPSCMCommon-000270")/*
                                                                                         * @res
                                                                                         * "��ʾ"
                                                                                         */, e.getMessage());
      return null;
    }
    // wyf 2002-11-12 add end

    // =======��������������
    if (!calNativeAndAssistCurrValue(billVO)) {
      return null;
    }

    // //////////////��Ч�Լ��--------ֻ�Խ�������ʾ�Ľ��м��
    try {
      billVO.validate();
    }
    catch (ValidationException e) {
      MessageDialog.showHintDlg(this,
          nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPPSCMCommon-000270")/*
                                                                                         * @res
                                                                                         * "��ʾ"
                                                                                         */, e.getMessage());
      return null;
    }
    catch (HintMessageException e) {
      int ret = MessageDialog.showYesNoDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401",
          "UPPSCMCommon-000270")/* @res "��ʾ" */, e.getMessage()
          + nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPP40040401-000159")/*
                                                                                           * @res
                                                                                           * "���Ƿ�������棿"
                                                                                           */);
      if (ret == MessageDialog.ID_NO) {
        return null;
      }
    }
    // =======�ı����������������޸ģ�ɾ��
    InvoiceItemVO[] changedBodyVOs = (InvoiceItemVO[]) getBillCardPanel().getBillModel().getBodyValueChangeVOs(
        InvoiceItemVO.class.getName());
    if (changedBodyVOs != null && changedBodyVOs.length != 0) {
      Vector vec = new Vector();
      for (int i = 0; i < changedBodyVOs.length; i++) {
        if (changedBodyVOs[i].getStatus() == VOStatus.DELETED && changedBodyVOs[i].getCinvoice_bid() != null
            && changedBodyVOs[i].getCinvoice_bid().trim().length() > 0) {
          vec.addElement(changedBodyVOs[i]);
        }
      }
      if (vec.size() != 0) {
        for (int i = 0; i < billVO.getBodyVO().length; i++) {
          vec.addElement(billVO.getBodyVO()[i]);
        }
        InvoiceItemVO[] itemVOs = new InvoiceItemVO[vec.size()];
        vec.copyInto(itemVOs);
        billVO.setChildrenVO(itemVOs);
      }
    }

    return billVO;
  }

  /**
   * ���ߣ���ӡ�� ���ܣ��õ��豣���VO �������� ���أ��� ���⣺�� ���ڣ�(2002-4-22 11:39:21)
   * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
   */
  private InvoiceVO getSavedVO() {
    // �ӽ���õ���VO,�������������ȵ�VO
    InvoiceVO retVO = getSavedInvVOFromBill();
    if (retVO == null) {
      return null;
    }
    String sKey = retVO.getHeadVO().getCinvoiceid();
    String sUpsourceBillType = retVO.getBodyVO()[0].getCupsourcebilltype();
    if (sUpsourceBillType != null && sUpsourceBillType.trim().length() > 0 && sUpsourceBillType.equals("50")) {
      retVO.setSource(InvoiceVO.FROM_ORDER);
    }
    // ���÷�Ʊ��Դ

    // ��Ʊ�޸�
    if (sKey != null && sKey.trim().length() > 0) {
      retVO.setSource(InvoiceVO.FROM_QUERY);
      // ���Ƶĵ��ݣ������ĵ���
    }
    else if (sUpsourceBillType == null || sUpsourceBillType.trim().length() == 0) {
      retVO.setSource(InvoiceVO.FROM_HAND);
    }
    else if (sUpsourceBillType != null && sUpsourceBillType.trim().length() > 0) {
      if (sUpsourceBillType.equals(ScmConst.PO_Order) || sUpsourceBillType.equals(ScmConst.SC_Order))
        retVO.setSource(InvoiceVO.FROM_ORDER);
      else if (sUpsourceBillType.equals("45") || sUpsourceBillType.equals("47") || sUpsourceBillType.equals("4T"))
        retVO.setSource(InvoiceVO.FROM_STO);
      else
        retVO.setSource(InvoiceVO.FROM_VMI);
    }
    if (m_bCopy) {
      retVO.setSource(InvoiceVO.FROM_HAND);// �����ĵ���
    }

    // ���ƻ򿽱��򻺴������VO
    if (retVO.getSource() == InvoiceVO.FROM_HAND) {
      if (getInvVOs() == null || getInvVOs().length == 0)
        addNewOneIntoInvVOs(retVO);
      // ��ε�����棬ֻ�򻺴����һ��VO
      else {
        String sUpBillType = getInvVOs()[getCurVOPos()].getBodyVO()[0].getCupsourcebilltype();
        if (sUpBillType != null && sUpBillType.trim().length() > 0)
          addNewOneIntoInvVOs(retVO);
      }
    }

    return retVO;
  }

  /**
   * �˴����뷽��˵���� �������ڣ�(2003-11-4 19:59:42)
   */
  private ArrayList getSelectedBills() {

    ArrayList aryRet = new ArrayList();
    InvoiceVO[] allvos = null;
    // ȫ��ѡ��ѯ�۵�
    int iPos = 0;
    for (int i = 0; i < getBillListPanel().getHeadBillModel().getRowCount(); i++) {
      if (getBillListPanel().getHeadBillModel().getRowState(i) == BillModel.SELECTED) {
        iPos = i;
        iPos = nc.ui.pu.pub.PuTool.getIndexBeforeSort(getBillListPanel(), iPos);
        aryRet.add(getInvVOs()[iPos]);
      }
    }
    allvos = (InvoiceVO[]) aryRet.toArray(new InvoiceVO[aryRet.size()]);

    // vAll.copyInto(allvos);
    // ���±�ͷ�б�.
    InvoiceVO curVO = null;
    for (int j = 0; j < allvos.length; j++) {
      curVO = allvos[j];
      if (!loadItemsForInvoiceVOs(new InvoiceVO[] {
        curVO
      })) {
        // setVoBodyToListPanle(-1);
      }
      else {
        // ���ñ����б�.
        // setVoBodyToListPanle(j);

      }
    }
    return aryRet;
  }

  /**
   * �˴����뷽��˵���� �������ڣ�(2001-9-14 12:03:06)
   * 
   * @return int[]
   */
  private int getSelectedRowCount() {
    return m_nSelectedRowCount;
  }

  /**
   * ����ʵ�ָ÷���������ҵ�����ı��⡣
   * 
   * @version (00-6-6 13:33:25)
   * @return java.lang.String
   */
  public String getTitle() {

    String title = nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPP40040401-000160")/*
                                                                                                 * @res
                                                                                                 * "ά����Ʊ"
                                                                                                 */;

    // �������ȱʡ��ϵͳģ���򣬷����û�����ı���
    if (m_billPanel != null && !nc.ui.pub.ClientEnvironment.getInstance().getCorporation().getPk_corp().equals("@@@@")) {

      title = m_billPanel.getTitle();
    }
    return title;
  }

  /**
   * @param ����˵��
   * @return ����ֵ
   * @exception �쳣����
   * @see ��Ҫ�μ�����������
   * @since �������һ���汾���˷�������ӽ���������ѡ��
   */
  private UFDate getToday() {
    return nc.ui.pub.ClientEnvironment.getInstance().getDate();
  }

  /**
   * ���ߣ���ӡ�� ���ܣ��÷�����ʵ�ֽӿ�ICheckRetVO�ķ��� �ýӿ�Ϊ��������ƣ���ʵ���������˵������ʱ���������ŷ�Ʊ
   * �벻Ҫ����ɾ�����޸ĸ÷������Ա������ �������� ���أ�nc.vo.pub.AggregatedValueObject Ϊ��ƱVO ���⣺��
   * ���ڣ�(2001-5-13 11:39:21) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
   */
  public nc.vo.pub.AggregatedValueObject getVo() throws Exception {

    showHintMessage(getHeadHintText() + nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPP40040401-000013")/*
                                                                                                                       * @res
                                                                                                                       * "���ڲ�ѯ"
                                                                                                                       */
        + ".......");

    InvoiceVO[] VOs = null;
    try {
      VOs = InvoiceHelper.queryVoByHid(getCauditid());
      // added by czp on 2003-03-01 begin
      if (VOs != null && VOs.length > 0) {
        loadItemsForInvoiceVOs(new InvoiceVO[] {
          VOs[0]
        });
      }
      // added by czp on 2003-03-01 end

    }
    catch (java.lang.Exception e) {
      SCMEnv.out(e);
      throw e;
    }

    // û�з��������ķ�Ʊ
    if (VOs == null) {
      return null;
    }

    return VOs[0];

  }

  /**
   * ���ߣ���ӡ�� ���ܣ��÷�����ʵ�ֽӿ�ICheckRetVO�ķ��� �ýӿ�Ϊ��������ƣ���ʵ���������˵������ʱ���������ŷ�Ʊ
   * �벻Ҫ����ɾ�����޸ĸ÷������Ա������ �������� ���أ�nc.vo.pub.AggregatedValueObject Ϊ��ƱVO ���⣺��
   * ���ڣ�(2001-5-13 11:39:21) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
   */
  public nc.vo.pub.AggregatedValueObject getLinkQueryVo(String pk_corp) throws Exception {

    showHintMessage(getHeadHintText() + nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPP40040401-000013")/*
                                                                                                                       * @res
                                                                                                                       * "���ڲ�ѯ"
                                                                                                                       */
        + ".......");

    NormalCondVO[] normalVOs = new NormalCondVO[2];
    // normalVOs[0] = new
    // NormalCondVO(nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UC000-0000791")/*@res
    // "����ID"*/, getCauditid());
    normalVOs[0] = new NormalCondVO("����ID", getCauditid());
    normalVOs[1] = new NormalCondVO("��˾", pk_corp);

    InvoiceVO[] VOs = null;
    try {
      VOs = InvoiceHelper.queryInvoiceVOsByCondsMy(getClientEnvironment().getUser().getPrimaryKey(), normalVOs, null);
      // added by czp on 2003-03-01 begin
      if (VOs != null && VOs.length > 0) {
        loadItemsForInvoiceVOs(new InvoiceVO[] {
          VOs[0]
        });
      }
      // added by czp on 2003-03-01 end

    }
    catch (java.lang.Exception e) {
      SCMEnv.out(e);
      throw e;
    }

    // û�з��������ķ�Ʊ
    if (VOs == null) {
      return null;
    }

    return VOs[0];

  }

  /**
   * ÿ�������׳��쳣ʱ������
   * 
   * @param exception
   *          java.lang.Throwable
   */
  public void handleException(java.lang.Throwable exception) {

    /* ��ȥ���и��е�ע�ͣ��Խ�δ��׽�����쳣��ӡ�� stdout�� */
    SCMEnv.out("--------- δ��׽�����쳣 ---------");
    SCMEnv.out(exception);
  }

  /**
   * ���ߣ���ӡ�� ���ܣ���Ʊ��Ƭ����ļ�����ʼ�� �������� ���أ��� ���⣺�� ���ڣ�(2001-5-13 11:39:21)
   * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־�� 2002-09-18 wyf ����Բֿⰴť�ļ���
   */
  private void initBillListener() {
    // ���ӵ��ݱ༭����
    getBillCardPanel().addEditListener(this);
    getBillCardPanel().addBodyEditListener2(this);
    // ��Ŀ���ռӼ���
    ((UIRefPane) getBillCardPanel().getBodyItem("cprojectphasename").getComponent()).getUIButton().addActionListener(
        this);
    // ������Ӽ���
    ((UIRefPane) getBillCardPanel().getBodyItem("vfree0").getComponent()).getUITextField().addActionListener(this);
    // ((UIRefPane)getInvBillPanel().getBodyItem("vfree0").getComponent()).getUIButton().addActionListener(this)
    // ;
    // ҵ��Ա����
    ((UIRefPane) getBillCardPanel().getHeadItem("cemployeeid").getComponent()).getUIButton().addActionListener(this);
    // ����Բ˵���ļ���
    getBillCardPanel().addBodyMenuListener(this);
    // wyf 2002-09-18 add begin
    // ҵ��Ա����
    ((UIRefPane) getBillCardPanel().getBodyItem("cwarehousename").getComponent()).getUIButton().addActionListener(this);
    // wyf 2002-09-18 add end

    getBillCardPanel().addActionListener(this);
  }

  /**
   * ��ť��ʼ��
   * 
   * @param ����˵��
   * @return ����ֵ
   * @exception �쳣����
   * @see ��Ҫ�μ�����������
   * @since �������һ���汾���˷�������ӽ���������ѡ��
   */
  private void initButtons() {

    // getBtnBrowsed();
    // getBtnMaintain();
    // ҵ�����ͳ�ʼ��
    PfUtilClient.retBusinessBtn(m_btnInvBillBusiType, ClientEnvironment.getInstance().getCorporation().getPk_corp(),
        nc.vo.scm.pu.BillTypeConst.PO_INVOICE);

    if (m_btnInvBillBusiType.getChildButtonGroup().length == 0) {
      // û��ҵ������
      m_btnInvBillBusiType.setVisible(false);
    }
    else {
      m_btnInvBillBusiType.setVisible(true);
    }
    m_btnBillMaintain.setEnabled(true);
    m_btnBillBrowsed.setEnabled(true);
    m_btnInvBillConversion.setEnabled(false);

    m_btnInvBillNew.setEnabled(true);
    ButtonObject btns[] = m_btnInvBillBusiType.getChildButtonGroup();
    if (btns != null && btns.length > 0 && btns[0] != null) {
      setCurBizeType(btns[0].getTag());
      m_bizButton = btns[0];
    }
    PuTool.initBusiAddBtns(m_btnInvBillBusiType, m_btnInvBillNew, ScmConst.PO_Invoice, getPk_corp());
    // ֧�ֲ�ҵ��������չ
    // if(getExtendBtns() != null){
    // m_BillBtnGroup = (ButtonObject[])
    // PuTool.appendArrayToArray(m_BillBtnGroup,getExtendBtns(),ButtonObject.class);
    // }

    setButtonsList();
//    setButtons(getBtnss());
    setButtons(m_btnTree.getButtonArray());
  }

  /**
   * ��Ʊ��Ƭ����ĳ�ʼ��
   * 
   * @param ����˵��
   * @return ����ֵ
   * @exception �쳣����
   * @see ��Ҫ�μ�����������
   * @since �������һ���汾���˷�������ӽ���������ѡ��
   * @deprecated �÷����������һ���汾���Ѿ������������滻������ѡ��
   */
  private void initCard() {

    setLayout(new java.awt.BorderLayout());
    add(getBillCardPanel(), java.awt.BorderLayout.CENTER);
    m_btnInvShift.setName(NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000464")/** @res "�б���ʾ" */
    );

  }

  /**
   * @���ܣ���ȡ��˾ID
   */
  private String getCorpId() {
    String corpid = null;
    corpid = nc.ui.pub.ClientEnvironment.getInstance().getCorporation().getPrimaryKey();
    if (corpid == null || corpid.trim().equals("")) {
      corpid = getCorpPrimaryKey();
    }
    return corpid;
  }

  /**
   * �����Ĺ��ܡ���;�������Եĸ��ģ��Լ�����ִ��ǰ������״̬��
   * 
   * @param ����˵��
   * @return ����ֵ
   * @exception �쳣����
   * @see ��Ҫ�μ�����������
   * @since �������һ���汾���˷�������ӽ���������ѡ��
   * @deprecated �÷����������һ���汾���Ѿ������������滻������ѡ��
   */
  private void initi() {
    setCurPanelMode(INV_PANEL_CARD);
    // V51�ع���Ҫ��ƥ��,��ťʵ��������
    createBtnInstances();
    initButtons();
    initRefpane(getBillCardPanel().getBillData());
    initCard();
    initState();
    initBillListener();

    getPoPubSetUi2();

    BillRowNo.loadRowNoItem(getBillCardPanel(), "crowno");

    try {
      // ��ʼ��:�Ƿ�������Ƶ���,��ⵥ��Ʊ���㷽ʽ,������Ʊ���㷽ʽ
      Hashtable t = SysInitBO_Client.queryBatchParaValues(PoPublicUIClass.getLoginPk_corp(), new String[] {
          "PO060", "PO30", "PO46", "PO52", "PO84"
      });

      if (t != null && t.size() > 0) {
        Object oTemp = t.get("PO060");
        isAllowedModifyByOther = (oTemp == null || oTemp.equals("N")) ? false : true;
        oTemp = t.get("PO30");
        m_sStock2InvoiceSettleMode = (oTemp == null) ? null : oTemp.toString();
        oTemp = t.get("PO46");
        m_sOrder2InvoiceSettleMode = (oTemp == null) ? null : oTemp.toString();
        oTemp = t.get("PO52");
        m_sZGYF = (oTemp == null) ? null : oTemp.toString();
        oTemp = t.get("PO84");
        iChange = "�����ۿ�".equalsIgnoreCase((oTemp == null) ? null : oTemp.toString()) ? 7 : 8;

      }
      getBillCardPanel().setBodyMenuShow(true);
    }
    catch (Exception e) {
      SCMEnv.out(e);
    }
    setMaxMnyDigit(iMaxMnyDigit);
    getBillCardPanel().setBillData(getBillCardPanel().getBillData());
    //since v55,ѡ��ģʽ����
    PuTool.setLineSelected(getBillCardPanel());
    PuTool.setLineSelectedList(getBillListPanel());

  }

  /**
   * ��Ʊ�б����ĳ�ʼ��
   * 
   * @param ����˵��
   * @return ����ֵ
   * @exception �쳣����
   * @see ��Ҫ�μ�����������
   * @since �������һ���汾���˷�������ӽ���������ѡ��
   * @deprecated �÷����������һ���汾���Ѿ������������滻������ѡ��
   */
  private void initList() {
    setLayout(new java.awt.BorderLayout());
    add(getBillListPanel(), java.awt.BorderLayout.CENTER);
    setMaxMnyDigitList(iMaxMnyDigit);
  }

  /**
   * ���ߣ���ά�� ���ܣ� ������ ���أ� ���⣺ ���ڣ�(2004-9-8 10:18:55) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
   */
  private void initRefpane(BillData bd) {
    UIRefPane refpane = null;
    String pk_corp = getClientEnvironment().getCorporation().getPrimaryKey();

    // �ɹ�����(�ɹ���ɹ����۵Ĳ���)
    refpane = (UIRefPane) bd.getHeadItem("cdeptid").getComponent();
    refpane.getRefModel().setWherePart(
        " (bd_deptdoc.deptattr = '2' or bd_deptdoc.deptattr = '4') and bd_deptdoc.pk_corp = '" + pk_corp + "' ");
    refpane.setCacheEnabled(false);
    refpane.getRefModel().setPk_corp(pk_corp);

    // ҵ��Ա(�ɹ����ŵ�)
    UIRefPane refBiz = (UIRefPane) bd.getHeadItem("cemployeeid").getComponent();
    // refpane.getRefModel().addWherePart(" and (bd_psndoc.pk_deptdoc in (select
    // bd_deptdoc.pk_deptdoc from bd_deptdoc where (bd_deptdoc.deptattr = 2 or
    // bd_deptdoc.deptattr = 4) and bd_deptdoc.pk_corp ='"+pk_corp+"'))");
    refBiz
        .getRefModel()
        .addWherePart(
            " and (bd_psndoc.pk_deptdoc in (select bd_deptdoc.pk_deptdoc from bd_deptdoc where (bd_deptdoc.deptattr = '2' or bd_deptdoc.deptattr = '4') and bd_deptdoc.pk_corp ='"
                + pk_corp + "'))");
    String s = " bd_psndoc.indocflag='Y' and (bd_psndoc.pk_deptdoc in (select bd_deptdoc.pk_deptdoc from bd_deptdoc where (bd_deptdoc.deptattr = '2' or bd_deptdoc.deptattr = '4') and bd_deptdoc.pk_corp ='"
        + pk_corp + "')) ";
    refBiz.getRefModel().setWherePart(s);
    refBiz.getRefModel().setPk_corp(pk_corp);

    // ����������
    UIRefPane refpanel = null;
    if (bd.getBodyItem("nassistnum") != null) {
      refpanel = (UIRefPane) bd.getBodyItem("nassistnum").getComponent();
      UITextField nAssistNumUI = refpanel.getUITextField();
      nAssistNumUI.setDelStr("-");
    }

    // ����������
    if (bd.getBodyItem("cassistunitname") != null) {
      refpanel = ((UIRefPane) (bd.getBodyItem("cassistunitname").getComponent()));
      refpanel.setIsCustomDefined(true);
      refpanel.setRefModel(new OtherRefModel("��������λ"));
      refpanel.setReturnCode(false);
      refpanel.setRefInputType(1);
      refpanel.setCacheEnabled(false);
    }

    // ������
    if (bd.getBodyItem("nexchangerate") != null) {
      refpanel = (UIRefPane) bd.getBodyItem("nexchangerate").getComponent();
      UITextField nExchangeRateUI = refpanel.getUITextField();
      nExchangeRateUI.setDelStr("-");
    }

  }

  /**
   * ��Ʊ�б����ĳ�ʼ��
   * 
   * @param ����˵��
   * @return ����ֵ
   * @exception �쳣����
   * @see ��Ҫ�μ�����������
   * @since �������һ���汾���˷�������ӽ���������ѡ��
   * @deprecated �÷����������һ���汾���Ѿ������������滻������ѡ��
   */
  private void initListListener() {
    // ��ѡ����
    m_listPanel.getHeadTable().setCellSelectionEnabled(false);
    m_listPanel.getHeadTable().setSelectionMode(javax.swing.ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
    m_listPanel.getHeadTable().getSelectionModel().addListSelectionListener(this);

    // ���еȼ���
    m_listPanel.addEditListener(this);
    m_listPanel.addMouseListener(this);
  }

  /**
   * һЩ��ʼ��״̬������
   * 
   * @param
   * @return
   * @exception �쳣����
   * @see ��Ҫ�μ�����������
   * @since �������һ���汾���˷�������ӽ���������ѡ��
   */
  private void initState() {
    setCurPanelMode(INV_PANEL_CARD);
    setCurOperState(STATE_INIT);
    setListOperState(STATE_LIST_NORMAL);
    setBillBrowseState(STATE_BROWSE_NORMAL);
    setButtonsAndPanelState();
  }

  /**
   * @param ����˵��
   * @return ����ֵ
   * @exception �쳣����
   * @see ��Ҫ�μ�����������
   * @since �������һ���汾���˷�������ӽ���������ѡ��
   */
  private boolean isCouldMaked() {
    return m_bCouldMaked;
  }

  /**
   * ����ԭ�Ҽ��㱾��
   */
  private boolean isCouldMakedForABizType(String strBizType) {
    return true;

    // ����ҵ�������Ƿ������Ƶ���
    // BillbusinessVO[] billSourceVOs = null;
    // String beanName=IPFConfig.class.getName();
    // IPFConfig bo =
    // (IPFConfig)nc.bs.framework.common.NCLocator.getInstance().lookup(beanName);
    // try {
    // billSourceVOs =
    // bo.querybillSource(nc.ui.pub.ClientEnvironment.getInstance().getCorporation().getPrimaryKey(),
    // nc.vo.scm.pu.BillTypeConst.PO_INVOICE, strBizType);
    // } catch (Exception e) {
    // SCMEnv.out(e);
    // return false;
    // }
    //
    // if (billSourceVOs != null) {
    // //����ҵ�������Ƿ������Ƶ���
    // for (int i = 0; i < billSourceVOs.length; i++) {
    // //�����Ƶ��ݣ��������
    // if (billSourceVOs[i].getPk_billtype().toUpperCase().equals("MAKEFLAG")) {
    // return true;
    // }
    // }
    // }
    //
    // return false;
  }

  /**
   * @param ����˵��
   * @return ����ֵ
   * @exception �쳣����
   * @see ��Ҫ�μ�����������
   * @since �������һ���汾���˷�������ӽ���������ѡ��
   */
  private boolean isEverQueryed() {

    return m_bEverQueryed;
  }

  /**
   * @���ܣ����ر�ͷ�������� �������ڣ�(2003-10-15 11:08:23)
   */
  private void loadBDData() {
    /* �������� */
    String strFormula = null, strVarValue = null, strValue = null;
    UIRefPane refpnl = null;
    InvoiceVO vo = getInvVOs()[getCurVOPos()];
    if (vo == null || vo.getParentVO() == null)
      return;

    /* ҵ������ */
    strVarValue = (String) vo.getParentVO().getAttributeValue("cbiztype");
    refpnl = (UIRefPane) getBillCardPanel().getHeadItem("cbiztype").getComponent();
    strValue = refpnl.getUITextField().getText();
    if (strVarValue != null && (strValue == null || strValue.trim().equals(""))) {
      strFormula = "cbiztypename->getColValue(bd_busitype,businame,pk_busitype,cbiztype)";
      Object ob = getBillCardPanel().execHeadFormula(strFormula);
      refpnl.getUITextField().setText((String) ob);
    }
    /* �����֯ */
    strVarValue = (String) vo.getParentVO().getAttributeValue("cstoreorganization");
    refpnl = (UIRefPane) getBillCardPanel().getHeadItem("cstoreorganization").getComponent();
    strValue = refpnl.getUITextField().getText();
    if (strVarValue != null && (strValue == null || strValue.trim().equals(""))) {
      strFormula = "cstoreorganizationname->getColValue(bd_calbody,bodyname,pk_calbody,cstoreorganization)";
      Object ob = getBillCardPanel().execHeadFormula(strFormula);
      refpnl.getUITextField().setText((String) ob);
    }
    /* ��Ӧ�� */
    strVarValue = (String) vo.getParentVO().getAttributeValue("cvendorbaseid");
    refpnl = (UIRefPane) getBillCardPanel().getHeadItem("cvendormangid").getComponent();
    strValue = refpnl.getUITextField().getText();
    if (strVarValue != null && (strValue == null || strValue.trim().equals(""))) {
      strFormula = "vendor->getColValue(bd_cubasdoc,custname,pk_cubasdoc,cvendorbaseid)";
      Object ob = getBillCardPanel().execHeadFormula(strFormula);
      refpnl.getUITextField().setText((String) ob);
    }
    /* ҵ��Ա */
    // strVarValue = (String) vo.getParentVO().getAttributeValue("cemployeeid");
    // refpnl = (UIRefPane)
    // getInvBillPanel().getHeadItem("cemployeeid").getComponent();
    // strValue = refpnl.getUITextField().getText();
    // if (strVarValue != null && (strValue == null ||
    // strValue.trim().equals(""))) {
    // strFormula =
    // "cemployee->getColValue(bd_psndoc,psnname,pk_psndoc,cemployeeid)";
    // Object obb = getInvBillPanel().execHeadFormula(strFormula);
    // refpnl.getUITextField().setText((String) obb);
    // }
    /* ���� */
    // strVarValue = (String) vo.getParentVO().getAttributeValue("cdeptid");
    // refpnl = (UIRefPane)
    // getInvBillPanel().getHeadItem("cdeptid").getComponent();
    // strValue = refpnl.getUITextField().getText();
    // if (strVarValue != null && (strValue == null ||
    // strValue.trim().equals(""))) {
    // strFormula =
    // "cdeptname->getColValue(bd_deptdoc,deptname,pk_deptdoc,cdeptid)";
    // Object ob = getInvBillPanel().execHeadFormula(strFormula);
    // refpnl.getUITextField().setText((String) ob);
    // }
    /* ɢ�� */
    strVarValue = (String) vo.getParentVO().getAttributeValue("cfreecustid");
    refpnl = (UIRefPane) getBillCardPanel().getHeadItem("cfreecustid").getComponent();
    strValue = refpnl.getUITextField().getText();
    if (strVarValue != null && (strValue == null || strValue.trim().equals(""))) {
      strFormula = "cfreecustname->getColValue(so_freecust,vcustname,cfreecustid,cfreecustid)";
      Object ob = getBillCardPanel().execHeadFormula(strFormula);
      refpnl.getUITextField().setText((String) ob);
    }
    /* ����Э�� */
    strVarValue = (String) vo.getParentVO().getAttributeValue("ctermprotocolid");
    refpnl = (UIRefPane) getBillCardPanel().getHeadItem("ctermprotocolid").getComponent();
    strValue = refpnl.getUITextField().getText();
    if (strVarValue != null && (strValue == null || strValue.trim().equals(""))) {
      strFormula = "ctermprotocolname->getColValue(bd_payterm,termname,pk_payterm,ctermprotocolid)";
      Object ob = getBillCardPanel().execHeadFormula(strFormula);
      refpnl.getUITextField().setText((String) ob);
    }
    /* ���� */
    strVarValue = (String) vo.getParentVO().getAttributeValue("ccurrencytypeid");
    refpnl = (UIRefPane) getBillCardPanel().getHeadItem("ccurrencytypeid").getComponent();
    strValue = refpnl.getUITextField().getText();
    if (strVarValue != null && (strValue == null || strValue.trim().equals(""))) {
      strFormula = "currname->getColValue(bd_currtype,currtypename,pk_currtype,ccurrencytypeid)";
      Object ob = getBillCardPanel().execHeadFormula(strFormula);
      refpnl.getUITextField().setText((String) ob);
    }
    /* �������� */
    if (getBillCardPanel().getHeadItem("cvendoraccount") != null) {
      strVarValue = getBillCardPanel().getHeadItem("cvendoraccount").getValue();
      if (strVarValue != null && strVarValue.trim().length() == 20) {
        String cfreecustid = getBillCardPanel().getHeadItem("cfreecustid").getValue();
        if (cfreecustid != null && cfreecustid.trim().length() > 0) {
          strFormula = "cvendoraccount->getColValue(so_freecust,vaccount,cfreecustid,cfreecustid)";
        }
        else {
          UIRefPane refpane = (UIRefPane) getBillCardPanel().getHeadItem("caccountbankid").getComponent();
          String pkcorp = getBillCardPanel().getCorp();
          String vendorbaseid = (String) getBillCardPanel().getHeadItem("cvendorbaseid").getValue();
          refpane.getRefModel().addWherePart(
                  " and bd_bankaccbas.pk_bankaccbas in (select  k.pk_accbank from bd_custbank k,bd_cumandoc m   where   m.pk_corp ='"
                      + pkcorp
                      + "'  and k.pk_cubasdoc=m.pk_cubasdoc and k.pk_cubasdoc='"
                      + vendorbaseid + "')");

          strFormula = "cvendoraccount->getColValue(bd_bankaccbas,accountname,pk_bankaccbas,caccountbankid)";
        }
        Object ob = getBillCardPanel().execHeadFormula(strFormula);
        getBillCardPanel().getHeadItem("cvendoraccount").setValue((String) ob);
      }
    }
  }

  /**
   * ���ߣ����� ���ܣ���̬װ�ر���VO ������ ���أ� ���⣺ ���ڣ�(2003-02-08 13:09:22) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
   * 
   * @param inv
   *          nc.vo.pi.InvoiceVO
   */
  public boolean loadItemsForInvoiceVOs(InvoiceVO[] invs) {

    // ���������Ϸ����ж�
    if (invs == null || invs.length == 0)
      return true;

    // ѡ������Ϊ�յ�InvoiceVO, ����װ��һ��������InvoiceVO
    Vector v = new Vector();
    Hashtable hash = new Hashtable();
    for (int i = 0; i < invs.length; i++) {
      if (invs == null)
        continue;
      InvoiceHeaderVO head = (InvoiceHeaderVO) invs[i].getHeadVO();
      InvoiceItemVO[] items = (InvoiceItemVO[]) invs[i].getChildrenVO();
      if (head != null && head.getPrimaryKey() != null && (items == null || items.length == 0)) {
        InvoiceHeaderVO lightHead = new InvoiceHeaderVO();

        // ����������VO:ֻ��������ʱ���
        lightHead.setPrimaryKey(head.getPrimaryKey());
        lightHead.setTs(head.getTs());

        InvoiceVO lightVO = new InvoiceVO();
        lightVO.setParentVO(lightHead);
        v.add(lightVO);
        hash.put(head.getPrimaryKey(), invs[i]);
      }
    }

    // ����Ҫ����,����True
    if (v.size() == 0)
      return true;

    // ��Vector����ȡ������VO����
    InvoiceVO[] lightVOs = null;
    lightVOs = new InvoiceVO[v.size()];
    v.copyInto(lightVOs);

    // �Ӻ�̨ˢ��lightVOs�ı���
    try {
      NormalCondVO[] normalVOs = getQueDlg().getNormalCondVOs();
      ConditionVO[] definedVOs = getQueDlg().getConditionVO();
      lightVOs = InvoiceHelper.queryItemsForInvoices(normalVOs, definedVOs, lightVOs);

    }
    catch (BusinessException e) {
      SCMEnv.out(e);
      MessageDialog.showHintDlg(this,
          nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPPSCMCommon-000270")/*
                                                                                         * @res
                                                                                         * "��ʾ"
                                                                                         */, e.getMessage());
      return false;
    }
    catch (Exception e2) {
      SCMEnv.out(e2);
      return false;
    }
    if (lightVOs != null && lightVOs.length > 0) {
      // ���ñ���
      for (int i = 0; i < lightVOs.length; i++) {
        InvoiceVO vo = (InvoiceVO) hash.get(lightVOs[i].getHeadVO().getPrimaryKey());
        vo.setChildrenVO(lightVOs[i].getChildrenVO());
      }
    }
    return true;
  }

  public void mouse_doubleclick(nc.ui.pub.bill.BillMouseEnent e) {
    onListBill();

    setButtonsAndPanelState();

    updateButtons();
    /**
     * ���ο������֧�� by zhaoyha at 2009.2.16
     */
    getInvokeEventProxy().mouse_doubleclick(e);
  }

  /**
   * ���ܣ��򵥾ݿ�Ƭ׷�ӵ����壨������
   */
  public void onActionAppendLine() {
    getBillCardPanel().addLine();
    // �����к�
    nc.ui.scm.pub.report.BillRowNo.addLineRowNo(getBillCardPanel(), nc.vo.scm.pu.BillTypeConst.PO_INVOICE, "crowno");
    int iNewLine = getBillCardPanel().getBillModel().getRowCount() - 1;
    // ���������е�Ĭ��ֵ
    setNewLineDefaultValue(iNewLine);
    setDefaultBody(iNewLine);
  }

  /**
   * ���ܣ��򵥾ݿ�Ƭ���뵥���壨������
   */
  public void onActionInsertLines(int iBeginRow, int iEndRow, int iInsertCount) {

    if (getBillCardPanel().getBillTable().getSelectedRowCount() <= 0) {
      showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPPSCMCommon-000354")/*
                                                                                                     * @res
                                                                                                     * "����ǰ����ѡ������У�"
                                                                                                     */);
      return;
    }
    int iCurRow = 0;
    for (int i = 0; i < iInsertCount; i++) {
      iCurRow = iBeginRow + i;
      getBillCardPanel().getBillModel().insertRow(iCurRow + 1);
      // �����²��е�Ĭ��ֵ
      // if (getInvBillPanel().getBillTable().getSelectedRows().length > 0) {
      setDefaultBody(iCurRow + 1);
      setNewLineDefaultValue(iCurRow + 1);
      // }
    }
    int iFinalEndRow = iBeginRow + iInsertCount + 1;

    // �����к�
    BillRowNo.insertLineRowNos(getBillCardPanel(), nc.vo.scm.pu.BillTypeConst.PO_INVOICE, "crowno", iFinalEndRow,
        iInsertCount);

  }

  /**
   * ��������:�����˵��Ҽ�����Ȩ�޿���
   * 
   * @deprecated ��Ϊʹ�� {@link #setPopMenuBtnsEnable()}
   */
  private void rightButtonRightControl() {
    setPopMenuBtnsEnable();
  }

  /**
   * ��������:�����˵��Ҽ�����Ȩ�޿���
   */
  private void setPopMenuBtnsEnable() {
    // û�з����в���Ȩ��
    if (m_btnInvBillRowOperation == null || m_btnInvBillRowOperation.getChildCount() == 0) {
      getBillCardPanel().getBodyPanel().getMiAddLine().setEnabled(false);
      getBillCardPanel().getBodyPanel().getMiCopyLine().setEnabled(false);
      getBillCardPanel().getBodyPanel().getMiDelLine().setEnabled(false);
      getBillCardPanel().getBodyPanel().getMiInsertLine().setEnabled(false);
      getBillCardPanel().getBodyPanel().getMiPasteLine().setEnabled(false);
      getBillCardPanel().getBodyPanel().getMiPasteLineToTail().setEnabled(false);
      if (m_miReSortRowNo != null) {
        m_miReSortRowNo.setEnabled(false);
      }
      if (m_miCardEdit != null) {
        m_miCardEdit.setEnabled(false);
      }
    }
    // �����в���Ȩ��
    else {
      getBillCardPanel().getBodyPanel().getMiAddLine().setEnabled(isPowerBtn(m_btnInvBillAddRow.getCode()));
      getBillCardPanel().getBodyPanel().getMiCopyLine().setEnabled(isPowerBtn(m_btnInvBillCopyRow.getCode()));
      getBillCardPanel().getBodyPanel().getMiDelLine().setEnabled(isPowerBtn(m_btnInvBillDeleteRow.getCode()));
      getBillCardPanel().getBodyPanel().getMiInsertLine().setEnabled(isPowerBtn(m_btnInvBillInsertRow.getCode()));
      getBillCardPanel().getBodyPanel().getMiPasteLine().setEnabled(isPowerBtn(m_btnInvBillPasteRow.getCode()));
      // ճ������β��ճ���������߼���ͬ
      getBillCardPanel().getBodyPanel().getMiPasteLineToTail().setEnabled(
          isPowerBtn(m_btnInvBillPasteRowTail.getCode()));
      if (m_miReSortRowNo != null) {
        m_miReSortRowNo.setEnabled(isPowerBtn(m_btnReSortRowNo.getCode()));
      }
      if (m_miCardEdit != null) {
        m_miCardEdit.setEnabled(isPowerBtn(m_btnCardEdit.getCode()));
      }
    }
  }

  /**
   * �ֹ�����һ���·�Ʊ
   * 
   * @param ����˵��
   * @return ����ֵ
   * @exception �쳣����
   * @see ��Ҫ�μ�����������
   * @since �������һ���汾���˷�������ӽ���������ѡ��
   */
  private void onAdd() {

    setInvoiceTypeComItem();// �������ơ����⡱��Ʊ

    // InvoiceVO vo = new InvoiceVO();
    m_bAdd = true;
    // //////////////////////////////////////////
    getBillCardPanel().addNew();
    getBillCardPanel().setEnabled(true);

    setCurOperState(STATE_EDIT);
    setButtonsAndPanelState();

    updateButtons();
    // �����˵��Ҽ�����Ȩ�޿���
    setPopMenuBtnsEnable();
    // ����VO
    // setVOToBillPanel() ;

    // ////////////////////////////////////////////////////////

    // V5 Del : setImageType(IMAGE_NULL);

    // ҵ������
    UIRefPane busiRef = (UIRefPane) getBillCardPanel().getHeadItem("cbiztype").getComponent();
    busiRef.setValue(m_bizButton.getName());
    busiRef.setPK(m_bizButton.getTag());
    setCurBizeType(m_bizButton.getTag());
    // ����״̬
    getBillCardPanel().getHeadItem("ibillstatus").setValue(new Integer(0));
    // ��Ʊ����,��Ʊ����,��Ʊ���ͣ���˰���
    getBillCardPanel().getHeadItem("dinvoicedate").setValue(getToday());
    getBillCardPanel().getHeadItem("darrivedate").setValue(getToday());
    getBillCardPanel().getHeadItem("iinvoicetype").setValue(new Integer(0));
    getBillCardPanel().getHeadItem("idiscounttaxtype").setValue(new Integer(1));
    // �Ƶ���
    ((UIRefPane) getBillCardPanel().getTailItem("coperator").getComponent()).setPK(nc.ui.pub.ClientEnvironment
        .getInstance().getUser().getPrimaryKey());
    ;

    try {
      // ����
      String sLocalCurrId = CurrParamQuery.getInstance().getLocalCurrPK(getPk_corp());
      getBillCardPanel().getHeadItem("ccurrencytypeid").setValue(sLocalCurrId);
      // ����
      // setCurrRateToBillHead(sLocalCurrId, null, null);

      afterEditWhenHeadCurrency(null);
    }
    catch (Exception e) {
      SCMEnv.out(e);
    }

    // �Ƿ������Ƶ���
    setCouldMaked(true);

    try {
      // ���˴������,��֤��ҵ�����������д���ҵ������ʱ�滻�����������д�������
      setInventoryRefFilter(getCurBizeType());

      // since v51, ����ҵ��ԱĬ��ֵ ���ݲ���Ա����ҵ��Ա
      setDefaultValueByUser();

    }
    catch (Exception e) {
      SCMEnv.out(e);
    }

    // �������޸�
    onAppendLine();

    SCMEnv.out("m_btnInvBillDeleteRow==" + m_btnInvBillDeleteRow.isPower());
  }

  /**
   * �����������������ô�����չ�������,����ҵ������ֻ��ʾ���д��������ȫ�������
   * <p>
   * <b>examples:</b>
   * <p>
   * ʹ��ʾ��
   * <p>
   * <b>����˵��</b>
   * @param cBizType
   * @throws BusinessException
   * <p>
   * @author zhaoyha
   * @time 2009-4-22 ����03:12:09
   */
  private void setInventoryRefFilter(String cBizType) throws BusinessException {
    boolean checker = false;
    if (!cBizTypeTable.contains(cBizType)) {
      Object oTemp = CacheTool.getCellValue("bd_busitype", "pk_busitype", "verifyrule", cBizType);
      if (oTemp != null) {
        Object o[] = (Object[]) oTemp;
        if (o != null && o.length > 0 && o[0] != null && o[0].equals("S")){
          checker = true;
          cBizTypeTable.add(cBizType);
        }
      }
    }
    else 
      checker = true;
    
    // ���˴������
    String sql = " and ( 1 =1 )";
    if (checker) 
       sql = " and (sellproxyflag = 'Y')";
    
    UIRefPane refCinventorycode = (UIRefPane) (getBillCardPanel().getBodyItem("invcode").getComponent());
    refCinventorycode.getRefModel().addWherePart(sql);
  }

  /**
   * ���ߣ���ӡ�� ���ܣ�����һ����Ʊ�� �������� ���أ��� ���⣺�� ���ڣ�(2001-5-13 11:39:21)
   * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־�� 2002-09-26 wyf ������һ�������һ�л�����ͬ�� ����ȡԭ�з���Ϊһ���÷�������Ӧ�����޸�
   */
  private void onAppendLine() {

    getBillCardPanel().addLine();
    int iNewRow = getBillCardPanel().getRowCount() - 1;
    setNewLineDefaultValueForAddLine(iNewRow);
    BillRowNo.addLineRowNo(getBillCardPanel(), ScmConst.PO_Invoice, "crowno");
    SCMEnv.out("m_btnInvBillDeleteRow==" + m_btnInvBillDeleteRow.isPower());
    // �����˵��Ҽ�����Ȩ�޿���
    setPopMenuBtnsEnable();
  }

  /**
   * �˴����뷽��˵���� ���ܣ���Ϣ����ר�� ������ ���أ� ���⣺ ���ڣ�(2002-10-15 16:15:35)
   * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
   */
  public void onUnAudit() {
    InvoiceVO[] proceVOs = new InvoiceVO[] {
      getInvVOs()[getCurVOPos()]
    };
    // ���������˼��������ڹ�ϣ������ʧ��ʱ�õ�
    HashMap<String, ArrayList<Object>> mapAuditInfo = new HashMap<String, ArrayList<Object>>();
    ArrayList<Object> listAuditInfo = null;

    try {
      // ���������
      for (int i = 0; i < proceVOs.length; i++) {
        // ����ʧ��ʱ�õ������������˼��������ڹ�ϣ��
        if (PuPubVO.getString_TrimZeroLenAsNull(proceVOs[i].getHeadVO().getCauditpsn()) != null) {
          listAuditInfo = new ArrayList<Object>();
          listAuditInfo.add(proceVOs[i].getHeadVO().getCauditpsn());
          listAuditInfo.add(proceVOs[i].getHeadVO().getDauditdate());
          mapAuditInfo.put(proceVOs[i].getHeadVO().getPrimaryKey(), listAuditInfo);
        }
        // ������Ա��ԭ������ID��Ϊ�ж��Ƿ������������˵ĵ���
        proceVOs[i].getHeadVO().setCoperatoridnow(getClientEnvironment().getUser().getPrimaryKey());
        proceVOs[i].getHeadVO().setAttributeValue("cauditpsnold", proceVOs[i].getHeadVO().getCauditpsn());

        proceVOs[i].getHeadVO().setCauditpsn(nc.ui.pub.ClientEnvironment.getInstance().getUser().getPrimaryKey());
        proceVOs[i].getHeadVO().setCuserid(nc.ui.pub.ClientEnvironment.getInstance().getUser().getPrimaryKey());
      }
      if (!loadItemsForInvoiceVOs(proceVOs)) {
        showHintMessage("");
        return;
      }

      // �ѽ���ķ�Ʊ�ܷ��������:
      // IF ��Ʊ��ҵ�����ͺ������Ϊ���д���,ֱ�˲ɹ�, ����Ϊ���ⷢƱ����Ϊ���Ʒ�Ʊ, ���ܿ���
      // ���� IF ��Ʊ��Դ����ⵥ���Ҹ�����⿪Ʊ�ܷ�������Ϊ��������, ���쳣
      if (m_sZGYF == null || !(new UFBoolean(m_sZGYF).booleanValue())) {
        InvoiceVO invoiceVO = new InvoiceVO();
        for (int i = 0; i < proceVOs.length; i++) {
          invoiceVO = proceVOs[i];
          Object oTemp = CacheTool.getCellValue("bd_busitype", "pk_busitype", "verifyrule", invoiceVO.getHeadVO()
              .getCbiztype());
          if (oTemp == null)
            continue;
          Object oTemp1[] = (Object[]) oTemp;
          if (oTemp1[0].equals("S") || oTemp1[0].equals("Z"))
            continue;// ���д�����ֱ�˲ɹ�ҵ������
          if (invoiceVO.getHeadVO().getIinvoicetype().intValue() == 3)
            continue;// ���ⷢƱ
          InvoiceItemVO tempBodyVO[] = invoiceVO.getBodyVO();
          for (int j = 0; j < tempBodyVO.length; j++) {
            if (tempBodyVO[j].getCupsourcebilltype() == null)
              continue;// ���Ʋɹ���Ʊ
            if ((tempBodyVO[j].getCupsourcebilltype().equals("45") || tempBodyVO[j].getCupsourcebilltype().equals("47"))
                && m_sOrder2InvoiceSettleMode != null && m_sStock2InvoiceSettleMode.equals("����ʱ�Զ�����")) {
              if (tempBodyVO[j].getNaccumsettmny() != null && tempBodyVO[j].getNaccumsettmny().doubleValue() != 0.0) {
                MessageDialog.showHintDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401",
                    "UPPSCMCommon-000270")/* @res "��ʾ" */, nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401",
                    "UPP40040401-000022")/* @res "�÷�Ʊ�ѽ��㣬��������" */);
                showHintMessage("");
                return;
              }
            }
          }
        }
      }
      /**
       * ���ο������֧�� by zhaoyha at 2009.2.16
       */
      getInvokeEventProxy().beforeAction(nc.vo.scm.plugin.Action.UNAUDIT, proceVOs);
 
      PfUtilClient.processBatch("UNAPPROVE" + nc.ui.pub.ClientEnvironment.getInstance().getUser().getPrimaryKey(),
          nc.vo.scm.pu.BillTypeConst.PO_INVOICE, ClientEnvironment.getInstance().getDate().toString(), proceVOs);

      //
      if (PfUtilClient.isSuccess()) {
        /**
         * ���ο������֧�� by zhaoyha at 2009.2.16
         */
        getInvokeEventProxy().afterAction(nc.vo.scm.plugin.Action.UNAUDIT, proceVOs);
 
        // ҵ����־
//        for (InvoiceVO invVO : proceVOs) {
//          invVO.getOperatelogVO().setOpratorname(nc.ui.pub.ClientEnvironment.getInstance().getUser().getUserName());
//          invVO.getOperatelogVO().setCompanyname(
//              nc.ui.pub.ClientEnvironment.getInstance().getCorporation().getUnitname());
//          invVO.getOperatelogVO().setOperatorId(nc.ui.pub.ClientEnvironment.getInstance().getUser().getPrimaryKey());
//          Operlog operlog = new Operlog();
//          operlog.insertBusinessExceptionlog(invVO, "����", "����", nc.vo.scm.funcs.Businesslog.MSGMESSAGE,
//              nc.vo.scm.pu.BillTypeConst.PO_INVOICE, nc.ui.sm.cmenu.Desktop.getApplet().getParameter("USER_IP"));
//        }
        InvoiceHeaderVO headVO = getInvVOs()[getCurVOPos()].getHeadVO();
        ArrayList arrRet = InvoiceHelper.queryForSaveAudit(headVO.getPrimaryKey());
        headVO.setDauditdate((UFDate) arrRet.get(0));
        headVO.setCauditpsn((String) arrRet.get(1));
        headVO.setIbillstatus((Integer) arrRet.get(2));
        headVO.setTs((String) arrRet.get(3));
        headVO.setTaudittime((String) arrRet.get(4));
        // ���ÿ�Ƭ��������
        setVOToBillPanel();
        // ���ð�ť״̬
        m_btnAudit.setEnabled(true);
        m_btnUnAudit.setEnabled(false);
        updateButtons();
      }
      else {
        // ���������˼���������
        if (mapAuditInfo.size() > 0) {
          for (int i = 0; i < proceVOs.length; i++) {
            if (!mapAuditInfo.containsKey(proceVOs[i].getHeadVO().getPrimaryKey())) {
              proceVOs[i].getHeadVO().setCauditpsn(null);
              proceVOs[i].getHeadVO().setDauditdate(null);
              continue;
            }
            listAuditInfo = (ArrayList<Object>) mapAuditInfo.get(proceVOs[i].getHeadVO().getPrimaryKey());
            proceVOs[i].getHeadVO().setCauditpsn((String) listAuditInfo.get(0));
            proceVOs[i].getHeadVO().setDauditdate((UFDate) listAuditInfo.get(1));
          }
        }
      }
    }
    catch (Exception e) {
      String strErrMsg = e.getMessage();
      if ((e instanceof TaskInvalidateException) || (e instanceof EngineException)) {
        MessageDialog.showHintDlg(this,
            nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPPSCMCommon-000270")/*
                                                                                           * @res
                                                                                           * "��ʾ"
                                                                                           */, strErrMsg);
      }
      else if (strErrMsg != null
          && (strErrMsg
              .indexOf(nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPP40040401-000163")/*
                                                                                                     * @res
                                                                                                     * "����"
                                                                                                     */) >= 0 || strErrMsg
              .indexOf(nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPP40040401-000164")/*
                                                                                                     * @res
                                                                                                     * "����ɾ��"
                                                                                                     */) >= 0)) {
        strErrMsg = nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPP40040401-000023")/*
                                                                                                   * @res
                                                                                                   * "�ɹ���Ʊ����ʧ�ܣ����ݴ��ں���������Ѿ�������ʵʱƾ֤����������!"
                                                                                                   */;
        MessageDialog.showHintDlg(this,
            nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPPSCMCommon-000270")/*
                                                                                           * @res
                                                                                           * "��ʾ"
                                                                                           */, strErrMsg);
      }
      else if (e instanceof BusinessException) {
        MessageDialog.showHintDlg(this,
            nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPPSCMCommon-000270")/*
                                                                                           * @res
                                                                                           * "��ʾ"
                                                                                           */, strErrMsg);
      }
      else {
        strErrMsg = nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPP40040401-000024")/*
                                                                                                   * @res
                                                                                                   * "����ʧ��!���������л���ԭ���磺\n1)������δ��ȷ����\n2)��NC�������ն�\n��ȷ�ϻ�����ȷ���ٴβ���!"
                                                                                                   */;
        SCMEnv.out("!!! ������Ϣ�ɷ���ϵͳ����Ա�ο���\n");
        SCMEnv.out(e);
        MessageDialog.showHintDlg(this,
            nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPPSCMCommon-000270")/*
                                                                                           * @res
                                                                                           * "��ʾ"
                                                                                           */, strErrMsg);
      }
      showHintMessage("");
      return;
    }
    finally {
      // ���������˼���������
      if (mapAuditInfo.size() > 0) {
        for (int i = 0; i < proceVOs.length; i++) {
          if (!mapAuditInfo.containsKey(proceVOs[i].getHeadVO().getPrimaryKey())) {
            proceVOs[i].getHeadVO().setCauditpsn(null);
            proceVOs[i].getHeadVO().setDauditdate(null);
            continue;
          }
          listAuditInfo = (ArrayList<Object>) mapAuditInfo.get(proceVOs[i].getHeadVO().getPrimaryKey());
          proceVOs[i].getHeadVO().setCauditpsn((String) listAuditInfo.get(0));
          proceVOs[i].getHeadVO().setDauditdate((UFDate) listAuditInfo.get(1));
        }
      }
    }

    showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UCH011"));
  }

  /**
   * �˴����뷽��˵���� ���ܣ���Ϣ����ר�� ������ ���أ� ���⣺ ���ڣ�(2002-10-15 16:15:35)
   * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
   */
  public void onAudit() {

    InvoiceVO[] proceVOs = new InvoiceVO[] {
      getInvVOs()[getCurVOPos()]
    };

    // ������������������
    String strOperPk = nc.ui.pub.ClientEnvironment.getInstance().getUser().getPrimaryKey();
    UFDate today = nc.ui.pub.ClientEnvironment.getInstance().getDate();

    // ���������˼��������ڹ�ϣ������ʧ��ʱ�õ�
    HashMap<String, ArrayList<Object>> mapAuditInfo = new HashMap<String, ArrayList<Object>>();
    ArrayList<Object> listAuditInfo = null;

    for (int i = 0; i < proceVOs.length; i++) {
      // ����ʧ��ʱ�õ������������˼��������ڹ�ϣ��
      if (PuPubVO.getString_TrimZeroLenAsNull(proceVOs[i].getHeadVO().getCauditpsn()) != null) {
        listAuditInfo = new ArrayList<Object>();
        listAuditInfo.add(proceVOs[i].getHeadVO().getCauditpsn());
        listAuditInfo.add(proceVOs[i].getHeadVO().getDauditdate());
        mapAuditInfo.put(proceVOs[i].getHeadVO().getPrimaryKey(), listAuditInfo);
      }
      proceVOs[i].getHeadVO().setCauditpsn(strOperPk);
      proceVOs[i].getHeadVO().setDauditdate(today);
      proceVOs[i].getHeadVO().setCuserid(strOperPk);
      proceVOs[i].getHeadVO().setCoperatoridnow(getClientEnvironment().getUser().getPrimaryKey());
    }
    // �������ڲ���С���Ƶ����ڼ��
    ClientLink cl = new ClientLink(nc.ui.pub.ClientEnvironment.getInstance());// ������Ϣ
    String errMsg = PuTool.getAuditLessThanMakeMsg(proceVOs, "dinvoicedate", "vinvoicecode", cl.getLogonDate(),
        ScmConst.PO_Invoice);
    if (errMsg != null) {
      MessageDialog.showErrorDlg(this,
          nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPPSCMCommon-000270")/*
           * @res
           * "��ʾ"
           */, errMsg);
      showHintMessage("");
      return;
    }
    
    try {
      /**
       * ���ο������֧�� by zhaoyha at 2009.2.16
       */
      getInvokeEventProxy().beforeAction(nc.vo.scm.plugin.Action.AUDIT, proceVOs);
      
    	  PfUtilClient.processBatchFlow(this, "APPROVE", nc.vo.scm.pu.BillTypeConst.PO_INVOICE, ClientEnvironment
          .getInstance().getDate().toString(), proceVOs, null);
      if (PfUtilClient.isSuccess()) {
        /**
         * ���ο������֧�� by zhaoyha at 2009.2.16
         */
        getInvokeEventProxy().afterAction(nc.vo.scm.plugin.Action.AUDIT, proceVOs);


        // ���÷�ƱVO����
        setInvVOs(new InvoiceVO[] {
          (InvoiceVO) getVo()
        });
        setCurVOPos(0);
        // ҵ����־
//        for (InvoiceVO invVO : proceVOs) {
//          invVO.getHeadVO().setCoperatoridnow(getClientEnvironment().getUser().getPrimaryKey());
//          invVO.getOperatelogVO().setOpratorname(nc.ui.pub.ClientEnvironment.getInstance().getUser().getUserName());
//          invVO.getOperatelogVO().setCompanyname(
//              nc.ui.pub.ClientEnvironment.getInstance().getCorporation().getUnitname());
//          invVO.getOperatelogVO().setOperatorId(nc.ui.pub.ClientEnvironment.getInstance().getUser().getPrimaryKey());
//          Operlog operlog = new Operlog();
//          operlog.insertBusinessExceptionlog(invVO, "����", "����", nc.vo.scm.funcs.Businesslog.MSGMESSAGE,
//              nc.vo.scm.pu.BillTypeConst.PO_INVOICE, nc.ui.sm.cmenu.Desktop.getApplet().getParameter("USER_IP"));
//        }
        // ���ÿ�Ƭ��������
        setVOToAuditedBillPanel();
        // ���ð�ť״̬
        m_btnAudit.setEnabled(false);
        m_btnUnAudit.setEnabled(true);
        updateButtons();
      }
      else {
        // ���������˼���������
        if (mapAuditInfo.size() > 0) {
          for (int i = 0; i < proceVOs.length; i++) {
            if (!mapAuditInfo.containsKey(proceVOs[i].getHeadVO().getPrimaryKey())) {
              proceVOs[i].getHeadVO().setCauditpsn(null);
              proceVOs[i].getHeadVO().setDauditdate(null);
              continue;
            }
            listAuditInfo = (ArrayList<Object>) mapAuditInfo.get(proceVOs[i].getHeadVO().getPrimaryKey());
            proceVOs[i].getHeadVO().setCauditpsn((String) listAuditInfo.get(0));
            proceVOs[i].getHeadVO().setDauditdate((UFDate) listAuditInfo.get(1));
          }
        }
      }
    }
    catch (Exception e) {
      SCMEnv.out(e);
      String strErrMsg = e.getMessage();
      if ((e instanceof TaskInvalidateException) || (e instanceof EngineException)) {
        MessageDialog.showHintDlg(this,
            nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPPSCMCommon-000270")/*
                                                                                           * @res
                                                                                           * "��ʾ"
                                                                                           */, strErrMsg);
      }
      else if (strErrMsg != null
          && strErrMsg
              .indexOf(nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPP40040401-000161")/*
                                                                                                     * @res
                                                                                                     * "Ӧ��ϵͳ�ѽ���"
                                                                                                     */) >= 0) {
        MessageDialog.showHintDlg(this,
            nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPPSCMCommon-000270")/*
                                                                                           * @res
                                                                                           * "��ʾ"
                                                                                           */,
            nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPP40040401-000019")/*
                                                                                           * @res
                                                                                           * "Ӧ��ϵͳ�ѽ��ʣ���Ʊ����������"
                                                                                           */);
      }
      else if (e instanceof BusinessException || e instanceof java.rmi.RemoteException) {
        MessageDialog.showHintDlg(this,
            nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPPSCMCommon-000270")/*
                                                                                           * @res
                                                                                           * "��ʾ"
                                                                                           */, e
                .getMessage());
      }
      else {
        MessageDialog.showHintDlg(this,
            nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPPSCMCommon-000270")/*
                                                                                           * @res
                                                                                           * "��ʾ"
                                                                                           */,
            nc.ui.ml.NCLangRes.getInstance().getStrByID("40040402", "UPP40040402-000005")/*
                                                                                           * @res
                                                                                           * "����δ�ɹ�"
                                                                                           */+ ":"
                + e.getMessage());
      }
      showHintMessage("");
      return;
    }
    finally {
      // ���������˼���������
      if (mapAuditInfo.size() > 0) {
        for (int i = 0; i < proceVOs.length; i++) {
          if (!mapAuditInfo.containsKey(proceVOs[i].getHeadVO().getPrimaryKey())) {
            proceVOs[i].getHeadVO().setCauditpsn(null);
            proceVOs[i].getHeadVO().setDauditdate(null);
            continue;
          }
          listAuditInfo = (ArrayList<Object>) mapAuditInfo.get(proceVOs[i].getHeadVO().getPrimaryKey());
          proceVOs[i].getHeadVO().setCauditpsn((String) listAuditInfo.get(0));
          proceVOs[i].getHeadVO().setDauditdate((UFDate) listAuditInfo.get(1));
        }
      }
    }

    showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UCH010"));
  }

  /**
   * �˴����뷽��˵���� ����:��Ƭ״̬������. ������ ���أ� ���⣺ ���ڣ�(2002-10-15 16:15:35)
   * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
   */
  private void onBillAudit() {
    nc.vo.scm.pu.Timer timer = new nc.vo.scm.pu.Timer();
    timer.start("��Ʊ����������ʼ");
    int iInit = getInvVOs()[getCurVOPos()].getHeadVO().getFinitflag().intValue();
    if (iInit == 1) {
      MessageDialog.showHintDlg(this,
          nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPPSCMCommon-000270")/*
                                                                                         * @res
                                                                                         * "��ʾ"
                                                                                         */,
          nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPP40040401-000021")/*
                                                                                         * @res
                                                                                         * "��ѡ��ƱΪ�ڳ���Ʊ,��������!"
                                                                                         */);
      showHintMessage("");
      return;
    }
    InvoiceVO[] proceVOs = new InvoiceVO[] {
      getInvVOs()[getCurVOPos()]
    };

    for (int i = 0; i < proceVOs.length; i++) {
      if (proceVOs[i] != null && proceVOs[i].getHeadVO().getPrimaryKey() != null) {
        for (int j = 0; j < proceVOs[i].getChildrenVO().length; j++) {
          try {
            if (PuPubVO.getString_TrimZeroLenAsNull(proceVOs[i].getChildrenVO()[j].getPrimaryKey()) == null) {
              proceVOs[i] = InvoiceHelper.findByPrimaryKey(getInvVOs()[getCurVOPos()].getHeadVO().getPrimaryKey());
              getInvVOs()[getCurVOPos()] = proceVOs[i];
            }
          }
          catch (BusinessException e) {
          }
          catch (Exception e) {
          }
        }
      }

    }
    // ������������������
    String strOperPk = nc.ui.pub.ClientEnvironment.getInstance().getUser().getPrimaryKey();
    UFDate today = nc.ui.pub.ClientEnvironment.getInstance().getDate();
    // �������ڲ���С���Ƶ����ڼ��
    ClientLink cl = new ClientLink(nc.ui.pub.ClientEnvironment.getInstance());// ������Ϣ
    String errMsg = PuTool.getAuditLessThanMakeMsg(proceVOs, "dinvoicedate", "vinvoicecode", cl.getLogonDate(),
        ScmConst.PO_Invoice);
    if (errMsg != null) {
      MessageDialog.showErrorDlg(this,
          nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPPSCMCommon-000270")/*
           * @res
           * "��ʾ"
           */, errMsg);
      showHintMessage("");
      return;
    }
    String[] oldAuditPsn=new String[proceVOs.length];
    String[] oldUserid=new String[proceVOs.length];
    UFDate[] oldAuditDate=new UFDate[proceVOs.length];
    String[] oldAuditTime=new String[proceVOs.length];
    
    for (int i = 0; i < proceVOs.length; i++) {
      //����ԭ��Ϣ��������ʱ����������쳣����ֱ����գ�Ҫ�ָ�ԭ���� 
      //by zhaoyha at 2009.6.29
      oldAuditPsn[i]=proceVOs[i].getHeadVO().getCauditpsn();
      oldUserid[i]=proceVOs[i].getHeadVO().getCuserid();
      oldAuditDate[i]=proceVOs[i].getHeadVO().getDauditdate();
      oldAuditTime[i]=proceVOs[i].getHeadVO().getTaudittime();
      
      proceVOs[i].getHeadVO().setCauditpsn(strOperPk);
      proceVOs[i].getHeadVO().setDauditdate(today);
      proceVOs[i].getHeadVO().setCuserid(strOperPk);
      proceVOs[i].getHeadVO().setTaudittime((new UFDateTime(new Date())).toString());
    }

    try {
    	//���÷�Ʊ����  add by QuSida (��ɽ����) 2010-9-27
    	if(feeFlag)
      	  PfUtilClient.processBatchFlow(this, "FEEAPPROVE", nc.vo.scm.pu.BillTypeConst.PO_INVOICE, ClientEnvironment
      	          .getInstance().getDate().toString(), proceVOs, null);
        else
      PfUtilClient.processBatchFlow(this, "APPROVE", nc.vo.scm.pu.BillTypeConst.PO_INVOICE, ClientEnvironment
          .getInstance().getDate().toString(), proceVOs, null);
      timer.addExecutePhase("ִ����������APPROVE");
      InvoiceVO resultVO = null;
      Integer iBillStatus = null;
      if (PfUtilClient.isSuccess()) {
        resultVO = InvoiceHelper.findByPrimaryKey(getInvVOs()[getCurVOPos()].getHeadVO().getPrimaryKey());
        getInvVOs()[getCurVOPos()] = resultVO;
        iBillStatus = new Integer(88);
        if (resultVO != null && resultVO.getHeadVO() != null && resultVO.getHeadVO().getIbillstatus() != null) {
          iBillStatus = resultVO.getHeadVO().getIbillstatus();
        }

        // ҵ����־
//        resultVO.getOperatelogVO().setOpratorname(nc.ui.pub.ClientEnvironment.getInstance().getUser().getUserName());
//        resultVO.getOperatelogVO().setCompanyname(
//            nc.ui.pub.ClientEnvironment.getInstance().getCorporation().getUnitname());
//        resultVO.getOperatelogVO().setOperatorId(nc.ui.pub.ClientEnvironment.getInstance().getUser().getPrimaryKey());
//        Operlog operlog = new Operlog();
//        operlog.insertBusinessExceptionlog(resultVO, "����", "����", nc.vo.scm.funcs.Businesslog.MSGMESSAGE,
//            nc.vo.scm.pu.BillTypeConst.PO_INVOICE, nc.ui.sm.cmenu.Desktop.getApplet().getParameter("USER_IP"));

        // ���ÿ�Ƭ��������
        setVOToBillPanel();
        // ���°�ť״̬
        // setButtonsAndPanelState();

        timer.addExecutePhase("setVOToBillPanel");
        timer.showAllExecutePhase("��Ʊ������������");
        if (iBillStatus.compareTo(nc.vo.scm.pu.BillStatus.FREE) == 0
            || iBillStatus.compareTo(nc.vo.scm.pu.BillStatus.AUDITFAIL) == 0) {
          showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPP40040401-000234")/*
                                                                                                         * @res
                                                                                                         * "����δ�ɹ�"
                                                                                                         */);

        }
        else if (iBillStatus.compareTo(nc.vo.scm.pu.BillStatus.AUDITED) == 0
            || iBillStatus.compareTo(nc.vo.scm.pu.BillStatus.AUDITING) == 0) {
          showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000236")/*
                                                                                                           * @res
                                                                                                           * "�����ɹ�"
                                                                                                           */);
        }
      }
    }
    catch (Exception e) {
      for (int i = 0; i < proceVOs.length; i++) {
        proceVOs[i].getHeadVO().setCauditpsn(oldAuditPsn[i]);
        proceVOs[i].getHeadVO().setDauditdate(oldAuditDate[i]);
        proceVOs[i].getHeadVO().setCuserid(oldUserid[i]);
        proceVOs[i].getHeadVO().setTaudittime(oldAuditTime[i]);
      }

      SCMEnv.out(e);
      String strErrMsg = e.getMessage();
      if (strErrMsg != null
          && strErrMsg
              .indexOf(nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPP40040401-000161")/*
                                                                                                     * @res
                                                                                                     * "Ӧ��ϵͳ�ѽ���"
                                                                                                     */) >= 0) {
        MessageDialog.showHintDlg(this,
            nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPPSCMCommon-000270")/*
                                                                                           * @res
                                                                                           * "��ʾ"
                                                                                           */,
            nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPP40040401-000019")/*
                                                                                           * @res
                                                                                           * "Ӧ��ϵͳ�ѽ��ʣ���Ʊ����������"
                                                                                           */);
      }
      else if (e instanceof BusinessException || e instanceof java.rmi.RemoteException) {
        MessageDialog.showHintDlg(this,
            nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPPSCMCommon-000270")/*
                                                                                           * @res
                                                                                           * "��ʾ"
                                                                                           */, e
                .getMessage());
      }
      else {
        MessageDialog.showHintDlg(this,
            nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPPSCMCommon-000270")/*
                                                                                           * @res
                                                                                           * "��ʾ"
                                                                                           */,
            nc.ui.ml.NCLangRes.getInstance().getStrByID("40040402", "UPP40040402-000005")/*
                                                                                           * @res
                                                                                           * "����δ�ɹ�"
                                                                                           */+ ":"
                + e.getMessage());
      }
      showHintMessage("");
      return;
    }

    showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UCH010"));
  }

  /**
   * ���ߣ���ӡ�� ���ܣ������б��һ��ѡ�еĵ��� �����������������ڳ��������� �������� ���أ��� ���⣺�� ���ڣ�(2002-3-13
   * 11:39:21) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־�� 2002-04-15 wyf
   * ��Ϻ��ֻ����TS�ж��Ƿ񵥾ݸı䣬���������һЩ���Ƿ��������ж� 2002-05-23 wyf ������ķ�Ʊ�ӽ���ȥ��
   */
  private void onListAudit() {
    // �õ���ѡ�е���
    Vector validVOsVEC = new Vector();
    // ������������
    Vector validIndexVEC = new Vector();
    // ������������ͷID
    Vector validHidKeyVEC = new Vector();
    for (int i = 0; i < getInvVOs().length; i++) {
      if (m_listPanel.getHeadBillModel().getRowState(i) == BillModel.SELECTED) {
        // �п��ܾ��������򣬵õ������ı�ͷINDEX
        int nCurIndex = PuTool.getIndexBeforeSort(m_listPanel, i);
        // �ų��ڳ��� 0-��ͨ 1-�ڳ� 2-ϵͳ
        int iInit = getInvVOs()[nCurIndex].getHeadVO().getFinitflag().intValue();
        if (iInit == 1) {
          MessageDialog.showHintDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON",
              "UPPSCMCommon-000270")/* @res "��ʾ" */, nc.ui.ml.NCLangRes.getInstance().getStrByID("40040402",
              "UPP40040402-000002")/* @res "��ѡ��Ʊ�����ڳ���Ʊ��" */);
          showHintMessage("");
          return;
        }
        // ����������
        validIndexVEC.addElement(new Integer(nCurIndex));
        validVOsVEC.addElement(getInvVOs()[nCurIndex]);
        validHidKeyVEC.add(getInvVOs()[nCurIndex].getHeadVO().getPrimaryKey());
      }
    }

    if (validVOsVEC.size() == 0) {
      MessageDialog.showHintDlg(this,
          nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000270")/*
                                                                                           * @res
                                                                                           * "��ʾ"
                                                                                           */,
          nc.ui.ml.NCLangRes.getInstance().getStrByID("40040402", "UPP40040402-000003")/*
                                                                                         * @res
                                                                                         * "����ѡ��Ҫ�����ķ�Ʊ��"
                                                                                         */);
      setSelectedRowCount(0);
      showHintMessage("");
      return;
    }
    else if (validVOsVEC.size() > 0) {
      for (int i = 0; i < validVOsVEC.size(); i++) {
        if (PuPubVO.getString_TrimZeroLenAsNull(((InvoiceVO) validVOsVEC.get(i)).getHeadVO().getCinvoiceid()) == null) {
          MessageDialog.showHintDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON",
              "UPPSCMCommon-000270")/* @res "��ʾ" */, nc.ui.ml.NCLangRes.getInstance().getStrByID("40040402",
              "UPP40040402-000003")/* @res "����ѡ��Ҫ�����ķ�Ʊ��" */);
          setSelectedRowCount(0);
          showHintMessage("");
          return;
        }
      }
    }

    // �õ���������VO����ӦINDEX
    InvoiceVO[] proceVOs = new InvoiceVO[validVOsVEC.size()];
    validVOsVEC.copyInto(proceVOs);

    // ������������������
    String strOperPk = nc.ui.pub.ClientEnvironment.getInstance().getUser().getPrimaryKey();
    UFDate today = nc.ui.pub.ClientEnvironment.getInstance().getDate();

    // ���������˼��������ڹ�ϣ������ʧ��ʱ�õ�
    HashMap<String, ArrayList<Object>> mapAuditInfo = new HashMap<String, ArrayList<Object>>();
    ArrayList<Object> listAuditInfo = null;

    // �������ڲ���С���Ƶ����ڼ��
    ClientLink cl = new ClientLink(nc.ui.pub.ClientEnvironment.getInstance());// ������Ϣ
    String errMsg = PuTool.getAuditLessThanMakeMsg(proceVOs, "dinvoicedate", "vinvoicecode", cl.getLogonDate(),
        ScmConst.PO_Invoice);
    if (errMsg != null) {
      MessageDialog.showErrorDlg(this,
          nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPPSCMCommon-000270")/*
                                                                                         * @res
                                                                                         * "��ʾ"
                                                                                         */, errMsg);
      showHintMessage("");
      return;
    }

    for (int i = 0; i < proceVOs.length; i++) {
      // ����ʧ��ʱ�õ������������˼��������ڹ�ϣ��
      if (PuPubVO.getString_TrimZeroLenAsNull(proceVOs[i].getHeadVO().getCauditpsn()) != null) {
        listAuditInfo = new ArrayList<Object>();
        listAuditInfo.add(proceVOs[i].getHeadVO().getCauditpsn());
        listAuditInfo.add(proceVOs[i].getHeadVO().getDauditdate());
        mapAuditInfo.put(proceVOs[i].getHeadVO().getPrimaryKey(), listAuditInfo);
      }
      proceVOs[i].getHeadVO().setCauditpsn(strOperPk);
      proceVOs[i].getHeadVO().setDauditdate(today);
      proceVOs[i].getHeadVO().setCuserid(strOperPk);
      proceVOs[i].getHeadVO().setTaudittime((new UFDateTime(new Date())).toString());
      proceVOs[i].getHeadVO().setCoperatoridnow(getClientEnvironment().getUser().getPrimaryKey());
    }

    try {
      if (!loadItemsForInvoiceVOs(proceVOs)) {
        showHintMessage("");
        return;
      }
      PfUtilClient.processBatchFlow(this, "APPROVE", nc.vo.scm.pu.BillTypeConst.PO_INVOICE, ClientEnvironment
          .getInstance().getDate().toString(), proceVOs, null);
    }
    catch (Exception e) {
      for (int i = 0; i < proceVOs.length; i++) {
        proceVOs[i].getHeadVO().setCauditpsn(null);
        proceVOs[i].getHeadVO().setDauditdate(null);
        proceVOs[i].getHeadVO().setCuserid(null);
        proceVOs[i].getHeadVO().setTaudittime(null);
      }

      SCMEnv.out(e);
      String strErrMsg = e.getMessage();
      if (strErrMsg != null
          && strErrMsg
              .indexOf(nc.ui.ml.NCLangRes.getInstance().getStrByID("40040402", "UPP40040402-000019")/*
                                                                                                     * @res
                                                                                                     * "Ӧ��ϵͳ�ѽ���"
                                                                                                     */) >= 0) {
        MessageDialog.showHintDlg(this,
            nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000270")/*
                                                                                             * @res
                                                                                             * "��ʾ"
                                                                                             */,
            nc.ui.ml.NCLangRes.getInstance().getStrByID("40040402", "UPP40040402-000004")/*
                                                                                           * @res
                                                                                           * "Ӧ��ϵͳ�ѽ��ʣ���Ʊ����������"
                                                                                           */);
      }
      else if (e instanceof BusinessException || e instanceof java.rmi.RemoteException) {
        MessageDialog.showHintDlg(this,
            nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000270")/*
                                                                                             * @res
                                                                                             * "��ʾ"
                                                                                             */, e
                .getMessage());
      }
      else {
        MessageDialog.showHintDlg(this,
            nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000270")/*
                                                                                             * @res
                                                                                             * "��ʾ"
                                                                                             */,
            nc.ui.ml.NCLangRes.getInstance().getStrByID("40040402", "UPP40040402-000005")/*
                                                                                           * @res
                                                                                           * "����δ�ɹ�"
                                                                                           */+ ":"
                + e.getMessage());
      }
      showHintMessage("");
      return;
    }
    finally {
      // ���������˼���������
      if (mapAuditInfo.size() > 0) {
        for (int i = 0; i < proceVOs.length; i++) {
          if (!mapAuditInfo.containsKey(proceVOs[i].getHeadVO().getPrimaryKey())) {
            proceVOs[i].getHeadVO().setCauditpsn(null);
            proceVOs[i].getHeadVO().setDauditdate(null);
            continue;
          }
          listAuditInfo = (ArrayList<Object>) mapAuditInfo.get(proceVOs[i].getHeadVO().getPrimaryKey());
          proceVOs[i].getHeadVO().setCauditpsn((String) listAuditInfo.get(0));
          proceVOs[i].getHeadVO().setDauditdate((UFDate) listAuditInfo.get(1));
        }
      }
    }

    if (PfUtilClient.isSuccess()) {

      // //�ӻ����г�ȥ
      // Integer[] iaIndex = new Integer[validIndexVEC.size()] ;
      // validIndexVEC.copyInto(iaIndex) ;
      //
      // removeSomeFromInvVOs(iaIndex) ;
      // setVOsToListPanel() ;
      // ˢ��
      // ҵ����־
//      Operlog operlog = new Operlog();
//      for (InvoiceVO vo : proceVOs) {
//        vo.getOperatelogVO().setOpratorname(nc.ui.pub.ClientEnvironment.getInstance().getUser().getUserName());
//        vo.getOperatelogVO().setCompanyname(nc.ui.pub.ClientEnvironment.getInstance().getCorporation().getUnitname());
//        vo.getOperatelogVO().setOperatorId(nc.ui.pub.ClientEnvironment.getInstance().getUser().getPrimaryKey());
//        operlog.insertBusinessExceptionlog(vo, "����", "����", nc.vo.scm.funcs.Businesslog.MSGMESSAGE,
//            nc.vo.scm.pu.BillTypeConst.PO_INVOICE, nc.ui.sm.cmenu.Desktop.getApplet().getParameter("USER_IP"));
//      }
      onListRefresh();
      ConditionVO[] definedVOs = null;
      // δ��ѯֱ�ӵ�ˢ��
      if (definedVOs == null) {
        Hashtable<String, InvoiceVO> resultH = new Hashtable<String, InvoiceVO>();
        for (int i = 0; i < validIndexVEC.size(); i++) {
          try {
            resultH = InvoiceHelper.findByPrimaryKeyBantch(validHidKeyVEC);
          }
          catch (Exception e) {
            // TODO �Զ����� catch ��
            SCMEnv.out(e);
          }
          if (resultH != null
              && resultH.get(getInvVOs()[((Integer) validIndexVEC.get(i)).intValue()].getHeadVO().getPrimaryKey()) != null) {
            getInvVOs()[((Integer) validIndexVEC.get(i)).intValue()] = resultH.get(getInvVOs()[((Integer) validIndexVEC
                .get(i)).intValue()].getHeadVO().getPrimaryKey());
          }
        }
        setInvVOs(getInvVOs());

        // ��ʾ�б�ǰ����VO,���Ծ�����ʽ�����ֵ��ֵ
        setVOsToListPanel();

        // ���ÿ�Ƭ���б����ʾ״̬
        setBillBrowseState(STATE_BROWSE_NORMAL);
        setListOperState(STATE_LIST_NORMAL);
        setCurOperState(getListOperState());

      }
      showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UCH010"));
      return;
    }
    else {
      // ���������˼���������
      if (mapAuditInfo.size() > 0) {
        for (int i = 0; i < proceVOs.length; i++) {
          if (!mapAuditInfo.containsKey(proceVOs[i].getHeadVO().getPrimaryKey())) {
            proceVOs[i].getHeadVO().setCauditpsn(null);
            proceVOs[i].getHeadVO().setDauditdate(null);
            continue;
          }
          listAuditInfo = (ArrayList<Object>) mapAuditInfo.get(proceVOs[i].getHeadVO().getPrimaryKey());
          proceVOs[i].getHeadVO().setCauditpsn((String) listAuditInfo.get(0));
          proceVOs[i].getHeadVO().setDauditdate((UFDate) listAuditInfo.get(1));
        }
      }
    }

    showHintMessage("");
    return;
  }

  /**
   * ���ߣ���ӡ�� ���ܣ������б��һ��ѡ�еĵ��� �������������������״̬ �������� ���أ��� ���⣺�� ���ڣ�(2002-3-13 11:39:21)
   * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־�� 2002-04-15 wyf ��Ϻ��ֻ����TS�ж��Ƿ񵥾ݸı䣬���������һЩ���Ƿ��������ж�
   * 2002-05-23 wyf �����ķ�Ʊ�ӽ���ȥ��
   */
  private void onListUnAudit() {
    // �õ���ѡ�е���
    Vector validVOsVEC = new Vector();
    // �����������
    Vector validIndexVEC = new Vector();
    for (int i = 0; i < getInvVOs().length; i++) {
      if (getBillListPanel().getHeadBillModel().getRowState(i) == BillModel.SELECTED) {
        // �п��ܾ��������򣬵õ������ı�ͷINDEX
        int nCurIndex = PuTool.getIndexBeforeSort(getBillListPanel(), i);
        // ���������
        validIndexVEC.addElement(new Integer(nCurIndex));
        validVOsVEC.addElement(getInvVOs()[nCurIndex]);
      }
    }
    if (validVOsVEC.size() == 0) {
      MessageDialog.showHintDlg(this,
          nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000270")/*
                                                                                           * @res
                                                                                           * "��ʾ"
                                                                                           */,
          nc.ui.ml.NCLangRes.getInstance().getStrByID("40040402", "UPP40040402-000006")/*
                                                                                         * @res
                                                                                         * "����ѡ��Ҫ����ķ�Ʊ��"
                                                                                         */);
      setSelectedRowCount(0);
      showHintMessage("");
      return;
    }

    InvoiceVO[] proceVOs = new InvoiceVO[validVOsVEC.size()];
    validVOsVEC.copyInto(proceVOs);

    String[] strAuditIDs = new String[proceVOs.length];

    // ���������˼��������ڹ�ϣ������ʧ��ʱ�õ�
    HashMap<String, ArrayList<Object>> mapAuditInfo = new HashMap<String, ArrayList<Object>>();
    ArrayList<Object> listAuditInfo = null;

    try {
      // ���������
      for (int i = 0; i < proceVOs.length; i++) {
        strAuditIDs[i] = proceVOs[i].getHeadVO().getCauditpsn();
        // ����ʧ��ʱ�õ������������˼��������ڹ�ϣ��
        if (PuPubVO.getString_TrimZeroLenAsNull(strAuditIDs[i]) != null) {
          listAuditInfo = new ArrayList<Object>();
          listAuditInfo.add(proceVOs[i].getHeadVO().getCauditpsn());
          listAuditInfo.add(proceVOs[i].getHeadVO().getDauditdate());
          mapAuditInfo.put(proceVOs[i].getHeadVO().getPrimaryKey(), listAuditInfo);
        }
        // ������Ա��ԭ������ID��Ϊ�ж��Ƿ������������˵ĵ���
        proceVOs[i].getHeadVO().setCoperatoridnow(getClientEnvironment().getUser().getPrimaryKey());
        proceVOs[i].getHeadVO().setAttributeValue("cauditpsnold", proceVOs[i].getHeadVO().getCauditpsn());

        proceVOs[i].getHeadVO().setCauditpsn(nc.ui.pub.ClientEnvironment.getInstance().getUser().getPrimaryKey());
        proceVOs[i].getHeadVO().setCuserid(nc.ui.pub.ClientEnvironment.getInstance().getUser().getPrimaryKey());
        proceVOs[i].getHeadVO().setTaudittime(null);
      }
      if (!loadItemsForInvoiceVOs(proceVOs)) {
        showHintMessage("");
        return;
      }

      // �ѽ���ķ�Ʊ�ܷ��������:
      // IF ��Ʊ��ҵ�����ͺ������Ϊ���д���,ֱ�˲ɹ�, ����Ϊ���ⷢƱ����Ϊ���Ʒ�Ʊ, ���ܿ���
      // ���� IF ��Ʊ��Դ����ⵥ���Ҹ�����⿪Ʊ�ܷ�������Ϊ��������, ���쳣
      if (m_sZGYF == null || !(new UFBoolean(m_sZGYF).booleanValue())) {
        InvoiceVO invoiceVO = new InvoiceVO();
        for (int i = 0; i < proceVOs.length; i++) {
          invoiceVO = proceVOs[i];
          Object oTemp = CacheTool.getCellValue("bd_busitype", "pk_busitype", "verifyrule", invoiceVO.getHeadVO()
              .getCbiztype());
          if (oTemp == null)
            continue;
          Object oTemp1[] = (Object[]) oTemp;
          if (oTemp1[0].equals("S") || oTemp1[0].equals("Z"))
            continue;// ���д�����ֱ�˲ɹ�ҵ������
          if (invoiceVO.getHeadVO().getIinvoicetype().intValue() == 3)
            continue;// ���ⷢƱ
          InvoiceItemVO tempBodyVO[] = invoiceVO.getBodyVO();
          for (int j = 0; j < tempBodyVO.length; j++) {
            if (tempBodyVO[j].getCupsourcebilltype() == null)
              continue;// ���Ʋɹ���Ʊ
            if ((tempBodyVO[j].getCupsourcebilltype().equals("45") || tempBodyVO[j].getCupsourcebilltype().equals("47"))
                && m_sOrder2InvoiceSettleMode != null && m_sStock2InvoiceSettleMode.equals("����ʱ�Զ�����")) {
              if (tempBodyVO[j].getNaccumsettmny() != null && tempBodyVO[j].getNaccumsettmny().doubleValue() != 0.0) {
                MessageDialog.showHintDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401",
                    "UPPSCMCommon-000270")/* @res "��ʾ" */, nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401",
                    "UPP40040401-000022")/* @res "�÷�Ʊ�ѽ��㣬��������" */);
                showHintMessage("");
                return;
              }
            }
          }
        }
      }
      PfUtilClient.processBatch("UNAPPROVE", nc.vo.scm.pu.BillTypeConst.PO_INVOICE, ClientEnvironment.getInstance().getDate().toString(), proceVOs);
    }
    catch (Exception e) {
      String strErrMsg = e.getMessage();
      if (strErrMsg != null
          && (strErrMsg
              .indexOf(nc.ui.ml.NCLangRes.getInstance().getStrByID("40040402", "UPP40040402-000020")/*
                                                                                                     * @res
                                                                                                     * "����"
                                                                                                     */) >= 0 || strErrMsg
              .indexOf(nc.ui.ml.NCLangRes.getInstance().getStrByID("40040402", "UPP40040402-000021")/*
                                                                                                     * @res
                                                                                                     * "����ɾ��"
                                                                                                     */) >= 0)) {
        strErrMsg = nc.ui.ml.NCLangRes.getInstance().getStrByID("40040402", "UPP40040402-000007")/*
                                                                                                   * @res
                                                                                                   * "�ɹ���Ʊ����ʧ�ܣ����ݴ��ں���������Ѿ�������ʵʱƾ֤����������!"
                                                                                                   */;
        MessageDialog.showHintDlg(this,
            nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000270")/*
                                                                                             * @res
                                                                                             * "��ʾ"
                                                                                             */, strErrMsg);
      }
      else if (e instanceof BusinessException) {
        MessageDialog.showHintDlg(this,
            nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000270")/*
                                                                                             * @res
                                                                                             * "��ʾ"
                                                                                             */, strErrMsg);
      }
      else {
        strErrMsg = nc.ui.ml.NCLangRes.getInstance().getStrByID("40040402", "UPP40040402-000008")/*
                                                                                                   * @res
                                                                                                   * "����ʧ��!���������л���ԭ���磺\n1)������δ��ȷ����\n2)��NC�������ն�\n��ȷ�ϻ�����ȷ���ٴβ���!"
                                                                                                   */;
        SCMEnv.out("!!! ������Ϣ�ɷ���ϵͳ����Ա�ο���\n");
        SCMEnv.out(e);
        MessageDialog.showHintDlg(this,
            nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000270")/*
                                                                                             * @res
                                                                                             * "��ʾ"
                                                                                             */, strErrMsg);
      }
      showHintMessage("");
      return;
    }
    finally {
      // ���������˼���������
      if (mapAuditInfo.size() > 0) {
        for (int i = 0; i < proceVOs.length; i++) {
          if (!mapAuditInfo.containsKey(proceVOs[i].getHeadVO().getPrimaryKey())) {
            proceVOs[i].getHeadVO().setCauditpsn(null);
            proceVOs[i].getHeadVO().setDauditdate(null);
            continue;
          }
          listAuditInfo = (ArrayList<Object>) mapAuditInfo.get(proceVOs[i].getHeadVO().getPrimaryKey());
          proceVOs[i].getHeadVO().setCauditpsn((String) listAuditInfo.get(0));
          proceVOs[i].getHeadVO().setDauditdate((UFDate) listAuditInfo.get(1));
        }
      }

    }

    if (PfUtilClient.isSuccess()) {
      // //�ӻ����г�ȥ
      // Integer[] iaIndex = new Integer[validIndexVEC.size()] ;
      // validIndexVEC.copyInto(iaIndex) ;
      // removeSomeFromInvVOs(iaIndex) ;
      // setVOsToListPanel();
      // ҵ����־
//      Operlog operlog = new Operlog();
//      for (InvoiceVO vo : proceVOs) {
//        vo.getOperatelogVO().setOpratorname(nc.ui.pub.ClientEnvironment.getInstance().getUser().getUserName());
//        vo.getOperatelogVO().setCompanyname(nc.ui.pub.ClientEnvironment.getInstance().getCorporation().getUnitname());
//        vo.getOperatelogVO().setOperatorId(nc.ui.pub.ClientEnvironment.getInstance().getUser().getPrimaryKey());
//        operlog.insertBusinessExceptionlog(vo, "����", "����", nc.vo.scm.funcs.Businesslog.MSGMESSAGE,
//            nc.vo.scm.pu.BillTypeConst.PO_INVOICE, nc.ui.sm.cmenu.Desktop.getApplet().getParameter("USER_IP"));
//      }
      // ˢ��
      onListRefresh();

      showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UCH011"));
      return;
    }
    else {
      // ���������˼���������
      if (mapAuditInfo.size() > 0) {
        for (int i = 0; i < proceVOs.length; i++) {
          if (!mapAuditInfo.containsKey(proceVOs[i].getHeadVO().getPrimaryKey())) {
            proceVOs[i].getHeadVO().setCauditpsn(null);
            proceVOs[i].getHeadVO().setDauditdate(null);
            continue;
          }
          listAuditInfo = (ArrayList<Object>) mapAuditInfo.get(proceVOs[i].getHeadVO().getPrimaryKey());
          proceVOs[i].getHeadVO().setCauditpsn((String) listAuditInfo.get(0));
          proceVOs[i].getHeadVO().setDauditdate((UFDate) listAuditInfo.get(1));
        }
      }
    }
    showHintMessage("");
  }

  /**
   * �����Ĺ��ܡ���;�������Եĸ��ģ��Լ�����ִ��ǰ������״̬��
   * 
   * @param ����˵��
   * @return ����ֵ
   * @exception �쳣����
   * @see ��Ҫ�μ�����������
   * @since �������һ���汾���˷�������ӽ���������ѡ��
   * @deprecated �÷����������һ���汾���Ѿ������������滻������ѡ��
   */
  private void onBillCancel() {
    // һ���µ�δ������ķ�Ʊ
    // /ID_OK = 1;ID_CANCEL = 2;ID_YES = 4; ID_NO = 8;
    int result = MessageDialog.ID_YES;// MessageDialog.showYesNoCancelDlg(this,
                                      // nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401","UPPSCMCommon-000270")/*@res
                                      // "��ʾ"*/,
                                      // nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401","UPP40040401-000162")/*@res
                                      // "�Ƿ��������?"*/);
    if (result == MessageDialog.ID_YES) {
      // ����ת��״̬
      int nVOStatus = 0;
      if (getInvVOs() != null){
        nVOStatus = getInvVOs()[getCurVOPos()].getSource();
      }
      try {
        if (getInvVOs() != null) {
          getInvVOs()[getCurVOPos()] = InvoiceHelper.findByPrimaryKey(getInvVOs()[getCurVOPos()].getHeadVO().getPrimaryKey());
          getInvVOs()[getCurVOPos()].setSource(InvoiceVO.FROM_QUERY);
        }
      }
      catch (Exception e) {
        SCMEnv.out(e);
      }

      // ���ǲ�ѯ���ĵ��ݴӻ�����ɾ��(�޸�״̬����)
      if (getInvVOs() != null && nVOStatus != InvoiceVO.FROM_QUERY) {
        removeOneFromInvVOs(getCurVOPos());
      }

      if (getInvVOs() == null) {
        setListOperState(STATE_LIST_NORMAL);
        setBillBrowseState(STATE_BROWSE_NORMAL);
      }

      // ����������ת����ת���б�
      if (nVOStatus != InvoiceVO.FROM_HAND && nVOStatus != InvoiceVO.FROM_QUERY) {
        // �����б��������
        setCurOperState(getListOperState());
        shiftShowModeTo(INV_PANEL_LIST);
        setVOsToListPanel();
        m_bCopy = false;
        return;
      }
      else {
        // ���ÿ�Ƭ��������
        if (getInvVOs() == null) {
          // V5 Del : setImageType(this.IMAGE_NULL);
          getBillCardPanel().addNew();
          getBillCardPanel().setEnabled(false);
          setCurOperState(getBillBrowseState());
        }
        else {
          if (getInvVOs() != null && getInvVOs().length > 0) {
            InvoiceVO curVO = getInvVOs()[getCurVOPos()];
            String id = curVO.getHeadVO().getCinvoiceid();
            if (id != null) {
              Object o = hBillStatusBeforeEdit.get(id);
              if (o != null) {
                Integer iBillStatus = new Integer(o.toString());
                if (iBillStatus.intValue() == 4) {
                  curVO.getHeadVO().setIbillstatus(new Integer(4));
                  hBillStatusBeforeEdit.put(id, new Integer(0));
                }
              }
            }
          }
          setCurOperState(getBillBrowseState());
          setVOToBillPanel();
        }
      }
    }
    else if (result == MessageDialog.ID_NO) {
      onSave();
    }
    m_bAdd = false;
    m_bCopy = false;
  }

  /**
   * �ϲ���ʾ �������ڣ�(2003-10-31 14:05:25)
   */
  private void onBillCombin() {
    CollectSettingDlg dlg = new CollectSettingDlg(this, NCLangRes.getInstance().getStrByID("common",
        "4004COMMON000000089")/* @res "�ϲ���ʾ" */, ScmConst.PO_Invoice, "40040401", getCorpId(), ClientEnvironment
        .getInstance().getUser().getPrimaryKey(), InvoiceVO.class.getName(), InvoiceHeaderVO.class.getName(),
        InvoiceItemVO.class.getName());
    //
    dlg.initData(getBillCardPanel(), new String[] {
        "invcode", "invname", "invspec", "invtype", "cproducearea", "noriginaltaxmny", "noriginalsummny"
    }, // �̶�������
        null, // new String[]{"dplanarrvdate"},ȱʡ������
        new String[] {
            "ninvoicenum", "noriginalcurprice", "noriginalcurmny", "noriginaltaxpricemny"
        },// �����
        null, new String[] {
            "noriginalcurprice", "norgnettaxprice"
        }, "ninvoicenum");
    dlg.showModal();
    // ���µ�ǰ����VO
    if(getBillCardPanel()!=null){
      if(getInvVOs()[getCurVOPos()].getPrimaryKey().equals(getCurVOonCard().getPrimaryKey()))
        getInvVOs()[getCurVOPos()]=getCurVOonCard();
    }
  }

  /**
   * ��ѯ���з��������ķ�Ʊ
   * 
   * @param ����˵��
   * @return ����ֵ
   * @exception �쳣����
   * @see ��Ҫ�μ�����������
   * @since �������һ���汾���˷�������ӽ���������ѡ��
   */

  // ��ʱȡ�����иõ�λ�ķ�Ʊ,��ͬʱ��ʾ��һ��
  private void onBillQuery() {
    //��ʾ��ѯ�Ի���
    getQueDlg();
    m_InvQueDlg.showModal();

    //��������Ȩ��:���ڲ�ѯ�Ի����showModal()֮����Ϊ��ʱ������Ȩ��Ҫȡ���ڶ๫˾ҳǩ��ѡ��Ĺ�˾. lixiaodong , v51
    m_InvQueDlg.setRefsDataPowerConVOs(getCurOperator(), new String[] {getPk_corp()}, IDataPowerForInv.REFNAMES, IDataPowerForInv.REFKEYS,
          IDataPowerForInv.RETURNTYPES);

    //�õ���ѯ����
    if (m_InvQueDlg.isCloseOK()) {
      //����ҵ������
//      setOldBizeType(getCurBizeType());

      //ִ�в�ѯ
      execBillQuery(m_InvQueDlg);

      //�����Ƿ��������
      setEverQueryed(true);
    }
  }

  /**
   * ����һ�εĲ�ѯ�������²�ѯ
   * 
   * @param ����˵��
   * @return ����ֵ
   * @exception �쳣����
   * @see ��Ҫ�μ�����������
   * @since �������һ���汾���˷�������ӽ���������ѡ��
   * @deprecated �÷����������һ���汾���Ѿ������������滻������ѡ��
   */
  private void onBillRefresh() {

    // δ���ٵĶԻ���
    execBillQuery(getQueDlg());
  }

  /**
   * �˴����뷽��˵���� ���ܣ���Ϣ����ר�� ������ ���أ� ���⣺ ���ڣ�(2002-10-15 16:15:35)
   * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
   */
  private void onBillUnAudit() {
    nc.vo.scm.pu.Timer timer = new nc.vo.scm.pu.Timer();
    timer.start("�ɹ���Ʊ����ʼ");

    InvoiceVO[] proceVOs = new InvoiceVO[] {
      getInvVOs()[getCurVOPos()]
    };

    // ���������˼��������ڹ�ϣ������ʧ��ʱ�õ�
    HashMap<String, ArrayList<Object>> mapAuditInfo = new HashMap<String, ArrayList<Object>>();
    ArrayList<Object> listAuditInfo = null;

    try {

      // ���������
      for (int i = 0; i < proceVOs.length; i++) {
        // ����ʧ��ʱ�õ������������˼��������ڹ�ϣ��
        if (PuPubVO.getString_TrimZeroLenAsNull(proceVOs[i].getHeadVO().getCauditpsn()) != null) {
          listAuditInfo = new ArrayList<Object>();
          listAuditInfo.add(proceVOs[i].getHeadVO().getCauditpsn());
          listAuditInfo.add(proceVOs[i].getHeadVO().getDauditdate());
          mapAuditInfo.put(proceVOs[i].getHeadVO().getPrimaryKey(), listAuditInfo);
        }
        // ������Ա��ԭ������ID��Ϊ�ж��Ƿ������������˵ĵ���
        proceVOs[i].getHeadVO().setCoperatoridnow(getClientEnvironment().getUser().getPrimaryKey());
        proceVOs[i].getHeadVO().setAttributeValue("cauditpsnold", proceVOs[i].getHeadVO().getCauditpsn());

        proceVOs[i].getHeadVO().setCauditpsn(nc.ui.pub.ClientEnvironment.getInstance().getUser().getPrimaryKey());
        proceVOs[i].getHeadVO().setCuserid(nc.ui.pub.ClientEnvironment.getInstance().getUser().getPrimaryKey());
      }
      if (!loadItemsForInvoiceVOs(proceVOs)) {
        showHintMessage("");
        return;
      }
      timer.addExecutePhase("��̬���ر���loadItemsForInvoiceVOs");

      // �ѽ���ķ�Ʊ�ܷ��������:
      // IF ��Ʊ��ҵ�����ͺ������Ϊ���д���,ֱ�˲ɹ�, ����Ϊ���ⷢƱ����Ϊ���Ʒ�Ʊ, ���ܿ���
      // ���� IF ��Ʊ��Դ����ⵥ���Ҹ�����⿪Ʊ�ܷ�������Ϊ��������, ���쳣
      if (m_sZGYF == null || !(new UFBoolean(m_sZGYF).booleanValue())) {
        InvoiceVO invoiceVO = new InvoiceVO();
        for (int i = 0; i < proceVOs.length; i++) {
          invoiceVO = proceVOs[i];
          Object oTemp = CacheTool.getCellValue("bd_busitype", "pk_busitype", "verifyrule", invoiceVO.getHeadVO()
              .getCbiztype());
          if (oTemp == null)
            continue;
          Object oTemp1[] = (Object[]) oTemp;
          if (oTemp1[0].equals("S") || oTemp1[0].equals("Z"))
            continue;// ���д�����ֱ�˲ɹ�ҵ������
          if (invoiceVO.getHeadVO().getIinvoicetype().intValue() == 3)
            continue;// ���ⷢƱ
          InvoiceItemVO tempBodyVO[] = invoiceVO.getBodyVO();
          for (int j = 0; j < tempBodyVO.length; j++) {
            if (tempBodyVO[j].getCupsourcebilltype() == null)
              continue;// ���Ʋɹ���Ʊ
            if ((tempBodyVO[j].getCupsourcebilltype().equals("45") || tempBodyVO[j].getCupsourcebilltype().equals("47"))
                && m_sOrder2InvoiceSettleMode != null && m_sStock2InvoiceSettleMode.equals("����ʱ�Զ�����")) {
              if (tempBodyVO[j].getNaccumsettmny() != null && tempBodyVO[j].getNaccumsettmny().doubleValue() != 0.0) {
                MessageDialog.showHintDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401",
                    "UPPSCMCommon-000270")/* @res "��ʾ" */, nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401",
                    "UPP40040401-000022")/* @res "�÷�Ʊ�ѽ��㣬��������" */);
                showHintMessage("");
                return;
              }
            }
          }
        }
      }
      PfUtilClient.processBatch("UNAPPROVE" + nc.ui.pub.ClientEnvironment.getInstance().getUser().getPrimaryKey(),
          nc.vo.scm.pu.BillTypeConst.PO_INVOICE, ClientEnvironment.getInstance().getDate().toString(), proceVOs);

      //
      InvoiceVO resultVO = null;
      if (PfUtilClient.isSuccess()) {
//        resultVO = InvoiceHelper.findByPrimaryKey(getInvVOs()[getCurVOPos()].getHeadVO().getPrimaryKey());
        // ҵ����־
//        resultVO.getOperatelogVO().setOpratorname(nc.ui.pub.ClientEnvironment.getInstance().getUser().getUserName());
//        resultVO.getOperatelogVO().setCompanyname(
//            nc.ui.pub.ClientEnvironment.getInstance().getCorporation().getUnitname());
//        resultVO.getOperatelogVO().setOperatorId(nc.ui.pub.ClientEnvironment.getInstance().getUser().getPrimaryKey());
//        Operlog operlog = new Operlog();
//        operlog.insertBusinessExceptionlog(resultVO, "����", "����", nc.vo.scm.funcs.Businesslog.MSGMESSAGE,
//            nc.vo.scm.pu.BillTypeConst.PO_INVOICE, nc.ui.sm.cmenu.Desktop.getApplet().getParameter("USER_IP"));
//        getInvVOs()[getCurVOPos()] = resultVO;
      }
      else {
        // ���������˼���������
        if (mapAuditInfo.size() > 0) {
          for (int i = 0; i < proceVOs.length; i++) {
            if (!mapAuditInfo.containsKey(proceVOs[i].getHeadVO().getPrimaryKey())) {
              proceVOs[i].getHeadVO().setCauditpsn(null);
              proceVOs[i].getHeadVO().setDauditdate(null);
              continue;
            }
            listAuditInfo = (ArrayList<Object>) mapAuditInfo.get(proceVOs[i].getHeadVO().getPrimaryKey());
            proceVOs[i].getHeadVO().setCauditpsn((String) listAuditInfo.get(0));
            proceVOs[i].getHeadVO().setDauditdate((UFDate) listAuditInfo.get(1));
          }
        }
      }

      timer.addExecutePhase("ִ��UNAPPROVE�ű�");

      if (PfUtilClient.isSuccess()) {
        InvoiceHeaderVO headVO = getInvVOs()[getCurVOPos()].getHeadVO();
        ArrayList arrRet = InvoiceHelper.queryForSaveAudit(headVO.getPrimaryKey());
        headVO.setDauditdate((UFDate) arrRet.get(0));
        headVO.setCauditpsn((String) arrRet.get(1));
        headVO.setIbillstatus((Integer) arrRet.get(2));
        headVO.setTs((String) arrRet.get(3));
        headVO.setTaudittime((String) arrRet.get(4));

        timer.addExecutePhase("��ѯqueryForSaveAudit����");

        // ���ÿ�Ƭ��������
        setVOToBillPanel();
        // ���°�ť״̬
        m_btnInvBillUnAudit.setEnabled(false);
        // setButtonsAndPanelState();

        timer.addExecutePhase("setVOToBillPanel");
        timer.showAllExecutePhase("�ɹ���Ʊ�������");
      }
    }
    catch (Exception e) {
      String strErrMsg = e.getMessage();
      if (strErrMsg != null
          && (strErrMsg
              .indexOf(nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPP40040401-000163")/*
                                                                                                     * @res
                                                                                                     * "����"
                                                                                                     */) >= 0 || strErrMsg
              .indexOf(nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPP40040401-000164")/*
                                                                                                     * @res
                                                                                                     * "����ɾ��"
                                                                                                     */) >= 0)) {
        strErrMsg = nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPP40040401-000023")/*
                                                                                                   * @res
                                                                                                   * "�ɹ���Ʊ����ʧ�ܣ����ݴ��ں���������Ѿ�������ʵʱƾ֤����������!"
                                                                                                   */;
        MessageDialog.showHintDlg(this,
            nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPPSCMCommon-000270")/*
                                                                                           * @res
                                                                                           * "��ʾ"
                                                                                           */, strErrMsg);
      }
      else if (e instanceof BusinessException) {
        MessageDialog.showHintDlg(this,
            nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPPSCMCommon-000270")/*
                                                                                           * @res
                                                                                           * "��ʾ"
                                                                                           */, strErrMsg);
      }
      else {
        strErrMsg = nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPP40040401-000024")/*
                                                                                                   * @res
                                                                                                   * "����ʧ��!���������л���ԭ���磺\n1)������δ��ȷ����\n2)��NC�������ն�\n��ȷ�ϻ�����ȷ���ٴβ���!"
                                                                                                   */;
        SCMEnv.out("!!! ������Ϣ�ɷ���ϵͳ����Ա�ο���\n");
        SCMEnv.out(e);
        MessageDialog.showHintDlg(this,
            nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPPSCMCommon-000270")/*
                                                                                           * @res
                                                                                           * "��ʾ"
                                                                                           */, strErrMsg);
      }
      showHintMessage("");
      return;
    }
    finally {
      // ���������˼���������
      if (mapAuditInfo.size() > 0) {
        for (int i = 0; i < proceVOs.length; i++) {
          if (!mapAuditInfo.containsKey(proceVOs[i].getHeadVO().getPrimaryKey())) {
            proceVOs[i].getHeadVO().setCauditpsn(null);
            proceVOs[i].getHeadVO().setDauditdate(null);
            continue;
          }
          listAuditInfo = (ArrayList<Object>) mapAuditInfo.get(proceVOs[i].getHeadVO().getPrimaryKey());
          proceVOs[i].getHeadVO().setCauditpsn((String) listAuditInfo.get(0));
          proceVOs[i].getHeadVO().setDauditdate((UFDate) listAuditInfo.get(1));
        }
      }
    }

    showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UCH011"));
  }

  /**
   * ����ʵ�ָ÷�������Ӧ��ť�¼���
   * 
   * @version (00-6-1 10:32:59)
   * @param bo
   *          ButtonObject
   */
  public void onButtonClicked(nc.ui.pub.ButtonObject btn) {
    // ���ο������֧�� by zhaoyha at 2009.3.2
    try{
      getInvokeEventProxy().beforeButtonClicked(btn);
    }catch(Exception e){
      showErrorMessage(e.getMessage());
      return;
    }
    if (getCurPanelMode() == INV_PANEL_CARD) {
      onButtonClickedBill(btn);
    }
    else if (getCurPanelMode() == INV_PANEL_LIST) {
      onButtonClickedList(btn);
    }

    setButtonsAndPanelState();

    updateButtons();

    // �ù�굽��ͷ��һ���ɱ༭��Ŀ
    if (m_isMakedBill)
      getBillCardPanel().transferFocusTo(BillCardPanel.HEAD);

    // ���ο������֧�� by zhaoyha at 2009.3.2
    try{
      getInvokeEventProxy().afterButtonClicked(btn);
    }catch(Exception e){
      showErrorMessage(e.getMessage());
      return;
    }
  }

  /**
   * VMI���Ļ��ܴ���VO����
   * 
   * @version (00-6-1 10:32:59)
   * @param bo
   *          ButtonObject
   */
  private InvoiceVO[] processAfterChange(String busiType, AggregatedValueObject[] arySourceVOs) {
    // ���зֵ�����һЩ��������
    InvoiceVO[] retVOs = getDistributedICVMISumVOs(arySourceVOs);
    return retVOs;
  }

  /**
   * ���ߣ���ӡ�� ���ܣ��Բɹ��������зֵ���Ĭ�ϰ���ҵ�����͡���Ӧ�̡����֡������֯ ������ ���أ��� ���⣺�� ���ڣ�(2001-10-13
   * 11:39:21) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־�� 2002-09-19 wyf ����Ĭ�ϰ������֯�ֵ�
   */
  private InvoiceVO[] getDistributedICVMISumVOs(AggregatedValueObject[] arySourceVOs) {
    
    // ���ֵ���ʽ�õ����з�Ʊ
    Hashtable ordTable = new Hashtable();
    //�Ƿ���Ҫ���òɹ�Ĭ�ϸ���λ
    boolean isTransPUAssUnit = false;
    SysInitVO para = null;
    try{
      para = nc.ui.pub.para.SysInitBO_Client.queryByParaCode(getCorpId(),"PO08");
    }catch(BusinessException e){
      SCMEnv.error(e);
    }
    if (para != null || para.getValue().equals("��")){
      isTransPUAssUnit = true;
    }
    int SourceVOsLength = arySourceVOs.length;
    nc.vo.ic.pub.vmi.VmiSumHeaderVO headVO = null;
    for (int i = 0; i < SourceVOsLength; i++) {

      // �õ����ڵڼ��Ŷ���
      headVO = (VmiSumHeaderVO) arySourceVOs[i].getParentVO();
      // ��Ӧ����VMI���Ļ������ɷ�Ʊ��Ĭ�Ϸֵ���ʽ
      String curKey = ((headVO.getCvendorid() == null || headVO.getCvendorid().trim().equals("")) ? "NULL" : headVO
          .getCvendorid());
      // �ֵ���ʽ
      String isplitmode = ((headVO.getAttributeValue("isplitmode") == null || headVO.getAttributeValue("isplitmode")
          .toString().trim().equals("")) ? curKey : headVO.getAttributeValue("isplitmode").toString().trim());

      // ���ݷֵ���ʽ�õ�KEY����
      if (isplitmode.trim().equals("1")) { // ��Ӧ��+���
        curKey += headVO.getCinventoryid();
      }
      else if (isplitmode.trim().equals("2")) { // ���Ļ��ܼ�¼
        curKey = headVO.getPrimaryKey();
      }
      // ���뵽HASH����
      if (!ordTable.containsKey(curKey)) {
        Vector vec = new Vector();
        vec.addElement(headVO);
        ordTable.put(curKey, vec);
      }
      else {
        Vector vec = (Vector) ordTable.get(curKey);
        vec.addElement(headVO);
      }

    }

    // �ӹ�ϣ����ȡ������VO
    InvoiceVO[] allVOs = null;
    InvoiceHeaderVO invoiceHeadVO = null;
    InvoiceItemVO[] items = null;
    if (ordTable.size() > 0) {
      allVOs = new InvoiceVO[ordTable.size()];
      Enumeration elems = ordTable.keys();
      int i = 0;
      ClientLink cl = new ClientLink(nc.ui.pub.ClientEnvironment.getInstance());// ������Ϣ
      while (elems.hasMoreElements()) {
        Object curKey = elems.nextElement();
        Vector vec = (Vector) ordTable.get(curKey);

        VmiSumHeaderVO[] headVOs = new VmiSumHeaderVO[vec.size()];
        vec.copyInto(headVOs);

        int headVOsLength = headVOs.length;
        // �γɷ�Ʊ��ͷ
        invoiceHeadVO = new InvoiceHeaderVO();
        // ��Ʊ����
        invoiceHeadVO.setAttributeValue("dinvoicedate", cl.getLogonDate());
        // ��˰���
        invoiceHeadVO.setIdiscounttaxtype(new Integer(1));

        // Ʊ������
        invoiceHeadVO.setAttributeValue("darrivedate", cl.getLogonDate());
        // ��˾
        invoiceHeadVO.setAttributeValue("pk_corp", ((headVOs[0].getAttributeValue("pk_corp") == null || headVOs[0]
            .getAttributeValue("pk_corp").toString().trim().equals("")) ? "" : headVOs[0].getAttributeValue("pk_corp")
            .toString().trim()));
        // �ڳ���־(��Ʊ��־)
        invoiceHeadVO.setAttributeValue("finitfalg", new Integer(0));
        // ҵ������
        invoiceHeadVO.setAttributeValue("cbiztype", getCurBizeType());
        // ��Ʊ����(����ר��(0))
        invoiceHeadVO.setAttributeValue("iinvoicetype", new Integer(0));
        // �����֯
        invoiceHeadVO.setAttributeValue("cstoreorganization", headVOs[0].getCcalbodyid());
        // ��Ӧ��(������)
        invoiceHeadVO.setAttributeValue("cvendormangid", headVOs[0].getCvendorid());
        // ��Ӧ��(��������)
        invoiceHeadVO.setAttributeValue("cvendorbaseid", headVOs[0].getAttributeValue("cvendorbasid"));
        // ���λ
        invoiceHeadVO.setAttributeValue("cpayunit", headVOs[0].getCvendorid());
        // ���ÿ��������������ʺŵ�ֵ
        setDefaultBankAccountForAVendor(invoiceHeadVO.getCvendorbaseid(), invoiceHeadVO);
        // ����Э��
        String payTermId = (String) PiPqPublicUIClass.getAResultFromTable("bd_cumandoc", "pk_payterm", "pk_cumandoc",
            invoiceHeadVO.getCvendormangid());
        invoiceHeadVO.setAttributeValue("ctermprotocolid", payTermId);
        // ҵ��Ա
        String cemployeeid = (String) PiPqPublicUIClass.getAResultFromTable("bd_cumandoc", "pk_resppsn1",
            "pk_cumandoc", invoiceHeadVO.getCvendormangid());
        invoiceHeadVO.setAttributeValue("cemployeeid", cemployeeid);
        // ����
        String cdeptid = (String) PiPqPublicUIClass.getAResultFromTable("bd_cumandoc", "pk_respdept1", "pk_cumandoc",
            invoiceHeadVO.getCvendormangid());
        invoiceHeadVO.setAttributeValue("cdeptid", cdeptid);
        // ����
        String ccurrencytypeid = (String) PiPqPublicUIClass.getAResultFromTable("bd_cumandoc", "pk_currtype1",
            "pk_cumandoc", invoiceHeadVO.getCvendormangid());
        if (ccurrencytypeid == null || ccurrencytypeid.trim().length() == 0) 
          ccurrencytypeid=PiPqPublicUIClass.getNativeCurrencyID();// ���Ļ���ֻ֧�ֱ�λ�ң�
        invoiceHeadVO.setAttributeValue("ccurrencytypeid",ccurrencytypeid); 
        
        // �Ƶ���
        invoiceHeadVO.setAttributeValue("coperator", cl.getUser());
        // ����״̬
        invoiceHeadVO.setIbillstatus(new Integer(0));
        // �γɷ�Ʊ����
        items = new InvoiceItemVO[headVOsLength];
        for (int k = 0; k < headVOsLength; k++) {
          items[k] = new InvoiceItemVO();
        }
        // ���ڳ���

        // ���ڳ����˻�

        // �ۼƿ�Ʊ����

        for (int j = 0; j < headVOsLength; j++) {

          // ���(������)
          items[j].setCbaseid(((headVOs[j].getAttributeValue("cinvbasdocid") == null || headVOs[j].getAttributeValue(
              "cinvbasdocid").toString().trim().equals("")) ? "" : headVOs[j].getAttributeValue("cinvbasdocid")
              .toString().trim()));
          // ���(��������)
          items[j].setCmangid(((headVOs[j].getAttributeValue("cinventoryid") == null || headVOs[j].getAttributeValue(
              "cinventoryid").toString().trim().equals("")) ? "" : headVOs[j].getAttributeValue("cinventoryid")
              .toString().trim()));
          // Ĭ�ϲɹ���Ʊ����
          items[j].setNinvoicenum(((headVOs[j].getAttributeValue("ninvoicenum") == null || headVOs[j]
              .getAttributeValue("ninvoicenum").toString().trim().equals("")) ? new UFDouble(0) : new UFDouble(
              headVOs[j].getAttributeValue("ninvoicenum").toString().trim())));
          // ����
          items[j].setCcurrencytypeid(ccurrencytypeid);
          // �۱��۸�����
          UFDouble[] daRate = null;
          String strCurrDate = invoiceHeadVO.getAttributeValue("dinvoicedate").toString();
          if (strCurrDate == null || strCurrDate.trim().length() == 0) {
            strCurrDate = PoPublicUIClass.getLoginDate() + "";
          }
          daRate = m_cardPoPubSetUI2.getBothExchRateValue(getPk_corp(), ccurrencytypeid, new UFDate(strCurrDate));
          items[j].setNexchangeotobrate(daRate[0]);
          // items[j].setNexchangeotoarate(daRate[1]);

          // ��Դ��������
          items[j].setCsourcebilltype("50");
          // ��Դ����ID
          items[j].setCsourcebillid(headVOs[j].getPrimaryKey());
          // ��Դ������ID
          items[j].setCsourcebillrowid(null);
          // �ϲ���Դ��������
          items[j].setCupsourcebilltype("50");
          // �ϲ���Դ����ID
          items[j].setCupsourcebillid(headVOs[j].getPrimaryKey());
          // �ϲ���Դ������ID
          items[j].setCupsourcebillrowid(null);

          items[j].setCupsourcehts(headVOs[j].getTs());
          items[j].setCupsourcebts(headVOs[j].getTs());
          items[j].setCwarehouseid(headVOs[j].getCwarehouseid());
          //
          items[j].setVfree1(headVOs[j].getVfree1());
          items[j].setVfree2(headVOs[j].getVfree2());
          items[j].setVfree3(headVOs[j].getVfree3());
          items[j].setVfree4(headVOs[j].getVfree4());
          items[j].setVfree5(headVOs[j].getVfree5());
          //���κ�
          items[j].setVproducenum(headVOs[j].getVlot());
        }

        allVOs[i] = new InvoiceVO();
        allVOs[i].setParentVO(invoiceHeadVO);
        allVOs[i].setChildrenVO(items);
        allVOs[i].setSource(InvoiceVO.FROM_VMI);
        // �����к�����
        BillRowNo.setVORowNoByRule(allVOs[i], ScmConst.PO_Invoice, "crowno");
        // ȡ�ƻ���
        String sCstoreorganization = allVOs[i].getHeadVO().getCstoreorganization();
        int size = allVOs[i].getChildrenVO().length;
        String[] sMangIds = new String[size];
        for (int p = 0; p < size; p++) {
          sMangIds[p] = items[p].getCmangid();
        }
        // ����ת������Ʊ���������Ĭ��˰��
        UFDouble[] uPrice = queryPlanPrices(sMangIds, sCstoreorganization);
        if (uPrice != null) {
          for (int p = 0; p < size; p++) {
            items[p].setNplanprice(uPrice[p]);
          }
        }
        i++;
      }
    }
    //����Ĭ�ϸ�������λ
    if(isTransPUAssUnit ){
      for(int i  = 0 ; i < allVOs.length ; i ++){
        for(int j  = 0 ; j < allVOs[i].getBodyLen() ; j++){
          if(PuTool.isAssUnitManaged(allVOs[i].getBodyVO()[j].getCbaseid())){
            String cassistunit = PuTool.getDefaultPUAssUnit(allVOs[i].getBodyVO()[j].getCbaseid());
            if(PuPubVO.getString_TrimZeroLenAsNull(cassistunit) != null
                && PuPubVO.getUFDouble_ZeroAsNull(allVOs[i].getBodyVO()[j].getNinvoicenum()) != null){
              allVOs[i].getBodyVO()[j].setCassistunit(cassistunit);
              UFDouble ufdConv = PuTool.getInvConvRateValue(allVOs[i].getBodyVO()[j].getCbaseid(), cassistunit);
              allVOs[i].getBodyVO()[j].setNexchangerate(ufdConv);
              allVOs[i].getBodyVO()[j].setNassistnum(allVOs[i].getBodyVO()[j].getNinvoicenum().div(ufdConv));
            }
          }
        }
      }
    }
    return allVOs;
  }

  /**
   * �ṩ��ʽ,�Ӹù�ʽ�õ���ѯ��� �ο�BillModel.execloadFormula(int)д��
   * 
   * @param ����˵��
   * @return ����ֵ
   * @exception �쳣����
   * @see ��Ҫ�μ�����������
   * @since �������һ���汾���˷�������ӽ���
   * @return java.lang.String
   */
  private void setDefaultBankAccountForAVendor(String strVendorBase, InvoiceHeaderVO invoiceHeadVO) {

    if (strVendorBase == null || strVendorBase.trim().equals("")) {
      return;
    }

    Object[][] retOb = CacheTool.getMultiColValue("bd_custbank", "pk_cubasdoc", new String[] {
        "pk_custbank", "account", "defflag"
    }, new String[] {
      strVendorBase
    });
    Vector v1 = new Vector(), v2 = new Vector();
    if (retOb != null && retOb.length > 0 && retOb[0] != null && retOb[0].length > 1) {
      for (int i = 0; i < retOb.length; i++) {
        if (retOb[i][2] != null && retOb[i][2].equals("Y")) {
          v1.add(retOb[i][0]);
          v2.add(retOb[i][1]);
        }
      }
    }

    // added by fangy 2002-10-22 18:34 begin
    //UIRefPane pane = (UIRefPane) getBillCardPanel().getHeadItem("caccountbankid").getComponent();
    //((AccountsForVendorRefModel) pane.getRef().getRefModel()).setCvendorbaseid(strVendorBase);
    // added by fangy 2002-10-22 18:34 begin

    if (v1.size() == 0) {
      // �������в���
      // UIRefPane pane =
      // (UIRefPane)getInvBillPanel().getHeadItem("caccountbankid").getComponent()
      // ;
      // ((AccountsForVendorRefModel)pane.getRef().getRefModel()).setCvendorbaseid(null)
      // ;
      // ���ò�������:��������
      invoiceHeadVO.setAttributeValue("caccountbankid", null);
      // �ʺ�
      invoiceHeadVO.setAttributeValue("cvendoraccount", null);
    }
    else {
      // �������в���
      // UIRefPane pane =
      // (UIRefPane)getInvBillPanel().getHeadItem("caccountbankid").getComponent()
      // ;
      // ((AccountsForVendorRefModel)pane.getRef().getRefModel()).setCvendorbaseid(strVendorBase)
      // ;
      // ���ò�������:��������
      invoiceHeadVO.setAttributeValue("caccountbankid", (String) v1.elementAt(0));
      // �ʺ�
      invoiceHeadVO.setAttributeValue("cvendoraccount", (String) v1.elementAt(0));
    }

  }

  /**
   * ����ʵ�ָ÷�������Ӧ��ť�¼���
   * 
   * @version (00-6-1 10:32:59)
   * @param btn
   *          ButtonObject �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־�� 2003-02-20 ����,ί�ⶩ����ͷ�����޷�����.
   */
  private void onButtonClickedBill(nc.ui.pub.ButtonObject btn) {
    if (btn == m_btnReSortRowNo) {
      onReSortRowNo();
    }
    else if (btn == m_btnCardEdit) {
      onCardEdit();
    }
    else if (btn == m_btnInvBillPasteRowTail) {
      // ճ����
      showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "SCMCOMMON000000266"));
      onPasteLineToTail();
      showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "SCMCOMMON000000266"));
    }
    // ================ҵ������
    else if (btn.getParent() == m_btnInvBillBusiType) {
      showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPPSCMCommon-000293")/*
                                                                                                     * @res
                                                                                                     * "ѡ��ҵ������"
                                                                                                     */);

      setCurOperState(STATE_BROWSE_NORMAL);

      // ��ǰҵ������
      // setOldBizeType(getCurBizeType());
      setCurBizeType(btn.getTag());
      m_strBtnTag = btn.getTag();
      // ����״̬����ʾ
      StringBuffer sbufHint = new StringBuffer("");
      setHeadHintText(sbufHint.toString());

      // ҵ�����͵�����Դ��������
      PfUtilClient.retAddBtn(m_btnInvBillNew, nc.ui.pub.ClientEnvironment.getInstance().getCorporation()
          .getPrimaryKey(), nc.vo.scm.pu.BillTypeConst.PO_INVOICE, btn);
      btn.setSelected(true);
      m_btnInvBillBusiType.setEnabled(true);
      //
      setButtons(m_btnTree.getButtonArray());
  //        setButtons(getBtnss());
      m_bizButton = btn;
      updateButtons();
    }
    else if (btn.getParent() == m_btnInvBillNew || btn.getParent() == btnBillAddContinue) {
      showHintMessage(getHeadHintText() + nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UCH028")/*
                                                                                                           * @res
                                                                                                           * "���ӷ�Ʊ"
                                                                                                           */);

      //���Ƶ���
      if (btn.getTag() != null && btn.getTag().indexOf("makeflag") >= 0) {
        setCurOperState(STATE_EDIT);
        // ��ǰҵ������
        if(m_strBtnTag!=null) setCurBizeType(m_strBtnTag);
        onAdd();

        m_isMakedBill = true;

        // �ɹ���Ʊ����ʱ���ɹ���˾Ĭ��Ϊ��ǰ��¼��˾
        getBillCardPanel().getHeadItem("pk_purcorp").setValue(getPk_corp());
      }
      else {
        // ��ȡ��Դ��������
        String tag = btn.getTag();
        int index = tag.indexOf(":");
        String strUpBillType = tag.substring(0, index);// ��Դ��������
        // �����Դ���������ǣ���50��(�������Ļ��ܼ�¼���ɲɹ���Ʊʱ)���������´���
        if ("50".equalsIgnoreCase(strUpBillType)) {
          PfUtilClient.childButtonClicked(btn, getCorpPrimaryKey(), "400404", nc.ui.pub.ClientEnvironment.getInstance()
              .getUser().getPrimaryKey(), ScmConst.PO_Invoice, this);
        }
      

        // if(m_bizButton!=null&&m_bizButton.getName()!=null&&m_bizButton.getName().equals("")){
        else {
        	  if ("D1".equalsIgnoreCase(strUpBillType)||"F1".equalsIgnoreCase(strUpBillType)){
                  feeFlag = true;  //���÷�Ʊ��־ add by QuSida (��ɽ����) 2010-9-26
              }
          PfUtilClient.childButtonClicked(btn, nc.ui.pub.ClientEnvironment.getInstance().getCorporation().getPk_corp(),
              getModuleCode(), nc.ui.pub.ClientEnvironment.getInstance().getUser().getPrimaryKey(),
              nc.vo.scm.pu.BillTypeConst.PO_INVOICE, this);
        }
        if (PfUtilClient.isCloseOK()) {
          InvoiceVO[] vos = null;
          if ("50".equalsIgnoreCase(strUpBillType)) {
            AggregatedValueObject[] arySourceVOs = PfUtilClient.getRetVos();
            vos = processAfterChange("50", arySourceVOs);// ����VO����
            for (int i = 0; i < vos.length; i++) {
              if (PuPubVO.getString_TrimZeroLenAsNull(vos[i].getHeadVO().getPk_purcorp()) == null) {// �ɹ���˾Ϊ����Ĭ��Ϊ��ǰ��¼��˾
                vos[i].getHeadVO().setPk_purcorp(getPk_corp());
              }
            }
          }
          else {
            vos = (InvoiceVO[]) PfUtilClient.getRetVos();
            if (vos == null) {
              MessageDialog.showHintDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401",
                  "UPPSCMCommon-000270")/* @res "��ʾ" */, nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401",
                  "UPP40040401-000003")/* @res "����ת��ʧ�ܣ�" */);
              return;
            }
            else {
              BillItem item = getBillListPanel().getBodyBillModel().getItemByKey("nplanprice");
              for (int i = 0; i < vos.length; i++) {

                if (PuPubVO.getString_TrimZeroLenAsNull(vos[i].getHeadVO().getPk_purcorp()) == null) {// �ɹ���˾Ϊ����Ĭ��Ϊ��ǰ��¼��˾
                  vos[i].getHeadVO().setPk_purcorp(getPk_corp());
                }
                // ���±��ֺ��к�����
                BillRowNo.setVORowNoByRule(vos[i], ScmConst.PO_Invoice, "crowno");
                setHeadCurrency(vos[i]);
                try {
                  InvoiceItemVO[] items = vos[i].getBodyVO();
                  // ���㱾������
                  computeValueFrmOtherBill(vos[i]);

                  // ��Ӧ�̹���ID
                  String sCvendormangid = vos[i].getHeadVO().getCvendormangid();
                  // �ո���Э��
                  String payTermId = vos[i].getHeadVO().getCtermprotocolid();
                  if (payTermId == null || payTermId.trim().length() < 1) {
                    Object oTemp = CacheTool.getCellValue("bd_cumandoc", "pk_cumandoc", "pk_payterm", sCvendormangid);
                    if (oTemp != null)
                      vos[i].getHeadVO().setCtermprotocolid(oTemp.toString());
                  }
                  // �ո��λ
                  String sPayUnit = vos[i].getHeadVO().getCpayunit();
                  // ��ԭ������Ŀ���⣺�޸ķ�Ʊ�ϵĹ�Ӧ��ΪB��λ������ǩ�ֺ�����Ӧ�����ϵĹ�Ӧ�̻�����A��λ
                  // if (sPayUnit == null || sPayUnit.trim().length() == 0) {
                  vos[i].getHeadVO().setCpayunit(sCvendormangid);
                  // }
                  // ȡ�ƻ���
                  if (item != null && (item.isShow())) {
                    String sCstoreorganization = vos[i].getHeadVO().getCstoreorganization();
                    int size = items.length;
                    String[] sMangIds = new String[size];
                    for (int j = 0; j < size; j++) {
                      sMangIds[j] = items[j].getCmangid();
                    }
                    UFDouble[] uPrice = queryPlanPrices(sMangIds, sCstoreorganization);
                    if (uPrice != null) {
                      for (int j = 0; j < size; j++) {
                        items[j].setNplanprice(uPrice[j]);
                      }
                    }
                  }
                }
                catch (Exception ex) {
                  reportException(ex);
                  PuTool.outException(this, ex);
                }
              }
              if (BillTypeConst.PO_ORDER.equalsIgnoreCase(strUpBillType)
                  || BillTypeConst.STORE_PO.equalsIgnoreCase(strUpBillType)) {
                resetStoreorgAndStordoc(vos, strUpBillType);
              }

            }
          }

          //setCurVOPos(0);
          sContinueBillTypeName = btn.getName();
          if (btn.getParent() == m_btnInvBillNew) {
            setInvVOs(vos);
          }
          else if (btn.getParent() == btnBillAddContinue) {

            AggregatedValueObject[] arySourceVOs = SourceRefDlg.getRetsVos();

            // ׷�ӵı�������
            Vector<InvoiceItemVO> continueItemVO = new Vector<InvoiceItemVO>();
            if (arySourceVOs != null && arySourceVOs.length > 0) {

              InvoiceHeaderVO voCur = getCurVOonCard().getHeadVO();// ��ǰ��Ƭ�ϵı�ͷ����
              InvoiceHeaderVO newvo = null;
              try {
                newvo = (InvoiceHeaderVO) ObjectUtils.serializableClone(voCur);
              }
              catch (Exception e) {
                SCMEnv.out(e);
              }

              BillItem[] headItems = getBillCardPanel().getHeadShowItems();// ������Ŀ��itemkey

              String[] headNotNullKeys = null;// �ǿյ���ͷItemKey
              Vector<String> v1 = new Vector<String>();
              for (int i = 0; headItems != null && i < headItems.length; i++) {
                if (headItems[i].isNull()) {
                  v1.add(headItems[i].getKey());
                }
              }
              if (v1.size() > 0) {
                headNotNullKeys = new String[v1.size()];
                v1.copyInto(headNotNullKeys);
              }

              for (int i = 0; i < arySourceVOs.length; i++) {
                boolean bRight = true;
                for (int j = 0; j < headNotNullKeys.length; j++) {
                  if (arySourceVOs[i].getParentVO().getAttributeValue(headNotNullKeys[j]) != null
                      && !(arySourceVOs[i].getParentVO().getAttributeValue(headNotNullKeys[j]) instanceof UFDate)
                      && !arySourceVOs[i].getParentVO().getAttributeValue(headNotNullKeys[j]).equals(
                          newvo.getAttributeValue(headNotNullKeys[j]))) {
                    bRight = false;
                    break;// �±�ͷ��ԭ��ͷ�����һ�����ܲ�������
                  }
                }
                if (bRight) {
                  InvoiceVO invoiceVO = (InvoiceVO) arySourceVOs[i];
                  if (getInvVOs() != null && getInvVOs()[getCurVOPos()] != null) {
                    InvoiceItemVO[] items = (InvoiceItemVO[]) getInvVOs()[getCurVOPos()].getChildrenVO();
                    for (int j = 0; j < items.length; j++) {// ������ԭ������
                      continueItemVO.add(items[j]);
                    }
                    for (int jj = 0; jj < invoiceVO.getChildrenVO().length; jj++) {// ��׷�ӵ���
                      ((InvoiceItemVO[]) invoiceVO.getChildrenVO())[jj].setCrowno(null);
                      continueItemVO.add(((InvoiceItemVO[]) invoiceVO.getChildrenVO())[jj]);
                    }
                  }
                }
              }

            }
            if (continueItemVO == null || continueItemVO.size() < 1) {
              return;
            }
            getInvVOs()[getCurVOPos()].setChildrenVO((InvoiceItemVO[]) continueItemVO
                .toArray(new InvoiceItemVO[continueItemVO.size()]));
            // ���±��ֺ��к�����
            BillRowNo.setVORowNoByRule(getInvVOs()[getCurVOPos()], ScmConst.PO_Invoice, "crowno");
          }
          // ��ʾ��Ʊ
          setListOperState(STATE_LIST_FROM_BILLS);
          // setBillBrowseState(STATE_BROWSE_FROM_BILL) ;
          setCurOperState(getListOperState());
          shiftShowModeTo(INV_PANEL_LIST);
          // �����б��������� 
          //by zhaoyha at 2009.6.16�����޸ģ��������ʱ��ת���б�ֻ��һ��ʱֱ�ӵ���Ƭ
          if (vos != null && vos.length > 1) {
            setVOsToListPanel();
          }

          m_vSavedVO = new Vector();

          if (vos != null && vos.length == 1) {
            onListBill();
          }
//          if("D1".equals(strUpBillType)){
//        	  getBillCardPanel().getHeadItem("nexchangeotobrate").setEnabled(true);
//          }
        }
      }
    }
    // �޸�
    else if (btn == m_btnInvBillModify) {// ��Ƭ���޸�
      showHintMessage(getHeadHintText() + nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UCH027")/*
                                                                                                           * @res
                                                                                                           * "�޸ķ�Ʊ"
                                                                                                           */);
      setCurOperState(STATE_EDIT);
      onModify();
      // //����
      m_btnInvBillAudit.setEnabled(false);
      updateButtons();

      // �ù�굽��ͷ��һ���ɱ༭��Ŀ
      getBillCardPanel().transferFocusTo(BillCardPanel.HEAD);
    }
    // ����
    else if (btn == m_btnInvBillSave) {
      // showHintMessage(getHeadHintText() +
      // nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UCH044")/*@res
      // "���淢Ʊ"*/);

      boolean bSucceed = onSave();

      if (bSucceed) {
        showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UCH005")/*
                                                                                         * res@
                                                                                         * "����ɹ�"
                                                                                         */);
      }
      else {
        showHintMessage("");
      }
    }
    // ����
    else if (btn == m_btnInvBillDiscard) {
      showHintMessage(getHeadHintText() + nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UCH051")/*
                                                                                                           * @res
                                                                                                           * "���Ϸ�Ʊ"
                                                                                                           */);

      onDiscard();
    }
    // ����
    else if (btn == m_btnInvBillCancel) {
      showHintMessage(getHeadHintText() + nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UCH045")/*
                                                                                                           * @res
                                                                                                           * "ȡ����Ʊ����"
                                                                                                           */);

      onBillCancel();
      showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UCH008")/*
                                                                                       * res@
                                                                                       * "ȡ���ɹ�"
                                                                                       */);
    }
    // ================�в���
    else if (btn == m_btnInvBillAddRow) {
      // ����
      showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UCH056"));
      onAppendLine();
      showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UCH036"));
    }
    else if (btn == m_btnInvBillDeleteRow) {
      // ɾ��
      showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UCH057"));
      onDeleteLine();
      showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UCH037"));
    }
    else if (btn == m_btnInvBillInsertRow) {
      // ����
      showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UCH058"));
      onInsertLine();
      showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UCH038"));
    }
    else if (btn == m_btnInvBillCopyRow) {
      // ������
      showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UCH059"));
      onCopyLine();
      showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UCH039"));
    }
    else if (btn == m_btnInvBillPasteRow) {
      // ճ����
      showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UCH060"));
      onPasteLine();
      showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UCH040"));
    }
    // ================ҳ����
    else if (btn == m_btnInvBillGoFirstOne) {
      // ����
      showHintMessage(getHeadHintText() + nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UCH031")/*
                                                                                                           * @res
                                                                                                           * "�����Ʊ"
                                                                                                           */);
      setCurOperState(getBillBrowseState());
      onFirst();
    }
    else if (btn == m_btnInvBillGoLastOne) {
      // ĩ��
      showHintMessage(getHeadHintText() + nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UCH032")/*
                                                                                                           * @res
                                                                                                           * "�����Ʊ"
                                                                                                           */);
      setCurOperState(getBillBrowseState());
      onLast();
    }
    else if (btn == m_btnInvBillGoPreviousOne) {
      // ����
      showHintMessage(getHeadHintText() + nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UCH033")/*
                                                                                                           * @res
                                                                                                           * "�����Ʊ"
                                                                                                           */);
      setCurOperState(getBillBrowseState());
      onPrevious();
    }
    else if (btn == m_btnInvBillGoNextOne) {
      // ����
      showHintMessage(getHeadHintText() + nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UCH034")/*
                                                                                                           * @res
                                                                                                           * "�����Ʊ"
                                                                                                           */);

      setCurOperState(getBillBrowseState());
      onNext();
    }
    // �б�
    else if (btn == m_btnInvShift) {
      showHintMessage(getHeadHintText() + nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UCH055")/*
                                                                                                           * @res
                                                                                                           * "��Ʊ�б�״̬"
                                                                                                           */);

      setCurOperState(getListOperState());
      onList();
    }
    // ��ѯ
    else if (btn == m_btnInvBillQuery) {
      showHintMessage(getHeadHintText() + nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UCH046")/*
                                                                                                           * @res
                                                                                                           * "��ѯ��Ʊ"
                                                                                                           */);

      setCurOperState(STATE_BROWSE_NORMAL);
      onBillQuery();
      showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UCH009"));
    }
    // ��Ƭ�¸���
    else if (btn == m_btnInvBillCopy) {
      showHintMessage(getHeadHintText() + nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPP40040401-000035")/*
                                                                                                                         * @res
                                                                                                                         * "���Ʒ�Ʊ"
                                                                                                                         */);

      setCurOperState(STATE_EDIT);
      onCopy();
    }
    // Ԥ��
    else if (btn == m_btnInvBillPreview) {
      showHintMessage(getHeadHintText() + nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UCH061")/*
                                                                                                           * @res
                                                                                                           * "��ӡ��Ʊ"
                                                                                                           */);
      setCurOperState(getBillBrowseState());
      // ��ӡԤ���������ӡ����
      onCardPrintPreview();
      // PuTool.onPreview(getPrintEntry(), new
      // nc.ui.rc.pub.PurchasePrintDS(getModuleCode(), getInvBillPanel()));
      showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UCH041"));
    }
    // ��ӡ
    else if (btn == m_btnInvBillPrint) {
      showHintMessage(getHeadHintText() + nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPP40040401-000036")/*
                                                                                                                         * @res
                                                                                                                         * "��ӡ��Ʊ"
                                                                                                                         */);
      setCurOperState(getBillBrowseState());
      // ��ӡ�������ӡ����
      onCardPrint();
      // PuTool.onPrint(getPrintEntry(), new
      // nc.ui.rc.pub.PurchasePrintDS(getModuleCode(), getInvBillPanel()));
      showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UCH041"));
    }

    // ˢ��
    else if (btn == m_btnInvBillRefresh) {
      showHintMessage(getHeadHintText() + nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UCH046")/*
                                                                                                           * @res
                                                                                                           * "��ѯ��Ʊ"
                                                                                                           */);

      setCurOperState(STATE_BROWSE_NORMAL);
      onBillRefresh();
      showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UCH007"));
    }
    else if (btn == m_btnLnkQuery) {// ��Ƭ������
      // showHintMessage(getHeadHintText() + CommonConstant.SPACE_MARK +"��ѯ��Ʊ" +
      // CommonConstant.SPACE_MARK ) ;
      int preState=m_nCurOperState;
      onLnkQuery();
      setCurOperState(preState);
    }
    else if (btn == m_btnInvBillAudit) {
      // ��Ƭ������
      showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UCH048"));
      onBillAudit();
      setCurOperState(STATE_BROWSE_NORMAL);
    }
    else if (btn == m_btnInvBillUnAudit) {
      // ��Ƭ������
      showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UCH049"));
      onBillUnAudit();
    }
    else if (btn == m_btnCrtAPBill) {
      onCrtAPBill();
    }
    else if (btn == m_btnDelAPBill) {
      onDelAPBill();
    }
    else if (btn == m_btnDocManage) {
      showHintMessage(getHeadHintText()
          + nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPPSCMCommon-000278")/*
                                                                                           * @res
                                                                                           * "�ĵ�����"
                                                                                           */);
      // �ĵ�����
      onDocManage();

    }
    else if (btn == m_btnHqhp) {
      // �����ż�ȡ��
      onHqhp();
    }

    // ����
    else if (btn == m_btnSendAudit) {
      showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UCH047"));
      boolean bContinue = false;
      if (getInvVOs() != null && getInvVOs()[getCurVOPos()] != null) {
        if (m_bAdd) {
          bContinue = onSendAudit(null);
        }
        else {
          bContinue = onSendAudit(getInvVOs()[getCurVOPos()]);
        }
      }
      else {
        bContinue = onSendAudit(null);
      }
      if (!bContinue) {
        return;
      }

      if (getInvVOs() != null && getInvVOs()[getCurVOPos()] != null) {
        // setButtonsAndPanelState() ;
      }
      else if (m_bAdd) {
        boolean isNeedSendToAuditQ = BusiBillManageTool.isNeedSendToAudit(ScmConst.PO_Invoice, getPk_corp(),
            getCurBizeType(), null, getCurOperator());

        Integer iBillStatus = new Integer(88);
        if (getInvVOs() != null) {
          InvoiceVO vo = getInvVOs()[getCurVOPos()];

          if (vo != null && vo.getHeadVO() != null) {
            iBillStatus = vo.getHeadVO().getIbillstatus();
          }
        }

        if (isNeedSendToAuditQ
            && (iBillStatus.compareTo(nc.vo.scm.pu.BillStatus.FREE) == 0 || (iBillStatus
                .compareTo(nc.vo.scm.pu.BillStatus.AUDITFAIL) == 0))) {
          m_btnSendAudit.setEnabled(true);
        }
      }
      try {
        // InvoiceVO resultVO =
        // InvoiceHelper.findByPrimaryKey(getInvVOs()[getCurVOPos()].getHeadVO().getPrimaryKey());
        // getInvVOs()[getCurVOPos()] = resultVO;
      }
      catch (Exception e) {
        SCMEnv.out(e);
      }

      setVOToBillPanel();
      if (getCurOperState() != STATE_LIST_FROM_BILLS)
        setCurOperState(STATE_BROWSE_NORMAL);
      showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UCH023"));
    }
    else if (btn == btnBillCombin)// ��Ƭ�ºϲ���ʾ
      onBillCombin();
    else if (btn == m_btnAudit) {
      showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UCH048"));
      onAudit();
    }
    else if (btn == m_btnQueryForAudit)
      onQueryForAudit();
    else if (btn == m_btnUnAudit) {
      showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UCH049"));
      onUnAudit();
    }
    else if (PuTool.isExist(getExtendBtns(), btn)) {
      onExtendBtnsClick(btn);
    }
    else if (btn == m_btnInvBillConversion) {
      showHintMessage(getHeadHintText() + nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPP40040401-000039")/*
                                                                                                                         * @res
                                                                                                                         * "����ת��"
                                                                                                                         */);
      // ����ת��
      onGiveupBillConversion();
    }
    

  }

  /*
   * ��üƻ���
   */
  private UFDouble[] queryPlanPrices(String cMangID[], String pk_calbody) {
    if (cMangID == null || cMangID.length == 0)
      return null;
    UFDouble nPrice[] = new UFDouble[cMangID.length];

    if (pk_calbody != null && pk_calbody.trim().length() > 0) {
      // ����������������ȡ�ƻ���
      String whereInPart=new TempTableUtil().getSubSql(cMangID); 
      /** ***************�޸��������������Ĳ�ѯ��ʽ���˵������汻ȡ������Ҫ�Ӻ�̨��ѯ��***************** */
      Object[][] retObj = null;
      try {
//        retObj = PubHelper.queryResultsFromTableByWhere("bd_produce", new String[] {
//          "jhj","pk_invmandoc"
//        }, alWhere.toArray(new String[alWhere.size()]));
        retObj = PubHelper.queryResultsFromAnyTable("bd_produce",new String[] {
            "pk_invmandoc","jhj"
        }, "PK_CALBODY='" + pk_calbody + "' and PK_INVMANDOC in " + whereInPart);
      }
      catch (Exception be) {
        SCMEnv.out(be);
        showErrorMessage(be.getMessage());
      }
      //����ѯ�������ݽ���Map
      Map<String,UFDouble> planPrices=new HashMap<String,UFDouble>();
      if(retObj != null && retObj.length > 0){
        for(int i=0;i<retObj.length;++i){
          Object[] oneRow=(Object[])retObj[i];
          if(oneRow==null || oneRow[0]==null || oneRow[0].toString().length()==0) continue;
          planPrices.put(oneRow[0].toString(), PuPubVO.getUFDouble_NullAsZero(oneRow[1]));
        }
      }
      for(int i = 0; i < cMangID.length; i++)
        nPrice[i]=planPrices.get(cMangID[i]);
      /** ************************************************ */
    }
    else {
      // �Ӵ������������ƻ���
      try {
        Object o = CacheTool.getColumnValue("bd_invmandoc", "pk_invmandoc", "planprice", cMangID);
        if (o != null) {
          Object oTemp[] = (Object[]) o;
          for (int i = 0; i < cMangID.length; i++) {
            if (oTemp[i] != null && oTemp[i].toString().trim().length() > 0)
              nPrice[i] = new UFDouble(oTemp[i].toString());
          }
        }
      }
      catch (Exception e) {
        SCMEnv.out(e);
      }
    }

    return nPrice;
  }

  /**
   * ����ʵ�ָ÷�������Ӧ��ť�¼���
   * 
   * @version (00-6-1 10:32:59)
   * @param bo
   *          ButtonObject
   */
  private void onButtonClickedList(nc.ui.pub.ButtonObject btn) {

    // ��ѯ �޸� ���� ���� ���� ��ӡ ˢ�� ������
    // ////////�б�״̬
    if (btn == m_btnInvBillQuery) {
      showHintMessage(getHeadHintText() + nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UCH046")/*
                                                                                                           * @res
                                                                                                           * "��ѯ��Ʊ"
                                                                                                           */);

      // ��ѯ
      onListQuery();
      showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UCH009"));

    }
    else if (btn == m_btnInvSelectAll) {
      showHintMessage(getHeadHintText() + nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPP40040401-000032")/*
                                                                                                                         * @res
                                                                                                                         * "�����Ʊ"
                                                                                                                         */);

      // ȫѡ
      onSelectAll();
      showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "4004COMMON000000033")/*
                                                                                                   * @res
                                                                                                   * "ȫѡ�ɹ�"
                                                                                                   */);
    }
    else if (btn == m_btnInvDeselectAll) {
      showHintMessage(getHeadHintText() + nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPP40040401-000032")/*
                                                                                                                         * @res
                                                                                                                         * "�����Ʊ"
                                                                                                                         */);

      // ȫ��
      onSelectNo();
      showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "4004COMMON000000034")/*
                                                                                                   * @res
                                                                                                   * "ȫ���ɹ�"
                                                                                                   */);
    }
    else if (btn == m_btnInvBillDiscard) {
      showHintMessage(getHeadHintText() + nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UCH051")/*
                                                                                                           * @res
                                                                                                           * "�������Ϸ�Ʊ"
                                                                                                           */);

      // ����
      onListDiscard();
      showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UCH006"));

    }
    else if (btn == m_btnInvBillModify) {
      showHintMessage(getHeadHintText() + nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UCH027")/*
                                                                                                           * @res
                                                                                                           * "�޸ķ�Ʊ"
                                                                                                           */);

      // �޸�
      onListModify();
      // �ù�굽��ͷ��һ���ɱ༭��Ŀ
      getBillCardPanel().transferFocusTo(BillCardPanel.HEAD);

    }
    else if (btn == m_btnInvShift) {
      showHintMessage(getHeadHintText() + nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UCH055")/*
                                                                                                           * @res
                                                                                                           * "�л������ݽ���"
                                                                                                           */);
      // �л�
      onListBill();
    }
    else if (btn == m_btnInvBillRefresh) {
      showHintMessage(getHeadHintText() + nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UCH046")/*
                                                                                                           * @res
                                                                                                           * "��ѯ��Ʊ"
                                                                                                           */);

      // ˢ��
      onListRefresh();
      showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UCH007"));

    }
    else if (btn == m_btnInvBillConversion) {
      showHintMessage(getHeadHintText() + nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPP40040401-000039")/*
                                                                                                                         * @res
                                                                                                                         * "����ת��"
                                                                                                                         */);
      // ����ת��
      onGiveupBillConversion();
    }
    else if (btn == m_btnCrtAPBill) {
      onCrtAPBill();
    }
    else if (btn == m_btnDelAPBill) {
      onDelAPBill();
    }
    else if (btn == m_btnDocManage) {
      showHintMessage(getHeadHintText()
          + nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPPSCMCommon-000278")/*
                                                                                           * @res
                                                                                           * "�ĵ�����"
                                                                                           */);
      // �ĵ�����
      onDocManage();

    }
    else if (btn == m_btnQueryForAudit) {
      onQueryForAudit();
    }
    // �б��ӡ
    else if (btn == m_btnInvBillPrint) {
      // if (printList == null)
      // printList = new nc.ui.pu.print.PuPrintTool(getInvBillPanel());
      // printList.setData(getSelectedBills());
      // printList.print();
      // ����ӡ
      onBatchPrint();
      // �б��ӡԤ��
    }
    else if (btn == m_btnInvBillPreview) {
      // if (printList == null)
      // printList = new nc.ui.pu.print.PuPrintTool(getInvBillPanel());
      // printList.setData(getSelectedBills());
      // printList.preview();
      // ����ӡ
      onBatchPrintPreview();
    }
    else if (PuTool.isExist(getExtendBtns(), btn)) {
      onExtendBtnsClick(btn);
    }

    else if (btn == m_btnInvBillCopy) {// �б��¸���
      showHintMessage(getHeadHintText() + nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPP40040401-000035")/*
                                                                                                                         * @res
                                                                                                                         * "���Ʒ�Ʊ"
                                                                                                                         */);
      setCurOperState(STATE_EDIT);
      onCopy();
      shiftShowModeTo(INV_PANEL_CARD);
    }
    else if (btn == btnBillCombin) {// �б��ºϲ���ʾ
      onBillCombin();
    }
    else if (btn == m_btnLnkQuery) {// �б�������
      setCurOperState(STATE_BROWSE_NORMAL);
      onLnkQuery();
    }
    else if (btn == m_btnInvBillAudit) {// �б�������
      showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UCH048"));
      onListAudit();
      setCurOperState(STATE_BROWSE_NORMAL);
    }
    else if (btn == m_btnSendAudit) //�б�������������
      onSendAuditBatch();
    else if (btn == m_btnInvBillUnAudit) {// �б�������
      showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UCH049"));
      onListUnAudit();
    }

  }

  /**
   * �رմ��ڵĿͻ��˽ӿڡ����ڱ���������ɴ��ڹر�ǰ�Ĺ�����
   * 
   * @return boolean ����ֵΪtrue��ʾ�����ڹرգ�����ֵΪfalse��ʾ�������ڹرա� �������ڣ�(2001-8-8
   *         13:52:37)
   */
  public boolean onClosing() {
    if (getListOperState() == STATE_LIST_FROM_BILLS) {
      int ret = MessageDialog
          .showYesNoCancelDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPPSCMCommon-000270"),
              nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPP40040401-000040")/*
                                                                                             * @res
                                                                                             * "�Ƿ����ת��δת����ϵĵ��ݣ��˳�?"
                                                                                             */);
      if (ret == MessageDialog.ID_YES) {
        return true;
      }
      return false;
    }

    if (getCurOperState() == STATE_EDIT) {
      int ret = MessageDialog
          .showYesNoCancelDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPPSCMCommon-000270"),
              nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UCH001")/*
                                                                               * @res
                                                                               * "�Ƿ񱣴����޸ĵ�����?"
                                                                               */);
      if (ret == MessageDialog.ID_YES) {
        if (onSave())
          return true;
      }
      else if (ret == MessageDialog.ID_NO) {
        return true;
      }
      return false;
    }

    return true;

  }

  /**
   * ����ǰ��Ʊ����һ�ŷ�Ʊ,ת��ȵ���ϢΪ��
   * 
   * @param ����˵��
   * @return ����ֵ
   * @exception �쳣����
   * @see ��Ҫ�μ�����������
   * @since �������һ���汾���˷�������ӽ���������ѡ��
   */

  private void onCopy() {

    // �б��²��ܸ���
    // �õ����Ƶĵ���
    // InvoiceVO vo = (InvoiceVO) getInvVOs()[getCurVOPos()].clone();
    // �����Ƶ���!!!!!!!!!
    // vo.getHeadVO().setCoperator(getCurOperator());
    // ��������
    // vo.getHeadVO().setDinvoicedate(nc.ui.pub.ClientEnvironment.getInstance().getDate());
    // vo.getHeadVO().setDarrivedate(nc.ui.pub.ClientEnvironment.getInstance().getDate());

    // ��ʾ
    // addNewOneIntoInvVOs(vo);
    setCurOperState(STATE_EDIT);
    // ����VO
    // setVOToBillPanel();

    dealWhenCopy();
    // ����Ƿ��з��Ĵ��
    BillCardPanelHelper.clearSealedInventories(getBillCardPanel(), "cmangid", new String[] {
        "cmangid", "cbaseid", "invcode", "invname", "invspec", "invtype"
    });

    // �Ƿ������Ƶ���
    setCouldMaked(true);
    m_bAdd = true;

    setButtonsAndPanelState();
    updateButtons();

    m_bCopy = true;
  }

  /**
   * ���ߣ���ӡ�� ���ܣ��и��� �������� ���أ��� ���⣺�� ���ڣ�(2001-5-13 11:39:21) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
   */
  private void onCopyLine() {

    getBillCardPanel().copyLine();
  }

  /**
   * ɾ��
   * 
   * @param ����˵��
   * @return ����ֵ
   * @exception �쳣����
   * @see ��Ҫ�μ�����������
   * @since �������һ���汾���˷�������ӽ���������ѡ��
   * @deprecated �÷����������һ���汾���Ѿ������������滻������ѡ��
   */
  private void onDeleteLine() {

    if (getBillCardPanel().getBillModel().getRowCount() <= 0) {
      return;
    }
    // ֻʣ���һ��
    /*
     * if(getInvBillPanel().getRowCount() == 1 ){
     * showWarningMessage("���һ�в���ɾ����") ; return ; }
     */
    getBillCardPanel().delLine();
  }

  /**
   * ���ߣ���ӡ�� ���ܣ��и��� �������ϵ������� a) �����ϣ� b) �����������ֻ�ȫ���� c) �Ѿ����㣻 �������� ���أ��� ���⣺��
   * ���ڣ�(2001-5-13 11:39:21) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
   */
  private void onDiscard() {

    int ret = MessageDialog
        .showYesNoDlg(this,
            nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPP40040401-000042")/*
                                                                                           * @res
                                                                                           * "ѯ��"
                                                                                           */,
            nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UCH002")/*
                                                                             * @res
                                                                             * "���ϵ�ǰ��Ʊ��\n���ϲ������ɻָ���"
                                                                             */);
    if (ret == MessageDialog.ID_NO || ret == MessageDialog.ID_CANCEL) {
      showHintMessage("");
      return;
    }

    // ���ؽ��

    // ����LIST
    ArrayList paraList = new ArrayList();
    paraList.add(null);

    // ��ǰ�����ϵķ�ƱVO
    InvoiceVO invVO = getInvVOs()[getCurVOPos()];
    for (int i = 0; i < invVO.getBodyVO().length; i++) {
      invVO.getBodyVO()[i].setNShowRow(i + 1);
    }
    invVO.getHeadVO().setCuserid(nc.ui.pub.ClientEnvironment.getInstance().getUser().getPrimaryKey());
    // Ϊ�ж��Ƿ���޸ġ����������˵���
    ((InvoiceHeaderVO) invVO.getParentVO()).setCoperatoridnow(nc.ui.pub.ClientEnvironment.getInstance().getUser()
        .getPrimaryKey());

    // ״̬��
    showHintMessage(getHeadHintText() + CommonConstant.SPACE_MARK
        + nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UC001-0000005")/*
                                                                                 * @res
                                                                                 * "����"
                                                                                 */
        + CommonConstant.SPACE_MARK + CommonConstant.BEGIN_MARK + "......" + CommonConstant.END_MARK);

    boolean bConfirm = false;
    invVO.setUserConfirmFlag(new UFBoolean(false));
    while (!invVO.getUserConfirmFlag().booleanValue()) {
      try {
        if (bConfirm)
          invVO.setUserConfirmFlag(new UFBoolean(true));

        // ��ɽ�̹�԰:��¼ҵ����־
        invVO.getOperatelogVO().setOpratorname(nc.ui.pub.ClientEnvironment.getInstance().getUser().getUserName());
        invVO.getOperatelogVO()
            .setCompanyname(nc.ui.pub.ClientEnvironment.getInstance().getCorporation().getUnitname());
        invVO.getOperatelogVO().setOperatorId(nc.ui.pub.ClientEnvironment.getInstance().getUser().getPrimaryKey());
        Operlog operlog = new Operlog();
        operlog.insertBusinessExceptionlog(invVO, "ɾ��", "ɾ��", nc.vo.scm.funcs.Businesslog.MSGMESSAGE,
            nc.vo.scm.pu.BillTypeConst.PO_INVOICE, nc.ui.sm.cmenu.Desktop.getApplet().getParameter("USER_IP"));
        /**
         * ���ο������֧�� by zhaoyha at 2009.2.16
         */
        getInvokeEventProxy().beforeAction(nc.vo.scm.plugin.Action.DELETE, new AggregatedValueObject[]{invVO});
 
        PfUtilClient.processBatch("DISCARD", nc.vo.scm.pu.BillTypeConst.PO_INVOICE, getToday().toString(),
            new InvoiceVO[] {
              invVO
            }, new Object[] {
              paraList
            });
        /**
         * ���ο������֧�� by zhaoyha at 2009.2.16
         */
        getInvokeEventProxy().afterAction(nc.vo.scm.plugin.Action.DELETE, new AggregatedValueObject[]{invVO});

      }
      catch (RwtPiToPoException ex) {
        SCMEnv.out(ex);
         /*if(MessageDialog.showYesNoDlg(this,
         nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401","UPPSCMCommon-000270")@res
         "��ʾ", ex.getMessage()) == MessageDialog.ID_YES){
         //����
        bConfirm = true;
         }else{*/
        showHintMessage(ex.getMessage());
        /* 
        return;
         }*/
      }
      catch (RwtPiToScException ex) {
        SCMEnv.out(ex);
        // if(MessageDialog.showYesNoDlg(this,
        // nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401","UPPSCMCommon-000270")/*@res
        // "��ʾ"*/, ex.getMessage()) == MessageDialog.ID_YES){
        // ����
        bConfirm = true;
        // }else{
        showHintMessage(ex.getMessage());
        // return;
        // }
      }
      catch (Exception e) {
        SCMEnv.out(e);
        MessageDialog.showErrorDlg(this,
            nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPP40040401-000016")/*
                                                                                           * @res
                                                                                           * "ϵͳ����"
                                                                                           */,
            nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPP40040401-000044")/*
                                                                                           * @res
                                                                                           * "����δ�ɹ���"
                                                                                           */
            +System.getProperty("line.separator")+e.getMessage()); //�����쳣��Ϣ
        showHintMessage(e.getMessage());
        return;
      }
      if (!bConfirm)
        break;// δ���쳣,����
    }

    // =================================================
    if (PfUtilClient.isSuccess()) {
      // ��ʾ�û�
      // MessageDialog.showHintDlg(this, "��ʾ", "���Ϸ�Ʊ������");
      // showHintMessage(getHeadHintText() + CommonConstant.SPACE_MARK +
      // nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401","UPP40040401-000045")/*@res
      // "���Ͻ���"*/ + CommonConstant.SPACE_MARK);
      showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UCH006")/*
                                                                                       * res@
                                                                                       * "ɾ���ɹ�"
                                                                                       */);
      // �ӻ����г�ȥ
      removeOneFromInvVOs(getCurVOPos());
      // ���ÿ�Ƭ��������
      setVOToBillPanel();
    }
    //showHintMessage("");
    return;
  }

  private void onDocManage() {
    if(getCurPanelMode() == INV_PANEL_CARD){
      InvoiceVO vo = (InvoiceVO) getInvVOs()[getCurVOPos()];
      if (vo != null && vo.getHeadVO().getPrimaryKey() != null){
        DocumentManager.showDM(this,ScmConst.PO_Invoice, 
            new String[]{vo.getHeadVO().getPrimaryKey()});
      }
    }else {
      if (getInvVOs() != null && getInvVOs().length > 0) {
        String[] billIDs = null;
        String[] billCodes = null;
        Vector v1 = new Vector();
        Vector v2 = new Vector();
        
        for (int i = 0; i < getInvVOs().length; i++) {
          if (getInvVOs()[i].getHeadVO().getPrimaryKey() != null
              && getBillListPanel().getHeadBillModel().getRowState(i) == BillModel.SELECTED) {
            
            v1.add(getInvVOs()[i].getHeadVO().getPrimaryKey());
            v2.add(getInvVOs()[i].getHeadVO().getVinvoicecode());
          }
        }
        if (v1.size() > 0 && v1.size() == v2.size()) {
          billIDs = new String[v1.size()];
          billCodes = new String[v2.size()];
          v1.copyInto(billIDs);
          v2.copyInto(billCodes);
          DocumentManager.showDM(this,ScmConst.PO_Invoice, billIDs);
        }
      }
    }
  }

  private void onHqhp() {

    // ֻ���ڱ༭״̬�²���ʹ�ñ�����
    if (getCurOperState() != STATE_EDIT) {
      return;
    }

    //
    int bodyNum = getBillCardPanel().getRowCount();
    if (bodyNum < 1) {
      return;
    }

    // ����Դ��ⵥ��Ӧ�ļ۸���㵥��ȡ�������Һ�˰���ۡ�
    InvoiceVO billVO = new InvoiceVO(bodyNum);
    getBillCardPanel().getBillValueVO(billVO);
    int iLen = billVO.getBodyVO().length;
    String lStr_NativeCurrencyID = PiPqPublicUIClass.getNativeCurrencyID();
    Vector lVec_RowIndex = new Vector();
    Vector lVec_Temp = new Vector();
    for (int i = 0; i < iLen; i++) {
      if (billVO.getBodyVO()[i].getCupsourcebilltype().equals("45")
          && billVO.getBodyVO()[i].getCcurrencytypeid().equals(lStr_NativeCurrencyID)) {// ������ⵥ���ɵģ��ұ���Ϊ��λ�ҵĲɹ���Ʊ���Խ��С������ż�ȡ�ۡ�

        PricParaVO tempVO = new PricParaVO();
        tempVO.setCgeneralbid(billVO.getBodyVO()[i].getCupsourcebillrowid());
        lVec_Temp.addElement(tempVO);

        lVec_RowIndex.addElement(new Integer(i));

      }
    }

    PricParaVO VOs[] = new PricParaVO[lVec_Temp.size()];
    lVec_Temp.copyInto(VOs);
    try {
      VOs = PricStlHelper.queryPricStlPrices(VOs);
    }
    catch (Exception e) {
      SCMEnv.out(e);
    }

    // ȡ���Һ�˰���ۣ�ע�⣺��Ʊ��û�б��Һ�˰���ۣ����ӿ�����ķ�Ʊ��ԭ��==���ң����Կ��Զ�Ӧ����Ʊ�ϵġ�ԭ�Ҿ���˰���ۡ���xhq��
    StringBuffer error = new StringBuffer();

    if (VOs != null && VOs.length > 0) {
      for (int i = 0; i < VOs.length; i++) {

        if (VOs[i].getNtaxprice() != null) {
          int lint_row = ((Integer) lVec_RowIndex.elementAt(i)).intValue();

          if (getUFDouble(VOs[i].getNtaxprice()) != null && getUFDouble(VOs[i].getNtaxprice()).doubleValue() < 0.0) {
            error.append(billVO.getBodyVO()[lint_row].getCrowno() + ",");
          }
          else {
            getBillCardPanel().setBodyValueAt(VOs[i].getNtaxprice(), lint_row, "norgnettaxprice");
            BillEditEvent event = new BillEditEvent(getBillCardPanel().getBillTable(), VOs[i].getNtaxprice(),
                "norgnettaxprice", lint_row);// ԭ�Ҿ���˰����
            afterEditInvBillRelations(event);// ������ؼ���
          }
        }
      }
    }
    if (error != null && error.length() > 0) {
      MessageDialog.showHintDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("scmcommon", "UPPSCMCommon-000270")/** @res* "��ʾ" */
      , "��" + error.toString() + "\n" + nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UC000-0000450")/* @res:������ */
          + nc.ui.ml.NCLangRes.getInstance().getStrByID("scmpub", "UPPscmpub-000507")/*
                                                                                       * @res
                                                                                       * "����Ϊ�������������롣"
                                                                                       */);
    }

  }

  /* ����ITEMKEY�õ��õ���ǰ�е�ֵ ֻҪ��ֵ�������Ƿ�Ϊ�㣬��ȡ���ֵ Ϊ�ջ�մ���ʱ������NULL */
  private UFDouble getUFDouble(UFDouble double1) {

    if (double1 == null || double1.equals("")) {
      return null;
    }

    return PuPubVO.getUFDouble_ValueAsValue(double1);

  }

  /**
   * ����
   * 
   * @param ����˵��
   * @return ����ֵ
   * @exception �쳣����
   * @see ��Ҫ�μ�����������
   * @since �������һ���汾���˷�������ӽ���������ѡ��
   * @deprecated �÷����������һ���汾���Ѿ������������滻������ѡ��
   */
  private void onFirst() {
    // ����ԭ����λ��
    m_nLstInvVOPos = getCurVOPos();

    setCurVOPos(0);
    // ���ý���λ��Ϊ����λ��
    setMaxMnyDigit(iMaxMnyDigit);

    // ���ÿ�Ƭ��������
    setVOToBillPanel();
  }

  /**
   * ���ߣ����� ���ܣ�����ת�� �������� ���أ��� ���⣺�� ���ڣ�(2001-5-13 11:39:21) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
   */
  private void onGiveupBillConversion() {

    Vector v = new Vector();
    for (int i = 0; getInvVOs() != null && i < getInvVOs().length; i++) {
      if (getInvVOs()[i].getPrimaryKey() == null) {
        v.add(new Integer(i));
      }
    }

    Integer[] indexes = null;
    if (v.size() > 0) {
      indexes = new Integer[v.size()];
      v.copyInto(indexes);
    }
    if (indexes != null) {
      removeSomeFromInvVOs(indexes);
    }

    if (m_vSavedVO != null && m_vSavedVO.size() > 0) {
      InvoiceVO tempVO[] = new InvoiceVO[m_vSavedVO.size()];
      m_vSavedVO.copyInto(tempVO);
      setInvVOs(tempVO);
    }
    else {
      setInvVOs(getInvVOsByContinue());
    }
    m_vSavedVO = null;

    if (getInvVOs() == null) {
      setCurVOPos(-1);
      // ���ÿ�Ƭ��������
      setVOToBillPanel();
    }
    else {
      setCurVOPos(getInvVOs().length - 1);
      setVOToBillPanel();
    }

    setListOperState(STATE_LIST_NORMAL);
    setCurOperState(getBillBrowseState());
    shiftShowModeTo(INV_PANEL_CARD);
    m_btnInvBillConversion.setEnabled(false);

  }

  /**
   * ���ߣ���ӡ�� ���ܣ������� �������� ���أ��� ���⣺�� ���ڣ�(2001-5-13 11:39:21) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
   * 2002-09-26 wyf ������²����е�һЩֵ��Ĭ������
   */
  private void onInsertLine() {
    int iRow = getBillCardPanel().getBillTable().getSelectedRow();

    getBillCardPanel().insertLine();

    if (iRow >= 0) {
      // wyf 2002-09-26 add begin
      setNewLineDefaultValue(iRow);
      // wyf 2002-09-26 add end
    }

    BillRowNo.insertLineRowNo(getBillCardPanel(), ScmConst.PO_Invoice, "crowno");
  }

  /**
   * ĩ��
   * 
   * @param ����˵��
   * @return ����ֵ
   * @exception �쳣����
   * @see ��Ҫ�μ�����������
   * @since �������һ���汾���˷�������ӽ���������ѡ��
   * @deprecated �÷����������һ���汾���Ѿ������������滻������ѡ��
   */
  private void onLast() {
    // ����ԭ����λ��
    m_nLstInvVOPos = getCurVOPos();

    setCurVOPos(getInvVOs().length - 1);
    // ���ý���λ��Ϊ����λ��
    setMaxMnyDigit(iMaxMnyDigit);

    // ���ÿ�Ƭ��������
    setVOToBillPanel();

  }

  /**
   * �л����б�״̬
   * 
   * @param ����˵��
   * @return ����ֵ
   * @exception �쳣����
   * @see ��Ҫ�μ�����������
   * @since �������һ���汾���˷�������ӽ���������ѡ��
   * @deprecated �÷����������һ���汾���Ѿ������������滻������ѡ��
   */
  private void onList() {

    if (getInvVOs() != null) {
      int nVOStatus = 0;
      int len = getInvVOs().length;
      for (int i = 0; i < len; i++) {
        nVOStatus = getInvVOs()[i].getSource();
        // ���ǲ�ѯ���ĵ��ݴӻ�����ɾ��
        if (PuPubVO.getString_TrimZeroLenAsNull(getInvVOs()[i].getHeadVO().getVinvoicecode()) == null
            || PuPubVO.getString_TrimZeroLenAsNull(getInvVOs()[i].getHeadVO().getVinvoicecode()) == null) {
          removeOneFromInvVOs(i);
        }

      }
    }

    shiftShowModeTo(INV_PANEL_LIST);
    // ����VO���鵽�б����
    setVOsToListPanel();
    // �б�״̬
    setCurOperState(getListOperState());
  }

  /**
   * �л�����Ƭ״̬
   * 
   * @param ����˵��
   * @return ����ֵ
   * @exception �쳣����
   * @see ��Ҫ�μ�����������
   * @since �������һ���汾���˷�������ӽ���������ѡ��
   */
  private void onListBill() {

    // �������������ת��,��˫��������
    if (getCurOperState() == STATE_LIST_FROM_BILLS) {
      showHintMessage(getHeadHintText() + nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UCH027")/*
                                                                                                           * @res
                                                                                                           * "�޸ķ�Ʊ"
                                                                                                           */);
      // �޸�
      onListModify();
      // �ù�굽��ͷ��һ���ɱ༭��Ŀ
      getBillCardPanel().transferFocusTo(BillCardPanel.HEAD);
      return;
    }

    if (getInvVOs() == null)
      getBillCardPanel().addNew();
    else {
      int row = getSelectedRowOnList();
      row = PuTool.getIndexBeforeSort(getBillListPanel(), row);

      if (getInvVOs()[row].getBodyVO() == null) {
        return;
      }
      setCurVOPos(row);
      // ���ý���λ��Ϊ����λ��
      setMaxMnyDigit(iMaxMnyDigit);
      // ���ÿ�Ƭ��������
      setVOToBillPanel();
      // ���ÿɱ༭��
      getBillCardPanel().getHeadItem("nexchangeotobrate").setEdit(false);
    }
    setCurOperState(getBillBrowseState());
    shiftShowModeTo(INV_PANEL_CARD);
  }


  /**
   * ���ߣ���ӡ�� ���ܣ������б��һ��ѡ�еĵ��� �������� ���أ��� ���⣺�� ���ڣ�(2002-3-13 11:39:21)
   * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־�� 2002-04-15 wyf ��Ϻ��ֻ����TS�ж��Ƿ񵥾ݸı䣬���������һЩ���Ƿ��������ж�
   * 2002-06-19 wyf �޸���ʾ 2002-09-26 wyf ��������ⷢƱ�Ŀ���
   */
  private void onListDiscard() {

    // �õ���ѡ�е�VO
    Vector vDiscardVO = new Vector();
    Vector vDiscardIndex = new Vector();
    for (int i = 0; i < getInvVOs().length; i++) {
      if (getBillListPanel().getHeadBillModel().getRowState(i) == BillModel.SELECTED) {
        // �п��ܾ��������򣬵õ������ı�ͷINDEX
        int nCurIndex = PuTool.getIndexBeforeSort(getBillListPanel(), i);
        vDiscardVO.add(getInvVOs()[nCurIndex]);
        vDiscardIndex.add(new Integer(nCurIndex));
      }
    }

    // װ�ر���
    InvoiceVO[] vos = null;
    if (vDiscardVO.size() > 0) {
      vos = new InvoiceVO[vDiscardVO.size()];
      vDiscardVO.copyInto(vos);
      if (!loadItemsForInvoiceVOs(vos))
        return;
    }

    // �жϽ�Ҫɾ����VO�Ϸ���
    for (int i = 0; vos != null && i < vos.length; i++) {
      // ���������������Ʊ�򷵻�
      Integer iBillStatus = vos[i].getHeadVO().getIbillstatus();
      if (iBillStatus.compareTo(nc.vo.scm.pu.BillStatus.AUDITED) == 0
          || iBillStatus.compareTo(nc.vo.scm.pu.BillStatus.AUDITING) == 0) {
        MessageDialog
            .showHintDlg(this,
                nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPPSCMCommon-000270")/*
                                                                                               * @res
                                                                                               * "��ʾ"
                                                                                               */,
                nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPP40040401-000046")/*
                                                                                               * @res
                                                                                               * "��ѡ��Ʊ���������������������ķ�Ʊ��"
                                                                                               */);
        return;
      }
      // ����������ⷢƱ�򷵻�
      if (vos[i].isVirtual()) {
        MessageDialog.showHintDlg(this,
            nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPPSCMCommon-000270")/*
                                                                                           * @res
                                                                                           * "��ʾ"
                                                                                           */,
            nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPP40040401-000047")/*
                                                                                           * @res
                                                                                           * "��ѡ��Ʊ�������ⷢƱ��"
                                                                                           */);
        return;
      }
      // ������ڽ���ķ�Ʊ�򷵻�
      InvoiceItemVO[] voaItem = vos[i].getBodyVO();
      int iSettle = -1;
      int iLen = voaItem.length;
      for (int j = 0; j < iLen; j++) {
        if (voaItem[j].getNaccumsettmny() != null
            && voaItem[j].getNaccumsettmny().compareTo(nc.vo.scm.pu.VariableConst.ZERO) != 0) {
          iSettle = i + 1;
          break;
        }
      }
      if (iSettle != -1) {
        MessageDialog.showHintDlg(this,
            nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPPSCMCommon-000270")/*
                                                                                           * @res
                                                                                           * "��ʾ"
                                                                                           */,
            nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPP40040401-000048")/*
                                                                                           * @res
                                                                                           * "��ѡ��Ʊ�����ѽ���ķ�Ʊ��"
                                                                                           */);
        return;
      }
    }

    if (vDiscardVO.size() == 0) {
      MessageDialog.showHintDlg(this,
          nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPPSCMCommon-000270")/*
                                                                                         * @res
                                                                                         * "��ʾ"
                                                                                         */,
          nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPP40040401-000049")/*
                                                                                         * @res
                                                                                         * "����ѡ��Ҫ���ϵķ�Ʊ��"
                                                                                         */);
      setSelectedRowCount(0);
      return;
    }

    int ret = MessageDialog.showYesNoDlg(this,
        nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UCH002")/*
                                                                         * @res
                                                                         * "ѯ��"
                                                                         */, nc.ui.ml.NCLangRes.getInstance()
            .getStrByID("40040401", "UPP40040401-000050")/*
                                                           * @res
                                                           * "����ѡ�еķ�Ʊ��\n���ϲ������ɻָ���"
                                                           */);

    if (ret == MessageDialog.ID_NO || ret == MessageDialog.ID_CANCEL) {
      return;
    }

    // �õ������ϵ�VO
    InvoiceVO[] proceVOs = new InvoiceVO[vDiscardVO.size()];
    vDiscardVO.copyInto(proceVOs);

    // ��ǰ�����ϵķ�ƱVO
    int len = proceVOs.length;
    for (int i = 0; i < len; i++) {
      int bLen = proceVOs[i].getBodyVO().length;
      for (int j = 0; j < bLen; j++) {
        proceVOs[i].getBodyVO()[j].setNShowRow(j + 1);

      }
    }
    // ����LIST
    Object[] paraLists = new Object[len];
    len = paraLists.length;
    for (int i = 0; i < len; i++) {
      paraLists[i] = new ArrayList();
      ((ArrayList) paraLists[i]).add(null);
    }

    for (int i = 0; i < proceVOs.length; i++) {
      proceVOs[i].getHeadVO().setCuserid(nc.ui.pub.ClientEnvironment.getInstance().getUser().getPrimaryKey());
      // Ϊ�ж��Ƿ���޸ġ����������˵���
      ((InvoiceHeaderVO) proceVOs[i].getParentVO()).setCoperatoridnow(nc.ui.pub.ClientEnvironment.getInstance()
          .getUser().getPrimaryKey());

      // ��ɽ�̹�԰:��¼ҵ����־
      proceVOs[i].getOperatelogVO().setOpratorname(nc.ui.pub.ClientEnvironment.getInstance().getUser().getUserName());
      proceVOs[i].getOperatelogVO().setCompanyname(
          nc.ui.pub.ClientEnvironment.getInstance().getCorporation().getUnitname());
      proceVOs[i].getOperatelogVO().setOperatorId(nc.ui.pub.ClientEnvironment.getInstance().getUser().getPrimaryKey());
      Operlog operlog = new Operlog();
      operlog.insertBusinessExceptionlog(proceVOs[i], "ɾ��", "ɾ��", nc.vo.scm.funcs.Businesslog.MSGMESSAGE,
          nc.vo.scm.pu.BillTypeConst.PO_INVOICE, nc.ui.sm.cmenu.Desktop.getApplet().getParameter("USER_IP"));

    }

    // ״̬��
    showHintMessage(getHeadHintText() + CommonConstant.SPACE_MARK
        + nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPP40040401-000051")/*
                                                                                         * @res
                                                                                         * "��������"
                                                                                         */
        + CommonConstant.SPACE_MARK + CommonConstant.BEGIN_MARK + "......" + CommonConstant.END_MARK);

    boolean bConfirm = false;
    for (int i = 0; i < len; i++) {
      vos[i].setUserConfirmFlag(new UFBoolean(false));
      while (!vos[i].getUserConfirmFlag().booleanValue()) {
        try {
          if (bConfirm)
            vos[0].setUserConfirmFlag(new UFBoolean(true));
          PfUtilClient.processBatch("DISCARD", BillTypeConst.PO_INVOICE, ClientEnvironment.getInstance().getDate()
              .toString(), new InvoiceVO[] {
            proceVOs[i]
          }, paraLists);
          break;
        }
        catch (RwtPiToPoException ex) {
          SCMEnv.out(ex);
          // if(MessageDialog.showYesNoDlg(this,
          // nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401","UPPSCMCommon-000270")/*@res
          // "��ʾ"*/, ex.getMessage()) == MessageDialog.ID_YES){
          // ����
          bConfirm = true;
          // continue;
          // }else{
          // showHintMessage("");
          // return;
          // }
        }
        catch (RwtPiToScException ex) {
          SCMEnv.out(ex);
          // if(MessageDialog.showYesNoDlg(this,
          // nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401","UPPSCMCommon-000270")/*@res
          // "��ʾ"*/, ex.getMessage()) == MessageDialog.ID_YES){
          // ����
          bConfirm = true;
          // }else{
          // showHintMessage("");
          // return;
          // }
        }
        catch (Exception e) {
          SCMEnv.out(e);
          MessageDialog.showHintDlg(this,
              nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPP40040401-000016")/*
                                                                                             * @res
                                                                                             * "ϵͳ����"
                                                                                             */,
              nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPP40040401-000052")/*
                                                                                             * @res
                                                                                             * "��������δ�ɹ���"
                                                                                             */);
          return;
        }

      }
    }
    if (PfUtilClient.isSuccess()) {
      // �ӻ����г�ȥ
      Integer[] iaIndex = new Integer[vDiscardIndex.size()];
      vDiscardIndex.copyInto(iaIndex);
      removeSomeFromInvVOs(iaIndex);
      setVOsToListPanel();
      // onListRefresh();
    }

  }

  /**
   * ���ߣ���ӡ�� ���ܣ��б�ť���޸ġ���Ӧ�ĺ��� �������� ���أ��� ���⣺�� ���ڣ�(2001-5-13 11:39:21)
   * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־�� 2002-09-26 wyf ��������ⷢƱ�Ŀ��� 2002-11-12 wyf
   * �޸�����ʧ�ܵķ�Ʊ�����޸ĵ�����
   */
  private void onListModify() {

    int nCurIndex = -1;
    int VOPos = -1;
    for (int i = 0; i < getInvVOs().length; i++) {
      if (getBillListPanel().getHeadBillModel().getRowState(i) == BillModel.SELECTED) {
        nCurIndex = i;
        break;
      }
    }

    // �п��ܾ��������򣬵õ������ı�ͷINDEX
    nCurIndex = PuTool.getIndexBeforeSort(getBillListPanel(), nCurIndex);
    VOPos = nCurIndex;
    if (VOsPos != null && VOsPos.length > 0) {
      nCurIndex = new Integer(VOsPos[nCurIndex]).intValue();
      VOsPos = null;
    }

    if (getCurOperState() != STATE_LIST_FROM_BILLS) {
      // ////////////////////�����������ķ�Ʊ�����޸�
      // ��������:�����޸�
      Integer iStatus = getInvVOs()[nCurIndex].getHeadVO().getIbillstatus();
      if (iStatus != null && iStatus.compareTo(nc.vo.scm.pu.BillStatus.FREE) != 0
          && iStatus.compareTo(nc.vo.scm.pu.BillStatus.AUDITFAIL) != 0) { // ����,����δͨ��
        MessageDialog.showHintDlg(this,
            nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPPSCMCommon-000270")/*
                                                                                           * @res
                                                                                           * "��ʾ"
                                                                                           */,
            nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPP40040401-000055")/*
                                                                                           * @res
                                                                                           * "�÷�Ʊ�������������޸ģ�"
                                                                                           */);
        return;
      }
      // �ѽ���ķ�Ʊ�������޸�
      for (int i = 0; i < getInvVOs()[nCurIndex].getBodyVO().length; i++) {
        if ((getInvVOs()[nCurIndex].getBodyVO()[i].getNaccumsettmny() != null && getInvVOs()[nCurIndex].getBodyVO()[i]
            .getNaccumsettmny().doubleValue() != 0.0)) {
          MessageDialog.showHintDlg(this, nc.ui.ml.NCLangRes.getInstance()
              .getStrByID("40040401", "UPPSCMCommon-000270")/* @res "��ʾ" */, nc.ui.ml.NCLangRes.getInstance()
              .getStrByID("40040401", "UPP40040401-000056")/*
                                                             * @res
                                                             * "�÷�Ʊ�ѽ��㣬�����޸ģ�"
                                                             */);
          return;
        }
      }
      // ���ⷢƱ:�����޸�
      if (getInvVOs()[nCurIndex].isVirtual()) {
        MessageDialog.showHintDlg(this,
            nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPPSCMCommon-000270")/*
                                                                                           * @res
                                                                                           * "��ʾ"
                                                                                           */,
            nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPP40040401-000057")/*
                                                                                           * @res
                                                                                           * "�÷�ƱΪ���ⷢƱ�������޸ģ�"
                                                                                           */);
        return;
      }

      // ҵ������
      setCurBizeType(getInvVOs()[nCurIndex].getHeadVO().getCbiztype());
    }
    if (getBillListPanel().getHeadBillModel().getValueAt(VOPos, "cinvoiceid") == null && splitFlag) {
      // if(nCurIndex != 0){
      setCurVOPos(currentPos + VOPos);
      // }else{
      // setCurVOPos(currentPos);
      // }
      splitFlag = false;
    }
    else {
      setCurVOPos(nCurIndex);
    }
    setCurOperState(STATE_EDIT);
    setInvoiceTypeComItem();// �������ơ����⡱��Ʊ
    shiftShowModeTo(INV_PANEL_CARD);
    // ���ÿ�Ƭ��������
    setVOToBillPanel();

    // ȡҵ������
    String cBizType = getBillCardPanel().getHeadItem("cbiztype").getValue();
    if (cBizType == null || cBizType.toString().trim().length() == 0) {
      cBizType = getCurBizeType();
      getBillCardPanel().setHeadItem("cbiztype", cBizType);
    }
    try {
      // ���˴������,��֤��ҵ�����������д���ҵ������ʱ�滻�����������д�������
      setInventoryRefFilter(cBizType);
    }
    catch (Exception e) {
      SCMEnv.out(e);
    }

    for (int i = 0; i < getBillCardPanel().getRowCount(); i++) {
      // ���ñ����еĿ�˰������ͷ��ͬ
      if (getBillCardPanel().getBodyValueAt(i, "idiscounttaxtype") == null) {
        getBillCardPanel().setBodyValueAt(
            ((UIComboBox) getBillCardPanel().getHeadItem("idiscounttaxtype").getComponent()).getSelectedItem(), i,
            "idiscounttaxtype");
      }
      // ���������Ĭ��˰��
      if (getBillCardPanel().getBodyValueAt(i, "ntaxrate") == null) {
        setRelated_Taxrate(i, i);
      }
    }
    //Ч���Ż� by zhaoyha at 2009.9
    boolean isNeedCal=getBillCardPanel().getBillModel().isNeedCalculate();
    getBillCardPanel().getBillModel().setNeedCalculate(false);
    getBillCardPanel().setDefaultPrice(getBillCardPanel());
    //Ч���Ż� by zhaoyha at 2009.9
    getBillCardPanel().getBillModel().setNeedCalculate(isNeedCal);
    if (getCurOperState() != STATE_LIST_FROM_BILLS) {
      afterEditWhenHeadCurrency(null);
    }
    // since v51, ����ҵ��ԱĬ��ֵ ���ݲ���Ա����ҵ��Ա
    setDefaultValueByUser();
    //For V56 ������������ͨ������,�����������Ϣ
    clearAuditInfo(null, getBillCardPanel());
    //clear
    // �����˵��Ҽ�����Ȩ�޿���
    setPopMenuBtnsEnable();
    createAddContinueBtn();
    updateButtons();
  }

  /**
   * ��ѯ���з��������ķ�Ʊ
   * 
   * @param ����˵��
   * @return ����ֵ
   * @exception �쳣����
   * @see ��Ҫ�μ�����������
   * @since �������һ���汾���˷�������ӽ���������ѡ��
   */

  // ��ʱȡ�����иõ�λ�ķ�Ʊ,��ͬʱ��ʾ��һ��
  private void onListQuery() {

    //��ʾ��ѯ�Ի���
    InvQueDlg curDlg = getQueDlg();
    curDlg.showModal();

    //��ѯ
    if (curDlg.isCloseOK()) {

      // ����ҵ������
      // setOldBizeType(getCurBizeType());

      // ��ѯ
      execListQuery(curDlg);
      // �����Ƿ��������
      setEverQueryed(true);
    }

  }

  /**
   * ��ѯ���з��������ķ�Ʊ
   * 
   * @param ����˵��
   * @return ����ֵ
   * @exception �쳣����
   * @see ��Ҫ�μ�����������
   * @since �������һ���汾���˷�������ӽ���������ѡ��
   */

  // ��ʱȡ�����иõ�λ�ķ�Ʊ,��ͬʱ��ʾ��һ��
  private void onListRefresh() {
    // δ���ٵĶԻ���
    execListQuery(getQueDlg());
  }

  /**
   * ����
   * 
   * @param ����˵��
   * @return ����ֵ
   * @exception �쳣����
   * @see ��Ҫ�μ�����������
   * @since �������һ���汾���˷�������ӽ���������ѡ��
   * @deprecated �÷����������һ���汾���Ѿ������������滻������ѡ��
   */
  private void onLnkQuery() {

    InvoiceVO vo = null;
    if(m_nCurOperState==STATE_EDIT)
      vo=(InvoiceVO)getBillCardPanel().getBillValueVO(InvoiceVO.class.getName(), 
          InvoiceHeaderVO.class.getName(), InvoiceItemVO.class.getName());
    else
      vo= getInvVOs()[getCurVOPos()];

    if (vo == null || vo.getParentVO() == null)
      return;

    nc.ui.scm.sourcebill.SourceBillFlowDlg soureDlg = new nc.ui.scm.sourcebill.SourceBillFlowDlg(this,
        ScmConst.PO_Invoice, ((InvoiceHeaderVO) vo.getParentVO()).getPrimaryKey(), null, nc.ui.pub.ClientEnvironment
            .getInstance().getUser().getPrimaryKey(), ((InvoiceHeaderVO) vo.getParentVO()).getVinvoicecode());
    soureDlg.showModal();
  }

  /**
   * ����Ŀ�ݷ�ʽ
   */
  public void onMenuItemClick(java.awt.event.ActionEvent e) {

    UIMenuItem menuItem = (UIMenuItem) e.getSource();
    if (menuItem == getBillCardPanel().getAddLineMenuItem()) {
      // ����
      onAppendLine();
    }
    else if (menuItem == getBillCardPanel().getDelLineMenuItem()) {
      // ɾ��
      onDeleteLine();
    }
    else if (menuItem == getBillCardPanel().getCopyLineMenuItem()) {
      // ����
      onCopyLine();
    }
    else if (menuItem == getBillCardPanel().getPasteLineMenuItem()) {
      // ճ��
      onPasteLine();
    }
    else if (menuItem == getBillCardPanel().getInsertLineMenuItem()) {
      // ����
      onInsertLine();
    }
    else if (menuItem == getBillCardPanel().getPasteLineToTailMenuItem()) {
      onPasteLineToTail();
    }
    // �����к�
    else if (menuItem.equals(m_miReSortRowNo)) {
      onReSortRowNo();
    }
    else if (menuItem.equals(m_miCardEdit)) {
      onCardEdit();
    }
    /**
     * ���ο������֧�� by zhaoyha at 2009.2.16
     */
    getInvokeEventProxy().onMenuItemClick(e);
  }

  /*
   * �����к�
   */
  private void onReSortRowNo() {
    PuTool.resortRowNo(getBillCardPanel(), ScmConst.PO_Invoice, "crowno");
    showHintMessage(NCLangRes.getInstance().getStrByID("common", "SCMCOMMON000000284")/*
                                                                                       * @res
                                                                                       * "�����кųɹ�"
                                                                                       */);
  }

  private void onCardEdit() {
    //���ø������ȿɱ༭״̬
    int rows=getBillCardPanel().getRowCount();
    for(int i=0;i<rows;++i){
      beforeEditBodyAssistUnitNumber(getBillCardPanel(), i);
      //���������ҪΪ�˳�ʼ������
      Object vf=getBillCardPanel().getBodyValueAt(i, "vfree0");
      if(null!=vf && !StringUtil.isEmptyWithTrim(vf.toString())){
        beforeEdit(new BillEditEvent(this,null,null,"vfree0",i,BillItem.BODY));
      }
    }
    getBillCardPanel().startRowCardEdit();
  }

  /**
   * �����Ĺ��ܡ���;�������Եĸ��ģ��Լ�����ִ��ǰ������״̬��
   * 
   * @param ����˵��
   * @return ����ֵ
   * @exception �쳣����
   * @see ��Ҫ�μ�����������
   * @since �������һ���汾���˷�������ӽ���������ѡ��
   * @deprecated �÷����������һ���汾���Ѿ������������滻������ѡ��
   */
  private void onModify() {

    setInvoiceTypeComItem();// �������ơ����⡱��Ʊ

    // ȡҵ������
    String cBizType = getBillCardPanel().getHeadItem("cbiztype").getValue();
    // ���˴������
    try {
      // ���˴������,��֤��ҵ�����������д���ҵ������ʱ�滻�����������д�������
      setInventoryRefFilter(cBizType);
    }
    catch (Exception e) {
      SCMEnv.out(e);
    }

    // String cinventorycode =
    // getInvBillPanel().getBodyItem("cinventorycode").getValue();
    //For V56 ������������ͨ������,�����������Ϣ
    clearAuditInfo(null, getBillCardPanel());
    // �����˵��Ҽ�����Ȩ�޿���
    setPopMenuBtnsEnable();
    setButtonsAndPanelState();
    createAddContinueBtn();
    updateButtons();
  }

  /**
   * ��һ��
   * 
   * @param ����˵��
   * @return ����ֵ
   * @exception �쳣����
   * @see ��Ҫ�μ�����������
   * @since �������һ���汾���˷�������ӽ���������ѡ��
   * @deprecated �÷����������һ���汾���Ѿ������������滻������ѡ��
   */
  private void onNext() {
    // ����ԭ����λ��
    m_nLstInvVOPos = getCurVOPos();

    setCurVOPos(getCurVOPos() + 1);
    // ���ý���λ��Ϊ����λ��
    setMaxMnyDigit(iMaxMnyDigit);

    // ���ÿ�Ƭ��������
    setVOToBillPanel();

  }

  /**
   * ���ߣ���ӡ�� ���ܣ���ճ�� �������� ���أ��� ���⣺�� ���ڣ�(2001-5-13 11:39:21) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
   * 2002-09-20 wyf �������Դ�������ͼ���Դ���ݺŵ���ʾ 2002-11-25 wyf ��TS����
   */
  private void onPasteLine() {

    // ���ճ��ǰ�ı�������
    int nRowCount1 = getBillCardPanel().getRowCount();
    getBillCardPanel().pasteLine();
    // ���ճ����ı�������
    int nRowCount2 = getBillCardPanel().getRowCount();
    // ���ճ��������
    int nPastRowCount = nRowCount2 - nRowCount1;

    // �Ա�ճ������,�����޸�ĳд�ֶε�ֵ
    if (nPastRowCount > 0) {
      // ȡ��ͷ����
//      String strCurr = getBillCardPanel().getHeadItem("ccurrencytypeid").getValue();
      int iSelectRow = getBillCardPanel().getBillTable().getSelectedRow();
      for (int i = iSelectRow - nPastRowCount; i < iSelectRow; i++) {
        // �����ÿյ�ֵ
        getBillCardPanel().setBodyValueAt(null, i, "cinvoiceid");
        getBillCardPanel().setBodyValueAt(null, i, "cinvoice_bid");
        // ����Դ���ϲ���Դ�й�
        // getInvBillPanel().setBodyValueAt(null, i, "corderid");
        // getInvBillPanel().setBodyValueAt(null, i, "corder_bid");
        getBillCardPanel().setBodyValueAt(null, i, "ts");
        // �����²���ı����б���Ĭ��Ϊ��ͷ����
       // getBillCardPanel().setBodyValueAt(strCurr, i, "ccurrencytypeid");
        // ���û���
        setExchangeRateBody(i, true, null);
      }
      BillRowNo.pasteLineRowNo(getBillCardPanel(), ScmConst.PO_Invoice, "crowno", nPastRowCount);
    }
  }

  /**
   * �˴����뷽��˵���� �������ڣ�(2003-11-10 11:19:18)
   */
  private void onPasteLineToTail() {
    int iOldRowCnt = getBillCardPanel().getRowCount();
    getBillCardPanel().pasteLineToTail();
    int iNewRowCnt = getBillCardPanel().getRowCount();
    if (iOldRowCnt > 0 && iNewRowCnt > 0 && iOldRowCnt == iNewRowCnt)
      showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPPSCMCommon-000424")/*
                                                                                                     * @res
                                                                                                     * "ճ���е���βδ�ɹ�,����ԭ��û�п������ݻ�δȷ��Ҫճ����λ��"
                                                                                                     */);
    else {
      // ���ճ��������
      int nPastRowCount = iNewRowCnt - iOldRowCnt;

      // �Ա�ճ������,�����޸�ĳд�ֶε�ֵ
      if (nPastRowCount > 0) {
        // int iSelectRow = getInvBillPanel().getBillTable().getSelectedRow();
        // ȡ��ͷ����
        String strCurr = getBillCardPanel().getHeadItem("ccurrencytypeid").getValue();
        for (int i = iOldRowCnt; i < iNewRowCnt; i++) {
          // �����ÿյ�ֵ
          getBillCardPanel().setBodyValueAt(null, i, "cinvoiceid");
          getBillCardPanel().setBodyValueAt(null, i, "cinvoice_bid");
          // ����Դ���ϲ���Դ�й�
          // getInvBillPanel().setBodyValueAt(null, i, "corderid");
          // getInvBillPanel().setBodyValueAt(null, i, "corder_bid");

          getBillCardPanel().setBodyValueAt(null, i, "ts");
          // �����²���ı����б���Ĭ��Ϊ��ͷ����
          getBillCardPanel().setBodyValueAt(strCurr, i, "ccurrencytypeid");
          // ���û���
          setExchangeRateBody(i, true, null);
        }
        // �����к�
        BillRowNo.addLineRowNos(getBillCardPanel(), ScmConst.PO_Invoice, "crowno", nPastRowCount);
      }
      showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPP40040401-000058")/*
                                                                                                     * @res
                                                                                                     * (iNewRowCnt -
                                                                                                     * iOldRowCnt) +
                                                                                                     * "�з�Ʊ��ճ��"
                                                                                                     */);
    }
  }

  /**
   * ��һ��
   * 
   * @param ����˵��
   * @return ����ֵ
   * @exception �쳣����
   * @see ��Ҫ�μ�����������
   * @since �������һ���汾���˷�������ӽ���������ѡ��
   * @deprecated �÷����������һ���汾���Ѿ������������滻������ѡ��
   */
  private void onPrevious() {
    // ����ԭ����λ��
    m_nLstInvVOPos = getCurVOPos();

    setCurVOPos(getCurVOPos() - 1);
    // ���ý���λ��Ϊ����λ��
    setMaxMnyDigit(iMaxMnyDigit);

    // ���ÿ�Ƭ��������
    setVOToBillPanel();
  }

  /**
   * ��ѯ��ǰ��������״̬
   */

  public void onQueryForAudit() {

    if (getInvVOs() != null && getInvVOs().length > 0) {
      // ����õ��ݴ�����������״̬��ִ��������䣺
      // by zhaoyha ����ҵ������
      nc.ui.pub.workflownote.FlowStateDlg approvestatedlg = new nc.ui.pub.workflownote.FlowStateDlg(this, "25",
          getInvVOs()[getCurVOPos()].getHeadVO().getCbiztype(),getInvVOs()[getCurVOPos()].getHeadVO().getPrimaryKey());
      approvestatedlg.showModal();
      showHintMessage(NCLangRes.getInstance().getStrByID("common", "UCH035")/*
                                                                             * @res
                                                                             * "����״̬��ѯ�ɹ�"
                                                                             */);
    }
    else {
      showHintMessage(NCLangRes.getInstance().getStrByID("40040101", "UPP40040101-000539")/*
                                                                                           * @res
                                                                                           * "����������"
                                                                                           */);
    }

  }

  /**
   * ���浱ǰ��Ʊ
   * 
   * @param ����˵��
   * @return ����ֵ
   * @exception �쳣����
   * @see ��Ҫ�μ�����������
   * @since �������һ���汾���˷�������ӽ���������ѡ��
   */
  private boolean onSave() {

    nc.vo.scm.pu.Timer timer = new nc.vo.scm.pu.Timer();
    timer.start("�ɹ���Ʊ���濪ʼ");

    // showHintMessage(getHeadHintText() + CommonConstant.SPACE_MARK +
    // nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401","UPP40040401-000059")/*@res
    // "����ǰУ��"*/ + CommonConstant.SPACE_MARK);
    // ��ֹ�༭
    getBillCardPanel().stopEditing();
    // ���ǿ���
    filterNullLine();

    if (!BillRowNo.verifyRowNosCorrect(getBillCardPanel(), "crowno")) {
      return false;
    }

    int nRow = getBillCardPanel().getRowCount();
    for (int i = 0; i < nRow; i++) {
      UFDouble nNum = new UFDouble(0), nMoney = new UFDouble(0);
      Object oTemp = getBillCardPanel().getBodyValueAt(i, "ninvoicenum");
      if (oTemp != null)
        nNum = new UFDouble(oTemp.toString());
      oTemp = getBillCardPanel().getBodyValueAt(i, "noriginalcurmny");
      if (oTemp != null)
        nMoney = new UFDouble(oTemp.toString());
      if (nNum.doubleValue() * nMoney.doubleValue() < 0) {
        MessageDialog.showWarningDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401",
            "UPPSCMCommon-000132")/* @res "����" */, nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401",
            "UPP40040401-000253")/* @res"�����ͽ���һ��!" */);
        return false;
      }
    }

    // �õ��豣���VO
    InvoiceVO willSavedVO = getSavedVO();
    if (willSavedVO == null) {
      showHintMessage(getHeadHintText() + CommonConstant.SPACE_MARK
          + nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPP40040401-000060")/*
                                                                                           * @res
                                                                                           * "�༭��Ʊ"
                                                                                           */
          + CommonConstant.SPACE_MARK);
      return false;
    }

    timer.addExecutePhase("�õ���Ҫ�����VO");

    // showHintMessage(getHeadHintText() + CommonConstant.SPACE_MARK +
    // nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401","UPP40040401-000062")/*@res
    // "��Ʊ����"*/ + CommonConstant.SPACE_MARK + CommonConstant.BEGIN_MARK +
    // "........" + CommonConstant.END_MARK);

    // ����ArrayList
    // 0 ArrayList
    // 1 ArrayList
    // 2 ArrayList �������õĲ���ArrayList 0��ǰ����,1�Ƿ���,2�ݹ�����ʽ,3����ת�뷽ʽ
    ArrayList paraList = new ArrayList();
    paraList.add(null);
    paraList.add(null);
    // ���LIST 0���� 1��ƱVO 2���ݲ�ʱ��ArrayLIST
    ArrayList retList = null;

    InvoiceVO vos = null;

    try {
      // ����LIST
      if (paraList.size() != 3) {
        // ����LIST
        ArrayList forSettleList = new ArrayList();
        forSettleList.add(nc.ui.pub.ClientEnvironment.getInstance().getDate());
        forSettleList.add(new UFBoolean(nc.ui.pub.ClientEnvironment.getInstance().isGroup()));
        String sKey = willSavedVO.getHeadVO().getCinvoiceid();
        if (sKey == null || sKey.trim().length() == 0)
          forSettleList.add(new UFBoolean(false));
        else
          forSettleList.add(new UFBoolean(true));

        paraList.add(forSettleList);
      }
      // �Ƿ�������Ƶ���
      if (willSavedVO != null
          && willSavedVO.getHeadVO() != null
          && !isAllowedModifyByOther
          && ((InvoiceHeaderVO) willSavedVO.getParentVO()).getIbillstatus().equals(BillStatus.FREE)) {
        ((InvoiceHeaderVO) willSavedVO.getParentVO()).setCoperator(nc.ui.pub.ClientEnvironment.getInstance().getUser()
            .getPrimaryKey());
      }
      // Ϊ���������������뵱ǰ����Ա
      ((InvoiceHeaderVO) willSavedVO.getParentVO()).setCuserid(nc.ui.pub.ClientEnvironment.getInstance().getUser()
          .getPrimaryKey());
      // Ϊ�ж��Ƿ���޸ġ����������˵���
      ((InvoiceHeaderVO) willSavedVO.getParentVO()).setCoperatoridnow(nc.ui.pub.ClientEnvironment.getInstance()
          .getUser().getPrimaryKey());

      // ֧�ֹ�Ӧ�̺�׼���
      paraList.add(new Integer(0));
      paraList.add("cvendormangid");

      InvoiceVO oldVO = getInvVOs()[getCurVOPos()];
      if (oldVO != null)
        paraList.add(oldVO);
      else
        paraList.add(null);
      // ��Ʊͬ��������
      // if(!negativeAndPlusCtrl(willSavedVO)){
      // MessageDialog.showWarningDlg(this,nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401","UPPSCMCommon-000132")/*@res
      // "����"*/,nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401","UPP40040401-000063")/*@res
      // "���ڷ�Ʊ��������Դ��������������һ��,���飡"*/);
      // return false;
      // }

      timer
          .addExecutePhase(nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPP40040401-000103")/*
                                                                                                         * @res
                                                                                                         * "���淢Ʊǰ��׼��"
                                                                                                         */);

      // ���ñ�ͷ˰��
      String ntaxrate = getBillCardPanel().getHeadItem("ntaxrate").getValue();
      /**
       * ���ο������֧�� by zhaoyha at 2009.2.16
       */
      getInvokeEventProxy().beforeAction(nc.vo.scm.plugin.Action.SAVE, new AggregatedValueObject[]{willSavedVO});

      InvoiceItemVO tempVO[] = willSavedVO.getBodyVO();
      if (tempVO[0].getCupsourcebilltype() != null) {
        boolean bConfirm = false;
        willSavedVO.setUserConfirmFlag(new UFBoolean(false));
        while (!willSavedVO.getUserConfirmFlag().booleanValue()) {
          try {
            if (bConfirm)
              willSavedVO.setUserConfirmFlag(new UFBoolean(true));
            retList = (ArrayList) PfUtilClient.processActionNoSendMessage(this, "SAVEBASE",
                nc.vo.scm.pu.BillTypeConst.PO_INVOICE, ClientEnvironment.getInstance().getDate().toString(),
                willSavedVO, paraList, null, null);
          }
          catch (RwtPiToPoException ex) {
            SCMEnv.out(ex);
            if (MessageDialog.showYesNoDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401",
                "UPPSCMCommon-000270")/* @res "��ʾ" */, ex.getMessage()) == MessageDialog.ID_YES) {
              // ��������
              bConfirm = true;
            }
            else {
              return false;
            }
          }
          catch (RwtPiToScException ex) {
            SCMEnv.out(ex);
            if (MessageDialog.showYesNoDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401",
                "UPPSCMCommon-000270")/* @res "��ʾ" */, ex.getMessage()) == MessageDialog.ID_YES) {
              // ��������
              bConfirm = true;
            }
            else {
              return false;
            }
          }
          catch (nc.vo.pu.exception.MaxStockException ex) {
            SCMEnv.out(ex);
            if (MessageDialog.showYesNoDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401",
                "UPPSCMCommon-000270")/* @res "��ʾ" */, ex.getMessage()) == MessageDialog.ID_YES) {
              // ��������
              bConfirm = true;
            }
            else {
              return false;
            }
          }
          if (!bConfirm)
            break;// δ���쳣,����
        }
      }
      else {
        retList = (ArrayList) PfUtilClient.processActionNoSendMessage(this, "SAVEBASE",
            nc.vo.scm.pu.BillTypeConst.PO_INVOICE, ClientEnvironment.getInstance().getDate().toString(), willSavedVO,
            paraList, null, null);
      }
      timer
          .addExecutePhase(nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPP40040401-000029")/*
                                                                                                         * @res
                                                                                                         * "���淢Ʊ"
                                                                                                         */);

      timer
          .addExecutePhase(nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPP40040401-000104")/*
                                                                                                         * @res
                                                                                                         * "����Ʊ"
                                                                                                         */);
      vos = (InvoiceVO) retList.get(1);

      // ��������������������������������������ʱ�Ƶ��˿���ֱ���������ݣ���ʱ��Ҫˢ����ʾ
      // ArrayList arrRet = InvoiceHelper.queryForSaveAudit(sInvoiceId);
      ((InvoiceHeaderVO) vos.getParentVO()).setDauditdate(vos.getHeadVO().getDauditdate());
      ((InvoiceHeaderVO) vos.getParentVO()).setCauditpsn(vos.getHeadVO().getCauditpsn());
      ((InvoiceHeaderVO) vos.getParentVO()).setIbillstatus(vos.getHeadVO().getIbillstatus());
      ((InvoiceHeaderVO) vos.getParentVO()).setTs(vos.getHeadVO().getTs());
      // ���ñ�ͷ˰��
      UFDouble ntaxrateUF = null;
      if (ntaxrate != null && ntaxrate.length() > 0) {
        ntaxrateUF = new UFDouble(ntaxrate);
        ((InvoiceHeaderVO) vos.getParentVO()).setNtaxrate(ntaxrateUF);
      }

      if (vos != null) {
        InvoiceVO tempVOs[] = getInvVOs();
        tempVOs[getCurVOPos()] = vos;
        setInvVOs(tempVOs);
        getInvVOs()[getCurVOPos()].setSource(InvoiceVO.FROM_QUERY);
        // ��ɽ�̹�԰:��¼ҵ����־
        // Ч���Ż����Ƶ���ִ̨�� by zhaoyha at 2009.8
//        vos.getOperatelogVO().setOpratorname(nc.ui.pub.ClientEnvironment.getInstance().getUser().getUserName());
//        vos.getOperatelogVO().setCompanyname(nc.ui.pub.ClientEnvironment.getInstance().getCorporation().getUnitname());
//        vos.getOperatelogVO().setOperatorId(nc.ui.pub.ClientEnvironment.getInstance().getUser().getPrimaryKey());
//        Operlog operlog = new Operlog();
//        operlog.insertBusinessExceptionlog(vos, "����", "����", nc.vo.scm.funcs.Businesslog.MSGMESSAGE,
//            nc.vo.scm.pu.BillTypeConst.PO_INVOICE, nc.ui.sm.cmenu.Desktop.getApplet().getParameter("USER_IP"));
      }

    }
    catch (ValidationException e) {
      SCMEnv.out(e);
      MessageDialog.showErrorDlg(this,
          nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPPSCMCommon-000270")/*
                                                                                         * @res
                                                                                         * "��ʾ"
                                                                                         */, e.getMessage());
      return false;
    }
    catch (Exception e) {
      SCMEnv.out(e);
      if (e instanceof BusinessException
          || e instanceof java.rmi.RemoteException
          || (e.getMessage() != null && (e.getMessage().indexOf(
              nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UC000-0000275")/*
                                                                                     * @res
                                                                                     * "��Ӧ��"
                                                                                     */) >= 0
              || e.getMessage()
                  .indexOf(nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPP40040401-000165")/*
                                                                                                         * @res
                                                                                                         * "�ɹ�"
                                                                                                         */) >= 0
              || e.getMessage()
                  .indexOf(nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPP40040401-000166")/*
                                                                                                         * @res
                                                                                                         * "��Ʊ"
                                                                                                         */) >= 0
              || e.getMessage()
                  .indexOf(nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPP40040401-000110")/*
                                                                                                         * @res
                                                                                                         * "����"
                                                                                                         */) >= 0
              || e.getMessage()
                  .indexOf(nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPP40040401-000167")/*
                                                                                                         * @res
                                                                                                         * "��׼"
                                                                                                         */) >= 0 || e
              .getMessage()
              .indexOf(nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPP40040401-000168")/*
                                                                                                     * @res
                                                                                                     * "����"
                                                                                                     */) >= 0))) {
        MessageDialog.showErrorDlg(this,
            nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPPSCMCommon-000270")/*
                                                                                           * @res
                                                                                           * "��ʾ"
                                                                                           */, e
                .getMessage());
      }
      else {
        MessageDialog.showErrorDlg(this,
            nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPPSCMCommon-000270")/*
                                                                                           * @res
                                                                                           * "��ʾ"
                                                                                           */,
            nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPP40040401-000064")/*
                                                                                           * @res
                                                                                           * "����δ�ɹ���"
                                                                                           */);
      }
      return false;
    }

    // ���н�����ʾ״̬�л�:
    // �����������ת��,��ӻ�����ȥ�����ŷ�Ʊ,��ת���б�״̬
    if (willSavedVO.getSource() == InvoiceVO.FROM_HAND) {
      // ������VO
      InvoiceVO resultVO = null;
      if (!PfUtilClient.isSuccess()) { // �û�ѡ�񡰷�
        removeOneFromInvVOs(getCurVOPos());
        // ���ÿ�Ƭ��������
        setCurOperState(getBillBrowseState());
        setVOToBillPanel();
      }
      else {
        // resultVO = (InvoiceVO) retList.get(1);
        resultVO = vos;
        resultVO.setSource(InvoiceVO.FROM_QUERY);
        getInvVOs()[getCurVOPos()] = resultVO;
        setCurOperState(getBillBrowseState());
        // ���ÿ�Ƭ��������
        setVOToBillPanel();
      }
    }
    else if (willSavedVO.getSource() == InvoiceVO.FROM_QUERY) {
      // ������VO
      InvoiceVO resultVO = null;
      if (!PfUtilClient.isSuccess()) { // �û�ѡ�񡰷�
        setCurOperState(getBillBrowseState());
        // ���ÿ�Ƭ��������
        setVOToBillPanel();
      }
      else {
        // resultVO = (InvoiceVO) retList.get(1);
        resultVO = vos;
        getInvVOs()[getCurVOPos()] = resultVO;
        setCurOperState(getBillBrowseState());
        if(getListOperState()==STATE_LIST_FROM_BILLS)
          setListOperState(STATE_LIST_NORMAL);
        // ���ÿ�Ƭ��������
        setVOToBillPanel();
      }
    }
    else {

      /** ***********�޸�ת�����������**************** */
      // ���ɹ�ת���ĵ��ݱ����ڶ�Ӧλ�õĻ�����
      // InvoiceVO resultVO = (InvoiceVO) retList.get(1);
      InvoiceVO resultVO = vos;
      resultVO.setSource(InvoiceVO.FROM_QUERY);
      // getInvVOs()[getCurVOPos()] = resultVO;cunpos
      if (cunpos == 0)
        getInvVOs()[getCurVOPos()] = resultVO;
      else
        getInvVOs()[cunpos] = resultVO;

      if (m_vSavedVO == null)
        m_vSavedVO = new Vector();
      m_vSavedVO.addElement(resultVO);
      removeOneFromInvVOs(getCurVOPos());

      // ����Ƿ��е���û��ת�����.
      if (getInvVOs() != null && getInvVOs().length > 0) {
        // ���ò���״̬
        setCurOperState(getListOperState());
        shiftShowModeTo(INV_PANEL_LIST);
        // �����б��������
        setSpiltVOsToListPanel();
      }
      else {
        setCurOperState(getBillBrowseState());
        setListOperState(STATE_LIST_NORMAL);

        InvoiceVO tempVO[] = new InvoiceVO[m_vSavedVO.size()];
        m_vSavedVO.copyInto(tempVO);
        setInvVOs(tempVO);
        setCurVOPos(tempVO.length - 1);
        m_vSavedVO = null;

        // ���ÿ�Ƭ��������
        setVOToBillPanel();
      }
      cunpos = 0;
    }
    timer.addExecutePhase("��������ʾ����");
    /**
     * ���ο������֧�� by zhaoyha at 2009.2.16
     */
    try {
      getInvokeEventProxy().afterAction(nc.vo.scm.plugin.Action.SAVE, new AggregatedValueObject[]{willSavedVO});
    }
    catch (BusinessException e) {
      //��־�쳣
      nc.vo.scm.pub.SCMEnv.out(e);
      //���淶�׳��쳣
      return false;
    }


    // showHintMessage(getHeadHintText() + CommonConstant.SPACE_MARK +
    // nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401","UPPSCMCommon-000006")/*@res
    // "����ɹ�"*/ + CommonConstant.SPACE_MARK);
    timer.showAllExecutePhase("�ɹ���Ʊ�������");
    m_bAdd = false;
    m_bCopy = false;
    return true;

  }

  /**
   * �б�״̬��ѡ�����з�Ʊ
   * 
   * @param ����˵��
   * @return ����ֵ
   * @exception �쳣����
   * @see ��Ҫ�μ�����������
   * @since �������һ���汾���˷�������ӽ���������ѡ��
   * @deprecated �÷����������һ���汾���Ѿ������������滻������ѡ��
   */
  private void onSelectAll() {

    int iLen = getInvVOs().length;
    // ��Ϊȫ��ѡ��
    getBillListPanel().getHeadTable().setRowSelectionInterval(0, iLen - 1);
  }

  /**
   * �б�״̬�����з�Ʊ����������
   * 
   * @param ����˵��
   * @return ����ֵ
   * @exception �쳣����
   * @see ��Ҫ�μ�����������
   * @since �������һ���汾���˷�������ӽ���������ѡ��
   * @deprecated �÷����������һ���汾���Ѿ������������滻������ѡ��
   */
  private void onSelectNo() {
    int iLen = getInvVOs().length;
    // ��Ϊȫ��ѡ��
    getBillListPanel().getHeadTable().removeRowSelectionInterval(0, iLen - 1);
  }

  /**
   * �˴����뷽��˵���� �������ڣ�(2003-10-29 12:10:06)
   */
  private boolean onSendAudit(InvoiceVO vo) {
    // //������������������
    // String strOperPk =
    // nc.ui.pub.ClientEnvironment.getInstance().getUser().getPrimaryKey();
    // UFDate today = nc.ui.pub.ClientEnvironment.getInstance().getDate();

    String billid = null;
    if (vo != null && vo.getHeadVO() != null) {
      billid = vo.getHeadVO().getCinvoiceid();
    }

    boolean isNeedSendToAuditQ = BusiBillManageTool.isNeedSendToAudit(ScmConst.PO_Invoice, getPk_corp(),
        getCurBizeType(), billid, getCurOperator());
    try {
      // �༭״̬���󣽡����桱��������
      if (vo == null && m_btnBillMaintain.isEnabled() == true) {
        boolean bContinue = onSave();
        if (!bContinue) {
          return false;
        }
        vo = getInvVOs()[getCurVOPos()];
      }

      else if (getCurOperState() == STATE_EDIT && isNeedSendToAuditQ && vo.getHeadVO().getIbillstatus() != null
          && (vo.getHeadVO().getIbillstatus().intValue() == 0 || vo.getHeadVO().getIbillstatus().intValue() == 4)) {
        boolean bContinue = onSave();
        if (!bContinue) {
          return false;
        }
        vo = getInvVOs()[getCurVOPos()];
      }

      if (isNeedSendToAuditQ && vo.getHeadVO().getIbillstatus() != null
          && (vo.getHeadVO().getIbillstatus().intValue() == 0 || vo.getHeadVO().getIbillstatus().intValue() == 4)) {
        
        Object[] retValues = (Object[]) PfUtilClient.processAction("SAVE", nc.vo.scm.pu.BillTypeConst.PO_INVOICE,
            ClientEnvironment.getInstance().getDate().toString(), vo);
        //
        InvoiceVO resultVO = null;
        if (PfUtilClient.isSuccess()) {
          updateCacheVo((Map<String, Map<String, Object>>) retValues[1]);
//          resultVO = InvoiceHelper.findByPrimaryKey(getInvVOs()[getCurVOPos()].getHeadVO().getPrimaryKey());
//          getInvVOs()[getCurVOPos()] = resultVO;
          
        }
      }
      m_btnSendAudit.setEnabled(false);// ����
      m_btnInvBillAudit.setEnabled(false);// ����
      m_btnInvBillUnAudit.setEnabled(true);// ����
      // isAlreadySendToAudit =true;
    }
    catch (Exception e) {
      SCMEnv.out(e);
      MessageDialog.showErrorDlg(this,
          nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPPSCMCommon-000270")/*
                                                                                         * @res
                                                                                         * "��ʾ"
                                                                                         */, e.getMessage());
      showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPP40040401-000257")/*
                                                                                                     * @res
                                                                                                     * "����δ�ɹ���"
                                                                                                     */);
      return false;
    }
    return true;
  }

  /**
   * �ӻ�����ȥ��һ�ŵ���
   * 
   * @param ����˵��
   * @return ����Ϊ�գ����أ�1 ���ߵ������һ��,���ص�һ��. ���򷵻�ԭ��λ��.
   * @exception �쳣����
   * @see ��Ҫ�μ�����������
   * @since �������һ���汾���˷�������ӽ���
   * @return nc.vo.pi.InvoiceVO[]
   */
  private int removeOneFromInvVOs(int removedPos) {

    if (removedPos < 0 || removedPos >= getInvVOs().length)
      return -1;

    if (getInvVOs().length == 1) {
      setInvVOs(null);
      setCurVOPos(-1);
      return -1;
    }
    else {
      InvoiceVO[] vos = new InvoiceVO[getInvVOs().length - 1];
      int j = 0;
      for (int i = 0; i < getInvVOs().length; i++) {
        if (i != removedPos)
          vos[j++] = getInvVOs()[i];
      }
      setInvVOs(vos);

      // ԭ����ʾ�������һ��
      if (removedPos == getInvVOs().length) {
        setCurVOPos(getInvVOs().length - 1);
        return 0;
      }
      else {
        setCurVOPos(removedPos);
        return removedPos;
      }

    }
  }

  /**
   * �ӻ�����ȥ�������ŵ���
   * 
   * @param ����˵��
   * @return ����Ϊ�գ����أ�1 �����õ�ǰVOλ��Ϊ��һ��
   * @exception �쳣����
   * @see ��Ҫ�μ�����������
   * @since �������һ���汾���˷�������ӽ���
   * @return nc.vo.pi.InvoiceVO[]
   */
  private int removeSomeFromInvVOs(Integer removedPos[]) {

    if (removedPos == null)
      return 0;

    // ��ΪremovedPos����δ��������һHashtable��
    Hashtable hashRevIndex = new Hashtable();
    for (int i = 0; i < removedPos.length; i++) {
      hashRevIndex.put(removedPos[i], "");
    }

    // �����ķ���һVECTOR��
    Vector reservedVEC = new Vector();

    if (getInvVOs() != null && getInvVOs().length > 0) {
      for (int i = 0; i < getInvVOs().length; i++) {
        if (!hashRevIndex.containsKey(new Integer(i)))
          reservedVEC.addElement(getInvVOs()[i]);
      }
    }
    // ��ȫ��ȥ��
    if (reservedVEC.size() == 0) {
      setInvVOs(null);
      return -1;
    }

    // ���軺��
    InvoiceVO[] vos = new InvoiceVO[reservedVEC.size()];
    reservedVEC.copyInto(vos);
    setInvVOs(vos);
    setCurVOPos(0);
    return 0;
  }

  /**
   * �˴����뷽��˵���� �������ڣ�(2001-9-19 9:38:47)
   * 
   * @param newBillBrowseState
   *          java.lang.String
   */
  private void setBillBrowseState(int newBillBrowseState) {
    m_nBillBrowseState = newBillBrowseState;
  }

  /**
   * ���ߣ���ӡ�� ���ܣ��԰�ť״̬��PANEL�Ƿ���ý������� �������� ���أ��� ���⣺�� ���ڣ�(2002-3-13 11:39:21)
   * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
   */
  private void setButtonsAndPanelState() {
    int iVal = -999;// ֧�ֲ�ҵ��������չ
    boolean isNeedSendToAuditQ = false;
    if (getCurOperState() == STATE_INIT) {
      // showHintMessage(getHeadHintText() + CommonConstant.SPACE_MARK +
      // nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401","UPP40040401-000065")/*@res
      // "��ʼ��"*/ + CommonConstant.SPACE_MARK);
      setButtonsStateInit();
      getBillCardPanel().setEnabled(false);
      iVal = 0;
    }
    else if (getCurOperState() == STATE_BROWSE_NORMAL) {
      // showHintMessage(getHeadHintText() + CommonConstant.SPACE_MARK +
      // nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401","UPP40040401-000032")/*@res
      // "�����Ʊ"*/ + CommonConstant.SPACE_MARK);
      isNeedSendToAuditQ = setSendAuditBtnState();
      setButtonsStateBrowseNormal();
      Integer iBillStatus = new Integer(88);
      String billid = null;
      if (getInvVOs() != null && getCurVOPos() > -1) {
        InvoiceVO vo = getInvVOs()[getCurVOPos()];

        // Integer iBillStatus = new Integer(88);
        if (vo != null && vo.getHeadVO() != null) {
          iBillStatus = vo.getHeadVO().getIbillstatus();
          billid = vo.getHeadVO().getCinvoiceid();
        }
      }

      // if((iBillStatus.compareTo(nc.vo.scm.pu.BillStatus.FREE) == 0 ||
      // (iBillStatus.compareTo(nc.vo.scm.pu.BillStatus.AUDITFAIL) == 0))){
      // if(iBillStatus.compareTo(nc.vo.scm.pu.BillStatus.FREE) == 0 ){
      // m_btnSendAudit.setEnabled(isNeedSendToAuditQ);}
      // else if((iBillStatus.compareTo(nc.vo.scm.pu.BillStatus.AUDITFAIL) == 0)
      // || (iBillStatus.compareTo(nc.vo.scm.pu.BillStatus.AUDITFAIL) == 0)){
      // m_btnSendAudit.setEnabled(false);
      // }
      // }
      //������  ����û�������ˣ���������������������
      if (!isNeedSendToAuditQ 
          && (iBillStatus.compareTo(nc.vo.scm.pu.BillStatus.AUDITING) == 0) 
          && PuPubVO.getString_TrimZeroLenAsNull(getInvVOs()[getCurVOPos()].getHeadVO().getCauditpsn()) == null) {
        m_btnInvBillAudit.setEnabled(true);
        m_btnInvBillUnAudit.setEnabled(false);
        // }else {
        // m_btnInvBillAudit.setEnabled(false);
        // m_btnInvBillUnAudit.setEnabled(true);
      }//������  �������ˣ�������������������
      else if(!isNeedSendToAuditQ 
          && (iBillStatus.compareTo(nc.vo.scm.pu.BillStatus.AUDITING) == 0) 
          && PuPubVO.getString_TrimZeroLenAsNull(getInvVOs()[getCurVOPos()].getHeadVO().getCauditpsn()) != null){
        m_btnInvBillAudit.setEnabled(true);
          m_btnInvBillUnAudit.setEnabled(true);
      }
      getBillCardPanel().setEnabled(false);
      iVal = 1;
      if (getInvVOs() == null || getInvVOs().length == 0)
        setButtonsStateInit();
    }
    else if (getCurOperState() == STATE_EDIT) {
      // showHintMessage(getHeadHintText() + CommonConstant.SPACE_MARK +
      // nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401","UPP40040401-000060")/*@res
      // "�༭��Ʊ"*/ + CommonConstant.SPACE_MARK);

      setButtonsStateEdit();

      getBillCardPanel().setEnabled(true);

      Integer iBillStatus = new Integer(88);
      String cbizType = null;
      String billid = null;
      if (getInvVOs() != null && getCurVOPos() > -1) {
        InvoiceVO vo = getInvVOs()[getCurVOPos()];
        if (vo != null && vo.getHeadVO() != null) {
          iBillStatus = vo.getHeadVO().getIbillstatus();
          cbizType = vo.getHeadVO().getCbiztype();
          billid = vo.getHeadVO().getCinvoiceid();
        }
      }
      if (m_bAdd) {
        isNeedSendToAuditQ = BusiBillManageTool.isNeedSendToAudit(ScmConst.PO_Invoice, getPk_corp(), 
            m_bizButton.getTag(),
            null, getClientEnvironment().getUser().getPrimaryKey());
      }
      else {
        isNeedSendToAuditQ = setSendAuditBtnState();
      }

      if (m_bAdd
          || (iBillStatus.compareTo(nc.vo.scm.pu.BillStatus.FREE) == 0 || (iBillStatus
              .compareTo(nc.vo.scm.pu.BillStatus.AUDITFAIL) == 0))) {
        m_btnSendAudit.setEnabled(isNeedSendToAuditQ);
      }

      // ������Ϣ���ɱ༭
      getBillCardPanel().getBodyItem("nmoney").setEnabled(false);
      getBillCardPanel().getBodyItem("ntaxmny").setEnabled(false);
      getBillCardPanel().getBodyItem("nsummny").setEnabled(false);
      m_btnAudit.setEnabled(false);

      // ����ɢ�����۱����ʵȵĿ���״̬
      setEditableWhenEdit();
      iVal = 2;

    }
    else if (getCurOperState() == STATE_LIST_NORMAL) {
      // showHintMessage(getHeadHintText() + CommonConstant.SPACE_MARK +
      // nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401","UPP40040401-000032")/*@res
      // "�����Ʊ"*/ + CommonConstant.SPACE_MARK);
      setButtonsStateListNormal();
      getBillCardPanel().setEnabled(false);
      setButtonsStateBrowseNormal();
      iVal = 3;
    }
    else if (getCurOperState() == STATE_LIST_FROM_BILLS) {
      // showHintMessage(getHeadHintText() + CommonConstant.SPACE_MARK +
      // nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401","UPP40040401-000032")/*@res
      // "�����Ʊ"*/ + CommonConstant.SPACE_MARK);
      setButtonsStateListFromBills();
      getBillCardPanel().setEnabled(false);
      iVal = 4;
    }
    setExtendBtnsStat(iVal);
  }

  /**
   * ���ߣ���ӡ�� ���ܣ������״̬�İ�ť�������� �������� ���أ��� ���⣺�� ���ڣ�(2002-3-13 11:39:21)
   * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־�� 2002-07-16 wyf ���������δͨ���Ĵ��� 2002-09-26 wyf
   * ��������ⷢƱ�Ĵ���ͬ��������Ʊ�������Ʋ�����
   */
  private void setButtonsStateBrowseNormal() {
    if (getCurPanelMode() == INV_PANEL_CARD) {
      m_btnInvBillBusiType.setEnabled(true);// ҵ������
      m_btnInvSelectAll.setEnabled(false);
      m_btnInvDeselectAll.setEnabled(false);
      m_btnInvBillConversion.setEnabled(false);// ����ת��
      m_btnInvShift.setName(NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000464")/** @res* "�б���ʾ" */
      );
    }
    else if (getCurPanelMode() == INV_PANEL_LIST) {
      m_btnInvBillBusiType.setEnabled(false);// ҵ������
      m_btnInvSelectAll.setEnabled(true);
      m_btnInvDeselectAll.setEnabled(true);
      m_btnInvShift.setName(NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000463")/** @res* "��Ƭ��ʾ" */
      );
    }

    m_btnInvBillExcute.setEnabled(true);
    // ����
    if (getCurPanelMode() == INV_PANEL_LIST || m_btnInvBillBusiType.isVisible() == false || getCurBizeType() == null) {
      // û��ҵ�����������Ӱ�ť����
      m_btnInvBillNew.setEnabled(false);
    }
    else if (getCurPanelMode() == INV_PANEL_CARD) {
      m_btnInvBillNew.setEnabled(true);
    }
    // ����
    m_btnInvBillSave.setEnabled(false);
    // ����
    m_btnInvBillCancel.setEnabled(false);
    // �в���
    m_btnInvBillRowOperation.setEnabled(false);
    // ��ӡ
    m_btnInvBillPrint.setEnabled(true);
    // ��ѯ
    m_btnInvBillQuery.setEnabled(true);
    // �б�
    m_btnInvShift.setEnabled(true);

    // �����ż�ȡ��
    m_btnHqhp.setEnabled(false);
    // ˢ��
    if (isEverQueryed()) {
      m_btnInvBillRefresh.setEnabled(true);
    }
    else {
      m_btnInvBillRefresh.setEnabled(false);
    }
    // ��ҳ
    // m_btnInvBillPageOperation.setEnabled(true);
    m_btnQueryForAudit.setEnabled(true);
    // �ϲ���ʾ
    btnBillCombin.setEnabled(true);
    // ������ĩ��
    if (getInvVOs() == null) {
      // ����
      m_btnInvBillDiscard.setEnabled(false);
      // �޸�
      m_btnInvBillModify.setEnabled(false);
      // ���ݸ���
      m_btnInvBillCopy.setEnabled(false);

      // ��ҳ
      // m_btnInvBillPageOperation.setEnabled(false);
      m_btnInvBillGoFirstOne.setEnabled(false);
      m_btnInvBillGoPreviousOne.setEnabled(false);
      m_btnInvBillGoNextOne.setEnabled(false);
      m_btnInvBillGoLastOne.setEnabled(false);

      // �ĵ�����
      m_btnDocManage.setEnabled(false);
      // ���鰴ť
      m_btnLnkQuery.setEnabled(false);
    }
    else {

      // �������鰴ť״̬
      m_btnLnkQuery.setEnabled(true);
      // �ĵ�����
      m_btnDocManage.setEnabled(true);

      // ================================���ݸ���:
      // 1����ǰ���ݵ�ҵ������ֻ�п����Ƶ���ʱ���ſɸ��ơ�
      // 2�������ǰ����Ϊ���ⵥ�ݣ����ɸ��ơ�
      // wyf 2002-09-27 modify begin
      // if(isCouldMakedForABizType(getInvVOs()[getCurVOPos()].getHeadVO().getCbiztype())){
      if (getInvVOs() != null && getInvVOs().length > 0 && getCurVOPos() > -1
          && isCouldMakedForABizType(getInvVOs()[getCurVOPos()].getHeadVO().getCbiztype())
          && !getInvVOs()[getCurVOPos()].isVirtual() && getCurPanelMode() != INV_PANEL_LIST) {
        // wyf 2002-09-27 modify end
        m_btnInvBillCopy.setEnabled(true);
      }
      else {
        m_btnInvBillCopy.setEnabled(false);
      }
      
      // ����
      Integer iBillStatus = -1;
      if (getInvVOs() != null && getCurVOPos() > -1) {
        InvoiceVO vo = getInvVOs()[getCurVOPos()];

        if (vo != null && vo.getHeadVO() != null) {
          iBillStatus = vo.getHeadVO().getIbillstatus();
        }
      }
      boolean isNeedSendToAuditQ = isNeedSendAudit(iBillStatus);

      // ================================���ϡ��޸�
      m_btnInvBillModify.setEnabled(true);
      m_btnInvBillDiscard.setEnabled(true);
      if (getCurVOPos() != -1) {
        int intBillStatus = getInvVOs()[getCurVOPos()].getHeadVO().getIbillstatus().intValue();
        if (intBillStatus == 0) { // ����

          if (m_btnSendAudit.isEnabled()) {// ����
            m_btnInvBillAudit.setEnabled(false);// ����
            m_btnInvBillUnAudit.setEnabled(false);// ����
          }
          else if (m_btnInvBillAudit.isEnabled()) {// ����
            m_btnInvBillUnAudit.setEnabled(false);
          }
          else if (m_btnInvBillUnAudit.isEnabled()) {
            m_btnInvBillAudit.setEnabled(false);
          }
          else if (!m_btnInvBillUnAudit.isEnabled()) {
            m_btnInvBillAudit.setEnabled(true);
          }

          if ((iBillStatus.compareTo(nc.vo.scm.pu.BillStatus.FREE) == 0 || (iBillStatus
              .compareTo(nc.vo.scm.pu.BillStatus.AUDITFAIL) == 0))) {
            if (isNeedSendToAuditQ) {
              m_btnSendAudit.setEnabled(true);
            }
            else {
              m_btnSendAudit.setEnabled(false);
              m_btnInvBillAudit.setEnabled(true);
              m_btnInvBillUnAudit.setEnabled(false);
              m_btnInvBillModify.setEnabled(true);// �޸�
              m_btnInvBillDiscard.setEnabled(true);// ����
            }
          }
          m_btnInvBillModify.setEnabled(true);// �޸�
          m_btnInvBillDiscard.setEnabled(true);// ����
        }
        else if (intBillStatus == 0 || intBillStatus == 4) { // ���ɡ�����δͨ��
          m_btnInvBillAudit.setEnabled(true);
          m_btnInvBillUnAudit.setEnabled(false);
          m_btnInvBillModify.setEnabled(true);// �޸�
          m_btnInvBillDiscard.setEnabled(true);// ����

        }
        else if (intBillStatus == 2) {// ��������
          m_btnInvBillAudit.setEnabled(true);
          if(PuPubVO.getString_TrimZeroLenAsNull(getInvVOs()[getCurVOPos()].getHeadVO().getCauditpsn()) != null){
            m_btnInvBillUnAudit.setEnabled(true);
          }else{
            m_btnInvBillUnAudit.setEnabled(false);
          }
          m_btnInvBillDiscard.setEnabled(false);// ����
          //V55�����������Ϊ�գ�����ʱ�ǵ�һ�����������������������޸�
          BillItem item = getBillCardPanel().getHeadItem("cauditpsn");
          if(item == null){
            item = getBillCardPanel().getTailItem("cauditpsn");
          }
          if(item != null){
              String strAuditPsn = PuPubVO.getString_TrimZeroLenAsNull(item.getValueObject());            
            m_btnInvBillModify.setEnabled(strAuditPsn == null);
          }
        }
        else if (intBillStatus == 3) {// ����ͨ��
          m_btnInvBillAudit.setEnabled(false);
          m_btnInvBillUnAudit.setEnabled(true);
          m_btnInvBillModify.setEnabled(false);// �޸�
          m_btnInvBillDiscard.setEnabled(false);// ����
        }
      }

      if (getCurVOPos() < 0) {// �б�״̬��ȫѡ��ȫ��ʱ
        m_btnInvBillModify.setEnabled(false);
        return;

      }

      // �ѽ���ķ�Ʊ�������޸ģ���������
      double d1 = 0.0;
      InvoiceItemVO bodyVO[] = getInvVOs()[getCurVOPos()].getBodyVO();
      if (bodyVO != null && bodyVO.length > 0) {
        for (int i = 0; i < bodyVO.length; i++) {
          d1 = 0.0;
          if (getInvVOs()[getCurVOPos()].getBodyVO()[i].getNaccumsettmny() != null) {
            d1 = getInvVOs()[getCurVOPos()].getBodyVO()[i].getNaccumsettmny().doubleValue();
          }
          if (d1 != 0.0) {
            m_btnInvBillModify.setEnabled(false);
            m_btnInvBillDiscard.setEnabled(false);
            break;
          }
        }
      }
      // �����ɵ�:��������
      if (getInvVOs()[getCurVOPos()].getHeadVO().getCinvoiceid() == null
          || getInvVOs()[getCurVOPos()].getHeadVO().getCinvoiceid().trim().length() < 1) {
        m_btnInvBillDiscard.setEnabled(false);
      }
      // ��������:�����޸ģ���������
      Integer iStatus = getInvVOs()[getCurVOPos()].getHeadVO().getIbillstatus();
      if (iStatus != null && iStatus.compareTo(nc.vo.scm.pu.BillStatus.FREE) != 0
          && iStatus.compareTo(nc.vo.scm.pu.BillStatus.AUDITFAIL) != 0) { // ����,����δͨ��
        m_btnInvBillDiscard.setEnabled(false);
      }
      // ���ⷢƱ�������޸ģ���������
      // since v55, ���������������������ⷢƱֻ�ڽ���ʱά�����������޷�Ʊ���㣬ɾ�������Ͻ��㵥
      if (getInvVOs()[getCurVOPos()].isVirtual()) {
        m_btnInvBillModify.setEnabled(false);
        m_btnInvBillDiscard.setEnabled(false);
        m_btnInvBillAudit.setEnabled(false);
        m_btnInvBillUnAudit.setEnabled(false);
      }

      // ================================��ҳ
      if (getInvVOs().length == 1) {
        // ֻ��һ�ŷ�Ʊ
        // m_btnInvBillPageOperation.setEnabled(false);
        m_btnInvBillGoFirstOne.setEnabled(false); // ����
        m_btnInvBillGoPreviousOne.setEnabled(false); // ����
        m_btnInvBillGoNextOne.setEnabled(false); // ����
        m_btnInvBillGoLastOne.setEnabled(false); // ĩ��
      }
      else {
        // ���ǵ�һ�ŷ�Ʊ
        // m_btnInvBillPageOperation.setEnabled(true);
        if (getCurVOPos() == 0) {
          m_btnInvBillGoFirstOne.setEnabled(false); // ����
          m_btnInvBillGoPreviousOne.setEnabled(false); // ����
          m_btnInvBillGoNextOne.setEnabled(true); // ����
          m_btnInvBillGoLastOne.setEnabled(true); // ĩ��
        }
        // �������һ�ŷ�Ʊ
        else if (getCurVOPos() == getInvVOs().length - 1) {
          m_btnInvBillGoFirstOne.setEnabled(true); // ����
          m_btnInvBillGoPreviousOne.setEnabled(true); // ����
          m_btnInvBillGoNextOne.setEnabled(false); // ����
          m_btnInvBillGoLastOne.setEnabled(false); // ĩ��
        }
        // �м��κ�һ��
        else {
          m_btnInvBillGoFirstOne.setEnabled(true); // ����
          m_btnInvBillGoPreviousOne.setEnabled(true); // ����
          m_btnInvBillGoNextOne.setEnabled(true); // ����
          m_btnInvBillGoLastOne.setEnabled(true); // ĩ��
        }
      }
    }
    btnBillAddContinue.setEnabled(false);
    setButtonsBAPFlag();
    setButtonsList();

  }

  /**
   * �԰�ť״̬��������
   * 
   * @param
   * @return
   * @exception �쳣����
   * @see ��Ҫ�μ�����������
   * @since �������һ���汾���˷�������ӽ���������ѡ��
   * @deprecated �÷����������һ���汾���Ѿ������������滻������ѡ��
   */
  private void setButtonsStateEdit() {
    // ҵ������
    m_btnInvBillBusiType.setEnabled(false);
    // ����
    m_btnInvBillNew.setEnabled(false);
    // �޸�
    m_btnInvBillModify.setEnabled(false);
    // ����
    m_btnInvBillDiscard.setEnabled(false);
    // ����
    m_btnInvBillSave.setEnabled(true);
    // ����
    m_btnInvBillCopy.setEnabled(false);
    // ����
    m_btnInvBillCancel.setEnabled(true);

    // ����
    m_btnInvBillAudit.setEnabled(false);
    // ����
    m_btnInvBillUnAudit.setEnabled(false);
    // �в���
    m_btnInvBillRowOperation.setEnabled(true);
    // ���ھ����õ����Ƿ�������е�
    setCouldMaked(isCouldMakedForABizType(getCurBizeType()));
    // ======�˵�
    m_btnInvBillAddRow.setEnabled(isCouldMaked());
    m_btnInvBillCopyRow.setEnabled(isCouldMaked());
    m_btnInvBillPasteRow.setEnabled(isCouldMaked());
    m_btnInvBillInsertRow.setEnabled(isCouldMaked());
    // �κ�ʱ�����ɾ��
    if (getBillCardPanel().getRowCount() <= 0) {
      m_btnInvBillDeleteRow.setEnabled(false);
    }
    else {
      m_btnInvBillDeleteRow.setEnabled(true);
    }

    // ��ӡ
    m_btnInvBillPrint.setEnabled(false);
    // ��ѯ
    m_btnInvBillQuery.setEnabled(false);
    // �б�
    m_btnInvShift.setEnabled(false);
    // ������ĩ��
    // ��ҳ
    m_btnInvBillGoFirstOne.setEnabled(false);
    m_btnInvBillGoPreviousOne.setEnabled(false);
    m_btnInvBillGoNextOne.setEnabled(false);
    m_btnInvBillGoLastOne.setEnabled(false);
    // ����
    if (m_bAdd) {// �������ӵ���
      m_btnInvSelectAll.setEnabled(false);
      m_btnInvDeselectAll.setEnabled(false);
      // m_btnInvBillAssist.setEnabled(false);
    }
    else {
      m_btnInvSelectAll.setEnabled(true);
      m_btnInvDeselectAll.setEnabled(true);
      m_btnInvBillAudit.setEnabled(false);
      m_btnInvBillAssist.setEnabled(true);
      m_btnInvBillRefresh.setEnabled(false);
      btnBillCombin.setEnabled(false);
      m_btnQueryForAudit.setEnabled(false);
      m_btnInvBillPrint.setEnabled(false);
      m_btnInvBillPreview.setEnabled(false);
      m_btnLnkQuery.setEnabled(false);
      m_btnDocManage.setEnabled(false);
      m_btnHqhp.setEnabled(true);// �����ż�ȡ��
    }

    // ת����ť�ɼ�
    m_btnInvBillConversion.setEnabled(false);

    // ����
    Integer iBillStatus = -1;
    if (getInvVOs() != null && getCurVOPos() > -1) {
      InvoiceVO vo = getInvVOs()[getCurVOPos()];

      if (vo != null && vo.getHeadVO() != null) {
        iBillStatus = vo.getHeadVO().getIbillstatus();
      }
    }
    boolean isNeedSendToAuditQ = isNeedSendAudit(iBillStatus);

    if (isNeedSendToAuditQ
        && (iBillStatus.compareTo(nc.vo.scm.pu.BillStatus.FREE) == 0 || (iBillStatus
            .compareTo(nc.vo.scm.pu.BillStatus.AUDITFAIL) == 0))) {
      m_btnSendAudit.setEnabled(true);
    }
    setButtonsBAPFlag();

    createAddContinueBtn();
  }

  /**
   * �԰�ť״̬��������
   * 
   * @param
   * @return
   * @exception �쳣����
   * @see ��Ҫ�μ�����������
   * @since �������һ���汾���˷�������ӽ���������ѡ��
   * @deprecated �÷����������һ���汾���Ѿ������������滻������ѡ��
   */
  private void setButtonsStateInit() {
	 
    m_btnInvBillCopy.setEnabled(false);
    // �б��µ�ˢ�°�ť
    m_btnInvBillRefresh.setEnabled(false);
    m_btnLnkQuery.setEnabled(false);

    if (getCurPanelMode() == INV_PANEL_CARD) {
      m_btnInvBillBusiType.setEnabled(true);// ҵ������
    }
    else if (getCurPanelMode() == INV_PANEL_LIST) {
      m_btnInvBillBusiType.setEnabled(false);// ҵ������
    }

    // ����
    if (getCurPanelMode() == INV_PANEL_LIST || m_btnInvBillBusiType.isVisible() == false || getCurBizeType() == null) {
      // û��ҵ�����������Ӱ�ť����
      m_btnInvBillNew.setEnabled(false);
    }
    else if (getCurPanelMode() == INV_PANEL_CARD) {
      m_btnInvBillNew.setEnabled(true);
    }
    // �޸�
    m_btnInvBillModify.setEnabled(false);
    // ����
    m_btnInvBillSave.setEnabled(false);
    // ����
    m_btnInvBillDiscard.setEnabled(false);
    // ����
    m_btnInvBillCancel.setEnabled(false);
    // ����
    m_btnInvBillAudit.setEnabled(false);
    // ����
    m_btnInvBillUnAudit.setEnabled(false);
    // �в���
    m_btnInvBillRowOperation.setEnabled(false);

    // �����ż�ȡ��
    m_btnHqhp.setEnabled(false);

    // //��ӡ
    // m_btnInvBillPrint.setEnabled(true);
    // ��ѯ
    m_btnInvBillQuery.setEnabled(true);
    // �б�
    m_btnInvShift.setEnabled(true);
    // ������ĩ��
    // ��ҳ
    // m_btnInvBillPageOperation.setEnabled(false);
    m_btnInvBillGoFirstOne.setEnabled(false);
    m_btnInvBillGoPreviousOne.setEnabled(false);
    m_btnInvBillGoNextOne.setEnabled(false);
    m_btnInvBillGoLastOne.setEnabled(false);
    // //������ѯ
    // m_btnInvBillAssist.setEnabled(false);
    // �ĵ�����
    m_btnDocManage.setEnabled(false);
    // ����
    m_btnSendAudit.setEnabled(false);
    // �ϲ���ʾ
    btnBillCombin.setEnabled(false);
    // ״̬��ѯ
    m_btnQueryForAudit.setEnabled(false);

    btnBillAddContinue.setEnabled(false);

    setButtonsBAPFlag();
  }

  /**
   * ����������������Կ�Ƭ״̬���б�״̬���ظ���ť��������
   * <p>
   * <b>examples:</b>
   * <p>
   * ʹ��ʾ��
   * <p>
   * <b>����˵��</b>
   * <p>
   * 
   * @author lixiaodong
   * @time 2007-3-20 ����03:23:45
   */
  private void setButtonsList() {

    if (getInvVOs() != null && getInvVOs().length > 0) {
      m_btnInvBillPreview.setEnabled(true);// Ԥ��
      m_btnInvBillPrint.setEnabled(true);// ��ӡ
      btnBillCombin.setEnabled(true);// �ϲ���ʾ
    }
    else {
      m_btnInvBillPreview.setEnabled(false);// Ԥ��
      m_btnInvBillPrint.setEnabled(false);// ��ӡ
      btnBillCombin.setEnabled(false);// �ϲ���ʾ
      m_btnInvBillAudit.setEnabled(false);// ����
    }

    if (getCurPanelMode() == INV_PANEL_CARD) {
    }
    else if (getCurPanelMode() == INV_PANEL_LIST) {
      m_btnInvBillGoFirstOne.setEnabled(false);// ��ҳ
      m_btnInvBillGoNextOne.setEnabled(false);// ��һҳ
      m_btnInvBillGoPreviousOne.setEnabled(false);// ��һҳ
      m_btnInvBillGoLastOne.setEnabled(false);// ĩҳ
    }

  }

  /**
   * �԰�ť״̬��������:��������ת��ʱ,��ʱ����Ϊ�б�״̬
   * 
   * @param
   * @return
   * @exception �쳣����
   * @see ��Ҫ�μ�����������
   * @since �������һ���汾���˷�������ӽ���������ѡ��
   * @deprecated �÷����������һ���汾���Ѿ������������滻������ѡ��
   */
  private void setButtonsStateListFromBills() {
    if (getSelectedRowCount() == 0) {
      m_btnInvBillModify.setEnabled(false);
      m_btnInvDeselectAll.setEnabled(false);

      if (getInvVOs() != null && getInvVOs().length > 0) {
        m_btnInvSelectAll.setEnabled(true);
      }
      else {
        m_btnInvSelectAll.setEnabled(false);
      }
    }
    else {
      if (getSelectedRowCount() == 1) {
        m_btnInvBillModify.setEnabled(true);
      }
      else {
        m_btnInvBillModify.setEnabled(false);
      }

      m_btnInvDeselectAll.setEnabled(true);

      if (getInvVOs() != null && getInvVOs().length > getSelectedRowCount()) {
        m_btnInvSelectAll.setEnabled(true);
      }
      else {
        m_btnInvSelectAll.setEnabled(false);
      }
    }

    m_btnInvBillBusiType.setEnabled(false);// ҵ������
    m_btnInvBillNew.setEnabled(false);

    // ת����ť�ɼ�
    m_btnInvBillConversion.setEnabled(true);
    // ���
    m_btnInvBillAudit.setEnabled(false);
    // ����
    m_btnSendAudit.setEnabled(false);
    // ��ѯ
    m_btnInvBillQuery.setEnabled(false);
    // ����
    m_btnInvBillDiscard.setEnabled(false);
    // ˢ��
    m_btnInvBillRefresh.setEnabled(false);
    // �л�
    m_btnInvShift.setEnabled(false);

    m_btnDocManage.setEnabled(false);

    // ȡ��
    m_btnInvBillCancel.setEnabled(false);
    // ����
    m_btnInvBillCopy.setEnabled(false);
    // ����
    m_btnInvBillSave.setEnabled(false);
  }

  /**
   * �԰�ť״̬��������
   * 
   * @param
   * @return
   * @exception �쳣����
   * @see ��Ҫ�μ�����������
   * @since �������һ���汾���˷�������ӽ���������ѡ��
   * @deprecated �÷����������һ���汾���Ѿ������������滻������ѡ��
   */
  private void setButtonsStateListNormal() {

    if (getInvVOs() == null) {
      m_btnInvSelectAll.setEnabled(false);
      m_btnInvDeselectAll.setEnabled(false);
      m_btnInvBillDiscard.setEnabled(false);
      m_btnInvBillModify.setEnabled(false);
      m_btnInvShift.setEnabled(true);
      m_btnDocManage.setEnabled(false);
      m_btnQueryForAudit.setEnabled(false);
    }
    else {

      if (getSelectedRowCount() == 0) {
        m_btnInvSelectAll.setEnabled(true);
        m_btnInvDeselectAll.setEnabled(false);
        m_btnInvBillDiscard.setEnabled(false);
        m_btnInvBillModify.setEnabled(false);
        m_btnInvShift.setEnabled(false);
        m_btnDocManage.setEnabled(false);
        m_btnQueryForAudit.setEnabled(false);
      }
      else {
        m_btnDocManage.setEnabled(true);
        m_btnInvDeselectAll.setEnabled(true);

        if (getSelectedRowCount() == getInvVOs().length) {
          m_btnInvSelectAll.setEnabled(false);
        }
        else {
          m_btnInvSelectAll.setEnabled(true);
        }

        boolean bModify = true;
        if (getSelectedRowCount() == 1 && getInvVOs()[getCurVOPos()].getBodyVO() != null) {
          bModify = isModifyButnEditableList(new InvoiceVO[] {
            getInvVOs()[getCurVOPos()]
          });
          m_btnInvShift.setEnabled(true);
        }
        else {
          ArrayList list = getSelectedBills();
          InvoiceVO VO[] = new InvoiceVO[list.size()];
          list.toArray(VO);
          bModify = isModifyButnEditableList(VO);
          m_btnInvShift.setEnabled(false);
        }
        m_btnInvBillModify.setEnabled(bModify);

        if (getSelectedRowCount() == 1) {
          m_btnQueryForAudit.setEnabled(true);
          m_btnLnkQuery.setEnabled(true);
        }
        else {
          m_btnQueryForAudit.setEnabled(false);
          m_btnInvBillModify.setEnabled(false);
        }
      }
    }
    // ��ѯ
    m_btnInvBillQuery.setEnabled(true);
    m_btnInvBillConversion.setEnabled(false);
    // ˢ��
    if (isEverQueryed()) {
      m_btnInvBillRefresh.setEnabled(true);
    }
    else {
      m_btnInvBillRefresh.setEnabled(false);
    }

    setButtonsList();
  }

  private boolean isModifyButnEditableList(InvoiceVO VO[]) {
    boolean bModify = true;

    for (int i = 0; i < VO.length; i++) {
      if (VO[i].getHeadVO().getIbillstatus().intValue() != 0) {
        bModify = false;
        break;
      }
      else {
        InvoiceItemVO bodyVO[] = VO[i].getBodyVO();
        for (int j = 0; j < bodyVO.length; j++) {
          if (bodyVO[j].getNaccumsettmny() != null && Math.abs(bodyVO[j].getNaccumsettmny().doubleValue()) > 0) {
            bModify = false;
            break;
          }
        }
      }
    }

    return bModify;
  }

  /**
   * ����ʵ�ָ÷���������ҵ�����ı��⡣
   * 
   * @version (00-6-6 13:33:25)
   * @return java.lang.String
   */
  private void setCauditid(String newId) {

    m_cauditid = newId;
  }

  /**
   * @param ����˵��
   * @return ����ֵ
   * @exception �쳣����
   * @see ��Ҫ�μ�����������
   * @since �������һ���汾���˷�������ӽ���������ѡ��
   */
  private void setCouldMaked(boolean newBoolean) {
    m_bCouldMaked = newBoolean;
  }

  /**
   * ���ߣ���ӡ�� ���ܣ����õ�ǰҵ������ ������java.lang.String newCurBizeType �µ�ҵ������ ���أ��� ���⣺��
   * ���ڣ�(2002-3-13 11:39:21) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־�� 2002-10-29 wyf ��ʱȥ��ҵ�����ͶԴ��������
   */
  private void setCurBizeType(java.lang.String newCurBizeType) {
    m_strCurBizeType = newCurBizeType;
  }

  /**
   * ���õ�ǰ����״̬(��"����","��ѯ"��)
   * 
   * @param ����˵��
   * @return ����ֵ
   * @exception �쳣����
   * @see ��Ҫ�μ�����������
   * @since �������һ���汾���˷�������ӽ���
   * @param newCurOperState
   *          java.lang.String
   */
  private void setCurOperState(int newCurOperState) {
    m_nCurOperState = newCurOperState;
  }

  /**
   * ���õ�ǰ������ʾ��ʽ�ǿ�Ƭ�����б�
   * 
   * @param ����˵��
   * @return ����ֵ
   * @exception �쳣����
   * @see ��Ҫ�μ�����������
   * @since �������һ���汾���˷�������ӽ���
   * @param newCurPaneModel
   *          int
   */
  private void setCurPanelMode(int newCurPaneModel) {
    m_nCurPanelMode = newCurPaneModel;
  }

  /**
   * �ɿ�Ƭ�л����б����
   * 
   * @param ����˵��
   * @return ����ֵ
   * @exception �쳣����
   * @see ��Ҫ�μ�����������
   * @since �������һ���汾���˷�������ӽ���������ѡ��
   * @deprecated �÷����������һ���汾���Ѿ������������滻������ѡ��
   */
  private void setCurrMoneyDigitAndReCalToBill(String strCurr) {

    // ����С��λ
    if (strCurr == null || strCurr.length() < 1) {
      return;
    }

    // =================�����˰���˰�ϼƵľ���
    // ҵ�񾫶�
    int nDigit = POPubSetUI.getMoneyDigitByCurr_Busi(strCurr);
    // ���񾫶�
    int nDigit1 = POPubSetUI.getMoneyDigitByCurr_Finance(strCurr);
    
    int localDigit=getBillCardPanel().getNMoneyDecimal();
    int localFiDigit=getBillCardPanel().getNFiMoneyDecimal();

    getBillCardPanel().getBodyItem("noriginalcurmny").setDecimalDigits(nDigit);
    getBillCardPanel().getBodyItem("noriginaltaxmny").setDecimalDigits(nDigit);
    getBillCardPanel().getBodyItem("noriginalsummny").setDecimalDigits(nDigit);
    getBillCardPanel().getBodyItem("noriginalpaymentmny").setDecimalDigits(nDigit1);
    //by zhaoyha at 2009.11.28
    getBillCardPanel().getBodyItem("nmoney").setDecimalDigits(localDigit);
    getBillCardPanel().getBodyItem("ntaxmny").setDecimalDigits(localDigit);
    getBillCardPanel().getBodyItem("nsummny").setDecimalDigits(localDigit);
    getBillCardPanel().getBodyItem("npaymentmny").setDecimalDigits(localFiDigit);

    if (getOldCurrMoneyDigit() == nDigit) {
      // �����ǰ��������һ�α��ֵĽ��λ����ͬ���������¼���
      return;
    }
    else {
      setOldCurrMoneyDigit(nDigit);
    }

    // ���¼���
    if (getBillCardPanel().getBillModel().getRowCount() <= 0) {
      return;
    }

    UFDouble noriginaltaxmny = new UFDouble(0);
    UFDouble noriginalcurmny = new UFDouble(0);
    UFDouble noriginalsummny = new UFDouble(0);

    // V53����β�����Ʊ���ն���ʱ��������˰��+���+��˰�ϼƣ�������㵥��*����=���
    for (int i = 0; i < getBillCardPanel().getBillModel().getRowCount(); i++) {
      noriginaltaxmny = getBillCardPanel().getBodyValueAt(i, "noriginaltaxmny") == null ? new UFDouble(0)
          : (UFDouble) getBillCardPanel().getBodyValueAt(i, "noriginaltaxmny");// ԭ��˰��
      noriginalcurmny = getBillCardPanel().getBodyValueAt(i, "noriginalcurmny") == null ? new UFDouble(0)
          : (UFDouble) getBillCardPanel().getBodyValueAt(i, "noriginalcurmny");// ԭ����˰���
      noriginalsummny = getBillCardPanel().getBodyValueAt(i, "noriginalsummny") == null ? new UFDouble(0)
          : (UFDouble) getBillCardPanel().getBodyValueAt(i, "noriginalsummny");// ԭ�Ҽ�˰�ϼ�
      if (noriginaltaxmny.add(noriginalcurmny).compareTo(noriginalsummny) != 0) {
        afterEditInvBillRelations(new BillEditEvent(getBillCardPanel().getBodyItem("ninvoicenum"), getBillCardPanel()
            .getBodyValueAt(i, "ninvoicenum"), "ninvoicenum", i, BillItem.BODY));
      }
    }

  }

  /**
   * �ɿ�Ƭ�л����б����
   * 
   * @param ����˵��
   * @return ����ֵ
   * @exception �쳣����
   * @see ��Ҫ�μ�����������
   * @since �������һ���汾���˷�������ӽ���������ѡ��
   * @deprecated �÷����������һ���汾���Ѿ������������滻������ѡ��
   */
  private void setCurrMoneyDigitToBill(String strCurr) {

    if (strCurr == null || strCurr.length() < 1) {
      return;
    }

    // =================�����˰���˰�ϼƵľ���
    // ҵ�񾫶�
    int nDigit = POPubSetUI.getMoneyDigitByCurr_Busi(strCurr);
    // ���񾫶�
    int nDigit1 = POPubSetUI.getMoneyDigitByCurr_Finance(strCurr);

    getBillCardPanel().getBodyItem("noriginalcurmny").setDecimalDigits(nDigit);
    getBillCardPanel().getBodyItem("noriginaltaxmny").setDecimalDigits(nDigit);
    getBillCardPanel().getBodyItem("noriginalsummny").setDecimalDigits(nDigit);
    getBillCardPanel().getBodyItem("noriginalpaymentmny").setDecimalDigits(nDigit1);

    getBillCardPanel().getBodyItem("nmoney").setDecimalDigits(nDigit);
    getBillCardPanel().getBodyItem("ntaxmny").setDecimalDigits(nDigit);
    getBillCardPanel().getBodyItem("nsummny").setDecimalDigits(nDigit);
    getBillCardPanel().getBodyItem("npaymentmny").setDecimalDigits(nDigit1);

  }

  /**
   * ���ߣ���ӡ�� ���ܣ����ñ�ͷ�۱����۸����� ������String strCurr ����ID UFDouble dBRate �۱�����
   * �������գ����Զ������۱����ʣ�����ȡ��ֵ UFDouble dARate �۸����� �������գ����Զ������۸����ʣ�����ȡ��ֵ ���أ��� ���⣺��
   * ���ڣ�(2002-3-13 11:39:21) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־�� 2002-07-11 wyf ��Ϊʹ�ù�������
   * 2002-07-17 wyf �޸��۸�����д��KEY������
   */
  private void setCurrRateToBillHead(String sCurr, UFDouble dBRate) {

    String sRateDate = getBillCardPanel().getHeadItem("darrivedate").getValue();
    UFDate dOrderDate = null;
    if (sRateDate != null && !sRateDate.trim().equals("")) {
      dOrderDate = new UFDate(sRateDate);
    }

    // λ��
    int[] iaDigit = POPubSetUI.getBothExchRateDigit(getPk_corp(), sCurr);
    getBillCardPanel().getHeadItem("nexchangeotobrate").setDecimalDigits(iaDigit[0]);

    // �Ƿ�ɱ༭
    boolean[] baEditable = POPubSetUI.getBothExchRateEditable(getPk_corp(), sCurr);
    getBillCardPanel().getHeadItem("nexchangeotobrate").setEnabled(baEditable[0]);

    // ֵ
    UFDouble[] daValue = null;
    if (dBRate == null) {
      daValue = POPubSetUI.getBothExchRateValue(getPk_corp(), sCurr, dOrderDate);
      //add by QuSida (��ɽ����)��ֹ�۱�����Ϊ��
      UFDouble temp = daValue[0] == null?UFDouble.ONE_DBL: daValue[0];
      getBillCardPanel().setHeadItem("nexchangeotobrate", temp);
    }
    else {
      getBillCardPanel().setHeadItem("nexchangeotobrate", dBRate);
    }

  }

  /**
   * ���õ�ǰ��ʾ��VOλ��
   * 
   * @param ����˵��
   * @return ����ֵ
   * @exception �쳣����
   * @see ��Ҫ�μ�����������
   * @since �������һ���汾���˷�������ӽ���
   * @param newPreOperState
   *          java.lang.String
   */
  private void setCurVOPos(int pos) {
    m_nCurInvVOPos = pos;
  }

  /**
   * �ṩ��ʽ,�Ӹù�ʽ�õ���ѯ��� �ο�BillModel.execloadFormula(int)д��
   * 
   * @param ����˵��
   * @return ����ֵ
   * @exception �쳣����
   * @see ��Ҫ�μ�����������
   * @since �������һ���汾���˷�������ӽ���
   * @return java.lang.String
   */
  private void setDefaultBankAccountForAVendor(String strVendorBase) {
    String pkcorp = getBillCardPanel().getCorp();
    String sWherePart=" bd_bankaccbas.pk_bankaccbas in ("+
    "select pk_accbank from bd_custbank where pk_cubasdoc='"+
    strVendorBase+"')";

//    String sWherePart = " and bd_bankaccbas.pk_bankaccbas in (" +
//    		"select  k.pk_accbank from bd_custbank k,bd_cumandoc m   where   m.pk_corp ='"
//      + pkcorp + "'  and k.pk_cubasdoc=m.pk_cubasdoc ";
//    if (nc.vo.scm.pu.PuPubVO.getString_TrimZeroLenAsNull(strVendorBase) != null) {
//      sWherePart += "and k.pk_cubasdoc='" + strVendorBase + "'";
//    }
//    sWherePart += ")";
    UIRefPane ref = (UIRefPane) getBillCardPanel().getHeadItem("caccountbankid").getComponent();

    ref.getRefModel().setWherePart(sWherePart);

    //ref.getRefModel().reloadData();

  }

  /**
   * ���ܣ����ӱ�����ʱ������Ĭ������
   */
  private void setDefaultBody(int iCurRow) {

    // ID
    getBillCardPanel().setBodyValueAt(null, iCurRow, "cinvoice_bid");
    getBillCardPanel().setBodyValueAt(null, iCurRow, "cinvoiceid");
    // ��˾����
    getBillCardPanel().setBodyValueAt(getPk_corp(), iCurRow, "pk_corp");
    //
    updateUI();
  }

  /**
   * @param ����˵��
   * @return ����ֵ
   * @exception �쳣����
   * @see ��Ҫ�μ�����������
   * @since �������һ���汾���˷�������ӽ���������ѡ��
   */
  private void setDefaultInfoForAFreeCust(String cfreeId) {

    if (cfreeId == null) {
      getBillCardPanel().setHeadItem("cvendorphone", null);
      getBillCardPanel().setHeadItem("caccountbankid", null);
      getBillCardPanel().setHeadItem("cvendoraccount", null);
      return;
    }

    Object[][] strInfo = null;
    try {
      strInfo = nc.ui.pu.pub.PubHelper.queryResultsFromAnyTable("so_freecust", new String[] {
          "vphone", "vaccname", "vaccount"
      }, "cfreecustid='" + cfreeId + "'");
    }
    catch (Exception e1) {
      SCMEnv.out(e1);
      return;
    }
    if (strInfo != null) {
      getBillCardPanel().setHeadItem("cvendorphone", strInfo[0][0]);
      ((UIRefPane) getBillCardPanel().getHeadItem("caccountbankid").getComponent()).getUITextField().setText(
          (String) strInfo[0][1]);
      getBillCardPanel().setHeadItem("cvendoraccount", strInfo[0][2]);
    }
  }

  /**
   * �ṩ��ʽ,�Ӹù�ʽ�õ���ѯ��� �ο�BillModel.execloadFormula(int)д��
   * 
   * @param ����˵��
   * @return ����ֵ
   * @exception �쳣����
   * @see ��Ҫ�μ�����������
   * @since �������һ���汾���˷�������ӽ���
   * @return java.lang.String
   */
  private void setDefaultPhoneForAVendor(String strVendorBase) {

    if (strVendorBase == null || strVendorBase.trim().equals("")) {
      return;
    }

    BillItem item = getBillCardPanel().getHeadItem("cvendorphone");
    if (item == null || !item.isShow())
      return;

    Object[][] retOb = CacheTool.getMultiColValue("bd_cubasdoc", "pk_cubasdoc", new String[] {
        "phone1", "phone2", "phone3"
    }, new String[] {
      strVendorBase
    });

    if (retOb == null || retOb.length == 0) {
      getBillCardPanel().setHeadItem("cvendorphone", null);
    }
    else {
      String phone = null;
      for (int i = 0; i < retOb.length; i++) {
        if (retOb[i][0] != null && retOb[i][0].toString().trim().length() > 0) {
          phone = retOb[i][0].toString();
          break;
        }
      }
      getBillCardPanel().setHeadItem("cvendorphone", phone);
    }
  }

  /**
   * ���ߣ���ӡ�� ���ܣ���Ʊ�༭ʱ��������Ŀ�Ŀɱ༭�� ֻ���ڱ༭״̬���ҽ������з�ƱVO �������� ���أ��� ���⣺�� ���ڣ�(2001-10-13
   * 11:39:21) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־�� 2002-09-18 wyf ��������κŵĴ���
   */
  private void setEditableWhenEdit() {
    // ��Ʊ��
    //setEditableWhenEdit_Vinvoicecode();
    // ɢ��,��������
    setEditableWhenEdit_FreeCustAndBank();
    // �����Ƿ�ɱ༭
    setEditableWhenEdit_ExchRate();

    // wyf add 2002-07-18 begin
    // �����Դ����Ϊ��ⵥ�����������޸�
    setEditableWhenEdit_Inventory();
    // wyf add 2002-07-18 end

    // wyf add 2002-09-18 begin
    setEditableWhenEdit_VProduceNum();
    // wyf add 2002-09-18 end
    
    //����ҵ������Ҫ�����Դ�ڶ����ķ����������ֶβ������޸� For V56 by zhaoyha
    //setNumEditable();
  }

  /**
   * ���ߣ���ӡ�� ���ܣ���Ʊ�༭ʱ�����û��ʵĿɱ༭�� ֻ���ڱ༭״̬���ҽ������з�ƱVO �������� ���أ��� ���⣺�� ���ڣ�(2001-10-13
   * 11:39:21) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־�� 2002-09-18 wyf ��������κŵĴ���
   */
  private void setEditableWhenEdit_ExchRate() {
    // �����Ƿ�ɱ༭
    String sCurr = getBillCardPanel().getHeadItem("ccurrencytypeid").getValue();
//    if(!StringUtil.isEmptyWithTrim(sCurr)){
    boolean[] baEditable = POPubSetUI.getBothExchRateEditable(getPk_corp(), sCurr);
    getBillCardPanel().getHeadItem("nexchangeotobrate").setEnabled(baEditable[0]);
    
  }

  /**
   * ���ߣ���ӡ�� ���ܣ���Ʊ�༭ʱ������ɢ�����������еĿɱ༭�� ֻ���ڱ༭״̬���ҽ������з�ƱVO �������� ���أ��� ���⣺��
   * ���ڣ�(2001-10-13 11:39:21) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
   */
  private void setEditableWhenEdit_FreeCustAndBank() {
    // ɢ��,��������
    if (getBillCardPanel().getHeadItem("cfreecustid").getValue() == null) {
      // �жϹ�Ӧ���Ƿ�Ϊɢ������Ϊɢ������ɢ���Կ���
      if (getBillCardPanel().getHeadItem("cvendorbaseid").getValue() == null
          || getBillCardPanel().getHeadItem("cvendorbaseid").getValue().trim().length() < 1) {
        getBillCardPanel().getHeadItem("cfreecustid").setEnabled(false);
        getBillCardPanel().getHeadItem("caccountbankid").setEnabled(false);
      }
      else {
        String sFreeFlag = "N";
        Object oTemp = getBillCardPanel().getHeadItem("cvendorbaseid").getValue();
        if (oTemp != null)
          oTemp = PiPqPublicUIClass.getAResultFromTable("bd_cubasdoc", "freecustflag", "pk_cubasdoc", oTemp.toString());
        if (oTemp != null)
          sFreeFlag = oTemp.toString();
        if (sFreeFlag != null && sFreeFlag.trim() != null && sFreeFlag.equals("Y")) {
          getBillCardPanel().getHeadItem("cfreecustid").setEnabled(true);
          getBillCardPanel().getHeadItem("caccountbankid").setEnabled(false);
        }
        else {
          getBillCardPanel().getHeadItem("cfreecustid").setEnabled(false);
          getBillCardPanel().getHeadItem("caccountbankid").setEnabled(true);
        }
      }
    }
    else {
      // ɢ������
      getBillCardPanel().getHeadItem("cfreecustid").setEnabled(true);
      getBillCardPanel().getHeadItem("caccountbankid").setEnabled(false);
    }
    // added by fangy 2002-10-29 begin
    // getInvBillPanel().getHeadItem("caccountbankid").setEdit(false);
    getBillCardPanel().getHeadItem("cvendorphone").getComponent().setEnabled(false);
    // added by fangy 2002-10-29 end

  }

  /**
   * ���ߣ���ӡ�� ���ܣ���Ʊ�༭ʱ�����ô���Ŀɱ༭�� ֻ���ڱ༭״̬���ҽ������з�ƱVO �������� ���أ��� ���⣺�� ���ڣ�(2002-07-18
   * 11:39:21) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
   */
  private void setEditableWhenEdit_Inventory() {

    // �����Դ����Ϊ��ⵥ�����������޸�
    int iCount = getBillCardPanel().getRowCount();
    for (int i = 0; i < iCount; i++) {
      String sUpSourceBillType = (String) getBillCardPanel().getBodyValueAt(i, "cupsourcebilltype");
      if (sUpSourceBillType != null
          && (sUpSourceBillType.trim().equals(nc.vo.scm.pu.BillTypeConst.STORE_PO)
              || sUpSourceBillType.trim().equals(nc.vo.scm.pu.BillTypeConst.STORE_SC)
              || sUpSourceBillType.trim().equals(nc.vo.scm.pu.BillTypeConst.STORE_ES) || sUpSourceBillType.trim()
              .equals("50"))) {
        getBillCardPanel().setCellEditable(i, "invcode", false);
      }
      else {
        getBillCardPanel().setCellEditable(i, "invcode", true);
      }
    }
  }

  /**
   * ���ߣ���ӡ�� ���ܣ���Ʊ�༭ʱ�����÷�Ʊ�ŵĿɱ༭�� ֻ���ڱ༭״̬���ҽ������з�ƱVO �������� ���أ��� ���⣺�� ���ڣ�(2001-10-13
   * 11:39:21) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
   */
  private void setEditableWhenEdit_Vinvoicecode() {
    // ////��Ʊ��
    if (getBillCardPanel().getHeadItem("vinvoicecode").getValue() == null
        || getBillCardPanel().getHeadItem("vinvoicecode").getValue().trim().equals("")) {
      getBillCardPanel().getHeadItem("vinvoicecode").setEnabled(true);
    }
    else {
      if (getBillCardPanel().getHeadItem("cinvoiceid").getValue() == null
          || getBillCardPanel().getHeadItem("cinvoiceid").getValue().trim().equals("")) {
        getBillCardPanel().getHeadItem("vinvoicecode").setEnabled(true);
      }
      else {
        getBillCardPanel().getHeadItem("vinvoicecode").setEnabled(true);
      }
    }
  }

  /**
   * ���ߣ���ӡ�� ���ܣ��趨���κ���صĿɱ༭�� �������� ���أ��� ���⣺�� ���ڣ�(2002-9-18 11:39:21)
   * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
   */
  private void setEditableWhenEdit_VProduceNum() {

    // =========���� ��ͬ�ż�������ɱ༭
    int iRow = getBillCardPanel().getRowCount();
    if (iRow <= 0) {
      return;
    }
    for (int i = 0; i < iRow; i++) {
      String sMangId = (String) getBillCardPanel().getBodyValueAt(i, "cmangid");
      if (sMangId == null || sMangId.trim().length() < 1) {
        // =====���ɱ༭
        getBillCardPanel().setCellEditable(i, "vproducenum", false);
      }
      else {
        // =====�Ƿ�ɱ༭
        getBillCardPanel().setCellEditable(i, "vproducenum", PuTool.isBatchManaged(sMangId));
      }
    }
  }

  /**
   * @param ����˵��
   * @return ����ֵ
   * @exception �쳣����
   * @see ��Ҫ�μ�����������
   * @since �������һ���汾���˷�������ӽ���������ѡ��
   */
  private void setEverQueryed(boolean newQueryed) {

    m_bEverQueryed = newQueryed;
  }

  /**
   * ���ߣ����� ���ܣ����±�ͷ�ı���.(�ӱ�����) ������ ���أ� ���⣺ ���ڣ�(2003-02-08 13:09:22)
   * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
   * 
   * @param inv
   *          nc.vo.pi.InvoiceVO
   */
  public void setHeadCurrency(InvoiceVO inv) {

    InvoiceHeaderVO head = (InvoiceHeaderVO) inv.getHeadVO();
    InvoiceItemVO[] items = (InvoiceItemVO[]) inv.getChildrenVO();

    // �������Ϊ��,��Ӻ�̨��ȡ.
    if (head != null && items != null && items.length > 0) {
      if (m_listPoPubSetUI2 == null) {
        m_listPoPubSetUI2 = new POPubSetUI2();
      }
      // ����
      int[] iaExchRateDigit = null;
      for (int i = 0; i < items.length; i++) {
        // ��Ӧ�̹���ID
        String sCvendormangid = head.getCvendormangid();
        String upSourceType = items[i].getCupsourcebilltype();
        String currencytypeid = items[i].getCcurrencytypeid();
        if (currencytypeid != null) {
          head.setCcurrencytypeid(items[i].getCcurrencytypeid());
        }
        else {
          // ����й�Ӧ�̣�Ӧ����Ĭ�ϱ���
          if (upSourceType != null && upSourceType.trim().length() > 0
              && (upSourceType.equals("45") || upSourceType.equals("47") || upSourceType.equals("4T"))) {
            if (sCvendormangid != null && sCvendormangid.trim().length() > 0) {
              try {
                Object oTemp = CacheTool.getCellValue("bd_cumandoc", "pk_cumandoc", "pk_currtype1", sCvendormangid);
                if (oTemp != null) {
                  head.setCcurrencytypeid(oTemp.toString());
                  items[i].setCcurrencytypeid(oTemp.toString());
                }
              }
              catch (Exception e) {
                SCMEnv.out(e);
              }
            }
          }
        }
        // ȡ�ñ���
        String sCurrId = items[i].getCcurrencytypeid();
        // �õ��۱����۸����ʾ���
        iaExchRateDigit = m_listPoPubSetUI2.getBothExchRateDigit(getPk_corp(), sCurrId);
        if (iaExchRateDigit == null || iaExchRateDigit.length == 0) {
          getBillCardPanel().getBillModel().getItemByKey("nexchangeotobrate").setDecimalDigits(5);
        }
        else {
          getBillCardPanel().getBillModel().getItemByKey("nexchangeotobrate").setDecimalDigits(iaExchRateDigit[0]);
        }
      }

      if (iaExchRateDigit == null || iaExchRateDigit.length == 0) {
        getBillCardPanel().getHeadItem("nexchangeotobrate").setDecimalDigits(5);
      }
      else {
        getBillCardPanel().getHeadItem("nexchangeotobrate").setDecimalDigits(iaExchRateDigit[0]);
      }
      // �����۱����۸����ʵ�ֵ
      UFDouble[] d = POPubSetUI.getBothExchRateValue(getPk_corp(), head.getCcurrencytypeid(), head.getDarrivedate());
      head.setNexchangeotobrate(d[0]);
    }

  }

  /**
   * ����ʵ�ָ÷���������ҵ�����ı��⡣
   * 
   * @version (00-6-6 13:33:25)
   * @return java.lang.String
   */
  private void setHeadHintText(String strHint) {

    m_strHeadHintText = strHint;
  }

  /**
   * ���ߣ���ά�� ���ܣ��������ô���Ĺ��������Ϣ ����: BillCardPanel pnlBillCard ����ģ�� UIRefPane
   * refpaneInv ������� int iBeginRow ��ʼλ�� int iEndRow ����λ�� ���أ��� ���⣺��
   * ���ڣ�(2004-02-11 13:45:10) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
   */
  private void setInvEditFormulaInfo(BillCardPanel pnlBillCard, UIRefPane refpaneInv, int iBeginRow, int iEndRow) {
    if (pnlBillCard == null || refpaneInv == null) {
      SCMEnv.out("�����������ȷ��");
      return;
    }
    try {
      Object[] saMangIdRef = ((Object[]) refpaneInv.getRefValues("bd_invmandoc.pk_invmandoc"));
      Object[] saBaseIdRef = ((Object[]) refpaneInv.getRefValues("bd_invmandoc.pk_invbasdoc"));
      Object[] saMeasUnitRef = ((Object[]) refpaneInv.getRefValues("bd_invbasdoc.pk_measdoc"));
      Object[] saMangId = new Object[saMangIdRef.length];
      Object[] saBaseId = new Object[saBaseIdRef.length];
      Object[] saMeasUnit = new Object[saMeasUnitRef.length];
      if (saMangId == null || saBaseId == null || saMangId.length != saBaseId.length) {
        SCMEnv.out("���ݴ��󣺴��������IDΪ�ջ�������IDΪ�ջ���߳��Ȳ��ȣ�ֱ�ӷ���");
        return;
      }
      for (int i = 0; i < saMangId.length; i++) {
        saMangId[i] = saMangIdRef[i];
        saBaseId[i] = saBaseIdRef[i];
        saMeasUnit[i] = saMeasUnitRef[i];
      }
      for (int i = 0; i < saMangId.length; i++) {
        saMangIdRef[i] = (String) saMangIdRef[i];
        saBaseIdRef[i] = (String) saBaseIdRef[i];
        saMeasUnitRef[i] = (String) saMeasUnitRef[i];
      }
      int iLen = saMangId.length;

      // ================����������������������
      String[] saFormula = new String[] {
        "getColValue(bd_measdoc,measname,pk_measdoc,cmessureunit)"
      };
      PuTool.getFormulaParse().setExpressArray(saFormula);
      int iFormulaLen = saFormula.length;

      String[] sMeasUnits = new String[saMangId.length];
      String[] sMangIds = new String[saMangId.length];
      for (int i = 0; i < saMangId.length; i++) {
        sMeasUnits[i] = (String) sMeasUnits[i];
        sMangIds[i] = (String) saMangId[i];
      }
      for (int i = 0; i < iFormulaLen; i++) {
        PuTool.getFormulaParse().addVariable("cmessureunit", saMeasUnitRef);
      }

      String[][] saRet = PuTool.getFormulaParse().getValueSArray();
      String[] saUnitName = new String[iLen];
      if (saRet != null) {
        for (int i = 0; i < iLen; i++) {
          if (saRet[0] != null) {
            saUnitName[i] = saRet[0][i];
          }
        }
      }
      // ================����������:����
      saFormula = new String[] {
        "getColValue(bd_invmandoc,prodarea,pk_invmandoc,cmangid)"
      };
      iFormulaLen = saFormula.length;
      PuTool.getFormulaParse().setExpressArray(saFormula);

      for (int i = 0; i < iFormulaLen; i++) {
        PuTool.getFormulaParse().addVariable("cmangid", saMangIdRef);
      }

      saRet = PuTool.getFormulaParse().getValueSArray();
      // ����
      String[] saArea = new String[iLen];
      if (saRet != null) {
        for (int i = 0; i < iLen; i++) {
          if (saRet[0] != null) {
            saArea[i] = saRet[0][i];
          }
        }
      }
      // ================�Ա����������ֵ
      Object[] saCode = ((Object[]) refpaneInv.getRefValues("bd_invbasdoc.invcode"));
      Object[] saName = ((Object[]) refpaneInv.getRefValues("bd_invbasdoc.invname"));
      Object[] saSpec = ((Object[]) refpaneInv.getRefValues("bd_invbasdoc.invspec"));
      Object[] saType = ((Object[]) refpaneInv.getRefValues("bd_invbasdoc.invtype"));

      // ִ�б��幫ʽ
      int iPkIndex = 0;
      for (int i = iBeginRow; i <= iEndRow; i++) {
        iPkIndex = i - iBeginRow;
        // ����ID
        pnlBillCard.setBodyValueAt(saMangId[iPkIndex], i, "cmangid");
        // ����ID
        pnlBillCard.setBodyValueAt(saBaseId[iPkIndex], i, "cbaseid");
        // ����
        pnlBillCard.setBodyValueAt(saCode[iPkIndex], i, "invcode");
        // ����
        pnlBillCard.setBodyValueAt(saName[iPkIndex], i, "invname");
        // ���
        pnlBillCard.setBodyValueAt(saSpec[iPkIndex], i, "invspec");
        // �ͺ�
        pnlBillCard.setBodyValueAt(saType[iPkIndex], i, "invtype");
        // ������λNAME
        pnlBillCard.setBodyValueAt(saUnitName[iPkIndex], i, "measname");
        // ����
        pnlBillCard.setBodyValueAt(saArea[iPkIndex], i, "cproducearea");
      }
    }
    catch (Exception e) {
      SCMEnv.out("¼�����ʱ���ó���");
    }
  }

  /**
   * ���ߣ���ӡ�� ���ܣ����ݵ�ǰ�Ĳ���״̬������Ʊ�����������Ƿ��С����⡱ITEM �������� ���أ��� ���⣺�� ���ڣ�(2002-9-27
   * 11:39:21) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
   * @deprecated
   */
  private void setInvoiceTypeComItem() {
    
//    // ��Ʊ���� Ĭ��Ϊ��ר�á�
//    // ����ר�á�������ͨ������ר�á����� ������01234
//    UIComboBox comItem = (UIComboBox) getBillCardPanel().getHeadItem("iinvoicetype").getComponent();
//    if (getCurOperState() == STATE_EDIT) {
//      if (comItem.getItemCount() == 4) {
//        comItem.removeItem(nc.ui.ml.NCLangRes.getInstance().getStrByID(nc.vo.scm.pu.InvoiceType.m_sModuleNamePath,
//            nc.vo.scm.pu.InvoiceType.INVOICE_TYPE_VIRTUALID));
//      }
//    }
//    else {
//      if (comItem.getItemCount() == 3) {
//        comItem.addItem(nc.ui.ml.NCLangRes.getInstance().getStrByID(nc.vo.scm.pu.InvoiceType.m_sModuleNamePath,
//            nc.vo.scm.pu.InvoiceType.INVOICE_TYPE_VIRTUALID));
//      }
//    }
  }

  /**
   * �������з���������VO
   * 
   * @param ����˵��
   * @return ����ֵ
   * @exception �쳣����
   * @see ��Ҫ�μ�����������
   * @since �������һ���汾���˷�������ӽ���
   * @param newAllQualifiedVOs
   *          nc.vo.pi.InvoiceVO[]
   */
  public void setInvVOs(nc.vo.pi.InvoiceVO[] newInvVOs) {
    m_InvVOs = newInvVOs;
  }

  /**
   * �˴����뷽��˵���� �������ڣ�(2001-9-14 21:19:56)
   * 
   * @param newListOperState
   *          java.lang.String
   */
  private void setListOperState(int newListOperState) {
    m_nListOperState = newListOperState;
  }

  /**
   * ���ߣ���ӡ�� ���ܣ�����һ����Ʊ�� �÷�����onAppendLine()��onInsertLine()���� ������int iNewRow
   * Ҫ������кţ���0��ʼ ���أ��� ���⣺�� ���ڣ�(2002-9-26 11:39:21) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
   */
  private void setNewLineDefaultValue(int iNewRow) {
    // ���������ı����еĿ�˰������ͷ��ͬ
    getBillCardPanel().setBodyValueAt(
        ((UIComboBox) getBillCardPanel().getHeadItem("idiscounttaxtype").getComponent()).getSelectedItem(), iNewRow,
        "idiscounttaxtype");
    // ˰��=��ͷ˰��
    if (getBillCardPanel().getHeadItem("ntaxrate").getValue() != null) {
      getBillCardPanel().setBodyValueAt(getBillCardPanel().getHeadItem("ntaxrate").getValue(), iNewRow, "ntaxrate");
    }
    // �۱�����=��ͷ�۱�����,�۸�����=���۱�����
    getBillCardPanel().setBodyValueAt(getBillCardPanel().getHeadItem("nexchangeotobrate").getValue(), iNewRow,
        "nexchangeotobrate");
    // ����=��ͷ���� ��1��Ϊ����ID��ֵ��2����ʾ��������
    String strCurrTypeId = ((UIRefPane) getBillCardPanel().getHeadItem("ccurrencytypeid").getComponent()).getRefPK();
    getBillCardPanel().setBodyValueAt(strCurrTypeId, iNewRow, "ccurrencytypeid");
    getBillCardPanel().setBodyValueAt(
        ((UIRefPane) getBillCardPanel().getHeadItem("ccurrencytypeid").getComponent()).getRefName(), iNewRow,
        "currname");
    // ���û���
    setExchangeRateBody(iNewRow, true, null);
    // ��ע��մ�����֤����afterEdit
    getBillCardPanel().setBodyValueAt(null, iNewRow, "vmemo");
    // ��մ������cmangid
    getBillCardPanel().setBodyValueAt(null, iNewRow, "cmangid");
    getBillCardPanel().setBodyValueAt(null, iNewRow, "cbaseid");
    getBillCardPanel().setBodyValueAt(null, iNewRow, "invcode");
    getBillCardPanel().setBodyValueAt(null, iNewRow, "invname");
    getBillCardPanel().setBodyValueAt(null, iNewRow, "invspec");
    getBillCardPanel().setBodyValueAt(null, iNewRow, "invtype");
    getBillCardPanel().setBodyValueAt(null, iNewRow, "measname");
    getBillCardPanel().setBodyValueAt(null, iNewRow, "noriginalcurmny");
    getBillCardPanel().setBodyValueAt(null, iNewRow, "noriginaltaxmny");
    getBillCardPanel().setBodyValueAt(null, iNewRow, "noriginalsummny");
    getBillCardPanel().setBodyValueAt(null, iNewRow, "nmoney");
    getBillCardPanel().setBodyValueAt(null, iNewRow, "ntaxmny");
    getBillCardPanel().setBodyValueAt(null, iNewRow, "nsummny");
    // Ĭ�ϰ���id��Ϣ����
    getBillCardPanel().setBodyValueAt(null, iNewRow, "cinvoiceid");
    getBillCardPanel().setBodyValueAt(null, iNewRow, "cinvoice_bid");

    // �����Դ������Ϣ
    getBillCardPanel().setBodyValueAt(null, iNewRow, "corderid");
    getBillCardPanel().setBodyValueAt(null, iNewRow, "corder_bid");
    getBillCardPanel().setBodyValueAt(null, iNewRow, "csourcebillid");
    getBillCardPanel().setBodyValueAt(null, iNewRow, "csourcebillrowid");
    getBillCardPanel().setBodyValueAt(null, iNewRow, "csourcebilltype");
    getBillCardPanel().setBodyValueAt(null, iNewRow, "cupsourcebillid");
    getBillCardPanel().setBodyValueAt(null, iNewRow, "cupsourcebillrowid");
    getBillCardPanel().setBodyValueAt(null, iNewRow, "cupsourcebilltype");

  }

  /**
   * ���ߣ���ӡ�� ���ܣ�����һ����Ʊ�� �÷�����onAppendLine()��onInsertLine()���� ������int iNewRow
   * Ҫ������кţ���0��ʼ ���أ��� ���⣺�� ���ڣ�(2002-9-26 11:39:21) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
   */
  private void setNewLineDefaultValueForAddLine(int iNewRow) {
    // ���������ı����еĿ�˰������ͷ��ͬ
    getBillCardPanel().setBodyValueAt(
        ((UIComboBox) getBillCardPanel().getHeadItem("idiscounttaxtype").getComponent()).getSelectedItem(), iNewRow,
        "idiscounttaxtype");
    // ˰��=��ͷ˰��
    if (getBillCardPanel().getHeadItem("ntaxrate").getValue() != null) {
      getBillCardPanel().setBodyValueAt(getBillCardPanel().getHeadItem("ntaxrate").getValue(), iNewRow, "ntaxrate");
    }
    // �۱�����=��ͷ�۱�����,�۸�����=���۱�����
    getBillCardPanel().setBodyValueAt(getBillCardPanel().getHeadItem("nexchangeotobrate").getValue(), iNewRow,
        "nexchangeotobrate");
    // ����=��ͷ���� ��1��Ϊ����ID��ֵ��2����ʾ��������
    String strCurrTypeId = ((UIRefPane) getBillCardPanel().getHeadItem("ccurrencytypeid").getComponent()).getRefPK();
    getBillCardPanel().setBodyValueAt(strCurrTypeId, iNewRow, "ccurrencytypeid");
    getBillCardPanel().setBodyValueAt(
        ((UIRefPane) getBillCardPanel().getHeadItem("ccurrencytypeid").getComponent()).getRefName(), iNewRow,
        "currname");
    // ���û���
    setExchangeRateBody(iNewRow, true, null);
    // ��ע��մ�����֤����afterEdit
    getBillCardPanel().setBodyValueAt(null, iNewRow, "vmemo");
    // ��մ������cmangid
    getBillCardPanel().setBodyValueAt(null, iNewRow, "cmangid");
    getBillCardPanel().setBodyValueAt(null, iNewRow, "cbaseid");
    getBillCardPanel().setBodyValueAt(null, iNewRow, "invcode");
    getBillCardPanel().setBodyValueAt(null, iNewRow, "invname");
    getBillCardPanel().setBodyValueAt(null, iNewRow, "invspec");
    getBillCardPanel().setBodyValueAt(null, iNewRow, "invtype");
    getBillCardPanel().setBodyValueAt(null, iNewRow, "measname");
    getBillCardPanel().setBodyValueAt(null, iNewRow, "noriginalcurmny");
    getBillCardPanel().setBodyValueAt(null, iNewRow, "noriginaltaxmny");
    getBillCardPanel().setBodyValueAt(null, iNewRow, "noriginalsummny");
    getBillCardPanel().setBodyValueAt(null, iNewRow, "nmoney");
    getBillCardPanel().setBodyValueAt(null, iNewRow, "ntaxmny");
    getBillCardPanel().setBodyValueAt(null, iNewRow, "nsummny");
    // Ĭ�ϰ���id��Ϣ����
    getBillCardPanel().setBodyValueAt(null, iNewRow, "cinvoiceid");
    getBillCardPanel().setBodyValueAt(null, iNewRow, "cinvoice_bid");

    // �����Դ������Ϣ
    getBillCardPanel().setBodyValueAt(null, iNewRow, "corderid");
    getBillCardPanel().setBodyValueAt(null, iNewRow, "corder_bid");
    getBillCardPanel().setBodyValueAt(null, iNewRow, "csourcebillid");
    getBillCardPanel().setBodyValueAt(null, iNewRow, "csourcebillrowid");
    getBillCardPanel().setBodyValueAt(null, iNewRow, "csourcebilltype");
    getBillCardPanel().setBodyValueAt(null, iNewRow, "cupsourcebillid");
    getBillCardPanel().setBodyValueAt(null, iNewRow, "cupsourcebillrowid");
    getBillCardPanel().setBodyValueAt(null, iNewRow, "cupsourcebilltype");
  }

  /**
   * ��Ŀ�׶β���
   */
  private void setOldCurrMoneyDigit(int newValue) {

    m_oldCurrMoneyDigit = newValue;
  }

  /**
   * ���ߣ���ά�� ���ܣ�����޸ĺ���Ӧ��˰�ʱ仯 �ú�����afterEdit������������ڡ����ָı����á� ������ int iBeginRow
   * �����˰�������Ϣ�ı��忪ʼ�� int iEndRow �����˰�������Ϣ�ı�������� ���أ��� ���⣺�� ���ڣ�(2003-11-3
   * 11:39:21) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
   */
  public void setRelated_Taxrate(int iBeginRow, int iEndRow) {

    HashMap mapId = new HashMap();
    Vector vecRow = new Vector();
    for (int i = iBeginRow; i <= iEndRow; i++) {
      String sBaseId = PuPubVO.getString_TrimZeroLenAsNull((String) getBillCardPanel().getBodyValueAt(i, "cbaseid"));
      if (sBaseId != null) {
        mapId.put(sBaseId, "");
        vecRow.add(new Integer(i));
      }
    }
    int iSize = vecRow.size();
    if (iSize == 0) {
      return;
    }
    // ��������˰��
    PuTool.loadBatchTaxrate((String[]) mapId.keySet().toArray(new String[iSize]), getCorpId());

    // ����
    int iRow = 0;
    for (int i = 0; i < iSize; i++) {
      iRow = ((Integer) vecRow.get(i)).intValue();

      String sBaseId = (String) getBillCardPanel().getBodyValueAt(iRow, "cbaseid");

      UFDouble dCurTaxRate = PuTool.getInvTaxRate(sBaseId);
      if (dCurTaxRate != null) {
        getBillCardPanel().setBodyValueAt(dCurTaxRate, iRow, "ntaxrate");
        // ˰�ʸı䣬���㣨���������ۣ���˰���ۣ���˰�ʣ�˰���˰�ϼ�,���ʣ����ʵ��ۣ�֮��Ĺ�ϵ
        BillEditEvent tempE = new BillEditEvent(getBillCardPanel().getBodyItem("ntaxrate"), dCurTaxRate, "ntaxrate",
            iRow);
        // ������ֵ���¼����¼�
        afterEditInvBillRelations(tempE);
      }
    }

  }

  /**
   * �˴����뷽��˵���� �������ڣ�(2001-9-14 12:03:06)
   * 
   * @param newSelectedRows
   *          int[]
   */
  private void setSelectedRowCount(int newSelectedRow) {
    m_nSelectedRowCount = newSelectedRow;
  }

  /**
   * �˴����뷽��˵���� �������ڣ�(2003-10-29 12:11:52)
   */
  private boolean setSendAuditBtnState() {
    boolean b = false;
    if (getInvVOs() == null || getInvVOs().length <= 0) {
      m_btnSendAudit.setEnabled(false);
      return b;
    }

    InvoiceVO curVO = null;
    if (getCurVOPos() > -1) {
      curVO = getInvVOs()[getCurVOPos()];
    }
    // ClientLink cl = new
    // ClientLink(nc.ui.pub.ClientEnvironment.getInstance());//������Ϣ
    if (curVO == null)
      return b;
    if (curVO.getHeadVO().getCoperator() == null) {
      curVO.getHeadVO().setCoperator(nc.ui.pub.ClientEnvironment.getInstance().getUser().getPrimaryKey());
    }
    if (curVO.getHeadVO().getIbillstatus().intValue() == 4 && getCurOperState() == STATE_EDIT) {
      // ����δͨ��,�޸�ʱ,Ӧ����Ϊ����̬
      curVO.getHeadVO().setIbillstatus(new Integer(0));
      hBillStatusBeforeEdit.put(curVO.getHeadVO().getCinvoiceid(), new Integer(4));
    }
    //Ч���Ż� by zhaoyha at 2009.8
    UFBoolean needSend=(UFBoolean) curVO.getHeadVO().getAttributeValue(ConstantVO.EXT_ATTR_ISNEEDSENDAUDIT);
    if(null!=needSend)
      b=needSend.booleanValue();
    // ����
    else 
      b = PuTool.isNeedSendToAudit(nc.vo.scm.pu.BillTypeConst.PO_INVOICE, curVO);
    // if (b){
    // m_btnSendAudit.setEnabled(true);
    // m_btnInvBillAudit.setEnabled(false);
    // }
    // else{
    // m_btnSendAudit.setEnabled(false);
    // if(m_btnInvBillConversion.isEnabled()) {
    // m_btnInvBillAudit.setEnabled(false);
    // // }else {
    // // m_btnInvBillAudit.setEnabled(true);
    // }
    // }
    return b;
  }

  /**
   * ���ߣ���ӡ�� ���ܣ���ʾ��ǰVO�ı��嵽�б���� �������� ���أ��� ���⣺�� ���ڣ�(2002-4-22 11:39:21)
   * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
   */

  private void setVoBodyToListPanle(int nCurIndex) {

    // nCurIndex���������� ��ս���ı���
    if (nCurIndex < 0 || nCurIndex >= getInvVOs().length) {
      getBillListPanel().setBodyValueVO(null);

    }
    else {

      // ���ñ�����Ⱦ���
      // PiPqPublicUIClass.setCurrMoneyDigitToListBody(getInvListPanel(),
      // getInvVOs()[nCurIndex].getHeadVO().getCcurrencytypeid());
      // ����������
      // PiPqPublicUIClass.setVfree0ForAInvoice(getInvVOs()[nCurIndex]);
      InvoiceItemVO bodyVO[] = getInvVOs()[nCurIndex].getBodyVO();
      Vector vTemp = new Vector();
      for (int i = 0; i < bodyVO.length; i++) {
        if (bodyVO[i].getVfree1() != null || bodyVO[i].getVfree2() != null || bodyVO[i].getVfree3() != null
            || bodyVO[i].getVfree4() != null || bodyVO[i].getVfree5() != null)
          vTemp.addElement(bodyVO[i]);
      }
      if (vTemp.size() > 0) {
        InvoiceItemVO tempbodyVO[] = new InvoiceItemVO[vTemp.size()];
        vTemp.copyInto(tempbodyVO);
        new nc.ui.scm.pub.FreeVOParse().setFreeVO(tempbodyVO, "vfree0", "vfree", null, "cmangid", false);
      }
      // ���ý���λ��Ϊ����λ��
      setMaxMnyDigitList(iMaxMnyDigit);
      // ����VO
      getBillListPanel().setBodyValueVO(getInvVOs()[nCurIndex].getBodyVO());
      // ������־���
      try {
        resetBodyValueRelated_Curr(getPk_corp(), getInvVOs()[nCurIndex].getHeadVO().getCcurrencytypeid(),
            getBillListPanel().getBodyBillModel(), new BusinessCurrencyRateUtil(getPk_corp()), getBillListPanel()
                .getBodyBillModel().getRowCount(), m_listPoPubSetUI2);
      }
      catch (Exception e) {
        SCMEnv.out(e);
      }

      // int length = getInvVOs()[nCurIndex].getBodyVO().length;
      // String ccur = null;
      // for(int i = 0 ; i < length ; i ++){
      // ccur = getInvVOs()[nCurIndex].getBodyVO()[i].getCcurrencytypeid();
      // //���ñ�����Ⱦ���
      // PiPqPublicUIClass.setCurrMoneyDigitToListBody(getInvListPanel(), ccur);
      // //������־���
      // resetBodyValueRelated_Curr(getPk_corp(),ccur,getInvListPanel().getBodyBillModel(),new
      // BcurrArith(getPk_corp()),getInvListPanel().getBodyBillModel().getRowCount(),m_listPoPubSetUI2);
      // }
      // ִ�й�ʽ
      getBillListPanel().getBodyBillModel().execLoadFormula();

      // ������Դ��Ϣ
      // PuTool.loadSourceBillData(getInvListPanel(),
      // nc.vo.pu.pub.BillTypeConst.PO_INVOICE);
      // ����Դͷ��Ϣ
      // PuTool.loadAncestorBillData(getInvListPanel(),
      // nc.vo.pu.pub.BillTypeConst.PO_INVOICE);
      if(isNeedLoadSrcInfo(getBillListPanel().getBodyBillModel()))
        PuTool.loadSourceInfoAll(getBillListPanel(), BillTypeConst.PO_INVOICE);
      getBillListPanel().getBodyTable().clearSelection();
    }
  }

  /**
   * ���ߣ���ӡ�� ���ܣ����б�ģ������һ����֪��VO(ת��ר��) �������� ���أ��� ���⣺�� ���ڣ�(2001-7-01 11:39:21)
   * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־�� 2002-09-18 wyf �������Դ�������͡���Դ���ݺŵĴ��� �޸��б������ʾΪ���ù�������
   */
  private void setSpiltVOsToListPanel() {
    cunpos = 0;
    getBillListPanel().getHeadBillModel().clearBodyData();
    getBillListPanel().getBodyBillModel().clearBodyData();
    // V5 Del : setImageType(this.IMAGE_NULL);

    if (getInvVOs() == null)
      return;
    // showHintMessage(getHeadHintText() + CommonConstant.SPACE_MARK +
    // nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401","UPP40040401-000066")/*@res
    // "���ڼ��㹫ʽ"*/ + CommonConstant.SPACE_MARK + CommonConstant.BEGIN_MARK +
    // "......." + CommonConstant.END_MARK);

    if (getCurVOPos() == -1)
      setCurVOPos(0);
    int pos = 0;
    int j = 0;
    int count = 0;
    Vector contains = new Vector();
    Vector poss = new Vector();
    // ����VO������,������ͨ��setBodyVOs()����
    for (int i = 0; i < getInvHVOs().length; i++) {
      if (getInvVOs()[i].getHeadVO().getCinvoiceid() == null) {

        // ����
        getBillListPanel().getHeadBillModel().addLine();
        // �����۱����۸�����
        if (getInvHVOs()[i].getNexchangeotobrate() == null) {

          UFDouble[] d = POPubSetUI.getBothExchRateValue(getPk_corp(), getInvHVOs()[i].getCcurrencytypeid(),
              getInvHVOs()[i].getDarrivedate());

          getInvHVOs()[i].setNexchangeotobrate(d[0]);
        }
        // �����۱����۸����ʵľ���
        PiPqPublicUIClass.setCurrRateDigitToListHead(getBillListPanel(), getInvHVOs()[i].getCcurrencytypeid());
        // ����VO
        if (j == 0 && count == 0) {
          getBillListPanel().getHeadBillModel().setBodyRowVO(getInvHVOs()[i], 0);
          pos = i;
        }
        else {
          getBillListPanel().getHeadBillModel().setBodyRowVO(getInvHVOs()[i], j);
        }
        // pos =1;
        j++;
        count++;
        contains.add(getInvHVOs()[i]);
        poss.add(String.valueOf(i));
      }
    }
    VOsPos = null;
    if (count > 0) {
      VOsPos = new String[poss.size()];
      poss.copyInto(VOsPos);
    }
    // setCurVOPos(0);
    // ִ���б�ͷ��ʽ
    getBillListPanel().getHeadBillModel().execLoadFormula();
    if (count > 0) {
      InvoiceHeaderVO[] invoiceHeaderVOs = new InvoiceHeaderVO[contains.size()];
      contains.copyInto(invoiceHeaderVOs);
      InvoiceHeaderVO invoiceHeaderVO = new InvoiceHeaderVO();
      for (int i = 0; i < count; i++) {
        // 0��ͨ 1�ڳ� 2ϵͳ
        // �ڳ���־
        invoiceHeaderVO = invoiceHeaderVOs[i];
        if (invoiceHeaderVO.getCinvoiceid() == null) {
          if (invoiceHeaderVO.getFinitflag() == null || invoiceHeaderVO.getFinitflag().intValue() == 0) // ��ͨ
            getBillListPanel().getHeadBillModel().setValueAt(new Boolean(false), i, "finitflag");
          else if (invoiceHeaderVO.getFinitflag().intValue() == 1)
            getBillListPanel().getHeadBillModel().setValueAt(new Boolean(true), i, "finitflag");

          // �Ƿ������ı�־
          if (invoiceHeaderVO.getIbillstatus() != null && invoiceHeaderVO.getIbillstatus().intValue() == 3)
            getBillListPanel().getHeadBillModel().setValueAt(new Boolean(true), i, "isaudited");
          else
            getBillListPanel().getHeadBillModel().setValueAt(new Boolean(false), i, "isaudited");

        }

      }
    }

    // wyf 2002-09-18 modify begin
    // setVoBodyToListPanle(getCurVOPos()) ;
    // wyf 2002-09-18 modify begin
    // ��ǰ�Ŵ��ǲ���λ
    // if(count==1){
    getBillListPanel().getHeadTable().setRowSelectionInterval(0, 0);
    // }
    // else{
    // getInvListPanel().getHeadTable().setRowSelectionInterval(pos, pos);
    // }
    dispPlanPrice(false);
    setVoBodyToListPanle(pos);
    cunpos = pos;
    currentPos = pos;
    splitFlag = true;
  }

  /**
   * ���ߣ���ӡ�� ���ܣ����б�ģ������һ����֪��VO �������� ���أ��� ���⣺�� ���ڣ�(2001-7-01 11:39:21)
   * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־�� 2002-09-18 wyf �������Դ�������͡���Դ���ݺŵĴ��� �޸��б������ʾΪ���ù�������
   */
  private void setVOsToListPanel() {

    getBillListPanel().getHeadBillModel().clearBodyData();
    getBillListPanel().getBodyBillModel().clearBodyData();
    // V5 Del : setImageType(this.IMAGE_NULL);

    if (getInvVOs() == null)
      return;

    if (getCurVOPos() == -1)
      setCurVOPos(0);
    // ����VO������,������ͨ��setBodyVOs()����
    for (int i = 0; i < getInvHVOs().length; i++) {
      // ����
      getBillListPanel().getHeadBillModel().addLine();
      getBillListPanel().getHeadBillModel().setBodyRowVO(getInvHVOs()[i], i);

    }

    // ִ���б�ͷ��ʽ
    getBillListPanel().getHeadBillModel().execLoadFormula();

    for (int i = 0; i < getInvVOs().length; i++) {
      // 0��ͨ 1�ڳ� 2ϵͳ
      // �ڳ���־
      if (getInvVOs()[i].getHeadVO().getFinitflag() == null
          || getInvVOs()[i].getHeadVO().getFinitflag().intValue() == 0) // ��ͨ
        getBillListPanel().getHeadBillModel().setValueAt(new Boolean(false), i, "finitflag");
      else if (getInvVOs()[i].getHeadVO().getFinitflag().intValue() == 1)
        getBillListPanel().getHeadBillModel().setValueAt(new Boolean(true), i, "finitflag");

      // �Ƿ������ı�־
      if (getInvVOs()[i].getHeadVO().getIbillstatus() != null
          && getInvVOs()[i].getHeadVO().getIbillstatus().intValue() == 3)
        getBillListPanel().getHeadBillModel().setValueAt(new Boolean(true), i, "isaudited");
      else
        getBillListPanel().getHeadBillModel().setValueAt(new Boolean(false), i, "isaudited");

    }
    getBillListPanel().getHeadTable().setRowSelectionInterval(getCurVOPos(), getCurVOPos());

    dispPlanPrice(false);
  }

  /**
   * ���ߣ���ӡ�� ���ܣ�ר��Ϊ�����������˵���ĵ��ݽ������ ��setVOToBillPanel��Ψһ��������//V5 Del :
   * setImageType()û�� �����������иú������� ��Ҫ�����޸ļ�ɾ���ú�������������������������� �������� ���أ��� ���⣺��
   * ���ڣ�(2002-7-01 11:39:21) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־�� 2002-09-19 wyf ������Դ������Ϣ
   * 2008-10-23 wyf ҵ��Ա�����޸�Ϊ����״
   */
  private void setVOToAuditedBillPanel() {

    // showHintMessage(getHeadHintText() + CommonConstant.SPACE_MARK +
    // nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401","UPP40040401-000066")/*@res
    // "���ڼ��㹫ʽ"*/ + CommonConstant.SPACE_MARK + CommonConstant.BEGIN_MARK +
    // "......." + CommonConstant.END_MARK);
    getBillCardPanel().addNew();
    InvoiceVO curVO = getInvVOs()[getCurVOPos()];
    InvoiceHeaderVO head = getInvVOs()[getCurVOPos()].getHeadVO();

    // ��ͷ�����������ֱ���һ��
    if (head.getCcurrencytypeid() == null || head.getCcurrencytypeid().trim().length() < 1) {
      // ����ķ�Ʊ
      if (curVO.getBodyVO() != null) {
        head.setCcurrencytypeid(curVO.getBodyVO()[0].getCcurrencytypeid());
        head.setNexchangeotobrate(curVO.getBodyVO()[0].getNexchangeotobrate());
        // head.setNexchangeotoarate(curVO.getBodyVO()[0].getNexchangeotoarate());
      }
    }
    // ����������
    // PiPqPublicUIClass.setVfree0ForAInvoice(curVO);
    InvoiceItemVO bodyVO[] = curVO.getBodyVO();
    Vector vTemp = new Vector();
    for (int i = 0; i < bodyVO.length; i++) {
      if (bodyVO[i].getVfree1() != null || bodyVO[i].getVfree2() != null || bodyVO[i].getVfree3() != null
          || bodyVO[i].getVfree4() != null || bodyVO[i].getVfree5() != null)
        vTemp.addElement(bodyVO[i]);
//    //�����Ȿ�����ʾ���
//      setExchangeRateBody(i, false, curVO.getBodyVO()[i]);
    
    }
    if (vTemp.size() > 0) {
      InvoiceItemVO tempbodyVO[] = new InvoiceItemVO[vTemp.size()];
      vTemp.copyInto(tempbodyVO);
      new nc.ui.scm.pub.FreeVOParse().setFreeVO(tempbodyVO, "vfree0", "vfree", null, "cmangid", false);
    }

    // �õ���Ӧ�̻���ID
    String strVendorBase = head.getCvendorbaseid();
    if (head.getCvendormangid() != null && (strVendorBase == null || strVendorBase.trim().equals(""))) {
      // strVendorBase =
      // getResultFromFormula("getColValue(bd_cumandoc,pk_cubasdoc,pk_cumandoc,cvendormangid)","cvendormangid",head.getCvendormangid())
      // ;
      strVendorBase = (String) PiPqPublicUIClass.getAResultFromTable("bd_cumandoc", "pk_cubasdoc", "pk_cumandoc", head
          .getCvendormangid());
      head.setCvendorbaseid(strVendorBase);
    }

    // =============����VOǰ����С��λ
    // ����С��λ��
    setCurrMoneyDigitToBill(head.getCcurrencytypeid());
    // ����VO
    getBillCardPanel().setBillValueVO(curVO);
    // ����
    setCurrRateToBillHead(head.getCcurrencytypeid(), head.getNexchangeotobrate());

    // /////========�绰,��������,�ʺ�,ȡ��Ӧ�̻���ɢ��
    if (curVO.getHeadVO().getCfreecustid() == null) {
      // ɢ���û�,�����Ӧ��Ϣ
      getBillCardPanel().getHeadItem("cfreecustid").setValue(null);
      ((UIRefPane) getBillCardPanel().getHeadItem("caccountbankid").getComponent()).setButtonVisible(true);

      // ��Ӧ�̵绰
      setDefaultPhoneForAVendor(strVendorBase);
      // ��������:
      //UIRefPane pane1 = (UIRefPane) getBillCardPanel().getHeadItem("caccountbankid").getComponent();
      //((AccountsForVendorRefModel) pane1.getRef().getRefModel()).setCvendorbaseid(strVendorBase);
      if (head.getCaccountbankid() != null) {
        //pane1.setPK(head.getCaccountbankid());
        String strAccount = null;
        try {
          Object[][] retOb = nc.ui.pu.pub.PubHelper.queryResultsFromAnyTable("bd_bankaccbas", new String[] {
            "account"
          }, "pk_bankaccbas='" + head.getCaccountbankid() + "'");
          strAccount = (String) retOb[0][0];
        }
        catch (Exception e) {
          SCMEnv.out(e);
          SCMEnv.out("ȡ��Ӧ��Ĭ������ʱ�����쳣!");
        }
        // �ʺ�
        getBillCardPanel().setHeadItem("cvendoraccount", strAccount);
      }
      else {
        if (getCurOperState() == STATE_EDIT) {
          setDefaultBankAccountForAVendor(strVendorBase);
        }
      }
      // �ʺ�
      if (head.getCaccountbankid() != null)
        getBillCardPanel().setHeadItem("cvendoraccount",
            PiPqPublicUIClass.getAResultFromTable("bd_bankaccbas", "account", "pk_bankaccbas", head.getCaccountbankid()));
      }
    else {
      // �������в��ɱ༭,��ť���ɼ�
      ((UIRefPane) getBillCardPanel().getHeadItem("caccountbankid").getComponent()).setButtonVisible(false);
      // Ӱ�����Ϣ
      setDefaultInfoForAFreeCust(curVO.getHeadVO().getCfreecustid());
    }

    // ����ҵ��Ա
    UIRefPane pane2 = (UIRefPane) getBillCardPanel().getHeadItem("cemployeeid").getComponent();
    // wyf 2002-10-23 modify begin
    // ((EmployeesForDeptRefModel)pane2.getRef().getRefModel()).setCdeptid(head.getCdeptid())
    // ;
    // String pk_corp =
    // nc.ui.pub.ClientEnvironment.getInstance().getCorporation().getPrimaryKey();
    // String addWherePart = " and (bd_psndoc.pk_deptdoc in (select
    // bd_deptdoc.pk_deptdoc from bd_deptdoc where (bd_deptdoc.deptattr = '2' or
    // bd_deptdoc.deptattr = '4') and bd_deptdoc.pk_corp ='"+pk_corp+"'))";
    // pane2.setRefModel(new
    // EmployeesForDeptRefModel(head.getCdeptid(),addWherePart));
    String pk_corp = head.getPk_purcorp();
    String s = " bd_psndoc.indocflag='Y' and (bd_psndoc.pk_deptdoc in (select bd_deptdoc.pk_deptdoc from bd_deptdoc where (bd_deptdoc.deptattr = '2' or bd_deptdoc.deptattr = '4') and bd_deptdoc.pk_corp ='"
        + pk_corp + "')) ";
    // pane2.setRefModel(new
    // EmployeesForDeptRefModel(head.getCdeptid(),addWherePart));
    pane2.getRefModel().setWherePart(s);
    // wyf 2002-10-23 modify end
    if (head.getCemployeeid() != null) {
      pane2.setPK(head.getCemployeeid());
    }
    // �ڳ���־
    UICheckBox initCheck = (UICheckBox) getBillCardPanel().getHeadItem("finitflag").getComponent();
    if (head.getFinitflag() == null || head.getFinitflag().intValue() == 0) {
      initCheck.setSelected(false);
    }
    else {
      initCheck.setSelected(true);
    }
    // ��˰���
    int selectedIndex = ((UIComboBox) getBillCardPanel().getHeadItem("idiscounttaxtype").getComponent())
        .getSelectedIndex();
    ((UIComboBox) getBillCardPanel().getHeadItem("idiscounttaxtype").getComponent()).setSelectedIndex(selectedIndex);
    // ��ע
    if(getBillCardPanel().getHeadItem("vmemo").getComponent() instanceof UIRefPane){
      ((UIRefPane) getBillCardPanel().getHeadItem("vmemo").getComponent()).setText(head.getVmemo());
    }

    // ִ�б��幫ʽ
    if (curVO.getBodyVO() != null) {
      getBillCardPanel().getBillModel().execLoadFormula();
    }
 // ȡ��ͷ����
    String sCurrId = getBillCardPanel().getHeadItem("ccurrencytypeid").getValue();
    String sBodyCurrId = null;
    // ��ע��մ�����֤����afterEdit
    if (curVO.getBodyVO() != null) {
      for (int i = 0; i < curVO.getBodyVO().length; i++) {
        if (curVO.getBodyVO()[i].getVmemo() == null) {
          getBillCardPanel().setBodyValueAt("", i, "vmemo");
        }
        // ���������б���Ĭ��Ϊ��ͷ����
        sBodyCurrId = (String) getBillCardPanel().getBillModel().getValueAt(i, "ccurrencytypeid");
        if (sBodyCurrId == null || sBodyCurrId.equals("")) {
          getBillCardPanel().getBillModel().setValueAt(sCurrId, i, "ccurrencytypeid");
          // ��ȡ��������
          // ����������ر��ֵĸĶ�
          afterEditWhenBodyCurrency(new BillEditEvent(getBillCardPanel().getBodyItem("ccurrencytypeid").getComponent(),
              sCurrId, "ccurrencytypeid", i));
          // �����޸ı�־
          getBillCardPanel().getBillModel().setRowState(i, BillModel.MODIFICATION);
          getBillCardPanel().getBillModel().getValueAt(i, "ccurrencytypeid");
          setExchangeRateBody(i, true, null);
        }
        setExchangeRateBody(i, false, curVO.getBodyVO()[i]);
        // ����ԭ\��\������
        // setRowDigits_Mny(getPk_corp(),
        // i,getInvBillPanel().getBillModel(),m_cardPoPubSetUI2);
        // ԭ�ҽ����(ԭ�ҽ��--noriginalcurmny;ԭ��˰��--noriginaltaxmny;ԭ�Ҽ�˰�ϼ�--noriginalsummny)
        getBillCardPanel()
        .setBodyValueAt(getBillCardPanel().getBodyValueAt(i, "noriginalcurmny"), i, "noriginalcurmny");
        getBillCardPanel()
        .setBodyValueAt(getBillCardPanel().getBodyValueAt(i, "noriginaltaxmny"), i, "noriginaltaxmny");
        getBillCardPanel()
        .setBodyValueAt(getBillCardPanel().getBodyValueAt(i, "noriginalsummny"), i, "noriginalsummny");
        getBillCardPanel().setBodyValueAt(getBillCardPanel().getBodyValueAt(i, "noriginalpaymentmny"), i,
        "noriginalpaymentmny");
        
        getBillCardPanel().setBodyValueAt(getBillCardPanel().getBodyValueAt(i, "nmoney"), i, "nmoney");
        getBillCardPanel().setBodyValueAt(getBillCardPanel().getBodyValueAt(i, "ntaxmny"), i, "ntaxmny");
        getBillCardPanel().setBodyValueAt(getBillCardPanel().getBodyValueAt(i, "nsummny"), i, "nsummny");
        getBillCardPanel().setBodyValueAt(getBillCardPanel().getBodyValueAt(i, "npaymentmny"), i, "npaymentmny");
      }
    }
    // wyf 2002-09-18 add begin
    // ������Դ��Ϣ
    // PuTool.loadSourceBillData(getInvBillPanel(),
    // nc.vo.pu.pub.BillTypeConst.PO_INVOICE);
    // wyf 2002-09-18 add end
    // ����Դͷ��Ϣ
    // PuTool.loadAncestorBillData(getInvBillPanel(),
    // nc.vo.pu.pub.BillTypeConst.PO_INVOICE);
    if(isNeedLoadSrcInfo(getBillCardPanel().getBillModel()))
      PuTool.loadSourceInfoAll(getBillCardPanel(), BillTypeConst.PO_INVOICE);
    // ����ͷ��ʽ���ݣ����ⲻ�ɲ��ճ�������
    execHeadTailFormula(curVO);

    // ��Ҫ����SAVEʱ�ı�־
    getBillCardPanel().getBillModel().updateValue();

    if (getCurOperState() == STATE_EDIT) {
      showHintMessage(getHeadHintText() + CommonConstant.SPACE_MARK
          + nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPP40040401-000060")/*
                                                                                           * @res
                                                                                           * "�༭��Ʊ"
                                                                                           */
          + CommonConstant.SPACE_MARK);
    }
    else {
      showHintMessage(getHeadHintText() + CommonConstant.SPACE_MARK
          + nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPP40040401-000032")/*
                                                                                           * @res
                                                                                           * "�����Ʊ"
                                                                                           */
          + CommonConstant.SPACE_MARK);
    }
    getBillCardPanel().getHeadItem("iinvoicetype").setValue(head.getIinvoicetype().intValue());
    setInvoiceTypeComItem();
    UIComboBox comItem = (UIComboBox) getBillCardPanel().getHeadItem("iinvoicetype").getComponent();
    comItem.setSelectedIndex(head.getIinvoicetype().intValue());
  }

  /**
   * ���ߣ���ӡ�� ���ܣ��Ե���ģ������һ����֪��VO !!!!!��ͬʱ���·���setVOToAuditedBillPanel() �������� ���أ���
   * ���⣺�� ���ڣ�(2001-7-01 11:39:21) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־�� 2002-09-18 wyf
   * �������Դ�������͡���Դ���ݺŵĴ��� 2002-09-27 wyf ����Է�Ʊ��������������� 2002-10-23 wyf ҵ��Ա�����޸�Ϊ����״
   * 2003-02-25 fangy �޸�
   */
  private void setVOToBillPanel() {

    nc.vo.scm.pu.Timer timer = new nc.vo.scm.pu.Timer();
    timer.start("setVOToBillPanel");

    // wyf 2002-09-27 add begin
    setInvoiceTypeComItem();
    // wyf 2002-09-27 add end

    if (getInvVOs() == null || getInvVOs().length == 0) {
      // V5 Del : setImageType(this.IMAGE_NULL);

      getBillCardPanel().addNew();

      dispPlanPrice(true);
      return;
    }
    if(getCurVOPos()==-1)
      setCurVOPos(0);
    // added by Fangy 2002-10-11 14:28
    InvoiceVO curVO = getInvVOs()[getCurVOPos()];
    InvoiceHeaderVO head = getInvVOs()[getCurVOPos()].getHeadVO();
    if (curVO.getChildrenVO() == null || curVO.getChildrenVO().length == 0) {
      // װ�ر���
      if (!loadItemsForInvoiceVOs(new InvoiceVO[] {
        curVO
      })) {
        setCurVOPos(m_nLstInvVOPos);
        dispPlanPrice(true);
        return;
      }
    }
    timer.addExecutePhase("loadItemsForInvoiceVOs");

    // ���õ�ǰ���ݵ�ҵ������.
    setCurBizeType(head.getCbiztype());

    // ����������
    InvoiceItemVO[] items = (InvoiceItemVO[]) curVO.getChildrenVO();
    Vector vTemp = new Vector();
    if (items != null && items.length > 0) {
      for (int i = 0; i < items.length; i++) {
        if (items[i].getVfree1() != null || items[i].getVfree2() != null || items[i].getVfree3() != null
            || items[i].getVfree4() != null || items[i].getVfree5() != null) {
          vTemp.addElement(items[i]);
        }
      }
      if (vTemp.size() > 0) {
        InvoiceItemVO[] bodyVO = new InvoiceItemVO[vTemp.size()];
        vTemp.copyInto(bodyVO);
        new nc.ui.scm.pub.FreeVOParse().setFreeVO(bodyVO, "vfree0", "vfree", null, "cmangid", false);
      }
    }

    timer.addExecutePhase("����������");

    // �õ���Ӧ�̻���ID
    String strVendorBase = head.getCvendorbaseid();
    if (head.getCvendormangid() != null && (strVendorBase == null || strVendorBase.trim().equals(""))) {
      // strVendorBase =
      // getResultFromFormula("getColValue(bd_cumandoc,pk_cubasdoc,pk_cumandoc,cvendormangid)","cvendormangid",head.getCvendormangid())
      // ;
      Object oTemp = null;
      try {
        oTemp = CacheTool.getColumnValue("bd_cumandoc", "pk_cumandoc", "pk_cubasdoc", new String[] {
          head.getCvendormangid()
        });
      }
      catch (Exception e) {
        /** �����׳� */
        SCMEnv.out(e);
      }
      if (oTemp != null) {
        Object oo[] = (Object[]) oTemp;
        if (oo != null && oo.length > 0 && oo[0] != null)
          strVendorBase = oo[0].toString();
      }
      head.setCvendorbaseid(strVendorBase);
    }

    timer.addExecutePhase("�õ���Ӧ�̻���PiPqPublicUIClass.getAResultFromTable");

    // ���ò���
    String pk_corp = head.getPk_purcorp();
    UIRefPane pane2 = (UIRefPane) getBillCardPanel().getHeadItem("cdeptid").getComponent();
    // pane2.getRefModel().setWherePart(" (bd_deptdoc.deptattr = '2' or
    // bd_deptdoc.deptattr = '4') and bd_deptdoc.pk_corp = '" + pk_corp + "' ");
    pane2.getRefModel().setPk_corp(pk_corp);

    // ����ҵ��Ա
    pane2 = (UIRefPane) getBillCardPanel().getHeadItem("cemployeeid").getComponent();
    // wyf 2002-10-23 modify begin
    // ((EmployeesForDeptRefModel)pane2.getRef().getRefModel()).setCdeptid(head.getCdeptid())
    // ;
    // String s = " bd_psndoc.indocflag='Y' and (bd_psndoc.pk_deptdoc in (select
    // bd_deptdoc.pk_deptdoc from bd_deptdoc where (bd_deptdoc.deptattr = '2' or
    // bd_deptdoc.deptattr = '4') and bd_deptdoc.pk_corp ='"+pk_corp+"')) ";
    // pane2.setRefModel(new
    // EmployeesForDeptRefModel(head.getCdeptid(),addWherePart));
    // pane2.getRefModel().setWherePart(s);
    pane2.getRefModel().setPk_corp(pk_corp);
    // wyf 2002-10-23 modify end
    if (head.getCemployeeid() != null) {
      pane2.setPK(head.getCemployeeid());
    }
    //���¸��ݱ������û����ʾ��� by zhaoyha at 2009.9
    setVoExchgRateDigit(items);
    // ����VO
    getBillCardPanel().setBillValueVO(curVO);

    // =============����VO������С��λ

    // ����
    // ���ñ�ͷ���¼�����ʾ��ȡ��һ�е�ֵ
    InvoiceItemVO voFirstItem = curVO.getBodyVO()[0];

    getBillCardPanel().getHeadItem("ccurrencytypeid").setValue(voFirstItem.getCcurrencytypeid());
    head.setCcurrencytypeid(voFirstItem.getCcurrencytypeid());
    // ���ñ�ͷ���־���
    resetHeadCurrDigits();
    getBillCardPanel().getHeadItem("nexchangeotobrate").setValue(voFirstItem.getNexchangeotobrate());
    head.setNexchangeotobrate(voFirstItem.getNexchangeotobrate());

      
    // getInvBillPanel().getHeadItem("cprojectid").setValue(voFirstItem.getCprojectid());
    if (voFirstItem.getIdiscounttaxtype() != null && voFirstItem.getIdiscounttaxtype().toString().trim().length() > 0) {
      getBillCardPanel().getHeadItem("idiscounttaxtype").setValue(voFirstItem.getIdiscounttaxtype());
    }
    else {
      getBillCardPanel().getHeadItem("idiscounttaxtype").setValue(new Integer(1));
    }
    getBillCardPanel().getHeadItem("ntaxrate").setValue(voFirstItem.getNtaxrate());
    setCurrRateToBillHead(head.getCcurrencytypeid(), head.getNexchangeotobrate());
    // ����С��λ��
    // setCurrMoneyDigitToBill(head.getCcurrencytypeid());

    // /////========�绰,��������,�ʺ�,ȡ��Ӧ�̻���ɢ��
    if (curVO.getHeadVO().getCfreecustid() == null) {
      // ɢ���û�,�����Ӧ��Ϣ
      getBillCardPanel().getHeadItem("cfreecustid").setValue(null);
      ((UIRefPane) getBillCardPanel().getHeadItem("caccountbankid").getComponent()).setButtonVisible(true);

      // ��Ӧ�̵绰
      setDefaultPhoneForAVendor(strVendorBase);
      // ��������:
      UIRefPane pane1 = (UIRefPane) getBillCardPanel().getHeadItem("caccountbankid").getComponent();
      //((AccountsForVendorRefModel) pane1.getRef().getRefModel()).setCvendorbaseid(strVendorBase);
      if (head.getCaccountbankid() != null) {
        pane1.setPK(head.getCaccountbankid());
        String strAccount = null;
        try {
          Object oTemp = CacheTool.getColumnValue("bd_bankaccbas", "pk_bankaccbas", "accountname", new String[] {
            head.getCaccountbankid()
          });
          if (oTemp != null)
            strAccount = ((Object[]) oTemp)[0] == null ? null : ((Object[]) oTemp)[0].toString();

          timer.addExecutePhase("queryResultsFromAnyTable");
        }
        catch (Exception e) {
          SCMEnv.out(e);
          SCMEnv.out("ȡ��Ӧ��Ĭ������ʱ�����쳣!");
        }
        // �ʺ�
        getBillCardPanel().setHeadItem("cvendoraccount", strAccount);
      }
      else {
        if (getCurOperState() == STATE_EDIT) {
          setDefaultBankAccountForAVendor(strVendorBase);
        }
      }
      // //�ʺ�
      // if (head.getCaccountbankid() != null){
      // Object oTemp = null;
      // try {
      // oTemp = CacheTool.getColumnValue("bd_custbank", "pk_custbank",
      // "account",new String[]{head.getCaccountbankid()});
      // } catch (Exception e) {
      // /**�����׳�*/
      // SCMEnv.out(e);
      // }
      // if(oTemp != null)
      // getInvBillPanel().setHeadItem("cvendoraccount",((Object[])oTemp)[0].toString());
      //
      // timer.addExecutePhase("PiPqPublicUIClass.getAResultFromTable");
      // }

    }
    else {
      // �������в��ɱ༭,��ť���ɼ�
      ((UIRefPane) getBillCardPanel().getHeadItem("caccountbankid").getComponent()).setButtonVisible(false);
      // Ӱ�����Ϣ
      setDefaultInfoForAFreeCust(curVO.getHeadVO().getCfreecustid());
    }

    getBillCardPanel().getHeadItem("nexchangeotobrate").getValue();

    this.loadBDData();

    timer.addExecutePhase("loadBDData");

    // �ڳ���־
    UICheckBox initCheck = (UICheckBox) getBillCardPanel().getHeadItem("finitflag").getComponent();
    if (head.getFinitflag() == null || head.getFinitflag().intValue() == 0) {
      initCheck.setSelected(false);
    }
    else {
      initCheck.setSelected(true);
    }
    // ��˰���
    int selectedIndex = ((UIComboBox) getBillCardPanel().getHeadItem("idiscounttaxtype").getComponent())
        .getSelectedIndex();
    ((UIComboBox) getBillCardPanel().getHeadItem("idiscounttaxtype").getComponent()).setSelectedIndex(selectedIndex);
    // ��ע
    if(getBillCardPanel().getHeadItem("vmemo").getComponent() instanceof UIRefPane){
      ((UIRefPane) getBillCardPanel().getHeadItem("vmemo").getComponent()).setText(head.getVmemo());
    }

    // ִ�б��幫ʽ
    if (curVO.getBodyVO() != null) {
      getBillCardPanel().getBillModel().execLoadFormula();
    }

    timer.addExecutePhase("execLoadFormula");

    getBillCardPanel().getHeadItem("nexchangeotobrate").getValue();

    // ��ע��մ�����֤����afterEdit
    // ���ý���λ��Ϊ����λ��
    setMaxMnyDigit(iMaxMnyDigit);
    // ȡ��ͷ����
    String sCurrId = getBillCardPanel().getHeadItem("ccurrencytypeid").getValue();
    String sBodyCurrId = null;
    //Ч���Ż� by zhaoyha at 2009.9
    boolean isNeedCal=getBillCardPanel().getBillModel().isNeedCalculate();
    getBillCardPanel().getBillModel().setNeedCalculate(false);
    if (curVO.getBodyVO() != null) {
      for (int i = 0; i < curVO.getBodyVO().length; i++) {
        if (curVO.getBodyVO()[i].getVmemo() == null) {
          getBillCardPanel().setBodyValueAt("", i, "vmemo");
        }
        // ���������б���Ĭ��Ϊ��ͷ����
        sBodyCurrId = (String) getBillCardPanel().getBillModel().getValueAt(i, "ccurrencytypeid");
        if (sBodyCurrId == null || sBodyCurrId.equals("")) {
          getBillCardPanel().getBillModel().setValueAt(sCurrId, i, "ccurrencytypeid");
          // ��ȡ��������
          // ����������ر��ֵĸĶ�
          afterEditWhenBodyCurrency(new BillEditEvent(getBillCardPanel().getBodyItem("ccurrencytypeid").getComponent(),
              sCurrId, "ccurrencytypeid", i));
          // �����޸ı�־
          getBillCardPanel().getBillModel().setRowState(i, BillModel.MODIFICATION);
          getBillCardPanel().getBillModel().getValueAt(i, "ccurrencytypeid");
          setExchangeRateBody(i, true, null);
        }
        setExchangeRateBody(i, false, curVO.getBodyVO()[i]);
        // ����ԭ\��\������
        // setRowDigits_Mny(getPk_corp(),
        // i,getInvBillPanel().getBillModel(),m_cardPoPubSetUI2);
        // ԭ�ҽ����(ԭ�ҽ��--noriginalcurmny;ԭ��˰��--noriginaltaxmny;ԭ�Ҽ�˰�ϼ�--noriginalsummny)
        getBillCardPanel()
            .setBodyValueAt(getBillCardPanel().getBodyValueAt(i, "noriginalcurmny"), i, "noriginalcurmny");
        getBillCardPanel()
            .setBodyValueAt(getBillCardPanel().getBodyValueAt(i, "noriginaltaxmny"), i, "noriginaltaxmny");
        getBillCardPanel()
            .setBodyValueAt(getBillCardPanel().getBodyValueAt(i, "noriginalsummny"), i, "noriginalsummny");
        getBillCardPanel().setBodyValueAt(getBillCardPanel().getBodyValueAt(i, "noriginalpaymentmny"), i,
            "noriginalpaymentmny");

        getBillCardPanel().setBodyValueAt(getBillCardPanel().getBodyValueAt(i, "nmoney"), i, "nmoney");
        getBillCardPanel().setBodyValueAt(getBillCardPanel().getBodyValueAt(i, "ntaxmny"), i, "ntaxmny");
        getBillCardPanel().setBodyValueAt(getBillCardPanel().getBodyValueAt(i, "nsummny"), i, "nsummny");
        getBillCardPanel().setBodyValueAt(getBillCardPanel().getBodyValueAt(i, "npaymentmny"), i, "npaymentmny");
      }
    }


    // ������Դ��Ϣ,����Դͷ��Ϣ

    if(isNeedLoadSrcInfo(getBillCardPanel().getBillModel()))
      PuTool.loadSourceInfoAll(getBillCardPanel(), BillTypeConst.PO_INVOICE);
    timer.addExecutePhase("������Դ��Դͷ");

    // ��Ҫ����SAVEʱ�ı�־
    getBillCardPanel().getBillModel().updateValue();

    // ����ͷ��ʽ���ݣ����ⲻ�ɲ��ճ�������
    execHeadTailFormula(curVO);
    getBillCardPanel().execHeadEditFormulas();

    timer.addExecutePhase("execHeadTailFormula");

    // ----------------------------
    // modified by Czp
    // int iStatus = 0;
    // if (curVO.getHeadVO().getIbillstatus() != null)
    // iStatus = curVO.getHeadVO().getIbillstatus().intValue();
    // if (iStatus == 0)
    // V5 Del : setImageType(this.IMAGE_NULL);
    // else if (iStatus == 1)
    // V5 Del : setImageType(this.IMAGE_CANCEL);
    // else if (iStatus == 2)
    // V5 Del : setImageType(this.IMAGE_APPROVING);
    // else if (iStatus == 3)
    // V5 Del : setImageType(this.IMAGE_AUDIT);
    // else if (iStatus == 4)
    // V5 Del : setImageType(this.IMAGE_APPROVEANDFAIL);
    // else
    // V5 Del : setImageType(this.IMAGE_NULL);
    // ----------------------------
    if (getCurOperState() == STATE_EDIT) {
      // ----------------------------
      // modified by Czp
      // V5 Del : setImageType(this.IMAGE_NULL);
      // ----------------------------
      showHintMessage(getHeadHintText() + CommonConstant.SPACE_MARK
          + nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPP40040401-000060")/*
                                                                                           * @res
                                                                                           * "�༭��Ʊ"
                                                                                           */
          + CommonConstant.SPACE_MARK);
    }
    else {
      showHintMessage(getHeadHintText() + CommonConstant.SPACE_MARK
          + nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPP40040401-000032")/*
                                                                                           * @res
                                                                                           * "�����Ʊ"
                                                                                           */
          + CommonConstant.SPACE_MARK);
    }
    timer.addExecutePhase("��������");

    timer.showAllExecutePhase("setVOToBillPanel");

    dispPlanPrice(true);

    // ���òɹ���֯
    String cpurorganization = head.getCpurorganization();
    String strFormula = null;
    UIRefPane refpnl = null;
    strFormula = "cpurorganizationname->getColValue(bd_purorg,name,pk_purorg,cpurorganization)";
    Object ob = getBillCardPanel().execHeadFormula(strFormula);
    if (getBillCardPanel().getHeadItem("cpurorganizationname") != null) {
      refpnl = (UIRefPane) getBillCardPanel().getHeadItem("cpurorganizationname").getComponent();
      refpnl.setValue((String) ob);
    }

    // �ջ���˾
    String pk_arrvcorp = head.getPk_arrvcorp();
    strFormula = "arrvcorpname->getColValue(bd_corp,unitname,pk_corp,pk_arrvcorp)";
    ob = getBillCardPanel().execHeadFormula(strFormula);
    if (getBillCardPanel().getHeadItem("arrvcorpname") != null) {
      refpnl = (UIRefPane) getBillCardPanel().getHeadItem("arrvcorpname").getComponent();
      refpnl.setValue((String) ob);
    }
    
    //Ч���Ż� by zhaoyha at 2009.9
    getBillCardPanel().getBillModel().setNeedCalculate(isNeedCal);

  }

  /**
   * ���ߣ���־ƽ ���ܣ������۱����۸�����С��λ �������� ���أ��� ���⣺�� ���ڣ�2005-6-17 14:39:21
   * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־
   */
  private void resetHeadCurrDigits() {
    // =====��ͷ
    String sCurrId = getBillCardPanel().getHeadItem("ccurrencytypeid").getValue();
    if (sCurrId != null && sCurrId.trim().length() > 0) {
      // �õ����ʾ���
      int[] iaExchRateDigit = m_cardPoPubSetUI2.getBothExchRateDigit(getPk_corp(), sCurrId);
      // ���þ���
      getBillCardPanel().getHeadItem("nexchangeotobrate").setDecimalDigits(iaExchRateDigit[0]);
    }
  }

  /**
   * ���ߣ�WYF ���ܣ����ñ���������йص�ֵ�ľ��ȣ��˷������б����ͬʱʹ�� ������String pk_corp ��˾ID BillModel
   * billModel BillModel ���أ��� ���⣺�� ���ڣ�(2004-6-11 11:39:21) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
   */
  protected static void resetBodyValueRelated_Curr(String pk_corp, String strHeadCurId, BillModel billModel,
      BusinessCurrencyRateUtil bca, int iLen, POPubSetUI2 setUi) {
    // =====����
    // �������ϼ�
    String[] saMnyItem = new String[] {
        "noriginalcurmny", "noriginaltaxmny", "noriginalsummny", "noriginalpaymentmny", "nmoney", "ntaxmny", "nsummny",
        "npaymentmny"
    };
    int iMnyLen = saMnyItem.length;
    // �������λ��
    int iMaxMnyDigit = 0;

    // ��Ϊ���ϼƣ�����ռ�÷ǳ����ʱ��
    boolean bOldNeedCalculate = billModel.isNeedCalculate();
    billModel.setNeedCalculate(false);

    for (int i = 0; i < iLen; i++) {
      // �۱����۸�����
      setRowDigits_ExchangeRate(pk_corp, i, billModel, setUi);
      billModel.setValueAt(billModel.getValueAt(i, "nexchangeotobrate"), i, "nexchangeotobrate");
      // ��˰���˰�ϼ�
      // setRowDigits_Mny(pk_corp, i, billModel,setUi);
      for (int j = 0; j < iMnyLen; j++) {
        billModel.setValueAt(billModel.getValueAt(i, saMnyItem[j]), i, saMnyItem[j]);
      }
      if (billModel.getItemByKey(saMnyItem[0]).getDecimalDigits() > iMaxMnyDigit) {
        iMaxMnyDigit = billModel.getItemByKey(saMnyItem[0]).getDecimalDigits();
      }
    }

    // ����ϼ�
    for (int i = 0; i < iMnyLen; i++) {
      billModel.getItemByKey(saMnyItem[0]).setDecimalDigits(iMaxMnyDigit);
      billModel.reCalcurate(billModel.getBodyColByKey(saMnyItem[i]));
    }
    billModel.setNeedCalculate(bOldNeedCalculate);
  }

  private void dispPlanPrice(boolean bCard) {

    // ��ʾ�ƻ���*****
    if (bCard && getBillCardPanel().getRowCount() > 0) {
      BillItem item = getBillCardPanel().getBodyItem("nplanprice");
      if (item != null && item.isShow()) {
        int nRowCount = getBillCardPanel().getRowCount();
        String cMangID[] = new String[nRowCount];
        for (int i = 0; i < nRowCount; i++)
          cMangID[i] = (String) getBillCardPanel().getBodyValueAt(i, "cmangid");
        UFDouble nPrice[] = queryPlanPrices(cMangID, getBillCardPanel().getHeadItem("cstoreorganization").getValue());
        if (nPrice != null) {
          for (int i = 0; i < nRowCount; i++)
            getBillCardPanel().setBodyValueAt(nPrice[i], i, "nplanprice");
        }

      }
    }
    else if (!bCard && getBillListPanel().getBodyBillModel().getRowCount() > 0) {
      BillItem item = getBillListPanel().getBodyItem("nplanprice");
      if (item != null && item.isShow()) {
        int nRowCount = getBillListPanel().getBodyBillModel().getRowCount();
        String cMangID[] = new String[nRowCount];
        for (int i = 0; i < nRowCount; i++)
          cMangID[i] = (String) getBillListPanel().getBodyBillModel().getValueAt(i, "cmangid");
        UFDouble nPrice[] = queryPlanPrices(cMangID, (String) getBillListPanel().getHeadBillModel().getValueAt(
            getCurVOPos(), "cstoreorganization"));
        if (nPrice != null) {
          for (int i = 0; i < nRowCount; i++)
            getBillListPanel().getBodyBillModel().setValueAt(nPrice[i], i, "nplanprice");
        }
      }
    }
    // *****

  }

  /**
   * ���ߣ���ӡ�� ���ܣ��ڿ�Ƭ���б�֮���л� �������� ���أ��� ���⣺�� ���ڣ�(2001-7-01 11:39:21)
   * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־�� 2002-08-20 wyf ȥ����Ʊ���ע��
   */
  private void shiftShowModeTo(int stateIndex) {
    if (stateIndex == INV_PANEL_LIST && getCurPanelMode() != INV_PANEL_LIST) {
      // wyf 2002-08-20 delete begin
      // setTitleText("ά����Ʊ") ;
      // wyf 2002-08-20 delete end
      remove(getCurPanel());
      setCurPanelMode(INV_PANEL_LIST);
      initList();
      updateUI();
//    setButtons(getBtnss());
      setButtons(m_btnTree.getButtonArray());
      // setButtonsAndPanelState();
      updateButtons();
    }
    else if (stateIndex == INV_PANEL_CARD && getCurPanelMode() != INV_PANEL_CARD) {
      // wyf 2002-08-20 delete begin
      // setTitleText("ά����Ʊ") ;
      // wyf 2002-08-20 delete end
      remove(getCurPanel());
      setCurPanelMode(INV_PANEL_CARD);
      initCard();
      updateUI();
      //setButtons(getBtnss());
      setButtons(m_btnTree.getButtonArray());
      // setButtonsAndPanelState();
      updateButtons();
    }

  }

  /**
   * ���ߣ���ӡ�� ���ܣ����б�ģ������һ����֪��VO �������� ���أ��� ���⣺�� ���ڣ�(2001-7-01 11:39:21)
   * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־�� 2002-09-18 wyf �������Դ�������͡���Դ���ݺŵĴ��� �޸��б������ʾΪ���ù�������
   * 2003-02-21 ����, Ч���޸�,�����޸�,��ѡ��ѡ�޸�
   */
  private void showSelectedInvoice() {

    // װ�ر���
    int row = getCurVOPos();

    // ���±�ͷ�б�.
    if (row >= 0 && row < getInvVOs().length) {
      InvoiceVO curVO = getInvVOs()[row];
      if (!loadItemsForInvoiceVOs(new InvoiceVO[] {
        curVO
      })) {
        setVoBodyToListPanle(-1);
      }
      else {
        // ���ñ����б�.
        setVoBodyToListPanle(row);

      }
    }
    else {
      setVoBodyToListPanle(-1);
    }
  }

  /**
   * ���ߣ���ӡ�� ���ܣ�ʵ��ListSelectionListener�ļ������� ������ListSelectionEvent e �����¼� ���أ���
   * ���⣺�� ���ڣ�(2002-5-23 11:39:21) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־�� 2003-02-21 ����,
   * Ч���޸�,�����޸�,��ѡ��ѡ�޸�
   */
  public void valueChanged(ListSelectionEvent e) {

    // if(e.getValueIsAdjusting())
    // return;

    int iCount = getBillListPanel().getHeadTable().getRowCount();
    for (int i = 0; i < iCount; i++) {
      getBillListPanel().getHeadBillModel().setRowState(i, BillModel.NORMAL);
    }

    // �õ���ѡ�е���
    int[] iaSelectedRow = getBillListPanel().getHeadTable().getSelectedRows();
    if (iaSelectedRow == null || iaSelectedRow.length == 0) {
      setSelectedRowCount(0);
    }
    else {
      iCount = iaSelectedRow.length;
      // ѡ�е��б�ʾΪ�򣪺�
      for (int i = 0; i < iCount; i++)
        getBillListPanel().getHeadBillModel().setRowState(iaSelectedRow[i], BillModel.SELECTED);
      // Ӱ�찴ť�߼�
      setSelectedRowCount(iCount);
      if (iCount == 1) {
        // ��ѡʱ,����ʾ����
        int nCurIndex = PuTool.getIndexBeforeSort(getBillListPanel(), iaSelectedRow[0]);
        setCurVOPos(nCurIndex);
      }
      else {
        // ��ѡʱ,����ʾ�κα���
        setCurVOPos(-1);
      }
      showSelectedInvoice();
    }
    // ���°�ť״̬
    setButtonsAndPanelState();
    updateButtons();
  }

  /**
   * ��������:�Զ������PK(����)
   */
  private void setBodyDefPK(BillEditEvent event) {
    if (event.getKey().equals("vdef1")) {
      DefSetTool.afterEditBody(getBillCardPanel().getBillModel(), event.getRow(), "vdef1", "pk_defdoc1");
    }
    else if (event.getKey().equals("vdef2")) {
      DefSetTool.afterEditBody(getBillCardPanel().getBillModel(), event.getRow(), "vdef2", "pk_defdoc2");
    }
    else if (event.getKey().equals("vdef3")) {
      DefSetTool.afterEditBody(getBillCardPanel().getBillModel(), event.getRow(), "vdef3", "pk_defdoc3");
    }
    else if (event.getKey().equals("vdef4")) {
      DefSetTool.afterEditBody(getBillCardPanel().getBillModel(), event.getRow(), "vdef4", "pk_defdoc4");
    }
    else if (event.getKey().equals("vdef5")) {
      DefSetTool.afterEditBody(getBillCardPanel().getBillModel(), event.getRow(), "vdef5", "pk_defdoc5");
    }
    else if (event.getKey().equals("vdef6")) {
      DefSetTool.afterEditBody(getBillCardPanel().getBillModel(), event.getRow(), "vdef6", "pk_defdoc6");
    }
    else if (event.getKey().equals("vdef7")) {
      DefSetTool.afterEditBody(getBillCardPanel().getBillModel(), event.getRow(), "vdef7", "pk_defdoc7");
    }
    else if (event.getKey().equals("vdef8")) {
      DefSetTool.afterEditBody(getBillCardPanel().getBillModel(), event.getRow(), "vdef8", "pk_defdoc8");
    }
    else if (event.getKey().equals("vdef9")) {
      DefSetTool.afterEditBody(getBillCardPanel().getBillModel(), event.getRow(), "vdef9", "pk_defdoc9");
    }
    else if (event.getKey().equals("vdef10")) {
      DefSetTool.afterEditBody(getBillCardPanel().getBillModel(), event.getRow(), "vdef10", "pk_defdoc10");
    }
    else if (event.getKey().equals("vdef11")) {
      DefSetTool.afterEditBody(getBillCardPanel().getBillModel(), event.getRow(), "vdef11", "pk_defdoc11");
    }
    else if (event.getKey().equals("vdef12")) {
      DefSetTool.afterEditBody(getBillCardPanel().getBillModel(), event.getRow(), "vdef12", "pk_defdoc12");
    }
    else if (event.getKey().equals("vdef13")) {
      DefSetTool.afterEditBody(getBillCardPanel().getBillModel(), event.getRow(), "vdef13", "pk_defdoc13");
    }
    else if (event.getKey().equals("vdef14")) {
      DefSetTool.afterEditBody(getBillCardPanel().getBillModel(), event.getRow(), "vdef14", "pk_defdoc14");
    }
    else if (event.getKey().equals("vdef15")) {
      DefSetTool.afterEditBody(getBillCardPanel().getBillModel(), event.getRow(), "vdef15", "pk_defdoc15");
    }
    else if (event.getKey().equals("vdef16")) {
      DefSetTool.afterEditBody(getBillCardPanel().getBillModel(), event.getRow(), "vdef16", "pk_defdoc16");
    }
    else if (event.getKey().equals("vdef17")) {
      DefSetTool.afterEditBody(getBillCardPanel().getBillModel(), event.getRow(), "vdef17", "pk_defdoc17");
    }
    else if (event.getKey().equals("vdef18")) {
      DefSetTool.afterEditBody(getBillCardPanel().getBillModel(), event.getRow(), "vdef18", "pk_defdoc18");
    }
    else if (event.getKey().equals("vdef19")) {
      DefSetTool.afterEditBody(getBillCardPanel().getBillModel(), event.getRow(), "vdef19", "pk_defdoc19");
    }
    else if (event.getKey().equals("vdef20")) {
      DefSetTool.afterEditBody(getBillCardPanel().getBillModel(), event.getRow(), "vdef20", "pk_defdoc20");
    }
  }

  /**
   * ��������:�Զ������PK(��ͷ)
   */
  private void setHeadDefPK(BillEditEvent event) {
    if (event.getKey().equals("vdef1")) {
      DefSetTool.afterEditHead(getBillCardPanel().getBillData(), "vdef1", "pk_defdoc1");
    }
    else if (event.getKey().equals("vdef2")) {
      DefSetTool.afterEditHead(getBillCardPanel().getBillData(), "vdef2", "pk_defdoc2");
    }
    else if (event.getKey().equals("vdef3")) {
      DefSetTool.afterEditHead(getBillCardPanel().getBillData(), "vdef3", "pk_defdoc3");
    }
    else if (event.getKey().equals("vdef4")) {
      DefSetTool.afterEditHead(getBillCardPanel().getBillData(), "vdef4", "pk_defdoc4");
    }
    else if (event.getKey().equals("vdef5")) {
      DefSetTool.afterEditHead(getBillCardPanel().getBillData(), "vdef5", "pk_defdoc5");
    }
    else if (event.getKey().equals("vdef6")) {
      DefSetTool.afterEditHead(getBillCardPanel().getBillData(), "vdef6", "pk_defdoc6");
    }
    else if (event.getKey().equals("vdef7")) {
      DefSetTool.afterEditHead(getBillCardPanel().getBillData(), "vdef7", "pk_defdoc7");
    }
    else if (event.getKey().equals("vdef8")) {
      DefSetTool.afterEditHead(getBillCardPanel().getBillData(), "vdef8", "pk_defdoc8");
    }
    else if (event.getKey().equals("vdef9")) {
      DefSetTool.afterEditHead(getBillCardPanel().getBillData(), "vdef9", "pk_defdoc9");
    }
    else if (event.getKey().equals("vdef10")) {
      DefSetTool.afterEditHead(getBillCardPanel().getBillData(), "vdef10", "pk_defdoc10");
    }
    else if (event.getKey().equals("vdef11")) {
      DefSetTool.afterEditHead(getBillCardPanel().getBillData(), "vdef11", "pk_defdoc11");
    }
    else if (event.getKey().equals("vdef12")) {
      DefSetTool.afterEditHead(getBillCardPanel().getBillData(), "vdef12", "pk_defdoc12");
    }
    else if (event.getKey().equals("vdef13")) {
      DefSetTool.afterEditHead(getBillCardPanel().getBillData(), "vdef13", "pk_defdoc13");
    }
    else if (event.getKey().equals("vdef14")) {
      DefSetTool.afterEditHead(getBillCardPanel().getBillData(), "vdef14", "pk_defdoc14");
    }
    else if (event.getKey().equals("vdef15")) {
      DefSetTool.afterEditHead(getBillCardPanel().getBillData(), "vdef15", "pk_defdoc15");
    }
    else if (event.getKey().equals("vdef16")) {
      DefSetTool.afterEditHead(getBillCardPanel().getBillData(), "vdef16", "pk_defdoc16");
    }
    else if (event.getKey().equals("vdef17")) {
      DefSetTool.afterEditHead(getBillCardPanel().getBillData(), "vdef17", "pk_defdoc17");
    }
    else if (event.getKey().equals("vdef18")) {
      DefSetTool.afterEditHead(getBillCardPanel().getBillData(), "vdef18", "pk_defdoc18");
    }
    else if (event.getKey().equals("vdef19")) {
      DefSetTool.afterEditHead(getBillCardPanel().getBillData(), "vdef19", "pk_defdoc19");
    }
    else if (event.getKey().equals("vdef20")) {
      DefSetTool.afterEditHead(getBillCardPanel().getBillData(), "vdef20", "pk_defdoc20");
    }
  }

  /**
   * ���ߣ���ά�� ���ܣ������ӡ���� �������� ���أ��� ���⣺�� ���ڣ�(2004-12-15 11:39:21)
   */
  private void onCardPrint() {
    // ��ӡʱ��Ҫ�ٽ������� For V56 by zhaoyha at 2009.3.26
    getBillCardPanel().getBillModel().setSortColumn(null);
    InvoiceVO vo = (InvoiceVO) getInvVOs()[getCurVOPos()];
    ArrayList aryRslt = new ArrayList();
    aryRslt.add(vo);
    if (printCard == null) {
      printCard = new ScmPrintTool(this, getBillCardPanel(), aryRslt, getModuleCode());
    }
    else {
      try {
        printCard.setData(aryRslt);
      }
      catch (Exception e1) {
        SCMEnv.out(e1);
        return;
      }
    }
    try {
      //�����β��գ���ֹ��ӡʱ������� by zhaoyha at 2009.3.27
      //List<BillItem> maskedItems=PuTool.maskUIRefType(getBillCardPanel());
      printCard.onCardPrint(getBillCardPanel(), getBillListPanel(), ScmConst.PO_Invoice);
      if (PuPubVO.getString_TrimZeroLenAsNull(printCard.getPrintMessage()) != null) {
        MessageDialog.showHintDlg(this,
            nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000270")/*
                                                                                             * @res
                                                                                             * "��ʾ"
                                                                                             */, printCard
                .getPrintMessage());
      }
      //�ָ���ǰ���εĲ���
      //PuTool.restoreUIRefType(maskedItems);
    }
    catch (BusinessException e) {
      SCMEnv.out(e);
    }
  }

  /**
   * ���ߣ���ά�� ���ܣ������ӡ���� �������� ���أ��� ���⣺�� ���ڣ�(2004-12-15 11:39:21)
   */
  private void onCardPrintPreview() {
    // ��ӡʱ��Ҫ�ٽ������� For V56 by zhaoyha at 2009.3.26
    getBillCardPanel().getBillModel().setSortColumn(null);   
    InvoiceVO vo = (InvoiceVO) getInvVOs()[getCurVOPos()];
    ArrayList aryRslt = new ArrayList();
    aryRslt.add(vo);
    if (printCard == null) {
      printCard = new ScmPrintTool(this, getBillCardPanel(), aryRslt, getModuleCode());
    }
    else {
      try {
        printCard.setData(aryRslt);
      }
      catch (Exception e1) {
        SCMEnv.out(e1);
        return;
      }
    }
    try {
      //�����β��գ���ֹ��ӡʱ������� by zhaoyha at 2009.3.27
      //List<BillItem> maskedItems=PuTool.maskUIRefType(getBillCardPanel());
      printCard.onCardPrintPreview(getBillCardPanel(), getBillListPanel(), ScmConst.PO_Invoice);
      if (PuPubVO.getString_TrimZeroLenAsNull(printCard.getPrintMessage()) != null) {
        showHintMessage(printCard.getPrintMessage());
//        MessageDialog.showHintDlg(this,
//            nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000270")/*
//                                                                                             * @res
//                                                                                             * "��ʾ"
//                                                                                             */, printCard
//                .getPrintMessage());
      }
      //�ָ���ǰ���εĲ���
      //PuTool.restoreUIRefType(maskedItems);
    }
    catch (BusinessException e) {
      SCMEnv.out(e);
    }
    getBillCardPanel().updateUI();
  }

  /**
   * ���ߣ���ά�� ���ܣ�����ӡ �������� ���أ��� ���⣺�� ���ڣ�(2004-12-15 11:39:21)
   */
  private void onBatchPrint() {
    if (printList == null) {
      printList = new ScmPrintTool(this, getBillCardPanel(), getSelectedBills(), getModuleCode());
    }
    else {
      try {
        printList.setData(getSelectedBills());
      }
      catch (Exception e1) {
        SCMEnv.out(e1);
        return;
      }
    }
    try {
      printList.onBatchPrint(getBillListPanel(), getBillCardPanel(), ScmConst.PO_Invoice);
      if (PuPubVO.getString_TrimZeroLenAsNull(printList.getPrintMessage()) != null) {
        showHintMessage(printList.getPrintMessage());
//        MessageDialog.showHintDlg(this,
//            nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPPSCMCommon-000270")/*
//                                                                                           * @res
//                                                                                           * "��ʾ"
//                                                                                           */, printList
//                .getPrintMessage());
      }
    }
    catch (BusinessException e) {
      SCMEnv.out(e);
    }
  }

  /**
   * ���ߣ���ά�� ���ܣ�����ӡ �������� ���أ��� ���⣺�� ���ڣ�(2004-12-15 11:39:21)
   */
  private void onBatchPrintPreview() {
    if (printList == null) {
      printList = new ScmPrintTool(this, getBillCardPanel(), getSelectedBills(), getModuleCode());
    }
    else {
      try {
        printList.setData(getSelectedBills());
      }
      catch (Exception e1) {
        SCMEnv.out(e1);
        return;
      }
    }
    try {
      printList.onBatchPrintPreview(getBillListPanel(), getBillCardPanel(), ScmConst.PO_Invoice);
      if (PuPubVO.getString_TrimZeroLenAsNull(printList.getPrintMessage()) != null) {
        showHintMessage(printCard.getPrintMessage());
//        MessageDialog.showHintDlg(this,
//            nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000270")/*
//                                                                                             * @res
//                                                                                             * "��ʾ"
//                                                                                             */, printList
//                .getPrintMessage());
      }
    }
    catch (BusinessException e) {
      SCMEnv.out(e);
    }
  }

  /*
   * ���� Javadoc��
   * 
   * @see nc.ui.scm.pub.print.ISetBillVO#setBillVO(nc.vo.pub.AggregatedValueObject)
   */
  public void setBillVO(AggregatedValueObject vo) {
    // TODO �Զ����ɷ������
    // ����ԭ����λ��
    m_nLstInvVOPos = getCurVOPos();

    if (getCurPanelMode() == INV_PANEL_LIST) {
      for (int i = 0; i < getInvVOs().length; i++) {
        if (getInvVOs()[i].getPrimaryKey().equalsIgnoreCase(((InvoiceVO) vo).getPrimaryKey())) {
          setCurVOPos(i);
        }

      }
    }
    // ���ÿ�Ƭ��������
    setVOToBillPanel();
  }

  /**
   * ��ȡ���ʾ������ù���
   */
  public POPubSetUI2 getPoPubSetUi2() {
    if (m_cardPoPubSetUI2 == null) {
      m_cardPoPubSetUI2 = new POPubSetUI2();
    }
    return m_cardPoPubSetUI2;
  }

  /*
   * ���� Javadoc��
   * 
   * @see nc.ui.scm.pub.bill.IBillExtendFun#getExtendBtns()
   */
  public ButtonObject[] getExtendBtns() {
    // TODO �Զ����ɷ������
//		if (extendBtns == null || extendBtns.length == 0) {
//			// ������÷�Ʊ��ť add by QuSida 2010-9-22 ����ɽ���ܣ�
//			extendBtns = new ButtonObject[] { getBoFeeInvoice()
//					};
//			return extendBtns;
//		}
//		return extendBtns;
	  return null;
  }
//  public ButtonObject getBoFeeInvoice(){
//	  if(feeInvoice == null){
//		  feeInvoice = new ButtonObject("���÷�Ʊ","���÷�Ʊ","���÷�Ʊ");
//		  return feeInvoice;
//	  }
//	  return feeInvoice;
//  }

  /*
   * ���� Javadoc��
   * 
   * @see nc.ui.scm.pub.bill.IBillExtendFun#onExtendBtnsClick(nc.ui.pub.ButtonObject)
   */
  public void onExtendBtnsClick(ButtonObject bo) {
    // TODO �Զ����ɷ������
//		 if(bo == getBoFeeInvoice()){
//		    	this.onBoFeeInvoice();
//		    }
  }

  /*
   * ���� Javadoc��
   * 
   * @see nc.ui.scm.pub.bill.IBillExtendFun#setExtendBtnsStat(int)
   */
  public void setExtendBtnsStat(int iState) {
//    if(iState == 0){
//    	 getBoFeeInvoice().setEnabled(true);
//    }
//    else if(iState == 1){
//    	 getBoFeeInvoice().setEnabled(true);
//    }
//   else if(iState == 2){
//	   getBoFeeInvoice().setEnabled(false);
//      }
// else if(iState == 3){
//	 getBoFeeInvoice().setEnabled(false);
// }
// else if(iState == 4){
//	 getBoFeeInvoice().setEnabled(false);
// }
// else if(iState == 4){
//	 getBoFeeInvoice().setEnabled(false);
// }

  }
//private void onBoFeeInvoice(){
////	getBoFeeInvoice().setTag("D1:"+m_strCurBizeType);
// 	getBoFeeInvoice().setTag("D1:");
//	PfUtilClient.childButtonClicked(getBoFeeInvoice(), nc.ui.pub.ClientEnvironment.getInstance().getCorporation().getPk_corp(),
//            getModuleCode(), nc.ui.pub.ClientEnvironment.getInstance().getUser().getPrimaryKey(),
//            "25", this);
////	PfUtilClient.childButtonClicked(getBoFeeInvoice(),this.getCurrentCorp(),this.getNodeCode(),this.getDjSettingParam().getPk_user(),
////			getDjDataBuffer().getCurrentDjlxbm(),this);]
//	if(PfUtilClient.isCloseOK()){
//	AggregatedValueObject[] vos = PfUtilClient.getRetVos();
//	ArrayList list = new ArrayList();
//	for (int i = 0; i < vos.length; i++) {
//		InvoiceVO vo = new InvoiceVO();
//		
//	}
//	}
//	
//}
private InvoiceVO voProcess(AggregatedValueObject avo){
	InvoiceVO vo = new InvoiceVO();
	
	
	return vo;
}
  public Object[] getRelaSortObjectArray() {
    // TODO �Զ����ɷ������
    return m_InvVOs;
  }

  public void doMaintainAction(ILinkMaintainData maintaindata) {
    // TODO �Զ����ɷ������
    initi();
    String billID = maintaindata.getBillID();

    setCauditid(billID);

    // û�з��������ķ�Ʊ
    InvoiceVO vo = null;
    try {
      vo = (InvoiceVO) getVo();
    }
    catch (Exception e) {
      SCMEnv.out(e);
      MessageDialog.showHintDlg(this,
          nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPPSCMCommon-000270")/*
                                                                                         * @res
                                                                                         * "��ʾ"
                                                                                         */,
          nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPPSCMCommon-000428")/*
                                                                                         * @res
                                                                                         * "ϵͳ���ϣ�"
                                                                                         */);
    }

    if (vo == null) {
      setInvVOs(null);
      MessageDialog.showHintDlg(this,
          nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPPSCMCommon-000270")/*
                                                                                         * @res
                                                                                         * "��ʾ"
                                                                                         */,
          nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPP40040401-000008")/*
                                                                                         * @res
                                                                                         * "û�з��ϲ�ѯ�����ķ�Ʊ��"
                                                                                         */);
      getBillCardPanel().addNew();
      return;
    }

    //
    UIComboBox comItem = (UIComboBox) getBillCardPanel().getHeadItem("iinvoicetype").getComponent();
    getBillCardPanel().getHeadItem("iinvoicetype").setWithIndex(true);
    comItem.addItem(nc.ui.ml.NCLangRes.getInstance().getStrByID(nc.vo.scm.pu.InvoiceType.m_sModuleNamePath,
        nc.vo.scm.pu.InvoiceType.INVOICE_TYPE_SPECIALID));
    comItem.addItem(nc.ui.ml.NCLangRes.getInstance().getStrByID(nc.vo.scm.pu.InvoiceType.m_sModuleNamePath,
        nc.vo.scm.pu.InvoiceType.INVOICE_TYPE_GENERALID));
    comItem.addItem(nc.ui.ml.NCLangRes.getInstance().getStrByID(nc.vo.scm.pu.InvoiceType.m_sModuleNamePath,
        nc.vo.scm.pu.InvoiceType.INVOICE_TYPE_DEFINEID));
    comItem.addItem(nc.ui.ml.NCLangRes.getInstance().getStrByID(nc.vo.scm.pu.InvoiceType.m_sModuleNamePath,
        nc.vo.scm.pu.InvoiceType.INVOICE_TYPE_VIRTUALID));
    comItem.setTranslate(true);
    //

    // ���÷�ƱVO����
    setInvVOs(new InvoiceVO[] {
      vo
    });
    setCurVOPos(0);

    // ���ÿ�Ƭ���б����ʾ״̬
    // setBillBrowseState(STATE_BROWSE_NORMAL) ;

    // ���ÿ�Ƭ��������
    // setVOToBillPanel() ;
    setVOToAuditedBillPanel();
    InvoiceItemVO bodyVO[] = vo.getBodyVO();
    for (int i = 0; i < bodyVO.length; i++) {
      if (bodyVO[i].getNaccumsettmny() != null && Math.abs(bodyVO[i].getNaccumsettmny().doubleValue()) > 0)
        return;
    }

    // setCurOperState(STATE_BROWSE_NORMAL);
    setButtonsStateBrowseNormal();
    // ����
    m_btnInvBillAudit.setEnabled(false);
    m_btnInvSelectAll.setEnabled(false);
    m_btnInvDeselectAll.setEnabled(false);
    updateButtons();

  }

  public void doQueryAction(ILinkQueryData querydata) {
    // TODO �Զ����ɷ������
    initi();
    String billID = querydata.getBillID();
//    String pk_corp = querydata.getPkOrg();

    // �Ȱ��յ���PK��ѯ���������Ĺ�˾corpvalue
    InvoiceVO vo = null;
    try {
      vo = InvoiceHelper.findByPrimaryKey(billID);
      if (vo == null) {
        MessageDialog.showHintDlg(this,
            NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000270")/* "��ʾ" */, NCLangRes.getInstance()
                .getStrByID("SCMCOMMON", "UPPSCMCommon-000397")/* "û�з�������������" */);
        return;
      }
      String strPkCorp = vo.getPk_corp();

      // �繫˾�������飬���а�ť������
      if (strPkCorp != null && strPkCorp.trim().length() > 0 && getCorpPrimaryKey() != null
          && getCorpPrimaryKey().trim().length() > 0) {
        if (!strPkCorp.equals(getCorpPrimaryKey())) {// ������繫˾��ѯ����ʾ�κΰ�ť
          ButtonObject[] arrButtonObject = m_btnTree.getButtonArray();
          for (int i = 0; i < arrButtonObject.length; i++) {
            arrButtonObject[i].setVisible(false);
            updateButton(arrButtonObject[i]);
          }
        }
      }

      // v5.1����Ȩ�ޣ����ز�ѯģ��(ע��ȥ��ģ��Ĭ��ֵ)����ѯģ����û�й�˾ʱ��Ҫ�������⹫˾
      SCMQueryConditionDlg queryDlg = new SCMQueryConditionDlg(this);
      if (queryDlg.getAllTempletDatas() == null || queryDlg.getAllTempletDatas().length <= 0)
        queryDlg.setTempletID(strPkCorp, getModuleCode(), nc.ui.pub.ClientEnvironment.getInstance()
            .getUser().getPrimaryKey(), null);

      ArrayList<String> alcorp = new ArrayList<String>();
      alcorp.add(ClientEnvironment.getInstance().getCorporation().getPrimaryKey());
      queryDlg.initCorpRef(IDataPowerForInv.CORPKEY, ClientEnvironment.getInstance().getCorporation().getPrimaryKey(),
          alcorp);
      queryDlg.setCorpRefs(IDataPowerForInv.CORPKEY, IDataPowerForInv.REFKEYS);
      //���ù���������ȡ�ù�˾�п���Ȩ�޵ĵ�������VO����
      ConditionVO[] voaCond = queryDlg.getDataPowerConVOs(strPkCorp,IDataPowerForInv.REFKEYS);

      InvoiceVO[] VOs = null;
      VOs = InvoiceHelper.queryInvoiceVOsByCondsMy(getClientEnvironment().getUser().getPrimaryKey(), new NormalCondVO[] {new  NormalCondVO( "��˾",strPkCorp),new  NormalCondVO( "����ID",billID)}, voaCond);//����NormalCondVO���������յ���������˾����
      if (VOs == null || VOs.length <= 0 || VOs[0] == null) {
        MessageDialog.showHintDlg(this,
            NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000270")/* "��ʾ" */, NCLangRes.getInstance()
                .getStrByID("common", "SCMCOMMON000000161")/* "û�в쿴���ݵ�Ȩ��" */);
        return;
      }
      billID = ((InvoiceVO) VOs[0]).getPrimaryKey();// ֻ��һ������
    }
    catch (Exception e) {
      // ��־�쳣
      nc.vo.scm.pub.SCMEnv.out(e);
      MessageDialog.showHintDlg(this,
          nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPPSCMCommon-000270")/*
                                                                                         * @res
                                                                                         * "��ʾ"
                                                                                         */,
          nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPPSCMCommon-000428")/*
                                                                                         * @res
                                                                                         * "ϵͳ���ϣ�"
                                                                                         */);
    }

    setCauditid(billID);

    //
    // UIComboBox comItem = (UIComboBox)
    // getInvBillPanel().getHeadItem("iinvoicetype").getComponent();
    // getInvBillPanel().getHeadItem("iinvoicetype").setWithIndex(true);
    // comItem.addItem(nc.ui.ml.NCLangRes.getInstance().getStrByID(nc.vo.scm.pu.InvoiceType.m_sModuleNamePath,nc.vo.scm.pu.InvoiceType.INVOICE_TYPE_SPECIALID
    // ));
    // comItem.addItem(nc.ui.ml.NCLangRes.getInstance().getStrByID(nc.vo.scm.pu.InvoiceType.m_sModuleNamePath,nc.vo.scm.pu.InvoiceType.INVOICE_TYPE_GENERALID
    // ));
    // comItem.addItem(nc.ui.ml.NCLangRes.getInstance().getStrByID(nc.vo.scm.pu.InvoiceType.m_sModuleNamePath,nc.vo.scm.pu.InvoiceType.INVOICE_TYPE_DEFINEID
    // ));
    // comItem.addItem(nc.ui.ml.NCLangRes.getInstance().getStrByID(nc.vo.scm.pu.InvoiceType.m_sModuleNamePath,nc.vo.scm.pu.InvoiceType.INVOICE_TYPE_VIRTUALID
    // ));
    // comItem.setTranslate(true);
    //

    // ���÷�ƱVO����
    setInvVOs(new InvoiceVO[] {
      vo
    });
    setCurVOPos(0);

    // ���ÿ�Ƭ���б����ʾ״̬
    // setBillBrowseState(STATE_BROWSE_NORMAL) ;

    // ���ÿ�Ƭ��������
    // setVOToBillPanel() ;
    setVOToAuditedBillPanel();
    
    // ���ñ�ͷ���¼�����ʾ��ȡ��һ�е�ֵ
    InvoiceVO curVO = getInvVOs()[getCurVOPos()];
    InvoiceItemVO voFirstItem = curVO.getBodyVO()[0];

    if (voFirstItem.getIdiscounttaxtype() != null && voFirstItem.getIdiscounttaxtype().toString().trim().length() > 0) {
      getBillCardPanel().getHeadItem("idiscounttaxtype").setValue(voFirstItem.getIdiscounttaxtype());
    }
    else {
      getBillCardPanel().getHeadItem("idiscounttaxtype").setValue(new Integer(1));
    }
    getBillCardPanel().getHeadItem("ntaxrate").setValue(voFirstItem.getNtaxrate());

    getBillCardPanel().setEnabled(false);
    setButtonsStateBrowseNormal();
    updateButtons();
  }

  public void doAddAction(ILinkAddData adddata) {
    // TODO �Զ����ɷ������
    return;
  }

  public void doApproveAction(ILinkApproveData approvedata) {
    // TODO �Զ����ɷ������
    if (approvedata == null)
      return;

    String billID = approvedata.getBillID();
    String pk_corp = approvedata.getPkOrg();

    initi();

    setCauditid(billID);

    // û�з��������ķ�Ʊ
    InvoiceVO vo = null;
    try {
      vo = (InvoiceVO) getVo();
      //
      vo.setSource(InvoiceVO.FROM_QUERY);
      //
      setInvVOs(new InvoiceVO[] {vo});
    }
    catch (Exception e) {
      SCMEnv.out(e);
      MessageDialog.showHintDlg(this,
          nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPPSCMCommon-000270")/*
                                                                                         * @res
                                                                                         * "��ʾ"
                                                                                         */,
          nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPPSCMCommon-000428")/*
                                                                                         * @res
                                                                                         * "ϵͳ���ϣ�"
                                                                                         */);
    }
    // //���ð�ť��
    // if(m_btnAuditFlowAssist.getChildCount() == 0){
    // m_btnAuditFlowAssist.addChildButton(m_btnQueryForAudit);
    // m_btnAuditFlowAssist.addChildButton(m_btnDocManage);
    // }

    if (vo == null) {
      setInvVOs(null);
      MessageDialog.showHintDlg(this,
          nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPPSCMCommon-000270")/*
                                                                                         * @res
                                                                                         * "��ʾ"
                                                                                         */,
          nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPP40040401-000008")/*
                                                                                         * @res
                                                                                         * "û�з��ϲ�ѯ�����ķ�Ʊ��"
                                                                                         */);
      getBillCardPanel().addNew();
      // ���ð�ť״̬
      /* ����ť������ */
      for (int i = 0; i < aryForAudit.length; i++) {
        aryForAudit[i].setEnabled(false);
        if (aryForAudit[i].getChildCount() > 0) {
          for (int j = 0; j < aryForAudit[i].getChildCount(); j++)
            aryForAudit[i].getChildButtonGroup()[j].setEnabled(false);
        }
      }
      for (int i = 0; i < aryForAudit.length; i++) {
        updateButton(aryForAudit[i]);
      }
      return;
    }

    // ���ð�ť״̬
    /* ����ť���� */
    for (int i = 0; i < aryForAudit.length; i++) {
      if (aryForAudit[i] != null) {
        aryForAudit[i].setEnabled(true);
        if (aryForAudit[i].getChildCount() > 0) {
          for (int j = 0; j < aryForAudit[i].getChildCount(); j++)
            aryForAudit[i].getChildButtonGroup()[j].setEnabled(true);
        }
      }
    }
    for (int i = 0; i < aryForAudit.length; i++) {
      updateButton(aryForAudit[i]);
    }

    // ���÷�ƱVO����
    setInvVOs(new InvoiceVO[] {
      vo
    });
    setCurVOPos(0);

    // ���ÿ�Ƭ���б����ʾ״̬
    setCurOperState(STATE_BROWSE_NORMAL);
    // ���ÿ�Ƭ��������
    setVOToAuditedBillPanel();
    // �����б�������ݣ����б���沢����ʾ
    setVOsToListPanel();
    getBillCardPanel().setEnabled(false);

    // V5.1 ��������ť�߼��仯
    if (pk_corp != null && pk_corp.trim().length() > 0 && getCorpPrimaryKey() != null
        && getCorpPrimaryKey().trim().length() > 0) {
      if (pk_corp.equals(getCorpPrimaryKey())) {
        setButtons(m_btnTree.getButtonArray());// ����˾ ���ص��ݿ�Ƭ�����ť
       // setButtons(getBtnss());
        setButtonsStateInit();
        setButtonsStateBrowseNormal();
        updateButtons();
      }
      else {
        setButtons(aryForAudit);// �繫˾ ����ԭ�а�ť������
      }
    }
    // Ϊ��ʹ�ĵ�����ť��Ч����Ҫ���б�����һ������Ϊ��ѡ��
    getBillListPanel().getHeadBillModel().setRowState(0, BillModel.SELECTED);
  }

  /**
   * ���ߣ���ά�� ���ܣ�(���ο�Ʊ����+�ۼƿ�Ʊ����)*��������ⵥ������>0 ������ ���أ� ���⣺ �������ڣ�(2004-4-15 11:23:07)
   */
  private boolean negativeAndPlusCtrl(InvoiceVO billVO) throws Exception {
    InvoiceItemVO[] newItems = (InvoiceItemVO[]) billVO.getChildrenVO();

    InvoiceVO oldVO = getInvVOs()[getCurVOPos()];
    InvoiceItemVO[] oldItems = (InvoiceItemVO[]) oldVO.getChildrenVO();

    Vector vTemp = new Vector();
    String sUpSourceRowID = null;
    String sUpSourceType = null;
    for (int i = 0; i < newItems.length; i++) {
      sUpSourceRowID = newItems[i].getCupsourcebillrowid();
      if (sUpSourceRowID != null)
        vTemp.addElement(sUpSourceRowID);
      if (newItems[i].getCupsourcebilltype() != null)
        sUpSourceType = newItems[i].getCupsourcebilltype();
    }

    if (vTemp.size() == 0 || sUpSourceType == null)
      return true;

    String sUpSourceRowIDs[] = new String[vTemp.size()];
    vTemp.copyInto(sUpSourceRowIDs);
    if (!sUpSourceType.equals(ScmConst.PO_Order) && !sUpSourceType.equals(ScmConst.SC_Order)
        && !sUpSourceType.equals("45") && !sUpSourceType.equals("47"))
      return true;

    Object oTemp[][] = null;
    try {
      String beanName = IInvoiceD.class.getName();
      IInvoiceD bo = (IInvoiceD) nc.bs.framework.common.NCLocator.getInstance().lookup(beanName);
      Object o = bo.queryRelatedData(sUpSourceType, sUpSourceRowIDs);
      if (o != null) {
        oTemp = (Object[][]) o;
      }
    }
    catch (Exception e) {
      SCMEnv.out(e);
      throw e;
    }

    if (oTemp == null)
      return true;
    for (int i = 0; i < newItems.length; i++) {
      sUpSourceRowID = newItems[i].getCupsourcebillrowid();
      if (sUpSourceRowID == null || newItems[i].getStatus() == VOStatus.DELETED)
        continue;
      UFDouble dInvoiceNum = newItems[i].getNinvoicenum();
      if (dInvoiceNum == null)
        continue;
      UFDouble dOldInvoiceNum = new UFDouble(0);
      for (int j = 0; j < oldItems.length; j++) {
        if (sUpSourceRowID.equals(oldItems[j].getCupsourcebillrowid()) && oldItems[j].getCinvoice_bid() != null) {
          dOldInvoiceNum = oldItems[j].getNinvoicenum();
          break;
        }
      }
      for (int j = 0; j < sUpSourceRowIDs.length; j++) {
        if (sUpSourceRowID.equals(sUpSourceRowIDs[j])) {
          Object o1 = oTemp[j][0];
          Object o2 = oTemp[j][1];
          if (o1 != null && o2 != null) {
            UFDouble dAccumInvoiceNum = new UFDouble(o1.toString());
            UFDouble nNum = new UFDouble(o2.toString());
            if ((dInvoiceNum.doubleValue() - dOldInvoiceNum.doubleValue() + dAccumInvoiceNum.doubleValue())
                * nNum.doubleValue() < 0)
              return false;
          }
        }
      }
    }

    return true;
  }

  /**
   * ������������������ҵ��ԱĬ��ֵ ���ݲ���Ա����ҵ��Ա��
   * <p>
   * <b>examples:</b>
   * <p>
   * ʹ��ʾ��
   * <p>
   * <b>����˵��</b>
   * <p>
   * 
   * @author lixiaodong
   * @time 2007-3-29 ����10:42:05
   */
  private void setDefaultValueByUser() {
    // ����ҵ��ԱĬ��ֵ ���ݲ���Ա����ҵ��Ա
    UIRefPane cemployeeid = (UIRefPane) getBillCardPanel().getHeadItem("cemployeeid").getComponent();
    if (cemployeeid != null && cemployeeid.getRefPK() == null) {
      PsndocVO voPsnDoc = PuTool.getPsnByUser(cemployeeid.getRefPK(), getPk_corp());
//      IUserManageQuery qrySrv = (IUserManageQuery) NCLocator.getInstance().lookup(IUserManageQuery.class.getName());
//      PsndocVO voPsnDoc = null;
//      try {
//        voPsnDoc = qrySrv.getPsndocByUserid(nc.ui.pub.ClientEnvironment.getInstance().getCorporation().getPrimaryKey(),
//            getCurOperator());
//      }
//      catch (BusinessException be) {
//        SCMEnv.out(be);
//      }
      if (voPsnDoc != null) {
        cemployeeid.setPK(voPsnDoc.getPk_psndoc());

        UIRefPane cdeptid = (UIRefPane) getBillCardPanel().getHeadItem("cdeptid").getComponent();// ��������
        cdeptid.setPK(voPsnDoc.getPk_deptdoc());
      }
    }
  }

  /**
   * ����ť�߼�����
   * 
   * @author czp
   * @since v50
   * @date 2006-09-23
   * @ע�⣺��������Լ������������ʱ���ô˷�����Ҫ����״ֵ̬Ϊ -1
   */
  private boolean isNeedSendAudit(int iBillStatus) {

    // ����δͨ��
    boolean isNeedSendToAuditQ = (iBillStatus == BillStatus.AUDITFAIL);

    // ����
    if (iBillStatus == -1) {
      if(getSelectedRowCount()>1) return m_btnSendAudit.isEnabled();
      if (m_bizButton != null && m_bizButton.getTag() != null) {
        isNeedSendToAuditQ = BusiBillManageTool.isNeedSendToAudit("25", getClientEnvironment().getCorporation()
            .getPrimaryKey(), m_bizButton.getTag(), null, getClientEnvironment().getUser().getPrimaryKey());
      }
    }
    // ����(�޸����)
    else if (iBillStatus == BillStatus.FREE) {
      String billid = getInvVOs()[getCurVOPos()].getHeadVO().getCinvoiceid();
      String cbizType = getInvVOs()[getCurVOPos()].getHeadVO().getCbiztype();
      //Ч���Ż� by zhaoyha at 2009.8
      UFBoolean needSend=(UFBoolean)  getInvVOs()[getCurVOPos()].getHeadVO().getAttributeValue(ConstantVO.EXT_ATTR_ISNEEDSENDAUDIT);
      if(null==needSend && PuPubVO.getString_TrimZeroLenAsNull(cbizType) != null) {
        isNeedSendToAuditQ = BusiBillManageTool.isNeedSendToAudit("25", getClientEnvironment().getCorporation()
            .getPrimaryKey(), cbizType, billid, getClientEnvironment().getUser().getPrimaryKey());
      }else if(null!=needSend)
        isNeedSendToAuditQ=needSend.booleanValue();
    }
    //�����ǰ����Ա�뵱ǰλ�õ����Ƶ��˲�һ�£����������� by zhaoyha at 2009.8
    if(getCurVOPos()>=0 && null!=getInvVOs() &&
        !getCurOperator().equals(getInvVOs()[getCurVOPos()].getHeadVO().getCoperator()))
      isNeedSendToAuditQ=false;
    
    m_btnSendAudit.setEnabled(isNeedSendToAuditQ);
    updateButton(m_btnSendAudit);
    //
    return isNeedSendToAuditQ;
  }

  /**
   * ������ERP��Ŀ:���ɷ��ռ���ģʽ�£��ɹ���Ʊ��ͷ�����֯Ӧ���ǲɹ���֯��Ӧ���ڲ���������֯������ֿ�ӦΪֱ�˲֡�
   * <p>
   * <b>examples:</b>
   * <p>
   * ʹ��ʾ��
   * <p>
   * <b>����˵��</b>
   * 
   * @param vos
   *          InvoiceVO
   *          <p>
   * @author lixiaodong
   * @time 2007-8-27 ����11:00:41
   */
  private void resetStoreorgAndStordoc(InvoiceVO vos[], String strUpBillType) {
    int intLen = vos.length;

    // ���˵��Ƿ��ռ���ҵ������VO
    Vector<InvoiceVO> vctIsFSJJPurVO = new Vector<InvoiceVO>();
    InvoiceVO[] aryIsFSJJPurVO = null;
    for (int j = 0; j < intLen; j++) {
      String strPurCorp = vos[j].getHeadVO().getPk_purcorp();// �ɹ���˾
      String strArrCorp = vos[j].getHeadVO().getPk_arrvcorp();// ������˾
      String strInvoiceCorp = vos[j].getHeadVO().getPk_corp();// ��Ʊ��˾
      boolean isFSJJPur = OrderPubVO.isFSJJPur(strPurCorp, strArrCorp, strInvoiceCorp);
      if (isFSJJPur) {
        vctIsFSJJPurVO.add(vos[j]);
      }
    }

    if (vctIsFSJJPurVO == null || vctIsFSJJPurVO.size() < 1) {
      return;
    }
    else {
      aryIsFSJJPurVO = new InvoiceVO[vctIsFSJJPurVO.size()];
      vctIsFSJJPurVO.copyInto(aryIsFSJJPurVO);
    }

    // �ɹ���Ʊ��ͷ�����֯�滻Ϊ�ɹ���֯��Ӧ���ڲ���������֯
    Vector<String> vctStoreorganization = new Vector<String>();
    String sCstoreorganization = null;
    int IsFSJJPurVOLen = aryIsFSJJPurVO.length;

    if (BillTypeConst.STORE_PO.equalsIgnoreCase(strUpBillType)) {// ��Դ�ڿ����ⵥ����ձ�ͷ�����֯�ͱ���ֿ�
      for (int j = 0; j < IsFSJJPurVOLen; j++) {
        aryIsFSJJPurVO[j].getHeadVO().setCstoreorganization(null);
        InvoiceItemVO[] items = aryIsFSJJPurVO[j].getBodyVO();
        if (items != null) {
          int itemSize = items.length;
          for (int jj = 0; jj < itemSize; jj++) {
            items[jj].setCwarehouseid(null);
          }
        }
      }
    }
    else if (BillTypeConst.PO_ORDER.equalsIgnoreCase(strUpBillType)) {// ��Դ��ɹ������ı�ͷ�����֯ȡ�ɹ���֯��Ӧ���ڲ���������֯������ֿ�ȡ�����֯��Ӧ��ֱ�˲�

      for (int j = 0; j < IsFSJJPurVOLen; j++) {
        // V502
        // ���ɷ��ռ���ģʽ�£��ɹ���Ʊ��ͷ�����֯Ӧ���ǲɹ���֯��Ӧ���ڲ���������֯:�Ӳɹ���֯��bd_purorg�в��Ҷ�Ӧ���ڲ���������֯settlestockorg����ѯ������vos.getHeadVO().getAttributeValue("cpurorganization");����ŵ�cstoreorganization��Ʊ��ͷ��
        ClientCacheHelper.getColValue(new InvoiceHeaderVO[] {
          aryIsFSJJPurVO[j].getHeadVO()
        }, new String[] {
          "cstoreorganization"
        }, "bd_purorg", "pk_purorg", new String[] {
          "settlestockorg"
        }, "cpurorganization");
        sCstoreorganization = aryIsFSJJPurVO[j].getHeadVO().getCstoreorganization();
        if (PuPubVO.getString_TrimZeroLenAsNull(sCstoreorganization) != null) {
          vctStoreorganization.add(sCstoreorganization);// �����֯ID
        }
      }
      String[] saCstoreorganization = new String[vctStoreorganization.size()];
      vctStoreorganization.copyInto(saCstoreorganization);
      Hashtable t = null;
      try {
        t = InvoiceHelper.queryStordocByStoreorg(saCstoreorganization);// ���ݿ����֯ID�������Ҷ�Ӧ��ֱ�˲�ID
      }
      catch (java.lang.Exception e) {
        SCMEnv.out(e);
        MessageDialog.showHintDlg(this,
            nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPPSCMCommon-000270")/*
                                                                                           * @res
                                                                                           * "��ʾ"
                                                                                           */,
            nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPP40040401-000014")/*
                                                                                           * @res
                                                                                           * "ϵͳ����!"
                                                                                           */);
        return;
      }

      // ��ֱ�˲�ID������ֿ�
      if (t != null && t.size() > 0) {
        for (int jj = 0; jj < IsFSJJPurVOLen; jj++) {
          InvoiceItemVO[] items = aryIsFSJJPurVO[jj].getBodyVO();
          if (items != null) {
            int itemSize = items.length;
            String Cstoreorganization = aryIsFSJJPurVO[jj].getHeadVO().getCstoreorganization();// �����֯
            for (int jjj = 0; jjj < itemSize; jjj++) {
              Object oTemp = t.get(Cstoreorganization);// �ɹ���Ʊ����ֿ�ӦΪֱ�˲�
              items[jjj].setCwarehouseid(oTemp == null ? null : oTemp.toString());
            }
          }
        }
      }
    }
  }

  /**
   * ���ø���������
   * 
   * @param bcp
   * @param row
   *          ɭ��NC
   */
  private void setRefPaneAssistunit(BillCardPanel bcp, int row) {
    Object cbaseid = bcp.getBillModel().getValueAt(row, "cbaseid");
    bcp.getBillModel().getValueAt(row, "cinventoryunit");

    UIRefPane ref = (UIRefPane) bcp.getBodyItem("cassistunitname").getComponent();
    String wherePart = "bd_convert.pk_invbasdoc='" + cbaseid + "' ";
    ref.setWhereString(wherePart);
    StringBuffer unionPart = new StringBuffer();
    unionPart.append(" union all \n");
    unionPart.append("(select bd_measdoc.shortname,bd_measdoc.measname,bd_invbasdoc.pk_measdoc \n");
    unionPart.append("from bd_invbasdoc \n");
    unionPart.append("left join bd_measdoc  \n");
    unionPart.append("on bd_invbasdoc.pk_measdoc=bd_measdoc.pk_measdoc \n");
    unionPart.append("where bd_invbasdoc.pk_invbasdoc='" + cbaseid + "') \n");
    ref.getRefModel().setGroupPart(unionPart.toString());
  }

  /**
   * ����:�������������ʡ��������ɱ༭�ԡ��������������ݼ��ɱ༭��
   * 
   * @since V50
   */
  public void beforeEditBodyAssistUnitNumber(BillCardPanel bcp, int iRow) {
    try {
      String strCbaseid;
      String cassistunit;
      Object oTmp;
      String ass;
      String main;
      UFDouble ufdConv;
      // �Ƿ���и���������
      strCbaseid = (String) bcp.getBillModel().getValueAt(iRow, "cbaseid");
      // �д��
      if (strCbaseid != null && !strCbaseid.trim().equals("")) {
        if (PuTool.isAssUnitManaged(strCbaseid)) {
          // ���ø���������
          setRefPaneAssistunit(bcp, iRow);
          cassistunit = (String) bcp.getBillModel().getValueAt(iRow, "cassistunit");
          // ��������Ϊ��
          oTmp = bcp.getBillModel().getValueAt(iRow, "nexchangerate");
          if (oTmp == null || oTmp.toString().trim().equals("")) {
            // ���û�����(���ԭ�����ڻ����������ã���Ϊ�������Ѿ��ı��˵ķǹ̶�������)
            ufdConv = PuTool.getInvConvRateValue(strCbaseid, cassistunit);
            bcp.getBillModel().setValueAt(ufdConv, iRow, "nexchangerate");
          }
          // ���ÿɱ༭��
          bcp.setCellEditable(iRow, "cassistunitname", true);
          bcp.setCellEditable(iRow, "ninvoicenum", true);
          bcp.setCellEditable(iRow, "nassistnum", true);
          bcp.setCellEditable(iRow, "nmoney", true);
          bcp.setCellEditable(iRow, "nexchangerate", true);
          // ����������ǹ̶�������
          if (PuTool.isFixedConvertRate(strCbaseid, cassistunit)) {
            bcp.setCellEditable(iRow, "nexchangerate", false);
          }
          else {
            bcp.setCellEditable(iRow, "nexchangerate", true);
          }
          // ���������������ͬ,�����ʲ��ɱ༭
          ass = (String) bcp.getBillModel().getValueAt(iRow, "cassistunitname");
          main = (String) bcp.getBillModel().getValueAt(iRow, "cinventoryunit");
          if (ass != null && ass.equals(main)) {
            bcp.getBillModel().setValueAt(new UFDouble(1), iRow, "nexchangerate");
            bcp.setCellEditable(iRow, "nexchangerate", false);
          }
        }
        else {
          bcp.setCellEditable(iRow, "ninvoicenum", true);
          bcp.setCellEditable(iRow, "nmoney", true);
          bcp.setCellEditable(iRow, "nexchangerate", false);
          bcp.setCellEditable(iRow, "nassistnum", false);
          bcp.setCellEditable(iRow, "cassistunitname", false);
        }
      }
      // �޴��
      else {
        //bcp.setCellEditable(iRow, "ninvoicenum", false);
        bcp.setCellEditable(iRow, "nmoney", false);
        bcp.setCellEditable(iRow, "nexchangerate", false);
        bcp.setCellEditable(iRow, "nassistnum", false);
        bcp.setCellEditable(iRow, "cassistunitname", false);
      }
      //��Դ�ڶ����ķ����������ֶβ������޸� For V56 by zhaoyha
     setNumEditable();
    }
    catch (Exception e) {
      Logger.debug("¼�����ʱ���ó���");
    }
  }

  /**
   * �༭���¼�--������
   * 
   * @param bcp
   * @param e
   *          ɭ��NC
   */
  public void afterEditWhenBodyAssist(ToftPanel uiPanel, BillCardPanel bcp, BillEditEvent e) {
    int iRow = e.getRow();

    // �����������ID
    String sBaseID = (String) bcp.getBillModel().getValueAt(iRow, "cbaseid");
    // ����������
    String sCassId = (String) bcp.getBillModel().getValueAt(iRow, "cassistunit");
    //
    if (e.getValue() == null || e.getValue().toString().trim().length() == 0) {
      bcp.getBillModel().setValueAt(null, iRow, "nassistnum");
      bcp.getBillModel().setValueAt(null, iRow, "nexchangerate");
      bcp.getBillModel().setValueAt(null, iRow, "cassistunitname");
      return;
    }
    // ��ȡ������
    UFDouble nExchangeRate = PuTool.getInvConvRateValue(sBaseID, sCassId);
    // //////////////////
    UIRefPane ref = (UIRefPane) bcp.getBodyItem("cassistunitname").getComponent();
    String pk_measdoc = ref.getRefPK();
    String name = ref.getRefName();
    bcp.getBillModel().setValueAt(pk_measdoc, iRow, "cassistunit");
    bcp.getBillModel().setValueAt(name, iRow, "cassistunitname");
    sCassId = (String) bcp.getBillModel().getValueAt(iRow, "cassistunit");
    nExchangeRate = PuTool.getInvConvRateValue(sBaseID, sCassId);
    bcp.getBillModel().setValueAt(nExchangeRate, iRow, "nexchangerate");

    // �����ʸı䣬���¼���
    BillEditEvent tempE = new BillEditEvent(bcp.getBodyItem("nexchangerate"),
        bcp.getBodyValueAt(iRow, "nexchangerate"), "nexchangerate", iRow);
    afterEditWhenBodyRate(uiPanel, bcp, tempE);

    // �˴��ſ����ڿɱ༭���ڱ༭ǰ���������
    bcp.setCellEditable(iRow, "ninvoicenum", true);
    bcp.setCellEditable(iRow, "noriginalcurmny", true);
    bcp.setCellEditable(iRow, "nexchangerate", true);
    bcp.setCellEditable(iRow, "nassistnum", true);
    bcp.setCellEditable(iRow, "cassistunitname", true);
  }

  /**
   * �༭���¼�--������
   * 
   * @param ui
   * @param bcp
   * @param e
   */
  public void afterEditWhenBodyRate(ToftPanel ui, BillCardPanel bcp, BillEditEvent e) {
    UFDouble ninvoicenum = PuPubVO.getUFDouble_NullAsZero(bcp.getBodyValueAt(e.getRow(), "ninvoicenum"));
    String sBaseID = (String) bcp.getBillModel().getValueAt(e.getRow(), "cbaseid");
    String sCassId = (String) bcp.getBillModel().getValueAt(e.getRow(), "cassistunit");
    UFDouble nExchangeRate = null;// nc.ui.pu.pub.PuTool.getInvConvRateValue(sBaseID,
                                  // sCassId);
    // ����
    UFDouble nOriginalCurPrice = PuPubVO.getUFDouble_NullAsZero(bcp.getBodyValueAt(e.getRow(), "noriginalcurprice"));
    // �޸ļ���������,�빺�����仯
    Object oTemp = bcp.getBodyValueAt(e.getRow(), "nexchangerate");
    if (oTemp != null && oTemp.toString().length() > 0)
      nExchangeRate = (UFDouble) oTemp;
    else
      nExchangeRate = null;
    if (nExchangeRate != null) {
      if (nExchangeRate.doubleValue() < 0) {
        MessageDialog.showErrorDlg(ui, nc.ui.ml.NCLangRes.getInstance().getStrByID("40040101", "UPP40040101-000040")
        /*
         * @res "��������"
         */, nc.ui.ml.NCLangRes.getInstance().getStrByID("40040101", "UPP40040101-000045")
        /*
         * @res "���������ʲ���Ϊ����"
         */);
        return;
      }
      UFDouble nAssistNum = PuPubVO.getUFDouble_NullAsZero(bcp.getBodyValueAt(e.getRow(), "nassistnum"));
      if (ninvoicenum.doubleValue() != 0) {
        bcp.setBodyValueAt(ninvoicenum.div(nExchangeRate), e.getRow(), "nassistnum");
      }
      else {
        bcp.setBodyValueAt(nAssistNum.multiply(nExchangeRate), e.getRow(), "ninvoicenum");
        afterEditWhenBodyRelationsCal(new BillEditEvent(bcp.getBodyItem("ninvoicenum"), bcp.getBodyValueAt(e.getRow(),
            "ninvoicenum"), "ninvoicenum", e.getRow(), BillItem.BODY));
      }
      // ��Ʊ�����仯,����Զ��仯
      // ninvoicenum = PuPubVO.getUFDouble_NullAsZero(bcp.getBodyValueAt(e
      // .getRow(), "ninvoicenum"));
      // if (nOriginalCurPrice != null) {
      // final double d = ninvoicenum.doubleValue()
      // * nOriginalCurPrice.doubleValue();
      // bcp.setBodyValueAt(new UFDouble(d), e.getRow(), "noriginalcurmny");
      // }
    }
    else {
      bcp.setBodyValueAt(null, e.getRow(), "noriginalcurmny");

    }
  }

  /**
   * �༭���¼�--������
   * 
   * @param ui
   * @param bcp
   * @param e
   *          ɭ��NC
   */
  public void afterEditWhenBodyAssNum(ToftPanel ui, BillCardPanel bcp, BillEditEvent e) {
    UFDouble ninvoicenum = PuPubVO.getUFDouble_NullAsZero(bcp.getBodyValueAt(e.getRow(), "ninvoicenum"));
    UFDouble nOriginalCurPrice = PuPubVO.getUFDouble_NullAsZero(bcp.getBodyValueAt(e.getRow(), "noriginalcurprice"));
    String sBaseID = (String) bcp.getBillModel().getValueAt(e.getRow(), "cbaseid");
    String sCassId = (String) bcp.getBillModel().getValueAt(e.getRow(), "cassistunit");
    UFDouble nExchangeRate = nc.ui.pu.pub.PuTool.getInvConvRateValue(sBaseID, sCassId);
    // �޸ĸ�����,��Ʊ�����仯
    Object oTemp = bcp.getBodyValueAt(e.getRow(), "nassistnum");
    UFDouble nAssistNum = null;
    if (oTemp != null && oTemp.toString().length() > 0)
      nAssistNum = (UFDouble) oTemp;
    if (nAssistNum != null) {
      if (nAssistNum.doubleValue() < 0) {
        MessageDialog.showErrorDlg(ui, nc.ui.ml.NCLangRes.getInstance().getStrByID("40040101", "UPP40040101-000040")
        /*
         * @res "��������"
         */, nc.ui.ml.NCLangRes.getInstance().getStrByID("40040101", "UPP40040101-000044")/*
                                                                                           * @res
                                                                                           * "����������Ϊ����"
                                                                                           */);
        return;
      }
      // �����Ƿǹ̶������ʣ����Բ����� m_nExchangeRate, Ҫ��ģ����ȡ
      Object exc = bcp.getBillModel().getValueAt(e.getRow(), "nexchangerate");
      if (exc != null && !exc.toString().trim().equals(""))
        nExchangeRate = new UFDouble(exc.toString().trim());
      if (nExchangeRate != null) {
        final double d = nAssistNum.doubleValue() * nExchangeRate.doubleValue();
        bcp.setBodyValueAt(new UFDouble(d), e.getRow(), "ninvoicenum");
        afterEditWhenBodyRelationsCal(new BillEditEvent(bcp.getBodyItem("ninvoicenum"), bcp.getBodyValueAt(e.getRow(),
            "ninvoicenum"), "ninvoicenum", e.getRow(), BillItem.BODY));

      }
      // ��Ʊ�����仯,����Զ��仯
      // ninvoicenum = (UFDouble) bcp.getBodyValueAt(e.getRow(), "ninvoicenum");
      // if (nOriginalCurPrice != null && ninvoicenum != null) {
      // final double d = ninvoicenum.doubleValue()
      // * nOriginalCurPrice.doubleValue();
      // bcp.setBodyValueAt(new UFDouble(d), e.getRow(), "noriginalcurmny");
      // }
    }
    else {
      bcp.setBodyValueAt(null, e.getRow(), "noriginalcurmny");// ���
      bcp.setBodyValueAt(null, e.getRow(), "ninvoicenum");
      bcp.setBodyValueAt(null, e.getRow(), "noriginaltaxmny");// ˰��
      bcp.setBodyValueAt(null, e.getRow(), "noriginalsummny");// ��˰�ϼ�
    }
  }

  private void aftereditWhenBodyInvoicenum(ToftPanel ui, BillCardPanel bcp, BillEditEvent e) {
    // ��Ʊ����
    UFDouble ninvoicenum = PuPubVO.getUFDouble_NullAsZero(bcp.getBodyValueAt(e.getRow(), "ninvoicenum"));
    // �����������ID
    String sBaseID = (String) bcp.getBillModel().getValueAt(e.getRow(), "cbaseid");
    // ����������
    String sCassId = (String) bcp.getBillModel().getValueAt(e.getRow(), "cassistunit");
    // ��ȡ������
    UFDouble nExchangeRate = PuTool.getInvConvRateValue(sBaseID, sCassId);
    // �Ƿ�̶�������
    boolean bFixedFlag = PuTool.isFixedConvertRate(sBaseID, sCassId);
    // �̶�������,�������淢Ʊ�����仯;����,�������淢Ʊ�����仯
    if (bFixedFlag) {
      if (nExchangeRate != null && nExchangeRate.doubleValue() != 0.0) {
        bcp.setBodyValueAt(ninvoicenum.div(nExchangeRate), e.getRow(), "nassistnum");
      }
    }
    else {
      // �ǹ̶�������
      UFDouble nAssistNum = null;
      Object oTemp = bcp.getBodyValueAt(e.getRow(), "nassistnum");
      if (oTemp != null && oTemp.toString().length() > 0)
        nAssistNum = (UFDouble) oTemp;
      if (nAssistNum != null) {
        if (nAssistNum.doubleValue() != 0.0) {
          bcp.setBodyValueAt(ninvoicenum.div(nAssistNum), e.getRow(), "nexchangerate");
        }
        else {
          // ������Ϊ0,����޸Ļ�����,�����������/������!=0,��������Ϊ0��ì��
          // Ϊ��,�޸ĸ�����,���ı任����
          if (nExchangeRate != null && nExchangeRate.doubleValue() != 0.0) {
            bcp.setBodyValueAt(ninvoicenum.div(nExchangeRate), e.getRow(), "nassistnum");
          }
        }
      }
      else {
        Object objTmp = bcp.getBodyValueAt(e.getRow(), "nexchangerate");
        if (objTmp != null && !objTmp.toString().trim().equals("")) {
          nExchangeRate = new UFDouble(objTmp.toString());
          bcp.setBodyValueAt(ninvoicenum.div(nExchangeRate), e.getRow(), "nassistnum");
        }
        else {
          bcp.setBodyValueAt(null, e.getRow(), "nassistnum");
          bcp.setBodyValueAt(null, e.getRow(), "nexchangerate");
        }
      }
    }
  }

  /**
   * ����"��������"���Ӱ�ť
   * 
   * Ŀǰ��������ֻ֧����Դ����Ϊ�ɹ������Ͳɹ���ⵥ
   * 
   */
  private void createAddContinueBtn() {
    // Ϊ�������������Ӱ�ť
    String strBizType = getBillCardPanel().getHeadItem("cbiztype").getValue();
    btnBillAddContinue.setEnabled(false);
    if (PuPubVO.getString_TrimZeroLenAsNull(strBizType) == null) {
      return;
    }
    InvoiceVO[] contiVos=getInvVOsByContinue();
    if (contiVos == null || contiVos.length < 1)   return;
    String sUpsourcebilltype = PuPubVO.getString_TrimZeroLenAsNull(getBillCardPanel().getBodyValueAt(0, "cupsourcebilltype"));
    if (!BillTypeConst.PO_ORDER.equalsIgnoreCase(sUpsourcebilltype) && !BillTypeConst.STORE_PO.equalsIgnoreCase(sUpsourcebilltype)) {
      return;
    }
    ButtonObject bo = new ButtonObject("btncontinue");
    bo.setTag(strBizType);
    PfUtilClient.retAddBtn(btnBillAddContinue, nc.ui.pub.ClientEnvironment.getInstance().getCorporation()
        .getPrimaryKey(), nc.vo.scm.pu.BillTypeConst.PO_INVOICE, bo);
    Vector vecBtn = btnBillAddContinue.getChildren();
    if (vecBtn.size() > 0) {
      int intLen = vecBtn.size();
      for (int i = 0; i < intLen; i++) {
        ButtonObject btn = (ButtonObject) vecBtn.get(i);
        if ("���Ƶ���".equalsIgnoreCase(btn.getName()) || "Self-prepared document".equalsIgnoreCase(btn.getName()) || !sContinueBillTypeName.equalsIgnoreCase(btn.getName())) {
          // btnBillAddContinue.removeChildButton(btn);
          btn.setVisible(false);
        }
        //For V56 by zhaoyha
        if (BillTypeConst.PO_ORDER.equalsIgnoreCase(sUpsourcebilltype) ||
            isRefFeeOrder(strBizType).booleanValue()) {
          if (BillTypeConst.getBillTypeName(BillTypeConst.PO_ORDER).equalsIgnoreCase(btn.getName())) {
            btn.setVisible(true);
          }
        }
        //For V56 by zhaoyha
        if (BillTypeConst.STORE_PO.equalsIgnoreCase(sUpsourcebilltype) ||
            isRefFeeOrder(strBizType).booleanValue()) {
          if (BillTypeConst.getBillTypeName(BillTypeConst.STORE_PO).equalsIgnoreCase(btn.getName())) {
            btn.setVisible(true);
          }
        }

      }
      for (int i = 0; i < intLen; i++) {
        if(((ButtonObject) vecBtn.get(i)).isVisible()) {
          btnBillAddContinue.setEnabled(true);
          break;
        }
      }
    }
    
   setButtons(m_btnTree.getButtonArray());
//    setButtons(getBtnss());
  }

  private void setButtonsBAPFlag() {
    if (getCurOperState() == STATE_EDIT || getCurOperState() == STATE_LIST_FROM_BILLS) {
      m_btnCrtAPBill.setEnabled(false);
      m_btnDelAPBill.setEnabled(false);
      return;
    }
    if (getInvVOs() == null || getInvVOs()[getCurVOPos()] == null
        || BillStatus.AUDITED.compareTo(getInvVOs()[getCurVOPos()].getHeadVO().getIbillstatus()) != 0) {
      m_btnCrtAPBill.setEnabled(false);
      m_btnDelAPBill.setEnabled(false);
      return;
    }
    // ���ⷢƱ����Ӧ����ȡ����Ӧ����������
    Integer iInvoiceType = getInvVOs()[getCurVOPos()].getHeadVO().getIinvoicetype();
    boolean bVirtFlag = (iInvoiceType != null && iInvoiceType.intValue() == 3);
    //
    UFBoolean bapflag = getInvVOs()[getCurVOPos()].getHeadVO().getBapflag();
    if (PuPubVO.getUFBoolean_NullAs(bapflag, UFBoolean.FALSE).booleanValue()) {
      m_btnCrtAPBill.setEnabled(false);
      m_btnDelAPBill.setEnabled(!bVirtFlag);
    }
    else {
      m_btnCrtAPBill.setEnabled(!bVirtFlag);
      m_btnDelAPBill.setEnabled(false);
    }
    /*
     * ����: Ҫȷ�������ִ���Ӧ��������������ɹ������ ���ҵ������������������������Ӧ�������������ݵģ���ʱ����ʹ��������
     * ���Ƿ������ȡ����Ӧ�����ܣ�Ҫ����������Ƿ�����ȡ����Ӧ������
     */
    // ����Ӧ��: ����ͨ��+(������������{������Ӧ��������ȡ��Ӧ��}||���ⷢƱ)
    if (PuPubVO.getUFBoolean_NullAs(bapflag, UFBoolean.FALSE).booleanValue()) {
      m_btnInvBillUnAudit.setEnabled(BillStatus.AUDITED.compareTo(getInvVOs()[getCurVOPos()].getHeadVO()
          .getIbillstatus()) == 0
          && (isBusitypeAppriveDrive(getInvVOs()[getCurVOPos()].getHeadVO().getCbiztype()) || bVirtFlag));
    }
    // δ��Ӧ��������ͨ��,����������񲻱ع���
    else {
      m_btnInvBillUnAudit.setEnabled(BillStatus.AUDITED.compareTo(getInvVOs()[getCurVOPos()].getHeadVO()
          .getIbillstatus()) == 0);
    }
  }

  /**
   * ����������������Ҫ�����������Ĺ��ܡ�
   * <p>
   * <b>examples:</b>
   * <p>
   * ʹ��ʾ��
   * <p>
   * <b>����˵��</b>
   * <p>
   * 
   * @author lixiaodong
   * @time 2008-8-15 ����10:04:12
   */
  private void onCrtAPBill() {
    if (getInvVOs() == null) {
      return;
    }
    Vector vctKey = new Vector();
    Vector vctInvoice = new Vector();
    if (getCurPanelMode() == INV_PANEL_CARD) {
      vctKey.add(getInvVOs()[getCurVOPos()].getHeadVO().getPrimaryKey());
      vctInvoice.add(getInvVOs()[getCurVOPos()]);
    }
    else if (getCurPanelMode() == INV_PANEL_LIST) {
      ArrayList list = getSelectedBills();
      if (list == null || list.size() < 1) {
        return;
      }
      InvoiceVO inVO[] = new InvoiceVO[list.size()];
      list.toArray(inVO);
      for (int i = 0; i < inVO.length; i++) {
        if (!PuPubVO.getUFBoolean_NullAs(inVO[i].getHeadVO().getBapflag(), UFBoolean.FALSE).booleanValue()) {
          vctKey.add(inVO[i].getHeadVO().getPrimaryKey());
          vctInvoice.add(inVO[i]);
        }
      }
    }

    if (vctKey == null || vctKey.size() < 1) {
      return;
    }
    String[] key = new String[vctKey.size()];
    vctKey.copyInto(key);

    InvoiceVO[] voaInvoice = new InvoiceVO[vctInvoice.size()];
    vctInvoice.copyInto(voaInvoice);
    String[] cinvoiceid = null;
    Object oaUserOne = "MANUAL";
    Object[] oaUser = new Object[voaInvoice.length];
    for (int i = 0; i < oaUser.length; i++) {
      oaUser[i] = oaUserOne;
    }
    try {
      voaInvoice[0].getHeadVO().setCoperatoridnow(ClientEnvironment.getInstance().getUser().getPrimaryKey());
      PfUtilClient.processBatch(this, "CRTAPBILL", nc.vo.scm.pu.BillTypeConst.PO_INVOICE, ClientEnvironment
          .getInstance().getDate().toString(), voaInvoice, oaUser);
      boolean bSucced = PfUtilClient.isSuccess();
      if (bSucced) {
        cinvoiceid = new String[voaInvoice.length];
        for (int i = 0; i < voaInvoice.length; i++) {
          cinvoiceid[i] = voaInvoice[i].getHeadVO().getPrimaryKey();
        }
      }
    }
    catch (Exception e) {
      MessageDialog.showErrorDlg(this,
          nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPPSCMCommon-000270")/*
                                                                                         * @res
                                                                                         * "��ʾ"
                                                                                         */, e.getMessage());
    }

    if (cinvoiceid != null && cinvoiceid.length > 0) {
      Hashtable<String, InvoiceVO> resultH = new Hashtable<String, InvoiceVO>();
      try {
        resultH = InvoiceHelper.findByPrimaryKeyBantch(vctKey);
      }
      catch (Exception e) {
        SCMEnv.out(e);
        MessageDialog.showErrorDlg(this,
            nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPPSCMCommon-000270")/*
                                                                                           * @res
                                                                                           * "��ʾ"
                                                                                           */, e
                .getMessage());
      }
      if (resultH != null) {
        for (int i = 0; i < getInvVOs().length; i++) {
          if (resultH.containsKey(getInvVOs()[i].getHeadVO().getPrimaryKey())) {
            getInvVOs()[i] = resultH.get(getInvVOs()[i].getHeadVO().getPrimaryKey());
          }
        }
      }

      // ���ÿ�Ƭ��������
      setVOToBillPanel();

      setButtonsBAPFlag();
      showHintMessage("�����ɹ�");
    }
    else {
      showHintMessage("���ܴ�Ӧ��");
    }

  }

  /**
   * ��������������ȡ����Ӧ����
   * <p>
   * <b>examples:</b>
   * <p>
   * ʹ��ʾ��
   * <p>
   * <b>����˵��</b>
   * <p>
   * 
   * @author lixiaodong
   * @throws BusinessException
   * @time 2008-8-15 ����10:04:15
   */
  private void onDelAPBill() {
    if (getInvVOs() == null) {
      return;
    }
    String[] key = null;
    Vector vctKey = new Vector();
    Vector vctInvoice = new Vector();

    if (getCurPanelMode() == INV_PANEL_CARD) {
      vctKey.add(getInvVOs()[getCurVOPos()].getHeadVO().getPrimaryKey());
      vctInvoice.add(getInvVOs()[getCurVOPos()]);
    }
    else if (getCurPanelMode() == INV_PANEL_LIST) {
      ArrayList list = getSelectedBills();
      if (list == null || list.size() < 1) {
        return;
      }
      InvoiceVO inVO[] = new InvoiceVO[list.size()];
      list.toArray(inVO);
      key = new String[list.size()];
      for (int i = 0; i < inVO.length; i++) {
        if (PuPubVO.getUFBoolean_NullAs(inVO[i].getHeadVO().getBapflag(), UFBoolean.FALSE).booleanValue()) {
          vctKey.add(inVO[i].getHeadVO().getPrimaryKey());
          vctInvoice.add(inVO[i]);
        }
      }
    }

    if (vctKey == null || vctKey.size() < 1) {
      return;
    }

    key = new String[vctKey.size()];
    vctKey.copyInto(key);

    InvoiceVO[] voaInvoice = new InvoiceVO[vctInvoice.size()];
    vctInvoice.copyInto(voaInvoice);
    String[] cinvoiceid = null;
    Object oaUserOne = "MANUAL";
    Object[] oaUser = new Object[voaInvoice.length];
    for (int i = 0; i < oaUser.length; i++) {
      oaUser[i] = oaUserOne;
    }
    try {
      Object o = PfUtilClient.processBatch(this, "DELAPBILL", nc.vo.scm.pu.BillTypeConst.PO_INVOICE, ClientEnvironment
          .getInstance().getDate().toString(), voaInvoice, oaUser);
      boolean bSucced = PfUtilClient.isSuccess();
      if (bSucced) {
        cinvoiceid = new String[voaInvoice.length];
        for (int i = 0; i < voaInvoice.length; i++) {
          cinvoiceid[i] = voaInvoice[i].getHeadVO().getPrimaryKey();
        }
      }
    }
    catch (Exception e) {
      MessageDialog.showErrorDlg(this,
          nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401", "UPPSCMCommon-000270")/*
                                                                                         * @res
                                                                                         * "��ʾ"
                                                                                         */, e.getMessage());
      showHintMessage(e.getMessage());
      return;
    }

    Hashtable<String, InvoiceVO> resultH = new Hashtable<String, InvoiceVO>();
    try {
      resultH = InvoiceHelper.findByPrimaryKeyBantch(vctKey);
    }
    catch (Exception e) {
      SCMEnv.out(e);
    }
    if (resultH != null) {
      for (int i = 0; i < getInvVOs().length; i++) {
        if (resultH.containsKey(getInvVOs()[i].getHeadVO().getPrimaryKey())) {
          getInvVOs()[i] = resultH.get(getInvVOs()[i].getHeadVO().getPrimaryKey());
        }
      }

      if (getCurPanelMode() == INV_PANEL_CARD) {
        // ���ÿ�Ƭ��������
        setVOToBillPanel();
      }else{
        // ���ÿ�Ƭ��������
        setVOsToListPanel();
      }
      setButtonsBAPFlag();
      showHintMessage("�����ɹ�");
    }
    else {
      showHintMessage("����ȡ����Ӧ��");
    }
  }

  public boolean onEditAction(int action) {
    if (action == BillScrollPane.ADDLINE) {
      getBillCardPanel().getBillModel().addLine();
      // �����к�
      nc.ui.scm.pub.report.BillRowNo.addLineRowNo(getBillCardPanel(), nc.vo.scm.pu.BillTypeConst.PO_INVOICE, "crowno");
      int iNewLine = getBillCardPanel().getBillModel().getRowCount() - 1;
      // ���������е�Ĭ��ֵ
      setNewLineDefaultValue(iNewLine);
      setDefaultBody(iNewLine);
      return false;
    }
    return true;
  }

  /**
   * ���ߣ���ӡ�� ���ܣ�������������ڡ������޸ĺ���Ӧ������޼۱仯 �ú�����afterEdit������������ڡ����ָı����á� ������ int
   * iBeginRow ������ͬ�����Ϣ�ı��忪ʼ�� int iEndRow ������ͬ�����Ϣ�ı�������� ���أ��� ���⣺��
   * ���ڣ�(2002-3-13 11:39:21) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־�� 2003-11-14 wyf ��������
   */
  private void setRelated_AssistUnit(int iBeginRow, int iEndRow) {

    // ��������
    String[] saBaseId = (String[]) PuGetUIValueTool.getArrayNotNull(getBillCardPanel().getBillModel(), "cbaseid",
        String.class, iBeginRow, iEndRow);
    PuTool.loadBatchAssistManaged(saBaseId);

    // ����������
    Vector vecAssistUnitIndex = new Vector();
    Vector vecBaseId = new Vector();
    Vector vecAssistId = new Vector();

    // ����ֵ

    // ����Ĭ�ϸ�����
    String[] aryAssistunit = new String[] {
        // "<formulaset><cachetype>FOREDBCACHE</cachetype></formulaset>" +
        "cassistunit->getColValue(bd_invbasdoc,pk_measdoc2,pk_invbasdoc,cbaseid)",
        "cassistunitname->getColValue(bd_measdoc,measname,pk_measdoc,cassistunit)"
    };
    getBillCardPanel().getBillModel().execFormulas(aryAssistunit, iBeginRow, iEndRow);
    //
    String sBaseId = null;
    String sAssistUnit = null;
    for (int iRow = iBeginRow; iRow <= iEndRow; iRow++) {
      sBaseId = (String) getBillCardPanel().getBodyValueAt(iRow, "cbaseid");
      if (PuTool.isAssUnitManaged(sBaseId)) {
        sAssistUnit = (String) getBillCardPanel().getBodyValueAt(iRow, "cassistunit");
        // Ϊ����������׼��
        if (PuPubVO.getString_TrimZeroLenAsNull(sAssistUnit) != null) {
          vecAssistUnitIndex.add(new Integer(iRow));
          vecBaseId.add(sBaseId);
          vecAssistId.add(sAssistUnit);
        }
      }
      else {
        getBillCardPanel().getBillModel().setValueAt(null, iRow, "cassistunitname");
        getBillCardPanel().getBillModel().setValueAt(null, iRow, "cassistunit");
        getBillCardPanel().getBillModel().setValueAt(null, iRow, "nassistnum");
        getBillCardPanel().getBillModel().setValueAt(null, iRow, "nexchangerate");
      }
    }

    // �������ø���������
    int iAssistUnitLen = vecAssistUnitIndex.size();
    if (iAssistUnitLen > 0) {

      // ��������
      PuTool.loadBatchInvConvRateInfo((String[]) vecBaseId.toArray(new String[iAssistUnitLen]), (String[]) vecAssistId
          .toArray(new String[iAssistUnitLen]));

      // String[] saCurrId =
      // getAllCurrIdFromCard(getRowCount(),getCorp(),getHeadItem("ccurrencytypeid").getValue(),getBillModel());
      // HashMap mapRateMny =
      // m_cardPoPubSetUI2.getMoneyDigitByCurr_Busi(saCurrId);
      // BusinessCurrencyRateUtil bca =
      // m_cardPoPubSetUI2.getCurrArith_Busi(getCorp());

      // ѭ��ִ��
      int iRow = 0;
      for (int i = 0; i < iAssistUnitLen; i++) {
        iRow = ((Integer) vecAssistUnitIndex.get(i)).intValue();

        Object[] oConvRate = PuTool.getInvConvRateInfo((String) vecBaseId.get(i), (String) vecAssistId.get(i));
        if (oConvRate == null) {
          getBillCardPanel().getBillModel().setValueAt(null, iRow, "nexchangerate");
        }
        else {
          getBillCardPanel().getBillModel().setValueAt((UFDouble) oConvRate[0], iRow, "nexchangerate");
        }

        // �����ʸı䣬���¼���
        BillEditEvent tempE = new BillEditEvent(getBillCardPanel().getBodyItem("nexchangerate"), getBillCardPanel()
            .getBodyValueAt(iRow, "nexchangerate"), "nexchangerate", iRow);
        afterEditInvBillRelations(tempE);
      }
    }

    // ���ÿɱ༭��
    // setEnabled_BodyAssistUnitRelated(iBeginRow,iEndRow) ;

  }

  /**
   * ����ҵ�����������������Ƿ�����������������Ӧ�� + ��������ȡ����Ӧ��
   * <p>
   * <b>examples:</b>
   * <p>
   * ʹ��ʾ��
   * <p>
   * <b>����˵��</b>
   * 
   * @param strBiztypeId
   * @return
   *          <p>
   * @author czp
   * @time 2008-10-8 ����12:42:38
   */
  protected boolean isBusitypeAppriveDrive(String strBiztypeId) {
    if (strBiztypeId == null)
      return false;
    if (m_mapBusitypeAppriveDrive == null) {
      m_mapBusitypeAppriveDrive = new HashMap<String, UFBoolean>();
    }
    if (m_mapBusitypeAppriveDrive.containsKey(strBiztypeId)) {
      return m_mapBusitypeAppriveDrive.get(strBiztypeId).booleanValue();
    }
    IPFMetaModel srvPFMetaModel = (IPFMetaModel) NCLocator.getInstance().lookup(IPFMetaModel.class.getName());
    try {
      MessagedriveVO[] driveVOs = srvPFMetaModel.queryAllMsgdrvVOs(getCorpPrimaryKey(), BillTypeConst.PO_INVOICE,
          strBiztypeId, "APPROVE");
      if (driveVOs == null || driveVOs.length == 0) {
        m_mapBusitypeAppriveDrive.put(strBiztypeId, UFBoolean.FALSE);
      }
      else {
        driveVOs = srvPFMetaModel.queryAllMsgdrvVOs(getCorpPrimaryKey(), BillTypeConst.PO_INVOICE, strBiztypeId,
            "UNAPPROVE");
        if (driveVOs == null || driveVOs.length == 0) {
          m_mapBusitypeAppriveDrive.put(strBiztypeId, UFBoolean.FALSE);
        }
        else {
          m_mapBusitypeAppriveDrive.put(strBiztypeId, UFBoolean.TRUE);
        }
      }
    }
    catch (BusinessException e) {
      showHintMessage(e.getMessage());
      return false;
    }
    return m_mapBusitypeAppriveDrive.get(strBiztypeId).booleanValue();
  }
  
  /**
   * 
   * ���������������õ���ǰ��Ƭ�ϵķ�ƱVO��
   * <p>
   * <b>examples:</b>
   * <p>
   * ʹ��ʾ��
   * <p>
   * <b>����˵��</b>
   * @return
   * <p>
   * @author zhaoyha
   * @time 2009-1-19 ����11:45:34
   */
  private InvoiceVO getCurVOonCard(){
    return (InvoiceVO)getBillCardPanel().getBillValueVO(InvoiceVO.class.getName(), 
        InvoiceHeaderVO.class.getName(), InvoiceItemVO.class.getName());
  }
  
  /**
   * 
   * ����������������Ҫ�����������Ĺ��ܡ�
   * <p>
   * <p>���ο������֧�� by zhaoyha at 2009.1.19
   * <p>
   * <b>examples:</b>
   * <p>
   * ʹ��ʾ��
   * <p>
   * <b>����˵��</b>
   * @return
   * <p>
   * @author zhyhang
   * @time 2009-1-19 ����08:19:18
   */
  public PIPluginUI getPluginUI() {
    if(pluginui==null)
      pluginui = new PIPluginUI(this);
    return pluginui;
  }
  
  /**
   * 
   * ����������������Ҫ�����������Ĺ��ܡ�
   * <p>
   * <p>���ο������֧�� by zhaoyha at 2009.1.19
   * <p>
   * <b>examples:</b>
   * <p>
   * ʹ��ʾ��
   * <p>
   * <b>����˵��</b>
   * @return
   * <p>
   * @author zhyhang
   * @time 2009-1-19 ����08:14:18
   */
  public InvokeEventProxy getInvokeEventProxy() {
    if(invokeEventProxy == null){
      invokeEventProxy = new InvokeEventProxy("pu",BillTypeConst.PO_INVOICE, getPluginUI());
    }
    return invokeEventProxy;
  }
  
  public void clearAuditInfo(InvoiceVO vo,BillCardPanel bcp){
    if(vo!=null && vo.getHeadVO()!=null){
      vo.getHeadVO().setCauditpsn(null);
      vo.getHeadVO().setDauditdate(null);
      vo.getHeadVO().setTaudittime(null);
    }
    if(bcp!=null){
      bcp.getTailItem("cauditpsn").setValue(null);
      bcp.getTailItem("dauditdate").setValue(null);
      bcp.getTailItem("taudittime").setValue(null);
    }
  }
 
  /**
   * 
   * ���������������õ���ǰ�ķ�Ʊ����VO���顣
   * <p>
   * <b>examples:</b>
   * <p>
   * ʹ��ʾ��
   * <p>
   * <b>����˵��</b>
   * @return
   * <p>
   * @author zhaoyha
   * @time 2009-3-18 ����02:15:36
   */
  public Object[] getCurrBodyVOs(){
    if(getInvVOs()!=null && getInvVOs().length>0)
      return getInvVOs()[getCurVOPos()].getBodyVO();
    else
      return null;   
  }
  
  /**
   * 
   * ��������������ҵ�������Ƿ񣬷�Ʊ�ж�������ⵥ������Դ������Ʊֻ�ܴӶ������շ�����
   * <p>For V56
   * <p> 
   * <b>examples:</b>
   * <p>
   * ʹ��ʾ��
   * <p>
   * <b>����˵��</b>
   * @return
   * <p>
   * @author zhaoyha
   * @time 2009-3-23 ����09:44:55
   */
  private UFBoolean isRefFeeOrder(){ 
    String cbiztype=(String) getBillCardPanel().getHeadItem("cbiztype").getValueObject();
    if(!StringUtil.isEmptyWithTrim(cbiztype)) return isRefFeeOrder(cbiztype);
    return UFBoolean.FALSE;
  }
  
  /**
   * ��������������ҵ�������Ƿ񣬷�Ʊ�ж�������ⵥ������Դ������Ʊֻ�ܴӶ������շ�����
   * @param bizType 
   * @return UFBoolean
   */
  private UFBoolean isRefFeeOrder(String bizType){ 
    if(!onlyRefFeeOrder.containsKey(bizType))
      onlyRefFeeOrder.put(bizType, new UFBoolean(InvoiceHelper.isPIOnlyRefFeeOrder(bizType)));
    return onlyRefFeeOrder.get(bizType);
  }
  
  /**
   * 
   * ������������������ҵ������Ҫ�����Դ�ڶ����ķ����������ֶβ������޸ġ�
   * <p>For V56
   * <p>
   * <b>examples:</b>
   * <p>
   * ʹ��ʾ��
   * <p>
   * <b>����˵��</b>
   * <p>
   * @author zhaoyha
   * @time 2009-3-23 ����10:27:10
   */
  private void setNumEditable(){
    if(!isRefFeeOrder().booleanValue()) return;
    int iCount = getBillCardPanel().getRowCount();
    for (int i = 0; i < iCount; i++) {
      if(ScmConst.PO_Order.equals(getBillCardPanel().getBodyValueAt(i, "cupsourcebilltype")) &&
          PuTool.isFee(PuPubVO.getString_TrimZeroLenAsNull(getBillCardPanel().getBodyValueAt(i, "cbaseid")))){
        getBillCardPanel().setCellEditable(i, "ninvoicenum", false);
        getBillCardPanel().setCellEditable(i, "nassistnum", false);
      }
    }
  }
  
  /**
   * 
   * �������������������������ⷢƱ��
   * <p>
   * <b>examples:</b>
   * <p>
   * ʹ��ʾ��
   * <p>
   * <b>����˵��</b>
   * @param e
   * <p>
   * @author zhaoyha
   * @time 2009-7-17 ����10:26:06
   */
  private void afterEditInvoicetype(BillEditEvent e){
    UIComboBox comItem = (UIComboBox) getBillCardPanel().getHeadItem("iinvoicetype").getComponent();
    //�����ѡ�������ⷢƱ����Ļ�ԭ��Ʊ����
    if(comItem.getSelectedIndex()==3){
      showErrorMessage(NCLangRes.getInstance().getStrByID("40040401",
          "UPP40040401-000273")/*"�����������ⷢƱ��"*/);
      comItem.setSelectedIndex(curInvType);
    }
    else
      setCurInvoiceType();
  }
 
  /**
   * 
   * �����������������浱ǰ��Ʊ���͡�
   * <p>
   * <b>examples:</b>
   * <p>
   * ʹ��ʾ��
   * <p>
   * <b>����˵��</b>
   * <p>
   * @author zhaoyha
   * @time 2009-7-17 ����10:25:52
   */
  private void setCurInvoiceType(){
    UIComboBox comItem = (UIComboBox) getBillCardPanel().getHeadItem("iinvoicetype").getComponent();
    curInvType=comItem.getSelectedIndex(); 
  }
 
  
  /**
   * 
   * ���������������б���������
   * <p>
   * <b>examples:</b>
   * <p>
   * ʹ��ʾ��
   * <p>
   * <b>����˵��</b>
   * <p>
   * @author zhaoyha
   * @time 2009-8-20 ����03:26:21
   */
  private void onSendAuditBatch(){
    InvoiceVO[] vos=getSendAuditVos();
    if(0==vos.length) return;
    try {
      //������������Ϣ���������n����ʼλ��(n=vos.length)
      Object[] retValues=PfUtilClient.runBatch(this, "SAVE", ScmConst.PO_Invoice, ClientEnvironment.getInstance().getDate().toString(),
          vos, null, null, null);
      if(PfUtilClient.isSuccess()){
        //���»��漰UI
        updateCacheVo((Map<String, Map<String, Object>>) retValues[vos.length]);
        updateListUI((Map<String, Map<String, Object>>) retValues[vos.length]);
        showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UCH023"));
        return;
      }
    }
    catch (Exception e) {
      //��־�쳣
      SCMEnv.out(e);
      MessageDialog.showErrorDlg(this, null, e.getMessage());
    }
    showHintMessage(NCLangRes.getInstance().getStrByID("40040401", "UPP40040401-000257")
        /*
         * @res
         * "����δ�ɹ���"
         */);
  }
  
  private InvoiceVO[] getSendAuditVos(){
    List<InvoiceVO> selectedVos=new ArrayList<InvoiceVO>();
    for(int row:getBillListPanel().getHeadTable().getSelectedRows()){
      int status=(Integer) getBillListPanel().getHeadBillModel().getValueAt(row, "ibillstatus");
      String billMaker=(String) getBillListPanel().getHeadBillModel().getValueAt(row, "coperator");
      if((BillStatus.FREE.intValue()==status || BillStatus.AUDITFAIL.intValue()==status) && getCurOperator().equals(billMaker)){
        InvoiceVO vo=new InvoiceVO();
        vo.setParentVO(new InvoiceHeaderVO());
        setHeadInfoFrmList(vo.getHeadVO(),row);
        selectedVos.add(vo);
      }
    }
    return selectedVos.toArray(new InvoiceVO[selectedVos.size()]);
  }
  
  /**
   * 
   * �����������������ñ�ͷVOҪ��������ƽ̨�ı�Ҫ��Ϣ��
   * <p>
   * <b>examples:</b>
   * <p>
   * ʹ��ʾ��
   * <p>
   * <b>����˵��</b>
   * @param head
   * @param row
   * <p>
   * @author zhaoyha
   * @time 2009-8-20 ����10:50:33
   */
  private void setHeadInfoFrmList(InvoiceHeaderVO head,int row){
    head.setCinvoiceid((String) getBillListPanel().getHeadBillModel().getValueAt(row, "cinvoiceid"));
    head.setVinvoicecode((String) getBillListPanel().getHeadBillModel().getValueAt(row, "vinvoicecode"));
    head.setIbillstatus((Integer) getBillListPanel().getHeadBillModel().getValueAt(row, "ibillstatus"));
    head.setCoperator((String) getBillListPanel().getHeadBillModel().getValueAt(row, "coperator"));
    head.setPk_corp((String) getBillListPanel().getHeadBillModel().getValueAt(row, "pk_corp"));
    head.setCbiztype((String) getBillListPanel().getHeadBillModel().getValueAt(row, "cbiztype"));
  }
  
  /**
   * 
   * �����������������»���VO��Ϣ��
   * <p>
   * <b>examples:</b>
   * <p>
   * ʹ��ʾ��
   * <p>
   * <b>����˵��</b>
   * @param newInfos
   * <p>
   * @author zhaoyha
   * @time 2009-8-20 ����10:51:10
   */
  private void updateCacheVo(Map<String,Map<String,Object>> newInfos){
    Map<String, InvoiceVO> cacheVos = getCacheVoMap();
    for(String id:newInfos.keySet()){
      Map<String,Object> info=newInfos.get(id);
      //���»���VO
      InvoiceVO vo=cacheVos.get(id);
      if(null!=vo)
        for(String column:info.keySet())
          vo.getHeadVO().setAttributeValue(column, info.get(column));
    }
  }

  /**
   * �������������������б�������Ϣ��
   * <p>
   * <b>examples:</b>
   * <p>
   * ʹ��ʾ��
   * <p>
   * <b>����˵��</b>
   * @param newInfos
   * @param cacheVos
   * @param billsOnList
   * <p>
   * @author zhaoyha
   * @time 2009-8-20 ����10:53:51
   */
  private void updateListUI(Map<String, Map<String, Object>> newInfos) {
    Map<String,Integer> billsOnList=getBillListIndexMap();
    for(String id:newInfos.keySet()){
      Map<String,Object> info=newInfos.get(id);
      //�����б����
      Integer row=billsOnList.get(id);
      if(null!=row)
        for(String column:info.keySet())
          getBillListPanel().getHeadBillModel().setValueAt(info.get(column), row, column);
    }
  }

  /**
   * ���������������õ����б�Ʊ�������б��е�Map��
   * <p>
   * <b>examples:</b>
   * <p>
   * ʹ��ʾ��
   * <p>
   * <b>����˵��</b>
   * @return
   * <p>
   * @author zhaoyha
   * @time 2009-8-20 ����10:43:23
   */
  private Map<String, Integer> getBillListIndexMap() {
    //�����б�VO��map
    Map<String,Integer> billsOnList=new HashMap<String, Integer>();
    for (int i = 0; i < getBillListPanel().getHeadBillModel().getRowCount(); i++)
      billsOnList.put((String) getBillListPanel().getHeadBillModel().getValueAt(i, "cinvoiceid"), i);
    return billsOnList;
  }

  /**
   * ���������������õ��ķ�ƱVO������VO��Map��
   * <p>
   * <b>examples:</b>
   * <p>
   * ʹ��ʾ��
   * <p>
   * <b>����˵��</b>
   * @return
   * <p>
   * @author zhaoyha
   * @time 2009-8-20 ����10:42:05
   */
  private Map<String, InvoiceVO> getCacheVoMap() {
    //��������VO��map
    Map<String,InvoiceVO> cacheVos=new HashMap<String, InvoiceVO>();
    for(InvoiceVO vo:getInvVOs())
      cacheVos.put(vo.getHeadVO().getCinvoiceid(), vo);
    return cacheVos;
  }

  /**
   * ���������������õ��б�ǰѡ����(ע����ʱΪ�˴���getCurVOPos()������˲�һ�£�
   * ��������ʼ�շ����б�ǰѡ���У���ѡʱ��Ϊ����µ��С�
   * <p>
   * <b>examples:</b>
   * <p>
   * ʹ��ʾ��
   * <p>
   * <b>����˵��</b>
   * @return
   * <p>
   * @author zhaoyha
   * @time 2009-8-20 ����02:58:17
   */
  private int getSelectedRowOnList() {
    // �õ�ѡ�е���
    int row = -1;
    for (int i = 0; i < getInvVOs().length; i++) {
      if (getBillListPanel().getHeadBillModel().getRowState(i) == BillModel.SELECTED) {
        row = i;
        break;
      }
    }
    return row;
  }

  /**
   * @return billTempletVo
   */
  public BillTempletVO getBillTempletVo() {
    if(null == billTempletVo)
      billTempletVo=BillUIUtil.getDefaultTempletStatic(getModuleCode(), 
          null, getCurOperator(), getCorpId(), null, null);
    return billTempletVo;
  }
  
  /**
   * 
   * ���������������ж��Ƿ���Ҫ������Դ��Դͷ������Ϣ��
   * <p>
   * <b>examples:</b>
   * <p>
   * ʹ��ʾ��
   * <p>
   * <b>����˵��</b>
   * @param model
   * @return
   * <p>
   * @author zhaoyha
   * @time 2009-8-24 ����09:54:22
   */
  private boolean isNeedLoadSrcInfo(BillModel model){
    String[] checkItems=new String[]{"csourcebillcode","csourcebillrowno",
        "cancestorbillcode","cancestorbillrowno"};
    for(String key:checkItems)
      if(null!=model.getItemByKey(key) && model.getItemByKey(key).isShow())
        return true;
    return false;
  }
  
  private void setVoExchgRateDigit(InvoiceItemVO[] items){
    if(null==items || 0==items.length) return;
    for(InvoiceItemVO item:items){
      if(null==item.getNexchangeotobrate() || StringUtil.isEmptyWithTrim(item.getCcurrencytypeid())) continue;
      int[] digits=getPoPubSetUi2().getBothExchRateDigit(getPk_corp(), item.getCcurrencytypeid());
      if(null==digits || 0==digits.length) continue;
      item.setNexchangeotobrate(item.getNexchangeotobrate().add(UFDouble.ZERO_DBL, digits[0]));
    }
  }
  
}