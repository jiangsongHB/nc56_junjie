package nc.ui.pu.jjpanel;

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
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillEditListener;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillListPanel;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.bill.BillModel;
import nc.ui.pub.bill.BillScrollPane;
import nc.ui.pub.hotkey.HotkeyUtil;
import nc.ui.pu.jj.JJPuScmPubHelper;
import nc.vo.am.util.importtools.VOCheck;
import nc.vo.ml.IProductCode;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pu.jjvo.InformationCostVO;
import nc.vo.scm.pub.SCMEnv;
import nc.vo.trade.pub.HYBillVO;

/**
 * @function ������Ϣ��¼�����
 * 
 * @author QuSida
 * 
 * @date 2010-8-20 ����10:00:59
 * 
 */
public class InfoCostPanel extends UIDialog implements ActionListener,BillEditListener{

	//�ϼ���Ƭ  by ������ 2010-10-13
	private BillCardPanel parentCardPanel = null;
	
	private BillListPanel billListPanel = null;
	// ȷ����ť
	private nc.ui.pub.beans.UIButton m_btnOK = null;
	// ȡ����ť
	private nc.ui.pub.beans.UIButton m_btnCancel = null;
	// ���Ӱ�ť
	private nc.ui.pub.beans.UIButton m_btnAdd = null;
	// ɾ����ť
	private nc.ui.pub.beans.UIButton m_btnDel = null;
	// �޸İ�ť
	private nc.ui.pub.beans.UIButton m_btnMod = null;
	// ��˾ID
	private String m_sLoginCorp = null;
	// ����ԱID
	private String m_sOperator = null;
	// ��ť���
	private nc.ui.pub.beans.UIPanel m_panelSouth = null;
	// UIDialog�����
	private javax.swing.JPanel m_dialogContentPane = null;
	// ������
	private nc.ui.pub.beans.UIPanel m_pnlFillSpaceWest = null;
	private nc.ui.pub.beans.UIPanel m_pnlFillSpaceEast = null;
	private boolean m_closeMark = false;
	// ������Ϣvo����
	private InformationCostVO[] icvos = null;
	// ���������  by ������ 2010-10-13
	private UFDouble arrnumber = null;
	// ��ǰ�������� by ������ 2010-10-30
	private String billtype = null;

	/**
	 * @function ���븸����,��˾,����Ա�Ĺ��캯��
	 * 
	 * @author QuSida
	 * 
	 * @date 2010-8-20 ����10:02:36
	 * 
	 */
	public InfoCostPanel(java.awt.Container parent, String sPk_Corp,
			String sOperatorID) {
		super(parent);
		setArrnumber(parent);//add by ������
		setBillType(parent);//add by ������ 2010-10-30
		m_sLoginCorp = sPk_Corp;
		initDialog(); // ��ʼ���Ի���
	}



	/**
	 * @function ��д���ഫ�븸�����Ĺ��캯��
	 * 
	 * @author QuSida
	 * 
	 * @date 2010-8-20 ����10:12:28
	 * 
	 */
	public InfoCostPanel(java.awt.Container parent,InformationCostVO[] vos) {
		super(parent);
		setArrnumber(parent);//add by ������
		setBillType(parent);//add by ������ 2010-10-30
		initDialog();
		this.getBillListPanel().setHeaderValueVO(vos);
		this.getBillListPanel().getHeadBillModel().execLoadFormula();
//		this.getBillListPanel().getHeadItem("noriginalcurmny").setEdit(false);
	
	
	}
	public InfoCostPanel(java.awt.Container parent) {
		super(parent);
		setArrnumber(parent);//add by ������
		setBillType(parent);//add by ������ 2010-10-30
		initDialog();
		}

