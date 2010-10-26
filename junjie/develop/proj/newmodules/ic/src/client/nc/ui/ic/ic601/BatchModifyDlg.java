package nc.ui.ic.ic601;

/**
 * 阮睿对话框 
 */

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.*;
import java.util.HashMap;
import javax.swing.JPanel;
import nc.ui.ml.NCLangRes;
import nc.ui.pub.beans.calendar.CalendarPanel;
import nc.ui.pub.beans.calendar.UICalendar;
import nc.ui.pub.beans.*;
import nc.vo.bd.b15.BatchUpdateCtlVO;
import nc.vo.pub.lang.*;

import nc.ui.pub.beans.UIDialog;
import java.awt.Dimension;

public class BatchModifyDlg extends UIDialog implements ActionListener {
	private UIButton batchBtnCancel;

	private UIButton batchBtnOK;

	private UIComboBox batchCbbFields;

	private JPanel batchUIDialogContentPane;

	private UILabel batchUILabel1;

	private UILabel batchUILabel2;

	private UILabel batchUILabel3;

	public static final int TEXTFIELD = 0;

	public static final int CHECKBOX = 1;

	public static final int COMBOX = 2;

	public static final int REFPANE = 3;

	public static final int CHAR = 11;

	public static final int INTEGER = 12;

	public static final int DOUBLE = 13;

	private BatchUpdateCtlVO m_ctlvos[];

	private HashMap m_hm;

	protected Component m_component;

	private BatchUpdateCtlVO m_vo;

	private JPanel batchcenter;

	private JPanel batchsouth;

	private UIPanel m_UIPnlNorth;

	private UIPanel getUIPnlNorth() {
		if (m_UIPnlNorth == null) {
			m_UIPnlNorth = new UIPanel();
			m_UIPnlNorth.setName("north");
			m_UIPnlNorth.setPreferredSize(new Dimension(300, 50));
			m_UIPnlNorth.setLayout(getcenterUILabelLayout());
		}
		return m_UIPnlNorth;
	}

	public BatchModifyDlg() {
		batchBtnCancel = null;
		batchBtnOK = null;
		batchCbbFields = null;
		batchUIDialogContentPane = null;
		batchUILabel1 = null;
		batchUILabel2 = null;
		batchUILabel3 = null;
		m_ctlvos = null;
		m_hm = null;
		m_component = null;
		m_vo = null;
		batchcenter = null;
		batchsouth = null;
		m_UIPnlNorth = null;
		initialize();
	}

	public BatchModifyDlg(Container arg1) {
		super(arg1);
		batchBtnCancel = null;
		batchBtnOK = null;
		batchCbbFields = null;
		batchUIDialogContentPane = null;
		batchUILabel1 = null;
		batchUILabel2 = null;
		batchUILabel3 = null;
		m_ctlvos = null;
		m_hm = null;
		m_component = null;
		m_vo = null;
		batchcenter = null;
		batchsouth = null;
		m_UIPnlNorth = null;
		initialize();
	}

	public BatchModifyDlg(Container arg1, String arg2) {
		super(arg1, arg2);
		batchBtnCancel = null;
		batchBtnOK = null;
		batchCbbFields = null;
		batchUIDialogContentPane = null;
		batchUILabel1 = null;
		batchUILabel2 = null;
		batchUILabel3 = null;
		m_ctlvos = null;
		m_hm = null;
		m_component = null;
		m_vo = null;
		batchcenter = null;
		batchsouth = null;
		m_UIPnlNorth = null;
		initialize();
	}

	public BatchModifyDlg(Frame arg1) {
		super(arg1);
		batchBtnCancel = null;
		batchBtnOK = null;
		batchCbbFields = null;
		batchUIDialogContentPane = null;
		batchUILabel1 = null;
		batchUILabel2 = null;
		batchUILabel3 = null;
		m_ctlvos = null;
		m_hm = null;
		m_component = null;
		m_vo = null;
		batchcenter = null;
		batchsouth = null;
		m_UIPnlNorth = null;
	}

