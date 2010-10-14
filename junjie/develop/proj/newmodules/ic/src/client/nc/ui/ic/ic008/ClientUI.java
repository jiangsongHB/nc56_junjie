package nc.ui.ic.ic008;

/**
 * ����˵����
 * �������ڣ�(2003-2-20 10:55:46)
 * @author��������
 * �޸����ڣ�(2003-2-20 10:55:46)
 * �޸��ˣ�@author��������
 * �޸�ԭ��
 * ����˵����
 * 
 */
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.itf.uap.IVOPersistence;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.ui.ic.pub.LongTimeTask;
import nc.ui.ic.pub.bill.query.QueryConditionDlg;
import nc.ui.ic.pub.bill.query.QueryConditionDlgNew;
import nc.ui.ic.pub.query.QueryDlgUtil;
import nc.ui.ic.pub.tools.GenMethod;
import nc.ui.ml.NCLangRes;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.ToftPanel;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UITextArea;
import nc.vo.ic.pub.bill.QryConditionVO;
import nc.vo.ic.pub.lang.ResBase;
import nc.vo.ic.xcl.MdxclBVO;
import nc.vo.ic.xcl.MdxclVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.query.ConditionVO;
import nc.vo.querytemplate.TemplateInfo;

public class ClientUI extends ToftPanel {
	// ��ť
	private ButtonObject m_bofix = new ButtonObject(
			nc.ui.ml.NCLangRes.getInstance().getStrByID("40080226",
					"UPT40080226-000001")/* @res "�����ִ���" */,
			nc.ui.ml.NCLangRes.getInstance().getStrByID("40080226",
					"UPT40080226-000001")/* @res "�����ִ���" */, 2, "�����ִ���"); /*-=notranslate=-*/

	private ButtonObject m_boFixBarCode = new ButtonObject(
			nc.ui.ml.NCLangRes.getInstance().getStrByID("40080226",
					"UPT40080226-000002")/* @res "���������ִ���" */,
			nc.ui.ml.NCLangRes.getInstance().getStrByID("40080226",
					"UPT40080226-000002")/* @res "���������ִ���" */, 2, "���������ִ���"); /*-=notranslate=-*/

	private ButtonObject m_bofixMonth = new ButtonObject(ResBase
			.get008BtnMonth(), ResBase.get008BtnMonth(), 2, "�����½�����"); /*-=notranslate=-*/

	// ������ add 2010-10-14
	private ButtonObject m_boMdcs = new ButtonObject(nc.ui.ml.NCLangRes
			.getInstance().getStrByID("40083002", "�뵥����"), nc.ui.ml.NCLangRes
			.getInstance().getStrByID("40083002", "�뵥����"), 2, "�뵥����"); /*-=notranslate=-*/

	// ������ end

	private ButtonObject m_boFixMonthDBiz = new ButtonObject(ResBase
			.get008BtnMonth_Biz(), ResBase.get008BtnMonth_Biz(), 2,
			"������������½������"); /*-=notranslate=-*/

	private ButtonObject m_boFixMonthDaccount = new ButtonObject(ResBase
			.get008BtnMonth_acc(), ResBase.get008BtnMonth_acc(), 2,
			"����ǩ�������½������"); /*-=notranslate=-*/

	// TODO:���й��ʻ�
	private ButtonObject m_boMonthConfig = new ButtonObject(nc.ui.ml.NCLangRes
			.getInstance().getStrByID("40080226", "UPT40080226-000007"),
			nc.ui.ml.NCLangRes.getInstance().getStrByID("40080226",
					"UPT40080226-000007"), 2, "����½�����"); /*-=����½�����=-*/

	private ButtonObject m_boTest = new ButtonObject("Test", "Test", 2, "Test"); /*-=notranslate=-*/

	private static boolean DEBUG = false;

	// ������ �޸� 2010-10-14
	// ��ť��
	private ButtonObject[] m_MainButtonGroup = { m_bofix, m_bofixMonth,
			m_boFixBarCode, m_boMdcs };

