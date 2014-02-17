package nc.ui.so.taxinvoice;

import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.sun.org.apache.bcel.internal.verifier.structurals.ExceptionHandlers;

import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.bs.scm.pub.smart.SmartDMO;
import nc.itf.so.taxinvoice.ITaxInvoice;
import nc.itf.uap.IVOPersistence;
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
import nc.ui.scm.so.SaleBillType;
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
import nc.vo.so.SaleInvoiceBVOForVerify;
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

//	private nc.ui.pub.bill.BillCardPanel ivjPanel = null;
	private UIButton UIButtonDel;
	private UIButton UIButtonEdit;
	private UIButton UIButtonSave;
	private UIButton UIButtonCan;
	private UIPanel UIPanel;
	private BillCardPanel ijVerifyCardPanel;
	private TaxInvoiceVO taxInvoiceVO; 
	private TaxInvoiceItemVO taxInvoiceItemVO;
	private VerifyQryDialog ijVerifyQryDialog;
	private ArrayList<TaxInvoiceBbVO> alDelvos = new ArrayList();
	
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
	
//	public nc.ui.pub.bill.BillCardPanel getIvjPanel() {
//		return ivjPanel;
//	}
//
//	public void setIvjPanel(nc.ui.pub.bill.BillCardPanel ivjDocPanel) {
//		this.ivjPanel = ivjDocPanel;
//	}
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
		getVerifyCardPanel().addEditListener(this);
	}
	
	private void initButtons() {
		// TODO Auto-generated method stub
		getUIButtonDel().setEnabled(false);
		getUIButtonDel().addActionListener(this);
//		getUIButtonEdit().setEnabled(true);
//		getUIButtonEdit().addActionListener(this);
		getUIButtonSave().addActionListener(this);
		getUIButtonCan().addActionListener(this);
	}

	private ArrayList<TaxInvoiceDealVO> getDealVOs(){ 
		
		ArrayList<TaxInvoiceDealVO> alvos = new ArrayList();
		TaxInvoiceDealVO vo = null;
		for (int i=0 ; i<getVerifyCardPanel().getBillTable().getRowCount(); i++){
				vo = (TaxInvoiceDealVO)getVerifyCardPanel().getBillModel().getBodyValueRowVO(i, TaxInvoiceDealVO.class.getName());
				alvos.add(vo);
		}
		
		return alvos;
	}
	
	private UFDouble getDealNumSum(ArrayList<TaxInvoiceDealVO> alvos) {
		// TODO Auto-generated method stub
		UFDouble nnumdealsum = UFDouble.ZERO_DBL;
		
		for (int i = 0; i<alvos.size(); i++ ){
			nnumdealsum = nnumdealsum.add(alvos.get(i).getNdealnum()==null?UFDouble.ZERO_DBL:alvos.get(i).getNdealnum() );
		}
			
		return nnumdealsum;
	}

	private UFDouble getDealMnySum(ArrayList<TaxInvoiceDealVO> alvos) {
		// TODO Auto-generated method stub
		UFDouble nmnydealsum = UFDouble.ZERO_DBL;
		for (int i = 0; i<alvos.size(); i++ ){
			nmnydealsum = nmnydealsum.add(alvos.get(i).getNdealmny()==null?UFDouble.ZERO_DBL:alvos.get(i).getNdealmny() );
		}
		return nmnydealsum;
	}
	
	private void updateHeadData() {
		// TODO Auto-generated method stub
		TaxInvoiceItemVO selvo = getTaxInvoiceItemVO();
		UFDouble nmnybal = selvo.getNsummny().sub(selvo.getNtotaldealmny()==null? UFDouble.ZERO_DBL:selvo.getNtotaldealmny()) ;  //得到发票行的数量和金额余额
		UFDouble nnumbal = selvo.getNnumber().sub(selvo.getNtotaldealnum()==null? UFDouble.ZERO_DBL:selvo.getNtotaldealnum());
		
		ArrayList<TaxInvoiceDealVO> alvos = getDealVOs();
		UFDouble nmnydealsum  = getDealMnySum(alvos);
		UFDouble nnumdealsum = getDealNumSum(alvos);
		getVerifyCardPanel().getHeadItem("nmnybal").setValue(nmnybal.sub(nmnydealsum));
		getVerifyCardPanel().getHeadItem("nnumbal").setValue(nnumbal.sub(nnumdealsum));
		getVerifyCardPanel().getHeadItem("ctaxinvoice_bid").setValue(selvo.getCtaxinvoice_bid());
		getVerifyCardPanel().execHeadLoadFormulas();   //执行一下公式，支持自定义公式应用。
	}

	/**
	 * 返回 DocPanel 特性值。
	 * @return nc.ui.pub.bill.BillCardPanel
	 */
	/* 警告：此方法将重新生成。 */
