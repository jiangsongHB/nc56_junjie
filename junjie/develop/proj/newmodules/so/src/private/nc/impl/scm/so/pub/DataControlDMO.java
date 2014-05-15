package nc.impl.scm.so.pub;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Vector;

import nc.bs.framework.common.NCLocator;
import nc.bs.ml.NCLangResOnserver;
import nc.bs.scm.pub.smart.SmartDMO;
import nc.bs.uap.lock.PKLock;
import nc.impl.scm.so.so016.SOToolsDMO;
import nc.itf.ic.service.IICToSO;
import nc.itf.scm.pub.lock.LockTool;
import nc.itf.scm.so.pub.IDataControlDMO;
import nc.itf.scm.so.pub.ISOInventoryDiscount;
import nc.itf.uap.busibean.SysinitAccessor;
import nc.itf.uap.pf.IPFMetaModel;
import nc.itf.uap.pf.IPFWorkflowQry;
import nc.itf.uap.sf.IOperateLogService;
import nc.ui.scm.so.SaleBillType;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.VOStatus;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.pf.IPFConfigInfo;
import nc.vo.pub.pfflow04.MessagedriveVO;
import nc.vo.scm.pub.SCMEnv;
import nc.vo.so.pub.GeneralSqlString;
import nc.vo.so.pub.Operlog;
import nc.vo.so.pub.SOGenMethod;
import nc.vo.so.so001.ISaleOrderAction;
import nc.vo.so.so001.SaleOrderVO;
import nc.vo.so.so001.SaleorderBVO;
import nc.vo.so.so001.SaleorderHVO;
import nc.vo.so.so002.SaleinvoiceVO;
import nc.vo.so.soreceive.SaleReceiveVO;

/**
 * 数据(回写)函数。 
 * 
 * 创建日期：(2001-6-22 8:40:22)
 * 
 * @author：宋杰
 * 
 */
