package nc.ui.gl.initcontrast;

import java.awt.Dimension;
import java.awt.event.ItemEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import nc.bd.accperiod.AccountCalendar;
import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.cmp.utils.ArrayUtil;
import nc.cmp.utils.StringUtil;
import nc.itf.ebank.IEbankUI;
import nc.itf.fi.pub.Currency;
import nc.itf.fts.pub.IAccountService;
import nc.itf.gl.contrast.ContrastServiceFactory;
import nc.itf.gl.contrast.IBankReceiptPrv;
import nc.itf.gl.contrast.IContrastAccountPrv;
import nc.itf.gl.contrast.IContrastPrv;
import nc.ui.bd.BDGLOrgBookAccessor;
import nc.ui.cmp.contrastcall.ContrastContext;
import nc.ui.fi.printdirect.FiPrintDirectProxy;
import nc.ui.gl.contrastbase.CalTableCellEditor;
import nc.ui.gl.contrastbase.CalTableCellRender;
import nc.ui.gl.contrastbase.CalTableModel;
import nc.ui.gl.contrastbase.CalTableSelectionEvent;
import nc.ui.gl.contrastbase.CalUITable;
import nc.ui.gl.contrastbase.ICalTableSelectionListener;
import nc.ui.gl.contrastpub.LinkComBox;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.beans.ValueChangedEvent;
import nc.vo.cmp.cb.BankQueryVO;
import nc.vo.cmp.cb.BankReceiptVO;
import nc.vo.fts.account.AccDetailQueryVO;
import nc.vo.fts.account.AccDetailVO;
import nc.vo.gl.contrast.AccountlinkVO;
import nc.vo.gl.contrast.Accperiod;
import nc.vo.gl.contrast.BankreceiptVO;
import nc.vo.gl.contrast.ContrastQueryVO;
import nc.vo.gl.contrast.CurrtypeVO;
import nc.vo.gl.contrast.GLQueryVO;
import nc.vo.gl.contrast.GlContrastVO;
import nc.vo.gl.contrast.PrintVO;
import nc.vo.gl.contrastpub.ColumnName;
import nc.vo.logging.Debug;
import nc.vo.ml.NCLangRes4VoTransl;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;

/**
 * 银行对账单
 * 创建日期：(2002-1-22 11:46:01)
 * @author：张乾
 */
@SuppressWarnings({"unchecked","serial","deprecation"})
public class BankReceiptClientUI extends nc.ui.pub.ToftPanel implements ICalTableSelectionListener,
		nc.ui.pub.beans.ValueChangedListener, java.awt.event.KeyListener, java.awt.event.ItemListener {
	/////////////////////////////////////////////////////////////
	//设置按钮

	///
	//Ctrl+Ins				插入一行记录
	//Ctrl+Del				删除一行记录
	//Ctrl+S				保存
	//Ctrl+E               修改
	//Ctrl+D				删除
	//Ctrl+F				查询
	//Ctrl+P				打印
	//Ctrl+R				刷新
	private ButtonObject m_bButtonQuery = new ButtonObject(nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801",
			"UPP2004365801-000057")/*@res "查询"*/, nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801",
			"UPP2004365801-000057")/*@res "查询"*/, 2, "查询"); /*-=notranslate=-*/
	private ButtonObject m_bButtonAppend = new ButtonObject(nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801",
			"UPP2004365801-000215")/*@res "增加"*/, nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801",
			"UPP2004365801-000215")/*@res "增加"*/, 2, "增加"); /*-=notranslate=-*/
	private ButtonObject m_bButtonEdit = new ButtonObject(nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801",
			"UPP2004365801-000216")/*@res "修改"*/, nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801",
			"UPP2004365801-000216")/*@res "修改"*/, 2, "修改"); /*-=notranslate=-*/
	private ButtonObject m_bButtonSave = new ButtonObject(nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801",
			"UPP2004365801-000053")/*@res "保存"*/, nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801",
			"UPP2004365801-000053")/*@res "保存"*/, 2, "保存"); /*-=notranslate=-*/
	private ButtonObject m_bButtonCancel = new ButtonObject(nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801",
			"UPP2004365801-000021")/*@res "取消"*/, nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801",
			"UPP2004365801-000021")/*@res "取消"*/, 2, "取消"); /*-=notranslate=-*/
	private ButtonObject m_bButtonDelete = new ButtonObject(nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801",
			"UPP2004365801-000217")/*@res "删除"*/, nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801",
			"UPP2004365801-000217")/*@res "删除"*/, 2, "删除"); /*-=notranslate=-*/
	private ButtonObject m_bButtonDeleteAll = new ButtonObject(nc.ui.ml.NCLangRes.getInstance().getStrByID(
			"2004365801", "UPP2004365801-000218")/*@res "批量删除"*/, nc.ui.ml.NCLangRes.getInstance().getStrByID(
			"2004365801", "UPP2004365801-000218")/*@res "批量删除"*/, 2, "批量删除"); /*-=notranslate=-*/
	private ButtonObject m_bButtonInput = new ButtonObject(NCLangRes4VoTransl.getNCLangRes().getStrByID("contrast", "UPPcontrast-000098")/*"从资金组织引入"*/,
			NCLangRes4VoTransl.getNCLangRes().getStrByID("contrast", "UPPcontrast-000098")/*"从资金组织引入"*/, 2, "从资金组织引入"); /*-=notranslate=-*/
	private ButtonObject m_bButtonPrint = new ButtonObject(nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801",
			"UPP2004365801-000220")/*@res "打印"*/, nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801",
			"UPP2004365801-000220")/*@res "打印"*/, 2, "打印"); /*-=notranslate=-*/
	private ButtonObject m_bButtonDirectPrint = new ButtonObject(nc.ui.ml.NCLangRes.getInstance().getStrByID("contrast",
	"UPPcontrast-000130")/*@res"直接打印"*/,nc.ui.ml.NCLangRes.getInstance().getStrByID("contrast",
	"UPPcontrast-000130")/*@res"直接打印"*/,2,"直接打印");
	private ButtonObject m_bButtonTemplatePrint = new ButtonObject(nc.ui.ml.NCLangRes.getInstance().getStrByID("contrast",
	"UPPcontrast-000131")/*@res"模板打印"*/,nc.ui.ml.NCLangRes.getInstance().getStrByID("contrast",
	"UPPcontrast-000131")/*@res"模板打印"*/,2,"模板打印");
	private ButtonObject m_bButtonRefresh = new ButtonObject(nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801",
	"UPP2004365801-000266")/* @res "刷新" */, nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801",
	"UPP2004365801-000267")/* @res "刷新数据" */, 2, "刷新"); /*-=notranslate=-*/
	//add by zhaozh
	private ButtonObject m_bButtonImport = new ButtonObject(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("2004","UPP2004-000228")/*@res "导入"*/, nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("2004","UPP2004-000229")/*@res "导入EXCEL表"*/, 2, "导入");	/*-=notranslate=-*/
	private ButtonObject m_bButtonExport = new ButtonObject(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("2004","UPP2004-000230")/*@res "导出模板"*/, nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("2004","UPP2004-000231")/*@res "导出银行对帐单EXCEL格式"*/, 2, "导出对帐单格式");	/*-=notranslate=-*/
	private ButtonObject m_bFileImprot = new ButtonObject(NCLangRes4VoTransl.getNCLangRes().getStrByID("contrast", "UPPcontrast-000097")/*"文件导入"*/,
			NCLangRes4VoTransl.getNCLangRes().getStrByID("contrast", "UPPcontrast-000097")/*"文件导入"*/,2,nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("2004","UPP2004-000233")/*@res "文件导入"*/);
//	private ButtonObject[] m_aryButtonGroup = { m_bButtonQuery, m_bButtonAppend, m_bButtonEdit, m_bButtonDelete,
//			m_bButtonSave, m_bButtonCancel, m_bButtonInput, m_bButtonPrint, m_bButtonImport,//增加导入按钮
//			m_bButtonExport //增加导出按钮（新需求）
//	};
//	private ButtonObject[] m_aryButtonGroup = { m_bButtonQuery, m_bButtonAppend, m_bButtonEdit, m_bButtonDelete,
//			m_bButtonSave, m_bButtonCancel, m_bButtonInput, m_bButtonPrint, m_bButtonImport,//增加导入按钮
//	m_bButtonExport,m_bFileImprot
//	};
	private ButtonObject[] m_aryButtonGroup = { m_bButtonQuery,m_bButtonAppend, m_bButtonEdit, m_bButtonDelete,m_bButtonSave,m_bButtonCancel,m_bFileImprot,m_bButtonInput,m_bButtonPrint,m_bButtonRefresh};
