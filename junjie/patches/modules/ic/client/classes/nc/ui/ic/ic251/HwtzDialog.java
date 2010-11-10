package nc.ui.ic.ic251;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIButton;
import nc.ui.pub.bill.BillListPanel;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDouble;

public class HwtzDialog extends nc.ui.pub.beans.UIDialog {

	private nc.ui.pub.beans.UIButton ivjbtnClose = null;

	private nc.ui.pub.beans.UIPanel ivjpanelSouth = null;

	private javax.swing.JPanel ivjUIDialogContentPane = null;

	private BillListPanel billList; // �б���ʾ

	private ClientEnvironment clientEnv; // �ͻ��˻���

	private String ccalbodyid; // �����֯

	private String cwarehouseid; // �ֿ�PK

	private String pk_invbasdoc; // �����������

	private UIButton UIButtonQR = null; // ȷ����ť

	private int sstatus = 0;// ����״̬ 0����ʼ״̬��1��û��ѡ������гɹ���

	private String cspaceid;// ��λID

	private String pk_cspaceid;// ��λID

	private UFDouble noutassistnum = new UFDouble(0);// ������

	private UFDouble noutnum = new UFDouble(0);// ����

	private HwtzVO[] zzvoArrays = null;// ������VO

	class IvjEventHandler implements java.awt.event.ActionListener {
		public void actionPerformed(java.awt.event.ActionEvent e) {
			if (e.getSource() == HwtzDialog.this.getbtnClose())
				connEtoC1(e);
			if (e.getSource() == UIButtonQR) {
				onboQr();
			}
		};
	};

	IvjEventHandler ivjEventHandler = new IvjEventHandler();

	/**
	 * SNDialog1 ������ע�⡣
	 */
	public HwtzDialog() {
		super();
		initialize();
	}

	/**
	 * SNDialog1 ������ע�⡣
	 */
	public HwtzDialog(ClientEnvironment client, String ccalbodyid,
			String cwarehouseid, String pk_invbasdoc) {
		super();
		this.clientEnv = client;
		this.ccalbodyid = ccalbodyid;
		this.cwarehouseid = cwarehouseid;
		this.pk_invbasdoc = pk_invbasdoc;
		initialize();
	}

