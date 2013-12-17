package nc.ui.reportquery.component;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Stack;
import java.util.Vector;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

import nc.bs.framework.common.NCLocator;
import nc.bs.framework.common.RuntimeEnv;
import nc.bs.logging.Logger;
import nc.itf.uap.queryengine.IColumnFormat;
import nc.itf.uap.queryengine.IEmbedCodeUtil;
import nc.ui.ml.NCLangRes;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UIMenuItem;
import nc.ui.pub.beans.UIPopupMenu;
import nc.ui.pub.beans.UIScrollPane;
import nc.ui.pub.beans.UITable;
import nc.ui.pub.beans.table.AttributiveCellTableModel;
import nc.ui.pub.beans.table.CellSpan;
import nc.ui.pub.beans.table.ColumnGroup;
import nc.ui.pub.beans.table.GroupableTableHeader;
import nc.ui.pub.beans.table.GroupableTableHeaderUI;
import nc.ui.pub.print.datastruct.PrintCellData;
import nc.ui.pub.querymodel.PeneRuleDlg;
import nc.ui.pub.querymodel.RotateCrossSetDlg;
import nc.ui.pub.querymodel.SimpleCrossSetDlg;
import nc.ui.pub.querymodel.StyleUtil;
import nc.ui.pub.querymodel.UIUtil;
import nc.ui.pub.style.Style;
import nc.ui.reportquery.component.table.AggrByRHTableModel;
import nc.ui.reportquery.component.table.AggregateDescriptor;
import nc.ui.reportquery.component.table.AggregateDlg;
import nc.ui.reportquery.component.table.AggregateTableModel;
import nc.ui.reportquery.component.table.ColFormatConstants;
import nc.ui.reportquery.component.table.ColFormatDelegate;
import nc.ui.reportquery.component.table.ColInfo;
import nc.ui.reportquery.component.table.CommonRptTableModel;
import nc.ui.reportquery.component.table.CrossVOUtil;
import nc.ui.reportquery.component.table.DSListenerEnable;
import nc.ui.reportquery.component.table.DsFootPrint;
import nc.ui.reportquery.component.table.FormatOutputUtil;
import nc.ui.reportquery.component.table.FormulaColDescriptor;
import nc.ui.reportquery.component.table.GainHeaderModel;
import nc.ui.reportquery.component.table.RQRowHeader;
import nc.ui.reportquery.component.table.RQTableCellRenderer;
import nc.ui.reportquery.component.table.RowHeaderInfo;
import nc.ui.reportquery.component.table.RptTableModel;
import nc.ui.reportquery.component.table.TableHeaderBuilder;
import nc.ui.reportquery.demo.QEBannerDialog;
import nc.vo.iuforeport.businessquery.QueryUtil;
import nc.vo.iuforeport.businessquery.SelectFldVO;
import nc.vo.jcom.bean.NCBeanUtils;
import nc.vo.pub.cquery.FldgroupVO;
import nc.vo.pub.dbbase.DataSetScrollEvent;
import nc.vo.pub.dbbase.DataSetScrollEventListener;
import nc.vo.pub.dbbase.PassiveLoadingEvent;
import nc.vo.pub.dbbase.QEDataSet;
import nc.vo.pub.dbbase.QEDataSetAfterProcessDescriptor;
import nc.vo.pub.dbbase.ScrollableDataSet;
import nc.vo.pub.headerlens.FmdPrintInfoNew;
import nc.vo.pub.headerlens.HeaderdetailVO;
import nc.vo.pub.headerlens.HeaderinfoVO;
import nc.vo.pub.querymodel.ConfigParamVO;
import nc.vo.pub.querymodel.CrossTable;
import nc.vo.pub.querymodel.DataformatVO;
import nc.vo.pub.querymodel.DatasetFormulaUtil;
import nc.vo.pub.querymodel.DatasetUtil;
import nc.vo.pub.querymodel.ModelUtil;
import nc.vo.pub.querymodel.ParamUtil;
import nc.vo.pub.querymodel.ParamVO;
import nc.vo.pub.querymodel.PenetrateRuleVO;
import nc.vo.pub.querymodel.QueryBaseVO;
import nc.vo.pub.querymodel.QueryConst;
import nc.vo.pub.querymodel.QueryModelDef;
import nc.vo.pub.querymodel.RotateCrossVO;
import nc.vo.pub.querymodel.SimpleCrossVO;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.hssf.util.Region;

import com.borland.dbswing.DBExceptionHandler;
import com.borland.dbswing.DBUtilities;
import com.borland.dbswing.TableFastStringRenderer;
import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.DataRow;
import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.DataSetException;
import com.borland.dx.dataset.NavigationEvent;
import com.borland.dx.dataset.NavigationListener;
import com.borland.dx.dataset.Variant;

/**
 * 表格控件
 * 
 * @author：阳雄 2003-8-15 9:50:02
 * @modifier:张志辉
 * 
 */
