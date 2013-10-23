package nc.ui.pub.pf;

import java.awt.Container;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Stack;
import java.util.Vector;

import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.itf.uap.pf.IPFConfig;
import nc.itf.uap.pf.IPFWorkflowQry;
import nc.itf.uap.pf.IWorkflowMachine;
import nc.itf.uap.pf.IplatFormEntry;
import nc.itf.uap.rbac.function.IFuncPower;
import nc.uap.pf.metadata.ActionClientParams;
import nc.ui.pf.change.PfUtilUITools;
import nc.ui.pf.pub.PfUIDataCache;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.change.PfChangeBO_Client;
import nc.ui.pub.pf.dispatch.WFStartDispatchDialog;
import nc.ui.pub.pf.dispatch.WFWorkitemAcceptDlg;
import nc.ui.querytemplate.IBillReferQuery;
import nc.ui.querytemplate.QueryConditionDLG;
import nc.ui.querytemplate.meta.FilterMeta;
import nc.ui.querytemplate.valueeditor.IFieldValueElementEditor;
import nc.ui.querytemplate.valueeditor.IFieldValueElementEditorFactory;
import nc.ui.querytemplate.valueeditor.RefElementEditor;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.pf.change.PfUtilBaseTools;
import nc.vo.pf.pub.BusitypeVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.billtobill.BilltobillreferVO;
import nc.vo.pub.pf.CurrencyInfo;
import nc.vo.pub.pf.IPfRetException;
import nc.vo.pub.pf.IPfRetExceptionStyle;
import nc.vo.pub.pf.IWorkFlowStatus;
import nc.vo.pub.pf.PfUtilActionVO;
import nc.vo.pub.pf.PfUtilBillActionVO;
import nc.vo.pub.pf.PfUtilWorkFlowVO;
import nc.vo.pub.pf.Pfi18nTools;
import nc.vo.pub.pf.workflow.IPFActionName;
import nc.vo.pub.pfflow.PFBusiAddInfo;
import nc.vo.pub.pfflow01.BillbusinessVO;
import nc.vo.pub.template.ITemplateStyle;
import nc.vo.querytemplate.TemplateInfo;
import nc.vo.uap.pf.PFBusinessException;
import nc.vo.uap.pf.PFRuntimeException;
import nc.vo.uap.rbac.power.PowerResultVO;
import nc.vo.wfengine.definition.IApproveflowConst;
import nc.vo.wfengine.pub.WFTask;

/**
 * 流程平台客户端工具类
 * 
 * @author fangj 2001-10
 * @modifier leijun 2005-5 取消单据类型UI类名必须以<Y>开头才可指派的限制
 * @modifier leijun 2006-7 送审时的指派对话框，如果用户点击取消，则不送审
 * @modifier leijun 2007-5 使用新的查询模板
 * @modifier leijun 2008-3 重构动作处理的API，进一步精简
 */
public class PfUtilClient {

	/**
	 * 审批变量如果审批则true反之false;
	 */
	private static boolean m_checkFlag = true;

	// 当前单据类型
	private static String m_currentBillType = null;

	/** 当前审批节点的审批结果 */
	private static int m_iCheckResult = IApproveflowConst.CHECK_RESULT_PASS;

	private static boolean m_isOk = false;

	/** 判断VO是否进行转换 */
	private static boolean m_isRetChangeVo = false;

	/** fgj2001-11-27 判断当前动作是否执行成功 */
	private static boolean m_isSuccess = true;

	/** 源单据类型 */
	private static String m_sourceBillType = null;

	private static AggregatedValueObject m_tmpRetVo = null;

	private static AggregatedValueObject[] m_tmpRetVos = null;

	// 单据自制标志
	public static boolean makeFlag = false;

	private PfUtilClient() {
		// Noop!
	}

	/**
	 * 提交单据时,需要的指派信息
	 * <li>只有"SAVE","EDIT"动作才调用
	 */
	private static PfUtilWorkFlowVO checkOnSave(Container parent, String actionName, String billType,
			String currentDate, AggregatedValueObject billVo, Stack dlgResult, HashMap hmPfExParams)
			throws BusinessException {
		PfUtilWorkFlowVO wfVo = NCLocator.getInstance().lookup(IWorkflowMachine.class)
				.checkWorkitemOnSave(actionName, billType, currentDate, billVo, hmPfExParams);

		if (wfVo != null) {
			// 得到可指派的输入数据
			Vector assignInfos = wfVo.getTaskInfo().getAssignableInfos();
			if (assignInfos != null && assignInfos.size() > 0) {
				// 显示指派对话框并收集实际指派信息
				DispatchDialog dd = new DispatchDialog(parent);
				dd.initByWorkflowVO(wfVo);
				int iClose = dd.showModal();
				if (iClose == UIDialog.ID_CANCEL)
					dlgResult.push(new Integer(iClose));
			}
		}
		return wfVo;
	}

	/**
	 * 单据启动工作流时,需要的指派信息
	 * <li>包括选择后继活动参与者、选择后继分支转移
	 */
	private static PfUtilWorkFlowVO checkOnStart(Container parent, String actionName,
			String billType, String currentDate, AggregatedValueObject billVo, Stack dlgResult,
			HashMap hmPfExParams) throws BusinessException {
		PfUtilWorkFlowVO wfVo = NCLocator.getInstance().lookup(IWorkflowMachine.class)
				.checkWorkitemOnSave(actionName, billType, currentDate, billVo, hmPfExParams);

		if (wfVo != null) {
			// 得到可指派的信息
			Vector assignInfos = wfVo.getTaskInfo().getAssignableInfos();
			Vector tSelectInfos = wfVo.getTaskInfo().getTransitionSelectableInfos();
			if (assignInfos.size() > 0 || tSelectInfos.size() > 0) {
				// 显示指派对话框并收集实际指派信息
				WFStartDispatchDialog wfdd = new WFStartDispatchDialog(parent, wfVo, billVo);
				int iClose = wfdd.showModal();
				if (iClose == UIDialog.ID_CANCEL)
					dlgResult.push(new Integer(iClose));
			}
		}
		return wfVo;
	}

	/**
	 * 检查当前单据是否处于工作流程中，并进行交互
	 */
	private static PfUtilWorkFlowVO checkWorkitemWhenSignal(Container parent, String actionName,
			String billType, String currentDate, AggregatedValueObject billVo, HashMap hmPfExParams)
			throws BusinessException {
		PfUtilWorkFlowVO wfVo = null;
		WFWorkitemAcceptDlg clientWorkFlow = null;
		try {
			wfVo = NCLocator.getInstance().lookup(IWorkflowMachine.class).checkWorkFlow(actionName,
					billType, currentDate, billVo, hmPfExParams);
			if (wfVo == null) {
				m_checkFlag = true;
				return wfVo;
			} else {
				String billId = wfVo.getTaskInfo().getTask().getBillID();
				CurrencyInfo ci = wfVo.getHmCurrency().get(billId);
				boolean bHasMoney = ci != null && ci.isShowMoney();
				clientWorkFlow = new WFWorkitemAcceptDlg(parent, wfVo, bHasMoney, billVo);

				if (clientWorkFlow.showModal() == UIDialog.ID_OK) {
					// 返回处理后的工作项
					m_checkFlag = true;
					wfVo = clientWorkFlow.getWorkFlow();
				} else {
					// 用户取消
					m_checkFlag = false;
					wfVo = null;
				}
			}
		} finally {
			if (clientWorkFlow != null) {
				nc.ui.pub.beans.UIComponentUtil.removeAllComponentRefrence(clientWorkFlow);
			}
		}
		return wfVo;
	}

	/**
	 * 检查当前单据是否处于审批流程中，并进行交互
	 */
	private static PfUtilWorkFlowVO checkWorkitemWhenApprove(Container parent, String actionName,
			String billType, String currentDate, AggregatedValueObject billVo, HashMap hmPfExParams)
			throws BusinessException {
		PfUtilWorkFlowVO wfVo = null;
		WorkFlowCheckDlg clientWorkFlow = null;
		try {
			wfVo = NCLocator.getInstance().lookup(IWorkflowMachine.class).checkWorkFlow(actionName,
					billType, currentDate, billVo, hmPfExParams);
			if (wfVo == null) {
				m_checkFlag = true;
				return wfVo;
			} else {
				String billId = wfVo.getTaskInfo().getTask().getBillID();
				CurrencyInfo ci = wfVo.getHmCurrency().get(billId);
				boolean bHasMoney = ci != null && ci.isShowMoney();
				clientWorkFlow = new WorkFlowCheckDlg(parent, wfVo, bHasMoney);

				if (clientWorkFlow.showModal() == UIDialog.ID_OK) { // 如果用户审批
					// 返回审批的工作项
					m_checkFlag = true;
					wfVo = clientWorkFlow.getWorkFlow();
				} else { // 用户不审批
					m_checkFlag = false;
					wfVo = null;
				}
			}
		} finally {
			if (clientWorkFlow != null) {
				nc.ui.pub.beans.UIComponentUtil.removeAllComponentRefrence(clientWorkFlow);
			}
		}
		return wfVo;
	}

	/**
	 * 子菜单按钮响应事件调用该方法，用于
	 * <li>1.查询来源单据
	 * <li>2.显示来源单据，并进行选择
	 * <li>3.获取选择的来源单据
	 */
	public static void childButtonClicked(ButtonObject bo, String pkCorp, String FunNode,
			String pkOperator, String currentBillType, Container parent) {
		childButtonClicked(bo, pkCorp, FunNode, pkOperator, currentBillType, parent, null);
	}

