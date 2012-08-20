package nc.ui.ic.mdck;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

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

public class MDGbftdialog extends UIDialog implements ActionListener {

	private MdGbPanel OrigPiecePanel = null;

	private ChInfoVO[] infoVOs;

	private UFDouble noutassistnum;// 实出辅数量

	private UFDouble noutnum;// 实出数量

	private GeneralBillClientUI ui;
	
	//add by ouyangzhb 2012-08-18 增加码单的实出主数量，实出辅数量
	HashMap<String,UFDouble> noutnummap ;
	HashMap<String,UFDouble> noutassnummap ;
	public HashMap<String, UFDouble> getNoutnummap() {
		return noutnummap;
	}

	public void setNoutnummap(Map<String, UFDouble> noutnummap2) {
		this.noutnummap = (HashMap<String, UFDouble>) noutnummap2;
	}

	public HashMap<String, UFDouble> getNoutassnummap() {
		return noutassnummap;
	}

	public void setNoutassnummap(Map<String, UFDouble> noutassnummap2) {
		this.noutassnummap = (HashMap<String, UFDouble>) noutassnummap2;
	}
//add end 
	
	private GeneralBillItemVO[] bodyvo;

	public GeneralBillItemVO[] getBodyvo() {
		return bodyvo;
	}

	public void setBodyvo(GeneralBillItemVO[] bodyvo) {
		this.bodyvo = bodyvo;
	}

	/**
	 * This method initializes
	 * 
	 */
	public MDGbftdialog(java.awt.Container parent, String title, ChInfoVO[] infoVO) {
		super(parent, title);
		this.infoVOs = infoVO;
		initialize();

	}

	public MDGbftdialog(ChInfoVO infoVO[]) {
		this.infoVOs = infoVO;
		initialize();
	}

	public MDGbftdialog(GeneralBillClientUI ui) throws BusinessException {
		this.ui = ui;
		infoVOs = getInfoVOs();
		for(int i=0;i<infoVOs.length;i++){
			ChInfoVO infoVO = infoVOs[i];
			if (infoVO.getCgeneralbid() == null
					|| infoVO.getCgeneralbid().equals(""))
				throw new BusinessException("表体PK为空，请先保存单据！");
		}
		initialize();
	}

	/**
	 * add by ouyangzhb 2012-08-18 获取表体行信息
	 * @return
	 * @throws BusinessException
	 */
	private ChInfoVO[] getInfoVOs() throws BusinessException {
		int[] selrows = ui.getBillCardPanel().getBillTable().getSelectedRows();
		if (selrows==null || selrows.length == 0)
			throw new BusinessException("请选择需要过磅分摊的表体行");
		ChInfoVO[] infoVOs = new ChInfoVO[selrows.length];
		
		for(int i=0;i<selrows.length;i++){
			ChInfoVO infoVO = new ChInfoVO();
			infoVO.setCorpVo(ClientEnvironment.getInstance().getCorporation());// 公司
			infoVO.setUfdate(ClientEnvironment.getInstance().getDate());// 日期
			infoVO.setUserVo(ClientEnvironment.getInstance().getUser());// 用户
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
			GeneralBillItemVO itemVOa = ((GeneralBillItemVO[]) billvo
					.getChildrenVO())[selrows[i]];
			// 是否退货
			if (hvo.getFreplenishflag().booleanValue() == true) {
				itemVOa.setNoutassistnum(new UFDouble(-itemVOa.getNinassistnum()
						.doubleValue())); // 辅数量
				itemVOa.setNoutnum(new UFDouble(-itemVOa.getNinnum().doubleValue()));// 数量
				infoVO.setCbodybilltypecode("4I"); // 单据类型
			}
			infoVO.setCgeneralbid(itemVOa.getCgeneralbid());// 表体行PK
			infoVO.setPk_invbasdoc(itemVOa.getCinvbasid());// 存货基本档案
			infoVO.setPk_invmandoc(itemVOa.getCinventoryid());// 存货管理档案
			if (itemVOa.getNoutassistnum() == null
					|| itemVOa.getNoutassistnum().doubleValue() == 0)
				throw new BusinessException("实发辅数量为空！");
			infoVO.setNoutassistnum(itemVOa.getNoutassistnum());// 实出辅数量
			infoVO.setNoutnum(itemVOa.getNoutnum());// 实出数量
			infoVO.setLydjh(itemVOa.getCfirstbillbid());// 来源单据号cfirstbillbid
			infoVOs[i] = infoVO;
		}
		return infoVOs;
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

	public MdGbPanel getOrigPiecePanel() {
		if (OrigPiecePanel == null) {
			OrigPiecePanel = new MdGbPanel(infoVOs, this);
		}
		return OrigPiecePanel;
	}

	public void actionPerformed(ActionEvent e) {
	}

	public void setOrigPiecePanel(MdGbPanel origPiecePanel) {
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
		
		GeneralBillHeaderVO hvo = bill.getHeaderVO();
		hvo.setTs(MDConstants.getCurrentDateTime());
		GeneralBillItemVO[] itemVOaArrays = (GeneralBillItemVO[]) bill
				.getChildrenVO();
		HashMap noutnummap = new HashMap();
		HashMap noutassnummap = new HashMap();
		noutnummap = getNoutnummap();
		noutassnummap = getNoutassnummap();

		int[] srows = getGenSelectRowIDs();
		for(int i=0;i<srows.length;i++){
			GeneralBillItemVO itemVOa = itemVOaArrays[srows[i]];
			itemVOa.setNoutassistnum((UFDouble) noutassnummap.get(itemVOa.getCgeneralbid()));
			itemVOa.setNoutnum((UFDouble) noutnummap.get(itemVOa.getCgeneralbid()));
			itemVOaArrays[srows[i]] = itemVOa;
		}
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
	
	/**
	 * 获取多选的的行
	 * @return
	 */
	public int[] getGenSelectRowIDs() {
		int[] rownow ;
		if (ui.getM_iCurPanel() == BillMode.Card) {
			rownow = ui.getBillCardPanel().getBillTable().getSelectedRows();
		} else {
			rownow = ui.getBillListPanel().getBodyTable().getSelectedRows();
		}
		return rownow;
	}
	

}
