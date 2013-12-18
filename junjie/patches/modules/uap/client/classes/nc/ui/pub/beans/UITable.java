package nc.ui.pub.beans;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Event;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.DefaultCellEditor;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JToolTip;
import javax.swing.JViewport;
import javax.swing.KeyStroke;
import javax.swing.Popup;
import javax.swing.PopupFactory;
import javax.swing.UIManager;
import javax.swing.event.TableModelEvent;
import javax.swing.plaf.ComponentUI;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

import nc.ui.bill.tools.DefaultTableHeaderCellRenderer;
import nc.ui.pub.beans.action.DoNothingAction;
import nc.ui.pub.beans.action.TableNavigationalAction;
import nc.ui.pub.beans.action.TableShortCutAction;
import nc.ui.pub.beans.table.GroupableTableHeader;
import nc.ui.pub.beans.table.GroupableTableHeaderUI;
import nc.ui.pub.beans.table.IMutilSortableTableModel;
import nc.ui.pub.beans.table.ISortableTableModel;
import nc.ui.pub.beans.table.ITableModel;
import nc.ui.pub.beans.table.NCLineBorder;
import nc.ui.pub.beans.table.UITableCellEditor;
import nc.ui.pub.beans.table.UIVarLenTextField;
import nc.ui.pub.style.Style;
import nc.vo.logging.Debug;

/**
 * @author Larrylau
 */
public class UITable extends javax.swing.JTable
{
	private boolean			translate				= true;
	private boolean			fieldSortEnabled		= false;
	// default header flag
	private boolean			defaultHeader			= false;
	// the flag of cell locate
	private boolean			cellLacateEnabled		= true;
	// CaseMatched or not for locate
	private boolean			locateCaseMatched		= false;
	// popupMenu to dealwith locate
	private UIPopupMenu		headerPopupMenu			= new UIPopupMenu();
	protected UIMenuItem	locateMenu				= new UIMenuItem(nc.ui.ml.NCLangRes.getInstance().getStrByID("_Beans", "UPP_Beans-000079")/* @res "定位" */
															+ "...");
	private int				selectedHeaderColumn	= -1;
	private int[]			notNullCols				= null;
	private boolean			showTip					= false;
	/**
	 * 记录各列的排序状态
	 */
	private int[]			sortstatusarray			= null;
	private List<SortItem>	sortItemList			= null;
	private static Font		sortnumFont				= new Font(Style.getFontname(), Font.PLAIN, 9);

	public static class SortItem
	{
		public int		column;
		public boolean	ascending;

		public int getColumn()
		{
			return column;
		}

		public boolean isAscending()
		{
			return ascending;
		}

		public void setAscending(boolean flag)
		{
			ascending = flag;
		}

		public SortItem(int i)
		{
			column = i;
			ascending = true;
		}

		public SortItem(int i, boolean flag)
		{
			column = i;
			ascending = flag;
		}
	}

	/** 是否有任何一列正处于排序状态 */
	private boolean				isSorting			= false;
	final private static int	SORT_NONE			= -1;
	final private static int	SORT_UP				= 1;
	final private static int	SORT_DOWN			= 0;
	private static Icon			ICON_UP				= null;
	private static Icon			ICON_DOWN			= null;
	public static final int		UITABLE_CELL_HEIGHT	= 20;
	/** 表格表头高度 */
	public static final int		UITABLE_HEAD_HEIGHT	= 21;
	/**确保listener只new且只加入一次*/
	private MouseListener		sortListener		= null;
	/**定位对话框*/
	private UITableSearchDialog	searchDialog		= null;
	static
	{
		URL url = UITable.class.getResource("/images/v5control/sort_asc.gif");
		URL downUrl = UITable.class.getResource("/images/v5control/sort_dsc.gif");
		if (url != null)
			ICON_UP = new ImageIcon(url);
		if (downUrl != null)
			ICON_DOWN = new ImageIcon(downUrl);
	}

	private class HeaderPopupMouseAdapter extends MouseAdapter
	{
		public void mouseReleased(java.awt.event.MouseEvent e)
		{
			if (e.isPopupTrigger())
			{
				headerPopupAction(e);
			}
			;
		};
	}