	/**
	 * 子菜单按钮响应事件调用该方法，用于
	 * <li>1.查询来源单据
	 * <li>2.显示来源单据，并进行选择
	 * <li>3.获取选择的来源单据
	 * 
	 * @param btnObj
	 * @param pkCorp
	 * @param funNode
	 * @param pkOperator
	 * @param currentBillType
	 * @param parent
	 * @param userObj
	 */
	public static void childButtonClicked(ButtonObject btnObj, String pkCorp, String funNode,
			String pkOperator, String currentBillType, Container parent, Object userObj) {
		childButtonClicked(btnObj, pkCorp, funNode, pkOperator, currentBillType, parent, userObj, null);
	}

	/**
	 * 子菜单按钮响应事件调用该方法，用于
	 * <li>1.获取来源单据的查询对话框，并查询出来源单据
	 * <li>2.显示来源单据，并进行选择
	 * <li>3.获取选择的来源单据
	 * <li>4.如果来源单据ID为空，则进行查询条件的弹出用户选择条件，进行选择参照，否则，直接进入参照上游单据界面.
	 * 
	 * @param btnObj
	 * @param pkCorp
	 * @param funNode
	 * @param pkOperator
	 * @param currentBillType
	 * @param parent
	 * @param userObj
	 * @param sourceBillId
	 */
	public static void childButtonClicked(ButtonObject btnObj, String pkCorp, String funNode,
			String pkOperator, String currentBillType, Container parent, Object userObj,
			String sourceBillId) {
		// 获得BUTTON的TAG
		String tmpString = btnObj.getTag();
		int findIndex = tmpString.indexOf(":");
		// 1.TAG中保存的所选择的源单据类型PK
		String srcBillType = tmpString.substring(0, findIndex);
		// 2.TAG中保存的业务类型PK
		String businessType = tmpString.substring(findIndex + 1);
		if (businessType.trim().length() == 0) {
			businessType = null;
		}
		makeFlag = false;
		if (srcBillType.toUpperCase().equals("MAKEFLAG")) {
			Logger.debug("******自制单据******");
			makeFlag = true;
			return;
		}

		Logger.debug("******参照来源单据******");
		try {
			funNode = PfUIDataCache.getBillTypeInfo(srcBillType).getNodecode();
			if (funNode == null || funNode.equals(""))
				throw new PFBusinessException("请注册单据" + srcBillType + "的功能节点号");

			// 判断查询Id的信息与参照的信息
			String strQueryTemplateId = null;
			String referClassName = null; // 来源单据的显示UI类
			String srcNodekey = null; // 用于查询来源单据的查询模板的节点标识

			// 获得单据对单据的来源关系VO
			BilltobillreferVO billtobillVO = PfUtilBaseTools.findBilltobill(currentBillType, srcBillType);
			if (billtobillVO != null) {
				strQueryTemplateId = billtobillVO.getQuerytemplateid();
				strQueryTemplateId = strQueryTemplateId == null ? null : strQueryTemplateId.trim();
				referClassName = billtobillVO.getReferclassname();
				srcNodekey = billtobillVO.getChangeclassname();

				if (srcNodekey != null && srcNodekey.startsWith("<") && srcNodekey.endsWith(">")) {
					srcNodekey = srcNodekey.substring(1, srcNodekey.length() - 1);
				} else {
					srcNodekey = null;
				}
			}
			// a.获取来源单据的查询对话框，即为m_condition赋值
			IBillReferQuery qcDLG = null;
			boolean isQueryRelationCorp = true;
			if (StringUtil.isEmptyWithTrim(strQueryTemplateId)) {
				Logger.debug("获取来源单据类型对应节点使用的查询模板对话框");
				strQueryTemplateId = PfUtilBO_Client.getTemplateId(ITemplateStyle.queryTemplate, pkCorp,
						funNode, pkOperator, businessType, srcNodekey);
				isQueryRelationCorp = false;
				qcDLG = setConditionClient(strQueryTemplateId, parent, isQueryRelationCorp, pkOperator,
						funNode, pkCorp);
			} else if (strQueryTemplateId.startsWith("<")) {
				Logger.debug("获取产品组定制的来源单据查询对话框");
				strQueryTemplateId = strQueryTemplateId.substring(1, strQueryTemplateId.length() - 1);
				qcDLG = loadUserQuery(parent, strQueryTemplateId, pkCorp, pkOperator, funNode,
						businessType, currentBillType, srcBillType, srcNodekey, userObj);
			} else {
				Logger.debug("支持多单位帐查询的产品组自定义查询对话框");
				isQueryRelationCorp = true;
				qcDLG = setConditionClient(strQueryTemplateId, parent, isQueryRelationCorp, pkOperator,
						funNode, pkCorp);
			}

			if (sourceBillId == null) {
				//b.显示来源单据的查询对话框
				if (qcDLG.showModal() == UIDialog.ID_OK) {
					// c.显示来源单据，并进行选择
					refBillSource(pkCorp, funNode, pkOperator, currentBillType, parent, userObj, srcBillType,
							businessType, strQueryTemplateId, referClassName, srcNodekey, isQueryRelationCorp,
							sourceBillId, qcDLG);

				} else {
					m_isOk = false;
					return;
				}
			} else {
				//b'.直接显示来源单据
				refBillSource(pkCorp, funNode, pkOperator, currentBillType, parent, userObj, srcBillType,
						businessType, strQueryTemplateId, referClassName, srcNodekey, isQueryRelationCorp,
						sourceBillId, qcDLG);
				return;
			}
		} catch (Exception ex) {
			Logger.error(ex.getMessage(), ex);
			MessageDialog.showErrorDlg(parent, "错误", "参照上游单据出现异常=" + ex.getMessage());
		}

	}

	/**
	 * 参照来源单据进行选择
	 * 
	 * @param pkCorp
	 * @param funNode
	 * @param pkOperator
	 * @param currentBillType
	 * @param parent
	 * @param userObj
	 * @param billType
	 * @param businessType
	 * @param strQueryTemplateId
	 * @param referClassName
	 * @param srcNodekey
	 * @param isQueryRelationCorp
	 * @param sourceBillId
	 * @param qcDLG
	 * @throws Exception
	 */
	private static void refBillSource(String pkCorp, String funNode, String pkOperator,
			String currentBillType, Container parent, Object userObj, String billType,
			String businessType, String strQueryTemplateId, String referClassName, String srcNodekey,
			boolean isQueryRelationCorp, String sourceBillId, IBillReferQuery qcDLG) throws Exception {
		// 获取主表的关键字段
		String pkField = PfUtilBaseTools.findPkField(billType);

		if (pkField == null || pkField.trim().length() == 0)
			throw new PFBusinessException("流程平台：无法通过单据类型获取单据实体的主表PK字段");
		String whereString = null;
		if (sourceBillId == null) {
			whereString = qcDLG.getWhereSQL();
		} else
			whereString = pkField + "='" + sourceBillId + "'";
          //add by QuSida (佛山骏杰)2010-9-24
		  // 采购发票参照采购应付单的过滤条件(是否红冲：否    是否暂估应付：是)
		if(currentBillType.trim().equals("25")&&billType.trim().equals("D1")){
		whereString = whereString.substring(0, whereString.length()-1);
		//add by zhang xiao wei and isnull(zb.zyx20,'N')='N' 处理参照回写的问题。
		//add by ouyangzhb 2013-10-23 增加对表体条件的过滤
		whereString = whereString+" and zb.isreded = 'N' and zb.zgyf = 1 and zb.dr = 0 and abs(nvl(fb.ntotalinvoicenumber,0))< abs(nvl(fb.dfshl,0)) and  fb.isverifyfinished = 'N' ) ";
		}
		
		
		
		// fgj2001-12-06在条件语句的后边增加0(与公司无关)或1(与公司有关)
		if (isQueryRelationCorp) {
			whereString = whereString + "1";
		} else {
			whereString = whereString + "0";
		}

		//载入来源单据展现对话框
		AbstractReferQueryUI billReferUI = null;
		if (!StringUtil.isEmptyWithTrim(referClassName)) {
			Logger.debug("产品组定制的来源单据展现对话框，必须继承自AbstractReferQueryUI");
			billReferUI = loadReferUI(referClassName, pkField, pkCorp, pkOperator, funNode, whereString,
					billType, businessType, strQueryTemplateId, currentBillType, srcNodekey, userObj, parent);
		} else {
			Logger.debug("使用通用的来源单据展现对话框"); // BillSourceDLG BillSourceDLG2
			billReferUI = new BillSourceDLG(pkField, pkCorp, pkOperator, funNode, whereString, billType,
					businessType, strQueryTemplateId, currentBillType, srcNodekey, userObj, parent);
		}

		//显示来源单据展现对话框
		if (billReferUI instanceof BillSourceDLG || billReferUI instanceof BillSourceDLG2) {
			// 需要进行VO转换
			m_isRetChangeVo = false;
		} else {
			// 不需要进行VO转换
			m_isRetChangeVo = true;
		}
		// ******2003-05-03***放入查询条件的DLG
		billReferUI.setQueyDlg(qcDLG);
		// 加载模版
		billReferUI.addBillUI();
		// 加载数据
		billReferUI.loadHeadData();
		// ***********************************
		if (billReferUI.showModal() == UIDialog.ID_OK) {
			m_sourceBillType = billType;
			m_currentBillType = currentBillType;
			m_tmpRetVo = billReferUI.getRetVo();
			m_tmpRetVos = billReferUI.getRetVos();
			m_isOk = true;
		} else {
			m_isOk = false;
		}
	}

	/**
	 * 返回 当前审批节点的处理结果 lj+ 2005-1-20
	 */
	public static int getCurrentCheckResult() {
		return m_iCheckResult;
	}

