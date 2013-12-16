package nc.ui.ic.ic261;

/**
 * 创建者：顾焱
 * 创建日期：(2001-5-14 13:32:23)
 * 功能：
 * 修改日期，修改人，修改原因，注释标志：
 */
import java.util.ArrayList;

import nc.bd.accperiod.AccountCalendar;
import nc.bs.logging.Logger;
import nc.jdbc.framework.processor.ArrayListProcessor;
import nc.jdbc.framework.processor.ResultSetProcessor;

import nc.ui.dbcache.DBCacheFacade;
import nc.ui.ic.pub.bill.SpecialBillHelper;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIButton;
import nc.ui.pub.beans.UICheckBox;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UILabel;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UIRadioButton;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.beans.UITextField;
import nc.ui.pub.beans.ValueChangedListener;
import nc.ui.scm.pub.cache.CacheTool;

import nc.vo.dbcache.query.Criteria;
import nc.vo.dbcache.query.MatchCriteria;
import nc.vo.dbcache.query.QueryClause;
import nc.vo.ic.ic261.CheckMode;
import nc.vo.ic.ic261.ChoosespaceVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.BusinessRuntimeException;
import nc.vo.pub.lang.UFBoolean;

public class CheckTypeChooser extends UIDialog implements ValueChangedListener {
	private javax.swing.JPanel ivjUIDialogContentPane = null;
	private UILabel ivjUILabelWarehouseName = null;
	private UIRadioButton ivjUIRadBtnCyc = null;
	private UIRadioButton ivjUIRadBtnDate = null;
	private UIRadioButton ivjUIRadBtnGoods = null;
	private UIRadioButton ivjUIRadBtnMinusStore = null;
	private UIRadioButton ivjUIRadBtnSpace = null;
	
	private UIRadioButton ivjUIRadBtnMD = null;
	private UIButton ivjBtnChooseMDSpace = null;
	
	
	private UIRadioButton ivjUIRadBtnStatic = null;
	private UIRadioButton ivjUIRadBtnWhAdmin = null;
	private javax.swing.ButtonGroup group= new javax.swing.ButtonGroup();
	private UIRadioButton ivjUIRadBtnWhlWh = null;
	IvjEventHandler ivjEventHandler = new IvjEventHandler();
	private UIButton ivjUIBtnCancel = null;
	private UIButton ivjUIBtnOK = null;
	private UILabel ivjUILabel3 = null;
	private UIRefPane ivjUIRefPane1 = null;
	private UIButton ivjBtnChooseGooods = null;
	private UIButton ivjBtnChooseSpace = null;
	private UILabel ivjUILabel4 = null;
	private UILabel ivjUILabel5 = null;
	private UIRefPane ivjUIRefPKeeper = null;
	private UITextField ivjUITxtFldDateDay = null;
	private UITextField ivjUITxtFldNActDay = null;
	private boolean m_bIsSpcaceMgt=false;
	private String m_sStartDate=null;//系统启用日期
	//修改人：刘家清 修改日期：2007-10-23上午10:18:23 修改原因：用登录日期来代替系统日期来做后台查询
	private String m_sLogDate=null;//系统登录日期
	/**
		radFlag=CheckMode.WholeWh;		盘点类型
		freeItemFlag=CheckMode.UnCheck; 自由项选中标志
		pcFlag=CheckMode.UnCheck;		批次选中标志
		spaceFlag=CheckMode.UnCheck;	货位选中标志
		param1Flag=CheckMode.UnCheck;	账面为零参加盘点选中标志
		param2Flag=CheckMode.UnCheck;	n天未出库且账面为零参加盘点选中标志
		param2DayNum=0;					n天未出库且账面为零参加盘点天数
		dateDayNum=0;					保质期盘点天数
		nActdayNum=0;					无动态盘点天数
		keeperID=null;					库管员id

		dlgGoods=new ChooseGoods();		选择存货
		dlgSpace=new ChooseSpace();		选择货位

	*/
	private int radFlag= CheckMode.WholeWh;
	private int dateDayNum= 0;
	private int nActdayNum= 0;
	private UIPanel ivjUIPanelCheckType = null;
	private UIPanel ivjUIPanelParam = null;
	private UICheckBox ivjUIChkDataZero = null;
	private UICheckBox ivjUIChkNoOut = null;
	private UITextField ivjUITxtFldDayNum = null;
	private ChooseGoods ivjdlgGoods = null;
	private ChooseSpace ivjdlgSpace = null;
	private ArrayList m_alReturnInfo= null;
	private String m_sCorpID= null;
	private String m_sUserID= null;
	private String m_WhID= null;
	private UILabel ivjUILabel31 = null;
	private UILabel ivjUILabel311 = null;
	private UILabel ivjUILabel3111 = null;
	private UICheckBox ivjUIChkBCCount = null;

class IvjEventHandler implements java.awt.event.ActionListener, nc.ui.pub.beans.ValueChangedListener {
		public void actionPerformed(java.awt.event.ActionEvent e) {
			if (e.getSource() == CheckTypeChooser.this.getUIBtnOK())
				connEtoC1(e);
			if (e.getSource() == CheckTypeChooser.this.getUIBtnCancel())
				connEtoC2(e);
			if (e.getSource() == CheckTypeChooser.this.getUIRadBtnWhlWh())
				connEtoC3();
			if (e.getSource() == CheckTypeChooser.this.getUIRadBtnSpace())
				connEtoC4();
			if (e.getSource() == CheckTypeChooser.this.getUIRadBtnGoods())
				connEtoC5();
			if (e.getSource() == CheckTypeChooser.this.getUIRadBtnCyc())
				connEtoC6();
			if (e.getSource() == CheckTypeChooser.this.getUIRadBtnWhAdmin())
				connEtoC7();
			if (e.getSource() == CheckTypeChooser.this.getUIRadBtnMinusStore())
				connEtoC8();
			if (e.getSource() == CheckTypeChooser.this.getUIRadBtnStatic())
				connEtoC9();
			if (e.getSource() == CheckTypeChooser.this.getUIRadBtnDate())
				connEtoC10();
			if (e.getSource() == CheckTypeChooser.this.getBtnChooseSpace())
				connEtoC11();
			if (e.getSource() == CheckTypeChooser.this.getBtnChooseGooods())
				connEtoC12();
			if (e.getSource() == CheckTypeChooser.this.getUIChkDataZero())
				connEtoC14(e);
			if (e.getSource() == CheckTypeChooser.this.getUIChkNoOut())
				connEtoC15(e);
			if (e.getSource() == CheckTypeChooser.this.getUIRadBtnDynamic())
				connEtoC16();
			//ADD BY OUYANGZHB 2012-04-12
			if (e.getSource() == CheckTypeChooser.this.getBtnChooseMDSpace())
				connEtoC11();
			if (e.getSource() == CheckTypeChooser.this.getUIRadBtnMD())
				uIRadBtnMD_onClick();
		};
		public void valueChanged(nc.ui.pub.beans.ValueChangedEvent event) {
			if (event.getSource() == CheckTypeChooser.this.getUIRefPane1())
				connEtoC13(event);
		};
	};
	private UICheckBox ivjUIChkDataPeriod = null;

/**
 * CheckTypeChooser 构造子注解。
 */
public CheckTypeChooser() {
	super();
	initialize();
}
/**
 * CheckTypeChooser 构造子注解。
 * @param parent java.awt.Container
 */
public CheckTypeChooser(java.awt.Container parent) {
	super(parent);
	initialize();
}
/**
 * CheckTypeChooser 构造子注解。
 * @param parent java.awt.Container
 * @param title java.lang.String
 */
public CheckTypeChooser(java.awt.Container parent, String title) {
	super(parent, title);
	initialize();
}
/**
 * CheckTypeChooser 构造子注解。
 * @param owner java.awt.Frame
 */
public CheckTypeChooser(java.awt.Frame owner) {
	super(owner);
	initialize();
}
/**
 * CheckTypeChooser 构造子注解。
 * @param owner java.awt.Frame
 * @param title java.lang.String
 */
public CheckTypeChooser(java.awt.Frame owner, String title) {
	super(owner, title);
	initialize();
}
/**
 * Comment
 */
protected void btnChooseGooods_onClick() {
	try {

		m_WhID= getUIRefPane1().getRefPK();

		getdlgGoods().setWhID(m_WhID);
		getdlgGoods().setCorpID(m_sCorpID);
		getdlgGoods().setUserID(m_sUserID);
		if (getdlgGoods().showModal() == UIDialog.ID_OK) {
		};

		return;
	} catch (Exception e) {
		nc.vo.scm.pub.SCMEnv.error(e);
	}
}
/**
 * Comment
 */
protected void btnChooseSpace_onClick() {
	try {
		
		
		String[] keys= new String[1];
		keys[0]= getUIRefPane1().getRefPK();

		ResultSetProcessor rs = new ArrayListProcessor();
		ArrayList al = (ArrayList)DBCacheFacade.runQuery("select pk_cargdoc, cscode, csname, endflag from bd_cargdoc where pk_stordoc='"+keys[0]+"' and endflag='Y'", rs);
	
		if (al == null || al.size() == 0) {
			nc.ui.pub.beans.MessageDialog.showWarningDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec","UPP4008spec-000151")/*@res "警告！"*/, nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec","UPP4008spec-000152")/*@res "无此仓库货位信息！"*/);

		} else {
			ChoosespaceVO[] vos= new ChoosespaceVO[al.size()];
			for (int i= 0; i < al.size(); i++) {
				vos[i]= new ChoosespaceVO();
				Object[] x = (Object[])al.get(i);
				if (x==null) continue;
				vos[i].setPk_cargdoc((String) x[0]);
				vos[i].setCscode((String) x[1]);
				vos[i].setCsname((String) x[2]);
				vos[i].setCsendflag((String) x[3]);
			}
			
			getdlgSpace().setValue(vos);
			if (getdlgSpace().showModal() ==UIDialog.ID_OK) {
			};
		}

	} catch (Exception e) {
		nc.vo.scm.pub.SCMEnv.error(e);
	}

	return;
}
/**
 * connEtoC1:  (UIBtnOK.action.actionPerformed(java.awt.event.ActionEvent) --> CheckTypeChooser.uIBtnOK_ActionPerformed(Ljava.awt.event.ActionEvent;)V)
 * @param arg1 java.awt.event.ActionEvent
 */
