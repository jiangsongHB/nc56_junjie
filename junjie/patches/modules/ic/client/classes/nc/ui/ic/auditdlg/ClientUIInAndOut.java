package nc.ui.ic.auditdlg;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.itf.arap.pub.IArapBillPublic;
import nc.itf.ia.bill.IBill;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.ui.ic.ic001.BatchCodeDefSetTool;
import nc.ui.ic.ic233.ClientUI;
import nc.ui.ic.jj.JJIcScmPubHelper;
import nc.ui.ic.pub.bill.GeneralBillClientUI;
import nc.ui.ic.pub.bill.GeneralBillUICtl;
import nc.ui.ic.pub.bill.GeneralButtonManager;
import nc.ui.ic.pub.bill.SpecialBillBaseUI;
import nc.ui.ic.pub.bill.initref.RefFilter;
import nc.ui.ml.NCLangRes;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.bill.BillItem;
import nc.ui.scm.pub.AccreditLoginDialog;
import nc.vo.ep.dj.DJZBHeaderVO;
import nc.vo.ep.dj.DJZBItemVO;
import nc.vo.ep.dj.DJZBVO;
import nc.vo.ia.bill.BillHeaderVO;
import nc.vo.ia.bill.BillItemVO;
import nc.vo.ia.bill.BillVO;
import nc.vo.ic.jjvo.InformationCostVO;
import nc.vo.ic.pub.GenMethod;
import nc.vo.ic.pub.bc.BarCodeVO;
import nc.vo.ic.pub.bill.GeneralBillHeaderVO;
import nc.vo.ic.pub.bill.GeneralBillItemVO;
import nc.vo.ic.pub.bill.GeneralBillVO;
import nc.vo.ic.pub.bill.SpecialBillVO;
import nc.vo.ic.pub.bill.Timer;
import nc.vo.ic.pub.exp.RightcheckException;
import nc.vo.ic.pub.locator.LocatorVO;
import nc.vo.ic.pub.smallbill.SMGeneralBillVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.VOStatus;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.constant.ic.BillMode;
import nc.vo.scm.pub.SCMEnv;
import nc.vo.scm.pub.session.ClientLink;

/**
 * 生成其他出/入库单界面

   显示一个同时有其他出/入库单 的界面，可以做保存等动作。


   支持批保存。

   支持出/入库单分别保存。

   用于特殊单、订单对于现存量的修改。

 * 创建日期：(2001-6-6 9:40:32)
 * @author：顾焱

   修改：2003-08-19:wnj:代码规范化。
 */
public class ClientUIInAndOut extends nc.ui.pub.beans.UIDialog {
	//2010-11-09 MeiChao 全局费用信息
	InformationCostVO[] expenseVOs = null;
	
	
	
	private javax.swing.JPanel ivjUIDialogContentPane = null;
	//其他入库单界面
	private nc.ui.ic.ic207.ClientUI ivjChldClientUIIn = null;
	//其他出库单界面
	private nc.ui.ic.ic217.ClientUI ivjChldClientUIOut = null;
	//状态条
	private nc.ui.pub.beans.UITextField ivjUITxtFldStatus = null;
	//按钮
	private nc.ui.pub.beans.UIButton ivjUIBtnCancel = null;
	private nc.ui.pub.beans.UIButton ivjUIBtnSave = null;
	private nc.ui.pub.beans.UIButton ivjUIBtnSaveSign = null;
	private nc.ui.pub.beans.UIButton ivjUIBtnSerials = null;
	private nc.ui.pub.beans.UIButton ivjUIBtnSpace = null;
	private nc.ui.pub.beans.UIButton ivjUIBtnSwitch = null;
	private nc.ui.pub.beans.UIButton ivjbtnClose = null;
	private nc.ui.pub.beans.UIButton ivjUIBtnDelRow = null;
	//tabpanel
	private nc.ui.pub.beans.UIPanel ivjUIPaneIn = null;
	private nc.ui.pub.beans.UIPanel ivjUIPaneOut = null;
	private nc.ui.pub.beans.UITabbedPane ivjUITabPane = null;

	private final int TabIn = 0; //入库单tab界面
	private final int TabOut = 1; //出库单tab界面
	public int m_TabFlag = TabIn; //显示哪个tabpanel
	public int m_SwitchFlag = BillMode.List; //卡片还是列表界面

	private ArrayList m_inVOs = null; //所有入库单
	private ArrayList m_outVOs = null; //所有出库单
	private boolean m_bIsTabInSaved = false; //是否所有入库单保存了
	private boolean m_bIsTabOutSaved = false; //是否所有出库单保存了
	private boolean m_bIsTabInHaveValue = false; //是否有入库单
	private boolean m_bIsTabOutHaveValue = false; //是否有出库单

	private String m_sSrcBillTypeCode = null; //来源单据类型
	private final String m_sBillTypeCodeIn = nc.vo.ic.pub.BillTypeConst.m_otherIn;
	//其他入库单
	private final String m_sBillTypeCodeOut = nc.vo.ic.pub.BillTypeConst.m_otherOut;
	//其他出库单

	private String m_sOldPK = null; //旧pk
	private String m_sCorpPK = null; //公司pk
	private String m_sUserID = null; //用户pk

	private boolean m_bIsOK = false; //是否保存了

	private IvjEventHandler ivjEventHandler;
	private java.awt.event.ActionListener actionListenerIn;
	private java.awt.event.ActionListener actionListenerOut;
	