	/**
	 * 返回 用户选择的VO
	 */
	public static AggregatedValueObject getRetOldVo() {
		return m_tmpRetVo;
	}

	/**
	 * 返回 用户选择VO数组.
	 */
	public static AggregatedValueObject[] getRetOldVos() {
		return m_tmpRetVos;
	}

	/**
	 * 返回 用户选择的VO或交换过的VO
	 * 
	 * @return
	 */
	public static AggregatedValueObject getRetVo() {
		try {
			if (!m_isRetChangeVo) {
				// 需要进行VO交换
				m_tmpRetVo = PfChangeBO_Client.pfChangeBillToBill(m_tmpRetVo, m_sourceBillType,
						m_currentBillType);
			}
		} catch (BusinessException ex) {
			Logger.error(ex.getMessage(), ex);
			throw new PFRuntimeException("VO交换错误：" + ex.getMessage(), ex);
		}
		return m_tmpRetVo;
	}

	/**
	 * 返回 用户选择的VO或交换过的VO
	 * 
	 * @return
	 */
	public static AggregatedValueObject[] changeVos(AggregatedValueObject[] vos) {
		AggregatedValueObject[] tmpRetVos = null;

		try {
			tmpRetVos = PfChangeBO_Client.pfChangeBillToBillArray(vos, m_sourceBillType,
					m_currentBillType);
		} catch (BusinessException ex) {
			Logger.error(ex.getMessage(), ex);
			throw new PFRuntimeException("VO交换错误：" + ex.getMessage(), ex);
		}

		return tmpRetVos;
	}

	/**
	 * 返回 用户选择VO数组或交换过的VO数组
	 * 
	 * @return
	 */
	public static AggregatedValueObject[] getRetVos() {
		if (!m_isRetChangeVo)
			// 需要进行VO交换
			m_tmpRetVos = changeVos(m_tmpRetVos);

		return m_tmpRetVos;
	}

	/**
	 * 判断用户是否点击了＂取消＂按钮
	 * 
	 * @return boolean leijun+
	 */
	public static boolean isCanceled() {
		return !m_checkFlag;
	}

	/**
	 * 返回 参照单据是否正常关闭
	 * 
	 * @return boolean
	 */
	public static boolean isCloseOK() {
		return m_isOk;
	}

	/**
	 * 返回 当前单据动作执行是否成功
	 * 
	 * @return boolean
	 */
	public static boolean isSuccess() {
		return m_isSuccess;
	}

	/**
	 * 调用DLG界面
	 * 
	 * @param parent
	 * @param actionVo
	 * @param userObj
	 */
	private static void loadDLG(Container parent, PfUtilActionVO actionVo, Object userObj) {
		try {
			Class c = Class.forName(actionVo.getClassName());
			Class[] ArgsClass = new Class[] { Container.class };
			Object[] Arguments = new Object[] { parent };
			Constructor ArgsConstructor = c.getConstructor(ArgsClass);
			Object retObj = (Object) ArgsConstructor.newInstance(Arguments);
			if (retObj instanceof IinitData) {
				((IinitData) retObj).initData(userObj.toString());
			} else {
				((IinitData2) retObj).initData(userObj);
			}
			((UIDialog) retObj).showModal();
		} catch (Exception ex) {
			Logger.error(ex.getMessage(), ex);
		}
	}

	/**
	 * 载入产品组自己的源单据参照UI
	 * 
	 * @param referClassName
	 *            参照类名称
	 * @param pkField
	 * @param pkCorp
	 * @param operator
	 * @param funNode
	 * @param queryWhere
	 * @param billType
	 * @param businessType
	 * @param templateId
	 * @param currentBillType
	 * @param nodeKey
	 * @param userObj
	 * @param parent
	 * @return
	 */
	public static AbstractReferQueryUI loadReferUI(String referClassName, String pkField,
			String pkCorp, String operator, String funNode, String queryWhere, String billType,
			String businessType, String templateId, String currentBillType, String nodeKey,
			Object userObj, Container parent) {
		Class c = null;

		try {
			c = Class.forName(referClassName);
			// NC220变更后的处理
			Class[] Args220Class = new Class[] { String.class, String.class, String.class, String.class,
					String.class, String.class, String.class, String.class, String.class, String.class,
					Object.class, Container.class };
			Object[] Arguments = new Object[] { pkField, pkCorp, operator, funNode, queryWhere, billType,
					businessType, templateId, currentBillType, nodeKey, userObj, parent };
			// 实例化带参构造方法
			Constructor ArgsConstructor = c.getConstructor(Args220Class);
			return (AbstractReferQueryUI) ArgsConstructor.newInstance(Arguments);
		} catch (NoSuchMethodException ex) {
			// 再找另一个构造方法
			try {
				Class[] ArgsClass = new Class[] { String.class, String.class, String.class, String.class,
						String.class, String.class, String.class, String.class, String.class, Container.class };
				Object[] Arguments = new Object[] { pkField, pkCorp, operator, funNode, queryWhere,
						billType, businessType, templateId, currentBillType, parent };
				Constructor ArgsConstructor = c.getConstructor(ArgsClass);
				return (AbstractReferQueryUI) ArgsConstructor.newInstance(Arguments);
			} catch (Exception e) {
				Logger.error(e.getMessage(), e);
				MessageDialog.showErrorDlg(parent, "错误", "产品组自定义参照错误:" + ex.getMessage());
			}
		} catch (Exception ex) {
			Logger.error(ex.getMessage(), ex);
			MessageDialog.showErrorDlg(parent, "错误", "产品组220以后自定义参照错误:" + ex.getMessage());
		}
		return null;
	}

	/**
	 * 载入产品组自己的源单据查询UI
	 * 
	 * @param parent
	 * @param className
	 *            来源单据查询UI类名
	 * @param pkCorp
	 * @param pkOperator
	 * @param FunNode
	 * @param businessType
	 * @param currBillType
	 * @param sourceBillType
	 * @param nodeKey
	 * @param userObj
	 */
	private static IBillReferQuery loadUserQuery(Container parent, String className, String pkCorp,
			String pkOperator, String FunNode, String businessType, String currBillType,
			String sourceBillType, String nodeKey, Object userObj) {

		Class c = null;
		try {
			c = Class.forName(className);
			// 先判定是否为新查询模板UI的子类
			Class[] ArgsClass2 = new Class[] { Container.class, TemplateInfo.class };
			Constructor ArgsConstructor2 = c.getConstructor(ArgsClass2);

			String qtId = PfUtilBO_Client.getTemplateId(ITemplateStyle.queryTemplate, pkCorp, FunNode,
					pkOperator, businessType, nodeKey);
			TemplateInfo ti = new TemplateInfo();
			ti.setTemplateId(qtId);
			ti.setPk_Org(pkCorp);
			ti.setUserid(pkOperator);
			ti.setCurrentCorpPk(pkCorp);
			ti.setFunNode(FunNode);
			Object[] Arguments = new Object[] { parent, ti };
			Object retObj = ArgsConstructor2.newInstance(Arguments);
			//对查询模版对话框的一些定制初始化
			if (retObj instanceof IinitQueryData) {
				((IinitQueryData) retObj).initData(pkCorp, pkOperator, FunNode, businessType, currBillType,
						sourceBillType, userObj);
			} else {
				((IinitQueryData2) retObj).initData(pkCorp, pkOperator, FunNode, businessType,
						currBillType, sourceBillType, nodeKey, userObj);
			}

			return (IBillReferQuery) retObj;
		} catch (NoSuchMethodException e) {
			Logger.warn("流程平台：找不到新查询模板UI的构造方法，继续判定是否有老查询模板的构造方法", e);

			try {
				// 应该为老查询模板UI的子类
				Class[] ArgsClass = new Class[] { Container.class };
				Constructor ArgsConstructor = c.getConstructor(ArgsClass);

				Object[] Arguments = new Object[] { parent };
				Object retObj = ArgsConstructor.newInstance(Arguments);
				//对查询模版对话框的一些定制初始化
				if (retObj instanceof IinitQueryData) {
					((IinitQueryData) retObj).initData(pkCorp, pkOperator, FunNode, businessType,
							currBillType, sourceBillType, userObj);
				} else {
					((IinitQueryData2) retObj).initData(pkCorp, pkOperator, FunNode, businessType,
							currBillType, sourceBillType, nodeKey, userObj);
				}
				return (IBillReferQuery) retObj;
			} catch (Exception ex) {
				Logger.error(ex.getMessage(), ex);
			}
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
		}
		return null;
	}

	/**
	 * 单据动作处理
	 * 
	 * @deprecated 5.5 替换为runAction方法
	 */
	public static Object processAction(Container parent, String actionName, String billType,
			String currentDate, AggregatedValueObject vo, Object userObj) throws Exception {
		return runAction(parent, actionName, billType, currentDate, vo, userObj, null, null, null);
	}

