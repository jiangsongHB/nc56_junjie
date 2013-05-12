package nc.ui.so.so001.panel.card;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import nc.bs.bd.b21.BusinessCurrencyRateUtil;
import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.ui.ic.pub.QueryOnHandInfoPanel;
import nc.ui.ic.service.IQueryOnHandInfoPanel;
import nc.ui.pf.query.ICheckRetVO;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.UIComboBox;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.beans.UISplitPane;
import nc.ui.pub.bill.BillActionListener;
import nc.ui.pub.bill.BillCellEditor;
import nc.ui.pub.bill.BillData;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillModel;
import nc.ui.pub.bill.BillUIUtil;
import nc.ui.pub.bill.action.BillTableLineAction;
import nc.ui.pub.para.SysInitBO_Client;
import nc.ui.scm.extend.IFuncExtend;
import nc.ui.scm.freecust.UFRefGridUI;
import nc.ui.scm.plugin.InvokeEventProxy;
import nc.ui.scm.pub.InvoInfoBYFormula;
import nc.ui.scm.pub.ctrl.BillLineInfoListener;
import nc.ui.scm.pub.def.DefSetTool;
import nc.ui.scm.pub.report.BillRowNo;
import nc.ui.scm.ref.prm.CustAddrRefModel;
import nc.ui.scm.so.SaleBillType;
import nc.ui.so.pub.ColoredTableCellRenderer;
import nc.ui.so.pub.InvAttrCellRenderer;
import nc.ui.so.pub.UITimeTextField;
import nc.ui.so.so001.SaleOrderBO_Client;
import nc.ui.so.so001.panel.bom.BillTools;
import nc.ui.so.so001.panel.bom.SaleBillBomUI;
import nc.ui.so.so001.panel.list.SaleOrderVOCache;
import nc.vo.bd.def.DefVO;
import nc.vo.bd.ref.IFilterStrategy;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.BusinessRuntimeException;
import nc.vo.pub.ValidationException;
import nc.vo.pub.bill.BillTempletVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.query.ConditionVO;
import nc.vo.scm.ic.bill.InvVO;
import nc.vo.scm.pub.OnHandRefreshVO;
import nc.vo.scm.pub.SCMEnv;
import nc.vo.so.pub.SOCurrencyRateUtil;
import nc.vo.so.so001.SORowData;
import nc.vo.so.so001.SaleOrderVO;
import nc.vo.so.so001.SaleorderBVO;
import nc.vo.so.so001.SaleorderHVO;
import nc.vo.so.so001.SoVoConst;

/**
 * 销售订单UI端抽象基类(主要处理卡片模板操作)
 * 
 * @author zhongwei
 * 
 */
public abstract class SaleBillCardUI extends SaleBillBomUI implements
		BillLineInfoListener, ICheckRetVO ,BillActionListener{

	protected SOBillCardPanel cardpanel = null;

	public SaleOrderVOCache vocache = new SaleOrderVOCache();

	public String id = ""; // 当前ID
	
	// 单据显示状态
	public String strShowState = "列表";

	// 价格参数
	public UFBoolean SA_02 = null; // 基价含税

	public UFBoolean SA_50 = null; // 负数是否询价
	
	public UFBoolean SO78 = null; // 销售订单修订是否询价

	protected UFBoolean SO_17 = null; // 是否执行合同价格

	protected UFBoolean SA_41 = null; // 是否允许修改折扣

	// 单据参数
	protected Integer SO_01 = null; // 单据限行

	protected UFBoolean SO_03 = null; // 同一货物是否可列多行

	protected UFBoolean SO_14 = null;

	public UFBoolean SO_20 = new UFBoolean("Y"); // 销售管理是否使用通用模版

	//public UFBoolean SA_16 = null; // 建议发货

	protected UFBoolean IC003 = null; // 是否超订单出库

	protected UFDouble IC004 = null; // 超订单出库容差

	protected UFBoolean SO25 = null; // 是否超订单(出库单)开票

	protected UFDouble SO26 = null; // 超订单开票容差

	protected UFBoolean SO23 = null; // 是否超订单出库

	protected UFDouble SO24 = null; // 允许超销售订单出库%

	protected UFDouble SO29 = null; // 发货关闭下限%

	protected UFDouble SO43 = null; // 貨源安排容差比例
	
	public String SO40 = null; // 调单价还是调单品折扣（默认调折扣）
	
	protected UFBoolean SO72 = null;//红字订单是否可以订单收款、收款核销

	// yt add 2003-10-16
	// yt update 2003-10-27
	protected String SO_21 = null; // 预收优先
	

	// 公共参数
	protected UFBoolean BD302 = null; // 是否主辅币核算

	public UFBoolean SO59 = null; // 销售订单/销售发票上的赠品行是否保留单价/金额”,默认为“是”20070104

	public Integer BD502 = null; // 辅计量数量小数位数

	public Integer BD503 = null; // 换算率

	public Integer BD505 = null; // 单价小数位数

	// DRP参数
	//protected UFBoolean DRP04 = null; // 补货单是否严格执行

	public BusinessCurrencyRateUtil currtype = null; // 币种

	// 修改前单据号
	protected String m_oldreceipt = null;

	protected BillTempletVO billtempletVO = null;

	// 设置自定义项表头
	protected DefVO[] head_defs = null;

	// 设置自定义项表体
	protected DefVO[] body_defs = null;

	protected Hashtable hsparas = null;

	// 是否严格按照产品线进行应收的冲减
	protected UFBoolean SO27 = null;

	// 控制ATP存量不足的处理方式(v5.3去掉)
	//protected String SO28 = null;

	protected UFDouble SO34 = null;

	// 销售订单数量是否允许负数
	protected UFBoolean SO45 = null;

	// 修改订单是否修改制单人
	protected UFBoolean SO41 = null;

	protected boolean binitFuncExtend = false;

	protected UISplitPane pnlCardAndBc = null;

	private SOBillCardTools cardtools;
	
	private SOCurrencyRateUtil socrutil;

	// 复制行数
	private int iCopyRowCount = 0;

	// 复制中间量数组
	private Object[] oCopy = null;

	// 显示或者隐藏现存量面板
	protected boolean m_bOnhandShowHidden = false;

	// 可用量状态
	public Vector vRowATPStatus = new Vector();

	protected UIPanel m_pnlOnHand = null;

	protected IFuncExtend m_funcExtend = null;

	// 行切换时现存量显示入口参数
	protected OnHandRefreshVO m_voLineOnHand = new OnHandRefreshVO();

	// 配置销售按钮
	public ButtonObject boBom = new ButtonObject(
			nc.ui.ml.NCLangRes.getInstance().getStrByID("40060301",
					"UPT40060301-000031")/* @res "配置销售" */,
			nc.ui.ml.NCLangRes.getInstance().getStrByID("40060301",
					"UPT40060301-000031")/* @res "配置销售" */, 0, "配置销售"); /*-=notranslate=-*/

	public ButtonObject boStockLock;
	
	//发货安排
	public ButtonObject boSendInv;
	public static String SENDINV = "SendInv";
	
	//补货安排
	public ButtonObject boSupplyInv;
	public static String SUPPLYINV = "SupplyInv";
	
	//直运安排
	public ButtonObject boDirectInv;
	public static String DIRECTINV = "DirectInv";

	protected ButtonObject boAddLine;

	protected ButtonObject boDelLine;

	protected ButtonObject boInsertLine;

	protected ButtonObject boCopyLine;

	protected ButtonObject boPasteLine;
	
	protected ButtonObject boPasteLineToTail;
	
	protected ButtonObject boFindPrice;
	
	protected ButtonObject boResortRowNo;
	
	protected ButtonObject boCardEdit;

	public ButtonObject boBusiType = new ButtonObject(
			nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
					"UC001-0000003")/* @res "业务类型" */, nc.ui.ml.NCLangRes
					.getInstance().getStrByID("SCMCOMMON",
							"UPPSCMCommon-000293")/* @res "选择业务类型" */, 1,
			"业务类型"); /*-=notranslate=-*/

	// 原来颜色
	private Color initColor = null;

	class myCellRenderer extends ColoredTableCellRenderer {
		public Component getTableCellRendererComponent(
				javax.swing.JTable table, Object value, boolean isSelected,
				boolean hasFocus, int row, int column) {
			Component comp = super.getTableCellRendererComponent(table, value,
					isSelected, hasFocus, row, column);
			if (initColor == null) {
				initColor = getForeground();
			}
			if (vRowATPStatus == null || vRowATPStatus.size() == 0
					|| row >= vRowATPStatus.size()) {
				setForeground(initColor);
			} else if (vRowATPStatus.elementAt(row) == null ? false
					: ((UFBoolean) vRowATPStatus.elementAt(row)).booleanValue()) {
				setForeground(java.awt.Color.red);
			} else {
				setForeground(initColor);
			}
			Color background = getBackGround(row, column);
			if (background != null && table.getModel() instanceof BillModel) {
				setBackground(background);
			}
			else {
				setBackground(null);
			}
			return comp;
		}
	}

	public SaleBillCardUI() {
		// initialize();
	}

	public SaleBillCardUI(String pk_corp, String billtype, String busitype,
			String operator, String id) {
		initialize(pk_corp, billtype, busitype, operator, id);
	}

	@Override
	protected void postInit() {
		initialize();
		initCustomerUI();
	}

	/**
	 * 初始化客户自己的界面
	 * 
	 * 此方法应该在所有初始化的最后
	 */
	protected void initCustomerUI() {

	}

	/**
	 * 执行单据加载公式
	 */
	public void execBodyLoadFormula(String showState){

		List<String> list = new ArrayList<String>(); 
		// 表体显示公式
		BillItem[] bms = getBillCardPanel().getBillModel().getBodyItems();
		for (int i = 0; i < bms.length; i++) {
			if (bms[i].getLoadFormula() != null && bms[i].getLoadFormula().length > 0) {
				if (!("csourcebillid".equals(bms[i].getKey())
						||"ct_manageid".equals(bms[i].getKey())
						||"cpricepolicyid".equals(bms[i].getKey())
						||"cpriceitemid".equals(bms[i].getKey())
						||"cpriceitemtable".equals(bms[i].getKey()))
				){
						//||"cquoteunitid".equals(bms[i].getKey())
					    //||"cpackunitid".equals(bms[i].getKey()))){
					for (int j=0,len=bms[i].getLoadFormula().length;j<len;j++)
						list.add(bms[i].getLoadFormula()[j]);
				}
			}
		}
	    // 执行表体公式
		String[] bodyformulas = list.toArray(new String[list.size()]);
		if (showState.equals("卡片")){
			getBillCardPanel().getBillModel().execFormulas(bodyformulas);
		}
		else{
			getBillListPanel().getBodyBillModel().execFormulas(bodyformulas);
		}

	}
	
	/**
	 * 初始化类。
	 */
	protected void initialize() {
		long st = System.currentTimeMillis();

		// 设置界面
		try {
			setName("SaleOrder");
			setSize(774, 419);
			add(getSplitPanelBc(), "Center");
		} catch (java.lang.Throwable ivjExc) {
			handleException(ivjExc);
		}

		initVars(null);
		
		initCurrency();

		// 初试化按钮
		initButtons();

		loadCardTemplet();

		strState = "自由";

		// 设置默认业务类型
		getBillCardPanel().setBusiType(boBusiType.getTag());

		//初始化业务处理配置
		getBillCardPanel().initbusiProcessFlag();
		
		SCMEnv.out(" initialize cost time "	+ (System.currentTimeMillis() - st));

	}

	protected UISplitPane getSplitPanelBc() {
		if (pnlCardAndBc == null) {

			pnlCardAndBc = new nc.ui.pub.beans.UISplitPane(
					nc.ui.pub.beans.UISplitPane.VERTICAL_SPLIT);
			pnlCardAndBc.setName("card2");
			pnlCardAndBc.add(getBillCardPanel(),
					nc.ui.pub.beans.UISplitPane.TOP);
		}

		return pnlCardAndBc;

	}
	