//	private String m_sCorpID = "0001"; //单位编码 默认为0001
	private String m_sTitle = nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801", "UPP2004365801-000045")/*@res "银行对账单"*/; //设置标题
	//列标题数组

	private CalUITable m_tUITableBank = null; //表格
	private CalTableModel m_mTableModelBank = null; //表格模型

	private QueryUI query = null;
	private ImportFundOrgDlg input = null;

	private GlContrastVO contrast = null; //当前账户
	private Vector banks = new Vector(); //
	AccountlinkVO[] linkVO = null;

	private int status = 0; //状态 0 浏览 1 添加 2 编辑
	private int bankRow = 0; //银行当前编辑行

	private ContrastQueryVO cvo = null; //查询对象

	private UFDouble nowB = new UFDouble(0);
	private ClientEnvironment ce = ClientEnvironment.getInstance();

	private int precision = 2;
	private Accperiod period = null;

	//银行编辑参照
	private nc.ui.pub.beans.UIRefPane ivjUIRefPane1Bankdate = null;
	private nc.ui.pub.beans.UIRefPane ivjUIRefPane1Bankzy = null;
	private nc.ui.pub.beans.UIRefPane ivjUIRefPane1Bankjs = null;
	//private nc.ui.pub.beans.UIRefPane ivjUIRefPanelBankjsh = null;

	private nc.ui.pub.beans.UILabel ivjUILabel1 = null;
	private nc.ui.pub.beans.UILabel ivjUILabel2 = null;
	private nc.ui.pub.beans.UIPanel ivjUIPanelhead = null;
	private nc.ui.pub.beans.UILabel ivjUILabel3 = null;
	private nc.ui.pub.beans.UILabel ivjUILabel4 = null;
	private nc.ui.pub.beans.UIPanel ivjUIPanel1 = null;
	private nc.ui.pub.beans.UITablePane ivjUITablePane1 = null;
	private static IBankReceiptPrv bankService = null;

	int ifManyUnit = 1; //账户是否多单位对帐
	boolean ifoldAccount = true; //账户2.3以前账户
	private LinkComBox ivjLinkComBox1 = null;

	private ColumnName bankColumn = null;

	//记录新增加的行数
	private int addRows = 0;
	
	private ContrastContext context = ContrastContext.getInstance();
	
	/**
	 * BankReceiptClientUI 构造子注解。
	 */
	public BankReceiptClientUI() {
		super();
		initialize();
	}

	/**
	 * 函数功能:表格编辑触发
	 *
	 *  参数:
	 * 		MyNCTableSelectionEvent e ----- 事件

	 * 返回值:
	 *
	 * 异常:
	 *
	 */
	public void afterEdit(CalTableSelectionEvent e) {
		int iColumn = bankColumn.getCountByName(nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801",
				"UPP2004365801-000071")/*@res "摘要"*/);
		;
		if (iColumn == e.getColumn()) {
			m_mTableModelBank.setValueAt(getUIRefPane1Bankzy().getRefName() == null ? getUIRefPane1Bankzy()
					.getUITextField().getText() : getUIRefPane1Bankzy().getRefName(), e.getRow(), e.getColumn());
		}
		iColumn = bankColumn.getCountByName(nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801",
				"UPP2004365801-000075")/*@res "日期"*/);
		if (iColumn == e.getColumn()) {
			m_mTableModelBank.setValueAt(getUIRefPane1Bankdate().getRefName() == null ? getUIRefPane1Bankdate()
					.getUITextField().getText() : getUIRefPane1Bankdate().getRefName(), e.getRow(), e.getColumn());
		}
	}

	/**
	 * 	函数功能:表格变化触发触发
	 *
	 *  参数:
	 * 		MyNCTableSelectionEvent e ----- 事件

	 * 	返回值:
	 *
	 * 	异常:
	 *
	 */
	public void CalTableSelectionChanged(CalTableSelectionEvent e) {

	}

	/**
	 * 函数功能:
	 *
	 *  参数:
	 * 		MyNCTableSelectionEvent e ----- 事件

	 * 返回值:
	 *
	 * 异常:
	 *
	 */
	public boolean checkBank(BankreceiptVO vo) {
		UFDouble deb = null;
		UFDouble cre = null;
		if (vo.getDebitamount() == null) {
			deb = new UFDouble(0);
		} else {
			deb = vo.getDebitamount();
		}
		if (vo.getCreditamount() == null) {
			cre = new UFDouble(0);
		} else {
			cre = vo.getCreditamount();
		}
		if (deb.doubleValue() == 0 && cre.doubleValue() == 0) {
			MessageDialog.showWarningDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801",
					"UPP2004365801-000027")/*@res "提示"*/, nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801",
					"UPP2004365801-000221")/*@res "借方 贷方不能同时为空！"*/);
			return false;
		}
		if (deb.doubleValue() != 0 && cre.doubleValue() != 0) {
			MessageDialog.showWarningDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801",
					"UPP2004365801-000027")/*@res "提示"*/, nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801",
					"UPP2004365801-000222")/*@res "借方 贷方不能同时有数！"*/);
			return false;
		}
		///////////////////////
		//检查科目和公司的关系
		if (ifManyUnit == 2) {
			if (linkVO == null) {
				return false;
			}
			if (vo.getPk_corp() == null) {
				return true;
			}
			if (contrast.getSource().intValue() == 1) {
				String corp = vo.getPk_corp() == null ? "" : vo.getPk_corp();
				String sub = vo.getPk_subject() == null ? "" : vo.getPk_subject();
				int flag = 0;
				for (int i = 0; i < linkVO.length; i++) {
					String corp2 = linkVO[i].getPk_corp() == null ? "" : linkVO[i].getPk_corp();
					String sub2 = linkVO[i].getPk_subject() == null ? "" : linkVO[i].getPk_subject();
					if (corp.equals(corp2) && sub.equals(sub2)) {
						flag = 1;
						break;
					}
				}
				if (flag == 0) {
					MessageDialog.showWarningDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801",
							"UPP2004365801-000027")/*@res "提示"*/, nc.ui.ml.NCLangRes.getInstance().getStrByID(
							"2004365801", "UPP2004365801-000223")/*@res "单位，科目关系不符合账户设置！"*/);
					return false;
				}
			} else {
				String corp = vo.getPk_corp() == null ? "" : vo.getPk_corp();
				String bank = vo.getPk_bank() == null ? "" : vo.getPk_bank();
				int flag = 0;
				for (int i = 0; i < linkVO.length; i++) {
					String corp2 = linkVO[i].getPk_corp() == null ? "" : linkVO[i].getPk_corp();
					String bank2 = linkVO[i].getBankaccount() == null ? "" : linkVO[i].getBankaccount();
					if (corp.equals(corp2) && bank.equals(bank2)) {
						flag = 1;
						break;
					}
				}
				if (flag == 0) {
					MessageDialog.showWarningDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801",
							"UPP2004365801-000027")/*@res "提示"*/, nc.ui.ml.NCLangRes.getInstance().getStrByID(
							"2004365801", "UPP2004365801-000224")/*@res "单位，账户关系不符合账户设置！"*/);
					return false;
				}
			}
		}
		////////////////////
		return true;
	}

	/**
	 * 函数功能:
	 *
	 *  参数:
	 * 		MyNCTableSelectionEvent e ----- 事件

	 * 返回值:
	 *
	 * 异常:
	 *
	 */
	public BankreceiptVO getBankVO(boolean source, int row) {
		BankreceiptVO vo = new BankreceiptVO();
		///组织vo
		vo.setPk_contrastaccount(contrast.getPk_contrastaccount()); //账户主键
		int iColumnIndex = 0;
		if (!ifoldAccount) {
			AccountlinkVO linkTemp = null;
			//如果是多单位对账--v31
			if (this.ifManyUnit == 2) {
				iColumnIndex = m_tUITableBank
						.getColumnModel()
						.getColumnIndex(
								nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801", "UPP2004365801-000074")/*@res "子账户"*/);
				if (m_mTableModelBank.getValueAt(row, iColumnIndex) == null
						|| m_mTableModelBank.getValueAt(row, iColumnIndex).toString().length() < 1) {
					MessageDialog.showWarningDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801",
							"UPP2004365801-000027")/*@res "提示"*/, nc.ui.ml.NCLangRes.getInstance().getStrByID(
							"2004365801", "UPP2004365801-000225")/*@res "必须录入来源信息！"*/);
					return null;
				}
				linkTemp = getLinkComBox1().getSelectLinks(m_mTableModelBank.getValueAt(row, iColumnIndex).toString());
			} else {
				linkTemp = getLinkComBox1().getSelectLinks();
			}
			if (linkTemp == null) {
				MessageDialog.showWarningDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801",
						"UPP2004365801-000027")/*@res "提示"*/, nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"2004365801", "UPP2004365801-000225")/*@res "必须录入来源信息！"*/);
				return null;
			} else {
				vo.setPk_corp(linkTemp.getPk_corp());
				vo.setMemo(linkTemp.getPk_accountlink());
				vo.setPk_subject(linkTemp.getPk_subject());
				vo.setPk_bank(linkTemp.getBankaccount());
				vo.setPk_ass(linkTemp.getPk_ass());
				vo.setM_unitname(linkTemp.getM_corp());

				//m_mTableModelBank.setValueAt(linkTemp.getM_corp(),row,bankColumn.getCountByName("单位"));
				if (contrast.getSource().intValue() == 1) {
					vo.setM_subject(linkTemp.getM_sub());
					//m_mTableModelBank.setValueAt(linkTemp.getM_sub(),row,bankColumn.getCountByName("科目"));
				} else {
					vo.setM_bank(linkTemp.getM_bank());
					//m_mTableModelBank.setValueAt(linkTemp.getM_bank(),row,bankColumn.getCountByName("科目"));
				}
			}
		}
		/*if(ifManyUnit == 1){
		 if(linkVO != null && linkVO.length == 1){
		 vo.setPk_corp(linkVO[0].getPk_corp());
		 if(contrast.getSource().intValue()==1){
		 vo.setPk_subject(linkVO[0].getPk_subject());   //科目
		 }else{
		 vo.setPk_bank(linkVO[0].getBankaccount());   //账户
		 }
		 }
		 }else{
		 iColumnIndex = m_tUITableBank.getColumnModel().getColumnIndex("单位");
		 if(getUIRefPaneUnit().getRefPK()==null || m_mTableModelBank.getValueAt(row,iColumnIndex)==null || m_mTableModelBank.getValueAt(row,iColumnIndex).toString().length()<1){
		 MessageDialog.showWarningDlg(this,"提示","必须录入单位！");
		 return null;
		 }else{
		 vo.setPk_corp(getUIRefPaneUnit().getRefPK());   //单位
		 vo.setM_unitname(getUIRefPaneUnit().getRefName());   //单位
		 }
		 if(contrast.getSource().intValue()==1){
		 iColumnIndex = m_tUITableBank.getColumnModel().getColumnIndex("科目");
		 if(getUIRefPaneSubject().getRefPK()==null || m_mTableModelBank.getValueAt(row,iColumnIndex)==null || m_mTableModelBank.getValueAt(row,iColumnIndex).toString().length()<1){
		 MessageDialog.showWarningDlg(this,"提示","必须录入科目！");
		 return null;
		 }else{
		 vo.setPk_subject(getUIRefPaneSubject().getRefPK());   //科目
		 vo.setM_subject(getUIRefPaneSubject().getRefName());   //科目
		 }
		 }else{
		 iColumnIndex = m_tUITableBank.getColumnModel().getColumnIndex("账户");
		 if(getUIRefPaneBank().getRefPK()==null || m_mTableModelBank.getValueAt(row,iColumnIndex)==null || m_mTableModelBank.getValueAt(row,iColumnIndex).toString().length()<1){
		 MessageDialog.showWarningDlg(this,"提示","必须录入账户！");
		 return null;
		 }else{
		 vo.setPk_bank(getUIRefPaneBank().getRefPK());   //账户
		 vo.setM_bank(getUIRefPaneBank().getRefName());   //账户
		 }
		 }
		 }*/
		iColumnIndex = m_tUITableBank.getColumnModel().getColumnIndex(
				nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801", "UPP2004365801-000075")/*@res "日期"*/);
		if (m_mTableModelBank.getValueAt(row, iColumnIndex) == null
				|| m_mTableModelBank.getValueAt(row, iColumnIndex).toString().length() < 1) {
			MessageDialog.showWarningDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801",
					"UPP2004365801-000027")/*@res "提示"*/, nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801",
					"UPP2004365801-000226")/*@res "必须录入日期！"*/);
			return null;
		} else {
			UFDate da = new UFDate(m_mTableModelBank.getValueAt(row, iColumnIndex).toString());
			if (da.before(contrast.getStartdate())) {
				MessageDialog.showWarningDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801",
						"UPP2004365801-000027")/*@res "提示"*/, nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"2004365801", "UPP2004365801-000227")/*@res "数据日期应大于启用日期！"*/);
				return null;
			}
			/*****v31***/
			nc.vo.bd.period.AccperiodVO accperiodvo = null;
			if (contrast.getSource().intValue() == 1) {

				String pkglorgbook = null;
				try {
					//v50--adjust--for-xyj--20051011
					//if(contrast.getMemo()!=null && contrast.getMemo().equals("one") || linkVO.length<=1){
					/**本单位对账时contrast.getPk_corp()取的是主体账簿主键，若多单位对账且仅关联一个主体账簿科目时，
					 * 会进入该分支，且contrast.getPk_corp()取的是登录公司的公司主键，导致取会计期间错误
					 * modified by jh 2007-12-6
					 */
					if (contrast.getMemo() != null && contrast.getMemo().equals("one")) {
						pkglorgbook = contrast.getPk_corp();
					}
					//else if(contrast.getMemo()!=null && contrast.getMemo().equals("many") || linkVO.length>1){
					else if (contrast.getMemo() != null && contrast.getMemo().equals("many")) {
						AccountlinkVO linkVO = getLinkComBox1().getSelectLinks();
						pkglorgbook = linkVO.getPk_corp();
					}
					//						accperiodvo=AccperiodBO_Client.queryAccyearByDate(pkglorgbook,da);
					AccountCalendar calendar = AccountCalendar.getInstanceByGlorgbook(pkglorgbook);
					calendar.setDate(da);
					accperiodvo = calendar.getYearVO();
					//仅仅根据年份判断不准，尤其是对账帐户对应主体账簿为非自然月会计期间时 07-10-17
					//if(accperiodvo==null || !accperiodvo.getPeriodyear().equals(ce.getAccountYear())){
					if (accperiodvo == null || da.before(accperiodvo.getBegindate())
							|| da.after(accperiodvo.getEnddate())) {
						MessageDialog.showWarningDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801",
								"UPP2004365801-000027")/*@res "提示"*/, nc.ui.ml.NCLangRes.getInstance().getStrByID(
								"2004365801", "UPP2004365801-000228")/*@res "日期必须在登陆会计期间内"*/);
						return null;
					}
				} catch (Exception e) {
					Logger.error(e.getMessage(), e);
					MessageDialog.showWarningDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801",
							"UPP2004365801-000027")/*@res "提示"*/, nc.ui.ml.NCLangRes.getInstance().getStrByID(
							"2004365801", "UPP2004365801-000228")/*@res "日期必须在登陆会计期间内"*/);
					return null;
				}
			}

			/*****end*/
			else {
				try {
					//						accperiodvo=AccperiodBO_Client.queryByYear(ce.getAccountYear());
					AccountCalendar calendar = AccountCalendar.getInstance();
					calendar.set(ce.getAccountYear());
					accperiodvo = calendar.getYearVO();

				} catch (Exception e) {
					MessageDialog.showWarningDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801",
							"UPP2004365801-000027")/*@res "提示"*/, nc.ui.ml.NCLangRes.getInstance().getStrByID(
							"2004365801", "UPP2004365801-000228")/*@res "日期必须在登陆会计期间内"*/);
					return null;
				}

				if (((da.after(accperiodvo.getBegindate()) && da.before(accperiodvo.getEnddate()))
						|| UFDate.getDaysBetween(da, accperiodvo.getBegindate()) == 0 || UFDate.getDaysBetween(da, accperiodvo
						.getEnddate()) == 0)
						&& accperiodvo.getPeriodyear().equals(ce.getAccountYear())) {

				}
				//					if(period.getPeriod(da) !=null && period.getPeriod(da).equals(ce.getAccountYear())){
				else {
					MessageDialog.showWarningDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801",
							"UPP2004365801-000027")/*@res "提示"*/, nc.ui.ml.NCLangRes.getInstance().getStrByID(
							"2004365801", "UPP2004365801-000228")/*@res "日期必须在登陆会计期间内"*/);
					return null;
				}
			}
			vo.setCheckdate(new UFDate(m_mTableModelBank.getValueAt(row, iColumnIndex).toString())); //日期
			vo.setYears(ce.getAccountYear());
		}

		iColumnIndex = m_tUITableBank.getColumnModel().getColumnIndex(
				nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801", "UPP2004365801-000071")/*@res "摘要"*/);
		if (m_mTableModelBank.getValueAt(row, iColumnIndex) == null
				|| m_mTableModelBank.getValueAt(row, iColumnIndex).toString().length() < 1) {
			//MessageDialog.showWarningDlg(this,"提示","必须录入摘要！");
			//return null;
		} else {
			vo.setExplanation(m_mTableModelBank.getValueAt(row, iColumnIndex).toString()); //摘要
		}

		iColumnIndex = m_tUITableBank.getColumnModel().getColumnIndex(
				nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801", "UPP2004365801-000072")/*@res "结算方式"*/);
		if (getUIRefPane1Bankjs().getRefPK() == null || m_mTableModelBank.getValueAt(row, iColumnIndex) == null
				|| m_mTableModelBank.getValueAt(row, iColumnIndex).toString().length() < 1) {
			if (m_mTableModelBank.getValueAt(row, iColumnIndex) != null) {
				String key = "";

				key = m_mTableModelBank.getValueAt(
						row,
						bankColumn.getCountByName(nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801",
								"UPP2004365801-000138")/*@res "主键"*/)).toString();

				BankreceiptVO bvo = getVO(key);
				vo.setCheckstyle(bvo.getCheckstyle());
				vo.setM_jsfsh(bvo.getM_jsfsh());
			}
			//MessageDialog.showWarningDlg(this,"提示","必须录入结算方式！");
			//return null;
		} else {
			vo.setCheckstyle(getUIRefPane1Bankjs().getRefPK()); //结算类型
			vo.setM_jsfsh(getUIRefPane1Bankjs().getRefName()); //结算类型
		}

		iColumnIndex = m_tUITableBank.getColumnModel().getColumnIndex(
				nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801", "UPP2004365801-000073")/*@res "结算号"*/);
		if (m_mTableModelBank.getValueAt(row, iColumnIndex) == null
				|| m_mTableModelBank.getValueAt(row, iColumnIndex).toString().length() < 1) {
			//MessageDialog.showWarningDlg(this,"提示","必须录入结算号！");
			//return null;
		} else {
			vo.setPk_check(m_mTableModelBank.getValueAt(row, iColumnIndex).toString()); //结算号
			if (vo.getCheckstyle() == null) {
				showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801", "UPP2004365801-000229")/*@res "缺少结算方式！"*/);
				//MessageDialog.showWarningDlg(this,"提示","缺少结算方式！");
				//return null;
			}
		}

		iColumnIndex = m_tUITableBank.getColumnModel().getColumnIndex(
				nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801", "UPP2004365801-000209")/*@res "借方"*/);
		if (m_mTableModelBank.getValueAt(row, iColumnIndex) == null
				|| m_mTableModelBank.getValueAt(row, iColumnIndex).toString().length() < 1) {
			vo.setDebitamount(new UFDouble(0));
			if (m_mTableModelBank.getValueAt(row, iColumnIndex + 1) == null
					|| m_mTableModelBank.getValueAt(row, iColumnIndex + 1).toString().length() < 1) {
				MessageDialog.showWarningDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801",
						"UPP2004365801-000027")/*@res "提示"*/, nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"2004365801", "UPP2004365801-000230")/*@res "必须录入借方或贷方！"*/);
				return null;
			}
		} else {
			vo.setDebitamount(new UFDouble(m_mTableModelBank.getValueAt(row, iColumnIndex).toString()));
		}

		iColumnIndex = m_tUITableBank.getColumnModel().getColumnIndex(
				nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801", "UPP2004365801-000210")/*@res "贷方"*/);
		if (m_mTableModelBank.getValueAt(row, iColumnIndex) == null
				|| m_mTableModelBank.getValueAt(row, iColumnIndex).toString().length() < 1) {
			vo.setCreditamount(new UFDouble(0));
		} else {
			vo.setCreditamount(new UFDouble(m_mTableModelBank.getValueAt(row, iColumnIndex).toString()));
		}
		return vo;
	}

	/**
	 * 返回 LinkComBox1 特性值。
	 * @return nc.ui.gl.contrastpub.LinkComBox
	 */
	/* 警告：此方法将重新生成。 */
	private LinkComBox getLinkComBox1() {
		if (ivjLinkComBox1 == null) {
			try {
				ivjLinkComBox1 = new nc.ui.gl.contrastpub.LinkComBox();
				ivjLinkComBox1.setName("LinkComBox1");
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjLinkComBox1;
	}

	/**
	 * 得到实际vo
	 */
	public int getPosition(String key) {
		int count = banks.size();
		for (int i = 0; i < count; i++) {
			if (((BankreceiptVO) banks.get(i)).getPk_bankreceipt().equals(key)) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * 得到精度
	 */
	public int getPrecision(String columnName) {
		return precision;
	}

	/**
	 * getTitle 方法注解。
	 */
	public String getTitle() {
		return m_sTitle;
	}

	/**
	 * 返回 UILabel1 特性值。
	 * @return nc.ui.pub.beans.UILabel
	 */
	/* 警告：此方法将重新生成。 */
	private nc.ui.pub.beans.UILabel getUILabel1() {
		if (ivjUILabel1 == null) {
			try {
				ivjUILabel1 = new nc.ui.pub.beans.UILabel();
				ivjUILabel1.setName("UILabel1");
				ivjUILabel1
						.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801", "UPP2004365801-000231")/*@res "启动日期：2002年01月22日"*/);
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjUILabel1;
	}

	/**
	 * 返回 UILabel2 特性值。
	 * @return nc.ui.pub.beans.UILabel
	 */
	/* 警告：此方法将重新生成。 */
	private nc.ui.pub.beans.UILabel getUILabel2() {
		if (ivjUILabel2 == null) {
			try {
				ivjUILabel2 = new nc.ui.pub.beans.UILabel();
				ivjUILabel2.setName("UILabel2");
				ivjUILabel2
						.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801", "UPP2004365801-000232")/*@res "    期初余额"*/);
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjUILabel2;
	}

	/**
	 * 返回 UILabel3 特性值。
	 * @return nc.ui.pub.beans.UILabel
	 */
	/* 警告：此方法将重新生成。 */
	private nc.ui.pub.beans.UILabel getUILabel3() {
		if (ivjUILabel3 == null) {
			try {
				ivjUILabel3 = new nc.ui.pub.beans.UILabel();
				ivjUILabel3.setName("UILabel3");
				ivjUILabel3
						.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801", "UPP2004365801-000233")/*@res "    账户：XXXXXXXXXX"*/);
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjUILabel3;
	}

	/**
	 * 返回 UILabel4 特性值。
	 * @return nc.ui.pub.beans.UILabel
	 */
	/* 警告：此方法将重新生成。 */
	private nc.ui.pub.beans.UILabel getUILabel4() {
		if (ivjUILabel4 == null) {
			try {
				ivjUILabel4 = new nc.ui.pub.beans.UILabel();
				ivjUILabel4.setName("UILabel4");
				ivjUILabel4
						.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801", "UPP2004365801-000234")/*@res "图例：已勾对  已核销"*/);
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjUILabel4;
	}

	/**
	 * 主面板
	 * 返回 UIPanel1 特性值。
	 * @return nc.ui.pub.beans.UIPanel
	 */
	/* 警告：此方法将重新生成。 */
	private nc.ui.pub.beans.UIPanel getUIPanel1() {
		if (ivjUIPanel1 == null) {
			try {
				ivjUIPanel1 = new nc.ui.pub.beans.UIPanel();
				ivjUIPanel1.setName("UIPanel1");
				ivjUIPanel1.setLayout(new java.awt.BorderLayout());
				getUIPanel1().add(getUIPanelhead(), "North");
				getUIPanel1().add(getUITablePane1(), "Center");
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjUIPanel1;
	}

	/**
	 * 返回 UIPanelhead 特性值。
	 * @return nc.ui.pub.beans.UIPanel
	 */
	/* 警告：此方法将重新生成。 */
	private nc.ui.pub.beans.UIPanel getUIPanelhead() {
		if (ivjUIPanelhead == null) {
			try {
				ivjUIPanelhead = new nc.ui.pub.beans.UIPanel();
				ivjUIPanelhead.setName("UIPanelhead");
				ivjUIPanelhead.setLayout(new java.awt.GridLayout(2, 2));
				getUIPanelhead().add(getUILabel2(), getUILabel2().getName());
				getUIPanelhead().add(getUILabel1(), getUILabel1().getName());
				getUIPanelhead().add(getUILabel3(), getUILabel3().getName());
				getUIPanelhead().add(getUILabel4(), getUILabel4().getName());
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjUIPanelhead;
	}

	/**
	 * 返回 UIRefPane11 特性值。
	 * @return nc.ui.pub.beans.UIRefPane
	 */
	/* 警告：此方法将重新生成。 */
	private nc.ui.pub.beans.UIRefPane getUIRefPane1Bankdate() {
		if (ivjUIRefPane1Bankdate == null) {
			try {
				ivjUIRefPane1Bankdate = new nc.ui.pub.beans.UIRefPane();
				ivjUIRefPane1Bankdate.setName("ivjUIRefPane1Bankdate ");
				//ivjUIRefPane1Bankdate.setLocation(88, 77);
				ivjUIRefPane1Bankdate.setRefNodeName("日历");
				ivjUIRefPane1Bankdate.setDelStr("\' \'");
				// user code begin {1}
				ivjUIRefPane1Bankdate.setVisible(true);
				ivjUIRefPane1Bankdate.addValueChangedListener(this);
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjUIRefPane1Bankdate;
	}

	/**
	 * 返回 UIRefPane11 特性值。
	 * @return nc.ui.pub.beans.UIRefPane
	 */
	/* 警告：此方法将重新生成。 */
	private nc.ui.pub.beans.UIRefPane getUIRefPane1Bankjs() {
		if (ivjUIRefPane1Bankjs == null) {
			try {
				ivjUIRefPane1Bankjs = new nc.ui.pub.beans.UIRefPane();
				ivjUIRefPane1Bankjs.setName("ivjUIRefPane1Bankjs");
				//ivjUIRefPane1Bankdate.setLocation(88, 77);
				ivjUIRefPane1Bankjs.setRefNodeName("结算方式");
				ivjUIRefPane1Bankjs.setDelStr("\' \'");
				// user code begin {1}
				ivjUIRefPane1Bankjs.setVisible(true);
				ivjUIRefPane1Bankjs.setReturnCode(false);
				ivjUIRefPane1Bankjs.addValueChangedListener(this);
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjUIRefPane1Bankjs;
	}

	/**
	 * 返回 UIRefPane11 特性值。
	 * @return nc.ui.pub.beans.UIRefPane
	 */
	/* 警告：此方法将重新生成。 */
	private nc.ui.pub.beans.UIRefPane getUIRefPane1Bankzy() {
		if (ivjUIRefPane1Bankzy == null) {
			try {
				ivjUIRefPane1Bankzy = new nc.ui.pub.beans.UIRefPane();
				ivjUIRefPane1Bankzy.setName("ivjUIRefPane1Bankzy");
				//ivjUIRefPane1Bankdate.setLocation(88, 77);
				ivjUIRefPane1Bankzy.setRefNodeName("常用摘要");

				// user code begin {1}
				ivjUIRefPane1Bankzy.setAutoCheck(false);
				ivjUIRefPane1Bankzy.addValueChangedListener(this);

				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjUIRefPane1Bankzy;
	}

	/**
	 * 返回 UITablePane1 特性值。
	 * @return nc.ui.pub.beans.UITablePane
	 */
	/* 警告：此方法将重新生成。 */
	private nc.ui.pub.beans.UITablePane getUITablePane1() {
		if (ivjUITablePane1 == null) {
			try {
				ivjUITablePane1 = new nc.ui.pub.beans.UITablePane();
				ivjUITablePane1.setName("UITablePane1");
				// user code begin {1}
				m_tUITableBank = new CalUITable();
				ivjUITablePane1.setTable(m_tUITableBank);
				m_mTableModelBank = new CalTableModel();
				m_tUITableBank.setModel(m_mTableModelBank);
				m_tUITableBank.setSelectionMode(0);

				//防止出现虚线
				//m_tUITableBank.getColumnModel().setColumnMargin(0);
				//m_tUITableBank.setRowMargin(0);
				//m_tUITableBank.setShowGrid(true);
				//
				//MyTableUI ui = new MyTableUI();
				//m_tUITableBank.setUI(ui);
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjUITablePane1;
	}

	/**
	 * 得到实际vo
	 */
	public BankreceiptVO getVO(String key) {
		int count = banks.size();
		for (int i = 0; i < count; i++) {
			if (((BankreceiptVO) banks.get(i)).getPk_bankreceipt().equals(key)) {
				return (BankreceiptVO) banks.get(i);
			}
		}
		return null;
	}

	/**
	 * 每当部件抛出异常时被调用
	 * @param exception java.lang.Throwable
	 */
	private void handleException(java.lang.Throwable exception) {
		Logger.error(exception.getMessage());
		MessageDialog.showErrorDlg(this, null, exception.getMessage());
	}

	/**
	 * 初始化类。
	 */
	/* 警告：此方法将重新生成。 */
	private void initialize() {
		try {
			// user code begin {1}
			m_bButtonPrint.addChildButton(m_bButtonDirectPrint);
			m_bButtonPrint.addChildButton(m_bButtonTemplatePrint);
			m_bButtonAppend.setEnabled(false);
			m_bButtonCancel.setEnabled(false);
			m_bButtonDelete.setEnabled(false);
			m_bButtonDeleteAll.setEnabled(false);
			m_bButtonEdit.setEnabled(false);
			m_bButtonSave.setEnabled(false);
			m_bButtonInput.setEnabled(false);
			m_bButtonImport.setEnabled(false);
//			m_bFileImprot.setEnabled(false);
			m_bButtonRefresh.setEnabled(false);
			setButtons(m_aryButtonGroup);
//			if (isJSZX(ce.getCorporation().getPk_corp())) {
//				m_bButtonInput.setVisible(false);
//			}
			// user code end
			setName("BankReceiptClientUI");
			setLayout(new java.awt.CardLayout());
			setSize(774, 419);
			add(getUIPanel1(), getUIPanel1().getName());
			context.callRemoteService();
		} catch (java.lang.Throwable ivjExc) {
			handleException(ivjExc);
		}
		// user code begin {2}
		try {
			if (ContrastContext.getInstance().getSimple("period") == null) {
				period = new Accperiod();
				ContrastContext.getInstance().putSimple("period", new Accperiod());
			}
			period = (Accperiod) ContrastContext.getInstance().getSimple("period");
			String[][] col2 = new String[][] {
					{
							nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801", "UPP2004365801-000075")/*@res "日期"*/,
							nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801", "UPP2004365801-000071")/*@res "摘要"*/,
							nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801", "UPP2004365801-000072")/*@res "结算方式"*/,
							nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801", "UPP2004365801-000073")/*@res "结算号"*/,
							nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801", "UPP2004365801-000209")/*@res "借方"*/,
							nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801", "UPP2004365801-000210")/*@res "贷方"*/,
							nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801", "UPP2004365801-000213")/*@res "余额"*/,
							nc.ui.ml.NCLangRes.getInstance().getStrByID("contrast", "UPPcontrast-000116")/*"标识码"*/,
							nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801", "UPP2004365801-000138")/*@res "主键"*/,
							nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801", "UPP2004365801-000062")/*@res "两清"*/,
							nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801", "UPP2004365801-000157")/*@res "单位"*/,
							nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801", "UPP2004365801-000172") /*@res "科目"*/},
					{
							nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801", "UPP2004365801-000074")/*@res "子账户"*/,
							nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801", "UPP2004365801-000075")/*@res "日期"*/,
							nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801", "UPP2004365801-000071")/*@res "摘要"*/,
							nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801", "UPP2004365801-000072")/*@res "结算方式"*/,
							nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801", "UPP2004365801-000073")/*@res "结算号"*/,
							nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801", "UPP2004365801-000209")/*@res "借方"*/,
							nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801", "UPP2004365801-000210")/*@res "贷方"*/,
							nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801", "UPP2004365801-000213")/*@res "余额"*/,
							nc.ui.ml.NCLangRes.getInstance().getStrByID("contrast", "UPPcontrast-000116")/*"标识码"*/,
							nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801", "UPP2004365801-000138")/*@res "主键"*/,
							nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801", "UPP2004365801-000062")/*@res "两清"*/,
							nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801", "UPP2004365801-000157")/*@res "单位"*/,
							nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801", "UPP2004365801-000172") /*@res "科目"*/} };
			bankColumn = new ColumnName(col2);

			//将数据设置到表格中
			initTable();
			//		query = new QueryUI();
			//增加选择变化的监听
			m_tUITableBank.addKeyListener(this);

			m_tUITableBank.addCalTableSelectionListener(this);
			getLinkComBox1().addItemListener(this);
		} catch (Throwable e) {
			Logger.error(e.getMessage(), e);
			MessageDialog.showErrorDlg(this, null, e.getMessage());
		}
		// user code end
	}

	/**
	 * 	函数功能:初始化表格数据
	 *	控制银行对账单录入借贷金额为19位
	 */
	private void initTable() throws Throwable {

		//构建表头
		if (ifManyUnit == 1) {
			bankColumn.setCurrentColumn(0);
		} else {
			bankColumn.setCurrentColumn(1);
		}

		//设置表格数据
		m_mTableModelBank.setDataVector(new Vector(), bankColumn.getColumnVector());
		////
		m_tUITableBank.setSelectionMode(1); //设置只能选择一行
		m_tUITableBank.setAutoResizeMode(0);
		////////////////////////////////
		CalTableCellEditor cell1 = new CalTableCellEditor(getUIRefPane1Bankdate());
		cell1.setClickCountToStart(1);
		m_tUITableBank.getColumnModel().getColumn(
				bankColumn.getCountByName(nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801",
						"UPP2004365801-000075")/*@res "日期"*/)).setCellEditor(cell1);

		CalTableCellEditor cell2 = new CalTableCellEditor(getUIRefPane1Bankjs());
		cell2.setClickCountToStart(1);

		m_tUITableBank.getColumnModel().getColumn(
				bankColumn.getCountByName(nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801",
						"UPP2004365801-000072")/*@res "结算方式"*/)).setCellEditor(cell2);

		CalTableCellEditor cell3 = new CalTableCellEditor(getUIRefPane1Bankzy());
		cell3.setClickCountToStart(1);
		m_tUITableBank.getColumnModel().getColumn(
				bankColumn.getCountByName(nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801",
						"UPP2004365801-000071")/*@res "摘要"*/)).setCellEditor(cell3);

		CalTableCellRender tc = new CalTableCellRender();
		tc.setAlignMode(CalTableCellRender.ALIGN_RIGHT);
		CalTableCellRender tl = new CalTableCellRender();
		tl.setAlignMode(CalTableCellRender.ALIGN_LEFT);
		nc.ui.pub.beans.UITextField t0 = new nc.ui.pub.beans.UITextField();
		t0.setTextType(nc.ui.pub.beans.textfield.UITextType.TextStr);

		nc.ui.pub.beans.UITextField t = new nc.ui.pub.beans.UITextField();
		t.setTextType(nc.ui.pub.beans.textfield.UITextType.TextDbl);
		t.setMaxLength(19);
		t.setNumPoint(getPrecision(""));
		t.setDelStr("");

		CalTableCellEditor cell4 = new CalTableCellEditor(t0);
		CalTableCellEditor cell5 = new CalTableCellEditor(t);
		CalTableCellEditor cell6 = new CalTableCellEditor(t);
		CalTableCellEditor cell7 = new CalTableCellEditor(t);
		cell4.setClickCountToStart(1);
		cell5.setClickCountToStart(1);
		cell6.setClickCountToStart(1);
		cell7.setClickCountToStart(1);

		m_tUITableBank.getColumnModel().getColumn(
				bankColumn.getCountByName(nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801",
						"UPP2004365801-000073")/*@res "结算号"*/)).setCellEditor(cell4);
		m_tUITableBank.getColumnModel().getColumn(
				bankColumn.getCountByName(nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801",
						"UPP2004365801-000073")/*@res "结算号"*/)).setCellRenderer(tl);

		m_tUITableBank.getColumnModel().getColumn(
				bankColumn.getCountByName(nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801",
						"UPP2004365801-000209")/*@res "借方"*/)).setCellEditor(cell5);
		m_tUITableBank.getColumnModel().getColumn(
				bankColumn.getCountByName(nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801",
						"UPP2004365801-000209")/*@res "借方"*/)).setCellRenderer(tc);

		m_tUITableBank.getColumnModel().getColumn(
				bankColumn.getCountByName(nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801",
						"UPP2004365801-000210")/*@res "贷方"*/)).setCellEditor(cell6);
		m_tUITableBank.getColumnModel().getColumn(
				bankColumn.getCountByName(nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801",
						"UPP2004365801-000210")/*@res "贷方"*/)).setCellRenderer(tc);

		m_tUITableBank.getColumnModel().getColumn(
				bankColumn.getCountByName(nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801",
						"UPP2004365801-000213")/*@res "余额"*/)).setCellEditor(cell7);
		m_tUITableBank.getColumnModel().getColumn(
				bankColumn.getCountByName(nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801",
						"UPP2004365801-000213")/*@res "余额"*/)).setCellRenderer(tc);

		//////////////////
		CalTableCellEditor cell11 = new CalTableCellEditor(getLinkComBox1());
		cell11.setClickCountToStart(1);

		if (ifManyUnit == 2) {
			m_tUITableBank.getColumnModel().getColumn(
					bankColumn.getCountByName(nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801",
							"UPP2004365801-000074")/*@res "子账户"*/)).setCellEditor(cell11);
		}
		//隐藏列

		m_tUITableBank.removeColumn(m_tUITableBank.getColumnModel().getColumn(
				bankColumn.getCountByName(nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801",
						"UPP2004365801-000172")/*@res "科目"*/)));
		m_tUITableBank.removeColumn(m_tUITableBank.getColumnModel().getColumn(
				bankColumn.getCountByName(nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801",
						"UPP2004365801-000157")/*@res "单位"*/)));
		m_tUITableBank.removeColumn(m_tUITableBank.getColumnModel().getColumn(
				bankColumn.getCountByName(nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801",
						"UPP2004365801-000062")/*@res "两清"*/)));
		m_tUITableBank.removeColumn(m_tUITableBank.getColumnModel().getColumn(
				bankColumn.getCountByName(nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801",
						"UPP2004365801-000138")/*@res "主键"*/)));

		if (ifManyUnit == 1 || linkVO.length == 1 || cvo.getM_pk_link() != null) {

		} else {
			m_tUITableBank.removeColumn(m_tUITableBank.getColumnModel().getColumn(
					bankColumn.getCountByName(nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801",
							"UPP2004365801-000213")/*@res "余额"*/)));

		}
	}

	/**
	 * 将新添加vo加入队列
	 */
	public void insertVO(BankreceiptVO vo) {
		if (vo != null) {
			int count = banks.size();
			BankreceiptVO vt = null;
			for (int i = 0; i < count; i++) {
				vt = (BankreceiptVO) banks.get(i);
				if (vo.getCheckdate().before(vt.getCheckdate())) {
					banks.add(i, vo);
					return;
				}
			}
			banks.add(vo);
		}
	}

	/**
	 * 函数功能:
	 *
	 *  参数:
	 * 		MyNCTableSelectionEvent e ----- 事件

	 * 返回值:
	 *
	 * 异常:
	 *
	 */
	public boolean isChangeBank(BankreceiptVO newVO, BankreceiptVO oldVO) {

		return true;
	}

	/**
	 * 此处插入方法描述。
	 * 创建日期：(2004-4-13 11:11:22)
	 * 功能：返回当前公司是否为结算中心
	 * version:V30
	 */
	@SuppressWarnings("unused")
	private boolean isJSZX(String pk_corp) {
		try {
			return nc.ui.bd.CorpBO_Client.findByPrimaryKey(pk_corp).isSettleCenter();
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
			MessageDialog.showErrorDlg(this, null, e.getMessage());
			return false;
		}
	}

	/**
	 * Invoked when an item has been selected or deselected.
	 * The code written for this method performs the operations
	 * that need to occur when an item is selected (or deselected).
	 */
	public void itemStateChanged(ItemEvent e) {
		if (e.getSource() == this.getLinkComBox1()) {
			if (e.getStateChange() == ItemEvent.DESELECTED) {

			} else {
				if (status == 1 || status == 2) {
					if (getLinkComBox1().getSelectLinks() != null) {
						//					setRefWhereParts(getLinkComBox1().getSelectLinks().getPk_corp());
						if (contrast.getSource().intValue() == 1) {
							//根据主体主体账簿返回对应的公司
							String pkCorp = BDGLOrgBookAccessor.getPk_corp(getLinkComBox1().getSelectLinks()
									.getPk_corp());
							setRefWhereParts(pkCorp);
						} else {
							setRefWhereParts(getLinkComBox1().getSelectLinks().getPk_corp());
						}
					}
					//m_mTableModelBank.setValueAt(null,iSelectRow,iColumnIndex);
				}
			}
		}

	}

	/**
	 * Invoked when a key has been pressed.
	 */
	public void keyPressed(java.awt.event.KeyEvent e) {
		//	if(!this.contrast.getMemo().equals("one")) return ;

		int key = e.getKeyCode();

		//键盘操作
		if (key == java.awt.event.KeyEvent.VK_N && e.isControlDown()) {
			//添加
			if (status == 0)
				onButtonAppend();
		} else if (key == KeyEvent.VK_S && e.isControlDown()) {
			//保存
			if (status != 0)
				onButtonSave();
		} else if (key == KeyEvent.VK_D && e.isControlDown()) {
			//删除
			if (status == 0)
				onButtonDelete();
		} else if (key == KeyEvent.VK_C && e.isControlDown()) {
			//取消
			if (status != 0)
				onButtonCancel();
		} else if (key == KeyEvent.VK_E && e.isControlDown()) {
			//编辑
			if (status == 0)
				onButtonEdit();
		} else if (key == KeyEvent.VK_F3) {
			//查询
			if (status == 0)
				onButtonQuery();
		}
		//e.consume();

		e.consume();
		if (status == 2) {
			//获得回车键
			if (e.getKeyChar() == KeyEvent.VK_ENTER) {
				int iColumnIndex = m_tUITableBank.getColumnCount() - 1;
				if (m_tUITableBank.getSelectedColumn() == iColumnIndex
						&& m_tUITableBank.getSelectedRow() == m_tUITableBank.getRowCount() - 1) {
					if (onButtonSave()) {
						onButtonAppend();
						return;
					}
				}
			}
		} else if (status == 1) {
			//获得回车键
			if (e.getKeyChar() == KeyEvent.VK_ENTER) {
				int iColumnIndex = m_tUITableBank.getColumnCount() - 1;
				if (m_tUITableBank.getSelectedColumn() == iColumnIndex
						&& m_tUITableBank.getSelectedRow() == m_tUITableBank.getRowCount() - 1) {
					if (onButtonSave()) {
						onButtonAppend();
						return;
					}
				}
			}
		}
		if (e.getKeyChar() == KeyEvent.VK_ENTER) {
			//		int row = m_tUITableBank.getSelectedRow();
			//		int column = m_tUITableBank.getSelectedColumn();
			//		m_tUITableBank.setColumnSelectionInterval(column+1,column+1);
			//		m_tUITableBank.setRowSelectionInterval(row,row);
			//		//将选中行滚动到当前显示
			//		m_tUITableBank.scrollRectToVisible(m_tUITableBank.getCellRect(row,column+1,false));
			//		m_tUITableBank.editCellAt(row,column+1);
			//		if(column + 1 == bankColumn.getCountByName(nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801","UPP2004365801-000071")/*@res "摘要"*/)){
			//			getUIRefPane1Bankzy().getUITextField().requestFocus();
			//		}else if(column + 1 == bankColumn.getCountByName(nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801","UPP2004365801-000072")/*@res "结算方式"*/)){
			//			getUIRefPane1Bankjs().getUITextField().requestFocus();
			//		}
		}

	}

	/**
	 * Invoked when a key has been released.
	 */
	public void keyReleased(java.awt.event.KeyEvent e) {
		if (status == 2) {
			//获得回车键
			if (e.getKeyChar() == KeyEvent.VK_ENTER) {
				int iColumnIndex = m_tUITableBank.getColumnCount() - 1;
				if (m_tUITableBank.getSelectedColumn() == iColumnIndex
						&& m_tUITableBank.getSelectedRow() == m_tUITableBank.getRowCount() - 1) {
					if (onButtonSave()) {
						onButtonAppend();
						return;
					}
				}
			}
		} else if (status == 1) {
			//获得回车键
			if (e.getKeyChar() == KeyEvent.VK_ENTER) {
				int iColumnIndex = m_tUITableBank.getColumnCount() - 1;
				if (m_tUITableBank.getSelectedColumn() == iColumnIndex
						&& m_tUITableBank.getSelectedRow() == m_tUITableBank.getRowCount() - 1) {
					if (onButtonSave()) {
						onButtonAppend();
						return;
					}
				}
			}
		}
	}

	/**
	 * Invoked when a key has been typed.
	 * This event occurs when a key press is followed by a key release.
	 */
	public void keyTyped(java.awt.event.KeyEvent e) {
	}

	/**
	 * 	函数功能:添加
	 *
	 *  参数:
	 *
	 * 	返回值:
	 *
	 * 	异常:
	 *
	 */
	private void onButtonAppend() {
		//增加一行
		int iRowIndex = m_mTableModelBank.getRowCount();
		m_mTableModelBank.addRow(new Vector());
		m_mTableModelBank.setEditableRowColumn(iRowIndex, -99);
		int iColumnIndex = m_tUITableBank.getColumnModel().getColumnIndex(
				nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801", "UPP2004365801-000075")/*@res "日期"*/);
		if (iRowIndex >= 1) {
			getUIRefPane1Bankdate().setText(ce.getDate().toString());
			iColumnIndex = m_tUITableBank.getColumnModel().getColumnIndex(
					nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801", "UPP2004365801-000075")/*@res "日期"*/);
			Object d = m_mTableModelBank.getValueAt(iRowIndex - 1, iColumnIndex);
			if (d == null || d.toString().length() < 1) {
				getUIRefPane1Bankdate().setText(ce.getDate().toString());
				m_mTableModelBank.setValueAt(ce.getDate().toString(), iRowIndex, iColumnIndex);
			} else {
				getUIRefPane1Bankdate().setText(d.toString());
				m_mTableModelBank.setValueAt(d.toString(), iRowIndex, iColumnIndex);
			}
		} else {
			getUIRefPane1Bankdate().setText(ce.getDate().toString());
			m_mTableModelBank.setValueAt(ce.getDate().toString(), iRowIndex, iColumnIndex);
		}
		if (ifManyUnit == 1) {
			getLinkComBox1().setSelectedIndex(1);
		}

		m_tUITableBank.setRowSelectionInterval(iRowIndex, iRowIndex);
		m_tUITableBank.setColumnSelectionInterval(0, 0);
		m_tUITableBank.editCellAt(iRowIndex, 0);
		getUIRefPane1Bankdate().getUITextField().requestFocus();
		//将选中行滚动到当前显示
		m_tUITableBank.scrollRectToVisible(m_tUITableBank.getCellRect(iRowIndex, 0, false));

		//设置按钮状态
		m_bButtonAppend.setEnabled(false);

		m_bButtonEdit.setEnabled(false);
		m_bButtonDelete.setEnabled(false);
		m_bButtonDeleteAll.setEnabled(false);
		m_bButtonSave.setEnabled(true);
		m_bButtonCancel.setEnabled(true);
		m_bButtonQuery.setEnabled(false);
		setButtons(m_aryButtonGroup);
		this.addRows++;
		status = 1;
	}

	/**
	 * 	函数功能:添加
	 *
	 *  参数:
	 *
	 * 	返回值:
	 *
	 * 	异常:
	 *
	 */
	private void onButtonCancel() {

		if (m_tUITableBank.isEditing()) {
			m_tUITableBank.editingStopped(new javax.swing.event.ChangeEvent(this));
		}
		//增加一行
		if (true) {
			if (status == 1) {
				//adjust--20041209
				int rowCounts = m_mTableModelBank.getRowCount();
				for (int i = 0; i < this.addRows; i++) {
					m_mTableModelBank.removeRow(rowCounts - 1 - i);
					m_mTableModelBank.setEditableRowColumn(-99, -99);
				}
				this.addRows = 0;
			}
			if (status == 2) {
				m_mTableModelBank.setDataVector(new Vector());
				int rows = m_tUITableBank.getSelectedRow();
//				int count = m_mTableModelBank.getRowCount();
				refresh();
				if (rows >= 0) {
					m_tUITableBank.setRowSelectionInterval(rows, rows);
				}
			}
			m_mTableModelBank.setEditableRowColumn(-99, -99);
			status = 0;

			setInterfaceBank(nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801", "UPP2004365801-000209")/*@res "借方"*/);
			setInterfaceBank(nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801", "UPP2004365801-000210")/*@res "贷方"*/);
			if (ifManyUnit == 1 || linkVO.length == 1 || cvo.getM_pk_link() != null) {
				setInterfaceBank(nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801", "UPP2004365801-000213")/*@res "余额"*/);
			}
			if (m_mTableModelBank.getRowCount() > 0) {
				getUIRefPane1Bankdate().setText("");
			}
		}

		if (m_mTableModelBank.getRowCount() > 0) {
			m_tUITableBank.setRowSelectionInterval(0, 0);
		}
		//设置按钮状态
		m_bButtonAppend.setEnabled(true);
		m_bButtonEdit.setEnabled(true);
		m_bButtonDelete.setEnabled(true);
		m_bButtonDeleteAll.setEnabled(true);
		m_bButtonSave.setEnabled(false);
		m_bButtonCancel.setEnabled(false);
		m_bButtonQuery.setEnabled(true);
		setButtons(m_aryButtonGroup);

	}

	/**
	 * onButtonClicked 方法注解。
	 */
	public void onButtonClicked(nc.ui.pub.ButtonObject arg1) {
		if (arg1 == m_bButtonQuery) {
			this.showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("2002100555", "UPP2002100555-000064")/*@res "正在查询.."*/);
			onButtonQuery();
			return;
		} else if (arg1 == m_bButtonDirectPrint) {
//			onButtonPrint();
			onDirectPrint();
			this.showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801", "UPP2004365801-000220")/*@res "打印"*/);
			return;
		}else if(arg1 == m_bButtonTemplatePrint){
			onButtonPrint();
			this.showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801", "UPP2004365801-000220")/*@res "打印"*/);
			return;
		} else if (arg1 == m_bFileImprot) {
			onFileImport();
			return;
		} else if (arg1 == m_bButtonInput) {
			onButtonInput();
			this.showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801", "UPP2004365801-000219")/*@res "引入"*/);
			return;
		} else if (arg1 == m_bButtonAppend) {
			onButtonAppend();
			this.showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("2002130055", "UPP2002100555-000058")/*@res "正在增加.."*/);
			return;
		} else if (arg1 == m_bButtonEdit) {
			onButtonEdit();
			this.showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("2002100555", "UPP2002100555-000055")/*@res "正在修改.."*/);
			return;
		} else if (arg1 == m_bButtonDelete) {
			onButtonDelete();
			this.showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("2002100555", "UPP2002100555-000041")/*@res "删除完成"*/);
			return;
		} else if (arg1 == m_bButtonSave) {
			onButtonSave();
			this.showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("2002100555", "UPP2002100555-000006")/*@res "保存成功"*/);
			return;
		} else if (arg1 == m_bButtonCancel) {
			onButtonCancel();
			this.showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801", "UPP2004365801-000021")/*@res "取消"*/);
			return;
		} else if (arg1 == m_bButtonDeleteAll) {
			onButtonDeleteAll();
			return;
		}/*else if (arg1 == m_bButtonImport) {
			onButtonImport();
			this.showHintMessage(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("contrast","UPPcontrast-000049")@res "导入完成！");
			return;
		} else if (arg1 == m_bButtonExport) {
			onButtonExport();
			this.showHintMessage(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("contrast","UPPcontrast-000050")@res "导出完成！");
			return;
		}*/else if (arg1 == m_bButtonRefresh) {
			reQuery("");
			this.showHintMessage(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("contrast","UPPcontrast-000037")/*@res "刷新结束"*/);
			return;
		}
	}

	/**
	 * *文件导入对账单
	 * @author zhaozh
	 * 创建日期：(2008-12-5 下午03:26:09)
	 */
	private void onFileImport() {
		try {
			if(contrast != null && contrast.getM_stopdate() != null){
				MessageDialog.showWarningDlg(this, null, NCLangRes4VoTransl.getNCLangRes().getStrByID("contrast", "UPPcontrast-000117")/*"停用的账户不能导入银行对账单"*/);
				return;
			}
			EbankExcelDialog dlg = new EbankExcelDialog(this);
			dlg.setSize(new Dimension(431, 147));
			if(dlg.showModal() == UIDialog.ID_OK){
				File file = dlg.getFile();
				if(file == null){
					MessageDialog.showErrorDlg(this, null, NCLangRes4VoTransl.getNCLangRes().getStrByID("contrast", "UPPcontrast-000101")/*"请选择Excel文件"*/);
					return;
				}
				String fileName = file.getAbsolutePath();
				IEbankUI ebank = (IEbankUI)Class.forName("nc.ui.ebank.out.EbankEntrance").newInstance();
				ebank.inputExcelDetail(fileName);
				reQuery("ALL");
			}
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
			MessageDialog.showErrorDlg(this, null, e.getMessage());
		}
	}

	/**
	 * @author zhaozh
	 * 2008-7-9 下午03:50:37
	 * 导出银行对账单的格式
	 */
	@SuppressWarnings("unused")
	private void onButtonExport() {
		if (contrast == null || linkVO.length == 0) {
			MessageDialog.showWarningDlg(this, null, nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("2004","UPP2004-000234")/*@res "请先查询对账账户"*/);
		}
	}

	/**
	 * 	函数功能:添加
	 *
	 *  参数:
	 *
	 * 	返回值:
	 *
	 * 	异常:
	 *
	 */
	private void onButtonDelete() {
//		long start = System.currentTimeMillis();
		if (m_tUITableBank.getSelectedRowCount() < 1 && m_mTableModelBank.getRowCount() < 1) {
			MessageDialog.showWarningDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801",
					"UPP2004365801-000027")/*@res "提示"*/, nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801",
					"UPP2004365801-000235")/*@res "没有选中行"*/);
			return;
		}
		if (m_tUITableBank.getSelectedRowCount() == 1) {
			int rows = m_tUITableBank.getSelectedRow();
			int allrows = m_mTableModelBank.getRowCount();

			if (rows >= 0 && allrows > 0) {
				if (MessageDialog.showYesNoDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801",
						"UPP2004365801-000217")/*@res "删除"*/, nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"2004365801", "UPP2004365801-000236")/*@res "确实要删除么？"*/) == MessageDialog.ID_YES) {
					try {

						String key = m_mTableModelBank.getValueAt(
								rows,
								bankColumn.getCountByName(nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801",
										"UPP2004365801-000138")/*@res "主键"*/)).toString();
						BankreceiptVO v = getVO(key);
						int count = getPosition(key);

						if (v.getflag().intValue() > 0) {
							MessageDialog
									.showWarningDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801",
											"UPP2004365801-000027")/*@res "提示"*/, nc.ui.ml.NCLangRes.getInstance()
											.getStrByID("2004365801", "UPP2004365801-000237")/*@res "记录已被勾对，不能删除"*/);
							return;
						}
						if (((IContrastPrv) NCLocator.getInstance().lookup(IContrastPrv.class.getName())).isContrast(
								null, v.getPk_bankreceipt(), 2).booleanValue()) {
							MessageDialog
									.showWarningDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801",
											"UPP2004365801-000027")/*@res "提示"*/, nc.ui.ml.NCLangRes.getInstance()
											.getStrByID("2004365801", "UPP2004365801-000237")/*@res "记录已被勾对，不能删除"*/);
							return;
						}
						if (v != null) {
							getBankService().deleteBank(v, ce.getUser().getUserCode());
						}
						m_mTableModelBank.removeRow(rows);

						if (count != -1) {
							banks.remove(count);
						}

						if (m_tUITableBank.getRowCount() == 0) {
						} else {
							if (rows == 0) {
								m_tUITableBank.setRowSelectionInterval(0, 0);
							} else {
								m_tUITableBank.setRowSelectionInterval(rows - 1, rows - 1);
							}
						}
						setBalanceVO();
						refresh();
						if (ifManyUnit == 1 || linkVO.length == 1 || cvo.getM_pk_link() != null) {
							setInterfaceBank(nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801",
									"UPP2004365801-000213")/*@res "余额"*/);
						}

						if (m_tUITableBank.getRowCount() == 0) {
						} else {
							if (rows == 0) {
								m_tUITableBank.setRowSelectionInterval(0, 0);
							} else {
								m_tUITableBank.setRowSelectionInterval(rows - 1, rows - 1);
							}
						}
					} catch (Throwable e) {
						MessageDialog.showWarningDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801",
								"UPP2004365801-000238")/*@res "删除出现错误"*/, nc.ui.ml.NCLangRes.getInstance().getStrByID(
								"2004365801", "UPP2004365801-000000")/*@res "当前记录可能正被其他系统操作!"*/);
						return;
					}
					reQuery("");//删除之后重查一次，以免余额计算错误
				}
			} else {
				MessageDialog.showWarningDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801",
						"UPP2004365801-000027")/*@res "提示"*/, nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"2004365801", "UPP2004365801-000235")/*@res "没有选中行"*/);
			}
		} else {
			int allrow = m_mTableModelBank.getRowCount();
			int[] rows = m_tUITableBank.getSelectedRows();
			int rowcount = m_tUITableBank.getSelectedRowCount();
//			int rowcount = rows.length;
			ArrayList<String> vopks = new ArrayList<String>();
			if (allrow > 0 && rowcount > 0) {
				if (MessageDialog.showYesNoDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801",
						"UPP2004365801-000217")/*@res "删除"*/, nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"2004365801", "UPP2004365801-000239")/*@res "确实要删除全部数据么？"*/) == MessageDialog.ID_YES) {
					try {
						for (int i = rowcount - 1; i >= 0; i--) {
							int row = rows[i];
							String key = m_mTableModelBank.getValueAt(
									row,
									bankColumn.getCountByName(nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801",
											"UPP2004365801-000138")/*@res "主键"*/)).toString();
							BankreceiptVO v = getVO(key);
							if (v.getflag().intValue() > 0) {
								continue;
								//MessageDialog.showWarningDlg(this,"提示","记录已被勾对，不能删除") ;
								//return;
							}
							//记住没有勾对的账单。
							vopks.add(v.getPk_bankreceipt());

							//if(((IContrastPrv)NCLocator.getInstance().lookup(IContrastPrv.class.getName())).isContrast(null,v.getPk_bankreceipt(),2).booleanValue()){
							//	continue;
							//	//MessageDialog.showWarningDlg(this,"提示","记录已被勾对，不能删除") ;
							//	//return;
							//}
							//if(v != null){
							//	((IBankReceiptPrv)NCLocator.getInstance().lookup(IBankReceiptPrv.class.getName())).deleteBank(v,ce.getUser().getUserCode());
							//}
							//m_mTableModelBank.removeRow(row);
							//if(count != -1){
							//	banks.remove(count);
							//}
							//setBalanceVO();
							//refresh();
							//if(ifManyUnit == 1 || linkVO.length == 1 || cvo.getM_pk_link() != null){
							//	setInterfaceBank(nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801","UPP2004365801-000213")/*@res "余额"*/);
							//}
						}

						//1.call ContrastBO.deleteBankReceiptArray
						IBankReceiptPrv bankReceiptPrv = (IBankReceiptPrv) NCLocator.getInstance().lookup(
								IBankReceiptPrv.class.getName());
						Map deleteResult = bankReceiptPrv.deleteBankReceiptArray(vopks.toArray(new String[0]), ce
								.getUser().getUserCode());
						//2.根据返回值，删除表格中的数据
						for (Iterator iterator = deleteResult.keySet().iterator(); iterator.hasNext();) {
							String key = (String) iterator.next();
							String value = (String) deleteResult.get(key);
							if (value.equals("0")) {
								int pos = getPosition(key);
								if (pos != -1) {
									banks.remove(pos);
								}
							}
						}
						refresh();
						if (ifManyUnit == 1 || linkVO.length == 1 || cvo.getM_pk_link() != null) {
							setInterfaceBank(nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801",
									"UPP2004365801-000213")/*@res "余额"*/);
						}
					} catch (Throwable e) {
						MessageDialog.showWarningDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801",
								"UPP2004365801-000238")/*@res "删除出现错误"*/, e.toString());
						return;
					}
					reQuery("");//删除之后重查一次，以免余额计算错误
				}
			} else {
				MessageDialog.showWarningDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801",
						"UPP2004365801-000027")/*@res "提示"*/, nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"2004365801", "UPP2004365801-000240")/*@res "没有选择纪录！"*/);
				return;
			}

		}
