package nc.ui.ic.mdck;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import nc.ui.ic.pub.bill.GeneralBillClientUI;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.UIDialog;
import nc.vo.ic.pub.bill.GeneralBillHeaderVO;
import nc.vo.ic.pub.bill.GeneralBillItemVO;
import nc.vo.ic.pub.bill.GeneralBillVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.constant.ic.BillMode;

public class MdwhDlg extends UIDialog implements ActionListener {

	private MdwhPanel OrigPiecePanel = null;

	private ChInfoVO infoVO;

	private UFDouble noutassistnum;// ʵ��������

	private UFDouble noutnum;// ʵ������

	private GeneralBillClientUI ui;

	/**
	 * This method initializes
	 * 
	 */
	public MdwhDlg(java.awt.Container parent, String title, ChInfoVO infoVO) {
		super(parent, title);
		this.infoVO = infoVO;
		initialize();

	}

	public MdwhDlg(ChInfoVO infoVO) {
		this.infoVO = infoVO;
		initialize();
	}

	public MdwhDlg(GeneralBillClientUI ui) throws BusinessException {
		this.ui = ui;
		infoVO = getInfoVO();
		if (infoVO.getCgeneralbid() == null
				|| infoVO.getCgeneralbid().equals(""))
			throw new BusinessException("����PKΪ�գ����ȱ��浥�ݣ�");

		initialize();
	}

	// ����InfoVO
	private ChInfoVO getInfoVO() throws BusinessException {
		ChInfoVO infoVO = new ChInfoVO();
		infoVO.setCorpVo(ClientEnvironment.getInstance().getCorporation());// ��˾
		infoVO.setUfdate(ClientEnvironment.getInstance().getDate());// ����
		infoVO.setUserVo(ClientEnvironment.getInstance().getUser());// �û�
		GeneralBillVO billvo = getGeneralBillVO();
		if (billvo == null)
			throw new BusinessException("��ѡ�񵥾ݣ�");
		GeneralBillHeaderVO hvo = (GeneralBillHeaderVO) billvo.getParentVO();
		if (hvo == null)
			throw new BusinessException("��ѡ����Ҫά���뵥�ĵ��ݣ�");
		infoVO.setCbodybilltypecode(hvo.getCbilltypecode()); // ��������
		infoVO.setCwarehouseidb(hvo.getCwarehouseid());// �ֿ�
		infoVO.setCcalbodyidb(hvo.getPk_calbody());// pk_calbody�����֯
		infoVO.setFbillflag(hvo.getFbillflag());// ����״̬��
		int srow = getGenSelectRowID();
		if (srow < 0)
			throw new BusinessException("��ѡ����Ҫά���뵥�ı�����");
		GeneralBillItemVO itemVOa = ((GeneralBillItemVO[]) billvo
				.getChildrenVO())[srow];
		infoVO.setCgeneralbid(itemVOa.getCgeneralbid());// ������PK
		infoVO.setPk_invbasdoc(itemVOa.getCinvbasid());// �����������
		infoVO.setPk_invmandoc(itemVOa.getCinventoryid());// ���������
		// infoVO.setNoutassistnum(itemVOa.getNoutassistnum());// ʵ��������
		// infoVO.setNoutnum(itemVOa.getNoutnum());// ʵ������
		//infoVO.setNoutassistnum(itemVOa.getNshouldoutassistnum());// Ӧ��������
		//infoVO.setNoutnum(itemVOa.getNshouldoutnum());// Ӧ������
		if(itemVOa.getNoutassistnum()==null||itemVOa.getNoutassistnum().doubleValue()==0)
			throw new BusinessException("ʵ��������Ϊ�գ�����ά���뵥��");
		infoVO.setNoutassistnum(itemVOa.getNoutassistnum());// ʵ��������
		infoVO.setNoutnum(itemVOa.getNoutnum());// ʵ������
		infoVO.setLydjh(itemVOa.getCfirstbillbid());// ��Դ���ݺ�cfirstbillbid
		return infoVO;
	}

	/**
	 * This method initializes this
	 * 
	 */
	private void initialize() {
		this.setTitle("�뵥ά��");
		// dialog�Ĺ�����
		this.setContentPane(getOrigPiecePanel());
		this.setSize(800, 400);
		// this.setNoutassistnum(infoVO.getNoutassistnum());
		// this.setNoutnum(infoVO.getNoutnum());
	}

	public MdwhPanel getOrigPiecePanel() {
		if (OrigPiecePanel == null) {
			OrigPiecePanel = new MdwhPanel(infoVO, this);
		}
		return OrigPiecePanel;
	}

	public void actionPerformed(ActionEvent e) {
	}

	public void setOrigPiecePanel(MdwhPanel origPiecePanel) {
		OrigPiecePanel = origPiecePanel;
	}

	public UFDouble getNoutassistnum() {
		return noutassistnum;
	}

	public void setNoutassistnum(UFDouble noutassistnum) {
		this.noutassistnum = noutassistnum;
	}

	public UFDouble getNoutnum() {
		return noutnum;
	}

	public void setNoutnum(UFDouble noutnum) {
		this.noutnum = noutnum;
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

	// ���½�������
	public GeneralBillVO getUpdateUIVO() {
		// ����VO
		GeneralBillVO bill = getGeneralBillVO();
		// ѡ�����
		int srow = getGenSelectRowID();
		GeneralBillHeaderVO hvo = bill.getHeaderVO();
		hvo.setTs(MDConstants.getCurrentDateTime());
		GeneralBillItemVO[] itemVOaArrays = (GeneralBillItemVO[]) bill
				.getChildrenVO();
		GeneralBillItemVO itemVOa = itemVOaArrays[srow];
		itemVOa.setNoutassistnum(getNoutassistnum());
		itemVOa.setNoutnum(getNoutnum());
		//itemVOa.set//��������
		
		itemVOaArrays[srow] = itemVOa;
		for (int t = 0; t < itemVOaArrays.length; t++)
			itemVOaArrays[t].setTs(MDConstants.getCurrentDateTime());
		GeneralBillVO billvo = new GeneralBillVO();
		billvo.setParentVO(hvo);
		billvo.setChildrenVO(itemVOaArrays);
		return billvo;
	}

}
