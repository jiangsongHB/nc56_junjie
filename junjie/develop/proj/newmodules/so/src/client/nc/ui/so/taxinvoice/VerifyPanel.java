package nc.ui.so.taxinvoice;

import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.ArrayList;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.sun.org.apache.bcel.internal.verifier.structurals.ExceptionHandlers;

import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.bs.scm.pub.smart.SmartDMO;
import nc.itf.scm.so.so002.ISaleinvoiceQuery;
import nc.itf.uap.IUAPQueryBS;
import nc.itf.uap.IVOPersistence;
import nc.jdbc.framework.mapping.IMappingMeta;
import nc.jdbc.framework.processor.ArrayListProcessor;
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
import nc.ui.pub.bill.BillTableSelectionChangeListener;
import nc.ui.pub.bill.BillTableSelectionEvent;
import nc.ui.pub.bill.BillTotalListener;
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
import nc.ui.trade.base.IBillConst;
import nc.ui.trade.business.HYPubBO_Client;
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
BillEditListener, BillEditListener2 {

	private nc.ui.pub.bill.BillCardPanel ivjPanel = null;
	private UIButton UIButtonDistr;
	private UIButton UIButtonClear;
	private UIButton UIButtonSave;
	private UIButton UIButtonCan;
	private UIPanel UIPanel;
	private BillCardPanel ijVerifyCardPanel;
	private TaxInvoiceVO taxInvoiceVO; 
	private TaxInvoiceItemVO taxInvoiceItemVO; 
	private VerifyDialog ijVerifyDialog;
	
	public VerifyPanel() {
		super();
		//initialize();
		//initData();
	}

	/**
	 * VerifyInTimePanel ������ע�⡣
	 * @param p0 java.awt.LayoutManager
	 */
	public VerifyPanel(java.awt.LayoutManager p0) {
		super(p0);
	}
	/**
	 * VerifyInTimePanel ������ע�⡣
	 * @param p0 java.awt.LayoutManager
	 * @param p1 boolean
	 */
	public VerifyPanel(java.awt.LayoutManager p0, boolean p1) {
		super(p0, p1);
	}
	/**
	 * VerifyInTimePanel ������ע�⡣
	 * @param p0 boolean
	 */
	public VerifyPanel(boolean p0) {
		super(p0);
	}
	
	public nc.ui.pub.bill.BillCardPanel getIvjPanel() {
		return ivjPanel;
	}

	public void setIvjPanel(nc.ui.pub.bill.BillCardPanel ivjDocPanel) {
		this.ivjPanel = ivjDocPanel;
	}
	/**
	 * ��ʼ���ࡣ
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
	}

	/**
	 * ���� DocPanel ����ֵ��
	 * @return nc.ui.pub.bill.BillCardPanel
	 */
	/* ���棺�˷������������ɡ� */
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

		QryInvoiceData();

	}

	private void QryInvoiceDataV1() {
		// TODO Auto-generated method stub
		getVerifyCardPanel().getHeadItem("dealmny");
		
		boolean isfee = false; 
		
		String cinvoicetype = (String)taxInvoiceVO.getParentVO().getAttributeValue("cinvoicetype");
		
		nc.itf.uif.pub.IUifService srv =(nc.itf.uif.pub.IUifService)NCLocator.getInstance().lookup(nc.itf.uif.pub.IUifService.class.getName());
		
		SaleinvoiceVO[] invvos = null;
		ArrayList<TaxInvoiceDealVO> alvos = new ArrayList();
		TaxInvoiceDealVO vo = new TaxInvoiceDealVO();
		try {
			TaxInvoiceTypeVO taxInvTypeVo = (TaxInvoiceTypeVO) srv.queryByPrimaryKey(TaxInvoiceTypeVO.class, cinvoicetype);
			
			isfee = taxInvTypeVo.getIffee().booleanValue();
			
			//��Ʊ��ͷ������״̬���ͻ�
			String strWhere = " so_saleinvoice.creceiptcorpid = '" + taxInvoiceVO.getParentVO().getAttributeValue("cordermanid") + "' " +
			" and ( so_saleinvoice.fstatus = " + BillStatus.AUDIT + " or so_saleinvoice.fstatus = " + BillStatus.FINISH + " ) " ;
			
			//strWhere += " and so_saleinvoice.dbilldate >= '2013-01-01' ";
			
			//��Ʊ�����������Ƿ�������
			strWhere += " and isnull(so_saleinvoice_b.dr,0) = 0 and (isnull(so_saleinvoice_b.ntotaldealnum,0) < so_saleinvoice_b.nnumber  " +   // ��Ʊ������
					"or isnull(so_saleinvoice_b.ntotaldealmny,0) < so_saleinvoice_b.nsummny ) ";
			
			//���÷�Ʊ����
			if (isfee){
				strWhere += " and exists (" +
				"select * from bd_invbasdoc where (isnull(discountflag,'N') = 'Y' or isnull(laborflag,'N') = 'Y') " +
				" and  pk_invbasdoc = so_saleinvoice_b.cinvbasdocid ) ";
			}else{
				strWhere += " and exists (" +
				"select * from bd_invbasdoc where (isnull(discountflag,'N') = 'N' and isnull(laborflag,'N') = 'N') " +
				" and  pk_invbasdoc = so_saleinvoice_b.cinvbasdocid ) ";
			}
			
			//Ԥ��Ʊ����������Ʊ����
			if (((UFBoolean)taxInvoiceVO.getParentVO().getAttributeValue("ispray")).booleanValue()){ //�����Ԥ��Ʊ����ѡ��ķ�Ʊ���ڷ�Χ��ʵ�ʷ�Ʊ������֮��
				strWhere += " and so_saleinvoice.dbilldate >= '" + taxInvoiceVO.getParentVO().getAttributeValue("dinvoicedate") + "' " ;
			}else{
				strWhere += " and so_saleinvoice.dbilldate <= '" + taxInvoiceVO.getParentVO().getAttributeValue("dinvoicedate") + "' " ;
			}
			
			ISaleinvoiceQuery query = NCLocator.getInstance().lookup(
					ISaleinvoiceQuery.class);
			invvos = query.queryBillDataByWhere(strWhere);
			
			if(invvos != null && invvos.length >0) {

				for (int i =0 ; i< invvos.length ; i ++ ) {

					SaleinvoiceBVO[] invbvo = invvos[i].getBodyVO();
					UFDouble ntotaldealmny = new UFDouble(0.0);
					UFDouble ntotaldealnum = new UFDouble(0.0);
					UFDouble nmny = new UFDouble(0.0);
					UFDouble nnum = new UFDouble(0.0);
					
					for (int j = 0 ; j < invbvo.length; j++){
						//??��֪��Ϊʲô���������ֶδ���
//						ntotaldealmny = invbvo[j].getAttributeValue("pk_corp")==null? new UFDouble(0.0) : new UFDouble(invbvo[j].getAttributeValue("pk_corp").toString());
//						ntotaldealnum = invbvo[j].getAttributeValue("scalefactor")==null? new UFDouble(0.0) : new UFDouble(invbvo[j].getAttributeValue("scalefactor").toString());
//						ntotaldealmny = invbvo[j].getAttributeValue("ntotaldealmny")==null? new UFDouble(0.0) : new UFDouble(invbvo[j].getAttributeValue("ntotaldealmny").toString());
//						ntotaldealnum = invbvo[j].getAttributeValue("ntotaldealnum")==null? new UFDouble(0.0) : new UFDouble(invbvo[j].getAttributeValue("ntotaldealnum").toString());
						
						FormulaParseFather f = new nc.ui.pub.formulaparse.FormulaParse();
						//
						String[] formulas = new String[]{
	                       "ntotaldealmny->getColValue(\"so_saleinvoice_b\", \"ntotaldealmny\", \"cinvoice_bid\", cinvoice_bid)",
	                       "ntotaldealnum->getColValue(\"so_saleinvoice_b\", \"ntotaldealnum\", \"cinvoice_bid\", cinvoice_bid)"
	                       };
						f.addVariable("cinvoice_bid", invbvo[j].getCsale_bid());

						f.setExpressArray(formulas);
						String[][] vOs = f.getValueSArray();
						
						ntotaldealmny = new UFDouble(vOs[0][0]==null? "0.0": vOs[0][0]);
						ntotaldealnum = new UFDouble(vOs[1][0]==null? "0.0": vOs[0][0]);
											
						nmny = invbvo[j].getAttributeValue("nmny")==null? new UFDouble(0.0) : new UFDouble(invbvo[j].getAttributeValue("nmny").toString());
						nnum = invbvo[j].getAttributeValue("nnumber")==null? new UFDouble(0.0) : new UFDouble(invbvo[j].getAttributeValue("nnumber").toString());
						InvbasdocVO invinfovo = (InvbasdocVO) srv.queryByPrimaryKey(InvbasdocVO.class, invbvo[j].getCinvbasdocid().toString());
						if ((ntotaldealmny.compareTo(nmny) != 0 || ntotaldealnum.compareTo(nnum) !=0 ) &&
								(( isfee && (invinfovo.laborflag.booleanValue() || invinfovo.discountflag.booleanValue())) ||
								( !isfee && (!invinfovo.laborflag.booleanValue() && !invinfovo.discountflag.booleanValue()) )
								)) {
							vo = new TaxInvoiceDealVO();
							vo.setDbilldate(invvos[i].getHeadVO().getDbilldate()) ;
							vo.setCsaleid(invvos[i].getHeadVO().getCsaleid());
							vo.setPk_corp(invvos[i].getHeadVO().getPk_corp());
							vo.setVreceiptcode(invvos[i].getHeadVO().getVreceiptcode());
							
							vo.setCinvoice_bid(invbvo[j].getCinvoice_bid());
							vo.setNmny(nmny);
							vo.setNnumber(nnum);
							vo.setCinvbasdocid(invbvo[j].getCinvbasdocid());
							vo.setNtotaldealmny(ntotaldealmny);
							vo.setNtotaldealnum(ntotaldealnum);
							vo.setCrowno(invbvo[j].getCrowno());
							vo.setCtaxinvoiceid(taxInvoiceVO.getParentVO().getAttributeValue("ctaxinvoiceid").toString());
							vo.setCtaxinvoice_bid(taxInvoiceItemVO.getCtaxinvoice_bid());
							alvos.add(vo);
						}
					}
				}
				TaxInvoiceDealVO[] vos = new TaxInvoiceDealVO[alvos.size()];
				alvos.toArray(vos);
				getVerifyCardPanel().getBillModel().setBodyDataVO(vos);
				getVerifyCardPanel().getBillModel().execLoadFormula();// ��ʾ��ʽ
				//getVerifyCardPanel().startRowCardEdit();
			}
			
		}catch (BusinessException e) {
			e.printStackTrace();
			MessageDialog
					.showWarningDlg(this, "��ʾ", "���ݳ�ʼ������" + e.getMessage());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private void QryInvoiceData() {
		// TODO Auto-generated method stub
		getVerifyCardPanel().getHeadItem("dealmny");
		
		boolean isfee = false; 
		
		String cinvoicetype = (String)taxInvoiceVO.getParentVO().getAttributeValue("cinvoicetype");
		
		nc.itf.uif.pub.IUifService srv =(nc.itf.uif.pub.IUifService)NCLocator.getInstance().lookup(nc.itf.uif.pub.IUifService.class.getName());
		
		ArrayList<TaxInvoiceDealVO> alvos = new ArrayList();
		TaxInvoiceDealVO vo = new TaxInvoiceDealVO();
		try {
			TaxInvoiceTypeVO taxInvTypeVo = (TaxInvoiceTypeVO) srv.queryByPrimaryKey(TaxInvoiceTypeVO.class, cinvoicetype);
			
			isfee = taxInvTypeVo.getIffee().booleanValue();
			
			String sql = "select so_saleinvoice.dbilldate, so_saleinvoice.csaleid, so_saleinvoice.pk_corp,  " +
					"so_saleinvoice.vreceiptcode, so_saleinvoice_b.cinvoice_bid, so_saleinvoice_b.nmny ,  " +
					"so_saleinvoice_b.nnumber,  so_saleinvoice_b.cinvbasdocid, isnull(so_saleinvoice_b.ntotaldealnum,0),  " +
					"isnull(so_saleinvoice_b.ntotaldealmny,0), so_saleinvoice_b.crowno " +
					"from so_saleinvoice inner join so_saleinvoice_b on so_saleinvoice.csaleid = so_saleinvoice_b.csaleid " +
					"where so_saleinvoice_b.dr = 0 and so_saleinvoice.dr = 0 ";
			
			//��Ʊ��ͷ������״̬���ͻ�
			String strWhere = " and  so_saleinvoice.creceiptcorpid = '" + taxInvoiceVO.getParentVO().getAttributeValue("cordermanid") + "' " +
			" and ( so_saleinvoice.fstatus = " + BillStatus.AUDIT + " or so_saleinvoice.fstatus = " + BillStatus.FINISH + " ) " ;
			
			//strWhere += " and so_saleinvoice.dbilldate >= '2013-01-01' ";
			
			//��Ʊ�����������Ƿ�������
			strWhere += " and isnull(so_saleinvoice_b.dr,0) = 0 and (isnull(so_saleinvoice_b.ntotaldealnum,0) < so_saleinvoice_b.nnumber  " +   // ��Ʊ������
					"or isnull(so_saleinvoice_b.ntotaldealmny,0) < so_saleinvoice_b.nsummny ) ";
			
			//���÷�Ʊ����
			if (isfee){
				strWhere += " and exists (" +
				"select * from bd_invbasdoc where (isnull(discountflag,'N') = 'Y' or isnull(laborflag,'N') = 'Y') " +
				" and  pk_invbasdoc = so_saleinvoice_b.cinvbasdocid ) ";
			}else{
				strWhere += " and exists (" +
				"select * from bd_invbasdoc where (isnull(discountflag,'N') = 'N' and isnull(laborflag,'N') = 'N') " +
				" and  pk_invbasdoc = so_saleinvoice_b.cinvbasdocid ) ";
			}
			
			//Ԥ��Ʊ����������Ʊ����
			if (((UFBoolean)taxInvoiceVO.getParentVO().getAttributeValue("ispray")).booleanValue()){ //�����Ԥ��Ʊ����ѡ��ķ�Ʊ���ڷ�Χ��ʵ�ʷ�Ʊ������֮��
				strWhere += " and so_saleinvoice.dbilldate >= '" + taxInvoiceVO.getParentVO().getAttributeValue("dinvoicedate") + "' " ;
			}else{
				strWhere += " and so_saleinvoice.dbilldate <= '" + taxInvoiceVO.getParentVO().getAttributeValue("dinvoicedate") + "' " ;
			}
			
			String strOrder = " order by so_saleinvoice.dbilldate , so_saleinvoice.vreceiptcode, so_saleinvoice_b.crowno ";
			
			sql = sql + strWhere + strOrder;
			
			IUAPQueryBS iUAPQueryBS = (IUAPQueryBS) NCLocator.getInstance().lookup(
					IUAPQueryBS.class.getName());
			
			ArrayList reslist= (ArrayList)iUAPQueryBS.executeQuery( sql , new ArrayListProcessor());  //��߲�ѯЧ�ʣ����ô˷�����
			
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
			getVerifyCardPanel().getBillModel().setBodyDataVO(vos);
			getVerifyCardPanel().getBillModel().execLoadFormula();// ��ʾ��ʽ
			//getVerifyCardPanel().startRowCardEdit();
			
		}catch (BusinessException e) {
			e.printStackTrace();
			MessageDialog
					.showWarningDlg(this, "��ʾ", "���ݳ�ʼ������" + e.getMessage());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * ��ʼ��ϵͳ��Ϣ��
	 * �������ڣ�(2002-4-3 14:58:25)
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
			UIButtonDistr.setBounds(new Rectangle(352, 4, 75, 20));
			UIButtonDistr.setText("��  ��");
			UIButtonDistr.setToolTipText("<HTML><B>�Զ��������������</B></HTML>");
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
			UIButtonClear.setBounds(new Rectangle(432, 4, 75, 20));
			UIButtonClear.setText("��  ��");
			UIButtonClear.setToolTipText("<HTML><B>��ձ��κ�������</B></HTML>");
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
			UIButtonSave.setBounds(new Rectangle(512, 4, 75, 20));
			UIButtonSave.setText("��  ��");
			UIButtonSave.setToolTipText("<HTML><B>����������</B></HTML>");
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
			UIButtonCan.setText("��  ��");
			UIButtonCan.setToolTipText("<HTML><B>�رպ�������(CTRL + X)</B></HTML>");
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
			UIPanel.add(getUIButtonDistr(), null);
			UIPanel.add(getUIButtonClear(), null);
			UIPanel.add(getUIButtonSave(), null);
			UIPanel.add(getUIButtonCan(), null);
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
			ijVerifyCardPanel.getBillModel().addRowStateChangeEventListener(this);
			ijVerifyCardPanel.getBodyItem("ndealnum").setDecimalDigits(4);
			ijVerifyCardPanel.getBodyItem("nnumber").setDecimalDigits(4);
			ijVerifyCardPanel.getBodyItem("nrewritenum").setDecimalDigits(4);
			ijVerifyCardPanel.getBodyItem("ntotaldealnum").setDecimalDigits(4);
			ijVerifyCardPanel.getBodyItem("nmny").setDecimalDigits(2);
			ijVerifyCardPanel.getBodyItem("ndealmny").setDecimalDigits(2);
			ijVerifyCardPanel.getBodyItem("nrewritemny").setDecimalDigits(2);
			ijVerifyCardPanel.getBodyItem("ntotaldealmny").setDecimalDigits(2);

		}
		return ijVerifyCardPanel;
	}
	
	public void afterEdit(BillEditEvent e) {
		// TODO Auto-generated method stub
		int irow = e.getRow();
		if(e.getKey().equalsIgnoreCase("ndealnum")){
			UFDouble nNumber = getVerifyCardPanel().getBillModel().getValueAt(irow,"nnumber")==null? new UFDouble(0.0) : new UFDouble(getVerifyCardPanel().getBillModel().getValueAt(irow,"nnumber").toString());
			UFDouble nTotalDealNum = getVerifyCardPanel().getBillModel().getValueAt(irow,"ntotaldealnum")==null? new UFDouble(0.0) : new UFDouble(getVerifyCardPanel().getBillModel().getValueAt(irow,"ntotaldealnum").toString());
			UFDouble ndealnum = e.getValue() == null? new UFDouble(0.0): new UFDouble(e.getValue().toString());
			if (ndealnum.compareTo(nNumber.sub(nTotalDealNum)) > 0 ) {
				MessageDialog.showErrorDlg(this, "�������", "�� " + irow + " �����ݣ����κ����������ܴ��ڷ�Ʊ�е�δ��������,���������롣");
				getVerifyCardPanel().setBodyValueAt(e.getOldValue(), irow, "ndealnum");
				return;
			}
			getVerifyCardPanel().getBillModel().setRowState(irow, BillModel.SELECTED);
			getVerifyCardPanel().getBillModel().updateValue();
			getVerifyCardPanel().getBillModel().setValueAt(e.getValue(), irow, "nrewritenum");
		}
		if(e.getKey().equalsIgnoreCase("nrewritenum")){
			UFDouble nNumber = getVerifyCardPanel().getBillModel().getValueAt(irow,"nnumber")==null? new UFDouble(0.0) : new UFDouble(getVerifyCardPanel().getBillModel().getValueAt(irow,"nnumber").toString());
			UFDouble nTotalDealNum = getVerifyCardPanel().getBillModel().getValueAt(irow,"ntotaldealnum")==null? new UFDouble(0.0) : new UFDouble(getVerifyCardPanel().getBillModel().getValueAt(irow,"ntotaldealnum").toString());
			UFDouble nrewrietnum = e.getValue() == null? new UFDouble(0.0): new UFDouble(e.getValue().toString());
			if (nrewrietnum.compareTo(nNumber.sub(nTotalDealNum)) > 0 ) {
				MessageDialog.showErrorDlg(this, "�������", "�� " + irow + " �����ݣ����λ�д�������ܴ��ڷ�Ʊ�е�δ��������,���������롣");
				getVerifyCardPanel().setBodyValueAt(e.getOldValue(), irow, "nrewritenum");
			}
			getVerifyCardPanel().getBillModel().setRowState(irow, BillModel.SELECTED);
			getVerifyCardPanel().getBillModel().updateValue();
		}
		if(e.getKey().equalsIgnoreCase("ndealmny")){
			UFDouble nMny = getVerifyCardPanel().getBillModel().getValueAt(irow,"nmny")==null? new UFDouble(0.0) : new UFDouble(getVerifyCardPanel().getBillModel().getValueAt(irow,"nmny").toString());
			UFDouble nTotalDealMny = getVerifyCardPanel().getBillModel().getValueAt(irow,"ntotaldealmny")==null? new UFDouble(0.0) : new UFDouble(getVerifyCardPanel().getBillModel().getValueAt(irow,"ntotaldealmny").toString());
			UFDouble ndealnum = e.getValue() == null? new UFDouble(0.0): new UFDouble(e.getValue().toString());
			if (ndealnum.compareTo(nMny.sub(nTotalDealMny)) > 0 ) {
				MessageDialog.showErrorDlg(this, "�������", "�� " + irow + " �����ݣ����κ������ܴ��ڷ�Ʊ�е�δ�������,���������롣");
				getVerifyCardPanel().setBodyValueAt(e.getOldValue(), irow, "ndealmny");
			}
			getVerifyCardPanel().getBillModel().setRowState(irow, BillModel.SELECTED);
			getVerifyCardPanel().getBillModel().updateValue();
			getVerifyCardPanel().getBillModel().setValueAt(e.getValue(), irow, "nrewritemny");
		}
		if(e.getKey().equalsIgnoreCase("nrewritemny")){
			UFDouble nNumber = getVerifyCardPanel().getBillModel().getValueAt(irow,"nnumber")==null? new UFDouble(0.0) : new UFDouble(getVerifyCardPanel().getBillModel().getValueAt(irow,"nnumber").toString());
			UFDouble nTotalDealNum = getVerifyCardPanel().getBillModel().getValueAt(irow,"ntotaldealnum")==null? new UFDouble(0.0) : new UFDouble(getVerifyCardPanel().getBillModel().getValueAt(irow,"ntotaldealnum").toString());
			UFDouble nrewrietmny = e.getValue() == null? new UFDouble(0.0): new UFDouble(e.getValue().toString());
			if (nrewrietmny.compareTo(nNumber.sub(nTotalDealNum)) > 0 ) {
				MessageDialog.showErrorDlg(this, "�������", "�� " + irow + " �����ݣ����λ�д���ܴ��ڷ�Ʊ�е�δ��������,���������롣");
				getVerifyCardPanel().setBodyValueAt(e.getOldValue(), irow, "nrewritemny");
			}
			getVerifyCardPanel().getBillModel().setRowState(irow, BillModel.SELECTED);
			getVerifyCardPanel().getBillModel().updateValue();
		}
		//checkValue();
	}


	public void valueChanged(RowStateChangeEvent e) {   
		// TODO Auto-generated method stub
		int irow = e.getRow();
		UFDouble nNumber = new UFDouble(0.0);
		UFDouble nTotalDealNum = new UFDouble(0.0);
		UFDouble nMny = new UFDouble(0.0);
		UFDouble nTotalDealMny = new UFDouble(0.0);
		UFDouble ndealnum = new UFDouble(0.0);
		UFDouble ndealmny = new UFDouble(0.0);
		UFDouble nrewrietnum = new UFDouble(0.0);
		UFDouble nrewrietmny =  new UFDouble(0.0);
		if (e.isSelectState()) {
			nNumber = getVerifyCardPanel().getBillModel().getValueAt(irow,"nnumber")==null? new UFDouble(0.0) : new UFDouble(getVerifyCardPanel().getBillModel().getValueAt(irow,"nnumber").toString());
			nTotalDealNum = getVerifyCardPanel().getBillModel().getValueAt(irow,"ntotaldealnum")==null? new UFDouble(0.0) : new UFDouble(getVerifyCardPanel().getBillModel().getValueAt(irow,"ntotaldealnum").toString());
			nMny = getVerifyCardPanel().getBillModel().getValueAt(e.getRow(),"nmny")==null? new UFDouble(0.0) : new UFDouble(getVerifyCardPanel().getBillModel().getValueAt(e.getRow(),"nmny").toString());
			nTotalDealMny = getVerifyCardPanel().getBillModel().getValueAt(e.getRow(),"ntotaldealmny")==null? new UFDouble(0.0) : new UFDouble(getVerifyCardPanel().getBillModel().getValueAt(e.getRow(),"ntotaldealmny").toString());
			ndealnum = nNumber.sub(nTotalDealNum);
			ndealmny = nMny.sub(nTotalDealMny);
			nrewrietnum = ndealnum;
			nrewrietmny =  ndealmny;
		} else{
			ndealnum = new UFDouble(0.0);
			ndealmny = new UFDouble(0.0);
			nrewrietnum = ndealnum;
			nrewrietmny =  ndealmny;
		}
		getVerifyCardPanel().setBodyValueAt(ndealnum, irow, "ndealnum");
		getVerifyCardPanel().setBodyValueAt(ndealmny, irow, "ndealmny");
		getVerifyCardPanel().setBodyValueAt(nrewrietnum, irow, "nrewritenum");
		getVerifyCardPanel().setBodyValueAt(nrewrietmny, irow, "nrewritemny");
		
	}
	
	private void checkValue() {
		// TODO Auto-generated method stub
		
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
	}

	private void onBtnSave() {
		// TODO Auto-generated method stub
		ArrayList<TaxInvoiceDealVO> alvos = new ArrayList();
		TaxInvoiceDealVO vo = null;
		for (int i=0 ; i<getVerifyCardPanel().getBillTable().getRowCount(); i++){
			if (getVerifyCardPanel().getBillModel().getRowState(i) == BillModel.SELECTED) {
				vo = (TaxInvoiceDealVO)getVerifyCardPanel().getBillModel().getBodyValueRowVO(i, TaxInvoiceDealVO.class.getName());
				alvos.add(vo);
			}
		}
		
		if (alvos ==null || alvos.size() == 0 )
			return;
		
		saveDealData(alvos);
		reWriteTaxInvoice(alvos);
		reWriteSaleInvoice(alvos);
		
		ijVerifyDialog.closeOK();
		
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

		IVOPersistence iVOPersistence = (IVOPersistence) NCLocator
		.getInstance().lookup(IVOPersistence.class.getName());
		
		TaxInvoiceBbVO[] bbvos = new TaxInvoiceBbVO[albbvos.size()];
		try {
			iVOPersistence.insertVOArray(albbvos.toArray(bbvos));
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void reWriteTaxInvoice(ArrayList<TaxInvoiceDealVO> alvos) {
		// TODO Auto-generated method stub
		if (alvos ==null || alvos.size() == 0)
			return;
		
		UFDouble ndealnum = new UFDouble(0.0);
		UFDouble ndealmny = new UFDouble(0.0);
		for (int i= 0 ; i < alvos.size(); i++){
			ndealnum = ndealnum.add(alvos.get(i).getNdealnum());
			ndealmny = ndealmny.add(alvos.get(i).getNdealmny());
		}
		taxInvoiceItemVO.setCtaxinvoiceid(taxInvoiceVO.getParentVO().getAttributeValue("ctaxinvoiceid").toString());
		taxInvoiceItemVO.setNtotaldealmny(taxInvoiceItemVO.getNtotaldealmny()==null?ndealmny: taxInvoiceItemVO.getNtotaldealmny().add(ndealmny));
		taxInvoiceItemVO.setNtotaldealnum(taxInvoiceItemVO.getNtotaldealnum()==null?ndealnum: taxInvoiceItemVO.getNtotaldealnum().add(ndealnum));
		taxInvoiceItemVO.setStatus(VOStatus.UPDATED);
		
		IVOPersistence iVOPersistence = (IVOPersistence) NCLocator
		.getInstance().lookup(IVOPersistence.class.getName());
		
		try {
			iVOPersistence.updateVO(taxInvoiceItemVO);
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
			vo.setNtotaldealnum(alvos.get(i).getNrewritenum());
			vo.setNtotaldealmny(alvos.get(i).getNrewritemny());
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
		
	}

	private void onBtnCan() {
		// TODO Auto-generated method stub 
		ijVerifyDialog.closeOK();
	}

	private void onBtnClear() {
		// TODO Auto-generated method stub
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




}
