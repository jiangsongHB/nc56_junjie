package nc.ui.so.taxinvoice;

import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.sun.org.apache.bcel.internal.verifier.structurals.ExceptionHandlers;

import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.bs.scm.pub.smart.SmartDMO;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIButton;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillEditListener;
import nc.ui.pub.bill.BillEditListener2;
import nc.ui.pub.bill.BillModel;
import nc.ui.pub.bill.BillStatus;
import nc.ui.pub.bill.IBillModelRowStateChangeEventListener;
import nc.ui.pub.bill.RowStateChangeEvent;
import nc.ui.pub.querymodel.SWTUtil;
import nc.ui.scm.pattern.tool.InvInfoTool;
import nc.ui.so.so002.SaleInvoiceQuery;
import nc.ui.so.so002.SaleInvoiceTools;
import nc.ui.so.so002.SaleinvoiceBO_Client;
import nc.ui.so.so002.pf.SaleInvoiceBillRefListPanel;
import nc.ui.so.so042.SaleIncomeDetailBO_Client;
import nc.uif.pub.exception.UifException;
import nc.vo.bd.invdoc.InvbasdocVO;
import nc.vo.pi.InvoiceVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.VOStatus;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scmpub.TaxInvoiceTypeVO;
import nc.vo.so.TaxInvoiceBbVO;
import nc.vo.so.TaxInvoiceDealVO;
import nc.vo.so.TaxInvoiceHeaderVO;
import nc.vo.so.TaxInvoiceItemVO;
import nc.vo.so.TaxInvoiceVO;
import nc.vo.so.so002.SaleVO;
import nc.vo.so.so002.SaleinvoiceBVO;
import nc.vo.so.so002.SaleinvoiceVO;
import nc.vo.trade.pub.IBillStatus;