public class DataControlDMO extends nc.bs.pub.DataManageObject implements
		IDataControlDMO {
	/**
	 * DataControlDMO 构造子注解。
	 * 
	 * @exception javax.naming.NamingException
	 *                异常说明。
	 * @exception nc.bs.pub.SystemException
	 *                异常说明。
	 */
	public DataControlDMO() throws javax.naming.NamingException,
			nc.bs.pub.SystemException {
		super();
	}

	/**
	 * DataControlDMO 构造子注解。
	 * 
	 * @param dbName
	 *            java.lang.String
	 * @exception javax.naming.NamingException
	 *                异常说明。
	 * @exception nc.bs.pub.SystemException
	 *                异常说明。
	 */
	public DataControlDMO(String dbName) throws javax.naming.NamingException,
			nc.bs.pub.SystemException {
		super(dbName);
	}
	
	/**
	 * 判断当前业务类型中，销售订单是跨公司直运推式生成调拨订单
	 * 
	 * @param vo
	 * @return
	 * @throws BusinessException
	 */
	public boolean ifPushSave205A5D(nc.vo.so.so001.SaleOrderVO vo) throws BusinessException {
		boolean flag = false;

		// **********过滤掉发货公司和订单公司相同的订单行**********
		String pk_corp = vo.getPk_corp();
		SaleorderBVO[] bvos = vo.getBodyVOs();
		String consigncorpid;
		ArrayList<SaleorderBVO> al_to = new ArrayList<SaleorderBVO>();
		for (int i = 0, len = bvos.length; i < len; i++) {
			consigncorpid = bvos[i].getCconsigncorpid();
			if (consigncorpid == null || consigncorpid.trim().length() <= 0
					|| consigncorpid.equals(pk_corp))
				continue;
			al_to.add(bvos[i]);
		}// end for
		if (al_to.size() == 0)
			return flag;
		// **********过滤掉发货公司和订单公司相同的订单行**********

		// 读取消息驱动信息
		IPFMetaModel bo = (IPFMetaModel) NCLocator.getInstance().lookup(
				IPFMetaModel.class.getName());

		MessagedriveVO message[] = bo.queryAllMsgdrvVOs(vo.getPk_corp(), SaleBillType.SaleOrder, vo
				.getBizTypeid(), "APPROVE");

		// 公司流程查不到、查集团
		if (message == null || message.length <= 0) {
			message = bo.queryAllMsgdrvVOs("@@@@", SaleBillType.SaleOrder, vo.getBizTypeid(),
					"APPROVE");
		}

		// 判断当前业务类型中，销售订单是否推式生成调拨订单
		if (message != null && message.length != 0) {
			for (int j = 0; j < message.length; j++) {
				if (message[j].getActiontype().toString().equals("PushWrite205A5D")) {
					flag = true;
					break;
				}
			}
		}

		return flag;
	}
	
	/**
	 * 送审状态校验
	 * @param sovo
	 * @throws BusinessException
	 */
	public void checkWorkflowState(AggregatedValueObject sovo) throws BusinessException{
		String billtype = null;
		String biztype = null;
		String primarykey = null;
		//销售订单
		 if (sovo instanceof SaleOrderVO) {
			 SaleOrderVO saleorder = (SaleOrderVO)sovo;
			 billtype = SaleBillType.SaleOrder;
			 biztype = saleorder.getBizTypeid();
			 primarykey = saleorder.getParentVO().getPrimaryKey();
		 //销售发票
		 }else{
			SaleinvoiceVO saleinvoice = (SaleinvoiceVO)sovo;
			billtype = SaleBillType.SaleInvoice;
			biztype = saleinvoice.getHeadVO().getCbiztype();
			primarykey = saleinvoice.getHeadVO().getCsaleid();
		 }
		
		
		//送审状态教验
		IPFWorkflowQry bsWorkflow = (IPFWorkflowQry) NCLocator.getInstance().lookup(IPFWorkflowQry.class.getName());
		boolean isExist = false;
		isExist = bsWorkflow.isExistWorkFlow(billtype, biztype, SOGenMethod.getBSCorp(),SOGenMethod.getBSUser());

		if (isExist == false) {
			throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("SCMCOMMON",
					"UPPSCMCommon-000111")/* @res "该操作员没有配置审批流"*/);
		}
		
		int iWorkflowstate = 0;
		iWorkflowstate = bsWorkflow.queryWorkflowStatus(biztype, billtype,primarykey);

		// 单据的审批流状态应该为不在工作流中或尚未开始审批或审批未通过
		if (iWorkflowstate != nc.vo.pub.pf.IWorkFlowStatus.NOT_STARTED_IN_WORKFLOW
				&& iWorkflowstate != nc.vo.pub.pf.IWorkFlowStatus.BILL_NOT_IN_WORKFLOW
				&& iWorkflowstate != nc.vo.pub.pf.IWorkFlowStatus.NOT_APPROVED_IN_WORKFLOW) {

			if (iWorkflowstate == nc.vo.pub.pf.IWorkFlowStatus.WORKFLOW_FINISHED) {
				throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("SCMCOMMON",
						"UPPSCMCommon-000078")/*
												 * @res "单据已审批完成"
												 */);
			} else if (iWorkflowstate == nc.vo.pub.pf.IWorkFlowStatus.WORKFLOW_ON_PROCESS) {
				throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("SCMCOMMON",
						"UPPSCMCommon-000079")/*
												 * @res "单据正在审批中"
												 */);
			} else if (iWorkflowstate == nc.vo.pub.pf.IWorkFlowStatus.NOT_APPROVED_IN_WORKFLOW) {
				throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("SCMCOMMON",
						"UPPSCMCommon-000071")/*
												 * @res "单据审批未通过"
												 */);
			} else if (iWorkflowstate == nc.vo.pub.pf.IWorkFlowStatus.BILLTYPE_NO_WORKFLOW) {
				throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("SCMCOMMON",
						"UPPSCMCommon-000112")/*
												 * @res "该单据类型没有配置工作流"
												 */);
			} else if (iWorkflowstate == nc.vo.pub.pf.IWorkFlowStatus.BILL_NOT_IN_WORKFLOW) {
				throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("SCMCOMMON",
						"UPPSCMCommon-000070")/*
												 * @res
												 * "单据类型配置了工作流，但该单据没有在工作流中"
												 */);
			}
		}
	}

	public void autoSetInventoryFinish(AggregatedValueObject bill)
			throws Exception {

		CircularlyAccessibleValueObject[] billbody = bill.getChildrenVO();
		CircularlyAccessibleValueObject head = bill.getParentVO();
		Object headid = head.getAttributeValue("cgeneralhid");
		if (headid == null || headid.toString().trim().length() == 0) {
			throw new BusinessException(nc.bs.ml.NCLangResOnserver
					.getInstance().getStrByID("sopub", "UPPsopub-000075")/*
																			 * @res
																			 * "接口参数主表id为空！"
																			 */);
		}

		if (billbody == null || billbody.length <= 0)
			return;

		Object type = billbody[0].getAttributeValue("cfirsttype");
		if (type == null)
			return;
		if (type.equals("30") || type.equals("3A")) {
		} else
			return;

		ArrayList alist = new ArrayList();

		for (int i = 0; i < billbody.length; i++) {
			Object id = billbody[i].getAttributeValue("cfirstbillbid");
			if (id != null && !alist.contains(id)) {
				alist.add(id);
			}
		}

		if (alist.size() <= 0)
			return;

		// 查询订单行信息
		nc.impl.scm.so.so001.SaleOrderDMO saledmo = new nc.impl.scm.so.so001.SaleOrderDMO();
		nc.vo.so.so001.SaleorderBVO[] oldordbvos = (nc.vo.so.so001.SaleorderBVO[]) saledmo
				.queryBodyDataForUpdateStatus((String[]) alist
						.toArray(new String[alist.size()]));
		if (oldordbvos == null || oldordbvos.length <= 0)
			throw new BusinessException(nc.bs.ml.NCLangResOnserver
					.getInstance().getStrByID("sopub", "UPPsopub-000076")/*
																			 * @res
																			 * "查询订单行信息失败"
																			 */);

		// 处理出库状态逻辑
		saledmo.processOutState(oldordbvos);

	}
	
	/**
	 * 自动结束开票状态。 创建日期：(2001-6-21 14:00:35)
	 * 
	 * @return nc.vo.pub.lang.UFDouble
	 * @param strDepartmentID
	 *            java.lang.String
	 */
	public void autoSetInvoicetFinish(AggregatedValueObject bill)
			throws SQLException, BusinessException,
			javax.naming.NamingException, nc.bs.pub.SystemException {
		//TODO pub_function
	}

	/**
	 * 作者：晁志平 功能：释放单据锁 参数：AggregatedValueObject 返回：void 例外： 创建日期：(2002-4-15
	 * 10:27:42) 修改日期，修改人，修改原因，注释标志：
	 */
	public void freePkForVo(nc.vo.pub.AggregatedValueObject vo)
			throws BusinessException, SQLException,
			javax.naming.NamingException, javax.ejb.RemoveException {
		try {
			// 操作员
			String sCuser = null;
			sCuser = (String) vo.getParentVO().getAttributeValue("coperatorid");
			if (sCuser == null || sCuser.trim().equals(""))
				throw new BusinessException(
						nc.bs.ml.NCLangResOnserver.getInstance().getStrByID(
								"sopub", "UPPsopub-000077")/*
															 * @res
															 * "获取操作员出错，不能进行相关操作！"
															 */);
			String[] saPk = getPksForVoFree(vo);
			// nc.vo.scm.pub.SCMEnv.out("------------------------------------------");
			// nc.vo.scm.pub.SCMEnv.out("=====================USER:" + sCuser);
			// for(int i = 0;i<saPk.length;i++){
			// nc.vo.scm.pub.SCMEnv.out("=====================ID:" + saPk[i]);
			// }
			// nc.vo.scm.pub.SCMEnv.out("------------------------------------------");
			PKLock lock = PKLock.getInstance();
			if (saPk != null) {
				lock.releaseBatchLock(saPk, sCuser, "");
			}
		} catch (Exception e) {
			throw new BusinessException(e.getMessage());
		}
	}

	/**
	 * 获得原始金额。 创建日期：(2001-9-3 9:30:24)
	 * 
	 * @return nc.vo.pub.lang.UFDouble
	 */
	private UFDouble getOldMoney(String strBillType, String strID)
			throws SQLException {
		UFDouble nmoney = new UFDouble(0.0);
		// String strSQL = "Select nmny From ";
		String strSQL = "Select nsummny From ";
		String strField = null;

		// 订单
		if (strBillType.equals(SaleBillType.SaleOrder)) {
			strSQL = strSQL + " so_saleorder_b ";
			strField = "corder_bid";
		}
		// 发票
		if (strBillType.equals(SaleBillType.SaleInvoice)) {
			strSQL = strSQL + " so_saleinvoice_b ";
			strField = "cinvoice_bid";
		}
		
		/**v5.5发货单原有逻辑不适用*/
		// 发货单
		/*if (strBillType.equals(SaleBillType.SaleReceipt)) {
			strSQL = strSQL + " so_salereceipt_b ";
			strField = "creceipt_bid";
		}*/

		strSQL = strSQL + " Where " + strField + " =  ?";

		Connection con = null;
		PreparedStatement stmt = null;

		try {
			con = getConnection();
			stmt = con.prepareStatement(strSQL);

			// pk
			stmt.setString(1, strID);

			ResultSet rs = stmt.executeQuery();
			//
			if (rs.next()) {
				BigDecimal n = (BigDecimal) rs.getObject(1);
				nmoney = (n == null ? new UFDouble(0) : new UFDouble(n));
			}
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
			} catch (Exception e) {
			}
			try {
				if (con != null) {
					con.close();
				}
			} catch (Exception e) {
			}
		}

		return nmoney;
	}

	/**
	 * 获得原始数量。 创建日期：(2001-9-3 9:30:24)
	 * 
	 * @return nc.vo.pub.lang.UFDouble
	 */
	private UFDouble getOldNumber(String strBillType, String strID)
			throws SQLException {
		UFDouble nnumber = new UFDouble(0.0);
		String strSQL = "Select nnumber From ";
		String strField = null;

		// 订单
		if (strBillType.equals(SaleBillType.SaleOrder)) {
			strSQL = strSQL + " so_saleorder_b ";
			strField = "corder_bid";
		}
		// 发票
		if (strBillType.equals(SaleBillType.SaleInvoice)) {
			strSQL = strSQL + " so_saleinvoice_b ";
			strField = "cinvoice_bid";
		}
		// 发货单
		if (strBillType.equals(SaleBillType.SaleReceive)) {
			strSQL = strSQL + " so_salereceive_b ";
			strField = "csalereceiveid_bid";
		}

		strSQL = strSQL + " Where " + strField + " =  ?";

		Connection con = null;
		PreparedStatement stmt = null;

		try {
			con = getConnection();
			stmt = con.prepareStatement(strSQL);

			// pk
			stmt.setString(1, strID);

			ResultSet rs = stmt.executeQuery();
			//
			if (rs.next()) {
				BigDecimal n = (BigDecimal) rs.getObject(1);
				nnumber = (n == null ? new UFDouble(0) : new UFDouble(n));
			}
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
			} catch (Exception e) {
			}
			try {
				if (con != null) {
					con.close();
				}
			} catch (Exception e) {
			}
		}

		return nnumber;
	}

	/**
	 * 作者：晁志平 功能：公用算法,锁单据和释放单据锁时用于取所有需要加锁的ID） 参数：AggregatedValueObject
	 * 返回：String[] 例外： 创建日期：(2002-4-15 10:27:42) 修改日期，修改人，修改原因，注释标志：
	 * 
	 * @return String[]
	 * @param vo
	 *            nc.vo.pub.AggregatedValueObject
	 */
	private String[] getPksForVo(nc.vo.pub.AggregatedValueObject vo)
			throws BusinessException {
		String[] saRslt = null;
		// 合法性检查
		if (vo == null)
			return null;
		if (vo.getParentVO() == null
				&& (vo.getChildrenVO() == null || vo.getChildrenVO().length <= 0))
			return null;
		// Vector vTmp = new Vector();
		if (vo.getParentVO() == null) {
			nc.vo.scm.pub.SCMEnv.out("数据错误：无单据表头数据,加锁失败");
			return null;
		}
		// 单据ID向量
		Vector vPk = new Vector();
		// 单据新增标志
		boolean bIsNew = (vo.getParentVO().getPrimaryKey() == null || vo
				.getParentVO().getPrimaryKey().trim().equals(""));
		String sTmp = null;
		// 如果是新增单据
		if (bIsNew) {
			// 是否有参照的上层数据
			boolean bIsRef = false;
			if (vo.getChildrenVO() == null || vo.getChildrenVO().length <= 0) {
				throw new BusinessException(
						nc.bs.ml.NCLangResOnserver.getInstance().getStrByID(
								"sopub", "UPPsopub-000078")/*
															 * @res
															 * "数据错误：新增的单据表体为空,加锁失败"
															 */);
			}
			for (int i = 0; i < vo.getChildrenVO().length; i++) {
				if (vo.getClass() == nc.vo.so.so001.SaleOrderVO.class
						|| vo.getClass() == nc.vo.so.soreceive.SaleReceiveVO.class)
					sTmp = (String) vo.getChildrenVO()[i]
							.getAttributeValue("csourcebillbodyid");
				else
					sTmp = (String) vo.getChildrenVO()[i]
							.getAttributeValue("cupsourcebillbodyid");
				// sTmp = (String)
				// vo.getChildrenVO()[i].getAttributeValue("cupsourcebillbodyid");
				if (sTmp != null && !sTmp.trim().equals("")) {
					bIsRef = true;
					break;
				}
			}
			// 如果所有单据行均为自制
			if (!bIsRef) {
				nc.vo.scm.pub.SCMEnv.out("单据行全部自制，不必加锁");
				return null;
			}
			for (int i = 0; i < vo.getChildrenVO().length; i++) {
				if (vo.getClass() == nc.vo.so.so001.SaleOrderVO.class
						|| vo.getClass() == nc.vo.so.soreceive.SaleReceiveVO.class)
					sTmp = (String) vo.getChildrenVO()[i]
							.getAttributeValue("csourcebillbodyid");
				else
					sTmp = (String) vo.getChildrenVO()[i]
							.getAttributeValue("cupsourcebillbodyid");
				// sTmp = (String)
				// vo.getChildrenVO()[i].getAttributeValue("cupsourcebillbodyid");
				if (sTmp != null && !sTmp.trim().equals("")) {
					if (!vPk.contains(sTmp))
						vPk.addElement(sTmp);
					if (vo.getClass() == nc.vo.so.so001.SaleOrderVO.class
							|| vo.getClass() == nc.vo.so.soreceive.SaleReceiveVO.class)
						sTmp = (String) vo.getChildrenVO()[i]
								.getAttributeValue("csourcebillid");
					else
						sTmp = (String) vo.getChildrenVO()[i]
								.getAttributeValue("cupsourcebillid");
					// sTmp = (String)
					// vo.getChildrenVO()[i].getAttributeValue("cupsourcebillid");
					if (sTmp == null || sTmp.trim().equals("")) {
						nc.vo.scm.pub.SCMEnv
								.out("存在数据错误：参照的单据上层行ID找不到匹配的上层头ID,加锁失败");
						return null;
					}
					if (!vPk.contains(sTmp))
						vPk.addElement(sTmp);
				}
			}
		}
		// 如果是修改单据
		else {
			sTmp = vo.getParentVO().getPrimaryKey();
			if (sTmp == null || sTmp.trim().equals("")) {
				nc.vo.scm.pub.SCMEnv.out("数据错误：找不到表头ID,加锁失败");
				return null;
			}
			vPk.addElement(sTmp);
			if (vo.getChildrenVO() != null && vo.getChildrenVO().length > 0) {
				for (int i = 0; i < vo.getChildrenVO().length; i++) {
					sTmp = vo.getChildrenVO()[i].getPrimaryKey();
					if (sTmp != null && sTmp.trim().length() > 0
							&& !vPk.contains(sTmp))
						vPk.addElement(sTmp);
				}
			}
		}
		if (vPk != null && vPk.size() > 0) {
			saRslt = new String[vPk.size()];
			vPk.copyInto(saRslt);
		}
		// 如果单据是新增
		return saRslt;

	}

	/**
	 * 作者：晁志平 功能：公用算法,锁单据和释放单据锁时用于取所有需要加锁的ID） 参数：AggregatedValueObject
	 * 返回：String[] 例外： 创建日期：(2002-4-15 10:27:42) 修改日期，修改人，修改原因，注释标志： 2002-05-17
	 * 王印芬 单据保存后不能解锁上层来源单据，修改为最大化集合
	 * 
	 */
	private String[] getPksForVoFree(nc.vo.pub.AggregatedValueObject vo)
			throws BusinessException, SQLException {
		String[] saRslt = null;

		// 合法性检查
		if (vo == null)
			return null;

		// Vector vTmp = new Vector();
		if (vo.getParentVO() == null) {
			nc.vo.scm.pub.SCMEnv.out("数据错误：无单据表头数据,加锁失败");
			return null;
		}

		// 单据ID向量
		Vector vPk = new Vector();
		String sTmp = null;

		sTmp = vo.getParentVO().getPrimaryKey();
		if (sTmp != null && !sTmp.trim().equals("")) {
			vPk.addElement(sTmp);
		}

		if (vo.getChildrenVO() != null && vo.getChildrenVO().length > 0) {
			for (int i = 0; i < vo.getChildrenVO().length; i++) {
				if (vo.getClass() == nc.vo.so.so001.SaleOrderVO.class)
					sTmp = (String) vo.getChildrenVO()[i]
							.getAttributeValue("csourcebillbodyid");
				else
					sTmp = (String) vo.getChildrenVO()[i]
							.getAttributeValue("cupsourcebillbodyid");

				// sTmp = (String)
				// vo.getChildrenVO()[i].getAttributeValue("cupsourcebillbodyid");
				if (sTmp != null && !sTmp.trim().equals("")) {
					if (!vPk.contains(sTmp))
						vPk.addElement(sTmp);
					if (vo.getClass() == nc.vo.so.so001.SaleOrderVO.class)
						sTmp = (String) vo.getChildrenVO()[i]
								.getAttributeValue("csourcebillid");
					else
						sTmp = (String) vo.getChildrenVO()[i]
								.getAttributeValue("cupsourcebillid");
					// sTmp = (String)
					// vo.getChildrenVO()[i].getAttributeValue("cupsourcebillid");
					if (sTmp == null || sTmp.trim().equals("")) {
						throw new BusinessException(
								nc.bs.ml.NCLangResOnserver.getInstance()
										.getStrByID("sopub", "UPPsopub-000079")/*
																				 * @res
																				 * "存在数据错误：参照的单据上层行ID找不到匹配的上层头ID,加锁失败"
																				 */);

					}
					if (!vPk.contains(sTmp)) {
						vPk.addElement(sTmp);
					}
				}

				sTmp = vo.getChildrenVO()[i].getPrimaryKey();
				if (sTmp != null && sTmp.trim().length() > 0
						&& !vPk.contains(sTmp))
					vPk.addElement(sTmp);

			}
		}

		if (vPk != null && vPk.size() > 0) {
			saRslt = new String[vPk.size()];
			vPk.copyInto(saRslt);
		}
		return saRslt;

	}

	/**
	 * 返回来源单据ID。
	 * 
	 * @exception 异常描述
	 * 
	 * @see 需要参见的其它内容
	 * 
	 * @since 从类的那一个版本，此方法被添加进来。（可选）
	 * 
	 * @deprecated该方法从类的那一个版本后，已经被其它方法替换。（可选）
	 * 
	 * 
	 * @return nc.vo.pub.lang.UFDouble
	 * @param salebillType
	 *            java.lang.String
	 * @param SaleID
	 *            java.lang.String
	 * @param SaleDetailID
	 *            java.lang.String
	 */
	private String getSourceBillTypeID(String strDetailID)
			throws java.sql.SQLException, BusinessException {
		String strSourceID = null;

		String strSQL = "Select creceipttype from so_saleinvoice_b Where cinvoice_bid ";

		strSQL = strSQL + " =  ?";

		Connection con = null;
		PreparedStatement stmt = null;

		try {
			con = getConnection();
			stmt = con.prepareStatement(strSQL);

			// pk
			stmt.setString(1, strDetailID);

			ResultSet rs = stmt.executeQuery();
			//
			if (rs.next()) {

				strSourceID = rs.getString(1);
			}
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
			} catch (Exception e) {
			}
			try {
				if (con != null) {
					con.close();
				}
			} catch (Exception e) {
			}
		}

		return strSourceID;
	}


	/**
	 * 作者：晁志平 功能：锁单据 参数：AggregatedValueObject 返回：UFBoolean 例外： 创建日期：(2002-4-15
	 * 10:27:42) 修改日期，修改人，修改原因，注释标志：
	 */
	public UFBoolean lockPkForVo(nc.vo.pub.AggregatedValueObject vo) throws BusinessException {

		String[] saPk = getPksForVo(vo);
		if (saPk == null || saPk.length == 0)
			return UFBoolean.TRUE;

		for (int i = 0; i < saPk.length; i++) {
			String pk = saPk[i];
			if (!PKLock.getInstance().addDynamicLock(pk)) {
				throw new BusinessException(nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("sopub",
						"UPPsopub-000080")/*
											 * @res "存在并发操作，请稍后再试"
											 */);
			}
		}

		return UFBoolean.TRUE;
	}

	/**
	 * 
	 * 方法功能描述：销售发票删除时回写上游和来源单据。
	 * <p>
	 * <b>examples:</b>
	 * <p>
	 * 使用示例
	 * <p>
	 * <b>参数说明</b>
	 * @param bill
	 * @throws SQLException
	 * @throws BusinessException
	 * <p>
	 * @author fengjb
	 * @time 2009-11-25 上午10:59:27
	 * 2009-11-25 冯加滨 改为批提交并进行删除时是否超订单开票检查
	 */
	public void setDecreaseInvoiceNum(AggregatedValueObject bill)
			throws SQLException, BusinessException{
        //参数合法性校验
		CircularlyAccessibleValueObject[] billbody = bill.getChildrenVO();
		CircularlyAccessibleValueObject billhead = bill.getParentVO();
		if (billhead == null || billbody == null || billbody.length <= 0)
			return;

		ArrayList alistids = new ArrayList();
		String[] corder_bids = null;

		// 如果来源为销售订单直接回写
		// 如果来源为销售出库单（可能包含销售发货单）、销售订单
		// 如果来源期初销售出库单 、期初销售订单

		// 源头单据ID csourcebillhid
		String csourcebillhid = null;
		// 源头单据附表ID csourcebiibid
		String csourcebiibid = null;
		// 来源单据ID csourcebillhid
		String cupsourcebillhid = null;
		// 来源单据附表ID csourcebiibid
		String cupsourcebiibid = null;

		//源头单据类型
		String creceipttype = null;

		for (int i = 0, loop = billbody.length; i < loop; i++) {
			creceipttype = billbody[i].getAttributeValue("creceipttype") == null ? ""
					: billbody[i].getAttributeValue("creceipttype").toString();
			if (!SaleBillType.SaleOrder.equals(creceipttype))
				continue;
			//源头单据附表ID 
			Object id = billbody[i].getAttributeValue("csourcebillbodyid");
			if (id != null && !alistids.contains(id)) {
				alistids.add(id);
			}
		}

		// 更新前保留开票数量，处理开票状态
		HashMap hsntotalinvoicenumber = null;
		if (alistids.size() > 0) {
			corder_bids = (String[]) alistids.toArray(new String[alistids.size()]);
			hsntotalinvoicenumber = SOToolsDMO.getAnyValueUFDouble(
					"so_saleexecute", "ntotalinvoicenumber", "csale_bid",
					corder_bids, " creceipttype = '30' ");
		}

		// 原数量
		UFDouble oldnnumber = new UFDouble(0);
		// 原金额
		UFDouble oldnmny = new UFDouble(0);
		// 原冲减金额
		UFDouble oldnsubmny = new UFDouble(0);

		Hashtable[] hasdata = getOldData("32", billhead.getPrimaryKey());

		Hashtable hasNumber = hasdata[0];
		Hashtable hasNmny = hasdata[1];
		Hashtable hasSubmny = hasdata[2];

		String SQLUpdate = "update so_saleexecute set ntotalinvoicenumber=isnull(ntotalinvoicenumber,0) - ?  ,ntotalinvoicemny=isnull(ntotalinvoicemny,0) - ? ,narsubinvmny=isnull(narsubinvmny,0) - ?  where csale_bid = ?  and csaleid = ? and creceipttype = '30' ";

		Connection con = null;
		PreparedStatement stmt = null;
		//校验是否超订单开票使用
		HashSet<String> hssourcebid = new HashSet<String>();
		try {
			con = getConnection();
			
			for (int i = 0; i < bill.getChildrenVO().length; i++) {
				// 销售发票单子表VO
				CircularlyAccessibleValueObject bodyVO = bill.getChildrenVO()[i];
                // 来源单据不为空
				if (bodyVO.getAttributeValue("cupreceipttype") != null
						&& bodyVO.getAttributeValue("cupsourcebillid") != null) {

					// 源头单据类型
					creceipttype = (String) bodyVO
							.getAttributeValue("creceipttype");

					// 来源单据ID 
					cupsourcebillhid = (String) bodyVO
							.getAttributeValue("cupsourcebillid");
					// 来源单据附表ID 
					cupsourcebiibid = (String) bodyVO
							.getAttributeValue("cupsourcebillbodyid");
					// 源头单据ID
					csourcebillhid = (String) bodyVO
							.getAttributeValue("csourcebillid");
					// 源头单据附表ID
					csourcebiibid = (String) bodyVO
							.getAttributeValue("csourcebillbodyid");

					if (hasNumber.containsKey(bodyVO.getPrimaryKey()))
						oldnnumber = (UFDouble) hasNumber.get(bodyVO
								.getPrimaryKey());

					if (hasNmny.containsKey(bodyVO.getPrimaryKey()))
						oldnmny = (UFDouble) hasNmny
								.get(bodyVO.getPrimaryKey());

					if (hasSubmny != null
							&& hasSubmny.containsKey(bodyVO.getPrimaryKey()))
						oldnsubmny = (UFDouble) hasSubmny.get(bodyVO
								.getPrimaryKey());
                    // 来源单据类型
					String cupreceipttype = (String) bodyVO
							.getAttributeValue("cupreceipttype");
//					if (cupreceipttype.equals("32")) {
//						SaleinvoiceBVO upvo = invDMO
//								.queryBodyDataByBID(cupsourcebiibid);
//						cupsourcebillhid = upvo.getCupsourcebillid();
//						cupsourcebiibid  = upvo.getCupsourcebillbodyid();
//						cupreceipttype   = upvo.getCupreceipttype();
//					}

					// 如果来源是销售订单 直接回写销售订单的开票数量
					if (cupreceipttype.equals(SaleBillType.SaleOrder)) {

//						con = getConnection();
						 hssourcebid.add(csourcebiibid);
						if (stmt == null){
						  stmt = prepareStatement(con,SQLUpdate);
						}

						// old
						stmt.setBigDecimal(1, oldnnumber.toBigDecimal());
						// old
						stmt.setBigDecimal(2, oldnmny.toBigDecimal());
						// oldsubmny
						stmt.setBigDecimal(3, oldnsubmny.toBigDecimal());
						// csale_bid
						stmt.setString(4, cupsourcebiibid);
						// csaleid
						stmt.setString(5, cupsourcebillhid);
						
						executeUpdate(stmt);
//						stmt.executeUpdate();
						// 关闭连接
//						stmt.close();
//						con.close();
                   //如果来源为销售出库单，源头是销售订单
					} else if (cupreceipttype.equals(SaleBillType.SaleOutStore)) {
                         // 第一步 销售出库单子表
                         IICToSO icTOso = (IICToSO) NCLocator.getInstance().lookup(IICToSO.class.getName());
                         nc.bs.pub.para.SysInitDMO initdmo = new nc.bs.pub.para.SysInitDMO();
                         UFDouble so65 = initdmo.getParaDbl((String) billhead.getAttributeValue("pk_corp"),"SO65");
                         UFBoolean so64 = initdmo.getParaBoolean((String) billhead.getAttributeValue("pk_corp"),"SO64");
//                       UFDouble so26 = initdmo.getParaDbl((String) billhead.getAttributeValue("pk_corp"),"SO26");
//                       UFBoolean so25 = initdmo.getParaBoolean((String) billhead.getAttributeValue("pk_corp"),"SO25");

                         // V5不允许超出库单开票
                        if (!so64.booleanValue()){
                        	so65 = new UFDouble(0);
                        }
//                      if (!so25.booleanValue()) so26 = new UFDouble(0);
                        Hashtable<String, UFDouble> hs = new Hashtable<String, UFDouble>();
                        hs.put(cupsourcebiibid, new UFDouble(0).sub(oldnnumber));
                        icTOso.reWrite4CInvoiceNum(hs, so65);

							
							// 累计开票数量
//							String SQLOUTUpdate = "update ic_general_bb3 set nsignnum=isnull(nsignnum,0) - ?  where cgeneralbid = ?  and cgeneralhid = ?";
//
//							con = getConnection();
//							stmt = con.prepareStatement(SQLOUTUpdate);
//
//							stmt.setBigDecimal(1, oldnnumber.toBigDecimal());
//
//							// csale_bid
//							stmt.setString(2, cupsourcebiibid);
//							// csaleid
//							stmt.setString(3, cupsourcebillhid);
//							stmt.executeUpdate();
//							// 关闭连接
//							stmt.close();
//							con.close();

//							nc.vo.scm.pub.SCMEnv.out("回写销售出库单的累计开票数量");

							// 第二步 查找销售发票的上上层来源

							// 再更新销售订单的累计开票数量
							if (SaleBillType.SaleOrder.equals(creceipttype)) {

								hssourcebid.add(csourcebiibid);
								if(stmt == null){
								 stmt = prepareStatement(con,SQLUpdate);
								}
								// oldnumber
								stmt.setBigDecimal(1, oldnnumber.toBigDecimal());
								// oldmny
								stmt.setBigDecimal(2, oldnmny.toBigDecimal());
								// oldsubmny
								stmt.setBigDecimal(3, oldnsubmny.toBigDecimal());
								// csale_bid
								stmt.setString(4, csourcebiibid);
								// csaleid
								stmt.setString(5, csourcebillhid);
								
								executeUpdate(stmt);
//								stmt.executeUpdate();
								// 关闭连接
//								stmt.close();
//								con.close();
							}

						} 
//						else {
//							// 如果来源期初销售出库单 、期初销售订单
//
//							if (bodyVO
//									.getAttributeValue("cupreceipttype")
//									.equals(
//											nc.ui.scm.so.SaleBillType.SaleInitOutStore)) {
//
//								// 第一步 销售出库单子表
//								// 累计开票数量
//								String SQLOUTUpdate = "update ic_general_bb3 set nsignnum=isnull(nsignnum,0) -?  where cgeneralbid = ?  and cgeneralhid = ?";
//
//								con = getConnection();
//								stmt = con.prepareStatement(SQLOUTUpdate);
//								stmt
//										.setBigDecimal(1, oldnnumber
//												.toBigDecimal());
//								// csale_bid
//								stmt.setString(2, cupsourcebiibid);
//								// csaleid
//								stmt.setString(3, cupsourcebillhid);
//								stmt.executeUpdate();
//								// 关闭连接
//								stmt.close();
//								con.close();
//
//								nc.vo.scm.pub.SCMEnv.out("回写期初销售出库单的累计开票数量");
//
//								// 从发票中得到上上层ID
//
//								if (nc.ui.scm.so.SaleBillType.SaleOrder
//										.equals(creceipttype)) {
//
//									con = getConnection();
//									stmt = con.prepareStatement(SQLUpdate);
//									// old
//									stmt.setBigDecimal(1, oldnnumber
//											.toBigDecimal());
//									// old
//									stmt.setBigDecimal(2, oldnmny
//											.toBigDecimal());
//									// oldsubmny
//									stmt.setBigDecimal(3, oldnsubmny
//											.toBigDecimal());
//									// csale_bid
//									stmt.setString(4, csourcebiibid);
//									// csaleid
//									stmt.setString(5, csourcebillhid);
//									stmt.executeUpdate();
//									// 关闭连接
//									stmt.close();
//									con.close();
//									/*System.out
//											.println("回写期初销售出库单的上层期初期初单的累计开票数量");*/
//									SCMEnv.out("回写期初销售出库单的上层期初期初单的累计开票数量");
//									// 再更新销售订单的累计开票数量
//								}
//							}
//
//						}

//					}

				}
			}
			if (stmt != null)
				executeBatch(stmt);

      //检查开票数量和订单数量关系后移至此，在update语句提交后统一进行校验
      String pk_corp = (String) billhead.getAttributeValue("pk_corp");
      UFDouble so26 = SysinitAccessor.getInstance().getParaDbl(pk_corp,"SO26");
      UFBoolean so25 =SysinitAccessor.getInstance().getParaBoolean(pk_corp,"SO25");
      
      if (null == so25 || !so25.booleanValue())
        so26 = new UFDouble(0);
      
      reWrite30InvoiceNum(hssourcebid, so26);
		} catch (Exception e) {
			throw new BusinessException(e.getMessage());
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
			} catch (Exception e) {
			}
			try {
				if (con != null) {
					con.close();
				}
			} catch (Exception e) {
			}
		}

		// 处理开票状态
		if (corder_bids != null && corder_bids.length > 0) {

			try {
				// 查询订单行信息
				nc.impl.scm.so.so001.SaleOrderDMO saledmo = new nc.impl.scm.so.so001.SaleOrderDMO();
				nc.vo.so.so001.SaleorderBVO[] oldordbvos = (nc.vo.so.so001.SaleorderBVO[]) saledmo
						.queryBodyDataForUpdateStatus(corder_bids);
				if (oldordbvos == null || oldordbvos.length <= 0)
					return;

				if (hsntotalinvoicenumber != null) {
					// 设置修改前的出库数量
					for (int i = 0, loop = oldordbvos.length; i < loop; i++) {
						oldordbvos[i]
								.setNtotalinvoicenumber_old((UFDouble) hsntotalinvoicenumber
										.get(oldordbvos[i].getCorder_bid()));
					}
				}

				// 处理开票状态逻辑
				saledmo.processInvoicendState(oldordbvos);

			} catch (Exception e) { 
				throw new BusinessException(e.getMessage());
			}

		}
	}

	/**
	 * 减少(回写)发货数量。 创建日期：(2001-6-21 14:00:35)
	 * 
	 * @return nc.vo.pub.lang.UFDouble
	 * @param strDepartmentID
	 *            java.lang.String
	 */
	public void setDecreaseReceiptNumber(AggregatedValueObject bill)
			throws SQLException, BusinessException {
		/** ********************************************************** */
		// 保留的系统管理接口：
		beforeCallMethod("nc.bs.so.pub.DataControlDMO",
				"setTotalReceiptNumber", new Object[] { bill });
		/** ********************************************************** */
		CircularlyAccessibleValueObject[] billbody = bill.getChildrenVO();
		String sql = "update so_saleexecute set ntotalreceivenumber=isnull(ntotalreceivenumber,0) - ? where csale_bid = ? and csaleid = ?";
		Connection con = null;
		PreparedStatement stmt = null;
		try {
			con = getConnection();
			stmt = con.prepareStatement(sql);
			for (int i = 0; i < billbody.length; i++) {
				// oldnumber
				UFDouble oldnumber = new UFDouble(0);
				if (billbody[i].getStatus() != VOStatus.NEW)
					oldnumber = getOldNumber(bill.getParentVO()
							.getAttributeValue("creceipttype").toString(),
							billbody[i].getPrimaryKey().toString());
				stmt.setBigDecimal(1, oldnumber.toBigDecimal());
				// csale_bid
				stmt.setString(2, billbody[i].getAttributeValue(
						"cupsourcebillbodyid").toString());
				// csaleid
				stmt.setString(3, billbody[i].getAttributeValue(
						"cupsourcebillid").toString());
				stmt.executeUpdate();
			}
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
			} catch (Exception e) {
			}
			try {
				if (con != null) {
					con.close();
				}
			} catch (Exception e) {
			}
		}
		/** ********************************************************** */
		// 保留的系统管理接口：
		afterCallMethod("nc.bs.so.pub.DataControlDMO", "setTotalReceiptNumber",
				new Object[] { bill });
		/** ********************************************************** */
	}