	/**
	 * 前台单据动作处理API，算法如下：
	 * <li>1.动作执行前提示以及事前处理，如果用户取消，则方法直接返回
	 * <li>2.查看扩展参数，判断是否需要审批流相关处理。如果为提交动作，则可能需要收集提交人的指派信息；
	 * 如果为审批动作，则可能需要收集审批人的审批信息
	 * <li>3.后台执行动作。如果出现异常，则进一步的补偿处理
	 * <li>4.对动作处理的返回值进一步处理
	 * 
	 * @param parent 父窗体
	 * @param actionName 动作编码，比如"SAVE"
	 * @param billType 单据类型（或交易类型）PK
	 * @param currentDate 业务日期
	 * @param billvo 单据聚合VO
	 * @param userObj 用户自定义对象
	 * @param strBeforeUIClass 前台事前处理类
	 * @param checkVo 校验单据聚合VO
	 * @param eParam 扩展参数
	 * @return 动作处理的返回结果
	 * @throws Exception
	 * @since 5.5
	 */
	public static Object runAction(Container parent, String actionName, String billType,
			String currentDate, AggregatedValueObject billvo, Object userObj, String strBeforeUIClass,
			AggregatedValueObject checkVo, HashMap eParam) throws Exception {
		Logger.debug("*单据动作处理 开始");
		debugParams(actionName, billType, currentDate, billvo, userObj, strBeforeUIClass, checkVo);
		long start = System.currentTimeMillis();
		m_isSuccess = true;

		if (checkVo == null) {
			checkVo = billvo;
		}

		// 1.动作执行前提示以及事前处理
		boolean isContinue = beforeProcessAction(parent, actionName, billType, checkVo, userObj,
				strBeforeUIClass);
		if (!isContinue) {
			Logger.debug("*动作执行前提示以及事前处理，返回");
			m_isSuccess = false;
			return null;
		}

		// 2.查看扩展参数，是否需要审批流相关的交互处理
		PfUtilWorkFlowVO workflowVo = null;
		Object paramNoApprove = eParam == null ? null : eParam.get(PfUtilBaseTools.PARAM_NOAPPROVE);
		if (paramNoApprove == null && (isSaveAction(actionName) || isApproveAction(actionName))) {
			workflowVo = actionAboutApproveflow(parent, actionName, billType, currentDate, billvo, eParam);
			if (!m_isSuccess)
				return null;
		} else if (isStartAction(actionName) || isSignalAction(actionName)) {
			//3.工作流相关的交互处理
			workflowVo = actionAboutWorkflow(parent, actionName, billType, currentDate, billvo, eParam);
			if (!m_isSuccess)
				return null;
		}
		if (workflowVo == null) {
			//检查不到工作项，则后台无需再次检查
			if (eParam == null)
				eParam = new HashMap<String, String>();
			eParam.put(PfUtilBaseTools.PARAM_NOTE_CHECKED, PfUtilBaseTools.PARAM_NOTE_CHECKED);
		}

		// 4.后台执行动作
		Object retObj = null;
		try {
			Logger.debug("*后台动作处理 开始");
			long start2 = System.currentTimeMillis();
			IplatFormEntry iIplatFormEntry = (IplatFormEntry) NCLocator.getInstance().lookup(
					IplatFormEntry.class.getName());
			retObj = iIplatFormEntry.processAction(actionName, billType, currentDate, workflowVo, billvo,
					userObj, eParam);
			Logger.debug("*后台动作处理 结束=" + (System.currentTimeMillis() - start2) + "ms");

			m_isSuccess = true;
		} catch (Exception ex) {
			Logger.error(ex.getMessage(), ex);

			// 5.动作处理异常后的进一步处理
			retObj = actionExceptionProcess(parent, null, actionName, billType, currentDate, userObj,
					retObj, ex);
		}
		// 5.返回对象执行
		retObjRun(parent, retObj);
		Logger.debug("*单据动作处理 结束=" + (System.currentTimeMillis() - start) + "ms");

		return retObj;
	}

	/**
	 * 工作流相关的交互处理
	 * @throws BusinessException 
	 */
	private static PfUtilWorkFlowVO actionAboutWorkflow(Container parent, String actionName,
			String billType, String currentDate, AggregatedValueObject billvo, HashMap eParam)
			throws BusinessException {
		PfUtilWorkFlowVO workflowVo = null;

		if (isStartAction(actionName)) {
			Logger.debug("*启动动作=" + actionName + "，检查工作流");
			Stack dlgResult = new Stack();
			workflowVo = checkOnStart(parent, actionName, billType, currentDate, billvo, dlgResult,
					eParam);
			if (dlgResult.size() > 0) {
				m_isSuccess = false;
				Logger.debug("*用户指派时点击了取消，则停止启动工作流");
			}
		} else if (isSignalAction(actionName)) {
			Logger.debug("*执行动作=" + actionName + "，检查工作流");
			// 检查该单据是否处于工作流中
			workflowVo = checkWorkitemWhenSignal(parent, actionName, billType, currentDate, billvo,
					eParam);
			if (workflowVo != null) {
				if (workflowVo.getIsCheckPass()) {
					// XXX::驳回也作为审批通过的一种,需要继续判断 lj+
					WFTask currTask = workflowVo.getTaskInfo().getTask();
					if (currTask.getTaskType() == WFTask.TYPE_BACKWARD) {
						if (currTask.isBackToFirstActivity())
							m_iCheckResult = IApproveflowConst.CHECK_RESULT_REJECT_FIRST;
						else
							m_iCheckResult = IApproveflowConst.CHECK_RESULT_REJECT_LAST;
					} else
						m_iCheckResult = IApproveflowConst.CHECK_RESULT_PASS;
				} else
					m_iCheckResult = IApproveflowConst.CHECK_RESULT_NOPASS;
			} else if (!m_checkFlag) {
				m_isSuccess = false;
				Logger.debug("*用户驱动工作流时点击了取消，则停止执行工作流");
			}
		}
		return workflowVo;
	}

	/**
	 * 审批流相关的交互处理
	 * @throws BusinessException 
	 */
	private static PfUtilWorkFlowVO actionAboutApproveflow(Container parent, String actionName,
			String billType, String currentDate, AggregatedValueObject billvo, HashMap eParam)
			throws BusinessException {
		PfUtilWorkFlowVO workflowVo = null;

		if (isSaveAction(actionName)) {
			Logger.debug("*提交动作=" + actionName + "，检查审批流");
			// 如果为提交动作，可能需要收集提交人的指派信息，这里统一动作名称 lj@2005-4-8
			Stack dlgResult = new Stack();
			workflowVo = checkOnSave(parent, IPFActionName.SAVE, billType, currentDate, billvo,
					dlgResult, eParam);
			if (dlgResult.size() > 0) {
				m_isSuccess = false;
				Logger.debug("*用户指派时点击了取消，则停止送审");
			}
		} else if (isApproveAction(actionName)) {
			Logger.debug("*审批动作=" + actionName + "，检查审批流");
			// 检查该单据是否处于审批流中，并收集审批人的审批信息
			workflowVo = checkWorkitemWhenApprove(parent, actionName, billType, currentDate, billvo,
					eParam);
			if (workflowVo != null) {
				if (workflowVo.getIsCheckPass()) {
					// XXX::驳回也作为审批通过的一种,需要继续判断 lj+
					WFTask currTask = workflowVo.getTaskInfo().getTask();
					if (currTask.getTaskType() == WFTask.TYPE_BACKWARD) {
						if (currTask.isBackToFirstActivity())
							m_iCheckResult = IApproveflowConst.CHECK_RESULT_REJECT_FIRST;
						else
							m_iCheckResult = IApproveflowConst.CHECK_RESULT_REJECT_LAST;
					} else
						m_iCheckResult = IApproveflowConst.CHECK_RESULT_PASS;
				} else
					m_iCheckResult = IApproveflowConst.CHECK_RESULT_NOPASS;
			} else if (!m_checkFlag) {
				m_isSuccess = false;
				Logger.debug("*用户审批时点击了取消，则停止审批");
			}
		}
		return workflowVo;
	}

	/**
	 * 单据动作处理
	 * 
	 * @deprecated 5.5 替换为runAction方法
	 */
	public static Object processAction(Container parent, String actionName, String billType,
			String currentDate, AggregatedValueObject billvo, Object userObj, String strBeforeUIClass,
			AggregatedValueObject checkVo) throws Exception {
		return runAction(parent, actionName, billType, currentDate, billvo, userObj, strBeforeUIClass,
				checkVo, null);
	}

	/**
	 * 日志一下动作处理的上下文参数
	 */
	private static void debugParams(String actionName, String billType, String currentDate,
			Object billEntity, Object userObj, String strBeforeUIClass, AggregatedValueObject checkVo) {
		Logger.debug("*********************************************");
		Logger.debug("* actionName=" + actionName);
		Logger.debug("* billType=" + billType);
		Logger.debug("* currentDate=" + currentDate);
		Logger.debug("* billEntity=" + billEntity);
		Logger.debug("* userObj=" + userObj);
		Logger.debug("* strBeforeUIClass=" + strBeforeUIClass);
		Logger.debug("* checkVo=" + checkVo);
		Logger.debug("*********************************************");
	}

	/**
	 * 判断某单据动作编码是否为"提交"或"编辑"动作
	 * <li>即以"SAVE"或"EDIT"结尾
	 * @param actionName 动作编码
	 * @return
	 */
	public static boolean isSaveAction(String actionName) {
		String strUpperName = actionName.toUpperCase();
		return strUpperName.endsWith(IPFActionName.SAVE) || strUpperName.endsWith(IPFActionName.EDIT);
	}

	/**
	 * 判断某单据动作编码是否为"启动工作流"动作
	 * 
	 * @param actionName 动作编码
	 * @return
	 */
	private static boolean isStartAction(String actionName) {
		String strUpperName = actionName.toUpperCase();
		return strUpperName.endsWith(IPFActionName.START);
	}

	/**
	 * 判断某单据动作编码是否为"执行工作流"动作
	 * 
	 * @param actionName 动作编码
	 * @return
	 */
	private static boolean isSignalAction(String actionName) {
		int leng = IPFActionName.SIGNAL.length();
		return actionName.length() >= leng
				&& actionName.toUpperCase().substring(0, leng).equals(IPFActionName.SIGNAL);
	}

