package nc.ui.arap.ageanalyse;

/**
 * Ӧ�ա�Ӧ����������������������
 * �������ڣ�(2001-5-17 11:59:57)
 * @author����÷
 * �޸��ˣ�����
 */
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import nc.bs.dao.DAOException;
import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Log;
import nc.impl.arap.proxy.ProxyBill;
import nc.itf.arap.report.IExtInfo;
import nc.vo.arap.ageanalyse.task.AgeAnalyseAsynResultVO;
import nc.ui.arap.billprint.ZlPubPrint;
import nc.ui.arap.billquery.QueryPubMethod;
import nc.ui.arap.file.FileManageUtils;
import nc.ui.arap.newbalancequery.DumYearCheckUtil;
import nc.ui.arap.newbalancequery.MultiDataSourceUtil;
import nc.ui.arap.pub.AccountTableQueryBasePanel;
import nc.ui.arap.pub.ConditionVO;
import nc.ui.arap.pub.IuiConstData;
import nc.ui.arap.pub.LinkQuery;
import nc.ui.arap.pub.MyClientEnvironment;
import nc.ui.arap.pub.NormalPanel;
import nc.ui.arap.pub.NormalPanelCreater;
import nc.ui.arap.pub.PubMethodUI;
import nc.ui.arap.pub.QryObjTools;
import nc.ui.arap.pub.ReportTools;
import nc.ui.ml.NCLangRes;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIComboBox;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UITable;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillEditListener;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.IBillItem;
import nc.ui.pub.linkoperate.ILinkMaintain;
import nc.ui.pub.linkoperate.ILinkMaintainData;
import nc.ui.pub.msg.SwingWorker;
import nc.ui.pub.para.SysInitBO_Client;
import nc.ui.pub.query.QueryConditionClient;
import nc.ui.pub.report.ReportBaseClass;
import nc.ui.pub.report.ReportItem;
import nc.ui.querytemplate.IQueryTemplateTotalVOProcessor;
import nc.ui.querytemplate.QueryConditionDLG;
import nc.ui.uap.sf.SFClientUtil;
import nc.vo.arap.exception.ExceptionHandler;
import nc.vo.arap.pub.ClientVO;
import nc.vo.arap.pub.HeadVO;
import nc.vo.arap.pub.NoDataException;
import nc.vo.arap.pub.NoTempletException;
import nc.vo.arap.pub.PowerCtrlVO;
import nc.vo.arap.pub.PubConstData;
import nc.vo.arap.pub.PubMethodVO;
import nc.vo.arap.pub.QryCondArrayVO;
import nc.vo.arap.pub.QryCondVO;
import nc.vo.arap.pub.QryObjVO;
import nc.vo.arap.pub.QryObjectVO;
import nc.vo.arap.pub.QueryStructVO;
import nc.vo.bank_cvp.type.IType;
import nc.vo.glcom.query.CkResultVO;
import nc.vo.ml.NCLangRes4VoTransl;
import nc.vo.pub.BusinessException;
import nc.vo.pub.bill.BillRendererVO;
import nc.vo.pub.cquery.FldgroupVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.query.QueryConditionVO;
import nc.vo.pub.query.QueryTempletTotalVO;
import nc.vo.querytemplate.TemplateInfo;
import nc.vo.querytemplate.querytype.IQueryType;
import nc.vo.scm.pub.smart.SmartVO;
public abstract class AgeAnalyseYSPane
extends AccountTableQueryBasePanel
implements
ActionListener,
PubConstData,
BillEditListener, ILinkMaintain {

	/**��ѯģ��*/
	private QueryConditionDLG m_qryDlg = null;
	/**����ģ��*/
	private ReportBaseClass m_ReportTemplet = null;
	/**�����������öԻ���*/
	@SuppressWarnings("restriction")
	private nc.ui.groupware.particularquery.DateDlg m_dateDlg = null;
	/**��ѯģ���еĳ�������*/
	private NormalPanel m_pNormalPane = null;
	/**ǰ̨���ݹ�����*/
	private ManageDataYS m_manageData = null;
	/**���������*/
	private UIController m_UiController = null;
	/**����ģ�幤��*/
	private ReportTools m_ReportTools = null;
	/**�����ѯ������Ϊ�����ϸ��ť���л���ҳ��ʽʱ���²�ѯ��׼��*/
	private Vector<Object> m_vBase = null;
	private Vector<Object> m_vObj = null;
	private Vector<Object> m_vOther = null;
	private UFDate m_dateJZRQ = null;

	/**��ǰѡ�����ҳ��ʽ*/
	private String m_sCurrentGS = null;
	/**��ǰ��ʾ��ʽ*/
	private String m_sCurrXsGS = null;
	/**��ǰΪ������չ������ʽ*/
	private boolean m_bCurrentDetail = false;
	/**�Ƿ���ʾ����*/
	private boolean m_bRateFlag = false;
	/**�Ƿ����ڷ���*/
	private boolean m_bRQQJ = false;
	/**���ܽڵ��*/
	protected int m_iSysCode = 3;
	/**��������*/
	protected int m_iWldx = 0;
	/**�洢����ģ���ж��������*/
	private ConditionVO m_voConds = null;
	/**��ͷvo*/
	private ClientVO m_voHead = null;
	private Object[] m_oBodyData = null;
	protected 	IuiConstData constdata= new IuiConstData();

	//����ģ���ʼ��Item
	private ReportItem[] oldItems=null;

	private String[] modelBegin;

	/**
	 * AllPane ������ע�⡣
	 */
	public AgeAnalyseYSPane() {
		super();
	}
	/** ���ܣ���������¼�
	 *  ���ߣ�����
	 *  ����ʱ�䣺2001-06-25
	 *  �����������¼�
	 *  ����ֵ��
	 */
	public void actionPerformed(java.awt.event.ActionEvent e) {
		try {
			Object oTarget = e.getSource();
			if (oTarget.equals(getReportBase().getHeadItem("zygs").getComponent())) {
				onBillTypeChanged();
			}
			else if (oTarget.equals(getReportBase().getHeadItem("xsgs").getComponent())) {
				onShowTypeChanged();
			}
		} catch (NoTempletException ex) {
			Log.getInstance(this.getClass()).error(ex.getMessage(),ex);
			showHintMessage(ex.getMessage());
		}
	}
	public void afterEdit(BillEditEvent e){

	}
	public void bodyRowChange(BillEditEvent e){

		//add by twei �ж���ѡ���Ƿ���Խ��е�������
		String[] oId = null;
		boolean status=true;

		try {
			oId=AccountTableQueryBasePanel.getVouchidFromReport(getReportBase(),"zb_vouchid");
		} catch (NoTempletException e1) {
			Log.getInstance(this.getClass()).error(e1.getMessage());
		}
		if(oId==null||oId.length==0)
		{
			status=false;
		}

		setLinkQueryButtonEnable(new boolean[]{status});
	}
	/**
	 * ���ܣ��ı���ҳ��ʽ��ȡֵ��Χ
	 * ���ߣ�����
	 * ����ʱ�䣺(2001-9-25 11:41:36)
	 * ������<|>
	 * ����ֵ��
	 * �㷨��
	 * �쳣������
	 */
	private void changeZygsValues() {
		try {
			UIComboBox cbZygs =
				((UIComboBox) getReportBase().getHeadItem("zygs").getComponent());
			cbZygs.removeActionListener(this);
			getUIController().changeCombValues(cbZygs);
			cbZygs.addActionListener(this);
		} catch (NoTempletException e) {
			Log.getInstance(this.getClass()).error(e.getMessage(),e);
			showHintMessage(e.getMessage());
		}
	}
	/**
	 * a����:
	 * ���ߣ�����
	 * ����ʱ�䣺(2002-4-4 14:22:48)
	 * ������<|>
	 * ����ֵ��
	 * �㷨��
	 */
	public void doInputCond() {
		getQryDlg().showModal();
		if (getQryDlg().getResult() != UIDialog.ID_OK)
			return;
		//û��ѡ��ȷ����

		m_bRQQJ = getUINormalPane().getValueCbBoxIdx("model") == 1;
		m_dateJZRQ = new UFDate(getUINormalPane().getValueRef("jzrq", constdata.TYPE_TEXT));
		if (m_bRQQJ) {
			//getDateDlg().setJZRQ(m_dateJZRQ);
			getDateDlg().showModal();
			if (getDateDlg().getResult() != UIDialog.ID_OK)
				return;
			//û��ѡ��ȷ����
		}
	}
	/**
	 * a����:
	 * ���ߣ�����
	 * ����ʱ�䣺(2002-4-4 13:55:48)
	 * ������<|>
	 * ����ֵ��
	 * �㷨��
	 */
	public boolean doQuery() {
		boolean status=false;
		try{

			showProgressBar(true);

			getUINormalPane().stopEdit();
			m_voHead = getUINormalPane().getHeadValues();
			////m_voHead = setCorpItem(m_voHead);
			/**�޸���ҳ��ʽȡֵ��Χ*/
			changeZygsValues();
			/**ʹ����ϸ��ť*/
			m_bCurrentDetail = false;
			m_bRateFlag = false;
			/**���ò�ѯ����*/
			setQueryCond();
			getDataManager().removeAll();
			getDataManager().setFbFlag(MyClientEnvironment.isFracUsed());
			QueryStructVO queryStructVO = new QueryStructVO();
			queryStructVO.setCorp(getUINormalPane().getCorpPKs());
			queryStructVO.setVetQryObj(getUINormalPane().getValueQryObj());
			queryStructVO.setVoNormalCond(new QryCondArrayVO[]{getUINormalPane().getValueCond()});
			CkResultVO ckResultVO = MultiDataSourceUtil.check(queryStructVO, getNote());
			if (ckResultVO != null && ckResultVO.isChangeFlag()) {
				int selected = MessageDialog.showYesNoCancelDlg(this, nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("2006","UPP2006-v56-000046")/*@res "�ж��Ƿ��л�����Դ"*/, ckResultVO.getShowMessage());
				if (selected == UIDialog.ID_YES) {
					m_vBase.add(ckResultVO.getNewDataSource());
				} else if (selected != UIDialog.ID_NO) {
					return false;
				}
			}
			status=runQuery();
			/**ʹ�ܱ�ͷ��ҳ��ʽ*/
			getReportTools().setHeadItemsEnable(true);
		}catch(Exception e){
			setButtonEnable(false);//û�в�������ݣ�������ť����Ϊ������
			this.showErrorMessage(e.getMessage());
			return false;
		}
		finally{
			showProgressBar(false);
		}
		return status;
	}
	/**
	 * ���ܣ��õ�����
	 * ���ߣ�����
	 * ����ʱ�䣺(2001-11-14 14:17:13)
	 * ������<|>
	 * ����ֵ��
	 * �㷨��
	 * �쳣������
	 *
	 */
	protected void getBill() throws Exception{
		String[] sOids;
		try {
			sOids =AccountTableQueryBasePanel.getVouchidFromReport(getReportBase(),"zb_vouchid");
			if(sOids==null || sOids.length<=0)
				throw new Exception(NCLangRes4VoTransl.getNCLangRes().getStrByID("20060504", "UPP20060504-000088")/*
				 * @res
				 * "��ѡ����ϸ������Ϣ���飡"
				 */);
		} catch (Exception exp) {
			Log.getInstance(this.getClass()).error(exp.getMessage(), exp);
			throw new NoDataException(NCLangRes4VoTransl.getNCLangRes().getStrByID("20060504", "UPP20060504-000088")/*
			 * @res
			 * "��ѡ����ϸ������Ϣ���飡"
			 */);
		}
		String node = "2006030102";
		if(this.m_iSysCode == 4){
			node = "2008030102";
		}else if(m_iSysCode == 5){
			node = "2010030102";
		}
		SFClientUtil.openLinkedQueryDialog( node,this, new LinkQuery(sOids));
	}
	/**
	 * ���ܣ��õ���ѯ����״̬
	 * ����:����
	 * ����ʱ��:(2002-12-3 13:34:29)
	 * ����:<|>
	 * ����ֵ:
	 * �㷨:
	 *
	 * @return boolean[]
	 */
	public boolean[] getbillStat() {
		return getUINormalPane().getValueChkBox();
	}
	/**
	 * a���ܣ�
	 * ����:����
	 * ����ʱ��:(2002-12-5 16:14:19)
	 * ����:<|>
	 * ����ֵ:
	 * �㷨:
	 *
	 * @return java.lang.Object[]
	 */
	public Object[] getBodyData() {
		return m_oBodyData;
	}
	/**
	 * ���ܣ��õ����ݹ�����
	 * ���ߣ�����
	 * ����ʱ�䣺(2001-8-23 13:48:38)
	 * ������<|>
	 * ����ֵ��
	 * �㷨��
	 * �쳣������
	 */
	public ManageDataYS getDataManager() {
		if(m_manageData==null){
			m_manageData = new ManageDataYS();
		}
		return m_manageData;
	}
	/**
	 * �˴����뷽��˵����
	 * �������ڣ�(2001-6-21 12:32:57)
	 * @return nc.ui.arap.pub.QueryDialog
	 */
	@SuppressWarnings("restriction")
	private nc.ui.groupware.particularquery.DateDlg getDateDlg() {
		if(m_dateDlg == null){
			m_dateDlg = new nc.ui.groupware.particularquery.DateDlg(this);
		}
		return m_dateDlg;
	}
	/**
	 * ���ܣ��õ���ͷ��ʾvo
	 * ����:����
	 * ����ʱ��:(2002-6-28 15:57:46)
	 * ����:<|>
	 * ����ֵ:
	 * �㷨:
	 * @return nc.vo.arap.pub.HeadVO
	 */
	public HeadVO getHeadShowVO() {
		HeadVO vo = new HeadVO();
		vo.setData(getHeadVO());
		vo.setObject((Vector)m_vObj.elementAt(0));
		return vo;
	}
	/**
	 * ���ܣ��õ���ͷvo
	 * ����:����
	 * ����ʱ��:(2002-6-27 16:39:48)
	 * ����:<|>
	 * ����ֵ:
	 * �㷨:
	 * @return nc.vo.arap.pub.ClientVO
	 */
	public ClientVO getHeadVO() {
		if (m_voHead == null) {
			m_voHead = getUINormalPane().getHeadValues();
		}
		return m_voHead;
	}
	/**
	 * ���ܣ��õ�ϵͳ���
	 * ����:����
	 * ����ʱ��:(2002-12-3 13:08:55)
	 * ����:<|>
	 * ����ֵ:
	 * �㷨:
	 *
	 * @return int
	 */
	public int getISysCode() {
		return m_iSysCode;
	}
	/**
	 * �������ش˷����������ֲ�ͬ�ĵ��ýڵ�
	 * �������ڣ�(2001-6-21 12:38:40)
	 * @return java.lang.String
	 */
	public abstract String getNote();
	/**
	 * ���ܣ��õ���ǰ�е�oid
	 * ���ߣ�����
	 * ����ʱ�䣺(2001-11-14 14:18:18)
	 * ������<|>
	 * ����ֵ��
	 * �㷨��
	 * �쳣������
	 *
	 * @return java.lang.String[]
	 */
//	private String[] getOids() throws NoTempletException {

////	Object sOid =getReportBase().getBodyDataVO()[m_iCurrLine].getAttributeValue("zb_vouchid");

//	int selectRow = getReportBase().getBillTable().getSelectedRow();
//	BillModel billModel=(BillModel) getReportBase().getBillTable().getModel();
//	Vector vectorValueObj=(Vector)billModel.getDataVector().get(selectRow);
//	int i=billModel.getBodyColByKey("zb_vouchid");
//	Object sOid =null;

//	if(i!=-1)
//	{
//	sOid =vectorValueObj.get(i);
//	}

//	if (sOid != null && ((String) sOid).trim().length() > 0) {
//	return new String[] {(String) sOid };
//	}
//	return new String[0];
//	}
	/**
	 * ���ܣ��õ�������ѯ����
	 * ���ߣ�����
	 * ����ʱ�䣺(2001-9-11 14:13:21)
	 * ������<|>
	 * ����ֵ��
	 * �㷨��
	 * �쳣������
	 * @return nc.vo.arap.pub.QryCondArrayVO[]
	 */
	private QryCondArrayVO[] getOtherConds() {
		/**���ڼ�¼��ѯ����vo����*/
		Vector<QryCondArrayVO> vetOtherConds = new Vector<QryCondArrayVO>();
		/**�õ���ѯ��������*/
		QryCondArrayVO voObjCond = getUINormalPane().getValueCond();
		if(voObjCond!= null){
			vetOtherConds.addElement(voObjCond);
		}
		/**�õ����ֿͻ���Ӧ�̵�����*/
		voObjCond = getUINormalPane().getValueCustCond();
		if(voObjCond!=null){
			vetOtherConds.addElement(voObjCond);
		}
		//����Ӧ�շ�Χ
		voObjCond = getUINormalPane().getValueDataRangCond();
		if(voObjCond!=null){
			vetOtherConds.addElement(voObjCond);
		}
		/**�õ�������������*/
		String[] sBzs = getUINormalPane().getValuesRef("bz",constdata.TYPE_PK);
		/**���ý��С��λ��*/
		getReportTools().setMaxJeDec(sBzs);
		getDataManager().setIybdig(ReportTools.getMaxJeDec(sBzs));
		/**�ı���ҳ��ʽ������Ӧ������һ��*/
		if(sBzs == null || sBzs.length<=0 ||PubMethodUI.isBB(sBzs)){
			m_sCurrentGS = constdata.ZYGS_JE;
		}else{
			m_sCurrentGS = constdata.ZYGS_WBJE;
		}
		setBillType(m_sCurrentGS);

		/**��ӱ�������*/
		if(sBzs!=null && sBzs.length>0){
			Vector<QryCondVO> vetBz = new Vector<QryCondVO>();
			for(int i=0;i<sBzs.length;i++){
				QryCondVO tmpVO = new QryCondVO();
				tmpVO.setFldorigin("fb");
				tmpVO.setQryfld("bzbm");
				tmpVO.setBoolopr("=");
				tmpVO.setValue(sBzs[i]);
				tmpVO.setFldtype(new Integer(STRING));
				vetBz.addElement(tmpVO);
			}
			QryCondVO[] voBz = new QryCondVO[vetBz.size()];
			vetBz.copyInto(voBz);
			QryCondArrayVO voBzConds = new QryCondArrayVO();
			voBzConds.setItems(voBz);
			voBzConds.setLogicAnd(false);
			vetOtherConds.addElement(voBzConds);
		}

		if(vetOtherConds.size()<=0){
			return null;
		}
		QryCondArrayVO[] voResults = new QryCondArrayVO[vetOtherConds.size()];
		vetOtherConds.copyInto(voResults);
		return voResults;
	}
	/**
	 * ���ܣ��õ�Ȩ�޿���vo
	 * ����:����
	 * ����ʱ��:(2002-8-7 13:20:45)
	 * ����:<|>
	 * ����ֵ:
	 * �㷨:
	 * @return nc.vo.arap.pub.PowerCtrlVO
	 */
	private PowerCtrlVO getPwCtrlvo() {
		return null;
	}
	public QueryConditionDLG getQryDlg() {
		if (m_qryDlg == null) {
			TemplateInfo tempinfo = new TemplateInfo();
			tempinfo.setQueryType(IQueryType.ASYN_ONLY);
			tempinfo.setPk_Org(ClientEnvironment.getInstance().getCorporation().getPrimaryKey());
			tempinfo.setCurrentCorpPk(ClientEnvironment.getInstance().getCorporation().getPrimaryKey());
			tempinfo.setFunNode(getNote());
			tempinfo.setUserid(ClientEnvironment.getInstance().getUser().getPrimaryKey());
			m_qryDlg = new QueryConditionDLG(this,null ,tempinfo,
					nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UC000-0002782")/*@res "��ѯ����"*/);
			m_qryDlg.setNormalPanel(getUINormalPane());
			m_qryDlg.setVisibleNormalPanel(true);
			m_qryDlg.setVisibleUserDefPanel(true);
			m_qryDlg.registerQueryTemplateTotalVOProceeor(new IQueryTemplateTotalVOProcessor() {
				public void processQueryTempletTotalVO(QueryTempletTotalVO totalVO) {
					QueryConditionVO[] conds = totalVO.getConditionVOs();
					// ������δ��Ĭ��ֵʱ�����ñ��ֵ�Ĭ��ֵ
					QueryPubMethod.updateDefItems(ProxyBill.getInstance(), conds,
							ClientEnvironment.getInstance().getCorporation().getPk_corp());
				}
			});
		}
		return m_qryDlg;
	}
	/**
	 * ���� ReportBaseClass ����ֵ��
	 * @return nc.ui.pub.report.ReportBaseClass
	 */
	/* ���棺�˷������������ɡ� */
	private ReportBaseClass getReportBase() throws NoTempletException {
		if (m_ReportTemplet == null) {
			try {
				m_ReportTemplet = new ReportBaseClass();
				m_ReportTemplet.setName("ReportBase");
				// user code begin {1}
				//����ģ�����
				getReportBase().setTempletID(
						PubMethodUI.getCorp_Pk(),
						getNote(),
						PubMethodUI.getUser_Pk(),
						null);
				getReportBase().getBodyUIPanel().setVisible(true);
				BillRendererVO voCell = new BillRendererVO();
				voCell.setShowThMark(true);
				voCell.setShowZeroLikeNull(true);
				m_ReportTemplet.setBodyShowFlags(voCell);
				getReportBase().getBodyPanel().getTable().removeSortListener();getReportBase().getBodyPanel().setBBodyMenuShow(false);
				oldItems =  getReportBase().getBody_Items();
				//m_ReportTemplet.setBodyShowThMark(true);
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
				throw new NoTempletException(NCLangRes.getInstance().getStrByID("20060504","UPP20060504-000262"));
			}
		}
		return m_ReportTemplet;
	}
	/**
	 * ���ܣ��õ�����ģ�幤��
	 * ���ߣ�����
	 * ����ʱ�䣺(2001-9-24 13:56:44)
	 * ������<|>
	 * ����ֵ��
	 * �㷨��
	 * �쳣������
	 * @return nc.ui.arap.pub.ReportTools
	 */
	private ReportTools getReportTools() {
		try {
			if (m_ReportTools == null) {
				m_ReportTools = new ReportTools();
				m_ReportTools.setReportTemplet(getReportBase());
			}
			return m_ReportTools;
		} catch (NoTempletException e) {
			Log.getInstance(this.getClass()).error(e.getMessage(),e);
			showHintMessage(e.getMessage());
			return null;
		}
	}
	/**
	 * a����:
	 * ���ߣ�����
	 * ����ʱ�䣺(2002-4-4 13:21:20)
	 * ������<|>
	 * ����ֵ��
	 * �㷨��
	 * @return java.lang.Object[][]
	 */
	public UITable getResultData() {
		try {
			UITable tab = getReportBase().getBillTable();
			//int iCol = tab.getModel().getColumnCount();
			//int iRow = tab.getModel().getRowCount();
			//Object[][] oResult = new Object[iRow][iCol];
			//for (int i = 0; i < iRow; i++) {
			//for (int j = 0; j < iCol; j++) {
			//oResult[i][j] = tab.getModel().getValueAt(i, j);
			//if(oResult[i][j] instanceof nc.vo.pub.lang.UFDouble){
			//oResult[i][j] = ((nc.vo.pub.lang.UFDouble) oResult[i][j]).toDouble();
			//}
			//}
			//}
			return tab;
		} catch (Exception e) {
			Log.getInstance(this.getClass()).error(e.getMessage(),e);
			showHintMessage(e.getMessage());
			return null;
		}
	}
	/**
	 * ȡ����ʾ��ʽ��
	 * �������ڣ�(2003-9-24 14:21:45)
	 * @return java.lang.String
	 */
	private String getShowType() {

		try {
			UIComboBox cbb =
				(UIComboBox) getReportBase().getHeadItem("xsgs").getComponent();

			m_sCurrXsGS = cbb.getSelectedItem().toString();

		} catch (NoTempletException e) {
			Log.getInstance(this.getClass()).error(e.getMessage(),e);
			showHintMessage(e.getMessage());
		}

		return m_sCurrXsGS;
	}
	public String getTitle() {
		try {
			return getReportBase().getReportTitle();
		} catch (NoTempletException e) {
			Log.getInstance(this.getClass()).error(e.getMessage(),e);
			showHintMessage(e.getMessage());
			return null;
		}
	}
	/**
	 * ���ܣ��õ����������
	 * ���ߣ�����
	 * ����ʱ�䣺(2001-9-24 20:25:24)
	 * ������<|>
	 * ����ֵ��
	 * �㷨��
	 * �쳣������
	 * @return nc.ui.arap.balancequery.UIController
	 */
	private UIController getUIController() {
		if(m_UiController == null){
			m_UiController = new UIController();

			getUIController().setPane(getUINormalPane());
			getUIController().setMainPane(this);
		}
		return m_UiController;
	}
	/**
	 *  ���ܣ��õ���ѯģ�峣�ò�ѯ����pane
	 *  ���ߣ�����
	 *  ����ʱ�䣺(2001-8-16 15:51:39)
	 *  ������<|>
	 *  ����ֵ��
	 *  �㷨��
	 *  �쳣������
	 * @return nc.ui.arap.pub.NormalPanel
	 */
	private NormalPanel getUINormalPane() {
		if (m_pNormalPane == null) {
			QueryConditionDLG dlg = this.getQryDlg();
			m_pNormalPane =
				new NormalPanel(getNote(), modelBegin, dlg);
			getUINormalPane().addControlListener(getUIController());
			m_pNormalPane =NormalPanelCreater.getNormalPanelForZL(m_pNormalPane,m_iSysCode ,this.getNote() );
			/** Ϊ��������ҳǩ��Ӽ��� */



		}
		return m_pNormalPane;
	}
	/**
	 * ���ܣ��õ����������ѯ����
	 *  ���ߣ�����
	 *  ����ʱ�䣺(2001-6-22 14:18:41)
	 *  ������<|>
	 *  ����ֵ��
	 *  �㷨��
	 *  �쳣������
	 * @param iCbBoxValues int[]
	 */
	private int getWldxConds() {

		Object oTmp = getUINormalPane().getComponent("wldx");
		if (oTmp != null) {
			// ����������������
			int iIdx = ((UIComboBox) oTmp).getSelectedIndex();
			/* ����"ȫ��"ѡ�� */
//			if (iIdx == 0) {
//				m_iWldx = 5;
//			} else {
			m_iWldx =iIdx;
			if (m_iSysCode == PubConstData.iApFlag && iIdx == 0)
				m_iWldx = PubConstData.iGYSflag;
			if(iIdx >= 1)
				m_iWldx= iIdx + 1;
		}
		return m_iWldx;
	}
	/**
	 * ÿ�������׳��쳣ʱ������
	 * @param exception java.lang.Throwable
	 */
	private void handleException(java.lang.Throwable e) {

		Log.getInstance(this.getClass()).error(e.getMessage(),e);
		showHintMessage(e.getMessage());
	}
	protected void initialize() {

		// called by concret class
		try {
			setName("AgeAnalyseYSPane");
			setSize(774, 419);
			setLayout(new java.awt.BorderLayout());
			add(getReportBase(), "Center");
			setButtons(new ButtonObject[] { m_boQuery, m_boDetail, m_boRate, m_boBill,m_boPrint, m_boRefresh });
			m_boPrint.addChildButton(directPrintBtn);
			m_boPrint.addChildButton(templetBtn);
			m_boPrint.addChildButton(templetViewBtn);
			setButtonEnable(false);
			initTemplet();
			modelBegin = PubMethodUI.getModelBegin(m_iSysCode);
		} catch (NoTempletException ex) {
			MessageDialog.showErrorDlg(this, NCLangRes.getInstance().getStrByID("20060504","UPP20060504-000102")/*@res "����"*/, ex.getHint());
		} catch (Throwable ivjExc) {
			handleException(ivjExc);
		}

	}
	/**
	 * ��ʼ��ģ��
	 * �������ڣ�(01-7-7 14:34:21)
	 */
	public void initTemplet() {
		m_voConds = getReportTools().getTempletData();

		/**�����ͷ��Ϣ*/
		insertHead();
	}
	/**
	 * ���ܣ������ͷ��ҳ��ʽ��Ϣ
	 * ���ߣ�����
	 * ����ʱ�䣺(2001-9-25 13:37:59)
	 * ������<|>
	 * ����ֵ��
	 * �㷨��
	 * �쳣������
	 */
	private void insertHead() {
		try {
			ReportItem[] items = new ReportItem[2];
			items[0] =
				PubMethodUI.getItem("zygs", NCLangRes.getInstance().getStrByID("20060504","UPP20060504-000103")/*@res "��ҳ��ʽ"*/, BillItem.COMBO, 4, 1);
			UIComboBox cbb =
				(UIComboBox) items[0].getComponent();
			cbb.addItem(constdata.ZYGS_JE);
			cbb.addItem(constdata.ZYGS_SHLJE);
			cbb.addItem(constdata.ZYGS_WBJE);
			cbb.addItem(constdata.ZYGS_SHLWB);
			items[1] =
				PubMethodUI.getItem("xsgs", NCLangRes.getInstance().getStrByID("common","UC000-0002449")/*@res "��ʾ��ʽ"*/, BillItem.COMBO, 5, 1);
			UIComboBox cbb1 =
				(UIComboBox) items[1].getComponent();
			cbb1.addItem(NCLangRes.getInstance().getStrByID("common","UC000-0001155")/*@res "����"*/);
			cbb1.addItem(NCLangRes.getInstance().getStrByID("common","UC000-0003279")/*@res "����"*/);
			cbb1.addItem(NCLangRes.getInstance().getStrByID("20060504","UPP20060504-000104")/*@res "����+����"*/);
			cbb1.setSelectedIndex(0);
			getReportBase().addHeadItem(items);
			getReportBase().addEditListener(this);
			(
					(UIComboBox) getReportBase()
					.getHeadItem("zygs")
					.getComponent())
					.addActionListener(
							this);
			(
					(UIComboBox) getReportBase()
					.getHeadItem("xsgs")
					.getComponent())
					.addActionListener(
							this);
			getReportTools().setHeadItemsEnable(false);
		} catch (NoTempletException e) {
			Log.getInstance(this.getClass()).error(e.getMessage(),e);
			showHintMessage(e.getMessage());
		}
	}
	/**
	 * ���ܣ������ʲ���ʽ�ı�
	 * ���ߣ�����
	 *  ����ʱ�䣺(2001-6-27 9:39:40)
	 *  ������<|>
	 *  ����ֵ��
	 *  �㷨��
	 *  �쳣������
	 */
	private void onBillTypeChanged() {
		try {
			String strNewType = (String)getReportBase().getHeadItem("zygs").getValueObject();
			if (!m_sCurrentGS.equals(strNewType)) {
				setBillType(strNewType);
				//�����ʲ���ʽ�ı�����ݱ䶯
				runQuery();
			}
		} catch (NoTempletException e) {
			Log.getInstance(this.getClass()).error(e.getMessage(),e);
			showHintMessage(e.getMessage());
		}
	}
	/**
	 * ����ʵ�ָ÷�������Ӧ��ť�¼���
	 * @version (00-6-1 10:32:59)
	 *
	 * @param bo ButtonObject
	 */
	public void onButtonClicked(ButtonObject bo) {

		int result=START;
		beginPressBtn(bo);

		try{
			if (bo == m_boDetail) {
				result=onDetail();

			} else if (bo == m_boQuery) {
				result=onQuery();

			} else if (bo == m_boRate) {
				result=onRate();

			} else if (bo == m_boBill) {
				try {
					getBill();
				} catch (Exception e) {
					showErrorMessage(e.getMessage());
				}
			}else if(bo == directPrintBtn){
				onPrint();
			}else if(bo == templetBtn){
				try {
					templatePrint();
				} catch (NoTempletException e) {
					Log.getInstance(this.getClass()).error(e.getMessage(),e);
				}
			}else if(bo == templetViewBtn){
				try {
					templatePrintView();
				} catch (NoTempletException e) {
					Log.getInstance(this.getClass()).error(e.getMessage(),e);
				}
			} else if (bo == m_boRefresh) {
				boolean msgShow=!doQuery();

				if(msgShow)
				{
					result = AccountTableQueryBasePanel.SHOWMSG;
				}
				else
				{
					result = getQryDlg().getResult();
				}
			}

		}finally{
			showProgressBar(false);
		}

		showButtonMessage(bo, result);
	}

	/**
	 * �˴����뷽��˵����
	 * �������ڣ�(2001-6-18 14:57:08)
	 */
	public int onDetail() {
		//m_boDetail.setEnabled(false);
		boolean msgShow=false;
		try{
			m_bCurrentDetail = !m_bCurrentDetail;
			//m_bRateFlag = false;
			String[] sDWBM = getUINormalPane().getCorpPKs();
			if (sDWBM == null) {
				sDWBM = new String[] { getClientEnvironment().getCorporation().getPk_corp()};
			}
			Map<String, List<String>> datas = this.getReportTools().getSelectedQryObjs(getReportBase(), (Vector<QryObjVO>)m_vObj.get(0));
			QryCondArrayVO origCond = null;
			if (m_bCurrentDetail) {
				origCond = (QryCondArrayVO) ((QryCondArrayVO[]) m_vObj.get(1))[0].clone();
				QryCondArrayVO arrayvo = QryObjTools.appendQryCondArrayVO(((QryCondArrayVO[]) m_vObj.get(1))[0], datas, sDWBM, (Vector<QryObjVO>)m_vObj.get(0));
				((QryCondArrayVO[]) m_vObj.get(1))[0] = arrayvo;
			}

			m_bRateFlag = getDataManager().getRate();
			m_vBase.setElementAt(new Boolean(m_bCurrentDetail),1);
			getDataManager().removeAll();
			getDataManager().setRate(m_bRateFlag);
			msgShow=!runQuery();
			if (m_bCurrentDetail) {
				((QryCondArrayVO[]) m_vObj.get(1))[0] = origCond;
			}

		}catch(Exception e){
			handleException(e);
			msgShow=true;
		}
		if(msgShow)
		{
			return SHOWMSG;
		}
		return SUCCESS;
	}
	/**
	 * ���ܣ���ӡ����
	 * ���ߣ�����
	 * ����ʱ�䣺(2001-10-15 18:33:25)
	 * ������<|>
	 * ����ֵ��
	 * �㷨��
	 * �쳣������
	 */
	public void onPrint() {
		try {
			getReportBase().previewData();
			//PrintDirectEntry print =
			//PrintManager.getDirectPrinter(getReportBase().getBillTable());
			//String[][] sTopStr = new String[1][2];
			//String sCurrType = getUINormalPane().getValueRef("bz", TYPE_NAME);
			//if (sCurrType == null || sCurrType.length() == 0) {
			//sCurrType = "ȫ������";
			//}
			//sTopStr[0][0] = "���֣�" + sCurrType;
			//sTopStr[0][1] = "��ҳ��ʽ��" + m_sCurrentGS;
			//print.setTopStr(sTopStr);
			//int iWidth = getReportBase().getBillTable().getColumnCount() - 1;
			//print.setTopStrColRange(new int[] { 2, iWidth });
			////��Ӧ�����һ�е�����
			//print.setTopStrAlign(new int[] { 0, 2 });
			//print.setTitle(getTitle());
			//print.preview();
		} catch (NoTempletException e) {
			Log.getInstance(this.getClass()).error(e.getMessage(),e);
			showHintMessage(e.getMessage());
		}
	}


//	@hl 2006-10-18 ģ���ӡԤ��
	private void templatePrintView() throws NoTempletException {
		ZlPubPrint zlPrint=new ZlPubPrint(getReportBase(),getBodyData(),getModuleCode());
		zlPrint.templatePrintView(getConditionVO());
	}

	private void templatePrint() throws NoTempletException {

		ZlPubPrint zlPrint=new ZlPubPrint(getReportBase(),getBodyData(),getModuleCode());
		if (getConditionVO().getVQryObj() == null ) {
			
		}
		zlPrint.templatePrint(getConditionVO());
	}

	private ConditionVO getConditionVO() {
		//
		return m_voConds;
	}
	/**
	 * �˴����뷽��˵����
	 * �������ڣ�(2001-5-17 15:13:30)
	 */
	protected int onQuery() {
		boolean msgShow=false;
		getQryDlg().showModal();
		if(getQryDlg().getResult() != QueryConditionClient.ID_OK)
			return CANCEL;//û��ѡ��ȷ����
		m_bRQQJ = getUINormalPane().getValueCbBoxIdx("model")==1;
		m_dateJZRQ = new UFDate(getUINormalPane().getValueRef("jzrq",constdata.TYPE_TEXT));
		if(m_bRQQJ){
			//getDateDlg().setJZRQ(m_dateJZRQ);
			getDateDlg().showModal();
			if(getDateDlg().getResult() != UIDialog.ID_OK)
				return CANCEL;//û��ѡ��ȷ����
		}
		showHintMessage(NCLangRes.getInstance().getStrByID("common","UCH046"));
		m_voConds.setQueryTypeInfo(getQryDlg().getQueryTypeInfo());
		m_voConds.setOffline(getQryDlg().getQueryTypeInfo().isAsynchronous());
		lockUI();
		new MySwingWork().start();

		setButtonEnable(true);
		if(msgShow)
		{
			return AccountTableQueryBasePanel.SHOWMSG;
		}
		else
		{
			return getQryDlg().getResult();
		}

		
	}

	private class MySwingWork extends SwingWorker {

		MySwingWork() {
		}

		public void finished() {
			releaseUI();
		}

		public Object construct() {

			// ���������Ĳ���

			return !doQuery();

		}

	}

	/**
	 * ���ܣ����������ʾ
	 * ���ߣ�����
	 * ����ʱ�䣺(2001-9-25 13:37:59)
	 * ������<|>
	 * ����ֵ��
	 * �㷨��
	 * �쳣������
	 */
	protected int onRate() {
		boolean msgShow=false;
		m_bRateFlag = !m_bRateFlag;
		getDataManager().setRate(m_bRateFlag);
		msgShow=!runQuery();

		if(msgShow)
		{
			return SHOWMSG;
		}
		return SUCCESS;
	}
	/**
	 * ���ܣ�������ʾ��ʽ�ı�
	 * ���ߣ�wyan
	 *  ����ʱ�䣺(2003-9-24 13:08:40)
	 *  ������<|>
	 *  ����ֵ��
	 *  �㷨��
	 *  �쳣������
	 */
	@SuppressWarnings("deprecation")
	private void onShowTypeChanged() {
		try {
			String strType = getReportBase().getHeadItem("xsgs").getValue();
			if (!m_sCurrXsGS.equals(strType)) {
				setShowType(strType);
				/**������ʾ��ʽ�ı�����ݱ䶯*/
				runQuery();
			}
		} catch (NoTempletException e) {
			Log.getInstance(this.getClass()).error(e.getMessage(),e);
			showHintMessage(e.getMessage());
		}
	}


	/**
	 * ���ܣ�ִ�в�ѯ
	 * ���ߣ�����
	 * ����ʱ�䣺(2001-8-23 15:59:27)
	 * ������<|>
	 * ����ֵ��
	 * �㷨��
	 * �쳣������
	 */
	private boolean runQuery() {

		try {
			m_voConds.setShowType(getShowType());
			Vector<String> cusCond =null;
			if (getQryDlg().getWhereSqlWithoutPower() != null) {
				cusCond = new Vector<String>();
				cusCond.add(getQryDlg().getWhereSqlWithoutPower());
			}
			m_voConds.setVetCustCond(cusCond);
			String msg=this.getUINormalPane().check();
			if(msg!=null)
				throw ExceptionHandler.createException(msg);
			insertDispCol2QryObj();

			if(m_bRQQJ&&m_vOther.get(1)==null ){
				showErrorMessage( nc.ui.ml.NCLangRes.getInstance().getStrByID("2006","UPP2006-000351")/*@res "������ѡ����ʾһ����������."*/);
				return false;
			}

			PubMethodVO.deleteChongfuQryobj(m_vObj);
			if(m_bRQQJ){
				Object[][] dates = (Object[][]) m_vOther.get(1);
				if (dates != null) {
					if ( dates[0][1] == null) {
						dates[0][1] = "1900-01-01";
					}
					if (!DumYearCheckUtil.check((String) dates[0][1], getUINormalPane().getCorpPKs(), this)) {
						return true;
					}
				}
			}


			getDataManager().setHeadVO(m_voHead);
			m_oBodyData =
				getDataManager().getResult(m_vBase, m_vObj, m_vOther, m_sCurrentGS, m_voConds,getPwCtrlvo());
			//

			addExtInfo(m_oBodyData); //wanglei 2014-04-17 ���ܲ��丽����Ϣ
			
			//
			if (!m_voConds.isOffline()) {
				getReportTools().onDisplayResult(getHeadShowVO(), m_oBodyData);
			} else {
				MessageDialog.showHintDlg(this, "��ʾ", "�첽�������ύ�����עƽ̨��Ϣ��");
			}

		} catch(NoDataException ex){
			getReportTools().setBodyData(new ClientVO[0]);
			showHintMessage(ex.getHint());
			return false;
		}catch (Exception e) {
			Log.getInstance(this.getClass()).error(e.getMessage());
			getReportTools().setBodyData(new ClientVO[0]);
			this.showErrorMessage(e.getMessage());
			showHintMessage(e.getMessage());
			return false;
		}

		finally{
			showProgressBar(false);
		}
		return true;
	}

private void addExtInfo(Object[] oBodyData) throws DAOException, InstantiationException, IllegalAccessException {
		// TODO Auto-generated method stub
//    m_Results = new Object[3];
//    m_Results[0] = PubMethodUI.convert(voResults);
//    Vector vetFlds = voConds.getVetDisplayFlds();
//    ReportItem[] items = new ReportItem[vetFlds.size()];
//    vetFlds.copyInto(items);
//    m_Results[1] = items;
//    FldgroupVO[] Flds = new FldgroupVO[m_vFldGroup.size()];
//    m_vFldGroup.copyInto(Flds);
//    m_Results[2] = Flds;
    
	ReportItem[] ext_items = new ReportItem[4];
	//��ʼ������
	for (int i =0; i< ext_items.length; i++){
		ext_items[i] = new ReportItem();
		ext_items[i].setWidth(100);
	}

	ext_items[3].setKey("ext_yqje");
	ext_items[3].setName("���ڽ��");
	ext_items[3].setDataType(IBillItem.DECIMAL);
	ext_items[0].setKey("ext_ywy");
	ext_items[0].setName("ҵ��Ա");
	ext_items[0].setDataType(IBillItem.STRING);
	ext_items[1].setKey("ext_ywbm");
	ext_items[1].setName("����");
	ext_items[1].setDataType(IBillItem.STRING);
	ext_items[2].setKey("ext_djrq");
	ext_items[2].setName("ҵ������");
	ext_items[2].setDataType(IBillItem.STRING);
	
	ReportItem[] com_items = new ReportItem[((ReportItem[])oBodyData[1]).length + 4];
	//��ʼ������
	
	for (int i =0; i< com_items.length; i++){   
		com_items[i] = new ReportItem();
	}
	int ishift = 2;
	if(((ReportItem[])oBodyData[1])[0].getKey().equalsIgnoreCase("zb_dwbm")){
		ishift = 4;  //�๫˾��ѯ
	}
	
	int j = 0; //�ϲ�����ļ���
	//1-2
	for (int i = 0; i < ishift; i ++) {   //�ͻ���Ϣ  fb_hbbm, fb_hbbm_name
		com_items[j] = ((ReportItem[])oBodyData[1])[i];
		com_items[j].setShowOrder(i);
		j++;
	}
	//3-5
	for (int i = 0; i < 3; i ++) {  //������չ��Ϣ�Ĳ��ţ���Ա������
		com_items[j] = ext_items[i];
		com_items[j].setShowOrder(j);
		j++;
	}
	
	//6-7
	for (int i = ishift; i < ishift + 2; i ++) {   //����������         bbye , bbye0
		com_items[j] = ((ReportItem[])oBodyData[1])[i];
		com_items[j].setShowOrder(j);
		j++;
	}
	
	//8
	
	com_items[j] = ext_items[3];    //���ڽ��
	com_items[j].setShowOrder(j);
	j++;
	//9-- ����չ���ֶ�
	for (int i = ishift + 2; i < ((ReportItem[])oBodyData[1]).length; i ++) {
		com_items[j] = ((ReportItem[])oBodyData[1])[i];
		com_items[j].setShowOrder(j);
		j++;
	}
	
	oBodyData[1] = com_items;
	
	IExtInfo srv = (IExtInfo) NCLocator.getInstance().lookup(IExtInfo.class);
	for (int i = 0 ; i < ((ClientVO[])oBodyData[0]).length; i++){
		UFDouble bbye =((ClientVO[])oBodyData[0])[i].getAttributeValue("bbye") == null? UFDouble.ZERO_DBL: (UFDouble)((ClientVO[])oBodyData[0])[i].getAttributeValue("bbye");
		UFDouble bbye0 =((ClientVO[])oBodyData[0])[i].getAttributeValue("bbye0") == null? UFDouble.ZERO_DBL: (UFDouble)((ClientVO[])oBodyData[0])[i].getAttributeValue("bbye0");
		String pk_org = ((ClientVO[])oBodyData[0])[i].getAttributeValue("zb_dwbm")==null? 
				ClientEnvironment.getInstance().getCorporation().getPk_corp() : (String)((ClientVO[])oBodyData[0])[i].getAttributeValue("zb_dwbm");
		HashMap<String, Object> extinfo = srv.qryBusiPsnByCust(pk_org, (String)((ClientVO[])oBodyData[0])[i].getAttributeValue("fb_hbbm"));
		((ClientVO[])oBodyData[0])[i].addField("ext_yqje", IBillItem.DECIMAL, bbye.sub(bbye0));
		((ClientVO[])oBodyData[0])[i].addField("ext_ywy", IBillItem.STRING, extinfo.get("ywymc"));
		((ClientVO[])oBodyData[0])[i].addField("ext_ywbm", IBillItem.STRING, extinfo.get("bmmc"));
		//((ClientVO[])oBodyData[0])[i].addField("ext_djrq", IBillItem.STRING,"");
		((ClientVO[])oBodyData[0])[i].addField("ext_djrq", IBillItem.STRING, srv.qryBusiDatebyCust(pk_org, (String)((ClientVO[])oBodyData[0])[i].getAttributeValue("fb_hbbm")));
	}
}
	//	[0]= QryObjVO  (��ʶ=4679)
//	m_fldCode= null
//	m_fldorigin= "fb"
//	m_fldtype= Integer  (��ʶ=785)
//	m_isBhxj= false
//	m_isDirty= false
//	m_isSum= UFBoolean  (��ʶ=786)
//	m_qryfld= "ksbm_cl"
//	m_strDisplayName= "�ͻ�"
//	�ѱ���ģ�������õ���ʾ����Ϊ��ѯ������뵽m_vObj�н��в�ѯ
	private void insertDispCol2QryObj(){
		ReportItem[] bodyItem = this.oldItems;

		Vector<ReportItem> vItem = new Vector<ReportItem>();
		HashSet<String> set = new HashSet<String>();
		String[] busiAttrs=null;
		Vector<String> v = new Vector<String>();
		Vector<QryObjVO> vQryObj = (Vector<QryObjVO>)m_vObj.get(0);
		for(int i = 0; i<vQryObj.size(); i++){
			QryObjVO qryobjVO = vQryObj.get(i);
			v.add(qryobjVO.getQryfld());
		}
		busiAttrs = new String[v.size()];
		v.copyInto(busiAttrs);
		if(bodyItem!=null){
			for(int i = 0; i < bodyItem.length;i ++){
				int pos = bodyItem[i].getPos();
				boolean isShow = bodyItem[i].isShow();
				int dataType = bodyItem[i].getDataType();
				if(pos==1&& dataType!= IBillItem.DECIMAL){
					String key = getDotKey(bodyItem[i]);
					int idx_dot=key.indexOf('.');
					String qryfld = key;
					if(idx_dot>-1){
						qryfld = key.substring(idx_dot+1);
					}
					if(qryfld!=null&&(qryfld.endsWith("_name")|| qryfld.endsWith("_code"))){
						qryfld=qryfld.substring(0,qryfld.length()-5);
					}
					boolean b = isFind(busiAttrs, qryfld);
					if(b){
						continue;
					}
					if(isShow && !set.contains(qryfld)){
						set.add(key);
						vItem.add(bodyItem[i]);

					}
				}
			}
		}
		for(int i=0; i<vItem.size(); i++){

			ReportItem item = vItem.get(i);
			boolean b =insertQryobj2vQryobj(item);
			if(!b){
				continue;
			}
			String key = getDotKey(item);
			String fldorigin = "fb";
			String qryfld = "";
			int idx_dot=key.indexOf('.');
			if(idx_dot>-1){
				fldorigin = key.substring(0,idx_dot);
				qryfld = key.substring(idx_dot+1);
				if(qryfld!=null&&(qryfld.endsWith("_name")|| qryfld.endsWith("_code"))){
					qryfld=qryfld.substring(0,qryfld.length()-5);
				}
			}else{
				Log.getInstance(this.getClass()).debug("####Error item key Ӧ����fb_xxxxxx:item.getKey():"+item.getKey());
			}
			String displayNam = item.getName();
			QryObjVO qryObjvo = new QryObjVO();
			qryObjvo.setQryfld(qryfld);
			qryObjvo.setFldorigin(fldorigin);
			qryObjvo.setM_strDisplayName(displayNam);
			qryObjvo.setIsSum(new UFBoolean(true));
			vQryObj.add(qryObjvo);
		}
	}

//	����ɹ� ����true
	private boolean insertQryobj2vQryobj(ReportItem item){
		String s = item.getKey();
		QryObjectVO[] vo = getUINormalPane().getQryObj();
		if(vo!=null){
			for(int i=0; i<vo.length; i++){
				String cond_fld = vo[i].getCond_fld();
				if(s!=null && cond_fld!=null&&cond_fld.trim().length()>0 && (s.equals(cond_fld)||s.indexOf(cond_fld)!=-1)){//item key == cond_fld
					QryObjVO qryObjvo = new QryObjVO();

					qryObjvo.setQryfld(vo[i].getCond_fld());
					qryObjvo.setFldorigin(vo[i].getCond_tab());
					qryObjvo.setM_strDisplayName(item.getName());
					qryObjvo.setIsSum(new UFBoolean(true));
					qryObjvo.setPk_bdinfo(vo[i].getPk_bdbdinfo());
					((Vector<QryObjVO>)m_vObj.get(0)).add(qryObjvo);

//					//ˢ��bddatavo HashMap����
//					String pk_bdinfo = vo[i].getPk_bdbdinfo();
//					getUINormalPane().createAllBddataVOs(pk_bdinfo);
					return true;
				}
			}
		}
		return false;
	}

	private String getDotKey(BillItem item){
		String key = item.getKey();
		if(key.indexOf("mingchen_")!=-1){
			key=key.substring(9);
		}
		if(key.indexOf("zb_")!=-1){
			key = key.replaceFirst("zb_", "zb.");//��һ��_
		}
		if(key.indexOf("fb_")!=-1){
			key = key.replaceFirst("fb_", "fb.");//��һ��_
		}
		return key;
	}
	private boolean isFind(String[] str,String s){
		if(str==null || s == null){
			return false;
		}
		if(s.endsWith("_mingchen")){
			return true;
		}
		for(int i = 0; i < str.length; i++){
			if(str[i]!=null &&( str[i].trim().equals(s.trim())||str[i].indexOf(s.trim())!=-1)){
				return true;
			}
		}
		return false;
	}

	/**
	 * ���ܣ�������ҳ��ʽ
	 *  ���ߣ�����
	 *  ����ʱ�䣺(2001-6-25 12:52:12)
	 *  ������int iType ��ʽ���� 0�������ʽ��
 	1������ҽ��ʽ��2�����������ʽ��3�����������ʽ
	 *  ����ֵ��
	 *  �㷨��
	 *  �쳣������
	 */
	protected void setBillType(String strType) {
		try {
			m_sCurrentGS = strType;
			UIComboBox cbb =
				(UIComboBox) getReportBase().getHeadItem("zygs").getComponent();
			cbb.removeActionListener(this);
			cbb.setSelectedItem(m_sCurrentGS);
			cbb.addActionListener(this);
			getHeadVO().setAttributeValue("zygs",m_sCurrentGS);
		} catch (NoTempletException e) {

		}
	}
	/**
	 * ���ܣ����ð�ť�Ŀ�����
	 * ���ߣ�����
	 * ����ʱ�䣺(2001-9-25 13:50:06)
	 * ������<|>
	 * ����ֵ��
	 * �㷨��
	 * �쳣������
	 * @param bFlag boolean
	 */
	public void setButtonEnable(boolean bFlag) {
		m_boDetail.setEnabled(bFlag);
		m_boRate.setEnabled(bFlag);
		m_boPrint.setEnabled(bFlag);
		m_boRefresh.setEnabled(bFlag);

		m_boBill.setEnabled(false);
		this.updateButtons();
	}
	@Override
	public void setLinkQueryButtonEnable(boolean[] flag)
	{
		m_boBill.setEnabled(flag[0]);
		updateButton(m_boBill);
	}


	/**
	 * ���ܣ����ñ�ͷ��Ϣ�еĵ�λ��Ϣ,�Լ���ҳ��ʽ��Ϣ
	 * ����:����
	 * ����ʱ��:(2002-6-27 16:53:03)
	 * ����:<|>
	 * ����ֵ:
	 * �㷨:
	 * @return nc.vo.arap.pub.ClientVO
	 * @param param nc.vo.arap.pub.ClientVO
	 */
	private ClientVO setCorpItem(ClientVO voHead) {
		try {
//			RefResultVO[] voCorp = getQryDlg().getMutiUnits();
			String sCorp = "";
//			if (voCorp != null && voCorp.length > 0) {
//				for (int i = 0; i < voCorp.length; i++) {
//					sCorp += "," + voCorp[i].getRefName();
//				}
//			} else {
				sCorp = "," + ClientEnvironment.getInstance().getCorporation().getUnitname();
//			}
			voHead.getValues()[voHead.getAttributeIndex("corp")] = sCorp.substring(1);
			voHead.getValues()[voHead.getAttributeIndex("zygs")] = m_sCurrentGS;
		} catch (Exception e) {
			Log.getInstance(this.getClass()).error(e.getMessage(),e);
		}
		return voHead;
	}
	/**
	 * ���ܣ����ó�������
	 * ���ߣ�����
	 * ����ʱ�䣺(2001-9-21 10:51:05)
	 * ������<|>
	 * ����ֵ��
	 * �㷨��
	 * �쳣������
	 */
//	@SuppressWarnings("unchecked")
//	private void setCustomCond() {
//		Vector vCustomConds = new Vector();
////		vCustomConds.addElement(getQryDlg().getConditionVO());
//		m_voConds.setVetCustCond(vCustomConds);//�Զ�������
//	}
	/**
	 * ���ܣ����ò�ѯ����
	 * ���ߣ�����
	 * ����ʱ�䣺(2001-8-23 15:47:03)
	 * ������<|>
	 * ����ֵ��
	 * �㷨��
	 * �쳣������
	 */
	@SuppressWarnings("restriction")
	private void setQueryCond() throws BusinessException{
		/**[iPoint,iModel,�����or������iWLDX��������]*/
		int[] iPMF = new int[4];
		/**Ӧ�ա�Ӧ��*/
		if ((m_iSysCode == iArFlag || m_iSysCode == iApFlag) && !m_bRQQJ) {
			iPMF[1] =
				getUINormalPane().getValueCbBoxIdx("model") * 10
				+ getUINormalPane().getValueCbBoxIdx("date");

		} else { /**����*/
			iPMF[1] =
				getUINormalPane().getValueCbBoxIdx("model") * 10
				+ getUINormalPane().getValueCbBoxIdx("date")
				+ 1;
		}
		iPMF[0] = m_iSysCode;
		iPMF[2] = getUINormalPane().getValueCbBoxIdx("zzye");
		iPMF[3] = getWldxConds();

		int iAspect = getUINormalPane().getValueCbBoxIdx("aspect");

		boolean[] bFB = new boolean[] { true, true }; //���ҡ�����

		String[] sDWBM = getUINormalPane().getCorpPKs();
		if (sDWBM == null) {
			sDWBM = new String[] { getClientEnvironment().getCorporation().getPk_corp()};
		}
		boolean[] bZt = getUINormalPane().getValueChkBox();
		String sZLFA = getUINormalPane().getValueRef("zlfa", constdata.TYPE_PK);

		if(!m_bRQQJ&&sZLFA==null)
			throw ExceptionHandler.createException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("2006","UPP2006-v55-010260")/*@res "�������������ѡ�����䷽��!"*/);

		//׼����ѯ��������
		Vector<QryObjVO> vQryObj = getUINormalPane().getValueQryObj();
		//׼����ѯ��������
		QryCondArrayVO[] voConds = null;
		voConds = getOtherConds();
		getUINormalPane().getValueCustCond();

		//��Ҫ�����ѯ����
		m_vBase = new Vector<Object>();
		m_vBase.addElement(iPMF); //0
		m_vBase.addElement(new Boolean(m_bCurrentDetail)); //1
		m_vBase.addElement(new Integer(iAspect)); //2
		m_vBase.addElement(m_dateJZRQ.toString()); //3
		m_vBase.addElement(null); //4sBZ
		m_vBase.addElement(bFB); //5
		m_vBase.addElement(bZt); //6
		m_vBase.addElement(new Integer(0)); //7 Ӧ��/Ӧ���������

		int sel_djzt = getUINormalPane().getValueCbBoxIdx("djzt");
		int djzt = -10000;
		if(sel_djzt == 0){
			//ȫ��
			djzt=-10000;
		}else if(sel_djzt == 1){
			//�ѱ���
			djzt=1;
		}else if(sel_djzt == 2){
			//�����
			djzt=2;
		}else if(sel_djzt ==3){
			//����Ч
			djzt=10;
		}
		m_vBase.addElement(new Integer(djzt)); //8 ����״̬

		m_vObj = new Vector<Object>();
		m_vObj.addElement(vQryObj); //0
		m_vObj.addElement(voConds); //1

		m_vOther = new Vector<Object>();
		m_vOther.addElement(sDWBM); //0
		if (m_bRQQJ){
			Object[][] dates = getDateDlg().getData();
			if (dates == null) throw new BusinessException( nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("20060601","UPT20060601-v55-000042")/*@res "������ѡ����ʾһ����������."*/);
			if (dates[0][1] == null) {
				dates[0][1] = "1900-01-01";
				//throw new BusinessException( nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("20060601","UPT20060601-v55-000042")/*@res "������ѡ����ʾһ����������."*/);
			}
			m_vOther.addElement(dates);

//			if(getDateDlg().getData()==null || getDateDlg().getData().length==0){
//			showErrorMessage( "������ѡ����ʾһ����������.");
//			return;
//			}
		}
		//1
		else
			m_vOther.addElement(sZLFA);
		//1

		m_voConds.setVQryObj(vQryObj);
		setShowType(NCLangRes.getInstance().getStrByID("common","UC000-0001155")/*@res "����"*/);
		m_voConds.setShowType(getShowType());
		String custcond = null;
		try {
			UFBoolean isPower = SysInitBO_Client.getParaBoolean(ClientEnvironment.getInstance().getCorporation().getPk_corp(), "FICOMMON05");
			if (isPower.booleanValue()) {
				custcond = this.getQryDlg().getWhereSQL();
			} else {
				custcond =  this.getQryDlg().getWhereSqlWithoutPower();
			}
		} catch (BusinessException e) {
			Log.getInstance(this.getClass()).error(e.getMessage(),e);
		}

		if(null!=custcond&&custcond.length()>0){
			Vector<String> v=new Vector<String>();
			v.add(custcond);
			m_voConds.setVetCustCond(v);
		}
	}
	/**
	 * ���ܣ�������ʾ��ʽ
	 *  ���ߣ�wyan
	 *  ����ʱ�䣺(2003-9-24 13:09:12)
	 *  ������String strType ���ʽ����ҽ��ʽ
	 *  ����ֵ��
	 *  �㷨��
	 *  �쳣������
	 */
	protected void setShowType(String strType) {
		try {
			m_sCurrXsGS = strType;
			UIComboBox cbb =
				(UIComboBox) getReportBase().getHeadItem("xsgs").getComponent();
			cbb.removeActionListener(this);
			cbb.setSelectedItem(m_sCurrXsGS);
			cbb.addActionListener(this);
			getHeadVO().setAttributeValue("xsgs",m_sCurrXsGS);
		} catch (NoTempletException e) {
			Log.getInstance(this.getClass()).error(e.getMessage(),e);
			showHintMessage(e.getMessage());
		}
	}

	public void doMaintainAction(ILinkMaintainData maintaindata) {
		try {
			AgeAnalyseAsynResultVO result = (AgeAnalyseAsynResultVO) FileManageUtils.readObjectByFileName(maintaindata.getUserObject().toString());
			m_voHead = result.getHeadVO();
			m_voConds.setStatValueObjects(result.getStatValueObjects());
			m_voConds.setOffline(false);
			m_voConds.setOfflineDis(true);
			m_vObj = result.getParamVO().getM_vObj();
			m_vBase = result.getParamVO().getM_vBase();
			m_vOther = result.getParamVO().getM_vOther();
			if (m_vObj != null && m_vObj.size() > 0) {
				getConditionVO().setVQryObj((Vector<QryObjVO>) m_vObj.get(0));
			}
			
			//getConditionVO().setVQryObj(m_vObj);
			getQryDlg();
			setBillType(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("2006","UPP2006-v56-000047")/*@res "���ʽ"*/);
			changeZygsValues();
			runQuery();
			m_voConds.setOfflineDis(false);
			m_boRefresh.setEnabled(false);
			m_boRate.setEnabled(true);
			m_boDetail.setEnabled(true);
			m_boPrint.setEnabled(true);
			updateButtons();
			getReportTools().setHeadItemsEnable(true);
		} catch (NoTempletException e) {
			ExceptionHandler.consume(e);
		} catch (Exception e) {
			ExceptionHandler.consume(e);
		}
	}
}