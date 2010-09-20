package nc.ui.ic.ic201;

import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import nc.bs.framework.common.NCLocator;
import nc.itf.pps.IPricStl;
import nc.ui.ic.auditdlg.ClientUIInAndOut;
import nc.ui.ic.ic211.GeneralHHelper;
import nc.ui.ic.pub.bill.GeneralBillClientUI;
import nc.ui.ic.pub.bill.GeneralBillHelper;
import nc.ui.ic.pub.bill.GeneralBillUICtl;
import nc.ui.ic.pub.bill.GeneralButtonManager;
import nc.ui.ic.pub.bill.IButtonManager;
import nc.ui.ic.pub.bill.ICButtonConst;
import nc.ui.ic.pub.bill.QueryDlgHelp;
import nc.ui.ic.pub.bill.initref.RefFilter;
import nc.ui.ic.pub.tools.GenMethod;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.FramePanel;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillModel;
import nc.ui.querytemplate.valueeditor.DefaultFieldValueEditor;
import nc.ui.scm.jjpanel.InfoCostPanel;
import nc.vo.ic.pub.bill.GeneralBillHeaderVO;
import nc.vo.ic.pub.bill.GeneralBillItemVO;
import nc.vo.ic.pub.bill.GeneralBillVO;
import nc.vo.ic.pub.bill.IItemKey;
import nc.vo.ic.pub.bill.QryInfoConst;
import nc.vo.ic.pub.billtype.BillTypeFactory;
import nc.vo.ic.pub.billtype.IBillType;
import nc.vo.ic.pub.lang.ResBase;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.constant.ic.BillMode;
import nc.vo.scm.constant.ic.InOutFlag;
import nc.vo.scm.ic.bill.InvVO;
import nc.vo.scm.jjvo.InformationCostVO;
import nc.vo.scm.pub.SCMEnv;
import nc.vo.scm.pub.session.ClientLink;

/**
 * �˴���������˵���� �������ڣ�(2001-11-23 15:39:43)
 * 
 * @author�����˾�
 * 
 */
public class ClientUI extends nc.ui.ic.pub.bill.GeneralBillClientUI {
//	protected ButtonObject m_boRatioOut = new ButtonObject(
//			nc.ui.ml.NCLangRes.getInstance().getStrByID("40080602",
//					"UPT40080804-000004")/* @res "��ȳ���" */,
//			nc.ui.ml.NCLangRes.getInstance().getStrByID("40080602",
//					"UPT40080804-000004")/* @res "��ȳ���" */, 0, "��ȳ���");/*-=notranslate=-*/

	// ���׽���Ի���
	private nc.ui.ic.auditdlg.ClientUIInAndOut m_dlgInOut = null;

	// �����תҵ������id
	private ArrayList m_alBrwLendBusitype = null;

	// ҵ������itemkey
	private final String m_sBusiTypeItemKey = "cbiztype";

	private IButtonManager m_buttonManager;
	
	private ButtonObject boInfoCost = null;//����¼�밴ť  add by QuSida 2010-9-5 (��ɽ����)
	
	private UFDouble number = null;
	
	private ButtonObject[] extendBtns ; //���ο�����ť����  add by QuSida 2010-8-28 (��ɽ����)
	
	
	
	private ButtonObject getBoInfoCost(){
		if(boInfoCost == null){
			//����¼�밴ť add by QuSida 2010-8-10  ����ɽ���ܣ�
			boInfoCost = new ButtonObject("����¼��","����¼��","����¼��");
			return boInfoCost;
		}
		 return boInfoCost;
	}
	
	/**
	 * ClientUI2 ������ע�⡣
	 */
	public ClientUI() {
		super();
		initialize();
		   getBoInfoCost().setEnabled(false);
	}
  
  /**
   * ClientUI ������ע�⡣
   * add by liuzy 2007-12-18 ���ݽڵ�����ʼ������ģ��
   */
  public ClientUI(FramePanel fp) {
   super(fp);
   initialize();
   getBoInfoCost().setEnabled(false);
  }

	/**
	 * ClientUI ������ע�⡣ nc 2.2 �ṩ�ĵ������鹦�ܹ����ӡ�
	 * 
	 */
	public ClientUI(String pk_corp, String billType, String businessType,
			String operator, String billID) {
		super(pk_corp, billType, businessType, operator, billID);

	}

	/**
	 * ����¼�뼯�вɹ��ı����ֶ�
	 */

	public void setBodyDefaultData(int istartrow, int count) {

		 //�鿴�Ƿ��������
		  if (getBillCardPanel().getRowCount()>0) {
		   Object obj = getBillCardPanel().getBodyValueAt(0,IItemKey.CSOURCEBILLBID);
		   if (obj!=null&&obj.toString().trim().length()>0)
		    return;
		  }
		  
		String pk_corp = getBillCardPanel().getHeadItem(IItemKey.PKCORP)
				.getValue();
		String calid = getBillCardPanel().getHeadItem("pk_calbody").getValue();
		String whid = getBillCardPanel().getHeadItem("cwarehouseid").getValue();

		for (int i = istartrow; i < count; i++) {
			// �������˾��Ϊ��,���ܴ����ͷ��ֵ
			String reqCorp = (String) getBillCardPanel().getBodyValueAt(i,IItemKey.REQ_CORP);
			if (reqCorp==null||reqCorp.trim().length()==0)  {
				getBillCardPanel().setBodyValueAt(pk_corp, i, IItemKey.REQ_CORP);
				getBillCardPanel().getBillModel().execEditFormulaByKey(i,
						IItemKey.REQ_CORPNAME);
			}
			//req_cal
			String reqCal = (String) getBillCardPanel().getBodyValueAt(i,IItemKey.REQ_CAL);
			if (reqCal==null)  {
				getBillCardPanel().setBodyValueAt(calid, i, IItemKey.REQ_CAL);
				getBillCardPanel().getBillModel().execEditFormulaByKey(i,
						IItemKey.REQ_CALNAME);
			}
			// req_wh
			String reqWh = (String) getBillCardPanel().getBodyValueAt(i,IItemKey.REQ_WH);
			if (reqWh==null)  {
				getBillCardPanel().setBodyValueAt(whid, i, IItemKey.REQ_WH);
				getBillCardPanel().getBillModel().execEditFormulaByKey(i,
						IItemKey.REQ_WHNAME);
			}

			// inv_corp
			String invCorp = (String) getBillCardPanel().getBodyValueAt(i,IItemKey.INV_CORP);
			if (invCorp==null)  {
				getBillCardPanel().setBodyValueAt(pk_corp, i, IItemKey.INV_CORP);
				getBillCardPanel().getBillModel().execEditFormulaByKey(i,
						IItemKey.INV_CORPNAME);
			}

		}
	}

	protected void afterBillEdit(nc.ui.pub.bill.BillEditEvent e) {
		String sItemKey = e.getKey();
		int row = e.getRow();
		if ("vbodynote2".equalsIgnoreCase(sItemKey)) {
			nc.ui.pub.beans.UIRefPane refPaneReasonBody = (nc.ui.pub.beans.UIRefPane) getBillCardPanel()
					.getBodyItem("vbodynote2").getComponent();
			String sBodyNoteCode = refPaneReasonBody.getText();
			String sBodyNoteCodeContent = (String) refPaneReasonBody
					.getRefValue("cbackreasonname");

			String sReturnResult = null;
			// ����ж�Ӧ������
			if (sBodyNoteCodeContent != null)
				sReturnResult = sBodyNoteCodeContent;
			else if (sBodyNoteCode != null) // ����ı���û�ж�Ӧ���˿����ɣ�����Ϊֱ����������
				sReturnResult = sBodyNoteCode;
			// ���Ϸ��ؽ��
			if (sReturnResult != null) {
				getBillCardPanel().setBodyValueAt(sReturnResult, row,
						"vbodynote2");
			}

		}
		if("ninnum".equalsIgnoreCase(sItemKey)){
		    //add by QuSida 2010-9-5 (��ɽ����)  --- begin
		    //function ����������޸ĺ�ʱ���·�����Ϣ�е�����
		    int temp = getBillCardPanel().getBillModel("table").getRowCount();
		   number = new UFDouble(0.0);
//		    UFDouble plannumber = new UFDouble(0.0);
//		    UFDouble taxmny = null;
		    UFDouble mny = null;
		    UFDouble inmny = null;

		    
		    for (int i = 0; i < temp; i++) {
		    	number = number.add(new UFDouble((getBillCardPanel().getBodyValueAt(i,"ninnum")==null?0.0:getBillCardPanel().getBodyValueAt(i,"ninnum")).toString()));    
//		    	plannumber = plannumber.add(new UFDouble(getBillCardPanel().getBodyValueAt(i,"nplanarrvnum").toString()));    
		    	  
			}
		    temp = getBillCardPanel().getBillModel("jj_scm_informationcost").getRowCount();
		    UFDouble innum = ((GeneralButtonManager)getButtonManager()).getArrnum(); 
		    int length = 0;
		    if(((GeneralButtonManager)getButtonManager()).getInfovos()!=null){
		    	length = ((GeneralButtonManager)getButtonManager()).getInfovos().length;
		    }		    
		    for (int i = length; i < temp; i++) {
		    	Boolean ismny = (Boolean)getBillCardPanel().getBillModel("jj_scm_informationcost").getValueAt(i, "ismny");		    	
		    	getBillCardPanel().getBillModel("jj_scm_informationcost").setValueAt(number, i, "nnumber");
		    	if(ismny == null || !ismny){
		    	mny = new UFDouble(getBillCardPanel().getBillModel("jj_scm_informationcost").getValueAt(i,"noriginalcurprice").toString()).multiply(number);
		    	inmny = new UFDouble(getBillCardPanel().getBillModel("jj_scm_informationcost").getValueAt(i,"noriginalcurprice").toString()).multiply(number.add(innum==null?new UFDouble(0.0):innum));
//		    	taxmny = new UFDouble(getBillCardPanel().getBodyValueAt(i,"noriginalcurtaxprice").toString()).multiply(arrnumber);
		    	
		    	
		    	
//		    	getBillCardPanel().getBillModel("jj_scm_informationcost").setValueAt(taxmny, i, "noriginalcursummny");
		    	getBillCardPanel().getBillModel("jj_scm_informationcost").setValueAt(mny, i, "noriginalcurmny");
		    	
//		    	getBillCardPanel().getBillModel("jj_scm_informationcost").setValueAt(taxmny, i, "ninvoriginalcursummny");
		    	getBillCardPanel().getBillModel("jj_scm_informationcost").setValueAt(inmny, i, "ninstoreoriginalcurmny");
		    	}else{
		    		UFDouble price =  new UFDouble(getBillCardPanel().getBillModel("jj_scm_informationcost").getValueAt(i, "noriginalcurmny").toString()).div(number);
		    		getBillCardPanel().getBillModel("jj_scm_informationcost").setValueAt(price, i, "noriginalcurprice");	
		    
		    	}
		    	
		        
		    }
		    //add by QuSida 2010-9-5 (��ɽ����)  --- end
		}

	}