	/**
	 * 预算单据特定的保存动作处理
	 * <li>直接传入流程定义PK，不需要按照制单人查找流程定义了。
	 * 
	 * @author leijun 2007-5-21
	 */
	public static Object processSaveActionWithDefPK(Container parent, String actionName,
			String billType, String currentDate, AggregatedValueObject billvo, Object userObj,
			String strBeforeUIClass, AggregatedValueObject checkVo, String flowDefPK) throws Exception {

		if (StringUtil.isEmptyWithTrim(flowDefPK))
			throw new PFBusinessException("该单据动作处理API必须传入流程定义PK参数");

		HashMap<String, String> eParam = new HashMap<String, String>();
		eParam.put(PfUtilBaseTools.PARAM_FLOWPK, flowDefPK);
		return runAction(parent, actionName, billType, currentDate, billvo, userObj, strBeforeUIClass,
				checkVo, eParam);
	}

	/**
	 * 动作处理异常后的进一步处理
	 */
	private static Object actionExceptionProcess(Container parent, PfUtilWorkFlowVO workFlow,
			String actionName, String billType, String currentDate, Object userObj, Object retObj,
			Exception ex) throws Exception {
		if (ex instanceof IPfRetException) {
			IPfRetException retEx = (IPfRetException) ex;
			// 判断是否进行处理异常
			if (retEx.getBusiStyle() == IPfRetExceptionStyle.DEAL) {
				try {
					// 执行事后处理
					retObj = runAfterActionException(parent, workFlow, actionName, billType, currentDate,
							retEx, userObj);
					// 成功运行
					m_isSuccess = true;
				} catch (Exception e) {
					m_isSuccess = false;
					throw e;
				}
			} else {
				m_isSuccess = false;
				throw ex;
			}
		} else {
			m_isSuccess = false;
			throw ex;
		}
		return retObj;
	}

	/**
	 * 单据动作处理
	 * 
	 * @deprecated 5.5 替换为runAction方法
	 */
	public static Object processAction(String actionName, String billType, String currentDate,
			AggregatedValueObject vo) throws Exception {
		return runAction(null, actionName, billType, currentDate, vo, null, null, null, null);
	}

	/**
	 * 单据动作处理
	 * 
	 * @deprecated 5.5 替换为runAction方法
	 */
	public static Object processAction(String actionName, String billType, String currentDate,
			AggregatedValueObject vo, Object userObj) throws Exception {
		return runAction(null, actionName, billType, currentDate, vo, userObj, null, null, null);
	}

	/**
	 * 单据动作处理
	 * 
	 * @deprecated 5.5 替换为runAction方法
	 */
	public static Object processActionFlow(Container parent, String actionName, String billType,
			String currentDate, AggregatedValueObject vo, Object userObj) throws Exception {
		return runAction(parent, actionName, billType, currentDate, vo, userObj, null, null, null);
	}

	/**
	 * 单据动作处理
	 * 
	 * @deprecated 5.5 替换为runAction方法
	 */
	public static Object processActionFlow(Container parent, String actionName, String billType,
			String currentDate, AggregatedValueObject billVo, Object userObj, String strBeforeUIClass)
			throws Exception {
		return runAction(parent, actionName, billType, currentDate, billVo, userObj, strBeforeUIClass,
				null, null);
	}

	/**
	 * 判断某单据动作编码是否为"审批"动作
	 * <li>即以"APPROVE"开头
	 * @param actionName
	 * @return
	 */
	public static boolean isApproveAction(String actionName) {
		return actionName.length() >= 7
				&& actionName.toUpperCase().substring(0, 7).equals(IPFActionName.APPROVE);
	}

	/**
	 * 动作执行前提示以及事前处理
	 */
	private static boolean beforeProcessAction(Container parent, String actionName, String billType,
			AggregatedValueObject billVo, Object userObj, String strBeforeUIClass) throws Exception {
		ActionClientParams acp = new ActionClientParams();
		acp.setUiBeforeClz(strBeforeUIClass);
		acp.setUiContainer(parent);
		acp.setActionCode(actionName);
		acp.setBillType(billType);
		acp.setBillEntity(billVo);
		acp.setUserObject(userObj);

		return PfUtilUITools.beforeAction(acp, false);
	}

	/**
	 * 单据动作内部处理，即动作处理异常后的补偿处理
	 * <li> 被runAfterActionException()调用
	 */
	private static Object processActionInner(Container parent, String actionName, String billType,
			String currentDate, AggregatedValueObject vo, Object userObj, PfUtilWorkFlowVO workFlow)
			throws Exception {
		Logger.debug("*单据动作内部处理 开始");
		debugParams(actionName, billType, currentDate, vo, userObj, null, null);
		long start = System.currentTimeMillis();

		// 运行的返回对象
		Object retObj = null;
		try {
			// 1.后台动作处理
			IplatFormEntry iIplatFormEntry = (IplatFormEntry) NCLocator.getInstance().lookup(
					IplatFormEntry.class.getName());
			retObj = iIplatFormEntry.processAction(actionName, billType, currentDate, workFlow, vo,
					userObj, null);

			m_isSuccess = true;
		} catch (Exception ex) {
			Logger.error(ex.getMessage(), ex);
			// 2.异常后的进一步处理
			retObj = actionExceptionProcess(parent, null, actionName, billType, currentDate, userObj,
					retObj, ex);
		}

		// 3.返回对象执行
		retObjRun(parent, retObj);
		Logger.debug("*单据动作内部处理 结束=" + (System.currentTimeMillis() - start) + "ms");
		return retObj;
	}

	/**
	 * 返回对象执行
	 * 
	 * @param parent
	 * @param retObj
	 */
	private static void retObjRun(Container parent, Object retObj) {
		if (retObj instanceof PfUtilActionVO) {
			PfUtilActionVO retVo = (PfUtilActionVO) retObj;
			if (retVo.getIsDLG()) {
				Logger.debug("*执行DLG");
				loadDLG(parent, retVo, retVo.getUIObj());
			} else {
				Logger.debug("*执行PANEL");
				BillQueryDLG bQDlg = new BillQueryDLG(parent, retVo, retVo.getUIObj());
				bQDlg.showModal();
			}
		}
	}

	/**
	 * 仅执行单据动作，不进行审批流的相关处理
	 * 
	 * @return
	 */
	public static Object processActionNoSendMessage(Container parent, String actionName,
			String billType, String currentDate, AggregatedValueObject billvo, Object userObj,
			String strBeforeUIClass, AggregatedValueObject checkVo) throws Exception {

		HashMap<String, String> eParam = new HashMap<String, String>();
		eParam.put(PfUtilBaseTools.PARAM_NOAPPROVE, PfUtilBaseTools.PARAM_NOAPPROVE);

		return runAction(parent, actionName, billType, currentDate, billvo, userObj, strBeforeUIClass,
				checkVo, eParam);
	}

	/**
	 * 单据动作批处理
	 * 
	 * @deprecated 5.5 替换为runBatch方法
	 */
	public static Object[] processBatch(Container parent, String actionName, String billType,
			String currentDate, AggregatedValueObject[] voAry, Object[] userObjAry) throws Exception {
		return runBatch(parent, actionName, billType, currentDate, voAry, userObjAry, null, null);
	}

	/**
	 * 单据动作的批处理
	 * 
	 * @deprecated 5.5 替换为runBatch方法
	 */
	public static Object[] processBatch(Container parent, String actionName, String billType,
			String currentDate, AggregatedValueObject[] voAry, Object[] userObjAry,
			String strBeforeUIClass) throws Exception {
		return runBatch(parent, actionName, billType, currentDate, voAry, userObjAry, strBeforeUIClass,
				null);
	}

	/**
	 * 前台单据动作批处理API，算法如下：
	 * <li>1.动作执行前提示以及事前处理，如果用户取消，则方法直接返回
	 * <li>2.查看扩展参数，判断是否需要审批流相关处理。如果为提交动作，且单据VO数组中只有一张单据时可能需要收集提交人的指派信息；
	 * 如果为审批动作，则针对第一张单据可能需要收集审批人的审批信息
	 * <li>3.后台执行批动作。如果出现异常，则进一步的补偿处理
	 * 
	 * @param parent 父窗体
	 * @param actionName 动作编码，比如"SAVE"
	 * @param billType 单据类型（或交易类型）PK
	 * @param currentDate 业务日期
	 * @param voAry 单据聚合VO数组
	 * @param userObjAry 用户自定义对象数组
	 * @param strBeforeUIClass 前台事前处理类
	 * @param eParam 扩展参数
	 * @return 动作批处理的返回结果
	 * @throws Exception
	 * @since 5.5
	 */
	public static Object[] runBatch(Container parent, String actionName, String billType,
			String currentDate, AggregatedValueObject[] voAry, Object[] userObjAry,
			String strBeforeUIClass, HashMap eParam) throws Exception {
		Logger.debug("*单据动作批处理 开始");
		debugParams(actionName, billType, currentDate, voAry, userObjAry, strBeforeUIClass, null);
		long start = System.currentTimeMillis();
		m_isSuccess = true;

		// 1.动作执行前提示以及事前处理
		boolean isContinue = beforeProcessBatchAction(parent, actionName, billType, voAry, userObjAry,
				strBeforeUIClass);
		if (!isContinue) {
			Logger.debug("*动作执行前提示以及事前处理，返回");
			m_isSuccess = false;
			return null;
		}

		// 2.查看扩展参数，是否需要审批流相关处理
		PfUtilWorkFlowVO workflowVo = null;
		Object paramNoApprove = eParam == null ? null : eParam.get(PfUtilBaseTools.PARAM_NOAPPROVE);
		if (paramNoApprove == null) {
			// 需要进行审批流处理 begin
			if (isSaveAction(actionName)) {
				Logger.debug("*提交动作，检查审批流");
				if (voAry.length == 1) {
					// 如果单据VO数组中只含有一张单据,则提交时需指派信息 lj+2005-6-7
					Stack dlgResult = new Stack();
					workflowVo = checkOnSave(parent, IPFActionName.SAVE, billType, currentDate, voAry[0],
							dlgResult, eParam);
					if (dlgResult.size() > 0) {
						m_isSuccess = false;
						Logger.debug("*用户指派时点击了取消，则停止送审");
						return null;
					}
				}
			} else if (isApproveAction(actionName)) {
				Logger.debug("*审批动作，检查审批流");
				// 弹出审批对话框,获取审批意见和指派信息->只针对第一张单据?!
				workflowVo = checkWorkitemWhenApprove(parent, actionName, billType, currentDate, voAry[0],
						eParam);
				// 如果工作流为空并且不审核则退出动作处理
				if ((workflowVo == null) && (!m_checkFlag)) {
					Logger.debug("*用户审批时点击了取消，则停止审批");
					m_isSuccess = false;
					return null;
				}
			}

			if (workflowVo == null) {
				//如果没有审批流或工作流，则无需启动，避免在EngineService.startApproveflow中再次查询
				if (eParam == null)
					eParam = new HashMap<String, String>();
				eParam.put(PfUtilBaseTools.PARAM_NOAPPROVE, PfUtilBaseTools.PARAM_NOAPPROVE);
			}
		}// 需要进行审批流处理 end

		// 3.后台批处理动作
		Object retObj = null;
		try {
			Logger.debug("*后台动作批处理 开始");
			long start2 = System.currentTimeMillis();
			retObj = NCLocator.getInstance().lookup(IplatFormEntry.class).processBatch(actionName,
					billType, currentDate, workflowVo, voAry, userObjAry, eParam);
			Logger.debug("*后台动作批处理 结束=" + (System.currentTimeMillis() - start2) + "ms");

			m_isSuccess = true;
		} catch (Exception ex) {
			Logger.error(ex.getMessage(), ex);
			// 4.动作批处理异常后的进一步处理
			retObj = batchActionExceptionProcess(parent, null, actionName, billType, currentDate,
					userObjAry, retObj, ex);
		}
		Logger.debug("*单据动作批处理 结束=" + (System.currentTimeMillis() - start) + "ms");
		return (Object[]) retObj;
	}

