package nc.ui.ic.pub.bill.dialog;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

import nc.ui.ic.md.dialog.MDUtils;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIButton;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.bill.BillCardPanel;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDouble;

/**
 * @author
 */
public class MBJSDialog extends UIDialog implements ActionListener {

	private final String ok = "确定";

	private final String cancel = "取消";

	private final String calc = "计算";

	private HashMap<String, UFDouble> hmpResult = new HashMap<String, UFDouble>();

	private HashMap<String, UFDouble> hmpfrom = null;

	private UIButton UIButtonOK = null;

	private UIButton UIButtonCalcu = null;

	private UIButton UIButtonCancel = null;

	UIPanel UIpanel2;

	private UFDouble grossprice = null;

	private UFDouble grossweight = null;

	private UFDouble grosssumny = null;

	private UFDouble stuffprice = null;

	private UFDouble stuffweight = null;

	private UFDouble stuffsumny = null;

	private UFDouble grossratio = null;

	Object cinvbasid;

	Object cproviderid;

	public MBJSDialog(Container parent, HashMap<String, UFDouble> hmpfrom,
			Object cinvbasid, Object cproviderid) {
		super(parent, "毛边计算");
		this.hmpfrom = hmpfrom;
		this.cinvbasid = cinvbasid;
		this.cproviderid = cproviderid;

		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.add(getBillCardPanel(), BorderLayout.CENTER);

		UIpanel2 = new UIPanel();
		UIpanel2.add(getUIButtonOK());
		UIpanel2.add(getUIButtonCancel());
		UIpanel2.add(getUIButtonCalcu());

		add(UIpanel2, BorderLayout.SOUTH);

		this.setSize(550, 260);

		initdata();

	}

	private void initdata() {
		getBillCardPanel().getHeadItem("edgeprice").setValue(
				hmpfrom.get("grossprice"));
		getBillCardPanel().getHeadItem("edgezl").setValue(
				hmpfrom.get("grossweight"));
		getBillCardPanel().getHeadItem("edgemny").setValue(
				hmpfrom.get("grosssumny"));
		getBillCardPanel().getHeadItem("materprice").setValue(
				hmpfrom.get("stuffprice"));
		getBillCardPanel().getHeadItem("materzl").setValue(
				hmpfrom.get("stuffweight"));
		getBillCardPanel().getHeadItem("matermny").setValue(
				hmpfrom.get("stuffsumny"));

		getBillCardPanel().getHeadItem("invpk").setValue(cinvbasid);
		getBillCardPanel().getHeadItem("cproviderid").setValue(cproviderid);

		getBillCardPanel().execHeadLoadFormulas();
	}

	BillCardPanel card;

	BillCardPanel getBillCardPanel() {
		if (card == null) {
			card = new BillCardPanel();
			card.loadTemplet("MD02", "MD02", ClientEnvironment.getInstance()
					.getUser().getPrimaryKey(), ClientEnvironment.getInstance()
					.getCorporation().getPrimaryKey());
		}
		return card;
	}

	/**
	 * This method initializes UIButtonOK
	 * 
	 * @return nc.ui.pub.beans.UIButton
	 */
	private UIButton getUIButtonOK() {
		if (UIButtonOK == null) {
			UIButtonOK = new UIButton();
			// UIButtonOK.setBounds(50, 120, 55, 23);
			UIButtonOK.setText(ok);
			UIButtonOK.addActionListener(this);
		}
		return UIButtonOK;
	}

	private UIButton getUIButtonCalcu() {
		if (UIButtonCalcu == null) {
			UIButtonCalcu = new UIButton();
			// UIButtonOK.setBounds(50, 120, 55, 23);
			UIButtonCalcu.setText(calc);
			UIButtonCalcu.addActionListener(this);
			// UIButtonCalcu.addActionListener(new
			// java.awt.event.ActionListener() {
			// public void actionPerformed(java.awt.event.ActionEvent e) {
			// onCalcu();
			// }
			//
			// private void onCalcu() {
			//					
			// }
			// });
		}
		return UIButtonCalcu;
	}

