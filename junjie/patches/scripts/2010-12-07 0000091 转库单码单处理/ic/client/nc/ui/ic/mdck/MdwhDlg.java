package nc.ui.ic.mdck;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import nc.ui.ic.pub.bill.GeneralBillClientUI;
import nc.ui.ic.pub.bill.SpecialBillBaseUI;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.UIDialog;
import nc.vo.ic.pub.bill.GeneralBillHeaderVO;
import nc.vo.ic.pub.bill.GeneralBillItemVO;
import nc.vo.ic.pub.bill.GeneralBillVO;
import nc.vo.ic.pub.bill.SpecialBillHeaderVO;
import nc.vo.ic.pub.bill.SpecialBillItemVO;
import nc.vo.ic.pub.bill.SpecialBillVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.constant.ic.BillMode;

public class MdwhDlg extends UIDialog implements ActionListener {

	private MdwhPanel OrigPiecePanel = null;

	private ChInfoVO infoVO;

	private UFDouble noutassistnum;// 实出辅数量

	private UFDouble noutnum;// 实出数量

	private GeneralBillClientUI ui;

	public UFBoolean sfsqmd = new UFBoolean(false); // 是否删除码单

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
			throw new BusinessException("表体PK为空，请先保存单据！");

		initialize();
	}

	// 创建InfoVO
	private ChInfoVO getInfoVO() throws BusinessException {
		ChInfoVO infoVO = new ChInfoVO();
		infoVO.setCorpVo(ClientEnvironment.getInstance().getCorporation());// 公司
		infoVO.setUfdate(ClientEnvironment.getInstance().getDate());// 日期
		infoVO.setUserVo(ClientEnvironment.getInstance().getUser());// 用户
		if (ui.getM_iMode() != BillMode.Browse)
			throw new BusinessException("只有在浏览的状态下才可以维护码单，请先保存单据！");
		GeneralBillVO billvo = getGeneralBillVO();
		if (billvo == null)
			throw new BusinessException("请选择单据！");
		GeneralBillHeaderVO hvo = (GeneralBillHeaderVO) billvo.getParentVO();
		if (hvo == null)
			throw new BusinessException("请选择需要维护码单的单据！");
		infoVO.setCbodybilltypecode(hvo.getCbilltypecode()); // 单据类型
		infoVO.setCwarehouseidb(hvo.getCwarehouseid());// 仓库
		infoVO.setCcalbodyidb(hvo.getPk_calbody());// pk_calbody库存组织
		infoVO.setFbillflag(hvo.getFbillflag());// 单据状态；
		int srow = getGenSelectRowID();
		if (srow < 0)
			throw new BusinessException("请选择需要维护码单的表体行");
		GeneralBillItemVO itemVOa = ((GeneralBillItemVO[]) billvo
				.getChildrenVO())[srow];
		// 是否退货
		if (hvo.getFreplenishflag().booleanValue() == true) {
			itemVOa.setNoutassistnum(new UFDouble(-itemVOa.getNinassistnum()
					.doubleValue())); // 辅数量
			itemVOa
					.setNoutnum(new UFDouble(-itemVOa.getNinnum().doubleValue()));// 数量
			infoVO.setCbodybilltypecode("4I"); // 单据类型
		}
		infoVO.setCgeneralbid(itemVOa.getCgeneralbid());// 表体行PK
		infoVO.setPk_invbasdoc(itemVOa.getCinvbasid());// 存货基本档案
		infoVO.setPk_invmandoc(itemVOa.getCinventoryid());// 存货管理档案
		// infoVO.setNoutassistnum(itemVOa.getNoutassistnum());// 实出辅数量
		// infoVO.setNoutnum(itemVOa.getNoutnum());// 实出数量
		// infoVO.setNoutassistnum(itemVOa.getNshouldoutassistnum());// 应出辅数量
		// infoVO.setNoutnum(itemVOa.getNshouldoutnum());// 应出数量
		if (itemVOa.getNoutassistnum() == null
				|| itemVOa.getNoutassistnum().doubleValue() == 0)
			throw new BusinessException("实发辅数量为空，不能维护码单！");
		infoVO.setNoutassistnum(itemVOa.getNoutassistnum());// 实出辅数量
		infoVO.setNoutnum(itemVOa.getNoutnum());// 实出数量
		infoVO.setLydjh(itemVOa.getCfirstbillbid());// 来源单据号cfirstbillbid
		return infoVO;
	}

	/**
	 * This method initializes this
	 * 
	 */
	private void initialize() {
		this.setTitle("码单维护");
		// dialog的滚动条
		this.setContentPane(getOrigPiecePanel());
		this.setSize(1024, 700);
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

	// 更新界面数据
	public GeneralBillVO getUpdateUIVO() {
		// 单据VO
		GeneralBillVO bill = getGeneralBillVO();
		// 选择的行
		int srow = getGenSelectRowID();
		GeneralBillHeaderVO hvo = bill.getHeaderVO();
		hvo.setTs(MDConstants.getCurrentDateTime());
		GeneralBillItemVO[] itemVOaArrays = (GeneralBillItemVO[]) bill
				.getChildrenVO();
		GeneralBillItemVO itemVOa = itemVOaArrays[srow];
		itemVOa.setNoutassistnum(getNoutassistnum());
		itemVOa.setNoutnum(getNoutnum());
		// itemVOa.set//出库日期

		itemVOaArrays[srow] = itemVOa;
		for (int t = 0; t < itemVOaArrays.length; t++)
			itemVOaArrays[t].setTs(MDConstants.getCurrentDateTime());
		GeneralBillVO billvo = new GeneralBillVO();
		billvo.setParentVO(hvo);
		billvo.setChildrenVO(itemVOaArrays);
		return billvo;
	}
	
	// 需要更新的转库单界面数据
	public SpecialBillVO getUpdateSpecialUIVO(int selectedColumn) {
		// 单据VO
		SpecialBillVO bill = ((SpecialBillBaseUI)this.getParent()).getM_voBill();
		// 选择的行
		int srow = selectedColumn;
		SpecialBillHeaderVO hvo = bill.getHeaderVO();
		hvo.setTs(MDConstants.getCurrentDateTime());
		SpecialBillItemVO[] itemVOaArrays = (SpecialBillItemVO[]) bill
				.getChildrenVO();
		SpecialBillItemVO itemVOa = itemVOaArrays[srow];
		itemVOa.setNshldtransastnum(getNoutassistnum());
		itemVOa.setDshldtransnum(getNoutnum());
		// itemVOa.set//出库日期

		itemVOaArrays[srow] = itemVOa;
		for (int t = 0; t < itemVOaArrays.length; t++)
			itemVOaArrays[t].setTs(MDConstants.getCurrentDateTime());
		SpecialBillVO billvo = new SpecialBillVO();
		billvo.setParentVO(hvo);
		billvo.setChildrenVO(itemVOaArrays);
		return billvo;
	}

	public UFBoolean getSfsqmd() {
		return sfsqmd;
	}

	public void setSfsqmd(UFBoolean sfsqmd) {
		this.sfsqmd = sfsqmd;
	}

}
