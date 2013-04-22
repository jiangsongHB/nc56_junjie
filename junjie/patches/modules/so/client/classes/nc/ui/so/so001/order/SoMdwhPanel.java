package nc.ui.so.so001.order;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import nc.bs.framework.common.NCLocator;
import nc.itf.gl.accbook.IBillModel;
import nc.itf.uap.IVOPersistence;
import nc.ui.ic.mdck.MdProcessBean;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIButton;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillEditListener;
import nc.ui.pub.bill.BillEditListener2;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillModel;
import nc.ui.so.so001.panel.SaleBillUI;
import nc.ui.so.so001.revise.SaleOrderReviseUI;
import nc.vo.fp.combase.pub01.IBillStatus;
import nc.vo.ic.md.MdcrkVO;
import nc.vo.ic.sd.MdsdVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.so.so001.SaleorderBVO;
import nc.vo.so.so001.SaleorderHVO;

public class SoMdwhPanel extends UIPanel implements ActionListener,
		BillEditListener, BillEditListener2 {

	SoMdwhDlg dlg = null;

	private UIPanel UIPanel = null;

	private UIButton UIButtonAdd = null;

	private UIButton UIButtonDel = null;

	private UIButton UIButtonSave = null;

	private UIButton UIButtonCan = null;

	private BillCardPanel billCardPanel = null;

	private String m_pkcorp = null;

	private String m_pkuser = null;

	private SaleorderHVO hvo;

	private SaleorderBVO bvo;
	
	//chenjianhua 2013-04-11
	private SaleBillUI saleOrderAdminUI;

	public SoMdwhPanel(SoMdwhDlg dlg, SaleorderHVO hvo, SaleorderBVO bvo, SaleBillUI saleOrderAdminUI) {
		super();
		this.hvo = hvo;
		this.bvo = bvo;
		this.dlg = dlg;
		//chenjianhua 2013-04-11
		this.saleOrderAdminUI=saleOrderAdminUI;
		
		initialize();
		initDate();
		// ���ǩ�ֺ����ܱ༭
		if (hvo.getFstatus() == 2 &&!(saleOrderAdminUI instanceof SaleOrderReviseUI))
			buttonState(false, false, false, true);
		else
			buttonState(true, true, false, true);

	}

	

	/**
	 * This method initializes this
	 * 
	 */
	private void initialize() {
		this.setLayout(new BorderLayout());
		this.setSize(new Dimension(1024, 700));
		this.add(getOPBillCardPanel(), BorderLayout.CENTER);
		this.add(getUIPanel(), BorderLayout.SOUTH);
		getUIButtonAdd().addActionListener(this);
		getUIButtonDel().addActionListener(this);
		getUIButtonSave().addActionListener(this);
		getUIButtonCan().addActionListener(this);
		getOPBillCardPanel().setEnabled(false);
		getOPBillCardPanel().addEditListener(this);
		getOPBillCardPanel().addBodyEditListener2(this);
		m_pkcorp = ClientEnvironment.getInstance().getCorporation()
				.getPk_corp();
		m_pkuser = ClientEnvironment.getInstance().getUser().getPrimaryKey();
	}

	private void initDate() {
		// ʼʼ��ֵ
		getOPBillCardPanel().setHeadItem("invcode", bvo.getCinvbasdocid());
		getOPBillCardPanel().execHeadLoadFormulas();
		MdsdVO[] mdsdVos = null;
		try {
			mdsdVos = new MdsdBean().querySdVOS(bvo);
			if (mdsdVos != null && mdsdVos.length > 0)
				// �����ִ���
				for (int i = 0; i < mdsdVos.length; i++) {
					MdsdVO sdvo = (MdsdVO) mdsdVos[i].clone();
					MdsdVO kylvo = new MdsdBean().queryKyl(sdvo, mdsdVos[i]
							.getSxrq());
					mdsdVos[i].setDef1(mdsdVos[i].getSdzs()
							.add(kylvo.getDef1()));// �ִ�֧��
					// �Ƿ�Ǽ���
					if (mdsdVos[0].getDef4().booleanValue() == true)
						getOPBillCardPanel().setHeadItem("fjs",
								new UFBoolean(true));
				}

		} catch (BusinessException e) {
			e.printStackTrace();
			MessageDialog
					.showWarningDlg(dlg, "��ʾ", "���ݳ�ʼ������" + e.getMessage());
		}
		// ���ǩ�ֺ����ܱ༭
		if (hvo.getFstatus() == 2&&!(saleOrderAdminUI instanceof SaleOrderReviseUI))
			getOPBillCardPanel().setEnabled(false);
		else
			getOPBillCardPanel().setEnabled(true);
		if (mdsdVos == null || mdsdVos.length == 0) {
			onBtnAdd();
		} else {
			getOPBillCardPanel().getBillModel().setBodyDataVO(mdsdVos);
			getOPBillCardPanel().getBillModel().execLoadFormula();// ��ʾ��ʽ
		}
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
			UIPanel.add(getUIButtonAdd(), null);
			UIPanel.add(getUIButtonDel(), null);
			UIPanel.add(getUIButtonSave(), null);
			UIPanel.add(getUIButtonCan(), null);
		}
		return UIPanel;
	}

	/**
	 * This method initializes UIButtonAdd
	 * 
	 * @return nc.ui.pub.beans.UIButton
	 */
	private UIButton getUIButtonAdd() {
		if (UIButtonAdd == null) {
			UIButtonAdd = new UIButton();
			UIButtonAdd.setBounds(new Rectangle(352, 4, 75, 20));
			UIButtonAdd.setText("�� ��");
			UIButtonAdd.setToolTipText("<HTML><B>����(CTRL + L)</B></HTML>");
		}
		return UIButtonAdd;
	}

	/**
	 * This method initializes UIButtonDel
	 * 
	 * @return nc.ui.pub.beans.UIButton
	 */
	private UIButton getUIButtonDel() {
		if (UIButtonDel == null) {
			UIButtonDel = new UIButton();
			UIButtonDel.setBounds(new Rectangle(432, 4, 75, 20));
			UIButtonDel.setText("ɾ  ��");
			UIButtonDel.setToolTipText("<HTML><B>ɾ��(CTRL + D)</B></HTML>");
		}
		return UIButtonDel;
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
			UIButtonSave.setToolTipText("<HTML><B>����(CTRL + S)</B></HTML>");
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
			UIButtonCan.setToolTipText("<HTML><B>ȡ��(CTRL + X)</B></HTML>");
		}
		return UIButtonCan;
	}

	/**
	 * This method initializes BombillCardPanel
	 * 
	 * @return nc.ui.pub.bill.BillCardPanel
	 */
	private BillCardPanel getOPBillCardPanel() {
		if (billCardPanel == null) {
			billCardPanel = new BillCardPanel();
			billCardPanel.setSize(1024, 650);
			billCardPanel.loadTemplet("H003", null, m_pkuser, m_pkcorp);
			billCardPanel.setTatolRowShow(true);
		}
		return billCardPanel;
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(getUIButtonAdd())) {
			onBtnAdd();
		}
		if (e.getSource().equals(getUIButtonDel())) {
			onBtnDel();
		}
		if (e.getSource().equals(getUIButtonSave())) {
			onBtnSave();
		}
		if (e.getSource().equals(getUIButtonCan())) {
			onBtnCan();
		}
	}

	/**
	 * ���Ӱ�ť�¼�����
	 */
	private void onBtnAdd() {
		// getOPBillCardPanel().setEnabled(true);
		getOPBillCardPanel().getBillModel().addLine();
		buttonState(true, true, true, true);
	}

	/**
	 * ɾ����ť�¼�����
	 */
	private void onBtnDel() {
		getOPBillCardPanel().tableStopCellEditing();
		getOPBillCardPanel().delLine();
		buttonState(true, true, true, true);
	}

	/**
	 * ���水ť�¼�����
	 */
	private void onBtnSave() {
		// ��ȡ��������
		MdsdVO[] vos = (MdsdVO[]) getOPBillCardPanel().getBillData()
				.getBodyValueVOs(nc.vo.ic.sd.MdsdVO.class.getName());
		IVOPersistence iVOPersistence = (IVOPersistence) NCLocator
				.getInstance().lookup(IVOPersistence.class.getName());
		try {
			String fjsStr = getOPBillCardPanel().getHeadItem("fjs").getValue();
			boolean fjs_boolean = false;
			if (fjsStr.equals("true"))
				fjs_boolean = true;
			UFDouble sum_sdzs = new UFDouble(0);
			
			UFDouble sum_weight=UFDouble.ZERO_DBL;//��������  chenjianhua 2013-04-11
			
			
			for (int i = 0; i < vos.length; i++) {
				if (vos[i].getSdzs() == null)
					throw new BusinessException("��" + (i + 1) + "����������Ų���Ϊ��!");
				if (vos[i].getSdzs() == null)
					throw new BusinessException("��" + (i + 1) + "������֧������Ϊ��!");
				sum_sdzs = sum_sdzs.add(vos[i].getSdzs());
				vos[i].setDef4(new UFBoolean(fjs_boolean));
				
				sum_weight=sum_weight.add(vos[i].getDef3());//��������  chenjianhua 2013-04-11
			}
			// npacknumber ������
			if (sum_sdzs.doubleValue() > bvo.getNpacknumber().doubleValue())
				throw new BusinessException("����֧��" + sum_sdzs.doubleValue()
						+ "���ܴ������۶���������" + bvo.getNpacknumber().doubleValue()
						+ "������ʧ�ܣ�");
			buttonState(true, true, false, true);
			MdsdBean bean = new MdsdBean();
			MdsdVO[] rsvos = bean.buliderMdcrkVOs(vos, hvo, bvo);
			// ɾ���뵥������ʷ����
			boolean ders = bean.deleteAndRK(bvo);
			// ���浽����ⵥλ����
			if (rsvos == null && ders == false)
				throw new BusinessException("����ʧ�ܣ�û���뵥��ϸ��");
			if (rsvos == null && ders == true)
				throw new BusinessException("�뵥��ϸȫ��ɾ���ɹ���");
			iVOPersistence.insertVOArray(rsvos);
			// getUIButtonCan().setText("�� ��");
			MessageDialog.showWarningDlg(dlg, "��ʾ", "����ɹ���");
			onBtnCan();
			
			
			//chenjianhua 2013-04-11  �������۶�������ļ����Ͷ���	
			boolean isChange=false;//���������Ƿ�仯��
			int slectedRow= 
				saleOrderAdminUI.getBillCardPanel().getBodyPanel().getTable().getSelectedRow();//���ѡ����к�
			if(sum_weight.compareTo(bvo.getNnumber())!=0){
				//����
				this.saleOrderAdminUI.getBillCardPanel().setBodyValueAt(sum_weight, slectedRow, "nnumber");	
				bvo.setNnumber(sum_weight);
				isChange=true;
			}
			if(sum_sdzs.compareTo(bvo.getNpacknumber())!=0){
			   //����
			   this.saleOrderAdminUI.getBillCardPanel().setBodyValueAt(sum_sdzs, slectedRow, "npacknumber");
			   bvo.setNpacknumber(sum_sdzs);
			   isChange=true;
			}
			if(isChange){
				if(saleOrderAdminUI instanceof SaleOrderAdminUI){
					this.saleOrderAdminUI.strState="�޸�";
					saleOrderAdminUI.getBillCardPanel().getBillModel().setRowState(slectedRow, BillModel.MODIFICATION);
				}else if(saleOrderAdminUI instanceof SaleOrderReviseUI){
					((SaleOrderReviseUI)saleOrderAdminUI).onModification();
					saleOrderAdminUI.getBillCardPanel().getBillModel().setRowState(slectedRow, BillModel.MODIFICATION);
					saleOrderAdminUI.getBillCardPanel().getBillModel().getBodyValueRowVO(slectedRow, SaleorderBVO.class.getName()).setStatus(VOStatus.UPDATED);
				}
				
				
				BillEditEvent be = new BillEditEvent(saleOrderAdminUI.getBillCardPanel()//�������༭�¼�
				          .getBodyItem("nnumber"),saleOrderAdminUI.getBillCardPanel().getBillModel().getValueAt(slectedRow, "nnumber"),"nnumber",slectedRow,BillItem.BODY);
				saleOrderAdminUI.getBillCardPanel().afterEdit(be);
				if(saleOrderAdminUI instanceof SaleOrderAdminUI){
					((SaleOrderAdminUI)saleOrderAdminUI).onSave();
				}else if(saleOrderAdminUI instanceof SaleOrderReviseUI){
					((SaleOrderReviseUI)saleOrderAdminUI).onSave();
					
				}
				saleOrderAdminUI.setButtonsState();
				
			}		
			//end 2013-04-11
			
		} catch (BusinessException e) {
			e.printStackTrace();
			MessageDialog.showWarningDlg(dlg, "��ʾ", e.getMessage());
		}
	}

	/**
	 * ȡ����ť�¼�����
	 */
	private void onBtnCan() {
		dlg.closeOK();
	}

	public void afterEdit(BillEditEvent arg0) {
		if (arg0.getKey().equals("jbh")) {
			// ִ�����б༭��ʽ
			String[] s = getOPBillCardPanel().getBodyItem(arg0.getKey())
					.getEditFormulas();
			if (s != null && s.length > 0) {
				getOPBillCardPanel().execBodyFormulas(arg0.getRow(), s);
			}
			MdsdVO[] vos = (MdsdVO[]) getOPBillCardPanel().getBillData()
					.getBodyValueVOs(nc.vo.ic.sd.MdsdVO.class.getName());
			MdsdVO vo = vos[arg0.getRow()]; // �༭��VO
			try {
				UFDate cdate = ClientEnvironment.getInstance().getDate();
				MdsdVO kylVo = new MdsdBean().queryKyl(vo, cdate);
				vos[arg0.getRow()] = kylVo;
				getOPBillCardPanel().getBillModel().setBodyDataVO(vos);
				getOPBillCardPanel().getBillModel().execLoadFormula();// ��ʾ��ʽ
				buttonState(true, true, true, true);
			} catch (BusinessException e) {
				e.printStackTrace();
				MessageDialog.showWarningDlg(dlg, "��ʾ", "��������ѯ����!");
				return;
			}
		}
		if (arg0.getKey().equals("sdzs")) {
			MdsdVO[] vos = (MdsdVO[]) getOPBillCardPanel().getBillData()
					.getBodyValueVOs(nc.vo.ic.sd.MdsdVO.class.getName());
			MdsdVO vo = vos[arg0.getRow()];
			UFDate cdate = ClientEnvironment.getInstance().getDate();
			// �༭���֧��
			String bjhzs = (String) arg0.getValue();
			if (bjhzs == null || bjhzs.equals("")
					|| new UFDouble(bjhzs).doubleValue() == 0) {
				MessageDialog.showWarningDlg(dlg, "��ʾ", "����֧������Ϊ�ջ�Ϊ0!");
				vos[arg0.getRow()].setSdzs(vo.getDef1());
				vos[arg0.getRow()].setDef3(vo.getDef2());
				getOPBillCardPanel().getBillData().setBodyValueVO(vos);
				getOPBillCardPanel().getBillModel().execLoadFormula();// ��ʾ��ʽ
				buttonState(true, true, true, true);
				return;
			}
			if (new UFDouble(bjhzs).doubleValue() > vo.getDef1().doubleValue()) {
				MessageDialog.showWarningDlg(dlg, "��ʾ", "����֧�����ܴ��ڿ�����֧��"
						+ vo.getDef1().doubleValue());
				vos[arg0.getRow()].setSdzs(vo.getDef1());
				vos[arg0.getRow()].setDef3(vo.getDef2());
				getOPBillCardPanel().getBillData().setBodyValueVO(vos);
				getOPBillCardPanel().getBillModel().execLoadFormula();// ��ʾ��ʽ
				return;
			}
			UFDouble sdzs = new UFDouble(bjhzs); // ����֧��
			UFDouble sdzl = sdzs.multiply(vo.getDef2()).div(vo.getDef1(), 4); // ��������
			vos[arg0.getRow()].setSdzs(sdzs); // ����֧��
			vos[arg0.getRow()].setDef3(sdzl);
			getOPBillCardPanel().getBillData().setBodyValueVO(vos);
			getOPBillCardPanel().getBillModel().execLoadFormula();// ��ʾ��ʽ
			buttonState(true, true, true, true);
		}
		// ������˷Ǽ���
		if (arg0.getKey().equals("fjs")) {
			getOPBillCardPanel().getBillData().setBodyValueVO(null);
			// ����
			onBtnAdd();
			buttonState(true, true, true, true);
		}
		if (arg0.getKey().equals("sxrq")) {
			buttonState(true, true, true, true);
		}
	}

	public void bodyRowChange(BillEditEvent arg0) {
		// TODO Auto-generated method stub

	}

	public boolean beforeEdit(BillEditEvent e) {
		if (e.getKey().equals("jbh")) {
			String fsjStr = getOPBillCardPanel().getHeadItem("fjs").getValue();
			BillItem jbh = getOPBillCardPanel().getBodyItem("jbh");
			UIRefPane jbhPa = (UIRefPane) jbh.getComponent();
			String sqlWhere = " pk_corp='" + hvo.getPk_corp()
					+ "' and dr=0  and ccalbodyidb='" + hvo.getCcalbodyid()
					+ "'  and cinvbasid='" + bvo.getCinvbasdocid()
					+ "' and cinventoryidb='" + bvo.getCinventoryid() + "'";			
			if (fsjStr.equals("true"))
				sqlWhere += " and def4='Y'";
			else
				sqlWhere += " and def4='N'";			
			jbhPa.setWhereString(sqlWhere);
		}
		return true;

	}

	public void buttonState(boolean bolean_AddLine, boolean bolean_DelLine,
			boolean bolean_Save, boolean bolean_Cancel) {
		getUIButtonAdd().setEnabled(bolean_AddLine);
		getUIButtonDel().setEnabled(bolean_DelLine);
		getUIButtonSave().setEnabled(bolean_Save);
		getUIButtonCan().setEnabled(bolean_Cancel);
	}

}