/* 警告：此方法将重新生成。 */
private void connEtoC1(java.awt.event.ActionEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.onOK(arg1);
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC10:  (UIRadBtnDate.action. --> CheckTypeChooser.uIRadBtnDate_ActionEvents1()V)
 */
/* 警告：此方法将重新生成。 */
private void connEtoC10() {
	try {
		// user code begin {1}
		// user code end
		this.uIRadBtnDate_onClick();
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC11:  (BtnChooseSpace.action. --> CheckTypeChooser.btnChooseSpace_ActionEvents()V)
 */
/* 警告：此方法将重新生成。 */
private void connEtoC11() {
	try {
		// user code begin {1}
		// user code end
		this.btnChooseSpace_onClick();
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC12:  (BtnChooseGooods.action. --> CheckTypeChooser.btnChooseGooods_ActionEvents()V)
 */
/* 警告：此方法将重新生成。 */
private void connEtoC12() {
	try {
		// user code begin {1}
		// user code end
		this.btnChooseGooods_onClick();
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC13:  (UIRefPane1.valueChanged.valueChanged(nc.ui.pub.beans.ValueChangedEvent) --> CheckTypeChooser.uIRefPane1_ValueChanged(Lnc.ui.pub.beans.ValueChangedEvent;)V)
 * @param arg1 nc.ui.pub.beans.ValueChangedEvent
 */
/* 警告：此方法将重新生成。 */
private void connEtoC13(nc.ui.pub.beans.ValueChangedEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.uIRefPane1_ValueChanged(arg1);
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC14:  (UIChkDataZero.action.actionPerformed(java.awt.event.ActionEvent) --> CheckTypeChooser.uIChkDataZero_onClick(Ljava.awt.event.ActionEvent;)V)
 * @param arg1 java.awt.event.ActionEvent
 */
/* 警告：此方法将重新生成。 */
private void connEtoC14(java.awt.event.ActionEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.uIChkDataZero_onClick(arg1);
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC15:  (UIChkNoOut.action.actionPerformed(java.awt.event.ActionEvent) --> CheckTypeChooser.uIChkNoOut_onClick(Ljava.awt.event.ActionEvent;)V)
 * @param arg1 java.awt.event.ActionEvent
 */
/* 警告：此方法将重新生成。 */
private void connEtoC15(java.awt.event.ActionEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.uIChkNoOut_onClick(arg1);
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC2:  (UIBtnCancel.action.actionPerformed(java.awt.event.ActionEvent) --> CheckTypeChooser.uIBtnCancel_ActionPerformed(Ljava.awt.event.ActionEvent;)V)
 * @param arg1 java.awt.event.ActionEvent
 */
/* 警告：此方法将重新生成。 */
private void connEtoC2(java.awt.event.ActionEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.onCancel(arg1);
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC3:  (UIRadBtnWhlWh.action. --> CheckTypeChooser.uIRadBtnWhlWh_ActionEvents()V)
 */
/* 警告：此方法将重新生成。 */
private void connEtoC3() {
	try {
		// user code begin {1}
		// user code end
		this.uIRadBtnWhlWh_onClick();
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC4:  (UIRadBtnSpace.action. --> CheckTypeChooser.uIRadBtnSpace_ActionEvents1()V)
 */
/* 警告：此方法将重新生成。 */
private void connEtoC4() {
	try {
		// user code begin {1}
		// user code end
		this.uIRadBtnSpace_onClick();
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC5:  (UIRadBtnGoods.action. --> CheckTypeChooser.uIRadBtnGoods_ActionEvents1()V)
 */
/* 警告：此方法将重新生成。 */
private void connEtoC5() {
	try {
		// user code begin {1}
		// user code end
		this.uIRadBtnGoods_onClick();
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC6:  (UIRadBtnCyc.action. --> CheckTypeChooser.uIRadBtnCyc_ActionEvents()V)
 */
/* 警告：此方法将重新生成。 */
private void connEtoC6() {
	try {
		// user code begin {1}
		// user code end
		this.uIRadBtnCyc_onClick();
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC7:  (UIRadBtnWhAdmin.action. --> CheckTypeChooser.uIRadBtnWhAdmin_ActionEvents()V)
 */
/* 警告：此方法将重新生成。 */
private void connEtoC7() {
	try {
		// user code begin {1}
		// user code end
		this.uIRadBtnWhAdmin_onClick();
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC8:  (UIRadBtnMinusStore.action. --> CheckTypeChooser.uIRadBtnMinusStore_ActionEvents()V)
 */
/* 警告：此方法将重新生成。 */
private void connEtoC8() {
	try {
		// user code begin {1}
		// user code end
		this.uIRadBtnMinusStore_onClick();
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC9:  (UIRadBtnStatic.action. --> CheckTypeChooser.uIRadBtnStatic_ActionEvents()V)
 */
/* 警告：此方法将重新生成。 */

private void connEtoC9() {
	try {
		// user code begin {1}
		// user code end
		this.uIRadBtnStatic_onClick();
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}

private void connEtoC16() {
	try {
		this.uIRadBtnDynamic_onClick();
	} catch (java.lang.Throwable ivjExc) {
		handleException(ivjExc);
	}
}

/**
 * 返回 BtnChooseGooods 特性值。
 * @return nc.ui.pub.beans.UIButton
 */
/* 警告：此方法将重新生成。 */
private nc.ui.pub.beans.UIButton getBtnChooseGooods() {
	if (ivjBtnChooseGooods == null) {
		try {
			ivjBtnChooseGooods = new nc.ui.pub.beans.UIButton();
			ivjBtnChooseGooods.setName("BtnChooseGooods");
			ivjBtnChooseGooods.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec","UPP4008spec-000153")/*@res "选择存货"*/);
			ivjBtnChooseGooods.setBounds(114, 73, 94, 22);
			ivjBtnChooseGooods.setEnabled(false);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjBtnChooseGooods;
}
/**
 * 返回 BtnChooseSpace 特性值。
 * @return nc.ui.pub.beans.UIButton
 */
/* 警告：此方法将重新生成。 */
private nc.ui.pub.beans.UIButton getBtnChooseSpace() {
	if (ivjBtnChooseSpace == null) {
		try {
			ivjBtnChooseSpace = new nc.ui.pub.beans.UIButton();
			ivjBtnChooseSpace.setName("BtnChooseSpace");
			ivjBtnChooseSpace.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec","UPP4008spec-000154")/*@res "选择货位"*/);
			ivjBtnChooseSpace.setBounds(114, 45, 94, 22);
			ivjBtnChooseSpace.setEnabled(false);
			ivjBtnChooseSpace.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjBtnChooseSpace;
}

/**
 * 返回 BtnChooseSpace 特性值。
 * @return nc.ui.pub.beans.UIButton
 */
/* 警告：此方法将重新生成。 */
private nc.ui.pub.beans.UIButton getBtnChooseMDSpace() {
	if (ivjBtnChooseMDSpace == null) {
		try {
			ivjBtnChooseMDSpace = new nc.ui.pub.beans.UIButton();
			ivjBtnChooseMDSpace.setName("BtnChooseMDSpace");
			ivjBtnChooseMDSpace.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec","UPP4008spec-000154")/*@res "选择货位"*/);
			ivjBtnChooseMDSpace.setBounds(114, 250, 94, 22);
			ivjBtnChooseMDSpace.setEnabled(false);
			ivjBtnChooseMDSpace.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjBtnChooseMDSpace;
}




/**
 * 返回 dlgGoods 特性值。
 * @return nc.ui.ic.ic261.ChooseGoods
 */
/* 警告：此方法将重新生成。 */
private ChooseGoods getdlgGoods() {
	if (ivjdlgGoods == null) {
		try {
//			ivjdlgGoods = new nc.ui.ic.ic261.ChooseGoods();
//			ivjdlgGoods.setName("dlgGoods");
//			ivjdlgGoods.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
			// user code begin {1}
			//ivjdlgGoods= null;
			ivjdlgGoods= new nc.ui.ic.ic261.ChooseGoods(this);
			ivjdlgGoods.setName("dlgGoods");
			ivjdlgGoods.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjdlgGoods;
}
/**
 * 返回 dlgSpace 特性值。
 * @return nc.ui.ic.ic261.ChooseSpace
 */
/* 警告：此方法将重新生成。 */
private ChooseSpace getdlgSpace() {
	if (ivjdlgSpace == null) {
		try {
			ivjdlgSpace = new nc.ui.ic.ic261.ChooseSpace();
			ivjdlgSpace.setName("dlgSpace");
			ivjdlgSpace.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
			// user code begin {1}
			//ivjdlgSpace= null;
			ivjdlgSpace= new nc.ui.ic.ic261.ChooseSpace(this);
			ivjdlgSpace.setName("dlgSpace");
			//ivjdlgSpace.setDefaultCloseOperation(
				//javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjdlgSpace;
}
/**
 * 返回 UIButton2 特性值。
 * @return nc.ui.pub.beans.UIButton
 */
/* 警告：此方法将重新生成。 */
private nc.ui.pub.beans.UIButton getUIBtnCancel() {
	if (ivjUIBtnCancel == null) {
		try {
			ivjUIBtnCancel = new nc.ui.pub.beans.UIButton();
			ivjUIBtnCancel.setName("UIBtnCancel");
			ivjUIBtnCancel.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UC001-0000008")/*@res "取消"*/);
//			ivjUIBtnCancel.setLocation(153, 267);
			ivjUIBtnCancel.setBounds(153,267,70,22);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjUIBtnCancel;
}
/**
 * 返回 UIButton1 特性值。
 * @return nc.ui.pub.beans.UIButton
 */
/* 警告：此方法将重新生成。 */
private nc.ui.pub.beans.UIButton getUIBtnOK() {
	if (ivjUIBtnOK == null) {
		try {
			ivjUIBtnOK = new nc.ui.pub.beans.UIButton();
			ivjUIBtnOK.setName("UIBtnOK");
			ivjUIBtnOK.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UC001-0000044")/*@res "确定"*/);
//			ivjUIBtnOK.setLocation(45, 267);
			ivjUIBtnOK.setBounds(45,267,70,22);
			
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjUIBtnOK;
}
/**
 * 返回 UIChkDataPeriod 特性值。
 * @return nc.ui.pub.beans.UICheckBox
 */
/* 警告：此方法将重新生成。 */
public nc.ui.pub.beans.UICheckBox getUIChkDataPeriod() {
	if (ivjUIChkDataPeriod == null) {
		try {
			ivjUIChkDataPeriod = new nc.ui.pub.beans.UICheckBox();
			ivjUIChkDataPeriod.setName("UIChkDataPeriod");
			ivjUIChkDataPeriod.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec","UPT40081016-000020")/*@res "是否计算期间业务量"*/);
			ivjUIChkDataPeriod.setBounds(6, 57, 144, 22);
			ivjUIChkDataPeriod.setEnabled(false);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjUIChkDataPeriod;
}
/**
 * 返回 UICheckBox4 特性值。
 * @return nc.ui.pub.beans.UICheckBox
 */
/* 警告：此方法将重新生成。 */
private nc.ui.pub.beans.UICheckBox getUIChkDataZero() {
	if (ivjUIChkDataZero == null) {
		try {
			ivjUIChkDataZero = new nc.ui.pub.beans.UICheckBox();
			ivjUIChkDataZero.setName("UIChkDataZero");
			ivjUIChkDataZero.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec","UPP4008spec-000155")/*@res "现存量为零参加盘点"*/);
			ivjUIChkDataZero.setBounds(6, 55, 144, 22);
			ivjUIChkDataZero.setEnabled(false);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjUIChkDataZero;
}
/**
 * 返回 UICheckBox5 特性值。
 * @return nc.ui.pub.beans.UICheckBox
 */
/* 警告：此方法将重新生成。 */
private nc.ui.pub.beans.UICheckBox getUIChkNoOut() {
	if (ivjUIChkNoOut == null) {
		try {
			ivjUIChkNoOut = new nc.ui.pub.beans.UICheckBox();
			ivjUIChkNoOut.setName("UIChkNoOut");
			ivjUIChkNoOut.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec","UPP4008spec-000156")/*@res "n天未出库且现存量为零参加盘点"*/);
			ivjUIChkNoOut.setBounds(6, 82, 215, 22);
			ivjUIChkNoOut.setEnabled(false);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjUIChkNoOut;
}

public nc.ui.pub.beans.UICheckBox getUIChkBCCount() {
	if (ivjUIChkBCCount == null) {
		try {
			ivjUIChkBCCount = new nc.ui.pub.beans.UICheckBox();
			ivjUIChkBCCount.setName("UIChkBCCount");
			ivjUIChkBCCount.setText("是否条码盘点");
			ivjUIChkBCCount.setBounds(6, 28, 144, 22);
			ivjUIChkBCCount.setEnabled(false);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjUIChkBCCount;
}
/**
 * 返回 UIDialogContentPane 特性值。
 * @return javax.swing.JPanel
 */
/* 警告：此方法将重新生成。 */
private javax.swing.JPanel getUIDialogContentPane() {
	if (ivjUIDialogContentPane == null) {
		try {
			ivjUIDialogContentPane = new javax.swing.JPanel();
			ivjUIDialogContentPane.setName("UIDialogContentPane");
			ivjUIDialogContentPane.setLayout(null);
			getUIDialogContentPane().add(getUILabelWarehouseName(), getUILabelWarehouseName().getName());
			getUIDialogContentPane().add(getUIBtnOK(), getUIBtnOK().getName());
			getUIDialogContentPane().add(getUIBtnCancel(), getUIBtnCancel().getName());
			getUIDialogContentPane().add(getUIRefPane1(), getUIRefPane1().getName());
			getUIDialogContentPane().add(getUIPanelCheckType(), getUIPanelCheckType().getName());
			getUIDialogContentPane().add(getUIPanelParam(), getUIPanelParam().getName());
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
 * 返回 UILabel3 特性值。
 * @return nc.ui.pub.beans.UILabel
 */
/* 警告：此方法将重新生成。 */
private nc.ui.pub.beans.UILabel getUILabel3() {
	if (ivjUILabel3 == null) {
		try {
			ivjUILabel3 = new nc.ui.pub.beans.UILabel();
			ivjUILabel3.setName("UILabel3");
			ivjUILabel3.setFont(new java.awt.Font("dialog", 1, 12));
			ivjUILabel3.setText("n=");
			ivjUILabel3.setBounds(52, 103, 21, 22);
			ivjUILabel3.setEnabled(false);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjUILabel3;
}
/**
 * 返回 UILabel31 特性值。
 * @return nc.ui.pub.beans.UILabel
 */
/* 警告：此方法将重新生成。 */
private nc.ui.pub.beans.UILabel getUILabel31() {
	if (ivjUILabel31 == null) {
		try {
			ivjUILabel31 = new nc.ui.pub.beans.UILabel();
			ivjUILabel31.setName("UILabel31");
			ivjUILabel31.setFont(new java.awt.Font("dialog", 1, 12));
			ivjUILabel31.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec","UPP4008spec-000157")/*@res "天"*/);
			ivjUILabel31.setBounds(143, 105, 21, 22);
			ivjUILabel31.setEnabled(false);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjUILabel31;
}
/**
 * 返回 UILabel311 特性值。
 * @return nc.ui.pub.beans.UILabel
 */
/* 警告：此方法将重新生成。 */
private nc.ui.pub.beans.UILabel getUILabel311() {
	if (ivjUILabel311 == null) {
		try {
			ivjUILabel311 = new nc.ui.pub.beans.UILabel();
			ivjUILabel311.setName("UILabel311");
			ivjUILabel311.setFont(new java.awt.Font("dialog", 1, 12));
			ivjUILabel311.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec","UPP4008spec-000157")/*@res "天"*/);
			ivjUILabel311.setBounds(201, 177, 21, 22);
			ivjUILabel311.setEnabled(false);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjUILabel311;
}
/**
 * 返回 UILabel3111 特性值。
 * @return nc.ui.pub.beans.UILabel
 */
/* 警告：此方法将重新生成。 */
private nc.ui.pub.beans.UILabel getUILabel3111() {
	if (ivjUILabel3111 == null) {
		try {
			ivjUILabel3111 = new nc.ui.pub.beans.UILabel();
			ivjUILabel3111.setName("UILabel3111");
			ivjUILabel3111.setFont(new java.awt.Font("dialog", 1, 12));
			ivjUILabel3111.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec","UPP4008spec-000157")/*@res "天"*/);
			ivjUILabel3111.setBounds(201, 203, 21, 22);
			ivjUILabel3111.setEnabled(false);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjUILabel3111;
}
/**
 * 返回 UILabel4 特性值。
 * @return nc.ui.pub.beans.UILabel
 */
/* 警告：此方法将重新生成。 */
private nc.ui.pub.beans.UILabel getUILabel4() {
	if (ivjUILabel4 == null) {
		try {
			ivjUILabel4 = new nc.ui.pub.beans.UILabel();
			ivjUILabel4.setName("UILabel4");
			ivjUILabel4.setText("n=");
			ivjUILabel4.setBounds(114, 177, 21, 22);
			ivjUILabel4.setEnabled(false);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjUILabel4;
}
/**
 * 返回 UILabel5 特性值。
 * @return nc.ui.pub.beans.UILabel
 */
/* 警告：此方法将重新生成。 */
private nc.ui.pub.beans.UILabel getUILabel5() {
	if (ivjUILabel5 == null) {
		try {
			ivjUILabel5 = new nc.ui.pub.beans.UILabel();
			ivjUILabel5.setName("UILabel5");
			ivjUILabel5.setText("n=");
			ivjUILabel5.setBounds(114, 203, 18, 22);
			ivjUILabel5.setEnabled(false);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjUILabel5;
}
/**
 * 返回 UILabelWarehouseName 特性值。
 * @return nc.ui.pub.beans.UILabel
 */
/* 警告：此方法将重新生成。 */
private nc.ui.pub.beans.UILabel getUILabelWarehouseName() {
	if (ivjUILabelWarehouseName == null) {
		try {
			ivjUILabelWarehouseName = new nc.ui.pub.beans.UILabel();
			ivjUILabelWarehouseName.setName("UILabelWarehouseName");
			ivjUILabelWarehouseName.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec","UPP4008spec-000158")/*@res "盘点仓库"*/);
			ivjUILabelWarehouseName.setBounds(36, 21, 60, 20);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjUILabelWarehouseName;
}
/**
 * 返回 UIPanelCheckType 特性值。
 * @return nc.ui.pub.beans.UIPanel
 */
/* 警告：此方法将重新生成。 */
private nc.ui.pub.beans.UIPanel getUIPanelCheckType() {
	if (ivjUIPanelCheckType == null) {
		try {
			ivjUIPanelCheckType = new nc.ui.pub.beans.UIPanel();
			ivjUIPanelCheckType.setName("UIPanelCheckType");
			ivjUIPanelCheckType.setLayout(null);
			ivjUIPanelCheckType.setBounds(270, 19, 235, 277);
			ivjUIPanelCheckType.setEnabled(true);
			getUIPanelCheckType().add(getUIRadBtnWhlWh(), getUIRadBtnWhlWh().getName());
			getUIPanelCheckType().add(getBtnChooseSpace(), getBtnChooseSpace().getName());
			getUIPanelCheckType().add(getUIRefPKeeper(), getUIRefPKeeper().getName());
			getUIPanelCheckType().add(getUITxtFldNActDay(), getUITxtFldNActDay().getName());
			getUIPanelCheckType().add(getUILabel5(), getUILabel5().getName());
			getUIPanelCheckType().add(getUITxtFldDateDay(), getUITxtFldDateDay().getName());
			getUIPanelCheckType().add(getBtnChooseGooods(), getBtnChooseGooods().getName());
			
			/**码单盘点*/
			getUIPanelCheckType().add(getUIRadBtnMD(), getUIRadBtnMD().getName());
			getUIPanelCheckType().add(getBtnChooseMDSpace(), getBtnChooseMDSpace().getName());
			/**码单盘点*/
			
			getUIPanelCheckType().add(getUIRadBtnSpace(), getUIRadBtnSpace().getName());
			getUIPanelCheckType().add(getUIRadBtnGoods(), getUIRadBtnGoods().getName());
			getUIPanelCheckType().add(getUIRadBtnCyc(), getUIRadBtnCyc().getName());
			getUIPanelCheckType().add(getUIRadBtnWhAdmin(), getUIRadBtnWhAdmin().getName());
			getUIPanelCheckType().add(getUIRadBtnMinusStore(), getUIRadBtnMinusStore().getName());
			getUIPanelCheckType().add(getUIRadBtnStatic(), getUIRadBtnStatic().getName());
			getUIPanelCheckType().add(getUIRadBtnDate(), getUIRadBtnDate().getName());
			getUIPanelCheckType().add(getUILabel4(), getUILabel4().getName());
			getUIPanelCheckType().add(getUILabel311(), getUILabel311().getName());
			getUIPanelCheckType().add(getUILabel3111(), getUILabel3111().getName());

			getUIPanelCheckType().add(getUIRadBtnDynamic(), getUIRadBtnDynamic().getName());
			getUIPanelCheckType().add(getUILabel51(), getUILabel51().getName());
			getUIPanelCheckType().add(getUITxtFldDynamic(), getUITxtFldDynamic().getName());
			getUIPanelCheckType().add(getUILabel31111(), getUILabel31111().getName());
			// user code begin {1}
			ivjUIPanelCheckType.setBorder(javax.swing.BorderFactory.createTitledBorder(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec","UPT40081016-000016")/*@res "盘点方式"*/));
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjUIPanelCheckType;
}
/**
 * 返回 UIPanelParam 特性值。
 * @return nc.ui.pub.beans.UIPanel
 */
/* 警告：此方法将重新生成。 */
private nc.ui.pub.beans.UIPanel getUIPanelParam() {
	if (ivjUIPanelParam == null) {
		try {
			ivjUIPanelParam = new nc.ui.pub.beans.UIPanel();
			ivjUIPanelParam.setName("UIPanelParam");
			ivjUIPanelParam.setBorder(new javax.swing.border.CompoundBorder());
			ivjUIPanelParam.setLayout(null);
			ivjUIPanelParam.setBounds(18, 69, 230, 156);
			ivjUIPanelParam.setEnabled(true);
			getUIPanelParam().add(getUIChkBCCount(), getUIChkBCCount().getName());
			getUIPanelParam().add(getUIChkDataZero(), getUIChkDataZero().getName());
			getUIPanelParam().add(getUIChkNoOut(), getUIChkNoOut().getName());		
			getUIPanelParam().add(getUILabel3(), getUILabel3().getName());
			getUIPanelParam().add(getUITxtFldDayNum(), getUITxtFldDayNum().getName());
			getUIPanelParam().add(getUILabel31(), getUILabel31().getName());
			//getUIPanelParam().add(getUIChkDataPeriod(), getUIChkDataPeriod().getName());
			// user code begin {1}
			ivjUIPanelParam.setBorder(javax.swing.BorderFactory.createTitledBorder(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec","UPP4008spec-000159")/*@res "参数"*/));
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjUIPanelParam;
}
/**
 * 返回 UIRadioButton4 特性值。
 * @return nc.ui.pub.beans.UIRadioButton
 */
/* 警告：此方法将重新生成。 */
private nc.ui.pub.beans.UIRadioButton getUIRadBtnCyc() {
	if (ivjUIRadBtnCyc == null) {
		try {
			ivjUIRadBtnCyc = new nc.ui.pub.beans.UIRadioButton();
			ivjUIRadBtnCyc.setName("UIRadBtnCyc");
			ivjUIRadBtnCyc.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec","UPP4008spec-000160")/*@res "周期盘点"*/);
			ivjUIRadBtnCyc.setContentAreaFilled(false);
			ivjUIRadBtnCyc.setBounds(12, 101, 78, 22);
			ivjUIRadBtnCyc.setEnabled(false);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjUIRadBtnCyc;
}
/**
 * 返回 UIRadioButton8 特性值。
 * @return nc.ui.pub.beans.UIRadioButton
 */
/* 警告：此方法将重新生成。 */
private nc.ui.pub.beans.UIRadioButton getUIRadBtnDate() {
	if (ivjUIRadBtnDate == null) {
		try {
			ivjUIRadBtnDate = new nc.ui.pub.beans.UIRadioButton();
			ivjUIRadBtnDate.setName("UIRadBtnDate");
			ivjUIRadBtnDate.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec","UPP4008spec-000161")/*@res "保质期盘点"*/);
			ivjUIRadBtnDate.setContentAreaFilled(false);
			ivjUIRadBtnDate.setBounds(12, 205, 100, 22);
			ivjUIRadBtnDate.setEnabled(false);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjUIRadBtnDate;
}
/**
 * 返回 UIRadioButton3 特性值。
 * @return nc.ui.pub.beans.UIRadioButton
 */
/* 警告：此方法将重新生成。 */
private nc.ui.pub.beans.UIRadioButton getUIRadBtnGoods() {
	if (ivjUIRadBtnGoods == null) {
		try {
			ivjUIRadBtnGoods = new nc.ui.pub.beans.UIRadioButton();
			ivjUIRadBtnGoods.setName("UIRadBtnGoods");
			ivjUIRadBtnGoods.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec","UPP4008spec-000162")/*@res "存货盘点"*/);
			ivjUIRadBtnGoods.setContentAreaFilled(false);
			ivjUIRadBtnGoods.setBounds(12, 73, 78, 22);
			ivjUIRadBtnGoods.setEnabled(false);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjUIRadBtnGoods;
}
/**
 * 返回 UIRadioButton6 特性值。
 * @return nc.ui.pub.beans.UIRadioButton
 */
/* 警告：此方法将重新生成。 */
private nc.ui.pub.beans.UIRadioButton getUIRadBtnMinusStore() {
	if (ivjUIRadBtnMinusStore == null) {
		try {
			ivjUIRadBtnMinusStore = new nc.ui.pub.beans.UIRadioButton();
			ivjUIRadBtnMinusStore.setName("UIRadBtnMinusStore");
			ivjUIRadBtnMinusStore.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec","UPP4008spec-000163")/*@res "负库存盘点"*/);
			ivjUIRadBtnMinusStore.setContentAreaFilled(false);
			ivjUIRadBtnMinusStore.setBounds(12, 149, 100, 22);
			ivjUIRadBtnMinusStore.setEnabled(false);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjUIRadBtnMinusStore;
}
/**
 * 返回 UIRadioButton21 特性值。
 * @return nc.ui.pub.beans.UIRadioButton
 */
/* 警告：此方法将重新生成。 */
private nc.ui.pub.beans.UIRadioButton getUIRadBtnSpace() {
	if (ivjUIRadBtnSpace == null) {
		try {
			ivjUIRadBtnSpace = new nc.ui.pub.beans.UIRadioButton();
			ivjUIRadBtnSpace.setName("UIRadBtnSpace");
			ivjUIRadBtnSpace.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec","UPP4008spec-000164")/*@res "货位盘点"*/);
			ivjUIRadBtnSpace.setContentAreaFilled(false);
			ivjUIRadBtnSpace.setBounds(12, 45, 75, 22);
			ivjUIRadBtnSpace.setEnabled(false);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjUIRadBtnSpace;
}

/**
 * 返回 UIRadioButton21 特性值。
 * @return nc.ui.pub.beans.UIRadioButton
 */
/* 警告：此方法将重新生成。 */
private nc.ui.pub.beans.UIRadioButton getUIRadBtnMD() {
	if (ivjUIRadBtnMD == null) {
		try {
			ivjUIRadBtnMD = new nc.ui.pub.beans.UIRadioButton();
			ivjUIRadBtnMD.setName("UIRadBtnMD");
			ivjUIRadBtnMD.setText("码单盘点");
			ivjUIRadBtnMD.setContentAreaFilled(false);
			ivjUIRadBtnMD.setBounds(12, 250, 75, 22);
			ivjUIRadBtnMD.setEnabled(false);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjUIRadBtnMD;
}




/**
 * 返回 UIRadioButton7 特性值。
 * @return nc.ui.pub.beans.UIRadioButton
 */
/* 警告：此方法将重新生成。 */
private nc.ui.pub.beans.UIRadioButton getUIRadBtnStatic() {
	if (ivjUIRadBtnStatic == null) {
		try {
			ivjUIRadBtnStatic = new nc.ui.pub.beans.UIRadioButton();
			ivjUIRadBtnStatic.setName("UIRadBtnStatic");
			ivjUIRadBtnStatic.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec","UPP4008spec-000165")/*@res "无动态盘点"*/);
			ivjUIRadBtnStatic.setContentAreaFilled(false);
			ivjUIRadBtnStatic.setBounds(12, 177, 100, 22);
			ivjUIRadBtnStatic.setEnabled(false);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjUIRadBtnStatic;
}
/**
 * 返回 UIRadioButton5 特性值。
 * @return nc.ui.pub.beans.UIRadioButton
 */
/* 警告：此方法将重新生成。 */
private nc.ui.pub.beans.UIRadioButton getUIRadBtnWhAdmin() {
	if (ivjUIRadBtnWhAdmin == null) {
		try {
			ivjUIRadBtnWhAdmin = new nc.ui.pub.beans.UIRadioButton();
			ivjUIRadBtnWhAdmin.setName("UIRadBtnWhAdmin");
			ivjUIRadBtnWhAdmin.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec","UPP4008spec-000166")/*@res "保管员盘点"*/);
			ivjUIRadBtnWhAdmin.setContentAreaFilled(false);
			ivjUIRadBtnWhAdmin.setBounds(12, 124, 101, 22);
			ivjUIRadBtnWhAdmin.setEnabled(false);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjUIRadBtnWhAdmin;
}
/**
 * 返回 UIRadioButton11 特性值。
 * @return nc.ui.pub.beans.UIRadioButton
 */
/* 警告：此方法将重新生成。 */
private nc.ui.pub.beans.UIRadioButton getUIRadBtnWhlWh() {
	if (ivjUIRadBtnWhlWh == null) {
		try {
			ivjUIRadBtnWhlWh = new nc.ui.pub.beans.UIRadioButton();
			ivjUIRadBtnWhlWh.setName("UIRadBtnWhlWh");
			ivjUIRadBtnWhlWh.setSelected(true);
			ivjUIRadBtnWhlWh.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec","UPP4008spec-000167")/*@res "整仓盘点"*/);
			ivjUIRadBtnWhlWh.setContentAreaFilled(false);
			ivjUIRadBtnWhlWh.setBounds(12, 17, 100, 22);
			ivjUIRadBtnWhlWh.setEnabled(false);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjUIRadBtnWhlWh;
}
/**
 * 返回 UIRefPane1 特性值。
 * @return nc.ui.pub.beans.UIRefPane
 */
/* 警告：此方法将重新生成。 */
private nc.ui.pub.beans.UIRefPane getUIRefPane1() {
	if (ivjUIRefPane1 == null) {
		try {
			ivjUIRefPane1 = new nc.ui.pub.beans.UIRefPane();
			ivjUIRefPane1.setName("UIRefPane1");
			ivjUIRefPane1.setBounds(110, 21, 80, 20);
			ivjUIRefPane1.setRefNodeName("仓库档案");
			ivjUIRefPane1.setReturnCode(false);
			// user code begin {1}
			ivjUIRefPane1.setButtonFireEvent(true);
			ivjUIRefPane1.addValueChangedListener(this);
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjUIRefPane1;
}
/**
 * 返回 UIRefPKeeper 特性值。
 * @return nc.ui.pub.beans.UIRefPane
 */
/* 警告：此方法将重新生成。 */
private nc.ui.pub.beans.UIRefPane getUIRefPKeeper() {
	if (ivjUIRefPKeeper == null) {
		try {
			ivjUIRefPKeeper = new nc.ui.pub.beans.UIRefPane();
			ivjUIRefPKeeper.setName("UIRefPKeeper");
			ivjUIRefPKeeper.setBounds(114, 124, 94, 18);
			ivjUIRefPKeeper.setRefNodeName("人员档案");
			ivjUIRefPKeeper.setEnabled(false);
			ivjUIRefPKeeper.setRefInputType(1/** 名称*/);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjUIRefPKeeper;
}
/**
 * 返回 UITxtFldDateDay 特性值。
 * @return nc.ui.pub.beans.UITextField
 */
/* 警告：此方法将重新生成。 */
private nc.ui.pub.beans.UITextField getUITxtFldDateDay() {
	if (ivjUITxtFldDateDay == null) {
		try {
			ivjUITxtFldDateDay = new nc.ui.pub.beans.UITextField();
			ivjUITxtFldDateDay.setName("UITxtFldDateDay");
			ivjUITxtFldDateDay.setBounds(129, 203, 65, 20);
			ivjUITxtFldDateDay.setEnabled(false);
			ivjUITxtFldDateDay.setTextType("TextInt");
			ivjUITxtFldDateDay.setMinValue(0);
			ivjUITxtFldDateDay.setMaxLength(4);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjUITxtFldDateDay;
}
/**
 * 返回 UITextField1 特性值。
 * @return nc.ui.pub.beans.UITextField
 */
/* 警告：此方法将重新生成。 */
private nc.ui.pub.beans.UITextField getUITxtFldDayNum() {
	if (ivjUITxtFldDayNum == null) {
		try {
			ivjUITxtFldDayNum = new nc.ui.pub.beans.UITextField();
			ivjUITxtFldDayNum.setName("UITxtFldDayNum");
			ivjUITxtFldDayNum.setText("30");
			ivjUITxtFldDayNum.setBounds(72, 103, 64, 20);
			ivjUITxtFldDayNum.setEnabled(false);
			ivjUITxtFldDayNum.setMinValue(0);
			ivjUITxtFldDayNum.setMaxLength(4);
			ivjUITxtFldDayNum.setTextType("TextInt");

			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjUITxtFldDayNum;
}
/**
 * 返回 UITxtFldNActDay 特性值。
 * @return nc.ui.pub.beans.UITextField
 */
/* 警告：此方法将重新生成。 */
private nc.ui.pub.beans.UITextField getUITxtFldNActDay() {
	if (ivjUITxtFldNActDay == null) {
		try {
			ivjUITxtFldNActDay = new nc.ui.pub.beans.UITextField();
			ivjUITxtFldNActDay.setName("UITxtFldNActDay");
			ivjUITxtFldNActDay.setBounds(129, 177, 65, 20);
			ivjUITxtFldNActDay.setEnabled(false);
			ivjUITxtFldNActDay.setTextType("TextInt");

			ivjUITxtFldNActDay.setMinValue(0);
			ivjUITxtFldNActDay.setMaxLength(4);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjUITxtFldNActDay;
}
/**
 * Comment
 */
public ArrayList getvos() {
	return m_alReturnInfo;
}
/**
 * 每当部件抛出异常时被调用
 * @param exception java.lang.Throwable
 */
private void handleException(java.lang.Throwable exception) {

	/* 除去下列各行的注释，以将未捕捉到的异常打印至 stdout。 */
	// nc.vo.scm.pub.SCMEnv.out("--------- 未捕捉到的异常 ---------");
	// nc.vo.scm.pub.SCMEnv.error(exception);
}
/**
 * 初始化连接
 * @exception java.lang.Exception 异常说明。
 */
/* 警告：此方法将重新生成。 */
private void initConnections() throws java.lang.Exception {
	// user code begin {1}
	// user code end
	getUIBtnOK().addActionListener(ivjEventHandler);
	getUIBtnCancel().addActionListener(ivjEventHandler);
	getUIRadBtnWhlWh().addActionListener(ivjEventHandler);
	getUIRadBtnSpace().addActionListener(ivjEventHandler);
	
	/**ADD BY OUYANGZHB 2012-04-12 码单盘点按钮*/
	getUIRadBtnMD().addActionListener(ivjEventHandler);
	getBtnChooseMDSpace().addActionListener(ivjEventHandler);
	/**add end */
	getUIRadBtnGoods().addActionListener(ivjEventHandler);
	getUIRadBtnCyc().addActionListener(ivjEventHandler);
	getUIRadBtnWhAdmin().addActionListener(ivjEventHandler);
	getUIRadBtnMinusStore().addActionListener(ivjEventHandler);
	getUIRadBtnStatic().addActionListener(ivjEventHandler);
	getUIRadBtnDate().addActionListener(ivjEventHandler);
	getBtnChooseSpace().addActionListener(ivjEventHandler);
	getBtnChooseGooods().addActionListener(ivjEventHandler);
	getUIRefPane1().addValueChangedListener(ivjEventHandler);
	getUIChkDataZero().addActionListener(ivjEventHandler);
	getUIChkNoOut().addActionListener(ivjEventHandler);
	getUIChkBCCount().addActionListener(ivjEventHandler);
	
	getUIRadBtnDynamic().addActionListener(ivjEventHandler);
}
/**
 * 初始化类。
 */
/* 警告：此方法将重新生成。 */
private void initialize() {
	try {
		// user code begin {1}
		group.add(getUIRadBtnCyc());
		group.add(getUIRadBtnDate());
		group.add(getUIRadBtnGoods());
		group.add(getUIRadBtnMinusStore());
		group.add(getUIRadBtnMD());
		group.add(getUIRadBtnSpace());
		group.add(getUIRadBtnStatic());
		group.add(getUIRadBtnWhAdmin());
		group.add(getUIRadBtnWhlWh());
		group.add(getUIRadBtnDynamic());
		// user code end
		setName("CheckTypeChooser");
		setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		setSize(530, 344);
		setTitle(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec","UPP4008spec-000168")/*@res "请选择盘点方式"*/);
		setContentPane(getUIDialogContentPane());
		initConnections();
	} catch (java.lang.Throwable ivjExc) {
		handleException(ivjExc);
	}
	// user code begin {2}
	// user code end
}
/**
 * 主入口点 - 当部件作为应用程序运行时，启动这个部件。
 * @param args java.lang.String[]
 */
public static void main(java.lang.String[] args) {
	try {
		CheckTypeChooser aCheckTypeChooser;
		aCheckTypeChooser = new CheckTypeChooser();
		aCheckTypeChooser.setModal(true);
		aCheckTypeChooser.addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosing(java.awt.event.WindowEvent e) {
				System.exit(0);
			};
		});
		//aCheckTypeChooser.show();
		java.awt.Insets insets = aCheckTypeChooser.getInsets();
		aCheckTypeChooser.setSize(aCheckTypeChooser.getWidth() + insets.left + insets.right, aCheckTypeChooser.getHeight() + insets.top + insets.bottom);
		aCheckTypeChooser.setVisible(true);
	} catch (Throwable exception) {
		nc.vo.scm.pub.SCMEnv.out("nc.ui.pub.beans.UIDialog 的 main() 中发生异常");
		nc.vo.scm.pub.SCMEnv.error(exception);
	}
}
/**
 * Comment
 */
public void onCancel(java.awt.event.ActionEvent actionEvent) {
	this.closeCancel();
}
/**
 	radFlag=CheckMode.WholeWh;盘点类型
	param1Flag=CheckMode.UnCheck;账面为零参加盘点选中标志
	param2Flag=CheckMode.UnCheck;n天未出库且账面为零参加盘点选中标志
	param2DayNum=0;n天未出库且账面为零参加盘点天数
	dateDayNum=0;保质期盘点天数
	nActdayNum=0;无动态盘点天数
	keeperID=null;库管员id

	dlgGoods=new ChooseGoods();选择存货
	dlgSpace=new ChooseSpace();选择货位

	//不再使用
	freeItemFlag=CheckMode.UnCheck; 自由项选中标志
 	pcFlag=CheckMode.UnCheck;批次选中标志
	spaceFlag=CheckMode.UnCheck;货位选中标志
	修改人：刘家清 修改日期：2007-10-23上午10:37:42 修改原因：为了后台查询时把业务逻辑中原先系统日期改为登录日期，在List中加入登录日期
 * @throws Exception 
 */
public void onOK(java.awt.event.ActionEvent actionEvent) throws Exception {
	m_alReturnInfo = new ArrayList();
	if ((null == getUIRefPane1().getRefPK())
		|| (getUIRefPane1().getRefPK().toString().trim().length() == 0)) {
		nc.ui.pub.beans.MessageDialog.showWarningDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec","UPP4008spec-000151")/*@res "警告！"*/, nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec","UPP4008spec-000169")/*@res "请输入仓库！"*/);
		return;
	}
	m_alReturnInfo.add(getUIRefPane1().getRefPK());
	//return
	ArrayList alReturnInfoRow1 = new ArrayList();
	alReturnInfoRow1.add(new Integer(radFlag));
	String tempStr = null;
	//加入对应的天数
	ArrayList alDay = new ArrayList();

	switch (radFlag) {
		case CheckMode.WholeWh :
			if (m_bIsSpcaceMgt) {
				nc.ui.pub.beans.MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec","UPPSCMCommon-000059")/*@res "错误"*/, nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec","UPP4008spec-000170")/*@res "请选择货位盘点！"*/);
				return;
			}
			m_alReturnInfo.add(alReturnInfoRow1);
			break;
		case CheckMode.Circle :
			m_alReturnInfo.add(alReturnInfoRow1);
			m_alReturnInfo.add(m_sStartDate);
			m_alReturnInfo.add(getLogDate());
			break;
		case CheckMode.Goods :
			//加入对应的存货
			ArrayList alInvs = getdlgGoods().getvos();
			if ((alInvs == null) || (alInvs.size() == 0)) {
				nc.ui.pub.beans.MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec","UPPSCMCommon-000059")/*@res "错误"*/, nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec","UPP4008spec-000171")/*@res "未选存货或存货类！"*/);
				return;
			}
			alReturnInfoRow1.add(alInvs);
			m_alReturnInfo.add(alReturnInfoRow1);
			break;
		case CheckMode.Keeper :
			//加入对应的库管员
			ArrayList alManager = new ArrayList();
			alManager.add(getUIRefPKeeper().getRefPK());
			if ((getUIRefPKeeper().getRefPK() == null)
				|| (getUIRefPKeeper().getRefPK().trim().length() == 0)) {
				nc.ui.pub.beans.MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec","UPPSCMCommon-000059")/*@res "错误"*/, nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec","UPP4008spec-000172")/*@res "未选库管员！"*/);
				return;
			}
			alReturnInfoRow1.add(alManager);
			m_alReturnInfo.add(alReturnInfoRow1);
			break;
		case CheckMode.Minus :
			m_alReturnInfo.add(alReturnInfoRow1);
			break;
		case CheckMode.NActive :
			tempStr = getUITxtFldNActDay().getText();
			if (tempStr != null && tempStr.trim().length() > 0)
				nActdayNum = Integer.parseInt(tempStr.trim());
			else {
				nc.ui.pub.beans.MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec","UPPSCMCommon-000059")/*@res "错误"*/, nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec","UPP4008spec-000173")/*@res "未输入无动态盘点天数！"*/);
				return;
			}
			alDay.add(new Integer(nActdayNum));
			alReturnInfoRow1.add(alDay);
			m_alReturnInfo.add(alReturnInfoRow1);
			m_alReturnInfo.add(getLogDate());
			break;
		//动态盘点
		case CheckMode.Dynamic :
			tempStr = getUITxtFldDynamic().getText();
			if (tempStr != null && tempStr.trim().length() > 0)
				nActdayNum = Integer.parseInt(tempStr.trim());
			else {
				nc.ui.pub.beans.MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec","UPPSCMCommon-000059")/*@res "错误"*/, nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec","UPP4008spec-000546")/*@res "未输入动态盘点天数！"*/);
				return;
			}
			alDay.add(new Integer(nActdayNum));
			alReturnInfoRow1.add(alDay);
			m_alReturnInfo.add(alReturnInfoRow1);
			m_alReturnInfo.add(getLogDate());
			break;

		case CheckMode.Space :
			if (!m_bIsSpcaceMgt) {
				nc.ui.pub.beans.MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec","UPPSCMCommon-000059")/*@res "错误"*/, nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec","UPP4008spec-000174")/*@res "非货位管理仓库！"*/);
				return;
			}
			//加入对应的货位
			ArrayList alSpaces = getdlgSpace().getvos();
			if ((alSpaces == null) || (alSpaces.size() == 0)) {
				nc.ui.pub.beans.MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec","UPPSCMCommon-000059")/*@res "错误"*/, nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec","UPP4008spec-000175")/*@res "未输入货位！"*/);
				return;
			}
			alReturnInfoRow1.add(alSpaces);
			m_alReturnInfo.add(alReturnInfoRow1);
			break;
		case CheckMode.Term :
			tempStr = getUITxtFldDateDay().getText();
			if (tempStr != null && tempStr.trim().length() > 0)
				dateDayNum = Integer.parseInt(tempStr.trim());
			else {
				nc.ui.pub.beans.MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec","UPPSCMCommon-000059")/*@res "错误"*/, nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec","UPP4008spec-000176")/*@res "未输入保质期天数！"*/);
				return;
			}
			alDay.add(new Integer(dateDayNum));
			alReturnInfoRow1.add(alDay);
			m_alReturnInfo.add(alReturnInfoRow1);
			m_alReturnInfo.add(getLogDate());
			break;
			
			/**add by ouyangzhb 2012-04-12 码单盘点*/
		case CheckMode.md :
			if (!m_bIsSpcaceMgt) {
				nc.ui.pub.beans.MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec","UPPSCMCommon-000059")/*@res "错误"*/, nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec","UPP4008spec-000174")/*@res "非货位管理仓库！"*/);
				return;
			}
			//加入对应的货位
			ArrayList almdSpaces = getdlgSpace().getvos();
			if ((almdSpaces == null) || (almdSpaces.size() == 0)) {
				nc.ui.pub.beans.MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec","UPPSCMCommon-000059")/*@res "错误"*/, nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec","UPP4008spec-000175")/*@res "未输入货位！"*/);
				return;
			}
			alReturnInfoRow1.add(almdSpaces);
			m_alReturnInfo.add(alReturnInfoRow1);
			break;
			
	}
	//if (getUIChkFreeItem().isSelected())
	//freeItemFlag=CheckMode.Check;
	//else
	//freeItemFlag=CheckMode.UnCheck;

	//if (getUIChkPc().isSelected())
	//pcFlag=CheckMode.Check;
	//else
	//pcFlag=CheckMode.UnCheck;

	//if (getUIChkSpace().isSelected())
	//spaceFlag=CheckMode.Check;
	//else
	//spaceFlag=CheckMode.UnCheck;

	if (getUIChkDataZero().isSelected()) {
		ArrayList alReturnInfoRow2 = new ArrayList();
		alReturnInfoRow2.add(new Integer(1));
		m_alReturnInfo.add(2, alReturnInfoRow2);

	} else {
		if (getUIChkNoOut().isSelected()) {
			tempStr = getUITxtFldDayNum().getText();
			if (tempStr != null && tempStr.trim().length() > 0)
				dateDayNum = Integer.parseInt(tempStr.trim());
			else {
				nc.ui.pub.beans.MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec","UPPSCMCommon-000059")/*@res "错误"*/, nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec","UPP4008spec-000177")/*@res "未输入天数！"*/);
				return;
			}
			ArrayList alReturnInfoRow2 = new ArrayList();
			alReturnInfoRow2.add(new Integer(2));
			alReturnInfoRow2.add(new Integer(dateDayNum));
			m_alReturnInfo.add(2, alReturnInfoRow2);
		} else {
			ArrayList alReturnInfoRow2 = new ArrayList();
			alReturnInfoRow2.add(new Integer(0));
			m_alReturnInfo.add(2, alReturnInfoRow2);
		}
	}
	
	if (getUIChkBCCount().isSelected()){
		nc.vo.scm.ic.bill.WhVO voWh =
			(nc.vo.scm.ic.bill.WhVO) SpecialBillHelper.queryInfo(new Integer(1), getUIRefPane1().getRefPK());
		//货位管理仓库，按钮可用控制
		m_bIsSpcaceMgt = (voWh.getIsLocatorMgt().intValue() == 1);
		if (m_bIsSpcaceMgt) {
			nc.ui.pub.beans.MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec","UPPSCMCommon-000059")/*@res "错误"*/, nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec","UPP4008spec-000579")/*@res "货位管理仓库不支持条码盘点"*/);
			return;
		}
			
		m_alReturnInfo.add(3,UFBoolean.TRUE);
		
	}
	else
		m_alReturnInfo.add(3,UFBoolean.FALSE);
	
	this.closeOK();
}
/**
 * 创建者：顾焱
 * 功能：
 * 参数：
 * 返回：
 * 例外：
 * 日期：(2001-6-2 16:45:45)
 * 修改日期，修改人，修改原因，注释标志：
 *
 */
public void resetCompnent(boolean enableFlag) {
	getUIChkDataZero().setEnabled(enableFlag);
	getUIChkDataPeriod().setEnabled(enableFlag);
	//getUIChkFreeItem().setEnabled(enableFlag);
	getUIChkNoOut().setEnabled(enableFlag);
	getUIChkBCCount().setEnabled(enableFlag);
	//getUIChkPc().setEnabled(enableFlag);
	//getUIChkSpace().setEnabled(enableFlag);
	getUIRadBtnCyc().setEnabled(enableFlag);
	getUIRadBtnDate().setEnabled(enableFlag);
	getUIRadBtnGoods().setEnabled(enableFlag);
	getUIRadBtnMinusStore().setEnabled(enableFlag);
	getUIRadBtnSpace().setEnabled(enableFlag);
	getUIRadBtnStatic().setEnabled(enableFlag);
	//dynamic pandian
	getUIRadBtnDynamic().setEnabled(enableFlag);

	getUIRadBtnWhAdmin().setEnabled(enableFlag);
	getUIRadBtnWhlWh().setEnabled(enableFlag);
	getUITxtFldDayNum().setEnabled(enableFlag);


	getUILabel3().setEnabled(enableFlag);
	getUILabel4().setEnabled(enableFlag);
	getUILabel5().setEnabled(enableFlag);
	
	//add by ouyangzhb 2012-04-12
	getUIRadBtnMD().setEnabled(enableFlag);
}
/**
 * 创建者：顾焱
 * 功能：
 * 参数：
 * 返回：
 * 例外：
 * 日期：(2001-5-22 10:53:53)
 * 修改日期，修改人，修改原因，注释标志：
 *
 */
private void setChooseState() {
	getBtnChooseGooods().setEnabled(false);
	getBtnChooseSpace().setEnabled(false);
	//ADD BY ouyangzhb 2012-04-12 
	getBtnChooseMDSpace().setEnabled(false);
	getUITxtFldDateDay().setEnabled(false);
	getUITxtFldNActDay().setEnabled(false);
	getUIRefPKeeper().setEnabled(false);
	getUITxtFldDynamic().setEnabled(false);
	
}

public void setUserID(String newUserID){
	m_sUserID=newUserID;
}

/**
 * 创建者：顾焱
 * 功能：
 * 参数：
 * 返回：
 * 例外：
 * 日期：(2001-5-22 10:53:53)
 * 修改日期，修改人，修改原因，注释标志：
 *
 */
public void setCorpID(String CorpID) {
	try{
	m_sCorpID = CorpID;
	getUIRefPane1().setWhereString(
		"gubflag='N' and sealflag='N' and pk_corp='" + m_sCorpID + "'");

	//系统启用日期
	if (m_sStartDate == null) {
		String[] sdate =
			nc.ui.sm.user.UserPowerUI.queryEnabledPeriod(m_sCorpID, "IC");
		if (sdate != null && sdate.length > 2) {
			AccountCalendar calendar1=AccountCalendar.getInstance();
			calendar1.set(sdate[0], sdate[1]);
			m_sStartDate=calendar1.getMonthVO().getBegindate().toString();
//			nc.vo.bd.period2.AccperiodmonthVO vo =
//				nc.ui.bd.pub.CallPeriod.findMonth(sdate[0], sdate[1]);
//			if (vo != null)
//				m_sStartDate = vo.getBegindate().toString();

		}
	}
	}catch (BusinessException be){
        Logger.error(be.getMessage(),be);
        MessageDialog.showErrorDlg(this,null,be.getMessage());
    } catch (BusinessRuntimeException bre){
        Logger.error(bre.getMessage(),bre);
        MessageDialog.showErrorDlg(this,null,bre.getMessage());
    } catch (Exception ee) {
        MessageDialog.showUnknownErrorDlg(this,ee);
    }
	
}
/**
 * 创建者：顾焱
 * 功能：
 * 参数：
 * 返回：
 * 例外：
 * 日期：(2001-5-22 10:53:53)
 * 修改日期，修改人，修改原因，注释标志：
 *
 */
public void setCorpID(String CorpID,String sLogDate) {
	m_sCorpID = CorpID;
	getUIRefPane1().setWhereString(
		"gubflag='N' and sealflag='N' and pk_corp='" + m_sCorpID + "'");

	//系统启用日期
	if (m_sStartDate == null) {
		m_sStartDate=sLogDate;
	}
}
/**
 * Comment
 */
protected void uIChkDataZero_onClick(java.awt.event.ActionEvent actionEvent) {
	if (getUIChkDataZero().isSelected()) {
		getUIChkNoOut().setSelected(!getUIChkDataZero().isSelected());
	}
	return;
}
/**
 * Comment
 */
protected void uIChkNoOut_onClick(java.awt.event.ActionEvent actionEvent) {
	if (getUIChkNoOut().isSelected()) {
		getUIChkDataZero().setSelected(!getUIChkNoOut().isSelected());
	}
	return;
}
/**
 * Comment
 */
protected void uIRadBtnCyc_onClick() {
	if (getUIRadBtnCyc().isSelected())
	{
		radFlag=CheckMode.Circle;
		setChooseState();
	}
	return;
}
/**
 * Comment
 */
protected void uIRadBtnDate_onClick() {
	if (getUIRadBtnDate().isSelected())
	{
		radFlag=CheckMode.Term;
		setChooseState();
		getUITxtFldDateDay().setEnabled(true);
	}
	return;
}
/**
 * Comment
 */
protected void uIRadBtnGoods_onClick() {
	if (getUIRadBtnGoods().isSelected())
	{
		radFlag=CheckMode.Goods;
		setChooseState();
		getBtnChooseGooods().setEnabled(true);
	}

}
/**
 * Comment
 */
protected void uIRadBtnMinusStore_onClick() {
	if (getUIRadBtnMinusStore().isSelected())
	{
		radFlag=CheckMode.Minus;
		setChooseState();

	}
	return;
}
/**
 * Comment
 */
protected void uIRadBtnSpace_onClick() {
	if (getUIRadBtnSpace().isSelected())
	{
		radFlag=CheckMode.Space;
		setChooseState();
		getBtnChooseSpace().setEnabled(true);
	}
}

/**
 * Comment
 */
protected void uIRadBtnMD_onClick() {
	if (getUIRadBtnMD().isSelected())
	{
		radFlag=CheckMode.md;
		setChooseState();
		getBtnChooseMDSpace().setEnabled(true);
	}
}


/**
 * Comment
 */
protected void uIRadBtnStatic_onClick() {
	if (getUIRadBtnStatic().isSelected())
	{
		radFlag=CheckMode.NActive;
		setChooseState();
		getUITxtFldNActDay().setEnabled(true);
	}
	return;
}
/**
 * Comment
 */
protected void uIRadBtnWhAdmin_onClick() {
	if (getUIRadBtnWhAdmin().isSelected())
	{
		radFlag=CheckMode.Keeper;
		setChooseState();
		getUIRefPKeeper().setEnabled(true);
	}
	return;
}
/**
 * Comment
 */
protected void uIRadBtnWhlWh_onClick() {
	if (getUIRadBtnWhlWh().isSelected())
	{
		radFlag=CheckMode.WholeWh;
		setChooseState();
	}
}
/**
 * Comment
 */
protected void uIRefPane1_Editable(boolean alreadyEdit) {
	if (alreadyEdit)
		resetCompnent(alreadyEdit);
	return;
}
/**
 * Comment
 */
protected void uIRefPane1_ValueChanged(
	nc.ui.pub.beans.ValueChangedEvent event) {
	if (getUIRefPane1().getRefPK().equals(null)
		|| getUIRefPane1().getRefPK().trim() == "")
		resetCompnent(false);
	else
		resetCompnent(true);
	String sID = getUIRefPane1().getRefPK();

	//保存名称以在列表形式下显示。
	try {
		nc.vo.scm.ic.bill.WhVO voWh =
			(nc.vo.scm.ic.bill.WhVO) SpecialBillHelper.queryInfo(new Integer(1), sID);
		//货位管理仓库，按钮可用控制
		m_bIsSpcaceMgt = (voWh.getIsLocatorMgt().intValue() == 1);
		if (m_bIsSpcaceMgt) {
			getUIRadBtnSpace().setEnabled(true);
			getUIRadBtnWhlWh().setEnabled(false);

			if (getUIRadBtnWhlWh().isSelected()) {
				getUIRadBtnWhlWh().setSelected(false);
				getUIRadBtnSpace().setSelected(true);
			}
			if (getUIRadBtnSpace().isSelected()) {
			radFlag = CheckMode.Space;
			}
		} else {
			getUIRadBtnWhlWh().setEnabled(true);
			getUIRadBtnSpace().setEnabled(false);
			if (getUIRadBtnSpace().isSelected()) {
				getUIRadBtnSpace().setSelected(false);
				getUIRadBtnWhlWh().setSelected(true);
			}
			if (getUIRadBtnWhlWh().isSelected()) {
			radFlag = CheckMode.WholeWh;
			}

		}
		//选择货位按钮控制
		if (getUIRadBtnSpace().isSelected())
			getBtnChooseSpace().setEnabled(true);
		else
			getBtnChooseSpace().setEnabled(false);
	} catch (Exception e) {
	}
	return;
}
/**
 * 参照内容变化事件监听者必须实现的接口方法
 * @param event valueChangedEvent 参照内容变化事件
 */
public void valueChanged(nc.ui.pub.beans.ValueChangedEvent event) {
	connEtoC13(event);
}

	private UILabel ivjUILabel31111 = null;
	private UILabel ivjUILabel51 = null;
	private UIRadioButton ivjUIRadBtnDynamic = null;
	private UITextField ivjUITxtFldDateDay1 = null;

/**
 * 返回 UILabel31111 特性值。
 * @return nc.ui.pub.beans.UILabel
 */
/* 警告：此方法将重新生成。 */
private nc.ui.pub.beans.UILabel getUILabel31111() {
	if (ivjUILabel31111 == null) {
		try {
			ivjUILabel31111 = new nc.ui.pub.beans.UILabel();
			ivjUILabel31111.setName("UILabel31111");
			ivjUILabel31111.setFont(new java.awt.Font("dialog", 1, 12));
			ivjUILabel31111.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec","UPP4008spec-000157")/*@res "天"*/);
			ivjUILabel31111.setBounds(203, 228, 21, 22);
			ivjUILabel31111.setEnabled(false);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjUILabel31111;
}

/**
 * 返回 UILabel51 特性值。
 * @return nc.ui.pub.beans.UILabel
 */
/* 警告：此方法将重新生成。 */
private nc.ui.pub.beans.UILabel getUILabel51() {
	if (ivjUILabel51 == null) {
		try {
			ivjUILabel51 = new nc.ui.pub.beans.UILabel();
			ivjUILabel51.setName("UILabel51");
			ivjUILabel51.setText("n=");
			ivjUILabel51.setBounds(116, 228, 18, 22);
			ivjUILabel51.setEnabled(false);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjUILabel51;
}

/**
 * 返回 UIRadBtnDynamic 特性值。
 * @return nc.ui.pub.beans.UIRadioButton
 */
/* 警告：此方法将重新生成。 */
private nc.ui.pub.beans.UIRadioButton getUIRadBtnDynamic() {
	if (ivjUIRadBtnDynamic == null) {
		try {
			ivjUIRadBtnDynamic = new nc.ui.pub.beans.UIRadioButton();
			ivjUIRadBtnDynamic.setName("UIRadBtnDynamic");
			ivjUIRadBtnDynamic.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec","UPP4008spec-000547")/*@res "动态盘点"*/);
			ivjUIRadBtnDynamic.setContentAreaFilled(false);
			ivjUIRadBtnDynamic.setBounds(13, 228, 100, 22);
			ivjUIRadBtnDynamic.setEnabled(false);
			ivjUIRadBtnDynamic.setActionCommand(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec","UPP4008spec-000547")/*@res "动态盘点"*/);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjUIRadBtnDynamic;
}

/**
 * 返回 UITxtFldDateDay1 特性值。
 * @return nc.ui.pub.beans.UITextField
 */
/* 警告：此方法将重新生成。 */
private nc.ui.pub.beans.UITextField getUITxtFldDynamic() {
	if (ivjUITxtFldDateDay1 == null) {
		try {
			ivjUITxtFldDateDay1 = new nc.ui.pub.beans.UITextField();
			ivjUITxtFldDateDay1.setName("UITxtFldDynamic");
			ivjUITxtFldDateDay1.setBounds(131, 228, 65, 20);
			ivjUITxtFldDateDay1.setEnabled(false);
			ivjUITxtFldDateDay1.setTextType("TextInt");
			ivjUITxtFldDateDay1.setMinValue(0);
			ivjUITxtFldDateDay1.setMaxLength(4);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {}
	}
	return ivjUITxtFldDateDay1;
}

/**
 * Comment
 */
public void uIRadBtnDynamic_onClick() {
	if (getUIRadBtnDynamic().isSelected())
	{
		radFlag=CheckMode.Dynamic;
		setChooseState();
		getUITxtFldDynamic().setEnabled(true);
	}
	return;
}
public String getLogDate() {
	return m_sLogDate;
}
public void setLogDate(String logDate) {
	m_sLogDate = logDate;
}
/**
 * 创建人：刘家清
创建日期：2007-12-3下午02:08:10
创建原因：清除数据。
 *
 */
public void clearOldCache(){
	if (null != getdlgSpace().getvos())
		getdlgSpace().getvos().clear();
}

}