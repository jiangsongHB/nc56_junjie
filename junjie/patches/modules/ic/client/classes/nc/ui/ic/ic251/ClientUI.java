package nc.ui.ic.ic251;

import java.awt.Dialog;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.util.ArrayList;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ArrayProcessor;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.ui.ic.pub.bill.GeneralBillClientUI;
import nc.ui.ic.pub.bill.GeneralBillHelper;
import nc.ui.ic.pub.bill.GeneralBillUICtl;
import nc.ui.ic.pub.bill.IButtonManager;
import nc.ui.ic.pub.bill.ICButtonConst;
import nc.ui.ic.pub.bill.QueryDlgHelp;
import nc.ui.ic.pub.bill.query.QueryConditionDlgForBill;
import nc.ui.ic.pub.locatorref.LocatorRefPane;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.FramePanel;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.beans.constenum.DefaultConstEnum;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillItem;
import nc.ui.scm.ic.exp.GeneralMethod;
import nc.vo.ic.pub.GenMethod;
import nc.vo.ic.pub.bill.GeneralBillItemVO;
import nc.vo.ic.pub.bill.GeneralBillVO;
import nc.vo.ic.pub.bill.IItemKey;
import nc.vo.ic.pub.bill.QryInfoConst;
import nc.vo.ic.pub.check.VOCheck;
import nc.vo.ic.pub.locator.LocatorVO;
import nc.vo.ic.pub.sn.SerialVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.constant.ic.BillMode;
import nc.vo.scm.constant.ic.InOutFlag;
import nc.vo.scm.ic.bill.InvVO;
import nc.vo.scm.ic.bill.WhVO;
import nc.vo.scm.ic.exp.ICDateException;
import nc.vo.scm.ic.exp.ICHeaderNullFieldException;
import nc.vo.scm.ic.exp.ICLocatorException;
import nc.vo.scm.ic.exp.ICNullFieldException;
import nc.vo.scm.ic.exp.ICNumException;
import nc.vo.scm.ic.exp.ICPriceException;
import nc.vo.scm.ic.exp.ICRepeatException;
import nc.vo.scm.ic.exp.ICSNException;
import nc.vo.scm.pub.SCMEnv;

/**
 * 货位调整单 创建日期：(2001-11-23 15:39:43)
 * 
 * @author：张欣
 * 
 */
public class ClientUI extends nc.ui.ic.pub.bill.GeneralBillClientUI {
	// 货位参照1,2
	private LocatorRefPane m_refLocator = null, m_refLocator2 = null;

	// 查询对话框
	protected QueryConditionDlgForBill ivjQueryConditionDlg = null;

	private IButtonManager m_buttonManager;

	// add heyq 2010-10-25
	private Map hwtzMap = new HashMap();// 调整的VOMap

	public Map getHwtzMap() {
		return hwtzMap;
	}

	public void setHwtzMap(Map hwtzMap) {
		this.hwtzMap = hwtzMap;
	}

	// end heyq 2010-10-25

	/**
	 * ClientUI2 构造子注解。
	 */
	public ClientUI() {
		super();
		this.initialize();
	}

	/**
	 * ClientUI 构造子注解。 add by liuzy 2007-12-18 根据节点编码初始化单据模版
	 */
	public ClientUI(FramePanel fp) {
		super(fp);
		initialize();
	}

	/**
	 * ClientUI 构造子注解。 nc 2.2 提供的单据联查功能构造子。
	 * 
	 */
	public ClientUI(String pk_corp, String billType, String businessType,
			String operator, String billID) {
		super(pk_corp, billType, businessType, operator, billID);

	}

	protected void appendLocator(GeneralBillVO voBill) {
		GeneralBillItemVO[] voItems = (GeneralBillItemVO[]) voBill
				.getChildrenVO();
		if (voItems != null) {
			UFDouble uf1 = new UFDouble(0);
			SCMEnv.out("body rows=" + voItems.length);
			for (int i = 0; i < voItems.length; i++) {
				voItems[i].setNoutnum(uf1);
			}
		}

		super.appendLocator(voBill);

		boolean isQueried = false;

		if (voItems != null) {
			SCMEnv.out("body rows=" + voItems.length);
			for (int i = 0; i < voItems.length; i++) {
				LocatorVO[] lvos = ((LocatorVO[]) voItems[i].getLocator());

				if (lvos != null && lvos.length > 0) {
					voItems[i].setCspaceid(lvos[0].getCspaceid());
					voItems[i].setVspacename(lvos[0].getVspacename());
					voItems[i]
							.setNoutnum(lvos[0].getNoutspacenum() == null ? lvos[0]
									.getNinspacenum()
									: lvos[0].getNoutspacenum());
					voItems[i]
							.setNoutassistnum(lvos[0].getNoutspaceassistnum());
					voItems[i]
							.setNoutgrossnum(lvos[0].getNoutgrossnum() == null ? lvos[0]
									.getNingrossnum()
									: lvos[0].getNoutgrossnum());

					if (lvos.length > 1) {
						voItems[i].setCspace2id(lvos[1].getCspaceid());
						voItems[i].setVspace2name(lvos[1].getVspacename());

					}
				} else {
					voItems[i].setCspaceid(null);
					voItems[i].setVspacename(null);
				}
			}
		}
	}

	/**
	 * 创建者：王乃军 功能：单据编辑后处理 参数： 返回： 例外： 日期：(2001-5-9 9:23:32) 修改日期，修改人，修改原因，注释标志：
	 * 
	 * 
	 * 
	 * 
	 */
	protected void afterBillEdit(nc.ui.pub.bill.BillEditEvent e) {
		// nc.vo.scm.pub.SCMEnv.out("haha,bill edit/.");
		try {
			if (e.getKey().equals(IItemKey.CALBODY))
				// 库存组织
				afterCalbodyEdit(e);

			int row = getBillCardPanel().getBillTable().getSelectedRow();
			String sItemKey = e.getKey();
			if ("cinventorycode".equals(sItemKey)
					|| "cwarehouseid".equals(sItemKey)) {
				// 货位参照设置

				getBillCardPanel().setBodyValueAt(null, row, "vspace2name");
				getBillCardPanel().setBodyValueAt(null, row, "cspace2id");

				getLocatorRefPane().setParam(getM_voBill().getWh(),
						getM_voBill().getItemInv(row));
				getLocatorRefPane2().setParam(getM_voBill().getWh(),
						getM_voBill().getItemInv(row));
				GeneralBillUICtl.synUi2Vo(getBillCardPanel(), getM_voBill(),
						new String[] { "vspace2name", "cspace2id" }, row);
				// 转出货位在父类中会被清除

			} // 货位改变
			else if (sItemKey.equals("vspacename")
					|| sItemKey.equals("vspace2name"))
				afterSpaceEdit(e);
			else if (getEnvironment().getNumItemKey().equals(sItemKey)
					|| getEnvironment().getAssistNumItemKey().equals(sItemKey)) {
				// 修改了数量，分配数量被父类清空，所以要重置。
				afterSpaceEdit(e);
				checkNum(row);
			} else {
				afterSpaceEdit(e);
			}

		} catch (Exception ee) {
		}

	}

	/**
	 * 创建者：王乃军 功能：表体行列选择改变 参数： 返回： 例外： 日期：(2001-5-9 9:23:32) 修改日期，修改人，修改原因，注释标志：
	 * 
	 * 
	 * 
	 * 
	 */
	protected void afterBillItemSelChg(int iRow, int iCol) {
		// nc.vo.scm.pub.SCMEnv.out("haha,sel chged!");
		String sItemKey = getBillCardPanel().getBillModel().getBodyKeyByCol(
				iCol).trim();
		if (sItemKey.equals("vspacename")) {
			filterSpace(iRow);
		} else if (sItemKey.equals("vspace2name")) {
			filterSpace2(iRow);
		}
	}

	/**
	 * 创建者：王乃军 功能：库存组织改变事件处理 参数：e单据编辑事件 返回： 例外： 日期：(2001-5-8 19:08:05)
	 * 修改日期，修改人，修改原因，注释标志：
	 * 
	 * 
	 * 
	 * 
	 */
	public void afterCalbodyEdit(nc.ui.pub.bill.BillEditEvent e) {
		try {
			super.afterCalbodyEdit(e);

			String sNewID = ((nc.ui.pub.beans.UIRefPane) getBillCardPanel()
					.getHeadItem(e.getKey()).getComponent()).getRefPK();
			// 清空了库存组织
			// 如果当前的仓库不属于
			// 过滤仓库参照
			String sConstraint[] = null;
			if (sNewID != null) {
				sConstraint = new String[1];
				sConstraint[0] = " and csflag='Y' AND pk_calbody='" + sNewID
						+ "'";
			} else {
				sConstraint = new String[1];
				sConstraint[0] = " and csflag='Y' ";
			}

			nc.ui.pub.bill.BillItem bi = getBillCardPanel().getHeadItem(
					IItemKey.WAREHOUSE);
			nc.ui.ic.pub.bill.initref.RefFilter.filtWh(bi, getEnvironment()
					.getCorpID(), sConstraint);
			// 处理:clear warehouse
			nc.ui.pub.bill.BillItem biWh = getBillCardPanel().getHeadItem(
					IItemKey.WAREHOUSE);
			if (biWh != null)
				biWh.setValue(null);

		} catch (Exception e2) {
			nc.vo.scm.pub.SCMEnv.out(e2);
		}

	}