	/**
	 * @function ��ʼ���Ի���
	 * 
	 * @author QuSida
	 * 
	 * @return void
	 * 
	 * @date 2010-8-20 ����10:13:42
	 */
	private void initDialog() {

		setName("InfoCostDialog");
		setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		setTitle("����¼��");
		setBounds(37, 563, 1000, 400);
		setResizable(false);
		// ������ʾ����
		setContentPane(getUIDialogContentPane());
		// �޸Ĳ���,ֻ��ʾӦ˰�����־Ϊ'Y'�Ĵ��
		UIRefPane refPane = (UIRefPane) getBillListPanel().getHeadItem("costcode").getComponent();
		nc.ui.bd.ref.busi.InvmandocDefaultRefModel refModel = (InvmandocDefaultRefModel) refPane.getRefModel();
		refModel.setWherePart(" bd_invbasdoc.laborflag = 'Y' and bd_invmandoc.pk_corp = '"+ClientEnvironment.getInstance().getCorporation().getPk_corp()+"'");


	}

	/**
	 * @function �õ���ʾ�������
	 * 
	 * @author QuSida
	 * 
	 * @return javax.swing.JPanel
	 * 
	 * @date 2010-8-20 ����10:14:10
	 */
	private javax.swing.JPanel getUIDialogContentPane() {
		if (m_dialogContentPane == null) {
			m_dialogContentPane = new javax.swing.JPanel();
			m_dialogContentPane.setName("UIDialogContentPane");
			m_dialogContentPane.setLayout(new java.awt.BorderLayout());
			// ��ťpanel
			m_dialogContentPane.add(getPnlSouth(), "South");
			// ��˾panel
			m_dialogContentPane.add(getBillListPanel(), "Center");
			// �߽����panel
			m_dialogContentPane.add(getPnlFillSpaceWest(), "West");
			m_dialogContentPane.add(getPnlFillSpaceEast(), "East");
		}
		return m_dialogContentPane;
	}

	/**
	 * @function ��ð�ť���
	 * 
	 * @author QuSida
	 * 
	 * @return nc.ui.pub.beans.UIPanel
	 * 
	 * @date 2010-8-20 ����10:15:33
	 */
	private nc.ui.pub.beans.UIPanel getPnlSouth() {
		if (m_panelSouth == null) {
			m_panelSouth = new nc.ui.pub.beans.UIPanel();
			m_panelSouth.setName("PnlSouth");
			// ����
			m_panelSouth.add(getBtnAdd(), getBtnAdd().getName());
			// ɾ��
			m_panelSouth.add(getBtnDel(), getBtnDel().getName());
			// ȷ��
			m_panelSouth.add(getBtnOK(), getBtnOK().getName());
			// ȡ��
			m_panelSouth.add(getBtnCancel(), getBtnCancel().getName());
			//�޸�
			m_panelSouth.add(getBtnMod(),getBtnMod().getName());
		}
		return m_panelSouth;
	}

	/**
	 * @function �õ�ȷ����ť
	 * 
	 * @author QuSida
	 * 
	 * @return nc.ui.pub.beans.UIButton
	 * 
	 * @date 2010-8-20 ����10:19:17
	 */
	private nc.ui.pub.beans.UIButton getBtnOK() {
		if (m_btnOK == null) {
			m_btnOK = new nc.ui.pub.beans.UIButton();
			m_btnOK.setName("BtnOK");
			HotkeyUtil.setHotkeyAndText(m_btnOK, 'O', nc.ui.ml.NCLangRes
					.getInstance().getStrByID(IProductCode.PRODUCTCODE_COMMON,
							"UC001-0000044"));/* @res "ȷ��" */
			m_btnOK.addActionListener(this);
		}
		return m_btnOK;
	}

	/**
	 * @function �õ�ɾ����ť
	 * 
	 * @author QuSida
	 * 
	 * @return nc.ui.pub.beans.UIButton
	 * 
	 * @date 2010-8-20 ����10:19:39
	 */
	private nc.ui.pub.beans.UIButton getBtnDel() {
		if (m_btnDel == null) {

			m_btnDel = new nc.ui.pub.beans.UIButton("ɾ��");
			m_btnDel.setName("BtnDel");

			m_btnDel.addActionListener(this);
		}
		return m_btnDel;
	}

