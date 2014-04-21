package nc.ui.arap.ageanalyse;

/**
 * 应收、应付、往来账龄分析界面基类
 * 创建日期：(2001-5-17 11:59:57)
 * @author：金冬梅
 * 修改人：宋涛
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

	/**查询模板*/
	private QueryConditionDLG m_qryDlg = null;
	/**报表模板*/
	private ReportBaseClass m_ReportTemplet = null;
	/**日期区间设置对话框*/
	@SuppressWarnings("restriction")
	private nc.ui.groupware.particularquery.DateDlg m_dateDlg = null;
	/**查询模板中的常用条件*/
	private NormalPanel m_pNormalPane = null;
	/**前台数据管理类*/
	private ManageDataYS m_manageData = null;
	/**界面控制类*/
	private UIController m_UiController = null;
	/**报表模板工具*/
	private ReportTools m_ReportTools = null;
	/**缓存查询条件，为点击详细按钮或切换账页格式时重新查询做准备*/
	private Vector<Object> m_vBase = null;
	private Vector<Object> m_vObj = null;
	private Vector<Object> m_vOther = null;
	private UFDate m_dateJZRQ = null;

	/**当前选择的账页格式*/
	private String m_sCurrentGS = null;
	/**当前显示格式*/
	private String m_sCurrXsGS = null;
	/**当前为按单据展开的形式*/
	private boolean m_bCurrentDetail = false;
	/**是否显示比率*/
	private boolean m_bRateFlag = false;
	/**是否按日期分析*/
	private boolean m_bRQQJ = false;
	/**功能节点号*/
	protected int m_iSysCode = 3;
	/**往来对象*/
	protected int m_iWldx = 0;
	/**存储报表模板中定义的数据*/
	private ConditionVO m_voConds = null;
	/**表头vo*/
	private ClientVO m_voHead = null;
	private Object[] m_oBodyData = null;
	protected 	IuiConstData constdata= new IuiConstData();

	//报表模板初始的Item
	private ReportItem[] oldItems=null;

	private String[] modelBegin;

	/**
	 * AllPane 构造子注解。
	 */
	public AgeAnalyseYSPane() {
		super();
	}
	/** 功能：处理界面事件
	 *  作者：宋涛
	 *  创建时间：2001-06-25
	 *  参数：界面事件
	 *  返回值：
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

		//add by twei 判断所选行是否可以进行单据联查
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
	 * 功能：改变帐页格式的取值范围
	 * 作者：宋涛
	 * 创建时间：(2001-9-25 11:41:36)
	 * 参数：<|>
	 * 返回值：
	 * 算法：
	 * 异常描述：
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
	 * a功能:
	 * 作者：宋涛
	 * 创建时间：(2002-4-4 14:22:48)
	 * 参数：<|>
	 * 返回值：
	 * 算法：
	 */
	public void doInputCond() {
		getQryDlg().showModal();
		if (getQryDlg().getResult() != UIDialog.ID_OK)
			return;
		//没有选择“确定”

		m_bRQQJ = getUINormalPane().getValueCbBoxIdx("model") == 1;
		m_dateJZRQ = new UFDate(getUINormalPane().getValueRef("jzrq", constdata.TYPE_TEXT));
		if (m_bRQQJ) {
			//getDateDlg().setJZRQ(m_dateJZRQ);
			getDateDlg().showModal();
			if (getDateDlg().getResult() != UIDialog.ID_OK)
				return;
			//没有选择“确定”
		}
	}
	/**
	 * a功能:
	 * 作者：宋涛
	 * 创建时间：(2002-4-4 13:55:48)
	 * 参数：<|>
	 * 返回值：
	 * 算法：
	 */
	public boolean doQuery() {
		boolean status=false;
		try{

			showProgressBar(true);

			getUINormalPane().stopEdit();
			m_voHead = getUINormalPane().getHeadValues();
			////m_voHead = setCorpItem(m_voHead);
			/**修改帐页格式取值范围*/
			changeZygsValues();
			/**使能详细按钮*/
			m_bCurrentDetail = false;
			m_bRateFlag = false;
			/**设置查询条件*/
			setQueryCond();
			getDataManager().removeAll();
			getDataManager().setFbFlag(MyClientEnvironment.isFracUsed());
			QueryStructVO queryStructVO = new QueryStructVO();
			queryStructVO.setCorp(getUINormalPane().getCorpPKs());
			queryStructVO.setVetQryObj(getUINormalPane().getValueQryObj());
			queryStructVO.setVoNormalCond(new QryCondArrayVO[]{getUINormalPane().getValueCond()});
			CkResultVO ckResultVO = MultiDataSourceUtil.check(queryStructVO, getNote());
			if (ckResultVO != null && ckResultVO.isChangeFlag()) {
				int selected = MessageDialog.showYesNoCancelDlg(this, nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("2006","UPP2006-v56-000046")/*@res "判断是否切换数据源"*/, ckResultVO.getShowMessage());
				if (selected == UIDialog.ID_YES) {
					m_vBase.add(ckResultVO.getNewDataSource());
				} else if (selected != UIDialog.ID_NO) {
					return false;
				}
			}
			status=runQuery();
			/**使能表头帐页格式*/
			getReportTools().setHeadItemsEnable(true);
		}catch(Exception e){
			setButtonEnable(false);//没有查出来数据，其他按钮设置为不可用
			this.showErrorMessage(e.getMessage());
			return false;
		}
		finally{
			showProgressBar(false);
		}
		return status;
	}
	/**
	 * 功能：得到单据
	 * 作者：宋涛
	 * 创建时间：(2001-11-14 14:17:13)
	 * 参数：<|>
	 * 返回值：
	 * 算法：
	 * 异常描述：
	 *
	 */
	protected void getBill() throws Exception{
		String[] sOids;
		try {
			sOids =AccountTableQueryBasePanel.getVouchidFromReport(getReportBase(),"zb_vouchid");
			if(sOids==null || sOids.length<=0)
				throw new Exception(NCLangRes4VoTransl.getNCLangRes().getStrByID("20060504", "UPP20060504-000088")/*
				 * @res
				 * "请选择详细单据信息联查！"
				 */);
		} catch (Exception exp) {
			Log.getInstance(this.getClass()).error(exp.getMessage(), exp);
			throw new NoDataException(NCLangRes4VoTransl.getNCLangRes().getStrByID("20060504", "UPP20060504-000088")/*
			 * @res
			 * "请选择详细单据信息联查！"
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
	 * 功能：得到查询单据状态
	 * 作者:宋涛
	 * 创建时间:(2002-12-3 13:34:29)
	 * 参数:<|>
	 * 返回值:
	 * 算法:
	 *
	 * @return boolean[]
	 */
	public boolean[] getbillStat() {
		return getUINormalPane().getValueChkBox();
	}
	/**
	 * a功能：
	 * 作者:宋涛
	 * 创建时间:(2002-12-5 16:14:19)
	 * 参数:<|>
	 * 返回值:
	 * 算法:
	 *
	 * @return java.lang.Object[]
	 */
	public Object[] getBodyData() {
		return m_oBodyData;
	}
	/**
	 * 功能：得到数据管理类
	 * 作者：宋涛
	 * 创建时间：(2001-8-23 13:48:38)
	 * 参数：<|>
	 * 返回值：
	 * 算法：
	 * 异常描述：
	 */
	public ManageDataYS getDataManager() {
		if(m_manageData==null){
			m_manageData = new ManageDataYS();
		}
		return m_manageData;
	}
	/**
	 * 此处插入方法说明。
	 * 创建日期：(2001-6-21 12:32:57)
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
	 * 功能：得到表头显示vo
	 * 作者:宋涛
	 * 创建时间:(2002-6-28 15:57:46)
	 * 参数:<|>
	 * 返回值:
	 * 算法:
	 * @return nc.vo.arap.pub.HeadVO
	 */
	public HeadVO getHeadShowVO() {
		HeadVO vo = new HeadVO();
		vo.setData(getHeadVO());
		vo.setObject((Vector)m_vObj.elementAt(0));
		return vo;
	}
	/**
	 * 功能：得到表头vo
	 * 作者:宋涛
	 * 创建时间:(2002-6-27 16:39:48)
	 * 参数:<|>
	 * 返回值:
	 * 算法:
	 * @return nc.vo.arap.pub.ClientVO
	 */
	public ClientVO getHeadVO() {
		if (m_voHead == null) {
			m_voHead = getUINormalPane().getHeadValues();
		}
		return m_voHead;
	}
	/**
	 * 功能：得到系统编号
	 * 作者:宋涛
	 * 创建时间:(2002-12-3 13:08:55)
	 * 参数:<|>
	 * 返回值:
	 * 算法:
	 *
	 * @return int
	 */
	public int getISysCode() {
		return m_iSysCode;
	}
	/**
	 * 子类重载此方法，来区分不同的调用节点
	 * 创建日期：(2001-6-21 12:38:40)
	 * @return java.lang.String
	 */
	public abstract String getNote();
	/**
	 * 功能：得到当前行的oid
	 * 作者：宋涛
	 * 创建时间：(2001-11-14 14:18:18)
	 * 参数：<|>
	 * 返回值：
	 * 算法：
	 * 异常描述：
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
	 * 功能：得到其他查询条件
	 * 作者：宋涛
	 * 创建时间：(2001-9-11 14:13:21)
	 * 参数：<|>
	 * 返回值：
	 * 算法：
	 * 异常描述：
	 * @return nc.vo.arap.pub.QryCondArrayVO[]
	 */
	private QryCondArrayVO[] getOtherConds() {
		/**用于记录查询条件vo集合*/
		Vector<QryCondArrayVO> vetOtherConds = new Vector<QryCondArrayVO>();
		/**得到查询常用条件*/
		QryCondArrayVO voObjCond = getUINormalPane().getValueCond();
		if(voObjCond!= null){
			vetOtherConds.addElement(voObjCond);
		}
		/**得到区分客户供应商的条件*/
		voObjCond = getUINormalPane().getValueCustCond();
		if(voObjCond!=null){
			vetOtherConds.addElement(voObjCond);
		}
		//增加应收范围
		voObjCond = getUINormalPane().getValueDataRangCond();
		if(voObjCond!=null){
			vetOtherConds.addElement(voObjCond);
		}
		/**得到币种输入条件*/
		String[] sBzs = getUINormalPane().getValuesRef("bz",constdata.TYPE_PK);
		/**设置金额小数位数*/
		getReportTools().setMaxJeDec(sBzs);
		getDataManager().setIybdig(ReportTools.getMaxJeDec(sBzs));
		/**改变帐页格式，与相应币种相一致*/
		if(sBzs == null || sBzs.length<=0 ||PubMethodUI.isBB(sBzs)){
			m_sCurrentGS = constdata.ZYGS_JE;
		}else{
			m_sCurrentGS = constdata.ZYGS_WBJE;
		}
		setBillType(m_sCurrentGS);

		/**添加币种条件*/
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
	 * 功能：得到权限控制vo
	 * 作者:宋涛
	 * 创建时间:(2002-8-7 13:20:45)
	 * 参数:<|>
	 * 返回值:
	 * 算法:
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
					nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UC000-0002782")/*@res "查询条件"*/);
			m_qryDlg.setNormalPanel(getUINormalPane());
			m_qryDlg.setVisibleNormalPanel(true);
			m_qryDlg.setVisibleUserDefPanel(true);
			m_qryDlg.registerQueryTemplateTotalVOProceeor(new IQueryTemplateTotalVOProcessor() {
				public void processQueryTempletTotalVO(QueryTempletTotalVO totalVO) {
					QueryConditionVO[] conds = totalVO.getConditionVOs();
					// 当币种未设默认值时，设置币种的默认值
					QueryPubMethod.updateDefItems(ProxyBill.getInstance(), conds,
							ClientEnvironment.getInstance().getCorporation().getPk_corp());
				}
			});
		}
		return m_qryDlg;
	}
	/**
	 * 返回 ReportBaseClass 特性值。
	 * @return nc.ui.pub.report.ReportBaseClass
	 */
	/* 警告：此方法将重新生成。 */
	private ReportBaseClass getReportBase() throws NoTempletException {
		if (m_ReportTemplet == null) {
			try {
				m_ReportTemplet = new ReportBaseClass();
				m_ReportTemplet.setName("ReportBase");
				// user code begin {1}
				//设置模板编码
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
	 * 功能：得到报表模板工具
	 * 作者：宋涛
	 * 创建时间：(2001-9-24 13:56:44)
	 * 参数：<|>
	 * 返回值：
	 * 算法：
	 * 异常描述：
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
	 * a功能:
	 * 作者：宋涛
	 * 创建时间：(2002-4-4 13:21:20)
	 * 参数：<|>
	 * 返回值：
	 * 算法：
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
	 * 取得显示格式。
	 * 创建日期：(2003-9-24 14:21:45)
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
	 * 功能：得到界面控制类
	 * 作者：宋涛
	 * 创建时间：(2001-9-24 20:25:24)
	 * 参数：<|>
	 * 返回值：
	 * 算法：
	 * 异常描述：
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
	 *  功能：得到查询模板常用查询条件pane
	 *  作者：宋涛
	 *  创建时间：(2001-8-16 15:51:39)
	 *  参数：<|>
	 *  返回值：
	 *  算法：
	 *  异常描述：
	 * @return nc.ui.arap.pub.NormalPanel
	 */
	private NormalPanel getUINormalPane() {
		if (m_pNormalPane == null) {
			QueryConditionDLG dlg = this.getQryDlg();
			m_pNormalPane =
				new NormalPanel(getNote(), modelBegin, dlg);
			getUINormalPane().addControlListener(getUIController());
			m_pNormalPane =NormalPanelCreater.getNormalPanelForZL(m_pNormalPane,m_iSysCode ,this.getNote() );
			/** 为常用条件页签添加监听 */



		}
		return m_pNormalPane;
	}
	/**
	 * 功能：得到往来对象查询条件
	 *  作者：宋涛
	 *  创建时间：(2001-6-22 14:18:41)
	 *  参数：<|>
	 *  返回值：
	 *  算法：
	 *  异常描述：
	 * @param iCbBoxValues int[]
	 */
	private int getWldxConds() {

		Object oTmp = getUINormalPane().getComponent("wldx");
		if (oTmp != null) {
			// 处理往来对象条件
			int iIdx = ((UIComboBox) oTmp).getSelectedIndex();
			/* 增加"全部"选项 */
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
	 * 每当部件抛出异常时被调用
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
			MessageDialog.showErrorDlg(this, NCLangRes.getInstance().getStrByID("20060504","UPP20060504-000102")/*@res "错误"*/, ex.getHint());
		} catch (Throwable ivjExc) {
			handleException(ivjExc);
		}

	}
	/**
	 * 初始化模板
	 * 创建日期：(01-7-7 14:34:21)
	 */
	public void initTemplet() {
		m_voConds = getReportTools().getTempletData();

		/**插入表头信息*/
		insertHead();
	}
	/**
	 * 功能：插入表头帐页格式信息
	 * 作者：宋涛
	 * 创建时间：(2001-9-25 13:37:59)
	 * 参数：<|>
	 * 返回值：
	 * 算法：
	 * 异常描述：
	 */
	private void insertHead() {
		try {
			ReportItem[] items = new ReportItem[2];
			items[0] =
				PubMethodUI.getItem("zygs", NCLangRes.getInstance().getStrByID("20060504","UPP20060504-000103")/*@res "帐页格式"*/, BillItem.COMBO, 4, 1);
			UIComboBox cbb =
				(UIComboBox) items[0].getComponent();
			cbb.addItem(constdata.ZYGS_JE);
			cbb.addItem(constdata.ZYGS_SHLJE);
			cbb.addItem(constdata.ZYGS_WBJE);
			cbb.addItem(constdata.ZYGS_SHLWB);
			items[1] =
				PubMethodUI.getItem("xsgs", NCLangRes.getInstance().getStrByID("common","UC000-0002449")/*@res "显示格式"*/, BillItem.COMBO, 5, 1);
			UIComboBox cbb1 =
				(UIComboBox) items[1].getComponent();
			cbb1.addItem(NCLangRes.getInstance().getStrByID("common","UC000-0001155")/*@res "名称"*/);
			cbb1.addItem(NCLangRes.getInstance().getStrByID("common","UC000-0003279")/*@res "编码"*/);
			cbb1.addItem(NCLangRes.getInstance().getStrByID("20060504","UPP20060504-000104")/*@res "名称+编码"*/);
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
	 * 功能：处理帐簿格式改变
	 * 作者：宋涛
	 *  创建时间：(2001-6-27 9:39:40)
	 *  参数：<|>
	 *  返回值：
	 *  算法：
	 *  异常描述：
	 */
	private void onBillTypeChanged() {
		try {
			String strNewType = (String)getReportBase().getHeadItem("zygs").getValueObject();
			if (!m_sCurrentGS.equals(strNewType)) {
				setBillType(strNewType);
				//处理帐簿格式改变后数据变动
				runQuery();
			}
		} catch (NoTempletException e) {
			Log.getInstance(this.getClass()).error(e.getMessage(),e);
			showHintMessage(e.getMessage());
		}
	}
	/**
	 * 子类实现该方法，响应按钮事件。
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
	 * 此处插入方法说明。
	 * 创建日期：(2001-6-18 14:57:08)
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
	 * 功能：打印报表
	 * 作者：宋涛
	 * 创建时间：(2001-10-15 18:33:25)
	 * 参数：<|>
	 * 返回值：
	 * 算法：
	 * 异常描述：
	 */
	public void onPrint() {
		try {
			getReportBase().previewData();
			//PrintDirectEntry print =
			//PrintManager.getDirectPrinter(getReportBase().getBillTable());
			//String[][] sTopStr = new String[1][2];
			//String sCurrType = getUINormalPane().getValueRef("bz", TYPE_NAME);
			//if (sCurrType == null || sCurrType.length() == 0) {
			//sCurrType = "全部币种";
			//}
			//sTopStr[0][0] = "币种：" + sCurrType;
			//sTopStr[0][1] = "帐页格式：" + m_sCurrentGS;
			//print.setTopStr(sTopStr);
			//int iWidth = getReportBase().getBillTable().getColumnCount() - 1;
			//print.setTopStrColRange(new int[] { 2, iWidth });
			////对应的最后一列的列数
			//print.setTopStrAlign(new int[] { 0, 2 });
			//print.setTitle(getTitle());
			//print.preview();
		} catch (NoTempletException e) {
			Log.getInstance(this.getClass()).error(e.getMessage(),e);
			showHintMessage(e.getMessage());
		}
	}


//	@hl 2006-10-18 模板打印预览
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
	 * 此处插入方法说明。
	 * 创建日期：(2001-5-17 15:13:30)
	 */
	protected int onQuery() {
		boolean msgShow=false;
		getQryDlg().showModal();
		if(getQryDlg().getResult() != QueryConditionClient.ID_OK)
			return CANCEL;//没有选择“确定”
		m_bRQQJ = getUINormalPane().getValueCbBoxIdx("model")==1;
		m_dateJZRQ = new UFDate(getUINormalPane().getValueRef("jzrq",constdata.TYPE_TEXT));
		if(m_bRQQJ){
			//getDateDlg().setJZRQ(m_dateJZRQ);
			getDateDlg().showModal();
			if(getDateDlg().getResult() != UIDialog.ID_OK)
				return CANCEL;//没有选择“确定”
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

			// 处理大事务的操作

			return !doQuery();

		}

	}

	/**
	 * 功能：处理比率显示
	 * 作者：宋涛
	 * 创建时间：(2001-9-25 13:37:59)
	 * 参数：<|>
	 * 返回值：
	 * 算法：
	 * 异常描述：
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
	 * 功能：处理显示格式改变
	 * 作者：wyan
	 *  创建时间：(2003-9-24 13:08:40)
	 *  参数：<|>
	 *  返回值：
	 *  算法：
	 *  异常描述：
	 */
	@SuppressWarnings("deprecation")
	private void onShowTypeChanged() {
		try {
			String strType = getReportBase().getHeadItem("xsgs").getValue();
			if (!m_sCurrXsGS.equals(strType)) {
				setShowType(strType);
				/**处理显示格式改变后数据变动*/
				runQuery();
			}
		} catch (NoTempletException e) {
			Log.getInstance(this.getClass()).error(e.getMessage(),e);
			showHintMessage(e.getMessage());
		}
	}


	/**
	 * 功能：执行查询
	 * 作者：宋涛
	 * 创建时间：(2001-8-23 15:59:27)
	 * 参数：<|>
	 * 返回值：
	 * 算法：
	 * 异常描述：
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
				showErrorMessage( nc.ui.ml.NCLangRes.getInstance().getStrByID("2006","UPP2006-000351")/*@res "请至少选择显示一个日期区间."*/);
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

			addExtInfo(m_oBodyData); //wanglei 2014-04-17 骏杰补充附加信息
			
			//
			if (!m_voConds.isOffline()) {
				getReportTools().onDisplayResult(getHeadShowVO(), m_oBodyData);
			} else {
				MessageDialog.showHintDlg(this, "提示", "异步任务已提交，请关注平台消息！");
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
	//初始化数组
	for (int i =0; i< ext_items.length; i++){
		ext_items[i] = new ReportItem();
		ext_items[i].setWidth(100);
	}

	ext_items[3].setKey("ext_yqje");
	ext_items[3].setName("逾期金额");
	ext_items[3].setDataType(IBillItem.DECIMAL);
	ext_items[0].setKey("ext_ywy");
	ext_items[0].setName("业务员");
	ext_items[0].setDataType(IBillItem.STRING);
	ext_items[1].setKey("ext_ywbm");
	ext_items[1].setName("部门");
	ext_items[1].setDataType(IBillItem.STRING);
	ext_items[2].setKey("ext_djrq");
	ext_items[2].setName("业务日期");
	ext_items[2].setDataType(IBillItem.STRING);
	
	ReportItem[] com_items = new ReportItem[((ReportItem[])oBodyData[1]).length + 4];
	//初始化数组
	
	for (int i =0; i< com_items.length; i++){   
		com_items[i] = new ReportItem();
	}
	int ishift = 2;
	if(((ReportItem[])oBodyData[1])[0].getKey().equalsIgnoreCase("zb_dwbm")){
		ishift = 4;  //多公司查询
	}
	
	int j = 0; //合并数组的计数
	//1-2
	for (int i = 0; i < ishift; i ++) {   //客户信息  fb_hbbm, fb_hbbm_name
		com_items[j] = ((ReportItem[])oBodyData[1])[i];
		com_items[j].setShowOrder(i);
		j++;
	}
	//3-5
	for (int i = 0; i < 3; i ++) {  //插入扩展信息的部门，人员和日期
		com_items[j] = ext_items[i];
		com_items[j].setShowOrder(j);
		j++;
	}
	
	//6-7
	for (int i = ishift; i < ishift + 2; i ++) {   //余额和账期内         bbye , bbye0
		com_items[j] = ((ReportItem[])oBodyData[1])[i];
		com_items[j].setShowOrder(j);
		j++;
	}
	
	//8
	
	com_items[j] = ext_items[3];    //逾期金额
	com_items[j].setShowOrder(j);
	j++;
	//9-- 账期展开字段
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
	//	[0]= QryObjVO  (标识=4679)
//	m_fldCode= null
//	m_fldorigin= "fb"
//	m_fldtype= Integer  (标识=785)
//	m_isBhxj= false
//	m_isDirty= false
//	m_isSum= UFBoolean  (标识=786)
//	m_qryfld= "ksbm_cl"
//	m_strDisplayName= "客户"
//	把报表模板中设置的显示列作为查询对象插入到m_vObj中进行查询
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
				Log.getInstance(this.getClass()).debug("####Error item key 应该是fb_xxxxxx:item.getKey():"+item.getKey());
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

//	插入成功 返回true
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

//					//刷新bddatavo HashMap缓存
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
			key = key.replaceFirst("zb_", "zb.");//第一个_
		}
		if(key.indexOf("fb_")!=-1){
			key = key.replaceFirst("fb_", "fb.");//第一个_
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
	 * 功能：设置帐页格式
	 *  作者：宋涛
	 *  创建时间：(2001-6-25 12:52:12)
	 *  参数：int iType 格式代码 0——金额式、
 	1——外币金额式、2——数量金额式、3——数量外币式
	 *  返回值：
	 *  算法：
	 *  异常描述：
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
	 * 功能：设置按钮的可用性
	 * 作者：宋涛
	 * 创建时间：(2001-9-25 13:50:06)
	 * 参数：<|>
	 * 返回值：
	 * 算法：
	 * 异常描述：
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
	 * 功能：设置表头信息中的单位信息,以及账页格式信息
	 * 作者:宋涛
	 * 创建时间:(2002-6-27 16:53:03)
	 * 参数:<|>
	 * 返回值:
	 * 算法:
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
	 * 功能：设置常用条件
	 * 作者：宋涛
	 * 创建时间：(2001-9-21 10:51:05)
	 * 参数：<|>
	 * 返回值：
	 * 算法：
	 * 异常描述：
	 */
//	@SuppressWarnings("unchecked")
//	private void setCustomCond() {
//		Vector vCustomConds = new Vector();
////		vCustomConds.addElement(getQryDlg().getConditionVO());
//		m_voConds.setVetCustCond(vCustomConds);//自定义条件
//	}
	/**
	 * 功能：设置查询条件
	 * 作者：宋涛
	 * 创建时间：(2001-8-23 15:47:03)
	 * 参数：<|>
	 * 返回值：
	 * 算法：
	 * 异常描述：
	 */
	@SuppressWarnings("restriction")
	private void setQueryCond() throws BusinessException{
		/**[iPoint,iModel,点余额or最终余额，iWLDX往来对象]*/
		int[] iPMF = new int[4];
		/**应收、应付*/
		if ((m_iSysCode == iArFlag || m_iSysCode == iApFlag) && !m_bRQQJ) {
			iPMF[1] =
				getUINormalPane().getValueCbBoxIdx("model") * 10
				+ getUINormalPane().getValueCbBoxIdx("date");

		} else { /**往来*/
			iPMF[1] =
				getUINormalPane().getValueCbBoxIdx("model") * 10
				+ getUINormalPane().getValueCbBoxIdx("date")
				+ 1;
		}
		iPMF[0] = m_iSysCode;
		iPMF[2] = getUINormalPane().getValueCbBoxIdx("zzye");
		iPMF[3] = getWldxConds();

		int iAspect = getUINormalPane().getValueCbBoxIdx("aspect");

		boolean[] bFB = new boolean[] { true, true }; //辅币、数量

		String[] sDWBM = getUINormalPane().getCorpPKs();
		if (sDWBM == null) {
			sDWBM = new String[] { getClientEnvironment().getCorporation().getPk_corp()};
		}
		boolean[] bZt = getUINormalPane().getValueChkBox();
		String sZLFA = getUINormalPane().getValueRef("zlfa", constdata.TYPE_PK);

		if(!m_bRQQJ&&sZLFA==null)
			throw ExceptionHandler.createException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("2006","UPP2006-v55-010260")/*@res "按账龄分析必须选择账龄方案!"*/);

		//准备查询对象数据
		Vector<QryObjVO> vQryObj = getUINormalPane().getValueQryObj();
		//准备查询条件数据
		QryCondArrayVO[] voConds = null;
		voConds = getOtherConds();
		getUINormalPane().getValueCustCond();

		//需要传入查询条件
		m_vBase = new Vector<Object>();
		m_vBase.addElement(iPMF); //0
		m_vBase.addElement(new Boolean(m_bCurrentDetail)); //1
		m_vBase.addElement(new Integer(iAspect)); //2
		m_vBase.addElement(m_dateJZRQ.toString()); //3
		m_vBase.addElement(null); //4sBZ
		m_vBase.addElement(bFB); //5
		m_vBase.addElement(bZt); //6
		m_vBase.addElement(new Integer(0)); //7 应收/应付账龄分析

		int sel_djzt = getUINormalPane().getValueCbBoxIdx("djzt");
		int djzt = -10000;
		if(sel_djzt == 0){
			//全部
			djzt=-10000;
		}else if(sel_djzt == 1){
			//已保存
			djzt=1;
		}else if(sel_djzt == 2){
			//已审核
			djzt=2;
		}else if(sel_djzt ==3){
			//已生效
			djzt=10;
		}
		m_vBase.addElement(new Integer(djzt)); //8 单据状态

		m_vObj = new Vector<Object>();
		m_vObj.addElement(vQryObj); //0
		m_vObj.addElement(voConds); //1

		m_vOther = new Vector<Object>();
		m_vOther.addElement(sDWBM); //0
		if (m_bRQQJ){
			Object[][] dates = getDateDlg().getData();
			if (dates == null) throw new BusinessException( nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("20060601","UPT20060601-v55-000042")/*@res "请至少选择显示一个日期区间."*/);
			if (dates[0][1] == null) {
				dates[0][1] = "1900-01-01";
				//throw new BusinessException( nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("20060601","UPT20060601-v55-000042")/*@res "请至少选择显示一个日期区间."*/);
			}
			m_vOther.addElement(dates);

//			if(getDateDlg().getData()==null || getDateDlg().getData().length==0){
//			showErrorMessage( "请至少选择显示一个日期区间.");
//			return;
//			}
		}
		//1
		else
			m_vOther.addElement(sZLFA);
		//1

		m_voConds.setVQryObj(vQryObj);
		setShowType(NCLangRes.getInstance().getStrByID("common","UC000-0001155")/*@res "名称"*/);
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
	 * 功能：设置显示格式
	 *  作者：wyan
	 *  创建时间：(2003-9-24 13:09:12)
	 *  参数：String strType 金额式、外币金额式
	 *  返回值：
	 *  算法：
	 *  异常描述：
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
			setBillType(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("2006","UPP2006-v56-000047")/*@res "金额式"*/);
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