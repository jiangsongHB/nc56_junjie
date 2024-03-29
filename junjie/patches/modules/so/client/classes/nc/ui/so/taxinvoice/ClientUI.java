package nc.ui.so.taxinvoice;

import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.bs.logging.Logger;
import nc.ui.trade.manage.ManageEventHandler;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.ClientEnvironment;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.formulaset.FormulaParseFather;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scmpub.ITaxInvoiceApproveType;
import nc.vo.so.TaxInvoiceHeaderVO;
import nc.vo.so.TaxInvoiceItemVO;
import nc.vo.so.TaxInvoiceVO;
import nc.vo.trade.field.IBillField;
import nc.vo.trade.pub.IBillStatus;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillModelCellEditableController;
import nc.ui.pub.bill.BillMouseEnent;
import nc.ui.pub.bill.BillScrollPane;
import nc.ui.pub.bill.BillTotalListener;
 
  
import nc.ui.trade.base.IBillOperate;
import nc.ui.trade.bocommand.IUserDefButtonCommand;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.linkoperate.ILinkMaintain;
import nc.ui.pub.linkoperate.ILinkQuery;
import nc.ui.pub.linkoperate.ILinkQueryData;
import nc.ui.querytemplate.IBillReferQuery;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;  

import com.sun.tools.javac.code.Attribute.Array;

import nc.ui.so.taxinvoice.command.btRevDealBoCommand;
import nc.ui.so.taxinvoice.command.btDealBoCommand;
import nc.ui.so.taxinvoice.command.btDealGpBoCommand;

