package nc.ui.ic.jjpanel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.itf.uap.IVOPersistence;
import nc.jdbc.framework.processor.ColumnProcessor;
//import nc.ui.am.inventory.command.ShowEqualCommand;
import nc.ui.bd.ref.busi.InvmandocDefaultRefModel;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UIDialogListener;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.bill.BillData;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillEditListener;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillListData;
import nc.ui.pub.bill.BillListPanel;
import nc.ui.pub.bill.BillModel;
import nc.ui.pub.bill.BillScrollPane;
import nc.ui.pub.hotkey.HotkeyUtil;
import nc.ui.scm.pub.ScmPubHelper;
import nc.vo.am.util.importtools.VOCheck;
import nc.vo.ml.IProductCode;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.NullFieldException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDouble;
import nc.vo.ic.jjvo.InformationCostVO;
import nc.vo.scm.pub.SCMEnv;
import nc.vo.trade.pub.HYBillVO;

/**
 * @function 费用信息的录入面板
 * 
 * @author QuSida
 * 
 * @date 2010-8-20 上午10:00:59
 * 
 */
public class InfoCostPanel extends UIDialog implements ActionListener,BillEditListener{

	//上级卡片  by 付世超 2010-10-13
	private BillCardPanel parentCardPanel = null;
	
	private BillListPanel billListPanel = null;
	// 确定按钮
	private nc.ui.pub.beans.UIButton m_btnOK = null;
	// 取消按钮
	private nc.ui.pub.beans.UIButton m_btnCancel = null;
	// 增加按钮
	private nc.ui.pub.beans.UIButton m_btnAdd = null;
	// 删除按钮
	private nc.ui.pub.beans.UIButton m_btnDel = null;
	// 修改按钮
	private nc.ui.pub.beans.UIButton m_btnMod = null;
	// 公司ID
	private String m_sLoginCorp = null;
	// 操作员ID
	private String m_sOperator = null;
	// 按钮面板
	private nc.ui.pub.beans.UIPanel m_panelSouth = null;
	// UIDialog的面板
	private javax.swing.JPanel m_dialogContentPane = null;
	// 填充面板
	private nc.ui.pub.beans.UIPanel m_pnlFillSpaceWest = null;
	private nc.ui.pub.beans.UIPanel m_pnlFillSpaceEast = null;
	private boolean m_closeMark = false;
	// 费用信息vo数组
	private InformationCostVO[] icvos = null;
	// 存货总数量  by 付世超 2010-10-13
	private UFDouble arrnumber = null;
	// 当前单据类型 by 付世超 2010-10-30
	private String billtype = null;
	
	/**
	 * @function 传入父容器,公司,操作员的构造函数
	 * 
	 * @author QuSida
	 * 
	 * @date 2010-8-20 上午10:02:36
	 * 
	 */
	public InfoCostPanel(java.awt.Container parent, String sPk_Corp,
			String sOperatorID) {
		super(parent);
		setArrnumber(parent);//add by 付世超
		setBillType(parent);//add by 付世超 2010-10-30
		m_sLoginCorp = sPk_Corp;
		initDialog(); // 初始化对话框
	}

	/**
	 * @function 重写父类传入父容器的构造函数
	 * 
	 * @author QuSida
	 * 
	 * @date 2010-8-20 上午10:12:28
	 * 
	 */
	public InfoCostPanel(java.awt.Container parent,InformationCostVO[] vos) {
		super(parent);
		setArrnumber(parent);//add by 付世超
		setBillType(parent);//add by 付世超 2010-10-30
		initDialog();
		this.getBillListPanel().setHeaderValueVO(vos);
		this.getBillListPanel().getHeadBillModel().updateValue();  //wanglei 2013-05-13 刷新数据状态
		this.getBillListPanel().getHeadBillModel().execLoadFormula();
//		this.getBillListPanel().getHeadItem("noriginalcurmny").setEdit(false);
	
	
	}
	public InfoCostPanel(java.awt.Container parent) {
		super(parent);
		setArrnumber(parent);//add by 付世超
		setBillType(parent);//add by 付世超 2010-10-30
		initDialog();
		}