//	private Hashtable<String,Object> getPara(String pkcorp,String[] codes) throws BusinessException{
//		Hashtable<String,Object> hpara = new Hashtable<String,Object>(); 
//		Object paraValue = null;
//		for (int i = 0; i<codes.length; i++){
//			paraValue = SysinitAccessor.getInstance().getParaByPk_org
//	         (OrgnizeTypeVO.COMPANY_TYPE, pkcorp, codes[i], UFBoolean.valueOf(false));
//			hpara.put(codes[i], paraValue);
//		}
//		return hpara;
//	}

	/**
	 * 初试化单据变量，通过一次远程调用完成
	 */
	private void initVars(String pkcorp) {

		String salecorp = null;

		if (pkcorp == null || pkcorp.trim().length() <= 0)
			salecorp = getClientEnvironment().getCorporation().getPrimaryKey();
		else
			salecorp = pkcorp;

		// 批量获取初始参数
		String syParas[] = { "SA02", "SA15","SA39","SA40","SA41","SA50",
				"BD302","BD501", "BD502", "BD503", "BD505", 
				"SO14", "SO17","SO21", "SO27", "SO34", "SO45","SO01", "SO03",
				"SO41", "IC090", "SO25", "SO26", "SO23", "SO24","SO43", "SO29", "SO59",
				"SO40","SO72","SO78" };

		// 批量获取
		try {
	
			hsparas = SysInitBO_Client.queryBatchParaValues(salecorp, syParas);
			//hsparas = getPara(salecorp,syParas);

			head_defs = DefSetTool.getDefHead(salecorp, getBillType());

			body_defs = DefSetTool.getDefBody(salecorp, getBillType());

		} catch (Exception e) {
			SCMEnv.out(e);
		}

		// 加载参数
		getSystemPara();

	}

	/**
	 * 获得系统参数。
	 * 
	 * 创建日期：(2001-8-8 9:26:11)
	 * 
	 * 修改日期：2003-10-16 修改人：杨涛 修改内容：增加SO_21的取值（预收优先，Y-比例，N-金额）
	 * 
	 * 修改日期：2003-10-27 修改人：杨涛 修改内容：SO_21的取值（“比例”和“金额”）
	 * 
	 */
	private void getSystemPara() {
		try {
			
			// hsparas为空，再次加载参数
			java.util.Hashtable h = hsparas;
			if (h == null) {
				String para[] = { "SA02", "SA15", "SA39","SA40","SA41","SA50",
						"SO01", "SO03", "SO14","SO17", "SO21", "SO22", "SO23", "SO24", "SO25",
						"SO26", "SO27", "SO29", "SO34", "SO41", "SO43", "SO45",
						"IC090", "BD302", "BD501", "BD502",
						"BD503", "BD505","SO59","SO40","SO72","SO78" };
				String sCurrPkCorp = ClientEnvironment.getInstance()
				.getCorporation().getPk_corp();
				h = SysInitBO_Client.queryBatchParaValues(sCurrPkCorp, para);
			}
			
			// 查询集团级参数
			Hashtable h0001 = new Hashtable();
			String para0001[] = { "SA13","SA09" };
			h0001 = SysInitBO_Client.queryBatchParaValues("0001", para0001);

			SA_39 = getParaBoolean(h, "SA39");
			SA_40 = getParaBoolean(h, "SA40");
			SA_41 = getParaBoolean(h, "SA41");
			
			SA_15 = getParaBoolean(h, "SA15");
			if (SA_15 == null)
				SA_15 = UFBoolean.FALSE;
			SA_50 = getParaBoolean(h, "SA50");
			if (SA_50 == null)
				SA_50 = UFBoolean.FALSE;
			
			SO72 = getParaBoolean(h, "SO72");
			if (SO72==null)
				SO72 = new UFBoolean(false);

			SO78 = getParaBoolean(h, "SO78");
			if (SO78==null)
				SO78 = new UFBoolean(false);
					
			SO27 = getParaBoolean(h, "SO27");
			SO_01 = getParaInt(h, "SO01");
			SO_03 = getParaBoolean(h, "SO03");
			SO_14 = getParaBoolean(h, "SO14");
			SO_17 = getParaBoolean(h, "SO17");

			BD302 = getParaBoolean(h, "BD302");
			BD501 = getParaInt(h, "BD501");
			BD502 = getParaInt(h, "BD502");
			BD503 = getParaInt(h, "BD503");
			BD505 = getParaInt(h, "BD505");

			// yt update 2003-10-27
			SO_21 = (String) h.get("SO21");
			if (SO_21 == null)
				SO_21 = "比例";/*-=notranslate=-*/

			// 基价含税同时考虑集团和公司
			String SA13 = (String) h0001.get("SA13");
			if ("集团定价".equals(SA13)) {//SA09--集团参数基价是否含税
				SA_02 = getParaBoolean(h0001, "SA09");
			} else {//SA02--公司参数基价是否含税
				SA_02 = getParaBoolean(h, "SA02");
			}

			if (SA_02 == null) {
				SA_02 = getParaBoolean(h, "SA02");
			}
			if (SA_02 == null)
				SA_02 = UFBoolean.FALSE;

			String svalue = (String) h.get("SO34");
			if (svalue != null && svalue.trim().length() > 0) {
				SO34 = new UFDouble(svalue.trim());
			} else {
				SO34 = new UFDouble(1.0);
			}

			SO45 = getParaBoolean(h, "SO45");
			if (SO45 == null)
				SO45 = SoVoConst.buffalse;

			SO41 = getParaBoolean(h, "SO41");
			if (SO41 == null)
				SO41 = SoVoConst.buffalse;

			SO25 = getParaBoolean(h, "SO25");
			svalue = (String) h.get("SO26");
			if (svalue != null && svalue.trim().length() > 0) {
				SO26 = new UFDouble(svalue.trim());
			} else {
				SO26 = new UFDouble(0);
			}
			svalue = (String) h.get("SO43");
			if (svalue != null && svalue.trim().length() > 0) {
				SO43 = new UFDouble(svalue.trim());
			} else {
				SO43 = new UFDouble(0);
			}

			SO23 = getParaBoolean(h, "SO23");
			IC003 = SO23;
			svalue = (String) h.get("SO24");
			if (svalue != null && svalue.trim().length() > 0) {
				SO24 = new UFDouble(svalue.trim());
			} else {
				SO24 = new UFDouble(0);
			}
			IC004 = SO24;
			svalue = (String) h.get("SO29");
			if (svalue != null && svalue.trim().length() > 0) {
				SO29 = new UFDouble(svalue.trim());
			} else {
				SO29 = new UFDouble(0);
			}
			SO59 = getParaBoolean(h, "SO59");
			if (SO59 == null)
				SO59 = SoVoConst.buftrue;
			
			SO40 = (String) h.get("SO40");

		} catch (Exception e) {
			SCMEnv.out("系统参数获取失败!");
			handleException(e);
		}
	}

	/**
	 * 获得布尔类型参数。 创建日期：(2001-6-23 13:42:53)
	 * 
	 * @return nc.vo.pub.lang.UFDouble
	 */
	private UFBoolean getParaBoolean(java.util.Hashtable h, String key) {
		return nc.vo.scm.bd.SmartVODataUtils.getUFBoolean(h.get(key));
	}

	/**
	 * 获得整数类型参数。 创建日期：(2001-6-23 13:42:53)
	 * 
	 * @return nc.vo.pub.lang.UFDouble
	 */
	private Integer getParaInt(java.util.Hashtable h, String key) {
		return nc.vo.scm.bd.SmartVODataUtils.getInteger(h.get(key));
	}

	/**
	 * 初始化币种。
	 * 
	 * 创建日期：(2001-11-1 13:25:16)
	 * 
	 */
	private void initCurrency() {
		try {
			currtype = new BusinessCurrencyRateUtil(getCorpPrimaryKey());
		} catch (Exception exp) {
			SCMEnv.out(exp);
			throw new BusinessRuntimeException(exp.getMessage());
		}
	}

	protected abstract void initButtons();

	public abstract void setButtonsState();

	/**
	 * 获得节点号。 创建日期：(2001-11-27 13:51:07)
	 * 
	 * @return java.lang.String
	 */
	public abstract String getNodeCode();

	/**
	 * 加载卡片模板。
	 * 
	 * 创建日期：(2001-11-15 9:03:35)
	 * 
	 */
	private void loadCardTemplet() {
		
		// 设置业务类型
		for (int i = 0; i < boBusiType.getChildCount(); i++) {
			if (boBusiType.getChildButtonGroup()[i].isSelected()) {
				getBillCardPanel().setBusiType(boBusiType.getChildButtonGroup()[i].getTag());
				break;
			}
		}

		showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
				"SCMCOMMON", "UPPSCMCommon-000136")/* @res "开始加载模板...." */);

/*		billtempletVO = getBillCardPanel().getTempletData(getNodeCode(),
				SO_20.booleanValue() ? null : getBillCardPanel().getBusiType(),
				getClientEnvironment().getUser().getPrimaryKey(),
				getCorpPrimaryKey());*/
		/**v5.6流量改造**/
		billtempletVO = BillUIUtil.getDefaultTempletStatic(getNodeCode(), getBillCardPanel().getBusiType(), 
				getClientEnvironment().getUser().getPrimaryKey(), getCorpPrimaryKey(), null, null);
		/**v5.6流量改造**/

		SOBillCardTools.addExeTs(billtempletVO);
		BillData bd = new BillData(billtempletVO);

		BillItem bm = bd.getHeadItem("naccountperiod");
		if (bm != null) {
			bm.setDecimalDigits(0);
		}

		SOBillCardTools.processCTBillItem(bd, null);

		// 改变界面
		setCardPanel(bd);

		// 设置界面，置入数据源		
		getBillCardPanel().setBillData(bd);	
		
		getBillCardPanel().addSortLstn();
		getBillCardPanel().addSortRelaLstn();
		getBillCardPanel().addEditLstn();
		getBillCardPanel().addEditLstn2();
		getBillCardPanel().addTotalLstn();
		getBillCardPanel().addSortPrepareListener();

		getBillCardPanel().getBodyPanel().setAutoAddLine(false);
		getBillCardPanel().getBodyPanel().getTable().addKeyListener(new KeyListener() {

			public void keyPressed(KeyEvent e) {}

			public void keyReleased(KeyEvent e) {
				/** 当编辑最后一行存货后，按“向下”键，重复的增行动作，删除一行弥补* */
				if ((e.getKeyCode() == KeyEvent.VK_DOWN) && (getBillCardPanel().binvedit)) {
					int row = getBillCardPanel().getRowCount() - 2;
					int col = getBillCardPanel().getBodyColByKey("cinventorycode");
					col = getBillCardPanel().getBillTable("table").convertColumnIndexToView(col);

					onDelLine(new int[] { row + 1 });
					getBillCardPanel().binvedit = false;

					getBillCardPanel().getBillTable().setRowSelectionInterval(row, row);
					getBillCardPanel().getBillTable().setColumnSelectionInterval(col, col);

					// getBillCardPanel().transferFocusTo(IBillItem.BODY);
				}// end if

			}

			public void keyTyped(KeyEvent e) {}
			
		});
		
		// 过滤业务类型参照 
		UIRefPane biztypeRef = (UIRefPane) getBillCardPanel().getHeadItem("cbiztype").getComponent();
		if ( biztypeRef.getRefModel()!=null){
			String sWherePart = biztypeRef.getRefModel().getWherePart();
			sWherePart += " AND pk_busitype IN (SELECT pk_businesstype FROM pub_billbusiness WHERE coalesce(dr,0) = 0";
			sWherePart += " AND (pk_corp ='" + getCorpPrimaryKey()+ "' or pk_corp ='@@@@') ";
			sWherePart += " AND pk_billtype ='30')";
			biztypeRef.getRefModel().setWherePart(sWherePart);
		}

		// 初始化公式
		BillTools.initItemKeys();

		// 限制输入长度
		getBillCardPanel().setInputLimit();

		// 设置下拉框
		initBodyComboBox();

		//initState();

		((UIRefPane) getBillCardPanel().getBodyItem("cbatchid").getComponent())
				.setReturnCode(true);

		BillItem crownoitem = bd.getBodyItem("crowno");

		javax.swing.table.TableColumn tal = null;

		if (crownoitem != null) {
			try {
				tal = getBillCardPanel().getBodyPanel().getTable().getColumn(
						crownoitem.getName());
				if (tal != null) {
					tal.setCellRenderer(new myCellRenderer());
				}
			} catch (Exception e) {
				// e.printStackTrace();
			}

		}

		// 设置时间编辑器
		String[] tabCodes = getBillCardPanel().getBillData().getBodyTableCodes();
		for (String tabCode : tabCodes) {
			BillItem tconsigntimeitem = bd.getBodyItem("tconsigntime");
			if (tconsigntimeitem != null ) {
				// 设置时间编辑器
				try {
					//计划发货时间只在"sharecode"
					tal = getBillCardPanel().getBodyPanel(tabCode).getTable().getColumn(tconsigntimeitem.getName());
					if (tal != null) {
						BillCellEditor timecelledit = new BillCellEditor(
								new UITimeTextField());
						tal.setCellEditor(timecelledit);
					}
				} catch (Exception e) {
					// e.printStackTrace();
				}
			}
	
			BillItem tdelivertimeitem = bd.getBodyItem("tdelivertime");
			if (tdelivertimeitem != null ) {
				try {
					// 设置时间编辑器
					//要求到货时间只在"sharecode"
					tal = getBillCardPanel().getBodyPanel(tabCode).getTable().getColumn(tdelivertimeitem.getName());
					if (tal != null) {
						BillCellEditor timecelledit = new BillCellEditor(
								new UITimeTextField());
						tal.setCellEditor(timecelledit);
					}
				} catch (Exception e) {
					// e.printStackTrace();
				}
			}
		}

		BillItem bmbiztypename = bd.getHeadItem("cbiztypename");
		if (bmbiztypename != null) {
			bmbiztypename.setShow(false);
			bmbiztypename.setEdit(false);
		}

