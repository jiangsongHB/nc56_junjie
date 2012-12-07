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
import nc.vo.fp.combase.pub01.IBillStatus;
import nc.vo.ic.md.MdcrkTempVO;
import nc.vo.ic.md.MdcrkVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.ValueObject;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDouble;

public class MdGbPanel extends UIPanel implements ActionListener,
		BillEditListener, BillEditListener2 {

	MDGbftdialog dlg = null;

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

	private ChInfoVO[] infoVOs;

	private String pk_moid = null; // @jve:decl-index=0:

	private MdcrkVO[] crkvos = null;

	private boolean b_fjs = false; // 销售订单锁定的数据是否为非计算的类型

	private boolean ufsfbj = false;// 是否磅计

	public MdGbPanel(ChInfoVO[] infoVOs, MDGbftdialog dlg) {
		super();
		this.infoVOs = infoVOs;
		this.dlg = dlg;
		initialize();
		 initDate();
		if (infoVOs[0].getFbillflag() == 3)
			buttonState(false, false, false, true, false);
		else {
			buttonState(true, true, false, true, true);
		}
		
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

	private void initDate() {
			try {
				crkvos = new MdProcessBean().queryCrkVOSByArray(infoVOs);
			} catch (BusinessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(crkvos==null){
				return ;
			}
			getOPBillCardPanel().getBillModel().setBodyDataVO(crkvos);
			getOPBillCardPanel().setEnabled(true);
			getOPBillCardPanel().getBillModel().execLoadFormula();// 显示公式
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
			UIPanel.add(getUIButtonSave(), null);
			UIPanel.add(getUIButtonCan(), null);
			UIPanel.add(getUIButtonJs(), null);
		}
		return UIPanel;
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
			billCardPanel.loadTemplet("H005", null, m_pkuser, m_pkcorp);
			billCardPanel.setTatolRowShow(true);
		}
		return billCardPanel;
	}

	public void actionPerformed(ActionEvent e) {
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
	 * 保存按钮事件处理
	 */
	private void onBtnSave() {
		//2010-12-03 MeiChao 在转库单(4K)的码单维护中保存时,执行此段代码.
			buttonState(true, true, false, true, false);
			// 获取表体数据
			MdcrkVO[] vos = (MdcrkVO[]) getOPBillCardPanel().getBillData()
					.getBodyValueVOs(nc.vo.ic.md.MdcrkVO.class.getName());
			IVOPersistence iVOPersistence = (IVOPersistence) NCLocator
					.getInstance().lookup(IVOPersistence.class.getName());
			try {
				MdProcessBean bean = new MdProcessBean();
				HashMap<String,UFDouble> map = new HashMap<String,UFDouble>();
				for(int i=0;i<vos.length;i++){
					map.put(vos[i].getPk_mdcrk(), vos[i].getSrkzl());
					
				}
				for(int j=0;j<crkvos.length;j++){
					crkvos[j].setSrkzl(map.get(crkvos[j].getPk_mdcrk()));
				}
				// 构造并更新现存量主子表
				bean.updateXcl(crkvos);
				iVOPersistence.updateVOArray(crkvos);
				Map<String,UFDouble> noutnummap = new HashMap<String,UFDouble>();//
				Map<String,UFDouble> noutassnummap = new HashMap<String,UFDouble>();
				for(int i=0;i<vos.length;i++){
					if(noutnummap.containsKey(vos[i].getCgeneralbid())){
						noutnummap.put(vos[i].getCgeneralbid(), noutnummap.get(vos[i].getCgeneralbid()).add(vos[i].getSrkzl()));
						noutassnummap.put(vos[i].getCgeneralbid(), noutassnummap.get(vos[i].getCgeneralbid()).add(vos[i].getSrkzs()));
					}else{
						noutnummap.put(vos[i].getCgeneralbid(),vos[i].getSrkzl());
						noutassnummap.put(vos[i].getCgeneralbid(),vos[i].getSrkzs());
					}
				}
				dlg.setNoutassnummap(noutassnummap);
				dlg.setNoutnummap(noutnummap);
				
				onBtnCan();
			} catch (BusinessException e) {
				e.printStackTrace();
				MessageDialog.showWarningDlg(dlg, "提示", e.getMessage());
				buttonState(true, true, true, true, false);
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
		
		try {
			Map kylMap = new HashMap();
			if (crkvos != null)
				for (int j = 0; j < crkvos.length; j++)
					kylMap.put(crkvos[j].getPk_mdxcl_b(), crkvos[j].getDef1());

			for (int i = 0; i < vos.length; i++) {
					vos[i].setDef1((UFDouble) kylMap.get(vos[i]
							.getPk_mdxcl_b()));
			}
			UFDouble gbzl = new UFDouble(getOPBillCardPanel().getHeadItem(
					"gbzl").getValueObject().toString());
			/**
			 * add by ouyangzhb 2012-12-06 用新的算法计算：，
			 * （1）对于有验收宽度和验收长度的，按照码单的厚（规格）×验收宽度×验收长度得到体积来合计，然后按照每个码单所占比例进行分摊，余数放在最后一个码单上；
			 * （2）如果只有长度的，则按照理算系数×长度合计，然后按照每个码单所占比例进行分摊，余数放在最后一个码单上。
			 */
			vos = MDUtils.mdGBBJ(vos, gbzl);// 磅计计算
			getOPBillCardPanel().getBillModel().setBodyDataVO(vos);
			int row = getOPBillCardPanel().getBillModel().getRowCount();
			for(int i=0;i<row;i++){
				getOPBillCardPanel().getBillModel().setRowState(i, BillModel.MODIFICATION);
			}
			getOPBillCardPanel().getBillModel().execLoadFormula();// 显示公式
			buttonState(true, true, true, true, true);
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

	public void afterEdit(BillEditEvent arg0) {}

	public void bodyRowChange(BillEditEvent arg0) {
		// TODO Auto-generated method stub

	}

	public boolean beforeEdit(BillEditEvent e) {
		return true;
		
	}

	public void buttonState(boolean bolean_AddLine, boolean bolean_DelLine,
			boolean bolean_Save, boolean bolean_Cancel, boolean bolean_Js) {
		getUIButtonSave().setEnabled(bolean_Save);
		getUIButtonCan().setEnabled(bolean_Cancel);
		getUIButtonJs().setEnabled(bolean_Js);
	}

}