	/**
	 * �����ߣ����˾� ���ܣ������塢�б��ϱ�༭�¼����� ������e ���ݱ༭�¼� ���أ� ���⣺ ���ڣ�(2001-5-8 19:08:05)
	 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 * 
	 * 
	 * 
	 */
	@Override
	public void bodyRowChange(nc.ui.pub.bill.BillEditEvent e) {
		super.bodyRowChange(e);

		// // ����������Խ�һ�б����У�����ѡ������жϣ����£����״̬��δǩ�֣�δ�������׵ĵ���,����Դ��������Դ��������ģ�鵥��
		// // ���װ�ť���á�
		// if (e.getSource() == getBillCardPanel().getBillTable()
		// && m_boDispense != null) {
		// int rownum = e.getRow();
		// if (BillMode.Browse == m_iMode && isSigned() != SIGNED
		// && isSetInv(m_voBill, rownum)
		// && !isDispensedBill(m_voBill, rownum)
		// && getSourBillTypeCode() != null
		// && !getSourBillTypeCode().startsWith("4")
		// ) {
		// m_boDispense.setEnabled(true);
		//
		// } else {
		// m_boDispense.setEnabled(false);
		// }
		// updateButton(m_boDispense);
		// }
	}

	/**
	 * �����ߣ����˾� ���ܣ����󷽷�������ǰ��VO��� �����������浥�� ���أ� ���⣺ ���ڣ�(2001-5-24 ���� 5:17)
	 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 */
	protected boolean checkVO(nc.vo.ic.pub.bill.GeneralBillVO voBill) {
		return this.checkVO();
	}

	/**
	 * �����ߣ����˾� ���ܣ��õ���ѯ�Ի��� ������ ���أ� ���⣺ ���ڣ�(2001-5-9 9:23:32) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 * 
	 * 
	 * 
	 * 
	 */
//	protected QueryConditionDlgForBill getConditionDlg() {
//		if (ivjQueryConditionDlg == null) {
//			ivjQueryConditionDlg = super.getConditionDlg();
//			ivjQueryConditionDlg.setCombox("freplenishflag", new String[][] {
//					{
//							nc.vo.ic.pub.BillTypeConst.BILLNORMAL,
//							nc.ui.ml.NCLangRes.getInstance().getStrByID(
//									"4008busi", "UPP4008busi-000367") /*
//																		 * @res
//																		 * "���"
//																		 */},
//					{
//							nc.vo.ic.pub.BillTypeConst.BILLSENDBACK,
//							nc.ui.ml.NCLangRes.getInstance().getStrByID(
//									"4008busi", "UPT40080602-000014") /*
//																		 * @res
//																		 * "�˿�"
//																		 */},
//					{
//							nc.vo.ic.pub.BillTypeConst.BILLALL,
//							nc.ui.ml.NCLangRes.getInstance().getStrByID(
//									"4008busi", "UPPSCMCommon-000217") /*
//																		 * @res
//																		 * "ȫ��"
//																		 */} });
//			// zhy2005-04-23�ɹ���ⵥ��Ҫ���˹�Ӧ��Ȩ��
//			// �Ȼ�����һ����Ӧ��
//			ivjQueryConditionDlg.setCorpRefs("head.pk_corp",
//					new String[] { "head.cproviderid" });
//		}
//
//		return ivjQueryConditionDlg;
//	}
  
  public QueryDlgHelp getQryDlgHelp() {
    if(m_qryDlgHelp==null){
      m_qryDlgHelp = new QueryDlgHelp(this){
        protected void init() {
          super.init();
          getQueryDlg().setCombox("freplenishflag", new String[][] {
              {
                  nc.vo.ic.pub.BillTypeConst.BILLNORMAL,
                  nc.ui.ml.NCLangRes.getInstance().getStrByID(
                      "4008busi", "UPP4008busi-000367") /*
                                         * @res
                                         * "���"
                                         */},
              {
                  nc.vo.ic.pub.BillTypeConst.BILLSENDBACK,
                  nc.ui.ml.NCLangRes.getInstance().getStrByID(
                      "4008busi", "UPT40080602-000014") /*
                                         * @res
                                         * "�˿�"
                                         */},
              {
                  nc.vo.ic.pub.BillTypeConst.BILLALL,
                  nc.ui.ml.NCLangRes.getInstance().getStrByID(
                      "4008busi", "UPPSCMCommon-000217") /*
                                         * @res
                                         * "ȫ��"
                                         */} });
          
          getQueryDlg().setInitDate("freplenishflag", nc.vo.ic.pub.BillTypeConst.BILLNORMAL);
          
          // zhy2005-04-23�ɹ���ⵥ��Ҫ���˹�Ӧ��Ȩ��
          // �Ȼ�����һ����Ӧ��
          getQueryDlg().setCorpRefs("head.pk_corp",
              new String[] { "head.cproviderid" });
        }
      };
    }
    return m_qryDlgHelp;
  }