	private class LocateMenuActionListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			locateCell();
		}
	}

	public UITable()
	{
		super();
		initialize();
	}

	/**
	 * UITable 构造子注释.
	 * 
	 * @param rowData
	 *            java.lang.Object[][]
	 * @param columnNames
	 *            java.lang.Object[]
	 */
	public UITable(java.lang.Object[][] rowData, java.lang.Object[] columnNames)
	{
		super(rowData, columnNames);
		initialize();
	}

	/**
	 * UITable 构造子注释.
	 * 
	 * @param numRows
	 *            int
	 * @param numColumns
	 *            int
	 */
	public UITable(int numRows, int numColumns)
	{
		super(numRows, numColumns);
		initialize();
	}

	/**
	 * UITable 构造子注释.
	 * 
	 * @param rowData
	 *            java.util.Vector
	 * @param columnNames
	 *            java.util.Vector
	 */
	public UITable(java.util.Vector rowData, java.util.Vector columnNames)
	{
		super(rowData, columnNames);
		initialize();
	}

	/**
	 * UITable 构造子注释.
	 * 
	 * @param dm
	 *            javax.swing.table.TableModel
	 */
	public UITable(javax.swing.table.TableModel dm)
	{
		super(dm);
		initialize();
	}

	/**
	 * UITable 构造子注释.
	 * 
	 * @param dm
	 *            javax.swing.table.TableModel
	 * @param cm
	 *            com.sun.java.swing.table.TableColumnModel
	 */
	public UITable(javax.swing.table.TableModel dm, javax.swing.table.TableColumnModel cm)
	{
		super(dm, cm);
		initialize();
	}

	/**
	 * UITable 构造子注释.
	 * 
	 * @param dm
	 *            javax.swing.table.TableModel
	 * @param cm
	 *            com.sun.java.swing.table.TableColumnModel
	 * @param sm
	 *            com.sun.java.swing.ListSelectionModel
	 */
	public UITable(javax.swing.table.TableModel dm, javax.swing.table.TableColumnModel cm, javax.swing.ListSelectionModel sm)
	{
		super(dm, cm, sm);
		initialize();
	}

	static class SortIcon implements Icon
	{
		Icon	m_icon		= null;
		String	m_numText	= "1";

		public SortIcon(Icon icon, String numText)
		{
			m_icon = icon;
			m_numText = numText;
		}

		public int getIconWidth()
		{
			return m_icon.getIconWidth() + 6;
		}

		public int getIconHeight()
		{
			return m_icon.getIconHeight();
		}

		public void paintIcon(Component c, Graphics g, int x, int y)
		{
			m_icon.paintIcon(c, g, x, y);
			Color oldC = g.getColor();
			Font of = g.getFont();
			g.setColor(Color.BLACK);
			g.setFont(sortnumFont);
			g.drawString(m_numText, x + m_icon.getIconWidth(), y + getIconHeight());
			g.setColor(oldC);
			g.setFont(of);
		}
	}

	/**
	 * 表头排序时的Render
	 * 
	 * @author cch (cch@ufida.com.cn) 2006-3-7-11:23:38
	 * 
	 */
	class SortableHeaderRenderer extends DefaultTableHeaderCellRenderer implements TableCellRenderer
	{
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
		{
			//默认不需要边框
			setDefaultBorder(null);
			Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
			if (c instanceof JLabel)
			{
				JLabel l = (JLabel) c;
				l.setHorizontalTextPosition(JLabel.LEFT);
				l.setIcon(getSortIcon(column));
				
//				//判断字体宽度决定是否现实tooltip
//				Font defaultfont = Style.getDefaultFont();
//				Graphics g = table.getGraphics();
//				if (g != null)
//				{
//					FontMetrics fontm = g.getFontMetrics(defaultfont);
//					int textWidth = fontm.stringWidth(l.getText());
//					int columnWidth = getColumnModel().getColumn(column).getWidth();
//					if (textWidth > columnWidth)
//						l.setToolTipText(getText());
//					else
//						l.setToolTipText(null);
//				}
//				l.setPreferredSize(new Dimension(l.getWidth(), UITABLE_HEAD_HEIGHT));
			}
			return c;
		}

	}

	private SortItem getSortItemByColumn(int col)
	{
		List<SortItem> items = getSortItemList();
		if (items != null)
		{
			for (SortItem item : items)
			{
				if (item.getColumn() == col)
				{
					return item;
				}
			}
		}
		return null;
	}

	static class ToolTipOnTheFly
	{
		public static void showToolTip(String msg, Point pos)
		{
			JToolTip toolTip = new JToolTip();
			toolTip.setTipText(msg);
			final Popup pop = PopupFactory.getSharedInstance().getPopup(null, toolTip, pos.x, pos.y);
			pop.show();
			Runnable r = new Runnable()
			{
				public void run()
				{
					try
					{
						Thread.sleep(3000);
						pop.hide();
					}
					catch (InterruptedException e)
					{
						e.printStackTrace();
					}
				}
			};
			new Thread(r).start();
		}
	}

	private void toggleSortOrder(int col, boolean mutil, Point pos)
	{
		if (!showTip)
		{
			ToolTipOnTheFly.showToolTip(nc.ui.ml.NCLangRes.getInstance().getStrByID("_Beans", "UPP_Beans-050201")/*"按住Ctrl键选择多列排序"*/, pos);
			showTip = true;
		}
		SortItem item = getSortItemByColumn(col);
		if (item != null)
		{
			if (!item.isAscending())
			{
				if (!mutil)
				{
					int nsize = getSortItemList().size();
					getSortItemList().clear();
					if (nsize > 1)
						getSortItemList().add(new SortItem(col));
				}
				else
					getSortItemList().remove(item);
				return;
			}
			else
			{
				item.setAscending(!item.isAscending());
			}
		}
		else
		{
			item = new SortItem(col);
		}
		if (!mutil)
		{
			if (getSortItemList().size() > 1)
			{
				item.setAscending(true);
			}
			getSortItemList().clear();
		}
		if (!getSortItemList().contains(item))
		{
			getSortItemList().add(item);
		}
	}

	/**
	 * 
	 * 创建日期:(2001-4-18 14:18:23)
	 */
	public void addSortListener()
	{
		fieldSortEnabled = true;
		if (sortListener == null)
		{
			sortListener = new java.awt.event.MouseAdapter()
			{
				@SuppressWarnings("deprecation")
				public void mouseClicked(java.awt.event.MouseEvent e)
				{
					Point p = e.getPoint();
					
					int column = getTableHeader().columnAtPoint(p);

		            if (column == -1) { 
		                 return; 
		            }
		            Rectangle r = getTableHeader().getHeaderRect(column); 
		            r.grow(-3, 0); 
		            if (r.contains(p)) { 
			            TableModel model = getModel();
						if (e.getClickCount() == 1 && e.getButton() != MouseEvent.BUTTON3 && !getUITable().isEditing() && getUITable().isSortEnabled()
								&& (model instanceof ISortableTableModel || model instanceof ITableModel || model instanceof IMutilSortableTableModel))
						{
							//该方法放在内部类外，为了让leijun重载
							doMouseClickSort(e);
						}
		            }
				}
			};
		}
		javax.swing.table.JTableHeader jth = (javax.swing.table.JTableHeader) getTableHeader();
		jth.removeMouseListener(sortListener);
		jth.addMouseListener(sortListener);
	}

	
	/**
	 * 处理排序鼠标点击事件
	 * @date 2007-10-25
	 * @author Larrylau
	 * @param e
	 */
	@SuppressWarnings("deprecation")
	protected void doMouseClickSort(java.awt.event.MouseEvent e)
	{
		TableModel model = getModel();
		TableColumnModel tcm = getColumnModel();
		int vc = tcm.getColumnIndexAtX(e.getX());
		int mc = convertColumnIndexToModel(vc);
		if (vc >= 0 && vc < getColumnCount())
		{
			int[] oldrow = getSelectedRows();
			// 增加排序状态的管理 //Modified BY CCH 2006-03-07
			maintainSortStatus(mc, model.getColumnCount());
			if (model instanceof IMutilSortableTableModel)
			{
				//getTableHeader().setToolTipText(nc.ui.ml.NCLangRes.getInstance().getStrByID("_Beans", "UPP_Beans-050201")/*"按住Ctrl键选择多列排序"*/);
				//toggleSortOrder(mc, (e.getModifiers() & Event.CTRL_MASK) == 2,e.getPoint());
				Point locationOnScreen = ((JComponent) e.getSource()).getLocationOnScreen();
				locationOnScreen.translate(e.getX(), e.getY());
				toggleSortOrder(mc, (e.getModifiers() & Event.CTRL_MASK) == 2, locationOnScreen);
				((IMutilSortableTableModel) model).sortByColumns(getSortItemList(), oldrow);
				if (oldrow.length > 0)
				{
					setRowSelectionInterval(oldrow[0], oldrow[0]);
					for (int i = 1; i < oldrow.length; i++)
					{
						addRowSelectionInterval(oldrow[i], oldrow[i]);
					}
					scrollRectToVisible(UITable.this.getCellRect(oldrow[0], vc, false));
				}
			}
			else if (model instanceof ISortableTableModel)
			{
				((ISortableTableModel) model).sortByColumn(mc, sortstatusarray[vc] == SORT_UP ? true : false, oldrow);
				if (oldrow.length > 0)
				{
					setRowSelectionInterval(oldrow[0], oldrow[0]);
					for (int i = 1; i < oldrow.length; i++)
					{
						addRowSelectionInterval(oldrow[i], oldrow[i]);
					}
					scrollRectToVisible(UITable.this.getCellRect(oldrow[0], vc, false));
				}
			}
			else if (model instanceof ITableModel)
			{
				((ITableModel) model).sortByColumn(mc, sortstatusarray[vc] == SORT_UP ? true : false);
				Debug.error("如果希望排序后原有行保持选中状态，表模型请实现ISortableTableModel接口");
			}
			//有列处于排序
			isSorting = true;
			getTableHeader().repaint();
			repaint();
		}
	}

	/**
	 * 排序状态初始化
	 */
	private void initSortStatus()
	{
		if (getModel() == null)
			return;
		int ncol = getModel().getColumnCount();
		if (ncol == 0)
			return;
		sortstatusarray = null;
		sortstatusarray = new int[ncol];
		for (int i = 0; i < sortstatusarray.length; i++)
		{
			sortstatusarray[i] = SORT_NONE; // 不排序
		}
		isSorting = false;
		setSortItemList(new ArrayList<SortItem>());
		//刷新表头状态
		this.getTableHeader().repaint();
	}

	public void tableChanged(TableModelEvent e)
	{
		if (isSorting) //One column is sorting...
		{
			if (getModel() instanceof ISortableTableModel)
			{
				ISortableTableModel sortmodel = (ISortableTableModel) getModel();
				if (sortmodel.getSortColumn() == -1) //表格被刷新或者根本没有排序过
				{
					initSortStatus();
				}
			}
			if (getModel() instanceof IMutilSortableTableModel)
			{
				IMutilSortableTableModel sortmodel = (IMutilSortableTableModel) getModel();
				if (sortmodel.getSortColumns() == null || sortmodel.getSortColumns().size() == 0) //表格被刷新或者根本没有排序过
				{
					initSortStatus();
				}
			}
		}
		super.tableChanged(e);
	}

	/**
	 * 管理排序状态
	 * 
	 * @param vc
	 *            view column
	 * @param model
	 */
	private void maintainSortStatus(int vc, int colsize)
	{
		// 以vc为主，开关
		int nstatus = sortstatusarray[vc];
		for (int i = 0; i < sortstatusarray.length; i++)
		{
			sortstatusarray[i] = SORT_NONE; // 非排序状态
		}
		sortstatusarray[vc] = (nstatus == -1 ? SORT_UP : 1 - nstatus);
	}

	/*
	 * @see javax.swing.JTable#setModel(javax.swing.table.TableModel)
	 */
	public void setModel(TableModel dataModel)
	{
		super.setModel(dataModel);
		initHeaderCellRender();
	}

	/* 
	 * @see javax.swing.JTable#setColumnModel(javax.swing.table.TableColumnModel)
	 */
	public void setColumnModel(TableColumnModel columnModel)
	{
		super.setColumnModel(columnModel);
		initHeaderCellRender();
	}

	/**
	 * 初始化表头排序render
	 */
	private void initHeaderCellRender()
	{
		if (getTableHeader() == null)
			return; // 表体无表头
		initSortStatus();
		// 设置render
		TableColumnModel cm = getColumnModel();
		if (cm == null)
			return;
		for (int column = 0; column < cm.getColumnCount(); column++)
		{
			TableColumn aColumn = cm.getColumn(column);
			TableCellRenderer render = aColumn.getHeaderRenderer();
			if (render == null)
			{
				SortableHeaderRenderer hrender = new SortableHeaderRenderer();
				aColumn.setHeaderRenderer(hrender);
			}
		}
	}

	/**
	 * 设置列为非空列
	 * @param colIndex
	 */
	public void setColumnNotNullColor(int colIndex)
	{
		if (colIndex > getColumnCount() - 1)
			return;
		TableColumn tc = getColumnModel().getColumn(colIndex);
		if (tc == null)
			return;
		TableCellRenderer renderer = tc.getHeaderRenderer();
		if (renderer == null || !(renderer instanceof nc.ui.bill.tools.DefaultTableHeaderCellRenderer))
		{
			renderer = new nc.ui.bill.tools.DefaultTableHeaderCellRenderer();
			tc.setHeaderRenderer(renderer);
		}
		((nc.ui.bill.tools.DefaultTableHeaderCellRenderer) renderer).setForeground(UILabel.notNullColor);
	}

	/**
	 * 设置列为非空列
	 * @param colIndex
	 */
	public void setColumnNotNullColor(String colIndentify)
	{
		TableColumn tc = getColumn(colIndentify);
		if (tc == null)
			return;
		TableCellRenderer renderer = tc.getHeaderRenderer();
		if (renderer == null || !(renderer instanceof nc.ui.bill.tools.DefaultTableHeaderCellRenderer))
		{
			renderer = new nc.ui.bill.tools.DefaultTableHeaderCellRenderer();
			tc.setHeaderRenderer(renderer);
		}
		((nc.ui.bill.tools.DefaultTableHeaderCellRenderer) renderer).setForeground(UILabel.notNullColor);
	}

	// /**
	// * Updates the selection models of the table, depending on the state of
	// the
	// * two flags: <code>toggle</code> and <code>extend</code>. All changes
	// * to the selection that are the result of keyboard or mouse events
	// received
	// * by the UI are channeled through this method so that the behavior may be
	// * overridden by a subclass.
	// * <p>
	// * This implementation uses the following conventions:
	// * <ul>
	// * <li> <code>toggle</code>: <em>false</em>, <code>extend</code>:
	// <em>false</em>.
	// * Clear the previous selection and ensure the new cell is selected.
	// * <li> <code>toggle</code>: <em>false</em>, <code>extend</code>:
	// <em>true</em>.
	// * Extend the previous selection to include the specified cell.
	// * <li> <code>toggle</code>: <em>true</em>, <code>extend</code>:
	// <em>false</em>.
	// * If the specified cell is selected, deselect it. If it is not selected,
	// select it.
	// * <li> <code>toggle</code>: <em>true</em>, <code>extend</code>:
	// <em>true</em>.
	// * Leave the selection state as it is, but move the anchor index to the
	// specified location.
	// * </ul>
	// * @param rowIndex affects the selection at <code>row</code>
	// * @param columnIndex affects the selection at <code>column</code>
	// * @param toggle see description above
	// * @param extend if true, extend the current selection
	// *
	// */
	// public void changeSelection(
	// int rowIndex,
	// int columnIndex,
	// boolean toggle,
	// boolean extend) {
	// javax.swing.ListSelectionModel rsm = getSelectionModel();
	// javax.swing.ListSelectionModel csm =
	// getColumnModel().getSelectionModel();
	//
	// // Update column selection model
	// changeSelectionModel(csm, columnIndex, toggle, extend);
	//
	// // Update row selection model
	// changeSelectionModel(rsm, rowIndex, toggle, extend);
	//
	// // Scroll after changing the selection as blit scrolling is immediate,
	// // so that if we cause the repaint after the scroll we end up painting
	// // everything!
	// // Autoscrolling support.
	// if (getAutoscrolls()) {
	// java.awt.Rectangle cellRect = getCellRect(rowIndex, columnIndex, false);
	// if (cellRect != null) {
	// scrollRectToVisible(cellRect);
	// }
	// }
	// }
	// private void changeSelectionModel(
	// javax.swing.ListSelectionModel sm,
	// int index,
	// boolean toggle,
	// boolean extend) {
	// if (extend) {
	// if (toggle) {
	// sm.setAnchorSelectionIndex(index);
	// } else {
	// sm.setLeadSelectionIndex(index);
	// }
	// } else {
	// if (toggle) {
	// if (sm.isSelectedIndex(index)) {
	// sm.removeSelectionInterval(index, index);
	// } else {
	// sm.addSelectionInterval(index, index);
	// }
	// } else {
	// sm.setSelectionInterval(index, index);
	// }
	// }
	// }
	/**
	 * 建立默认表头
	 * 
	 */
	protected javax.swing.table.JTableHeader createDefaultTableHeader()
	{
		return new GroupableTableHeader(columnModel);
	}

	/**
	 * param@ col--the column to locate. param@ value--the finding String. value
	 * is the whole cellValue or part of cellValue. When value is part of
	 * cellValue,you must add prefix(%) or subfix(%). But not support with
	 * prefix(%) and subfix(%) at the same time; param@
	 * caseMatched--caseSensitive or not 创建日期:(2003-2-12 15:08:51)
	 */
	// private String doLocateCell(int col, String value, boolean caseMatched) {
	//
	// if (col == -1)
	// return nc.ui.ml.NCLangRes.getInstance().getStrByID("_Beans",
	// "UPP_Beans-000080")/* @res "没有选择列" */;
	// if (value == null || value.length() == 0)
	// return nc.ui.ml.NCLangRes.getInstance().getStrByID("_Beans",
	// "UPP_Beans-000081")/* @res "定位字符串为空" */;
	//
	// javax.swing.table.TableModel model = this.getModel();
	// if (model == null)
	// return nc.ui.ml.NCLangRes.getInstance().getStrByID("_Beans",
	// "UPP_Beans-000082")/* @res "没有数据" */;
	// int rows = model.getRowCount();
	// if (rows == 0)
	// return null;
	//
	// // filter type 0-- no filter("XXX"), 1--prefix("XXX%"),
	// // 2--subfix("%XXX")
	// int filter = 0;
	// if (value.endsWith("%")) {
	// filter = 1;
	// value = value.substring(0, value.length() - 1);
	// }
	// else if (value.startsWith("%")) {
	// filter = 2;
	// value = value.substring(1);
	// }
	//
	// // change to lowercase
	// if (!caseMatched)
	// value = value.toLowerCase();
	//
	// // find the row
	// int row = -1;
	// for (int i = 0; i < rows; i++) {
	// Object o = model.getValueAt(i, col);
	// if (o != null) {
	// String cellVal = o.toString();
	// if (!caseMatched)
	// cellVal = cellVal.toLowerCase();
	// // System.out.println("row="+i+", col=" + col);
	// // System.out.println("cell="+cellVal + ", filter="+filter);
	// if ((filter == 0 && cellVal.equals(value))
	// || (filter == 1 && cellVal.startsWith(value))
	// || (filter == 2 && cellVal.endsWith(value))) {
	// row = i;
	// break;
	// }
	// }
	// }
	// if (row != -1) {
	// // if found the row
	// // first stop celleditor
	// if (getCellEditor() != null)
	// getCellEditor().stopCellEditing();
	//
	// // set selection row
	// this.requestFocus();
	// this.setRowSelectionInterval(row, row);
	// this.setColumnSelectionInterval(selectedHeaderColumn,
	// selectedHeaderColumn);
	// // this.setColumnSelectionInterval(col, col);
	// return null;
	// }
	//
	// return nc.ui.ml.NCLangRes.getInstance().getStrByID("_Beans",
	// "UPP_Beans-000083")/* @res "没有找到" */
	// + "!";
	// }
	public boolean editCellAt(int row, int column, java.util.EventObject e)
	{
		boolean editable = super.editCellAt(row, column, e);
		// changed jdk1.4
		// if (editable && editorComp instanceof javax.swing.JComboBox) {
		// editorComp.requestFocus();
		// }
		return editable;
	}

	public TableCellEditor getCellEditor(int row, int column)
	{
		TableCellEditor editor = super.getCellEditor(row, column);
		if (editor instanceof UITableCellEditor)
		{
			int maxWidth = 0;
			TableColumnModel model = getColumnModel();
			// 使用Table列宽计算:
			for (int i = column; i < model.getColumnCount(); i++)
			{
				maxWidth += model.getColumn(i).getWidth();
			}
			if (getParent() instanceof JViewport)
			{
				// 使用Viewport的宽度计算:
				int width = (int) (((JViewport) getParent()).getSize().getWidth() - ((UIVarLenTextField) ((UITableCellEditor) editor).getComponent())
						.getBounds().getX());
				if (width < maxWidth)
				{
					maxWidth = width;
				}
			}
			//
			((UIVarLenTextField) ((UITableCellEditor) editor).getComponent()).setMaxWidth(maxWidth);
			((UIVarLenTextField) ((UITableCellEditor) editor).getComponent()).setMinWidth(model.getColumn(column).getWidth());
		}
		return editor;
	}

	@SuppressWarnings("unchecked")
	public TableCellEditor getDefaultEditor(Class columnClass)
	{
		TableCellEditor editor = super.getDefaultEditor(columnClass);
		final Component component;
		if (editor != null)
		{
			if (editor instanceof DefaultCellEditor)
			{
				component = ((DefaultCellEditor) editor).getComponent();
				if (!(editor instanceof UITableCellEditor))
				{
					if (component instanceof JTextField)
					{
						editor = new UITableCellEditor(new UIVarLenTextField());
						defaultEditorsByColumnClass.put(columnClass, editor);
					}
				}
			}
		}
		return editor;
	}

	/**
	 * 
	 * 创建日期:(2003-3-20 16:54:01)
	 * 
	 * @return nc.ui.pub.beans.UIPopupMenu
	 */
	public UIPopupMenu getHeaderPopupMenu()
	{
		return headerPopupMenu;
	}

	/**
	 * 
	 * 创建日期:(2003-3-20 11:14:33)
	 * 
	 * @return nc.ui.pub.beans.UIMenuItem
	 */
	public UIMenuItem getLocateMenu()
	{
		return locateMenu;
	}

	/**
	 * 
	 * 创建日期:(2003-3-18 14:41:30)
	 * 
	 * @return int
	 */
	public int getSelectedHeaderColumn()
	{
		return selectedHeaderColumn;
	}

	/**
	 * 
	 * 创建日期:(2001-11-7 14:03:57)
	 * 
	 * @return nc.ui.pub.beans.UITable
	 */
	private UITable getUITable()
	{
		return this;
	}

	/**
	 * 
	 * 创建日期:(2003-2-12 15:08:51)
	 */
	private void headerPopupAction(java.awt.event.MouseEvent e)
	{
		JTableHeader th = getTableHeader();
		// Locate the renderer under the event location
		selectedHeaderColumn = th.columnAtPoint(e.getPoint());
		if (selectedHeaderColumn < 0)
			return;
		if (!cellLacateEnabled)
			return;
		if (getModel() == null || getModel().getRowCount() == 0)
			return;
		if (selectedHeaderColumn >= 0 && headerPopupMenu != null)
			headerPopupMenu.show((java.awt.Component) e.getSource(), e.getX(), e.getY());
	}

	/**
	 * 
	 * 创建日期:(2003-3-20 16:55:14)
	 */
	private void initHeaderPopupMenu()
	{
		// add popupmouseAdapter
		getTableHeader().addMouseListener(new HeaderPopupMouseAdapter());
		locateMenu.addActionListener(new LocateMenuActionListener());
		setCellLacateEnabled(cellLacateEnabled);
	}

	/**
	 * 
	 * 创建日期:(2001-4-27 9:39:38)
	 */
	private void initialize()
	{
		// 根据版本号,决定对齐表格,1.2不须设置
		setSurrendersFocusOnKeystroke(true);// jdk1.4
		javax.swing.table.JTableHeader jth = (javax.swing.table.JTableHeader) getTableHeader();
		jth.addMouseMotionListener(new java.awt.event.MouseMotionAdapter()
		{
			public void mouseDragged(java.awt.event.MouseEvent e)
			{
				if (getUITable().getCellEditor() != null)
				{
					getUITable().getCellEditor().stopCellEditing();
				}
			}
		});
		// initHeaderPopupMenu
		initHeaderPopupMenu();
		// register key action
		registerKey();
		// 初始化表头render
		initHeaderCellRender();
		//根据人机要求进行的一些修改设置
		initSetting();
	}

	/**
	 * 
	 * lkp add
	 * 根据人机设计要求，设置背景色及标准宽度
	 *
	 */
	private void initSetting()
	{
		//设置TableHeader,JDK1.5  JTable.updateUI()时会将表头UI恢复成默认;
		if (getTableHeader() != null)
			getTableHeader().setUI(new GroupableTableHeaderUI());
		/*设置表格外部边框颜色*/
		setBorder(new NCLineBorder(UIManager.getColor("Table.borderColor")));
		setRowMargin(1);
		/*每行高度18pix*/
		setRowHeight(UITABLE_CELL_HEIGHT);
		/** 设置表头高度19 */
		setHeaderHeight(UITABLE_HEAD_HEIGHT);
		
		// added by wanglei 2011-8-19  可以拖表头排序
		this.getTableHeader().setReorderingAllowed(true);
	}

	/**
	 * 
	 * 创建日期:(2003-2-12 15:16:42)
	 * 
	 * @return boolean
	 */
	public boolean isCellLacateEnabled()
	{
		return cellLacateEnabled;
	}

	/**
	 * 
	 * 创建日期:(2003-2-20 11:42:23)
	 * 
	 * @return boolean
	 */
	public boolean isDefaultHeader()
	{
		return defaultHeader;
	}

	/**
	 * 
	 * 创建日期:(2003-2-13 15:09:22)
	 * 
	 * @return boolean
	 */
	public boolean isLocateCaseMatched()
	{
		return locateCaseMatched;
	}

	/**
	 * 获取 sortEnabled 特性 (boolean) 值.
	 * 
	 * @return sortEnabled 特性值.
	 * @see #setSortEnabled
	 */
	public boolean isSortEnabled()
	{
		return fieldSortEnabled;
	}

	/**
	 * 
	 * 创建日期:(2002-3-18 14:07:45)
	 * 
	 * @return boolean
	 */
	public boolean isTranslate()
	{
		return translate;
	}

	/**
	 * 
	 * 创建日期:(2003-2-12 15:08:51)
	 */
	public void locateCell()
	{
		if (selectedHeaderColumn == -1)
			return;
		if (getModel() == null || getModel().getRowCount() == 0)
		{
			nc.ui.pub.beans.util.NCOptionPane.showMessageDialog(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("_Beans", "UPP_Beans-000082")/* @res "没有数据" */
					+ ".", nc.ui.ml.NCLangRes.getInstance().getStrByID("_Beans", "UPP_Beans-000053")/* @res "提示" */
					+ ":", nc.ui.pub.beans.util.NCOptionPane.OK_CANCEL_OPTION);
			return;
		}
		showSearchDialog();
		/*
		 * String colName = null; try { Object o =
		 * getTableHeader().getColumnModel().getColumn(selectedHeaderColumn).getHeaderValue();
		 * colName = o == null ? "" : o.toString(); //else if(renderer
		 * instanceof group } catch (Exception e) { }
		 * 
		 * if (colName != "") colName = "\"" + colName + "\""; String findString =
		 * nc.ui.pub.beans.util.NCOptionPane.showInputDialog( this, "输入定位的值
		 * (\"xxx\",或者\"%xxx\",或者\"xxx%\") :", colName + "列定位:",
		 * nc.ui.pub.beans.util.NCOptionPane.OK_CANCEL_OPTION);
		 * 
		 * //(String) MessageDialog.showInputDlg(this, "输入定位的字符串:", "定位的字符串:",
		 * null); if (findString == null || findString.length() == 0) return;
		 * 
		 * int realCol = getRealColumn(selectedHeaderColumn);
		 * 
		 * String msg = doLocateCell(realCol, findString, locateCaseMatched); if
		 * (msg != null) { System.out.println("Table cell lacate message: " +
		 * msg + "!!!"+"!");
		 * nc.ui.pub.beans.util.NCOptionPane.showMessageDialog(this, msg); }
		 */
	}

	private void showSearchDialog()
	{
		searchDialog = new UITableSearchDialog(this, this);
		searchDialog.showModal();
	}

	/**
	 * 
	 * 创建日期:(2002-4-19 16:29:13)
	 * 
	 * @param keyEvent
	 *            java.awt.event.KeyEvent
	 */
	protected void processKeyEvent(java.awt.event.KeyEvent keyEvent)
	{
		// add by hxr to process shortcut
		// int[] rows = getSelectedRows();
		// int[] cols = getSelectedColumns();
		// int oldrow = getSelectedRow();
		// int oldcol = getSelectedColumn();
		// System.out.println("controldown::" + keyEvent.isControlDown() + "
		// keytype::" + keyEvent.getID() == KeyEvent.KEY_TYPED);
		// ctrl + X, is defined shortcut, so skiped.
		super.processKeyEvent(keyEvent);
		// if (keyEvent.getID() == KeyEvent.KEY_PRESSED && keyEvent.getKeyCode()
		// == java.awt.event.KeyEvent.VK_F3) {
		// if (keyEvent.getID() == KeyEvent.KEY_PRESSED &&
		// keyEvent.isControlDown()
		// && keyEvent.getKeyCode() == java.awt.event.KeyEvent.VK_T) {
		// java.awt.Component parent = keyEvent.getComponent();
		// while (parent.getParent() != null) {
		// if (parent instanceof javax.swing.JTable || parent instanceof
		// javax.swing.JViewport) {
		// parent = parent.getParent();
		// } else
		// break;
		// }
		// if (MiscUtils.findChildByClass(parent.getParent(),
		// nc.ui.pub.bill.BillCardPanel.class) == null)
		// parent.transferFocus();
		// //keyEvent.consume();
		// return;
		// }
		// 处理表格中参照用F2直接调出的问题
		// Component comp = null;
		// if (getCellEditor() instanceof nc.ui.bd.manage.UIRefCellEditor) {
		// comp = ((nc.ui.bd.manage.UIRefCellEditor) getCellEditor())
		// .getComponent();
		// } else if (getCellEditor() instanceof
		// nc.ui.bd.manage.UIRefCellEditorNew) {
		// comp = ((nc.ui.bd.manage.UIRefCellEditorNew) getCellEditor())
		// .getComponent();
		// } else if (getCellEditor() instanceof nc.ui.pub.bill.BillCellEditor)
		// {
		// comp = ((nc.ui.pub.bill.BillCellEditor) getCellEditor())
		// .getComponent();
		// }
		// if (comp != null) {
		// if (comp instanceof UIRefPane
		// && keyEvent.getID() == KeyEvent.KEY_PRESSED
		// && keyEvent.getKeyCode() == KeyEvent.VK_F2) {
		// if (((UIRefPane) comp).isButtonVisible()
		// && isCellEditable(this.getSelectedRow(), this
		// .getSelectedColumn())) {
		// editCellAt(this.getSelectedRow(), this.getSelectedColumn());
		// ((UIRefPane) comp).getUITextField().requestFocus();
		// ((UIRefPane) comp).connEtoC1();
		// return;
		// }
		// }
		// }
		if (getCellEditor() instanceof DefaultCellEditor)
		{
			DefaultCellEditor dce = (DefaultCellEditor) getCellEditor();
			if (dce.getComponent() instanceof nc.ui.pub.beans.table.UIVarLenTextField)
			{
				nc.ui.pub.beans.table.UIVarLenTextField uvtf = (nc.ui.pub.beans.table.UIVarLenTextField) dce.getComponent();
				uvtf.adjustLength();
			}
		}
		// used to process keyboard
		// if (isEnabled()
		// && keyEvent.getID() == java.awt.event.KeyEvent.KEY_PRESSED
		// && keyEvent.getKeyCode() == KeyEvent.VK_ENTER
		// && MiscUtils.getIndexOfInt(cols, this.getColumnCount() - 1) >= 0
		// && MiscUtils.getIndexOfInt(rows, this.getRowCount() - 1) >= 0) {
		// FocusUtils.focusNextComponent(this);
		// }
		// //select editable cell
		// if (isNavigateEditableCell() && isEnabled()
		// && keyEvent.getID() == java.awt.event.KeyEvent.KEY_PRESSED
		// && keyEvent.getKeyCode() == KeyEvent.VK_ENTER){
		// int row = getSelectedRow();
		// int col = getSelectedColumn();
		// if(row >=0 && col >=0 && row != oldrow || col != oldcol) {
		// if(hasFocus()&& getModel() != null &&
		// !getModel().isCellEditable(row,convertColumnIndexToModel(col))){
		// selectNextEditableCell(row,col);
		// }
		// }
		// }
	}

	/**
	 * 
	 * 创建日期:(2002-6-26 8:59:15)
	 */
	public void removeSortListener()
	{
		setSortEnabled(false);
	}

	/**
	 * 排序后选择显示. 创建日期:(2001-11-22 13:32:58)
	 */
	// private void reSelect(int[] oldSortIndex, int oldrow) {
	// if (oldSortIndex == null) {
	// oldSortIndex = new int[getModel().getRowCount()];
	// for (int i = 0; i < oldSortIndex.length; i++) {
	// oldSortIndex[i] = i;
	// }
	// }
	// if (oldrow > -1 && oldSortIndex != null) {
	// int[] array = null;
	// TableModel model = getModel();
	// if (model instanceof NCTableModel)
	// array = ((NCTableModel) model).getSortIndexes();
	// else if (model instanceof VOTableModel)
	// array = ((VOTableModel) model).getSortIndexes();
	// if (array != null) {
	// int[] array1 = new int[array.length];
	// for (int i = 0; i < array.length; i++) {
	// array1[array[i]] = i;
	// }
	//
	// int newrow = array1[oldSortIndex[oldrow]];
	// // System.out.println("oldrow:" + newrow);
	// setRowSelectionInterval(newrow, newrow);
	// scrollRectToVisible(getCellRect(newrow, 0, false));
	// }
	// }
	// }
	// private void selectNextEditableCell(int row, int col) {
	// if (row >= 0 && col >= 0) {
	// if (!getModel().isCellEditable(row, convertColumnIndexToModel(col))) {
	// if (col < getColumnCount() - 1) {
	// selectNextEditableCell(row, col + 1);
	// }
	// else if (row < getRowCount() - 1)
	// selectNextEditableCell(row + 1, 0);
	// else if (col == getColumnCount() - 1
	// && row == getRowCount() - 1)
	// changeSelection(row, col, false, false);
	// }
	// else {
	// changeSelection(row, col, false, false);
	// }
	// }
	// }
	/**
	 * 
	 * 创建日期:(2003-2-12 15:16:42)
	 * 
	 * @param newCellLacateEnabled
	 *            boolean
	 */
	public void setCellLacateEnabled(boolean newCellLacateEnabled)
	{
		cellLacateEnabled = newCellLacateEnabled;
		if (cellLacateEnabled)
		{
			if (headerPopupMenu.getComponentIndex(locateMenu) < 0)
				headerPopupMenu.add(locateMenu);
		}
		else
		{
			headerPopupMenu.remove(locateMenu);
		}
	}

	/**
	 * 设置表列宽度.
	 * 
	 * @param iWidth
	 *            int[] 列宽数据
	 * @return getColumnModel().getTotalColumnWidth() int 表总宽度
	 */
	public int setColumnWidth(int[] iWidth)
	{
		for (int i = 0; i < getColumnCount(); i++)
		{
			String sColumnName = getColumnName(i);
			if (i >= iWidth.length || iWidth[i] <= 0)
				super.getColumn(sColumnName).setPreferredWidth(10);
			else
				super.getColumn(sColumnName).setPreferredWidth(iWidth[i]);
		}
		return getColumnModel().getTotalColumnWidth();
	}

	/**
	 * 
	 * 创建日期:(2003-2-20 11:42:23)
	 * 
	 * @param newDefaultHeader
	 *            boolean
	 */
	public void setDefaultHeader(boolean newDefaultHeader)
	{
		defaultHeader = newDefaultHeader;
		if (defaultHeader)
			setTableHeader(super.createDefaultTableHeader());
	}

	/**
	 * 
	 * 创建日期:(2002-3-18 14:07:45)
	 * 
	 * @param newTranslate
	 *            boolean
	 */
	public void setEnterKeyNavigationHorizontal(boolean flag)
	{
		String key;
		InputMap map = getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
		ActionMap am = getActionMap();
		if (flag)
			key = TableNavigationalAction.SELECT_NEXT_COLUMN_CELL;
		else
			key = TableNavigationalAction.SELECT_NEXT_ROW_CELL;
		KeyStroke ks = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0, true);
		map.put(ks, key);
		TableNavigationalAction navigationalAction = TableNavigationalAction.createAction(key);
		navigationalAction.setFocusNextComponentIfEnd(true);
		am.put(key, navigationalAction);
	}

	public void setHeaderHeight(int iHeight)
	{
		// ((GroupableTableHeaderUI) ((GroupableTableHeader)
		// this.getTableHeader())
		// .getUI()).setHeaderHeight(iHeight);
		//
		JTableHeader h = getTableHeader();
		ComponentUI ui = h.getUI();
		if (ui instanceof GroupableTableHeaderUI)
			((GroupableTableHeaderUI) ui).setHeaderHeight(iHeight);
		else
		{
			h.setSize(h.getWidth(), iHeight);
		}
	}

	/**
	 * 
	 * 创建日期:(2003-2-13 15:09:22)
	 * 
	 * @param newLocateCaseMatched
	 *            boolean
	 */
	public void setLocateCaseMatched(boolean newLocateCaseMatched)
	{
		locateCaseMatched = newLocateCaseMatched;
	}

	/**
	 * 设置 sortEnabled 特性 (boolean) 值.
	 * 
	 * @param sortEnabled
	 *            新的特性值.
	 * @see #getSortEnabled
	 */
	public void setSortEnabled(boolean sortEnabled)
	{
		boolean oldValue = fieldSortEnabled;
		fieldSortEnabled = sortEnabled;
		firePropertyChange("sortEnabled", new Boolean(oldValue), new Boolean(sortEnabled));
	}

	/**
	 * 
	 * 创建日期:(2002-3-18 14:07:45)
	 * 
	 * @param newTranslate
	 *            boolean
	 */
	public void setTranslate(boolean newTranslate)
	{
		// translate = newTranslate;
	}

	private void registerKey()
	{
		// set Horizontal navigation orientation
		setEnterKeyNavigationHorizontal(true);
		String key;
		KeyStroke ks;
		InputMap map = getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
		ActionMap am = getActionMap();
		key = DoNothingAction.DO_NOTHING;
		// inputmap need one donothing key,but actionmap cann't have the
		// corresponding action.
		// if not have one key,it will get parent's action
		// if have donothing action ,it will return true and skip other's
		// binding key action after run donothingaction
		map.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0, false), key);
		am.put(key, DoNothingAction.getDefaultAction());
		// key = TableNavigationalAction.SELECT_NEXT_ROW_CELL;
		// KeyStroke ks = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0, true);
		// map.put(ks, key);
		// TableNavigationalAction navigationalAction = TableNavigationalAction
		// .createAction(key);
		// // action.setBeforeNavigateAction(new AddRowAction(this,ks));
		// navigationalAction.setFocusNextComponentIfEnd(true);
		// am.put(key, navigationalAction);
		key = TableShortCutAction.SHORTCUT_KEY_CTRL_T;
		ks = KeyStroke.getKeyStroke(KeyEvent.VK_T, KeyEvent.CTRL_MASK, false);
		map.put(ks, key);
		am.put(key, TableShortCutAction.createAction(key));
		key = TableShortCutAction.SHORTCUT_KEY_F2;
		ks = KeyStroke.getKeyStroke(KeyEvent.VK_F2, 0, true);
		map.put(ks, key);
		am.put(key, TableShortCutAction.createAction(key));
		// key = "pressed, not SHIFT, CTRL, ALT, \"accepted\" by editor";
		// ks = KeyStroke.getKeyStroke(key);
		// if(ks != null)
		// map.put(ks,key);
	}

	public TableNavigationalAction getEnterKeyNavigationalAction()
	{
		InputMap map = getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
		KeyStroke ks = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0, true);
		Object key = map.get(ks);
		if (key != null)
		{
			Action ac = (Action) getActionMap().get(key);
			if (ac instanceof TableNavigationalAction)
				return (TableNavigationalAction) ac;
		}
		return null;
	}

	public void setNotNullColumnByModelColumns(int[] cols)
	{
		notNullCols = cols;
		if (getTableHeader() != null)
			getTableHeader().repaint(getTableHeader().getVisibleRect());
		// revalidate();
		// repaint();
	}

	public int[] getNotNullColumnByModelColumns()
	{
		return notNullCols;
	}

	// set default selected row and column
	// public void paint(Graphics g) {
	// super.paint(g);
	// if (getRowCount() > 0) {
	// if (getSelectedColumn() < 0 && getSelectedRow() < 0) {
	// changeSelection(0,0,false,false);
	// } else {
	// if (getSelectedRow() < 0)
	// setRowSelectionInterval(0, 0);
	// if (getSelectedColumn() < 0)
	// setColumnSelectionInterval(0, 0);
	// }
	// }
	// }
	protected boolean processKeyBinding(KeyStroke ks, KeyEvent e, int condition, boolean pressed)
	{
		if (e.isControlDown() || e.isAltDown() || e.getKeyCode() == KeyEvent.VK_F6 || e.getKeyCode() == KeyEvent.VK_F7)
		{
			setSurrendersFocusOnKeystroke(false);
		}
		if (e.getID() == KeyEvent.KEY_TYPED && e.getKeyChar() != '\n')
		{
			int anchorRow = getSelectionModel().getAnchorSelectionIndex();
			int anchorColumn = getColumnModel().getSelectionModel().getAnchorSelectionIndex();
			if (anchorRow != -1 && anchorColumn != -1 && !isEditing())
			{
				if (!editCellAt(anchorRow, anchorColumn))
				{
					return false;
				}
			}
		}
		boolean retValue = super.processKeyBinding(ks, e, condition, pressed);
		setSurrendersFocusOnKeystroke(true);
		return retValue;
	}

	public void sizeColumnsToFitTitle()
	{
		setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		java.awt.FontMetrics fm = getTableHeader().getFontMetrics(getTableHeader().getFont());
		TableColumnModel cm = getColumnModel();
		if (cm != null && fm != null)
		{
			for (int i = 0; i < cm.getColumnCount(); i++)
			{
				TableColumn tc = cm.getColumn(i);
				Object title = tc.getHeaderValue();
				String sTitle = title == null ? null : title.toString();
				if (sTitle != null)
				{
					int l = fm.stringWidth(sTitle) + 5;
					if (l > tc.getWidth())
						tc.setWidth(l);
				}
			}
			for (int i = 0; i < cm.getColumnCount(); i++)
			{
				sizeColumnsToFit(i);
			}
		}
	}

	private void setSortItemList(List<SortItem> sortItemList)
	{
		this.sortItemList = sortItemList;
	}

	private List<SortItem> getSortItemList()
	{
		return sortItemList;
	}

	public Icon getSortIcon(int column){
		Icon sortIcon = null;
		if (getModel() instanceof IMutilSortableTableModel)
		{
			int mc = convertColumnIndexToModel(column);
			SortItem item = getSortItemByColumn(mc);
			if (fieldSortEnabled && item != null)
			{
				Icon icon = item.isAscending() ? ICON_UP : ICON_DOWN;
				sortIcon = new SortIcon(icon, String.valueOf(getSortItemList().indexOf(item) + 1));
			}
		}
		else
		{
			sortIcon = getIconBySortStatus(column);
		}
		
		return sortIcon;
	}

	/**
	 * 取得图标
	 * 
	 * @param vc
	 * @return
	 */
	private Icon getIconBySortStatus(int vc)
	{
		Icon result = null;
		if (fieldSortEnabled && sortstatusarray != null)
		{
			int nstatus = sortstatusarray[vc];
			switch (nstatus)
			{
			case SORT_UP:
				result = ICON_UP;
				break;
			case SORT_DOWN:
				result = ICON_DOWN;
				break;
			default:
				break;
			}
		}
		return result;
	}
}