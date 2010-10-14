package nc.ui.ic.ic601;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Vector;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.itf.uap.IVOPersistence;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.ui.ic.ic001.BatchCodeDefSetTool;
import nc.ui.ic.pub.bc.BarCodeViewDlg;
import nc.ui.ic.pub.bill.query.ICMultiCorpQryClient;
import nc.ui.ic.pub.bill.query.ICheckCondition;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.report.ReportBaseClass;
import nc.ui.scm.pub.FreeVOParse;
import nc.ui.scm.pub.billutil.ClientCacheHelper;
import nc.vo.ic.ic601.InvOnHandHeaderVO;
import nc.vo.ic.ic601.InvOnHandItemVO;
import nc.vo.ic.ic601.InvOnHandVO;
import nc.vo.ic.pub.ICConst;
import nc.vo.ic.pub.bc.BarCodeViewVO;
import nc.vo.ic.pub.lang.ResBase;
import nc.vo.ic.xcl.MdxclBVO;
import nc.vo.ic.xcl.MdxclVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.query.ConditionVO;
import nc.vo.scm.pub.BD_TYPE;
import nc.vo.scm.pub.SCMEnv;

public class ClientUI extends nc.ui.ic.pub.report.IcBaseReport implements
		nc.ui.ic.pub.thread.IPrint, ICheckCondition {
	private ReportBaseClass ivjReportBase;

	private ICMultiCorpQryClient ivjQueryConditionDlg;

	private SNDialog m_dlgSN;

	private String m_sRNodeName = "40083002SYS";

	// private String m_sQTempletID = "11113206400000313742";

	private String m_sPNodeCode = "40083002";

	// private String m_sUserID = "";

	private String m_sCorpID;

	private nc.vo.pub.lang.UFDate m_sLogDate;

	private Hashtable m_htShowFlag = new Hashtable();

	private nc.vo.pub.AggregatedValueObject m_voReport;

	private ButtonObject m_boQuery = new ButtonObject(
			nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
					"UC001-0000006")/* @res "查询" */,
			nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
					"UC001-0000006")/* @res "查询" */, 2, "查询"); /*-=notranslate=-*/

	private ButtonObject m_boShowSN = new ButtonObject(
			nc.ui.ml.NCLangRes.getInstance().getStrByID("40083002",
					"UPT40083002-000002")/* @res "显示序列号明细" */,
			nc.ui.ml.NCLangRes.getInstance().getStrByID("40083002",
					"UPT40083002-000002")/* @res "显示序列号明细" */, 2, "显示序列号明细"); /*-=notranslate=-*/

	private ButtonObject m_boShowBarCode = new ButtonObject(
			nc.ui.ml.NCLangRes.getInstance().getStrByID("40083002",
					"UPT40083002-000006")/* @res "显示条码明细" */,
			nc.ui.ml.NCLangRes.getInstance().getStrByID("40083002",
					"UPT40083002-000006")/* @res "显示条码明细" */, 2, "显示条码明细"); /*-=notranslate=-*/

	/**
	 * 何意求增加开始 2010-08-31
	 */
	private ButtonObject m_boShowDetails = new ButtonObject(nc.ui.ml.NCLangRes
			.getInstance().getStrByID("40083002", "显示码单明细"), nc.ui.ml.NCLangRes
			.getInstance().getStrByID("40083002", "显示码单明细"), 2, "显示码单明细"); /*-=notranslate=-*/

	private ButtonObject m_boMdcs = new ButtonObject(nc.ui.ml.NCLangRes
			.getInstance().getStrByID("40083002", "码单重算"), nc.ui.ml.NCLangRes
			.getInstance().getStrByID("40083002", "码单重算"), 2, "码单重算"); /*-=notranslate=-*/

	/**
	 * 何意求增加结束 2010-08-31
	 */

	/**
	 * 何意求 修改 开始 2010-08-31
	 */
	// 按钮组
	private ButtonObject[] m_MainButtonGroup = { m_boQuery, m_boShowSN,
			m_boShowBarCode, m_boShowDetails };

	/**
	 * 何意求 修改 结束 2010-08-31
	 */
	/**
	 * ClientUI 构造子注解。
	 */
	public ClientUI() {
		super();
		initialize();
	}

	/**
	 * 此处插入方法说明。 创建日期：(2001-7-12 12:58:52)
	 * 
	 * @return java.lang.String
	 * @param table
	 *            nc.ui.pub.beans.UITable
	 */
	public String checkICCondition(ConditionVO[] voCons) {

		boolean isflag = false;
		if (voCons != null) {
			for (int i = 0; i < voCons.length; i++) {
				if (voCons[i].getFieldCode().equals("vfree0flag")
						|| voCons[i].getFieldCode().equals("cscodeflag")
						|| voCons[i].getFieldCode().equals("vbatchcodeflag")
						|| voCons[i].getFieldCode().equals("measnameflag")) {
					if ("true".equals(voCons[i].getValue()))
						isflag = true;
				}

			}

		}
		try {

			getConditionDlg()
					.checkOncetime(
							voCons,
							new String[] { "vfree0flag", "cscodeflag",
									"vbatchcodeflag", "invclasscodeflag",
									"measnameflag", "invclasslev", "num",
									"assistnum" });
			java.util.ArrayList alKey = new java.util.ArrayList();
			getConditionDlg().checkAllHaveOrNot(voCons,
					new String[] { "invclasslev" });
			if (isflag) {
				String[] sStrs1 = new String[] { "invclasslev" };
				String[] sStrs2 = new String[] { "vfree0flag", "cscodeflag",
						"vbatchcodeflag", "measnameflag" };
				alKey.add(sStrs1);
				alKey.add(sStrs2);
				getConditionDlg().checkOneNotOther(voCons, alKey, false);
			}
			// 当按照分类汇总时，invclasslev字段必须填写
			getConditionDlg().checkOneTrueAnotherMustFillin(
					voCons,
					"classflag",
					"invclasslev",
					nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
							"UC000-0000566")/* @res "分类汇总级次" */);
		} catch (nc.vo.pub.BusinessException be) {
			return be.getMessage();
			// nc.ui.pub.beans.MessageDialog.showErrorDlg(this,"错误",be.getMessage());
		}

		return null;
	}

	/**
	 * 此处插入方法说明。 创建日期：(2001-7-31 11:44:35)
	 * 
	 * @param htFlag
	 *            java.util.Hashtable
	 * @param htShowFlag
	 *            java.util.Hashtable
	 */
	private void adjustColumns(Hashtable htFieldFlag) {

		// 货位
		if (m_bHasCscode) {
			setShowFlag("cscode", true);
			setShowFlag("csname", true);
		} else {
			setShowFlag("cscode", false);
			setShowFlag("csname", false);
		}

		// 按存货分类汇总
		if (m_bClassflag) {
			setShowFlag("invclasscode", true);
			setShowFlag("invclassname", true);
			setShowFlag("invcode", false);
			setShowFlag("invname", false);
			setShowFlag("invspec", false);
			setShowFlag("invtype", false);
			setShowFlag("mainmeasname", false);
			setShowFlag("castunitname", false);
			setShowFlag("hsl", false);
			setShowFlag("vbatchcode", false);
			setShowFlag("dvalidate", false);
			setShowFlag("vfree0", false);
			setShowFlag("assistnum", false);
		} else {
			// 存货
			setShowFlag("invclasscode", false);
			setShowFlag("invclassname", false);
			setShowFlag("invcode", true);
			setShowFlag("invname", true);
			setShowFlag("invspec", true);
			setShowFlag("invtype", true);
			setShowFlag("mainmeasname", true);

			if (m_bHasAssist) {
				setShowFlag("castunitname", true);
				setShowFlag("hsl", true);
				setShowFlag("assistnum", true);
			} else {
				setShowFlag("castunitname", true);
				setShowFlag("hsl", false);
				setShowFlag("assistnum", false);

			}

			if (m_bHasBatchcode) {
				setShowFlag("vbatchcode", true);
				setShowFlag("dvalidate", true);
			} else {
				setShowFlag("vbatchcode", false);
				setShowFlag("dvalidate", false);

			}

			if (m_bHasVfree0) {
				setShowFlag("vfree0", true);

			} else {
				setShowFlag("vfree0", false);
			}

			// zhy
			setShowFlag("custcode", m_bHasVendor);
			setShowFlag("custname", m_bHasVendor);
			setShowFlag("hsl", m_bHasHsl);

		}

	}

	/**
	 * 此处插入方法说明。 创建日期：(2001-8-13 19:20:19)
	 * 
	 * @return nc.vo.pub.query.ConditionVO[]
	 * @param voCons
	 *            nc.vo.pub.query.ConditionVO[]
	 */
	private nc.vo.pub.query.ConditionVO[] adjustCondition(
			nc.vo.pub.query.ConditionVO[] voCons) {
		return getConditionDlg().getExpandVOs(voCons);
	}

	/**
	 * 创建者：王乃军 功能：得到环境初始数据，如制单人等。 参数： 返回： 例外： 日期：(2001-5-9 9:23:32)
	 * 修改日期，修改人，修改原因，注释标志：
	 * 
	 * 
	 * 
	 * 
	 */
	protected void getCEnvInfo() {
		try {
			// 当前使用者ID
			nc.ui.pub.ClientEnvironment ce = nc.ui.pub.ClientEnvironment
					.getInstance();
			try {
				m_sUserID = ce.getUser().getPrimaryKey();
			} catch (Exception e) {

			}
			// 公司ID
			try {
				m_sCorpID = ce.getCorporation().getPrimaryKey();
			} catch (Exception e) {

			}
			// 日期
			try {
				if (ce.getDate() != null)
					m_sLogDate = ce.getDate();
			} catch (Exception e) {

			}
		} catch (Exception e) {

		}
	}

	/**
	 * 返回 QueryConditionClient1 特性值。
	 * 
	 * @return nc.ui.pub.query.QueryConditionClient
	 */
	private ICMultiCorpQryClient getConditionDlg() {
		if (ivjQueryConditionDlg == null) {
			ivjQueryConditionDlg = new ICMultiCorpQryClient(this, m_sUserID,
					m_sCorpID, "40083002");

			// 读查询模版数据
			ivjQueryConditionDlg.setTempletID(m_sCorpID, getPNodeCode(),
					m_sUserID, null);

			ivjQueryConditionDlg.setFreeItem("vfree0", "inv.invcode");
			// 自动清除
			ivjQueryConditionDlg.setAutoClear("kp.pk_corp", new String[] {
					"kp.ccalbodyid", "wh1.storcode", "cargdoc.cscode" });
			ivjQueryConditionDlg.setAutoClear("inv.invcode", new String[] {
					"vfree0", "vbatchcode", "meas2.pk_measdoc" });

			ivjQueryConditionDlg.setAstUnit("meas2.pk_measdoc", new String[] {
					"kp.pk_corp", "inv.invcode" });

			// 以下为对参照的初始化
			// 货位
			ivjQueryConditionDlg.setRefInitWhereClause("cargdoc.cscode",
					"货位档案", /*-=notranslate=-*/
					" bd_cargdoc.endflag='Y' and bd_cargdoc.pk_stordoc=",
					"wh1.storcode");

			// 将封存标志过滤去掉 可参照封存的仓库档案 陈倪娜 2009-07-10
			String s = " isdirectstore = 'N' and gubflag='N' and pk_calbody in (select pk_calbody from  bd_calbody  where (sealflag is null or sealflag ='N') and property in (0,1))";
			ivjQueryConditionDlg.setRefInitWhereClause("wh1.storcode", "仓库档案", /*-=notranslate=-*/
			s, null);

			ivjQueryConditionDlg
					.initRefWhere(
							"ccustomerid",
							" and (custflag ='0' or custflag ='2') and (bd_cumandoc.frozenflag='N' OR bd_cumandoc.frozenflag='n'  OR bd_cumandoc.frozenflag IS NULL) ");
			ivjQueryConditionDlg
					.initRefWhere(
							"cproviderid",
							" and (custflag ='1' or custflag ='3') and (bd_cumandoc.frozenflag='N' OR bd_cumandoc.frozenflag='n'  OR bd_cumandoc.frozenflag IS NULL) ");
			ivjQueryConditionDlg
					.initRefWhere("inv.invcode",
							" and bd_invbasdoc.discountflag='N' and bd_invbasdoc.laborflag='N'");

			ivjQueryConditionDlg.setPowerRefsOfCorp("kp.pk_corp", new String[] {
					"wh1.storcode", "kp.ccalbodyid", "inv.invcode",
					"invcl.invclasscode", "kp.cwarehouseid", "kp.cinventoryid",
					"cstoreadminid" }, null);

			ivjQueryConditionDlg.addICheckCondition(this);

		}
		return ivjQueryConditionDlg;
	}

	/**
	 * 此处插入方法说明。 创建日期：(2001-7-31 10:03:13)
	 * 
	 * @return java.util.ArrayList
	 * @param voCons
	 *            nc.vo.pub.query.ConditionVO[]
	 */
	public java.util.Hashtable getFieldFlag(nc.vo.pub.query.ConditionVO[] voCons) {
		Hashtable htRes = new Hashtable();

		if (voCons == null || voCons.length < 1)
			return htRes;

		for (int i = 0; i < voCons.length; i++) {
			String sItemkey = voCons[i].getFieldCode();

			if (!htRes.containsKey(sItemkey))
				htRes.put(sItemkey, voCons[i].getValue());
		}

		return htRes;
	}

	/**
	 * 此处插入方法说明。 创建日期：(2001-8-13 16:56:16)
	 * 
	 * @return nc.vo.ic.ic601.InvOnHandHeaderVO
	 * @param voCons
	 *            nc.vo.pub.query.ConditionVO[]
	 */
	public InvOnHandHeaderVO getHeader(nc.vo.pub.query.ConditionVO[] voCons) {

		InvOnHandHeaderVO voHead = new InvOnHandHeaderVO();
		// logdate
		voHead.setQuerydate(m_sLogDate);
		for (int i = 0; i < voCons.length; i++) {
			if (voCons[i].getFieldCode().equals("kp.ccalbodyid")) {
				nc.vo.pub.query.RefResultVO ref = voCons[i].getRefResult();
				if (ref != null) {
					voHead.setCcalbodyname(ref.getRefName());
				}
			}
			if (voCons[i].getFieldCode().equals("kp.pk_corp")) {
				nc.vo.pub.query.RefResultVO ref = voCons[i].getRefResult();
				if (ref != null) {
					voHead.setUnitname(ref.getRefName());
				}
			}
		}
		voHead.setQuerycondition(getConditionDlg().getChText());
		return voHead;
	}

	/**
	 * 此处插入方法说明。 创建日期：(2001-9-19 16:02:07)
	 * 
	 * @return nc.ui.ic.ic101.OrientDialog
	 * @author:余大英
	 */
	public SNDialog getSNDlg() {
		if (m_dlgSN == null) {
			m_dlgSN = new SNDialog(this);
		}
		return m_dlgSN;
	}

	/**
	 * 子类实现该方法，返回业务界面的标题。
	 * 
	 * @version (00-6-6 13:33:25)
	 * 
	 * @return java.lang.String
	 */
	public String getTitle() {
		if (getReportBaseClass().getReportTitle() != null)
			return getReportBaseClass().getReportTitle();
		else
			return nc.ui.ml.NCLangRes.getInstance().getStrByID("4008report",
					"UPP4008report-000002")/* @res "现存量查询" */;
	}

	/**
	 * 此处插入方法说明。 创建日期：(2001-7-24 20:03:46)
	 */
	private void initialize() {

		setName("ClientUI");
		setLayout(new java.awt.BorderLayout());
		setSize(774, 419);
		add(getReportBaseClass(), "Center");
		getCEnvInfo();
		// 设置按钮组
		setButtons(getButtonArray(m_MainButtonGroup));
		// 初始化模板
		initReportTemplet(m_sRNodeName);
		getReportBaseClass().setShowNO(true);
		getReportBaseClass().setTatolRowShow(true);
		getReportBaseClass().setMaxLenOfHeadItem("unitname", 100);
		getReportBaseClass().setMaxLenOfHeadItem("querycondition", 500);
		getReportBaseClass().getBillTable().setRowSelectionAllowed(true);
		getReportBaseClass().getBillTable().setColumnSelectionAllowed(false);
		getReportBaseClass().getBillTable().setSelectionMode(
				javax.swing.ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		getReportBaseClass().setBodyMenuShow(false);
		// 精度
		nc.ui.ic.pub.scale.ScaleInit si = new nc.ui.ic.pub.scale.ScaleInit(
				m_sUserID, m_sCorpID);
		ArrayList alParam = new ArrayList();
		alParam.add(new String[] { "num", "freezenum", "ngrossnum",
				"nfreezegrossnum" });
		alParam.add(new String[] { "assistnum" });
		alParam.add(null);
		alParam.add(null);
		alParam.add(new String[] { "hsl" });
		try {
			si.setScale(getReportBaseClass(), alParam);
			addSortListener();
		} catch (Exception e) {
			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"4008report", "UPP4008report-000003")/* @res "精度设置失败：" */
					+ e.getMessage());
		}
	}

	/**
	 * 此处插入方法说明。 创建日期：(2001-7-24 18:42:06)
	 * 
	 * @param sNodeName
	 *            java.lang.String
	 */
	private void initReportTemplet(String sNodeName) {
		// 读取模版数据
		try {
			getReportBaseClass().setTempletID(m_sCorpID, getPNodeCode(),
					m_sUserID, null);
		} catch (Exception e) {
			showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"SCMCOMMON", "UPPSCMCommon-000019")/*
														 * @res
														 * "不能得到模版，请与系统管理员联系！"
														 */);
			return;
		}
		nc.ui.pub.bill.BillData bd = getReportBaseClass().getBillData();
		bd = BatchCodeDefSetTool.changeBillDataByBCUserDef(m_sCorpID, bd);
		if (bd == null) {
			nc.vo.scm.pub.SCMEnv.out("--> billdata null.");
			return;
		}
		getReportBaseClass().setBillData(bd);

		// 获得表体、表头、表尾元素信息
		String[] strBodyFields = getReportBaseClass().getBodyFields();
		if (strBodyFields != null) {
			for (int i = 0; i < strBodyFields.length; i++) {
				m_htShowFlag.put(strBodyFields[i], new Boolean(true));

			}
		}
		return;

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
		showHintMessage("");
		if (bo == m_boQuery)
			onQuery(true);
		else if (bo == m_boShowSN)
			onShowSN();
		else if (bo == m_boShowBarCode)
			onShowBarCode();
		/**
		 * 何意求增加开始 2010-08-31
		 */
		else if (bo == m_boShowDetails)
			onShowDetails();
		else if (bo == m_boMdcs)
			onBoMdcs();
		/**
		 * 何意求增加结束 2010-08-31
		 */
		else
			super.onButtonClicked(bo);

	}

	/**
	 * 何意求增加开始 2010-08-31
	 */
	private void onShowDetails() {
		int iCurRow = getReportBaseClass().getBillTable().getSelectedRow();
		String[] keys = new String[] { "ccalbodyid", "cwarehouseid",
				"cinventoryid", "pk_invbasdoc" };
		if (iCurRow >= 0
				&& iCurRow < getReportBaseClass().getBillModel().getRowCount()) {
			InvOnHandItemVO voItem = new InvOnHandItemVO();
			nc.ui.pub.bill.BillModel bm = getReportBaseClass().getBillModel();

			for (int j = 0; j < keys.length; j++) {
				try {
					voItem.setAttributeValue(keys[j], bm.getValueAt(iCurRow,
							keys[j]));
				} catch (Exception e) {
				}
			}
			// 库存组织
			String ccalbodyid = voItem.getAttributeValue("ccalbodyid")
					.toString();
			// 仓库PK
			String cwarehouseid = voItem.getAttributeValue("cwarehouseid")
					.toString();
			// 存货管理档案
			String cinventoryid = voItem.getAttributeValue("cinventoryid")
					.toString();
			// 存货基本档案
			String pk_invbasdoc = voItem.getAttributeValue("pk_invbasdoc")
					.toString();
			MdDetailDialog dlg = new MdDetailDialog(getClientEnvironment(),
					ccalbodyid, cwarehouseid, cinventoryid, pk_invbasdoc);
			dlg.showModal();
			return;
		} else {
			showWarningMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"SCMCOMMON", "UPPSCMCommon-000356"));
			// showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
			// "SCMCOMMON", "UPPSCMCommon-000356")/* @res "请选择表体行！" */);
			return;
		}
	}

	// 码单重算
	private void onBoMdcs() {
		IUAPQueryBS iUAPQueryBS = (IUAPQueryBS) NCLocator.getInstance().lookup(
				IUAPQueryBS.class.getName());
		int rs = showYesNoMessage("您确定要对出入库码单进行重算吗？请慎重当前重算，此操作不可逆！");
		if (rs == 8||rs==2)
			return;
		String pk_corp = getCorpID();
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

	/**
	 * 何意求增加结束 2010-08-31
	 */

	/**
	 * 此处插入方法说明。 创建日期：(2001-7-24 18:45:22)
	 */
	public void onQuery(boolean bQuery) {
		if (bQuery || !m_bEverQry) {
			getConditionDlg().hideNormal();
			getConditionDlg().showModal();
			m_bEverQry = true;
		} else {
			getConditionDlg().onButtonConfig();
		}

		if (!getConditionDlg().isCloseOK())
			return;
		String[] corps = getConditionDlg().getSelectedCorpIDs();
		if (corps == null || corps.length < 0) {
			showWarningMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"4008report", "UPP4008report-000186")/*
															 * @res
															 * "没有选择公司查询条件!"
															 */);
			onQuery(true);
			return;
		}

		setDlgSubTotal(null);
		try {
			nc.vo.ic.pub.bill.QryConditionVO voQry = new nc.vo.ic.pub.bill.QryConditionVO();
			voQry.setParam(0, corps);

			nc.vo.pub.query.ConditionVO[] voCons = getConditionDlg()
					.getConditionVO();

			voQry.setParam(1, adjustCondition(voCons));

			voQry.setParam(13, getDataPowerWhereSql());// 加入权限SQL

			if (!checkConds(voCons))
				return;

			long lTime = System.currentTimeMillis();
			long lTimes = System.currentTimeMillis();

			InvOnHandVO vo = IC601InvOnHandHelper.queryXcl(voQry);
			if (vo == null) {
				getReportBaseClass().getBillModel().clearBodyData();
				return;
			}

			showTime(lTimes, "查询时间："); /*-=notranslate=-*/
			// 获得查询字段展开属性
			getField(voCons);
			// 调整显示列
			adjustColumns(getFieldFlag(voCons));
			// 合计行
			// 显示报表
			InvOnHandHeaderVO voHead = getHeader(voCons);
			getReportBaseClass().setHeadDataVO(voHead);
			vo.setParentVO(voHead);
			if (vo.getChildrenVO() != null && vo.getChildrenVO().length > 0) {
				// getReportBaseClass().setBodyDataVO(vo.getChildrenVO());
				// 公式处理
				setReportData(vo.getChildrenVO(), voCons);

			} else
				getReportBaseClass().getBillModel().clearBodyData();

			// getReportBaseClass().getBillModel().execLoadFormula();
			// 初始化定位VO
			m_voReport = vo;
			calculateTotal();
			showTime(lTime, "查询总时间" + vo.getChildrenVO().length + "条数"); /*-=notranslate=-*/
		} catch (Exception e) {
			nc.vo.scm.pub.SCMEnv.error(e);
			showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"4008report", "UPP4008report-000004")/* @res "查询出错：" */
					+ e.getMessage());
		}
		return;
	}

	/**
	 * 此处插入方法说明。 创建日期：(2001-9-19 10:17:38)
	 */
	public void onShowSN() {

		int iCurRow = getReportBaseClass().getBillTable().getSelectedRow();
		String[] scodes = null;
		StringBuffer sInv = new StringBuffer();
		String sNum = "0";
		String[] keys = new String[] { "pk_corp", "cwarehouseid",
				"cinventoryid", "cspaceid", "castunitid", "hsl", "vbatchcode",
				"vfree1", "vfree2", "vfree3", "vfree4", "vfree5", "vfree6",
				"vfree7", "vfree8", "vfree9", "vfree10", "cvendorid" };
		if (iCurRow >= 0
				&& iCurRow < getReportBaseClass().getBillModel().getRowCount()) {
			InvOnHandItemVO voItem = new InvOnHandItemVO();
			nc.ui.pub.bill.BillModel bm = getReportBaseClass().getBillModel();

			for (int j = 0; j < keys.length; j++) {
				try {
					voItem.setAttributeValue(keys[j], bm.getValueAt(iCurRow,
							keys[j]));
				} catch (Exception e) {
				}

			}

			if (voItem != null) {
				try {
					scodes = IC601InvOnHandHelper.querySN(voItem);
				} catch (Exception e) {
					showErrorMessage(nc.ui.ml.NCLangRes.getInstance()
							.getStrByID("4008report", "UPP4008report-000005")/*
																				 * @res
																				 * "查询序列号错误！请检查数据库连接！"
																				 */);

				}
			}
			if (bm.getValueAt(iCurRow, "invcode") != null)
				sInv.append((String) bm.getValueAt(iCurRow, "invcode"));
			sInv.append(" ");
			if (bm.getValueAt(iCurRow, "invname") != null)
				sInv.append((String) bm.getValueAt(iCurRow, "invname"));

			if (scodes != null && scodes.length > 0)
				sNum = new Integer(scodes.length).toString();

		} else {
			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"SCMCOMMON", "UPPSCMCommon-000356")/* @res "请选择表体行！" */);
			return;
		}

		if (scodes == null || scodes.length < 1) {
			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"4008report", "UPP4008report-000006")/* @res "该存货没有序列号！" */);
			return;
		}

		Vector vdata = new Vector(scodes.length);

		for (int i = 0; i < scodes.length; i++) {
			Vector v = new Vector(2);
			v.addElement(Integer.toString(i + 1));
			v.addElement(scodes[i]);
			vdata.addElement(v);
		}
		m_dlgSN = null;
		getSNDlg().setData(sInv.toString(), sNum, vdata);
		getSNDlg().showModal();
	}

	public void onShowBarCode() {

		int iCurRow = getReportBaseClass().getBillTable().getSelectedRow();
		BarCodeViewVO[] scodes = null;
		StringBuffer sInv = new StringBuffer();
		String sNum = "0";
		String[] keys = new String[] { "pk_corp", "cwarehouseid",
				"cinventoryid", "cspaceid", "castunitid", "hsl", "vbatchcode",
				"vfree1", "vfree2", "vfree3", "vfree4", "vfree5", "vfree6",
				"vfree7", "vfree8", "vfree9", "vfree10", "cvendorid" };
		if (iCurRow >= 0
				&& iCurRow < getReportBaseClass().getBillModel().getRowCount()) {
			InvOnHandItemVO voItem = new InvOnHandItemVO();
			nc.ui.pub.bill.BillModel bm = getReportBaseClass().getBillModel();

			for (int j = 0; j < keys.length; j++) {
				try {
					voItem.setAttributeValue(keys[j], bm.getValueAt(iCurRow,
							keys[j]));
				} catch (Exception e) {
				}

			}

			if (voItem != null) {
				try {
					scodes = IC601InvOnHandHelper.queryBarCode(voItem);
				} catch (Exception e) {
					showErrorMessage(nc.ui.ml.NCLangRes.getInstance()
							.getStrByID("4008report", "UPP4008report-000220")/*
																				 * @res
																				 * "查询结存条码错误！请检查数据库连接！"
																				 */);

				}
			}

		} else {
			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"SCMCOMMON", "UPPSCMCommon-000356")/* @res "请选择表体行！" */);
			return;
		}

		if (scodes == null || scodes.length < 1) {
			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"4008report", "UPP4008report-000221")/* @res "该存货没有结存条码！" */);
			return;
		}
		getOnhandBCViewDlg().setDataVO(scodes);
		getOnhandBCViewDlg().showModal();

	}

	protected BarCodeViewDlg m_onhandBCViewDlg = null;// 结存条码明细

	private BarCodeViewDlg getOnhandBCViewDlg() {
		if (m_onhandBCViewDlg == null) {
			m_onhandBCViewDlg = new BarCodeViewDlg(this,
					nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec",
							"UPP4008spec-000569")/* @res "结存条码明细" */,
					ICConst.BarCodeViewDlgType.ViewOnhandBC);
		}
		return m_onhandBCViewDlg;
	}

	/**
	 * 此处插入方法说明。 创建日期：(2001-7-31 14:30:15)
	 * 
	 * @param strKey
	 *            java.lang.String
	 */
	public void setShowFlag(String strKey, boolean flag) {

		if (flag) {
			if (m_htShowFlag.containsKey(strKey)) {
				if (!((Boolean) m_htShowFlag.get(strKey)).booleanValue()) {

					getReportBaseClass().showBodyTableCol(strKey);
					// m_htShowFlag.remove(strKey);

					m_htShowFlag.put(strKey, new Boolean(true));
				}
			} else {
				// showErrorMessage("报表中的列"+strKey+"为隐藏列，不能正确显示结果！请重新设置报表模版！");
			}

		} else {
			if (m_htShowFlag.containsKey(strKey)) {
				if (((Boolean) m_htShowFlag.get(strKey)).booleanValue()) {

					getReportBaseClass().hideBodyTableCol(strKey);
					// m_htShowFlag.remove(strKey);
					m_htShowFlag.put(strKey, new Boolean(false));
				}
			}

		}
		return;
	}

	/**
	 * ClientUI 构造子注解。
	 */
	public ClientUI(nc.ui.pub.FramePanel ff) {
		super();
		setFrame(ff);
		initialize();
	}

	/**
	 * 此处插入方法说明。 功能： 参数： 返回： 例外： 日期：(2002-11-5 10:38:55) 修改日期，修改人，修改原因，注释标志：
	 * 
	 * @return java.lang.String
	 */
	public String getDefaultPNodeCode() {
		return m_sPNodeCode;
	}

	/**
	 * 返回 ReportBaseClass 特性值。
	 * 
	 * @return nc.ui.pub.report.ReportBaseClass
	 */
	/* 警告：此方法将重新生成。 */
	public ReportBaseClass getReportBaseClass() {
		if (ivjReportBase == null) {
			ivjReportBase = new nc.ui.pub.report.ReportBaseClass();
			ivjReportBase.setName("ReportBase");
		}
		return ivjReportBase;
	}

	/**
	 * 创建者：王乃军 功能：显示消耗的时间 参数： 返回： 例外： 日期：(2001-11-23 18:11:18)
	 * 修改日期，修改人，修改原因，注释标志：
	 */
	protected void showTime(long lStartTime, String sTaskHint) {
		long lTime = System.currentTimeMillis() - lStartTime;
		nc.vo.scm.pub.SCMEnv.out("执行<" /*-=notranslate=-*/
				+ sTaskHint + ">消耗的时间为：" /*-=notranslate=-*/
				+ (lTime / 60000) + "分" /*-=notranslate=-*/
				+ ((lTime / 1000) % 60) + "秒" /*-=notranslate=-*/
				+ (lTime % 1000) + "毫秒"); /*-=notranslate=-*/

	}

	boolean m_bClassflag = false; // 按分类汇总

	boolean m_bHasAssist = false;

	boolean m_bHasBatchcode = false;

	boolean m_bHasCscode = false;

	boolean m_bHasVfree0 = false;

	boolean m_bHasVendor = false;

	boolean m_bHasHsl = false;

	// 判断是否按对应项目展开 by hanwei 2003-06-06
	Hashtable m_htField = null;

	/**
	 * 此处插入方法说明。 创建日期：(2003-9-24 14:20:02)
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getCorpID() {
		return m_sCorpID;
	}

	/**
	 * 此处插入方法说明。 创建日期：(2001-7-31 10:03:13)
	 * 
	 * @return java.util.Hashtable
	 * @param voCons
	 *            nc.vo.pub.query.ConditionVO[] 建立查询条件的索引
	 */
	private java.util.Hashtable getField(nc.vo.pub.query.ConditionVO[] voCons) {
		m_htField = new Hashtable();
		if (voCons != null && voCons.length > 0) {

			for (int i = 0; i < voCons.length; i++) {
				String sItemkey = voCons[i].getFieldCode();

				if (!m_htField.containsKey(sItemkey))
					m_htField.put(sItemkey, voCons[i].clone());
			}
		}
		if (m_htField.containsKey("classflag")
				&& ((ConditionVO) m_htField.get("classflag")).getValue()
						.equalsIgnoreCase("Y"))
			m_bClassflag = true;
		else
			m_bClassflag = false;

		if (m_htField.containsKey("meas2.assistnum")
				|| m_htField.containsKey("bd_measdoc2.measname")
				|| (m_htField.containsKey("measnameflag") && ((ConditionVO) m_htField
						.get("measnameflag")).getValue().equalsIgnoreCase("Y")))
			m_bHasAssist = true;
		else
			m_bHasAssist = false;

		if (m_htField.containsKey("cargdoc.cscode")
				|| m_htField.containsKey("cscodeflag")
				&& ((ConditionVO) m_htField.get("cscodeflag")).getValue()
						.equalsIgnoreCase("Y")) {
			nc.vo.scm.pub.SCMEnv.out("m_bHasCscode = true;");

			m_bHasCscode = true;
		} else {
			m_bHasCscode = false;
			nc.vo.scm.pub.SCMEnv.out("m_bHasCscode = false;");
		}

		if (m_htField.containsKey("vbatchcode")
				|| m_htField.containsKey("dvalidate")
				|| (m_htField.containsKey("vbatchcodeflag") && ((ConditionVO) m_htField
						.get("vbatchcodeflag")).getValue()
						.equalsIgnoreCase("Y")))
			m_bHasBatchcode = true;
		else
			m_bHasBatchcode = false;

		if (m_htField.containsKey("vfree1")
				|| m_htField.containsKey("vfree2")
				|| m_htField.containsKey("vfree3")
				|| m_htField.containsKey("vfree4")
				|| m_htField.containsKey("vfree5")
				|| m_htField.containsKey("vfree6")
				|| m_htField.containsKey("vfree7")
				|| m_htField.containsKey("vfree8")
				|| m_htField.containsKey("vfree9")
				|| m_htField.containsKey("vfree10")
				|| (m_htField.containsKey("vfree0flag") && ((ConditionVO) m_htField
						.get("vfree0flag")).getValue().equalsIgnoreCase("Y")))
			m_bHasVfree0 = true;
		else
			m_bHasVfree0 = false;

		if (m_htField.containsKey("vendor.custcode")
				|| (m_htField.containsKey("cvendorflag") && ((ConditionVO) m_htField
						.get("cvendorflag")).getValue().equalsIgnoreCase("Y"))) {
			m_bHasVendor = true;
		} else
			m_bHasVendor = false;

		if (m_htField.containsKey("hslflag")
				&& ((ConditionVO) m_htField.get("hslflag")).getValue()
						.equalsIgnoreCase("Y")) {
			m_bHasHsl = true;
		} else
			m_bHasHsl = false;

		return m_htField;
	}

	/**
	 * 此处插入方法说明。 创建日期：(2003-9-24 14:20:02)
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getUserID() {
		return m_sUserID;
	}

	/**
	 * 此处插入方法说明。 功能描述: 输入参数: 返回值: 异常处理: 日期:
	 * 
	 * @param reportData
	 *            CircularlyAccessibleValueObject[]
	 * @param sQryLevel
	 *            java.lang.String
	 */
	private void setReportData(
			nc.vo.pub.CircularlyAccessibleValueObject[] reportData,
			nc.vo.pub.query.ConditionVO[] voCons) {

		// 通用的公式：仓库，部门
		// 根据查询条件判断是否需要生成有关的
		// 存在仓库
		// 设置数据到报表上
		long lTime = System.currentTimeMillis();
		// 公司
		ClientCacheHelper.getColValue(reportData, new String[] { "unitcode",
				"unitname" }, "bd_corp", "pk_corp", new String[] { "unitcode",
				"unitname" }, "pk_corp");
		//
		// //库存组织
		ClientCacheHelper.getColValue(reportData, new String[] { "bodycode",
				"bodyname" }, "bd_calbody", "pk_calbody", new String[] {
				"bodycode", "bodyname" }, "ccalbodyid");
		//
		nc.ui.scm.pub.billutil.ClientCacheHelper.getColValue(reportData,
				new String[] { "storcode", "storname" }, "bd_stordoc",
				"pk_stordoc", new String[] { "storcode", "storname" },
				"cwarehouseid");
		// 按货位展开
		if (m_bHasCscode) {
			nc.ui.scm.pub.billutil.ClientCacheHelper.getColValue(reportData,
					new String[] { "cscode", "csname" }, "bd_cargdoc",
					"pk_cargdoc", new String[] { "cscode", "csname" },
					"cspaceid");
		}

		// nc.ui.scm.pub.billutil.ClientCacheHelper
		// .getColValue(reportData, new String[] { "pk_invbasdoc" },
		// "bd_invmandoc", "pk_invmandoc",
		// new String[] { "pk_invbasdoc" }, "cinventoryid");
		//
		// nc.ui.scm.pub.billutil.ClientCacheHelper.getColValue(reportData,
		// new String[] { "invname", "invcode", "invspec", "invtype",
		// "mainmeasname" }, "bd_invbasdoc", "pk_invbasdoc",
		// new String[] { "invname", "invcode", "invspec", "invtype",
		// "pk_measdoc" }, "pk_invbasdoc");
		//
		// nc.ui.scm.pub.billutil.ClientCacheHelper.getColValue(reportData,
		// new String[] { "mainmeasname" }, "bd_measdoc", "pk_measdoc",
		// new String[] { "measname" }, "mainmeasname");

		// 按辅计量展开
		if (m_bHasAssist || m_bHasHsl) { // 辅助计量单位
			nc.ui.scm.pub.billutil.ClientCacheHelper.getColValue(reportData,
					new String[] { "castunitname" }, "bd_measdoc",
					"pk_measdoc", new String[] { "measname" }, "castunitid");
		}

		// 供应商
		// nc.ui.scm.pub.billutil.ClientCacheHelper.getColValue(reportData,
		// new String[] { "pk_cubasdoc"}, "bd_cumandoc",
		// "pk_cumandoc", new String[] { "pk_cubasdoc" },
		// "cvendorid");
		// nc.ui.scm.pub.billutil.ClientCacheHelper.getColValue(reportData,
		// new String[] { "custcode", "custname" }, "bd_cubasdoc",
		// "pk_cubasdoc", new String[] { "custcode", "custname" },
		// "pk_cubasdoc");
		//		
		// ClientCacheHelper.getColValue(reportData,new
		// String[]{"pk_invbasdoc"},"bd_invmandoc","pk_invmandoc",new
		// String[]{"pk_invbasdoc"},"cinventoryid");

		// 批次号档案相关字段
		// 修改人：刘家清 修改时间：2009-11-18 上午09:57:35 修改原因：已经放到后台执行批次信息补齐
		// BatchCodeDefSetTool.execFormulaBatchCodeForReport(reportData);
		// ClientCacheHelper.getColValue2(reportData,
		// new
		// String[]{"dvalidate","dproducedate","cqualitylevelid","vvendbatchcode","tchecktime"},
		// "scm_batchcode",
		// "pk_invbasdoc",
		// "vbatchcode",
		// new
		// String[]{"dvalidate","dproducedate","cqualitylevelid","vvendbatchcode","tchecktime"},
		// "pk_invbasdoc",
		// "vbatchcode");

		// setBodyDatabyFormulaItem(reportData, arylistItemField, false);
		SCMEnv.showTime(lTime, "普通基本档案公式解析New:"); /*-=notranslate=-*/
		// zhy不再重算换算率
		// if (m_bHasAssist) {
		// //赋予换算率通过calAllConvRate方法反算回来
		// lTime = System.currentTimeMillis();
		// nc.vo.ic.pub.GenMethod method = new nc.vo.ic.pub.GenMethod();
		// method.calAllConvRate(reportData, "fixedflag", "hsl", "nterminnum",
		// "nterminastnum", "num", "assistnum");
		// SCMEnv.showTime(lTime, "hsl计算"); /*-=notranslate=-*/
		// }
		// 自由项
		if (m_bHasVfree0) {
			lTime = System.currentTimeMillis();
			// nc.ui.scm.pub.FreeVOParse freeVOParse = new
			// nc.ui.scm.pub.FreeVOParse();
			// freeVOParse.setFreeVO(reportData, "pk_invbasdoc", null, true);
			FreeVOParse freeVOParse = new FreeVOParse();
			freeVOParse.setFreeVOFromBs(reportData, "cinventoryid", "vfree0",
					"vfree0");
			SCMEnv.showTime(lTime, "自由项解析"); /*-=notranslate=-*/
		}
		lTime = System.currentTimeMillis();
		getReportBaseClass().setBodyDataVO(reportData, true);
		// getReportBaseClass().getBillModel().execLoadFormula();
		SCMEnv.showTime(lTime, "前台数据加载界面"); /*-=notranslate=-*/

	}

	private boolean checkConds(ConditionVO[] cons) {
		String sField = null;
		boolean bcstoreadminid = false;
		boolean binvclass = false;
		if (null != m_boShowBarCode)
			m_boShowBarCode.setEnabled(true);
		for (int i = 0; i < cons.length; i++) {
			sField = cons[i].getFieldCode();
			if (sField.equals("cstoreadminid")) {
				bcstoreadminid = true;
			} else if (sField.equals("classflag")
					&& cons[i].getValue().equalsIgnoreCase("Y")) {
				binvclass = true;
			} else if (sField.equals("cscodeflag")
					&& cons[i].getValue().equalsIgnoreCase("Y")) {
				if (null != m_boShowBarCode)
					m_boShowBarCode.setEnabled(false);
			}
		}
		if (null != m_boShowBarCode)
			updateButtons();
		if (bcstoreadminid && binvclass) {
			showErrorMessage(ResBase.get601AdminAndClass()/* "库管员和按分类汇总不能同时选择!" */);
			return false;
		}
		return true;
	}

	protected String getDataPowerWhereSql() {
		// 因现存量查询仍沿用老查询模板，故新权限处理方式只处理货位，其它仍沿用老处理方式
		String[] dataPowerFields = { "kp.cspaceid" };
		BD_TYPE[] fieldsBDType = new BD_TYPE[] { BD_TYPE.CargoDoc };
		return super.getDataPowerWhereSql(new String[] { m_sCorpID },
				dataPowerFields, fieldsBDType);
	}

}