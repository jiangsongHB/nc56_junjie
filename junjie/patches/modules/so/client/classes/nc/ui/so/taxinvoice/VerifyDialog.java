package nc.ui.so.taxinvoice;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import nc.ui.pub.beans.UIDialog;
import nc.vo.so.TaxInvoiceItemVO;
import nc.vo.so.TaxInvoiceVO;

public class VerifyDialog extends UIDialog implements ActionListener {

	private VerifyPanel ijVerifyPanel;
	private TaxInvoiceVO ijTaxInvoiceVO;
	private TaxInvoiceItemVO ijTaxInvoiceItemVO;

	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub

	}

	public VerifyDialog(java.awt.Container parent, String title) {
		super(parent, title);

	}
	
	public VerifyDialog(TaxInvoiceVO vo, TaxInvoiceItemVO itemvo) {
		super();
		ijTaxInvoiceVO = vo;
		ijTaxInvoiceItemVO = itemvo;
		initialize();
	}
	private void initialize() {
		this.setTitle("发票核销");
		// dialog的滚动条
		this.setContentPane(getVerifyPanel());
		this.setSize(1024, 700);
		this.setResizable(true);
	}

	public VerifyPanel getVerifyPanel() {
		if (ijVerifyPanel == null) {
			
			ijVerifyPanel = new VerifyPanel();
			ijVerifyPanel.setTaxInvoiceVO(ijTaxInvoiceVO);
			ijVerifyPanel.setTaxInvoiceItemVO(ijTaxInvoiceItemVO);
			ijVerifyPanel.setIjVerifyDialog(this);
			ijVerifyPanel.initialize();
			ijVerifyPanel.initData();
			
		}
		return ijVerifyPanel;
	}

	public void setIjVerifyPanel(VerifyPanel ijVerifyPanel) {
		this.ijVerifyPanel = ijVerifyPanel;
	}

	public TaxInvoiceItemVO getIjTaxInvoiceItemVO() {
		return ijTaxInvoiceItemVO;
	}

	public void setIjTaxInvoiceItemVO(TaxInvoiceItemVO ijTaxInvoiceItemVO) {
		this.ijTaxInvoiceItemVO = ijTaxInvoiceItemVO;
	}

}