	/**
	 * @function 初始化对话框
	 * 
	 * @author QuSida
	 * 
	 * @return void
	 * 
	 * @date 2010-8-20 上午10:13:42
	 */
	private void initDialog() {

		setName("InfoCostDialog");
		setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		setTitle("费用录入");
		setBounds(37, 563, 1000, 400);
		setResizable(false);
		// 给定显示内容
		setContentPane(getUIDialogContentPane());
		// 修改参照,只显示应税劳务标志为'Y'的存货
		UIRefPane refPane = (UIRefPane) getBillListPanel().getHeadItem("costcode").getComponent();
		nc.ui.bd.ref.busi.InvmandocDefaultRefModel refModel = (InvmandocDefaultRefModel) refPane.getRefModel();
		refModel.setWherePart(" bd_invbasdoc.laborflag = 'Y' and bd_invmandoc.pk_corp = '"+ClientEnvironment.getInstance().getCorporation().getPk_corp()+"'");


	}

	/**
	 * @function 得到显示内容面板
	 * 
	 * @author QuSida
	 * 
	 * @return javax.swing.JPanel
	 * 
	 * @date 2010-8-20 上午10:14:10
	 */
	private javax.swing.JPanel getUIDialogContentPane() {
		if (m_dialogContentPane == null) {
			m_dialogContentPane = new javax.swing.JPanel();
			m_dialogContentPane.setName("UIDialogContentPane");
			m_dialogContentPane.setLayout(new java.awt.BorderLayout());
			// 按钮panel
			m_dialogContentPane.add(getPnlSouth(), "South");
			// 公司panel
			m_dialogContentPane.add(getBillListPanel(), "Center");
			// 边界填充panel
			m_dialogContentPane.add(getPnlFillSpaceWest(), "West");
			m_dialogContentPane.add(getPnlFillSpaceEast(), "East");
		}
		return m_dialogContentPane;
	}

	/**
	 * @function 获得按钮面板
	 * 
	 * @author QuSida
	 * 
	 * @return nc.ui.pub.beans.UIPanel
	 * 
	 * @date 2010-8-20 上午10:15:33
	 */
	private nc.ui.pub.beans.UIPanel getPnlSouth() {
		if (m_panelSouth == null) {
			m_panelSouth = new nc.ui.pub.beans.UIPanel();
			m_panelSouth.setName("PnlSouth");
			// 增加
			m_panelSouth.add(getBtnAdd(), getBtnAdd().getName());
			// 删除
			m_panelSouth.add(getBtnDel(), getBtnDel().getName());
			// 确定
			m_panelSouth.add(getBtnOK(), getBtnOK().getName());
			// 取消
			m_panelSouth.add(getBtnCancel(), getBtnCancel().getName());
			//修改
			m_panelSouth.add(getBtnMod(),getBtnMod().getName());
		}
		return m_panelSouth;
	}

	/**
	 * @function 得到确定按钮
	 * 
	 * @author QuSida
	 * 
	 * @return nc.ui.pub.beans.UIButton
	 * 
	 * @date 2010-8-20 上午10:19:17
	 */
	private nc.ui.pub.beans.UIButton getBtnOK() {
		if (m_btnOK == null) {
			m_btnOK = new nc.ui.pub.beans.UIButton();
			m_btnOK.setName("BtnOK");
			HotkeyUtil.setHotkeyAndText(m_btnOK, 'O', nc.ui.ml.NCLangRes
					.getInstance().getStrByID(IProductCode.PRODUCTCODE_COMMON,
							"UC001-0000044"));/* @res "确定" */
			m_btnOK.addActionListener(this);
		}
		return m_btnOK;
	}

	/**
	 * @function 得到删除按钮
	 * 
	 * @author QuSida
	 * 
	 * @return nc.ui.pub.beans.UIButton
	 * 
	 * @date 2010-8-20 上午10:19:39
	 */
	private nc.ui.pub.beans.UIButton getBtnDel() {
		if (m_btnDel == null) {

			m_btnDel = new nc.ui.pub.beans.UIButton("删除");
			m_btnDel.setName("BtnDel");

			m_btnDel.addActionListener(this);
		}
		return m_btnDel;
	}

	/**
	 * @function 得到增加按钮
	 * 
	 * @author QuSida
	 * 
	 * @return nc.ui.pub.beans.UIButton
	 * 
	 * @date 2010-8-20 上午10:20:49
	 */
	private nc.ui.pub.beans.UIButton getBtnAdd() {
		if (m_btnAdd == null) {

			m_btnAdd = new nc.ui.pub.beans.UIButton("增加");
			m_btnAdd.setName("BtnAdd");

			m_btnAdd.addActionListener(this);
		}
		return m_btnAdd;
	}
	/**
	 * @function 得到修改按钮
	 *
	 * @author QuSida
	 *
	 * @return nc.ui.pub.beans.UIButton
	 *
	 * @date 2010-9-25 上午09:49:41
	 */
	private nc.ui.pub.beans.UIButton getBtnMod() {
		if (m_btnMod == null) {

			m_btnMod = new nc.ui.pub.beans.UIButton("修改");
			m_btnMod.setName("BtnMod");

			m_btnMod.addActionListener(this);
		}
		return m_btnMod;
	}