	/**
	 * @function �õ����Ӱ�ť
	 * 
	 * @author QuSida
	 * 
	 * @return nc.ui.pub.beans.UIButton
	 * 
	 * @date 2010-8-20 ����10:20:49
	 */
	private nc.ui.pub.beans.UIButton getBtnAdd() {
		if (m_btnAdd == null) {

			m_btnAdd = new nc.ui.pub.beans.UIButton("����");
			m_btnAdd.setName("BtnAdd");

			m_btnAdd.addActionListener(this);
		}
		return m_btnAdd;
	}
	/**
	 * @function �õ��޸İ�ť
	 *
	 * @author QuSida
	 *
	 * @return nc.ui.pub.beans.UIButton
	 *
	 * @date 2010-9-25 ����09:49:41
	 */
	private nc.ui.pub.beans.UIButton getBtnMod() {
		if (m_btnMod == null) {

			m_btnMod = new nc.ui.pub.beans.UIButton("�޸�");
			m_btnMod.setName("BtnMod");

			m_btnMod.addActionListener(this);
		}
		return m_btnMod;
	}

	/**
	 * @function �õ�ȡ����ť
	 * 
	 * @author QuSida
	 * 
	 * @return nc.ui.pub.beans.UIButton
	 * 
	 * @date 2010-8-20 ����10:22:01
	 */
	private nc.ui.pub.beans.UIButton getBtnCancel() {
		if (m_btnCancel == null) {
			m_btnCancel = new nc.ui.pub.beans.UIButton();
			m_btnCancel.setName("BtnCancel");
			HotkeyUtil.setHotkeyAndText(m_btnCancel, 'C', nc.ui.ml.NCLangRes
					.getInstance().getStrByID(IProductCode.PRODUCTCODE_COMMON,
							"UC001-0000008"));/* @res "ȡ��" */
			m_btnCancel.addActionListener(this);
		}
		return m_btnCancel;
	}

	/**
	 * @function �õ�����������Ķ����
	 * 
	 * @author QuSida
	 * 
	 * @return nc.ui.pub.beans.UIPanel
	 * 
	 * @date 2010-8-20 ����10:22:15
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
	 * @function �õ�����������������
	 * 
	 * @author QuSida
	 * 
	 * @return nc.ui.pub.beans.UIPanel
	 * 
	 * @date 2010-8-20 ����10:22:47
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
	 * @function �õ������б����
	 * 
	 * @author QuSida
	 * 
	 * @return BillListPanel
	 * 
	 * @date 2010-8-20 ����10:23:09
	 */
	private BillListPanel getBillListPanel() {
		if (billListPanel == null) {
			billListPanel = new BillListPanel();
			// ����ģ��
			billListPanel.loadTemplet("0001ZZ10000000001TLB");		
			//billListPanel.getHeadBillModel().setEnabledAllItems(true);
			billListPanel.addEditListener(this);
		}
		return billListPanel;
	}

	/**
	 * @function ��ť��������
	 * 
	 * @author QuSida
	 * 
	 * @param e
	 * 
	 * @date 2010-8-20 ����10:41:37
	 */
	public void actionPerformed(ActionEvent e) {
		// ȡ����ť�Ķ���
		if (e.getSource() == this.getBtnCancel()) {
			this.m_closeMark = false;
			this.closeCancel();
		}
		// ȷ����ť����
		else if (e.getSource() == this.getBtnOK()) {
			icvos = (InformationCostVO[])getBillListPanel().getHeadBillModel().getBodyValueVOs(InformationCostVO.class.getName());
			if( icvos == null || icvos.length == 0){
				this.m_closeMark = true;
				this.closeOK();
			}
               String message = voCheck(icvos).trim();
			if(!message.equals("")){
				MessageDialog.showHintDlg(this, "��ʾ",message);
				return;
			}
//			for (int i = 0; i < icvos.length; i++) {
//				icvos[i].setStatus(nc.vo.pub.VOStatus.NEW);
//			}
			this.m_closeMark = true;
			this.closeOK();
		}
		// ���Ӱ�ť����
		else if (e.getSource() == this.m_btnAdd) {
			getBillListPanel().getHeadBillModel().addLine();
			getBillListPanel().getHeadBillModel().execLoadFormula();//add by MeiChao 2010-11-09
			getBillListPanel().getHeadBillModel().setEnabledAllItems(true);
		//add by ������ 2010-10-13 begin
			int row = getBillListPanel().getHeadTable().getRowCount();
			getBillListPanel().getHeadBillModel().setValueAt(arrnumber, row-1, "nnumber");
			getBillListPanel().getHeadBillModel().setValueAt(billtype, row-1, "vdef10");//ʹ���Զ�����vdef10 �洢��ǰ���ö�Ӧ�ĵ������� by ������ 2010-10-30
			//add by ������ 2010-10-13 end			
		}
		// ɾ����ť����
		else if (e.getSource() == this.m_btnDel) {
			int[] delRows = getBillListPanel().getHeadTable().getSelectedRows();
			getBillListPanel().getHeadBillModel().delLine(delRows);
		}
		// �޸İ�ť����
		else if (e.getSource() == this.m_btnMod) {
			getBillListPanel().getHeadBillModel().setEnabledAllItems(true);
		}

	}