	// ������ �޸� 2010-10-14 end

	// �����洢�½����öԻ����ʵ��
	private MonthConfigDlg m_monthConfigdlg;

	// ��˾�Ͳ���Ա
	String m_sCorpID = null;

	String m_sUserID = null;

	// �ڵ����
	private String m_sPNodeCode = "40080226";

	// ��ѯ�Ի���
	private QueryConditionDlgNew m_QueryConditionDlg = null;

	protected ArrayList m_alUserCorpID = null;

	private nc.ui.pub.beans.UILabel ivjUILbCondition = null;

	private nc.ui.pub.beans.UITextArea ivjUITACondition = null;

	private nc.ui.pub.beans.UIScrollPane ivjUIScrollPaneText = null;

	private UITextArea m_taTextArea1 = null;

	/**
	 * ClientUI ������ע�⡣
	 */
	public ClientUI() {
		super();
		initialize();
	}

	/**
	 * �˴����뷽��˵���� ���ߣ����Ӣ �������ڣ�(2001-6-18 10:47:32)
	 * 
	 * @return java.lang.String
	 */
	public void getCeInfo() {
		// ���ص�λ�����pk
		nc.ui.pub.ClientEnvironment ce = nc.ui.pub.ClientEnvironment
				.getInstance();
		try {
			m_sCorpID = ce.getCorporation().getPrimaryKey();
			m_sUserID = ce.getUser().getPrimaryKey();
		} catch (Exception e) {
			showErrorMessage(e.getMessage());
		}
		return;

	}

	/**
	 * �����ߣ������� ���ܣ��õ���ѯ�Ի��� ������ ���أ� ���⣺ ���ڣ�(2002-10-10 9:23:32)
	 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 */
	private QueryConditionDlgNew getConditionDlg() {
		if (m_QueryConditionDlg == null) {

			TemplateInfo info = QueryDlgUtil.getTemplateInfo(m_sCorpID,
					m_sPNodeCode, m_sUserID);
			m_QueryConditionDlg = new QueryConditionDlgNew(this, info);
			m_QueryConditionDlg.hideNormal();
			m_QueryConditionDlg.setVisibleAdvancePanel(false);

			// ����Ϊ�Բ��յĳ�ʼ��
			m_QueryConditionDlg.initQueryDlgRef();
			m_QueryConditionDlg.initCorpRef("pk_corp", m_sCorpID,
					m_alUserCorpID);

			m_QueryConditionDlg.setRefInitWhereClause("ccalbodyid", "�����֯",
					" property in (0,1) and pk_corp=", "pk_corp");

			m_QueryConditionDlg.setRefInitWhereClause("cwarehouseid", "�ֿ⵵��",
					"bd_stordoc.pk_calbody=", "ccalbodyid");
			// m_QueryConditionDlg.get
		}
		return m_QueryConditionDlg;
	}

	/**
	 * ����ʵ�ָ÷���������ҵ�����ı��⡣
	 * 
	 * @version (00-6-6 13:33:25)
	 * 
	 * @return java.lang.String
	 */
	public String getTitle() {
		return nc.ui.ml.NCLangRes.getInstance().getStrByID("4008busi",
				"UPT40080226-000001")/* @res "�����ִ���" */;

	}

