package nc.ui.report.base;

import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import nc.bs.logging.Logger;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.FramePanel;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIScrollPane;
import nc.ui.pub.beans.UISplitPane;
import nc.ui.pub.beans.UITable;
import nc.ui.pub.bill.BillModel;
import nc.ui.pub.print.PrintDirectEntry;
import nc.ui.pub.report.ReportBaseClass;
import nc.ui.pub.report.ReportItem;
import nc.ui.querytemplate.QueryConditionDLG;
import nc.ui.trade.report.controller.IReportCtl;
import nc.ui.trade.report.controller.ReportCtl;
import nc.ui.trade.report.group.GroupTableModel;
import nc.ui.trade.report.query.QueryDLG;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.report.ReportModelVO;
import nc.vo.pub.report.SubtotalContext;
import nc.vo.querytemplate.TemplateInfo;
import nc.vo.trade.report.ConvertTool;
import nc.vo.trade.report.IReportModelSelectType;
import nc.vo.trade.report.ReportDataType2UFDateType;
import nc.vo.trade.report.ReportVO;
import nc.vo.trade.report.ReportVOMetaClass;
import nc.vo.trade.report.TableField;

/**
 * 汇总报表基类
 * 
 * @author：刘剑 修改 dengjt
 */

public abstract class ReportUIBase extends nc.ui.pub.ToftPanel {
	// 在报表上显示查询条件的Panel
	private nc.ui.pub.beans.UIPanel conditionPanel = null;

	// 分组列表与表体分隔栏
	private UISplitPane veriSplitPane = null;

	// 报表模板
	private ReportBaseClass m_report = null;

	// 用来批量打印的报表模板
	private ReportBaseClass m_reportForPrint = null;

	// 分隔符号，
	protected static final char columnSystemDelimiter = '_';

	// 报表模板字段组
	// if the value of column_system fields of ReportModelVOs has same prefix
	// which follow by character '_'
	// then we say these RemportModelVO blong to a group
	// we put column_code values of ReportModelVOs of a group into a arraylist
	// and put the ArrayList into another Arraylist
	private ArrayList reportModelColumnGroups = new ArrayList();

	private IReportCtl m_uiCtl;

	// 备份的ReportModelVO,因为报表模板可能会改变这个东西，我们自己把它备份下来
	private nc.vo.pub.report.ReportModelVO[] copyOfReportModelVOs = null;

	// 查询对话框
	private QueryDLG m_qryDlg = null;

	// 保存自添加按钮以及相应事件处理类
	private ButtonAssets button_action_map = new ButtonAssets(this);

	// 分组显示Table
	private UITable groupTable = null;

	// 分组hash表
	private HashMap groupMap = new HashMap();

	// 分组key列表
	private ArrayList groupKeys = new ArrayList();

	private CircularlyAccessibleValueObject[] allBodyDataVO = null;

	private boolean needGroup = false;

	private UIScrollPane groupSPane = null;
	
	private QueryConditionDLG m_newqryDlg;

	/**
	 * TotalReportBase 构造子注解。
	 */
	public ReportUIBase() {
		super();
		initialize();
	}

	/**
	 * TotalReportBase 构造子注解。
	 */
	public ReportUIBase(FramePanel fp) {
		super();
		setFrame(fp);
		initialize();
	}

	/**
	 * 备份报表模板数据，因为在某些操作中可能被改变
	 */
	protected void backupReportModelVOs() {
		ArrayList al = new ArrayList();
		for (int i = 0; i < getReportBase().getAllBodyVOs().length; i++) {
			// 我们只备份 select_type!=未被选择的VO
			if (!getReportBase().getAllBodyVOs()[i].getSelectType().equals(
					IReportModelSelectType.UNSELECTED))
				al.add(getReportBase().getAllBodyVOs()[i].clone());

		}
		copyOfReportModelVOs = (nc.vo.pub.report.ReportModelVO[]) al
				.toArray(new nc.vo.pub.report.ReportModelVO[0]);

	}

	/**
	 * 组合查询条件
	 * 
	 * @return java.lang.String
	 * @param customSqlWhere
	 *            用户传入的查询条件
	 */
	protected String combineDefaultSqlWhere(String customSqlWhere) {

		String result = null;
		boolean isCustomSqlWhereEmpty = (customSqlWhere == null || customSqlWhere
				.length() == 0);
		boolean isDefaultSqlWhereEmpty = (getUIControl().getDefaultSqlWhere() == null || getUIControl()
				.getDefaultSqlWhere().trim().length() == 0);

		if (isCustomSqlWhereEmpty)
			result = getUIControl().getDefaultSqlWhere();
		if (isDefaultSqlWhereEmpty)
			result = customSqlWhere;
		if ((!isCustomSqlWhereEmpty) && (!isDefaultSqlWhereEmpty))
			result = customSqlWhere + " and "
					+ getUIControl().getDefaultSqlWhere();
		if (result != null && result.trim().length() == 0)
			result = null;
		return result;

	}

	/**
	 * 将以'_'分隔的ReportModel的fieldName转换为以'.'分隔的VOFieldName
	 * 
	 * @return java.lang.String
	 * @param reportFieldName
	 *            java.lang.String
	 */
	protected String convertReportModelFieldNameToVOFieldName(
			String reportFieldName) {
		if (reportFieldName.indexOf('_') == -1)
			return reportFieldName;
		else
			return reportFieldName.substring(0, reportFieldName.indexOf('_'))
					+ "."
					+ reportFieldName.substring(
							reportFieldName.indexOf('_') + 1, reportFieldName
									.length());
	}

