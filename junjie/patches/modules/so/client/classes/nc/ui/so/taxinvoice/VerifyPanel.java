package nc.ui.so.taxinvoice;

import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.sun.org.apache.bcel.internal.verifier.structurals.ExceptionHandlers;

import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.bs.scm.pub.smart.SmartDMO;
import nc.itf.scm.so.so002.ISaleinvoiceQuery;
import nc.itf.so.taxinvoice.ITaxInvoice;
import nc.itf.uap.IUAPQueryBS;
import nc.itf.uap.IVOPersistence;
import nc.jdbc.framework.mapping.IMappingMeta;
import nc.jdbc.framework.processor.ArrayListProcessor;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIButton;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillEditListener;
import nc.ui.pub.bill.BillEditListener2;
import nc.ui.pub.bill.BillModel;
import nc.ui.pub.bill.BillStatus;
import nc.ui.pub.bill.BillTableSelectionChangeListener;
import nc.ui.pub.bill.BillTableSelectionEvent;
import nc.ui.pub.bill.BillTotalListener;
import nc.ui.pub.bill.IBillModelRowStateChangeEventListener;
import nc.ui.pub.bill.RowStateChangeEvent;
import nc.ui.pub.component.ButtonPanel;
import nc.ui.pub.querymodel.SWTUtil;
import nc.ui.querytemplate.QueryConditionDLG;
import nc.ui.scm.pattern.tool.InvInfoTool;
import nc.ui.scm.so.SaleBillType;
import nc.ui.so.so002.SaleInvoiceQuery;
import nc.ui.so.so002.SaleInvoiceTools;
import nc.ui.so.so002.SaleinvoiceBO_Client;
import nc.ui.so.so002.pf.SaleInvoiceBillRefListPanel;
import nc.ui.so.so042.SaleIncomeDetailBO_Client;
import nc.ui.trade.base.IBillConst;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.trade.query.HYQueryConditionDLG;
import nc.uif.pub.exception.UifException;
import nc.vo.bd.invdoc.InvbasdocVO;
import nc.vo.pi.InvoiceVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.VOStatus;
import nc.vo.pub.formulaset.FormulaParseFather;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.querytemplate.TemplateInfo;
import nc.vo.scm.pub.IBillCode;
import nc.vo.scmpub.TaxInvoiceTypeVO;
import nc.vo.so.SaleInvoiceBVOForVerify;
import nc.vo.so.TaxInvoiceBbVO;
import nc.vo.so.TaxInvoiceDealVO;
import nc.vo.so.TaxInvoiceItemVO;
import nc.vo.so.TaxInvoiceVO;
import nc.vo.so.so002.SaleVO;
import nc.vo.so.so002.SaleinvoiceBVO;
import nc.vo.so.so002.SaleinvoiceBVOMeta;
import nc.vo.so.so002.SaleinvoiceVO;
import nc.vo.trade.pub.IBillStatus;