public class VerifyQryPanel extends UIPanel implements ActionListener, ChangeListener,IBillModelRowStateChangeEventListener,
BillEditListener, BillEditListener2 {

	private nc.ui.pub.bill.BillCardPanel ivjPanel = null;
	private UIButton UIButtonDel;
	private UIButton UIButtonEdit;
	private UIButton UIButtonSave;
	private UIButton UIButtonCan;
	private UIPanel UIPanel;
	private BillCardPanel ijVerifyCardPanel;
	private TaxInvoiceVO taxInvoiceVO; 
	private TaxInvoiceItemVO taxInvoiceItemVO;
	private VerifyQryDialog ijVerifyQryDialog;
	
	public VerifyQryPanel() {
		super();
		//initialize();
		//initData();
	}

	public VerifyQryPanel(int act) {
		super();
		initialize();
		//initData();
	}
	/**
	 * VerifyInTimePanel 构造子注解。
	 * @param p0 java.awt.LayoutManager
	 */
	public VerifyQryPanel(java.awt.LayoutManager p0) {
		super(p0);
	}
	/**
	 * VerifyInTimePanel 构造子注解。
	 * @param p0 java.awt.LayoutManager
	 * @param p1 boolean
	 */
	public VerifyQryPanel(java.awt.LayoutManager p0, boolean p1) {
		super(p0, p1);
	}
	/**
	 * VerifyInTimePanel 构造子注解。
	 * @param p0 boolean
	 */
	public VerifyQryPanel(boolean p0) {
		super(p0);
	}
	
	public nc.ui.pub.bill.BillCardPanel getIvjPanel() {
		return ivjPanel;
	}

	public void setIvjPanel(nc.ui.pub.bill.BillCardPanel ivjDocPanel) {
		this.ivjPanel = ivjDocPanel;
	}
	/**
	 * 初始化类。
	 */
	public void initialize() {
		try {
			this.setLayout(new BorderLayout());
			this.setSize(new Dimension(1024, 700));
			this.add(getVerifyCardPanel(), BorderLayout.CENTER);
			this.add(getUIPanel(), BorderLayout.SOUTH);
			getVerifyCardPanel().setEnabled(true);
			getVerifyCardPanel().addEditListener(this);
			getVerifyCardPanel().addBodyEditListener2(this);
			initButtons();
		} catch (Exception e) {
			Logger.error(e.getMessage());
		}
		getBillCardPanel().addEditListener(this);
	}
	
	private void initButtons() {
		// TODO Auto-generated method stub
		getUIButtonDel().show(true);
		getUIButtonEdit().show(true);
		getUIButtonDel().setEnabled(true);
		getUIButtonDel().addActionListener(this);
		getUIButtonEdit().setEnabled(true);
		getUIButtonEdit().addActionListener(this);
		getUIButtonSave().addActionListener(this);
		getUIButtonCan().addActionListener(this);
	}

	/**
	 * 返回 DocPanel 特性值。
	 * @return nc.ui.pub.bill.BillCardPanel
	 */
	/* 警告：此方法将重新生成。 */
	private BillCardPanel getBillCardPanel() {
		if (ivjPanel == null) {
			try {
				ivjPanel = new BillCardPanel();
				ivjPanel.setName("VerifyQryPanel");
				// user code begin {1}
				ivjPanel.setEnabled(true);
				ivjPanel.getBillTable().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
				ivjPanel.setTatolRowShow(true);
				// user code end
			} catch  (Exception e) {
				Logger.error(e.getMessage());
			}
		}
		return ivjPanel;
	}
	
	private void initBtn() {
		// TODO Auto-generated method stub
		
	}

	public void initData(){

		QryDealData();
		getVerifyCardPanel().getBillModel().setEnabled(true);

	}
	
	private void QryDealData() {
		// TODO Auto-generated method stub
		nc.itf.uif.pub.IUifService srv =(nc.itf.uif.pub.IUifService)NCLocator.getInstance().lookup(nc.itf.uif.pub.IUifService.class.getName());
		try {
			String taxinvoiceid = ((TaxInvoiceHeaderVO)taxInvoiceVO.getParentVO()).getCtaxinvoiceid();
			String taxinvoice_bid = taxInvoiceItemVO.getCtaxinvoice_bid();
			TaxInvoiceBbVO[] vos = (TaxInvoiceBbVO[])srv.queryByCondition(TaxInvoiceBbVO.class, "ctaxinvoiceid = '" + taxinvoiceid +"' and ctaxinvoice_bid = '"+ taxinvoice_bid + "' "  );
			getVerifyCardPanel().getBillModel().setBodyDataVO(vos);
		} catch (UifException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 初始化系统信息。
	 * 创建日期：(2002-4-3 14:58:25)
	 */
	private void initSysInfo() {

		try {

		} catch (Exception ex) {
			Logger.error(ex.getMessage());
		}
	}
	private UIButton getUIButtonDel() {
		if (UIButtonDel == null) {
			UIButtonDel = new UIButton();
			UIButtonDel.setBounds(new Rectangle(352, 4, 75, 20));
			UIButtonDel.setText("删  除");
			UIButtonDel.setToolTipText("<HTML><B>删除分配的核销数据</B></HTML>");
		}
		return UIButtonDel;
	}

	/**
	 * This method initializes UIButtonDel
	 * 
	 * @return nc.ui.pub.beans.UIButton
	 */
	private UIButton getUIButtonEdit() {
		if (UIButtonEdit == null) {
			UIButtonEdit = new UIButton();
			UIButtonEdit.setBounds(new Rectangle(432, 4, 75, 20));
			UIButtonEdit.setText("编  辑");
			UIButtonEdit.setToolTipText("<HTML><B>修改核销数据</B></HTML>");
		}
		return UIButtonEdit;
	}

	/**
	 * This method initializes UIButtonSave
	 * 
	 * @return nc.ui.pub.beans.UIButton
	 */
	private UIButton getUIButtonSave() {
		if (UIButtonSave == null) {
			UIButtonSave = new UIButton();
			UIButtonSave.setBounds(new Rectangle(512, 4, 75, 20));
			UIButtonSave.setText("保  存");
			UIButtonSave.setToolTipText("<HTML><B>保存核销结果</B></HTML>");
		}
		return UIButtonSave;
	}

	/**
	 * This method initializes UIButtonCan
	 * 
	 * @return nc.ui.pub.beans.UIButton
	 */
	private UIButton getUIButtonCan() {
		if (UIButtonCan == null) {
			UIButtonCan = new UIButton();
			UIButtonCan.setBounds(new Rectangle(592, 4, 75, 20));
			UIButtonCan.setText("关  闭");
			UIButtonCan.setToolTipText("<HTML><B>关闭核销窗口(CTRL + X)</B></HTML>");
		}
		return UIButtonCan;
	}
	/**
	 * This method initializes UIPanel
	 * 
	 * @return nc.ui.pub.beans.UIPanel
	 */
	private UIPanel getUIPanel() {
		if (UIPanel == null) {
			UIPanel = new UIPanel();
			UIPanel.setLayout(null);
			UIPanel.setPreferredSize(new Dimension(1024, 50));
			UIPanel.add(getUIButtonDel(), null);
			UIPanel.add(getUIButtonEdit(), null);
			UIPanel.add(getUIButtonSave(), null);
			UIPanel.add(getUIButtonCan(), null);
		}
		return UIPanel;
	}
	
	private BillCardPanel getVerifyCardPanel() {
		if (ijVerifyCardPanel == null) {
			ijVerifyCardPanel = new BillCardPanel();
			ijVerifyCardPanel.setSize(1024, 650);
			ijVerifyCardPanel.loadTemplet( "32H03" , null, ClientEnvironment.getInstance().getUser().getPrimaryKey(), ClientEnvironment.getInstance().getCorporation().getPk_corp());
			ijVerifyCardPanel.setTatolRowShow(true);
			ijVerifyCardPanel.setRowNOShow(true);
			ijVerifyCardPanel.setBodyMultiSelect(true);
			ijVerifyCardPanel.getBodyItem("ndealnum").setDecimalDigits(4);
			ijVerifyCardPanel.getBodyItem("nwriteinvoicenum").setDecimalDigits(4);
			ijVerifyCardPanel.getBodyItem("ndealmny").setDecimalDigits(2);
			ijVerifyCardPanel.getBodyItem("nwriteinvoicemny").setDecimalDigits(2);
		}
		return ijVerifyCardPanel;
	}
	
	public void afterEdit(BillEditEvent e) {
		// TODO Auto-generated method stub

	}

	public void bodyRowChange(BillEditEvent e) {
		// TODO Auto-generated method stub

	}

	public void stateChanged(ChangeEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public boolean beforeEdit(BillEditEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if (e.getSource().equals(getUIButtonEdit())) {
			onBtnEdit();
		}
		if (e.getSource().equals(getUIButtonCan())) {
			onBtnCan();
		}
		if (e.getSource().equals(getUIButtonDel())) {
			onBtnDel();
		}
		if (e.getSource().equals(getUIButtonSave())) {
			onBtnSave();
		}
	}

	private void onBtnSave() {
		// TODO Auto-generated method stub
		
	}

	private void onBtnDel() {
		// TODO Auto-generated method stub
		TaxInvoiceDealVO[] vos = getSelectedVO();
		for (int i = 0; i< vos.length; i++)
			vos[i].setStatus(VOStatus.DELETED);
		
		getVerifyCardPanel().updateValue();
	}

	private void onBtnCan() {
		// TODO Auto-generated method stub 
		getIjVerifyQryDialog().closeOK();
	}

	private void onBtnEdit() {
		// TODO Auto-generated method stub
		if (getSelectedVO().length > 1) {
			MessageDialog.showErrorDlg(this, "错误", "请选择一行数据进行编辑！");
			return;
		}
		
	}

	private TaxInvoiceDealVO[] getSelectedVO(){
		ArrayList<TaxInvoiceDealVO> alvos = new ArrayList();
		TaxInvoiceDealVO vo = null;
		for (int i=0 ; i<getVerifyCardPanel().getBillTable().getRowCount(); i++){
			if (getVerifyCardPanel().getBillModel().getRowState(i) == BillModel.SELECTED) {
				vo = (TaxInvoiceDealVO)getVerifyCardPanel().getBillModel().getBodyValueRowVO(i, TaxInvoiceDealVO.class.getName());
				alvos.add(vo);
			}
		}
		

		if (alvos ==null || alvos.size() == 0 )
			return null;
		
		TaxInvoiceDealVO[] vos = new TaxInvoiceDealVO[alvos.size()];
			return alvos.toArray(vos);
		
	}
	public TaxInvoiceVO getTaxInvoiceVO() {
		return taxInvoiceVO;
	}

	public void setTaxInvoiceVO(TaxInvoiceVO taxInvoiceVO) {
		this.taxInvoiceVO = taxInvoiceVO;
	}

	public TaxInvoiceItemVO getTaxInvoiceItemVO() {
		return taxInvoiceItemVO;
	}

	public void setTaxInvoiceItemVO(TaxInvoiceItemVO taxInvoiceItemVO) {
		this.taxInvoiceItemVO = taxInvoiceItemVO;
	}

	public VerifyQryDialog getIjVerifyQryDialog() {
		return ijVerifyQryDialog;
	}

	public void setIjVerifyQryDialog(VerifyQryDialog ijVerifyQryDialog) {
		this.ijVerifyQryDialog = ijVerifyQryDialog;
	}

	public void valueChanged(RowStateChangeEvent event) {
		// TODO Auto-generated method stub
		
	}

}