public class RQTable extends RQCompBase implements ListSelectionListener,
		NavigationListener, TableModelListener, TableConstants,
		DataSetScrollEventListener {
	private static final long serialVersionUID = 2112640212383299985L;

	private UITable m_dbTable = new UITable() {
		protected JTableHeader createDefaultTableHeader() {
			JTableHeader header = new GroupableTableHeader(columnModel);
			// 颜色
			header.setBackground(UIManager.getColor("TableHeader.background"));
			header.setForeground(Color.black);
			return header;
		}
	};

	// 绑定结果集中的列
	private ColInfo[] m_colInfos;

	// 动态列信息
	private ArrayList m_dynColInfo;

	// 所有列信息
	private ColInfo[] m_allColInfos;

	// 表头ID
	private java.lang.String m_headerId;

	// 算子
	private AggregateDescriptor m_aggregateDescriptor = null;

	// 查询结果集
	private DataSet m_ds;

	// 记录设置列格式之前的显示列数
	private int m_oldshowcolumn;

	// 列锁定标记
	private boolean isLockCol = false;

	private UIScrollPane m_scrollPane = new UIScrollPane();

	// 锁定列的表头
	private UITable lockColTable = null;

	// 右键菜单项
	protected UIMenuItem m_subTotalMenu = new UIMenuItem(nc.ui.ml.NCLangRes
			.getInstance().getStrByID("10241201", "UPP10241201-000069")/*
	 * @res
	 * "小计合计"
	 */);

	protected UIMenuItem m_rotateCrossMenu = new UIMenuItem(nc.ui.ml.NCLangRes
			.getInstance().getStrByID("10241201", "UPP10241201-000070")/*
	 * @res
	 * "交叉"
	 */);

	protected UIMenuItem m_penetrateMenu = new UIMenuItem(nc.ui.ml.NCLangRes
			.getInstance().getStrByID("10241201", "UPP10241201-000060")/*
	 * @res
	 * "穿透"
	 */);

	protected UIMenuItem m_backMenu = new UIMenuItem(nc.ui.ml.NCLangRes
			.getInstance().getStrByID("10241201", "UPP10241201-000071")/*
	 * @res
	 * "回退"
	 */);

	protected UIMenuItem m_lockColMenu = new UIMenuItem(nc.ui.ml.NCLangRes
			.getInstance().getStrByID("10241201", "UPP10241201-001507")/*
	 * @res
	 * "锁定"
	 */);

	protected UIMenuItem m_unlockColMenu = new UIMenuItem(nc.ui.ml.NCLangRes
			.getInstance().getStrByID("10241201", "UPP10241201-001508")/*
	 * @res
	 * "解锁"
	 */);

	// 浏览时锁定列
	private int m_iLockCol = -1;

	// 是否打印时锁定行
	private boolean lockRow = false;

	// 表类型：0正常表，1合计表
	private int m_tableType = NORMAL;

	// //产生树表工具
	// private CreateTreeTableTool cttt = null;
	// 行表头信息（设了行表头的情况不能树表展现）
	private RowHeaderInfo m_rowHeaderInfo = new RowHeaderInfo();

	// 表行头
	private RQRowHeader m_rowHeader;

	//
	private boolean m_ignoreNavigation;

	// 记录数据集信息
	private Stack m_actionStack = new Stack();

	// 判断交叉类型
	private transient int m_crossType = QueryConst.CROSSTYPE_NONE;

	// 当前数据集信息
	private DsFootPrint m_curDsFp;

	// 监听数据集消息标志
	private DSListenerEnable m_enableListen;

	// 定义态交叉设置
	private RotateCrossVO defRotateCrossVO;

	// 实际上定义态交叉设置的一个克隆,用以保存设计态的交叉设置(defRotateCrossVO在setID()里面被设成null了)
	private RotateCrossVO defRotateCrossVO_inDlg;

	private SimpleCrossVO[] defSimpleCrossVO;

	private SimpleCrossVO[] defSimpleCrossVO_inDlg;

	// 原始字段哈希表(zjb+)
	private Hashtable m_hashOriginAlias = new Hashtable();

	// 字段别名和列格式信息哈希表(jl+)
	private Hashtable htColAliasColInfo = new Hashtable();

	private PageController pageController = null;

	private ScrollableDataSet scrollableDataSet = null;

	// 页面索引
	private int pageIndex = 1;
	
	//wanglei 2013-12-16 穿透返回到选择行
	private int iPenetrateRow = 0;
	private HashMap hm = new HashMap();

	// 分页事件控制器
	class PageEventHandler implements ActionListener, KeyListener {

		public void actionPerformed(ActionEvent e) {
			if (PageController.COMMAND_NEXTPAGE.equalsIgnoreCase(e
					.getActionCommand())) {
				nextpage();
			} else if (PageController.COMMAND_PREVIOUSPAGE.equalsIgnoreCase(e
					.getActionCommand())) {
				previouspage();
			} else if (PageController.COMMAND_FIRSTPAGE.equalsIgnoreCase(e
					.getActionCommand())) {
				firstpage();
			} else if (PageController.COMMAND_LASTPAGE.equalsIgnoreCase(e
					.getActionCommand())) {
				lastpage();
			}
		}

		private void lastpage() {
			if (pageIndex < getPageController().getTotalPageCount()) {
				getScrollableDataSet().absoluteDataSet(
						getPageController().getTotalPageCount() - 1);
				pageIndex = getPageController().getTotalPageCount();
			}
		}

		private void firstpage() {
			getScrollableDataSet().absoluteDataSet(0);
			pageIndex = 1;
		}

		private void previouspage() {
			if (pageIndex > 1) {
				getScrollableDataSet().previousDataSet();
			}
		}

		private void nextpage() {
			if (pageIndex < getPageController().getTotalPageCount()) {
				getScrollableDataSet().nextDataSet();
			}
		}

		// 计算公式列
		private void calculateFormula(QEDataSet ds) {
			if (getTopPanel().getHtFormulaCols().containsKey(getID())) {
				FormulaColDescriptor[] fcds = (FormulaColDescriptor[]) getTopPanel()
						.getHtFormulaCols().get(getID());
				for (int fnum = 0; fnum < fcds.length; fnum++) {
					// jl+ 结果集为空或者结果集行数为0就不计算公式列了
					if (ds != null
							&& ds.getRowCount() > 0
							&& !DatasetUtil.containColumn(ds, fcds[fnum]
									.getColAlias())) {
						if (!DatasetFormulaUtil
								.addFormulaCol2DS(ds, fcds[fnum])) {
							MessageDialog.showWarningDlg(RQTable.this,
									nc.ui.ml.NCLangRes.getInstance()
											.getStrByID("10241201",
													"UPP10241201-000066")/*
									 * @res
									 * "警告"
									 */, nc.ui.ml.NCLangRes.getInstance()
											.getStrByID("10241201",
													"UPP10241201-000251")/*
									 * @res
									 * "公式列计算错误:"
									 */
											+ fcds[fnum].getColName());
						}
					}
				}
			}
		}

		public void keyPressed(KeyEvent e) {
			// 用户按下ENTER键事件
			if (e.getKeyCode() == KeyEvent.VK_ENTER) {
				if (pageIndex >= 1
						&& pageIndex <= getPageController().getTotalPageCount()) {
					getPageController().refreshDisplay();
					/* QEDataSet ds = (QEDataSet) */getScrollableDataSet()
							.absoluteDataSet(
									getPageController().getCurrentPageCount() - 1);
				}
			}
		}

		public void keyReleased(KeyEvent e) {

		}

		public void keyTyped(KeyEvent e) {

		}
	}

	// 分页事件处理器
	private PageEventHandler pageEventHandler = new PageEventHandler();

	/**
	 * RQTable 构造子注解。
	 */
	// public RQTable() {
	// this(true);
	// }
	/**
	 * RQTable 构造子注解。
	 * 
	 * @param designState
	 *            boolean
	 */
	public RQTable(boolean designState) {
		super(designState);
		init();
	}

	/**
	 * getActComponent 方法注解。
	 * 
	 * @return JComponent
	 */
	public javax.swing.JComponent getActComponent() {
		return m_dbTable;
	}

	/**
	 * 获取标签控件。 创建日期：(2003-6-8 18:21:19)
	 * 
	 * @return javax.swing.JComponent
	 */
	public javax.swing.JLabel getLabelComponent() {
		return null;
	}

	/**
	 * getValue 方法注解。
	 * 
	 * @return Object
	 */
	public Object getValue() {
		return null;
	}

	/**
	 * 初始化控件 创建日期：（2003-8-12 13:22:20）
	 */
	protected void init() {
		compType = COMPLEXCOMP;
		this.add(m_scrollPane, BorderLayout.CENTER);
		m_scrollPane.setViewportView(m_dbTable);
		initPopupMenu();
		m_rowHeader = new RQRowHeader();
		if (isDesignState()) {
			m_dbTable.setEnabled(false);
			addSubCompMouseListener(this);
		} else {
			m_dbTable.getSelectionModel().addListSelectionListener(this);
			m_dbTable.getSelectionModel().setSelectionMode(
					ListSelectionModel.SINGLE_SELECTION);
		}
		m_dbTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		m_dbTable.getTableHeader().resizeAndRepaint();
		//wanglei 2013-12-17
		m_dbTable.setSelectionBackground(Color.yellow);

		MouseAdapter adapter = new MouseAdapter() {
			public void mouseReleased(java.awt.event.MouseEvent e) {
				if (e.isPopupTrigger()) {
					// 在表体的时候锁定菜单不可用
					int compCount = m_dbTable.getHeaderPopupMenu()
							.getComponentCount();
					UIMenuItem lockMenu = (UIMenuItem) m_dbTable
							.getHeaderPopupMenu().getComponent(compCount - 1);
					if (e.getSource() == m_dbTable) {
						int row = m_dbTable.rowAtPoint(e.getPoint());
						m_dbTable.getSelectionModel().setSelectionInterval(row,
								row);
						lockMenu.setEnabled(false);
					} else {
						lockMenu.setEnabled(true);
					}
					m_dbTable.getHeaderPopupMenu().show(
							(java.awt.Component) e.getSource(), e.getX(),
							e.getY());
				}
			}
		};
		m_dbTable.getTableHeader().addMouseListener(adapter);
		m_dbTable.addMouseListener(adapter);
	}

	/**
	 * 获取控件属性描述。 创建日期：(2003-6-10 15:34:51)
	 * 
	 * @return RQCompDescriptor
	 */
	public RQCompDescriptor getCompDescriptor() {
		RQCompDescriptor desTable = super.getCompDescriptor();

		// 类型
		desTable.setAttribute(STR_CT_COMPONENT_TYPE, CJTable + "");
		// 表头ID
		desTable.setAttribute("headerId", getHeaderId());
		// 行表头信息
		desTable.setAttribute("rowHeader", m_rowHeaderInfo.getInfoString());
		// 列信息
		desTable.setAttribute("colInfo", getColInfoString());

		// 小计合计信息-用AggregateDescriptor来描述小计合计的信息
		desTable.setAttribute("tabletype", "" + m_tableType);

		desTable.setAttribute("lockcol", String.valueOf(m_iLockCol));
		if (m_aggregateDescriptor != null) {
			desTable.setAttribute("subtotal", m_aggregateDescriptor
					.getInfoString());

		}

		if (getDefRotateCrossVO() != null) {
			desTable.setAttribute("rotatecross", CrossVOUtil
					.rcVO2String(getDefRotateCrossVO()));
		}
		if (getDefSimpleCrossVO() != null) {
			desTable.setAttribute("simplecross", CrossVOUtil
					.scVOs2String(getDefSimpleCrossVO()));
		}
		// 打印锁定行
		desTable.setAttribute(STR_LOCKROW, isLockRow() ? "Y" : "N");
		return desTable;
	}

	/**
	 * InitPara 方法注解。
	 * 
	 * @param name
	 *            String
	 * @param value
	 *            String
	 */
	public void initPara(java.lang.String name, java.lang.String value) {
		if (name.equals(STR_TEXT)) {

		} else if (name.equals("ID")) {
			super.setID(value);
		} else if (name.equals("headerId")) {
			setHeaderId0(value);
		} else if (name.equals("rowHeader")) {
			m_rowHeaderInfo.parseString(value);
		} else if (name.equals("colInfo")) {
			parseColInfoString(value);
		} else if (name.equals("tabletype")) {
			m_tableType = Integer.parseInt(value);
			bakTableType = m_tableType;
		} else if (name.equals("subtotal")) {
			bakAggrInfo = value;
			m_aggregateDescriptor = AggregateDescriptor.parseString(value);
		} else if (name.equals("rotatecross")) {
			setDefRotateCrossVO(CrossVOUtil.parseString2RcVO(value));
		} else if (name.equals("simplecross")) {
			setDefSimpleCrossVO(CrossVOUtil.parseString2ScVOs(value));
		} else if (name.equals("lockcol")) {
			m_iLockCol = Integer.parseInt(value);
		} else if (name.equals(STR_LOCKROW)) {
			setLockRow(value.equalsIgnoreCase("Y"));
		} else {
			super.initPara(name, value);
		}
	}

	/**
	 * 得到列格式信息数组。 创建日期：(2003-9-25 9:35:27)
	 * 
	 * @return nc.reportquery.component.RQTable.ColInfo[]
	 */
	public ColInfo[] getColInfos() {
		if (isDesignState()) {
			refreshColsWidth();
			m_oldshowcolumn = getShownColCount();
		}
		return m_colInfos;
	}

	/**
	 * 得到列格式信息串。 创建日期：(2003-9-25 10:06:31)
	 * 
	 * @return java.lang.String
	 */
	public String getColInfoString() {
		if (isDesignState()) {
			refreshColsWidth();
		}
		if (m_colInfos != null && m_colInfos.length > 0) {
			StringBuffer strBuf = new StringBuffer();
			int colCount = m_colInfos.length;
			for (int i = 0; i < colCount; i++) {
				if (i != 0) {
					strBuf.append(';');
				}
				strBuf.append(m_colInfos[i].getInfoString());
			}
			return strBuf.toString();
		} else {
			return "";
		}
	}

	/**
	 * 得到绑定的表头ID。 创建日期：(2003-9-16 10:19:20)
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getHeaderId() {
		return m_headerId;
	}

	/**
	 * 解析列格式信息串。 创建日期：(2003-9-25 10:07:20)
	 * 
	 * @param colInfoString
	 *            java.lang.String
	 * @i18n upp08110600602=:解析列格式出错
	 */
	private void parseColInfoString(String colInfoString) {
		try {
			java.util.StringTokenizer st = new java.util.StringTokenizer(
					colInfoString, ";", false);
			int colCount = st.countTokens();
			m_colInfos = new ColInfo[colCount];
			for (int i = 0; i < colCount; i++) {
				m_colInfos[i] = new ColInfo();
				String infoStr = st.nextToken();

				m_colInfos[i].parseString(infoStr);
			}
		} catch (Exception e) {
			Logger.error(getName() + ":解析列格式出错", e);
			m_colInfos = null;
		}
	}

	/**
	 * 刷新表（表体列，表头等）。 创建日期：(2003-9-24 10:00:03)
	 */
	public void refresh() {
		try {
			if (isDesignState()) {
				refreshDesignState();
			} else {
				m_actionStack.clear();
				// 如果运行态，则绑定查询结果集
				if (getID() != null) {
					QueryModelDef qmd = getTopPanel().getQryDefByAlias(getID());
					// 如果设置了分页则加上分页控件
					ParamVO[] params = qmd.getParamVOs();
					ConfigParamVO configParamVO = ParamUtil
							.getConfigParam(params);
					if (configParamVO != null
							&& configParamVO.isPagable().booleanValue()
							&& configParamVO.getPagesize() > 0) {
						// 走分页分支
						// Hashtable dynamicHashParam = (Hashtable)
						// getTopPanel().getHtQryParams().get(getID());
						setScrollableDataSet((ScrollableDataSet) getTopPanel()
								.getHtScrollableDataSet().get(getID()));
						getPageController().removeActionListener();
						getPageController().removeKeyListener();
						getPageController().addActionListener(pageEventHandler);
						getPageController().addKeyListener(pageEventHandler);
						// 重置页面索引
						if (pageIndex != 1)
							pageIndex = 1;
						getPageController().setCurrentPageCount(pageIndex);
						int pageCount = (int) Math
								.ceil((float) getScrollableDataSet()
										.getTotalRowCount()
										/ getScrollableDataSet().getPageSize());
						getPageController().setTotalPageCount(pageCount);
						// jl+@20070320
						getScrollableDataSet().addDataSetScrollEventListener(
								this);
						this.add(getPageController(), BorderLayout.SOUTH);
					}
					// 普通查询
					QEDataSet sds = getTopPanel().getQueryDataSet(getID());
					doRefreshWithDataSet(qmd, sds);
				}

				// 锁定态刷新处理
				if (isLockCol) {
					lockColTable = null;
					isLockCol = false;
					// 刷新恢复为原始状态较安全，因不同参数设置可能导致不同列结构
					// lockCol(m_iLockCol);
					m_iLockCol = -1;
					m_lockColMenu.setName(nc.ui.ml.NCLangRes.getInstance()
							.getStrByID("10241201", "UPP10241201-001507")/*
					 * @res
					 * "锁定"
					 */);
					m_lockColMenu.setText(nc.ui.ml.NCLangRes.getInstance()
							.getStrByID("10241201", "UPP10241201-001507")/*
					 * @res
					 * "锁定"
					 */);
					refreshPopMenu();
				}

				// jl+ --简直像定时炸弹一样(不知道什么时候会犯错)
				if (m_iLockCol > -1) {
					lockCol(m_iLockCol);
				}
				m_scrollPane.revalidate();
			}
		} catch (Exception e) {
			Logger.error(e);
		}
	}

	// 执行以结果集为数据模型的更新
	private void doRefreshWithDataSet(QueryModelDef qmd, QEDataSet sds) {
		m_tableType = bakTableType;
		if (m_tableType == SUBTOTAL) {
			m_aggregateDescriptor = AggregateDescriptor
					.parseString(bakAggrInfo);
		}

		m_curDsFp = new DsFootPrint(sds, qmd);
		m_dsOrig = sds;
		ArrayList selectFldFormula = new ArrayList();
		// 记录原始字段哈希表(zjb+)
		if (qmd != null) {
			QueryBaseVO qb = qmd.getQueryBaseVO();
			if (qb != null) {
				SelectFldVO[] sfs = qb.getSelectFlds();
				if (sfs != null) {
					for (int i = 0; i < sfs.length; i++) {
						m_hashOriginAlias.put(sfs[i].getFldalias(), sfs[i]);
						// jl+ 20060508 增加NC公式的支持
						if (sfs[i].getExpression() != null
								&& sfs[i].getExpression().startsWith("'{")
								&& sfs[i].getExpression().endsWith("}'")) {
							selectFldFormula.add(sfs[i]);
						}
					}
					// 旋转交叉合计行(zjb)——浏览初始时才会进此分支
					if (qb.getRotateCross() != null
							&& m_aggregateDescriptor != null) {
						m_tableType = ROTATE_SUBTOTAL;
					}
				}
			}
		}
		SelectFldVO[] withFormula = (SelectFldVO[]) selectFldFormula
				.toArray(new SelectFldVO[selectFldFormula.size()]);
		handleSelectFldFormula(withFormula, sds);
		setDataset(sds);
		bindDataSet(true);
		getTopPanel().getHtQryDataSet().put(qmd.getDisplayName(), sds);
		m_crossType = ModelUtil.getCrossType(getTopPanel().getQryDefByAlias(
				getID()));
		if (m_crossType == QueryConst.CROSSTYPE_ROTATE) {
			DsFootPrint fp = new DsFootPrint(getTopPanel()
					.getQueryDataSetB4Cross(getID()), qmd);
			m_actionStack.push(fp);
			m_curDsFp.setFldGrpVOs(qmd.getFldgroups());
			m_curDsFp.setRotateCrossVO(qmd.getRotateCross());
			m_backMenu.setEnabled(true);
			// m_initRotated = true;
		} else if (m_crossType == QueryConst.CROSSTYPE_SIMPLE) {
			m_curDsFp.setSimpleCrossVOs(qmd.getQueryBaseVO().getScs());
		}

		// 添加动态列格式
		FormulaColDescriptor[] fcDsp = getTopPanel().getFormulaColByAlias(
				getRealID());
		if (fcDsp != null) {
			for (int i = 0; i < fcDsp.length; i++) {
				ColInfo colInfo = new ColInfo();
				colInfo.colType = QueryUtil.variantTypeToSqlType(fcDsp[i]
						.getColType());
				colInfo.colAlias = fcDsp[i].getColAlias();
				colInfo.colName = fcDsp[i].getColName();
				colInfo.colWidth = 80;
				colInfo.setColFormatID(fcDsp[i].getColFmtID());
				if (!getDynColInfo().contains(colInfo)) {
					getDynColInfo().add(colInfo);
				}
			}
		}
		// 初始化列和列头
		refreshColsAndHeader();
		// 初始化行头
		setRowHeaderInfo(m_rowHeaderInfo);

		// 如果预定义了交叉，执行一下
		if (getDefRotateCrossVO() != null
				&& !getDefRotateCrossVO().equals(qmd.getRotateCross())) {
			doRotateCross(false);
		} else if (getDefSimpleCrossVO() != null
				&& !Arrays.equals(getDefSimpleCrossVO(), qmd.getScs())) {
			doSimpleCross(false);
		}

		// 如果是第一类交叉，而且设了小计合计，需要通过重新绑定数据集
		// 因为这种情况的小计合计依赖行表头，前一次绑定数据集时行表头
		// 还没有生成
		if (m_crossType == QueryConst.CROSSTYPE_SIMPLE
				&& m_tableType == SUBTOTAL) {
			bindDataSet(false);
			updateRowHeader();
		}

		// 刷新菜单
		refreshPenetrateItem();

	}

	/**
	 * 刷新右键菜单 created on 2006-5-31 <strong>最后修改人: jl<strong> <strong>最后修改日期:
	 * 2006-5-31<stong>
	 */
	private void refreshPopMenu() {
		m_subTotalMenu.setEnabled(!isLockCol());
		m_rotateCrossMenu.setEnabled(!isLockCol());
		if (!isLockCol()) {
			if (getCurrentQMD().getPenetrateRules() != null
					&& getCurrentQMD().getPenetrateRules().length > 0) {
				m_penetrateMenu.setEnabled(true);
			}
		} else {
			m_penetrateMenu.setEnabled(!isLockCol());
		}
		// 判断当前刷新是否在最初数据集上
		boolean isFirst = m_actionStack.isEmpty();
		if (isFirst)
			m_backMenu.setEnabled(false);
		else
			m_backMenu.setEnabled(!isLockCol());
	}

	/**
	 * 处理在查询定义中定义过的NC公式 created on 2006-5-8
	 * 
	 * @param sfVOs
	 * @param dataset
	 *            <strong>最后修改人: jl<strong> <strong>最后修改日期: 2006-5-8<stong>
	 */
	private void handleSelectFldFormula(SelectFldVO[] sfVOs, QEDataSet dataset) {
		if (sfVOs == null || sfVOs.length == 0)
			return;
		try {
			int len=sfVOs.length;
			String[] formulas=new String[len];
			String[] colNames=new String[len];
			int[] colTypes=new int[len];
			for (int i = 0; i < sfVOs.length; i++) {
				int start = sfVOs[i].getExpression().indexOf("'{");
				int end = sfVOs[i].getExpression().indexOf("}'");
				formulas[i] = sfVOs[i].getExpression().substring(start + 2,
						end).trim();
				colNames[i] = sfVOs[i].getFldalias().toUpperCase();
				//FormulaParseFather.garbage()中用到公式名称
				if(formulas[i].indexOf("->")<0)
					formulas[i]=sfVOs[i].getFldalias()+"->"+formulas[i];
				// 有该列才进行操作
				if (dataset.findOrdinal( colNames[i]) >= 0) {
					// 数据类型
					colTypes[i] = dataset.getColumn(colNames[i]).getDataType();

					Column col = dataset.getColumn( colNames[i]);
					int oldordinal = col.getOrdinal();
					dataset.dropColumn(col);
					dataset.addColumn(col);
					col.setPrecision(-1);
					int newordinal = dataset.getColumn( colNames[i]).getOrdinal();
					dataset.moveColumn(newordinal, oldordinal);
				}
			}
			DatasetFormulaUtil.calColVal(dataset, colNames, colTypes,
					formulas,null);
		} catch (DataSetException e) {
			Logger.error(e);
		} catch (Exception e) {
			Logger.error(e);
		}
	}

	/**
	 * (只有在设计态下属性编辑器更改属性时才应调用该方法) 创建日期：(2003-9-25 9:35:27)
	 * 
	 * @param newColInfos
	 *            nc.reportquery.component.RQTable.ColInfo[]
	 */
	public void setColInfos(ColInfo[] newColInfos) {
		m_colInfos = newColInfos;
		m_allColInfos = null;
		if (isDesignState() && m_colInfos != null) {
			// 更新列显示
			HashMap hmCol = new HashMap();
			TableColumnModel colModel = m_dbTable.getColumnModel();

			int colCount = colModel.getColumnCount();
			for (int i = 0; i < colCount; i++) {
				TableColumn tc = colModel.getColumn(i);
				if (tc.getIdentifier() != null) {
					hmCol.put(tc.getIdentifier(), tc);
				}
			}

			colCount = m_colInfos.length;
			for (int i = 0; i < colCount; i++) {
				TableColumn tc = (TableColumn) hmCol
						.get(m_colInfos[i].colAlias);
				if (tc != null) {
					if (!m_colInfos[i].isShown) {
						colModel.removeColumn(tc);
						hmCol.remove(tc.getIdentifier());
						continue;
					}
					tc.setHeaderValue(m_colInfos[i].colName);
					tc.setWidth(m_colInfos[i].colWidth);
					tc.setPreferredWidth(m_colInfos[i].colWidth);
					int origIndex = colModel.getColumnIndex(tc.getIdentifier());
					colModel.moveColumn(origIndex, getShownColIndex(
							getAllColInfos(), i));
				} else {
					if (m_colInfos[i].isShown) {
						tc = new TableColumn();
						tc.setIdentifier(m_colInfos[i].colAlias);
						tc.setHeaderValue(m_colInfos[i].colName);
						tc.setWidth(m_colInfos[i].colWidth);
						tc.setPreferredWidth(m_colInfos[i].colWidth);
						colModel.addColumn(tc);
						int origIndex = colModel.getColumnIndex(tc
								.getIdentifier());
						colModel.moveColumn(origIndex, getShownColIndex(
								getAllColInfos(), i));
						hmCol.put(tc.getIdentifier(), tc);
					}
				}
			}
			// m_dbTable.sizeColumnsToFit(-1);
			// 列数发生变化，清除多表头
			if (m_oldshowcolumn != getShownColCount()) {
				setHeaderId(null);
			} else {
				setHeaderId(getHeaderId());
			}
			// m_dbTable.getTableHeader().resizeAndRepaint();
			m_dbTable.revalidate();
		}
	}

	/**
	 * (只有在设计态下属性编辑器更改属性时才应调用该方法) 设置绑定的查询结果集的ID 绑定结果集时调用该方法
	 * 
	 * @param newID
	 *            String
	 */
	public void setID(java.lang.String newID) {
		String oldID = getID();
		super.setID(newID);
		// 如果是设计态，则获得查询结果集中的列，以进行设置
		if (isDesignState()) {
			if (getID() == null) {
				m_crossType = QueryConst.CROSSTYPE_NONE;
				setDefRotateCrossVO_inDlg(null);
				setDefSimpleCrossVO_inDlg(null);
				setDefRotateCrossVO(null);
				setDefSimpleCrossVO(null);
				setHeaderId(null);
				m_dbTable.setModel(new DefaultTableModel(0, 0));
				// rowHeader.clearRowGroups();
				m_colInfos = new ColInfo[0];
				refreshRowHeader(new RowHeaderInfo());
				m_scrollPane.revalidate();
				return;
			}
			try {
				m_crossType = ModelUtil.getCrossType(getTopPanel()
						.getQryDefByAlias(getID()));

				QueryModelDef qmd = getTopPanel().getQryDefByAlias(newID);
				SelectFldVO[] fldVOs = ModelUtil.getSelectFlds(qmd);
				if (fldVOs == null) {
					return;
				}
				setDefRotateCrossVO_inDlg(getDefRotateCrossVO() == null ? null
						: (RotateCrossVO) getDefRotateCrossVO().clone());
				int iLen = getDefSimpleCrossVO() == null ? 0
						: getDefSimpleCrossVO().length;
				SimpleCrossVO[] scVO = new SimpleCrossVO[iLen];
				for (int i = 0; i < iLen; i++) {
					scVO[i] = getDefSimpleCrossVO() == null ? null
							: (SimpleCrossVO) getDefSimpleCrossVO()[i].clone();
				}
				setDefSimpleCrossVO_inDlg(scVO);
				// 04.12.23+
				setDefSimpleCrossVO(qmd.getScs());
				setDefRotateCrossVO(qmd.getRotateCross());

				// 设置表的ColumnModel，仅显示用（土得不能再土的办法）
				DefaultTableColumnModel colModel = new DefaultTableColumnModel();
				int colCount = fldVOs.length;

				// 获得初始列宽(V5)
				Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
				double tableWidth = dim.getWidth(); // getSize().getWidth();
				int iColWidth = (colCount <= 0) ? 0 : (int) tableWidth
						/ colCount - 10;
				if (iColWidth < 100) {
					iColWidth = 100;
				}

				m_colInfos = new ColInfo[colCount];
				for (int i = 0; i < colCount; i++) {
					m_colInfos[i] = new ColInfo();

					// 设置一下初始列宽(V5)
					m_colInfos[i].colWidth = iColWidth;

					m_colInfos[i].colType = (fldVOs[i].getColtype() == null) ? java.sql.Types.VARCHAR
							: fldVOs[i].getColtype().intValue();
					m_colInfos[i].colAlias = fldVOs[i].getFldalias()
							.toUpperCase();
					m_colInfos[i].colName = fldVOs[i].getFldname();
					TableColumn tc = new TableColumn(i);
					tc.setIdentifier(m_colInfos[i].colAlias);
					tc.setWidth(m_colInfos[i].colWidth);
					tc.setPreferredWidth(m_colInfos[i].colWidth);
					tc.setHeaderValue(m_colInfos[i].colName);
					colModel.addColumn(tc);
				}
				m_dbTable.setColumnModel(colModel);
				// m_dbTable.sizeColumnsToFit(-1);

				if (!newID.equals(oldID)) {
					setHeaderId(null);
					refreshRowHeader(new RowHeaderInfo());
				}
				m_scrollPane.revalidate();
			} catch (Exception e) {
				Logger
						.error(
								"查询结果集的列信息出错——nc.reportquery.component.RQTable.setID(String)",
								e);
			}
		}
	}

	/**
	 * 得到显示列数（设计态用）。 创建日期：(2003-9-28 18:54:47)
	 * 
	 * @return int
	 */
	public int getShownColCount() {
		int count = 0;
		if (m_colInfos != null) {
			for (int i = m_colInfos.length - 1; i >= 0; i--) {
				if (m_colInfos[i].isShown) {
					count++;
				}
			}
		}
		return count;
	}

	/**
	 * 得到列的显示序号（这里的列是指列格式中的预定义列，不是指实际显示列）。 创建日期：(2003-9-28 18:54:47)
	 * 
	 * @return int
	 * @param colinfoIndex
	 *            int
	 */
	private int getShownColIndex(ColInfo[] colInfos, int colinfoIndex) {
		int showIndex = -1;
		// ColInfo[] colInfos = getAllColInfos();
		if (colInfos != null && colinfoIndex < colInfos.length) {
			for (int i = colinfoIndex; i >= 0; i--) {
				if (colInfos[i].isShown) {
					showIndex++;
				}
			}
		}
		return showIndex;
	}

	/**
	 * 更新列格式信息中的列宽（设计态）。 创建日期：(2003-9-28 19:56:40)
	 */
	private void refreshColsWidth() {
		if (m_colInfos != null) {
			TableColumnModel tcm = m_dbTable.getColumnModel();
			for (int i = 0; i < m_colInfos.length; i++) {
				if (m_colInfos[i].isShown) {
					int index = -1;
					for (int j = 0; j < tcm.getColumnCount(); j++) {
						if (m_colInfos[i].colAlias.equals(tcm.getColumn(j)
								.getIdentifier())) {
							index = j;
							break;
						}
					}
					if (index == -1) {
						continue;
					}
					m_colInfos[i].colWidth = tcm.getColumn(index).getWidth();
				}
			}
		}
	}

	/**
	 * (只有在设计态下属性编辑器更改属性时才应调用该方法)。 创建日期：(2003-9-16 10:19:20)
	 * 
	 * @param newHeaderId
	 *            java.lang.String
	 */
	public void setHeaderId(String newHeaderId) {
		m_headerId = newHeaderId;
		try {
			// nc.ui.pub.headerlens.GainHeader gh = new
			// nc.ui.pub.headerlens.GainHeader();
			GainHeaderModel ghModel = new GainHeaderModel();
			ghModel.addHeaderChangeListener(new TableHeaderBuilder(m_dbTable));
			// 戴表头
			if (m_headerId != null && m_headerId.length() != 0) {

				ghModel.setHeader(m_headerId, getTopPanel().getDefDsName());

			} else {
				GroupableTableHeader header = (GroupableTableHeader) (m_dbTable
						.getTableHeader());
				header.clearColumnGroups();
				header.resizeAndRepaint();
			}
			// 设置合适的表头高度
			GroupableTableHeaderUI headerUI = (GroupableTableHeaderUI) m_dbTable
					.getTableHeader().getUI();
			headerUI.setHeaderHeight(0);
			int preferheight = headerUI.getHeaderHeight();
			headerUI.setHeaderHeight(preferheight);

			m_scrollPane.revalidate();
		} catch (Exception e) {
			Logger.error(e);
		}
	}

	/**
	 * 设置表头ID（不更新显示）。 创建日期：(2003-9-16 10:19:20)
	 * 
	 * @param newHeaderId
	 *            java.lang.String
	 */
	public void setHeaderId0(java.lang.String newHeaderId) {
		m_headerId = newHeaderId;
	}

	class MenuActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == m_subTotalMenu) {
				doSubTotal();
			} else if (e.getSource() == m_rotateCrossMenu) {
				if (RQTable.this.m_crossType == QueryConst.CROSSTYPE_SIMPLE) {
					if (RQTable.this.isDesignState()) {
						doSCDef();
					} else {
						doSimpleCross(true);
					}
				} else {
					if (RQTable.this.isDesignState()) {
						doRCDef();
					} else {
						doRotateCross(true);
					}
				}
			} else if (e.getSource() == m_backMenu) {
				popOperStep();
				//wanglei 2013-12-16 穿透返回到穿透行，目前只支持一层穿透
				//m_dbTable.getSelectionModel().setSelectionInterval(iPenetrateRow,iPenetrateRow);
				//wanglei 2013-12-17 支持多级穿透，根据查询id作为键值
				m_dbTable.getSelectionModel().setSelectionInterval(((Integer)hm.get(getID())).intValue(),((Integer)hm.get(getID())).intValue());
//				m_dbTable.setSelectionBackground(Color.yellow);
			} else if (e.getSource() == m_penetrateMenu) {
				doPenetrate();
			}
		}
	}

	class RptTableFastStringRenderer extends TableFastStringRenderer {
		public Component getTableCellRendererComponent(JTable table,
				Object value, boolean isSelected, boolean hasFocus, int row,
				int column) {
			super.getTableCellRendererComponent(table, value, isSelected,
					hasFocus, row, column);
			if (table.getModel() instanceof RptTableModel) {
				RptTableModel model = (RptTableModel) table.getModel();

				int rowLevel = model.getRowLevel(row);
				if (rowLevel != 0 && rowLevel < getLevelColor().length && !isSelected) {
					setBackground(new Color(getLevelColor()[rowLevel]));
				}
			}
			return this;
		}
	}

	class RptTableFormatedRenderer extends RQTableCellRenderer {
		public Component getTableCellRendererComponent(JTable table,
				Object value, boolean isSelected, boolean hasFocus, int row,
				int column) {
			super.getTableCellRendererComponent(table, value, isSelected,
					hasFocus, row, column);
			if (table.getModel() instanceof RptTableModel) {
				RptTableModel model = (RptTableModel) table.getModel();

				int rowLevel = model.getRowLevel(row);
				if (rowLevel != 0 && rowLevel < getLevelColor().length && !isSelected) {
					setBackground(new Color(getLevelColor()[rowLevel]));
				}
			}
			return this;
		}

		public String toString() {
			return "" + hashCode();
		}
	}

	class TableImageRenderer extends DefaultTableCellRenderer {
		public TableImageRenderer() {
			setHorizontalAlignment(JLabel.CENTER);
		}

		public void setValue(Object value) {
			// md by jl 20051219如果是Icon类型则转换，如果不是显示字符串
			if (value instanceof Icon) {
				setIcon((Icon) value);
			} else
				// modified by csli(09-02-24):非法数据类型的时候，对value判空
				setText(value == null ? "unsupported type" : value.toString());
		}
	}

	/**
	 * 绑定数据集(参数为是否更新列)。 创建日期：(2003-12-12 10:08:40)
	 * 
	 * @param updateColumn
	 *            boolean
	 */
	private void bindDataSet(boolean updateColumn) {
		if (m_ds == null) {
			return;
		}

		if (!m_ds.isOpen()) {
			m_ds.open();
		}

		m_ds.removeNavigationListener(this);

		try {
			TableModel model = m_dbTable.getModel();
			if (model != null && model instanceof RptTableModel) {
				RptTableModel oldModel = (RptTableModel) model;
				oldModel.deviateFromDS();
				oldModel.removeTableModelListener(this);
			}
		} catch (Exception e) {
			Logger.error(e);
		}

		if (m_aggregateDescriptor == null) {
			m_tableType = NORMAL;
		}

		RptTableModel m = null;
		switch (m_tableType) {
		case NORMAL:
			m = new CommonRptTableModel(m_ds);
			m_ds.addNavigationListener(this);
			m_aggregateDescriptor = null;

			// V55+ 不显示定位菜单
			showLocatePopupItem(false);

			break;
		case SUBTOTAL: {
			if (m_crossType == QueryConst.CROSSTYPE_SIMPLE) {
				m = new AggrByRHTableModel(m_ds, m_aggregateDescriptor,
						m_rowHeader.getRowTableModel());
			} else {
				m = new AggregateTableModel(m_ds, m_aggregateDescriptor);
			}

			// V55+ 显示定位菜单
			showLocatePopupItem(true);

			break;
		}
			// 旋转交叉合计行(zjb)
		case ROTATE_SUBTOTAL:
			try {
				// 计算旋转交叉合计行
				makeAggDesc();
				// 设置表模型(必然是旋转交叉)
				if (m_aggregateDescriptor != null) {
					m = new AggregateTableModel(m_ds, m_aggregateDescriptor);
				}

				// V55+ 显示定位菜单
				showLocatePopupItem(true);

			} catch (Exception e) {
				Logger.error(e);
				// CASE NORMAL
				m = new CommonRptTableModel(m_ds);
				m_ds.addNavigationListener(this);
				m_aggregateDescriptor = null;
			}
			break;
		}

		TableColumnModel oldcm = m_dbTable.getColumnModel();
		int colCount = oldcm.getColumnCount();
		TableColumn[] tcs = new TableColumn[colCount];
		for (int i = 0; i < colCount; i++) {
			tcs[i] = oldcm.getColumn(i);
		}
		m_dbTable.setModel(m);
		m.addTableModelListener(this);
		m.setEnableListen(m_enableListen);

		if (updateColumn) {
			TableColumnModel cm = m_dbTable.getColumnModel();
			while (cm.getColumnCount() > 0) {
				cm.removeColumn(cm.getColumn(0));
			}

			// Create new columns from the data model info
			for (int i = 0; i < m.getColumnCount(); i++) {
				Column columnm = m.getColumn(i);

				if (columnm.isHidden()) {
					continue;
				}
				TableColumn newColumn = new TableColumn(i);

				bindDataSetColumnProperties(newColumn, columnm);
				// newColumn.s
				m_dbTable.addColumn(newColumn);
			}
		} else {
			TableColumnModel cm = m_dbTable.getColumnModel();
			while (cm.getColumnCount() > 0) {
				cm.removeColumn(cm.getColumn(0));
			}

			for (int i = 0; i < colCount; i++) {
				m_dbTable.addColumn(tcs[i]);
			}
		}

		if (!isDesignState() && m_tableType == NORMAL) {
			navigated(new NavigationEvent(m_ds));
		}
	}

	/**
	 * 根据数据集中列的格式设定表的列格式 创建日期：(2003-12-12 10:08:40)
	 * 
	 * @param tableColumn
	 *            TableColumn
	 * @param dataSetColumn
	 *            Column
	 */
	private void bindDataSetColumnProperties(TableColumn tableColumn,
			Column dataSetColumn) {
		tableColumn.setHeaderValue(dataSetColumn.getCaption());
		tableColumn.setIdentifier(dataSetColumn.getColumnName());

		tableColumn.setPreferredWidth(80);
		tableColumn.setWidth(tableColumn.getPreferredWidth());

		if (dataSetColumn.getItemPainter() != null
				&& dataSetColumn.getItemPainter() instanceof TableCellRenderer) {
			tableColumn.setCellRenderer((TableCellRenderer) dataSetColumn
					.getItemPainter());
		} else {
			tableColumn.setCellRenderer(getDefaultCellRenderer(dataSetColumn));
		}
	}

	/**
	 * 获取小计合计描述符
	 * 
	 * @return AggregateDescriptor
	 */
	// private AggregateDescriptor getAggregateDescriptor() {
	// return m_aggregateDescriptor;
	// }
	/**
	 * 根据数据集的列获取默认的渲染器
	 * 
	 * @param dataSetColumn
	 *            Column
	 * @return TableCellRenderer
	 */
	protected TableCellRenderer getDefaultCellRenderer(Column dataSetColumn) {
		int type = dataSetColumn.getDataType();

		if (type > Variant.NULL_TYPES && type < Variant.OBJECT
				&& type != Variant.BOOLEAN) {
			if (type == Variant.INPUTSTREAM) {
				// INPUTSTREAMs are assumed to contain image data
				// A custom renderer should be specified on an INPUTSTREAM not
				// containing image data
				return new TableImageRenderer();
			} else {
				RptTableFastStringRenderer renderer = new RptTableFastStringRenderer();

				if (dataSetColumn.getForeground() != null) {
					renderer
							.setDefaultForeground(dataSetColumn.getForeground());
				}
				if (dataSetColumn.getBackground() != null) {
					renderer
							.setDefaultBackground(dataSetColumn.getBackground());
				}
				if (dataSetColumn.getFont() != null) {
					renderer.setDefaultFont(dataSetColumn.getFont());
				}
				renderer.setDefaultAlignment(dataSetColumn.getAlignment());

				return renderer;
			}
		}

		TableCellRenderer renderer = getDefaultRenderer(RptTableModel
				.getJavaClass(dataSetColumn.getDataType()));
		if (renderer instanceof DefaultTableCellRenderer) {
			DefaultTableCellRenderer cellRenderer = (DefaultTableCellRenderer) renderer;
			if (dataSetColumn.getForeground() != null) {
				cellRenderer.setForeground(dataSetColumn.getForeground());
			}
			if (dataSetColumn.getBackground() != null) {
				cellRenderer.setBackground(dataSetColumn.getBackground());
			}
			if (dataSetColumn.getFont() != null) {
				cellRenderer.setFont(dataSetColumn.getFont());
			}
			cellRenderer.setHorizontalAlignment(DBUtilities
					.convertJBCLToSwingAlignment(dataSetColumn.getAlignment(),
							true));
			cellRenderer.setVerticalAlignment(DBUtilities
					.convertJBCLToSwingAlignment(dataSetColumn.getAlignment(),
							false));
			return cellRenderer;
		}

		return renderer;
	}

	/**
	 * 根据表中列类型获取默认的渲染器
	 * 
	 * @param columnClass
	 *            Class
	 * @return TableCellRenderer
	 */
	protected TableCellRenderer getDefaultRenderer(Class columnClass) {
		if (columnClass == null) {
			return null;
		} else {
			Object renderer = m_dbTable.getDefaultRenderer(columnClass);
			if (renderer != null) {
				return (TableCellRenderer) renderer;
			} else {
				return getDefaultRenderer(columnClass.getSuperclass());
			}
		}
	}

	/**
	 * 返回打印信息数组。 创建日期：(2003-9-29 15:06:16)
	 * 
	 * @return nc.ui.pub.print.datastruct.PrintCellData[][]
	 */
	public PrintCellData[][] getPrintCellData() {

		FmdPrintInfoNew printInfo = getTopPanel().getPrintInfo();
		// 是否打印行号
		boolean bShowNum = printInfo.isShowTableRowNum();
		// 是否需要调整行号的打印与否(因为如果多行头里已经设定了不显示行头则不需另外处理)
		boolean bNeedAlterRowNum = false;

		// 多行头列数
		int rowHeaderColNum = m_rowHeader.getColumnCount();
		if (!bShowNum && m_rowHeader.isShowRowNum()) {
			bNeedAlterRowNum = true;
		}

		// 表体列数
		int tableColNum = m_dbTable.getColumnCount();
		// 总列数
		int totalColNum = rowHeaderColNum + tableColNum;

		if (totalColNum == 0) {
			return new PrintCellData[0][0];
		}

		// 哈希已经设定的列格式，方便查找
		ColInfo[] colInfos = getAllColInfos();
		Hashtable htColInfos = new Hashtable();
		if (colInfos != null) {
			for (int i = 0; i < colInfos.length; i++) {
				htColInfos.put(colInfos[i].colAlias.toUpperCase(), colInfos[i]);
			}
		}

		int startCol = 0;
		if (bNeedAlterRowNum) {
			startCol = (m_rowHeader.isRowNumAtFront()) ? 1 : 0;
			totalColNum--;
			rowHeaderColNum--;
		}
		// 获得列宽数组（以表体为准）
		int[] iWidthsTemp = new int[totalColNum];
		ColInfo[] showColInfos = new ColInfo[totalColNum];
		int iWidthsAll = 0;
		// jl+ 锁定状态下是锁定表头的列模型
		TableColumnModel tcm = m_rowHeader.getColumnModel();
		for (int i = 0; i < rowHeaderColNum; i++) {
			TableColumn tc = tcm.getColumn(i + startCol);
			// 其实这是不需要的，不过为了统一起见，弄一下子
			showColInfos[i] = new ColInfo();
			showColInfos[i].colAlias = tc.getIdentifier() == null ? "" : tc
					.getIdentifier().toString();
			showColInfos[i].colName = tc.getHeaderValue() == null ? "" : tc
					.getHeaderValue().toString();
			iWidthsTemp[i] = tc.getWidth();
			iWidthsAll += iWidthsTemp[i];
		}
		// int j = 0;
		tcm = m_dbTable.getColumnModel();
		for (int i = rowHeaderColNum; i < totalColNum; i++) {
			TableColumn tc = tcm.getColumn(i - rowHeaderColNum);
			String identifier = (tc.getIdentifier() == null) ? "NULL" : tc
					.getIdentifier().toString().toUpperCase();
			if (tc.getIdentifier() != null
					&& htColInfos.containsKey(identifier)) {
				showColInfos[i] = (ColInfo) htColInfos.get(identifier);
			}
			// jl+
			else if (htColAliasColInfo.containsKey(identifier)) {
				try {
					showColInfos[i] = (ColInfo) ((ColInfo) htColAliasColInfo
							.get(identifier)).clone();
				} catch (CloneNotSupportedException e) {
					Logger.error(e);
					showColInfos[i] = new ColInfo();
				}
				showColInfos[i].colAlias = tc.getIdentifier() == null ? "" : tc
						.getIdentifier().toString();
				showColInfos[i].colName = tc.getHeaderValue() == null ? "" : tc
						.getHeaderValue().toString();
			} else {
				showColInfos[i] = new ColInfo();
				showColInfos[i].colAlias = tc.getIdentifier() == null ? "" : tc
						.getIdentifier().toString();
				showColInfos[i].colName = tc.getHeaderValue() == null ? "" : tc
						.getHeaderValue().toString();
			}
			iWidthsTemp[i] = tc.getWidth();
			iWidthsAll += iWidthsTemp[i];
		}

		int[] iWidths = iWidthsTemp;

		PrintCellData[][] cellsesBodyHead = getHeaderCellData(iWidths,
				showColInfos, bNeedAlterRowNum);
		PrintCellData[][] cellsesBody = getBodyCellData(iWidths, showColInfos,
				bNeedAlterRowNum);

		// 组装整表打印信息数组
		int totalrows = 0;
		if (cellsesBodyHead != null) {
			totalrows += cellsesBodyHead.length;
		}
		if (cellsesBody != null) {
			totalrows += cellsesBody.length;

		}
		PrintCellData[][] cellsesTable = new PrintCellData[totalrows][];
		int row = 0;
		if (cellsesBodyHead != null) {
			for (int i = 0; i < cellsesBodyHead.length; i++) {
				cellsesTable[row] = cellsesBodyHead[i];
				row++;
			}
		}
		if (cellsesBody != null) {
			for (int i = 0; i < cellsesBody.length; i++) {
				cellsesTable[row] = cellsesBody[i];
				row++;
			}
		}
		return cellsesTable;
	}

	/**
	 * 初始化弹出菜单。 创建日期：(2003-9-30 14:12:54)
	 */
	public void initPopupMenu() {
		UIPopupMenu popupMenu = m_dbTable.getHeaderPopupMenu();

		// V55+ 运行态控制显示定位菜单，以便小计合计时能够查找数据（以前没有这个判断）
		if (m_designState) {
			while (popupMenu.getComponentCount() > 0) {
				popupMenu.remove(0);
			}
		}

		popupMenu.add(m_subTotalMenu);
		popupMenu.add(m_rotateCrossMenu);
		popupMenu.add(m_penetrateMenu);
		popupMenu.add(m_backMenu);
		popupMenu.addSeparator();
		// jl+
		popupMenu.add(m_lockColMenu);

		MenuActionListener actionListener = new MenuActionListener();
		m_subTotalMenu.addActionListener(actionListener);
		m_rotateCrossMenu.addActionListener(actionListener);
		m_penetrateMenu.addActionListener(actionListener);
		m_backMenu.addActionListener(actionListener);
		// 加锁定监听器
		ActionListener lockListener = new LockTableActionListener();

		m_lockColMenu.addActionListener(lockListener);
		m_unlockColMenu.addActionListener(lockListener);

		if (isDesignState()) {
			// m_rotateCrossMenu.setEnabled(false);
			m_penetrateMenu.setEnabled(false);
			// jl+ 设计态下面没法锁定
			// m_lockColMenu.setEnabled(false);
		}
		m_backMenu.setEnabled(false);
	}

	/**
	 * 设置小计合计描述符
	 * 
	 * @param aggregateDescriptor
	 *            AggregateDescriptor
	 */
	private void setAggregateDescriptor(AggregateDescriptor aggregateDescriptor) {
		this.m_aggregateDescriptor = aggregateDescriptor;
		m_tableType = (aggregateDescriptor == null ? 0 : 1);
		// table.setSortEnabled(aggregateDescriptor == null);
	}

	/**
	 * 设置绑定数据集
	 * 
	 * @param ds
	 *            DataSet
	 */
	public void setDataset(DataSet ds) {
		// 去掉对原数据结果集的监听
		if (this.m_ds != null) {
			this.m_ds.removeNavigationListener(this);
		}
		this.m_ds = ds;
		// 如果绑定的是最初的数据库，去RQTopPanel里取监听标志
		if (!isDSChanged()) {
			m_enableListen = getTopPanel().getListenFlagByAlias(getID());
			getTopPanel().removeDynDataSet(getName());
		}
		// 如果否（即结果集是自身维护的），新建标志
		else {
			m_enableListen = new DSListenerEnable(true);
			// 更新底层Panel中的动态数据集
			getTopPanel().addDynDataSet(getName(),
					(QEDataSet) getBindDataSet(), getCurrentQMD(),
					m_curDsFp.getFormulas(), m_enableListen);
		}
	}

	/**
	 * 小计合计。 创建日期：(2003-9-30 14:28:40)
	 */
	public void doSubTotal() {
		if (getID() == null || getID().trim().length() == 0) {
			MessageDialog.showWarningDlg(this, nc.ui.ml.NCLangRes.getInstance()
					.getStrByID("10241201", "UPP10241201-000069")/*
			 * @res
			 * "小计合计"
			 */, nc.ui.ml.NCLangRes.getInstance().getStrByID("10241201",
					"UPP10241201-000073")/*
			 * @res "请先绑定结果集"
			 */);
			return;
		}
		AggregateDlg dlg = null;
		if (m_crossType == QueryConst.CROSSTYPE_SIMPLE) {
			dlg = new AggregateDlg(RQTable.this, getAllColInfos(),
					m_aggregateDescriptor, m_rowHeader.getRoot().getDepth());
		} else {
			dlg = new AggregateDlg(RQTable.this, m_colInfos,
					m_aggregateDescriptor);
		}
		dlg.showModal();
		dlg.destroy();
		if (dlg.getResult() == UIDialog.ID_OK) {
			setAggregateDescriptor(dlg.getAggregateDescriptor());
			if (!isDesignState()) {
				if (getBindDataSet() == null) {
					return;
				}
				if (m_tableType == 0) {
					m_rowHeader.reloadRGs();
				}
				bindDataSet(false);
				updateRowHeader();
			}
		}
	}

	/**
	 * 更新行头。 创建日期：(2003-10-20 17:14:15)
	 */
	private void updateRowHeader() {
		if (m_rowHeader != null) {
			// 在非锁定状态下重置
			if (!isLockCol()) {
				m_rowHeader.setTable(m_dbTable);
				m_rowHeader.revalidate();
				m_rowHeader.repaint();
				m_scrollPane.getRowHeader().setPreferredSize(
						m_rowHeader.getPreferredSize());
			}
		}
	}

	/**
	 * Called whenever the value of the selection changes.
	 * 
	 * @param e
	 *            the event that characterizes the change.
	 */
	public void valueChanged(javax.swing.event.ListSelectionEvent e) {
		// 限制合计表
		if (m_tableType != 0) {
			return;
		} else {
			if (e.getSource() == m_dbTable.getColumnModel().getSelectionModel()) {
				int tableColumn = m_dbTable.getSelectedColumn();
				if (!e.getValueIsAdjusting()
						&& m_ds != null
						&& tableColumn > -1
						&& tableColumn < m_dbTable.getColumnModel()
								.getColumnCount()) {
					int modelColumn = m_dbTable
							.convertColumnIndexToModel(tableColumn);
					TableModel tableModel = m_dbTable.getModel();

					if (modelColumn >= 0
							&& modelColumn < tableModel.getColumnCount()
							&& m_ds != null
							&& tableModel instanceof RptTableModel) {
						Column column = ((RptTableModel) tableModel)
								.getColumn(modelColumn);
						m_ds.setLastColumnVisited(column.getColumnName());
					}

				}
			} else {
				int selectedRow = m_dbTable.getSelectedRow();
				if (selectedRow >= m_dbTable.getRowCount()) { // This test is
					// a
					// fix for 149157.
					// Charles
					selectedRow = m_dbTable.getRowCount() - 1;
				}
				if (!m_ignoreNavigation && !e.getValueIsAdjusting()
						&& m_ds != null && m_ds.isOpen()
						&& selectedRow != m_ds.getRow() && selectedRow > -1) {
					// If a row is being edited, then we first have to see if we
					// can post the
					// row before navigating to a different row. Note that this
					// may cause the row to move
					// to a different row position.
					if (m_ds.isEditing()) {
						try {
							m_ds.post();
						} catch (DataSetException ex) {
							m_ignoreNavigation = true;
							// m_ds.getRow() should still be a valid row, so no
							// need to check for valid range here
							m_dbTable.setRowSelectionInterval(m_ds.getRow(),
									m_ds.getRow());
							m_ignoreNavigation = false;
							DBExceptionHandler.handleException(m_ds, ex);
							return;
						}
					}
					if (m_ds.getRow() != selectedRow) {
						m_ignoreNavigation = true;
						try {
							m_ds.goToRow(selectedRow);
							DataRow row = new DataRow(m_ds);
							m_ds.getDataRow(selectedRow, row);
							((QEDataSet) m_ds)
									.dispatchPassiveLoadingEvent(new PassiveLoadingEvent(
											row));
							if (selectedRow > 0) {
								m_dbTable.setRowSelectionInterval(selectedRow,
										selectedRow);
							}
						} catch (DataSetException ex) {
							// m_ds.getRow() should still be a valid row, so no
							// need to check for valid range here
							m_dbTable.setRowSelectionInterval(m_ds.getRow(),
									m_ds.getRow());
							DBExceptionHandler.handleException(m_ds, ex);
						} finally {
							m_ignoreNavigation = false;
						}
					}
				}
			}
		}
	}

	/**
	 * 此处插入方法说明。 创建日期：(2003-10-21 16:31:46)
	 */
	// private void doTreeTableShow() {
	// TreeTableExDlg dlg = new TreeTableExDlg(RQTable.this, colInfos,
	// treetableDescriptor);
	// dlg.showModal();
	// if (dlg.getResult() == UIDialog.ID_OK) {
	// TableModel oldModel = m_dbTable.getModel();
	// treetableDescriptor = dlg.getTreeTableDescriptor();
	// if (cttt == null) {
	// cttt = new CreateTreeTableTool(m_dbTable);
	// }
	// if (treetableDescriptor.type == TreeCreateTool.CONSTRUCTBYID) {
	// int idIndex =
	// m_dbTable.convertColumnIndexToModel(treetableDescriptor.mainFieldIndex);
	// int parentidIndex =
	// m_dbTable.convertColumnIndexToModel(treetableDescriptor.parentFieldIndex);
	// int valueIndex =
	// m_dbTable.convertColumnIndexToModel(treetableDescriptor.valueFieldIndex);
	// cttt.createTreeTable(idIndex, parentidIndex, valueIndex);
	// }
	// else if(treetableDescriptor.type == TreeCreateTool.CONSTRUCTBYCODE){
	// int codeFieldIndex =
	// m_dbTable.convertColumnIndexToModel(treetableDescriptor.mainFieldIndex);
	// String codeRule = treetableDescriptor.codeRule;
	// int valueIndex =
	// m_dbTable.convertColumnIndexToModel(treetableDescriptor.valueFieldIndex);
	// cttt.createTreeTableByCode(codeFieldIndex, codeRule, valueIndex);
	// }
	// oldModel.removeTableModelListener(modelListener);
	// m_dbTable.getModel().addTableModelListener(modelListener);
	// //树表状态下不能导航
	// m_dbTable.getSelectionModel().removeListSelectionListener(this);
	// }
	// }
	/**
	 * 添加列。 创建日期：(2003-12-12 10:08:40)
	 * 
	 * @param evt
	 *            javax.swing.event.TableModelEvent
	 */
	private void addColumn(TableModelEvent evt) {
		if (isLockCol()) {
			MessageDialog.showWarningDlg(this, nc.ui.ml.NCLangRes.getInstance()
					.getStrByID("10241201", "UPP10241201-000099")/*
			 * @res
			 * "查询引擎"
			 */, nc.ui.ml.NCLangRes.getInstance().getStrByID("10241201",
					"UPP10241201-001506")/*
			 * @res "请解锁之后再操作"
			 */);
			return;
		}
		int modelCol = evt.getColumn();
		TableColumn tc = new TableColumn(modelCol);
		Column col = ((RptTableModel) m_dbTable.getModel()).getColumn(modelCol);

		FormulaColDescriptor[] fcDsp = getTopPanel().getFormulaColByAlias(
				getRealID());
		if (fcDsp != null) {
			int i;
			for (i = 0; i < fcDsp.length; i++) {
				if (fcDsp[i].getColAlias()
						.equalsIgnoreCase(col.getColumnName())) {
					if (fcDsp[i].getColFmtVO(getTopPanel().getDefDsName()) != null) { // 公式列格式不为空
						DataformatVO dataFmt = fcDsp[i]
								.getColFmtVO(getTopPanel().getDefDsName());
						RptTableFormatedRenderer rcr = new RptTableFormatedRenderer();
						rcr.initLabel(dataFmt);
						tc.setCellRenderer(rcr);
						tc.setHeaderValue(col.getCaption());
						tc.setIdentifier(col.getColumnName());
						tc.setPreferredWidth(80);
						tc.setWidth(tc.getPreferredWidth());
					} else {
						bindDataSetColumnProperties(tc, col);
					}
					// 添加动态列格式
					ColInfo colInfo = new ColInfo();
					colInfo.colType = QueryUtil.variantTypeToSqlType(fcDsp[i]
							.getColType());
					colInfo.colAlias = col.getColumnName();
					colInfo.colName = col.getCaption();
					colInfo.colWidth = 80;
					colInfo.setColFormatID(fcDsp[i].getColFmtID());
					if (!getDynColInfo().contains(colInfo))
						getDynColInfo().add(colInfo);
					break;
				}
			}
			if (i == fcDsp.length) {
				bindDataSetColumnProperties(tc, col);
			}
		} else {
			bindDataSetColumnProperties(tc, col);
		}
		m_dbTable.addColumn(tc);
	}

	/**
	 * 判断当前列名内包含有哪个初始列名（用于处理交叉后的列名）。 创建日期：(2003-12-16 10:36:55) modify by jl on
	 * 2007-10-19 修改判别方式
	 * 
	 * @return java.lang.String
	 * @param colName
	 *            java.lang.String
	 */
	private String containsColKey(String colName) {
		if (colName == null) {
			return colName;
		}
		ColInfo[] colInfos = getAllColInfos();
		String key = null;
		// int iIndex = colName.indexOf(CrossTable.CRS_DELIM);
		// V502 JL改
		String[] colName_splitted = StringUtils.split(colName,
				CrossTable.CRS_DELIM);

		for (int i = 0; i < colInfos.length; i++) {
			if (ArrayUtils.contains(colName_splitted, colInfos[i].colName)) {
				key = colInfos[i].colName;
				break;
			}
			// // if (ArrayUtils.indexOf(origincolnames, colInfos[i].colName) >=
			// 0)
			// // {
			// // // if (colName.indexOf(colInfos[i].colName) >= 0) {
			// if (iIndex >= 0) {
			// if (colName.substring(0, iIndex).equals(colInfos[i].colName)) {
			// key = colInfos[i].colName;
			// break;
			// }
			// } else {
			// if (colName.equals(colInfos[i].colName)) {
			// key = colInfos[i].colName;
			// break;
			// }
			// }
		}
		return key;
	}

	/**
	 * 删除列。 创建日期：(2003-12-12 10:08:40)
	 * 
	 * @param evt
	 *            javax.swing.event.TableModelEvent
	 */
	private void delColumn(TableModelEvent evt) {
		if (isLockCol()) {
			MessageDialog.showWarningDlg(this, nc.ui.ml.NCLangRes.getInstance()
					.getStrByID("10241201", "UPP10241201-000099")/*
			 * @res
			 * "查询引擎"
			 */, nc.ui.ml.NCLangRes.getInstance().getStrByID("10241201",
					"UPP10241201-001509")/*
			 * @res "请解锁之后再操作"
			 */);
			return;
		}
		int modelCol = evt.getColumn();
		TableColumnModel tcm = m_dbTable.getColumnModel();
		// 待删除的列List
		ArrayList colList = new ArrayList();
		int colCount = tcm.getColumnCount();
		String identifier = null;
		for (int i = 0; i < colCount; i++) {
			TableColumn tc = tcm.getColumn(i);

			int modelIndex = tc.getModelIndex();
			// 找到待删除列
			if (modelIndex == modelCol) {
				colList.add(tc);
			}
			// 修改被删除列后的列的ModelIndex
			else if (modelIndex > modelCol) {
				tc.setModelIndex(--modelIndex);
			}
		}
		for (int i = 0; i < colList.size(); i++) {
			if (i == 0) {
				identifier = ""
						+ ((TableColumn) colList.get(i)).getIdentifier();
			}
			tcm.removeColumn((TableColumn) colList.get(i));
		}
		// 删除动态列格式
		if (identifier != null) {
			int size = getDynColInfo().size();
			for (int i = size - 1; i >= 0; i--) {
				ColInfo colInfo = (ColInfo) getDynColInfo().get(i);
				if (identifier.equalsIgnoreCase(colInfo.colAlias)) {
					getDynColInfo().remove(i);
				}
			}
		}
	}

	/**
	 * 获取绑定数据集。 创建日期：(2003-12-12 12:59:47)
	 * 
	 * @return com.borland.dx.dataset.DataSet
	 */
	public DataSet getBindDataSet() {
		return m_ds;
	}

	/**
	 * 得到当前的查询定义。 创建日期：(2003-12-15 17:08:13)
	 * 
	 * @return nc.vo.pub.querymodel.QueryModelDef
	 */
	private QueryModelDef getCurrentQMD() {
		return m_curDsFp.getQmd();
	}

	/**
	 * 获取行头信息。 创建日期：(2003-11-16 13:07:27)
	 * 
	 * @return nc.ui.reportquery.component.RowHeaderInfo
	 */
	public RowHeaderInfo getRowHeaderInfo() {
		return m_rowHeaderInfo;
	}

	/**
	 * navigated 方法注解。
	 * 
	 * @param arg1
	 *            NavigationEvent
	 */
	public void navigated(com.borland.dx.dataset.NavigationEvent arg1) {
		if (!m_enableListen.enabled) {
			return;
		}
		int row = ((DataSet) arg1.getSource()).getRow();
		if (!m_ignoreNavigation) {
			m_ignoreNavigation = true;
			if (m_dbTable.getRowCount() <= 0) {
				m_ignoreNavigation = false;
				return;
			}
			m_dbTable.setRowSelectionInterval(row, row);

			Rectangle rc = m_dbTable.getCellRect(row, 0, true);
			m_dbTable.scrollRectToVisible(rc);
			repaint();
			m_ignoreNavigation = false;
		}
	}

	/**
	 * 当表类型为旋转交叉表时处理生成多表头的方法。 创建日期：(2003-11-26 14:22:45)
	 * 
	 * @param fldgrps
	 *            nc.vo.pub.cquery.FldgroupVO[]
	 */
	private void procFldGrps(FldgroupVO[] fldgrps) {
		GainHeaderModel ghModel = new GainHeaderModel();
		ghModel.addHeaderChangeListener(new TableHeaderBuilder(m_dbTable));
		ghModel.setHeader(fldgrps);

		m_scrollPane.revalidate();
	}

	/**
	 * 当前操作入栈
	 */
	private void pushOperStep() {
		m_curDsFp.setCrosstype(m_crossType);
		m_curDsFp.setTabletype(m_tableType);
		if (m_tableType == SUBTOTAL) {
			m_curDsFp.setAggrDescriptor(m_aggregateDescriptor);
		}
		m_curDsFp.setFormulas(getTopPanel().getFormulaColByAlias(getRealID()));
		m_curDsFp.setDynColInfo(m_dynColInfo);
		m_actionStack.push(m_curDsFp);
		m_backMenu.setEnabled(true);
	}

	/**
	 * 运行态刷新。 创建日期：(2003-11-26 8:45:43)
	 * 
	 * @i18n upp08110600603=:列格式设置出错:
	 */
	private void refreshColsAndHeader() {
		// 判断当前刷新是否在最初数据集上
		boolean isFirst = m_actionStack.isEmpty();

		// 设置列格式
		// NCV5.3SCM补丁
		// ColInfo[] colInfos = getAllColInfos();
		ColInfo[] colInfos = (ColInfo[]) NCBeanUtils
				.cloneBeanArray(getAllColInfos());

		int iLen = colInfos == null ? 0 : colInfos.length;
		LinkedList colInfoIDList = new LinkedList();
		// String dsName = null;
		boolean bHasColFormat = false;
		for (int i = 0; i < iLen; i++) {
			colInfoIDList.add(colInfos[i].getColFormatID());
			if (colInfos[i].getColFormatID() != null) {
				// 记录此标志是为了避免无效的远程调用
				bHasColFormat = true;
			}
		}
		try {
			DataformatVO[] formatVOArray = null;
			if (bHasColFormat) {
				// md by jl on 2006-07-12(修改格式对象的获取方式，一次取出所有列格式)
				IColumnFormat colFmtGetter = (IColumnFormat) NCLocator
						.getInstance().lookup(IColumnFormat.class.getName());
				String[] colfmtID = (String[]) colInfoIDList
						.toArray(new String[colInfoIDList.size()]);
				formatVOArray = colFmtGetter.getColFormats(colfmtID,
						getTopPanel().getDefDsName());
			}
			int iFormatLen = (formatVOArray == null) ? 0 : formatVOArray.length;
			if (colInfos != null) {
				TableColumnModel tcm = m_dbTable.getColumnModel();
				// jl+ @ 2007-10-23
				// 设置了列格式之后，锁定时会出现错位的现象，原因是在根据列格式调整表列模型时，没有把锁定表的模型一起调整了。
				lockColTable = null;
				TableColumnModel lcm = getLockColTable().getColumnModel();
				// 如果是最初的数据集，则须处理列信息中的序号，显示与否，格式和宽度等信息
				if (isFirst) {
					int colCount = colInfos.length;
					for (int i = 0; i < colCount; i++) {
						try {
							int origIndex = -1;
							for (int j = 0; j < tcm.getColumnCount(); j++) {
								if (colInfos[i].colAlias.equalsIgnoreCase(tcm
										.getColumn(j).getIdentifier()
										.toString())) {
									origIndex = j;
									break;
								}
							}
							// 没有该列
							if (origIndex == -1) {
								colInfos[i].isShown = false;
								continue;
							}
							TableColumn tc = tcm.getColumn(origIndex);
							TableColumn ltc = lcm.getColumn(origIndex);
							// 如果列格式设定不显示，则删除改列
							if (!colInfos[i].isShown) {
								tcm.removeColumn(tc);
								lcm.removeColumn(ltc);
								continue;
							}
							tc.setHeaderValue(colInfos[i].colName);
							// colInfos[i].colName =
							// tc.getHeaderValue().toString();
							// 设置列格式（无列格式时iFormatLen为0，不会进入冒号后的分支
							DataformatVO format = (i >= iFormatLen) ? null
									: formatVOArray[i];
							if (format != null) {
								RptTableFormatedRenderer rcr = new RptTableFormatedRenderer();
								rcr.initLabel(format);
								tc.setCellRenderer(rcr);
							}
							tc.setWidth(colInfos[i].colWidth);
							tc.setPreferredWidth(colInfos[i].colWidth);
							// 移动列到指定位置
							tcm.moveColumn(origIndex, getShownColIndex(
									colInfos, i));
							lcm.moveColumn(origIndex, getShownColIndex(
									colInfos, i));
						} catch (Exception e) {
							Logger.error(getName() + ":列格式设置出错:"
									+ colInfos[i].colName, e);
						}
					}
					if (m_crossType != QueryConst.CROSSTYPE_ROTATE) {
						setHeaderId(getHeaderId());
					}
				}
				// 非最初数据集只处理列信息中的列格式，宽度，显示与否等信息
				else {
					// 哈希列信息
					Hashtable htColInfo = new Hashtable();

					for (int i = 0; i < colInfos.length; i++) {
						htColInfo.put(colInfos[i].colName, colInfos[i]);
					}
					int realColCount = tcm.getColumnCount();
					// 将要删掉的列
					ArrayList col2bDel = new ArrayList();
					// 清一下
					htColAliasColInfo.clear();
					for (int i = 0; i < realColCount; i++) {
						TableColumn tc = tcm.getColumn(i);
						String id = tc.getHeaderValue() == null ? null : tc
								.getHeaderValue().toString();
						String colIdentifier = tc.getIdentifier() == null ? "NULL"
								: tc.getIdentifier().toString().toUpperCase();
						if (id != null) {
							String header = containsColKey(id);

							// V5.5+ Edit by: yza 2008-5-28 旋转交叉小计合计列格式丢失问题
							ColInfo colinfo = null;
						    // ZJB+，穿透状态会新生很多列，这些并不是交叉合计列
							boolean bCrossTotal = false;
							if (m_crossType != QueryConst.CROSS_NONE
									&& header == null) {
								bCrossTotal = true;
							}
							if (!bCrossTotal) // 普通列
							// if (header != null) // 普通列
							{
								colinfo = header == null ? null : (ColInfo) htColInfo.get(header);
								if (colinfo == null) {
									continue;
								}
								// 如果列格式设定不显示，则删除改列
								if (!colinfo.isShown) {
									col2bDel.add(tc);
									continue;
								}

								htColAliasColInfo.put(colIdentifier, colinfo);
								tc.setWidth(colinfo.colWidth);
								tc.setPreferredWidth(colinfo.colWidth);
							} else {
								// YZA+，合计列
								colinfo = getAvgCol(id, htColInfo);
							}

							// 原来代码 ---
							// ColInfo colinfo = header == null ? null :
							// (ColInfo) htColInfo.get(header);
							// if (colinfo == null) {
							// continue;
							// }
							// // 如果列格式设定不显示，则删除改列
							// if (!colinfo.isShown) {
							// col2bDel.add(tc);
							// continue;
							// }
							//
							// htColAliasColInfo.put(colIdentifier, colinfo);
							// ---

							// 设置列格式
							DataformatVO format = colinfo
									.getColFormatVO(getTopPanel()
											.getDefDsName());
							if (format != null) {
								RptTableFormatedRenderer rcr = new RptTableFormatedRenderer();
								rcr.initLabel(format);
								tc.setCellRenderer(rcr);
							}
							// tc.setWidth(colinfo.colWidth);
							// tc.setPreferredWidth(colinfo.colWidth);
						}
					}
					// 删除隐藏列
					for (int i = col2bDel.size() - 1; i >= 0; i--) {
						tcm.removeColumn((TableColumn) col2bDel.get(i));
					}

					if (m_crossType != QueryConst.CROSSTYPE_ROTATE) {
						GroupableTableHeader header = (GroupableTableHeader) (m_dbTable
								.getTableHeader());
						header.clearColumnGroups();
						header.resizeAndRepaint();
						GroupableTableHeaderUI headerUI = (GroupableTableHeaderUI) header
								.getUI();
						headerUI.setHeaderHeight(0);
						int preferheight = headerUI.getHeaderHeight();
						headerUI.setHeaderHeight(preferheight);
						m_scrollPane.revalidate();
					}
				}
			}
			// 如果是交叉表处理表头
			if (m_crossType == QueryConst.CROSSTYPE_ROTATE) {
				procFldGrps(m_curDsFp.getFldGrpVOs());
			}
		} catch (Exception e) {
			Logger.error(e);
		}
	}

	/*
	 * V5.5+ yza 2008-5-28 返回做合计的真实列,这里只关心其列格式
	 */
	/**
	 * @i18n upp08110600012=合计
	 */
	private ColInfo getAvgCol(String colName, Hashtable columns) {
		if (colName == null || colName.trim().length() == 0)
			return null;
		int place = colName.lastIndexOf(NCLangRes.getInstance().getStrByID(
				"20081106", "upp08110600012"));
		String avgColumnName = colName.substring(0, place);
		ColInfo avgColumn = (ColInfo) columns.get(avgColumnName);
		ColInfo newColumn = null;
		try {
			newColumn = (ColInfo) avgColumn.clone();
		} catch (CloneNotSupportedException e) {
			return null;
		}
		return newColumn;
	}

	/**
	 * 刷新行头。 创建日期：(2003-12-16 11:40:55)
	 * 
	 * @param rhInfo
	 *            nc.ui.reportquery.component.table.RowHeaderInfo
	 */
	private void refreshRowHeader(RowHeaderInfo rhInfo) {
		m_rowHeader.initByRHInfo(rhInfo, getTopPanel().getDefDsName());
		if (m_crossType == QueryConst.CROSSTYPE_SIMPLE
				&& rhInfo.getRowHeaderID() == null && getID() != null) {
			try {
				createDefaultRowHeader();
			} catch (Exception e) {

			}
		}
		if (!isDesignState()) {
			m_rowHeader.setTable(m_dbTable);
		}
		m_scrollPane.setCorner(JScrollPane.UPPER_LEFT_CORNER, m_rowHeader
				.getTableHeader());
		// 行头有自定义表头的情况
		if (rhInfo.getColHeaderID() != null) {
			GroupableTableHeaderUI headerUI = (GroupableTableHeaderUI) m_dbTable
					.getTableHeader().getUI();

			int headerHeight1 = m_rowHeader.getTableHeaderHeight();
			// 先设为零保证下面取到的高度为表体列头的首选高度
			headerUI.setHeaderHeight(0);
			int headerHeight2 = headerUI.getHeaderHeight();

			int maxHeight = headerHeight1 > headerHeight2 ? headerHeight1
					: headerHeight2;
			headerUI.setHeaderHeight(maxHeight);
		}
		m_scrollPane.getRowHeader().setPreferredSize(
				m_rowHeader.getPreferredSize());
		m_scrollPane.revalidate();
	}

	/**
	 * 设置行头信息。 创建日期：(2003-11-16 13:07:27)
	 * 
	 * @param newRowHeaderInfo
	 *            nc.ui.reportquery.component.table.RowHeaderInfo
	 */
	public void setRowHeaderInfo(RowHeaderInfo newRowHeaderInfo) {
		if (newRowHeaderInfo == null) {
			return;
		}
		try {
			m_rowHeaderInfo = newRowHeaderInfo;
			m_rowHeader.initByRHInfo(m_rowHeaderInfo, getTopPanel()
					.getDefDsName());
			// 如果未定义行头格式，则需特殊处理
			if (m_crossType == QueryConst.CROSSTYPE_SIMPLE
					&& m_rowHeaderInfo.getRowHeaderID() == null
					&& getID() != null) {
				createDefaultRowHeader();
			}

			if (!isDesignState()) {
				m_rowHeader.setTable(m_dbTable);
			}
			m_scrollPane.setCorner(JScrollPane.UPPER_LEFT_CORNER, m_rowHeader
					.getTableHeader());
			// 行头有自定义表头的情况
			if (m_rowHeaderInfo.getColHeaderID() != null) {
				GroupableTableHeaderUI headerUI = (GroupableTableHeaderUI) m_dbTable
						.getTableHeader().getUI();

				int headerHeight1 = m_rowHeader.getTableHeaderHeight();
				// 先设为零保证下面取到的高度为表体列头的首选高度
				headerUI.setHeaderHeight(0);
				int headerHeight2 = headerUI.getHeaderHeight();
				int maxHeight = headerHeight1 > headerHeight2 ? headerHeight1
						: headerHeight2;
				headerUI.setHeaderHeight(maxHeight);
			}

			m_rowHeader.addComponentListener(new ComponentAdapter() {
				public void componentResized(ComponentEvent e) {
					m_scrollPane.getRowHeader().setPreferredSize(
							m_rowHeader.getPreferredSize());
				}
			});

			m_scrollPane.setRowHeaderView(m_rowHeader);
			m_scrollPane.getRowHeader().setPreferredSize(
					m_rowHeader.getPreferredSize());
			m_scrollPane.getRowHeader().setBackground(
					Style.getColor("Table.background"));
			m_scrollPane.revalidate();
		} catch (Exception e) {
			Logger.error(e);
			m_rowHeaderInfo = new RowHeaderInfo();
			setRowHeaderInfo(m_rowHeaderInfo);
		}
	}

	/**
	 * This fine grain notification tells listeners the exact range of cells,
	 * rows, or columns that changed.
	 * 
	 * @param e
	 *            TableModelEvent
	 */
	public void tableChanged(javax.swing.event.TableModelEvent e) {
		int type = e.getType();
		switch (type) {
		case TableModelEvent.DELETE: {
			delColumn(e);
			break;
		}
		case TableModelEvent.INSERT: {
			addColumn(e);
			break;
		}
		case TableModelEvent.UPDATE: {
			if (e.getColumn() == Integer.MAX_VALUE) {
				modifyColumn(e);
			}
			updateRowHeader();
			break;
		}
		default:
			break; // make the compiler happy
		}
	}

	/**
	 * 回退操作。 创建日期：(2003-12-17 10:42:27)
	 */
	public void popOperStep() {
		if (!m_backMenu.isEnabled()) {// 这是为了防止不通过菜单触发该操作
			return;
		}
		m_curDsFp = (DsFootPrint) m_actionStack.pop();
		m_crossType = m_curDsFp.getCrosstype();
		m_tableType = m_curDsFp.getTabletype();
		m_dynColInfo = m_curDsFp.getDynColInfo();
		m_allColInfos = null; // 迫使重新计算列格式
		if (m_tableType == SUBTOTAL) {
			m_aggregateDescriptor = m_curDsFp.getAggrDescriptor();
		}
		setDataset(m_curDsFp.getDs());
		bindDataSet(true);

		refreshColsAndHeader();

		if (isDSChanged()) {
			if (m_crossType == QueryConst.CROSSTYPE_SIMPLE) {
				refreshRowHeader(new RowHeaderInfo());
				if (m_tableType == SUBTOTAL) {
					bindDataSet(false);
					updateRowHeader();
				}
			} else {
				updateRowHeader();
			}
		} else {
			refreshRowHeader(m_rowHeaderInfo);
			if (m_crossType == QueryConst.CROSSTYPE_SIMPLE
					&& m_tableType == SUBTOTAL) {
				bindDataSet(false);
				updateRowHeader();
			}
		}
		refreshPenetrateItem();
		if (m_actionStack.isEmpty()) {
			m_backMenu.setEnabled(false);
		}
	}

	/**
	 * 穿透。 创建日期：(2003-12-17 13:11:27)
	 */
	public void doPenetrate() {
		if (!m_penetrateMenu.isEnabled()) {// 这是为了防止不通过菜单触发该操作
			return;
		}
		int rowLevel = ((RptTableModel) m_dbTable.getModel())
				.getRowLevel(m_dbTable.getSelectedRow());
		if (rowLevel != 0) {
			MessageDialog.showHintDlg(this, nc.ui.ml.NCLangRes.getInstance()
					.getStrByID("10241201", "UPP10241201-000060")/*
			 * @res "穿透"
			 */, nc.ui.ml.NCLangRes.getInstance().getStrByID("10241201",
					"UPP10241201-000074")/*
			 * @res "当前行不能穿透，请选择原始数据行穿透"
			 */);
			return;
		}
		try {
			int selectedRow = m_dbTable.getSelectedRow();
			//wanglei 2013-12-16
			iPenetrateRow = selectedRow;
			//wanlei 2013-12-17 
			hm.put(getID(), selectedRow);
			
			int selectedCol = m_dbTable.getSelectedColumn();
			SimpleCrossVO[] scs = getCurSimpleCrossVOs();
			if (selectedRow < 0) {
				MessageDialog.showHintDlg(this, nc.ui.ml.NCLangRes
						.getInstance().getStrByID("10241201",
								"UPP10241201-000060")/*
				 * @res "穿透"
				 */, nc.ui.ml.NCLangRes.getInstance().getStrByID("10241201",
						"UPP10241201-000075")/*
				 * @res "请选中某行进行穿透"
				 */);
				return;
			}
			// zjb+
			if (selectedCol < 0 && scs != null) {
				MessageDialog.showHintDlg(this, nc.ui.ml.NCLangRes
						.getInstance().getStrByID("10241201",
								"UPP10241201-000060")/*
				 * @res "穿透"
				 */, nc.ui.ml.NCLangRes.getInstance().getStrByID("10241201",
						"UPP10241201-000076")/*
				 * @res "请选中某单元格穿透"
				 */);
				return;
			}
			// 查询参数沿用
			Hashtable htParam = getTopPanel().getQryParamsByAlias(getID());

			PenetrateRuleVO[] prs = getCurrentQMD().getPenetrateRules();

			// V53+ 通过数据加工改变参数哈希表,使得一个查询对象能够使用另一个查询对象的穿透规则
			if (m_actionStack.size() == 0
					&& htParam.containsKey(QueryConst.PENE_RULE_ID)) {
				ParamVO paramPene = (ParamVO) htParam
						.get(QueryConst.PENE_RULE_ID);
				String queryId = paramPene.getValue();
				// 获得模型定义
				QueryModelDef qmdTemp = ModelUtil.getQueryDef(queryId,
						getTopPanel().getDefDsName());
				// 获得全部穿透规则定义
				prs = qmdTemp.getPenetrateRules();
			}

			PeneRuleDlg dlg = new PeneRuleDlg(this, getTopPanel()
					.getDefDsName(), false);
			dlg.setPenetrateRules(prs);
			dlg.showModal();
			dlg.destroy();
			if (dlg.getResult() == UIDialog.ID_OK) {
				int iSelRow = dlg.getSelRow();
				if (iSelRow == -1) {
					return;
				}

				// 停止监听
				m_enableListen.enabled = false;

				// 获得真实选中行
				int realRow = getOrigRowFromModel((RptTableModel) m_dbTable
						.getModel(), selectedRow);

				// 多值投影特殊处理(zjb+)
				if (scs != null) {
					SelectFldVO[] sfs = getCurrentQMD().getQueryBaseVO()
							.getSelectFlds();
					selectedCol = selectedCol / sfs.length;
					scs = ModelUtil.getRowColScs(scs, realRow, selectedCol);
				}

				String destFunCode = prs[iSelRow].getDestFunCode();

				// V53+ 穿透效率
				prs[iSelRow].setPrimaryKey(String.valueOf(iSelRow));

				if (destFunCode != null && !destFunCode.trim().equals("")) {
					// 穿透到业务节点
					String strErr = UIUtil.pene2Node(destFunCode, prs[iSelRow],
							htParam, scs, getPeneRow(), getID(), getTopPanel()
									.getDefDsName(), m_dbTable);
					// 恢复原监听标志
					m_enableListen.enabled = true;
					// 异常提示
					if (strErr != null) {
						MessageDialog.showWarningDlg(this, nc.ui.ml.NCLangRes
								.getInstance().getStrByID("10241201",
										"UPP10241201-000099")/*
						 * @res "查询引擎"
						 */, strErr);
						return;
					}
				} else {
					// 执行穿透(耗时)
					final PenetrateRuleVO pr = prs[iSelRow];
					final Hashtable htParam0 = htParam;
					final SimpleCrossVO[] scs0 = scs;
					new Thread(new Runnable() {
						public void run() {
							// 进度提示
							QEBannerDialog dialog = new QEBannerDialog(
									RQTable.this);
							dialog.setStartText(nc.ui.ml.NCLangRes
									.getInstance().getStrByID("10241201",
											"UPP10241201-001510")/*
							 * @res
							 * "穿透执行中，请稍候..."
							 */);
							dialog.setWorkThread(Thread.currentThread());
							dialog.setClosable(false);
							try {
								dialog.start();

								// 获得穿透结果
								IEmbedCodeUtil name = (IEmbedCodeUtil) NCLocator
										.getInstance().lookup(
												IEmbedCodeUtil.class.getName());
								QEDataSetAfterProcessDescriptor dsdwc = (QEDataSetAfterProcessDescriptor) name
										.penetrate(pr, htParam0, getPeneRow(),
												scs0, null);
								String queryID = dsdwc.getQmdID();
								QEDataSet sds = (QEDataSet) DatasetUtil
										.makeDataSetByQEDataSetDescriptor(dsdwc);
								QueryModelDef qmd = dsdwc.getQmd();
								if (qmd == null) {
									qmd = ModelUtil.getQueryDef(queryID,
											getTopPanel().getDefDsName());
								}
								// 恢复原监听标志
								m_enableListen.enabled = true;

								// 将当前数据集状态缓存
								pushOperStep();
								m_curDsFp = new DsFootPrint(sds, qmd);
								m_curDsFp.setRotateCrossVO(getCurrentQMD()
										.getRotateCross());
								m_curDsFp.setSimpleCrossVOs(getCurrentQMD()
										.getScs());
								// 绑定新数据集
								m_tableType = NORMAL;
								setDataset(sds);
								bindDataSet(true);
								// 清除动态列格式
								m_dynColInfo = new ArrayList();
								m_allColInfos = null; // 迫使重新计算列格式
								// 刷新表头
								m_crossType = ModelUtil
										.getCrossType(getCurrentQMD());
								m_curDsFp.setFldGrpVOs(getCurrentQMD()
										.getFldgroups());
								// 列头
								refreshColsAndHeader();
								// 行头
								refreshRowHeader(new RowHeaderInfo());
								// 穿透项
								refreshPenetrateItem();

								// V53+
								// 穿透后刷新参数项（没用，目标查询的参数哈希表是穿透规则里定的，甚至目标查询就是认为构造出来的数据集）
								Hashtable htQryParams = getTopPanel()
										.getHtQryParams();
								refreshEnvConst(getTopPanel(), htQryParams);
							} catch (Exception e) {
								Logger.error(e);
							} finally {
								dialog.end();
							}
						}
					}).start();
				}
			}
		} catch (Exception e) {
			Logger.error(e);
			Logger.error(e.getMessage(), e);
		}
	}

	/**
	 * 获得选中行哈希表 创建日期：(2003-10-8 12:31:48)
	 * 
	 * @return java.util.Hashtable
	 */
	private Hashtable getPeneRow() {
		Hashtable hashPeneRow = null;
		int iSelRow = m_dbTable.getSelectedRow();
		if (iSelRow != -1) {
			hashPeneRow = new Hashtable();
			RptTableModel model = (RptTableModel) m_dbTable.getModel();
			int iColCount = model.getColumnCount();
			for (int i = 0; i < iColCount; i++) {
				// 取值
				Object obj = model.getValueAt(iSelRow, i);
				// V55改 ZJB：在穿透到节点应用中，该穿透行取值将作为目标查询的同名参数取值，设为有长度空格将导致相应筛选条件起作用
				//String value = (obj == null) ? " " : obj.toString();
				String value = (obj == null) ? "" : obj.toString();
				// 列名
				String colName = model.getColumnIdentifier(i);
				// 加入
				hashPeneRow.put(colName, value);
			}

			// V55+ 增加选中列别名信息
			int iSelCol = m_dbTable.getSelectedColumn();
			if (iSelCol != -1) {
				String selColName = getBindDataSet().getColumn(iSelCol)
						.getColumnName();
				hashPeneRow.put(QueryConst.PENE_SELECTEDCOLUMN_KEY, selColName);
			}
		}
		return hashPeneRow;
	}

	/**
	 * 设计态（DesignState）初始化时刷新。 创建日期：(2003-12-15 9:44:27)
	 * by csli:修改查询定义后，格式设计中列信息的同步。
	 */
	private void refreshDesignState() {
		if (getID() != null) {
			ColInfo[] bakcolInfos = getColInfos();
			String bakheaderID = getHeaderId();
			RowHeaderInfo bakRHID = getRowHeaderInfo();
			setID(getID());
			// jl改 -- 列格式信息在第一次进入FormatPanel的时候是为空,这时不应该将空值设回，而应该引用绑定的查询对象之后的列格式
			if (bakcolInfos != null) {
				if(ArrayUtils.isEquals(bakcolInfos, m_colInfos)){
					setColInfos(bakcolInfos);
				} else {
					for (int x=0;x<m_colInfos.length;x++) {
						int index = ArrayUtils.indexOf(bakcolInfos, m_colInfos[x]);
						if (index > -1)
							m_colInfos[x] = bakcolInfos[index];
					}
					setColInfos(m_colInfos);
				}
			}else{
			//added by csli(09-02-12):在setColInfos方法中已经对headerId进行了设置
			setHeaderId(bakheaderID);
			}
			setRowHeaderInfo(bakRHID);
		} else {
			setColInfos(new ColInfo[0]);
			setHeaderId(null);
			setRowHeaderInfo(new RowHeaderInfo());
		}
	}


	/**
	 * 更新穿透按钮的状态。 创建日期：(2003-12-17 13:08:31)
	 */
	private void refreshPenetrateItem() {
		if (getCurrentQMD() == null) {
			m_penetrateMenu.setEnabled(false);
			return;
		}
		PenetrateRuleVO[] prs = getCurrentQMD().getPenetrateRules();
		boolean bPenetratable = prs != null && prs.length != 0;
		m_penetrateMenu.setEnabled(bPenetratable);
	}

	// 原始数据集
	private DataSet m_dsOrig = null;

	// 标志：判断绑定查询结果集是否初始旋转交叉
	// private boolean m_initRotated = false;

	/**
	 * 生成默认行头。 创建日期：(2003-12-18 10:17:53)
	 * 
	 * @throws Exception
	 */
	private void createDefaultRowHeader() throws Exception {
		SimpleCrossVO[] scs = ModelUtil.getRowColScs(getCurSimpleCrossVOs())[0];
		if (scs != null && scs.length > 0) {
			HeaderinfoVO hi = new HeaderinfoVO();
			// 整理节点数组
			int length = scs.length;
			HeaderdetailVO[] hds = new HeaderdetailVO[scs.length];
			for (int i = 0; i < length; i++) {
				hds[i] = new HeaderdetailVO();
				hds[i].setHeadercode(scs[i].getFldCode());
				hds[i].setHeadername(scs[i].getFldName());
			}
			hi.setHeaderdetails(hds);
			m_rowHeader.setHeaderInfoVO(hi);
		}
	}

	/**
	 * 获取当前绑定结果集的ID。 创建日期：(2003-12-19 11:34:05)
	 * 
	 * @return java.lang.String
	 */
	public String getRealID() {
		if (isDSChanged()) {
			return getName();
		} else {
			return getID();
		}
	}

	private String bakAggrInfo = "";

	private int bakTableType = NORMAL;

	private  Integer[] rowColors ;

	/**
	 * 此处插入方法说明。 创建日期：(2004-7-23 13:07:37)
	 * 
	 * @param rqComp
	 *            nc.ui.reportquery.component.RQCompBase
	 */
	void addSubCompMouseListener(RQCompBase rqComp) {
		m_dbTable.addMouseListener(rqComp);
		m_dbTable.addMouseMotionListener(rqComp);
		m_dbTable.getTableHeader().addMouseListener(rqComp);
		m_dbTable.getTableHeader().addMouseMotionListener(rqComp);
		m_scrollPane.getViewport().addMouseListener(rqComp);
		m_scrollPane.getViewport().addMouseMotionListener(rqComp);
		m_rowHeader.addMouseListener(rqComp);
		m_rowHeader.addMouseMotionListener(rqComp);
	}

	/**
	 * 由列格式得到对应的Excel格式。 创建日期：(2004-6-25 13:50:55)
	 * 
	 * @return org.apache.poi.hssf.usermodel.HSSFCellStyle
	 * @param style
	 *            org.apache.poi.hssf.usermodel.HSSFCellStyle
	 * @param colInfo
	 *            nc.vo.pub.querymodel.DataformatVO
	 * @param hssfFmt
	 *            HSSFDataFormat
	 */
	protected HSSFCellStyle buildStyleByDfmt(HSSFCellStyle style,
			ColInfo colInfo, HSSFDataFormat hssfFmt) {
		style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		style.setWrapText(true);
		if (colInfo == null) {
			return style;
		}

		DataformatVO dataFmt = colInfo.getColFormatVO(getTopPanel()
				.getDefDsName());
		if (dataFmt != null) {
			short fmt = 0;
			String strType = dataFmt.getFldtype();
			int iType = Integer.parseInt(strType);
			if (iType == ColFormatConstants.NUMBER_FIELD
					|| iType == ColFormatConstants.MONEY_FIELD
					|| iType == ColFormatConstants.PERCENT_FIELD) {
				fmt = hssfFmt.getFormat(FormatOutputUtil
						.procexcelFmtPattern(dataFmt));
			} else {
				fmt = HSSFDataFormat.getBuiltinFormat("General");
			}
			style.setDataFormat(fmt);

			String alignStr = dataFmt.getAlignment();

			int align = Integer.parseInt(alignStr);
			short colAlign = HSSFCellStyle.ALIGN_CENTER;
			if (align == SwingConstants.LEFT) {
				colAlign = HSSFCellStyle.ALIGN_LEFT;
			} else if (align == SwingConstants.RIGHT) {
				colAlign = HSSFCellStyle.ALIGN_RIGHT;
			}
			style.setAlignment(colAlign);
		}
		return style;
	}

	/**
	 * 定义旋转交叉
	 */
	public void doRCDef() {
		if (getID() == null || getID().trim().length() == 0) {
			MessageDialog.showWarningDlg(this, nc.ui.ml.NCLangRes.getInstance()
					.getStrByID("10241201", "UPP10241201-000077")/*
			 * @res
			 * "旋转交叉"
			 */, nc.ui.ml.NCLangRes.getInstance().getStrByID("10241201",
					"UPP10241201-000073")/*
			 * @res "请先绑定结果集"
			 */);
			return;
		}
		try {
			RotateCrossSetDlg dlg = new RotateCrossSetDlg(this);

			dlg.setRotateCross(getDefRotateCrossVO_inDlg(), getTopPanel()
					.getSelectFldsByAlias(getID()));
			dlg.showModal();
			dlg.destroy();
			if (dlg.getResult() == UIDialog.ID_OK) {
				setDefRotateCrossVO(dlg.getRotateCross());
				setDefRotateCrossVO_inDlg(dlg.getRotateCross());
			}
		} catch (Exception e) {
			Logger.error(e);
		}
	}

	/**
	 * 旋转交叉(运行态)。 创建日期：(2003-12-12 14:20:34)
	 */
	public void doRotateCross(boolean showDefCrossDlg) {
		if (getBindDataSet() == null) {
			return;
		}

		QEDataSet origSds = null;
		if (m_crossType == QueryConst.CROSSTYPE_ROTATE) {
			DsFootPrint dsFp = (DsFootPrint) m_actionStack.peek();
			origSds = (QEDataSet) dsFp.getDs();
		} else {
			origSds = (QEDataSet) getBindDataSet();
		}
		if (origSds == null || origSds.isEmpty())
			return;
		RotateCrossVO rc = null;
		if (showDefCrossDlg) {
			RotateCrossSetDlg dlg = new RotateCrossSetDlg(this);
			dlg.setRotateCross(getCurRotateCrossVO(), DatasetUtil
					.getSelectFldByDataset(origSds.getColumns()));
			dlg.showModal();
			dlg.destroy();
			if (dlg.getResult() != UIDialog.ID_OK) {
				return;
			}
			rc = dlg.getRotateCross();
		} else {
			rc = getDefRotateCrossVO();
		}

		if (rc != null) {
			// 执行交叉
			try {
				// 停止监听
				m_enableListen.enabled = false;
				if (m_crossType != QueryConst.CROSSTYPE_ROTATE) {
					pushOperStep();
					m_dynColInfo = (ArrayList) getDynColInfo().clone();
				}
				Object[] objs = DatasetUtil.getDatasetForRotateCross(rc,
						origSds);
				QEDataSet sds = (QEDataSet) objs[0];
				// 篡改列名
				int iRowCount = (rc == null || rc.getStrRows() == null) ? 0
						: rc.getStrRows().length;
				DatasetUtil.tamperColname(sds, iRowCount);
				// 恢复原监听标志
				m_enableListen.enabled = true;

				m_curDsFp = new DsFootPrint(sds, getCurrentQMD());
				m_curDsFp.setRotateCrossVO(rc);
				m_tableType = NORMAL;

				// 旋转交叉合计行(zjb)
				if (m_aggregateDescriptor != null) {
					m_tableType = ROTATE_SUBTOTAL;
				}

				setDataset(sds);
				bindDataSet(true);

				// 旋转交叉列头处理
				FldgroupVO[] fldgroups = (FldgroupVO[]) objs[1];
				m_curDsFp.setFldGrpVOs(fldgroups);
				m_crossType = QueryConst.CROSSTYPE_ROTATE;
				refreshColsAndHeader();

				// 行头
				refreshRowHeader(new RowHeaderInfo());

				refreshPenetrateItem();
			} catch (Exception e) {
				Logger.error(e);
			}
		}
	}

	/**
	 * 投影交叉定义(设计态)
	 */
	public void doSCDef() {
		if (getID() == null || getID().trim().length() == 0) {
			MessageDialog.showWarningDlg(this, nc.ui.ml.NCLangRes.getInstance()
					.getStrByID("10241201", "UPP10241201-000078")/*
			 * @res
			 * "简单交叉"
			 */, nc.ui.ml.NCLangRes.getInstance().getStrByID("10241201",
					"UPP10241201-000073")/*
			 * @res "请先绑定结果集"
			 */);
			return;
		}
		if (getDefSimpleCrossVO_inDlg() == null
				|| getDefSimpleCrossVO_inDlg().length == 0) {
			MessageDialog.showWarningDlg(this, nc.ui.ml.NCLangRes.getInstance()
					.getStrByID("10241201", "UPP10241201-000078")/*
			 * @res
			 * "简单交叉"
			 */, nc.ui.ml.NCLangRes.getInstance().getStrByID("10241201",
					"UPP10241201-000079")/*
			 * @res "交叉定义数据有错！"
			 */);
			return;
		}

		// zjb+
		if (ModelUtil.isMergeQuery(getTopPanel().getQryDefByAlias(getID()))) {
			MessageDialog.showWarningDlg(this, nc.ui.ml.NCLangRes.getInstance()
					.getStrByID("10241201", "UPP10241201-000078")/*
			 * @res
			 * "简单交叉"
			 */, nc.ui.ml.NCLangRes.getInstance().getStrByID("10241201",
					"UPP10241201-000080")/*
			 * @res "合并查询不支持再交叉"
			 */);
			return;
		}

		SimpleCrossVO[] newScs = new SimpleCrossVO[getDefSimpleCrossVO_inDlg().length];
		for (int i = 0; i < getDefSimpleCrossVO_inDlg().length; i++) {
			newScs[i] = (SimpleCrossVO) getDefSimpleCrossVO_inDlg()[i].clone();
		}
		SimpleCrossSetDlg dlg = new SimpleCrossSetDlg(this);
		dlg.setScs(newScs);
		dlg.showModal();
		dlg.destroy();
		if (dlg.getResult() == UIDialog.ID_OK) {
			setDefSimpleCrossVO(dlg.getScs());
			setDefSimpleCrossVO_inDlg(dlg.getScs());
		}
	}

	/**
	 * 投影交叉(运行态)。 创建日期：(2003-12-12 14:20:34)
	 */
	public void doSimpleCross(boolean showDefCrossDlg) {
		SimpleCrossVO[] scs = showDefCrossDlg ? getCurSimpleCrossVOs()
				: getDefSimpleCrossVO();
		if (scs == null || scs.length == 0) {
			MessageDialog.showWarningDlg(this, nc.ui.ml.NCLangRes.getInstance()
					.getStrByID("10241201", "UPP10241201-000078")/*
			 * @res
			 * "简单交叉"
			 */, nc.ui.ml.NCLangRes.getInstance().getStrByID("10241201",
					"UPP10241201-000079")/*
			 * @res "交叉定义数据有错！"
			 */);
			return;
		}

		// zjb+
		if (ModelUtil.isMergeQuery(getCurrentQMD())) {
			MessageDialog.showWarningDlg(this, nc.ui.ml.NCLangRes.getInstance()
					.getStrByID("10241201", "UPP10241201-000078")/*
			 * @res
			 * "简单交叉"
			 */, nc.ui.ml.NCLangRes.getInstance().getStrByID("10241201",
					"UPP10241201-000080")/*
			 * @res "合并查询不支持再交叉"
			 */);
			return;
		}

		SimpleCrossVO[] newScs = new SimpleCrossVO[scs.length];
		for (int i = 0; i < scs.length; i++) {
			newScs[i] = (SimpleCrossVO) scs[i].clone();
		}
		if (showDefCrossDlg) {
			SimpleCrossSetDlg dlg = new SimpleCrossSetDlg(this);
			dlg.setScs(newScs);
			dlg.showModal();
			dlg.destroy();
			if (dlg.getResult() != UIDialog.ID_OK) {
				return;
			}
			newScs = dlg.getScs();
		}
		// 执行交叉
		try {
			// 查询参数沿用
			Hashtable htParam = getTopPanel().getQryParamsByAlias(getID());
			QEDataSet sds = (QEDataSet) DatasetUtil.getDatasetForSimpleCross(
					newScs, getCurrentQMD().getQueryBaseDef(), htParam);

			pushOperStep();
			m_curDsFp = new DsFootPrint(sds, getCurrentQMD());
			m_curDsFp.setSimpleCrossVOs(newScs);
			m_tableType = NORMAL;
			setDataset(sds);
			bindDataSet(true);

			m_crossType = QueryConst.CROSSTYPE_SIMPLE;
			refreshColsAndHeader();
			// 行头
			refreshRowHeader(new RowHeaderInfo());

			refreshPenetrateItem();
		} catch (Exception e) {
			Logger.error(e);
		}
	}

	/**
	 * 导出为Excel工作薄中的一个表单。 创建日期：(2004-6-23 10:28:38)
	 * 
	 * @param workbook
	 *            org.apache.poi.hssf.usermodel.HSSFWorkbook
	 * @i18n upp08110600604=[查询引擎] 导出Excel--成功导出
	 * @i18n upp08110600605=条数据
	 */
	// public void export2Excel(File file) { // V502+
	public void export2Excel(File file, int maxRow) {
		if (getBindDataSet() == null) {
			return;
		}
		// 多行头列数
		int rowHeaderColNum = m_rowHeader.getColumnCount();
		// 表体列数
		int tableColNum = m_dbTable.getColumnCount();
		// 总列数
		int totalColNum = rowHeaderColNum + tableColNum;

		// 输出流
		FileOutputStream out = null;
		HSSFWorkbook workbook = new HSSFWorkbook();

		HSSFSheet sheet = workbook.createSheet(getName());
		// 构造显示的列格式信息
		ColInfo[] showColInfos = getShowColInfos(totalColNum, rowHeaderColNum);
		// 构建表头
		int iHeaderRowCount = makeHeader(workbook, sheet, showColInfos);
		// 表体行数
		int iBodyRowCount = m_dbTable.getRowCount();

		HSSFCellStyle headerStyle = getHeaderStyle(workbook);

		// HSSFRow[] bodyRows = new HSSFRow[iBodyRowCount];
		// 合并行头
		CellSpan cellSpan = (CellSpan) ((AttributiveCellTableModel) m_rowHeader
				.getModel()).getCellAttribute();

		HSSFDataFormat hssfFmt = workbook.createDataFormat();
		// 表体的cell列格式
		//HSSFCellStyle[] bodyStyle = new HSSFCellStyle[tableColNum];

		// int maxExcelRowCount =
		// getTopPanel().getPreference().getMaxExcelRowCount();
		
		//modified by csli(09-02-16):去除冗余代码
//		for (int i = 0; i < tableColNum; i++) {
//			bodyStyle[i] = workbook.createCellStyle();
//			// +V5.5 Update 添加列格式颜色 yza 2008-4-30
//			bodyStyle[i] = buildStyleByDfmt(workbook, bodyStyle[i],
//					showColInfos[i + rowHeaderColNum], hssfFmt);
//			// bodyStyle[i] = buildStyleByDfmt(bodyStyle[i], showColInfos[i
//			// + rowHeaderColNum], hssfFmt);
//		}
		Object cellObj;
		int linecount = 0;
		int filecount = 1;
		boolean isPaging = false;
		// 一列的最大宽度
		int[] maxColWidth = new int[totalColNum];
		String cellObjStr = null;
		//add by csli:解决导出excel时，列格式中小数位设置失效问题
		//获取列格式Renderer数组,避免每次都要执行远程调用
		RptTableFormatedRenderer[] formatArr = new RptTableFormatedRenderer[totalColNum];
		for(int f=0;f<totalColNum;f++){
			DataformatVO format=	showColInfos[f].getColFormatVO(getTopPanel().getDefDsName());
			if (format != null) {
				formatArr[f] = new RptTableFormatedRenderer();
				formatArr[f].initLabel(format);
			}
		}
		// 开始工作
		for (int i = 0; i < iBodyRowCount; i++) {
			if (linecount >= maxRow) {
				try {
					// 重新构造文件
					String filepath = file.getAbsolutePath();
					String fullName = filepath.substring(0, filepath.lastIndexOf(".")) + "_" + filecount + ".xls";
					File newfile = new File(fullName);
					if (newfile.exists()) {
						int result = MessageDialog.showOkCancelDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID(
								"10241201", "UPP10241201-000108"), nc.ui.ml.NCLangRes.getInstance().getStrByID(
								"10241201", "UPP10241201-001567", null, new String[] { fullName }));
						if (result == MessageDialog.ID_CANCEL)
							break;
					}
					newfile.createNewFile();
					 // if (!file.exists()) {
					// file.createNewFile();
					// }
					out = new FileOutputStream(newfile, true);
					workbook.write(out);
					Logger.debug("[查询引擎] 导出Excel--成功导出" + i + "条数据");
					out.close();
				} catch (FileNotFoundException e) {
					Logger.error(e);
				} catch (IOException e) {
					Logger.error(e);
				}
				workbook = new HSSFWorkbook();
				sheet = workbook.createSheet(getName());
				iHeaderRowCount = makeHeader(workbook, sheet, showColInfos);
				// 清空行数计数器
				linecount = 0;
				// 文件计数器增加1
				filecount++;
				// 分页标志
				isPaging = true;
				//by csli@V56:解决分文件导出的时候，非首个文件中行头格式丢失
				headerStyle = getHeaderStyle(workbook);
				try {
//					System.gc();不得显式调用 liuyy+
					Thread.sleep(100);
				} catch (InterruptedException e) {
					Logger.error(e);
				}
			}
			// edit by csli(08-12-16):解决内存溢出问题
			HSSFRow bodyRow = sheet.createRow(linecount + iHeaderRowCount);
			for (int j = 0; j < totalColNum; j++) {
				// 处理行表头的情况
				if (j < rowHeaderColNum) {
					cellObj = m_rowHeader.getValueAt(i, j);
					int[] span = cellSpan.getSpan(i, j);
					if (span[0] < 0 || span[1] < 0) {
						continue;
					}
					cellObjStr = cellObj == null ? "" : cellObj.toString();
					maxColWidth[j] = UIUtil.getMaxColWidth(cellObjStr,
							m_dbTable, null, sheet.getColumnWidth((short) j));
					HSSFCell cell = bodyRow.createCell((short) j);
					// cell.setCellValue(cellObj != null ? cellObj.toString()
					// : "");
					cell.setCellValue(new HSSFRichTextString(
							cellObj != null ? cellObj.toString() : ""));
					// 行表头的格式跟表头格式保持一致
					cell.setCellStyle(headerStyle);
					if (span[0] > 1 || span[1] > 1) {
						sheet.addMergedRegion(new Region(iHeaderRowCount + i,
								(short) j, iHeaderRowCount + i + span[0] - 1,
								(short) (j + span[1] - 1)));
					}
				} else {
					cellObj = m_dbTable.getValueAt(i, j - rowHeaderColNum);
					if (cellObj == null) {
						continue;
					}
					cellObjStr = cellObj.toString();
					HSSFCell cell = bodyRow.createCell((short) j);
					if (formatArr[j] == null) {// 如果无列格式信息
						if (cellObj instanceof Integer) {
							cell.setCellValue(Double.parseDouble(cellObj.toString()));
						} else if (cellObj instanceof Double) {
							cell.setCellValue(((Double) cellObj).doubleValue());
						} else if (cellObj instanceof java.math.BigDecimal) {
							cell.setCellValue(((java.math.BigDecimal) cellObj).doubleValue());
						} else {
							cell.setCellValue(new HSSFRichTextString(cellObj.toString()));
						}
					} else {
						formatArr[j].setValue(cellObj);
						cell.setCellValue(new HSSFRichTextString(formatArr[j].getText()));
					}
					maxColWidth[j] = UIUtil.getMaxColWidth(cellObjStr,
							m_dbTable, showColInfos[j], sheet
									.getColumnWidth((short) j));
					// cell.setCellStyle(bodyStyle[j - rowHeaderColNum]);
					// +V5.5 yza 预警格式丢失问题
					HSSFCellStyle rowStyle = buildRowColorStyle(workbook,
							showColInfos[j], hssfFmt, cellObj);
					if (rowStyle != null) {
						cell.setCellStyle(rowStyle);
					} else {
//						cell.setCellStyle(bodyStyle[j - rowHeaderColNum]); liuyy+ 过多设置style会导致导出excel编辑很慢。
					}
				}

			}
			linecount++;
		}
		// 锁定行头
		sheet.createFreezePane(rowHeaderColNum, iHeaderRowCount);
		// 设置列宽
		for (int i = 0; i < maxColWidth.length; i++) {
			// 最小列宽
			if (maxColWidth[i] < 20)
				maxColWidth[i] = 20;
			sheet
					.setColumnWidth((short) i,
							(short) ((maxColWidth[i] + 5) * 50));
		}
		String filename = file.getAbsolutePath();
		// 最终写出
		String hintMsg = null;
		if (isPaging) {
			filename = filename.substring(0, filename.lastIndexOf("."))
					+"_"+ filecount + ".xls";
			File newfile = new File(filename);
			if (newfile.exists()) {
				int result = MessageDialog.showOkCancelDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"10241201", "UPP10241201-000108"), nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"10241201", "UPP10241201-001567", null, new String[] { filename }));
				if (result == MessageDialog.ID_CANCEL)
				return;
			}
			hintMsg = nc.ui.ml.NCLangRes.getInstance().getStrByID("10241201",
					"UPP10241201-001335", null,
					new String[] { String.valueOf(filecount) });
			// 避免服务器端导出的时候弹框
			if (!RuntimeEnv.getInstance().isRunningInServer()) {
				MessageDialog.showWarningDlg(this, nc.ui.ml.NCLangRes
						.getInstance().getStrByID("10241201",
								"UPP10241201-000099")/* @res "查询引擎" */,
						hintMsg);
			}
		}
		try {
			out = new FileOutputStream(filename);
			workbook.write(out);
			hintMsg = nc.ui.ml.NCLangRes.getInstance().getStrByID("10241201",
					"UPP10241201-001337")/* @res "导出成功" */;
			if (!RuntimeEnv.getInstance().isRunningInServer()) {
				MessageDialog.showHintDlg(this, nc.ui.ml.NCLangRes
						.getInstance().getStrByID("10241201",
								"UPP10241201-000099")/* @res "查询引擎" */,
						hintMsg);
			}
		} catch (FileNotFoundException e) {
			Logger.error(e);
			if (!RuntimeEnv.getInstance().isRunningInServer()) {
				MessageDialog.showWarningDlg(this, nc.ui.ml.NCLangRes
						.getInstance().getStrByID("10241201",
								"UPP10241201-000099")/* @res "查询引擎" */,
						nc.ui.ml.NCLangRes.getInstance().getStrByID("10241201",
								"UPP10241201-001338")/* @res "导出有误" */);
			}
			return;
		} catch (IOException e) {
			Logger.error(e);
			if (!RuntimeEnv.getInstance().isRunningInServer()) {
				MessageDialog.showWarningDlg(this, nc.ui.ml.NCLangRes
						.getInstance().getStrByID("10241201",
								"UPP10241201-000099")/* @res "查询引擎" */,
						nc.ui.ml.NCLangRes.getInstance().getStrByID("10241201",
								"UPP10241201-001339")/* @res "IO错误" */);
			}
			return;
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					Logger.error(e);
				}
			}
		}
	}

	/**
	 * 构造表头 created on 2006-3-30
	 * 
	 * @param workbook
	 *            <strong>最后修改人: jl<strong> <strong>最后修改日期: 2006-3-30<stong>
	 */
	private int makeHeader(HSSFWorkbook workbook, HSSFSheet sheet,
			ColInfo[] showColInfos) {
		// 多行头列数
		int rowHeaderColNum = m_rowHeader.getColumnCount();
		// 表体列数
		int tableColNum = m_dbTable.getColumnCount();
		// 总列数
		int totalColNum = rowHeaderColNum + tableColNum;

		// 解析多行头表头信息(即ScrollPane左上角的corner)
		JTableHeader corner = m_rowHeader.getTableHeader();
		boolean isGroupHeader = false;
		if (corner instanceof GroupableTableHeader) {
			isGroupHeader = true;

			// 得到列信息
			// 哈希已经设定的列格式，方便查找
		}
		Object[] objCGs = new Object[totalColNum];
		int[] iCGs = new int[totalColNum]; // 记录某一列的多表头层数

		HSSFCellStyle headerStyle = getHeaderStyle(workbook);

		// 表头的最大行数（将取两个表头的最大值）
		int iHeaderRowCount = getHeaderGroupInfo(iCGs, objCGs, false);

		HSSFRow[] headerRows = new HSSFRow[iHeaderRowCount];
		for (int i = 0; i < iHeaderRowCount; i++) {
			headerRows[i] = sheet.createRow(i);
		}

		int col = 0;
		if (!isGroupHeader) { // 未设行头的表头
			col = rowHeaderColNum;
			HSSFCell cell = headerRows[0].createCell((short) 0);
			cell.setCellValue(new HSSFRichTextString(""));
			sheet.addMergedRegion(new Region(0, (short) 0, iHeaderRowCount - 1,
					(short) (rowHeaderColNum - 1)));
		}
		// 纵向合并表头
		for (int j = col; j < totalColNum; j++) {
			HSSFCell cell = headerRows[iCGs[j]].createCell((short) j);
			cell.setCellValue(new HSSFRichTextString(showColInfos[j].colName));
			cell.setCellStyle(headerStyle);
			if (iCGs[j] < iHeaderRowCount - 1) {
				sheet.addMergedRegion(new Region(iCGs[j], (short) j,
						iHeaderRowCount - 1, (short) j));
			}
			// sheet.setColumnWidth((short)j,(short)(showColInfos[j].colWidth*50));
		}
		// 横向合并表头
		int k = 0;
		for (int i = 0; i < iHeaderRowCount - 1; i++) {
			for (int j = 0; j < totalColNum; j++) {
				if (iCGs[j] <= i) {
					continue;
				}
				String strGroupName_J = ((ColumnGroup) ((Vector) objCGs[j])
						.elementAt(i)).getHeaderValue().toString();
				for (k = j + 1; k < totalColNum; k++) {
					if (iCGs[k] <= i) {
						break;
					}
					if (((Vector) objCGs[j]).elementAt(i) != ((Vector) objCGs[k])
							.elementAt(i)) {
						break;
					}
				}
				HSSFCell cell = headerRows[i].createCell((short) j);
				cell.setCellValue(new HSSFRichTextString(strGroupName_J));
				cell.setCellStyle(headerStyle);
				if (k > j + 1) {
					sheet.addMergedRegion(new Region(i, (short) j, i,
							(short) (k - 1)));
					j = k - 1;
				}
			}
		}
		return iHeaderRowCount;
	}

	/**
	 * 构造表头格式 created on 2006-3-30
	 * 
	 * @param workbook
	 * @return <strong>最后修改人: jl<strong> <strong>最后修改日期: 2006-3-30<stong>
	 */
	private HSSFCellStyle getHeaderStyle(HSSFWorkbook workbook) {
		// 构造表头字体
		HSSFFont headerFont = workbook.createFont();
		headerFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		headerFont.setColor(HSSFColor.WHITE.index);
		// 表头格式
		HSSFCellStyle headerStyle = workbook.createCellStyle();
		headerStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		headerStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		headerStyle.setFont(headerFont);
		// 填充背景色
		headerStyle.setFillForegroundColor(HSSFColor.BLUE_GREY.index);
		headerStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		headerStyle.setWrapText(true);

		return headerStyle;
	}

	/**
	 * 获取显示在界面上的所有列格式信息 这里的所有列格式包括 固定列+公式列+行表头表的列 两个参数分别表示所有列的数目和行表头表列的数目
	 * created on 2006-3-30
	 * 
	 * @param totalColNum
	 * @param rowHeaderColNum
	 * @return <strong>最后修改人: jl<strong> <strong>最后修改日期: 2006-3-30<stong>
	 */
	private ColInfo[] getShowColInfos(int totalColNum, int rowHeaderColNum) {
		ColInfo[] colInfos = getAllColInfos();
		Hashtable htColInfos = new Hashtable();
		if (colInfos != null) {
			for (int i = 0; i < colInfos.length; i++) {
				htColInfos.put(colInfos[i].colAlias.toUpperCase(), colInfos[i]);
			}
		}
		ColInfo[] showColInfos = new ColInfo[totalColNum];
		TableColumnModel tcm = m_rowHeader.getColumnModel();
		for (int i = 0; i < rowHeaderColNum; i++) {
			TableColumn tc = tcm.getColumn(i);
			showColInfos[i] = new ColInfo();
			showColInfos[i].colAlias = tc.getIdentifier() == null ? "" : tc
					.getIdentifier().toString();
			showColInfos[i].colName = tc.getHeaderValue() == null ? "" : tc
					.getHeaderValue().toString();
		}
		tcm = m_dbTable.getColumnModel();
		for (int i = rowHeaderColNum; i < totalColNum; i++) {
			TableColumn tc = tcm.getColumn(i - rowHeaderColNum);
			String identifier = (tc.getIdentifier() == null) ? "NULL" : tc
					.getIdentifier().toString().toUpperCase();
			if (tc.getIdentifier() != null
					&& htColInfos.containsKey(identifier)) {
				showColInfos[i] = (ColInfo) htColInfos.get(identifier);
			}
			// jl+
			else if (htColAliasColInfo.containsKey(identifier)) {
				try {
					showColInfos[i] = (ColInfo) ((ColInfo) htColAliasColInfo
							.get(identifier)).clone();
				} catch (CloneNotSupportedException e) {
					Logger.error(e);
					showColInfos[i] = new ColInfo();
				}
				showColInfos[i].colAlias = tc.getIdentifier() == null ? "" : tc
						.getIdentifier().toString();
				showColInfos[i].colName = tc.getHeaderValue() == null ? "" : tc
						.getHeaderValue().toString();
			} else {
				showColInfos[i] = new ColInfo();
				showColInfos[i].colAlias = tc.getIdentifier() == null ? "" : tc
						.getIdentifier().toString();
				showColInfos[i].colName = tc.getHeaderValue() == null ? "" : tc
						.getHeaderValue().toString();
			}
		}
		return showColInfos;
	}

	/**
	 * 所有列信息（含固定列和公式列,运行态才用到此方法）。 所有列==固定列+公式列
	 * 
	 * @return nc.ui.reportquery.component.table.ColInfo[]
	 */
	public ColInfo[] getAllColInfos() {
		if (m_allColInfos == null) {
			if (getDynColInfo().size() == 0) {
				m_allColInfos = m_colInfos == null ? new ColInfo[0]
						: m_colInfos;
			} else {
				int length = m_colInfos == null ? 0 : m_colInfos.length;
				m_allColInfos = new ColInfo[length + getDynColInfo().size()];
				System.arraycopy(m_colInfos, 0, m_allColInfos, 0, length);
				for (int i = 0; i < getDynColInfo().size(); i++) {
					m_allColInfos[length + i] = (ColInfo) getDynColInfo()
							.get(i);
				}
			}
		}
		return m_allColInfos;
	}

	/**
	 * 得到表体打印数据。 创建日期：(2003-9-29 15:16:08)
	 * 
	 * @return nc.ui.pub.print.datastruct.PrintCellData[][]
	 * @param colWidths
	 *            int[]
	 * @param showColInfos
	 *            ColInfo[]
	 * @param bNeedAlterRowNum
	 *            boolean
	 */
	private PrintCellData[][] getBodyCellData(int[] colWidths,
			ColInfo[] showColInfos, boolean bNeedAlterRowNum) {
		FmdPrintInfoNew printInfo = getTopPanel().getPrintInfo();

		String[] fontinfo = printInfo.getFontBody();

		Font font = UIUtil.getFontFromStr(fontinfo);
		// 多行头列数
		int rowHeaderColNum = m_rowHeader.getColumnCount();
		// 表体列数
		int tableColNum = m_dbTable.getColumnCount();
		// 总列数
		int totalColNum = colWidths.length;

		// 行数
		int iBodyRowCount = m_dbTable.getRowCount();

		PrintCellData[][] cellses = new PrintCellData[iBodyRowCount][totalColNum];
		for (int i = 0; i < iBodyRowCount; i++) {
			cellses[i] = new PrintCellData[totalColNum];
		}
		// 这里所产生的随机数是为了设置合并单元格的时候给被合并的单元格设置同一个parentID所用
		// 如果是两张不同的表，则他们的name不同，所以哈希码就不会相同，而我们一个格式设计中的表
		// 不允许重名，这样就可以在使用同时放置两张表的时候打印合并的单元格的parentID不会指向同
		// 一个，+1的意思是为了跟getHeaderCellData()方法中的同样的为同样目的而产生的随机数区
		// 分开。
		int random = getName().hashCode() + 1;
		// 行头打印格式
		CellSpan cellSpan = (CellSpan) ((AttributiveCellTableModel) m_rowHeader
				.getModel()).getCellAttribute();
		int startCol = (bNeedAlterRowNum && m_rowHeader.isRowNumAtFront()) ? 1
				: 0;
		int endCol = (bNeedAlterRowNum && !m_rowHeader.isRowNumAtFront()) ? rowHeaderColNum - 1
				: rowHeaderColNum;
		for (int i = 0; i < iBodyRowCount; i++) {
			for (int j = startCol; j < endCol; j++) {
				PrintCellData cell = new PrintCellData();
				cell.setCellSize(
						(int) (colWidths[j - startCol] * PRINTZOOMRATE),
						ROWHEIGHT);
				cell.setContType(0);
				Object obj = m_rowHeader.getValueAt(i, j);
				int[] span = cellSpan.getSpan(i, j);
				if (span[0] > 1 || span[1] > 1) {
					cell.setCombined(true);
					cell.setParentID(100 * i + j + random);
				} else if (span[0] < 0 || span[1] < 0) {
					int row = i + span[0];
					int column = j + span[1];
					cell.setCombined(true);
					cell.setParentID(100 * row + column + random);
					obj = m_rowHeader.getValueAt(row, column);
				}
				cell.setHorAlign(1);
				cell.setVerAlign(1);
				cell.setStr(obj != null ? obj.toString() : "");
				cell.setStrMode(0); // QQ
				cell.setCellColor(Color.white, Color.black); // QQ
				cell.setLineVisible(true, true, true, true); // QQ
				cell.setDataRela(null);
				cell.setLineType(new int[] { 1, 1, 1, 1 }); // QQ
				cell.setLineColor(new Color[] { Color.black, Color.black,
						Color.black, Color.black });
				cell.setFont(font);

				cellses[i][j - startCol] = cell;
			}
		}
		rowHeaderColNum = endCol - startCol;

		// 表体打印格式
		for (int j = 0; j < tableColNum; j++) {
			DataformatVO format = null;
			RptTableFormatedRenderer rcr = null;

			format = showColInfos[j + rowHeaderColNum]
					.getColFormatVO(getTopPanel().getDefDsName());
			if (format != null) {
				rcr = new RptTableFormatedRenderer();
				rcr.initLabel(format);
			}
			boolean isNumType = QueryUtil.isNumberType(showColInfos[j
					+ rowHeaderColNum].colType);

			for (int i = 0; i < iBodyRowCount; i++) {
				PrintCellData cell = new PrintCellData();
				cell.setCellSize(
						(int) (colWidths[j + rowHeaderColNum] * PRINTZOOMRATE),
						ROWHEIGHT);
				// 待测
				cell.setContType(0); // 纯文本
				// cell.setIsNumber(isNumType);
				Object obj = m_dbTable.getValueAt(i, j);
				String str = "";
				int iColAlign = isNumType ? 2 : 0;

				if (format != null) {
					rcr.setValue(obj);
					str = rcr.getText();

					String alignStr = format.getAlignment();
					int align = Integer.parseInt(alignStr);
					if (align == SwingConstants.LEFT) {
						iColAlign = 0;
					} else if (align == SwingConstants.RIGHT) {
						iColAlign = 2;
					} else {
						iColAlign = 1;
					}

					cell.setCellColor(rcr.getBackground(), rcr.getForeground());
				} else {
					if (obj != null) {
						str = obj.toString();
					} else {
						str = "";
					}
				}

				cell.setHorAlign(iColAlign);
				// 将0显示为空串，前提是str必须是数字组成的数组
				if (printInfo.isShowZeroLikeNulls() && isNumType
						&& StringUtils.isNotBlank(str)) {
					BigDecimal decimal = new BigDecimal(str);
					if (decimal.doubleValue() == 0) {
						str = "";
					}
				}

				cell.setStr(str);
				cell.setStrMode(0); // QQ
				cell.setCellColor(Color.white, Color.black); // QQ
				cell.setLineVisible(true, true, true, true); // QQ
				cell.setDataRela(null);
				cell.setLineType(new int[] { 1, 1, 1, 1 }); // QQ
				cell.setLineColor(new Color[] { Color.black, Color.black,
						Color.black, Color.black });
				cell.setFont(font);

				cellses[i][j + rowHeaderColNum] = cell;
			}
		}

		return cellses;
	}

	/**
	 * 得到当前设置的旋转交叉VO。 创建日期：(2003-12-16 16:00:41)
	 * 
	 * @return nc.vo.pub.querymodel.RotateCrossVO
	 */
	private RotateCrossVO getCurRotateCrossVO() {
		return m_curDsFp.getRotateCrossVO();
	}

	/**
	 * 得到当前设置的投影交叉VO。 创建日期：(2003-12-23 11:14:47)
	 * 
	 * @return nc.vo.pub.querymodel.SimpleCrossVO[]
	 */
	private SimpleCrossVO[] getCurSimpleCrossVOs() {
		if (isDesignState()) {
			QueryModelDef qmd = getTopPanel().getQryDefByAlias(getID());
			return qmd.getQueryBaseVO().getScs();
		}
		return m_curDsFp.getSimpleCrossVOs();
	}

	/**
	 * 取得动态列信息（即公式列）。 创建日期：(2003-12-20 8:46:30)
	 * 
	 * @return java.util.ArrayList
	 */
	private ArrayList getDynColInfo() {
		if (m_dynColInfo == null) {
			m_dynColInfo = new ArrayList();
		}
		return m_dynColInfo;
	}

	/**
	 * 组织表头打印数据（包括多行头的表头和表体的表头）。 创建日期：(2003-9-29 15:16:08)
	 * 
	 * @return nc.ui.pub.print.datastruct.PrintCellData[][]
	 * @param colWidths
	 *            int[]
	 * @param showColInfos
	 *            java.lang.String[]
	 * @param bNeedAlterRowNum
	 *            boolean
	 */
	private PrintCellData[][] getHeaderCellData(int[] colWidths,
			ColInfo[] showColInfos, boolean bNeedAlterRowNum) {

		// 表体列数
		int tableColNum = m_dbTable.getColumnCount();
		// 总列数
		int totalColNum = colWidths.length;
		// 多行头列数
		int rowHeaderColNum = totalColNum - tableColNum;

		Font font = new Font("Dialog", Font.PLAIN, 14);

		// 解析多行头表头信息(即ScrollPane左上角的corner)
		JTableHeader corner = m_rowHeader.getTableHeader();
		boolean isGroupHeader = false;
		if (corner instanceof GroupableTableHeader) {
			isGroupHeader = true;

		}
		Object[] objCGs = new Object[totalColNum];
		int[] iCGs = new int[totalColNum]; // 记录某一列的多表头层数

		// 表头的最大行数（将取两个表头的最大值）
		int iMaxRowCount = getHeaderGroupInfo(iCGs, objCGs, bNeedAlterRowNum);

		// 设置多行头表头cell信息和表体表头cell信息
		PrintCellData[][] cellses = new PrintCellData[iMaxRowCount][totalColNum];
		for (int i = 0; i < iMaxRowCount; i++) {
			PrintCellData[] rowcells = new PrintCellData[totalColNum];
			for (int j = 0; j < totalColNum; j++) {
				PrintCellData cell = new PrintCellData();
				cell.setCellSize((int) (colWidths[j] * PRINTZOOMRATE),
						ROWHEIGHT); // 待测
				cell.setContType(0); // 纯文本
				cell.setStrMode(0); // QQ
				cell.setCellColor(Color.white, Color.black); // QQ
				cell.setLineVisible(true, true, true, true); // QQ
				cell.setDataRela(null);
				cell.setLineType(new int[] { 1, 1, 1, 1 }); // QQ
				cell.setLineColor(new Color[] { Color.black, Color.black,
						Color.black, Color.black });
				cell.setVerAlign(1);
				cell.setHorAlign(1);

				cell.setFont(font);

				if (isLockRow())
					cell.setFrozenRow(true);
				rowcells[j] = cell;
			}
			cellses[i] = rowcells;
		}

		int groupHeader = getName().hashCode();
		// 设置纵向合并表头
		for (int j = 0; j < totalColNum; j++) {
			int iMergeCount = iCGs[j];
			for (int i = 0; i < iMaxRowCount; i++) {
				if (!isGroupHeader && j < rowHeaderColNum) {
					cellses[i][j].setCombined(true);
					cellses[i][j].setParentID(groupHeader);
					cellses[i][j].setStr("");
					continue;
				}
				if (i < iMergeCount) {
					cellses[i][j].setCombined(false);
				} else {
					if (iMaxRowCount - iMergeCount > 1) {
						cellses[i][j].setCombined(true);
						cellses[i][j].setParentID(j + 1 + groupHeader);
					}
					// System.out.println(1 + j);
					cellses[i][j].setStr(showColInfos[j].colName);
				}
			}
		}
		// 设置横向合并表头
		int k = 0;
		int random = groupHeader / 2;
		for (int i = 0; i < iMaxRowCount - 1; i++) {
			for (int j = 0; j < totalColNum; j++) {
				if (iCGs[j] <= i) {
					continue;
				}
				String strGroupName_J = ((ColumnGroup) ((Vector) objCGs[j])
						.elementAt(i)).getHeaderValue().toString();
				for (k = j + 1; k < totalColNum; k++) {
					if (iCGs[k] <= i) {
						break;
					}
					if (((Vector) objCGs[j]).elementAt(i) != ((Vector) objCGs[k])
							.elementAt(i)) {
						break;
					}
				}
				if (k > j + 1) {
					for (int l = j; l < k; l++) {
						cellses[i][l].setCombined(true);
						cellses[i][l].setParentID(100 * (i + 1) + j + random);
						// System.out.println(100 * (i + 1) + j);
						cellses[i][l].setStr(strGroupName_J);
					}
					j = k - 1;
				} else {
					cellses[i][j].setStr(strGroupName_J);
					// j = k;
				}
			}
		}

		return cellses;
	}

	/**
	 * 得到表头合并信息（表头含行头的表头），返回表头行数。 创建日期：(2004-6-23 13:09:44)
	 * 
	 * @return int
	 * @param iCgs
	 *            int[]
	 * @param oCgs
	 *            java.lang.Object[]
	 * @param bNeedAlterRowNum
	 *            boolean
	 * @i18n upp08110600606=多行头第
	 * @i18n upp08110600607=列
	 * @i18n upp08110600608=表体第
	 * @i18n upp08110600609=列不存在
	 */
	private int getHeaderGroupInfo(int[] iCgs, Object[] oCgs,
			boolean bNeedAlterRowNum) {
		// 多行头列数
		int rowHeaderColNum = m_rowHeader.getColumnCount();
		// 表体列数
		int tableColNum = m_dbTable.getColumnCount();
		// 总列数
		// int totalColNum = iCgs.length;

		int iMaxRowCount = 0;

		// 解析多行头表头信息(即ScrollPane左上角的corner)
		JTableHeader corner = m_rowHeader.getTableHeader();
		// boolean isGroupHeader = false;

		if (corner instanceof GroupableTableHeader) {
			int startCol = (bNeedAlterRowNum && m_rowHeader.isRowNumAtFront()) ? 1
					: 0;
			int endCol = (bNeedAlterRowNum && !m_rowHeader.isRowNumAtFront()) ? rowHeaderColNum - 1
					: rowHeaderColNum;
			// isGroupHeader = true;
			GroupableTableHeader groupCorner = (GroupableTableHeader) corner;
			for (int i = startCol; i < endCol; i++) {
				TableColumn tc = null;
				try {
					tc = m_rowHeader.getColumnModel().getColumn(i);
				} catch (Exception e) {
					Logger.error("多行头第" + i + "列", e);
					continue;
				}
				Enumeration cgs = groupCorner.getColumnGroups(tc);
				Vector vecCG = new Vector();
				if (cgs != null) {
					while (cgs.hasMoreElements()) {
						vecCG.addElement(cgs.nextElement());
					}
				}
				iCgs[i - startCol] = vecCG.size();
				if (iMaxRowCount < iCgs[i - startCol]) {
					iMaxRowCount = iCgs[i - startCol];
				}
				oCgs[i - startCol] = vecCG;
			}
			rowHeaderColNum = endCol - startCol;
		} else {
			rowHeaderColNum = bNeedAlterRowNum ? rowHeaderColNum - 1
					: rowHeaderColNum;
			for (int i = 0; i < rowHeaderColNum; i++) {
				iCgs[i] = 0;
				oCgs[i] = null;
			}
		}

		// 解析表体多表头信息
		GroupableTableHeader header = (GroupableTableHeader) m_dbTable
				.getTableHeader();

		for (int i = 0; i < tableColNum; i++) {
			TableColumn tc = null;
			try {
				tc = m_dbTable.getColumnModel().getColumn(i);
			} catch (Exception e) {
				Logger.error("表体第" + i + "列不存在", e);
				continue;
			}
			Enumeration colGroups = header.getColumnGroups(tc);
			Vector vecCG = new Vector();
			if (colGroups != null) {
				while (colGroups.hasMoreElements()) {
					vecCG.addElement(colGroups.nextElement());
				}
			}
			iCgs[i + rowHeaderColNum] = vecCG.size();
			if (iMaxRowCount < iCgs[i + rowHeaderColNum]) {
				iMaxRowCount = iCgs[i + rowHeaderColNum];
			}
			oCgs[i + rowHeaderColNum] = vecCG;
		}
		iMaxRowCount++;

		return iMaxRowCount;
	}

	/**
	 * 从Model中得到当前行在原始行中的序号。 创建日期：(2003-12-24 17:06:01)
	 * 
	 * @return int
	 * @param model
	 *            nc.ui.reportquery.component.table.RptTableModel
	 * @param row
	 *            int
	 */
	private int getOrigRowFromModel(RptTableModel model, int row) {
		if (model instanceof CommonRptTableModel) {
			return row;
		} else {
			if (model.getRowLevel(row) > 0) { // 当前行不是原始行
				return -1;
			} else {
				int realrow = -1;
				for (int i = 0; i <= row; i++) {
					if (model.getRowLevel(i) == 0) {
						realrow++;
					}
				}
				return realrow;
			}
		}
	}

	/**
	 * 此处插入方法说明。 创建日期：(2004-6-28 15:07:36)
	 * 
	 * @return int
	 */
	public int getType() {
		return CJTable;
	}

	/**
	 * 初始化行颜色。 创建日期：(2004-6-23 10:10:58)
	 */
	// private static void initColors() {
	// Color level0 = javax.swing.UIManager.getColor("Table.background");
	// Color level5 =
	// javax.swing.UIManager.getColor("Table.selectionBackground");
	// int r0 = level0.getRed();
	// int g0 = level0.getGreen();
	// int b0 = level0.getBlue();
	//
	// int r5 = level5.getRed();
	// int g5 = level5.getGreen();
	// int b5 = level5.getBlue();
	//
	// rowColors = new Color[5]; // 5级颜色
	// for (int i = 0; i < 5; i++) {
	// int r = (int) ((r0 * (5 - i) + r5 * i) / 5);
	// int g = (int) ((g0 * (5 - i) + g5 * i) / 5);
	// int b = (int) ((b0 * (5 - i) + b5 * i) / 5);
	// rowColors[i] = new Color(r, g, b);
	// }
	//		
	// // 河北电力资金项目专项
	// rowColors[0] = Color.orange;
	// rowColors[1] = new Color(204, 204, 255);
	// rowColors[2] = new Color(153, 255, 255);
	// rowColors[3] = Color.pink;
	// rowColors[4] = Color.magenta;
	// }
	/**
	 * 判断当前绑定数据集是否是初始数据集。 创建日期：(2003-12-19 13:04:13)
	 * 
	 * @return boolean
	 */
	private boolean isDSChanged() {
		return (getBindDataSet() != m_dsOrig);
	}

	/**
	 * 修改列。 创建日期：(2003-12-23 13:35:33)
	 * 
	 * @param evt
	 *            javax.swing.event.TableModelEvent
	 */
	private void modifyColumn(TableModelEvent evt) {
		FormulaColDescriptor[] fcDsp = getTopPanel().getFormulaColByAlias(
				getRealID());

		Hashtable htDynColInfo = new Hashtable();
		for (int i = 0; i < getDynColInfo().size(); i++) {
			ColInfo colInfo = (ColInfo) getDynColInfo().get(i);
			htDynColInfo.put(colInfo.colAlias, colInfo);
		}

		for (int i = 0; i < fcDsp.length; i++) {
			ColInfo colInfo = (ColInfo) htDynColInfo
					.get(fcDsp[i].getColAlias());
			if (colInfo != null) {
				// 列格式已更改
				if (!("" + colInfo.getColFormatID()).equalsIgnoreCase(""
						+ fcDsp[i].getColFmtID())) {
					colInfo.setColFormatID(fcDsp[i].getColFmtID());
					Column col = getBindDataSet().getColumn(
							fcDsp[i].getColAlias());
					int ordinal = col.getOrdinal();
					int modelIndex = ((RptTableModel) m_dbTable.getModel())
							.convertDSordinal2ModelIndex(ordinal);
					TableColumn tc = m_dbTable.getColumnModel().getColumn(
							m_dbTable.convertColumnIndexToView(modelIndex));
					if (colInfo.getColFormatVO(getTopPanel().getDefDsName()) != null) { // 公式列格式不为空
						DataformatVO dataFmt = colInfo
								.getColFormatVO(getTopPanel().getDefDsName());
						RptTableFormatedRenderer rcr = new RptTableFormatedRenderer();
						rcr.initLabel(dataFmt);
						tc.setCellRenderer(rcr);
					} else {
						bindDataSetColumnProperties(tc, col);
					}
				}
			}
		}
	}

	/**
	 * 此处插入方法说明。 创建日期：(2004-7-23 13:07:37)
	 * 
	 * @param rqComp
	 *            nc.ui.reportquery.component.RQCompBase
	 */
	void removeSubCompMouseListener(RQCompBase rqComp) {
		m_dbTable.removeMouseListener(rqComp);
		m_dbTable.removeMouseMotionListener(rqComp);
		m_dbTable.getTableHeader().removeMouseListener(rqComp);
		m_dbTable.getTableHeader().removeMouseMotionListener(rqComp);
		m_scrollPane.getViewport().removeMouseListener(rqComp);
		m_scrollPane.getViewport().removeMouseMotionListener(rqComp);
		m_rowHeader.removeMouseListener(rqComp);
		m_rowHeader.removeMouseMotionListener(rqComp);
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see nc.ui.reportquery.component.RQCompBase#getPrintValues(java.lang.String)
	 */
	public String[] getPrintValues(String itemExpress) {
		int index = itemExpress.lastIndexOf(".");
		String itemName = itemExpress.substring(index + 1);
		RptTableModel rpttm = (RptTableModel) m_dbTable.getModel();
		int colOrdinal = rpttm.getColumnOrdinal(itemName);

		DataformatVO format = null;
		RptTableFormatedRenderer rcr = null;

		ColInfo[] colInfos = getAllColInfos();
		HashMap htColInfos = new HashMap();
		if (colInfos != null) {
			for (int i = 0; i < colInfos.length; i++) {
				htColInfos.put(colInfos[i].colAlias.toUpperCase(), colInfos[i]);
			}
		}
		ColInfo colInfo = (ColInfo) htColInfos.get(itemName);
		format = null;
		if (colInfo != null) {
			colInfo.getColFormatVO(getTopPanel().getDefDsName());
			if (format != null) {
				rcr = new RptTableFormatedRenderer();
				rcr.initLabel(format);
			}
		}

		int rowCount = rpttm.getRowCount();
		String[] values = new String[rowCount];
		for (int i = 0; i < rowCount; i++) {
			Object obj = rpttm.getValueAt(i, colOrdinal);
			String str = "";

			if (format != null) {
				rcr.setValue(obj);
				str = rcr.getText();
			} else {
				if (obj != null) {
					str = obj.toString();
				} else {
					str = "";
				}
			}
			values[i] = str;
		}
		return values;
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see nc.ui.reportquery.component.RQCompBase#isNumberType(java.lang.String)
	 */
	public boolean isNumberType(String itemExpress) {
		int index = itemExpress.lastIndexOf(".");
		String itemName = itemExpress.substring(index + 1);
		RptTableModel rpttm = (RptTableModel) m_dbTable.getModel();
		int colOrdinal = rpttm.getColumnOrdinal(itemName);
		int dataType = rpttm.getColumn(colOrdinal).getDataType();// Variant
		// Type
		int sqlType = QueryUtil.variantTypeToSqlType(dataType);
		return QueryUtil.isNumberType(sqlType);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nc.ui.reportquery.toolkit.CompClonable#cloneComp(boolean)
	 */
	public RQCompBase cloneComp(boolean isdesignstate, String name) {
		RQCompBase temp = null;
		temp = new RQTable(isdesignstate);
		return temp;
	}

	/*
	 * 旋转交叉合计行
	 */
	private void makeAggDesc() {
		// 获得原始合计设置
		AggregateDescriptor originAgg = AggregateDescriptor
				.parseString(bakAggrInfo);
		// 获得原始合计列
		String[] originAggCols = originAgg.getAggrColumns();
		int iLenAgg = (originAggCols == null) ? 0 : originAggCols.length;
		if (iLenAgg != 0) {
			String[] originAggOprs = originAgg.getAggrOperators();
			Vector vecNewAggCol = new Vector();
			Vector vecNewAggOpr = new Vector();
			// 获得新的列名
			SelectFldVO[] fldVOs = DatasetUtil.getSelectFldByDataset(m_ds
					.getColumns());
			// 匹配出新的合计列
			// RotateCrossVO rc = m_curDsFp.getRotateCrossVO();
			int iLenFld = (fldVOs == null) ? 0 : fldVOs.length;
			for (int i = 0; i < iLenAgg; i++) {
				if (m_hashOriginAlias.containsKey(originAggCols[i])) {
					// 获得原始合计列的显示名
					String originAggColName = ((SelectFldVO) m_hashOriginAlias
							.get(originAggCols[i])).getFldname();
					for (int j = 0; j < iLenFld; j++) {
						// 获得新列的显示名
						String newColName = fldVOs[j].getFldname();
						// 不能这么判断，因为交叉列只有交叉砝码时没有上划线
						// if (newColName.indexOf(originAggColName +
						// QueryConst.CROSS_SEPERATOR) != -1 ||
						// newColName.indexOf(QueryConst.CROSS_SEPERATOR +
						// originAggColName) != -1) {
						// 执行匹配(因为交叉后别名信息已丢失, 因此需要依靠显示名进行匹配)
//						if (newColName.indexOf(originAggColName) != -1) {
//							vecNewAggCol.addElement(fldVOs[j].getFldalias());
//							vecNewAggOpr.addElement(originAggOprs[i]);
//						}
						//V55+ yza 2009-3-6 旋转交叉合计翻倍问题
						if(newColName.indexOf("ˉ") !=-1){
							//有复合列维度
							if (newColName.indexOf(originAggColName+"ˉ") != -1) {
								vecNewAggCol
										.addElement(fldVOs[j].getFldalias());
								vecNewAggOpr.addElement(originAggOprs[i]);
							}
						}else{
							//没有复合列维度
							if (newColName.equals(originAggColName)) {
								vecNewAggCol.addElement(fldVOs[j].getFldalias());
								vecNewAggOpr.addElement(originAggOprs[i]);
							}
						}
					}
				}
			}
			// 设置新的合计信息
			String[] newAggCols = new String[vecNewAggCol.size()];
			String[] newAggOprs = new String[vecNewAggOpr.size()];
			if (vecNewAggCol.size() != 0) {
				vecNewAggCol.copyInto(newAggCols);
				vecNewAggOpr.copyInto(newAggOprs);
			}
			m_aggregateDescriptor.setAggrColumns(newAggCols);
			m_aggregateDescriptor.setAggrOperators(newAggOprs);
		}
	}

	/**
	 * 是否锁定列
	 * 
	 * @return
	 */
	public boolean isLockCol() {
		return isLockCol;
	}

	public void setLockCol(boolean flag) {
		this.isLockCol = flag;
	}

	/**
	 * 锁定列表头
	 * 
	 * @return
	 */
	private UITable getLockColTable() {
		if (lockColTable == null) {
			lockColTable = new UITable(m_dbTable.getModel());
			lockColTable.setCellSelectionEnabled(false);
			lockColTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
			lockColTable.getTableHeader().setReorderingAllowed(false);
			lockColTable.getTableHeader().setResizingAllowed(false);
			lockColTable.setRowSelectionAllowed(true);
			lockColTable.setSelectionModel(m_dbTable.getSelectionModel());
			lockColTable.getHeaderPopupMenu().addSeparator();
			lockColTable.getHeaderPopupMenu().add(m_unlockColMenu);
			lockColTable.setBackground(lockColTable.getTableHeader()
					.getBackground());
			lockColTable.setFocusable(false);
			lockColTable.setRowHeight(m_dbTable.getRowHeight());
		}
		return lockColTable;
	}

	// jl+ 锁定列事件监听器
	private class LockTableActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (isLockCol()) {
				unlockCol();
			} else {
				lockCol(getDBTable().getSelectedHeaderColumn() + 1);
			}
		}
	}

	private UITable getDBTable() {
		return m_dbTable;
	}

	/**
	 * 锁定列方法
	 * 
	 * @param lockCol
	 */
	private void lockCol(int lockCol) {

		if (m_crossType != QueryConst.CROSSTYPE_NONE
				|| getDefSimpleCrossVO() != null
				|| getDefRotateCrossVO() != null || m_headerId != null) {
			MessageDialog.showWarningDlg(this.getParent(), nc.ui.ml.NCLangRes
					.getInstance().getStrByID("10241201", "UPP10241201-000099")/*
			 * @res
			 * "查询引擎"
			 */, nc.ui.ml.NCLangRes.getInstance().getStrByID("10241201",
					"UPP10241201-001511")/*
			 * @res "在当前状态下不支持锁定"
			 */);
			return;
		}
		if (isLockCol())
			return;
		if (lockCol == 0 || lockCol == getDBTable().getColumnCount())
			return;
		if (isDesignState()) {
			m_iLockCol = lockCol;
			setLockCol(true);
			m_lockColMenu.setName(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"10241201", "UPP10241201-001508")/*
			 * @res "解锁"
			 */);
			m_lockColMenu.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"10241201", "UPP10241201-001508")/*
			 * @res "解锁"
			 */);
			return;
		}
		m_iLockCol = lockCol;
		// 原始表的列模型
		TableColumnModel tcm = getDBTable().getColumnModel();
		TableColumn tcol;
		// jl+ @2007-11-6 要兼容列格式和列锁定，列格式对列顺序的调整在锁定前后应该保持一致。
		TableColumnModel lcm = new DefaultTableColumnModel();
		for (int i = 0; i < tcm.getColumnCount(); i++) {
			lcm.addColumn(tcm.getColumn(i));
		}
		getLockColTable().setColumnModel(lcm);
		for (int i = getLockColTable().getColumnCount() - 1; i >= lockCol; i--) {
			lcm.removeColumn(lcm.getColumn(i));
		}
		// 移去主表行
		int iWidth = 0;

		for (int i = 0; i < lockCol; i++) {
			tcol = tcm.getColumn(0);
			iWidth = iWidth + tcol.getPreferredWidth();// + columnMargin;
			getLockColTable().getColumnModel().getColumn(i).setPreferredWidth(
					tcol.getPreferredWidth());// +
			// columnMargin);
			tcm.removeColumn(tcol);
			getLockColTable().getColumnModel().getColumn(i).setCellRenderer(
					tcol.getCellRenderer());
		}
		// TableColumn rowNum = m_rowHeader.getColumnModel().getColumn(0);
		//
		// if(rowNum!=null&&"行号".equals(rowNum.getHeaderValue().toString())){
		// getLockColTable().addColumn(rowNum);
		// int colCount = getLockColTable().getColumnCount();
		// iWidth+=rowNum.getPreferredWidth();
		// getLockColTable().getColumnModel().getColumn(colCount-1).setPreferredWidth(rowNum.getPreferredWidth());
		// getLockColTable().getColumnModel().getColumn(colCount-1).setCellRenderer(rowNum.getCellRenderer());
		// getLockColTable().getColumnModel().moveColumn(colCount-1,0);
		// }

		// 头表尺寸
		if (System.getProperty("java.vm.version").startsWith("1.2"))
			getLockColTable().setPreferredScrollableViewportSize(
					new java.awt.Dimension(iWidth + lockCol, 10));
		else
			getLockColTable().setPreferredScrollableViewportSize(
					new java.awt.Dimension(iWidth, 10));
		m_scrollPane.setRowHeaderView(getLockColTable());
		m_scrollPane.getRowHeader().setPreferredSize(
				getLockColTable().getPreferredScrollableViewportSize());
		m_scrollPane.setCorner(JScrollPane.UPPER_LEFT_CORNER, getLockColTable()
				.getTableHeader());
		m_scrollPane.revalidate();
		// repaint();
		setLockCol(true);
		m_lockColMenu.setName(nc.ui.ml.NCLangRes.getInstance().getStrByID(
				"10241201", "UPP10241201-001508")/*
		 * @res "解锁"
		 */);
		m_lockColMenu.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID(
				"10241201", "UPP10241201-001508")/*
		 * @res "解锁"
		 */);
		refreshPopMenu();
	}

	/**
	 * 解锁
	 * 
	 */
	private void unlockCol() {
		if (!isLockCol())
			return;

		if (isDesignState()) {
			m_iLockCol = -1;
			setLockCol(false);

			m_lockColMenu.setName(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"10241201", "UPP10241201-001507")/*
			 * @res "锁定"
			 */);
			m_lockColMenu.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"10241201", "UPP10241201-001507")/*
			 * @res "锁定"
			 */);
			return;
		}
		// 原始表的列模型
		TableColumnModel tcm = getDBTable().getColumnModel();
		TableColumn tcol;
		TableColumnModel lcm = getLockColTable().getColumnModel();
		// 添加主表行
		for (int i = 0; i < m_iLockCol; i++) {
			tcol = lcm.getColumn(m_iLockCol - i - 1);
			tcm.addColumn(tcol);
			int numCols = tcm.getColumnCount();
			tcm.moveColumn(numCols - 1, 0);
		}
		// 清一下信息
		lockColTable = null;
		m_scrollPane.setRowHeaderView(m_rowHeader);
		m_scrollPane.revalidate();
		m_iLockCol = -1;
		setLockCol(false);
		m_lockColMenu.setName(nc.ui.ml.NCLangRes.getInstance().getStrByID(
				"10241201", "UPP10241201-001507")/*
		 * @res "锁定"
		 */);
		m_lockColMenu.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID(
				"10241201", "UPP10241201-001507")/*
		 * @res "锁定"
		 */);
		refreshPopMenu();
		// 刷新一下行头
		refreshRowHeader(getRowHeaderInfo());
	}

	/**
	 * 获取行表头 created on 2006-3-28
	 * 
	 * @return <strong>最后修改人: jl<strong> <strong>最后修改日期: 2006-3-28<stong>
	 */
	public RQRowHeader getTableRowHeader() {
		return m_rowHeader;
	}

	public Hashtable getHtColAliasColInfo() {
		return htColAliasColInfo;
	}

	/**
	 * created on 2006-7-6
	 * 
	 * @param defRotateCrossVO
	 *            The defRotateCrossVO to set.
	 */
	private void setDefRotateCrossVO(RotateCrossVO defRotateCrossVO) {
		this.defRotateCrossVO = defRotateCrossVO;
	}

	/**
	 * created on 2006-7-6
	 * 
	 * @return Returns the defRotateCrossVO.
	 */
	private RotateCrossVO getDefRotateCrossVO() {
		return defRotateCrossVO;
	}

	/**
	 * created on 2006-7-6
	 * 
	 * @param defSimpleCrossVO
	 *            The defSimpleCrossVO to set.
	 */
	private void setDefSimpleCrossVO(SimpleCrossVO[] defSimpleCrossVO) {
		this.defSimpleCrossVO = defSimpleCrossVO;
	}

	/**
	 * created on 2006-7-6
	 * 
	 * @return Returns the defSimpleCrossVO.
	 */
	private SimpleCrossVO[] getDefSimpleCrossVO() {
		return defSimpleCrossVO;
	}

	private RotateCrossVO getDefRotateCrossVO_inDlg() {
		return defRotateCrossVO_inDlg;
	}

	private void setDefRotateCrossVO_inDlg(RotateCrossVO defRotateCrossVO_inDlg) {
		this.defRotateCrossVO_inDlg = defRotateCrossVO_inDlg;
	}

	private SimpleCrossVO[] getDefSimpleCrossVO_inDlg() {
		return defSimpleCrossVO_inDlg;
	}

	private void setDefSimpleCrossVO_inDlg(
			SimpleCrossVO[] defSimpleCrossVO_inDlg) {
		this.defSimpleCrossVO_inDlg = defSimpleCrossVO_inDlg;
	}

	public boolean isLockRow() {
		return lockRow;
	}

	public void setLockRow(boolean lockRow) {
		this.lockRow = lockRow;
	}

	private PageController getPageController() {
		if (pageController == null) {
			pageController = new PageController();
		}
		return pageController;
	}

	private ScrollableDataSet getScrollableDataSet() {
		return scrollableDataSet;
	}

	private void setScrollableDataSet(ScrollableDataSet scrollableDataSet) {
		this.scrollableDataSet = scrollableDataSet;
	}

	public void dataSetScroll(DataSetScrollEvent event) {
		QEDataSet ds = event.getSource() == null ? null : (QEDataSet) event
				.getSource();
		if (ds != null) {
			int dataSetIndex = event.getDatasetIndex();
			pageEventHandler.calculateFormula(ds);
			doRefreshWithDataSet(getTopPanel().getQryDefByAlias(getID()), ds);
			getPageController().setCurrentPageCount(dataSetIndex);
			pageIndex = dataSetIndex;
		}
	}

	protected void refreshEnvConst(Container container, Hashtable htQryParams) {
		int nComp = container.getComponentCount();
		for (int i = 0; i < nComp; i++) {
			Component comp = container.getComponent(i);
			try {
				if (comp instanceof RQTextField) {
					((RQTextField) comp).refreshEnvConst(htQryParams);
				} else if (comp instanceof RQLabel) {
					((RQLabel) comp).refreshEnvConst(htQryParams);
				} else if (comp instanceof RQTextArea) {
					((RQTextArea) comp).refreshEnvConst(htQryParams);
				}
			} catch (Exception e) {
				Logger.error(e);
			}
			if (comp instanceof Container) {
				refreshEnvConst((Container) comp, htQryParams);
			}
		}
	}

	/*
	 * 处理预警颜色及列颜色丢失问题 +V5.01 yza
	 */
	protected HSSFCellStyle buildRowColorStyle(HSSFWorkbook workbook,
			ColInfo colInfo, HSSFDataFormat hssfFmt, Object rowValue) {
		DataformatVO dataFmt = colInfo.getColFormatVO(getTopPanel()
				.getDefDsName());
		if (dataFmt == null || colInfo == null)
			return null;
		ColFormatDelegate cfd = new ColFormatDelegate(dataFmt);
		if (cfd == null)
			return null;
		cfd.setObjValue(rowValue);
		if (cfd.isFiltered() < 0)
			return null;
		HSSFCellStyle style = workbook.createCellStyle();
		style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		style.setWrapText(true);
		short fmt = 0;
		String strType = dataFmt.getFldtype();
		int iType = Integer.parseInt(strType);
		if (iType == ColFormatConstants.NUMBER_FIELD
				|| iType == ColFormatConstants.MONEY_FIELD
				|| iType == ColFormatConstants.PERCENT_FIELD) {
			fmt = hssfFmt.getFormat(FormatOutputUtil
					.procexcelFmtPattern(dataFmt));
		} else {
			fmt = HSSFDataFormat.getBuiltinFormat("General");
		}
		style.setDataFormat(fmt);
		String alignStr = dataFmt.getAlignment();

		int align = Integer.parseInt(alignStr);
		short colAlign = HSSFCellStyle.ALIGN_CENTER;
		if (align == SwingConstants.LEFT) {
			colAlign = HSSFCellStyle.ALIGN_LEFT;
		} else if (align == SwingConstants.RIGHT) {
			colAlign = HSSFCellStyle.ALIGN_RIGHT;
		}
		style.setAlignment(colAlign);
		Color color = cfd.getClrFilter(cfd.isFiltered());
		short shColor = RQTable.getColor(color);
		HSSFFont currFont = workbook.createFont();
		currFont.setColor(shColor);
		style.setFont(currFont);
		return style;
	}

	/**
	 * 为导出Excel行添加格式 +V5.5 yza
	 */