	/**
	 * ���� m_taTextArea1 ����ֵ��
	 * 
	 * @return UITextArea
	 * 
	 * @author shaobing �۱� on Jun 30, 2005 ���Ӣ�ĵ���ʾ���⣨ʹ֮������ʾ����
	 */
	private UITextArea getTextAreaHint() {
		if (m_taTextArea1 == null) {
			try {
				String sText = nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"4008busi", "UPP4008busi-000237")/*
															 * @res
															 * "���������������ִ������е������ڵ���ǰ��Կ��ҵ����й��ˣ�"
															 */;
				sText += "\n\n";
				sText += nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"4008busi", "UPP4008busi-000238")/*
															 * @res
															 * "��������ִ����漰�ķ�Χ�ȽϹ㣬������ϵͳ����ʱ����е�����"
															 */;
				m_taTextArea1 = new UITextArea();
				m_taTextArea1.setName("UITextArea2");

				m_taTextArea1.setRows(5);
				m_taTextArea1.setLineWrap(true); // �Զ�����
				m_taTextArea1.setEditable(false); // ���ɱ༭
				m_taTextArea1.setEnabled(false); // ���ɼ���

				m_taTextArea1.setBackground(this.getBackground()); // ���ñ���ɫ
				m_taTextArea1.setDisabledTextColor(java.awt.Color.BLACK); // ����������ɫ

				m_taTextArea1.setText(sText);
				m_taTextArea1.setBounds(74, 62, 650, 80);
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return m_taTextArea1;
	}

