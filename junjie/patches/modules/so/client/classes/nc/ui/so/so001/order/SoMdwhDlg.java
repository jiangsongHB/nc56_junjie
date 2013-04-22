package nc.ui.so.so001.order;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import nc.ui.pub.beans.UIDialog;
import nc.ui.so.so001.panel.SaleBillUI;
import nc.vo.so.so001.SaleorderBVO;
import nc.vo.so.so001.SaleorderHVO;

public class SoMdwhDlg extends UIDialog implements ActionListener {

	private SoMdwhPanel OrigPiecePanel = null;

	private SaleorderHVO hvo;

	private SaleorderBVO bvo;
	
	//chenjianhua 2013-04-11
	private SaleBillUI saleOrderAdminUI;

	/**
	 * This method initializes
	 * 
	 */
	public SoMdwhDlg(java.awt.Container parent, String title) {
		super(parent, title);
		initialize();

	}

	public SoMdwhDlg(SaleorderHVO hvo, SaleorderBVO bvo, SaleBillUI saleOrderAdminUI) {
		super(saleOrderAdminUI);
		this.hvo = hvo;
		this.bvo = bvo;
		
		//chenjianhua 2013-04-11
		this.saleOrderAdminUI=saleOrderAdminUI;
		//end 
		
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 */
	private void initialize() {
		this.setTitle("码单锁定");
		// dialog的滚动条
		this.setContentPane(getOrigPiecePanel());
		this.setSize(1024, 700);
	}

	public SoMdwhPanel getOrigPiecePanel() {
		if (OrigPiecePanel == null) {
			//OrigPiecePanel = new SoMdwhPanel(this, hvo, bvo);
			
			//chenjianhua 2013-04-11
			OrigPiecePanel = new SoMdwhPanel(this, hvo, bvo,saleOrderAdminUI);
			//end 
			
		}
		return OrigPiecePanel;
	}

	public void actionPerformed(ActionEvent e) {
	}

	public void setOrigPiecePanel(SoMdwhPanel origPiecePanel) {
		OrigPiecePanel = origPiecePanel;
	}

}
