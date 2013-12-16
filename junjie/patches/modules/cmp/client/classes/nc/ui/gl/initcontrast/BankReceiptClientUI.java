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
 * ���ж��˵�
 * �������ڣ�(2002-1-22 11:46:01)
 * @author����Ǭ
 */
@SuppressWarnings({"unchecked","serial","deprecation"})
public class BankReceiptClientUI extends nc.ui.pub.ToftPanel implements ICalTableSelectionListener,
		nc.ui.pub.beans.ValueChangedListener, java.awt.event.KeyListener, java.awt.event.ItemListener {
	/////////////////////////////////////////////////////////////
	//���ð�ť

	///
	//Ctrl+Ins				����һ�м�¼
	//Ctrl+Del				ɾ��һ�м�¼
	//Ctrl+S				����
	//Ctrl+E               �޸�
	//Ctrl+D				ɾ��
	//Ctrl+F				��ѯ
	//Ctrl+P				��ӡ
	//Ctrl+R				ˢ��
	private ButtonObject m_bButtonQuery = new ButtonObject(nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801",
			"UPP2004365801-000057")/*@res "��ѯ"*/, nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801",
			"UPP2004365801-000057")/*@res "��ѯ"*/, 2, "��ѯ"); /*-=notranslate=-*/
	private ButtonObject m_bButtonAppend = new ButtonObject(nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801",
			"UPP2004365801-000215")/*@res "����"*/, nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801",
			"UPP2004365801-000215")/*@res "����"*/, 2, "����"); /*-=notranslate=-*/
	private ButtonObject m_bButtonEdit = new ButtonObject(nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801",
			"UPP2004365801-000216")/*@res "�޸�"*/, nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801",
			"UPP2004365801-000216")/*@res "�޸�"*/, 2, "�޸�"); /*-=notranslate=-*/
	private ButtonObject m_bButtonSave = new ButtonObject(nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801",
			"UPP2004365801-000053")/*@res "����"*/, nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801",
			"UPP2004365801-000053")/*@res "����"*/, 2, "����"); /*-=notranslate=-*/
	private ButtonObject m_bButtonCancel = new ButtonObject(nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801",
			"UPP2004365801-000021")/*@res "ȡ��"*/, nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801",
			"UPP2004365801-000021")/*@res "ȡ��"*/, 2, "ȡ��"); /*-=notranslate=-*/
	private ButtonObject m_bButtonDelete = new ButtonObject(nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801",
			"UPP2004365801-000217")/*@res "ɾ��"*/, nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801",
			"UPP2004365801-000217")/*@res "ɾ��"*/, 2, "ɾ��"); /*-=notranslate=-*/
	private ButtonObject m_bButtonDeleteAll = new ButtonObject(nc.ui.ml.NCLangRes.getInstance().getStrByID(
			"2004365801", "UPP2004365801-000218")/*@res "����ɾ��"*/, nc.ui.ml.NCLangRes.getInstance().getStrByID(
			"2004365801", "UPP2004365801-000218")/*@res "����ɾ��"*/, 2, "����ɾ��"); /*-=notranslate=-*/
	private ButtonObject m_bButtonInput = new ButtonObject(NCLangRes4VoTransl.getNCLangRes().getStrByID("contrast", "UPPcontrast-000098")/*"���ʽ���֯����"*/,
			NCLangRes4VoTransl.getNCLangRes().getStrByID("contrast", "UPPcontrast-000098")/*"���ʽ���֯����"*/, 2, "���ʽ���֯����"); /*-=notranslate=-*/
	private ButtonObject m_bButtonPrint = new ButtonObject(nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801",
			"UPP2004365801-000220")/*@res "��ӡ"*/, nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801",
			"UPP2004365801-000220")/*@res "��ӡ"*/, 2, "��ӡ"); /*-=notranslate=-*/
	private ButtonObject m_bButtonDirectPrint = new ButtonObject(nc.ui.ml.NCLangRes.getInstance().getStrByID("contrast",
	"UPPcontrast-000130")/*@res"ֱ�Ӵ�ӡ"*/,nc.ui.ml.NCLangRes.getInstance().getStrByID("contrast",
	"UPPcontrast-000130")/*@res"ֱ�Ӵ�ӡ"*/,2,"ֱ�Ӵ�ӡ");
	private ButtonObject m_bButtonTemplatePrint = new ButtonObject(nc.ui.ml.NCLangRes.getInstance().getStrByID("contrast",
	"UPPcontrast-000131")/*@res"ģ���ӡ"*/,nc.ui.ml.NCLangRes.getInstance().getStrByID("contrast",
	"UPPcontrast-000131")/*@res"ģ���ӡ"*/,2,"ģ���ӡ");
	private ButtonObject m_bButtonRefresh = new ButtonObject(nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801",
	"UPP2004365801-000266")/* @res "ˢ��" */, nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801",
	"UPP2004365801-000267")/* @res "ˢ������" */, 2, "ˢ��"); /*-=notranslate=-*/
	//add by zhaozh
	private ButtonObject m_bButtonImport = new ButtonObject(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("2004","UPP2004-000228")/*@res "����"*/, nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("2004","UPP2004-000229")/*@res "����EXCEL��"*/, 2, "����");	/*-=notranslate=-*/
	private ButtonObject m_bButtonExport = new ButtonObject(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("2004","UPP2004-000230")/*@res "����ģ��"*/, nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("2004","UPP2004-000231")/*@res "�������ж��ʵ�EXCEL��ʽ"*/, 2, "�������ʵ���ʽ");	/*-=notranslate=-*/
	private ButtonObject m_bFileImprot = new ButtonObject(NCLangRes4VoTransl.getNCLangRes().getStrByID("contrast", "UPPcontrast-000097")/*"�ļ�����"*/,
			NCLangRes4VoTransl.getNCLangRes().getStrByID("contrast", "UPPcontrast-000097")/*"�ļ�����"*/,2,nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("2004","UPP2004-000233")/*@res "�ļ�����"*/);
//	private ButtonObject[] m_aryButtonGroup = { m_bButtonQuery, m_bButtonAppend, m_bButtonEdit, m_bButtonDelete,
//			m_bButtonSave, m_bButtonCancel, m_bButtonInput, m_bButtonPrint, m_bButtonImport,//���ӵ��밴ť
//			m_bButtonExport //���ӵ�����ť��������
//	};
//	private ButtonObject[] m_aryButtonGroup = { m_bButtonQuery, m_bButtonAppend, m_bButtonEdit, m_bButtonDelete,
//			m_bButtonSave, m_bButtonCancel, m_bButtonInput, m_bButtonPrint, m_bButtonImport,//���ӵ��밴ť
//	m_bButtonExport,m_bFileImprot
//	};
	private ButtonObject[] m_aryButtonGroup = { m_bButtonQuery,m_bButtonAppend, m_bButtonEdit, m_bButtonDelete,m_bButtonSave,m_bButtonCancel,m_bFileImprot,m_bButtonInput,m_bButtonPrint,m_bButtonRefresh};