	/**
	 * ���� ReturnDlg1 ����ֵ��
	 * 
	 * @return nc.ui.ic.auditdlg.ClientUIInAndOut
	 */
	/* ���棺�˷������������ɡ� */
	protected nc.ui.ic.auditdlg.ClientUIInAndOut getDispenseDlg(String sTitle,
			ArrayList alInVO, ArrayList alOutVO) {
		if (m_dlgInOut == null) {
			try {
				// user code begin {1}
				m_dlgInOut = new ClientUIInAndOut(this, sTitle);
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		// if (m_voBill == null)
		setM_voBill((GeneralBillVO) ((GeneralBillVO) getM_alListData()
				.get(getM_iLastSelListHeadRow())).clone());
		m_dlgInOut.setVO(getM_voBill(), alInVO, alOutVO, getBillType(), getM_voBill()
				.getPrimaryKey().trim(), getEnvironment().getCorpID(), getEnvironment().getUserID());
		m_dlgInOut.setName("BillDlg");
		// m_dlgInOut.setDefaultCloseOperation(javax.swing.WindowConstants.HIDE_ON_CLOSE);
		return m_dlgInOut;
	}

	/**
	 * ����ʵ�ָ÷���������ҵ�����ı��⡣
	 * 
	 * @version (00-6-6 13:33:25)
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getTitle() {
		return super.getTitle();
	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2003-11-19 9:10:04)
	 */
	public void initialize() {
		super.initialize();
		
		long lTime = System.currentTimeMillis();

		getBillCardPanel().addBodyEditListener2(this);		
		SCMEnv.showTime(lTime, "initialize:addBodyEditListener2:");

		lTime = System.currentTimeMillis();
		nc.ui.pub.beans.UIRefPane refPaneReason = (nc.ui.pub.beans.UIRefPane) getBillCardPanel()
				.getHeadItem("vheadnote2").getComponent();
		refPaneReason.setAutoCheck(false);
		refPaneReason.setReturnCode(false);
		nc.ui.pub.beans.UIRefPane refPaneReasonBody = (nc.ui.pub.beans.UIRefPane) getBillCardPanel()
				.getBodyItem("vbodynote2").getComponent();
		refPaneReasonBody.setAutoCheck(false);
		refPaneReasonBody.setReturnCode(true);
		SCMEnv.showTime(lTime, "initialize:init�˻�����:");/*-=notranslate=-*/
	}

	/**
	 * �����ߣ����˾� ���ܣ���ʼ��ϵͳ���� ������ ���أ� ���⣺ ���ڣ�(2001-5-9 9:23:32) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 * 
	 * 
	 * 
	 */
	protected void initPanel() {
		// ��Ҫ���ݲ���
		super.setNeedBillRef(true);
	}

	public String getBillType() {
		return nc.vo.ic.pub.BillTypeConst.m_purchaseIn;
	}

	public String getFunctionNode() {
		return "40080602";
	}

	public int getInOutFlag() {
		return InOutFlag.IN;
	}

	/**
	 * �����ߣ����˾� ���ܣ��Ƿ��ת����
	 * 
	 * ��һ����Ҫ���⡣
	 * 
	 * ������ ���أ� ���⣺ ���ڣ�(2001-11-24 12:15:42) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 */
	protected boolean isBrwLendBiztype() {
		try {
			GeneralBillVO voMyBill = null;
			// ҵ������
			String sBusitypeid = null;
			if (getM_iCurPanel() == BillMode.List) { // �б���ʽ��
				if (getM_alListData() != null && getM_iLastSelListHeadRow() >= 0
						&& getM_alListData().size() > getM_iLastSelListHeadRow()
						&& getM_alListData().get(getM_iLastSelListHeadRow()) != null) {
					voMyBill = ((GeneralBillVO) getM_alListData()
							.get(getM_iLastSelListHeadRow()));
					sBusitypeid = (String) voMyBill
							.getHeaderValue(m_sBusiTypeItemKey);
				}
			} else { // ��
				if (getBillCardPanel().getHeadItem(m_sBusiTypeItemKey) != null
						&& getBillCardPanel().getHeadItem(m_sBusiTypeItemKey)
								.getComponent() != null) {
					nc.ui.pub.beans.UIRefPane ref = ((nc.ui.pub.beans.UIRefPane) getBillCardPanel()
							.getHeadItem(m_sBusiTypeItemKey).getComponent());
					// ��pk
					sBusitypeid = ref.getRefPK();
				}
			}
			// ҵ�����Ͳ�Ϊ��ʱ
			// ��һ����Ҫ���⡣
			if (sBusitypeid != null && m_alBrwLendBusitype == null) {
				/*ArrayList alParam = new ArrayList();
				alParam.add(getEnvironment().getCorpID());
				m_alBrwLendBusitype = (ArrayList) GeneralBillHelper
						.queryInfo(new Integer(
								QryInfoConst.QRY_BRW_LEND_BIZTYPE), alParam);*/
				m_alBrwLendBusitype = GenMethod.getBrwLendBiztypes(getEnvironment().getCorpID());

				// ������ؿգ���ʼ��֮����־�Ѿ������ˣ���û�н�ת���Ͱ�!
				if (m_alBrwLendBusitype == null)
					m_alBrwLendBusitype = new ArrayList();
			}
			// �ǽ�ת���͵ģ����ء��ǡ�
			if (sBusitypeid != null && m_alBrwLendBusitype != null
					&& m_alBrwLendBusitype.contains(sBusitypeid))
				return true;
		} catch (Exception e) {
			nc.vo.scm.pub.SCMEnv.error(e);
		}
		return false;
	}

	protected class ButtonManager201 extends nc.ui.ic.pub.bill.GeneralButtonManager {
		public ButtonManager201(GeneralBillClientUI clientUI) throws BusinessException {
			super(clientUI);
		}
		
		/**
		 * ����ʵ�ָ÷�������Ӧ��ť�¼���
		 * 
		 * @version (00-6-1 10:32:59)
		 * 
		 * @param bo
		 *            ButtonObject
		 */
		public void onButtonClicked(nc.ui.pub.ButtonObject bo) {
			showHintMessage(bo.getName());
//			onExtendBtnsClick(bo);
			if (bo == getButtonManager().getButton(ICButtonConst.BTN_ASSIST_FUNC_DISPENSE))
				onDispense();
			else if (bo == getButtonManager().getButton(ICButtonConst.BTN_ASSIST_FUNC_MANUAL_RETURN))
				onNewReplenishInvBill();
			else if (bo == getButtonManager().getButton(ICButtonConst.BTN_ASSIST_FUNC_PO_RETURN))
				onNewReplenishInvBillByOrder();

			// v5
			else if (bo == getButtonManager().getButton(ICButtonConst.BTN_LINE_KD_CALCULATE))
				onKDJS();
			else if (bo == getButtonTree().getButton(ICButtonConst.BTN_ASSIST_GEN_ASSET_CARD))
				onGenerateAssetCard();
			else if (bo == getButtonTree().getButton(ICButtonConst.BTN_ASSIST_CGEN_ASSET_CARD))
				onCancelGenerateAssetCard();
//			else if (bo == getBoInfoCost()){
//				onBoInfoCost();
//			}

			else
				super.onButtonClicked(bo);
		}
		
		/**
		 * 
		 * �����������������ɹ̶��ʲ���Ƭ��
		 * <p>
		 * <b>����˵��</b>
		 * <p>
		 * @author duy
		 * @time 2008-5-22 ����10:14:45
		 */
		private void onGenerateAssetCard() {
			try {
				// ���ú�̨�ӿ����ɹ̶��ʲ���Ƭ
				String ts = (String) nc.ui.ic.pub.tools.GenMethod.callICService(
						"nc.bs.ic.ic201.GeneralHBO", "generateAssetCard",
						new Class[] { GeneralBillVO.class, String.class },
						new Object[] { getM_voBill(), getClientEnvironment().getUser().getPrimaryKey() });
				getM_voBill().getHeaderVO().setTs(ts);
				getM_voBill().getHeaderVO().setBassetcard(UFBoolean.TRUE);
				setBillVO(getM_voBill());
				updateBillToList(getM_voBill());
				setButtonStatus(true);
			} catch (Exception e) {
				//��־�쳣
				nc.vo.scm.pub.SCMEnv.out(e);
				showErrorMessage(e.getMessage());
			}
		}
		
		/**
		 * 
		 * ��������������ȡ�����ɹ̶��ʲ���Ƭ��
		 * <p>
		 * <b>����˵��</b>
		 * <p>
		 * @author duy
		 * @time 2008-5-22 ����10:26:35
		 */
		private void onCancelGenerateAssetCard() {
			try {
				// ���ú�̨�ӿ����ɹ̶��ʲ���Ƭ
				String ts = (String) nc.ui.ic.pub.tools.GenMethod.callICService(
						"nc.bs.ic.ic201.GeneralHBO", "cancelGenerateAssetCard",
						new Class[] { GeneralBillVO.class, String.class },
						new Object[] { getM_voBill(), getClientEnvironment().getUser().getPrimaryKey() });
				getM_voBill().getHeaderVO().setTs(ts);
				getM_voBill().getHeaderVO().setBassetcard(UFBoolean.FALSE);
				setBillVO(getM_voBill());
				updateBillToList(getM_voBill());
				setButtonStatus(true);
			} catch (Exception e) {
				//��־�쳣
				nc.vo.scm.pub.SCMEnv.out(e);
				showErrorMessage(e.getMessage());
			}
		}

		/**
		 * v5:����ʱ����ͷ�Ĺ�˾����Ϊ���������˾�Ͳɹ���˾
		 * 
		 */
		protected void onAddLine() {
			super.onAddLine();
			int sel = getBillCardPanel().getBillTable().getSelectedRow();
			if (sel < 0)
				return;

			setReqAndInvField(sel);
		}

		/**
		 * v5:����ʱ����ͷ�Ĺ�˾����Ϊ���������˾�Ͳɹ���˾
		 * 
		 */
		protected void onInsertLine() {
			super.onInsertLine();
			int sel = getBillCardPanel().getBillTable().getSelectedRow();
			if (sel < 0)
				return;
			setReqAndInvField(sel);
		}

		/**
		 * �����ߣ����˾� ���ܣ����棬����ǽ�ת���͵ģ����λ�����кš� ������ ���أ� ���⣺ ���ڣ�(2001-11-24 12:15:42)
		 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
		 * 
		 */
		protected boolean onSave() {
			getBillCardPanel().getBodyTabbedPane().setSelectedIndex(0);
			if (isBrwLendBiztype()) {
				// ����ǰ���λ����
				m_alLocatorDataBackup = getM_alLocatorData();
				setM_alLocatorData(null);
				// ����ǰ�����к�����
				m_alSerialDataBackup = getM_alSerialData();
				setM_alSerialData(null);
			}
			// ������Դͷ���ݺŵ���Ʒ�����Ƿ����0

			// �޸��ˣ������� �޸����ڣ�2007-8-13����04:51:06
			// �޸�ԭ�򣺽����������,�����˻�����ⵥ��¼�����������Ϊ���ģ�Ȼ����������ʱ��¼�����룬���ӵ�����Ҳ��Ϊ���ġ�
			if (super.onSave()) {
				setFixBarcodenegative(false);// ��������Ϊ����
				return true;
			} else
				return false;
		}

		/**
		 * �����ߣ����˾� ���ܣ����кŷ��� ������ ���أ� ���⣺ ���ڣ�(2001-5-9 9:23:32) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
		 * 
		 * 
		 * 
		 * 
		 */
		public void onSNAssign() {
			// �����ģʽ�£�����ǽ�ת���͵ģ�����Ҫ�˲�����
			if (BillMode.Browse != getM_iMode() && isBrwLendBiztype()) {
				showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"4008busi", "UPP4008busi-000273")/*
															 * @res
															 * "����ҵ�����ͣ�����Ҫִ�д˲������������״̬�²鿴��"
															 */);
				return;
			} else {
				if (isBrwLendBiztype()) {

					GeneralBillVO voMyBill = null;
					if (getM_alListData() != null && getM_iLastSelListHeadRow() >= 0
							&& getM_alListData().size() > getM_iLastSelListHeadRow()
							&& getM_alListData().get(getM_iLastSelListHeadRow()) != null) {
						voMyBill = ((GeneralBillVO) getM_alListData()
								.get(getM_iLastSelListHeadRow()));
						String sBillPK = (String) voMyBill.getItemValue(0,
								"cfirstbillhid");
						if (sBillPK == null || sBillPK.trim().length() == 0) {
							showErrorMessage(nc.ui.ml.NCLangRes.getInstance()
									.getStrByID("4008busi", "UPP4008busi-000274")/*
																					 * @res
																					 * "û�ж�Ӧ�Ľ���/�������޷���ѯ����λ�����к����ݡ����鵥����Դ��"
																					 */);
							return;
						} else
							showErrorMessage(nc.ui.ml.NCLangRes.getInstance()
									.getStrByID("4008busi", "UPP4008busi-000275")/*
																					 * @res
																					 * "��쿴��ؽ���/����������ݡ�"
																					 */);
					}
					return;
				}
			}
			super.onSNAssign();
		}

		/**
		 * �����ߣ����˾� ���ܣ���λ���� ������ ���أ� ���⣺ ���ڣ�(2001-5-9 9:23:32) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
		 * 
		 * 
		 * 
		 * 
		 */
		public void onSpaceAssign() {
			// �����ģʽ�£�����ǽ�ת���͵ģ�����Ҫ�˲�����
			if (BillMode.Browse != getM_iMode() && isBrwLendBiztype()) {
				showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"4008busi", "UPP4008busi-000273")/*
															 * @res
															 * "����ҵ�����ͣ�����Ҫִ�д˲������������״̬�²鿴��"
															 */);
				return;
			} else {
				if (isBrwLendBiztype()) {
					GeneralBillVO voMyBill = null;
					if (getM_alListData() != null && getM_iLastSelListHeadRow() >= 0
							&& getM_alListData().size() > getM_iLastSelListHeadRow()
							&& getM_alListData().get(getM_iLastSelListHeadRow()) != null) {
						voMyBill = ((GeneralBillVO) getM_alListData()
								.get(getM_iLastSelListHeadRow()));
						String sBillPK = (String) voMyBill.getItemValue(0,
								"cfirstbillhid");
						if (sBillPK == null || sBillPK.trim().length() == 0) {
							showErrorMessage(nc.ui.ml.NCLangRes.getInstance()
									.getStrByID("4008busi", "UPP4008busi-000274")/*
																					 * @res
																					 * "û�ж�Ӧ�Ľ���/�������޷���ѯ����λ�����к����ݡ����鵥����Դ��"
																					 */);
							return;
						} else
							showErrorMessage(nc.ui.ml.NCLangRes.getInstance()
									.getStrByID("4008busi", "UPP4008busi-000275")/*
																					 * @res
																					 * "��쿴��ؽ���/����������ݡ�"
																					 */);
					}
					return;
				}
			}
			super.onSpaceAssign();
		}

		/**
		 * ���׹��ܰ�ť������ ���ܣ� ������ ���أ� ���⣺ ���ڣ�(2002-04-18 10:43:46) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
		 */
		private void onDispense() {
			IBillType billType = BillTypeFactory.getInstance().getBillType(getSourBillTypeCode());
			if (BillMode.Browse == getM_iMode() && isSigned() != SIGNED
					&& getSourBillTypeCode() != null
					&& !billType.typeOf(nc.vo.ic.pub.billtype.ModuleCode.IC)) {

			} else
				return;

			if (getBillCardPanel().getBillTable().getSelectedRows().length >= 1) {

				if (nc.ui.pub.beans.UIDialog.ID_CANCEL == nc.ui.pub.beans.MessageDialog
						.showOkCancelDlg(getClientUI(), null,
								nc.ui.ml.NCLangRes.getInstance().getStrByID(
										"4008busi", "UPP4008busi-000268")/*
																			 * @res
																			 * "�Ƿ�Ա�����ѡ���������г��׼��Զ��������״�����������������ⵥ��?"
																			 */)) {
					return;

				}

				// showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
				// "4008busi", "UPT40080602-000007")/* @res "����" */);

				GeneralBillVO voBill = (GeneralBillVO) ((GeneralBillVO) getM_alListData()
						.get(getM_iLastSelListHeadRow())).clone();
				GeneralBillVO voBillclone = (GeneralBillVO) voBill.clone();

				ArrayList alOutGeneralVO = new ArrayList();
				ArrayList alInGeneralVO = new ArrayList();

				ArrayList aloutitem = new ArrayList();
				ArrayList alinitem = new ArrayList();
				int[] rownums = getBillCardPanel().getBillTable()
						.getSelectedRows();

				for (int i = 0; i < rownums.length; i++) {

					if (!isSetInv(voBill, rownums[i])
							|| isDispensedBill(voBill, rownums[i]))
						continue;

					// ������
					GeneralBillItemVO voParts = voBill.getItemVOs()[rownums[i]];// searchInvKit(voBill.getItemVOs()[rownums[i]]);
					UFDouble ufSetNum = null;
					// if (voParts != null) {
					// outnum to innum
					ufSetNum = voParts.getNinnum();
					voParts.setAttributeValue("nshouldoutnum", voParts
							.getNinnum());
					voParts.setAttributeValue("nshouldoutassistnum", voParts
							.getNinassistnum());
					voParts.setAttributeValue("noutnum", voParts.getNinnum());
					voParts.setAttributeValue("noutassistnum", voParts
							.getNinassistnum());
					// after set null to noutnum and noutassistnum zhx 030616
					voParts.setAttributeValue("ninnum", null);
					voParts.setAttributeValue("ninassistnum", null);
					voParts.setAttributeValue("nshouldinnum", null);
					voParts.setAttributeValue("nneedinassistnum", null);
					voParts.setDbizdate(new nc.vo.pub.lang.UFDate(getEnvironment().getLogDate()));

					// soucebill
					voParts.setAttributeValue("csourcetype", voBill
							.getHeaderVO().getCbilltypecode());
					voParts.setAttributeValue("csourcebillhid", voBill
							.getHeaderVO().getPrimaryKey());
					voParts.setAttributeValue("csourcebillbid", voBill
							.getItemVOs()[rownums[i]].getPrimaryKey());
					voParts.setAttributeValue("vsourcebillcode", voBill
							.getHeaderVO().getVbillcode());
					// �޸���:������ �޸�����:2007-04-12
					// �޸�ԭ��:���ڳ���������Ĵ������Ӧ��ⵥ�źͶ�Ӧ�������к��Զ�Я�������ɳ��ⵥ��
					voParts.setAttributeValue("ccorrespondcode", voBill
							.getHeaderVO().getVbillcode());
					voParts.setAttributeValue("ccorrespondbid", voBill
							.getItemVOs()[rownums[i]].getCgeneralbid());

					voParts.setCgeneralbid(null);
					voParts.setCgeneralbb3(null);
					voParts.setCsourceheadts(null);
					voParts.setCsourcebodyts(null);

					aloutitem.add(voParts);
					// alOutGeneralVO.add(gbvoOUT);

					// ���������VO��Ӧ�ǲɹ���ⵥ�ݵ���������

					voParts.setLocator(null);// zhy
					GeneralBillItemVO[] tempItemVO = splitInvKit(voParts,
							voBillclone.getHeaderVO(), ufSetNum);
					if (tempItemVO != null && tempItemVO.length > 0) {
						for (int j = 0; j < tempItemVO.length; j++) {
							alinitem.add(tempItemVO[j]);

						}

					} else {
						showErrorMessage(nc.ui.ml.NCLangRes.getInstance()
								.getStrByID("4008busi", "UPP4008busi-000270")/*
																				 * @res
																				 * "�������׼�û�ж���������붨��������ٽ������ף�"
																				 */);
						return;
					}
				}

				if (aloutitem.size() == 0 || alinitem.size() == 0)

					return;

				GeneralBillVO gbvoOUT = new GeneralBillVO();
				voBill.getHeaderVO().setCoperatorid(getEnvironment().getUserID());
				voBill.getHeaderVO().setDbilldate(
						new nc.vo.pub.lang.UFDate(getEnvironment().getLogDate()));
				gbvoOUT.setParentVO(voBill.getParentVO());
				gbvoOUT.getHeaderVO().setPrimaryKey(null);
				gbvoOUT.getHeaderVO().setVbillcode(null);
				gbvoOUT.getHeaderVO().setCbilltypecode(
						nc.vo.ic.pub.BillTypeConst.m_otherOut);
				gbvoOUT.getHeaderVO().setStatus(nc.vo.pub.VOStatus.NEW);
				gbvoOUT.getHeaderVO().setAttributeValue("bdispenseflag", "Y");

				GeneralBillItemVO[] outbodys = new GeneralBillItemVO[aloutitem
						.size()];
				aloutitem.toArray(outbodys);

				gbvoOUT.setChildrenVO(outbodys);

				nc.ui.scm.pub.report.BillRowNo.setVORowNoByRule(gbvoOUT,
						nc.vo.ic.pub.BillTypeConst.m_otherOut, "crowno");

				alOutGeneralVO.add(gbvoOUT);

				GeneralBillVO gbvoIn = new GeneralBillVO();
				gbvoIn.setParentVO(voBillclone.getParentVO());
				gbvoIn.getHeaderVO().setPrimaryKey(null);
				gbvoIn.getHeaderVO().setVbillcode(null);
				gbvoIn.getHeaderVO().setCbilltypecode(
						nc.vo.ic.pub.BillTypeConst.m_otherIn);
				gbvoIn.getHeaderVO().setStatus(nc.vo.pub.VOStatus.NEW);
				gbvoIn.getHeaderVO().setAttributeValue("bdispenseflag", "Y");

				GeneralBillItemVO[] inbodys = new GeneralBillItemVO[alinitem
						.size()];
				alinitem.toArray(inbodys);

				gbvoIn.setChildrenVO(inbodys);

				// // �ɹ�������ɵ������ⵥ��Ҫ���õ��ݺ�
				nc.ui.scm.pub.report.BillRowNo.setVORowNoByRule(gbvoIn,
						nc.vo.ic.pub.BillTypeConst.m_otherIn, "crowno");

				alInGeneralVO.add(gbvoIn);

				getDispenseDlg(
						nc.ui.ml.NCLangRes.getInstance().getStrByID("4008busi",
								"UPP4008busi-000269")/*
														 * @res "�������ɣ�������/�������ⵥ"
														 */, alInGeneralVO, alOutGeneralVO).showModal();
				if (m_dlgInOut.isOK()) {
					try { // ���±�β
						// setAuditBillFlag();
						filterNullLine();

						setDispenseFlag(
								(GeneralBillVO) ((GeneralBillVO) getM_alListData()
										.get(getM_iLastSelListHeadRow())),
								rownums);
						// super.freshStatusTs(voBill.getHeaderVO().getPrimaryKey());

						setM_voBill((GeneralBillVO) ((GeneralBillVO) getM_alListData()
								.get(getM_iLastSelListHeadRow())).clone());
						super.setButtonStatus(false);
						// ���׳ɹ�����Ҫ���õ��ݵİ�ť��ɾ�����޸ģ����ư�ť�����ã���

						// setBillState();
						// can not dispense the inv more over, after create
						// the other in and out bill!
						// m_boDispense.setEnabled(false);
						ctrlSourceBillButtons(true);
						/*
						 * for(int i=0;i<rownums.length;i++)
						 * super.refreshSelBill(rownums[i]);
						 */
						// �޸�bug-NCdp200017720-���ȱ��:NCdp200016800-������-200603191641--����Ts
						String billCardPrimaryKey = getBillCardPanel()
								.getHeadItem("cgeneralhid").getValue()
								.toString().trim();
						ArrayList alFreshRet = (ArrayList) GeneralBillHelper
								.queryInfo(new Integer(
										QryInfoConst.BILL_STATUS_TS),
										billCardPrimaryKey);
						if (alFreshRet == null || alFreshRet.get(0) == null) {
							SCMEnv.out("Err,ret");
						}
						// set
						// ts
						if (alFreshRet != null && alFreshRet.size() >= 2
								&& alFreshRet.get(1) != null) {
							ArrayList alTs = (ArrayList) alFreshRet.get(1);
							GeneralBillUICtl.updateDataAfterDispense(
									getClientUI(), getM_voBill());
							// freshTs(alTs);
						}
						// �޸�bug-NCdp200017720-���ȱ��:NCdp200016800-������-200603191641

					} catch (Exception e) {
						handleException(e);
						showErrorMessage(e.getMessage());
					}
				}
			}
		}

		/**
		 * �ɹ�����ʱ������������۶ּ���ķ��������� ë��-Ƥ�� ���ص����� ���������õ���������
		 * 
		 * @author ljun
		 * @since v5 �߲ɹ�->�ʼ�->�������̲��п۶ּ���
		 */
		private void onKDJS() {

			// ��ǰ����״̬Ϊ�༭
			if (getM_iMode() == BillMode.Browse) {
				showWarningMessage(ResBase.get201KD01());
				return;
			}
			int rows = getBillCardPanel().getBillTable().getRowCount();
			if (rows <= 0) {
				return;
			}
			// ��ǰ��˾�Ͳɹ���˾�����ʱ�������۶�
			String purcorp = getBillCardPanel().getHeadItem(IItemKey.PUR_CORP) == null ? null
					: getBillCardPanel().getHeadItem(IItemKey.PUR_CORP)
							.getValue();
			if (purcorp == null) {
				return;
			} else {
				if (!purcorp.equals(getEnvironment().getCorpID())) {
					showWarningMessage(ResBase.get201KD03());
					return;
				}
			}
			// ���������鵽���ĲŽ��п۶�
			if (getBillCardPanel().getBodyValueAt(0, "csourcebillbid") == null
					|| getBillCardPanel().getBodyValueAt(0, "csourcetype") == null
					|| !getBillCardPanel().getBodyValueAt(0, "csourcetype")
							.toString().equalsIgnoreCase("23")) {
				showWarningMessage(ResBase.get201KD02());
				return;
			}

			clearBillBodyItem(getBillCardPanel(), "nkdnum");

			GeneralBillItemVO[] voaItem = getM_voBill().getItemVOs();
			if (voaItem == null)
				return;

			UFDouble[] ufdArray = null;
			try {
				IPricStl obj = (IPricStl) NCLocator.getInstance().lookup(
						IPricStl.class.getName());
				ufdArray = obj.servForQnty(getM_voBill().getItemVOs(),
						new ClientLink(getClientEnvironment()));
			} catch (BusinessException exx) {
				nc.vo.scm.pub.SCMEnv.error(exx);
				showHintMessage(exx.getMessage());
			}

			// ���ÿ۶ֵ�����
			HashMap map = new HashMap();
			if (ufdArray == null)
				return;
			for (int i = 0; i < ufdArray.length; i++) {
				Integer iI = new Integer(i);

				UFDouble ufdGrossNum = (UFDouble) voaItem[i]
						.getAttributeValue("ningrossnum");
				if (ufdGrossNum == null || ufdArray[i] == null)
					map.put(iI, null);
				else {
					map.put(iI, ufdArray[i]);// �۶�����
				}
			}

			// ���ÿ۶�����������۶������ֶ� //����ʵ������: ʵ������������ë��-Ƥ��-koudun
			if (getBillCardPanel().getBodyItem("nkdnum") != null) {

				for (int i = 0; i < rows; i++) {
					if (getBillCardPanel().getBodyValueAt(i, "cinventoryid") == null)
						continue;
					Integer iX = new Integer(i);

					UFDouble ufd = (UFDouble) map.get(iX);
					getBillCardPanel().setBodyValueAt(ufd, i, "nkdnum");

					//
					// exec num's editformula
					getBillCardPanel().execBodyFormula(i, "ninnum");
					// ���������ȱ仯
					Object vl = getBillCardPanel().getBodyValueAt(i, "ninnum");

					getBillCardPanel().execBodyFormula(i, "nkdnum");

					getEditCtrl().afterNumEdit(
							new BillEditEvent(this, null, vl, "ninnum", i,
									BillItem.BODY));

					getBillCardPanel().getBillModel().setRowState(i,
							BillModel.MODIFICATION);

				}
			}
		}

		/**
		 * �˴����뷽��˵���� �������ڣ�(2003-10-9 14:43:10) �����˿ⵥ �˻���־Ϊ"Y"�� �˻����ɿɱ༭��
		 * ����������˻����ɲ��ɱ༭�� ��Ҫ����onNew(),���˻�������Ϊ���ɱ༭��
		 */
		public void onNewReplenishInvBill() {
			super.onAdd();
			// ���ñ�����˻����ɺͱ�ͷ���˻����ɿ��Ա༭
			// ������������Ǹ���
			nc.ui.ic.pub.bill.GeneralBillUICtl.setSendBackBillState(getClientUI(), true);
			getM_voBill().getHeaderVO().setFreplenishflag(new UFBoolean(true));
			
			setFixBarcodenegative(true);// ��������Ϊ����

			getBillCardPanel().getHeadItem("cproviderid").setEdit(true);
			getBillCardPanel().getHeadItem("cproviderid").setEnabled(true);
		}

		/**
		 * �˴����뷽��˵���� �������ڣ�(2003-10-9 14:43:10) ���ղɹ��˻����������˿ⵥ��by hanwei 2003-10-14
		 * 
		 * 
		 */
		public void onNewReplenishInvBillByOrder() {

			IFromPoUI ui = null;
			try {
				ui = (IFromPoUI) Class.forName("nc.ui.ic.ic201.FromPoUI")
						.newInstance();
			} catch (Exception e) {
				showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"4008busi", "UPP4008busi-000272")/*
															 * @res
															 * "�ù��ܲ����ã�ԭ�򣺲ɹ�����ϵͳû�а�װ����"
															 */);
				return;
			}
			ui.onNewReplenishInvBillByOrder((ClientUI)getClientUI(), getClientUI().getEnvironment().getCorpID());

		}
	}
	
	public IButtonManager getButtonManager() {
		if (m_buttonManager == null) {
			try {
				m_buttonManager = new ButtonManager201(this);
			} catch (BusinessException e) {
				//��־�쳣
				nc.vo.scm.pub.SCMEnv.error(e);
				showErrorMessage(e.getMessage());
			}
		}
		return m_buttonManager;
	}
	
	private void setFixBarcodenegative(boolean value) {
		super.m_bFixBarcodeNegative = value;
	}

	public static void clearBillBodyItem(BillCardPanel bcp, String itemkey) {
		int rows = bcp.getBillTable().getRowCount();
		for (int i = 0; i < rows; i++) {
			bcp.setBodyValueAt(null, i, itemkey);
		}
	}

	/**
	 * ����ʱ,����ͷ�Ƿ��й�˾����Ϣ, �ѹ�˾�����֯�ֿ������������
	 * 
	 * @see afterWHEdit, afterCalbodyEdit: �����֯�Ͳֿ�༭��,Ҫ����ͷ�Ĳֿ�Ϳ����֯�������ڵı�������
	 * 
	 */
	public void addRowNums(int rownums) {
		super.addRowNums(rownums);
		// v5 lj ���вɹ�Ĭ����������
		setBodyDefaultData(0, rownums);// ����
		// ��ͷ�ɹ���˾����Ĭ��Ϊ��½��˾
		// ���ղɹ���������ʱ,�˷����������ö�������֮ǰ����,�������ﲻ���ִ�����պ�����
		String pk_corp = getClientEnvironment().getCorporation().getPk_corp();
		getBillCardPanel().setHeadItem(IItemKey.PUR_CORP, pk_corp);
		BillItem it = getBillCardPanel().getHeadItem(IItemKey.PUR_CORP);
		// ((UIRefPane)getBillCardPanel().getHeadItem(IItemKey.PUR_CORP).getComponent()).

		getBillCardPanel().execHeadTailEditFormulas(it);
	}

	/**
	 * �ӵ��ݱ����У����ҳ����ǳ��׼��Ĵ�������ع�һ���µı�����VO[] ���ܣ� ������ ���أ� ���⣺ ���ڣ�(2002-04-18
	 * 11:29:23) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 */
	GeneralBillItemVO[] searchInvKit(GeneralBillItemVO[] cvos) {
		ArrayList alInvKit = null;
		GeneralBillItemVO[] resultvos = null;
		if (cvos != null) {
			alInvKit = new ArrayList();
			for (int i = 0; i < cvos.length; i++) {
				if (cvos[i].getIsSet() != null
						&& cvos[i].getIsSet().intValue() == 1)
					alInvKit.add(cvos[i]);
			}
			if (alInvKit.size() > 0) {
				resultvos = new GeneralBillItemVO[alInvKit.size()];
				alInvKit.toArray(resultvos);
			}
			return resultvos;
		}
		return null;
	}

	/**
	 * �ӵ��ݱ����У����ҳ����ǳ��׼��Ĵ�������ع�һ���µı�����VO[] ���ܣ� ������ ���أ� ���⣺ ���ڣ�(2002-04-18
	 * 11:29:23) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 */
	GeneralBillItemVO searchInvKit(GeneralBillItemVO cvos) {
		if (cvos != null) {
			if (cvos.getIsSet() != null && cvos.getIsSet().intValue() == 1)
				return cvos;
		}
		return null;
	}

	/**
	 * �����ߣ����˾� ���ܣ����б�ʽ��ѡ��һ�ŵ��� ������ ������alListData�е����� ���أ��� ���⣺ ���ڣ�(2001-11-23
	 * 18:11:18) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 */
	protected void selectBillOnListPanel(int iBillIndex) {
	}

	/**
	 * �˴����뷽��˵���� �Ѷ��ŵ���VO�ϲ���һ�� �Բɹ���ⵥ�ݣ���Ҫ���ر����� �������ڣ�(2004-3-17 15:35:51)
	 * 
	 * @return nc.vo.ic.pub.bill.GeneralBillVO
	 * @param vos
	 *            nc.vo.pub.AggregatedValueObject[]
	 */
	// protected GeneralBillVO setBillRefResultCombinVo(
	// nc.vo.pub.AggregatedValueObject[] vos) throws Exception {
	//
	// nc.ui.ic.pub.pfconv.VoHandle handle = new nc.ui.ic.pub.pfconv.VoHandle();
	// String sCsourcetype = null;
	//
	// // ͨ����ͷ��ÿ����֯������Դ��������Ϊ�ɹ�������ҵ�񣬴ӱ���ȡ�����֯
	// // ����Դ��������Ϊ�ɹ�������ҵ�񣬴ӱ�ͷȡ�����֯
	// boolean bhead = true;
	// if (vos != null && vos.length > 0) {
	// GeneralBillItemVO[] itemVOs = (GeneralBillItemVO[]) vos[0]
	// .getChildrenVO();
	// if (itemVOs != null && itemVOs.length > 0) {
	// sCsourcetype = itemVOs[0].getCsourcetype();
	// // /����Դ��������Ϊ�ɹ�������ҵ�񣬴ӱ���ȡ�����֯
	// if (sCsourcetype != null && "21".equalsIgnoreCase(sCsourcetype)) {
	// bhead = false;
	// }
	// }
	//
	// }
	// // Ĭ�ϼ������֯����ͷ�ķ�ʽ������
	// GeneralBillVO voRet = handle.combinVo(vos, bhead);
	//
	// return voRet;
	// }
	/**
	 * �˴����뷽��˵���� ���ò��չ�����������Ʒ�����Ա༭ �÷������˿�����Ĳ��ն����������������� �������ڣ�(2003-10-14
	 * 14:29:30)
	 * 
	 * @param BusiTypeID
	 *            java.lang.String
	 * @param vos
	 *            nc.vo.pub.AggregatedValueObject[]
	 */
	protected void setBillRefResultVO(String sBusiTypeID,
			nc.vo.pub.AggregatedValueObject[] vos) throws Exception {
		// ���չ�����������Ʒ�����Ա༭
		nc.vo.ic.pub.GenMethod.setFlargessEdit(vos, false);
    nc.vo.ic.pub.GenMethod.processFlargessLine((GeneralBillVO[])vos);
		// �����෽��
		super.setBillRefResultVO(sBusiTypeID, vos);

	}

	/**
	 * �����ߣ����˾� ���ܣ����ݵ�ǰ���ݵĴ���״̬����ǩ��/ȡ��ǩ���Ǹ����� ������ ���أ� ���⣺ ���ڣ�(2001-5-9 9:23:32)
	 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 * 
	 * 
	 * 
	 */
	protected void setBtnStatusSign(boolean bUpdateButtons) {
		// ֻ�����״̬�²��ҽ������е���ʱ����
		if (BillMode.Browse != getM_iMode() || getM_iLastSelListHeadRow() < 0
				|| m_iBillQty <= 0) {
			getButtonManager().getButton(ICButtonConst.BTN_SIGN).setEnabled(false);
      getButtonManager().getButton(ICButtonConst.BTN_EXECUTE_SIGN_CANCEL).setEnabled(false);
			return;
		}
		int iSignFlag = isSigned();
		if (SIGNED == iSignFlag) {
			// ��ǩ�֣��������ð�ť״̬,ǩ�ֲ����ã�ȡ��ǩ�ֿ���
      getButtonManager().getButton(ICButtonConst.BTN_SIGN).setEnabled(false);
      getButtonManager().getButton(ICButtonConst.BTN_EXECUTE_SIGN_CANCEL).setEnabled(true);
			// ����ɾ����
      getButtonManager().getButton(ICButtonConst.BTN_BILL_EDIT).setEnabled(false);
      getButtonManager().getButton(ICButtonConst.BTN_BILL_DELETE).setEnabled(false);
			getButtonManager().getButton(ICButtonConst.BTN_ASSIST_FUNC_DISPENSE).setEnabled(false);

		} else if (NOTSIGNED == iSignFlag) {
			// δǩ�֣��������ð�ť״̬,ǩ�ֿ��ã�ȡ��ǩ�ֲ�����
			// �ж��Ƿ���������������Ϊ�����������ģ�����ֻҪ����һ�о����ˡ�

			getButtonManager().getButton(ICButtonConst.BTN_SIGN).setEnabled(true);
      getButtonManager().getButton(ICButtonConst.BTN_EXECUTE_SIGN_CANCEL).setEnabled(false);
			// ��ɾ����
			if (isCurrentTypeBill()) {

				getButtonManager().getButton(ICButtonConst.BTN_BILL_EDIT).setEnabled(true);
				getButtonManager().getButton(ICButtonConst.BTN_BILL_DELETE).setEnabled(true);
			} else {

				getButtonManager().getButton(ICButtonConst.BTN_BILL_EDIT).setEnabled(false);
				getButtonManager().getButton(ICButtonConst.BTN_BILL_DELETE).setEnabled(false);
			}
			// if (isSetInv(m_voBill, m_iFirstSelectRow) &&
			// !isDispensedBill(null))
      if (BillMode.Card == getM_iCurPanel()) {
        getButtonManager().getButton(ICButtonConst.BTN_ASSIST_FUNC_DISPENSE).setEnabled(true);
      }
			// else
			// getButtonManager().getButton(ICButtonConst.BTN_ASSIST_FUNC_DISPENSE).setEnabled(false);

		} else { // ����ǩ�ֲ���
			getButtonManager().getButton(ICButtonConst.BTN_SIGN).setEnabled(false);
			getButtonManager().getButton(ICButtonConst.BTN_EXECUTE_SIGN_CANCEL).setEnabled(false);
			// ��ɾ����
			if (isCurrentTypeBill()) {
				getButtonManager().getButton(ICButtonConst.BTN_BILL_EDIT).setEnabled(true);
				getButtonManager().getButton(ICButtonConst.BTN_BILL_DELETE).setEnabled(true);
			} else {
				getButtonManager().getButton(ICButtonConst.BTN_BILL_EDIT).setEnabled(false);
				getButtonManager().getButton(ICButtonConst.BTN_BILL_DELETE).setEnabled(false);
			}
		}
		// ʹ������Ч
		if (bUpdateButtons)
			updateButtons();

	}

	/**
	 * ���ǻ��෽��
	 */
	public void setBillVO(GeneralBillVO bvo, boolean bUpdateBotton,
			boolean bExeFormule) {

		// ���вɹ��繫˾���ݴ���
		// v5:���ı�ͷ�ɹ�Ա�Ͳɹ�����,��Ӧ�̵Ĳ���Ϊ�����Բɹ���˾
		String purcorp = (String) ((GeneralBillVO) bvo)
				.getHeaderValue(IItemKey.PUR_CORP);
		if (purcorp!=null && !purcorp.equals(getEnvironment().getCorpID())) {
			BillItem it = getBillCardPanel().getHeadItem("cbizid");
			if (it!=null) {
				//RefFilter.filterPsnByDept(it, purcorp, null);// ���ϲ��Ź�˾����
				((UIRefPane) it.getComponent()).setPk_corp(purcorp);
			}
			
			BillItem it1 = getBillCardPanel().getHeadItem("cdptid");
			if (it1!=null) {
				((UIRefPane) it1.getComponent()).getRefModel().setWherePart(null);
				((UIRefPane) it1.getComponent()).setPk_corp(purcorp);
			}
			
			BillItem it2 = getBillCardPanel().getHeadItem("cproviderid");
			if (it2!=null) {
				((UIRefPane) it2.getComponent()).getRefModel().setWherePart(null);
				((UIRefPane) it2.getComponent()).setPk_corp(purcorp);
			}
		}

		//�繫˾���δ���
		if (purcorp!=null && !purcorp.equals(getEnvironment().getCorpID())) {
			if (bvo!=null) {
				GeneralBillItemVO[] voa = bvo.getItemVOs();
				if (voa!=null) {
					for (int i=0;i<voa.length;i++) {
						if (voa[i]==null) continue;
						Integer isBatch = voa[i].getInv().getIsLotMgt();
						if (isBatch==null||isBatch.intValue()==1) 
							continue;
						if (isBatch.intValue()==0) { //�����ι���clear batchcode
							voa[i].setVbatchcode(null);
						}
					}
				}
			}
		}
		
		super.setBillVO(bvo, bUpdateBotton, bExeFormule);
	}

	/**
	 * �����ߣ����˾� ���ܣ����󷽷������ð�ť״̬����setButtonStatus�е��á� ������ ���أ� ���⣺ ���ڣ�(2001-5-9
	 * 9:23:32) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 */
	protected void setButtonsStatus(int iBillMode) {
//		switch(iBillMode){
//		case BillMode.New: getBoInfoCost().setEnabled(true);
//		case BillMode.Update:getBoInfoCost().setEnabled(true);
//		case BillMode.Browse:getBoInfoCost().setEnabled(false);
//		}
		setExtendBtnsStat(iBillMode);
		// ���ģʽ�£��е��ݲ����Ѿ�ǩ�ֲſ���
		long lTime = System.currentTimeMillis();
		// in card browser status, can use dispense button.
		if (getButtonManager().getButton(ICButtonConst.BTN_ASSIST_FUNC_DISPENSE) != null) {
			if (getM_iCurPanel() == BillMode.Card && iBillMode == BillMode.Browse
					&& m_iBillQty > 0 && isSigned() != SIGNED)
				getButtonManager().getButton(
						ICButtonConst.BTN_ASSIST_FUNC_DISPENSE)
						.setEnabled(true);
			else
				getButtonManager().getButton(
						ICButtonConst.BTN_ASSIST_FUNC_DISPENSE).setEnabled(
						false);
		}
		
		// �����ʲ���Ƭ/ȡ�������ʲ���Ƭ�İ�ť״������
		if (getM_iCurPanel() == BillMode.Card && iBillMode == BillMode.Browse
				&& m_iBillQty > 0
				&& isSigned() == SIGNED
				&& getM_voBill().getWh().getIsCapitalStor().booleanValue()
				&& !getM_voBill().getHeaderVO().getFreplenishflag().booleanValue()
				&& nc.ui.ic.pub.tools.GenMethod.isProductEnabled(getCorpPrimaryKey(), "AIM")) {
			// ��ǩ�֡��ʲ��֡����˻���־���ʲ�ģ���Ѿ�������Щ���������ϣ��Ž�����������ť������
			if (getM_voBill().getHeaderVO().getBassetcard().booleanValue()) {
				getButtonManager().getButton(ICButtonConst.BTN_ASSIST_GEN_ASSET_CARD).setEnabled(false);
				getButtonManager().getButton(ICButtonConst.BTN_ASSIST_CGEN_ASSET_CARD).setEnabled(true);
			}
			else {
				getButtonManager().getButton(ICButtonConst.BTN_ASSIST_GEN_ASSET_CARD).setEnabled(true);
				getButtonManager().getButton(ICButtonConst.BTN_ASSIST_CGEN_ASSET_CARD).setEnabled(false);
			}
		}
		else {
			getButtonManager().getButton(ICButtonConst.BTN_ASSIST_GEN_ASSET_CARD).setEnabled(false);
			getButtonManager().getButton(ICButtonConst.BTN_ASSIST_CGEN_ASSET_CARD).setEnabled(false);
		}
		updateButton(getButtonManager().getButton(ICButtonConst.BTN_ASSIST_GEN_ASSET_CARD));
		updateButton(getButtonManager().getButton(ICButtonConst.BTN_ASSIST_CGEN_ASSET_CARD));


		// //��Ҫ�������ð�ť��ˢ�����ఴť��״̬��
		// ���ﲻ�ܵ��ø��Ǹ��෽��
		initButtonsData();

		// �жϲɹ���Ʒģ���Ƿ�����
		lTime = System.currentTimeMillis();
		if (nc.ui.ic.pub.tools.GenMethod.isProductEnabled(getCorpPrimaryKey(),
				nc.vo.pub.ProductCode.PROD_PO)) {
			// ֻҪ�����˲ɹ�ģ�飬���˿�˵����κ�ʱ���ǿ��õģ����Ҫ��

	
			if (getM_iMode() == BillMode.Browse) {
				// v5
				getButtonManager().getButton(ICButtonConst.BTN_LINE_KD_CALCULATE)
						.setEnabled(false);
				//�ڱ༭״̬�£��˿ⰴť������ ������ 2009-08-10
//				getButtonManager().getButton(
//						ICButtonConst.BTN_ASSIST_FUNC_MANUAL_RETURN)
//						.setEnabled(true);
//				getButtonManager().getButton(
//						ICButtonConst.BTN_ASSIST_FUNC_PO_RETURN).setEnabled(
//						true);
			} else {
				// v5
				getButtonManager().getButton(ICButtonConst.BTN_LINE_KD_CALCULATE)
						.setEnabled(true);
			}
		} else {
			getButtonManager().getButton(
					ICButtonConst.BTN_ASSIST_FUNC_MANUAL_RETURN).setEnabled(
					false);
			getButtonManager().getButton(ICButtonConst.BTN_ASSIST_FUNC_PO_RETURN)
					.setEnabled(false);

			// v5
			getButtonManager().getButton(ICButtonConst.BTN_LINE_KD_CALCULATE)
					.setEnabled(false);
		}

		lTime = System.currentTimeMillis();
		
		updateButtons();
		
		SCMEnv.showTime(lTime, "setButtonsStatus(int)_6:");

	}

	/**
	 * ��������֮����Ҫ�������׵ı�־�û�VO�� ���ܣ� ������ ���أ� ���⣺ ���ڣ�(2002-06-03 14:39:46)
	 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 */
	void setDispenseFlag(GeneralBillVO gvo) {
		if (gvo == null || gvo.getItemCount() == 0)
			return;
		ArrayList alBid = null;
		GeneralBillItemVO[] resultvos = gvo.getItemVOs();
		if (resultvos != null) {
			alBid = new ArrayList();
			for (int i = 0; i < resultvos.length; i++) {
				if (resultvos[i].getIsSet() != null
						&& resultvos[i].getIsSet().intValue() == 1) {
					resultvos[i].setFbillrowflag(new Integer(
							nc.vo.ic.pub.BillRowType.afterConvert));
					alBid.add(resultvos[i].getPrimaryKey());
				}
			}
			if (alBid.size() > 0) {
				try {
					GeneralHHelper.setDispense(alBid);
				} catch (Exception e) {
					nc.vo.scm.pub.SCMEnv.error(e);

				}

			}

		}

	}

	/**
	 * ��������֮����Ҫ�������׵ı�־�û�VO�� ���ܣ� ������ ���أ� ���⣺ ���ڣ�(2002-06-03 14:39:46)
	 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 */
	void setDispenseFlag(GeneralBillVO gvo, int[] rownums) {
		if (gvo == null || gvo.getItemCount() == 0)
			return;
		ArrayList alBid = null;
		GeneralBillItemVO[] resultvos = gvo.getItemVOs();
		if (resultvos != null) {
			alBid = new ArrayList();

			for (int i = 0; i < rownums.length; i++) {
				if (!isSetInv(gvo, rownums[i]))
					continue;
				resultvos[rownums[i]].setFbillrowflag(new Integer(
						nc.vo.ic.pub.BillRowType.afterConvert));
				alBid.add(resultvos[rownums[i]].getPrimaryKey());

			}

		}

	}

	/**
	 * �����ߣ�zhx ���ܣ����׼��������Ĵ������� ���������׼��Ĵ������ID�����׼����������ڼ������������ ���أ� ���⣺
	 * ���ڣ�(2001-5-8 19:08:05) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 */
	public GeneralBillItemVO[] splitInvKit(GeneralBillItemVO itemvo,
			GeneralBillHeaderVO headervo, UFDouble nsetnum) {

		if (itemvo == null)
			return null;
		String sInvSetID = itemvo.getCinventoryid();

		if (sInvSetID != null) {
			ArrayList alInvvo = new ArrayList();
			try {
				alInvvo = GeneralBillHelper.queryPartbySetInfo(sInvSetID);
			} catch (Exception e2) {
				nc.vo.scm.pub.SCMEnv.error(e2);
			}
			if (alInvvo == null) {
				nc.vo.scm.pub.SCMEnv.out("�ó��׼�û��������������ݿ�...");
				return null;
			}
			int rowcount = alInvvo.size();

			GeneralBillItemVO[] voParts = new GeneralBillItemVO[rowcount];
			nc.vo.pub.lang.UFDate db = new nc.vo.pub.lang.UFDate(getEnvironment().getLogDate());
			for (int i = 0; i < rowcount; i++) {
				voParts[i] = new GeneralBillItemVO();
				voParts[i].setInv((InvVO) alInvvo.get(i));
				voParts[i].setDbizdate(db);
				UFDouble nchildnum = ((InvVO) alInvvo.get(i))
						.getAttributeValue("childsnum") == null ? new UFDouble(
						0) : new UFDouble(((InvVO) alInvvo.get(i))
						.getAttributeValue("childsnum").toString());
				UFDouble ntotalnum = null;
				if (nsetnum != null)
					ntotalnum = nchildnum.multiply(nsetnum);
				else
					ntotalnum = nchildnum;
				UFDouble hsl = ((InvVO) alInvvo.get(i)).getHsl() == null ? null
						: new UFDouble(((InvVO) alInvvo.get(i)).getHsl()
								.toString());
				UFDouble ntotalastnum = null;
				if (hsl != null && hsl.doubleValue() != 0) {
					ntotalastnum = ntotalnum.div(hsl);
				}

				voParts[i].setNinnum(ntotalnum);
				voParts[i].setNinassistnum(ntotalastnum);
				voParts[i].setNshouldinnum(ntotalnum);
				voParts[i].setNneedinassistnum(ntotalastnum);
				voParts[i].setDbizdate(new nc.vo.pub.lang.UFDate(getEnvironment().getLogDate()));
				voParts[i].setCsourceheadts(null);
				voParts[i].setCsourcebodyts(null);
				voParts[i].setCsourcetype(headervo.getCbilltypecode());
				voParts[i].setCsourcebillhid(headervo.getPrimaryKey());
				voParts[i].setCsourcebillbid(itemvo.getPrimaryKey());
				voParts[i].setVsourcebillcode(headervo.getVbillcode());
				voParts[i].setAttributeValue("creceieveid", itemvo
						.getCreceieveid());
				voParts[i].setAttributeValue("cprojectid", itemvo
						.getCprojectid());
				String s = "vuserdef";
				String ss = "pk_defdoc";
				for (int j = 0; j < 20; j++) {

					voParts[i]
							.setAttributeValue(s + String.valueOf(j + 1),
									itemvo.getAttributeValue(s
											+ String.valueOf(j + 1)));
					voParts[i].setAttributeValue(ss + String.valueOf(j + 1),
							itemvo
									.getAttributeValue(ss
											+ String.valueOf(j + 1)));
				}
				voParts[i].setCgeneralhid(null);
				voParts[i].setCgeneralbid(null);
				voParts[i].setStatus(nc.vo.pub.VOStatus.NEW);
			}
			return voParts;
		}
		return null;

	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2003-10-28 8:47:55)
	 * 
	 * @param iRow
	 *            int
	 * @param m_voBill
	 *            nc.vo.ic.pub.bill.GeneralBillVO ճ���к�����ճ���е����ԣ���������ʹ��
	 *            ����Ʒ�еĸ����У����Ա༭����Ʒ�� ��Ʒ�еĸ����У������Ա༭����Ʒ��
	 */
	public void voBillPastLineSetAttribe(int iRow, GeneralBillVO voBill) {
		Object oTemp = voBill.getItemVOs()[iRow].getAttributeValue("flargess");
		boolean bFlarg = false; // �Ƿ���Ʒ
		if (oTemp != null) {
			UFBoolean ufbflargess = (UFBoolean) oTemp;
			bFlarg = ufbflargess.booleanValue();
		}
		if (!bFlarg) // ����Ʒ�еĸ����У����Ա༭����Ʒ��
			voBill.getItemVOs()[iRow].setAttributeValue("isflargessedit", "Y");
		else
			voBill.getItemVOs()[iRow].setAttributeValue("isflargessedit", "N");
		return;
	}

	// test
	// public void onBillExcel(int iflag) {
	//	      
	// getBillCardPanel().setBodyValueAt(Boolean.FALSE,0,"bsourcelargess");
	//		
	// m_voBill.setItemValue(0,"bsourcelargess",new UFBoolean(false));
	//
	// getBillCardPanel().setBodyValueAt(Boolean.TRUE,0,"bsourcelargess");
	//
	// m_voBill.setItemValue(1,"bsourcelargess",new UFBoolean(true));
	//		
	// getBillCardPanel().setBodyValueAt("21",0,IItemKey.CSOURCETYPE);
	// getBillCardPanel().setBodyValueAt("21",0,IItemKey.CSOURCEBILLBID);
	// getBillCardPanel().setBodyValueAt("21",0,IItemKey.CSOURCEBILLHID);
	//		
	//
	// getBillCardPanel().setBodyValueAt("21",1,IItemKey.CSOURCETYPE);
	// getBillCardPanel().setBodyValueAt("21",1,IItemKey.CSOURCEBILLBID);
	// getBillCardPanel().setBodyValueAt("21",1,IItemKey.CSOURCEBILLHID);
	//		
	//		
	// m_voBill.setItemValue(0,IItemKey.CSOURCETYPE,"21");
	// m_voBill.setItemValue(0,IItemKey.CSOURCEBILLBID,"21");
	// m_voBill.setItemValue(0,IItemKey.CSOURCEBILLHID,"21");
	//
	// m_voBill.setItemValue(1,IItemKey.CSOURCETYPE,"21");
	// m_voBill.setItemValue(1,IItemKey.CSOURCEBILLBID,"21");
	// m_voBill.setItemValue(1,IItemKey.CSOURCEBILLHID,"21");
	//		
	// }
  
  /**
   * v5:����ʱ����ͷ�Ĺ�˾����Ϊ���������˾�Ͳɹ���˾
   * 
   */
  private void setReqAndInvField(int row) {
    String pk_corp = getBillCardPanel().getHeadItem(IItemKey.PKCORP).getValue();
    String calid = getBillCardPanel().getHeadItem("pk_calbody").getValue();
    String whid = getBillCardPanel().getHeadItem("cwarehouseid").getValue();
    
    if(getBillCardPanel().getBodyValueAt(row, IItemKey.REQ_CORP)==null){
      getBillCardPanel().setBodyValueAt(pk_corp, row, IItemKey.REQ_CORP);
      getBillCardPanel().getBillModel().execEditFormulaByKey(row,
          IItemKey.REQ_CORPNAME);
    }
    
    if(getBillCardPanel().getBodyValueAt(row, IItemKey.REQ_CAL)==null){
      getBillCardPanel().setBodyValueAt(calid, row, IItemKey.REQ_CAL);
      getBillCardPanel().getBillModel().execEditFormulaByKey(row,
          IItemKey.REQ_CALNAME);
    }
    
    if(getBillCardPanel().getBodyValueAt(row, IItemKey.REQ_WH)==null){
      getBillCardPanel().setBodyValueAt(whid, row, IItemKey.REQ_WH);
      getBillCardPanel().getBillModel().execEditFormulaByKey(row,
          IItemKey.REQ_WHNAME);
    }
    
    if(getBillCardPanel().getBodyValueAt(row, IItemKey.INV_CORP)==null){
      getBillCardPanel().setBodyValueAt(pk_corp, row, IItemKey.INV_CORP);
      getBillCardPanel().getBillModel().execEditFormulaByKey(row,
          IItemKey.INV_CORPNAME);
    }
  }

	protected void afterBillItemSelChg(int iRow, int iCol) {

	}

	/**
	 * �ֿ�Ϳ����֯�༭��,���ñ���ļ��вɹ��ֶ�, ��������,û�������
	 */
	@Override
	public void afterCalbodyEdit(BillEditEvent e) {
		super.afterCalbodyEdit(e);
		setDefaultDataByHead();
	}

	/**
	 * �ֿ�Ϳ����֯�༭��,���ñ���ļ��вɹ��ֶ�, ��������,û�������
	 */
	@Override
	public void afterWhEdit(BillEditEvent e) {
		super.afterWhEdit(e);
		setDefaultDataByHead();
	}

	private void setBodyData(int irow, String key, String pk, String keyName) {
		if (getBillCardPanel().getBodyValueAt(irow, key) == null
				|| getBillCardPanel().getBodyValueAt(irow, key).toString()
						.trim().length() == 0) {

			getBillCardPanel().setBodyValueAt(pk, irow, key);
			getBillCardPanel().getBillModel().execEditFormulaByKey(irow,
					keyName);
		}
	}

	public void setDefaultDataByHead() {
		int row = getBillCardPanel().getRowCount();
		if (row <= 0)
			return;

		
		String pk_corp = getBillCardPanel().getHeadItem(IItemKey.PKCORP)
				.getValue();
		String calid = getBillCardPanel().getHeadItem("pk_calbody").getValue();
		String whid = getBillCardPanel().getHeadItem("cwarehouseid").getValue();

		for (int i = 0; i < row; i++) {
			 //�鿴�Ƿ��������
			  
			   Object obj = getBillCardPanel().getBodyValueAt(i,IItemKey.CSOURCEBILLBID);
			   if (obj!=null&&obj.toString().trim().length()>0)
			    continue;
			 
			setBodyData(i, IItemKey.REQ_CORP, pk_corp, IItemKey.REQ_CORPNAME);
			setBodyData(i, IItemKey.REQ_CAL, calid, IItemKey.REQ_CALNAME);
			setBodyData(i, IItemKey.REQ_WH, whid, IItemKey.REQ_WHNAME);

		}

	}

	public boolean beforeBillItemEdit(BillEditEvent e) {
		// TODO �Զ����ɷ������
		return false;
	}

	/**
	 * ��ͷ�ɹ�˾������ʱ����ͷҵ��ԱӦ���ݲɹ���˾����
	 * 
	 * @since v5
	 * @author ljun
	 * 
	 */
	// v5
	public boolean beforeEdit(nc.ui.pub.bill.BillItemEvent e) {

		boolean bret = super.beforeEdit(e);
		if (bret == false)
			return false;

		String sItemKey = e.getItem().getKey();
		// �����ͷ�Ĳɹ���˾��ֵ�����ղɹ���˾���˲ɹ�Ա

/*		if (sItemKey.equals("cbizid")) {

			nc.ui.pub.bill.BillItem bi2 = getBillCardPanel().getHeadItem(
					"cbizid");

			UIRefPane purcorp = (UIRefPane) getBillCardPanel().getHeadItem(
					IItemKey.PUR_CORP).getComponent();

			String pkcorpValue = purcorp.getRefPK();
			if (pkcorpValue != null && pkcorpValue.trim().length() != 0)
				RefFilter.filterPsnByDept(bi2, pkcorpValue, null);
		}*/
		
		// �ֿ�༭ǰ��Ҫ���տ����֯����
		if (sItemKey.equals(IItemKey.WAREHOUSE)) {
			String sCalID = getBillCardPanel().getHeadItem("pk_calbody") == null ? null
					: (String) getBillCardPanel().getHeadItem("pk_calbody")
							.getValueObject();
			if (sCalID != null && sCalID.length()>0) {

				RefFilter.filtWh(getBillCardPanel().getHeadItem(
						IItemKey.WAREHOUSE), getEnvironment().getCorpID(),
						new String[] { " AND pk_calbody='" + sCalID + "'" });
			}
		}

		return true;

	}
  
  /**
   * �����ߣ����˾� ���ܣ�����¼����� ������e���ݱ༭�¼� ���أ� ���⣺ ���ڣ�(2001-5-8 19:08:05)
   * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
   * 
   */
	@Override
  public void afterInvMutiEdit(nc.ui.pub.bill.BillEditEvent e) {
    super.afterInvMutiEdit(e);
    for(int i=0;i<getBillCardPanel().getRowCount();i++){
      setReqAndInvField(i);
    }
  }

	protected void beforeBillItemSelChg(int iRow, int iCol) {
		// TODO �Զ����ɷ������
	  
	}
	
	/**
	 * �����ˣ������� ����ʱ�䣺2009-3-5 ����09:33:22 ����ԭ��л���뿪��������һ�£�����ȷ��V55�޸ķ���Ϊ��
 
1�����ɹ���ⵥ�Ĳɹ����š��ɹ�Ա�����в��š���ԱȨ�޿��ƣ�
2�����ɹ���ⵥ�������δ������ɹ���ⵥ��������ⵥ��Ӧ����Ӧ����ʱ������ɹ���ⵥ�Ĳ��š���Ա���ǵ�ǰ��˾�Ĳ��š���Ա����������ε��ݵĲ��š���Ա��
 
��ʷ���ݲ���������������⣬ͨ��ר������
	 */
	public void filterRef(String sCorpID) {
		super.filterRef(sCorpID);
		nc.ui.pub.bill.BillItem bi = getBillCardPanel().getHeadItem("cbizid");
		RefFilter.filterPsnByDept(bi, null, null);
		bi = getBillCardPanel().getHeadItem("cdptid");
		RefFilter.filterDept(bi, null, null);
	}
	private void onBoInfoCost() {
		InformationCostVO[] vos = (InformationCostVO[] )getBillCardPanel().getBillModel("jj_scm_informationcost").getBodyValueVOs(InformationCostVO.class.getName());
		ArrayList voList = new ArrayList();
		for (int i = 0; i < vos.length; i++) {
			if(vos[i].getPrimaryKey() == null||vos[i].getPrimaryKey().length() == 0){
				voList.add(vos[i]);
			}
		}
		InfoCostPanel c = null;
		if(voList.size()!=0&&voList!=null){
//		voList.toArray(vos);
			InformationCostVO[] vos1 = new InformationCostVO[voList.size()];
			voList.toArray(vos1);
//		vos = voList.toArray();
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
				UFDouble mny = null;
				UFDouble inmny = null;
				UFDouble ninum = ((GeneralButtonManager)getButtonManager()).getArrnum(); ;
		
				   int temp = getBillCardPanel().getBillModel("table").getRowCount();
				  number= new UFDouble(0.0);
				   
			    for (int i = 0; i < temp; i++) {
			    	number = number.add(new UFDouble((getBillCardPanel().getBillModel("table").getValueAt(i,"ninnum")==null?0.0:getBillCardPanel().getBillModel("table").getValueAt(i,"ninnum")).toString()));    			    	  
				}
				
				for (int i = 0; i < infoCostVOs.length; i++) {
					infoCostVOs[i].setNnumber(number);
					UFBoolean ismny = (UFBoolean)infoCostVOs[i].getAttributeValue("ismny");
					if(ismny == null || !ismny.booleanValue()){
			    	mny = new UFDouble(infoCostVOs[i].getAttributeValue("noriginalcurprice").toString()).multiply(number);
			    	inmny = new UFDouble(infoCostVOs[i].getAttributeValue("noriginalcurprice").toString()).multiply(number.add(ninum==null?new UFDouble(0.0):ninum));
//			    	taxmny = new UFDouble(infoCostVOs[i].getAttributeValue("noriginalcurtaxprice").toString()).multiply(arrnumber);
//			    	infoCostVOs[i].setAttributeValue("noriginalcursummny", taxmny);
			    	infoCostVOs[i].setAttributeValue("noriginalcurmny", mny);
//			    	infoCostVOs[i].setAttributeValue("ninvoriginalcursummny", taxmny);
			    	infoCostVOs[i].setAttributeValue("ninstoreoriginalcurmny", inmny);	}
					else{
						infoCostVOs[i].setAttributeValue("noriginalcurprice", infoCostVOs[i].getNoriginalcurmny().div(number));	
						infoCostVOs[i].setAttributeValue("ninstoreoriginalcurmny", infoCostVOs[i].getNoriginalcurmny());
					}
				}
										
					vos = ((GeneralButtonManager)getButtonManager()).getInfovos();
					
					if(vos.length != 0 && vos != null){
						
						InformationCostVO[] newVOs = new InformationCostVO[vos.length+infoCostVOs.length];	
						
						System.arraycopy(vos, 0, newVOs, 0, vos.length);						
						System.arraycopy(infoCostVOs, 0, newVOs, vos.length, infoCostVOs.length);	
						
							getBillCardPanel().getBillData().setBodyValueVO(
									"jj_scm_informationcost", newVOs);
							//getBillCardPanel().getBillModel("jj_scm_informationcost").execLoadFormula();
					}
					else getBillCardPanel().getBillData().setBodyValueVO(
							"jj_scm_informationcost", infoCostVOs);
					getBillCardPanel().getBillModel("jj_scm_informationcost").execLoadFormula();
				}	
			
		}
	}
	public ButtonObject[] getExtendBtns() {
		if (extendBtns == null || extendBtns.length == 0) {
			// �������¼�밴ť add by QuSida 2010-9-5 ����ɽ���ܣ�
			extendBtns = new ButtonObject[] { getBoInfoCost() };
			return extendBtns;
		}
		return extendBtns;
	}

	public void onExtendBtnsClick(ButtonObject bo) {
		 if(bo == getBoInfoCost()){
		    	this.onBoInfoCost();
		    }
	}


	public void setExtendBtnsStat(int iState) {
		switch (iState) {
		case BillMode.New:
			getBoInfoCost().setEnabled(true);
			break;
		case BillMode.Update:
			getBoInfoCost().setEnabled(true);
			break;
		case BillMode.Browse:
			getBoInfoCost().setEnabled(false);
			break;
		default:
			getBoInfoCost().setEnabled(false);
			break;
		}
	}



}