/**
 * <b> 在此处简要描述此类的功能 </b>
 *
 * <p>
 *     在此处添加此类的描述信息
 * </p>
 *
 *
 * @author author
 * @version tempProject version
 */
 public class ClientUI extends AbstractClientUI implements BillTotalListener, ILinkQuery, ILinkMaintain {
	 
	private List<String> pricefld = Arrays.asList( new String[]{"nprice","ntaxprice","ncurprice","ncurtaxprice"});
	private List<String> mnyfld = Arrays.asList(new String[]{"nmny","ntaxman","nsummny","ncurmny","ncurtaxmny","ncursummny","ntotaldealmny"});
	private List<String> numfld = Arrays.asList( new String[]{"nnumber","ntotaldealnum"});
       
	protected ManageEventHandler createEventHandler() {
		return new MyEventHandler(this, getUIControl());
	}
       
	public void setBodySpecialData(CircularlyAccessibleValueObject[] vos)
			throws Exception {
	}

	protected void setHeadSpecialData(CircularlyAccessibleValueObject vo,
			int intRow) throws Exception {}

	protected void setTotalHeadSpecialData(CircularlyAccessibleValueObject[] vos)
			throws Exception {	}

	protected void initSelfData() {	
		//getBillCardPanel().getBillModel().addTotalListener(this);
		setDigits();
	}
	/**
	 * 返回 VerifyPanel 特性值。
	 *
	 * @return javax.swing.JPanel
	 */
	/* 警告：此方法将重新生成。 */
	public void showVerifyPanel() {
		try {
			
			if(getBillCardPanel().getBillTable().getSelectedRow() == -1 ) {
				MessageDialog.showErrorDlg(this,"错误", "核销处理需要选择表体行，请选择发票行！");
				return;
			}
			
			TaxInvoiceItemVO selvo = (TaxInvoiceItemVO) getBillCardPanel().getBillModel().getBodyValueRowVO(
					getBillCardPanel().getBillTable().getSelectedRow(),
					TaxInvoiceItemVO.class.getName());
			
			UFDouble ntotaldealmny =  selvo.getNtotaldealmny()==null? UFDouble.ZERO_DBL:selvo.getNtotaldealmny() ;
			UFDouble nmny = selvo.getNsummny();
			UFDouble ntotaldealnum = selvo.getNtotaldealnum()==null? UFDouble.ZERO_DBL:selvo.getNtotaldealnum();
			UFDouble nnumber = selvo.getNnumber();
			
			if (ntotaldealmny.compareTo(nmny) >= 0 && ntotaldealnum.compareTo(nnumber) >=0	){
				MessageDialog.showErrorDlg(this,"错误", "本行发票已经核销完成，不能再进行核销！");
				return;
			}
			
			VerifyDialog dlg = new VerifyDialog((TaxInvoiceVO)this.getVOFromUI(), 
					(TaxInvoiceItemVO) getBillCardPanel().getBillModel().getBodyValueRowVO(
							getBillCardPanel().getBillTable().getSelectedRow(),
							TaxInvoiceItemVO.class.getName()) );
			dlg.showModal();
			
		} catch (Exception e) {
			Logger.error(e.getMessage());
		}
	}

	public void showVerifyQryPanel() {
		// TODO Auto-generated method stub
		try {
			
			if(getBillCardPanel().getBillTable().getSelectedRow() == -1 ) {
				MessageDialog.showErrorDlg(this,"错误", "核销处理需要选择表体行，请选择发票行！");
				return;
			}
			
			VerifyQryDialog dlg = new VerifyQryDialog((TaxInvoiceVO)this.getVOFromUI(), 
					(TaxInvoiceItemVO) getBillCardPanel().getBillModel().getBodyValueRowVO(
							getBillCardPanel().getBillTable().getSelectedRow(),
							TaxInvoiceItemVO.class.getName()));
			dlg.showModal();
			
		} catch (Exception e) {
			Logger.error(e.getMessage());
		}
	}
	 @Override
	public void afterEdit(BillEditEvent e) {
		// TODO Auto-generated method stub
		//super.afterEdit(e);
		 
		if(pricefld.contains(e.getKey()) || mnyfld.contains(e.getKey())  || numfld.contains(e.getKey()) ||  e.getKey().equalsIgnoreCase("ntaxrate"))
		{
			 recalculatebody(e.getRow(),e.getKey());
		}
		 
		if (e.getKey().equalsIgnoreCase("cinvoicemamid")){
			String cinvoicemamid = ((UIRefPane)getBillCardPanel().getHeadItem("cinvoicemamid").getComponent()).getRefPK();
			((UIRefPane)getBillCardPanel().getHeadItem("cordermanid").getComponent()).setPK(cinvoicemamid) ;
			//((UIRefPane)getBillCardPanel().getHeadItem("cservicemanid").getComponent()).setPK(cinvoicemamid) ; //取消这里默认设置
		}
		
	}

	private void recalculatebody(int irow,String key) {
		// TODO Auto-generated method stub
		UFDouble nzero = new UFDouble(0.0);
		UFDouble ntaxrate =  getBillCardPanel().getBodyValueAt(irow, "ntaxrate") == null ? new UFDouble(0.0) : new UFDouble( getBillCardPanel().getBodyValueAt(irow, "ntaxrate").toString());
		UFDouble number =  getBillCardPanel().getBodyValueAt(irow, "nnumber") == null ? new UFDouble(0.0) : new UFDouble(getBillCardPanel().getBodyValueAt(irow, "nnumber").toString());
		UFDouble ntaxprice = getBillCardPanel().getBodyValueAt(irow, "ntaxprice") == null ? new UFDouble(0.0) : new UFDouble(getBillCardPanel().getBodyValueAt(irow, "ntaxprice").toString());
		UFDouble nprice = getBillCardPanel().getBodyValueAt(irow, "nprice") == null ? new UFDouble(0.0) : new UFDouble(getBillCardPanel().getBodyValueAt(irow, "nprice").toString());
		UFDouble nroe = getBillCardPanel().getBodyValueAt(irow, "nroe") == null ? new UFDouble(1.0) : new UFDouble( getBillCardPanel().getBodyValueAt(irow, "nroe").toString());
		UFDouble ncurprice = getBillCardPanel().getBodyValueAt(irow, "ncurprice") == null ? new UFDouble(0.0) : new UFDouble(getBillCardPanel().getBodyValueAt(irow, "ncurprice").toString());
		UFDouble ncurtaxprice = getBillCardPanel().getBodyValueAt(irow, "ncurtaxprice") == null ? new UFDouble(0.0) : new UFDouble(getBillCardPanel().getBodyValueAt(irow, "ncurtaxprice").toString());
		UFDouble nmny = getBillCardPanel().getBodyValueAt(irow, "nmny") == null ? new UFDouble(0.0) : new UFDouble(getBillCardPanel().getBodyValueAt(irow, "nmny").toString());
		UFDouble ntaxman = getBillCardPanel().getBodyValueAt(irow, "ntaxman") == null ? new UFDouble(0.0) : new UFDouble(getBillCardPanel().getBodyValueAt(irow, "ntaxman").toString());
		UFDouble nsummny = getBillCardPanel().getBodyValueAt(irow, "nsummny") == null ? new UFDouble(0.0) : new UFDouble(getBillCardPanel().getBodyValueAt(irow, "nsummny").toString());
		UFDouble ncurtaxmny = getBillCardPanel().getBodyValueAt(irow, "ncurtaxmny") == null ? new UFDouble(0.0) : new UFDouble(getBillCardPanel().getBodyValueAt(irow, "ncurtaxmny").toString());
		UFDouble ncursummny = getBillCardPanel().getBodyValueAt(irow, "ncursummny") == null ? new UFDouble(0.0) : new UFDouble(getBillCardPanel().getBodyValueAt(irow, "ncursummny").toString());
		UFDouble ncurmny = getBillCardPanel().getBodyValueAt(irow, "ncurmny") == null ? new UFDouble(0.0) : new UFDouble(getBillCardPanel().getBodyValueAt(irow, "ncurmny").toString());
		
		if (pricefld.contains(key) || numfld.contains(key) || key.equalsIgnoreCase("ntaxrate")){
			if (key.equalsIgnoreCase("nprice")){
				ntaxprice =  nprice.compareTo(nzero)==0 ? new UFDouble(0.0) : 
					ntaxrate.compareTo(nzero) == 0 ? nprice: nprice.multiply((ntaxrate.div(100)).add(1),UFDouble.ROUND_HALF_UP);

			}
			if (key.equalsIgnoreCase("ntaxprice") || key.equalsIgnoreCase("ntaxrate")) {
				nprice =  ntaxprice.compareTo(nzero) == 0 ? new UFDouble(0.0) : 
					ntaxrate.compareTo(nzero) == 0 ? ntaxprice: ntaxprice.div((ntaxrate.div(100)).add(1), UFDouble.ROUND_HALF_UP);
			}
			getBillCardPanel().setBodyValueAt(ntaxprice, irow, "ntaxprice");
			getBillCardPanel().setBodyValueAt(nprice, irow, "nprice");
			ncurprice = nprice.multiply(nroe,UFDouble.ROUND_HALF_UP);
			getBillCardPanel().setBodyValueAt(ncurprice, irow, "ncurprice");
			ncurtaxprice = ntaxprice.multiply(nroe,UFDouble.ROUND_HALF_UP);
			getBillCardPanel().setBodyValueAt(ncurtaxprice, irow, "ncurtaxprice");
			nsummny = number.multiply(ntaxprice,UFDouble.ROUND_HALF_UP);
			getBillCardPanel().setBodyValueAt(nsummny, irow, "nsummny");
			nmny = number.multiply(nprice, UFDouble.ROUND_HALF_UP);
			getBillCardPanel().setBodyValueAt(nmny, irow, "nmny");
			ntaxman = nsummny.sub(nmny, UFDouble.ROUND_HALF_UP);
			getBillCardPanel().setBodyValueAt(ntaxman, irow, "ntaxman");
			ncursummny = number.multiply(ncurtaxprice, UFDouble.ROUND_HALF_UP);
			getBillCardPanel().setBodyValueAt(ncursummny, irow, "ncursummny");
			ncurmny = number.multiply(ncurprice, UFDouble.ROUND_HALF_UP);
			getBillCardPanel().setBodyValueAt(ncurmny, irow, "ncurmny");
			ncurtaxmny = ncursummny.sub(ncurmny, UFDouble.ROUND_HALF_UP);
			getBillCardPanel().setBodyValueAt(ncurtaxmny, irow, "ncurtaxmny");
		}
		if (mnyfld.contains(key)){
			if(key.equalsIgnoreCase("nsummny")){
				ntaxprice =  nsummny.compareTo(nzero)==0 ? new UFDouble(0.0) : 
					number.compareTo(nzero) == 0 ? new UFDouble(0.0): nsummny.div(number,UFDouble.ROUND_HALF_UP);
				nprice =  ntaxprice.compareTo(nzero) == 0 ? new UFDouble(0.0) : 
					ntaxrate.compareTo(nzero) == 0 ? ntaxprice: ntaxprice.div((ntaxrate.div(100)).add(1), UFDouble.ROUND_HALF_UP);	
				getBillCardPanel().setBodyValueAt(ntaxprice, irow, "ntaxprice");
				getBillCardPanel().setBodyValueAt(nprice, irow, "nprice");
				ncurprice = nprice.multiply(nroe,UFDouble.ROUND_HALF_UP);
				getBillCardPanel().setBodyValueAt(ncurprice, irow, "ncurprice");
				ncurtaxprice = ntaxprice.multiply(nroe,UFDouble.ROUND_HALF_UP);
				getBillCardPanel().setBodyValueAt(ncurtaxprice, irow, "ncurtaxprice");
				//nsummny = number.multiply(ntaxprice,UFDouble.ROUND_HALF_UP);
				//getBillCardPanel().setBodyValueAt(nsummny, irow, "nsummny");
				nmny = number.multiply(nprice, UFDouble.ROUND_HALF_UP);
				getBillCardPanel().setBodyValueAt(nmny, irow, "nmny");
				ntaxman = nsummny.sub(nmny, UFDouble.ROUND_HALF_UP);
				getBillCardPanel().setBodyValueAt(ntaxman, irow, "ntaxman");
				ncursummny = number.multiply(ncurtaxprice, UFDouble.ROUND_HALF_UP);
				getBillCardPanel().setBodyValueAt(ncursummny, irow, "ncursummny");
				ncurmny = number.multiply(ncurprice, UFDouble.ROUND_HALF_UP);
				getBillCardPanel().setBodyValueAt(ncurmny, irow, "ncurmny");
				ncurtaxmny = ncursummny.sub(ncurmny, UFDouble.ROUND_HALF_UP);
				getBillCardPanel().setBodyValueAt(ncurtaxmny, irow, "ncurtaxmny");

			}
			if(key.equalsIgnoreCase("nmny")){
				nprice =  nmny.compareTo(nzero)==0 ? new UFDouble(0.0) : 
					number.compareTo(nzero) == 0 ? new UFDouble(0.0): nmny.div(number,UFDouble.ROUND_HALF_UP);

				ntaxprice =  nprice.compareTo(nzero) == 0 ? new UFDouble(0.0) : 
					ntaxrate.compareTo(nzero) == 0 ? nprice: nprice.multiply((ntaxrate.div(100)).add(1), UFDouble.ROUND_HALF_UP);	
				
				getBillCardPanel().setBodyValueAt(ntaxprice, irow, "ntaxprice");
				getBillCardPanel().setBodyValueAt(nprice, irow, "nprice");
				ncurprice = nprice.multiply(nroe,UFDouble.ROUND_HALF_UP);
				getBillCardPanel().setBodyValueAt(ncurprice, irow, "ncurprice");
				ncurtaxprice = ntaxprice.multiply(nroe,UFDouble.ROUND_HALF_UP);
				getBillCardPanel().setBodyValueAt(ncurtaxprice, irow, "ncurtaxprice");
				nsummny = number.multiply(ntaxprice,UFDouble.ROUND_HALF_UP);
				getBillCardPanel().setBodyValueAt(nsummny, irow, "nsummny");
				//nmny = number.multiply(nprice, UFDouble.ROUND_HALF_UP);
				//getBillCardPanel().setBodyValueAt(nmny, irow, "nmny");
				ntaxman = nsummny.sub(nmny, UFDouble.ROUND_HALF_UP);
				getBillCardPanel().setBodyValueAt(ntaxman, irow, "ntaxman");
				ncursummny = number.multiply(ncurtaxprice, UFDouble.ROUND_HALF_UP);
				getBillCardPanel().setBodyValueAt(ncursummny, irow, "ncursummny");
				ncurmny = number.multiply(ncurprice, UFDouble.ROUND_HALF_UP);
				getBillCardPanel().setBodyValueAt(ncurmny, irow, "ncurmny");
				ncurtaxmny = ncursummny.sub(ncurmny, UFDouble.ROUND_HALF_UP);
				getBillCardPanel().setBodyValueAt(ncurtaxmny, irow, "ncurtaxmny");

			}
		}

	}

	@Override
	public boolean beforeEdit(BillEditEvent e) {
		// TODO Auto-generated method stub
		return super.beforeEdit(e);
		
	}

	protected List<IUserDefButtonCommand> creatUserButtons(){
		 
		  List<IUserDefButtonCommand> bos = new ArrayList<IUserDefButtonCommand>();
		  bos.add(new btRevDealBoCommand(this, getUIControl()));
		  bos.add(new btDealBoCommand(this, getUIControl()));
		  bos.add(new btDealGpBoCommand(this, getUIControl()));
		  return bos;
		}

	public void setDefaultData() throws Exception {
		IBillField fileDef = getBillField();
		String billtype = getUIControl().getBillType();
		String busitype = getBusinessType();
		String pkCorp = ClientEnvironment.getInstance().getCorporation().getPrimaryKey();
		
		String[] itemkeys = new String[]{
				fileDef.getField_Busitype(),
				fileDef.getField_Corp(),
				fileDef.getField_Operator(),
				fileDef.getField_Billtype(),
				fileDef.getField_BillStatus()
				};
		Object[] values = new Object[]{
				busitype,
				pkCorp,
				ClientEnvironment.getInstance().getUser().getPrimaryKey(),
				billtype,
				new Integer(IBillStatus.FREE).toString()
				};
		
		for(int i = 0; i < itemkeys.length; i++){
			BillItem item = null;
			item = getBillCardPanel().getHeadItem(itemkeys[i]);
			if(item == null)
				item = getBillCardPanel().getTailItem(itemkeys[i]);
			if(item != null)
				item.setValue(values[i]);
		}
		
		getBillCardPanel().getHeadItem("dinvoicedate").setValue(ClientEnvironment.getInstance().getDate());
		getBillCardPanel().getHeadItem("dreceivedate").setValue(ClientEnvironment.getInstance().getDate());
		getBillCardPanel().getHeadItem("nroe").setValue(new UFDouble(1));


	}

	public UFDouble calcurateTotal(String key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isSaveAndCommitTogether() {
		// TODO Auto-generated method stub
		return true;
		//return super.isSaveAndCommitTogether();  
	}

	/* (non-Javadoc)
	 * @see nc.ui.trade.manage.BillManageUI#loadListHeadData(java.lang.String, nc.ui.querytemplate.IBillReferQuery)
	 */
	@Override
	public void loadListHeadData(String strWhere, IBillReferQuery qryCondition)
			throws Exception {
		// TODO Auto-generated method stub
		super.loadListHeadData(strWhere, qryCondition);
		//setDigits();
	}

	private void setDigits() {
		// TODO Auto-generated method stub
		 setPricefld( Arrays.asList( new String[]{"nprice","ntaxprice","ncurprice","ncurtaxprice"}));
		 setMnyfld(Arrays.asList(new String[]{"nmny","ntaxman","nsummny","ncurmny","ncurtaxmny","ncursummny","ntotaldealmny"}));
		 setNumfld(Arrays.asList( new String[]{"nnumber","ntotaldealnum"}));
		
		//if (isListPanelSelected()) {
			for (int i = 0 ; i < numfld.size(); i++){
				getBillListPanel().getBodyItem(numfld.get(i)).setDecimalDigits(4);
			}
			
			for (int i = 0 ; i < mnyfld.size(); i++){
				getBillListPanel().getBodyItem(mnyfld.get(i)).setDecimalDigits(2);
			}
			
			for (int i = 0 ; i < pricefld.size(); i++){
				getBillListPanel().getBodyItem(pricefld.get(i)).setDecimalDigits(6);
			}
			
			getBillListPanel().getBodyItem("ntaxrate").setDecimalDigits(2);
			
			getBillListPanel().getHeadItem("nroe").setValue(new UFDouble(1.0));
			
			getBillListPanel().getBodyItem("ntaxrate").setDecimalDigits(2);
			
		//} else
		//{
			for (int i = 0 ; i < numfld.size(); i++){
				getBillCardPanel().getBodyItem(numfld.get(i)).setDecimalDigits(4);
			}
			
			for (int i = 0 ; i < mnyfld.size(); i++){
				getBillCardPanel().getBodyItem(mnyfld.get(i)).setDecimalDigits(2);
			}
			
			for (int i = 0 ; i < pricefld.size(); i++){
				getBillCardPanel().getBodyItem(pricefld.get(i)).setDecimalDigits(6);
			}
			
			getBillCardPanel().getBodyItem("ntaxrate").setDecimalDigits(2);
			getBillCardPanel().getHeadItem("ntaxrate").setDecimalDigits(2);
			getBillCardPanel().getHeadItem("nroe").setDecimalDigits(2);

		//}
	}

	/**
	 * @return the pricefld
	 */
	public List<String> getPricefld() {
		return pricefld;
	}

	/**
	 * @param pricefld the pricefld to set
	 */
	public void setPricefld(List<String> pricefld) {
		this.pricefld = pricefld;
	}

	/**
	 * @return the mnyfld
	 */
	public List<String> getMnyfld() {
		return mnyfld;
	}

	/**
	 * @param mnyfld the mnyfld to set
	 */
	public void setMnyfld(List<String> mnyfld) {
		this.mnyfld = mnyfld;
	}

	/**
	 * @return the numfld
	 */
	public List<String> getNumfld() {
		return numfld;
	}

	/**
	 * @param numfld the numfld to set
	 */
	public void setNumfld(List<String> numfld) {
		this.numfld = numfld;
	}

	public boolean checkAudit() {
	// TODO Auto-generated method stub

		boolean ibhasdeal = checkDeal();
		//从模板上获得核销策略（最好是从VO）
		int iapprovetype = Integer.parseInt(getBillCardPanel().getHeadItem("iapprovetype").getValue());
		
		if (iapprovetype == ITaxInvoiceApproveType.DEAL_AFTER_AUDIT) 
		{
			if (!ibhasdeal)
				return true;
			else
				return false;
				
		}else //审核后核销
		{
			if (ibhasdeal)
				return true;
			else
				return false;
		}
	}

	 private void setUserButtonStatus() {
		// TODO Auto-generated method stub
		try { 
//			if (((ClientUI)getBillUI()).isListPanelSelected()) {
//				setStrShowState(strPanelList);
//			}else
//			{
//				setStrShowState(strPanelCard);
//			}
			if (getBillOperate() == IBillOperate.OP_INIT ) {
				return;
			}
			if (getBillOperate() == IBillOperate.OP_ADD ||
					getBillOperate() == IBillOperate.OP_INIT ||
					getBillOperate() == IBillOperate.OP_REFADD ||
					getBillOperate() == IBillOperate.OP_NOTEDIT  &&   isListPanelSelected() ) {
				getButtonManager().getButton(nc.ui.so.taxinvoice.command.btDealGpBoCommand.BUTTON_NO).setEnabled(false);
			}
			
			int iapprovetype = Integer.parseInt(getBillCardPanel().getHeadItem("iapprovetype").getValue()==null?  "1" : getBillCardPanel().getHeadItem("iapprovetype").getValue() );
			TaxInvoiceVO taxinvvo = (TaxInvoiceVO) getVOFromUI();
			int ibillstate = ((TaxInvoiceHeaderVO)taxinvvo.getParentVO()).getIbillstatus();
			
			if (getBillOperate() == IBillOperate.OP_NOTEDIT && !isListPanelSelected()) {
				getButtonManager().getButton(nc.ui.so.taxinvoice.command.btDealGpBoCommand.BUTTON_NO).setEnabled(true);
				//getButtonManager().getButton(nc.ui.so.taxinvoice.command.btRevDealBoCommand.BUTTON_NO).setEnabled(true);

				if (iapprovetype == ITaxInvoiceApproveType.DEAL_AFTER_AUDIT){  //核销前审核
					if ( ibillstate == IBillStatus.CHECKPASS ){
						getButtonManager().getButton(nc.ui.so.taxinvoice.command.btDealBoCommand.BUTTON_NO).setEnabled(true);
						getButtonManager().getButton(nc.ui.so.taxinvoice.command.btRevDealBoCommand.BUTTON_NO).setEnabled(true);
					}else{
						getButtonManager().getButton(nc.ui.so.taxinvoice.command.btDealBoCommand.BUTTON_NO).setEnabled(false);
						getButtonManager().getButton(nc.ui.so.taxinvoice.command.btRevDealBoCommand.BUTTON_NO).setEnabled(true);
					}
				}else{
					if (ibillstate == IBillStatus.FREE || ibillstate == IBillStatus.CHECKGOING  || ibillstate == IBillStatus.COMMIT ){
						getButtonManager().getButton(nc.ui.so.taxinvoice.command.btDealBoCommand.BUTTON_NO).setEnabled(true);
						getButtonManager().getButton(nc.ui.so.taxinvoice.command.btRevDealBoCommand.BUTTON_NO).setEnabled(true);
					}else{
						getButtonManager().getButton(nc.ui.so.taxinvoice.command.btDealBoCommand.BUTTON_NO).setEnabled(false);
						getButtonManager().getButton(nc.ui.so.taxinvoice.command.btRevDealBoCommand.BUTTON_NO).setEnabled(true);
					}
//					getButtonManager().getButton(nc.ui.so.taxinvoice.command.btDealBoCommand.BUTTON_NO).setEnabled(true);
//					getButtonManager().getButton(nc.ui.so.taxinvoice.command.btRevDealBoCommand.BUTTON_NO).setEnabled(true);
				}
			}
			
//			if(((ClientUI)getBillUI()).isSaveAndCommitTogether()) {
//				if ( ibillstate == IBillStatus.COMMIT || ibillstate == IBillStatus.FREE){
//					getButtonManager().getButton(nc.ui.trade.button.IBillButton.Edit).setEnabled(true);
//					getButtonManager().getButton(nc.ui.trade.button.IBillButton.Del).setEnabled(true);
//				}
//			}
			if (isSaveAndCommitTogether()) {
				getButtonManager().getButton(nc.ui.trade.button.IBillButton.Commit).setVisible(false);  //2014-02-16 自动提交的话，这里就不显示这个按钮了 
			}
			if (checkDeal()) {
				getButtonManager().getButton(nc.ui.trade.button.IBillButton.Del).setEnabled(false); //如果已经核销，不能删除，修改
				getButtonManager().getButton(nc.ui.trade.button.IBillButton.Edit).setEnabled(false);
			}
			//2014-02-17 处理一下审核按钮；
			if (iapprovetype == ITaxInvoiceApproveType.DEAL_BEFORE_AUDIT ){
				if( checkDeal() && (ibillstate == IBillStatus.FREE  || ibillstate == IBillStatus.COMMIT)){

				getButtonManager().getButton(nc.ui.trade.button.IBillButton.Audit).setEnabled(true);
				}else{
				getButtonManager().getButton(nc.ui.trade.button.IBillButton.Audit).setEnabled(false);
			}
			}
			
			if (iapprovetype == ITaxInvoiceApproveType.DEAL_AFTER_AUDIT){
				if( !checkDeal() && (ibillstate == IBillStatus.FREE  || ibillstate == IBillStatus.COMMIT)){
				getButtonManager().getButton(nc.ui.trade.button.IBillButton.Audit).setEnabled(true);
			}else{
				getButtonManager().getButton(nc.ui.trade.button.IBillButton.Audit).setEnabled(false);
			}
			}
//
//			if (isListPanelSelected()) {
//				getButtonManager().getButton(nc.ui.trade.button.IBillButton.Del).setEnabled(false); //列表下禁用删除
//			}
			
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		getBillUI().updateButtons();

	}

		public boolean checkDeal() {
			// TODO Auto-generated method stub
			boolean ibhasdeal = false;

				try {
					TaxInvoiceVO taxinvvo = (TaxInvoiceVO) getVOFromUI();
					if (null == taxinvvo)
						return false;
					TaxInvoiceItemVO[] taxinvitemvo = (TaxInvoiceItemVO[]) taxinvvo
							.getChildrenVO();
					for (int i = 0; i < taxinvitemvo.length; i++) {
						UFDouble totaldealmny = taxinvitemvo[i].getNtotaldealmny()==null? UFDouble.ZERO_DBL: taxinvitemvo[i].getNtotaldealmny();
						UFDouble totaldealnmb = taxinvitemvo[i].getNtotaldealnum()==null? UFDouble.ZERO_DBL: taxinvitemvo[i].getNtotaldealnum();
						if (!totaldealmny.equals(UFDouble.ZERO_DBL) || !totaldealnmb.equals(UFDouble.ZERO_DBL)) {
							ibhasdeal = true;
							break;
						}
					}
				} catch (NumberFormatException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			return 	ibhasdeal;

		}

		/* (non-Javadoc)
		 * @see nc.ui.pub.ToftPanel#updateButtons()
		 */
		@Override
		public void updateButtons() {
			// TODO Auto-generated method stub
			setUserButtonStatus();
			super.updateButtons();
		}

		/* (non-Javadoc)
		 * @see nc.ui.so.taxinvoice.AbstractClientUI#doQueryAction(nc.ui.pub.linkoperate.ILinkQueryData)
		 */
		@Override
		public void doQueryAction(ILinkQueryData querydata) {
			// TODO Auto-generated method stub

			super.doQueryAction(querydata);
			setUserButtonStatus();
		}

}