	/**
	 * 创建者：仲瑞庆 功能：货位修改事件处理 参数：e单据编辑事件 返回： 例外： 日期：(2001-5-8 19:08:05)
	 * 修改日期，修改人，修改原因，注释标志：
	 * 
	 * 
	 * 
	 * 
	 */
	public void afterSpaceEdit(nc.ui.pub.bill.BillEditEvent e) {
		// //出货位参照
		nc.ui.pub.beans.UIRefPane refOutSpace = null;
		if (getBillCardPanel().getBodyItem("vspacename") != null)
			refOutSpace = ((nc.ui.pub.beans.UIRefPane) getBillCardPanel()
					.getBodyItem("vspacename").getComponent());

		// 入货位参照
		nc.ui.pub.beans.UIRefPane refInSpace = null;
		if (getBillCardPanel().getBodyItem("vspace2name") != null)
			refInSpace = ((nc.ui.pub.beans.UIRefPane) getBillCardPanel()
					.getBodyItem("vspace2name").getComponent());

		if (refOutSpace == null || refInSpace == null) {
			nc.vo.scm.pub.SCMEnv.out("set locator ref pls.");
			return;
		}
		// 修改列Key
		String sItemKey = e.getKey();
		// 当前行
		int row = e.getRow();

		String cOutsname = null;
		String cOutspaceid = null;
		String cInsname = null;
		String cInspaceid = null;

		if (e.getKey().equals("vspacename")) {
			cInsname = (String) getBillCardPanel().getBodyValueAt(row,
					"vspace2name");
			cInspaceid = (String) getBillCardPanel().getBodyValueAt(row,
					"cspace2id");
			cOutsname = refOutSpace.getRefName();
			cOutspaceid = refOutSpace.getRefPK();
			// 表体参照批填充处理 陈倪娜 2009-08-05
			if (null == cOutspaceid && null != e.getValue()
					&& e.getValue() instanceof DefaultConstEnum) {
				cOutspaceid = (String) ((DefaultConstEnum) e.getValue())
						.getValue();
				cOutsname = ((DefaultConstEnum) e.getValue()).getName();
			}
		} else if (e.getKey().equals("vspace2name")) {// zhy 2005-03-09
			// 加入这个条件是因为数量改变也进入该方法
			cInsname = refInSpace.getRefName();
			cInspaceid = refInSpace.getRefPK();
			if (null == cInspaceid && null != e.getValue()
					&& e.getValue() instanceof DefaultConstEnum) {
				cInspaceid = (String) ((DefaultConstEnum) e.getValue())
						.getValue();
				cInsname = ((DefaultConstEnum) e.getValue()).getName();
			}
			cOutsname = (String) getBillCardPanel().getBodyValueAt(row,
					"vspacename");
			cOutspaceid = (String) getBillCardPanel().getBodyValueAt(row,
					"cspaceid");
		} else {
			cInsname = (String) getBillCardPanel().getBodyValueAt(row,
					"vspace2name");
			cInspaceid = (String) getBillCardPanel().getBodyValueAt(row,
					"cspace2id");

			cOutsname = (String) getBillCardPanel().getBodyValueAt(row,
					"vspacename");
			cOutspaceid = (String) getBillCardPanel().getBodyValueAt(row,
					"cspaceid");
		}

		// 如果ID是空则清除name
		if (cOutspaceid == null)
			cOutsname = null;
		if (cInspaceid == null)
			cInsname = null;
		// String cOutsname = refOutSpace.getRefName();
		// String cOutspaceid = refOutSpace.getRefPK();
		// String cInsname =refInSpace.getRefName();
		// String cInspaceid = refInSpace.getRefPK();
		// 两个货位不能相同
		if (cOutspaceid != null && cOutspaceid.equals(cInspaceid)) {
			showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"4008spec", "UPP4008spec-000145")/* @res "两个货位不能相同,请修改！" */);
			// 清除
			if (sItemKey != null && sItemKey.equals("vspace2name")) {
				cInsname = null;
				cInspaceid = null;
				// m_voBill.setItemValue(row, "cspace2id", null);
				// m_voBill.setItemValue(row, "vspace2name", null);
				// getBillCardPanel().setBodyValueAt(null, row, "vspace2name");
				// getBillCardPanel().setBodyValueAt(null, row, "cspace2id");
				// 清参照数据
				// getLocatorRefPane2().setPK(null);
			} else {
				// m_voBill.setItemValue(row, "cspaceid", null);
				// m_voBill.setItemValue(row, "vspacename", null);
				// getBillCardPanel().setBodyValueAt(null, row, "vspacename");
				// getBillCardPanel().setBodyValueAt(null, row, "cspaceid");
				cOutsname = null;
				cOutspaceid = null;
				// 清参照数据
				// getLocatorRefPane().setPK(null);
			}
		} else
			showHintMessage("");
		// 设置m_vo
		getM_voBill().setItemValue(row, "cspaceid", cOutspaceid);
		getM_voBill().setItemValue(row, "vspacename", cOutsname);
		getM_voBill().setItemValue(row, "cspace2id", cInspaceid);
		getM_voBill().setItemValue(row, "vspace2name", cInsname);

		// 置入界面
		getBillCardPanel().setBodyValueAt(cOutsname, row, "vspacename");
		getBillCardPanel().setBodyValueAt(cOutspaceid, row, "cspaceid");
		getBillCardPanel().setBodyValueAt(cInsname, row, "vspace2name");
		getBillCardPanel().setBodyValueAt(cInspaceid, row, "cspace2id");

		LocatorVO voInSpace = new LocatorVO();
		LocatorVO voOutSpace = new LocatorVO();
		LocatorVO[] lvos = new LocatorVO[2];

		// initliaze to null
		lvos[0] = null;
		lvos[1] = null;
		// 如果分配货位，构造LocatorVO

		// 数量
		UFDouble dTempNum = null, dTempAstNum = null, dTempGrossNum = null;
		Object oTempValue = getBillCardPanel().getBodyValueAt(row, "noutnum");
		if (oTempValue != null && oTempValue.toString().trim().length() > 0)
			dTempNum = new UFDouble(oTempValue.toString());
		oTempValue = getBillCardPanel().getBodyValueAt(row, "noutassistnum");
		if (oTempValue != null && oTempValue.toString().trim().length() > 0)
			dTempAstNum = new UFDouble(oTempValue.toString());
		oTempValue = getBillCardPanel().getBodyValueAt(row, "noutgrossnum");
		if (oTempValue != null && oTempValue.toString().trim().length() > 0)
			dTempGrossNum = new UFDouble(oTempValue.toString());

		// 出
		if (cOutspaceid != null && cOutspaceid.trim().length() > 0) {
			voOutSpace.setCspaceid(cOutspaceid);
			voOutSpace.setVspacename(cOutsname);
			voOutSpace.setNoutspacenum(dTempNum);
			voOutSpace.setNoutspaceassistnum(dTempAstNum);
			voOutSpace.setNoutgrossnum(dTempGrossNum);
			lvos[0] = voOutSpace;
		}
		// 入
		if (cInspaceid != null && cInspaceid.trim().length() > 0) {
			voInSpace.setCspaceid(cInspaceid);
			voInSpace.setVspacename(cInsname);
			voInSpace.setNinspacenum(dTempNum);
			voInSpace.setNinspaceassistnum(dTempAstNum);
			voInSpace.setNingrossnum(dTempGrossNum);
			lvos[1] = voInSpace;
		}
		// 设置
		getM_alLocatorData().set(row, lvos);

		// 如果修改了出货位，清此行的序列号数据
		if (sItemKey != null && sItemKey.equals("vspacename")
				&& getM_alSerialData() != null
				&& getM_alSerialData().size() > row) {
			getM_alSerialData().set(row, null);
			// showHintMessage("修改了转出货位，请重新录入此行的序列号。");
		}
		return;
	}

	/**
	 * 创建者：王乃军 功能：单据编辑事件处理 参数：e单据编辑事件 返回： 例外： 日期：(2001-5-8 19:08:05)
	 * 修改日期，修改人，修改原因，注释标志：
	 * 
	 * 
	 * 
	 * 
	 */
	public boolean beforeBillItemEdit(nc.ui.pub.bill.BillEditEvent e) {
		return true;
	}

	/**
	 * 创建者：王乃军 功能：表体行列选择改变 参数： 返回： 例外： 日期：(2001-5-9 9:23:32) 修改日期，修改人，修改原因，注释标志：
	 * 
	 * 
	 * 
	 * 
	 */
	protected void beforeBillItemSelChg(int iRow, int iCol) {
		// nc.vo.scm.pub.SCMEnv.out("haha,before sel");

	}

	/**
	 * 创建者：王乃军 功能：单据编辑前事件处理 根据 isflargessedit 字段判断赠品列是否可以编辑 参数：e单据编辑事件 返回： 例外：
	 * 日期：(2001-5-8 19:08:05) 修改日期，修改人，修改原因，注释标志： *
	 * 
	 * 
	 */

	public boolean beforeEdit(nc.ui.pub.bill.BillEditEvent e) {
		// add heyq 2010-10-21

		if (e.getKey().equals("noutassistnum") || e.getKey().equals("noutnum")
				|| e.getKey().equals("vspacename")) {
			// 当前行
			int j = getBillCardPanel().getBillTable().getSelectedRow();
			if (j < 0)
				return true;
			String cinvbasid = (String) getBillCardPanel().getBodyValueAt(j,
					"cinvbasid");
			try {
				// 如果不进行码单管理，则返回
				if (querySfmdwf(cinvbasid) == false)
					return true;
				// 库存组织 pk_calbody
				String pk_calbody = getBillCardPanel()
						.getHeadItem("pk_calbody").getValue();
				// 仓库 cwarehouseid
				String cwarehouseid = getBillCardPanel().getHeadItem(
						"cwarehouseid").getValue();
				if (pk_calbody == null || pk_calbody.equals(""))
					throw new BusinessException("库存组织不能为空！");
				if (cwarehouseid == null || cwarehouseid.equals(""))
					throw new BusinessException("仓库不能为空！");
				int t = this.getBillCardPanel().getBillTable().getSelectedRow(); // 行数
				// 辅计量单位
				if (getBillCardPanel().getBodyValueAt(t, "castunitid") == null)
					throw new BusinessException("辅计量单位不能为空！");
				HwtzDialog dlg = new HwtzDialog(getClientEnvironment(),
						pk_calbody, cwarehouseid, cinvbasid);
				dlg.showModal();
				// 选择失败，重新选择
				if (dlg.getSstatus() == 0)
					return false;

				// 设置相关的值 cspaceid

				getBillCardPanel().setBodyValueAt(dlg.getNoutassistnum(), t,
						"noutassistnum"); // 辅助数量
				getM_voBill().setItemValue(t, "noutassistnum",
						dlg.getNoutassistnum());
				BillEditEvent e1 = new BillEditEvent(getBillCardPanel()
						.getBodyItem("noutassistnum").getComponent(),
						getBillCardPanel().getBodyValueAt(t, "noutassistnum"),
						"noutassistnum", t, BillItem.BODY);
				afterEdit(e1);

				getBillCardPanel().setBodyValueAt(dlg.getNoutnum(), t,
						"noutnum"); // 数量
				getM_voBill().setItemValue(t, "noutnum", dlg.getNoutnum());
				BillEditEvent e2 = new BillEditEvent(getBillCardPanel()
						.getBodyItem("noutnum").getComponent(),
						getBillCardPanel().getBodyValueAt(t, "noutnum"),
						"noutnum", t, BillItem.BODY);
				afterEdit(e2);

				getBillCardPanel().setBodyValueAt(dlg.getPk_cspaceid(), t,
						"cspaceid");
				getM_voBill().setItemValue(t, "cspaceid", dlg.getPk_cspaceid());
				getBillCardPanel().execBodyFormula(t, "vspacename");

				this.getHwtzMap().put(cinvbasid, dlg.getZzvoArrays());

			} catch (BusinessException e1) {
				e1.printStackTrace();
				showWarningMessage(e1.getMessage());
				return false;
			}
		}
		// end heyq 2010-10-21
		return true;
	}

	// onButtonClicked

	// add heyq 2010-10-21
	// 判断是否码单维护
	public boolean querySfmdwf(String cinvbasid) throws BusinessException {
		IUAPQueryBS iUAPQueryBS = (IUAPQueryBS) NCLocator.getInstance().lookup(
				IUAPQueryBS.class.getName());
		String sql = "select def20 from bd_invbasdoc where pk_invbasdoc='"
				+ cinvbasid + "'";
		Object[] objs = (Object[]) iUAPQueryBS.executeQuery(sql,
				new ArrayProcessor());
		if (objs == null || objs.length == 0)
			throw new BusinessException("当前存货异常，在存货基本档案中不存在！");
		if (objs[0] == null)
			return false;
		else {
			String bz = (String) objs[0];
			bz = bz.toUpperCase();
			if (bz.equals("Y"))
				return true;
			else
				return false;
		}
	}

	// end heyq 2010-10-21

	/**
	 * 创建人：刘家清 创建时间：2008-5-22 下午08:05:50 创建原因：仓库不处理，在货位调整单中。
	 */
	public boolean beforeEdit(nc.ui.pub.bill.BillItemEvent e) {
		if (null != e && IItemKey.WAREHOUSE.equals(e.getItem().getKey()))
			return true;
		return super.beforeEdit(e);
	}

	/**
	 * UAP提供的编辑前控制
	 * 
	 * @param value
	 * @param row
	 * @param itemkey
	 * @return
	 * 
	 */
	public boolean isCellEditable(boolean value, int row, String itemkey) {

		if (getM_iMode() == BillMode.Browse)
			return false;
		// zhy 加此行，就会先after后before
		getBillCardPanel().stopEditing();
		nc.ui.pub.bill.BillItem bi = getBillCardPanel().getBodyItem(itemkey);
		WhVO voWh = getM_voBill().getWh();
		// zhy2005-04-22非存货列，需要先输存货
		if (!itemkey.equals("cinventorycode")) {
			// 存货编码
			Object oTempInvCode = getBillCardPanel().getBodyValueAt(row,
					"cinventorycode");
			// 存货名
			Object oTempInvName = getBillCardPanel().getBodyValueAt(row,
					"invname");
			// 如果本行未输入存货或清空存货则本行所有列不可编辑。
			if (oTempInvCode == null
					|| oTempInvCode.toString().trim().length() == 0) {
				// 修改人：刘家清 修改日期：2007-9-12下午04:11:32 修改原因：不能把设置Enabled
				// bi.setEnabled(false);
				showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"4008bill", "UPP4008bill-000026")/* @res "请先输入存货!" */);
				return false;
			}
		}
		if (itemkey.equals("vspacename")) {
			if (voWh == null || voWh.getIsLocatorMgt() == null
					|| voWh.getIsLocatorMgt().intValue() == 0) {

				bi.setEnabled(false);
				return false;
			} else {
				filterSpace(row);
				bi.setEnabled(true);
				return true;
			}
		} else if (itemkey.equals("vspace2name")) {
			if (voWh == null || voWh.getIsLocatorMgt() == null
					|| voWh.getIsLocatorMgt().intValue() == 0) {

				bi.setEnabled(false);
				return false;
			} else {
				filterSpace2(row);
				bi.setEnabled(true);
				return true;
			}
		} else
			return super.isCellEditable(value, row, itemkey);

	}

	/**
	 * 创建者：王乃军 功能：检查数量/辅数量必须大于0，同步货位分配数量。 参数： 返回： 例外： 日期：(2001-11-24 12:15:42)
	 * 修改日期，修改人，修改原因，注释标志：
	 */
	protected void checkNum(int row) {
		nc.vo.pub.lang.UFDouble dNum = null;
		// 同步货位分配数量
		LocatorVO[] voaLoc = (LocatorVO[]) getM_alLocatorData().get(row);

		if (getBillCardPanel().getBodyItem(getEnvironment().getNumItemKey()) != null) {
			if (getBillCardPanel().getBodyValueAt(row,
					getEnvironment().getNumItemKey()) != null)
				dNum = (nc.vo.pub.lang.UFDouble) getBillCardPanel()
						.getBodyValueAt(row, getEnvironment().getNumItemKey());
			if (dNum != null && dNum.doubleValue() <= 0) {
				showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"4008spec", "UPP4008spec-000144")/*
															 * @res
															 * "请注意：数量、辅数量均应大于零。"
															 */);
				// 刷新界面
				getBillCardPanel().setBodyValueAt(null, row,
						getEnvironment().getNumItemKey());
				dNum = null;
			}
			// 数量
			if (voaLoc != null && voaLoc.length >= 2) {
				// clear show message
				showHintMessage("");
				// 0==出
				if (voaLoc[0] != null)
					voaLoc[0].setNoutspacenum(dNum);
				// 1==入
				if (voaLoc[1] != null)
					voaLoc[1].setNinspacenum(dNum);
			}
		}

		dNum = null; // clear it .

		if (getBillCardPanel().getBodyItem(
				getEnvironment().getAssistNumItemKey()) != null) {
			if (getBillCardPanel().getBodyValueAt(row,
					getEnvironment().getAssistNumItemKey()) != null)
				dNum = (nc.vo.pub.lang.UFDouble) getBillCardPanel()
						.getBodyValueAt(row,
								getEnvironment().getAssistNumItemKey());
			if (dNum != null && dNum.doubleValue() <= 0) {
				showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"4008spec", "UPP4008spec-000144")/*
															 * @res
															 * "请注意：数量、辅数量均应大于零。"
															 */);
				// 刷新界面
				getBillCardPanel().setBodyValueAt(null, row,
						getEnvironment().getAssistNumItemKey());
				dNum = null;
			}
			// 辅数量
			if (voaLoc != null && voaLoc.length >= 2) {
				// clear show message
				showHintMessage("");
				// 0==出
				if (voaLoc[0] != null)
					voaLoc[0].setNoutspaceassistnum(dNum);
				// 1==入
				if (voaLoc[1] != null)
					voaLoc[1].setNinspaceassistnum(dNum);
			}

		}

		dNum = null; // clear it .

		if (getBillCardPanel().getBodyItem(
				getEnvironment().getGrossNumItemKey()) != null) {
			if (getBillCardPanel().getBodyValueAt(row,
					getEnvironment().getGrossNumItemKey()) != null)
				dNum = (nc.vo.pub.lang.UFDouble) getBillCardPanel()
						.getBodyValueAt(row,
								getEnvironment().getGrossNumItemKey());
			if (dNum != null && dNum.doubleValue() <= 0) {
				showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"4008spec", "UPP4008spec-000144")/*
															 * @res
															 * "请注意：数量、辅数量均应大于零。"
															 */);
				// 刷新界面
				getBillCardPanel().setBodyValueAt(null, row,
						getEnvironment().getGrossNumItemKey());
				dNum = null;
			}
			// 毛重
			if (voaLoc != null && voaLoc.length >= 2) {
				// clear show message
				showHintMessage("");
				// 0==出
				if (voaLoc[0] != null)
					voaLoc[0].setNoutgrossnum(dNum);
				// 1==入
				if (voaLoc[1] != null)
					voaLoc[1].setNingrossnum(dNum);
			}

		}

	}

	/**
	 * 创建者：仲瑞庆 功能： 参数： 返回： 例外： 日期：(2001-5-24 下午 5:17) 修改日期，修改人，修改原因，注释标志：
	 */
	public boolean checkVO1() {
		try {

			// 执行以下检查，将不具有的检查注释------------------------------------------------
			// ------------------------------------------------------------------------------
			// VO存在检查
			VOCheck.checkNullVO(getM_voBill());
			// ------------------------------------------------------------------------------
			// 应发数量检查,要放在前面
			// 本节点使用=====================

			// 数值输入全部性检查
			VOCheck.checkNumInput(getM_voBill().getChildrenVO(),
					getEnvironment().getNumItemKey());

			// 本节点使用=====================
			// 表头表体非空检查
			VOCheck.validate(getM_voBill(), GeneralMethod
					.getHeaderCanotNullString(getBillCardPanel()),
					GeneralMethod.getBodyCanotNullString(getBillCardPanel()));
			// ------------------------------------------------------------------------------
			// 业务项检查

			// 自由项
			VOCheck.checkFreeItemInput(getM_voBill(), getEnvironment()
					.getNumItemKey());
			// 批次号
			VOCheck.checkLotInput(getM_voBill(), getEnvironment()
					.getNumItemKey());
			// 辅计量
			VOCheck.checkAssistUnitInput(getM_voBill(), getEnvironment()
					.getNumItemKey(), getEnvironment().getAssistNumItemKey());
			// VOCheck.checkAssistUnitInput(m_voBill,
			// getEnvironment().getNumItemKey(),"hsl");
			// 失效日期
			VOCheck.checkInvalidateDateInput(getM_voBill(), getEnvironment()
					.getNumItemKey());
			// 入库日期
			// VOCheck.checkdbizdate(m_voBill,
			// getEnvironment().getNumItemKey());
			// 价格>0检查
			VOCheck.checkGreaterThanZeroInput(getM_voBill().getChildrenVO(),
					"nprice", nc.ui.ml.NCLangRes.getInstance().getStrByID(
							"common", "UC000-0000741")/* @res "单价" */);
			// 数量>0检查
			VOCheck
					.checkGreaterThanZeroInput(getM_voBill().getChildrenVO(),
							getEnvironment().getNumItemKey(),
							nc.ui.ml.NCLangRes.getInstance().getStrByID(
									"common", "UC000-0002282")/* @res "数量" */);
			VOCheck
					.checkGreaterThanZeroInput(getM_voBill().getChildrenVO(),
							getEnvironment().getAssistNumItemKey(),
							nc.ui.ml.NCLangRes.getInstance().getStrByID(
									"common", "UC000-0002282")/* @res "数量" */);
			// 数量非零检查
			VOCheck
					.checkNotZeroInput(getM_voBill().getChildrenVO(),
							getEnvironment().getNumItemKey(),
							nc.ui.ml.NCLangRes.getInstance().getStrByID(
									"common", "UC000-0002282")/* @res "数量" */);
			VOCheck
					.checkNotZeroInput(getM_voBill().getChildrenVO(),
							getEnvironment().getAssistNumItemKey(),
							nc.ui.ml.NCLangRes.getInstance().getStrByID(
									"common", "UC000-0003971")/* @res "辅数量" */);
			// 序列号检查
			VOCheck.checkSNInput(getM_voBill().getChildrenVO(),
					getEnvironment().getNumItemKey());
			// 毛重检查
			VOCheck.checkGrossNumInput(getM_voBill().getChildrenVO(),
					getEnvironment().getGrossNumItemKey(), getEnvironment()
							.getNumItemKey());
			return true;

		} catch (ICDateException e) {
			// 显示提示
			String sErrorMessage = GeneralMethod.getBodyErrorMessage(
					getBillCardPanel(), e.getErrorRowNums(), e.getHint());
			showErrorMessage(sErrorMessage);
			return false;
		} catch (ICNullFieldException e) {
			// 显示提示
			String sErrorMessage = GeneralMethod.getBodyErrorMessage(
					getBillCardPanel(), e.getErrorRowNums(), e.getHint());
			showErrorMessage(sErrorMessage);
			return false;
		} catch (ICNumException e) {
			// 显示提示
			String sErrorMessage = GeneralMethod.getBodyErrorMessage(
					getBillCardPanel(), e.getErrorRowNums(), e.getHint());
			showErrorMessage(sErrorMessage);
			return false;
		} catch (ICPriceException e) {
			// 显示提示
			String sErrorMessage = GeneralMethod.getBodyErrorMessage(
					getBillCardPanel(), e.getErrorRowNums(), e.getHint());
			showErrorMessage(sErrorMessage);
			return false;
		} catch (ICSNException e) {
			// 显示提示
			String sErrorMessage = GeneralMethod.getBodyErrorMessage(
					getBillCardPanel(), e.getErrorRowNums(), e.getHint());
			showErrorMessage(sErrorMessage);
			return false;
		} catch (ICLocatorException e) {
			// 显示提示
			String sErrorMessage = GeneralMethod.getBodyErrorMessage(
					getBillCardPanel(), e.getErrorRowNums(), e.getHint());
			showErrorMessage(sErrorMessage);
			return false;
		} catch (ICRepeatException e) {
			// 显示提示
			String sErrorMessage = GeneralMethod.getBodyErrorMessage(
					getBillCardPanel(), e.getErrorRowNums(), e.getHint());
			showErrorMessage(sErrorMessage);
			return false;
		} catch (ICHeaderNullFieldException e) {
			// 显示提示
			String sErrorMessage = GeneralMethod.getHeaderErrorMessage(
					getBillCardPanel(), e.getErrorRowNums(), e.getHint());
			showErrorMessage(sErrorMessage);
			return false;
		} catch (nc.vo.pub.NullFieldException e) {
			showErrorMessage(e.getHint());
			return false;
		} catch (nc.vo.pub.ValidationException e) {
			nc.vo.scm.pub.SCMEnv.out("校验异常！其他未知故障...");
			handleException(e);
			return false;
		}
	}

	/**
	 * 创建者：王乃军 功能：抽象方法：保存前的VO检查 参数：待保存单据 返回： 例外： 日期：(2001-5-24 下午 5:17)
	 * 修改日期，修改人，修改原因，注释标志：
	 */
	protected boolean checkVO(nc.vo.ic.pub.bill.GeneralBillVO voBill) {
		return this.checkVO();
	}

	/**
	 * 过滤单据参照 创建者：张欣 功能：初始化参照过滤 参数： 返回： 例外： 日期：(2001-7-17 10:33:20)
	 * 修改日期，修改人，修改原因，注释标志：
	 * 
	 */
	public void filterRef(String sCorpID) {
		super.filterRef(sCorpID);
		try {
			// 只能是货位管理仓库

			((nc.ui.pub.beans.UIRefPane) getBillCardPanel().getHeadItem(
					IItemKey.WAREHOUSE).getComponent())
					.setWhereString("csflag='Y' AND "
							+ nc.ui.ic.pub.bill.initref.RefFilter.getWhFiltStr(
									sCorpID, null));

		} catch (Exception e) {
		}
	}

	/**
	 * 如果是固定货位的存货，过滤出存货的固定货位 作者：余大英 创建日期：(2001-7-6 16:53:38)
	 */
	protected void filterSpace(int row) {
		if (getM_voBill() == null)
			return;
		String sItemKey = "vspacename";
		nc.vo.scm.ic.bill.WhVO voWh = getM_voBill().getWh();

		String sName = (String) getBillCardPanel()
				.getBodyValueAt(row, sItemKey);
		String spk = (String) getBillCardPanel()
				.getBodyValueAt(row, "cspaceid");
		if (GenMethod.isNull(spk)) {
			spk = null;
			getBillCardPanel().setBodyValueAt(null, row, "cspaceid");
		}

		getLocatorRefPane().setOldValue(sName, null, spk);
		getLocatorRefPane().setPK(spk);

		getLocatorRefPane().setParam(voWh, getM_voBill().getItemInv(row));

		return;

	}

	/**
	 * 如果是固定货位的存货，过滤出存货的固定货位 作者：余大英 创建日期：(2001-7-6 16:53:38)
	 */
	private void filterSpace2(int row) {

		if (getM_voBill() == null)
			return;
		String sItemKey = "vspace2name";
		nc.vo.scm.ic.bill.WhVO voWh = getM_voBill().getWh();

		String sName = (String) getBillCardPanel()
				.getBodyValueAt(row, sItemKey);
		String spk = (String) getBillCardPanel().getBodyValueAt(row,
				"cspace2id");
		if (GenMethod.isNull(spk)) {
			spk = null;
			getBillCardPanel().setBodyValueAt(null, row, "cspace2id");
		}
		getLocatorRefPane2().setOldValue(sName, null, spk);
		getLocatorRefPane2().setPK(spk);

		getLocatorRefPane2().setParam(voWh, getM_voBill().getItemInv(row));

		return;

	}

	/**
	 * 创建者：王乃军 功能：得到查询对话框 参数： 返回： 例外： 日期：(2001-5-9 9:23:32) 修改日期，修改人，修改原因，注释标志：
	 * 
	 * 
	 * 
	 * 
	 */
	// protected QueryConditionDlgForBill getConditionDlg() {
	// if (ivjQueryConditionDlg == null) {
	// ivjQueryConditionDlg=super.getConditionDlg();
	// //设置下拉框显示
	// ivjQueryConditionDlg.setCombox("qbillstatus", new String[][] { { "A",
	// nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec","UPPSCMCommon-000217")/*@res
	// "全部"*/ }
	// });
	//
	// }
	// return ivjQueryConditionDlg;
	// }
	public QueryDlgHelp getQryDlgHelp() {
		if (m_qryDlgHelp == null) {
			m_qryDlgHelp = new QueryDlgHelp(this) {
				protected void init() {
					super.init();
					// 设置下拉框显示
					getQueryDlg().setCombox(
							"qbillstatus",
							new String[][] { {
									"A",
									nc.ui.ml.NCLangRes.getInstance()
											.getStrByID("4008spec",
													"UPPSCMCommon-000217") /*
																			 * @res
																			 * "全部"
																			 */} });
				}
			};
		}
		return m_qryDlgHelp;
	}

	/**
	 * 去存货管理档案定义出库是否跟踪入库的参数 功能： 参数：存货VO 返回：boolean 例外： 日期：(2002-05-20 19:55:18)
	 * 修改日期，修改人，修改原因，注释标志：
	 */
	protected boolean getIsInvTrackedBill(InvVO invvo) {
		// 206,216,251 等单据不需要跟踪到入库单据的界面控制。强制返回 false；
		return false;
	}

	/**
	 * 返回 LotNumbRefPane1 特性值。
	 * 
	 * @return nc.ui.ic.pub.lot.LotNumbRefPane
	 */
	/* 警告：此方法将重新生成。 */
	protected LocatorRefPane getLocatorRefPane() {
		if (m_refLocator == null) {
			try {
				m_refLocator = new LocatorRefPane(InOutFlag.OUT);
				m_refLocator.setName("LotNumbRefPane");
				m_refLocator.setLocation(38, 1);
				// user code begin {1}
				m_refLocator.setInOutFlag(InOutFlag.OUT);
				// BillData bd = getBillCardPanel().getBillData();
				// 设置货位参照
				// if(bd.getBodyItem("vspacename")!=null)
				// bd.getBodyItem("vspacename").setComponent(m_refLocator);
				// getBillCardPanel().setBillData(bd);
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return m_refLocator;
	}

	/**
	 * 返回 LotNumbRefPane1 特性值。
	 * 
	 * @return nc.ui.ic.pub.lot.LotNumbRefPane
	 */
	/* 警告：此方法将重新生成。 */
	private LocatorRefPane getLocatorRefPane2() {
		if (m_refLocator2 == null) {
			try {
				m_refLocator2 = new LocatorRefPane(InOutFlag.IN);
				m_refLocator2.setName("LotNumbRefPane2");
				m_refLocator2.setLocation(38, 1);
				// user code begin {1}
				m_refLocator2.setInOutFlag(InOutFlag.IN);
				// BillData bd = getBillCardPanel().getBillData();
				// 设置货位参照
				// if (bd.getBodyItem("vspace2name") != null)
				// bd.getBodyItem("vspace2name").setComponent(m_refLocator2);
				// getBillCardPanel().setBillData(bd);
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return m_refLocator2;
	}

	/**
	 * 创建者：王乃军 功能：初始化 参数： 返回： 例外： 日期：(2001-12-7 18:59:44) 修改日期，修改人，修改原因，注释标志：
	 * 
	 */
	public void initialize() {
		// 不添加条码编辑功能
		super.m_bAddBarCodeField = false;
		super.initialize();
		// 不用管是否跟踪入库
		// super.m_sTrackedBillFlag = "N";
		try {
			// 只能是货位管理仓库

			((nc.ui.pub.beans.UIRefPane) getBillCardPanel().getHeadItem(
					IItemKey.WAREHOUSE).getComponent())
					.setWhereString("csflag='Y' AND "
							+ nc.ui.ic.pub.bill.initref.RefFilter.getWhFiltStr(
									getEnvironment().getCorpID(), null));

		} catch (Exception e) {
		}

	}

	/**
	 * 创建者：王乃军 功能：初始化系统参数 参数： 返回： 例外： 日期：(2001-5-9 9:23:32) 修改日期，修改人，修改原因，注释标志：
	 * 
	 * 
	 * 
	 * 
	 */
	protected void initPanel() {
		// 需要单据参照
		super.setNeedBillRef(false);

		nc.vo.scm.pub.SCMEnv.out("disable line op.");

		try {
			nc.ui.pub.bill.BillData bd = getBillCardPanel().getBillData();
			if (bd != null) {
				// 设置货位参照
				try {
					bd.getBodyItem("vspacename").setComponent(
							getLocatorRefPane());
				} catch (Exception e) {
				}
				try {
					bd.getBodyItem("vspace2name").setComponent(
							getLocatorRefPane2());
				} catch (Exception e) {
				}
			}
			getBillCardPanel().setBillData(bd);

		} catch (Exception e) {
		}
	}

	public String getBillType() {
		return nc.vo.ic.pub.BillTypeConst.m_spaceAdjust;
	}

	public String getFunctionNode() {
		return "40081014";
	}

	public int getInOutFlag() {
		return InOutFlag.OUT;
	}

	/**
	 * 创建者：王乃军 功能： 参数： 返回： 例外： 日期：(2001-11-24 12:15:42) 修改日期，修改人，修改原因，注释标志：
	 */
	void newMethod() {
	}

	protected class ButtonManager251 extends
			nc.ui.ic.pub.bill.GeneralButtonManager {

		public ButtonManager251(GeneralBillClientUI clientUI)
				throws BusinessException {
			super(clientUI);
		}

		/**
		 * 子类实现该方法，响应按钮事件。
		 * 
		 * @version (00-6-1 10:32:59)
		 * 
		 * @param bo
		 *            ButtonObject
		 */
		public void onButtonClicked(nc.ui.pub.ButtonObject bo) {
			// add heyq 2010-10-25
			// 如果点的是保存,先较验一下

			if (bo.getCode().equals(ICButtonConst.BTN_SAVE)) {
				try {
					int rs = checkSfMdgl();
					if (rs == 2) {
						showWarningMessage("同一个货位调整单的存货，存货必须全部是码单管理的或者全部都不是码单管理，保存失败！");
						return;
					}
					if (rs == 0) {
						int r = showYesNoMessage("当前货位调整单存货全部是码单管理，保存后不能再修改和删除，是否要继续保存？");
						if (r != 4)
							return;
					}
				} catch (BusinessException e) {
					e.printStackTrace();
					showWarningMessage(e.getMessage());
					return;
				}
			}
			// 如果点的是修改，则先较验一下
			if (bo.getCode().equals(ICButtonConst.BTN_BILL_EDIT)) {
				try {
					if (checkSfMdgl() == 0) {
						showWarningMessage("码单管理的货位调整单保存后不能再修改！请重新做再做一张货位调整单");
						return;
					}
				} catch (BusinessException e) {
					e.printStackTrace();
					showWarningMessage(e.getMessage());
					return;
				}
			}
			// 如果点的删除，则先较验一下
			if (bo.getCode().equals(ICButtonConst.BTN_BILL_DELETE)) {
				try {
					if (checkSfMdgl() == 0) {
						showWarningMessage("码单管理的货位调整单保存后不能再删除！请重新做再做一张货位调整单");
						return;
					}
				} catch (BusinessException e) {
					e.printStackTrace();
					showWarningMessage(e.getMessage());
					return;
				}
			}
			// end heyq 2010-10-25

			getBillCardPanel().stopEditing();
			super.onButtonClicked(bo);

			// add heyq 2010-10-25
			// 如果点的是保存
			if (bo.getCode().equals(ICButtonConst.BTN_SAVE)) {
				try {
					if (checkSfMdgl() != 0)
						return;
				} catch (BusinessException e1) {
					e1.printStackTrace();
					showWarningMessage(e1.getMessage());
					return;
				}
				Map tzmap = getHwtzMap();
				GeneralBillItemVO[] items = (GeneralBillItemVO[]) getM_voBill()
						.getChildrenVO();
				if (tzmap != null && items != null && items.length > 0) {
					IUAPQueryBS iUAPQueryBS = (IUAPQueryBS) NCLocator
							.getInstance().lookup(IUAPQueryBS.class.getName());
					for (int i = 0; i < items.length; i++) {
						String pk_cinvbasid = items[i].getCinvbasid();// 存货主键
						if (items[i].getCspace2id() == null
								|| items[i].getCspace2id().equals(""))
							return;
						HwtzVO[] vos = (HwtzVO[]) tzmap.get(pk_cinvbasid);
						String sql = "update nc_mdxcl_b set cspaceid='"
								+ items[i].getCspace2id()
								+ "' where pk_mdxcl_b in(";
						if (vos != null || vos.length > 0) {
							for (int j = 0; j < vos.length; j++)
								sql += "'" + vos[j].getPk_mdxcl_b() + "',";
							sql = sql.substring(0, sql.length() - 1);
							sql = sql + ")";
							try {
								iUAPQueryBS.executeQuery(sql,
										new ColumnProcessor());
							} catch (BusinessException e) {
								e.printStackTrace();
							}
						}
					}
					// 清空数据
					setHwtzMap(new HashMap());
				}

			}
			// end heyq 2010-10-25
		}

		// add heyq 2010-10-25

		// 0 全部码单管理，1全部不是码单管理, 2 较验失败
		private int checkSfMdgl() throws BusinessException {
			boolean checkFlg = true;
			// 数据较验，表体存货是否全是码单管理
			GeneralBillItemVO[] items = (GeneralBillItemVO[]) getM_voBill()
					.getChildrenVO();
			if (items != null && items.length > 0) {
				for (int t = 0; t < items.length; t++) {
					if (t == 0)
						checkFlg = querySfmdwf(items[t].getCinvbasid());
					if (querySfmdwf(items[t].getCinvbasid()) != checkFlg) {
						return 2;
					}
				}
			}
			if (checkFlg == true)
				return 0;
			else
				return 1;
		}

		// end heyq 2010-10-25

		/**
		 * 创建者：王乃军 功能：确认（保存）处理 参数：无 返回： true: 成功 false: 失败
		 * 
		 * 例外： 日期：(2001-5-9 9:23:32) 修改日期，修改人，修改原因，注释标志：
		 * 2001/10/29,wnj,拆分出保存新增/保存修改两个方法，使得更规范。
		 * 
		 * 
		 * 
		 */
		public boolean onSave() {
			if (super.onSave()) {
				setBillVO((GeneralBillVO) getM_alListData().get(
						getM_iLastSelListHeadRow()));
				return true;
			} else
				return false;
		}

		/**
		 * 创建者：王乃军 功能：序列号分配 参数： 返回： 例外： 日期：(2001-5-9 9:23:32)
		 * 修改日期，修改人，修改原因，注释标志：
		 * 
		 * 
		 * 
		 * 
		 */
		public void onSNAssign() {
			// 浏览方式下先查询货位/序列号，当然qryLocSN对每张单据只查一次库。
			// if (BillMode.Browse == m_iMode)
			qryLocSN(getM_iLastSelListHeadRow(), QryInfoConst.LOC_SN);
			// 当前选中的行
			int iCurSelBodyLine = -1;
			if (BillMode.Card == getM_iCurPanel()) {
				iCurSelBodyLine = getBillCardPanel().getBillTable()
						.getSelectedRow();
				if (iCurSelBodyLine < 0) {
					showHintMessage(nc.ui.ml.NCLangRes.getInstance()
							.getStrByID("4008spec", "UPP4008spec-000146")/*
																			 * @res
																			 * "请选中要进行序列号分配的行。"
																			 */);
					return;
				}
			} else {
				iCurSelBodyLine = getBillListPanel().getBodyTable()
						.getSelectedRow();
				if (iCurSelBodyLine < 0) {
					showHintMessage(nc.ui.ml.NCLangRes.getInstance()
							.getStrByID("4008spec", "UPP4008spec-000147")/*
																			 * @res
																			 * "请选中要查看序列号的行。"
																			 */);
					return;
				}
			}
			InvVO voInv = null;
			// 仓库
			WhVO voWh = null;

			// 浏览状态下察看，先读数据，所以一定要放在setInitVOs之前

			// 读表体vo,区分列表下还是表单下
			// 表单形式下
			if (BillMode.Card == getM_iCurPanel()) {
				if (getM_voBill() == null || getM_voBill().getItemCount() <= 0) {
					nc.vo.scm.pub.SCMEnv.out("bill null E.");
					return;
				}
				// 数据VO
				GeneralBillItemVO voItem = getBodyVO(iCurSelBodyLine);
				// 属性VO
				GeneralBillItemVO voItemPty = getM_voBill().getItemVOs()[iCurSelBodyLine];
				// 合并
				if (voItemPty != null)
					voItemPty.setIDItems(voItem);
				// 完整的存货行数据
				if (voItem != null)
					voInv = voItemPty.getInv();

				if (getM_voBill() != null)
					voWh = getM_voBill().getWh();

			} else
			// 列表形式下察看，先读数据，所以一定要放在setInitVOs之前
			if (getM_iLastSelListHeadRow() >= 0
					&& getM_iLastSelListHeadRow() < getM_alListData().size()) {
				// 置入各行的货位分配等辅表数据
				GeneralBillVO bvo = (GeneralBillVO) getM_alListData().get(
						getM_iLastSelListHeadRow());
				if (bvo == null) {
					nc.vo.scm.pub.SCMEnv.out("bill null E.");
					return;
				}
				voInv = bvo.getItemInv(iCurSelBodyLine);
				// warehouse
				voWh = bvo.getWh();
			}

			if (voInv != null) {
				// 非浏览模式下数量不能为零
				if (BillMode.Card == getM_iCurPanel()
						&& (BillMode.Update == getM_iMode() || BillMode.New == getM_iMode())) {
					Object oQty = voInv.getAttributeValue(getEnvironment()
							.getNumItemKey());
					if (oQty == null || oQty.toString().trim().length() == 0) {
						showHintMessage(nc.ui.ml.NCLangRes.getInstance()
								.getStrByID("4008spec", "UPP4008spec-000148")/*
																				 * @res
																				 * "请先输入数量。"
																				 */);
						return;
					}
				}

				// if (BillMode.Browse == m_iMode)
				// //置入各行的货位分配等辅表数据
				// resetBodyAssistData(m_iLastSelListHeadRow);

				// --- && ---- 只置出货位的数据

				LocatorVO voOutLoc = null;
				if (getM_alLocatorData() != null
						&& iCurSelBodyLine < getM_alLocatorData().size()
						&& getM_alLocatorData().get(iCurSelBodyLine) != null)
					voOutLoc = ((LocatorVO[]) getM_alLocatorData().get(
							iCurSelBodyLine))[0];
				// 如果是空则初始化之.

				if (voOutLoc == null)
					voOutLoc = new LocatorVO();
				getSerialAllocationDlg().setOutSNByLimitLoc(true);
				getSerialAllocationDlg().setDataVO(
						nc.ui.ic.pub.sn.SerialAllocationDlg.OUT, voWh,
						voOutLoc, voInv, getM_iMode(),
						(SerialVO[]) getM_alSerialData().get(iCurSelBodyLine),
						new LocatorVO[] { voOutLoc });

				// 当前的序列号们

				int result = getSerialAllocationDlg().showModal();
				if (nc.ui.pub.beans.UIDialog.ID_OK == result
						&& (BillMode.New == getM_iMode() || BillMode.Update == getM_iMode())) {
					// --- && ---- 货位分配数据, -----------------
					// --- && ---- 只能选中同一个货位上的序列号。

					ArrayList alRes = getSerialAllocationDlg().getDataSpaceVO();
					if (alRes != null && alRes.size() > 0) {
						setSpaceData(iCurSelBodyLine, alRes);
					} else { // 这里必须有货位数据
						showErrorMessage(nc.ui.ml.NCLangRes.getInstance()
								.getStrByID("4008spec", "UPP4008spec-000149")/*
																				 * @res
																				 * "无货位数据，请重新选择。"
																				 */);
						return;
					}

					SerialVO voaSN[] = getSerialAllocationDlg().getDataSNVO();
					// 保存分配结果
					getM_alSerialData().set(iCurSelBodyLine, voaSN);
					if (voaSN != null)
						for (int i = 0; i < voaSN.length; i++)
							nc.vo.scm.pub.SCMEnv.out("ret sn[" + i + "] is"
									+ voaSN[i].getVserialcode());

				}
			}
		}
	}

	public IButtonManager getButtonManager() {
		if (m_buttonManager == null) {
			try {
				m_buttonManager = new ButtonManager251(this);
			} catch (BusinessException e) {
				// 日志异常
				nc.vo.scm.pub.SCMEnv.error(e);
				showErrorMessage(e.getMessage());
			}
		}
		return m_buttonManager;
	}

	protected void qryLocSN(GeneralBillVO voMyBill, int iMode) {
		try {
			// ERRRR,needless to read,如新增单据的情况，
			if (voMyBill == null || voMyBill.getPrimaryKey() == null) {
				int iFaceRowCount = getBillCardPanel().getRowCount();
				// 初始化数组 if necessary
				if (getM_alLocatorData() == null) {
					initM_alLocatorData();
					for (int i = 0; i < iFaceRowCount; i++)
						getM_alLocatorData().add(null);
				}
				if (getM_alSerialData() == null) {
					initM_alSerialData();
					for (int i = 0; i < iFaceRowCount; i++)
						getM_alSerialData().add(null);
				}
				SCMEnv.out("null bill,init loc ,sn");
				return;
			}
			// 只在浏览状态下，才会需要赋值和查询，否则会取消已做的修改。
			if (getM_iMode() == BillMode.Browse) {
				// 测试此单据是否填了数量，如填了则需要继续执行，否则单据本来就没有这些数据，不用读了
				int i = 0, iRowCount = voMyBill.getItemCount();
				// 测试是否已经读过这些数据了。

				boolean hasLoc = true;
				WhVO voWh = voMyBill.getWh();
				if (voWh != null) {
					if (voWh.getIsLocatorMgt() != null
							&& voWh.getIsLocatorMgt().intValue() != 0) {
						for (i = 0; i < iRowCount; i++) {
							if (voMyBill.getItemValue(i, "locator") == null) {
								hasLoc = false;
								break;
							}
						}
						if (hasLoc) {
							try {
								VOCheck.checkSpaceInput(voMyBill, new Integer(
										getInOutFlag()));
							} catch (Exception e) {
								nc.vo.scm.pub.SCMEnv.error(e);
								hasLoc = false;
							}

						}

					}

				}
				InvVO voInv = null;
				boolean hasSN = true;
				String[] sKeys = new String[iRowCount];
				GeneralBillItemVO item = null;
				for (i = 0; i < iRowCount; i++) {
					item = voMyBill.getItemVOs()[i];
					// zhy得到sKeys,为了处理入货位序列号数据
					sKeys[i] = item.getCgeneralbid() + item.getCspaceid();
					voInv = voMyBill.getItemInv(i);
					// 有序列号管理的行但此行还没有序列号。
					if (voInv != null && voInv.getIsSerialMgt() != null
							&& voInv.getIsSerialMgt().intValue() != 0
							&& voMyBill.getItemValue(i, "serial") == null) {
						hasSN = false;
						break;
					}
				}
				// 已经读过数据,把数据放到成员变量中，并同步vo(needless now )2003-08-07
				if (hasLoc)
					setM_alLocatorData(voMyBill.getLocators());
				if (hasSN)
					setM_alSerialData(voMyBill.getSNs());
				if (hasLoc && hasSN)
					return;

				// =============================================================
				// 初始化数组 if necessary
				if (getM_alLocatorData() == null) {
					initM_alLocatorData();
					for (i = 0; i < iRowCount; i++)
						getM_alLocatorData().add(null);
				}
				if (getM_alSerialData() == null) {
					initM_alSerialData();
					for (i = 0; i < iRowCount; i++)
						getM_alSerialData().add(null);
				}

				for (i = 0; i < iRowCount; i++)
					// 测试实出/入数量
					if (voMyBill.getItemValue(i, "ninnum") != null
							&& voMyBill.getItemValue(i, "ninnum").toString()
									.length() > 0
							|| voMyBill.getItemValue(i, "noutnum") != null
							&& voMyBill.getItemValue(i, "noutnum").toString()
									.length() > 0)
						break;

				if (i >= iRowCount) // 无数量
					return; // =============================================================

				// 先清空货位、序列号数据
				Integer iSearchMode = null;
				// 需要查货位
				if (!hasLoc
						&& (iMode == QryInfoConst.LOC_SN || iMode == QryInfoConst.LOC)) {
					iSearchMode = new Integer(iMode);
				}
				// 需要查序列号
				if (!hasSN
						&& (iMode == QryInfoConst.LOC_SN || iMode == QryInfoConst.SN)) {
					iSearchMode = new Integer(iMode);
				}
				if (iSearchMode == null)
					return;
				// WhVO voWh = voMyBill.getWh();
				// 货位管理的仓库，并且还没有货位，需要读货位数据、序列号

				// iMode = 3;
				// Integer iSearchMode = new Integer(iMode);

				// ////////////////////////////iMode); //查货位 & 序列号 3 or 序列号 4
				// zhy货位调整单会查出重复的序列号(包含出货位和入货位的),需要将入货位的序列号数据去掉
				ArrayList alAllData = (ArrayList) GeneralBillHelper.queryInfo(
						iSearchMode, voMyBill.getPrimaryKey());
				// 先清空货位、序列号数据
				ArrayList alTempLocatorData = null;
				ArrayList alTempSerialData = null;

				if (iMode == QryInfoConst.LOC_SN) {
					if (alAllData != null && alAllData.size() >= 2) {
						alTempLocatorData = (ArrayList) alAllData.get(0);
						alTempSerialData = (ArrayList) alAllData.get(1);
					} // else
				} else if (iMode == QryInfoConst.SN) { // === SN only
					if (alAllData != null && alAllData.size() >= 1)
						alTempSerialData = (ArrayList) alAllData.get(0);
					// else
				} else if (iMode == QryInfoConst.LOC) { // === LOC only
					if (alAllData != null && alAllData.size() >= 1)
						alTempLocatorData = (ArrayList) alAllData.get(0);
				} // else

				// --------------------------------------------------------

				// 如果有的话置货位数据
				if (alTempLocatorData != null) {
					// 放到vo中，根据表体id执行数据匹配。
					voMyBill.setLocators(alTempLocatorData);
					// getLocators处理后的 by hanwei 2004-01-06
					setM_alLocatorData(voMyBill.getLocators());
				}
				// 如果有的话置序列号数据
				if (alTempSerialData != null) {
					// 放到vo中，根据表体id执行数据匹配。
					// zhy去掉入货位序列号数据
					alTempSerialData = dropInSpaceSN(alTempSerialData, sKeys);
					voMyBill.setSNs(alTempSerialData);
					// getSNs处理后的 by hanwei 2004-01-06
					setM_alSerialData(voMyBill.getSNs());

				}

			}

		} catch (Exception e) {
			nc.vo.scm.pub.SCMEnv.error(e);
		}

	}

	private ArrayList dropInSpaceSN(ArrayList alData, String[] sKeys) {
		if (sKeys == null || alData == null || alData.size() == 0
				|| sKeys.length == 0)
			return alData;

		ArrayList al = new ArrayList();
		nc.vo.ic.pub.sn.SerialVO voaSN[] = null;
		for (int j = 0; j < alData.size(); j++) {
			Object oSNVOs = alData.get(j);
			Vector vSN = new Vector();
			if (oSNVOs != null) {
				voaSN = (nc.vo.ic.pub.sn.SerialVO[]) oSNVOs;
				if (voaSN != null && voaSN.length > 0 && voaSN[0] != null
						&& voaSN[0].getCgeneralbid() != null
						&& voaSN[0].getCspaceid() != null) {
					for (int k = 0; k < voaSN.length; k++) {
						String sKey = voaSN[k].getCgeneralbid()
								+ voaSN[k].getCspaceid();
						for (int i = 0; i < sKeys.length; i++) {
							if (sKey.equals(sKeys[i])) {
								vSN.add(voaSN[k]);
							}
						}
					}

				}
			}
			if (vSN != null && vSN.size() > 0) {
				nc.vo.ic.pub.sn.SerialVO[] sn = new nc.vo.ic.pub.sn.SerialVO[vSN
						.size()];
				vSN.copyInto(sn);
				al.add(sn);
			}

		}
		return al;

	}

	protected void qryLocSN(int iBillNum, int iMode) {
		GeneralBillVO voMyBill = null;
		// arraylist 有的话用他的,没有话,就是新的.
		if (getM_alListData() != null && getM_alListData().size() > iBillNum
				&& iBillNum >= 0)
			voMyBill = (GeneralBillVO) getM_alListData().get(iBillNum);
		qryLocSN(voMyBill, iMode);
	}

	/**
	 * 创建者：王乃军 功能：在列表方式下选择一张单据 参数： 单据在alListData中的索引 返回：无 例外： 日期：(2001-11-23
	 * 18:11:18) 修改日期，修改人，修改原因，注释标志：
	 */
	protected void selectBillOnListPanel(int iBillIndex) {
		// 查询当前表单的表体货位
		// ydy
		if (getM_alListData() == null)
			return;
		GeneralBillVO voBill = (GeneralBillVO) getM_alListData()
				.get(iBillIndex);
		GeneralBillItemVO[] voItems = (GeneralBillItemVO[]) voBill
				.getChildrenVO();
		if (voItems != null) {
			nc.vo.scm.pub.SCMEnv.out("body rows=" + voItems.length);
			for (int i = 0; i < voItems.length; i++) {
				LocatorVO[] lvos = ((LocatorVO[]) voItems[i].getLocator());
				if (lvos == null) {
					qryLocSN(iBillIndex, QryInfoConst.LOC_SN);
					lvos = ((LocatorVO[]) voItems[i].getLocator());
				}
				if (lvos != null && lvos.length > 0) {
					voItems[i].setCspaceid(lvos[0].getCspaceid());
					voItems[i].setVspacename(lvos[0].getVspacename());
				}

			}
			// ------------
			setListBodyData(voItems);
		}

		// 选中表体第一行
		// 表体不可能为空
		if (getBillListPanel().getBodyTable().getRowCount() > 0)
			getBillListPanel().getBodyTable().setRowSelectionInterval(0, 0);
		// end ydy
	}

	/**
	 * 创建者：王乃军 功能：抽象方法：设置按钮状态，在setButtonStatus中调用。 参数： 返回： 例外： 日期：(2001-5-9
	 * 9:23:32) 修改日期，修改人，修改原因，注释标志：
	 * 
	 * 
	 * 
	 * 
	 */
	protected void setButtonsStatus(int iBillMode) {
		// 浏览状态下
		if (iBillMode == BillMode.Browse) {
			// 有单据的话可删除
			if (m_iBillQty > 0)
				getButtonManager().getButton(ICButtonConst.BTN_BILL_DELETE)
						.setEnabled(true);
		} else {
			getButtonManager().getButton(ICButtonConst.BTN_BILL_DELETE)
					.setEnabled(false);
		}

	}

	/**
	 * 创建者：王乃军 功能： 参数： 返回： 例外： 日期：(2001-10-Oct 9:23:32) 修改日期，修改人，修改原因，注释标志：
	 * 
	 * 
	 * 
	 * 
	 */
	protected void setSpaceData(int iCurSelBodyLine, ArrayList alRes) {
		if (alRes == null || alRes.size() <= 0) {
			nc.vo.scm.pub.SCMEnv.out("space is not ready!");
			return;
		}
		// LocatorVO voaLoc[] = new LocatorVO[alRes.size()];
		// 比较，只能选一个货位上的序列号
		LocatorVO voLoc0 = null, voLoci = null;
		voLoc0 = (LocatorVO) alRes.get(0);
		for (int i = 1; i < alRes.size(); i++) {
			voLoci = (LocatorVO) alRes.get(i);
			// equals ,err !
			if (voLoc0.getCspaceid() != null
					&& voLoc0.getCspaceid().equals(voLoci.getCspaceid())) {
				showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"4008spec", "UPP4008spec-000150")/*
															 * @res
															 * "只能选中同一个货位得存货，请重新选择。"
															 */);
				return;
			}

		}
		String cInsname = getLocatorRefPane2().getRefName();
		String cInspaceid = getLocatorRefPane2().getRefPK();
		// 重置出货位
		getM_voBill().setItemValue(iCurSelBodyLine, "cspaceid",
				voLoc0.getCspaceid());
		getM_voBill().setItemValue(iCurSelBodyLine, "vspacename",
				voLoc0.getVspacename());
		// m_voBill.setItemValue(iCurSelBodyLine, "cspace2id", cInspaceid);
		// m_voBill.setItemValue(iCurSelBodyLine, "vspace2name", cInsname);
		// add by hanwei 2003-08-11
		// 避免出现 选择转入货位时 导致清空界面的错误
		getLocatorRefPane().setPK(voLoc0.getCspaceid());

		getBillCardPanel().setBodyValueAt(voLoc0.getCspaceid(),
				iCurSelBodyLine, "cspaceid");
		getBillCardPanel().setBodyValueAt(voLoc0.getVspacename(),
				iCurSelBodyLine, "vspacename");
		// 保存的货位数据
		LocatorVO[] voaCurLoc = null;
		if (getM_alLocatorData() != null
				&& iCurSelBodyLine < getM_alLocatorData().size()) {
			if (getM_alLocatorData().get(iCurSelBodyLine) != null
					&& ((LocatorVO[]) getM_alLocatorData().get(iCurSelBodyLine)).length >= 2)
				voaCurLoc = (LocatorVO[]) getM_alLocatorData().get(
						iCurSelBodyLine);
			else
				voaCurLoc = new LocatorVO[2];

			// 数量
			UFDouble dTempNum = null, dTempAstNum = null, dTempGrossNum = null;
			Object oTempValue = getBillCardPanel().getBodyValueAt(
					iCurSelBodyLine, "noutnum");
			if (oTempValue != null && oTempValue.toString().trim().length() > 0)
				dTempNum = new UFDouble(oTempValue.toString());
			oTempValue = getBillCardPanel().getBodyValueAt(iCurSelBodyLine,
					"noutassistnum");
			if (oTempValue != null && oTempValue.toString().trim().length() > 0)
				dTempAstNum = new UFDouble(oTempValue.toString());
			oTempValue = getBillCardPanel().getBodyValueAt(iCurSelBodyLine,
					"noutgrossnum");
			if (oTempValue != null && oTempValue.toString().trim().length() > 0)
				dTempGrossNum = new UFDouble(oTempValue.toString());

			// 只写出库货位
			voaCurLoc[0] = voLoc0;
			// 由于序列号参照上面没有毛重,此处特意重置一下
			voaCurLoc[0].setNoutgrossnum(dTempGrossNum);
			// 入
			if (voaCurLoc[1] != null) {
				voaCurLoc[1].setNinspacenum(dTempNum);
				voaCurLoc[1].setNinspaceassistnum(dTempAstNum);
				voaCurLoc[1].setNingrossnum(dTempGrossNum);
			} else {
				if (cInspaceid != null && cInspaceid.trim().length() > 0) {
					voaCurLoc[1] = new LocatorVO();
					voaCurLoc[1].setCspaceid(cInspaceid);
					voaCurLoc[1].setVspacename(cInsname);
					voaCurLoc[1].setNinspacenum(dTempNum);
					voaCurLoc[1].setNinspaceassistnum(dTempAstNum);
					voaCurLoc[1].setNingrossnum(dTempGrossNum);
				}
			}

			getM_alLocatorData().set(iCurSelBodyLine, voaCurLoc);
		}
	}

	// add heyq 2010-10-22
	@Override
	public void onButtonClicked(ButtonObject bo) {
		super.onButtonClicked(bo);

	}

	// end heyq 2010-10-22s
}