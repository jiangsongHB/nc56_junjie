package nc.ui.pub.querymodel;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;
import javax.swing.WindowConstants;

import nc.bs.logging.Logger;
import nc.ui.ml.NCLangRes;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIButton;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.queryplugin.QEUserTempletSchemaDlg;
import nc.ui.pub.queryplugin.QEUserTempletUtil;
import nc.vo.pub.querymodel.IEnvParam;
import nc.vo.pub.querymodel.MacroParamVO;
import nc.vo.pub.querymodel.ParamUtil;
import nc.vo.pub.querymodel.ParamVO;
import nc.vo.pub.querymodel.QEUserTempletVO;
import nc.vo.pub.querymodel.QueryParamVO;

import org.apache.commons.lang.ArrayUtils;

/**
 * �������öԻ��� �������ڣ�(2003-8-8 14:41:00)
 * 
 * @author���쿡��
 */
public class ParamSetDlg extends AbstractParamSetDlg {

	// ��������Դ
	private String m_defDsName = null;

	private UIPanel ivjPnSouth = null;

	private JPanel ivjUIDialogContentPane = null;

	private UIButton ivjBnCancel = null;

	private UIButton ivjBnOK = null;

	private ParamSetPanel ivjParamSetPanel = null;

	private UIButton ivjBnResemble = null;

	// V56+ ������ʷ���ð�ť
	private UIButton ivjBnHistory = null;

	// V56+ �û������ܱ�����Ϣ������QE��ģ��Ӧ�ã�
	private String[] m_strUserFunccodes = null;

	// V56+
	private String[] m_ids = null;

	IvjEventHandler ivjEventHandler = new IvjEventHandler();

