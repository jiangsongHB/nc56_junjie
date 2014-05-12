package nc.ui.sp.sp002;

import static nc.vo.jcom.lang.StringUtil.isEmpty;
import static nc.vo.jcom.lang.StringUtil.isEmptyWithTrim;
import static nc.vo.sp.sp002.AdjFindPriceResultVO.FIND_SUCCESS;
import static nc.vo.sp.sp002.AdjFindPriceResultVO.NO_PRICE;
import static nc.vo.sp.sp002.AdjFindPriceResultVO.NO_PRICE_TYPE;
import static nc.vo.sp.sp002.AdjFindPriceResultVO.OCCUR_ERROR;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.swing.JComboBox;

import nc.bs.framework.common.NCLocator;
import nc.bs.scm.pub.query.QueryUtil;
import nc.itf.scm.sp.sp002.QuickInputResultVO;
import nc.itf.scm.sp.sp002.QuickInputResultVO.PriceType;
import nc.itf.uap.pf.IPFWorkflowQry;
import nc.ui.ml.NCLangRes;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UIMenuItem;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.bill.BillBodyMenuListener;
import nc.ui.pub.bill.BillCardBeforeEditListener;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.bill.BillData;
import nc.ui.pub.bill.BillEditListener;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillItemEvent;
import nc.ui.pub.bill.BillListData;
import nc.ui.pub.bill.BillListPanel;
import nc.ui.pub.bill.BillModel;
import nc.ui.pub.bill.BillStatus;
import nc.ui.pub.bill.BillTableMouseListener;
import nc.ui.pub.linkoperate.ILinkApprove;
import nc.ui.pub.linkoperate.ILinkApproveData;
import nc.ui.pub.linkoperate.ILinkMaintain;
import nc.ui.pub.linkoperate.ILinkMaintainData;
import nc.ui.pub.linkoperate.ILinkQuery;
import nc.ui.pub.linkoperate.ILinkQueryData;
import nc.ui.pub.para.SysInitBO_Client;
import nc.ui.pub.pf.PfUtilClient;
import nc.ui.pub.print.PrintEntry;
import nc.ui.pub.query.QueryConditionClient;
import nc.ui.scm.print.IFreshTsListener;
import nc.ui.scm.print.PrintLogClient;
import nc.ui.scm.print.SalePubPrintDS;
import nc.ui.scm.pub.billutil.ClientCacheHelper;
import nc.ui.scm.pub.cache.CacheTool;
import nc.ui.scm.pub.def.DefSetTool;
import nc.ui.scm.pub.query.SCMTreeQueryConditionDLG;
import nc.ui.scm.pub.report.LocateDialog;
import nc.ui.scm.so.SaleBillType;
import nc.ui.sp.pub.FindSalePriceBO_Client;
import nc.ui.sp.pub.PublicPriceParam;
import nc.ui.sp.pub.ShowMsgDlg;
import nc.ui.sp.pub.UniBillFillSrv;
import nc.ui.sp.sp001.DiscountBOClient;
import nc.ui.sp.sp001.PricetypeBO_Client;
import nc.ui.sp.sp001.TariffBO_Client;
import nc.ui.sp.sp004.SpecialwarepriceBO_Client;
import nc.vo.bd.b15.CorpInventoryVO;
import nc.vo.bd.b15.GroupInventoryVO;
import nc.vo.bd.b15.InventoryVO;
import nc.vo.dm.pub.DMDataVO;
import nc.vo.ml.NCLangRes4VoTransl;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pub.bill.BillTempletVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.lang.UFTime;
import nc.vo.pub.pf.IWorkFlowStatus;
import nc.vo.pub.query.ConditionVO;
import nc.vo.scm.print.PrintConst;
import nc.vo.scm.print.ScmPrintlogVO;
import nc.vo.scm.pub.SCMEnv;
import nc.vo.scm.pub.isnull.NullStr;
import nc.vo.sp.pub.TransUniBillBodyVO;
import nc.vo.sp.pub.TransUniBillHeadVO;
import nc.vo.sp.pub.TransUniBillVO;
import nc.vo.sp.pub.bill.SO2BillType;
import nc.vo.sp.service.PriceAskResultVO;
import nc.vo.sp.service.SalePriceVO;
import nc.vo.sp.sp001.DiscountlistVO;
import nc.vo.sp.sp001.PricetypeVO;
import nc.vo.sp.sp001.PrmtariffHeadVO;
import nc.vo.sp.sp001.PrmtariffItemVO;
import nc.vo.sp.sp002.AdjFindPriceResultVO;
import nc.vo.sp.sp002.AdjustpriceHeaderVO;
import nc.vo.sp.sp002.AdjustpriceItemVO;
import nc.vo.sp.sp002.AdjustpriceVO;
import nc.vo.sp.sp002.InvDisplayVO;
import nc.vo.sp.sp004.SpecialwarepriceVO;

/**
 * ���۵��۵���
 * �������ڣ�(2001-5-29 13:23:57)
 * @author������
 */
public class AdjustPriceUI
	extends nc.ui.pub.ToftPanel
	implements BillEditListener,nc.ui.pub.bill.BillEditListener2, ItemListener,
	BillTableMouseListener, nc.ui.pf.query.ICheckRetVO,ILinkQuery,ILinkApprove,ILinkMaintain,IFreshTsListener,BillCardBeforeEditListener {
	private static final long serialVersionUID = 1L;
	private BillCardPanel cpAdjust = null;
	private BillListPanel lpAdjust = null;
//	private BillModel bmAdjust = null;
	private JComboBox adjustmode = null;
	private AdjustpriceVO voAdjustprice = null;
//	private SCMQueryConditionDlg  dlgQuery = null;
	private SCMTreeQueryConditionDLG  dlgQuery = null;
	/** ��ѯ���������� */
	private boolean queryForApprove = false;
	private ClientEnvironment ce = ClientEnvironment.getInstance();
//	private Hashtable<String, UFBoolean> m_ht = new Hashtable<String, UFBoolean>(); //��������ֶα༭��
	
	//��ť
	protected ButtonObject bnPrint = new ButtonObject(nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UC001-0000007")/*@res "��ӡ"*/, nc.ui.ml.NCLangRes.getInstance().getStrByID("scmsoprice","UPPscmsoprice-000248")/*@res "��ӡ���۵�"*/, 0,"��ӡ");	/*-=notranslate=-*/
	protected ButtonObject bnPreview = new ButtonObject(nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON","UPPSCMCommon-000305")/*@res "Ԥ��"*/, nc.ui.ml.NCLangRes.getInstance().getStrByID("scmsoprice","UPPscmsoprice-000249")/*@res "Ԥ�����۵�"*/, 0,"Ԥ��");	/*-=notranslate=-*/
	protected ButtonObject bnExport = new ButtonObject(nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON","UPPSCMCommon-000249")/*@res "���"*/, nc.ui.ml.NCLangRes.getInstance().getStrByID("scmsoprice","UPPscmsoprice-000250")/*@res "������۵�"*/, 0,"���");	/*-=notranslate=-*/
	protected ButtonObject bnQuick = new ButtonObject(nc.ui.ml.NCLangRes.getInstance().getStrByID("scmsoprice","UPPscmsoprice-000251")/*@res "��¼"*/, nc.ui.ml.NCLangRes.getInstance().getStrByID("scmsoprice","UPPscmsoprice-000252")/*@res "һ��¼�������"*/, 0,"��¼");	/*-=notranslate=-*/
	protected ButtonObject bnAdd = new ButtonObject(nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UC001-0000002")/*@res "����"*/, nc.ui.ml.NCLangRes.getInstance().getStrByID("scmsoprice","UPPscmsoprice-000253")/*@res "���ӵ��۵�"*/, 0,"����");	/*-=notranslate=-*/
//	protected ButtonObject bnModifyBatch = new ButtonObject(nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UC001-0000085")/*@res ""*/, nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UC001-0000085")/*@res "���ӵ��۵�"*/, 0,"����");	/*-=notranslate=-*/

	protected ButtonObject bnAddBase = new ButtonObject(nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UC000-0003735")/*@res "������"*/, nc.ui.ml.NCLangRes.getInstance().getStrByID("scmsoprice","UPPscmsoprice-000254")/*@res "���ӵ����۵��۵�"*/, 0,"������");	/*-=notranslate=-*/
	protected ButtonObject bnAddDiscount = new ButtonObject(nc.ui.ml.NCLangRes.getInstance().getStrByID("scmsoprice","UPPSCMSOPRICE-000807")/*@res "����׼�ۿ�"*/, nc.ui.ml.NCLangRes.getInstance().getStrByID("scmsoprice","UPPSCMSOPRICE-000808")/*@res "���ӵ���׼�ۿ۵��۵�"*/, 0,"����׼�ۿ�");	/*-=notranslate=-*/
	protected ButtonObject bnAddSpec = new ButtonObject(nc.ui.ml.NCLangRes.getInstance().getStrByID("scmsoprice","UPPscmsoprice-000255")/*@res "���ؼ�"*/, nc.ui.ml.NCLangRes.getInstance().getStrByID("scmsoprice","UPPscmsoprice-000256")/*@res "���ӵ��ؼ۵��۵�"*/, 0,"���ؼ�");	/*-=notranslate=-*/

	protected ButtonObject bnCopy = new ButtonObject(nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UC001-0000043")/*@res "����"*/, nc.ui.ml.NCLangRes.getInstance().getStrByID("scmsoprice","UPPscmsoprice-000257")/*@res "���Ƶ��۵�"*/, 0,"����");	/*-=notranslate=-*/

	protected ButtonObject bnUpdate = new ButtonObject(nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UC001-0000045")/*@res "�޸�"*/, nc.ui.ml.NCLangRes.getInstance().getStrByID("scmsoprice","UPPscmsoprice-000258")/*@res "�޸ĵ��۵�"*/, 1,"�޸�");	/*-=notranslate=-*/
	protected ButtonObject bnLineOptr = new ButtonObject(nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UC001-0000011")/*@res "�в���"*/, nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UC001-0000011")/*@res "�в���"*/, 2,"�в���");	/*-=notranslate=-*/
	protected ButtonObject bnAddLine = new ButtonObject(nc.ui.ml.NCLangRes.getInstance().getStrByID("scmsoprice","UPPSCMSOPRICE-000077")/*@res "������"*/, nc.ui.ml.NCLangRes.getInstance().getStrByID("scmsoprice","UPPSCMSOPRICE-000078")/*@res "����һ��"*/, 2,"������");	/*-=notranslate=-*/
	protected ButtonObject bnAddLineBatch = new ButtonObject(nc.ui.ml.NCLangRes.getInstance().getStrByID("scmsoprice","UPPscmsoprice-000259")/*@res "���ٵ���"*/, nc.ui.ml.NCLangRes.getInstance().getStrByID("scmsoprice","UPPscmsoprice-000259")/*@res "���ٵ���"*/, 2,"���ٵ���");	/*-=notranslate=-*/
	protected ButtonObject bnDelLine = new ButtonObject(nc.ui.ml.NCLangRes.getInstance().getStrByID("scmsoprice","UPPSCMSOPRICE-000080")/*@res "ɾ����"*/, nc.ui.ml.NCLangRes.getInstance().getStrByID("scmsoprice","UPPSCMSOPRICE-000081")/*@res "ɾ��һ��"*/, 3,"ɾ����");	/*-=notranslate=-*/
	protected ButtonObject bnSave = new ButtonObject(nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UC001-0000001")/*@res "����"*/, nc.ui.ml.NCLangRes.getInstance().getStrByID("scmsoprice","UPPscmsoprice-000260")/*@res "������۵�"*/, 3,"����");	/*-=notranslate=-*/
	protected ButtonObject bnDelete = new ButtonObject(nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UC001-0000039")/*@res "ɾ��"*/, nc.ui.ml.NCLangRes.getInstance().getStrByID("scmsoprice","UPPscmsoprice-000261")/*@res "���ϵ��۵�"*/, 3,"����");	/*-=notranslate=-*/
	protected ButtonObject bnCancel = new ButtonObject(nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UC001-0000008")/*@res "ȡ��"*/, nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON","UPPSCMCommon-000205")/*@res "ȡ������"*/, 0,"ȡ��");	/*-=notranslate=-*/
	protected ButtonObject bnModifySpecialStopTime = new ButtonObject(NCLangRes.getInstance().getStrByID("scmsoprice","UPPSCMSOPRICE-000809")/*@res "������ֹ"*/, NCLangRes.getInstance().getStrByID("scmsoprice","UPPSCMSOPRICE-000809")/*@res "������ֹ"*/, 0,"������ֹ");	/*-=notranslate=-*/;
	protected ButtonObject bnSendAudit = new ButtonObject(nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON","UPPSCMCommon-000265")/*@res "����"*/, nc.ui.ml.NCLangRes.getInstance().getStrByID("scmsoprice","UPPscmsoprice-000262")/*@res "������۵�"*/, 0,"����");	/*-=notranslate=-*/
	protected ButtonObject bnAudit = new ButtonObject(nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UC001-0000027")/*@res "����"*/, nc.ui.ml.NCLangRes.getInstance().getStrByID("scmsoprice","UPPscmsoprice-000263")/*@res "�������۵�"*/, 0,"����");	/*-=notranslate=-*/
	protected ButtonObject bnUnAudit = new ButtonObject(nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UC001-0000028")/*@res "ȡ������"*/, nc.ui.ml.NCLangRes.getInstance().getStrByID("scmsoprice","UPPscmsoprice-000264")/*@res "ȡ���������۵�"*/, 0,"ȡ������");	/*-=notranslate=-*/
	protected ButtonObject bnQuery = new ButtonObject(nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UC001-0000006")/*@res "��ѯ"*/, nc.ui.ml.NCLangRes.getInstance().getStrByID("scmsoprice","UPPscmsoprice-000265")/*@res "��ѯ���۵�"*/, 0,"��ѯ");	/*-=notranslate=-*/
	protected ButtonObject bnAuditFlowStatus = new ButtonObject(nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON","UPPSCMCommon-000240")/*@res "������״̬"*/, nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON","UPPSCMCommon-000240")/*@res "������״̬"*/, 0,"������״̬");	/*-=notranslate=-*/
	protected ButtonObject bnFirst = new ButtonObject(nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON","UPPSCMCommon-000248")/*@res "����"*/, nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON","UPPSCMCommon-000248")/*@res "����"*/, 0,"����");	/*-=notranslate=-*/
	protected ButtonObject bnPrev = new ButtonObject(nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON","UPPSCMCommon-000232")/*@res "����"*/, nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON","UPPSCMCommon-000232")/*@res "����"*/, 0,"����");	/*-=notranslate=-*/
	protected ButtonObject bnNext = new ButtonObject(nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON","UPPSCMCommon-000281")/*@res "����"*/, nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON","UPPSCMCommon-000281")/*@res "����"*/, 0,"����");	/*-=notranslate=-*/
	protected ButtonObject bnLast = new ButtonObject(nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON","UPPSCMCommon-000177")/*@res "ĩ��"*/, nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON","UPPSCMCommon-000177")/*@res "ĩ��"*/, 0,"ĩ��");	/*-=notranslate=-*/
	protected ButtonObject bnBrowse = new ButtonObject(nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UC001-0000021")/*@res "���"*/, nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UC001-0000021")/*@res "���"*/, 0,"���");	/*-=notranslate=-*/
	protected ButtonObject bnReturn = new ButtonObject(nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON","UPPSCMCommon-000464")/*@res "�л�"*/, nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON","UPPSCMCommon-000464")/*@res "�л�"*/, 0,"�л�");	/*-=notranslate=-*/
	protected ButtonObject bnLocate = new ButtonObject(nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON","UPPSCMCommon-000089")/*@res "��λ"*/, nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON","UPPSCMCommon-000089")/*@res "��λ"*/, 0,"��λ");	/*-=notranslate=-*/
	protected ButtonObject bnHelp = new ButtonObject(nc.ui.ml.NCLangRes.getInstance().getStrByID("scmsoprice","UPPSCMSOPRICE-000083")/*@res "����"*/, nc.ui.ml.NCLangRes.getInstance().getStrByID("scmsoprice","UPPSCMSOPRICE-000083")/*@res "����"*/, 0,"����");	/*-=notranslate=-*/

	protected ButtonObject bnAffectCorp = new ButtonObject(nc.ui.ml.NCLangRes.getInstance().getStrByID("scmsoprice","UPPscmsoprice-000266")/*@res "Ӱ�칫˾"*/, nc.ui.ml.NCLangRes.getInstance().getStrByID("scmsoprice","UPPscmsoprice-000266")/*@res "Ӱ�칫˾"*/, 0,"Ӱ�칫˾");	/*-=notranslate=-*/
	protected ButtonObject bnAdjustAnalyse = new ButtonObject(nc.ui.ml.NCLangRes.getInstance().getStrByID("scmsoprice","UPPscmsoprice-000267")/*@res "���۷���"*/, nc.ui.ml.NCLangRes.getInstance().getStrByID("scmsoprice","UPPscmsoprice-000267")/*@res "���۷���"*/, 0,"���۷���");	/*-=notranslate=-*/
	protected ButtonObject boDocument = new ButtonObject(nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON","UPPSCMCommon-000278")/*@res "�ĵ�����"*/, nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON","UPPSCMCommon-000278")/*@res "�ĵ�����"*/, 0,"�ĵ�����");	/*-=notranslate=-*/
	protected ButtonObject boAuditMsg = new ButtonObject(nc.ui.ml.NCLangRes.getInstance().getStrByID("scmsoprice","UPPscmsoprice-000776")/*@res "������Ϣ"*/, nc.ui.ml.NCLangRes.getInstance().getStrByID("scmsoprice","UPPscmsoprice-000776")/*@res "������Ϣ"*/, 0,"������Ϣ");	/*-=notranslate=-*/
	protected ButtonObject boAction = new ButtonObject(
			nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
					"UC001-0000026")/* @res "ִ��" */, nc.ui.ml.NCLangRes
					.getInstance().getStrByID("SCMCOMMON",
							"UPPSCMCommon-000323")/* @res "ִ�в���" */, 0, "ִ��"); /*-=notranslate=-*/

	protected ButtonObject[] aButtonGroup =
		{
			bnBrowse,
			bnAdd,
			bnUpdate,
			bnSave,
			bnLineOptr,
			bnCancel,
			bnModifySpecialStopTime,
			boAction,
			bnAffectCorp,
			
			bnAuditFlowStatus,

			bnPreview,
			bnPrint,
			bnReturn,
			boDocument,
			bnFirst,
			bnPrev,
			bnNext,
			bnLast,
			bnAdjustAnalyse};
	//��ʾ�ֶκ͸�ʽ
	private int iStatus = 0;
	private int iOldStatus = 0;
	protected final int INIT = 0;
	protected final int NEW = 1;
	protected final int UPDATED = 2;
	protected final int DELETED = 3;
	protected final int FREE = 4;
	protected final int AUDITED = 5;
	protected final int AUDITING=7;
	private boolean isQuery = false;
	protected boolean isList = true;
	private int iCurrRow = -1;
	private String queryCondition = null;
	protected int pricedigit = 8;
	private Boolean m_bcodechanged=new Boolean(false);
	private String m_oldcadjpriceno=null;
	/** �Ƿ�������۸���Ϣ(���ۻ�����ۿ�) */
	protected boolean isAdjustBase=true;
	/** �Ƿ����׼�ۿۣ�ֻ�е��������۸���Ϣʱ�����Բ������� */
	protected boolean isAdjustDiscount = false;
	private InfectionCorp dlgAffetCorp=null;
	private AdjustPrmAnalysis dlgAnalyse=null;
	private PrmtariffHeadVO m_curtariffvo=null;
	private String m_localcurrid=null;

	private DMDataVO[] affectvos;

//	private boolean bInMsgPanel=false;//�жϵ�ǰ�Ƿ�����Ϣ�Ի�����
	//��Ҫ���÷���־�Ĳ����б�
	private ArrayList m_arselreflist=new ArrayList();
	//ԭ����ʾ����־, 1-��ʾ,0-����ʾ
	private int m_lastselrefflag=-1;

	private UFBoolean m_isExistAuditFlow=null;
	private String m_defbusitype="KHHH0000000000000001";

	private Hashtable<String, PricetypeVO> hsPricetypeVosBR=new Hashtable<String, PricetypeVO>();
	private OptionChoseUI m_dlgQuick=null;
	
//	���ؼ۱���������޹�
	private UFBoolean bAdjSpecMustNRelateBase=null;
	//
	private String SA13=null;
	private String BD101=null;
	private String BD301=null;
	private Integer BD505=null;
	private UFBoolean SA38=null;
	private BillTempletVO mbilltempvo=null;
	private UniBillFillSrv unisrv=new UniBillFillSrv();
	private ShowMsgDlg msgdlg = null;
	private String sendman = null;
	protected boolean isEditCust=false;
	/**
	 * �޸ģ�������
	 * ���ڣ�2009-9-3
	 * CQ�ţ�
	 * ԭ����ǰ̨���浱ǰѡ���еı���VO,�����ظ��غ�̨����
	 */
	private AdjustpriceItemVO[] adjitemVOs;
/**
 * AdjustPriceUI ������ע�⡣
 */
public AdjustPriceUI() {
	super();
	initialize();
}

/**
 * �˴����뷽��˵����
 * �������ڣ�(2002-12-12 13:18:55)
 * @return boolean
 * @param iRow int
 */
public boolean addNullLine(int iRow) {
	int rowCount = getBillCardPanel().getRowCount();
	if (iRow == (rowCount - 1) ) {
		onAddLine();
	}
	return false;
}
/**
 * �༭����
 * �������ڣ�(2001-4-26 14:36:55)
 * @param e nc.ui.pub.bill.BillEditEvent
 */
public void afterEdit(nc.ui.pub.bill.BillEditEvent e) {
	try{
		if (e.getPos() == BillItem.HEAD) {
			if(e.getKey().startsWith("vdef")){
				DefSetTool.afterEditHead(this.getBillCardPanel().getBillData(),e.getKey(),e.getKey().replaceAll("vdef","pk_defdoc"));
			}
		}
		else if (e.getPos() == BillItem.BODY) {
			if(e.getKey().startsWith("vdef")){
				DefSetTool.afterEditBody(this.getBillCardPanel().getBillModel(),e.getRow(),e.getKey(),e.getKey().replaceAll("vdef","pk_defdoc"));
			}
		}

		if ("code".equals(e.getKey())){
			if (afterInventoryMutiEdit(e))
				return;
		}
		// ��Ŀ��
		if ("cpricetariffid".equals(e.getKey())){
			/*
			 * �޸ģ���ǿ��
			 * ���ڣ�2008-8-14
			 * ԭ�򣺵��ۿ�ʱ��ѡ��ļ�Ŀ���������ۿ۱��壬���������
			 */
			if (isAdjustDiscount) {
				String cpricetariffid = (String) getBillCardPanel().getHeadItem("cpricetariffid").getValueObject();
				if (!isEmpty(cpricetariffid)) {
					String cond = "cpricetariffid = '" + cpricetariffid + "' and dr = 0";
					if (DiscountBOClient.queryBycondition(cond).length == 0) {
						showErrorMessage(NCLangRes.getInstance().getStrByID("403003", "UPT403003-000041"));//"ѡ��ļ�Ŀ��û�ж����ۿ۱����ܵ��ۿ�");
						getBillCardPanel().getHeadItem("cpricetariffid").setValue(null);
					}
				}
			}
			//����������ѯ��,����
			for(int i=0;i<getBillCardPanel().getRowCount();i++){
				findPriceBase("code",i);
				autoCalcBase("code",i);
			}
			String[] fms={"cpricetariffname->getColValue(prm_tariff,cpricetariffname,cpricetariffid,cpricetariffid)"};
			getBillCardPanel().execHeadFormulas(fms);
			getBillCardPanel().updateUI();
		}
		if("sourceprice".equals(e.getKey()) || "code".equals(e.getKey())){
			Object pricefield = getBillCardPanel().getBillModel().getValueAt(e.getRow(),"cpricefield");
			Object cinventoryid = getBillCardPanel().getBillModel().getValueAt(e.getRow(),"cinventoryid");
			if(cinventoryid != null && pricefield != null && isAdjustBase){
				getBillCardPanel().getBillModel().setCellEditable(e.getRow(),"npricefactor",true);
				getBillCardPanel().getBillModel().setCellEditable(e.getRow(),"npriceadd",true);
			}else if (isAdjustBase){
				getBillCardPanel().getBillModel().setValueAt(null,e.getRow(),"nbaseprice");
				getBillCardPanel().getBillModel().setValueAt(null,e.getRow(),"nnewprice");
			}
			if("code".equals(e.getKey())){
				addNullLine(e.getRow());
			}
		}
		if("cadjpriceno".equals(e.getKey())){
			m_bcodechanged=new Boolean(true);
		}

		if("sourceprice".equals(e.getKey())){
			//String[] fmls=new String[1];
			//fmls[0]="ccurrencyname->getColValue( bd_currtype , currtypename , pk_currtype , ccurrencyid)";
			//getBillCardPanel().getBillModel().execFormula(e.getRow(),fmls);
			getBillCardPanel().execBodyFormulas(e.getRow(),
				getBillCardPanel().getBodyItem("csrcpriceid").getLoadFormula());
		}


		//���ؼ�
		if (!isAdjustBase){
			isEditCust=false;
			boolean b = (e.getValue() == null || e.getValue().equals(""));
			//�����
			if (e.getKey().equals("cinvclassname")){
				getBillCardPanel().getBillModel().setValueAt(null,e.getRow(),"cinventoryid");
				getBillCardPanel().getBillModel().setValueAt(null,e.getRow(),"cinvbasdocid");
				getBillCardPanel().getBillModel().setValueAt("",e.getRow(),"code");
				getBillCardPanel().getBillModel().setValueAt("",e.getRow(),"name");
				getBillCardPanel().getBillModel().setValueAt("",e.getRow(),"size");
				getBillCardPanel().getBillModel().setValueAt("",e.getRow(),"type");
				//getBillCardPanel().getBillModel().setValueAt("",e.getRow(),"cunitid");
				//getBillCardPanel().getBillModel().setValueAt("",e.getRow(),"unit");
				getBillCardPanel().getBillModel().setCellEditable(e.getRow(),"code",b);
			}
			//�������
			else if (e.getKey().equals("code")){
				getBillCardPanel().getBillModel().setValueAt("",e.getRow(),"cinvclassname");
				getBillCardPanel().getBillModel().setValueAt(null,e.getRow(),"cinvclassid");
				getBillCardPanel().getBillModel().setCellEditable(e.getRow(),"cinvclassname",b);
			}
			//�ͻ���
			else if (e.getKey().equals("ccustclassname")){
				isEditCust=true;
				getBillCardPanel().getBillModel().setValueAt("",e.getRow(),"ccustomername");
				getBillCardPanel().getBillModel().setValueAt(null,e.getRow(),"ccustomerid");
				getBillCardPanel().getBillModel().setCellEditable(e.getRow(),"ccustomername",b);
			}
			//�ͻ�
			else if (e.getKey().equals("ccustomername")){
				isEditCust=true;
				getBillCardPanel().getBillModel().setValueAt("",e.getRow(),"ccustclassname");
				getBillCardPanel().getBillModel().setValueAt(null,e.getRow(),"ccustclassid");
				getBillCardPanel().getBillModel().setCellEditable(e.getRow(),"ccustclassname",b);
			}else if(e.getKey().equals("dvalidate")){
				String dvalidate=getBillCardPanel().getHeadItem("dvalidate").getValue();
				if (dvalidate.length()==0){
					showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("scmsoprice","UPPscmsoprice-000273")/*@res "��ʼʱ�䲻��Ϊ�գ�"*/);
					return;
				}

			} else if(e.getKey().equals("dexpiredate")){
				String dexpiredate=getBillCardPanel().getHeadItem("dexpiredate").getValue();
				if (dexpiredate.length()==0){
					showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("scmsoprice","UPPscmsoprice-000272")/*@res "��ֹʱ�䲻��Ϊ�գ�"*/);
					return;
				}
			}
//			������޹�,�޸Ŀͻ�/�ͻ��������Ҫ����ѯ�ۣ�����ѯ�۽�����¼۸��ԭ�۸��¼۸񲻱䣻
//			���û��ѯ���ۣ��۸���ֲ��䣻ԭ�۸���գ��¼۸񲻱䡣

			//ѯ�۸���
			Boolean brelatebase = (Boolean) getBillCardPanel().getBodyValueAt(e.getRow(), "brelatebase");
			if (!((brelatebase!=null&&!brelatebase.booleanValue()&&(e.getKey().equals("ccustclassname")||e.getKey().equals("ccustomername")))||e.getKey().equals("npriceadd") || e.getKey().equals("npricefactor") || e.getKey().equals("sourceprice") || e.getKey().equals("nnewprice"))){
				getBillCardPanel().setBodyValueAt(null, e.getRow(), "csrcpriceid");
				getBillCardPanel().execBodyFormulas(e.getRow(), getBillCardPanel().getBodyItem("csrcpriceid").getLoadFormula());

			}
			//ѯ��
			findPrice(e.getKey(),e.getRow());

			//���ø��еı༭״̬
			if (e.getKey().equals("unit") || e.getKey().equals("cinvclassname") || e.getKey().equals("code") || e.getKey().equals("brelatebase"))
				setRowEditCol(e.getRow());
			//�����Ƿ������������ó�ֵ
			if ( e.getKey().equals("brelatebase")){
				if (((Boolean)e.getValue()).booleanValue()){
					getBillCardPanel().getBillModel().setValueAt(new UFDouble(1),e.getRow(),"npricefactor");
					getBillCardPanel().getBillModel().setValueAt(new UFDouble(0),e.getRow(),"npriceadd");
				}
				else{
					getBillCardPanel().getBillModel().setValueAt(null,e.getRow(),"npricefactor");
					getBillCardPanel().getBillModel().setValueAt(null,e.getRow(),"npriceadd");
				}
			}
			//�޸ı�ͷ��Ч����,ʧЧ�����Զ��޸ı���
			//��Ҫ����ѯ��
			//
			if (e.getKey().equals("dvalidate")){
				if (e.getValue()!=null)
					for (int i=0;i<getBillCardPanel().getBillModel().getRowCount();i++){
						if (i==e.getRow()) continue;
						findPrice(e.getKey(),i);
						autoCalc(e.getKey(),i);
					}
			}

			//�Զ�����
			autoCalc(e.getKey(),e.getRow());
			getBillCardPanel().updateUI();
		}
		else{
			boolean b = (e.getValue() == null || e.getValue().equals(""));
			//�����
			if (e.getKey().equals("cinvclassname")){
				getBillCardPanel().getBillModel().setValueAt(null,e.getRow(),"cinventoryid");
				getBillCardPanel().getBillModel().setValueAt(null,e.getRow(),"cinvbasdocid");
				getBillCardPanel().getBillModel().setValueAt("",e.getRow(),"code");
				getBillCardPanel().getBillModel().setValueAt("",e.getRow(),"name");
				getBillCardPanel().getBillModel().setValueAt("",e.getRow(),"size");
				getBillCardPanel().getBillModel().setValueAt("",e.getRow(),"type");
				//getBillCardPanel().getBillModel().setValueAt("",e.getRow(),"cunitid");
				//getBillCardPanel().getBillModel().setValueAt("",e.getRow(),"unit");
				getBillCardPanel().getBillModel().setCellEditable(e.getRow(),"code",b);
			}
			//�������
			else if (e.getKey().equals("code")){
				getBillCardPanel().getBillModel().setValueAt("",e.getRow(),"cinvclassname");
				getBillCardPanel().getBillModel().setValueAt(null,e.getRow(),"cinvclassid");
				getBillCardPanel().getBillModel().setCellEditable(e.getRow(),"cinvclassname",b);
			}
			//�ͻ���
			if (e.getKey().equals("ccustclassname")){
				getBillCardPanel().getBillModel().setValueAt("",e.getRow(),"ccustomername");
				getBillCardPanel().getBillModel().setValueAt(null,e.getRow(),"ccustomerid");
				//getBillCardPanel().getBillModel().setCellEditable(e.getRow(),"ccustomername",b);
			}
			//�ͻ�
			else if (e.getKey().equals("ccustomername")){
				getBillCardPanel().getBillModel().setValueAt("",e.getRow(),"ccustclassname");
				getBillCardPanel().getBillModel().setValueAt(null,e.getRow(),"ccustclassid");
				//getBillCardPanel().getBillModel().setCellEditable(e.getRow(),"ccustclassname",b);
			}
			//ѯ��
			findPriceBase(e.getKey(),e.getRow());

			//�Զ�����
			autoCalcBase(e.getKey(),e.getRow());
			getBillCardPanel().updateUI();
		}

		if (e.getKey().equals("ccustclassname") || e.getKey().equals("ccustomername")
			|| e.getKey().equals("cinvclassname") || e.getKey().equals("code") || e.getKey().equals("unit") ){
			setRowCustEdit(e.getRow());
		}

	}catch(Exception evt){
		SCMEnv.error(evt);
		showErrorMessage(evt.getMessage());
	}
}
/**
 * �༭����
 * �������ڣ�(2001-4-26 14:36:55)
 * @param e nc.ui.pub.bill.BillEditEvent
 */
public boolean afterInventoryMutiEdit(nc.ui.pub.bill.BillEditEvent e) {
	//��ȡ��ǰ��
	int row=e.getRow();
	UIRefPane invRef =
		(UIRefPane) getBillCardPanel().getBodyItem("code").getComponent();
	String[] refPks = invRef.getRefPKs();
	String[] refCodes = invRef.getRefCodes();
	String[] refNames = invRef.getRefNames();


	if(refPks==null || refPks.length<=1 ){
		return false;
	}
	//���һ�в������ѡ
	if(getBillCardPanel().getRowCount()!=row+1){
		showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("scmsoprice","UPPSCMSOPRICE-000179")/*@res "���һ�в������ѡ!"*/);
		return false;
	}

	/**
	 * �޸ģ�������
	 * ���ڣ�2009-9-4
	 * CQ�ţ�NCdp200991312
	 * ԭ��������Щ����ռ���˺ܶ������������ǲ��ҳ����Ĵ����Ϣ�ں��沢û��ʹ��
	 */
	
	//������ȡ�����Ϣ
//	InvVO[] invvos=new InvVO[refPks.length];
//	for(int i = 0; i < invvos.length; i++){
//		invvos[i]=new InvVO();
//		invvos[i].setCinventoryid(refPks[i]);
//	}
//
//	InvoInfoBYFormula invvosget =new InvoInfoBYFormula();
//	invvos=invvosget.getQuryInvVOs(invvos,false,true,1);
	/********������Щ����ռ���˺ܶ������������ǲ��ҳ����Ĵ����Ϣ�ں��沢û��ʹ��******************/

	//���Ƽ�¼
	int oldrowcount=getBillCardPanel().getRowCount();
	for(int i=1;i<refPks.length;i++){
		//onAddLine();
		getBillCardPanel().addLine();
	}
	try{
		int currow=0;
		int[] newRows=new int[refPks.length];
		BillModel bm=getBillCardPanel().getBillModel();
		//��������
		for(int i=0;i<refPks.length;i++){
			currow=row+i;
			newRows[i]=currow;
			//���ƴ������
			getBillCardPanel().getBillModel().setValueAt(refPks[i],currow,"cinventoryid");
			if ("0001".equals(getCorpPrimaryKey()))
				getBillCardPanel().getBillModel().setValueAt(refPks[i],currow,"cinvbasdocid");
			getBillCardPanel().getBillModel().setValueAt(refCodes[i],currow,"code");
			//getBillCardPanel().execBodyFormula(currow,"code");
			if (i>0){
				//�����Ƶ�λ��ԭ��(�����ͬ,������λ���ܲ�ͬ)
				bm.setValueAt(bm.getValueAt(row,"csrcpriceid"),currow,"csrcpriceid");
				bm.setValueAt(bm.getValueAt(row,"ccustclassid"),currow,"ccustclassid");
				bm.setValueAt(bm.getValueAt(row,"ccustclassname"),currow,"ccustclassname");
				bm.setValueAt(bm.getValueAt(row,"ccustomerid"),currow,"ccustomerid");
				bm.setValueAt(bm.getValueAt(row,"ccustomername"),currow,"ccustomername");
				bm.setValueAt(bm.getValueAt(row,"ccustbasid"),currow,"ccustbasid");
				bm.setValueAt(bm.getValueAt(row,"ccurrencyid"),currow,"ccurrencyid");
				bm.setValueAt(bm.getValueAt(row,"ccurrencyname"),currow,"ccurrencyname");
				bm.setValueAt(bm.getValueAt(row,"csaleorganid"),currow,"csaleorganid");
				bm.setValueAt(bm.getValueAt(row,"csaleorganname"),currow,"csaleorganname");
				bm.setValueAt(bm.getValueAt(row,"creceiptareaid"),currow,"creceiptareaid");
				bm.setValueAt(bm.getValueAt(row,"creceiptareaname"),currow,"creceiptareaname");
				bm.setValueAt(bm.getValueAt(row,"sourceprice"),currow,"sourceprice");
				bm.setValueAt(bm.getValueAt(row,"npricefactor"),currow,"npricefactor");
				bm.setValueAt(bm.getValueAt(row,"npriceadd"),currow,"npriceadd");
				bm.setValueAt(bm.getValueAt(row,"cpricefield"),currow,"cpricefield");
				bm.setValueAt(bm.getValueAt(row,"brelatebase"),currow,"brelatebase");
			}
			

			if (!isAdjustBase){
				getBillCardPanel().getBillModel().setValueAt("",currow,"cinvclassname");
				getBillCardPanel().getBillModel().setValueAt(null,currow,"cinvclassid");
				getBillCardPanel().getBillModel().setCellEditable(currow,"cinvclassname",false);

			}
		}
		if ("0001".equals(getCorpPrimaryKey())){
			/**
			 * �޸ģ�������
			 * ���ڣ�2009-9-4
			 * CQ�ţ�NCdp200991312
			 * ԭ��һ��������ִ�й�ʽ
			 */
			
			String[] formulas = {"name->getColValue(bd_invbasdoc,invname,pk_invbasdoc,cinvbasdocid)",
					             "size->getColValue(bd_invbasdoc,invspec,pk_invbasdoc,cinvbasdocid)",
					             "type->getColValue(bd_invbasdoc,invtype,pk_invbasdoc,cinvbasdocid)",
					             "cunitid->getColValue(bd_invbasdoc,pk_measdoc,pk_invbasdoc,cinvbasdocid)",
					             "unit->getColValue(bd_measdoc,measname,pk_measdoc,cunitid)",
					             "height->getColValue(bd_invbasdoc,height,pk_invbasdoc,cinvbasdocid);",  //wanglei 2014-05-12 ���ӹ�ʽ
					             "graphid->getColValue(bd_invbasdoc,graphid,pk_invbasdoc,cinvbasdocid);"};
			getBillCardPanel().getBillModel().execFormulas(formulas, newRows[0], newRows[newRows.length - 1]);
//			ClientCacheHelper.getColValueBatch(getBillCardPanel(),newRows,
//				new String[]{"name","size","type"},"bd_invbasdoc","pk_invbasdoc",new String[]{"invname","invspec","invtype"},"cinventoryid");
			/***********һ��������ִ�й�ʽ**************/
		}
		else{
			/**
			 * �޸ģ�������
			 * ���ڣ�2009-9-4
			 * CQ�ţ�NCdp200991312
			 * ԭ��һ��������ִ�й�ʽ
			 */
			String[] formulas = {"cinvbasdocid->getColValue(bd_invmandoc,pk_invbasdoc,pk_invmandoc,cinventoryid)",
					             "name->getColValue(bd_invbasdoc,invname,pk_invbasdoc,cinvbasdocid)",
		                         "size->getColValue(bd_invbasdoc,invspec,pk_invbasdoc,cinvbasdocid)",
		                         "type->getColValue(bd_invbasdoc,invtype,pk_invbasdoc,cinvbasdocid)",
		                         "cunitid->getColValue(bd_invbasdoc,pk_measdoc,pk_invbasdoc,cinvbasdocid)",
					             "unit->getColValue(bd_measdoc,measname,pk_measdoc,cunitid)",
					             "height->getColValue(bd_invbasdoc,height,pk_invbasdoc,cinvbasdocid);",  //wanglei 2014-05-12 ���ӹ�ʽ
					             "graphid->getColValue(bd_invbasdoc,graphid,pk_invbasdoc,cinvbasdocid);"};
			getBillCardPanel().getBillModel().execFormulas(formulas, newRows[0], newRows[newRows.length - 1]);
			/***********һ��������ִ�й�ʽ**************/
//			ClientCacheHelper.getColValueBatch(getBillCardPanel(),newRows,
//					new String[]{"cinvbasdocid"},"bd_invmandoc","pk_invmandoc",new String[]{"pk_invbasdoc"},"cinventoryid");
//			ClientCacheHelper.getColValueBatch(getBillCardPanel(),newRows,
//					new String[]{"name","size","type"},"bd_invbasdoc","pk_invbasdoc",new String[]{"invname","invspec","invtype"},"cinvbasdocid");
		}
		/**
		 * �޸ģ�������
		 * ���ڣ�2009-9-4
		 * CQ�ţ�NCdp200991312
		 * ԭ��һ��������ִ�й�ʽ
		 */
//		ClientCacheHelper.getColValueBatch(getBillCardPanel(),newRows,
//				new String[]{"cunitid"},"bd_invbasdoc","pk_invbasdoc",new String[]{"pk_measdoc"},"cinvbasdocid");
//		ClientCacheHelper.getColValueBatch(getBillCardPanel(),newRows,
//				new String[]{"unit"},"bd_measdoc","pk_measdoc",new String[]{"measname"},"cunitid");
		/***********һ��������ִ�й�ʽ**************/
		

		//����ѯ��
		if (isAdjustBase){
			findPriceBaseBatch(newRows);

		}
		else{
			Boolean isRelatebase=(Boolean)getBillCardPanel().getBodyValueAt(row,"brelatebase");
			if (isRelatebase.booleanValue()){
				findSPBasePriceBatch(newRows);
			}
			else
				findOranginPriceBatch(newRows);
		}
		//��������״̬
		for(int i=0;i<refPks.length;i++){			
			currow=row+i;
			if (!isAdjustBase){
				//ѯ��
				//findPrice(e.getKey(),currow);

				//���ø��еı༭״̬
				setRowEditCol(currow);
				//���û����еı༭״̬
				setRowCustEdit(currow);
				/**
				 * �޸ģ�������
				 * ���ڣ�2009-9-1
				 * CQ�ţ�
				 * ԭ��ѭ��ִ�й�ʽ���������ƫ�࣬�������еĹ�ʽ����һ��ִ�У�����
				 *       ������
				 */
				//�Զ�����
//				autoCalc(e.getKey(),currow);
			}
			else{
				//ѯ��
				//findPriceBase(e.getKey(),currow);
				//���û����еı༭״̬
				setRowCustEdit(currow);
				/**
				 * �޸ģ�������
				 * ���ڣ�2009-9-1
				 * CQ�ţ�
				 * ԭ��ѭ��ִ�й�ʽ���������ƫ�࣬�������еĹ�ʽ����һ��ִ�У�����
				 *       ������
				 */
				
				//�Զ�����
//				autoCalcBase(e.getKey(),currow);
			}
		}
		/**
		 * �޸ģ�������
		 * ���ڣ�2009-9-1
		 * CQ�ţ�
		 * ԭ�򣺰������еĹ�ʽ����һ��ִ�У�����
		 *       ������
		 */
		
		if (!isAdjustBase){
			Boolean brelatebase=(Boolean)getBillCardPanel().getBodyValueAt(row,"brelatebase");
			if (brelatebase.booleanValue()){
				String[] formulas = {"nnewprice->npriceadd+nbaseprice*npricefactor*nbasediscount/100"};
				getBillCardPanel().getBillModel().execFormulas(formulas, row, row + refPks.length - 1);
			}
		}else {
			String[] formulas = {"nnewprice->npriceadd+nbaseprice*npricefactor"};
			getBillCardPanel().getBillModel().execFormulas(formulas, row, row + refPks.length - 1);
		}


	}catch(Exception evt){
		showErrorMessage(evt.getMessage());
		SCMEnv.out(evt);
	}
	getBillCardPanel().updateUI();
	return true;
}
/**
 * �������۵���
 * �������ڣ�(2001-07-23 9:36:49)
 * @param voAdjustprice nc.vo.sp.sp002.AdjustpriceVO
 */
public void audit(AdjustpriceVO voAdjustprice, boolean isAudit)
	throws Exception {
	//����������־
	voAdjustprice.setStatus(isAudit ? BillStatus.AUDIT : BillStatus.FREE);
	//����������
	/*((AdjustpriceHeaderVO) voAdjustprice.getParentVO()).setCauditorid(
		isAudit ? ce.getUser().getPrimaryKey() : null);
	*/
	((AdjustpriceHeaderVO) voAdjustprice.getParentVO()).setCauditorid(
		ce.getUser().getPrimaryKey());

	//������������
	((AdjustpriceHeaderVO) voAdjustprice.getParentVO()).setDauditdate(
		isAudit ? ce.getDate() : null);

	//AdjustpriceBO_Client.audit(voAdjustprice);
	//modify by zxj 20030820
	PfUtilClient.processAction(
		"SoUnApprove"+ce.getUser().getPrimaryKey(),
		getBillType(),
		getClientEnvironment().getDate().toString(),
		voAdjustprice);
}
/**
 * �Զ�����
 * @ version (00-6-6 13:33:25)
 */
public void autoCalc(String editcolname,int row) {
	if (row<0)
		return;

	//ֻ��Ӱ����۵��иı�����¼���
	if (editcolname.equals("code") || editcolname.equals("sourceprice") || editcolname.equals("brelatebase")
		|| editcolname.equals("ccustclassname") || editcolname.equals("ccustomername") || editcolname.equals("ccurrencyname")
		|| editcolname.equals("unit") || editcolname.equals("creceiptareaname") || editcolname.equals("csaleorganname")
		|| editcolname.equals("npricefactor") || editcolname.equals("npriceadd")
		|| editcolname.equals("nnewprice") || editcolname.equals("brelatebase") ){
		}
	else
		return;

	//
	Boolean brelatebase=(Boolean)getBillCardPanel().getBodyValueAt(row,"brelatebase");
	if (brelatebase.booleanValue()){
		if (editcolname.equals("nnewprice")	){
			/*
			 * �޸ģ���ǿ��
			 * ���ڣ�2008-9-10
			 * ԭ�򣺴�����ʽ���ǻ�׼�ۿ�
			 */
//			String[] formula = {"npriceadd->nnewprice-nbaseprice*npricefactor"};
			String[] formula = {"npriceadd->nnewprice-nbaseprice*npricefactor*nbasediscount/100"};
			getBillCardPanel().getBillModel().execFormula(row,formula);
		}
		else{
			/*
			 * �޸ģ���ǿ��
			 * ���ڣ�2008-9-10
			 * ԭ�򣺴�����ʽ���ǻ�׼�ۿ�
			 */
//			String[] formula = {"nnewprice->npriceadd+nbaseprice*npricefactor"};
			String[] formula = {"nnewprice->npriceadd+nbaseprice*npricefactor*nbasediscount/100"};
			getBillCardPanel().getBillModel().execFormula(row,formula);
		}
	}
}
/**
 * �Զ�����(������)
 * @ version (00-6-6 13:33:25)
 */
public void autoCalcBase(String editcolname,int row) {
	if (row<0)
		return;

	//ֻ��Ӱ����۵��иı�����¼���
	if (editcolname.equals("code") || editcolname.equals("sourceprice") || editcolname.equals("ccurrencyname")
		|| editcolname.equals("cpricetariffid") || editcolname.equals("unit") || editcolname.equals("ccustclassname")
		|| editcolname.equals("ccustomername") || editcolname.equals("creceiptareaname") || editcolname.equals("csaleorganname")
		|| editcolname.equals("npricefactor") || editcolname.equals("npriceadd")
		|| editcolname.equals("nnewprice") ){
		}
	else
		return;


	if (editcolname.equals("nnewprice")) {
		String[] formula = {"npriceadd->nnewprice-nbaseprice*npricefactor"};
		getBillCardPanel().getBillModel().execFormula(row,formula);
	} else {
		String[] formula = {"nnewprice->npriceadd+nbaseprice*npricefactor"};
		getBillCardPanel().getBillModel().execFormula(row,formula);
	}

}
/**
 * �༭ǰ����
 * �������ڣ�(2001-3-23 2:02:27)
 * @param e ufbill.BillEditEvent
 */
public boolean beforeEdit(nc.ui.pub.bill.BillEditEvent e) {
	if (e.getKey().equals("unit")){
		UIRefPane cpackunitname = (UIRefPane)getBillCardPanel().getBodyItem("unit").getComponent();
		String cinvbasdocid = (String)getBillCardPanel().getBodyValueAt(e.getRow(),"cinvbasdocid");
		if (cinvbasdocid==null)
			cpackunitname.setWhereString(null);
		else
		cpackunitname.setWhereString(
			"(pk_measdoc in (select pk_measdoc from bd_convert where pk_invbasdoc = '"
				+ cinvbasdocid.toString()
				+ "') or pk_measdoc in (select pk_measdoc from bd_invbasdoc where pk_invbasdoc= '"
				+ cinvbasdocid.toString()
				+ "') )");
	}
	if (e.getKey().equals("code") && !isAdjustBase && e.getRow()>=0){
		Boolean brelatebase=(Boolean)getBillCardPanel().getBodyValueAt(e.getRow(),"brelatebase");
		if (brelatebase.booleanValue()){
			if (!"0001".equals(getCorpPrimaryKey())){
				((UIRefPane)getBillCardPanel().getBodyItem("code").getComponent()).setWhereString(" bd_invmandoc.pk_corp='"+getCorpPrimaryKey()
					+"' and exists (select * from prm_tariff,prm_tariffcurlist where prm_tariff.cpricetariffid=prm_tariffcurlist.cpricetariffid and prm_tariffcurlist.cinventoryid=bd_invmandoc.pk_invmandoc and prm_tariff.pk_corp='"+getCorpPrimaryKey()+"')");
			}
			else{
				((UIRefPane)getBillCardPanel().getBodyItem("code").getComponent()).setWhereString(
					" exists (select * from prm_tariff,prm_tariffcurlist where prm_tariff.cpricetariffid=prm_tariffcurlist.cpricetariffid and prm_tariffcurlist.cinventoryid=bd_invbasdoc.pk_invbasdoc and prm_tariff.pk_corp='0001') ");
			}		
		}else{
			if (!"0001".equals(getCorpPrimaryKey())){
				((UIRefPane)getBillCardPanel().getBodyItem("code").getComponent()).setWhereString(" bd_invmandoc.pk_corp='"+getCorpPrimaryKey()
					+"' ");
			}
			else{
				((UIRefPane)getBillCardPanel().getBodyItem("code").getComponent()).setWhereString(null);
			}
		}
	}else if (e.getKey().equals("code") && isAdjustBase && e.getRow()>=0){
		if (!"0001".equals(getCorpPrimaryKey())){
			((UIRefPane)getBillCardPanel().getBodyItem("code").getComponent()).setWhereString(" bd_invmandoc.pk_corp='"+getCorpPrimaryKey()
				+"' ");
		}
		else{ 
			((UIRefPane)getBillCardPanel().getBodyItem("code").getComponent()).setWhereString(null);
		}
	}
	
	return true;
}
/**
 * ��ѡ�д���
 * �������ڣ�(2001-4-26 14:36:30)
 * @param e nc.ui.pub.bill.BillEditEvent
 */
public void bodyRowChange(nc.ui.pub.bill.BillEditEvent e) {
	if (e.getSource() == getBillListPanel().getHeadTable()) {
		iCurrRow = e.getRow();
		loadListBody(iCurrRow);
		setButton(iCurrRow);
		affectvos=null;
		updateUI();
	}
}
/**
 * �Ϸ��Լ�顣
 * �������ڣ�(2001-7-6 10:28:22)
 */
public void checkData(AdjustpriceVO vo) throws Exception {
	AdjustpriceHeaderVO header = (AdjustpriceHeaderVO) vo.getParentVO();
	AdjustpriceItemVO[] items = (AdjustpriceItemVO[]) vo.getChildrenVO();
	//��Ч����С�ڵ���ʧЧ����
//	if (header.getFadjtype() == AdjustpriceVO.BASE_PRICE || header.getFadjtype() == AdjustpriceVO.BASE_DISCOUNT){
		if (header.getDvalidate().before(ce.getDate())) {
			throw new Exception(nc.ui.ml.NCLangRes.getInstance().getStrByID("scmsoprice","UPPscmsoprice-000268")/*@res "��Ч���ڱ�����ڵ��ڷ�������ǰ����"*/);
		}
//	}
	if (header.getDexpiredate() != null) {
//		if (header.getDexpiredate().before(header.getDvalidate())) {
		if (!header.getStopDateTime().after(header.getStartDateTime())) {
			throw new Exception(NCLangRes.getInstance().getStrByID("scmsoprice","UPPscmsoprice-000269")/*@res "ʧЧʱ����������Чʱ��ͷ�������ǰʱ��"*/);
		}
	}
	String cpricetariffid=(String)getBillCardPanel().getHeadItem("cpricetariffid").getValueObject();
	if (cpricetariffid==null|| cpricetariffid .length()==0){
		/**
		 * �޸ģ�������
		 * ���ڣ�2009-8-6
		 * CQ�ţ�NCdp200929388
		 * ԭ��û�н��ж��﷭��
		 */
//		throw new Exception("��Ŀ����Ϊ��!");
		throw new Exception(NCLangRes4VoTransl.getNCLangRes().getStrByID("403003", "UPT403003-000053"));
	}
	boolean bHaveError=false;
	String strErrormsg="";

	//����Ϸ����ж�
	int iRows = getBillCardPanel().getRowCount();
	if (iRows <= 0) {
		throw new Exception(nc.ui.ml.NCLangRes.getInstance().getStrByID("scmsoprice","UPPscmsoprice-000270")/*@res "���岻��Ϊ�գ�"*/);
	} else {
		//������ʱ
		if (header.getFadjtype() == AdjustpriceVO.BASE_PRICE || header.getFadjtype() == AdjustpriceVO.BASE_DISCOUNT) {
			for (int i = 0; i < items.length; i++) {
				String inv = items[i].getCinventoryid();
				String invclass = (String) items[i].getAttributeValue("cinvclassid");
				String custtype = (String) items[i].getAttributeValue("ccustclassid");
				String cust = (String) items[i].getAttributeValue("ccustomerid");

				if ((invclass == null || invclass.length() == 0)
					&& (inv == null || inv.length() == 0) ) {
					bHaveError=true;
					strErrormsg+=nc.ui.ml.NCLangRes.getInstance().getStrByID("scmsoprice","UPPscmsoprice-000344",null,new String[]{String.valueOf((i+1))}) + "\n"/*@res "��{0}�У�����ࡢ�������ͬʱΪ�գ�"*/;
				}
				//�����в���ͬʱ��ֵ
				if (custtype != null && custtype.trim().length() > 0 && cust != null && cust.trim().length() > 0) {
					bHaveError=true;
					strErrormsg+=nc.ui.ml.NCLangRes.getInstance().getStrByID("scmsoprice","UPPscmsoprice-000345",null,new String[]{String.valueOf((i+1))}) + "\n"/*@res "��{0}�У��ͻ��ࡢ�ͻ�����ͬʱ��ֵ��"*/;
				}
				/*
				 * �޸ģ���ǿ��
				 * ���ڣ�2008-8-12
				 * ԭ��V55��ʼ��û��ƥ�䵽�ĵ�����ĿҲ���Ա��棬ִ��ʱ��������Ŀ����ۿ۱�
				 */
//				if (items[i].getStatus() == VOStatus.NEW || items[i].getStatus() == VOStatus.UPDATED) {
//					if (items[i].getBinqusucess() == null || !items[i].getBinqusucess().booleanValue()){
//						bHaveError=true;
//						strErrormsg+=nc.ui.ml.NCLangRes.getInstance().getStrByID("scmsoprice","UPPscmsoprice-000346",null,new String[]{String.valueOf((i+1))}) + "\n"/*@res "��{0}�У�û�гɹ�ѯ�ۣ����ܱ��棡"*/;
//					}
//				}
			}
		} 
		//��������ʱ
		else {
			for (int i = 0; i < items.length; i++) {
				UFBoolean brelatebase = (UFBoolean) items[i].getAttributeValue("brelatebase");
				if (brelatebase.booleanValue()) {
					double pricefactor = items[i].getNpricefactor().doubleValue();
					if (pricefactor <= 0.0) {
						bHaveError=true;
						strErrormsg+=nc.ui.ml.NCLangRes.getInstance().getStrByID("scmsoprice","UPPscmsoprice-000347",null,new String[]{String.valueOf((i+1))}) + "\n"/*@res "��{0}�У��۸�ָ����������㣡"*/;
					}
					//v5.02���ӵ���������������ʱ������ɹ�ѯ�� by zhangcheng///////////////////////
					if (items[i].getStatus() == VOStatus.NEW || items[i].getStatus() == VOStatus.UPDATED) {
						if (items[i].getBinqusucess() == null || !items[i].getBinqusucess().booleanValue()){
							bHaveError=true;
							strErrormsg+=nc.ui.ml.NCLangRes.getInstance().getStrByID("scmsoprice","UPPscmsoprice-000346",null,new String[]{String.valueOf((i+1))}) + "\n"/*@res "��{0}�У�û�гɹ�ѯ�ۣ����ܱ��棡"*/;
						}
					}
					//////////////////////////////////////////////////////////////////////////
				}
				if (bAdjSpecMustNRelateBase!=null && bAdjSpecMustNRelateBase.booleanValue() && brelatebase.booleanValue()){
					throw new Exception("���Ų������ƣ����ؼ۵��۵�����������޹أ�");
				}
				String inv = items[i].getCinventoryid();
				String invclass = (String) items[i].getAttributeValue("cinvclassid");
				String custtype = (String) items[i].getAttributeValue("ccustclassid");
				String cust = (String) items[i].getAttributeValue("ccustomerid");
				String csaleorganid = (String) items[i].getAttributeValue("csaleorganid");
				String creceiptareaid = (String) items[i].getAttributeValue("creceiptareaid");

				if ((invclass == null || invclass.length() == 0)
					&& (inv == null || inv.length() == 0)
					&& (custtype == null || custtype.length() == 0)
					&& (cust == null || cust.length() == 0)
					&& (csaleorganid == null || csaleorganid.length() == 0)
					&& (creceiptareaid == null || creceiptareaid.length() == 0)) {
					bHaveError=true;
					strErrormsg+=nc.ui.ml.NCLangRes.getInstance().getStrByID("scmsoprice","UPPscmsoprice-000348",null,new String[]{String.valueOf((i+1))}) + "\n"/*@res "��{0}�У�����ࡢ������ͻ��ࡢ�ͻ���������֯���ջ���������ͬʱΪ�գ�"*/;
				}
				//�����в���ͬʱ��ֵ
				if (invclass != null && invclass.trim().length() > 0 && inv != null && inv.trim().length() > 0) {
					bHaveError=true;
					strErrormsg+=nc.ui.ml.NCLangRes.getInstance().getStrByID("scmsoprice","UPPscmsoprice-000349",null,new String[]{String.valueOf((i+1))}) + "\n"/*@res "��{0}�У�����ࡢ�������ͬʱ��ֵ��"*/;
				}
				if (custtype != null && custtype.trim().length() > 0 && cust != null && cust.trim().length() > 0) {
					bHaveError=true;
					strErrormsg+=nc.ui.ml.NCLangRes.getInstance().getStrByID("scmsoprice","UPPscmsoprice-000345",null,new String[]{String.valueOf((i+1))}) + "\n"/*@res "��{0}�У��ͻ��ࡢ�ͻ�����ͬʱ��ֵ��"*/;
				}
				//if ((inv == null || inv.trim().length() == 0) && !(brelatebase.booleanValue())) {
					//throw new Exception("û��ָ�����������������أ�");
				//}
				if ((inv==null || inv.trim().length()==0) && (invclass==null || invclass.trim().length()==0) && !brelatebase.booleanValue()){
					bHaveError=true;
					strErrormsg+=nc.ui.ml.NCLangRes.getInstance().getStrByID("scmsoprice","UPPscmsoprice-000350",null,new String[]{String.valueOf((i+1))}) + "\n"/*@res "��{0}�У�û��ָ������ʹ����������������أ�"*/;
				}
			}
		}
	}
	if (bHaveError){
		throw new BusinessException(strErrormsg);
	}
	//���ŵ���ʱ������ǰ�жϵ��۵��Ƿ���䵽��˾��zhiwh��
	if ("0001".equals(getCorpPrimaryKey()) && !"���Ŷ���".equals(SA13) ) {
		String key = header.getCadjpriceid();
		if (affectvos == null && key != null) {
			//�޸�״̬ʱ��Ҫ��û�жԷ��乫˾�����޸�ʱ��Ҫ���²�ѯһ��
			affectvos = nc.ui.sp.sp002.AdjustpriceBO_Client.getAffectCorpByKey(key);
		}
		if (affectvos == null || affectvos.length == 0)
			throw new Exception(nc.ui.ml.NCLangRes.getInstance().getStrByID("scmsoprice","UPPscmsoprice-000271")/*@res "���۵�����ǰ�������Ӱ�칫˾�ķ���!"*/);
	}
	//��ʼʱ�䡢��ֹʱ��ͬʱΪ�ջ�ͬʱ�ǿ�
	if (header.getFadjpricemode().intValue() == 0)
		return;
	if (header.getDtstarttime() == null && header.getDtstoptime() == null)
		return;
	if (header.getDtstarttime() != null && header.getDtstoptime() == null)
		throw new Exception(nc.ui.ml.NCLangRes.getInstance().getStrByID("scmsoprice","UPPscmsoprice-000272")/*@res "��ֹʱ�䲻��Ϊ�գ�"*/);
	if (header.getDtstarttime() == null && header.getDtstoptime() != null)
		throw new Exception(nc.ui.ml.NCLangRes.getInstance().getStrByID("scmsoprice","UPPscmsoprice-000273")/*@res "��ʼʱ�䲻��Ϊ�գ�"*/);
}
/**
 * �Ϸ��Լ�顣
 * �������ڣ�(2001-7-6 10:28:22)
 */
public void checkHeaderData(AdjustpriceVO vo) throws Exception {
	//���ݺ�
	//AdjustpriceHeaderVO header = (AdjustpriceHeaderVO) vo.getParentVO();
	//if (header == null
		//|| header.getCadjpriceno() == null
		//|| header.getCadjpriceno().trim().length() == 0)
		//throw new Exception("���ݺŲ���Ϊ�գ�");
}
public void checkAuditData(AdjustpriceVO vo) throws Exception {
	//���ݺ�
	//AdjustpriceHeaderVO header = (AdjustpriceHeaderVO) vo.getParentVO();
	//if (header == null
		//|| header.getCadjpriceno() == null
		//|| header.getCadjpriceno().trim().length() == 0)
		//throw new Exception("���ݺŲ���Ϊ�գ�");
//	String time=((AdjustpriceHeaderVO) voAdjustprice.getParentVO()).getDbilltime().toString();
//	time=time.substring(0,time.indexOf(" "));
//	if (ce.getDate().before(new UFDate(time)))
//		throw new Exception(nc.ui.ml.NCLangRes.getInstance().getStrByID("scmsoprice","UPPscmsoprice-000794",null,new String[]{ce.getDate().toString(),time}));
}
/**
 * �����Ĺ��ܡ���;�������Եĸ��ģ��Լ�����ִ��ǰ������״̬��
 *  @param	         ����˵��
 *  @return	         ����ֵ
 *  @exception     �쳣����
 *  @see               ��Ҫ�μ�����������
 *  @since	         �������һ���汾���˷�������ӽ���������ѡ��
 *  @author            zhongyue
 * @return boolean
 * @param adjustpriceHeaders nc.vo.sp.sp002.AdjustpriceHeaderVO[]
 */
public boolean checkUnAudit(AdjustpriceVO adjustpricehvo) {
	if (adjustpricehvo == null)
		return false;
	else {
		try{
		if (AdjustpriceBO_Client.checkRefByOtherBill(adjustpricehvo).booleanValue()){
			showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("scmsoprice","UPPscmsoprice-000274")/*@res "�Ѿ���������������,��������!"*/);
			return false;
		}
		}catch(Exception e){
			SCMEnv.out(e.getMessage());
			showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON","UPPSCMCommon-000213",null,new String[]{e.getMessage()})/*@res "ȡ���������� {0}"*/);
			return false;
		}
		AdjustpriceHeaderVO header = (AdjustpriceHeaderVO) adjustpricehvo.getParentVO();
		AdjustpriceItemVO[] items = (AdjustpriceItemVO[]) adjustpricehvo.getChildrenVO();
		if (header != null && header.getDvalidate() != null && items != null) {
			if (checkUnAuditTime(adjustpricehvo) == true) { //���۵�������ʱ���ǵ��۵������д���ĵ��۵����������ʱ��
				return true;
			}
			if (header.getDvalidate().after(getClientEnvironment().getDate()) == true) {
				return true;
			}
			else {
				for (int i = 0; i < items.length; i++) {
					if (items[i] != null) {
						if (items[i].getNnewprice().equals(items[i].getNoriginalprice()) == false) {
							showOkCancelMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("scmsoprice","UPPscmsoprice-000275")/*@res "�ѵ���Чʱ�䲢�ҵ��۵��еĵ�����۸����������еļ۸�ͬ���ҵ��۵��ĵ�ǰ����ʱ�䲻�ǵ��۵������д���ĵ��۵����������ʱ�䣬��������"*/);
							return false;
						}
					}
				}
				//����ʱ�ѵ���Чʱ�䵫�ǵ��۵��еĵ�����۸����������еļ۸���ͬ����������
				return true;
			}
		}
		else { //������Ϊ��if (header != null && header.getDvalidate() != null && items != null)
			return false;
		}
	}
}
/**
 * �޸ģ�������
 * ���ڣ�2009-9-8
 * CQ�ţ�NCdp200978809
 * ԭ��ר��Ϊ�޸��������޸ģ�������У�����ֻռһ��������
 */
private static final String AUDITTIME = "AuditTime";
private static final String CHECKREFBYOTHERBILL = "checkRefByOtherBill";
private boolean checkUnAudit2(AdjustpriceVO adjustpricehvo) {
	Hashtable<String,UFBoolean> retHT = new Hashtable<String,UFBoolean>();
	try{
		retHT = AdjustpriceBO_Client.checkUnAudit(adjustpricehvo, getClientEnvironment().getDate());
		if(retHT.get(CHECKREFBYOTHERBILL) != null && retHT.get(CHECKREFBYOTHERBILL).booleanValue()){
			showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("scmsoprice","UPPscmsoprice-000274")/*@res "�Ѿ���������������,��������!"*/);
			return false;
		}
		if(retHT.get(AUDITTIME) == null){
			return false;
		}else if (retHT.get(AUDITTIME).booleanValue() == false){
			showOkCancelMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("scmsoprice","UPPscmsoprice-000275")/*@res "�ѵ���Чʱ�䲢�ҵ��۵��еĵ�����۸����������еļ۸�ͬ���ҵ��۵��ĵ�ǰ����ʱ�䲻�ǵ��۵������д���ĵ��۵����������ʱ�䣬��������"*/);
			return false;
		}
	}catch(Exception e){
		showErrorMessage(e.getMessage());
		SCMEnv.out(e.getMessage());
		return false;
	}
	return true;
}
public boolean checkUnAuditTime(AdjustpriceVO adjustpricehvo) {
	try {
		nc.vo.pub.lang.UFDate today = getClientEnvironment().getDate();
		AdjustpriceItemVO[] items = (AdjustpriceItemVO[]) adjustpricehvo.getChildrenVO();
		StringBuffer whereStr = new StringBuffer("");
		whereStr.append(
			" prm_adjustprice.cadjpriceid in (select prm_adjustprice.cadjpriceid from prm_adjustprice");
		whereStr.append(
			" left outer join prm_adjustprice_b on prm_adjustprice_b.cadjpriceid= prm_adjustprice.cadjpriceid ");
		whereStr.append(" where prm_adjustprice_b.dr=0 and prm_adjustprice.dr=0 and ( 1=0 ");
		for (int i = 0; i < items.length; i++) {
			if (items[i] != null) {
				if (items[i].getCinventoryid() != null) {
					whereStr.append(" or prm_adjustprice_b.cinventoryid='");
					whereStr.append(items[i].getCinventoryid());
					whereStr.append("'");
				}
			}
		}
		whereStr.append(") )");
		AdjustpriceHeaderVO[] headers = AdjustpriceBO_Client.queryAllHeaders(whereStr.toString());
		for (int i = 0; i < headers.length; i++) {
			if (headers[i] != null) {
				if (headers[i].getDauditdate() != null) {
					if (headers[i].getDauditdate().after(today)) {
						return false;
					}
				}
			}
		}
	}
	catch (Exception e) {
		showErrorMessage(e.getMessage());
		SCMEnv.out(e.getMessage());
	}
	return true;
}
/**
 * �˴����뷽��˵����
 * �������ڣ�(2001-11-12 15:32:38)
 */
public void checkUnique(AdjustpriceVO adjvo) throws Exception {

	AdjustpriceHeaderVO vo=(AdjustpriceHeaderVO)adjvo.getParentVO();
	AdjustpriceItemVO[] voitems=(AdjustpriceItemVO[])adjvo.getChildrenVO();
//	if (vo.getFadjtype().intValue()==0)
//		return;
	if (voitems==null || voitems.length==0){
		adjvo=(AdjustpriceVO) getBillCardPanel().getBillValueVO("nc.vo.sp.sp002.AdjustpriceVO",
				"nc.vo.sp.sp002.AdjustpriceHeaderVO",
				"nc.vo.sp.sp002.AdjustpriceItemVO");
//		voitems=
	}else{
		for (int i=0;i<voitems.length;i++){
			voitems[i].setAffectcorp(vo.getPk_corp());
		}

		//����Ƿ�����ظ�
		if (voitems.length>0){
			for (int i=0;i<voitems.length;i++){
				String cinvclassid = (String)voitems[i].getAttributeValue("cinvclassid");
				if (cinvclassid==null || cinvclassid.trim().length()==0) cinvclassid="";
				String cinventoryid = (String)voitems[i].getCinventoryid();
				if (cinventoryid==null || cinventoryid.trim().length()==0) cinventoryid="";
				String ccurrencyid = (String)voitems[i].getCcurrencyid();
				if (ccurrencyid==null || ccurrencyid.trim().length()==0) ccurrencyid="";
				String cmeasdocid = (String)voitems[i].getAttributeValue("cmeasdocid");
				if (cmeasdocid==null || cmeasdocid.trim().length()==0) cmeasdocid="";

				String ccustclassid = (String)voitems[i].getAttributeValue("ccustclassid");
				if (ccustclassid==null || ccustclassid.trim().length()==0) ccustclassid="";
				String ccustomerid = (String)voitems[i].getAttributeValue("ccustomerid");
				if (ccustomerid==null || ccustomerid.trim().length()==0) ccustomerid="";
				String csaleorganid = (String)voitems[i].getAttributeValue("csaleorganid");
				if (csaleorganid==null || csaleorganid.trim().length()==0) csaleorganid="";
				String creceiptareaid = (String)voitems[i].getAttributeValue("creceiptareaid");
				if (creceiptareaid==null || creceiptareaid.trim().length()==0) creceiptareaid="";
				String csrcpriceid=(String)voitems[i].getCsrcpriceid();
				if (csrcpriceid==null || csrcpriceid.trim().length()==0) csrcpriceid="";

				for (int j=i+1;j<voitems.length;j++){
					String cinvclassid1 = (String)voitems[j].getAttributeValue("cinvclassid");
					if (cinvclassid1==null || cinvclassid1.trim().length()==0) cinvclassid1="";
					String cinventoryid1 = (String)voitems[j].getCinventoryid();
					if (cinventoryid1==null || cinventoryid1.trim().length()==0) cinventoryid1="";
					String ccurrencyid1 = (String)voitems[j].getCcurrencyid();
					if (ccurrencyid1==null || ccurrencyid1.trim().length()==0) ccurrencyid1="";
					String cmeasdocid1 = (String)voitems[j].getAttributeValue("cmeasdocid");
					if (cmeasdocid1==null || cmeasdocid1.trim().length()==0) cmeasdocid1="";

					String ccustclassid1 = (String)voitems[j].getAttributeValue("ccustclassid");
					if (ccustclassid1==null || ccustclassid1.trim().length()==0) ccustclassid1="";
					String ccustomerid1 = (String)voitems[j].getAttributeValue("ccustomerid");
					if (ccustomerid1==null || ccustomerid1.trim().length()==0) ccustomerid1="";
					String csaleorganid1 = (String)voitems[j].getAttributeValue("csaleorganid");
					if (csaleorganid1==null || csaleorganid1.trim().length()==0) csaleorganid1="";
					String creceiptareaid1 = (String)voitems[j].getAttributeValue("creceiptareaid");
					if (creceiptareaid1==null || creceiptareaid1.trim().length()==0) creceiptareaid1="";
					String csrcpriceid1=(String)voitems[j].getCsrcpriceid();
					if (csrcpriceid1==null || csrcpriceid1.trim().length()==0) csrcpriceid1="";
					if (vo.getFadjtype() == AdjustpriceVO.BASE_PRICE || vo.getFadjtype() == AdjustpriceVO.BASE_DISCOUNT){
						if (cinvclassid.equals(cinvclassid1) && cinventoryid.equals(cinventoryid1) && csrcpriceid.equals(csrcpriceid1)
							&& ccurrencyid.equals(ccurrencyid1) && cmeasdocid.equals(cmeasdocid1)
							&& ccustclassid.equals(ccustclassid1) && ccustomerid.equals(ccustomerid1)
							&& csaleorganid.equals(csaleorganid1) && creceiptareaid.equals(creceiptareaid1)){
								PrmtariffItemVO tariffitemvo=new PrmtariffItemVO();
								tariffitemvo.setCinvclassid(cinvclassid);
								tariffitemvo.setCinventoryid(cinventoryid);
								tariffitemvo.setCinvbasdocid(voitems[i].getCinvbasdocid());
								tariffitemvo.setCcustomerid(ccustomerid);
								tariffitemvo.setCcubasdocid((String)voitems[j].getAttributeValue("ccustbasid"));
								tariffitemvo.setCmeasdocid(cmeasdocid);
								tariffitemvo.setCcurrencyid(ccurrencyid);
								tariffitemvo.setCcustclassid(ccustclassid);
								tariffitemvo.setCsaleorganid(csaleorganid);
								tariffitemvo.setCreceiptareaid(creceiptareaid);

								String msg = TariffBO_Client.getMsg(tariffitemvo);
								throw new Exception(msg + nc.ui.ml.NCLangRes.getInstance().getStrByID("scmsoprice","UPPscmsoprice-000351")/*@res "�ļ�¼������ͬ�ļ�¼�����ܱ��棡"*/);
							}
					}
					else{
						if (cinvclassid.equals(cinvclassid1) && cinventoryid.equals(cinventoryid1)
							&& ccurrencyid.equals(ccurrencyid1) && cmeasdocid.equals(cmeasdocid1)
							&& ccustclassid.equals(ccustclassid1) && ccustomerid.equals(ccustomerid1)
							&& csaleorganid.equals(csaleorganid1) && creceiptareaid.equals(creceiptareaid1)){
								SpecialwarepriceVO spvo=new SpecialwarepriceVO();
								spvo.setPk_corp(getCorpPrimaryKey());
								spvo.setCinventorytypeid(cinvclassid);
								spvo.setCinventoryid(cinventoryid);
								spvo.setCcustomerid(ccustomerid);
								spvo.setcmeasdocid(cmeasdocid);
								spvo.setccurrencyid(ccurrencyid);
								spvo.setCcustomertype(ccustclassid);
								spvo.setcsaleorganid(csaleorganid);
								spvo.setcreceiptareaid(creceiptareaid);

								String msg = SpecialwarepriceBO_Client.getMsg(spvo);
								throw new Exception(msg + nc.ui.ml.NCLangRes.getInstance().getStrByID("scmsoprice","UPPscmsoprice-000351")/*@res "�ļ�¼������ͬ�ļ�¼�����ܱ��棡"*/);
						}
					}
				}

			}

		}
	}

	/**
	 * �޸ģ�������
	 * ���ڣ�2009-9-2
	 * CQ�ţ�NCdp200978799
	 * ԭ��Ψһ��У������͡����桱�������󡱵Ȳ����ϲ���һ�����ӣ�����������
	 */
	

	/**
	 * �޸ģ�������
	 * ���ڣ�2009-12-9
	 * CQ�ţ�NCdp201101859
	 * ԭ�����ϴ��޸���������ʱ���ǰ�У��Ψһ�Ժͱ������ŵ���̨һ��ִ�У�����������
	 *       �������޸ı����ʱ�򣬴�����̨��VO�ǲ���VO,����������޸�û���޸ı�������
	 *       ��ʱ�򣬴����̨��VO�ı����о��ǿգ��ں�̨У���ʱ���ֱ������У�飬���Ի����
	 *       ���Ѿ�������ͬ��Чʱ�䣬��ͬά�ȵļ�¼��ʱ�򣬱���Ҳ��У��ͨ����Ϊ�˽���������
	 *       ֻ������һ�κ�̨��������У��ͱ�������ν���ִ�С����԰�ԭ���޸ĵĴ��뻹ԭ  
	 */
	try{
		AdjustpriceBO_Client.beforeAuditCheck(adjvo);
	}catch (Exception e){
		SCMEnv.error(e);
		throw new Exception(e.getMessage(), e);
	}
}
/**
 * �˴����뷽��˵����
 * �������ڣ�(2002-12-12 13:18:55)
 * @return boolean
 * @param iRow int
 */
public void cleanNullLine() {

		int rowCount = getBillCardPanel().getRowCount();
		for (int i = rowCount - 1; i >= 0; i--) {
			String cinvclassid = (String)getBillCardPanel().getBodyValueAt(i, "cinvclassid");
			String cinventoryid = (String)getBillCardPanel().getBodyValueAt(i, "cinventoryid");
			String ccustclassid = (String)getBillCardPanel().getBodyValueAt(i, "ccustclassid");
			String ccustomerid = (String)getBillCardPanel().getBodyValueAt(i, "ccustomerid");
			String csaleorganid = (String)getBillCardPanel().getBodyValueAt(i, "csaleorganid");
			String creceiptareaid = (String)getBillCardPanel().getBodyValueAt(i, "creceiptareaid");

			if( (cinvclassid==null || cinvclassid.trim().length()==0) &&
			 (cinventoryid==null || cinventoryid.trim().length()==0) &&
			 (ccustclassid==null || ccustclassid.trim().length()==0) &&
			 (ccustomerid==null || ccustomerid.trim().length()==0) &&
			 (csaleorganid==null || csaleorganid.trim().length()==0) &&
			 (creceiptareaid==null || creceiptareaid.trim().length()==0) ){
				int[] rowIndex = { i };
				getBillCardPanel().getBillData().getBillModel().delLine(rowIndex);
			}
		}

}
/**
 * ѯ��
 * @ version (00-6-6 13:33:25)
 */
public void findPrice(String editcolname, int row)  {
	if (row < 0)
		return;
	String cinvbasdocid = (String) getBillCardPanel().getBodyValueAt(row, "cinvbasdocid");
	String cinvclassid=(String)getBillCardPanel().getBodyValueAt(row,"cinvclassid");
	if (cinvbasdocid==null && cinvclassid==null){
		return;
	}

	String csourcepriceid = (String) getBillCardPanel().getBodyValueAt(row, "csrcpriceid");
	if (csourcepriceid == null) {
		//zhiwh2004-08-20
		//����Ҫ����ؼ�ʱ��ѡ��ͻ�����֮���Զ�������Ŀ������Ӧ�ͻ���������ü۸���
		//return;
	}
	Boolean brelatebase = (Boolean) getBillCardPanel().getBodyValueAt(row, "brelatebase");
	if (brelatebase.booleanValue()) {
		if (editcolname.equals("code")
			|| editcolname.equals("sourceprice")
			|| editcolname.equals("brelatebase")
			|| editcolname.equals("cinvclassname")
			|| editcolname.equals("ccustclassname")
			|| editcolname.equals("ccustomername")
			|| editcolname.equals("ccurrencyname")
			|| editcolname.equals("unit")
			|| editcolname.equals("creceiptareaname")
			|| editcolname.equals("csaleorganname")) {
			//����
		} else
			return;
		//ѯ����
		try {
			UFDouble saleprice = findSPBasePrice(row);

			getBillCardPanel().getBillModel().setValueAt(saleprice, row, "nbaseprice");
			getBillCardPanel().getBillModel().setValueAt(null, row, "noriginalprice");
			if (saleprice!=null)
				getBillCardPanel().getBillModel().setValueAt(new UFBoolean(true),row,"binqusucess");
			else
			    getBillCardPanel().getBillModel().setValueAt(new UFBoolean(false),row,"binqusucess");
		} catch (Exception e) {
			getBillCardPanel().getBillModel().setValueAt(null, row, "nbaseprice");
			getBillCardPanel().getBillModel().setValueAt(null, row, "noriginalprice");
			getBillCardPanel().getBillModel().setValueAt(new UFBoolean(false),row,"binqusucess");
			SCMEnv.error(e);
			//showErrorMessage("ѯ����ʧ��!");
			return;
		}
	} else {
		if (editcolname.equals("code")
			|| editcolname.equals("sourceprice")
			|| editcolname.equals("brelatebase")
			|| editcolname.equals("cinvclassname")
			|| editcolname.equals("ccustclassname")
			|| editcolname.equals("ccustomername")
			|| editcolname.equals("ccurrencyname")
			|| editcolname.equals("unit")
			|| editcolname.equals("creceiptareaname")
			|| editcolname.equals("csaleorganname")) {
			//����
		} else
			return;
		//ѯԭ��
		SpecialwarepriceVO arySpecialwarepriceVO = new SpecialwarepriceVO();
		//arySpecialwarepriceVO = (SpecialwarepriceVO) getBillCardPanel().getBillModel().getBodyValueRowVO(row,SpecialwarepriceVO.class.getName());
		AdjustpriceItemVO item =
			(AdjustpriceItemVO) getBillCardPanel().getBillModel().getBodyValueRowVO(row, AdjustpriceItemVO.class.getName());
		arySpecialwarepriceVO.setPk_corp(getCorpPrimaryKey());
		arySpecialwarepriceVO.setCinventoryid(item.getCinventoryid());
		arySpecialwarepriceVO.setCsourcepriceid(item.getCsrcpriceid());
		arySpecialwarepriceVO.setCinventorytypeid((String) item.getAttributeValue("cinvclassid"));
		arySpecialwarepriceVO.setCcustomertype((String) item.getAttributeValue("ccustclassid"));
		arySpecialwarepriceVO.setCcustomerid((String) item.getAttributeValue("ccustomerid"));
		arySpecialwarepriceVO.setcsaleorganid((String) item.getAttributeValue("csaleorganid"));
		arySpecialwarepriceVO.setcreceiptareaid((String) item.getAttributeValue("creceiptareaid"));
		arySpecialwarepriceVO.setcmeasdocid((String) item.getAttributeValue("cmeasdocid"));
		arySpecialwarepriceVO.setccurrencyid((String) item.getAttributeValue("ccurrencyid"));
		String cpricetariffid=(String)getBillCardPanel().getHeadItem("cpricetariffid").getValue();
		arySpecialwarepriceVO.setCpricetariffid(cpricetariffid);
		String dvalidate=getBillCardPanel().getHeadItem("dvalidate").getValue();
		String dexpiredate=getBillCardPanel().getHeadItem("dexpiredate").getValue();
		arySpecialwarepriceVO.setDstartdate(new UFDate(dvalidate));
		arySpecialwarepriceVO.setDstopdate(new UFDate(dexpiredate));
		try {
			if (arySpecialwarepriceVO.getCsourcepriceid() == null) {
				String cPricetypeid =
					nc.ui.sp.pub.FindSalePriceBO_Client.getOrgSpecpriceItemFromsp(arySpecialwarepriceVO, ce.getDate());

				if (cPricetypeid == null) {
					getBillCardPanel().getBillModel().setValueAt(null, row, "nbaseprice");
					getBillCardPanel().getBillModel().setValueAt(null, row, "noriginalprice");
					if(isEditCust==false)
					getBillCardPanel().getBillModel().setValueAt(null, row, "nnewprice");
					return;
				}else{
					getBillCardPanel().setBodyValueAt(cPricetypeid, row, "csrcpriceid");
					getBillCardPanel().execBodyFormulas(row, getBillCardPanel().getBodyItem("csrcpriceid").getLoadFormula());
					arySpecialwarepriceVO.setCsourcepriceid(cPricetypeid);
				}
			}
		} catch (Exception e) {
			SCMEnv.error(e);
		}

		try {
			UFDouble saleprice = SpecialwarepriceBO_Client.getOrgSpecpriceFromsp(arySpecialwarepriceVO, ce.getDate());
			getBillCardPanel().getBillModel().setValueAt(null, row, "nbaseprice");
			getBillCardPanel().getBillModel().setValueAt(saleprice, row, "noriginalprice");
			if(isEditCust==false)
			getBillCardPanel().getBillModel().setValueAt(saleprice, row, "nnewprice");
		} catch (Exception e) {
			getBillCardPanel().getBillModel().setValueAt(null, row, "nbaseprice");
			getBillCardPanel().getBillModel().setValueAt(null, row, "noriginalprice");
			showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("scmsoprice","UPPscmsoprice-000276")/*@res "ѯԭ��ʧ��!"*/);
			return;
		}
	}

}
/**
 * ������ѯ��
 * @ version (00-6-6 13:33:25)
 */
public void findPriceBase(String editcolname,int row) {
	if (editcolname.equals("code") || editcolname.equals("sourceprice") || editcolname.equals("ccurrencyname") || editcolname.equals("cinvclassname")
		|| editcolname.equals("cpricetariffid") || editcolname.equals("unit") || editcolname.equals("ccustclassname")
		|| editcolname.equals("ccustomername") || editcolname.equals("creceiptareaname") || editcolname.equals("csaleorganname") ){
		//����
	}
	else
		return;

	if (row<0)
		return;
	String cinvbasdocid=(String)getBillCardPanel().getBodyValueAt(row,"cinvbasdocid");
	String cinvclassid=(String)getBillCardPanel().getBodyValueAt(row,"cinvclassid");
	if (cinvbasdocid==null && cinvclassid==null){
		return;
	}
	/*
	 * �޸ģ���ǿ��
	 * ���ڣ�2008-8-14
	 * ԭ�򣺵���׼�ۿ�ʱ�����Ҽ۸���
	 */
	if (!isAdjustDiscount) {
		String csourcepriceid=(String)getBillCardPanel().getBodyValueAt(row,"csrcpriceid");
		if (csourcepriceid==null){
			SalePriceVO salepriceVO = constructSalepriceVO(row);
			try {
				csourcepriceid =
					nc.ui.sp.pub.FindSalePriceBO_Client.findInventorySaleItem(salepriceVO);
				getBillCardPanel().setBodyValueAt(csourcepriceid, row, "csrcpriceid");
				if(csourcepriceid==null)return;
			} catch (Exception e) {
				SCMEnv.error(e);
	
			}
	
		}
	}
//	if ( editcolname.equals("sourceprice"))
		getBillCardPanel().execBodyFormulas(row,
			getBillCardPanel().getBodyItem("csrcpriceid").getLoadFormula());
	String ccurrencyid=(String)getBillCardPanel().getBodyValueAt(row,"ccurrencyid");
	if (ccurrencyid==null){
		return;
	}
	String cmeasdocid=(String)getBillCardPanel().getBodyValueAt(row,"cunitid");
	if (cmeasdocid==null){
		return;
	}

	//�ϳ�SQL���
	String cinventoryid=(String)getBillCardPanel().getBodyValueAt(row,"cinventoryid");
	String cpricetariffid=(String) getBillCardPanel().getHeadItem("cpricetariffid").getValueObject();
//	UFBoolean bCurrTariff=new UFBoolean(false);
	//if (m_curtariffvo==null)
//		initCurrtariff();
//	if (cpricetariffid!=null && cpricetariffid.trim().length()>0){
//		if (m_curtariffvo!=null && cpricetariffid.equals(m_curtariffvo.getCpricetariffid()))
//			bCurrTariff=new UFBoolean(true);
//	}
//	else{
//		bCurrTariff=new UFBoolean(true);
//		if (m_curtariffvo==null){
//			showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("scmsoprice","UPPscmsoprice-000277")/*@res "ѯ�۷�������û��ָ����Ŀ��ϵͳ��Ҳû�е�ǰ��Ŀ��.ѯ��ʧ�ܣ�"*/);
//			return;
//		}
//	}
	String csaleorganid=(String)getBillCardPanel().getBodyValueAt(row,"csaleorganid");
	String creceiptareaid=(String)getBillCardPanel().getBodyValueAt(row,"creceiptareaid");
	String ccustomerid=(String)getBillCardPanel().getBodyValueAt(row,"ccustomerid");
	String ccustclassid=(String)getBillCardPanel().getBodyValueAt(row,"ccustclassid");
	String cpricefield=(String)getBillCardPanel().getBodyValueAt(row,"cpricefield");

	String cond="";
	if (cinventoryid!=null)
		cond=" cinventoryid='"+cinventoryid+"' ";
	else
		cond=" cinvclassid='"+cinvclassid+"' ";

	cond+=" and ccurrencyid='"+ccurrencyid+"' ";
	cond+=" and cmeasdocid='"+cmeasdocid+"' ";
	if (cpricetariffid!=null && cpricetariffid.trim().length()>0)
		cond+=" and cpricetariffid='"+cpricetariffid+"' ";
	else
		return;
	if (csaleorganid!=null && csaleorganid.trim().length()>0)
		cond+=" and csaleorganid='"+csaleorganid+"' ";
	else
		cond+=" and csaleorganid ='"+NullStr.ID+"' ";
	if (creceiptareaid!=null && creceiptareaid.trim().length()>0)
		cond+=" and creceiptareaid='"+creceiptareaid+"' ";
	else
		cond+=" and creceiptareaid ='"+NullStr.ID+"' ";
	if (ccustomerid!=null && ccustomerid.trim().length()>0)
		cond+=" and ccustomerid='"+ccustomerid+"' ";
	else
		cond+=" and ccustomerid ='"+NullStr.ID+"' ";
	if (ccustclassid!=null && ccustclassid.trim().length()>0)
		cond+=" and ccustclassid='"+ccustclassid+"' ";
	else
		cond+=" and ccustclassid ='"+NullStr.ID+"' ";


	UFDouble ufbprice=null;
	try {
		/*
		 * �޸ģ���ǿ��
		 * ���ڣ�2008-7-31
		 * ԭ�򣺵���׼�ۿ�ʱѯ�ۿ�
		 */
		if (isAdjustDiscount) {
			// ѯ��׼�ۿ�
			DiscountlistVO[] vos = DiscountBOClient.queryDiscountListBatch(new String[]{cond});
			if (null != vos[0] && vos.length > 0) {
				ufbprice = vos[0].getBasediscount();
			}
		} else {
			// ѯ����
			PrmtariffItemVO[] vos = TariffBO_Client.queryTariffList(cond);
			if (vos != null && vos.length > 0) {
				ufbprice = (UFDouble) vos[0].getAttributeValue(cpricefield);
			}
		}
	} catch(Exception e) {
		getBillCardPanel().getBillModel().setValueAt(new UFDouble(0),row,"nbaseprice");
		getBillCardPanel().getBillModel().setValueAt(new UFDouble(0),row,"nnewprice");
		getBillCardPanel().getBillModel().setValueAt(new UFDouble(1),row,"npricefactor");
		getBillCardPanel().getBillModel().setValueAt(new UFDouble(0),row,"npriceadd");
		getBillCardPanel().getBillModel().setValueAt(new UFBoolean(false),row,"binqusucess");
		SCMEnv.error(e);
		showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("scmsoprice","UPPscmsoprice-000276")/*@res "ѯԭ��ʧ��!"*/);
		return;
	}
	if(isAdjustDiscount){
		getBillCardPanel().getBillData().getBodyItem("nbaseprice").setDecimalDigits(6);
		getBillCardPanel().getBillData().getBodyItem("nbasediscount").setDecimalDigits(6);
		getBillCardPanel().getBillData().getBodyItem("nnewprice").setDecimalDigits(6);
		
	}else{
		getBillCardPanel().getBillData().getBodyItem("nnewprice").setDecimalDigits(pricedigit);
		getBillCardPanel().getBillData().getBodyItem("nbaseprice").setDecimalDigits(pricedigit);
	}
	if (ufbprice != null && ufbprice.doubleValue() > 0) {
		getBillCardPanel().getBillModel().setValueAt(ufbprice,row,"nbaseprice");
		getBillCardPanel().setCellEditable(row,"nnewprice",true);
		getBillCardPanel().setCellEditable(row,"npricefactor",true);
		getBillCardPanel().setCellEditable(row,"npriceadd",true);
		getBillCardPanel().getBillModel().setValueAt(UFBoolean.TRUE,row,"binqusucess");
	} else {
		getBillCardPanel().getBillModel().setValueAt(new UFDouble(isAdjustDiscount ? 100 : 0),row,"nbaseprice");
		getBillCardPanel().getBillModel().setValueAt(UFBoolean.FALSE,row,"binqusucess");
	}
}
/**
 * ѯ��
 * @ version (00-6-6 13:33:25)
 */
public UFDouble findSPBasePrice(int row) {
	UFDouble saleprice = null;
	try {
		SalePriceVO salepriceVO = constructSalepriceVO(row);
		//zhiwh����
		if (salepriceVO.getPriceTypeid() == null) {
			String cPricetypeid = nc.ui.sp.pub.FindSalePriceBO_Client.findInventorySaleItem(salepriceVO);
			//if (cPricetypeid != null && salepriceVO.getPriceTypeid() == null){
			getBillCardPanel().setBodyValueAt(cPricetypeid, row, "csrcpriceid");
			getBillCardPanel().execBodyFormulas(row, getBillCardPanel().getBodyItem("csrcpriceid").getLoadFormula());
			salepriceVO.setPriceTypeid(cPricetypeid);
			//}
			if (salepriceVO.getPriceTypeid() == null) {
				getBillCardPanel().getBillModel().setValueAt(null, row, "nbaseprice");
				getBillCardPanel().getBillModel().setValueAt(null, row, "noriginalprice");
				return null;
			}
		}
		//
		PriceAskResultVO rvo = nc.ui.sp.pub.FindSalePriceBO_Client.askBasePrice(salepriceVO);
		if (rvo != null && rvo.getErrFlag().intValue() == 0) {
			saleprice = rvo.getNum();
			/*
			 * �޸ģ���ǿ��
			 * ���ڣ�2008-8-25
			 * ԭ�����ӻ�׼�ۿ���ʾ
			 */
			getBillCardPanel().getBillModel().setValueAt(rvo.getBaseDiscount(), row, "nbasediscount");
		} else if (rvo != null) {
			if (rvo.getErrFlag().intValue() == 1) {
				//showWarningMessage(rvo.getErrMessage());
			} else
				showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON","UPPSCMCommon-000296",null,new String[]{ rvo.getErrMessage()})/*@res "ѯ��ʧ�ܣ�{0}"*/ + rvo.getErrMessage());
		} else {
			showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("scmsoprice","UPPscmsoprice-000278")/*@res "ѯ�۷�������,û�з���ֵ��"*/);
		}
	} catch (Exception e) {
		SCMEnv.error(e);
	}
	return saleprice;

}

protected SalePriceVO constructSalepriceVO(int row) {
	//���ò�ѯ����0
	SalePriceVO salepriceVO = new SalePriceVO();
	//��˾
	salepriceVO.setCropID(getCorpPrimaryKey());
	//ҵ������

	//�ͻ�
	String strCustomerID = (String) getBillCardPanel().getBillModel().getValueAt(row, "ccustomerid");
	salepriceVO.setCustomerID(strCustomerID);
	String ccustbasid = (String) getBillCardPanel().getBillModel().getValueAt(row, "ccustbasid");
	salepriceVO.setCustomerBaseID(ccustbasid);
	//�ͻ�����
	String ccustomertype = (String) getBillCardPanel().getBillModel().getValueAt(row, "ccustclassid");
	salepriceVO.setCustomerClass(ccustomertype);
	//����
	String strCurrencyID = null;
	strCurrencyID = (String) getBillCardPanel().getBillModel().getValueAt(row, "ccurrencyid");
	salepriceVO.setCurrencyID(strCurrencyID);
	//���������ID
	String strInventoryID = (String)getBillCardPanel().getBodyValueAt(row, "cinventoryid");
	salepriceVO.setInventoryID(strInventoryID);
	//�����������ID
	String strInvbasID = (String)getBillCardPanel().getBodyValueAt(row, "cinvbasdocid");
	salepriceVO.setInventoryBaseID(strInvbasID);
	//�������ID
	String strInvclassID = (String)getBillCardPanel().getBodyValueAt(row, "cinvclassid");
	salepriceVO.setInventoryClass(strInvclassID);
//		5.02�¼ӵļ�Ŀ��ID
	String cpricetariffid = (String)getBillCardPanel().getHeadItem("cpricetariffid").getValue();
	salepriceVO.setPricetariffid(cpricetariffid);

	//ϵͳ����
	salepriceVO.setSystemData(getClientEnvironment().getDate());
	//��Ч�ڴ�
	UFDate dstartdate = null;
	dstartdate = new UFDate(getBillCardPanel().getHeadItem("dvalidate").getValue());
	salepriceVO.setDatefrom(dstartdate);
	//�۸���
	String pricetype = (String) getBillCardPanel().getBodyValueAt(row, "csrcpriceid");
	salepriceVO.setPriceTypeid(pricetype);
	//ѯ�ۼ�����λ
	String cpriceunitid = (String) getBillCardPanel().getBodyValueAt(row, "cunitid");
	salepriceVO.setMeasdocid(cpriceunitid);

	//������֯
	String csaleorganid = (String) getBillCardPanel().getBodyValueAt(row, "csaleorganid");
	salepriceVO.setSaleStrucid(csaleorganid);
	//�ջ�����
	String creceiptareaid = (String) getBillCardPanel().getBodyValueAt(row, "creceiptareaid");
	salepriceVO.setReceiptAreaid(creceiptareaid);
	return salepriceVO;
}
/**
*	���۵���ƬPanel
*/
protected BillCardPanel getBillCardPanel() {
	if (cpAdjust == null) {
		try {
			cpAdjust = new BillCardPanel();
			cpAdjust.setName("BillCardPanel");

			//BillData bd = new BillData(getBilltempVO());
			BillData bd=null;
			try{
				bd = new BillData(cpAdjust.getTempletData(SaleBillType.SaleAdjustPrice,null,getClientEnvironment().getUser().getPrimaryKey(),getClientEnvironment().getCorporation().getPrimaryKey()));
				//bd.getHeadItem("dtstarttime").get
			}
			catch(Exception ex){
				SCMEnv.out(ex);
			}
			
			initDecimalFormat(bd);
			cpAdjust.setBillData(bd);
			cpAdjust.setBodyMenuShow(false);
			cpAdjust.addEditListener(this);
			cpAdjust.addBodyEditListener2(this);
			cpAdjust.addBillEditListenerHeadTail(this);
			cpAdjust.setBillBeforeEditListenerHeadTail(this);
		} catch (Throwable e) {
			SCMEnv.error("������Ƭ��巢���쳣", e);
		}
	}
	return cpAdjust;
}
/**
*	���۵��б�Panel
*/
protected BillListPanel getBillListPanel() {
	if (lpAdjust == null) {
		try {
			lpAdjust = new BillListPanel();
			lpAdjust.setName("BillListPanel");

			//BillListData bld = new BillListData(lpAdjust.getTempletData("prm00000000000adjust"));
			//BillListData bld = new BillListData(cpAdjust.getTempletData(SaleBillType.SaleAdjustPrice,"null",getClientEnvironment().getUser().getPrimaryKey(),getClientEnvironment().getCorporation().getPrimaryKey()));
			//initDecimalFormat(bld);
			//lpAdjust.setListData(bld);

			//BillListData bd = new BillListData(getBilltempVO());
			BillListData bd = new BillListData(lpAdjust.getDefaultTemplet(SaleBillType.SaleAdjustPrice,null,getClientEnvironment().getUser().getPrimaryKey(),getClientEnvironment().getCorporation().getPrimaryKey()));

			initDecimalFormat(bd);
			//���ý��棬��������Դ
			lpAdjust.setListData(bd);

			lpAdjust.addEditListener(this);
			lpAdjust.addMouseListener(this);
			//lpAdjust.getChildListPanel().setTatolRowShow(true);



		} catch (Throwable e) {
		}
	}
	return lpAdjust;
}
/**
 * ��õ������͡�
 * �������ڣ�(2001-11-15 8:52:43)
 * @return java.lang.String
 */
public String getBillType(){
	return SaleBillType.SaleAdjustPrice;
}
/**
 * ����ʵ�ָ÷���������ҵ�����ı��⡣
 * @version (00-6-6 13:33:25)
 *
 * @return java.lang.String
 */
public String getTitle() {
	return nc.ui.ml.NCLangRes.getInstance().getStrByID("scmsoprice","UPPscmsoprice-000279")/*@res "���۵�"*/;
}
/**
 * �����Ĺ��ܡ���;�������Եĸ��ģ��Լ�����ִ��ǰ������״̬��
 *  @param	         ����˵��
 *  @return	         ����ֵ
 *  @exception     �쳣����
 *  @see               ��Ҫ�μ�����������
 *  @since	         �������һ���汾���˷�������ӽ���������ѡ��
 *  @deprecated   �÷����������һ���汾���Ѿ������������滻������ѡ��
 *  @author            zhongyue
 * @return nc.vo.pub.AggregatedValueObject
 */
public nc.vo.pub.AggregatedValueObject getVo() {
	AdjustpriceVO hvo =
		(AdjustpriceVO) getBillCardPanel().getBillValueChangeVO(
			"nc.vo.sp.sp002.AdjustpriceVO",
			"nc.vo.sp.sp002.AdjustpriceHeaderVO",
			"nc.vo.sp.sp002.AdjustpriceItemVO");
	return hvo;
}
/**
 * ��ť��ʼ����
 * �������ڣ�(2001-6-16 17:40:18)
 */
public void initButton() {
	bnBrowse.addChildButton(bnQuery);
	bnBrowse.addChildButton(bnLocate);
	//bnBrowse.addChildButton(bnFirst);
	//bnBrowse.addChildButton(bnPrev);
	//bnBrowse.addChildButton(bnNext);
	//bnBrowse.addChildButton(bnLast);
	bnLineOptr.addChildButton(bnAddLine);
	bnLineOptr.addChildButton(bnAddLineBatch);
//	bnLineOptr.addChildButton(bnModifyBatch);
	
	bnLineOptr.addChildButton(bnDelLine);
	boAction.addChildButton(bnSendAudit);
	boAction.addChildButton(bnAudit);
    boAction.addChildButton(bnUnAudit);
    boAction.addChildButton(bnDelete);
	bnAdd.addChildButton(bnAddBase);
	bnAdd.addChildButton(bnAddDiscount);
	bnAdd.addChildButton(bnAddSpec);
	bnAdd.addChildButton(bnCopy);
}
/**
 * �������ʼ����
 * �������ڣ�(2001-4-20 9:07:26)
 */
public void initComboBox() {
	getBillCardPanel().getHeadItem("fadjpricemode").setWithIndex(true);
	adjustmode = (JComboBox)getBillCardPanel().getHeadItem("fadjpricemode").getComponent();
	adjustmode.addItem(nc.ui.ml.NCLangRes.getInstance().getStrByID("scmsoprice","UPPscmsoprice-000280")/*@res "�����Ե���"*/);
	adjustmode.addItem(nc.ui.ml.NCLangRes.getInstance().getStrByID("scmsoprice","UPPscmsoprice-000281")/*@res "�����Ե���"*/);
	adjustmode.addItemListener(this);
	getBillCardPanel().getHeadItem("fstartday").setWithIndex(true);
	JComboBox fstartday = (JComboBox)getBillCardPanel().getHeadItem("fstartday").getComponent();
	fstartday.addItem("");
	fstartday.addItem(nc.ui.ml.NCLangRes.getInstance().getStrByID("scmsoprice","UPPscmsoprice-000282")/*@res "����һ"*/);
	fstartday.addItem(nc.ui.ml.NCLangRes.getInstance().getStrByID("scmsoprice","UPPscmsoprice-000283")/*@res "���ڶ�"*/);
	fstartday.addItem(nc.ui.ml.NCLangRes.getInstance().getStrByID("scmsoprice","UPPscmsoprice-000284")/*@res "������"*/);
	fstartday.addItem(nc.ui.ml.NCLangRes.getInstance().getStrByID("scmsoprice","UPPscmsoprice-000285")/*@res "������"*/);
	fstartday.addItem(nc.ui.ml.NCLangRes.getInstance().getStrByID("scmsoprice","UPPscmsoprice-000286")/*@res "������"*/);
	fstartday.addItem(nc.ui.ml.NCLangRes.getInstance().getStrByID("scmsoprice","UPPscmsoprice-000287")/*@res "������"*/);
	fstartday.addItem(nc.ui.ml.NCLangRes.getInstance().getStrByID("scmsoprice","UPPscmsoprice-000288")/*@res "������"*/);
	getBillCardPanel().getHeadItem("fstopday").setWithIndex(true);
	JComboBox fstopday = (JComboBox)getBillCardPanel().getHeadItem("fstopday").getComponent();
	fstopday.addItem("");
	fstopday.addItem(nc.ui.ml.NCLangRes.getInstance().getStrByID("scmsoprice","UPPscmsoprice-000282")/*@res "����һ"*/);
	fstopday.addItem(nc.ui.ml.NCLangRes.getInstance().getStrByID("scmsoprice","UPPscmsoprice-000283")/*@res "���ڶ�"*/);
	fstopday.addItem(nc.ui.ml.NCLangRes.getInstance().getStrByID("scmsoprice","UPPscmsoprice-000284")/*@res "������"*/);
	fstopday.addItem(nc.ui.ml.NCLangRes.getInstance().getStrByID("scmsoprice","UPPscmsoprice-000285")/*@res "������"*/);
	fstopday.addItem(nc.ui.ml.NCLangRes.getInstance().getStrByID("scmsoprice","UPPscmsoprice-000286")/*@res "������"*/);
	fstopday.addItem(nc.ui.ml.NCLangRes.getInstance().getStrByID("scmsoprice","UPPscmsoprice-000287")/*@res "������"*/);
	fstopday.addItem(nc.ui.ml.NCLangRes.getInstance().getStrByID("scmsoprice","UPPscmsoprice-000288")/*@res "������"*/);
	getBillCardPanel().getTailItem("fstatus").setWithIndex(true);
	JComboBox billstatus = (JComboBox)getBillCardPanel().getTailItem("fstatus").getComponent();
	billstatus.addItem("");
	billstatus.addItem(nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON","UPPSCMCommon-000340")/*@res "����"*/);
	billstatus.addItem(nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UC001-0000027")/*@res "����"*/);
	billstatus.addItem(nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UC001-0000030")/*@res "����"*/);
	billstatus.addItem(nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON","UPPSCMCommon-000119")/*@res "�ر�"*/);
	billstatus.addItem(nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UC001-0000005")/*@res "����"*/);
	billstatus.addItem(nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON","UPPSCMCommon-000128")/*@res "����"*/);
	billstatus.addItem(nc.ui.ml.NCLangRes.getInstance().getStrByID("scmsoprice","UPPscmsoprice-000289")/*@res "��������"*/);
	billstatus.addItem(nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON","UPPSCMCommon-000242")/*@res "����δͨ��"*/);
	getBillListPanel().getHeadItem("fstatus").setWithIndex(true);
	JComboBox fstatus = (JComboBox)getBillListPanel().getHeadItem("fstatus").getComponent();
	fstatus.addItem("");
	fstatus.addItem(nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON","UPPSCMCommon-000340")/*@res "����"*/);
	fstatus.addItem(nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UC001-0000027")/*@res "����"*/);
	fstatus.addItem(nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UC001-0000030")/*@res "����"*/);
	fstatus.addItem(nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON","UPPSCMCommon-000119")/*@res "�ر�"*/);
	fstatus.addItem(nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UC001-0000005")/*@res "����"*/);
	fstatus.addItem(nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON","UPPSCMCommon-000128")/*@res "����"*/);
	fstatus.addItem(nc.ui.ml.NCLangRes.getInstance().getStrByID("scmsoprice","UPPscmsoprice-000289")/*@res "��������"*/);
	fstatus.addItem(nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON","UPPSCMCommon-000242")/*@res "����δͨ��"*/);
	getBillListPanel().getHeadItem("fadjpricemode").setWithIndex(true);
	JComboBox listadjustmode = (JComboBox)getBillListPanel().getHeadItem("fadjpricemode").getComponent();
	listadjustmode.addItem(nc.ui.ml.NCLangRes.getInstance().getStrByID("scmsoprice","UPPscmsoprice-000280")/*@res "�����Ե���"*/);
	listadjustmode.addItem(nc.ui.ml.NCLangRes.getInstance().getStrByID("scmsoprice","UPPscmsoprice-000281")/*@res "�����Ե���"*/);

	getBillListPanel().getHeadItem("fadjtype").setWithIndex(true);
	JComboBox listadjusttype = (JComboBox)getBillListPanel().getHeadItem("fadjtype").getComponent();
	listadjusttype.addItem(nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UC000-0003735")/*@res "������"*/);
	listadjusttype.addItem(nc.ui.ml.NCLangRes.getInstance().getStrByID("scmsoprice","UPPscmsoprice-000255")/*@res "���ؼ�"*/);
	listadjusttype.addItem(NCLangRes.getInstance().getStrByID("scmsoprice","UPPSCMSOPRICE-000807")/*@res "����׼�ۿ�"*/);

	getBillCardPanel().getHeadItem("fadjtype").setWithIndex(true);
	JComboBox cardadjusttype = (JComboBox)getBillCardPanel().getHeadItem("fadjtype").getComponent();
	cardadjusttype.addItem(nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UC000-0003735")/*@res "������"*/);
	cardadjusttype.addItem(nc.ui.ml.NCLangRes.getInstance().getStrByID("scmsoprice","UPPscmsoprice-000255")/*@res "���ؼ�"*/);
	cardadjusttype.addItem(NCLangRes.getInstance().getStrByID("scmsoprice","UPPSCMSOPRICE-000807")/*@res "����׼�ۿ�"*/);

	////������ר�����
	//getBillCardPanel().getHeadItem("vdef1").setWithIndex(false);
	//JComboBox vdef1 = (JComboBox)getBillCardPanel().getHeadItem("vdef1").getComponent();
	//vdef1.addItem("��");
	//vdef1.addItem("��");
	//getBillListPanel().getHeadItem("vdef1").setWithIndex(false);
	//JComboBox listvdef1 = (JComboBox)getBillListPanel().getHeadItem("vdef1").getComponent();
	//listvdef1.addItem("��");
	//listvdef1.addItem("��");
	////end


}
/**
 * �˴����뷽��˵����
 * �������ڣ�(2002-7-8 9:15:50)
 */
public void initCurrtariff() {
	PrmtariffHeadVO[] oldcurrtariffvos=null;
	try{
		//�ж��Ƿ���ڵ�ǰ��Ŀ����л�
		oldcurrtariffvos=TariffBO_Client.queryBycondition(" pk_corp='"+getCorpPrimaryKey()+"'");// and bcurrenttariff='Y' );
		if (oldcurrtariffvos!=null && oldcurrtariffvos.length>0){
			m_curtariffvo=oldcurrtariffvos[0];
		}
	}
	catch( Exception e){
		SCMEnv.out(e.getMessage());
		showErrorMessage(e.getMessage());
		return;
	}
	if (m_localcurrid==null || m_localcurrid.trim().length()==0)
		m_localcurrid = BD301;//nc.ui.sp.pub.PublicPriceParam.getLocalCurrency(getCorpPrimaryKey());
}
public void doQueryAction(ILinkQueryData querydata){
	loadCardData(querydata.getBillID());

	this.remove(getBillListPanel());
	getBillCardPanel().setEnabled(false);
	this.add(getBillCardPanel());
	isList=false;
	
}
/* (non-Javadoc)
 * @see nc.ui.pub.linkoperate.ILinkMaintain#doMaintainAction(nc.ui.pub.linkoperate.ILinkMaintainData)
 */
public void doMaintainAction(ILinkMaintainData arg0) {
	loadCardData(arg0.getBillID());

	this.remove(getBillListPanel());
	getBillCardPanel().setEnabled(false);
	this.add(getBillCardPanel());
	isList=false;
	String status = null;
	status = getBillCardPanel().getTailItem("fstatus").getValue();
	if (nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UC001-0000027")/*@res "����"*/.equals(status) || "2".equals(status))
		iStatus = AUDITED;
	else if (nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UC001-0000005")/*@res "����"*/.equals(status) || "5".equals(status))
		iStatus = DELETED;
	else if (nc.ui.ml.NCLangRes.getInstance().getStrByID("scmsoprice","UPPscmsoprice-000289")/*@res "��������"*/.equals(status) || "7".equals(status))
		iStatus = AUDITING;
	else
		iStatus = FREE;
	if (iStatus == FREE)
	onUpdate();
	else
	setButton(iStatus);
}

/**
 * �˴����뷽��˵����
 * �������ڣ�(2002-9-17 14:09:03)
 */
public void initData(nc.vo.pub.msg.MessageVO msgvo) {
	if (msgvo == null) {
//		bInMsgPanel=false;

	} else {
		SCMEnv.out("into initData ");
		SCMEnv.out("msgvo.getCorpPK():" + msgvo.getCorpPK());
		SCMEnv.out("msgvo.getBillTypeCode():" + msgvo.getPk_billtype());
		SCMEnv.out("msgvo.getBusiTypePK():" + msgvo.getBusiTypePK());
		SCMEnv.out("msgvo.getCheckerCode():" + msgvo.getCheckerCode());
		SCMEnv.out("msgvo.getBillPK():" + msgvo.getBillPK());
//		bInMsgPanel=true;
		sendman = msgvo.getSenderCode();

		initParam();
		setName("PriceTypeUI");
		pricedigit = PublicPriceParam.getPriceDigit(getCorpPrimaryKey());
		setBnStatus(INIT);
		initButton();
		setButtons(aButtonGroup);
		add(getBillCardPanel());
		initComboBox();
		initRef();
//		bmAdjust = getBillCardPanel().getBillModel();
		getBillCardPanel().setEnabled(false);
		loadCardData(msgvo.getBillPK());

		bnAudit.setTag("APPROVE");
		bnUnAudit.setTag("UNAPPROVE");
		ButtonObject[] btns = {
			bnAudit, //����
			bnUnAudit,   //����
			bnAuditFlowStatus,            //������״̬
			boDocument
			//boAuditMsg
		};
		//id=msgvo.getBillPK();
		this.remove(getBillListPanel());
		getBillCardPanel().setEnabled(false);
		this.add(getBillCardPanel());
		isList=false;
		
		String status = null;
		status = String.valueOf(getBillCardPanel().getTailItem("fstatus").getValueObject());
		if (nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UC001-0000027")/*@res "����"*/.equals(status) || "2".equals(status))
			iStatus = AUDITED;
		else if (nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UC001-0000005")/*@res "����"*/.equals(status) || "5".equals(status))
			iStatus = DELETED;
		else if (nc.ui.ml.NCLangRes.getInstance().getStrByID("scmsoprice","UPPscmsoprice-000289")/*@res "��������"*/.equals(status) || "7".equals(status))
			iStatus = AUDITING;
		else
			iStatus = FREE;

		if (iStatus == AUDITED) {
			bnAudit.setEnabled(false);
			bnUnAudit.setEnabled(true);
		}
		else{
			bnAudit.setEnabled(true);
			bnUnAudit.setEnabled(false);
		}		
		bnAuditFlowStatus.setEnabled(true);
		//bnAudit.setEnabled(true);
		boDocument.setEnabled(true);
		setButtons(btns);
		SCMEnv.out("end initData ");
	}
}
/**
 * �˴����뷽��˵����
 * �������ڣ�(2001-11-22 11:30:02)
 */
public void initDecimalFormat(BillData bd) {
	bd.getBodyItem("nbaseprice").setDecimalDigits(pricedigit);
	bd.getBodyItem("noriginalprice").setDecimalDigits(pricedigit);
	bd.getBodyItem("nnewprice").setDecimalDigits(pricedigit);
	bd.getBodyItem("npriceadd").setDecimalDigits(pricedigit);
	bd.getBodyItem("npricefactor").setDecimalDigits(4);
}
/**
 * �˴����뷽��˵����
 * �������ڣ�(2001-11-22 11:30:02)
 */
public void initDecimalFormat(BillListData bld) {
	bld.getBodyItem("nbaseprice").setDecimalDigits(pricedigit);
	bld.getBodyItem("noriginalprice").setDecimalDigits(pricedigit);
	bld.getBodyItem("nnewprice").setDecimalDigits(pricedigit);
	bld.getBodyItem("npriceadd").setDecimalDigits(pricedigit);
	bld.getBodyItem("npricefactor").setDecimalDigits(4);
}

protected void initialize() {
	initialize1();
}


/**
 * �����ʼ����
 * �������ڣ�(2001-4-20 9:25:23)
 */
protected void initialize1() {
	if (getClientEnvironment().getUser().getPrimaryKey()==null||getClientEnvironment().getUser().getPrimaryKey().length()<20) {
		javax.swing.JLabel label = new javax.swing.JLabel(nc.ui.ml.NCLangRes.getInstance()
				.getStrByID("scmsoprice", "UPPscmsoprice-000827")/*
																	 * @res
																	 * "�����û������������ҵ��"
																	 */);
		label.setHorizontalAlignment(javax.swing.JLabel.CENTER);
		add(label);
		return;
	}
	initParam();
	setName("PriceTypeUI");
	if (!isNodeEnable()) {
		javax.swing.JLabel label = new javax.swing.JLabel(nc.ui.ml.NCLangRes.getInstance().getStrByID("scmsoprice","UPPSCMSOPRICE-000099")/*@res "�˽ڵ���Ч��"*/);
		label.setHorizontalAlignment(javax.swing.JLabel.CENTER);
		add(label);
		return;
	}
	if (BD505!=null)
		pricedigit = BD505.intValue();
	
	initButton();
	setButtons(aButtonGroup);
	add(getBillListPanel());
	initComboBox();
	initRef();
	initCurrtariff();
	initInterface();
	initSelreflist();
//	bmAdjust = getBillCardPanel().getBillModel();
	getBillCardPanel().setEnabled(false);
	setBodyCol();

	/*
	 * �޸ģ���ǿ��
	 * ���ڣ�2009-2-27
	 * CQ�ţ�NCdp200733421
	 * ԭ����������˵������ֺ͡��в������˵��µ�һ��
	 */
	initBodyMenu();

	setBnStatus(INIT);
	bAdjSpecMustNRelateBase=SA38;//PublicPriceParam.getUFBParam("0001","SA38");
	getBillListPanel().updateUI();
}

/**
 * ��ʼ�������Ҽ��˵�
 * 
 * @author ��ǿ��
 * @since 2009-2-27
 */
private void initBodyMenu() {
	UIMenuItem addLineBatch = new UIMenuItem(bnAddLineBatch.getName());
	addLineBatch.addActionListener(new ActionListener(){

		public void actionPerformed(ActionEvent e) {
			onAddLineBatch();
		}});

	getBillCardPanel().setBodyMenu(new UIMenuItem[]{
			getBillCardPanel().getAddLineMenuItem(),
			addLineBatch,
			getBillCardPanel().getDelLineMenuItem(),
		});
	getBillCardPanel().addBodyMenuListener(new BillBodyMenuListener(){

		public void onMenuItemClick(ActionEvent e) {
			UIMenuItem item = (UIMenuItem) e.getSource();
			if (item == getBillCardPanel().getAddLineMenuItem()) {
				onAddLine();
			} else if (item == getBillCardPanel().getDelLineMenuItem()) {
				onDelLine();
			}
		}
		public void actionPerformed(ActionEvent e) {}
	});
}




/**
 * �����Ĺ��ܡ���;�������Եĸ��ģ��Լ�����ִ��ǰ������״̬��
 *  @param	         ����˵��
 *  @return	         ����ֵ
 *  @exception     �쳣����
 *  @see               ��Ҫ�μ�����������
 *  @since	         �������һ���汾���˷�������ӽ���������ѡ��
 *  @author            zhongyue
 * @param pk_corp java.lang.String
 * @param billtype java.lang.String
 * @param busitype java.lang.String
 * @param operator java.lang.String
 * @param id java.lang.String
 */
public void doApproveAction(ILinkApproveData approvedata) {
//	bInMsgPanel=true;
	//sendman = approvedata.getUserObject().toString();

	loadCardData(approvedata.getBillID());

	bnAudit.setTag("APPROVE");
	bnUnAudit.setTag("UNAPPROVE");
	ButtonObject[] btns = {
		bnAudit, //����
		bnUnAudit,   //����
		bnAuditFlowStatus,            //������״̬
		boDocument
		//boAuditMsg
	};

	/*
	 * �޸ģ���ǿ��
	 * ���ڣ�2009-6-6
	 * CQ�ţ�NCdp200870727
	 * ԭ����Ϊ�ڽ��湹��Ĺ������Ѿ���һЩ��ť��Ϊ�Ӱ�ť��ʼ������(��initButton����)��
	 *       ���������������ֱ���Ϊ������ť�ŵ�toftPanel�У�ƽ̨�ڴ����������ʱ�Ὣ��ݼ���ʧ��
	 *       ��Ϊ������ť��Ӧ����û�и���ť�ģ��������ｫ����ť�ÿ�
	 */
	bnAudit.setParent(null);
	bnUnAudit.setParent(null);
	bnAuditFlowStatus.setParent(null);
	boDocument.setParent(null);



	//id=msgvo.getBillPK();
	this.remove(getBillListPanel());
	getBillCardPanel().setEnabled(false);
	this.add(getBillCardPanel());
	isList=false;
	
	String status = null;
	status = getBillCardPanel().getTailItem("fstatus").getValue();
	if (nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UC001-0000027")/*@res "����"*/.equals(status) || "2".equals(status))
		iStatus = AUDITED;
	else if (nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UC001-0000005")/*@res "����"*/.equals(status) || "5".equals(status))
		iStatus = DELETED;
	else if (nc.ui.ml.NCLangRes.getInstance().getStrByID("scmsoprice","UPPscmsoprice-000289")/*@res "��������"*/.equals(status) || "7".equals(status))
		iStatus = AUDITING;
	else
		iStatus = FREE;

	if (iStatus == AUDITED) {
		bnAudit.setEnabled(false);
		bnUnAudit.setEnabled(true);
	}
	else{
		bnAudit.setEnabled(true);
		bnUnAudit.setEnabled(false);
	}		
	bnAuditFlowStatus.setEnabled(true);
	//bnAudit.setEnabled(true);
	boDocument.setEnabled(true);
	setButtons(btns);
}
/**
 * �����ʼ����
 * �������ڣ�(2001-4-20 9:07:26)
 */
public void initInterface() {

}
/**
 * �˴����뷽��˵����
 * �������ڣ�(2002-7-8 9:15:50)
 */
public void initRef() {
	if ("0001".equals(getCorpPrimaryKey())) {
		String[] editf = getBillCardPanel().getBodyItem("code").getEditFormulas();
		editf[0] = "cinventoryid->getColValue(bd_invbasdoc,pk_invbasdoc,pk_invbasdoc,cinventoryid)";
		editf[1] = "cinvbasdocid->getColValue(bd_invbasdoc,pk_invbasdoc,pk_invbasdoc,cinventoryid)";
	}

	//�޸ı���������
	UIRefPane invRef =
		(UIRefPane) getBillCardPanel().getBodyItem("code").getComponent();
	if ("0001".equals(getCorpPrimaryKey()))
		invRef.setRefNodeName("�����������");
	invRef.setTreeGridNodeMultiSelected(true);
	invRef.setMultiSelectedEnabled(true);

	UIRefPane cuRef =
		(UIRefPane) getBillCardPanel().getBodyItem("ccustomername").getComponent();
	if ("0001".equals(getCorpPrimaryKey())){

		//�ͻ�
		String[] custFormular = getBillCardPanel().getBodyItem("ccustomername").getEditFormulas();
		custFormular[0]="ccustomerid->getColValue(bd_cubasdoc,pk_cubasdoc,pk_cubasdoc,ccustomerid)";
		custFormular[1]="ccustbasid->getColValue(bd_cubasdoc,pk_cubasdoc,pk_cubasdoc,ccustomerid)";

		cuRef.setRefNodeName("���̻�������");

		String[] custFormularload = getBillCardPanel().getBodyItem("ccustomerid").getLoadFormula();
		custFormularload[0]="ccustbasid->getColValue(bd_cubasdoc,pk_cubasdoc,pk_cubasdoc,ccustomerid)";
		String[] custFormularloadList = getBillListPanel().getBodyItem("ccustomerid").getLoadFormula();
		custFormularloadList[0]="ccustbasid->getColValue(bd_cubasdoc,pk_cubasdoc,pk_cubasdoc,ccustomerid)";
	}


	UIRefPane tariffRef =
		(UIRefPane) getBillCardPanel().getHeadItem("cpricetariffid").getComponent();
	tariffRef.setReturnCode(true);

//	((UIRefPane)getBillCardPanel().getBodyItem("ccustclassname").getComponent()).setRefModel(new nc.ui.bd.b08.PriceGroupRefModel());
	((UIRefPane)getBillCardPanel().getBodyItem("ccustclassname").getComponent()).setReturnCode(false);
	((UIRefPane)getBillCardPanel().getBodyItem("ccurrencyname").getComponent()).setReturnCode(false);
	((UIRefPane)getBillCardPanel().getBodyItem("cinvclassname").getComponent()).setReturnCode(false);
	((UIRefPane)getBillCardPanel().getBodyItem("ccustomername").getComponent()).setReturnCode(false);
	((UIRefPane)getBillCardPanel().getBodyItem("creceiptareaname").getComponent()).setReturnCode(false);
	((UIRefPane)getBillCardPanel().getBodyItem("csaleorganname").getComponent()).setReturnCode(false);
	((UIRefPane)getBillCardPanel().getBodyItem("sourceprice").getComponent()).setReturnCode(false);
	((UIRefPane)getBillCardPanel().getBodyItem("unit").getComponent()).setReturnCode(false);

	//if (!"0001".equals(getCorpPrimaryKey()))
	//	((UIRefPane)getBillCardPanel().getBodyItem("code").getComponent()).setWhereString(" bd_invmandoc.pk_corp='"+getCorpPrimaryKey()+"' and invlifeperiod<>3 ");

	((UIRefPane)getBillCardPanel().getBodyItem("csaleorganname").getComponent()).setWhereString("(pk_corp='0001' or pk_corp='"+getCorpPrimaryKey()+"')");

}
/**
 * ��ʼ���з���־�Ĳ����б�
 * �������ڣ�(2004-7-21 9:07:26)
 */
public void initSelreflist() {

	//��ʽΪ: ����,��ͷ/����/��β
	m_arselreflist.clear();
	String[] inv=new String[]{"code","B"};
	m_arselreflist.add(inv);
	String[] ccustomername=new String[]{"ccustomername","B"};
	m_arselreflist.add(ccustomername);
	String[] csaleorganname=new String[]{"csaleorganname","B"};
	m_arselreflist.add(csaleorganname);
	String[] ccustclassname=new String[]{"ccustclassname","B"};
	m_arselreflist.add(ccustclassname);

}
/**
 * ���۷�ʽѡ����
 * �������ڣ�(2001-5-30 9:33:47)
 */
public void itemStateChanged(ItemEvent e) {
	if(e.getSource() == adjustmode)
		setAdjustMode(e.getItem().toString());
}
/**
 * �������롣
 * �������ڣ�(2001-4-21 10:36:57)
 */
public void loadCardData(String id) {
	try {
		//��idƴwhere�Ӿ�
		if (id == null) {
			id = "";
		}
		AdjustpriceVO vo = AdjustpriceBO_Client.findByPrimaryKey(id);	
		/*getBillListPanel().setHeaderValueVO(
			new nc.vo.pub.CircularlyAccessibleValueObject[] { vo.getParentVO()});
		AdjustpriceItemVO[] vaAdjustpriceItem =
			(AdjustpriceItemVO[]) vo.getChildrenVO();
		if (vaAdjustpriceItem != null && vaAdjustpriceItem.length != 0) {
			getBillListPanel().setBodyValueVO(vaAdjustpriceItem);
			getBillListPanel().getBodyBillModel().execLoadFormula();
			getBillListPanel().getBodyBillModel().updateValue();
		} else {
			getBillListPanel().getBodyBillModel().clearBodyData();
			getBillListPanel().getBodyBillModel().updateValue();
		}
		getBillListPanel().updateUI();*/
		getBillCardPanel().setBillValueVO(vo);
		getBillCardPanel().execHeadLoadFormulas();
		getBillCardPanel().getBillModel().execLoadFormula();
		for(int i=0;i<getBillCardPanel().getBillModel().getRowCount();i++)
			getBillCardPanel().getBillModel().setValueAt(new UFBoolean(true),i,"binqusucess");

		getBillCardPanel().updateValue();

	} catch (Exception e) {
		SCMEnv.out(e.getMessage());
	}
}
/**
 * ���ؿ�Ƭģ�塣
 * �������ڣ�(2001-11-15 9:03:35)
 */
public void loadCardTemplet(String billtype, String operator, String pk_corp) {
	//����ҵ������
	//for (int i=0;i<boBusiType.getChildCount();i++){
	//if (boBusiType.getChildButtonGroup()[i].isSelected()){
	//getBillCardPanel().setBusiType(boBusiType.getChildButtonGroup()[i].getTag());
	//break;
	//}
	//}

	showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON","UPPSCMCommon-000136")/*@res "��ʼ����ģ��...."*/);

	BillData bd = new BillData(cpAdjust.getTempletData(billtype, null, operator, pk_corp));
	//BillData bd = new BillData(getBillCardPanel().getTempletData());
	//BillData bd= new BillData(getBillCardPanel().getTempletData(getTempletID()));

	//�ı����
	//setCardPanel(bd);

	//���ý��棬��������Դ
	getBillCardPanel().setBillData(bd);

	//�������볤��
	//setInputLimit();

	//����������
	//initBodyComboBox();

	//���Ի�״̬
	//initState();
	getBillCardPanel().setEnabled(false);

	//���úϼƼ���
	//getBillCardPanel().addBodyTotalListener(this);
	//getBillCardPanel().getBillTable().getSelectionModel().addListSelectionListener(this);
	//getBillCardPanel().getBillTable().getColumnModel().addColumnModelListener(this);
	//getBillCardPanel().getBillModel().addTableModelListener(this);

	showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON","UPPSCMCommon-000176")/*@res "ģ����سɹ���"*/);
}
/**
 * ���ص��۵��б�������ݡ�
 * �������ڣ�(2001-6-20 14:22:44)
 */
public void loadListBody(int row) {
	try {
		String pk =
			getBillListPanel().getHeadBillModel().getValueAt(row, "cadjpriceid").toString();
		AdjustpriceItemVO[] vaAdjustpriceItem = AdjustpriceBO_Client.queryBody(pk);
		if (vaAdjustpriceItem != null && vaAdjustpriceItem.length != 0) {
			/**
			 * �޸ģ�������
			 * ���ڣ�2009-9-3
			 * CQ�ţ�
			 * ԭ����ǰ̨���浱ǰѡ���еı���VO,�����ظ��غ�̨����
			 */
			adjitemVOs = vaAdjustpriceItem;
			getBillListPanel().setBodyValueVO(vaAdjustpriceItem);
			getBillListPanel().getBodyBillModel().execLoadFormula();
			String[] fmls=new String[1];
			fmls[0]="ccurrencyname->getColValue( bd_currtype , currtypename , pk_currtype , ccurrencyid)";
			getBillListPanel().getBodyBillModel().execFormulas(fmls);
			getBillListPanel().getBodyBillModel().updateValue();
		}else{
			getBillListPanel().getBodyBillModel().clearBodyData();
			getBillListPanel().getBodyBillModel().updateValue();
		}
	} catch (Exception e) {
		SCMEnv.out(e.getMessage());
	}
}
/**
 * ���ص��۵��б����ݡ�
 * �������ڣ�(2001-6-20 14:00:19)
 * @param sqlCon java.lang.String
 */
private void loadListHeader(String sqlCon) {
	try{
		AdjustpriceHeaderVO[] vaAdjustpriceHeader = AdjustpriceBO_Client.queryAllHeaders(sqlCon);
		if (queryForApprove) {
			vaAdjustpriceHeader = filterForApprove(vaAdjustpriceHeader);
		}
		if(vaAdjustpriceHeader != null && vaAdjustpriceHeader.length != 0){
			getBillListPanel().getHeadTable().clearSelection();
			getBillListPanel().setHeaderValueVO(vaAdjustpriceHeader);
			getBillListPanel().getHeadBillModel().execLoadFormula();
			getBillListPanel().getHeadBillModel().updateValue();
			isQuery = true;
		}
	}catch(Exception e){
		SCMEnv.error(e);
	}
}

	/**
	 * ���˳���������ҵ��VO
	 * 
	 * @param vos ���д����˵�ҵ��VO
	 * @return ���д�������ҵ��VO
	 * @author ��ǿ��
	 * @throws BusinessException 
	 * @since 2008-8-15
	 */
	private AdjustpriceHeaderVO[] filterForApprove(AdjustpriceHeaderVO[] vos) throws BusinessException {
		String billType = "36";
		String userId = ce.getUser().getPrimaryKey();
		return QueryUtil.filterForApprove(vos, billType, userId);
	}

	/** ��������ѯ�ӿڣ��������˴��������� */
	private IPFWorkflowQry workflowQry = null;

	/**
	 * ��������ѯ�ӿڣ��������˴���������
	 * 
	 * @return ��������ѯ�ӿ�
	 * @author ��ǿ��
	 * @since 2008-8-15
	 */
	private IPFWorkflowQry getWorkflowQry() {
		if (null == workflowQry) {
			workflowQry = (IPFWorkflowQry) NCLocator.getInstance().lookup(IPFWorkflowQry.class.getName());
		}
		return workflowQry;
	}

public void mouse_doubleclick(nc.ui.pub.bill.BillMouseEnent e){
	if (e.getPos() == BillItem.HEAD)
		onDoubleClick();
}
/**
 * ���Ӳ�����
 * �������ڣ�(2001-5-30 14:58:22)
 */
public void onAdd() {
	
	if(isList)
		iOldStatus = INIT;
	else
		iOldStatus = iStatus;
	iStatus = NEW;
//	���ñ����е����Ƿ�ɼ�
	if (isList) {
		isList = false;
		setBodyCol();
		remove(getBillListPanel());
		add(getBillCardPanel(), "Center");
		getBillCardPanel().updateUI();
	}
	else{
		setBodyCol();
	}

	m_dlgQuick=null;
	getBillCardPanel().setEnabled(true);


	getBillCardPanel().addNew();
	//Ԥ�ƿ���
	//getBillCardPanel().getBodyPanel().addLine();
	getBillCardPanel().getHeadItem("pk_corp").setValue(getCorpPrimaryKey());
	getBillCardPanel().getHeadItem("dadjpricedate").setValue(ce.getDate());

	getBillCardPanel().getHeadItem("dvalidate").setValue(ce.getDate());
	getBillCardPanel().getTailItem("cadjusterid").setValue(ce.getUser().getPrimaryKey());
	getBillCardPanel().getTailItem("fstatus").setValue(new Integer(1));
	getBillCardPanel().getHeadItem("fadjpricemode").setValue(new Integer(0));
	getBillCardPanel().getHeadItem("dtstarttime").setValue(new UFTime("00:00:00"));

	if (isAdjustBase) {
		/*
		 * �޸ģ���ǿ��
		 * ���ڣ�2008-7-30
		 * ԭ�����ӵ���׼�ۿ�
		 */
		if (isAdjustDiscount) {
			getBillCardPanel().getHeadItem("fadjtype").setValue(AdjustpriceVO.BASE_DISCOUNT);
		} else {
			getBillCardPanel().getHeadItem("fadjtype").setValue(AdjustpriceVO.BASE_PRICE);
		}
		getBillCardPanel().getHeadItem("dtstoptime").setValue(null);
	}
	else {
		getBillCardPanel().getHeadItem("fadjtype").setValue(AdjustpriceVO.SPECIAL_PRICE);
		getBillCardPanel().getHeadItem("dtstoptime").setValue(new UFTime("23:59:59"));
	}

//	if (isAdjustBase){
//		//��Ŀ��ȱʡΪ��ǰ��Ŀ��
//		if (m_curtariffvo!=null){
//			getBillCardPanel().getHeadItem("cpricetariffid").setValue(m_curtariffvo.getCpricetariffid());
//			getBillCardPanel().getHeadItem("cpricetariffname").setValue(m_curtariffvo.getCpricetariffname());
//		}
//	}

	////������ר�����
	////getBillCardPanel().getHeadItem("vdef1").setValue(new Integer(0));
	//JComboBox vdef1 = (JComboBox)getBillCardPanel().getHeadItem("vdef1").getComponent();
	//vdef1.setSelectedIndex(0);
	////end

	//getBillCardPanel().addLine();

	//ȥ����ԭ�򣺿��ٵ��������п���
	//onAddLine();

	setBnStatus(iStatus);
	showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("scmsoprice","UPPscmsoprice-000290")/*@res "���ӵ��۵���"*/);
	//����궨λ�ڵ�һ���ɱ༭��
	getBillCardPanel().transferFocusTo(BillCardPanel.HEAD);
	m_bcodechanged = false;

	affectvos = null;
	
}
/**
 * ����һ�С�
 * �������ڣ�(2001-4-20 15:52:17)
 */
public void onAddLine() {
	if (!isAdjustBase){
		if (!validateNotNull("dvalidate",NCLangRes.getInstance().getStrByID("scmsoprice","UPPscmsoprice-000273")/*@res "��ʼʱ�䲻��Ϊ�գ�"*/)) {
			return;
		}
		if (!validateNotNull("dtstarttime",NCLangRes.getInstance().getStrByID("scmsoprice","UPPscmsoprice-000810")/*@res "��Чʱ�䲻��Ϊ��"*/)) {
			return;
		}
		if (!validateNotNull("dexpiredate",NCLangRes.getInstance().getStrByID("scmsoprice","UPPscmsoprice-000272")/*@res "��ֹʱ�䲻��Ϊ�գ�"*/)) {
			return;
		}
		if (!validateNotNull("dtstoptime",NCLangRes.getInstance().getStrByID("scmsoprice","UPPscmsoprice-000811")/*@res "ʧЧʱ�䲻��Ϊ��"*/)) {
			return;
		}

	}
	String cpricetariffid = (String)getBillCardPanel().getHeadItem("cpricetariffid").getValueObject();
	if (isEmpty(cpricetariffid)){
		showErrorMessage("��Ŀ����Ϊ��");
		return;
	}
	this.showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCommon","UPPSCMCommon-000465")/*
             * @res
             * "����������..."
             */);
	getBillCardPanel().addLine();
	int iLastRow = getBillCardPanel().getRowCount() - 1;
	getBillCardPanel().getBillModel().setValueAt(new Integer(1),iLastRow,"npricefactor");
	getBillCardPanel().getBillModel().setValueAt(new Integer(0),iLastRow,"npriceadd");
	//getBillCardPanel().getBillModel().setCellEditable(iLastRow,"npricefactor",false);
	//getBillCardPanel().getBillModel().setCellEditable(iLastRow,"npriceadd",false);
	getBillCardPanel().getBillModel().setCellEditable(iLastRow,"npricefactor",true);
	getBillCardPanel().getBillModel().setCellEditable(iLastRow,"npriceadd",true);
	bnCancel.setEnabled(true);
	updateButton(bnCancel);
	

	//���õ�ǰ�����еĿɱ༭��״̬
	if (!isAdjustBase){
		if (bAdjSpecMustNRelateBase!=null && bAdjSpecMustNRelateBase.booleanValue())
			getBillCardPanel().setBodyValueAt(new UFBoolean("N"),iLastRow,"brelatebase");
		else
			getBillCardPanel().setBodyValueAt(new UFBoolean("Y"),iLastRow,"brelatebase");
		//getBillCardPanel().setBodyValueAt(new UFBoolean("Y"),iLastRow,"brelatebase");
		setRowEditCol(getBillCardPanel().getRowCount()-1);
	}
	getBillCardPanel().getBillModel().setValueAt(m_localcurrid,iLastRow,"ccurrencyid");
	getBillCardPanel().getBillModel().execFormula(iLastRow,
		getBillCardPanel().getBodyItem("ccurrencyid").getLoadFormula());
}


	/**
	 * ������Ԫ�صķǿ�У�飬���Ϊ�գ�����������ʾ��Ϣ��������false�����򷵻�true
	 * 
	 * @param itemkey ҪУ����ֶ�
	 * @param errMsg �ֶ�Ϊ��ʱ�Ĵ�����ʾ��Ϣ
	 * @return ���Ϊ�գ�����������ʾ��Ϣ��������false�����򷵻�true
	 * @author ��ǿ��
	 * @since 2008-8-19
	 */
	private boolean validateNotNull(String itemkey, String errMsg) {
		String value = getBillCardPanel().getHeadItem(itemkey).getValueObject().toString();
		if (isEmptyWithTrim(value)) {
			showErrorMessage(errMsg);
			return false;
		}
		return true;
	}


/**
 * ���۷�����
 * �������ڣ�(2001-6-15 9:45:18)
 */
public void onAdjustAnalyse() {
	//if (!isAdjustBase){
		//showErrorMessage("ֻ�е����۵õ��۵����ܲ���!");
		//return;
	//}
	if (isList){
		if (getBillListPanel().getHeadBillModel().getRowCount()==0){
			showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("scmsoprice","UPPscmsoprice-000292")/*@res "��ǰû�м�¼���ܲ鿴!"*/);
			return;
		}
	}
	else {
		if (getBillCardPanel().getRowCount()==0){
			showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("scmsoprice","UPPscmsoprice-000292")/*@res "��ǰû�м�¼���ܲ鿴!"*/);
			return;
		}
	}

	//��Ϊѡ����

	//ȡ���,ԭ��,�ּ� ����
	//ArrayList al=new ArrayList();
	Vector al = new Vector();
	String key=null;
	BillModel bm=null;
	int[] selrows=null;
	if (isList){
		int selrow=-1;
		//if (getBillListPanel().getHeadTable().getSelectedRow() > -1) {
			//selrow=getBillListPanel().getHeadTable().getSelectedRow();
		//}else{
			//selrow=getBillListPanel().getHeadBillModel().getEditRow();
		//}
		//if (selrow==-1 && iCurrRow>0 )
			//selrow=iCurrRow;
		//if (selrow==-1)
			//return;
		//if (getBillListPanel().getBodyBillModel().getRowCount()==0){
			//loadListBody(selrow);
			//setButton(selrow);
			//affectvos=null;
			//updateUI();
		//}
		if (getBillListPanel().getBodyTable().getRowCount()==0 || getBillListPanel().getBodyTable().getSelectedRowCount()==0){
			showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("scmsoprice","UPPscmsoprice-000293")/*@res "����ѡ����Ҫ��������!"*/);
			return;
		}
		selrows=getBillListPanel().getBodyTable().getSelectedRows();
		bm=getBillListPanel().getBodyBillModel();
	}
	else{
		if (getBillCardPanel().getRowCount()==0 || getBillCardPanel().getBillTable().getSelectedRowCount()==0){
			showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("scmsoprice","UPPscmsoprice-000293")/*@res "����ѡ����Ҫ��������!"*/);
			return;
		}
		selrows= getBillCardPanel().getBillTable().getSelectedRows();
		bm=getBillCardPanel().getBillModel();
	}
	if (selrows==null || selrows.length==0)
		return;
	for (int idx=0;idx<selrows.length;idx++){
		int i=selrows[idx];
		if (i>=bm.getRowCount())
			continue;
		String cinventoryid=(String)bm.getValueAt(i,"cinventoryid");
		String cinvbasdocid=(String)bm.getValueAt(i,"cinvbasdocid");
		if (cinvbasdocid!=null && cinvbasdocid.trim().length()!=0){

			String code=(String)bm.getValueAt(i,"code");
			String name=(String)bm.getValueAt(i,"name");
			String size=(String)bm.getValueAt(i,"size");
			String type=(String)bm.getValueAt(i,"type");
			String unit=(String)bm.getValueAt(i,"unit");
			String invname="";
			invname = (code==null?"":code);
			invname = invname + (name==null?"":"/"+name);
			invname = invname + (size==null?"":"/"+size);
			invname = invname + (type==null?"":"/"+type);
			String sourceprice=(String)bm.getValueAt(i,"sourceprice");

			UFDouble orgprice=null;
			UFDouble newprice=null;
			orgprice=(UFDouble)bm.getValueAt(i,"nbaseprice");
			if (orgprice==null || orgprice.doubleValue()<=0){
				orgprice=(UFDouble)bm.getValueAt(i,"noriginalprice");
				if (orgprice==null || orgprice.doubleValue()<=0){
					orgprice=new UFDouble(0);
				}
			}
			newprice=(UFDouble)bm.getValueAt(i,"nnewprice");
			if (!(orgprice.doubleValue()>0 && newprice.doubleValue()>0)){
				continue;
			}
			DMDataVO dmvo=new DMDataVO();
			dmvo.setAttributeValue("cinventoryid",cinventoryid);
			dmvo.setAttributeValue("cinvbasdocid",cinvbasdocid);
			dmvo.setAttributeValue("invname",invname);
			dmvo.setAttributeValue("unit",unit);
			dmvo.setAttributeValue("orgprice",orgprice);
			dmvo.setAttributeValue("newprice",newprice);
			dmvo.setAttributeValue("pricetype",sourceprice);
			al.add(dmvo);
			//code name size type unit
		}
		else{
			String cinvclassid=(String)bm.getValueAt(i,"cinvclassid");
			String csrcpriceid=(String)bm.getValueAt(i,"csrcpriceid");
			//���Ӵ������۵��������˵�
			if (isAdjustBase)
				continue;
			
			if (cinvclassid==null || cinvclassid.trim().length()==0)
				continue;
			if (csrcpriceid==null || csrcpriceid.trim().length()==0)
				continue;
			try{
				nc.vo.sp.sp001.PricetypeVO prvo=
				nc.ui.sp.sp001.PricetypeBO_Client.findByPrimaryKey(csrcpriceid);
				nc.vo.bd.b14.InvclVO invclvo=
				nc.ui.bd.b14.InvclBO_Client.findByPrimaryKey(cinvclassid);
				//InventorysalepriceVO[] vaInventoryprice = InventorysalepriceBO_Client.queryInventory(getCorpPrimaryKey(),invclvo.getInvclasscode(),new Boolean("0001".equals(getCorpPrimaryKey())));
				InventoryVO[] manvos=null;
				PriceAskResultVO[] rvos=null;
				try{
					if ("0001".equals(getCorpPrimaryKey())){
						manvos=nc.ui.bd.b15.InventoryBO_Client.queryInventoryByClassID(cinvclassid);
					}
					else{
						manvos=nc.ui.bd.b15.InventoryBO_Client.queryInventoryByClassID16(cinvclassid,getCorpPrimaryKey());
					}
					if (manvos!=null && manvos.length>0){
						SalePriceVO[] saleprices=new SalePriceVO[manvos.length];
						for (int iinv=0;iinv<manvos.length;iinv++){
							//���ò�ѯ����0
							SalePriceVO salepriceVO = new SalePriceVO();
							//��˾
							salepriceVO.setCropID(getCorpPrimaryKey());
							//ҵ������

							//�ͻ�
							String strCustomerID = (String)bm.getValueAt(i,"ccustomerid");
							salepriceVO.setCustomerID(strCustomerID);
							String ccustbasid = (String)bm.getValueAt(i,"ccustbasid");
							salepriceVO.setCustomerBaseID(ccustbasid);
							//�ͻ�����
							String ccustomertype = (String)bm.getValueAt(i,"ccustclassid");
							salepriceVO.setCustomerClass(ccustomertype);
							//����
							String strCurrencyID = null;
							strCurrencyID = (String)bm.getValueAt(i,"ccurrencyid");
							salepriceVO.setCurrencyID(strCurrencyID);
							GroupInventoryVO basvo=(GroupInventoryVO)manvos[iinv].getParentVO();
							if ("0001".equals(getCorpPrimaryKey())){
								//���������ID
								salepriceVO.setInventoryID(basvo.getPk_invbasdoc());
								//�����������ID
								salepriceVO.setInventoryBaseID(basvo.getPk_invbasdoc());
							}else{
								//���������ID
								CorpInventoryVO manvo=(CorpInventoryVO)manvos[iinv].getCorpInventoryVO();
								salepriceVO.setInventoryID(manvo.getPk_invmandoc());
								//�����������ID
								salepriceVO.setInventoryBaseID(basvo.getPk_invbasdoc());
							}

							//ϵͳ����
							salepriceVO.setSystemData(getClientEnvironment().getDate());
							//��Ч�ڴ�
							UFDate dstartdate=null;
							if (isList)
								dstartdate=new UFDate((String)getBillListPanel().getHeadBillModel().getValueAt(iCurrRow,"dvalidate"));
							else
								dstartdate=new UFDate(getBillCardPanel().getHeadItem("dvalidate").getValue());
							salepriceVO.setDatefrom(dstartdate);
							//�۸���
							String pricetype = (String) bm.getValueAt(i, "csrcpriceid");
							salepriceVO.setPriceTypeid(pricetype);
							//ѯ�ۼ�����λ
							String cpriceunitid=basvo.getPk_measdoc();
							salepriceVO.setMeasdocid(cpriceunitid);

							//������֯
							String csaleorganid=(String)bm.getValueAt(i,"csaleorganid");
							salepriceVO.setSaleStrucid(csaleorganid);
							//�ջ�����
							String creceiptareaid=(String)bm.getValueAt(i,"creceiptareaid");
							salepriceVO.setReceiptAreaid(creceiptareaid);

							saleprices[iinv]=salepriceVO;
						}
						rvos=nc.ui.sp.pub.FindSalePriceBO_Client.askBaseprices(saleprices);
					}
				}catch(Exception e){
					SCMEnv.out(e.getMessage());
				}

				if (rvos==null || rvos.length==0)
					continue;

				ArrayList alinv=new ArrayList();
				for (int iinv=0;iinv<rvos.length;iinv++){
					if (rvos[iinv].getErrFlag().intValue()==0 && rvos[iinv].getNum()!=null && rvos[iinv].getNum().doubleValue()>0 )
						alinv.add(new Integer(iinv));
				}

				if (alinv.size()==0)
					continue;

				UFDouble npricefactor=(UFDouble)bm.getValueAt(i,"npricefactor");
				if (npricefactor==null)
					npricefactor=new UFDouble(0);
				UFDouble npriceadd=(UFDouble)bm.getValueAt(i,"npriceadd");
				if (npriceadd==null)
					npriceadd=new UFDouble(0);

				for (int iloop=0;iloop<alinv.size();iloop++){
					int iinv=((Integer)alinv.get(iloop)).intValue();

					DMDataVO dmvo=new DMDataVO();

					GroupInventoryVO basvo=(GroupInventoryVO)manvos[iinv].getParentVO();
					if ("0001".equals(getCorpPrimaryKey())){
						//���������ID
						dmvo.setAttributeValue("cinventoryid",basvo.getPk_invbasdoc());
					}else{
						//���������ID
						CorpInventoryVO manvo=(CorpInventoryVO)manvos[iinv].getCorpInventoryVO();
						dmvo.setAttributeValue("cinventoryid",manvo.getPk_invmandoc());
					}
					dmvo.setAttributeValue("cinvbasdocid",basvo.getPk_invbasdoc());
					String invname="";
					invname = (basvo.getInvcode()==null?"":basvo.getInvcode());
					invname = invname + (basvo.getInvname()==null?"":"/"+basvo.getInvname());
					invname = invname + (basvo.getInvspec()==null?"":"/"+basvo.getInvspec());
					invname = invname + (basvo.getInvtype()==null?"":"/"+basvo.getInvtype());

					UFDouble baseprice=rvos[iinv].getNum();
					if (baseprice==null)
						baseprice=new UFDouble(0);

					dmvo.setAttributeValue("invname",invname);
					//dmvo.setAttributeValue("unit",units[iloop]);
					dmvo.setAttributeValue("orgprice",baseprice);
					UFDouble newprice=baseprice.multiply(npricefactor);
					newprice=newprice.add(npriceadd);
					dmvo.setAttributeValue("newprice",newprice);
					dmvo.setAttributeValue("pricetype",(String)bm.getValueAt(i,"sourceprice"));
					al.add(dmvo);
				}
			}
			catch(Exception e){
				SCMEnv.out(e.getMessage());
			}
		}
	}

	if (al.size()==0){
		showWarningMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("scmsoprice","UPPscmsoprice-000294")/*@res "û�з��������Ĵ����Ϣ���޷��鿴��"*/);
		return;
	}
	DMDataVO[] dmInvs=new DMDataVO[al.size()];
	al.copyInto(dmInvs);
//dlgAnalyse=null;	
	if (dlgAnalyse==null)
		dlgAnalyse=new AdjustPrmAnalysis(this.getParent());
	dlgAnalyse.setInvDMVO(dmInvs);
	dlgAnalyse.setCorpPrimaryKey(getCorpPrimaryKey());
	dlgAnalyse.clearData();

	dlgAnalyse.show();

}

/**
 * �������صĵ�������û�гɹ�ѯ�۲�����Ӱ�칫˾
 * @param vo
 */
private String ifSuccessFindPrice(AdjustpriceVO vo){
	AdjustpriceHeaderVO header = (AdjustpriceHeaderVO) vo.getParentVO();
	AdjustpriceItemVO[] items = (AdjustpriceItemVO[]) vo.getChildrenVO();
	String msg = null;
	for (int i = 0; i < items.length; i++) {
		UFBoolean brelatebase = (UFBoolean) items[i].getAttributeValue("brelatebase");
		if (brelatebase.booleanValue()) {
			if (items[i].getStatus() == VOStatus.NEW || items[i].getStatus() == VOStatus.UPDATED) {
				if (items[i].getBinqusucess() == null || !items[i].getBinqusucess().booleanValue()){
				msg = nc.ui.ml.NCLangRes.getInstance().getStrByID(
								"scmsoprice", "UPPscmsoprice-000346", null,
								new String[] { String.valueOf((i + 1)) })/*
																			 * @res
																			 * "��{0}�У�û�гɹ�ѯ�ۣ����ܱ��棡"
																			 */;
				}
			}
		}
	}
	return msg;
}

/**
 * Ӱ�칫˾��
 * �������ڣ�(2001-6-15 9:45:18)
 */
public void onAffectCorp() {
	if (!"0001".equals(getCorpPrimaryKey())){
		showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("scmsoprice","UPPscmsoprice-000295")/*@res "ֻ�м��ŵ���ʱ����Ӱ��������˾!"*/);
		return;
	}
	
	//v5.02�������صĵ�������û�гɹ�ѯ�۲�����Ӱ�칫˾//////////////////////////
	voAdjustprice =
		(AdjustpriceVO) getBillCardPanel().getBillValueChangeVO(
			"nc.vo.sp.sp002.AdjustpriceVO",
			"nc.vo.sp.sp002.AdjustpriceHeaderVO",
			"nc.vo.sp.sp002.AdjustpriceItemVO");
	String errormsg = null;
	if (( errormsg = ifSuccessFindPrice(voAdjustprice))!=null){
		showErrorMessage(errormsg);
		return;
	}
	//v5.02�������صĵ�������û�гɹ�ѯ�۲�����Ӱ�칫˾//////////////////////////
	
	if (dlgAffetCorp==null){
		dlgAffetCorp=new InfectionCorp(this.getParent());
	}

	boolean canEdit=false;
	if (iStatus==UPDATED || iStatus==NEW ){
		canEdit=true;
	}

	String key=null;
	AdjustpriceVO vo=null;
	//ȡ�����ѡ��
	if (isList){
		int selrow=-1;
		if (getBillListPanel().getHeadTable().getSelectedRow() > -1) {
			selrow=getBillListPanel().getHeadTable().getSelectedRow();
		}else{
			selrow=getBillListPanel().getHeadBillModel().getEditRow();
		}
		if (selrow==-1)
			return;
		key=(String)getBillListPanel().getHeadBillModel().getValueAt(selrow,"cadjpriceid");
		//vo=(AdjustpriceVO)getBillListPanel().getBillValueVO(selrow,"nc.vo.sp.sp002.AdjustpriceVO","nc.vo.sp.sp002.AdjustpriceHeaderVO","nc.vo.sp.sp002.AdjustpriceItemVO");
	}
	else{
		key=getBillCardPanel().getHeadItem("cadjpriceid").getValue();
		//vo=(AdjustpriceVO)getBillCardPanel().getBillValueVO("nc.vo.sp.sp002.AdjustpriceVO","nc.vo.sp.sp002.AdjustpriceHeaderVO","nc.vo.sp.sp002.AdjustpriceItemVO");
	}

	if (affectvos==null || affectvos.length==0){
		if (!(iStatus==NEW) ){
			//ȡ�����ѡ��
			try{
				affectvos=nc.ui.sp.sp002.AdjustpriceBO_Client.getAffectCorpByKey(key);
			}catch(Exception e){
				SCMEnv.out(e.getMessage());
				showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("scmsoprice","UPPscmsoprice-000296")/*@res "��ѯԭӰ�칫˾�����쳣!"*/);
				return;
			}
		}
		else{
			try{
				//ȡȱʡ������
				affectvos=nc.ui.sp.sp002.AdjustpriceBO_Client.getAffectCorpByKey(null);
			}catch(Exception e){
				SCMEnv.out(e.getMessage());
				showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("scmsoprice","UPPscmsoprice-000297")/*@res "��ѯȱʡӰ�칫˾�����쳣!"*/);
				return;
			}
		}
	}
	if (key!=null && key.trim().length()>0){
		try{
			vo = AdjustpriceBO_Client.findByPrimaryKey(key);
		}catch(Exception e){
			SCMEnv.out(e.getMessage());
			showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON","UPPSCMCommon-000033",null,new String[]{e.getMessage()})/*@res "��ѯ����{0}"*/);
		}
		//״̬Ϊ����, ������������û�е���Чʱ��Ҳ�����޸�
		if (vo!=null){
			AdjustpriceHeaderVO headvo=(AdjustpriceHeaderVO)vo.getParentVO();
			if (headvo.getFstatus().intValue()==1){
				canEdit=true;
			}
			else if (headvo.getFadjtype() == AdjustpriceVO.BASE_PRICE || headvo.getFadjtype() == AdjustpriceVO.BASE_DISCOUNT){
				if(headvo.getDvalidate().after(ce.getDate())){
					canEdit=true;
				}
			}
		}
	}

	dlgAffetCorp.setEnabled(canEdit);
	dlgAffetCorp.setLastResultVO(affectvos);
	dlgAffetCorp.initSelected();
	if (dlgAffetCorp.showModal()!=UIDialog.ID_OK){
		return;
	}
	if (iStatus==UPDATED || iStatus==NEW ){
		affectvos=dlgAffetCorp.getResultVO();
	}
	else if (canEdit){
		//����״̬�����²���
		if (key!=null){
			try{
				nc.ui.sp.sp002.AdjustpriceBO_Client.insertAffectCorp(key,dlgAffetCorp.getResultVO());
				affectvos=dlgAffetCorp.getResultVO();
			}catch(Exception e){
				SCMEnv.out(e.getMessage());
				showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON","UPPSCMCommon-000011",null,new String[]{e.getMessage()})/*@res "����ʧ�ܣ�{0}"*/);
				return;
			}
		}
	}
}

/**
 * ����
 * xiegx 2004-12-28
 */
@SuppressWarnings("deprecation")
public void onSendAudit(){

	 try {
	    if (!isExistAuditFlow()){
	    	showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("scmsoprice","UPPscmsoprice-000298")/*@res "�ò���Աû������������!"*/);
		 	return;
	    }
	 }catch(Exception e){
	 	SCMEnv.out(e.getMessage());
	 	showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("scmsoprice","UPPscmsoprice-000352",null,new String[]{e.getLocalizedMessage()})/*@res "��ѯ�����Ƿ�������������������:{0}"*/);
	 	return;
	 }

	//�༭״̬���ȱ���������
	if (iStatus==NEW || iStatus==UPDATED){
		getBillCardPanel().tableStopCellEditing();
		cleanNullLine();
		voAdjustprice =
			(AdjustpriceVO) getBillCardPanel().getBillValueChangeVO(
				"nc.vo.sp.sp002.AdjustpriceVO",
				"nc.vo.sp.sp002.AdjustpriceHeaderVO",
				"nc.vo.sp.sp002.AdjustpriceItemVO");
		long time = System.currentTimeMillis();
		voAdjustprice.setAffectVO(affectvos);

		try {
			checkHeaderData(voAdjustprice);
			voAdjustprice.setStatus(BillStatus.FREE);
			voAdjustprice.validate();
			checkData(voAdjustprice);

			checkUnique(voAdjustprice);

			String pk = null;
			TransUniBillVO unibillvo=null;
			AdjustpriceVO allvo=(AdjustpriceVO) getBillCardPanel().getBillValueVO("nc.vo.sp.sp002.AdjustpriceVO",
					"nc.vo.sp.sp002.AdjustpriceHeaderVO",
					"nc.vo.sp.sp002.AdjustpriceItemVO");
			
			if (iStatus == NEW) {
				unibillvo =
					(TransUniBillVO) PfUtilClient.processActionNoSendMessage(this,
						"SoSaveing",
						getBillType(),
						getClientEnvironment().getDate().toString(),
						voAdjustprice,
						null,null,null);
				pk = ((TransUniBillHeadVO)unibillvo.getParentVO()).getM_billid();
			} else if (iStatus == UPDATED) {
				pk = voAdjustprice.getPrimaryKey();
				if (m_bcodechanged.booleanValue()){
					((AdjustpriceHeaderVO)voAdjustprice.getParentVO()).setAttributeValue("bcodechanged",m_bcodechanged);
					((AdjustpriceHeaderVO)voAdjustprice.getParentVO()).setoldCadjpriceno(m_oldcadjpriceno);
				}
				unibillvo =
					(TransUniBillVO)PfUtilClient.processActionNoSendMessage(this,
					"SoEditing",
					getBillType(),
					getClientEnvironment().getDate().toString(),
					voAdjustprice,
					null,null,null);
				

			}
			int[] rows=unisrv.getAllRows(allvo.getChildrenVO(), unibillvo.getChildrenVO());
			unisrv.autoFillResultToUI("36",unibillvo,getBillCardPanel(),rows);
			try{
				voAdjustprice = (AdjustpriceVO) getBillCardPanel().getBillValueVO("nc.vo.sp.sp002.AdjustpriceVO",
						"nc.vo.sp.sp002.AdjustpriceHeaderVO",
				"nc.vo.sp.sp002.AdjustpriceItemVO");
				PfUtilClient.processAction(
						"SAVE",
						getBillType(),
						getClientEnvironment().getDate().toString(),
						voAdjustprice);
				voAdjustprice = AdjustpriceBO_Client.findByPrimaryKey(pk);
				getBillCardPanel().setBillValueVO(voAdjustprice);
				getBillCardPanel().execHeadLoadFormulas();
				getBillCardPanel().getBillModel().execLoadFormula();
			}catch(Exception ex1){
				SCMEnv.out(ex1);
				showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("scmsoprice","UPPscmsoprice-000749")/*@res "����ɹ�������û�гɹ�����Ҫ��������"*/);
			}
			time = System.currentTimeMillis() - time;

			getBillCardPanel().updateValue();
			getBillCardPanel().setEnabled(false);

			int iNewStatus =
				((AdjustpriceHeaderVO) voAdjustprice.getParentVO()).getFstatus().intValue();

			if (iNewStatus == 1)
				iStatus = FREE;
			else if (iNewStatus == 2)
				iStatus = AUDITED;
			else if (iNewStatus == 7)
				iStatus = AUDITING;


			setBnStatus(iStatus);

			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("scmsoprice","UPPscmsoprice-000750",null,new String[]{String.valueOf(time/1000.0)})/*@res "����ɹ�[����ʱ{0}��]"*/);

		} catch (Exception e) {
			showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("scmsoprice","UPPscmsoprice-000751",null,new String[]{e.getMessage()})/*@res "����ʧ�ܣ�{0}"*/);
			SCMEnv.error(e);
		}
	}
	else{
		try {
			if (!isList) {
				voAdjustprice =
					(AdjustpriceVO) getBillCardPanel().getBillValueVO(
						"nc.vo.sp.sp002.AdjustpriceVO",
						"nc.vo.sp.sp002.AdjustpriceHeaderVO",
						"nc.vo.sp.sp002.AdjustpriceItemVO");


				//����
				PfUtilClient.processAction(
						"SAVE",
						getBillType(),
						getClientEnvironment().getDate().toString(),
						voAdjustprice);

				//modify by zxj 20030911
				String pk =
					((AdjustpriceHeaderVO) voAdjustprice.getParentVO()).getCadjpriceid();
				voAdjustprice = AdjustpriceBO_Client.findByPrimaryKey(pk);
				//
				getBillCardPanel().setBillValueVO(voAdjustprice);
				getBillCardPanel().getBillModel().execLoadFormula();
				getBillCardPanel().execHeadLoadFormulas();
				getBillCardPanel().updateValue();

				//by zxj 0918
				int iNewStatus =
					((AdjustpriceHeaderVO) voAdjustprice.getParentVO()).getFstatus().intValue();
				if (iNewStatus == BillStatus.NOPASS)
					iStatus = FREE;
				else if (iNewStatus == BillStatus.AUDIT)
					iStatus = AUDITED;
				else if (iNewStatus == BillStatus.AUDITING)
					iStatus = AUDITING;
				setBnStatus(iStatus);
			} else {
				int iRow = getBillListPanel().getHeadTable().getSelectedRow();
				String pk =
					getBillListPanel()
						.getHeadBillModel()
						.getValueAt(iRow, "cadjpriceid")
						.toString();
				voAdjustprice = AdjustpriceBO_Client.findByPrimaryKey(pk);
				PfUtilClient.processAction(
						"SAVE",
						getBillType(),
						getClientEnvironment().getDate().toString(),
						voAdjustprice);


				//modify by zxj 20030911
				voAdjustprice = AdjustpriceBO_Client.findByPrimaryKey(pk);
				int iNewStatus =
					((AdjustpriceHeaderVO) voAdjustprice.getParentVO()).getFstatus().intValue();
				//

				if (iNewStatus == BillStatus.NOPASS)
					getBillListPanel().getHeadBillModel().setValueAt(nc.ui.ml.NCLangRes.getInstance().getStrByID("scmsoprice","UPPscmsoprice-000299")/*@res "������ͨ��"*/, iRow, "fstatus");
				else if (iNewStatus == BillStatus.AUDIT)
					getBillListPanel().getHeadBillModel().setValueAt(nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UC001-0000027")/*@res "����"*/, iRow, "fstatus");
				else if (iNewStatus == BillStatus.AUDITING)
					getBillListPanel().getHeadBillModel().setValueAt(nc.ui.ml.NCLangRes.getInstance().getStrByID("scmsoprice","UPPscmsoprice-000289")/*@res "��������"*/, iRow, "fstatus");
//				getBillListPanel().getHeadBillModel().setValueAt(
//					ce.getDate(),
//					iRow,
//					"dauditdate");

				if (iNewStatus == BillStatus.NOPASS)
					iStatus = FREE;
				else if (iNewStatus == BillStatus.AUDIT)
					iStatus = AUDITED;
				else if (iNewStatus == BillStatus.AUDITING)
					iStatus = AUDITING;
				setBnStatus(iStatus);
			}
			iOldStatus = iStatus;

			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("scmsoprice","UPPscmsoprice-000750")/*@res "����ɹ���"*/);
		} catch (Exception e) {
			//showErrorMessage("�������� �õ��Ѿ�������Ч������δͨ��");
			showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("scmsoprice","UPPscmsoprice-000751",null,new String[]{e.getMessage()})/*@res "����ʧ�ܣ�{0}"*/);
			SCMEnv.out(e.getMessage());
		}
	}
}

/**
 * ����������
 * �������ڣ�(2001-6-15 9:45:18)
 */
public void onAudit() {
	showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
				"SCMCommon", "UPPSCMCommon-000319")/* @res �������������Ժ�... */);
	try {
		AdjustpriceHeaderVO[] vaAdjustpriceHeader = null;
		if (!isList) {
			voAdjustprice =
				(AdjustpriceVO) getBillCardPanel().getBillValueVO(
					"nc.vo.sp.sp002.AdjustpriceVO",
					"nc.vo.sp.sp002.AdjustpriceHeaderVO",
					"nc.vo.sp.sp002.AdjustpriceItemVO");

			//modify by zxj 20030820

			//����������־
			voAdjustprice.setStatus(BillStatus.AUDIT);
			//����������
			((AdjustpriceHeaderVO) voAdjustprice.getParentVO()).setCauditorid(
				ce.getUser().getPrimaryKey());
			//������������
			((AdjustpriceHeaderVO) voAdjustprice.getParentVO()).setDauditdate(ce.getDate());
			checkAuditData(voAdjustprice);
			/**
			 * �޸ģ�������
			 * ���ڣ�2009-9-3
			 * CQ�ţ�NCdp200978803
			 * ԭ�򣺰�Ψһ��У��͡������������ϲ���һ������
			 */
			
			//����ؼ۵�Ψһ��
//			AdjustpriceBO_Client.beforeAuditCheck(voAdjustprice);
			//��Ϊ������
			PfUtilClient.processActionFlow(
				this,
				"APPROVE",
				getBillType(),
				getClientEnvironment().getDate().toString(),
				voAdjustprice,
				null);
			//
			//audit(voAdjustprice, true);

			//modify by zxj 20030911
			String pk =
				((AdjustpriceHeaderVO) voAdjustprice.getParentVO()).getCadjpriceid();
			voAdjustprice = AdjustpriceBO_Client.findByPrimaryKey(pk);
			//
			/**
			 * �޸ģ�������
			 * ���ڣ�2009-9-3
			 * CQ�ţ�NCdp200978809
			 * ԭ�򣺲�������������VO,��������ÿ�����ı���ֶΣ�Ҳ����ִ�й�ʽ������������
			 */
			updateChangedItemValuesAfterAuditOrUnAudit();
			/**
			 * �޸ģ�������
			 * ���ڣ�2009-9-3
			 * CQ�ţ�NCdp200978803
			 * ԭ��ȡ����ʽ��ִ��
			 */
//			getBillCardPanel().setBillValueVO(voAdjustprice);
//			getBillCardPanel().getBillModel().execLoadFormula();
//			getBillCardPanel().execHeadLoadFormulas();
			getBillCardPanel().updateValue();

			for (int i=0;i<getBillListPanel().getHeadBillModel().getRowCount();i++){
				if (pk.equals(getBillListPanel().getHeadBillModel().getValueAt(i, "cadjpriceid") )){
					getBillListPanel().getHeadBillModel().setValueAt(ce.getUser().getUserName(), i, "caduitorname");
					getBillListPanel().getHeadBillModel().setValueAt(((AdjustpriceHeaderVO) voAdjustprice.getParentVO()).getDaudittime(), i, "daudittime");
				}
			}
			
			//by zxj 0918
			int iNewStatus =
				((AdjustpriceHeaderVO) voAdjustprice.getParentVO()).getFstatus().intValue();
			if (iNewStatus == BillStatus.NOPASS)
				iStatus = FREE;
			else if (iNewStatus == BillStatus.AUDIT)
				iStatus = AUDITED;
			else if (iNewStatus == BillStatus.AUDITING)
				iStatus = AUDITING;
			setBnStatus(iStatus);
		} else {
			int iRow = getBillListPanel().getHeadTable().getSelectedRow();
			if(iRow<0){
				showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("scmsoprice","UPPSCMSOPRICE-000101"));
				return;
			}
			String pk =
				getBillListPanel()
					.getHeadBillModel()
					.getValueAt(iRow, "cadjpriceid")
					.toString();
			voAdjustprice = AdjustpriceBO_Client.findByPrimaryKey(pk);

			voAdjustprice.getParentVO().setAttributeValue("ts",new UFDateTime(getBillListPanel().getHeadBillModel().getValueAt(iRow, "ts").toString()));
			
			//modify by zxj 20030820

			//����������־
			voAdjustprice.setStatus(BillStatus.AUDIT);
			//����������
			((AdjustpriceHeaderVO) voAdjustprice.getParentVO()).setCauditorid(
				ce.getUser().getPrimaryKey());
			//������������
			((AdjustpriceHeaderVO) voAdjustprice.getParentVO()).setDauditdate(ce.getDate());
			checkAuditData(voAdjustprice);
			/**
			 * �޸ģ�������
			 * ���ڣ�2009-9-3
			 * CQ�ţ�NCdp200978803
			 * ԭ�򣺰�Ψһ��У��͡������������ϲ���һ������
			 */
			//����ؼ۵�Ψһ��
//			AdjustpriceBO_Client.beforeAuditCheck(voAdjustprice);

			voAdjustprice.getParentVO().setAttributeValue("ts",new UFDateTime(getBillListPanel().getHeadBillModel().getValueAt(iRow, "ts").toString()));
			
			
			PfUtilClient.processActionFlow(
				this,
				"APPROVE",
				getBillType(),
				getClientEnvironment().getDate().toString(),
				voAdjustprice,
				null);
			//
			//audit(voAdjustprice, true);

			
			//modify by zxj 20030911
			voAdjustprice = AdjustpriceBO_Client.findByPrimaryKey(pk);
			
			getBillListPanel().getHeadBillModel().setValueAt(voAdjustprice.getParentVO().getAttributeValue("ts"), iRow, "ts");
			getBillListPanel().getHeadBillModel().setValueAt(((AdjustpriceHeaderVO) voAdjustprice.getParentVO()).getDaudittime(), iRow, "daudittime");
			int iNewStatus =
				((AdjustpriceHeaderVO) voAdjustprice.getParentVO()).getFstatus().intValue();
			//

			if (iNewStatus == BillStatus.NOPASS)
				getBillListPanel().getHeadBillModel().setValueAt(nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON","UPPSCMCommon-000242")/*@res "����δͨ��"*/, iRow, "fstatus");
			else if (iNewStatus == BillStatus.AUDIT){
				getBillListPanel().getHeadBillModel().setValueAt(nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UC001-0000027")/*@res "����"*/, iRow, "fstatus");
				getBillListPanel().getHeadBillModel().setValueAt(ce.getUser().getUserName(), iRow, "caduitorname");
			}
			else if (iNewStatus == BillStatus.AUDITING)
				getBillListPanel().getHeadBillModel().setValueAt(nc.ui.ml.NCLangRes.getInstance().getStrByID("scmsoprice","UPPscmsoprice-000289")/*@res "��������"*/, iRow, "fstatus");
			getBillListPanel().getHeadBillModel().setValueAt(
				ce.getDate(),
				iRow,
				"dauditdate");

			if (iNewStatus == BillStatus.NOPASS)
				iStatus = FREE;
			else if (iNewStatus == BillStatus.AUDIT)
				iStatus = AUDITED;
			else if (iNewStatus == BillStatus.AUDITING)
				iStatus = AUDITING;
			setBnStatus(iStatus);
		}
		iOldStatus = iStatus;
		//iStatus = AUDITED;
		//setBnStatus(iStatus);

		showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("scmsoprice","UPPscmsoprice-000300")/*@res "�����ɹ���"*/);
	} catch (Exception e) {
		//showErrorMessage("�������� �õ��Ѿ�������Ч������δͨ��");
		showErrorMessage(NCLangRes.getInstance().getStrByID("SCMCOMMON","UPPSCMCommon-000241",null,new String[]{e.getMessage()})/*@res "����ʧ�ܣ�{0}"*/);
		SCMEnv.error(e);
	}
}
/**
 * �˴����뷽��˵����
 * ��������:������״̬
 * �������:
 * ����ֵ:
 * �쳣����:
 * ����:
 */
public void onAuditFlowStatus() {
	String pk = "";
	if (!isList) {
		pk = getBillCardPanel().getHeadItem("cadjpriceid").getValue();

	} else {
		int iRow = getBillListPanel().getHeadTable().getSelectedRow();
		if (iRow<0){
			showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("scmsoprice","UPPscmsoprice-000301")/*@res "��ѡ�񵥾�!"*/);
			return;
		}
		pk =
			getBillListPanel()
				.getHeadBillModel()
				.getValueAt(iRow, "cadjpriceid")
				.toString();
	}

	if (pk == null) {
		showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON","UPPSCMCommon-000067")/*@res "���ݺ�Ϊ��"*/);
		return;
	} else {
		nc.ui.pub.workflownote.FlowStateDlg approvestatedlg =
			new nc.ui.pub.workflownote.FlowStateDlg(this, "36", pk);
		approvestatedlg.showModal();
	}

}
/**
 * ����ʵ�ָ÷�������Ӧ��ť�¼���
 * @version (00-6-1 10:32:59)
 *
 * @param bo ButtonObject
 */
public void onButtonClicked(nc.ui.pub.ButtonObject bo) {
	if(bnQuery == bo)
		onQuery();
	if(bnLocate == bo)
		onLocate();
	//if(bnAdd == bo)
		//onAdd();
	if(bnFirst == bo)
		onFirst();
	if(bnPrev == bo)
		onPrev();
	if(bnNext == bo)
		onNext();
	if(bnLast == bo)
		onLast();
	if(bnAddLine == bo)
		onAddLine();
	if(bnDelLine == bo)
		onDelLine();
	if(bnCancel == bo)
		onCancel();
	if (bnModifySpecialStopTime == bo) {
		onModifySpecialStopTime();
	}
	if(bnSave == bo)
		onSave();
	if(bnUpdate == bo)
		onUpdate();
	if(bnDelete == bo)
		onDelete();
	if(bnAudit == bo)
		onAudit();
	if(bnUnAudit == bo)
		onUnAudit();
	if(bnReturn == bo)
		onReturn();
	if(bnAddBase == bo){
		isAdjustBase = true;
		isAdjustDiscount = false;
		onAdd();
	}
	if(bnAddDiscount == bo) {
		isAdjustBase = true;
		isAdjustDiscount = true;
		onAdd();
	}
	if (bnAddSpec == bo){
		isAdjustBase=false;
		isAdjustDiscount = false;
		onAdd();
	}
	if (bnCopy == bo){
		onCopy();
	}
//    if(bnModifyBatch == bo)
//    	onModifyBatch();
    
	if(bnPrint == bo)
		onPrint(false);
	else if(bnPreview==bo){
		onPrint(true);
	}

	if (bnAffectCorp == bo){
		onAffectCorp();
	}
	if (bnAdjustAnalyse == bo){
		onAdjustAnalyse();
	}
	if (bnAuditFlowStatus == bo){
		onAuditFlowStatus();
	}
	if (boDocument == bo){
		onDocument();
	}
	if(bnSendAudit == bo)
		onSendAudit();

	if(bnAddLineBatch == bo)
		onAddLineBatch();
    if (boAuditMsg == bo)
    	onAuditMsg();

}

	/**
	 * �޸Ĵ���������ֹʱ�䣬����������ǰ��ֹ��������
	 * 
	 * @author ��ǿ��
	 * @since 2008-8-20
	 */
	private void onModifySpecialStopTime() {
		AdjustpriceHeaderVO headerVO = getCurHeadVO();
		if (headerVO.getStopDateTime().before(ClientEnvironment.getServerTime())) {
			showErrorMessage(NCLangRes.getInstance().getStrByID("403003", "UPT403003-000051"));//���������Ѿ�ʧЧ��������ֹ
			bnModifySpecialStopTime.setEnabled(false);
			updateButton(bnModifySpecialStopTime);
			return;
		}
		SpecialStopTimeDlg specialStopTimeDlg = new SpecialStopTimeDlg(this);
		specialStopTimeDlg.setDateTime(ClientEnvironment.getServerTime());
		if (UIDialog.ID_OK == specialStopTimeDlg.showModal()) {
			UFDateTime stopTime = specialStopTimeDlg.getDateTime();
			if (!stopTime.after(headerVO.getStartDateTime())) {
				SCMEnv.error("ʧЧʱ����������Чʱ��");
				showErrorMessage(NCLangRes4VoTransl.getNCLangRes().getStrByID("scmsoprice","UPPscmsoprice-000269"));// ʧЧʱ����������Чʱ��
				return;
			}
			try {
				AdjustpriceBO_Client.modifySpecialStopTime(headerVO.getCadjpriceid(), stopTime);

				headerVO.setDexpiredate(stopTime.getDate());
				headerVO.setDtstoptime(stopTime.getUFTime());

				setCurHeadVO(headerVO);

				getBillCardPanel().execHeadEditFormulas();
			} catch (BusinessException e) {
				SCMEnv.error(e);
				showErrorMessage(e.getMessage());
			}
		}
	}

	/**
	 * ��õ�ǰ���ڴ���ĵ��۵�����VO����Ƭ���ǵ�ǰ��ʾ��VO��
	 * �б����ǵ�ǰѡ�е�VO�����û��ѡ���򷵻�null
	 * 
	 * @return ��ǰ���ڴ���ĵ��۵�����VO
	 * @author ��ǿ��
	 * @since 2008-8-20
	 */
	private AdjustpriceHeaderVO getCurHeadVO() {
		AdjustpriceHeaderVO headerVO = null;
		if (isList) {
			int row = getBillListPanel().getHeadTable().getSelectedRow();
			if (-1 != row) {
				headerVO = (AdjustpriceHeaderVO) getBillListPanel().getHeadBillModel().getBodyValueRowVO(row, AdjustpriceHeaderVO.class.getName());
			}
		} else {
			headerVO = (AdjustpriceHeaderVO) getBillCardPanel().getBillData().getHeaderValueVO(AdjustpriceHeaderVO.class.getName());
		}
		return headerVO;
	}

	/**
	 * ���õ�ǰ����ĵ��۵�����VO����Ƭ���ǵ�ǰ��ʾ��VO��
	 * �б����ǵ�ǰѡ�е�VO�����û��ѡ���򱻺���
	 * 
	 * @param headerVO ���۵�����VO
	 * @author ��ǿ��
	 * @since 2008-8-20
	 */
	private void setCurHeadVO(AdjustpriceHeaderVO headerVO) {
		if (isList) {
			int row = getBillListPanel().getHeadTable().getSelectedRow();
			if (-1 != row) {
				getBillListPanel().getHeadBillModel().setBodyRowVO(headerVO, row);
			}
		} else {
			getBillCardPanel().getBillData().setHeaderValueVO(headerVO);
		}
	}
	

/**
 * ���ش��ڹر�ǰ�ķ���
 * ����ƬΪ�޶�״̬ʱ����ʾ�Ƿ���Ҫ����
 */
public boolean onClosing() {
	int state=nc.ui.pub.beans.UIDialog.ID_NO;
	
		if((iStatus == NEW || iStatus == UPDATED)){

			state=showYesNoCancelMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCommon", "UPPSCMCommon-000469")
					/*@res 
					 * �Ƿ���Ҫ����
					 */);
		}

	if(nc.ui.pub.beans.UIDialog.ID_YES==state){
		//����Ҫ����ʱ
		if(onSave())return true;
		//������Ϻ󷵻�true
		return false;
	}
	else if(nc.ui.pub.beans.UIDialog.ID_NO==state){
		return true;
	}
	else if (nc.ui.pub.beans.UIDialog.ID_CANCEL==state){
		return false;
	}
	else return true;
	
}
/**
 * ����������
 * �������ڣ�(2001-4-20 15:55:06)
 */
public void onCancel() {
	this.showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCommon","UPPSCMCommon-000204")/*
             * @res
             * "ȡ���༭"
             */);
	getBillCardPanel().resumeValue();
//	iStatus = iOldStatus;
//	setBnStatus(iStatus);
	setButton(iCurrRow);
	affectvos=null;
	//�ж�ȡ����õ��۷�ʽ�Ƿ�͸ղŵ÷�ʽ��ͬ
	Integer fadjtype = (Integer) getBillCardPanel().getHeadItem("fadjtype").getValueObject();
	if (null != fadjtype){
		boolean oldAdjustBase = false;
		boolean oldAdjustDiscount = false;
		if (fadjtype == AdjustpriceVO.BASE_PRICE) {
			oldAdjustBase = true;
			oldAdjustDiscount = false;
		} else if (fadjtype == AdjustpriceVO.BASE_DISCOUNT) {
			oldAdjustBase = true;
			oldAdjustDiscount = true;
		} else {
			oldAdjustBase = false;
			oldAdjustDiscount = false;
		}
		if (isAdjustBase != oldAdjustBase || isAdjustDiscount != oldAdjustDiscount) {
			isAdjustBase = oldAdjustBase;
			isAdjustDiscount = oldAdjustDiscount;
			remove(getBillCardPanel());
			setBodyCol();
			add(getBillCardPanel(), "Center");
		}
	}
	getBillCardPanel().setEnabled(false);
	getBillCardPanel().execHeadLoadFormulas();
	getBillCardPanel().getBillModel().execLoadFormula();
	

	//
	//setButton(iCurrRow);
	
}
/**
 * ���Ʋ�����
 * �������ڣ�(2004-2-06)
 */
public void onCopy() {
	if (iStatus==NEW || iStatus==UPDATED){
		showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("scmsoprice","UPPscmsoprice-000302")/*@res "��ʾ: �������޸�״̬�²��ܸ���!"*/);
		return;
	}


	AdjustpriceVO oldvo=null;
	int selrow=-1;
	//��ȡ��ǰ�ɸ��Ƶ�VO
	if (isList){
		if (getBillListPanel().getHeadTable().getSelectedRow() > -1) {
			selrow=getBillListPanel().getHeadTable().getSelectedRow();
		}else{
			selrow=getBillListPanel().getHeadTable().getEditingRow();
		}
		if (selrow>-1)
			try{
			oldvo = AdjustpriceBO_Client.findByPrimaryKey(getBillListPanel().getHeadBillModel().getValueAt(selrow, "cadjpriceid").toString());
			}catch(Exception e){
				SCMEnv.out(e.getMessage());
				
			}
			//oldvo=(AdjustpriceVO)getBillListPanel().getBillValueVO(selrow,"nc.vo.sp.sp002.AdjustpriceVO",
			//"nc.vo.sp.sp002.AdjustpriceHeaderVO",
			//"nc.vo.sp.sp002.AdjustpriceItemVO");
		else
			return;
	}else{
		oldvo =
		(AdjustpriceVO) getBillCardPanel().getBillValueVO(
			"nc.vo.sp.sp002.AdjustpriceVO",
			"nc.vo.sp.sp002.AdjustpriceHeaderVO",
			"nc.vo.sp.sp002.AdjustpriceItemVO");
	}
	if (oldvo==null ){
		return;
	}
	AdjustpriceHeaderVO oldparvo=(AdjustpriceHeaderVO)oldvo.getParentVO();
	//if (oldparvo.getFstatus().intValue()!=2){
		//showErrorMessage("ֻ��������ļ�¼���ܽ��и��Ʊ���!");
		//return;
	//}
	if (oldvo.getChildrenVO().length==0){
		try{
			AdjustpriceItemVO[] items=AdjustpriceBO_Client.queryBody(oldparvo.getPrimaryKey());
			if (items==null || items.length==0){
				showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("scmsoprice","UPPscmsoprice-000303")/*@res "���۵�������Ϊ�գ����ܽ��и��ƣ�"*/);
				return;
			}
			oldvo.setChildrenVO(items);
		}catch(Exception e){
			return;
		}
	}


	//����һ��
	onAdd();

	//��������

	oldparvo.setCadjpriceno(null);
	oldparvo.setDvalidate(oldparvo.getDvalidate());
	oldparvo.setDadjpricedate(ce.getDate());
	oldparvo.setPrimaryKey(null);
	oldparvo.setPk_corp(getCorpPrimaryKey());
	oldparvo.setCadjusterid(ce.getUser().getPrimaryKey());
	oldparvo.setDauditdate(null);
	oldparvo.setCauditorid(null);
	oldparvo.setCadjpricedocno(null);
	oldparvo.setDaudittime(null);
	oldparvo.setDbilltime(null);
	oldparvo.setDmoditime(null);
	//����״̬Ϊ����
	oldparvo.setFstatus(new Integer(1));

	AdjustpriceItemVO[] oldchildvos=(AdjustpriceItemVO[])oldvo.getChildrenVO();
	for(int i=0;i<oldchildvos.length;i++){
		oldchildvos[i].setPrimaryKey(null);
		oldchildvos[i].setCadjpriceid(null);
	}

	getBillCardPanel().setBillValueVO(oldvo);
	getBillCardPanel().execHeadLoadFormulas();
	getBillCardPanel().getBillModel().execLoadFormula();

	iStatus=NEW;

	getBillCardPanel().updateUI();
	updateUI();

	/*
	 * �޸ģ���ǿ��
	 * ���ڣ�2009-2-26
	 * CQ�ţ�NCdp200731827
	 * ԭ�򣺶��ڵ������ĵ��۵������ƺ��������ѯ��
	 */
	if (oldparvo.getFadjtype() == AdjustpriceVO.BASE_PRICE || oldparvo.getFadjtype() == AdjustpriceVO.BASE_DISCOUNT) {
		if (showYesNoMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("scmsoprice","UPPscmsoprice-000304")/*@res "�Ƿ�����ѯ�ۣ�"*/)==UIDialog.ID_YES){
//			for(int i=0;i<getBillCardPanel().getRowCount();i++){
//				findPriceBase("code",i);
//				autoCalcBase("code",i);
//			}
			/**
			 * �޸ģ�������
			 * ���ڣ�2009-9-4
			 * CQ�ţ�NCdp200979075
			 * ԭ��������ѯ��
			 */
			
			findPriceBase2(getBillCardPanel().getRowCount() - 1);
			for(int i=0;i<getBillCardPanel().getRowCount();i++){
				autoCalcBase("code",i);
			}
		} else {
			for(int i=0;i<getBillCardPanel().getRowCount();i++){
				getBillCardPanel().getBillModel().setValueAt(new UFBoolean(true),i,"binqusucess");
			}
		}
	} else {
		/**
		 * �޸ģ�������
		 * ���ڣ�2009-9-7
		 * CQ�ţ�NCdp200978982
		 * ԭ�������д���ѯ��
		 */
		findPromotionPricesBatch2(getBillCardPanel().getRowCount() - 1);
		for(int i=0;i<getBillCardPanel().getRowCount();i++){
//			findPrice("code",i);
			autoCalc("code",i);
		}
	}

	//�ټ���һ��
//	if (showYesNoMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("scmsoprice","UPPscmsoprice-000304")/*@res "�Ƿ�����ѯ�ۣ�"*/)==UIDialog.ID_YES){
//		for(int i=0;i<getBillCardPanel().getRowCount();i++){
//			if (oldparvo.getFadjtype() == AdjustpriceVO.BASE_PRICE || oldparvo.getFadjtype() == AdjustpriceVO.BASE_DISCOUNT){
//				findPriceBase("code",i);
//				autoCalcBase("code",i);
//			}
//			else{
//				findPrice("code",i);
//				autoCalc("code",i);
//			}
//		}
//	}
//	else if (oldparvo.getFadjtype() == AdjustpriceVO.BASE_PRICE || oldparvo.getFadjtype() == AdjustpriceVO.BASE_DISCOUNT){
//		for(int i=0;i<getBillCardPanel().getRowCount();i++){
//			getBillCardPanel().getBillModel().setValueAt(new UFBoolean(true),i,"binqusucess");
//		}
//	}

}
/**
 * �޸ģ�������
 * ���ڣ�2009-9-7
 * CQ�ţ�NCdp200978982
 * ԭ������ѯ�ۣ�����������
 */
private void findPromotionPricesBatch2(int maxRow) {
	if (maxRow < 0)
		return;
	Hashtable<Integer,SalePriceVO> salepriceVOHT = new Hashtable<Integer,SalePriceVO>();
	Hashtable<Integer,SpecialwarepriceVO> arySpecialwarepriceVOHT = new Hashtable<Integer,SpecialwarepriceVO>();
	for(int i=0;i<=maxRow;i++){
//		salepriceVOs[i] = constructSalepriceVO(i);
		Boolean brelatebase = (Boolean) getBillCardPanel().getBodyValueAt(i, "brelatebase");
		if (brelatebase.booleanValue()) {
			salepriceVOHT.put(i, constructSalepriceVO(i));
		} else {
			arySpecialwarepriceVOHT.put(i, constructSpecialwarepriceVO(i));
		}
			
	}
	// ����ѯ�۸���
	// ��������
	try{
//		Hashtable<Integer, String> inventorySaleItemHT;
		Hashtable<Integer, String> saleItemHT = FindSalePriceBO_Client.findSaleItemBatch(salepriceVOHT, arySpecialwarepriceVOHT, ce.getDate());
//		inventorySaleItemHT = FindSalePriceBO_Client.findInventorySaleItemBatch(salepriceVOHT);
		for (Iterator iterator = saleItemHT.keySet().iterator(); iterator.hasNext(); ) {
			Integer integer = (Integer) iterator.next();
			SalePriceVO salepriceVO = salepriceVOHT.get(integer);
			SpecialwarepriceVO specialwarepriceVO = arySpecialwarepriceVOHT.get(integer);
			// ��������
			if(salepriceVO != null){
				if (salepriceVO.getPriceTypeid() == null) {
					getBillCardPanel().setBodyValueAt(saleItemHT.get(integer), integer, "csrcpriceid");
					getBillCardPanel().execBodyFormulas(integer, getBillCardPanel().getBodyItem("csrcpriceid").getLoadFormula());
					salepriceVO.setPriceTypeid(saleItemHT.get(integer));
					//}
					if (salepriceVO.getPriceTypeid() == null) {
						getBillCardPanel().getBillModel().setValueAt(null, integer, "nbaseprice");
						getBillCardPanel().getBillModel().setValueAt(null, integer, "noriginalprice");
					}
				}
			}else if (specialwarepriceVO != null){// ������޹�
				if (specialwarepriceVO.getCsourcepriceid() == null) {
					String cPricetypeid = saleItemHT.get(integer);
					if (cPricetypeid == null) {
						getBillCardPanel().getBillModel().setValueAt(null, integer, "nbaseprice");
						getBillCardPanel().getBillModel().setValueAt(null, integer, "noriginalprice");
						if(isEditCust==false)
						getBillCardPanel().getBillModel().setValueAt(null, integer, "nnewprice");
						return;
					}else{
						getBillCardPanel().setBodyValueAt(cPricetypeid, integer, "csrcpriceid");
						getBillCardPanel().execBodyFormulas(integer, getBillCardPanel().getBodyItem("csrcpriceid").getLoadFormula());
						specialwarepriceVO.setCsourcepriceid(cPricetypeid);
					}
				}
			}
		}
//		for (Iterator iterator = inventorySaleItemHT.keySet().iterator(); iterator.hasNext(); ) {
//			Integer integer = (Integer) iterator.next();
//			SalePriceVO salepriceVO = salepriceVOHT.get(integer);
//			if (salepriceVO.getPriceTypeid() == null) {
//				getBillCardPanel().setBodyValueAt(inventorySaleItemHT.get(integer), integer, "csrcpriceid");
//				getBillCardPanel().execBodyFormulas(integer, getBillCardPanel().getBodyItem("csrcpriceid").getLoadFormula());
//				salepriceVO.setPriceTypeid(inventorySaleItemHT.get(integer));
//				//}
//				if (salepriceVO.getPriceTypeid() == null) {
//					getBillCardPanel().getBillModel().setValueAt(null, integer, "nbaseprice");
//					getBillCardPanel().getBillModel().setValueAt(null, integer, "noriginalprice");
//				}
//			}
//		}
//		// ������޹�
////		Hashtable<Integer, String> orgSpecpriceItemHT = null;
////		orgSpecpriceItemHT = FindSalePriceBO_Client.getOrgSpecpriceItemFromspBatch(arySpecialwarepriceVOHT, ce.getDate());
//		if(orgSpecpriceItemHT != null){
//			for (Iterator iterator = orgSpecpriceItemHT.keySet().iterator(); iterator.hasNext(); ) {
//				Integer integer = (Integer) iterator.next();
//				SpecialwarepriceVO specialwarepriceVO = arySpecialwarepriceVOHT.get(integer);
//				if (specialwarepriceVO.getCsourcepriceid() == null) {
//					String cPricetypeid = orgSpecpriceItemHT.get(integer);
//					if (cPricetypeid == null) {
//						getBillCardPanel().getBillModel().setValueAt(null, integer, "nbaseprice");
//						getBillCardPanel().getBillModel().setValueAt(null, integer, "noriginalprice");
//						if(isEditCust==false)
//						getBillCardPanel().getBillModel().setValueAt(null, integer, "nnewprice");
//						return;
//					}else{
//						getBillCardPanel().setBodyValueAt(cPricetypeid, integer, "csrcpriceid");
//						getBillCardPanel().execBodyFormulas(integer, getBillCardPanel().getBodyItem("csrcpriceid").getLoadFormula());
//						specialwarepriceVO.setCsourcepriceid(cPricetypeid);
//					}
//				}
//			}
//		}

		// ����ѯ��
		Hashtable<Integer,Object> retHT = nc.ui.sp.pub.FindSalePriceBO_Client.findPromotionPricesBatch(salepriceVOHT, arySpecialwarepriceVOHT, ce.getDate());
		for (Iterator iterator = retHT.keySet().iterator(); iterator.hasNext(); ) {
			Integer integer = (Integer) iterator.next();
			Object element = retHT.get(integer);
			// ������޹�
			if(element instanceof UFDouble){
				UFDouble saleprice = (UFDouble)retHT.get(integer);
				getBillCardPanel().getBillModel().setValueAt(null, integer.intValue(), "nbaseprice");
				getBillCardPanel().getBillModel().setValueAt(saleprice, integer.intValue(), "noriginalprice");
				if(isEditCust==false)
				getBillCardPanel().getBillModel().setValueAt(saleprice, integer.intValue(), "nnewprice");
			}else if (element instanceof PriceAskResultVO){// ��������
				PriceAskResultVO rvo = (PriceAskResultVO)retHT.get(integer);
				UFDouble saleprice = null;
				if (rvo != null && rvo.getErrFlag().intValue() == 0) {
					saleprice = rvo.getNum();
					/*
					 * �޸ģ���ǿ��
					 * ���ڣ�2008-8-25
					 * ԭ�����ӻ�׼�ۿ���ʾ
					 */
					getBillCardPanel().getBillModel().setValueAt(rvo.getBaseDiscount(), integer.intValue(), "nbasediscount");
				} else if (rvo != null) {
					if (rvo.getErrFlag().intValue() == 1) {
					} else
						showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON","UPPSCMCommon-000296",null,new String[]{ rvo.getErrMessage()})/*@res "ѯ��ʧ�ܣ�{0}"*/ + rvo.getErrMessage());
				} else {
					showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("scmsoprice","UPPscmsoprice-000278")/*@res "ѯ�۷�������,û�з���ֵ��"*/);
				}
				getBillCardPanel().getBillModel().setValueAt(saleprice, integer.intValue(), "nbaseprice");
				getBillCardPanel().getBillModel().setValueAt(null, integer.intValue(), "noriginalprice");
				if (saleprice!=null)
					getBillCardPanel().getBillModel().setValueAt(new UFBoolean(true),integer.intValue(),"binqusucess");
				else
					getBillCardPanel().getBillModel().setValueAt(new UFBoolean(false),integer.intValue(),"binqusucess");
			}
		}
	}catch (Exception e){
		SCMEnv.error(e);
		return;
	}
}
private SpecialwarepriceVO constructSpecialwarepriceVO(int row){
	SpecialwarepriceVO arySpecialwarepriceVO = new SpecialwarepriceVO();
	AdjustpriceItemVO item =
		(AdjustpriceItemVO) getBillCardPanel().getBillModel().getBodyValueRowVO(row, AdjustpriceItemVO.class.getName());
	arySpecialwarepriceVO.setPk_corp(getCorpPrimaryKey());
	arySpecialwarepriceVO.setCinventoryid(item.getCinventoryid());
	arySpecialwarepriceVO.setCsourcepriceid(item.getCsrcpriceid());
	arySpecialwarepriceVO.setCinventorytypeid((String) item.getAttributeValue("cinvclassid"));
	arySpecialwarepriceVO.setCcustomertype((String) item.getAttributeValue("ccustclassid"));
	arySpecialwarepriceVO.setCcustomerid((String) item.getAttributeValue("ccustomerid"));
	arySpecialwarepriceVO.setcsaleorganid((String) item.getAttributeValue("csaleorganid"));
	arySpecialwarepriceVO.setcreceiptareaid((String) item.getAttributeValue("creceiptareaid"));
	arySpecialwarepriceVO.setcmeasdocid((String) item.getAttributeValue("cmeasdocid"));
	arySpecialwarepriceVO.setccurrencyid((String) item.getAttributeValue("ccurrencyid"));
	String cpricetariffid=(String)getBillCardPanel().getHeadItem("cpricetariffid").getValue();
	arySpecialwarepriceVO.setCpricetariffid(cpricetariffid);
	String dvalidate=getBillCardPanel().getHeadItem("dvalidate").getValue();
	String dexpiredate=getBillCardPanel().getHeadItem("dexpiredate").getValue();
	arySpecialwarepriceVO.setDstartdate(new UFDate(dvalidate));
	arySpecialwarepriceVO.setDstopdate(new UFDate(dexpiredate));
	return arySpecialwarepriceVO;
}
/**
 * �޸ģ�������
 * ���ڣ�2009-9-4
 * CQ�ţ�NCdp200979075
 * ԭ��ѯ�����м۸񣬼���������
 */
private void findPriceBase2(int maxRow){
	if (maxRow<0)
		return;
	for(int i=0;i<=maxRow;i++){
		String cinvbasdocid=(String)getBillCardPanel().getBodyValueAt(i,"cinvbasdocid");
		String cinvclassid=(String)getBillCardPanel().getBodyValueAt(i,"cinvclassid");
		if (cinvbasdocid==null && cinvclassid==null){
			continue;
		}/*
		 * �޸ģ���ǿ��
		 * ���ڣ�2008-8-14
		 * ԭ�򣺵���׼�ۿ�ʱ�����Ҽ۸���
		 */
		if (!isAdjustDiscount) {
			String csourcepriceid=(String)getBillCardPanel().getBodyValueAt(i,"csrcpriceid");
			if (csourcepriceid==null){
				SalePriceVO salepriceVO = constructSalepriceVO(i);
				try {
					csourcepriceid =
						nc.ui.sp.pub.FindSalePriceBO_Client.findInventorySaleItem(salepriceVO);
					getBillCardPanel().setBodyValueAt(csourcepriceid, i, "csrcpriceid");
					if(csourcepriceid==null)return;
				} catch (Exception e) {
					SCMEnv.error(e);
		
				}
		
			}
		}
	}
	getBillCardPanel().getBillModel().execFormulas(getBillCardPanel().getBodyItem("csrcpriceid").getLoadFormula(),
			0, maxRow);
	String[] cpricefield = new String[maxRow + 1];
	String[] conds = new String[maxRow + 1];
	for(int i=0;i<=maxRow;i++){
		cpricefield[i]=(String)getBillCardPanel().getBodyValueAt(i,"cpricefield");
		conds[i] = concatConditionStrForBase(i);
	}
    UFDouble[] ufbprices = new UFDouble[maxRow + 1];
	try {
		/*
		 * �޸ģ���ǿ��
		 * ���ڣ�2008-7-31
		 * ԭ�򣺵���׼�ۿ�ʱѯ�ۿ�
		 */
		if (isAdjustDiscount) {
			// ѯ��׼�ۿ�
			DiscountlistVO[] vos = DiscountBOClient.queryDiscountListBatch2(conds);
			for(int i=0;i<=maxRow;i++){
				if (null != vos[i] && vos.length > 0) {
					ufbprices[i] = vos[i].getBasediscount();
				}
			}
		} else {
			// ѯ����
			PrmtariffItemVO[][] vos = TariffBO_Client.queryTariffList2(conds);
			for(int i=0;i<=maxRow;i++){
				if (vos[i][0] != null && vos[i].length > 0) {
					ufbprices[i] = (UFDouble) vos[i][0].getAttributeValue(cpricefield[i]);
				}
			}
		}
	} catch(Exception e) {
		for(int i=0;i<=maxRow;i++){
			getBillCardPanel().getBillModel().setValueAt(new UFDouble(0),i,"nbaseprice");
			getBillCardPanel().getBillModel().setValueAt(new UFDouble(0),i,"nnewprice");
			getBillCardPanel().getBillModel().setValueAt(new UFDouble(1),i,"npricefactor");
			getBillCardPanel().getBillModel().setValueAt(new UFDouble(0),i,"npriceadd");
			getBillCardPanel().getBillModel().setValueAt(new UFBoolean(false),i,"binqusucess");
		}
		SCMEnv.error(e);
		showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("scmsoprice","UPPscmsoprice-000276")/*@res "ѯԭ��ʧ��!"*/);
		return;
	}
	if(isAdjustDiscount){
		getBillCardPanel().getBillData().getBodyItem("nbaseprice").setDecimalDigits(6);
		getBillCardPanel().getBillData().getBodyItem("nbasediscount").setDecimalDigits(6);
		getBillCardPanel().getBillData().getBodyItem("nnewprice").setDecimalDigits(6);
		
	}else{
		getBillCardPanel().getBillData().getBodyItem("nnewprice").setDecimalDigits(pricedigit);
		getBillCardPanel().getBillData().getBodyItem("nbaseprice").setDecimalDigits(pricedigit);
	}
	for(int i=0;i<=maxRow;i++){
		if (ufbprices[i] != null && ufbprices[i].doubleValue() > 0) {
			getBillCardPanel().getBillModel().setValueAt(ufbprices[i],i,"nbaseprice");
			getBillCardPanel().setCellEditable(i,"nnewprice",true);
			getBillCardPanel().setCellEditable(i,"npricefactor",true);
			getBillCardPanel().setCellEditable(i,"npriceadd",true);
			getBillCardPanel().getBillModel().setValueAt(UFBoolean.TRUE,i,"binqusucess");
		} else {
			getBillCardPanel().getBillModel().setValueAt(new UFDouble(isAdjustDiscount ? 100 : 0),i,"nbaseprice");
			getBillCardPanel().getBillModel().setValueAt(UFBoolean.FALSE,i,"binqusucess");
		}
	}
	
}
private String concatConditionStrForBase(int row){
	String cinvclassid=(String)getBillCardPanel().getBodyValueAt(row,"cinvclassid");
	String ccurrencyid=(String)getBillCardPanel().getBodyValueAt(row,"ccurrencyid");
	if (ccurrencyid==null){
		return null;
	}
	String cmeasdocid=(String)getBillCardPanel().getBodyValueAt(row,"cunitid");
	if (cmeasdocid==null){
		return null;
	}
	//�ϳ�SQL���
	String cinventoryid=(String)getBillCardPanel().getBodyValueAt(row,"cinventoryid");
	String cpricetariffid=(String) getBillCardPanel().getHeadItem("cpricetariffid").getValueObject();
	String csaleorganid=(String)getBillCardPanel().getBodyValueAt(row,"csaleorganid");
	String creceiptareaid=(String)getBillCardPanel().getBodyValueAt(row,"creceiptareaid");
	String ccustomerid=(String)getBillCardPanel().getBodyValueAt(row,"ccustomerid");
	String ccustclassid=(String)getBillCardPanel().getBodyValueAt(row,"ccustclassid");
	String cond="";
	if (cinventoryid!=null)
		cond=" cinventoryid='"+cinventoryid+"' ";
	else
		cond=" cinvclassid='"+cinvclassid+"' ";

	cond+=" and ccurrencyid='"+ccurrencyid+"' ";
	cond+=" and cmeasdocid='"+cmeasdocid+"' ";
	if (cpricetariffid!=null && cpricetariffid.trim().length()>0)
		cond+=" and cpricetariffid='"+cpricetariffid+"' ";
	else
		return null;
	if (csaleorganid!=null && csaleorganid.trim().length()>0)
		cond+=" and csaleorganid='"+csaleorganid+"' ";
	else
		cond+=" and csaleorganid ='"+NullStr.ID+"' ";
	if (creceiptareaid!=null && creceiptareaid.trim().length()>0)
		cond+=" and creceiptareaid='"+creceiptareaid+"' ";
	else
		cond+=" and creceiptareaid ='"+NullStr.ID+"' ";
	if (ccustomerid!=null && ccustomerid.trim().length()>0)
		cond+=" and ccustomerid='"+ccustomerid+"' ";
	else
		cond+=" and ccustomerid ='"+NullStr.ID+"' ";
	if (ccustclassid!=null && ccustclassid.trim().length()>0)
		cond+=" and ccustclassid='"+ccustclassid+"' ";
	else
		cond+=" and ccustclassid ='"+NullStr.ID+"' ";
	return cond;
}
/**
 * ���ϲ�����
 * �������ڣ�(2001-6-14 20:26:27)
 */
public void onDelete() {
//	this.showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCommon","UPPSCMCommon-000318")/*
//             * @res
//             * "����ɾ�������Ժ�..."
//             */);
	if (showOkCancelMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("scmsoprice","UPPscmsoprice-000305")/*@res "���Ҫ������"*/) == UIDialog.ID_CANCEL)
		return;
	try {
		AdjustpriceHeaderVO[] vaAdjustpriceHeader = null;
		if (!isList) {
			voAdjustprice =
				(AdjustpriceVO) getBillCardPanel().getBillValueVO(
					"nc.vo.sp.sp002.AdjustpriceVO",
					"nc.vo.sp.sp002.AdjustpriceHeaderVO",
					"nc.vo.sp.sp002.AdjustpriceItemVO");
			//�������ϱ�־
			voAdjustprice.setStatus(BillStatus.BLANKOUT);
			//����������
			 ((AdjustpriceHeaderVO) voAdjustprice.getParentVO()).setCdeleterid(ce.getUser().getPrimaryKey());
			//������������
			 ((AdjustpriceHeaderVO) voAdjustprice.getParentVO()).setDdeletedate(ce.getDate());
			 
			 voAdjustprice.getParentVO().setStatus(nc.vo.pub.VOStatus.DELETED);

			AdjustpriceBO_Client.delete(voAdjustprice);
			//modify by zxj 20030820
			/*PfUtilClient.processAction(
				"DELETE",
				getBillType(),
				getClientEnvironment().getDate().toString(),
				voAdjustprice);*/
			getBillCardPanel().addNew();
			getBillCardPanel().getBillModel().clearBodyData();
//			getBillCardPanel().getBillModel().execLoadFormula();
//			getBillCardPanel().execHeadLoadFormulas();
			getBillCardPanel().updateValue();
		}
		else {
			int iRow = getBillListPanel().getHeadTable().getSelectedRow();
			voAdjustprice = new AdjustpriceVO();
			String pk = getBillListPanel().getHeadBillModel().getValueAt(iRow, "cadjpriceid").toString();
			voAdjustprice = AdjustpriceBO_Client.findByPrimaryKey(pk);
			voAdjustprice.setParentVO((AdjustpriceHeaderVO)getBillListPanel().getHeadBillModel().getBodyValueRowVO(iRow,"nc.vo.sp.sp002.AdjustpriceHeaderVO"));
			//voAdjustprice.setParentVO(new AdjustpriceHeaderVO(pk));
			//�������ϱ�־
			voAdjustprice.setStatus(BillStatus.BLANKOUT);
			//����������
			 ((AdjustpriceHeaderVO) voAdjustprice.getParentVO()).setCdeleterid(ce.getUser().getPrimaryKey());
			//������������
			 ((AdjustpriceHeaderVO) voAdjustprice.getParentVO()).setDdeletedate(ce.getDate());
			//��˾
			String pk_corp = ce.getCorporation().getPk_corp();
			((AdjustpriceHeaderVO) voAdjustprice.getParentVO()).setPk_corp(pk_corp);
			//���ݺ�
			String cadjpriceno = getBillListPanel().getHeadBillModel().getValueAt(iRow, "cadjpriceno").toString();
			((AdjustpriceHeaderVO) voAdjustprice.getParentVO()).setCadjpriceno(cadjpriceno);
			
			voAdjustprice.getParentVO().setStatus(nc.vo.pub.VOStatus.DELETED);


			AdjustpriceBO_Client.delete(voAdjustprice);
			//vaAdjustpriceHeader = (AdjustpriceHeaderVO[])getBillListPanel().getHeadBillModel().getBodySelectedVOs("nc.vo.sp.sp002.AdjustpriceHeaderVO");
			//for(int i=0;i<vaAdjustpriceHeader.length;i++){
			////�������ϱ�־
			//vaAdjustpriceHeader[i].setFstatus(new Integer(BillStatus.BLANKOUT));
			////����������
			//vaAdjustpriceHeader[i].setCdeleterid(ce.getUser().getPrimaryKey());
			////������������
			//vaAdjustpriceHeader[i].setDdeletedate(ce.getDate());
			//}
			//getBillListPanel().getHeadItem("fstatus").setValue(new Integer(BillStatus.BLANKOUT));
			getBillListPanel().getHeadBillModel().setValueAt(nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UC001-0000005")/*@res "����"*/, iRow, "fstatus");

			/*
			 * �޸ģ���ǿ��
			 * ���ڣ�2009-8-20
			 * ԭ��ֻ��һ��ʱɾ���У�������������գ����ٽ��к�̨��ѯ
			 */
			if (1 == getBillListPanel().getHeadBillModel().getRowCount()) {
				getBillListPanel().getHeadBillModel().clearBodyData();
			} else {
				loadListHeader(queryCondition);
			}

			iCurrRow=iRow-1;
			getBillListPanel().getBodyBillModel().clearBodyData();
		}
		
		//�����������͵���������Ϣ
		//sendAuditFlowDelMessage();
		
		iStatus = DELETED;
		/**
		 * �޸ģ�������
		 * ���ڣ�2009-9-3
		 * CQ�ţ�
		 * ԭ��ǰ̨����ѡ���еı���VO
		 */
		
		adjitemVOs=null;
		setBnStatus(iStatus);
		showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("scmsoprice","UPPscmsoprice-000306")/*@res "ɾ���ɹ���"*/);
		
		//		showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON","UPPSCMCommon-000002",null,new String[]{nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UC001-0000005")/*@res "����"*/})/*@res "���ϳɹ�"*/);
		updateUI();
	}
	catch (Exception e) {
		SCMEnv.error(e);
		showErrorMessage(e.getMessage());
	}
}

	/**
	 * �����������͵���������Ϣ
	 */
	private void sendAuditFlowDelMessage() {
		try {
			PfUtilClient.processActionNoSendMessage(this, "SoBlankOut",
					SaleBillType.SaleAdjustPrice, getClientEnvironment().getDate()
							.toString(), voAdjustprice, null, null, null);
		} catch (Exception e) {
			SCMEnv.out(e.getMessage());
			showErrorMessage(e.getMessage());
		}
	}



/**
 * ɾ��һ�С�
 * �������ڣ�(2001-4-20 15:52:35)
 */
public void onDelLine() {
	this.showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCommon","UPPSCMCommon-000466")/*
             * @res
             * "����ɾ����..."
             */);
	if(getBillCardPanel().getBillTable().getSelectedRowCount()<=0){
		showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCommon","UPPSCMCommon-000169"));
		return;
	}
	getBillCardPanel().delLine();
	this.showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCommon","UPPSCMCommon-000225")/*
             * @res
             * "ɾ���ɹ�"
             */);
}
/**
 * �޸ġ�
 * �������ڣ�(2001-3-17 9:00:09)
 */
public void onDocument() {
	String pk = null;
	String billcode = null;
	if (!isList) {
		pk = getBillCardPanel().getHeadItem("cadjpriceid").getValue();
		billcode = getBillCardPanel().getHeadItem("cadjpriceno").getValue();

	} else {
		int iRow = getBillListPanel().getHeadTable().getSelectedRow();
		if (iRow<0){
			showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("scmsoprice","UPPscmsoprice-000301")/*@res "��ѡ�񵥾�!"*/);
			return;
		}
		pk =getBillListPanel()
				.getHeadBillModel()
				.getValueAt(iRow, "cadjpriceid")
				.toString();
		billcode =getBillListPanel()
				.getHeadBillModel()
				.getValueAt(iRow, "cadjpriceno")
				.toString();

	}

	if (pk == null || billcode==null) {
		showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON","UPPSCMCommon-000067")/*@res "���ݺ�Ϊ��"*/);
		return;
	}

	nc.ui.scm.file.DocumentManager.showDM(this,getBillType(), pk);
}
/**
 * �˴����뷽��˵����
 * �������ڣ�(2001-7-6 9:47:19)
 */
public void onDoubleClick() {
   try {
   		isList = false;
   		setBodyCol();
		remove(getBillListPanel());
		add(getBillCardPanel(), "Center");
		updateUI();
		showBillAtRow(iCurrRow);
		showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("scmsoprice","UPPSCMSOPRICE-000215")/*@res "�鿴����"*/);
		bnPrint.setEnabled(true);
		updateButton(bnPrint);
		getBillCardPanel().execHeadLoadFormulas();

		updateUI();
	} catch (Exception e) {
		SCMEnv.error(e);
	}
}
/**
 * ���������
 * �������ڣ�(2001-4-20 15:54:00)
 */
public void onExport() {}
/**
 * �鿴�б��һ�ŵ��ݡ�
 * �������ڣ�(2001-7-11 13:39:54)
 */
public void onFirst() {
	try {
		if (getBillListPanel().getHeadTable().getRowCount() <= 0)
			return;
		showBillAtRow(0);
		iCurrRow = 0;
		showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("scmsoprice","UPPscmsoprice-000307")/*@res "�鿴��һ�ŵ���"*/);
		isList = false;
		updateUI();
	} catch (Exception e) {
		SCMEnv.out(e.getMessage());
	}
}
/**
 * �����ĵ���
 * �������ڣ�(2001-4-20 15:55:49)
 */
public void onHelp() {}
/**
 * �鿴�б����һ�ŵ��ݡ�
 * �������ڣ�(2001-7-11 13:40:36)
 */
public void onLast() {
	try {
		if (getBillListPanel().getHeadTable().getRowCount() <= 0)
			return;
		int iLastRow = getBillListPanel().getHeadTable().getRowCount()-1;
		showBillAtRow(iLastRow);
		iCurrRow = iLastRow;
		showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("scmsoprice","UPPscmsoprice-000308")/*@res "�鿴���һ�ŵ���"*/);
		isList = false;
		updateUI();
	} catch (Exception e) {
		SCMEnv.out(e.getMessage());
	}
}
/**
 * �˴����뷽��˵����
 * �������ڣ�(2001-11-9 11:38:20)
 */
public void onLocate() {
	LocateDialog dlgLocate = null;
	if (isList)
		dlgLocate = new LocateDialog(this,getBillListPanel().getHeadTable());
	else
		dlgLocate = new LocateDialog(this,getBillCardPanel().getBillTable());

	dlgLocate.showModal();

}
/**
 * �鿴�б���һ�ŵ��ݡ�
 * �������ڣ�(2001-7-11 13:40:24)
 */
public void onNext() {
	try {
		if (getBillListPanel().getHeadTable().getRowCount() <= 0)
			return;
		int iLastRow = getBillListPanel().getHeadTable().getRowCount()-1;
		showBillAtRow(iCurrRow == iLastRow ? iLastRow : ++iCurrRow);
		showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("scmsoprice","UPPscmsoprice-000309")/*@res "�鿴��һ�ŵ���"*/);
		isList = false;
		updateUI();
	} catch (Exception e) {
		SCMEnv.out(e.getMessage());
	}
}
/**
 * �鿴�б���һ�ŵ��ݡ�
 * �������ڣ�(2001-7-11 13:40:09)
 */
public void onPrev() {
	try {
		if (getBillListPanel().getHeadTable().getRowCount() <= 0)
			return;
		showBillAtRow(iCurrRow == 0 ? 0 : --iCurrRow);
		showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("scmsoprice","UPPscmsoprice-000310")/*@res "�鿴��һ�ŵ���"*/);
		isList = false;
		updateUI();
	} catch (Exception e) {
		SCMEnv.out(e.getMessage());
	}
}
/**
 * ��ӡ��ǰ���۵���
 * �������ڣ�(2001-4-20 15:53:48)
 */
public void onPrint(boolean previewflag) {
	if (isList){
		if (getBillListPanel().getHeadBillModel().getRowCount()==0){
			showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("scmsoprice","UPPscmsoprice-000311")/*@res "û�����ݣ����ܴ�ӡ��"*/);
			return;
		}
		int thisrow=0;
		if (iCurrRow>=0)
			thisrow=iCurrRow;
		try{
			showBillAtRow(thisrow);
		}catch(Exception e){
			SCMEnv.out(e.getMessage());
		}
	}
	else{
		if (getBillCardPanel().getRowCount()==0){
			showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("scmsoprice","UPPscmsoprice-000311")/*@res "û�����ݣ����ܴ�ӡ��"*/);
			return;
		}
	}
	SalePubPrintDS ds =
		new SalePubPrintDS("403003", getBillCardPanel());
	PrintEntry print = new PrintEntry(null, null);
	print.setTemplateID(getCorpPrimaryKey(),"403003",ce.getUser().getPrimaryKey(),null);
	print.setDataSource(ds);
	if (print.selectTemplate()<0)
		return;

	ds.setPageRows(print.getBreakPos());
	print.setDataSource(ds);

    nc.ui.scm.print.PrintLogClient plc = new PrintLogClient();
    plc.setPrintInfo(getScmPrintlogVO());
    plc.addFreshTsListener(this);
	//���ô�ӡ����
    print.setPrintListener(plc);
	//
    plc.setPrintEntry(print);//ֻ���ڵ���ʱ

	if(previewflag){
		print.preview();
	}else{
		print.print(true);
	}
}
/**
 * ��ѯ���۵���
 * �������ڣ�(2001-6-20 10:53:02)
 */
public void onQuery() {
    this.showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
				"scmcommon", "UPPSCMCommon-000040")/* @res "��ѯ����..." */);
	if(dlgQuery == null){
//		dlgQuery = new SCMQueryConditionDlg(this,nc.ui.ml.NCLangRes.getInstance().getStrByID("scmsoprice","UPPscmsoprice-000312")/*@res "���۵���ѯ"*/);
		dlgQuery = SCMTreeQueryConditionDLG.getInstance(this, this.getModuleCode(), false);
		dlgQuery.hideNormal();
//		dlgQuery.setIsWarningWithNoInput(true);
//		dlgQuery.setTempletID(getCorpPrimaryKey(),getModuleCode(),getClientEnvironment().getUser().getPrimaryKey(),null);


		ClientEnvironment ce=ClientEnvironment.getInstance();
		dlgQuery.setDefaultValue(null,"dadjpricedate",ce.getDate().toString(),null);
		if ("0001".equals(getCorpPrimaryKey()))	{
			   dlgQuery.setValueRef("prm_adjustprice_b.cinventoryid","�����������");
			   dlgQuery.setValueRef("prm_adjustprice_b.ccustomerid","���̻�������");
		}
//		dlgQuery.setSealedDataShow(true);
//		dlgQuery.setDataPower(true,ce.getCorporation().getPk_corp());
	}
/*
	dlgQuery.setRefsDataPowerConVOs(
			ClientEnvironment.getInstance().getUser().getPrimaryKey(),
			new String[]{getCorpPrimaryKey()},
			new String[] { "�ͻ�����","�������", "�������", "������֯", 
					 "��������" }, new String[] {
					"prm_adjustprice_b.ccustomerid","prm_adjustprice_b.cinventoryid",
					"prm_adjustprice_b.cinvclassid", "prm_adjustprice_b.csaleorganid", 
					"prm_adjustprice_b.creceiptareaid", }, new int[] { 2,2, 2, 2,2 });
*/

	if(dlgQuery.showModal() == QueryConditionClient.ID_CANCEL)
		return;

	// �����ݿ��ֶβ�ѯ����
	ConditionVO[] lgcCondvos = dlgQuery.getLogicalConditionVOs();
	for (ConditionVO conditionVO : lgcCondvos) {
		// ����������
		if ("forApprove".equals(conditionVO.getFieldCode())) {
			queryForApprove = UFBoolean.valueOf(conditionVO.getValue()).booleanValue();
		}
	}

	ConditionVO[] gnrCondvos = dlgQuery.getQryCondEditor().getGeneralCondtionVOs();
	for (ConditionVO condvo : gnrCondvos) {
        if (condvo.getFieldCode().equals("fadjtype")){
    		if ("0".equals(condvo.getValue())){ // ������
    			isAdjustBase = true;
    			isAdjustDiscount = false;
    		} else if ("1".equals(condvo.getValue())){ // ����������
    			isAdjustBase = false;
    			isAdjustDiscount = false;
    		} else { // ����׼�ۿ�
    			isAdjustBase = true;
    			isAdjustDiscount = true;
    		}
        }
	}

/*
	ConditionVO[] condvos=dlgQuery.getConditionVO();
		//����һ���������������ת��
		for (int i=0;i<condvos.length;i++){
			if(condvos[i].getValue().equals(" NULL ")){
				condvos[i].setOperaCode(" = ");
				condvos[i].setValue("'#123456789*123456789'");
			}
			if (condvos[i].getFieldCode().equals("prm_adjustprice_b.cinvclassid") && condvos[i].getOperaCode().equals("=") ){
				//�������ID
				String invclasscode=condvos[i].getRefResult().getRefCode();
				String classid=condvos[i].getRefResult().getRefPK();
				String sql=" or cinvbasdocid in (select pk_invbasdoc from bd_invbasdoc,bd_invcl where bd_invbasdoc.pk_invcl=bd_invcl.pk_invcl and bd_invcl.invclasscode like '"+invclasscode+"%') ";
				condvos[i].setDataType(1);
				condvos[i].setValue("'"+classid+"' "+ sql);
			}else if (condvos[i].getFieldCode().equals("prm_adjustprice_b.cinvclassid") && condvos[i].getOperaCode().equals("in")){
				String classid=condvos[i].getValue();
				String sql=" or cinvbasdocid in (select pk_invbasdoc from bd_invbasdoc where bd_invbasdoc.pk_invcl in "+classid+")";
				//condvos[i].setDataType(1);
				condvos[i].setValue(classid + sql);
				
			}
		}
		List<ConditionVO> condList = new ArrayList<ConditionVO>();
	    for (int i = 0; i < condvos.length; i++) {
            if (condvos[i].getFieldCode().equals("fadjtype")){
        		if ("0".equals(condvos[i].getValue())){ // ������
        			isAdjustBase = true;
        			isAdjustDiscount = false;
        		} else if ("1".equals(condvos[i].getValue())){ // ����������
        			isAdjustBase = false;
        			isAdjustDiscount = false;
        		} else { // ����׼�ۿ�
        			isAdjustBase = true;
        			isAdjustDiscount = true;
        		}
            }
            if ("forApprove".equals(condvos[i].getFieldCode())) {
            	queryForApprove = UFBoolean.valueOf(condvos[i].getValue()).booleanValue();
            } else {
            	condList.add(condvos[i]);
            }
        }
*/
//		queryCondition = dlgQuery.getWhereSQL(condList.toArray(new ConditionVO[condList.size()]));
		queryCondition = dlgQuery.getWhereSQL();


	if(queryCondition != null) {
		queryCondition += " AND ";
	}
	queryCondition += " prm_adjustprice.pk_corp = '" + getCorpPrimaryKey() + "'";

    if (queryCondition.indexOf("prm_adjustprice_b")>=0){
       queryCondition=" cadjpriceid in ( select prm_adjustprice.cadjpriceid from prm_adjustprice,prm_adjustprice_b where prm_adjustprice.cadjpriceid=prm_adjustprice_b.cadjpriceid and "+ queryCondition+")";
    }	
    queryCondition += " and prm_adjustprice.fstatus != 5 ";
    
	getBillListPanel().getBodyBillModel().clearBodyData();
	getBillListPanel().getHeadBillModel().clearBodyData();
	
	loadListHeader(queryCondition);
	if (!isList) {
		remove(getBillCardPanel());
		add(getBillListPanel(), "Center");
		isList = true;
	}

	getBillListPanel().getHeadTable().clearSelection();
	iStatus = INIT;
	setBnStatus(iStatus);

	setBodyCol();
	affectvos=null;
	/**
	 * �޸ģ�������
	 * ���ڣ�2009-9-3
	 * CQ�ţ�
	 * ԭ����ǰ̨���浱ǰѡ���еı���VO,�����ظ��غ�̨����
	 */
	adjitemVOs=null;
    this.showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
            "SCMCommon", "UPPSCMCommon-000036")/* @res "��ѯ�������" */);
	updateUI();
}
/**
 * ���ز�����
 * �������ڣ�(2001-7-3 15:19:05)
 */
public void onReturn() {
	if (isList){
		if ( iCurrRow<0 && getBillListPanel().getHeadBillModel().getRowCount()>0 )
			iCurrRow=0;
		if(getBillListPanel().getHeadBillModel().getRowCount()<=0){
			getBillCardPanel().addNew();
			getBillCardPanel().getBillModel().clearBodyData();
			getBillCardPanel().updateValue();
		}
		onDoubleClick();
	}
	else{
		getBillListPanel().getBodyBillModel().clearBodyData();
		getBillListPanel().getHeadBillModel().clearBodyData();
		if(isQuery)
			loadListHeader(queryCondition);
		if (!isList) {
			remove(getBillCardPanel());
			add(getBillListPanel(), "Center");
			getBillListPanel().getBodyBillModel().clearBodyData();
			voAdjustprice=(AdjustpriceVO)getBillCardPanel().getBillValueVO("nc.vo.sp.sp002.AdjustpriceVO","nc.vo.sp.sp002.AdjustpriceHeaderVO","nc.vo.sp.sp002.AdjustpriceItemVO");
			if(!isQuery&&voAdjustprice!=null){
				 String id=(String) voAdjustprice.getParentVO().getAttributeValue("cadjpriceid");
				 loadListHeader("cadjpriceid='"+id+"'");
			}
			updateUI();
			isList = true;
		}
		getBillListPanel().getHeadTable().clearSelection();
		if(iCurrRow < getBillListPanel().getHeadTable().getRowCount() && iCurrRow>=0 ){
			setButton(iCurrRow);
			/**
			 * �޸ģ�������
			 * ���ڣ�2009-9-4
			 * CQ�ţ�NCdp200987758
			 * ԭ���ں������getBillListPanel().getHeadTable().getSelectionModel().setSelectionInterval����
			 *       ��ʱ��ᴥ���¼�����һ��loadListBody(iCurrRow)���Դ˴ε���û�б�Ҫ
			 */
			
//			loadListBody(iCurrRow);
		}
		else if (iCurrRow<0){
			iStatus = INIT;
			setBnStatus(iStatus);
		}
		setBodyCol();
		updateUI();
	}
	getBillListPanel().getHeadTable().getSelectionModel().setSelectionInterval(iCurrRow, iCurrRow);
}
/**
 * ������۵���
 * �������ڣ�(2001-4-20 15:54:46)
 */
public boolean onSave() {
	showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
            "scmcommon", "UPPSCMCommon-000312")/* @res "���ڱ���..." */);
   

	getBillCardPanel().tableStopCellEditing();
	cleanNullLine();
	voAdjustprice =
		(AdjustpriceVO) getBillCardPanel().getBillValueChangeVO(
			"nc.vo.sp.sp002.AdjustpriceVO",
			"nc.vo.sp.sp002.AdjustpriceHeaderVO",
			"nc.vo.sp.sp002.AdjustpriceItemVO");
	long time = System.currentTimeMillis();
	voAdjustprice.setAffectVO(affectvos);

//	AdjustpriceVO allvo=(AdjustpriceVO) getBillCardPanel().getBillValueVO("nc.vo.sp.sp002.AdjustpriceVO",
//			"nc.vo.sp.sp002.AdjustpriceHeaderVO",
//			"nc.vo.sp.sp002.AdjustpriceItemVO");
	try {
		getBillCardPanel().dataNotNullValidate();

		checkHeaderData(voAdjustprice);
		voAdjustprice.setStatus(BillStatus.FREE);
		voAdjustprice.validate();
		checkData(voAdjustprice);

		checkUnique(voAdjustprice);

//		String pk = null;
		TransUniBillVO unibillvo=null;
		if (iStatus == NEW) {
			voAdjustprice.getParentVO().setStatus(nc.vo.pub.VOStatus.NEW);
			//pk = AdjustpriceBO_Client.insert(voAdjustprice);
			//modify by zxj 20030820
			//modify by xgx 2004-07-16  ������������,�����SAVE����
			//pk =
				//(String) PfUtilClient.processAction(
					//"SoSaveing",
					//getBillType(),
					//getClientEnvironment().getDate().toString(),
					//voAdjustprice);
			unibillvo =
				(TransUniBillVO) PfUtilClient.processActionNoSendMessage(this,
					"SoSaveing",
					getBillType(),
					getClientEnvironment().getDate().toString(),
					voAdjustprice,
					null,null,null);

			//voAdjustprice = AdjustpriceBO_Client.findByPrimaryKey(pk);
			//�����Զ�����
//			PfUtilClient.processAction(
//					"SAVE",
//					getBillType(),
//					getClientEnvironment().getDate().toString(),
//					voAdjustprice);
			//getBillCardPanel().getHeadItem("cadjpriceid").setValue(pk);
		} else if (iStatus == UPDATED) {
//			pk = voAdjustprice.getPrimaryKey();
			voAdjustprice.getParentVO().setStatus(nc.vo.pub.VOStatus.UPDATED);
			if (m_bcodechanged.booleanValue()){
				((AdjustpriceHeaderVO)voAdjustprice.getParentVO()).setAttributeValue("bcodechanged",m_bcodechanged);
				((AdjustpriceHeaderVO)voAdjustprice.getParentVO()).setoldCadjpriceno(m_oldcadjpriceno);
			}
			////���ݺ�
			//AdjustpriceHeaderVO header = (AdjustpriceHeaderVO) voAdjustprice.getParentVO();
			//if (header == null
				//|| header.getCadjpriceno() == null
				//|| header.getCadjpriceno().trim().length() == 0){
				//showErrorMessage("���ݺŲ���Ϊ�գ�");
				//return;
			//}
			//AdjustpriceBO_Client.update(voAdjustprice);
			//modify by zxj 20030820

			//modify by xgx 2004-07-16  ������������,�����SAVE����
			//PfUtilClient.processAction(
				//"SoEditing",
				//getBillType(),
				//getClientEnvironment().getDate().toString(),
				//voAdjustprice);
			unibillvo=(TransUniBillVO)PfUtilClient.processActionNoSendMessage(this,
				"SoEditing",
				getBillType(),
				getClientEnvironment().getDate().toString(),
				voAdjustprice,
				null,null,null);
			//voAdjustprice = AdjustpriceBO_Client.findByPrimaryKey(pk);
			//�����Զ�����
//			PfUtilClient.processAction(
//					"SAVE",
//					getBillType(),
//					getClientEnvironment().getDate().toString(),
//					voAdjustprice);

		}
		
		time = System.currentTimeMillis() - time;
//		if (unibillvo!=null){
//			int[] rows=unisrv.getAllRows(allvo.getChildrenVO(), unibillvo.getChildrenVO());
//			unisrv.autoFillResultToUI("36",unibillvo,getBillCardPanel(),rows);
//		}
//		else{
		/**
		 * �޸ģ�������
		 * ���ڣ�2009-9-2
		 * CQ�ţ�NCdp200978799
		 * ԭ��ֻ���±仯���ֶΣ�����ѯ����VO
		 */
		updateChangedItemValuesAfterSave(unibillvo);
//		getBillCardPanel().getHeadItem("ts").setValue(transUniBillHeadVO.getAttributeValue("ts"));
//			voAdjustprice = AdjustpriceBO_Client.findByPrimaryKey(unibillvo.getParentVO().getAttributeValue("billid").toString());
//			getBillCardPanel().setBillValueVO(voAdjustprice);
//			getBillCardPanel().execHeadLoadFormulas();
//			getBillCardPanel().getBillModel().execLoadFormula();
//		}

		getBillCardPanel().updateValue();
		getBillCardPanel().setEnabled(false);

		int iNewStatus =
			((AdjustpriceHeaderVO) voAdjustprice.getParentVO()).getFstatus().intValue();

		if (iNewStatus == 1)
			iStatus = FREE;
		else if (iNewStatus == 2)
			iStatus = AUDITED;
		else if (iNewStatus == 7)
			iStatus = AUDITING;

		//iStatus = FREE;

		getBillCardPanel().getBillModel().execLoadFormula();
		setBnStatus(iStatus);


		showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON","UPPSCMCommon-000007",null,new String[]{String.valueOf(time/1000.0)})/*@res "����ɹ�[����ʱ{0}��]"*/);
	} catch (Exception e) {
		showErrorMessage(e.getMessage());
		SCMEnv.error(e);
		return false;
	}
	return true;
}
private void updateChangedItemValuesAfterSave(TransUniBillVO unibillvo){
	TransUniBillHeadVO transUniBillHeadVO = (TransUniBillHeadVO) unibillvo.getParentVO();
	AdjustpriceHeaderVO adjhvo = voAdjustprice.getParentVO();
	if(transUniBillHeadVO != null){
		getBillCardPanel().getHeadItem("ts").setValue(transUniBillHeadVO.getAttributeValue("ts"));
		adjhvo.setAttributeValue("ts", transUniBillHeadVO.getAttributeValue("ts"));
		getBillCardPanel().getHeadItem("cadjpriceid").setValue(transUniBillHeadVO.getAttributeValue("billid"));
		adjhvo.setAttributeValue("cadjpriceid", transUniBillHeadVO.getAttributeValue("billid"));
		getBillCardPanel().getHeadItem("cadjpriceno").setValue(transUniBillHeadVO.getAttributeValue("billno"));
		adjhvo.setAttributeValue("cadjpriceno", transUniBillHeadVO.getAttributeValue("billno"));
		getBillCardPanel().getTailItem("fstatus").setValue(transUniBillHeadVO.getAttributeValue("fstatus"));
		adjhvo.setAttributeValue("fstatus", transUniBillHeadVO.getAttributeValue("fstatus"));
		getBillCardPanel().getTailItem("cauditorid").setValue(transUniBillHeadVO.getAttributeValue("auditorid"));
		adjhvo.setAttributeValue("cauditorid", transUniBillHeadVO.getAttributeValue("auditorid"));
		getBillCardPanel().getTailItem("caduitorname").setValue(transUniBillHeadVO.getAttributeValue("auditorname"));
		adjhvo.setAttributeValue("caduitorname", transUniBillHeadVO.getAttributeValue("auditorname"));
		getBillCardPanel().getTailItem("dauditdate").setValue(transUniBillHeadVO.getAttributeValue("auditdate"));
		adjhvo.setAttributeValue("dauditdate", transUniBillHeadVO.getAttributeValue("auditdate"));
	}
	
	TransUniBillBodyVO[] transUniBillBodyVOs = (TransUniBillBodyVO[]) unibillvo.getChildrenVO();
	if(transUniBillBodyVOs == null){
		return;
	}
	AdjustpriceItemVO[] adjbvos = voAdjustprice.getChildrenVO();
	for(int i = 0; i<transUniBillBodyVOs.length;i++){
		getBillCardPanel().getBillModel().setValueAt(transUniBillBodyVOs[i].getAttributeValue("bodyid"), i, "cadjprice_bid");
		adjbvos[i].setAttributeValue("cadjprice_bid", transUniBillBodyVOs[i].getAttributeValue("bodyid"));
		getBillCardPanel().getBillModel().setValueAt(transUniBillBodyVOs[i].getAttributeValue("ts"), i, "ts");
		adjbvos[i].setAttributeValue("ts", transUniBillBodyVOs[i].getAttributeValue("ts"));
		
	}
}
/**
 * ���۵�ȡ��������
 * �������ڣ�(2001-7-9 9:39:29)
 */
public void onUnAudit() {
	showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
				"SCMCommon", "UPPSCMCommon-000317")/* @res �����������Ժ�... */);
	try {
//		AdjustpriceHeaderVO[] vaAdjustpriceHeader = null;
		if (!isList) {
			voAdjustprice =
				(AdjustpriceVO) getBillCardPanel().getBillValueVO(
					"nc.vo.sp.sp002.AdjustpriceVO",
					"nc.vo.sp.sp002.AdjustpriceHeaderVO",
					"nc.vo.sp.sp002.AdjustpriceItemVO");
			/**
			 * �޸ģ�������
			 * ���ڣ�2009-9-8
			 * CQ�ţ�NCdp200978809
			 * ԭ�򣺴���������Ƿ����á��͡�����ʱ���Ƿ��ѹ���Чʱ�䡱У��
			 */
			
//			if (checkUnAudit(voAdjustprice) == true) {
			if (checkUnAudit2(voAdjustprice) == true) {
				if (voAdjustprice.getChildrenVO()==null || voAdjustprice.getChildrenVO().length==0)
					voAdjustprice = AdjustpriceBO_Client.findByPrimaryKey(((AdjustpriceHeaderVO)voAdjustprice.getParentVO()).getCadjpriceid());
				voAdjustprice.setStatus(VOStatus.UPDATED);
				audit(voAdjustprice, false);
				//ǿ�н�����������Ϊ��
				voAdjustprice = AdjustpriceBO_Client.findByPrimaryKey(((AdjustpriceHeaderVO)voAdjustprice.getParentVO()).getCadjpriceid());
				for (int i=0;i<getBillListPanel().getHeadBillModel().getRowCount();i++){
					if (((AdjustpriceHeaderVO)voAdjustprice.getParentVO()).getCadjpriceid().equals(
							getBillListPanel().getHeadBillModel().getValueAt(i, "cadjpriceid") ))
//						getBillListPanel().getHeadBillModel().setValueAt(null, i, "caduitorname");
//						getBillListPanel().getHeadBillModel().setValueAt(null, i, "dauditdate");
//						getBillListPanel().getHeadBillModel().setValueAt(null, i, "daudittime");
						getBillListPanel().getHeadBillModel().setBodyRowVO(voAdjustprice.getParentVO(), i);
				}
				/**
				 * �޸ģ�������
				 * ���ڣ�2009-9-3
				 * CQ�ţ�NCdp200978809
				 * ԭ�򣺲�������������VO,��������ÿ�����ı���ֶΣ�Ҳ����ִ�й�ʽ������������
				 */
				updateChangedItemValuesAfterAuditOrUnAudit();
				
//				getBillCardPanel().setBillValueVO(voAdjustprice);
//				getBillCardPanel().getBillModel().execLoadFormula();
//				getBillCardPanel().execHeadLoadFormulas();
				getBillCardPanel().updateValue();
			} else
				return;
		} else {
			int iRow = getBillListPanel().getHeadTable().getSelectedRow();
			if(iRow<0){
				showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("scmsoprice","UPPSCMSOPRICE-000101"));
				return;
			}
			String pk = getBillListPanel().getHeadBillModel().getValueAt(iRow, "cadjpriceid").toString();
//			voAdjustprice = AdjustpriceBO_Client.findByPrimaryKey(pk);
			/**
			 * �޸ģ�������
			 * ���ڣ�2009-9-3
			 * CQ�ţ�
			 * ԭ��ǰ̨����ѡ���еı���VO,���������Ϻ�̨���в�ѯ
			 */
			
			voAdjustprice = (AdjustpriceVO) getBillListPanel().getBillValueVO(iRow, AdjustpriceVO.class.getName(),
					AdjustpriceHeaderVO.class.getName(), AdjustpriceItemVO.class.getName());
			voAdjustprice.setChildrenVO(adjitemVOs);
			voAdjustprice.getParentVO().setAttributeValue("ts",new UFDateTime(getBillListPanel().getHeadBillModel().getValueAt(iRow, "ts").toString()));
			
			/**
			 * �޸ģ�������
			 * ���ڣ�2009-9-8
			 * CQ�ţ�NCdp200978809
			 * ԭ�򣺴���������Ƿ����á��͡�����ʱ���Ƿ��ѹ���Чʱ�䡱У��
			 */
//			if (checkUnAudit(voAdjustprice) == true) {
			if (checkUnAudit2(voAdjustprice) == true) {
				voAdjustprice.setStatus(VOStatus.UPDATED);
				audit(voAdjustprice, false);
				voAdjustprice = AdjustpriceBO_Client.findByPrimaryKey(pk);
//				getBillListPanel().getHeadBillModel().setValueAt(voAdjustprice.getParentVO().getAttributeValue("ts"), iRow, "ts");
//				getBillListPanel().getHeadBillModel().setValueAt(null, iRow, "daudittime");
//				
//				getBillListPanel().getHeadBillModel().setValueAt(nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON","UPPSCMCommon-000340")/*@res "����"*/, iRow, "fstatus");
//				getBillListPanel().getHeadBillModel().setValueAt(null, iRow, "dauditdate");
//				
//				getBillListPanel().getHeadBillModel().setValueAt(null, iRow, "caduitorname");
				getBillListPanel().getHeadBillModel().setBodyRowVO(voAdjustprice.getParentVO(), iRow);
			} else
				return;
		}
		iOldStatus = iStatus;
		iStatus = FREE;
		setBnStatus(iStatus);
		showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON","UPPSCMCommon-000184")/*@res "����ɹ�"*/);
		updateUI();
	} catch (Exception e) {
		showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON","UPPSCMCommon-000213",null,new String[]{e.getMessage()})/*@res "ȡ���������� {0}"*/);
		SCMEnv.out(e.getMessage());
	}
}
/**
 * �޸ģ�������
 * ���ڣ�2009-9-3
 * CQ�ţ�NCdp200978809
 * ԭ�򣺲�������������VO,��������ÿ�����ı���ֶΣ�Ҳ����ִ�й�ʽ������������
 */
private void updateChangedItemValuesAfterAuditOrUnAudit(){
	AdjustpriceHeaderVO adjheadVO = voAdjustprice.getParentVO();
    if(adjheadVO != null){
    	getBillCardPanel().getTailItem("fstatus").setValue(adjheadVO.getFstatus());
    	getBillCardPanel().getTailItem("cauditorid").setValue(adjheadVO.getCauditorid());
    	getBillCardPanel().getTailItem("dauditdate").setValue(adjheadVO.getDauditdate());
    	getBillCardPanel().getTailItem("daudittime").setValue(adjheadVO.getDaudittime());
    	getBillCardPanel().getHeadItem("ts").setValue(adjheadVO.getTs());
	}
	
	AdjustpriceItemVO[] adjitemVOs = voAdjustprice.getChildrenVO();
	if(adjitemVOs != null){
		for(int i = 0;i<adjitemVOs.length;i++){
			getBillCardPanel().getBillModel().setValueAt(adjitemVOs[i].getTs(), i, "ts");
		}
	}
	voAdjustprice.setStatus(VOStatus.NEW);
}
/**
 * �޸ĵ��۵���
 * �������ڣ�(2001-4-20 15:54:15)
 */
public void onUpdate() {
	showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCommon","UPPSCMCommon-000350")/*
             * @res
             * "�༭����..."
             */);
	try {
		//���ж��Ƿ���������������
//		String pkid=null;
//		if (isList) {
//			pkid =	getBillListPanel().getHeadBillModel().getValueAt(iCurrRow, "cadjpriceid").toString();
//		}
//		else{
//			pkid =	(String)getBillCardPanel().getHeadItem("cadjpriceid").getValue();
//		}
//		if (pkid!=null && pkid.trim().length()>0){
//			int workflowstatus=PfUtilClient.queryWorkFlowStatus(m_defbusitype,SO2BillType.SaleAdjustPrice, pkid);
//			if (workflowstatus==IWorkFlowStatus.WORKFLOW_ON_PROCESS){
//			    // ||workflowstatus==IWorkFlowStatus.NOT_STARTED_IN_WORKFLOW
//				showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("scmsoprice","UPPscmsoprice-000314")/*@res "������������������,��Ҫ�ȴ��������޸�!"*/);
//				return;
//			}
//		}

		m_dlgQuick=null;
		iOldStatus = iStatus;
		iStatus = UPDATED;
		//
		if (isList) {
			isList = false;
			setBodyCol();
			remove(getBillListPanel());
			add(getBillCardPanel(), "Center");
			updateUI();
			/**
			 * �޸ģ�������
			 * ���ڣ�2009-9-3
			 * CQ�ţ�
			 * ԭ�򣺴�ǰ̨����ȥVO,���Ӻ�̨����
			 */
			
//			String pk =	getBillListPanel().getHeadBillModel().getValueAt(iCurrRow, "cadjpriceid").toString();
//			voAdjustprice = AdjustpriceBO_Client.findByPrimaryKey(pk);
			voAdjustprice = (AdjustpriceVO) getBillListPanel().getBillValueVO(iCurrRow, AdjustpriceVO.class.getName(),
					AdjustpriceHeaderVO.class.getName(), AdjustpriceItemVO.class.getName());
			voAdjustprice.setChildrenVO(adjitemVOs);
			if (voAdjustprice != null)
				getBillCardPanel().setBillValueVO(voAdjustprice);
			getBillCardPanel().getBillModel().execLoadFormula();
			getBillCardPanel().execHeadLoadFormulas();
			getBillCardPanel().updateValue();
		}
		getBillCardPanel().setEnabled(true);
		getBillCardPanel().getBillData().getHeadItem("dexpiredate").setEnabled(!isAdjustBase);
		getBillCardPanel().getBillData().getHeadItem("dtstoptime").setEnabled(!isAdjustBase);
		Object adjustMode = getBillCardPanel().getHeadItem("fadjpricemode").getValueObject();
		setAdjustMode(null == adjustMode ? null : adjustMode.toString());
		for (int row=0;row<getBillCardPanel().getBillModel().getRowCount();row++){
			//���õ�ǰ�����еĿɱ༭��״̬
			if (!isAdjustBase){
				setRowEditCol(row);
			}
			else{
				getBillCardPanel().setCellEditable(row,"npricefactor",true);
				getBillCardPanel().setCellEditable(row,"npriceadd",true);
			}
		}
		setBnStatus(iStatus);
		
		updateUI();
	} catch (Exception e) {
		SCMEnv.error(e);
	}
	//����궨λ�ڵ�һ���ɱ༭��
	getBillCardPanel().transferFocusTo(BillCardPanel.HEAD);
	m_bcodechanged=new Boolean(false);
	AdjustpriceVO hvo;
	hvo=(AdjustpriceVO) getBillCardPanel().getBillValueVO(
			AdjustpriceVO.class.getName(),
			AdjustpriceHeaderVO.class.getName(),
			AdjustpriceItemVO.class.getName());
	AdjustpriceHeaderVO header = (AdjustpriceHeaderVO) hvo.getParentVO();
	m_oldcadjpriceno = header.getCadjpriceno();

	//�����е��а����������ñ༭��
//	for (int row=0;row<getBillCardPanel().getRowCount();row++)
//		setRowEditCol(row);

	try{
		affectvos=nc.ui.sp.sp002.AdjustpriceBO_Client.getAffectCorpByKey(header.getCadjpriceid());
	}catch(Exception e){
		SCMEnv.out(e.getMessage());
		showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("scmsoprice","UPPscmsoprice-000296")/*@res "��ѯԭӰ�칫˾�����쳣!"*/);
		return;
	}

	//���û�����
	for (int row=0;row<getBillCardPanel().getRowCount();row++){
		String cinventoryid=(String)getBillCardPanel().getBodyValueAt(row,"cinventoryid");
		String cinvbasdocid=(String)getBillCardPanel().getBodyValueAt(row,"cinvbasdocid");
		String cinvclassid=(String)getBillCardPanel().getBodyValueAt(row,"cinvclassid");
		boolean b = ( cinvbasdocid==null || cinvbasdocid.trim().length()==0) && (cinventoryid==null || cinventoryid.trim().length()==0);
		if (!b){
			getBillCardPanel().getBillModel().setCellEditable(row,"code",true);
			getBillCardPanel().getBillModel().setCellEditable(row,"cinvclassname",false);
		}
		else if (cinvclassid!=null && cinvclassid.trim().length()>0){
			getBillCardPanel().getBillModel().setCellEditable(row,"code",false);
			getBillCardPanel().getBillModel().setCellEditable(row,"cinvclassname",true);
		}
		else{
			getBillCardPanel().getBillModel().setCellEditable(row,"code",true);
			getBillCardPanel().getBillModel().setCellEditable(row,"cinvclassname",true);
		}

		String ccustomerid=(String)getBillCardPanel().getBodyValueAt(row,"ccustomerid");
		String ccustclassid=(String)getBillCardPanel().getBodyValueAt(row,"ccustclassid");
		if ( ccustomerid!=null && ccustomerid.trim().length()>0){
			getBillCardPanel().getBillModel().setCellEditable(row,"ccustomername",true);
			getBillCardPanel().getBillModel().setCellEditable(row,"ccustclassname",false);
		}
		else if ( ccustclassid!=null && ccustclassid.trim().length()>0){
			getBillCardPanel().getBillModel().setCellEditable(row,"ccustomername",false);
			getBillCardPanel().getBillModel().setCellEditable(row,"ccustclassname",true);
		}
		else{
			getBillCardPanel().getBillModel().setCellEditable(row,"ccustomername",true);
			getBillCardPanel().getBillModel().setCellEditable(row,"ccustclassname",true);
		}
	}
	for(int i=0;i<getBillCardPanel().getBillModel().getRowCount();i++)
		getBillCardPanel().getBillModel().setValueAt(new UFBoolean(true),i,"binqusucess");

	
	getBillCardPanel().updateUI();
}
/**
 * ���õ���ģʽ��
 * �������ڣ�(2001-6-14 16:54:44)
 * @param mode java.lang.String
 */
public void setAdjustMode(String mode) {
	if (nc.ui.ml.NCLangRes.getInstance().getStrByID("scmsoprice","UPPscmsoprice-000280")/*@res "�����Ե���"*/.equals(mode) || "0".equals(mode)) {
		getBillCardPanel().getHeadItem("fstartday").setValue(new Integer(0));
		getBillCardPanel().getHeadItem("fstopday").setValue(new Integer(0));
//		getBillCardPanel().getHeadItem("dtstarttime").setValue(null);
//		getBillCardPanel().getHeadItem("dtstoptime").setValue(null);
		getBillCardPanel().getHeadItem("fstartday").setEnabled(false);
		getBillCardPanel().getHeadItem("fstopday").setEnabled(false);
//		getBillCardPanel().getHeadItem("dtstarttime").setEnabled(false);
//		getBillCardPanel().getHeadItem("dtstoptime").setEnabled(false);
	} else {
		getBillCardPanel().getHeadItem("fstartday").setEnabled(true);
		getBillCardPanel().getHeadItem("fstopday").setEnabled(true);
//		getBillCardPanel().getHeadItem("dtstarttime").setEnabled(true);
//		getBillCardPanel().getHeadItem("dtstoptime").setEnabled(true);
		getBillCardPanel().getHeadItem("fstartday").setEdit(true);
		getBillCardPanel().getHeadItem("fstopday").setEdit(true);
//		getBillCardPanel().getHeadItem("dtstarttime").setEdit(true);
//		getBillCardPanel().getHeadItem("dtstoptime").setEdit(true);
	}
}
/**
 * ���ð�ť״̬��
 * �������ڣ�(2001-4-27 18:30:57)
 */
public void setBnStatus(int status) {
	//��ʼ������״̬
	if (status == INIT || status == DELETED) {
		bnBrowse.setEnabled(true);
		bnQuery.setEnabled(true);
		bnFirst.setEnabled(isQuery && !isList);
		bnPrev.setEnabled(isQuery && !isList);
		bnNext.setEnabled(isQuery && !isList);
		bnLast.setEnabled(isQuery && !isList);
		bnPrint.setEnabled(false);
		bnPreview.setEnabled(false);
		bnAdd.setEnabled(true);
		bnUpdate.setEnabled(false);
		bnSave.setEnabled(false);
		bnCancel.setEnabled(false);
		bnLineOptr.setEnabled(false);
		bnDelete.setEnabled(false);
		bnAudit.setEnabled(false);
		bnUnAudit.setEnabled(false);
		//bnReturn.setEnabled(true);
		//bnHelp.setEnabled(true);
		bnCopy.setEnabled(true);
		bnSendAudit.setEnabled(false);
		getBillCardPanel().setEnabled(false);
	}
	//���ӡ��޸�״̬
	if (status == NEW || status == UPDATED) {
		boAction.setEnabled(false);

		bnBrowse.setEnabled(false);
		bnQuery.setEnabled(false);
		bnFirst.setEnabled(false);
		bnPrev.setEnabled(false);
		bnNext.setEnabled(false);
		bnLast.setEnabled(false);
		bnPrint.setEnabled(false);
		bnPreview.setEnabled(false);
		bnAdd.setEnabled(false);
		bnUpdate.setEnabled(false);
		bnSave.setEnabled(true);
		bnCancel.setEnabled(true);
		bnLineOptr.setEnabled(true);
		bnDelete.setEnabled(false);
		bnAudit.setEnabled(false);
		bnUnAudit.setEnabled(false);
		//bnReturn.setEnabled(false);
		//bnHelp.setEnabled(true);
		bnCopy.setEnabled(false);
		bnSendAudit.setEnabled(isExistAuditFlow());}
	else{
			boAction.setEnabled(true);

	}
	//����״̬
	if (status == FREE) {
		bnBrowse.setEnabled(true);
		bnQuery.setEnabled(true);
		bnFirst.setEnabled(isQuery && !isList);
		bnPrev.setEnabled(isQuery && !isList);
		bnNext.setEnabled(isQuery && !isList);
		bnLast.setEnabled(isQuery && !isList);
		bnPrint.setEnabled(true);
		bnPreview.setEnabled(true);
		bnAdd.setEnabled(true);
		bnUpdate.setEnabled(true);
		bnSave.setEnabled(false);
		bnCancel.setEnabled(false);
		bnLineOptr.setEnabled(false);
		bnDelete.setEnabled(true);
		bnAudit.setEnabled(true);
		bnUnAudit.setEnabled(false);
		//bnReturn.setEnabled(true);
		//bnHelp.setEnabled(true);
		bnCopy.setEnabled(true);
		bnSendAudit.setEnabled(isExistAuditFlow());
	}
	//����״̬
	if (status == AUDITED) {
		bnBrowse.setEnabled(true);
		bnQuery.setEnabled(true);
		bnFirst.setEnabled(isQuery && !isList);
		bnPrev.setEnabled(isQuery && !isList);
		bnNext.setEnabled(isQuery && !isList);
		bnLast.setEnabled(isQuery && !isList);
		bnPrint.setEnabled(true);
		bnPreview.setEnabled(true);
		bnAdd.setEnabled(true);
		bnUpdate.setEnabled(false);
		bnSave.setEnabled(false);
		bnCancel.setEnabled(false);
		bnLineOptr.setEnabled(false);
		bnDelete.setEnabled(false);
		bnAudit.setEnabled(false);
		bnUnAudit.setEnabled(true);
		//bnReturn.setEnabled(true);
		//bnHelp.setEnabled(true);
		bnCopy.setEnabled(true);
		bnSendAudit.setEnabled(false);
		getBillCardPanel().setEnabled(false);
	}
	//��������״̬
	if(status==AUDITING){
		bnBrowse.setEnabled(true);
		bnQuery.setEnabled(true);
		bnFirst.setEnabled(isQuery && !isList);
		bnPrev.setEnabled(isQuery && !isList);
		bnNext.setEnabled(isQuery && !isList);
		bnLast.setEnabled(isQuery && !isList);
		bnPrint.setEnabled(true);
		bnPreview.setEnabled(true);
		bnAdd.setEnabled(true);
		bnUpdate.setEnabled(false);
		bnSave.setEnabled(false);
		bnCancel.setEnabled(false);
		bnLineOptr.setEnabled(false);
		bnDelete.setEnabled(false);
		bnAudit.setEnabled(true);
		bnUnAudit.setEnabled(false);
		bnCopy.setEnabled(true);
		bnSendAudit.setEnabled(false);
		getBillCardPanel().setEnabled(false);
	}
	//��λ��ť
	if ( (!isList && getBillCardPanel().getRowCount()>0) || (isList && getBillListPanel().getHeadBillModel().getRowCount()>0) ){
		bnLocate.setEnabled(true);
		bnAuditFlowStatus.setEnabled(true);
		boDocument.setEnabled(true);
	}
	else{
		bnLocate.setEnabled(false);
		bnAuditFlowStatus.setEnabled(false);
		boDocument.setEnabled(false);
	}

	if ( (status == NEW || status == UPDATED) || (!isList && getBillCardPanel().getRowCount()>0) || (isList && getBillListPanel().getHeadBillModel().getRowCount()>0) ){
		if ("���Ŷ���".equals(SA13)){
			bnAffectCorp.setEnabled(false);
		}
		else{
			bnAffectCorp.setEnabled(true);
		}
		bnAdjustAnalyse.setEnabled(true);
	}
	else{
		bnAffectCorp.setEnabled(false);
		bnAdjustAnalyse.setEnabled(false);
	}

	//�����ж��Ƿ��������,����
	String pkid=null;
	if (isList) {
		pkid =	(String)getBillListPanel().getHeadBillModel().getValueAt(iCurrRow, "cadjpriceid");
		
		bnReturn.setName(nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCommon", "UPPSCMCommon-000463")
		/* @res "��Ƭ��ʾ" */	);
		bnReturn.setHint(nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCommon", "UPPSCMCommon-000463")
		/* @res "��Ƭ��ʾ" */	);
	}
	else{
		pkid =	(String)getBillCardPanel().getHeadItem("cadjpriceid").getValueObject();
		bnReturn.setName(nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCommon", "UPPSCMCommon-000464")
		/*@res "�б���ʾ" */);
		bnReturn.setHint(nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCommon", "UPPSCMCommon-000464")
		/*@res "�б���ʾ" */);
	}

	if (pkid!=null && pkid.trim().length()>0){
		//����,�޸�״̬�µ�����ť��������ť
		if (isExistAuditFlow() && (status == FREE || status == UPDATED) ){
			int workflowstatus=PfUtilClient.queryWorkFlowStatus(m_defbusitype,SO2BillType.SaleAdjustPrice, pkid);
			if (workflowstatus==IWorkFlowStatus.BILL_NOT_IN_WORKFLOW
					|| workflowstatus==IWorkFlowStatus.NOT_APPROVED_IN_WORKFLOW){
				bnSendAudit.setEnabled(true);
				bnAudit.setEnabled(false);
			}
			else{
				bnSendAudit.setEnabled(false);
				bnAudit.setEnabled(true);
			}
		}
	}

	if (isList)
		bnReturn.setEnabled(true);
	else{
		if (status == NEW || status == UPDATED) {
			bnReturn.setEnabled(false);
		}
		else{
			bnReturn.setEnabled(true);
		}
	}
	//
	//������ʾ������ݵ�����
	if (!isList){
		if (status == NEW)
			setSelrefshow(getBillCardPanel(),0);
		else
			setSelrefshow(getBillCardPanel(),1);
	}
	if(!"0001".equals(getCorpPrimaryKey()))
	{
		bnAffectCorp.setEnabled(false);
	}

	{// ά��������ֹ��ť״̬
		// ��������������״̬������ֹʱ���ڵ�ǰʱ��֮��ſ���
		AdjustpriceHeaderVO curHeadVO = getCurHeadVO();
		boolean enabled = !isAdjustBase
						&& AUDITED == status
						&& null != curHeadVO
						&& curHeadVO.getStopDateTime().after(ClientEnvironment.getServerTime());
		bnModifySpecialStopTime.setEnabled(enabled);
	}


	updateButtons();
}
/**
 * ���ñ����е����Ƿ���ʾ
 * �������ڣ�(2004-1-16 9:44:26)
 */
public void setBodyCol() {

	if(isList){
		BillListData bd=getBillListPanel().getBillListData();
		//bd.getBodyItem("cinvclassname").setShow(!isAdjustBase);

		bd.getBodyItem("brelatebase").setShow(!isAdjustBase);
		bd.getBodyItem("noriginalprice").setShow(!isAdjustBase);

		bd.getHeadItem("cpricetariffid").setShow(false);
		bd.getHeadItem("cpricetariffname").setShow(isAdjustBase);

		/*
		 * �޸ģ���ǿ��
		 * ���ڣ�2008-7-30
		 * ԭ�����ӵ���׼�ۿ�
		 */
		{
			// �۸����ʾ
			bd.getBodyItem("sourceprice").setShow(!isAdjustDiscount);
			// ��׼�ۿ�ֻ�е�����ʱ����ʾ
			bd.getBodyItem("nbasediscount").setShow(!isAdjustBase);
			// �۸�ָ������ʾ
			bd.getBodyItem("npricefactor").setShow(!isAdjustDiscount);
			// �۸�ӳɲ���ʾ
			bd.getBodyItem("npriceadd").setShow(!isAdjustDiscount);
			// ������ʾΪ��׼�ۿ�
			String baseName = isAdjustDiscount ? NCLangRes.getInstance().getStrByID("403003", "UPT40060501-000069"/*��׼�ۿ�*/) : NCLangRes.getInstance().getStrByID("common", "UC000-0001308"/*����*/);
			bd.getBodyItem("nbaseprice").setName(baseName);
			// �¼۸���ʾΪ���ۿ�
			String newName = isAdjustDiscount ? NCLangRes.getInstance().getStrByID("403003", "UPT40060501-000070"/*���ۿ�*/) : NCLangRes.getInstance().getStrByID("common", "UC000-0002298"/*�¼۸�*/);
			bd.getBodyItem("nnewprice").setName(newName);
			
		}

		getBillListPanel().setListData(bd);
		getBillListPanel().updateUI();

//		getBillListPanel().getBillListData().getBodyItem("brelatebase").setShow(!isAdjustBase);
//		getBillListPanel().getBillListData().getBodyItem("noriginalprice").setShow(!isAdjustBase);
//		getBillListPanel().getBillListData().getHeadItem("cpricetariffid").setShow(false);
//		getBillListPanel().getBillListData().getHeadItem("cpricetariffname").setShow(!isAdjustBase);
//		getBillListPanel().updateUI();
	}
	else{
		BillData bd=getBillCardPanel().getBillData();
		//bd.getBodyItem("cinvclassname").setShow(!isAdjustBase);
		bd.getBodyItem("brelatebase").setShow(!isAdjustBase);
		bd.getBodyItem("noriginalprice").setShow(!isAdjustBase);
		bd.getHeadItem("dexpiredate").setEnabled(!isAdjustBase);
		bd.getHeadItem("dexpiredate").setEdit(!isAdjustBase);
		bd.getHeadItem("dtstoptime").setEnabled(!isAdjustBase);
		bd.getHeadItem("dtstoptime").setEdit(!isAdjustBase);
		if (isAdjustBase) {
			bd.getHeadItem("dexpiredate").setValue(null);
			bd.getHeadItem("dtstoptime").setValue(null);
		}

		bd.getHeadItem("cpricetariffid").setShow(true);
		bd.getHeadItem("cpricetariffname").setShow(true);

		/*
		 * �޸ģ���ǿ��
		 * ���ڣ�2008-7-30
		 * ԭ�����ӵ���׼�ۿ�
		 */
		{
			// �۸����ʾ
			bd.getBodyItem("sourceprice").setShow(!isAdjustDiscount);
			// ��׼�ۿ�ֻ�е�����ʱ����ʾ
			bd.getBodyItem("nbasediscount").setShow(!isAdjustBase);
			// �۸�ָ������ʾ
			bd.getBodyItem("npricefactor").setShow(!isAdjustDiscount);
			// �۸�ӳɲ���ʾ
			bd.getBodyItem("npriceadd").setShow(!isAdjustDiscount);
			// ������ʾΪ��׼�ۿ�
			String baseName = isAdjustDiscount ? NCLangRes.getInstance().getStrByID("403003", "UPT40060501-000069"/*��׼�ۿ�*/) : NCLangRes.getInstance().getStrByID("common", "UC000-0001308"/*����*/);
			bd.getBodyItem("nbaseprice").setName(baseName);
			// �¼۸���ʾΪ���ۿ�
			String newName = isAdjustDiscount ? NCLangRes.getInstance().getStrByID("403003", "UPT40060501-000070"/*���ۿ�*/) : NCLangRes.getInstance().getStrByID("common", "UC000-0002298"/*�¼۸�*/);
			bd.getBodyItem("nnewprice").setName(newName);
			
		}

		getBillCardPanel().setBillData(bd);
//		BillData bdnew=getBillCardPanel().getBillData();
		getBillCardPanel().updateUI();


//		getBillCardPanel().getBillData().getBodyItem("brelatebase").setShow(!isAdjustBase);
//		getBillCardPanel().getBillData().getBodyItem("noriginalprice").setShow(!isAdjustBase);
//		getBillCardPanel().getBillData().getHeadItem("dexpiredate").setShow(!isAdjustBase);
//		getBillCardPanel().getBillData().getHeadItem("cpricetariffid").setShow(!isAdjustBase);
//		getBillCardPanel().getBillData().getHeadItem("cpricetariffname").setShow(!isAdjustBase);
//		if (isAdjustBase) getBillCardPanel().getBillData().getHeadItem("dexpiredate").setValue(null);
//		getBillCardPanel().updateUI();

//		getBillCardPanel().getBodyItem("brelatebase").setShow(!isAdjustBase);
//		getBillCardPanel().getBodyItem("noriginalprice").setShow(!isAdjustBase);
//		getBillCardPanel().getHeadItem("dexpiredate").setShow(!isAdjustBase);
//		getBillCardPanel().getHeadItem("cpricetariffid").setShow(!isAdjustBase);
//		getBillCardPanel().getHeadItem("cpricetariffname").setShow(!isAdjustBase);
//		if (isAdjustBase) getBillCardPanel().getHeadItem("dexpiredate").setValue(null);
//		getBillCardPanel().updateUI();


	}
}
/**
 * �����б�ѡ���е�����ť״̬��
 * �������ڣ�(2001-7-11 16:22:22)
 * @param iCurrRow int
 */
public void setButton(int iCurrRow) {
	String status = null;
	if (isList) {
		status = getBillListPanel().getHeadBillModel().getValueAt(iCurrRow, "fstatus").toString();
	} else {
		Integer iState = (Integer) getBillCardPanel().getTailItem("fstatus").getValueObject();
		status = null == iState ? "" : iState.toString();
	}
	if (nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UC001-0000027")/*@res "����"*/.equals(status) || "2".equals(status))
		iStatus = AUDITED;
	else if (nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UC001-0000005")/*@res "����"*/.equals(status) || "5".equals(status))
		iStatus = DELETED;
	else if (nc.ui.ml.NCLangRes.getInstance().getStrByID("scmsoprice","UPPscmsoprice-000289")/*@res "��������"*/.equals(status) || "7".equals(status))
		iStatus = AUDITING;
	else if ("".equals(status))
		iStatus = INIT;
	else
		iStatus = FREE;

	setBnStatus(iStatus);
}
/**
 * ����ĳ�пɱ༭
 * �������ڣ�(2001-4-27 18:30:57)
 */
public void setRowCustEdit(int m_irow) {
	//���ÿͻ�����Ϳͻ��ı༭Լ��
	String ccustclassid=(String)getBillCardPanel().getBillModel().getValueAt(m_irow,"ccustclassid");
	String ccustomerid=(String)getBillCardPanel().getBillModel().getValueAt(m_irow,"ccustomerid");
	if (ccustclassid!=null && ccustclassid.trim().length()>0){
		getBillCardPanel().setCellEditable(m_irow,"ccustclassname",true);
		getBillCardPanel().setCellEditable(m_irow,"ccustomername",false);
	}
	else if (ccustomerid!=null && ccustomerid.trim().length()>0){
		getBillCardPanel().setCellEditable(m_irow,"ccustclassname",false);
		getBillCardPanel().setCellEditable(m_irow,"ccustomername",true);
	}
	else{
		getBillCardPanel().setCellEditable(m_irow,"ccustclassname",true);
		getBillCardPanel().setCellEditable(m_irow,"ccustomername",true);
	}

	//���ô������ʹ���ı༭Լ��
	String cinvclassid=(String)getBillCardPanel().getBillModel().getValueAt(m_irow,"cinvclassid");
	String cinventoryid=(String)getBillCardPanel().getBillModel().getValueAt(m_irow,"cinventoryid");
	if (cinvclassid!=null && cinvclassid.trim().length()>0){
		getBillCardPanel().setCellEditable(m_irow,"cinvclassname",true);
		getBillCardPanel().setCellEditable(m_irow,"code",false);
		getBillCardPanel().setCellEditable(m_irow,"unit",true);
	}
	else if (cinventoryid!=null && cinventoryid.trim().length()>0){
		getBillCardPanel().setCellEditable(m_irow,"cinvclassname",false);
		getBillCardPanel().setCellEditable(m_irow,"code",true);
		getBillCardPanel().setCellEditable(m_irow,"unit",true);
	}
	else{
		getBillCardPanel().setCellEditable(m_irow,"cinvclassname",true);
		getBillCardPanel().setCellEditable(m_irow,"code",true);
		getBillCardPanel().setCellEditable(m_irow,"unit",false);
	}
}
/**
 * ���õ�ǰ�еı༭״̬
 * �������ڣ�(2004-2-2 9:37:57)
 */
public void setRowEditCol(int nrow) {

	String cinventoryid=(String)getBillCardPanel().getBodyValueAt(nrow,"cinventoryid");
	String cinvbasdocid=(String)getBillCardPanel().getBodyValueAt(nrow,"cinvbasdocid");
	String cinventorytypeid=(String)getBillCardPanel().getBodyValueAt(nrow,"cinvclassid");

	//��ǰ�д��ID��Ϊ��, ������޹�, ���ɱ༭: �¼۸���
	if ( (cinventoryid!=null && cinventoryid.trim().length()>0 ) ) {
		//getBillCardPanel().setCellEditable(nrow,"brelatebase",true);
		getBillCardPanel().setCellEditable(nrow,"nnewprice",true);
		getBillCardPanel().setCellEditable(nrow,"unit",true);
		Boolean brelatebase=(Boolean)getBillCardPanel().getBodyValueAt(nrow,"brelatebase");
		if (brelatebase.booleanValue()){
			getBillCardPanel().setCellEditable(nrow,"npricefactor",true);
			getBillCardPanel().setCellEditable(nrow,"npriceadd",true);
		}
		else{
			getBillCardPanel().setCellEditable(nrow,"npricefactor",false);
			getBillCardPanel().setCellEditable(nrow,"npriceadd",false);
		}
	}
	//��ǰ�д��IDΪ��, ���ɱ༭: ��������, �¼۸���
	else if ( (cinventorytypeid!=null && cinventorytypeid.trim().length()>0 ) ){
		String cunitid=(String)getBillCardPanel().getBodyValueAt(nrow,"cunitid");
		if (cunitid!=null && cunitid.trim().length()>0){
			//getBillCardPanel().setCellEditable(nrow,"brelatebase",true);
			getBillCardPanel().setCellEditable(nrow,"nnewprice",true);
			getBillCardPanel().setCellEditable(nrow,"unit",true);
			Boolean brelatebase=(Boolean)getBillCardPanel().getBodyValueAt(nrow,"brelatebase");
			if (brelatebase.booleanValue()){
				getBillCardPanel().setCellEditable(nrow,"npricefactor",true);
				getBillCardPanel().setCellEditable(nrow,"npriceadd",true);
			}
			else{
				getBillCardPanel().setCellEditable(nrow,"npricefactor",false);
				getBillCardPanel().setCellEditable(nrow,"npriceadd",false);
			}
		}
		else{
			//getBillCardPanel().setBodyValueAt(new UFBoolean("Y"),nrow,"brelatebase");
			//getBillCardPanel().setCellEditable(nrow,"brelatebase",false);
			getBillCardPanel().setCellEditable(nrow,"nnewprice",false);
			getBillCardPanel().setCellEditable(nrow,"npricefactor",true);
			getBillCardPanel().setCellEditable(nrow,"npriceadd",true);
			getBillCardPanel().setCellEditable(nrow,"unit",true);
		}
	}
	else if(nrow!=0){
		//getBillCardPanel().setBodyValueAt(new UFBoolean("Y"),nrow,"brelatebase");
		//getBillCardPanel().setCellEditable(nrow,"brelatebase",false);
		getBillCardPanel().setCellEditable(nrow,"nnewprice",false);
		getBillCardPanel().setCellEditable(nrow,"npricefactor",true);
		getBillCardPanel().setCellEditable(nrow,"npriceadd",true);
		getBillCardPanel().setCellEditable(nrow,"unit",false);
	}

	getBillCardPanel().updateUI();
}
/**
 * ���÷�������ʾ���
 * @param billcardpanel
 *    int showflag   1-��ʾ,0-����ʾ
 */
public void setSelrefshow(BillCardPanel bill, int iflag) {
	if (m_lastselrefflag==-1 || m_lastselrefflag!=iflag){
		for (int i=0;i<m_arselreflist.size();i++){
			String[] field=(String[])m_arselreflist.get(i);
			UIRefPane ref=null;
			if (field[1].equals("H"))
				ref=(UIRefPane)bill.getHeadItem(field[0]).getComponent();
			else if	(field[1].equals("B"))
				ref=(UIRefPane)bill.getBodyItem(field[0]).getComponent();
			else if (field[1].equals("T"))
				ref=(UIRefPane)bill.getTailItem(field[0]).getComponent();
			if (ref	!=null) {
				ref.getRefModel().setSealedDataShow(iflag==1);
			}
		}
		m_lastselrefflag=iflag;
	}
}
/**
 * ��ʾ�б��е��۵���
 * �������ڣ�(2001-7-11 13:46:20)
 * @param iRow int
 */
public void showBillAtRow(int iRow) throws Exception{
	Object pk =getBillListPanel().getHeadBillModel().getValueAt(iRow, "cadjpriceid");
	if(pk!=null){
		voAdjustprice = AdjustpriceBO_Client.findByPrimaryKey(pk.toString());
		if (voAdjustprice != null)
			getBillCardPanel().setBillValueVO(voAdjustprice);
		getBillCardPanel().execHeadLoadFormulas();
		getBillCardPanel().getBillModel().execLoadFormula();
		getBillCardPanel().setEnabled(false);
		getBillCardPanel().updateValue();
	}
	

	setButton(iRow);
}

/**
 * ����˵��: ���۵��Ƿ�������������
 * ��������:
 * ����ֵ����:
 * @author xiegx 2004-12-30
 */
public boolean isExistAuditFlow(){
	if (m_isExistAuditFlow!=null)
		return m_isExistAuditFlow.booleanValue();

 	 String strPk_Corp = ClientEnvironment.getInstance().getCorporation().getPk_corp();
	 String strUserID = ClientEnvironment.getInstance().getUser().getPrimaryKey();
	 UFDate loginDate = ClientEnvironment.getInstance().getBusinessDate();
	 boolean isExist = false;

	 try {


	    isExist = nc.ui.pub.pf.PfUtilBO_Client.isExistWorkFlow(
	         getBillType(), m_defbusitype,
	         strPk_Corp, strUserID);
	    m_isExistAuditFlow=new UFBoolean(isExist);
	 }catch(Exception e){
	 	SCMEnv.out(e.getMessage());
//	 	showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("scmsoprice","UPPscmsoprice-000352",null,new String[]{e.getLocalizedMessage()})/*@res "��ѯ�����Ƿ�������������������:{0}"*/);
	 	return false;
	 }
	return isExist;
}

protected ScmPrintlogVO getScmPrintlogVO(){

    ScmPrintlogVO voaSpl=new ScmPrintlogVO();
    String sPk = getBillCardPanel().getHeadItem("cadjpriceid").getValue();
    if((sPk==null)||(sPk.trim().length()==0)){
        showWarningMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON","UPPSCMCommon-000189")/*@res "�뱣���ˢ�µ����ٴ�ӡ"*/);
        return null;
    }
    voaSpl.setCbillid(sPk);//���������ID
    voaSpl.setCbilltypecode(getBillType());
    voaSpl.setCoperatorid(ce.getUser().getCorpId());
    voaSpl.setIoperatetype(new Integer(PrintConst.PAT_OK));//�̶�
    voaSpl.setPk_corp(ce.getCorporation().getPk_corp());
    voaSpl.setTs(getBillCardPanel().getHeadItem("ts").getValue());//���������TS

    return voaSpl;

}

//ʵ�ֽӿ�
//IFreshTsListener
public void freshTs(String sBillID,String sTS, Integer iPrintCount){
    if(sTS!=null)
        getBillCardPanel().setHeadItem("ts",sTS);
    if(iPrintCount!=null)
        getBillCardPanel().setHeadItem("iprintcount",iPrintCount);
}


public void onAddLineBatch() {
	if (!isAdjustBase){
		String dvalidate=getBillCardPanel().getHeadItem("dvalidate").getValue();
		String dexpiredate=getBillCardPanel().getHeadItem("dexpiredate").getValue();
		if (dvalidate.length()==0){
			showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("scmsoprice","UPPscmsoprice-000273")/*@res "��ʼʱ�䲻��Ϊ�գ�"*/);
			return;
		}
		if (dvalidate.length()!=0&&dexpiredate.length()==0){
			showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("scmsoprice","UPPscmsoprice-000272")/*@res "��ֹʱ�䲻��Ϊ�գ�"*/);
			return;
		}

	}
	String cpricetariffid=(String)getBillCardPanel().getHeadItem("cpricetariffid").getValue();
	if (cpricetariffid==null|| cpricetariffid .length()==0){
		showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("scmsoprice","UPPscmsoprice-000801")/*@res "��Ŀ����Ϊ�գ�"*/);
		return;
	}
	if (m_dlgQuick==null){
		m_dlgQuick=new OptionChoseUI(ce.getCorporation().getPk_corp(),new Boolean(isAdjustBase), isAdjustDiscount, this.getParent());
		m_dlgQuick.setInitData(m_localcurrid);
		if (!isAdjustBase && bAdjSpecMustNRelateBase!=null && bAdjSpecMustNRelateBase.booleanValue())
			m_dlgQuick.initDisRelateBase();
	}
	if(m_dlgQuick.showModal()==OptionChoseUI.ID_OK){
		QuickInputResultVO ar=m_dlgQuick.getConditions();
		this.showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCommon","UPPSCMCommon-000465")/*
	             * @res
	             * "����������..."
	             */);
		addLineBatch(ar);
	}
}

private void addLineBatch(QuickInputResultVO resultVO){
//	QuickInputResultVO resultVO = new QuickInputResultVO();
/*
	//λ�ã��������˳��0,�Ƿ�ͻ�/�ͻ�����;1���ͻ�/�ͻ����飬2��ѡ����Ǵ��/������࣬3��ѡ��Ĵ��������������飬4���Ƿ��������أ�5���۸����б�6������, 7,������λ
	Boolean isCus=(Boolean)arCond.get(0);
	Object [] vCustOrCustClass=(Object [])arCond.get(1);
	Boolean isInv=(Boolean)arCond.get(2);
	Object[] objs=(Object[])arCond.get(3);

	InvDisplayVO[] values=new InvDisplayVO[objs.length];
	for (int i=0;i<objs.length;i++)
		values[i]=(InvDisplayVO)objs[i];

	Boolean isRelatebase=(Boolean)arCond.get(4);
	Vector vPricetype=(Vector)arCond.get(5);
	String strCurrPk=(String)arCond.get(6);
	String strMeasPk=(String)arCond.get(7);
//*/
	boolean isCus = resultVO.isCustomerSelected();
	InvDisplayVO[] vCustOrCustClass = isCus ? resultVO.getCustomers() : resultVO.getCustomerGroups();
	boolean isInv = resultVO.isInventorySelected();
	InvDisplayVO[] values = isInv ? resultVO.getInventories() : resultVO.getInventoryClasses();
	PriceType[] priceTypes = resultVO.getPriceTypes();
	boolean isRelatebase = resultVO.isBaseRelative();
	String strCurrPk = resultVO.getCurrency();
	String strMeasPk = resultVO.getMeasure();

	hsPricetypeVosBR.clear();
	for (PriceType priceType : priceTypes) {
		PricetypeVO prtvo = priceType.getPricetypeVO();
		hsPricetypeVosBR.put(prtvo.getCpricetypeid(), prtvo);
	}

	BillModel bm=getBillCardPanel().getBillModel();
	int iRowCountBefore=bm.getRowCount();

/*
	hsPricetypeVosBR.clear();
	for (int iprt=0;iprt<vPricetype.size();iprt++){
		Vector vd=(Vector)vPricetype.get(iprt);
		PricetypeVO prtvo=(PricetypeVO)vd.get(0);
		hsPricetypeVosBR.put(prtvo.getCpricetypeid(),prtvo);
	}
//*/
	ArrayList<Integer> arExeInv=new ArrayList<Integer>();
	ArrayList<Integer> arMeasInv=new ArrayList<Integer>();
	ArrayList<Integer> arMeasInput=new ArrayList<Integer>();
	
	//"cinventoryid","cunitid","cinvclassid","ccustclassid","ccustomerid","creceiptareaid","csaleorganid","csrcpriceid","ccurrencyid"
	for (int iInv=0;iInv<values.length;iInv++){
		InvDisplayVO invVo=values[iInv];
		int ictCustClass=0;
		while (true){
			for (PriceType priceType : priceTypes){
//				Vector vd=(Vector)vPricetype.get(iprt);
//				PricetypeVO prtvo=(PricetypeVO)vd.get(0);
//				UFDouble dPriceFactor=(UFDouble)vd.get(1);
//				UFDouble dPriceAdd=(UFDouble)vd.get(2);
//				UFDouble dPriceNewPrice=(UFDouble)vd.get(3);

				PricetypeVO prtvo = priceType.getPricetypeVO();
				UFDouble dPriceFactor = priceType.getFactor();
				UFDouble dPriceAdd = priceType.getAdd();
				UFDouble dPriceNewPrice = priceType.getNewPrice();

				getBillCardPanel().addLine();

				int newRow=bm.getRowCount()-1;
				if (vCustOrCustClass!=null && vCustOrCustClass.length>0)
				{
					if(isCus){
						bm.setValueAt(vCustOrCustClass[ictCustClass].getPk_invmandoc(),newRow,"ccustomerid");
					}else{
						bm.setValueAt(vCustOrCustClass[ictCustClass].getPk_invcl(),newRow,"ccustclassid");
					}
					
				}
				if (isInv){
					bm.setValueAt(invVo.getPk_invmandoc(),newRow,"cinventoryid");
					bm.setValueAt(invVo.getPk_invbasdoc(),newRow,"cinvbasdocid");
					bm.setValueAt(invVo.getInvcode(),newRow,"code");
				}
				else{
					bm.setValueAt(invVo.getPk_invcl(),newRow,"cinvclassid");
					bm.setValueAt(strMeasPk,newRow,"cunitid");
				}
				bm.setValueAt(strCurrPk,newRow,"ccurrencyid");

				bm.setValueAt(prtvo.getCpricetypeid(),newRow,"csrcpriceid");
				if (dPriceNewPrice!=null && dPriceNewPrice.doubleValue()>0)
					bm.setValueAt(dPriceNewPrice,newRow,"nnewprice");
				else{
					if (dPriceFactor!=null && dPriceFactor.doubleValue()>0)
						bm.setValueAt(dPriceFactor,newRow,"npricefactor");
					else
						bm.setValueAt(new UFDouble(1),newRow,"npricefactor");
					if (dPriceAdd!=null && dPriceAdd.doubleValue()!=0)
						bm.setValueAt(dPriceAdd,newRow,"npriceadd");
					else
						bm.setValueAt(new UFDouble(0),newRow,"npriceadd");
				}
				if (!isAdjustBase){
					if (bAdjSpecMustNRelateBase!=null && bAdjSpecMustNRelateBase.booleanValue())
						bm.setValueAt(new UFBoolean("N"),newRow,"brelatebase");
					else
						bm.setValueAt(new UFBoolean(isRelatebase),newRow,"brelatebase");
					//bm.setValueAt(new UFBoolean(isRelatebase.booleanValue()),newRow,"brelatebase");
				}
				if (isInv){
					arExeInv.add(new Integer(newRow));
					if (strMeasPk!=null ){
						getBillCardPanel().getBillModel().setValueAt(strMeasPk,newRow,"cunitid");
						arMeasInput.add(new Integer(newRow));
						//getBillCardPanel().getBillModel().execFormulas(newRow, getBillCardPanel().getBodyItem("cunitid").getLoadFormula());
					}
					else{
						arMeasInv.add(new Integer(newRow));
					}
				}
				else{
					arMeasInput.add(new Integer(newRow));
					arExeInv.add(new Integer(newRow));
					//getBillCardPanel().getBillModel().execFormulas(newRow, getBillCardPanel().getBodyItem("cinvclassid").getLoadFormula());
					//getBillCardPanel().getBillModel().execFormulas(newRow, getBillCardPanel().getBodyItem("cunitid").getLoadFormula());
				}

//				//�ж��Ƿ������ͬ��¼
//				if (isExistsRowBefore(iRowCountBefore,newRow)){
//					bm.delLine(new int[]{newRow});
//				}
//				else{
//					//���õ�ǰ�����еĿɱ༭��״̬
//					if (!isAdjustBase){
//						setRowEditCol(getBillCardPanel().getRowCount()-1);
//					}
//				}
			}
			//�ͻ������ѭ����Ϊ��Ҳ��Ҫѭ��һ�Σ����Բ���whileѭ��
			ictCustClass++;
			if (vCustOrCustClass==null || vCustOrCustClass.length==0 || ictCustClass>=vCustOrCustClass.length)
				break;
		}
	}

	if (arExeInv.size()>0){
		if (isInv){
			int[] invrows=new int[arExeInv.size()];
			for (int i=0;i<arExeInv.size();i++){
				invrows[i]=((Integer)arExeInv.get(i)).intValue();
			}
			if ("0001".equals(getCorpPrimaryKey())){
				ClientCacheHelper.getColValueBatch(getBillCardPanel(),invrows,
					new String[]{"name","size","type"},"bd_invbasdoc","pk_invbasdoc",new String[]{"invname","invspec","invtype"},"cinventoryid");
			}
			else{
				ClientCacheHelper.getColValueBatch(getBillCardPanel(),invrows,
						new String[]{"cinvbasdocid"},"bd_invmandoc","pk_invmandoc",new String[]{"pk_invbasdoc"},"cinventoryid");
				ClientCacheHelper.getColValueBatch(getBillCardPanel(),invrows,
						new String[]{"name","size","type"},"bd_invbasdoc","pk_invbasdoc",new String[]{"invname","invspec","invtype"},"cinvbasdocid");
			}
		}
		else{
			int[] invrows=new int[arExeInv.size()];
			for (int i=0;i<arExeInv.size();i++){
				invrows[i]=((Integer)arExeInv.get(i)).intValue();
			}
			ClientCacheHelper.getColValueBatch(getBillCardPanel(),invrows,
					new String[]{"cinvclassname"},"bd_invcl","pk_invcl",new String[]{"invclassname"},"cinvclassid");
		}
	}
	if (arMeasInv.size()>0){
		int[] invrows=new int[arMeasInv.size()];
		for (int i=0;i<arMeasInv.size();i++){
			invrows[i]=((Integer)arMeasInv.get(i)).intValue();
		}

		ClientCacheHelper.getColValueBatch(getBillCardPanel(),invrows,
				new String[]{"cunitid"},"bd_invbasdoc","pk_invbasdoc",new String[]{"pk_measdoc"},"cinvbasdocid");
		ClientCacheHelper.getColValueBatch(getBillCardPanel(),invrows,
				new String[]{"unit"},"bd_measdoc","pk_measdoc",new String[]{"measname"},"cunitid");
	}
	if (arMeasInput.size()>0){
		int[] invrows=new int[arMeasInput.size()];
		for (int i=0;i<arMeasInput.size();i++){
			invrows[i]=((Integer)arMeasInput.get(i)).intValue();
		}

		ClientCacheHelper.getColValueBatch(getBillCardPanel(),invrows,
				new String[]{"unit"},"bd_measdoc","pk_measdoc",new String[]{"measname"},"cunitid");
	}
	

	int iRowCountAfter=bm.getRowCount();
	for (int row=iRowCountAfter-1;row>=iRowCountBefore;row--){
		//�ж��Ƿ������ͬ��¼
		if (isExistsRowBefore(iRowCountBefore,row)){
			bm.delLine(new int[]{row});
		}
	}
	
	iRowCountAfter=bm.getRowCount();
	
//	for (int row=iRowCountBefore;row<iRowCountAfter;row++){
//		//���õ�ǰ�����еĿɱ༭��״̬
//		if (!isAdjustBase){
//			setRowEditCol(row);
//		}
//	}
	
	if (iRowCountAfter>iRowCountBefore){
		bnCancel.setEnabled(true);
		updateButton(bnCancel);
		showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("scmsoprice","UPPscmsoprice-000353",null,new String[]{String.valueOf((iRowCountAfter-iRowCountBefore))})/*@res "����{0}��"*/);
	}
	else{
		return;
	}
	
	int[] newRows=new int[iRowCountAfter-iRowCountBefore];
	//ִ�й�ʽ, ���ø��еı༭״̬
	for (int row=iRowCountBefore;row<iRowCountAfter;row++){
		newRows[row-iRowCountBefore]=row;
		getBillCardPanel().getBillModel().execFormulas(row, getBillCardPanel().getBodyItem("ccustomerid").getLoadFormula());
//		getBillCardPanel().getBillModel().execFormulas(row, getBillCardPanel().getBodyItem("csrcpriceid").getLoadFormula());
//		getBillCardPanel().getBillModel().execFormulas(row, getBillCardPanel().getBodyItem("ccurrencyid").getLoadFormula());
	}
	
	ClientCacheHelper.getColValueBatch(getBillCardPanel(),newRows,
			new String[]{"sourceprice"},"prm_pricetype","cpricetypeid",new String[]{"cpricetypename"},"csrcpriceid");
	ClientCacheHelper.getColValueBatch(getBillCardPanel(),newRows,
			new String[]{"ccurrencyname"},"bd_currtype","pk_currtype",new String[]{"currtypename"},"ccurrencyid");
	ClientCacheHelper.getColValueBatch(getBillCardPanel(),newRows,
			new String[]{"ccustclassname"},"bd_defdoc","pk_defdoc",new String[]{"docname"},"ccustclassid");
//	ClientCacheHelper.getColValueBatch(getBillCardPanel(),newRows,
//			new String[]{"ccustomerid"},"bd_cubasdoc","pk_cubasdoc",new String[]{"custname"},"ccustomerid");
//	
	//����ѯ��
	int[] rows=new int[iRowCountAfter-iRowCountBefore];
	for (int i=iRowCountBefore;i<iRowCountAfter;i++){
		rows[i-iRowCountBefore]=i;
	}
	if (isAdjustBase){
		findPriceBaseBatch(rows);
		/*
		 * �޸ģ���ǿ��
		 * ���ڣ�2008-8-7
		 * ԭ��V55��ʼ��������(������׼�ۿ�)�ĵ��۵������û��ƥ�䵽��Ŀ��������
		 */
//		int irowninq=0;
//		for (int i=iRowCountBefore;i<iRowCountAfter;i++){
//			String strinq=getBillCardPanel().getBillModel().getValueAt(i,"binqusucess").toString();
//			UFBoolean binq=new UFBoolean(strinq);
//			if (binq!=null && !binq.booleanValue())
//				irowninq++;
//		}
//		if (irowninq>0){
//			showWarningMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("scmsoprice","UPPscmsoprice-000354",null,new String[]{String.valueOf(irowninq)})/*@res "���ٵ�����{0}��,û�в�ѯ����Ӧ��Ŀ����Ŀ����ɾ��!"*/);
//		}
//		for (int i=iRowCountAfter-1;i>=iRowCountBefore;i--){
//			String strinq=getBillCardPanel().getBillModel().getValueAt(i,"binqusucess").toString();
//			UFBoolean binq=new UFBoolean(strinq);
//			if (binq!=null && !binq.booleanValue()) {
//				getBillCardPanel().getBillModel().delLine(new int[]{i});
//			}
//		}
	}
	else{
		if (isRelatebase){
			findSPBasePriceBatch(rows);
		}
		else
			findOranginPriceBatch(rows);
	}
	//���¼����¼۸�򷴹�������ӳ�
	reCalcPriceAfterQuickInput(rows);
	//ִ�й�ʽ, ���ø��еı༭״̬
	for (int row=iRowCountBefore;row<iRowCountAfter;row++){
		if(getBillCardPanel().getBillModel().getRowCount()>0){
//			if (isAdjustBase){
			//���ø��еı༭״̬
			setRowEditCol(row);
			//���û����еı༭״̬
			setRowCustEdit(row);
//		}
//		else{
//			//���û����еı༭״̬
//			setRowCustEdit(row);
//		}
		}

	}
	//getBillCardPanel().getBillModel().execLoadFormula();
	getBillCardPanel().updateUI();

}

private boolean isExistsRowBefore(int ictrowBeforeInsert,int newRow){
	if (ictrowBeforeInsert<=0)
		return false;
	if (newRow<ictrowBeforeInsert)
		return false;
	for (int irow=0;irow<ictrowBeforeInsert;irow++){
		if (isEqualRow(irow,newRow))
			return true;
	}
	return false;
}


private boolean isEqualRow(int oldRow,int newRow){
	//�Ƚϵ��ֶ�
	//�ͻ����飬�ͻ��������������࣬���֣��۸��������λ��������֯���ջ�����
	String[] colnames=null;
	if (isAdjustBase)
		colnames=new String[]{"cinventoryid","cunitid","cinvclassid","ccustclassid","ccustomerid","creceiptareaid","csaleorganid","csrcpriceid","ccurrencyid"};
	else
		colnames=new String[]{"cinventoryid","cunitid","cinvclassid","ccustclassid","ccustomerid","creceiptareaid","csaleorganid","ccurrencyid"};
	for (int i=0;i<colnames.length;i++){
		String oldValue=(String)getBillCardPanel().getBillModel().getValueAt(oldRow,colnames[i]);
		String newValue=(String)getBillCardPanel().getBillModel().getValueAt(newRow,colnames[i]);
		if (oldValue==null){
			if (newValue!=null && newValue.trim().length()>0)
				return false;
		}
		else{
			if (!oldValue.equals(newValue))
				return false;
		}
	}

	return true;
}


/**
 * ����������ѯ��
 * @ version (00-6-6 13:33:25)
 */
public void findPriceBaseBatch(int[] rows) {
	if (rows==null || rows.length ==0)
		return;
	String cpricetariffid=getBillCardPanel().getHeadItem("cpricetariffid").getValueObject().toString();
	UFBoolean bCurrTariff=new UFBoolean(false);
	if (m_curtariffvo==null)
		initCurrtariff();
	if (cpricetariffid!=null && cpricetariffid.trim().length()>0){
		if (m_curtariffvo!=null && cpricetariffid.equals(m_curtariffvo.getCpricetariffid()))
			bCurrTariff=new UFBoolean(true);
	}
	else{
		bCurrTariff=new UFBoolean(true);
		if (m_curtariffvo==null){
			//showErrorMessage("ѯ�۷�������û��ָ����Ŀ��ϵͳ��Ҳû�е�ǰ��Ŀ��.ѯ��ʧ�ܣ�");
			return;
		}
	}

	String[] conds=new String[rows.length];
	for (int i=0;i<rows.length;i++){
		int row=rows[i];
//		String cinvbasdocid=(String)getBillCardPanel().getBodyValueAt(row,"cinvbasdocid");
		String cinvclassid=(String)getBillCardPanel().getBodyValueAt(row,"cinvclassid");
//		String csourcepriceid=(String)getBillCardPanel().getBodyValueAt(row,"csrcpriceid");
		String ccurrencyid=(String)getBillCardPanel().getBodyValueAt(row,"ccurrencyid");
		String cmeasdocid=(String)getBillCardPanel().getBodyValueAt(row,"cunitid");
		//�ϳ�SQL���
		String cinventoryid=(String)getBillCardPanel().getBodyValueAt(row,"cinventoryid");

		String csaleorganid=(String)getBillCardPanel().getBodyValueAt(row,"csaleorganid");
		String creceiptareaid=(String)getBillCardPanel().getBodyValueAt(row,"creceiptareaid");
		String ccustomerid=(String)getBillCardPanel().getBodyValueAt(row,"ccustomerid");
		String ccustclassid=(String)getBillCardPanel().getBodyValueAt(row,"ccustclassid");
//		String cpricefield=(String)getBillCardPanel().getBodyValueAt(row,"cpricefield");

		String cond="";
		if (cinventoryid!=null)
			cond=" cinventoryid='"+cinventoryid+"' ";
		else
			cond=" cinvclassid='"+cinvclassid+"' ";

		cond+=" and ccurrencyid='"+ccurrencyid+"' ";
		cond+=" and cmeasdocid='"+cmeasdocid+"' ";
		if (!isEmptyWithTrim(cpricetariffid))
			cond+=" and cpricetariffid='"+cpricetariffid+"' ";
		else
			cond+=" and cpricetariffid='"+m_curtariffvo.getCpricetariffid()+"' ";
		if (!isEmptyWithTrim(csaleorganid))
			cond+=" and csaleorganid='"+csaleorganid+"' ";
		else
			cond+=" and csaleorganid ='"+NullStr.ID+"' ";
		if (!isEmptyWithTrim(creceiptareaid))
			cond+=" and creceiptareaid='"+creceiptareaid+"' ";
		else
			cond+=" and creceiptareaid ='"+NullStr.ID+"' ";
		if (!isEmptyWithTrim(ccustomerid))
			cond+=" and ccustomerid='"+ccustomerid+"' ";
		else
			cond+=" and ccustomerid ='"+NullStr.ID+"' ";
		if (!isEmptyWithTrim(ccustclassid))
			cond+=" and ccustclassid='"+ccustclassid+"' ";
		else
			cond+=" and ccustclassid ='"+NullStr.ID+"' ";
		conds[i]=cond;
	}

	AdjFindPriceResultVO[] vos;
	/*
	 * �޸ģ���ǿ��
	 * ���ڣ�2008-7-31
	 * ԭ�򣺿��Ե�����׼�ۿ�
	 */
	if (isAdjustDiscount) {
		vos = findDiscountBatch(conds);
	} else {
		vos = FindAdjBaseBatch(conds, bCurrTariff, rows);
	}

	if (vos!=null && vos.length>0){
		for (int i=0;i<rows.length;i++){
			int row=rows[i];
			if (vos[i].getResultFlag() == FIND_SUCCESS){
				UFDouble ufbprice=vos[i].getRetPrice();
				if (ufbprice!=null && ufbprice.doubleValue()>0){
					getBillCardPanel().getBillModel().setValueAt(ufbprice,row,"nbaseprice");
					getBillCardPanel().setCellEditable(row,"nnewprice",true);
					getBillCardPanel().setCellEditable(row,"npricefactor",true);
					getBillCardPanel().setCellEditable(row,"npriceadd",true);
					getBillCardPanel().getBillModel().setValueAt(UFBoolean.TRUE,row,"binqusucess");
				} else {
					getBillCardPanel().getBillModel().setValueAt(new UFDouble(isAdjustDiscount ? 100 : 0),row,"nbaseprice");
					getBillCardPanel().getBillModel().setValueAt(UFBoolean.FALSE,row,"binqusucess");
				}
			} else {
				getBillCardPanel().getBillModel().setValueAt(new UFDouble(isAdjustDiscount ? 100 : 0),row,"nbaseprice");
				getBillCardPanel().getBillModel().setValueAt(UFBoolean.FALSE,row,"binqusucess");
			}
		}
	}
}


public void findSPBasePriceBatch(int[] rows) {
	SalePriceVO[] salepriceVOs = new SalePriceVO[rows.length];
	for (int i=0;i<rows.length;i++){
		int row=rows[i];
//		UFDouble saleprice = null;
		//���ò�ѯ����0
		SalePriceVO salepriceVO = new SalePriceVO();
		//��˾
		salepriceVO.setCropID(getCorpPrimaryKey());
		//ҵ������

		//�ͻ�
		String strCustomerID = (String) getBillCardPanel().getBillModel().getValueAt(row, "ccustomerid");
		salepriceVO.setCustomerID(strCustomerID);
		String ccustbasid = (String) getBillCardPanel().getBillModel().getValueAt(row, "ccustbasid");
		salepriceVO.setCustomerBaseID(ccustbasid);
		//�ͻ�����
		String ccustomertype = (String) getBillCardPanel().getBillModel().getValueAt(row, "ccustclassid");
		salepriceVO.setCustomerClass(ccustomertype);
		//����
		String strCurrencyID = null;
		strCurrencyID = (String) getBillCardPanel().getBillModel().getValueAt(row, "ccurrencyid");
		salepriceVO.setCurrencyID(strCurrencyID);
		//���������ID
		String strInventoryID = (String)getBillCardPanel().getBodyValueAt(row, "cinventoryid");
		salepriceVO.setInventoryID(strInventoryID);
		//�����������ID
		String strInvbasID = (String)getBillCardPanel().getBodyValueAt(row, "cinvbasdocid");
		salepriceVO.setInventoryBaseID(strInvbasID);
		//�������ID
		String strInvclassID = (String)getBillCardPanel().getBodyValueAt(row, "cinvclassid");
		salepriceVO.setInventoryClass(strInvclassID);

		//ϵͳ����
		salepriceVO.setSystemData(getClientEnvironment().getDate());
		//��Ч�ڴ�
		UFDate dstartdate = null;
		dstartdate = new UFDate(getBillCardPanel().getHeadItem("dvalidate").getValue());
		salepriceVO.setDatefrom(dstartdate);
		//�۸���
		String pricetype = (String) getBillCardPanel().getBodyValueAt(row, "csrcpriceid");
		salepriceVO.setPriceTypeid(pricetype);
		//ѯ�ۼ�����λ
		String cpriceunitid = (String) getBillCardPanel().getBodyValueAt(row, "cunitid");
		salepriceVO.setMeasdocid(cpriceunitid);

		//������֯
		String csaleorganid = (String) getBillCardPanel().getBodyValueAt(row, "csaleorganid");
		salepriceVO.setSaleStrucid(csaleorganid);
		//�ջ�����
		String creceiptareaid = (String) getBillCardPanel().getBodyValueAt(row, "creceiptareaid");
		salepriceVO.setReceiptAreaid(creceiptareaid);
		salepriceVO.setPricetariffid((String) (String)getBillCardPanel().getHeadItem("cpricetariffid").getValue());

		salepriceVOs[i]=salepriceVO;
	}

	try{
		PriceAskResultVO[] rvos = nc.ui.sp.pub.FindSalePriceBO_Client.askBaseprices(salepriceVOs);
		Object oTemp = null, o[] = null;
		Hashtable<String, Object> t = new Hashtable<String, Object>();
		for (int i=0;i<rows.length;i++){
			int row=rows[i];
			PriceAskResultVO rvo=rvos[i];
			if (rvo != null && rvo.getErrFlag().intValue() == 0) {
				UFDouble saleprice = rvo.getNum();
				getBillCardPanel().getBillModel().setValueAt(saleprice, row, "nbaseprice");
				getBillCardPanel().getBillModel().setValueAt(null, row, "noriginalprice");
				/*
				 * �޸ģ���ǿ��
				 * ���ڣ�2008-9-10
				 * ԭ�����ӻ�׼�ۿ۵���ʾ
				 */
				getBillCardPanel().getBillModel().setValueAt(rvo.getBaseDiscount(), row, "nbasediscount");

				getBillCardPanel().getBillModel().setValueAt(rvo.getPriceTypeid(), row, "csrcpriceid");
				if(rvo.getPriceTypeid() == null) continue;
				if(t.get(rvo.getPriceTypeid()) == null){
					oTemp = CacheTool.getCellValue("prm_pricetype", "cpricetypeid", "cpricetypename", rvo.getPriceTypeid());
					if(oTemp != null){
						o = (Object[])oTemp;
						if(o != null && o.length > 0){
							getBillCardPanel().getBillModel().setValueAt(o[0], row, "sourceprice");
							t.put(rvo.getPriceTypeid(), o[0]);
						}
					}	
				}else{
					getBillCardPanel().getBillModel().setValueAt(t.get(rvo.getPriceTypeid()), row, "sourceprice");
				}
				/*
				 * �޸ģ���ǿ��
				 * ���ڣ�2008-12-5
				 * CQ�ţ�NCdp200617122
				 * ԭ��ѯ�۳ɹ����óɹ���־
				 */
				getBillCardPanel().getBillModel().setValueAt(UFBoolean.TRUE, row, "binqusucess");
			}
		}
	} catch (Exception e) {
		SCMEnv.error(e);
	}
}


public void findOranginPriceBatch(int[] rows) {
	SpecialwarepriceVO[] arySpecialwarepriceVOs = new SpecialwarepriceVO[rows.length];
	for (int i=0;i<rows.length;i++){
		int row=rows[i];

		String cinvbasdocid = (String) getBillCardPanel().getBodyValueAt(row, "cinvbasdocid");
		String cinvclassid=(String)getBillCardPanel().getBodyValueAt(row,"cinvclassid");


		String csourcepriceid = (String) getBillCardPanel().getBodyValueAt(row, "csrcpriceid");

		Boolean brelatebase = (Boolean) getBillCardPanel().getBodyValueAt(row, "brelatebase");
		//ѯԭ��
		SpecialwarepriceVO arySpecialwarepriceVO = new SpecialwarepriceVO();
		//arySpecialwarepriceVO = (SpecialwarepriceVO) getBillCardPanel().getBillModel().getBodyValueRowVO(row,SpecialwarepriceVO.class.getName());
		AdjustpriceItemVO item =
			(AdjustpriceItemVO) getBillCardPanel().getBillModel().getBodyValueRowVO(row, AdjustpriceItemVO.class.getName());
		arySpecialwarepriceVO.setPk_corp(getCorpPrimaryKey());
		arySpecialwarepriceVO.setCinventoryid(item.getCinventoryid());
		arySpecialwarepriceVO.setCsourcepriceid(item.getCsrcpriceid());
		arySpecialwarepriceVO.setCinventorytypeid((String) item.getAttributeValue("cinvclassid"));
		arySpecialwarepriceVO.setCcustomertype((String) item.getAttributeValue("ccustclassid"));
		arySpecialwarepriceVO.setCcustomerid((String) item.getAttributeValue("ccustomerid"));
		arySpecialwarepriceVO.setcsaleorganid((String) item.getAttributeValue("csaleorganid"));
		arySpecialwarepriceVO.setcreceiptareaid((String) item.getAttributeValue("creceiptareaid"));
		arySpecialwarepriceVO.setcmeasdocid((String) (String) item.getAttributeValue("cunitid"));
		arySpecialwarepriceVO.setccurrencyid((String) item.getAttributeValue("ccurrencyid"));
		arySpecialwarepriceVO.setDstartdate(new UFDate(getBillCardPanel().getHeadItem("dvalidate").getValue()));
		arySpecialwarepriceVO.setDstopdate(new UFDate(getBillCardPanel().getHeadItem("dexpiredate").getValue()));
		arySpecialwarepriceVO.setCpricetariffid((String) getBillCardPanel().getHeadItem("cpricetariffid").getValue());
		arySpecialwarepriceVOs[i]=arySpecialwarepriceVO;


	}

	try {
		UFDouble[] saleprices = SpecialwarepriceBO_Client.getOrgSpecpriceFromsps(arySpecialwarepriceVOs, ce.getDate());
		for (int i=0;i<rows.length;i++){
			int row=rows[i];
			if (saleprices[i]!=null && saleprices[i].doubleValue()>0){
				getBillCardPanel().getBillModel().setValueAt(null, row, "nbaseprice");
				getBillCardPanel().getBillModel().setValueAt(saleprices[i], row, "noriginalprice");
			}
		}
	} catch (Exception e) {
		SCMEnv.error(e);
		return;
	}

}

/***
 *
 * ����˵��: ���ٵ��ۺ�����۸�֮��Ĺ�ϵ
 * ��������:
 * ����ֵ����:
 * @author xiegx 2005-1-27
 */
protected void reCalcPriceAfterQuickInput(int[] rows ){
	for (int i=0;i<rows.length;i++){
		int row=rows[i];
		UFDouble nnewprice=(UFDouble)getBillCardPanel().getBillModel().getValueAt(row,"nnewprice");
		if (isAdjustBase){
			if (nnewprice!=null && nnewprice.doubleValue()>0){
				getBillCardPanel().getBillModel().setValueAt(new UFDouble(1),row,"npricefactor");
				getBillCardPanel().getBillModel().setValueAt(new UFDouble(0),row,"npriceadd");
				String[] formula = {"npriceadd->nnewprice-nbaseprice*npricefactor"};
				getBillCardPanel().getBillModel().execFormula(row,formula);
			}
			else{
				String[] formula = {"nnewprice->npriceadd+nbaseprice*npricefactor"};
				getBillCardPanel().getBillModel().execFormula(row,formula);
			}
		}
		else{
			Boolean brelatebase=(Boolean)getBillCardPanel().getBodyValueAt(row,"brelatebase");
			if (brelatebase.booleanValue()){
				if (nnewprice!=null && nnewprice.doubleValue()>0){
					getBillCardPanel().getBillModel().setValueAt(new UFDouble(1),row,"npricefactor");
					getBillCardPanel().getBillModel().setValueAt(new UFDouble(0),row,"npriceadd");
					/*
					 * �޸ģ���ǿ��
					 * ���ڣ�2008-9-10
					 * ԭ�򣺴�����ʽ���ǻ�׼�ۿ�
					 */
//					String[] formula = {"npriceadd->nnewprice-nbaseprice*npricefactor"};
					String[] formula = {"npriceadd->nnewprice-nbaseprice*npricefactor*nbasediscount/100"};
					getBillCardPanel().getBillModel().execFormula(row,formula);
				}
				else{
					/*
					 * �޸ģ���ǿ��
					 * ���ڣ�2008-9-10
					 * ԭ�򣺴�����ʽ���ǻ�׼�ۿ�
					 */
//					String[] formula = {"nnewprice->npriceadd+nbaseprice*npricefactor"};
					String[] formula = {"nnewprice->npriceadd+nbaseprice*npricefactor*nbasediscount/100"};
					getBillCardPanel().getBillModel().execFormula(row,formula);
				}
			}
		}
	}

}

private AdjFindPriceResultVO[] FindAdjBaseBatch(String[] conds,UFBoolean bCurrTariff, int[] rows){
	if (conds==null || conds.length==0)
		return null;

	AdjFindPriceResultVO[] results=new AdjFindPriceResultVO[conds.length];
	for (int i=0;i<conds.length;i++){
		AdjFindPriceResultVO vo=new AdjFindPriceResultVO();
		vo.setResultFlag(OCCUR_ERROR/*new Integer(3)*/);
		results[i]=vo;
	}
	PrmtariffItemVO[] vos=null;
	try{
		//vos=TariffBO_Client.queryTariffList(conds[0],bCurrTariff);
		vos=TariffBO_Client.queryTariffListBatch(conds);
	}
	catch( Exception e){
		SCMEnv.out(e.getMessage());
		//showErrorMessage("ѯԭ��ʧ��!");
		for (int i=0;i<conds.length;i++){
			results[i].setMsgInfo(e.getMessage());
		}
		return results;
	}

	if (vos!=null && vos.length>0){
		for (int i=0;i<vos.length;i++){
			int row=rows[i];
			if (vos[i]!=null){
				String csourcepriceid=(String)getBillCardPanel().getBodyValueAt(row,"csrcpriceid");
				if (csourcepriceid==null){
					csourcepriceid=vos[i].getCdefpricetypeid();
					getBillCardPanel().getBillModel().setValueAt(csourcepriceid,row,"csrcpriceid");
					/**
					 * �޸ģ�������
					 * ���ڣ�2009-9-1
					 * CQ�ţ�
					 * ԭ��ѭ�����ù�ʽʱ������̫��Ӧ��һ��ִ�������еġ��۸����ʽ
					 */
					
//					getBillCardPanel().execBodyFormulas(row, getBillCardPanel().getBodyItem("csrcpriceid").getLoadFormula());
				}
				String cpricefield=null;
				UFDouble ufbprice=null;
				if (!hsPricetypeVosBR.containsKey(csourcepriceid)){
					try{
						nc.vo.sp.sp001.PricetypeVO[] allData=PricetypeBO_Client.queryAll(ce.getCorporation().getPk_corp());
						if (allData!=null)
							for (int j=0;j<allData.length;j++){
								if (!hsPricetypeVosBR.containsKey(allData[j].getCpricetypeid())){
									hsPricetypeVosBR.put(allData[j].getCpricetypeid(),allData[j]);
								}
							}
					}catch(Exception e){
						SCMEnv.out(e.getMessage());
						showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("scmsoprice","UPPscmsoprice-000316")/*@res "��ѯ�۸������"*/);
					}
				}

				if (hsPricetypeVosBR.containsKey(csourcepriceid)){
					PricetypeVO prtvo=(PricetypeVO)hsPricetypeVosBR.get(csourcepriceid);
					cpricefield=prtvo.getCpricefield();
					ufbprice=(UFDouble)vos[i].getAttributeValue(cpricefield);
					getBillCardPanel().getBillModel().setValueAt(cpricefield,row,"cpricefield");
					if (ufbprice!=null && ufbprice.doubleValue()>0){
						results[i].setResultFlag(FIND_SUCCESS/*new Integer(0)*/);
						results[i].setRetPrice(ufbprice);
						results[i].setAskPricetarifflistid(vos[i].getCpricetariff_bid());
					}
					else{
						results[i].setResultFlag(NO_PRICE_TYPE/*new Integer(1)*/);
						results[i].setMsgInfo(nc.ui.ml.NCLangRes.getInstance().getStrByID("scmsoprice","UPPscmsoprice-000317")/*@res "��ѯ���۸���۸���Ϊ�գ�"*/);
					}

				}
				else{
					results[i].setResultFlag(OCCUR_ERROR/*new Integer(3)*/);
					results[i].setMsgInfo(nc.ui.ml.NCLangRes.getInstance().getStrByID("scmsoprice","UPPscmsoprice-000318")/*@res "û�ж���۸��"*/);
				}
			}
			else{
				results[i].setResultFlag(NO_PRICE/*new Integer(2)*/);
				results[i].setMsgInfo(nc.ui.ml.NCLangRes.getInstance().getStrByID("scmsoprice","UPPscmsoprice-000319")/*@res "û�в�ѯ����¼"*/);
			}
		}
		/**
		 * �޸ģ�������
		 * ���ڣ�2009-9-1
		 * CQ�ţ�
		 * ԭ��һ��ִ�������еġ��۸����ʽ
		 */
		getBillCardPanel().getBillModel().execFormulas(getBillCardPanel().getBodyItem("csrcpriceid").getLoadFormula(), rows[0], rows[rows.length - 1]);
	}
	return results;
}

private void initParam(){
	try{
		Hashtable hspar=SysInitBO_Client.queryBatchParaValues(getCorpPrimaryKey(),new String[]{"SA13","BD505","BD101","BD301","SA38","BD211"});
		if (hspar.containsKey("SA13"))
			SA13=(String)hspar.get("SA13");
		if (hspar.containsKey("SA38"))
			SA38=new UFBoolean(hspar.get("SA38").toString());
		if ("0001".equals(getCorpPrimaryKey())){
			if (hspar.containsKey("BD211"))
				BD301=(String)hspar.get("BD211");
		}
		else{
			if (hspar.containsKey("BD301"))
				BD301=(String)hspar.get("BD301");
		}
		if (hspar.containsKey("BD101"))
			BD101=(String)hspar.get("BD101");
		if (hspar.containsKey("BD505"))
			BD505=new Integer(hspar.get("BD505").toString());
		
	}catch(Exception e){
		SCMEnv.out(e.getMessage());
	}
	
}

private boolean isNodeEnable(){
	boolean b = false;
	try {
		boolean bgrprc = "���Ŷ���".equals(SA13);/*-=notranslate=-*/
		boolean bgroup = "0001".equals(getCorpPrimaryKey());
		if ("���Ŷ���".equals(SA13))/*-=notranslate=-*/
			b = bgroup;
		else
			if ("��˾����".equals(SA13))/*-=notranslate=-*/
				b = !bgroup;
			else
				b = true;
	} catch (Exception e) {
		SCMEnv.out(e.getMessage());
	}
	return b;
}

private BillTempletVO getBilltempVO(){
	if (mbilltempvo==null){
		mbilltempvo=cpAdjust.getTempletData(SaleBillType.SaleAdjustPrice,"null",getClientEnvironment().getUser().getPrimaryKey(),getClientEnvironment().getCorporation().getPrimaryKey());
	}
	return mbilltempvo;
}

private void onAuditMsg(){
	   String[] smsg =  null;
	   try{
		   Object osend = CacheTool.getCellValue("sm_user","user_code","cuserid",sendman);
		   String ssendid = null;
		   if (osend!=null) ssendid = osend.toString();
	       //PfWorkflowBO_Client.queryCurrTrans(getBillType(),getVo(),ssendid);
	   }catch(Exception ex){
	       smsg = new String[]{""};
	       SCMEnv.out(ex);
	   }
	   if (smsg == null) smsg = new String[]{""};
	   msgdlg = new ShowMsgDlg(this.getParent(),getTitle(),smsg);
	   msgdlg.showModal();
	}

public boolean beforeEdit(BillItemEvent e) {
//	 if (e.getItem().getKey().equals("dtstarttime")){
//			if(!isAdjustBase)
//				getBillCardPanel().getHeadItem("dtstarttime").setEnabled(false);
//			else{
//				
//			}
//	 }
	return true;
}

//private void onModifyBatch() {
//	nc.ui.sp.pub.ModifyBatchDlg modifyBatchDlg=new nc.ui.sp.pub.ModifyBatchDlg(getBillCardPanel());
//	int[] n=getBillCardPanel().getBillTable().getSelectedRows();
//	if(n==null||n.length==0){
//		showErrorMessage("û��ѡ��Ҫ���޸ĵ���!");
//		return;
//	}
//	if(modifyBatchDlg.showModal()==nc.ui.pub.beans.UIDialog.ID_OK){
//		UFDouble[] uds = new UFDouble[n.length];
//		//��¼ ԭ�����¼۸�
//		for(int i=0;i<n.length;i++){
//			uds[i]=(UFDouble)getBillCardPanel().getBodyValueAt(n[i], "nnewprice");
//		}
//		//Ѱ��
//		for(int i=0;i<n.length;i++){
//			ReAskPrice(n[i], n[i]+1);
//		}
//		//��д�¼۸�
//		for(int i=0;i<n.length;i++){
//			getBillCardPanel().setBodyValueAt(uds[i],n[i], "nnewprice");
//		}
//		//���¼���
//			reCalcPriceAfterQuickInput(n);
//	}
//}
protected void ReAskPrice(int iRowCountBefore, int iRowCountAfter) {
	//����ѯ��
	int[] rows=new int[iRowCountAfter-iRowCountBefore];
	for (int i=iRowCountBefore;i<iRowCountAfter;i++){
		rows[i-iRowCountBefore]=i;
	}
	if (isAdjustBase){
		findPriceBaseBatch(rows);
		int irowninq=0;
		for (int i=iRowCountBefore;i<iRowCountAfter;i++){
			String strinq=getBillCardPanel().getBillModel().getValueAt(i,"binqusucess").toString();
			UFBoolean binq=new UFBoolean(strinq);
			if (binq!=null && !binq.booleanValue())
				irowninq++;
		}
//		if (irowninq>0){
//			showWarningMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("scmsoprice","UPPscmsoprice-000354",null,new String[]{String.valueOf(irowninq)})/*@res "���ٵ�����{0}��,û�в�ѯ����Ӧ��Ŀ����Ŀ����ɾ��!"*/);
//		}
//		for (int i=iRowCountAfter-1;i>=iRowCountBefore;i--){
//			String strinq=getBillCardPanel().getBillModel().getValueAt(i,"binqusucess").toString();
//			UFBoolean binq=new UFBoolean(strinq);
//			if (binq!=null && !binq.booleanValue())
//				getBillCardPanel().getBillModel().delLine(new int[]{i});
//		}
	}
	else{
		/*
		 * �޸ģ���ǿ��
		 * ���ڣ�2008-11-26
		 * ԭ��Ӧ�������Ƿ�����������ѯ��
		 */
//		if (isAdjustBase){
//			findSPBasePriceBatch(rows);
//		}
//		else
//			findOranginPriceBatch(rows);

		// �����ڻ�����ص���
		List<Integer> brelatebaseRows = new ArrayList<Integer>();
		// ����������޹ص���
		List<Integer> notbrelatebaseRows = new ArrayList<Integer>();
		for (int row : rows) {
			Boolean brelatebase = (Boolean) getBillCardPanel().getBodyValueAt(row, "brelatebase");
			if (null != brelatebase && brelatebase.booleanValue()) {
				brelatebaseRows.add(row);
			} else {
				notbrelatebaseRows.add(row);
			}
		}
		{ // ������أ������������ص�ѯ�۷���
			int[] rRows = new int[brelatebaseRows.size()];
			for (int i = 0; i < rRows.length; i++) {
				rRows[i] = brelatebaseRows.get(i);
			}
			findSPBasePriceBatch(rRows);
		}
		{// �����޹أ�����ѯԭ�۵ķ���
			int[] nrRows = new int[notbrelatebaseRows.size()];
			for (int i = 0; i < nrRows.length; i++) {
				nrRows[i] = notbrelatebaseRows.get(i);
			}
			findOranginPriceBatch(nrRows);
		}
	}
	//���¼����¼۸�򷴹�������ӳ�
	reCalcPriceAfterQuickInput(rows);
	//ִ�й�ʽ, ���ø��еı༭״̬
	for (int row=iRowCountBefore;row<iRowCountAfter;row++){
		if(getBillCardPanel().getBillModel().getRowCount()>0){
//			if (isAdjustBase){
			//���ø��еı༭״̬
			setRowEditCol(row);
			//���û����еı༭״̬
			setRowCustEdit(row);
//		}
//		else{
//			//���û����еı༭״̬
//			setRowCustEdit(row);
//		}
		}

	}
	//getBillCardPanel().getBillModel().execLoadFormula();
	getBillCardPanel().updateUI();

}

	/**
	 * ����ѯ�����ۿ�
	 * 
	 * @param conds ��ѯ����
	 * @return ��ѯ���VO����
	 * @author ��ǿ��
	 * @since 2008-7-31
	 */
	private AdjFindPriceResultVO[] findDiscountBatch(String[] conds) {
		AdjFindPriceResultVO[] resultVOs = new AdjFindPriceResultVO[conds.length];
		for (int i = 0; i < resultVOs.length; i++) {
			resultVOs[i] = new AdjFindPriceResultVO();
		}
		DiscountlistVO[] discountlistVOs = null;
		try {
			discountlistVOs = DiscountBOClient.queryDiscountListBatch(conds);
		} catch (Exception e) {
			SCMEnv.error(e.getMessage());
			SCMEnv.error(e);
			for (AdjFindPriceResultVO resultVO : resultVOs) {
				resultVO.setMsgInfo(e.getMessage());
				resultVO.setResultFlag(OCCUR_ERROR);
			}
		}
		for (int i = 0; i < discountlistVOs.length; i++) {
			if (null != discountlistVOs[i]) {
				resultVOs[i].setRetPrice(discountlistVOs[i].getBasediscount());
				resultVOs[i].setResultFlag(FIND_SUCCESS);
			} else {
				resultVOs[i].setResultFlag(NO_PRICE);
				resultVOs[i].setMsgInfo(NCLangRes.getInstance().getStrByID("scmsoprice","UPPscmsoprice-000319")/*@res "û�в�ѯ����¼"*/);
			}
		}
		return resultVOs;
	}
}