//	private HSSFCellStyle buildStyleByDfmt(HSSFWorkbook workbook,
//			HSSFCellStyle style, ColInfo colInfo, HSSFDataFormat hssfFmt) {
//		style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
//		style.setWrapText(true);
//		if (colInfo == null) {
//			return style;
//		}
//
//		DataformatVO dataFmt = colInfo.getColFormatVO(getTopPanel()
//				.getDefDsName());
//		if (dataFmt != null) {
//			short fmt = 0;
//			String strType = dataFmt.getFldtype();
//			int iType = Integer.parseInt(strType);
//			if (iType == ColFormatConstants.NUMBER_FIELD
//					|| iType == ColFormatConstants.MONEY_FIELD
//					|| iType == ColFormatConstants.PERCENT_FIELD) {
//				fmt = hssfFmt.getFormat(FormatOutputUtil
//						.procexcelFmtPattern(dataFmt));
//			} else {
//				fmt = HSSFDataFormat.getBuiltinFormat("General");
//			}
//			style.setDataFormat(fmt);
//
//			String alignStr = dataFmt.getAlignment();
//
//			int align = Integer.parseInt(alignStr);
//			short colAlign = HSSFCellStyle.ALIGN_CENTER;
//			if (align == SwingConstants.LEFT) {
//				colAlign = HSSFCellStyle.ALIGN_LEFT;
//			} else if (align == SwingConstants.RIGHT) {
//				colAlign = HSSFCellStyle.ALIGN_RIGHT;
//			}
//			style.setAlignment(colAlign);
//			ColFormatDelegate cfd = new ColFormatDelegate(dataFmt);
//			if (cfd.getStrFilters() == null && cfd.getClrFilters() == null) {
//				// 没有预警条件
//				short shColor = RQTable.getColor(cfd.getClrText());
//				HSSFFont currFont = workbook.createFont();
//				currFont.setColor(shColor);
//				style.setFont(currFont);
//			}
//		}
//		return style;
//	}

	/**
	 * V55+ 表头菜单中控制是否显示“定位”
	 */
	public void showLocatePopupItem(boolean bShow) {
		UIPopupMenu popupMenu = m_dbTable.getHeaderPopupMenu();
		if (popupMenu.getComponentCount() > 0) {
			popupMenu.getComponent(0).setVisible(bShow);
		}
	}

	/**
	 * RGB颜色转为POI的short颜色值 +V5.5 yza
	 */
	public static short getColor(Color color) {
		if (color == null)
			return -1;

		int diff = Integer.MAX_VALUE;
		short minDiffIndex = -1;
		for (Enumeration enumeration = RQTable.s_colorIndexHash.keys(); enumeration
				.hasMoreElements();) {
			Integer index = (Integer) enumeration.nextElement();
			HSSFColor aColor = (HSSFColor) RQTable.s_colorIndexHash.get(index);
			short[] aRgb = aColor.getTriplet();
			int aDiff = (int) (Math.pow(aRgb[0] - color.getRed(), 4)
					+ Math.pow(aRgb[1] - color.getGreen(), 4) + Math.pow(
					aRgb[2] - color.getBlue(), 4));
			if (aDiff == 0) {
				return index.shortValue();
			} else {
				if (aDiff < diff) {
					diff = aDiff;
					minDiffIndex = index.shortValue();
				}
			}
		}
		return minDiffIndex;
	}
	
	private Integer[] getLevelColor() {
		if (rowColors == null || rowColors.length < 0) {
			if (m_aggregateDescriptor == null || m_aggregateDescriptor.getColors() == null
					|| m_aggregateDescriptor.getColors().length < 0)
				return StyleUtil.getLevelColorsValue(AggregateDescriptor.colorLevel);
			return m_aggregateDescriptor.getColors();
		}
		return rowColors;

	}

	/**
	 * POI的short颜色值哈希表 +V5.5 yza
	 */
	public static Hashtable s_colorIndexHash = HSSFColor.getIndexHash();
}