//		long end = System.currentTimeMillis();
//		long costTime = end - start;
	}

	/**
	 * 	函数功能:添加
	 *
	 *  参数:
	 *
	 * 	返回值:
	 *
	 * 	异常:
	 *
	 */
	private void onButtonDeleteAll() {
		//DeleteAll delete = new DeleteAll();
		//delete.setParent(this);
		//delete.setContrast(contrast);
		//if (delete.showModal() == 1){

		//}
		if (m_mTableModelBank.getRowCount() > 0 && m_tUITableBank.getSelectedRowCount() > 1) {
			int allrow = m_mTableModelBank.getRowCount();
			int[] rows = m_tUITableBank.getSelectedRows();
			int rowcount = m_tUITableBank.getSelectedRowCount();
			if (allrow > 0 && rowcount > 0) {
				if (MessageDialog.showYesNoDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801",
						"UPP2004365801-000217")/*@res "删除"*/, nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"2004365801", "UPP2004365801-000239")/*@res "确实要删除全部数据么？"*/) == MessageDialog.ID_YES) {
					try {

						for (int i = rowcount - 1; i >= 0; i--) {
							int row = rows[i];
							String key = m_mTableModelBank.getValueAt(
									row,
									bankColumn.getCountByName(nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801",
											"UPP2004365801-000138")/*@res "主键"*/)).toString();
							BankreceiptVO v = getVO(key);
							int count = getPosition(key);
							if (v.getflag().intValue() > 0) {
								continue;
								//MessageDialog.showWarningDlg(this,"提示","记录已被勾对，不能删除") ;
								//return;
							}
							if (((IContrastPrv) NCLocator.getInstance().lookup(IContrastPrv.class.getName()))
									.isContrast(null, v.getPk_bankreceipt(), 2).booleanValue()) {
								continue;
								//MessageDialog.showWarningDlg(this,"提示","记录已被勾对，不能删除") ;
								//return;
							}
							if (v != null) {
								getBankService().deleteBank(v, ce.getUser().getUserCode());
							}
							m_mTableModelBank.removeRow(row);
							if (count != -1) {
								banks.remove(count);
							}
						}
						setBalanceVO();
					} catch (Throwable e) {
						MessageDialog.showWarningDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801",
								"UPP2004365801-000238")/*@res "删除出现错误"*/, e.toString());
						return;
					}
				}
			} else {
				MessageDialog.showWarningDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801",
						"UPP2004365801-000027")/*@res "提示"*/, nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"2004365801", "UPP2004365801-000240")/*@res "没有选择纪录！"*/);
				return;
			}
		}
	}

	/**
	 * 	函数功能:添加
	 *
	 *  参数:
	 *
	 * 	返回值:
	 *
	 * 	异常:
	 *
	 */
	private void onButtonEdit() {
		//编辑银行
		int allrows = m_mTableModelBank.getRowCount();
		int rows = m_tUITableBank.getSelectedRow();
		if (m_tUITableBank.getSelectedRowCount() > 1) {
			MessageDialog.showWarningDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801",
					"UPP2004365801-000027")/*@res "提示"*/, nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801",
					"UPP2004365801-000241")/*@res "只能选择一条纪录！"*/);
			return;
		}
		if (rows >= 0 && allrows > 0 && rows < allrows) {
			try {
				String key = m_mTableModelBank.getValueAt(
						rows,
						bankColumn.getCountByName(nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801",
								"UPP2004365801-000138")/*@res "主键"*/)).toString();
				BankreceiptVO v = getVO(key);
				if (v.getflag().intValue() > 0) {
					MessageDialog.showWarningDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801",
							"UPP2004365801-000027")/*@res "提示"*/, nc.ui.ml.NCLangRes.getInstance().getStrByID(
							"2004365801", "UPP2004365801-000242")/*@res "记录已被勾对，不能编辑！"*/);
					return;
				}
				if (((IContrastPrv) NCLocator.getInstance().lookup(IContrastPrv.class.getName())).isContrast(null,
						v.getPk_bankreceipt(), 2).booleanValue()) {
					MessageDialog.showWarningDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801",
							"UPP2004365801-000027")/*@res "提示"*/, nc.ui.ml.NCLangRes.getInstance().getStrByID(
							"2004365801", "UPP2004365801-000243")/*@res "记录已被勾对，不能编辑!"*/);
					return;
				}
				if (ifManyUnit == 1) {
					getLinkComBox1().setSelectedIndex(1);
				}
				if (ifManyUnit == 2) {
					getLinkComBox1().setSelectLink(v.getMemo());
					//					setRefWhereParts(v.getPk_corp());
					if (contrast.getSource().intValue() == 1) {
						//根据主体主体账簿返回对应的公司
						String pkCorp = BDGLOrgBookAccessor.getPk_corp(v.getPk_corp());
						setRefWhereParts(pkCorp);
					} else {
						setRefWhereParts(v.getPk_corp());
					}
					//					getUIRefPane1Bankjs().setPK(v.getCheckstyle());
				} else {
					//setRefWhereParts(v.getPk_corp());
				}

			} catch (Throwable e) {
				Logger.error(e.getMessage(), e);
				MessageDialog.showErrorDlg(this, null, e.getMessage());
				return;
			}
			bankRow = rows;
			m_mTableModelBank.setEditableRowColumn(rows, -99);
			if (ifManyUnit == 1) {
				int iColumnIndexs = m_tUITableBank
						.getColumnModel()
						.getColumnIndex(
								nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801", "UPP2004365801-000213")/*@res "余额"*/);
				m_mTableModelBank.setEditDisableRowColumn(rows, iColumnIndexs);
			}
			m_mTableModelBank.setRowEditable(rows, true);
		} else {
			MessageDialog.showWarningDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801",
					"UPP2004365801-000027")/*@res "提示"*/, nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801",
					"UPP2004365801-000244")/*@res "没有选择行！"*/);
			return;
		}
		//设置按钮状态
		m_bButtonAppend.setEnabled(false);
		//m_bButtonInsert.setEnabled(false);
		m_bButtonEdit.setEnabled(false);
		m_bButtonDelete.setEnabled(false);
		m_bButtonDeleteAll.setEnabled(false);
		m_bButtonSave.setEnabled(true);
		m_bButtonCancel.setEnabled(true);
		m_bButtonQuery.setEnabled(false);
		setButtons(m_aryButtonGroup);
		status = 2;
	}

	/**
	 * 函数功能:从资金组织（原结算中心）导入内部账户账数据作为对账单
	 * 创建日期：(2008-9-11 下午07:01:39)
	 * @author zhaozh
	 */
	private void onButtonInput() {
		if(contrast.getM_stopdate() != null){
			MessageDialog.showWarningDlg(this, null, NCLangRes4VoTransl.getNCLangRes().getStrByID("contrast", "UPPcontrast-000117")/*"停用的账户不能导入银行对账单"*/);
			return;
		}
		input = new ImportFundOrgDlg(this,nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("contrast","UPPcontrast-000052")/*@res "从资金组织引入"*/);
		UFDate beginDate = null;
		UFDate endDate = null;
//		input.setContrast(contrast);
//		input.getLinkComBox1().setData(contrast.getPk_contrastaccount(), true);
		AccountlinkVO link = query.getLinkComBox1().getSelectLinks();
		if( link == null && contrast.getMemo() != null && contrast.getMemo().equals("many")){
			showErrorMessage(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("contrast","UPPcontrast-000053")/*@res "请选择子帐户！"*/);
			return;
		}else if(link == null){
			link = linkVO[0];
		}
		int[] results = new int[2];
//		if(getLinkComBox1().getSelectedIndex() <= 1){
//			getLinkComBox1().setSelectedIndex(1);
//		}
		String pk_accid = link.getBankaccount();
		if(!isAccid(pk_accid,link.getPk_corp())){
			showErrorMessage(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("contrast","UPPcontrast-000054")/*@res "不是内部帐户，不能导入对账单！"*/);
			return;
		}
		if(input.showModal() == UIDialog.ID_OK){
			if(!StringUtil.isEmptyWithTrim(input.getRefBeginDate().getText())){
				beginDate = new UFDate(input.getRefBeginDate().getText());
			}else
				beginDate = getClientEnvironment().getBusinessDate();//为空默认为登录日期
			if(StringUtil.isEmptyWithTrim(input.getRefEndDate().getText())){
				showErrorMessage(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("contrast","UPPcontrast-000055")/*@res "结束日期不能为空"*/);
				return;
			}else
				endDate = new UFDate(input.getRefEndDate().getText());
			try {
				IAccountService service = NCLocator.getInstance().lookup(IAccountService.class);
				AccDetailQueryVO qvo = new AccDetailQueryVO();
				qvo.setPk_accid(pk_accid);
				qvo.setStartdate(beginDate);
				qvo.setEnddate(endDate);
				qvo.setCheck(UFBoolean.FALSE);
				AccDetailVO[] accvos = service.queryAccDetailforCbank(new AccDetailQueryVO[]{qvo});
				if(accvos != null && accvos.length>0){
					BankQueryVO bqvo = new BankQueryVO();
					bqvo.setPk_bank(link.getBankaccount());
					bqvo.setStartdate(beginDate);
					bqvo.setEnddate(endDate);
					bqvo.setContrastaccount(link.getPk_contrastaccount());
					results = ContrastServiceFactory.getBankService().precessSettleBanks(bqvo, accvos, link);
				}
				if(results[0] > 0){
					MessageDialog.showHintDlg(this, null, nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("2004","UPP2004-000236")/*@res "更新"*/+ results[0] +nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("2004","UPP2004-000237")/*@res "条数据"*/);
				}
				if(results[1] > 0){
					MessageDialog.showHintDlg(this, null, nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("contrast","UPPcontrast-000038",null,new String[]{String.valueOf(results[1])})/*@res "插入{0}条数据"*/);
				}
//				List<String> pks = new ArrayList<String>(); 
//				if(accvos != null && accvos.length>0){
//					for(AccDetailVO dvo: accvos)
//						pks.add(dvo.getPrimaryKey());
//					String inSql = StringUtil.getInClause(pks.toArray(new String[0]));
//					getBankService().updateJszxDetail(inSql,qvo,link);
//					convert(accvos,link);
//					if(bankvos != null && bankvos.length>0){
//						getBankService().insertSettleBanks(bankvos);
//						MessageDialog.showHintDlg(this, null, nc.vo.ml.NCLangRes4VoTransl.getNCLangRes()
//								.getStrByID("contrast","UPPcontrast-000038",null,
//									new String[]{String.valueOf(bankvos.length)})/*@res "插入{0}条数据"*/);
//					}
//				}
			} catch (BusinessException e) {
				Debug.error(e.getMessage(), e);
				MessageDialog.showErrorDlg(this, null, e.getMessage());
			}
			reQuery("");
		}
	}

	/**
	 * 创建日期：(2008-9-11 下午09:03:06)
	 * 将内部帐户来的对账单转换为现金银行对账单数据
	 * modifier by zhaozh on 2008-12-19
	 * 	重复判断，增加结算方式，结算号取单据编号，修改的数据更新
	 * @param accvos
	 * @param qvo 
	 * @param qvo 
	 * @param linkvo
	 * @return
	 * @throws BusinessException
	 */
	@SuppressWarnings("unused")
	private BankReceiptVO[] convert(AccDetailVO[] accvos,BankQueryVO qvo, AccountlinkVO linkvo) throws BusinessException {
		if(accvos == null || accvos.length<=0){
			MessageDialog.showHintDlg(this, null, nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("2004","UPP2004-000235")/*@res "没有数据"*/);
			return null;
		}
		ArrayList<BankReceiptVO> list = new ArrayList<BankReceiptVO>();
		List<BankReceiptVO> updateList = new ArrayList<BankReceiptVO>();
		List<String> pklist = new ArrayList<String>();
		for(AccDetailVO avo:accvos)
			pklist.add(avo.getPrimaryKey());
		qvo.setPk_jszxdetails(pklist);
		Map<String, BankReceiptVO> banks = getBankService().getRepeat(qvo);
		for(AccDetailVO avo:accvos){
			BankReceiptVO bank = banks.get(avo.getPrimaryKey());
			if( bank != null){//重复数据，更新关键数据
				bank.setCheckdate(avo.getTallydate());//日期
				bank.setCheckstyle("");//结算方式取哪个？
				bank.setExplanation(avo.getMemo());//备注
				bank.setPk_check(avo.getFbmbillno());//结算号取票据编号
				bank.setDebitamount(avo.getInmoney());//借方取收款原币
				bank.setCreditamount(avo.getOutmoney());//贷方取付款原币
				bank.setStyleflag(avo.getIseffective().booleanValue()?"A":"B");//更新有效标志
				updateList.add(bank);
			}else{//新数据插入
				BankReceiptVO bvo = new BankReceiptVO();
				bvo.setCheckdate(avo.getTallydate());//日期
				bvo.setCheckstyle("");//结算方式取哪个？
				bvo.setExplanation(avo.getMemo());//备注
				bvo.setPk_check(avo.getFbmbillno());//结算号取票据编号
				bvo.setDebitamount(avo.getInmoney());//借方取收款原币
				bvo.setCreditamount(avo.getOutmoney());//贷方取付款原币
				bvo.setPk_jszxdetail(avo.getPrimaryKey());
				//补充信息
				bvo.setMemo(linkvo.getPk_accountlink());
				bvo.setPk_bank(linkvo.getBankaccount());
				bvo.setPk_corp(linkvo.getPk_corp());
				bvo.setYears(linkvo.getYears());
				bvo.setPk_subject(linkvo.getPk_subject());
				bvo.setPk_ass(linkvo.getPk_ass());
				bvo.setPk_contrastaccount(linkvo.getPk_contrastaccount());
				bvo.setStyleflag(avo.getIseffective().booleanValue()?"A":"B");//设置标志为有效
				list.add(bvo);
			}
		}
		if((updateList != null && updateList.size() > 0)
				|| (null != list && list.size() >0)){
			MessageDialog.showHintDlg(this, null, nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("2004","UPP2004-000236")/*@res "更新"*/+updateList.size()+nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("2004","UPP2004-000237")/*@res "条数据"*/);
			MessageDialog.showHintDlg(this, null, nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("contrast","UPPcontrast-000038",null,new String[]{String.valueOf(list.size())})/*@res "插入{0}条数据"*/);
		}
		return null;
//		return list.toArray(new BankReceiptVO[list.size()]);
	}

	/**
	 * 判断是否是内部帐户。。。通过接口查询bd_accid表
	 * @param bankaccount
	 * @param pk_corp
	 * @return
	 */
	private boolean isAccid(String bankaccount, String pk_corp) {
		boolean flag = true;
//		String con = " pk_accid='" + bankaccount + "' ";
		try {
			CurrtypeVO curr = NCLocator.getInstance().lookup(IContrastAccountPrv.class).getCurrtype(bankaccount);
			if(curr == null)
				return false;
			if(contrast.getPk_currtype()!=null &&
				!contrast.getPk_currtype().equals(curr.getPk_currtype())){
					showErrorMessage(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("contrast","UPPcontrast-000057")/*@res "对帐帐户与结算中心帐户的币种不一致！"*/);
					return false;
			}
		} catch (Exception e) {
			Debug.debug(e.getMessage(), e);
			MessageDialog.showErrorDlg(this,nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("2004","UPP2004-000238")/*@res "发生异常"*/, e.getMessage());
		}
		return flag;
	}

	@SuppressWarnings("restriction")
	private void onDirectPrint(){
		FiPrintDirectProxy proxy = new FiPrintDirectProxy();
		proxy.setTitle(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("contrast","UPPcontrast-000058")/*@res "银行对账单"*/);
		try {
			proxy.printDirect(this);
		} catch (BusinessException e) {
			Debug.debug(e.getMessage(), e);
			showErrorMessage(e.getMessage());
		}
	}
	/**
	 * 	函数功能:打印
	 */
	@SuppressWarnings("unused")
	private void onButtonPrint() {
		String m_sNodeCode = "20040605";
		//打印VO  设置表头数据
		PrintVO vo = new PrintVO();
		String[] account = new String[1];
		String[] date = new String[1];
		if (contrast != null) {
			account[0] = contrast.getContrastaccountname();
		} else {
			account[0] = "";
		}
		if (query != null) {
			date[0] = query.getUIRefPanedate1().getText()
					+ nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801", "UPP2004365801-000245")/*@res "——"*/
					+ query.getUIRefPanedate2().getText();
		} else {
			date[0] = "";
		}
		String[] ba = new String[1];
		ba[0] = getUILabel2().getText().substring(5);
		//	ba[0] = getUILabel2().getText();
		vo.setInitbalance(ba);
		vo.setAccountname(account); //表头数据
		vo.setDatescope(date);
		vo.setPrecision(precision);
		/////设置表体数据
		//{"accountname","date","summary","settletype","settlenum","billdate","debit","credit","reconciled","datescope"};
		Object[][] temps = m_mTableModelBank.getDataArray();

		String[][] datas;
		if (temps != null && temps.length > 0) {
			datas = new String[temps.length][11];
			if (ifManyUnit == 1) {
				for (int i = 0; i < temps.length; i++) {
					datas[i][0] = temps[i][0] == null ? null : temps[i][0].toString();
					datas[i][1] = temps[i][1] == null ? null : temps[i][1].toString();
					datas[i][2] = temps[i][2] == null ? null : temps[i][2].toString();
					datas[i][3] = temps[i][3] == null ? null : temps[i][3].toString();
					datas[i][4] = temps[i][4] == null ? null : temps[i][4].toString();
					datas[i][5] = temps[i][5] == null ? null : temps[i][5].toString();
					if (temps[i][8] != null) {
						datas[i][6] = temps[i][8] == "N" ? "" : temps[i][8].toString(); //两清
					}
					datas[i][7] = temps[i][6] == null ? null : temps[i][6].toString(); //余额
				}
			} else {
				for (int i = 0; i < temps.length; i++) {
					datas[i][0] = temps[i][1] == null ? null : temps[i][1].toString();
					datas[i][1] = temps[i][2] == null ? null : temps[i][2].toString();
					datas[i][2] = temps[i][3] == null ? null : temps[i][3].toString();
					datas[i][3] = temps[i][4] == null ? null : temps[i][4].toString();
					datas[i][4] = temps[i][5] == null ? null : temps[i][5].toString();
					datas[i][5] = temps[i][6] == null ? null : temps[i][6].toString();
					if (temps[i][9] != null) {
						datas[i][6] = temps[i][9] == "N" ? "" : temps[i][9].toString(); //两清
					}
					datas[i][7] = temps[i][7] == null ? null : temps[i][7].toString(); //余额
					if (contrast.getSource().intValue() == 1) {
						datas[i][8] = temps[i][10] == null ? null : temps[i][10].toString(); //单位
						datas[i][9] = temps[i][11] == null ? null : temps[i][11].toString(); //科目
						datas[i][10] = null;
					} else {
						datas[i][8] = temps[i][10] == null ? null : temps[i][10].toString(); //单位
						datas[i][9] = null;
						datas[i][10] = temps[i][11] == null ? null : temps[i][11].toString(); //账户
					}
				}
			}

		} else {
			MessageDialog.showWarningDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801",
					"UPP2004365801-000027")/*@res "提示"*/, nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801",
					"UPP2004365801-000246")/*@res "没有数据，不能打印！"*/);
			return;
		}
		//////////////////
		BankReceipPrint dataSource = new BankReceipPrint();

		dataSource.setDataItemValues(datas, vo); //初始化数据

		//实例化打印入口
		nc.ui.pub.print.PrintEntry print = new nc.ui.pub.print.PrintEntry(null, null);
		//选择该数据源对应的打印模板，选定模板后再选择预览或打印
		if (this.contrast.getSource().intValue() == 1) {
			print.setTemplateID(this.contrast.getPk_corp(), m_sNodeCode, ce.getUser().getPrimaryKey(), null);
		} else {
			print.setTemplateID(ce.getCorporation().getPk_corp(), m_sNodeCode, ce.getUser().getPrimaryKey(), null);
		}
		print.setDataSource(dataSource);
		if (print.selectTemplate() < 0) {
			return;
		}
		print.preview();

		/*//下面是直接打印代码
		 String[] m_sColumnName = {"日期","摘要","结算方式","结算号","借方","贷方","余额"};

		 if(m_mTableModelBank.getRowCount()<1){
		 return;
		 }

		 int rowH[] = new int[m_mTableModelBank.getRowCount()+1];
		 for (int i = 0; i < rowH.length; i++){
		 rowH[i] = 20;
		 }

		 Object[][] temp = new Object[m_mTableModelBank.getRowCount()][7];

		 for (int i = 0; i < m_mTableModelBank.getRowCount(); i++){
		 //设置两清标记
		 temp[i][0] = m_mTableModelBank.getValueAt(i,0);
		 temp[i][1] = m_mTableModelBank.getValueAt(i,1);
		 temp[i][2] = m_mTableModelBank.getValueAt(i,2);
		 temp[i][3] = m_mTableModelBank.getValueAt(i,3);
		 temp[i][4] = m_mTableModelBank.getValueAt(i,4);
		 temp[i][5] = m_mTableModelBank.getValueAt(i,5);
		 temp[i][6] = m_mTableModelBank.getValueAt(i,6);
		 }
		 String[][] colname = new String[1][m_sColumnName.length];
		 for(int i=0;i<m_sColumnName.length;i++){
		 colname[0][i] = m_sColumnName[i];
		 }

		 int columnCount = 7;
		 int[] colwidth = new int[columnCount] ;
		 int[] alignflag = new int[columnCount] ;
		 //{"日期","摘要","结算方式","结算号","借方","贷方","余额"};
		 int[] cols = {70,60,60,50,75,75,75};
		 int[] aligns = {1,0,0,0,2,2,2};

		 for(int i=0;i<columnCount;i++){
		 colwidth[i] = cols[i];
		 alignflag[i] = aligns[i];
		 }
		 String title = "银行对帐单打印";
		 java.awt.Font font = new java.awt.Font("dialog", java.awt.Font.BOLD, 30);
		 java.awt.Font font1 = new java.awt.Font("dialog", java.awt.Font.PLAIN, 12);

		 String topstr ="单位名称： "+ce.getCorporation().getUnitname()+"    帐户名称："+contrast.getContrastaccountname();
		 String botstr ="制表时间："+ce.getDate()+"          制表人： "+ce.getUser().getUserName();
		 //
		 PrintDirectEntry print = new PrintDirectEntry();
		 print.setTitle(title);	    	//标题  可选
		 print.setTitleFont(font);	    //标题字体   可选
		 print.setContentFont(font1);	//内容字体（表头、表格、表尾） 可选
		 print.setTopStr(topstr);	    //表头信息  可选
		 print.setBottomStr(botstr);	    //表尾信息   可选
		 print.setColNames(colname);     //表格列名（二维数组形式）
		 print.setData(temp);	        //表格数据
		 print.setColWidth(colwidth);	//表格列宽    可选
		 print.setAlignFlag(alignflag);		//表格每列的对齐方式（0-左, 1-中, 2-右）可选
		 ///
		 print.setTitleHeight(25);         //标题高度
		 print.setPrintDirection(true);    //打印方向 纵向
		 print.setRowHeight(rowH);         //设置行高
		 print.setTopStrFixed(true);       //表头是否固定打印

		 //
		 print.preview(); 	//预览
		 */

	}

	/**
	 * 	函数功能:添加
	 *  @author zhaozh
	 *  创建日期：(2008-9-4 下午04:50:15)
	 *  修改：银行对账单余额计算错误
	 */
	private void onButtonQuery() {
		//	query.setParent(this);
		if (query == null) //added by jh 不用重新生成
		{
			query = new QueryUI(this);
			query.setUnitFlag(1);
			GLQueryVO qvo = new GLQueryVO();
			qvo.setPK_corp(ce.getCorporation().getPk_corp());
			qvo.setAccounttype(GLQueryVO.CORP);
			query.setAccount(qvo);
		}
		if (query.showModal() == 1) {
			ContrastQueryVO q = new ContrastQueryVO();
			contrast = query.getContrast();
			cvo = query.getQueryVO();
			try {
//				String nowYear = ContrastServiceFactory.getAccountService().getCarry(contrast.getPk_contrastaccount());
				String nowYear = context.getCarry(contrast.getPk_contrastaccount()).toString();
				if (!nowYear.equals(contrast.getYears())) {
					showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801", "UPP2004365801-000247")/*@res "帐户已经结转！不能操作！"*/);
					m_bButtonAppend.setEnabled(false);
					//m_bButtonInsert.setEnabled(false);
					m_bButtonEdit.setEnabled(false);
					m_bButtonDelete.setEnabled(false);
					m_bButtonSave.setEnabled(false);
					m_bButtonCancel.setEnabled(false);
					m_bButtonInput.setEnabled(false);
					setButtons(m_aryButtonGroup);
					return;
				}
				if(context.getExcept(contrast.getPk_contrastaccount())){
					showErrorMessage(NCLangRes4VoTransl.getNCLangRes().getStrByID("contrast", "UPPcontrast-000118")/*"发现银行对账单有异常记录被勾兑，请进行处理"*/);
				}
			} catch (Throwable er) {
				Logger.error(er.getMessage(), er);
				this.showHintMessage("BankReceiptClientUI:" + er.getMessage());
				return;
			}
			//cvo.setFlag(ContrastQueryVO.CHECK);
			cvo.setInit(ContrastQueryVO.NOTINIT);
			q.setContrastaccount(cvo.getContrastaccount());
			q.setYear(ce.getAccountYear());
			q.setDate1(cvo.getDate1());
			q.setDate2(cvo.getDate2());
			q.setInit(ContrastQueryVO.NOTINIT);
			q.setFlag(ContrastQueryVO.VERIFY);
			q.setVoucher(contrast.getSource().intValue());
			q.setOnlyWrongRec(cvo.getOnlyWrongRec());
//			try {
//				if (ContrastServiceFactory.getAccountService().isExceptional(contrast)) {
//					showErrorMessage(NCLangRes4VoTransl.getNCLangRes().getStrByID("contrast", "UPPcontrast-000118")/*"发现银行对账单有异常记录被勾兑，请进行处理"*/);
//				}
//			} catch (BusinessException e) {
//				MessageDialog.showErrorDlg(this, null, e.getMessage());
//			}
			UFDouble b = new UFDouble(0);
			if (cvo != null) {
				try {
					banks.removeAllElements();
					if (q.getDate1() == null) {
						q.setDate1(contrast.getStartdate());

					}
					if (q.getDate2() == null) {
						q.setDate2(getPeriod().getPeriod(ce.getAccountYear()).getEnddate());

					}
//					linkVO = ContrastServiceFactory.getAccountService().queryAccountLink(contrast.getPk_contrastaccount(), ce.getAccountYear());
					linkVO = ArrayUtil.changeListtovos((context.getLinks(contrast.getPk_contrastaccount())),AccountlinkVO.class); 
					if (contrast.getMemo() != null && contrast.getMemo().trim().equals("one")) {
						ifManyUnit = 1;
						ifoldAccount = false;
						contrast.setM_pk_link(linkVO[0].getPk_accountlink());
					} else if (contrast.getMemo() != null && (contrast.getMemo().trim().equals("many") || contrast.getMemo().trim().equals("merge"))) {
						ifManyUnit = 2;
						ifoldAccount = false;
					} else {
						ifManyUnit = 1;
						ifoldAccount = true;
					}
					q.setManyCorp(ifManyUnit);
					/**@author zhaozh 创建日期：(2008-7-28 上午10:44:29)增加方向查询条件*/
					q.setAspect(cvo.getAspect());
					if(!contrast.getMemo().equals("merge") && query.getLinkComBox1().getSelectLinks() != null)
						q.setM_pk_link(query.getLinkComBox1().getSelectLinks().getPk_accountlink());
					/***********************************************/
					BankreceiptVO[] vt = getBankService().queryBankConditionInit(q);
					getLinkComBox1().setData(q.getContrastaccount(), true);
					/*********************************************************/
					if (cvo.getDate1() != null) {
						b = ContrastServiceFactory.getAccountService().getBankMoney(cvo.getContrastaccount(), ce.getAccountYear(), cvo.getDate1(), 1);
					}

					///////////////////////////////
					//设置精度
					setPrecision(contrast.getPk_currtype());
					setInterfaceBank(nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801", "UPP2004365801-000209")/*@res "借方"*/);
					setInterfaceBank(nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801", "UPP2004365801-000210")/*@res "贷方"*/);
					getUILabel1().setText(
							nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801", "UPP2004365801-000248")/*@res "启动日期："*/
									+ query.getContrast().getStartdate());
					getUILabel3().setText(
							nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801", "UPP2004365801-000249")/*@res "    账户："*/
									+ query.getContrast().getContrastaccountname());
					NumberFormat fx = NumberFormat.getNumberInstance();
					int pre = getPrecision("");
					fx.setMaximumFractionDigits(pre);
					fx.setMinimumFractionDigits(pre);
					if (ifManyUnit == 2 && cvo.getM_pk_link() != null) {
						for (int i = 0; i < linkVO.length; i++) {
							if (cvo.getM_pk_link().equals(linkVO[i].getPk_accountlink())) {
								query.getContrast().setDebitamount(
										linkVO[i].getDebitamount() == null ? new UFDouble(0.0) : linkVO[i]
												.getDebitamount());
								query.getContrast().setCreditamount(
										linkVO[i].getCreditamount() == null ? new UFDouble(0.0) : linkVO[i]
												.getCreditamount());
								break;
							}
						}
					}
					if (new UFDouble(query.getContrast().getCreditamount().toString()).doubleValue() == 0.0
							&& new UFDouble(query.getContrast().getDebitamount().toString()).doubleValue() == 0.0) {
						if (contrast.getContrastaspect().intValue() == 1) {
							b = new UFDouble(0).sub(b);
						}
						UFDouble dTemp = query.getContrast().getDebitamount().add(b);
						nowB = new UFDouble(dTemp);
						String sTemp = fx.format(dTemp);
						getUILabel2().setText(
								nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801", "UPP2004365801-000250")/*@res "    期初余额："*/
										+ sTemp);

					} else if (query.getContrast().getContrastaspect().intValue() == 0
							&& query.getContrast().getDebitamount() != null
							&& new UFDouble(query.getContrast().getCreditamount().toString()).doubleValue() == 0.0) {
						UFDouble dTemp = query.getContrast().getDebitamount().add(b);
						nowB = new UFDouble(dTemp);
						String sTemp = fx.format(dTemp);
						getUILabel2().setText(
								nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801", "UPP2004365801-000250")/*@res "    期初余额："*/
										+ sTemp);
					} else if (query.getContrast().getContrastaspect().intValue() == 1
							&& query.getContrast().getCreditamount() != null
							&& new UFDouble(query.getContrast().getDebitamount().toString()).doubleValue() == 0.0) {

						UFDouble dTemp = query.getContrast().getCreditamount().sub(b);
						nowB = new UFDouble(dTemp);
						String sTemp = fx.format(dTemp);
						getUILabel2().setText(
								nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801", "UPP2004365801-000250")/*@res "    期初余额："*/
										+ sTemp);

					} else {
						nowB = new UFDouble(0);
						getUILabel2()
								.setText(
										nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801",
												"UPP2004365801-000251")/*@res "    期初余额： 0"*/);
					}
					//////////////////////////////////////////////
//					filter(vt);
//					setBalanceVO();
					if (vt != null && vt.length > 0) {
						int count = vt.length;
						for (int i = 0; i < count; i++) {
							banks.add(vt[i]);
						}
						setBalanceVO();
					}
					initTable();
					refresh();
				} catch (Throwable er) {
					Logger.error(er.getMessage(), er);
					MessageDialog.showErrorDlg(this, null, er.getMessage());
				}
			}
			setInterfaceBank(nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801", "UPP2004365801-000209")/*@res "借方"*/);
			setInterfaceBank(nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801", "UPP2004365801-000210")/*@res "贷方"*/);
			if (ifManyUnit == 1 || linkVO.length == 1 || cvo.getM_pk_link() != null) {
				setInterfaceBank(nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801", "UPP2004365801-000213")/*@res "余额"*/);
			}
			m_bButtonAppend.setEnabled(true);
			m_bButtonCancel.setEnabled(false);
			m_bButtonDelete.setEnabled(true);
			m_bButtonDeleteAll.setEnabled(true);
			m_bButtonEdit.setEnabled(true);
			m_bFileImprot.setEnabled(true);
			m_bButtonRefresh.setEnabled(true);
//			m_bButtonSave.setEnabled(true);
//			add by zhaozh 导入导出只支持本单位，不支持多单位
			m_bButtonImport.setEnabled(ifManyUnit == 1 || isMergeAccount());
			m_bButtonExport.setEnabled(ifManyUnit == 1);
			//v31newneed:现金银行－银行对账－银行对账单，增加以下控制：初始化设置中“开户银行账号”字段为空的账户，才可以引入结算中心的结算凭证数据；初始化设置中“开户银行账号”字段非空的账户，不能引入结算中心的结算凭证数据。
			if (this.linkVO != null && this.linkVO.length > 0 && this.linkVO[0].getM_pk_khAccount() != null) {
				m_bButtonInput.setEnabled(false);
			} else {
				m_bButtonInput.setEnabled(true);
			}
			setButtons(m_aryButtonGroup);
		} else {
			//设置按钮状态
			if (getUILabel3()
					.getText()
					.equals(nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801", "UPP2004365801-000233")/*@res "    账户：XXXXXXXXXX"*/)) {
				m_bButtonAppend.setEnabled(false);
				m_bButtonEdit.setEnabled(false);
				m_bButtonDelete.setEnabled(false);
				m_bButtonDeleteAll.setEnabled(false);
				m_bButtonSave.setEnabled(false);
				m_bButtonCancel.setEnabled(false);
				m_bButtonInput.setEnabled(false);
//				m_bFileImprot.setEnabled(false);
				m_bButtonRefresh.setEnabled(false);
				setButtons(m_aryButtonGroup);
			}
		}
		this.showHintMessage(NCLangRes4VoTransl.getNCLangRes().getStrByID("2002100555", "UPP2002100555-000065")/*@res "查询结束"*/);
	}

//	private void callRemoteService() throws BusinessException {
//		List<IRemoteCallItem> callList = new ArrayList<IRemoteCallItem>();
//		if(context.getCarry(contrast.getPk_contrastaccount()) == null)
//			callList.add(itemFactory.getCarryYearCall(contrast.getPk_contrastaccount()));
//		if(context.getExcept(contrast.getPk_contrastaccount()) == null){
//			callList.add(itemFactory.getExceptCall(contrast));
//		}
//		if(context.getLinks(contrast.getPk_contrastaccount()) == null)
//			callList.add(itemFactory.getLinkCall(contrast.getPk_contrastaccount(), ce.getAccountYear()));
//		RometCallProxy.callRemoteService(callList);
//	}

	/**
	 * @return
	 */
	private boolean isMergeAccount() {
		return contrast.getMemo().equals("merge");
	}

	/**
	 * @param vt
	 * 按照查询条件过滤银行对账单,以使余额计算正确
	 */
	@SuppressWarnings("unused")
	private void filter(BankreceiptVO[] vt) {
		if (vt != null && vt.length > 0) {
			int count = vt.length;
			for (int i = 0; i < count; i++) {
				if (cvo.getFlag() == 1) {
					if (((BankreceiptVO) vt[i]).getflag().intValue() > 1) {
						continue;
					}
				} else if (cvo.getFlag() == 0) {
					if (((BankreceiptVO) vt[i]).getflag().intValue() > 0) {
						continue;
					}
				}
				if (cvo.getCheck() != null && cvo.getCheck().toString().length() > 0) {
					if (((BankreceiptVO)vt[i]).getCheckstyle() != null
							&& ((BankreceiptVO)vt[i]).getCheckstyle().equals(cvo.getCheck())) {
					} else {
						continue;
					}
				}
				if (cvo.getCheckno() != null && cvo.getCheckno().toString().length() > 0) {
					if (((BankreceiptVO)vt[i]).getPk_check() != null
							&& ((BankreceiptVO)vt[i]).getPk_check().equals(cvo.getCheckno())) {
					} else {
						continue;
					}
				}
				if (cvo.getM_pk_corp() != null && cvo.getM_pk_corp().length > 0) {
					int a = 1;
					for (int j = 0; j < cvo.getM_pk_corp().length; j++) {
						if (((BankreceiptVO)vt[i]).getPk_corp() != null
								&& ((BankreceiptVO) vt[i]).getPk_corp().toString().trim().equals(
										cvo.getM_pk_corp()[j])) {
							a = 0;
							break;
						}
					}
					if (a == 1) {
						continue;
					}
				}
				if (cvo.getM_pk_bank() != null && cvo.getM_pk_bank().toString().length() > 0) {
					if (((BankreceiptVO) vt[i]).getPk_bank() != null
							&& ((BankreceiptVO) vt[i]).getPk_bank().equals(cvo.getM_pk_bank())) {
					} else {
						continue;
					}
				}
				if (cvo.getM_pk_sub() != null && cvo.getM_pk_sub().toString().length() > 0) {
					if (((BankreceiptVO) vt[i]).getPk_subject() != null
							&& ((BankreceiptVO)vt[i]).getPk_subject().equals(cvo.getM_pk_sub())) {
					} else {
						continue;
					}
				}
				////////////////////////////////
				//金额方向过滤
				if (cvo.getAspect() != -1) {
					if (cvo.getAspect() == 0) {
						if (((BankreceiptVO)vt[i]).getDebitamount() != null && ((BankreceiptVO)vt[i]).getDebitamount().doubleValue() == 0.0d) {
							continue;
						}
					}
					if (cvo.getAspect() == 1) {
						if (((BankreceiptVO)vt[i]).getCreditamount() != null && ((BankreceiptVO)vt[i]).getCreditamount().doubleValue() == 0.0d) {
							continue;
						}
					}
				}
				//金额过滤
				if (cvo.getArea1() != null && cvo.getArea1().toString().length() > 0) {
					UFDouble temp = new UFDouble(0);
					UFDouble temp1 = new UFDouble(cvo.getArea1().toString());
					if (((BankreceiptVO)vt[i]).getDebitamount() != null && ((BankreceiptVO)vt[i]).getDebitamount().doubleValue() != 0.0d) {
						temp = ((BankreceiptVO)vt[i]).getDebitamount();
					} else if (((BankreceiptVO)vt[i]).getCreditamount().doubleValue() != 0.0d) {
						temp = ((BankreceiptVO)vt[i]).getCreditamount();
					}
					if (temp.sub(temp1).doubleValue() < 0.0d) {
						continue;
					}
				}
				//金额过滤
				if (cvo.getArea2() != null && cvo.getArea2().toString().length() > 0) {
					UFDouble temp = new UFDouble(0);
					UFDouble temp2 = new UFDouble(cvo.getArea2().toString());
					if (((BankreceiptVO)vt[i]).getDebitamount() != null && ((BankreceiptVO)vt[i]).getDebitamount().doubleValue() != 0.0d) {
						temp = ((BankreceiptVO)vt[i]).getDebitamount();
					} else if (((BankreceiptVO)vt[i]).getCreditamount().doubleValue() != 0.0d) {
						temp = ((BankreceiptVO)vt[i]).getCreditamount();
					}
					if (temp.sub(temp2).doubleValue() > 0.0d) {
						continue;
					}
				}
				banks.add(vt[i]);
			}
		}
	}

	/**
	 * 	函数功能:添加
	 *
	 *  参数:
	 *
	 * 	返回值:
	 *
	 * 	异常:
	 *
	 */
	@SuppressWarnings("unchecked")
	private boolean onButtonSave() {
		if (m_tUITableBank.isEditing()) {
			m_tUITableBank.editingStopped(new javax.swing.event.ChangeEvent(this));
		}
		//添加银行
		if (status == 1) {
			if (m_tUITableBank.isEditing()) {
				m_tUITableBank.editingStopped(new javax.swing.event.ChangeEvent(this));
			}
			//			adjust--20041209
			int rowCounts = m_mTableModelBank.getRowCount();
			for (int i = 0; i < this.addRows; i++) {
				int row = rowCounts - this.addRows + i;
				BankreceiptVO vo = new BankreceiptVO();
				vo = getBankVO(true, row);
				if (vo == null) {
					return false;
				}
				if (!checkBank(vo)) {
					return false;
				}
				///////////////////
				try {
					String key = getBankService().insertBank(vo);
					m_mTableModelBank.setValueAt(key, row, 7);
					vo.setPk_bankreceipt(key);
					insertVO(vo);

				} catch (Throwable e) {
					Logger.error(e.getMessage(), e);
					MessageDialog.showErrorDlg(this, null, e.getMessage());
					return false;
				}
			}
			this.addRows = 0;

		} else {

			//编辑保存
			if (m_tUITableBank.isEditing()) {
				m_tUITableBank.editingStopped(new javax.swing.event.ChangeEvent(this));
			}
			int row = bankRow;

			//iColumnIndex = m_tUITableBank.getColumnModel().getColumnIndex("主键");
			String key = "";

			key = m_mTableModelBank.getValueAt(
					row,
					bankColumn.getCountByName(nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801",
							"UPP2004365801-000138")/*@res "主键"*/)).toString();

			BankreceiptVO v = getVO(key);
			int count = getPosition(key);

			if (v.getflag().intValue() > 0) {
				MessageDialog.showWarningDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801",
						"UPP2004365801-000027")/*@res "提示"*/, nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"2004365801", "UPP2004365801-000237")/*@res "记录已被勾对，不能删除"*/);
				return false;

			}
			BankreceiptVO vo = new BankreceiptVO();
			vo = getBankVO(true, row);
			if (vo == null) {
				return false;
			}
			//vo.setPeriod("00");  //会计月
			vo.setPk_bankreceipt(v.getPk_bankreceipt());
			if (!checkBank(vo)) {
				return false;
			}
			if (isChangeBank(vo, v)) {
				try {
					getBankService().updateBank(vo,ce.getUser().getUserCode());
					banks.setElementAt(vo, count);
				} catch (Throwable e) {
					Logger.error(e.getMessage(), e);
					MessageDialog.showErrorDlg(this, null, e.getMessage());
					showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801", "UPP2004365801-000087")/*@res "数据可能被他人使用！"*/);
					return false;
				}
			}

			getUIRefPane1Bankjs().setPK(null);
		}
		m_mTableModelBank.setEditableRowColumn(-99, -99);

		//设置按钮状态
		m_bButtonAppend.setEnabled(true);
		//m_bButtonInsert.setEnabled(true);
		m_bButtonEdit.setEnabled(true);
		m_bButtonDelete.setEnabled(true);
		m_bButtonDeleteAll.setEnabled(true);
		m_bButtonSave.setEnabled(false);
		m_bButtonCancel.setEnabled(false);
		m_bButtonQuery.setEnabled(true);
		setButtons(m_aryButtonGroup);
		setBalanceVO();
		refresh();
		if (status == 1) {
			m_tUITableBank.setRowSelectionInterval(m_mTableModelBank.getRowCount() - 1,
					m_mTableModelBank.getRowCount() - 1);
		} else {
			m_tUITableBank.setRowSelectionInterval(bankRow, bankRow);
		}
		setInterfaceBank(nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801", "UPP2004365801-000209")/*@res "借方"*/);
		setInterfaceBank(nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801", "UPP2004365801-000210")/*@res "贷方"*/);

		if (ifManyUnit == 1 || linkVO.length == 1 || cvo.getM_pk_link() != null) {
			setInterfaceBank(nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801", "UPP2004365801-000213")/*@res "余额"*/);
		}

		if (m_mTableModelBank.getRowCount() > 0) {
			//getUIRefPane1Bankdate().setText("");
		}
		status = 0;
		//getUIRefPaneUnit().setPK("");

		return true;

	}

	/**
	 * 	函数功能:刷新并且查询条件过滤数据
	 *  modifier by zhaozh on 2009-6-15
	 *  升级后没有关联银行账户的也能查出来
	 *
	 */
	private void refresh() {

		Vector bankV = new Vector();
		if (banks != null && banks.size() > 0) {
			for (int i = 0; i < banks.size(); i++) {
				if (cvo.getFlag() == 1) {
					if (((BankreceiptVO) banks.get(i)).getflag().intValue() > 1) {
						continue;
					}
				} else if (cvo.getFlag() == 0) {
					if (((BankreceiptVO) banks.get(i)).getflag().intValue() > 0) {
						continue;
					}
				}
				if (cvo.getCheck() != null && cvo.getCheck().toString().length() > 0) {
					if (((BankreceiptVO) banks.get(i)).getCheckstyle() != null
							&& ((BankreceiptVO) banks.get(i)).getCheckstyle().equals(cvo.getCheck())) {
						//continue;
					} else {
						continue;

					}
				}
				if (cvo.getCheckno() != null && cvo.getCheckno().toString().length() > 0) {
					if (((BankreceiptVO) banks.get(i)).getPk_check() != null
							&& ((BankreceiptVO) banks.get(i)).getPk_check().equals(cvo.getCheckno())) {
						//continue;
					} else {
						continue;

					}
				}
				/////////////////////////////////
				if (!contrast.getMemo().equals("merge") && cvo.getM_pk_corp() != null && cvo.getM_pk_corp().length > 0) {
					int a = 1;
					for (int j = 0; j < cvo.getM_pk_corp().length; j++) {
						if (((BankreceiptVO) banks.get(i)).getPk_corp() != null
								&& ((BankreceiptVO) banks.get(i)).getPk_corp().toString().trim().equals(
										cvo.getM_pk_corp()[j])) {
							a = 0;
							break;
						}
					}
					if (a == 1) {
						continue;
					}

				}
				////////////////////////////////
				if (cvo.getM_pk_bank() != null && cvo.getM_pk_bank().toString().length() > 0) {
					if (((BankreceiptVO) banks.get(i)).getPk_bank() != null
							&& !((BankreceiptVO) banks.get(i)).getPk_bank().equals(cvo.getM_pk_bank())) {
						continue;
					}
				}
				if (cvo.getM_pk_sub() != null && cvo.getM_pk_sub().toString().length() > 0) {
					if (((BankreceiptVO) banks.get(i)).getPk_subject() != null
							&& ((BankreceiptVO) banks.get(i)).getPk_subject().equals(cvo.getM_pk_sub())) {
						//continue;
					} else {
						continue;
					}
				}

				////////////////////////////////
				//金额方向过滤
				if (cvo.getAspect() != -1) {
					if (cvo.getAspect() == 0) {
						if (((BankreceiptVO) banks.get(i)).getDebitamount() != null && ((BankreceiptVO) banks.get(i)).getDebitamount().doubleValue() == 0.0d) {
							continue;
						}
					}
					if (cvo.getAspect() == 1) {
						if (((BankreceiptVO) banks.get(i)).getCreditamount() != null && ((BankreceiptVO) banks.get(i)).getCreditamount().doubleValue() == 0.0d) {
							continue;
						}
					}
				}
				//金额过滤
				if (cvo.getArea1() != null && cvo.getArea1().toString().length() > 0) {
					UFDouble temp = new UFDouble(0);
					UFDouble temp1 = new UFDouble(cvo.getArea1().toString());
					if (((BankreceiptVO) banks.get(i)).getDebitamount() != null && ((BankreceiptVO) banks.get(i)).getDebitamount().doubleValue() != 0.0d) {
						temp = ((BankreceiptVO) banks.get(i)).getDebitamount();
					} else if (((BankreceiptVO) banks.get(i)).getCreditamount().doubleValue() != 0.0d) {
						temp = ((BankreceiptVO) banks.get(i)).getCreditamount();
					}
					if (temp.sub(temp1).doubleValue() < 0.0d) {
						continue;
					}
				}
				//金额过滤
				if (cvo.getArea2() != null && cvo.getArea2().toString().length() > 0) {
					UFDouble temp = new UFDouble(0);
					UFDouble temp2 = new UFDouble(cvo.getArea2().toString());
					if (((BankreceiptVO) banks.get(i)).getDebitamount() != null && ((BankreceiptVO) banks.get(i)).getDebitamount().doubleValue() != 0.0d) {
						temp = ((BankreceiptVO) banks.get(i)).getDebitamount();
					} else if (((BankreceiptVO) banks.get(i)).getCreditamount().doubleValue() != 0.0d) {
						temp = ((BankreceiptVO) banks.get(i)).getCreditamount();
					}
					if (temp.sub(temp2).doubleValue() > 0.0d) {
						continue;
					}
				}
				Vector vo = new Vector();
				//getUIRefPane1Bankjs().setPK(((BankreceiptVO)banks.get(i)).getCheckstyle());

				if (ifManyUnit == 2) {
					String pk_link = ((BankreceiptVO) banks.get(i)).getMemo(); //来源主键
					vo.addElement(getLinkComBox1().getLinkMemo(pk_link));
				}

				vo.addElement(((BankreceiptVO) banks.get(i)).getCheckdate());
				vo.addElement(((BankreceiptVO) banks.get(i)).getExplanation());
				//vo.addElement(getUIRefPane1Bankjs().getRefName());
				vo.addElement(((BankreceiptVO) banks.get(i)).getM_jsfsh());
				vo.addElement(((BankreceiptVO) banks.get(i)).getPk_check());
				vo.addElement(((BankreceiptVO) banks.get(i)).getDebitamount());
				vo.addElement(((BankreceiptVO) banks.get(i)).getCreditamount());
				vo.addElement(((BankreceiptVO) banks.get(i)).getM_balance());
				vo.addElement(((BankreceiptVO) banks.get(i)).getNetbanknumber());
				vo.addElement(((BankreceiptVO) banks.get(i)).getPk_bankreceipt());
				vo.addElement(((BankreceiptVO) banks.get(i)).getflag().intValue() == 0 ? "N" : "Y");
				vo.addElement(((BankreceiptVO) banks.get(i)).getM_unitname());
				if (contrast.getSource().intValue() == 1) {
					vo.addElement(((BankreceiptVO) banks.get(i)).getM_subject());
				} else {
					vo.addElement(((BankreceiptVO) banks.get(i)).getM_bank());
				}
				bankV.addElement(vo.clone());
			}
			m_mTableModelBank.setDataVector(bankV);
		} else {
			m_mTableModelBank.setDataVector(new Vector());
		}
		m_tUITableBank.getSelectionModel().clearSelection();//add by zhaozh 0806
		setInterfaceBank(nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801", "UPP2004365801-000209")/*@res "借方"*/);
		setInterfaceBank(nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801", "UPP2004365801-000210")/*@res "贷方"*/);
		if (ifManyUnit == 1 || linkVO.length == 1 || cvo.getM_pk_link() != null) {
			setInterfaceBank(nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801", "UPP2004365801-000213")/*@res "余额"*/);
		}
	}

	/**
	 * 计算余额
	 * 创建日期：(2001-8-23 16:22:22)
	 */
	private void setBalanceVO() {
		if (banks == null) {
			return;
		}
		UFDouble balance = new UFDouble(0);
		UFDouble bal = new UFDouble(0);
		if (contrast.getContrastaspect().intValue() == 0 && contrast.getDebitamount() != null) {
			balance = new UFDouble(nowB);
		} else if (contrast.getCreditamount() != null) {
			balance = new UFDouble(nowB);
		}
		Object cTemp = null;
		Object dTemp = null;
		for (int i = 0; i < banks.size(); i++) {
			cTemp = ((BankreceiptVO) banks.get(i)).getCreditamount();
			dTemp = ((BankreceiptVO) banks.get(i)).getDebitamount();
			if (contrast.getContrastaspect().intValue() == 0) {
				if (cTemp != null && new UFDouble(cTemp.toString()).doubleValue() != 0.0) {
					bal = new UFDouble(cTemp.toString());
					balance = new UFDouble(balance.sub(bal).doubleValue(),getPrecision(""));
					((BankreceiptVO) banks.get(i)).setM_balance(balance);

				} else if (dTemp != null && new UFDouble(dTemp.toString()).doubleValue() != 0.0) {
					bal = new UFDouble(dTemp.toString());
					balance = new UFDouble(balance.add(bal).doubleValue(),getPrecision(""));
					((BankreceiptVO) banks.get(i)).setM_balance(balance);
				}
			} else {
				if (cTemp != null && new UFDouble(cTemp.toString()).doubleValue() != 0.0) {
					bal = new UFDouble(cTemp.toString());
					balance = new UFDouble(balance.add(bal).doubleValue(),getPrecision(""));
					((BankreceiptVO) banks.get(i)).setM_balance(balance);
				} else if (dTemp != null && new UFDouble(dTemp.toString()).doubleValue() != 0.0) {
					bal = new UFDouble(dTemp.toString());
					balance = new UFDouble(balance.sub(bal).doubleValue(),getPrecision(""));
					((BankreceiptVO) banks.get(i)).setM_balance(balance);
				}
			}

		}
	}

	/**
	 * 小数位，格式化
	 * 创建日期：(2001-8-23 16:22:22)
	 */
	private void setInterfaceBank(String str) {
		try {
			NumberFormat fx = NumberFormat.getNumberInstance();
			int pre = getPrecision(str);
			fx.setMaximumFractionDigits(pre);
			fx.setMinimumFractionDigits(pre);
			for (int i = 0; i < m_mTableModelBank.getRowCount(); i++) {
				int iColumnIndex = m_tUITableBank.getColumnModel().getColumnIndex(str);
				Object oTemp = m_mTableModelBank.getValueAt(i, iColumnIndex);
				UFDouble dTemp = null;
				String sTemp = null;
				if (oTemp != null) {
					dTemp = new UFDouble(oTemp.toString());
					if (dTemp.doubleValue() == 0.00) {
						sTemp = null;
					} else {
						dTemp = dTemp.setScale(pre, UFDouble.ROUND_HALF_UP);
						sTemp = fx.format(dTemp);
					}
				} else {
					sTemp = null;
				}
				m_mTableModelBank.setValueAt(sTemp, i, iColumnIndex);
			}
		} catch (Throwable e) {
			Logger.error(e.getMessage(), e);
			MessageDialog.showErrorDlg(this, null, e.getMessage());
		}
	}

	/**
	 * 设置币种位数
	 * 创建日期：(2002-4-10 13:49:03)
	 * @param newPrecision int
	 */
	public void setPrecision(String key) {
		try {
			precision = Currency.getCurrDigit(key);
		} catch (Throwable e) {
			Logger.error(e.getMessage(), e);
			MessageDialog.showErrorDlg(this, null, e.getMessage());
		}

	}

	/**
	 * 函数功能:
	 *
	 * 参数:
	 *
	 * 返回值:
	 *
	 * 异常:
	 *
	 *
	 */
	public void setRefWhereParts(String sPk_corp) {
		if (contrast == null || sPk_corp == null) {
			return;
		}
		getUIRefPane1Bankjs().getRefModel().setWherePart(" ( pk_corp = '" + sPk_corp + "' or pk_corp = '0001') ");
		getUIRefPane1Bankjs().getRefModel().reloadData();
		getUIRefPane1Bankzy().getRefModel().setPk_corp(sPk_corp);//setWherePart(" (groupid = '" + sPk_corp + "' or groupid = '0001') ");
		getUIRefPane1Bankzy().getRefModel().reloadData();
	}

	/**
	 * 参照内容变化事件监听者必须实现的接口方法
	 * @param event valueChangedEvent 参照内容变化事件
	 */
	public void valueChanged(ValueChangedEvent event) {
		if (status == 2 || status == 1) {
			Object o = event.getSource();
			String sControlName = ((UIRefPane) o).getName();
			Object oCode = event.getNewValue();
			String sCode = "";
//			String sRefName = "";
			if (oCode != null) {
				sCode = oCode.toString();
			} else {
				return;
			}
//			if (sControlName.equals(getUIRefPane1Bankzy().getName())) {
//				//银行摘要参照
//				if (sCode.trim().length() > 0) {
//					if (getUIRefPane1Bankzy().getRefName() == null) {
//						sRefName = "";
//					} else {
//						sRefName = getUIRefPane1Bankzy().getRefName();
//					}
//				}
//			}
			if (sControlName.equals(getUIRefPane1Bankjs().getName())) {
				//银行结算方式参照
				int iSelectRow = m_tUITableBank.getSelectedRow();
				int iColumnIndex = bankColumn.getCountByName(nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801",
						"UPP2004365801-000072")/*@res "结算方式"*/);
				if (sCode.trim().length() > 0) {
					//getUIRefPane1Bankjs().setBlurValue(sCode);
//					if (getUIRefPane1Bankjs().getRefName() == null) {
//						sRefName = "";
//					} else {
//						sRefName = getUIRefPane1Bankjs().getRefName();
//					}
					m_mTableModelBank.setValueAt(sCode, iSelectRow, iColumnIndex);
				} else {
					m_mTableModelBank.setValueAt(null, iSelectRow, iColumnIndex);
				}
			}
		}
	}

	public boolean onClosing() {
		if (status == 1 || status == 2) {
			int re = MessageDialog.showYesNoCancelDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("200235",
					"UPP200235-100275")/*@res* "提示"*/, nc.ui.ml.NCLangRes.getInstance().getStrByID("200235",
					"UPP200235-100278")/*
			 * @res
			 * "数据尚未保存，要保存并退出吗"
			 */);
			if (re == MessageDialog.ID_YES) {
				onButtonSave();
				if (status == 0)
					return true;
				else
					return false; //说明没有满足输入条件，没有保存成功
			} else if (re == MessageDialog.ID_NO)
				return true;
			else
				//取消
				return false;
		}
		return true;
	}

	/**
	 * @author zhaozh
	 * 2008-6-2 下午02:39:06
	 * 从EXCEL表中导入银行对帐单
	 * 修改日期：(2008-10-21 下午08:01:27)
	 * 	多单位合并对账支持导入银行对账单
	 */
//	private void onButtonImport() {
//		if(ifManyUnit != 1 && !isMergeAccount()){
//			showWarningMessage(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("contrast","UPPcontrast-000059")/*@res "只支持本单位对账单的导入！"*/);
//			return;
//		}
//		BankreceiptVO[] banks = null;
//		ImportExcelDialog dlg = new ImportExcelDialog(this);
//		String[] banksPK = null;
//		UFDouble creditAmount = new UFDouble();
//		UFDouble debitAmount = new UFDouble();
//		UFDouble diffAmount = new UFDouble();
//		String da = "";
//		String ca = "";
//		String dia = "";
//		//本单位导入银行对帐单
//		if (dlg.showModal() == UIDialog.ID_OK && (ifManyUnit == 1 || isMergeAccount())) {
//			banks = dlg.importFromExcel(contrast);
//			if (banks != null && banks.length > 0) {
//				try {
//					banksPK = getBankService().insertBanks(banks);
//				} catch (BusinessException e) {
//					Logger.error(e.getMessage(), e);
//					MessageDialog.showWarningDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801",
//							"UPP2004365801-000027")/* @res "提示" */, nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("contrast","UPPcontrast-000060")/*@res "导入失败！\n"*/ + e.getMessage());
//				}
//			}
//			if(banks != null && banks.length>0){
//				for (int i = 0; i < banks.length; i++) {
//					if (banks[i].getCreditamount() != null)
//						creditAmount = creditAmount.add(banks[i].getCreditamount());
//					if (banks[i].getDebitamount() != null)
//						debitAmount = debitAmount.add(banks[i].getDebitamount());
//				}
//				if (contrast.getContrastaspect() == 0) {
//					diffAmount = debitAmount.sub(creditAmount);
//				} else
//					diffAmount = creditAmount.sub(debitAmount);
//				int pre = getPrecision("");
//				NumberFormat nf = NumberFormat.getNumberInstance();
//				nf.setMaximumFractionDigits(pre);
//				nf.setMinimumFractionDigits(pre);
//				da = nf.format(debitAmount);
//				ca = nf.format(creditAmount);
//				dia = nf.format(diffAmount);
//			}
//		}
//		if (banksPK != null && banksPK.length >= 0) {
//			MessageDialog.showWarningDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801",
//					"UPP2004365801-000027")/* @res "提示" */, "共导入" + (banks == null ? 0 : banksPK.length)
//					+ "条数据！\n借方合计：" + da + "\n贷方合计:" + ca + "\n差额：" + dia);
//			reQuery("ALL");
//		} else {
//			MessageDialog.showWarningDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801",
//					"UPP2004365801-000027")/* @res "提示" */, nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("contrast","UPPcontrast-000061")/*@res "导入0条数据！"*/);
//		}
//	}

	/**
	 * 重新刷新数据
	 * @author zhaozh
	 * 2008-6-5 下午04:31:06
	 * @param type
	 */
	private void reQuery(String type) {
		if(query == null) return;
		ContrastQueryVO q = new ContrastQueryVO();
		contrast = query.getContrast();
		cvo = query.getQueryVO();
		try {
//			String nowYear = ((IContrastAccountPrv) NCLocator.getInstance().lookup(IContrastAccountPrv.class.getName()))
//					.getCarry(contrast.getPk_contrastaccount());
			String nowYear = context.getCarry(contrast.getPk_contrastaccount()).toString();
			if (!nowYear.equals(contrast.getYears())) {
				showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801", "UPP2004365801-000247")/*@res "帐户已经结转！不能操作！"*/);
				m_bButtonAppend.setEnabled(false);
				//m_bButtonInsert.setEnabled(false);
				m_bButtonEdit.setEnabled(false);
				m_bButtonDelete.setEnabled(false);
				m_bButtonSave.setEnabled(false);
				m_bButtonCancel.setEnabled(false);
				m_bButtonInput.setEnabled(false);
				setButtons(m_aryButtonGroup);
				return;
			}
		} catch (Throwable e) {
			Logger.error(e.getMessage(), e);
			this.showHintMessage("BankReceiptClientUI:" + e.getMessage());
			return;
		}
		cvo.setInit(ContrastQueryVO.NOTINIT);
		q.setContrastaccount(cvo.getContrastaccount());
		q.setYear(ce.getAccountYear());
		q.setDate1(type.equals("ALL")?null:cvo.getDate1());
		q.setDate2(type.equals("ALL")?null:cvo.getDate2());
		q.setInit(ContrastQueryVO.NOTINIT);
		q.setFlag(ContrastQueryVO.VERIFY);
		q.setVoucher(contrast.getSource().intValue());
		q.setOnlyWrongRec(cvo.getOnlyWrongRec());

		UFDouble b = new UFDouble(0);
		if (cvo != null) {
			try {
				banks.removeAllElements();
				if (q.getDate1() == null) {
					q.setDate1(contrast.getStartdate());

				}
				if (q.getDate2() == null) {
					q.setDate2(period.getPeriod(ce.getAccountYear()).getEnddate());

				}
//				linkVO = ((IContrastAccountPrv) NCLocator.getInstance().lookup(IContrastAccountPrv.class.getName()))
//						.queryAccountLink(contrast.getPk_contrastaccount(), ce.getAccountYear());
				linkVO = ArrayUtil.changeListtovos((context.getLinks(contrast.getPk_contrastaccount())),AccountlinkVO.class); 
				if (contrast.getMemo() != null && contrast.getMemo().trim().equals("one")) {
					ifManyUnit = 1;
					ifoldAccount = false;
					contrast.setM_pk_link(linkVO[0].getPk_accountlink());
				} else if (contrast.getMemo() != null && contrast.getMemo().trim().equals("many")) {
					ifManyUnit = 2;
					ifoldAccount = false;
				} else {
					ifManyUnit = 1;
					ifoldAccount = true;
				}
				q.setManyCorp(ifManyUnit);
				if(!contrast.getMemo().equals("merge") && query.getLinkComBox1().getSelectLinks() != null)
					q.setM_pk_link(query.getLinkComBox1().getSelectLinks().getPk_accountlink());
				/***********************************************/
				BankreceiptVO[] vt = getBankService().queryBankConditionInit(q);
				getLinkComBox1().setData(q.getContrastaccount(), true);
				/*********************************************************/
				if (cvo.getDate1() != null) {
					b = ContrastServiceFactory.getAccountService().getBankMoney(cvo.getContrastaccount(), ce.getAccountYear(), cvo.getDate1(), 1);
				}
				///////////////////////////////
				//设置精度
				setPrecision(contrast.getPk_currtype());
				setInterfaceBank(nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801", "UPP2004365801-000209")/*@res "借方"*/);
				setInterfaceBank(nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801", "UPP2004365801-000210")/*@res "贷方"*/);
				getUILabel1().setText(
						nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801", "UPP2004365801-000248")/*@res "启动日期："*/
								+ query.getContrast().getStartdate());
				getUILabel3().setText(
						nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801", "UPP2004365801-000249")/*@res "    账户："*/
								+ query.getContrast().getContrastaccountname());
				NumberFormat fx = NumberFormat.getNumberInstance();
				int pre = getPrecision("");
				fx.setMaximumFractionDigits(pre);
				fx.setMinimumFractionDigits(pre);
				if (ifManyUnit == 2 && cvo.getM_pk_link() != null) {
					for (int i = 0; i < linkVO.length; i++) {
						if (cvo.getM_pk_link().equals(linkVO[i].getPk_accountlink())) {
							query.getContrast()
									.setDebitamount(
											linkVO[i].getDebitamount() == null ? new UFDouble(0.0) : linkVO[i]
													.getDebitamount());
							query.getContrast().setCreditamount(
									linkVO[i].getCreditamount() == null ? new UFDouble(0.0) : linkVO[i]
											.getCreditamount());
							break;
						}
					}
				}
				if (new UFDouble(query.getContrast().getCreditamount().toString()).doubleValue() == 0.0
						&& new UFDouble(query.getContrast().getDebitamount().toString()).doubleValue() == 0.0) {
					if (contrast.getContrastaspect().intValue() == 1) {
						b = new UFDouble(0).sub(b);
					}
					UFDouble dTemp = query.getContrast().getDebitamount().add(b);
					nowB = new UFDouble(dTemp);
					String sTemp = fx.format(dTemp);
					getUILabel2().setText(
							nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801", "UPP2004365801-000250")/*@res "    期初余额："*/
									+ sTemp);

				} else if (query.getContrast().getContrastaspect().intValue() == 0
						&& query.getContrast().getDebitamount() != null
						&& new UFDouble(query.getContrast().getCreditamount().toString()).doubleValue() == 0.0) {
					UFDouble dTemp = query.getContrast().getDebitamount().add(b);
					nowB = new UFDouble(dTemp);
					String sTemp = fx.format(dTemp);
					getUILabel2().setText(
							nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801", "UPP2004365801-000250")/*@res "    期初余额："*/
									+ sTemp);
				} else if (query.getContrast().getContrastaspect().intValue() == 1
						&& query.getContrast().getCreditamount() != null
						&& new UFDouble(query.getContrast().getDebitamount().toString()).doubleValue() == 0.0) {

					UFDouble dTemp = query.getContrast().getCreditamount().sub(b);
					nowB = new UFDouble(dTemp);
					String sTemp = fx.format(dTemp);
					getUILabel2().setText(
							nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801", "UPP2004365801-000250")/*@res "    期初余额："*/
									+ sTemp);

				} else {
					nowB = new UFDouble(0);
					getUILabel2()
							.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801", "UPP2004365801-000251")/*@res "    期初余额： 0"*/);
				}
				//////////////////////////////////////////////
				if (vt != null && vt.length > 0) {
					int count = vt.length;
					for (int i = 0; i < count; i++) {
						banks.add(vt[i]);
					}
					setBalanceVO();
				}
				//////////////////////

				/////////////////
				initTable();
				refresh();
			} catch (Throwable e) {
				Logger.error(e.getMessage(), e);
				MessageDialog.showErrorDlg(this, null, e.getMessage());
			}
		}

		setInterfaceBank(nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801", "UPP2004365801-000209")/*@res "借方"*/);
		setInterfaceBank(nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801", "UPP2004365801-000210")/*@res "贷方"*/);
		if (ifManyUnit == 1 || linkVO.length == 1 || cvo.getM_pk_link() != null) {
			setInterfaceBank(nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801", "UPP2004365801-000213")/*@res "余额"*/);
		}
		m_bButtonAppend.setEnabled(true);
		m_bButtonCancel.setEnabled(false);
		m_bButtonDelete.setEnabled(true);
		m_bButtonDeleteAll.setEnabled(true);
		m_bButtonEdit.setEnabled(true);
		m_bButtonSave.setEnabled(false);
		if (ifManyUnit == 1)
			m_bButtonImport.setEnabled(true);//add by zhaozh
		//v31newneed:现金银行－银行对账－银行对账单，增加以下控制：初始化设置中“开户银行账号”字段为空的账户，才可以引入结算中心的结算凭证数据；初始化设置中“开户银行账号”字段非空的账户，不能引入结算中心的结算凭证数据。
		if (this.linkVO != null && this.linkVO.length > 0 && this.linkVO[0].getM_pk_khAccount() != null) {
			m_bButtonInput.setEnabled(false);
		} else {
			m_bButtonInput.setEnabled(true);
		}
		setButtons(m_aryButtonGroup);
	}

	public static IBankReceiptPrv getBankService() {
		if(bankService == null)
			bankService = NCLocator.getInstance().lookup(IBankReceiptPrv.class);
		return bankService;
	}

	public Accperiod getPeriod() {
		return period;
	}
}
