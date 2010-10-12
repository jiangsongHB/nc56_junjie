package nc.ui.ic.mdck;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IVOPersistence;
import nc.ui.ic.md.dialog.MDUtils;
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
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDouble;

public class MdwhPanel extends UIPanel implements ActionListener,
		BillEditListener, BillEditListener2 {

	MdwhDlg dlg = null;

	private UIPanel UIPanel = null;

	private UIButton UIButtonAdd = null;

	private UIButton UIButtonDel = null;

	private UIButton UIButtonSave = null;

	private UIButton UIButtonCan = null;

	private UIButton UIButtonJs = null;

	private BillCardPanel billCardPanel = null;

	private String m_pkcorp = null;

	private String m_pkuser = null;

	private String moid = null;

	private ChInfoVO infoVO;

	private String pk_moid = null; // @jve:decl-index=0:

	private MdcrkVO[] crkvos = null;

	public MdwhPanel(ChInfoVO infoVO, MdwhDlg dlg) {
		super();
		this.infoVO = infoVO;
		this.dlg = dlg;
		initialize();
		int rs = initDate();
		if (infoVO.getFbillflag() == 3)
			buttonState(false, false, false, true, false);
		else {
			if (getOPBillCardPanel().getHeadItem("sfbj").getValue().equals(
					"true"))
				buttonState(true, true, false, true, false);
			else {
				buttonState(true, true, false, true, false);
				getOPBillCardPanel().getHeadItem("gbzl").setEdit(false);
			}

		}
		if (rs == 1)
			buttonState(true, true, false, true, true);
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
		getUIButtonJs().addActionListener(this);
		getOPBillCardPanel().setEnabled(false);
		getOPBillCardPanel().addEditListener(this);
		getOPBillCardPanel().addBodyEditListener2(this);
		m_pkcorp = ClientEnvironment.getInstance().getCorporation()
				.getPk_corp();
		m_pkuser = ClientEnvironment.getInstance().getUser().getPrimaryKey();
	}

	private int initDate() {
		// 始始化值
		getOPBillCardPanel().setHeadItem("invcode", infoVO.getPk_invbasdoc());
		getOPBillCardPanel().setHeadItem("gbzl", infoVO.getNoutnum());
		getOPBillCardPanel().setHeadItem("sfbj", new UFBoolean(true));
		getOPBillCardPanel().execHeadLoadFormulas();
		int mdsd = 0;
		try {
			crkvos = new MdProcessBean().queryCrkVOS(infoVO);
			// 如果没有出库记录，则查询锁定记录
			if (crkvos == null) {
				crkvos = new MdProcessBean().queryCrkVOSByXsdd(infoVO, dlg);
				mdsd = 1;
			}
		} catch (BusinessException e) {
			e.printStackTrace();
			MessageDialog
					.showWarningDlg(dlg, "提示", "数据初始化出错：" + e.getMessage());
		}
		// 如果签字后，则不能编辑
		if (infoVO.getFbillflag() == 3)
			getOPBillCardPanel().setEnabled(false);
		else
			getOPBillCardPanel().setEnabled(true);
		if (crkvos == null || crkvos.length == 0) {
			onBtnAdd();
		} else {
			// 加载现存量
			try {
				for (int i = 0; i < crkvos.length; i++) {
					MdcrkVO mvo = (MdcrkVO) crkvos[i].clone();
					MdcrkVO kylMdCrkVo = new MdProcessBean().queryMdCrkKyl(mvo,
							mvo.getSfbj());
					crkvos[i].setDef1(crkvos[i].getSrkzs().add(
							kylMdCrkVo.getDef1()));// 现存支数
					crkvos[i].setDef2(crkvos[i].getSrkzl().add(
							kylMdCrkVo.getDef2()));// 现存重量
				}
			} catch (BusinessException e) {
				e.printStackTrace();
				MessageDialog.showErrorDlg(dlg, "错误", "现存量加载错误"
						+ e.getMessage());
				return mdsd;
			}
			getOPBillCardPanel().getBillModel().setBodyDataVO(crkvos);
			getOPBillCardPanel().getBillModel().execLoadFormula();// 显示公式
			getOPBillCardPanel().setHeadItem("sfbj", crkvos[0].getSfbj());
			// 如果锁定带出来的数据，则修改背景颜色
			if (mdsd == 1) {
				TableColumnModel tcm = getOPBillCardPanel().getBodyPanel()
						.getTable().getColumnModel();
				for (int i = 0, n = tcm.getColumnCount(); i < n; i++) {
					TableColumn tc = tcm.getColumn(i);
					tc.setCellRenderer(new MyRowRenderer());
				}
			}

		}
		return mdsd;
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
			UIPanel.add(getUIButtonJs(), null);
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
			UIButtonAdd.setBounds(new Rectangle(200, 4, 75, 20));
			UIButtonAdd.setText("增 行");
			UIButtonAdd.setToolTipText("<HTML><B>增行(CTRL + L)</B></HTML>");
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
			UIButtonDel.setBounds(new Rectangle(280, 4, 75, 20));
			UIButtonDel.setText("删  行");
			UIButtonDel.setToolTipText("<HTML><B>删行(CTRL + D)</B></HTML>");
		}
		return UIButtonDel;
	}

	private UIButton getUIButtonJs() {
		if (UIButtonJs == null) {
			UIButtonJs = new UIButton();
			UIButtonJs.setBounds(new Rectangle(360, 4, 75, 20));
			UIButtonJs.setText("计  算");
			UIButtonJs.setToolTipText("<HTML><B>计算(CTRL + X)</B></HTML>");
		}
		return UIButtonJs;
	}

	/**
	 * This method initializes UIButtonSave
	 * 
	 * @return nc.ui.pub.beans.UIButton
	 */
	private UIButton getUIButtonSave() {
		if (UIButtonSave == null) {
			UIButtonSave = new UIButton();
			UIButtonSave.setBounds(new Rectangle(440, 4, 75, 20));
			UIButtonSave.setText("保  存");
			UIButtonSave.setToolTipText("<HTML><B>保存(CTRL + S)</B></HTML>");
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
			UIButtonCan.setBounds(new Rectangle(520, 4, 75, 20));
			UIButtonCan.setText("关  闭");
			UIButtonCan.setToolTipText("<HTML><B>取消(CTRL + X)</B></HTML>");
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
			billCardPanel.loadTemplet("H002", null, m_pkuser, m_pkcorp);
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
		if (e.getSource().equals(getUIButtonJs())) {
			onBtnJs();
		}
	}

	/**
	 * 增加按钮事件处理
	 */
	private void onBtnAdd() {
		// getOPBillCardPanel().setEnabled(true);
		getOPBillCardPanel().getBillModel().addLine();
		String sfbj = getOPBillCardPanel().getHeadItem("sfbj").getValue();
		if (sfbj.equals("true"))
			buttonState(true, true, false, true, true);
		else
			buttonState(true, true, true, true, false);
	}

	/**
	 * 删除按钮事件处理
	 */
	private void onBtnDel() {
		getOPBillCardPanel().tableStopCellEditing();
		getOPBillCardPanel().delLine();
		String sfbj = getOPBillCardPanel().getHeadItem("sfbj").getValue();
		if (sfbj.equals("true"))
			buttonState(true, true, false, true, true);
		else
			buttonState(true, true, true, true, false);

		MdcrkVO[] vos = (MdcrkVO[]) getOPBillCardPanel().getBillData()
				.getBodyValueVOs(nc.vo.ic.md.MdcrkVO.class.getName());
		if (vos == null || vos.length == 0)
			buttonState(true, true, true, true, false);
	}

	/**
	 * 保存按钮事件处理
	 */
	private void onBtnSave() {
		buttonState(true, true, false, true, false);
		// 获取表体数据
		MdcrkVO[] vos = (MdcrkVO[]) getOPBillCardPanel().getBillData()
				.getBodyValueVOs(nc.vo.ic.md.MdcrkVO.class.getName());
		IVOPersistence iVOPersistence = (IVOPersistence) NCLocator
				.getInstance().lookup(IVOPersistence.class.getName());
		try {
			MdProcessBean bean = new MdProcessBean();
			// 计算方式
			String sfbj = getOPBillCardPanel().getHeadItem("sfbj").getValue();
			if (sfbj == "false")
				infoVO.setSfbj(new UFBoolean(false));
			else
				infoVO.setSfbj(new UFBoolean(true));
			// 表体数据构造成较验
			MdcrkVO[] rsvos = bean.buliderMdcrkVOs(vos, infoVO);
			// 删除码单出入库单信息，并对原有单据做入库处理
			boolean ders = bean.deleteAndRK(infoVO);
			// 保存到出入库单位表体
			if (rsvos == null && ders == false)
				throw new BusinessException("保存失败，没有码单明细！");
			if (rsvos == null && ders == true) {
				bean.updateSdbs(infoVO, "0");// 还原码单锁定数据
				bean.updateBillNull(infoVO);// 将数据实出库数量、支数清空
				dlg.setNoutnum(new UFDouble(0));
				dlg.setNoutassistnum(new UFDouble(0));
				// buttonState(true, true, false, true, false);
				throw new BusinessException("码单明细全部删除成功！");
			}
			// 构造并更新现存量主子表
			bean.updateXcl(vos);
			iVOPersistence.insertVOArray(rsvos);
			// 更新出库单表体数据库中数据
			bean.updateBill(dlg, rsvos);
			// 将锁定记录标识为无效
			bean.updateSdbs(infoVO, "2");
			// getUIButtonCan().setText("关 闭");
			// buttonState(true, true, false, true, false);
			MessageDialog.showWarningDlg(dlg, "提示", "保存成功！");
		} catch (BusinessException e) {
			e.printStackTrace();
			MessageDialog.showWarningDlg(dlg, "提示", e.getMessage());
		}
	}

	/**
	 * 取消按钮事件处理
	 */
	private void onBtnCan() {
		dlg.closeOK();
	}

	/**
	 * 计算按钮
	 * 
	 */
	private void onBtnJs() {
		// 获取表体数据
		MdcrkVO[] vos = (MdcrkVO[]) getOPBillCardPanel().getBillData()
				.getBodyValueVOs(nc.vo.ic.md.MdcrkVO.class.getName());
		IVOPersistence iVOPersistence = (IVOPersistence) NCLocator
				.getInstance().lookup(IVOPersistence.class.getName());
		// 计算方式
		String sfbj = getOPBillCardPanel().getHeadItem("sfbj").getValue();
		if (sfbj == "false")
			infoVO.setSfbj(new UFBoolean(false));
		else
			infoVO.setSfbj(new UFBoolean(true));
		MdProcessBean bean = new MdProcessBean();
		try {
			MdcrkVO[] rsvos = bean.buliderMdcrkVOs(vos, infoVO);
			if (rsvos == null)
				throw new BusinessException("请维护码单！");
			Map kylMap = new HashMap();
			if (crkvos != null)
				for (int j = 0; j < crkvos.length; j++)
					kylMap.put(crkvos[j].getPk_mdxcl_b(), crkvos[j].getDef1());

			for (int i = 0; i < rsvos.length; i++) {
				if (kylMap.get(rsvos[i].getPk_mdxcl_b()) == null) {
					// 查询码单出入库可用量VO
					MdcrkVO mvo = (MdcrkVO) rsvos[i].clone();
					MdcrkVO kylMdCrkVo = new MdProcessBean().queryMdCrkKyl(mvo,
							new UFBoolean(true));
					rsvos[i].setDef1(kylMdCrkVo.getDef1());
					// rsvos[i].setDef2(kylMdCrkVo.getDef2());
				} else {
					rsvos[i].setDef1((UFDouble) kylMap.get(rsvos[i]
							.getPk_mdxcl_b()));
				}
			}
			UFDouble gbzl = new UFDouble(getOPBillCardPanel().getHeadItem(
					"gbzl").getValueObject().toString());
			rsvos = MDUtils.mdBJ(rsvos, gbzl);// 磅计计算
			getOPBillCardPanel().getBillModel().setBodyDataVO(rsvos);
			getOPBillCardPanel().getBillModel().execLoadFormula();// 显示公式
			buttonState(true, true, true, true, false);
			MessageDialog.showWarningDlg(dlg, "提示", "计算成功！");
		} catch (BusinessException e) {
			e.printStackTrace();
			MessageDialog.showWarningDlg(dlg, "提示", e.getMessage());
		}

	}

	/**
	 * This method initializes CountUIBtn
	 * 
	 * @return nc.ui.pub.beans.UIButton
	 */

	public String getMoid() {
		return moid;
	}

	public void setMoid(String moid) {
		this.moid = moid;
	}

	public String getPk_moid() {
		return pk_moid;
	}

	public void setPk_moid(String pk_moid) {
		this.pk_moid = pk_moid;
	}

	public void afterEdit(BillEditEvent arg0) {
		if (arg0.getKey().equals("jbh")) {
			String[] s = getOPBillCardPanel().getBodyItem(arg0.getKey())
					.getEditFormulas();
			if (s != null && s.length > 0) {
				getOPBillCardPanel().execBodyFormulas(arg0.getRow(), s);
			}
			String sfbj = getOPBillCardPanel().getHeadItem("sfbj").getValue();
			MdcrkVO[] vos = (MdcrkVO[]) getOPBillCardPanel().getBillData()
					.getBodyValueVOs(nc.vo.ic.md.MdcrkVO.class.getName());
			MdcrkVO evo = vos[arg0.getRow()]; // 编辑的VO
			UFBoolean bsfbj = new UFBoolean(true);
			if (sfbj.equals("false"))
				bsfbj = new UFBoolean(false);
			try {
				// 查询码单出入库可用量VO
				MdcrkVO kylMdCrkVo = new MdProcessBean().queryMdCrkKyl(evo,
						bsfbj);
				vos[arg0.getRow()] = kylMdCrkVo;
				getOPBillCardPanel().getBillModel().setBodyDataVO(vos);
				getOPBillCardPanel().getBillModel().execLoadFormula();// 显示公式
				if (bsfbj.booleanValue() == true)
					buttonState(true, true, false, true, true);
				else
					buttonState(true, true, true, true, false);
			} catch (BusinessException e) {
				e.printStackTrace();
				MessageDialog.showWarningDlg(dlg, "提示", "可用量查询出错!");
				return;
			}

		}
		// 编辑了支数
		if (arg0.getKey().equals("srkzs")) {
			MdcrkVO[] vos = (MdcrkVO[]) getOPBillCardPanel().getBillData()
					.getBodyValueVOs(nc.vo.ic.md.MdcrkVO.class.getName());
			String sfbj = getOPBillCardPanel().getHeadItem("sfbj").getValue();
			if (sfbj.equals("false")) { // 理计
				MdcrkVO evo = vos[arg0.getRow()];
				UFDouble zl = evo.getDef2(); // 重量
				UFDouble zs = evo.getDef1();// 支数
				if (zs == null || zl == null) {
					MessageDialog.showWarningDlg(dlg, "提示", "请先选择码单！");
					return;
				}
				if (evo.getSrkzs().doubleValue() > zs.doubleValue()) {
					MessageDialog.showWarningDlg(dlg, "提示", "码单出库支数不能大于可用量支数"
							+ zs.doubleValue());
					vos[arg0.getRow()].setSrkzl(zl);
					vos[arg0.getRow()].setSrkzs(zs);
					getOPBillCardPanel().getBillData().setBodyValueVO(vos);
					getOPBillCardPanel().getBillModel().execLoadFormula();// 显示公式
					return;
				}
				if (evo.getSrkzs().doubleValue() == zs.doubleValue()) {
					vos[arg0.getRow()].setSrkzl(zl);
					getOPBillCardPanel().getBillData().setBodyValueVO(vos);
					getOPBillCardPanel().getBillModel().execLoadFormula();// 显示公式
					buttonState(true, true, true, true, false);
					return;
				}
				UFDouble dwzl = zl.div(zs, MDConstants.ZL_XSW); // 单位重量。
				UFDouble ufzl = evo.getSrkzs().multiply(dwzl,
						MDConstants.ZL_XSW);// 重量
				vos[arg0.getRow()].setSrkzl(ufzl);
				getOPBillCardPanel().getBillData().setBodyValueVO(vos);
				getOPBillCardPanel().getBillModel().execLoadFormula();// 显示公式
				buttonState(true, true, true, true, false);
			} else { // 磅计
				buttonState(true, true, false, true, true);
				MdcrkVO evo = vos[arg0.getRow()];
				UFDouble zs = evo.getDef1();// 支数
				if (zs == null) {
					MessageDialog.showWarningDlg(dlg, "提示", "请先选择码单！");
					return;
				}
				if (evo.getSrkzs().doubleValue() > zs.doubleValue()) {
					MessageDialog.showWarningDlg(dlg, "提示", "码单出库支数不能大于可用量支数"
							+ zs.doubleValue());
					vos[arg0.getRow()].setSrkzs(evo.getDef1());
					getOPBillCardPanel().getBillData().setBodyValueVO(vos);
					getOPBillCardPanel().getBillModel().execLoadFormula();// 显示公式
				}
			}

		}
		if (arg0.getKey().equals("sfbj")) {
			String usfbj = (String) arg0.getValue();
			if (usfbj.equals("true")) {
				getOPBillCardPanel().getHeadItem("gbzl").setEdit(true);
				infoVO.setSfbj(new UFBoolean(true));
			} else {
				infoVO.setSfbj(new UFBoolean(false));
				getOPBillCardPanel().getHeadItem("gbzl").setEdit(false);
			}
			getOPBillCardPanel().getBillModel().setBodyDataVO(null);
			onBtnAdd();
			buttonState(true, true, false, true, false);
		}
		// 过磅重量
		if (arg0.getKey().equals("gbzl")) {
			buttonState(true, true, false, true, true);
		}
	}

	public void bodyRowChange(BillEditEvent arg0) {
		// TODO Auto-generated method stub

	}

	public boolean beforeEdit(BillEditEvent e) {
		if (e.getKey().equals("jbh")) {
			BillItem jbh = getOPBillCardPanel().getBodyItem("jbh");
			UIRefPane jbhPa = (UIRefPane) jbh.getComponent();
			String sqlWhere = " pk_corp='" + infoVO.getCorpVo().getPk_corp()
					+ "' and dr=0 and zhishu>0  and ccalbodyidb='"
					+ infoVO.getCcalbodyidb() + "' and cwarehouseidb='"
					+ infoVO.getCwarehouseidb() + "' and cinvbasid='"
					+ infoVO.getPk_invbasdoc() + "' and cinventoryidb='"
					+ infoVO.getPk_invmandoc() + "'";
			jbhPa.setWhereString(sqlWhere);
		}
		return true;

	}

	public void buttonState(boolean bolean_AddLine, boolean bolean_DelLine,
			boolean bolean_Save, boolean bolean_Cancel, boolean bolean_Js) {
		getUIButtonAdd().setEnabled(bolean_AddLine);
		getUIButtonDel().setEnabled(bolean_DelLine);
		getUIButtonSave().setEnabled(bolean_Save);
		getUIButtonCan().setEnabled(bolean_Cancel);
		getUIButtonJs().setEnabled(bolean_Js);
	}

}