	/**
	 * 将以'.'分隔的VOFieldName转换为以'_'分隔的ReportModel的fieldName
	 * 
	 * @return java.lang.String
	 * @param voFieldName
	 *            java.lang.String
	 */
	protected String convertVOFieldNameToReportModelFieldName(String voFieldName) {
		if (voFieldName.indexOf('.') == -1)
			return voFieldName;
		else
			return voFieldName.substring(0, voFieldName.indexOf('.'))
					+ "_"
					+ voFieldName.substring(voFieldName.indexOf('.') + 1,
							voFieldName.length());
	}

	/**
	 * 从传入的VO中创建查询字符串数组
	 * 
	 * @return java.lang.String[]
	 */
	protected String[] createConditionsFromConditionVO(
			nc.vo.pub.query.ConditionVO[] vos) {
		String[] conditions = new String[vos.length];
		for (int i = 0; i < vos.length; i++) {
			conditions[i] = vos[i].getFieldName() + vos[i].getOperaCode();
			if (vos[i].getRefResult() != null)
				conditions[i] += vos[i].getRefResult().getRefName();
			else
				conditions[i] += vos[i].getValue();
		}
		return conditions;
	}

	/**
	 * 默认返回从数据库中查询出的报表控制信息，如果需自己控制，只需重载此方法即可。
	 * 
	 * @return nc.ui.trade.report.controller.IReportCtl
	 */
	protected IReportCtl createIReportCtl() {
		return new ReportCtl();
	}

	/**
	 * 创建查询对话框
	 * 
	 * @return nc.ui.trade.report.query.QueryDLG
	 */
	protected QueryDLG createQueryDLG() {
		QueryDLG dlg = new QueryDLG();

		dlg.setTempletID(getUIControl()._getPk_corp(), getModuleCode(),
				getUIControl()._getOperator(), null);
		dlg.setNormalShow(false);
		return dlg;
	}

	/**
	 * 
	 * @return nc.vo.tm.report.TableField
	 * @param vo
	 *            nc.vo.pub.report.ReportModelVO
	 */
	protected TableField createTableFieldFromReportModelVO(
			nc.vo.pub.report.ReportModelVO vo) {
		return new TableField(convertReportModelFieldNameToVOFieldName(vo
				.getColumnCode()), vo.getColumnUser(), vo.getDataType()
				.intValue() == 1
				|| vo.getDataType().intValue() == 2);
	}

	/**
	 * 得到标题所有列的编码
	 * 
	 * @return java.lang.String[]
	 */
	protected String[] getAllColumnCodes() {
		nc.vo.pub.report.ReportModelVO[] vos = getReportBase().getAllBodyVOs();
		String[] names = new String[vos.length];
		for (int i = 0; i < vos.length; i++) {
			names[i] = vos[i].getColumnCode();
		}
		return names;
	}

	/**
	 * 得到所有的字段名称和数据类型
	 * 
	 * @param fields
	 *            java.util.ArrayList
	 * @param dataTypes
	 *            java.util.ArrayList
	 */
	protected void getAllFieldsAndDataType(ArrayList fields, ArrayList dataTypes) {
		if (fields == null || dataTypes == null)
			throw new IllegalArgumentException(
					"getQueryFieldsAndDataType param is null");

		nc.vo.pub.report.ReportModelVO[] vos = getModelVOs();

		// TableField[] vfs = getVisibleFields();
		// List vfl = Arrays.asList(vfs);
		for (int i = 0; i < vos.length; i++) {
			TableField f = createTableFieldFromReportModelVO(vos[i]);
			fields.add(f.getFieldName());
			dataTypes.add(new Integer(ReportDataType2UFDateType.convert(vos[i]
					.getDataType())));
		}

	}

	/**
	 * 此方法返回预定按钮和自定义按钮
	 * 
	 * @author dengjt 创建日期 2005-8-24
	 */
	private ButtonObject[] getAllBtnAry() {
		if (button_action_map.getVisibleButtonsByOrder().size() == 0)
			return null;
		return (ButtonObject[]) button_action_map.getVisibleButtonsByOrder()
				.toArray(new ButtonObject[0]);
	}

	/**
	 * 
	 * @return java.lang.String[]
	 * @param column_code
	 *            java.lang.String
	 */
	protected String[] getColumnGroupsByColumnCode(String column_code) {
		for (int i = 0; i < reportModelColumnGroups.size(); i++) {
			ArrayList al = (ArrayList) reportModelColumnGroups.get(i);
			if (al.contains(column_code))
				return (String[]) al.toArray(new String[0]);
		}

		return new String[0];
	}

	/**
	 * 
	 * @return nc.ui.pub.beans.UIPanel
	 */
	protected nc.ui.pub.beans.UIPanel getConditionPanel() {
		if (conditionPanel == null) {
			conditionPanel = new nc.ui.pub.beans.UIPanel();
			conditionPanel.setName("ConditionPanel");
			java.awt.FlowLayout l = new java.awt.FlowLayout();
			l.setAlignment(java.awt.FlowLayout.LEFT);
			l.setHgap(10);

			conditionPanel.setLayout(l);// java.awt.GridLayout(2,6,0,1));
		}
		return conditionPanel;
	}

