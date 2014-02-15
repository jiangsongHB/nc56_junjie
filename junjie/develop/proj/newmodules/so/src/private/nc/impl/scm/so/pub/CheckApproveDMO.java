package nc.impl.scm.so.pub;

/**
 * 单据状态函数
 * 创建日期：(2001-9-19 13:54:02)
 * @author：刘杰
 */
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Hashtable;

import nc.bs.framework.common.NCLocator;
import nc.itf.uif.pub.IUifService;
import nc.ui.scm.so.SaleBillType;
import nc.uif.pub.exception.UifException;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.VOStatus;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDouble;
import nc.vo.so.SaleInvoiceBVOForVerify;
import nc.vo.so.so001.SoVoConst;
import nc.vo.so.so002.SaleinvoiceBVO;
import nc.vo.so.so002.SaleinvoiceVO;

public class CheckApproveDMO extends nc.bs.pub.DataManageObject {
	Hashtable hNumber = null;

	/**
	 * CheckStatusDMO 构造子注解。
	 * 
	 * @exception javax.naming.NamingException
	 *                异常说明。
	 * @exception nc.bs.pub.SystemException
	 *                异常说明。
	 */
	public CheckApproveDMO() throws javax.naming.NamingException, nc.bs.pub.SystemException {
		super();
	}

	/**
	 * CheckStatusDMO 构造子注解。
	 * 
	 * @param dbName
	 *            java.lang.String
	 * @exception javax.naming.NamingException
	 *                异常说明。
	 * @exception nc.bs.pub.SystemException
	 *                异常说明。
	 */
	public CheckApproveDMO(String dbName) throws javax.naming.NamingException, nc.bs.pub.SystemException {
		super(dbName);
	}

	/**
	 * 检查是否已经核销。 创建日期：(2014-01-15)
	 * 
	 * @return nc.vo.pub.lang.UFBoolean
	 * @throws Exception 
	 */
	public UFBoolean isHasDeal(AggregatedValueObject billVO) throws Exception{
		UFBoolean bhasdeal = UFBoolean.FALSE;
		if (billVO instanceof SaleinvoiceVO) {
			SaleinvoiceVO vo = (SaleinvoiceVO) billVO;
			if (vo.getChildrenVO() == null ){
				return UFBoolean.TRUE;
			}
			
			SaleinvoiceBVO[] bvo =   vo.getChildrenVO();
			IUifService srv =(IUifService)NCLocator.getInstance().lookup(IUifService.class.getName());

			for (int i = 0; i < bvo.length; i++) {
				SaleInvoiceBVOForVerify[] dealvo = (SaleInvoiceBVOForVerify[])srv.queryByCondition(SaleInvoiceBVOForVerify.class, "cinvoice_bid = '" + bvo[i].getCinvoice_bid() + "'");
				if (!(dealvo[0].getNtotaldealmny() == null? UFDouble.ZERO_DBL: dealvo[0].getNtotaldealmny()  ).equals(UFDouble.ZERO_DBL) ||
						!(dealvo[0].getNtotaldealnum() == null? UFDouble.ZERO_DBL: dealvo[0].getNtotaldealnum()  ).equals(UFDouble.ZERO_DBL)	){
					return UFBoolean.TRUE;
				}
			}
		}
		return bhasdeal;
	}
	
	/**
	 * 获得原始数量。 创建日期：(2001-9-3 9:30:24)
	 * 
	 * @return nc.vo.pub.lang.UFDouble
	 */
	private Hashtable getOldData(String strBillType, String strID) throws SQLException {

		Hashtable hNumber = new Hashtable();

		if (strID == null)
			return hNumber;

		UFDouble nnumber = new UFDouble(0.0);

		String strSQL = "Select nnumber, ";
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
			strField = "cinvoice_bid";
		}

		strSQL = strSQL + strField + " from " + strTable + " Where csaleid =  ? and dr = 0";

		Connection con = null;
		PreparedStatement stmt = null;

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

				id = rs.getString(2);

				hNumber.put(id, nnumber);

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