	/**
	 * 动作执行前提示以及事前处理
	 */
	private static boolean beforeProcessBatchAction(Container parent, String actionName,
			String billType, AggregatedValueObject[] voAry, Object[] userObjAry, String strBeforeUIClass)
			throws Exception {

		ActionClientParams acp = new ActionClientParams();
		acp.setUiBeforeClz(strBeforeUIClass);
		acp.setUiContainer(parent);
		acp.setActionCode(actionName);
		acp.setBillType(billType);
		acp.setBillEntity(voAry);
		acp.setUserObject(userObjAry);

		return PfUtilUITools.beforeAction(acp, true);
	}

	/**
	 * 批处理动作执行后的异常处理
	 */
	private static Object batchActionExceptionProcess(Container parent, PfUtilWorkFlowVO workflowVo,
			String actionName, String billType, String currentDate, Object[] userObjAry, Object retObj,
			Exception ex) throws Exception {
		// 判断异常是否需要进行业务处理后继续进行动作处理
		if (ex instanceof IPfRetException) {
			IPfRetException retEx = (IPfRetException) ex;
			// 判断是否进行处理异常
			if (retEx.getBusiStyle() == IPfRetExceptionStyle.DEAL) {
				try {
					// 执行事后处理
					retObj = runAfterBatchException(parent, workflowVo, actionName, billType, currentDate,
							retEx, userObjAry);
					// 成功运行
					m_isSuccess = true;
				} catch (Exception e) {
					m_isSuccess = false;
					throw e;
				}
			} else {
				m_isSuccess = false;
				throw ex;
			}
		} else {
			m_isSuccess = false;
			throw ex;
		}
		return retObj;
	}

	/**
	 * 单据动作的批处理
	 * 
	 * @deprecated 5.5 替换为runBatch方法
	 */
	public static Object[] processBatch(String actionName, String billType, String currentDate,
			AggregatedValueObject[] voAry) throws Exception {
		return runBatch(null, actionName, billType, currentDate, voAry, null, null, null);
	}

	/**
	 * 单据动作的批处理
	 * 
	 * @deprecated 5.5 替换为runBatch方法
	 */
	public static Object[] processBatch(String actionName, String billType, String currentDate,
			AggregatedValueObject[] voAry, Object[] userObjAry) throws Exception {
		return runBatch(null, actionName, billType, currentDate, voAry, userObjAry, null, null);
	}

	/**
	 * 单据动作的批处理
	 * 
	 * @deprecated 5.5 替换为runBatch方法
	 */
	public static Object[] processBatchFlow(Container parent, String actionName, String billType,
			String currentDate, AggregatedValueObject[] voAry, Object[] userObjAry) throws Exception {
		return runBatch(parent, actionName, billType, currentDate, voAry, userObjAry, null, null);
	}

	/**
	 * 单据动作内部批处理，即动作批处理异常后的补偿处理
	 * <li> 被runAfterBatchException()调用
	 */
	private static Object[] processBatchInner(Container parent, String actionName, String billType,
			String currentDate, AggregatedValueObject[] voAry, Object[] userObjAry,
			PfUtilWorkFlowVO workFlow) throws Exception {
		Object retObj = null;
		try {
			// 批处理动作
			//retObj = NCLocator.getInstance().lookup(IPFBusiAction.class).processBatch(actionName,
			//		billType, currentDate, voAry, userObjAry, workFlow);
			retObj = NCLocator.getInstance().lookup(IplatFormEntry.class).processBatch(actionName,
					billType, currentDate, workFlow, voAry, userObjAry, null);

			// 动作运行成功
			m_isSuccess = true;
		} catch (Exception ex) {
			Logger.error(ex.getMessage(), ex);
			// 判断异常是否需要进行业务处理后继续进行动作处理
			retObj = batchActionExceptionProcess(parent, workFlow, actionName, billType, currentDate,
					userObjAry, retObj, ex);
		}
		return (Object[]) retObj;
	}

	/**
	 * 查询单据所处的审批状态
	 * 
	 * @param busiType
	 * @param billType
	 * @param billId
	 * @return
	 */
	public static int queryWorkFlowStatus(String busiType, String billType, String billId) {
		try {
			return NCLocator.getInstance().lookup(IPFWorkflowQry.class).queryWorkflowStatus(busiType,
					billType, billId);
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
			return IWorkFlowStatus.ABNORMAL_WORKFLOW_STATUS;
		}
	}

	/**
	 * 为"新增"按钮添加子菜单按钮
	 * <li>这些子菜单按钮即为可参照的来源单据
	 * <li>获得某单据类型(或交易类型)在某业务类型下配置的所有来源单据类型
	 * 
	 * @param boAdd "新增"按钮
	 * @param corpId 公司PK
	 * @param billType 当前单据类型(或交易类型)PK
	 * @param boBusiness "业务类型"按钮(为空时添加所有单据类型(或交易类型)的来源单据类型)
	 */
	public static void retAddBtn(ButtonObject boAdd, String corpId, String billType,
			ButtonObject boBusiness) {

		if (boBusiness != null) {
			String businessType = null;
			businessType = boBusiness.getTag();

			// 获得该单据类型(或交易类型)在某业务类型下配置的所有来源单据类型
			BillbusinessVO[] billBusiVOs = PfUIDataCache.getSourceByCorpAndBillAndBusi(corpId, billType,
					businessType);
			boAdd.removeAllChildren();
			if (billBusiVOs == null)
				return;

			// 将来源单据都作为子菜单按钮
			ButtonObject btnAddChild = null;
			for (int i = 0; i < billBusiVOs.length; i++) {
				String showName = Pfi18nTools.i18nBilltypeName(billBusiVOs[i].getPk_billtype(),
						billBusiVOs[i].getBilltypename());
				btnAddChild = new ButtonObject(showName, showName, showName);
				btnAddChild.setPowerContrl(false);
				// 设置按钮的TAG为“3C:1001AA10000000004SG5”
				btnAddChild.setTag(billBusiVOs[i].getPk_billtype().trim() + ":"
						+ billBusiVOs[i].getPk_businesstype().trim());
				boAdd.addChildButton(btnAddChild);
			}
		} else {
			retAddBtn(boAdd, corpId, billType);
		}

	}

	/**
	 * 根据单据类型查找"新增"按钮的子菜单按钮
	 * 
	 * @param boAdd
	 *            "新增"按钮
	 * @param billType
	 *            当前单据类型PK
	 * @throws BusinessException
	 */
	public static ButtonObject getRefAddBtn(ButtonObject boAdd, String billType)
			throws BusinessException {

		// 将来源单据都作为子菜单按钮
		ButtonObject[] btnAddChild = boAdd.getChildButtonGroup();
		for (int i = 0; i < btnAddChild.length; i++) {
			ButtonObject bo = btnAddChild[i];
			if (bo.getTag().startsWith(billType))
				return bo;
		}
		throw new BusinessException("Not found billtype:" + billType + "'s Add Btn");
	}