	/**
	 * @function 得到取消按钮
	 * 
	 * @author QuSida
	 * 
	 * @return nc.ui.pub.beans.UIButton
	 * 
	 * @date 2010-8-20 上午10:22:01
	 */
	private nc.ui.pub.beans.UIButton getBtnCancel() {
		if (m_btnCancel == null) {
			m_btnCancel = new nc.ui.pub.beans.UIButton();
			m_btnCancel.setName("BtnCancel");
			HotkeyUtil.setHotkeyAndText(m_btnCancel, 'C', nc.ui.ml.NCLangRes
					.getInstance().getStrByID(IProductCode.PRODUCTCODE_COMMON,
							"UC001-0000008"));/* @res "取消" */
			m_btnCancel.addActionListener(this);
		}
		return m_btnCancel;
	}

	/**
	 * @function 得到用于填充界面的东面版
	 * 
	 * @author QuSida
	 * 
	 * @return nc.ui.pub.beans.UIPanel
	 * 
	 * @date 2010-8-20 上午10:22:15
	 */
	private nc.ui.pub.beans.UIPanel getPnlFillSpaceEast() {
		if (m_pnlFillSpaceEast == null) {
			m_pnlFillSpaceEast = new nc.ui.pub.beans.UIPanel();
			m_pnlFillSpaceEast.setName("PnlFillSpaceEast");
			m_pnlFillSpaceEast.setPreferredSize(new java.awt.Dimension(15, 10));
		}
		return m_pnlFillSpaceEast;
	}

	/**
	 * @function 得到用于填充界面的西面板
	 * 
	 * @author QuSida
	 * 
	 * @return nc.ui.pub.beans.UIPanel
	 * 
	 * @date 2010-8-20 上午10:22:47
	 */
	private nc.ui.pub.beans.UIPanel getPnlFillSpaceWest() {
		if (m_pnlFillSpaceWest == null) {
			m_pnlFillSpaceWest = new nc.ui.pub.beans.UIPanel();
			m_pnlFillSpaceWest.setName("PnlFillSpaceWest");
			m_pnlFillSpaceWest.setPreferredSize(new java.awt.Dimension(15, 16));
		}
		return m_pnlFillSpaceWest;
	}

	/**
	 * @function 得到单据列表面板
	 * 
	 * @author QuSida
	 * 
	 * @return BillListPanel
	 * 
	 * @date 2010-8-20 上午10:23:09
	 */
	private BillListPanel getBillListPanel() {
		if (billListPanel == null) {
			billListPanel = new BillListPanel();
			// 加载模板
			//billListPanel.loadTemplet("0001ZZ10000000001TLB");
			billListPanel.loadTemplet("HJ1001", null, ClientEnvironment.getInstance().getUser().getPrimaryKey(),
					ClientEnvironment.getInstance().getCorporation().getPk_corp());  //wanglei 2014-04-20 调整调用模板方式
			//billListPanel.getHeadBillModel().setEnabledAllItems(true);
			billListPanel.addEditListener(this);
		}
		return billListPanel;
	}