	/**
	 * This method initializes UIButtonCancel
	 * 
	 * @return nc.ui.pub.beans.UIButton
	 */
	private UIButton getUIButtonCancel() {
		if (UIButtonCancel == null) {
			UIButtonCancel = new UIButton();
			// UIButtonCancel.setBounds(129, 120, 55, 23);
			UIButtonCancel.setText(cancel);
			UIButtonCancel.addActionListener(this);
		}
		return UIButtonCancel;
	}

	/**
	 * @return 返回 result。
	 */
	public HashMap<String, UFDouble> gethmpResult() {
		return hmpResult;
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() instanceof UIButton) {
			UIButton button = (UIButton) e.getSource();
			if (button.equals(getUIButtonCalcu())) {
				onCalc();
			} else if (button.equals(getUIButtonCancel())) {
				closeCancel();
			} else if (button.equals(getUIButtonOK())) {
				if (isCalc) {
					hmpResult
							.put("grossprice", new UFDouble(
									(String) getBillCardPanel().getHeadItem(
											"edgeprice").getValueObject()));
					hmpResult.put("grosssumny", new UFDouble(
							(String) getBillCardPanel().getHeadItem("edgemny")
									.getValueObject()));
					hmpResult.put("grossweight", new UFDouble(
							(String) getBillCardPanel().getHeadItem("edgezl")
									.getValueObject()));
					hmpResult.put("stuffprice", new UFDouble(
							(String) getBillCardPanel().getHeadItem(
									"materprice").getValueObject()));
					hmpResult.put("stuffweight", new UFDouble(
							(String) getBillCardPanel().getHeadItem("materzl")
									.getValueObject()));
					hmpResult.put("stuffsumny", new UFDouble(
							(String) getBillCardPanel().getHeadItem("matermny")
									.getValueObject()));
					closeOK();
				} else {
					MessageDialog.showErrorDlg(this, "提示", "还没有计算毛边重量");
				}
			}
		}
	}

	boolean isCalc = false;

	private void onCalc() {
		String gg = (String) getBillCardPanel().getHeadItem("gg")
				.getValueObject();// 规格
		boolean isnum = true;
		UFDouble hd = UFDouble.ZERO_DBL;
		try {
			hd = new UFDouble(gg);
		} catch (Exception e) {
			isnum = false;
		}
		if (!isnum) {
			MessageDialog.showErrorDlg(this, "错误", "该存货不能执行毛边计算");
			return;
		}

		grossprice = new UFDouble((String) getBillCardPanel().getHeadItem(
				"edgeprice").getValueObject());
		UFDouble materzl = new UFDouble((String) getBillCardPanel()
				.getHeadItem("materzl").getValueObject());
		if (grossprice == null || grossprice.doubleValue() <= 0) {
			MessageDialog.showErrorDlg(this, "错误", "毛边单价不能小于等于0");
			return;
		}
		if (materzl == null || materzl.doubleValue() == 0) {
			MessageDialog.showErrorDlg(this, "错误", "正材重量不能等于0");
			return;
		}

		UFDouble mbxs = new UFDouble(0);
		try {
			mbxs = MDUtils.getMBXS(cinvbasid.toString(),
					cproviderid.toString(), hd);
			if (mbxs == null || mbxs.doubleValue() <= 0) {
				MessageDialog.showErrorDlg(this, "错误", "毛边系数不能小于等于0");
				return;
			}
		} catch (BusinessException e) {
			e.printStackTrace();
			MessageDialog.showErrorDlg(this, "错误", e.getMessage());
			return;
		}
		// 毛边重量=毛边系数*正材重量
		// 毛边金额=毛边单价*毛边重量

		grossweight = mbxs.multiply(materzl);
		grosssumny = grossprice.multiply(grossweight);

		getBillCardPanel().getHeadItem("edgezl").setValue(grossweight);
		getBillCardPanel().getHeadItem("edgemny").setValue(grosssumny);
		isCalc = true;
	}
}