	/**
	 * 为"业务类型"按钮添加子菜单按钮。
	 * <li>获取该单据类型(或交易类型)配置过的所有业务类型，并以子按钮形式返回；
	 * <li>包括集团进行的流程配置；
	 * <li>集团/公司的业务类型以分隔符分隔。
	 * 
	 * @param inoutBoBusiness "业务类型"主菜单按钮
	 * @param corpId 公司PK
	 * @param billType 单据类型(或交易类型)PK
	 */
	public static void retBusinessBtn(ButtonObject inoutBoBusiness, String corpId, String billType) {
		try {
			// 查询该单据类型配置的所有业务流程，包括集团和本公司的（集团在前）
			//		合并远程调用 V5.6 dingxm@2009-08-07
			//			BusitypeVO[] busiTypeVos = getBusiByCorpAndBill(corpId, billType);
			BusitypeVO[] busiTypeVos = NCLocator.getInstance().lookup(IPFConfig.class)
					.getBusiByCorpAndBill(corpId, billType);
			inoutBoBusiness.removeAllChildren();
			if (busiTypeVos == null || busiTypeVos.length == 0)
				return;
			addBusinessBtn(busiTypeVos, inoutBoBusiness);
		} catch (Exception ex) {
			Logger.error(ex.getMessage(), ex);
		}
	}

	/**
	 * 查询流程配置中某单据类型(或交易类型)有权限的业务类型，包括集团和本公司的（集团在前）
	 * @param corpId
	 * @param billType 单据类型(或交易类型)
	 * @return
	 * @throws BusinessException
	 */
	public static BusitypeVO[] getBusiByCorpAndBill(String corpId, String billType)
			throws BusinessException {
		BusitypeVO[] busitypeAll = PfUIDataCache.getBusiByCorpAndBill(corpId, billType);
		if (busitypeAll == null || busitypeAll.length == 0)
			return null;
		//查询当前登录用户有权限的业务类型
		String sUserid = ClientEnvironment.getInstance().getUser().getPrimaryKey();
		IPFConfig ipf = (IPFConfig) NCLocator.getInstance().lookup(IPFConfig.class.getName());
		PowerResultVO voPower = ipf.queryPowerBusiness(sUserid, corpId, billType);
		if (!voPower.isPowerControl())
			return busitypeAll;// 不受权限控制，则返回公司能够看到的所有业务类型
		String[] sBusinessOfPower = voPower.getPowerId();
		if (sBusinessOfPower == null || sBusinessOfPower.length == 0)
			return null;
		HashSet<String> setBusitypeOfPower = new HashSet<String>();
		for (int i = 0; i < sBusinessOfPower.length; i++) {
			setBusitypeOfPower.add(sBusinessOfPower[i]);
		}
		ArrayList<BusitypeVO> listBusitype = new ArrayList<BusitypeVO>();
		for (int i = 0; i < busitypeAll.length; i++) {
			if (setBusitypeOfPower.contains(busitypeAll[i].getPrimaryKey()))
				listBusitype.add(busitypeAll[i]);
		}
		return listBusitype.toArray(new BusitypeVO[0]);
	}

	/**
	 * 返回某单据类型(或交易类型)某动作组的所有动作，并作为子按钮填充
	 * 
	 * @param boElse 其他按钮
	 * @param billType 单据类型(或交易类型)PK
	 * @param actionStyle 动作组编码
	 */
	public static void retElseBtn(ButtonObject boElse, String billType, String actionStyle) {
		if (boElse == null)
			return;
		//获得单据类型(或交易类型)的单据类型PK
		billType = PfUtilBaseTools.getRealBilltype(billType);

		PfUtilBillActionVO[] billActionVos = (PfUtilBillActionVO[]) PfUIDataCache
				.getButtonByBillAndGrp(billType, actionStyle);
		boElse.removeAllChildren();

		ButtonObject btnChild = null;
		for (int i = 0; i < (billActionVos == null ? 0 : billActionVos.length); i++) {
			// 动作多语化
			String showName = Pfi18nTools.i18nActionName(billActionVos[i].getPkBillType(),
					billActionVos[i].getActionName(), billActionVos[i].getActionNote());
			btnChild = new ButtonObject(showName, showName, billActionVos[i].getActionName());
			btnChild.setPowerContrl(false);
			btnChild.setTag(billActionVos[i].getActionName().trim());
			boElse.addChildButton(btnChild);
		}

	}

	/**
	 * 出现异常需要再次进行处理的执行类
	 */
	private static Object runAfterActionException(Container parent, PfUtilWorkFlowVO workFlow,
			String actionName, String billType, String currentDate, IPfRetException retEx, Object userObj)
			throws Exception {
		Object retObj = null;
		try {
			// 异常中的处理类
			String procClassName = retEx.getProcClass();
			// 异常中的返回对象
			Object retValObj = retEx.getObject();
			// 运行类的实例
			Class c = Class.forName(procClassName);
			Object retRunObj = c.newInstance();
			if (retRunObj instanceof IRunClassAfterException) {
				IRunClassAfterException runAfter = (IRunClassAfterException) retRunObj;
				runAfter.runClass(parent, retValObj);
				// 前台运行的返回对象
				Object retProcObj = runAfter.getobj();
				// 调用方法处理
				retObj = processActionInner(parent, actionName, billType, currentDate,
						(AggregatedValueObject) retProcObj, userObj, workFlow);
			}
		} catch (Exception ex) {
			throw ex;
		}
		return retObj;
	}

	/**
	 * 出现异常需要再次进行处理的执行类
	 * 
	 * @return
	 */
	private static Object runAfterBatchException(Container parent, PfUtilWorkFlowVO workFlow,
			String actionName, String billType, String currentDate, IPfRetException retEx,
			Object[] userObjAry) throws Exception {
		Object retObj = null;
		try {
			// 异常中的处理类
			String procClassName = retEx.getProcClass();
			// 异常中的返回对象
			Object retValObj = retEx.getObject();
			// 运行类的实例
			Class c = Class.forName(procClassName);
			Object retRunObj = c.newInstance();
			if (retRunObj instanceof IRunClassAfterException) {
				IRunClassAfterException runAfter = (IRunClassAfterException) retRunObj;
				runAfter.runClass(parent, retValObj);
				// 前台运行的返回对象
				Object retProcObj = runAfter.getobj();
				// 调用方法处理
				retObj = processBatchInner(parent, actionName, billType, currentDate,
						(AggregatedValueObject[]) retProcObj, userObjAry, workFlow);
			}
		} catch (Exception ex) {
			throw ex;
		}
		return retObj;
	}

	/**
	 * 构造一个查询对话框，并为其设置查询模板
	 * 
	 * @param templateId
	 * @param parent
	 * @param isRelationCorp
	 * @param pkOperator
	 * @param funNode
	 */
	private static IBillReferQuery setConditionClient(String templateId, Container parent,
			boolean isRelationCorp, final String pkOperator, final String funNode, String pkCorp) {
		nc.vo.querytemplate.TemplateInfo ti = new nc.vo.querytemplate.TemplateInfo();
		ti.setTemplateId(templateId);
		ti.setPk_Org(pkCorp);
		ti.setUserid(pkOperator);
		ti.setCurrentCorpPk(pkCorp);
		ti.setFunNode(funNode);

		QueryConditionDLG qcDlg = new QueryConditionDLG(parent, ti);

		if (isRelationCorp) {
			// FIXME::多公司处理？
			qcDlg.setPlatForm(isRelationCorp);

			qcDlg.registerFieldValueEelementEditorFactory(new IFieldValueElementEditorFactory() {
				public IFieldValueElementEditor createFieldValueElementEditor(FilterMeta meta) {

					if ("公司目录".equals(meta.getValueEditorDescription())) {
						UIRefPane refPane = new UIRefPane("公司目录");
						refPane.setWhereString(constructWherePart(pkOperator, funNode));
						return new RefElementEditor(refPane, meta.getDispType());

					}
					return null;
				}
			});

		} else {
			qcDlg.setVisibleNormalPanel(false);
		}
		// qcDlg.setTemplateID(templateId);
		return qcDlg;
	}

	/**
	 * 构造“公司目录”参照的筛选条件SQL
	 * 
	 * @param pkOperator
	 *            用户PK
	 * @param funNode
	 *            功能节点编码
	 * @return
	 */
	private static String constructWherePart(String pkOperator, String funNode) {
		String baseSql = " where (isseal is null or isseal<>'Y') and ishasaccount='Y' ";
		// 返回某用户对某节点有权限的公司
		IFuncPower ifp = (IFuncPower) NCLocator.getInstance().lookup(IFuncPower.class.getName());
		String[] strPkCorps = null;
		try {
			strPkCorps = ifp.queryCorpByUserAndFunc(pkOperator, funNode);
		} catch (BusinessException e) {
			Logger.error(e.getMessage(), e);
		}
		if (strPkCorps == null || strPkCorps.length == 0)
			return baseSql;
		String whereSQL = baseSql + " and pk_corp in (";
		for (int i = 0; i < strPkCorps.length; i++) {
			whereSQL += "'" + strPkCorps[i] + "',";
		}
		whereSQL = whereSQL.substring(0, whereSQL.length() - 1) + ")";

		// NC31的实现方式 WARN::视图v_sm_userpower在NC50已经不存在
		// whereSQL = baseSql
		// + " and pk_corp in (select distinct pk_corp from v_sm_userpower where
		// userid = '"
		// + pkOperator + "' and fun_code = '" + funNode + "')";

		return whereSQL;
	}

	/**
	 * 为"新增"按钮添加子菜单按钮
	 * <li>这些子菜单按钮即为所有单据类型(或交易类型)在某业务类型下配置的所有来源单据类型可参照的来源单据
	 * 
	 * @param boAdd "新增"按钮
	 * @param corpId 公司PK
	 * @param billType 当前单据类型(或交易类型)PK
	 */
	public static void retAddBtn(ButtonObject boAdd, String corpId, String billType) {
		retAddBtn2(boAdd, corpId, billType, null);
	}