	/**
	 * SNDialog1 ������ע�⡣
	 * 
	 * @param parent
	 *            java.awt.Container
	 */
	public HwtzDialog(java.awt.Container parent) {
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
	public HwtzDialog(java.awt.Container parent, String title) {
		super(parent, title);
		initialize();
	}

	/**
	 * SNDialog1 ������ע�⡣
	 * 
	 * @param owner
	 *            java.awt.Frame
	 */
	public HwtzDialog(java.awt.Frame owner) {
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
	public HwtzDialog(java.awt.Frame owner, String title) {
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

	// ȷ���¼�
	private void onboQr() {
		HwtzVO[] vos = (HwtzVO[]) billList.getBillListData().getBodyBillModel()
				.getBodyValueVOs(HwtzVO.class.getName());
		// ���û�б���������
		if (vos == null || vos.length == 0) {
			this.setSstatus(0);
			this.closeOK();
			return;
		}
		// ѡ�������
		List selectVoList = new ArrayList();
		for (int i = 0; i < vos.length; i++) {
			if (vos[i].getIsselect().booleanValue() == true)
				selectVoList.add(vos[i]);
		}
		// ���û��ѡ���������
		if (selectVoList.size() == 0) {
			this.setSstatus(0);
			this.closeOK();
			return;
		}
		// �ж�ѡ��ı��������Ƿ�Ϊͬһ��λ
		for (int j = 0; j < selectVoList.size(); j++) {
			HwtzVO vo = (HwtzVO) selectVoList.get(j);
			if (pk_cspaceid == null || pk_cspaceid.equals(""))
				pk_cspaceid = vo.getPk_cspaceid();// ��λ
			else {
				if (!pk_cspaceid.equals(vo.getPk_cspaceid())) {
					MessageDialog.showErrorDlg(this, "����",
							"������λʱ��ѡ����뵥������ͬһ����λ!");
					this.setSstatus(0);
					this.pk_cspaceid = null;
					return;
				}
			}
		}
		// ��ѡ����뵥���ݽ�����Ӵ���
		for (int t = 0; t < selectVoList.size(); t++) {
			HwtzVO vo = (HwtzVO) selectVoList.get(t);
			this.noutassistnum = this.noutassistnum.add(vo.getZhishu()); // ������
			this.noutnum = this.noutnum.add(vo.getZhongliang());// ����
		}
		this.setSstatus(1);
		zzvoArrays = new HwtzVO[selectVoList.size()];
		selectVoList.toArray(zzvoArrays);
		this.closeOK();
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
				ivjpanelSouth
						.setPreferredSize(new java.awt.Dimension(1024, 50));
				// ����ȷ����ť
				getpanelSouth().add(getUIButtonQR(), getUIButtonQR().getName());
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
						1024, 700));
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
		getUIButtonQR().addActionListener(ivjEventHandler);
	}

	/**
	 * ��ʼ���ࡣ
	 */
	/* ���棺�˷������������ɡ� */
	private void initialize() {
		try {
			billList = getBillList();
			setName("SNDialog1");
			setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
			setSize(1024, 700);
			setTitle("�뵥��λ����");
			setContentPane(getUIDialogContentPane());
			initDate();
			initConnections();
		} catch (java.lang.Throwable ivjExc) {
			handleException(ivjExc);
			System.out.println(ivjExc.toString());
		}
	}

	// ��ʼ������
	private void initDate() throws BusinessException {
		HwtzVO[] rsVos = null;
		String sql = "select t4.pk_invbasdoc,"
				+ " t4.invcode,"
				+ " t4.invname,"
				+ " t3.csname,"
				+ " t3.pk_cargdoc as cspaceid,"
				+ " t2.jbh,"
				+ " t2.zhishu,"
				+ " t2.zhongliang,"
				+ " t2.md_width,"
				+ " t2.md_length,"
				+ " t2.md_meter,"
				+ " t2.md_note,"
				+ " t2.md_lph,"
				+ " t2.md_zyh,"
				+ " t2.md_zlzsh,"
				+ " t2.remark,"
				+ " t2.def6,"
				+ " t2.def7,"
				+ " t2.def8,"
				+ " t2.def9,"
				+ " t2.pk_mdxcl_b"
				+ " from nc_mdxcl t1"
				+ " left join nc_mdxcl_b t2 on t1.pk_mdxcl = t2.pk_mdxcl"
				+ " left join bd_cargdoc t3 on t2.cspaceid = t3.pk_cargdoc"
				+ " left join bd_invbasdoc t4 on t1.cinvbasid = t4.pk_invbasdoc"
				+ " left join nc_mdcrk t5 on t2.pk_mdxcl_b=t5.pk_mdxcl_b"
				+ " left join ic_general_b t6 on t5.cgeneralbid=t6.cgeneralbid"
				+ " left join ic_general_h t7 on t6.cgeneralhid=t7.cgeneralhid"
				+ " where t1.dr=0 and t2.dr=0 and t1.cinvbasid='"
				+ pk_invbasdoc + "' and t1.ccalbodyidb='" + ccalbodyid
				+ "' and t1.cwarehouseidb='" + cwarehouseid
				+ "' and t5.cbodybilltypecode in ('45','4A')"
				+ " and t7.fbillflag='3' order by t4.invcode desc";
		IUAPQueryBS iUAPQueryBS = (IUAPQueryBS) NCLocator.getInstance().lookup(
				IUAPQueryBS.class.getName());
		List qList = (List) iUAPQueryBS.executeQuery(sql,
				new MapListProcessor());
		if (qList == null || qList.size() == 0)
			return;
		rsVos = new HwtzVO[qList.size()];
		for (int i = 0; i < qList.size(); i++) {
			Map rsvo = (Map) qList.get(i);
			HwtzVO vo = new HwtzVO();
			vo.setPk_mdxcl_b((String) rsvo.get("pk_mdxcl_b"));
			vo.setInvcode((String) rsvo.get("invcode"));// �������
			vo.setInvname((String) rsvo.get("invname"));// �������
			vo.setCspaceid((String) rsvo.get("csname"));// ��λ
			vo.setPk_cspaceid((String) rsvo.get("cspaceid"));// ��λPK
			vo.setJbh((String) rsvo.get("jbh"));// �����
			vo.setZhishu(new UFDouble((BigDecimal) rsvo.get("zhishu")));// ֧��
			vo.setZhongliang(new UFDouble((BigDecimal) rsvo.get("zhongliang")));// ֧��
			vo.setIsselect(new UFBoolean(false)); // �Ƿ�ѡ��

			BigDecimal md_width = (BigDecimal) rsvo.get("md_width");
			if (md_width != null)
				vo.setMd_width(new UFDouble((BigDecimal) rsvo.get("md_width")));// ���
			BigDecimal md_length = (BigDecimal) rsvo.get("md_length");
			if (md_length != null)
				vo
						.setMd_length(new UFDouble((BigDecimal) rsvo
								.get("md_length")));// ����
			BigDecimal md_meter = (BigDecimal) rsvo.get("md_meter");
			if (md_meter != null)
				vo.setMd_meter(new UFDouble((BigDecimal) rsvo.get("md_meter")));// ����
			vo.setDef6((String) rsvo.get("def6"));// ���
			vo.setMd_note((String) rsvo.get("md_note"));// ʵ���*��*�� md_note
			vo.setMd_lph((String) rsvo.get("md_lph"));// ¯���� md_lph
			vo.setMd_zyh((String) rsvo.get("md_zyh"));// ��Դ�� md_zyh
			vo.setMd_zlzsh((String) rsvo.get("md_zlzsh"));// �ʱ�֤��� md_zlzsh
			vo.setRemark((String) rsvo.get("remark"));// ��ע remark
			vo.setDef7((String) rsvo.get("def7"));
			vo.setDef8((String) rsvo.get("def8"));
			vo.setDef9((String) rsvo.get("def9"));
			rsVos[i] = vo;
		}
		billList.setBodyValueVO(rsVos);
		if (rsVos != null && rsVos.length > 0)
			for (int i = 0; i < rsVos.length; i++) {
				billList.getBodyBillModel()
						.setCellEditable(i, "isselect", true);
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
			HwtzDialog aSNDialog1;
			aSNDialog1 = new HwtzDialog();
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
		billList.loadTemplet("H004", null, clientEnv.getUser().getPrimaryKey(),
				clientEnv.getCorporation().getPk_corp());
		billList.setSize(new Dimension(1024, 650));
		return billList;
	}

	private UIButton getUIButtonQR() {
		if (UIButtonQR == null) {
			UIButtonQR = new UIButton();
			UIButtonQR.setBounds(new Rectangle(512, 4, 75, 20));
			UIButtonQR.setText("ȷ  ��");
			UIButtonQR.setToolTipText("<HTML><B>ȷ��(CTRL + S)</B></HTML>");
		}
		return UIButtonQR;
	}

	public int getSstatus() {
		return sstatus;
	}

	public void setSstatus(int sstatus) {
		this.sstatus = sstatus;
	}

	public String getCspaceid() {
		return cspaceid;
	}

	public void setCspaceid(String cspaceid) {
		this.cspaceid = cspaceid;
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

	public String getPk_cspaceid() {
		return pk_cspaceid;
	}

	public void setPk_cspaceid(String pk_cspaceid) {
		this.pk_cspaceid = pk_cspaceid;
	}

	public HwtzVO[] getZzvoArrays() {
		return zzvoArrays;
	}

	public void setZzvoArrays(HwtzVO[] zzvoArrays) {
		this.zzvoArrays = zzvoArrays;
	}

}