package nc.ui.so.so001.order;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import nc.bs.framework.common.NCLocator;
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
import nc.vo.ic.md.MdcrkVO;
import nc.vo.ic.sd.MdsdVO;
import nc.vo.pub.BusinessException;
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

	public SoMdwhPanel(SoMdwhDlg dlg, SaleorderHVO hvo, SaleorderBVO bvo) {
		super();
		this.hvo = hvo;
		this.bvo = bvo;
		this.dlg = dlg;
		initialize();
		initDate();
		// ���ǩ�ֺ����ܱ༭
		if (hvo.getFstatus() == 2)
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
		this.setSize(new Dimension(800, 400));
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
				}

		} catch (BusinessException e) {
			e.printStackTrace();
			MessageDialog
					.showWarningDlg(dlg, "��ʾ", "���ݳ�ʼ������" + e.getMessage());
		}
		// ���ǩ�ֺ����ܱ༭
		if (hvo.getFstatus() == 2)
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
			UIPanel.setPreferredSize(new Dimension(10, 30));
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
			UIButtonAdd.setBounds(new Rectangle(240, 4, 75, 20));
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
			UIButtonDel.setBounds(new Rectangle(320, 4, 75, 20));
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
			UIButtonSave.setBounds(new Rectangle(400, 4, 75, 20));
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
			UIButtonCan.setBounds(new Rectangle(480, 4, 75, 20));
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
			billCardPanel.setSize(800, 370);
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
				getOPBillCardPanel().getBillData().setBodyValueVO(vos);
				getOPBillCardPanel().getBillModel().execLoadFormula();// ��ʾ��ʽ
				buttonState(true, true, true, true);
				return;
			}
			if (new UFDouble(bjhzs).doubleValue() > vo.getDef1().doubleValue()) {
				MessageDialog.showWarningDlg(dlg, "��ʾ", "����֧�����ܴ��ڿ�����֧��"
						+ vo.getDef1().doubleValue());
				vos[arg0.getRow()].setSdzs(vo.getDef1());
				getOPBillCardPanel().getBillData().setBodyValueVO(vos);
				getOPBillCardPanel().getBillModel().execLoadFormula();// ��ʾ��ʽ
			}
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
			BillItem jbh = getOPBillCardPanel().getBodyItem("jbh");
			UIRefPane jbhPa = (UIRefPane) jbh.getComponent();
			String sqlWhere = " pk_corp='" + hvo.getPk_corp()
					+ "' and dr=0  and ccalbodyidb='" + hvo.getCcalbodyid()
					+ "'  and cinvbasid='" + bvo.getCinvbasdocid()
					+ "' and cinventoryidb='" + bvo.getCinventoryid() + "'";
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