	/**
	 * 为"新增"按钮添加子菜单按钮
	 * <li>这些子菜单按钮即为所有单据类型(或交易类型)在某业务类型下配置的所有来源单据类型可参照的来源单据
	 * 
	 * @param boAdd "新增"按钮
	 * @param corpId 公司PK
	 * @param billType 当前单据类型(或交易类型)PK
	 */
	public static void retAddBtn2(ButtonObject boAdd, String corpId, String billType, String transType) {
		String businessType = null;

		try {
			// 查询流程配置中某单据类型(或交易类型)有权限的业务类型，包括集团和本公司的（集团在前）
			BusitypeVO[] biztypeVOs = PfUtilClient.getBusiByCorpAndBill(corpId, billType);

			// XXX:leijun 2009-6 避免多次远程调用，预先缓存 
			HashSet<String> hsBiztypePK = new HashSet<String>();
			for (int i = 0; i < (biztypeVOs == null ? 0 : biztypeVOs.length); i++) {
				hsBiztypePK.add(biztypeVOs[i].getPk_busitype());
			}
			if (hsBiztypePK.size() > 0)
				PfUIDataCache.preCacheSourceByCorpAndBillAndBusi(corpId, billType, transType, hsBiztypePK
						.toArray(new String[0]));
			////////////////////////////////////////

			boAdd.removeAllChildren();

			HashMap<String, StringBuffer> refbills = new HashMap<String, StringBuffer>();

			ButtonObject btnAddChild = null;

			for (int i = 0; i < (biztypeVOs == null ? 0 : biztypeVOs.length); i++) {

				businessType = biztypeVOs[i].getPk_busitype();

				// 获得该单据类型(或交易类型)在某业务类型下配置的所有来源单据类型
				BillbusinessVO[] billBusiVOs = PfUIDataCache.getSourceByCorpAndBillAndBusi2(corpId,
						billType, transType, businessType);

				// 将来源单据都作为子菜单按钮
				for (int j = 0; j < billBusiVOs.length; j++) {

					String showName = Pfi18nTools.i18nBilltypeName(billBusiVOs[j].getPk_billtype(),
							billBusiVOs[j].getBilltypename());
					if (billBusiVOs[j].getPk_billtype().toUpperCase().equals("MAKEFLAG")) {
						showName = showName + "_[" + biztypeVOs[i].getBusiname() + "]";

						btnAddChild = new ButtonObject(showName, showName, showName);
						btnAddChild.setPowerContrl(false);
						// 设置按钮的TAG为“MAKEFLAG:1001AA10000000004SG5”
						btnAddChild.setTag(billBusiVOs[j].getPk_billtype().trim() + ":" + businessType);
						boAdd.addChildButton(btnAddChild);
					} else {
						StringBuffer tag = null;
						if (refbills.get(showName) == null) {
							tag = new StringBuffer();
							tag.append(billBusiVOs[j].getPk_billtype().trim());
							tag.append(":");
						} else {
							tag = refbills.get(showName);
							tag.append(",");
						}
						tag.append(businessType);
						refbills.put(showName, tag);
					}
				}
			}

			for (String key : refbills.keySet()) {

				btnAddChild = new ButtonObject(key, key, key);
				btnAddChild.setPowerContrl(false);
				// 设置按钮的TAG为“3C:1001AA10000000004SG5,1001AA10000000004SG6,1001AA10000000004SG7”
				btnAddChild.setTag(refbills.get(key).toString());
				boAdd.addChildButton(btnAddChild);

			}
		} catch (Exception ex) {
			Logger.error(ex.getMessage(), ex);
		}

	}

	/**
	 * 为"复制"按钮添加子菜单按钮
	 * <li>当前单据或交易类型配置的勾选自制属性的所有业务流程
	 * 
	 * @param boAdd "复制"按钮
	 * @param corpId 公司PK
	 * @param billType 当前单据类型PK  
	 * @param transType 交易类型PK
	 */
	public static void retCopyBtn(ButtonObject boCopy, String corpId, String billType,
			String transType) {
		String businessType = null;

		try {
			// 查询流程配置中某单据类型(或交易类型)有权限的业务类型，包括集团和本公司的（集团在前）
			BusitypeVO[] types = PfUtilClient.getBusiByCorpAndBill(corpId, billType);
			boCopy.removeAllChildren();
			for (int i = 0; i < (types == null ? 0 : types.length); i++) {
				businessType = types[i].getPk_busitype();

				// 获得该单据类型(或交易类型)在某业务类型下配置的所有来源单据类型
				BillbusinessVO[] billBusiVOs = PfUIDataCache.getSourceByCorpAndBillAndBusi2(corpId,
						billType, transType, businessType);

				// 将来源单据都作为子菜单按钮
				ButtonObject btnAddChild = null;
				for (int j = 0; j < billBusiVOs.length; j++) {
					if (billBusiVOs[j].getPk_billtype().toUpperCase().equals("MAKEFLAG")) {
						String showName = boCopy.getName() + "_[" + types[i].getBusiname() + "]";

						btnAddChild = new ButtonObject(showName, showName, showName);
						btnAddChild.setPowerContrl(false);
						// 设置按钮的TAG为“MAKEFLAG:1001AA10000000004SG5”
						btnAddChild.setTag(billBusiVOs[j].getPk_billtype().trim() + ":" + businessType);
						boCopy.addChildButton(btnAddChild);
					}
				}
			}

		} catch (Exception ex) {
			Logger.error(ex.getMessage(), ex);
		}

	}

	/**
	 * 为业务流程按钮添加子按钮，并用第一个业务流程填充boAdd按钮
	 * 合并 retAddBtn() 和 retBusinessBtn() 减少远程调用次数 V56 dingxm@2008-08-07
	 * @param inoutBoBusiness
	 * @param boAdd
	 * @param corpId
	 * @param billType
	 */
	public static void retBusiAddBtn(ButtonObject inoutBoBusiness, ButtonObject boAdd, String corpId,
			String billType) {
		try {

			PFBusiAddInfo busiAddInfo = NCLocator.getInstance().lookup(IPFConfig.class)
					.getPFBillAndBusiInfo(corpId, billType);
			//			// 查询该单据类型配置的所有业务流程，包括集团和本公司的（集团在前）
			//			BusitypeVO[] busiTypeVos = NCLocator.getInstance().lookup(IPFConfig.class)
			//					.getBusiByCorpAndBill(corpId, billType);
			//			String businessType = busiTypeVos[0].getPrimaryKey();
			//			// 获得该单据类型(或交易类型)在某业务类型下配置的所有来源单据类型
			//			BillbusinessVO[] billBusiVOs = PfUIDataCache.getSourceByCorpAndBillAndBusi(corpId, billType,
			//					businessType);

			BusitypeVO[] busiTypeVos = busiAddInfo.getBusiTypeVos();
			BillbusinessVO[] billBusiVOs = busiAddInfo.getBillBusiVOs();
			inoutBoBusiness.removeAllChildren();
			if (busiTypeVos == null || busiTypeVos.length == 0)
				return;

			//1.为“业务流程”按钮增加子按钮
			addBusinessBtn(busiTypeVos, inoutBoBusiness);
			//2.为“增加”按钮增加子按钮
			boAdd.removeAllChildren();
			if (billBusiVOs == null)
				return;

			// 将来源单据都作为子菜单按钮
			ButtonObject btnAddChild = null;
			for (int i = 0; i < billBusiVOs.length; i++) {
				String showName = Pfi18nTools.i18nBilltypeName(billBusiVOs[i].getPk_billtype(),
						billBusiVOs[i].getBilltypename());
				btnAddChild = new ButtonObject(showName, showName, showName);
				btnAddChild.setPowerContrl(false);
				// 设置按钮的TAG为“3C:1001AA10000000004SG5”
				btnAddChild.setTag(billBusiVOs[i].getPk_billtype().trim() + ":"
						+ billBusiVOs[i].getPk_businesstype().trim());
				boAdd.addChildButton(btnAddChild);
			}
		} catch (Exception ex) {
			Logger.error(ex.getMessage(), ex);
		}
	}

	/**
	 * 根据参数信息 为业务流程按钮增加子按钮
	 * @param busiTypeVos
	 * @param inoutBoBusiness
	 */
	private static void addBusinessBtn(BusitypeVO[] busiTypeVos, ButtonObject inoutBoBusiness) {
		ButtonObject btnBusitype = null;
		String strOldCorp = busiTypeVos[0].getPk_corp();// "@@@@";
		// //集团业务类型，不要通过@@@@作为初值
		String strCurrCorp = null;
		for (int i = 0; i < busiTypeVos.length; i++) {
			//XXX:leijun+2009-6 过滤掉封存的业务类型
			if (busiTypeVos[i].getEncapsulate() != null && busiTypeVos[i].getEncapsulate().booleanValue())
				continue;

			strCurrCorp = busiTypeVos[i].getPk_corp();
			if (!strCurrCorp.equals(strOldCorp)) {
				// 肯定存在集团业务类型
				btnBusitype = new ButtonObject("", "", "");
				btnBusitype.setSeperator(true);
				inoutBoBusiness.addChildButton(btnBusitype);
			}

			// FIXME::业务类型没有i18n？
			String showName = null;
			// NCLangRes.getInstance().getStrByID("busitype", "D" +
			// billReferVo[i].getPk_busitype());
			// 业务类型名称
			if (showName == null || showName.trim().length() == 0)
				showName = busiTypeVos[i].getBusiname();
			btnBusitype = new ButtonObject(showName, showName, showName);

			btnBusitype.setPowerContrl(false);
			// TAG=业务类型主键
			btnBusitype.setTag(busiTypeVos[i].getPrimaryKey());
			btnBusitype.setData(busiTypeVos[i]);
			inoutBoBusiness.addChildButton(btnBusitype);

			strOldCorp = strCurrCorp;
		}
	}

}