	/**
	 * @function �رձ�־
	 * 
	 * @author QuSida
	 * 
	 * @return boolean
	 * 
	 * @date 2010-8-20 ����10:24:20
	 */
	public boolean isCloseOK() {
		return m_closeMark;
	}

	/**
	 * @function ���÷�����Ϣvo���飬��Ϊ�����������ã����಻Ҫʹ��
	 * 
	 * @author QuSida
	 * 
	 * @return InformationCostVO[]
	 * 
	 * @date 2010-8-20 ����10:24:46
	 */
	public InformationCostVO[] getInfoCostVOs() {
		return icvos;
	}
	private String voCheck(InformationCostVO[] icvos) {

	    for (int i = 0; i < icvos.length; i++) {
			if(icvos[i].getCostcode() == null ||icvos[i].getCostcode().length() == 0 ){
				SCMEnv.out("costcode is null");
				return "��"+i+"��,���ñ���Ϊ��";				
			}
			else if(icvos[i].getCcostunitid() == null ||icvos[i].getCcostunitid().length() == 0 ){
				SCMEnv.out("costunitid is null");
				return "��"+i+"��,���õ�λΪ��";				
			}
			else if(icvos[i].getCurrtypeid() == null ||icvos[i].getCurrtypeid().length() == 0 ){
				SCMEnv.out("currtypeid is null");
				return "��"+i+"��,����Ϊ��";				
			}
//			else if(icvos[i].getNoriginalcurprice() == null){
//				SCMEnv.out("originalcurprice is null");
//				return "��"+i+"��,��˰����Ϊ��";				
//			}
		}
	    return "";
	}

	public void afterEdit(BillEditEvent e) {
		if(e.getKey().equals("ismny")){
		if(	(Boolean)e.getValue()){
		   getBillListPanel().getHeadItem("noriginalcurprice").setValue(null);
		   getBillListPanel().getHeadItem("noriginalcurprice").setEdit(false);
		   getBillListPanel().getHeadItem("noriginalcurmny").setEdit(true);
		}else{
			 getBillListPanel().getHeadItem("noriginalcurmny").setValue(null);
			   getBillListPanel().getHeadItem("noriginalcurmny").setEdit(false);
			   getBillListPanel().getHeadItem("noriginalcurprice").setEdit(true);
		}
		   
		}
		
	}

	/**
	 * @function ���ô��������
	 * 
	 * @author ������
	 * 
	 * @return void
	 * 
	 * @date 2010-10-13
	 */
	public void setArrnumber(java.awt.Container parent) {
		if(arrnumber == null){
			arrnumber = new UFDouble(0.0);
		}
		parentCardPanel = (BillCardPanel) parent;
		int temp = parentCardPanel.getBillModel("table").getRowCount();
		for (int i = 0; i < temp; i++) {
			if("21".equals(parentCardPanel.getBillType())){//�ɹ������������
				arrnumber = arrnumber.add(new UFDouble((parentCardPanel.getBillModel("table").getValueAt(i,"nordernum")==null?"0.0":parentCardPanel.getBillModel("table").getValueAt(i,"nordernum").toString())));	
			}else if("23".equals(parentCardPanel.getBillType())){//����������
				arrnumber = arrnumber.add(new UFDouble(parentCardPanel.getBillModel("table").getValueAt(i,"narrvnum").toString()));    			    	  
				
			}
		}
		
	}
	
	/**
	 * @function ���õ�ǰ��������
	 * 
	 * @author ������
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
		
	}

}