	/**
	 * @function 按钮动作处理
	 * 
	 * @author QuSida
	 * 
	 * @param e
	 * 
	 * @date 2010-8-20 上午10:41:37
	 */
	public void actionPerformed(ActionEvent e) {
		// 取消按钮的动作
		if (e.getSource() == this.getBtnCancel()) {
			this.m_closeMark = false;
			this.closeCancel();
		}
		// 确定按钮动作
		else if (e.getSource() == this.getBtnOK()) {

			if (!checkNotNullItems()){
				return;
			}
				//wanglei 2014-04-20 增加非空校验
			icvos = (InformationCostVO[])getBillListPanel().getHeadBillModel().getBodyValueVOs(InformationCostVO.class.getName());
			
			if( icvos == null || icvos.length == 0){
				this.m_closeMark = true;
				this.closeOK();
			}
               String message = voCheck(icvos).trim();
			if(!message.equals("")){
				MessageDialog.showHintDlg(this, "提示",message);
				return;
			}

//			for (int i = 0; i < icvos.length; i++) {
//				icvos[i].setStatus(nc.vo.pub.VOStatus.NEW);
//			}
			this.m_closeMark = true;
			this.closeOK();
		}
		// 增加按钮动作
		else if (e.getSource() == this.m_btnAdd) {
			getBillListPanel().getHeadBillModel().addLine();
			getBillListPanel().getHeadBillModel().execLoadFormula();//add by MeiChao 2010-11-09
			getBillListPanel().getHeadBillModel().setEnabledAllItems(true);
		//add by 付世超 2010-10-13 begin
			int row = getBillListPanel().getHeadTable().getRowCount();
			getBillListPanel().getHeadBillModel().setValueAt(arrnumber, row-1, "nnumber");
			getBillListPanel().getHeadBillModel().setValueAt(billtype, row-1, "vdef10");//使用自定义项vdef10 存储当前费用对应的单据类型 by 付世超 2010-10-30
		//add by 付世超 2010-10-13 end		

		}
		// 删除按钮动作
		else if (e.getSource() == this.m_btnDel) {
			int[] delRows = getBillListPanel().getHeadTable().getSelectedRows();
			getBillListPanel().getHeadBillModel().delLine(delRows);
			getBillListPanel().updateUI();
		}
		// 修改按钮动作
		else if (e.getSource() == this.m_btnMod) {
			getBillListPanel().getHeadBillModel().setEnabledAllItems(true);
		}

	}

	private void setRowEditStatus(int i) {
		// TODO Auto-generated method stub
		getBillListPanel().getHeadBillModel().setCellEditable(i, "noriginalcurprice", false); //设置非编辑项  yiming 2014-6-16
		getBillListPanel().getHeadBillModel().setCellEditable(i, "noriginalcurmny", false); //设置非编辑项  yiming 2014-6-16
	}

	private boolean checkNotNullItems()  {
		// TODO Auto-generated method stub
        StringBuffer message = null;
        BillItem[] headtailitems = getBillListPanel().getBillListData().getHeadItems();
        if (headtailitems != null) {
        	for (int j = 0; j<getBillListPanel().getHeadBillModel().getRowCount(); j++) {
	            for (int i = 0; i < headtailitems.length; i++) {
	                if (headtailitems[i].isNull())
	                    if (isNULL(getBillListPanel().getBillListData().getHeadBillModel().getValueAt(j, headtailitems[i].getKey()))) {
	                        if(message == null)
	                            message = new StringBuffer();
	                        message.append("第").append(j+1).append("行:");
	                        message.append("[");
	                        message.append(headtailitems[i].getName());
	                        message.append("]");
	                        message.append(",");
	                    }
	            }
	            //message.append("\n\r");        	
	            }
        }
        if (message != null){
        	message.append("\n\r");
        	MessageDialog.showErrorDlg(this, "错误", message.append(" 不允许为空，请检查！").toString());
        	return false;
        }
        return true;
	}
	
    private boolean isNULL(Object o) {
        if (o == null || o.toString().trim().equals(""))
            return true;
        return false;
    }
	/**
	 * @function 关闭标志
	 * 
	 * @author QuSida
	 * 
	 * @return boolean
	 * 
	 * @date 2010-8-20 上午10:24:20
	 */
	public boolean isCloseOK() {
		return m_closeMark;
	}

	/**
	 * @function 调用费用信息vo数组，仅为其他类所调用，本类不要使用
	 * 
	 * @author QuSida
	 * 
	 * @return InformationCostVO[]
	 * 
	 * @date 2010-8-20 上午10:24:46
	 */
	public InformationCostVO[] getInfoCostVOs() {
		return icvos;
	}
	private String voCheck(InformationCostVO[] icvos) {

	    for (int i = 0; i < icvos.length; i++) {
			if(icvos[i].getCostcode() == null ||icvos[i].getCostcode().length() == 0 ){
				SCMEnv.out("costcode is null");
				return "第"+i+"行,费用编码为空";				
			}
			else if(icvos[i].getCcostunitid() == null ||icvos[i].getCcostunitid().length() == 0 ){
				SCMEnv.out("costunitid is null");
				return "第"+i+"行,费用单位为空";				
			}
			else if(icvos[i].getCurrtypeid() == null ||icvos[i].getCurrtypeid().length() == 0 ){
				SCMEnv.out("currtypeid is null");
				return "第"+i+"行,币种为空";				
			}
//			else if(icvos[i].getNoriginalcurprice() == null){
//				SCMEnv.out("originalcurprice is null");
//				return "第"+i+"行,无税单价为空";				
//			}
		}
	    return "";
	}