	/**
	 * 得到报表模板上隐藏的列(select_type == 1,但又没在界面上显示的列。 select_type==2
	 * 的列被称为“终身隐藏列”排除在外)
	 * 
	 * @return nc.vo.tm.report.TableField[]
	 */
	protected TableField[] getInvisibleFields() {
		nc.vo.pub.report.ReportModelVO[] vos = getModelVOs();// getReportBase().getAllBodyVOs();
		ArrayList invisible = new ArrayList();
		for (int i = 0; i < vos.length; i++) {
			if (vos[i].getSelectType().intValue() == IReportModelSelectType.VISIBLE
					.intValue()) {
				try {
					getReportBase().getBillTable().getColumn(
							vos[i].getColumnUser());

				}
				// 抛出异常，说明界面上已经隐藏
				catch (Exception e) {
					invisible.add(createTableFieldFromReportModelVO(vos[i]));
				}

			}
		}

		TableField[] invisibleFields = (TableField[]) invisible
				.toArray(new TableField[0]);

		return invisibleFields;
	}

	/**
	 * 得到报表模板上的可见列，就是界面上可以看到的列
	 * 
	 * @return nc.vo.tm.report.TableField[]
	 */
	protected TableField[] getVisibleFields() {
		nc.vo.pub.report.ReportModelVO[] vos = getModelVOs();// getReportBase().getAllBodyVOs();
		ArrayList visible = new ArrayList();
		for (int i = 0; i < vos.length; i++) {
			if (vos[i].getSelectType().intValue() == IReportModelSelectType.VISIBLE
					.intValue()) {
				try {
					getReportBase().getBillTable().getColumn(
							vos[i].getColumnUser());
					visible.add(createTableFieldFromReportModelVO(vos[i]));
				} catch (Exception e) {

				}
			}
		}

		TableField[] visibleFields = (TableField[]) visible
				.toArray(new TableField[0]);

		return visibleFields;
	}

	public QueryDLG getQryDlg() {
		if (m_qryDlg == null) {
			m_qryDlg = createQueryDLG();
		}
		return m_qryDlg;
	}