//	/**
//	 * 累计(回写)成本金额。 创建日期：(2001-6-21 14:00:35)
//	 * 
//	 * @return nc.vo.pub.lang.UFDouble
//	 * @param strDepartmentID
//	 *            java.lang.String
//	 */
//	public void setTotalCostmny(AggregatedValueObject bill) throws SQLException {
//
//		CircularlyAccessibleValueObject[] billbody = bill.getChildrenVO();
//
//		String sql = "update so_saleexecute set ntotalcostmny=isnull(ntotalcostmny,0) + ? where csale_bid = ? and csaleid = ?";
//
//		Connection con = null;
//		PreparedStatement stmt = null;
//
//		try {
//			con = getConnection();
//			stmt = con.prepareStatement(sql);
//
//			for (int i = 0; i < billbody.length; i++) {
//				// monny
//				UFDouble nmny = (UFDouble) billbody[i]
//						.getAttributeValue("nmny");
//				if (nmny == null)
//					nmny = new UFDouble(0.0);
//				stmt.setBigDecimal(1, nmny.toBigDecimal());
//				// csale_bid
//				stmt.setString(2, billbody[i].getAttributeValue(
//						"cupsourcebillbodyid").toString());
//				// csaleid
//				stmt.setString(3, billbody[i].getAttributeValue(
//						"cupsourcebillid").toString());
//				stmt.executeUpdate();
//			}
//		} finally {
//			try {
//				if (stmt != null) {
//					stmt.close();
//				}
//			} catch (Exception e) {
//			}
//			try {
//				if (con != null) {
//					con.close();
//				}
//			} catch (Exception e) {
//			}
//		}
//
//	}

	/**
	 * 累计(回写)出库数量。 创建日期：(2001-6-21 14:00:35)
	 * 
	 * @return nc.vo.pub.lang.UFDouble
	 * @param strDepartmentID
	 *            java.lang.String
	 */
	public void setTotalInventoryNumber(AggregatedValueObject bill)
			throws SQLException {
		/** ********************************************************** */
		// 保留的系统管理接口：
		beforeCallMethod("nc.bs.so.pub.DataControlDMO",
				"setTotalInventoryNumber", new Object[] { bill });
		/** ********************************************************** */

		CircularlyAccessibleValueObject[] billbody = bill.getChildrenVO();

		String sql = "update so_saleexecute set ntotalinventorynumber=isnull(ntotalinventorynumber,0) + ? where csale_bid = ? and csaleid = ?";

		Connection con = null;
		PreparedStatement stmt = null;

		try {
			con = getConnection();
			stmt = con.prepareStatement(sql);

			for (int i = 0; i < billbody.length; i++) {
				// nc.vo.scm.pub.SCMEnv.out("-----------------------:" +
				// billbody[i].getAttributeValue("nnumber"));
				// nc.vo.scm.pub.SCMEnv.out("-----------------------:" +
				// billbody[i].getAttributeValue("cupsourcebillbodyid"));
				// nc.vo.scm.pub.SCMEnv.out("-----------------------:" +
				// billbody[i].getAttributeValue("cupsourcebillid"));
				if (billbody[i].getAttributeValue("cupsourcebillbodyid") != null) {
					// number
					UFDouble nnumber = (UFDouble) billbody[i]
							.getAttributeValue("nnumber");
					if (nnumber == null)
						nnumber = new UFDouble(0.0);
					stmt.setBigDecimal(1, nnumber.toBigDecimal());
					// csale_bid
					stmt.setString(2, billbody[i].getAttributeValue(
							"cupsourcebillbodyid").toString());
					// csaleid
					stmt.setString(3, billbody[i].getAttributeValue(
							"cupsourcebillid").toString());

					stmt.executeUpdate();
				}
			}
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
			} catch (Exception e) {
			}
			try {
				if (con != null) {
					con.close();
				}
			} catch (Exception e) {
			}
		}

		/** ********************************************************** */
		// 保留的系统管理接口：
		afterCallMethod("nc.bs.so.pub.DataControlDMO",
				"setTotalInventoryNumber", new Object[] { bill });
		/** ********************************************************** */
	}

	/**
	 * 累计(回写)开票金额。 创建日期：(2001-6-21 14:00:35)
	 * 
	 * @return nc.vo.pub.lang.UFDouble
	 * @param strDepartmentID
	 *            java.lang.String 修改日期：2003-11-22 修改人：杨涛 修改内容：回写内容为价税合计
	 */
	public void setTotalInvoiceMoney(AggregatedValueObject bill)
			throws SQLException, BusinessException {
		/** ********************************************************** */
		// 保留的系统管理接口：
		beforeCallMethod("nc.bs.so.pub.DataControlDMO", "setTotalInvoiceMoney",
				new Object[] { bill });
		/** ********************************************************** */

		// CircularlyAccessibleValueObject[] billbody = bill.getChildrenVO();
		// 如果来源为销售订单直接回写
		// 如果来源为销售出库单（可能包含销售发货单）、销售订单
		// 如果来源期初销售出库单 、期初销售订单
		// 来源单据ID csourcebillhid
		String csourcebillhid = null;
		// 来源单据附表ID csourcebiibid
		String csourcebiibid = null;

		// 现数量
		UFDouble nmny = new UFDouble(0.0);
		// 原数量
		UFDouble oldnmny = new UFDouble(0.0);

//		String sourceBillID[];

		String SourceBillTypeID;

		String SQLUpdate = "update so_saleexecute set ntotalinvoicemny=isnull(ntotalinvoicemny,0) - ? + ? where csale_bid = ?  and csaleid = ?";

		Connection con = null;
		PreparedStatement stmt = null;

		String errMsg = null;

		try {

			for (int i = 0; i < bill.getChildrenVO().length; i++) {
				// 销售发票单子表VO
				CircularlyAccessibleValueObject bodyVO = bill.getChildrenVO()[i];

				if (bodyVO.getAttributeValue("cupreceipttype") != null) {
					// 来源单据ID csourcebillhid
					csourcebillhid = bodyVO
							.getAttributeValue("cupsourcebillid").toString();
					// 来源单据附表ID csourcebiibid
					csourcebiibid = bodyVO.getAttributeValue(
							"cupsourcebillbodyid").toString();

					if (bodyVO.getStatus() != VOStatus.DELETED) {
						// nmny = (UFDouble)bodyVO.getAttributeValue("nmny");
						nmny = (UFDouble) bodyVO.getAttributeValue("nsummny");
						if (nmny == null)
							nmny = new UFDouble(0.0);
					}
					if (bodyVO.getStatus() != VOStatus.NEW
							&& bodyVO.getStatus() != VOStatus.UNCHANGED) {
						oldnmny = getOldMoney(SaleBillType.SaleInvoice, bodyVO
								.getPrimaryKey());
						if (oldnmny == null)
							oldnmny = new UFDouble(0.0);
					}

					// 如果来源是销售订单/期初销售订单 直接回写销售订单的开票数量
					if (bodyVO.getAttributeValue("cupreceipttype").equals(
							nc.ui.scm.so.SaleBillType.SaleOrder)) {

						con = getConnection();
						stmt = con.prepareStatement(SQLUpdate);
						stmt.setBigDecimal(1, oldnmny.toBigDecimal());
						stmt.setBigDecimal(2, nmny.toBigDecimal());
						// csale_bid
						stmt.setString(3, csourcebiibid);
						// csaleid
						stmt.setString(4, csourcebillhid);
						stmt.executeUpdate();
						// 关闭连接
						stmt.close();
						con.close();

					} else {
						// 如果来源为销售出库单（可能包含销售发货单）、销售订单

						if (bodyVO.getAttributeValue("cupreceipttype").equals(
								"4C")) {

							// 第一步 销售出库单子表
							// 累计开票数量
							// SQLUpdate =
							// "update ic_general_bb3 set
							// nsignnum=isnull(nsignnum,0) + ? where cgeneralbid
							// = ? and cgeneralhid = ?";

							// con = getConnection();
							// stmt = con.prepareStatement(SQLUpdate);
							// stmt.setBigDecimal(1, nmny.toBigDecimal());
							// csale_bid
							// stmt.setString(2, csourcebiibid);
							// csaleid
							// stmt.setString(3, csourcebillhid);
							// stmt.executeUpdate();
							// 关闭连接
							// stmt.close();
							// con.close();

							// nc.vo.scm.pub.SCMEnv.out("回写销售出库单的累计开票数量");
							// 第二步 查找销售发票的上上层来源

							SourceBillTypeID = getSourceBillTypeID(bodyVO
									.getAttributeValue("cinvoice_bid")
									.toString());

							if (SourceBillTypeID == null) {
								errMsg = nc.bs.ml.NCLangResOnserver
										.getInstance().getStrByID("sopub",
												"UPPsopub-000082")/*
																	 * @res
																	 * "来自销售出库单的发票的销售出库单来源没有传递给发票"
																	 */;
								BusinessException e = new BusinessException(
										errMsg);
								throw e;

							}

							SQLUpdate = "update so_saleexecute set ntotalinvoicemny=isnull(ntotalinvoicemny,0) - ? + ? where csale_bid = ?  and csaleid = ?";

							// 如果来自销售发货单
							if (SourceBillTypeID
									.equals(nc.ui.scm.so.SaleBillType.SaleReceive)) {
								/**v5.5发货单原有逻辑不适用*/
								/*
								// 先更新销售发货单的累计执行数量
								// 从发票中得到上上层ID

								con = getConnection();
								stmt = con.prepareStatement(SQLUpdate);
								stmt.setBigDecimal(1, oldnmny.toBigDecimal());
								stmt.setBigDecimal(2, nmny.toBigDecimal());
								// csale_bid
								stmt.setString(3, bodyVO.getAttributeValue(
										"csourcebillbodyid").toString());
								// csaleid
								stmt.setString(4, bodyVO.getAttributeValue(
										"csourcebillid").toString());
								stmt.executeUpdate();
								// 关闭连接
								stmt.close();
								con.close();
								nc.vo.scm.pub.SCMEnv
										.out("回写销售出库单的上层发货单的累计开票数量");
								// 再更新销售订单的累计开票数量

								sourceBillID = getSourceID(
										nc.ui.scm.so.SaleBillType.SaleReceipt,
										csourcebiibid);
								con = getConnection();
								stmt = con.prepareStatement(SQLUpdate);
								stmt.setBigDecimal(1, oldnmny.toBigDecimal());
								stmt.setBigDecimal(2, nmny.toBigDecimal());
								// csale_bid
								stmt.setString(3, sourceBillID[1]);
								// csaleid
								stmt.setString(4, sourceBillID[2]);
								nc.vo.scm.pub.SCMEnv
										.out("回写销售出库单的上层发货单的销售订单的累计开票数量");
							*/} else {
								// 更新销售定单的累计执行数量
								if (SourceBillTypeID
										.equals(nc.ui.scm.so.SaleBillType.SaleOrder)) {
									// 先更新销售发货单的累计执行数量
									// 从发票中得到上上层ID

									con = getConnection();
									stmt = con.prepareStatement(SQLUpdate);
									stmt.setBigDecimal(1, oldnmny
											.toBigDecimal());
									stmt.setBigDecimal(2, nmny.toBigDecimal());
									// csale_bid
									stmt.setString(3, bodyVO.getAttributeValue(
											"csourcebillbodyid").toString());
									// csaleid
									stmt.setString(4, bodyVO.getAttributeValue(
											"csourcebillid").toString());
									stmt.executeUpdate();
									// 关闭连接
									stmt.close();
									con.close();
									nc.vo.scm.pub.SCMEnv
											.out("回写销售出库单的上层销售订单的累计开票数量");
								}
							}

						} else {
							// 如果来源期初销售出库单 、期初销售订单

							if (bodyVO
									.getAttributeValue("cupreceipttype")
									.equals(
											nc.ui.scm.so.SaleBillType.SaleInitOutStore)) {

								// //第一步 销售出库单子表
								// //累计开票数量
								// SQLUpdate =
								// "update ic_general_bb3 set
								// nsignnum=isnull(nsignnum,0) + ? where
								// cgeneralbid = ? and cgeneralhid = ?";

								// con = getConnection();
								// stmt = con.prepareStatement(SQLUpdate);
								// stmt.setBigDecimal(1, nmny.toBigDecimal());
								// //csale_bid
								// stmt.setString(2, csourcebiibid);
								// //csaleid
								// stmt.setString(3, csourcebillhid);
								// stmt.executeUpdate();
								// //关闭连接
								// stmt.close();
								// con.close();

								// nc.vo.scm.pub.SCMEnv.out("回写期初销售出库单的累计开票数量");

								SourceBillTypeID = getSourceBillTypeID(bodyVO
										.getAttributeValue("cinvoice_bid")
										.toString());

								if (SourceBillTypeID == null) {
									errMsg = nc.bs.ml.NCLangResOnserver
											.getInstance().getStrByID("sopub",
													"UPPsopub-000083")/*
																		 * @res
																		 * "来自期初销售出库单的期初发票的期初销售出库单来源没有传递给发票"
																		 */;
									BusinessException e = new BusinessException(
											errMsg);
									throw e;

								}

								SQLUpdate = "update so_saleexecute set ntotalinvoicemny=isnull(ntotalinvoicemny,0) - ? + ? where csale_bid = ?  and csaleid = ?";

							}

						}

					}

				}
			}

		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
			} catch (Exception e) {
			}
			try {
				if (con != null) {
					con.close();
				}
			} catch (Exception e) {
			}
		}

		/** ********************************************************** */
		// 保留的系统管理接口：
		afterCallMethod("nc.bs.so.pub.DataControlDMO", "setTotalInvoiceMoney",
				new Object[] { bill });
		/** ********************************************************** */
	}

	/**
	 * 
	 * 方法功能描述：销售发票新增修改时回写源头和上游累计开票数量、金额。。
	 * <p>
	 * <b>examples:</b>
	 * <p>
	 * 使用示例
	 * <p>
	 * <b>参数说明</b>
	 * @param bill
	 * @throws SQLException
	 * @throws BusinessException
	 * <p>
	 * @author fengjb
	 * @time 2009-11-25 上午11:10:18
	 */
	public void setTotalInvoiceNum(AggregatedValueObject bill)
			throws SQLException, BusinessException{

		CircularlyAccessibleValueObject[] billbody = bill.getChildrenVO();
		CircularlyAccessibleValueObject billhead = bill.getParentVO();
		if (billhead == null || billbody == null || billbody.length <= 0)
			return;

		//过滤得到所有变化的行，顺序不可调整
		Vector<CircularlyAccessibleValueObject> vTemp = new Vector<CircularlyAccessibleValueObject>();
		for(int i = 0; i < billbody.length; i++){
			if(billbody[i].getStatus() == VOStatus.DELETED){
              vTemp.addElement(billbody[i]);
			}
		}
		for(int i = 0; i < billbody.length; i++){
			if(billbody[i].getStatus() == VOStatus.UPDATED){
             vTemp.addElement(billbody[i]);
			}
		}
		for(int i = 0; i < billbody.length; i++){
			if(billbody[i].getStatus() != VOStatus.UPDATED && billbody[i].getStatus() != VOStatus.DELETED){ 
             vTemp.addElement(billbody[i]);
			}
		}

		billbody = new CircularlyAccessibleValueObject[vTemp.size()];
		vTemp.copyInto(billbody);
		//2007-12-11 xhq
		
		ArrayList alistids = new ArrayList();
		String[] corder_bids = null;

		// 如果来源为销售订单直接回写
		// 如果来源为销售出库单源头是销售订单

		// 源单据ID csourcebillhid
		String csourcebillhid = null;
		// 源单据附表ID csourcebiibid
		String csourcebiibid = null;
		// 来源单据ID csourcebillhid
		String cupsourcebillhid = null;
		// 来源单据附表ID csourcebiibid
		String cupsourcebiibid = null;

		String creceipttype = null;

		for (int i = 0, loop = billbody.length; i < loop; i++) {
			creceipttype = billbody[i].getAttributeValue("creceipttype") == null ? ""
					: billbody[i].getAttributeValue("creceipttype").toString();
			if (!"30".equals(creceipttype))
				continue;
			Object id = billbody[i].getAttributeValue("csourcebillbodyid");
			if (id != null && !alistids.contains(id)) {
				alistids.add(id);
			}
		}

		// 更新前保留开票数量，处理开票状态

		HashMap hsntotalinvoicenumber = null;
		if (alistids.size() > 0) {
			corder_bids = (String[]) alistids.toArray(new String[alistids
					.size()]);
			hsntotalinvoicenumber = SOToolsDMO.getAnyValueUFDouble(
					"so_saleexecute", "ntotalinvoicenumber", "csale_bid",
					corder_bids, " creceipttype = '30' ");

		}

		// 现数量
		UFDouble nnumber = new UFDouble(0.0);
		// 原数量
		UFDouble oldnnumber = new UFDouble(0.0);
		// 现金额
		UFDouble nmny = new UFDouble(0.0);
		// 原金额
		UFDouble oldnmny = new UFDouble(0.0);
		// 现冲减金额
		UFDouble nsubmny = new UFDouble(0.0);
		// 原冲减金额
		UFDouble oldnsubmny = new UFDouble(0.0);

		Hashtable[] hasdata = getOldData(SaleBillType.SaleInvoice, billhead
				.getPrimaryKey());

		Hashtable hasNumber = hasdata[0];
		Hashtable hasNmny = hasdata[1];
		Hashtable hasSubmny = hasdata[2];
    
		//更新销售执行明细中的累计开票数量和金额
        HashSet<String> hssourcebid = new HashSet<String>();
		String SQLUpdate = "update so_saleexecute set ntotalinvoicenumber=isnull(ntotalinvoicenumber,0) - ? + ? ,ntotalinvoicemny=isnull(ntotalinvoicemny,0) - ? + ? ,narsubinvmny=isnull(narsubinvmny,0) - ? + ? where csale_bid = ?  and csaleid = ? and creceipttype = '30' ";

		Connection con = null;
		PreparedStatement stmtSQLUpdate = null;
        //更新库存BB3表中的累计开票数量和金额
//		String SQLOutUpdate = "update ic_general_bb3 set nsignnum=isnull(nsignnum,0) - ? + ? where cgeneralbid = ?  and cgeneralhid = ?";

//		PreparedStatement stmtSQLOutUpdate = null;

		try {

			con = getConnection();

//			SaleinvoiceDMO invDMO = new SaleinvoiceDMO();

			for (int i = 0; i < billbody.length; i++) {
				// 销售发票单子表VO

				CircularlyAccessibleValueObject bodyVO = billbody[i];
                //存在上游单据
				if (bodyVO.getAttributeValue("cupreceipttype") != null
						&& bodyVO.getAttributeValue("cupsourcebillid") != null) {
					// 源头单据类型 creceipttype
					creceipttype = (String) bodyVO
							.getAttributeValue("creceipttype");
					// 来源单据ID csourcebillhid
					cupsourcebillhid = (String) bodyVO
							.getAttributeValue("cupsourcebillid");
					// 来源单据附表ID csourcebiibid
					cupsourcebiibid = (String) bodyVO
							.getAttributeValue("cupsourcebillbodyid");
					// 源头单据ID
					csourcebillhid = (String) bodyVO
							.getAttributeValue("csourcebillid");
					// 源头单据附表ID
					csourcebiibid = (String) bodyVO
							.getAttributeValue("csourcebillbodyid");
                    //来源单据ID
					String cupreceipttype = (String) bodyVO
							.getAttributeValue("cupreceipttype");
//					if (cupreceipttype.equals("32")) {
//						SaleinvoiceBVO upvo = invDMO
//								.queryBodyDataByBID(cupsourcebiibid);
//						cupsourcebillhid = upvo.getCupsourcebillid();
//						cupsourcebiibid  = upvo.getCupsourcebillbodyid();
//						cupreceipttype   = upvo.getCupreceipttype();
//					}

					oldnnumber = new UFDouble(0.0);
					oldnmny = new UFDouble(0.0);
					nnumber = new UFDouble(0.0);
					nmny = new UFDouble(0.0);
					if (csourcebillhid == null || csourcebiibid == null
							|| cupsourcebillhid == null
							|| cupsourcebiibid == null) {
						throw new BusinessException(
								"单据来源ID存在空值，不能更新累计开票数量金额，请检查！");
					}

					if (bodyVO.getStatus() != VOStatus.DELETED) {
						nnumber = bodyVO.getAttributeValue("nnumber") == null ? new UFDouble(
								0)
								: (UFDouble) bodyVO
										.getAttributeValue("nnumber");

						// nmny
						nmny = bodyVO.getAttributeValue("nsummny") == null ? new UFDouble(
								0)
								: (UFDouble) bodyVO
										.getAttributeValue("nsummny");

						// nsubmny
						UFDouble nsubcursummny = bodyVO
								.getAttributeValue("nsubcursummny") == null ? new UFDouble(
								0)
								: (UFDouble) bodyVO
										.getAttributeValue("nsubcursummny");
						nsubmny = nsubcursummny.sub(nmny);
					}
					if (bodyVO.getStatus() != VOStatus.NEW
							&& bodyVO.getStatus() != VOStatus.UNCHANGED) {
						if (hasNumber.containsKey(bodyVO.getPrimaryKey()))
							oldnnumber = (UFDouble) hasNumber.get(bodyVO
									.getPrimaryKey());
						else
							oldnnumber = new UFDouble(0.0);
						if (hasNmny.containsKey(bodyVO.getPrimaryKey()))
							oldnmny = (UFDouble) hasNmny.get(bodyVO
									.getPrimaryKey());
						else
							oldnmny = new UFDouble(0.0);
						if (hasSubmny.containsKey(bodyVO.getPrimaryKey()))
							oldnsubmny = (UFDouble) hasSubmny.get(bodyVO
									.getPrimaryKey());
						else
							oldnsubmny = new UFDouble(0.0);
					}

					// 如果来源是销售订单  直接回写销售订单的开票数量
					if (cupreceipttype.equals(SaleBillType.SaleOrder)) {

						//2007-11-20 xhq
//						IICToSO icTOso = (IICToSO) NCLocator.getInstance().lookup(IICToSO.class.getName());
//						nc.bs.pub.para.SysInitDMO initdmo = new nc.bs.pub.para.SysInitDMO();
//						UFDouble so26 = initdmo.getParaDbl((String) billhead.getAttributeValue("pk_corp"),"SO26");
//						UFBoolean so25 = initdmo.getParaBoolean((String) billhead.getAttributeValue("pk_corp"),"SO25");
//						if (!so25.booleanValue())
//                       so26 = new UFDouble(0);
//						Hashtable<String, UFDouble> hs = new Hashtable<String, UFDouble>();
//						hs.put(csourcebiibid, nnumber.sub(oldnnumber));
//						reWrite30InvoiceNum(hs, so26);
                        hssourcebid.add(csourcebiibid);
//						2007-11-20 xhq

						if (stmtSQLUpdate == null)
							stmtSQLUpdate = prepareStatement(con, SQLUpdate);

						// old
						stmtSQLUpdate.setBigDecimal(1, oldnnumber
								.toBigDecimal());
						// new
						stmtSQLUpdate.setBigDecimal(2, nnumber.toBigDecimal());
						// old
						stmtSQLUpdate.setBigDecimal(3, oldnmny.toBigDecimal());
						// new
						stmtSQLUpdate.setBigDecimal(4, nmny.toBigDecimal());
						// oldsubmny
						stmtSQLUpdate.setBigDecimal(5, oldnsubmny
								.toBigDecimal());
						// nsubmny
						stmtSQLUpdate.setBigDecimal(6, nsubmny.toBigDecimal());
						// csale_bid
						stmtSQLUpdate.setString(7, cupsourcebiibid);
						// csaleid
						stmtSQLUpdate.setString(8, cupsourcebillhid);

						executeUpdate(stmtSQLUpdate);

						// nc.vo.scm.pub.SCMEnv.out("回写更新----------------直接来源销售订单-------------");
                        //如果来源为销售出库单  源头为销售订单
					} else if (cupreceipttype.equals(SaleBillType.SaleOutStore)) {

							IICToSO icTOso = (IICToSO) NCLocator.getInstance().lookup(IICToSO.class.getName());
							nc.bs.pub.para.SysInitDMO initdmo = new nc.bs.pub.para.SysInitDMO();
							UFDouble so65 = initdmo.getParaDbl((String) billhead.getAttributeValue("pk_corp"),"SO65");
							UFBoolean so64 = initdmo.getParaBoolean((String) billhead.getAttributeValue("pk_corp"),"SO64");
//							UFDouble so26 = initdmo.getParaDbl((String) billhead.getAttributeValue("pk_corp"),"SO26");
//							UFBoolean so25 = initdmo.getParaBoolean((String) billhead.getAttributeValue("pk_corp"),"SO25");

							// V5不允许超出库单开票
							if (!so64.booleanValue()){ 
								so65 = new UFDouble(0);
							}
//							if (!so25.booleanValue()) so26 = new UFDouble(0);
							Hashtable<String, UFDouble> hs = new Hashtable<String, UFDouble>();
							hs.put(cupsourcebiibid, nnumber.sub(oldnnumber));
							icTOso.reWrite4CInvoiceNum(hs, so65);

							// 第一步 销售出库单子表

							// 第二步 查找销售发票的上上层来源
							// 再更新销售订单的累计开票数量
							if (SaleBillType.SaleOrder.equals(creceipttype)) {
//								2007-11-20 xhq
//								hs = new Hashtable<String, UFDouble>();
//								hs.put(csourcebiibid, nnumber.sub(oldnnumber));
//								reWrite30InvoiceNum(hs, so26);
                                hssourcebid.add(csourcebiibid);
//								2007-11-20 xhq
								if (stmtSQLUpdate == null)
									stmtSQLUpdate = prepareStatement(con,SQLUpdate);

								stmtSQLUpdate.setBigDecimal(1, oldnnumber
										.toBigDecimal());
								stmtSQLUpdate.setBigDecimal(2, nnumber
										.toBigDecimal());
								// old
								stmtSQLUpdate.setBigDecimal(3, oldnmny
										.toBigDecimal());
								// new
								stmtSQLUpdate.setBigDecimal(4, nmny
										.toBigDecimal());
								// oldsubmny
								stmtSQLUpdate.setBigDecimal(5, oldnsubmny
										.toBigDecimal());
								// nsubmny
								stmtSQLUpdate.setBigDecimal(6, nsubmny
										.toBigDecimal());
								// csale_bid
								stmtSQLUpdate.setString(7, csourcebiibid);
								// csaleid
								stmtSQLUpdate.setString(8, csourcebillhid);

								executeUpdate(stmtSQLUpdate);

							}

						} 
//						else {
//							// 如果来源期初销售出库单 、期初销售订单
//
//							if (cupreceipttype
//									.equals(nc.ui.scm.so.SaleBillType.SaleInitOutStore)) {
//
//								// 第一步 销售出库单子表
//
//								if (stmtSQLOutUpdate == null)
//									stmtSQLOutUpdate = prepareStatement(con,
//											SQLOutUpdate);
//
//								stmtSQLOutUpdate.setBigDecimal(1, oldnnumber
//										.toBigDecimal());
//								stmtSQLOutUpdate.setBigDecimal(2, nnumber
//										.toBigDecimal());
//								// csale_bid
//								stmtSQLOutUpdate.setString(3, cupsourcebiibid);
//								// csaleid
//								stmtSQLOutUpdate.setString(4, cupsourcebillhid);
//
//								executeUpdate(stmtSQLOutUpdate);
//
//								if (nc.ui.scm.so.SaleBillType.SaleOrder
//										.equals(creceipttype)) {
//
//									if (stmtSQLUpdate == null)
//										stmtSQLUpdate = prepareStatement(con,
//												SQLUpdate);
//
//									stmtSQLUpdate.setBigDecimal(1, oldnnumber
//											.toBigDecimal());
//									stmtSQLUpdate.setBigDecimal(2, nnumber
//											.toBigDecimal());
//									// old
//									stmtSQLUpdate.setBigDecimal(3, oldnmny
//											.toBigDecimal());
//									// new
//									stmtSQLUpdate.setBigDecimal(4, nmny
//											.toBigDecimal());
//									// oldsubmny
//									stmtSQLUpdate.setBigDecimal(5, oldnsubmny
//											.toBigDecimal());
//									// nsubmny
//									stmtSQLUpdate.setBigDecimal(6, nsubmny
//											.toBigDecimal());
//									// csale_bid
//									stmtSQLUpdate.setString(7, csourcebiibid);
//									// csaleid
//									stmtSQLUpdate.setString(8, csourcebillhid);
//
//									executeUpdate(stmtSQLUpdate);
//
//								}
//
//							}
//
//						}

//					}

				}
			}

			if (stmtSQLUpdate != null)
				executeBatch(stmtSQLUpdate);

//			if (stmtSQLOutUpdate != null)
//				executeBatch(stmtSQLOutUpdate);
      //检查开票数量和订单数量关系后移至此，在update语句提交后统一进行校验
      String pk_corp = (String) billhead.getAttributeValue("pk_corp");
      UFDouble so26 = SysinitAccessor.getInstance().getParaDbl(pk_corp,"SO26");
      UFBoolean so25 =SysinitAccessor.getInstance().getParaBoolean(pk_corp,"SO25");
      
      if (null == so25 || !so25.booleanValue())
        so26 = new UFDouble(0);
      
      reWrite30InvoiceNum(hssourcebid, so26);


		} catch (BusinessException e) {
			SCMEnv.out(">>>>>>>>销售发票回写上游单据信息出错"+e);
			throw e;
		} catch (Exception e) {
			SCMEnv.out(">>>>>>>销售发票回写上游单据信息出错"+e);
			throw new BusinessException("回写上游单据信息出错："+ e);
		} finally {
			try {
				if (stmtSQLUpdate != null) {
					stmtSQLUpdate.close();
				}
			} catch (Exception e) {
			}
//			try {
//				if (stmtSQLOutUpdate != null) {
//					stmtSQLOutUpdate.close();
//				}
//			} catch (Exception e) {
//			}
			try {
				if (con != null) {
					con.close();
				}
			} catch (Exception e) {
			}
		}

		// 处理开票状态
		if (corder_bids != null && corder_bids.length > 0) {

			try {
				// 查询订单行信息
				nc.impl.scm.so.so001.SaleOrderDMO saledmo = new nc.impl.scm.so.so001.SaleOrderDMO();
				nc.vo.so.so001.SaleorderBVO[] oldordbvos = (nc.vo.so.so001.SaleorderBVO[]) saledmo
						.queryBodyDataForUpdateStatus(corder_bids);
				if (oldordbvos == null || oldordbvos.length <= 0)
					return;

				if (hsntotalinvoicenumber != null) {
					// 设置修改前的出库数量
					for (int i = 0, loop = oldordbvos.length; i < loop; i++) {
						oldordbvos[i]
								.setNtotalinvoicenumber_old((UFDouble) hsntotalinvoicenumber
										.get(oldordbvos[i].getCorder_bid()));
					}
				}

				// 处理开票状态逻辑
				saledmo.processInvoicendState(oldordbvos);

			} catch (BusinessException e) {
				SCMEnv.out(">>>>>>>发票回写上游信息处理开票状态异常"+e);
				throw e;
			} catch (Exception e) {
				SCMEnv.out(">>>>>>>发票回写上游信息处理开票状态异常"+e);
				throw new BusinessException(e);
			}

		}

	}

	 /**
	  * 回写销售订单累计开票数量
	  * 
	  * @param hsNum：
	  * key--存放销售订单表体id String
	  * value--存放本次修改的差值 UFDouble
	  *           
	  * @param soparam 超订单开票比例，so参数，为20或10
	  * @throws java.lang.Exception
	  * 2007-11-20 xhq
	  */
	  public void reWrite30InvoiceNum(HashSet<String> hssourcebid,UFDouble soparam)
	    throws BusinessException{
		  
	    if(null == hssourcebid || hssourcebid.size() <= 0) 
        return ;
      //计算超订单开票比例
	    if(null == soparam)
	      soparam = new UFDouble(1);
	    else
	      soparam = soparam.div(new UFDouble(100)).add(new UFDouble(1));
	    
	    Connection con = null;
	    PreparedStatement stmt = null;	    
	    try{
        //来源订单号ID
	      String[] bids = hssourcebid.toArray(new String[0]);
	      boolean isLockSucc = false;
	      isLockSucc = LockTool.setDynamicLockForPks(bids,null);
		  if (!isLockSucc) {
			throw new BusinessException(nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("sopub", "UPPsopub-000080")/* @res* "存在并发操作，请稍后再试"*/);
		  }

	      String sql = "select csale_bid,nnumber,nrushnum,ntotalinvoicenumber,invcode,invname ";
	      sql += "from so_saleexecute A, so_saleorder_b B, bd_invbasdoc C where A.dr = 0 and B.dr = 0 and A.csale_bid = B.corder_bid ";
	      sql += "and cinvbasdocid = pk_invbasdoc "+ GeneralSqlString.formInSQL("A.csale_bid", bids);
	      
	      con = getConnection();
	      stmt = con.prepareStatement(sql);
	      ResultSet rs = stmt.executeQuery();
	      HashMap hmnum = new HashMap();
	      String id = null, invcode = null, invname = null;
	      Object oTemp = null;
	      UFDouble nNumber = null, nRushNum = null, nSignNum = null;
	      while(rs.next()){
	    	  id = rs.getString(1);
	    	  
          //订单数量
	    	  oTemp = rs.getObject(2);
	    	  if(null !=oTemp) 
            nNumber = new UFDouble(oTemp.toString());
	    	  else 
            nNumber = new UFDouble(0);
          //累计出库对冲数量
	    	  oTemp = rs.getObject(3);
	    	  if(null !=oTemp) 
            nRushNum = new UFDouble(oTemp.toString());
	    	  else 
            nRushNum = new UFDouble(0);
          //累计开票数量
	    	  oTemp = rs.getObject(4);
	    	  if(null !=oTemp) 
            nSignNum = new UFDouble(oTemp.toString());
	    	  else 
            nSignNum = new UFDouble(0);
          //存货编码
	    	  invcode = rs.getString(5);
	    	  //存货名称
          invname = rs.getString(6);
	    	  
	    	  if(null != id) 
            hmnum.put(id, new Object[]{nNumber,nRushNum,nSignNum,invcode,invname});
	      }
        
	      if(null !=rs) 
          rs.close();
	      
	      boolean haveError = false;
	      StringBuilder sb_throwmsg = new StringBuilder(nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("sopub","UPPsopub-000064")/*@res "开票数量大于订单数量"*/).append("\n");
	      
	      Object o[] = null;
	      UFDouble dTemp = null;
	      for(int i = 0,iloop = bids.length; i <iloop; i++){
	        haveError = false;
	  
	        oTemp = hmnum.get(bids[i]);
	        if(null == oTemp) 
            continue;	  
          
	        o = (Object[])oTemp;
	        nNumber = (UFDouble)o[0];
	        nRushNum = (UFDouble)o[1];
	        nSignNum = (UFDouble)o[2];
	        invcode = (String)o[3];
	        invname = (String)o[4];
	                  
	        dTemp = nNumber.multiply(soparam).sub(nRushNum).sub(nSignNum);
          //2009-03-17 V56 fengjb 订单数量和累计开票数量同号 
          if(nNumber.multiply(nSignNum).compareTo(new UFDouble(0)) < 0 )
            haveError = true;
          else if(nNumber.doubleValue() >= 0){
	          if(dTemp.doubleValue() < 0) haveError = true;
	        }else{
	          if(dTemp.doubleValue() > 0) haveError = true;
	        }
	        
	        if(haveError){
	        	sb_throwmsg.append(nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("sopub","UPPsopub-000060")/*@res "存货编码"*/ + ":")
	                  .append(invcode).append(";");
	            sb_throwmsg.append(nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("sopub","UPPsopub-000061")/*@res "存货名称"*/  + ":")
	                  .append(invname).append(";");
	            throw new BusinessException (sb_throwmsg.toString());       
	        }
	      }
	      	      	      
	    }catch(Exception e){
	      throw new BusinessException(e.getMessage());
	    }finally{
	    	try{
	    		if(stmt != null) stmt.close();
	    		if(con != null) con.close();
	    	}catch(Exception ex){}
	    }
	    
	  }
	  
	/*
	 * 红蓝出库单对冲时更新订单开票状态,不更新订单累计开票数量 2007-08-30 xhq
	 */
	public void setTotalInvoiceNumForRush(AggregatedValueObject bill)
			throws SQLException, BusinessException{

		CircularlyAccessibleValueObject[] billbody = bill.getChildrenVO();
		CircularlyAccessibleValueObject billhead = bill.getParentVO();
		if (billhead == null || billbody == null || billbody.length <= 0)
			return;

		ArrayList alistids = new ArrayList();
		String[] corder_bids = null;

		String creceipttype = null;
		HashMap hRushNum = new HashMap();// 订单行对应的累计对冲数量
		UFDouble dTemp = null;

		for (int i = 0, loop = billbody.length; i < loop; i++) {
			creceipttype = billbody[i].getAttributeValue("creceipttype") == null ? ""
					: billbody[i].getAttributeValue("creceipttype").toString();
			if (!"30".equals(creceipttype))
				continue;
			Object id = billbody[i].getAttributeValue("csourcebillbodyid");
			if (id != null && !alistids.contains(id)) {
				alistids.add(id);
			}
			if (id != null) {
				if (hRushNum.get(id) == null) {
					hRushNum.put(id, (UFDouble) billbody[i]
							.getAttributeValue("nnumber"));
				} else {
					dTemp = (UFDouble) hRushNum.get(id);
					dTemp = dTemp.add((UFDouble) billbody[i]
							.getAttributeValue("nnumber"));
					hRushNum.put(id, dTemp);
				}
			}
		}

		// 更新前保留开票数量，处理开票状态
		HashMap hsntotalinvoicenumber = null;
		if (alistids.size() > 0) {
			corder_bids = (String[]) alistids.toArray(new String[alistids
					.size()]);
			hsntotalinvoicenumber = SOToolsDMO.getAnyValueUFDouble(
					"so_saleexecute", "ntotalinvoicenumber", "csale_bid",
					corder_bids, " creceipttype = '30' ");

		}

		// 处理开票状态
		if (corder_bids != null && corder_bids.length > 0) {

			try {
				// 查询订单行信息
				nc.impl.scm.so.so001.SaleOrderDMO saledmo = new nc.impl.scm.so.so001.SaleOrderDMO();
				nc.vo.so.so001.SaleorderBVO[] oldordbvos = (nc.vo.so.so001.SaleorderBVO[]) saledmo
						.queryBodyDataForUpdateStatus(corder_bids);
				if (oldordbvos == null || oldordbvos.length <= 0)
					return;

				UFDouble d = new UFDouble(0);
				if (hsntotalinvoicenumber != null) {
					// 设置修改前的出库数量
					for (int i = 0, loop = oldordbvos.length; i < loop; i++) {
						dTemp = (UFDouble) hsntotalinvoicenumber
								.get(oldordbvos[i].getCorder_bid());
						if (dTemp == null)
							dTemp = new UFDouble(0);
						if (hRushNum.get(oldordbvos[i].getCorder_bid()) != null)
							d = (UFDouble) hRushNum.get(oldordbvos[i]
									.getCorder_bid());
						oldordbvos[i].setNtotalinvoicenumber_old(dTemp.sub(d));
						oldordbvos[i].setNtotalinvoicenumber(dTemp);
					}
				}

				// 处理开票状态逻辑
				saledmo.processInvoicendState(oldordbvos);

			} catch (BusinessException e) {
				throw e;
			} catch (Exception e) {
				throw new BusinessException("Remote Call", e);
			}

		}

	}

	/**
	 * 获得原始数量。 创建日期：(2001-9-3 9:30:24)
	 * 
	 * @return nc.vo.pub.lang.UFDouble
	 * 
	 * 修改日期：2003-12-16 修改内容：由无税金额nmny->改为价税合计nsummny
	 */
	private Hashtable[] getOldData(String strBillType, String strID)
			throws SQLException {

		Hashtable[] hasreturn = new Hashtable[3];

		UFDouble nnumber = new UFDouble(0.0);
		UFDouble nmny = new UFDouble(0.0);
		UFDouble nsubmny = new UFDouble(0);
		// String strSQL = "Select nnumber,nmny, ";
		String strSQL = "select nnumber,nsummny, ";
		String strField = null;
		String strTable = null;

		// 订单
		if (strBillType.equals(SaleBillType.SaleOrder)) {
			strTable = " so_saleorder_b ";
			strField = "corder_bid";
		}
		// 发票
		if (strBillType.equals(SaleBillType.SaleInvoice)) {

			strTable = " so_saleinvoice_b ";
			strField = "cinvoice_bid, nsubcursummny - nsummny as submny ";
		}

		strSQL = strSQL + strField + " from " + strTable
				+ " where csaleid =  ? ";

		Connection con = null;
		PreparedStatement stmt = null;
		Hashtable hNumber = new Hashtable();
		Hashtable hNmny = new Hashtable();
		Hashtable hSubmny = null;

		try {
			con = getConnection();
			stmt = con.prepareStatement(strSQL);

			// pk
			stmt.setString(1, strID);

			ResultSet rs = stmt.executeQuery();
			//
			BigDecimal n = null;
			String id = null;

			while (rs.next()) {
				n = (BigDecimal) rs.getObject(1);
				nnumber = (n == null ? new UFDouble(0) : new UFDouble(n));
				n = (BigDecimal) rs.getObject(2);
				nmny = (n == null ? new UFDouble(0) : new UFDouble(n));
				id = rs.getString(3);
				if (strBillType.equals(SaleBillType.SaleInvoice)) {
					BigDecimal dTemp = rs.getBigDecimal(4);
					nsubmny = (dTemp == null ? new UFDouble(0) : new UFDouble(
							dTemp));
				}

				hNumber.put(id, nnumber);
				hNmny.put(id, nmny);

				if (hSubmny == null)
					hSubmny = new Hashtable();
				hSubmny.put(id, nsubmny);

			}
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
			} catch (Exception e) {
			}
			try {
				if (con != null) {
					con.close();
				}
			} catch (Exception e) {
			}
		}

		hasreturn[0] = hNumber;
		hasreturn[1] = hNmny;
		hasreturn[2] = hSubmny;

		return hasreturn;
	}

	/**
	 * HANWEI 功能：获得加工过的OperatelogVO 参数： 返回： 例外： 日期：(2004-6-24 15:46:23)
	 * 修改日期，修改人，修改原因，注释标志：
	 * 
	 * @return nc.vo.sm.log.OperatelogVO
	 * @param sMsg
	 *            java.lang.String
	 * @param sEnterbutton
	 *            java.lang.String
	 * @param sMessgageType
	 *            java.lang.String
	 * @param vo
	 *            nc.vo.pub.AggregatedValueObject
	 */
	public nc.vo.sm.log.OperatelogVO getOperLogVO(String sMsg,
			String sEnterbutton, String sMessgageType, String cuserid,
			String scudate, nc.vo.pub.AggregatedValueObject vo) {

		if (vo == null || vo.getParentVO() == null)
			return null;

		if ("ERROR".equalsIgnoreCase(sMessgageType)) {
			// 异常信息
			sMsg = nc.vo.scm.funcs.Businesslog.MSGERROR + sMsg;
		} else {
			if (sMsg == null || sMsg.trim().length() == 0) {
				sMsg = nc.vo.scm.funcs.Businesslog.MSGMESSAGE;
			} else {
				// 警告信息
				sMsg = nc.vo.scm.funcs.Businesslog.MSGWARNING + sMsg;
			}
		}

		try {

			FetchValueDMO fetchdmo = new FetchValueDMO();

			if (vo.getClass() == SaleOrderVO.class) {

				SaleOrderVO ordvo = (SaleOrderVO) vo;
				nc.vo.sm.log.OperatelogVO operatelogvo = new nc.vo.sm.log.OperatelogVO();

				SaleorderHVO ordhvo = ordvo.getHeadVO();

				String cusername = ordvo.getCusername();

				String ccorpname = ordvo.getCcorpname();

				if (ccorpname == null || ccorpname.trim().length() <= 0) {
					ccorpname = fetchdmo.fetchValue("bd_corp", "unitname",
							" pk_corp ='" + ordhvo.getPk_corp() + "' ");
				}

				if (cuserid == null || cuserid.trim().length() == 0)
					cuserid = ordhvo.getCoperatorid();

				if ((cusername == null || cusername.trim().length() <= 0)
						&& cuserid != null) {
					cusername = fetchdmo.fetchValue("sm_user", "user_name",
							" cuserid ='" + cuserid + "' ");
				}

				operatelogvo.setCompanyname(ccorpname);

				operatelogvo.setOpratorname(cusername);

				operatelogvo
						.setEnterfunction((ordvo.getCnodename() == null || ordvo
								.getCnodename().trim().length() <= 0) ? nc.bs.ml.NCLangResOnserver
								.getInstance().getStrByID("40060301",
										"UPT40060301-000021")
								/* @res "销售订单" */
								: ordvo.getCnodename());
				operatelogvo.setEnterbutton(sEnterbutton);

				if (ordvo.getIAction() == ISaleOrderAction.A_ADD) {
					operatelogvo.setEnterbutton(nc.bs.ml.NCLangResOnserver
							.getInstance().getStrByID("SCMCOMMON",
									"UPPSCMCommon-000288")
					/* @res "新增" */);
				} else if (ordvo.getIAction() == ISaleOrderAction.A_EDIT) {
					operatelogvo
							.setEnterbutton(nc.bs.ml.NCLangResOnserver
									.getInstance().getStrByID("common",
											"UC001-0000045")/* @res "修改" */);
				} else if (ordvo.getIAction() == ISaleOrderAction.A_MODIFY) {
					operatelogvo.setEnterbutton(nc.bs.ml.NCLangResOnserver
							.getInstance().getStrByID("SCMCOMMON",
									"UPPSCMCommon-000290")/* @res "修订" */);
				} else if (ordvo.getIAction() == ISaleOrderAction.A_AUDIT) {
					operatelogvo
							.setEnterbutton(nc.bs.ml.NCLangResOnserver
									.getInstance().getStrByID("common",
											"UC001-0000027")/* @res "审批" */);
				} else if (ordvo.getIAction() == ISaleOrderAction.A_UNAUDIT) {
					operatelogvo
							.setEnterbutton(nc.bs.ml.NCLangResOnserver
									.getInstance().getStrByID("common",
											"UC001-0000028")/* @res "弃审" */);
				} else if (ordvo.getIAction() == ISaleOrderAction.A_BLANKOUT) {
					operatelogvo
							.setEnterbutton(nc.bs.ml.NCLangResOnserver
									.getInstance().getStrByID("common",
											"UC001-0000005")/* @res "作废" */);
				} else if (ordvo.getIAction() == ISaleOrderAction.A_FREEZE) {
					operatelogvo
							.setEnterbutton(nc.bs.ml.NCLangResOnserver
									.getInstance().getStrByID("common",
											"UC001-0000030")/* @res "冻结" */);
				} else if (ordvo.getIAction() == ISaleOrderAction.A_UNFREEZE) {
					operatelogvo
							.setEnterbutton(nc.bs.ml.NCLangResOnserver
									.getInstance().getStrByID("common",
											"UC001-0000031")/* @res "解冻" */);
				} else if (ordvo.getIAction() == ISaleOrderAction.A_CLOSE) {
					operatelogvo.setEnterbutton(nc.bs.ml.NCLangResOnserver
							.getInstance().getStrByID("SCMCOMMON",
									"UPPSCMCommon-000128")/* @res "结束" */);
				}
				operatelogvo.setBillcode(ordhvo.getVreceiptcode());
				operatelogvo.setBillType("30");
				operatelogvo.setBillId(ordhvo.getCsaleid());
				operatelogvo.setBusiType(ordhvo.getCbiztype());
				operatelogvo.setPKCorp(ordhvo.getPk_corp());
				operatelogvo.setOperatorId(cuserid);
	
				UFDateTime logtime = new UFDateTime(System.currentTimeMillis());
				if (logtime == null && ordvo.getDcurdate() != null) {
					logtime = new UFDateTime(ordvo.getDcurdate().toString());
				}
				operatelogvo.setLoginTime(logtime == null ? "" : logtime
						.toString());
				operatelogvo.setEnterip(ordvo.getLogip());
				operatelogvo
						.setOperatetype(nc.vo.sm.log.OperatelogVO.BUSINESS_LOG);

				if (sMsg != null && sMsg.length() > 3999) {
					sMsg = sMsg.substring(0, 3999);
				}
				operatelogvo.setBusinessLog(sMsg);

				return operatelogvo;

			}

		} catch (Exception e) {
			/** 辅助功能，不向外抛异常 */
			nc.vo.scm.pub.SCMEnv.out(nc.bs.ml.NCLangResOnserver.getInstance()
					.getStrByID("4008check", "UPP4008check-000069")/*
																	 * @res
																	 * "nc.bs.ic.pub.check.CheckDMO.insertBusinesslog插入业务日志出现错误！"
																	 */);
			SCMEnv.out(e);
		}

		return null;
	}

	/**
	 * 发货单插入业务日志
	 * @param vo -- 发货单vo
	 * @param action -- 动作名
	 */
	public void insertBusinesslog(nc.vo.pub.AggregatedValueObject vo,String action){
		Operlog operlog=new Operlog();
		SaleReceiveVO srvo = (SaleReceiveVO)vo;
		srvo.getOperatelogVO().setOpratorname(getOpratorname(srvo.getCl().getUser()));
		srvo.getOperatelogVO().setCompanyname(getCompanyname(srvo.getCl().getCorp()));
		srvo.getOperatelogVO().setOperatorId(srvo.getCl().getUser());
	    operlog.insertBusinessExceptionlog(vo, null, action, nc.vo.scm.funcs.Businesslog.MSGMESSAGE, SaleBillType.SaleReceive);
	}
	
	public String getCompanyname(String pk_corp){
		String ccorpname = null;
		try {
		FetchValueDMO fetchdmo = new FetchValueDMO();
		ccorpname = fetchdmo.fetchValue("bd_corp", "unitname",
					" pk_corp ='" + pk_corp + "' ");
		} catch (Exception e) {
			nc.vo.scm.pub.SCMEnv.out(nc.bs.ml.NCLangResOnserver.getInstance()
					.getStrByID("4008check", "UPP4008check-000069")/*
																	 * @res
																	 * "nc.bs.ic.pub.check.CheckDMO.insertBusinesslog插入业务日志出现错误！"
																	 */);
			SCMEnv.out(e);
		}
		return ccorpname;
	}
	
    public String getOpratorname(String cuserid){
    	String cusername = null;
		try {
		FetchValueDMO fetchdmo = new FetchValueDMO();
		cusername = fetchdmo.fetchValue("sm_user", "user_name",
				" cuserid ='" + cuserid + "' ");
		} catch (Exception e) {
			nc.vo.scm.pub.SCMEnv.out(nc.bs.ml.NCLangResOnserver.getInstance()
					.getStrByID("4008check", "UPP4008check-000069")/*
																	 * @res
																	 * "nc.bs.ic.pub.check.CheckDMO.insertBusinesslog插入业务日志出现错误！"
																	 */);
			SCMEnv.out(e);
		}
		return cusername;
	}
	
	/**
	 * 此处插入方法说明。 插入业务日志 by hanwei 创建日期：(2004-4-2 11:21:59)
	 * 
	 * @param voBill
	 *            nc.vo.ic.pub.bill.GeneralBillVO
	 * @param sMsg
	 *            java.lang.String ：错误信息 sEnterbutton：功能入口名称（新增保存，修改保存，审核，弃审）
	 */
	public void insertBusinesslog(nc.vo.pub.AggregatedValueObject vo,
			String sMessgageType, String sMsg, String sEnterbutton,
			String cuserid, String sdate) {

		if (vo == null || vo.getParentVO() == null)
			return;

		nc.vo.sm.log.OperatelogVO operatelogvo = getOperLogVO(sMsg,
				sEnterbutton, sMessgageType, cuserid, sdate, vo);

		insertBusinesslog(new nc.vo.sm.log.OperatelogVO[] { operatelogvo });

	}

	/**
	 * 此处插入方法说明。 插入业务日志 by hanwei 创建日期：(2004-4-2 11:21:59)
	 * 
	 * @param voBill
	 *            nc.vo.ic.pub.bill.GeneralBillVO
	 * @param sMsg
	 *            java.lang.String ：错误信息 sEnterbutton：功能入口名称（新增保存，修改保存，审核，弃审）
	 */
	public void insertBusinesslog(nc.vo.sm.log.OperatelogVO[] logvos) {

		try {

			if (logvos == null || logvos.length <= 0)
				return;

			IOperateLogService log = (IOperateLogService) NCLocator
					.getInstance().lookup(IOperateLogService.class.getName());
			log.insertArray(logvos);

		} catch (Exception e) {
			/** 辅助功能，不向外抛异常 */
			nc.vo.scm.pub.SCMEnv.out(nc.bs.ml.NCLangResOnserver.getInstance()
					.getStrByID("4008check", "UPP4008check-000069")/*
																	 * @res
																	 * "nc.bs.ic.pub.check.CheckDMO.insertBusinesslog插入业务日志出现错误！"
																	 */);
			SCMEnv.out(e);
		}

	}

	/**
	 * 此处插入方法说明。 插入业务日志 by hanwei 创建日期：(2004-4-2 11:21:59)
	 * 
	 * @param voBill
	 *            nc.vo.ic.pub.bill.GeneralBillVO
	 * @param sMsg
	 *            java.lang.String ：错误信息 sEnterbutton：功能入口名称（新增保存，修改保存，审核，弃审）
	 */
	public void insertBusinesslog(nc.vo.pub.AggregatedValueObject vo,
			String sMsg, String sEnterbutton, String cuserid, String sdate) {

		insertBusinesslog(vo, "WARNING", sMsg, sEnterbutton, cuserid, sdate);

	}

	/**
	 * 此处插入方法说明。 插入异常业务日志 by hanwei 创建日期：(2004-4-2 11:21:59)
	 * 
	 * @param voBill
	 *            nc.vo.ic.pub.bill.GeneralBillVO
	 * @param sMsg
	 *            java.lang.String
	 */
	public void insertBusinessExceptionlog(nc.vo.pub.AggregatedValueObject vo,
			String sMsg, String sEnterbutton, String cuserid, String sdate) {

		if (vo == null || vo.getParentVO() == null)
			return;

		nc.vo.sm.log.OperatelogVO operatelogvo = getOperLogVO(sMsg,
				sEnterbutton, "ERROR", cuserid, sdate, vo);

		insertBusinessExceptionlog(new nc.vo.sm.log.OperatelogVO[] { operatelogvo });

	}

	/**
	 * 此处插入方法说明。 插入异常业务日志 by hanwei 创建日期：(2004-4-2 11:21:59)
	 * 
	 * @param voBill
	 *            nc.vo.ic.pub.bill.GeneralBillVO
	 * @param sMsg
	 *            java.lang.String
	 */
	public void insertBusinessExceptionlog(nc.vo.sm.log.OperatelogVO[] logvos) {

		if (logvos == null || logvos.length <= 0)
			return;

		// OperatelogBO log = null;
		try {
			/** 辅助功能，不向外抛异常 */
			// nc.bs.sm.log.OperatelogHome lh = (nc.bs.sm.log.OperatelogHome)
			// getBeanHome(
			// nc.bs.sm.log.OperatelogHome.class,
			// "nc.bs.sm.log.OperatelogBO");
			IOperateLogService log = (IOperateLogService) NCLocator
					.getInstance().lookup(IOperateLogService.class.getName());

			log.insertArray(logvos);

		} catch (Exception e) {
			/** 辅助功能，不向外抛异常 */
			nc.vo.scm.pub.SCMEnv.out(nc.bs.ml.NCLangResOnserver.getInstance()
					.getStrByID("4008check", "UPP4008check-000011")/*
																	 * @res
																	 * "nc.bs.ic.pub.check.CheckBO.insertBusinessExceptionlog插入业务日志出现错误！"
																	 */);
			SCMEnv.out(e);
		} 

	}

	/**
	 * from v50 单据的最大折扣 比较各个表体的 整单折扣 *表体的单品折扣 返回最小值
	 * 
	 * @param billVO
	 * @return
	 */
	public UFDouble getMaxInvDiscountrate(AggregatedValueObject billVO) {
		UFDouble result = new UFDouble(100.0);
		if (billVO == null || billVO.getChildrenVO() == null
				|| billVO.getChildrenVO().length < 1
				|| !(billVO.getChildrenVO()[0] instanceof ISOInventoryDiscount))
			return result;
		UFDouble dDiscont = null;
		UFDouble dItemDiscont = null;
		UFDouble allDiscont = null;
		for (int i = 0; i < billVO.getChildrenVO().length; i++) {
			dDiscont = ((ISOInventoryDiscount) billVO.getChildrenVO()[i])
					.getDiscount();
			dItemDiscont = ((ISOInventoryDiscount) billVO.getChildrenVO()[i])
					.getItemDiscount();
			dDiscont = dDiscont == null ? new UFDouble(0.0) : dDiscont;
			dItemDiscont = dItemDiscont == null ? new UFDouble(0.0)
					: dItemDiscont;
			allDiscont = dDiscont.multiply(dItemDiscont).div(100.0);
			if (result.compareTo(allDiscont) > 0)
				result = allDiscont;
		}
		return result;
	}

	/**
	 * from v51 单据的最小折扣 比较各个表体的 整单折扣 *表体的单品折扣 返回最大值
	 * 
	 * @param billVO
	 * @return
	 */
	public UFDouble getMinInvDiscountrate(AggregatedValueObject billVO) {
		UFDouble result = new UFDouble(0.0);
		if (billVO == null || billVO.getChildrenVO() == null
				|| billVO.getChildrenVO().length < 1
				|| !(billVO.getChildrenVO()[0] instanceof ISOInventoryDiscount))
			return result;
		UFDouble dDiscont = null;
		UFDouble dItemDiscont = null;
		UFDouble allDiscont = null;
		for (int i = 0; i < billVO.getChildrenVO().length; i++) {
			dDiscont = ((ISOInventoryDiscount) billVO.getChildrenVO()[i])
					.getDiscount();
			dItemDiscont = ((ISOInventoryDiscount) billVO.getChildrenVO()[i])
					.getItemDiscount();
			dDiscont = dDiscont == null ? new UFDouble(0.0) : dDiscont;
			dItemDiscont = dItemDiscont == null ? new UFDouble(0.0)
					: dItemDiscont;
			allDiscont = dDiscont.multiply(dItemDiscont).div(100.0);
			if (result.compareTo(allDiscont) < 0)
				result = allDiscont;
		}
		return result;
	}

	/**
	 * from v50 销售价格不能小于存货最低售价 取得所有表体的存货 取得存货的最低售价
	 * 检查表体是否有小于存货最低售价，如果有返回true;否则false 20060613
	 * 在客户的管理档案中增加一个属性“存货最低售价比例”，为百分数，默认为100，如设置为：80，表示的意义为最低售价不能低于“存货管理档案的最低售价”*80%。
	 * ，用存货的含税净价与“存货管理档案的最低售价”*“存货最低售价比例”%去进行比较。
	 * 
	 * @param billVO
	 * @return
	 */
	public UFBoolean lessSaleMinPrice(AggregatedValueObject billVO) {
		UFBoolean result = new UFBoolean(true);
		if (billVO == null || billVO.getChildrenVO() == null
				|| billVO.getChildrenVO().length < 1
				|| !(billVO.getChildrenVO()[0] instanceof ISOInventoryDiscount))
			return result;
		// 取得所有表体的存货
		// 取得存货的最低售价
		// 检查表体是否有小于存货最低售价，如果有返回true;否则false.
		// 取得客户管理档案的最低售价比例
		SmartDMO dmo = null;
		UFDouble udLowRate = null;
		try {
			dmo = new SmartDMO();
			String smanSql = " select stockpriceratio from bd_cumandoc where pk_cumandoc='"
					+ billVO.getParentVO().getAttributeValue("ccustomerid")
					+ "' and dr=0";
			Object[] o = dmo.selectBy2(smanSql);
			if (o == null || o.length == 0)
				udLowRate = new UFDouble(1);
			else {
				Object[] o1 = (Object[]) o[0];
				if (o1 == null || o1.length == 0 || o1[0] == null)
					udLowRate = new UFDouble(1);
				else {
					udLowRate = new UFDouble(o1[0].toString());
					udLowRate = udLowRate.div(100);
				}
			}
		} catch (Exception e) {
			SCMEnv.out(e);
		}

		String[] invs = new String[billVO.getChildrenVO().length];
		HashMap map = new HashMap(billVO.getChildrenVO().length);
		for (int i = 0; i < billVO.getChildrenVO().length; i++) {

			invs[i] = ((ISOInventoryDiscount) billVO.getChildrenVO()[i])
					.getInventory();
			Object blargessflag = billVO.getChildrenVO()[i]
					.getAttributeValue("blargessflag");
			if (blargessflag != null
					&& new UFBoolean(blargessflag.toString()).booleanValue())
				continue;
			if (map.containsKey(invs[i])) {
				UFDouble price2 = ((ISOInventoryDiscount) billVO
						.getChildrenVO()[i]).getTaxNetPrice();
				price2 = price2 == null ? new UFDouble(0.0) : price2;
				UFDouble price = (UFDouble) map.get(invs[i]);
				price = price == null ? new UFDouble(0.0) : price;
				if (price2.compareTo(price) < 0) {
					map.put(invs[i], price2);
				}
			} else {
				UFDouble price2 = ((ISOInventoryDiscount) billVO
						.getChildrenVO()[i]).getTaxNetPrice();
				price2 = price2 == null ? new UFDouble(0.0) : price2;
				map.put(invs[i], price2);
			}
		}

		String sql = "select pk_invmandoc,lowestprice from bd_invmandoc where 1=1 "
				+ GeneralSqlString.formInSQL("pk_invmandoc", invs);
		String sHint;
		String ss="";
		
		try {
			Object[] o = dmo.selectBy2(sql);
			Object[] onerow = null;
			UFDouble minPrice = null;
			UFDouble price;
			if (o != null) {
				for (int i = 0; i < o.length; i++) {
					/** for each inventory* */
					onerow = (Object[]) o[i];
					if (onerow != null && onerow.length > 1) {
						minPrice = (UFDouble) map.get(onerow[0]);
						price = (new UFDouble(onerow[1].toString()))
								.multiply(udLowRate);
						/** compare price* */
						if (minPrice != null && onerow[1] != null
								&& minPrice.compareTo(price) < 0) {
							sHint = "";
							ss += nc.bs.ml.NCLangResOnserver.getInstance()
									.getStrByID("SCMCOMMON",
											"UPPSCMCommon-000289");
							for (int j = 0; j < billVO.getChildrenVO().length; j++) {
								// 存货相同，价格满足要求
								if ((((ISOInventoryDiscount) billVO
										.getChildrenVO()[j]).getInventory()
										.equals(onerow[0]))
										&& (((ISOInventoryDiscount) billVO
												.getChildrenVO()[j])
												.getTaxNetPrice()
												.compareTo(
														(new UFDouble(onerow[1]
																.toString()))
																.multiply(udLowRate)) < 0)) {
									sHint += billVO.getChildrenVO()[j]
											.getAttributeValue("crowno")
											+ " ";
								}// end if
							}// end for
							if (sHint.length() > 0) {
								ss += sHint + " 最低售价:" + price + "\n";
								// nc.bs.logging.Logger.setMappedThreadState(IPFConfigInfo.FUNCTION_OUT,ss);
							}
							// return new UFBoolean(false);
						}// end if price

					}
				}// end for each o
			}// end if o

			if (ss != null && ss.length() > 0) {
				nc.bs.logging.Logger.setMappedThreadState(
						IPFConfigInfo.FUNCTION_OUT, ss);
				return UFBoolean.FALSE;
			}

		} catch (Exception e) {
			SCMEnv.out(e);
		}

		return result;
	}

	/**
	 * from v50 销售无税价格不能小于存货最低售价 取得所有表体的存货 取得存货的最低售价
	 * 检查表体是否有小于存货最低售价，如果有返回true;否则false 20060613
	 * 在客户的管理档案中增加一个属性“存货最低售价比例”，为百分数，默认为100，如设置为：80，表示的意义为最低售价不能低于“存货管理档案的最低售价”*80%。
	 * ，用存货的含税净价与“存货管理档案的最低售价”*“存货最低售价比例”%去进行比较。
	 * 
	 * @param billVO
	 * @return
	 */
	public UFBoolean lessSaleMinPriceNew(AggregatedValueObject billVO) {
		UFBoolean result = new UFBoolean(true);
		if (billVO == null || billVO.getChildrenVO() == null
				|| billVO.getChildrenVO().length < 1
				|| !(billVO.getChildrenVO()[0] instanceof ISOInventoryDiscount))
			return result;
		// 取得所有表体的存货
		// 取得存货的最低售价
		// 检查表体是否有小于存货最低售价，如果有返回true;否则false.
		// 取得客户管理档案的最低售价比例
		SmartDMO dmo = null;
		UFDouble udLowRate = null;
		try {
			dmo = new SmartDMO();
			String smanSql = " select stockpriceratio from bd_cumandoc where pk_cumandoc='"
					+ billVO.getParentVO().getAttributeValue("ccustomerid")
					+ "' and dr=0";
			Object[] o = dmo.selectBy2(smanSql);
			if (o == null || o.length == 0)
				udLowRate = new UFDouble(1);
			else {
				Object[] o1 = (Object[]) o[0];
				if (o1 == null || o1.length == 0 || o1[0] == null)
					udLowRate = new UFDouble(1);
				else {
					udLowRate = new UFDouble(o1[0].toString());
					udLowRate = udLowRate.div(100);
				}
			}
		} catch (Exception e) {
			SCMEnv.out(e);
		}

		String[] invs = new String[billVO.getChildrenVO().length];
		HashMap map = new HashMap(billVO.getChildrenVO().length);
		for (int i = 0; i < billVO.getChildrenVO().length; i++) {

			invs[i] = ((ISOInventoryDiscount) billVO.getChildrenVO()[i])
					.getInventory();
			Object blargessflag = billVO.getChildrenVO()[i]
					.getAttributeValue("blargessflag");
			if (blargessflag != null
					&& new UFBoolean(blargessflag.toString()).booleanValue())
				continue;
			if (map.containsKey(invs[i])) {
				UFDouble price2 = ((ISOInventoryDiscount) billVO
						.getChildrenVO()[i]).getNnetprice();
				price2 = price2 == null ? new UFDouble(0.0) : price2;
				UFDouble price = (UFDouble) map.get(invs[i]);
				price = price == null ? new UFDouble(0.0) : price;
				if (price2.compareTo(price) < 0) {
					map.put(invs[i], price2);
				}
			} else {
				UFDouble price2 = ((ISOInventoryDiscount) billVO
						.getChildrenVO()[i]).getNnetprice();
				price2 = price2 == null ? new UFDouble(0.0) : price2;
				map.put(invs[i], price2);
			}
		}

		String sql = "select pk_invmandoc,lowestprice from bd_invmandoc where 1=1 "
				+ GeneralSqlString.formInSQL("pk_invmandoc", invs);
		String ss = "";
		try {
			Object[] o = dmo.selectBy2(sql);
			Object[] onerow = null;
			UFDouble minPrice = null;
			UFDouble price;
			if (o != null) {
				for (int i = 0; i < o.length; i++) {
					onerow = (Object[]) o[i];
					if (onerow != null && onerow.length > 1) {
						minPrice = (UFDouble) map.get(onerow[0]);
						price = (new UFDouble(onerow[1].toString()))
								.multiply(udLowRate);
						if (minPrice != null && onerow[1] != null
								&& minPrice.compareTo(price) < 0) {
							String sHint = "";
							ss += nc.bs.ml.NCLangResOnserver.getInstance()
									.getStrByID("SCMCOMMON",
											"UPPSCMCommon-000289");
							for (int j = 0; j < billVO.getChildrenVO().length; j++) {
								// 存货相同，价格满足要求
								if ((((ISOInventoryDiscount) billVO
										.getChildrenVO()[j]).getInventory()
										.equals(onerow[0]))
										&& (((ISOInventoryDiscount) billVO
												.getChildrenVO()[j])
												.getNnetprice()
												.compareTo(
														(new UFDouble(onerow[1]
																.toString()))
																.multiply(udLowRate)) < 0)) {

									sHint += billVO.getChildrenVO()[j]
											.getAttributeValue("crowno")
											+ " ";
								}// end if
							}// end for
							if (sHint.length() > 0) {
								ss += sHint + " 最低售价:" + price + "\n";
							}
						}
					}
				}
			}

			if (ss != null && ss.length() > 0) {
				nc.bs.logging.Logger.setMappedThreadState(
						IPFConfigInfo.FUNCTION_OUT, ss);
				return UFBoolean.FALSE;
			}

		} catch (Exception e) {
			SCMEnv.out(e);
		}

		return result;
	}

	//wanglei 2014-05-14
	//返回订单中行中，订单单价预分配费用金额/行金额最高值
	public UFDouble getMaxOrderFeeRate(AggregatedValueObject billVO) {
		UFDouble result = UFDouble.ZERO_DBL;
		if (billVO == null || billVO.getChildrenVO() == null
				|| billVO.getChildrenVO().length < 1
				|| !(billVO.getChildrenVO()[0] instanceof ISOInventoryDiscount))
			return result;

		SaleorderBVO[] bodyvos =  (SaleorderBVO[]) billVO.getChildrenVO();
		UFDouble ndistfeemny = UFDouble.ZERO_DBL;
		UFDouble nsummny = UFDouble.ZERO_DBL;
		for (SaleorderBVO bodyvo : bodyvos) {

			Object blargessflag = bodyvo.getAttributeValue("blargessflag");  //赠品行不检查
			if (blargessflag != null
					&& new UFBoolean(blargessflag.toString()).booleanValue())
				continue;
			
			ndistfeemny =  bodyvo.getAttributeValue("ndistfeemny") == null? UFDouble.ZERO_DBL : new UFDouble(bodyvo.getAttributeValue("ndistfeemny").toString());
			nsummny =  bodyvo.getNsummny() == null? UFDouble.ZERO_DBL : bodyvo.getNsummny();
			
			if (ndistfeemny.doubleValue() == 0.0 )  //没有分配的行业跳过
				continue;
			
			if ((ndistfeemny.div(nsummny)).doubleValue() > result.doubleValue()) {
				result = ndistfeemny.div(nsummny);
			} 

		}

		return result;
	}
}