	public BatchModifyDlg(Frame arg1, String arg2) {
		super(arg1, arg2);
		batchBtnCancel = null;
		batchBtnOK = null;
		batchCbbFields = null;
		batchUIDialogContentPane = null;
		batchUILabel1 = null;
		batchUILabel2 = null;
		batchUILabel3 = null;
		m_ctlvos = null;
		m_hm = null;
		m_component = null;
		m_vo = null;
		batchcenter = null;
		batchsouth = null;
		m_UIPnlNorth = null;
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == getCbbFields()) {
			String key = ((BatchUpdateCtlVO) getCbbFields().getSelectedItem())
					.getFieldName();
			m_vo = (BatchUpdateCtlVO) m_hm.get(key);
			initUI(m_vo);
		} else if (e.getSource() == getBtnOK()) {
			int rs = MessageDialog.showYesNoDlg(this, "警告",
					"您确定要批量修改选中的数据吗？修改后数据不可以还原");
			if (rs != 4)
				return;
			switch (m_vo.getComponentType()) {
			default:
				break;
			case 0: // '\0'
				String txtValue = getTextFieldValue();
				if (m_vo.getDataType() == 13) {
					UFDouble value = new UFDouble(txtValue);
					m_vo.setValue(value);
					break;
				} else if (m_vo.getDataType() == 12) {
					Integer value = new Integer(txtValue);
					m_vo.setValue(value);
				} else {
					m_vo.setValue(txtValue);
				}
				break;

			case 1: // '\001'
				UICheckBox chb = (UICheckBox) m_component;
				m_vo.setValue(new UFBoolean(chb.isSelected()));
				break;

			case 2: // '\002'
				UIComboBox cb = (UIComboBox) m_component;
				if (m_vo.getFieldVOName().equals("columnName3"))
					m_vo.setValue(cb.getSelectedItem().toString());
				else
					m_vo.setValue(new Integer(cb.getSelectedIndex()));
				break;

			case 3: // '\003'
				UIRefPane ref = (UIRefPane) m_component;
				if (m_vo.getFieldName().equals("columnName4")
						|| m_vo.getFieldName().equals("columnNameXX"))
					m_vo.setValue(new UFDate(ref.getRefPK()));
				else
					m_vo.setValue(ref.getRefPK());
				break;
			}
			closeOK();
		} else if (e.getSource() == getBtnCancel())
			closeCancel();
	}

	protected String getTextFieldValue() {
		UITextField tf = (UITextField) m_component;
		String txtValue = tf.getText();
		return txtValue;
	}

	private UIButton getBtnCancel() {
		if (batchBtnCancel == null)
			try {
				batchBtnCancel = new UIButton();
				batchBtnCancel.setName("BtnCancel");
				batchBtnCancel.setText("取消");
				batchBtnCancel.setBounds(231, 26, 71, 22);
			} catch (Throwable ivjExc) {
				handleException(ivjExc);
			}
		return batchBtnCancel;
	}

	private UIButton getBtnOK() {
		if (batchBtnOK == null)
			try {
				batchBtnOK = new UIButton();
				batchBtnOK.setName("BtnOK");
				batchBtnOK.setText("确定");
				batchBtnOK.setBounds(119, 28, 70, 22);
			} catch (Throwable ivjExc) {
				handleException(ivjExc);
			}
		return batchBtnOK;
	}

	private UIComboBox getCbbFields() {
		if (batchCbbFields == null)
			try {
				batchCbbFields = new UIComboBox();
				batchCbbFields.setName("CbbFields");
				batchCbbFields.setPreferredSize(new Dimension(160, 22));
			} catch (Throwable ivjExc) {
				handleException(ivjExc);
			}
		return batchCbbFields;
	}

	private JPanel getcenter() {
		if (batchcenter == null)
			try {
				batchcenter = new JPanel();
				batchcenter.setName("center");
				batchcenter.setLayout(getcenterUILabelLayout());
				getcenter().add(getUILabel2(), getUILabel2().getName());
				getcenter().add(getCbbFields(), getCbbFields().getName());
				getcenter().add(getUILabel3(), getUILabel3().getName());
			} catch (Throwable ivjExc) {
				handleException(ivjExc);
			}
		return batchcenter;
	}

	private UILabelLayout getcenterUILabelLayout() {
		UILabelLayout batchcenterUILabelLayout = null;
		try {
			batchcenterUILabelLayout = new UILabelLayout();
			batchcenterUILabelLayout.setLabelAndTextFieldGap(5);
			batchcenterUILabelLayout.setColumns(1);
		} catch (Throwable ivjExc) {
			handleException(ivjExc);
		}
		return batchcenterUILabelLayout;
	}

	public BatchUpdateCtlVO getResultVO() {
		return m_vo;
	}

	private JPanel getsouth() {
		if (batchsouth == null)
			try {
				batchsouth = new JPanel();
				batchsouth.setName("south");
				batchsouth.setPreferredSize(new Dimension(0, 60));
				batchsouth.setLayout(null);
				getsouth().add(getBtnOK(), getBtnOK().getName());
				getsouth().add(getBtnCancel(), getBtnCancel().getName());
			} catch (Throwable ivjExc) {
				handleException(ivjExc);
			}
		return batchsouth;
	}

	private JPanel getUIDialogContentPane() {
		if (batchUIDialogContentPane == null)
			try {
				batchUIDialogContentPane = new JPanel();
				batchUIDialogContentPane.setName("UIDialogContentPane");
				batchUIDialogContentPane.setLayout(new BorderLayout());
				getUIDialogContentPane().add(getUIPnlNorth(), "North");
				getUIDialogContentPane().add(getsouth(), "South");
				getUIDialogContentPane().add(getcenter(), "Center");
			} catch (Throwable ivjExc) {
				handleException(ivjExc);
			}
		return batchUIDialogContentPane;
	}

	private UILabel getUILabel2() {
		if (batchUILabel2 == null)
			try {
				batchUILabel2 = new UILabel();
				batchUILabel2.setName("UILabel2");
				batchUILabel2.setPreferredSize(new Dimension(100, 22));
				batchUILabel2.setText("批改字段");
				batchUILabel2.setPreferredSize(new Dimension(batchUILabel2
						.getText().getBytes().length * 6, 22));
			} catch (Throwable ivjExc) {
				handleException(ivjExc);
			}
		return batchUILabel2;
	}

	private UILabel getUILabel3() {
		if (batchUILabel3 == null)
			try {
				batchUILabel3 = new UILabel();
				batchUILabel3.setName("UILabel3");
				batchUILabel3.setText("批改的值");
				batchUILabel3.setPreferredSize(new Dimension(batchUILabel3
						.getText().getBytes().length * 6, 22));
			} catch (Throwable ivjExc) {
				handleException(ivjExc);
			}
		return batchUILabel3;
	}

	private void handleException(Throwable throwable) {
	}

	private void initconnection() {
		getCbbFields().addActionListener(this);
		getBtnOK().addActionListener(this);
		getBtnCancel().addActionListener(this);
	}

	private void initdata() {
		String names[] = getNames();
		String mlang_names[] = getMlang_names();
		String voFieldNames[] = getVoFieldNames();
		int componentType[] = getComponentType();
		int dataType[] = getDataTypes();
		m_ctlvos = new BatchUpdateCtlVO[names.length];
		m_hm = new HashMap();
		for (int i = 0; i < m_ctlvos.length; i++) {
			m_ctlvos[i] = new BatchUpdateCtlVO();
			m_ctlvos[i].setFieldName(names[i]);
			m_ctlvos[i].setShowName(mlang_names[i]);
			m_ctlvos[i].setFieldVOName(voFieldNames[i]);
			m_ctlvos[i].setComponentType(componentType[i]);
			m_ctlvos[i].setDataType(dataType[i]);
			m_hm.put(m_ctlvos[i].getFieldName(), m_ctlvos[i]);
			getCbbFields().addItem(m_ctlvos[i]);
		}

	}

	// 11,文本型；12，整型；13，数值型，14，日期格式的文本
	protected int[] getDataTypes() {
		// int dataType[] = { 11, 11, 11 };
		int dataType[] = { 11 };
		return dataType;
	}

	// 0，文本框控件；1，选择框控件；2，下拉框控件；3，参照控件
	protected int[] getComponentType() {
		// int componentType[] = { 3, 3, 0 };
		int componentType[] = { 0 };
		return componentType;
	}

	protected String[] getVoFieldNames() {
		// String voFieldNames[] = { "dconsigndate", "ddeliverdate",
		// "frownote"};
		String voFieldNames[] = { "zbzsh" };
		return voFieldNames;
	}

	protected String[] getMlang_names() {
		// String mlang_names[] = { "计划日期", "发货日期", "备注" };
		String mlang_names[] = { "质量保证书号" };
		return mlang_names;
	}

	protected String[] getNames() {
		// String names[] = { "计划日期", "发货日期", "备注" };
		String names[] = { "质量保证书号" };
		return names;
	}

	private void initialize() {
		try {
			setName("BatchUpdateDlg");
			setDefaultCloseOperation(2);
			setSize(430, 300);
			setTitle("数据批改");
			setContentPane(getUIDialogContentPane());
		} catch (Throwable ivjExc) {
			handleException(ivjExc);
		}
		initconnection();
		initdata();
	}

	private void initUI(BatchUpdateCtlVO vo) {
		int size = 30;
		if (vo == null)
			return;
		if (m_component != null)
			getcenter().remove(m_component);
		switch (vo.getComponentType()) {

		case 0: // '\0'
			if (vo.getDataType() == 11) {
				setTextComponent(vo);
				break;
			}
			if (vo.getDataType() == 13) {
				setDoubleComponent(vo);
				break;
			}
			if (vo.getDataType() == 14) {
				setDateComponent(vo);
				break;
			}
			if (vo.getDataType() == 12)
				setIntegerComponent(vo);
			break;

		case 1: // '\001'
			UICheckBox chb = new UICheckBox(vo.getShowName());
			chb.setPreferredSize(new Dimension(160, 22));
			m_component = chb;
			break;

		case 2: // '\002'
			UIComboBox cb = new UIComboBox();
			cb.setSize(size, 22);
			if (vo.getFieldName().equals("下拉框字段")) {
				cb.addItem("value1");
				cb.addItem("value2");
				cb.addItem("value3");
				cb.addItem("value4");
			} else if (vo.getFieldName().equals("columnXX")) {
				cb.addItem("aaa");
				cb.addItem("bbb");
				cb.addItem("ccc");
			} else {
				cb.addItem("A");
				cb.addItem("B");
				cb.addItem("C");
			}
			m_component = cb;
			break;

		case 3: // '\003'
			UIRefPane ref = initRefPanel(vo);
			m_component = ref;
			break;
		default:

			break;
		}
		getcenter().add(m_component);
		getcenter().revalidate();
		getUIDialogContentPane().repaint();
		getUIDialogContentPane().revalidate();
	}

	protected void setIntegerComponent(BatchUpdateCtlVO vo) {
		UITextField tf = new UITextField(20);
		tf.setTextType("TextInt");
		m_component = tf;
	}

	protected void setTextComponent(BatchUpdateCtlVO vo) {
		UITextField tf = new UITextField(20);
		m_component = tf;
	}

	protected void setDoubleComponent(BatchUpdateCtlVO vo) {
		UITextField tf = new UITextField(20);
		tf.setTextType("TextDbl");
		m_component = tf;
	}

	protected void setDateComponent(BatchUpdateCtlVO vo) {
		UITextField tf = new UITextField(20);
		tf.setTextType("TextDate");
		m_component = tf;
	}

	protected UIRefPane initRefPanel(BatchUpdateCtlVO vo) {
		UIRefPane ref = new UIRefPane();
		if (vo.getFieldName().equals("参照字段"))
			ref.setRefNodeName("客户档案");
		else if (vo.getFieldName().equals("参照字段2"))
			ref.setRefNodeName("nc.ui.scm.ref.prm.CustAddrRefModel");
		else if (vo.getFieldName().equals("发货日期")
				|| vo.getFieldName().equals("计划日期")) {
			// ref.setBounds(new Rectangle(90, 184, 150, 20));
			ref.setRefNodeName("日历");
		}

		return ref;
	}

} // @jve:decl-index=0:visual-constraint="10,10"