		return hNumber;
	}

	/**
	 * 获得原始数量。 创建日期：(2001-9-3 9:30:24)
	 * 
	 * @return nc.vo.pub.lang.UFDouble
	 */
	private UFDouble getOldNumber(String strBillType, String strID) throws SQLException {
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

		/** v5.5发货单原有逻辑不适用 */
		// 发货单
		/*
		 * if (strBillType.equals(SaleBillType.SaleReceipt)){ strSQL = strSQL + "
		 * so_salereceipt_b "; strField = "creceipt_bid"; }
		 */

		// 出库单
		if (strBillType.equals(SaleBillType.SaleInitOutStore) || strBillType.equals(SaleBillType.SaleOutStore)) {
			strSQL = "select noutnum from ic_general_b ";
			strField = "cgeneralbid";
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
	 * 检查销售订单是否允许取消。 创建日期：(2001-9-18 11:36:22)
	 * 
	 * @return boolean
	 * @param 单据附表ID:ReceiptDetailID
	 *            java.lang.StringBuffer
	 * @param 单据主键ID:ReceiptID
	 *            java.lang.StringBuffer
	 */
	public void isInitOutAppRequst(AggregatedValueObject billVO) throws SQLException, nc.vo.pub.BusinessException {

		nc.vo.scm.pub.SCMEnv.out("执行isReceiptAppRequst");

		for (int i = 0; i < billVO.getChildrenVO().length; i++) {
			CircularlyAccessibleValueObject bodyVO = billVO.getChildrenVO()[i];
			// 销售发票关联订单主表ID，子表ID，开票数量
			if (bodyVO.getStatus() != VOStatus.DELETED)
				vinitOutAppOrder(bodyVO.getAttributeValue("csourcebillid").toString(), bodyVO.getAttributeValue(
						"csourcebillbodyid").toString(), bodyVO.getPrimaryKey(), (UFDouble) bodyVO
						.getAttributeValue("nnumber"), bodyVO.getStatus());

		}

	}

	/**
	 * 检查销售订单是否允许取消。 创建日期：(2001-9-18 11:36:22)
	 * 
	 * @return boolean
	 * @param 单据附表ID:ReceiptDetailID
	 *            java.lang.StringBuffer
	 * @param 单据主键ID:ReceiptID
	 *            java.lang.StringBuffer
	 */
	public void isInvoiceAppRequst(AggregatedValueObject billVO) throws SQLException, nc.vo.pub.BusinessException {

		nc.vo.scm.pub.SCMEnv.out("执行isInvoiceAppRequst");

		for (CircularlyAccessibleValueObject bodyVO:billVO.getChildrenVO()) {
			// 销售发票关联订单主表ID，子表ID，开票数量
			if (null != bodyVO.getAttributeValue("cupsourcebillid")
					&& null != bodyVO.getAttributeValue("cupreceipttype")
          && null != bodyVO.getAttributeValue("cupsourcebillbodyid")
          && null != bodyVO.getAttributeValue("nnumber"))
        
					if (VOStatus.DELETED != bodyVO.getStatus())
						vInvoiceAppOrder(bodyVO.getAttributeValue("cupreceipttype").toString(), bodyVO
								.getAttributeValue("cupsourcebillid").toString(), bodyVO.getAttributeValue(
								"cupsourcebillbodyid").toString(), billVO.getParentVO().getPrimaryKey(), bodyVO
								.getPrimaryKey(), (UFDouble) bodyVO.getAttributeValue("nnumber"), bodyVO.getStatus());

		}

	}

	/**
	 * 检查销售订单修订。 创建日期：(2001-9-18 11:36:22)
	 * 
	 * @return boolean
	 * @param 单据附表ID:ReceiptDetailID
	 *            java.lang.StringBuffer
	 * @param 单据主键ID:ReceiptID
	 *            java.lang.StringBuffer
	 */
	public void isOrderAlterRequst(AggregatedValueObject billVO) throws SQLException, nc.vo.pub.BusinessException {

		nc.vo.scm.pub.SCMEnv.out("执行isOrderAlterRequst");

		for (int i = 0; i < billVO.getChildrenVO().length; i++) {
			CircularlyAccessibleValueObject bodyVO = billVO.getChildrenVO()[i];
			// 销售出库单关联订单主表ID，子表ID，开票数量
			if (bodyVO.getAttributeValue("csaleid") != null && bodyVO.getAttributeValue("corder_bid") != null)
				vOrderAlterOrder(bodyVO.getAttributeValue("csaleid").toString(), bodyVO.getAttributeValue("corder_bid")
						.toString(), (UFDouble) bodyVO.getAttributeValue("nnumber"));

		}

	}

	/**
	 * 检查销售出库单。 创建日期：(2001-9-18 11:36:22)
	 * 
	 * @return boolean
	 * @param 单据附表ID:ReceiptDetailID
	 *            java.lang.StringBuffer
	 * @param 单据主键ID:ReceiptID
	 *            java.lang.StringBuffer
	 */
	public void isOutAppRequst(AggregatedValueObject billVO) throws SQLException, nc.vo.pub.BusinessException {

		nc.vo.scm.pub.SCMEnv.out("执行isReceiptAppRequst");
		if (billVO == null || billVO.getChildrenVO() == null || billVO.getChildrenVO().length <= 0)
			return;

		String[] corderbids = nc.vo.so.so016.SoVoTools.getVOsOnlyValues(billVO.getChildrenVO(), "cfirstbillbid");

		if (corderbids == null || corderbids.length <= 0)
			return;

		// 累计出库数量：已经出库数量 + 本次出库数量
		HashMap hsrow = nc.impl.scm.so.so016.SOToolsDMO
				.getAnyValueSORow("so_saleorder_b,so_saleexecute", new String[] { "isnull(ntotalinventorynumber,0)",
						"nnumber" }, "so_saleorder_b.corder_bid", corderbids,
						" so_saleorder_b.csaleid = so_saleexecute.csaleid and so_saleorder_b.corder_bid = so_saleexecute.csale_bid ");

		if (hsrow == null)
			return;

		HashMap hsoldnumber = nc.impl.scm.so.so016.SOToolsDMO.getAnyValueUFDouble("ic_general_b", "noutnum",
				"cgeneralbid", corderbids, null);

		UFDouble dblNumber = null;
		UFDouble dblNumber1 = null;

		UFDouble oldnnumber = null;

		UFDouble nReceiptNum = null;
		String cfirstbillbid = null;

		nc.vo.so.so001.SORowData row = null;

		UFDouble uf0 = new UFDouble(0);

		for (int i = 0; i < billVO.getChildrenVO().length; i++) {
			CircularlyAccessibleValueObject bodyVO = billVO.getChildrenVO()[i];
			if (bodyVO.getStatus() == VOStatus.DELETED)
				continue;

			cfirstbillbid = (String) bodyVO.getAttributeValue("cfirstbillbid");
			if (cfirstbillbid == null)
				continue;
			nReceiptNum = (UFDouble) bodyVO.getAttributeValue("noutnum");
			if (nReceiptNum == null)
				continue;

			row = (nc.vo.so.so001.SORowData) hsrow.get(cfirstbillbid);
			if (row == null)
				continue;

			oldnnumber = null;
			if (hsoldnumber != null) {
				oldnnumber = (UFDouble) hsoldnumber.get("cfirstbillbid");
			}

			if (oldnnumber == null)
				oldnnumber = uf0;

			dblNumber = row.getUFDouble(0);
			dblNumber1 = row.getUFDouble(1);

			// 累计出库数量：已经出库数量 + 本次出库数量
			if (dblNumber == null)
				dblNumber = uf0;

			dblNumber = dblNumber.sub(oldnnumber).add(nReceiptNum);

			if (dblNumber1.floatValue() > 0) {

				if (dblNumber.floatValue() < 0)
					throw new nc.vo.pub.BusinessException(nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("sopub",
							"UPPsopub-000000")/* @res "累计出库数量不能小于零。" */);

			} else {

				if (dblNumber.floatValue() > 0)
					throw new nc.vo.pub.BusinessException(nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("sopub",
							"UPPsopub-000001")/* @res "累计出库数量不能大于零。" */);

			}

			// 销售出库单关联订单主表ID，子表ID，开票数量
			// if(bodyVO.getAttributeValue("cfirstbillhid") != null &&
			// bodyVO.getAttributeValue("cfirstbillbid") != null)
			// if(bodyVO.getStatus() != VOStatus.DELETED)
			// vOutAppOrder(bodyVO.getAttributeValue("cfirstbillhid").toString(),bodyVO.getAttributeValue("cfirstbillbid").toString(),bodyVO.getPrimaryKey(),(UFDouble)bodyVO.getAttributeValue("noutnum"),bodyVO.getStatus());

		}

	}

	/**
	 * 检查销售订单是否允许取消。 创建日期：(2001-9-18 11:36:22)
	 * 
	 * @return boolean
	 * @param 单据附表ID:ReceiptDetailID
	 *            java.lang.StringBuffer
	 * @param 单据主键ID:ReceiptID
	 *            java.lang.StringBuffer
	 */
	public void isReceiptAppRequst(AggregatedValueObject billVO) throws SQLException, nc.vo.pub.BusinessException {

		nc.vo.scm.pub.SCMEnv.out("执行isReceiptAppRequst");

		for (int i = 0; i < billVO.getChildrenVO().length; i++) {
			CircularlyAccessibleValueObject bodyVO = billVO.getChildrenVO()[i];
			// 销售发票关联订单主表ID，子表ID，开票数量

			if (bodyVO.getStatus() != VOStatus.DELETED)
				vReceiptAppOrder(bodyVO.getAttributeValue("csourcebillid").toString(), bodyVO.getAttributeValue(
						"csourcebillbodyid").toString(), bodyVO.getPrimaryKey(), (UFDouble) bodyVO
						.getAttributeValue("nnumber"), bodyVO.getStatus());

		}

	}

	/**
	 * 检查期初出库情况函数。 创建日期：(2001-9-18 11:36:22)
	 * 
	 * @return boolean
	 * @param 相关订单附表ID:ReceiptDetailID
	 * @param 相关订单主键ID:ReceiptID
	 * @param 发货数量:nReceiptNum
	 */
	private void vinitOutAppOrder(String SaleID, String SaleDetailID, String ReceiptDetailID, UFDouble nReceiptNum,
			int state) throws SQLException, nc.vo.pub.BusinessException {

		nc.vo.scm.pub.SCMEnv.out("执行vinitOutAppOrder");

		// 原数量
		UFDouble oldnnumber = new UFDouble(0.0);
		if (state == VOStatus.UPDATED) {
			oldnnumber = getOldNumber(nc.ui.scm.so.SaleBillType.SaleInitOutStore, ReceiptDetailID);
			if (oldnnumber == null)
				oldnnumber = new UFDouble(0.0);
		}

		String SQLRowStatus = "select isnull(ntotalinventorynumber,0)-(nnumber)+(" + nReceiptNum + ")";
		SQLRowStatus = SQLRowStatus
				+ " nReceiptNum  from  so_saleorder_b left outer join so_saleexecute on so_saleorder_b.csaleid = so_saleexecute.csaleid ";
		SQLRowStatus = SQLRowStatus + " and so_saleorder_b.corder_bid = so_saleexecute.csale_bid ";

		// 条件
		SQLRowStatus = SQLRowStatus + " where so_saleorder_b.csaleid ='" + SaleID + "' ";
		SQLRowStatus = SQLRowStatus + " and so_saleorder_b.corder_bid ='" + SaleDetailID + "' ";

		// nReceiptNum 别名：已经发货数量 - 订单数量 + 本次发货数量

		Connection con = null;
		PreparedStatement stmt = null;

		String bResultMsg = null;

		BigDecimal dblNumber = new BigDecimal(0);

		try {
			con = getConnection();
			stmt = con.prepareStatement(SQLRowStatus);

			ResultSet rstNumber = stmt.executeQuery();
			// 判断是否为空纪录
			if (rstNumber.next()) {
				// 判断是否为空
				// 判断发货数量
				// 取发货数量
				Object o = rstNumber.getObject("nReceiptNum");

				if (o != null)
					dblNumber = new BigDecimal(o.toString());
				if (dblNumber.floatValue() > 0)

					bResultMsg = nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("sopub", "UPPsopub-000002")/*
																												 * @res
																												 * "出库数量不能大于订购数量。"
																												 */;

			}

		}

		finally {
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

		if (bResultMsg != null) {
			nc.vo.pub.BusinessException e = new nc.vo.pub.BusinessException(bResultMsg);
			throw e;
		}

	}

	/**
	 * 检查订单开票情况函数。 创建日期：(2001-9-18 11:36:22)
	 * 
	 * @return boolean
	 * @param 相关订单附表ID:ReceiptDetailID
	 * @param 相关订单主键ID:ReceiptID
	 * @param 开票数量:nInvoiceNum
	 */
	private void vInvoiceAppOrder(String billType, String SaleID, String SaleDetailID, String ReceiptID,
			String ReceiptDetailID, UFDouble nInvoiceNum, int state) throws SQLException, nc.vo.pub.BusinessException {

		nc.vo.scm.pub.SCMEnv.out("执行vInvoiceAppOrder");

		// 原数量
		UFDouble oldnnumber = new UFDouble(0.0);

		if (hNumber == null)
			hNumber = getOldData(nc.ui.scm.so.SaleBillType.SaleInvoice, ReceiptID);

		if (state == VOStatus.UPDATED) {
			// oldnnumber =
			// getOldNumber(nc.ui.scm.so.SaleBillType.SaleInvoice,ReceiptDetailID);
			//2009-12-16 fengjb zhangcheng fangchan 销售发票推式保存的时候vo状态是错的，但是这个时候
			//ReceiptDetailID为空，为避免空指异常判断下ReceiptDetailID即可
			if (!StringUtil.isEmptyWithTrim(ReceiptDetailID) && hNumber.containsKey(ReceiptDetailID))
				oldnnumber = (UFDouble) hNumber.get(ReceiptDetailID);
			else
				oldnnumber = new UFDouble(0.0);
		}

		String SQLRowStatus = null;
		String errMSG = null;
		String bResultMsg = null;
		nc.vo.scm.pub.SCMEnv.out("#######################: " + billType);
		if (billType.equals(nc.ui.scm.so.SaleBillType.SaleOrder)) {

			SQLRowStatus = "select isnull(ntotalinvoicenumber,0)-nnumber-(" + oldnnumber + ")+(" + nInvoiceNum + ")";
			SQLRowStatus = SQLRowStatus
					+ " nInvoiceNum,nnumber,isnull(ntotalinvoicenumber,0)-("
					+ oldnnumber
					+ ") ntotalinvoicenumber  from  so_saleorder_b left outer  join so_saleexecute on so_saleorder_b.csaleid = so_saleexecute.csaleid ";
			SQLRowStatus = SQLRowStatus + " and so_saleorder_b.corder_bid = so_saleexecute.csale_bid ";

			// 条件
			SQLRowStatus = SQLRowStatus + " where so_saleorder_b.csaleid ='" + SaleID + "' ";
			SQLRowStatus = SQLRowStatus + " and so_saleorder_b.corder_bid ='" + SaleDetailID + "' ";
			SQLRowStatus = SQLRowStatus + " and so_saleexecute.csaleid ='" + SaleID + "' ";
			SQLRowStatus = SQLRowStatus + " and so_saleexecute.csale_bid ='" + SaleDetailID + "' ";

			errMSG = nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("sopub", "UPPsopub-000003")/*
																									 * @res
																									 * "开票数量不能大于订购数量。"
																									 */;
		} else {

			// 销售出库单
			SQLRowStatus = "select isnull(ic_general_bb3.nsignnum,0)-ic_general_b.noutnum -(" + oldnnumber + ")+("
					+ nInvoiceNum + ")";
			SQLRowStatus = SQLRowStatus
					+ " nInvoiceNum ,ic_general_b.noutnum as nnumber,isnull(ic_general_bb3.nsignnum,0)-("
					+ oldnnumber
					+ ") ntotalinvoicenumber FROM ic_general_h INNER JOIN    ic_general_b ON ic_general_h.cgeneralhid = ic_general_b.cgeneralhid INNER JOIN ";
			SQLRowStatus = SQLRowStatus
					+ " ic_general_bb3 ON ic_general_h.cgeneralhid = ic_general_bb3.cgeneralhid AND   ic_general_b.cgeneralbid = ic_general_bb3.cgeneralbid  ";

			// 条件
			SQLRowStatus = SQLRowStatus + " where ic_general_b.cgeneralhid ='" + SaleID + "' ";
			SQLRowStatus = SQLRowStatus + " and ic_general_b.cgeneralbid ='" + SaleDetailID + "' ";
			// 条件
			SQLRowStatus = SQLRowStatus + " and ic_general_bb3.cgeneralhid ='" + SaleID + "' ";
			SQLRowStatus = SQLRowStatus + " and ic_general_bb3.cgeneralbid ='" + SaleDetailID + "' ";

			errMSG = nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("sopub", "UPPsopub-000004")/*
																									 * @res
																									 * "开票数量不能大于实际出库数量。"
																									 */;
		}
		// nInvoiceNum 别名：已经开票数量 - 订单数量 + 本次开票数量

		Connection con = null;
		PreparedStatement stmt = null;

		BigDecimal dblNumber = new BigDecimal(0);
		BigDecimal dblNumber1 = new BigDecimal(0);
		BigDecimal dblNumber2 = new BigDecimal(0);

		try {
			con = getConnection();
			stmt = con.prepareStatement(SQLRowStatus);

			ResultSet rstNumber = stmt.executeQuery();
			// 判断是否为空纪录
			if (rstNumber.next()) {
				// 判断是否为空
				// 判断开票数量
				// 取开票数量
				Object o = rstNumber.getObject("nInvoiceNum");
				Object o1 = rstNumber.getObject("nnumber");
				Object o2 = rstNumber.getObject("ntotalinvoicenumber");

				if (o != null)
					dblNumber = new BigDecimal(o.toString());
				dblNumber1 = new BigDecimal(o1 == null ? "0.0" : o1.toString());
				dblNumber2 = new BigDecimal(o2 == null ? "0.0" : o2.toString());

				if (dblNumber1.floatValue() > 0) {
					if (nInvoiceNum.doubleValue() < 0) {
						if (nInvoiceNum.abs().doubleValue() > dblNumber2.doubleValue())
							bResultMsg = nc.bs.ml.NCLangResOnserver.getInstance()
									.getStrByID("sopub", "UPPsopub-000005")/*
																			 * @res
																			 * "负数量不能大于已开票数量。"
																			 */;
					} else {
						if (dblNumber.floatValue() > 0) {
							bResultMsg = errMSG;
						}
					}
				} else {
					if (dblNumber.floatValue() < 0 || nInvoiceNum.doubleValue() > 0)
						bResultMsg = errMSG;
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

		if (bResultMsg != null) {
			nc.vo.pub.BusinessException e = new nc.vo.pub.BusinessException(bResultMsg);
			throw e;
		}

	}

	/**
	 * 检查订单修订情况函数。 创建日期：(2001-9-18 11:36:22)
	 * 
	 * @return boolean
	 * @param 相关订单附表ID:ReceiptDetailID
	 * @param 相关订单主键ID:ReceiptID
	 * @param 开票数量:nInvoiceNum
	 */
	private void vOrderAlterOrder(String SaleID, String SaleDetailID, UFDouble nInvoiceNum) throws SQLException,
			nc.vo.pub.BusinessException {

		nc.vo.scm.pub.SCMEnv.out("执行vOrderAlterOrder");

		String SQLRowStatus = null;
		String errMSG = null;
		String bResultMsg = null;

		SQLRowStatus = "select nnumber,isnull(ntotalinvoicenumber,0) ntotalinvoicenumber,isnull(ntotalreceivenumber,0) ntotalreceivenumber,isnull(ntotalinventorynumber,0) ntotalinventorynumber ";
		SQLRowStatus = SQLRowStatus
				+ " from  so_saleorder_b left outer  join so_saleexecute on so_saleorder_b.csaleid = so_saleexecute.csaleid ";
		SQLRowStatus = SQLRowStatus + " and so_saleorder_b.corder_bid = so_saleexecute.csale_bid ";

		// 条件
		SQLRowStatus = SQLRowStatus + " where so_saleorder_b.csaleid ='" + SaleID + "' ";
		SQLRowStatus = SQLRowStatus + " and so_saleorder_b.corder_bid ='" + SaleDetailID + "' ";

		errMSG = nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("sopub", "UPPsopub-000006")/*
																								 * @res
																								 * "修订数量不能小于已发生数量。"
																								 */;

		Connection con = null;
		PreparedStatement stmt = null;

		BigDecimal dblNumber = new BigDecimal(0);
		BigDecimal dblNumber1 = new BigDecimal(0);
		BigDecimal dblNumber2 = new BigDecimal(0);
		BigDecimal dblNumber3 = new BigDecimal(0);

		nInvoiceNum = nInvoiceNum == null ? SoVoConst.duf0 : nInvoiceNum;

		try {
			con = getConnection();
			stmt = con.prepareStatement(SQLRowStatus);

			ResultSet rstNumber = stmt.executeQuery();
			// 判断是否为空纪录
			if (rstNumber.next()) {
				// 判断是否为空
				// 判断开票数量
				// 取开票数量
				Object o = rstNumber.getObject("nnumber");
				Object o1 = rstNumber.getObject("ntotalinvoicenumber");
				Object o2 = rstNumber.getObject("ntotalreceivenumber");
				Object o3 = rstNumber.getObject("ntotalinventorynumber");

				if (o != null)
					dblNumber = new BigDecimal(o.toString());
				dblNumber1 = new BigDecimal(o1 == null ? "0.0" : o1.toString());
				dblNumber2 = new BigDecimal(o2 == null ? "0.0" : o2.toString());
				dblNumber3 = new BigDecimal(o3 == null ? "0.0" : o3.toString());

				if (dblNumber.floatValue() > 0) {
					if (nInvoiceNum.doubleValue() < 0) {
						bResultMsg = nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("sopub", "UPPsopub-000007")/*
																													 * @res
																													 * "修订数量不能与原数量符号相反。"
																													 */;
					} else {
						if (nInvoiceNum.doubleValue() < dblNumber1.doubleValue()
								|| nInvoiceNum.doubleValue() < dblNumber2.doubleValue()
								|| nInvoiceNum.doubleValue() < dblNumber3.doubleValue()) {
							bResultMsg = errMSG;
						}
					}
				} else {
					if (nInvoiceNum.doubleValue() > 0) {
						bResultMsg = nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("sopub", "UPPsopub-000007")/*
																													 * @res
																													 * "修订数量不能与原数量符号相反。"
																													 */;
					} else {
						if (nInvoiceNum.doubleValue() > dblNumber1.doubleValue()
								|| nInvoiceNum.doubleValue() > dblNumber2.doubleValue()
								|| nInvoiceNum.doubleValue() > dblNumber3.doubleValue()) {
							bResultMsg = errMSG;
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

		if (bResultMsg != null) {
			nc.vo.pub.BusinessException e = new nc.vo.pub.BusinessException(bResultMsg);
			throw e;
		}

	}

	/**
	 * 检查订单发货情况函数。 创建日期：(2001-9-18 11:36:22)
	 * 
	 * @return boolean
	 * @param 相关订单附表ID:ReceiptDetailID
	 * @param 相关订单主键ID:ReceiptID
	 * @param 发货数量:nReceiptNum
	 */
	private void vReceiptAppOrder(String SaleID, String SaleDetailID, String ReceiptDetailID, UFDouble nReceiptNum,
			int state) throws SQLException, nc.vo.pub.BusinessException {

		nc.vo.scm.pub.SCMEnv.out("执行vReceiptAppOrder");

		// 原数量
		UFDouble oldnnumber = new UFDouble(0.0);
		/** v5.5发货单原有逻辑不适用 */
		/*
		 * if(state == VOStatus.UPDATED){ oldnnumber =
		 * getOldNumber(nc.ui.scm.so.SaleBillType.SaleReceipt,ReceiptDetailID);
		 * if (oldnnumber == null) oldnnumber = new UFDouble(0.0); }
		 */

		String SQLRowStatus = "select isnull(ntotalreceivenumber,0)-nnumber-(" + oldnnumber + ")+(" + nReceiptNum + ")";
		SQLRowStatus = SQLRowStatus
				+ " nReceiptNum,nnumber,isnull(ntotalreceivenumber,0)-("
				+ oldnnumber
				+ ") ntotalreceivenumber from  so_saleorder_b left outer  join so_saleexecute on so_saleorder_b.csaleid = so_saleexecute.csaleid ";
		SQLRowStatus = SQLRowStatus + " and so_saleorder_b.corder_bid = so_saleexecute.csale_bid ";

		// 条件
		SQLRowStatus = SQLRowStatus + " where so_saleorder_b.csaleid ='" + SaleID + "' ";
		SQLRowStatus = SQLRowStatus + " and so_saleorder_b.corder_bid ='" + SaleDetailID + "' ";

		// nReceiptNum 别名：已经发货数量 - 订单数量 + 本次发货数量

		Connection con = null;
		PreparedStatement stmt = null;

		String bResultMsg = null;

		BigDecimal dblNumber = new BigDecimal(0);
		BigDecimal dblNumber1 = new BigDecimal(0);
		BigDecimal dblNumber2 = new BigDecimal(0);

		try {
			con = getConnection();
			stmt = con.prepareStatement(SQLRowStatus);

			ResultSet rstNumber = stmt.executeQuery();
			// 判断是否为空纪录
			if (rstNumber.next()) {
				// 判断是否为空
				// 判断发货数量
				// 取发货数量

				Object o = rstNumber.getObject("nReceiptNum");
				Object o1 = rstNumber.getObject("nnumber");
				Object o2 = rstNumber.getObject("ntotalreceivenumber");

				if (o != null)
					dblNumber = new BigDecimal(o.toString());

				if (o1 != null)
					dblNumber1 = new BigDecimal(o1.toString());

				if (o2 != null)
					dblNumber2 = new BigDecimal(o2.toString());

				if (dblNumber1.floatValue() > 0) {
					if (nReceiptNum.doubleValue() < 0) {
						if (nReceiptNum.abs().doubleValue() > dblNumber2.doubleValue())
							bResultMsg = nc.bs.ml.NCLangResOnserver.getInstance()
									.getStrByID("sopub", "UPPsopub-000008")/*@res "负数量不能大于已发货数量。"*/;
					} else {
						if (dblNumber.floatValue() > 0)
							bResultMsg = nc.bs.ml.NCLangResOnserver.getInstance()
									.getStrByID("sopub", "UPPsopub-000009")/*@res "发货数量不能大于订购数量。"*/;
					}
				} else

				if (dblNumber.floatValue() < 0 || nReceiptNum.doubleValue() > 0)

					bResultMsg = nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("sopub", "UPPsopub-000009")/*@res "发货数量不能大于订购数量。"*/;

			}

		}

		finally {
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

		if (bResultMsg != null) {
			nc.vo.pub.BusinessException e = new nc.vo.pub.BusinessException(bResultMsg);
			throw e;
		}

	}
}