//	private String m_sCorpID = "0001"; //��λ���� Ĭ��Ϊ0001
	private String m_sTitle = nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801", "UPP2004365801-000045")/*@res "���ж��˵�"*/; //���ñ���
	//�б�������

	private CalUITable m_tUITableBank = null; //���
	private CalTableModel m_mTableModelBank = null; //���ģ��

	private QueryUI query = null;
	private ImportFundOrgDlg input = null;

	private GlContrastVO contrast = null; //��ǰ�˻�
	private Vector banks = new Vector(); //
	AccountlinkVO[] linkVO = null;

	private int status = 0; //״̬ 0 ��� 1 ��� 2 �༭
	private int bankRow = 0; //���е�ǰ�༭��

	private ContrastQueryVO cvo = null; //��ѯ����

	private UFDouble nowB = new UFDouble(0);
	private ClientEnvironment ce = ClientEnvironment.getInstance();

	private int precision = 2;
	private Accperiod period = null;

	//���б༭����
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

	int ifManyUnit = 1; //�˻��Ƿ�൥λ����
	boolean ifoldAccount = true; //�˻�2.3��ǰ�˻�
	private LinkComBox ivjLinkComBox1 = null;

	private ColumnName bankColumn = null;

	//��¼�����ӵ�����
	private int addRows = 0;
	
	private ContrastContext context = ContrastContext.getInstance();
	
	/**
	 * BankReceiptClientUI ������ע�⡣
	 */
	public BankReceiptClientUI() {
		super();
		initialize();
	}

	/**
	 * ��������:���༭����
	 *
	 *  ����:
	 * 		MyNCTableSelectionEvent e ----- �¼�

	 * ����ֵ:
	 *
	 * �쳣:
	 *
	 */
	public void afterEdit(CalTableSelectionEvent e) {
		int iColumn = bankColumn.getCountByName(nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801",
				"UPP2004365801-000071")/*@res "ժҪ"*/);
		;
		if (iColumn == e.getColumn()) {
			m_mTableModelBank.setValueAt(getUIRefPane1Bankzy().getRefName() == null ? getUIRefPane1Bankzy()
					.getUITextField().getText() : getUIRefPane1Bankzy().getRefName(), e.getRow(), e.getColumn());
		}
		iColumn = bankColumn.getCountByName(nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801",
				"UPP2004365801-000075")/*@res "����"*/);
		if (iColumn == e.getColumn()) {
			m_mTableModelBank.setValueAt(getUIRefPane1Bankdate().getRefName() == null ? getUIRefPane1Bankdate()
					.getUITextField().getText() : getUIRefPane1Bankdate().getRefName(), e.getRow(), e.getColumn());
		}
	}

	/**
	 * 	��������:���仯��������
	 *
	 *  ����:
	 * 		MyNCTableSelectionEvent e ----- �¼�

	 * 	����ֵ:
	 *
	 * 	�쳣:
	 *
	 */
	public void CalTableSelectionChanged(CalTableSelectionEvent e) {

	}

	/**
	 * ��������:
	 *
	 *  ����:
	 * 		MyNCTableSelectionEvent e ----- �¼�

	 * ����ֵ:
	 *
	 * �쳣:
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
					"UPP2004365801-000027")/*@res "��ʾ"*/, nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801",
					"UPP2004365801-000221")/*@res "�跽 ��������ͬʱΪ�գ�"*/);
			return false;
		}
		if (deb.doubleValue() != 0 && cre.doubleValue() != 0) {
			MessageDialog.showWarningDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801",
					"UPP2004365801-000027")/*@res "��ʾ"*/, nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801",
					"UPP2004365801-000222")/*@res "�跽 ��������ͬʱ������"*/);
			return false;
		}
		///////////////////////
		//����Ŀ�͹�˾�Ĺ�ϵ
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
							"UPP2004365801-000027")/*@res "��ʾ"*/, nc.ui.ml.NCLangRes.getInstance().getStrByID(
							"2004365801", "UPP2004365801-000223")/*@res "��λ����Ŀ��ϵ�������˻����ã�"*/);
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
							"UPP2004365801-000027")/*@res "��ʾ"*/, nc.ui.ml.NCLangRes.getInstance().getStrByID(
							"2004365801", "UPP2004365801-000224")/*@res "��λ���˻���ϵ�������˻����ã�"*/);
					return false;
				}
			}
		}
		////////////////////
		return true;
	}

	/**
	 * ��������:
	 *
	 *  ����:
	 * 		MyNCTableSelectionEvent e ----- �¼�

	 * ����ֵ:
	 *
	 * �쳣:
	 *
	 */
	public BankreceiptVO getBankVO(boolean source, int row) {
		BankreceiptVO vo = new BankreceiptVO();
		///��֯vo
		vo.setPk_contrastaccount(contrast.getPk_contrastaccount()); //�˻�����
		int iColumnIndex = 0;
		if (!ifoldAccount) {
			AccountlinkVO linkTemp = null;
			//����Ƕ൥λ����--v31
			if (this.ifManyUnit == 2) {
				iColumnIndex = m_tUITableBank
						.getColumnModel()
						.getColumnIndex(
								nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801", "UPP2004365801-000074")/*@res "���˻�"*/);
				if (m_mTableModelBank.getValueAt(row, iColumnIndex) == null
						|| m_mTableModelBank.getValueAt(row, iColumnIndex).toString().length() < 1) {
					MessageDialog.showWarningDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801",
							"UPP2004365801-000027")/*@res "��ʾ"*/, nc.ui.ml.NCLangRes.getInstance().getStrByID(
							"2004365801", "UPP2004365801-000225")/*@res "����¼����Դ��Ϣ��"*/);
					return null;
				}
				linkTemp = getLinkComBox1().getSelectLinks(m_mTableModelBank.getValueAt(row, iColumnIndex).toString());
			} else {
				linkTemp = getLinkComBox1().getSelectLinks();
			}
			if (linkTemp == null) {
				MessageDialog.showWarningDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801",
						"UPP2004365801-000027")/*@res "��ʾ"*/, nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"2004365801", "UPP2004365801-000225")/*@res "����¼����Դ��Ϣ��"*/);
				return null;
			} else {
				vo.setPk_corp(linkTemp.getPk_corp());
				vo.setMemo(linkTemp.getPk_accountlink());
				vo.setPk_subject(linkTemp.getPk_subject());
				vo.setPk_bank(linkTemp.getBankaccount());
				vo.setPk_ass(linkTemp.getPk_ass());
				vo.setM_unitname(linkTemp.getM_corp());

				//m_mTableModelBank.setValueAt(linkTemp.getM_corp(),row,bankColumn.getCountByName("��λ"));
				if (contrast.getSource().intValue() == 1) {
					vo.setM_subject(linkTemp.getM_sub());
					//m_mTableModelBank.setValueAt(linkTemp.getM_sub(),row,bankColumn.getCountByName("��Ŀ"));
				} else {
					vo.setM_bank(linkTemp.getM_bank());
					//m_mTableModelBank.setValueAt(linkTemp.getM_bank(),row,bankColumn.getCountByName("��Ŀ"));
				}
			}
		}
		/*if(ifManyUnit == 1){
		 if(linkVO != null && linkVO.length == 1){
		 vo.setPk_corp(linkVO[0].getPk_corp());
		 if(contrast.getSource().intValue()==1){
		 vo.setPk_subject(linkVO[0].getPk_subject());   //��Ŀ
		 }else{
		 vo.setPk_bank(linkVO[0].getBankaccount());   //�˻�
		 }
		 }
		 }else{
		 iColumnIndex = m_tUITableBank.getColumnModel().getColumnIndex("��λ");
		 if(getUIRefPaneUnit().getRefPK()==null || m_mTableModelBank.getValueAt(row,iColumnIndex)==null || m_mTableModelBank.getValueAt(row,iColumnIndex).toString().length()<1){
		 MessageDialog.showWarningDlg(this,"��ʾ","����¼�뵥λ��");
		 return null;
		 }else{
		 vo.setPk_corp(getUIRefPaneUnit().getRefPK());   //��λ
		 vo.setM_unitname(getUIRefPaneUnit().getRefName());   //��λ
		 }
		 if(contrast.getSource().intValue()==1){
		 iColumnIndex = m_tUITableBank.getColumnModel().getColumnIndex("��Ŀ");
		 if(getUIRefPaneSubject().getRefPK()==null || m_mTableModelBank.getValueAt(row,iColumnIndex)==null || m_mTableModelBank.getValueAt(row,iColumnIndex).toString().length()<1){
		 MessageDialog.showWarningDlg(this,"��ʾ","����¼���Ŀ��");
		 return null;
		 }else{
		 vo.setPk_subject(getUIRefPaneSubject().getRefPK());   //��Ŀ
		 vo.setM_subject(getUIRefPaneSubject().getRefName());   //��Ŀ
		 }
		 }else{
		 iColumnIndex = m_tUITableBank.getColumnModel().getColumnIndex("�˻�");
		 if(getUIRefPaneBank().getRefPK()==null || m_mTableModelBank.getValueAt(row,iColumnIndex)==null || m_mTableModelBank.getValueAt(row,iColumnIndex).toString().length()<1){
		 MessageDialog.showWarningDlg(this,"��ʾ","����¼���˻���");
		 return null;
		 }else{
		 vo.setPk_bank(getUIRefPaneBank().getRefPK());   //�˻�
		 vo.setM_bank(getUIRefPaneBank().getRefName());   //�˻�
		 }
		 }
		 }*/
		iColumnIndex = m_tUITableBank.getColumnModel().getColumnIndex(
				nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801", "UPP2004365801-000075")/*@res "����"*/);
		if (m_mTableModelBank.getValueAt(row, iColumnIndex) == null
				|| m_mTableModelBank.getValueAt(row, iColumnIndex).toString().length() < 1) {
			MessageDialog.showWarningDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801",
					"UPP2004365801-000027")/*@res "��ʾ"*/, nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801",
					"UPP2004365801-000226")/*@res "����¼�����ڣ�"*/);
			return null;
		} else {
			UFDate da = new UFDate(m_mTableModelBank.getValueAt(row, iColumnIndex).toString());
			if (da.before(contrast.getStartdate())) {
				MessageDialog.showWarningDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801",
						"UPP2004365801-000027")/*@res "��ʾ"*/, nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"2004365801", "UPP2004365801-000227")/*@res "��������Ӧ�����������ڣ�"*/);
				return null;
			}
			/*****v31***/
			nc.vo.bd.period.AccperiodVO accperiodvo = null;
			if (contrast.getSource().intValue() == 1) {

				String pkglorgbook = null;
				try {
					//v50--adjust--for-xyj--20051011
					//if(contrast.getMemo()!=null && contrast.getMemo().equals("one") || linkVO.length<=1){
					/**����λ����ʱcontrast.getPk_corp()ȡ���������˲����������൥λ�����ҽ�����һ�������˲���Ŀʱ��
					 * �����÷�֧����contrast.getPk_corp()ȡ���ǵ�¼��˾�Ĺ�˾����������ȡ����ڼ����
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
					//������������жϲ�׼�������Ƕ����ʻ���Ӧ�����˲�Ϊ����Ȼ�»���ڼ�ʱ 07-10-17
					//if(accperiodvo==null || !accperiodvo.getPeriodyear().equals(ce.getAccountYear())){
					if (accperiodvo == null || da.before(accperiodvo.getBegindate())
							|| da.after(accperiodvo.getEnddate())) {
						MessageDialog.showWarningDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801",
								"UPP2004365801-000027")/*@res "��ʾ"*/, nc.ui.ml.NCLangRes.getInstance().getStrByID(
								"2004365801", "UPP2004365801-000228")/*@res "���ڱ����ڵ�½����ڼ���"*/);
						return null;
					}
				} catch (Exception e) {
					Logger.error(e.getMessage(), e);
					MessageDialog.showWarningDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801",
							"UPP2004365801-000027")/*@res "��ʾ"*/, nc.ui.ml.NCLangRes.getInstance().getStrByID(
							"2004365801", "UPP2004365801-000228")/*@res "���ڱ����ڵ�½����ڼ���"*/);
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
							"UPP2004365801-000027")/*@res "��ʾ"*/, nc.ui.ml.NCLangRes.getInstance().getStrByID(
							"2004365801", "UPP2004365801-000228")/*@res "���ڱ����ڵ�½����ڼ���"*/);
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
							"UPP2004365801-000027")/*@res "��ʾ"*/, nc.ui.ml.NCLangRes.getInstance().getStrByID(
							"2004365801", "UPP2004365801-000228")/*@res "���ڱ����ڵ�½����ڼ���"*/);
					return null;
				}
			}
			vo.setCheckdate(new UFDate(m_mTableModelBank.getValueAt(row, iColumnIndex).toString())); //����
			vo.setYears(ce.getAccountYear());
		}

		iColumnIndex = m_tUITableBank.getColumnModel().getColumnIndex(
				nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801", "UPP2004365801-000071")/*@res "ժҪ"*/);
		if (m_mTableModelBank.getValueAt(row, iColumnIndex) == null
				|| m_mTableModelBank.getValueAt(row, iColumnIndex).toString().length() < 1) {
			//MessageDialog.showWarningDlg(this,"��ʾ","����¼��ժҪ��");
			//return null;
		} else {
			vo.setExplanation(m_mTableModelBank.getValueAt(row, iColumnIndex).toString()); //ժҪ
		}

		iColumnIndex = m_tUITableBank.getColumnModel().getColumnIndex(
				nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801", "UPP2004365801-000072")/*@res "���㷽ʽ"*/);
		if (getUIRefPane1Bankjs().getRefPK() == null || m_mTableModelBank.getValueAt(row, iColumnIndex) == null
				|| m_mTableModelBank.getValueAt(row, iColumnIndex).toString().length() < 1) {
			if (m_mTableModelBank.getValueAt(row, iColumnIndex) != null) {
				String key = "";

				key = m_mTableModelBank.getValueAt(
						row,
						bankColumn.getCountByName(nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801",
								"UPP2004365801-000138")/*@res "����"*/)).toString();

				BankreceiptVO bvo = getVO(key);
				vo.setCheckstyle(bvo.getCheckstyle());
				vo.setM_jsfsh(bvo.getM_jsfsh());
			}
			//MessageDialog.showWarningDlg(this,"��ʾ","����¼����㷽ʽ��");
			//return null;
		} else {
			vo.setCheckstyle(getUIRefPane1Bankjs().getRefPK()); //��������
			vo.setM_jsfsh(getUIRefPane1Bankjs().getRefName()); //��������
		}

		iColumnIndex = m_tUITableBank.getColumnModel().getColumnIndex(
				nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801", "UPP2004365801-000073")/*@res "�����"*/);
		if (m_mTableModelBank.getValueAt(row, iColumnIndex) == null
				|| m_mTableModelBank.getValueAt(row, iColumnIndex).toString().length() < 1) {
			//MessageDialog.showWarningDlg(this,"��ʾ","����¼�����ţ�");
			//return null;
		} else {
			vo.setPk_check(m_mTableModelBank.getValueAt(row, iColumnIndex).toString()); //�����
			if (vo.getCheckstyle() == null) {
				showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801", "UPP2004365801-000229")/*@res "ȱ�ٽ��㷽ʽ��"*/);
				//MessageDialog.showWarningDlg(this,"��ʾ","ȱ�ٽ��㷽ʽ��");
				//return null;
			}
		}

		iColumnIndex = m_tUITableBank.getColumnModel().getColumnIndex(
				nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801", "UPP2004365801-000209")/*@res "�跽"*/);
		if (m_mTableModelBank.getValueAt(row, iColumnIndex) == null
				|| m_mTableModelBank.getValueAt(row, iColumnIndex).toString().length() < 1) {
			vo.setDebitamount(new UFDouble(0));
			if (m_mTableModelBank.getValueAt(row, iColumnIndex + 1) == null
					|| m_mTableModelBank.getValueAt(row, iColumnIndex + 1).toString().length() < 1) {
				MessageDialog.showWarningDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801",
						"UPP2004365801-000027")/*@res "��ʾ"*/, nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"2004365801", "UPP2004365801-000230")/*@res "����¼��跽�������"*/);
				return null;
			}
		} else {
			vo.setDebitamount(new UFDouble(m_mTableModelBank.getValueAt(row, iColumnIndex).toString()));
		}

		iColumnIndex = m_tUITableBank.getColumnModel().getColumnIndex(
				nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801", "UPP2004365801-000210")/*@res "����"*/);
		if (m_mTableModelBank.getValueAt(row, iColumnIndex) == null
				|| m_mTableModelBank.getValueAt(row, iColumnIndex).toString().length() < 1) {
			vo.setCreditamount(new UFDouble(0));
		} else {
			vo.setCreditamount(new UFDouble(m_mTableModelBank.getValueAt(row, iColumnIndex).toString()));
		}
		return vo;
	}

	/**
	 * ���� LinkComBox1 ����ֵ��
	 * @return nc.ui.gl.contrastpub.LinkComBox
	 */
	/* ���棺�˷������������ɡ� */
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
	 * �õ�ʵ��vo
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
	 * �õ�����
	 */
	public int getPrecision(String columnName) {
		return precision;
	}

	/**
	 * getTitle ����ע�⡣
	 */
	public String getTitle() {
		return m_sTitle;
	}

	/**
	 * ���� UILabel1 ����ֵ��
	 * @return nc.ui.pub.beans.UILabel
	 */
	/* ���棺�˷������������ɡ� */
	private nc.ui.pub.beans.UILabel getUILabel1() {
		if (ivjUILabel1 == null) {
			try {
				ivjUILabel1 = new nc.ui.pub.beans.UILabel();
				ivjUILabel1.setName("UILabel1");
				ivjUILabel1
						.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801", "UPP2004365801-000231")/*@res "�������ڣ�2002��01��22��"*/);
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
	 * ���� UILabel2 ����ֵ��
	 * @return nc.ui.pub.beans.UILabel
	 */
	/* ���棺�˷������������ɡ� */
	private nc.ui.pub.beans.UILabel getUILabel2() {
		if (ivjUILabel2 == null) {
			try {
				ivjUILabel2 = new nc.ui.pub.beans.UILabel();
				ivjUILabel2.setName("UILabel2");
				ivjUILabel2
						.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801", "UPP2004365801-000232")/*@res "    �ڳ����"*/);
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
	 * ���� UILabel3 ����ֵ��
	 * @return nc.ui.pub.beans.UILabel
	 */
	/* ���棺�˷������������ɡ� */
	private nc.ui.pub.beans.UILabel getUILabel3() {
		if (ivjUILabel3 == null) {
			try {
				ivjUILabel3 = new nc.ui.pub.beans.UILabel();
				ivjUILabel3.setName("UILabel3");
				ivjUILabel3
						.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801", "UPP2004365801-000233")/*@res "    �˻���XXXXXXXXXX"*/);
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
	 * ���� UILabel4 ����ֵ��
	 * @return nc.ui.pub.beans.UILabel
	 */
	/* ���棺�˷������������ɡ� */
	private nc.ui.pub.beans.UILabel getUILabel4() {
		if (ivjUILabel4 == null) {
			try {
				ivjUILabel4 = new nc.ui.pub.beans.UILabel();
				ivjUILabel4.setName("UILabel4");
				ivjUILabel4
						.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801", "UPP2004365801-000234")/*@res "ͼ�����ѹ���  �Ѻ���"*/);
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
	 * �����
	 * ���� UIPanel1 ����ֵ��
	 * @return nc.ui.pub.beans.UIPanel
	 */
	/* ���棺�˷������������ɡ� */
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
	 * ���� UIPanelhead ����ֵ��
	 * @return nc.ui.pub.beans.UIPanel
	 */
	/* ���棺�˷������������ɡ� */
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
	 * ���� UIRefPane11 ����ֵ��
	 * @return nc.ui.pub.beans.UIRefPane
	 */
	/* ���棺�˷������������ɡ� */
	private nc.ui.pub.beans.UIRefPane getUIRefPane1Bankdate() {
		if (ivjUIRefPane1Bankdate == null) {
			try {
				ivjUIRefPane1Bankdate = new nc.ui.pub.beans.UIRefPane();
				ivjUIRefPane1Bankdate.setName("ivjUIRefPane1Bankdate ");
				//ivjUIRefPane1Bankdate.setLocation(88, 77);
				ivjUIRefPane1Bankdate.setRefNodeName("����");
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
	 * ���� UIRefPane11 ����ֵ��
	 * @return nc.ui.pub.beans.UIRefPane
	 */
	/* ���棺�˷������������ɡ� */
	private nc.ui.pub.beans.UIRefPane getUIRefPane1Bankjs() {
		if (ivjUIRefPane1Bankjs == null) {
			try {
				ivjUIRefPane1Bankjs = new nc.ui.pub.beans.UIRefPane();
				ivjUIRefPane1Bankjs.setName("ivjUIRefPane1Bankjs");
				//ivjUIRefPane1Bankdate.setLocation(88, 77);
				ivjUIRefPane1Bankjs.setRefNodeName("���㷽ʽ");
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
	 * ���� UIRefPane11 ����ֵ��
	 * @return nc.ui.pub.beans.UIRefPane
	 */
	/* ���棺�˷������������ɡ� */
	private nc.ui.pub.beans.UIRefPane getUIRefPane1Bankzy() {
		if (ivjUIRefPane1Bankzy == null) {
			try {
				ivjUIRefPane1Bankzy = new nc.ui.pub.beans.UIRefPane();
				ivjUIRefPane1Bankzy.setName("ivjUIRefPane1Bankzy");
				//ivjUIRefPane1Bankdate.setLocation(88, 77);
				ivjUIRefPane1Bankzy.setRefNodeName("����ժҪ");

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
	 * ���� UITablePane1 ����ֵ��
	 * @return nc.ui.pub.beans.UITablePane
	 */
	/* ���棺�˷������������ɡ� */
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

				//��ֹ��������
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
	 * �õ�ʵ��vo
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
	 * ÿ�������׳��쳣ʱ������
	 * @param exception java.lang.Throwable
	 */
	private void handleException(java.lang.Throwable exception) {
		Logger.error(exception.getMessage());
		MessageDialog.showErrorDlg(this, null, exception.getMessage());
	}

	/**
	 * ��ʼ���ࡣ
	 */
	/* ���棺�˷������������ɡ� */
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
							nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801", "UPP2004365801-000075")/*@res "����"*/,
							nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801", "UPP2004365801-000071")/*@res "ժҪ"*/,
							nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801", "UPP2004365801-000072")/*@res "���㷽ʽ"*/,
							nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801", "UPP2004365801-000073")/*@res "�����"*/,
							nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801", "UPP2004365801-000209")/*@res "�跽"*/,
							nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801", "UPP2004365801-000210")/*@res "����"*/,
							nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801", "UPP2004365801-000213")/*@res "���"*/,
							nc.ui.ml.NCLangRes.getInstance().getStrByID("contrast", "UPPcontrast-000116")/*"��ʶ��"*/,
							nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801", "UPP2004365801-000138")/*@res "����"*/,
							nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801", "UPP2004365801-000062")/*@res "����"*/,
							nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801", "UPP2004365801-000157")/*@res "��λ"*/,
							nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801", "UPP2004365801-000172") /*@res "��Ŀ"*/},
					{
							nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801", "UPP2004365801-000074")/*@res "���˻�"*/,
							nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801", "UPP2004365801-000075")/*@res "����"*/,
							nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801", "UPP2004365801-000071")/*@res "ժҪ"*/,
							nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801", "UPP2004365801-000072")/*@res "���㷽ʽ"*/,
							nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801", "UPP2004365801-000073")/*@res "�����"*/,
							nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801", "UPP2004365801-000209")/*@res "�跽"*/,
							nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801", "UPP2004365801-000210")/*@res "����"*/,
							nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801", "UPP2004365801-000213")/*@res "���"*/,
							nc.ui.ml.NCLangRes.getInstance().getStrByID("contrast", "UPPcontrast-000116")/*"��ʶ��"*/,
							nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801", "UPP2004365801-000138")/*@res "����"*/,
							nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801", "UPP2004365801-000062")/*@res "����"*/,
							nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801", "UPP2004365801-000157")/*@res "��λ"*/,
							nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801", "UPP2004365801-000172") /*@res "��Ŀ"*/} };
			bankColumn = new ColumnName(col2);

			//���������õ������
			initTable();
			//		query = new QueryUI();
			//����ѡ��仯�ļ���
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
	 * 	��������:��ʼ���������
	 *	�������ж��˵�¼�������Ϊ19λ
	 */
	private void initTable() throws Throwable {

		//������ͷ
		if (ifManyUnit == 1) {
			bankColumn.setCurrentColumn(0);
		} else {
			bankColumn.setCurrentColumn(1);
		}

		//���ñ������
		m_mTableModelBank.setDataVector(new Vector(), bankColumn.getColumnVector());
		////
		m_tUITableBank.setSelectionMode(1); //����ֻ��ѡ��һ��
		m_tUITableBank.setAutoResizeMode(0);
		////////////////////////////////
		CalTableCellEditor cell1 = new CalTableCellEditor(getUIRefPane1Bankdate());
		cell1.setClickCountToStart(1);
		m_tUITableBank.getColumnModel().getColumn(
				bankColumn.getCountByName(nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801",
						"UPP2004365801-000075")/*@res "����"*/)).setCellEditor(cell1);

		CalTableCellEditor cell2 = new CalTableCellEditor(getUIRefPane1Bankjs());
		cell2.setClickCountToStart(1);

		m_tUITableBank.getColumnModel().getColumn(
				bankColumn.getCountByName(nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801",
						"UPP2004365801-000072")/*@res "���㷽ʽ"*/)).setCellEditor(cell2);

		CalTableCellEditor cell3 = new CalTableCellEditor(getUIRefPane1Bankzy());
		cell3.setClickCountToStart(1);
		m_tUITableBank.getColumnModel().getColumn(
				bankColumn.getCountByName(nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801",
						"UPP2004365801-000071")/*@res "ժҪ"*/)).setCellEditor(cell3);

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
						"UPP2004365801-000073")/*@res "�����"*/)).setCellEditor(cell4);
		m_tUITableBank.getColumnModel().getColumn(
				bankColumn.getCountByName(nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801",
						"UPP2004365801-000073")/*@res "�����"*/)).setCellRenderer(tl);

		m_tUITableBank.getColumnModel().getColumn(
				bankColumn.getCountByName(nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801",
						"UPP2004365801-000209")/*@res "�跽"*/)).setCellEditor(cell5);
		m_tUITableBank.getColumnModel().getColumn(
				bankColumn.getCountByName(nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801",
						"UPP2004365801-000209")/*@res "�跽"*/)).setCellRenderer(tc);

		m_tUITableBank.getColumnModel().getColumn(
				bankColumn.getCountByName(nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801",
						"UPP2004365801-000210")/*@res "����"*/)).setCellEditor(cell6);
		m_tUITableBank.getColumnModel().getColumn(
				bankColumn.getCountByName(nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801",
						"UPP2004365801-000210")/*@res "����"*/)).setCellRenderer(tc);

		m_tUITableBank.getColumnModel().getColumn(
				bankColumn.getCountByName(nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801",
						"UPP2004365801-000213")/*@res "���"*/)).setCellEditor(cell7);
		m_tUITableBank.getColumnModel().getColumn(
				bankColumn.getCountByName(nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801",
						"UPP2004365801-000213")/*@res "���"*/)).setCellRenderer(tc);

		//////////////////
		CalTableCellEditor cell11 = new CalTableCellEditor(getLinkComBox1());
		cell11.setClickCountToStart(1);

		if (ifManyUnit == 2) {
			m_tUITableBank.getColumnModel().getColumn(
					bankColumn.getCountByName(nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801",
							"UPP2004365801-000074")/*@res "���˻�"*/)).setCellEditor(cell11);
		}
		//������

		m_tUITableBank.removeColumn(m_tUITableBank.getColumnModel().getColumn(
				bankColumn.getCountByName(nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801",
						"UPP2004365801-000172")/*@res "��Ŀ"*/)));
		m_tUITableBank.removeColumn(m_tUITableBank.getColumnModel().getColumn(
				bankColumn.getCountByName(nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801",
						"UPP2004365801-000157")/*@res "��λ"*/)));
		m_tUITableBank.removeColumn(m_tUITableBank.getColumnModel().getColumn(
				bankColumn.getCountByName(nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801",
						"UPP2004365801-000062")/*@res "����"*/)));
		m_tUITableBank.removeColumn(m_tUITableBank.getColumnModel().getColumn(
				bankColumn.getCountByName(nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801",
						"UPP2004365801-000138")/*@res "����"*/)));

		if (ifManyUnit == 1 || linkVO.length == 1 || cvo.getM_pk_link() != null) {

		} else {
			m_tUITableBank.removeColumn(m_tUITableBank.getColumnModel().getColumn(
					bankColumn.getCountByName(nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801",
							"UPP2004365801-000213")/*@res "���"*/)));

		}
	}

	/**
	 * �������vo�������
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
	 * ��������:
	 *
	 *  ����:
	 * 		MyNCTableSelectionEvent e ----- �¼�

	 * ����ֵ:
	 *
	 * �쳣:
	 *
	 */
	public boolean isChangeBank(BankreceiptVO newVO, BankreceiptVO oldVO) {

		return true;
	}

	/**
	 * �˴����뷽��������
	 * �������ڣ�(2004-4-13 11:11:22)
	 * ���ܣ����ص�ǰ��˾�Ƿ�Ϊ��������
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
							//�������������˲����ض�Ӧ�Ĺ�˾
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

		//���̲���
		if (key == java.awt.event.KeyEvent.VK_N && e.isControlDown()) {
			//���
			if (status == 0)
				onButtonAppend();
		} else if (key == KeyEvent.VK_S && e.isControlDown()) {
			//����
			if (status != 0)
				onButtonSave();
		} else if (key == KeyEvent.VK_D && e.isControlDown()) {
			//ɾ��
			if (status == 0)
				onButtonDelete();
		} else if (key == KeyEvent.VK_C && e.isControlDown()) {
			//ȡ��
			if (status != 0)
				onButtonCancel();
		} else if (key == KeyEvent.VK_E && e.isControlDown()) {
			//�༭
			if (status == 0)
				onButtonEdit();
		} else if (key == KeyEvent.VK_F3) {
			//��ѯ
			if (status == 0)
				onButtonQuery();
		}
		//e.consume();

		e.consume();
		if (status == 2) {
			//��ûس���
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
			//��ûس���
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
			//		//��ѡ���й�������ǰ��ʾ
			//		m_tUITableBank.scrollRectToVisible(m_tUITableBank.getCellRect(row,column+1,false));
			//		m_tUITableBank.editCellAt(row,column+1);
			//		if(column + 1 == bankColumn.getCountByName(nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801","UPP2004365801-000071")/*@res "ժҪ"*/)){
			//			getUIRefPane1Bankzy().getUITextField().requestFocus();
			//		}else if(column + 1 == bankColumn.getCountByName(nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801","UPP2004365801-000072")/*@res "���㷽ʽ"*/)){
			//			getUIRefPane1Bankjs().getUITextField().requestFocus();
			//		}
		}

	}

	/**
	 * Invoked when a key has been released.
	 */
	public void keyReleased(java.awt.event.KeyEvent e) {
		if (status == 2) {
			//��ûس���
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
			//��ûس���
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
	 * 	��������:���
	 *
	 *  ����:
	 *
	 * 	����ֵ:
	 *
	 * 	�쳣:
	 *
	 */
	private void onButtonAppend() {
		//����һ��
		int iRowIndex = m_mTableModelBank.getRowCount();
		m_mTableModelBank.addRow(new Vector());
		m_mTableModelBank.setEditableRowColumn(iRowIndex, -99);
		int iColumnIndex = m_tUITableBank.getColumnModel().getColumnIndex(
				nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801", "UPP2004365801-000075")/*@res "����"*/);
		if (iRowIndex >= 1) {
			getUIRefPane1Bankdate().setText(ce.getDate().toString());
			iColumnIndex = m_tUITableBank.getColumnModel().getColumnIndex(
					nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801", "UPP2004365801-000075")/*@res "����"*/);
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
		//��ѡ���й�������ǰ��ʾ
		m_tUITableBank.scrollRectToVisible(m_tUITableBank.getCellRect(iRowIndex, 0, false));

		//���ð�ť״̬
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
	 * 	��������:���
	 *
	 *  ����:
	 *
	 * 	����ֵ:
	 *
	 * 	�쳣:
	 *
	 */
	private void onButtonCancel() {

		if (m_tUITableBank.isEditing()) {
			m_tUITableBank.editingStopped(new javax.swing.event.ChangeEvent(this));
		}
		//����һ��
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

			setInterfaceBank(nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801", "UPP2004365801-000209")/*@res "�跽"*/);
			setInterfaceBank(nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801", "UPP2004365801-000210")/*@res "����"*/);
			if (ifManyUnit == 1 || linkVO.length == 1 || cvo.getM_pk_link() != null) {
				setInterfaceBank(nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801", "UPP2004365801-000213")/*@res "���"*/);
			}
			if (m_mTableModelBank.getRowCount() > 0) {
				getUIRefPane1Bankdate().setText("");
			}
		}

		if (m_mTableModelBank.getRowCount() > 0) {
			m_tUITableBank.setRowSelectionInterval(0, 0);
		}
		//���ð�ť״̬
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
	 * onButtonClicked ����ע�⡣
	 */
	public void onButtonClicked(nc.ui.pub.ButtonObject arg1) {
		if (arg1 == m_bButtonQuery) {
			this.showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("2002100555", "UPP2002100555-000064")/*@res "���ڲ�ѯ.."*/);
			onButtonQuery();
			return;
		} else if (arg1 == m_bButtonDirectPrint) {
//			onButtonPrint();
			onDirectPrint();
			this.showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801", "UPP2004365801-000220")/*@res "��ӡ"*/);
			return;
		}else if(arg1 == m_bButtonTemplatePrint){
			onButtonPrint();
			this.showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801", "UPP2004365801-000220")/*@res "��ӡ"*/);
			return;
		} else if (arg1 == m_bFileImprot) {
			onFileImport();
			return;
		} else if (arg1 == m_bButtonInput) {
			onButtonInput();
			this.showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801", "UPP2004365801-000219")/*@res "����"*/);
			return;
		} else if (arg1 == m_bButtonAppend) {
			onButtonAppend();
			this.showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("2002130055", "UPP2002100555-000058")/*@res "��������.."*/);
			return;
		} else if (arg1 == m_bButtonEdit) {
			onButtonEdit();
			this.showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("2002100555", "UPP2002100555-000055")/*@res "�����޸�.."*/);
			return;
		} else if (arg1 == m_bButtonDelete) {
			onButtonDelete();
			this.showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("2002100555", "UPP2002100555-000041")/*@res "ɾ�����"*/);
			return;
		} else if (arg1 == m_bButtonSave) {
			onButtonSave();
			this.showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("2002100555", "UPP2002100555-000006")/*@res "����ɹ�"*/);
			return;
		} else if (arg1 == m_bButtonCancel) {
			onButtonCancel();
			this.showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801", "UPP2004365801-000021")/*@res "ȡ��"*/);
			return;
		} else if (arg1 == m_bButtonDeleteAll) {
			onButtonDeleteAll();
			return;
		}/*else if (arg1 == m_bButtonImport) {
			onButtonImport();
			this.showHintMessage(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("contrast","UPPcontrast-000049")@res "������ɣ�");
			return;
		} else if (arg1 == m_bButtonExport) {
			onButtonExport();
			this.showHintMessage(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("contrast","UPPcontrast-000050")@res "������ɣ�");
			return;
		}*/else if (arg1 == m_bButtonRefresh) {
			reQuery("");
			this.showHintMessage(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("contrast","UPPcontrast-000037")/*@res "ˢ�½���"*/);
			return;
		}
	}

	/**
	 * *�ļ�������˵�
	 * @author zhaozh
	 * �������ڣ�(2008-12-5 ����03:26:09)
	 */
	private void onFileImport() {
		try {
			if(contrast != null && contrast.getM_stopdate() != null){
				MessageDialog.showWarningDlg(this, null, NCLangRes4VoTransl.getNCLangRes().getStrByID("contrast", "UPPcontrast-000117")/*"ͣ�õ��˻����ܵ������ж��˵�"*/);
				return;
			}
			EbankExcelDialog dlg = new EbankExcelDialog(this);
			dlg.setSize(new Dimension(431, 147));
			if(dlg.showModal() == UIDialog.ID_OK){
				File file = dlg.getFile();
				if(file == null){
					MessageDialog.showErrorDlg(this, null, NCLangRes4VoTransl.getNCLangRes().getStrByID("contrast", "UPPcontrast-000101")/*"��ѡ��Excel�ļ�"*/);
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
	 * 2008-7-9 ����03:50:37
	 * �������ж��˵��ĸ�ʽ
	 */
	@SuppressWarnings("unused")
	private void onButtonExport() {
		if (contrast == null || linkVO.length == 0) {
			MessageDialog.showWarningDlg(this, null, nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("2004","UPP2004-000234")/*@res "���Ȳ�ѯ�����˻�"*/);
		}
	}

	/**
	 * 	��������:���
	 *
	 *  ����:
	 *
	 * 	����ֵ:
	 *
	 * 	�쳣:
	 *
	 */
	private void onButtonDelete() {
//		long start = System.currentTimeMillis();
		if (m_tUITableBank.getSelectedRowCount() < 1 && m_mTableModelBank.getRowCount() < 1) {
			MessageDialog.showWarningDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801",
					"UPP2004365801-000027")/*@res "��ʾ"*/, nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801",
					"UPP2004365801-000235")/*@res "û��ѡ����"*/);
			return;
		}
		if (m_tUITableBank.getSelectedRowCount() == 1) {
			int rows = m_tUITableBank.getSelectedRow();
			int allrows = m_mTableModelBank.getRowCount();

			if (rows >= 0 && allrows > 0) {
				if (MessageDialog.showYesNoDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801",
						"UPP2004365801-000217")/*@res "ɾ��"*/, nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"2004365801", "UPP2004365801-000236")/*@res "ȷʵҪɾ��ô��"*/) == MessageDialog.ID_YES) {
					try {

						String key = m_mTableModelBank.getValueAt(
								rows,
								bankColumn.getCountByName(nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801",
										"UPP2004365801-000138")/*@res "����"*/)).toString();
						BankreceiptVO v = getVO(key);
						int count = getPosition(key);

						if (v.getflag().intValue() > 0) {
							MessageDialog
									.showWarningDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801",
											"UPP2004365801-000027")/*@res "��ʾ"*/, nc.ui.ml.NCLangRes.getInstance()
											.getStrByID("2004365801", "UPP2004365801-000237")/*@res "��¼�ѱ����ԣ�����ɾ��"*/);
							return;
						}
						if (((IContrastPrv) NCLocator.getInstance().lookup(IContrastPrv.class.getName())).isContrast(
								null, v.getPk_bankreceipt(), 2).booleanValue()) {
							MessageDialog
									.showWarningDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801",
											"UPP2004365801-000027")/*@res "��ʾ"*/, nc.ui.ml.NCLangRes.getInstance()
											.getStrByID("2004365801", "UPP2004365801-000237")/*@res "��¼�ѱ����ԣ�����ɾ��"*/);
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
									"UPP2004365801-000213")/*@res "���"*/);
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
								"UPP2004365801-000238")/*@res "ɾ�����ִ���"*/, nc.ui.ml.NCLangRes.getInstance().getStrByID(
								"2004365801", "UPP2004365801-000000")/*@res "��ǰ��¼������������ϵͳ����!"*/);
						return;
					}
					reQuery("");//ɾ��֮���ز�һ�Σ��������������
				}
			} else {
				MessageDialog.showWarningDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801",
						"UPP2004365801-000027")/*@res "��ʾ"*/, nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"2004365801", "UPP2004365801-000235")/*@res "û��ѡ����"*/);
			}
		} else {
			int allrow = m_mTableModelBank.getRowCount();
			int[] rows = m_tUITableBank.getSelectedRows();
			int rowcount = m_tUITableBank.getSelectedRowCount();
//			int rowcount = rows.length;
			ArrayList<String> vopks = new ArrayList<String>();
			if (allrow > 0 && rowcount > 0) {
				if (MessageDialog.showYesNoDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801",
						"UPP2004365801-000217")/*@res "ɾ��"*/, nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"2004365801", "UPP2004365801-000239")/*@res "ȷʵҪɾ��ȫ������ô��"*/) == MessageDialog.ID_YES) {
					try {
						for (int i = rowcount - 1; i >= 0; i--) {
							int row = rows[i];
							String key = m_mTableModelBank.getValueAt(
									row,
									bankColumn.getCountByName(nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801",
											"UPP2004365801-000138")/*@res "����"*/)).toString();
							BankreceiptVO v = getVO(key);
							if (v.getflag().intValue() > 0) {
								continue;
								//MessageDialog.showWarningDlg(this,"��ʾ","��¼�ѱ����ԣ�����ɾ��") ;
								//return;
							}
							//��סû�й��Ե��˵���
							vopks.add(v.getPk_bankreceipt());

							//if(((IContrastPrv)NCLocator.getInstance().lookup(IContrastPrv.class.getName())).isContrast(null,v.getPk_bankreceipt(),2).booleanValue()){
							//	continue;
							//	//MessageDialog.showWarningDlg(this,"��ʾ","��¼�ѱ����ԣ�����ɾ��") ;
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
							//	setInterfaceBank(nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801","UPP2004365801-000213")/*@res "���"*/);
							//}
						}

						//1.call ContrastBO.deleteBankReceiptArray
						IBankReceiptPrv bankReceiptPrv = (IBankReceiptPrv) NCLocator.getInstance().lookup(
								IBankReceiptPrv.class.getName());
						Map deleteResult = bankReceiptPrv.deleteBankReceiptArray(vopks.toArray(new String[0]), ce
								.getUser().getUserCode());
						//2.���ݷ���ֵ��ɾ������е�����
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
									"UPP2004365801-000213")/*@res "���"*/);
						}
					} catch (Throwable e) {
						MessageDialog.showWarningDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801",
								"UPP2004365801-000238")/*@res "ɾ�����ִ���"*/, e.toString());
						return;
					}
					reQuery("");//ɾ��֮���ز�һ�Σ��������������
				}
			} else {
				MessageDialog.showWarningDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801",
						"UPP2004365801-000027")/*@res "��ʾ"*/, nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"2004365801", "UPP2004365801-000240")/*@res "û��ѡ���¼��"*/);
				return;
			}

		}