	protected abstract class ActionListenerInAndOut extends
			GeneralButtonManager implements java.awt.event.ActionListener {
		public ActionListenerInAndOut(GeneralBillClientUI clientUI) throws BusinessException {
			super(clientUI);
		}
		

		/**
		 * 保存处理

		 * 创建者：顾焱
		 * 功能：
		 * 参数：
		 * 返回：
		 * 例外：
		 * 日期：(2001-5-16 15:27:06)
		 * 修改日期，修改人，修改原因，注释标志：
		 *
		 */
		protected boolean onSave() {
			return onSave(false);
		}

		protected boolean onSave(Boolean isSign) {			
			try {
				// 获得出入库的普通单据VO

				GeneralBillVO[] voaAllBill = null;
				voaAllBill = onSaveGetAllBillVO();

				// 保存开始时间
				Timer t = new Timer();
				t.start(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008busi",
						"UPP4008busi-000329")/* @res "保存开始" */);
				onSaveBaseKernel(voaAllBill, m_sUserID, m_sBillDate,isSign);
				t.stopAndShow(nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"4008busi", "UPP4008busi-000330")/* @res "保存结束" */);

				// 保存后界面处理
				onSaveSetUI(isSign);

				return true;
			} catch (Exception e) {
				/*nc.ui.pub.beans.MessageDialog.showErrorDlg(getClientUI(),
						nc.ui.ml.NCLangRes.getInstance().getStrByID("4008busi",
								"UPP4008busi-000157") @res "保存错误" , e
								.getMessage());*/
				nc.ui.ic.pub.tools.GenMethod.handleException(getClientUI(), null, e);
				/*nc.ui.pub.beans.MessageDialog.showErrorDlg(getClientUI(),
						nc.ui.ml.NCLangRes.getInstance().getStrByID("smcomm",
								"UPP1005-000019") @res "错误" , e.getMessage());*/
				return false;
			}
		}
	}

	protected class ActionListenerIn extends ActionListenerInAndOut {
		public ActionListenerIn(GeneralBillClientUI clientUI)
				throws BusinessException {
			super(clientUI);
		}

		public void actionPerformed(java.awt.event.ActionEvent e) {
			if (e.getSource() == ClientUIInAndOut.this.getUIBtnSwitch())
				onSwitch();
			else if (e.getSource() == ClientUIInAndOut.this.getUIBtnSpace())
				onSpaceAssign();
			else if (e.getSource() == ClientUIInAndOut.this.getUIBtnSerials())
				onSNAssign();
			else if (e.getSource() == ClientUIInAndOut.this.getUIBtnSave())
				onSave();
			if (e.getSource() == ClientUIInAndOut.this.getUIBtnSaveSign())
				onSave(true);
			else if (e.getSource() == ClientUIInAndOut.this.getbtnClose())
				ClientUIInAndOut.this.closeOK();
			else if (e.getSource() == ClientUIInAndOut.this.getUIBtnCancel())
				ClientUIInAndOut.this.closeCancel();
			else if (e.getSource() == ClientUIInAndOut.this.getUIBtnDelRow())
				onDeleteRow(true);
			else if (e.getSource() == ClientUIInAndOut.this.getUIBtnPickAuto())
				nc.ui.pub.beans.MessageDialog.showErrorDlg(getClientUI(), nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON","UPPSCMCommon-000270")/*@res "提示"*/, nc.ui.ml.NCLangRes.getInstance().getStrByID("4008busi","UPP4008busi-000163")/*@res "请在其他出库单上做自动拣货！"*/);
			else if (e.getSource() == ClientUIInAndOut.this.getUIBtnBarcode())
				onBarCodeEdit();
		}

		/**
		 * 卡片/列表 切换处理

		 * 创建者：顾焱
		 * 功能：
		 * 参数：
		 * 返回：
		 * 例外：
		 * 日期：(2001-5-16 15:27:06)
		 * 修改日期，修改人，修改原因，注释标志：
		 *
		 */
		protected void onSwitch() {
			if (nc.vo.scm.constant.ic.BillMode.List == getChldClientUIIn()
					.getCurPanel()) {
				super.onSwitch();
				getUIBtnSwitch()
						.setText(
								nc.ui.ml.NCLangRes.getInstance().getStrByID(
										"scmcommon", "UPPSCMCommon-000146")/*
																			 * @res
																			 * "列表"
																			 */+ "(L)");
				getUIBtnSwitch().setMnemonic('l');
				getUIBtnSwitch().setBounds(792, 229, 97, 25);
				getUIBtnSpace().setEnabled(true);
				getUIBtnSerials().setEnabled(true);
				getUIBtnPickAuto().setEnabled(false);
				if (!m_bIsOK)// 没有保存情况下
					getUIBtnBarcode().setEnabled(true);
				else
					getUIBtnBarcode().setEnabled(false);
			} else {
				GeneralBillVO billVO = getInVO();
				// billVO.setIDItems(getChldClientUIIn().getBillVO());

				int row = getChldClientUIIn().getLastSelListHeadRow();
				m_inVOs.set(row, billVO);
				getChldClientUIIn().setAllData(m_inVOs);
				// getChldClientUIIn().setLastSelListHeadRow(row);
				super.onSwitch();
				getUIBtnSwitch().setText(
						nc.ui.ml.NCLangRes.getInstance().getStrByID("4008busi",
								"UPP4008busi-000346")/* @res "表单" */+ "(L)");
				getUIBtnSwitch().setBounds(792, 229, 97, 25);
				getUIBtnSwitch().setMnemonic('l');
				getUIBtnSpace().setEnabled(false);
				getUIBtnSerials().setEnabled(false);
				getUIBtnPickAuto().setEnabled(false);
				getUIBtnBarcode().setEnabled(false);

			}
			setExpenseVOs();//2010-11-09 MeiChao 设置费用信息
			getUIBtnSave().setEnabled(getUIBtnSwitch().getText().equals(nc.ui.ml.NCLangRes.getInstance().getStrByID("scmcommon","UPPSCMCommon-000146")/*@res "列表"*/+ "(L)")&& !m_bIsTabInSaved);
			getUIBtnSaveSign().setEnabled(
					getUIBtnSwitch().getText().equals(nc.ui.ml.NCLangRes.getInstance().getStrByID("scmcommon","UPPSCMCommon-000146")/*@res "列表"*/) && !m_bIsTabInSaved);
		}

		/**
		 * Comment
		 */
		protected void onSpaceAssign() {
			if (getChldClientUIIn().isLocatorMgt()) {
				super.onSpaceAssign();
			} else
				nc.ui.pub.beans.MessageDialog.showErrorDlg(getClientUI(), nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON","UPPSCMCommon-000059")/*@res "错误"*/, nc.ui.ml.NCLangRes.getInstance().getStrByID("4008busi","UPP4008busi-000159")/*@res "不是货位管理仓库。"*/);
		}

		/**
		 * Comment
		 */
		protected void onSNAssign() {
			if (getChldClientUIIn().isSNmgt()) {
				super.onSNAssign();
			} else
				nc.ui.pub.beans.MessageDialog.showErrorDlg(getClientUI(), nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON","UPPSCMCommon-000059")/*@res "错误"*/, nc.ui.ml.NCLangRes.getInstance().getStrByID("4008busi","UPP4008busi-000158")/*@res "此行不是序列号管理。"*/);
		}

		/**
		 * Comment
		 */
		protected void onDeleteRow(boolean isevent) {
      if (isevent && nc.vo.ic.pub.BillTypeConst.m_transfer.equals(m_sSrcBillTypeCode) && m_bIsDirectOut){
        if (getChldClientUIIn().getCardTableRowNum() > 1 && 
            getChldClientUIIn().getCardTableRowNum()==getChldClientUIOut().getCardTableRowNum() && actionListenerOut!=null) {
          
            int[] rows = getChldClientUIIn().getBillCardPanel().getBillTable().getSelectedRows();
            if(rows==null || rows.length<=0)
              return;
            super.onDeleteRow();
            getChldClientUIOut().getBillCardPanel().getBillTable().getSelectionModel().clearSelection();
            for(int i=0;i<rows.length;i++){
              getChldClientUIOut().getBillCardPanel().getBillTable().getSelectionModel().addSelectionInterval(rows[i], rows[i]);
            }
            ((ActionListenerOut)actionListenerOut).onDeleteRow(false);
        }
      }else{
  			if (getChldClientUIIn().getCardTableRowNum() > 1) {
  				super.onDeleteRow();
  			}
      }
		}
	}

	protected class ActionListenerOut extends ActionListenerInAndOut {
		public ActionListenerOut(GeneralBillClientUI clientUI)
				throws BusinessException {
			super(clientUI);
		}

		public void actionPerformed(java.awt.event.ActionEvent e) {
			if (e.getSource() == ClientUIInAndOut.this.getUIBtnSwitch())
				onSwitch();
			else if (e.getSource() == ClientUIInAndOut.this.getUIBtnSpace())
				onSpaceAssign();
			else if (e.getSource() == ClientUIInAndOut.this.getUIBtnSerials())
				onSNAssign();
			else if (e.getSource() == ClientUIInAndOut.this.getUIBtnSave())
				onSave();
			else if (e.getSource() == ClientUIInAndOut.this.getUIBtnSaveSign())
				onSave(true);
			else if (e.getSource() == ClientUIInAndOut.this.getbtnClose())
				ClientUIInAndOut.this.closeOK();
			else if (e.getSource() == ClientUIInAndOut.this.getUIBtnCancel())
				ClientUIInAndOut.this.closeCancel();
			else if (e.getSource() == ClientUIInAndOut.this.getUIBtnDelRow())
				onDeleteRow(true);
			else if (e.getSource() == ClientUIInAndOut.this.getUIBtnPickAuto())
				onPickAuto();
			else if (e.getSource() == ClientUIInAndOut.this.getUIBtnBarcode())
				onBarCodeEdit();
		}
		
		protected void onSwitch() {
			if (BillMode.List == getChldClientUIOut().getCurPanelParam()) {
				super.onSwitch();
				getUIBtnSwitch().setText(nc.ui.ml.NCLangRes.getInstance().getStrByID("scmcommon","UPPSCMCommon-000146")/*@res "列表"*/+ "(L)");
				getUIBtnSwitch().setBounds(792, 229, 97, 25);
				getUIBtnSwitch().setMnemonic('l');
				getUIBtnSpace().setEnabled(true);
				getUIBtnSerials().setEnabled(true);
				getUIBtnPickAuto().setEnabled(false);
				if (!m_bIsOK)//没有保存情况下
					getUIBtnBarcode().setEnabled(true);
				else
					getUIBtnBarcode().setEnabled(false);

			} else {
				GeneralBillVO billVO = getOutVO();
				//GeneralBillVO billVOd= getChldClientUIOut().getm_VOBill();
				//billVO.setIDItems(getChldClientUIIn().getBillVO());

				int row = getChldClientUIOut().getLastSelListHeadRow();
				m_outVOs.set(row, billVO);
				getChldClientUIOut().setAllData(m_outVOs);
				getChldClientUIOut().setLastSelListHeadRow(row);
				super.onSwitch();
				getUIBtnSwitch().setText(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008busi","UPP4008busi-000346")/*@res "表单"*/+ "(L)");
				getUIBtnSwitch().setBounds(792, 229, 97, 25);
				getUIBtnSwitch().setMnemonic('l');
				getUIBtnSpace().setEnabled(false);
				getUIBtnSerials().setEnabled(false);
				getUIBtnBarcode().setEnabled(false);

			}

			getUIBtnSave().setEnabled(
				getUIBtnSwitch().getText().equals(nc.ui.ml.NCLangRes.getInstance().getStrByID("scmcommon","UPPSCMCommon-000146")/*@res "列表"*/+ "(L)") && !m_bIsTabOutSaved);
			getUIBtnSaveSign().setEnabled(
					getUIBtnSwitch().getText().equals(nc.ui.ml.NCLangRes.getInstance().getStrByID("scmcommon","UPPSCMCommon-000146")/*@res "列表"*/+ "(L)") && !m_bIsTabOutSaved);
		getUIBtnPickAuto().setEnabled(getUIBtnSave().isEnabled());
		}
		
		protected void onSpaceAssign() {
			if (getChldClientUIOut().isLocatorMgt()) {
				super.onSpaceAssign();
			} else
				nc.ui.pub.beans.MessageDialog.showErrorDlg(getClientUI(), nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON","UPPSCMCommon-000059")/*@res "错误"*/, nc.ui.ml.NCLangRes.getInstance().getStrByID("4008busi","UPP4008busi-000159")/*@res "不是货位管理仓库。"*/);
		}
		
		protected void onSNAssign() {
			if (getChldClientUIOut().isSNmgt()) {
				super.onSNAssign();
			} else
				nc.ui.pub.beans.MessageDialog.showErrorDlg(getClientUI(), nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON","UPPSCMCommon-000059")/*@res "错误"*/, nc.ui.ml.NCLangRes.getInstance().getStrByID("4008busi","UPP4008busi-000158")/*@res "此行不是序列号管理。"*/);
		}
		
		protected void onDeleteRow(boolean isevent) {
      if (isevent && nc.vo.ic.pub.BillTypeConst.m_transfer.equals(m_sSrcBillTypeCode) && m_bIsDirectOut){
        if (getChldClientUIOut().getCardTableRowNum() > 1 && 
            getChldClientUIIn().getCardTableRowNum()==getChldClientUIOut().getCardTableRowNum() && actionListenerIn!=null) {
          
            int[] rows = getChldClientUIOut().getBillCardPanel().getBillTable().getSelectedRows();
            if(rows==null || rows.length<=0)
              return;
            super.onDeleteRow();
            getChldClientUIIn().getBillCardPanel().getBillTable().getSelectionModel().clearSelection();
            for(int i=0;i<rows.length;i++){
              getChldClientUIIn().getBillCardPanel().getBillTable().getSelectionModel().addSelectionInterval(rows[i], rows[i]);
            }
            ((ActionListenerIn)actionListenerIn).onDeleteRow(false);
        }
      }else{
  			if (getChldClientUIOut().getCardTableRowNum() > 1) 
  				super.onDeleteRow();
			}
		}
	}
	
	//按钮事件处理
	private class IvjEventHandler implements javax.swing.event.ChangeListener {
		public void stateChanged(javax.swing.event.ChangeEvent e) {
			if (e.getSource() == ClientUIInAndOut.this.getUITabPane())
				TabStateChanged();
		}
	}
	
/**
 * ClientUIInAndOut 构造子注解。
 */
public ClientUIInAndOut() {
	super();
	initialize();
}
/**
 * ClientUIInAndOut 构造子注解。
 * @param parent java.awt.Container
 */
private ClientUIInAndOut(java.awt.Container parent) {
	super(parent);
}
/**
 * ClientUIInAndOut 构造子注解。
 * @param parent java.awt.Container
 * @param title java.lang.String
 */
public ClientUIInAndOut(java.awt.Container parent, String title) {
	super(parent, title);
	initialize();
	}
/**
 * ClientUIInAndOut 构造子注解。
 * @param parent java.awt.Container
 * @param title java.lang.String
 * sBillTypeCode 单据类型
 */
public ClientUIInAndOut(
	java.awt.Container parent,
	String title,
	ArrayList invos,
	ArrayList outvos,
	String sBillTypeCode,
	String sOldPK,
	String sCorpPK,
	String sUserID) {

	super(parent, title);

	if (((invos == null) && (outvos == null))
		|| (sBillTypeCode == null)
		|| (sBillTypeCode.trim().length() == 0)
		|| (sCorpPK == null)
		|| (sCorpPK.trim().length() == 0)
		|| (sUserID == null)
		|| (sUserID.trim().length() == 0))
		return;

	m_sSrcBillTypeCode= sBillTypeCode.trim();
	m_sCorpPK= sCorpPK.trim();
	m_sUserID= sUserID.trim();
	m_sOldPK= sOldPK;

	initialize();

	if (invos != null) {
		m_inVOs= (ArrayList) invos.clone();
		getChldClientUIIn().onAdd(true, null);
		getChldClientUIIn().setAllData(invos);
		//getChldClientUIIn().setBillVO((GeneralBillVO) invos.get(0));
		getUITabPane().setEnabledAt(0, true);
		getUITabPane().setSelectedIndex(0);
		m_bIsTabInHaveValue= true;
	} else {
		getUIPaneIn().setEnabled(false);
		getUITabPane().setEnabledAt(0, false);
		m_bIsTabInHaveValue= false;
	}
	if (outvos != null) {
		m_outVOs= (ArrayList) outvos.clone();
		getChldClientUIOut().onAdd(true, null);
		getChldClientUIOut().setAllData(outvos);
		//getChldClientUIOut().setBillVO((GeneralBillVO) outvos.get(0));
		getUITabPane().setEnabledAt(1, true);
		getUITabPane().setSelectedIndex(1);
		m_bIsTabOutHaveValue= true;
	} else {
		getUIPaneOut().setEnabled(false);
		getUITabPane().setEnabledAt(1, false);
		m_bIsTabOutHaveValue= false;
	}

}
/**
 * ClientUIInAndOut 构造子注解。
 * @param owner java.awt.Frame
 */
private ClientUIInAndOut(java.awt.Frame owner) {
	super(owner);
}
/**
 * ClientUIInAndOut 构造子注解。
 * @param owner java.awt.Frame
 * @param title java.lang.String
 */
private ClientUIInAndOut(java.awt.Frame owner, String title) {
	super(owner, title);
}
/**
 * 创建者：仲瑞庆
 * 功能：
 * 参数：
 * 返回：
 * 例外：
 * 日期：(2001-10-25 15:15:41)
 * 修改日期，修改人，修改原因，注释标志：
 */
private void afterInit() {
	//没保存
	m_bIsTabInSaved = false;
	m_bIsTabOutSaved = false;
	//ydy
	m_bIsOK = false;
	m_sLogDate=nc.ui.pub.ClientEnvironment.getInstance().getDate().toString();
	//getUIBtnSwitch().setEnabled(false);
	getbtnClose().setEnabled(false);
	getUIBtnCancel().setEnabled(true);

//	TabStateChanged();
	getChldClientUIIn().setBodyMenuShow(false);
	getChldClientUIOut().setBodyMenuShow(false);
	//置删行按钮
	if (m_sSrcBillTypeCode.equals(nc.vo.ic.pub.BillTypeConst.m_transfer)) {






		//转库
		getUIBtnDelRow().setEnabled(true);
	} else if (m_sSrcBillTypeCode.equals(nc.vo.ic.pub.BillTypeConst.m_assembly)) {





		//组装
		getUIBtnDelRow().setEnabled(false);
	} else if (
		m_sSrcBillTypeCode.equals(nc.vo.ic.pub.BillTypeConst.m_disassembly)
			|| m_sSrcBillTypeCode.equals(nc.vo.ic.pub.BillTypeConst.m_saleOut)
			|| m_sSrcBillTypeCode.equals(nc.vo.ic.pub.BillTypeConst.m_purchaseIn)) {
		//拆卸//销售出库单或采购入库单的配套
		getUIBtnDelRow().setEnabled(false);
	} else if (
		m_sSrcBillTypeCode.equals(nc.vo.ic.pub.BillTypeConst.m_transform)) { //形转
		getUIBtnDelRow().setEnabled(false);
	} else if (
		m_sSrcBillTypeCode.equals(nc.vo.ic.pub.BillTypeConst.m_check)) { //盘点
		getUIBtnDelRow().setEnabled(true);
	} else if (
		m_sSrcBillTypeCode.equals(
			nc.vo.ic.pub.BillTypeConst.m_AllocationOrder)) //调拨订单
		getUIBtnDelRow().setEnabled(true);
}

/**
 * 返回 btnClose 特性值。
 * @return nc.ui.pub.beans.UIButton
 */
/* 警告：此方法将重新生成。 */
private nc.ui.pub.beans.UIButton getbtnClose() {
	if (ivjbtnClose == null) {
		try {
			ivjbtnClose = new nc.ui.pub.beans.UIButton();
			ivjbtnClose.setName("btnClose");
			ivjbtnClose.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON","UPPSCMCommon-000119")/*@res "关闭"*/+ "(G)");
			//ivjbtnClose.setBounds(584, 14, 97, 25);
			ivjbtnClose.setBounds(792, 340, 97, 25);
			ivjbtnClose.setMnemonic('g');
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjbtnClose;
}


/**
 * 返回 ChldClientUI 特性值。
 * @return nc.ui.ic.ic207.ClientUI
 */
/* 警告：此方法将重新生成。 */
public nc.ui.ic.ic207.ClientUI getChldClientUIIn() {
	if (ivjChldClientUIIn == null) {
		try {
			ivjChldClientUIIn = new nc.ui.ic.ic207.ClientUI();
			ivjChldClientUIIn.setName("ChldClientUIIn");
			ivjChldClientUIIn.setBounds(1, 0, 770, 428);
			// user code begin {1}
			// 0605 by zhx remove the right menu button
			ivjChldClientUIIn.setBodyMenuShow(false);

			//不允许添加新行
			ivjChldClientUIIn.getBarcodeCtrl().setAddNewInvLine(false);
			//ivjChldClientUIIn.getBarcodeCtrl().setModifyBillUINum(false);
			
			//修改人：刘家清 修改日期：2007-05-22 修改原因：因为表格排序会导致其它出库单和其它入库单数据对应出错，所以暂时封掉！
			ivjChldClientUIIn.getBillCardPanel().getBodyPanel().getTable().setSortEnabled(false);
			ivjChldClientUIIn.getBillListPanel().getHeadTable().setSortEnabled(false);
			ivjChldClientUIIn.getBillListPanel().getBodyTable().setSortEnabled(false);

			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjChldClientUIIn;
}
/**
 * 返回 ChldClientUIOut 特性值。
 * @return nc.ui.ic.ic217.ClientUI
 */
/* 警告：此方法将重新生成。 */
public nc.ui.ic.ic217.ClientUI getChldClientUIOut() {
	if (ivjChldClientUIOut == null) {
		try {
			ivjChldClientUIOut = new nc.ui.ic.ic217.ClientUI();
			ivjChldClientUIOut.setName("ChldClientUIOut");
			ivjChldClientUIOut.setBounds(2, 0, 770, 428);
			// user code begin {1}
			// 0605 by zhx remove the right menu button

			ivjChldClientUIOut.getBarcodeCtrl().setAddNewInvLine(false);
			//ivjChldClientUIIn.getBarcodeCtrl().setModifyBillUINum(false);

			ivjChldClientUIOut.setBodyMenuShow(false);
			
			//修改人：刘家清 修改日期：2007-05-22 修改原因：因为表格排序会导致其它出库单和其它入库单数据对应出错，所以暂时封掉！
			ivjChldClientUIOut.getBillCardPanel().getBodyPanel().getTable().setSortEnabled(false);
			ivjChldClientUIOut.getBillListPanel().getHeadTable().setSortEnabled(false);
			ivjChldClientUIOut.getBillListPanel().getBodyTable().setSortEnabled(false);
			
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjChldClientUIOut;
}
/**
 *
   得到界面上显示的vo,并设为新增单据。

 * 创建日期：(2001-6-19 10:16:12)
 * @return nc.vo.ic.ic207.GeneralHVO
 */
protected GeneralBillVO getInVO() {
	GeneralBillVO inVO = null;
	//入库单可用时才读之
	if (getUITabPane().isEnabledAt(0)) {
		inVO = getChldClientUIIn().getBillVO();
		if (inVO != null
			&& inVO.getChildrenVO() != null
			&& inVO.getChildrenVO().length > 0) {
			inVO.setStatus(VOStatus.NEW);

		}
	}
	
	return inVO;
}
/**

	得到界面上显示的vo,并设为新增单据。

   * 创建日期：(2001-6-23 0:22:07)
 * @return nc.vo.ic.pub.bill.GeneralBillVO[]
 */
protected GeneralBillVO getOutVO() {
	GeneralBillVO outVO = null;
	//入库单可用时才读之
	if (getUITabPane().isEnabledAt(1)) {
		outVO = getChldClientUIOut().getBillVO();
		if (outVO != null
			&& outVO.getChildrenVO() != null
			&& outVO.getChildrenVO().length > 0) {
			outVO.setStatus(VOStatus.NEW);
		}
	}
	return outVO;
}
/**
 * 返回 UIButton21 特性值。
 * @return nc.ui.pub.beans.UIButton
 */
/* 警告：此方法将重新生成。 */
private nc.ui.pub.beans.UIButton getUIBtnCancel() {
	if (ivjUIBtnCancel == null) {
		try {
			ivjUIBtnCancel = new nc.ui.pub.beans.UIButton();
			ivjUIBtnCancel.setName("UIBtnCancel");
			ivjUIBtnCancel.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UC001-0000008")/*@res "取消"*/ + "(C)");
      ivjUIBtnCancel.setMnemonic('c');
			ivjUIBtnCancel.setBounds(792, 373, 97, 25);

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
 * 返回 UIBtnDelRow 特性值。
 * @return nc.ui.pub.beans.UIButton
 */
/* 警告：此方法将重新生成。 */
private nc.ui.pub.beans.UIButton getUIBtnDelRow() {
	if (ivjUIBtnDelRow == null) {
		try {
			ivjUIBtnDelRow = new nc.ui.pub.beans.UIButton();
			ivjUIBtnDelRow.setName("UIBtnDelRow");
			ivjUIBtnDelRow.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UC001-0000013")/*@res "删行"*/+ "(D)");
			ivjUIBtnDelRow.setBounds(792, 130, 97, 25);
			ivjUIBtnDelRow.setMnemonic('d');
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjUIBtnDelRow;
}
/**
 * 返回 UIButton1 特性值。
 * @return nc.ui.pub.beans.UIButton
 */
/* 警告：此方法将重新生成。 */
private nc.ui.pub.beans.UIButton getUIBtnSave() {
	if (ivjUIBtnSave == null) {
		try {
			ivjUIBtnSave = new nc.ui.pub.beans.UIButton();
			ivjUIBtnSave.setName("UIBtnSave");
			ivjUIBtnSave.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UC001-0000001")/*@res "保存"*/ + "(S)");
      //modified by liuzy 2008-01-03 元祖 设置快捷键 200712271315098077
      ivjUIBtnSave.setMnemonic('s');
			ivjUIBtnSave.setBounds(792, 64, 97, 25);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjUIBtnSave;
}


private nc.ui.pub.beans.UIButton getUIBtnSaveSign() {
	if (ivjUIBtnSaveSign == null) {
		try {
			ivjUIBtnSaveSign = new nc.ui.pub.beans.UIButton();
			ivjUIBtnSaveSign.setName("UIBtnSaveSign");
			ivjUIBtnSaveSign.setText(NCLangRes.getInstance().getStrByID("4008busi", "ClientUIInAndOut-000000", null, new String[]{})/*保存＆签字(A)*/);
      //modified by liuzy 2008-01-03 元祖 设置快捷键 200712271315098077
			ivjUIBtnSaveSign.setMnemonic('a');
			ivjUIBtnSaveSign.setBounds(792, 31, 97, 25);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjUIBtnSaveSign;
}

/**
 * 返回 UIButton41 特性值。
 * @return nc.ui.pub.beans.UIButton
 */
/* 警告：此方法将重新生成。 */
private nc.ui.pub.beans.UIButton getUIBtnSerials() {
	if (ivjUIBtnSerials == null) {
		try {
			ivjUIBtnSerials = new nc.ui.pub.beans.UIButton();
			ivjUIBtnSerials.setName("UIBtnSerials");
			ivjUIBtnSerials.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UC000-0001819")/*@res "序列号"*/+ "(X)");
			ivjUIBtnSerials.setBounds(792, 196, 97, 25);
			ivjUIBtnSerials.setMnemonic('x');
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjUIBtnSerials;
}
/**
 * 返回 UIButton31 特性值。
 * @return nc.ui.pub.beans.UIButton
 */
/* 警告：此方法将重新生成。 */
private nc.ui.pub.beans.UIButton getUIBtnSpace() {
	if (ivjUIBtnSpace == null) {
		try {
			ivjUIBtnSpace = new nc.ui.pub.beans.UIButton();
			ivjUIBtnSpace.setName("UIBtnSpace");
			ivjUIBtnSpace.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UC000-0003830")/*@res "货位"*/+ "(H)");
			ivjUIBtnSpace.setBounds(792, 163, 97, 25);
			ivjUIBtnSpace.setMnemonic('h');
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjUIBtnSpace;
}

/**
 * 设置所有单据为新增状态
 * @param al
 */
private void setVOStatusNew(ArrayList al) {
    if (al == null||al.size()<=0)
        return;
    for (int i=0;i<al.size();i++) {
        GeneralBillVO vo = (GeneralBillVO) al.get(i);
        vo.setStatus(VOStatus.NEW);
    }
}



/**
 * 返回 UIBtnSwitch 特性值。
 * @return nc.ui.pub.beans.UIButton
 */
/* 警告：此方法将重新生成。 */
private nc.ui.pub.beans.UIButton getUIBtnSwitch() {
	if (ivjUIBtnSwitch == null) {
		try {
			ivjUIBtnSwitch = new nc.ui.pub.beans.UIButton();
			ivjUIBtnSwitch.setName("UIBtnSwitch");
			ivjUIBtnSwitch.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID("scmcommon","UPPSCMCommon-000146")/*@res "列表"*/+ "(L)");
			ivjUIBtnSwitch.setBounds(792, 229, 97, 25);
			ivjUIBtnSwitch.setMnemonic('l');
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjUIBtnSwitch;
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
			ivjUIDialogContentPane.setMinimumSize(new java.awt.Dimension(100, 100));
			
			getUIDialogContentPane().add(getUIBtnSave(), getUIBtnSave().getName());
			//getUIDialogContentPane().add(getUIBtnSaveSign(), getUIBtnSaveSign().getName());
			getUIDialogContentPane().add(getUIBtnPickAuto(), getUIBtnPickAuto().getName());
			getUIDialogContentPane().add(getUIBtnSerials(), getUIBtnSerials().getName());
			getUIDialogContentPane().add(getUIBtnSpace(), getUIBtnSpace().getName());
			getUIDialogContentPane().add(getUIBtnSwitch(), getUIBtnSwitch().getName());
			getUIDialogContentPane().add(getbtnClose(), getbtnClose().getName());
			getUIDialogContentPane().add(getUIBtnDelRow(), getUIBtnDelRow().getName());
			getUIDialogContentPane().add(getUIBtnBarcode(), getUIBtnBarcode().getName());			
			getUIDialogContentPane().add(getUIBtnCancel(), getUIBtnCancel().getName());
			
			getUIDialogContentPane().add(getUITabPane(), getUITabPane().getName());
			getUIDialogContentPane().add(getUITxtFldStatus(), getUITxtFldStatus().getName());
			
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
 * 返回 UIPanel1 特性值。
 * @return nc.ui.pub.beans.UIPanel
 */
/* 警告：此方法将重新生成。 */
private nc.ui.pub.beans.UIPanel getUIPaneIn() {
	if (ivjUIPaneIn == null) {
		try {
			ivjUIPaneIn = new nc.ui.pub.beans.UIPanel();
			ivjUIPaneIn.setName("UIPaneIn");
			ivjUIPaneIn.setLayout(null);
			ivjUIPaneIn.add(getChldClientUIIn(), getChldClientUIIn().getName());
//			getUIPaneIn().add(getChldClientUIIn(), getChldClientUIIn().getName());
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjUIPaneIn;
}
/**
 * 返回 UIPanel2 特性值。
 * @return nc.ui.pub.beans.UIPanel
 */
/* 警告：此方法将重新生成。 */
private nc.ui.pub.beans.UIPanel getUIPaneOut() {
	if (ivjUIPaneOut == null) {
		try {
			ivjUIPaneOut = new nc.ui.pub.beans.UIPanel();
			ivjUIPaneOut.setName("UIPaneOut");
//			ivjUIPaneOut.setLayout(null);
			ivjUIPaneOut.add(getChldClientUIOut(), getChldClientUIOut().getName());
//			getUIPaneOut()
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjUIPaneOut;
}
/**
 * 返回 UITabbedPane1 特性值。
 * @return nc.ui.pub.beans.UITabbedPane
 */
/* 警告：此方法将重新生成。 */
private nc.ui.pub.beans.UITabbedPane getUITabPane() {
	if (ivjUITabPane == null) {
		try {
			ivjUITabPane = new nc.ui.pub.beans.UITabbedPane();
			ivjUITabPane.setName("UITabPane");
			ivjUITabPane.setBounds(5, 14, 782, 463);
			ivjUITabPane.insertTab(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008busi","UPT40080608-000002")/*@res "其它入库单"*/, null, getUIPaneIn(), null, 0);
			ivjUITabPane.insertTab(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008busi","UPT40080808-000002")/*@res "其它出库单"*/, null, getUIPaneOut(), null, 1);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjUITabPane;
}
/**
 * 返回 UITextField1 特性值。
 * @return nc.ui.pub.beans.UITextField
 */
/* 警告：此方法将重新生成。 */
private nc.ui.pub.beans.UITextField getUITxtFldStatus() {
	if (ivjUITxtFldStatus == null) {
		try {
			ivjUITxtFldStatus = new nc.ui.pub.beans.UITextField();
			ivjUITxtFldStatus.setName("UITxtFldStatus");
			ivjUITxtFldStatus.setBorder(new nc.ui.pub.style.TableHeaderBorder());
			ivjUITxtFldStatus.setText("");
			ivjUITxtFldStatus.setBounds(10, 498, 782, 21);
			ivjUITxtFldStatus.setEditable(false);
			ivjUITxtFldStatus.setMaxLength(200);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjUITxtFldStatus;
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
private void initConnections() {
	if (ivjEventHandler == null) {
		ivjEventHandler = new IvjEventHandler();
		getUITabPane().addChangeListener(ivjEventHandler);
	}
	try {
		if (actionListenerIn == null) {
			actionListenerIn = new ActionListenerIn(getChldClientUIIn());
		}
		if (actionListenerOut == null) {
			actionListenerOut = new ActionListenerOut(getChldClientUIOut());
		}
	}
	catch (Exception e) {
		nc.ui.pub.beans.MessageDialog.showErrorDlg(this,
					nc.ui.ml.NCLangRes.getInstance().getStrByID("4008busi",
							"UPP4008busi-000157")/* @res "保存错误" */, e
							.getMessage());
		return;
	}
	
	// 删除In的动作监听
	getUIBtnSwitch().removeActionListener(actionListenerIn);
	getUIBtnSpace().removeActionListener(actionListenerIn);
	getUIBtnSerials().removeActionListener(actionListenerIn);
	getUIBtnSave().removeActionListener(actionListenerIn);
	getUIBtnSaveSign().removeActionListener(actionListenerIn);
	getbtnClose().removeActionListener(actionListenerIn);
	getUIBtnCancel().removeActionListener(actionListenerIn);
	getUIBtnDelRow().removeActionListener(actionListenerIn);
	getUIBtnPickAuto().removeActionListener(actionListenerIn);
	getUIBtnBarcode().removeActionListener(actionListenerIn);
	// 删除Out的动作监听
	getUIBtnSwitch().removeActionListener(actionListenerOut);
	getUIBtnSpace().removeActionListener(actionListenerOut);
	getUIBtnSerials().removeActionListener(actionListenerOut);
	getUIBtnSave().removeActionListener(actionListenerOut);
	getUIBtnSaveSign().removeActionListener(actionListenerOut);
	getbtnClose().removeActionListener(actionListenerOut);
	getUIBtnCancel().removeActionListener(actionListenerOut);
	getUIBtnDelRow().removeActionListener(actionListenerOut);
	getUIBtnPickAuto().removeActionListener(actionListenerOut);
	getUIBtnBarcode().removeActionListener(actionListenerOut);
	
	if (m_TabFlag == TabIn) {
		// 添加In的动作监听
		getUIBtnSwitch().addActionListener(actionListenerIn);
		getUIBtnSpace().addActionListener(actionListenerIn);
		getUIBtnSerials().addActionListener(actionListenerIn);
		getUIBtnSave().addActionListener(actionListenerIn);
		getUIBtnSaveSign().addActionListener(actionListenerIn);
		getbtnClose().addActionListener(actionListenerIn);
		getUIBtnCancel().addActionListener(actionListenerIn);
		getUIBtnDelRow().addActionListener(actionListenerIn);
		getUIBtnPickAuto().addActionListener(actionListenerIn);
		getUIBtnBarcode().addActionListener(actionListenerIn);
	}
	else {
		// 添加Out的动作监听
		getUIBtnSwitch().addActionListener(actionListenerOut);
		getUIBtnSpace().addActionListener(actionListenerOut);
		getUIBtnSerials().addActionListener(actionListenerOut);
		getUIBtnSave().addActionListener(actionListenerOut);
		getUIBtnSaveSign().addActionListener(actionListenerOut);
		getbtnClose().addActionListener(actionListenerOut);
		getUIBtnCancel().addActionListener(actionListenerOut);
		getUIBtnDelRow().addActionListener(actionListenerOut);
		getUIBtnPickAuto().addActionListener(actionListenerOut);
		getUIBtnBarcode().addActionListener(actionListenerOut);
	}
}
/**
 * 初始化类。
 */
/* 警告：此方法将重新生成。 */
private void initialize() {
	try {
		m_sLogDate=nc.ui.pub.ClientEnvironment.getInstance().getDate().toString();
		m_sCorpName=nc.ui.pub.ClientEnvironment.getInstance().getCorporation().getUnitname();
		setCorpID(nc.ui.pub.ClientEnvironment.getInstance().getCorporation().getPk_corp());
		setName("ClientUIInAndOut");
		setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
		setSize(900, 560);
		setTitle(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008busi","UPP4008busi-000328")/*@res "其它入其它出"*/);
		setContentPane(getUIDialogContentPane());
		initConnections();
	} catch (java.lang.Throwable ivjExc) {
		handleException(ivjExc);
	}

}
/**

 * 方法说明：返回是否保存了单据

 * 创建日期：(2003-1-16 11:40:31)
 * 作者：王乃军
 * 修改日期：
 * 修改人：
 * 修改原因：
 * 算法说明：
 * @return boolean
 */
public boolean isOK() {
	return m_bIsOK;
}
/**
 * 主入口点 - 当部件作为应用程序运行时，启动这个部件。
 * @param args java.lang.String[]
 */
public static void main(java.lang.String[] args) {
	try {
		ClientUIInAndOut aClientUIInAndOut;
		aClientUIInAndOut = new ClientUIInAndOut();
		aClientUIInAndOut.setModal(true);
		aClientUIInAndOut.addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosing(java.awt.event.WindowEvent e) {
				System.exit(0);
			};
		});
		aClientUIInAndOut.show();
		java.awt.Insets insets = aClientUIInAndOut.getInsets();
		aClientUIInAndOut.setSize(aClientUIInAndOut.getWidth() + insets.left + insets.right, aClientUIInAndOut.getHeight() + insets.top + insets.bottom);
		aClientUIInAndOut.setVisible(true);
	} catch (Throwable exception) {
		nc.vo.scm.pub.SCMEnv.out(NCLangRes.getInstance().getStrByID("4008busi", "ClientUIInAndOut-000001")/*nc.ui.pub.beans.UIDialog 的 main() 中发生异常*/);
		nc.vo.scm.pub.SCMEnv.error(exception);
	}
}

/**
 * 类型说明： 设置“已保存”状态。
 * 创建日期：(2003-1-16 11:40:31)
 * 作者：王乃军
 * 修改日期：
 * 修改人：
 * 修改原因：
 * 算法说明：
 * @return boolean
 */
public void setIsOK(boolean isok) {
	m_bIsOK = isok;
}

/**
 * 在状态条显示信息
 * 创建日期：(2001-6-6 14:26:29)
 */
public void showStatus(String msg) {
	getUITxtFldStatus().setText(msg);

}
/**
 * 创建者：王乃军
 * 功能：显示消耗的时间
 * 参数：
 * 返回：
 * 例外：
 * 日期：(2001-11-23 18:11:18)
 *  修改日期，修改人，修改原因，注释标志：
 */
protected void showTime(long lStartTime, String sTaskHint) {
	long lTime = System.currentTimeMillis() - lStartTime;
	nc.vo.scm.pub.SCMEnv.out(
		NCLangRes.getInstance().getStrByID("4008busi", "ClientUIInAndOut-000002")/*执行<*/
			+ sTaskHint
			+ NCLangRes.getInstance().getStrByID("4008busi", "ClientUIInAndOut-000003")/*>消耗的时间为：*/
			+ (lTime / 60000)
			+ NCLangRes.getInstance().getStrByID("4008busi", "ClientUIInAndOut-000004")/*分*/
			+ ((lTime / 1000) % 60)
			+ NCLangRes.getInstance().getStrByID("4008busi", "ClientUIInAndOut-000005")/*秒*/
			+ (lTime % 1000)
			+ NCLangRes.getInstance().getStrByID("4008busi", "ClientUIInAndOut-000006")/*毫秒*/);

}
/**
 * Comment
 */
public void TabStateChanged() {
	int i = getUITabPane().getSelectedIndex();
	switch (i) {
		case 0 :
			{
				m_TabFlag = TabIn;
				//置保存和切换状态
				getUIBtnSwitch().setEnabled(true);
				getUIBtnPickAuto().setEnabled(false);

				//置切换显示字
				if (BillMode.List == getChldClientUIIn().getCurPanel()) {
					getUIBtnSave().setEnabled(false);
					getUIBtnSaveSign().setEnabled(false);

					getUIBtnSwitch().setText(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008busi","UPP4008busi-000346")/*@res "表单"*/+ "(L)");
					getUIBtnSwitch().setBounds(792, 229, 97, 25);
					getUIBtnSwitch().updateUI();
				
					getUIBtnSpace().setEnabled(false);
					getUIBtnSerials().setEnabled(false);
					getUIBtnBarcode().setEnabled(false);
				} else {
					getUIBtnSave().setEnabled(!m_bIsTabOutSaved);
					getUIBtnSaveSign().setEnabled(!m_bIsTabOutSaved);
					
					getUIBtnSwitch().setText(nc.ui.ml.NCLangRes.getInstance().getStrByID("scmcommon","UPPSCMCommon-000146")/*@res "列表"*/+ "(L)");
					getUIBtnSwitch().setBounds(792, 229, 97, 25);
				 	getUIBtnSwitch().updateUI();

					getUIBtnSpace().setEnabled(true);
					getUIBtnSerials().setEnabled(true);
					if (!m_bIsOK)//没有保存情况下
						getUIBtnBarcode().setEnabled(true);
					else
						getUIBtnBarcode().setEnabled(false);	

					//直接转库入库单不能编辑条码
					if (m_bIsDirectOut) {
    	                getUIBtnBarcode().setEnabled(false);

    	                //getUIBtnDelRow().setEnabled(false);
                      
						//getChldClientUIIn().getBillCardPanel().setEnabled(alse);
						//getChldClientUIIn().getBillCardPanel().getBillModel().setEnabledAllItems(false);


						String[] keys=new String[]{"cdispatcherid","cproviderid","vnote","cwhsmanagerid","cbizid","cdptid"};
						for(int j=0;j<keys.length;j++){
						if(getChldClientUIIn().getBillCardPanel().getHeadItem(keys[j])!=null){
							getChldClientUIIn().getBillCardPanel().getHeadItem(keys[j]).setEnabled(true);
							getChldClientUIIn().getBillCardPanel().getHeadItem(keys[j]).setEdit(true);

							}
						}
						
						//getChldClientUIIn().setEnabled(false);
						synOut2InBill();
					}
					else {
		                getUIBtnBarcode().setEnabled(true);
						//getChldClientUIIn().getBillCardPanel().getBillModel().setEnabledAllItems(true);
					}

				}

				//置可编辑状态
				if(m_bIsOK)
					getChldClientUIIn().setCardPanelEnable(false);
				break;
			}
		case 1 :
			{
				m_TabFlag = TabOut;
				getUIBtnSwitch().setEnabled(true);

				//置切换显示字
				if (BillMode.List == getChldClientUIOut().getCurPanelParam()) {
					getUIBtnSave().setEnabled(false);
					getUIBtnSaveSign().setEnabled(false);
					getUIBtnPickAuto().setEnabled(false);

					getUIBtnSwitch().setText(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008busi","UPP4008busi-000346")/*@res "表单"*/+ "(L)");
					getUIBtnSwitch().setBounds(792, 229, 97, 25);
	    			getUIBtnSwitch().updateUI();
		
					getUIBtnSpace().setEnabled(false);
					getUIBtnSerials().setEnabled(false);
					getUIBtnBarcode().setEnabled(false);
				} else {
					getUIBtnSave().setEnabled(!m_bIsTabInSaved);
					getUIBtnSaveSign().setEnabled(!m_bIsTabInSaved);
					getUIBtnPickAuto().setEnabled(getUIBtnSave().isEnabled());

					getUIBtnSwitch().setText(nc.ui.ml.NCLangRes.getInstance().getStrByID("scmcommon","UPPSCMCommon-000146")/*@res "列表"*/+ "(L)");
					getUIBtnSwitch().setBounds(792, 229, 97, 25);
					getUIBtnSwitch().updateUI();

	    			getUIBtnSpace().setEnabled(true);
	    			getUIBtnSerials().setEnabled(true);
	    			if (!m_bIsOK)//没有保存情况下
	    				getUIBtnBarcode().setEnabled(true);
	    			else
	    				getUIBtnBarcode().setEnabled(false);

					if (m_bIsDirectOut) {
						getUIBtnSerials().setEnabled(true);
						getUIBtnSpace().setEnabled(true);
            
						//getUIBtnDelRow().setEnabled(false);
					}
				}

				break;
			}
	};
	initConnections();
	return;
}

	private nc.ui.pub.beans.UIButton ivjUIBtnBarcode = null;
	private nc.ui.pub.beans.UIButton ivjUIBtnPickAuto = null;
	//用户名、密码校验UI
	protected nc.ui.scm.pub.AccreditLoginDialog m_AccreditLoginDialog;
	private boolean m_bIsDirectOut = false; // 是否直接出库: 需要同时保存其它出, 其它入库单.
	private String m_sBillDate = null; //登陆日期
	private boolean m_bIsSignOK = false;
	public java.lang.String m_sCorpID;
	public String m_sCorpName;
	private String m_sLogDate = null; //登陆日期
	//特殊单 VO add by hanwei 2003-11-03
	protected AggregatedValueObject m_voSpBill;
	public static final nc.vo.pub.lang.UFDouble UFDZERO = new nc.vo.pub.lang.UFDouble(0.0);

/**
 * 李俊
 * 功能：
 * 参数：
 * 返回：
 * 例外：
 * 日期：(2005-1-19 11:39:16)
 * 修改日期，修改人，修改原因，注释标志：
 *
 */
private void exeAllFormulas(nc.ui.ic.pub.bill.GeneralBillClientUI ui,ArrayList alVOs) {

  if (alVOs == null || alVOs.size()<=0) return;
  ui.setAlistDataByFormula(-1, alVOs);
  GeneralBillHeaderVO[] voaHeads = new GeneralBillHeaderVO[alVOs.size()];
  ArrayList<GeneralBillItemVO> listitemvos = new ArrayList<GeneralBillItemVO>();
  GeneralBillItemVO[] itemvos = null;
  GeneralBillVO billvo = null;
  for (int i = 0; i < alVOs.size(); i++) {
    billvo = (GeneralBillVO)alVOs.get(i);
    voaHeads[i] = billvo.getHeaderVO();
    //  (GeneralBillHeaderVO) ((AggregatedValueObject) alVOs.get(i)).getParentVO();
    itemvos = billvo.getItemVOs();
    for(GeneralBillItemVO itemvo :itemvos)
      listitemvos.add(itemvo);
  }
  ui.setListHeadData(voaHeads);
  if(listitemvos.size()>0){
    itemvos = listitemvos.toArray(new GeneralBillItemVO[itemvos.length]);
//  执行批次号档案公式
    try {
      BatchCodeDefSetTool.execFormulaForBatchCode(itemvos);
    }catch(Exception e) {
      nc.vo.scm.pub.SCMEnv.error(e);
    }
  }
}

/**
 * 此处插入方法说明。
   获得权限认证UI
 * 创建日期：(2004-4-19 14:11:06)
 * @return nc.ui.scm.pub.AccreditLoginDialog
 */
public nc.ui.scm.pub.AccreditLoginDialog getAccreditLoginDialog() {
	if (m_AccreditLoginDialog==null)
	m_AccreditLoginDialog=new AccreditLoginDialog();
	return m_AccreditLoginDialog;
}

/**
 *

 * 创建者：王乃军
 * 功能： 得到检查后的单据们

 * 参数：对象将被修改。
 * 返回：vo[] or null
 * 例外：
 * 日期：(2001-5-16 15:27:06)
 * 修改日期，修改人，修改原因，注释标志：
 *
 */
private GeneralBillVO[] getCheckedInBills() throws Exception {
	GeneralBillVO[] voaIn = null;
	//==============================================
	//VO校验
	if (m_inVOs != null && m_inVOs.size() != 0) {

		//滤去表单形式下的空行
		getChldClientUIIn().filterNullLine();

		//VO校验准备数据
		GeneralBillVO voNowBill = getInVO();
		
		if (voNowBill != null && (voNowBill.getItemVOs() == null || voNowBill.getItemVOs().length == 0))
			throw new Exception(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008busi","UPP4008busi-000160")/*@res "表体为空！您可以关闭本界面，然后重新进入..."*/);
		int iNowSelBill = getChldClientUIIn().getLastSelListHeadRow();
		//刷新list中的单据
		m_inVOs.set(iNowSelBill, voNowBill);
		getChldClientUIIn().setAllData(m_inVOs);
		//把单据写到界面上，调用需要的界面检查方法。
		GeneralBillVO voTempBill = null;
		GeneralBillHeaderVO voHead=null;
		for (int i = 0; i < m_inVOs.size(); i++) {
            setVOStatusNew(m_inVOs);
			voTempBill =(GeneralBillVO) m_inVOs.get(i);
			//通过平台实现保存即签字，置入签字人字段，在出入库单保存时清除之。
			//支持平台，clone一个，以便于以后的处理，同时防止修改了m_voBill
			voHead =voTempBill.getHeaderVO();
			voHead.setPk_corp(m_sCorpPK);
			//签字人
		//	voHead.setCregister(m_sUserID);
			//因为登录日期和单据日期是可以不同的，所以必须要登录日期。
		//	voHead.setDaccountdate(new nc.vo.pub.lang.UFDate(m_sLogDate));
			//vo可能要传给平台，所以要做成和签字后的单据
		//	voHead.setFbillflag(new Integer(nc.vo.ic.pub.bill.BillStatus.SIGNED));
			voHead.setCoperatoridnow(m_sUserID); //当前操作员2002-04-10.wnj
			voHead.setAttributeValue("clogdatenow", m_sLogDate); //当前登录日期2003-01-05

			voTempBill = (GeneralBillVO) ((GeneralBillVO) m_inVOs.get(i)).clone();
			getChldClientUIIn().setBillVO(voTempBill, false);
			getChldClientUIIn().setLastSelListHeadRow(i);
			if (!getChldClientUIIn().setBillCodeAuto()) {
				throw new Exception(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008busi","UPP4008busi-000161")/*@res "单据号获取失败!"*/);
			}
			//直接出库需要检查其它入库单的是否货位仓库,是否填写货位.
			//if (m_bIsDirectOut) {
				//try {
					//nc.vo.ic.pub.check.VOCheck.checkLocatorInput(voTempBill, new Integer(InOutFlag.IN));
				//} catch (ICLocatorException ex) {
					////显示提示
					////String sErrorMessage = GeneralMethod.getBodyErrorMessage(getBillCardPanel(), e.getErrorRowNums(), e.getHint());

					////return null;
					//throw new Exception("直接转库产生的其它出库单,"+ex.getHint()+" 请分配入库单货位! ");
				//}

			//} else {

				if (!getChldClientUIIn().checkVO()) {
					throw new Exception(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008busi","UPP4008busi-000162")/*@res "请输入完整信息!"*/);
				}
			//}
			//？？？wnj m_inVOs.set(i, (GeneralBillVO) getinVOs().clone());
		}
		//转库单只支持一个
		if (m_sSrcBillTypeCode.equals(nc.vo.ic.pub.BillTypeConst.m_transfer)) {

			voaIn = new GeneralBillVO[1];
			voaIn[0] = voNowBill;
		} else {
			voaIn = new GeneralBillVO[m_inVOs.size()];
			for (int i = 0; i < m_inVOs.size(); i++) {
				voaIn[i] = (GeneralBillVO) m_inVOs.get(i);
			}
		}
		
		for(GeneralBillVO voaInVO : voaIn )
			voaInVO.setSaveBadBarcode(getChldClientUIIn().getbBadBarcodeSave());
	}
	return voaIn;
}

/**

 * 创建者：王乃军
 * 功能： 得到检查后的单据们

 * 参数：对象将被修改。
 * 返回：ArrayList:
	 0:  是否成功
	 1:	 vo[]:如果 0 是 false,返回空，否则返回vo[]

 * 例外：
 * 日期：(2001-5-16 15:27:06)
 * 修改日期，修改人，修改原因，注释标志：
 *
 */
private GeneralBillVO[] getCheckedOutBills() throws Exception {
	//==============================================
	//VO校验
	GeneralBillVO[] voaOut = null;
	if ((m_outVOs != null) && (m_outVOs.size() != 0)) {


//  VO校验准备数据
    GeneralBillVO voNowBill = getOutVO();
    
		//滤去表单形式下的空行
		getChldClientUIOut().filterNullLine();


		if (voNowBill != null
			&& (voNowBill.getItemVOs() == null || voNowBill.getItemVOs().length == 0))
			throw new Exception(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008busi","UPP4008busi-000160")/*@res "表体为空！您可以关闭本界面，然后重新进入..."*/);
    
    voNowBill.getChildrenVO()[0].getAttributeValue("cinventorycode");
    voNowBill.getChildrenVO()[0].getAttributeValue("cinventoryid");

		int iNowSelBill = getChldClientUIOut().getLastSelListHeadRow();
		//刷新list中的单据
		m_outVOs.set(iNowSelBill, voNowBill);
		getChldClientUIOut().setAllData(m_outVOs);
		//把单据写到界面上，调用需要的界面检查方法。
		GeneralBillVO voTempBill = null;
		GeneralBillHeaderVO voHead=null;
		for (int i = 0; i < m_outVOs.size(); i++) {
			setVOStatusNew(m_outVOs);
			voTempBill =(GeneralBillVO) m_outVOs.get(i);
			//通过平台实现保存即签字，置入签字人字段，在出入库单保存时清除之。
			//支持平台，clone一个，以便于以后的处理，同时防止修改了m_voBill
			voHead =voTempBill.getHeaderVO();
			voHead.setPk_corp(m_sCorpPK);
			//签字人
		//	voHead.setCregister(m_sUserID);
			//因为登录日期和单据日期是可以不同的，所以必须要登录日期。
		//	voHead.setDaccountdate(new nc.vo.pub.lang.UFDate(m_sLogDate));
			//vo可能要传给平台，所以要做成和签字后的单据
		//	voHead.setFbillflag(new Integer(nc.vo.ic.pub.bill.BillStatus.SIGNED));
			voHead.setCoperatoridnow(m_sUserID); //当前操作员2002-04-10.wnj
			voHead.setAttributeValue("clogdatenow", m_sLogDate); //当前登录日期2003-01-05

			voTempBill = (GeneralBillVO) ((GeneralBillVO) m_outVOs.get(i)).clone();

			getChldClientUIOut().setBillVO(voTempBill, false);
			getChldClientUIOut().setLastSelListHeadRow(i);
			if (!getChldClientUIOut().setBillCodeAuto()) {

				throw new Exception(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008busi","UPP4008busi-000161")/*@res "单据号获取失败!"*/);
			}
			if (!getChldClientUIOut().checkVO()) {

				throw new Exception(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008busi","UPP4008busi-000162")/*@res "请输入完整信息!"*/);

			}
			//？？？wnj m_outVOs.set(i, (GeneralBillVO) getoutVOs().clone());
		}
		//转库单只支持一个
		if (m_sSrcBillTypeCode.equals(nc.vo.ic.pub.BillTypeConst.m_transfer)) {

			voaOut = new GeneralBillVO[1];
			voaOut[0] = voNowBill;
		} else {
			voaOut = new GeneralBillVO[m_outVOs.size()];
			for (int i = 0; i < m_outVOs.size(); i++) {
				voaOut[i] = (GeneralBillVO) m_outVOs.get(i);
			}
		}
		
		for(GeneralBillVO voaOutVO :voaOut)
			voaOutVO.setSaveBadBarcode(getChldClientUIOut().getbBadBarcodeSave());
	}
	return voaOut;
}

/**
 * 此处插入方法说明。
 * 创建日期：(2004-4-19 20:26:37)
 * @return java.lang.String
 */
public java.lang.String getCorpID() {
	return m_sCorpID;
}

/**
 * 此处插入方法说明。
   获得特殊单VO
 * 创建日期：(2003-11-3 16:50:29)
 * @return nc.vo.ic.pub.bill.SpecialBillVO
 */
public AggregatedValueObject getSpBill() {
	return m_voSpBill;
}

/**
 * 返回 UIBtnBarcode 特性值。
 * @return nc.ui.pub.beans.UIButton
 */
/* 警告：此方法将重新生成。 */
private nc.ui.pub.beans.UIButton getUIBtnBarcode() {
	if (ivjUIBtnBarcode == null) {
		try {
			ivjUIBtnBarcode = new nc.ui.pub.beans.UIButton();
			ivjUIBtnBarcode.setName("UIBtnBarcode");
			ivjUIBtnBarcode.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID("40080402","UPT40080402-000027")/*@res "条码编辑"*/+ "(B)");
			ivjUIBtnBarcode.setBounds(792, 262, 97, 25);
			ivjUIBtnBarcode.setMnemonic('b');
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjUIBtnBarcode;
}

/**
 * 返回 UIButton1 特性值。
 * @return nc.ui.pub.beans.UIButton
 */
/* 警告：此方法将重新生成。 */
private nc.ui.pub.beans.UIButton getUIBtnPickAuto() {
	if (ivjUIBtnPickAuto == null) {
		try {
			ivjUIBtnPickAuto = new nc.ui.pub.beans.UIButton();
			ivjUIBtnPickAuto.setName("UIBtnPickAuto");
			ivjUIBtnPickAuto.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID("40080802","UPT40080802-000032")/*@res "自动拣货"*/ + "(A)");
      ivjUIBtnPickAuto.setMnemonic('a');      
			ivjUIBtnPickAuto.setBounds(792, 97, 97, 25);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjUIBtnPickAuto;
}

/**
 * 创建者：保存基本方法中的核心方法
 * 功能：确认（保存）处理
 * 参数：无
 * 例外：
 * 日期：(2004-4-1 9:23:32)
 * 修改日期，修改人，修改原因，注释标志：
   2004-4-1   韩卫
 */
protected void onSaveBaseKernel(GeneralBillVO[] voaAllBill,String sAccreditUserID, String sBillDate,Boolean isSign)
	throws Exception {

	try {
		nc.vo.sm.log.OperatelogVO log = new nc.vo.sm.log.OperatelogVO();
		log.setCompanyname(m_sCorpName);
		if (!nc.ui.pub.ClientEnvironment.getInstance().isInDebug())
		log.setEnterip(nc.ui.sm.cmenu.Desktop.getApplet().getParameter("USER_IP"));
		log.setPKCorp(m_sCorpID);
		if (voaAllBill!=null && voaAllBill.length>0)
		{
			for (int i=0;i<voaAllBill.length;i++)
			{
				voaAllBill[i].setAccreditUserID(sAccreditUserID);
				voaAllBill[i].setOperatelogVO(log);
				
				int iLocatorMgt = voaAllBill[i].getHeaderVO().getIsLocatorMgt().intValue();
				LocatorVO[] voaLoc = null;
				GeneralBillItemVO[] voInItems= voaAllBill[i].getItemVOs();
				if (iLocatorMgt == 1){
					boolean isErr = false;
					for(int j=0;j<voInItems.length;j++){
						voaLoc =voInItems[j].getLocator();
						if (voaLoc!=null && voaLoc.length>1){
							isErr = true ;
							break;
						}
					}
					if (isErr){
						//modified by lirr 货位数据不能超过一行
						throw new BusinessException("货位数据不能超过一行");
					}
				}
			}
		}
		
		m_voSpBill.getParentVO().setAttributeValue("coperatoridnow", m_sUserID);

		ArrayList osPrimaryKey = null;
		if (m_sSrcBillTypeCode.equals(nc.vo.ic.pub.BillTypeConst.m_check)) {
			//盘点
			nc.ui.ic.ic261.SpecialHHelper.checkIsAlreadySigned(m_sOldPK);
			osPrimaryKey =
				(ArrayList) nc.ui.pub.pf.PfUtilClient.processAction("ADJUST", m_sSrcBillTypeCode, sBillDate, m_voSpBill, voaAllBill);
		} else if (m_sSrcBillTypeCode.equals(nc.vo.ic.pub.BillTypeConst.m_transfer)) {
			//如果是直接出库,并且入库仓库是货位管理,则需要置参数 m_voSpBill 或 null ,
			//后台动作脚本中根据getVo() 的空否? 处理自动拣货
			//转库单
			osPrimaryKey =
				(ArrayList) nc.ui.pub.pf.PfUtilClient.processAction("SAVEOTHER", m_sSrcBillTypeCode, sBillDate, m_voSpBill, voaAllBill);
		
		} else if (m_sSrcBillTypeCode.equals(nc.vo.ic.pub.BillTypeConst.m_AllocationOrder)) {
			if (voaAllBill != null) {
				osPrimaryKey =
					(ArrayList) nc.ui.pub.pf.PfUtilClient.processAction("SAVEOTHER", m_sSrcBillTypeCode, sBillDate, null, voaAllBill);
			}

		} else if (
			m_sSrcBillTypeCode.equals(nc.vo.ic.pub.BillTypeConst.m_saleOut)
				|| m_sSrcBillTypeCode.equals(nc.vo.ic.pub.BillTypeConst.m_purchaseIn)) {

			//2004-8-03 ydy 分步保存，不是同一个事务 陈倪娜 增加返回值 2009-09-23
			osPrimaryKey=(ArrayList)nc.ui.pub.pf.PfUtilClient.processAction("SAVEOTHER", nc.vo.ic.pub.BillTypeConst.m_purchaseIn, sBillDate, m_voSpBill, voaAllBill);

			//if (voaAllBill != null) {
				//for (int i = 0; i < voaAllBill.length; i++) {
					//nc.ui.pub.pf.PfUtilClient.processAction("SAVE", nc.vo.ic.pub.BillTypeConst.m_otherIn, sBillDate, voaAllBill[i]);
				//}
			//}

		} else {
			//组装、拆卸、形态转换单
			nc.ui.ic.ic231.SpecialHHelper.checkIsAlreadySigned(m_sOldPK);
			osPrimaryKey =
				(ArrayList) nc.ui.pub.pf.PfUtilClient.processAction("SAVEOTHER", m_sSrcBillTypeCode, sBillDate, m_voSpBill, voaAllBill);
		}
		
		m_bIsSignOK = false;
		//2010-11-09 MeiChao Begin 当单据保存完毕之后,将费用信息保存入
		if(this.m_voSpBill!=null&&voaAllBill.length==2){//只允许单张形态转换.(即只有1张出库,1张入库)
			/**
			 * 根据setvo()方法传进的形态转换单VO,获取其PK
			 * 并通过形态转换单的PK 查询出其生成的其他入库单PK,
			 * 再将费用信息以其他入库单子表的形式存入数据库
			 */
			//获取当前形态转换单PK
			String specialBillPK=this.m_voSpBill.getParentVO().getPrimaryKey();
			String queryPKSQL="select distinct t.cgeneralhid " +
					"from ic_general_b t where " +
					"t.cbodybilltypecode='4A' and t.cfirsttype='4N' " +
					"and t.cfirstbillhid='"+specialBillPK+"' and t.csourcetype='4N' and " +
					"t.csourcebillhid='"+specialBillPK+"' and t.dr=0";
			//获取客户端查询接口
			IUAPQueryBS iQueryService=(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
			//查询形态转换生成的其他入库单PK
			Object generalBillPK=iQueryService.executeQuery(queryPKSQL, new ColumnProcessor());

			if(generalBillPK!=null){//如果成功查询到,其他入库单PK,那么保存费用信息.
				for(int i=0;i<this.expenseVOs.length;i++){//遍历费用信息,将其他入库单PK存入其VO中
					expenseVOs[i].setCbillid(generalBillPK.toString());
					expenseVOs[i].setVdef10("4A");//自定义字段10: 所属单据类型
				}
				//
				JJIcScmPubHelper expenseHelper=new JJIcScmPubHelper();
				//插入费用信息VO
				expenseHelper.insertSmartVOs(expenseVOs);
				Logger.debug("形态转换单->其他入库单转化,插入费用信息成功!");
			}
		}
		//2010-11-9 MeiChao End 当单据保存完毕之后,将费用信息保存入
		
		
		if (isSign){
			//修改人：刘家清 修改时间：2008-11-20 下午03:07:21 修改原因：把数据刷新到要进行下一步操作的VO中。
			if (null != osPrimaryKey)
				for(int i = 0 ;i < voaAllBill.length ; i++){
					if (null == osPrimaryKey.get(i) || !(osPrimaryKey.get(i) instanceof ArrayList))
						continue;
					ArrayList alPK =(ArrayList) osPrimaryKey.get(i); 
					GeneralBillVO billVO = voaAllBill[i];
					if (alPK == null || alPK.size() < 3 || alPK.get(1) == null
							|| alPK.get(2) == null) {
					} else {
						int iRowCount = billVO.getItemCount();
						ArrayList alMyPK = (ArrayList) alPK.get(1);
						if (alMyPK == null || alMyPK.size() < (iRowCount + 1)
								|| alMyPK.get(0) == null || alMyPK.get(1) == null) {
	
						} else {
							// 表头的OID
							String m_sCurBillOID = (String) alMyPK.get(0);
		
							billVO.getParentVO().setPrimaryKey(m_sCurBillOID);
							SMGeneralBillVO smbillvo = null;
							smbillvo = (SMGeneralBillVO) alPK.get(2);
						
							billVO.setSmallBillVO(smbillvo);
	
						}
					}
				}
			
			try{
				/*ArrayList<GeneralBillVO> newvoaAllBill = 	(java.util.ArrayList<GeneralBillVO>)nc.ui.ic.pub.tools.GenMethod.callICEJBService("nc.bs.ic.pub.bill.GeneralBillDMO", "queryBillBySourcePks", 
				          new Class[]{String.class,String[].class}, new Object[]{m_sSrcBillTypeCode,new String[]{m_voSpBill.getParentVO().getPrimaryKey()}});*/
			ArrayList<GeneralBillVO> listInBills = new ArrayList<GeneralBillVO>();
			ArrayList<GeneralBillVO> listOutBills = new ArrayList<GeneralBillVO>();
			for(GeneralBillVO vo:voaAllBill){
				setBillBCVOStatus(vo,nc.vo.pub.VOStatus.UNCHANGED);
				vo = getAuditVO(vo,null);
				if (vo.getCbilltypecode() != null && "4A".equals(vo.getCbilltypecode()))
					listInBills.add(vo);
				else if (vo.getCbilltypecode() != null && "4I".equals(vo.getCbilltypecode()))
					listOutBills.add(vo);
			}
			
			/*if (m_sSrcBillTypeCode.equals(nc.vo.ic.pub.BillTypeConst.m_disassembly)
					||m_sSrcBillTypeCode.equals(nc.vo.ic.pub.BillTypeConst.m_disassembly)){*/
				if (listOutBills.size() > 0){
					GeneralBillVO[] voOutBills = new GeneralBillVO[listOutBills.size()];
					listOutBills.toArray(voOutBills);
					nc.ui.pub.pf.PfUtilClient.processBatch("SIGN", "4I",
							m_sLogDate,voOutBills);
				}
				if (listInBills.size() > 0){
					GeneralBillVO[] voInBills = new GeneralBillVO[listInBills.size()];
					listInBills.toArray(voInBills);
					nc.ui.pub.pf.PfUtilClient.processBatch("SIGN", "4A",
							m_sLogDate,voInBills);
				}

			/*}
			else{
				if (listInBills.size() > 0){
					GeneralBillVO[] voInBills = new GeneralBillVO[listInBills.size()];
					listInBills.toArray(voInBills);
					nc.ui.pub.pf.PfUtilClient.processBatch("SIGN", "4A",
							m_sLogDate,voInBills);
				}
				if (listOutBills.size() > 0){
					GeneralBillVO[] voOutBills = new GeneralBillVO[listOutBills.size()];
					listOutBills.toArray(voOutBills);
					nc.ui.pub.pf.PfUtilClient.processBatch("SIGN", "4I",
							m_sLogDate,voOutBills);
				}
			}*/
			
			m_bIsSignOK = true;
			
			//2010-11-09 MeiChao Begin 
			/**
			 * 2010-11-09 MeiChao 形态转换: 保存并签字功能扩展
			 * 在签字后,由于如果其他入库单中含费用信息,需要在签字时,同时传暂估应付及存货核算
			 * 需要在此加入此段代码.
			 */
			//只允许在有且仅有1张其他入库单的时候执行对费用信息的传暂估功能
			if(listInBills.size()==1){
				
			}
			
			//2010-11-09 MeiChao end 
			
			}catch(Exception e){
				m_bIsSignOK = false;
				if (e != null && e.getClass() == nc.vo.ic.pub.exp.OtherOut4MException.class)
					showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008busi", "UPP4008busi-000401")/** @res "拆卸单生成的其他入库单以下单据行实际入库的子件数量超过按实际出库套件数量拆分对应的子件数量"*/ + e.getMessage());
				else
					showErrorMessage(e.getMessage());	
			}
			
		}

	} catch (RightcheckException e) {
		showErrorMessage(e.getMessage() + nc.ui.ml.NCLangRes.getInstance().getStrByID("4008busi","UPP4008busi-000164")/*@res "。"*/+"\n"+nc.ui.ml.NCLangRes.getInstance().getStrByID("4008busi","UPP4008busi-000165")/*@res "权限校验不通过，保存失败。"*/);
		getAccreditLoginDialog().setCorpID(m_sCorpID);
		getAccreditLoginDialog().clearPassWord();
		if (getAccreditLoginDialog().showModal() == nc.ui.pub.beans.UIDialog.ID_OK) {
			String sUserID = getAccreditLoginDialog().getUserID();
			if (sUserID == null) {
				throw new Exception(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008busi","UPP4008busi-000165")/*@res "权限校验不通过，保存失败。"*/);
			} else {
				for(GeneralBillVO voaBill :voaAllBill)
					voaBill.setAccreditBarcodeUserID(e.getFunCodeForRightCheck(), sUserID);
				//onSaveBaseKernel(voaAllBill,sUserID, sBillDate);
				onSaveBaseKernel(voaAllBill,m_sUserID, sBillDate,isSign);
			}
		} else {
			throw new Exception(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008busi","UPP4008busi-000165")/*@res "权限校验不通过，保存失败。"*/);

		}

	} catch (Exception e) {
		throw e;
	}

}

public void setBillBCVOStatus(GeneralBillVO bvo, int Status) {

	if (bvo != null && bvo.getItemVOs() != null) {
		GeneralBillItemVO itemVO = null;
		BarCodeVO[] bcvos = null;
		BarCodeVO[] bcvosTemp = null;
		java.util.ArrayList alBarcodeVO = null;
		for (int i = 0; i < bvo.getItemVOs().length; i++) {
			itemVO = bvo.getItemVOs()[i];
			bcvos = itemVO.getBarCodeVOs();
			if (bcvos != null) {
				alBarcodeVO = new java.util.ArrayList();
				for (int j = 0; j < bcvos.length; j++) {
					if (bcvos[j].getStatus() != nc.vo.pub.VOStatus.DELETED) {
						bcvos[j].setStatus(Status);
						alBarcodeVO.add(bcvos[j]);
					}
				}
				if (alBarcodeVO.size() > 0) {
					bcvosTemp = new BarCodeVO[alBarcodeVO.size()];
					alBarcodeVO.toArray(bcvosTemp);
					itemVO.setBarCodeVOs(bcvosTemp);
				} else {
					itemVO.setBarCodeVOs(null);
				}
			}

		}

	}

}

  public GeneralBillVO getAuditVO(GeneralBillVO voAudit,UFDateTime sysdatetime) {
    
//  设置条码状态未没有被修改
    setBillBCVOStatus(voAudit, nc.vo.pub.VOStatus.UNCHANGED);
    // 支持平台，clone一个，以便于以后的处理，同时防止修改了m_voBill
    GeneralBillHeaderVO voHead = voAudit.getHeaderVO();

    // 签字人
    voHead.setCregister(m_sUserID);
    // 可以不是当前登录单位的单据，所以不需要修改单据。
    // voHead.setPk_corp(m_sCorpID);
    voHead.setDaccountdate(new nc.vo.pub.lang.UFDate(m_sLogDate));
    //voHead.setAttributeValue("taccounttime", sysdatetime.toString()); // 签字时间//zhy2005-06-15签字时间=登陆日期+系统时间

    // vo可能要传给平台，所以要做成和签字后的单据
    // voHead.setFbillflag(new
    // Integer(nc.vo.ic.pub.bill.BillStatus.SIGNED));
    voHead.setCoperatoridnow(m_sUserID); // 当前操作员2002-04-10.wnj
    voHead.setAttributeValue("clogdatenow", m_sLogDate); // 当前登录日期

    voAudit.setParentVO(voHead);

    // 根据仓库解析获得仓库是否存货核算属性 add by hanwei 2004-4-30
    getGenBillUICtl().setBillIscalculatedinvcost(voAudit);

    // 平台：需要表体带表头PK
    GeneralBillItemVO voaItem[] = voAudit.getItemVOs();
    int iRowCount = voAudit.getItemCount();
    for (int i = 0; i < iRowCount; i++) {
      // 表头PK
      voaItem[i].setCgeneralhid(voHead.getPrimaryKey());
      // set delete flag------- obligatory for ts test.
    }
    voAudit.setChildrenVO(voaItem);

    voAudit.setStatus(nc.vo.pub.VOStatus.UNCHANGED);
    setBillBCVOStatus(voAudit, nc.vo.pub.VOStatus.UNCHANGED);
    
    voAudit.setIsCheckCredit(true);
    voAudit.setIsCheckPeriod(true);
    voAudit.setIsCheckAtp(true);
    
    //nc.vo.sm.log.OperatelogVO log = getNormalOperateLog();
    voAudit.setAccreditUserID(m_sUserID);
   // voAudit.setOperatelogVO(log);
    
    //帐期、信用信息
    voAudit.m_iActionInt = nc.vo.scm.pub.bill.CreditConst.ICREDIT_ACT_OTHER;
    voAudit.m_sActionCode = "SIGN";
    
    return voAudit;

  }
  public GeneralBillUICtl m_GenBillUICtl;
	public GeneralBillUICtl getGenBillUICtl() {
		if (m_GenBillUICtl == null)
			m_GenBillUICtl = new GeneralBillUICtl();
		return m_GenBillUICtl;
	}  

/**
 * 此处插入方法说明。
 * 创建日期：(2004-4-19 20:35:29)
 * @return java.lang.String
 * @param voaAllBill nc.vo.ic.pub.bill.GeneralBillVO[]
 * 
 * 修改人：刘家清 修改时间：2008-11-17 下午04:08:40 修改原因：先出库单后入库单。
 */
protected GeneralBillVO[]  onSaveGetAllBillVO() throws Exception {
	//单据日期
		//VO校验
		GeneralBillVO[] voaAllBill=null;
		GeneralBillVO[] voaOutBill = getCheckedOutBills();
		//有错单，返回
		if (m_outVOs != null && m_outVOs.size() != 0 && (voaOutBill == null || voaOutBill.length == 0))
			return voaAllBill;
		GeneralBillVO[] voaInBill = null;
		//如果不是直接出库则
		if (!m_bIsDirectOut) {
			try {
				voaInBill = getCheckedInBills();
			} catch (Exception e) {
				
				SCMEnv.error(e);
				throw new Exception(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008busi","UPP4008busi-000167")/*@res "请填写完整的其它入库单，"*/ + e.getMessage());

			}
			//有错单 ， 返回
			if (m_inVOs != null && m_inVOs.size() != 0 && (voaInBill == null || voaInBill.length == 0))
				return voaAllBill;
		} else {
			//入库=出库
			synOut2InBill();//如果保存时同步入库和出库,那么在入库单上修改的信息可能被同步成出库单上的信息,发生数据错误
			try {
				voaInBill = getCheckedInBills();
			} catch (Exception e1) {
				nc.vo.scm.pub.SCMEnv.error(e1);
				throw new Exception(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008busi","UPP4008busi-000167")/*@res "请填写完整的其它入库单，"*/ + e1.getMessage());

			}
			if (voaInBill == null || voaInBill.length == 0)
				return voaAllBill;
		}

		//入库vo个数
		int iOutBillCount = voaOutBill == null ? 0 : voaOutBill.length;
		//保存

		if (m_bIsDirectOut) {

			//--
			voaAllBill = new GeneralBillVO[iOutBillCount + (voaInBill == null ? 0 : voaInBill.length)];
			if (voaOutBill != null) {
				for (int i = 0; i < voaOutBill.length; i++) {
					//置出单据日期
					if (m_sBillDate == null) {
						if (voaOutBill[i].getHeaderVO().getDbilldate() != null) {
							m_sBillDate = voaOutBill[i].getHeaderVO().getDbilldate().toString().trim();
							if (m_sBillDate.length() == 0)
								m_sBillDate = null;
						}

					}
					voaAllBill[i] = voaOutBill[i];
					voaAllBill[i].getHeaderVO().setCoperatoridnow(m_sUserID);
				}
			}
			if (voaInBill != null) {
				for (int i = 0; i < voaInBill.length; i++) {
					//置入单据日期
					if (m_sBillDate == null) {
						if (voaInBill[i].getHeaderVO().getDbilldate() != null) {
							m_sBillDate = voaInBill[i].getHeaderVO().getDbilldate().toString().trim();
							if (m_sBillDate.length() == 0)
								m_sBillDate = null;
						}

					}
					for (int j = 0; j < voaInBill[i].getItemCount(); j++) {
						//入库数量
						voaInBill[i].setItemValue(j, "ninnum", voaOutBill[i].getItemValue(j, "noutnum"));
						//入库辅数量
						voaInBill[i].setItemValue(j, "ninassistnum", voaOutBill[i].getItemValue(j, "noutassistnum"));
						nc.vo.ic.pub.sn.SerialVO[] svo = voaOutBill[i].getItemVOs()[j].getSerial();
						nc.vo.ic.pub.sn.SerialVO[] svobak = voaInBill[i].getItemVOs()[j].getSerial();
						if (svo != null && voaInBill[i].getHeaderVO().getIsLocatorMgt().intValue()!=1) {
							svobak = new nc.vo.ic.pub.sn.SerialVO[svo.length];
							for (int k = 0; k < svobak.length; k++) {
								if (svobak[k] == null)
									svobak[k] = new nc.vo.ic.pub.sn.SerialVO();
								svobak[k].setStatus(2);
								svobak[k].setSnStatus(new Integer(-1));
								svobak[k].setCserialid(null);
								svobak[k].setVserialcode(svo[k].getVserialcode());
								svobak[k].setDbillindate(svo[k].getDbilloutdate());
								//svobak[k].setDbilloutdate(null);

							}
						voaInBill[i].getItemVOs()[j].setSerial(svobak);
						}


						//直接转库，把条码直接转入 add by hanwei 2004-04-12
						nc.vo.ic.pub.bc.BarCodeVO[] outBarcodeVOs = voaOutBill[i].getItemVOs()[j].getBarCodeVOs();
						if (outBarcodeVOs != null) {
							voaInBill[i].getItemVOs()[j].setBarCodeVOs(outBarcodeVOs);
						}
					}
					if (m_inVOs != null) {
						voaInBill[i].setHeaderValue("cwarehouseid", ((GeneralBillVO) m_inVOs.get(i)).getHeaderValue("cwarehouseid"));
					}
					//将其它出库单据VO转换成其它入库单据VO
					//voaAllBill[i + iInBillCount] = transferOut2InVO(voaInBill[i]);
					voaAllBill[i + iOutBillCount] = voaInBill[i];
					//
					voaAllBill[i + iOutBillCount].getHeaderVO().setCoperatoridnow(m_sUserID);
				}
			}

		}
		//不是直接出库
		else {
			voaAllBill = new GeneralBillVO[iOutBillCount + (voaInBill == null ? 0 : voaInBill.length)];
			
			if (voaOutBill != null) {
				for (int i = 0; i < voaOutBill.length; i++) {
					//置入单据日期
					if (m_sBillDate == null) {
						if (voaOutBill[i].getHeaderVO().getDbilldate() != null) {
							m_sBillDate = voaOutBill[i].getHeaderVO().getDbilldate().toString().trim();
							if (m_sBillDate.length() == 0)
								m_sBillDate = null;
						}

					}
					voaAllBill[i] = voaOutBill[i];
					voaAllBill[i].getHeaderVO().setCoperatoridnow(m_sUserID);
				}
			}
			
			
			if (voaInBill != null) {
				for (int i = 0; i < voaInBill.length; i++) {
					//置入单据日期
					if (m_sBillDate == null) {
						if (voaInBill[i].getHeaderVO().getDbilldate() != null) {
							m_sBillDate = voaInBill[i].getHeaderVO().getDbilldate().toString().trim();
							if (m_sBillDate.length() == 0)
								m_sBillDate = null;
						}

					}
					voaAllBill[i + iOutBillCount] = voaInBill[i];
					voaAllBill[i + iOutBillCount].getHeaderVO().setCoperatoridnow(m_sUserID);
				}
			}


		}

	return voaAllBill;
}

/**
 * 此处插入方法说明。
   设置保存后的状态
 * 创建日期：(2004-4-19 20:30:17)
 */
protected void onSaveSetUI(Boolean isSign) {
		m_bIsTabInSaved = true;
		m_bIsTabOutSaved = true;
		TabStateChanged();

	    //置保存、货位、序列号、删除和切换按钮
		getUIBtnSave().setEnabled(false);
		getUIBtnSaveSign().setEnabled(false);
		//getUIBtnSwitch().setEnabled(true);
		getUIBtnSpace().setEnabled(true);
		getUIBtnSerials().setEnabled(true);
		getUIBtnDelRow().setEnabled(false);
		getUIBtnBarcode().setEnabled(false);

		//置关闭按钮
		getbtnClose().setEnabled(
			!(((m_bIsTabInHaveValue) && (m_bIsTabInSaved == false)) || ((m_bIsTabOutHaveValue) && (m_bIsTabOutSaved == false))));
		getUIBtnCancel().setEnabled((m_bIsTabInSaved == false) && (m_bIsTabOutSaved == false));

		getChldClientUIIn().setCardPanelEnable(!m_bIsTabOutSaved);
		getChldClientUIOut().setCardPanelEnable(!m_bIsTabInSaved);
		getChldClientUIIn().setCardMode(BillMode.Browse);
		getChldClientUIOut().setCardMode(BillMode.Browse);

		//ljun单据保存后，不能条码编辑：直接保存不能获得单据ID
		getUIBtnBarcode().setEnabled(false);
		getUIBtnBarcode().setEnabled(false);
		if (isSign){
			if (m_bIsSignOK)
				nc.ui.pub.beans.MessageDialog.showHintDlg(this, NCLangRes.getInstance().getStrByID("4008busi", "ClientUIInAndOut-000007")/*保存成功&签字成功*/, NCLangRes.getInstance().getStrByID("4008busi", "ClientUIInAndOut-000008")/*所有单据均已保存并且签字成功！*/);
			else
				nc.ui.pub.beans.MessageDialog.showHintDlg(this, NCLangRes.getInstance().getStrByID("4008busi", "ClientUIInAndOut-000009")/*保存成功&签字失败*/, NCLangRes.getInstance().getStrByID("4008busi", "ClientUIInAndOut-000010")/*所有单据均已保存但是签字失败！*/);
		}
		else
			nc.ui.pub.beans.MessageDialog.showHintDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON","UPPSCMCommon-000006")/*@res "保存成功"*/, nc.ui.ml.NCLangRes.getInstance().getStrByID("4008busi","UPP4008busi-000168")/*@res "所有单据均已保存！"*/);

		m_bIsOK = true;
		
		//保存成功后即关闭此对话框
		closeOK();
	}

/**
 * 此处插入方法说明。
 * 创建日期：(2004-4-19 20:26:37)
 * @param newCorpID java.lang.String
 */
public void setCorpID(java.lang.String newCorpID) {
	m_sCorpID = newCorpID;
}

/**
 * 此处插入方法说明。
 * 创建日期：(2003-12-04 19:00:33)
 * @param newIsDirectOut boolean
 */
public void setIsDirectOut(boolean newIsDirectOut) {
	m_bIsDirectOut = newIsDirectOut;
}

/**
 * 此处插入方法说明。
   设置单据状态为直接转库
   ic_general_h bdirecttranflag：true
 * 创建日期：(2004-4-19 10:23:01)
 * @param albillVOs java.util.ArrayList
 */
public void setIsDirectOutFlag(ArrayList albillVOs) {
	if (albillVOs != null && albillVOs.size() > 0) {
		nc.vo.ic.pub.bill.GeneralBillVO billvo = null;
		nc.vo.ic.pub.bill.GeneralBillHeaderVO billheadvo = null;
		UFBoolean ufbTrue = new UFBoolean(true);
		for (int i = 0; i < albillVOs.size(); i++) {
			billvo = (GeneralBillVO) albillVOs.get(i);
			if (billvo != null) {
				billheadvo = billvo.getHeaderVO();
				if (billheadvo != null) {
					billheadvo.setBdirecttranflag(ufbTrue);
				}
			}
		}

	}

}

/**
 * 此处插入方法说明。
   设置特殊单VO
 * 创建日期：(2003-11-3 16:50:29)
 * @param newSpBill nc.vo.ic.pub.bill.SpecialBillVO
 */
public void setSpBill(AggregatedValueObject newSpBill) {
	m_voSpBill = newSpBill;
	//if (getChldClientUIIn().getBillCardPanel().getBodyItems() != null)
		//for (int i = 0; i < getChldClientUIIn().getBillCardPanel().getBodyItems().length; i++) {
			//getChldClientUIIn().getBillCardPanel().getBodyItems()[i].setEdit(true);
		//}
}

/**
 * ClientUIInAndOut 构造子注解。
 * @param parent java.awt.Container
 * @param title java.lang.String
 * sBillTypeCode 单据类型
 AggregatedValueObject:特殊单VO(源单据)
 */
public void setVO(
	AggregatedValueObject voBill,
	ArrayList invos,
	ArrayList outvos,
	String sSrcBillTypeCode,
	String sOldPK,
	String sCorpPK,
	String sUserID) {
    long lTime = System.currentTimeMillis();
	if (((invos == null) && (outvos == null))
		|| (sSrcBillTypeCode == null)
		|| (sSrcBillTypeCode.trim().length() == 0)
		|| (sCorpPK == null)
		|| (sCorpPK.trim().length() == 0)
		|| (sUserID == null)
		|| (sUserID.trim().length() == 0)
		|| (voBill == null)) {
//		nc.ui.ic.pub.tools.ICEnv.out("in/out data err.ret...");
		return;
	}

	m_sSrcBillTypeCode = sSrcBillTypeCode.trim();
	m_sCorpPK = sCorpPK.trim();
	m_sUserID = sUserID.trim();
	m_sOldPK = sOldPK;
	m_voSpBill = voBill;

	afterInit();

	
	if (invos != null) {
    GeneralBillVO billvo = null;
    ArrayList<GeneralBillItemVO[]> itemVos = new ArrayList<GeneralBillItemVO[]> ();
    
    for(int i=0;i<invos.size();i++){
      billvo = (GeneralBillVO)invos.get(i);
      if(billvo==null)
        continue;
      
      itemVos.add(billvo.getItemVOs());
      
      if(GenMethod.isSEmptyOrNull((String)billvo.getHeaderValue("clastmodiid"))){
        billvo.setHeaderValue("clastmodiid", ClientEnvironment.getInstance().getUser().getPrimaryKey());
        billvo.setHeaderValue("clastmodiname", ClientEnvironment.getInstance().getUser().getUserName());
      }
    }
		getChldClientUIIn().setUITxtFldStatus(getUITxtFldStatus());
		getChldClientUIIn().onAdd(true, null);
		//excecute formulas
		exeAllFormulas(getChldClientUIIn(),invos);
		//added by lirr 2009-01-12
		if(null != itemVos && itemVos.size() > 0){
			for(GeneralBillItemVO[] item : itemVos){
				getChldClientUIIn().getBillCardPanel().getBillModel().execFormulasWithVO(item, new String []{"nplannedmny->ninnum*nplannedprice;"});
				
			}
			
		}//end
		getChldClientUIIn().setScaleOfListData(invos);

		getChldClientUIIn().setAllData(invos);
		//2010-11-09 MeiChao Begin 添加 根据上游
		
		if(invos.size()==1 && voBill instanceof SpecialBillVO){//如果只有1个入库单,那么允许费用植入下游其他入库单中
				// 如果parent是形态转换单.
				// 获取PK
				String specialVOPk = ((SpecialBillVO)voBill).getHeaderVO().getPrimaryKey();
				// 获取协助类
				JJIcScmPubHelper expenseManager = new JJIcScmPubHelper();
				try {
					// 根据PK查询对应费用信息
					expenseVOs = (InformationCostVO[]) expenseManager
							.querySmartVOs(InformationCostVO.class, null,
									" dr=0 and cbillid='" + specialVOPk + "'");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				// 将费用信息植入其他入库单表体
				setExpenseVOs();//2010-11-09 
				//getChldClientUIIn().getBillCardPanel().execHeadLoadFormulas();// 执行显示公式
				//getChldClientUIIn().updateUI();
				getChldClientUIIn().selectListBill(0);
		}
		//2010-11-09 MeiChao End 添加
		
		
		
		
		//克隆之，防止被修改。
		m_inVOs = (ArrayList) invos.clone();
		
		//参照过滤
		BillItem bi = getChldClientUIIn().getBillCardPanel().getHeadItem("cotherwhid");
        RefFilter.filtWh(bi,m_sCorpID,null);

		if (getChldClientUIIn().m_htBItemEditFlag != null) {
			UFBoolean bEdit = new UFBoolean(true);
			for (int i = 0; i < getChldClientUIIn().getBillCardPanel().getBodyItems().length; i++) {
				if (getChldClientUIIn().getBillCardPanel().getBodyItems()[i].isEdit())
					getChldClientUIIn().m_htBItemEditFlag.put(getChldClientUIIn().getBillCardPanel().getBodyItems()[i].getKey(), bEdit);
			}
		}
		//zhy2005-05-27效率问题，在getChldClientUIOut().setAllData(outvos);时已经指向第0行数据了，所以在此不需要重做一遍，注释掉下面一行
//		getChldClientUIIn().selectListBill(0);
		getChldClientUIIn().removeListHeadMouseListener();
		//getChldClientUIIn().setBillVO((GeneralBillVO) invos.get(0));
		getUITabPane().setEnabledAt(0, true);
		getUITabPane().setSelectedIndex(0);
		m_bIsTabInHaveValue = true;
	} else {
		   
		m_inVOs = null;
		getUIPaneIn().setEnabled(false);
		getUITabPane().setEnabledAt(0, false);
		m_bIsTabInHaveValue = false;
	}
	if (outvos != null) {
		ArrayList<GeneralBillItemVO[]> itemVos = new ArrayList<GeneralBillItemVO[]> ();
		
    GeneralBillVO billvo = null;
    for(int i=0;i<outvos.size();i++){
      billvo = (GeneralBillVO)outvos.get(i);
      if(billvo==null)
        continue;
      itemVos.add(billvo.getItemVOs());
      if(GenMethod.isSEmptyOrNull((String)billvo.getHeaderValue("clastmodiid"))){
        billvo.setHeaderValue("clastmodiid", ClientEnvironment.getInstance().getUser().getPrimaryKey());
        billvo.setHeaderValue("clastmodiname", ClientEnvironment.getInstance().getUser().getUserName());
      }
    }
    
		getChldClientUIOut().setUITxtFldStatus(getUITxtFldStatus());
		getChldClientUIOut().onAdd(true, null);
		//zhy2005-05-26加注释：此处仅对表头（所有单据表头）做了执行公式
		//excecute formulas
		exeAllFormulas(getChldClientUIOut(),outvos);
//		added by lirr 2009-01-12
		if(null != itemVos && itemVos.size() > 0){
			for(GeneralBillItemVO[] item : itemVos){
				getChldClientUIIn().getBillCardPanel().getBillModel().execFormulasWithVO(item, new String []{"nplannedmny->noutnum*nplannedprice;"});
				
			}
			
		}//end
		getChldClientUIOut().setScaleOfListData(outvos);
		getChldClientUIOut().setAllData(outvos);
	
		//克隆之，防止被修改。
		m_outVOs = (ArrayList) outvos.clone();
		
		//参照过滤
		BillItem bi = getChldClientUIOut().getBillCardPanel().getHeadItem("cotherwhid");
        RefFilter.filtWh(bi,m_sCorpID,null);

        //zhy2005-05-27效率问题，在getChldClientUIOut().setAllData(outvos);时已经指向第0行数据了，所以在此不需要重做一遍，注释掉下面一行
//		getChldClientUIOut().selectListBill(0);
		//getChldClientUIOut().setBillVO((GeneralBillVO) outvos.get(0));
		getUITabPane().setEnabledAt(1, true);
		getUITabPane().setSelectedIndex(1);
		m_bIsTabOutHaveValue = true;
		//zhy2005-05-27解决效率问题，下行的方法主要是处理批次改变后自动检验失效日期等信息，此处主要为初始化过程，并未改动批次，故此出可不进行调用，注释下面行
//		getChldClientUIOut().setAllLotRefAuto();
		getChldClientUIOut().removeListHeadMouseListener();
	} else {
		m_outVOs = null;
		getUIPaneOut().setEnabled(false);
		getUITabPane().setEnabledAt(1, false);
		m_bIsTabOutHaveValue = false;
	}

	
	TabStateChanged();
}

/**
 * ClientUIInAndOut 构造子注解。
 * @param parent java.awt.Container
 * @param title java.lang.String
 * sBillTypeCode 单据类型
   直接转库使用
 */
public void setVO4Direct(
	ArrayList invos,
	ArrayList outvos,
	String sSrcBillTypeCode,
	String sOldPK,
	String sCorpPK,
	String sUserID) {

	if (((invos == null) && (outvos == null))
		|| (sSrcBillTypeCode == null)
		|| (sSrcBillTypeCode.trim().length() == 0)
		|| (sCorpPK == null)
		|| (sCorpPK.trim().length() == 0)
		|| (sUserID == null)
		|| (sUserID.trim().length() == 0)) {
//		nc.ui.ic.pub.tools.ICEnv.out("in/out data err.ret...");
		return;
	}

	m_sSrcBillTypeCode = sSrcBillTypeCode.trim();
	m_sCorpPK = sCorpPK.trim();
	m_sUserID = sUserID.trim();
	m_sOldPK = sOldPK;

	afterInit();
	m_bIsDirectOut = true;
	if (invos != null) {
		//克隆之，防止被修改。
		m_inVOs = (ArrayList) invos.clone();
		getChldClientUIIn().setUITxtFldStatus(getUITxtFldStatus());
		getChldClientUIIn().onAdd(true, null);

//		excecute formulas
		exeAllFormulas(getChldClientUIIn(),invos);
		getChldClientUIIn().setAllData(invos);
//		//参照过滤
//		BillItem bi = getChldClientUIIn().getBillCardPanel().getHeadItem("cotherwhid");
//        RefFilter.filtWh(bi,m_sCorpID,null);

        //zhy2005-05-27效率问题，在getChldClientUIOut().setAllData(outvos);时已经指向第0行数据了，所以在此不需要重做一遍，注释掉下面一行
//		getChldClientUIIn().selectListBill(0);
		if (getChldClientUIIn().m_htBItemEditFlag != null) {
			UFBoolean bEdit = new UFBoolean(false);
			nc.ui.pub.bill.BillItem bBI = null;

			for (int i = 0; i < getChldClientUIIn().getBillCardPanel().getBodyItems().length; i++) {
				bBI = getChldClientUIIn().getBillCardPanel().getBodyItems()[i];
				if (bBI.isEdit())
					getChldClientUIIn().m_htBItemEditFlag.put(bBI.getKey(), bEdit);
			}
		}
		getChldClientUIIn().removeListHeadMouseListener();
		getUITabPane().setEnabledAt(0, true);
		m_bIsTabInHaveValue = true;
	} else {
		m_inVOs = null;
		getUIPaneIn().setEnabled(false);
		getUITabPane().setEnabledAt(0, false);
		m_bIsTabInHaveValue = false;
	}
	if (outvos != null) {
		//克隆之，防止被修改。
		m_outVOs = (ArrayList) outvos.clone();
		getChldClientUIOut().setUITxtFldStatus(getUITxtFldStatus());
		getChldClientUIOut().onAdd(true, null);

        //excecute formulas
		exeAllFormulas(getChldClientUIOut(),outvos);
		getChldClientUIOut().setAllData(outvos);

		//参照过滤
		BillItem bi = getChldClientUIOut().getBillCardPanel().getHeadItem("cotherwhid");
        RefFilter.filtWh(bi,m_sCorpID,null);

        //zhy2005-05-27效率问题，在getChldClientUIOut().setAllData(outvos);时已经指向第0行数据了，所以在此不需要重做一遍，注释掉下面一行
//		getChldClientUIOut().selectListBill(0);
		getUITabPane().setEnabledAt(1, true);
		getUITabPane().setSelectedIndex(1);
		m_bIsTabOutHaveValue = true;
		//zhy2005-05-27解决效率问题，下行的方法主要是处理批次改变后自动检验失效日期等信息，此处主要为初始化过程，并未改动批次，故此出可不进行调用，注释下面行
//		getChldClientUIOut().setAllLotRefAuto();
		getChldClientUIOut().removeListHeadMouseListener();
	} else {
		m_outVOs = null;
		getUIPaneOut().setEnabled(false);
		getUITabPane().setEnabledAt(1, false);
		m_bIsTabOutHaveValue = false;
	}
	TabStateChanged();

}

/**
 * 创建者：王乃军
 * 功能：重载的显示提示信息对话框功能
 * 参数：
 * 返回：
 * 例外：
 * 日期：(2001-5-9 9:23:32)
 * 修改日期，修改人，修改原因，注释标志：
 *
 *
 *
 *
 */
public void showErrorMessage(String sMsg) {
		nc.ui.pub.beans.MessageDialog.showHintDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON","UPPSCMCommon-000270")/*@res "提示"*/, sMsg);

}

/**
 * 此处插入方法说明。
 * 创建日期：(2003-12-06 13:50:22)
 * 修改人：刘家清 修改日期：2007-12-26下午03:12:24 修改原因：出库单增加同步"皮重主数量"到入库单
 * 修改人：刘家清 修改时间：2008-11-20 下午02:14:58 修改原因：如果出库单上的是空的话，就不要同步了。
 */
private void synOut2In() {
	String[] saOutBillItemKey =
		new String[] {
			"castunitid",
			"vfree0","vfree1","vfree2","vfree3","vfree4","vfree5",
			"vbatchcode",
			"scrq",
			"dvalidate",
			"noutnum",
			"noutassistnum",
			"noutgrossnum",
			"ntarenum",
			"nprice",
			"nmny","dbizdate" ,"cvendorid","cprojectid","cprojectphaseid","vnote","hsl","cinvbasid","nshouldoutassistnum","castunitname"};

	String[] saInBillItemKey =
		new String[] {
			"castunitid",
			"vfree0","vfree1","vfree2","vfree3","vfree4","vfree5",
			"vbatchcode",
			"scrq",
			"dvalidate",
			"ninnum",
			"ninassistnum",
			"ningrossnum",
			"ntarenum",
			"nprice",
			"nmny" ,"dbizdate","cvendorid","cprojectid","cprojectphaseid","vnote","hsl","cinvbasid","nneedinassistnum","castunitname"};

	
	GeneralBillVO voInBill=getChldClientUIIn().getBillVO();
	GeneralBillVO voOutBill=getChldClientUIOut().getBillVO();
	
	if (getChldClientUIOut() != null && getChldClientUIOut() != null) {
		//序列号
		if (getChldClientUIOut().getSerialData() != null) {
			ArrayList alSNOut = getChldClientUIOut().getSerialData();
			ArrayList alSNIN = getChldClientUIIn().getSerialData();
			if (voInBill.getHeaderVO().getIsLocatorMgt().intValue()
				== 1
				&& alSNIN != null&& alSNIN .size()>0) {
				//入库为货位管理,且存在序列号，不用同步序列号，避免货位丢失

			} else {
				//判断入库的序列号
				alSNIN = new ArrayList(alSNOut.size());
				nc.vo.ic.pub.sn.SerialVO[] voSNs = null;
				for (int i = 0; i < alSNOut.size(); i++) {
					voSNs = (nc.vo.ic.pub.sn.SerialVO[]) alSNOut.get(i);
					if (voSNs != null) {
						nc.vo.ic.pub.sn.SerialVO[] voSNins = new nc.vo.ic.pub.sn.SerialVO[voSNs.length];
						for (int j = 0; j < voSNs.length; j++) {
							voSNins[j] = (nc.vo.ic.pub.sn.SerialVO) voSNs[j].clone();
							voSNins[j].setCcustomerid(null);
							voSNins[j].setCfreezeid(null);
							voSNins[j].setCgeneralbid(null);
							voSNins[j].setCinbillbodyid(null);
							voSNins[j].setCinbillheadid(null);
							voSNins[j].setCserialid(null);
							voSNins[j].setCspaceid(null);
							voSNins[j].setCwarehouseid(null);
							voSNins[j].setVinbillcode(null);
							voSNins[j].setDbillindate(null);
							voSNins[j].setCinbilltypecode(null);
							voSNins[j].setSnStatus(new Integer(VOStatus.NEW));

						}
						alSNIN.add(voSNins);
					} else
						alSNIN.add(null);

				}
				getChldClientUIIn().setSerialData(alSNIN);
			}
		}
        
		getChldClientUIOut().getBillCardPanel().getBillModel().setNeedCalculate(false);
		String sOutRowNO = null;
		String sInRowNO = null;
	//	GeneralBillVO vo = (GeneralBillVO)m_inVOs.get(getChldClientUIIn().getLastSelListHeadRow());
		GeneralBillVO vo = getChldClientUIIn().getBillVO();//(GeneralBillVO)m_inVOs.get(getChldClientUIIn().getLastSelListHeadRow());
		int iLocatorMgt = vo.getHeaderVO().getIsLocatorMgt().intValue();
		
		int iOutRows =getChldClientUIOut().getBillCardPanel().getRowCount();
		for (int i = 0;i < iOutRows;i++) {
		    //如果其他出库单捡货时有拆行，其他入库单需作相应处理；否则会产生串行。
		    //从行号是否匹配入手
		    sOutRowNO = (String)getChldClientUIOut().getBillCardPanel().getBodyValueAt(i,"crowno");
		    sInRowNO = (String)getChldClientUIIn().getBillCardPanel().getBodyValueAt(i,"crowno");
		    if(sOutRowNO!=null&&sInRowNO!=null&&!sOutRowNO.equals(sInRowNO)){//处理插行
		        GeneralBillItemVO outvo = (GeneralBillItemVO)voOutBill.getItemVOs()[i].clone();
		        outvo.setAttributeValue("ccorrespondcode",null);
		        outvo.setAttributeValue("ccorrespondtype",null);
		        outvo.setAttributeValue("ccorrespondhid",null);
		        outvo.setAttributeValue("vcorrespondrowno",null);
		        outvo.setAttributeValue("ccorrespondbid",null);
		        //出库的应发对应到入库的应收
		        outvo.setNshouldinnum(outvo.getNshouldoutnum());
		        outvo.setNneedinassistnum(outvo.getNshouldoutassistnum());
		        outvo.setNshouldoutnum(null);
		        outvo.setNneedinassistnum(null);

		        outvo.setCgeneralhid(null);
		        outvo.setCgeneralbid(null);
		        outvo.setLocator(null);
		        getChldClientUIIn().getBillCardPanel().getBillTable().setRowSelectionInterval(i,i);
		        getChldClientUIIn().getBillCardPanel().insertLine();//
		        getChldClientUIIn().getBillCardPanel().getBillModel().setBodyRowVO(outvo,i);
		        getChldClientUIIn().getMVOBill().setItem(i,outvo);

		    }else if(sInRowNO==null&&i<iOutRows){//处理追加行
		        GeneralBillItemVO outvo = (GeneralBillItemVO)voOutBill.getItemVOs()[i].clone();
		        outvo.setAttributeValue("ccorrespondcode",null);
		        outvo.setAttributeValue("ccorrespondtype",null);
		        outvo.setAttributeValue("ccorrespondhid",null);
		        outvo.setAttributeValue("vcorrespondrowno",null);
		        outvo.setAttributeValue("ccorrespondbid",null);
		        outvo.setAttributeValue("ccorrespondbid",null);
		        outvo.setNshouldinnum(outvo.getNshouldoutnum());

		        outvo.setCgeneralhid(null);
		        outvo.setCgeneralbid(null);
		        outvo.setLocator(null);
		        getChldClientUIIn().getBillCardPanel().addLine();//
		        getChldClientUIIn().getBillCardPanel().getBillTable().setRowSelectionInterval(i,i);
		        getChldClientUIIn().getBillCardPanel().getBillModel().setBodyRowVO(outvo,i);
		        getChldClientUIIn().getMVOBill().setItem(i,outvo);

		    }
		        
			for (int j = 0; j < saOutBillItemKey.length; j++){
				if (null != getChldClientUIOut().getBillCardPanel().getBodyValueAt(i, saOutBillItemKey[j])){
				    getChldClientUIIn().getBillCardPanel().setBodyValueAt(getChldClientUIOut().getBillCardPanel().getBodyValueAt(i, saOutBillItemKey[j]),i,saInBillItemKey[j]);
				    getChldClientUIIn().getMVOBill().setItemValue(i,saInBillItemKey[j],getChldClientUIOut().getBillCardPanel().getBodyValueAt(i, saOutBillItemKey[j]));
				}
			}
			for (int j = 1; j <= 20; j++){
				String def="pk_defdoc"+String.valueOf(j);
				if (null != getChldClientUIOut().getBillCardPanel().getBodyValueAt(i, def)){
				    getChldClientUIIn().getBillCardPanel().setBodyValueAt(getChldClientUIOut().getBillCardPanel().getBodyValueAt(i, def),i,def);
				    getChldClientUIIn().getMVOBill().setItemValue(i,def,getChldClientUIOut().getBillCardPanel().getBodyValueAt(i, def));
				}
			    def="vuserdef"+String.valueOf(j);
			    if (null != getChldClientUIOut().getBillCardPanel().getBodyValueAt(i, def)){
				    getChldClientUIIn().getBillCardPanel().setBodyValueAt(getChldClientUIOut().getBillCardPanel().getBodyValueAt(i, def),i,def);
				    getChldClientUIIn().getMVOBill().setItemValue(i,def,getChldClientUIOut().getBillCardPanel().getBodyValueAt(i, def));
			    }
			
			}
			
			
		//	vo.setItem(i, (GeneralBillItemVO)getChldClientUIIn().getBillCardPanel().getBillModel().getBodyValueRowVO(i,GeneralBillItemVO.class.getName()));
	
		}	
		
			voInBill=getChldClientUIIn().getBillVO();
			m_inVOs.set(getChldClientUIIn().getLastSelListHeadRow(),voInBill);
			
//			getChldClientUIIn().setAllData(m_inVOs);//此处大大影响效率,放在循环外
			
			
			
			
			
			LocatorVO[] voaLoc = null;
			GeneralBillItemVO[] voInItems=voInBill.getItemVOs();
			for(int i=0;i<voInItems.length;i++){
				//货位同步处理,出库单修改数量后入库单的货位数据要修改 05-05-14 lj
				//如果货位数据不是一行，必须晴空货位数据；如果为一行，修改货位数据的数量为同步数量
				if (iLocatorMgt == 1){
					voaLoc =voInItems[i].getLocator();
				
					if (voaLoc!=null && voaLoc.length>1){
						for (int m=0;m<voaLoc.length;m++){
							voaLoc[m] = null;
						}
	
						getChldClientUIIn().getLocatorData().set(i,voaLoc);
					}
					if (voaLoc!=null && voaLoc.length == 1 && voaLoc[0]!=null ){
						voaLoc[0].setAttributeValue( "ninspacenum", getChldClientUIOut().getBillCardPanel().getBodyValueAt(i, "noutnum") );	
						voaLoc[0].setAttributeValue( "ninspaceassistnum",getChldClientUIOut().getBillCardPanel().getBodyValueAt(i, "noutassistnum") );
						((LocatorVO[])getChldClientUIIn().getLocatorData().get(i))[0].setAttributeValue( "ninspacenum", getChldClientUIOut().getBillCardPanel().getBodyValueAt(i, "noutnum") );
						((LocatorVO[])getChldClientUIIn().getLocatorData().get(i))[0].setAttributeValue( "ninspaceassistnum", getChldClientUIOut().getBillCardPanel().getBodyValueAt(i, "noutassistnum") );
						
					}
				}
				//同步条码
				voInItems[i].setBarCodeVOs(voOutBill.getItemVOs()[i].getBarCodeVOs());

				
		}

		getChldClientUIIn().setAllData(m_inVOs);
		
		getChldClientUIIn().getBillCardPanel().getBillModel().setNeedCalculate(true);
		getChldClientUIIn().getBillCardPanel().getBillModel().reCalcurateAll();
		
		
	}

}

/**
 * 此处插入方法说明。
   同步单据的条码VO
 * 创建日期：(2004-4-22 11:47:23)
 * @param billItemPnlIn nc.vo.ic.pub.bill.GeneralBillItemVO[]
 * @param billItemPnlOut nc.vo.ic.pub.bill.GeneralBillItemVO[]
 */
private void synOut2InBarcode(GeneralBillItemVO[] billItemPnlIn, GeneralBillItemVO[] billItemPnlOut) {

	if (billItemPnlIn != null
		&& billItemPnlIn.length > 0
		&& billItemPnlOut != null
		&& billItemPnlOut.length > 0
		&& billItemPnlOut.length == billItemPnlIn.length) {
		nc.vo.ic.pub.bc.BarCodeVO[] barcodevos = null;
		for (int i = 0; i < billItemPnlIn.length; i++) {
			barcodevos = billItemPnlOut[i].getBarCodeVOs();
			billItemPnlIn[i].setBarCodeVOs(barcodevos);
		}
	}
}

/**
 * 此处插入方法说明。
 * 创建日期：(2003-12-06 13:50:22)
 */
private void synOut2InBill() {
	if (getChldClientUIOut() != null && getChldClientUIOut() != null 
      && getChldClientUIOut().getCardTableRowNum()>0 && getChldClientUIIn().getCardTableRowNum()==getChldClientUIOut().getCardTableRowNum() ) {
	//	nc.ui.pub.bill.BillCardPanel pnlIn=getChldClientUIIn().getBillCardPanel();

//		GeneralBillItemVO[] billItemPnlIn= (GeneralBillItemVO[])getChldClientUIIn().getBillVO().getChildrenVO();
//		GeneralBillItemVO[] billItemPnlOut= (GeneralBillItemVO[])getChldClientUIOut().getBillVO().getChildrenVO();
//		//同步条码数据
//	 	synOut2InBarcode(billItemPnlIn,billItemPnlOut);

		synOut2In();
	

	}

}
/**
 * MeiChao
 * 2010
 * @return
 */
private boolean setExpenseVOs(){
	
	this.getChldClientUIIn().getBillCardPanel().getBillModel("jj_scm_informationcost").setBodyDataVO(this.expenseVOs);
	this.getChldClientUIIn().getBillListPanel().getBodyBillModel("jj_scm_informationcost").setBodyDataVO(this.expenseVOs);
	if(this.expenseVOs==null){
		return false;
	}else{
		return true;
	}
}



/**
 * 其他入库单签字时执行此方法
 * 根据当前其他入库单中的费用信息,生成暂估应付单以及IA的库存调整单
 * @author MeiChao
 * @param GeneralBillVO -必须是其他入库单VO
 * @since 2010-11-09 从产品代码:其他入库单 中移出成为一个独立的方法.
 * @return boolean true:传应付,存货核算 成功 false: 一项或某项失败. 失败原因请参见异常信息.
 */
protected boolean icToAPAndIA(GeneralBillVO generalBill){
//	  
//	  InformationCostVO[] expenseVOs=(InformationCostVO[])this.getClientUI().getBillCardPanel().getBillData().getBodyValueVOs("jj_scm_informationcost", InformationCostVO.class.getName());
//		if(expenseVOs==null){
//			//如果无费用信息则,不做任何操作.
//			return true;
//		}else if(expenseVOs!=null&&expenseVOs.length>0){
//			/**
//			 * 开始处理费用信息.
//			 * 过滤费用信息,按客商分类.
//			 */
//			Map<String,List<InformationCostVO>> expenseAsCustomerMap=new HashMap();
//			List expenseListTemp=Arrays.asList(expenseVOs);//将费用数组转化成List 
//			List<InformationCostVO> allExpenseList=new ArrayList<InformationCostVO>(expenseListTemp);//新建(复制)一个List,用以后续处理.
//			while(allExpenseList.size()>0){//只要AllExpenseList中还有值.那么便继续循环
//			String customerTemp=allExpenseList.get(0).getCcostunitid();//初始化起始客商id(客商管理id),默认选择第一个客商.
//			List<InformationCostVO> oneExpenseList=new ArrayList();//初始化费用List,按客商分类的费用信息均存在此列表中.
//			for(int i=0;i<allExpenseList.size();){
//				if(customerTemp.equals(allExpenseList.get(i).getCcostunitid())){//一旦当前循环中的客商管理id与初始化的客商管理id相等
//					oneExpenseList.add(allExpenseList.get(i));//将该费用信息加入到List中
//					allExpenseList.remove(i);//将该费用信息从源List中移除
//					i=0;//循环指针i归零(重新循环,也可以不写这一句,直接校验List中的下一对象,但不保险.)
//				}else{
//					i++;//否则,循环指针i累加.
//				}
//			}
//			expenseAsCustomerMap.put(customerTemp, oneExpenseList);//循环结束后,将按客商分类好的费用信息存入Map中.
//		 }
//			/**
//			 * 开始组织暂估应付单VO
//			 */
//			ClientEnvironment ce=ClientEnvironment.getInstance();//初始化环境常量
//			//暂估应付单列表
//			List<DJZBVO> estimationTempVOs=new ArrayList();
//			//费用keySet,key=客商管理pk
//			Object[] pk_cumandocArray=expenseAsCustomerMap.keySet().toArray();
//			//其他入库单VO
//			// 支持平台，clone一个，以便于以后的处理，同时防止修改了m_voBill
//			GeneralBillVO voAudit = (GeneralBillVO) generalBill.clone();
//			GeneralBillHeaderVO generalHead=voAudit.getHeaderVO();//其他入库单表头
//			GeneralBillItemVO[] generalBody=voAudit.getItemVOs();//其他入库单表体
//			IUAPQueryBS query = NCLocator.getInstance().lookup(IUAPQueryBS.class);//实例化客户端查询接口
//			for(int i=0;i<pk_cumandocArray.length;i++){
//				DJZBVO oneAPVO=new DJZBVO();//实例化一个应付单VO
//				DJZBHeaderVO head=new DJZBHeaderVO();//实例化一个暂估应付单表头VO.
//				//某客商下所有费用List
//				List<InformationCostVO> oneCustomerExpense=expenseAsCustomerMap.get(pk_cumandocArray[i].toString());
//				Double oneExpenseSummny=new Double(0.0);//某暂估应付单的整单应付金额
//				DJZBItemVO[] bodyVOs=new DJZBItemVO[oneCustomerExpense.size()];//初始化表体VO数组
//				for(int j=0;j<oneCustomerExpense.size();j++){//遍历某客商下的费用列表
//					InformationCostVO oneExpense=oneCustomerExpense.get(j);//获取某客商下的一个费用VO
//					DJZBItemVO body=new DJZBItemVO();//实例化一个暂估应付单表体VO
//					body.setBbhl(new UFDouble(1.0));//本币汇率
//					body.setBbye(new UFDouble(oneExpense.getNoriginalcurmny()));//本币余额--无税金额
//					body.setBilldate(new UFDate());//日期
//					body.setBzbm(oneExpense.getCurrtypeid());//币种编码--币种
//					//body.setcheckflag 对账标记
//					String sql="select pk_invbasdoc from bd_invbasdoc where invcode='"+oneExpense.getCostcode();
//					sql+="' and invname='"+oneExpense.getCostname()+"'";
//					Object invbasid=null;
//					try {
//						invbasid= query.executeQuery(sql, new ColumnProcessor());
//					} catch (BusinessException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//						return false;
//					}   
//					body.setCinventoryid(invbasid==null?null:invbasid.toString());//存货基本档案ID--费用存货基本档案id
//					body.setCksqsh(oneExpense.getPk_informantioncost());//源头单据行id--费用信息表id
//					body.setDdhh(oneExpense.getPk_informantioncost());//上层来源单据行id--费用信息表id
//					body.setDdlx(generalHead.getPrimaryKey());//上层来源单据id--其他入库单ID
//					body.setDeptid(generalHead.getCdptid());//部门pk-其他入库单中的部门PK
//					body.setDfbbje(new UFDouble(oneExpense.getNoriginalcurmny()));//贷方本币金额--无税金额
//					body.setDfbbsj(new UFDouble(new Double(0.0)));//贷方本币税金--0
//					body.setDfbbwsje(new UFDouble(oneExpense.getNoriginalcurmny()));//贷方本币无税金额--无税金额
//					body.setDfshl(new UFDouble(oneExpense.getNnumber()));//贷方数量--数量
//					body.setDfybje(new UFDouble(oneExpense.getNoriginalcurmny()));//贷方原币金额--无税金额
//					body.setDfybsj(new UFDouble(new Double(0.0)));//贷方原币税金--0
//					body.setDfybwsje(new UFDouble(oneExpense.getNoriginalcurmny()));//贷方原币无税金额--无税金额
//					body.setDj(new UFDouble(oneExpense.getNoriginalcurprice()));//单价--单价
//					body.setDjdl("yf");//单据大类--yf
//					body.setDjlxbm("D1");//单据类型编码--D1
//					body.setDr(0);
//					body.setDwbm(this.getEnvironment().getCorpID());//公司pk--当前登陆公司id
//					body.setFbye(new UFDouble(new Double(0.0)));//辅币余额--0
//					body.setFlbh(j);//分录编号--既行号
//					body.setFx(-1);//方向
//					//根据费用信息中的客商管理档案PK,查询对应的客商基本档案PK
//					String queryCustomerBasSQL="select t.pk_cubasdoc from bd_cumandoc t where t.pk_cumandoc='"+oneExpense.getCcostunitid()+"'";
//					Object cubasid=null;
//					try {
//						cubasid= query.executeQuery(queryCustomerBasSQL, new ColumnProcessor());
//					} catch (BusinessException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//						return false;
//					}
//					body.setHbbm(cubasid==null?null:cubasid.toString());//伙伴编码--客商管理id
//					body.setHsdj(new UFDouble(oneExpense.getNoriginalcurprice()));//含税单价--单价
//					body.setIsSFKXYChanged(new UFBoolean(false));//收付款协议是否发生变化--N
//					body.setIsverifyfinished(new UFBoolean(false));//是否核销完成--N
//					body.setJsfsbm("4A");//上层来源单据类型--4A 其他入库单
//					body.setKslb(1);//扣税类别--1
//					body.setOld_flag(new UFBoolean(false));
//					body.setOld_sys_flag(new UFBoolean(false));
//					body.setPausetransact(new UFBoolean(false));//挂起标志--N
//					body.setPh("4A");//源头单据类型--4A
//					body.setpjdirection("none");//票据方向--none
//					body.setQxrq(new UFDate());//起效日期--当前日期
//					body.setSfbz("3");//收付标志--"3"
//					body.setShlye(new UFDouble(oneExpense.getNnumber()));//数量余额--数量
//					body.setSl(new UFDouble(new Double(0.0)));//税率--0
//					body.setVerifyfinisheddate(new UFDate("3000-01-01"));//核销完成日期--默认3000-01-01
//					body.setWldx(1);//往来对象标志--1
//					body.setXgbh(-1);//并帐标志 ---   -1
//					body.setXyzh(generalHead.getPrimaryKey());//源头单据id--其他入库单id
//					body.setYbye(new UFDouble(oneExpense.getNoriginalcurmny()));//原币余额--无税金额
//					body.setYwbm("0001AA10000000006MFZ");//单据类型PK--固定0001AA10000000006MFZ
//					body.setYwybm(generalHead.getCbizid());//业务员PK--其他入库单业务员id
//					
//					/**
//					 * 特殊标志: 自定义项18 19
//					 */
//					body.setZyx18("tureFree");//2010-11-07 "费用暂估应付"标志,启用于: 暂估处理,See:EstimateImpl 约9181行 用于生成采购发票时的处理
//					body.setZyx19(body.getHbbm());//2010-11-07 "客商管理ID" 启用于: 暂估处理 ,See:EstimateImpl 约9423行 用于生成采购发票时的处理
//					
//					
//					oneExpenseSummny+=body.getDfbbje().toDouble();//累加贷方本币金额
//					bodyVOs[j]=body;
//				}
//				head.setBbje(new UFDouble(oneExpenseSummny));//本币金额--表体累加金额
//				head.setDjdl("yf");//单据大类--yf
//				head.setDjkjnd(ce.getAccountYear());//会计年度--当前系统的会计年度
//				head.setDjkjqj(ce.getAccountMonth());//会计期间--当前系统会计期间
//				head.setDjlxbm("D1");//单据类型编码--D1
//				head.setDjrq(new UFDate());//单据日期--当前系统日期
//				head.setDjzt(1);//单据状态--1 表示已保存 2表示已生效
//				head.setDr(0);
//				head.setDwbm(this.getCorpID());//单位编码--公司ID
//				head.setEffectdate(new UFDate());//起效日期--当前系统日期
//				head.setHzbz("-1");//坏账标志--  -1 表示不是坏账
//				head.setIsjszxzf(new UFBoolean(false));//是否结算中心支付--否
//				head.setIsnetready(new UFBoolean(false));//是否已经补录--否
//				//head.setIspaid(new UFBoolean(false));//是否付款
//				head.setIsreded(new UFBoolean(false));//是否红冲
//				head.setIsselectedpay(1);//选择付款--1
//				head.setLrr(this.getEnvironment().getUserID());//录入人--当前登陆用户id
//				head.setLybz(4);//来源标志--4 表示系统生成, 1 表示自制
//				head.setPrepay(new UFBoolean(false));//预收款标志--N
//				head.setPzglh(1);//系统标志--1
//				head.setQcbz(new UFBoolean(false));//期初标志--N
//				head.setSpzt(null);//为空,表示未审批
//				head.setSxbz(0);//生效标志--0表示未生效  10 表示已生效
//				String queryBusitype="select t.pk_busitype from bd_busitype t where t.busicode='arap' and t.businame='收付通用流程'";
//				Object busitype=null;
//				if(generalHead.getCbiztypeid()==null){
//				try {//如果当前其他入库单的销售类型字段为空,那么开始查询通用收付流程的业务类型编码.
//					busitype= query.executeQuery(queryBusitype, new ColumnProcessor());
//				} catch (BusinessException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//					return false;
//				}
//				}
//				head.setXslxbm(generalHead.getCbiztypeid()==null?busitype==null?"00011110000000002RGT":busitype.toString():generalHead.getCbiztypeid());//销售类型编码--其他入库单的业务类型(业务流程)编码
//				head.setYbje(new UFDouble(oneExpenseSummny));//本币金额--表体累加金额
//				head.setYwbm("0001AA10000000006MFZ");//单据类型--默认0001AA10000000006MFZ
//				head.setZgyf(1);//暂估应付标志--1表示暂估应付 0表示非暂估应付
//				head.setZzzt(0);//支付状态--0
//				head.setZyx20("Y");//2010-11-07  MeiChao 由于后续费用处理需要,加入此值,尚不明了其意义.
//				
//				oneAPVO.setParentVO(head);//加入表头
//				oneAPVO.setChildrenVO(bodyVOs);//加入表体
//				estimationTempVOs.add(oneAPVO);//将VO加入数组中.
//			}
//			//获取应收应付的对外操作接口
//			IArapBillPublic iARAP=(IArapBillPublic)NCLocator.getInstance().lookup(IArapBillPublic.class.getName());
//			try {
//				DJZBVO[] apVOs=new DJZBVO[estimationTempVOs.size()];
//				iARAP.saveArapBills(estimationTempVOs.toArray(apVOs));
//			} catch (BusinessException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//				MessageDialog.showErrorDlg(this.getParent(),"警告","签字成功,但传暂估应付失败!");
//				return false;
//			}
//			
//			/**
//			 * 开始组织存货核算的库存调整单VO
//			 */
//			//初始化库存调整单VO
//			BillVO changeBillVO=new BillVO();
//			BillHeaderVO changeBillHead=new BillHeaderVO();
//			BillItemVO[] changeBillBody=new BillItemVO[generalBody.length];
//			//初始化存货核算IA接口
//			IBill iBill=(IBill)NCLocator.getInstance().lookup(IBill.class.getName());
//			//存货核算接口进行保存操作时需要的参数
//			ClientLink cl=new ClientLink(ce);
//			//调整总金额
//			Double iaAdjustAmount=0.0;
//			for(int i=0;i<estimationTempVOs.size();i++){//计算调整总金额
//				iaAdjustAmount+=((DJZBHeaderVO)estimationTempVOs.get(i).getParentVO()).getBbje()==null?0.0:((DJZBHeaderVO)estimationTempVOs.get(i).getParentVO()).getBbje().toDouble();
//			}
//			//存货总数量
//			Double invSUM=0.0;
//			for(int i=0;i<generalBody.length;i++){//计算存货总金额
//				invSUM+=generalBody[i].getNinnum()==null?0.0:generalBody[i].getNinnum().toDouble();
//			}
//			
//			//调整单表头
//			changeBillHead.setBauditedflag(new UFBoolean(false));
//			changeBillHead.setBdisableflag(new UFBoolean(false));
//			changeBillHead.setBestimateflag(new UFBoolean(false));
//			changeBillHead.setBoutestimate(new UFBoolean(false));
//			changeBillHead.setBwithdrawalflag(new UFBoolean(false));
//			changeBillHead.setCbilltypecode("I9");//单据类型
//			changeBillHead.setClastoperatorid(ce.getUser().getPrimaryKey());//最后修改人
//			changeBillHead.setCoperatorid(ce.getUser().getPrimaryKey());//操作员
//			changeBillHead.setCrdcenterid(generalHead.getPk_calbody());//库存组织
//			changeBillHead.setCsourcemodulename("IC");//来源模块
//			changeBillHead.setDbilldate(new UFDate());
//			changeBillHead.setDr(0);
//			changeBillHead.setFdispatchflag(0);
//			changeBillHead.setIdebtflag(-1);
//			changeBillHead.setPk_corp(this.getCorpID());
//			changeBillHead.setTlastmaketime(new UFDateTime(new Date()).toString());
//			changeBillHead.setTmaketime(new UFDateTime(new Date()).toString());
//			SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMddHHmm");
//			changeBillHead.setVbillcode("I9"+sdf.format(new Date()));
//			//调整单表体
//			for(int i=0;i<changeBillBody.length;i++){
//				changeBillBody[i]=new BillItemVO();
//				changeBillBody[i].setBadjustedItemflag(new UFBoolean(false));
//				changeBillBody[i].setBauditbatchflag(new UFBoolean(false));
//				changeBillBody[i].setBlargessflag(new UFBoolean(false));
//				changeBillBody[i].setBretractflag(new UFBoolean(false));
//				changeBillBody[i].setBrtvouchflag(new UFBoolean(false));
//				changeBillBody[i].setBtransferincometax(new UFBoolean(false));
//				changeBillBody[i].setCadjustbillid(null);//调整对象单据id
//				changeBillBody[i].setCadjustbillitemid(null);//调整对象单据体id
//				changeBillBody[i].setCbilltypecode("I9");
//				changeBillBody[i].setCfirstbillid(null);//源头单据id
//				changeBillBody[i].setCfirstbillitemid(null);//源头单据体id
//				changeBillBody[i].setCfirstbilltypecode("4A");//源头单据类型"其他入库单"
//				changeBillBody[i].setCicbillcode(generalHead.getVbillcode());//上层来源单据编号,其他入库单编号
//				changeBillBody[i].setCicbillid(generalHead.getCgeneralhid());//上层来源单据id,其他入库单id
//				changeBillBody[i].setCicbilltype("4A");//上层来源单据类型 25
//				changeBillBody[i].setCicitemid(generalBody[i].getCgeneralbid());//上层来源单据体id--其他入库单表体id
//				changeBillBody[i].setCvendorbasid(generalBody[i].getPk_cubasdoc());//供应商基本档案id,取库存表体对应字段
//				changeBillBody[i].setCvendorid(generalBody[i].getCvendorid());//供应商管理档案id,取库存表体对应字段
//				changeBillBody[i].setCinvbasid(generalBody[i].getCinvbasid());//存货基本档案id,取库存表体对应字段
//				changeBillBody[i].setCinventoryid(generalBody[i].getCinventoryid());//存货管理档案id,取库存表体对应字段
//				changeBillBody[i].setDbizdate(new UFDate());
//				changeBillBody[i].setDr(0);
//				changeBillBody[i].setFcalcbizflag(0);
//				changeBillBody[i].setFdatagetmodelflag(1);
//				changeBillBody[i].setFolddatagetmodelflag(1);
//				changeBillBody[i].setFoutadjustableflag(new UFBoolean(false));
//				changeBillBody[i].setFpricemodeflag(3);
//				changeBillBody[i].setIauditsequence(-1);
//				changeBillBody[i].setIrownumber(i);//行号
//				changeBillBody[i].setNadjustnum(new UFDouble(generalBody[i].getNinnum()));
//				//计算调整金额
//				java.text.NumberFormat  formater  =  java.text.DecimalFormat.getInstance();  
//				formater.setMaximumFractionDigits(2);  
//				formater.setMinimumFractionDigits(2);  
//				Double changemoney=iaAdjustAmount*(generalBody[i].getNinnum().doubleValue())/invSUM;
//				//调整金额计算完毕
//				changeBillBody[i].setNmoney(new UFDouble(formater.format(changemoney)));//将调整金额四舍五入成2位小数,并赋值.
//				changeBillBody[i].setNsimulatemny(changeBillBody[i].getNmoney());
//				changeBillBody[i].setPk_corp(this.getCorpID());
//				changeBillBody[i].setVbillcode(changeBillHead.getVbillcode());
//			}
//			changeBillVO.setParentVO(changeBillHead);
//			changeBillVO.setChildrenVO(changeBillBody);
//			//传存货核算
//			try {
//				iBill.insertBill(changeBillVO, cl);
//			} catch (BusinessException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//				MessageDialog.showErrorDlg(this.getParent(),"提示","传暂估应付成功,但传存货核算库存调整单出错!");
//				return false;
//			}
//			
//			/**
//			 * 传暂估应付及存货核算库存调整单完毕.
//			 */
//			
//		}
	  return true;
}




}