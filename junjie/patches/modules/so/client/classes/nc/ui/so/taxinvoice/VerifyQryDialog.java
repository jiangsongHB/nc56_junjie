package nc.ui.so.taxinvoice;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import nc.ui.pub.beans.UIDialog;
import nc.vo.so.TaxInvoiceItemVO;
import nc.vo.so.TaxInvoiceVO;

public class VerifyQryDialog extends UIDialog implements ActionListener {

	private VerifyQryPanel ijVerifyQryPanel;
	private TaxInvoiceVO ijTaxInvoiceVO;
	private TaxInvoiceItemVO ijTaxInvoiceItemVO;

	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub

	}

	public VerifyQryDialog(java.awt.Container parent, String title) {
		super(parent, title);

	}
	
	public VerifyQryDialog(TaxInvoiceVO vo, TaxInvoiceItemVO itemvo) {
		super();
		this.ijTaxInvoiceVO = vo;
		this.ijTaxInvoiceItemVO = itemvo;
		this.setIjTaxInvoiceItemVO(itemvo);
		initialize();
	}
	private void initialize() {
		this.setTitle("核销查询");
		// dialog的滚动条
		this.setContentPane(getVerifyQryPanel());
		this.setSize(1024, 700);
	}

	public VerifyQryPanel getVerifyQryPanel() {
		if (ijVerifyQryPanel == null) {
			
			ijVerifyQryPanel = new VerifyQryPanel();
			ijVerifyQryPanel.setTaxInvoiceVO(ijTaxInvoiceVO);
			ijVerifyQryPanel.setTaxInvoiceItemVO(ijTaxInvoiceItemVO);
			ijVerifyQryPanel.setIjVerifyQryDialog(this);
			ijVerifyQryPanel.initialize();
			ijVerifyQryPanel.initData();
			
		}
		return ijVerifyQryPanel;
	}

	public void setIjVerifyQryPanel(VerifyQryPanel ijVerifyPanel) {
		this.ijVerifyQryPanel = ijVerifyPanel;
	}

	public TaxInvoiceItemVO getIjTaxInvoiceItemVO() {
		return ijTaxInvoiceItemVO;
	}

	public void setIjTaxInvoiceItemVO(TaxInvoiceItemVO ijTaxInvoiceItemVO) {
		this.ijTaxInvoiceItemVO = ijTaxInvoiceItemVO;
	}

}