//		long end = System.currentTimeMillis();
//		long costTime = end - start;
	}

	/**
	 * 	��������:���
	 *
	 *  ����:
	 *
	 * 	����ֵ:
	 *
	 * 	�쳣:
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
						"UPP2004365801-000217")/*@res "ɾ��"*/, nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"2004365801", "UPP2004365801-000239")/*@res "ȷʵҪɾ��ȫ������ô��"*/) == MessageDialog.ID_YES) {
					try {

						for (int i = rowcount - 1; i >= 0; i--) {
							int row = rows[i];
							String key = m_mTableModelBank.getValueAt(
									row,
									bankColumn.getCountByName(nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801",
											"UPP2004365801-000138")/*@res "����"*/)).toString();
							BankreceiptVO v = getVO(key);
							int count = getPosition(key);
							if (v.getflag().intValue() > 0) {
								continue;
								//MessageDialog.showWarningDlg(this,"��ʾ","��¼�ѱ����ԣ�����ɾ��") ;
								//return;
							}
							if (((IContrastPrv) NCLocator.getInstance().lookup(IContrastPrv.class.getName()))
									.isContrast(null, v.getPk_bankreceipt(), 2).booleanValue()) {
								continue;
								//MessageDialog.showWarningDlg(this,"��ʾ","��¼�ѱ����ԣ�����ɾ��") ;
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
								"UPP2004365801-000238")/*@res "ɾ�����ִ���"*/, e.toString());
						return;
					}
				}
			} else {
				MessageDialog.showWarningDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801",
						"UPP2004365801-000027")/*@res "��ʾ"*/, nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"2004365801", "UPP2004365801-000240")/*@res "û��ѡ���¼��"*/);
				return;
			}
		}
	}

	/**
	 * 	��������:���
	 *
	 *  ����:
	 *
	 * 	����ֵ:
	 *
	 * 	�쳣:
	 *
	 */
	private void onButtonEdit() {
		//�༭����
		int allrows = m_mTableModelBank.getRowCount();
		int rows = m_tUITableBank.getSelectedRow();
		if (m_tUITableBank.getSelectedRowCount() > 1) {
			MessageDialog.showWarningDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801",
					"UPP2004365801-000027")/*@res "��ʾ"*/, nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801",
					"UPP2004365801-000241")/*@res "ֻ��ѡ��һ����¼��"*/);
			return;
		}
		if (rows >= 0 && allrows > 0 && rows < allrows) {
			try {
				String key = m_mTableModelBank.getValueAt(
						rows,
						bankColumn.getCountByName(nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801",
								"UPP2004365801-000138")/*@res "����"*/)).toString();
				BankreceiptVO v = getVO(key);
				if (v.getflag().intValue() > 0) {
					MessageDialog.showWarningDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801",
							"UPP2004365801-000027")/*@res "��ʾ"*/, nc.ui.ml.NCLangRes.getInstance().getStrByID(
							"2004365801", "UPP2004365801-000242")/*@res "��¼�ѱ����ԣ����ܱ༭��"*/);
					return;
				}
				if (((IContrastPrv) NCLocator.getInstance().lookup(IContrastPrv.class.getName())).isContrast(null,
						v.getPk_bankreceipt(), 2).booleanValue()) {
					MessageDialog.showWarningDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801",
							"UPP2004365801-000027")/*@res "��ʾ"*/, nc.ui.ml.NCLangRes.getInstance().getStrByID(
							"2004365801", "UPP2004365801-000243")/*@res "��¼�ѱ����ԣ����ܱ༭!"*/);
					return;
				}
				if (ifManyUnit == 1) {
					getLinkComBox1().setSelectedIndex(1);
				}
				if (ifManyUnit == 2) {
					getLinkComBox1().setSelectLink(v.getMemo());
					//					setRefWhereParts(v.getPk_corp());
					if (contrast.getSource().intValue() == 1) {
						//�������������˲����ض�Ӧ�Ĺ�˾
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
								nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801", "UPP2004365801-000213")/*@res "���"*/);
				m_mTableModelBank.setEditDisableRowColumn(rows, iColumnIndexs);
			}
			m_mTableModelBank.setRowEditable(rows, true);
		} else {
			MessageDialog.showWarningDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801",
					"UPP2004365801-000027")/*@res "��ʾ"*/, nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801",
					"UPP2004365801-000244")/*@res "û��ѡ���У�"*/);
			return;
		}
		//���ð�ť״̬
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
	 * ��������:���ʽ���֯��ԭ�������ģ������ڲ��˻���������Ϊ���˵�
	 * �������ڣ�(2008-9-11 ����07:01:39)
	 * @author zhaozh
	 */
	private void onButtonInput() {
		if(contrast.getM_stopdate() != null){
			MessageDialog.showWarningDlg(this, null, NCLangRes4VoTransl.getNCLangRes().getStrByID("contrast", "UPPcontrast-000117")/*"ͣ�õ��˻����ܵ������ж��˵�"*/);
			return;
		}
		input = new ImportFundOrgDlg(this,nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("contrast","UPPcontrast-000052")/*@res "���ʽ���֯����"*/);
		UFDate beginDate = null;
		UFDate endDate = null;
//		input.setContrast(contrast);
//		input.getLinkComBox1().setData(contrast.getPk_contrastaccount(), true);
		AccountlinkVO link = query.getLinkComBox1().getSelectLinks();
		if( link == null && contrast.getMemo() != null && contrast.getMemo().equals("many")){
			showErrorMessage(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("contrast","UPPcontrast-000053")/*@res "��ѡ�����ʻ���"*/);
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
			showErrorMessage(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("contrast","UPPcontrast-000054")/*@res "�����ڲ��ʻ������ܵ�����˵���"*/);
			return;
		}
		if(input.showModal() == UIDialog.ID_OK){
			if(!StringUtil.isEmptyWithTrim(input.getRefBeginDate().getText())){
				beginDate = new UFDate(input.getRefBeginDate().getText());
			}else
				beginDate = getClientEnvironment().getBusinessDate();//Ϊ��Ĭ��Ϊ��¼����
			if(StringUtil.isEmptyWithTrim(input.getRefEndDate().getText())){
				showErrorMessage(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("contrast","UPPcontrast-000055")/*@res "�������ڲ���Ϊ��"*/);
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
					MessageDialog.showHintDlg(this, null, nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("2004","UPP2004-000236")/*@res "����"*/+ results[0] +nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("2004","UPP2004-000237")/*@res "������"*/);
				}
				if(results[1] > 0){
					MessageDialog.showHintDlg(this, null, nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("contrast","UPPcontrast-000038",null,new String[]{String.valueOf(results[1])})/*@res "����{0}������"*/);
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
//									new String[]{String.valueOf(bankvos.length)})/*@res "����{0}������"*/);
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
	 * �������ڣ�(2008-9-11 ����09:03:06)
	 * ���ڲ��ʻ����Ķ��˵�ת��Ϊ�ֽ����ж��˵�����
	 * modifier by zhaozh on 2008-12-19
	 * 	�ظ��жϣ����ӽ��㷽ʽ�������ȡ���ݱ�ţ��޸ĵ����ݸ���
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
			MessageDialog.showHintDlg(this, null, nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("2004","UPP2004-000235")/*@res "û������"*/);
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
			if( bank != null){//�ظ����ݣ����¹ؼ�����
				bank.setCheckdate(avo.getTallydate());//����
				bank.setCheckstyle("");//���㷽ʽȡ�ĸ���
				bank.setExplanation(avo.getMemo());//��ע
				bank.setPk_check(avo.getFbmbillno());//�����ȡƱ�ݱ��
				bank.setDebitamount(avo.getInmoney());//�跽ȡ�տ�ԭ��
				bank.setCreditamount(avo.getOutmoney());//����ȡ����ԭ��
				bank.setStyleflag(avo.getIseffective().booleanValue()?"A":"B");//������Ч��־
				updateList.add(bank);
			}else{//�����ݲ���
				BankReceiptVO bvo = new BankReceiptVO();
				bvo.setCheckdate(avo.getTallydate());//����
				bvo.setCheckstyle("");//���㷽ʽȡ�ĸ���
				bvo.setExplanation(avo.getMemo());//��ע
				bvo.setPk_check(avo.getFbmbillno());//�����ȡƱ�ݱ��
				bvo.setDebitamount(avo.getInmoney());//�跽ȡ�տ�ԭ��
				bvo.setCreditamount(avo.getOutmoney());//����ȡ����ԭ��
				bvo.setPk_jszxdetail(avo.getPrimaryKey());
				//������Ϣ
				bvo.setMemo(linkvo.getPk_accountlink());
				bvo.setPk_bank(linkvo.getBankaccount());
				bvo.setPk_corp(linkvo.getPk_corp());
				bvo.setYears(linkvo.getYears());
				bvo.setPk_subject(linkvo.getPk_subject());
				bvo.setPk_ass(linkvo.getPk_ass());
				bvo.setPk_contrastaccount(linkvo.getPk_contrastaccount());
				bvo.setStyleflag(avo.getIseffective().booleanValue()?"A":"B");//���ñ�־Ϊ��Ч
				list.add(bvo);
			}
		}
		if((updateList != null && updateList.size() > 0)
				|| (null != list && list.size() >0)){
			MessageDialog.showHintDlg(this, null, nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("2004","UPP2004-000236")/*@res "����"*/+updateList.size()+nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("2004","UPP2004-000237")/*@res "������"*/);
			MessageDialog.showHintDlg(this, null, nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("contrast","UPPcontrast-000038",null,new String[]{String.valueOf(list.size())})/*@res "����{0}������"*/);
		}
		return null;
//		return list.toArray(new BankReceiptVO[list.size()]);
	}

	/**
	 * �ж��Ƿ����ڲ��ʻ�������ͨ���ӿڲ�ѯbd_accid��
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
					showErrorMessage(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("contrast","UPPcontrast-000057")/*@res "�����ʻ�����������ʻ��ı��ֲ�һ�£�"*/);
					return false;
			}
		} catch (Exception e) {
			Debug.debug(e.getMessage(), e);
			MessageDialog.showErrorDlg(this,nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("2004","UPP2004-000238")/*@res "�����쳣"*/, e.getMessage());
		}
		return flag;
	}

	@SuppressWarnings("restriction")
	private void onDirectPrint(){
		FiPrintDirectProxy proxy = new FiPrintDirectProxy();
		proxy.setTitle(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("contrast","UPPcontrast-000058")/*@res "���ж��˵�"*/);
		try {
			proxy.printDirect(this);
		} catch (BusinessException e) {
			Debug.debug(e.getMessage(), e);
			showErrorMessage(e.getMessage());
		}
	}
	/**
	 * 	��������:��ӡ
	 */
	@SuppressWarnings("unused")
	private void onButtonPrint() {
		String m_sNodeCode = "20040605";
		//��ӡVO  ���ñ�ͷ����
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
					+ nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801", "UPP2004365801-000245")/*@res "����"*/
					+ query.getUIRefPanedate2().getText();
		} else {
			date[0] = "";
		}
		String[] ba = new String[1];
		ba[0] = getUILabel2().getText().substring(5);
		//	ba[0] = getUILabel2().getText();
		vo.setInitbalance(ba);
		vo.setAccountname(account); //��ͷ����
		vo.setDatescope(date);
		vo.setPrecision(precision);
		/////���ñ�������
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
						datas[i][6] = temps[i][8] == "N" ? "" : temps[i][8].toString(); //����
					}
					datas[i][7] = temps[i][6] == null ? null : temps[i][6].toString(); //���
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
						datas[i][6] = temps[i][9] == "N" ? "" : temps[i][9].toString(); //����
					}
					datas[i][7] = temps[i][7] == null ? null : temps[i][7].toString(); //���
					if (contrast.getSource().intValue() == 1) {
						datas[i][8] = temps[i][10] == null ? null : temps[i][10].toString(); //��λ
						datas[i][9] = temps[i][11] == null ? null : temps[i][11].toString(); //��Ŀ
						datas[i][10] = null;
					} else {
						datas[i][8] = temps[i][10] == null ? null : temps[i][10].toString(); //��λ
						datas[i][9] = null;
						datas[i][10] = temps[i][11] == null ? null : temps[i][11].toString(); //�˻�
					}
				}
			}

		} else {
			MessageDialog.showWarningDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801",
					"UPP2004365801-000027")/*@res "��ʾ"*/, nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801",
					"UPP2004365801-000246")/*@res "û�����ݣ����ܴ�ӡ��"*/);
			return;
		}
		//////////////////
		BankReceipPrint dataSource = new BankReceipPrint();

		dataSource.setDataItemValues(datas, vo); //��ʼ������

		//ʵ������ӡ���
		nc.ui.pub.print.PrintEntry print = new nc.ui.pub.print.PrintEntry(null, null);
		//ѡ�������Դ��Ӧ�Ĵ�ӡģ�壬ѡ��ģ�����ѡ��Ԥ�����ӡ
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

		/*//������ֱ�Ӵ�ӡ����
		 String[] m_sColumnName = {"����","ժҪ","���㷽ʽ","�����","�跽","����","���"};

		 if(m_mTableModelBank.getRowCount()<1){
		 return;
		 }

		 int rowH[] = new int[m_mTableModelBank.getRowCount()+1];
		 for (int i = 0; i < rowH.length; i++){
		 rowH[i] = 20;
		 }

		 Object[][] temp = new Object[m_mTableModelBank.getRowCount()][7];

		 for (int i = 0; i < m_mTableModelBank.getRowCount(); i++){
		 //����������
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
		 //{"����","ժҪ","���㷽ʽ","�����","�跽","����","���"};
		 int[] cols = {70,60,60,50,75,75,75};
		 int[] aligns = {1,0,0,0,2,2,2};

		 for(int i=0;i<columnCount;i++){
		 colwidth[i] = cols[i];
		 alignflag[i] = aligns[i];
		 }
		 String title = "���ж��ʵ���ӡ";
		 java.awt.Font font = new java.awt.Font("dialog", java.awt.Font.BOLD, 30);
		 java.awt.Font font1 = new java.awt.Font("dialog", java.awt.Font.PLAIN, 12);

		 String topstr ="��λ���ƣ� "+ce.getCorporation().getUnitname()+"    �ʻ����ƣ�"+contrast.getContrastaccountname();
		 String botstr ="�Ʊ�ʱ�䣺"+ce.getDate()+"          �Ʊ��ˣ� "+ce.getUser().getUserName();
		 //
		 PrintDirectEntry print = new PrintDirectEntry();
		 print.setTitle(title);	    	//����  ��ѡ
		 print.setTitleFont(font);	    //��������   ��ѡ
		 print.setContentFont(font1);	//�������壨��ͷ����񡢱�β�� ��ѡ
		 print.setTopStr(topstr);	    //��ͷ��Ϣ  ��ѡ
		 print.setBottomStr(botstr);	    //��β��Ϣ   ��ѡ
		 print.setColNames(colname);     //�����������ά������ʽ��
		 print.setData(temp);	        //�������
		 print.setColWidth(colwidth);	//����п�    ��ѡ
		 print.setAlignFlag(alignflag);		//���ÿ�еĶ��뷽ʽ��0-��, 1-��, 2-�ң���ѡ
		 ///
		 print.setTitleHeight(25);         //����߶�
		 print.setPrintDirection(true);    //��ӡ���� ����
		 print.setRowHeight(rowH);         //�����и�
		 print.setTopStrFixed(true);       //��ͷ�Ƿ�̶���ӡ

		 //
		 print.preview(); 	//Ԥ��
		 */

	}

	/**
	 * 	��������:���
	 *  @author zhaozh
	 *  �������ڣ�(2008-9-4 ����04:50:15)
	 *  �޸ģ����ж��˵����������
	 */
	private void onButtonQuery() {
		//	query.setParent(this);
		if (query == null) //added by jh ������������
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
					showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801", "UPP2004365801-000247")/*@res "�ʻ��Ѿ���ת�����ܲ�����"*/);
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
					showErrorMessage(NCLangRes4VoTransl.getNCLangRes().getStrByID("contrast", "UPPcontrast-000118")/*"�������ж��˵����쳣��¼�����ң�����д���"*/);
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
//					showErrorMessage(NCLangRes4VoTransl.getNCLangRes().getStrByID("contrast", "UPPcontrast-000118")/*"�������ж��˵����쳣��¼�����ң�����д���"*/);
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
					/**@author zhaozh �������ڣ�(2008-7-28 ����10:44:29)���ӷ����ѯ����*/
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
					//���þ���
					setPrecision(contrast.getPk_currtype());
					setInterfaceBank(nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801", "UPP2004365801-000209")/*@res "�跽"*/);
					setInterfaceBank(nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801", "UPP2004365801-000210")/*@res "����"*/);
					getUILabel1().setText(
							nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801", "UPP2004365801-000248")/*@res "�������ڣ�"*/
									+ query.getContrast().getStartdate());
					getUILabel3().setText(
							nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801", "UPP2004365801-000249")/*@res "    �˻���"*/
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
								nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801", "UPP2004365801-000250")/*@res "    �ڳ���"*/
										+ sTemp);

					} else if (query.getContrast().getContrastaspect().intValue() == 0
							&& query.getContrast().getDebitamount() != null
							&& new UFDouble(query.getContrast().getCreditamount().toString()).doubleValue() == 0.0) {
						UFDouble dTemp = query.getContrast().getDebitamount().add(b);
						nowB = new UFDouble(dTemp);
						String sTemp = fx.format(dTemp);
						getUILabel2().setText(
								nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801", "UPP2004365801-000250")/*@res "    �ڳ���"*/
										+ sTemp);
					} else if (query.getContrast().getContrastaspect().intValue() == 1
							&& query.getContrast().getCreditamount() != null
							&& new UFDouble(query.getContrast().getDebitamount().toString()).doubleValue() == 0.0) {

						UFDouble dTemp = query.getContrast().getCreditamount().sub(b);
						nowB = new UFDouble(dTemp);
						String sTemp = fx.format(dTemp);
						getUILabel2().setText(
								nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801", "UPP2004365801-000250")/*@res "    �ڳ���"*/
										+ sTemp);

					} else {
						nowB = new UFDouble(0);
						getUILabel2()
								.setText(
										nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801",
												"UPP2004365801-000251")/*@res "    �ڳ��� 0"*/);
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
			setInterfaceBank(nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801", "UPP2004365801-000209")/*@res "�跽"*/);
			setInterfaceBank(nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801", "UPP2004365801-000210")/*@res "����"*/);
			if (ifManyUnit == 1 || linkVO.length == 1 || cvo.getM_pk_link() != null) {
				setInterfaceBank(nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801", "UPP2004365801-000213")/*@res "���"*/);
			}
			m_bButtonAppend.setEnabled(true);
			m_bButtonCancel.setEnabled(false);
			m_bButtonDelete.setEnabled(true);
			m_bButtonDeleteAll.setEnabled(true);
			m_bButtonEdit.setEnabled(true);
			m_bFileImprot.setEnabled(true);
			m_bButtonRefresh.setEnabled(true);
//			m_bButtonSave.setEnabled(true);
//			add by zhaozh ���뵼��ֻ֧�ֱ���λ����֧�ֶ൥λ
			m_bButtonImport.setEnabled(ifManyUnit == 1 || isMergeAccount());
			m_bButtonExport.setEnabled(ifManyUnit == 1);
			//v31newneed:�ֽ����У����ж��ˣ����ж��˵����������¿��ƣ���ʼ�������С����������˺š��ֶ�Ϊ�յ��˻����ſ�������������ĵĽ���ƾ֤���ݣ���ʼ�������С����������˺š��ֶηǿյ��˻�����������������ĵĽ���ƾ֤���ݡ�
			if (this.linkVO != null && this.linkVO.length > 0 && this.linkVO[0].getM_pk_khAccount() != null) {
				m_bButtonInput.setEnabled(false);
			} else {
				m_bButtonInput.setEnabled(true);
			}
			setButtons(m_aryButtonGroup);
		} else {
			//���ð�ť״̬
			if (getUILabel3()
					.getText()
					.equals(nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801", "UPP2004365801-000233")/*@res "    �˻���XXXXXXXXXX"*/)) {
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
		this.showHintMessage(NCLangRes4VoTransl.getNCLangRes().getStrByID("2002100555", "UPP2002100555-000065")/*@res "��ѯ����"*/);
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
	 * ���ղ�ѯ�����������ж��˵�,��ʹ��������ȷ
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
				//�������
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
				//������
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
				//������
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
	 * 	��������:���
	 *
	 *  ����:
	 *
	 * 	����ֵ:
	 *
	 * 	�쳣:
	 *
	 */
	@SuppressWarnings("unchecked")
	private boolean onButtonSave() {
		if (m_tUITableBank.isEditing()) {
			m_tUITableBank.editingStopped(new javax.swing.event.ChangeEvent(this));
		}
		//�������
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

			//�༭����
			if (m_tUITableBank.isEditing()) {
				m_tUITableBank.editingStopped(new javax.swing.event.ChangeEvent(this));
			}
			int row = bankRow;

			//iColumnIndex = m_tUITableBank.getColumnModel().getColumnIndex("����");
			String key = "";

			key = m_mTableModelBank.getValueAt(
					row,
					bankColumn.getCountByName(nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801",
							"UPP2004365801-000138")/*@res "����"*/)).toString();

			BankreceiptVO v = getVO(key);
			int count = getPosition(key);

			if (v.getflag().intValue() > 0) {
				MessageDialog.showWarningDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801",
						"UPP2004365801-000027")/*@res "��ʾ"*/, nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"2004365801", "UPP2004365801-000237")/*@res "��¼�ѱ����ԣ�����ɾ��"*/);
				return false;

			}
			BankreceiptVO vo = new BankreceiptVO();
			vo = getBankVO(true, row);
			if (vo == null) {
				return false;
			}
			//vo.setPeriod("00");  //�����
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
					showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801", "UPP2004365801-000087")/*@res "���ݿ��ܱ�����ʹ�ã�"*/);
					return false;
				}
			}

			getUIRefPane1Bankjs().setPK(null);
		}
		m_mTableModelBank.setEditableRowColumn(-99, -99);

		//���ð�ť״̬
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
		setInterfaceBank(nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801", "UPP2004365801-000209")/*@res "�跽"*/);
		setInterfaceBank(nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801", "UPP2004365801-000210")/*@res "����"*/);

		if (ifManyUnit == 1 || linkVO.length == 1 || cvo.getM_pk_link() != null) {
			setInterfaceBank(nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801", "UPP2004365801-000213")/*@res "���"*/);
		}

		if (m_mTableModelBank.getRowCount() > 0) {
			//getUIRefPane1Bankdate().setText("");
		}
		status = 0;
		//getUIRefPaneUnit().setPK("");

		return true;

	}

	/**
	 * 	��������:ˢ�²��Ҳ�ѯ������������
	 *  modifier by zhaozh on 2009-6-15
	 *  ������û�й��������˻���Ҳ�ܲ����
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
				//�������
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
				//������
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
				//������
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
					String pk_link = ((BankreceiptVO) banks.get(i)).getMemo(); //��Դ����
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
		setInterfaceBank(nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801", "UPP2004365801-000209")/*@res "�跽"*/);
		setInterfaceBank(nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801", "UPP2004365801-000210")/*@res "����"*/);
		if (ifManyUnit == 1 || linkVO.length == 1 || cvo.getM_pk_link() != null) {
			setInterfaceBank(nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801", "UPP2004365801-000213")/*@res "���"*/);
		}
	}

	/**
	 * �������
	 * �������ڣ�(2001-8-23 16:22:22)
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
	 * С��λ����ʽ��
	 * �������ڣ�(2001-8-23 16:22:22)
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
	 * ���ñ���λ��
	 * �������ڣ�(2002-4-10 13:49:03)
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
	 * ��������:
	 *
	 * ����:
	 *
	 * ����ֵ:
	 *
	 * �쳣:
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
	 * �������ݱ仯�¼������߱���ʵ�ֵĽӿڷ���
	 * @param event valueChangedEvent �������ݱ仯�¼�
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
//				//����ժҪ����
//				if (sCode.trim().length() > 0) {
//					if (getUIRefPane1Bankzy().getRefName() == null) {
//						sRefName = "";
//					} else {
//						sRefName = getUIRefPane1Bankzy().getRefName();
//					}
//				}
//			}
			if (sControlName.equals(getUIRefPane1Bankjs().getName())) {
				//���н��㷽ʽ����
				int iSelectRow = m_tUITableBank.getSelectedRow();
				int iColumnIndex = bankColumn.getCountByName(nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801",
						"UPP2004365801-000072")/*@res "���㷽ʽ"*/);
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
					"UPP200235-100275")/*@res* "��ʾ"*/, nc.ui.ml.NCLangRes.getInstance().getStrByID("200235",
					"UPP200235-100278")/*
			 * @res
			 * "������δ���棬Ҫ���沢�˳���"
			 */);
			if (re == MessageDialog.ID_YES) {
				onButtonSave();
				if (status == 0)
					return true;
				else
					return false; //˵��û����������������û�б���ɹ�
			} else if (re == MessageDialog.ID_NO)
				return true;
			else
				//ȡ��
				return false;
		}
		return true;
	}

	/**
	 * @author zhaozh
	 * 2008-6-2 ����02:39:06
	 * ��EXCEL���е������ж��ʵ�
	 * �޸����ڣ�(2008-10-21 ����08:01:27)
	 * 	�൥λ�ϲ�����֧�ֵ������ж��˵�
	 */
