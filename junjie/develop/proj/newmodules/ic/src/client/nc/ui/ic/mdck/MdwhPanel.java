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
import nc.itf.uap.IUAPQueryBS;
import nc.itf.uap.IVOPersistence;
import nc.jdbc.framework.processor.ColumnProcessor;
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
import nc.ui.pub.bill.BillModel;
import nc.vo.bd.access.BddataVO;
import nc.vo.ic.md.MdcrkTempVO;
import nc.vo.ic.md.MdcrkVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.ValueObject;
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

	private boolean b_fjs = false; // 销售订单锁定的数据是否为非计算的类型

	private boolean ufsfbj = false;// 是否磅计

	public MdwhPanel(ChInfoVO infoVO, MdwhDlg dlg) {
		super();
		this.infoVO = infoVO;
		this.dlg = dlg;
		initialize();
		int rs = initDate();

		// 设置非计算的标识
		getOPBillCardPanel().setHeadItem("fjs", new UFBoolean(b_fjs));
		if (b_fjs == true) {
			getOPBillCardPanel().setHeadItem("sfbj", new UFBoolean(false));
			getOPBillCardPanel().getHeadItem("sfbj").setEnabled(false);
			getOPBillCardPanel().getHeadItem("gbzl").setEnabled(false);
			getOPBillCardPanel().getBodyItem("srkzl").setEnabled(true);
		}

		if (infoVO.getFbillflag() == 3)
			buttonState(false, false, false, true, false);
		else {
			if (getOPBillCardPanel().getHeadItem("sfbj").getValue().equals(
					"true"))
				buttonState(true, true, false, true, true);
			else {
				buttonState(true, true, true, true, false);
				getOPBillCardPanel().getHeadItem("gbzl").setEdit(false);
			}

		}
		// if (rs == 1)

		// buttonState(true, true, false, true, true);
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
		getOPBillCardPanel().setHeadItem("sfbj", new UFBoolean(false));
		getOPBillCardPanel().execHeadLoadFormulas();
		int mdsd = 0;
		try {
			crkvos = new MdProcessBean().queryCrkVOS(infoVO);
			// 如果没有出库记录，则查询锁定记录
			if (crkvos == null) {
				crkvos = new MdProcessBean().queryCrkVOSByXsdd(infoVO, dlg);
				mdsd = 1;
			}
			//2010-12-04 MeiChao 
			//如果没有出库记录,也没有锁定记录,并且其单据类型为{ 4K(转库) }  或  {单据类型为4I(库存其他出)&&上游单据类型为4K}
			//那么去查找nc_mdcrk_temp表(转库单上的 码单出入库信息临时表)
//			if (crkvos == null) {
//				IUAPQueryBS queryBS = (IUAPQueryBS) NCLocator.getInstance()
//						.lookup(IUAPQueryBS.class.getName());
//				Object sourcetype = queryBS.executeQuery(
//						"select t.csourcetype from ic_general_b t where t.cgeneralbid='"
//								+ infoVO.getCgeneralbid()
//								+ "' and t.cbodybilltypecode='4I'",
//						new ColumnProcessor());
//				if ("4K".equals(infoVO.getCbodybilltypecode())
//						|| ("4I".equals(infoVO.getCbodybilltypecode()) && "4K"
//								.equals(sourcetype.toString()))) {
//					crkvos = new MdProcessBean().queryCrkVOS(infoVO);
//					if (crkvos != null) {
//						mdsd = 0;// 如果成功查询到临时表,那么把码单锁定标志重置
//					}
//				}
//			}
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
			// 只要加有历史数据，非计算就不能再编辑
			getOPBillCardPanel().getHeadItem("fjs").setEnabled(false);
			// 是否非计算
			b_fjs = crkvos[0].getDef4().booleanValue();
			// 加载现存量
			try {
				for (int i = 0; i < crkvos.length; i++) {
					MdcrkVO mvo = (MdcrkVO) crkvos[i].clone();
					MdcrkVO kylMdCrkVo = new MdProcessBean().queryMdCrkKyl(mvo,
							mvo.getSfbj());
					// add by ouyangzhb 2011-02-24 修改 出库钢厂重量
//					crkvos[i].setDef1(crkvos[i].getSrkzs().add(
//							kylMdCrkVo.getDef1()));// 现存支数
//					crkvos[i].setDef2(crkvos[i].getSrkzl().add(
//							kylMdCrkVo.getDef2()));// 现存重量
				}
			} catch (BusinessException e) {
				e.printStackTrace();
				MessageDialog.showErrorDlg(dlg, "错误", "现存量加载错误"
						+ e.getMessage());
				return mdsd;
			}
			getOPBillCardPanel().getBillModel().setBodyDataVO(crkvos);
			if (mdsd == 1)
				getOPBillCardPanel().getBillModel().execLoadFormula();// 显示公式
			else {
				String[] s1 = getOPBillCardPanel().getBodyItem("huowei")
						.getLoadFormula();
				s1[1] = ";";
				if (s1 != null && s1.length > 0) {
					for (int n = 0; n < crkvos.length; n++)
						getOPBillCardPanel().execBodyFormulas(n, s1);
				}
			}
			// 是否磅计
			getOPBillCardPanel().setHeadItem("sfbj", crkvos[0].getSfbj());
			if (crkvos[0].getSfbj().booleanValue() == true)
				getOPBillCardPanel().getHeadItem("sfbj").setEnabled(false);

			ufsfbj = crkvos[0].getSfbj().booleanValue();
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
			UIPanel.setPreferredSize(new Dimension(1024, 50));
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
			UIButtonAdd.setBounds(new Rectangle(312, 4, 75, 20));
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
			UIButtonDel.setBounds(new Rectangle(392, 4, 75, 20));
			UIButtonDel.setText("删  行");
			UIButtonDel.setToolTipText("<HTML><B>删行(CTRL + D)</B></HTML>");
		}
		return UIButtonDel;
	}

	private UIButton getUIButtonJs() {
		if (UIButtonJs == null) {
			UIButtonJs = new UIButton();
			UIButtonJs.setBounds(new Rectangle(472, 4, 75, 20));
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
			UIButtonSave.setBounds(new Rectangle(552, 4, 75, 20));
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
			UIButtonCan.setBounds(new Rectangle(632, 4, 75, 20));
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
			billCardPanel.setSize(1024, 650);
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
		//2010-12-03 MeiChao 在转库单(4K)的码单维护中保存时,执行此段代码.
		if ("4K".equals(infoVO.getCbodybilltypecode())) {
			buttonState(true, true, false, true, false);
			// 获取表体数据
			MdcrkVO[] vos = (MdcrkVO[]) getOPBillCardPanel().getBillData()
					.getBodyValueVOs(nc.vo.ic.md.MdcrkVO.class.getName());
			IVOPersistence iVOPersistence = (IVOPersistence) NCLocator
					.getInstance().lookup(IVOPersistence.class.getName());
			try {
				MdProcessBean bean = new MdProcessBean();
				// 计算方式
				String sfbj = getOPBillCardPanel().getHeadItem("sfbj")
						.getValue();
				if (sfbj == "false")
					infoVO.setSfbj(new UFBoolean(false));
				else
					infoVO.setSfbj(new UFBoolean(true));
				// 表体数据构造成较验
				MdcrkVO[] rsvos = bean.buliderMdcrkVOs(vos, infoVO, b_fjs);
				MdcrkTempVO[] tempvos=new MdcrkTempVO[rsvos.length];//开始将构造成的原始码单数据转变为码单Temp并保存
				for(int i=0;i<rsvos.length;i++){
					String[] attributeNames=rsvos[i].getAttributeNames();
					MdcrkTempVO MdcrkTemp=new MdcrkTempVO();
					for(int j=0;j<attributeNames.length;j++){
						MdcrkTemp.setAttributeValue(attributeNames[j], rsvos[i].getAttributeValue(attributeNames[j]));
					}
					tempvos[i]=MdcrkTemp;
				}
				if(crkvos!=null&&crkvos.length>0){
				//获取当前页面的原生VO,(根据页面的出入库码单VO 获取原始的  转库码单VO)
				MdcrkTempVO[] oldtempvos=new MdcrkTempVO[crkvos.length];//
				for(int i=0;i<crkvos.length;i++){
					String[] attributeNames=crkvos[i].getAttributeNames();
					MdcrkTempVO MdcrkTemp=new MdcrkTempVO();
					for(int j=0;j<attributeNames.length;j++){
						MdcrkTemp.setAttributeValue(attributeNames[j], crkvos[i].getAttributeValue(attributeNames[j]));
					}
					oldtempvos[i]=MdcrkTemp;
				}
				//删除历史转库码单vo
				iVOPersistence.deleteVOArray(oldtempvos);
				}
				//保存新的MdcrkTempVO到nc_mdcrk_temp表中
				iVOPersistence.insertVOArray(tempvos);
				
				
				// 更新出库单表体数据库中数据
				bean.updateBill(dlg, rsvos);
				onBtnCan();
			} catch (BusinessException e) {
				e.printStackTrace();
				MessageDialog.showWarningDlg(dlg, "提示", e.getMessage());
				buttonState(true, true, true, true, false);
			}
			

		} else {
			buttonState(true, true, false, true, false);
			// 获取表体数据
			MdcrkVO[] vos = (MdcrkVO[]) getOPBillCardPanel().getBillData()
					.getBodyValueVOs(nc.vo.ic.md.MdcrkVO.class.getName());
			IVOPersistence iVOPersistence = (IVOPersistence) NCLocator
					.getInstance().lookup(IVOPersistence.class.getName());
			try {
				MdProcessBean bean = new MdProcessBean();
				// 计算方式
				String sfbj = getOPBillCardPanel().getHeadItem("sfbj")
						.getValue();
				if (sfbj == "false")
					infoVO.setSfbj(new UFBoolean(false));
				else
					infoVO.setSfbj(new UFBoolean(true));
				// 表体数据构造成较验
				MdcrkVO[] rsvos = bean.buliderMdcrkVOs(vos, infoVO, b_fjs);
				// 删除码单出入库单信息，并对原有单据做入库处理
				boolean ders = bean.deleteAndRK(infoVO);
				// 保存到出入库单位表体
				if (rsvos == null && ders == false)
					throw new BusinessException("保存失败，没有码单明细！");
				if (rsvos == null && ders == true) {
					bean.updateSdbs(infoVO, "0");// 还原码单锁定数据
					dlg.setNoutnum(null);
					dlg.setNoutassistnum(null);
					dlg.setSfsqmd(new UFBoolean(true));// 是否删除码单
					onBtnCan();
					return;
				}
				// 构造并更新现存量主子表
				bean.updateXcl(vos);
				iVOPersistence.insertVOArray(rsvos);
				// 更新出库单表体数据库中数据
				//add by ouyangzhb 2012-02-10 调用新的方法（针对退库的修改）
				bean.updateBill_new(dlg, rsvos,infoVO);
				// 将锁定记录标识为无效
				bean.updateSdbs(infoVO, "2");
				onBtnCan();
			} catch (BusinessException e) {
				e.printStackTrace();
				MessageDialog.showWarningDlg(dlg, "提示", e.getMessage());
				buttonState(true, true, true, true, false);
			}
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
			MdcrkVO[] rsvos = bean.buliderMdcrkVOs(vos, infoVO, b_fjs);
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
					
					//add by ouyangzhb 2011-09-15 出库钢厂重量应该为实际的出库重量，而不应该是现存的重量，需要注释掉
//					rsvos[i].setDef1(kylMdCrkVo.getDef1());
					
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
			//add by ouyangzhb 2012-08-13 设置码单参照允许多选
			BillItem jbh = getOPBillCardPanel().getBodyItem("jbh");
			UIRefPane jbhPa = (UIRefPane) jbh.getComponent();
			String[] pks = jbhPa.getRefPKs();
			int len = pks==null?0: pks.length;
			for (int j = 0; j < len; j++) {
				if (pks != null) {
					if (j > 0) {
						getOPBillCardPanel().insertLine();
					}
				}
			}
			jbhPa.setPKs(null);
			
			String[] s = getOPBillCardPanel().getBodyItem(arg0.getKey())
					.getEditFormulas();
			MdcrkVO[] vos = (MdcrkVO[]) getOPBillCardPanel().getBillData()
			.getBodyValueVOs(nc.vo.ic.md.MdcrkVO.class.getName());
			for(int i=0;pks !=null &&i<pks.length;i++){
				 getOPBillCardPanel().getBillModel().setValueAt(pks[i], arg0.getRow()+i, "pk_mdxcl_b"); 
				 vos[arg0.getRow()+i].setPk_mdxcl_b(pks[i]);
			if (s != null && s.length > 0) {
				getOPBillCardPanel().execBodyFormulas(arg0.getRow()+i, s);
			}
			String sfbj = getOPBillCardPanel().getHeadItem("sfbj").getValue();
			MdcrkVO evo = vos[arg0.getRow()+i]; // 编辑的VO
			UFBoolean bsfbj = new UFBoolean(true);
			if (sfbj.equals("false"))
				bsfbj = new UFBoolean(false);
			try {
				// 查询码单出入库可用量VO
				MdcrkVO kylMdCrkVo = new MdProcessBean().queryMdCrkKyl(evo,
						bsfbj);
				vos[arg0.getRow()+i] = kylMdCrkVo;
				getOPBillCardPanel().getBillModel().setBodyDataVO(vos);
				getOPBillCardPanel().getBillModel().execLoadFormula();// 显示公式
				//2010-12-30 MeiChao add 每次选择参照之后,将钢厂重量放到临时字段上去,后面修改支数时,可以计算出钢厂重量(不得已而为之)
				getOPBillCardPanel().getBillModel().setValueAt(kylMdCrkVo.getDef1(), arg0.getRow()+i, "myrefweighttemp");
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
			//add by ouyangzhb 2012-08-13 end

		}
		// 编辑了支数
		if (arg0.getKey().equals("srkzs")) {
			MdcrkVO[] vos = (MdcrkVO[]) getOPBillCardPanel().getBillData()
					.getBodyValueVOs(nc.vo.ic.md.MdcrkVO.class.getName());
			String sfbj = getOPBillCardPanel().getHeadItem("sfbj").getValue();
			if (sfbj.equals("false")) { // 理计
				MdcrkVO evo = vos[arg0.getRow()];
				UFDouble zl = evo.getDef2(); // 重量
				UFDouble zs = evo.getDef3();// 支数
				//2010-12-30 MeiChao 钢厂可用总重量
				UFDouble myrefweighttemp=new UFDouble(getOPBillCardPanel().getBillModel().getValueAt(arg0.getRow(), "myrefweighttemp").toString());
				
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
					getOPBillCardPanel().getBillModel().setValueAt(myrefweighttemp.toString(), arg0.getRow(), "myrefweighttemp");//赋值
					return;
				}
				if (evo.getSrkzs().doubleValue() == zs.doubleValue()) {
					vos[arg0.getRow()].setSrkzl(zl);
					getOPBillCardPanel().getBillData().setBodyValueVO(vos);
					getOPBillCardPanel().getBillModel().execLoadFormula();// 显示公式
					//2010-12-30 MeiChao 开始计算
					UFDouble newfactoryweight=myrefweighttemp.multiply(evo.getSrkzs()).div(zs);//当前重量
					getOPBillCardPanel().getBillModel().setValueAt(newfactoryweight, arg0.getRow(), "def1");//赋值
					getOPBillCardPanel().getBillModel().setValueAt(myrefweighttemp.toString(), arg0.getRow(), "myrefweighttemp");//赋值
					
					buttonState(true, true, true, true, false);
					return;
				}
				UFDouble dwzl = zl.div(zs, MDConstants.ZL_XSW); // 单位重量。
				UFDouble ufzl = evo.getSrkzs().multiply(dwzl,
						MDConstants.ZL_XSW);// 重量
				vos[arg0.getRow()].setSrkzl(ufzl);
				getOPBillCardPanel().getBillData().setBodyValueVO(vos);
				getOPBillCardPanel().getBillModel().execLoadFormula();// 显示公式
				//2010-12-30 MeiChao 开始计算
				UFDouble newfactoryweight=myrefweighttemp.multiply(evo.getSrkzs()).div(zs);//当前重量
				getOPBillCardPanel().getBillModel().setValueAt(newfactoryweight, arg0.getRow(), "def1");//赋值
				getOPBillCardPanel().getBillModel().setValueAt(myrefweighttemp.toString(), arg0.getRow(), "myrefweighttemp");//赋值
				buttonState(true, true, true, true, false);
			} else { // 磅计
				buttonState(true, true, false, true, true);
				MdcrkVO evo = vos[arg0.getRow()];
				UFDouble zs = evo.getDef3();// 支数
				//2010-12-30 MeiChao 钢厂可用总重量
				UFDouble myrefweighttemp=new UFDouble(getOPBillCardPanel().getBillModel().getValueAt(arg0.getRow(), "myrefweighttemp").toString());
				
				if (zs == null) {
					MessageDialog.showWarningDlg(dlg, "提示", "请先选择码单！");
					return;
				}
				if (evo.getSrkzs().doubleValue() > zs.doubleValue()) {
					MessageDialog.showWarningDlg(dlg, "提示", "码单出库支数不能大于可用量支数"
							+ zs.doubleValue());
					vos[arg0.getRow()].setSrkzs(evo.getDef3());
					getOPBillCardPanel().getBillData().setBodyValueVO(vos);
					getOPBillCardPanel().getBillModel().execLoadFormula();// 显示公式
					getOPBillCardPanel().getBillModel().setValueAt(myrefweighttemp.toString(), arg0.getRow(), "myrefweighttemp");//赋值
				
				}
				UFDouble newfactoryweight=myrefweighttemp.multiply(evo.getSrkzs()).div(zs);//当前重量
				getOPBillCardPanel().getBillModel().setValueAt(newfactoryweight, arg0.getRow(), "def1");//赋值
				getOPBillCardPanel().getBillModel().setValueAt(myrefweighttemp.toString(), arg0.getRow(), "myrefweighttemp");//赋值
			}

		}
		if (arg0.getKey().equals("sfbj")) {
			if (ufsfbj == true)
				return;
			String usfbj = (String) arg0.getValue();
			if (usfbj.equals("true")) {
				getOPBillCardPanel().getHeadItem("gbzl").setEdit(true);
				infoVO.setSfbj(new UFBoolean(true));
				ufsfbj = true;
				getOPBillCardPanel().getHeadItem("sfbj").setEnabled(false);
			} else {
				infoVO.setSfbj(new UFBoolean(false));
				getOPBillCardPanel().getHeadItem("gbzl").setEdit(false);
			}
			MdcrkVO[] vos = (MdcrkVO[]) getOPBillCardPanel().getBillData()
					.getBodyValueVOs(nc.vo.ic.md.MdcrkVO.class.getName());
			for (int i = 0; i < vos.length; i++)
				vos[i].setSrkzl(null);
			getOPBillCardPanel().getBillData().setBodyValueVO(vos);
			getOPBillCardPanel().getBillModel().execLoadFormula();// 显示公式
			buttonState(true, true, false, true, true);
		}
		// 过磅重量
		if (arg0.getKey().equals("gbzl")) {
			buttonState(true, true, false, true, true);
		}
		// 非计算
		if (arg0.getKey().equals("fjs")) {
			String fjsStr = (String) arg0.getValue();
			if (fjsStr.equals("true")) {
				getOPBillCardPanel().getHeadItem("sfbj").setValue(
						new UFBoolean(false));
				getOPBillCardPanel().getHeadItem("sfbj").setEdit(false);
				getOPBillCardPanel().getHeadItem("gbzl").setEdit(false);
				getOPBillCardPanel().getBodyItem("srkzl").setEnabled(true);
				b_fjs = true;
			} else {
				getOPBillCardPanel().getHeadItem("sfbj").setValue(
						new UFBoolean(false));
				getOPBillCardPanel().getHeadItem("sfbj").setEdit(true);
				getOPBillCardPanel().getHeadItem("gbzl").setEdit(false);
				getOPBillCardPanel().getBodyItem("srkzl").setEnabled(false);
				b_fjs = false;
			}
			getOPBillCardPanel().getBillModel().setBodyDataVO(null);
			onBtnAdd();
			buttonState(true, true, false, true, false);
		}
		//2010-11-25 MeiChao add begin
		if(arg0.getKey().equals("box")){//如果修改了货位编码
			getOPBillCardPanel().getBillModel().execLoadFormula();
		}
		//2010-11-25 MeiChao add end
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
					+ infoVO.getPk_invmandoc() + "' and kyzs>0 ";//2011-01-03 MeiChao 增加 可用支数>0 的条件
			/**
			 * add by ouyangzhb 2012-08-13 码单的参照界面不需要有是否“非计算”条件的限制
			 
			if (b_fjs == true)
				sqlWhere += " and def4='Y'";
			else
				sqlWhere += " and def4='N'";
			*/
			jbhPa.setWhereString(sqlWhere);
			
			//add by ouyangzhb 2012-08-13 码单的参照允许多选
			jbhPa.setMultiSelectedEnabled(true);
			//add by ouyangzhb 2013-04-28 清缓存
			jbhPa.getRefModel().clearCacheData();
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