/*		// 表头业务员参照
		*//** 只有属性为销售或者采购和销售部门里的人员 V502 jiangzhe * *//*
		UIRefPane refPsn = (UIRefPane) getBillCardPanel().getHeadItem("cemployeeid").getComponent();
		if (refPsn != null)
			refPsn.setWhereString(" (bd_deptdoc.deptattr = '3' or bd_deptdoc.deptattr= '4') and bd_psndoc.pk_corp='"
							+ getCorpPrimaryKey() + "'");*/

		// 修改表体存货参照
		UIRefPane invRef = (UIRefPane) getBillCardPanel().getBodyItem("cinventorycode").getComponent();

		if (invRef != null) {
			invRef.setTreeGridNodeMultiSelected(true);
			invRef.setMultiSelectedEnabled(true);
		}

		// 行号
		BillRowNo.loadRowNoItem(getBillCardPanel(), getBillCardPanel().getRowNoItemKey());

		getBillCardTools().reload(getBillCardPanel(), getClientEnvironment());

		getBillCardPanel().setTatolRowShow(true);
		
		getBillCardTools().getInitBillItemEidtState();

		// 表体拖拽属性初始化
		initBodyFillStatus(bd);
		
	    // 表体加背景色
		String[] tableCodes = getBillCardPanel().getBillData().getBodyTableCodes();
		for (String tableCode : tableCodes) {
			getBillCardPanel().getBillTable(tableCode).setRowSelectionAllowed(true);
			getBillCardPanel().getBillTable(tableCode).setColumnSelectionAllowed(false);
		}
		
		//表体菜单监听
		String[] tabcodes = getBillCardPanel().getBillData().getTableCodes(BillItem.BODY);
		for (String tabCode : tabcodes) {
			getBillCardPanel().addActionListener(tabCode, this);
		}
		
		showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
				"SCMCOMMON", "UPPSCMCommon-000176")/* @res "模板加载成功！" */);
	}

	private void initBodyFillStatus(BillData bd) {
		HashSet<String> hs_key = new HashSet();

		// 币种、汇率、单品折扣、数量
		// 发货公司、发货库存组织、发货仓库、发货日期、发货时间、收货单位、收货地区、
		// 收货地点、收货地址、到货日期、到货时间、收货库存组织、
		// 项目、项目阶段、备注、表体自定义项1-20

		String[] skeys = new String[] {
				//
				"ccurrencytypename", "nexchangeotobrate", "nitemdiscountrate","nnumber","nquoteunitnum",
				//
				"cconsigncorp", "cadvisecalbody", "cbodywarehousename", "dconsigndate", "tconsigntime",
				//
				"creceiptcorpname", "creceiptareaname", "crecaddrnodename", "vreceiveaddress", "ddeliverdate",
				"tdelivertime", "creccalbody",
				//
				"cprojectname", "cprojectphasename", "frownote",
				//		
				"vdef1", "vdef2", "vdef3", "vdef4", "vdef5", "vdef6", "vdef7", "vdef8", "vdef9", "vdef10",
				//
				"vdef11", "vdef12", "vdef13", "vdef14", "vdef15", "vdef16", "vdef17", "vdef18", "vdef19", "vdef20", };
		hs_key.addAll(Arrays.asList(skeys));

		BillItem[] bis = bd.getBodyItems();
		for (BillItem bi : bis) {
			if (hs_key.contains(bi.getKey()) && bi.isEdit()) {
				bi.setFillEnabled(true);
			} else {
				bi.setFillEnabled(false);
			}
		}

	}

	/**
	 * 设置下拉项目
	 * 
	 * 创建日期：(01-2-26 13:29:17)
	 * 
	 */
	protected void initBodyComboBox() {
		UIComboBox comHeadItem = (UIComboBox) getBillCardPanel().getHeadItem(
				"fstatus").getComponent();
		int count = comHeadItem.getItemCount();
		if (count == 0) {
			comHeadItem.setTranslate(true);
			getBillCardPanel().getHeadItem("fstatus").setWithIndex(true);
			comHeadItem.addItem("");
			comHeadItem.addItem(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"SCMCOMMON", "UPPSCMCommon-000340")/* @res "自由" */);
			comHeadItem.addItem(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"common", "UC001-0000027")/* @res "审批" */);
			comHeadItem.addItem(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"common", "UC001-0000030")/* @res "冻结" */);
			comHeadItem.addItem(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"SCMCOMMON", "UPPSCMCommon-000119")/* @res "关闭" */);
			comHeadItem.addItem(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"common", "UC001-0000005")/* @res "作废" */);
			comHeadItem.addItem(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"SCMCOMMON", "UPPSCMCommon-000128")/* @res "结束" */);
			comHeadItem.addItem(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"SCMCOMMON", "UPPSCMCommon-000320")/* @res "正在审批中" */);
			comHeadItem.addItem(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"SCMCOMMON", "UPPSCMCommon-000242")/* @res "审批未通过" */);
		}
		UIComboBox comBodyItem = (UIComboBox) getBillCardPanel().getBodyItem(
				"frowstatus").getComponent();
		count = comBodyItem.getItemCount();
		if (count == 0) {
			getBillCardPanel().getBodyItem("frowstatus").setWithIndex(true);
			comBodyItem.addItem("");
			comBodyItem.addItem(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"SCMCOMMON", "UPPSCMCommon-000340")/* @res "自由" */);
			comBodyItem.addItem(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"common", "UC001-0000027")/* @res "审批" */);
			comBodyItem.addItem(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"common", "UC001-0000030")/* @res "冻结" */);
			comBodyItem.addItem(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"SCMCOMMON", "UPPSCMCommon-000119")/* @res "关闭" */);
			comBodyItem.addItem(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"common", "UC001-0000005")/* @res "作废" */);
			comBodyItem.addItem(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"SCMCOMMON", "UPPSCMCommon-000128")/* @res "结束" */);
			comBodyItem.addItem(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"SCMCOMMON", "UPPSCMCommon-000320")/* @res "正在审批中" */);
			comBodyItem.addItem(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"SCMCOMMON", "UPPSCMCommon-000242")/* @res "审批未通过" */);
		}
		UIComboBox comBatch = (UIComboBox) getBillCardPanel().getBodyItem(
				"fbatchstatus").getComponent();
		count = comBatch.getItemCount();
		if (count == 0) {
			getBillCardPanel().getBodyItem("fbatchstatus").setWithIndex(true);
			comBatch.addItem(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"SCMCOMMON", "UPPSCMCommon-000340")/* @res "自由" */);
			comBatch.addItem(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"40060301", "UPP40060301-000152")/* @res "批指定" */);
			comBatch.addItem(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"40060301", "UPP40060301-000153")/* @res "整批" */);
		}
	}

	/**
	 * 初试化单据状态
	 * 
	 * 创建日期：(2001-11-15 9:02:22)
	 * 
	 */
	private void initState() {

		// 初试化
		id = "";
		strState = "自由"/*-=notranslate=-*/;

		getBillCardPanel().setEnabled(false);

	}

	/**
	 * 加载卡片模板。
	 * 
	 * 创建日期：(2001-11-15 9:03:35)
	 * 
	 */
	private void loadCardTemplet(String billType, String busiType,
			String userid, String corpid) {

		BillData bd = null;

		billtempletVO = null;

		if (SO_20.booleanValue()) {

			if (billtempletVO == null) {
				billtempletVO = getBillCardPanel().getTempletData(
						"40060302".equals(getNodeCode()) ? getNodeCode()
								: getBillType(), null,
						getClientEnvironment().getUser().getPrimaryKey(),
						getCorpPrimaryKey());
			}
			SOBillCardTools.addExeTs(billtempletVO);
			bd = new BillData(billtempletVO);

		} else {
			if (billtempletVO == null) {
				billtempletVO = getBillCardPanel().getTempletData(
						"40060302".equals(getNodeCode()) ? getNodeCode()
								: getBillType(), busiType,
						getClientEnvironment().getUser().getPrimaryKey(),
						getCorpPrimaryKey());
			}
			SOBillCardTools.addExeTs(billtempletVO);
			bd = new BillData(billtempletVO);
		}

		// 设置销售公司为不可编辑项
		if (bd != null) {
			BillItem salecorpbm = bd.getHeadItem("salecorp");
			if (salecorpbm != null) {
				salecorpbm.setEdit(false);
				salecorpbm.setEnabled(false);
			}

			BillItem bm = bd.getHeadItem("naccountperiod");
			if (bm != null) {
				bm.setDecimalDigits(0);
			}

		}

		SOBillCardTools.processCTBillItem(bd, null);

		// 改变界面
		setCardPanel(bd);

		// 设置界面，置入数据源
		getBillCardPanel().setBillData(bd);

		// 初始化公式
		BillTools.initItemKeys();

		// 设置下拉框
		initBodyComboBox();

		// 初试化状态
		initState();

		// 设置表头参照的公司
		BillItem[] bms = bd.getHeadItems();
		nc.ui.bd.ref.AbstractRefModel refmodel = null;

		try {

			for (int i = 0, loop = bms.length; i < loop; i++) {
				if (bms[i].getDataType() == BillItem.UFREF) {
					refmodel = ((UIRefPane) bms[i].getComponent())
							.getRefModel();
					if (refmodel != null)
						refmodel.setPk_corp(corpid);
				}
			}

		} catch (Exception e) {
			SCMEnv.out("设置参照的公司约束失败！");
		}

		BillItem bmbiztypename = bd.getHeadItem("cbiztypename");
		if (bmbiztypename != null) {
			bmbiztypename.setShow(false);
			bmbiztypename.setEdit(false);
		}

		BillItem bmbiztype = bd.getHeadItem("cbiztype");
		if (bmbiztype != null) {
			bmbiztype.setEdit(false);
		}

		// 修改表体存货参照
		UIRefPane invRef = (UIRefPane) getBillCardPanel().getBodyItem(
				"cinventorycode").getComponent();

		if (invRef != null) {
				invRef.setTreeGridNodeMultiSelected(true);
				invRef.setMultiSelectedEnabled(true);
		}

		// 行号
		BillRowNo.loadRowNoItem(getBillCardPanel(), getBillCardPanel()
				.getRowNoItemKey());

		getBillCardPanel().setTatolRowShow(true);

		showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
				"SCMCOMMON", "UPPSCMCommon-000176")/* @res "模板加载成功！" */);

	}

	/**
	 * 初始化前改变界面。
	 * 
	 * 创建日期：(2001-11-15 9:20:13)
	 * 
	 * @param bdData
	 *            nc.ui.pub.bill.BillData
	 * 
	 */
	private void setCardPanel(BillData bdData) {
		setCardPanelByPara(bdData);
		setCardPanelByOther(bdData);
	}

	/**
	 * 根据参数改变界面(主要是各字段精度控制)。
	 * 
	 * 创建日期：(2001-9-27 16:13:57)
	 * 
	 */
	private void setCardPanelByPara(BillData bdData) {
		try {
			
			/** V53 根据需求发货库存组织的编辑和显示不再受SO46参数的影响
			 *  SA_16根据SO46改变而改变
			 **/
			/*if (getBillType().equals(SaleBillType.SaleOrder)
					|| getBillType().equals(SaleBillType.SaleInitOrder)) {
				// 建议发货组织
				if (SA_16.booleanValue()) {
					if (bdData.getBodyItem("sharecode2", "cadvisecalbody") != null)
						bdData.getBodyItem("sharecode2", "cadvisecalbody")
								.setShow(true);
					else
						bdData.getBodyItem("cadvisecalbody").setShow(true);
				} else {
					if (bdData.getBodyItem("sharecode2", "cadvisecalbody") != null)
						bdData.getBodyItem("sharecode2", "cadvisecalbody")
								.setShow(false);
					else
						bdData.getBodyItem("cadvisecalbody").setShow(false);
				}
				// bdData.getBodyItem("table", "cadvisecalbody").setShow(false);
			}*/

			/** V502 项目补丁合进通版zhanghaiyan zhongwei* */
			// // 基价是否含税
			// if (SA_02 != null && SA_02.booleanValue()) {
			// // 无税金额
			// if (bdData.getBodyItem("noriginalcurmny") != null)
			// bdData.getBodyItem("noriginalcurmny").setEdit(false);
			// else
			// System.out
			// .println(" bdData.getBodyItem(noriginalcurmny) is null ");
			// // 价税合计
			// if (bdData.getBodyItem("noriginalcursummny") != null)
			// getBillCardTools().resumeBillBodyItemEdit(
			// bdData.getBodyItem("noriginalcursummny"));
			// // bdData.getBodyItem("noriginalcursummny").setEdit(true);
			// else
			// System.out
			// .println(" bdData.getBodyItem(noriginalcursummny) is null ");
			// } else {
			// // 无税金额
			// if (bdData.getBodyItem("noriginalcurmny") != null)
			// // bdData.getBodyItem("noriginalcurmny").setEdit(true);
			// getBillCardTools().resumeBillBodyItemEdit(
			// bdData.getBodyItem("noriginalcurmny"));
			//
			// else
			// System.out
			// .println(" bdData.getBodyItem(noriginalcurmny) is null ");
			// // 价税合计
			// if (bdData.getBodyItem("noriginalcursummny") != null)
			// bdData.getBodyItem("noriginalcursummny").setEdit(false);
			// else
			// System.out
			// .println(" bdData.getBodyItem(noriginalcursummny) is null ");
			// }
			/** V502 项目补丁合进通版zhanghaiyan zhongwei* */

			// 设置折扣小数位数
			if (bdData.getBodyItem("nitemdiscountrate") != null)
				bdData.getBodyItem("nitemdiscountrate").setDecimalDigits(6);
			if (bdData.getBodyItem("ndiscountrate") != null)
				bdData.getBodyItem("ndiscountrate").setDecimalDigits(6);
            if(bdData.getHeadItem("ndiscountrate")!= null)
            	bdData.getHeadItem("ndiscountrate").setDecimalDigits(6);
            bdData.getHeadItem("ndiscountrate").setLength(21);
            bdData.getBodyItem("ndiscountrate").setLength(21);
            bdData.getBodyItem("nitemdiscountrate").setLength(21);          
            
			// 币种汇率
			if (getBillType().equals(SaleBillType.SaleOrder)) {
				if (bdData.getHeadItem("nexchangeotobrate") != null)
					bdData.getHeadItem("nexchangeotobrate").setDecimalDigits(4);
			}
			if (bdData.getBodyItem("nexchangeotobrate") != null)
				bdData.getBodyItem("nexchangeotobrate").setDecimalDigits(4);

			if (BD501 != null) {
				String[] aryNum = SOBillCardTools.getSaleItems_Num();
				for (int i = 0; i < aryNum.length; i++) {
					if (bdData.getBodyItem(aryNum[i]) != null)
						bdData.getBodyItem(aryNum[i]).setDecimalDigits(
								BD501.intValue());
					else
						SCMEnv.out(" bdData.getBodyItem(" + aryNum[i]
								+ ") is null ");
				}
			}

			SCMEnv.out(" into setCardPanelByPara 4 ");

			if (BD502 != null) {
				bdData.getBodyItem("npacknumber").setDecimalDigits(
						BD502.intValue());
				// bdData.getBodyItem("nquoteunitnum").setDecimalDigits(BD502.intValue());
			}

			// 单价
			if (BD505 != null) {
				for (int i = 0; i < SOBillCardTools.getSaleItems_Price().length; i++) {
					bdData.getBodyItem(SOBillCardTools.getSaleItems_Price()[i])
							.setDecimalDigits(BD505.intValue());
				}
			}

			// 换算率
			if (BD503 != null) {
				bdData.getBodyItem("scalefactor").setDecimalDigits(
						BD503.intValue());
				bdData.getBodyItem("nqtscalefactor").setDecimalDigits(
						BD503.intValue());
				bdData.getHeadItem("npreceiverate").setDecimalDigits(
						BD503.intValue());
			}

			// 如果报价单位无税单/净价，报价单位含税单/净价可见，则设置无税单/净价，含税单/净价等不可编辑
			/*if (bdData.getBodyItem("norgqtprc").isShow()
					|| bdData.getBodyItem("norgqttaxprc").isShow()
					|| bdData.getBodyItem("norgqtnetprc").isShow()
					|| bdData.getBodyItem("norgqttaxnetprc").isShow()) {
				bdData.getBodyItem("noriginalcurprice").setEdit(false);
				bdData.getBodyItem("noriginalcurtaxprice").setEdit(false);
				bdData.getBodyItem("noriginalcurtaxnetprice").setEdit(false);
				bdData.getBodyItem("noriginalcurnetprice").setEdit(false);
			}*/

			// 如果报价数量显示，则主数量不可编辑
			/*if (bdData.getBodyItem("nquoteunitnum").isShow()) {
				bdData.getBodyItem("nnumber").setEdit(false);
			}*/
			
			//表体折本汇率、币种均不允许编辑
			getBillCardTools().setBodyItemEnable(new String[]{"ccurrencytypeid",
					"ccurrencytypename","nexchangeotobrate"},false);
			
			//无论模板和参数如何设置，本币和辅币均不可编辑
			getBillCardTools().setBodyItemEnable(
					SOBillCardTools.getSaleOrderItems_Price_Mny_NoOriginal(),false);
			
			getBillCardTools().setBodyItemEnable(
					SOBillCardTools.getSaleOrderCurrTypeDigit(), false);
			
			//折扣额
			getBillCardTools().setBodyItemEnable(new String[]{"noriginalcurdiscountmny","ndiscountmny"}, false);
			
		} catch (Exception e) {
			SCMEnv.out(e);
			// e.printStackTrace();
		}

	}

	/**
	 * 由其它原因改变界面。
	 * 
	 * 创建日期：(2001-11-15 9:18:13)
	 * 
	 * @param bdData
	 *            nc.ui.pub.bill.BillData
	 * 
	 */
	private void setCardPanelByOther(BillData bdData) {

		try {
			//隐藏捆绑、预订单是否关闭
			getBillCardPanel().hideBodyTableCol("bbindflag");
			getBillCardPanel().hideBodyTableCol("bpreorderclose");
			// 存货参照
			UIRefPane refInv = (UIRefPane) bdData.getBodyItem("cinventorycode")
					.getComponent();
			refInv.getRefModel().addWherePart(" AND bd_invmandoc.sealflag='N'");
			// 批次号
			bdData.getBodyItem("cbatchid").setComponent(
					getBillCardPanel().getLotNumbRefPane());
			bdData.getBodyItem("cbatchid").setDataType(BillItem.STRING);

			// 散户
			UIRefPane ref = (UIRefPane) bdData.getHeadItem("cfreecustid")
					.getComponent();
			ref.getRef().setRefUI(new UFRefGridUI());
			if (getBillType().equals(SaleBillType.SaleOrder)) {
				// 表头收货地址
				UIRefPane refHeadAddress = (UIRefPane) bdData.getHeadItem(
						"vreceiveaddress").getComponent();
				refHeadAddress.setAutoCheck(false);
				refHeadAddress.setReturnCode(true);
				// 表体收货地址
				UIRefPane vreceiveaddress = (UIRefPane) bdData.getBodyItem(
						"vreceiveaddress").getComponent();
				vreceiveaddress.setAutoCheck(false);
				vreceiveaddress.setReturnCode(true);
				bdData.getBodyItem("vreceiveaddress").setDataType(
						BillItem.USERDEF);
				// 收货地区
				UIRefPane creceiptareaid = (UIRefPane) bdData.getBodyItem(
						"creceiptareaid").getComponent();
				creceiptareaid.setReturnCode(false);
				// 收货单位
				UIRefPane creceiptcorpid = (UIRefPane) bdData.getBodyItem(
						"creceiptcorpid").getComponent();
				creceiptcorpid.setReturnCode(false);
				// 建议发货库存组织
				UIRefPane cadvisecalbody = (UIRefPane) bdData.getBodyItem(
						"cadvisecalbody").getComponent();
				cadvisecalbody.setReturnCode(false);
			}

			// // 项目
			// ref = (UIRefPane)
			// bdData.getBodyItem("cprojectname").getComponent();
			// ref.setRefType(nc.ui.bd.ref.IBusiType.GRIDTREE);
			// ref.setRefModel(new nc.ui.bd.b39.JobRefTreeModel("0001",
			// getCorpPrimaryKey(), null));

			// 改变新参照的长度
			getBillCardPanel().getFreeItemRefPane().setMaxLength(1000);

			// 设置新的参照，要求指出相应的字段名
			// 表体,自由项
			bdData.getBodyItem("vfree0").setComponent(getBillCardPanel().getFreeItemRefPane());
			
			// 设置自定义项
			DefVO[] defs = null;
			// 表头
			// 查得对应于公司的该单据的自定义项设置
			defs = head_defs;

/*			if (defs != null) {
				bdData.updateItemByDef(defs, "vdef", true);
			}

			// 表体
			// 查得对应于公司的该单据的自定义项设置
			defs = body_defs;

			if (defs != null) {
				bdData.updateItemByDef(defs, "vdef", false);
			}*/
			
			getBillCardPanel().setBillData(bdData);
			//卡片
			DefSetTool.updateBillCardPanelUserDef(getBillCardPanel(),
					getClientEnvironment().getCorporation().getPk_corp(),
					SaleBillType.SaleOrder, "vdef", "vdef");
			
			getBillCardPanel().setBillData(bdData);
           
			/** 统计型的自定义项可以编辑录入 龙旗提出* */
/*			for (int i = 1; i <= 20; i++) {
				nc.ui.pub.bill.BillItem item = bdData.getBodyItem("vdef" + i);
				if (item != null && item.getDataType() == nc.ui.pub.bill.BillItem.USERDEF) {
					((nc.ui.pub.beans.UIRefPane) item.getComponent()).setAutoCheck(false);
				}
				if (item != null && item.getComponent() != null)
					((nc.ui.pub.beans.UIRefPane) item.getComponent()).setEditable(item.isEdit());
			}*/
			/** 统计型的自定义项可以编辑录入 龙旗提出* */
			
		} catch (Exception e) {
			SCMEnv.out(e);
			// e.printStackTrace();
		}
	}

	/**
	 * 初始化类。
	 */
	protected void initialize(String pk_corp, String billtype, String busitype,
			String operator, String id) {
		// 设置界面

		String cbilltype = billtype;
		if (cbilltype == null || cbilltype.trim().length() <= 0)
			cbilltype = "30";
		String csalecorp = pk_corp;
		String cbusitype = busitype;
		String cuserid = operator;
		String cbillid = id;

		try {
			setName("SaleOrder");
			setSize(774, 419);
			add(getBillCardPanel(), "Center");

			String sql = "select pk_corp,vreceiptcode,cbiztype,coperatorid from so_sale "
					+ "where so_sale.csaleid='" + cbillid.trim() + "'";
			SORowData[] rows = null;
			rows = nc.ui.so.so016.SOToolsBO_Client.getSORows(sql);
			if (rows != null && rows.length > 0 && rows[0] != null) {
				csalecorp = rows[0].getString(0);
				cbusitype = rows[0].getString(2);
				cuserid = rows[0].getString(3);
			}

		} catch (java.lang.Throwable ivjExc) {
			handleException(ivjExc);
		}
		// 用当前登陆公司初始化参数
		initVars(getCorpPrimaryKey());

		// 初试化币种
		initCurrency();

		// 加载模板
		loadCardTemplet(cbilltype, cbusitype, cuserid, csalecorp);

		strState = "自由";

		// 行号
		BillRowNo.loadRowNoItem(getBillCardPanel(), getBillCardPanel()
				.getRowNoItemKey());

		// 加载数据
		loadCardDataByID(id);
	}

	protected void loadCardDataByID(String sID) {
		loadCardDataByID(sID, false);
	}

	/**
	 * 根据订单ID加载卡片数据。
	 * 
	 * 创建日期：(2001-11-15 9:02:22)
	 * 
	 */
	protected void loadCardDataByID(String sID, boolean b_datapower) {
		try {
			long s = System.currentTimeMillis();
			SaleOrderVO saleorderVO = (SaleOrderVO) SaleOrderBO_Client
					.queryData(sID);

			// 用数据权限过滤
			if (b_datapower) {

				getQueryDlg(saleorderVO.getPk_corp()).initConditions();
				ConditionVO[] voCondition = getQueryDlg().getConditionVO();
				java.util.Set<String> datapowerConditions = new java.util.HashSet<String>();
				for (ConditionVO c : voCondition) {
					if (c!=null){
						if (c.getValue().trim().indexOf("(select distinct power.resource_data_id") > 0) {
							datapowerConditions.add(c.getFieldCode());
						}
					}
				}

				// 过滤条件
				ArrayList<ConditionVO> filted = new ArrayList<ConditionVO>();
				for (int i = 0, len = voCondition.length; i < len; i++) {
					if (voCondition[i]!=null){
						// // 判断数据权限条件
						if (datapowerConditions.contains(voCondition[i].getFieldCode())) {
							filted.add(voCondition[i]);
						}
					}
				}
				if (filted.size()>0)
					voCondition = filted.toArray(new ConditionVO[0]);

//				// 地区分类权限条件转换
//				DataPowerUtils.trnsDPCndFromAreaToCust(voCondition,
//						"ccustomerid");

				if (voCondition != null && voCondition.length > 0) {
					ConditionVO[] conditions = new ConditionVO[voCondition.length + 1];

					System.arraycopy(voCondition, 0, conditions, 0,
							voCondition.length);

					ConditionVO con_id = new ConditionVO();
					con_id.setFieldCode("so_sale.csaleid");
					con_id.setOperaCode("=");
					con_id.setValue(sID);

					conditions[voCondition.length] = con_id;

					SaleorderHVO[] hvo = SaleOrderBO_Client
							.queryHeadAllData(getQueryDlg().getWhereSQL(
									conditions));
					if (hvo == null || hvo.length == 0) {
						saleorderVO = null;
						showErrorMessage(nc.ui.ml.NCLangRes.getInstance()
								.getStrByID("40060301", "UPP40060301-000539")/*
																				 * @res
																				 * "没有查看单据的权限"
																				 */);
						return;
					}
				}

			}// end if datapower

			// 设置表头参照的公司
			BillItem[] bms = getBillCardPanel().getHeadItems();
			nc.ui.bd.ref.AbstractRefModel refmodel = null;

			for (int i = 0, loop = bms.length; i < loop; i++) {
				if (bms[i].getDataType() == BillItem.UFREF) {
					refmodel = ((UIRefPane) bms[i].getComponent())
							.getRefModel();
					if (refmodel != null) {
						try {
							refmodel.setPk_corp(saleorderVO.getHeadVO()
									.getPk_corp());
							refmodel.setUseDataPower(false);
						} catch (Exception e) {
							SCMEnv.out("设置参照：" + bms[i].getName() + "的公司约束失败！");
						}
					}
				}
			}

			// 收货地址参照
			UIRefPane vreceiveaddress = (UIRefPane) getBillCardPanel()
					.getHeadItem("vreceiveaddress").getComponent();

			SCMEnv.out("数据读取[共用时" + (System.currentTimeMillis() - s) + "]");
			s = System.currentTimeMillis();

			// 金额按币种携带精度
			getBillCardPanel().setPanelByCurrency(
					((SaleorderBVO) saleorderVO.getChildrenVO()[0])
							.getCcurrencytypeid());

			// 重新设置精度 2007-11-27 xhq
			if (!getCorpPrimaryKey().equals(
					saleorderVO.getHeadVO().getPk_corp())) {
				initVars(saleorderVO.getHeadVO().getPk_corp());
				nc.ui.pub.bill.BillData bd = getBillCardPanel().getBillData();
				setCardPanelByPara(bd);
			}		
			
			// 加载数据
			getBillCardPanel().setBillValueVO(saleorderVO);
			
			// 给1-20自定义项pk设值  liuzwc 2006-06-25
			for (int i=1; i<=20; i++){
				Object tempDefdoc= saleorderVO.getParentVO().getAttributeValue("pk_defdoc"+i) ;
				if(tempDefdoc != null) {
					UIRefPane tempVdef = (UIRefPane)getBillCardPanel().getHeadItem("vdef"+i).getComponent();
					tempVdef.setPK(tempDefdoc);
				}
			}
			
			SCMEnv.out("数据设置[共用时" + (System.currentTimeMillis() - s) + "]");
			long s1 = System.currentTimeMillis();
			
			getBillCardPanel().getBillModel().execLoadFormula();
			SCMEnv.out("执行公式[共用时" + (System.currentTimeMillis() - s1) + "]");

			// 收货地址参照
			if (getBillCardPanel().getHeadItem("creceiptcustomerid") != null)
				((CustAddrRefModel) vreceiveaddress.getRefModel())
						.setCustId(getBillCardPanel().getHeadItem(
								"creceiptcustomerid").getValue());

			Object temp = saleorderVO.getParentVO().getAttributeValue(
					"vreceiveaddress");
			// 基础档案ID
			String formula = "getColValue(bd_cumandoc,pk_cubasdoc,pk_cumandoc,creceiptcustomerid)";
			String pk_cubasdoc = (String) getBillCardPanel().execHeadFormula(
					formula);

			// 收货地址
			vreceiveaddress.setAutoCheck(false);
			String strvreceiveaddress = BillTools.getColValue2("bd_custaddr",
					"pk_custaddr", "defaddrflag", "Y", "pk_cubasdoc",
					pk_cubasdoc);
			if (temp != null) {
				vreceiveaddress.getUITextField().setText(temp.toString());
			} else if (strvreceiveaddress != null
					&& strvreceiveaddress.trim().length() > 0) {
				vreceiveaddress.setPK(strvreceiveaddress);
			}

			// 表体属性加载
			Object cpackunitid, fixedflag, value;
			for (int i = 0; i < getBillCardPanel().getRowCount(); i++) {
				// 附属品标志加载
				String[] appendFormula = { "isappendant->getColValue(bd_invmandoc,isappendant,pk_invmandoc,cinventoryid)" };
				getBillCardPanel().execBodyFormulas(i, appendFormula);

				// 非固定换算辅计量
				cpackunitid = getBillCardPanel().getBodyValueAt(i,
						"cpackunitid");
				if (cpackunitid != null) {
					fixedflag = BillTools.getColValue2("bd_convert",
							"fixedflag", "pk_invbasdoc",
							(String) getBillCardPanel().getBodyValueAt(i,
									"cinvbasdocid"), "pk_measdoc",
							(String) cpackunitid);

					if (fixedflag != null && "N".equals(fixedflag.toString())) {
						value = ((UFDouble) getBillCardPanel().getBodyValueAt(
								i, "nnumber"))
								.div((UFDouble) getBillCardPanel()
										.getBodyValueAt(i, "npacknumber"));
						getBillCardPanel().setBodyValueAt(value, i,
								"scalefactor");
					}

				}

			}// end for

			setHeadDefaultData();
			getBillCardPanel().initFreeItem();

			// 设置整单折扣
			UFDouble ndiscountrate = getBillCardTools().getHeadUFDoubleValue(
					"ndiscountrate");
			if (ndiscountrate == null) {
				ndiscountrate = getBillCardTools().getBodyUFDoubleValue(0,
						"ndiscountrate");
			}
			if (ndiscountrate == null)
				ndiscountrate = new UFDouble(100);

			getBillCardPanel().setHeadItem("ndiscountrate", ndiscountrate);

			formula = "salecorp->getColValue(bd_corp,unitname,pk_corp,pk_corp)";
			// 销售公司
			getBillCardPanel().execHeadFormula(formula);

			getBillCardPanel().updateValue();
			getBillCardPanel().getBillModel().reCalcurateAll();

			showHintMessage(nc.ui.ml.NCLangRes.getInstance()
					.getStrByID(
							"40060301",
							"UPP40060301-000186",
							null,
							new String[] { (System.currentTimeMillis() - s)
									/ 1000 + "" }));

			// 加载至缓存
			vocache.setCatheData(new SaleOrderVO[] { saleorderVO });

		} catch (ValidationException e) {
			showErrorMessage(e.getMessage());
		} catch (Exception e) {
			SCMEnv.out(e);
			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"SCMCOMMON", "UPPSCMCommon-000256")/* @res "数据加载失败！" */);
			//e.printStackTrace();
		}
	}

	/**
	 * 修改单据时设置表头默认数据。
	 * 
	 * 创建日期：(2001-8-27 10:05:59)
	 * 
	 */
	protected void setHeadDefaultData() {
		if (getBillCardPanel().getRowCount() > 0) {
			// 币种
			UIRefPane ccurrencytypeid = (UIRefPane) getBillCardPanel()
					.getHeadItem("ccurrencytypeid").getComponent();
			ccurrencytypeid.setPK(getBillCardPanel().getBodyValueAt(0,
					"ccurrencytypeid"));

			// 折本汇率
			UIRefPane nexchangeotobrate = (UIRefPane) getBillCardPanel()
					.getHeadItem("nexchangeotobrate").getComponent();
			if (getBillCardPanel().getBodyValueAt(0, "nexchangeotobrate") != null)
				nexchangeotobrate.setValue(getBillCardPanel().getBodyValueAt(0,
						"nexchangeotobrate").toString());
		}
	}

	public SOBillCardPanel getBillCardPanel() {
		if (cardpanel == null) {
			try {
				cardpanel = new SOBillCardPanel(this, "BillCardPanel",
						getBillType(), getCorpPrimaryKey(),
						getClientEnvironment().getUser().getPrimaryKey());
				//隐藏是否捆绑
				//cardpanel.hideBodyTableCol("bbindflag");
				// 设置表头编辑公式自动执行
				cardpanel.setAutoExecHeadEditFormula(true);
				
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return cardpanel;
	}

	public SOBillCardTools getBillCardTools() {
		if (cardtools == null) {
			cardtools = new SOBillCardTools(getBillCardPanel(),
					getClientEnvironment());
		}
		return cardtools;
	}
	
	/**
	 * 币种换算工具
	 */
	public SOCurrencyRateUtil getSOCurrencyRateUtil() {
		if (socrutil == null) {
			socrutil = new SOCurrencyRateUtil(getClientEnvironment().getCorporation().getPrimaryKey());
		}
		return socrutil;
	}

	public abstract String getBillType();

	/**
	 * 重排行号
	 */
	public void onResortRowNo(){
		nc.ui.scm.pub.report.BillRowNo.addNewRowNo(getBillCardPanel(),
				SaleBillType.SaleOrder, "crowno");
	}
	
	/**
	 * 卡片编辑
	 */
	public void onCardEdit(){
		try{	        
	        //getBillCardPanel().addActionListener(this);
			//清空‘过滤条件’ zhongwei  2009-06-09
			if (SO_03 != null && !SO_03.booleanValue()) {
				((UIRefPane) getBillCardPanel().getBodyItem("cinventorycode")
				.getComponent()).getRefModel().setFilterPks(new String[]{},
						IFilterStrategy.REFDATACOLLECT_MINUS_INSECTION);
			}
			
	        getBillCardPanel().startRowCardEdit();
	      }finally{
	       // getBillCardPanel().addActionListener(this);
	      }
	}
	
	/**
	 * 插入行。 创建日期：(2001-6-23 14:41:56)
	 */
	public void onInsertLine() {
		int row = getBillCardPanel().getBillTable().getSelectedRow();
		onInsertLine(row);
		//行颜色为原有背景色
		getBillCardPanel().resetErrorRowColor(row);
	}

	/**
	 * 插入行。 创建日期：(2001-6-23 14:41:56)
	 */
	public void onInsertLine(int row) {

		getBillCardPanel().alInvs.add(row, null);
		vRowATPStatus.insertElementAt(new UFBoolean(false), row);
		getBillCardPanel().insertLine();
		BillRowNo.insertLineRowNo(getBillCardPanel(), getBillType(),
				getRowNoItemKey());
		getBillCardPanel().setBodyDefaultData(row, row + 1);

		// 设置自由项输入提示
		// 批量获取存货信息
		InvVO[] invvos = new InvVO[getBillCardPanel().getRowCount()];
		for (int i = 0; i < invvos.length; i++) {
			invvos[i] = new InvVO();
			invvos[i].setCinventoryid((String) getBillCardPanel()
					.getBodyValueAt(i, "cinventoryid"));
		}

		InvoInfoBYFormula invvosget = new InvoInfoBYFormula();
		invvos = invvosget.getQuryInvVOs(invvos, false, true, 1);

		ArrayList al_invs = new ArrayList();
		for (InvVO invvo : invvos) {
			al_invs.add(invvo);
		}

		InvAttrCellRenderer ficr = new InvAttrCellRenderer();
		ficr.setFreeItemRenderer(getBillCardPanel(), al_invs);

		updateUI();
	}

	/**
	 * 增加行。
	 * 
	 * 创建日期：(2001-4-13 16:37:56)
	 * 
	 */
	public void onAddLine() {
		if (checkAddLine()) {
			addLine();
		}
	}

	/**
	 * 具体增行操作（没有条件判断）
	 * 
	 */
	public void addLine() {
		BillActionListener  bal = getBillCardPanel().getBodyPanel().getBillActionListener(); 
		getBillCardPanel().getBodyPanel().removeBillActionListener();
		getBillCardPanel().addLine();
		getBillCardPanel().getBodyPanel().addBillActionListener(bal);

		// 存货
		if (getBillCardPanel().alInvs != null)
			getBillCardPanel().alInvs.add(null);
		// 可用量状态
		if (vRowATPStatus != null)
			vRowATPStatus.add(new UFBoolean(false));

		// 默认数据
		int row = getBillCardPanel().getRowCount() - 1;

		// 计算并设置新增行号
		BillRowNo.addLineRowNo(getBillCardPanel(), getBillType(),
				getRowNoItemKey());
		getBillCardPanel().setBodyDefaultData(row, row + 1);
		
		// 计划发货时间
	    getBillCardPanel().setBodyValueAt("00:00:00", row, "tconsigntime");
		// 要求收货时间
	    getBillCardPanel().setBodyValueAt("00:00:00", row, "tdelivertime");

		// yb add 03-09-16
		setDefaultCellEditable(row);

		/** 如指定选中行，鼠标焦点定位会有问题* */
		// getBillCardPanel().getBillTable().getSelectionModel()
		// .setSelectionInterval(row, row + 1);
		/** 如指定选中行，鼠标焦点定位会有问题* */
		
		//行颜色为原有背景色
		//getBillCardPanel().resetErrorRowColor(row);
	}

	/**
	 * 增加行时检测。
	 * 
	 * 创建日期：(2001-6-23 14:06:13)
	 * 
	 * @return boolean
	 * 
	 */
	private boolean checkAddLine() {

		// 无表体行
		if (getBillCardPanel().getRowCount() == 0) {
			String strCustomerID = getBillCardPanel()
					.getHeadItem("ccustomerid").getValue();
			if (strCustomerID == null) {
				showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"40060301", "UPP40060301-000180")/* @res "客户不能为空。" */);
				return false;
			}
			String strCurrTypeID = getBillCardPanel().getHeadItem(
					"ccurrencytypeid").getValue();
			if (strCurrTypeID == null) {
				showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"40060301", "UPP40060301-000181")/* @res "原币不能为空。" */);
				return false;
			}
		}
		// 单据限行
		if (SO_01 != null) {
			if (SO_01.intValue() != 0) {
				if (SO_01.intValue() < getBillCardPanel().getRowCount()+1) {
					showErrorMessage(nc.ui.ml.NCLangRes.getInstance()
							.getStrByID("40060301", "UPP40060301-000171", null,
									new String[] { SO_01.intValue() + "" }));
					// 单据限SO_01.intValue()行
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * 在表体增行时设置表体项的可编辑性。
	 * 
	 * yb 创建日期：(2003-9-16 10:20:12)
	 * 
	 */
	protected void setDefaultCellEditable(int irow) {
		if (irow < 0
				|| irow > getBillCardPanel().getBillModel().getRowCount() - 1)
			return;
		// 设置辅单位不可编辑
		getBillCardPanel().setCellEditable(irow, "cpackunitname", false);
		// 设置批次不可编辑
		getBillCardPanel().setCellEditable(irow, "cbatchid", false);
		// 设置批次状态不可编辑
		getBillCardPanel().setCellEditable(irow, "fbatchstatus", false);
		// 设置渠道类型不可编辑
		getBillCardPanel().setCellEditable(irow, "cchantype", false);

	}
	
	/**
	 * 手工询价 v5.5 
	 */
	public void onFindPrice(){
		
		int[] selRows = getBillCardPanel().getBillTable().getSelectedRows();
		
		getBillCardPanel().findPrice(selRows, null,	false);
		
		ArrayList rowList  = new ArrayList();
		for (int i = 0;i<selRows.length;i++)
			rowList.add(new Integer(selRows[i]));
		
		//重新计算表头价税合计
		getBillCardPanel().getHeadItem("nheadsummny").setValue(
				getBillCardPanel().getTotalValue("noriginalcursummny"));
		
		nc.ui.scm.pub.panel.SetColor.resetColor(getBillCardPanel(), rowList); 
	}

	/**
	 * 删除行。 创建日期：(2001-4-13 16:38:18)
	 */
	public void onDelLine() {
		int[] irows = getBillCardPanel().getBillTable().getSelectedRows();
		if (irows == null || irows.length == 0)
			return;
		for (int i = 0; i < irows.length; i++) {
			if (isHasBackwardDoc(irows[i]))
				return;
			//add by ouyangzhb 2013-05-13 判断行是否存在码单锁定
			if(isHasMDWH(irows[i])){
				return ;
			}
			
		}
		// 存货
		int[] aryRows = getBillCardPanel().getBillTable().getSelectedRows();
		int[] inewdelline = getBillCardPanel()
				.setBlargebindLineWhenDelLine(aryRows,1);
		int[] aryRows1 = aryRows;
		if (inewdelline != null && inewdelline.length > 0) {
			aryRows = new int[aryRows1.length + inewdelline.length];
			for (int i = 0; i < aryRows1.length; i++) {
				aryRows[i] = aryRows1[i];
			}
			for (int i = aryRows1.length; i < aryRows1.length
					+ inewdelline.length; i++) {
				aryRows[i] = inewdelline[i - aryRows1.length];
			}
		}
		onDelLine(aryRows);

	}

	public void onDelLineCore(int[] aryRows){
		if (aryRows == null || aryRows.length == 0)
			return;

		boolean bisCalculate = getBillCardPanel().getBillModel()
				.isNeedCalculate();
		getBillCardPanel().getBillModel().setNeedCalculate(false);

		// 存货
		ArrayList dellist = new ArrayList();
		ArrayList dellistrow = new ArrayList();

		ArrayList invidlist = new ArrayList();
		String invid = null;
		for (int i = aryRows.length - 1; i >= 0; i--) {
			if (vRowATPStatus != null && vRowATPStatus.size() > aryRows[i]
					&& vRowATPStatus.size() > 0) {
				dellistrow.add(vRowATPStatus.get(aryRows[i]));
			}
			if (getBillCardPanel().alInvs != null
					&& aryRows[i] < getBillCardPanel().alInvs.size()
					&& getBillCardPanel().alInvs.size() > 0) {

				if (getBillCardPanel().alInvs.get(aryRows[i]) == null) {
					InvVO delinvvo = new InvVO();
					getBillCardPanel().alInvs.set(aryRows[i], delinvvo);
					dellist.add(delinvvo);
				} else {
					dellist.add(getBillCardPanel().alInvs.get(aryRows[i]));
				}
			}
			invid = getBillCardTools().getBodyStringValue(aryRows[i],
					"cinventoryid");
			if (invid != null && invid.trim().length() > 0) {
				invidlist.add(invid);
			}
		}
		for (int i = 0; i < dellist.size(); i++)
			getBillCardPanel().alInvs.remove(dellist.get(i));
		for (int i = 0; i < dellistrow.size(); i++)
			vRowATPStatus.remove(dellistrow.get(i));

		try {
			BillActionListener  bal = getBillCardPanel().getBodyPanel().getBillActionListener(); 
			getBillCardPanel().getBodyPanel().removeBillActionListener();
			getBillCardPanel().getBillModel().delLine(aryRows);
			getBillCardPanel().getBodyPanel().addBillActionListener(bal);
		} catch (Exception e) {
			SCMEnv.out(e);
		}

		//v5.6--询价逻辑不在使用
		// 寻价处理
/*		if (invidlist.size() > 0 && getBillCardPanel().getRowCount() > 0) {

			ArrayList rowlist = new ArrayList();
			for (int i = 0, loop = invidlist.size(); i < loop; i++) {
				for (int j = 0, loopj = getBillCardPanel().getRowCount(); j < loopj; j++) {
					if (invidlist.get(i).equals(
							getBillCardTools().getBodyStringValue(j,
									"cinventoryid"))) {
						rowlist.add(new Integer(j));
						break;
					}
				}
			}
			if (rowlist.size() > 0) {
				int[] findpricerows = new int[rowlist.size()];
				for (int i = 0, loop = rowlist.size(); i < loop; i++)
					findpricerows[i] = ((Integer) rowlist.get(i)).intValue();
				// getBillCardPanel().afterNumberEdit(findpricerows, "nnumber",
				// null, false, true);
			}
		}*/
		
		
		getBillCardPanel().getBillModel().setNeedCalculate(bisCalculate);
		if (getBillCardPanel().getHeadItem("nheadsummny") != null)
			getBillCardPanel().getHeadItem("nheadsummny").setValue(
					getBillCardPanel().getTotalValue("noriginalcursummny"));
	}
	
	/**
	 * 删除行。 创建日期：(2001-4-13 16:38:18)
	 */
	public void onDelLine(int[] aryRows) {

		onDelLineCore(aryRows);

		//重新设置自由项
		InvAttrCellRenderer ficr = new InvAttrCellRenderer();
		ficr.setFreeItemRenderer(getBillCardPanel(), getBillCardPanel().alInvs);

		updateUI();
	}

	/**
	 * 检查当前行是否生成下游单据
	 * 
	 * @param i
	 * @return
	 */
	protected boolean isHasBackwardDoc(int irow) {
		String sPk = (String) getBillCardPanel()
				.getBodyValueAt(irow, "csaleid");
		String sBodyPk = (String) getBillCardPanel().getBodyValueAt(irow,
				"corder_bid");
		if (sPk == null || sPk.trim().length() == 0 || sBodyPk == null
				|| sBodyPk.trim().length() == 0)
			return false;

		SaleOrderVO svo = vocache.getSaleOrderVO(sPk);

		if (svo == null)
			return false;

		SaleorderBVO[] bvos = svo.getBodyVOs();
		if (bvos == null || bvos.length == 0)
			return false;
		for (int i = 0; i < bvos.length; i++) {
			if (bvos[i].getCorder_bid().equals(sBodyPk)) {
				String[] sNames = { "ntotalreceivenumber",// 累计发货数量
						"ntotalinvoicenumber",// 累计开票数量
						"ntotalinventorynumber",// 累计出库数量
						"ntotalshouldoutnum",// 出库应发
						"ntotalbalancenumber",// 累计结算数量
						"ntotalreturnnumber",// 累计退货数量
						"narrangescornum",// 累计安排委外订单数量
						"narrangepoapplynum",// 累计安排请购单数量
						"narrangetoornum",// 累计安排调拨订单数量
						"norrangetoapplynum",// 累计安排调拨申请数量
						"narrangemonum"// 累计安排生产订单数量
				};
				for (int j = 0; j < sNames.length; j++) {
					if (bvos[i].getAttributeValue(sNames[j]) != null
							&& ((UFDouble) bvos[i].getAttributeValue(sNames[j]))
									.doubleValue() != 0)
						return true;
				}
				break;
			}
		}
		return false;

	}

	  public void setSortEnable(boolean isenable) {
		    if(!isenable){
		      getBillCardPanel().getBillTable().setSortEnabled(false);
		      getBillCardPanel().getBillTable().removeSortListener();
		    }else{
		      getBillCardPanel().getBillTable().setSortEnabled(true);
		      getBillCardPanel().getBillTable().addSortListener();
		    }
	  }
	
	  /**
	   * 
	   * 单据表体菜单动作监听.
	   * @param e ufbill.BillEditEvent
	   */
	  public boolean onEditAction(int action){
	    
	    boolean isSort = getBillCardPanel().getBillTable().isSortEnabled();
	    BillActionListener  bal = getBillCardPanel().getBodyPanel().getBillActionListener(); 
	    boolean isdo = false;
	    try {
	      if(bal!=null)
	        getBillCardPanel().getBodyPanel().removeBillActionListener();
	      if (isSort)
	        setSortEnable(false);
	      //getBillCardPanel().getBillModel().setNeedCalculate(false);
	    
	      switch(action){
	        case BillTableLineAction.ADDLINE:
	          onAddLine();
	          // 此处不能返回true，如果返回true，UAP会自动增加一空行
	          break;
	        case BillTableLineAction.DELLINE:
	          onDelLine();
	          isdo = true;
	          break;
	        /*
	         * 目前UAP的卡片编辑只支持增行和删行，因此上面只需要处理ADDLINE和DELLINE
	         * 为了避免出现死循环，则addLine()和delLine()方法中有removeListener的语句
	         * 如果日后需要支持下面的方法，则需要在各自相应的方法中去加上removeListener的语句
	        case BillTableLineAction.INSERTLINE:
		          onInsertLine();
		          break;  
	        case BillTableLineAction.COPYLINE:
	          onCopyLine();
	          break;
	        case BillTableLineAction.PASTELINE:
	          onPasteLine();
	          break;
	        case BillTableLineAction.PASTELINETOTAIL:
	          onPasteLineToTail();
	          break;
	        case BillTableLineAction.EDITLINE:
	          onCardEdit();
	          break;
			*/
	        default:
	        	isdo = true;
	      }
	    
	    } finally {
	      if(bal!=null)
	        getBillCardPanel().getBodyPanel().addBillActionListener(bal);
	      if (isSort)
	        setSortEnable(true);
	      //getBillCardPanel().getBillModel().setNeedCalculate(true);
	    }
	    
	    return isdo;
	  }
	
	/**
	 * 复制行。 创建日期：(2001-4-13 16:37:56)
	 */
	public void onCopyLine() {
		getBillCardPanel().copyLine();
		iCopyRowCount = 0;
		if (getBillCardPanel().getBillTable().getSelectedRow() > -1) {
			int[] indexRows = getBillCardPanel().getBillTable()
					.getSelectedRows();
			oCopy = new Object[indexRows.length];
			if (getBillCardPanel().alInvs != null) {
				for (int i = 0; i < oCopy.length; i++) {
					oCopy[i] = getBillCardPanel().alInvs.get(indexRows[i]);
				}
			}
			iCopyRowCount = getBillCardPanel().getBillTable()
					.getSelectedRowCount();
		}
	}

	public void onPasteLine() {
		onPasteLine(true);
	}
	
	/**
	 * 粘贴行。 创建日期：(2001-4-13 16:37:56)
	 * setErrRowColor -- 是否触发修改行颜色逻辑
	 */
	public void onPasteLine(boolean setErrRowColor) {
		
	    //创建时间工具类实例
		nc.vo.scm.pu.Timer timer = new nc.vo.scm.pu.Timer();
		timer.start();
		
		boolean bisCalculate = getBillCardPanel().getBillModel().isNeedCalculate();
		getBillCardPanel().getBillModel().setNeedCalculate(false);
		
		if (checkPasteLine()) {
			// 取得粘贴前行数
			int iBefore = getBillCardPanel().getRowCount();
			int rowOld = getBillCardPanel().getBillTable().getSelectedRow();
			if (rowOld < 0)
				return;
			getBillCardPanel().pasteLine();
			timer.addExecutePhase(">>>>>>>>>销售订单[粘贴行--单据模板粘贴行]用时：>>>>>>>>>>>");

			int rowNew = getBillCardPanel().getBillTable().getSelectedRow();
			boolean boriginalcurprice = false;
			boolean boriginalcurtaxprice = false;
			try {
				int colCount = getBillCardPanel().getBillTable().getColumnCount();
				String originalcurpriceName = getBillCardPanel().getBodyItem("table", "noriginalcurprice").getName();
				String originalcurtaxpriceName = getBillCardPanel().getBodyItem("table", "noriginalcurtaxprice").getName();
				for (int i = 0; i < colCount; i++) {
					if (getBillCardPanel().getBillTable().getColumnName(i).equals(originalcurpriceName)) {
						boriginalcurprice = getBillCardPanel().getBillTable("table").isCellEditable(rowNew, i);
					}
					else if (getBillCardPanel().getBillTable().getColumnName(i).equals(originalcurtaxpriceName)) {
						boriginalcurtaxprice = getBillCardPanel().getBillTable("table").isCellEditable(rowNew, i);
					}
				}
			}
			catch (Exception e) {
			}
			
			 //    粘贴行的时候手工修改过价格的行变色 
		      ArrayList<Integer> aryrow = new ArrayList<Integer>();
			for (int i = rowOld; i < rowNew; i++) {
				getBillCardPanel().setCellEditable(i, "noriginalcurprice", boriginalcurprice);
				getBillCardPanel().setCellEditable(i, "noriginalcurtaxprice", boriginalcurtaxprice);
				
				// 清空来源项
				setItemsValue(getNeedSetNullItemsWhenCopy(), i, null);
				vRowATPStatus.insertElementAt(new UFBoolean(false), i);
				// 自由项粘贴
				if (getBillCardPanel().alInvs != null) {
					int indexCopy = i - rowOld;
					getBillCardPanel().alInvs.add(i, oCopy[indexCopy]);
				}
				
				if(getBillCardPanel().ifModifyPrice(i))
		            aryrow.add(i);
			}
			
			if (setErrRowColor)
				nc.ui.scm.pub.panel.SetColor.setErrRowColor(getBillCardPanel(), aryrow);
			timer.addExecutePhase(">>>>>>>>>销售订单[粘贴行--手工修改过价格的行变色]用时：>>>>>>>>>>>");
			
/*			if (vRowATPStatus.size() != getBillCardPanel().getRowCount()) {
				SCMEnv.out(getBillCardPanel().getRowCount() + "");
			}*/
			
			
			// 计量单位编辑状态处理
			setAssistUnit(rowOld, rowNew);
			timer.addExecutePhase(">>>>>>>>>销售订单[粘贴行--设置单位信息]用时：>>>>>>>>>>>");
			
			// 批次
			setBatch(rowOld, rowNew);
			timer.addExecutePhase(">>>>>>>>>销售订单[粘贴行--设置批次]用时：>>>>>>>>>>>");
			
			// 取得粘贴后行数
			int iAfter = getBillCardPanel().getRowCount();
			// 计算粘贴行数
			int iRow = iAfter - iBefore;
			if (iBefore > 0 && iAfter > 0 && iRow > 0) {
				BillRowNo.pasteLineRowNo(getBillCardPanel(), getBillType(),
						getRowNoItemKey(), iRow);
			}
			timer.addExecutePhase(">>>>>>>>>销售订单[粘贴行--设置行号]用时：>>>>>>>>>>>");

			//关联合同
			getBillCardPanel().setDefaultCtItem(rowOld,iRow,true);
			timer.addExecutePhase(">>>>>>>>>销售订单[粘贴行--关联合同]用时：>>>>>>>>>>>");
			
			// 设置捆绑和买赠件
			getBillCardPanel().setBindAndLargeWhenPaste(rowOld, rowNew);
			timer.addExecutePhase(">>>>>>>>>销售订单[粘贴行--设置捆绑和买赠件]用时：>>>>>>>>>>>");
			
			getBillCardPanel().getBillModel().setNeedCalculate(bisCalculate);
			if (getBillCardPanel().getHeadItem("nheadsummny") != null)
				getBillCardPanel().getHeadItem("nheadsummny").setValue(
						getBillCardPanel().getTotalValue("noriginalcursummny"));
			timer.addExecutePhase(">>>>>>>>>销售订单[粘贴行--计算合计]用时：>>>>>>>>>>>");
			
			//输出各个业务操作占用时间分布
			timer.showAllExecutePhase(">>>>>>>>>销售订单[粘贴行]结束：>>>>>>>>>>>");

		}
	}

	/**
	 * 粘贴加行时检测。
	 * 
	 * 创建日期：(2001-6-23 14:06:13)
	 * 
	 * @return boolean
	 * 
	 */
	private boolean checkPasteLine() {
		// 无表体行
		if (getBillCardPanel().getRowCount() == 0) {
			String strCustomerID = getBillCardPanel()
					.getHeadItem("ccustomerid").getValue();
			if (strCustomerID == null) {
				showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"40060301", "UPP40060301-000180")/* @res "客户不能为空。" */);
				return false;
			}
			String strCurrTypeID = getBillCardPanel().getHeadItem(
					"ccurrencytypeid").getValue();
			if (strCurrTypeID == null) {
				showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"40060301", "UPP40060301-000181")/* @res "原币不能为空。" */);
				return false;
			}
		}
		// 单据限行
		if (SO_01 != null) {
			if (SO_01.intValue() != 0) {
				if (SO_01.intValue() < getBillCardPanel().getRowCount()
						+ iCopyRowCount) {
					showErrorMessage(nc.ui.ml.NCLangRes.getInstance()
							.getStrByID("40060301", "UPP40060301-000171", null,
									new String[] { SO_01.intValue() + "" }));
					// showErrorMessage("单据限" + CommonConstant.BEGIN_MARK
					// + SO_01.intValue() + CommonConstant.END_MARK + "行。");
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * 得到复制行时，需要清空的项
	 * 
	 * wsy 2005-10-10
	 * 
	 */
	protected String[] getNeedSetNullItemsWhenCopy() {

		return new String[] {

				// v30 so_saleorder_b
//				"tconsigntime", // 发货时间
//				"tdelivertime",// 到货时间
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
				"ntotalreturnnumber",// 累计退货数量
				"ntranslossnum",// 运输货损数量
				"biftransfinish",// 运输结束标志
				"dlastconsigdate",// 最近一次发货日期
				"dlasttransdate",// 最近一次运输日期
				"dlastoutdate",// 最近一次出库日期
				"dlastinvoicedt",// 最近一次开票日期
				"dlastpaydate",// 最近一次回款日期

				"bifinventoryfinish", "bifinvoicefinish", "bifpaybalance",
				"bifreceivefinish", "bifpaybalance", "bifpayfinish",
				"bifpaysign", "biftransfinish",

				"barrangedflag", "narrangemonum", "narrangepoapplynum",
				"narrangescornum", "narrangetoornum", "norrangetoapplynum",
				"narrangepoordernum",

				"carrangepersonid", "tlastarrangetime",

				"cfreezeid", "corder_bid",

				"ntotalbalancenumber", "ntotalinventorynumber",
				"ntotalinvoicenumber", "ntotalreceivenumber",
				"ntotalsignnumber","narrangetotolenum","arrangenum",

				"ntotalcostmny", "ntotalinvoicemny", "ntotalpaymny",
				"ntotalshouldoutnum", "ntotlbalcostnum", "nrushnum",

				"ts", "bindpricetype" };

	}

	private void setItemsValue(String[] sItemNames, int irow, Object value) {
		for (int i = 0; i < sItemNames.length; i++) {
			getBillCardPanel().setBodyValueAt(value, irow, sItemNames[i]);
		}
	}

	/**
	 * 是否辅计量。 创建日期：(2001-11-30 15:20:14)
	 * 
	 * @param row
	 *            int
	 */
	private void setAssistUnit(int rowOld, int rowNew) {
		for (int row = rowOld; row < rowNew; row++) {
			getBillCardPanel().setAssistUnit(row);
		}
	}

	/**
	 * 批次控制
	 * 
	 * 创建日期：(2001-11-30 15:20:14)
	 * 
	 * @param row
	 *            int
	 * 
	 */
	private void setBatch(int rowOld, int rowNew) {
		for (int row = rowOld; row < rowNew; row++) {
			// 批次控制:改到单据摸版内项目：wholemanaflag 控制
			Object temp = getBillCardPanel().getBodyValueAt(row,
					"wholemanaflag");
			boolean wholemanaflag = (temp == null ? false : new UFBoolean(temp
					.toString()).booleanValue());
			getBillCardPanel().setCellEditable(row, "fbatchstatus",
					wholemanaflag);
			getBillCardPanel().setCellEditable(row, "cbatchid", wholemanaflag);
		}
	}

	/**
	 * 粘贴行。 创建日期：(2001-4-13 16:37:56)
	 */
	public void onPasteLineToTail() {
		
	    //创建时间工具类实例
		nc.vo.scm.pu.Timer timer = new nc.vo.scm.pu.Timer();
		timer.start();
		
		if (iCopyRowCount <= 0)
			return;
		
		boolean bisCalculate = getBillCardPanel().getBillModel().isNeedCalculate();
        getBillCardPanel().getBillModel().setNeedCalculate(false);

		if (checkPasteLine()) {
			// 取得粘贴前行数
			int iBefore = getBillCardPanel().getRowCount();
			getBillCardPanel().pasteLineToTail();
			timer.addExecutePhase(">>>>>>>>>销售订单[粘贴行到表尾--单据模板粘贴行]用时：>>>>>>>>>>>");

			// 取得粘贴后行数
			int iAfter = getBillCardPanel().getRowCount();
			
			 // 粘贴行的时候手工修改过价格的行变色 
		      ArrayList<Integer> aryrow = new ArrayList<Integer>();
			for (int i = iBefore; i < iAfter; i++) {
				setItemsValue(getNeedSetNullItemsWhenCopy(), i, null);
				vRowATPStatus.insertElementAt(new UFBoolean(false), i);
				// 自由项粘贴
				if (getBillCardPanel().alInvs != null) {
					int indexCopy = i - iBefore;
					getBillCardPanel().alInvs.add(i, oCopy[indexCopy]);
				}
				if(getBillCardPanel().ifModifyPrice(i))
		            aryrow.add(i);
			}
			nc.ui.scm.pub.panel.SetColor.setErrRowColor(getBillCardPanel(), aryrow);
			timer.addExecutePhase(">>>>>>>>>销售订单[粘贴行到表尾--手工修改过价格的行变色]用时：>>>>>>>>>>>");
			
			//？？？打印出来的意图
/*			if (vRowATPStatus.size() != getBillCardPanel().getRowCount()) {
				SCMEnv.out(getBillCardPanel().getRowCount() + "");
			}*/
			
			// 计量单位编辑状态处理
			setAssistUnit(iBefore, iAfter);
			timer.addExecutePhase(">>>>>>>>>销售订单[粘贴行到表尾--设置单位信息]用时：>>>>>>>>>>>");
			
			// 批次
			setBatch(iBefore, iAfter);
			timer.addExecutePhase(">>>>>>>>>销售订单[粘贴行到表尾--设置批次]用时：>>>>>>>>>>>");

			// 计算粘贴行数
			int iRow = iAfter - iBefore;
			if (iBefore > 0 && iAfter > 0 && iRow > 0) {
				// 计算并设置新增行号
				BillRowNo.addLineRowNos(getBillCardPanel(), getBillType(),
						getRowNoItemKey(), iRow);
			}
			timer.addExecutePhase(">>>>>>>>>销售订单[粘贴行到表尾--设置行号]用时：>>>>>>>>>>>");
			
			//关联合同
			getBillCardPanel().setDefaultCtItem(iBefore,iRow,true);
			timer.addExecutePhase(">>>>>>>>>销售订单[粘贴行到表尾--关联合同]用时：>>>>>>>>>>>");
			
			// 设置捆绑和买赠件
			getBillCardPanel().setBindAndLargeWhenPaste(iBefore, iAfter);
			timer.addExecutePhase(">>>>>>>>>销售订单[粘贴行到表尾--设置捆绑和买赠件]用时：>>>>>>>>>>>");
			
			getBillCardPanel().getBillModel().setNeedCalculate(bisCalculate);
			if (getBillCardPanel().getHeadItem("nheadsummny") != null)
				getBillCardPanel().getHeadItem("nheadsummny").setValue(
						getBillCardPanel().getTotalValue("noriginalcursummny"));
			timer.addExecutePhase(">>>>>>>>>销售订单[粘贴行到表尾--计算合计]用时：>>>>>>>>>>>");

		}
		
		//输出各个业务操作占用时间分布
		timer.showAllExecutePhase(">>>>>>>>>销售订单[粘贴行到表尾]结束：>>>>>>>>>>>");
	}

	public void updateButton(ButtonObject bo) {
		super.updateButton(bo);
	}

	public void freshOnhandnum(int row) {
		if (!m_bOnhandShowHidden)
			return;
		if (row < 0) {
			((IQueryOnHandInfoPanel) getPnlSouth(this)).showData(null);
			return;
		}
		OnHandRefreshVO onhandnumvo = getSelectedItemHandInfo();

		// 设置现存量查询条件：出货库存组织、出货存货、出货批次、出货仓库
		((IQueryOnHandInfoPanel) getPnlSouth(this)).showData(onhandnumvo);
	}

	protected UIPanel getPnlSouth(BillLineInfoListener listener) {

		if (m_pnlOnHand == null) {

			Object obj = null;
			try {
				obj = new QueryOnHandInfoPanel(listener, false);
			} catch (Exception e) {
				// 解耦接口的程序错误
				// e.printStackTrace();
				SCMEnv.out(e);
			}

			if (obj != null) {
				try {
					m_pnlOnHand = (UIPanel) ((IQueryOnHandInfoPanel) obj);
				} catch (Exception e) {
					SCMEnv.out(e);
					// e.printStackTrace();
				}
			}

		}
		return m_pnlOnHand;
	}

	public OnHandRefreshVO getSelectedItemHandInfo() {

		int row = getBillCardPanel().getBillTable().getSelectedRow();
		SaleOrderVO billVO = (SaleOrderVO) getVo();
		if (billVO == null || billVO.getChildrenVO() == null
				|| billVO.getChildrenVO().length == 0)
			return null;
		if (row < 0 || row >= billVO.getChildrenVO().length)
			return null;
		SaleorderBVO billitem = (billVO.getBodyVOs())[row];

		String[] saParam = new String[] { "ccorpid", "ccalbodyid", "cwhid",
				"cinvbasdoc", "cinvid", "castunitid", "invcode", "cspaceid",
				"vfree1", "vfree2", "vfree3", "vfree4", "vfree5" };
		String[] saOrg = new String[] { "pk_corp", "cadvisecalbodyid",
				"cbodywarehouseid", "cinvbasdocid", "cinventoryid",
				"cpackunitid", "cinventorycode", "cbatchid", "vfree1",
				"vfree2", "vfree3", "vfree4", "vfree5" };
		// 对参数进行判断
		if ((billitem.getAttributeValue("pk_corp") == null || billitem
				.getAttributeValue("pk_corp").toString().trim().length() == 0)
				|| ((billitem.getAttributeValue("cinventoryid") == null || billitem
						.getAttributeValue("cinventoryid").toString().trim()
						.length() == 0))) {
			return null;
		}
		for (int i = 0; i < saParam.length; i++) {
			m_voLineOnHand.setAttributeValue(saParam[i], billitem
					.getAttributeValue(saOrg[i]));
		}

		return m_voLineOnHand;
	}

	public IFuncExtend getFuncExtend() {
		if (m_funcExtend == null && !binitFuncExtend) {
			m_funcExtend = nc.ui.scm.extend.FuncExtendInfo
					.getFuncExtendInstance("30");
			binitFuncExtend = true;
		}
		return m_funcExtend;
	}

	public boolean addNullLine(int istartrow, int count) {

		// if (checkAddLine(count)) {
		if (count <= 0)
			return false;
		
		for (int i = 1; i <= count; i++) {
			
			getBillCardPanel().addLine();
			
			// 存货
			getBillCardPanel().alInvs.add(null);
			// 可用量状态
			vRowATPStatus.add(UFBoolean.FALSE);
			// yb add 03-09-16
			setDefaultCellEditable(istartrow + i);
		}
		// 默认数据
		// 计算并设置新增行号
		BillRowNo.addLineRowNos(getBillCardPanel(), getBillType(),
				getRowNoItemKey(), count);

		getBillCardPanel().setBodyDefaultData(istartrow + 1,istartrow + 1 + count);
		

		
		return true;

	}

	public boolean insertNullLine(int istartrow, int count) {

		if (count <= 0)
			return true;
		if (istartrow >= getBillCardPanel().getRowCount() - 1)
			return false;
		int no = istartrow + 1;
		getBillCardPanel().getBillTable().getSelectionModel()
				.setSelectionInterval(no, no);

		for (int i = 1; i <= count; i++) {
			getBillCardPanel().insertLine();
			// 存货
			if (no <= getBillCardPanel().alInvs.size())
				getBillCardPanel().alInvs.add(no, null);
			
			if (vRowATPStatus.size()>=no)
				// 可用量状态
				vRowATPStatus.insertElementAt(new UFBoolean(false), no);
			
			//TODO 不应该去设置原行的默认可编辑性  v5.5 去掉 
			// yb add 03-09-16
			//setDefaultCellEditable(no - 1);
			
			no++;
			getBillCardPanel().getBillTable().getSelectionModel()
					.setSelectionInterval(no, no);
		}

		// 默认数据
		// 计算并设置新增行号
		getBillCardPanel().getBillTable().getSelectionModel()
				.setSelectionInterval(no, no);
		BillRowNo.insertLineRowNos(getBillCardPanel(), getBillType(),
				getRowNoItemKey(), no, count);

		getBillCardPanel().setBodyDefaultData(istartrow + 1,
				istartrow + 1 + count);
		getBillCardPanel().getBillTable().getSelectionModel()
				.setSelectionInterval(istartrow, istartrow);

		return true;

	}

	/**
	 * 得到单据VO。 创建日期：(2001-6-23 9:47:36)
	 * 
	 * @return nc.vo.so.so001.SaleOrderVO
	 */
	public AggregatedValueObject getVo() {
		return getVO(false);
	}

	/**
	 * 得到单据VO。 创建日期：(2001-6-23 9:47:36)
	 * 
	 * @return nc.vo.so.so001.SaleOrderVO
	 */
	public AggregatedValueObject getVO(boolean isNeedRemoveDiscountInv) {
		SaleOrderVO saleorder = null;
		saleorder = (SaleOrderVO) getBillCardPanel().getBillValueVO(
				SaleOrderVO.class.getName(), SaleorderHVO.class.getName(),
				SaleorderBVO.class.getName());
		if (isNeedRemoveDiscountInv) {
			Vector vTemp = new Vector();
			SaleorderBVO[] itemVOs = (SaleorderBVO[]) saleorder.getChildrenVO();
			int indexSelected = getBillCardPanel().getBillTable()
					.getSelectedRow();

			for (int i = 0; i < itemVOs.length; i++) {
				// 若不是劳务类存货
				boolean notLabor = itemVOs[i].getLaborflag() == null
						|| !itemVOs[i].getLaborflag().booleanValue();
				// 若不是折扣类存货
				boolean notDiscount = itemVOs[i].getDiscountflag() == null
						|| !itemVOs[i].getDiscountflag().booleanValue();

				if (notLabor && notDiscount) {
					if (indexSelected > -1 && i == indexSelected) {
						// 如果
						vTemp.addElement(itemVOs[i]);
					}
				}
			}
			SaleorderBVO[] itemsNew = new SaleorderBVO[vTemp.size()];
			vTemp.copyInto(itemsNew);
			saleorder.setChildrenVO(itemsNew);
		}
		// 单位编码
		if (saleorder.getHeadVO().getPk_corp() == null
				|| saleorder.getHeadVO().getPk_corp().trim().length() <= 0)
			((SaleorderHVO) saleorder.getParentVO())
					.setPk_corp(getCorpPrimaryKey());
		// 单据类型
		((SaleorderHVO) saleorder.getParentVO())
				.setCreceipttype(SaleBillType.SaleOrder);
		// 审批人
		((SaleorderHVO) saleorder.getParentVO())
				.setCapproveid(getClientEnvironment().getUser().getPrimaryKey());
		// 收货地址
		UIRefPane vreceiveaddress = (UIRefPane) getBillCardPanel().getHeadItem(
				"vreceiveaddress").getComponent();
		((SaleorderHVO) saleorder.getParentVO())
				.setVreceiveaddress(vreceiveaddress.getUITextField().getText());

		return saleorder;
	}

	protected String getRowNoItemKey() {
		return getBillCardPanel().getRowNoItemKey();
	}

	// 获得查询对话框控件
	protected abstract SOBillQueryDlg getQueryDlg();

	// 获得查询对话框控件 跨公司专用
	protected abstract SOBillQueryDlg getQueryDlg(String pk_corp);

	public abstract InvokeEventProxy getPluginProxy();
	
	
	/**
	 * add by ouyangzhb 2013-05-13 判断表体行是否存在码单锁定行
	 * @param irow
	 * @return
	 */
	public boolean isHasMDWH(int irow){
		String sPk = (String) getBillCardPanel()
		.getBodyValueAt(irow, "csaleid");
		String sBodyPk = (String) getBillCardPanel().getBodyValueAt(irow,
		"corder_bid");
		if (sPk == null || sPk.trim().length() == 0 || sBodyPk == null
				|| sBodyPk.trim().length() == 0)
			return false;
		
		String sql = "select distinct d.xsddbt_pk  from nc_mdsd d where d.xsddbt_pk='"+sBodyPk+"' and nvl(d.dr,0)=0";
		IUAPQueryBS query1 = NCLocator.getInstance().lookup(IUAPQueryBS.class);
		Object o;
		try {
			o = query1.executeQuery(sql,new ColumnProcessor());
			if (o != null && !"".equals(o)) {
				showErrorMessage("第【"+ getBillCardPanel().getBodyValueAt(irow,"crowno")+ "】行已存在码单锁定明细，不能删行，请检查！");
				return true;
			}
		} catch (BusinessException e) {
			e.printStackTrace();
		}
		
		return false;
		
	}
	
	

}