//	private void onButtonImport() {
//		if(ifManyUnit != 1 && !isMergeAccount()){
//			showWarningMessage(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("contrast","UPPcontrast-000059")/*@res "ֻ֧�ֱ���λ���˵��ĵ��룡"*/);
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
//		//����λ�������ж��ʵ�
//		if (dlg.showModal() == UIDialog.ID_OK && (ifManyUnit == 1 || isMergeAccount())) {
//			banks = dlg.importFromExcel(contrast);
//			if (banks != null && banks.length > 0) {
//				try {
//					banksPK = getBankService().insertBanks(banks);
//				} catch (BusinessException e) {
//					Logger.error(e.getMessage(), e);
//					MessageDialog.showWarningDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801",
//							"UPP2004365801-000027")/* @res "��ʾ" */, nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("contrast","UPPcontrast-000060")/*@res "����ʧ�ܣ�\n"*/ + e.getMessage());
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
//					"UPP2004365801-000027")/* @res "��ʾ" */, "������" + (banks == null ? 0 : banksPK.length)
//					+ "�����ݣ�\n�跽�ϼƣ�" + da + "\n�����ϼ�:" + ca + "\n��" + dia);
//			reQuery("ALL");
//		} else {
//			MessageDialog.showWarningDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801",
//					"UPP2004365801-000027")/* @res "��ʾ" */, nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("contrast","UPPcontrast-000061")/*@res "����0�����ݣ�"*/);
//		}
//	}

	/**
	 * ����ˢ������
	 * @author zhaozh
	 * 2008-6-5 ����04:31:06
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
				showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801", "UPP2004365801-000247")/*@res "�ʻ��Ѿ���ת�����ܲ�����"*/);
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
				//���þ���
				setPrecision(contrast.getPk_currtype());
				setInterfaceBank(nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801", "UPP2004365801-000209")/*@res "�跽"*/);
				setInterfaceBank(nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801", "UPP2004365801-000210")/*@res "����"*/);
				getUILabel1().setText(
						nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801", "UPP2004365801-000248")/*@res "�������ڣ�"*/
								+ query.getContrast().getStartdate());
				getUILabel3().setText(
						nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801", "UPP2004365801-000249")/*@res "    �˻���"*/
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
							nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801", "UPP2004365801-000250")/*@res "    �ڳ���"*/
									+ sTemp);

				} else if (query.getContrast().getContrastaspect().intValue() == 0
						&& query.getContrast().getDebitamount() != null
						&& new UFDouble(query.getContrast().getCreditamount().toString()).doubleValue() == 0.0) {
					UFDouble dTemp = query.getContrast().getDebitamount().add(b);
					nowB = new UFDouble(dTemp);
					String sTemp = fx.format(dTemp);
					getUILabel2().setText(
							nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801", "UPP2004365801-000250")/*@res "    �ڳ���"*/
									+ sTemp);
				} else if (query.getContrast().getContrastaspect().intValue() == 1
						&& query.getContrast().getCreditamount() != null
						&& new UFDouble(query.getContrast().getDebitamount().toString()).doubleValue() == 0.0) {

					UFDouble dTemp = query.getContrast().getCreditamount().sub(b);
					nowB = new UFDouble(dTemp);
					String sTemp = fx.format(dTemp);
					getUILabel2().setText(
							nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801", "UPP2004365801-000250")/*@res "    �ڳ���"*/
									+ sTemp);

				} else {
					nowB = new UFDouble(0);
					getUILabel2()
							.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801", "UPP2004365801-000251")/*@res "    �ڳ��� 0"*/);
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

		setInterfaceBank(nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801", "UPP2004365801-000209")/*@res "�跽"*/);
		setInterfaceBank(nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801", "UPP2004365801-000210")/*@res "����"*/);
		if (ifManyUnit == 1 || linkVO.length == 1 || cvo.getM_pk_link() != null) {
			setInterfaceBank(nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801", "UPP2004365801-000213")/*@res "���"*/);
		}
		m_bButtonAppend.setEnabled(true);
		m_bButtonCancel.setEnabled(false);
		m_bButtonDelete.setEnabled(true);
		m_bButtonDeleteAll.setEnabled(true);
		m_bButtonEdit.setEnabled(true);
		m_bButtonSave.setEnabled(false);
		if (ifManyUnit == 1)
			m_bButtonImport.setEnabled(true);//add by zhaozh
		//v31newneed:�ֽ����У����ж��ˣ����ж��˵����������¿��ƣ���ʼ�������С����������˺š��ֶ�Ϊ�յ��˻����ſ�������������ĵĽ���ƾ֤���ݣ���ʼ�������С����������˺š��ֶηǿյ��˻�����������������ĵĽ���ƾ֤���ݡ�
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