	/**
	 * 
	 * @return nc.ui.report.base.ReportBase
	 */
	public ReportBaseClass getReportBase() {
		if (m_report == null) {
			try {
				m_report = new ReportBaseClass();
				m_report.setName("ReportBase");
				m_report.setTempletID(getUIControl()._getPk_corp(),
						getModuleCode(), getUIControl()._getOperator(), null);

				m_report.getBillTable().getSelectionModel()
						.addListSelectionListener(new ListSelectionListener() {

							public void valueChanged(ListSelectionEvent e) {
								updateAllButtons();
							}

						});
			} catch (Exception ex) {
				MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes
						.getInstance().getStrByID("uifactory_report",
								"UPPuifactory_report-000032")/*
																 * @res "错误信息"
																 */, nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"uifactory_report", "UPPuifactory_report-000033")/*
																			 * @res
																			 * "未找到报表模板!"
																			 */);
				Logger.error(ex.getMessage(), ex);
			}
		}
		return m_report;
	}

	protected UITable getGroupTable() {
		if (groupTable == null) {
			groupTable = new UITable(new DefaultTableModel());
			groupTable.addMouseListener(new MouseAdapter() {
				public void mouseReleased(MouseEvent e) {
					int row = groupTable.getSelectedRow();
					if (row == -1)
						return;
					int count = groupTable.getModel().getColumnCount();
					StringBuffer key = new StringBuffer();
					for (int i = 0; i < count; i++) {
						key
								.append(groupTable.getModel()
										.getValueAt(row, i) == null ? ""
										: groupTable.getModel().getValueAt(row,
												i).toString());
						if (i != count - 1)
							key.append(":");
					}
					ArrayList tmpVO = (ArrayList) groupMap.get(key.toString());
					if (tmpVO != null && tmpVO.size() > 0) {
						setBodyDataVO(
								(CircularlyAccessibleValueObject[]) tmpVO
										.toArray(new CircularlyAccessibleValueObject[0]),
								false);
					}
					setHeadItems(convertVOKeysToModelKeys((String[]) groupKeys
							.toArray(new String[0])),
							getValuesFromGroupTable(row));
				}

			});
		}
		return groupTable;
	}

	/**
	 * 根据模板标题设置报表title
	 * 
	 */
	protected void updateTitle() {
		updateTitle(getReportBase().getReportTitle());
	}

	/**
	 * 根据输入的字符串设置报表title
	 * 
	 * @param strTitle
	 */
	protected void updateTitle(String strTitle) {
		setTitleText(strTitle);
	}

	protected void updateReportBase() {
		if (m_report == null)
			return;
		try {
			m_report.setTempletID(getUIControl()._getPk_corp(),
					getModuleCode(), getUIControl()._getOperator(), null);
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
		}
	}

	/**
	 * 获得报表模版基类，此基类为连续打印预备。
	 * 
	 * @return nc.ui.report.base.ReportBase
	 */
	public ReportBaseClass getReportBaseForPrint() {
		if (m_reportForPrint == null) {
			try {
				m_reportForPrint = new ReportBaseClass();
				m_reportForPrint.setName("ReportBaseForPrint");
				m_reportForPrint.setTempletID(getUIControl()._getPk_corp(),
						getModuleCode(), getUIControl()._getOperator(), null);
			} catch (Exception ex) {
				System.out.println("基类:未找到报表模板......");
			}
		}
		return m_reportForPrint;
	}

	/**
	 * 得到对应于报表模板上所有列的VO的metaClass。
	 * 
	 * @return nc.vo.trade.report.ReportVOMetaClass
	 */
	protected ReportVOMetaClass getReportVOMetaClassOfAllFields()

	{
		ArrayList fs = new ArrayList();
		ArrayList ds = new ArrayList();
		getAllFieldsAndDataType(fs, ds);
		String[] fieldsname = (String[]) fs.toArray(new String[0]);
		Integer[] datatypes = (Integer[]) ds.toArray(new Integer[0]);
		String[] fieldAlias = (String[]) ConvertTool
				.createFieldAlias(fieldsname);

		return new ReportVOMetaClass(fieldsname, fieldAlias, datatypes,
				getUIControl().getAllTableAlias(), getUIControl()
						.getTableJoinClause());

	}

	/**
	 * 子类实现该方法，返回业务界面的标题。
	 * 
	 * @return java.lang.String
	 */
	public String getTitle() {
		return getReportBase().getReportTitle();
	}

	/**
	 * 返回当前UI对应的控制类实例。
	 * 
	 * @return nc.ui.tm.pub.ControlBase
	 */
	public IReportCtl getUIControl() {
		if (m_uiCtl == null)
			m_uiCtl = createIReportCtl();
		return m_uiCtl;
	}

	/**
	 * 这个方法仅在未动态的给UI增加列时才能正确工作
	 * 
	 * @return nc.vo.pub.CircularlyAccessibleValueObject[]
	 */
	public CircularlyAccessibleValueObject[] getVOFromUI() {
		ReportVOMetaClass voClass = getReportVOMetaClassOfAllFields();
		ReportItem[] items = getReportBase().getBody_Items();
		int rows = getReportBase().getRowCount();
		ReportVO[] result = new ReportVO[rows];
		for (int row = 0; row < rows; row++) {
			result[row] = voClass.createReportVO();
			for (int i = 0; i < items.length; i++) {
				// System.out.println(items[i].getKey());
				result[row].setAttributeValue(items[i].getKey(),
						getReportBase().getBodyValueAt(row, items[i].getKey()));

			}
		}

		return result;
	}

	/**
	 * 返回当前VO，如果从分组表中找不到，则直接从界面上得到。从界面上得到VO比较耗时，但是，如果数据和
	 * VO顺序等密切相关，则需直接getVOFromUI，因为一些操作，比如排序，没有在分组表中体现出来
	 * 
	 * @return
	 */
	public CircularlyAccessibleValueObject[] getCurrentVO() {
		CircularlyAccessibleValueObject[] cvos = null;
		if ((cvos = getCurrentVOFromGroupMap()) == null) {
			cvos = getVOFromUI();
		}
		return cvos;
	}

	/**
	 * 返回所有数据。
	 * 
	 * @return
	 * @throws Exception
	 */
	public CircularlyAccessibleValueObject[] getAllBodyDataVO() {
		if (allBodyDataVO == null) {
			try {
				allBodyDataVO = getVOFromUI();
			} catch (Exception e) {
				Logger.error(e.getMessage(), e);
			}
		}
		return allBodyDataVO;
	}

	/**
	 * 此处插入方法说明。
	 */
	protected void initColumnGroups() {
		nc.vo.pub.report.ReportModelVO[] vos = getModelVOs();// getReportBase().getAllBodyVOs();
		HashMap tmpHash = new HashMap();

		for (int i = 0; i < vos.length; i++) {
			int index = vos[i].getColumnSystem().indexOf(columnSystemDelimiter);
			String key;
			ArrayList al;
			if (index != -1) {
				key = vos[i].getColumnSystem().substring(0, index);
			} else {
				key = vos[i].getColumnSystem();
			}
			if (tmpHash.get(key) == null) {
				al = new ArrayList();
				tmpHash.put(key, al);
			} else {
				al = (ArrayList) tmpHash.get(key);
			}
			al.add(vos[i].getColumnCode());

		}
		reportModelColumnGroups.addAll(tmpHash.values());
	}

	private void initialize() {
		setName("GeneralPane");
		setSize(774, 419);

		UISplitPane horiSplitPane = new UISplitPane(UISplitPane.VERTICAL_SPLIT,
				null, getVeriSplitPane());
		// 根据控制信息，决定是否显示查询条件显示栏
		if (!getUIControl().isShowCondition()) {
			horiSplitPane.setLeftComponent(null);
			horiSplitPane.setDividerLocation(0);
			horiSplitPane.setEnabled(false);
			horiSplitPane.setDividerSize(0);
		} else {
			horiSplitPane.setLeftComponent(getConditionPanel());
			horiSplitPane.setDividerLocation(80);
			horiSplitPane.setOneTouchExpandable(true);
			horiSplitPane.setDividerSize(6);
		}

		add(horiSplitPane);

		// 设置自定义按钮
		setPrivateButtons();

		getReportBase();

		setButtons(getAllBtnAry());
		// 更新按钮，使初始状态只能查询。 dengjt
		updateAllButtons();
		// 备份模板model VO
		backupReportModelVOs();

		if (getUIControl().getGroupKeys() != null)
			needGroup = true;

		initColumnGroups();

		setDigitFormat();

		getReportBase().setShowNO(true);

	}

	/**
	 * @param veriSplitPane
	 */
	private void setVeriSplitEnabled(boolean enabled) {
		if (enabled) {
			veriSplitPane.setLeftComponent(getGroupPanel());
			veriSplitPane.setDividerLocation(200);
			veriSplitPane.setEnabled(true);
			veriSplitPane.setDividerSize(15);
			veriSplitPane.setOneTouchExpandable(true);
			getReportBase().setShowNO(true);
		} else {
			veriSplitPane.setLeftComponent(null);
			veriSplitPane.setDividerLocation(0);
			veriSplitPane.setEnabled(false);
			veriSplitPane.setDividerSize(0);
			veriSplitPane.setOneTouchExpandable(false);
			getReportBase().setShowNO(false);
		}
	}

	/**
	 * 响应按钮事件。
	 * 
	 * @param bo
	 */
	public void onButtonClicked(ButtonObject bo) {
		try {
			if (button_action_map.get(bo) != null) {
				IButtonActionAndState action = (IButtonActionAndState) button_action_map
						.get(bo);
				action.execute();
			}
		} catch (nc.vo.pub.BusinessException ex) {
			showErrorMessage(ex.getMessage());
			Logger.error(ex.getMessage(), ex);
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
		}

		updateAllButtons();
	}

	/**
	 * 对界面vo排序
	 */
	protected void onSort(String[] fields, int[] asc) {
		CircularlyAccessibleValueObject[] vos = null;
		if ((vos = getCurrentVO()) == null)
			return;
		getReportBase().getReportSortUtil().multiSort(vos, fields, asc);
		setBodyDataVO(vos, false);
	}

	private CircularlyAccessibleValueObject[] getCurrentVOFromGroupMap() {
		if (groupKeys.size() == 0)
			return null;
		int selectedRow = 0;
		if (getGroupTable().getSelectedRow() != -1)
			selectedRow = getGroupTable().getSelectedRow();
		String[] keys = getValuesFromGroupTable(selectedRow);
		String key = "";
		for (int i = 0; i < keys.length; i++) {
			key += keys[i];
			if (i != keys.length - 1)
				key += ":";
		}
		return (groupMap.get(key) == null ? null
				: (CircularlyAccessibleValueObject[]) ((ArrayList) groupMap
						.get(key))
						.toArray(new CircularlyAccessibleValueObject[0]));
	}

	/**
	 * 按照模板打印
	 */
	protected void onPrintTemplet() {
		getReportBase().printData();
	}

	/**
	 * 更新所有按钮状态
	 */
	protected void updateAllButtons() {
		boolean hasData = false;
		BillModel bm = m_report.getBillModel();
		if (bm != null
				&& (bm.getDataVector() == null || bm.getDataVector().size() == 0)) {
			hasData = false;
		} else {
			hasData = true;
		}
		setButtons(getAllBtnAry());
		setAllButtonState(hasData);
		updateButtons();

	}

	/**
	 * 返回模板数据VO，目前返回的是备份下来的VO。在模板类提供了相应功能后，则直接调用模板类的相应方法
	 * 
	 * @return 模板modelVO
	 */
	protected ReportModelVO[] getModelVOs() {
		return copyOfReportModelVOs; // getReportBase().getAllBodyVOs();
	}

	/**
	 * 设置所有button状态。依据是IButtonActionAndState接口的isButtonAvailable
	 * 
	 * @param hasData
	 */
	private void setAllButtonState(boolean hasData) {
		Iterator it = button_action_map.keySet().iterator();
		while (it.hasNext()) {
			ButtonObject obj = (ButtonObject) it.next();
			IButtonActionAndState state = (IButtonActionAndState) button_action_map
					.get(obj);
			int result = state.isButtonAvailable();
			if (result == 0) {
				obj.setEnabled(false);
			} else if (result == 1) {
				obj.setEnabled(true);
			} else if (result == IButtonActionAndState.ENABLE_ALWAYS) {
				obj.setEnabled(true);
			} else if (result == IButtonActionAndState.ENABLE_WHEN_HAS_DATA) {
				if (hasData) {
					obj.setEnabled(true);
				} else
					obj.setEnabled(false);
			}

		}
	}

	/**
	 * 条件过滤
	 */
	protected void onFilter(String strFomula) throws Exception {
		getReportBase().filter(strFomula);
	}

	/**
	 * 报表交叉
	 * 
	 * @param rows:
	 *            the cross rows
	 * @param cols:
	 *            the cross cols
	 * @param values:
	 *            the cross values
	 * @throws Exception
	 */
	protected void onCross(String[] rows, String[] cols, String[] values)
			throws Exception {
		getReportBase().drawCrossTable(rows, cols, values);
	}

	/**
	 * 更改显示界面内容，包括标题，表项顺序以及表项显示名称
	 * 
	 * @param title
	 *            欲显示标题
	 * @param fieldNames
	 *            表项key值，将会按照它的顺序显示
	 * @param showNames
	 *            相应key的显示名称，可为空，空即认为不更改显示名称。如果与key的长度不一致，也认为为空
	 * @throws Exception
	 */
	protected void onColumnFilter(String title, String[] fieldNames,
			String[] showNames, boolean isAdjustOrder) throws Exception {
		CircularlyAccessibleValueObject[] vos = getBodyDataVO();
		getReportBase().hideColumn(getAllColumnCodes());

		// 更新模板中标题
		getReportBase().setReportTitle(title);
		// 更新显示标题
		setTitleText(title);
		getReportBase().showHiddenColumn(fieldNames);
		if (isAdjustOrder)
			setColumnOrder(fieldNames);
		if (showNames != null && showNames.length == fieldNames.length)
			setColumnName(fieldNames, showNames);
		setBodyDataVO(vos, true);
	}

	/**
	 * 更改界面模板的显示名称
	 * 
	 * @param fieldNames
	 * @param showNames
	 */
	private void setColumnName(String[] fieldNames, String[] showNames) {
		ReportItem[] items = getReportBase().getBody_Items();
		HashMap tmpHas = new HashMap();
		for (int i = 0; i < items.length; i++) {
			tmpHas.put(items[i].getKey(), items[i]);
		}

		for (int i = 0; i < fieldNames.length; i++) {
			if (tmpHas.containsKey(fieldNames[i])) {
				ReportItem tmpItem = (ReportItem) tmpHas.get(fieldNames[i]);
				if (!tmpItem.getName().equals(showNames[i]))
					tmpItem.setName(showNames[i]);
			}

		}
		getReportBase().setBody_Items(items);
	}

	/**
	 * 直接打印
	 * 
	 * @throws Exception
	 */
	public void onPrintDirect() throws Exception {
		PrintDirectEntry print = PrintManager.getDirectPrinter(getReportBase()
				.getBillTable(), getReportBase().getHead_Items());
		print.setTitle(getTitle());
		print.preview();
	}

	/**
	 * 打印预览
	 * 
	 * @throws Exception
	 */
	public void onPrintPreview() throws Exception {
		getReportBase().previewData();
	}

	protected abstract void onQuery() throws Exception;

	/**
	 * 根据上次的查询条件重新查询
	 */
	protected void onRefresh() throws Exception {
	}

	/**
	 * 
	 * @param context
	 *            小计合计上下文
	 * @throws Exception
	 */
	protected void onSubTotal(SubtotalContext context) throws Exception {
		getReportBase().setSubtotalContext(context);
		getReportBase().subtotal();
	}

	/**
	 * 分组
	 * 
	 * @param context
	 * @throws Exception
	 * @throws Exception
	 */
	protected void onGroup(String[] keys) {
		String[] colNames = getColumnNamesByKeys(convertVOKeysToModelKeys(keys));
		onGroup(keys, colNames);
	}

	/**
	 * 分组
	 * 
	 * @param keys
	 * @param names
	 */
	protected void onGroup(String[] keys, String[] names) {
		String[] convertGroupKeys = convertVOKeysToModelKeys((String[]) groupKeys
				.toArray(new String[0]));
		// 显示上次隐藏的列
		getReportBase().showHiddenColumn(convertGroupKeys);
		// 清理上次添加的表头
		removeHeadItems(convertGroupKeys);
		// 清理分组记录
		groupKeys.clear();
		// 认为取消分组
		if (keys == null || names == null || keys.length == 0
				|| names.length == 0 || keys.length != names.length) {
			setVeriSplitEnabled(false);
			// 设置回原数据
			setBodyDataVO(allBodyDataVO, false);
			return;
		}

		groupKeys.addAll(Arrays.asList(keys));
		// 启动分隔栏
		setVeriSplitEnabled(true);
		// 从界面获取VO
		if (allBodyDataVO == null || allBodyDataVO.length == 0)
			allBodyDataVO = getVOFromUI();
		// 清空分组hash
		groupMap.clear();
		// 安分组key hash，key的形式是 a:b:c::空值认为是" ",不影响使用
		for (int i = 0; i < allBodyDataVO.length; i++) {
			StringBuffer key = new StringBuffer();
			for (int j = 0; j < keys.length; j++) {
				key
						.append(allBodyDataVO[i].getAttributeValue(keys[j]) == null ? " "
								: allBodyDataVO[i].getAttributeValue(keys[j])
										.toString());
				if (j != keys.length - 1)
					key.append(":");
			}
			addVoToHashmap(key.toString(), allBodyDataVO[i]);
		}
		String[] convertedKeys = convertVOKeysToModelKeys(keys);
		extractItemsToHead(convertedKeys, names);
		getReportBase().hideColumn(convertedKeys);
		// 构造表model
		GroupTableModel model = new GroupTableModel();
		model.addColumns(names);
		model.addRows(groupMap.keySet());
		getGroupTable().setModel(model);
		// 显示第一行数据
		ArrayList tmpVO = (ArrayList) groupMap.get(groupMap.keySet().iterator()
				.next());
		if (tmpVO != null && tmpVO.size() > 0) {
			setBodyDataVO((CircularlyAccessibleValueObject[]) tmpVO
					.toArray(new CircularlyAccessibleValueObject[0]), false);
			setHeadItems(convertedKeys, getValuesFromGroupTable(0));
		}
	}

	/**
	 * @param strings
	 */
	private void removeHeadItems(String[] strs) {
		ReportItem[] headItems = getReportBase().getHead_Items();
		ArrayList list = new ArrayList();
		if (headItems != null && headItems.length > 0) {
			for (int i = 0; i < headItems.length; i++) {
				if (contains(strs, headItems[i].getKey()))
					continue;
				list.add(headItems[i]);
			}
		}
		getReportBase().setHead_Items(
				(ReportItem[]) list.toArray(new ReportItem[0]));
	}

	/**
	 * 从分组table中获取指定行数据
	 * 
	 * @param row
	 * @return
	 */
	private String[] getValuesFromGroupTable(int row) {
		UITable table = getGroupTable();
		int count = table.getColumnCount();
		String[] str = new String[count];
		for (int i = 0; i < str.length; i++) {
			str[i] = (String) table.getModel().getValueAt(row, i);
		}
		return str;
	}

	/**
	 * 将keys所对应的item提取到表头
	 * 
	 * @param keys
	 *            主键
	 * @param colNames
	 *            列名称
	 */
	private void extractItemsToHead(String[] keys, String[] colNames) {
		String[] convertKeys = convertVOKeysToModelKeys(keys);
		ReportItem[] items = new ReportItem[keys.length];
		for (int i = 0; i < convertKeys.length; i++) {
			items[i] = new ReportItem();
			items[i].setKey(convertKeys[i]);
			items[i].setShow(true);
			items[i].setName(colNames[i]);
		}
		getReportBase().addHeadItem(items);
	}

	/**
	 * @param keys
	 * @return
	 */
	public String[] convertVOKeysToModelKeys(String[] keys) {
		if (keys == null || keys.length == 0)
			return null;
		String[] convertedKeys = new String[keys.length];
		for (int i = 0; i < keys.length; i++) {
			convertedKeys[i] = convertVOFieldNameToReportModelFieldName(keys[i]);
		}
		return convertedKeys;
	}

	/**
	 * 设置表头数据
	 * 
	 * @param key
	 * @param value
	 */
	private void setHeadItems(String[] keys, Object[] values) {
		if (keys == null || values == null || keys.length == 0
				|| values.length == 0)
			return;
		if (keys.length != values.length) {
			System.out.println("键和值的数目不匹配");
			return;
		}
		for (int i = 0; i < keys.length; i++) {
			getReportBase().setHeadItem(keys[i], values[i]);
		}
	}

	/**
	 * 通过keys获得col显示名
	 * 
	 * @param keys
	 *            必须是ModelKey，即以'_'分隔的key
	 * @return
	 */
	protected String[] getColumnNamesByKeys(String[] keys) {
		if (keys == null || keys.length == 0)
			return null;
		ReportModelVO[] fields = getModelVOs();
		// 不使用固定数组，防止数组元素为NULL，妨碍后面的判断
		ArrayList list = new ArrayList();
		if (fields != null && fields.length != 0) {
			for (int i = 0; i < keys.length; i++) {
				for (int j = 0; j < fields.length; j++) {
					if (fields[j].getColumnCode().equals(keys[i]))
						list.add(fields[j].getColumnUser());
				}
			}
		}
		return (String[]) list.toArray(new String[0]);
	}

	/**
	 * 通过key获得Col显示名
	 * 
	 * @param key
	 *            必须是ModelKey，即以'_'分隔的key
	 */
	protected String getColumnNameByKey(String key) {
		if (key == null)
			return null;
		ReportModelVO[] fields = getModelVOs();
		if (fields != null && fields.length != 0) {
			for (int j = 0; j < fields.length; j++) {
				if (fields[j].getColumnCode().equals(key))
					return fields[j].getColumnUser();
			}
		}
		return null;
	}

	/**
	 * 将VO添加到HashMap中
	 * 
	 * @param key
	 * @param vo
	 */
	private void addVoToHashmap(String key, CircularlyAccessibleValueObject vo) {
		ArrayList list = null;
		if ((list = (ArrayList) groupMap.get(key)) == null) {
			list = new ArrayList();
			list.add(vo);
			groupMap.put(key, list);
		} else
			list.add(vo);
	}

	// protected void onCode
	/**
	 * 此方法是在查询出数据后调用。可以重载此方法，以对这些VO进行额外设置
	 * 
	 * @return nc.vo.pub.CircularlyAccessibleValueObject[]
	 * @param vos
	 *            nc.vo.pub.CircularlyAccessibleValueObject[]
	 */
	protected CircularlyAccessibleValueObject[] processVOs(
			CircularlyAccessibleValueObject[] vos) {
		return vos;
	}

	/**
	 * 这个方法设置报表的列的顺序
	 * 
	 */
	protected void setColumnOrder(String[] column_codes) {
		ReportItem[] items = getReportBase().getBody_Items();

		ArrayList al = new ArrayList();
		HashMap tmpHas = new HashMap();
		for (int i = 0; i < items.length; i++) {
			tmpHas.put(items[i].getKey(), items[i]);
		}

		for (int i = 0; i < column_codes.length; i++) {
			if (tmpHas.containsKey(column_codes[i])) {
				al.add(tmpHas.get(column_codes[i]));
				tmpHas.remove(column_codes[i]);
			}

		}
		al.addAll(tmpHas.values());

		ReportItem[] newitems = (ReportItem[]) al.toArray(new ReportItem[0]);
		getReportBase().setBody_Items(newitems);

	}

	/**
	 * 设置数字类型格式
	 */
	protected void setDigitFormat() {
		ReportItem[] items = getReportBase().getBody_Items();

		for (int i = 0; i < items.length; i++) {
			if (items[i].getDataType() == 2 || items[i].getDataType() == 1) {
				items[i].setDecimalDigits(2);
			}

		}

	}

	/**
	 * 如需自定义按钮,重载此方法，此方法在初始化时调用。
	 */
	protected void setPrivateButtons() {
	}

	/**
	 * 除掉已注册按钮
	 */
	protected void unRegisterButton(ButtonObject obj) {
		if (obj != null) {
			button_action_map.remove(obj);
		}

	}

	/**
	 * 注册按钮，-1表示放在最后
	 */
	protected void registerButton(ButtonObject obj, IButtonActionAndState action) {
		registerButton(obj, action, -1);
	}

	/**
	 * 
	 * 注册按钮,按照指定位置加入按钮，位置从零开始计数
	 */
	protected void registerButton(ButtonObject obj,
			IButtonActionAndState action, int pos) {
		if (obj == null || action == null) {
			System.out.println("按钮或动作为空,不能加入");
			return;
		}
		if (button_action_map.get(obj) != null) {
			System.out.println("此按钮已经添加");
			return;
		}
		button_action_map.put(obj, action, pos);
	}

	/**
	 * 将传入的字符串数组显示于查询显示面板上
	 * 
	 * @param conditions
	 *            待显示字符串数组
	 */
	public void showCondition(String[] conditions) {

		getConditionPanel().removeAll();
		if (conditions == null || conditions.length == 0)
			return;

		String[] temp = conditions;
		nc.ui.pub.beans.UILabel tmp = new nc.ui.pub.beans.UILabel(temp[0]);
		java.awt.FontMetrics metrics = tmp.getFontMetrics(tmp.getFont());
		int[] widths = new int[conditions.length];
		for (int i = 0; i < conditions.length; i++) {
			widths[i] = metrics.stringWidth(conditions[i]);

		}
		Arrays.sort(widths);

		int width = widths[widths.length - 1];
		int heigth = metrics.getHeight();
		for (int i = 0; i < conditions.length; i++) {
			nc.ui.pub.beans.UILabel l = new nc.ui.pub.beans.UILabel(
					conditions[i]);
			getConditionPanel().add(l);
			l.setPreferredSize(new java.awt.Dimension(width, heigth));

		}
		getConditionPanel().invalidate();
		getConditionPanel().repaint();

	}

	/**
	 * 
	 * @return 返回 bodyDataVO。
	 */
	public CircularlyAccessibleValueObject[] getBodyDataVO() {
		return getReportBase().getBodyDataVO();
	}

	/**
	 * @param bodyDataVO
	 *            要设置的 bodyDataVO。
	 * @param isLoadFormula
	 *            是否执行加载公式
	 */
	protected void setBodyDataVO(CircularlyAccessibleValueObject[] dataVO,
			boolean isLoadFormula) {
		getReportBase().setBodyDataVO(dataVO, isLoadFormula);
		if (needGroup) {
			needGroup = false;
			onGroup(getUIControl().getGroupKeys());
		}
		updateAllButtons();
	}

	/**
	 * 
	 * @return nc.vo.tm.report.TableField[]
	 * @param type
	 *            java.lang.Integer
	 */
	protected TableField[] getVisibleFieldsByDataType(Integer type) {
		TableField[] visibleFields = getVisibleFields();
		ArrayList al = new ArrayList();
		nc.vo.pub.report.ReportModelVO[] vos = getModelVOs();// getReportBase().getAllBodyVOs();
		for (int i = 0; i < vos.length; i++) {
			if (vos[i].getDataType().equals(type)) {
				TableField f = createTableFieldFromReportModelVO(vos[i]);
				if (Arrays.asList(visibleFields).contains(f)) {
					al.add(f);
				}
			}
		}
		return (TableField[]) al.toArray(new TableField[0]);
	}

	/**
	 * @return 返回 groupKeys。
	 */
	protected ArrayList getGroupKeys() {
		return groupKeys;
	}

	public boolean contains(String[] source, String element) {
		if (source != null) {
			for (int i = 0; i < source.length; i++) {
				if (source[i].equals(element))
					return true;
			}
		}
		return false;
	}

	/**
	 * @return 返回 veriSplitPane。
	 */
	private UISplitPane getVeriSplitPane() {
		if (veriSplitPane == null) {

			veriSplitPane = new UISplitPane(UISplitPane.HORIZONTAL_SPLIT,
					false, null, getReportBase());

			setVeriSplitEnabled(false);
		}
		return veriSplitPane;
	}

	private UIScrollPane getGroupPanel() {
		if (groupSPane == null) {
			groupSPane = new UIScrollPane();
			groupSPane.setViewportView(getGroupTable());
		}
		return groupSPane;
	}
	
	//wanglei 2014-08-24
	  public QueryConditionDLG getNewQryDlg()
	  {
	    if (this.m_newqryDlg == null) {
	      this.m_newqryDlg = createNewQueryDLG();
	    }
	    return this.m_newqryDlg;
	  }
	  
	  protected QueryConditionDLG createNewQueryDLG() {
		    QueryConditionDLG dlg = new QueryConditionDLG(getParent());

		    TemplateInfo tempinfo = new TemplateInfo();
		    tempinfo.setPk_Org(ClientEnvironment.getInstance().getCorporation().getPrimaryKey());
		    tempinfo.setCurrentCorpPk(ClientEnvironment.getInstance().getCorporation().getPrimaryKey());
		    tempinfo.setFunNode(getModuleCode());
		    tempinfo.setUserid(ClientEnvironment.getInstance().getUser().getPrimaryKey());
		    dlg = new QueryConditionDLG(this, null, tempinfo);

		    return dlg;
		  }
}