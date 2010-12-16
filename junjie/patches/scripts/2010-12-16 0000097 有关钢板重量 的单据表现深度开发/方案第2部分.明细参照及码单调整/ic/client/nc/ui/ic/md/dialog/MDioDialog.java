package nc.ui.ic.md.dialog;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import nc.bs.framework.common.NCLocator;
import nc.itf.ic.md.IMDTools;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ArrayProcessor;
import nc.ui.ic.mdck.MDConstants;
import nc.ui.ic.pub.bill.Environment;
import nc.ui.ic.pub.bill.GeneralBillClientUI;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIButton;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UILabel;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillEditListener;
import nc.ui.pub.bill.BillEditListener2;
import nc.ui.pub.bill.BillItem;
import nc.ui.trade.business.HYPubBO_Client;
import nc.uif.pub.exception.UifException;
import nc.vo.ic.md.MdcrkVO;
import nc.vo.ic.pub.bill.GeneralBillItemVO;
import nc.vo.ic.pub.bill.GeneralBillVO;
import nc.vo.ic.xcl.MdxclVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.constant.ic.BillMode;
import nc.vo.scm.constant.ic.InOutFlag;

/**
 * �뵥��ʾ����
 * 
 * @author zhoujie
 * @since 2010-09-02 17:34:34
 */