	class IvjEventHandler implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == ParamSetDlg.this.getBnCancel())
				connEtoC3(e);
			if (e.getSource() == ParamSetDlg.this.getBnOK())
				connEtoC4(e);
			if (e.getSource() == ParamSetDlg.this.getBnResemble())
				connEtoC1(e);
			// V56+
			if (e.getSource() == ParamSetDlg.this.getBnHistory())
				bnHistory_ActionPerformed(e);
		};
	};

	/**
	 * ParamDefDlg ������ע�⡣
	 * 
	 * @param parent
	 *            Container
	 */
	public ParamSetDlg(Container parent, String defDsName) {
		super(parent, defDsName);
		m_defDsName = defDsName;
		initialize();
	}

	/**
	 * ȡ��
	 */
	private void bnCancel_ActionPerformed(ActionEvent actionEvent) {
		closeCancel();
	}

	/**
	 * ȷ��
	 */
	private void bnOK_ActionPerformed(ActionEvent actionEvent) {
		getParamSetPanel().stopTableEditing();
		String strErr = getParamSetPanel().check();
		if (strErr != null) {
			MessageDialog.showWarningDlg(this,
					NCLangRes.getInstance().getStrByID("10241201",
							"UPP10241201-000099")/* @res "��ѯ����" */, strErr);
			return;
		}
		closeOK();
	}

	/**
	 * ��ҳǩͬ���������¼��
	 */
	private void bnResemble_ActionPerformed(ActionEvent actionEvent) {
		boolean bClose = getParamSetPanel().resemble();
		// �ر�
		if (bClose) {
			bnOK_ActionPerformed(null);
		}
	}

	/**
	 * connEtoC1: (BnResemble.action.actionPerformed(ActionEvent) -->
	 * ParamSetDlg.bnResemble_ActionPerformed(LActionEvent;)V)
	 * 
	 * @param arg1
	 *            ActionEvent
	 */
	/* ���棺�˷������������ɡ� */
	private void connEtoC1(ActionEvent arg1) {
		try {
			// user code begin {1}
			// user code end
			this.bnResemble_ActionPerformed(arg1);
			// user code begin {2}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {3}
			// user code end
			handleException(ivjExc);
		}
	}

	/**
	 * connEtoC3: (BnCancel.action.actionPerformed(ActionEvent) -->
	 * ParamDefDlg.bnCancel_ActionPerformed(LActionEvent;)V)
	 * 
	 * @param arg1
	 *            ActionEvent
	 */
	/* ���棺�˷������������ɡ� */
	private void connEtoC3(ActionEvent arg1) {
		try {
			// user code begin {1}
			// user code end
			this.bnCancel_ActionPerformed(arg1);
			// user code begin {2}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {3}
			// user code end
			handleException(ivjExc);
		}
	}

	/**
	 * connEtoC4: (BnOK.action.actionPerformed(ActionEvent) -->
	 * ParamDefDlg.bnOK_ActionPerformed(LActionEvent;)V)
	 * 
	 * @param arg1
	 *            ActionEvent
	 */
	/* ���棺�˷������������ɡ� */
	private void connEtoC4(ActionEvent arg1) {
		try {
			// user code begin {1}
			// user code end
			this.bnOK_ActionPerformed(arg1);
			// user code begin {2}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {3}
			// user code end
			handleException(ivjExc);
		}
	}

	/**
	 * ���� BnCancel ����ֵ��
	 * 
	 * @return UIButton
	 */
	/* ���棺�˷������������ɡ� */
	private UIButton getBnCancel() {
		if (ivjBnCancel == null) {
			try {
				ivjBnCancel = new UIButton();
				ivjBnCancel.setName("BnCancel");
				ivjBnCancel.setText(NCLangRes.getInstance().getStrByID(
						"10241201", "UPP10241201-000000")/* @res "ȡ��" */);
				// user code begin {1}
				ivjBnCancel.setPreferredSize(new Dimension(90, 22));
				UIUtil.autoSizeComp(ivjBnCancel, ivjBnCancel.getText());
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjBnCancel;
	}

	/**
	 * ���� BnOK ����ֵ��
	 * 
	 * @return UIButton
	 */
	/* ���棺�˷������������ɡ� */
	private UIButton getBnOK() {
		if (ivjBnOK == null) {
			try {
				ivjBnOK = new UIButton();
				ivjBnOK.setName("BnOK");
				ivjBnOK.setText(NCLangRes.getInstance().getStrByID("10241201",
						"UPP10241201-000001")/* @res "ȷ��" */);
				// user code begin {1}
				ivjBnOK.setPreferredSize(new Dimension(90, 22));
				UIUtil.autoSizeComp(ivjBnOK, ivjBnOK.getText());
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjBnOK;
	}

	/**
	 * ���� BnResemble ����ֵ��
	 * 
	 * @return UIButton
	 */
	/* ���棺�˷������������ɡ� */
	private UIButton getBnResemble() {
		if (ivjBnResemble == null) {
			try {
				ivjBnResemble = new UIButton();
				ivjBnResemble.setName("BnResemble");
				ivjBnResemble.setText(NCLangRes.getInstance().getStrByID(
						"10241201", "UPP10241201-001168")/* @res "���" */);
				// user code begin {1}
				ivjBnResemble.setPreferredSize(new Dimension(90, 22));
				// V55+ zjb
				ivjBnResemble.setToolTipText("����һҳǩ�в�����ȡֵ���Ƶ�����ҳǩ��ͬ������");
				UIUtil.autoSizeComp(ivjBnResemble, ivjBnResemble.getText());
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjBnResemble;
	}

	/**
	 * ��ö������VO���� �������ڣ�(2003-8-8 14:57:25)
	 * 
	 * @param params
	 *            nc.vo.iuforeport.businessquery.ParamVO[]
	 */
	public ParamVO[][] getParamsArray() {
		return getParamSetPanel().getParamsArray();
	}

	/**
	 * ���� PnSouth ����ֵ��
	 * 
	 * @return UIPanel
	 */
	/* ���棺�˷������������ɡ� */
	private UIPanel getPnSouth() {
		if (ivjPnSouth == null) {
			try {
				ivjPnSouth = new UIPanel();
				ivjPnSouth.setName("PnSouth");
				ivjPnSouth.setLayout(getPnSouthFlowLayout());
				// @md by jl 20051214 ���ؿ�ݰ�ť
				// getPnSouth().add(getBnResemble(), getBnResemble().getName());

				// V56+
				getPnSouth().add(getBnHistory(), getBnHistory().getName());

				// zjb+
				getPnSouth().add(getBnResemble(), getBnResemble().getName());

				getPnSouth().add(getBnOK(), getBnOK().getName());
				getPnSouth().add(getBnCancel(), getBnCancel().getName());
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjPnSouth;
	}

	/**
	 * ���� PnSouthFlowLayout ����ֵ��
	 * 
	 * @return FlowLayout
	 */
	/* ���棺�˷������������ɡ� */
	private FlowLayout getPnSouthFlowLayout() {
		FlowLayout ivjPnSouthFlowLayout = null;
		try {
			/* �������� */
			ivjPnSouthFlowLayout = new FlowLayout();
			ivjPnSouthFlowLayout.setVgap(8);
			ivjPnSouthFlowLayout.setHgap(20);
		} catch (java.lang.Throwable ivjExc) {
			handleException(ivjExc);
		}
		;
		return ivjPnSouthFlowLayout;
	}

	/**
	 * ���� TabbedPn ����ֵ��
	 * 
	 * @return UITabbedPane
	 */
	/* ���棺�˷������������ɡ� */
	private ParamSetPanel getParamSetPanel() {
		if (ivjParamSetPanel == null) {
			try {
				ivjParamSetPanel = new ParamSetPanel();
				ivjParamSetPanel.setName("ParamSetPanel");
				ivjParamSetPanel.setDefDsName(m_defDsName);
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjParamSetPanel;
	}

	/**
	 * ���� UIDialogContentPane ����ֵ��
	 * 
	 * @return JPanel
	 */
	/* ���棺�˷������������ɡ� */
	private JPanel getUIDialogContentPane() {
		if (ivjUIDialogContentPane == null) {
			try {
				ivjUIDialogContentPane = new JPanel();
				ivjUIDialogContentPane.setName("UIDialogContentPane");
				ivjUIDialogContentPane.setLayout(new BorderLayout());
				getUIDialogContentPane().add(getPnSouth(), "South");
				getUIDialogContentPane().add(getParamSetPanel(), "Center");
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
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
	 * @i18n upp08110600001=--------- δ��׽�����쳣 ---------
	 */
	private void handleException(java.lang.Throwable exception) {

		/* ��ȥ���и��е�ע�ͣ��Խ�δ��׽�����쳣��ӡ�� stdout�� */
		Logger.error("--------- δ��׽�����쳣 ---------", exception);
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
		getBnCancel().addActionListener(ivjEventHandler);
		getBnOK().addActionListener(ivjEventHandler);
		getBnResemble().addActionListener(ivjEventHandler);
		// V56+
		getBnHistory().addActionListener(ivjEventHandler);
	}

	/**
	 * ��ʼ���ࡣ
	 */
	/* ���棺�˷������������ɡ� */
	private void initialize() {
		try {
			// user code begin {1}
			// user code end
			setName("ParamSetDlg");
			setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			//setSize(600, 400);
			setSize(700, 400); //wangei 2014-07-07 �ӿ��ʼ��ʾ
			setTitle(NCLangRes.getInstance().getStrByID("10241201",
					"UPP10241201-000528")/* @res "��������" */);
			setContentPane(getUIDialogContentPane());
			initConnections();
			setResizable(true);
		} catch (java.lang.Throwable ivjExc) {
			handleException(ivjExc);
		}
		// user code begin {2}
		// user code end
	}

	/**
	 * ���ö������VO���� �������ڣ�(2003-8-8 14:57:25)
	 * 
	 * @param params
	 *            nc.vo.iuforeport.businessquery.ParamVO[]
	 */
	public void setParamsArray(ParamVO[][] paramsArray, String[] ids) {
		// V56+
		m_ids = ids;

		// TODO:�˴�Ӧ����ParamVO[][]--ֻ�ṩ����Ĳ�ѯ����
		int iLen = (paramsArray == null) ? 0 : paramsArray.length;
		ParamVO[][] queryParamArray = new ParamVO[iLen][];
		MacroParamVO[][] macroParamArray = new MacroParamVO[iLen][];
		for (int i = 0; i < iLen; i++) {
			ParamVO[] param = paramsArray[i];
			//
			QueryParamVO[] queryParam = ParamUtil.getQueryParam(param);
			queryParamArray[i] = (ParamVO[]) ArrayUtils.addAll(
					queryParamArray[i], queryParam);
			//
			MacroParamVO[] macroParam = ParamUtil.getMacroParam(param);
			macroParamArray[i] = (MacroParamVO[]) ArrayUtils.addAll(
					macroParamArray[i], macroParam);
		}
		getParamSetPanel()
				.setParamsArray(queryParamArray, ids, macroParamArray);
		// ��ݰ�ť�Ƿ�ɼ�(zjb+)
		getBnResemble().setVisible(iLen > 1);
	}

	/**
	 * �����Զ��廷�������ӿ� �������ڣ�(2004-12-2 17:09:38)
	 * 
	 * @param newM_iEnvParam
	 *            nc.vo.pub.querymodel.IEnvParam
	 */
	public void setIEnvParam(IEnvParam newIEnvParam) {
		getParamSetPanel().setIEnvParam(newIEnvParam);
	}

	/**
	 * V56+ �����û��Ȼ�����Ϣ
	 */
	public void setUserFunccodes(String[] strUserFunccodes) {
		m_strUserFunccodes = strUserFunccodes;
		getBnHistory().setVisible(true);
	}

	/**
	 * V56+ ������ʷ���ð�ť
	 */
	private UIButton getBnHistory() {
		if (ivjBnHistory == null) {
			try {
				ivjBnHistory = new UIButton();
				ivjBnHistory.setName("BnHistory");
				ivjBnHistory.setText("��ʷ����");
				// user code begin {1}
				ivjBnHistory.setPreferredSize(new Dimension(90, 22));
				UIUtil.autoSizeComp(ivjBnHistory, ivjBnHistory.getText());
				ivjBnHistory.setVisible(false);
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjBnHistory;
	}

	/**
	 * V56+ ������ʷ���ã�����������ʷ������
	 */
	private void bnHistory_ActionPerformed(ActionEvent actionEvent) {
		ParamVO[] params = getParamSetPanel().getParamsArray()[0];
		// ����
		QEUserTempletSchemaDlg dlg = new QEUserTempletSchemaDlg(this);
		dlg.setParams(params);
		boolean bSuccess = dlg.setUserFunccodes(m_strUserFunccodes);
		if (!bSuccess) {
			MessageDialog.showWarningDlg(this, "��ѯ����", "δ������ѯ����ģ����Ϣ��");
			return;
		}
		dlg.showModal();
		dlg.destroy();
		if (dlg.getResult() == UIDialog.ID_OK) {
			// ���ݼ��ط����ı䵱ǰ��������
			QEUserTempletVO[] templets = dlg.getSelUserTemplet();
			QEUserTempletUtil.changeParamSetting(params, templets);
			// ���³�ʼ��
			setParamsArray(getParamSetPanel().getParamsArray0(), m_ids);
		}
	}
}