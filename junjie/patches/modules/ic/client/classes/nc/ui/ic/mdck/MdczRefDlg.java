package nc.ui.ic.mdck;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.ListSelectionModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import nc.ui.bd.ref.AbstractRefModel;
import nc.ui.ia.ia301.AuditBillListPanel;
import nc.ui.ml.NCLangRes;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIButton;
import nc.ui.pub.beans.UILabel;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UITable;
import nc.ui.pub.beans.UITextArea;
import nc.ui.pub.beans.table.NCTableModel;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillEditListener;
import nc.ui.pub.bill.BillEditListener2;
import nc.ui.pub.bill.BillListPanel;
import nc.ui.pub.bill.BillModel;
import nc.ui.pub.bill.BillScrollPane;
import nc.ui.pub.bill.BillSortListener2;
import nc.ui.pub.bill.BillTableSelectionChangeListener;
import nc.ui.pub.bill.BillTableSelectionEvent;
import nc.ui.pub.bill.BillTotalListener;
import nc.vo.ia.ia301.AuditBillVO;
import nc.vo.pub.CommonConstant;
import nc.vo.pub.lang.UFDouble;


/**

 */
public class MdczRefDlg extends nc.ui.pub.beans.UIDialog implements
		java.awt.event.ActionListener, nc.ui.bd.ref.IRefUINew, BillEditListener,BillTableSelectionChangeListener  ,  BillTotalListener, BillEditListener2, ChangeListener {

	private UIButton m_btnOK = null;

	private UIButton m_btnCancel = null;

	private UITable m_table = null;

	private boolean m_bCloseOK = false;

	private AbstractRefModel m_refModel = null;
	//�����
	private BillCardPanel m_tbpnl = null;
	
	private javax.swing.JPanel m_btpnl = null;
	
	private String[] refpks = null;



	/**
	 * StandInputDlg ������ע�⡣
	 * 
	 * @param parent
	 *            java.awt.Container
	 * @param title
	 *            java.lang.String
	 */
	public MdczRefDlg(java.awt.Container parent, String title , String whsql) {

		super(parent, title);
		
		this.setSize(1000, 400);
		initialize();
		loaddata(whsql);
		
	}

	private void loaddata(String whsql){
		if (m_refModel == null)
			m_refModel = new nc.ui.ic.mdck.MdczRef(whsql);
		m_refModel.clearCacheData();  //wanglei 2014-01-14 ����ǰ��һ�²��ջ��棻
			
		m_tbpnl.setDataVector(m_refModel.getData());
		
		
		//m_tbpnl.getBodyPanel().getTableModel().clearTotalModel();
		
	}
	/**
	 * �˴����뷽��˵���� ��������: �������: ����ֵ: �쳣����: ����:
	 */
	public void actionPerformed(java.awt.event.ActionEvent event) {
		if ((UIButton) event.getSource() == m_btnCancel) {
			m_bCloseOK = false;
			this.closeCancel();
		} else {
//			if (m_table.isEditing()) {
//				if (m_table.getCellEditor() != null) {
//					m_table.getCellEditor().stopCellEditing();
//				}
//			}
//
//			if (m_bCloseOK)
		this.closeOK();

		}
	}

	/**
	 * �˴����뷽��˵���� ��������:�жϱ�׼ֵ�Ƿ�����Ƿ��ַ� �������: ����ֵ: �쳣����: ����:2002/06/05
	 * 
	 * @return boolean
	 * @param value
	 *            java.lang.String
	 */
	private boolean hasIllegalChar(String value) {
		return false;
	}

	/**
	 * �˴����뷽��˵���� ��������: �������: ����ֵ: �쳣����: ����:
	 */
	private void initialize() {     
		
		setResizable(true);
		getContentPane().setLayout(new BorderLayout());
		
		getContentPane().add(getTablePane());
		getContentPane().add(getBtnPane(),BorderLayout.SOUTH);
		//this.getRefModel().addChangeListener(this);
		//m_refModel = new nc.ui.ic.mdck.MdczRef();
			
	}
	
	/**
	 * ��ȡȷ����ť
	 * @return
	 */
	private UIButton getBtnOk(){
		if(m_btnOK == null){
			m_btnOK = new UIButton();
			m_btnOK.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
					"UC001-0000044")/* @res "ȷ��" */);
			m_btnOK.addActionListener(this);
		}
		return m_btnOK;
	}
	/**
	 * ��ȡȡ����ť
	 * @return
	 */
	private UIButton getBtnCancel(){
		if(m_btnCancel == null){
			m_btnCancel = new UIButton();
			m_btnCancel.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"common", "UC001-0000008")/* @res "ȡ��" */);
			m_btnCancel.addActionListener(this);
		}
		return m_btnCancel;
	}
	/**
	 * ��ȡ������
	 * @return
	 */
	private BillCardPanel getTablePane(){
		if(m_tbpnl == null){
			m_tbpnl = new BillCardPanel();
			m_tbpnl.loadTemplet("H006", null, ClientEnvironment.getInstance().getUser().getPrimaryKey(), ClientEnvironment.getInstance().getCorporation().getPrimaryKey());
			m_tbpnl.setTatolRowShow(true);
			m_tbpnl.setRowNOShow(true);
			m_tbpnl.getBillTable().setRowSelectionAllowed(true);
			m_tbpnl.getBillTable().setSelectionMode(
			        ListSelectionModel.SINGLE_SELECTION);
			m_tbpnl.addEditListener(this);
			m_tbpnl.addBodyTotalListener(this);
			m_tbpnl.setBodyMenuShow(false);
			m_tbpnl.setBodyMultiSelect(true);
		}
		return m_tbpnl;
	}

	/**
	 * ��ȡ��ť�����
	 * @return
	 */
	private javax.swing.JPanel getBtnPane(){
		if(m_btpnl == null){
			m_btpnl = new javax.swing.JPanel();
			m_btpnl.setLayout(new FlowLayout());
			
			m_btpnl.add(getBtnOk());
			m_btpnl.add(getBtnCancel());
		}
		return m_btpnl;
	}

	/**
	 * �˴����뷽��˵���� ��������: �������: ����ֵ: �쳣����: ����:
	 * 
	 * @return boolean
	 */
	public boolean isCloseOK() {
		return m_bCloseOK;
	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2004-6-18 15:36:18)
	 */
	public void setFilterDlgShow(boolean isFilterDlgShow) {//
	}

	/**
	 * setIncludeSubShow ����ע�⡣
	 */
	public void setIncludeSubShow(boolean newIncludeSubShow) {//
	}

	/**
	 * 
	 * �������ڣ�(2003-7-23 21:33:23) �Ƿ�๫˾���տ���
	 */
	public void setMultiCorpRef(boolean isMultiCorpRef) {//
	}

	/**
	 * �����Ƿ������ѡ�� �������ڣ�(2001-8-24 21:33:23)
	 * 
	 * @param isMultiSelectedEnabled
	 *            boolean
	 */
	public void setMultiSelectedEnabled(boolean isMultiSelectedEnabled) {//
		
	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2001-10-26 9:46:41)
	 * 
	 * @param newNotLeafSelectedEnabled
	 *            boolean
	 */
	public void setNotLeafSelectedEnabled(boolean newNotLeafSelectedEnabled) {//
	}

	/**
	 * ���ò���ģ�͡� �������ڣ�(2001-8-24 8:36:55)
	 * 
	 * @param refModel
	 *            nc.ui.bd.ref.AbstractRefModel
	 */

	/**
	 * 
	 * �������ڣ�(2003-7-23 21:33:23) ���õ�һ�β�����ʾ���ݵĹ�˾
	 */
	public void setTreeGridNodeMultiSelected(boolean isMulti) {//
	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2004-6-18 15:36:18)
	 */
	public void setVersionButtonShow(boolean isVersionButtonShow) {//
	}


	public AbstractRefModel getRefModel() {
		// TODO Auto-generated method stub
		return m_refModel;
	}


	public void setRefModel(AbstractRefModel refModel) {
		// TODO Auto-generated method stub
		m_refModel = refModel;
	}

	public void afterEdit(BillEditEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void bodyRowChange(BillEditEvent e) {
		// TODO Auto-generated method stub
//		calcurateTotal("zhishu");
	}

	public UFDouble calcurateTotal(String key) {
		// TODO Auto-generated method stub
	      if (key == null) {
	          return null;
	        }
	      
	      	int iselrow[]  = m_tbpnl.getBodyPanel().getTable().getSelectedRows();
	      	
	      	if (iselrow == null) {
	          return new UFDouble(0);
	        }
	        UFDouble total = new UFDouble(0);
	        boolean addFlag = false;
	        if (key.equals("zhishu")) {
	          addFlag = true;
	        }
	        if (key.equals("kyzs")) {
		          addFlag = true;
		        }
	        else if (key.equals("zhongliang")) {
	          addFlag = true;
	        }
	        if (!addFlag) {
	          return total;
	        }
	        
			for (int i = 0; i < m_tbpnl.getBodyPanel().getTable().getRowCount(); i++) {
				
				Object value  = null;
				
				if (m_tbpnl.getBodyPanel().getTableModel().getRowState(i) == BillModel.SELECTED)
					value = m_tbpnl.getBodyPanel().getTableModel().getValueAt(i, key);
				
				if (value != null) {
					total = total.add(new UFDouble(value.toString()));
				}
			}
	        return total;
	}

	public void stateChanged(ChangeEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public void selectionChanged(BillTableSelectionEvent e) {
		// TODO Auto-generated method stub
		calcurateTotal("zhishu");
		calcurateTotal("zhongliang");
		calcurateTotal("kyzs");
	}

	public boolean beforeEdit(BillEditEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see nc.ui.pub.beans.UIDialog#closeCancel()
	 */
	@Override
	public void closeCancel() {
		// TODO Auto-generated method stub
		super.closeCancel();
	}

	/* (non-Javadoc)
	 * @see nc.ui.pub.beans.UIDialog#closeOK()
	 */
	@Override
	public void closeOK() {
		// TODO Auto-generated method stub

		refpks = null;
		ArrayList alpks = new ArrayList(); 
		for (int i = 0; i < m_tbpnl.getBodyPanel().getTable().getRowCount(); i++) {
			
			Object value  = null;
			
			if (m_tbpnl.getBodyPanel().getTableModel().getRowState(i) == BillModel.SELECTED)
				value = m_tbpnl.getBodyPanel().getTableModel().getValueAt(i, "pk_mdxcl_b");
			
			if (value != null) {
				alpks.add(value.toString());
			}
		}
		
		refpks = (String[])alpks.toArray(new String[alpks.size()]);
		//this.getParent();
		setReturndata();
		super.closeOK();
	}

	public java.lang.String[] getRefPKs() {
		return refpks;
	}
	
	private void setReturndata(){
		Vector vSelectedData = null;
//		int[] selectedRows = m_tbpnl.getBodyPanel().getTable().getSelectedRows();
//		if (selectedRows != null && selectedRows.length > 0) {
			vSelectedData = new Vector();
			for (int i = 0; i < m_tbpnl.getBodyPanel().getTable().getRowCount(); i++) {
				if (m_tbpnl.getBodyPanel().getTableModel().getRowState(i) == BillModel.SELECTED) {
				Vector vRecord = new Vector();
				for (int j = 0; j < m_tbpnl.getBodyPanel().getTable().getModel()
						.getColumnCount(); j++) {
					vRecord.addElement(m_tbpnl.getBodyPanel().getTable()
							.getModel().getValueAt(i, j));
				}
				vSelectedData.addElement((Vector) vRecord);
				}
			}
//		}
		getRefModel().setSelectedData(vSelectedData);
		
		//closeOK();
	}

}