public class MDioDialog extends UIDialog implements ActionListener,
		BillEditListener, BillEditListener2 {

	private static final long serialVersionUID = 1L;

	GeneralBillClientUI ui;

	BillCardPanel cardPanel;

	UIPanel buttonPanel;

	UILabel bottomPanel;

	int panelStatus;

	int status;

	boolean canloaddata = false;

	boolean edited = false;
	//2010-12-13 MeiChao  begin ��� �ֳ����� 
	public UFDouble factoryweight= new UFDouble(0);

	public UFDouble getFactoryweight() {
		return factoryweight;
	}

	public void setFactoryweight(UFDouble factoryweight) {
		this.factoryweight = factoryweight;
	}
	//2010-12-13 MeiChao end ��� �ֳ�����

	public UFDouble ssfsl = new UFDouble(0);// ʵ�ո�����

	public UFDouble sssl = new UFDouble(0);// ʵ������

	public UFDouble nprice = new UFDouble(0);// ʵ��ⵥ��

	public UFDouble nmny = new UFDouble(0);// ʵ����

	public UFDouble grossprice = new UFDouble(0);// ë�ߵ���

	public UFDouble grossweight = new UFDouble(0);// ë������

	public UFDouble grosssumny = new UFDouble(0);// ë�߽��

	public UFDouble stuffprice = new UFDouble(0);// ���ĵ���

	public UFDouble stuffweight = new UFDouble(0);// ��������

	public UFDouble stuffsumny = new UFDouble(0);// ���Ľ��

	public UFBoolean sfsqmd = new UFBoolean(false); // �Ƿ�ɾ���뵥

	private String md_note;// ��*��*��

	private String md_zyh;// ��Դ��

	private boolean sfth = false;// �Ƿ��˻�

	private String vfree1;// ������1

	public MDioDialog(GeneralBillClientUI ui, boolean sfth)
			throws BusinessException {
		super(ui, MDUtils.getBillNameByBilltype(ui.getBillType()) + "���뵥ά��");
		this.ui = ui;
		this.setSize(1024, 700);
		this.sfth = sfth;
		init();
	}

	GeneralBillVO getGeneralBillVO() {
		GeneralBillVO nowVObill = null;
		int rownow = -1;
		if (ui.getM_iCurPanel() == BillMode.Card) {
			nowVObill = ui.getM_voBill();
		} else {
			nowVObill = (GeneralBillVO) ui.getM_alListData().get(
					ui.getM_iLastSelListHeadRow());
		}
		// �Ƿ��˻�
		if (sfth == true) {
			GeneralBillItemVO[] bvos = (GeneralBillItemVO[]) nowVObill
					.getChildrenVO();
			if (bvos != null && bvos.length > 0) {
				for (int i = 0; i < bvos.length; i++) {
					bvos[i].setNinassistnum(new UFDouble(-bvos[i]
							.getNoutassistnum().doubleValue()));
					bvos[i].setNinnum(new UFDouble(-bvos[i].getNoutnum()
							.doubleValue()));
				}
			}
		}

		return nowVObill;
	}

	int getGenSelectRowID() {
		int rownow = -1;
		if (ui.getM_iCurPanel() == BillMode.Card) {
			rownow = ui.getBillCardPanel().getBillTable().getSelectedRow();
		} else {
			rownow = ui.getBillListPanel().getBodyTable().getSelectedRow();
		}
		return rownow;
	}

	MdxclVO mdxclvo;

	/**
	 * ��ʼ����ͷ����
	 */
	private void initHeadData() throws BusinessException {
		// voname nc.vo.ic.pub.bill.GeneralBillItemVO
		GeneralBillVO nowVObill = getGeneralBillVO();
		// String WhID = "";
		String InvID = "";
		if (nowVObill != null && getGenSelectRowID() >= 0) {
			// WhID = (String)nowVObill.getHeaderValue("cwarehouseid");
			InvID = (String) nowVObill.getItemValue(getGenSelectRowID(),
					"cinventoryid");
		}
		getBillCardPanel().getHeadItem("invpk").setValue(InvID);// �������PK
		getBillCardPanel().getHeadItem("realWeight").setValue(
				getGeneralBillVO().getItemValue(getGenSelectRowID(), "ninnum"));// ʵ������
		getBillCardPanel().execHeadLoadFormulas();

		// ��ʼ����Դ�š���*��*��
		GeneralBillItemVO itemvo = (GeneralBillItemVO) nowVObill
				.getChildrenVO()[getGenSelectRowID()];
		this.setMd_note(itemvo.getVuserdef2()); // �������ֶ�ȷ��
		this.setMd_zyh(itemvo.getVuserdef1()); // �������ֶ�ȷ��
		this.setVfree1(itemvo.getVfree1());// ������
		// ʵ������
		// this.setSssl((UFDouble) (getGeneralBillVO().getItemValue(
		// getGenSelectRowID(), "ninnum")));
		// ʵ�븨����
		this.setSsfsl((UFDouble) (getGeneralBillVO().getItemValue(
				getGenSelectRowID(), "ninassistnum")));
		if (this.getSsfsl() == null || this.getSsfsl().doubleValue() == 0)
			throw new BusinessException("ʵ�ո�����Ϊ�գ�����ά���뵥��");

		String gg = (String) getBillCardPanel().getHeadItem("thickness")
				.getValueObject();// ���
		UFDouble g;
		try {
			g = new UFDouble(gg);
		} catch (Exception e) {
			setMessage("�ô�����ڹ���ԭ��,����ά�����ȺͿ��");
			getBillCardPanel().getBodyItem("md_width").setEdit(false);
			getBillCardPanel().getBodyItem("md_length").setEdit(false);
		}

		mdxclvo = new MdxclVO();
		mdxclvo.setDr(0);
		mdxclvo.setPk_corp(getEnvironment().getCorpID());
		mdxclvo.setCcalbodyidb((String) nowVObill.getHeaderValue("pk_calbody")); // �����֯
		mdxclvo.setCwarehouseidb((String) nowVObill
				.getHeaderValue("cwarehouseid"));// �ֿ�
		mdxclvo.setCinvbasid((String) nowVObill.getItemValue(
				getGenSelectRowID(), "cinvbasid"));// ��������
		mdxclvo.setCinventoryidb(InvID);// ���.

		// ��ʼ�����ĵ���,����������ĵ���Ϊ�գ���ȡ�����еĵ��ۡ������Ϊ�գ���һֱȡ���ĵ���
		GeneralBillItemVO itemVOa = ((GeneralBillItemVO[]) nowVObill
				.getChildrenVO())[getGenSelectRowID()];
		UFDouble stuffprice = itemVOa.getStuffprice();
		if (stuffprice == null || stuffprice.doubleValue() == 0) {
			UFDouble nprice = (UFDouble) nowVObill.getItemValue(
					getGenSelectRowID(), "nprice");
			if (nprice == null)
				nprice = new UFDouble(0);
			this.setStuffprice(nprice);
		} else
			this.setStuffprice(stuffprice);
		// ��ʼ��ë�ߵ���
		UFDouble grossprice = itemVOa.getGrossprice();
		if (grossprice != null && grossprice.doubleValue() != 0)
			getBillCardPanel().setHeadItem("grossprice", grossprice);
	}

	/**
	 * ��ʼ��
	 */
	private void init() throws BusinessException {
		this.add(getBillCardPanel(), BorderLayout.CENTER);
		this.add(getButtonPanel(), BorderLayout.SOUTH);
		// this.add(getBottomPanel(), BorderLayout.SOUTH);BorderLayout.NORTH
		this.initData();
	}

	/**
	 * ����״̬����Ϣ��
	 * 
	 * @param message
	 */
	void setMessage(String message) {
		getBottomPanel().setText("  " + message);
	}

	/**
	 * ��ʼ������
	 */
	private void initData() throws BusinessException {
		this.initPanelStatus();
		this.initHeadData();
		this.initBodyData();
		if (this.status == MDUtils.INIT_CANEDIT) {
			String cwarehouseid = (String) getGeneralBillVO().getHeaderValue(
					"cwarehouseid");
			UIRefPane panel = (UIRefPane) getBillCardPanel().getBodyItem("box")
					.getComponent();
			panel.getRefModel().addWherePart(
					" and bd_stordoc.pk_stordoc='" + cwarehouseid + "'");
		}
	}

	/**
	 * ��ʼ������״̬
	 */
	void initPanelStatus() {
		if (ui.getM_iMode() == BillMode.Browse) {
			if (getGeneralBillVO() == null) {
				setMessage("���ݽ���û������,�޷�����...");
				setStatus(MDUtils.INIT);
			} else if (getGeneralBillVO().getItemVOs() == null) {
				setMessage("���ݱ���û������,�޷�����...");
				setStatus(MDUtils.INIT);
			} else if (getGenSelectRowID() == -1) {
				setMessage("���ݱ���û��ѡ������,�޷�����...");
				setStatus(MDUtils.INIT);
			} else {
				canloaddata = true;
				if (getGeneralBillVO().getHeaderValue("fbillflag").equals(2)) {
					setMessage("");
					setStatus(MDUtils.INIT_CANEDIT);
				} else {
					setMessage("���ݷ�����̬,�޷��༭�뵥��Ϣ...");
					setStatus(MDUtils.INIT);
				}
			}
		} else {
			setMessage("ֻ�����������ʱ�ſ���ά���뵥��Ϣ...");
			setStatus(MDUtils.INIT);
		}
	}

	/*
	 * /** ��д���ڹرհ�ť ʹ֮ʧЧ
	 */
	/*
	 * @Override protected void processWindowEvent(WindowEvent e) { }
	 */

	/**
	 * ��ʼ����������
	 */
	private void initBodyData() {
		if (canloaddata) {
			// getGeneralBillVO().getItemValue(getGenSelectRowID(),
			// "cgeneralbid")
			try {
				MdcrkVO[] vos = (MdcrkVO[]) HYPubBO_Client.queryByCondition(
						MdcrkVO.class, " isnull(dr,0)=0 and cgeneralbid='"
								+ getGeneralBillVO().getItemValue(
										getGenSelectRowID(), "cgeneralbid")
								+ "'");
				if (vos != null && vos.length > 0) {
					getBillCardPanel().getBillModel().setBodyDataVO(vos);
					getBillCardPanel().getBillModel().execLoadFormula();
				}else{//2010-12-01 MeiChao begin��Ӵ�else�ж�,��ǰ���Ѵ����뵥��Ϣʱ.
					//����ȥѰ����ͬ���ε��ݵĶ�Ӧ�������ⵥ��Ӧ�ı������µ��뵥��Ϣ.(�е���)
					vos = (MdcrkVO[]) HYPubBO_Client.queryByCondition(
							MdcrkVO.class, " isnull(dr,0)=0 and cgeneralbid=(select t.cgeneralbid " +
									"from ic_general_b t where t.csourcebillbid=" +
									"(select t.csourcebillbid from ic_general_b t where t.cgeneralbid='" 
									+ getGeneralBillVO().getItemValue(
											getGenSelectRowID(), "cgeneralbid")
									+ "' and t.dr=0) and t.cbodybilltypecode='4I' and t.dr=0)");
					if (vos != null && vos.length > 0) {
						//����ɹ���ѯ����Ӧ���뵥VO,��ô���䵱�е�ĳ�����޸ĵ�: 
						//pk_mdcrk,cbodybilltypecode,pk_mdxcl_b,cgeneralbid,cwarehouseidb,cspaceid,jbh,voperatorid
						for(int i=0;i<vos.length;i++){
							vos[i].setPk_mdcrk(null);//�뵥PK����Ϊ��
							vos[i].setCbodybilltypecode(this.ui.getBillTypeCode());
							vos[i].setPk_mdxcl_b(null);
							vos[i].setCgeneralbid(getGeneralBillVO().getItemValue(getGenSelectRowID(), "cgeneralbid").toString());
							vos[i].setCwarehouseidb(getGeneralBillVO().getHeaderVO().getCwarehouseid());
							vos[i].setCspaceid(null);
							//vos[i].setJbh(null);
							vos[i].setVoperatorid(ClientEnvironment.getInstance().getUser().getPrimaryKey());
						}
					getBillCardPanel().getBillModel().setBodyDataVO(vos);
					getBillCardPanel().getBillModel().execLoadFormula();
					}
				}
				//2010-12-01 MeiChao end
			} catch (UifException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * ���ð�ť�뵥��״̬
	 * 
	 * @param status
	 * @see nc.ui.ic.md.dialog.MDUtils#INIT
	 * @see nc.ui.ic.md.dialog.MDUtils#INIT_CANEDIT
	 * @see nc.ui.ic.md.dialog.MDUtils#EDITING
	 */
	void setStatus(int status) {
		edited = false;
		this.status = status;
		switch (status) {
		case MDUtils.INIT:
			setPanelEnable(false);
			System.out.println("��ʼ���ɱ༭...");
			break;
		case MDUtils.INIT_CANEDIT:
			setPanelEnable(false);
			getUIButton(MDUtils.EDIT_BUTTON).setEnabled(true);
			System.out.println("�ɱ༭״̬...");
			break;
		case MDUtils.EDITING:
			setPanelEnable(true);
			getUIButton(MDUtils.EDIT_BUTTON).setEnabled(false);
			setMessage("ά���뵥��Ϣ");
			break;
		default:
			break;
		}
	}

	public UILabel getBottomPanel() {
		if (bottomPanel == null) {
			bottomPanel = new UILabel();
			bottomPanel.setPreferredSize(new Dimension(1024, 20));
			// bottomPanel.setBorder(BorderFactory.createLoweredBevelBorder());
			bottomPanel.setRequestFocusEnabled(false);
		}
		return bottomPanel;
	}

	UIButton getUIButton(String name) {
		for (Component com : getButtonPanel().getComponents()) {
			if (com instanceof UIButton) {
				UIButton button = (UIButton) com;
				if (button.getText().equals(name)) {
					return button;
				}
			}
		}
		return MDUtils.BLANK_BUTTON;
	}

	void setPanelEnable(boolean isEnadble) {
		for (Component com : getButtonPanel().getComponents()) {
			if (com instanceof UIButton) {
				UIButton button = (UIButton) com;
				if (!button.getText().equals(MDUtils.CANCEL_BUTTON)) {
					button.setEnabled(isEnadble);
				}
			}
		}
		getBillCardPanel().setEnabled(isEnadble);
	}

	/**
	 * ��ʼ����ť���
	 * 
	 * @see nc.ui.ic.md.dialog.MDUtils#getButtons()
	 * @return ӵ�а�ť�����
	 */
	public UIPanel getButtonPanel() {
		if (buttonPanel == null) {
			buttonPanel = new UIPanel();
			// buttonPanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
			buttonPanel.setPreferredSize(new Dimension(1024, 50));
			for (String button_code : MDUtils.getButtons()) {
				UIButton button = new UIButton(button_code);
				buttonPanel.add(button);
				button.addActionListener(this);

			}
		}
		return buttonPanel;
	}

	BillCardPanel getBillCardPanel() {
		if (cardPanel == null) {
			cardPanel = new BillCardPanel();

			cardPanel.loadTemplet("MD01", "MD01", getEnvironment().getUserID(),
					getEnvironment().getCorpID());
			cardPanel.setRowNOShow(true);
			cardPanel.setTatolRowShow(true);
			// cardPanel.setShowMenuBar(true);
			cardPanel.addEditListener(this);
			cardPanel.addBillEditListenerHeadTail(this);
			cardPanel.addBodyEditListener2(this);
			cardPanel.setSize(new Dimension(1024, 650));
			// cardPanel.addLine();
		}
		return cardPanel;
	}

	private Environment getEnvironment() {
		if (ui != null) {
			return ui.getEnvironment();
		}
		return null;
	}

	public void actionPerformed(ActionEvent e) {
		try {
			if (e.getSource() instanceof UIButton) {
				UIButton button = (UIButton) e.getSource();
				if (button.getText().equals(MDUtils.ADDLINE_BUTTON)) {
					onAddline();
				} else if (button.getText().equals(MDUtils.DELLINE_BUTTON)) {
					onDelline();
				} else if (button.getText().equals(MDUtils.SAVE_BUTTON)) {
					onSave();
				} else if (button.getText().equals(MDUtils.CALC_BUTTON)) {
					onCalc();
				} else if (button.getText().equals(MDUtils.CANCEL_BUTTON)) {
					if (this.status == MDUtils.EDITING) {
						int i = MessageDialog.showYesNoDlg(this, "��ʾ",
								"����ά���뵥��Ϣ,�Ƿ�Ҫȡ������?");
						if (i == this.ID_YES) {
							closeCancel();
						}
					} else
						closeCancel();
				} else if (button.getText().equals(MDUtils.EDIT_BUTTON)) {
					onEdit();
				} else {
					onElse(button);
				}
			}
		} catch (Exception e2) {
			e2.printStackTrace();
			MessageDialog.showErrorDlg(this, "�쳣", e2.getMessage());
		}
	}

	@Override
	public void closeCancel() {
		super.closeCancel();
		// ˢ�½���
		// ui.getButtonManager().onButtonClicked(ui.getButtonManager().getButton(ICButtonConst.BTN_BROWSE_REFRESH));
	}

	private void onEdit() {
		setStatus(MDUtils.EDITING);
		GeneralBillVO nowVObill = getGeneralBillVO();
		String InvID = "";
		if (nowVObill != null && getGenSelectRowID() >= 0) {
			// WhID = (String)nowVObill.getHeaderValue("cwarehouseid");
			InvID = (String) nowVObill.getItemValue(getGenSelectRowID(),
					"cinventoryid");
		}
		// ��ʼ��ë�ߵ��������
		boolean sfmbjs;
		try {
			sfmbjs = CheckSfmbjs(InvID);
			if (sfmbjs == true)
				getBillCardPanel().getHeadItem("grossprice").setEdit(true);
			else
				getBillCardPanel().getHeadItem("grossprice").setEdit(false);
		} catch (BusinessException e) {
			e.printStackTrace();
		}

	}

	private void onDelline() {
		getBillCardPanel().delLine();

		setMessage("ɾ��һ������...");
		setZLNULl();
		edited = true;
	}

	private void onElse(UIButton button) {

	}

	private void onCalc() throws BusinessException {
		String ispj = (String) getBillCardPanel().getHeadItem("ispj")
				.getValueObject();
		MdcrkVO[] mdvos = getBodyVOs();
		if (mdvos.length < 1)
			throw new BusinessException("�뵥����û�����ݣ�");
		// ��������
		UFDouble sum_srkzs = new UFDouble(0);
		boolean ispjboolean = ispj.equals("true");
		for (int i = 0; i < mdvos.length; i++) {
			mdvos[i].setDef4(new UFBoolean(false)); // �Ƿ�Ǽ���
			mdvos[i].setSfbj(new UFBoolean(ispjboolean)); // �Ƿ����
			if (mdvos[i].getSrkzs() == null
					|| mdvos[i].getSrkzs().doubleValue() == 0)
				throw new BusinessException("��" + (i + 1) + "�У�֧������Ϊ�գ�");
			sum_srkzs = sum_srkzs.add(mdvos[i].getSrkzs());
		}
		if (sum_srkzs.doubleValue() != this.getSsfsl().doubleValue())
			throw new BusinessException("�뵥�����֧��" + sum_srkzs.doubleValue()
					+ "������ʵ��⸨����" + this.getSsfsl().doubleValue());
		UFDouble num = new UFDouble((String) getBillCardPanel().getHeadItem(
				"realWeight").getValueObject());
		if (ispj == null || ispj.equals("false")) {
			// �����������
			mdvos = MDUtils.mdLJ(mdvos);
			// �жϵ�ǰ����Ƿ���Ҫ����ë�߼���
			GeneralBillVO nowVObill = getGeneralBillVO();
			String InvID = "";
			if (nowVObill != null && getGenSelectRowID() >= 0) {
				InvID = (String) nowVObill.getItemValue(getGenSelectRowID(),
						"cinventoryid");
			}
			boolean sfmbjs = CheckSfmbjs(InvID);
			// ���ë�ߵ���������������������ȫ���ĵ���
			if (sfmbjs == true)
				mdvos = mdMbjs(mdvos);
			else
				this.setNprice(this.getStuffprice());// ���ĵ���stuffprice
		} else {
			mdvos = MDUtils.mdBJ(mdvos, num);
			this.setNprice(this.getStuffprice());// ���ĵ���stuffprice
		}
		getBillCardPanel().getBillData().setBodyValueVO(mdvos);
		getBillCardPanel().getBillModel().execLoadFormula();
		// setMessage("����ɹ�...");
		edited = true;
		MessageDialog.showWarningDlg(this, "��ʾ", "����ɹ�!");
	}

	MdcrkVO[] getBodyVOs() {
		MdcrkVO[] rsvo = (MdcrkVO[]) getBillCardPanel().getBillData()
				.getBodyValueVOs(MdcrkVO.class.getName());
		if (rsvo == null || rsvo.length == 0)
			return rsvo;
		boolean bj = getBillCardPanel().getHeadItem("ispj").getValue().equals(
				"true");
		boolean fjs = getBillCardPanel().getHeadItem("fjs").getValue().equals(
				"true");
		for (int i = 0; i < rsvo.length; i++) {
			rsvo[i].setSfbj(new UFBoolean(bj));
			rsvo[i].setDef4(new UFBoolean(fjs));
		}
		return rsvo;
	}

	/**
	 * �޸��˱���������Ҫ���¼�������
	 */
	void setZLNULl() {
		int i = getBillCardPanel().getRowCount();
		if (i > 0) {
			getBillCardPanel().getBillModel().setValueAt(null, i - 1, "srkzl");// �޸��˱���������Ҫ���¼�������
		}
	}

	private void onSave() throws BusinessException {
		getBillCardPanel().dataNotNullValidate();
		if (edited) {
			MdcrkVO[] mdvos = getBodyVOs();

			// �Ƿ�Ǽ���
			String fjs = (String) getBillCardPanel().getHeadItem("fjs")
					.getValueObject();
			if (fjs != null && !fjs.equals("false")) {
				// ��������
				UFDouble sum_srkzs = new UFDouble(0);
				for (int i = 0; i < mdvos.length; i++) {
					if (mdvos[i].getSrkzs() == null
							|| mdvos[i].getSrkzs().doubleValue() == 0)
						throw new BusinessException("��" + (i + 1) + "�У�֧������Ϊ�գ�");
					sum_srkzs = sum_srkzs.add(mdvos[i].getSrkzs());
				}
				if (sum_srkzs.doubleValue() != this.getSsfsl().doubleValue())
					throw new BusinessException("�뵥�����֧��"
							+ sum_srkzs.doubleValue() + "������ʵ��⸨����"
							+ this.getSsfsl().doubleValue());
			}

			IMDTools tools = NCLocator.getInstance().lookup(IMDTools.class);
			tools.saveMDrk(mdvos, mdxclvo, (String) getGeneralBillVO()
					.getItemValue(getGenSelectRowID(), "cgeneralbid"));
			UFDouble sum_sssl = new UFDouble(0);
			UFDouble sum_factoryWeight=new UFDouble(0);
			for (int i = 0; i < mdvos.length; i++) {
				sum_sssl = sum_sssl.add(mdvos[i].getSrkzl());
				sum_factoryWeight=sum_factoryWeight.add(mdvos[i].getDef1()==null?new UFDouble(0):mdvos[i].getDef1());//MeiChao 2010-12-13
			}
			// �Ƿ�ɾ���뵥
			if (sum_sssl.doubleValue() == 0)
				this.setSfsqmd(new UFBoolean(true));
			// ����ʵ������
			this.setSssl(sum_sssl);
			//���øֳ�����--MeiChao 2010-12-13
			this.setFactoryweight(sum_factoryWeight);
			setMessage("����ɹ�...");
			setStatus(MDUtils.INIT_CANEDIT);
			initBodyData();

			// �����������������
			String ispj = (String) getBillCardPanel().getHeadItem("ispj")
					.getValueObject();
			GeneralBillVO nowVObill = getGeneralBillVO();
			String InvID = "";
			if (nowVObill != null && getGenSelectRowID() >= 0) {
				InvID = (String) nowVObill.getItemValue(getGenSelectRowID(),
						"cinventoryid");
			}
			boolean sfmbjs = CheckSfmbjs(InvID);
			if (ispj != null && !ispj.equals("false")) {
				// ��������֮��*���ĵ���=���Ľ�� stuffsumny
				this.setStuffsumny(sum_sssl.multiply(this.getStuffprice(),
						MDConstants.JE_XSW));
				this.setStuffweight(sum_sssl);// ��������
				this.setNmny(this.getStuffsumny().add(this.getGrosssumny())); // ʵ�����
			} else {
				if (sfmbjs == false) {
					// ��������֮��*���ĵ���=���Ľ�� stuffsumny
					this.setStuffsumny(sum_sssl.multiply(this.getStuffprice(),
							MDConstants.JE_XSW));
					this.setStuffweight(sum_sssl);// ��������
					this
							.setNmny(this.getStuffsumny().add(
									this.getGrosssumny())); // ʵ�����
				}
			}

			// �Ƿ�Ǽ���
			if (fjs != null && !fjs.equals("false")) {
				// ��������֮��*���ĵ���=���Ľ�� stuffsumny
				this.setStuffsumny(sum_sssl.multiply(this.getStuffprice(),
						MDConstants.JE_XSW));
				this.setStuffweight(sum_sssl);// ��������
				this.setNmny(this.getStuffsumny().add(this.getGrosssumny())); // ʵ�����
			}

			closeCancel();
			// ui.getButtonManager().onButtonClicked(ui.getButtonManager().getButton(ICButtonConst.BTN_BROWSE_REFRESH));
		} else
			MessageDialog.showWarningDlg(this, "��ʾ", "���ȼ��㣡");
	}

	private void onAddline() {
		setMessage("����һ������...");
		// ѡ�����
		int srow = getBillCardPanel().getBillTable().getSelectedRow();
		getBillCardPanel().addLine();
		int i = getBillCardPanel().getRowCount();
		// ��������  ������˻���ʱȫ����Ϊ������ⵥ��
		if (sfth == false)
			getBillCardPanel().getBillModel().setValueAt(ui.getBillType(),
					i - 1, "cbodybilltypecode");
		else
			getBillCardPanel().getBillModel().setValueAt("4A", i - 1,
					"cbodybilltypecode");
		// ���ݷ���
		UFBoolean frep = (UFBoolean) getGeneralBillVO().getHeaderValue(
				"freplenishflag");// �Ƿ��˻���ʶ;
		UFBoolean rk = UFBoolean.TRUE;

		// ���õ��ݷ���, ����: �ɹ����ҵ��:���ݷ���Ϊ����, �ɹ���� �˻�:���ݷ���Ϊ����
		if (frep.booleanValue()) {
			getBillCardPanel().getBillModel().setValueAt(1, i - 1, "djfx");
			if (ui.getInOutFlag() == InOutFlag.IN) {
				rk = UFBoolean.FALSE;
			}
		} else {
			if (ui.getInOutFlag() == InOutFlag.OUT) {
				rk = UFBoolean.FALSE;
			}
			getBillCardPanel().getBillModel().setValueAt(0, i - 1, "djfx");
		}

		// ���� �뵥����ⷽ��
		if (rk.booleanValue()) {
			getBillCardPanel().getBillModel().setValueAt(0, i - 1, "crkfx");
		} else {
			getBillCardPanel().getBillModel().setValueAt(1, i - 1, "crkfx");
		}

		// cwarehouseidb �ֿ�pk cwarehouseid
		getBillCardPanel().getBillModel().setValueAt(
				getGeneralBillVO().getHeaderValue("cwarehouseid"), i - 1,
				"cwarehouseidb");

		// ccalbodyidb �����֯PK pk_calbody
		getBillCardPanel().getBillModel().setValueAt(
				getGeneralBillVO().getHeaderValue("pk_calbody"), i - 1,
				"ccalbodyidb");

		getBillCardPanel().getBillModel().setValueAt(
				getGeneralBillVO().getItemValue(getGenSelectRowID(),
						"cgeneralbid"), i - 1, "cgeneralbid");
		getBillCardPanel().getBillModel().setValueAt(
				getEnvironment().getCorpID(), i - 1, "pk_corp");
		getBillCardPanel().getBillModel().setValueAt(
				getEnvironment().getLogDate(), i - 1, "dmakedate");
		getBillCardPanel().getBillModel().setValueAt(
				getEnvironment().getUserID(), i - 1, "voperatorid");

		getBillCardPanel().getBillModel().setValueAt(this.getMd_note(), i - 1,
				"md_note"); // ���
		getBillCardPanel().getBillModel().setValueAt(this.getMd_zyh(), i - 1,
				"md_zyh"); // ��Դ��

		getBillCardPanel().getBillModel().setValueAt(
				getBillCardPanel().getHeadItem("thickness").getValueObject(),
				i - 1, "def6");
		getBillCardPanel().getBillModel().setValueAt(
				getBillCardPanel().getHeadItem("ispj").getValueObject(), i - 1,
				"sfbj");// �Ƿ����

		getBillCardPanel().getBillModel().setValueAt(this.getVfree1(), i - 1,
				"remark");// ��ע
		getBillCardPanel().getBillModel().setValueAt(
				getBillCardPanel().getHeadItem("fjs").getValueObject(), i - 1,
				"def4");// �Ǽ���

		// ���ѡ����д���0
		if (srow >= 0) {
			MdcrkVO[] vos = (MdcrkVO[]) getBillCardPanel().getBillData()
					.getBodyValueVOs(nc.vo.ic.md.MdcrkVO.class.getName());
			MdcrkVO vo = vos[srow];
			getBillCardPanel().getBillModel().setValueAt(vo.getCspaceid(),
					i - 1, "cspaceid"); // ��λ
			getBillCardPanel().getBillModel().setValueAt(vo.getJbh(), i - 1,
					"jbh"); // �����
			getBillCardPanel().getBillModel().setValueAt(vo.getMd_length(),
					i - 1, "md_length"); // ����
			getBillCardPanel().getBillModel().setValueAt(vo.getMd_width(),
					i - 1, "md_width"); // ���
			getBillCardPanel().getBillModel().setValueAt(vo.getMd_meter(),
					i - 1, "md_meter"); // ����
			getBillCardPanel().getBillModel().setValueAt(vo.getSrkzs(), i - 1,
					"srkzs"); // ֧��
			getBillCardPanel().getBillModel().setValueAt(vo.getMd_lph(), i - 1,
					"md_lph"); // ¯����
			getBillCardPanel().getBillModel().setValueAt(vo.getMd_zlzsh(),
					i - 1, "md_zlzsh"); // ������֤���
			getBillCardPanel().getBillModel().setValueAt(vo.getMd_note(),
					i - 1, "md_note"); // ���
			getBillCardPanel().getBillModel().setValueAt(vo.getMd_zyh(), i - 1,
					"md_zyh");// ��Դ��
			getBillCardPanel().getBillModel().setValueAt(vo.getDef7(), i - 1,
					"def7");// def7
			getBillCardPanel().getBillModel().setValueAt(vo.getDef8(), i - 1,
					"def8");// def8
			getBillCardPanel().getBillModel().setValueAt(vo.getDef9(), i - 1,
					"def9");// def9
			getBillCardPanel().getBillModel().setValueAt(vo.getRemark(), i - 1,
					"remark");// ��ע
			getBillCardPanel().getBillModel().setValueAt(vo.getDef4(), i - 1,
					"def4");// def4
			getBillCardPanel().getBillModel().setValueAt(vo.getSfbj(), i - 1,
					"sfbj");// �Ƿ����
			getBillCardPanel().getBillModel().execLoadFormula();// ��ʾ��ʽ
		}

	}

	/**
	 * �༭���¼�
	 */
	public void afterEdit(BillEditEvent editEvent) {
		String key = editEvent.getKey();
		if (key.equals("ispj")) {
			if (editEvent.getValue().equals("true")) {
				getBillCardPanel().getHeadItem("realWeight").setEdit(true);
				getBillCardPanel().getHeadItem("grossprice").setEdit(false);
				setIspj(UFBoolean.TRUE);
				MdcrkVO[] vos = (MdcrkVO[]) getBillCardPanel().getBillData()
						.getBodyValueVOs(nc.vo.ic.md.MdcrkVO.class.getName());
				if (vos != null && vos.length > 0) {
					for (int i = 0; i < vos.length; i++) {
						getBillCardPanel().getBillModel().setValueAt(null, i,
								"def1");
						getBillCardPanel().getBillModel().setValueAt(null, i,
								"def2");
						getBillCardPanel().getBillModel().setValueAt(
								new UFBoolean(false), i, "def4");
					}
				}
			} else {
				getBillCardPanel().getHeadItem("realWeight").setEdit(false);

				// �жϵ�ǰ����Ƿ���Ҫ����ë�߼���
				GeneralBillVO nowVObill = getGeneralBillVO();
				String InvID = "";
				if (nowVObill != null && getGenSelectRowID() >= 0) {
					InvID = (String) nowVObill.getItemValue(
							getGenSelectRowID(), "cinventoryid");
				}
				try {
					boolean sfmbjs = CheckSfmbjs(InvID);
					if (sfmbjs == true)
						getBillCardPanel().getHeadItem("grossprice").setEdit(
								true);
				} catch (BusinessException e) {
					e.printStackTrace();
				}
				setIspj(UFBoolean.FALSE);
			}
		}
		if (key.equals("md_meter")) {
			getBillCardPanel().getBillModel().setValueAt(null,
					editEvent.getRow(), "md_width");
			getBillCardPanel().getBillModel().setValueAt(null,
					editEvent.getRow(), "md_length");
			getBillCardPanel().getBillModel().setValueAt(null,
					editEvent.getRow(), "srkzl");
		} else if (key.equals("md_width") || key.equals("md_length")) {
			getBillCardPanel().getBillModel().setValueAt(null,
					editEvent.getRow(), "md_meter");
			getBillCardPanel().getBillModel().setValueAt(null,
					editEvent.getRow(), "srkzl");
		} else if (key.equals("srkzs")) {
			getBillCardPanel().getBillModel().setValueAt(null,
					editEvent.getRow(), "srkzl");
		} else if (key.equals("grossprice")) {
			MdcrkVO[] vos = (MdcrkVO[]) getBillCardPanel().getBillData()
					.getBodyValueVOs(nc.vo.ic.md.MdcrkVO.class.getName());
			if (vos != null && vos.length > 0) {
				MdcrkVO[] rsvo = (MdcrkVO[]) getBillCardPanel().getBillData()
						.getBodyValueVOs(MdcrkVO.class.getName());
				if (rsvo != null && rsvo.length > 0) {
					int row = rsvo.length;
					for (int i = 0; i < row; i++)
						getBillCardPanel().getBillModel().setValueAt(null, i,
								"srkzl");
				}
			}

		}
		// ������˷Ǽ���ѡ����
		else if (key.equals("fjs")) {
			try {
				boolean boolean_fjs = false;
				if (editEvent.getValue().equals("true")) {
					init();// ��ʼ��
					onEdit();// �༭
					getBillCardPanel().getHeadItem("realWeight").setEdit(false); // ʵ������
					getBillCardPanel().getHeadItem("grossprice").setEdit(false); // ë�ߵ���
					getBillCardPanel().getHeadItem("ispj").setValue(
							new UFBoolean(false));
					BillEditEvent e = new BillEditEvent(getBillCardPanel()
							.getHeadItem("ispj").getComponent(),
							getBillCardPanel().getHeadItem("ispj"), "ispj");
					// afterEdit(e);
					getBillCardPanel().getHeadItem("ispj").setEdit(false); // �Ƿ����
					getUIButton(MDUtils.CALC_BUTTON).setEnabled(false);
					getBillCardPanel().getBodyItem("srkzl").setEnabled(true);
					boolean_fjs = true;
				} else {
					getBillCardPanel().getHeadItem("ispj").setEdit(true); // �Ƿ����
					init();// ��ʼ��
					onEdit();// �༭
					getUIButton(MDUtils.CALC_BUTTON).setEnabled(true);
					getBillCardPanel().getBodyItem("srkzl").setEnabled(false);
					boolean_fjs = false;
				}
				// ��������ֵ
				MdcrkVO[] rsvo = (MdcrkVO[]) getBillCardPanel().getBillData()
						.getBodyValueVOs(MdcrkVO.class.getName());
				if (rsvo != null && rsvo.length > 0) {
					for (int i = 0; i < rsvo.length; i++) {
						getBillCardPanel().getBillModel().setValueAt(null, i,
								"srkzl");
						getBillCardPanel().getBillModel().setValueAt(
								new UFBoolean(false), i, "sfbj");
						getBillCardPanel().getBillModel().setValueAt(null, i,
								"def1");
						getBillCardPanel().getBillModel().setValueAt(null, i,
								"def2");
						getBillCardPanel().getBillModel().setValueAt(
								new UFBoolean(boolean_fjs), i, "def4");
					}
				}
			} catch (BusinessException e) {
				e.printStackTrace();
				MessageDialog.showWarningDlg(this, "����", e.getMessage());
			}
		}
		// �༭���Զ�����Ŀ7
		else if (key.equals("def7")) {
			String defStr = (String) editEvent.getValue();
			// �Ǽ����ֵ
			boolean fsjboolean = getBillCardPanel().getHeadItem("fjs")
					.getValue().equals("true");
			// ����ǷǼ���
			if (fsjboolean) {
				if (isNumber(defStr))
					getBillCardPanel().getBillModel().setValueAt(
							new UFDouble(defStr), editEvent.getRow(),
							"md_length");
				else
					getBillCardPanel().getBillModel().setValueAt(
							new UFDouble(0), editEvent.getRow(), "md_length");
			} else {
				if (isNumber(defStr))
					getBillCardPanel().getBillModel().setValueAt(
							new UFDouble(defStr), editEvent.getRow(),
							"md_length");
				else {
					getBillCardPanel().getBillModel().setValueAt(null,
							editEvent.getRow(), "def7");
					getBillCardPanel().getBillModel().setValueAt(null,
							editEvent.getRow(), "md_length");
					MessageDialog.showWarningDlg(this, "��ʾ", "���ȱ���������");
				}
			}
			getBillCardPanel().getBillModel().setValueAt(null,
					editEvent.getRow(), "def9");
			getBillCardPanel().getBillModel().setValueAt(null,
					editEvent.getRow(), "md_meter");
			getBillCardPanel().getBillModel().setValueAt(null,
					editEvent.getRow(), "srkzl");
		}
		// �༭���Զ�����Ŀ8
		else if (key.equals("def8")) {
			String defStr = (String) editEvent.getValue();
			// �Ǽ����ֵ
			boolean fsjboolean = getBillCardPanel().getHeadItem("fjs")
					.getValue().equals("true");
			// ����ǷǼ���
			if (fsjboolean) {
				if (isNumber(defStr))
					getBillCardPanel().getBillModel().setValueAt(
							new UFDouble(defStr), editEvent.getRow(),
							"md_width");
				else
					getBillCardPanel().getBillModel().setValueAt(
							new UFDouble(0), editEvent.getRow(), "md_width");
			} else {
				if (isNumber(defStr))
					getBillCardPanel().getBillModel().setValueAt(
							new UFDouble(defStr), editEvent.getRow(),
							"md_width");
				else {
					getBillCardPanel().getBillModel().setValueAt(null,
							editEvent.getRow(), "def8");
					getBillCardPanel().getBillModel().setValueAt(null,
							editEvent.getRow(), "md_width");
					MessageDialog.showWarningDlg(this, "��ʾ", "��ȱ���������");
				}
			}
			getBillCardPanel().getBillModel().setValueAt(null,
					editEvent.getRow(), "def9");
			getBillCardPanel().getBillModel().setValueAt(null,
					editEvent.getRow(), "md_meter");
			getBillCardPanel().getBillModel().setValueAt(null,
					editEvent.getRow(), "srkzl");
		}
		// �༭���Զ�����Ŀ9
		else if (key.equals("def9")) {
			String defStr = (String) editEvent.getValue();
			// �Ǽ����ֵ
			boolean fsjboolean = getBillCardPanel().getHeadItem("fjs")
					.getValue().equals("true");
			// ����ǷǼ���
			if (fsjboolean) {
				if (isNumber(defStr))
					getBillCardPanel().getBillModel().setValueAt(
							new UFDouble(defStr), editEvent.getRow(),
							"md_meter");
				else
					getBillCardPanel().getBillModel().setValueAt(
							new UFDouble(0), editEvent.getRow(), "md_meter");
			} else {
				if (isNumber(defStr))
					getBillCardPanel().getBillModel().setValueAt(
							new UFDouble(defStr), editEvent.getRow(),
							"md_meter");
				else {
					getBillCardPanel().getBillModel().setValueAt(null,
							editEvent.getRow(), "def9");
					getBillCardPanel().getBillModel().setValueAt(null,
							editEvent.getRow(), "md_meter");
					MessageDialog.showWarningDlg(this, "��ʾ", "��������������");
				}
			}
			getBillCardPanel().getBillModel().setValueAt(null,
					editEvent.getRow(), "def7");
			getBillCardPanel().getBillModel().setValueAt(null,
					editEvent.getRow(), "def8");
			getBillCardPanel().getBillModel().setValueAt(null,
					editEvent.getRow(), "md_width");
			getBillCardPanel().getBillModel().setValueAt(null,
					editEvent.getRow(), "md_length");
			getBillCardPanel().getBillModel().setValueAt(null,
					editEvent.getRow(), "srkzl");
		}
		//2010-11-25 MeiChao add begin
		else if(key.equals("box")){//����޸��˻�λ����
			getBillCardPanel().getBillModel().execLoadFormula();
		}
		//2010-11-25 MeiChao add end

		edited = true;
		// md_width
		// md_length
		// md_meter
	}

	void setIspj(UFBoolean issel) {
		MdcrkVO[] vos = (MdcrkVO[]) getBillCardPanel().getBillData()
				.getBodyValueVOs(nc.vo.ic.md.MdcrkVO.class.getName());
		if (vos != null && vos.length > 0) {
			int i = vos.length;
			for (int j = 0; j < i; j++) {
				getBillCardPanel().getBillModel().setValueAt(issel, j, "sfbj");
				getBillCardPanel().getBillModel().setValueAt(null, j, "srkzl");
			}
		}
	}

	public void bodyRowChange(BillEditEvent editEvent) {
	}

	public boolean beforeEdit(BillEditEvent billeditevent) {
		return true;
	}

	public UFDouble getSsfsl() {
		return ssfsl;
	}

	public void setSsfsl(UFDouble ssfsl) {
		this.ssfsl = ssfsl;
	}

	public UFDouble getSssl() {
		return sssl;
	}

	public void setSssl(UFDouble sssl) {
		this.sssl = sssl;
	}

	public UFDouble getGrossprice() {
		return grossprice;
	}

	public void setGrossprice(UFDouble grossprice) {
		this.grossprice = grossprice;
	}

	public UFDouble getGrosssumny() {
		return grosssumny;
	}

	public void setGrosssumny(UFDouble grosssumny) {
		this.grosssumny = grosssumny;
	}

	public UFDouble getGrossweight() {
		return grossweight;
	}

	public void setGrossweight(UFDouble grossweight) {
		this.grossweight = grossweight;
	}

	public UFDouble getStuffprice() {
		return stuffprice;
	}

	public void setStuffprice(UFDouble stuffprice) {
		this.stuffprice = stuffprice;
	}

	public UFDouble getStuffsumny() {
		return stuffsumny;
	}

	public void setStuffsumny(UFDouble stuffsumny) {
		this.stuffsumny = stuffsumny;
	}

	public UFDouble getStuffweight() {
		return stuffweight;
	}

	public void setStuffweight(UFDouble stuffweight) {
		this.stuffweight = stuffweight;
	}

	public UFDouble getNprice() {
		return nprice;
	}

	public void setNprice(UFDouble nprice) {
		this.nprice = nprice;
	}

	public UFDouble getNmny() {
		return nmny;
	}

	public void setNmny(UFDouble nmny) {
		this.nmny = nmny;
	}

	public UFBoolean getSfsqmd() {
		return sfsqmd;
	}

	public void setSfsqmd(UFBoolean sfsqmd) {
		this.sfsqmd = sfsqmd;
	}

	// ë�߼���
	public MdcrkVO[] mdMbjs(MdcrkVO[] mdcrkVos) throws BusinessException {
		// ë�ߵ���
		String grossprice_str = getBillCardPanel().getHeadItem("grossprice")
				.getValue();
		if (grossprice_str == null || grossprice_str.equals(""))
			throw new BusinessException("��ǰ���ά����ë�߼��㣬���ʱë�ߵ��۲���Ϊ�գ�");
		UFDouble grossprice = new UFDouble(grossprice_str, MDConstants.DJ_XSW);
		if (grossprice.doubleValue() == 0)
			throw new BusinessException("���ʱë�ߵ��۲���Ϊ0");
		// ë��ϵ��
		UFDouble mbxs = new UFDouble(0);
		mbxs = MDUtils.getMBXS(mdxclvo.getCinvbasid(), mdxclvo
				.getCinventoryidb(), null);
		if (mbxs == null || mbxs.doubleValue() <= 0)
			throw new BusinessException("ë��ϵ������С�ڵ���0");
		UFDouble sumZczl = new UFDouble(0);// ��������֮��
		UFDouble sumMbzl = new UFDouble(0);// ë������֮��
		for (int i = 0; i < mdcrkVos.length; i++) {
			mdcrkVos[i].setDef1(mdcrkVos[i].getSrkzl());// ��������
			mdcrkVos[i].setDef2(mdcrkVos[i].getSrkzl().multiply(mbxs,
					MDConstants.ZL_XSW));// ë������
			mdcrkVos[i].setSrkzl(mdcrkVos[i].getDef1().add(
					mdcrkVos[i].getDef2())); // ����+ë������,��ʵ�������
			sumZczl = sumZczl.add(mdcrkVos[i].getDef1());// �����������
			sumMbzl = sumMbzl.add(mdcrkVos[i].getDef2());// ë���������
		}
		// ë�ߵ���
		this.setGrossprice(grossprice);

		// ��������֮��*���ĵ���=���Ľ�� stuffsumny
		this.setStuffsumny(sumZczl.multiply(this.getStuffprice(),
				MDConstants.JE_XSW));
		this.setStuffweight(sumZczl);// ��������

		// ë������֮��*ë�ߵ���=ë�߽�� grosssumny
		this.setGrosssumny(sumMbzl.multiply(this.getGrossprice(),
				MDConstants.JE_XSW));
		this.setGrossweight(sumMbzl);// ë������

		// ��ʵ�ĵ��� nprice =(���Ľ��+ë�߽��)/(��������+ë������)
		this.setNprice((this.getStuffsumny().add(this.getGrosssumny())).div(
				this.getStuffweight().add(this.getGrossweight()),
				MDConstants.DJ_XSW));

		this.setNmny(this.getStuffsumny().add(this.getGrosssumny())); // ʵ�����

		return mdcrkVos;
	}

	// �Ƿ���Ҫ����ë�߼���
	private boolean CheckSfmbjs(String pk_invbas) throws BusinessException {
		IUAPQueryBS iUAPQueryBS = (IUAPQueryBS) NCLocator.getInstance().lookup(
				IUAPQueryBS.class.getName());
		Object[] objs = (Object[]) iUAPQueryBS
				.executeQuery(
						"select t1.def19 from bd_invbasdoc t1 left join bd_invmandoc t2 on t1.pk_invbasdoc=t2.pk_invbasdoc where t2.pk_invmandoc='"
								+ pk_invbas + "'", new ArrayProcessor());
		if (objs[0] != null && objs[0].toString() != null
				&& objs[0].toString().toUpperCase().equals("Y"))
			return true;
		return false;
	}

	public String getMd_note() {
		return md_note;
	}

	public void setMd_note(String md_note) {
		this.md_note = md_note;
	}

	public String getMd_zyh() {
		return md_zyh;
	}

	public void setMd_zyh(String md_zyh) {
		this.md_zyh = md_zyh;
	}

	// �ж�һ���ַ����Ƿ�������
	public static boolean isNumber(String strChar) {
		if (strChar == null || strChar.equals(""))
			return false;
		for (int i = strChar.length(); --i >= 0;) {
			if (!Character.isDigit(strChar.charAt(i)))
				return false;
		}
		return true;
	}

	public String getVfree1() {
		return vfree1;
	}

	public void setVfree1(String vfree1) {
		this.vfree1 = vfree1;
	}

}
