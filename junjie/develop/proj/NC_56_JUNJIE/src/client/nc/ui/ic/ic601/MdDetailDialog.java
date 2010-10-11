package nc.ui.ic.ic601;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.bill.BillListPanel;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDouble;

public class MdDetailDialog extends nc.ui.pub.beans.UIDialog {

	private nc.ui.pub.beans.UIButton ivjbtnClose = null;

	private nc.ui.pub.beans.UIPanel ivjpanelSouth = null;

	private javax.swing.JPanel ivjUIDialogContentPane = null;

	private BillListPanel billList; // �б���ʾ

	private ClientEnvironment clientEnv; // �ͻ��˻���

	private String ccalbodyid; // �����֯

	private String cwarehouseid; // �ֿ�PK

	private String cinventoryid; // ���������

	private String pk_invbasdoc; // �����������

	class IvjEventHandler implements java.awt.event.ActionListener {
		public void actionPerformed(java.awt.event.ActionEvent e) {
			if (e.getSource() == MdDetailDialog.this.getbtnClose())
				connEtoC1(e);
		};
	};

	IvjEventHandler ivjEventHandler = new IvjEventHandler();

	/**
	 * SNDialog1 ������ע�⡣
	 */
	public MdDetailDialog() {
		super();
		initialize();
	}

	/**
	 * SNDialog1 ������ע�⡣
	 */
	public MdDetailDialog(ClientEnvironment client, String ccalbodyid,
			String cwarehouseid, String cinventoryid, String pk_invbasdoc) {
		super();
		this.clientEnv = client;
		this.ccalbodyid = ccalbodyid;
		this.cwarehouseid = cwarehouseid;
		this.cinventoryid = cinventoryid;
		this.pk_invbasdoc = pk_invbasdoc;
		initialize();
	}

	/**
	 * SNDialog1 ������ע�⡣
	 * 
	 * @param parent
	 *            java.awt.Container
	 */
	public MdDetailDialog(java.awt.Container parent) {
		super(parent);
		initialize();
	}

	/**
	 * SNDialog1 ������ע�⡣
	 * 
	 * @param parent
	 *            java.awt.Container
	 * @param title
	 *            java.lang.String
	 */
	public MdDetailDialog(java.awt.Container parent, String title) {
		super(parent, title);
		initialize();
	}

	/**
	 * SNDialog1 ������ע�⡣
	 * 
	 * @param owner
	 *            java.awt.Frame
	 */
	public MdDetailDialog(java.awt.Frame owner) {
		super(owner);
		initialize();
	}

	/**
	 * SNDialog1 ������ע�⡣
	 * 
	 * @param owner
	 *            java.awt.Frame
	 * @param title
	 *            java.lang.String
	 */
	public MdDetailDialog(java.awt.Frame owner, String title) {
		super(owner, title);
		initialize();
	}

	/**
	 * Comment
	 */
	public void btnClose_ActionPerformed(java.awt.event.ActionEvent actionEvent) {
		closeOK();
		return;
	}

	/**
	 * connEtoC1: (btnClose.action.actionPerformed(java.awt.event.ActionEvent)
	 * --> SNDialog1.btnClose_ActionPerformed(Ljava.awt.event.ActionEvent;)V)
	 * 
	 * @param arg1
	 *            java.awt.event.ActionEvent
	 */
	/* ���棺�˷������������ɡ� */
	private void connEtoC1(java.awt.event.ActionEvent arg1) {
		try {
			this.btnClose_ActionPerformed(arg1);
		} catch (java.lang.Throwable ivjExc) {
			handleException(ivjExc);
		}
	}