public class VerifyPanel extends UIPanel implements ActionListener,IBillModelRowStateChangeEventListener, 
BillEditListener, BillEditListener2, MouseListener {

	//private nc.ui.pub.bill.BillCardPanel ivjPanel = null;
	private UIButton UIButtonDistr;
	private UIButton UIButtonClear;
	private UIButton UIButtonSave;
	private UIButton UIButtonCan;
	private UIButton UIButtonQry;
	private UIPanel UIPanel;
	private BillCardPanel ijVerifyCardPanel;
	private TaxInvoiceVO taxInvoiceVO; 
	private TaxInvoiceItemVO taxInvoiceItemVO; 
	private VerifyDialog ijVerifyDialog;
	private HYQueryConditionDLG ijSaleInvoiceQryDLG;
	
	public VerifyPanel() {
		super();
		//initialize();
		//initData();
	}

	/**
	 * VerifyInTimePanel 构造子注解。
	 * @param p0 java.awt.LayoutManager
	 */
	public VerifyPanel(java.awt.LayoutManager p0) {
		super(p0);
	}
	/**
	 * VerifyInTimePanel 构造子注解。
	 * @param p0 java.awt.LayoutManager
	 * @param p1 boolean
	 */
	public VerifyPanel(java.awt.LayoutManager p0, boolean p1) {
		super(p0, p1);
	}
	/**
	 * VerifyInTimePanel 构造子注解。
	 * @param p0 boolean
	 */
	public VerifyPanel(boolean p0) {
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
			initButtons();
		} catch (Exception e) {
			Logger.error(e.getMessage());
		}
//		getBillCardPanel().addEditListener(this);
	}
	
	private void initButtons() {
		// TODO Auto-generated method stub
		getUIButtonDistr().show(true);
		getUIButtonClear().show(true);
		getUIButtonDistr().setEnabled(true);
		getUIButtonDistr().addActionListener(this);
		getUIButtonClear().setEnabled(true);
		getUIButtonClear().addActionListener(this);
		getUIButtonSave().addActionListener(this);
		getUIButtonCan().addActionListener(this);
		getUIButtonQry().addActionListener(this);
//		getUIButtonQryLink().addActionListener(this);
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
//				ivjPanel.setName("VerifyPanel");
//				// user code begin {1}
//				//ivjPanel.setEnabled(true);
//				ivjPanel.getBillTable().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
//				ivjPanel.setTatolRowShow(true);
//				ivjPanel.setBodyMenuShow(false);
//				ivjPanel.addBodyEditListener2(this);
//				ivjPanel.addEditListener(this);
//				ivjPanel.setEnabled(true);
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
		String sql = null;
		QryInvoiceData(sql);
		updateHeadData();

	}

	private void updateHeadData() {
		// TODO Auto-generated method stub
		TaxInvoiceItemVO selvo = getTaxInvoiceItemVO();
		UFDouble nmnybal = selvo.getNsummny().sub(selvo.getNtotaldealmny()==null? UFDouble.ZERO_DBL:selvo.getNtotaldealmny()) ;  //得到发票行的数量和金额余额
		UFDouble nnumbal = selvo.getNnumber().sub(selvo.getNtotaldealnum()==null? UFDouble.ZERO_DBL:selvo.getNtotaldealnum());
		
		ArrayList<TaxInvoiceDealVO> alvos = getSelectedVOs();
		UFDouble nmnydealsum  = getDealMnySum(alvos);
		UFDouble nnumdealsum = getDealNumSum(alvos);
		getVerifyCardPanel().getHeadItem("nmnybal").setValue(nmnybal.sub(nmnydealsum));
		getVerifyCardPanel().getHeadItem("nnumbal").setValue(nnumbal.sub(nnumdealsum));
		getVerifyCardPanel().getHeadItem("ctaxinvoice_bid").setValue(selvo.getCtaxinvoice_bid());
		getVerifyCardPanel().execHeadLoadFormulas();   //执行一下公式，支持自定义公式应用。
	}

	private void QryInvoiceData(String othersql) {
		// TODO Auto-generated method stub
		
		
		boolean isfee = false; 
		
		String cinvoicetype = (String)taxInvoiceVO.getParentVO().getAttributeValue("cinvoicetype");
		
		nc.itf.uif.pub.IUifService srv =(nc.itf.uif.pub.IUifService)NCLocator.getInstance().lookup(nc.itf.uif.pub.IUifService.class.getName());
		
		ArrayList<TaxInvoiceDealVO> alvos = new ArrayList();
		TaxInvoiceDealVO vo = new TaxInvoiceDealVO();
		try {
			TaxInvoiceTypeVO taxInvTypeVo = (TaxInvoiceTypeVO) srv.queryByPrimaryKey(TaxInvoiceTypeVO.class, cinvoicetype);
			
			isfee = taxInvTypeVo.getIffee().booleanValue();
			
			String sql = "select so_saleinvoice.dbilldate, so_saleinvoice.csaleid, so_saleinvoice.pk_corp,  " +
					"so_saleinvoice.vreceiptcode, so_saleinvoice_b.cinvoice_bid, so_saleinvoice_b.nsummny ,  " +  //应当取价税合计
					"so_saleinvoice_b.nnumber,  so_saleinvoice_b.cinvbasdocid, isnull(so_saleinvoice_b.ntotaldealnum,0),  " +
					"isnull(so_saleinvoice_b.ntotaldealmny,0), so_saleinvoice_b.crowno " +
					"from so_saleinvoice inner join so_saleinvoice_b on so_saleinvoice.csaleid = so_saleinvoice_b.csaleid " +
					"where so_saleinvoice_b.dr = 0 and so_saleinvoice.dr = 0 ";
			
			//发票表头条件，状态，客户
			String strWhere = " and  so_saleinvoice.creceiptcorpid = '" + taxInvoiceVO.getParentVO().getAttributeValue("cordermanid") + "' " +
			" and ( so_saleinvoice.fstatus = " + BillStatus.AUDIT + " or so_saleinvoice.fstatus = " + BillStatus.FINISH + " ) " ;
			
			//strWhere += " and so_saleinvoice.dbilldate >= '2013-01-01' ";
			
			//发票表体条件，是否核销完成
//			strWhere += " and isnull(so_saleinvoice_b.dr,0) = 0 and (isnull(so_saleinvoice_b.ntotaldealnum,0) < so_saleinvoice_b.nnumber  " +   // 发票行条件
//					"or isnull(so_saleinvoice_b.ntotaldealmny,0) < so_saleinvoice_b.nsummny ) and isnull(blargessflag,'N') = 'N' ";  //2014-01-17 增加非赠品条件
			strWhere += " and isnull(so_saleinvoice_b.dr,0) = 0 " +   // 发票行条件 ,取消数量核销限制，只处理金额部分 2014-02-18 wanglei 
					"or isnull(so_saleinvoice_b.ntotaldealmny,0) < so_saleinvoice_b.nsummny ) and isnull(blargessflag,'N') = 'N' ";  //2014-01-17 增加非赠品条件
			
			//费用发票条件
			if (othersql == null || othersql.length() == 0) {  //允许覆盖的查询条件
				if (isfee){
					strWhere += " and exists (" +
					"select * from bd_invbasdoc where (isnull(discountflag,'N') = 'Y' or isnull(laborflag,'N') = 'Y') " +
					" and  pk_invbasdoc = so_saleinvoice_b.cinvbasdocid ) ";
				}else{
					strWhere += " and exists (" +
					"select * from bd_invbasdoc where (isnull(discountflag,'N') = 'N' and isnull(laborflag,'N') = 'N') " +
					" and  pk_invbasdoc = so_saleinvoice_b.cinvbasdocid ) ";
				}
			}
			
			//预开票条件，处理发票日期
			if (((UFBoolean)taxInvoiceVO.getParentVO().getAttributeValue("ispray")).booleanValue()){ //如果是预开票，则选择的发票日期范围在实际发票的日期之后
				strWhere += " and so_saleinvoice.dbilldate >= '" + taxInvoiceVO.getParentVO().getAttributeValue("dinvoicedate") + "' " ;
			}else{
				strWhere += " and so_saleinvoice.dbilldate <= '" + taxInvoiceVO.getParentVO().getAttributeValue("dinvoicedate") + "' " ;
			}
			
			if (othersql != null )
				strWhere += " and (" + othersql + ") "; 
			
			String strOrder = " order by so_saleinvoice.dbilldate , so_saleinvoice.vreceiptcode, so_saleinvoice_b.crowno ";
			
			sql = sql + strWhere + strOrder;
			
			IUAPQueryBS iUAPQueryBS = (IUAPQueryBS) NCLocator.getInstance().lookup(
					IUAPQueryBS.class.getName());
			
			ArrayList reslist= (ArrayList)iUAPQueryBS.executeQuery( sql , new ArrayListProcessor());  //提高查询效率，改用此方法。
			
			TaxInvoiceDealVO[] vos = new TaxInvoiceDealVO[reslist.size()];
			
			for (int i=0; i<reslist.size(); i++ ) {
				Object[] objs= (Object[]) reslist.get(i);	
				vo = new TaxInvoiceDealVO();
				vo.setDbilldate(new UFDate(objs[0].toString())) ;
				vo.setCsaleid(objs[1].toString());
				vo.setPk_corp(objs[2].toString());
				vo.setVreceiptcode(objs[3].toString());
				vo.setCinvoice_bid(objs[4].toString());
				vo.setNmny(new UFDouble(objs[5].toString()));
				vo.setNnumber(new UFDouble(objs[6].toString()));
				vo.setCinvbasdocid(objs[7].toString());
				vo.setNtotaldealmny(new UFDouble(objs[9]==null? "0.0": objs[9].toString()));
				vo.setNtotaldealnum(new UFDouble(objs[8]==null? "0.0": objs[9].toString()));
				vo.setCrowno(objs[10]==null? "": objs[10].toString());
				vo.setCtaxinvoiceid(taxInvoiceVO.getParentVO().getAttributeValue("ctaxinvoiceid").toString());
				vo.setCtaxinvoice_bid(taxInvoiceItemVO.getCtaxinvoice_bid());
				alvos.add(vo);
			}
			alvos.toArray(vos);
			getVerifyCardPanel().getBillModel().clearBodyData();
			getVerifyCardPanel().getBillModel().setBodyDataVO(vos);
			getVerifyCardPanel().getBillModel().execLoadFormula();// 显示公式
			//getVerifyCardPanel().startRowCardEdit();
			
		}catch (BusinessException e) {
			e.printStackTrace();
			MessageDialog
					.showWarningDlg(this, "提示", "数据初始化出错：" + e.getMessage());
		} catch (Exception e) {
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
	private UIButton getUIButtonDistr() {
		if (UIButtonDistr == null) {
			UIButtonDistr = new UIButton();
//			UIButtonDistr.setBounds(new Rectangle(352, 4, 75, 20));
			UIButtonDistr.setPreferredSize(new Dimension(70, 20));
			UIButtonDistr.setText("分  配");
//			UIButtonDistr.setToolTipText("<HTML><B>自动分配待核销数据</B></HTML>");
		}
		return UIButtonDistr;
	}

	/**
	 * This method initializes UIButtonDel
	 * 
	 * @return nc.ui.pub.beans.UIButton
	 */
	private UIButton getUIButtonClear() {
		if (UIButtonClear == null) {
			UIButtonClear = new UIButton();
//			UIButtonClear.setBounds(new Rectangle(432, 4, 75, 20));
			UIButtonClear.setPreferredSize(new Dimension(70, 20));
			UIButtonClear.setText("清  空");
//			UIButtonClear.setToolTipText("<HTML><B>清空本次核销数据</B></HTML>");
		}
		return UIButtonClear;
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
	private UIButton getUIButtonQry() {
		if (UIButtonQry == null) {
			UIButtonQry = new UIButton();
//			UIButtonQry.setBounds(new Rectangle(272, 4, 75, 20));
			UIButtonQry.setPreferredSize(new Dimension(70, 20));
			UIButtonQry.setText("查  询");
//			UIButtonQry.setToolTipText("<HTML>查询虚拟发票</HTML>");
		}
		return UIButtonQry;
	}
	
	/**
	 * This method initializes UIButtonCan
	 * 
	 * @return nc.ui.pub.beans.UIButton
	 */
	private UIButton getUIButtonQryLink() {
		if (UIButtonQry == null) {
			UIButtonQry = new UIButton();
//			UIButtonQry.setBounds(new Rectangle(272, 4, 75, 20));
			UIButtonQry.setPreferredSize(new Dimension(70, 20));
			UIButtonQry.setText("联查");
//			UIButtonQry.setToolTipText("<HTML>查询虚拟发票</HTML>");
		}
		return UIButtonQry;
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
			UIPanel.add(getUIButtonQry());			
			UIPanel.add(getUIButtonDistr());
			UIPanel.add(getUIButtonClear());
			UIPanel.add(getUIButtonSave());
			UIPanel.add(getUIButtonCan());
//			UIPanel.add(getUIButtonQryLink());
		}
		return UIPanel;
	}
	
	private BillCardPanel getVerifyCardPanel() {
		if (ijVerifyCardPanel == null) {
			ijVerifyCardPanel = new BillCardPanel();
			ijVerifyCardPanel.setSize(1024, 650);
			ijVerifyCardPanel.loadTemplet( "32H02" , null, ClientEnvironment.getInstance().getUser().getPrimaryKey(), ClientEnvironment.getInstance().getCorporation().getPk_corp());
			ijVerifyCardPanel.setTatolRowShow(true);
			ijVerifyCardPanel.setRowNOShow(true);
			ijVerifyCardPanel.setBodyMultiSelect(true);
			ijVerifyCardPanel.addBodyEditListener2(this);
			ijVerifyCardPanel.getBillTable().setRowSelectionAllowed(true);
			ijVerifyCardPanel.setBodyMultiSelect(true);
			ijVerifyCardPanel.addEditListener(this);
			ijVerifyCardPanel.addMouseListener(this);
			ijVerifyCardPanel.getBillModel().addRowStateChangeEventListener(this);
			ijVerifyCardPanel.getBodyItem("ndealnum").setDecimalDigits(4);
			ijVerifyCardPanel.getBodyItem("nnumber").setDecimalDigits(4);
			ijVerifyCardPanel.getBodyItem("nrewritenum").setDecimalDigits(4);
			ijVerifyCardPanel.getBodyItem("ntotaldealnum").setDecimalDigits(4);
			ijVerifyCardPanel.getBodyItem("nmny").setDecimalDigits(2);
			ijVerifyCardPanel.getBodyItem("ndealmny").setDecimalDigits(2);
			ijVerifyCardPanel.getBodyItem("nrewritemny").setDecimalDigits(2);
			ijVerifyCardPanel.getBodyItem("ntotaldealmny").setDecimalDigits(2);
			ijVerifyCardPanel.getHeadItem("nmnybal").setDecimalDigits(2);
			ijVerifyCardPanel.getHeadItem("nnumbal").setDecimalDigits(4);  

		}
		return ijVerifyCardPanel;
	}
	
	public void afterEdit(BillEditEvent e) {
		// TODO Auto-generated method stub
		int irow = e.getRow();
		if(e.getKey().equalsIgnoreCase("ndealnum")){
			UFDouble nNumber = getVerifyCardPanel().getBillModel().getValueAt(irow,"nnumber")==null?  UFDouble.ZERO_DBL : new UFDouble(getVerifyCardPanel().getBillModel().getValueAt(irow,"nnumber").toString());
			UFDouble nTotalDealNum = getVerifyCardPanel().getBillModel().getValueAt(irow,"ntotaldealnum")==null? UFDouble.ZERO_DBL : new UFDouble(getVerifyCardPanel().getBillModel().getValueAt(irow,"ntotaldealnum").toString());
			UFDouble ndealnum = e.getValue() == null?  UFDouble.ZERO_DBL: new UFDouble(e.getValue().toString());
			if (ndealnum.compareTo(nNumber.sub(nTotalDealNum)) > 0 ) {
				MessageDialog.showErrorDlg(this, "输入错误", "第 " + irow + " 行数据，本次核销数量不能大于发票行的未核销数量,请重新输入。");
				//getVerifyCardPanel().getBodyItem("").getComponent().requestFocus();
				getVerifyCardPanel().getBillModel().setValueAt(e.getOldValue(), irow, "ndealnum");
				return;
			}
			//getVerifyCardPanel().getBillModel().setRowState(irow, BillModel.SELECTED);
			//getVerifyCardPanel().getBillModel().updateValue();
			getVerifyCardPanel().getBillModel().setValueAt(e.getValue(), irow, "nrewritenum");
		}
		if(e.getKey().equalsIgnoreCase("nrewritenum")){
			UFDouble nNumber = getVerifyCardPanel().getBillModel().getValueAt(irow,"nnumber")==null?  UFDouble.ZERO_DBL : new UFDouble(getVerifyCardPanel().getBillModel().getValueAt(irow,"nnumber").toString());
			UFDouble nTotalDealNum = getVerifyCardPanel().getBillModel().getValueAt(irow,"ntotaldealnum")==null?  UFDouble.ZERO_DBL : new UFDouble(getVerifyCardPanel().getBillModel().getValueAt(irow,"ntotaldealnum").toString());
			UFDouble nrewrietnum = e.getValue() == null?  UFDouble.ZERO_DBL: new UFDouble(e.getValue().toString());
			if (nrewrietnum.compareTo(nNumber.sub(nTotalDealNum)) > 0 ) {
				MessageDialog.showErrorDlg(this, "输入错误", "第 " + irow + " 行数据，本次回写数量不能大于发票行的未核销数量,请重新输入。");
				getVerifyCardPanel().setBodyValueAt(e.getOldValue(), irow, "nrewritenum");
				return;
				//getVerifyCardPanel().getBodyItem("").getComponent().requestFocus();
			}
			//getVerifyCardPanel().getBillModel().setRowState(irow, BillModel.SELECTED);
			//getVerifyCardPanel().getBillModel().updateValue();
		}
		if(e.getKey().equalsIgnoreCase("ndealmny")){
			UFDouble nMny = getVerifyCardPanel().getBillModel().getValueAt(irow,"nmny")==null?  UFDouble.ZERO_DBL : new UFDouble(getVerifyCardPanel().getBillModel().getValueAt(irow,"nmny").toString());
			UFDouble nTotalDealMny = getVerifyCardPanel().getBillModel().getValueAt(irow,"ntotaldealmny")==null?  UFDouble.ZERO_DBL : new UFDouble(getVerifyCardPanel().getBillModel().getValueAt(irow,"ntotaldealmny").toString());
			UFDouble ndealmny = e.getValue() == null?  UFDouble.ZERO_DBL: new UFDouble(e.getValue().toString());
			if (ndealmny.compareTo(nMny.sub(nTotalDealMny)) > 0 ) {
				MessageDialog.showErrorDlg(this, "输入错误", "第 " + irow + " 行数据，本次核销金额不能大于发票行的未核销金额,请重新输入。");
				getVerifyCardPanel().setBodyValueAt(e.getOldValue(), irow, "ndealmny");
				return;
				//getVerifyCardPanel().getBodyItem("").getComponent().requestFocus();
			}
			//getVerifyCardPanel().getBillModel().setRowState(irow, BillModel.SELECTED);
			//getVerifyCardPanel().getBillModel().updateValue();
			getVerifyCardPanel().getBillModel().setValueAt(e.getValue(), irow, "nrewritemny");
		}
		if(e.getKey().equalsIgnoreCase("nrewritemny")){
			UFDouble nMny = getVerifyCardPanel().getBillModel().getValueAt(irow,"nmny")==null? UFDouble.ZERO_DBL : new UFDouble(getVerifyCardPanel().getBillModel().getValueAt(irow,"nmny").toString());
			UFDouble nTotalDealMny = getVerifyCardPanel().getBillModel().getValueAt(irow,"ntotaldealmny")==null?  UFDouble.ZERO_DBL : new UFDouble(getVerifyCardPanel().getBillModel().getValueAt(irow,"ntotaldealmny").toString());
			UFDouble nrewrietmny = e.getValue() == null?  UFDouble.ZERO_DBL: new UFDouble(e.getValue().toString());
			if (nrewrietmny.compareTo(nMny.sub(nTotalDealMny)) > 0 ) {
				MessageDialog.showErrorDlg(this, "输入错误", "第 " + irow + " 行数据，本次回写金额不能大于发票行的未核销数量,请重新输入。");
				getVerifyCardPanel().setBodyValueAt(e.getOldValue(), irow, "nrewritemny");
				//getVerifyCardPanel().getBodyItem("").getComponent().requestFocus();
			}
			//getVerifyCardPanel().getBillModel().setRowState(irow, BillModel.SELECTED);
			//getVerifyCardPanel().getBillModel().updateValue();
		}
		//checkValue();
		updateHeadData();
	}


	public void valueChanged(RowStateChangeEvent e) {   
		// TODO Auto-generated method stub
		int irow = e.getRow();
		UFDouble nNumber =  UFDouble.ZERO_DBL;
		UFDouble nTotalDealNum =  UFDouble.ZERO_DBL;
		UFDouble nMny =  UFDouble.ZERO_DBL;
		UFDouble nTotalDealMny =  UFDouble.ZERO_DBL;
		UFDouble ndealnum =  UFDouble.ZERO_DBL;
		UFDouble ndealmny =  UFDouble.ZERO_DBL;
		UFDouble nrewritenum =  UFDouble.ZERO_DBL;
		UFDouble nrewritemny =   UFDouble.ZERO_DBL;
		UFDouble nmnybal =  new UFDouble(getVerifyCardPanel().getHeadItem("nmnybal").getValue());
		UFDouble nnumbal = new UFDouble(getVerifyCardPanel().getHeadItem("nnumbal").getValue());

		if (e.isSelectState()) {
			nNumber = getVerifyCardPanel().getBillModel().getValueAt(irow,"nnumber")==null?  UFDouble.ZERO_DBL : new UFDouble(getVerifyCardPanel().getBillModel().getValueAt(irow,"nnumber").toString());
			nTotalDealNum = getVerifyCardPanel().getBillModel().getValueAt(irow,"ntotaldealnum")==null?  UFDouble.ZERO_DBL : new UFDouble(getVerifyCardPanel().getBillModel().getValueAt(irow,"ntotaldealnum").toString());
			nMny = getVerifyCardPanel().getBillModel().getValueAt(e.getRow(),"nmny")==null?  UFDouble.ZERO_DBL : new UFDouble(getVerifyCardPanel().getBillModel().getValueAt(e.getRow(),"nmny").toString());
			nTotalDealMny = getVerifyCardPanel().getBillModel().getValueAt(e.getRow(),"ntotaldealmny")==null?  UFDouble.ZERO_DBL : new UFDouble(getVerifyCardPanel().getBillModel().getValueAt(e.getRow(),"ntotaldealmny").toString());
			ndealnum = (nNumber.sub(nTotalDealNum)).compareTo(nnumbal) > 0 ? nnumbal: nNumber.sub(nTotalDealNum);  //取当前行与核销余额的最小值
			ndealmny = (nMny.sub(nTotalDealMny)).compareTo(nmnybal) > 0 ? nmnybal : nMny.sub(nTotalDealMny);
			nrewritenum = ndealnum;
			nrewritemny =  ndealmny;
		} else{
			ndealnum = UFDouble.ZERO_DBL;
			ndealmny = UFDouble.ZERO_DBL;
			nrewritenum = ndealnum;
			nrewritemny =  ndealmny;
		}

		getVerifyCardPanel().setBodyValueAt(ndealnum, irow, "ndealnum");
		getVerifyCardPanel().setBodyValueAt(ndealmny, irow, "ndealmny");
		getVerifyCardPanel().setBodyValueAt(nrewritenum, irow, "nrewritenum");
		getVerifyCardPanel().setBodyValueAt(nrewritemny, irow, "nrewritemny");
		
		if (!checkDealVO() && e.isSelectState()){
			MessageDialog.showWarningDlg(this, "警告", "选择本行后，累计的核销处理数量和金额超过了发票行的未核销余额，本行核销数据被清零，请手工调整！");
			ndealnum =  UFDouble.ZERO_DBL;
			ndealmny =  UFDouble.ZERO_DBL;
			nrewritenum = ndealnum;
			nrewritemny =  ndealmny;
			getVerifyCardPanel().setBodyValueAt(ndealnum, irow, "ndealnum");
			getVerifyCardPanel().setBodyValueAt(ndealmny, irow, "ndealmny");
			getVerifyCardPanel().setBodyValueAt(nrewritenum, irow, "nrewritenum");
			getVerifyCardPanel().setBodyValueAt(nrewritemny, irow, "nrewritemny");
		}
		updateHeadData();
	}
	
	private boolean checkDealVO() {
		// TODO Auto-generated method stub
		
		ArrayList<TaxInvoiceDealVO> alvos = getSelectedVOs();
		TaxInvoiceItemVO selvo = getTaxInvoiceItemVO();
		UFDouble nmnybal = selvo.getNsummny().sub(selvo.getNtotaldealmny()==null? UFDouble.ZERO_DBL:selvo.getNtotaldealmny()) ;  //得到发票行的数量和金额余额
		UFDouble nnumbal = selvo.getNnumber().sub(selvo.getNtotaldealnum()==null? UFDouble.ZERO_DBL:selvo.getNtotaldealnum());
//		UFDouble nmnybal = getVerifyCardPanel().getHeadItem("nmnybal").getValue()==null? UFDouble.ZERO_DBL: new UFDouble(getVerifyCardPanel().getHeadItem("nmnybal").getValue().toString());  //得到发票行的数量和金额余额
//		UFDouble nnumbal = getVerifyCardPanel().getHeadItem("nnumbal").getValue()==null? UFDouble.ZERO_DBL: new UFDouble(getVerifyCardPanel().getHeadItem("nnumbal").getValue().toString());

		UFDouble nmnydealsum = getDealMnySum(alvos);
		UFDouble nnumdealsum = getDealNumSum(alvos);
		
		if(nmnydealsum.compareTo(nmnybal) >0 || nnumdealsum.compareTo(nnumbal) >0 )  //核销的合计数量或金额超过了发票行的核销余额
			return false;
		
		return true;
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

	public void bodyRowChange(BillEditEvent e) {
		// TODO Auto-generated method stub
	
	}

	public boolean beforeEdit(BillEditEvent e) {
		// TODO Auto-generated method stub
		if (getVerifyCardPanel().getBillModel().getRowState(e.getRow()) == BillModel.SELECTED)
			return true;
		else
			return false;
	}
	
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if (e.getSource().equals(getUIButtonClear())) {
			onBtnClear();
		}
		if (e.getSource().equals(getUIButtonCan())) {
			onBtnCan();
		}
		if (e.getSource().equals(getUIButtonDistr())) {
			onBtnDistr();
		}
		if (e.getSource().equals(getUIButtonSave())) {
			onBtnSave();
		}
		if (e.getSource().equals(getUIButtonQry())) {
			onBtnQry();
		}
	}
	private void onBtnQry() {
		// TODO Auto-generated method stub
		HYQueryConditionDLG qryDLG = getIjSaleInvoiceQryDLG() ;
		if (qryDLG == null) {
			TemplateInfo tempinfo = new TemplateInfo();
			tempinfo.setPk_Org(ClientEnvironment.getInstance().getCorporation().getPk_corp());
			tempinfo.setCurrentCorpPk(ClientEnvironment.getInstance().getCorporation().getPk_corp());
			tempinfo.setFunNode("40060502");
			tempinfo.setUserid(ClientEnvironment.getInstance().getUser().getPrimaryKey());
			//tempinfo.setBusiType("");
			tempinfo.setNodekey("DEAL");
			qryDLG = new HYQueryConditionDLG(this, tempinfo);
			setIjSaleInvoiceQryDLG(qryDLG);
		}
		
		qryDLG.show();
		
		if(qryDLG.getResult() == UIDialog.ID_OK) {
			String sql = qryDLG.getWhereSQL();
			QryInvoiceData(sql);
		} //else{
		qryDLG.hide();
		//}
	}

	private ArrayList<TaxInvoiceDealVO> getSelectedVOs(){ 
		
		ArrayList<TaxInvoiceDealVO> alvos = new ArrayList();
		TaxInvoiceDealVO vo = null;
		for (int i=0 ; i<getVerifyCardPanel().getBillTable().getRowCount(); i++){
			if (getVerifyCardPanel().getBillModel().getRowState(i) == BillModel.SELECTED) {
				vo = (TaxInvoiceDealVO)getVerifyCardPanel().getBillModel().getBodyValueRowVO(i, TaxInvoiceDealVO.class.getName());
				if(vo.getNdealmny().compareTo(UFDouble.ZERO_DBL) != 0 ||     //2014-01-14 过滤掉全为0的行
						vo.getNdealnum().compareTo(UFDouble.ZERO_DBL) != 0 ||
								vo.getNrewritenum().compareTo(UFDouble.ZERO_DBL) != 0 ||
										vo.getNrewritenum().compareTo(UFDouble.ZERO_DBL) != 0) {
					alvos.add(vo);
				}
			}
		}
		
		return alvos;
	}
	

	private void onBtnSave() {
		// TODO Auto-generated method stub

		ArrayList<TaxInvoiceDealVO> alvos = getSelectedVOs();
		if (!checkDealVO()){
			MessageDialog.showErrorDlg(this, "错误", "本次发票核销的合计数量或金额超过了发票行的未核销余额，不能保存核销结果，请调整！");
			return;
		}
		UFDouble nmnybal = getVerifyCardPanel().getHeadItem("nmnybal").getValue()==null? UFDouble.ZERO_DBL: new UFDouble(getVerifyCardPanel().getHeadItem("nmnybal").getValue().toString());  //得到发票行的数量和金额余额
		UFDouble nnumbal = getVerifyCardPanel().getHeadItem("nnumbal").getValue()==null? UFDouble.ZERO_DBL: new UFDouble(getVerifyCardPanel().getHeadItem("nnumbal").getValue().toString());
		
//		if(nmnybal.compareTo(UFDouble.ZERO_DBL) != 0 || 
//				nnumbal.compareTo(UFDouble.ZERO_DBL) != 0	) {
//			if ( MessageDialog.showYesNoDlg(this, "提示", "当前发票行还有未核销完成的数量或金额余额，是否继续保存本次核销结果？") != MessageDialog.ID_YES)  //这里友好提示一下，选择是的话就继续保存
//				return;
//		}
		if(nmnybal.compareTo(UFDouble.ZERO_DBL) != 0 	) {
			if ( MessageDialog.showYesNoDlg(this, "提示", "当前发票行还有未核销完成的金额余额，是否继续保存本次核销结果？") != MessageDialog.ID_YES)  //这里友好提示一下，选择是的话就继续保存
				return;
		}
		//这里还不能作为一个事务进行处理，可能存在回写数据不完整情况
		saveDealData(alvos);
//		reWriteTaxInvoice(alvos);
//		reWriteSaleInvoice(alvos);
		
		getIjVerifyDialog().closeOK();
		
	}

	private void saveDealData(ArrayList<TaxInvoiceDealVO> alvos) {
		// TODO Auto-generated method stub
		if (alvos ==null || alvos.size() == 0)
			return;
		
		ArrayList<TaxInvoiceBbVO> albbvos = new ArrayList();
		for (int i= 0 ; i < alvos.size(); i++){

				TaxInvoiceBbVO bbvo = new TaxInvoiceBbVO();
				bbvo.setCdealoper(ClientEnvironment.getInstance().getUser().getPrimaryKey());
				bbvo.setCsourcebillid(alvos.get(i).getCsaleid());
				bbvo.setCsourcebilltype(SaleBillType.SaleInvoice);
				bbvo.setCsourcebillrowid(alvos.get(i).getCinvoice_bid());
				bbvo.setCtaxinvoiceid(alvos.get(i).getCtaxinvoiceid());
				bbvo.setCtaxinvoice_bid(alvos.get(i).getCtaxinvoice_bid());
				bbvo.setDdealdate(ClientEnvironment.getInstance().getDate());
				bbvo.setNdealmny(alvos.get(i).getNdealmny());
				bbvo.setNdealnum(alvos.get(i).getNdealnum());
				bbvo.setNwriteinvoicemny(alvos.get(i).getNrewritemny());
				bbvo.setNwriteinvoicenum(alvos.get(i).getNrewritenum());
				bbvo.setDr(0);
				bbvo.setStatus(VOStatus.NEW);
				albbvos.add(bbvo);

		}

//		IVOPersistence iVOPersistence = (IVOPersistence) NCLocator
//		.getInstance().lookup(IVOPersistence.class.getName());
//		
//		TaxInvoiceBbVO[] bbvos = new TaxInvoiceBbVO[albbvos.size()];
//		try {
//			iVOPersistence.insertVOArray(albbvos.toArray(bbvos));
//		} catch (BusinessException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		//2014-02-16 使用新的更新方法，放在一个事务中处理更新，保证数据一致性。
		if (albbvos.size() ==0) {
			return;
		}
		TaxInvoiceBbVO[] dealvos = new TaxInvoiceBbVO[albbvos.size()] ;
		
		ITaxInvoice isrv =(ITaxInvoice) NCLocator.getInstance().lookup(ITaxInvoice.class.getName());
		try {
			isrv.saveDeal(albbvos.toArray(dealvos));
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//2014-02-16 end
	}

	private void reWriteTaxInvoice(ArrayList<TaxInvoiceDealVO> alvos) {
		// TODO Auto-generated method stub
		if (alvos ==null || alvos.size() == 0)
			return;
		
		UFDouble ndealnum =  UFDouble.ZERO_DBL;
		UFDouble ndealmny =  UFDouble.ZERO_DBL;
		for (int i= 0 ; i < alvos.size(); i++){
			ndealnum = ndealnum.add(alvos.get(i).getNdealnum());
			ndealmny = ndealmny.add(alvos.get(i).getNdealmny());
		}
		getTaxInvoiceItemVO().setCtaxinvoiceid(getTaxInvoiceVO().getParentVO().getAttributeValue("ctaxinvoiceid").toString());
		getTaxInvoiceItemVO().setNtotaldealmny(getTaxInvoiceItemVO().getNtotaldealmny()==null?ndealmny: getTaxInvoiceItemVO().getNtotaldealmny().add(ndealmny));
		getTaxInvoiceItemVO().setNtotaldealnum(getTaxInvoiceItemVO().getNtotaldealnum()==null?ndealnum: getTaxInvoiceItemVO().getNtotaldealnum().add(ndealnum));
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

	private void reWriteSaleInvoice(ArrayList<TaxInvoiceDealVO> alvos) {
		// TODO Auto-generated method stub
		if (alvos == null || alvos.size() == 0)
			return;

		IVOPersistence iVOPersistence = (IVOPersistence) NCLocator
				.getInstance().lookup(IVOPersistence.class.getName());

		ArrayList<SaleInvoiceBVOForVerify> albvos = new ArrayList();
		for (int i = 0; i < alvos.size(); i++) {
			SaleInvoiceBVOForVerify vo = new SaleInvoiceBVOForVerify();
			vo.setCsaleid(alvos.get(i).getCsaleid());
			vo.setCinvoice_bid(alvos.get(i).getCinvoice_bid());
			vo.setNtotaldealnum(alvos.get(i).getNrewritenum().add(alvos.get(i).getNtotaldealnum()));  //2014-01-15 低级错误啊 wanglei
			vo.setNtotaldealmny(alvos.get(i).getNrewritemny().add(alvos.get(i).getNtotaldealmny()));
			albvos.add(vo);
		}
		try {
			SaleInvoiceBVOForVerify[] bvos = new SaleInvoiceBVOForVerify[alvos
					.size()];
			iVOPersistence.updateVOArray(albvos.toArray(bvos));
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}


	private void onBtnDistr() {
		// TODO Auto-generated method stub
		UFDouble nmnybal = getVerifyCardPanel().getHeadItem("nmnybal").getValue()==null? UFDouble.ZERO_DBL: new UFDouble(getVerifyCardPanel().getHeadItem("nmnybal").getValue().toString());  //得到发票行的数量和金额余额
		UFDouble nnumbal = getVerifyCardPanel().getHeadItem("nnumbal").getValue()==null? UFDouble.ZERO_DBL: new UFDouble(getVerifyCardPanel().getHeadItem("nnumbal").getValue().toString());

		if (nmnybal.equals(UFDouble.ZERO_DBL) && nnumbal.equals(UFDouble.ZERO_DBL)) {
			MessageDialog.showErrorDlg(this, "提示", "本次发票核销的核销余额已经为零，没有可以继续核销的数据！");
			return;
		}
		for (int i=0; i < getVerifyCardPanel().getBillTable().getRowCount(); i++) {
			getVerifyCardPanel().getBillModel().setRowState(i, BillModel.SELECTED);
			TaxInvoiceDealVO vo = (TaxInvoiceDealVO)getVerifyCardPanel().getBillModel().getBodyValueRowVO(i, TaxInvoiceDealVO.class.getName());
			if(vo.getNdealmny().compareTo(UFDouble.ZERO_DBL) == 0 ||     //2014-01-14 过滤掉全为0的行
					vo.getNdealnum().compareTo(UFDouble.ZERO_DBL) == 0 ||
							vo.getNrewritenum().compareTo(UFDouble.ZERO_DBL) == 0 ||
									vo.getNrewritenum().compareTo(UFDouble.ZERO_DBL) == 0) {
				getVerifyCardPanel().getBillModel().setRowState(i, BillModel.UNSTATE);
				getVerifyCardPanel().updateUI();
				return;
			}
		}
		
		
	}

	private void onBtnCan() {
		// TODO Auto-generated method stub 
		ijVerifyDialog.closeOK();
	}

	private void onBtnClear() {
		// TODO Auto-generated method stub
		for (int i=0; i < getVerifyCardPanel().getBillTable().getRowCount(); i++) {
			if (getVerifyCardPanel().getBillModel().getRowState(i) == BillModel.SELECTED) {
				getVerifyCardPanel().getBillModel().setValueAt(null, i, "ndealnum");
				getVerifyCardPanel().getBillModel().setValueAt(null, i, "ndealmny");
				getVerifyCardPanel().getBillModel().setValueAt(null, i, "nrewritenum");
				getVerifyCardPanel().getBillModel().setValueAt(null, i, "nrewritemny");	
				getVerifyCardPanel().getBillModel().setRowState(i, BillModel.UNSTATE);
			}
		}
		//getVerifyCardPanel().getBillModel().updateValue();
	}

	public TaxInvoiceVO getTaxInvoiceVO() {
		return taxInvoiceVO;
	}

	public void setTaxInvoiceVO(TaxInvoiceVO taxInvoiceVO) {
		this.taxInvoiceVO = taxInvoiceVO;
	}

	public VerifyDialog getIjVerifyDialog() {
		return ijVerifyDialog;
	}

	public void setIjVerifyDialog(VerifyDialog ijVerifyDialog) {
		this.ijVerifyDialog = ijVerifyDialog;
	}

	public TaxInvoiceItemVO getTaxInvoiceItemVO() {
		return taxInvoiceItemVO;
	}

	public void setTaxInvoiceItemVO(TaxInvoiceItemVO taxInvoiceItemVO) {
		this.taxInvoiceItemVO = taxInvoiceItemVO;
	}

	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		e.getClickCount();
		return;
	}

	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * @return the ijSaleInvoiceQryDLG
	 */
	public HYQueryConditionDLG getIjSaleInvoiceQryDLG() {
		return ijSaleInvoiceQryDLG;
	}

	/**
	 * @param ijSaleInvoiceQryDLG the ijSaleInvoiceQryDLG to set
	 */
	public void setIjSaleInvoiceQryDLG(HYQueryConditionDLG ijSaleInvoiceQryDLG) {
		this.ijSaleInvoiceQryDLG = ijSaleInvoiceQryDLG;
	}

}
