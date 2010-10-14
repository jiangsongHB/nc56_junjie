package nc.ui.ic.ic008;

/**
 * 类型说明。
 * 创建日期：(2003-2-20 10:55:46)
 * @author：程起伍
 * 修改日期：(2003-2-20 10:55:46)
 * 修改人：@author：程起伍
 * 修改原因：
 * 其它说明：
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
	// 按钮
	private ButtonObject m_bofix = new ButtonObject(
			nc.ui.ml.NCLangRes.getInstance().getStrByID("40080226",
					"UPT40080226-000001")/* @res "调整现存量" */,
			nc.ui.ml.NCLangRes.getInstance().getStrByID("40080226",
					"UPT40080226-000001")/* @res "调整现存量" */, 2, "调整现存量"); /*-=notranslate=-*/

	private ButtonObject m_boFixBarCode = new ButtonObject(
			nc.ui.ml.NCLangRes.getInstance().getStrByID("40080226",
					"UPT40080226-000002")/* @res "调整条码现存量" */,
			nc.ui.ml.NCLangRes.getInstance().getStrByID("40080226",
					"UPT40080226-000002")/* @res "调整条码现存量" */, 2, "调整条码现存量"); /*-=notranslate=-*/

	private ButtonObject m_bofixMonth = new ButtonObject(ResBase
			.get008BtnMonth(), ResBase.get008BtnMonth(), 2, "调整月结数据"); /*-=notranslate=-*/

	// 何意求 add 2010-10-14
	private ButtonObject m_boMdcs = new ButtonObject(nc.ui.ml.NCLangRes
			.getInstance().getStrByID("40083002", "码单重算"), nc.ui.ml.NCLangRes
			.getInstance().getStrByID("40083002", "码单重算"), 2, "码单重算"); /*-=notranslate=-*/

	// 何意求 end

	private ButtonObject m_boFixMonthDBiz = new ButtonObject(ResBase
			.get008BtnMonth_Biz(), ResBase.get008BtnMonth_Biz(), 2,
			"调整入库日期月结存数据"); /*-=notranslate=-*/

	private ButtonObject m_boFixMonthDaccount = new ButtonObject(ResBase
			.get008BtnMonth_acc(), ResBase.get008BtnMonth_acc(), 2,
			"调整签字日期月结存数据"); /*-=notranslate=-*/

	// TODO:进行国际化
	private ButtonObject m_boMonthConfig = new ButtonObject(nc.ui.ml.NCLangRes
			.getInstance().getStrByID("40080226", "UPT40080226-000007"),
			nc.ui.ml.NCLangRes.getInstance().getStrByID("40080226",
					"UPT40080226-000007"), 2, "库存月结配置"); /*-=库存月结配置=-*/

	private ButtonObject m_boTest = new ButtonObject("Test", "Test", 2, "Test"); /*-=notranslate=-*/

	private static boolean DEBUG = false;

	// 何意求 修改 2010-10-14
	// 按钮组
	private ButtonObject[] m_MainButtonGroup = { m_bofix, m_bofixMonth,
			m_boFixBarCode, m_boMdcs };

	// 何意求 修改 2010-10-14 end

	// 用来存储月结配置对话框的实例
	private MonthConfigDlg m_monthConfigdlg;

	// 公司和操作员
	String m_sCorpID = null;

	String m_sUserID = null;

	// 节点编码
	private String m_sPNodeCode = "40080226";

	// 查询对话框
	private QueryConditionDlgNew m_QueryConditionDlg = null;

	protected ArrayList m_alUserCorpID = null;

	private nc.ui.pub.beans.UILabel ivjUILbCondition = null;

	private nc.ui.pub.beans.UITextArea ivjUITACondition = null;

	private nc.ui.pub.beans.UIScrollPane ivjUIScrollPaneText = null;

	private UITextArea m_taTextArea1 = null;

	/**
	 * ClientUI 构造子注解。
	 */
	public ClientUI() {
		super();
		initialize();
	}

	/**
	 * 此处插入方法说明。 作者：余大英 创建日期：(2001-6-18 10:47:32)
	 * 
	 * @return java.lang.String
	 */
	public void getCeInfo() {
		// 返回单位编码的pk
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
	 * 创建者：程起舞 功能：得到查询对话框 参数： 返回： 例外： 日期：(2002-10-10 9:23:32)
	 * 修改日期，修改人，修改原因，注释标志：
	 * 
	 */
	private QueryConditionDlgNew getConditionDlg() {
		if (m_QueryConditionDlg == null) {

			TemplateInfo info = QueryDlgUtil.getTemplateInfo(m_sCorpID,
					m_sPNodeCode, m_sUserID);
			m_QueryConditionDlg = new QueryConditionDlgNew(this, info);
			m_QueryConditionDlg.hideNormal();
			m_QueryConditionDlg.setVisibleAdvancePanel(false);

			// 以下为对参照的初始化
			m_QueryConditionDlg.initQueryDlgRef();
			m_QueryConditionDlg.initCorpRef("pk_corp", m_sCorpID,
					m_alUserCorpID);

			m_QueryConditionDlg.setRefInitWhereClause("ccalbodyid", "库存组织",
					" property in (0,1) and pk_corp=", "pk_corp");

			m_QueryConditionDlg.setRefInitWhereClause("cwarehouseid", "仓库档案",
					"bd_stordoc.pk_calbody=", "ccalbodyid");
			// m_QueryConditionDlg.get
		}
		return m_QueryConditionDlg;
	}

	/**
	 * 子类实现该方法，返回业务界面的标题。
	 * 
	 * @version (00-6-6 13:33:25)
	 * 
	 * @return java.lang.String
	 */
	public String getTitle() {
		return nc.ui.ml.NCLangRes.getInstance().getStrByID("4008busi",
				"UPT40080226-000001")/* @res "调整现存量" */;

	}

	/**
	 * 返回 m_taTextArea1 特性值。
	 * 
	 * @return UITextArea
	 * 
	 * @author shaobing 邵兵 on Jun 30, 2005 解决英文的显示问题（使之完整显示）。
	 */
	private UITextArea getTextAreaHint() {
		if (m_taTextArea1 == null) {
			try {
				String sText = nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"4008busi", "UPP4008busi-000237")/*
															 * @res
															 * "根据以下条件对现存量进行调整。在调整前请对库存业务进行关账！"
															 */;
				sText += "\n\n";
				sText += nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"4008busi", "UPP4008busi-000238")/*
															 * @res
															 * "如果调整现存量涉及的范围比较广，建议在系统空闲时间进行调整！"
															 */;
				m_taTextArea1 = new UITextArea();
				m_taTextArea1.setName("UITextArea2");

				m_taTextArea1.setRows(5);
				m_taTextArea1.setLineWrap(true); // 自动换行
				m_taTextArea1.setEditable(false); // 不可编辑
				m_taTextArea1.setEnabled(false); // 不可激活

				m_taTextArea1.setBackground(this.getBackground()); // 设置背景色
				m_taTextArea1.setDisabledTextColor(java.awt.Color.BLACK); // 设置字体颜色

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
	 * 返回 UILbCondition 特性值。
	 * 
	 * @return nc.ui.pub.beans.UILabel
	 */
	/* 警告：此方法将重新生成。 */
	private nc.ui.pub.beans.UILabel getUILbCondition() {
		if (ivjUILbCondition == null) {
			try {
				ivjUILbCondition = new nc.ui.pub.beans.UILabel();
				ivjUILbCondition.setName("UILbCondition");
				ivjUILbCondition.setText(nc.ui.ml.NCLangRes.getInstance()
						.getStrByID("4008busi", "UPT40083620-000004")/*
																		 * @res
																		 * "查询条件"
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
	 * 返回 UIScrollPaneText 特性值。
	 * 
	 * @return nc.ui.pub.beans.UIScrollPane
	 */
	/* 警告：此方法将重新生成。 */
	private nc.ui.pub.beans.UIScrollPane getUIScrollPaneText() {
		if (ivjUIScrollPaneText == null) {
			try {
				ivjUIScrollPaneText = new nc.ui.pub.beans.UIScrollPane();
				ivjUIScrollPaneText.setName("UIScrollPaneText");

				ivjUIScrollPaneText.setToolTipText(nc.ui.ml.NCLangRes
						.getInstance().getStrByID("4008busi",
								"UPP4008busi-000239")/* @res "查询条件显示" */);
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
	 * 返回 UITACondition 特性值。
	 * 
	 * @return nc.ui.pub.beans.UITextArea
	 */
	/* 警告：此方法将重新生成。 */
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
	 * 每当部件抛出异常时被调用
	 * 
	 * @param exception
	 *            java.lang.Throwable
	 */
	private void handleException(java.lang.Throwable exception) {

		/* 除去下列各行的注释，以将未捕捉到的异常打印至 stdout。 */
		// nc.vo.scm.pub.SCMEnv.out("--------- 未捕捉到的异常 ---------");
		// nc.vo.scm.pub.SCMEnv.error(exception);
	}

	private void initButtonMenu() {
		m_bofixMonth.addChildButton(m_boFixMonthDBiz);
		m_bofixMonth.addChildButton(m_boFixMonthDaccount);

		// add by hanbin 原因：添加月结配置按钮
		m_bofixMonth.addChildButton(m_boMonthConfig);

		//
		if (DEBUG) {
			m_bofixMonth.addChildButton(m_boTest);
		}
	}

	/**
	 * 初始化类。
	 */
	/* 警告：此方法将重新生成。 */
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
	 * 主入口点 - 当部件作为应用程序运行时，启动这个部件。
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
			nc.vo.scm.pub.SCMEnv.out("nc.ui.pub.ToftPanel 的 main() 中发生异常");
			nc.vo.scm.pub.SCMEnv.error(exception);
		}
	}

	/**
	 * 子类实现该方法，响应按钮事件。
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
		// 何意求  增加 开始 2010-10-14
		else if (bo==m_boMdcs)
			onBoMdcs();
		// 何意求 增加 结束
		
		// add by hanbin,用来监听“月结配置”按钮
		else if (bo == m_boMonthConfig)
			this.onMonthConfig();

		else if (bo == m_boTest) {
			if (DEBUG) {
				onTest();
			}
		}

	}
	
	
	// 码单重算  何意求增加 
	private void onBoMdcs() {
		IUAPQueryBS iUAPQueryBS = (IUAPQueryBS) NCLocator.getInstance().lookup(
				IUAPQueryBS.class.getName());
		int rs = showYesNoMessage("您确定要对出入库码单进行重算吗？请慎重当前重算，此操作不可逆！");
		if (rs == 8||rs==2)
			return;
		String pk_corp = getClientEnvironment().getCorporation().getPk_corp();
		String sqlWhere = " dr=0 and pk_mdxcl in(";
		try {
			Collection coll_h = iUAPQueryBS.retrieveByClause(MdxclVO.class,
					" dr=0 and pk_corp='" + pk_corp + "'");
			if (coll_h == null || coll_h.size() == 0) {
				showWarningMessage("没有码单数据，重算失败！");
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
				showWarningMessage("没有码单数据，重算失败！");
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
				showWarningMessage("没有码单出入库单数据，重算失败！");
				return;
			}
			Map crkMap = new HashMap();
			for (int j = 0; j < crkList.size(); j++) {
				Map crk = (Map) crkList.get(j);
				String key = (String) crk.get("pk_mdxcl_b");
				crkMap.put(key, crk);
			}
			for (int i = 0; i < xclbArrays.length; i++) {
				xclbArrays[i].setZhishu(new UFDouble(0));// 支数清空
				xclbArrays[i].setZhongliang(new UFDouble(0));// 重量清空
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
						showWarningMessage("支数数据类型不正确，重算失败！");
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
						showWarningMessage("重量数据类型不正确，重算失败！");
						return;
					}

				}
			}
			IVOPersistence iVOPersistence = (IVOPersistence) NCLocator
					.getInstance().lookup(IVOPersistence.class.getName());
			iVOPersistence.updateVOArray(xclbArrays);
			showWarningMessage("码单重算成功！");
			return;
		} catch (BusinessException e) {
			e.printStackTrace();
			showWarningMessage("码单重算失败:" + e.getMessage());
			return;
		}

	}
	
	// 何意求 增加end

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
		// 月结alert预警测试
		// try {
		// LongTimeTask.callICEJBService(this, "Test...",
		// "nc.bs.ic.pub.monthsum.MonthServ", "insert_month_alert",
		// new Class[]{},new Object[]{});
		// } catch (Exception e) {
		// showErrorMessage("failed!");
		// return;
		// }

		// 月结插入数据测试
		// try {
		// LongTimeTask.callICEJBService(this, "Test...",
		// "nc.bs.ic.pub.monthsum.MonthServ", "insert_all",
		// new Class[]{Integer.class},new Object[]{Integer.valueOf(0)});
		// } catch (Exception e) {
		// showErrorMessage("failed!");
		// return;
		// }

		// 采购是否结算接口测试
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

		// 升级测试
		// try {
		// LongTimeTask.callICEJBService(this, "Test...",
		// "nc.bs.ic.upgrade211to220.Upgrade",
		// "upgradeV5", new Class[] {}, new Object[] {});
		// } catch (Exception e) {
		// showErrorMessage("failed!");
		// return;
		// }

		// 月结查询接口测试
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
						"ClientUI-000000")/* 请选择开始期间 */);
				return;
			}
			String pk_corp = dlg.getPk_corp();
			if (pk_corp == null || pk_corp.trim().length() <= 0) {
				int iyes = showYesNoMessage("公司没有选择，将计算整个集团的业结数据(这将消耗较长时间)，是否继续?");
				if (iyes != MessageDialog.ID_YES)
					return;
			}

			String calcstart = startdate.toString().substring(0, 7);

			this.showHintMessage(NCLangRes.getInstance().getStrByID("40080226",
					"ClientUI-000001")/* 正在生成月结数据... */);
			try {
				LongTimeTask
						.callICEJBService(this, "正在生成月结数据...",
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
				GenMethod.handleException(this, "错误", e);

				this.showHintMessage(NCLangRes.getInstance().getStrByID(
						"40080226", "ClientUI-000002")/* 月结数据生成失败... */);
				return;
			}

			showWarningMessage(NCLangRes.getInstance().getStrByID("40080226",
					"ClientUI-000003")/* 月结数据生成成功! */);
			this.showHintMessage(NCLangRes.getInstance().getStrByID("40080226",
					"ClientUI-000004")/* 月结数据生成成功... */);

		}
		// else{
		// this.showHintMessage("正在生成月结数据...");
		// try {
		// LongTimeTask.callICEJBService(this, "正在生成月结数据...",
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
		// GenMethod.handleException(this,"错误",e);
		//        
		// this.showHintMessage("月结数据生成失败...");
		// return;
		// }
		//  
		// showWarningMessage("月结数据生成成功!");
		// this.showHintMessage("月结数据生成成功...");
		// }

	}

	/**
	 * 类型说明： 创建日期：(2002-11-21 16:21:25) 作者：程起伍 修改日期： 修改人： 修改原因： 算法说明：
	 */
	public void onfix() {
		try {

			if (getConditionDlg().showModal() != nc.ui.pub.beans.UIDialog.ID_OK)
				// 取消返回
				return;
			nc.vo.pub.query.ConditionVO[] voCons = getConditionDlg()
					.getQryCondEditor().getGeneralCondtionVOs();
			// 得到汉语查询条件语句
			if (voCons != null && voCons.length > 0) {
				for (int i = 0; i < voCons.length; i++) {

					if ("like".equals(voCons[i].getOperaCode().toString())) {
						voCons[i].setValue(voCons[i].getValue() + "%");
					}
				}
			}
			// 显示：
			getUITACondition().setText(getConditionDlg().getSqlDscrpt());

			// v5 : Test Code 执行调整现存量
			if (DEBUG) {
				// onTestAdjustXCL(voCons);
			}

			// 执行调整现存量：
			ArrayList alMsg = (ArrayList) LongTimeTask.callICService(this,
					nc.ui.ml.NCLangRes.getInstance().getStrByID("4008busi",
							"UPT40080226-000001")/* @res "调整现存量" */,
					"nc.ui.ic.ic008.FixOnhandnumHelper", "fixOnhandnum",
					new Class[] { nc.vo.pub.query.ConditionVO[].class },
					new Object[] { voCons });

			// FixOnhandnumHelper.fixOnhandnum(voCons);

			if (alMsg != null && alMsg.size() > 0) {
				String sMsg = (String) alMsg.get(0);
				showHintMessage(sMsg);
			} else
				showHintMessage(m_bofix.getName() + ResBase.getSuccess());// nc.ui.ml.NCLangRes.getInstance().getStrByID("4008busi","UPP4008busi-000240")/*@res
			// "调整现存量完毕"*/);
		} catch (Exception e) {
			reportException(e);
			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"4008busi", "UPP4008busi-000241")/* @res "调整现存量有错误！" */);
			showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"4008busi", "UPP4008busi-000241")/* @res "调整现存量有错误！" */
					+ "\n" + e.getMessage());

		}

	}

	/**
	 * 创建人：刘家清 创建时间：2008-9-18 下午02:11:59 创建原因：条码现存量调整
	 * 
	 * @param voaCond
	 * @return
	 * @throws BusinessException
	 */
	public void onfixBarCode() {
		try {

			if (getConditionDlg().showModal() != nc.ui.pub.beans.UIDialog.ID_OK)
				// 取消返回
				return;
			nc.vo.pub.query.ConditionVO[] voCons = getConditionDlg()
					.getQryCondEditor().getGeneralCondtionVOs();
			// 得到汉语查询条件语句
			if (voCons != null && voCons.length > 0) {
				for (int i = 0; i < voCons.length; i++) {

					if ("like".equals(voCons[i].getOperaCode().toString())) {
						voCons[i].setValue(voCons[i].getValue() + "%");
					}
				}
			}
			// 显示：
			getUITACondition().setText(getConditionDlg().getSqlDscrpt());

			// 执行调整条码现存量：
			ArrayList alMsg = (ArrayList) LongTimeTask.callICService(this,
					nc.ui.ml.NCLangRes.getInstance().getStrByID("4008busi",
							"UPT40080226-000001")/* @res "调整条码现存量" */,
					"nc.ui.ic.ic008.FixOnhandnumHelper", "fixOnhandBBCnum",
					new Class[] { nc.vo.pub.query.ConditionVO[].class },
					new Object[] { voCons });

			if (alMsg != null && alMsg.size() > 0) {
				String sMsg = (String) alMsg.get(0);
				showHintMessage(sMsg);
			} else
				showHintMessage(m_bofix.getName() + ResBase.getSuccess());// nc.ui.ml.NCLangRes.getInstance().getStrByID("4008busi","UPP4008busi-000240")/*@res
			// "调整条码现存量完毕"*/);
		} catch (Exception e) {
			reportException(e);
			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"4008busi", "UPP4008busi-000319")/* @res "调整条码现存量有错误！" */);
			showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"4008busi", "UPP4008busi-000319")/* @res "调整条码现存量有错误！" */
					+ "\n" + e.getMessage());

		}

	}

	/**
	 * Created on 2009-8-17
	 * <p>
	 * Discription:[获得月结配置对话框的实例对象]
	 * </p>
	 * 
	 * @return 月结配置对话框
	 * @author: 韩彬 hanbin@ufida.com.cn
	 * @update:[日期YYYY-MM-DD] [更改人姓名]
	 */
	private MonthConfigDlg getMonthConfigDlg() {
		if (m_monthConfigdlg == null)
			m_monthConfigdlg = new MonthConfigDlg(this);
		return m_monthConfigdlg;
	}

	/**
	 * Created on 2009-8-17
	 * <p>
	 * Discription:[处理库存月结配置按钮的点击事件]
	 * </p>
	 * 
	 * @author: 韩彬 hanbin@ufida.com.cn
	 * @update:[日期YYYY-MM-DD] [更改人姓名]
	 */
	private void onMonthConfig() {
		// 获得对话框
		MonthConfigDlg dlg = this.getMonthConfigDlg();
		// 加载数据
		dlg.loadConfigData();
		// 显示对话框
		dlg.showModal();
	}
}