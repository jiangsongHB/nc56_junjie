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
 * ��λ������ �������ڣ�(2001-11-23 15:39:43)
 * 
 * @author������
 * 
 */
public class ClientUI extends nc.ui.ic.pub.bill.GeneralBillClientUI {
	// ��λ����1,2
	private LocatorRefPane m_refLocator = null, m_refLocator2 = null;

	// ��ѯ�Ի���
	protected QueryConditionDlgForBill ivjQueryConditionDlg = null;

	private IButtonManager m_buttonManager;

	// add heyq 2010-10-25
	private Map hwtzMap = new HashMap();// ������VOMap

	public Map getHwtzMap() {
		return hwtzMap;
	}

	public void setHwtzMap(Map hwtzMap) {
		this.hwtzMap = hwtzMap;
	}

	// end heyq 2010-10-25

	/**
	 * ClientUI2 ������ע�⡣
	 */
	public ClientUI() {
		super();
		this.initialize();
	}

	/**
	 * ClientUI ������ע�⡣ add by liuzy 2007-12-18 ���ݽڵ�����ʼ������ģ��
	 */
	public ClientUI(FramePanel fp) {
		super(fp);
		initialize();
	}

	/**
	 * ClientUI ������ע�⡣ nc 2.2 �ṩ�ĵ������鹦�ܹ����ӡ�
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
	 * �����ߣ����˾� ���ܣ����ݱ༭���� ������ ���أ� ���⣺ ���ڣ�(2001-5-9 9:23:32) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 * 
	 * 
	 * 
	 */
	protected void afterBillEdit(nc.ui.pub.bill.BillEditEvent e) {
		// nc.vo.scm.pub.SCMEnv.out("haha,bill edit/.");
		try {
			if (e.getKey().equals(IItemKey.CALBODY))
				// �����֯
				afterCalbodyEdit(e);

			int row = getBillCardPanel().getBillTable().getSelectedRow();
			String sItemKey = e.getKey();
			if ("cinventorycode".equals(sItemKey)
					|| "cwarehouseid".equals(sItemKey)) {
				// ��λ��������

				getBillCardPanel().setBodyValueAt(null, row, "vspace2name");
				getBillCardPanel().setBodyValueAt(null, row, "cspace2id");

				getLocatorRefPane().setParam(getM_voBill().getWh(),
						getM_voBill().getItemInv(row));
				getLocatorRefPane2().setParam(getM_voBill().getWh(),
						getM_voBill().getItemInv(row));
				GeneralBillUICtl.synUi2Vo(getBillCardPanel(), getM_voBill(),
						new String[] { "vspace2name", "cspace2id" }, row);
				// ת����λ�ڸ����лᱻ���

			} // ��λ�ı�
			else if (sItemKey.equals("vspacename")
					|| sItemKey.equals("vspace2name"))
				afterSpaceEdit(e);
			else if (getEnvironment().getNumItemKey().equals(sItemKey)
					|| getEnvironment().getAssistNumItemKey().equals(sItemKey)) {
				// �޸�������������������������գ�����Ҫ���á�
				afterSpaceEdit(e);
				checkNum(row);
			} else {
				afterSpaceEdit(e);
			}

		} catch (Exception ee) {
		}

	}