	/**
	 * ���� btnClose ����ֵ��
	 * 
	 * @return nc.ui.pub.beans.UIButton
	 */
	/* ���棺�˷������������ɡ� */
	private nc.ui.pub.beans.UIButton getbtnClose() {
		if (ivjbtnClose == null) {
			try {
				ivjbtnClose = new nc.ui.pub.beans.UIButton();
				ivjbtnClose.setName("btnClose");
				ivjbtnClose
						.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID(
								"SCMCOMMON", "UPPSCMCommon-000119")/* @res "�ر�" */);
				ivjbtnClose.setActionCommand("btnClose");
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return ivjbtnClose;
	}

	/**
	 * ���� panelSouth ����ֵ��
	 * 
	 * @return nc.ui.pub.beans.UIPanel
	 */
	/* ���棺�˷������������ɡ� */
	private nc.ui.pub.beans.UIPanel getpanelSouth() {
		if (ivjpanelSouth == null) {
			try {
				ivjpanelSouth = new nc.ui.pub.beans.UIPanel();
				ivjpanelSouth.setName("panelSouth");
				ivjpanelSouth.setPreferredSize(new java.awt.Dimension(800, 50));
				getpanelSouth().add(getbtnClose(), getbtnClose().getName());
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return ivjpanelSouth;
	}

	/**
	 * ���� UIDialogContentPane ����ֵ��
	 * 
	 * @return javax.swing.JPanel
	 */
	/* ���棺�˷������������ɡ� */
	private javax.swing.JPanel getUIDialogContentPane() {
		if (ivjUIDialogContentPane == null) {
			try {
				ivjUIDialogContentPane = new javax.swing.JPanel();
				ivjUIDialogContentPane.setName("UIDialogContentPane");
				ivjUIDialogContentPane.setPreferredSize(new java.awt.Dimension(
						800, 400));
				ivjUIDialogContentPane.setLayout(new java.awt.BorderLayout());
				getUIDialogContentPane().add(getBillList(), "Center");
				getUIDialogContentPane().add(getpanelSouth(), "South");
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return ivjUIDialogContentPane;
	}

	/**
	 * ÿ�������׳��쳣ʱ������
	 * 
	 * @param exception
	 *            java.lang.Throwable
	 */
	private void handleException(java.lang.Throwable exception) {

		/* ��ȥ���и��е�ע�ͣ��Խ�δ��׽�����쳣��ӡ�� stdout�� */
		// nc.vo.scm.pub.SCMEnv.out("--------- δ��׽�����쳣 ---------");
		// nc.vo.scm.pub.SCMEnv.error(exception);
	}

	/**
	 * ��ʼ������
	 * 
	 * @exception java.lang.Exception
	 *                �쳣˵����
	 */
	/* ���棺�˷������������ɡ� */
	private void initConnections() throws java.lang.Exception {
		// user code begin {1}
		// user code end
		getbtnClose().addActionListener(ivjEventHandler);
	}

	/**
	 * ��ʼ���ࡣ
	 */
	/* ���棺�˷������������ɡ� */
	private void initialize() {
		try {
			// user code begin {1}
			// user code end
			setName("SNDialog1");
			setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
			setSize(810, 410);
			setTitle("�뵥��ϸ");
			setContentPane(getUIDialogContentPane());
			initDate();
			initConnections();
		} catch (java.lang.Throwable ivjExc) {
			handleException(ivjExc);
			System.out.println(ivjExc.toString());
		}
	}

	// ��ʼ������
	private void initDate() {
		MdDetailVO[] detailVos = null;
		String sql = "select t3.invcode,"
				+ " t3.invname,"
				+ " t3.invspec,"
				+ " t3.invtype,"
				+ " t4.cscode as cspaceid,"
				+ " t1.jbh,"
				+ " t1.md_width,"
				+ " t1.md_length,"
				+ " t1.md_meter,"
				+ " t1.md_note,"
				+ " t1.md_lph,"
				+ " t1.md_zyh,"
				+ " t1.md_zlzsh,"
				+ " t1.remark,"
				+ " t1.zhishu,"
				+ " t1.zhongliang,"
				+ " b.sdzs as yxsdzs,"
				+ " (t1.zhishu - nvl(b.sdzs, 0)) as kyzs"
				+ " from nc_mdxcl_b t1"
				+ " left join nc_mdxcl t2 on t1.pk_mdxcl = t2.pk_mdxcl"
				+ " left join bd_invbasdoc t3 on t2.cinvbasid = t3.pk_invbasdoc"
				+ " left join bd_cargdoc t4 on t1.cspaceid = t4.pk_cargdoc"
				+ " left join (select pk_mdxcl_b, sum(to_number(sdzs)) as sdzs"
				+ " from nc_mdsd"
				+ " where sfsx='0' and to_date(sxrq, 'yyyy-mm-dd') > sysdate"
				+ " group by pk_mdxcl_b) b on b.pk_mdxcl_b = t1.pk_mdxcl_b"
				+ " where t2.ccalbodyidb = '" + ccalbodyid + "'"
				+ " and t2.cwarehouseidb = '" + cwarehouseid + "'"
				+ " and t2.cinventoryidb = '" + cinventoryid + "'"
				+ " and t2.cinvbasid = '" + pk_invbasdoc + "'"
				+ " and t1.dr = 0" + " and t2.dr = 0" + " and t1.zhishu > 0";

		IUAPQueryBS iUAPQueryBS = (IUAPQueryBS) NCLocator.getInstance().lookup(
				IUAPQueryBS.class.getName());
		try {
			ArrayList arList = (ArrayList) iUAPQueryBS.executeQuery(sql,
					new MapListProcessor());
			detailVos = new MdDetailVO[arList.size()];
			for (int i = 0; i < arList.size(); i++) {
				HashMap objMap = (HashMap) arList.get(i);
				MdDetailVO vo = new MdDetailVO();
				vo.setInvcode((String) objMap.get("invcode"));
				vo.setInvname((String) objMap.get("invname"));
				vo.setInvspec((String) objMap.get("invspec"));
				vo.setCspaceid((String) objMap.get("cspaceid"));
				vo.setJbh((String) objMap.get("jbh"));
				if (objMap.get("md_width") != null)
					vo.setMd_width(new UFDouble(((BigDecimal) objMap
							.get("md_width")).doubleValue()));
				if (objMap.get("md_length") != null)
					vo.setMd_length(new UFDouble(((BigDecimal) objMap
							.get("md_length")).doubleValue()));
				if (objMap.get("md_meter") != null)
					vo.setMd_meter(new UFDouble(((BigDecimal) objMap
							.get("md_meter")).doubleValue()));
				vo.setZhishu(new UFDouble(((BigDecimal) objMap.get("zhishu"))
						.doubleValue()));
				vo.setZhongliang(new UFDouble(((BigDecimal) objMap
						.get("zhongliang")).doubleValue()));
				vo.setMd_note((String) objMap.get("md_note"));
				vo.setMd_lph((String) objMap.get("md_lph"));
				vo.setMd_zyh((String) objMap.get("md_zyh"));
				vo.setMd_zlzsh((String) objMap.get("md_zlzsh"));
				vo.setRemark((String) objMap.get("remark"));
				// ��Ч����֧��
				if (objMap.get("yxsdzs") != null)
					vo.setYxsdzs(new UFDouble(((Integer) objMap.get("yxsdzs"))
							.doubleValue()));
				else
					vo.setYxsdzs(new UFDouble(0));
				// ����֧��
				if (objMap.get("kyzs") != null)
					vo.setKyzs(new UFDouble(((Integer) objMap.get("kyzs"))
							.doubleValue()));
				else
					vo.setKyzs(new UFDouble(0));
				detailVos[i] = vo;
			}

			// ����ϼ�����
			MdDetailVO[] hjVos = null;
			if (detailVos != null && detailVos.length > 0) {
				hjVos = new MdDetailVO[detailVos.length + 1];
				UFDouble sum_zhishu = new UFDouble(0); // �ϼ�֧��
				UFDouble sum_zhongliang = new UFDouble(0); // �ϼ�����
				UFDouble sum_yxsdzs = new UFDouble(0);// �ϼ���Ч����֧��
				UFDouble sum_kyzs = new UFDouble(0);// �ϼƿ���֧��
				for (int t = 0; t < detailVos.length; t++) {
					hjVos[t] = detailVos[t];
					sum_zhishu = sum_zhishu.add(detailVos[t].getZhishu());
					sum_zhongliang = sum_zhongliang.add(detailVos[t]
							.getZhongliang());
					sum_yxsdzs = sum_yxsdzs.add(detailVos[t].getYxsdzs());
					sum_kyzs = sum_kyzs.add(detailVos[t].getKyzs());
				}
				MdDetailVO hjvo = new MdDetailVO();
				hjvo.setInvcode("�ϼ�");
				hjvo.setZhishu(sum_zhishu);
				hjvo.setZhongliang(sum_zhongliang);
				hjvo.setYxsdzs(sum_yxsdzs);
				hjvo.setKyzs(sum_kyzs);
				hjVos[detailVos.length] = hjvo;
			}
			getBillList().setHeaderValueVO(hjVos);
		} catch (BusinessException e) {
			e.printStackTrace();
			MessageDialog.showErrorDlg(this, "����", e.getMessage());
		}

	}

	/**
	 * ����ڵ� - ��������ΪӦ�ó�������ʱ���������������
	 * 
	 * @param args
	 *            java.lang.String[]
	 */
	public static void main(java.lang.String[] args) {
		try {
			MdDetailDialog aSNDialog1;
			aSNDialog1 = new MdDetailDialog();
			aSNDialog1.setModal(true);
			aSNDialog1.addWindowListener(new java.awt.event.WindowAdapter() {
				public void windowClosing(java.awt.event.WindowEvent e) {
					System.exit(0);
				};
			});
			aSNDialog1.show();
			java.awt.Insets insets = aSNDialog1.getInsets();
			aSNDialog1.setSize(aSNDialog1.getWidth() + insets.left
					+ insets.right, aSNDialog1.getHeight() + insets.top
					+ insets.bottom);
			aSNDialog1.setVisible(true);
		} catch (Throwable exception) {
			nc.vo.scm.pub.SCMEnv.out("nc.ui.pub.beans.UIDialog �� main() �з����쳣");
			nc.vo.scm.pub.SCMEnv.error(exception);
		}
	}

	public BillListPanel getBillList() {
		if (billList == null) {
			billList = new BillListPanel();
		}
		billList.loadTemplet("H001", null, clientEnv.getUser().getPrimaryKey(),
				clientEnv.getCorporation().getPk_corp());
		return billList;
	}
}