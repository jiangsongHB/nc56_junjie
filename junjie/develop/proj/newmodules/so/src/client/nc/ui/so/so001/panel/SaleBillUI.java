package nc.ui.so.so001.panel;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.JComponent;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.pf.IPFMetaModel;
import nc.itf.uap.rbac.IUserManageQuery;
import nc.ui.bd.b21.CurrParamQuery;
import nc.ui.ml.NCLangRes;
import nc.ui.pf.pub.PfUIDataCache;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UIMenuItem;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.beans.UITable;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillModel;
import nc.ui.pub.bill.BillStatus;
import nc.ui.pub.change.PfChangeBO_Client;
import nc.ui.pub.linkoperate.ILinkAdd;
import nc.ui.pub.linkoperate.ILinkAddData;
import nc.ui.pub.linkoperate.ILinkApprove;
import nc.ui.pub.linkoperate.ILinkApproveData;
import nc.ui.pub.linkoperate.ILinkMaintain;
import nc.ui.pub.linkoperate.ILinkMaintainData;
import nc.ui.pub.linkoperate.ILinkQuery;
import nc.ui.pub.linkoperate.ILinkQueryData;
import nc.ui.pub.msg.PfLinkData;
import nc.ui.pub.pf.PfUtilClient;
import nc.ui.pub.print.PrintEntry;
import nc.ui.pub.query.QueryConditionClient;
import nc.ui.scm.file.DocumentManager;
import nc.ui.scm.message.Message;
import nc.ui.scm.plugin.InvokeEventProxy;
import nc.ui.scm.print.BillPrintTool;
import nc.ui.scm.print.IFreshTsListener;
import nc.ui.scm.print.PrintLogClient;
import nc.ui.scm.print.SalePubPrintDS;
import nc.ui.scm.print.SplitParams;
import nc.ui.scm.pub.CacheTool;
import nc.ui.scm.pub.InvoInfoBYFormula;
import nc.ui.scm.pub.ScmPubHelper;
import nc.ui.scm.pub.def.DefSetTool;
import nc.ui.scm.pub.print.DefaultFormulaJudge;
import nc.ui.scm.pub.query.ConvertQueryCondition;
import nc.ui.scm.pub.redunmulti.IUseSupplyTrans;
import nc.ui.scm.pub.report.BillRowNo;
import nc.ui.scm.pub.report.LocateDialog;
import nc.ui.scm.ref.prm.CustAddrRefModel;
import nc.ui.scm.so.RedunVOTool;
import nc.ui.scm.so.SaleBillType;
import nc.ui.scm.sourcebill.SourceBillFlowDlg;
import nc.ui.so.pub.DataPowerUtils;
import nc.ui.so.pub.IBatchWorker;
import nc.ui.so.pub.InvAttrCellRenderer;
import nc.ui.so.pub.ProccDlg;
import nc.ui.so.pub.SoTaskManager;
import nc.ui.so.pub.plugin.SOPluginUI;
import nc.ui.so.so001.SaleOrderBO_Client;
import nc.ui.so.so001.panel.bom.BillTools;
import nc.ui.so.so001.panel.bom.BomorderBO_Client;
import nc.ui.so.so001.panel.card.SOBillCardTools;
import nc.ui.so.so001.panel.list.SaleBillListUI;
import nc.ui.so.so003.BillToBillUI;
import nc.ui.so.so016.OrderBalanceCardUI;
import nc.ui.uap.sf.SFClientUtil;
import nc.vo.bd.b06.PsndocVO;
import nc.vo.ep.dj.DJZBVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.VOStatus;
import nc.vo.pub.ValidationException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.pfflow04.MessagedriveVO;
import nc.vo.pub.query.ConditionVO;
import nc.vo.scm.bd.SmartVODataUtils;
import nc.vo.scm.ic.bill.InvVO;
import nc.vo.scm.ic.exp.ATPNotEnoughException;
import nc.vo.scm.plugin.Action;
import nc.vo.scm.pub.SCMEnv;
import nc.vo.scm.pub.redun.ISourceRedunVO;
import nc.vo.scm.pub.session.ClientLink;
import nc.vo.scm.pub.smart.ObjectUtils;
import nc.vo.scm.relacal.SCMRelationsCal;
import nc.vo.so.pub.SOGenMethod;
import nc.vo.so.so001.AtpCheckException;
import nc.vo.so.so001.AtpSetException;
import nc.vo.so.so001.BomorderHeaderVO;
import nc.vo.so.so001.BomorderItemVO;
import nc.vo.so.so001.BomorderVO;
import nc.vo.so.so001.ISaleOrderAction;
import nc.vo.so.so001.SOToolVO;
import nc.vo.so.so001.SaleOrderVO;
import nc.vo.so.so001.SaleorderBVO;
import nc.vo.so.so001.SaleorderHVO;
import nc.vo.so.so001.SoVoConst;
import nc.vo.so.so016.SaleReceiveBVO;
import nc.vo.so.so016.SaleReceiveHVO;
import nc.vo.so.so016.SaleReceiveVO;
import nc.vo.so.so016.SoVoTools;
import nc.vo.so.so120.CreditNotEnoughException;
import nc.vo.so.so120.PeriodNotEnoughException;
import nc.vo.so.sodispart.SaleDispartVO;
import nc.vo.uap.pf.PFBusinessException;

/**
 * 销售单据基类。
 * 
 * 创建日期：(2001-4-13 15:26:05)
 * 
 * @author：宋杰
 * 
 * 修改时间：2002-10-07
 * 
 * 2003-04-21 顾焱增加联查
 * 
 * 修改日期：2003-09-27 修改人：杨涛 修改内容：含税单价和无税单位；含税净价和无税净价算法修改
 * 
 * 修改日期：2003-10-15 修改人：杨涛 修改内容：算法改进
 * 
 * 修改日期：2003-10-16 修改人：杨涛 修改内容：增加公司级参数SO21（预收优先）
 * 
 * 修改日期：2003-10-27 修改人：杨涛 修改内容：公司级参数SO21由逻辑型改为字符型，取值为“比例”和“金额”
 * 
 * @modified V5.01 zhongwei 修订时放开表体的项目、收货单位、收货地址、收货地点属性
 * 
 * 
 * 
 * @refactored V5.1 销售订单UI端抽象基类，描述订单的业务逻辑和部分公共操作(处理按钮动作) zhongwei
 * 
 * 修改日期：2008-06-25 修改人：周长胜 修改内容：v5.5销售订单修订表头增加修订版本号和修订日期、修订人的存储
 */
public abstract class SaleBillUI extends SaleBillListUI implements IFreshTsListener, ILinkAdd,
		ILinkMaintain, ILinkApprove, ILinkQuery, IUseSupplyTrans, IBatchWorker {

	protected ButtonObject boClose = new ButtonObject(NCLangRes.getInstance().getStrByID(
			"SCMCOMMON", "UPPSCMCommon-000119")/* @res "关闭" */, NCLangRes.getInstance()
			.getStrByID("SCMCOMMON", "UPPSCMCommon-000120")/* @res "关闭单据" */, 0, "关闭"); /*-=notranslate=-*/

	protected ButtonObject boOpen = new ButtonObject(NCLangRes.getInstance().getStrByID(
			"SCMCOMMON", "UPPSCMCommon-000060")/* @res "打开" */, NCLangRes.getInstance()
			.getStrByID("40060301", "UPP40060301-000157")/* @res "打开单据" */, 0, "打开"); /*-=notranslate=-*/

	protected ButtonObject boATP = new ButtonObject("ATP", "ATP", 0);

	protected ButtonObject boAfterAction;

	protected ButtonObject boConsignment = new ButtonObject(NCLangRes.getInstance().getStrByID(
			"40060301", "UPP40060301-000161")/* @res "发货" */, NCLangRes.getInstance().getStrByID(
			"40060301", "UPP40060301-000161")/*
												 * @res "发货"
												 */, 0, "发货");

	protected ButtonObject boOutStore = new ButtonObject(NCLangRes.getInstance().getStrByID(
			"40060301", "UPP40060301-000162")/* @res "出库" */, NCLangRes.getInstance().getStrByID(
			"40060301", "UPP40060301-000162")/*
												 * @res "出库"
												 */, 0, "出库");

	protected ButtonObject boMakeInvoice = new ButtonObject(NCLangRes.getInstance().getStrByID(
			"40060301", "UPP40060301-000163")/* @res "开票" */, NCLangRes.getInstance().getStrByID(
			"40060301", "UPP40060301-000163")/*
												 * @res "开票"
												 */, 0, "开票");

	protected ButtonObject boGathering = new ButtonObject(NCLangRes.getInstance().getStrByID(
			"40060301", "UPP40060301-000164")/* @res "收款" */, NCLangRes.getInstance().getStrByID(
			"40060301", "UPP40060301-000164")/*
												 * @res "收款"
												 */, 0, "收款");

	protected ButtonObject boRequestPurchase = new ButtonObject(NCLangRes.getInstance().getStrByID(
			"40060301", "UPP40060301-000165")/* @res "请购" */, NCLangRes.getInstance().getStrByID(
			"40060301", "UPP40060301-000165")/*
												 * @res "请购"
												 */, 0, "请购");

	// 配置销售按钮组
	protected ButtonObject[] bomButtonGroup;

	// 联查对话框
	SourceBillFlowDlg soureDlg = null;

	private boolean isSaveStart = true;

	protected boolean binitOnNewByOther = false;

	private String strUISource;

	protected boolean bInMsgPanel = false;

	protected String headwarehouseRefWhereSql = null;

	// 补货直运UI
	protected BillToBillUI billToBillUI = null;

	protected OrderBalanceCardUI orderBalanceUI = null;

	protected SoTaskManager soTaskManager = null;

	protected PrintLogClient printLogClient = null;

	protected Vector vRowATPStatus_bak = null;

	protected ArrayList alInvs_bak = null;

	protected HashMap hsMapSendAudit = new HashMap();

	// 列表多张单据打印byzxj
	// 初始化打印接口
	protected SaleOrderPrintDataInterface m_dataSource;

	protected SalePubPrintDS m_spdatasource;

	protected PrintEntry m_print;

	// 分单打印对话框
	private SaleOrderSplitDLG spDLG = null;

	// 是否已经进行过查询操作（判定刷新按钮状态使用）
	private boolean b_query;

	// 是否为退货界面
	private boolean b_refundment = false;

	// 是否为修订(暂用于控制‘修订理由’的显示)    liuzwc 2009-06-23 
	protected boolean b_isRevise = false;
	
	ProccDlg proccdlg;

	// 保存时的错误信息行
	private ArrayList<Integer> rowList = new ArrayList<Integer>();

	protected static final String SELFBILL = "makeflag";

	protected String coBusiType = null;

	protected String coPoOrderId = null;

	protected String coPoTs = null;

	// 界面缓存初始化VO:新增参照
	protected AggregatedValueObject initAvo = null;

	// 二次开发插件
	private InvokeEventProxy pluginproxy;

	// 分单缓存
	private TempBillVoCache tempBillVoCache = new TempBillVoCache();

	public SaleBillUI() {
		super();
	}

	public SaleBillUI(String pk_corp, String billtype, String busitype, String operator, String id) {
		super(pk_corp, billtype, busitype, operator, id);
	}

	public InvokeEventProxy getPluginProxy() {
		if (pluginproxy == null) {
			try {
				pluginproxy = new InvokeEventProxy("SO", "30", getClientUI(), new SOPluginUI(this,
						"30"));
			} catch (BusinessException e) {
				SCMEnv.out(e);
			}
		}

		return pluginproxy;
	}

	/**
	 * 获得实现类的名字
	 * 
	 * @return
	 */
	protected abstract String getClientUI();

	protected void loadAllBtns() {
		super.loadAllBtns();
		getBoAfterAction();
	}

	/**
	 * 传入订单VO，通过对订单表体VO的金额进行统计 （刨除：缺货、赠品、附属物），然后计算预收款比例和金额。
	 * 
	 * 创建日期：(2003-11-07 13:22:27)
	 * 
	 * @param saleOrder
	 *            nc.vo.so.so001.SaleOrderVO
	 * @author:杨涛
	 * @description:
	 */
	private boolean calculatePreceive(SaleOrderVO saleorder) {
		BillItem bi = getBillCardPanel().getHeadItem("npreceiverate");
		if (bi == null) {
			SCMEnv.out("错误：预收款比例在模版中不存在!");
			return true;
		}
		bi = getBillCardPanel().getHeadItem("npreceivemny");
		if (bi == null) {
			SCMEnv.out("错误：预收款金额在模版中不存在!");
			return true;
		}

		// 处理预收款比例和预收款金额
		UFDouble npreceiverate = null;
		UFDouble npreceivemny = null;
		// 原币价税合计
		UFDouble noriginalcursummny = new UFDouble(0);

		SaleorderHVO ordhvo = saleorder.getHeadVO();
		SaleorderBVO[] ordbvos = saleorder.getBodyVOs();
		for (int i = 0, loop = ordbvos.length; i < loop; i++) {
			// 缺货属性
			UFBoolean boosflag = ordbvos[i].getBoosflag() == null ? new UFBoolean(false)
					: ordbvos[i].getBoosflag();
			// 赠品属性
			UFBoolean blargessflag = ordbvos[i].getBlargessflag() == null ? new UFBoolean(false)
					: ordbvos[i].getBlargessflag();

			// 附属物属性

			if (ordbvos[i].getCinventoryid() == null
					|| ordbvos[i].getCinventoryid().trim().length() <= 0)
				continue;
			if (!boosflag.booleanValue() && !blargessflag.booleanValue()
			/* && !isappendant.booleanValue() */) {
				UFDouble dobj = ordbvos[i].getNoriginalcursummny();
				if (dobj == null)
					dobj = new UFDouble(0);
				noriginalcursummny = noriginalcursummny.add(dobj);
			}
		}
		npreceiverate = ordhvo.getNpreceiverate();
		npreceivemny = ordhvo.getNpreceivemny();

		if (npreceiverate == null && npreceivemny == null) {
			return true;
		}

		if (npreceiverate != null && npreceiverate.doubleValue() == 0 && npreceivemny != null
				&& npreceivemny.doubleValue() == 0) {
			return true;
		}

		UFDouble temp = npreceivemny;
		if (temp == null && npreceiverate != null && "比例".equals(SO_21)) {
			temp = noriginalcursummny.multiply(npreceiverate).div(100);
		}

		/** 新增单据不作比较* */
		if (ordhvo.getCsaleid() != null && ordhvo.getCsaleid().length() > 0) {
			if ((ordhvo.getNreceiptcathmny() != null && temp != null)
					&& (temp.sub(ordhvo.getNreceiptcathmny()).doubleValue() < 0)) {
				showErrorMessage(NCLangRes.getInstance().getStrByID("40060301",
						"UPP40060301-000544")/*
												 * @res "预收款金额不能小于收款金额"
												 */);
				return false;
			}

		}

		if (noriginalcursummny.doubleValue() < 0) {
			showErrorMessage(NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000174")/*
																									 * @res
																									 * "订单价税合计小于0时，不能设置预收款金额或预收款比例"
																									 */);
			return false;
		}

		if (noriginalcursummny.doubleValue() == 0) {
			ordhvo.setNpreceiverate(null);
			ordhvo.setNpreceivemny(null);
			getBillCardPanel().setHeadItem("npreceiverate", null);
			getBillCardPanel().setHeadItem("npreceivemny", null);
			return true;
		}

		// 处理预售款比例为空的情况
		if (npreceiverate == null) {
			if (npreceivemny.doubleValue() < 0
					|| npreceivemny.doubleValue() > noriginalcursummny.doubleValue()) {
				showErrorMessage(NCLangRes.getInstance().getStrByID("40060301",
						"UPP40060301-000175")/*
												 * @res "预收款金额不能大于订单价税合计或小于0"
												 */);
				return false;
			}
			npreceiverate = npreceivemny.div(noriginalcursummny).multiply(100);
			ordhvo.setNpreceiverate(npreceiverate);
			getBillCardPanel().setHeadItem("npreceiverate", npreceiverate);
			getBillCardPanel().setHeadItem("npreceivemny", npreceivemny);
			return true;

		}

		// 处理预收款金额为空的情况
		if (npreceivemny == null) {
			if (npreceiverate.doubleValue() < 0 || npreceiverate.doubleValue() > 100) {
				showErrorMessage(NCLangRes.getInstance().getStrByID("40060301",
						"UPP40060301-000176")/*
												 * @res "预收款比例不能大于100或小于0"
												 */);
				return false;
			}
			npreceivemny = noriginalcursummny.multiply(npreceiverate).div(100);
			ordhvo.setNpreceivemny(npreceivemny);
			getBillCardPanel().setHeadItem("npreceiverate", npreceiverate);
			getBillCardPanel().setHeadItem("npreceivemny", npreceivemny);
			return true;

		}

		if ("比例".equals(SO_21)) {/*-=notranslate=-*/
			if (npreceiverate.doubleValue() < 0 || npreceiverate.doubleValue() > 100) {
				showErrorMessage(NCLangRes.getInstance().getStrByID("40060301",
						"UPP40060301-000176")/*
												 * @res "预收款比例不能大于100或小于0"
												 */);
				return false;
			}
			npreceivemny = noriginalcursummny.multiply(npreceiverate).div(100);
		} else if ("金额".equals(SO_21)) {/*-=notranslate=-*/
			if (npreceivemny.doubleValue() < 0
					|| npreceivemny.doubleValue() > noriginalcursummny.doubleValue()) {
				showErrorMessage(NCLangRes.getInstance().getStrByID("40060301",
						"UPP40060301-000178")/*
												 * @res "预收款金额不能小于0或大于订单金额"
												 */);
				return false;
			}
			npreceiverate = npreceivemny.div(noriginalcursummny).multiply(100);
		}

		if (npreceiverate.doubleValue() > 100) {
			showErrorMessage(NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000179")/*
																									 * @res
																									 * "预收款比例不能大于100"
																									 */);
			return false;
		}

		ordhvo.setNpreceiverate(npreceiverate);
		ordhvo.setNpreceivemny(npreceivemny);
		getBillCardPanel().setHeadItem("npreceiverate", npreceiverate);
		getBillCardPanel().setHeadItem("npreceivemny", npreceivemny);
		return true;
	}

	/**
	 * 检查来源单据是否可以合并。 创建日期：(2003-8-4 10:28:45)
	 */
	private boolean checkSourceComb(AggregatedValueObject[] voSource) {
		SaleorderBVO[] saleBs = (SaleorderBVO[]) voSource[0].getChildrenVO();
		if (saleBs == null || saleBs.length == 0)
			return true;
		String creceipttype = saleBs[0].getCreceipttype();
		if (creceipttype == null || creceipttype.length() == 0) {
			return true;
		}
		// wsy 20060329 增加约束
		boolean bCustomer = false;// 需要客户唯一？
		boolean bcurr = false;// 需要币种唯一？
		boolean bsaleorg = false;// 需要客户唯一？
		if (creceipttype.equals(SaleBillType.SoContract)
				|| creceipttype.equals(SaleBillType.SoInitContract)) {
			// 合同 客户+币种
			bCustomer = true;
			bcurr = true;
		}
		// 报价单
		else if (creceipttype.equals(SaleBillType.SaleQuotation)) {
			// 销售报价单形成销售订单时需要增加约束条件
			// 客户+币种+销售组织进行唯一性约束
			bCustomer = true;
			bcurr = true;
			bsaleorg = true;
		} else if (creceipttype.equals("4H")) {
			// 借出单形成销售订单时需要增加约束条件：
			// ü 按照：客户进行唯一性约束
			bCustomer = true;
		}

		// 参照合同
		if (voSource.length > 1) {
			// 客户基准值
			if (bCustomer) {
				String ccustomerid0 = ((SaleorderHVO) voSource[0].getParentVO()).getCcustomerid();
				if (ccustomerid0 != null) {
					for (int i = 1; i < voSource.length; i++) {
						String ccustomerid = ((SaleorderHVO) voSource[i].getParentVO())
								.getCcustomerid();
						if (ccustomerid != null) {
							if (!ccustomerid.trim().equals(ccustomerid0.trim())) {
								showErrorMessage(NCLangRes.getInstance().getStrByID("40060301",
										"UPP40060301-000182")/*
																 * @res
																 * "请选择同一客户的数据合成订单"
																 */);
								return false;
							}
						}
					}
				}
			}
			// 币种基准值
			if (bcurr) {
				SaleorderBVO[] salebodyVO = (SaleorderBVO[]) voSource[0].getChildrenVO();
				String currencyid0 = salebodyVO[0].getCcurrencytypeid();
				if (currencyid0 != null) {
					for (int i = 1; i < voSource.length; i++) {
						salebodyVO = (SaleorderBVO[]) voSource[i].getChildrenVO();
						String currencyid = salebodyVO[0].getCcurrencytypeid();
						if (!currencyid.trim().equals(currencyid0.trim())) {
							showErrorMessage(NCLangRes.getInstance().getStrByID("40060301",
									"UPP40060301-000183")/*
															 * @res
															 * "请选择同一币种的数据合成订单"
															 */);
							return false;
						}
					}
				}
			}
			// 销售组织基准值
			if (bsaleorg) {
				String ccustomerid0 = ((SaleorderHVO) voSource[0].getParentVO()).getCsalecorpid();
				if (ccustomerid0 != null) {
					for (int i = 1; i < voSource.length; i++) {
						String ccustomerid = ((SaleorderHVO) voSource[i].getParentVO())
								.getCsalecorpid();
						if (ccustomerid != null) {
							if (!ccustomerid.trim().equals(ccustomerid0.trim())) {
								showErrorMessage(NCLangRes.getInstance().getStrByID("40060301",
										"UPP40060301-000513")/*
																 * @res
																 * "请选择同一销售组织的数据合成订单"
																 */);
								return false;
							}
						}
					}
				}

			}

		}

		return true;
	}

	/**
	 * 获得辅助需要参数。 创建日期：(2001-11-23 9:03:31)
	 * 
	 * @return java.lang.String
	 * @param bo
	 *            nc.ui.pub.ButtonObject
	 */
	private Object getAssistantPara(ButtonObject bo) throws ValidationException {

		int row = getBillListPanel().getHeadTable().getSelectedRow();

		Object o = null;
		// 订单情况(单据ID)
		if (bo.getTag().equals("OrderAlterRpt") || bo.getTag().equals("OrderExecRpt")) {
			if (strShowState.equals("列表")) { /*-=notranslate=-*/
				o = getBillListPanel().getHeadBillModel().getValueAt(num, "csaleid");
			} else {
				o = getBillCardPanel().getHeadItem("csaleid").getValue();
			}
		}
		// ATP
		if (bo.getTag().equals("ATP")) {
			o = getVO(true);
		}
		// 客户信息
		if (bo.getTag().equals("CustInfo")) {
			if (strShowState.equals("列表")) { /*-=notranslate=-*/
				o = getBillListPanel().getHeadBillModel().getValueAt(num, "ccustomerid");
			} else {
				o = getBillCardPanel().getHeadItem("ccustomerid").getValue();
			}
		}
		// 客户信用
		if (bo.getTag().equals("CustCredited")) {
			String sCust = null;
			String sBiztype = null;
			String sProductLine = null;
			if (strShowState.equals("列表")) { /*-=notranslate=-*/
				Object oTemp = getBillListPanel().getHeadBillModel().getValueAt(num, "ccustomerid");
				sCust = oTemp == null ? null : oTemp.toString();

				oTemp = getBillListPanel().getHeadBillModel().getValueAt(num, "cbiztype");
				sBiztype = oTemp == null ? null : oTemp.toString();

				oTemp = getBillListPanel().getBodyBillModel().getValueAt(0, "cprolineid");
				sProductLine = oTemp == null ? null : oTemp.toString();
			} else {
				sCust = (String) getBillCardPanel().getHeadItem("ccustomerid").getValueObject();
				sBiztype = (String) getBillCardPanel().getHeadItem("cbiztype").getValueObject();
				sProductLine = (String) getBillCardPanel().getBodyValueAt(0, "cprolineid");
			}

			nc.vo.so.pub.CustCreditVO voCredit = new nc.vo.so.pub.CustCreditVO();
			voCredit.setPk_cumandoc(sCust);
			voCredit.setCbiztype(sBiztype);
			// 如果不严格按照产品线，传null；否则，传第一行表体
			if (SO27 == null || !SO27.booleanValue())
				voCredit.setCproductline(null);
			else
				voCredit.setCproductline(sProductLine);

			o = voCredit;
		}
		// 审批情况
		if (bo.getTag().equals("FlowState")) {
			java.util.ArrayList alTemp = new java.util.ArrayList();
			alTemp.add(getBillType());
			if (strShowState.equals("列表")) { /*-=notranslate=-*/
				alTemp.add(getBillListPanel().getHeadBillModel().getValueAt(num, "csaleid"));
			} else {
				alTemp.add(getBillCardPanel().getHeadItem("csaleid").getValueObject());
			}
			o = alTemp;
		}
		// 毛利
		if (bo.getTag().equals("Prifit")) {
			if (strShowState.equals("列表")) { /*-=notranslate=-*/
				nc.vo.so.so006.ProfitHeaderVO headVO = new nc.vo.so.so006.ProfitHeaderVO();
				// 公司
				headVO.setPkcorp(getCorpPrimaryKey());
				// 库存组织ID
				headVO.setCcalbodyid((String) getBillListPanel().getHeadBillModel().getValueAt(row,
						"ccalbodyid"));
				// 单据类型
				headVO.setBilltype(getBillType());
				// 币种
				if (getBillListPanel().getBodyTable().getRowCount() > 0) {
					String curID = (String) (getBillListPanel().getBodyBillModel().getValueAt(0,
							"ccurrencytypeid"));
					headVO.setCurrencyid(curID);
				}
				// 库存组织名称
				headVO.setCcalbodyname((String) getBillListPanel().getHeadBillModel().getValueAt(
						row, "ccalbodyname"));
				nc.vo.so.so006.ProfitItemVO[] bodyVOs = new nc.vo.so.so006.ProfitItemVO[getBillListPanel()
						.getBodyBillModel().getRowCount()];
				for (int i = 0; i < bodyVOs.length; i++) {
					nc.vo.so.so006.ProfitItemVO bodyVO = new nc.vo.so.so006.ProfitItemVO();

					String creccalbodyid = (String) getBillListPanel().getBodyBillModel()
							.getValueAt(i, "creccalbodyid");
					if (creccalbodyid != null && creccalbodyid.trim().length() > 0) {
						// yb edit 2003-11-05
						bodyVO.setCbodycalbodyid((String) getBillListPanel().getBodyBillModel()
								.getValueAt(i, "creccalbodyid"));
						bodyVO.setCbodycalbodyname((String) getBillListPanel().getBodyBillModel()
								.getValueAt(i, "creccalbody"));
						bodyVO.setCbodywarehouseid((String) getBillListPanel().getBodyBillModel()
								.getValueAt(i, "crecwareid"));
						bodyVO.setCbodywarehousename((String) getBillListPanel().getBodyBillModel()
								.getValueAt(i, "crecwarehouse"));
					} else {
						// yb edit 2003-11-05
						bodyVO.setCbodycalbodyid((String) getBillListPanel().getBodyBillModel()
								.getValueAt(i, "cadvisecalbodyid"));
						bodyVO.setCbodycalbodyname((String) getBillListPanel().getBodyBillModel()
								.getValueAt(i, "cadvisecalbody"));
						bodyVO.setCbodywarehouseid((String) getBillListPanel().getBodyBillModel()
								.getValueAt(i, "cbodywarehouseid"));
						bodyVO.setCbodywarehousename((String) getBillListPanel().getBodyBillModel()
								.getValueAt(i, "cbodywarehousename"));
					}

					// 存货ID
					bodyVO.setCinventoryid((String) getBillListPanel().getBodyBillModel()
							.getValueAt(i, "cinventoryid"));
					// 存货编码
					bodyVO.setCode((String) getBillListPanel().getBodyBillModel().getValueAt(i,
							"cinventorycode"));
					// 存货名称
					bodyVO.setName((String) getBillListPanel().getBodyBillModel().getValueAt(i,
							"cinventoryname"));
					// 规格型号
					bodyVO.setSize((String) getBillListPanel().getBodyBillModel().getValueAt(i,
							"GGXX"));
					// 批次
					bodyVO.setCbatchid((String) getBillListPanel().getBodyBillModel().getValueAt(i,
							"cbatchid"));
					// 数量
					bodyVO.setNumber((UFDouble) getBillListPanel().getBodyBillModel().getValueAt(i,
							"nnumber"));
					// 净价
					bodyVO.setNnetprice((UFDouble) getBillListPanel().getBodyBillModel()
							.getValueAt(i, "nnetprice") == null ? new UFDouble(0)
							: (UFDouble) getBillListPanel().getBodyBillModel().getValueAt(i,
									"nnetprice"));
					bodyVO.setNmny((UFDouble) getBillListPanel().getBodyBillModel().getValueAt(i,
							"nmny") == null ? new UFDouble(0) : (UFDouble) getBillListPanel()
							.getBodyBillModel().getValueAt(i, "nmny"));

					bodyVOs[i] = bodyVO;
					if (getBillListPanel().getBodyBillModel().getValueAt(i, "blargessflag") != null
							&& getBillListPanel().getBodyBillModel().getValueAt(i, "blargessflag")
									.toString().equals("false"))
						bodyVO.m_blargessflag = new UFBoolean(false);
					else
						bodyVO.m_blargessflag = new UFBoolean(true);
				}
				nc.vo.so.so006.ProfitVO profit = new nc.vo.so.so006.ProfitVO();
				profit.setParentVO(headVO);
				profit.setChildrenVO(bodyVOs);
				profit.validate();
				o = profit;
			}// end list handle
			else {
				o = getProfitVOonCardPanel();
			}// end card handle
		}
		return o;
	}

	/**
	 * 从卡片界面上获取利润相关数据
	 * 
	 * @return
	 * @throws Exception
	 */
	private Object getProfitVOonCardPanel() throws ValidationException {
		nc.vo.so.so006.ProfitHeaderVO headVO = new nc.vo.so.so006.ProfitHeaderVO();
		// 公司
		headVO.setPkcorp(getCorpPrimaryKey());
		// 库存组织ID
		headVO.setCcalbodyid(getBillCardPanel().getHeadItem("ccalbodyid").getValue());
		// 库存组织名称
		UIRefPane calBodyName = (UIRefPane) getBillCardPanel().getHeadItem("ccalbodyid")
				.getComponent();
		headVO.setCcalbodyname(calBodyName.getRefName());
		// 单据类型
		headVO.setBilltype(getBillType());
		// 币种
		headVO.setCurrencyid((String) getBillCardPanel().getHeadItem("ccurrencytypeid")
				.getValueObject());
		nc.vo.so.so006.ProfitItemVO[] bodyVOs = new nc.vo.so.so006.ProfitItemVO[getBillCardPanel()
				.getRowCount()];
		for (int i = 0; i < bodyVOs.length; i++) {
			nc.vo.so.so006.ProfitItemVO bodyVO = new nc.vo.so.so006.ProfitItemVO();

			String creccalbodyid = (String) getBillCardPanel().getBodyValueAt(i, "creccalbodyid");
			if (creccalbodyid != null && creccalbodyid.trim().length() > 0) {
				// yb edit 2003-11-05
				bodyVO.setCbodycalbodyid((String) getBillCardPanel().getBodyValueAt(i,
						"creccalbodyid"));
				bodyVO.setCbodycalbodyname((String) getBillCardPanel().getBodyValueAt(i,
						"creccalbody"));
				bodyVO.setCbodywarehouseid((String) getBillCardPanel().getBodyValueAt(i,
						"crecwareid"));
				bodyVO.setCbodywarehousename((String) getBillCardPanel().getBodyValueAt(i,
						"crecwarehouse"));
			} else {
				// yb edit 2003-11-05
				bodyVO.setCbodycalbodyid((String) getBillCardPanel().getBodyValueAt(i,
						"cadvisecalbodyid"));
				bodyVO.setCbodycalbodyname((String) getBillCardPanel().getBodyValueAt(i,
						"cadvisecalbody"));
				bodyVO.setCbodywarehouseid((String) getBillCardPanel().getBodyValueAt(i,
						"cbodywarehouseid"));
				bodyVO.setCbodywarehousename((String) getBillCardPanel().getBodyValueAt(i,
						"cbodywarehousename"));
			}

			// 存货ID
			bodyVO.setCinventoryid((String) getBillCardPanel().getBodyValueAt(i, "cinventoryid"));
			// 存货编码
			bodyVO.setCode((String) getBillCardPanel().getBodyValueAt(i, "cinventorycode"));
			// 存货名称
			bodyVO.setName((String) getBillCardPanel().getBodyValueAt(i, "cinventoryname"));
			// 规格型号
			bodyVO.setSize((String) getBillCardPanel().getBodyValueAt(i, "GGXX"));
			// 批次
			bodyVO.setCbatchid((String) getBillCardPanel().getBodyValueAt(i, "cbatchid"));
			// 数量
			bodyVO.setNumber((UFDouble) getBillCardPanel().getBodyValueAt(i, "nnumber"));
			// 净价
			Object value = getBillCardPanel().getBodyValueAt(i, "nnetprice");
			if (value == null || value.toString().toString().equals(""))
				value = new UFDouble(0);
			bodyVO.setNnetprice((UFDouble) value);
			if (getBillCardPanel().getBodyValueAt(i, "blargessflag") != null
					&& getBillCardPanel().getBodyValueAt(i, "blargessflag").toString().equals(
							"false"))
				bodyVO.m_blargessflag = new UFBoolean(false);
			else
				bodyVO.m_blargessflag = new UFBoolean(true);
			value = getBillCardPanel().getBodyValueAt(i, "nmny");
			if (value == null || value.toString().toString().equals(""))
				value = new UFDouble(0);
			bodyVO.setNmny((UFDouble) value);
			bodyVOs[i] = bodyVO;
		}
		nc.vo.so.so006.ProfitVO profit = new nc.vo.so.so006.ProfitVO();
		profit.setParentVO(headVO);
		profit.setChildrenVO(bodyVOs);
		profit.validate();

		return profit;
	}

	/**
	 * 获得按钮动作。 创建日期：(2001-11-15 8:56:16)
	 * 
	 * @return java.lang.String
	 * @param bo
	 *            nc.ui.pub.ButtonObject
	 */
	public abstract String getBillButtonAction(ButtonObject bo);

	/**
	 * 获得单据按钮状态组。 创建日期：(2001-11-15 8:48:20)
	 * 
	 * @return java.lang.String
	 */
	public abstract String getBillButtonState();

	/**
	 * 获得按钮数组。 创建日期：(2001-11-15 8:58:51)
	 * 
	 * @return nc.ui.pub.ButtonObject[]
	 */
	public abstract ButtonObject[] getBillButtons();

	/**
	 * 返回当前处理单据ID。 创建日期：(2001-11-15 9:10:05)
	 * 
	 * @return java.lang.String
	 */
	public abstract String getBillID();

	/**
	 * 返回当前处理单据ID。 创建日期：(2001-11-15 9:10:05)
	 * 
	 * @return java.lang.String
	 */
	private String getBillIDSource() {
		if (getBillListPanel().isShowing()) {
			num = getBillListPanel().getHeadTable().getSelectedRow();
		}

		if (num >= 0 && num < getBillListPanel().getHeadBillModel().getRowCount())
			return (String) getBillListPanel().getHeadBillModel().getValueAt(num, "csaleid");
		else
			/** 取卡片上的值，处理在直接卡片而在列表上没有值的情况* */
			return (String) getBillCardPanel().getHeadItem("csaleid").getValueObject();
	}

	/**
	 * 获得单据类型。 创建日期：(2001-11-15 8:52:43)
	 * 
	 * @return java.lang.String
	 */
	public String getBillType() {
		return SaleBillType.SaleOrder;
	}

	/**
	 * 获得行号 该行号为表体存货记录对应的自然数序列行号,为标记该行的可用量作准备。
	 * 
	 * 创建日期：(2002-10-11 15:28:57)
	 * 
	 * @return nc.vo.so.so001.SaleOrderVO
	 * @param voSource
	 *            nc.vo.so.so001.SaleOrderVO
	 */
	private SaleOrderVO getLineNumber(SaleOrderVO voSource, SaleOrderVO voChange) {
		SaleorderBVO[] voBodys = (SaleorderBVO[]) voSource.getChildrenVO();
		Vector vec = new Vector();
		// 获得变化记录的行号
		for (int i = 0; i < getBillCardPanel().getRowCount(); i++) {
			if (getBillCardPanel().getBillModel().getRowState(i) != BillModel.NORMAL) {
				voBodys[i].setNlinenumber(new Integer(i + 1));
				vec.addElement(voBodys[i]);
			}
		}
		// 获得删除的行记录
		SaleorderBVO[] voChangeBodys = (SaleorderBVO[]) voChange.getChildrenVO();
		for (int i = 0; i < voChangeBodys.length; i++) {
			if (voChangeBodys[i].getStatus() == nc.vo.pub.VOStatus.DELETED) {
				vec.addElement(voChangeBodys[i]);
			}
		}
		SaleorderBVO[] retBodys = new SaleorderBVO[vec.size()];
		if (vec.size() > 0) {
			vec.copyInto(retBodys);
		}

		SaleOrderVO copyvo = new SaleOrderVO();
		copyvo.setParentVO(voSource.getParentVO());
		copyvo.setChildrenVO(retBodys);
		// voSource.setChildrenVO(retBodys);
		return copyvo;
	}

	/**
	 * 梦杰自由项问题修改。 创建日期：(2003-10-15 10:05:48)
	 * 
	 * @return int[]
	 */
	private int[] getNotCheckFreeItemLine() {
		if (getBillCardPanel().alInvs == null || getBillCardPanel().alInvs.size() <= 0)
			return null;
		ArrayList indexlist = new ArrayList();
		for (int i = 0; i < getBillCardPanel().alInvs.size(); i++) {
			InvVO vo = (InvVO) getBillCardPanel().alInvs.get(i);
			if (vo == null)
				continue;
			if (vo.getFreeItemVO() != null) {
				if ((vo.getFreeItemVO().getVfreeid1() == null || vo.getFreeItemVO().getVfreeid1()
						.length() <= 0)
						&& (vo.getFreeItemVO().getVfreeid2() == null || vo.getFreeItemVO()
								.getVfreeid2().length() <= 0)
						&& (vo.getFreeItemVO().getVfreeid3() == null || vo.getFreeItemVO()
								.getVfreeid3().length() <= 0)
						&& (vo.getFreeItemVO().getVfreeid4() == null || vo.getFreeItemVO()
								.getVfreeid4().length() <= 0)
						&& (vo.getFreeItemVO().getVfreeid5() == null || vo.getFreeItemVO()
								.getVfreeid5().length() <= 0)
						&& (vo.getFreeItemVO().getVfreeid6() == null || vo.getFreeItemVO()
								.getVfreeid6().length() <= 0)
						&& (vo.getFreeItemVO().getVfreeid7() == null || vo.getFreeItemVO()
								.getVfreeid7().length() <= 0)
						&& (vo.getFreeItemVO().getVfreeid8() == null || vo.getFreeItemVO()
								.getVfreeid8().length() <= 0)
						&& (vo.getFreeItemVO().getVfreeid9() == null || vo.getFreeItemVO()
								.getVfreeid9().length() <= 0)
						&& (vo.getFreeItemVO().getVfreeid10() == null || vo.getFreeItemVO()
								.getVfreeid10().length() <= 0)) {
					indexlist.add(new Integer(i));
				}
			} else
				indexlist.add(new Integer(i));

		}

		if (indexlist.size() <= 0)
			return null;

		int[] ret = new int[indexlist.size()];
		for (int i = 0; i < indexlist.size(); i++) {
			ret[i] = ((Integer) indexlist.get(i)).intValue();
		}
		return ret;
	}

	/**
	 * 获得单据类型。
	 * 
	 * 创建日期：(2001-11-15 8:52:43)
	 * 
	 * @return java.lang.String
	 */
	public nc.ui.scm.sourcebill.SourceBillFlowDlg getSourceDlg() {

		soureDlg = new nc.ui.scm.sourcebill.SourceBillFlowDlg(this, getBillType(),/* 当前单据类型 */
		getBillIDSource(),/* 当前单据ID */
		getBillCardPanel().getBusiType(),/* 当前业务类型 */
		getBillCardPanel().getOperator(),/* 当前用户ID */
		((SaleOrderVO) getVo()).getHeadVO().getVreceiptcode()/* 单据号 */
		);

		return soureDlg;
	}

	/**
	 * 子类实现该方法，返回业务界面的标题。
	 * 
	 * @version (00-6-6 13:33:25)
	 * 
	 * @return java.lang.String
	 */
	public abstract String getTitle();

	/**
	 * 保存部件抛出异常时被调用
	 * 
	 * @param ex
	 *            java.lang.Throwable
	 */
	protected void handleSaveException(java.lang.Throwable ex) {
		showWarningMessage(ex.getMessage());
		showHintMessage(NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000185")/*
																							 * @res
																							 * "保存失败！"
																							 */);
	}

	public void handleException(Exception e, String msg) {
		MessageDialog.showWarningDlg(this, "警告", msg + e.getMessage());
		SCMEnv.out(e.getMessage());
	}

	/**
	 * 初始化执行按钮
	 */
	protected void retElseBtn(String billbuttonstate, String billbuttonaction) {

		// 订单执行
		if (billbuttonaction.equals("Order001")) {
			boAction.removeAllChildren();

			// 新增
			if (billbuttonstate.equals("Order001")) {
				// boAudit.setTag("APPROVE");
				// boAction.addChildButton(boAudit);
			}

			// 管理、卡片
			else if (billbuttonstate.equals("Order002") || billbuttonstate.equals("Order003")) {

				boAction.addChildButton(boSendAudit);

				boAction.addChildButton(boCancelAudit);
				boAction.addChildButton(boFreeze);
				boAction.addChildButton(boCancelFreeze);
				boAction.addChildButton(boFinish);

			}
		}

		// 订单辅助
		else if (billbuttonaction.equals("Order003")) {

			boAssistant.removeAllChildren();

			// // 新增
			if (billbuttonstate.equals("Order001")) {

				boAssistant.addChildButton(boCustCredit);
				boAssistant.addChildButton(boCustInfo);
				boAssistant.addChildButton(boPrifit);
				boAssistant.addChildButton(boOnHandShowHidden);

			}

			// // 管理、卡片
			else if (billbuttonstate.equals("Order002") || billbuttonstate.equals("Order003")) {

				boAssistant.addChildButton(boRefundment);
				boAssistant.addChildButton(boCachPay);
				boAssistant.addChildButton(boOrdBalance);
				boAssistant.addChildButton(boStockLock);
				boAssistant.addChildButton(boSendInv);
				boAssistant.addChildButton(boSupplyInv);
				boAssistant.addChildButton(boDirectInv);
				boAssistant.addChildButton(boDocument);
				boAssistant.addChildButton(boBom);

			}

		}

		// 配制执行
		else if (billbuttonaction.equals("BomOrder001")) {
			boAction.removeAllChildren();

			// 新增
			if (billbuttonstate.equals("BomOrder001")) {
				boAction.addChildButton(boAudit);
			}

			// 管理
			else if (billbuttonstate.equals("BomOrder002")) {
				boAction.addChildButton(boAudit);
				boAction.addChildButton(boCancelAudit);

			}

			// 卡片
			else if (billbuttonstate.equals("BomOrder003")) {
				boAction.addChildButton(boAudit);
			}

		}
		// 配制后续
		else if (billbuttonaction.equals("BomOrder002")) {

		}

	}

	/**
	 * 初始化按钮
	 * 
	 * 创建日期：(01-2-26 13:29:17)
	 * 
	 * @comment 子类覆写此方法时，需参照此处处理
	 * 
	 */
	protected void initButtons() {

		// 业务类型
		PfUtilClient.retBusinessBtn(boBusiType, getCorpPrimaryKey(), getBillType());

		if (strState.equals("BOM")) {
			setButtons(bomButtonGroup);
		} else {
			initLineButton();

			if (boBusiType.getChildButtonGroup() != null
					&& boBusiType.getChildButtonGroup().length > 0) {

				ButtonObject[] bo = boBusiType.getChildButtonGroup();
				for (int i = 0; i < bo.length; i++) {
					bo[i].setName(bo[i].getName());
				}
				boBusiType.setChildButtonGroup(bo);

				boBusiType.setTag(boBusiType.getChildButtonGroup()[0].getTag());
				boBusiType.getChildButtonGroup()[0].setSelected(true);
				boBusiType.setCheckboxGroup(true);

				// 新增
				PfUtilClient.retAddBtn(boAdd, getCorpPrimaryKey(), getBillType(), boBusiType
						.getChildButtonGroup()[0]);

				bo = boAdd.getChildButtonGroup();
				for (int i = 0; i < bo.length; i++) {
					bo[i].setName(bo[i].getName());
				}
				boAdd.setChildButtonGroup(bo);

				retElseBtn(getBillButtonState(), getBillButtonAction(boAction));
				retElseBtn(getBillButtonState(), getBillButtonAction(boAssistant));
			} else {
				SCMEnv.out("没有初始化业务类型!");
			}

			setButtons(getBillButtons());
		}
	}

	private void initInvList() {

		getBillCardPanel().alInvs = new java.util.ArrayList();
		if (getBillCardPanel().getRowCount() <= 0)
			return;

		try {
			// 批量获取存货信息
			InvVO[] invvos = new InvVO[getBillCardPanel().getRowCount()];
			for (int i = 0; i < invvos.length; i++) {
				invvos[i] = new InvVO();
				invvos[i].setCinventoryid((String) getBillCardPanel().getBodyValueAt(i,
						"cinventoryid"));
			}

			InvoInfoBYFormula invvosget = new InvoInfoBYFormula();
			invvos = invvosget.getQuryInvVOs(invvos, false, true, 1);

			for (int i = 0; i < invvos.length; i++)
				getBillCardPanel().alInvs.add(invvos[i]);

			if (getBillCardPanel().alInvs != null) {
				for (int i = 0; i < getBillCardPanel().getRowCount(); i++) {
					InvVO voInv = (InvVO) getBillCardPanel().alInvs.get(i);
					getBillCardPanel().setBodyFreeValue(i, voInv);
				}
			}

		} catch (Exception ex) {
			SCMEnv.out("自由项设置失败!");
		}
	}

	/**
	 * 初始化行编辑按纽。
	 * 
	 * 创建日期：(2001-11-27 14:53:22)
	 * 
	 */
	protected void initLineButton() {
		boLine.removeAllChildren();
		boLine.addChildButton(boAddLine);
		boLine.addChildButton(boDelLine);
		boLine.addChildButton(boInsertLine);
		boLine.addChildButton(boCopyLine);
		boLine.addChildButton(boPasteLine);
		boLine.addChildButton(boPasteLineToTail);
		boLine.addChildButton(boFindPrice);
		boLine.addChildButton(boCardEdit);
		boLine.addChildButton(boResortRowNo);
	}

	/**
	 * 加载卡片数据。
	 * 
	 * 创建日期：(2001-11-15 9:02:22)
	 * 
	 */
	public void loadCardData() {
		try {
			long s = System.currentTimeMillis();
			boolean bisCalculate = getBillCardPanel().getBillModel().isNeedCalculate();
			getBillCardPanel().getBillModel().setNeedCalculate(false);

			SaleOrderVO saleorderVO = (SaleOrderVO) SaleOrderBO_Client.queryData(getBillID());
			// 查询现收金额
			if (saleorderVO != null && saleorderVO.getHeadVO().getCsaleid() != null) {
				saleorderVO.getHeadVO().setNreceiptcathmny(
						SaleOrderBO_Client
								.queryCachPayByOrdId(saleorderVO.getHeadVO().getCsaleid()));
			}
			SCMEnv.out("数据读取[共用时" + (System.currentTimeMillis() - s) + "]");
			s = System.currentTimeMillis();
			// 金额按币种携带精度
			getBillCardPanel().setPanelByCurrency(
					((SaleorderBVO) saleorderVO.getChildrenVO()[0]).getCcurrencytypeid());

			// 收货地址参照
			UIRefPane vreceiveaddress = (UIRefPane) getBillCardPanel().getHeadItem(
					"vreceiveaddress").getComponent();
			vreceiveaddress.setAutoCheck(false);

			// 加载数据
			getBillCardPanel().setBillValueVO(saleorderVO);
			
			// 给1-20自定义项pk设值
			DefSetTool.updateCardDefValues(getBillCardPanel().getBillData(), "vdef", "pk_defdoc");
			
			// 执行加载公式
			getBillCardPanel().getBillModel().execLoadFormula();

			// 加载是否打印合计
			copyIstotal();
			SCMEnv.out("数据设置[共用时" + (System.currentTimeMillis() - s) + "]");
			long s1 = System.currentTimeMillis();

			getBillCardPanel().getBillModel().execLoadFormula();

			SCMEnv.out("执行公式[共用时" + (System.currentTimeMillis() - s1) + "]");

			s1 = System.currentTimeMillis();

			// 收货地址参照
			if (getBillCardPanel().getHeadItem("creceiptcustomerid") != null)
				((CustAddrRefModel) vreceiveaddress.getRefModel())
						.setCustId((String) getBillCardPanel().getHeadItem("creceiptcustomerid")
								.getValueObject());

			Object temp = saleorderVO.getParentVO().getAttributeValue("vreceiveaddress");
			// 基础档案ID
			String formula = "getColValue(bd_cumandoc,pk_cubasdoc,pk_cumandoc,creceiptcustomerid)";
			getBillCardPanel().execHeadFormula(formula);

			// 收货地址
			vreceiveaddress.getUITextField().setText(temp == null ? "" : temp.toString());

			// 设置整单折扣
			UFDouble ndiscountrate = getBillCardTools().getHeadUFDoubleValue("ndiscountrate");
			if (ndiscountrate == null) {
				ndiscountrate = getBillCardTools().getBodyUFDoubleValue(0, "ndiscountrate");
			}
			if (ndiscountrate == null)
				ndiscountrate = new UFDouble(100);

			getBillCardPanel().setHeadItem("ndiscountrate", ndiscountrate);

			// 加载自由项
			getBillCardPanel().initFreeItem();

			// 加载表头项目携带到表体
			setHeadDefaultData();

			// 换算率
			getBillCardPanel().initUnit();

			SCMEnv.out("加载表头[共用时" + (System.currentTimeMillis() - s1) + "]");

			s1 = System.currentTimeMillis();

			// 批次状态公式,BOM标志 //yb 优化代码
			if (getBillType().equals(SaleBillType.SaleOrder)) {
				String[] formulas = {
						"wholemanaflag->getColValue(bd_invmandoc,wholemanaflag,pk_invmandoc,cinventoryid)",
						"isconfigable->getColValue(bd_invmandoc,isconfigable,pk_invmandoc,cinventoryid)",
						"isspecialty->getColValue(bd_invmandoc,isspecialty,pk_invmandoc,cinventoryid)" };
				getBillCardPanel().getBillModel().execFormulas(formulas);
			}
			// 附属品标志加载
			String[] appendFormula = { "isappendant->getColValue(bd_invmandoc,isappendant,pk_invmandoc,cinventoryid)" };
			getBillCardPanel().getBillModel().execFormulas(appendFormula);

			ArrayList alist = new ArrayList();
			for (int i = 0; i < getBillCardPanel().getRowCount(); i++) {
				// 计量单位

				/** 请使用新算法工具计算* */
				// // 计算辅计量无税单价,辅计量含税单价
				// BillTools.calcViaPrice1(i, getBillCardPanel().getBillModel(),
				// getBillType());
				// 合同存货类名称携带
				Object oCsourcebillid = getBillCardPanel().getBillModel().getValueAt(i,
						"csourcebillid");
				if (oCsourcebillid != null && oCsourcebillid.toString().length() != 0) {
					alist.add(new Integer(i));
				}
			}

			String[] bodyFormula = new String[2];
			bodyFormula[0] = "ctinvclassid->getColValue(ct_manage_b,invclid,pk_ct_manage_b,csourcebillbodyid)";
			bodyFormula[1] = "ctinvclass->getColValue(bd_invcl,invclassname,pk_invcl,ctinvclassid)";
			getBillCardTools().execBodyFormulas(bodyFormula, alist);

			// 销售公司
			getBillCardPanel().setHeadItem("salecorp",
					getClientEnvironment().getCorporation().getUnitname());

			SCMEnv.out("加载表体[共用时" + (System.currentTimeMillis() - s1) + "]");

			getBillCardPanel().updateValue();

			getBillCardPanel().getBillModel().setNeedCalculate(bisCalculate);
			if (getBillCardPanel().getHeadItem("nheadsummny") != null)
				getBillCardPanel().getHeadItem("nheadsummny").setValue(
						getBillCardPanel().getTotalValue("noriginalcursummny"));

			setButtonsState();
			showHintMessage(NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000186",
					null, new String[] { (System.currentTimeMillis() - s) / 1000 + "" }));

		} catch (ValidationException e) {
			showErrorMessage(e.getMessage());
		} catch (Exception e) {
			showHintMessage(NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000256")/*
																									 * @res
																									 * "数据加载失败！"
																									 */);
			// e.printStackTrace(System.out);
		}
	}

	private void copyIstotal() {
		int iRow = getBillCardPanel().getRowCount();
		for (int i = 0; i < iRow; i++) {
			Object largess = getBillCardPanel().getBodyValueAt(i, "blargessflag");
			if (largess != null && new UFBoolean(largess.toString()).booleanValue()) {
				getBillCardPanel().setBodyValueAt("1", i, "is_total");
			}
		}
	}

	/**
	 * 加载卡片数据。
	 * 
	 * ybadd 创建日期：(2003-8-26 9:02:22)
	 * 
	 */
	public void loadCardData(SaleOrderVO billvo) {
		if (billvo == null) {
			getBillCardPanel().addNew();
			return;
		}

		try {

			long s = System.currentTimeMillis();

			// 金额按币种携带精度
			getBillCardPanel().setPanelByCurrency(
					((SaleorderBVO) billvo.getChildrenVO()[0]).getCcurrencytypeid());
			// 收货地址参照
			UIRefPane vreceiveaddress = (UIRefPane) getBillCardPanel().getHeadItem(
					"vreceiveaddress").getComponent();
			vreceiveaddress.setAutoCheck(false);

			// 设置表头汇率精度
			getBillCardTools().setHeadItemDigits(
					"nexchangeotobrate",
					getSOCurrencyRateUtil().getExchangeRateDigits(
							billvo.getHeadVO().getCcurrencytypeid()));

			// 加载数据
			getBillCardPanel().setBillValueVO(billvo);
			
			// 给1-20自定义项pk设值
			DefSetTool.updateCardDefValues(getBillCardPanel().getBillData(), "vdef", "pk_defdoc");
			
			/*// 给1-20自定义项pk设值  liuzwc 2006-06-25
			for (int i=1; i<=20; i++){
				Object tempDefdoc= billvo.getParentVO().getAttributeValue("pk_defdoc"+i) ;
				if(tempDefdoc != null) {
					UIRefPane tempVdef = (UIRefPane)getBillCardPanel().getHeadItem("vdef"+i).getComponent();
					tempVdef.setPK(tempDefdoc);
				}
			}*/

			// 设置表体汇率精度
			nc.ui.so.so001.panel.bom.BillTools.setExchangeRateDigitsByCurrency(getBillCardPanel()
					.getBillModel(), billvo.getChildrenVO(), "ccurrencytypeid",
					billvo.getPk_corp(), "nexchangeotobrate");

			// 执行加载公式
			getBillCardPanel().execHeadLoadFormulas();
			execBodyLoadFormula("卡片");
			//getBillCardPanel().getBillModel().execLoadFormula();

			// 加载是否打印合计
			copyIstotal();

			long s1 = System.currentTimeMillis();

			SCMEnv.out("执行公式[共用时" + (System.currentTimeMillis() - s1) + "]");
			if (getBillCardPanel().getHeadItem("creceiptcustomerid") != null)
				((CustAddrRefModel) vreceiveaddress.getRefModel())
						.setCustId((String) getBillCardPanel().getHeadItem("creceiptcustomerid")
								.getValueObject());
			Object temp = billvo.getParentVO().getAttributeValue("vreceiveaddress");
			// 基础档案ID
			String formula = "getColValue(bd_cumandoc,pk_cubasdoc,pk_cumandoc,creceiptcustomerid)";
			// String pk_cubasdoc = (String)
			getBillCardPanel().execHeadFormula(formula);

			// 收货地址
			vreceiveaddress.getUITextField().setText(temp == null ? "" : temp.toString());

			// 加载自由项
			getBillCardPanel().initFreeItem();

			// 加载表头项目携带到表体
			/** 币种相关，表头去表体* */
			setHeadDefaultData();

			// 换算率
			getBillCardPanel().initUnit();

			SCMEnv.out("加载表头数据bo[共用时" + (System.currentTimeMillis() - s) + "]");
			s = System.currentTimeMillis();

			// 表体属性加栽
			String[] formulas = {
					"wholemanaflag->getColValue(bd_invmandoc,wholemanaflag,pk_invmandoc,cinventoryid)",
					"isconfigable->getColValue(bd_invmandoc,isconfigable,pk_invmandoc,cinventoryid)",
					"isspecialty->getColValue(bd_invmandoc,isspecialty,pk_invmandoc,cinventoryid)",
					"isappendant->getColValue(bd_invmandoc,isappendant,pk_invmandoc,cinventoryid)" };

			// 附属品标志加载
			getBillCardPanel().getBillModel().execFormulas(formulas);

			ArrayList alist = new ArrayList();
			for (int i = 0; i < getBillCardPanel().getRowCount(); i++) {
				// 计量单位

				/** 请使用新算法工具计算* */
				// 计算辅计量无税单价,辅计量含税单价
				// BillTools.calcViaPrice1(i, getBillCardPanel().getBillModel(),
				// getBillCardPanel().getBillType());
				// 合同存货类名称携带
				Object oCsourcebillid = getBillCardPanel().getBillModel().getValueAt(i,
						"csourcebillid");
				if (oCsourcebillid != null && oCsourcebillid.toString().length() != 0) {

					alist.add(new Integer(i));

				}
			}

			String[] bodyFormula = new String[]{"ctinvclass->getColValue(bd_invcl,invclassname,pk_invcl,ctinvclassid)"};
			getBillCardTools().execBodyFormulas(bodyFormula, alist);

			getBillCardTools().setBodyInventory1(0, getBillCardPanel().getRowCount());

			// 设置整单折扣
			UFDouble ndiscountrate = getBillCardTools().getHeadUFDoubleValue("ndiscountrate");
			if (ndiscountrate == null) {
				ndiscountrate = getBillCardTools().getBodyUFDoubleValue(0, "ndiscountrate");
			}
			if (ndiscountrate == null)
				ndiscountrate = new UFDouble(100);

			getBillCardPanel().setHeadItem("ndiscountrate", ndiscountrate);

			// 销售公司
			getBillCardPanel().setHeadItem("salecorp",
					getClientEnvironment().getCorporation().getUnitname());

			getBillCardPanel().updateValue();
			getBillCardPanel().getBillModel().reCalcurateAll();
			if (getBillCardPanel().getHeadItem("nheadsummny") != null)
				getBillCardPanel().getHeadItem("nheadsummny").setValue(
						getBillCardPanel().getTotalValue("noriginalcursummny"));
            //2009-06-24 升级上来的统计类型参照无法显示问题
			/* 处理自定义项 统计类型参照设置后 pk值未设为null问题 */
//			BillItem item;
//			for (int i = 1; i <= 20; i++) {
//				item = getBillCardPanel().getHeadItem("vdef" + i);
//				if (item.getDataType() == BillItem.USERDEF || item.getDataType() == BillItem.UFREF) {
//					((UIRefPane) item.getComponent()).setPK(getBillCardPanel().getHeadItem(
//							"pk_defdoc" + i).getValue());
//				}
//			}

			// 插件支持
			getPluginProxy().afterSetBillVOToCard(billvo);

			SCMEnv.out("加载表体数据bo[共用时" + (System.currentTimeMillis() - s) + "]");

		} catch (Exception e) {
			showHintMessage(NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000256")/*
																									 * @res
																									 * "数据加载失败！"
																									 */);
			// e.printStackTrace(System.out);
		}
	}

	/**
	 * 加载数据ID。
	 * 
	 * 创建日期：(2001-11-15 9:02:22)
	 * 
	 */
	private void loadIDafterADD(ArrayList listID) {
		try {
			removeOOSLine();
			// 重新加载表头

			if (listID == null)
				return;
			getBillCardPanel().setHeadItem("csaleid", (String) listID.get(0));
			getBillCardPanel().setHeadItem("vreceiptcode", (String) listID.get(1));
			
			int lastcount = 1;
			Object retObj = listID.get(listID.size() - 1);
			SaleOrderVO retVO = null;
			SaleorderBVO[] retbvos = null;
			String[] keys = null;
			if (retObj.getClass()== SaleOrderVO.class){
				retVO = (SaleOrderVO) listID.get(listID.size() - 1);
				retbvos = retVO.getBodyVOs();
				keys = new String[] { "noriginalcursummny", "noriginalcurmny",
						"noriginalcurtaxmny", "ntaxmny", "nmny", "nsummny" };
				lastcount = 2;
			}
			
			//表体ts
			Object ots = listID.get(listID.size() - lastcount);
			
			for (int i = 2; i < listID.size() - lastcount; i++) {
				getBillCardPanel().setBodyValueAt((String) listID.get(0), i - 2, "csaleid");
				getBillCardPanel().setBodyValueAt((String) listID.get(i), i - 2, "corder_bid");

				if (retbvos!=null)
					for (String key : keys)
						getBillCardPanel().setBodyValueAt(
								retbvos[i - 2].getAttributeValue(key), i - 2,key);

				getBillCardPanel().setBodyValueAt(ots, i - 2, "ts");
				getBillCardPanel().setBodyValueAt(ots, i - 2, "exets");

			}

			getBillCardPanel().setHeadItem("ts", ots);

		} catch (Exception e) {
			showHintMessage(NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000256")/*
																									 * @res
																									 * "数据加载失败！"
																									 */);
			// e.printStackTrace(System.out);
		}
	}

	/**
	 * 加载数据ID。
	 * listID[0] = 表头id
	 * listID[1..n] = 表体id
	 * listID[n+1] = vo
	 * listID[n+2] = 其他
	 * 
	 * 创建日期：(2001-11-15 9:02:22)
	 * 
	 */
	private void loadIDafterEDIT(ArrayList listID,boolean ifeditHeadcalvalue) {
		try {
			removeOOSLine();
/*			// 重新加载表头TS
			String formula[] = { "vreceiptcode->getColValue(so_sale,vreceiptcode,csaleid,csaleid)" };
			getBillCardPanel().execHeadFormulas(formula);*/

			if (listID == null)
				return;
			
			int lastcount = 2;
			Object retObj = listID.get(listID.size() - lastcount);
			SaleOrderVO retVO = null;
			SaleorderBVO[] retbvos = null;
			String[] keys = null;
			Map<String,SaleorderBVO> rowNoVo = new HashMap<String,SaleorderBVO>();
			if (retObj.getClass()== SaleOrderVO.class){
				retVO = (SaleOrderVO) retObj;
				retbvos = retVO.getBodyVOs();
				keys = new String[] { "noriginalcursummny", "noriginalcurmny",
						"noriginalcurtaxmny", "ntaxmny", "nmny", "nsummny" };
				lastcount = 3;
				for (SaleorderBVO bvo : retbvos)
					rowNoVo.put(bvo.getCrowno(), bvo);
			}
			
			//表体ts
			Object ots = listID.get(listID.size() - lastcount);
			
			//表体id索引
			int i = 1;
			String rowNo = null;
			SaleorderBVO bvo = null;
			for (int j = 0; j < getBillCardPanel().getRowCount(); j++) {				
				if (getBillCardPanel().getBillModel().getRowState(j) == BillModel.ADD) {
					// 查到行ID为空的，将新ID载入
					getBillCardPanel().setBodyValueAt(
							getBillCardPanel().getHeadItem("csaleid").getValueObject(), j,"csaleid");
					getBillCardPanel().setBodyValueAt((String) listID.get(i), j, "corder_bid");
					
					getBillCardPanel().setBodyValueAt(ots, j, "ts");
					getBillCardPanel().setBodyValueAt(ots, j, "exets");

					i++;
				}
				if (getBillCardPanel().getBillModel().getRowState(j) == BillModel.MODIFICATION
						|| ifeditHeadcalvalue) {
					getBillCardPanel().setBodyValueAt(ots, j, "ts");
					getBillCardPanel().setBodyValueAt(ots, j, "exets");
				}
				
				rowNo = getBillCardPanel().getBodyValueAt(j, "crowno").toString();
				bvo = rowNoVo.get(rowNo);
				if (bvo!=null){
					for (String key : keys)
						getBillCardPanel().setBodyValueAt(
								bvo.getAttributeValue(key), j,key);
				}
				
			}
			
			getBillCardPanel().setHeadItem("ts", ots);
			
		} catch (Exception e) {
			showHintMessage(NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000256")/*
																									 * @res
																									 * "数据加载失败！"
																									 */);
			// e.printStackTrace(System.out);
		}
	}

	/**
	 * 标记存货行的可用量状态。
	 * 
	 * 创建日期：(2002-10-11 15:52:18)
	 * 
	 * @param sMessage
	 *            java.lang.String
	 * 
	 */
	private void markRow(String message) {
		Vector vecTemp = new Vector();
		int pos0 = 0;
		if (message != null) {
			do {
				int start = message.indexOf("<-", pos0);
				if (start < 0)
					break;
				int end = message.indexOf("->", start);
				String temp = message.substring(start + 2, end);
				vecTemp.add(temp);
				SCMEnv.out(temp);
				pos0 = end;
			} while (true);
		}
		vRowATPStatus = new Vector();
		for (int i = 0; i < getBillCardPanel().getRowCount(); i++) {
			vRowATPStatus.addElement(new UFBoolean(false));
		}
		// 如果没有可用量不足的存货，就清空可用量状态向量
		if (vecTemp != null && vecTemp.size() != 0) {
			for (int i = 0; i < vecTemp.size(); i++) {
				int iRow = new Integer(vecTemp.elementAt(i).toString()).intValue();
				vRowATPStatus.setElementAt(new UFBoolean(true), iRow - 1);
			}
		}
		updateUI();
	}

	/**
	 * 判断当前业务类型中，销售订单是跨公司直运推式生成调拨订单
	 * 
	 * @param vo
	 * @return
	 * @throws BusinessException
	 */
	private boolean ifPushSave205A5D(SaleOrderVO vo) throws BusinessException {
		boolean flag = false;

		// **********过滤掉发货公司和订单公司相同的订单行**********
		String pk_corp = vo.getPk_corp();
		SaleorderBVO[] bvos = vo.getBodyVOs();
		String consigncorpid;
		ArrayList<SaleorderBVO> al_to = new ArrayList<SaleorderBVO>();
		for (int i = 0, len = bvos.length; i < len; i++) {
			consigncorpid = bvos[i].getCconsigncorpid();
			if (consigncorpid == null || consigncorpid.trim().length() <= 0
					|| consigncorpid.equals(pk_corp))
				continue;
			al_to.add(bvos[i]);
		}// end for
		if (al_to.size() == 0)
			return flag;
		// **********过滤掉发货公司和订单公司相同的订单行**********

		// 读取消息驱动信息
		IPFMetaModel bo = (IPFMetaModel) NCLocator.getInstance().lookup(
				IPFMetaModel.class.getName());

		MessagedriveVO message[] = bo.queryAllMsgdrvVOs(vo.getPk_corp(), SaleBillType.SaleOrder, vo
				.getBizTypeid(), "APPROVE");

		// 公司流程查不到、查集团
		if (message == null || message.length <= 0) {
			message = bo.queryAllMsgdrvVOs("@@@@", SaleBillType.SaleOrder, vo.getBizTypeid(),
					"APPROVE");
		}

		// 判断当前业务类型中，销售订单是否推式生成调拨订单
		if (message != null && message.length != 0) {
			for (int j = 0; j < message.length; j++) {
				if (message[j].getActiontype().toString().equals("PushWrite205A5D")) {
					flag = true;
					break;
				}
			}
		}

		return flag;
	}

	protected void onAction(ButtonObject bo) {

		long s_init = System.currentTimeMillis();

		if (bo == boClose) {
			return;
		}

		// 删除前提示
		if (bo.getTag().equals("SoBlankout")) {
			// 存在有效数据
			if ((strShowState.equals("列表") && getBillListPanel().getHeadTable()
					.getSelectedRowCount() >= 1)
					|| strShowState.equals("卡片")) {
				// 提示
				if (showYesNoMessage(NCLangRes.getInstance().getStrByID("40060301",
						"UPP40060301-000540")/*
												 * @res "是否确实要删除?"
												 */) != MessageDialog.ID_YES) {
					return;
				}
			}
		}

		// 列表界面下的批审处理
		if (strShowState.equals("列表") /*-=notranslate=-*/
				&& getBillListPanel().getHeadTable().getSelectedRowCount() > 1
				|| getBillListPanel().isTempVO()) {
			if (bo.getTag().equals("APPROVE")) {
				onApprove(bo);
			} else if (bo.getTag().equals("SoUnApprove")) {
				onUnApprove(bo);
			} else if (bo.getTag().equals("SoBlankout")) {
				onBlankout(bo);
			}

			return;

		}

		SaleOrderVO saleorder = (SaleOrderVO) getVO(false);

		Integer fstatus = saleorder.getHeadVO().getFstatus();
		if (fstatus != null && fstatus.intValue() == BillStatus.BLANKOUT) {
			showHintMessage(NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000500")/*
																								 * @res
																								 * "订单已作废！"
																								 */);
			return;
		}

		long s1 = 0;
		try {
			// long s = System.currentTimeMillis();

			if (saleorder != null)
				saleorder.validate();

			SaleorderBVO[] ordbvo = saleorder.getBodyVOs();

			if (bo.getTag().equals("SoBlankout")) {
				/** 更新表头状态 传递到crm使用* */
				saleorder.setStatus(VOStatus.DELETED);
				saleorder.setIAction(ISaleOrderAction.A_BLANKOUT);
				for (int i = 0, loop = ordbvo.length; i < loop; i++) {
					//add by ouyangzhb 2013-05-13 判断行是否存在码单锁定
					if(isHasMDWH(i)){
						return ;
					}
					// 设置atp更新动作，以便更新ATP
					ordbvo[i].setIAction(ISaleOrderAction.A_OUTEND);
					ordbvo[i].setStatus(VOStatus.DELETED);

				}
			} else if (bo.getTag().equals("SoUnApprove")) {
				saleorder.setIAction(ISaleOrderAction.A_UNAUDIT);
				for (int i = 0, loop = ordbvo.length; i < loop; i++) {
					// 设置atp更新动作，以便更新ATP
					ordbvo[i].setIAction(ISaleOrderAction.A_UNAUDIT);
				}
			} else if (bo.getTag().equals("APPROVE")) {
				saleorder.setIAction(ISaleOrderAction.A_AUDIT);
				for (int i = 0, loop = ordbvo.length; i < loop; i++) {
					// 设置atp更新动作，以便更新ATP
					ordbvo[i].setIAction(ISaleOrderAction.A_AUDIT);
				}
			} else if (bo.getTag().equals("OrderFinish")) {
				saleorder.setIAction(ISaleOrderAction.A_CLOSE);
				for (int i = 0, loop = ordbvo.length; i < loop; i++) {
					// 设置atp更新动作，以便更新ATP
					ordbvo[i].setIAction(ISaleOrderAction.A_OUTEND);
				}
			} else if (bo.getTag().equals("OrderFreeze")) {
				saleorder.setIAction(ISaleOrderAction.A_FREEZE);
				for (int i = 0, loop = ordbvo.length; i < loop; i++) {
					// //设置atp更新动作，以便更新ATP
					ordbvo[i].setIAction(ISaleOrderAction.A_DEFAULT);
				}
			} else if (bo.getTag().equals("OrderUnFreeze")) {
				saleorder.setIAction(ISaleOrderAction.A_UNFREEZE);
				for (int i = 0, loop = ordbvo.length; i < loop; i++) {
					// //设置atp更新动作，以便更新ATP
					ordbvo[i].setIAction(ISaleOrderAction.A_DEFAULT);
				}
			}

			if (bo.getTag().equals("APPROVE")) {
				onApproveCheck(saleorder);
				nc.vo.scm.pub.SCMEnv.out("审核检测通过");
			}
			if (bo.getTag().equals("SoUnApprove")) {
				onUnApproveCheck(saleorder);
			}

			nc.vo.scm.pub.SCMEnv.out("<====== 销售订单:[" + bo.getName() + "]动作 ## 调用脚本前准备时间共耗时["
					+ (System.currentTimeMillis() - s_init) / 1000 + "."
					+ (System.currentTimeMillis() - s_init) % 1000 + "秒]==============>");

			nc.vo.scm.pub.session.ClientLink clientLink = new nc.vo.scm.pub.session.ClientLink(
					getClientEnvironment());
			saleorder.setClientLink(clientLink);
			saleorder.setCuruserid(getClientEnvironment().getUser().getPrimaryKey());
			saleorder.setDcurdate(getClientEnvironment().getDate());
			saleorder.setCusername(getClientEnvironment().getUser().getUserName());
			saleorder.setCcorpname(getClientEnvironment().getCorporation().getUnitname());
			saleorder.setCnodename(NCLangRes.getInstance().getStrByID("40060301",
					"UPP40060301-000504")
			/* @res "訂單管理" */);

			s1 = System.currentTimeMillis();

			if (bo.getTag().equals("APPROVE")) {

				getBillCardTools().getPkarrivecorp(saleorder);
				saleorder.getHeadVO().setCapproveid(clientLink.getUser());

				saleorder.processVOForTrans();

				Object otemp = null;
				boolean bContinue = true;
				while (bContinue) {
					try {

						// 对象压缩 V51 流量调整
						ObjectUtils.objectReference(saleorder);

						otemp = PfUtilClient.processActionFlow(this, bo.getTag(), getBillType(),
								getClientEnvironment().getDate().toString(), saleorder, null);
						bContinue = false;
					} catch (Exception ex) {
						bContinue = doException(saleorder, null, ex);

					}
				}

				if (otemp != null) {
					String ErrMsg = otemp.toString();
					if (ErrMsg != null && ErrMsg.startsWith("ERR")) {
						showWarningMessage(ErrMsg.substring(3));
					}
				}
			} else if (bo.getTag().equals("SoUnApprove")) {

				saleorder.setCuruserid(getClientEnvironment().getUser().getPrimaryKey());
				saleorder.processVOForTrans();
				boolean bContinue = true;
				while (bContinue) {

					try {
						// 审批流调整
						Object obj = PfUtilClient.processActionFlow(this, bo.getTag()
								+ getClientEnvironment().getUser().getPrimaryKey(),
								SaleBillType.SaleOrder,
								getClientEnvironment().getDate().toString(), saleorder, null);
						bContinue = false;

						if (obj!=null&& UFBoolean.valueOf(obj.toString()).booleanValue())
							MessageDialog.showHintDlg(this, "弃审提示", "单据号为："
									+ saleorder.getHeadVO().getVreceiptcode()
									+ "的销售订单，已经自动删除下游调入申请/调拨订单");

					} catch (Exception ex) {
						bContinue = doException(saleorder, null, ex);

					}
				}

			}
			// 删除
			else {

				if (bo.getTag().equals("SoBlankout")) {
					saleorder.getParentVO().setStatus(VOStatus.UNCHANGED);
					((SaleorderHVO) saleorder.getParentVO())
							.setVoldreceiptcode(((SaleorderHVO) saleorder.getParentVO())
									.getVreceiptcode());

					// 打标记 否则不触发回写
					saleorder.setFirstTime(true);

					// 插件支持
					getPluginProxy().beforeAction(Action.DELETE, new SaleOrderVO[] { saleorder });

				}

				saleorder.setCuruserid(getClientEnvironment().getUser().getPrimaryKey());

				saleorder.processVOForTrans();

				boolean bContinue = true;
				while (bContinue) {
					try {

						PfUtilClient.processActionNoSendMessage(this, bo.getTag(),
								SaleBillType.SaleOrder,
								getClientEnvironment().getDate().toString(), saleorder, null, null,
								null);
						bContinue = false;

					} catch (Exception ex) {
						bContinue = doException(saleorder, null, ex);

					}
				}

			}

			nc.vo.scm.pub.SCMEnv.out("<====== 销售订单:[" + bo.getName() + "]动作 ## 调用脚本过程共耗时["
					+ (System.currentTimeMillis() - s1) / 1000 + "."
					+ (System.currentTimeMillis() - s1) % 1000 + "秒]==============>");

			if (PfUtilClient.isSuccess()) {

				s1 = System.currentTimeMillis();

				new BillTools().reLoadBillState(this, getClientEnvironment());

				showMessageWhenAction(bo);

				if (strShowState.equals("列表")) { /*-=notranslate=-*/
					if (bo.getTag().equals("OrderFinish")) {
						// 查询现收金额
						UFDouble dtempmny = SaleOrderBO_Client.queryCachPayByOrdId(saleorder
								.getHeadVO().getCsaleid());
						getBillListPanel().getHeadBillModel().setValueAt(dtempmny,
								getBillListPanel().getHeadTable().getSelectedRow(),
								"nreceiptcathmny");
					}
				}
				if (bo.getTag().equals("SoBlankout")) {
					vocache.deleteByID(saleorder.getHeadVO().getCsaleid());
					vIDs.remove(saleorder.getHeadVO().getCsaleid());
					int[] irows = new int[] { getBillListPanel().getHeadTable().getSelectedRow() };
					if (irows[0]==-1)
						irows[0] = this.num;
					this.num--;	
					
					if (strShowState.equals("列表")) {
						getBillListPanel().getHeadBillModel().delLine(irows);
						getBillListPanel().getHeadBillModel().updateValue();

						getBillListPanel().getBodyBillModel().clearBodyData();
						getBillListPanel().getBodyBillModel().updateValue();
					} else {
						getBillCardPanel().addNew();
						getBillCardPanel().setHeadItem("vreceiptcode", null);
						getBillCardPanel().getBillModel().clearBodyData();
						getBillCardPanel().updateValue();

						// //同时刷新列表界面
						if (irows[0] > -1) {
							getBillListPanel().getHeadBillModel().delLine(irows);
							getBillListPanel().getHeadTable().clearSelection();
							getBillListPanel().getHeadBillModel().updateValue();

							getBillListPanel().getBodyBillModel().clearBodyData();
							getBillListPanel().getBodyBillModel().updateValue();
						}

						if (vocache.getCacheSize() == 0)
							getBillListPanel().getHeadBillModel().clearBodyData();
					}

				} else {
					updateCacheVO();
				}

				if (!strShowState.equals("列表")) {
					getBillCardPanel().updateValue();
					nc.ui.scm.pub.panel.SetColor.resetColor(getBillCardPanel());
				}

				nc.vo.scm.pub.SCMEnv.out("<====== 销售订单:[" + bo.getName() + "]动作 ## 调用脚本后刷新界面共耗时["
						+ (System.currentTimeMillis() - s1) / 1000 + "."
						+ (System.currentTimeMillis() - s1) % 1000 + "秒]==============>");

			} else {
				showHintMessage(bo.getName()
						+ NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000339")/*
																								 * @res
																								 * "取消！"
																								 */);
			}
		} catch (BusinessException e) {
			showWarningMessage(e.getMessage() + bo.getName()
					+ NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000134")/*
																							 * @res
																							 * "失败！"
																							 */);
		} catch (Exception e) {
			showWarningMessage(e.getMessage() + " " + bo.getName()
					+ NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000134")/*
																							 * @res
																							 * "失败！"
																							 */);
			nc.vo.scm.pub.SCMEnv.out(e.getMessage());
		}

		nc.vo.scm.pub.SCMEnv.out("<====== 销售订单:[" + bo.getName() + "]动作 ## 共耗时["
				+ (System.currentTimeMillis() - s_init) / 1000 + "."
				+ (System.currentTimeMillis() - s_init) % 1000 + "秒]==============>");

	}

	private void showMessageWhenAction(ButtonObject bo) {
		int hit = 0;
		String msg = "";
		if ("APPROVE".equals(bo.getTag())) {
			if (strShowState.equals("列表"))
				msg = getBillListPanel().getHeadBillModel().getValueAt(
						getBillListPanel().getHeadTable().getSelectedRow(), "fstatus").toString();
			else {
				hit = Integer.parseInt(getHeadItemValue("fstatus").toString());
				msg = nc.vo.so.pub.ConstVO.billStatus[hit - 1];
			}
			showHintMessage(bo.getName() 
					+ NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000566") 
					+ msg + "]");
		} else
			showHintMessage(bo.getName()
					+ NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000132")/*
																							 * @res
																							 * "成功！"
																							 */);
	}

	// 业务动作批处理
	private BatchWorkerProcess batchWorkerProcess = null;

	private BatchWorkerProcess getBatchWorkerProcess() {
		if (batchWorkerProcess == null)
			batchWorkerProcess = new BatchWorkerProcess(this);
		return batchWorkerProcess;
	}

	public void doThreadWork(String WorkName) {
		getBatchWorkerProcess().doThreadWork(WorkName);
	}

	public void informWhenInterrupt(String sActionName) {
		;
	}

	public void informWhenSucceed(String sActionName) {
		;
	}

	/**
	 * 批操作后刷新界面
	 */
	public void reloadDataAfterRun() {
		reLoadListData();
	}

	protected void onApprove(ButtonObject bo) {

		proccdlg = new ProccDlg(this, bo.getHint(), this, bo.getTag());
		// work = new WorkThread(bo.getTag(), this);

		proccdlg.showModal();
		updateUI();

	}

	public int onApprove(SaleOrderVO vo, String tag) throws Exception {

		if (vo == null)
			return 0;

		SaleOrderVO saleorder = vo;

		if (vo.getChildrenVO() != null) {
			for (int i = 0; i < vo.getChildrenVO().length; i++) {
				vo.getChildrenVO()[i].setStatus(VOStatus.UNCHANGED);
			}
		}
		saleorder.setIAction(ISaleOrderAction.A_AUDIT);

		boolean bContinue = true;

		while (bContinue) {

			try {

				onApproveCheck(saleorder);

				nc.vo.scm.pub.session.ClientLink clientLink = new nc.vo.scm.pub.session.ClientLink(
						getClientEnvironment());

				saleorder.setClientLink(clientLink);

				getBillCardTools().getPkarrivecorp(saleorder);

				saleorder.setCuruserid(getClientEnvironment().getUser().getPrimaryKey());
				saleorder.setDcurdate(getClientEnvironment().getDate());
				saleorder.setCusername(getClientEnvironment().getUser().getUserName());
				saleorder.setCcorpname(getClientEnvironment().getCorporation().getUnitname());
				saleorder.setCnodename(NCLangRes.getInstance().getStrByID("40060301",
						"UPP40060301-000504")
				/* @res "訂單管理" */);
				vo.getHeadVO().setAttributeValue("daudittime", ClientEnvironment.getServerTime());

				Object otemp = PfUtilClient.processActionFlow(this, tag, getBillType(),
						getClientEnvironment().getDate().toString(), saleorder, null);

				if (otemp != null) {
					String ErrMsg = otemp.toString();
					if (ErrMsg != null && ErrMsg.startsWith("ERR")) {
						nc.ui.so.pub.ShowToolsInThread.showMessage(proccdlg, ErrMsg.substring(3));
					}
				}

				if (PfUtilClient.isSuccess()) {
					return 1;
				} else {
					return 0;
				}

			} catch (Exception ex) {
				bContinue = doException(saleorder, null, ex);
			}

		}// end while

		return 0;

	}

	protected void onUnApprove(ButtonObject bo) {

		proccdlg = new ProccDlg(this, bo.getHint(), this, bo.getTag());
		// work = new WorkThread(bo.getTag(), this);

		proccdlg.showModal();
		updateUI();

	}

	public int onUnApprove(SaleOrderVO vo, String tag) throws Exception {

		if (vo == null)
			return 0;

		SaleOrderVO saleorder = vo;
		if (vo.getChildrenVO() != null) {
			for (int i = 0; i < vo.getChildrenVO().length; i++) {
				vo.getChildrenVO()[i].setStatus(VOStatus.UNCHANGED);
			}
		}

		saleorder.setIAction(ISaleOrderAction.A_UNAUDIT);

		try {

			onUnApproveCheck(saleorder);

			ClientLink clientLink = new ClientLink(getClientEnvironment());
			saleorder.setClientLink(clientLink);
			saleorder.setCuruserid(getClientEnvironment().getUser().getPrimaryKey());
			saleorder.setDcurdate(getClientEnvironment().getDate());
			saleorder.setCusername(getClientEnvironment().getUser().getUserName());
			saleorder.setCcorpname(getClientEnvironment().getCorporation().getUnitname());
			saleorder.setCnodename(NCLangRes.getInstance().getStrByID("40060301",
					"UPP40060301-000504")
			/* @res "訂單管理" */);

			PfUtilClient.processAction(tag, SaleBillType.SaleOrder, getClientEnvironment()
					.getDate().toString(), saleorder);

			if (PfUtilClient.isSuccess()) {
				return 1;
			} else {
				return 0;
			}
		} catch (Exception e) {
			throw e;
		}

	}

	protected void onBlankout(ButtonObject bo) {
		if (getBillListPanel().isTempVO()) {
			deleteTempData();
			return;
		}

		proccdlg = new ProccDlg(this, bo.getHint(), this, bo.getTag());
		// work = new WorkThread(bo.getTag(), this);

		proccdlg.showModal();
		updateUI();

		showHintMessage(bo.getName()
				+ NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000132")/*
																						 * @res
																						 * "成功！"
																						 */);

	}

	public int onBlankout(SaleOrderVO vo, String tag) throws Exception {
		if (getBillListPanel().isTempVO()) {
			deleteTempData();
			return 0;
		}

		if (vo == null)
			return 0;

		SaleOrderVO saleorder = vo;
		if (vo.getChildrenVO() != null) {
			for (int i = 0; i < vo.getChildrenVO().length; i++) {
				vo.getChildrenVO()[i].setStatus(VOStatus.UNCHANGED);
			}
		}

		// 设置动作 为回写预订单做准备
		saleorder.setIAction(ISaleOrderAction.A_BLANKOUT);

		try {

			((SaleorderHVO) saleorder.getParentVO()).setVoldreceiptcode(((SaleorderHVO) saleorder
					.getParentVO()).getVreceiptcode());

			saleorder.setCuruserid(getClientEnvironment().getUser().getPrimaryKey());

			saleorder.processVOForTrans();

			PfUtilClient.processActionNoSendMessage(this, tag, SaleBillType.SaleOrder,
					getClientEnvironment().getDate().toString(), saleorder, null, null, null);

			if (PfUtilClient.isSuccess()) {
				return 1;
			} else {
				return 0;
			}
		} catch (Exception e) {
			throw e;
		}

	}

	/**
	 * 检测审批单据合法性。
	 * 
	 * 创建日期：(2001-8-8 10:09:01)
	 * 
	 * @return boolean
	 */
	private void onUnApproveCheck(SaleOrderVO saleorder) throws nc.vo.pub.ValidationException {
		if (saleorder.getHeadVO().getAttributeValue("bcooptopo") != null
				&& ((UFBoolean) saleorder.getHeadVO().getAttributeValue("bcooptopo"))
						.booleanValue()) {
			throw new ValidationException("该订单已经协同生成采购订单无法弃审。");
		}

		SaleorderBVO[] oldbodyVOs = saleorder.getBodyVOs();
		// 参数检测
		for (int i = 0; i < oldbodyVOs.length; i++) {

			if (oldbodyVOs[i].getNtotalinventorynumber() != null
					&& oldbodyVOs[i].getNtotalinventorynumber().doubleValue() != 0)
				throw new ValidationException(NCLangRes.getInstance().getStrByID("40060301",
						"UPP40060301-000294")/*
												 * @res "当前单据已出库，不能弃审!"
												 */);

			if (oldbodyVOs[i].getBifinvoicefinish() != null
					&& oldbodyVOs[i].getBifinvoicefinish().booleanValue())
				throw new ValidationException(NCLangRes.getInstance().getStrByID("40060301",
						"UPP40060301-000419")/*
												 * @res "当前单据存在开票结束的行，不能弃审!"
												 */);

			if (oldbodyVOs[i].getNarrangescornum() != null
					&& oldbodyVOs[i].getNarrangescornum().doubleValue() != 0)
				throw new ValidationException(NCLangRes.getInstance().getStrByID("sopub",
						"UPPsopub-000303")/*
											 * @res "已经部分(全部)生成委外订单,不能取消"
											 */);

			if (oldbodyVOs[i].getNarrangemonum() != null
					&& oldbodyVOs[i].getNarrangemonum().doubleValue() != 0)
				throw new ValidationException(NCLangRes.getInstance().getStrByID("sopub",
						"UPPsopub-000304")/*
											 * @res "已经部分(全部)生成生产订单,不能取消"
											 */);

			if (oldbodyVOs[i].getNarrangepoapplynum() != null
					&& oldbodyVOs[i].getNarrangepoapplynum().doubleValue() != 0)
				throw new ValidationException(NCLangRes.getInstance().getStrByID("sopub",
						"UPPsopub-000322")/*
											 * @res "已经部分(全部)生成请购单,不能取消"
											 */);

			/*
			 * if (oldbodyVOs[i].getNarrangetoornum() != null &&
			 * oldbodyVOs[i].getNarrangetoornum().doubleValue() != 0) throw new
			 * ValidationException(NCLangRes.getInstance().getStrByID("sopub",
			 * "UPPsopub-000036") @res "已经部分(全部)生成调拨审请单或调拨订单,不能取消" );
			 */

			// /** 修订过的订单不允许弃审 V502 yangchunlin jdm zhongwei* */
			// if (oldbodyVOs[i].getBeditflag() != null &&
			// oldbodyVOs[i].getBeditflag().booleanValue()) {
			// throw new
			// ValidationException(NCLangRes.getInstance().getStrByID("40060301",
			// "UPP40060301-000545")/*
			// * @res
			// * "单据进行过修订，不允许弃审"
			// */);
			// }
		}// end for

		boCancelAudit.setEnabled(false);
		updateButton(boCancelAudit);
		nc.vo.scm.pub.SCMEnv.out("弃审检测通过1");
	}

	/**
	 * 异常处理方法
	 * 
	 * @param vo
	 * @param oldvo
	 * @param e
	 *            只处理已知的业务异常，否则会抛出异常
	 * @return 是否处理完毕，或者是否正常进行
	 * @throws Exception
	 * 
	 * @comment 异常的前台校验和处理，必须符合业务本身的要求
	 */
	protected boolean doException(SaleOrderVO vo, SaleOrderVO oldvo, Exception e) throws Exception {
		e = nc.vo.so.pub.ExceptionUtils.marshException(e);

		// 如果不通过,抛异常
		if (e instanceof ATPNotEnoughException && ((ATPNotEnoughException) e).getHint() == null) {
			throw e;
		}

		/** 平台异常：可能是单据动作检查函数抛出 v5.5 */
		if (e instanceof nc.vo.uap.pf.PFBusinessException) {
			// 错误信息中行号的所在位置
			String rowNo_s = "行号为";
			int rowStringIndex = e.getMessage().indexOf(rowNo_s);

			// 错误信息行号
			String rowNo = e.getMessage().substring(rowStringIndex + rowNo_s.length(),
					rowStringIndex + rowNo_s.length() + 2);

			ArrayList<Integer> rowList = new ArrayList<Integer>();
			if (!strShowState.equals("列表")) {
				for (int row = 0; row < getBillCardPanel().getRowCount(); row++) {
					if (getBillCardTools().getBodyStringValue(row, "crowno").equals(rowNo))
						rowList.add(new Integer(row));
				}
				nc.ui.scm.pub.panel.SetColor.setErrRowColor(getBillCardPanel(), rowList);
			} else {
				for (int row = 0; row < getBillListPanel().getBodyTable().getRowCount(); row++) {
					if (getBillListPanel().getBodyBillModel().getValueAt(row, "crowno").equals(
							rowNo))
						rowList.add(new Integer(row));
				}
				nc.ui.scm.pub.panel.SetColor.setErrRowColor(getBillListPanel().getBodyTable(),
						rowList);
			}
		}
		/** 平台异常：可能是单据动作检查函数抛出 v5.5 */

		if (!(e instanceof ATPNotEnoughException || e instanceof CreditNotEnoughException
				|| e instanceof PeriodNotEnoughException || e instanceof nc.vo.scm.pub.SaveHintException))
			throw e;

		String sMsg = e.getMessage();

		sMsg += NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000230");/*
																						 * @res "
																						 * ,是否继续？"
																						 */
		if (showYesNoMessage(sMsg) != MessageDialog.ID_YES)
			return false;

		if (e instanceof ATPNotEnoughException) {
			vo.setBCheckATP(false);
			if (oldvo != null)
				oldvo.setBCheckATP(false);
		} else if (e instanceof CreditNotEnoughException) {
			vo.setCheckCredit(false);
			if (oldvo != null)
				oldvo.setCheckCredit(false);
		} else if (e instanceof PeriodNotEnoughException) {
			vo.setCheckPeriod(false);
			if (oldvo != null)
				oldvo.setCheckPeriod(false);
		} else if (e instanceof nc.vo.scm.pub.SaveHintException) {
			vo.setFirstTime(false);
			if (oldvo != null)
				oldvo.setFirstTime(false);
		}

		return true;
	}

	/**
	 * 后续动作。
	 * 
	 * 创建日期：(2001-6-1 13:12:36)
	 * 
	 */
	protected void onAfterAction(ButtonObject bo) {
		try {
			PfUtilClient.processAction(bo.getTag(), getBillType(), getClientEnvironment().getDate()
					.toString(), getVO(false));
			showHintMessage(bo.getName()
					+ NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000132")/*
																							 * @res
																							 * "成功！"
																							 */);
		} catch (Exception e) {
			showWarningMessage(bo.getName()
					+ NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000134")/*
																							 * @res
																							 * "失败！"
																							 */);
			// e.printStackTrace();
		}
	}

	/**
	 * 辅助执行。
	 * 
	 * 创建日期：(2001-6-1 13:12:36)
	 * 
	 */
	protected void onAssistant(ButtonObject bo) {

		if (strShowState.equals("列表")) {
			num = getBillListPanel().getHeadTable().getSelectedRow();

			if (num == -1)
				return;
		}

		// 发货、补货、直运安排
		if (bo.getTag().equals(SENDINV)) {
			onBoSendInv();
		} else if (bo.getTag().equals(SUPPLYINV)) {
			onBoSupplyDirectInv(bo);
		} else if (bo.getTag().equals(DIRECTINV)) {
			onBoSupplyDirectInv(bo);
		} else {
			try {
				PfUtilClient.processActionNoSendMessage(this, bo.getTag(), getBillType(),
						getClientEnvironment().getDate().toString(), getVO(false),
						getAssistantPara(bo), null, null);
			} catch (Exception e) {
				showErrorMessage(e.getMessage());
			}
		}

		// 辅助功能执行后刷新界面
		//BillTools.reLoadBillState(this, getClientEnvironment());
		
		// 刷新缓存TS
		refreshVOcacheTS();
	}

	private void refreshVOcacheTS() {
		SaleOrderVO svo = vocache.getSaleOrderVO(getHeadItemValue("csaleid").toString());
		if (svo != null) {
			svo.getHeadVO().setTs(SmartVODataUtils.getUFDateTime(getHeadItemValue("ts")));
			for (int i = 0; i < svo.getBodyVOs().length; i++) {
				SaleorderBVO bvo = svo.getBodyVOs()[i];
				bvo.setTs(SmartVODataUtils.getUFDateTime(getBillCardTools().getBodyValue(i, "ts")));
			}
		}
	}

	/**
	 * 发货安排
	 */
	private void onBoSendInv() {
		try {
			// 1 发货关闭、红字订单、劳务折扣行，不可做发货单
			ArrayList<SaleorderBVO> list = new ArrayList<SaleorderBVO>();
			SaleOrderVO svo = (SaleOrderVO) getVo();
			SaleorderBVO[] bvos = svo.getBodyVOs();

			boolean bsendendflag = false;
			boolean bretractflag = false;
			boolean bLaborDiscountflag = false;
			for (int i = 0; i < bvos.length; i++) {
				bLaborDiscountflag = bvos[i].getLaborflag().booleanValue()
						|| bvos[i].getDiscountflag().booleanValue();
				bsendendflag = bvos[i].getBifreceivefinish().booleanValue();
				bretractflag = bLaborDiscountflag
						|| bvos[i].getNnumber().compareTo(new UFDouble(0)) < 0 ? true : false;
				if (!bsendendflag
						&& !bretractflag
						&& (bvos[i].getBdericttrans() == null || !bvos[i].getBdericttrans()
								.booleanValue()) && !bLaborDiscountflag) {
					list.add(bvos[i]);
				}
			}
			if (list.size() == 0) {
				throw new BusinessException("发货关闭、红字订单、劳务折扣行，不能进行发货安排！");
			}

			SaleorderBVO[] newItems = new SaleorderBVO[list.size()];
			newItems = (SaleorderBVO[]) list.toArray(newItems);

			SaleOrderVO newVO = new SaleOrderVO();
			newVO.setParentVO(svo.getParentVO());
			newVO.setChildrenVO(newItems);

			// VO对照
			nc.vo.so.soreceive.SaleReceiveVO sendVO;

			sendVO = (nc.vo.so.soreceive.SaleReceiveVO) PfChangeBO_Client.pfChangeBillToBill(newVO,
					"30", "4331");

			PfLinkData linkData = new PfLinkData();
			linkData.setUserObject(sendVO);

			SFClientUtil.openLinkedADDDialog("40060401", this, linkData);

		} catch (BusinessException e) {
			showWarningMessage(e.getMessage());
		}
	}

	/**
	 * 补货直运安排
	 */
	private void onBoSupplyDirectInv(ButtonObject bo) {
		try {
			// 1.补货直运关闭，不可做补货直运
			ArrayList<SaleorderBVO> list = new ArrayList<SaleorderBVO>();
			SaleOrderVO svo = (SaleOrderVO) getVo();
			SaleorderBVO[] bvos = svo.getBodyVOs();
			for (int i = 0; i < bvos.length; i++) {
				boolean bsendendflag = bvos[i].getBarrangedflag().booleanValue();
				if (!bsendendflag
						&& (!bvos[i].getLaborflag().booleanValue() && !bvos[i].getDiscountflag().booleanValue())) 
						//&& bvos[i].getNnumber().doubleValue() > 0) 
				{
					list.add(bvos[i]);
				}
			}
			if (list.size() == 0) {
				throw new BusinessException("表体行已补货直运安排关闭，不能进行补货直运安排！");
			}

			SaleorderBVO[] newItems = new SaleorderBVO[list.size()];
			newItems = (SaleorderBVO[]) list.toArray(newItems);

			SaleOrderVO newVO = new SaleOrderVO();
			newVO.setParentVO(svo.getParentVO());
			newVO.setChildrenVO(newItems);

			// 过滤补货直运表体VO
			SaleOrderVO realVo = null;
			ISourceRedunVO sourceRedunVO = null;
			//加上是否直运业务类型标记
			boolean ifZ = getBillCardTools().isZVerifyRule(newVO.getHeadVO().getCbiztype());
			newVO.getHeadVO().setVerifyrule(ifZ?"Z":"N");
			if (bo.getTag().equals(SUPPLYINV)) {
				sourceRedunVO = new ISourceRedunVO(null, ISourceRedunVO.INDIRECT);
				realVo = ((SaleOrderVO) newVO).getBillRedunVO(sourceRedunVO);
				;
			} else if (bo.getTag().equals(DIRECTINV)) {
				sourceRedunVO = new ISourceRedunVO(null, ISourceRedunVO.DIRECT);				
				realVo = ((SaleOrderVO) newVO).getBillRedunVO(sourceRedunVO);
				;
			}

			// 2.初始化补货直运界面
			if(getBillToBillUI() != null)//liuzwc 换行补货直运需要
				billToBillUI = null;
			if (getBillToBillUI(realVo).getMainCtrl() != null) {
				if (getCurShowPanel() == null)
					remove(getBillListPanel());
				else
					remove(getCurShowPanel());

				add(getBillToBillUI(realVo), "Center");
				setTitleText(getBillToBillUI().getTitle());
				getBillToBillUI().setVisible(true);
				ButtonObject[] btns = getBillToBillUI().setButtenForUse("30");
				setButtons(btns, "40060402");
				updateButtons();
				updateUI();
			} else
				showWarningMessage("补货直运涉及采购、内部交易、生产制造，请检查相关产品安装情况！");

		} catch (BusinessException e) {
			showWarningMessage(e.getMessage());
		}
	}

	/**
	 * 由补货直运UI返回销售订单UI
	 */
	public void returnToMainUI() {

		// 重新初始化销售订单UI
		remove(getBillToBillUI());
		if (getCurShowPanel() == null)
			add(getBillListPanel(), "Center");
		else
			add(getCurShowPanel(), "Center");
		setTitleText(getTitle());
		setButtons();
		setButtonsState();

		// 刷新界面和缓存
		new BillTools().reLoadBillState(this, getClientEnvironment());
		updateCacheVO();
		if (!strShowState.equals("列表"))
			getBillCardPanel().updateValue();

		updateUI();
	}

	/**
	 * 每次产生一个新的实例
	 * 
	 * @param newVO
	 * @return
	 * 修改：liuzwc 209-09-15  修改连接数问题：补货直运安排（onBoSupplyDirectInv（））方法中两次调用此方法，
	 * 两次都new了BillToBillUI，为解决连接数重新if语句。
	 */
	public BillToBillUI getBillToBillUI(SaleOrderVO newVO) {
		if (billToBillUI == null)
		billToBillUI = new BillToBillUI(this, "30", new SaleOrderVO[] { newVO });
		return billToBillUI;
	}

	public BillToBillUI getBillToBillUI() {
		return billToBillUI;
	}

	/**
	 * 销售订单管理增加单据复制功能。(复制单据视同与自制新增)
	 * 
	 * 功能描述:单据复制功能菜单事件函数
	 * 
	 * 输入参数:无
	 * 
	 * 返回值:无
	 * 
	 * 异常处理:无
	 * 
	 * 日期:2003-08-25
	 * 
	 */
	protected void onCopyBill() throws Exception {
		
		//设置是否询价
		getBillCardPanel().setBFindPrice(true);

	    //创建时间工具类实例
		nc.vo.scm.pu.Timer timer = new nc.vo.scm.pu.Timer();
		timer.start();
		
		// 在当前业务类型的单据来源不存在自制单据时，不能进行复制
		// 复制的单据均为自制单据。在此进行判断
		PfUtilClient.retAddBtn(boAdd, getCorpPrimaryKey(), getBillType(), boBusiType);
		int ccount = boAdd.getChildCount();
		ButtonObject[] bos = boAdd.getChildButtonGroup();
		boolean bCanCopyFlag = false;
		for (int i = 0; i < ccount; i++) {
			if (bos[i].getTag().startsWith(SELFBILL)) {
				bCanCopyFlag = true;
				break;
			}
		}

		if (!bCanCopyFlag) {
			showWarningMessage(NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000289")/*
																									 * @res
																									 * "当前的业务类型无自制单据，不能复制单据"
																									 */);
			return;
		}
		if (strShowState.equals("列表")) {
			selectRow = getBillListPanel().getHeadTable().getSelectedRow();
			num = selectRow;
			getBillListPanel().getHeadTable().getSelectionModel().setSelectionInterval(selectRow,
					selectRow);
			onCard();
		}
		if (getBillCardPanel().getHeadItem("bretinvflag") != null
				&& getBillCardPanel().getHeadItem("bretinvflag").getValueObject() != null
				&& (new UFBoolean(getBillCardPanel().getHeadItem("bretinvflag").getValue()))
						.booleanValue()) {
			showWarningMessage(NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000290")/*
																									 * @res
																									 * "不能复制退货订单单据"
																									 */);
			return;

		}
		timer.addExecutePhase(">>>>>>>>>销售订单[复制订单--复制条件检查]用时：>>>>>>>>>>>");

		// head null items
		String[] headitems = new String[] { "bretinvflag", "binvoicendflag", "boutendflag",
				"breceiptendflag", "bpayendflag", "btransendflag", "boverdate", "capproveid",
				"dapprovedate", "vreceiptcode", "csaleid", "veditreason", "nreceiptcathmny",
				"boverdate", "iprintcount", "dbilltime", "daudittime", "dmoditime", "editionnum",
				"editdate", "editauthor" };
		for (int i = 0; i < headitems.length; i++) {
			if (getBillCardPanel().getHeadItem(headitems[i]) != null)
				getBillCardPanel().getHeadItem(headitems[i]).setValue(null);
		}
		String[] tails = new String[] { "capproveid", "dapprovedate", "iprintcount", "dbilltime",
				"daudittime", "dmoditime" };
		for (int i = 0; i < tails.length; i++) {
			if (getBillCardPanel().getTailItem(tails[i]) != null)
				getBillCardPanel().getTailItem(tails[i]).setValue(null);
		}

		if (getBillCardPanel().getHeadItem("bcodechanged") != null)
			getBillCardPanel().getHeadItem("bcodechanged").setValue(new UFBoolean(false));
		if (getBillCardPanel().getHeadItem("fstatus") != null)
			getBillCardPanel().getHeadItem("fstatus").setValue(new Integer(BillStatus.FREE));
		if (getBillCardPanel().getHeadItem("dbilldate") != null)
			getBillCardPanel().getHeadItem("dbilldate").setValue(getClientEnvironment().getDate());

		if (getBillCardPanel().getHeadItem("pk_corp") != null)
			getBillCardPanel().getHeadItem("pk_corp").setValue(
					getClientEnvironment().getCorporation().getPk_corp());

		// n30add
		String[] keys = getNeedSetNullItemsWhenCopy();

		String[] keys1 = getBillCardPanel().getNeedSetNullSourceItems();

		getBillCardTools().setHeadRefLimit(strState);

		// 以新增单据的方式加入复制单据
		getBillCardPanel().setEnabled(true);
		timer.addExecutePhase(">>>>>>>>>销售订单[复制订单--复制前字段编辑性准备]用时：>>>>>>>>>>>");

		// 订单号可编辑
		vRowATPStatus = new Vector();
		if (getBillCardPanel().alInvs == null) {
			initInvList();
		}
		timer.addExecutePhase(">>>>>>>>>销售订单[复制订单--自由项准备]用时：>>>>>>>>>>>");

		// 非直运流程复制直运流程单子时，清空发货仓库(直运仓库) liuzwc 2009-06-08
		Boolean ZtoNotZ = false; // 是否直运->非直运
		String oldCbiztyp = getBillCardPanel().getHeadItem("cbiztype").getValueCache()==null ? null 
				: getBillCardPanel().getHeadItem("cbiztype").getValueCache().toString();
		String newCbiztype = getBillCardPanel().getBusiType();
		if (oldCbiztyp!=null && !oldCbiztyp.equals(newCbiztype) 
				&& getBillCardTools().isZVerifyRule(oldCbiztyp)
				&& !getBillCardTools().isZVerifyRule(newCbiztype)) {
			ZtoNotZ = true;
		}
		timer.addExecutePhase(">>>>>>>>>销售订单[复制订单-- 非直运流程复制直运流程判断]用时：>>>>>>>>>>>");
		
		UFBoolean wholemanaflag = null, isDiscount = null, isLabor = null;
		String pk_stordoc = null;
		for (int i = 0; i < getBillCardPanel().getRowCount(); i++) {
			for (int j = 0; j < keys.length; j++) {
				getBillCardPanel().setBodyValueAt(null, i, keys[j]);
			}
			for (int j = 0; j < keys1.length; j++) {
				getBillCardPanel().setBodyValueAt(null, i, keys1[j]);
			}
			// 新增：非直运流程复制直运流程单子时，清空发货仓库(直运仓库) liuzwc 2009-06-08
			pk_stordoc = getBillCardTools().getBodyStringValue(i, "cbodywarehouseid");
			if (ZtoNotZ && pk_stordoc!=null && "Y".equals(getBillCardTools().isDirectStore(pk_stordoc))) {
				getBillCardPanel().setBodyValueAt(null, i, "cbodywarehouseid");
				getBillCardPanel().setBodyValueAt(null, i, "cbodywarehousename");
			}

			getBillCardPanel().setBodyValueAt(null, i, "csaleid");

			vRowATPStatus.addElement(new UFBoolean(false));
			getBillCardPanel().getBillModel().setRowState(i, BillModel.ADD);

			// 控制多公司行的编辑状态
			ctlUIOnCconsignCorpChg(i);

			// 表体
			getBillCardPanel().setAssistUnit(i);
			getBillCardPanel().setScaleEditableByRow(i);

			// 批次
			wholemanaflag = getBillCardTools().getBodyUFBooleanValue(i, "wholemanaflag");
			getBillCardPanel().setCellEditable(i, "fbatchstatus",
					wholemanaflag == null ? false : wholemanaflag.booleanValue());
			getBillCardPanel().setCellEditable(i, "cbatchid",
					wholemanaflag == null ? false : wholemanaflag.booleanValue());

			isDiscount = getBillCardTools().getBodyUFBooleanValue(i, "discountflag");
			isLabor = getBillCardTools().getBodyUFBooleanValue(i, "laborflag");

			if ((isDiscount != null && isDiscount.booleanValue())
					|| (isLabor != null && isLabor.booleanValue())) {

				getBillCardTools().setBodyCellsEdit(
						new String[] { "cconsigncorp", "creccalbody", "crecwarehouse",
								"bdericttrans", "boosflag", "bsupplyflag" }, i, false);
			}
		}
		timer.addExecutePhase(">>>>>>>>>销售订单[复制订单--循环设置表体行的可编辑性]用时：>>>>>>>>>>>");
		
		setDefaultData(false);
		timer.addExecutePhase(">>>>>>>>>销售订单[复制订单--设置默认数据]用时：>>>>>>>>>>>");
		
		// 如果部门被清空，则恢复到原值
		if (getBillCardPanel().getHeadItem("cdeptid").getValueObject() == null) {
			getBillCardPanel().getHeadItem("cdeptid").resumeValue();
		}

		// 表体收货地址恢复原值
		// 如果表体已经被排序过，可能会有问题
		SaleOrderVO saleorder = vocache.getSaleOrderVO(getBillCardPanel().getHeadItem("csaleid")
				.getValueCache().toString());
		SaleorderBVO[] items = saleorder.getBodyVOs();
		for (int i = 0, len = getBillCardPanel().getRowCount(); i < len; i++) {
			getBillCardPanel().setBodyValueAt(items[i].getVreceiveaddress(), i, "vreceiveaddress");
		}

		// 恢复模板项的初始编辑状态
		getBillCardTools().resumeBillItemEditToInit();
		timer.addExecutePhase(">>>>>>>>>销售订单[复制订单-- 恢复模板项的初始编辑状态]用时：>>>>>>>>>>>");
		
		// ctlCurrencyEdit();
		// 如果发现汇率有变化，则触发汇率事件，重新计算
		Object oldvalue = getBillCardPanel().getHeadItem("nexchangeotobrate").getValueCache();
		Object newvalue = getBillCardPanel().getHeadItem("nexchangeotobrate").getValueObject();
		if (((oldvalue != null) && !oldvalue.equals(newvalue))
				|| (oldvalue == null && newvalue != null)) {
			getBillCardPanel().afterChangeotobrateEdit(null);
			//getBillCardPanel().freshBodyLargess(2);
		}

		if (((oldvalue != null) && !oldvalue.equals(newvalue))
				|| (oldvalue == null && newvalue != null)) {
			//getBillCardPanel().freshBodyLargess(2);
		}
		// 如果发现汇率有变化，则触发汇率事件，重新计算
		timer.addExecutePhase(">>>>>>>>>销售订单[复制订单--重新计算汇率]用时：>>>>>>>>>>>");
		
		String formulas[] = {
		// 客户基本档案
				"ccustbasid->getColValue(bd_cumandoc,pk_cubasdoc,pk_cumandoc,ccustomerid)",
				// 散户标志
				"bfreecustflag->getColValue(bd_cubasdoc,freecustflag,pk_cubasdoc,ccustbasid)" };
		getBillCardPanel().execHeadFormulas(formulas);

		// 重新计算表头价税合计（表体价税合计有可能被重新计算，需要同步更新）
		getBillCardPanel().getHeadItem("nheadsummny").setValue(
				getBillCardPanel().getTotalValue("noriginalcursummny"));
		timer.addExecutePhase(">>>>>>>>>销售订单[复制订单--重新计算表头价税合计]用时：>>>>>>>>>>>");

		// 加自由项变色龙。
		InvVO[] invvos = new InvVO[getBillCardPanel().getRowCount()];
		for (int i = 0; i < invvos.length; i++) {
			invvos[i] = new InvVO();
			invvos[i].setCinventoryid((String) getBillCardPanel().getBodyValueAt(i, "cinventoryid"));
		}
		InvoInfoBYFormula invvosget = new InvoInfoBYFormula();
		invvos = invvosget.getQuryInvVOs(invvos, false, true, 1);

		ArrayList al_invs = new ArrayList();
		for (InvVO invvo : invvos) {
			al_invs.add(invvo);
		}
		InvAttrCellRenderer ficr = new InvAttrCellRenderer();
		ficr.setFreeItemRenderer(getBillCardPanel(), al_invs);
		timer.addExecutePhase(">>>>>>>>>销售订单[复制订单--设置自由项]用时：>>>>>>>>>>>");
		
		updateUI();

		strState = "新增"; /*-=notranslate=-*/
		setButtonsState();

		if (saleorder.isCoopped()) {
			// 协同时清空相关数据
			setCoopDefaultDataForCopy();
		}
		
		// 尝试关联合同：新增默认合同项
		getBillCardPanel().setDefaultCtItem(0, getBillCardPanel().getBillTable().getRowCount(),true);
		timer.addExecutePhase(">>>>>>>>>销售订单[复制订单--尝试关联合同]用时：>>>>>>>>>>>");
		
		// 销售订单复制后设置各字段可编辑性
		getBillCardPanel().setEditEnabledForCopy();

		//输出各个业务操作占用时间分布
		timer.showAllExecutePhase(">>>>>>>>>销售订单[复制订单]结束：>>>>>>>>>>>");
	}

	/**
	 * 复制时协同相关字段清空
	 * 
	 * @throws Exception
	 */
	private void setCoopDefaultDataForCopy() throws Exception {
		getBillCardPanel().setHeadItem("bcooptopo", new UFBoolean(false));
		getBillCardPanel().setHeadItem("bpocooptome", new UFBoolean(false));
		getBillCardPanel().setHeadItem("ccooppohid", null);

	}

	protected void onAuditFlowStatus() {
		SaleOrderVO hvo = null;
		hvo = (SaleOrderVO) getVO(false);
		if (hvo == null || hvo.getParentVO() == null) {
			showErrorMessage(NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000199")/*
																									 * @res
																									 * "请选择单据"
																									 */);
		} else {
			SaleorderHVO header = (SaleorderHVO) hvo.getParentVO();
			String pk = header.getCsaleid();
			String biztype = header.getCbiztype();

			if (pk == null) {
				showErrorMessage(NCLangRes.getInstance().getStrByID("SCMCOMMON",
						"UPPSCMCommon-000067")/*
												 * @res "单据号为空"
												 */);
			} else {
				nc.ui.pub.workflownote.FlowStateDlg approvestatedlg = new nc.ui.pub.workflownote.FlowStateDlg(
						this, "30", biztype, pk);
				approvestatedlg.showModal();
			}
		}
	}

	/**
	 * 退货保存
	 * 
	 */
	protected void onBatch() {
		long s = System.currentTimeMillis();
		
		getBillCardPanel().cleanNullLine();
		
		SaleOrderVO saleorder = (SaleOrderVO) getBillCardPanel().getBillValueVO(
				SaleOrderVO.class.getName(), SaleorderHVO.class.getName(),
				SaleorderBVO.class.getName());
		try {
			onCheck(saleorder);
			showHintMessage(NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000278")/*
																								 * @res
																								 * "开始退货...."
																								 */);

			// 设置表头表体状态
			saleorder.setStatus(nc.vo.pub.VOStatus.NEW);
			saleorder.getParentVO().setStatus(nc.vo.pub.VOStatus.NEW);

			// 设置更新VO动作
			saleorder.setIAction(ISaleOrderAction.A_ADD);

			saleorder.setDcurdate(getClientEnvironment().getDate());
			saleorder.setCuruserid(getClientEnvironment().getUser().getPrimaryKey());

			// 收货地址
			UIRefPane vreceiveaddress = (UIRefPane) getBillCardPanel().getHeadItem(
					"vreceiveaddress").getComponent();

			saleorder.getHeadVO().setVreceiveaddress(vreceiveaddress.getUITextField().getText());

			for (int i = 0; i < saleorder.getChildrenVO().length; i++) {
				saleorder.getChildrenVO()[i].setStatus(nc.vo.pub.VOStatus.NEW);
			}

			// 处理预收款比例和预收款金额
			if (calculatePreceive(saleorder) == false)
				return;

			java.util.ArrayList alistID = null;

			boolean bContinue = true;
			boolean bRight = true;
			while (bContinue) {
				try {
					saleorder.processVOForTrans();

					UFDateTime ud = ClientEnvironment.getServerTime();
					saleorder.getHeadVO().setAttributeValue("dbilltime", ud);

					alistID = (java.util.ArrayList) PfUtilClient.processActionNoSendMessage(this,
							"PreKeep", getBillType(), getClientEnvironment().getDate().toString(),
							saleorder, null, null, null);

					bContinue = false;
					bRight = true;
				} catch (Exception ex) {
					bContinue = doException(saleorder, null, ex);
					bRight = false;
				}
			}

			if (!bRight) {
				showHintMessage(NCLangRes.getInstance()
						.getStrByID("40060301", "UPP40060301-000185")/*
																		 * @res
																		 * "保存失败！"
																		 */);
				return;
			}

			vIDs.add((String) alistID.get(0));
			this.id = (String) alistID.get(0);
			num = vIDs.size() - 1;
			showHintMessage(NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000279",
					null, new String[] { (System.currentTimeMillis() - s) / 1000 + "" }));

			// 重新加载数据
			loadCardData();

			strState = "自由";

			getBillCardTools().setHeadRefLimit(strState);

			setCardButtonsState();

			// 加自由项变色龙。
			InvAttrCellRenderer ficr = new InvAttrCellRenderer();
			ficr.setFreeItemRenderer(getBillCardPanel(), null);

			// 恢复正常背景色
			nc.ui.scm.pub.panel.SetColor.resetColor(getBillCardPanel());

		} catch (ValidationException e) {
			nc.ui.scm.pub.panel.SetColor.setErrRowColor(getBillCardPanel(), rowList);
			showErrorMessage(e.getMessage()
					+ NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000280")/*
																							 * @res
																							 * "退货失败！"
																							 */);
		} catch (Exception e) {
			showWarningMessage(e.getMessage());
			showHintMessage(e.getMessage()
					+ NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000280")/*
																							 * @res
																							 * "退货失败！"
																							 */);
			// nc.vo.scm.pub.SCMEnv.out(e.getMessage());
		}
	}

	/**
	 * 设置卡片按钮状态。 创建日期：(2001-3-17 9:00:09)
	 */
	private void setCardButtonsState() {
		boFirst.setEnabled(true);
		boLast.setEnabled(true);
		getBoOnHandShowHidden().setEnabled(true);

		String csaleid = (String) getBillCardPanel().getHeadItem("csaleid").getValueObject();

		// 没有单据加载的浏览状态
		if ((csaleid == null || csaleid.length() == 0) && (strState.equals("自由"))) {
			boBusiType.setEnabled(true);
			boAdd.setEnabled(true);
			boSave.setEnabled(false);
			boMaintain.setEnabled(false);
			boLine.setEnabled(false);
			boAudit.setEnabled(false);
			boAction.setEnabled(false);
			boQuery.setEnabled(true);
			boBrowse.setEnabled(false);
			boReturn.setEnabled(true);
			boPrntMgr.setEnabled(false);
			boAssistant.setEnabled(true);
			boAsstntQry.setEnabled(false);

			boCoRefPo.setEnabled(true);
		} else if (strState.equals("自由")) { /*-=notranslate=-*/
			boBusiType.setEnabled(true);
			int ccount = boAdd.getChildCount();
			ButtonObject[] bos = boAdd.getChildButtonGroup();
			boolean bCanCopyFlag = false;
			for (int i = 0; i < ccount; i++) {
				if (bos[i].getTag().startsWith(SELFBILL)) {
					bCanCopyFlag = true;
					break;
				}
			}
			boAdd.setEnabled(true);
			boBatch.setEnabled(false);
			boEdit.setEnabled(true);
			boCancel.setEnabled(false);
			boSave.setEnabled(false);
			boMaintain.setEnabled(true);
			boBlankOut.setEnabled(true);
			setLineButtonStatus(false);
			boAudit.setEnabled(false);
			boClose.setEnabled(true);
			boFreeze.setEnabled(false);
			boDocument.setEnabled(true);
			boOrderQuery.setEnabled(true);
			boReturn.setEnabled(true);
			boAction.setEnabled(true);
			boPrntMgr.setEnabled(true);
			boAssistant.setEnabled(true);
			boAsstntQry.setEnabled(true);
			boQuery.setEnabled(true);
			boBrowse.setEnabled(true);
			boRefundment.setEnabled(false);
			boAfterAction.setEnabled(true);
			boBom.setEnabled(false);
			boPrint.setEnabled(true);
			boPreview.setEnabled(true);
			boSplitPrint.setEnabled(true);
			boPre.setEnabled(true);
			boNext.setEnabled(true);

			boSendAudit.setEnabled(true);
			boAuditFlowStatus.setEnabled(true);
			boCoRefPo.setEnabled(true);

			if (num == 0)
				boPre.setEnabled(false);
			if (num == vIDs.size() - 1)
				boNext.setEnabled(false);

			boCopyBill.setEnabled(bCanCopyFlag);

			getBillCardPanel().setEnabled(false);

			// 单据状态
			int iStatus = (Integer) getBillCardPanel().getHeadItem("fstatus").getValueObject();
			// 根据单据状态设置单据
			setBtnsByBillState(iStatus);

		} else if (strState.equals("新增")) { /*-=notranslate=-*/
			boBusiType.setEnabled(false);
			boAdd.setEnabled(false);
			boAudit.setEnabled(false);
			boCancelAudit.setEnabled(false);
			boFreeze.setEnabled(false);
			boCancelFreeze.setEnabled(false);
			boFinish.setEnabled(false);
			boQuery.setEnabled(false);
			boPrntMgr.setEnabled(false);
			boAssistant.setEnabled(true);
			boCachPay.setEnabled(false);
			boOrdBalance.setEnabled(false);
			boAsstntQry.setEnabled(true);
			boAction.setEnabled(true);
			boBrowse.setEnabled(false);
			boPrint.setEnabled(false);
			boPreview.setEnabled(false);
			boSplitPrint.setEnabled(false);
			boEdit.setEnabled(false);
			boCancel.setEnabled(true);
			boSave.setEnabled(true);
			boMaintain.setEnabled(true);
			boBlankOut.setEnabled(false);
			setLineButtonStatus(true);
			boFirst.setEnabled(false);
			boLast.setEnabled(false);
			boPre.setEnabled(false);
			boNext.setEnabled(false);
			boDocument.setEnabled(false);
			boOrderQuery.setEnabled(false);
			boReturn.setEnabled(false);
			boAfterAction.setEnabled(false);
			boRefundment.setEnabled(false);
			// 编辑状态下"配置销售"可用 dongwei zhongwei
			boBom.setEnabled(true);

			// yb add
			boCopyBill.setEnabled(false);

			boSendAudit.setEnabled(true);
			boAuditFlowStatus.setEnabled(false);

			boSendInv.setEnabled(false);
			boSupplyInv.setEnabled(false);
			boDirectInv.setEnabled(false);

			boCoRefPo.setEnabled(false);

			setLineButtonsState();
		} else if (strState.equals("修改")) { /*-=notranslate=-*/
			boBusiType.setEnabled(false);
			boAdd.setEnabled(false);
			boAudit.setEnabled(false);
			boQuery.setEnabled(false);
			boPrntMgr.setEnabled(false);
			boAssistant.setEnabled(true);
			boCachPay.setEnabled(false);
			boOrdBalance.setEnabled(false);
			boAsstntQry.setEnabled(true);
			boAction.setEnabled(true);
			boBrowse.setEnabled(false);
			boPrint.setEnabled(false);
			boPreview.setEnabled(false);
			boSplitPrint.setEnabled(false);
			boEdit.setEnabled(false);
			boCancel.setEnabled(true);
			boSave.setEnabled(true);
			boBlankOut.setEnabled(false);
			setLineButtonStatus(true);
			boFirst.setEnabled(false);
			boLast.setEnabled(false);
			boPre.setEnabled(false);
			boNext.setEnabled(false);
			boClose.setEnabled(false);
			boDocument.setEnabled(false);
			boOrderQuery.setEnabled(false);
			boReturn.setEnabled(false);
			boAfterAction.setEnabled(false);
			boRefundment.setEnabled(false);

			boCoRefPo.setEnabled(false);

			// 编辑状态下"配置销售"可用 dongwei zhongwei
			boBom.setEnabled(true);
			int selrow = getBillCardPanel().getBillTable().getSelectedRow();
			if (selrow >= 0) {
				UFBoolean isInvBom = getBillCardTools().getBodyUFBooleanValue(selrow,
						"isconfigable");
				if (isInvBom != null && isInvBom.booleanValue())
					boBom.setEnabled(true);
				else
					boBom.setEnabled(false);
			}

			// yb add
			boCopyBill.setEnabled(false);

			// 修改状态下送审不可用：ln zc v5.5
			boSendAudit.setEnabled(false);

			boAuditFlowStatus.setEnabled(true);

			setLineButtonsState();
		} else if (strState.equals("修订")) { /*-=notranslate=-*/
			boBusiType.setEnabled(false);
			boAdd.setEnabled(false);
			boAudit.setEnabled(false);
			boQuery.setEnabled(false);
			boPrntMgr.setEnabled(false);
			boAssistant.setEnabled(true);
			boCachPay.setEnabled(false);
			boOrdBalance.setEnabled(false);
			boAsstntQry.setEnabled(true);
			boAction.setEnabled(false);
			boBrowse.setEnabled(false);
			boPrint.setEnabled(false);
			boPreview.setEnabled(false);
			boSplitPrint.setEnabled(false);
			boEdit.setEnabled(false);
			boCancel.setEnabled(true);
			boSave.setEnabled(true);
			boBlankOut.setEnabled(false);
			setLineButtonStatus(true);
			boFirst.setEnabled(false);
			boLast.setEnabled(false);
			boPre.setEnabled(false);
			boNext.setEnabled(false);
			boClose.setEnabled(false);
			boDocument.setEnabled(false);
			boOrderQuery.setEnabled(false);
			boReturn.setEnabled(false);
			boAfterAction.setEnabled(false);
			boRefundment.setEnabled(false);
			boBom.setEnabled(false);

			boSendAudit.setEnabled(false);
			boAuditFlowStatus.setEnabled(true);

			boCoRefPo.setEnabled(false);
			boModification.setEnabled(false);

		}
		if (strState.equals("退货")) { /*-=notranslate=-*/
			boBatch.setEnabled(true);
			boRefundmentDocument.setEnabled(true);
			boOrderQuery.setEnabled(true);
			boReturn.setEnabled(true);
			boRefundment.setEnabled(false);
			boBom.setEnabled(false);
			setLineButtonStatus(true);

			// yb add
			boCopyBill.setEnabled(false);

			boSendAudit.setEnabled(false);
			boAuditFlowStatus.setEnabled(false);
		}

		boRefundment.setEnabled(false);

		getBillCardPanel().setBodyMenuShow(boLine.isEnabled());
		if (strState.equals("退货")) { /*-=notranslate=-*/
			getBillCardPanel().getAddLineMenuItem().setEnabled(false);
			getBillCardPanel().getDelLineMenuItem().setEnabled(boDelLine.isEnabled());
			getBillCardPanel().getPasteLineMenuItem().setEnabled(false);
			getBillCardPanel().getCopyLineMenuItem().setEnabled(false);
			getBillCardPanel().getInsertLineMenuItem().setEnabled(false);
		}

		int iStatus = -1;
		if (getBillCardPanel().getHeadItem("fstatus") != null
				&& getBillCardPanel().getHeadItem("fstatus").getValueObject() != null)
			iStatus = (Integer) getBillCardPanel().getHeadItem("fstatus").getValueObject();
		setBtnsByBillState(iStatus);

//		if (!strState.equals("修订")) { /*-=notranslate=-*/
		if(!b_isRevise) {
			getBillCardPanel().hideBodyTableCol("veditreason");
			getBillCardPanel().getBodyItem("veditreason").setEnabled(false);
		}

		boRefresh.setEnabled(b_query);

		/** 在卡片下不起作用的按钮* */
		boFind.setEnabled(false);
		boListSelectAll.setEnabled(false);
		boListDeselectAll.setEnabled(false);
		/** 在卡片下不起作用的按钮* */

		setBodyMenuStatus();

		updateButtons();
	}

	/**
	 * 配置销售
	 */
	protected void onBom() {
		int row;

		if (strShowState.equals("列表")) {
			row = getBillListPanel().getBodyTable().getSelectedRow();

			onCard();
		} else {
			row = getBillCardPanel().getBillTable().getSelectedRow();
		}

		if (row < 0) {
			showWarningMessage("请先选择表体行！");
			return;
		}

		orderrow = row;
		Object nnumber = getBillCardPanel().getBodyValueAt(row, "nnumber");
		Object ocustID = getBillCardPanel().getHeadItem("ccustomerid").getValueObject();
		Object ocurrID = getBillCardPanel().getHeadItem("ccurrencytypeid").getValueObject();
		Object oinvID = getBillCardPanel().getBodyValueAt(row, "cinventoryid");
		if (ocustID == null || ocurrID == null || oinvID == null) {
			showWarningMessage("请先选择客户、币种、存货！");
			return;
		}

		if (nnumber != null && nnumber.toString().length() != 0) {
			remove(getSplitPanelBc());
			add(getBillTreeCardPanel(), "Center");
			String saleID = (String) getBillCardPanel().getHeadItem("csaleid").getValueObject();
			String custID = (String) ocustID;
			String currID = (String) ocurrID;
			//
			String invID = oinvID.toString();
			//
			String invbaseID = getBillCardPanel().getBodyValueAt(row, "cinvbasdocid").toString();
			//
			String invname = getBillCardPanel().getBodyValueAt(row, "cinventoryname").toString();
			// isspecialty
			Object tempo = getBillCardPanel().getBodyValueAt(row, "isspecialty");
			String isspecialty = tempo == null ? "N"
					: (tempo.toString().equals("true") ? "Y" : "N");
			// 价格
			Object price = null;
			if (SA_02.booleanValue()) {
				price = getBillCardPanel().getBodyValueAt(row, "noriginalcurtaxprice");
			} else {
				price = getBillCardPanel().getBodyValueAt(row, "noriginalcurprice");
			}
			String nprice = (price == null ? null : price.toString());
			// 按币种设精度
			setPanelBomByCurrency(currID);
			// csaleid
			if (saleID != null && saleID.length() != 0) {
				getBillTreeCardPanel().setHeadItem("csaleid", saleID);
			}
			// 看是否做过配置
			Object bomCurrID = getBillCardPanel().getBodyValueAt(row, "cbomorderid");
			if (bomCurrID == null && (!strState.equals("新增") && !strState.equals("修改"))) {
				strBomState = "FREE";
				getBillTreeCardPanel().setEnabled(false);
			} else if (bomCurrID == null) {
				getBillTreeCardPanel().addNew();
				// 客户
				getBillTreeCardPanel().setHeadItem("ccustomerid", custID);
				// 币种
				getBillTreeCardPanel().setHeadItem("ccurrencytypeid", currID);
				// 存货
				getBillTreeCardPanel().setHeadItem("cinventoryid", invID);
				// 需求数量
				getBillTreeCardPanel().setHeadItem("nrequirenumber", nnumber.toString());
				// 销售价格
				getBillTreeCardPanel().setHeadItem("nsaleprice", nprice);
				// 制单日期
				getBillTreeCardPanel().setTailItem("dmakedate",
						getClientEnvironment().getDate().toString());
				// 制单人
				getBillTreeCardPanel().setTailItem("coperatorid",
						getClientEnvironment().getUser().getPrimaryKey());
				strBomState = "ADD";
				getBillTreeCardPanel().setEnabled(true);
			} else {
				strBomState = "FREE";
				loadBomData(row);
				getBillTreeCardPanel().setHeadItem("ccurrencytypeid", currID);
			}
			initBomTree(invbaseID, invname, invID, isspecialty);
			getBillTreeCardPanel().getBillModel().execLoadFormula();
			getBillTreeCardPanel().getBodyItem("nprice").setEdit(SA_15.booleanValue());
			getBillTreeCardPanel().getCustTree().setEnabled(true);
			getBillTreeCardPanel().getCustTree().setEditable(false);
			isSaveStart = true;
			strOldState = strState;
			strState = "BOM";
			// setButtons(getBillButtons());
			initButtons();
			setButtonsState();
			setTitleText(NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000190")/*
																								 * @res
																								 * "产品配置单"
																								 */);
			// 置灰修改键
			boBomEdit.setEnabled(false);
			updateButtons();
			updateUI();
		} else {
			showWarningMessage(NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000191")/*
																									 * @res
																									 * "请先输入存货数量！"
																									 */);
		}
	}

	/**
	 * 动作执行。
	 * 
	 * 创建日期：(2001-6-1 13:12:36)
	 * 
	 */
	protected void onBomAction(ButtonObject bo) {
		try {
			PfUtilClient.processAction(bo.getTag(), SaleBillType.BomOrder, getClientEnvironment()
					.getDate().toString(), getBomVO());
			if (PfUtilClient.isSuccess()) {
				loadBomCurrData();
				// setButtonsState();
				showHintMessage(bo.getName()
						+ NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000132")/*
																								 * @res
																								 * "成功！"
																								 */);
			} else {
				showHintMessage(bo.getName()
						+ NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000339")/*
																								 * @res
																								 * "取消！"
																								 */);
			}
		} catch (BusinessException e) {
			showErrorMessage(e.getMessage());
			showHintMessage(bo.getName()
					+ NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000134")/*
																							 * @res
																							 * "失败！"
																							 */);
		} catch (Exception e) {
			showErrorMessage(e.getMessage());
			showHintMessage(bo.getName()
					+ NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000134")/*
																							 * @res
																							 * "失败！"
																							 */);
			// e.printStackTrace();
		}
	}

	protected void onBomCancel() {
		if (strBomState.equals("ADD")) {
			getBillTreeCardPanel().getCustTree().setEnabled(true);
			// getBillTreeCardPanel().getBillModel().clearBodyData();
			strBomState = "CANCEL";
		}

		if (strBomState.equals("EDIT")) {
			getBillTreeCardPanel().resumeValue();
			getBillTreeCardPanel().getCustTree().setEnabled(true);
			// getBillTreeCardPanel().getBillModel().clearBodyData();
			strBomState = "FREE";
		}

		getBillTreeCardPanel().getCustTree().setEditable(false);
		setBomButtonsState();
	}

	protected void onBomEdit() {
		strBomState = "EDIT";
		getBillTreeCardPanel().setEnabled(true);
		setBomButtonsState();
		getBillTreeCardPanel().getCustTree().setEnabled(false);
		getBillTreeCardPanel().getCustTree().setEditable(false);
		setPzdRowSelected();

	}

	protected void onBomPrint() {
		// 此功能去掉，详细请查询以前版本
	}

	protected void onBomReturn() {
		try {
			// 重算存货BOM价格
			String idBom = (String) getBillTreeCardPanel().getHeadItem("cbomorderid")
					.getValueObject();
			if (idBom != null) {
				UFDouble dPrice = null;
				String idRoot = (String) getBillTreeCardPanel().getHeadItem("cinventoryid")
						.getValueObject();
				if (SO_14.booleanValue()) {
					if (idRoot != null && idRoot.length() != 0)
						dPrice = BomorderBO_Client.getBomPriceUnify(getCorpPrimaryKey(), idBom,
								idRoot);
				} else
					dPrice = BomorderBO_Client.getBomPrice(idBom);
				String sTmp = (String) getBillTreeCardPanel().getHeadItem("bomorderfee")
						.getValueObject();
				UFDouble dBomorderfee = (sTmp == null ? new UFDouble(0) : new UFDouble(sTmp.trim()));
				// 计算金额
				dPrice = (dPrice == null ? new UFDouble(0) : dPrice).add(dBomorderfee);
				if (dPrice != null && dPrice.doubleValue() != 0) {
					if (SA_02.booleanValue())
						getBillCardPanel().setBodyValueAt(dPrice, orderrow, "noriginalcurtaxprice");
					else
						getBillCardPanel().setBodyValueAt(dPrice, orderrow, "noriginalcurprice");
				}

				// 作过配置后不许修改存货，只能删除该存货
				getBillCardPanel().setCellEditable(orderrow, "cinventorycode", false);

				getBillCardPanel().setBodyRowState(orderrow);
			}
			if (SA_02.booleanValue())
				getBillCardPanel().calculateNumber(orderrow, "noriginalcurtaxprice");
			else
				getBillCardPanel().calculateNumber(orderrow, "noriginalcurprice");

			// 重新计算表头价税合计
			getBillCardPanel().getHeadItem("nheadsummny").setValue(
					getBillCardPanel().getTotalValue("noriginalcursummny"));

			remove(getBillTreeCardPanel());
			add(getSplitPanelBc(), "Center");
			long s1 = System.currentTimeMillis();
			getBillCardPanel().getBillModel().execLoadFormula();
			SCMEnv.out("执行公式[共用时" + (System.currentTimeMillis() - s1) + "]");
			getBillCardPanel().getBillTable().clearSelection();
			strState = strOldState;
			initButtons();
			setButtonsState();
			setTitleText(getBillCardPanel().getTitle());
			// 当前BOM的ID
			bomID = null;
			updateUI();
		} catch (Throwable ex) {
			handleException(ex);
		}
	}

	protected void onBomSave() {
		long s = System.currentTimeMillis();
		BomorderVO bomorder = null;
		try {
			if (strBomState.equals("ADD")) {
				bomorder = (BomorderVO) getBillTreeCardPanel().getBillValueVO(
						BomorderVO.class.getName(), BomorderHeaderVO.class.getName(),
						BomorderItemVO.class.getName());
				// 公司代码
				((BomorderHeaderVO) bomorder.getParentVO()).setPk_corp(getCorpPrimaryKey());
				// 单据类型
				((BomorderHeaderVO) bomorder.getParentVO()).setCreceipttype(SaleBillType.BomOrder);
				// 单据状态
				((BomorderHeaderVO) bomorder.getParentVO()).setFstatus(new Integer(
						nc.ui.pub.bill.BillStatus.FREE));
				bomorder.setStatus(nc.vo.pub.VOStatus.NEW);
			}
			if (strBomState.equals("EDIT")) {
				bomorder = (BomorderVO) getBillTreeCardPanel().getBillValueVO(
						BomorderVO.class.getName(), BomorderHeaderVO.class.getName(),
						BomorderItemVO.class.getName());
				// 公司代码
				((BomorderHeaderVO) bomorder.getParentVO()).setPk_corp(getCorpPrimaryKey());
				// 单据类型
				((BomorderHeaderVO) bomorder.getParentVO()).setCreceipttype(SaleBillType.BomOrder);
				// 单据状态
				((BomorderHeaderVO) bomorder.getParentVO()).setFstatus(new Integer(
						nc.ui.pub.bill.BillStatus.FREE));
				for (int i = 0; i < bomorder.getChildrenVO().length; i++) {
					((BomorderItemVO[]) bomorder.getChildrenVO())[i]
							.setStatus(nc.vo.pub.VOStatus.UPDATED);
				}
				bomorder.setStatus(nc.vo.pub.VOStatus.UPDATED);
			}
			bomorder.validate();
			showHintMessage(NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000136")/*
																								 * @res
																								 * "开始保存数据...."
																								 */);
			SCMEnv.out("开始保存：" + System.currentTimeMillis());
			if (strBomState.equals("ADD")) {
				if (isSaveStart) {
					bomID = BomorderBO_Client.insert(bomorder);
					int row = getBillCardPanel().getBillTable().getSelectedRow();
					getBillCardPanel().setBodyValueAt(bomID, row, "cbomorderid");
					getBillCardPanel().getBillModel().execLoadFormula(row);
					// loadBomCurrData();
				} else {
					BomorderBO_Client.insertItems((BomorderItemVO[]) bomorder.getChildrenVO(),
							bomID, getCorpPrimaryKey());
				}
				loadBomCurrData();
				isSaveStart = false;
			}
			if (strBomState.equals("EDIT")) {
				if (addData) {
					BomorderBO_Client.insertItems((BomorderItemVO[]) bomorder.getChildrenVO(),
							bomID, getCorpPrimaryKey());
					addData = false;
				} else {
					bomorder.setPrimaryKey(bomID);
					BomorderBO_Client.update(bomorder);
				}
			}
			getBillTreeCardPanel().updateValue();
			showHintMessage(Message.getSaveSuccessMessage(s));
			if (strBomState.equals("ADD")) {
				getBillTreeCardPanel().getCustTree().setEnabled(true);
				int yesno = showYesNoMessage(NCLangRes.getInstance().getStrByID("40060301",
						"UPP40060301-000396")/*
												 * @res "继续配置其它物料吗! "
												 */);
				if (yesno != UIDialog.ID_YES) {
					strBomState = "SAVE";
					getBillTreeCardPanel().setEnabled(false);
					setPzdSelectedEnabled(false);
					addData = false;
				} else {
					strBomState = "EDIT";
					addData = false;
				}
			} else if (strBomState.equals("EDIT")) {
				getBillTreeCardPanel().getCustTree().setEnabled(true);
				int yesno = showYesNoMessage(NCLangRes.getInstance().getStrByID("40060301",
						"UPP40060301-000138")/*
												 * @res "继续修改配置数据吗!"
												 */);
				if (yesno != UIDialog.ID_YES) {
					strBomState = "SAVE";
				}
			}
			setBomButtonsState();
			// getBillTreeCardPanel().getCustTree().setEnabled(true);
			SCMEnv.out("保存结束" + System.currentTimeMillis());
			showHintMessage(Message.getSaveSuccessMessage(s));
		} catch (ValidationException e) {
			showErrorMessage(e.getMessage());
		} catch (Exception e) {
			showErrorMessage(e.getMessage());
			SCMEnv.out("数据保存失败！");
			// e.printStackTrace(System.out);
		}
	}

	private String selectedBusyType = null;

	/**
	 * 业务类型变化。
	 * 
	 * 创建日期：(2001-9-14 9:41:00)
	 * 
	 * @param bo
	 *            nc.ui.pub.ButtonObject
	 * 
	 */
	protected void onBusiType(ButtonObject bo) {
		bo.setSelected(true);

		// 设置业务类型
		boBusiType.setTag(bo.getTag());
		getBillCardPanel().setBusiType(bo.getTag());
		getBillListPanel().setBusiType(bo.getTag());
		selectedBusyType = bo.getTag();


		// 在当前业务类型的单据来源不存在自制单据时，不能进行复制
		// 复制的单据均为自制单据。在此进行判断
		PfUtilClient.retAddBtn(boAdd, getCorpPrimaryKey(), getBillType(), boBusiType);
		
		// 变化按钮
		setButtonsState();
		setButtons(getBillButtons());

		/** 改变业务类型不影响模版和数据 V51* */
		// // 加载模板
		// if (!SO_20.booleanValue()) {
		// billtempletVO = null;
		// loadListTemplet();
		// loadCardTemplet();
		// setTitleText(getBillCardPanel().getTitle());
		// }
		/** 改变业务类型不影响模版和数据 V51* */
	}

	/**
	 * 子类实现该方法，响应按钮事件。
	 * 
	 * @version (00-6-1 10:32:59)
	 * 
	 * @param bo
	 *            ButtonObject
	 */
	public void onButtonClicked(ButtonObject bo) {

		String pk_corp = ClientEnvironment.getInstance().getCorporation().getPrimaryKey();
		if ("0001".equals(pk_corp)||"@@@@".equals(pk_corp)){
			showErrorMessage("销售订单不支持集团业务！");
			return;
		}
		
		// 二次开发扩展
		try {
			getPluginProxy().beforeButtonClicked(bo);

			// 支持二次开发扩展
			if (strShowState.equals("列表") && getFuncExtend() != null) {/*-=notranslate=-*/

				getFuncExtend().doAction(bo, this, getBillCardPanel(), getBillListPanel(),
						nc.ui.scm.extend.IFuncExtend.LIST);

			} else if (strShowState.equals("卡片") && getFuncExtend() != null) {/*-=notranslate=-*/

				getFuncExtend().doAction(bo, this, getBillCardPanel(), getBillListPanel(),
						nc.ui.scm.extend.IFuncExtend.CARD);

			}
			onExtendBtnsClick(bo);

		} catch (Throwable exx) {
			showErrorMessage(exx.getMessage());
			// //有异常直接返回
			return;
		}

		// 未选中行处理
		if (strShowState.equals("列表") /*-=notranslate=-*/
				&& getBillListPanel().getHeadTable().getSelectedRowCount() <= 0
				&& bo.getParent() != boBusiType && bo.getParent() != boBrowse && bo != boDocument
				&& bo != boRefundmentDocument && bo != boListDeselectAll && bo != boListSelectAll
				&& bo != boCoRefPo && bo != boCoPushPo && bo != boCard && bo != boQuery
				&& bo.getParent() != boAdd && bo.getParent() != boBusiType) {
			showWarningMessage(NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000281")/*
																									 * @res
																									 * "请选择一条单据"
																									 */);
			return;
		}

		if (bo == boListSelectAll)
			onSelectAll();
		else if (bo == boListDeselectAll)
			onDeSelectAll();

		// 列表选多行处理
		else if ((strShowState.equals("列表") /*-=notranslate=-*/
		&& getBillListPanel().getHeadTable().getSelectedRowCount() > 1) && bo != boSendAudit
				&& bo != boCard) {
			if (bo == boPrint || bo == boPreview || bo == boSplitPrint || bo == boQuery
					|| bo.getParent() == boBusiType || bo.getParent() == boBrowse
					|| (bo.getTag() != null && bo.getTag().equals("APPROVE"))
					|| (bo.getTag() != null && bo.getTag().equals("SoUnApprove"))
					|| (bo.getTag() != null && bo.getTag().equals("SoBlankout"))) {

				if (bo.getTag() != null && bo.getTag().equals("APPROVE")) {

					int[] selrow = getBillListPanel().getHeadTable().getSelectedRows();
					if (selrow != null && selrow.length > 0) {
						for (int i = 0; i < selrow.length; i++) {
							int iStatus = Integer.parseInt(getBillListPanel()
									.getHeadItem("fstatus").converType(
											getBillListPanel().getHeadBillModel().getValueAt(
													selrow[i], "fstatus")).toString());
							try {
								/*if (isExistWorkflow(selrow[i])) {
									showErrorMessage(NCLangRes.getInstance().getStrByID("40060301",
											"UPP40060301-000282", null,
											new String[] { (selrow[i] + 1) + "" }));

									return;
								}*/
							} catch (Exception e) {
								showErrorMessage(e.getMessage());
								return;
							}
							if (iStatus != BillStatus.FREE) {
								showErrorMessage(NCLangRes.getInstance().getStrByID("40060301",
										"UPP40060301-000284", null,
										new String[] { (selrow[i] + 1) + "" }));

								return;
							}
						}
					}

				} else if (bo.getTag() != null && bo.getTag().equals("SoUnApprove")) {

					int[] selrow = getBillListPanel().getHeadTable().getSelectedRows();
					for (int i = 0; i < selrow.length; i++) {

						int iStatus = Integer.parseInt(getBillListPanel().getHeadItem("fstatus")
								.converType(
										getBillListPanel().getHeadBillModel().getValueAt(selrow[i],
												"fstatus")).toString());
						try {
/*							if (isExistWorkflow(selrow[i])) {
								showErrorMessage(NCLangRes.getInstance().getStrByID("40060301",
										"UPP40060301-000286", null,
										new String[] { (selrow[i] + 1) + "" }));
								// showErrorMessage("所选择行[" + (selrow[i] +
								// 1)
								// + "]的订单配置了工作流，不能多条单据弃审");
								return;
							}*/
						} catch (Exception e) {
							showErrorMessage(e.getMessage());
							return;
						}
						if (iStatus != BillStatus.AUDIT) {
							showErrorMessage(NCLangRes.getInstance().getStrByID("40060301",
									"UPP40060301-000287", null,
									new String[] { (selrow[i] + 1) + "" }));
							// showErrorMessage("所选择的行[" + (selrow[i] + 1)
							// + "]不是已审批的订单，不能弃审");
							return;
						}

					}
				} else if (bo.getTag() != null && bo.getTag().equals("SoBlankout")) {

					int[] selrow = getBillListPanel().getHeadTable().getSelectedRows();
					for (int i = 0; i < selrow.length; i++) {

						int iStatus = Integer.parseInt(getBillListPanel().getHeadItem("fstatus")
								.converType(
										getBillListPanel().getHeadBillModel().getValueAt(selrow[i],
												"fstatus")).toString());
						if (iStatus != BillStatus.FREE) {
							showErrorMessage(NCLangRes.getInstance().getStrByID("40060301",
									"UPP40060301-000527", null,
									new String[] { (selrow[i] + 1) + "" }));
							// showErrorMessage("所选择的行[" + (selrow[i] + 1)
							// + "]不是已审批的订单，不能弃审");
							return;
						}

					}
				}

			} else {
				showErrorMessage(NCLangRes.getInstance().getStrByID("40060301",
						"UPP40060301-000288")/*
												 * @res "请选择单行"
												 */);
				return;
			}
		}

		getBillCardPanel().stopEditing();

		// 易用性修改 提示
		showHintMessage("");
		try {
			if (bo == boAddLine)
				onAddLine();
			else if (bo == boDelLine)
				onDelLine();
			else if (bo == boInsertLine)
				onInsertLine();
			else if (bo == boCopyLine)
				onCopyLine();
			else if (bo == boPasteLine)
				onPasteLine();
			else if (bo == boPasteLineToTail)
				onPasteLineToTail();
			else if (bo == boFindPrice)
				onFindPrice();
			else if (bo == boResortRowNo)
				onResortRowNo();
			else if (bo == boCardEdit)
				onCardEdit();
			else if (bo == boSave)
				onSave();
			else if (bo == boCancel)
				onCancel();
			else if (bo == boEdit)
				onEdit();
			else if (bo == boCard)
				onCard();
			else if (bo == boPreview)
				onPrint(true);
			else if (bo == boPrint)
				onPrint(false);
			else if (bo == boSplitPrint)
				onSplitPrint();
			else if (bo == boFind)
				onFind();
			else if (bo == boFirst)
				onFrist();
			else if (bo == boLast)
				onLast();
			else if (bo == boPre)
				onPre();
			else if (bo == boNext)
				onNext();
			else if (bo == boDocument || "document".equals(bo.getTag()))
				onDocument();
			else if (bo == boReturn)
				onReturn();
			else if (bo == boBack)
				onBack();
			else if (bo == boQuery)
				onQuery();
			else if (bo == boRefresh)
				onRefresh();
			else if (bo == boRefundment)
				onRefundment();
			else if (bo == boBatch)
				onBatch();
			else if (bo == boOrderQuery)
				// 联查
				onOrderQuery();
			else if (bo == boBom) {
				onBom();
				return;
			} else if (bo == boBomSave)
				onBomSave();
			else if (bo == boBomEdit)
				onBomEdit();
			else if (bo == boBomCancel)
				onBomCancel();
			else if (bo == boAuditFlowStatus || "auditflowstatus".equals(bo.getTag()))
				onAuditFlowStatus();
			else if (bo == boSendAudit)
				onSendAudit();
			else if (bo == boBomPrint)
				onBomPrint();
			else if (bo == boBomReturn)
				onBomReturn();
			else if (bo == boModification)
				onModification();
			else if (bo.getParent() == boBusiType) {
				// 业务类型
				onBusiType(bo);
			} else if (bo.getParent() == boAdd) {
				// 新增
				onNew(bo);
			} else if (bo == boCoRefPo) {
				// 参照协同采购订单
				onCoRefPo(bo);
			} else if (bo == boCoPushPo) {
				// 协同推采购订单
				onCoPushPo();
			} else if (bo == boCopyBill) {
				onCopyBill();
			} else if (bo.getParent() == boAction || "APPROVE".equals(bo.getTag())
					|| "SoUnApprove".equals(bo.getTag()) || "SoBlankout".equals(bo.getTag())) {
				// 执行
				onAction(bo);
			} else if (bo == boCachPay) {
				// 订单收款
				onCachPay();
				return;
			} else if (bo == boOrdBalance) {
				// 订单核销
				onOrderBalance();
				return;
			} else if (bo == boOnHandShowHidden) {
				// 存量显示/隐藏
				onOnHandShowHidden();
			} else if (bo == boCustCredit) {
				// 客户信用
				onAssistant(bo);
			} else if (bo == boOrderExecRpt) {
				// 订单执行情况
				onAssistant(bo);
			} else if (bo == boCustInfo) {
				// 客户信息
				onAssistant(bo);
			} else if (bo == boPrifit) {
				// 毛利预估
				onAssistant(bo);
			} else if (bo.getParent() == boAssistant) {
				// 辅助
				onAssistant(bo);
			} else if (bo.getParent() == boAfterAction) {
				// 后续动作
				onAfterAction(bo);
			} else {
				ButtonObject[] btns = getOrderBalanceUI().getButtons();
				if (btns != null && btns.length > 0) {
					for (int i = 0, loop = btns.length; i < loop; i++) {
						if (btns[i] == bo || btns[i] == bo.getParent()) {
							getOrderBalanceUI().onButtonClicked(bo);
							updateButtons();
							updateUI();
							return;
						}
					}
				}

				if (getBillToBillUI() != null) {
					btns = getBillToBillUI().setButtenForUse(SaleBillType.SaleOrder);
					if (btns != null && btns.length > 0) {
						for (int i = 0, loop = btns.length; i < loop; i++) {
							if (btns[i] == bo || btns[i] == bo.getParent()) {
								getBillToBillUI().onButtonClicked(bo);
								updateButtons();
								updateUI();
								return;
							}
						}
					}
				}

			}

			// 二次开发扩展
			getPluginProxy().afterButtonClicked(bo);

		} catch (Exception e) {
			handleException(e);
			showErrorMessage(e.getMessage());
		}

		setButtonsState();
		if (bo.getParent() == boAdd || bo == boCopyBill || bo == boRefundment || bo == boEdit) {
			// 将光标定位在第一个可编辑域
			getBillCardPanel().transferFocusTo(BillCardPanel.HEAD);
		}

	}

	protected void onSelectAll() {
		try {
			int maxindex = getBillListPanel().getHeadBillModel().getRowCount();
			if (maxindex > 0)
				getBillListPanel().getHeadTable().setRowSelectionInterval(0, maxindex - 1);
		} catch (Exception e) {
			nc.vo.scm.pub.SCMEnv.out(e.getMessage());
		}

	}

	protected void onDeSelectAll() {
		try {
			int maxindex = getBillListPanel().getHeadBillModel().getRowCount();
			if (maxindex > 0)
				getBillListPanel().getHeadTable().clearSelection();
		} catch (Exception e) {
			nc.vo.scm.pub.SCMEnv.out(e.getMessage());
		}
	}

	/**
	 * 检测审批单据合法性。
	 * 
	 * 创建日期：(2001-8-8 10:09:01)
	 * 
	 * @return boolean
	 */
	private boolean isExistWorkflow(int row) throws ValidationException {
		boolean isExist = false;
		try {
			isExist = nc.ui.pub.pf.PfUtilBO_Client.isExistWorkFlow(getBillType(), boBusiType
					.getTag(), getClientEnvironment().getCorporation().getPk_corp(),
					(String) getBillListPanel().getHeadBillModel().getValueAt(row, "coperatorid"));

		} catch (Throwable e) {
			throw new nc.vo.pub.ValidationException(NCLangRes.getInstance().getStrByID("40060301",
					"UPP40060301-000276")/*
											 * @res "判断单据是否处于工作流失败"
											 */);
		}
		return isExist;
	}

	/**
	 * 放弃输入。
	 * 
	 * 创建日期：(2001-4-21 10:36:57)
	 * 
	 */
	public void onCancel() {

		// // 恢复模板项的初始编辑状态
		// getBillCardTools().resumeBillItemEditToInit();

		/**新订单表头ID(解决保存时，拔掉网线导致数据重复的问题)*/
		clearOID();
		/**新订单表头ID(解决保存时，拔掉网线导致数据重复的问题)*/
		
		getBillCardPanel().setEnabled(false);

		strState = "自由";

		getBillCardPanel().resumeValue();

		if (SO41.booleanValue()) {
			if (getBillCardTools().getOldsaleordervo() != null)
				getBillCardPanel().setTailItem("coperatorid",
						getBillCardTools().getOldsaleordervo().getHeadVO().getCoperatorid());
		}

		getBillCardTools().setHeadRefLimit(strState);

		String csaleid = (String) getBillCardPanel().getHeadItem("csaleid").getValueObject();

		SaleOrderVO saleorder = vocache.getSaleOrderVO(csaleid);
		loadCardData(saleorder);
		setButtonsState();
		updateUI();

		showHintMessage("");

		// 加自由项变色龙。
		InvAttrCellRenderer ficr = new InvAttrCellRenderer();
		ficr.setFreeItemRenderer(getBillCardPanel(), null);

		nc.ui.scm.pub.panel.SetColor.resetColor(getBillCardPanel());
		if(getBillListPanel().isTempVO()){
			autoSwitchPanel();
		}
	}

	/**
	 * 单据由列表状态更改卡片状态。
	 * 
	 * 创建日期：(2001-3-17 9:00:09)
	 * 
	 */
	protected void onCard() {

		if (getBillListPanel().isTempVO()) {
			loadCardDataBYTemp(getBillListPanel().getHeadTable().getSelectedRow());

		} else {
			strShowState = "卡片"; /*-=notranslate=-*/
			strState = "自由"; /*-=notranslate=-*/
			switchInterface();
			num = getBillListPanel().getHeadTable().getSelectedRow();
			String csaleid = (String) getBillListPanel().getHeadBillModel().getValueAt(num,
					"csaleid");

			if (csaleid != null) {
				SaleOrderVO saleorder = vocache.getSaleOrderVO(csaleid);
				loadCardData(saleorder);
			} else {
				loadCardData(null);
			}
		}

		setButtons(getBillButtons());
		setBodyMenuItem(false);
		setButtonsState();

		updateUI();
	}

	/**
	 * 检测单据合法性。
	 * 
	 * 创建日期：(2001-8-8 10:09:01)
	 * 
	 * @return boolean
	 * 
	 */
	protected void onCheck(SaleOrderVO saleorder) throws ValidationException, BusinessException {

		// 清空错误行信息
		rowList.clear();
		
		//执行验证公式 
		getBillCardPanel().getBillData().execValidateFormulas();

		// VO检测
		saleorder.validate();

		//多行重复存货检查
		if (SO_03!=null&&!SO_03.booleanValue())
			saleorder.checkInvMoreRow();

		// 检查表头销售组织，必须为末级
		/** 针对复制单据，或者信息自动带出的情况 **/
		// v5.6连接数
		//getBillCardPanel().checkSaleCorp();

		// 单据体
		if (getBillCardPanel().getRowCount() == 0)
			throw new nc.vo.pub.ValidationException(NCLangRes.getInstance().getStrByID("SCMCOMMON",
					"UPPSCMCommon-000073")/* @res "单据体不能为空!" */);

		//
		int iOOSLine = 0;

		// 检查订单行中是否存在正负反号的行
		int iallMinusMnyrow = 0;
		int iallMinusNumrow = 0;
		SaleorderBVO[] bodyVOs = (SaleorderBVO[]) saleorder.getChildrenVO();

		UFBoolean bretflag = saleorder.getHeadVO().getBretinvflag();

		for (int i = 0; i < saleorder.getChildrenVO().length; i++) {

			if (bodyVOs[i].getNoriginalcursummny() != null
					&& bodyVOs[i].getNoriginalcursummny().doubleValue() < 0) {
				iallMinusMnyrow++;
			}

			if ((bodyVOs[i].getNoriginalcurtaxprice() != null && bodyVOs[i]
					.getNoriginalcurtaxprice().doubleValue() < 0)
					|| (bodyVOs[i].getNoriginalcurtaxnetprice() != null && bodyVOs[i]
							.getNoriginalcurtaxnetprice().doubleValue() < 0)
					|| (bodyVOs[i].getNoriginalcurprice() != null && bodyVOs[i]
							.getNoriginalcurprice().doubleValue() < 0)
					|| (bodyVOs[i].getNoriginalcurnetprice() != null && bodyVOs[i]
							.getNoriginalcurnetprice().doubleValue() < 0)) {
				rowList.add(new Integer(i));
				throw new ValidationException(NCLangRes.getInstance().getStrByID("40060301",
						"UPP40060301-000420", null, new String[] { (i + 1) + "" }));
				// throw new ValidationException("行[" + (i + 1)
				// + "]的价格（含税单价，不含税单价，含税净价，不含税净价）不能小于0! ");

			}

			if (bodyVOs[i].getNnumber() != null && bodyVOs[i].getNnumber().doubleValue() >= 0) {
				if (bretflag != null && bretflag.booleanValue()) {
					rowList.add(new Integer(i));
					throw new ValidationException(NCLangRes.getInstance().getStrByID("40060301",
							"UPP40060301-000195", null, new String[] { (i + 1) + "" }));
					// throw new ValidationException("对于退货订单，行[" + (i + 1)
					// + "]的数量不能大于等于0! ");
				}
			}

			// 数量<0检测
			if (bodyVOs[i].getNnumber() != null && bodyVOs[i].getNnumber().doubleValue() < 0) {
				if (!SO45.booleanValue() && (bretflag == null || !bretflag.booleanValue())) {
					rowList.add(new Integer(i));
					throw new ValidationException(NCLangRes.getInstance().getStrByID("40060301",
							"UPP40060301-000194", null, new String[] { (i + 1) + "" }));
					// throw new ValidationException("行[" + (i + 1)
					// + "]的数量不能小于0! ");
				}
				// 跨公司销售公司的红字行必须是直运
				else {
					if (bodyVOs[i].getCconsigncorpid() != null
							&& !bodyVOs[i].getCconsigncorpid().equals(bodyVOs[i].getPkcorp())
							&& (bodyVOs[i].getBdericttrans() == null || !bodyVOs[i]
									.getBdericttrans().booleanValue())) {
						rowList.add(new Integer(i));
						throw new ValidationException(NCLangRes.getInstance().getStrByID(
								"40060301", "UPT40060301-000548", null,
								new String[] { (i + 1) + "" }));
					}
					// throw new ValidationException("行[" + (i + 1)
					// + "]的存货必须走直运! ");
				}
				iallMinusNumrow++;
			}

			if (bodyVOs[i].getBoosflag() != null && bodyVOs[i].getBoosflag().booleanValue()) {

				iOOSLine++;
			} else {
				if (bodyVOs[i].getBoosflag() == null)
					bodyVOs[i].setBoosflag(new UFBoolean(false));
			}
		}
		if (iOOSLine == bodyVOs.length) {
			throw new ValidationException(NCLangRes.getInstance().getStrByID("40060301",
					"UPP40060301-000398")/*
											 * @res "单据行不能全选成缺货! "
											 */);
		}

		// 单据限行
		if (SO_01 != null) {
			if (SO_01.intValue() != 0) {
				if (SO_01.intValue() < getBillCardPanel().getRowCount()) {
					throw new ValidationException(NCLangRes.getInstance().getStrByID("40060301",
							"UPP40060301-000171", null, new String[] { SO_01.intValue() + "" }));
					// throw new ValidationException("单据限"
					// + CommonConstant.BEGIN_MARK + SO_01.intValue()
					// + CommonConstant.END_MARK + "行。");
				}
			}
		}

		boolean bischeckaddr = false;
		BillItem biaddr = getBillCardPanel().getHeadItem("vreceiveaddress");
		try {

			if (biaddr != null) {
				bischeckaddr = biaddr.isNull();
				biaddr.setNull(false);
			}

			// 用户单据设置非空项检测 yb modify 2003-10-15 修改自由项非空检查问题
			// getBillCardPanel().dataNotNullValidate();
			int[] notcheckline = getNotCheckFreeItemLine();
			if (notcheckline == null)
				getBillCardPanel().getBillData().dataNotNullValidate();
			else {
				BillItem item = getBillCardPanel().getBodyItem("vfree0");
				if (item == null)
					getBillCardPanel().getBillData().dataNotNullValidate();
				else {
					Hashtable ht = new Hashtable();
					ht.put(item, notcheckline);
					getBillCardPanel().getBillData().dataNotNullValidate(ht);
				}
			}

			if (biaddr != null) {
				if (bischeckaddr) {
					String saddr = ((UIRefPane) biaddr.getComponent()).getUITextField().getText();
					if (saddr == null || saddr.trim().length() <= 0) {
						throw new nc.vo.pub.ValidationException(NCLangRes.getInstance().getStrByID(
								"40060301", "UPP40060301-000083")
								+ biaddr.getName()/* @res "下列字段不能为空：" */
						);
						// "表头"+biaddr.getName()+"不能为空!");
						// showErrorMessage();
						// return
					}
				}
			}

		} finally {
			biaddr.setNull(bischeckaddr);
		}

		boolean retflag = false;
		UFBoolean bret = new UFBoolean(getBillCardPanel().getHeadItem("bretinvflag").getValue());
		if (bret != null && bret.toString().equals("Y"))
			retflag = true;

		// 生产线检查
		if (SO27 != null && SO27.booleanValue())
			getBillCardTools().checkProdLineForOne();

		HashMap hp = new HashMap();
		if (strState.equals("修订")) {
			SaleOrderVO svo = getBillCardTools().getOldsaleordervo();
			for (int i = 0; i < svo.getChildrenVO().length; i++) {
				if (svo.getChildrenVO()[i].getPrimaryKey() != null) {
					hp.put(svo.getChildrenVO()[i].getPrimaryKey(), svo.getChildrenVO()[i]);
				}
			}
		}
		for (int i = 0; i < saleorder.getChildrenVO().length; i++) {

			SaleorderBVO oldbodyVO = (SaleorderBVO) saleorder.getChildrenVO()[i];

			// 新增处理
			if (oldbodyVO.getStatus() == VOStatus.NEW) {
				oldbodyVO.setPrimaryKey(null);
				//oldbodyVO.setCsaleid(null);
			}

			// 非折扣类存货
			if (oldbodyVO.getDiscountflag() == null || !oldbodyVO.getDiscountflag().booleanValue()) {
				// 不是赠品时检测单价
				if (!(oldbodyVO.getBlargessflag() == null || oldbodyVO.getBlargessflag()
						.booleanValue())
				// && !(oldbodyVO.getIsappendant() == null ||
				// oldbodyVO.getIsappendant().booleanValue())
				) {

					// 数量
					if ((oldbodyVO.getNoriginalcurmny() != null && oldbodyVO.getNoriginalcurmny()
							.doubleValue() < 0)
							|| (oldbodyVO.getNoriginalcurtaxmny() != null && oldbodyVO
									.getNoriginalcurtaxmny().doubleValue() < 0)
							|| (oldbodyVO.getNoriginalcursummny() != null && oldbodyVO
									.getNoriginalcursummny().doubleValue() < 0))
						if (oldbodyVO.getNnumber().doubleValue() > 0) {
							rowList.add(new Integer(i));
							throw new ValidationException(NCLangRes.getInstance().getStrByID(
									"40060301", "UPP40060301-000209")/*
																		 * @res
																		 * "当前数量不允许大于零!"
																		 */);
						}
					// 数量、金额、价税合计
					if (oldbodyVO.getNoriginalcursummny() == null
					/** 允许价税合计为0的行 V55 jindongmei zhongwei* */
					// || oldbodyVO.getNoriginalcursummny().doubleValue() == 0
					) {
						rowList.add(new Integer(i));
						throw new ValidationException(NCLangRes.getInstance().getStrByID(
								"40060301", "UPP40060301-000210", null,
								new String[] { (i + 1) + "" })/*
																 * @res
																 * "第行价税合计不能为空!"
																 */);
					}
				}
			} else {
				// 数量、金额、价税合计
				if (oldbodyVO.getNoriginalcursummny() == null) {
					rowList.add(new Integer(i));
					throw new ValidationException(NCLangRes.getInstance().getStrByID("40060301",
							"UPP40060301-000210", null, new String[] { (i + 1) + "" })/*
																						 * @res
																						 * "第行价税合计不能为空!"
																						 */);
				}

				// 数量、金额、价税合计
				if (oldbodyVO.getNnumber() == null && oldbodyVO.getNoriginalcurmny() == null
						&& oldbodyVO.getNoriginalcursummny() == null) {
					rowList.add(new Integer(i));
					throw new ValidationException(NCLangRes.getInstance().getStrByID("SCMCOMMON",
							"UPPSCMCommon-000261")/*
													 * @res "数量、金额、价税合计不能同时为空!"
													 */);
				}
			}
			// 包装单位(是否采用辅计量)
			if (oldbodyVO.getAssistunit() != null && oldbodyVO.getAssistunit().booleanValue()) {
				if (oldbodyVO.getDiscountflag() != null
						&& !oldbodyVO.getDiscountflag().booleanValue()) {
					if (oldbodyVO.getCpackunitid() == null
							|| oldbodyVO.getCpackunitid().trim().length() == 0) {
						rowList.add(new Integer(i));
						throw new ValidationException(NCLangRes.getInstance().getStrByID(
								"40060301", "UPP40060301-000211", null,
								new String[] { (i + 1) + "" }));
						// throw new ValidationException("第" + (i + 1)
						// + "行辅单位不能为空!");
					}
					if (oldbodyVO.getNpacknumber() == null) {
						rowList.add(new Integer(i));
						throw new ValidationException(NCLangRes.getInstance().getStrByID(
								"40060301", "UPP40060301-000212", null,
								new String[] { (i + 1) + "" }));
						// throw new ValidationException("第" + (i + 1)
						// + "行辅数量不能为空!");
					}
					if (oldbodyVO.getScalefactor() == null) {
						rowList.add(new Integer(i));
						throw new ValidationException(NCLangRes.getInstance().getStrByID(
								"40060301", "UPP40060301-000548", null,
								new String[] { (i + 1) + "" }));
						// throw new ValidationException("第" + (i + 1)
						// + "行换算率不能为空!");
					} else {
						if (oldbodyVO.getNpacknumber().doubleValue()
								* oldbodyVO.getNnumber().doubleValue() < 0) {
							rowList.add(new Integer(i));
							throw new ValidationException(NCLangRes.getInstance().getStrByID(
									"40060301", "UPP40060301-000213", null,
									new String[] { (i + 1) + "" }));
							// throw new ValidationException("第" + (i + 1)
							// + "行数量和辅数量符号必须相同!");
						}
					}
				}
			}
			// 订单的发货日期小于订单日期
			UFDate dbilldate = new UFDate(getBillCardPanel().getHeadItem("dbilldate").getValue());
			UFDate dconsigndate = oldbodyVO.getDconsigndate();
			UFDate deliverdate = oldbodyVO.getDdeliverdate();
			if (dconsigndate == null || dconsigndate.toString().length() == 0
					|| deliverdate == null || deliverdate.toString().length() == 0) {
				rowList.add(new Integer(i));
				throw new ValidationException(NCLangRes.getInstance().getStrByID("40060301",
						"UPP40060301-000214")/*
												 * @res "订单的发货日期、交货日期不能为空!"
												 */);
			}
			if (!retflag && dconsigndate != null && dbilldate != null) {
				if (dbilldate.after(dconsigndate) && dbilldate != dconsigndate) {
					rowList.add(new Integer(i));
					throw new ValidationException(NCLangRes.getInstance().getStrByID("40060301",
							"UPP40060301-000033")/*
													 * @res "发货日期应大于等于单据日期!"
													 */);
				}
			}
			if (dconsigndate != null && deliverdate != null) {
				if (dconsigndate.after(deliverdate) && deliverdate != dconsigndate) {
					rowList.add(new Integer(i));
					throw new ValidationException(NCLangRes.getInstance().getStrByID("40060301",
							"UPP40060301-000034")/*
													 * @res "订单的交货日期应大于等于发货日期!""
													 */);
				}
			}

			// 根据参数检测
			String oldinventoryid = oldbodyVO.getCinventoryid(); // 存货
			// String oldbodywarehouseid = oldbodyVO.getCbodywarehouseid(); //仓库
			// String oldbatchid = oldbodyVO.getCbatchid(); //货物批次
			// Integer oldinvclass = null; //货物级次
			if (oldbodyVO.getBlargessflag() != null && !oldbodyVO.getBlargessflag().booleanValue()) {
				for (int j = i + 1; j < saleorder.getChildrenVO().length; j++) {
					SaleorderBVO newbodyVO = (SaleorderBVO) saleorder.getChildrenVO()[j];
					// SO_03 相同存货可否共存一单（赠品除外）
					if (!SO_03.booleanValue()) {
						String newinventoryid = newbodyVO.getCinventoryid(); // 存货
						if (newbodyVO.getBlargessflag() != null
								&& !newbodyVO.getBlargessflag().booleanValue()) {
							if (oldinventoryid.equals(newinventoryid)) {
								rowList.add(new Integer(i));
								throw new ValidationException(NCLangRes.getInstance().getStrByID(
										"40060301", "UPP40060301-000216", null,
										new String[] { (i + 1) + "", (j + 1) + "" }));
								// throw new nc.vo.pub.ValidationException("第"
								// + CommonConstant.BEGIN_MARK + (i + 1)
								// + CommonConstant.END_MARK + "行和第"
								// + CommonConstant.BEGIN_MARK + (j + 1)
								// + CommonConstant.END_MARK + "行存货相同！");
							}
						}
					}
				}
			}
			// 修订检测
			if (strState.equals("修订")) {/*-=notranslate=-*/

				UFDouble nnumber = oldbodyVO.getNnumber();
				CircularlyAccessibleValueObject coriVO = (CircularlyAccessibleValueObject) hp
						.get(oldbodyVO.getPrimaryKey());
				if (coriVO != null
						&& coriVO.getAttributeValue("nnumber") != null
						&& oldbodyVO.getNnumber() != null
						&& (((UFDouble) coriVO.getAttributeValue("nnumber")).multiply(oldbodyVO
								.getNnumber())).doubleValue() < 0) {
					rowList.add(new Integer(i));
					throw new ValidationException(NCLangRes.getInstance().getStrByID("sopub",
							"UPPsopub-000007"));
				}
				// 允差判断
				checkAllowDiffer(oldbodyVO, i);

				// +narrangescornum
				UFDouble ntotalconvertnum = SoVoTools.getMnyAdd(SoVoConst.duf0, oldbodyVO
						.getNarrangescornum());
				// +narrangepoapplynum
				ntotalconvertnum = SoVoTools.getMnyAdd(ntotalconvertnum, oldbodyVO
						.getNarrangepoapplynum());
				// +narrangetoornum
				ntotalconvertnum = SoVoTools.getMnyAdd(ntotalconvertnum, oldbodyVO
						.getNarrangetoornum());
				// +norrangetoapplynum
				ntotalconvertnum = SoVoTools.getMnyAdd(ntotalconvertnum, oldbodyVO
						.getNorrangetoapplynum());
				// +narrangemonum
				ntotalconvertnum = SoVoTools.getMnyAdd(ntotalconvertnum, oldbodyVO
						.getNarrangemonum());
				nnumber = oldbodyVO.getNnumber();
				if (nnumber != null && nnumber.doubleValue() < 0) {
					nnumber = nnumber.multiply(-1);
					ntotalconvertnum = ntotalconvertnum == null ? new UFDouble(0)
							: ntotalconvertnum.multiply(-1);
				}
				if (ntotalconvertnum != null
						&& nnumber != null
						&& ntotalconvertnum.abs().compareTo(nnumber.abs().multiply(SO43.add(1.0))) > 0) {
					rowList.add(new Integer(i));
					throw new ValidationException(NCLangRes.getInstance().getStrByID("40060301",
							"UPP40060301-000501", null, new String[] { (i + 1) + "" }));
					// UPP40060301-000501=第{0}行修訂數量不能小於轉請購單，委外訂單，生産訂單，調撥訂單，調撥申請單數量之和。
				}
			}
			// 退货检测
			if (strState.equals("退货")) {/*-=notranslate=-*/
				UFDouble nnumber = oldbodyVO.getNnumber();
				UFDouble ntotalreceiptnumber = oldbodyVO.getNtotalreceivenumber();
				if (nnumber != null)
					if (nnumber.compareTo(new UFDouble(0)) >= 0) {
						rowList.add(new Integer(i));
						throw new ValidationException(NCLangRes.getInstance().getStrByID(
								"40060301", "UPP40060301-000225", null,
								new String[] { (i + 1) + "" }));
					}
				// throw new nc.vo.pub.ValidationException("第"
				// + CommonConstant.BEGIN_MARK + (i + 1)
				// + CommonConstant.END_MARK + "行退货数量不能大于或等于零。");
				if (nnumber != null && ntotalreceiptnumber != null)
					if (nnumber.compareTo(ntotalreceiptnumber) == 1) {
						rowList.add(new Integer(i));
						throw new ValidationException(NCLangRes.getInstance().getStrByID(
								"40060301", "UPP40060301-000226", null,
								new String[] { (i + 1) + "" }));
					}

				UFDouble ntotalinvoicenumber = oldbodyVO.getNtotalinvoicenumber();
				if (nnumber != null && ntotalinvoicenumber != null)
					if (nnumber.compareTo(ntotalinvoicenumber) == 1) {
						rowList.add(new Integer(i));
						throw new ValidationException(NCLangRes.getInstance().getStrByID(
								"40060301", "UPP40060301-000228", null,
								new String[] { (i + 1) + "" }));
					}

				UFDouble ntotalinventorynumber = oldbodyVO.getNtotalinventorynumber();
				if (nnumber != null && ntotalinventorynumber != null)
					if (nnumber.compareTo(ntotalinventorynumber) == 1) {
						rowList.add(new Integer(i));
						throw new ValidationException(NCLangRes.getInstance().getStrByID(
								"40060301", "UPP40060301-000229", null,
								new String[] { (i + 1) + "" }));
					}

			}
		}
		// System.out.println("合法性检查用时：" + (System.currentTimeMillis() - s) +
		// "毫秒");

		// 全通过以后进行尾差处理
	}

	/**
	 * 检测是否超出允差 wsy 2005-9-13
	 */
	private void checkAllowDiffer(SaleorderBVO oldbodyVO, int i) throws ValidationException {

		SaleOrderVO vo = vocache.getSaleOrderVO(oldbodyVO.getCsaleid());
		SaleorderBVO bvo = null;
		if (vo != null && vo.getBodyVOs() != null)
			for (int k = 0; k < vo.getBodyVOs().length; k++) {
				if (vo.getBodyVOs()[k].getCorder_bid().equals(oldbodyVO.getCorder_bid())) {
					bvo = vo.getBodyVOs()[k];
				}

			}
		if (bvo == null)
			bvo = oldbodyVO;

		UFDouble nnumber = oldbodyVO.getNnumber() == null ? new UFDouble(0) : oldbodyVO
				.getNnumber();
		boolean bisNeg = false;
		if (nnumber.doubleValue() < 0) {
			bisNeg = true;
			nnumber = nnumber.multiply(-1);

		}

		UFDouble ntotalreceiptnumber = bvo.getNtotalreceivenumber() == null ? new UFDouble(0) : bvo
				.getNtotalreceivenumber();
		UFDouble ntotalinvoicenumber = bvo.getNtotalinvoicenumber() == null ? new UFDouble(0) : bvo
				.getNtotalinvoicenumber();
		UFDouble ntotalinventorynumber = (bvo.getNtotalinventorynumber() == null ? new UFDouble(0)
				: bvo.getNtotalinventorynumber())
				.add(bvo.getNtotalshouldoutnum() == null ? new UFDouble(0) : bvo
						.getNtotalshouldoutnum());
		UFDouble ntranslossnum = bvo.getNtranslossnum() == null ? new UFDouble(0) : bvo
				.getNtranslossnum();

		ntotalinventorynumber = ntotalinventorynumber.sub(ntranslossnum);

		if (bisNeg) {
			ntotalreceiptnumber = ntotalreceiptnumber.multiply(-1);
			ntotalinvoicenumber = ntotalinvoicenumber.multiply(-1);
			ntotalinventorynumber = ntotalinventorynumber.multiply(-1);
		}

		UFDouble postiveNum = null;

		// 第{0}行修订数量不能超过允许发货数量。
		String FHMSG = NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000218", null,
				new String[] { (i + 1) + "" });
		String CKMSG = NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000222", null,
				new String[] { (i + 1) + "" });
		String KPMSG = NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000221", null,
				new String[] { (i + 1) + "" });

		ArrayList al = new ArrayList();

		postiveNum = nnumber;

		if (postiveNum.compareTo(ntotalreceiptnumber) < 0) {
			al.add(FHMSG);
		}

		if (SO25.booleanValue())
			postiveNum = nnumber.multiply(1.0 + SO26.doubleValue() / 100.);
		else
			postiveNum = nnumber;

		if (postiveNum.compareTo(ntotalinvoicenumber) < 0) {
			al.add(KPMSG);
		}

		postiveNum = nnumber;

		if (postiveNum.compareTo(ntotalinventorynumber) < 0) {
			al.add(CKMSG + "\n" + NCLangRes.getInstance().getStrByID("common", "UC000-0003145")/*
																								 * @res
																								 * "累计出库数量"
																								 */
					+ ":" + ntotalinventorynumber);
		}

		// 第[]行修订数量{}超出[]数量{}的允差范围[]

		if (al.size() > 0) {
			String sScoremsg = "";
			for (int j = 0; j < al.size(); j++) {
				sScoremsg += (String) al.get(j) + "\n";
			}
			throw new ValidationException(sScoremsg);

		}

	}

	/**
	 * 文档管理。
	 * 
	 * 创建日期：(2001-4-24 9:55:56)
	 * 
	 */
	protected void onDocument() {
		String id = null;
		if (strShowState.equals("列表")) { /*-=notranslate=-*/
			id = getBillListPanel().getHeadBillModel().getValueAt(
					getBillListPanel().getHeadTable().getSelectedRow(), "csaleid").toString();
		} else {
			id = getBillCardPanel().getHeadItem("csaleid").getValue();
		}

		DocumentManager.showDM(this, SaleBillType.SaleOrder, id);
	}

	/**
	 * 修改。
	 * 
	 * 创建日期：(2001-3-17 9:00:09)
	 * 
	 */
	protected void onEdit() {
		SaleOrderVO saleorder = null;

		if (strShowState.equals("列表")) { /*-=notranslate=-*/
			// 设置状态
			if (getBillListPanel().isTempVO()) {
				loadCardDataBYTemp(getBillListPanel().getHeadTable().getSelectedRow());
				saleorder = tempBillVoCache.getTempVOByRow(getBillListPanel().getHeadTable()
						.getSelectedRow());
			} else {
				strShowState = "卡片"; /*-=notranslate=-*/
				switchInterface();
				num = getBillListPanel().getHeadTable().getSelectedRow();

				String csaleid = (String) getBillListPanel().getHeadBillModel().getValueAt(num,
						"csaleid");

				saleorder = vocache.getSaleOrderVO(csaleid);
				loadCardData(saleorder);

				setButtons(getBillButtons());
				setBodyMenuItem(false);
			}
		} else {
			saleorder = vocache.getSaleOrderVO(getCurrentOrderId());
		}

		strState = "修改"; /*-=notranslate=-*/
		setButtonsState();
		getBillCardPanel().setEnabled(true);

		// 恢复单据模板的初始编辑状态
		getBillCardTools().resumeBillItemEditToInit();

		vRowATPStatus = new Vector();
		for (int i = 0; i < getBillCardPanel().getRowCount(); i++) {
			vRowATPStatus.addElement(new UFBoolean(false));

			// 多公司行的编辑项
			ctlUIOnCconsignCorpChg(i);
		}

		if (getBillCardPanel().alInvs == null || getBillCardPanel().alInvs.size() == 0) {
			initInvList();
		}

		setNoEditItem();
		// 单据号
		// getBillCardPanel().getHeadItem("vreceiptcode").setEnabled(false);

		// 收货地址
		UIRefPane vreceiveaddress = (UIRefPane) getBillCardPanel().getHeadItem("vreceiveaddress")
				.getComponent();
		if (vreceiveaddress != null) {
			if (getBillCardPanel().getHeadItem("creceiptcustomerid") != null) {
				// 收货地址参照
				((CustAddrRefModel) vreceiveaddress.getRefModel()).setCustId(getBillCardPanel()
						.getHeadItem("creceiptcustomerid").getValue());
			}
		}
		// 审批未通过的置行状态为修改
		int iStatus = Integer.parseInt(getBillCardPanel().getHeadItem("fstatus").getValue());
		if (iStatus == BillStatus.NOPASS) {
			for (int i = 0; i < getBillCardPanel().getBillModel().getRowCount(); i++) {
				getBillCardPanel().getBillModel().setRowState(i, BillModel.MODIFICATION);
			}
		}

		// by zxj
		// getBillCardPanel().getHeadItem("vreceiptcode").setEnabled(true);
		// 保存修改前单据号
		SaleOrderVO hvo = (SaleOrderVO) getVO(false);
		SaleorderHVO header = (SaleorderHVO) hvo.getParentVO();
		m_oldreceipt = header.getVreceiptcode();

		/** v5.3去掉帐期字段 * */
		/*
		 * if (hvo.getHeadVO().getNaccountperiod() != null &&
		 * hvo.getHeadVO().getNaccountperiod().doubleValue() < 0.0)
		 * getBillCardPanel().setHeadItem("naccountperiod", null);
		 */

		if (SO41.booleanValue()) {
			getBillCardPanel().setTailItem("coperatorid",
					getClientEnvironment().getUser().getPrimaryKey());
		}

		// 保留初始的VO
		getBillCardTools().setOldsaleordervo(hvo);

		try {

			this.alInvs_bak = (ArrayList) nc.vo.scm.pub.smart.ObjectUtils
					.serializableClone(getBillCardPanel().alInvs);
			this.vRowATPStatus_bak = (Vector) nc.vo.scm.pub.smart.ObjectUtils
					.serializableClone(this.vRowATPStatus);

		} catch (Exception e) {
			nc.vo.scm.pub.SCMEnv.error(e);
			this.vRowATPStatus_bak = this.vRowATPStatus;
			this.alInvs_bak = getBillCardPanel().alInvs;
			initInvList();

		}
		// this.alInvs_bak = (ArrayList)this.alInvs.clone();
		// this.vRowATPStatus_bak = (Vector)this.vRowATPStatus.clone();

		// 清除缓存数据
		getBillCardTools().clearCatheData();

		showHintMessage(NCLangRes.getInstance().getStrByID("common", "UCH027"));

		// 加自由项变色龙。
		try {
			InvAttrCellRenderer ficr = new InvAttrCellRenderer();
			ficr.setFreeItemRenderer(getBillCardPanel(), getBillCardPanel().alInvs);
		} catch (Exception e) {

		}

		// 修改时原先手工修改过价格的行变色
		nc.ui.scm.pub.panel.SetColor.setErrorRowColor(getBillCardPanel(),
				getChangePriceRowFromCardUI());

		setCoopEditableByVo(saleorder);

	}

	/**
	 * 返回卡片界面上,手工修改过的价格行的序号。
	 */
	public ArrayList<Integer> getChangePriceRowFromCardUI() {
		// 当前卡片界面行数
		int count = getBillCardPanel().getRowCount();
		ArrayList<Integer> aryrow = new ArrayList<Integer>();
		// 校验每一行是否手工修改过价格，存在一行修改过的即返回
		for (int i = 0; i < count; i++) {
			if (getBillCardPanel().ifModifyPrice(i)) {
				aryrow.add(i);
			}
		}
		return aryrow;
	}

	/**
	 * 定位。
	 * 
	 * 创建日期：(2001-12-4 10:56:17)
	 * 
	 */
	protected void onFind() {

		num = getBillListPanel().getHeadTable().getSelectedRow();
		LocateDialog dlg = new LocateDialog(this, getBillListPanel().getHeadTable());
		dlg.showModal();
		int newnum = getBillListPanel().getHeadTable().getSelectedRow();
		if (newnum >= 0 && newnum < getBillListPanel().getHeadBillModel().getRowCount()) {
			BillEditEvent e = new BillEditEvent(this, num, newnum);
			bodyRowChange(e);
			num = getBillListPanel().getHeadTable().getSelectedRow();
		} else {
			getBillListPanel().getHeadTable().getSelectionModel().setSelectionInterval(
					getBillListPanel().getHeadTable().getSelectedRow(),
					getBillListPanel().getHeadTable().getSelectedRow());
		}

	}

	/**
	 * 修订单据。
	 * 
	 * 创建日期：(2001-4-25 10:48:26) 修改日期：（2003-08-26） 修改内容：修订时，与价格相关字段允许用户修改
	 * 
	 * 修改人：杨涛
	 * 
	 */
	protected void onModification() {

		if (strShowState.equals("列表")) { /*-=notranslate=-*/
			strShowState = "卡片"; /*-=notranslate=-*/
			strState = "自由"; /*-=notranslate=-*/
			switchInterface();
			num = getBillListPanel().getHeadTable().getSelectedRow();

			// 加载数据

			String csaleid = (String) getBillListPanel().getHeadBillModel().getValueAt(num,
					"csaleid");

			SaleOrderVO saleorder = vocache.getSaleOrderVO(csaleid);
			loadCardData(saleorder);

			updateCacheVO();

			setButtons(getBillButtons());
			setBodyMenuItem(false);

		} else {
			updateCacheVO();
		}

		getBillCardPanel().setEnabled(true);

		// 根据参数恢复模板的编辑状态
		getBillCardTools().resumeBillItemEditToInit();
		
		strState = "修订"; /*-=notranslate=-*/

		getBillCardTools().setHeadRefLimit(strState);

		vRowATPStatus = new Vector();
		for (int i = 0; i < getBillCardPanel().getRowCount(); i++) {
			vRowATPStatus.addElement(new UFBoolean(false));

			// 控制多公司行的编辑状态
			ctlUIOnCconsignCorpChg(i);
		}

		if (getBillCardPanel().alInvs == null) {
			initInvList();
		}

		setNoEditItem();

		// 订单修订时设置可编辑性
		setEditWhenModification();

		getBillCardTools().setCardPanelCellEditableByLargess(SO59.booleanValue());

		setNotReviseItems();

		// //显示修订理由
		// getBillCardPanel().showBodyTableCol("veditreason");
		// getBillCardPanel().getBodyItem("veditreason").setEnabled(true);

		SaleOrderVO ordoldvo = (SaleOrderVO) getVO(false);

		/** v5.3去掉帐期字段 * */
		/*
		 * if (ordoldvo.getHeadVO().getNaccountperiod() != null &&
		 * ordoldvo.getHeadVO().getNaccountperiod().doubleValue() < 0.0)
		 * getBillCardPanel().setHeadItem("naccountperiod", null);
		 */

		// 开票客户
		/** 有开票数量，则不允许修改 V502 yangcl zhongwei* */
		Object ntotalinvoicenumber;
		for (int i = 0, len = getBillCardPanel().getRowCount(); i < len; i++) {
			ntotalinvoicenumber = getBillCardPanel().getBodyValueAt(i, "ntotalinvoicenumber");
			if (ntotalinvoicenumber != null && ((UFDouble) ntotalinvoicenumber).doubleValue() > 0) {
				getBillCardPanel().getHeadItem("creceiptcorpid").setEnabled(false);
				break;
			}
		}

		// 保留初始的VO
		getBillCardTools().setOldsaleordervo(ordoldvo);

		try {

			this.alInvs_bak = (ArrayList) nc.vo.scm.pub.smart.ObjectUtils.serializableClone(getBillCardPanel().alInvs);
			this.vRowATPStatus_bak = (Vector) nc.vo.scm.pub.smart.ObjectUtils
					.serializableClone(this.vRowATPStatus);

		} catch (Exception e) {
			nc.vo.scm.pub.SCMEnv.out(e.getMessage());
			this.vRowATPStatus_bak = this.vRowATPStatus;
			this.alInvs_bak = getBillCardPanel().alInvs;
			initInvList();

		}

		// 清除缓存数据
		getBillCardTools().clearCatheData();

		// 加自由项变色龙。
		try {
			InvAttrCellRenderer ficr = new InvAttrCellRenderer();
			ficr.setFreeItemRenderer(getBillCardPanel(), getBillCardPanel().alInvs);
		} catch (Exception e) {

		}

		setButtonsState();

		updateUI();

	}

	/**
	 * 订单修订时设置可编辑性(2004-12-01 23:25:18)
	 * 
	 * @param
	 */
	private void setEditWhenModification() {

		// 1、设置所有不可编辑
		Object otemp = getBillCardPanel().getHeadItem("fstatus").getValue();
		if (otemp == null)
			otemp = "";

		getBillCardTools().setHeadEnable(false);

		getBillCardTools().resumeHeadItemsEditToInit(new String[] { "vreceiptcode", "dbilldate" });
		UFDouble dcathpay = getBillCardTools().getHeadUFDoubleValue("nreceiptcathmny");
		if ((dcathpay == null || dcathpay.doubleValue() == 0) && strState.equals("修订")
				&& isHeadCustCanbeModified()) {/*-=notranslate=-*/
			getBillCardTools().resumeHeadItemsEditToInit(new String[] { "ccustomerid" });
		}

		// Integer fstatus = (Integer)
		// getBillCardPanel().getHeadItem("fstatus").converType(otemp);周长胜修改
		Integer fstatus = SmartVODataUtils.getInteger(otemp);
		UFBoolean binvoicendflag = getBillCardTools().getHeadUFBooleanValue("binvoicendflag");

		// 2． 不能修订的约束：销售订单行结束 OR 销售订单行结算结束 OR 销售订单行开票结束
		if ((fstatus != null && fstatus.intValue() == BillStatus.FINISH)
				|| (binvoicendflag != null && binvoicendflag.booleanValue())) {
			for (int i = 0; i < getBillCardPanel().getRowCount(); i++) {
				getBillCardTools().setRowEnable(i, false);
			}
		}

		String[] bodyItems = { "cinventorycode", "nitemdiscountrate", "ntaxrate",
				"noriginalcurtaxprice", "noriginalcurprice", "noriginalcurnetprice",
				"noriginalcurtaxnetprice", "noriginalcurmny", "noriginalcursummny",
				"noriginalcurtaxmny", "noriginalcurdiscountmny", "blargessflag", "norgqttaxprc",
				"norgqtprc", "norgqttaxnetprc", "norgqtnetprc", "cpriceitem",
				"cpriceitemtablename", "cpricepolicy", "cconsigncorp", "creccalbody",
				"crecwarehouse", "bdericttrans", "crowno", "cadvisecalbody", "cbodywarehousename",
				"ctransmodeid", "cprojectname", "cprojectphasename", "creceiptcorpname",
				"vreceiveaddress", "crecaddrnodename" };

		String[] bodyNoModify = { "cbatchid", "vfree0" };
		// getBillCardTools().setBodyItemEnable(bodyItems,true);
		getBillCardTools().resumeBillBodyItemsEdit(bodyItems);

		for (int i = 0; i < getBillCardPanel().getRowCount(); i++) {
			if (isHasBackwardDoc(i))
				getBillCardTools().setBodyCellsEdit(bodyNoModify, i, false);

			getBillCardTools().setBodyCellsEdit(bodyItems, i, true);
		}
		
		// 币种
		boolean bedit_ccurrencytypeid_h = false;
		// 开票单位
		boolean bedit_creceiptcorpid_h = false;
		// 收货单位
		boolean bedit_creceiptcustomerid_h = false;
		// 收货地址
		boolean bedit_vreceiveaddress_h = false;
		// 备注
		boolean bedit_vnote_h = false;
		// 自定义项
		boolean bedit_vdef_h = false;

		//boolean bedit_nexchangeotobrate_h = false;

		// 2． 除了以上的情况，在其他状态下销售订单都可以进行修订，可以修订的内容：
		//
		// 1) 销售订单未收款 AND 未开票 AND 未结算的情况下的修订：
		//
		// a) 表头可以修订属性：币种、收货单位、收货地点、收货地址、开票单位、备注、自定义项；
		//
		// b) 表体可以修订属性：单价、数量、发货日期、发货时间、收货日期、收货时间、行备注、项目信息、收货单位、收货地区、收货地址、收货地点、
		// 是否价保、是否返利、行备注、项目信息、自定义项
		//
		// 2) 销售订单已收款 OR 已开票OR已结算的情况下的修订：
		//
		// a) 表头可以修订属性：收货单位、收货地点、收货地址、开票单位、备注、自定义项；
		//
		// b) 表体可以修订属性：单价、数量、发货日期、发货时间、收货日期、收货时间、行备注、项目信息、收货单位、收货地区、收货地址、收货地点、
		// 是否价保、是否返利、行备注、项目信息、自定义项

		boolean ispay = false;
		UFDouble nreceiptcathmny = getBillCardTools().getHeadUFDoubleValue("nreceiptcathmny");
		if (nreceiptcathmny != null && nreceiptcathmny.doubleValue() > 0) {
			ispay = true;
		}
		UFDouble ntotalinvoicenumber = null;
		UFDouble ntotalbalancenumber = null;
		UFDouble totalreceiptnumber = null;
		UFBoolean bifinvoicefinish = null;

		UFDouble ntotalinventorynumber = null;

		UFDouble ntalbalancemny = null;
		UFDouble nnumber = null;

		UFDouble nsummny = null;

		String cpackunitid = null;

		String[] keybodys = new String[] {
				// 数量
				"nnumber",
				// 发货日期
				"dconsigndate",
				// 发货时间
				"tconsigntime",
				// 收货日期
				"ddeliverdate",
				// 收货时间
				"tdelivertime",
				// 行备注
				"frownote",
				// 项目
				"cprojectname",
				// 项目阶段
				"cprojectphasename",
				// 收货单位
				"creceiptcorpname",
				// 收货地区
				"creceiptareaname",
				// 收货地址
				"vreceiveaddress",
				// 收货地点
				"crecaddrnodename",
				// 是否价保
				"bsafeprice",
				// 是否返利
				"breturnprofit",
				// 自定义项
				"vdef1", "vdef2", "vdef3", "vdef4", "vdef5", "vdef6", "vdef7", "vdef8", "vdef9",
				"vdef10", "vdef11", "vdef12", "vdef13", "vdef14", "vdef15", "vdef16", "vdef17",
				"vdef18", "vdef19", "vdef20",
				// 无税金额
				"noriginalcurmny",
				// 价税合计
				"noriginalcursummny",
				// 折扣额
				"noriginaldiscountmny",
				// 税率
				"ntaxrate"

		};

		String[] sNoEditItem = new String[] { "GGXX", "ct_code", "ctinvclass", "cpackunitname",
				"cunitname", "cquoteunit", "cconsigncorp", "cadvisecalbody", "cbodywarehousename",
				"cbomordercode", "creceiptareaname", "boosflag", "bsupplyflag", "cbatchid",
				"ccurrencytypename", "isappendant", "crecwarehouse", "bdericttrans", "creccalbody",
				"ntotalshouldoutnum", "ntotalreceivenumber", "ntotalinvoicenumber",
				"ntotalinventorynumber", "ntotalbalancenumber", "ntotalcostmny",
				"bifreceivefinish", "bifinventoryfinish", "frowstatus", "ntalplconsigmny",
				"ntaltransnum", "ntalbalancemny", "dlastconsigdate", "dlastoutdate",
				"dlastinvoicedt", "dlastpaydate", "cinvspec", "cinvtype", "narrangescornum",
				"narrangepoapplynum", "narrangetoornum", "norrangetoapplynum", "narrangemonum",
				"ntotlbalcostnum", "carrangepersonid", "nrushnum" };

		getBillCardTools().setBodyItemEnable(
				new String[] { "norgqttaxprc", "noriginalcurtaxprice", "norgqttaxnetprc",
						"noriginalcurtaxnetprice", "norgqtprc", "norginalcurprice", "norgqtnetprc",
						"noriginalcurnetprice", "npacknumber", "nquoteunitnum", "veditreason" },
				true);
		getBillCardTools().setBodyItemEnable(sNoEditItem, false);
		getBillCardTools().setBodyItemEnable(keybodys, true);

		boolean beditaddr = false;
		String[] keybodyfields = {
		// 发货日期
				"dconsigndate",
				// 发货时间
				"tconsigntime",
				// 收货日期
				// "ddeliverdate",
				// 收货时间
				// "tdelivertime",
				// 收货单位
				"creceiptcorpname",
				// 收货地区
				"creceiptareaname",
				// 收货地址
				"vreceiveaddress",
				// 收货地点
				"crecaddrnodename",

		};

		for (int i = 0; i < getBillCardPanel().getRowCount(); i++) {

			getBillCardPanel().getBillModel().setCellEditable(i, "veditreason", true);

			// 数量nnumber
			nnumber = getBillCardTools().getBodyUFDoubleValue(i, "nnumber");
			nnumber = nnumber == null ? SoVoConst.duf0 : nnumber;

			// 累计开票量
			ntotalinvoicenumber = getBillCardTools().getBodyUFDoubleValue(i, "ntotalinvoicenumber");
			ntotalinvoicenumber = ntotalinvoicenumber == null ? SoVoConst.duf0
					: ntotalinvoicenumber;

			// 累计开票结束
			bifinvoicefinish = getBillCardTools().getBodyUFBooleanValue(i, "bifinvoicefinish");
			bifinvoicefinish = bifinvoicefinish == null ? SoVoConst.buffalse : bifinvoicefinish;

			// 累计结算数量ntotalbalancenumber
			ntotalbalancenumber = getBillCardTools().getBodyUFDoubleValue(i, "ntotalbalancenumber");
			ntotalbalancenumber = ntotalbalancenumber == null ? SoVoConst.duf0
					: ntotalbalancenumber;

			// 累计发货量
			totalreceiptnumber = getBillCardTools().getBodyUFDoubleValue(i, "ntotalreceiptnumber");
			totalreceiptnumber = totalreceiptnumber == null ? SoVoConst.duf0 : totalreceiptnumber;

			// 累计出库数量
			ntotalinventorynumber = getBillCardTools().getBodyUFDoubleValue(i,
					"ntotalinventorynumber");
			ntotalinventorynumber = ntotalinventorynumber == null ? SoVoConst.duf0
					: ntotalinventorynumber;

			if (nnumber.doubleValue() != 0 && totalreceiptnumber.doubleValue() == 0
					&& ntotalinventorynumber.doubleValue() == 0 && !beditaddr) {
				beditaddr = true;
			}

			// 累计结算金额
			ntalbalancemny = getBillCardTools().getBodyUFDoubleValue(i, "ntalbalancemny");
			ntalbalancemny = ntalbalancemny == null ? SoVoConst.duf0 : ntalbalancemny;

			// b本币 价税合计
			nsummny = getBillCardTools().getBodyUFDoubleValue(i, "nsummny");
			nsummny = nsummny == null ? SoVoConst.duf0 : nsummny;

			cpackunitid = getBillCardTools().getBodyStringValue(i, "cpackunitid");

			// 1． 不能修订的约束：销售订单行结束 OR 销售订单行结算结束 OR 销售订单行开票结束
			if ((nnumber.doubleValue() != 0 && ntotalinvoicenumber.abs().compareTo(nnumber.abs()) >= 0)
					|| bifinvoicefinish.booleanValue()
					|| (nnumber.doubleValue() != 0 && ntotalbalancenumber.abs().compareTo(
							nnumber.abs()) >= 0)
					|| (ntalbalancemny.doubleValue() != 0 && ntalbalancemny.abs().compareTo(
							nsummny.abs()) >= 0)) {
				getBillCardTools().setRowEnable(i, false);

				// 销售订单未收款 AND未开票 AND 未结算的情况下：
			} else if (!ispay && ntotalinvoicenumber.compareTo(SoVoConst.duf0) == 0
					&& ntotalbalancenumber.compareTo(SoVoConst.duf0) == 0
					&& ntalbalancemny.compareTo(SoVoConst.duf0) == 0) {
				// 表头可以修订属性：币种、收货单位、收货地点、收货地址、开票单位、备注、自定义项；
				if (!bedit_ccurrencytypeid_h)
					bedit_ccurrencytypeid_h = true;
/*				if (!bedit_nexchangeotobrate_h)
					bedit_nexchangeotobrate_h = true;*/

				// 开票单位
				if (!bedit_creceiptcorpid_h)
					bedit_creceiptcorpid_h = true;
				// 收货单位
				if (!bedit_creceiptcustomerid_h)
					bedit_creceiptcustomerid_h = true;
				// 收货地址
				if (!bedit_vreceiveaddress_h)
					bedit_vreceiveaddress_h = true;
				// 备注
				if (!bedit_vnote_h)
					bedit_vreceiveaddress_h = true;
				// 自定义项
				if (!bedit_vdef_h)
					bedit_vdef_h = true;
				// 表体可以修订属性：单价、数量、发货日期、发货时间、收货日期、收货时间、行备注、项目信息、收货单位、
				// 收货地区、收货地址、收货地点、是否价保、是否返利、行备注、项目信息、自定义项

				if (SA_02.booleanValue()) {

					if (getBillCardPanel().getBodyItem("norgqttaxprc").isShow())
						getBillCardPanel().getBillModel().setCellEditable(i, "norgqttaxprc", true);
					else
						getBillCardPanel().getBillModel().setCellEditable(i,
								"noriginalcurtaxprice", true);

					if (getBillCardPanel().getBodyItem("norgqttaxnetprc").isShow())
						getBillCardPanel().getBillModel().setCellEditable(i, "norgqttaxnetprc",
								true);
					else
						getBillCardPanel().getBillModel().setCellEditable(i,
								"noriginalcurtaxnetprice", true);

				} else {

					if (getBillCardPanel().getBodyItem("norgqtprc").isShow())
						getBillCardPanel().getBillModel().setCellEditable(i, "norgqtprc", true);
					else
						getBillCardPanel().getBillModel().setCellEditable(i, "norginalcurprice",
								true);

					if (getBillCardPanel().getBodyItem("norgqtnetprc").isShow())
						getBillCardPanel().getBillModel().setCellEditable(i, "norgqtnetprc", true);
					else
						getBillCardPanel().getBillModel().setCellEditable(i,
								"noriginalcurnetprice", true);

				}
				if (SoVoTools.isEmptyString(cpackunitid))
					getBillCardPanel().getBillModel().setCellEditable(i, "npacknumber", false);
				else
					getBillCardPanel().getBillModel().setCellEditable(i, "npacknumber", true);
				getBillCardTools().setRowEnable(i, keybodys, true);

				//v5.6去掉
				/*if (getBillCardPanel().getBodyItem("nquoteunitnum").isShow()) {
					getBillCardPanel().getBillModel().setCellEditable(i, "nquoteunitnum", true);
					getBillCardPanel().getBillModel().setCellEditable(i, "nnumber", false);
				} else {
					getBillCardPanel().getBillModel().setCellEditable(i, "nquoteunitnum", false);
					getBillCardPanel().getBillModel().setCellEditable(i, "nnumber", true);
				}*/

			} else if (ispay || ntotalinvoicenumber.compareTo(SoVoConst.duf0) != 0
					|| ntotalbalancenumber.compareTo(SoVoConst.duf0) != 0
					|| ntalbalancemny.compareTo(SoVoConst.duf0) != 0) {
				// 销售订单已收款 OR 已开票OR已结算的情况下的修订：

				// 表头可以修订属性：收货单位、收货地点、收货地址、开票单位、备注、自定义项；
				// 开票单位
				if (!bedit_creceiptcorpid_h)
					bedit_creceiptcorpid_h = true;
				// 收货单位
				if (!bedit_creceiptcustomerid_h)
					bedit_creceiptcustomerid_h = true;
				// 收货地址
				if (!bedit_vreceiveaddress_h)
					bedit_vreceiveaddress_h = true;
				// 备注
				if (!bedit_vnote_h)
					bedit_vreceiveaddress_h = true;
				// 自定义项
				if (!bedit_vdef_h)
					bedit_vdef_h = true;

				// 表体
				// 单价、数量、发货日期、发货时间、收货日期、收货时间、行备注、项目信息、收货单位、
				// 收货地区、收货地址、收货地点、是否价保、是否返利、行备注、项目信息、自定义项
				if (SA_02.booleanValue()) {

					if (getBillCardPanel().getBodyItem("norgqttaxprc").isShow())
						getBillCardPanel().getBillModel().setCellEditable(i, "norgqttaxprc", true);
					else
						getBillCardPanel().getBillModel().setCellEditable(i,
								"noriginalcurtaxprice", true);

					if (getBillCardPanel().getBodyItem("norgqttaxnetprc").isShow())
						getBillCardPanel().getBillModel().setCellEditable(i, "norgqttaxnetprc",
								true);
					else
						getBillCardPanel().getBillModel().setCellEditable(i,
								"noriginalcurtaxnetprice", true);

				} else {

					if (getBillCardPanel().getBodyItem("norgqtprc").isShow())
						getBillCardPanel().getBillModel().setCellEditable(i, "norgqtprc", true);
					else
						getBillCardPanel().getBillModel().setCellEditable(i, "norginalcurprice",
								true);

					if (getBillCardPanel().getBodyItem("norgqtnetprc").isShow())
						getBillCardPanel().getBillModel().setCellEditable(i, "norgqtnetprc", true);
					else
						getBillCardPanel().getBillModel().setCellEditable(i,
								"noriginalcurnetprice", true);

				}

				if (SoVoTools.isEmptyString(cpackunitid))
					getBillCardPanel().getBillModel().setCellEditable(i, "npacknumber", false);
				else
					getBillCardPanel().getBillModel().setCellEditable(i, "npacknumber", true);
				getBillCardTools().setRowEnable(i, keybodys, true);

				if (getBillCardPanel().getBodyItem("nquoteunitnum").isShow()) {
					getBillCardPanel().getBillModel().setCellEditable(i, "nquoteunitnum", true);
					getBillCardPanel().getBillModel().setCellEditable(i, "nnumber", false);
				} else {
					getBillCardPanel().getBillModel().setCellEditable(i, "nquoteunitnum", false);
					getBillCardPanel().getBillModel().setCellEditable(i, "nnumber", true);
				}
			}

			getBillCardTools().setRowEnable(i, keybodyfields, beditaddr);
			getBillCardTools().setRowEnable(i, sNoEditItem, false);

		}

		// 表头
		//部门
		boolean bedit_cdeptid_h = true;
		//销售组织
		boolean bedit_csalecorpid_h = true;
		//库存组织
		boolean bedit_ccalbodyid_h = true;
		getBillCardPanel().getHeadItem("cdeptid").setEdit(bedit_cdeptid_h);
		getBillCardPanel().getHeadItem("csalecorpid").setEdit(bedit_csalecorpid_h);
		getBillCardPanel().getHeadItem("ccalbodyid").setEdit(bedit_ccalbodyid_h);
		

		getBillCardPanel().getHeadItem("ccurrencytypeid").setEnabled(bedit_ccurrencytypeid_h);
		getBillCardPanel().getHeadItem("ccurrencytypeid").setEdit(bedit_ccurrencytypeid_h);

/*		getBillCardPanel().getHeadItem("nexchangeotobrate").setEnabled(bedit_nexchangeotobrate_h);
		getBillCardPanel().getHeadItem("nexchangeotobrate").setEdit(bedit_nexchangeotobrate_h);*/

		getBillCardPanel().getHeadItem("creceiptcorpid").setEnabled(bedit_creceiptcorpid_h);
		getBillCardPanel().getHeadItem("creceiptcorpid").setEdit(bedit_creceiptcorpid_h);

		getBillCardPanel().getHeadItem("creceiptcustomerid").setEnabled(bedit_creceiptcustomerid_h);
		getBillCardPanel().getHeadItem("creceiptcustomerid").setEdit(bedit_creceiptcustomerid_h);

		getBillCardPanel().getHeadItem("vreceiveaddress").setEnabled(bedit_vreceiveaddress_h);
		getBillCardPanel().getHeadItem("vreceiveaddress").setEdit(bedit_vreceiveaddress_h);

		getBillCardPanel().getHeadItem("vnote").setEnabled(bedit_vnote_h);
		getBillCardPanel().getHeadItem("vnote").setEdit(bedit_vnote_h);

		getBillCardPanel().getHeadItem("creceiptcustomerid").setEnabled(beditaddr);
		getBillCardPanel().getHeadItem("creceiptcustomerid").setEdit(beditaddr);

		getBillCardPanel().getHeadItem("vreceiveaddress").setEnabled(beditaddr);
		getBillCardPanel().getHeadItem("vreceiveaddress").setEdit(beditaddr);

		String[] keysh = new String[] { "vdef1", "vdef2", "vdef3", "vdef4", "vdef5", "vdef6",
				"vdef7", "vdef8", "vdef9", "vdef10", "vdef11", "vdef12", "vdef13", "vdef14",
				"vdef15", "vdef16", "vdef17", "vdef18", "vdef19", "vdef20" };
		getBillCardTools().setHeadEnable(keysh, bedit_vdef_h);

		// 显示修订理由
		getBillCardPanel().showBodyTableCol("veditreason");
		getBillCardPanel().getBodyItem("veditreason").setEnabled(true);

		// 折扣额
		getBillCardTools().setBodyItemEnable(
				new String[] { "noriginalcurdiscountmny", "ndiscountmny" }, false);

	}

	/**
	 * 华孚需求：设置模板中不能修订的项
	 */
	private void setNotReviseItems() {

		BillItem[] bHeaditems = getBillCardPanel().getHeadItems();

		// 不能修订才置为不能编辑状态
		for (int i = 0; i < bHeaditems.length; i++) {

			if (!bHeaditems[i].isM_bReviseFlag())
				bHeaditems[i].setEnabled(false);
			else if (bHeaditems[i].getKey() != null && bHeaditems[i].getKey().startsWith("vdef")) {
				bHeaditems[i].setEnabled(true);
			}

		}
		BillItem[] bBodyitems = getBillCardPanel().getBodyItems();
		for (int i = 0; i < bBodyitems.length; i++) {
			if (!bBodyitems[i].isM_bReviseFlag()) {
				for (int j = 0, jLen = getBillCardPanel().getRowCount(); j < jLen; j++) {
					getBillCardPanel().setCellEditable(j, bBodyitems[i].getKey(), false);
				}

			} else if (bBodyitems[i].getKey().startsWith("vdef")) {
				for (int j = 0, jLen = getBillCardPanel().getRowCount(); j < jLen; j++) {
					getBillCardPanel().setCellEditable(j, bBodyitems[i].getKey(), true);
				}
			}
		}

	}

	protected void billSplitUIEntry(SaleOrderVO[] saleOrderVOs) {
		tempBillVoCache.initTempBillMap(saleOrderVOs);
		autoSwitchPanel();
		loadTempDataTOPanel();

	}

	public void loadCardDataBYTemp(int row) {
		try {
			autoSwitchPanel();
			SaleOrderVO vo = tempBillVoCache.getTempVOByRow(row);
			coBusiType = vo.getBizTypeid();
			coPoOrderId = vo.getHeadVO().getCoopPoId();
			coPoTs = vo.getHeadVO().getCoopPoTs();
			// CHG21TO30 voChanger=new CHG21TO30();
			// saleOrderVOs=new SaleOrderVO[saleOrderVOs.length];
			// saleOrderVOs=(SaleOrderVO[])voChanger.retChangeBusiVOs(saleOrderVOs,
			// saleOrderVOs);
			binitOnNewByOther = true;
			getBillCardPanel().setHeadItem("cbiztype", coBusiType);
			// saleOrderVOs[0].getHeadVO().getAttributeValue("ccooppohid");
			onNewByOther(new SaleOrderVO[] { vo });

			for (int i = 0; i < getBillCardPanel().getRowCount(); i++)
				vRowATPStatus.add(new UFBoolean(false));

			setCoopEditableByVo(vo);
		} catch (Exception e) {
			SCMEnv.error(e);
			showErrorMessage(e.getMessage());
		}

	}

	private void loadTempDataTOPanel() {
		getBillListPanel().setTempBillVoCache(tempBillVoCache);
		getBillListPanel().loadDataByTempHeadVos(tempBillVoCache.getHeadTempVOs());
	}

	private void clearTempData() {
		tempBillVoCache.clear();
		getBillListPanel().getHeadBillModel().clearBodyData();
		getBillListPanel().getBodyBillModel().clearBodyData();
	}

	/**
	 * 销售订单拉式协同采购订单
	 * 
	 * @param bo
	 * @throws Exception
	 */
	protected void onCoRefPo(ButtonObject bo) throws Exception {
		// SaleOrderBO_Client.setSoRefferedByPO(new
		// String[]{"1001D41000000000181P"},false);
		if (PfUIDataCache.getBillType("21") == null) {
			showErrorMessage("未安装或启用采购模块，无法进行购销协同。");
			return;
		}
		if (strShowState.equals("列表")){
		
			getBillCardPanel().getBillData().clearViewData();
			getBillCardPanel().setHeadItem("vreceiptcode", "");
			getBillCardPanel().updateUI();
		}

		switchToCardPanel();
		PfUtilClient.childButtonClicked(bo, getCorpPrimaryKey(), getNodeCode(),
				getClientEnvironment().getUser().getPrimaryKey(), getBillType(), this);
		vRowATPStatus = new Vector();
		boolean bisCalculate = getBillCardPanel().getBillModel().isNeedCalculate();
		// AggregatedValueObject[] pursueVOs=null;
		SaleOrderVO[] saleOrderVOs = null;
		try {
			if (PfUtilClient.isCloseOK()) {

				saleOrderVOs = ((PfUtilClient.getRetVos() == null) || (((Object[]) PfUtilClient
						.getRetVos()).length > 0 && !(((Object[]) PfUtilClient.getRetVos())[0] instanceof SaleOrderVO))) ? null
						: (SaleOrderVO[]) PfUtilClient.getRetVos();

			} else {
				return;

			}

			if (saleOrderVOs != null && saleOrderVOs.length == 1) {
				if (tempBillVoCache.getSize() > 0) {
					clearTempData();
				}

				coBusiType = saleOrderVOs[0].getBizTypeid();
				coPoOrderId = saleOrderVOs[0].getHeadVO().getCoopPoId();
				coPoTs = saleOrderVOs[0].getHeadVO().getCoopPoTs();
				// CHG21TO30 voChanger=new CHG21TO30();
				// saleOrderVOs=new SaleOrderVO[saleOrderVOs.length];
				// saleOrderVOs=(SaleOrderVO[])voChanger.retChangeBusiVOs(saleOrderVOs,
				// saleOrderVOs);
				binitOnNewByOther = true;
				// saleOrderVOs[0].getHeadVO().getAttributeValue("ccooppohid");
				onNewByOther(saleOrderVOs);

				for (int i = 0; i < getBillCardPanel().getRowCount(); i++)
					vRowATPStatus.add(new UFBoolean(false));

				setCoopEditableByVo(saleOrderVOs[0]);
			} else if (saleOrderVOs != null && saleOrderVOs.length > 1) {
				billSplitUIEntry(saleOrderVOs);

			} else {
				return;
			}

			getBillCardPanel().setHeadItem("cbiztype", coBusiType);
			getBillCardPanel().setBusiType(coBusiType);

			// 加自由项变色龙。
			InvAttrCellRenderer ficr = new InvAttrCellRenderer();
			ficr.setFreeItemRenderer(getBillCardPanel(), this.getBillCardPanel().alInvs);
		} catch (Exception e) {
			throw e;
		} finally {
			getBillCardPanel().getBillModel().setNeedCalculate(bisCalculate);
			if (bisCalculate)
				getBillCardPanel().getBillModel().reCalcurateAll();
			binitOnNewByOther = false;

		}

		// 清除缓存数据
		getBillCardTools().clearCatheData();

	}

	/**
	 * 设置协同单据的可操作性
	 * 
	 * @param saleorder
	 */
	private void setCoopEditableByVo(SaleOrderVO saleorder) {
		if (saleorder.getHeadVO().getAttributeValue("ccooppohid") != null) {
			getBillCardPanel().getHeadItem("ccustomerid").setEdit(false);
		}
	}

	/**
	 * @return 卡片、列表界面上是否有选中行
	 */
	private boolean ifHasData() {
		boolean flag = true;
		if (strShowState.equals("列表")) {
			if (getBillListPanel().getHeadTable().getSelectedRow() == -1)
				flag = false;
		} else
			flag = getBillCardPanel().getHeadItem("csaleid").getValueObject() != null;
		return flag;
	}

	private String getCurrentOrderId() {
		String csaleid = null;
		if (strShowState.equals("列表")) {
			csaleid = (String) getBillListPanel().getHeadBillModel().getValueAt(selectRow,
					"csaleid");
		} else {
			csaleid = getBillCardPanel().getHeadItem("csaleid").getValue();
		}
		return csaleid;

	}

	private SaleOrderVO getCurrentSaleOrderVO() {
		return vocache.getSaleOrderVO(getCurrentOrderId());

	}

	/**
	 * 销售订单推采购订单
	 */
	protected void onCoPushPo() {
		try {

			if (PfUIDataCache.getBillType("21") == null) {
				showErrorMessage("未安装或启用采购模块，无法进行购销协同。");
				return;
			}
			String sSaleid = getCurrentOrderId();
			if (sSaleid != null) {
				SaleOrderVO saleorder = vocache.getSaleOrderVO(sSaleid);
				if (saleorder.getHeadVO().getAttributeValue("bcooptopo") != null
						&& ((UFBoolean) saleorder.getHeadVO().getAttributeValue("bcooptopo"))
								.booleanValue()) {
					showErrorMessage("该销售订单已协同生成采购订单。");
					return;
				}
				if (saleorder.isCoopped()) {
					showErrorMessage("该销售订单已有协同采购订单。");
					return;

				}

				if (BillStatus.AUDIT != saleorder.getHeadVO().getFstatus()
						|| saleorder.hasRowClosed()) {
					showErrorMessage("只有审批后并且未有行关闭的销售订单才能进行此操作。");
					return;

				}

				PfUtilClient.processActionNoSendMessage(this, "SALETOPO", "21",
						getClientEnvironment().getDate().toString(), saleorder, null, null, null);
				// SaleOrderBO_Client.setSoRefferedByPO(new String[] { sSaleid
				// }, true);
				// saleorder.getHeadVO().setAttributeValue("bcooptopo", new
				// UFBoolean(true));
				// getBillCardPanel().setHeadItem("bcooptopo", new
				// UFBoolean(true));
				new BillTools().reLoadBillState(this, getClient());
				getCurrentSaleOrderVO().getHeadVO().setAttributeValue("bcooptopo",
						new UFBoolean(true));
				getCurrentSaleOrderVO().getHeadVO().setAttributeValue("ccooppohid",
						getBillCardPanel().getHeadItem("ccooppohid").getValue());
				setBtnsByBillState(BillStatus.AUDIT);

				// updateCacheVO();
				showHintMessage("该销售订单已成功协同生成采购订单");
			} else {
				showErrorMessage("请选择记录后再操作。");
				return;
			}
		} catch (Exception e) {
			SCMEnv.error(e);
			showErrorMessage(e.getMessage());
		}

	}

	
	
	//private java.util.Map<String, java.util.ArrayList<UIMenuItem>> m_bodyMenuItems;
	/**
	 * 
	 * 描述：获得卡片界面下表体行的右击菜单条目的集合
	 * <p>
	 * <b>参数说明</b>
	 * 
	 * @return 右击菜单集合
	 *         <p>
	 * @author duy
	 * @time 2008-12-11 上午09:42:55 修改：添加renew 标记（是否必须更新按钮组，在退货和订单间切换时应用）liuzwc
	 *       2009-05-18
	 */
	private java.util.Map<String, java.util.ArrayList<UIMenuItem>> getBodyMenuItems(boolean renew) {
		java.util.Map<String, java.util.ArrayList<UIMenuItem>> m_bodyMenuItems = null;
		if ((m_bodyMenuItems == null && boLine != null) || renew) {
			ButtonObject[] lineButtons = null;
			String[] tabCodes = getBillCardPanel().getBillData().getTableCodes(BillItem.BODY);
			m_bodyMenuItems = new java.util.HashMap<String, java.util.ArrayList<UIMenuItem>>();
			if (b_refundment) {// 修改：退货只有删除按钮 liuzwc 2009-05-18
				lineButtons = new ButtonObject[] { boDelLine };
			} else {
				lineButtons = boLine.getChildButtonGroup();
			}

			for (String tabCode : tabCodes) {
				java.util.ArrayList<UIMenuItem> items = new java.util.ArrayList<UIMenuItem>(
						lineButtons.length);
				for (ButtonObject lineButton : lineButtons) {
					SoUIMenuItem menuItem = new SoUIMenuItem(lineButton);
					items.add(menuItem);
				}
				m_bodyMenuItems.put(tabCode, items);
			}
		}
		return m_bodyMenuItems;
	}

	/**
	 * 
	 * 描述：设置表体菜单项
	 * <p>
	 * <b>参数说明</b>
	 * <p>
	 * 
	 * @author duy
	 * @time 2008-12-11 上午11:28:33 修改：添加renew 标记（是否必须更新按钮组，在退货和订单间切换时应用）liuzwc
	 *       2009-05-18
	 */
	protected void setBodyMenuItem(boolean renew) {
		String[] tabCodes = getBillCardPanel().getBillData().getTableCodes(BillItem.BODY);
		java.util.Map<String, java.util.ArrayList<UIMenuItem>> menuItems = getBodyMenuItems(renew);
		for (String tabCode : tabCodes) {
			java.util.ArrayList<UIMenuItem> items = menuItems.get(tabCode);
			UIMenuItem[] menus = items.toArray(new UIMenuItem[items.size()]);
			getBillCardPanel().setBodyMenu(tabCode, menus);
			getBillCardPanel().addMenuListener(menus);
		}
	}

	/**
	 * 
	 * 描述：设置表体菜单项的状态
	 * <p>
	 * <b>参数说明</b>
	 * <p>
	 * 
	 * @author duy
	 * @time 2008-12-11 上午10:32:27
	 */
	private void setBodyMenuStatus() {
		String[] tabCodes = getBillCardPanel().getBillData().getTableCodes(BillItem.BODY);
		for (String tabCode : tabCodes)
			getBillCardPanel().setBodyMenuShow(tabCode, boLine.isEnabled());

		if (!boLine.isEnabled())
			return;

		for (String tabCode : tabCodes) {
			UIMenuItem[] menuItems = getBillCardPanel().getBodyMenuItems(tabCode);
			if (menuItems != null) {
				for (UIMenuItem menuItem : menuItems) {
					if (menuItem instanceof SoUIMenuItem) {
						((SoUIMenuItem) menuItem).setEnabled(((SoUIMenuItem) menuItem)
								.getButtonObject().isEnabled());
						menuItem.updateUI();
					}
				}
			}
		}

		getBillCardPanel().getAddLineMenuItem().setEnabled(boAddLine.isEnabled());
		getBillCardPanel().getDelLineMenuItem().setEnabled(boDelLine.isEnabled());
		getBillCardPanel().getPasteLineMenuItem().setEnabled(boPasteLine.isEnabled());
		getBillCardPanel().getCopyLineMenuItem().setEnabled(boCopyLine.isEnabled());
		getBillCardPanel().getInsertLineMenuItem().setEnabled(boInsertLine.isEnabled());
		getBillCardPanel().getPasteLineToTailMenuItem().setEnabled(boPasteLine.isEnabled());
	}

	protected void switchToCardPanel() {
		// 切换到卡片
		if (strShowState.equals("列表")) {
			strShowState = "卡片";
			strState = "自由";
			switchInterface();
			setButtons(getBillButtons());
			setBodyMenuItem(false);
			updateUI();
		}

	}

	/**
	 * 切换界面。
	 * 
	 * 创建日期：(2001-10-25 12:34:10)
	 * 
	 */
	protected void autoSwitchPanel() {
		if (strShowState.equals("卡片")) { /*-=notranslate=-*/
			strShowState = "列表";
			remove(getSplitPanelBc());
			add(getBillListPanel(), "Center");
			setCurShowPanel(getBillListPanel());
			// 隐藏捆绑
			getBillListPanel().hideBodyTableCol("bbindflag");
		} else {
			strShowState = "卡片";
			remove(getBillListPanel());
			add(getSplitPanelBc(), "Center");
			setCurShowPanel(getSplitPanelBc());
			// 无论模板和参数如何设置，本币和辅币均不可编辑
			getBillCardTools().setBodyItemEnable(
					SOBillCardTools.getSaleOrderItems_Price_Mny_NoOriginal(), false);
			// 隐藏捆绑
			getBillCardPanel().hideBodyTableCol("bbindflag");
		}
		setButtons(getBillButtons());

		updateUI();
	}

	/**
	 * 新增单据。
	 * 
	 * 创建日期：(2001-4-21 14:11:14)
	 * 
	 */
	protected void onNew(ButtonObject bo) throws Exception {
		
		/**新订单表头ID(解决保存时，拔掉网线导致数据重复的问题)*/
		clearOID();
		/**新订单表头ID(解决保存时，拔掉网线导致数据重复的问题)*/
		
		// 切换到卡片
		if (strShowState.equals("列表")) {
			strShowState = "卡片";
			strState = "自由";
			switchInterface();
			setButtons(getBillButtons());
			updateUI();
		}

		// 销售不走询价：SA15--N,恢复模板的默认值
		if (!SA_15.booleanValue())
			getBillCardTools().resumeBillBodyItemsEdit(
					getBillCardTools().getSaleOrderItems_Price_Original());

		PfUtilClient.childButtonClicked(bo, getCorpPrimaryKey(), getNodeCode(),
				getClientEnvironment().getUser().getPrimaryKey(), getBillType(), this);
		vRowATPStatus = new Vector();

		boolean bisCalculate = getBillCardPanel().getBillModel().isNeedCalculate();
		getBillCardPanel().getBillModel().setNeedCalculate(false);

		try {
			if (nc.ui.pub.pf.PfUtilClient.makeFlag) {
				onNewBySelf();
			} else {
				if (PfUtilClient.isCloseOK()) {
					binitOnNewByOther = true;

					onNewByOther(PfUtilClient.getRetVos());
					// 可用量状态
					for (int i = 0; i < getBillCardPanel().getRowCount(); i++)
						vRowATPStatus.add(new UFBoolean(false));
				}else{
					getBillCardPanel().setEnabled(false);
					return;
				}
			}

			// 加自由项变色龙。
			InvAttrCellRenderer ficr = new InvAttrCellRenderer();
			ficr.setFreeItemRenderer(getBillCardPanel(), this.getBillCardPanel().alInvs);

		} catch (Exception e) {
			throw e;
		} finally {
			getBillCardPanel().getBillModel().setNeedCalculate(bisCalculate);
			if (bisCalculate)
				getBillCardPanel().getBillModel().reCalcurateAll();
			binitOnNewByOther = false;

		}

		// 设置表体菜单
		setBodyMenuItem(false);

		// 清除缓存数据
		getBillCardTools().clearCatheData();
	}

	/**
	 * 通过其它单据新增单据。
	 * 
	 * 创建日期：(2001-4-21 14:11:14)
	 * 
	 */
	protected void onNewByOther(AggregatedValueObject[] saleVOs) throws Exception {
		
	    //创建时间工具类实例
		nc.vo.scm.pu.Timer timer = new nc.vo.scm.pu.Timer();
		timer.start();
		
		if (saleVOs == null || saleVOs.length == 0)
			return;

		// 检查合并的条件
		if (!checkSourceComb(saleVOs))
			return;
		// 构造合并的来源业务VO
		// long s = System.currentTimeMillis();
		// SCMEnv.out("onNewByOther1开始...");

		SaleOrderVO saleVO = new SaleOrderVO();
		saleVO.setParentVO(saleVOs[0].getParentVO());

		saleVO.getHeadVO().setCapproveid(null);

		Vector vecTemp = new Vector();
		for (int i = 0; i < saleVOs.length; i++) {
			for (int j = 0; j < saleVOs[i].getChildrenVO().length; j++) {
				vecTemp.addElement(saleVOs[i].getChildrenVO()[j]);
			}
		}
		SaleorderBVO[] aryChildren = new SaleorderBVO[vecTemp.size()];
		if (vecTemp.size() > 0)
			vecTemp.copyInto(aryChildren);
		// 处理计划发货时间和要求收货时间
		for (SaleorderBVO bVO : aryChildren) {
			if ("".equals(bVO.getAttributeValue("tconsigntime"))
					|| null == bVO.getAttributeValue("tconsigntime"))
				bVO.setAttributeValue("tconsigntime", "00:00:00");
			if ("".equals(bVO.getAttributeValue("tdelivertime"))
					|| null == bVO.getAttributeValue("tdelivertime"))
				bVO.setAttributeValue("tdelivertime", "00:00:00");

		}
		saleVO.setChildrenVO(aryChildren);
		// 处理新增界面
		strState = "新增";
		getBillCardPanel().addNew();
		getBillCardPanel().setEnabled(true);

		getBillCardTools().setHeadRefLimit(strState);

		// 金额随币种精度变化
		Object oCurrency = saleVO.getChildrenVO()[0].getAttributeValue("ccurrencytypeid");
		try {
			if (oCurrency == null || oCurrency.toString().length() == 0) {
				for (int i = 0; i < saleVO.getChildrenVO().length; i++) {
					saleVO.getChildrenVO()[i].setAttributeValue("ccurrencytypeid", CurrParamQuery
							.getInstance().getLocalCurrPK(getCorpPrimaryKey()));
				}
			}
		} catch (Exception e) {
			SCMEnv.out(e);
		}
		oCurrency = saleVO.getChildrenVO()[0].getAttributeValue("ccurrencytypeid");
		if (oCurrency != null && oCurrency.toString().length() != 0) {
			getBillCardPanel().setPanelByCurrency(oCurrency.toString());
		}

		// 增加单据行号
		nc.ui.scm.pub.report.BillRowNo.setVORowNoByRule(saleVO, SaleBillType.SaleOrder,
				getBillCardPanel().getRowNoItemKey());

		// 界面缓存初始化VO
		this.initAvo = saleVO;

		getBillCardPanel().getBillModel().setNeedCalculate(false);
		
		// 加载数据
		getBillCardPanel().setBillValueVO(saleVO);

		// 处理默认数据
		ArrayList formulaslist = new ArrayList();
		// 开票单位
		String creceiptcorpid = getBillCardPanel().getHeadItem("creceiptcorpid").getValue();
		if (creceiptcorpid == null || creceiptcorpid.trim().length() <= 0)
			formulaslist
					.add("creceiptcorpid->getColValue(bd_cumandoc,pk_cusmandoc2,pk_cumandoc,ccustomerid)");
		// 收货单位
		String creceiptcustomerid = getBillCardPanel().getHeadItem("creceiptcustomerid") == null ? null
				: getBillCardPanel().getHeadItem("creceiptcustomerid").getValue();
		if (creceiptcustomerid == null || creceiptcustomerid.trim().length() <= 0)
			formulaslist
					.add("creceiptcustomerid->getColValue(bd_cumandoc,pk_cusmandoc3,pk_cumandoc,ccustomerid)");
		// 部门
		String cdeptid = getBillCardPanel().getHeadItem("cdeptid").getValue();
		if (cdeptid == null || cdeptid.trim().length() <= 0)
			formulaslist
					.add("cdeptid->getColValue(bd_cumandoc,pk_respdept1,pk_cumandoc,ccustomerid)");
		// 业务员
		String cemployeeid = getBillCardPanel().getHeadItem("cemployeeid").getValue();
		if (cemployeeid == null || cemployeeid.trim().length() <= 0)
			formulaslist
					.add("cemployeeid->getColValue(bd_cumandoc,pk_resppsn1,pk_cumandoc,ccustomerid)");
		// 整单扣率
		UFDouble ndiscountrate = getBillCardTools().getHeadUFDoubleValue("ndiscountrate");
		if (ndiscountrate == null)
			formulaslist
					.add("ndiscountrate->getColValue(bd_cumandoc,discountrate,pk_cumandoc,ccustomerid)");

		// String ctransmodeid =
		// getBillCardPanel().getHeadItem("ctransmodeid").getValue();
		// if (ctransmodeid == null || ctransmodeid.trim().length() <= 0)
		// formulaslist.add("ctransmodeid->getColValue(bd_cumandoc,pk_sendtype,pk_cumandoc,ccustomerid)");

		// 默认交易币种
		String ccurrencytypeid = getBillCardPanel().getHeadItem("ccurrencytypeid").getValue();
		if (ccurrencytypeid == null || ccurrencytypeid.trim().length() <= 0)
			formulaslist
					.add("ccurrencytypeid->getColValue(bd_cumandoc,pk_currtype1,pk_cumandoc,ccustomerid)");
		// 收付款协议
		String ctermprotocolid = getBillCardPanel().getHeadItem("ctermprotocolid").getValue();
		if (ctermprotocolid == null || ctermprotocolid.trim().length() <= 0)
			formulaslist
					.add("ctermprotocolid->getColValue(bd_cumandoc,pk_payterm,pk_cumandoc,ccustomerid)");
		// 库存组织
		String ccalbodyid = getBillCardPanel().getHeadItem("ccalbodyid").getValue();
		if (ccalbodyid == null || ccalbodyid.trim().length() <= 0)
			formulaslist
					.add("ccalbodyid->getColValue(bd_cumandoc,pk_calbody,pk_cumandoc,ccustomerid)");
		
		// 销售组织
		String csalecorpid = getBillCardPanel().getHeadItem("csalecorpid").getValue();
		if (csalecorpid == null || csalecorpid.trim().length() <= 0)
			formulaslist
					.add("csalecorpid->getColValue(bd_cumandoc,pk_salestru,pk_cumandoc,ccustomerid)");

		/** v5.3去掉帐期字段 * */
		/*
		 * formulaslist
		 * .add("naccountperiod->getColValue(bd_cumandoc,acclimit,pk_cumandoc,ccustomerid)");
		 */

		// 客户基本档案
		String ccustbasid = getBillCardPanel().getHeadItem("ccustbasid").getValue();
		if (ccustbasid == null || ccustbasid.trim().length() <= 0)
			formulaslist
					.add("ccustbasid->getColValue(bd_cumandoc,pk_cubasdoc,pk_cumandoc,ccustomerid)");

		// 散户标志
		UFBoolean bfreecustflag = getBillCardTools().getHeadUFBooleanValue("bfreecustflag");
		if (bfreecustflag == null)
			formulaslist
					.add("bfreecustflag->getColValue(bd_cubasdoc,freecustflag,pk_cubasdoc,ccustbasid)");

		// 预收款比例
		Object prepaidratio = getBillCardPanel().getHeadItem("npreceiverate").getValueObject();
		if (prepaidratio == null || prepaidratio.toString().trim().length() == 0) {
			formulaslist
					.add("npreceiverate->getColValue(bd_cumandoc,prepaidratio,pk_cumandoc,ccustomerid)");
		}

		if (formulaslist.size() > 0)
			getBillCardPanel().getBillData().execHeadFormulas(
					(String[]) formulaslist.toArray(new String[formulaslist.size()]));

		// 如未设置收货单位，则取客户
		if (getBillCardPanel().getHeadItem("creceiptcustomerid") != null
				&& (getBillCardPanel().getHeadItem("creceiptcustomerid").getValue() == null || getBillCardPanel()
						.getHeadItem("creceiptcustomerid").getValue().length() <= 0)) {
			getBillCardPanel().getHeadItem("creceiptcustomerid").setValue(
					getBillCardPanel().getHeadItem("ccustomerid").getValue());
		}
		// 如未设置开票单位，则取客户
		if (getBillCardPanel().getHeadItem("creceiptcorpid").getValue() == null
				|| getBillCardPanel().getHeadItem("creceiptcorpid").getValue().length() <= 0) {
			getBillCardPanel().getHeadItem("creceiptcorpid").setValue(
					getBillCardPanel().getHeadItem("ccustomerid").getValue());
		}

		List<String> list = new ArrayList<String>();
		list.add("cinvbasdocid->getColValue(bd_invmandoc,pk_invbasdoc,pk_invmandoc,cinventoryid)");
		list.add("laborflag->getColValue(bd_invbasdoc,laborflag,pk_invbasdoc,cinvbasdocid)");
		list.add("discountflag->getColValue(bd_invbasdoc,discountflag,pk_invbasdoc,cinvbasdocid)");
		list.add("cprolineid->getColValue(bd_invbasdoc,pk_prodline,pk_invbasdoc,cinvbasdocid)");
		list.add("cprolinename->getColValue(bd_prodline,prodlinename,pk_prodline,cprolineid)");
		list.add("nreturntaxrate->getColValue(bd_invmandoc,expaybacktax,pk_invmandoc,cinventoryid)");
		list.add("cpackunitname->getColValue(bd_measdoc,measname,pk_measdoc,cpackunitid);");
		list.add("cquoteunit->getColValue(bd_measdoc,measname,pk_measdoc,cquoteunitid);");
		list.add("wholemanaflag->getColValue(bd_invmandoc,wholemanaflag,pk_invmandoc,cinventoryid)");
		list.add("isconfigable->getColValue(bd_invmandoc,isconfigable,pk_invmandoc,cinventoryid)");
		list.add("isspecialty->getColValue(bd_invmandoc,isspecialty,pk_invmandoc,cinventoryid)" );
		//改为VO对照处理
		//"ct_name->getColValue(ct_manage,ct_name,pk_ct_manage,csourcebillid)" };
		// 合同存货类名称
		if (getSouceBillType().equals(SaleBillType.SoContract)
				|| getSouceBillType().equals(SaleBillType.SoInitContract)) 
			list.add("ctinvclass->getColValue(bd_invcl,INVCLASSCODE,pk_invcl,ctinvclassid)");
		// 产品线税率
		if (getSouceBillType().equals("4H") || getSouceBillType().equals("42")){
			list.add("ctaxitemid->getColValue(bd_invbasdoc,pk_taxitems,pk_invbasdoc,cinvbasdocid)");
			list.add("ntaxrate->getColValue(bd_taxitems,taxratio,pk_taxitems,ctaxitemid)");
		}
		
		// 表体显示公式
		BillItem[] bms = getBillCardPanel().getBillModel().getBodyItems();
		for (int i = 0; i < bms.length; i++) {
			if (bms[i].getLoadFormula() != null && bms[i].getLoadFormula().length > 0) {
				if ( !("cpackunitid".equals(bms[i].getKey())||"csourcebillid".equals(bms[i].getKey())
						||"ct_manageid".equals(bms[i].getKey())||"cquoteunitid".equals(bms[i].getKey()))){
					for (int j=0,len=bms[i].getLoadFormula().length;j<len;j++)
						list.add(bms[i].getLoadFormula()[j]);
				}
			}
		}
	    // 执行表体公式
		String[] bodyformulas = list.toArray(new String[list.size()]);
		getBillCardPanel().getBillModel().execFormulas(bodyformulas);

		getBillCardTools().setDefaultValueByTemplate(0, -1);

		// 新增单据时设置表头默认数据。
		setDefaultData(false);

		// 设置换算率 add:liuzwc 
		//已经在setDefaultData(false)中的getBillCardPanel().initUnit()处理
		// setScalefactor();
		
		if (getSouceBillType().equals(SaleBillType.SoContract)
				|| getSouceBillType().equals(SaleBillType.SoInitContract)) {

			if (getBillCardPanel().getHeadItem("creceiptcorpid").getValue() == null
					|| getBillCardPanel().getHeadItem("creceiptcorpid").getValue().length() <= 0) {
				getBillCardPanel().getHeadItem("creceiptcorpid").setValue(
						getBillCardPanel().getHeadItem("ccustomerid").getValue());
			}

			getBillCardPanel().afterCcalbodyidEdit(null);
			getBillCardPanel().afterCreceiptcorpEdit(null);
		}
		// 报价单
		else if (getSouceBillType().equals(SaleBillType.SaleQuotation)) {

			if (getBillCardPanel().getHeadItem("creceiptcorpid").getValue() == null
					|| getBillCardPanel().getHeadItem("creceiptcorpid").getValue().length() <= 0) {
				getBillCardPanel().getHeadItem("creceiptcorpid").setValue(
						getBillCardPanel().getHeadItem("ccustomerid").getValue());
			}

			// 恢复报价单上的部门
			if (saleVO.getHeadVO().getCdeptid() != null)
				getBillCardPanel().getHeadItem("cdeptid").setValue(saleVO.getHeadVO().getCdeptid());

			getBillCardPanel().afterCcalbodyidEdit(null);
			getBillCardPanel().afterCreceiptcorpEdit(null);
			// 计算辅计量无税单价,辅计量含税单价
			// BillTools.calcViaPriceAll(getBillCardPanel().getBillModel(),getBillCardPanel().getBillType());
		}
		// 借出转销售
		else if (getSouceBillType().equals("4H") || getSouceBillType().equals("42")) {
			//
			getBillCardPanel().getHeadItem("ccalbodyid").setEnabled(false);
			getBillCardPanel().getBodyItem("cadvisecalbody").setEnabled(false);
			getBillCardPanel().getBodyItem("cbodywarehousename").setEnabled(false);

			if (getBillCardPanel().getHeadItem("creceiptcorpid").getValue() == null
					|| getBillCardPanel().getHeadItem("creceiptcorpid").getValue().length() <= 0) {
				getBillCardPanel().getHeadItem("creceiptcorpid").setValue(
						getBillCardPanel().getHeadItem("ccustomerid").getValue());
			}
		} else if (getBillType().equals(SaleBillType.SaleOrder)) {
			// 收货地址参照
			UIRefPane vreceiveaddress = (UIRefPane) getBillCardPanel().getHeadItem(
					"vreceiveaddress").getComponent();
			if (getBillCardPanel().getHeadItem("creceiptcustomerid") != null)
				((CustAddrRefModel) vreceiveaddress.getRefModel()).setCustId(getBillCardPanel()
						.getHeadItem("creceiptcustomerid").getValue());
			vreceiveaddress.setValue((String) saleVO.getParentVO().getAttributeValue(
					"vreceiveaddress"));

		} else if (getSouceBillType().equals(SaleBillType.PurchaseOrder)) {

		} else if (getSouceBillType().equals("38") || getSouceBillType().equals("3B")) {// 预定单

			SaleorderBVO[] ordbvos = saleVO.getBodyVOs();

			String ccalbodyid_h = null;
			for (int i = 0, loop = ordbvos.length; i < loop; i++) {
				if (ccalbodyid_h == null) {
					if (ordbvos[i].getCconsigncorpid() == null
							|| ordbvos[i].getCconsigncorpid().equals(ordbvos[i].getPkcorp())) {
						ccalbodyid_h = ordbvos[i].getCadvisecalbodyid();
					}
				}
			}

			String oldcalbodyid = getBillCardPanel().getHeadItem("ccalbodyid").getValue();
			getBillCardPanel().getHeadItem("ccalbodyid").setValue(ccalbodyid_h);
			if (SoVoTools.isEmptyString(getBillCardPanel().getHeadItem("ccalbodyid").getValue())) {
				getBillCardPanel().getHeadItem("ccalbodyid").setValue(oldcalbodyid);
			}

		}

		setNoEditItem();

		/** 参照型自定义项需要把pk赋值到到value上* */
		// 表头
		if (head_defs != null) {
			for (int i = 1; i <= 20; i++) {
				// 加载自定义项（解决模板无法给pk_def%赋值问题）
				// 临时解决方案
				getBillCardPanel().getHeadItem("pk_defdoc" + i).setValue(
						saleVO.getHeadVO().getAttributeValue("pk_defdoc" + i));

				/*
				 * if (head_defs[i - 1] != null && head_defs[i -
				 * 1].getType().equals("统计")) {
				 * getBillCardPanel().getHeadItem("vdef" + i).setValue(
				 * saleVO.getHeadVO().getAttributeValue( "pk_defdoc" + i)); }
				 */
			}// end for head
		}

		// 表体
		/*
		 * if (body_defs != null) { int rowcount = saleVO.getBodyVOs().length;
		 * for (int i = 1; i <= 20; i++) { if (body_defs[i - 1] != null &&
		 * body_defs[i - 1].getType().equals("统计")) { for (int j = 0; j <
		 * rowcount; j++) { getBillCardPanel().setBodyValueAt(
		 * getBillCardPanel().getBodyValueAt(j, "pk_defdoc" + i), j, "vdef" +
		 * i); } } }// end for body }
		 */
		/** 参照型自定义项需要把pk赋值到到value上* */

		// 保留上游单据的折本汇率
		// 场景：来源于合同
		
			UFDouble nexchangeotobrate = saleVO.getBodyVOs()[0].getNexchangeotobrate();
			if (nexchangeotobrate != null
					&& nexchangeotobrate.toDouble().compareTo(
							Double.parseDouble(getBillCardPanel().getHeadItem("nexchangeotobrate")
									.getValueObject().toString())) != 0) {

				getBillCardPanel().getHeadItem("nexchangeotobrate").setValue(nexchangeotobrate);

				for (int i = 0, len = getBillCardPanel().getRowCount(); i < len; i++) {
					getBillCardPanel().setBodyValueAt(nexchangeotobrate, i, "nexchangeotobrate");

					getBillCardPanel().calculateNumber(i, "noriginalcursummny");
				}
			}
//		if (!saleVO.isCoopped()) {
//		} else {
//			getBillCardPanel().setCurrencyRate();
//
//		}

		// 来源于合同\预订单,保留表体的收货单位\收货地址\收货地区\收货地点
		if ("Z4".equals(getSouceBillType()) || "38".equals(getSouceBillType())) {
			SaleorderBVO[] items = saleVO.getBodyVOs();
			SaleorderBVO item;
			Object obj;
			for (int i = 0, len = items.length; i < len; i++) {
				item = items[i];

				// 收货单位
				// CustmandocDefaultRefModel
				if (item.getCreceiptcorpid() != null) {
					getBillCardPanel()
							.setBodyValueAt(item.getCreceiptcorpid(), i, "creceiptcorpid");
					getBillCardPanel().setBodyValueAt(
							((Object[]) CacheTool.getCellValue("bd_cubasdoc", "pk_cubasdoc",
									"custname", (String) ((Object[]) CacheTool.getCellValue(
											"bd_cumandoc", "pk_cumandoc", "pk_cubasdoc", item
													.getCreceiptcorpid()))[0]))[0], i,
							"creceiptcorpname");
				}

				// 收货地区
				// AreaclDefaultRefModel
				if (item.getCreceiptareaid() != null) {
					getBillCardPanel()
							.setBodyValueAt(item.getCreceiptareaid(), i, "creceiptareaid");
					obj = CacheTool.getCellValue("bd_areacl", "pk_areacl", "areaclname", item
							.getCreceiptareaid());
					getBillCardPanel().setBodyValueAt(obj == null ? null : ((Object[]) obj)[0], i,
							"creceiptareaname");
				}

				// 收货地点
				// AddressDefaultRefModel
				if (item.getCrecaddrnode() != null) {
					getBillCardPanel().setBodyValueAt(item.getCrecaddrnode(), i, "crecaddrnode");
					obj = CacheTool.getCellValue("bd_address", "pk_address", "addrname", item
							.getCrecaddrnode());
					getBillCardPanel().setBodyValueAt(obj == null ? null : ((Object[]) obj)[0], i,
							"crecaddrnodename");
				}

				if (item.getVreceiveaddress()!=null)
					// 收货地址
					getBillCardPanel().setBodyValueAt(item.getVreceiveaddress(), i, "vreceiveaddress");

			}// end for
		}// end if
		if (saleVO.isCoopped()) {
			getBillCardPanel().setHeadItem("cbiztype", coBusiType);
		}
		timer.addExecutePhase(">>>>>>>>>销售订单[参照上游单据确定]---默认数据处理：>>>>>>>>>>>");

		//如果是来源于预订单,自动关联合同
		if ("38".equals(getSouceBillType()) && getBillCardPanel().bLinkCT){
			getBillCardPanel().setDefaultCtItem(0, getBillCardPanel().getBillTable().getRowCount(),true);
		}
		timer.addExecutePhase(">>>>>>>>>销售订单[参照上游单据确定]---自动关联合同用时：>>>>>>>>>>>");
		
		// 自动关联买赠
		if (getBillCardPanel().bBindInvBuyLargess)
			getBillCardPanel().freshBodyLargess(2);
		timer.addExecutePhase(">>>>>>>>>销售订单[参照上游单据确定]---买赠捆绑询价用时：>>>>>>>>>>>");
		
		getBillCardPanel().getBillModel().setNeedCalculate(true);
		
		//输出各个业务操作占用时间分布
		timer.showAllExecutePhase(">>>>>>>>>销售订单[参照上游单据确定]结束：>>>>>>>>>>>");
	}

	/**
	 * 自制新增单据。
	 * 
	 * 创建日期：(2001-4-21 14:11:14)
	 * 
	 */
	protected void onNewBySelf() {
		strState = "新增";
		getBillCardPanel().addNew();
		getBillCardPanel().setEnabled(true);
		getBillCardTools().setHeadRefLimit(strState);
		getBillCardPanel().initFreeItem();
		getBillCardPanel().setBusiType(this.getSelectedBusyType());
		setDefaultData(true);
		setNoEditItem();

		// 自动增表体空行
		addLine();

	}

	/**
	 * 下一单据。
	 * 
	 * 创建日期：(2001-4-24 9:55:56)
	 * 
	 */
	protected void onNext() {
		if (num < vIDs.size()) {
			num++;

			String csaleid = (String) getBillListPanel().getHeadBillModel().getValueAt(num,
					"csaleid");
			this.csaleid = csaleid;
			
			SaleOrderVO vo = vocache.getSaleOrderVO(csaleid);
			if (vo != null) {
				// 加载表体
				if (vo.getChildrenVO() == null) {
					getBillListPanel().loadBodyData(num);
					vo = vocache.getSaleOrderVO(csaleid);
				}

				// 列表行变化
				getBillListPanel().getHeadTable().getSelectionModel()
						.setSelectionInterval(num, num);

				loadCardData(vo);

			} else {
				loadCardData();
				updateCacheVO();
			}
			setButtonsState();
		}
	}

	/**
	 * 最后一张。
	 * 
	 * 创建日期：(2001-4-24 9:55:56)
	 * 
	 */
	protected void onLast() {
		num = vIDs.size() - 1;

		String csaleid = (String) getBillListPanel().getHeadBillModel().getValueAt(num, "csaleid");
		this.csaleid = csaleid;
		
		SaleOrderVO vo = vocache.getSaleOrderVO(csaleid);

		if (vo != null) {
			// 加载表体
			if (vo.getChildrenVO() == null) {
				getBillListPanel().loadBodyData(num);
				vo = vocache.getSaleOrderVO(csaleid);
			}

			loadCardData(vo);
		} else {
			loadCardData();
			updateCacheVO();
		}
		setButtonsState();
	}

	/**
	 * 下一单据。
	 * 
	 * 创建日期：(2001-4-24 9:55:56)
	 * 
	 */
	protected void onFrist() {
		num = 0;

		String csaleid = (String) getBillListPanel().getHeadBillModel().getValueAt(num, "csaleid");
		this.csaleid = csaleid;
		
		SaleOrderVO vo = vocache.getSaleOrderVO(csaleid);

		if (vo != null) {
			// 加载表体
			if (vo.getChildrenVO() == null) {
				getBillListPanel().loadBodyData(num);
				vo = vocache.getSaleOrderVO(csaleid);
			}

			loadCardData(vo);
		} else {
			loadCardData();
			updateCacheVO();
		}
		setButtonsState();
	}

	/**
	 * 前一单据。
	 * 
	 * 创建日期：(2001-4-24 9:55:42)
	 * 
	 */
	protected void onPre() {
		if (num > 0) {
			num--;

			String csaleid = (String) getBillListPanel().getHeadBillModel().getValueAt(num,
					"csaleid");
			this.csaleid = csaleid;
			
			SaleOrderVO vo = vocache.getSaleOrderVO(csaleid);

			if (vo != null) {
				// 加载表体
				if (vo.getChildrenVO() == null) {
					getBillListPanel().loadBodyData(num);
					vo = vocache.getSaleOrderVO(csaleid);
				}

				loadCardData(vo);
			} else {
				loadCardData();
				updateCacheVO();
			}
			setButtonsState();
		}
	}

	/**
	 * 联查
	 */
	protected void onOrderQuery() {
		getSourceDlg().showModal();
	}

	/**
	 * 分单打印
	 */
	protected void onSplitPrint() {
		if (getSpDLG().showModal() == QueryConditionClient.ID_CANCEL)
			return;

		// 取分单条件
		SaleDispartVO[] receSplitVOs = (SaleDispartVO[]) getSpDLG().getSplitPanel()
				.getReceiveSplitPanel().getBillModel().getBodyValueVOs(
						SaleDispartVO.class.getName());

		// 分单打印
		try {
			// 带打印VO
			ArrayList<AggregatedValueObject> prlistvo = new ArrayList<AggregatedValueObject>();
			// 列表
			if (strShowState.equals("列表")) {
				int[] selRows = getBillListPanel().getHeadTable().getSelectedRows();
				if (selRows.length > 0) {
					for (int i = 0; i < selRows.length; i++) {
						num = selRows[i];
						// 加载数据
						loadCardData();
						prlistvo.add(getPrintVO());
					}
				}
			} else
				// 卡片
				prlistvo.add(getPrintVO());

			// 构造打印工具
			BillPrintTool printTool = new BillPrintTool(this, getNodeCode(), prlistvo,
					getBillCardPanel().getBillData(), null, getCorpPrimaryKey(),
					getClientEnvironment().getUser().getPrimaryKey(), "vreceiptcode", "csaleid");

			// 分单打印
			if (strShowState.equals("列表"))
				printTool.onBatchSplitPrintPreview(getBillListPanel(), SaleBillType.SaleOrder,
						transDispartVO(receSplitVOs));
			else
				printTool.onSplitCardPrintPreview(getBillCardPanel(), getBillListPanel(),
						SaleBillType.SaleOrder, transDispartVO(receSplitVOs));

		} catch (Exception e) {
			handleException(e, "销售订单分单打印出现异常! ");
		}
	}

	/**
	 * 将销售的分单VO转化为公共的分单VO
	 * 
	 * @param svos
	 *            --- 销售的分单VO
	 */
	private SplitParams[] transDispartVO(SaleDispartVO[] svos) {
		if (svos == null || svos.length <= 0)
			return null;
		SplitParams[] paramvos = null;

		int type = SplitParams.NoInput;
		Object value = null;
		ArrayList<SplitParams> list = new ArrayList<SplitParams>();
		for (int i = 0; i < svos.length; i++) {
			if (svos[i].getBdefault().booleanValue()) {
				// 设置发货日期
				if (svos[i].getDispartkey().equals("dconsigndate")) {
					type = SplitParams.NeedInputInt;
					value = svos[i].getValue() == null ? 1 : svos[i].getValue().intValue();
				} else {
					type = SplitParams.NoInput;
					value = null;
				}

				list.add(new SplitParams(svos[i].getDispartkey(), svos[i].getDispartname(), type,
						value, svos[i].getBdefault().booleanValue()));
			}
		}

		if (list.size() > 0) {
			paramvos = list.toArray(new SplitParams[list.size()]);
		}

		return paramvos;
	}

	/**
	 * 打印。
	 * 
	 * 创建日期：(2001-4-25 10:48:26)
	 * 
	 * @throws Exception
	 * 
	 */
	protected void onPrint(boolean needPreview) throws Exception {
		if (!strShowState.equals("列表")) { /*-=notranslate=-*/
			onPrintCard(needPreview);
		} else {
			onPrintListVos(needPreview);

		}
	}

	/**
	 * 打印。
	 * 
	 * 采用列表方式修改 参照wsy项目补丁修改
	 * 
	 * 创建日期：(2001-4-25 10:48:26)
	 * 
	 */
	protected void onPrintCard(boolean needPreview) {

		// 打印模板
		if (getPrintEntry().selectTemplate() < 0)
			return;

		SaleOrderVO saleorder = (SaleOrderVO) getBillCardPanel().getBillValueVO(
				SaleOrderVO.class.getName(), SaleorderHVO.class.getName(),
				SaleorderBVO.class.getName());

		nc.ui.scm.print.PrintLogClient plc = new nc.ui.scm.print.PrintLogClient();
		showHintMessage(plc.getBeforePrintMsg(needPreview, false));
		plc.addFreshTsListener(this);
		plc.setPrintEntry(getPrintEntry());
		plc.setPrintInfo(saleorder.getHeadVO().getScmPrintlogVO());

		// 打印模板上设置打印监听
		getPrintEntry().setPrintListener(plc);

		// 打印数据源
		SaleOrderPrintDataInterface prds = getDataSource();
		ArrayList prlistvo = new ArrayList();
		prlistvo.add(getPrintVO());
		prds.setListVOs(prlistvo);
		prds.setIsNeedSpaceRowInOneVO(false);// 不打印空行
		prds.setTotalLinesInOnePage(m_print.getBreakPos());

		// 打印模板上设置打印数据源
		getPrintEntry().setDataSource(prds);

		// 开始打印
		if (needPreview) {
//			getPrintEntry().previewInDialog(true);
			getPrintEntry().preview();
		} else {
			getPrintEntry().print();
		}

		showHintMessage(plc.getPrintResultMsg(needPreview));

	}

	/**
	 * @return 2007-8-21wsy 得到打印VO，这方法为了迎合以前的用法，因此带来很多不便。
	 */
	private AggregatedValueObject getPrintVO() {
		
		AggregatedValueObject printHvo = getBillCardPanel().getBillValueVO("nc.vo.dm.pub.DMVO",
				"nc.vo.dm.pub.DMDataVO", "nc.vo.dm.pub.DMDataVO");

		/** 补齐数据* */
		
		// 收货地址
		printHvo.getParentVO().setAttributeValue(
				"vreceiveaddress",
				((UIRefPane) getBillCardPanel().getHeadItem("vreceiveaddress").getComponent())
						.getUITextField().getText());
		
		//散户
		printHvo.getParentVO().setAttributeValue(
				"cfreecustid",				
				((UIRefPane) getBillCardPanel().getHeadItem("cfreecustid").getComponent())
						.getUITextField().getText());

		return printHvo;

	}

	/**
	 * 将缓存中的vo信息补全,为列表打印做准备
	 * 
	 * @throws Exception
	 */
	private void supplyVocacheForPrintList() throws Exception {
		SaleorderHVO[] hvos = vocache.getHeadVoWithOutBody();
		SaleorderBVO[] bvos = null;
		if (hvos != null && hvos.length > 0) {
			bvos = SaleOrderBO_Client.queryBodyAllDataByIDs(SoVoTools.getVOsOnlyValues(hvos,
					"csaleid"));
			SaleOrderVO[] svos = (SaleOrderVO[]) RedunVOTool.getBillVos(
					SaleOrderVO.class.getName(), "csaleid", hvos, bvos);
			for (SaleOrderVO svo : svos)
				vocache.setSaleOrderVO(svo.getPrimaryKey(), svo);
		}
	}

	/**
	 * 列表多张单据打印。
	 * 
	 * 修改为批量打印 V502
	 * 
	 * 创建日期：(2003-11-5 9:37:56)
	 * 
	 * 打印相关变量每次均重新初始化，保证每次可以在多模板时进行选择
	 * 
	 * 统一打印数据源，改为卡片方式
	 * 
	 * @throws Exception
	 * 
	 */
	protected void onPrintListVos(boolean needPreview) throws Exception {

		// 如果预览，则只打印打印第一张单据，等同于卡片打印
		if (needPreview) {
			num = getBillListPanel().getHeadTable().getSelectedRow();

			String csaleid = (String) getBillListPanel().getHeadBillModel().getValueAt(num,
					"csaleid");

			SaleOrderVO saleorder = vocache.getSaleOrderVO(csaleid);
			loadCardData(saleorder);

			onPrintCard(true);

			return;
		}

		/** 列表批打方式暂作为遗留* */

		java.util.ArrayList alvos = new java.util.ArrayList();

		int selectRows[] = getBillListPanel().getHeadTable().getSelectedRows();

		SaleorderHVO[] hvos = new SaleorderHVO[selectRows.length];
		for (int i = 0, loop = selectRows.length; i < loop; i++) {
			hvos[i] = (SaleorderHVO) getBillListPanel().getHeadBillModel().getBodyValueRowVO(
					selectRows[i], "nc.vo.so.so001.SaleorderHVO");
		}

		// 将缓存中的vo信息补全
		supplyVocacheForPrintList();

		for (int i = 0; i < selectRows.length; i++) {
			num = selectRows[i];

			String csaleid = (String) getBillListPanel().getHeadBillModel().getValueAt(num,
					"csaleid");

			SaleOrderVO saleorder = vocache.getSaleOrderVO(csaleid);
			loadCardData(saleorder);

			alvos.add(getPrintVO());

			if (needPreview)
				break;
		}

		if (null == alvos || alvos.size() == 0) {
			nc.ui.pub.beans.MessageDialog.showErrorDlg(this, NCLangRes.getInstance().getStrByID(
					"SCMCOMMON", "UPPSCMCommon-000270")/* @res "提示" */, NCLangRes.getInstance()
					.getStrByID("SCMCOMMON", "UPPSCMCommon-000153")/*
																	 * @res
																	 * "没有获得列表数据！"
																	 */);
			return;
		}

		if (getPrintEntry().selectTemplate() < 0)
			return;

		showHintMessage(PrintLogClient.getBeforePrintMsg(needPreview, true));

		if (needPreview) {
			// 处理打印日志
			getPrintLogClient().setPrintEntry(m_print);
			printLogClient.setPrintInfo(hvos[0].getScmPrintlogVO());
			m_print.setPrintListener(printLogClient);// 设置打印监听

			ArrayList prlistvo = new ArrayList();
			SaleOrderPrintDataInterface prds = getDataSource();
			prlistvo.add(alvos.get(0));
			prds.setListVOs(prlistvo);
			prds.setIsNeedSpaceRowInOneVO(false);// 不打印空行
			prds.setTotalLinesInOnePage(m_print.getBreakPos());

			// 向打印置入数据源，进行打印
			m_print.setDataSource(prds);
			m_print.preview();
		} else {
			// 处理打印日志
			getPrintLogClient().setPrintEntry(m_print);

			m_print.beginBatchPrint();
			SaleOrderPrintDataInterface prds = null;
			ArrayList prlistvo = null;
			printLogClient.setBatchPrint(true);// 设置是批打
			m_print.setPrintListener(printLogClient);// 设置打印监听
			for (int i = 0, loop = hvos.length; i < loop; i++) {
				// 处理打印日志
				printLogClient.setPrintInfo(hvos[i].getScmPrintlogVO());

				if (printLogClient.check()) {// 检查通过才执行打印，
					
					// 设置数据
					prds = getDataSource();
					prlistvo = new ArrayList();
					prlistvo.add(alvos.get(i));
					prds.setListVOs(prlistvo);
					prds.setIsNeedSpaceRowInOneVO(false);// 不打印空行
					prds.setTotalLinesInOnePage(m_print.getBreakPos());

					// 向打印置入数据源，进行打印
					m_print.setDataSource(prds);

				}
			}

			m_print.endBatchPrint();

		}

		showHintMessage(printLogClient.getPrintResultMsg(needPreview));
	}

	protected PrintEntry getPrintEntry() {
		if (null == m_print) {
			m_print = new nc.ui.pub.print.PrintEntry(this, null);
			m_print.setTemplateID(getClientEnvironment().getCorporation().getPk_corp(),
					getNodeCode(), getClientEnvironment().getUser().getPrimaryKey(), null);
		}
		return m_print;
	}

	/**
	 * 创建者：仲瑞庆
	 * 
	 * 功能：参数： 返回：
	 * 
	 * 例外： 日期：(2001-10-30 15:06:35)
	 * 
	 * 修改日期，修改人，修改原因，注释标志：
	 * 
	 */
	protected SaleOrderPrintDataInterface getDataSource() {
		// if (m_dataSource == null) {
		m_dataSource = new SaleOrderPrintDataInterface();
		// }

		m_dataSource.setBillData(getBillCardPanel().getBillData());
		m_dataSource.setModuleName(getNodeCode());
		m_dataSource.setTotalLinesInOnePage(getPrintEntry().getBreakPos());
		m_dataSource.setFormulaJudge(new DefaultFormulaJudge(getNodeCode(), getClient().getCorporation().getPrimaryKey()));

		return m_dataSource;
	}

	/**
	 * 查询数据。
	 * 
	 * 创建日期：(2001-6-1 13:12:36)
	 * 
	 */
	protected void onQuery() {

		// 显示查询对话况
		if (getQueryDlg().showModal() == QueryConditionClient.ID_CANCEL)
			return;

		onRefresh();

	}

	/**
	 * 刷新操作
	 * 
	 */
	protected void onRefresh() {
		
	    //创建时间工具类实例
		nc.vo.scm.pu.Timer timer = new nc.vo.scm.pu.Timer();
		timer.start();
		
		ConditionVO[] voCondition = getQueryDlg().getConditionVO();

		// 处理客商管理档案自定义项
		// 处理订单类型
		boolean queryallflag = false;
		int index = -1;
		if (voCondition != null) {
			for (int i = 0; i < voCondition.length; i++) {
				if (voCondition[i].getTableNameForMultiTable() != null
						&& voCondition[i].getTableNameForMultiTable().equals("bd_cumandoc")
						&& voCondition[i].getTableCodeForMultiTable().indexOf("def") >= 0) {
					voCondition[i].setTableCode(voCondition[i].getTableName() + "."
							+ voCondition[i].getTableCodeForMultiTable());
					voCondition[i].setFieldCode("ccustomerid");
				} else if ("so_sale.bretinvflag".equals(voCondition[i].getFieldCode())) {
					if (voCondition[i].getValue().equals(
							NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000547")/*
																								 * @res
																								 * "全部"
																								 */)) {
						queryallflag = true;
						index = i;
					} else if (voCondition[i].getValue().equals(
							NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000546")/*
																								 * @res
																								 * "非退货"
																								 */)) {
						voCondition[i].setValue("N");
					} else if (voCondition[i].getValue().equals(
							NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000151")/*
																								 * @res
																								 * "退货"
																								 */)) {
						voCondition[i].setValue("Y");
					}
				}

			}// end for
		}

		// 处理订单类型
		// 全部查询去掉此条件
		if (queryallflag) {
			ConditionVO[] conds1 = new ConditionVO[voCondition.length - 1];
			System.arraycopy(voCondition, 0, conds1, 0, index);
			System.arraycopy(voCondition, index + 1, conds1, index, conds1.length - index);
			voCondition = conds1;
		}

		// 部门，包含下级部门
		// 销售组织，包含下级销售组织
		try {
			ConditionVO[] newvo;
			newvo = ScmPubHelper.getTotalSubPkVOs(voCondition, getCorpPrimaryKey());
			voCondition = newvo;
		} catch (Exception e) {
			nc.vo.scm.pub.SCMEnv.out(e);
		}

		ArrayList al = new ArrayList();
		ArrayList<ConditionVO> qualified = new ArrayList();
		for (int i = 0, len = voCondition.length; i < len; i++) {
			// 判断数据权限条件
			if (!(voCondition[i].getOperaCode().trim().equalsIgnoreCase("IS")
					&& voCondition[i].getValue().trim().equalsIgnoreCase("NULL") || voCondition[i]
					.getValue().trim().indexOf(
					// "(select distinct power.resource_data_id"
							"(select ") >= 0)) {
				if ("客户地区分类".equals(voCondition[i].getFieldName())) {
					// 查询模板中TableCode为areaclname，value设置参照的name值
					if (null != voCondition[i].getRefResult())
						voCondition[i].setValue(voCondition[i].getRefResult().getRefName());
				}
				al.add(voCondition[i]);
			} else {
				qualified.add(voCondition[i]);
			}
		}// end for
		ConditionVO[] normals = new ConditionVO[al.size()];
		al.toArray(normals);

		// 穿透查询
		normals = ConvertQueryCondition.getConvertedVO(normals, getCorpPrimaryKey());

		// 重新组合条件
		for (int i = 0, len = normals.length; i < len; i++) {
			voCondition[i] = normals[i];
		}
		for (int i = 0, len = qualified.size(); i < len; i++) {
			voCondition[i + normals.length] = qualified.get(i);
		}

		boolean bfstatus = false;
		boolean bvreceiptcode = false;
		for (int i = 0; i < voCondition.length; i++) {
			if (voCondition[i].getFieldCode().equals("so_sale.fstatus")) {
				if (voCondition[i].getValue().equals("Y")) {
					Object objIsWaitAudit = new Object[2];
					((Object[]) objIsWaitAudit)[0] = "Y";
					((Object[]) objIsWaitAudit)[1] = ClientEnvironment.getInstance().getUser()
							.getPrimaryKey();
					getBillListPanel().setObjIsWaitAudit(objIsWaitAudit);
					voCondition[i].setValue(String.valueOf(BillStatus.FREE));
				} else if (voCondition[i].getValue().equals("N")) {
					voCondition[i].setFieldCode("so_sale.dr");
					voCondition[i].setValue("0");
				}
				bfstatus = true;
			}
			if (voCondition[i].getFieldCode().equals("so_sale.vreceiptcode")
					&& voCondition[i].getOperaCode().equals("like")
					&& !voCondition[i].getValue().contains("%")) {
				//添加前模糊查询（采购订单 也如此处理）  liuzwc 2009-07-22
				voCondition[i].setValue("%" + voCondition[i].getValue() + "%");
				bvreceiptcode = true;
			}
			if (bfstatus && bvreceiptcode)
				break;
		}

		// 地区分类权限条件转换
		DataPowerUtils.trnsDPCndFromAreaToCust(voCondition, "ccustomerid");

		String tablename = null;
		if (getBillType().equals(SaleBillType.SaleOrder))
			tablename = "so_sale";
		else
			tablename = "so_salereceipt";

		StringBuffer strWhere = new StringBuffer();
		strWhere.append(tablename + ".pk_corp = '" + getCorpPrimaryKey() + "' ");
		/** 订单查询只受条件中的业务类型限制，不受按钮业务类型取值限制 V51* */
		if (!getBillType().equals(SaleBillType.SaleOrder))
			strWhere.append(" AND " + tablename + ".cbiztype = '" + boBusiType.getTag() + "' ");
		/** 订单查询只受条件中的业务类型限制，不受按钮业务类型取值限制 V51* */
		strWhere.append(" AND " + tablename + ".dr=0 ");

		if (getBillType().equals(SaleBillType.SaleOrder))
			strWhere.append(" AND " + tablename + ".creceipttype = '30' ");
		else
			strWhere.append(" AND " + tablename + ".creceipttype = '31' ");

		// 单据状态处理
		if (getQueryDlg().m_rdoAll.isSelected()) {
			strWhere.append(" and " + tablename + ".fstatus != " + BillStatus.BLANKOUT);
		}
		// 待审批条件：objIsWaitAudit[0] -- "Y" 有待审批条件 "N" 无待审批条件
		// objIsWaitAudit[1] -- 当前操作员ID
		// else if (getQueryDlg().m_rdoIsWaitAudit.isSelected()) {
		// Object objIsWaitAudit = new Object[2];
		// ((Object[]) objIsWaitAudit)[0] = "Y";
		// ((Object[]) objIsWaitAudit)[1] =
		// ClientEnvironment.getInstance().getUser().getPrimaryKey();
		// getBillListPanel().setObjIsWaitAudit(objIsWaitAudit);
		// strWhere.append(" and " + tablename + ".fstatus in (" +
		// String.valueOf(BillStatus.FREE) + ")");
		// }
		// 非全部状态
		else {
			String fstatus = "";
			if (getQueryDlg().m_rdoFree.isSelected()) {
				fstatus += fstatus.equals("") ? String.valueOf(BillStatus.FREE) : ","
						+ String.valueOf(BillStatus.FREE);
			}
			if (getQueryDlg().m_rdoAudited.isSelected()) {
				fstatus += fstatus.equals("") ? String.valueOf(BillStatus.AUDIT) : ","
						+ String.valueOf(BillStatus.AUDIT);
			}
			if (getQueryDlg().m_rdoBlankOut.isSelected()) {
				fstatus += fstatus.equals("") ? String.valueOf(BillStatus.BLANKOUT) : ","
						+ String.valueOf(BillStatus.BLANKOUT);
			}
			if (getQueryDlg().m_rdoFreeze.isSelected()) {
				fstatus += fstatus.equals("") ? String.valueOf(BillStatus.FREEZE) : ","
						+ String.valueOf(BillStatus.FREEZE);
			}
			if (getQueryDlg().m_rdoAuditing.isSelected()) {
				fstatus += fstatus.equals("") ? String.valueOf(BillStatus.AUDITING) : ","
						+ String.valueOf(BillStatus.AUDITING);
			}
			if (getQueryDlg().m_rdoClosed.isSelected()) {
				fstatus += fstatus.equals("") ? String.valueOf(BillStatus.FINISH) : ","
						+ String.valueOf(BillStatus.FINISH);
			}

			if (fstatus.equals(""))
				strWhere.append(" and " + tablename + ".fstatus != " + BillStatus.BLANKOUT);
			else
				strWhere.append(" and " + tablename + ".fstatus in (" + fstatus + ")");
		}

		// if (getBillType().equals(SaleBillType.SaleOrder)) {
		// if (getQueryDlg().m_rdoBatch.isSelected())
		// strWhere.append(" and " + tablename + ".bretinvflag = 'Y'");
		// else
		// strWhere.append(" and " + tablename + ".bretinvflag = 'N'");
		// }

		if (getQueryDlg().getWhereSQL(voCondition) == null
				|| getQueryDlg().getWhereSQL(voCondition).length() == 0) {
			getBillListPanel().setWhere(strWhere.toString());
		} else {
			getBillListPanel().setWhere(
					strWhere + " AND (" + getQueryDlg().getWhereSQL(voCondition) + ") ");
		}

		setBtnsByBillState(-1);
		timer.addExecutePhase(">>>>>>>>>销售订单[查询--查询前准备条件]用时：>>>>>>>>>>>");
		
		getBillListPanel().reLoadData();
		timer.addExecutePhase(">>>>>>>>>销售订单[查询--查询加载列表数据]用时：>>>>>>>>>>>");
		
		//v5.6 去掉，在getBillListPanel().reLoadData()中已经做过
		//fillCacheByListPanel();
		initIDs();

		// 卡片下查询(bodyRowChange+onCard)
		if (strShowState.equals("卡片")) {

			String csaleid = (String) getBillListPanel().getHeadBillModel().getValueAt(selectRow,
					"csaleid");

			SaleOrderVO vo = vocache.getSaleOrderVO(csaleid);

			if (vo == null || vo.getParentVO() == null) {
				num = -1;
				getBillCardPanel().addNew();
				getBillCardPanel().getHeadItem("vreceiptcode").setValue(null);
			} else {
				getBillListPanel().loadBodyData(selectRow);
				strState = "自由";
				num = selectRow;
				loadCardData(vocache.getSaleOrderVO(csaleid));
			}

			updateUI();
		}
		timer.addExecutePhase(">>>>>>>>>销售订单[查询--设置卡片信息]用时：>>>>>>>>>>>");

		showHintMessage(NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000542", null,
				new String[] { "" + getBillListPanel().getHeadBillModel().getRowCount() })/* 共查询到{0}条单据 */);

		b_query = true;
		setButtonsState();
		timer.addExecutePhase(">>>>>>>>>销售订单[查询--设置按钮状态]用时：>>>>>>>>>>>");
		
		//输出各个业务操作占用时间分布
		timer.showAllExecutePhase(">>>>>>>>>销售订单[查询]结束：>>>>>>>>>>>");
		
	}
	
	/**
	 * 此处插入方法说明。 创建日期：(2003-11-16 11:37:56)
	 */
	private void fillCacheByListPanel() {

		if (getBillListPanel().getHeadBillModel().getRowCount() <= 0)
			return;

		SaleorderHVO[] hvos = (SaleorderHVO[]) getBillListPanel().getHeadBillModel()
				.getBodyValueVOs("nc.vo.so.so001.SaleorderHVO");
		// 取表头精度数据
		try {
			BillTools.getMnyByCurrencyFromModel(getBillListPanel().getHeadBillModel(), hvos,
					getCorpPrimaryKey(), "ccurrencytypeid", new String[] { "npreceivemny",
							"nreceiptcathmny" });
		} catch (Exception e) {
			handleException(e);
		}

	}

	/**
	 * 退货处理。
	 * 
	 * 创建日期：(2001-4-24 9:55:56)
	 * 
	 */
	protected void onRefundment() {

		b_refundment = true;
		// 切换截面
		if (strShowState.equals("列表"))
			onCard();

		// 重设右键菜单，退货只有‘删除’ liuzwc
		setBodyMenuItem(true);

		// switchInterface();

		SaleOrderVO saleorderVO = (SaleOrderVO) getVO(false);

		// 过滤劳务折扣行
		saleorderVO.filterField(new String[] { "laborflag", "discountflag" });

		strUISource = strShowState;
		// 设置状态
		strShowState = "退货"; /*-=notranslate=-*/
		strState = "退货"; /*-=notranslate=-*/

		getBillCardTools().setHeadRefLimit(strState);

		// 加载数据
		try {
			num = getBillListPanel().getHeadTable().getSelectedRow();
			long s = System.currentTimeMillis();
			/*
			 * SaleOrderVO saleorderVO =
			 * SaleOrderBO_Client.queryData(getBillID());
			 * nc.vo.scm.pub.SCMEnv.out( "数据读取[共用时" + CommonConstant.BEGIN_MARK +
			 * (System.currentTimeMillis() - s) + CommonConstant.END_MARK +
			 * "]");
			 */
			// SaleOrderVO saleorderVO=vocache.getSaleOrderVO(num);
			// 保留初始的VO
			getBillCardTools().setOldsaleordervo(saleorderVO);
			String csaleid = saleorderVO.getHeadVO().getCsaleid();

			// 设置数据
			saleorderVO.getHeadVO().setBretinvflag(new UFBoolean(true));
			saleorderVO.getHeadVO().setDbilldate(getClientEnvironment().getDate());
			saleorderVO.getHeadVO().setCsaleid(null);

			saleorderVO.getHeadVO().setAttributeValue("bcooptopo", null);
			saleorderVO.getHeadVO().setAttributeValue("bpocooptome", null);
			saleorderVO.getHeadVO().setVreceiptcode(null);
			saleorderVO.getHeadVO().setCapproveid(null);
			saleorderVO.getHeadVO().setDapprovedate(null);
			saleorderVO.getHeadVO().setBinvoicendflag(null);
			saleorderVO.getHeadVO().setBoutendflag(null);
			saleorderVO.getHeadVO().setBreceiptendflag(null);
			saleorderVO.getHeadVO().setBpayendflag(null);

			saleorderVO.getHeadVO().setFstatus(new Integer(BillStatus.FREE));
			saleorderVO.getHeadVO().setPrimaryKey(null);
			saleorderVO.getHeadVO().setTs(null);
			saleorderVO.getHeadVO().setDmakedate(getClientEnvironment().getDate());

			saleorderVO.getHeadVO().setDbilltime(null);
			saleorderVO.getHeadVO().setDmoditime(null);
			saleorderVO.getHeadVO().setDaudittime(null);

			// n30
			saleorderVO.getHeadVO().setNpreceivemny(null);
			saleorderVO.getHeadVO().setNpreceiverate(null);
			saleorderVO.getHeadVO().setNreceiptcathmny(null);
			saleorderVO.getHeadVO().setBoverdate(null);
			saleorderVO.getHeadVO().setBtransendflag(null);

			// n30add
			String[] keys = new String[] {
			// v30 so_saleorder_b
					"tconsigntime", // 发货时间
					"tdelivertime",// 到货时间
					"ntaldcnum", // 已参与价保主数量
					"nasttaldcnum",// 已参与价保辅数量
					"ntaldcmny", // 价保金额
					"nretprofnum", // 已参与返利主数量
					"nastretprofnum",// 已参与返利辅数量
					"nretprofmny", // 返利金额
					"natp", // ATP

					// v30 so_saleexecute
					"ntalplconsigmny", // 已计划（发货）金额
					"ntaltransnum",// 累计运输数量
					"ntalbalancemny",// 累计结算金额
					"ntranslossnum",// 运输货损数量
					"biftransfinish",// 运输结束标志
					"dlastconsigdate",// 最近一次发货日期
					"dlasttransdate",// 最近一次运输日期
					"dlastoutdate",// 最近一次出库日期
					"dlastinvoicedt",// 最近一次开票日期
					"dlastpaydate",// 最近一次回款日期
					// 累计安排委外订单数量
					"narrangescornum",
					// 累计安排请购单数量
					"narrangepoapplynum",
					// 累计安排调拨订单数量
					"narrangetoornum",
					// 累计安排调拨申请数量
					"norrangetoapplynum",
					// 是否货源安排完毕
					"barrangedflag",
					// 最后货源安排人
					"carrangepersonid",
					// 最后货源安排时间
					"tlastarrangetime",

					// 累计安排生产订单数量
					"narrangemonum", "ntotalshouldoutnum",
					
					//累计安排采购订单数量，累计补货直运数量，累计可安排数量
					"narrangepoordernum",
					"narrangetotolenum",
					"arrangenum"

			};

			for (int i = 0; i < saleorderVO.getBodyVOs().length; i++) {
				// 记录退货单的源头
				saleorderVO.getBodyVOs()[i].setCsourcebillid(csaleid);
				saleorderVO.getBodyVOs()[i].setCsourcebillbodyid(saleorderVO.getBodyVOs()[i]
						.getCorder_bid());	
				
				saleorderVO.getBodyVOs()[i].setCreceipttype(SaleBillType.SaleOrder);
				saleorderVO.getBodyVOs()[i].setPrimaryKey(null);
				saleorderVO.getBodyVOs()[i].setCsaleid(null);
				saleorderVO.getBodyVOs()[i].setFrowstatus(new Integer(BillStatus.FREE));

				saleorderVO.getBodyVOs()[i].setBifinventoryfinish(null);
				saleorderVO.getBodyVOs()[i].setBifinvoicefinish(null);
				saleorderVO.getBodyVOs()[i].setBifpayfinish(null);
				saleorderVO.getBodyVOs()[i].setBifreceivefinish(null);

				saleorderVO.getBodyVOs()[i].setNtotalbalancenumber(null);
				saleorderVO.getBodyVOs()[i].setNtotalcostmny(null);
				saleorderVO.getBodyVOs()[i].setNtotalinventorynumber(null);
				saleorderVO.getBodyVOs()[i].setNtotalinvoicenumber(null);
				saleorderVO.getBodyVOs()[i].setNtotalinvoicemny(null);
				saleorderVO.getBodyVOs()[i].setNtotalpaymny(null);
				saleorderVO.getBodyVOs()[i].setNtotalreceivenumber(null);
				saleorderVO.getBodyVOs()[i].setNtotalsignnumber(null);
				saleorderVO.getBodyVOs()[i].setCfreezeid(null);
				saleorderVO.getBodyVOs()[i].setTs(null);
				
				//v5.6 wwq cheney 退货订单保留发货公司、发货库存组织、发货仓库
				//saleorderVO.getBodyVOs()[i].setBdericttrans(null);
				//saleorderVO.getBodyVOs()[i].setCconsigncorp(null);
				//saleorderVO.getBodyVOs()[i].setCconsigncorpid(null);
				//saleorderVO.getBodyVOs()[i].setCadvisecalbodyid(null);
				//saleorderVO.getBodyVOs()[i].setCbodywarehouseid(null);
				
				saleorderVO.getBodyVOs()[i].setCreccalbody(null);
				saleorderVO.getBodyVOs()[i].setCreccalbodyid(null);
				saleorderVO.getBodyVOs()[i].setCrecwarehouse(null);
				saleorderVO.getBodyVOs()[i].setCrecwareid(null);
				saleorderVO.getBodyVOs()[i].setNtotlbalcostnum(null);

				// nc30add
				nc.vo.so.so016.SoVoTools.setVOValueForOne(saleorderVO.getBodyVOs()[i], keys, null);
				
				//设置负数数量、金额
				saleorderVO.getBodyVOs()[i].setNnumber(UFDouble.ZERO_DBL.sub(saleorderVO.getBodyVOs()[i].getNnumber()));
				if (saleorderVO.getBodyVOs()[i].getNpacknumber()!=null)
					saleorderVO.getBodyVOs()[i].setNpacknumber(UFDouble.ZERO_DBL.sub(saleorderVO.getBodyVOs()[i].getNpacknumber()));
				saleorderVO.getBodyVOs()[i].setNquoteunitnum(UFDouble.ZERO_DBL.sub(saleorderVO.getBodyVOs()[i].getNquoteunitnum()));
				for (int j = 0,len = SOBillCardTools.getSaleItems_ALLMny().length;j<len;j++)
					saleorderVO.getBodyVOs()[i].setAttributeValue(
							SOBillCardTools.getSaleItems_ALLMny()[j], 
							UFDouble.ZERO_DBL.sub((UFDouble)saleorderVO.getBodyVOs()[i].getAttributeValue(SOBillCardTools.getSaleItems_ALLMny()[j])));
				
			}
			
			//用新的负数数量重新计算
/*			SCMRelationsCal.calculate(saleorderVO.getBodyVOs(), saleorderVO.getHeadVO(), 
					SaleorderBVO.getCalculatePara("nnumber", null, SA_02,SO40), 
					"nnumber", SaleorderBVO.getKeys(), SaleorderBVO.getField());*/


			s = System.currentTimeMillis();
			getBillCardPanel().setBillValueVO(saleorderVO);
			nc.vo.scm.pub.SCMEnv.out("数据设置[共用时" + (System.currentTimeMillis() - s) + "]");

			long s1 = System.currentTimeMillis();
			// getBillCardPanel().getBillModel().execLoadFormula();
			BillItem[] bms = getBillCardPanel().getBillModel().getBodyItems();
			for (int i = 0; i < bms.length; i++) {
				if (bms[i].getLoadFormula() != null && bms[i].getLoadFormula().length > 0) {
					getBillCardPanel().getBillModel().execLoadFormulasByKey(bms[i].getKey());
				}
			}
			nc.vo.scm.pub.SCMEnv.out("执行公式[共用时" + (System.currentTimeMillis() - s1) + "]");

			// 重算整单价税合计
			if (getBillCardPanel().getHeadItem("nheadsummny") != null)
				getBillCardPanel().getHeadItem("nheadsummny").setValue(
						getBillCardPanel().getTotalValue("noriginalcursummny"));

			// 收货地址参照
			UIRefPane vreceiveaddress = (UIRefPane) getBillCardPanel().getHeadItem(
					"vreceiveaddress").getComponent();

			/** 保留表头收货地址 V502 qurui zhongwei* */
			// ((CustAddrRefModel) vreceiveaddress.getRefModel())
			// .setCustId(getBillCardPanel().getHeadItem(
			// "creceiptcustomerid").getValue());
			// // 基础档案ID
			// String formula =
			// "getColValue(bd_cumandoc,pk_cubasdoc,pk_cumandoc,creceiptcustomerid)";
			// String pk_cubasdoc = (String) getBillCardPanel().execHeadFormula(
			// formula);
			// String strvreceiveaddress = BillTools.getColValue2("bd_custaddr",
			// "pk_custaddr", "defaddrflag", "Y", "pk_cubasdoc",
			// pk_cubasdoc);
			// vreceiveaddress.setPK(strvreceiveaddress);
			/** 保留表头收货地址 V502 qurui zhongwei* */

			vreceiveaddress.getUITextField().setText(saleorderVO.getHeadVO().getVreceiveaddress());

			getBillCardPanel().initFreeItem();
			setHeadDefaultData();
			// 设置散户状态
			if (getBillCardPanel().getHeadItem("bfreecustflag").getValue() == null
					|| getBillCardPanel().getHeadItem("bfreecustflag").getValue().equals("false")) {
				getBillCardPanel().getHeadItem("cfreecustid").setEnabled(false);
			} else {
				getBillCardPanel().getHeadItem("cfreecustid").setEnabled(true);
			}
			// 计量单位
			getBillCardPanel().initUnit();

			/**v5.6 wwq cheney 退货订单保留发货公司、发货库存组织、发货仓库*/
			// 清空表体发货库存组织、发货仓库
/*			UIRefPane cadvisecalbody = (UIRefPane) getBillCardPanel().getBodyItem("cadvisecalbody")
					.getComponent();
			cadvisecalbody.setPK(null);

			UIRefPane cbodywarehousename = (UIRefPane) getBillCardPanel().getBodyItem(
					"cbodywarehousename").getComponent();
			cbodywarehousename.setPK(null);*/
			/**v5.6 wwq cheney 退货订单保留发货公司、发货库存组织、发货仓库*/

			for (int i = 0; i < getBillCardPanel().getRowCount(); i++) {

				getBillCardPanel().setAssistUnit(i);

				/** 保留库存组织、仓库 V502 jindongmei jiangchunxia zhongwei* */
				// getBillCardPanel().setBodyValueAt(null, i,
				// "cadvisecalbodyid");
				// getBillCardPanel().setBodyValueAt(null, i, "cadvisecalbody");
				// getBillCardPanel().setBodyValueAt(null, i,
				// "cbodywarehouseid");
				// getBillCardPanel().setBodyValueAt(null, i,
				// "cbodywarehousename");
				/** 保留库存组织、仓库 V502 jindongmei jiangchunxia zhongwei* */

				/** 去掉与合同的关联 V502 dongwei zhongwei* */
				getBillCardPanel().setBodyValueAt(null, i, "ct_manageid");
				getBillCardPanel().setBodyValueAt(null, i, "ct_name");
				getBillCardPanel().setBodyValueAt(null, i, "ct_code");
				getBillCardPanel().setBodyValueAt(null, i, "ctinvclassid");
				getBillCardPanel().setBodyValueAt(null, i, "ctinvclass");
				/** 去掉与合同的关联 V502 dongwei zhongwei* */

				// 如果基价含税
				if (SA_02.booleanValue()) {
					if (getBillCardPanel().getBodyItem("norgqttaxprc").isShow())
						getBillCardPanel().getBillModel().setCellEditable(i, "norgqttaxprc", true);
					else
						getBillCardPanel().getBillModel().setCellEditable(i,
								"noriginalcurtaxprice", true);
					if (getBillCardPanel().getBodyItem("norgqttaxnetprc").isShow())
						getBillCardPanel().getBillModel().setCellEditable(i, "norgqttaxnetprc",
								true);
					else
						getBillCardPanel().getBillModel().setCellEditable(i,
								"noriginalcurtaxnetprice", true);

					getBillCardPanel().getBillModel()
							.setCellEditable(i, "noriginalcurtaxmny", true);
					getBillCardPanel().getBillModel()
							.setCellEditable(i, "noriginalcursummny", true);
					getBillCardPanel().getBillModel().setCellEditable(i, "noriginaldiscountmny",
							true);
				} else {
					if (getBillCardPanel().getBodyItem("norgqtprc").isShow())
						getBillCardPanel().getBillModel().setCellEditable(i, "norgqtprc", true);
					else
						getBillCardPanel().getBillModel().setCellEditable(i, "norginalcurprice",
								true);
					if (getBillCardPanel().getBodyItem("norgqtnetprc").isShow())
						getBillCardPanel().getBillModel().setCellEditable(i, "norgqtnetprc", true);
					else
						getBillCardPanel().getBillModel().setCellEditable(i,
								"noriginalcurnetprice", true);

					getBillCardPanel().getBillModel().setCellEditable(i, "noriginalcurmny", true);
					getBillCardPanel().getBillModel()
							.setCellEditable(i, "noriginalcursummny", true);
					getBillCardPanel().getBillModel().setCellEditable(i, "noriginaldiscountmny",
							true);
				}

			}// end for

			/** 编辑前控制* */
			// // 设置合同名称不可编辑
			// getBillCardPanel().getBodyItem("ct_name").setEdit(false);
			getBillCardPanel().updateValue();
			getBillCardPanel().getBillModel().reCalcurateAll();
			getBillCardPanel().setEnabled(true);
			// 订单号可编辑
			// getBillCardPanel().getHeadItem("vreceiptcode").setEnabled(true);
			// 存货编码可编辑
			int rowcount = getBillCardPanel().getRowCount();
			for (int i = 0; i < rowcount; i++)
				getBillCardPanel().setCellEditable(i, "cinventorycode", false);

			// 清除缓存数据
			getBillCardTools().clearCatheData();

			/** v5.3去掉帐期字段 * */
			/*
			 * if (saleorderVO.getHeadVO().getNaccountperiod() != null &&
			 * saleorderVO.getHeadVO().getNaccountperiod() .doubleValue() < 0.0)
			 * getBillCardPanel().setHeadItem("naccountperiod", null);
			 */

		} catch (Exception e) {
			nc.vo.scm.pub.SCMEnv.out("数据加载失败！");
			nc.vo.scm.pub.SCMEnv.out(e.getMessage());
		}

		// 保存单据模板的编辑状态
		getBillCardTools().resumeBillItemEditToInit();

		// 设置按纽状态
		setButtons();
		setButtonsState();
		setNoEditItem();

		// 加自由项变色龙。
		try {
			InvAttrCellRenderer ficr = new InvAttrCellRenderer();
			ficr.setFreeItemRenderer(getBillCardPanel(), getBillCardPanel().alInvs);
		} catch (Exception e) {

		}
		updateUI();

	}

	/**
	 * 返回列表。
	 * 
	 * 创建日期：(2001-4-25 10:48:26)
	 * 
	 */
	protected void onReturn() {

		// 卡片上有数据
		boolean bcardHasData = false;
		if (getBillCardTools().getHeadValue("csaleid") != null
				&& getBillCardTools().getHeadValue("csaleid").toString().trim().length() != 0)
			bcardHasData = true;

		strShowState = "列表"; /*-=notranslate=-*/
		switchInterface();

		// selectRow = -1;
		// num = -1;

		getBillListPanel().getHeadTable().clearSelection();
		BillModel bmHead = getBillListPanel().getHeadBillModel();
		for (int i = 0, iRowCount = bmHead.getRowCount(); i < iRowCount; i++) {
			bmHead.setRowState(i, BillModel.NORMAL);
		}
		getBillListPanel().getBodyBillModel().clearBodyData();

		// 清空卡片信息
		getBillCardPanel().addNew();
		getBillCardPanel().getHeadItem("vreceiptcode").setValue(null);

		initIDs();

		setButtons(getBillButtons());

		updateUI();

		// 原先卡片上有数据
		if (bcardHasData && num>=0) {
			// 模拟表头行变换事件，使切换到列表后焦点再原先列表选中行上
			getBillListPanel().bodyRowChange(
					new BillEditEvent(getBillListPanel().getHeadTable(), -1, num));
			UITable table = getBillListPanel().getHeadTable();
			table.getSelectionModel().setSelectionInterval(num, num);
			table.scrollRectToVisible(table.getCellRect(num, 0, true));
			
			// 处理辅计量
			new UnitOfMeasureTool(getBillListPanel().getBodyBillModel()).calculateUnitOfMeasure();

			// 改变按钮状态
			setButtonsState();
		}

	}

	/**
	 * 退货界面下的返回
	 * 
	 */
	protected void onBack() {

		b_refundment = false;
		// 清空卡片界面信息
		getBillCardPanel().addNew();
		getBillCardPanel().getHeadItem("vreceiptcode").setValue(null);
		getBillCardPanel().setEnabled(false);

		// 返回上一级界面
		if (strUISource.equals("列表"))
			onReturn();
		else
			onCard();
		setBodyMenuItem(true);// 退货切换订单，更新右键菜单
		// 恢复正常背景色
		nc.ui.scm.pub.panel.SetColor.resetColor(getBillCardPanel());
	}

	/**
	 * 保存单据。
	 * 
	 * 创建日期：(2001-4-21 10:21:54)
	 * 
	 * 修改日期：2003-10-31 修改人：杨涛 修改内容：订单预收款比例和金额的计算
	 * 
	 */
	protected boolean onSave() {
		//山东胜利股份项目问题，做到通版里 去掉提示
		// 保存时判断表体行上的价格是否手工修改，若选择为是，返回单据界面
//		if(SA_15.booleanValue()) {
//			if (getChangePriceRowFromCardUI().size() > 0
//					&& !"true".equals(getBillCardPanel().getHeadItem("bpocooptome").getValue())) {
//				if (showYesNoMessage(NCLangRes.getInstance().getStrByID("40060301",
//						"UPT40060301-000576") /* @res"手工修改的存货价格，是否不重新询价，直接保存？" */) != nc.ui.pub.beans.UIDialog.ID_YES) {
//					showHintMessage(NCLangRes.getInstance().getStrByID("scmcommon",
//							"UPPSCMCommon-000291")/* @res "修改单据" */);
//					return true;
//				}
//				// 恢复正常背景色
//				nc.ui.scm.pub.panel.SetColor.resetColor(getBillCardPanel());
//			}
//		}
		// 恢复正常背景色
		nc.ui.scm.pub.panel.SetColor.resetColor(getBillCardPanel());

		// 处理通过复制增加的订单保存（保存后的后续处理不同）
		if ("新增".equals(strState) || getBillListPanel().isTempVO()) { /*-=notranslate=-*/
			onSaveCopyBill();
			return false;
		}

		try {
			getBillCardPanel().cleanNullLine();

			if (!BillRowNo.verifyRowNosCorrect(getBillCardPanel(), getRowNoItemKey())) {
				showWarningMessage(NCLangRes.getInstance().getStrByID(
						"common",
						"MC1",
						null,
						new String[] { NCLangRes.getInstance()
								.getStrByID("common", "UC000-0003389") }));
				return false;
			}

			SaleOrderVO saleorder = (SaleOrderVO) getBillCardPanel().getBillValueChangeVO(
					SaleOrderVO.class.getName(), SaleorderHVO.class.getName(),
					SaleorderBVO.class.getName());
			SaleOrderVO oldsaleorder = (SaleOrderVO) getBillCardPanel().getBillValueVO(
					SaleOrderVO.class.getName(), SaleorderHVO.class.getName(),
					SaleorderBVO.class.getName());

			// 单据类型
			((SaleorderHVO) saleorder.getParentVO()).setCreceipttype(SaleBillType.SaleOrder);

			// yt add 2003-10-31
			// 处理预收款比例和预收款金额
			if (!calculatePreceive(oldsaleorder))
				return false;

			SaleorderBVO[] bvos = saleorder.getBodyVOs();
			if (bvos != null) {
				for (int i = 0, iLen = bvos.length; i < iLen; i++) {
					if (bvos[i].getBoosflag() == null)
						bvos[i].setBoosflag(new UFBoolean(false));
				}
			}

			// 数据检查
			onCheck(oldsaleorder);
			// 获得行号
			saleorder = getLineNumber(oldsaleorder, saleorder);

			// 检验行号
			if (!nc.ui.scm.pub.report.BillRowNo.verifyRowNosCorrect(getBillCardPanel(),
					getRowNoItemKey()))
				return false;

			// 公司主键
			((SaleorderHVO) saleorder.getParentVO()).setPk_corp(getCorpPrimaryKey());
			// 号是否修改
			((SaleorderHVO) saleorder.getParentVO()).setBcodechanged(m_isCodeChanged);
			// if (m_isCodeChanged == true) {

			saleorder.getHeadVO().setVoldreceiptcode(
					getBillCardTools().getOldsaleordervo() == null ? null : getBillCardTools()
							.getOldsaleordervo().getHeadVO().getVreceiptcode());
			// 收货地址
			UIRefPane vreceiveaddress = (UIRefPane) getBillCardPanel().getHeadItem(
					"vreceiveaddress").getComponent();
			saleorder.getHeadVO().setVreceiveaddress(vreceiveaddress.getUITextField().getText());

			oldsaleorder.getHeadVO().setVreceiveaddress(vreceiveaddress.getUITextField().getText());
			if (coPoTs != null && saleorder.isCoopped()) {
				// 协同ts校验
				((SaleorderHVO) saleorder.getParentVO()).setCoopPoTs(coPoTs);

			}

			if (saleorder.getChildrenVO() != null) {
				for (int i = 0; i < saleorder.getChildrenVO().length; i++) {
					// 公司主键
					((SaleorderBVO) saleorder.getChildrenVO()[i]).setPkcorp(getCorpPrimaryKey());
					if (strState.equals("修订") /*-=notranslate=-*/
							&& saleorder.getChildrenVO()[i].getStatus() == VOStatus.UPDATED)
						saleorder.getChildrenVO()[i]
								.setStatus(nc.vo.pub.bill.BillVOStatus.MODIFICATION);
					/** v5.3 MM 梁微子 生产制造要求 */
					else if (strState.equals("修订") /*-=notranslate=-*/
							&& saleorder.getChildrenVO()[i].getStatus() == VOStatus.NEW)
						((SaleorderBVO) saleorder.getChildrenVO()[i])
								.setFrowstatus(BillStatus.AUDIT);
					/** v5.3 MM 梁微子 生产制造要求 */
				}
			}
			if (strState.equals("修改") || strState.equals("修订")) { /*-=notranslate=-*/
				saleorder.setStatus(VOStatus.UPDATED);
			}

			saleorder.setCuruserid(getClientEnvironment().getUser().getPrimaryKey());
			saleorder.setDcurdate(getClientEnvironment().getDate());
			saleorder.setCusername(getClientEnvironment().getUser().getUserName());
			saleorder.setCcorpname(getClientEnvironment().getCorporation().getUnitname());
			oldsaleorder.setCuruserid(getClientEnvironment().getUser().getPrimaryKey());
			oldsaleorder.setDcurdate(getClientEnvironment().getDate());
			saleorder.setCnodename(NCLangRes.getInstance().getStrByID("40060301",
					"UPP40060301-000504")
			/* @res "訂單管理" */);

			showHintMessage(NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000136")/*
																								 * @res
																								 * "开始保存数据...."
																								 */);
			// // 销售订单拉式协同采购定单，回写采购订单标志（孔晓东）ccooppohid
			if (saleorder.getHeadVO().isCoopped() && saleorder.getHeadVO().getCsaleid() == null) {
				saleorder.getHeadVO().setCoopPoId(coPoOrderId);
				saleorder.getHeadVO().setCbiztype(coBusiType);
			}
			nc.vo.scm.pub.SCMEnv.out("开始保存：" + System.currentTimeMillis());

			// 第一次保存合同保存回写
			saleorder.setFirstTime(true);
			boolean bContinue = true;
			boolean bRight = true;
			java.util.ArrayList listID = null;
			HashMap reths = null;

			fillDataBeforeSave(saleorder, oldsaleorder);

			nc.vo.scm.pub.session.ClientLink clientLink = new nc.vo.scm.pub.session.ClientLink(
					getClientEnvironment());
			saleorder.setClientLink(clientLink);

			// 插件支持
			getPluginProxy().beforeAction(Action.SAVE, new SaleOrderVO[] { saleorder });

			while (bContinue) {
				try {
					if (strState.equals("修改")) { /*-=notranslate=-*/
						// 单据状态
						int iStatus = Integer.parseInt(getBillCardPanel().getHeadItem("fstatus")
								.getValue());
						UFDateTime ud = ClientEnvironment.getServerTime();
						saleorder.getHeadVO().setAttributeValue("dmoditime", ud);
						
						//是否编辑了整单折扣或者价税合计
						boolean ifeditHead = false;
						
						if (iStatus == BillStatus.NOPASS) {
							// 主表VO状态
							listID = (java.util.ArrayList) PfUtilClient.processActionNoSendMessage(
									this, "SpecialWrite", SaleBillType.SaleOrder,
									getClientEnvironment().getDate().toString(), oldsaleorder,
									null, null, null);
							Integer istatus = new Integer(BillStatus.FREE);
							getBillCardPanel().setHeadItem("fstatus", istatus);
							getBillCardPanel().setTailItem("capproveid", null);
							getBillCardPanel().setTailItem("dapprovedate", null);
							getBillCardPanel().setTailItem("daudittime", null);
							for (int i = 0, loop = getBillCardPanel().getRowCount(); i < loop; i++) {
								getBillCardPanel().setBodyValueAt(istatus, i, "frowstatus");
							}

						} else {
							/**
							 * 编辑了整单折扣或者价税合计后表体行都应该更新
							 * oldsaleorder中bvo中的状态已经在fillDataBeforeSave中设置过
							 */
							SaleOrderVO oldvo = getBillCardTools().getOldsaleordervo();// 获取编辑前的VO
							if (oldvo.getHeadVO().getNdiscountrate().compareTo(
									oldsaleorder.getHeadVO().getNdiscountrate()) != 0) {
								saleorder.setChildrenVO(oldsaleorder.getChildrenVO());
								ifeditHead = true;
							}
							listID = (java.util.ArrayList) PfUtilClient.processActionNoSendMessage(
									this, "PreModify", SaleBillType.SaleOrder,
									getClientEnvironment().getDate().toString(), saleorder, null,
									null, null);

						}
						getBillCardPanel().setTailItem("dmoditime", ud);
						// 重载附表ID
						loadIDafterEDIT(listID,ifeditHead);
						if (listID != null)
							reths = (HashMap) listID.get(listID.size() - 1);
					}
					if (strState.equals("修订")) { /*-=notranslate=-*/
						UFDateTime ud = ClientEnvironment.getServerTime();
						saleorder.getHeadVO().setAttributeValue("dmoditime", ud);

						listID = (java.util.ArrayList) PfUtilClient.processActionNoSendMessage(
								this, "OrderAlter", SaleBillType.SaleOrder, getClientEnvironment()
										.getDate().toString(), saleorder, null, null, null);

						if (listID != null) {
							reths = (HashMap) listID.get(listID.size() - 1);
						}

						getBillCardPanel().setTailItem("dmoditime", ud);
						// 重新加载数据
						loadCardData();

					}
					// 合同回写是否作第二次
					bContinue = false;
					bRight = true;
				} catch (Exception ex) {
					bRight = false;
					bContinue = doException(saleorder, oldsaleorder, ex);
				}
			}
			if (!bRight) {
				showHintMessage(NCLangRes.getInstance()
						.getStrByID("40060301", "UPP40060301-000185")/*
																		 * @res
																		 * "保存失败！"
																		 */);
				return false;
			}

			nc.vo.scm.pub.SCMEnv.out("保存结束：" + System.currentTimeMillis());
			showHintMessage(NCLangRes.getInstance().getStrByID("common", "UCH005"));

			if (!strState.equals("退货")) /*-=notranslate=-*/
				strState = "自由"; /*-=notranslate=-*/

			//
			// String sourceId = (String) saleorder.getHeadVO().getCoopPoId();
			// reWritePO(new String[] { sourceId }, true);
			//
			// }

			// getBillCardTools().setHeadRefLimit(strState);

			// 加载时间戳
			// reLoadTS();
			getBillCardTools().reLoadConsignCorpAndCalbody(reths);

			vRowATPStatus = new Vector();
			for (int i = 0; i < getBillCardPanel().getRowCount(); i++) {
				vRowATPStatus.addElement(new UFBoolean(false));
			}

			// 查询现收金额
			if (reths != null) {
				getBillCardPanel().setHeadItem("nreceiptcathmny", reths.get("queryCachPayByOrdId"));
			}

			updateCacheVO();

			// 发票号是否修改
			m_isCodeChanged = false;

			getBillCardPanel().updateValue();

			// 加自由项变色龙。
			InvVO[] invvos = new InvVO[getBillCardPanel().getRowCount()];
			for (int i = 0; i < invvos.length; i++) {
				invvos[i] = new InvVO();
				invvos[i].setCinventoryid((String) getBillCardPanel().getBodyValueAt(i,
						"cinventoryid"));
			}

			InvoInfoBYFormula invvosget = new InvoInfoBYFormula();
			invvos = invvosget.getQuryInvVOs(invvos, false, true, 1);

			ArrayList al_invs = new ArrayList();
			for (InvVO invvo : invvos) {
				al_invs.add(invvo);
			}

			InvAttrCellRenderer ficr = new InvAttrCellRenderer();
			ficr.setFreeItemRenderer(getBillCardPanel(), al_invs);

			// 插件支持
			getPluginProxy().afterAction(Action.SAVE, new SaleOrderVO[] { saleorder });

			updateUI();
			
			loadCardData();  //wanglei 2014-05-15 因为有后台处理的数据，这里重新加载当前单据
			
			if (saleorder.getHeadVO().isCoopped()
					&& tempBillVoCache.getTempType() == tempBillVoCache.COOP
					&& tempBillVoCache.getSize() > 0) {
				synchronizedTempAfterSave();

			}

			return true;
		} catch (AtpSetException atpsetex) {

			showWarningMessage(atpsetex.getMessage());
			getBillCardTools().processAtpSetException(atpsetex.getMessage());
			return false;
		} catch (Exception e) {

			if (e instanceof ValidationException) // 错误行颜色标记
				nc.ui.scm.pub.panel.SetColor.setErrRowColor(getBillCardPanel(), rowList);

			// 标识该行
			if (e.getClass() != AtpCheckException.class
					|| (e.getClass() == AtpCheckException.class))/*-=notranslate=-*/

				showWarningMessage(e.getMessage());
			showHintMessage(NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000185")/*
																								 * @res
																								 * "保存失败！"
																								 */);
			markRow(e.getMessage());
			SCMEnv.out(e);
			return false;
		}
	}

	/**
	 * 增加复制功能，增加对复制单据的保存支持。
	 * 
	 * 创建日期：(2003-8-25 10:21:54)
	 * 
	 */
	protected void onSaveCopyBill() {
		if (!onSaveExt())
			return;
		
		// 新增的复制单据保存成功后，进行相关的后续处理
		SaleOrderVO oldsaleorder = (SaleOrderVO) getBillCardPanel().getBillValueVO(
				SaleOrderVO.class.getName(), SaleorderHVO.class.getName(),
				SaleorderBVO.class.getName());
		
		if (oldsaleorder != null) {
			vIDs.add(((SaleorderHVO) oldsaleorder.getParentVO()).getCsaleid());
			num = vIDs.size() - 1;
			addCacheVO();
		}
		
		loadCardData(); //wanglei 2014-05-15 如上
		
		getBillCardPanel().updateValue();
	}

	/**
	 * 保存前填充数据
	 */
	private void fillDataBeforeSave(SaleOrderVO saleorder, SaleOrderVO oldsaleorder)
			throws Exception {

		if (strState.equals("修改")) { /*-=notranslate=-*/
			saleorder.setPrimaryKey(getBillID());
			// 单据状态
			int iStatus = Integer.parseInt(getBillCardPanel().getHeadItem("fstatus").getValue());

			if (iStatus == BillStatus.NOPASS) {
				// 主表VO状态
				if (saleorder != null && saleorder.getBodyVOs() != null) {
					ArrayList dellinelist = new ArrayList();
					for (int i = 0, loop = saleorder.getBodyVOs().length; i < loop; i++) {
						if (saleorder.getBodyVOs()[i].getStatus() == VOStatus.DELETED) {
							dellinelist.add(saleorder.getBodyVOs()[i]);
						}
					}
					if (dellinelist.size() > 0) {
						SaleorderBVO[] newbvos = new SaleorderBVO[oldsaleorder.getBodyVOs().length
								+ dellinelist.size()];
						System.arraycopy(oldsaleorder.getBodyVOs(), 0, newbvos, 0, oldsaleorder
								.getBodyVOs().length);
						int pos = 0;
						for (int i = oldsaleorder.getBodyVOs().length, loop = newbvos.length; i < loop; i++) {
							newbvos[i] = (SaleorderBVO) dellinelist.get(pos);
							pos++;
						}
						oldsaleorder.setChildrenVO(newbvos);
					}
				}
				oldsaleorder.setStatus(VOStatus.UPDATED);
				oldsaleorder.getParentVO().setStatus(VOStatus.UPDATED);
				((SaleorderHVO) oldsaleorder.getParentVO())
						.setFstatus(new Integer(BillStatus.FREE));

				// 审批不通过后制单人修改保存，清空审批相关信息
				oldsaleorder.getHeadVO().setCapproveid(null);
				oldsaleorder.getHeadVO().setDapprovedate(null);
				oldsaleorder.getHeadVO().setDaudittime(null);

				// 子表VO状态
				SaleorderBVO[] voBodys = (SaleorderBVO[]) oldsaleorder.getChildrenVO();
				for (int i = 0; i < voBodys.length; i++) {
					if (voBodys[i].getStatus() == VOStatus.UNCHANGED)
						voBodys[i].setStatus(VOStatus.UPDATED);
					voBodys[i].setFrowstatus(new Integer(BillStatus.FREE));
					// 设置ATP更新方式
					voBodys[i].setIAction(ISaleOrderAction.A_DEFAULT);
				}

				// 获取编辑前的VO
				SaleOrderVO oldvo = getBillCardTools().getOldsaleordervo();
				for (int i = 0, loop = oldvo.getBodyVOs().length; i < loop; i++) {
					oldvo.getBodyVOs()[i].setIAction(ISaleOrderAction.A_DEFAULT);
				}

				oldsaleorder.setOldSaleOrderVO(oldvo);
				// 设置更新VO动作
				oldsaleorder.setIAction(ISaleOrderAction.A_SPECIALADD);

				// 设置修改后的完整VO
				saleorder.setAllSaleOrderVO((SaleOrderVO) getBillCardPanel().getBillValueChangeVO(
						SaleOrderVO.class.getName(), SaleorderHVO.class.getName(),
						SaleorderBVO.class.getName()));

				oldsaleorder.processVOForTrans();

			} else {

				// 删除订单中的缺货存货行
				saleorder = remarkOOSLine(saleorder);
				oldsaleorder = remarkOOSLine(oldsaleorder);

				oldsaleorder.setStatus(VOStatus.UPDATED);

				// 获取编辑前的VO
				SaleOrderVO oldvo = getBillCardTools().getOldsaleordervo();
				if (oldvo.getHeadVO().getNheadsummny().compareTo(
						oldsaleorder.getHeadVO().getNheadsummny()) != 0
						|| oldvo.getHeadVO().getNdiscountrate().compareTo(
								oldsaleorder.getHeadVO().getNdiscountrate()) != 0) {
					// 子表VO状态
					SaleorderBVO[] voBodys = (SaleorderBVO[]) oldsaleorder.getChildrenVO();
					for (int i = 0; i < voBodys.length; i++)
						if (voBodys[i].getStatus()!=VOStatus.DELETED)
							voBodys[i].setStatus(VOStatus.UPDATED);
				}

				// 获取编辑前的VO
				saleorder.setOldSaleOrderVO(getBillCardTools().getOldsaleordervo());
				// 设置更新VO动作
				saleorder.setIAction(ISaleOrderAction.A_EDIT);
				// 设置修改后的完整VO
				saleorder.setAllSaleOrderVO(oldsaleorder);

				saleorder.processVOForTrans();

			}

			saleorder.getHeadVO().setAttributeValue("dmoditime", ClientEnvironment.getServerTime());

		} else if (strState.equals("修订")) { /*-=notranslate=-*/

			saleorder.setPrimaryKey(getBillID());
			// actionname = "OrderAlter";
			// saleorderch = saleorder;

			// 版本号以及修订人等
			doEditBeforeModify(saleorder);

			// 删除订单中的缺货存货行
			oldsaleorder = remarkOOSLine(oldsaleorder);
			oldsaleorder.setStatus(VOStatus.UPDATED);

			// 获取编辑前的VO
			saleorder.setOldSaleOrderVO(getBillCardTools().getOldsaleordervo());
			// 设置更新VO动作
			saleorder.setIAction(ISaleOrderAction.A_MODIFY);
			// 设置修改后的完整VO
			saleorder.setAllSaleOrderVO(oldsaleorder);

			nc.vo.scm.pub.session.ClientLink clientLink = new nc.vo.scm.pub.session.ClientLink(
					getClientEnvironment());
			saleorder.setClientLink(clientLink);

			SaleorderBVO[] ordbvos = saleorder.getBodyVOs();
			if (ordbvos != null && ordbvos.length > 0) {
				UFDouble ntotalconvertnum = null;
				for (int i = 0, loop = ordbvos.length; i < loop; i++) {
					ntotalconvertnum = SoVoConst.duf0;
					// +narrangescornum
					ntotalconvertnum = SoVoTools.getMnyAdd(ntotalconvertnum, ordbvos[i]
							.getNarrangescornum());
					// +narrangepoapplynum
					ntotalconvertnum = SoVoTools.getMnyAdd(ntotalconvertnum, ordbvos[i]
							.getNarrangepoapplynum());
					// +narrangetoornum
					ntotalconvertnum = SoVoTools.getMnyAdd(ntotalconvertnum, ordbvos[i]
							.getNarrangetoornum());
					// +norrangetoapplynum
					ntotalconvertnum = SoVoTools.getMnyAdd(ntotalconvertnum, ordbvos[i]
							.getNorrangetoapplynum());
					// +narrangemonum
					ntotalconvertnum = SoVoTools.getMnyAdd(ntotalconvertnum, ordbvos[i]
							.getNarrangemonum());
					if (ntotalconvertnum != null && ordbvos[i].getNnumber() != null
							&& ntotalconvertnum.abs().compareTo(ordbvos[i].getNnumber().abs()) >= 0) {
						ordbvos[i].setBarrangedflag(SoVoConst.buftrue);
					}
				}
			}

			saleorder.processVOForTrans();
		}

		// 状态
		SaleorderBVO[] ordbvos = saleorder.getBodyVOs();
		for (SaleorderBVO ordbvo : ordbvos) {
			if (ordbvo.getStatus() == VOStatus.UPDATED
					&& (ordbvo.getCorder_bid() == null || ordbvo.getCorder_bid().length() == 0))
				ordbvo.setStatus(VOStatus.NEW);
		}
	}
	
	/**新订单表头ID(解决保存时，拔掉网线导致数据重复的问题)*/
	private String tempHeaderID = null;
	
    private void setOID(String pk_corp) throws Exception {
		if (tempHeaderID == null) {
			tempHeaderID = (String) SOGenMethod.callSOEJBService(
					"nc.impl.scm.so.so001.SaleOrderDMO", "getOID", 
					new Class[] { String.class }, new Object[] { pk_corp });
		}
	}
	private String getOID() { 
		return tempHeaderID;
	}
	public void clearOID() {
		tempHeaderID = null;
	}
	/**新订单表头ID(解决保存时，拔掉网线导致数据重复的问题)*/

	/**
	 * 保存数据，增加对订单管理界面中复制新增单据保存的支持（因订单 保存后的处理与标准订单录入不同）。
	 * 
	 * 参数： 无 返回值：boolean,成功返回true
	 * 
	 * 创建日期：(2003-8-25 10:21:54)
	 * 
	 */
	protected boolean onSaveExt() {

	    //创建时间工具类实例
		nc.vo.scm.pu.Timer timer = new nc.vo.scm.pu.Timer();
		timer.start();
		
		long s = System.currentTimeMillis();
		
		boolean bret = false;
		String actionname = null;
		if (strState.equals("新增") || getBillListPanel().isTempVO()) {/*-=notranslate=-*/
			actionname = "PreKeep";
		} else if (strState.equals("修改")) {/*-=notranslate=-*/
			actionname = "PreModify";
		}
		SaleOrderVO saleorder = null;

		try {

			getBillCardPanel().cleanNullLine();
			saleorder = (SaleOrderVO) getBillCardPanel().getBillValueChangeVO(
					SaleOrderVO.class.getName(), SaleorderHVO.class.getName(),
					SaleorderBVO.class.getName());
			SaleOrderVO saleoldorder = (SaleOrderVO) getBillCardPanel().getBillValueVO(
					SaleOrderVO.class.getName(), SaleorderHVO.class.getName(),
					SaleorderBVO.class.getName());

			// 单据类型
			((SaleorderHVO) saleorder.getParentVO()).setCreceipttype(getBillType());
			((SaleorderHVO) saleoldorder.getParentVO()).setCreceipttype(getBillType());
			getBillCardPanel().setHeadItem("creceipttype", getBillType());
			// 业务类型(加入协同时单据类型)
			if (saleorder.getHeadVO().getAttributeValue("ccooppohid") != null) {
				getBillCardPanel().setBusiType(coBusiType);
				((SaleorderHVO) saleorder.getParentVO()).setCbiztype(coBusiType);
				getBillCardPanel().setHeadItem("cbiztype", coBusiType);

			} else {
				((SaleorderHVO) saleorder.getParentVO()).setCbiztype(getBillCardPanel()
						.getBusiType());
				getBillCardPanel().setHeadItem("cbiztype", getBillCardPanel().getBusiType());
			}

			// yt add 2003-11-07
			if (calculatePreceive(saleoldorder) == false)
				return false;
			// 检测
			if (strState.equals("新增"))/*-=notranslate=-*/
				onCheck(saleorder);
			else
				onCheck(saleoldorder);
			// 获得行号
			saleorder = getLineNumber(saleoldorder, saleorder);
			// 检验行号
			if (!BillRowNo.verifyRowNosCorrect(getBillCardPanel(), getBillCardPanel()
					.getRowNoItemKey()))
				return bret;
			// 单据状态
			((SaleorderHVO) saleorder.getParentVO()).setFstatus(new Integer(BillStatus.FREE));
			// 公司主键
			((SaleorderHVO) saleorder.getParentVO()).setPk_corp(getCorpPrimaryKey());
			// 收货地址
			UIRefPane vreceiveaddress = (UIRefPane) getBillCardPanel().getHeadItem(
					"vreceiveaddress").getComponent();
			saleorder.getHeadVO().setVreceiveaddress(vreceiveaddress.getUITextField().getText());

			if (saleorder.getChildrenVO() != null) {
				for (int i = 0; i < saleorder.getChildrenVO().length; i++) {
					SaleorderBVO bodyVO = (SaleorderBVO) saleorder.getChildrenVO()[i];
					// 公司主键
					bodyVO.setPkcorp(getCorpPrimaryKey());
					if (bodyVO.getStatus() == VOStatus.NEW)
						bodyVO.setPrimaryKey(null);
				}
			}

			saleorder.setCuruserid(getClientEnvironment().getUser().getPrimaryKey());
			saleorder.setDcurdate(getClientEnvironment().getDate());
			saleorder.setCusername(getClientEnvironment().getUser().getUserName());
			saleorder.setCcorpname(getClientEnvironment().getCorporation().getUnitname());

			saleoldorder.setCuruserid(getClientEnvironment().getUser().getPrimaryKey());
			saleoldorder.setDcurdate(getClientEnvironment().getDate());

			showHintMessage(NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000136")/*
																								 * @res
																								 * "开始保存数据...."
																								 */);
			if (saleorder.getHeadVO().isCoopped() && saleorder.getHeadVO().getCsaleid() == null) {
				saleorder.getHeadVO().setCoopPoId(coPoOrderId);
				saleorder.getHeadVO().setCbiztype(coBusiType);
			}

			if (coPoTs != null && saleorder.isCoopped()) {
				// 协同ts校验
				((SaleorderHVO) saleorder.getParentVO()).setCoopPoTs(coPoTs);

			}

			SCMEnv.out("开始保存：" + System.currentTimeMillis());
			// 第一次保存合同保存回写
			saleorder.setFirstTime(true);
			boolean bContinue = true;
			// HashMap reths = null;
			// 设置更新VO动作
			saleorder.setIAction(ISaleOrderAction.A_ADD);

			saleorder.setStatus(nc.vo.pub.VOStatus.NEW);
			saleorder.processVOForTrans();
			nc.vo.scm.pub.session.ClientLink clientLink = new nc.vo.scm.pub.session.ClientLink(
					getClientEnvironment());
			saleorder.setClientLink(clientLink);

			// 插件支持
			getPluginProxy().beforeAction(Action.SAVE, new SaleOrderVO[] { saleorder });
			
			while (bContinue) {
				try {
					if (strState.equals("新增") || getBillListPanel().isTempVO()) {/*-=notranslate=-*/
						UFDateTime ud = ClientEnvironment.getServerTime();
						saleorder.getHeadVO().setAttributeValue("dbilltime", ud);
						// 周长胜
						String editionNum = saleorder.getHeadVO().getEditionNum();
						int editionNumNew = new UFDouble(editionNum == null ? "1.0" : editionNum
								.trim()).intValue();
						editionNum = String.valueOf(editionNumNew) + ".0";
						saleorder.getHeadVO().setEditionNum(editionNum);
						getBillCardPanel().setHeadItem("editionnum", editionNum);
						
						/**新订单表头ID(解决保存时，拔掉网线导致数据重复的问题)*/
					    String csaleid = getOID();
					    if ( csaleid == null ) {
					     setOID( saleorder.getHeadVO().getPk_corp() );
					    }
					    saleorder.getHeadVO().setAttributeValue(SaleorderHVO.CSALEID_TEMP, getOID());
					    /**新订单表头ID(解决保存时，拔掉网线导致数据重复的问题)*/
						
						// 周长胜
						ArrayList alistID = (ArrayList) PfUtilClient.processActionNoSendMessage(
								this, "PreKeep", getBillType(), getClientEnvironment().getDate()
										.toString(), saleorder, null, null, null);
						
						getBillCardPanel().setTailItem("dbilltime", ud);
						id = (String) alistID.get(0);
						loadIDafterADD(alistID);
						
						/**新订单表头ID(解决保存时，拔掉网线导致数据重复的问题)*/
						clearOID();
						/**新订单表头ID(解决保存时，拔掉网线导致数据重复的问题)*/
					}

					bContinue = false;
				} catch (Exception ex) {
					bContinue = doException(saleorder, null, ex);
					if (!bContinue)
						return false;
				}
			}
			
			SCMEnv.out("保存结束：" + System.currentTimeMillis());

			// if (saleorder.getHeadVO().isCoopped() ) {
			//
			// String sourceId = (String) saleorder.getHeadVO().getCoopPoId();
			// reWritePO(new String[] { sourceId }, true);
			//
			// }

			// 单据状态
			strState = "自由";
			bret = true;

			// getBillCardTools().setHeadRefLimit(strState);

			vRowATPStatus = new Vector();
			for (int i = 0; i < getBillCardPanel().getRowCount(); i++) {
				vRowATPStatus.addElement(new UFBoolean(false));
			}
			getBillCardPanel().updateValue();

			// 加自由项变色龙。
			try {
				InvAttrCellRenderer ficr = new InvAttrCellRenderer();
				ficr.setFreeItemRenderer(getBillCardPanel(), null);
			} catch (Exception einv) {

			}

			// 插件支持
			getPluginProxy().afterAction(Action.SAVE, new SaleOrderVO[] { saleorder });
			if (saleorder.getHeadVO().isCoopped()
					&& tempBillVoCache.getTempType() == tempBillVoCache.COOP
					&& tempBillVoCache.getSize() > 0) {
				synchronizedTempAfterSave();

			}

		} catch (AtpSetException atpsetex) {

			showWarningMessage(atpsetex.getMessage());
			getBillCardTools().processAtpSetException(atpsetex.getMessage());
		} catch (Exception ex) {

			if (ex instanceof ValidationException) // 错误行颜色标记
				nc.ui.scm.pub.panel.SetColor.setErrRowColor(getBillCardPanel(), rowList);

			if (ex instanceof SQLException){
				showWarningMessage("由于网络不稳定，可能已经生成此单据致，请检查后，进行确认!");
				return bret;
			}
			
			// 标识该行
			handleSaveException(getBillType(), getBillCardPanel().getBusiType(), actionname,
					getBillCardPanel().getCorp(), getBillCardPanel().getOperator(), saleorder, ex);
			markRow(ex.getMessage());
			// ex.printStackTrace();
			return bret;
		}
		
		showHintMessage(NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000232",
				null, new String[] { (System.currentTimeMillis() - s) / 1000 + "" }));
		
		timer.addExecutePhase(">>>>>>>>>销售订单保存远程调用用时：>>>>>>>>>>>");
		//输出各个业务操作占用时间分布
		timer.showAllExecutePhase(">>>>>>>>>销售订单保存远程调用结束：>>>>>>>>>>>");
		
		return bret;
	}

	private void deleteTempData() {
		int[] deleteRows = getBillListPanel().getHeadTable().getSelectedRows();
		tempBillVoCache.remove(deleteRows);
		if (strShowState.equals("卡片")) {
			autoSwitchPanel();
		}
		loadTempDataTOPanel();

	}

	private void synchronizedTempAfterSave() {
		deleteTempData();

	}

	protected void onSendAudit() {
		// 单据状态
		if (!"自由".equals(strState)) {/*-=notranslate=-*/
			onSave();
		}
		if (!"自由".equals(strState)) {/*-=notranslate=-*/
			return;
		}
		SaleOrderVO saleorder = (SaleOrderVO) getVO(false);
		if (saleorder == null || saleorder.getParentVO() == null) {
			showErrorMessage(NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000199")/*
																									 * @res
																									 * "请选择单据"
																									 */);
		} else {
			if (!getClientEnvironment().getUser().getPrimaryKey().equals(
					saleorder.getHeadVO().getCoperatorid())) {
				showErrorMessage(NCLangRes.getInstance().getStrByID("40060301",
						"UPP40060301-000505")/*
												 * @res "必须由制单人送审"
												 */);
				return;
			}
			try {

				saleorder.validate();

				nc.vo.scm.pub.session.ClientLink clientLink = new nc.vo.scm.pub.session.ClientLink(
						getClientEnvironment());
				saleorder.setClientLink(clientLink);

				saleorder.setCuruserid(getClientEnvironment().getUser().getPrimaryKey());
				saleorder.setDcurdate(getClientEnvironment().getDate());
				saleorder.setCusername(getClientEnvironment().getUser().getUserName());
				saleorder.setCcorpname(getClientEnvironment().getCorporation().getUnitname());

				saleorder.setCnodename(NCLangRes.getInstance().getStrByID("40060301",
						"UPP40060301-000504")
				/* @res "訂單管理" */);

				saleorder.setIAction(ISaleOrderAction.A_SENDAUDIT);
				String ErrMsg = null;

				// v5.5流量控制(将不用的key置NULL)
				/*
				 * AggregatedValueObject transVO =
				 * TransferVOProcessUtil.transferProcessBySetNull(saleorder,
				 * nc.vo.so.pub.TransferVOProcessField.closeOPDown_HeadKeys_4331,
				 * nc.vo.so.pub.TransferVOProcessField.closeOPDown_BodyKeys_4331,
				 * SaleOrderVO.class.getName(), SaleorderHVO.class.getName(),
				 * SaleorderBVO.class.getName());
				 */

				ArrayList al = (ArrayList) PfUtilClient.processAction(this, "SAVE", getBillType(),
						getClientEnvironment().getDate().toString(), saleorder, null);

				ErrMsg = (String) al.get(1);
				if (ErrMsg != null && ErrMsg.startsWith("ERR")) {
					ErrMsg = ErrMsg.substring(3);
					if (ErrMsg != null && ErrMsg.trim().length() > 0) {
						showWarningMessage(ErrMsg);
						if (ErrMsg.indexOf("账期") > 0)
							getBillCardPanel().setHeadItem("boverdate", new UFBoolean(true));
					}
				}

				if (PfUtilClient.isSuccess()) {
					new BillTools().reLoadBillState(this, getClientEnvironment());

					setButtonsState();
					showHintMessage(NCLangRes.getInstance().getStrByID("40060301",
							"UPP40060301-000291")/*
													 * @res "送审成功!"
													 */);
				} else {
					showHintMessage(NCLangRes.getInstance().getStrByID("40060301",
							"UPP40060301-000292")/*
													 * @res "送审操作已经被用户取消！"
													 */);
				}
				
				//v5.6连接次数流量优化
				//reLoadTS();
				
				updateCacheVO();
			} catch (BusinessException e) {
				showWarningMessage(NCLangRes.getInstance().getStrByID("40060301",
						"UPP40060301-000293")/*
												 * @res "送审失败:"
												 */
						+ e.getMessage());
			} catch (Exception e) {
				showWarningMessage(NCLangRes.getInstance().getStrByID("40060301",
						"UPP40060301-000293")/*
												 * @res "送审失败:"
												 */
						+ e.getMessage());
			}
		}
	}

	/**
	 * 重新加载TS。
	 * 
	 * 创建日期：(2002-7-11 9:51:36)
	 * 
	 */
	private void reLoadTS() {
		try {
			if (getBillListPanel().isShowing())
				num = getBillListPanel().getHeadTable().getSelectedRow();
			int temp = num;
			String sBillID = getBillID();

			// 重新加载表头TS
			if (sBillID != null) {
				if (strShowState.equals("列表")) { /*-=notranslate=-*/

					String sql = "select so_sale.csaleid,"// 0
							+ "so_sale.vreceiptcode," + "so_sale.fstatus,"
							+ "so_sale.ts,"
							+ "so_sale.capproveid "// 4
							+ "from so_sale " + "where so_sale.csaleid='" + sBillID.trim() + "'";

					nc.vo.so.so001.SORowData[] rows = null;

					rows = nc.ui.so.so016.SOToolsBO_Client.getSORows(sql);

					if (rows != null && rows.length > 0) {

						Integer fstatus = rows[0].getInteger(2);

						String value = SaleOrderVO.getBillState(fstatus);
						// 更改表头数据
						getBillListPanel().getHeadBillModel().setValueAt(rows[0].getUFDateTime(3),
								temp, "ts");
						getBillListPanel().getHeadBillModel().setValueAt(value, temp, "fstatus");

						if (fstatus.intValue() == BillStatus.AUDIT
								|| fstatus.intValue() == BillStatus.FINISH
								|| fstatus.intValue() == BillStatus.FREEZE) {
							getBillListPanel().getHeadBillModel().setValueAt(
									getClientEnvironment().getUser().getPrimaryKey().toString(),
									temp, "capproveid");
							getBillListPanel().getHeadBillModel().setValueAt(
									getClientEnvironment().getUser().getUserName().toString(),
									temp, "capprovename");
							getBillListPanel().getHeadBillModel().setValueAt(
									getClientEnvironment().getDate().toString(), temp,
									"dapprovedate");
						} else {
							getBillListPanel().getHeadBillModel().setValueAt(null, temp,
									"capproveid");
							getBillListPanel().getHeadBillModel().setValueAt(null, temp,
									"capprovename");
							getBillListPanel().getHeadBillModel().setValueAt(null, temp,
									"dapprovedate");
						}
						// 更改表体数据
						if (getBillListPanel().getBodyBillModel() != null) {
							if (getBillListPanel().getBodyTable().getRowCount() > 0) {
								for (int i = 0; i < getBillListPanel().getBodyBillModel()
										.getRowCount(); i++) {
									getBillListPanel().getBodyBillModel().setValueAt(value, i,
											"frowstatus");
								}
							}
						}
					}
				} else {
					String formula[] = { "ts->getColValue(so_sale,ts,csaleid,csaleid)",

					"vreceiptcode->getColValue(so_sale,vreceiptcode,csaleid,csaleid)" };

					getBillCardPanel().execHeadFormulas(formula);

				}
			}
			updateUI();
		} catch (Exception e) {
			nc.vo.scm.pub.SCMEnv.out("重新加载表头TS失败.");
			nc.vo.scm.pub.SCMEnv.out(e.getMessage());
		}
	}

	/**
	 * 删除订单中的缺货存货行。
	 * 
	 * 创建日期：(2001-4-13 16:38:18)
	 * 
	 */
	private SaleOrderVO remarkOOSLine(SaleOrderVO voSource) {
		// 选择为缺货的存货标记为删除状态
		SaleorderBVO[] voBodys = (SaleorderBVO[]) voSource.getChildrenVO();
		for (int i = 0; i < voBodys.length; i++) {
			if (voBodys[i].getBoosflag() != null && voBodys[i].getBoosflag().booleanValue()) {
				if (voSource.getChildrenVO()[i].getStatus() == VOStatus.UPDATED) {
					voSource.getChildrenVO()[i].setStatus(VOStatus.DELETED);
					// 为缺货登记修改记录进行的约定
					((SaleorderBVO[]) voSource.getChildrenVO())[i].setBsupplyflag(new UFBoolean(
							true));
				}
			}
		}
		return voSource;
	}

	/**
	 * 删除订单中的缺货存货行。
	 * 
	 * 创建日期：(2001-4-13 16:38:18)
	 * 
	 */
	private void removeOOSLine() {
		int iRowCount = getBillCardPanel().getBillTable().getRowCount();
		Vector vecOOSLine = new Vector();
		for (int i = iRowCount - 1; i >= 0; i--) {
			UFBoolean boosflag = getBillCardPanel().getBodyValueAt(i, "boosflag") == null ? new UFBoolean(
					false)
					: new UFBoolean(getBillCardPanel().getBodyValueAt(i, "boosflag").toString());
			if (boosflag.booleanValue()) {
				if (getBillCardPanel().alInvs != null && i < getBillCardPanel().alInvs.size()) {
					getBillCardPanel().alInvs.remove(i);
				}
				if (vRowATPStatus != null && vRowATPStatus.size() > 0 && i < vRowATPStatus.size()) {
					vRowATPStatus.remove(i);
				}
				vecOOSLine.addElement(i + "");
			}
		}
		int[] aryRow = new int[vecOOSLine.size()];
		for (int i = 0; i < vecOOSLine.size(); i++) {
			aryRow[i] = new Integer(vecOOSLine.elementAt(i).toString()).intValue();
		}

		getBillCardPanel().getBillModel().delLine(aryRow);
	}

	/**
	 * 设置表体发货和到货日期默认数据。
	 * 
	 * 创建日期：(2001-8-27 10:05:59)
	 * 
	 */
	private void setBodyDate(int row) {
		// 订单日期
		UFDate dbilldate = new UFDate(getBillCardPanel().getHeadItem("dbilldate").getValue());
		// 发货日期
		getBillCardPanel().setBodyValueAt(dbilldate.toString(), row, "dconsigndate");
		// 到货日期
		getBillCardPanel().afterBodyDateEdit(row, true);
		String sDateDeliver = getBillCardPanel().getBodyValueAt(row, "ddeliverdate") == null ? null
				: getBillCardPanel().getBodyValueAt(row, "ddeliverdate").toString().trim();
		if (sDateDeliver == null || sDateDeliver.length() == 0) {
			getBillCardPanel().setBodyValueAt(dbilldate.toString(), row, "ddeliverdate");
		}
	}

	/**
	 * 设置按钮状态。 创建日期：(2001-3-17 9:00:09)
	 */
	private void setBomButtonsState() {
		// 保证只能定单修改时才可进行操作
		if (strOldState.equals("新增") || strOldState.equals("修改")) {/*-=notranslate=-*/
			if (strBomState.equals("ADD")) {
				boBomSave.setEnabled(true);
				boBomEdit.setEnabled(false);
				boBomCancel.setEnabled(true);
				boBomPrint.setEnabled(false);
				boBomReturn.setEnabled(false);
			}
			if (strBomState.equals("FREE")) {
				boBomSave.setEnabled(false);
				boBomEdit.setEnabled(true);
				boBomCancel.setEnabled(false);
				boBomPrint.setEnabled(true);
				boBomReturn.setEnabled(true);
				getBillTreeCardPanel().setEnabled(false);
			}
			if (strBomState.equals("SAVE")) {
				boBomSave.setEnabled(false);
				boBomEdit.setEnabled(true);
				boBomCancel.setEnabled(false);
				boBomPrint.setEnabled(true);
				boBomReturn.setEnabled(true);
				getBillTreeCardPanel().setEnabled(false);
			}
			if (strBomState.equals("EDIT")) {
				boBomSave.setEnabled(true);
				boBomEdit.setEnabled(false);
				boBomCancel.setEnabled(true);
				boBomPrint.setEnabled(false);
				boBomReturn.setEnabled(false);
			}
			if (strBomState.equals("CANCEL")) {
				boBomSave.setEnabled(false);
				boBomEdit.setEnabled(false);
				boBomCancel.setEnabled(true);
				boBomPrint.setEnabled(false);
				boBomReturn.setEnabled(true);
			}
			setBomPrice();
		} else {
			boBomSave.setEnabled(false);
			boBomEdit.setEnabled(false);
			boBomCancel.setEnabled(false);
			boBomPrint.setEnabled(true);
			boBomReturn.setEnabled(true);
			getBillTreeCardPanel().setEnabled(false);
		}
		updateButtons();
	}

	/**
	 * 根据状态设置按钮 创建日期：(01-2-26 13:29:17)
	 */
	private void setButtons() {

		if (strShowState.equals("列表")) { /*-=notranslate=-*/
			setButtons(aryListButtonGroup, "40060301");
		} else if (strShowState.equals("卡片")) { /*-=notranslate=-*/
			setButtons(aryButtonGroup, "40060301");
		} else {
			boLine.removeAllChildren();
			boLine.addChildButton(boDelLine);
			setButtons(aryBatchButtonGroup, "40060301");
		}
	}

	/**
	 * 设置按钮状态。
	 * 
	 * 创建日期：(2001-3-17 9:00:09)
	 * 
	 */
	public void setButtonsState() {

		if (bInMsgPanel)
			return;

		if (strState.equals("BOM"))
			setBomButtonsState();
		else if (strShowState.equals("列表")) {
			setListButtonsState();
		} else {
			setCardButtonsState();
		}

		// 二次开发扩展
		getPluginProxy().setButtonStatus();

	}

	/**
	 * 设置列表按钮状态。
	 * 
	 * 创建日期：(2001-3-17 9:00:09)
	 * 
	 */
	private void setListButtonsState() {

		boBusiType.setEnabled(true);
		boAdd.setEnabled(true);
		boBrowse.setEnabled(true);

		int selectRowCount = getBillListPanel()==null? 0 : getBillListPanel().getHeadTable().getSelectedRowCount();
		if (selectRowCount > 1) {
			boMaintain.setEnabled(true);
			boBlankOut.setEnabled(true);
			boAudit.setEnabled(true);
			boAction.setEnabled(true);
			boSendAudit.setEnabled(true);
			boCancelAudit.setEnabled(true);
			boSendAudit.setEnabled(false);
			boFreeze.setEnabled(false);
			boCancelFreeze.setEnabled(false);
			boClose.setEnabled(false);
			boRefundment.setEnabled(false);
			boQuery.setEnabled(true);
			boCard.setEnabled(true);
			boPrntMgr.setEnabled(true);
			boPreview.setEnabled(true);
			boPrint.setEnabled(true);
			boSplitPrint.setEnabled(true);
			boAssistant.setEnabled(true);
			boAsstntQry.setEnabled(false);
			boAuditFlowStatus.setEnabled(true);
		} else if (selectRowCount == 1) {
			boMaintain.setEnabled(true);
			boEdit.setEnabled(true);
			boAction.setEnabled(true);
			boPrntMgr.setEnabled(true);
			boPrint.setEnabled(true);
			boSplitPrint.setEnabled(true);
			boPreview.setEnabled(true);
			boAssistant.setEnabled(true);
			boAsstntQry.setEnabled(true);
			boAfterAction.setEnabled(true);
			boBatch.setEnabled(false);
			boRefundment.setEnabled(true);
			boQuery.setEnabled(true);
			boCard.setEnabled(true);
			boDocument.setEnabled(true);
			boOrderQuery.setEnabled(true);

			int ccount = boAdd.getChildCount();
			ButtonObject[] bos = boAdd.getChildButtonGroup();
			boolean bCanCopyFlag = false;
			for (int i = 0; i < ccount; i++) {
				if (bos[i].getTag().startsWith(SELFBILL)) {
					bCanCopyFlag = true;
					break;
				}
			}
			boCopyBill.setEnabled(bCanCopyFlag);
			boSendAudit.setEnabled(true);
			boAuditFlowStatus.setEnabled(true);
			
		} else {
			boMaintain.setEnabled(false);
			boEdit.setEnabled(false);
			boAudit.setEnabled(false);
			boAction.setEnabled(false);
			boPrntMgr.setEnabled(false);
			boPrint.setEnabled(false);
			boSplitPrint.setEnabled(false);
			boPreview.setEnabled(false);
			boAssistant.setEnabled(true);
			boAsstntQry.setEnabled(false);
			boBatch.setEnabled(false);
			boQuery.setEnabled(true);
			boCard.setEnabled(false);
			boRefundment.setEnabled(false);
			boAfterAction.setEnabled(false);
			boDocument.setEnabled(false);
			boOrderQuery.setEnabled(false);
			boCopyBill.setEnabled(false);
			boSendAudit.setEnabled(false);
			boAuditFlowStatus.setEnabled(false);
			boCoPushPo.setEnabled(false);

		}

		int iStatus = 0;
		if (selectRow > -1
				&& getBillListPanel().getHeadBillModel().getValueAt(selectRow, "fstatus") != null) {
			// 单据状态
			iStatus = Integer.parseInt(getBillListPanel().getHeadItem("fstatus").converType(
					getBillListPanel().getHeadBillModel().getValueAt(selectRow, "fstatus"))
					.toString());
			setBtnsByBillState(iStatus);
			// 退货标记
			Object retinvflag = getBillListPanel().getHeadBillModel().getValueAt(selectRow,
					"bretinvflag");
			if (retinvflag != null && retinvflag.toString().equals("true")) {
				// 根据单据状态设置单据
				boRefundment.setEnabled(false);
			}
		}

		if (getBillListPanel() != null && getBillListPanel().getHeadTable() != null) {
			if (getBillListPanel().getHeadTable().getRowCount() > 0) {
				boFind.setEnabled(true);
				getBoListDeselectAll().setEnabled(true);
				getBoListSelectAll().setEnabled(true);
			} else {
				boFind.setEnabled(false);
				getBoListDeselectAll().setEnabled(false);
				getBoListSelectAll().setEnabled(false);
			}
		} else {
			boFind.setEnabled(false);
		}

		boRefresh.setEnabled(b_query);

		/** 在列表下不起作用的按钮* */
		boSave.setEnabled(false);
		boCancel.setEnabled(false);
		boLine.setEnabled(false);
		boFirst.setEnabled(false);
		boPre.setEnabled(false);
		boNext.setEnabled(false);
		boLast.setEnabled(false);

		getBoOnHandShowHidden().setEnabled(false);
		
		if (BillStatus.FREE == iStatus){
			boAudit.setEnabled(!getBillListPanel().isTempVO());
			boSendAudit.setEnabled(!getBillListPanel().isTempVO());
		}
		
		/** 在列表下不起作用的按钮* */

		updateButtons();
	}

	protected final ButtonObject getBoAfterAction() {
		if (boAfterAction == null) {
			boAfterAction = new ButtonObject(NCLangRes.getInstance().getStrByID("40060301",
					"UPP40060301-000128")/*
											 * @res "后续业务"
											 */, NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000128")/*
																						 * @res
																						 * "后续业务"
																						 */, 0, "后续业务"); /*-=notranslate=-*/
		}
		return boAfterAction;
	}

	/**
	 * 新增单据时设置表头默认数据。
	 * 
	 * 创建日期：(2001-8-27 10:05:59)
	 * 
	 */
	private void setDefaultData(boolean isfree) {

		// 销售公司
		getBillCardPanel().setHeadItem("salecorp",
				getClientEnvironment().getCorporation().getUnitname());

		// 公司
		getBillCardPanel().setHeadItem("pk_corp", getCorpPrimaryKey());

		// 业务类型
		getBillCardPanel().setHeadItem("cbiztype", getBillCardPanel().getBusiType());

		// 客户
		getBillCardPanel().getHeadItem("ccustomerid").setEnabled(true);

		// 散户
		getBillCardPanel().getHeadItem("cfreecustid").setEnabled(false);

		// 单据号
		getBillCardPanel().setHeadItem("vreceiptcode", null);

		// 单据日期
		getBillCardPanel().setHeadItem("dbilldate", getClientEnvironment().getDate().toString());

		// 单据状态
		getBillCardPanel().setHeadItem("fstatus", "1");

		// 业务员
		if (getBillCardPanel().getHeadItem("cemployeeid").getValueObject() == null) {
			String cemployeeid = getCemployeeId();
			getBillCardPanel().setHeadItem("cemployeeid", cemployeeid);
			((UIRefPane) getBillCardPanel().getHeadItem("cemployeeid").getComponent())
					.setPK(cemployeeid);

			// 由业务员带出部门
			getBillCardPanel().afterEmployeeEdit(null);
		}

		// 制单日期
		getBillCardPanel().setTailItem("dmakedate", getClientEnvironment().getDate().toString());
		// 制单人
		getBillCardPanel().setTailItem("coperatorid",
				getClientEnvironment().getUser().getPrimaryKey());
		// 审核日期
		getBillCardPanel().setTailItem("capproveid", null);
		// 审核人
		getBillCardPanel().setTailItem("dapprovedate", null);

		// 自制
		if (isfree) {
			// 整单折扣
			getBillCardPanel().setHeadItem("ndiscountrate", 100.00 + "");
			// 预估运费
			getBillCardPanel().setHeadItem("nevaluatecarriage", 0.00 + "");
			// 单据号
			((UIRefPane) getBillCardPanel().getHeadItem("vreceiptcode").getComponent())
					.getUITextField().setDelStr("+");
		} else {

			getBillCardPanel().initFreeItem();
			getBillCardPanel().initUnit();
			
			//初始化合同信息
			if (getSouceBillType().equals(SaleBillType.SoContract)
					|| getSouceBillType().equals(SaleBillType.SoInitContract))
				initCTTypeVO();

			getBillCardTools().setBodyInventory1(0, getBillCardPanel().getRowCount());

			// 取渠道类型
			getBillCardTools().setBodyCchantypeid(0, getBillCardPanel().getRowCount());

			// 散户
			UFBoolean bfreecustflag = getBillCardTools().getHeadUFBooleanValue("bfreecustflag");
			if (bfreecustflag == null || !bfreecustflag.booleanValue()) {
				getBillCardPanel().getHeadItem("cfreecustid").setEnabled(false);
			} else {
				getBillCardPanel().getHeadItem("cfreecustid").setEnabled(true);
			}
			// 币种
			UIRefPane ccurrencytypeid = (UIRefPane) getBillCardPanel().getHeadItem(
					"ccurrencytypeid").getComponent();
			if (getBillCardPanel().getRowCount() > 0) {
				Object oTemp = getBillCardPanel().getBodyValueAt(0, "ccurrencytypeid");
				if (oTemp != null && ((String) oTemp).trim().length() > 0) {

					ccurrencytypeid.setPK(getBillCardPanel().getBodyValueAt(0, "ccurrencytypeid"));
					// 汇率
					getBillCardPanel().setHeadItem("nexchangeotobrate",
							getBillCardPanel().getBodyValueAt(0, "nexchangeotobrate"));

					if (getBillCardTools().getBodyUFDoubleValue(0, "nexchangeotobrate") == null) {
						getBillCardPanel().afterCurrencyChange();
					} else {
						ctlCurrencyEdit();
					}
				} else {
					// 默认交易币种
					String[] formulas = { "ccurrencytypeid->getColValue(bd_cumandoc,pk_currtype1,pk_cumandoc,ccustomerid)" };
					getBillCardPanel().getBillData().execHeadFormulas(formulas);
					// 设置默认值
					try {
						if (ccurrencytypeid.getRefPK() == null)
							ccurrencytypeid.setPK(CurrParamQuery.getInstance().getLocalCurrPK(getCorpPrimaryKey()));
						getBillCardPanel().afterCurrencyChange();
					} catch (java.lang.Exception e1) {
					}
				}
				
				if (getBillCardPanel().getHeadItem("ndiscountrate").getValueObject()==null)
					// 设置整单折扣
					getBillCardPanel().setHeadItem("ndiscountrate",
						getBillCardTools().getBodyUFDoubleValue(0, "ndiscountrate"));
			}

			String headccurrencytypeid = getBillCardPanel().getHeadItem("ccurrencytypeid")
					.getValue();
			String headccurrencytypename = ccurrencytypeid.getRefName();
			// SCMEnv.out("::表头币种为headccurrencytypeid："+headccurrencytypeid);
			// 表体
			String headBrate = getBillCardPanel().getHeadItem("nexchangeotobrate").getValue();

			// 收货单位等带入表体
			getBillCardPanel().afterCreceiptcorpEdit();

			String[] keys = new String[] { "cconsigncorpid", // 发货公司id
					"cconsigncorp", // 发货公司
					"creccalbody", "creccalbodyid", "crecwarehouse", "crecwareid", "bdericttrans" };

			Object bodydate;
			for (int i = 0; i < getBillCardPanel().getRowCount(); i++) {

				// 控制多公司行的编辑状态
				getBillCardPanel().ctlUIOnCconsignCorpChgForEdit(i);

				// 发到货日期
				String sDateBody = getBillCardPanel().getBodyValueAt(i, "dconsigndate") == null ? null
						: getBillCardPanel().getBodyValueAt(i, "dconsigndate").toString().trim();
				if (sDateBody == null || sDateBody.length() == 0) {
					setBodyDate(i);
				}

				/** 调整以前的日期到登陆日期* */
				// 发货日期
				bodydate = getBillCardPanel().getBodyValueAt(i, "dconsigndate");
				if (bodydate == null
						|| bodydate.toString().compareTo(getClient().getDate().toString()) < 0) {
					getBillCardPanel().setBodyValueAt(getClient().getDate(), i, "dconsigndate");
				}

				// 到货日期
				bodydate = getBillCardPanel().getBodyValueAt(i, "ddeliverdate");
				if (bodydate == null
						|| bodydate.toString().compareTo(getClient().getDate().toString()) < 0) {
					getBillCardPanel().setBodyValueAt(getClient().getDate(), i, "ddeliverdate");
				}
				/** 调整以前的日期到登陆日期* */

				// 单据状态
				getBillCardPanel().setBodyValueAt(new Integer(BillStatus.FREE), i, "frowstatus");

				// 赠品
				if (getBillCardPanel().getBodyValueAt(i, "blargessflag") == null)
					getBillCardPanel().setBodyValueAt(new Boolean(false), i, "blargessflag");
				// 币种
				getBillCardPanel().setBodyValueAt(headccurrencytypeid, i, "ccurrencytypeid");

				getBillCardPanel().setBodyValueAt(headccurrencytypename, i, "ccurrencytypename");

				// 表头汇率带入表体
				getBillCardPanel().setBodyValueAt(headBrate, i, "nexchangeotobrate");

				// 借出转销售
				if (getSouceBillType().equals("4H") || getSouceBillType().equals("42")) {
					getBillCardTools().setBodyCellsEdit(keys, i, false);

					// 借出单的剩余待还辅数量=0，则对应订单上的辅数量应置null
					if (getBillCardPanel().getBodyValueAt(i, "npacknumber") != null) {	
						if (((UFDouble) getBillCardPanel().getBodyValueAt(i, "npacknumber"))
								.doubleValue() == 0)
							getBillCardPanel().setBodyValueAt(null, i, "npacknumber");
					}
				}

			}

			// 价格处理
			if (getBillType().equals(SaleBillType.SaleOrder)) {
				// 合同，借出单，网上订单需寻价处理
				if (getSouceBillType().equals(SaleBillType.SoContract)
						|| getSouceBillType().equals(SaleBillType.SoInitContract)
						|| getSouceBillType().equals("4H") || getSouceBillType().equals("42")
						|| getSouceBillType().equals("3B")) {
					boolean is3B = getSouceBillType().equals("3B");
					boolean isContract = false;
					if (getSouceBillType().equals(SaleBillType.SoContract)
							|| getSouceBillType().equals(SaleBillType.SoInitContract))
						isContract = true;
					int[] rows = new int[getBillCardPanel().getBillModel().getRowCount()];
					for (int i = 0, loop = getBillCardPanel().getBillModel().getRowCount(); i < loop; i++) {
						rows[i] = i;
						// 因为在VO对照时，辅数量一定会对过来
						if (getBillCardPanel().getBodyValueAt(i, "assistunit") == null
								|| getBillCardPanel().getBodyValueAt(i, "cpackunitid") == null) {
							getBillCardPanel().setBodyValueAt(null, i, "npacknumber");
							getBillCardPanel().setBodyValueAt(null, i, "scalefactor");
						}
					}
					//效率优化v5.6
					//getBillCardPanel().afterNumberEdit(rows, "nnumber", null, false, true);
					getBillCardPanel().findPriceForPF(rows, "nnumber", null, false, true);
				}

				// 表头价税合计计算
				getBillCardPanel().getHeadItem("nheadsummny").setValue(
						getBillCardPanel().getTotalValue("noriginalcursummny"));
			}
			// /价格处理

		}

		// 缓存，已备编辑整单折扣时使用
		getBillCardPanel().ndiscountrate = getBillCardTools().getHeadValue("ndiscountrate");
	}

	/**
	 * 由操作人获得业务员
	 * 
	 * @return
	 */
	private String getCemployeeId() {
		try {
			IUserManageQuery query = (IUserManageQuery) NCLocator.getInstance().lookup(
					"nc.itf.uap.rbac.IUserManageQuery");
			PsndocVO psn = query.getPsndocByUserid(getCorpPrimaryKey(), getClientEnvironment()
					.getUser().getPrimaryKey());

			if (psn != null)
				return psn.getPk_psndoc();
			else
				return null;
		} catch (BusinessException e) {
			SCMEnv.out(e);
			return null;
		}
	}

	/**
	 * 设置行操作按钮状态。
	 * 
	 * 创建日期：(2001-3-17 9:00:09)
	 * 
	 */
	private void setLineButtonsState() {
		// 无表体行
		if (getBillCardPanel().getRowCount() == 0) {
			boDelLine.setEnabled(false);
			boCopyLine.setEnabled(false);
			boPasteLine.setEnabled(false);
			boPasteLineToTail.setEnabled(false);
			boFindPrice.setEnabled(false);
			boCardEdit.setEnabled(false);
			boResortRowNo.setEnabled(false);
		} else {
			boDelLine.setEnabled(true);
			boCopyLine.setEnabled(true);
			boPasteLine.setEnabled(true);
			boPasteLineToTail.setEnabled(true);
			boFindPrice.setEnabled(true);
			boCardEdit.setEnabled(true);
			boResortRowNo.setEnabled(true);
		}
	}

	/**
	 * 设置不可修改项。
	 * 
	 * 创建日期：(2001-8-27 10:05:59)
	 * 
	 */
	private void setNoEditItem() {
		try {

			// 先处理表头
			// 收现款金额
			UFDouble dcathpay = getBillCardTools().getHeadUFDoubleValue("nreceiptcathmny");
			if ((dcathpay != null && dcathpay.doubleValue() != 0) || strState.equals("修订")) {/*-=notranslate=-*/
				if (!isHeadCustCanbeModified())
					getBillCardPanel().getHeadItem("ccustomerid").setEnabled(false);
			}

			if (!strState.equals("修订")) {/*-=notranslate=-*/
				String formulas[] = {
				// 客户基本档案
						"ccustbasid->getColValue(bd_cumandoc,pk_cubasdoc,pk_cumandoc,ccustomerid)",
						// 散户标志
						"bfreecustflag->getColValue(bd_cubasdoc,freecustflag,pk_cubasdoc,ccustbasid)" };
				getBillCardPanel().execHeadFormulas(formulas);

				UFBoolean bfreecustflag = getBillCardTools().getHeadUFBooleanValue("bfreecustflag");
				if (bfreecustflag != null && bfreecustflag.booleanValue()) {
					getBillCardPanel().getHeadItem("cfreecustid").setEnabled(true);
				} else {
					getBillCardPanel().getHeadItem("cfreecustid").setEnabled(false);
				}
			}

			// getBillCardPanel().getHeadItem("dbilldate").setEnabled(false);
			if (getBillType().equals(SaleBillType.SaleOrder)) {
				// 币种不可编辑
				// getBillCardPanel().getHeadItem("ccurrencytypeid").setEnabled(false);
				String curID = getBillCardPanel().getHeadItem("ccurrencytypeid").getValue();
				if (curID != null && curID.length() != 0) {
					if (CurrParamQuery.getInstance().isLocalCurrType(getCorpPrimaryKey(), curID)) {
						getBillCardPanel().getHeadItem("nexchangeotobrate").setEnabled(false);
					} else
						getBillCardPanel().getHeadItem("nexchangeotobrate").setEnabled(true);
				}
				if (strState.equals("修订")) {/*-=notranslate=-*/
					getBillCardPanel().getHeadItem("ccalbodyid").setEnabled(false);
					getBillCardPanel().getHeadItem("cwarehouseid").setEnabled(false);
				}
			}
			// 整单折扣
			((UIRefPane) getBillCardPanel().getHeadItem("ndiscountrate").getComponent())
					.setMinValue(0.0);
			// v30dd
			String[] bodyItems = { "nqttaxnetprc", "nqtnetprc", "nqttaxprc", "nqtprc" };
			for (int k = 0; k < bodyItems.length; k++) {
				if (getBillCardPanel().getBodyItem(bodyItems[k]) != null)
					getBillCardPanel().getBodyItem(bodyItems[k]).setEnabled(false);

			}

			// 由补货单生成
			// if (DRP04 != null && DRP04.booleanValue()) {
			// if (getSouceBillType().equals(SaleBillType.SoDRP)) {
			// getBillCardPanel().getBodyItem("nnumber").setEnabled(false);
			// getBillCardPanel().getBodyItem("nquoteunitnum").setEnabled(false);
			// getBillCardPanel().getBodyItem("npacknumber").setEnabled(false);
			// }
			// }

			// 严格执行合同价格且不询价，合同价被带过来后不允许修改
			for (int i = 0; i < getBillCardPanel().getRowCount(); i++) {

				getBillCardPanel().setAssistUnit(i);
				getBillCardPanel().setCtItemEditable(i);
				getBillCardPanel().setScaleEditableByRow(i);
			}

			// 补货单寻价
			if (getSouceBillType().equals(SaleBillType.SoDRP)) {
				for (int row = 0; row < getBillCardPanel().getRowCount(); row++) {
					if (getBillCardPanel().getBodyValueAt(row, "nnumber") != null) {
						getBillCardPanel().afterNumberEdit(new int[] { row }, "nnumber", null,
								false, true);
					}
				}
			}
			// 由订单生成发货单,重新计算辅数量
			if (getSouceBillType().equals(SaleBillType.SaleOrder)) {
				for (int row = 0; row < getBillCardPanel().getRowCount(); row++) {
					if (getBillCardPanel().getBodyValueAt(row, "fixedflag") != null
							&& getBillCardPanel().getBodyValueAt(row, "fixedflag").equals(
									new Boolean(true))) {
						/*
						 * String[] formula = {
						 * "npacknumber->iif(nnumber=NULL,0,iif(nnumber=\"\",0,nnumber))/scalefactor" };
						 * getBillCardPanel().getBillModel().execFormula(row,
						 * formula);
						 */

						BillModel bm = getBillCardPanel().getBillModel();
						getBillCardPanel().setBodyValueAt(
								BillTools.calc(row, BillTools.value(row, "nnumber",
										new UFDouble(0), bm), "scalefactor", BillTools.div, bm),
								row, "npacknumber");
					}
				}
			}
			// 散户
			if (getBillCardPanel().getHeadItem("bfreecustflag").getValue() == null
					|| getBillCardPanel().getHeadItem("bfreecustflag").getValue().equals("false")) {
				getBillCardPanel().getHeadItem("cfreecustid").setEnabled(false);
			} else {
				getBillCardPanel().getHeadItem("cfreecustid").setEnabled(true);
			}
			// 表体
			UFBoolean isDiscount = null;
			UFBoolean isLabor = null;
			for (int i = 0, iLen = getBillCardPanel().getRowCount(); i < iLen; i++) {
				// setAssistUnit(i);
				if (getBillType().equals(SaleBillType.SaleOrder)) {
					// 批次
					Object temp = getBillCardPanel().getBodyValueAt(i, "wholemanaflag");
					boolean wholemanaflag = (temp == null ? false : new UFBoolean(temp.toString())
							.booleanValue());
					getBillCardPanel().setCellEditable(i, "fbatchstatus", wholemanaflag);
					getBillCardPanel().setCellEditable(i, "cbatchid", wholemanaflag);
				}

				isDiscount = getBillCardTools().getBodyUFBooleanValue(i, "discountflag");
				isLabor = getBillCardTools().getBodyUFBooleanValue(i, "laborflag");

				if ((isDiscount != null && isDiscount.booleanValue())
						|| (isLabor != null && isLabor.booleanValue())) {

					getBillCardTools().setBodyCellsEdit(
							new String[] { "cconsigncorp", "creccalbody", "crecwarehouse",
									"bdericttrans", "boosflag", "bsupplyflag" }, i, false);
				}

				/** SA_07已不使用 v5.3 fangchan zhangcheng */
				/*
				 * if (SA_15 != null && SA_15.booleanValue() && SA_07 != null &&
				 * !SA_07.booleanValue()) { getBillCardTools().setBodyCellsEdit(
				 * SOBillCardTools.getSaleItems_Price(), i, false); }
				 */

			}
			// 设置赠品行编辑性
			getBillCardTools().setCardPanelCellEditableByLargess(SO59.booleanValue());

			// v30add如果现收金额大于0，设置币种不可编辑
			UFDouble nreceiptcathmny = getBillCardTools().getHeadUFDoubleValue("nreceiptcathmny");
			if (nreceiptcathmny != null && nreceiptcathmny.doubleValue() > 0) {
				getBillCardPanel().getHeadItem("ccurrencytypeid").setEnabled(false);
				getBillCardPanel().getHeadItem("nexchangeotobrate").setEnabled(false);
				getBillCardPanel().getHeadItem("creceiptcorpid").setEnabled(false);
			}

			// v30add v5.3去掉
			// 如果报价数量显示，则主数量不可编辑
			/*
			 * if (getBillCardPanel().getBodyItem("nquoteunitnum").isShow()) {
			 * getBillCardPanel().getBodyItem("nnumber").setEnabled(false); }
			 */

			if ("退货".equals(strState)) {/*-=notranslate=-*/
				getBillCardPanel().getHeadItem("npreceiverate").setEnabled(false);
				getBillCardPanel().getHeadItem("npreceivemny").setEnabled(false);

				/** v5.3去掉帐期字段 * */
				/*
				 * getBillCardPanel().getHeadItem("naccountperiod").setEnabled(
				 * false);
				 */
			}

		} catch (Throwable ex) {
			handleException(ex);
		}

	}

	/**
	 * 根据币种设置汇率和金额小数位数。
	 * 
	 * 创建日期：(2002-6-24 11:15:06)
	 * 
	 */
	private void setPanelBomByCurrency(String ccurrencytypeid) {
		try {
			if (ccurrencytypeid == null || ccurrencytypeid.length() == 0)
				return;
			nc.vo.bd.b21.CurrinfoVO currVO = currtype.getCurrinfoVO(ccurrencytypeid, null);

			// v30取业务精度
			int digit = currVO.getCurrdigit() == null ? 4 : currVO.getCurrdigit().intValue();

			// BOM金额项精度
			getBillTreeCardPanel().getBillData().getHeadItem("nsaleprice").setDecimalDigits(digit);
			getBillTreeCardPanel().getBillData().getHeadItem("bomorderfee").setDecimalDigits(digit);
			getBillTreeCardPanel().getBillData().getBodyItem("nprice").setDecimalDigits(digit);
			String name = getBillTreeCardPanel().getBodyItem("nprice").getName();
			if (getBillTreeCardPanel().getBodyPanel().hasShowCol(name))
				getBillTreeCardPanel().getBodyPanel().resetTableCellRenderer(name);
		} catch (Exception e) {
			SCMEnv.out("根据币种设置小数位数失败!");
			// e.printStackTrace();
		}
	}

	/**
	 * 设置以选择状态的行为不可再编辑状态 创建日期：(2002-6-13 16:55:52)
	 */
	private void setPzdSelectedEnabled(boolean isEnable) {
		for (int i = 0; i < getBillTreeCardPanel().getBillModel().getRowCount(); i++) {
			getBillTreeCardPanel().setCellEditable(i, "bselect", isEnable);
		}
	}

	/**
	 * 设置单据状态。
	 * 
	 * 创建日期：(2001-6-13 15:17:39)
	 * 
	 * @param iState
	 *            int
	 * 
	 */
	private void setBtnsByBillState(int iState) {

		if (strState.equals("修订") || strState.equals("修改") || strState.equals("新增")
				|| strState.equals("退货")) {
			boCoPushPo.setEnabled(false);
			return;
		}

		// v5.5 处于审批中状态，无审批人的单据可以修改，有审批人就不可以修改
		// v5.5 处于审批中状态，当前审批人可以弃申，其他人不能弃申
		Object auditUser = null;
		Object ordercorp = null;
		Object operatorUser = null;
		if (!strShowState.equals("列表")) {
			auditUser = getBillCardTools().getTailValue("capproveid");
			operatorUser = getBillCardTools().getTailValue("coperatorid");
			ordercorp = getBillCardTools().getHeadValue("pk_corp");
		} else {
			auditUser = getBillListPanel().getHeadBillModel().getValueAt(
					getBillListPanel().getHeadTable().getSelectedRow(), "capproveid");
			operatorUser = getBillListPanel().getHeadBillModel().getValueAt(
					getBillListPanel().getHeadTable().getSelectedRow(), "coperatorid");
			ordercorp = getBillListPanel().getHeadBillModel().getValueAt(
					getBillListPanel().getHeadTable().getSelectedRow(), "pk_corp");
		}

		String curuserid = getClientEnvironment().getUser().getPrimaryKey();
		String curlogcorp = getClientEnvironment().getCorporation().getPk_corp();

		switch (iState) {

		case -1: {
			boBlankOut.setEnabled(false);
			boEdit.setEnabled(false);
			boAudit.setEnabled(false);
			boCancelAudit.setEnabled(false);
			boFreeze.setEnabled(false);
			boCancelFreeze.setEnabled(false);
			boClose.setEnabled(false);
			boOpen.setEnabled(false);
			boFinish.setEnabled(false);
			boModification.setEnabled(false);
			boBatch.setEnabled(false);
			boRefundment.setEnabled(false);
			boAfterAction.setEnabled(false);
			boStockLock.setEnabled(false);

			boSendInv.setEnabled(false);
			boSupplyInv.setEnabled(false);
			boDirectInv.setEnabled(false);

			boSendAudit.setEnabled(false);
			boAuditFlowStatus.setEnabled(false);

			boCachPay.setEnabled(false);
			boOrdBalance.setEnabled(false);
			boCoPushPo.setEnabled(false);

			break;
		}
		case BillStatus.FREE: {
			boBlankOut.setEnabled(true);
			boEdit.setEnabled(true);
			boAudit.setEnabled(true);
			boCancelAudit.setEnabled(false);
			boFreeze.setEnabled(false);
			boCancelFreeze.setEnabled(false);
			boClose.setEnabled(false);
			boOpen.setEnabled(false);
			boFinish.setEnabled(false);
			boBatch.setEnabled(false);
			boModification.setEnabled(false);
			boRefundment.setEnabled(false);
			boAfterAction.setEnabled(false);
			boStockLock.setEnabled(false);

			boSendInv.setEnabled(false);
			boSupplyInv.setEnabled(false);
			boDirectInv.setEnabled(false);

			boSendAudit.setEnabled(true);
			boAuditFlowStatus.setEnabled(true);

			boCachPay.setEnabled(true);
			boOrdBalance.setEnabled(true);
			boCoPushPo.setEnabled(false);

			break;
		}
		case BillStatus.AUDIT: {
			boBlankOut.setEnabled(false);
			boAudit.setEnabled(false);
			boCancelAudit.setEnabled(true);
			boFreeze.setEnabled(true);
			boCancelFreeze.setEnabled(false);
			boClose.setEnabled(true);
			boOpen.setEnabled(false);
			boFinish.setEnabled(true);
			boModification.setEnabled(true);
			boEdit.setEnabled(false);
			boBatch.setEnabled(true);
			boRefundment.setEnabled(true);
			boAfterAction.setEnabled(true);
			boStockLock.setEnabled(true);

			boSendInv.setEnabled(true);
			boSupplyInv.setEnabled(true);
			boDirectInv.setEnabled(true);

			boSendAudit.setEnabled(false);
			boAuditFlowStatus.setEnabled(true);

			boCachPay.setEnabled(true);
			boOrdBalance.setEnabled(true);
			if (getCurrentOrderId() != null
					&& !vocache.getSaleOrderVO(getCurrentOrderId()).isCoopped()) {
				boCoPushPo.setEnabled(true);

			} else {
				boCoPushPo.setEnabled(false);
			}

			if (ifHasData()) {
				setBoSendInvEnabled();
				setBoSupplyInvEnabled();
				setBoDirectInvEnabled();
			}

			setImageType(IMAGE_AUDIT);

			break;
		}
		case BillStatus.AUDITING: {
			boFreeze.setEnabled(false);
			boCancelFreeze.setEnabled(false);
			boClose.setEnabled(false);
			boOpen.setEnabled(false);
			boFinish.setEnabled(false);
			boBatch.setEnabled(false);
			boModification.setEnabled(false);
			boRefundment.setEnabled(false);
			boAfterAction.setEnabled(false);
			boStockLock.setEnabled(false);

			boSendInv.setEnabled(false);
			boSupplyInv.setEnabled(false);
			boDirectInv.setEnabled(false);

			boSendAudit.setEnabled(false);
			boAuditFlowStatus.setEnabled(true);

			boCachPay.setEnabled(true);
			boOrdBalance.setEnabled(true);
			boCoPushPo.setEnabled(false);

			boCancelAudit.setEnabled(auditUser == null ? false
					: ((curuserid.equals(auditUser)) ? true : false));
			boAudit.setEnabled(true);
			if (curlogcorp.equals(ordercorp)) {
				boEdit.setEnabled(auditUser == null ? true : false);
				boBlankOut.setEnabled(auditUser == null ? true : false);
			} else {
				boEdit.setEnabled(false);
				boBlankOut.setEnabled(false);
			}

			break;
		}
		case BillStatus.NOPASS: {
			boBlankOut.setEnabled(false);
			boAudit.setEnabled(false);
			boCancelAudit.setEnabled(false);
			boFreeze.setEnabled(false);
			boCancelFreeze.setEnabled(false);
			boClose.setEnabled(false);
			boOpen.setEnabled(false);
			boFinish.setEnabled(false);
			boModification.setEnabled(false);
			boBatch.setEnabled(false);
			boRefundment.setEnabled(false);
			boAfterAction.setEnabled(false);
			boStockLock.setEnabled(false);

			boSendInv.setEnabled(false);
			boSupplyInv.setEnabled(false);
			boDirectInv.setEnabled(false);

			boSendAudit.setEnabled(false);
			boAuditFlowStatus.setEnabled(true);

			boCachPay.setEnabled(false);
			boOrdBalance.setEnabled(false);
			boCoPushPo.setEnabled(false);

			if (curlogcorp.equals(ordercorp)) {
				boEdit.setEnabled(curuserid.equals(operatorUser) ? true : false);
			} else {
				boEdit.setEnabled(false);
			}

			break;
		}
		case BillStatus.FREEZE: {
			boEdit.setEnabled(false);
			boCancel.setEnabled(false);
			boBlankOut.setEnabled(false);
			boModification.setEnabled(false);
			boAudit.setEnabled(false);
			boCancelAudit.setEnabled(false);
			boFreeze.setEnabled(false);
			boCancelFreeze.setEnabled(true);
			boClose.setEnabled(false);
			boOpen.setEnabled(false);
			boFinish.setEnabled(false);
			boBatch.setEnabled(false);
			boRefundment.setEnabled(false);
			boAfterAction.setEnabled(false);
			boStockLock.setEnabled(false);

			boSendInv.setEnabled(false);
			boSupplyInv.setEnabled(false);
			boDirectInv.setEnabled(false);

			boSendAudit.setEnabled(false);
			boAuditFlowStatus.setEnabled(true);

			boCachPay.setEnabled(false);
			boOrdBalance.setEnabled(false);
			boCoPushPo.setEnabled(false);

			break;
		}
		case BillStatus.CLOSE: {
			boAudit.setEnabled(true);
			boCancelAudit.setEnabled(false);
			boFreeze.setEnabled(true);
			boCancelFreeze.setEnabled(false);
			boClose.setEnabled(true);
			boOpen.setEnabled(false);
			boFinish.setEnabled(false);
			boBatch.setEnabled(false);
			boRefundment.setEnabled(false);
			boAfterAction.setEnabled(false);
			boEdit.setEnabled(false);
			boStockLock.setEnabled(false);

			boSendInv.setEnabled(false);
			boSupplyInv.setEnabled(false);
			boDirectInv.setEnabled(false);

			boSendAudit.setEnabled(false);
			boAuditFlowStatus.setEnabled(true);

			boCachPay.setEnabled(false);
			boOrdBalance.setEnabled(false);
			boCoPushPo.setEnabled(false);

			break;
		}
		case BillStatus.FINISH: {
			boAudit.setEnabled(false);
			boCancelAudit.setEnabled(false);
			boFreeze.setEnabled(false);
			boCancelFreeze.setEnabled(false);
			boClose.setEnabled(false);
			boOpen.setEnabled(false);
			boFinish.setEnabled(false);
			boModification.setEnabled(false);
			boBatch.setEnabled(true);
			boRefundment.setEnabled(true);
			boAfterAction.setEnabled(false);
			boBlankOut.setEnabled(false);
			boEdit.setEnabled(false);
			boStockLock.setEnabled(false);

			boSendInv.setEnabled(false);
			boSupplyInv.setEnabled(false);
			boDirectInv.setEnabled(false);

			boSendAudit.setEnabled(false);
			boAuditFlowStatus.setEnabled(true);

			boCachPay.setEnabled(false);
			boOrdBalance.setEnabled(false);
			boCoPushPo.setEnabled(false);

			setBoSendInvEnabled();
			setBoSupplyInvEnabled();
			setBoDirectInvEnabled();

			break;
		}
		case BillStatus.BLANKOUT: {
			boBlankOut.setEnabled(false);
			boEdit.setEnabled(false);
			boAudit.setEnabled(false);
			boCancelAudit.setEnabled(false);
			boFreeze.setEnabled(false);
			boCancelFreeze.setEnabled(false);
			boClose.setEnabled(false);
			boOpen.setEnabled(false);
			boFinish.setEnabled(false);
			boModification.setEnabled(false);
			boBatch.setEnabled(false);
			boRefundment.setEnabled(false);
			boAfterAction.setEnabled(false);
			boAssistant.setEnabled(false);
			boStockLock.setEnabled(false);

			boSendInv.setEnabled(false);
			boSupplyInv.setEnabled(false);
			boDirectInv.setEnabled(false);

			boSendAudit.setEnabled(false);
			boAuditFlowStatus.setEnabled(true);

			boCachPay.setEnabled(false);
			boOrdBalance.setEnabled(false);
			boCoPushPo.setEnabled(false);

			break;
		}
		}
		
		if (getBillListPanel().getHeadTable().getRowCount() <= 0)
			setExtendBtnsStat(iState);

		updateUI();
	}

	/**
	 * 发货安排按钮可用性
	 */
	private void setBoSendInvEnabled() {
		boolean isUse = true;

		String cbiztype = getHeadItemValue("cbiztype").toString();
		String breceiptendflag = SmartVODataUtils.getUFBoolean(getHeadItemValue("breceiptendflag"))
				.toString();
		SaleOrderVO svo = (SaleOrderVO) getVo();

		if (breceiptendflag.equals("Y"))
			isUse = false;
		else if (!getBillCardTools().isSendInvEnable(svo))
			isUse = false;
		else if (!UFBoolean.valueOf(
				getBillCardTools().getBusiInfo(SoVoConst.BIZTYPESENDINV,
						cbiztype).toString()).booleanValue())
			isUse = false;

		boSendInv.setEnabled(isUse);
	}

	/**
	 * 补货安排按钮可用性
	 */
	private void setBoSupplyInvEnabled() {
		boolean isUse = true;

		String cbiztype = getHeadItemValue("cbiztype").toString();

		if (!getBillCardTools().isArrangend((SaleOrderVO) getVo()))
			isUse = false;
		else if (!getBillCardTools().isBodyAllDirect((SaleOrderVO) getVo()))
			isUse = false;
		else if (UFBoolean.valueOf(
				getBillCardTools().getBusiInfo(SoVoConst.BIZTYPEZ,
						cbiztype).toString()).booleanValue())
			isUse = false;

		boSupplyInv.setEnabled(isUse);
	}

	/**
	 * 直运安排按钮可用性
	 */
	private void setBoDirectInvEnabled() {
		boolean isUse = true;

		String cbiztype = getHeadItemValue("cbiztype").toString();
		if (!getBillCardTools().isArrangend((SaleOrderVO) getVo()))
			isUse = false;
		else if (!getBillCardTools().isBodyDirect((SaleOrderVO) getVo())
				&& !getBillCardTools().isZVerifyRule(cbiztype))
			isUse = false;

		boDirectInv.setEnabled(isUse);
	}

	public AggregatedValueObject getVo() {
		if (!strShowState.equals("列表")) {
			return getVO(false);
		} else {
			int selrow = getBillListPanel().getHeadTable().getSelectedRow();
			if (selrow < 0)
				selrow = selectRow;
			return getBillListPanel().getBillValueVO(selrow, SaleOrderVO.class.getName(),
					SaleorderHVO.class.getName(), SaleorderBVO.class.getName());
		}
	}

	protected ATPUIQryDelegate atpQry = null;

	// nc240 new add
	protected SOBillCardTools soBillCardTools = null;

	/**
	 * 保存部件抛出异常时被调用
	 * 
	 * @param ex
	 *            java.lang.Throwable
	 */
	protected void handleSaveException(String billType, String businessType, String actionName,
			String corpId, String operator, SaleOrderVO vo, Exception ex) {
		String err = ex.getMessage();
		if (ex.getClass() != AtpCheckException.class || (ex.getClass() == AtpCheckException.class)) {
			// 处理流程配置中的异常
			if (ex instanceof PFBusinessException) {
				String errMsg = vo.getHeadVO().getErrMsg();
				if (errMsg != null && errMsg.startsWith("FUNC:lessSaleMinPrice")) {
					err += "/n" + errMsg.substring(errMsg.indexOf("$") + 1);
				}
			}
			showWarningMessage(err);
		}

		showHintMessage(NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000185")/*
																							 * @res
																							 * "保存失败！"
																							 */);
	}

	/**
	 * 获取合同信息。
	 * 
	 * 创建日期：(2001-10-9 13:05:04)
	 * 
	 */
	private void initCTTypeVO() {

		HashMap ht = new HashMap();

		if (getBillCardPanel().getBillModel().getRowCount() > 0) {
			if (getSouceBillType().equals(SaleBillType.SoContract)
					|| getSouceBillType().equals(SaleBillType.SoInitContract)) {
				for (int i = 0; i < getBillCardPanel().getBillModel().getRowCount(); i++) {
					String ct_manageid = (String) getBillCardPanel().getBodyValueAt(i,
							"ct_manageid");
					if (ct_manageid != null && ct_manageid.length() != 0)
						ht.put(ct_manageid, ct_manageid);
				}

			}
		}
		if (ht.size() <= 0)
			return;
		String[] ids = (String[]) ht.keySet().toArray(new String[0]);

		try {
			getBillCardPanel().hCTTypeVO = SaleOrderBO_Client.getAllContractType(ids);

		} catch (Exception ex) {
			SCMEnv.out("获取合同信息失败!");
		}
	}

	/**
	 * 币种编辑后事件处理。 创建日期：(2001-6-23 13:42:53)
	 * 
	 * @return nc.vo.pub.lang.UFDouble
	 */
	private void ctlCurrencyEdit() {
		UIRefPane ccurrencytypeid = (UIRefPane) getBillCardPanel().getHeadItem("ccurrencytypeid")
				.getComponent();

		try {
			if (BD302 == null || !BD302.booleanValue()) {
				// 如果币种等于本位币种则折本汇率不能修改，而且应该等于1
				if (CurrParamQuery.getInstance().isLocalCurrType(getCorpPrimaryKey(),
						ccurrencytypeid.getRefPK())) {
					getBillCardPanel().getHeadItem("nexchangeotobrate").setEnabled(false);
				} else
					getBillCardPanel().getHeadItem("nexchangeotobrate").setEnabled(true);

			} else {
				// 如果币种等于辅助币种则折辅助汇率不能修改,对本币汇率可以修改
				if (CurrParamQuery.getInstance().isFracCurrType(getCorpPrimaryKey(),
						ccurrencytypeid.getRefPK())) {

					getBillCardPanel().getHeadItem("nexchangeotobrate").setEnabled(true);
				} else {
					if (CurrParamQuery.getInstance().isLocalCurrType(getCorpPrimaryKey(),
							ccurrencytypeid.getRefPK())) {
						// 如果币种等于本币种则折辅助汇率，本币汇率不可以修改
						getBillCardPanel().getHeadItem("nexchangeotobrate").setEnabled(false);

					} else {
						getBillCardPanel().getHeadItem("nexchangeotobrate").setEnabled(true);

					}
				}
			}
		} catch (java.lang.Exception e1) {
			SCMEnv.out("获得汇率失败！");
			// e1.printStackTrace();
		}
	}

	public OrderBalanceCardUI getOrderBalanceUI() {
		if (orderBalanceUI == null)
			orderBalanceUI = new OrderBalanceCardUI(this);
		return orderBalanceUI;
	}

	/**
	 * 检测审批单据合法性。
	 * 
	 * 创建日期：(2001-8-8 10:09:01)
	 * 
	 * @return boolean
	 * 
	 */
	private void onApproveCheck(SaleOrderVO saleorder) throws nc.vo.pub.ValidationException {

		String salecorp = null;

		SaleorderBVO[] bodyVOs = saleorder.getBodyVOs();

		salecorp = saleorder.getHeadVO().getPk_corp();

		if (salecorp == null || salecorp.trim().length() <= 0)
			salecorp = getCorpPrimaryKey();

		boolean isBomOrder = false;

		// 审批日期不能小于制单日期
		if (saleorder.getHeadVO().getDmakedate().compareTo(getClientEnvironment().getDate()) > 0) {
			throw new ValidationException(NCLangRes.getInstance().getStrByID("40060301",
					"UPP40060301-000541")/*
											 * @res "审批日期不能小于制单日期"
											 */);
		}

		for (int i = 0, loop = bodyVOs.length; i < loop; i++) {
			SaleorderBVO oldbodyVO = bodyVOs[i];

			if (!SoVoTools.isEmptyString(oldbodyVO.getCbomorderid()) && !isBomOrder)
				isBomOrder = true;

			// 收货库存组织非空检验
			if (oldbodyVO.getCconsigncorpid() != null
					&& !oldbodyVO.getCconsigncorpid().equals(salecorp)) {

				if (oldbodyVO.getCreccalbodyid() == null
						|| oldbodyVO.getCreccalbodyid().trim().length() <= 0) {
					throw new ValidationException(NCLangRes.getInstance().getStrByID("40060301",
							"UPP40060301-000202")/*
													 * @res
													 * "下列字段不能为空：发货公司非本销售公司行的收货库存组织"
													 */);
				}
				if (oldbodyVO.getBdericttrans() == null
						|| !oldbodyVO.getBdericttrans().booleanValue()) {
					if (oldbodyVO.getCrecwareid() == null
							|| oldbodyVO.getCrecwareid().trim().length() <= 0)
						throw new ValidationException(NCLangRes.getInstance().getStrByID(
								"40060301", "UPP40060301-000203")/*
																	 * @res
																	 * "下列字段不能为空：非直运订单行的收货仓库"
																	 */);
				}

				if (oldbodyVO.getLaborflag() != null && oldbodyVO.getLaborflag().booleanValue())
					throw new ValidationException(NCLangRes.getInstance().getStrByID("40060301",
							"UPP40060301-000204")/*
													 * @res "劳务类存货不能从其它公司发货"
													 */);

				if (oldbodyVO.getDiscountflag() != null
						&& oldbodyVO.getDiscountflag().booleanValue())
					throw new ValidationException(NCLangRes.getInstance().getStrByID("40060301",
							"UPP40060301-000205")/*
													 * @res "折扣类存货不能从其它公司发货"
													 */);
			}
		}

		SCMEnv.out("审核：" + getSouceBillType());
		SCMEnv.out("审核：" + SaleBillType.SoDRP);

		if (!getSouceBillType().equals(SaleBillType.SoDRP))
			return;

		try {
			if (getBillType().equals(SaleBillType.SaleOrder)) {
				// 产品配置单审批检测
				if (isBomOrder && !SaleOrderBO_Client.isBomApproved(saleorder.getPrimaryKey())) {
					throw new nc.vo.pub.ValidationException(NCLangRes.getInstance().getStrByID(
							"40060301", "UPP40060301-000240")/*
																 * @res
																 * "产品配置单没有完全审批!"
																 */);
				}
				// 折本汇率
				if (getBillCardPanel().getHeadItem("nexchangeotobrate").getValue() == null
						|| getBillCardPanel().getHeadItem("nexchangeotobrate").getValue()
								.equals("")) {
					throw new nc.vo.pub.ValidationException(NCLangRes.getInstance().getStrByID(
							"SCMCOMMON", "UPPSCMCommon-000310")/*
																 * @res
																 * "折本汇率不能为空!"
																 */);
				}

			}

			// VO检测
			saleorder.validate();

			// 参数检测
			for (int i = 0; i < saleorder.getChildrenVO().length; i++) {
				SaleorderBVO oldbodyVO = (SaleorderBVO) saleorder.getChildrenVO()[i];

				// 收货库存组织非空检验
				if (oldbodyVO.getCconsigncorpid() != null
						&& !oldbodyVO.getCconsigncorpid().equals(salecorp)) {
					if (oldbodyVO.getCreccalbodyid() == null
							|| oldbodyVO.getCreccalbodyid().trim().length() <= 0) {
						throw new ValidationException(NCLangRes.getInstance().getStrByID(
								"40060301", "UPP40060301-000202")/*
																	 * @res
																	 * "下列字段不能为空：发货公司非本销售公司行的收货库存组织"
																	 */
								+ "\n行号：" + oldbodyVO.getCrowno());
					}
					if (oldbodyVO.getBdericttrans() == null
							|| !oldbodyVO.getBdericttrans().booleanValue()) {
						if (oldbodyVO.getCrecwareid() == null
								|| oldbodyVO.getCrecwareid().trim().length() <= 0)
							throw new ValidationException(NCLangRes.getInstance().getStrByID(
									"40060301", "UPP40060301-000203")/*
																		 * @res
																		 * "下列字段不能为空：非直运订单行的收货仓库"
																		 */);
					}

					if (oldbodyVO.getDiscountflag() != null
							&& oldbodyVO.getDiscountflag().booleanValue())
						throw new ValidationException(NCLangRes.getInstance().getStrByID(
								"40060301", "UPP40060301-000205")/*
																	 * @res
																	 * "折扣类存货不能从其它公司发货"
																	 */);
				}

				// 非折扣类存货
				if (oldbodyVO.getDiscountflag() != null
						&& !oldbodyVO.getDiscountflag().booleanValue()) {
					// 数量
					if (oldbodyVO.getNoriginalcurmny().doubleValue() < 0
							|| oldbodyVO.getNoriginalcurtaxmny().doubleValue() < 0
							|| oldbodyVO.getNoriginalcursummny().doubleValue() < 0)
						if (oldbodyVO.getNnumber().doubleValue() > 0)
							throw new ValidationException(NCLangRes.getInstance().getStrByID(
									"40060301", "UPP40060301-000209")/*
																		 * @res
																		 * "当前数量不允许大于零!"
																		 */);
					// 单价
					if (oldbodyVO.getNoriginalcurprice().doubleValue() < 0)
						throw new ValidationException(NCLangRes.getInstance().getStrByID(
								"SCMCOMMON", "UPPSCMCommon-000063")/*
																	 * @res
																	 * "单价必须大于等于零!"
																	 */);
				} else {
					// 数量、金额、价税合计
					if (oldbodyVO.getNnumber() == null && oldbodyVO.getNoriginalcurmny() == null
							&& oldbodyVO.getNoriginalcursummny() == null)
						throw new ValidationException(NCLangRes.getInstance().getStrByID(
								"SCMCOMMON", "UPPSCMCommon-000261")/*
																	 * @res
																	 * "数量、金额、价税合计不能同时为空!"
																	 */);
				}
				// 包装单位(是否采用辅计量)
				if (oldbodyVO.getAssistunit() != null && oldbodyVO.getAssistunit().booleanValue()) {
					if (oldbodyVO.getDiscountflag() != null
							&& !oldbodyVO.getDiscountflag().booleanValue()) {
						if (oldbodyVO.getCpackunitid() == null) {
							throw new ValidationException(NCLangRes.getInstance().getStrByID(
									"40060301", "UPP40060301-000241")/*
																		 * @res
																		 * "辅单位不能为空!"
																		 */);
						}
						if (oldbodyVO.getNpacknumber() == null) {
							throw new ValidationException(NCLangRes.getInstance().getStrByID(
									"40060301", "UPP40060301-000242")/*
																		 * @res
																		 * "辅数量不能为空!"
																		 */);
						} else {
							if (oldbodyVO.getNpacknumber().doubleValue()
									* oldbodyVO.getNnumber().doubleValue() < 0) {
								throw new ValidationException(NCLangRes.getInstance().getStrByID(
										"40060301", "UPP40060301-000243")/*
																			 * @res
																			 * "数量和辅数量符号必须相同!"
																			 */);
							}
						}
					}
				}

				// 订单的发货日期小于订单日期
				UFDate dbilldate = new UFDate(getBillCardPanel().getHeadItem("dbilldate")
						.getValue());
				UFDate dconsigndate = oldbodyVO.getDconsigndate();
				if (dconsigndate != null && dbilldate != null) {
					if (dbilldate.after(dconsigndate) && dbilldate != dconsigndate)
						throw new ValidationException(NCLangRes.getInstance().getStrByID(
								"40060301", "UPP40060301-000244")/*
																	 * @res
																	 * "订单的发货日期应大于等于订单日期!"
																	 */);
				}

				// 根据参数检测
				String oldinventoryid = oldbodyVO.getCinventoryid(); // 存货

				if (oldbodyVO.getBlargessflag() != null
						&& !oldbodyVO.getBlargessflag().booleanValue()) {
					for (int j = i + 1; j < saleorder.getChildrenVO().length; j++) {
						SaleorderBVO newbodyVO = (SaleorderBVO) saleorder.getChildrenVO()[j];
						// SO_03 相同存货可否共存一单（赠品除外）
						if (!SO_03.booleanValue()) {
							String newinventoryid = newbodyVO.getCinventoryid(); // 存货
							if (newbodyVO.getBlargessflag() != null
									&& !newbodyVO.getBlargessflag().booleanValue()) {
								if (oldinventoryid.equals(newinventoryid)) {
									throw new ValidationException(NCLangRes.getInstance()
											.getStrByID("40060301", "UPP40060301-000216", null,
													new String[] { (i + 1) + "", (j + 1) + "" }));
								}
							}
						}
					}
				}
			}

			// 订单收款>=订单价税合计订单收款>=订单预收款 by zxj
			SaleorderHVO header = (SaleorderHVO) saleorder.getParentVO();
			for (int i = 0; i < saleorder.getChildrenVO().length; i++) {
				SaleorderBVO saleorderitem = (SaleorderBVO) saleorder.getChildrenVO()[i];
				if (saleorderitem != null && saleorderitem.getNtotalpaymny() != null) {
					if (saleorderitem.getNsummny() != null
							&& saleorderitem.getNtotalpaymny().doubleValue() < saleorderitem
									.getNsummny().doubleValue()) {
						throw new ValidationException(NCLangRes.getInstance().getStrByID(
								"40060301", "UPP40060301-000245")/*
																	 * @res
																	 * "订单收款应>=订单价税合计!"
																	 */);
					}
					if (header.getNsubscription() != null
							&& saleorderitem.getNtotalpaymny().doubleValue() < header
									.getNsubscription().doubleValue()) {
						throw new ValidationException(NCLangRes.getInstance().getStrByID(
								"40060301", "UPP40060301-000246")/*
																	 * @res
																	 * "订单收款应>=订单预收款!"
																	 */);
					}
				}
			}
			//
			SCMEnv.out("审核检测通过1");
		} catch (Throwable e) {
			throw new nc.vo.pub.ValidationException(NCLangRes.getInstance().getStrByID("40060301",
					"UPP40060301-000247")/*
											 * @res "此单据由补货单生成数据不完整，请修改补齐。"
											 */);
		}
	}

	private Object getHeadItemValue(String key) {
		Object obj = null;
		if (strShowState.equals("列表")) {
			int selrow = getBillListPanel().getHeadTable().getSelectedRow();
			if (selrow >= 0 && selrow < getBillListPanel().getHeadBillModel().getRowCount()) {
				obj = getBillListPanel().getHeadBillModel().getValueAt(selrow, key);
			} else
				obj = getBillListPanel().getHeadBillModel().getValueAt(selectRow, key);
		} else
			obj = getBillCardTools().getHeadValue(key);
		return obj;
	}

	/**
	 * 订单收款
	 */
	protected void onCachPay() {
		
	    //创建时间工具类实例
		nc.vo.scm.pu.Timer timer = new nc.vo.scm.pu.Timer();
		timer.start();
		
		try {
			SaleOrderVO ordvo = (SaleOrderVO) getVO(false);
			if (ordvo == null)
				return;

			SaleorderBVO[] bodyVOs = ordvo.getBodysNoInludeOOSLine();

			if (bodyVOs.length <= 0) {
				return;
			}

			// SO72-红字订单是否可以订单收款、收款核销
			UFDouble noriginalcursummny = SOBillCardTools.getCurSumMny(ordvo);
			if (noriginalcursummny == null || noriginalcursummny.doubleValue() == 0
					|| (!SO72.booleanValue() && noriginalcursummny.doubleValue() < 0)) {
				showErrorMessage(NCLangRes.getInstance().getStrByID("40060301",
						"UPP40060301-000250")/*
												 * @res "价税合计小于等于0"
												 */);
				return;
			}

			/** v5.5 采用直接打开收款单节点******************************************* */
			SaleReceiveVO srvo = get3FVoFrom30Vo(ordvo);
			SOToolVO stvo = new SOToolVO();
			stvo.setAttributeValue("pk_corp", ordvo.getHeadVO().getPk_corp());
			stvo.setAttributeValue("csaleid", ordvo.getPrimaryKey());
			stvo.setAttributeValue("npreceivemny", ordvo.getHeadVO().getNpreceivemny());
			stvo.setAttributeValue("noriginalcursummny", noriginalcursummny);
			stvo.setAttributeValue("cbiztypeid", srvo.getHead().getCbiztypeid());
			// stvo.setAttributeValue("SaleReceiveVO",srvo);

			String[] dest = SaleOrderBO_Client.prepareCachPay(stvo);
			srvo.getHead().setCbiztypeid(dest[0]);
			timer.addExecutePhase(">>>>>>>>>销售订单[订单收款]销售订单数据准备用时：>>>>>>>>>>>");
			
			// vo交换：3FtoD2
			DJZBVO djzbvo = (DJZBVO) PfChangeBO_Client.pfChangeBillToBill(srvo, "3F", dest[1]);
			timer.addExecutePhase(">>>>>>>>>销售订单[订单收款]vo交换用时：>>>>>>>>>>>");
			
			PfLinkData linkData = new PfLinkData();
			linkData.setUserObject(new Object[] { new DJZBVO[] { djzbvo } });
			// 收款单节点号
			String nodecode = PfUIDataCache.getBillType(dest[1]).getNodecode();
			// 打开收款单节点
			SFClientUtil.openLinkedADDDialog(nodecode, this, linkData);
			timer.addExecutePhase(">>>>>>>>>销售订单[订单收款]打开收款单节点用时：>>>>>>>>>>>");
			
			timer.addExecutePhase(">>>>>>>>>销售订单[订单收款]远程调用用时：>>>>>>>>>>>");
			//输出各个业务操作占用时间分布
			timer.showAllExecutePhase(">>>>>>>>>销售订单[订单收款]远程调用结束：>>>>>>>>>>>");

		} catch (PFBusinessException pfe) {
			showErrorMessage(pfe.getMessage());
			return;
		} catch (Exception e) {
			SCMEnv.out(e);
		}
	}

	/**
	 * 由30得到3FVO
	 */
	public SaleReceiveVO get3FVoFrom30Vo(nc.vo.so.so001.SaleOrderVO ordVO) {

		SaleReceiveVO vo = getSaleReceiveVO(ordVO);
		SaleReceiveHVO hvo = (SaleReceiveHVO) vo.getParentVO();

		String csaleid = (String) ordVO.getParentVO().getAttributeValue("csaleid");
		// set ts
		vo.getParentVO().setAttributeValue("ts", ordVO.getHeadVO().getTs());
		// set csaleid
		vo.getParentVO().setAttributeValue("csaleid", csaleid);
		SoVoTools.setVOsValue(vo.getChildrenVO(), "csaleid", csaleid);

		// 客户(对应订单的客户)
		hvo.setCcustomerid(ordVO.getHeadVO().getCcustomerid());
		// 开票单位
		hvo.setCreceiptcorpid(ordVO.getHeadVO().getCreceiptcorpid());
		SoVoTools
				.execFormulas(
						new String[] {
								"ccustomerbasid->getColValue(bd_cumandoc,pk_cubasdoc,pk_cumandoc,ccustomerid)",
								"creceiptcorpbasid->getColValue(bd_cumandoc,pk_cubasdoc,pk_cumandoc,creceiptcorpid)" },
						new SaleReceiveHVO[] { hvo });

		// 设置结算方式
		hvo.setCbaltypeid(ordVO.getHeadVO().getCbaltypeid());

		return vo;
	}

	public SaleReceiveVO getSaleReceiveVO(SaleOrderVO ordVO) {

		if (ordVO == null)
			return null;
		SaleorderHVO ordhvo = (SaleorderHVO) ordVO.getParentVO();
		SaleorderBVO[] ordbvos = (SaleorderBVO[]) ordVO.getChildrenVO();

		// // 过滤赠品行/////////////////////////////////////////////////
		java.util.ArrayList ordbvolist = new java.util.ArrayList();
		for (int i = 0, loop = ordbvos.length; i < loop; i++) {
			if (ordbvos[i].getBlargessflag() != null && ordbvos[i].getBlargessflag().booleanValue())
				continue;
			ordbvolist.add(ordbvos[i]);
		}
		ordbvos = (SaleorderBVO[]) ordbvolist.toArray(new SaleorderBVO[ordbvolist.size()]);
		if (ordbvos == null || ordbvos.length <= 0)
			return null;

		// // vo值互换/////////////////////////////////////////////////
		SaleReceiveHVO hvo = (SaleReceiveHVO) SoVoTools.getVOByVO("nc.vo.so.so016.SaleReceiveHVO",
				ordhvo,

				/* src */new String[] { "csaleid", "cbiztype", "ccustomerid", "npreceivemny",
						"coperatorid", "cemployeeid", "cdeptid", "cfreecustid", "cfreecustname" },

				/* dest */new String[] { "csaleid", "cbiztypeid", "ccustomerid", "npreceivemny",
						"coperatorid", "cemployeeid", "cdeptid", "cfreecustid", "cfreecustname" });

		hvo.setCemployeeid(ordhvo.getCemployeeid());
		if (ordhvo.getCreceiptcorpid() != null && ordhvo.getCreceiptcorpid().trim().length() > 0)
			hvo.setCcustomerid(ordhvo.getCreceiptcorpid());

		// 订单客户
		hvo.setCcustomerid(ordhvo.getCcustomerid());
		// 设置结算方式
		hvo.setCbaltypeid(ordhvo.getCbaltypeid());

		SoVoTools.copyVOByVO(hvo, new String[] { "ccurrencytypeid", "nexchangeotobrate", },
				ordbvos[0], new String[] { "ccurrencytypeid", "nexchangeotobrate", });

		hvo.setCPkCorp(ordVO.getPk_corp());
		hvo.setDbilldate(getClientEnvironment().getDate());
		hvo.setNpreceivemny(ordhvo.getNpreceivemny());

		// 处理预收标志
		if (hvo.getNpreceivemny() != null && hvo.getNpreceivemny().doubleValue() > 0)
			hvo.setBpriorrecptflag(new nc.vo.pub.lang.UFBoolean(true));
		else
			hvo.setBpriorrecptflag(new nc.vo.pub.lang.UFBoolean(false));

		boolean bbyprodline = (SO27 == null ? false : SO27.booleanValue());
		SaleReceiveBVO bvo = new SaleReceiveBVO();
		bvo.setCsaleid(ordhvo.getCsaleid());
		bvo.setCbillordercode(ordhvo.getVreceiptcode());
		bvo.setCtermprotocolid(ordhvo.getCtermprotocolid());

		bvo.setNexchangeotobrate(ordbvos[0].getNexchangeotobrate());
		bvo.setCdeptid(ordhvo.getCdeptid());
		bvo.setCemployeeid(ordhvo.getCemployeeid());

		if (bbyprodline) {
			bvo.setCproductlineid(ordbvos[0].getCprolineid());
		} else {
			String[] prodlineids = SoVoTools.getVOsOnlyValues(ordbvos, "cprolineid");
			if (prodlineids != null && prodlineids.length == 1)
				bvo.setCproductlineid(prodlineids[0]);
			else
				bvo.setCproductlineid(null);
		}

		// 预收款金额!=null && >0
		if (hvo.getNpreceivemny() != null && hvo.getNpreceivemny().doubleValue() > 0) {
			// 原币价税合计、无税金额
			bvo.setAttributeValue("noriginalcursummny", SoVoTools.getMnySub(hvo.getNpreceivemny(),
					ordhvo.getNreceiptcathmny()));
		} else {
			/** ====原币价税合计 ==== */
			bvo.setAttributeValue("noriginalcursummny", SoVoTools.getMnySub(SoVoTools.getTotalMny(
					ordbvos, "noriginalcursummny"), ordhvo.getNreceiptcathmny()));
		}

		try {
			// 计算本币价税合计
			UFDouble nsummny = null;
			nsummny = currtype.getAmountByOpp(hvo.getCcurrencytypeid(),
					CurrParamQuery.getInstance().getLocalCurrPK(
							hvo.getcPkCorp()), SmartVODataUtils.getUFDouble(bvo
							.getAttributeValue("noriginalcursummny")), hvo
							.getNexchangeotobrate(), getClientEnvironment()
							.getDate().toString());
			bvo.setAttributeValue("nsummny", nsummny);

		} catch (Exception e) {
			nc.vo.scm.pub.SCMEnv.out("calcMny:计算金额失败");
		}

		// 订单收款不计税
		bvo.setAttributeValue("nmny", bvo.getAttributeValue("nsummny"));
		bvo.setAttributeValue("noriginalcurmny", bvo.getAttributeValue("noriginalcursummny"));

		SaleReceiveVO oldSaleReceiveVO = new SaleReceiveVO();
		oldSaleReceiveVO.setParentVO(hvo);
		oldSaleReceiveVO.setChildrenVO(new SaleReceiveBVO[] { bvo });

		hvo.setAttributeValue("nmny", bvo.getAttributeValue("nsummny"));
		hvo.setAttributeValue("noriginalcurmny", bvo.getAttributeValue("noriginalcursummny"));

		return oldSaleReceiveVO;
	}

	protected void onOrderBalance() {
		try {
			// 订单核销节点是否启用
			if (!nc.ui.so.pub.BillTools.ifNodeCodeUse("40060308")) {
				showWarningMessage("订单核销节点未启用，该按钮不可用！");
				return;
			}

			SaleOrderVO ordvo = (SaleOrderVO) getVO(false);
			if (ordvo == null)
				return;

			// 检查订单行中是否存在正负反号的行
			int iallMinusMnyrow = 0;
			int iallMinusNumrow = 0;
			SaleorderBVO[] bodyVOs = ordvo.getBodysNoInludeOOSLine();

			if (bodyVOs.length <= 0) {
				return;
			}

			for (int i = 0, loop = bodyVOs.length; i < loop; i++) {

				if (bodyVOs[i].getNoriginalcursummny() != null
						&& bodyVOs[i].getNoriginalcursummny().doubleValue() < 0) {
					iallMinusMnyrow++;
				}
				if (bodyVOs[i].getNnumber() != null && bodyVOs[i].getNnumber().doubleValue() < 0) {
					iallMinusNumrow++;
				}
			}

			ordvo.setChildrenVO(bodyVOs);

			// SO72-红字订单是否可以订单收款、收款核销
			UFDouble noriginalcursummny = SOBillCardTools.getCurSumMny(ordvo);
			if (noriginalcursummny == null || noriginalcursummny.doubleValue() == 0
					|| (!SO72.booleanValue() && noriginalcursummny.doubleValue() < 0)) {
				showErrorMessage(NCLangRes.getInstance().getStrByID("40060301",
						"UPP40060301-000250")/*
												 * @res "价税合计小于等于0"
												 */);
				return;
			}

			// 设置客户、开票单位的基本档案id
			SoVoTools
					.execFormulas(
							new String[] {
									"ccustbasid->getColValue(bd_cumandoc,pk_cubasdoc,pk_cumandoc,ccustomerid)",
									"creceiptcorpbasid->getColValue(bd_cumandoc,pk_cubasdoc,pk_cumandoc,creceiptcorpid)" },
							new SaleorderHVO[] { ordvo.getHeadVO() });

			getOrderBalanceUI().setOrdVO(ordvo);
			if (getCurShowPanel() == null)
				remove(getBillListPanel());
			else
				remove(getCurShowPanel());
			add(getOrderBalanceUI(), "Center");
			getOrderBalanceUI().initUI();
			setTitleText(getOrderBalanceUI().getTitle());

			showHintMessage("");

			ButtonObject[] btns = getOrderBalanceUI().getButtons();

			// 处理不可用按钮
			for (int i = 0, len = btns.length; i < len; i++) {
				if (btns[i].getCode().equals("列表显示") || btns[i].getCode().equals("浏览")
						|| btns[i].getCode().equals("打印管理")) {
					btns[i].setVisible(false);
				}
			}// end for

			setButtons(btns);

			updateUI();

		} catch (Exception e) {
			returnFormOrderBalanceUI();
			orderBalanceUI = null;
			nc.vo.scm.pub.SCMEnv.out(e.getMessage());
		}
	}

	public void returnFormOrderBalanceUI() {
		remove(getOrderBalanceUI());
		if (getCurShowPanel() == null)
			add(getBillListPanel(), "Center");
		else
			add(getCurShowPanel(), "Center");
		setTitleText(getTitle());
		setButtons();
		setButtonsState();
		getBillCardPanel().setHeadItem("nreceiptcathmny", getOrderBalanceUI().getNbalmny());

		int selrow = getBillListPanel().getHeadTable().getSelectedRow();
		if (selrow >= 0) {
			getBillListPanel().getHeadBillModel().setValueAt(getOrderBalanceUI().getNbalmny(),
					selrow, "nreceiptcathmny");
			// 同步更新缓存数据
			SaleOrderVO ordvo = vocache.getSaleOrderVO((String) getBillListPanel()
					.getHeadBillModel().getValueAt(selrow, "csaleid"));
			if (ordvo != null) {
				ordvo.getHeadVO().setNreceiptcathmny(getOrderBalanceUI().getNbalmny());
			}
		}
		updateUI();
	}

	public JComponent getCurShowPanel() {
		return curShowPanel;
	}

	/**
	 * 单据打印后刷新ts(2004-12-01 23:25:18)
	 * 
	 * @param
	 */
	public void freshTs(String sBillID, String sTS, Integer iPrintCount) {
		if (sTS == null || sTS.trim().length() <= 0)
			return;
		if (strShowState.equals("列表")) { /*-=notranslate=-*/
			if (sBillID == null || sBillID.trim().length() <= 0)
				return;
			String csaleid = null;
			for (int i = 0, loop = getBillListPanel().getHeadTable().getRowCount(); i < loop; i++) {
				csaleid = (String) getBillListPanel().getHeadBillModel().getValueAt(i, "csaleid");
				if (sBillID.equals(csaleid)) {
					getBillListPanel().getHeadBillModel().setValueAt(sTS, i, "ts");
					getBillListPanel().getHeadBillModel().setValueAt(iPrintCount, i, "iprintcount");
					break;
				}
			}
		} else {
			getBillCardPanel().setHeadItem("ts", sTS);
			getBillCardPanel().setTailItem("iprintcount", iPrintCount);
			String csaleid = null;
			for (int i = 0, loop = getBillListPanel().getHeadTable().getRowCount(); i < loop; i++) {
				csaleid = (String) getBillListPanel().getHeadBillModel().getValueAt(i, "csaleid");
				if (sBillID.equals(csaleid)) {
					getBillListPanel().getHeadBillModel().setValueAt(sTS, i, "ts");
					getBillListPanel().getHeadBillModel().setValueAt(iPrintCount, i, "iprintcount");
					break;
				}
			}
		}
		SaleOrderVO ordvo = vocache.getSaleOrderVO(sBillID);
		if (ordvo != null) {
			ordvo.getHeadVO().setIprintcount(iPrintCount);
			ordvo.getHeadVO().setTs(new UFDateTime(sTS.trim()));
		}
	}

	/**
	 * 初始化打印日志对象(2004-12-01 23:25:18)
	 * 
	 * @param
	 */
	protected PrintLogClient getPrintLogClient() {
		// if (printLogClient == null) {
		printLogClient = new PrintLogClient();
		printLogClient.addFreshTsListener(this);
		// }
		return printLogClient;
	}

	/**
	 * 1. 在销售订单修订时提供客户修改的功能，只能在销售订单没有发货/出库/开票/结算的情况下修订销售订单的客户； 2.
	 * 在已进行了货源安排的情况下可以进行客户修订，修订后不影响后续单据的客户信息； 3 客户修订后应重新询价，同时修改三个应收数据。
	 * 
	 * @param i
	 * @return
	 */
	private boolean isHeadCustCanbeModified() {
		int irowcount = getBillCardPanel().getRowCount();
		if (irowcount <= 0)
			return true;
		for (int irow = 0; irow < irowcount; irow++) {
			String sPk = (String) getBillCardPanel().getBodyValueAt(irow, "csaleid");
			String sBodyPk = (String) getBillCardPanel().getBodyValueAt(irow, "corder_bid");
			if (sPk == null || sPk.trim().length() == 0 || sBodyPk == null
					|| sBodyPk.trim().length() == 0)
				// 新行
				continue;

			SaleOrderVO svo = vocache.getSaleOrderVO(sPk);

			if (svo == null)
				continue;

			SaleorderBVO[] bvos = svo.getBodyVOs();
			if (bvos == null || bvos.length == 0)
				continue;
			for (int i = 0; i < bvos.length; i++) {
				if (bvos[i].getCorder_bid().equals(sBodyPk)) {
					String[] sNames = { "ntotalreceivenumber",// 累计发货数量
							"ntotalinvoicenumber",// 累计开票数量
							"ntotalinventorynumber",// 累计出库数量
							"ntotalshouldoutnum",// 出库应发
							"ntotalbalancenumber",// 累计结算数量
							"ntotalreturnnumber",// 累计退货数量
					// "narrangescornum",//累计安排委外订单数量
					// "narrangepoapplynum",//累计安排请购单数量
					// "narrangetoornum",//累计安排调拨订单数量
					// "norrangetoapplynum",//累计安排调拨申请数量
					// "narrangemonum"//累计安排生产订单数量
					};
					for (int j = 0; j < sNames.length; j++) {
						if (bvos[i].getAttributeValue(sNames[j]) != null
								&& ((UFDouble) bvos[i].getAttributeValue(sNames[j])).doubleValue() != 0)
							return false;
					}
					break;
				}
			}

		}
		return true;

	}

	/**
	 * 函数功能:存量查询
	 * 
	 * 参数:
	 * 
	 * 返回值:
	 * 
	 * 异常:
	 * 
	 */
	protected void onOnHandShowHidden() {
		// 存量查询界面只在卡片界面显示
		if (strShowState.equals("列表")) {
			onCard();

			// 选中的表体
			int selectedrow = getBillListPanel().getBodyTable().getSelectedRow();
			if (selectedrow > -1) {
				getBillCardPanel().getBillTable().getSelectionModel().setSelectionInterval(
						selectedrow, selectedrow + 1);
			}
		}

		// 存量查询界面只在卡片界面显示

		m_bOnhandShowHidden = !m_bOnhandShowHidden;

		show(true, m_bOnhandShowHidden);
		if (m_bOnhandShowHidden) {
			freshOnhandnum(getBillCardPanel().getBillTable().getSelectedRow());
		}
		updateUI();
	}

	/**
	 * 创建日期：(2004-2-10 18:30:34)
	 * 
	 * 作者：王乃军 参数： 返回： 说明：显示/隐藏panel
	 * 
	 */
	private void show(boolean bCardShow, boolean bSouthPanelShow) {
		getBillCardPanel().setVisible(bCardShow);
		getSplitPanelBc().setVisible(bCardShow);
		if (bCardShow) {
			if (bSouthPanelShow) {
				if (getSplitPanelBc().getBottomComponent() == null) {
					getSplitPanelBc().add(getPnlSouth(this), nc.ui.pub.beans.UISplitPane.BOTTOM);
				}
				if (getSplitPanelBc().getTopComponent() == null) {
					getSplitPanelBc().add(getBillCardPanel(), nc.ui.pub.beans.UISplitPane.TOP);
				}
				getSplitPanelBc().setDividerLocation((int) (getSplitPanelBc().getHeight() * 0.68));

			} else {
				if (getSplitPanelBc().getTopComponent() == null)
					getSplitPanelBc().add(getBillCardPanel(), nc.ui.pub.beans.UISplitPane.TOP);
				if (getSplitPanelBc().getBottomComponent() != null)
					getSplitPanelBc().remove(getPnlSouth(this));
				getSplitPanelBc().setDividerLocation((int) (getSplitPanelBc().getHeight() * 0.95));

			}
		}

	}

	/**
	 * 关闭窗口的客户端接口。可在本方法内完成窗口关闭前的工作。
	 * 
	 * @return boolean 返回值为true表示允许窗口关闭，返回值为false表示不允许窗口关闭。
	 * 
	 * 创建日期：(2001-8-8 13:52:37)
	 */
	public boolean onClosing() {

		boolean closeFlag = true;

		if (strState.equals("修改") || strState.equals("修订") || strState.equals("新增")) {
			int ireturn = MessageDialog.showYesNoCancelDlg(this, NCLangRes.getInstance()
					.getStrByID("SCMCOMMON", "UPPSCMCommon-000270")/*
																	 * @res "提示"
																	 */, NCLangRes.getInstance().getStrByID("common", "UCH001")/*
																		 * @res
																		 * "是否保存已修改的数据？（"
																		 */);
			if (ireturn == MessageDialog.ID_YES) {
				if (onSave())
					closeFlag = true;
				else
					closeFlag = false;
			} else if (ireturn == MessageDialog.ID_CANCEL)
				closeFlag = false;
		}

		if (closeFlag)
			processMemoryAfterClose();

		return closeFlag;
	}

	/**
	 * 关闭节点后，清空不必要的对象引用，已释放内存
	 */
	private void processMemoryAfterClose() {
		// 右键菜单
		/*if (m_bodyMenuItems != null) {
			m_bodyMenuItems.clear();
			m_bodyMenuItems = null;
		}*/
	}

	private void setLineButtonStatus(boolean bstatus) {
		boLine.setEnabled(bstatus);
		boAddLine.setEnabled(bstatus);
		boDelLine.setEnabled(bstatus);
		boInsertLine.setEnabled(bstatus);
		boCopyLine.setEnabled(bstatus);
		boPasteLine.setEnabled(bstatus);
		boPasteLineToTail.setEnabled(bstatus);
		boFindPrice.setEnabled(bstatus);
		boCardEdit.setEnabled(bstatus);
		boResortRowNo.setEnabled(bstatus);
		setBodyMenuStatus();
	}

	/**
	 * UI关联操作-新增
	 * 
	 * @author leijun 2006-5-24
	 * 
	 * @see 拉式下游消息相关参看发票--SaleInvoiceUI.java
	 * 
	 */
	public void doAddAction(ILinkAddData adddata) {
		try {
			if (PfUtilClient.isCloseOK()) {
				binitOnNewByOther = true;

				onNewByOther(PfUtilClient.getRetVos());
				// 可用量状态
				for (int i = 0; i < getBillCardPanel().getRowCount(); i++)
					vRowATPStatus.add(new UFBoolean(false));
			}

			// 加自由项变色龙。
			try {
				InvAttrCellRenderer ficr = new InvAttrCellRenderer();
				ficr.setFreeItemRenderer(getBillCardPanel(), this.getBillCardPanel().alInvs);
			} catch (Exception e) {

			}
			setBusitype();
		} catch (Exception e) {
			handleException(e);
		} finally {
			binitOnNewByOther = false;
		}

	}

	/**
	 * UI关联操作-审批
	 * 
	 * @author cch 2006-5-9-11:04:16
	 * 
	 */
	public void doApproveAction(ILinkApproveData approvedata) {

		if (strShowState == "列表") {
			onCard();
		}

		id = approvedata.getBillID();
		loadCardDataByID(id);
		setCardButtonsState();

		setButtons(getBillButtons());

		// 加载数据到列表界面上
		SaleOrderVO vo = vocache.getSaleOrderVO(id);
		if (vo != null) {
			getBillListPanel().fillHeadData(new SaleorderHVO[] { vo.getHeadVO() });
			fillCacheByListPanel();
			selectRow = -1;
			initIDs();
		}
	}

	/**
	 * UI关联操作-维护
	 * 
	 * @author leijun 2006-5-24
	 */
	public void doMaintainAction(ILinkMaintainData maintaindata) {

		if (strShowState == "列表") {
			onCard();
		}

		id = maintaindata.getBillID();
		loadCardDataByID(id);
		setCardButtonsState();

		// 获取当前单据的公司ID
		Object pk_corp = getBillCardPanel().getHeadItem("pk_corp").getValueObject();
		if (getCorpPrimaryKey().equals(pk_corp))
			setButtons(getBillButtons());
		else
			setButtons(new ButtonObject[0]);

		// 加载数据到列表界面上
		SaleOrderVO vo = vocache.getSaleOrderVO(id);
		if (vo != null) {
			getBillListPanel().fillHeadData(new SaleorderHVO[] { vo.getHeadVO() });
			fillCacheByListPanel();
			selectRow = -1;
			initIDs();
		}

	}

	/**
	 * UI关联操作-查询
	 * 
	 * @author leijun 2006-5-24
	 * 
	 * @modified v51 联查界面行为增强
	 * 
	 */
	public void doQueryAction(ILinkQueryData querydata) {
        //获得订单ID
		String billid = querydata.getBillID();;
		openSaleOrderByID(billid);
	}

	private void setBusitype() {

		String sBusitype = getBillCardPanel().getHeadItem("cbiztype").getValue();

		// 设置业务类型
		boBusiType.setTag(sBusitype);
		getBillCardPanel().setBusiType(sBusitype);

	}

	ClientEnvironment getClient() {
		return getClientEnvironment();
	}

	/**
	 * 得到单据VO。
	 * 
	 * 创建日期：(2001-6-23 9:47:36)
	 * 
	 * @return nc.vo.so.so001.SaleOrderVO
	 * 
	 */
	public AggregatedValueObject getVO(boolean needRemove) {
		SaleOrderVO saleorder = null;
		if (strShowState.equals("列表")) { /*-=notranslate=-*/
			int row = getBillListPanel().getHeadTable().getSelectedRow();

			if (row < 0)
				return null;

			saleorder = (SaleOrderVO) getBillListPanel().getBillValueVO(row,
					SaleOrderVO.class.getName(), SaleorderHVO.class.getName(),
					SaleorderBVO.class.getName());

		} else {

			saleorder = (SaleOrderVO) getBillCardPanel().getBillValueVO(
					SaleOrderVO.class.getName(), SaleorderHVO.class.getName(),
					SaleorderBVO.class.getName());
			// 单据类型
			((SaleorderHVO) saleorder.getParentVO()).setCreceipttype(SaleBillType.SaleOrder);
			// 主键
			((SaleorderHVO) saleorder.getParentVO()).setPrimaryKey(getBillID());
			// 收货地址
			UIRefPane vreceiveaddress = (UIRefPane) getBillCardPanel().getHeadItem(
					"vreceiveaddress").getComponent();
			((SaleorderHVO) saleorder.getParentVO()).setVreceiveaddress(vreceiveaddress
					.getUITextField().getText());
		}
		if (needRemove) {
			Vector vTemp = new Vector();
			SaleorderBVO[] itemVOs = (SaleorderBVO[]) saleorder.getChildrenVO();
			int indexSelected = -1;
			if (strShowState.equals("列表")) { /*-=notranslate=-*/
				indexSelected = getBillListPanel().getBodyTable().getSelectedRow();
			} else {
				indexSelected = getBillCardPanel().getBillTable().getSelectedRow();
			}
			for (int i = 0; i < itemVOs.length; i++) {
				// 若不是劳务类存货
				boolean notLabor = itemVOs[i].getLaborflag() == null
						|| !itemVOs[i].getLaborflag().booleanValue();
				// 若不是折扣类存货
				boolean notDiscount = itemVOs[i].getDiscountflag() == null
						|| !itemVOs[i].getDiscountflag().booleanValue();

				if (notLabor && notDiscount) {
					if (indexSelected > -1 && i == indexSelected) {
						vTemp.addElement(itemVOs[i]);
					}
				}
			}
			SaleorderBVO[] itemsNew = new SaleorderBVO[vTemp.size()];
			vTemp.copyInto(itemsNew);
			saleorder.setChildrenVO(itemsNew);
		}

		// 某些情况，表体无公司
		SaleorderBVO[] itemVOs = (SaleorderBVO[]) saleorder.getChildrenVO();
		for (int i = 0, len = itemVOs.length; i < len; i++) {
			if (itemVOs[i].getPkcorp() == null) {
				itemVOs[i].setPkcorp(getCorpPrimaryKey());
			}
		}

		// 审批人
		((SaleorderHVO) saleorder.getParentVO()).setCapproveid(getClientEnvironment().getUser()
				.getPrimaryKey());

		return saleorder;
	}

	/**
	 * 行列变换 创建日期：(01-2-26 13:29:17)
	 */
	public void bodyRowChange(BillEditEvent e) {

		if (e.getRow() == -1)
			return;
		super.bodyRowChange(e);
		if (getFuncExtend() != null) {
			// 支持功能扩展
			try {
				getFuncExtend().rowchange(this, getBillCardPanel(), getBillListPanel(),
						nc.ui.scm.extend.IFuncExtend.LIST, nc.ui.pub.bill.BillItem.HEAD);
			} catch (Throwable ee) {
				nc.vo.scm.pub.SCMEnv.out(ee.getMessage());
			}
		}

		if (strState.equals("修订")) { /*-=notranslate=-*/

			if (getBillCardPanel().getBillModel().getRowState(e.getRow()) == BillModel.ADD
					|| !isHasBackwardDoc(e.getRow())) {
				boDelLine.setEnabled(true);
				getBillCardPanel().getDelLineMenuItem().setEnabled(true);

			} else {
				boDelLine.setEnabled(false);
				getBillCardPanel().getDelLineMenuItem().setEnabled(false);

			}
			updateButton(boDelLine);
		}
		if (strShowState.equals("列表")) { /*-=notranslate=-*/
			selectRow = e.getRow();
			String csaleid = (String) getBillListPanel().getHeadBillModel().getValueAt(selectRow,
					"csaleid");

			SaleOrderVO vo = vocache.getSaleOrderVO(csaleid);

			if (vo == null || vo.getParentVO() == null)
				return;

			if (vo.getChildrenVO() == null || vo.getChildrenVO().length <= 0) {
				getBillListPanel().loadBodyData(selectRow);
				getBillListPanel().getBodyBillModel().updateValue();
			} else {
				getBillListPanel().setListBodyByCurrency(vo.getBodyVOs()[0].getCcurrencytypeid());
				getBillListPanel().setBodyValueVO(vo.getChildrenVO());
				getBillListPanel().getBodyBillModel().updateValue();
			}

			setButtonsState();

		} else if (strShowState.equals("卡片")) { /*-=notranslate=-*/
			// wsy 存量显示
			freshOnhandnum(e.getRow());

		}

	}

	public void reLoadListData() {
		if (strShowState.equals("列表")) { /*-=notranslate=-*/
			setBtnsByBillState(-1);
			getBillListPanel().reLoadData();
			fillCacheByListPanel();
			selectRow = -1;
			initIDs();
		}
	}

	/**
	 * 
	 * 方法功能描述：处理修订版本号，修订人，修订日期
	 * 
	 * @author 周长胜
	 * @time 2008-8-14 下午04:38:06
	 */
	public void doEditBeforeModify(SaleOrderVO saleorder) {
		// v5.5修订版本号：销售订单新增版本号1.0，之后每次修订加1
		String editionNum = saleorder.getHeadVO().getEditionNum();
		int editionNumNew = new UFDouble(editionNum == null ? "1.0" : editionNum.trim()).intValue() + 1;
		editionNum = String.valueOf(editionNumNew) + ".0";
		saleorder.getHeadVO().setEditionNum(editionNum);
		// 修订日期取当前修订得系统日期
		saleorder.getHeadVO().setEditDate(getClientEnvironment().getDate().toString());
		// 修订人取但前登陆人
		saleorder.getHeadVO().setEditAuthor(
				getClientEnvironment().getUser().getUserName().toString());
	}

	/**
	 * 获得来源单据类型。
	 * 
	 * 创建日期：(2001-11-16 13:24:23)
	 * 
	 * @return java.lang.String
	 */
	protected String getSouceBillType() {
		String creceipttype = null;
		if (strShowState.equals("卡片")) { /*-=notranslate=-*/
			if (getBillCardPanel().getRowCount() > 0) {
				creceipttype = (String) getBillCardPanel().getBodyValueAt(0, "creceipttype");
			}
		} else {
			if (getBillListPanel().getBodyBillModel().getRowCount() > 0) {
				creceipttype = (String) getBillListPanel().getBodyBillModel().getValueAt(0,
						"creceipttype");
			}
		}
		if (creceipttype == null || creceipttype.trim().equals(""))
			creceipttype = "NO";
		return creceipttype;
	}

	public SaleOrderSplitDLG getSpDLG() {
		if (spDLG == null) {
			spDLG = new SaleOrderSplitDLG(this);
		}
		return spDLG;
	}

	/**
	 * @param productCode
	 * @return 某产品是否启用
	 */
	public boolean ifProductUse(String productCode) throws BusinessException {
		nc.itf.uap.sf.ICreateCorpQueryService icorp = (nc.itf.uap.sf.ICreateCorpQueryService) nc.bs.framework.common.NCLocator
				.getInstance().lookup(nc.itf.uap.sf.ICreateCorpQueryService.class.getName());
		java.util.Hashtable pEnabled = icorp.queryProductEnabled(getClient().getCorporation()
				.getPrimaryKey(), new String[] { productCode });

		return ((nc.vo.pub.lang.UFBoolean) pEnabled.get(productCode)).booleanValue();
	}

	/**
	 * 设置换算率
	 * 
	 * @author liuzwc
	 * @time 2009-05-26
	 */
	private void setScalefactor() {

		// 处理可变换算率
		Object cunitid, cpackunitid, fixedflag, cinvbasdocid, mainmeasrate;
		UFDouble nnumber, npacknumber;
		for (int i = 0, len = getBillCardPanel().getRowCount(); i < len; i++) {
			cpackunitid = getBillCardPanel().getBodyValueAt(i, "cpackunitid");
			cunitid = getBillCardPanel().getBodyValueAt(i, "cunitid");
			cinvbasdocid = getBillCardPanel().getBodyValueAt(i, "cinvbasdocid");
			// 辅计量存货
			if (cpackunitid != null && cunitid != null) {

				if (cpackunitid.equals(cunitid)) {
					getBillCardPanel().setBodyValueAt(UFDouble.ONE_DBL, i, "scalefactor");
					getBillCardPanel().setBodyValueAt(UFBoolean.TRUE, i, "fixedflag");
				} else {
					fixedflag = getBillCardPanel().getBodyValueAt(i, "fixedflag");
					// 获得换算率
					mainmeasrate = BillTools.getColValue2("bd_convert", "mainmeasrate",
							"pk_invbasdoc", (String) cinvbasdocid, "pk_measdoc",
							(String) cpackunitid);
					getBillCardPanel().setBodyValueAt(mainmeasrate, i, "scalefactor");

					// 非固定换算率
					if (fixedflag != null
							&& ("false".equals(fixedflag.toString()) || "N".equals(fixedflag
									.toString()))) {
						nnumber = (UFDouble) getBillCardPanel().getBodyValueAt(i, "nnumber");
						npacknumber = (UFDouble) getBillCardPanel()
								.getBodyValueAt(i, "npacknumber");
						// 重新计算
						getBillCardPanel().setBodyValueAt(nnumber.div(npacknumber), i,
								"scalefactor");
					}
				}// end else

			}// end if

		}// end for

	}
	/**
	 * 方法功能描述：打开CRM中的销售订单。
	 * <b>参数说明</b>
	 * @param billid
	 * @author fengjb
	 * @time 2009-7-15 下午03:06:20
	 */
	public void openSaleOrderByID(String billid){

	    //创建时间工具类实例
		nc.vo.scm.pu.Timer timer = new nc.vo.scm.pu.Timer();
		timer.start();
		
		// 直接显示卡片界面
		if (strShowState == "列表") {
			onCard();
		}
		timer.addExecutePhase(">>>>>>>>>销售订单[联查进入销售订单--列表转卡片]用时：>>>>>>>>>>>");
		
        id = billid;
		loadCardDataByID(billid, false);
		timer.addExecutePhase(">>>>>>>>>销售订单[联查进入销售订单--卡片置数]用时：>>>>>>>>>>>");
		
		setCardButtonsState();
		timer.addExecutePhase(">>>>>>>>>销售订单[联查进入销售订单--设置卡片按钮状态]用时：>>>>>>>>>>>");

		// 获取当前单据的公司ID
		Object pk_corp = getBillCardPanel().getHeadItem("pk_corp").getValueObject();
		if (getCorpPrimaryKey().equals(pk_corp))
			setButtons(getBillButtons());
		else
			setButtons(new ButtonObject[0]);
		
        //加载数据到列表界面上
		SaleOrderVO vo = vocache.getSaleOrderVO(id);
		if (vo != null) {
			getBillListPanel().fillHeadData(new SaleorderHVO[] { vo.getHeadVO() });
			fillCacheByListPanel();
			vocache.getSaleOrderVO(id).setChildrenVO(vo.getChildrenVO());
			selectRow = -1;
			initIDs();
		}
		timer.addExecutePhase(">>>>>>>>>销售订单[联查进入销售订单--设置列表数据]用时：>>>>>>>>>>>");
		
		//输出各个业务操作占用时间分布
		timer.showAllExecutePhase(">>>>>>>>>销售订单[联查进入销售订单]结束：>>>>>>>>>>>");
	}

	public String getSelectedBusyType() {
		if (selectedBusyType==null){
			selectedBusyType = boBusiType.getChildButtonGroup()[0].getTag();
			boBusiType.setTag(selectedBusyType);
			getBillCardPanel().setBusiType(selectedBusyType);
			getBillListPanel().setBusiType(selectedBusyType);
		}
		return selectedBusyType;
	}

	public void setSelectedBusyType(String selectedBusyType) {
		this.selectedBusyType = selectedBusyType;
	}
}