	/**
	 * �����ߣ����˾� ���ܣ���������ѡ��ı� ������ ���أ� ���⣺ ���ڣ�(2001-5-9 9:23:32) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
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
	 * �����ߣ����˾� ���ܣ������֯�ı��¼����� ������e���ݱ༭�¼� ���أ� ���⣺ ���ڣ�(2001-5-8 19:08:05)
	 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
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
			// ����˿����֯
			// �����ǰ�Ĳֿⲻ����
			// ���˲ֿ����
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
			// ����:clear warehouse
			nc.ui.pub.bill.BillItem biWh = getBillCardPanel().getHeadItem(
					IItemKey.WAREHOUSE);
			if (biWh != null)
				biWh.setValue(null);

		} catch (Exception e2) {
			nc.vo.scm.pub.SCMEnv.out(e2);
		}

	}

	/**
	 * �����ߣ������� ���ܣ���λ�޸��¼����� ������e���ݱ༭�¼� ���أ� ���⣺ ���ڣ�(2001-5-8 19:08:05)
	 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 * 
	 * 
	 * 
	 */
	public void afterSpaceEdit(nc.ui.pub.bill.BillEditEvent e) {
		// //����λ����
		nc.ui.pub.beans.UIRefPane refOutSpace = null;
		if (getBillCardPanel().getBodyItem("vspacename") != null)
			refOutSpace = ((nc.ui.pub.beans.UIRefPane) getBillCardPanel()
					.getBodyItem("vspacename").getComponent());

		// ���λ����
		nc.ui.pub.beans.UIRefPane refInSpace = null;
		if (getBillCardPanel().getBodyItem("vspace2name") != null)
			refInSpace = ((nc.ui.pub.beans.UIRefPane) getBillCardPanel()
					.getBodyItem("vspace2name").getComponent());

		if (refOutSpace == null || refInSpace == null) {
			nc.vo.scm.pub.SCMEnv.out("set locator ref pls.");
			return;
		}
		// �޸���Key
		String sItemKey = e.getKey();
		// ��ǰ��
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
			// �����������䴦�� ������ 2009-08-05
			if (null == cOutspaceid && null != e.getValue()
					&& e.getValue() instanceof DefaultConstEnum) {
				cOutspaceid = (String) ((DefaultConstEnum) e.getValue())
						.getValue();
				cOutsname = ((DefaultConstEnum) e.getValue()).getName();
			}
		} else if (e.getKey().equals("vspace2name")) {// zhy 2005-03-09
			// ���������������Ϊ�����ı�Ҳ����÷���
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

		// ���ID�ǿ������name
		if (cOutspaceid == null)
			cOutsname = null;
		if (cInspaceid == null)
			cInsname = null;
		// String cOutsname = refOutSpace.getRefName();
		// String cOutspaceid = refOutSpace.getRefPK();
		// String cInsname =refInSpace.getRefName();
		// String cInspaceid = refInSpace.getRefPK();
		// ������λ������ͬ
		if (cOutspaceid != null && cOutspaceid.equals(cInspaceid)) {
			showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"4008spec", "UPP4008spec-000145")/* @res "������λ������ͬ,���޸ģ�" */);
			// ���
			if (sItemKey != null && sItemKey.equals("vspace2name")) {
				cInsname = null;
				cInspaceid = null;
				// m_voBill.setItemValue(row, "cspace2id", null);
				// m_voBill.setItemValue(row, "vspace2name", null);
				// getBillCardPanel().setBodyValueAt(null, row, "vspace2name");
				// getBillCardPanel().setBodyValueAt(null, row, "cspace2id");
				// ���������
				// getLocatorRefPane2().setPK(null);
			} else {
				// m_voBill.setItemValue(row, "cspaceid", null);
				// m_voBill.setItemValue(row, "vspacename", null);
				// getBillCardPanel().setBodyValueAt(null, row, "vspacename");
				// getBillCardPanel().setBodyValueAt(null, row, "cspaceid");
				cOutsname = null;
				cOutspaceid = null;
				// ���������
				// getLocatorRefPane().setPK(null);
			}
		} else
			showHintMessage("");
		// ����m_vo
		getM_voBill().setItemValue(row, "cspaceid", cOutspaceid);
		getM_voBill().setItemValue(row, "vspacename", cOutsname);
		getM_voBill().setItemValue(row, "cspace2id", cInspaceid);
		getM_voBill().setItemValue(row, "vspace2name", cInsname);

		// �������
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
		// ��������λ������LocatorVO

		// ����
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

		// ��
		if (cOutspaceid != null && cOutspaceid.trim().length() > 0) {
			voOutSpace.setCspaceid(cOutspaceid);
			voOutSpace.setVspacename(cOutsname);
			voOutSpace.setNoutspacenum(dTempNum);
			voOutSpace.setNoutspaceassistnum(dTempAstNum);
			voOutSpace.setNoutgrossnum(dTempGrossNum);
			lvos[0] = voOutSpace;
		}
		// ��
		if (cInspaceid != null && cInspaceid.trim().length() > 0) {
			voInSpace.setCspaceid(cInspaceid);
			voInSpace.setVspacename(cInsname);
			voInSpace.setNinspacenum(dTempNum);
			voInSpace.setNinspaceassistnum(dTempAstNum);
			voInSpace.setNingrossnum(dTempGrossNum);
			lvos[1] = voInSpace;
		}
		// ����
		getM_alLocatorData().set(row, lvos);

		// ����޸��˳���λ������е����к�����
		if (sItemKey != null && sItemKey.equals("vspacename")
				&& getM_alSerialData() != null
				&& getM_alSerialData().size() > row) {
			getM_alSerialData().set(row, null);
			// showHintMessage("�޸���ת����λ��������¼����е����кš�");
		}
		return;
	}

	/**
	 * �����ߣ����˾� ���ܣ����ݱ༭�¼����� ������e���ݱ༭�¼� ���أ� ���⣺ ���ڣ�(2001-5-8 19:08:05)
	 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 * 
	 * 
	 * 
	 */
	public boolean beforeBillItemEdit(nc.ui.pub.bill.BillEditEvent e) {
		return true;
	}

	/**
	 * �����ߣ����˾� ���ܣ���������ѡ��ı� ������ ���أ� ���⣺ ���ڣ�(2001-5-9 9:23:32) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 * 
	 * 
	 * 
	 */
	protected void beforeBillItemSelChg(int iRow, int iCol) {
		// nc.vo.scm.pub.SCMEnv.out("haha,before sel");

	}

	/**
	 * �����ߣ����˾� ���ܣ����ݱ༭ǰ�¼����� ���� isflargessedit �ֶ��ж���Ʒ���Ƿ���Ա༭ ������e���ݱ༭�¼� ���أ� ���⣺
	 * ���ڣ�(2001-5-8 19:08:05) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־�� *
	 * 
	 * 
	 */

	public boolean beforeEdit(nc.ui.pub.bill.BillEditEvent e) {
		// add heyq 2010-10-21

		if (e.getKey().equals("noutassistnum") || e.getKey().equals("noutnum")
				|| e.getKey().equals("vspacename")) {
			// ��ǰ��
			int j = getBillCardPanel().getBillTable().getSelectedRow();
			if (j < 0)
				return true;
			String cinvbasid = (String) getBillCardPanel().getBodyValueAt(j,
					"cinvbasid");
			try {
				// ����������뵥�����򷵻�
				if (querySfmdwf(cinvbasid) == false)
					return true;
				// �����֯ pk_calbody
				String pk_calbody = getBillCardPanel()
						.getHeadItem("pk_calbody").getValue();
				// �ֿ� cwarehouseid
				String cwarehouseid = getBillCardPanel().getHeadItem(
						"cwarehouseid").getValue();
				if (pk_calbody == null || pk_calbody.equals(""))
					throw new BusinessException("�����֯����Ϊ�գ�");
				if (cwarehouseid == null || cwarehouseid.equals(""))
					throw new BusinessException("�ֿⲻ��Ϊ�գ�");
				int t = this.getBillCardPanel().getBillTable().getSelectedRow(); // ����
				// ��������λ
				if (getBillCardPanel().getBodyValueAt(t, "castunitid") == null)
					throw new BusinessException("��������λ����Ϊ�գ�");
				HwtzDialog dlg = new HwtzDialog(getClientEnvironment(),
						pk_calbody, cwarehouseid, cinvbasid);
				dlg.showModal();
				// ѡ��ʧ�ܣ�����ѡ��
				if (dlg.getSstatus() == 0)
					return false;

				// ������ص�ֵ cspaceid

				getBillCardPanel().setBodyValueAt(dlg.getNoutassistnum(), t,
						"noutassistnum"); // ��������
				getM_voBill().setItemValue(t, "noutassistnum",
						dlg.getNoutassistnum());
				BillEditEvent e1 = new BillEditEvent(getBillCardPanel()
						.getBodyItem("noutassistnum").getComponent(),
						getBillCardPanel().getBodyValueAt(t, "noutassistnum"),
						"noutassistnum", t, BillItem.BODY);
				afterEdit(e1);

				getBillCardPanel().setBodyValueAt(dlg.getNoutnum(), t,
						"noutnum"); // ����
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
	// �ж��Ƿ��뵥ά��
	public boolean querySfmdwf(String cinvbasid) throws BusinessException {
		IUAPQueryBS iUAPQueryBS = (IUAPQueryBS) NCLocator.getInstance().lookup(
				IUAPQueryBS.class.getName());
		String sql = "select def20 from bd_invbasdoc where pk_invbasdoc='"
				+ cinvbasid + "'";
		Object[] objs = (Object[]) iUAPQueryBS.executeQuery(sql,
				new ArrayProcessor());
		if (objs == null || objs.length == 0)
			throw new BusinessException("��ǰ����쳣���ڴ�����������в����ڣ�");
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
	 * �����ˣ������� ����ʱ�䣺2008-5-22 ����08:05:50 ����ԭ�򣺲ֿⲻ�����ڻ�λ�������С�
	 */
	public boolean beforeEdit(nc.ui.pub.bill.BillItemEvent e) {
		if (null != e && IItemKey.WAREHOUSE.equals(e.getItem().getKey()))
			return true;
		return super.beforeEdit(e);
	}

	/**
	 * UAP�ṩ�ı༭ǰ����
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
		// zhy �Ӵ��У��ͻ���after��before
		getBillCardPanel().stopEditing();
		nc.ui.pub.bill.BillItem bi = getBillCardPanel().getBodyItem(itemkey);
		WhVO voWh = getM_voBill().getWh();
		// zhy2005-04-22�Ǵ���У���Ҫ������
		if (!itemkey.equals("cinventorycode")) {
			// �������
			Object oTempInvCode = getBillCardPanel().getBodyValueAt(row,
					"cinventorycode");
			// �����
			Object oTempInvName = getBillCardPanel().getBodyValueAt(row,
					"invname");
			// �������δ����������մ�����������в��ɱ༭��
			if (oTempInvCode == null
					|| oTempInvCode.toString().trim().length() == 0) {
				// �޸��ˣ������� �޸����ڣ�2007-9-12����04:11:32 �޸�ԭ�򣺲��ܰ�����Enabled
				// bi.setEnabled(false);
				showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"4008bill", "UPP4008bill-000026")/* @res "����������!" */);
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
	 * �����ߣ����˾� ���ܣ��������/�������������0��ͬ����λ���������� ������ ���أ� ���⣺ ���ڣ�(2001-11-24 12:15:42)
	 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 */
	protected void checkNum(int row) {
		nc.vo.pub.lang.UFDouble dNum = null;
		// ͬ����λ��������
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
															 * "��ע�⣺��������������Ӧ�����㡣"
															 */);
				// ˢ�½���
				getBillCardPanel().setBodyValueAt(null, row,
						getEnvironment().getNumItemKey());
				dNum = null;
			}
			// ����
			if (voaLoc != null && voaLoc.length >= 2) {
				// clear show message
				showHintMessage("");
				// 0==��
				if (voaLoc[0] != null)
					voaLoc[0].setNoutspacenum(dNum);
				// 1==��
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
															 * "��ע�⣺��������������Ӧ�����㡣"
															 */);
				// ˢ�½���
				getBillCardPanel().setBodyValueAt(null, row,
						getEnvironment().getAssistNumItemKey());
				dNum = null;
			}
			// ������
			if (voaLoc != null && voaLoc.length >= 2) {
				// clear show message
				showHintMessage("");
				// 0==��
				if (voaLoc[0] != null)
					voaLoc[0].setNoutspaceassistnum(dNum);
				// 1==��
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
															 * "��ע�⣺��������������Ӧ�����㡣"
															 */);
				// ˢ�½���
				getBillCardPanel().setBodyValueAt(null, row,
						getEnvironment().getGrossNumItemKey());
				dNum = null;
			}
			// ë��
			if (voaLoc != null && voaLoc.length >= 2) {
				// clear show message
				showHintMessage("");
				// 0==��
				if (voaLoc[0] != null)
					voaLoc[0].setNoutgrossnum(dNum);
				// 1==��
				if (voaLoc[1] != null)
					voaLoc[1].setNingrossnum(dNum);
			}

		}

	}

	/**
	 * �����ߣ������� ���ܣ� ������ ���أ� ���⣺ ���ڣ�(2001-5-24 ���� 5:17) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 */
	public boolean checkVO1() {
		try {

			// ִ�����¼�飬�������еļ��ע��------------------------------------------------
			// ------------------------------------------------------------------------------
			// VO���ڼ��
			VOCheck.checkNullVO(getM_voBill());
			// ------------------------------------------------------------------------------
			// Ӧ���������,Ҫ����ǰ��
			// ���ڵ�ʹ��=====================

			// ��ֵ����ȫ���Լ��
			VOCheck.checkNumInput(getM_voBill().getChildrenVO(),
					getEnvironment().getNumItemKey());

			// ���ڵ�ʹ��=====================
			// ��ͷ����ǿռ��
			VOCheck.validate(getM_voBill(), GeneralMethod
					.getHeaderCanotNullString(getBillCardPanel()),
					GeneralMethod.getBodyCanotNullString(getBillCardPanel()));
			// ------------------------------------------------------------------------------
			// ҵ������

			// ������
			VOCheck.checkFreeItemInput(getM_voBill(), getEnvironment()
					.getNumItemKey());
			// ���κ�
			VOCheck.checkLotInput(getM_voBill(), getEnvironment()
					.getNumItemKey());
			// ������
			VOCheck.checkAssistUnitInput(getM_voBill(), getEnvironment()
					.getNumItemKey(), getEnvironment().getAssistNumItemKey());
			// VOCheck.checkAssistUnitInput(m_voBill,
			// getEnvironment().getNumItemKey(),"hsl");
			// ʧЧ����
			VOCheck.checkInvalidateDateInput(getM_voBill(), getEnvironment()
					.getNumItemKey());
			// �������
			// VOCheck.checkdbizdate(m_voBill,
			// getEnvironment().getNumItemKey());
			// �۸�>0���
			VOCheck.checkGreaterThanZeroInput(getM_voBill().getChildrenVO(),
					"nprice", nc.ui.ml.NCLangRes.getInstance().getStrByID(
							"common", "UC000-0000741")/* @res "����" */);
			// ����>0���
			VOCheck
					.checkGreaterThanZeroInput(getM_voBill().getChildrenVO(),
							getEnvironment().getNumItemKey(),
							nc.ui.ml.NCLangRes.getInstance().getStrByID(
									"common", "UC000-0002282")/* @res "����" */);
			VOCheck
					.checkGreaterThanZeroInput(getM_voBill().getChildrenVO(),
							getEnvironment().getAssistNumItemKey(),
							nc.ui.ml.NCLangRes.getInstance().getStrByID(
									"common", "UC000-0002282")/* @res "����" */);
			// ����������
			VOCheck
					.checkNotZeroInput(getM_voBill().getChildrenVO(),
							getEnvironment().getNumItemKey(),
							nc.ui.ml.NCLangRes.getInstance().getStrByID(
									"common", "UC000-0002282")/* @res "����" */);
			VOCheck
					.checkNotZeroInput(getM_voBill().getChildrenVO(),
							getEnvironment().getAssistNumItemKey(),
							nc.ui.ml.NCLangRes.getInstance().getStrByID(
									"common", "UC000-0003971")/* @res "������" */);
			// ���кż��
			VOCheck.checkSNInput(getM_voBill().getChildrenVO(),
					getEnvironment().getNumItemKey());
			// ë�ؼ��
			VOCheck.checkGrossNumInput(getM_voBill().getChildrenVO(),
					getEnvironment().getGrossNumItemKey(), getEnvironment()
							.getNumItemKey());
			return true;

		} catch (ICDateException e) {
			// ��ʾ��ʾ
			String sErrorMessage = GeneralMethod.getBodyErrorMessage(
					getBillCardPanel(), e.getErrorRowNums(), e.getHint());
			showErrorMessage(sErrorMessage);
			return false;
		} catch (ICNullFieldException e) {
			// ��ʾ��ʾ
			String sErrorMessage = GeneralMethod.getBodyErrorMessage(
					getBillCardPanel(), e.getErrorRowNums(), e.getHint());
			showErrorMessage(sErrorMessage);
			return false;
		} catch (ICNumException e) {
			// ��ʾ��ʾ
			String sErrorMessage = GeneralMethod.getBodyErrorMessage(
					getBillCardPanel(), e.getErrorRowNums(), e.getHint());
			showErrorMessage(sErrorMessage);
			return false;
		} catch (ICPriceException e) {
			// ��ʾ��ʾ
			String sErrorMessage = GeneralMethod.getBodyErrorMessage(
					getBillCardPanel(), e.getErrorRowNums(), e.getHint());
			showErrorMessage(sErrorMessage);
			return false;
		} catch (ICSNException e) {
			// ��ʾ��ʾ
			String sErrorMessage = GeneralMethod.getBodyErrorMessage(
					getBillCardPanel(), e.getErrorRowNums(), e.getHint());
			showErrorMessage(sErrorMessage);
			return false;
		} catch (ICLocatorException e) {
			// ��ʾ��ʾ
			String sErrorMessage = GeneralMethod.getBodyErrorMessage(
					getBillCardPanel(), e.getErrorRowNums(), e.getHint());
			showErrorMessage(sErrorMessage);
			return false;
		} catch (ICRepeatException e) {
			// ��ʾ��ʾ
			String sErrorMessage = GeneralMethod.getBodyErrorMessage(
					getBillCardPanel(), e.getErrorRowNums(), e.getHint());
			showErrorMessage(sErrorMessage);
			return false;
		} catch (ICHeaderNullFieldException e) {
			// ��ʾ��ʾ
			String sErrorMessage = GeneralMethod.getHeaderErrorMessage(
					getBillCardPanel(), e.getErrorRowNums(), e.getHint());
			showErrorMessage(sErrorMessage);
			return false;
		} catch (nc.vo.pub.NullFieldException e) {
			showErrorMessage(e.getHint());
			return false;
		} catch (nc.vo.pub.ValidationException e) {
			nc.vo.scm.pub.SCMEnv.out("У���쳣������δ֪����...");
			handleException(e);
			return false;
		}
	}

	/**
	 * �����ߣ����˾� ���ܣ����󷽷�������ǰ��VO��� �����������浥�� ���أ� ���⣺ ���ڣ�(2001-5-24 ���� 5:17)
	 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 */
	protected boolean checkVO(nc.vo.ic.pub.bill.GeneralBillVO voBill) {
		return this.checkVO();
	}

	/**
	 * ���˵��ݲ��� �����ߣ����� ���ܣ���ʼ�����չ��� ������ ���أ� ���⣺ ���ڣ�(2001-7-17 10:33:20)
	 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 */
	public void filterRef(String sCorpID) {
		super.filterRef(sCorpID);
		try {
			// ֻ���ǻ�λ����ֿ�

			((nc.ui.pub.beans.UIRefPane) getBillCardPanel().getHeadItem(
					IItemKey.WAREHOUSE).getComponent())
					.setWhereString("csflag='Y' AND "
							+ nc.ui.ic.pub.bill.initref.RefFilter.getWhFiltStr(
									sCorpID, null));

		} catch (Exception e) {
		}
	}

	/**
	 * ����ǹ̶���λ�Ĵ�������˳�����Ĺ̶���λ ���ߣ����Ӣ �������ڣ�(2001-7-6 16:53:38)
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
	 * ����ǹ̶���λ�Ĵ�������˳�����Ĺ̶���λ ���ߣ����Ӣ �������ڣ�(2001-7-6 16:53:38)
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
	 * �����ߣ����˾� ���ܣ��õ���ѯ�Ի��� ������ ���أ� ���⣺ ���ڣ�(2001-5-9 9:23:32) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 * 
	 * 
	 * 
	 */
	// protected QueryConditionDlgForBill getConditionDlg() {
	// if (ivjQueryConditionDlg == null) {
	// ivjQueryConditionDlg=super.getConditionDlg();
	// //������������ʾ
	// ivjQueryConditionDlg.setCombox("qbillstatus", new String[][] { { "A",
	// nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec","UPPSCMCommon-000217")/*@res
	// "ȫ��"*/ }
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
					// ������������ʾ
					getQueryDlg().setCombox(
							"qbillstatus",
							new String[][] { {
									"A",
									nc.ui.ml.NCLangRes.getInstance()
											.getStrByID("4008spec",
													"UPPSCMCommon-000217") /*
																			 * @res
																			 * "ȫ��"
																			 */} });
				}
			};
		}
		return m_qryDlgHelp;
	}

	/**
	 * ȥ�����������������Ƿ�������Ĳ��� ���ܣ� ���������VO ���أ�boolean ���⣺ ���ڣ�(2002-05-20 19:55:18)
	 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 */
	protected boolean getIsInvTrackedBill(InvVO invvo) {
		// 206,216,251 �ȵ��ݲ���Ҫ���ٵ���ⵥ�ݵĽ�����ơ�ǿ�Ʒ��� false��
		return false;
	}

	/**
	 * ���� LotNumbRefPane1 ����ֵ��
	 * 
	 * @return nc.ui.ic.pub.lot.LotNumbRefPane
	 */
	/* ���棺�˷������������ɡ� */
	protected LocatorRefPane getLocatorRefPane() {
		if (m_refLocator == null) {
			try {
				m_refLocator = new LocatorRefPane(InOutFlag.OUT);
				m_refLocator.setName("LotNumbRefPane");
				m_refLocator.setLocation(38, 1);
				// user code begin {1}
				m_refLocator.setInOutFlag(InOutFlag.OUT);
				// BillData bd = getBillCardPanel().getBillData();
				// ���û�λ����
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
	 * ���� LotNumbRefPane1 ����ֵ��
	 * 
	 * @return nc.ui.ic.pub.lot.LotNumbRefPane
	 */
	/* ���棺�˷������������ɡ� */
	private LocatorRefPane getLocatorRefPane2() {
		if (m_refLocator2 == null) {
			try {
				m_refLocator2 = new LocatorRefPane(InOutFlag.IN);
				m_refLocator2.setName("LotNumbRefPane2");
				m_refLocator2.setLocation(38, 1);
				// user code begin {1}
				m_refLocator2.setInOutFlag(InOutFlag.IN);
				// BillData bd = getBillCardPanel().getBillData();
				// ���û�λ����
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
	 * �����ߣ����˾� ���ܣ���ʼ�� ������ ���أ� ���⣺ ���ڣ�(2001-12-7 18:59:44) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 */
	public void initialize() {
		// ���������༭����
		super.m_bAddBarCodeField = false;
		super.initialize();
		// ���ù��Ƿ�������
		// super.m_sTrackedBillFlag = "N";
		try {
			// ֻ���ǻ�λ����ֿ�

			((nc.ui.pub.beans.UIRefPane) getBillCardPanel().getHeadItem(
					IItemKey.WAREHOUSE).getComponent())
					.setWhereString("csflag='Y' AND "
							+ nc.ui.ic.pub.bill.initref.RefFilter.getWhFiltStr(
									getEnvironment().getCorpID(), null));

		} catch (Exception e) {
		}

	}

	/**
	 * �����ߣ����˾� ���ܣ���ʼ��ϵͳ���� ������ ���أ� ���⣺ ���ڣ�(2001-5-9 9:23:32) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 * 
	 * 
	 * 
	 */
	protected void initPanel() {
		// ��Ҫ���ݲ���
		super.setNeedBillRef(false);

		nc.vo.scm.pub.SCMEnv.out("disable line op.");

		try {
			nc.ui.pub.bill.BillData bd = getBillCardPanel().getBillData();
			if (bd != null) {
				// ���û�λ����
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
	 * �����ߣ����˾� ���ܣ� ������ ���أ� ���⣺ ���ڣ�(2001-11-24 12:15:42) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
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
		 * ����ʵ�ָ÷�������Ӧ��ť�¼���
		 * 
		 * @version (00-6-1 10:32:59)
		 * 
		 * @param bo
		 *            ButtonObject
		 */
		public void onButtonClicked(nc.ui.pub.ButtonObject bo) {
			// add heyq 2010-10-25
			// �������Ǳ���,�Ƚ���һ��

			if (bo.getCode().equals(ICButtonConst.BTN_SAVE)) {
				try {
					int rs = checkSfMdgl();
					if (rs == 2) {
						showWarningMessage("ͬһ����λ�������Ĵ�����������ȫ�����뵥����Ļ���ȫ���������뵥��������ʧ�ܣ�");
						return;
					}
					if (rs == 0) {
						int r = showYesNoMessage("��ǰ��λ���������ȫ�����뵥��������������޸ĺ�ɾ�����Ƿ�Ҫ�������棿");
						if (r != 4)
							return;
					}
				} catch (BusinessException e) {
					e.printStackTrace();
					showWarningMessage(e.getMessage());
					return;
				}
			}
			// ���������޸ģ����Ƚ���һ��
			if (bo.getCode().equals(ICButtonConst.BTN_BILL_EDIT)) {
				try {
					if (checkSfMdgl() == 0) {
						showWarningMessage("�뵥����Ļ�λ����������������޸ģ�������������һ�Ż�λ������");
						return;
					}
				} catch (BusinessException e) {
					e.printStackTrace();
					showWarningMessage(e.getMessage());
					return;
				}
			}
			// ������ɾ�������Ƚ���һ��
			if (bo.getCode().equals(ICButtonConst.BTN_BILL_DELETE)) {
				try {
					if (checkSfMdgl() == 0) {
						showWarningMessage("�뵥����Ļ�λ���������������ɾ����������������һ�Ż�λ������");
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
			// �������Ǳ���
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
						String pk_cinvbasid = items[i].getCinvbasid();// �������
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
					// �������
					setHwtzMap(new HashMap());
				}

			}
			// end heyq 2010-10-25
		}

		// add heyq 2010-10-25

		// 0 ȫ���뵥����1ȫ�������뵥����, 2 ����ʧ��
		private int checkSfMdgl() throws BusinessException {
			boolean checkFlg = true;
			// ���ݽ��飬�������Ƿ�ȫ���뵥����
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
		 * �����ߣ����˾� ���ܣ�ȷ�ϣ����棩���� �������� ���أ� true: �ɹ� false: ʧ��
		 * 
		 * ���⣺ ���ڣ�(2001-5-9 9:23:32) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
		 * 2001/10/29,wnj,��ֳ���������/�����޸�����������ʹ�ø��淶��
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
		 * �����ߣ����˾� ���ܣ����кŷ��� ������ ���أ� ���⣺ ���ڣ�(2001-5-9 9:23:32)
		 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
		 * 
		 * 
		 * 
		 * 
		 */
		public void onSNAssign() {
			// �����ʽ���Ȳ�ѯ��λ/���кţ���ȻqryLocSN��ÿ�ŵ���ֻ��һ�ο⡣
			// if (BillMode.Browse == m_iMode)
			qryLocSN(getM_iLastSelListHeadRow(), QryInfoConst.LOC_SN);
			// ��ǰѡ�е���
			int iCurSelBodyLine = -1;
			if (BillMode.Card == getM_iCurPanel()) {
				iCurSelBodyLine = getBillCardPanel().getBillTable()
						.getSelectedRow();
				if (iCurSelBodyLine < 0) {
					showHintMessage(nc.ui.ml.NCLangRes.getInstance()
							.getStrByID("4008spec", "UPP4008spec-000146")/*
																			 * @res
																			 * "��ѡ��Ҫ�������кŷ�����С�"
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
																			 * "��ѡ��Ҫ�鿴���кŵ��С�"
																			 */);
					return;
				}
			}
			InvVO voInv = null;
			// �ֿ�
			WhVO voWh = null;

			// ���״̬�²쿴���ȶ����ݣ�����һ��Ҫ����setInitVOs֮ǰ

			// ������vo,�����б��»��Ǳ���
			// ����ʽ��
			if (BillMode.Card == getM_iCurPanel()) {
				if (getM_voBill() == null || getM_voBill().getItemCount() <= 0) {
					nc.vo.scm.pub.SCMEnv.out("bill null E.");
					return;
				}
				// ����VO
				GeneralBillItemVO voItem = getBodyVO(iCurSelBodyLine);
				// ����VO
				GeneralBillItemVO voItemPty = getM_voBill().getItemVOs()[iCurSelBodyLine];
				// �ϲ�
				if (voItemPty != null)
					voItemPty.setIDItems(voItem);
				// �����Ĵ��������
				if (voItem != null)
					voInv = voItemPty.getInv();

				if (getM_voBill() != null)
					voWh = getM_voBill().getWh();

			} else
			// �б���ʽ�²쿴���ȶ����ݣ�����һ��Ҫ����setInitVOs֮ǰ
			if (getM_iLastSelListHeadRow() >= 0
					&& getM_iLastSelListHeadRow() < getM_alListData().size()) {
				// ������еĻ�λ����ȸ�������
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
				// �����ģʽ����������Ϊ��
				if (BillMode.Card == getM_iCurPanel()
						&& (BillMode.Update == getM_iMode() || BillMode.New == getM_iMode())) {
					Object oQty = voInv.getAttributeValue(getEnvironment()
							.getNumItemKey());
					if (oQty == null || oQty.toString().trim().length() == 0) {
						showHintMessage(nc.ui.ml.NCLangRes.getInstance()
								.getStrByID("4008spec", "UPP4008spec-000148")/*
																				 * @res
																				 * "��������������"
																				 */);
						return;
					}
				}

				// if (BillMode.Browse == m_iMode)
				// //������еĻ�λ����ȸ�������
				// resetBodyAssistData(m_iLastSelListHeadRow);

				// --- && ---- ֻ�ó���λ������

				LocatorVO voOutLoc = null;
				if (getM_alLocatorData() != null
						&& iCurSelBodyLine < getM_alLocatorData().size()
						&& getM_alLocatorData().get(iCurSelBodyLine) != null)
					voOutLoc = ((LocatorVO[]) getM_alLocatorData().get(
							iCurSelBodyLine))[0];
				// ����ǿ����ʼ��֮.

				if (voOutLoc == null)
					voOutLoc = new LocatorVO();
				getSerialAllocationDlg().setOutSNByLimitLoc(true);
				getSerialAllocationDlg().setDataVO(
						nc.ui.ic.pub.sn.SerialAllocationDlg.OUT, voWh,
						voOutLoc, voInv, getM_iMode(),
						(SerialVO[]) getM_alSerialData().get(iCurSelBodyLine),
						new LocatorVO[] { voOutLoc });

				// ��ǰ�����к���

				int result = getSerialAllocationDlg().showModal();
				if (nc.ui.pub.beans.UIDialog.ID_OK == result
						&& (BillMode.New == getM_iMode() || BillMode.Update == getM_iMode())) {
					// --- && ---- ��λ��������, -----------------
					// --- && ---- ֻ��ѡ��ͬһ����λ�ϵ����кš�

					ArrayList alRes = getSerialAllocationDlg().getDataSpaceVO();
					if (alRes != null && alRes.size() > 0) {
						setSpaceData(iCurSelBodyLine, alRes);
					} else { // ��������л�λ����
						showErrorMessage(nc.ui.ml.NCLangRes.getInstance()
								.getStrByID("4008spec", "UPP4008spec-000149")/*
																				 * @res
																				 * "�޻�λ���ݣ�������ѡ��"
																				 */);
						return;
					}

					SerialVO voaSN[] = getSerialAllocationDlg().getDataSNVO();
					// ���������
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
				// ��־�쳣
				nc.vo.scm.pub.SCMEnv.error(e);
				showErrorMessage(e.getMessage());
			}
		}
		return m_buttonManager;
	}

	protected void qryLocSN(GeneralBillVO voMyBill, int iMode) {
		try {
			// ERRRR,needless to read,���������ݵ������
			if (voMyBill == null || voMyBill.getPrimaryKey() == null) {
				int iFaceRowCount = getBillCardPanel().getRowCount();
				// ��ʼ������ if necessary
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
			// ֻ�����״̬�£��Ż���Ҫ��ֵ�Ͳ�ѯ�������ȡ���������޸ġ�
			if (getM_iMode() == BillMode.Browse) {
				// ���Դ˵����Ƿ���������������������Ҫ����ִ�У����򵥾ݱ�����û����Щ���ݣ����ö���
				int i = 0, iRowCount = voMyBill.getItemCount();
				// �����Ƿ��Ѿ�������Щ�����ˡ�

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
					// zhy�õ�sKeys,Ϊ�˴������λ���к�����
					sKeys[i] = item.getCgeneralbid() + item.getCspaceid();
					voInv = voMyBill.getItemInv(i);
					// �����кŹ�����е����л�û�����кš�
					if (voInv != null && voInv.getIsSerialMgt() != null
							&& voInv.getIsSerialMgt().intValue() != 0
							&& voMyBill.getItemValue(i, "serial") == null) {
						hasSN = false;
						break;
					}
				}
				// �Ѿ���������,�����ݷŵ���Ա�����У���ͬ��vo(needless now )2003-08-07
				if (hasLoc)
					setM_alLocatorData(voMyBill.getLocators());
				if (hasSN)
					setM_alSerialData(voMyBill.getSNs());
				if (hasLoc && hasSN)
					return;

				// =============================================================
				// ��ʼ������ if necessary
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
					// ����ʵ��/������
					if (voMyBill.getItemValue(i, "ninnum") != null
							&& voMyBill.getItemValue(i, "ninnum").toString()
									.length() > 0
							|| voMyBill.getItemValue(i, "noutnum") != null
							&& voMyBill.getItemValue(i, "noutnum").toString()
									.length() > 0)
						break;

				if (i >= iRowCount) // ������
					return; // =============================================================

				// ����ջ�λ�����к�����
				Integer iSearchMode = null;
				// ��Ҫ���λ
				if (!hasLoc
						&& (iMode == QryInfoConst.LOC_SN || iMode == QryInfoConst.LOC)) {
					iSearchMode = new Integer(iMode);
				}
				// ��Ҫ�����к�
				if (!hasSN
						&& (iMode == QryInfoConst.LOC_SN || iMode == QryInfoConst.SN)) {
					iSearchMode = new Integer(iMode);
				}
				if (iSearchMode == null)
					return;
				// WhVO voWh = voMyBill.getWh();
				// ��λ����Ĳֿ⣬���һ�û�л�λ����Ҫ����λ���ݡ����к�

				// iMode = 3;
				// Integer iSearchMode = new Integer(iMode);

				// ////////////////////////////iMode); //���λ & ���к� 3 or ���к� 4
				// zhy��λ�����������ظ������к�(��������λ�����λ��),��Ҫ�����λ�����к�����ȥ��
				ArrayList alAllData = (ArrayList) GeneralBillHelper.queryInfo(
						iSearchMode, voMyBill.getPrimaryKey());
				// ����ջ�λ�����к�����
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

				// ����еĻ��û�λ����
				if (alTempLocatorData != null) {
					// �ŵ�vo�У����ݱ���idִ������ƥ�䡣
					voMyBill.setLocators(alTempLocatorData);
					// getLocators������ by hanwei 2004-01-06
					setM_alLocatorData(voMyBill.getLocators());
				}
				// ����еĻ������к�����
				if (alTempSerialData != null) {
					// �ŵ�vo�У����ݱ���idִ������ƥ�䡣
					// zhyȥ�����λ���к�����
					alTempSerialData = dropInSpaceSN(alTempSerialData, sKeys);
					voMyBill.setSNs(alTempSerialData);
					// getSNs������ by hanwei 2004-01-06
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
		// arraylist �еĻ�������,û�л�,�����µ�.
		if (getM_alListData() != null && getM_alListData().size() > iBillNum
				&& iBillNum >= 0)
			voMyBill = (GeneralBillVO) getM_alListData().get(iBillNum);
		qryLocSN(voMyBill, iMode);
	}

	/**
	 * �����ߣ����˾� ���ܣ����б�ʽ��ѡ��һ�ŵ��� ������ ������alListData�е����� ���أ��� ���⣺ ���ڣ�(2001-11-23
	 * 18:11:18) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 */
	protected void selectBillOnListPanel(int iBillIndex) {
		// ��ѯ��ǰ���ı����λ
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

		// ѡ�б����һ��
		// ���岻����Ϊ��
		if (getBillListPanel().getBodyTable().getRowCount() > 0)
			getBillListPanel().getBodyTable().setRowSelectionInterval(0, 0);
		// end ydy
	}

	/**
	 * �����ߣ����˾� ���ܣ����󷽷������ð�ť״̬����setButtonStatus�е��á� ������ ���أ� ���⣺ ���ڣ�(2001-5-9
	 * 9:23:32) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 * 
	 * 
	 * 
	 */
	protected void setButtonsStatus(int iBillMode) {
		// ���״̬��
		if (iBillMode == BillMode.Browse) {
			// �е��ݵĻ���ɾ��
			if (m_iBillQty > 0)
				getButtonManager().getButton(ICButtonConst.BTN_BILL_DELETE)
						.setEnabled(true);
		} else {
			getButtonManager().getButton(ICButtonConst.BTN_BILL_DELETE)
					.setEnabled(false);
		}

	}

	/**
	 * �����ߣ����˾� ���ܣ� ������ ���أ� ���⣺ ���ڣ�(2001-10-Oct 9:23:32) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
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
		// �Ƚϣ�ֻ��ѡһ����λ�ϵ����к�
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
															 * "ֻ��ѡ��ͬһ����λ�ô����������ѡ��"
															 */);
				return;
			}

		}
		String cInsname = getLocatorRefPane2().getRefName();
		String cInspaceid = getLocatorRefPane2().getRefPK();
		// ���ó���λ
		getM_voBill().setItemValue(iCurSelBodyLine, "cspaceid",
				voLoc0.getCspaceid());
		getM_voBill().setItemValue(iCurSelBodyLine, "vspacename",
				voLoc0.getVspacename());
		// m_voBill.setItemValue(iCurSelBodyLine, "cspace2id", cInspaceid);
		// m_voBill.setItemValue(iCurSelBodyLine, "vspace2name", cInsname);
		// add by hanwei 2003-08-11
		// ������� ѡ��ת���λʱ ������ս���Ĵ���
		getLocatorRefPane().setPK(voLoc0.getCspaceid());

		getBillCardPanel().setBodyValueAt(voLoc0.getCspaceid(),
				iCurSelBodyLine, "cspaceid");
		getBillCardPanel().setBodyValueAt(voLoc0.getVspacename(),
				iCurSelBodyLine, "vspacename");
		// ����Ļ�λ����
		LocatorVO[] voaCurLoc = null;
		if (getM_alLocatorData() != null
				&& iCurSelBodyLine < getM_alLocatorData().size()) {
			if (getM_alLocatorData().get(iCurSelBodyLine) != null
					&& ((LocatorVO[]) getM_alLocatorData().get(iCurSelBodyLine)).length >= 2)
				voaCurLoc = (LocatorVO[]) getM_alLocatorData().get(
						iCurSelBodyLine);
			else
				voaCurLoc = new LocatorVO[2];

			// ����
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

			// ֻд�����λ
			voaCurLoc[0] = voLoc0;
			// �������кŲ�������û��ë��,�˴���������һ��
			voaCurLoc[0].setNoutgrossnum(dTempGrossNum);
			// ��
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