	public void afterEdit(BillEditEvent e) {
		if (e.getKey().equals("ismny")) {
			if ((Boolean) e.getValue()) {
				getBillListPanel().getHeadItem("noriginalcurprice").setValue(null);
				getBillListPanel().getHeadItem("noriginalcurprice").setEdit(false);
				getBillListPanel().getHeadItem("noriginalcurmny").setEdit(true);
			} else {
				getBillListPanel().getHeadItem("noriginalcurmny").setValue(null);
				getBillListPanel().getHeadItem("noriginalcurmny").setEdit(false);
				getBillListPanel().getHeadItem("noriginalcurprice").setEdit(true);
			}

		}
	
		
		//wanglei 2014-06-11 处理其他字段的编辑事件
		if (e.getKey().equals("nnumber")) {
			//getBillListPanel().getHeadItem("nnumber").setValue( e.getValue() == null? UFDouble.ZERO_DBL: new UFDouble(e.getValue().toString()));
			getBillListPanel().getHeadBillModel().setValueAt(e.getValue(),e.getRow(),"nnumber");
			int row=e.getRow();
			UFDouble nnumber = getBillListPanel().getHeadBillModel().getValueAt(row,"nnumber") == null? UFDouble.ZERO_DBL: (UFDouble) getBillListPanel().getHeadBillModel().getValueAt(row,"nnumber");
			UFDouble noriginalcurprice = getBillListPanel().getHeadBillModel().getValueAt(row,"noriginalcurprice") == null? UFDouble.ZERO_DBL: (UFDouble)getBillListPanel().getHeadBillModel().getValueAt(row,"noriginalcurprice");
			UFDouble ntaxrate = getBillListPanel().getHeadBillModel().getValueAt(row,"ntaxrate") == null? UFDouble.ZERO_DBL: (UFDouble) getBillListPanel().getHeadBillModel().getValueAt(row,"ntaxrate");
			UFDouble noriginalcurtaxprice = getBillListPanel().getHeadBillModel().getValueAt(row,"noriginalcurtaxprice") == null? UFDouble.ZERO_DBL: (UFDouble)getBillListPanel().getHeadBillModel().getValueAt(row,"noriginalcurtaxprice");
			
			UFDouble noriginalcursummny = nnumber.multiply(noriginalcurtaxprice).setScale(2, UFDouble.ROUND_HALF_UP);
			UFDouble noriginalcurmny = nnumber.multiply(noriginalcurtaxprice).div(ntaxrate.div(100).add(new UFDouble(1))).setScale(2, UFDouble.ROUND_HALF_UP);
			
			getBillListPanel().getHeadBillModel().setValueAt((nnumber.multiply(noriginalcurtaxprice)).setScale(2, UFDouble.ROUND_HALF_UP), row, "noriginalcursummny");
			getBillListPanel().getHeadBillModel().setValueAt(nnumber.multiply(noriginalcurtaxprice).div(ntaxrate.div(100).add(new UFDouble(1))).setScale(2, UFDouble.ROUND_HALF_UP), row, "noriginalcurmny");
			getBillListPanel().getHeadBillModel().setValueAt(noriginalcurprice, row, "nprice");
			getBillListPanel().getHeadBillModel().setValueAt(noriginalcurtaxprice, row, "ntaxprice");
			getBillListPanel().getHeadBillModel().setValueAt(noriginalcursummny, row, "nsummny");
			getBillListPanel().getHeadBillModel().setValueAt(noriginalcurmny, row, "nmny");
			//reCalculateRow(e.getRow());
			
			//noriginalcurmny->nnumber*noriginalcurprice;
			//noriginalcursummny->nnumber*noriginalcurtaxprice;
			//nprice->noriginalcurprice;
			//ntaxprice->noriginalcurtaxprice;
			//nsummny->noriginalcursummny;
			//nmny->noriginalcurmny;
			
		}
		if (e.getKey().equals("ntaxrate")) {
			//getBillListPanel().getHeadItem("nnumber").setValue( e.getValue() == null? UFDouble.ZERO_DBL: new UFDouble(e.getValue().toString()));
			getBillListPanel().getHeadBillModel().setValueAt(e.getValue(),e.getRow(),"ntaxrate");
			int row=e.getRow();
			UFDouble nnumber = getBillListPanel().getHeadBillModel().getValueAt(row,"nnumber") == null? UFDouble.ZERO_DBL: (UFDouble) getBillListPanel().getHeadBillModel().getValueAt(row,"nnumber");
			UFDouble ntaxrate = getBillListPanel().getHeadBillModel().getValueAt(row,"ntaxrate") == null? UFDouble.ZERO_DBL: (UFDouble) getBillListPanel().getHeadBillModel().getValueAt(row,"ntaxrate");
			UFDouble noriginalcurtaxprice = getBillListPanel().getHeadBillModel().getValueAt(row,"noriginalcurtaxprice") == null? UFDouble.ZERO_DBL: (UFDouble)getBillListPanel().getHeadBillModel().getValueAt(row,"noriginalcurtaxprice");
			UFDouble noriginalcursummny = getBillListPanel().getHeadBillModel().getValueAt(row,"noriginalcursummny") == null? UFDouble.ZERO_DBL: (UFDouble) getBillListPanel().getHeadBillModel().getValueAt(row,"noriginalcursummny");

			UFDouble noriginalcurmny = noriginalcursummny.div(ntaxrate.div(100).add(new UFDouble(1))).setScale(2, UFDouble.ROUND_HALF_UP);		
			UFDouble noriginalcurprice = noriginalcurtaxprice.div(ntaxrate.div(100).add(new UFDouble(1))).setScale(8, UFDouble.ROUND_HALF_UP);	//单价设置8位小数，yiming，20150305
			
			getBillListPanel().getHeadBillModel().setValueAt(noriginalcurtaxprice.div(ntaxrate.div(100).add(new UFDouble(1))).setScale(8, UFDouble.ROUND_HALF_UP), row, "noriginalcurprice"); //单价设置8位小数，yiming，20150305
			getBillListPanel().getHeadBillModel().setValueAt(noriginalcursummny.div(ntaxrate.div(100).add(new UFDouble(1))).setScale(2, UFDouble.ROUND_HALF_UP), row, "noriginalcurmny");
			getBillListPanel().getHeadBillModel().setValueAt(noriginalcurprice, row, "nprice");
			getBillListPanel().getHeadBillModel().setValueAt(noriginalcurtaxprice, row, "ntaxprice");
			getBillListPanel().getHeadBillModel().setValueAt(noriginalcursummny, row, "nsummny");
			getBillListPanel().getHeadBillModel().setValueAt(noriginalcurmny, row, "nmny");
			//reCalculateRow(e.getRow());
			
			//noriginalcurprice->noriginalcurtaxprice/(1+ntaxrate/100);
			//noriginalcurmny->nnumber*noriginalcurtaxprice/(1+ntaxrate/100);
			//nprice->noriginalcurprice;
			//ntaxprice->noriginalcurtaxprice;
			//nsummny->noriginalcursummny;
			//nmny->noriginalcurmny;

			
		}

				
		if (e.getKey().equals("noriginalcurtaxprice")) {
			getBillListPanel().getHeadBillModel().setValueAt(e.getValue(),e.getRow(),"noriginalcurtaxprice");
			int row=e.getRow();
			UFDouble nnumber = getBillListPanel().getHeadBillModel().getValueAt(row,"nnumber") == null? UFDouble.ZERO_DBL: (UFDouble) getBillListPanel().getHeadBillModel().getValueAt(row,"nnumber");
//			UFDouble noriginalcurprice = getBillListPanel().getHeadBillModel().getValueAt(row,"noriginalcurprice") == null? UFDouble.ZERO_DBL: (UFDouble)getBillListPanel().getHeadBillModel().getValueAt(row,"noriginalcurprice");
			UFDouble ntaxrate = getBillListPanel().getHeadBillModel().getValueAt(row,"ntaxrate") == null? UFDouble.ZERO_DBL: (UFDouble) getBillListPanel().getHeadBillModel().getValueAt(row,"ntaxrate");
			UFDouble noriginalcurtaxprice = getBillListPanel().getHeadBillModel().getValueAt(row,"noriginalcurtaxprice") == null? UFDouble.ZERO_DBL: (UFDouble)getBillListPanel().getHeadBillModel().getValueAt(row,"noriginalcurtaxprice");
//			UFDouble noriginalcursummny = getBillListPanel().getHeadBillModel().getValueAt(row,"noriginalcursummny") == null? UFDouble.ZERO_DBL: (UFDouble) getBillListPanel().getHeadBillModel().getValueAt(row,"noriginalcursummny");
//			UFDouble noriginalcurmny = getBillListPanel().getHeadBillModel().getValueAt(row,"noriginalcurmny") == null? UFDouble.ZERO_DBL: (UFDouble)getBillListPanel().getHeadBillModel().getValueAt(row,"noriginalcurmny");		

			UFDouble noriginalcurprice = noriginalcurtaxprice.div(ntaxrate.div(100).add(new UFDouble(1))).setScale(8, UFDouble.ROUND_HALF_UP); //单价设置8位小数，yiming，20150305
			UFDouble noriginalcursummny = noriginalcurtaxprice.multiply(nnumber, 2,  UFDouble.ROUND_HALF_UP);
			UFDouble noriginalcurmny = noriginalcurtaxprice.multiply(nnumber).div(ntaxrate.div(100).add(new UFDouble(1))).setScale(2, UFDouble.ROUND_HALF_UP);		
					
			getBillListPanel().getHeadBillModel().setValueAt(nnumber.multiply(noriginalcurtaxprice).setScale(2, UFDouble.ROUND_HALF_UP), row, "noriginalcursummny");
			getBillListPanel().getHeadBillModel().setValueAt(nnumber.multiply(noriginalcurtaxprice).div(ntaxrate.div(100).add(new UFDouble(1))).setScale(2, UFDouble.ROUND_HALF_UP), row, "noriginalcurmny");
			getBillListPanel().getHeadBillModel().setValueAt(noriginalcurtaxprice.div(ntaxrate.div(100).add(new UFDouble(1))).setScale(8, UFDouble.ROUND_HALF_UP), row, "noriginalcurprice"); //单价设置8位小数，yiming，20150305
			getBillListPanel().getHeadBillModel().setValueAt(noriginalcurprice, row, "nprice");
			getBillListPanel().getHeadBillModel().setValueAt(noriginalcurtaxprice, row, "ntaxprice");
			getBillListPanel().getHeadBillModel().setValueAt(noriginalcursummny, row, "nsummny");
			getBillListPanel().getHeadBillModel().setValueAt(noriginalcurmny, row, "nmny");
			
			//noriginalcursummny->nnumber*noriginalcurtaxprice;
			//noriginalcurmny->nnumber*noriginalcurprice;
			//noriginalcurprice->noriginalcurtaxprice/(1+ntaxrate/100);
			//nprice->noriginalcurprice;
			//ntaxprice->noriginalcurtaxprice;
			//nsummny->noriginalcursummny;
			//nmny->noriginalcurmny;
		}
		
		if (e.getKey().equals("noriginalcursummny")) {
			getBillListPanel().getHeadBillModel().setValueAt(e.getValue(),e.getRow(),"noriginalcursummny");
			int row=e.getRow();
			UFDouble nnumber = getBillListPanel().getHeadBillModel().getValueAt(row,"nnumber") == null? UFDouble.ZERO_DBL: (UFDouble) getBillListPanel().getHeadBillModel().getValueAt(row,"nnumber");
//			UFDouble noriginalcurprice = getBillListPanel().getHeadBillModel().getValueAt(row,"noriginalcurprice") == null? UFDouble.ZERO_DBL: (UFDouble)getBillListPanel().getHeadBillModel().getValueAt(row,"noriginalcurprice");
			UFDouble ntaxrate = getBillListPanel().getHeadBillModel().getValueAt(row,"ntaxrate") == null? UFDouble.ZERO_DBL: (UFDouble) getBillListPanel().getHeadBillModel().getValueAt(row,"ntaxrate");
//			UFDouble noriginalcurtaxprice = getBillListPanel().getHeadBillModel().getValueAt(row,"noriginalcurtaxprice") == null? UFDouble.ZERO_DBL: (UFDouble)getBillListPanel().getHeadBillModel().getValueAt(row,"noriginalcurtaxprice");
			UFDouble noriginalcursummny = getBillListPanel().getHeadBillModel().getValueAt(row,"noriginalcursummny") == null? UFDouble.ZERO_DBL: (UFDouble) getBillListPanel().getHeadBillModel().getValueAt(row,"noriginalcursummny");
//			UFDouble noriginalcurmny = getBillListPanel().getHeadBillModel().getValueAt(row,"noriginalcurmny") == null? UFDouble.ZERO_DBL: (UFDouble)getBillListPanel().getHeadBillModel().getValueAt(row,"noriginalcurmny");		

			UFDouble noriginalcurtaxprice = noriginalcursummny.div(nnumber).setScale(8, UFDouble.ROUND_HALF_UP); //单价设置8位小数，yiming，20150305
			UFDouble noriginalcurmny = noriginalcursummny.div(ntaxrate.div(100).add(new UFDouble(1))).setScale(2, UFDouble.ROUND_HALF_UP);
			UFDouble noriginalcurprice = noriginalcursummny.div(nnumber).div(ntaxrate.div(100).add(new UFDouble(1))).setScale(8, UFDouble.ROUND_HALF_UP);//单价设置8位小数，yiming，20150305		
					
			getBillListPanel().getHeadBillModel().setValueAt(noriginalcursummny.div(nnumber).setScale(8, UFDouble.ROUND_HALF_UP), row, "noriginalcurtaxprice");
			getBillListPanel().getHeadBillModel().setValueAt(noriginalcursummny.div(ntaxrate.div(100).add(new UFDouble(1))).setScale(2, UFDouble.ROUND_HALF_UP), row, "noriginalcurmny");
			getBillListPanel().getHeadBillModel().setValueAt(noriginalcursummny.div(nnumber).div(ntaxrate.div(100).add(new UFDouble(1))).setScale(8, UFDouble.ROUND_HALF_UP), row, "noriginalcurprice");
			getBillListPanel().getHeadBillModel().setValueAt(noriginalcurprice, row, "nprice");
			getBillListPanel().getHeadBillModel().setValueAt(noriginalcurtaxprice, row, "ntaxprice");
			getBillListPanel().getHeadBillModel().setValueAt(noriginalcursummny, row, "nsummny");
			getBillListPanel().getHeadBillModel().setValueAt(noriginalcurmny, row, "nmny");
			
			//noriginalcurtaxprice->noriginalcursummny/nnumber;
			//noriginalcurmny->noriginalcursummny/(1+ntaxrate/100);
			//noriginalcurprice->noriginalcursummny/nnumber/(1+ntaxrate/100);
			//nprice->noriginalcurprice;
			//ntaxprice->noriginalcurtaxprice;
			//nsummny->noriginalcursummny;
			//nmny->noriginalcurmny;
		}

		//end
	}