	/**
	 * ���� UILbCondition ����ֵ��
	 * 
	 * @return nc.ui.pub.beans.UILabel
	 */
	/* ���棺�˷������������ɡ� */
	private nc.ui.pub.beans.UILabel getUILbCondition() {
		if (ivjUILbCondition == null) {
			try {
				ivjUILbCondition = new nc.ui.pub.beans.UILabel();
				ivjUILbCondition.setName("UILbCondition");
				ivjUILbCondition.setText(nc.ui.ml.NCLangRes.getInstance()
						.getStrByID("4008busi", "UPT40083620-000004")/*
																		 * @res
																		 * "��ѯ����"
																		 */);
				ivjUILbCondition.setBounds(74, 150, 150, 22);
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjUILbCondition;
	}

	/**
	 * ���� UIScrollPaneText ����ֵ��
	 * 
	 * @return nc.ui.pub.beans.UIScrollPane
	 */
	/* ���棺�˷������������ɡ� */
	private nc.ui.pub.beans.UIScrollPane getUIScrollPaneText() {
		if (ivjUIScrollPaneText == null) {
			try {
				ivjUIScrollPaneText = new nc.ui.pub.beans.UIScrollPane();
				ivjUIScrollPaneText.setName("UIScrollPaneText");

				ivjUIScrollPaneText.setToolTipText(nc.ui.ml.NCLangRes
						.getInstance().getStrByID("4008busi",
								"UPP4008busi-000239")/* @res "��ѯ������ʾ" */);
				ivjUIScrollPaneText.setAutoscrolls(true);
				ivjUIScrollPaneText.setBackground(java.awt.Color.blue);
				ivjUIScrollPaneText.setBounds(74, 180, 650, 110);
				ivjUIScrollPaneText.setViewportView(getUITACondition());
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjUIScrollPaneText;
	}

	/**
	 * ���� UITACondition ����ֵ��
	 * 
	 * @return nc.ui.pub.beans.UITextArea
	 */
	/* ���棺�˷������������ɡ� */
	private nc.ui.pub.beans.UITextArea getUITACondition() {
		if (ivjUITACondition == null) {
			try {
				ivjUITACondition = new nc.ui.pub.beans.UITextArea();
				ivjUITACondition.setName("UITACondition");
				ivjUITACondition.setRows(5);
				ivjUITACondition
						.setBackground(new java.awt.Color(204, 204, 204));
				ivjUITACondition.setBounds(74, 149, 596, 110);
				ivjUITACondition.setEditable(false);
				ivjUITACondition.setMaxLength(5000);
				ivjUITACondition.setLineWrap(true);

				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjUITACondition;
	}

	/**
	 * ÿ�������׳��쳣ʱ������
	 * 
	 * @param exception
	 *            java.lang.Throwable
	 */
	private void handleException(java.lang.Throwable exception) {

		/* ��ȥ���и��е�ע�ͣ��Խ�δ��׽�����쳣��ӡ�� stdout�� */
		// nc.vo.scm.pub.SCMEnv.out("--------- δ��׽�����쳣 ---------");
		// nc.vo.scm.pub.SCMEnv.error(exception);
	}

	private void initButtonMenu() {
		m_bofixMonth.addChildButton(m_boFixMonthDBiz);
		m_bofixMonth.addChildButton(m_boFixMonthDaccount);

		// add by hanbin ԭ������½����ð�ť
		m_bofixMonth.addChildButton(m_boMonthConfig);

		//
		if (DEBUG) {
			m_bofixMonth.addChildButton(m_boTest);
		}
	}

	/**
	 * ��ʼ���ࡣ
	 */
	/* ���棺�˷������������ɡ� */
	private void initialize() {
		try {
			// user code begin {1}
			// user code end
			setName("ClientUI");
			setLayout(null);
			setSize(774, 419);
			getCeInfo();
			initButtonMenu();
			setButtons(m_MainButtonGroup);

			add(getTextAreaHint(), getTextAreaHint().getName());
			add(getUILbCondition(), getUILbCondition().getName());
			add(getUIScrollPaneText(), "center");

		} catch (java.lang.Throwable ivjExc) {
			handleException(ivjExc);
		}
		// user code begin {2}
		// user code end
	}

	/**
	 * ����ڵ� - ��������ΪӦ�ó�������ʱ���������������
	 * 
	 * @param args
	 *            java.lang.String[]
	 */
	public static void main(java.lang.String[] args) {
		try {
			javax.swing.JFrame frame = new javax.swing.JFrame();
			ClientUI aClientUI;
			aClientUI = new ClientUI();
			frame.setContentPane(aClientUI);
			frame.setSize(aClientUI.getSize());
			frame.addWindowListener(new java.awt.event.WindowAdapter() {
				public void windowClosing(java.awt.event.WindowEvent e) {
					System.exit(0);
				};
			});
			frame.show();
			java.awt.Insets insets = frame.getInsets();
			frame.setSize(frame.getWidth() + insets.left + insets.right, frame
					.getHeight()
					+ insets.top + insets.bottom);
			frame.setVisible(true);
		} catch (Throwable exception) {
			nc.vo.scm.pub.SCMEnv.out("nc.ui.pub.ToftPanel �� main() �з����쳣");
			nc.vo.scm.pub.SCMEnv.error(exception);
		}
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
		if (bo == m_bofix)
			onfix();
		else if (bo == m_boFixBarCode)
			onfixBarCode();
		else if (bo == m_boFixMonthDBiz)
			onfixMonthData(0);

		else if (bo == m_boFixMonthDaccount)
			onfixMonthData(1);
		// ������  ���� ��ʼ 2010-10-14
		else if (bo==m_boMdcs)
			onBoMdcs();
		// ������ ���� ����
		
		// add by hanbin,�����������½����á���ť
		else if (bo == m_boMonthConfig)
			this.onMonthConfig();

		else if (bo == m_boTest) {
			if (DEBUG) {
				onTest();
			}
		}

	}
	
	
	// �뵥����  ���������� 
	private void onBoMdcs() {
		IUAPQueryBS iUAPQueryBS = (IUAPQueryBS) NCLocator.getInstance().lookup(
				IUAPQueryBS.class.getName());
		int rs = showYesNoMessage("��ȷ��Ҫ�Գ�����뵥���������������ص�ǰ���㣬�˲��������棡");
		if (rs == 8||rs==2)
			return;
		String pk_corp = getClientEnvironment().getCorporation().getPk_corp();
		String sqlWhere = " dr=0 and pk_mdxcl in(";
		try {
			Collection coll_h = iUAPQueryBS.retrieveByClause(MdxclVO.class,
					" dr=0 and pk_corp='" + pk_corp + "'");
			if (coll_h == null || coll_h.size() == 0) {
				showWarningMessage("û���뵥���ݣ�����ʧ�ܣ�");
				return;
			}
			MdxclVO[] hvos = new MdxclVO[coll_h.size()];
			coll_h.toArray(hvos);
			for (int h = 0; h < hvos.length; h++) {
				sqlWhere += "'" + hvos[h].getPk_mdxcl() + "',";
			}
			sqlWhere = sqlWhere.substring(0, sqlWhere.length() - 1);
			sqlWhere = sqlWhere + ")";
			Collection coll = iUAPQueryBS.retrieveByClause(MdxclBVO.class,
					sqlWhere);
			if (coll == null || coll.size() == 0) {
				showWarningMessage("û���뵥���ݣ�����ʧ�ܣ�");
				return;
			}
			MdxclBVO[] xclbArrays = new MdxclBVO[coll.size()];
			coll.toArray(xclbArrays);
			String sql_c = "select tt1.pk_mdxcl_b,"
					+ " (nvl(tt1.t1_srkzs, 0) - nvl(tt2.t2_srkzs, 0)) as xczs,"
					+ " (nvl(tt1.t1_srkzl, 0) - nvl(tt2.t2_srkzl, 0)) as xczl"
					+ " from (select t1.pk_mdxcl_b,"
					+ " sum(t1.srkzs) as t1_srkzs,"
					+ " sum(t1.srkzl) as t1_srkzl" + " from nc_mdcrk t1"
					+ " where t1.dr = 0 and t1.pk_corp='" + pk_corp + "'"
					+ " and t1.cbodybilltypecode in ('45', '4A')"
					+ " group by t1.pk_mdxcl_b) tt1"
					+ " left join (select t2.pk_mdxcl_b,"
					+ " sum(t2.srkzs) as t2_srkzs,"
					+ " sum(t2.srkzl) as t2_srkzl" + " from nc_mdcrk t2"
					+ " where t2.dr = 0 and t2.pk_corp='" + pk_corp + "'"
					+ " and t2.cbodybilltypecode in ('4I', '4C')"
					+ " group by t2.pk_mdxcl_b) tt2 on tt1.pk_mdxcl_b ="
					+ " tt2.pk_mdxcl_b";
			ArrayList crkList = (ArrayList) iUAPQueryBS.executeQuery(sql_c,
					new MapListProcessor());
			if (crkList == null || crkList.size() == 0) {
				showWarningMessage("û���뵥����ⵥ���ݣ�����ʧ�ܣ�");
				return;
			}
			Map crkMap = new HashMap();
			for (int j = 0; j < crkList.size(); j++) {
				Map crk = (Map) crkList.get(j);
				String key = (String) crk.get("pk_mdxcl_b");
				crkMap.put(key, crk);
			}
			for (int i = 0; i < xclbArrays.length; i++) {
				xclbArrays[i].setZhishu(new UFDouble(0));// ֧�����
				xclbArrays[i].setZhongliang(new UFDouble(0));// �������
				String mdxclb_pk = xclbArrays[i].getPk_mdxcl_b();
				Map crkObj = (Map) crkMap.get(mdxclb_pk);
				if (crkObj == null)
					continue;
				else {
					Object zsObj = crkObj.get("xczs");
					if (zsObj instanceof BigDecimal) {
						BigDecimal zs = (BigDecimal) crkObj.get("xczs");
						xclbArrays[i].setZhishu(new UFDouble(zs));
					} else if (zsObj instanceof Integer) {
						Integer zs = (Integer) crkObj.get("xczs");
						xclbArrays[i].setZhishu(new UFDouble(zs));
					} else {
						showWarningMessage("֧���������Ͳ���ȷ������ʧ�ܣ�");
						return;
					}
					Object zlobj = crkObj.get("xczl");
					if (zlobj instanceof BigDecimal) {
						BigDecimal zl = (BigDecimal) crkObj.get("xczl");
						xclbArrays[i].setZhongliang(new UFDouble(zl));
					} else if (zlobj instanceof Integer) {
						Integer zl = (Integer) crkObj.get("xczl");
						xclbArrays[i].setZhongliang(new UFDouble(zl));
					} else {
						showWarningMessage("�����������Ͳ���ȷ������ʧ�ܣ�");
						return;
					}

				}
			}
			IVOPersistence iVOPersistence = (IVOPersistence) NCLocator
					.getInstance().lookup(IVOPersistence.class.getName());
			iVOPersistence.updateVOArray(xclbArrays);
			showWarningMessage("�뵥����ɹ���");
			return;
		} catch (BusinessException e) {
			e.printStackTrace();
			showWarningMessage("�뵥����ʧ��:" + e.getMessage());
			return;
		}

	}
	
	// ������ ����end

	private void onTestAdjustXCL(ConditionVO[] voa) {
		try {
			LongTimeTask.callICService(this, "Test...",
					"nc.bs.ic.pub.monthsum.MonthQuery", "testAdjustXCL",
					new Class[] { ConditionVO[].class }, new Object[] { voa });
		} catch (Exception e) {
			showErrorMessage("failed!");
			return;
		}
	}

	private void onTest() {
		// �½�alertԤ������
		// try {
		// LongTimeTask.callICEJBService(this, "Test...",
		// "nc.bs.ic.pub.monthsum.MonthServ", "insert_month_alert",
		// new Class[]{},new Object[]{});
		// } catch (Exception e) {
		// showErrorMessage("failed!");
		// return;
		// }

		// �½�������ݲ���
		// try {
		// LongTimeTask.callICEJBService(this, "Test...",
		// "nc.bs.ic.pub.monthsum.MonthServ", "insert_all",
		// new Class[]{Integer.class},new Object[]{Integer.valueOf(0)});
		// } catch (Exception e) {
		// showErrorMessage("failed!");
		// return;
		// }

		// �ɹ��Ƿ����ӿڲ���
		// try {
		// String[] sa = new String[]{"12766610000000008JZ1",
		// "12766610000000008JZ2"};
		// LongTimeTask.callICEJBService(this, "Test...qry",
		// "nc.bs.ic.ic201.GeneralHDMO", "qryIfSettleOrCalprice",
		// new Class[]{String[].class},new Object[]{sa});
		// } catch (Exception e) {
		// showErrorMessage("failed!");
		// return;
		// }

		// ��������
		// try {
		// LongTimeTask.callICEJBService(this, "Test...",
		// "nc.bs.ic.upgrade211to220.Upgrade",
		// "upgradeV5", new Class[] {}, new Object[] {});
		// } catch (Exception e) {
		// showErrorMessage("failed!");
		// return;
		// }

		// �½��ѯ�ӿڲ���
		try {
			LongTimeTask.callICEJBService(this, "Test...",
					"nc.bs.ic.pub.monthsum.MonthQuery", "test_invman",
					new Class[] { String[].class, UFDate.class }, new Object[] {
							new String[] { "0001661000000005AHIL" },
							new UFDate(System.currentTimeMillis()) });
		} catch (Exception e) {
			showErrorMessage("failed!");
			return;
		}

	}

	private MonthCalDlg m_monthdlg;

	private MonthCalDlg getMonthCalDlg() {
		if (m_monthdlg == null)
			m_monthdlg = new MonthCalDlg(this);
		return m_monthdlg;
	}

	private void onfixMonthData(int iMode) {

		MonthCalDlg dlg = getMonthCalDlg();
		if (dlg.showModal() == UIDialog.ID_OK) {

			UFDate startdate = dlg.getCalcStartDate();
			if (startdate == null) {
				showErrorMessage(NCLangRes.getInstance().getStrByID("40080226",
						"ClientUI-000000")/* ��ѡ��ʼ�ڼ� */);
				return;
			}
			String pk_corp = dlg.getPk_corp();
			if (pk_corp == null || pk_corp.trim().length() <= 0) {
				int iyes = showYesNoMessage("��˾û��ѡ�񣬽������������ŵ�ҵ������(�⽫���Ľϳ�ʱ��)���Ƿ����?");
				if (iyes != MessageDialog.ID_YES)
					return;
			}

			String calcstart = startdate.toString().substring(0, 7);

			this.showHintMessage(NCLangRes.getInstance().getStrByID("40080226",
					"ClientUI-000001")/* ���������½�����... */);
			try {
				LongTimeTask
						.callICEJBService(this, "���������½�����...",
								"nc.bs.ic.pub.monthsum.MonthServ",
								"insert_all", new Class[] { Integer.class,
										String.class, String.class },
								new Object[] { new Integer(iMode), calcstart,
										pk_corp });
				if (iMode == 0)
					showHintMessage(m_boFixMonthDBiz.getName()
							+ ResBase.getSuccess());
				else if (iMode == 1)
					showHintMessage(m_boFixMonthDaccount.getName()
							+ ResBase.getSuccess());
				else
					showHintMessage(m_bofixMonth.getName()
							+ ResBase.getSuccess());
			} catch (Exception e) {
				GenMethod.handleException(this, "����", e);

				this.showHintMessage(NCLangRes.getInstance().getStrByID(
						"40080226", "ClientUI-000002")/* �½���������ʧ��... */);
				return;
			}

			showWarningMessage(NCLangRes.getInstance().getStrByID("40080226",
					"ClientUI-000003")/* �½��������ɳɹ�! */);
			this.showHintMessage(NCLangRes.getInstance().getStrByID("40080226",
					"ClientUI-000004")/* �½��������ɳɹ�... */);

		}
		// else{
		// this.showHintMessage("���������½�����...");
		// try {
		// LongTimeTask.callICEJBService(this, "���������½�����...",
		// "nc.bs.ic.pub.monthsum.MonthServ", "insert_all",
		// new Class[] { Integer.class}, new Object[] { new Integer(
		// iMode)});
		// if (iMode == 0)
		// showHintMessage(m_boFixMonthDBiz.getName()
		// + ResBase.getSuccess());
		// else if (iMode == 1)
		// showHintMessage(m_boFixMonthDaccount.getName()
		// + ResBase.getSuccess());
		// else
		// showHintMessage(m_bofixMonth.getName() + ResBase.getSuccess());
		// } catch (Exception e) {
		// GenMethod.handleException(this,"����",e);
		//        
		// this.showHintMessage("�½���������ʧ��...");
		// return;
		// }
		//  
		// showWarningMessage("�½��������ɳɹ�!");
		// this.showHintMessage("�½��������ɳɹ�...");
		// }

	}

	/**
	 * ����˵���� �������ڣ�(2002-11-21 16:21:25) ���ߣ������� �޸����ڣ� �޸��ˣ� �޸�ԭ�� �㷨˵����
	 */
	public void onfix() {
		try {

			if (getConditionDlg().showModal() != nc.ui.pub.beans.UIDialog.ID_OK)
				// ȡ������
				return;
			nc.vo.pub.query.ConditionVO[] voCons = getConditionDlg()
					.getQryCondEditor().getGeneralCondtionVOs();
			// �õ������ѯ�������
			if (voCons != null && voCons.length > 0) {
				for (int i = 0; i < voCons.length; i++) {

					if ("like".equals(voCons[i].getOperaCode().toString())) {
						voCons[i].setValue(voCons[i].getValue() + "%");
					}
				}
			}
			// ��ʾ��
			getUITACondition().setText(getConditionDlg().getSqlDscrpt());

			// v5 : Test Code ִ�е����ִ���
			if (DEBUG) {
				// onTestAdjustXCL(voCons);
			}

			// ִ�е����ִ�����
			ArrayList alMsg = (ArrayList) LongTimeTask.callICService(this,
					nc.ui.ml.NCLangRes.getInstance().getStrByID("4008busi",
							"UPT40080226-000001")/* @res "�����ִ���" */,
					"nc.ui.ic.ic008.FixOnhandnumHelper", "fixOnhandnum",
					new Class[] { nc.vo.pub.query.ConditionVO[].class },
					new Object[] { voCons });

			// FixOnhandnumHelper.fixOnhandnum(voCons);

			if (alMsg != null && alMsg.size() > 0) {
				String sMsg = (String) alMsg.get(0);
				showHintMessage(sMsg);
			} else
				showHintMessage(m_bofix.getName() + ResBase.getSuccess());// nc.ui.ml.NCLangRes.getInstance().getStrByID("4008busi","UPP4008busi-000240")/*@res
			// "�����ִ������"*/);
		} catch (Exception e) {
			reportException(e);
			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"4008busi", "UPP4008busi-000241")/* @res "�����ִ����д���" */);
			showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"4008busi", "UPP4008busi-000241")/* @res "�����ִ����д���" */
					+ "\n" + e.getMessage());

		}

	}

	/**
	 * �����ˣ������� ����ʱ�䣺2008-9-18 ����02:11:59 ����ԭ�������ִ�������
	 * 
	 * @param voaCond
	 * @return
	 * @throws BusinessException
	 */
	public void onfixBarCode() {
		try {

			if (getConditionDlg().showModal() != nc.ui.pub.beans.UIDialog.ID_OK)
				// ȡ������
				return;
			nc.vo.pub.query.ConditionVO[] voCons = getConditionDlg()
					.getQryCondEditor().getGeneralCondtionVOs();
			// �õ������ѯ�������
			if (voCons != null && voCons.length > 0) {
				for (int i = 0; i < voCons.length; i++) {

					if ("like".equals(voCons[i].getOperaCode().toString())) {
						voCons[i].setValue(voCons[i].getValue() + "%");
					}
				}
			}
			// ��ʾ��
			getUITACondition().setText(getConditionDlg().getSqlDscrpt());

			// ִ�е��������ִ�����
			ArrayList alMsg = (ArrayList) LongTimeTask.callICService(this,
					nc.ui.ml.NCLangRes.getInstance().getStrByID("4008busi",
							"UPT40080226-000001")/* @res "���������ִ���" */,
					"nc.ui.ic.ic008.FixOnhandnumHelper", "fixOnhandBBCnum",
					new Class[] { nc.vo.pub.query.ConditionVO[].class },
					new Object[] { voCons });

			if (alMsg != null && alMsg.size() > 0) {
				String sMsg = (String) alMsg.get(0);
				showHintMessage(sMsg);
			} else
				showHintMessage(m_bofix.getName() + ResBase.getSuccess());// nc.ui.ml.NCLangRes.getInstance().getStrByID("4008busi","UPP4008busi-000240")/*@res
			// "���������ִ������"*/);
		} catch (Exception e) {
			reportException(e);
			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"4008busi", "UPP4008busi-000319")/* @res "���������ִ����д���" */);
			showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"4008busi", "UPP4008busi-000319")/* @res "���������ִ����д���" */
					+ "\n" + e.getMessage());

		}

	}

	/**
	 * Created on 2009-8-17
	 * <p>
	 * Discription:[����½����öԻ����ʵ������]
	 * </p>
	 * 
	 * @return �½����öԻ���
	 * @author: ���� hanbin@ufida.com.cn
	 * @update:[����YYYY-MM-DD] [����������]
	 */
	private MonthConfigDlg getMonthConfigDlg() {
		if (m_monthConfigdlg == null)
			m_monthConfigdlg = new MonthConfigDlg(this);
		return m_monthConfigdlg;
	}

	/**
	 * Created on 2009-8-17
	 * <p>
	 * Discription:[�������½����ð�ť�ĵ���¼�]
	 * </p>
	 * 
	 * @author: ���� hanbin@ufida.com.cn
	 * @update:[����YYYY-MM-DD] [����������]
	 */
	private void onMonthConfig() {
		// ��öԻ���
		MonthConfigDlg dlg = this.getMonthConfigDlg();
		// ��������
		dlg.loadConfigData();
		// ��ʾ�Ի���
		dlg.showModal();
	}
}