//	private BillCardPanel getBillCardPanel() {
//		if (ivjPanel == null) {
//			try {
//				ivjPanel = new BillCardPanel();
//				ivjPanel.setName("VerifyQryPanel");
//				// user code begin {1}
//				ivjPanel.setEnabled(true);
//				//ivjPanel.getBillTable().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
//				ivjPanel.setTatolRowShow(true);
//				// user code end
//			} catch  (Exception e) {
//				Logger.error(e.getMessage());
//			}
//		}
//		return ivjPanel;
//	}
	
	private void initBtn() {
		// TODO Auto-generated method stub
		
	}

	public void initData(){

		updateHeadData();
		QryDealData();
		getVerifyCardPanel().getBillModel().setEnabled(true);
		getVerifyCardPanel().getBillModel().execLoadFormula();
		getVerifyCardPanel().execHeadLoadFormulas();

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
//			UIButtonDel.setBounds(new Rectangle(432, 4, 75, 20));
			UIButtonDel.setPreferredSize(new Dimension(70, 20));
			UIButtonDel.setText("删  除");
//			UIButtonDel.setToolTipText("<HTML><B>删除分配的核销数据</B></HTML>");
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
//			UIButtonEdit.setBounds(new Rectangle(432, 4, 75, 20));
			UIButtonEdit.setPreferredSize(new Dimension(70, 20));
			UIButtonEdit.setText("编  辑");
//			UIButtonEdit.setToolTipText("<HTML><B>修改核销数据</B></HTML>");
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
//			UIButtonSave.setBounds(new Rectangle(512, 4, 75, 20));
			UIButtonSave.setPreferredSize(new Dimension(70, 20));
			UIButtonSave.setText("保  存");
//			UIButtonSave.setToolTipText("<HTML><B>保存核销结果</B></HTML>");
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
//			UIButtonCan.setBounds(new Rectangle(592, 4, 75, 20));
			UIButtonCan.setPreferredSize(new Dimension(70, 20));
			UIButtonCan.setText("关  闭");
//			UIButtonCan.setToolTipText("<HTML><B>关闭核销窗口(CTRL + X)</B></HTML>");
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
			UIPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
//			UIPanel.setPreferredSize(new Dimension(1024, 50));
			UIPanel.add(getUIButtonDel());
			//UIPanel.add(getUIButtonEdit(), null);  //暂时不支持在这里修改了，允许删除核销记录
			UIPanel.add(getUIButtonSave());
			UIPanel.add(getUIButtonCan());
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
			ijVerifyCardPanel.getBillModel().addRowStateChangeEventListener(this);
			ijVerifyCardPanel.getBodyItem("ndealnum").setDecimalDigits(4);
			ijVerifyCardPanel.getBodyItem("nwriteinvoicenum").setDecimalDigits(4);
			ijVerifyCardPanel.getBodyItem("ndealmny").setDecimalDigits(2);
			ijVerifyCardPanel.getBodyItem("nwriteinvoicemny").setDecimalDigits(2);
			ijVerifyCardPanel.getHeadItem("nmnybal").setDecimalDigits(2);
			ijVerifyCardPanel.getHeadItem("nnumbal").setDecimalDigits(4);  
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
//		if (e.getSource().equals(getUIButtonEdit())) {
//			onBtnEdit();
//		}
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
		ArrayList<TaxInvoiceBbVO> alvos = getAlDelvos();
		
//		reWriteTaxInvoice(alvos);
//		reWriteSaleInvoice(alvos);
//		
//		DeleteDealData(alvos);
		//2014-02-16 使用新的更新方法，放在一个事务中处理更新，保证数据一致性。
		if (alvos.size() ==0) {
			return;
		}
		TaxInvoiceBbVO[] dealvos = new TaxInvoiceBbVO[alvos.size()] ;
		
		ITaxInvoice isrv =(ITaxInvoice) NCLocator.getInstance().lookup(ITaxInvoice.class.getName());
		try {
			isrv.deleteDeal(alvos.toArray(dealvos));
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//2014-02-16 end
		getIjVerifyQryDialog().closeOK();
	}
	
	private void DeleteDealData(ArrayList<TaxInvoiceBbVO> alvos) {
		// TODO Auto-generated method stub
		if (alvos ==null || alvos.size() == 0)
			return;
		
//		ArrayList<TaxInvoiceBbVO> albbvos = new ArrayList();
//		for (int i= 0 ; i < alvos.size(); i++){
//			TaxInvoiceBbVO bbvo = new TaxInvoiceBbVO();
//			bbvo.setCdealoper(ClientEnvironment.getInstance().getUser().getPrimaryKey());
//			bbvo.setCsourcebillid(alvos.get(i).getCsaleid());
//			bbvo.setCsourcebilltype(SaleBillType.SaleInvoice);
//			bbvo.setCsourcebillrowid(alvos.get(i).getCinvoice_bid());
//			bbvo.setCtaxinvoiceid(alvos.get(i).getCtaxinvoiceid());
//			bbvo.setCtaxinvoice_bid(alvos.get(i).getCtaxinvoice_bid());
//			bbvo.setDdealdate(ClientEnvironment.getInstance().getDate());
//			bbvo.setNdealmny(alvos.get(i).getNdealmny());
//			bbvo.setNdealnum(alvos.get(i).getNdealnum());
//			bbvo.setNwriteinvoicemny(alvos.get(i).getNrewritemny());
//			bbvo.setNwriteinvoicenum(alvos.get(i).getNrewritenum());
//			bbvo.setDr(0);
//			bbvo.setStatus(VOStatus.NEW);
//			albbvos.add(bbvo);
//		}

		IVOPersistence iVOPersistence = (IVOPersistence) NCLocator
		.getInstance().lookup(IVOPersistence.class.getName());
		
		TaxInvoiceBbVO[] bbvos = new TaxInvoiceBbVO[alvos.size()];
		try {
			iVOPersistence.deleteVOArray(alvos.toArray(bbvos));
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void reWriteTaxInvoice(ArrayList<TaxInvoiceBbVO> alvos) {
		// TODO Auto-generated method stub
		if (alvos ==null || alvos.size() == 0)
			return;
		
		UFDouble ndealnum = new UFDouble(0.0);
		UFDouble ndealmny = new UFDouble(0.0);
		for (int i= 0 ; i < alvos.size(); i++){
			ndealnum = ndealnum.add(alvos.get(i).getNdealnum());
			ndealmny = ndealmny.add(alvos.get(i).getNdealmny());
		}
		getTaxInvoiceItemVO().setCtaxinvoiceid(getTaxInvoiceVO().getParentVO().getAttributeValue("ctaxinvoiceid").toString());
		getTaxInvoiceItemVO().setNtotaldealmny(getTaxInvoiceItemVO().getNtotaldealmny()==null?ndealmny: getTaxInvoiceItemVO().getNtotaldealmny().sub(ndealmny));
		getTaxInvoiceItemVO().setNtotaldealnum(getTaxInvoiceItemVO().getNtotaldealnum()==null?ndealnum: getTaxInvoiceItemVO().getNtotaldealnum().sub(ndealnum));
		getTaxInvoiceItemVO().setStatus(VOStatus.UPDATED);
		
		IVOPersistence iVOPersistence = (IVOPersistence) NCLocator
		.getInstance().lookup(IVOPersistence.class.getName());
		
		try {
			iVOPersistence.updateVO(getTaxInvoiceItemVO());
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void reWriteSaleInvoice(ArrayList<TaxInvoiceBbVO> alvos) {
		// TODO Auto-generated method stub
		if (alvos == null || alvos.size() == 0)
			return;
		try {
			
		nc.itf.uif.pub.IUifService srv =(nc.itf.uif.pub.IUifService)NCLocator.getInstance().lookup(nc.itf.uif.pub.IUifService.class.getName());
		
		IVOPersistence iVOPersistence = (IVOPersistence) NCLocator
				.getInstance().lookup(IVOPersistence.class.getName());

		ArrayList<SaleInvoiceBVOForVerify> albvos = new ArrayList();
		for (int i = 0; i < alvos.size(); i++) {
			SaleInvoiceBVOForVerify[] vo = (SaleInvoiceBVOForVerify[])srv.queryByCondition(SaleInvoiceBVOForVerify.class, "cinvoice_bid = '" + alvos.get(i).getCsourcebillrowid() + "'");
			vo[0].setCsaleid(alvos.get(i).getCsourcebillid());
			vo[0].setCinvoice_bid(alvos.get(i).getCsourcebillrowid());
			vo[0].setNtotaldealnum(vo[0].getNtotaldealnum()==null? UFDouble.ZERO_DBL: vo[0].getNtotaldealnum().sub(alvos.get(i).getNwriteinvoicenum()));
			vo[0].setNtotaldealmny(vo[0].getNtotaldealmny()==null? UFDouble.ZERO_DBL: vo[0].getNtotaldealmny().sub(alvos.get(i).getNwriteinvoicemny()));
			albvos.add(vo[0]);
		}

			SaleInvoiceBVOForVerify[] bvos = new SaleInvoiceBVOForVerify[alvos
					.size()];
			iVOPersistence.updateVOArray(albvos.toArray(bvos));
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void onBtnDel() {
		// TODO Auto-generated method stub
		
		ArrayList al = new ArrayList();
		ArrayList<TaxInvoiceBbVO> alvos = getAlDelvos();
		TaxInvoiceBbVO vo = null;
		
		for (int i = 0; i<getVerifyCardPanel().getBillModel().getRowCount(); i++) {
			if (getVerifyCardPanel().getBillModel().getRowState(i) == BillModel.SELECTED) {
				vo = (TaxInvoiceBbVO)getVerifyCardPanel().getBillModel().getBodyValueRowVO(i, TaxInvoiceBbVO.class.getName());
				alvos.add(vo);
				al.add(i);
			}
		}
		
		if (al == null || al.size()==0) {
			MessageDialog.showErrorDlg(this, "错误", "没有选择删除的行，请选择！");
			return;
		}else{
			if ( MessageDialog.showYesNoDlg(this, "提示", "确认要删除所选的核销明细？") != MessageDialog.ID_YES)  //这里友好提示一下，选择是的话就继续保存
				return;
		}
		
		int[] r = new int[al.size()];
		for (int i = 0 ; i< al.size(); i++) {
			r[i] = ((Integer)al.get(i)).intValue();
		}
		
		getVerifyCardPanel().getBillModel().delLine(r);
		
		updateHeadData();
		
		setAlDelvos(alvos);
		
		getUIButtonDel().setEnabled(false);
		//getVerifyCardPanel().updateValue();
	}

	private void onBtnCan() {
		// TODO Auto-generated method stub 
		ArrayList<TaxInvoiceBbVO> alvos = getAlDelvos();
		if(alvos != null && alvos.size() !=0 ){
			if ( MessageDialog.showYesNoDlg(this, "提示", "当前操作有删除的核销记录还未保存到服务器，是否继续取消？") != MessageDialog.ID_YES)  //这里友好提示一下，选择是的话就继续保存
				return;
		}
		getIjVerifyQryDialog().closeOK();
	}

	private void onBtnEdit() {
		// TODO Auto-generated method stub
//		if (getSelectedVO().length > 1) {
//			MessageDialog.showErrorDlg(this, "错误", "请选择一行数据进行编辑！");
//			return;
//		}
		
	}

//	private ArrayList<TaxInvoiceDealVO> getDeletedVOs(){
//		ArrayList<TaxInvoiceDealVO> alvos = new ArrayList();
//		
//		Vector v = getVerifyCardPanel().getBillModel().getDeleteRow();
//		
//		TaxInvoiceDealVO[] vos = new TaxInvoiceDealVO[v.size()];
//		v.toArray(vos);
//		
//		for (int i =0; i < vos.length; i++)
//			alvos.add(vos[i]);
//		
//		return alvos;
//		
//	}
	
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
		boolean hasselectrow = false;
		for (int i=0 ; i<getVerifyCardPanel().getBillTable().getRowCount(); i++){
			if (getVerifyCardPanel().getBillModel().getRowState(i) == BillModel.SELECTED) {
				hasselectrow = true;
				break;
			}
		}
		
		getUIButtonDel().setEnabled(hasselectrow);
		
	}

	/**
	 * @return the alDelvos
	 */
	public ArrayList<TaxInvoiceBbVO> getAlDelvos() {
		return alDelvos;
	}

	/**
	 * @param alDelvos the alDelvos to set
	 */
	public void setAlDelvos(ArrayList<TaxInvoiceBbVO> alDelvos) {
		this.alDelvos = alDelvos;
	}

}