	//wanglei 2014-06-11 处理其他字段的编辑事件
	private void reCalculateRow(int row){
		UFDouble nnumber = getBillListPanel().getHeadBillModel().getValueAt(row,"nnumber") == null? UFDouble.ZERO_DBL: (UFDouble) getBillListPanel().getHeadBillModel().getValueAt(row,"nnumber");
		UFDouble noriginalcurprice = getBillListPanel().getHeadBillModel().getValueAt(row,"noriginalcurprice") == null? UFDouble.ZERO_DBL: (UFDouble)getBillListPanel().getHeadBillModel().getValueAt(row,"noriginalcurprice");
		getBillListPanel().getHeadBillModel().setValueAt(nnumber.multiply(noriginalcurprice), row, "noriginalcurmny");
	}
	//end
	/**
	 * @function 设置存货总数量
	 * 
	 * @author 付世超
	 * 
	 * @return void
	 * 
	 * @date 2010-8-20 上午10:13:42
	 */
	public void setArrnumber(java.awt.Container parent) {
		if(arrnumber == null){
			arrnumber = new UFDouble(0.0);
		}
		parentCardPanel = (BillCardPanel) parent;
		int temp = parentCardPanel.getBillModel("table").getRowCount();
		for (int i = 0; i < temp; i++) {
			//当前只是入库单的数量 避免字段各单据ItemKey 定义的名不一样 其他单据如需使用请加入单据类型的判断 by 付世超 2010-10-30
			if("45".equals(parentCardPanel.getBillType())){//采购订单存货数量
				arrnumber = arrnumber.add(new UFDouble((parentCardPanel.getBillModel("table").getValueAt(i,"ninnum")==null?0.0:parentCardPanel.getBillModel("table").getValueAt(i,"ninnum")).toString())); 
			}
		}
		
	}
	
	/**
	 * @function 设置当前单据类型
	 * 
	 * @author 付世超
	 * 
	 * @return void
	 * 
	 * @date 2010-10-30
	 */
	public void setBillType(java.awt.Container parent) {
		
		parentCardPanel = (BillCardPanel) parent;
		this.billtype = parentCardPanel.getBillType();
	}
	public void bodyRowChange(BillEditEvent e) {
		// TODO Auto-generated method stub
		if(e.getRow() != -1 )  //删除行排除掉 2014-6-18 wanglei
			setRowEditStatus(e.getRow());
	}

}
