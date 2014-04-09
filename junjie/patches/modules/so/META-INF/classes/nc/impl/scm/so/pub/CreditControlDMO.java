package nc.impl.scm.so.pub;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;

import nc.bs.framework.common.NCLocator;
import nc.bs.ml.NCLangResOnserver;
import nc.bs.scm.pub.TempTableDMO;
import nc.bs.scm.pub.smart.SmartDMO;
import nc.itf.scm.so.pub.ICreditControl;
import nc.itf.scm.so.receive.IReceiveService;
import nc.itf.scm.sp.pub.IPriceFuncQuery;
import nc.itf.so.so120.IBillInvokeCreditManager;
import nc.itf.so.so120.ICreditMny;
import nc.itf.uap.sf.ICreateCorpQueryService;
import nc.ui.scm.so.SaleBillType;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.VOStatus;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.pf.PfUtilActionConstrictVO;
import nc.vo.scm.pub.SCMEnv;
import nc.vo.scm.pub.bill.IExamAVO;
import nc.vo.so.credit.AccountMnyVO;
import nc.vo.so.so001.SaleOrderVO;
import nc.vo.so.so001.SaleorderBVO;
import nc.vo.so.so001.SoVoConst;
import nc.vo.so.so002.SaleinvoiceVO;
import nc.vo.so.so016.SoVoTools;
import nc.vo.so.soreceive.SaleReceiveBVO;
import nc.vo.so.soreceive.SaleReceiveHVO;
import nc.vo.so.soreceive.SaleReceiveVO;

/**
 * 销售信用函数。 
 * 
 * 创建日期：(2001-6-21 13:57:52)
 *
 * @author：宋杰
 */
public class CreditControlDMO extends nc.bs.pub.DataManageObject implements ICreditControl {
	
	public CreditControlDMO() throws javax.naming.NamingException,
			nc.bs.pub.SystemException {

	}

	public CreditControlDMO(String dbName) throws javax.naming.NamingException,
			nc.bs.pub.SystemException {
		super(dbName);
	}
	
	/* (non-Javadoc)
	 * @see nc.impl.scm.so.pub.ICreditControl#getARsPrepayNotCheck(int, java.lang.String[])
	 */
	public Hashtable getARsPrepayNotCheck(int controlFlag, String[] ids)
			throws java.sql.SQLException {
		StringBuffer sqlCustManIDs = new StringBuffer();
		for (int i = 0; i < ids.length; i++) {
			if (i != 0)
				sqlCustManIDs.append(",");
			sqlCustManIDs.append("'");
			sqlCustManIDs.append(ids[i]);
			sqlCustManIDs.append("'");
		}
		String sconfield = null;
		switch (controlFlag) {
		case 0:
			sconfield = "arap_djfb.deptid";
			break;
		case 1:
			sconfield = "arap_djfb.ywybm";
			break;
		case 2:
			sconfield = "arap_djfb.chbm_cl";
			break;
		case 3:
			sconfield = "arap_djfb.ksbm_cl";
			break;
		default:
			throw new java.sql.SQLException(nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("sopub","UPPsopub-000070")/*@res "不支持的参数"*/);
		}
		;
		String sql = "select "
				+ sconfield
				+ ",sum(bbye) from arap_djzb left outer join arap_djfb on  arap_djfb.vouchid = arap_djzb.vouchid where "
				+ sconfield
				+ " in ("
				+ sqlCustManIDs.toString()
				+ ")  and arap_djzb.prepay = 'Y' and arap_djzb.djdl = 'sk' and arap_djzb.dr=0 and arap_djfb.dr=0 and arap_djzb.djzt>=1 group by arap_djfb.ksbm_cl";
		java.sql.Connection con = null;
		java.sql.PreparedStatement stmt = null;
		Hashtable ret = new Hashtable();
		try {
			con = getConnection();
			stmt = con.prepareStatement(sql);
			java.sql.ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				ret.put(rs.getString(1), new UFDouble(rs.getDouble(2)));
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

		return ret;
	}

	/**
	 * 获得客户默认业务员操作。 创建日期：(2001-9-3 9:30:24)
	 *
	 * @return nc.vo.pub.lang.UFDouble
	 */
	private String getCuIDTOPsnID(String strID) throws SQLException {
		String strPsnDocID = null;

		if (strID == null || strID.equals(""))
			return strPsnDocID;

		String strSQL = "Select pk_resppsn1 From bd_cumandoc where pk_cumandoc = ?";

		Connection con = null;
		PreparedStatement stmt = null;

		try {
			con = getConnection();
			stmt = con.prepareStatement(strSQL);

			//pk
			stmt.setString(1, strID);

			ResultSet rs = stmt.executeQuery();
			//
			if (rs.next()) {
				strPsnDocID = rs.getString(1);
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

		return strPsnDocID;
	}

	/* (non-Javadoc)
	 * @see nc.impl.scm.so.pub.ICreditControl#getCustCredit(AggregatedValueObject)
	 */
	public UFDouble getCustCredit(AggregatedValueObject vo)
			throws Exception {
				
		String strCustomerID = (String) vo.getParentVO().getAttributeValue(
				"ccustomerid");
		String beanname=IPriceFuncQuery.class.getName();
		IPriceFuncQuery dmo=(IPriceFuncQuery)NCLocator.getInstance().lookup(beanname);
		UFDouble custcredit = dmo.getCustCredit(strCustomerID);

		if (custcredit == null)
			custcredit = new UFDouble(0);
		return custcredit;
	}

	/**
	 * 客户信用保证金。 创建日期：(2001-6-21 14:00:35)
	 *
	 * @return nc.vo.pub.lang.UFDouble
	 * @param strCustomerID
	 *            java.lang.String
	 */
	private UFDouble getCustCreditMoney(String strCustomerID)
			throws SQLException {
		/** ********************************************************** */
		// 保留的系统管理接口：
		beforeCallMethod("nc.bs.so.pub.CreditControlDMO", "getCustCreditMoney",
				new Object[] { strCustomerID });
		/** ********************************************************** */

		String sql = "SELECT creditmoney FROM bd_cumandoc WHERE pk_cumandoc = ? ";

		Connection con = null;
		PreparedStatement stmt = null;

		UFDouble result = new UFDouble(0);
		try {
			con = getConnection();
			stmt = con.prepareStatement(sql);
			//ccustomerid
			stmt.setString(1, strCustomerID);

			ResultSet rs = stmt.executeQuery();
			//
			if (rs.next()) {
				BigDecimal n = rs.getBigDecimal(1);
				result = (n == null ? new UFDouble(0) : new UFDouble(n));
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
		afterCallMethod("nc.bs.so.pub.CreditControlDMO", "getCustCreditMoney",
				new Object[] { strCustomerID });
		/** ********************************************************** */

		return result;
	}

	/* (non-Javadoc)
	 * @see nc.impl.scm.so.pub.ICreditControl#getCustCreditMoney(AggregatedValueObject)
	 */
	public UFDouble getCustCreditMoney(AggregatedValueObject vo)
			throws SQLException {
		String strCustomerID = (String) vo.getParentVO().getAttributeValue(
				"ccustomerid");
		return getCustCreditMoney(strCustomerID);
	}

	/* (non-Javadoc)
	 * @see nc.impl.scm.so.pub.ICreditControl#getCustDiscountrate(java.lang.String)
	 */
	public UFDouble getCustDiscountrate(String strCustomerID)
			throws SQLException {
		/** ********************************************************** */
		// 保留的系统管理接口：
		beforeCallMethod("nc.bs.so.pub.CreditControlDMO",
				"getCustDiscountrate", new Object[] { strCustomerID });
		/** ********************************************************** */

		String sql = "SELECT discountrate FROM bd_cumandoc WHERE pk_cumandoc = ?";

		Connection con = null;
		PreparedStatement stmt = null;

		UFDouble result = new UFDouble(0);
		try {
			con = getConnection();
			stmt = con.prepareStatement(sql);
			stmt.setString(1, strCustomerID);
			ResultSet rs = stmt.executeQuery();
			//
			if (rs.next()) {
				BigDecimal n = rs.getBigDecimal(1);
				result = (n == null ? new UFDouble(0) : new UFDouble(n));
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
		afterCallMethod("nc.bs.so.pub.getCustDiscountrate",
				"getCustDiscountrate", new Object[] { strCustomerID });
		/** ********************************************************** */

		return result;
	}

	/* (non-Javadoc)
	 * @see nc.impl.scm.so.pub.ICreditControl#getCustDiscountrate(AggregatedValueObject)
	 */
	public UFDouble getCustDiscountrate(AggregatedValueObject vo)
			throws SQLException {
		String strCustomerID = (String) vo.getParentVO().getAttributeValue(
				"ccustomerid");
		return getCustDiscountrate(strCustomerID);
	}

	/* (non-Javadoc)
	 * @see nc.impl.scm.so.pub.ICreditControl#getCustLevel(AggregatedValueObject)
	 */
	public String getCustLevel(AggregatedValueObject vo)
			throws Exception {
		String beanname = IPriceFuncQuery.class.getName();
		IPriceFuncQuery dmo = (IPriceFuncQuery) NCLocator.getInstance().lookup(
				beanname);
		String strCustomerID = (String) vo.getParentVO().getAttributeValue(
				"ccustomerid");
		String sql = "select bd_defdoc.docname from bd_cumandoc,bd_defdoc where bd_cumandoc.creditlevel=bd_defdoc.pk_defdoc and pk_cumandoc = ?";
		// UFDate currdate = new UFDate(System.currentTimeMillis());
		String level = null;
		Connection con = null;
		PreparedStatement stmt = null;
		try {
			con = getConnection();
			stmt = con.prepareStatement(sql);
			stmt.setString(1, strCustomerID);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				Object tmp = rs.getObject(1);
				level = (tmp == null ? null : tmp.toString().trim());
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
		return level;
}

	/* (non-Javadoc)
	 * @see nc.impl.scm.so.pub.ICreditControl#getCustOrderAR(AggregatedValueObject)
	 */
	public UFDouble getCustOrderAR(AggregatedValueObject vo)
			throws SQLException {
		String strCustomerID = (String) vo.getParentVO().getAttributeValue(
				"ccustomerid");
//		String strCorpID = (String) vo.getParentVO().getAttributeValue(
//				"pk_corp");

		//return
		// getCustOrderAR(strCustomerID,strCorpID).add(getCustFinanceUserAR(vo)).sub(getCustFinanceBalance(vo));

		//yb modify 2003-10-13 统一应收，统一调用祝奇提供的接口
		//Hashtable ret = getARsOfOrder(new String[] { strCustomerID });
		//if (ret != null)
		//return (UFDouble) ret.get(strCustomerID);
		//else
		//return null;

		//yb modify 2004-05-24 统一调用信用提供的接口
		try {
			//nc.bs.so.so120.CreditMny creditmny = new nc.bs.so.so120.CreditMny();
			ICreditMny creditmny = (ICreditMny)nc.bs.framework.common.NCLocator.getInstance().lookup(ICreditMny.class.getName());;			
			AccountMnyVO accountvo = new AccountMnyVO();
			accountvo.setAttributeValue("pk_cumandoc", strCustomerID);
			UFDouble[] retmnys = creditmny.getAccountMny(accountvo);
			if (retmnys != null)
				return retmnys[0];
			else
				return null;
		} catch (Exception e) {
			nc.vo.scm.pub.SCMEnv.out(e.getMessage());
			/**
			 * 未获得客户应收金额
			 */
			return null;
		}

	}

	/* (non-Javadoc)
	 * @see nc.impl.scm.so.pub.ICreditControl#getCustOrderAR2(java.lang.String)
	 */
	public UFDouble getCustOrderAR2(String strCustomerID) throws SQLException {

		String sql = "SELECT ordawmny FROM bd_cumandoc WHERE pk_cumandoc = ? ";

		Connection con = null;
		PreparedStatement stmt = null;

		UFDouble result = new UFDouble(0);
		try {
			con = getConnection();
			stmt = con.prepareStatement(sql);
			//ccustomerid
			stmt.setString(1, strCustomerID);
			ResultSet rs = stmt.executeQuery();
			//
			if (rs.next()) {
				BigDecimal n = rs.getBigDecimal(1);
				result = (n == null ? new UFDouble(0) : new UFDouble(n));
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

		return result;
	}

	/* (non-Javadoc)
	 * @see nc.impl.scm.so.pub.ICreditControl#getCustOrderAR2(AggregatedValueObject)
	 */
	public UFDouble getCustOrderAR2(AggregatedValueObject vo)
			throws SQLException {
		String strCustomerID = (String) vo.getParentVO().getAttributeValue(
				"ccustomerid");
		return getCustOrderAR2(strCustomerID);
	}

	/* (non-Javadoc)
	 * @see nc.impl.scm.so.pub.ICreditControl#getCustTestSaleMoney(java.lang.String, java.lang.String)
	 */
	public UFDouble getCustTestSaleMoney(String strCustomerID, String strCorpID)
			throws SQLException {
		/** ********************************************************** */
		// 保留的系统管理接口：
		beforeCallMethod("nc.bs.so.pub.CreditControlDMO",
				"getCustTestSaleMoney", new Object[] { strCustomerID });
		/** ********************************************************** */

		String sql = "SELECT testsalemoney FROM bd_cumandoc WHERE pk_cumandoc = ? ";

		Connection con = null;
		PreparedStatement stmt = null;

		UFDouble result = new UFDouble(0);
		try {
			con = getConnection();
			stmt = con.prepareStatement(sql);
			//ccustomerid
			stmt.setString(1, strCustomerID);

			ResultSet rs = stmt.executeQuery();
			//
			if (rs.next()) {
				BigDecimal n = rs.getBigDecimal(1);
				result = (n == null ? new UFDouble(0) : new UFDouble(n));
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
		afterCallMethod("nc.bs.so.pub.CreditControlDMO",
				"getCustTestSaleMoney", new Object[] { strCustomerID });
		/** ********************************************************** */

		return result;
	}
	
	/* (non-Javadoc)
	 * @see nc.impl.scm.so.pub.ICreditControl#getMaxDay(nc.vo.so.so001.SaleOrderVO)
	 */
	public int getMaxDay(nc.vo.so.so001.SaleOrderVO bill) {
		int result = 0;

		nc.vo.so.so001.SaleorderHVO billhead = (nc.vo.so.so001.SaleorderHVO) bill
				.getParentVO();
		nc.vo.so.so001.SaleorderBVO[] billbody = (nc.vo.so.so001.SaleorderBVO[]) bill
				.getChildrenVO();

		UFDate billdata = billhead.getDbilldate();

		if (billbody != null) {
			for (int i = 0; i < billbody.length; i++) {
				int d = UFDate.getDaysBetween(billdata, billbody[i]
						.getDconsigndate());
				if (d > result)
					result = d;
			}
		}
		return result;
	}

	/* (non-Javadoc)
	 * @see nc.impl.scm.so.pub.ICreditControl#getMaxInvDiscountrate(nc.vo.so.so001.SaleOrderVO)
	 */
	public UFDouble getMaxInvDiscountrate(nc.vo.so.so001.SaleOrderVO bill) {
		UFDouble result = null;
		double d = 0;
		nc.vo.so.so001.SaleorderBVO[] billbody = (nc.vo.so.so001.SaleorderBVO[]) bill
				.getChildrenVO();
		if (billbody != null) {
			for (int i = 0; i < billbody.length; i++) {
				if (billbody[i].getNdiscountrate() != null
						&& billbody[i].getNitemdiscountrate() != null) {
					double discountrate = ((billbody[i].getNdiscountrate()
							.getDouble() / 100) * (billbody[i]
							.getNitemdiscountrate().getDouble() / 100)) * 100;
					if (discountrate > d)
						d = discountrate;
				}
			}
		}
		result = new UFDouble(d);
		return result;
	}

	/* (non-Javadoc)
	 * @see nc.impl.scm.so.pub.ICreditControl#getMinDay(nc.vo.so.so001.SaleOrderVO)
	 */
	public int getMinDay(nc.vo.so.so001.SaleOrderVO bill) {
		int result = 100000;

		nc.vo.so.so001.SaleorderHVO billhead = (nc.vo.so.so001.SaleorderHVO) bill
				.getParentVO();
		nc.vo.so.so001.SaleorderBVO[] billbody = (nc.vo.so.so001.SaleorderBVO[]) bill
				.getChildrenVO();

		UFDate billdata = billhead.getDbilldate();

		if (billbody != null) {
			for (int i = 0; i < billbody.length; i++) {
				int d = UFDate.getDaysBetween(billdata, billbody[i]
						.getDconsigndate());
				if (i == 0 || d < result)
					result = d;
			}
		}
		return result;
	}

	/* (non-Javadoc)
	 * @see nc.impl.scm.so.pub.ICreditControl#getMinInvDiscountrate(nc.vo.so.so001.SaleOrderVO)
	 */
	public UFDouble getMinInvDiscountrate(nc.vo.so.so001.SaleOrderVO bill) {
		UFDouble result = null;
		double d = 100;
		nc.vo.so.so001.SaleorderBVO[] billbody = (nc.vo.so.so001.SaleorderBVO[]) bill
				.getChildrenVO();
		if (billbody != null) {
			for (int i = 0; i < billbody.length; i++) {
				if (billbody[i].getNdiscountrate() != null
						&& billbody[i].getNitemdiscountrate() != null) {
					double discountrate = ((billbody[i].getNdiscountrate()
							.getDouble() / 100) * (billbody[i]
							.getNitemdiscountrate().getDouble() / 100)) * 100;
					if (discountrate < d)
						d = discountrate;
				}
			}
		}
		result = new UFDouble(d);
		return result;
	}

	/**
	 * 获得操作员对应的业务员。 创建日期：(2001-9-3 9:30:24)
	 *
	 * @return nc.vo.pub.lang.UFDouble
	 */
	private String getOpIDTOPsnID(String strID) throws SQLException {
		String strPsnDocID = null;
		if (strID == null || strID.equals(""))
			return strPsnDocID;
		String strSQL = "Select pk_psndoc From sm_userandclerk where userid = ?";
		Connection con = null;
		PreparedStatement stmt = null;
		try {
			con = getConnection();
			stmt = con.prepareStatement(strSQL);
			//pk
			stmt.setString(1, strID);
			ResultSet rs = stmt.executeQuery();
			//
			if (rs.next()) {
				strPsnDocID = rs.getString(1);
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
		return strPsnDocID;
	}

	/* (non-Javadoc)
	 * @see nc.impl.scm.so.pub.ICreditControl#getReceiptSumMny(nc.vo.so.so001.SaleOrderVO)
	 */
	public UFDouble getReceiptSumMny(nc.vo.so.so001.SaleOrderVO vo)
			throws SQLException {
		String strOrderID = (String) vo.getParentVO().getAttributeValue(
				"csaleid");

		HashMap hs = new HashMap();
		UFDouble money = new UFDouble(0.0);
		if (((nc.vo.so.so001.SaleorderHVO) vo.getParentVO()).getCsaleid() != null
				&& ((nc.vo.so.so001.SaleorderHVO) vo.getParentVO())
						.getCsaleid().trim().length() > 0) {
			try {
				nc.impl.scm.so.so001.SaleOrderDMO dmo = new nc.impl.scm.so.so001.SaleOrderDMO();
				CircularlyAccessibleValueObject[] oldbodyVOs = dmo
						.queryAllBodyData(strOrderID);
				if (oldbodyVOs != null) {
					for (int i = 0; i < oldbodyVOs.length; i++) {
						nc.vo.so.so001.SaleorderBVO bvo = (nc.vo.so.so001.SaleorderBVO) oldbodyVOs[i];
						hs.put(bvo.getCorder_bid(), bvo);
						if (bvo.getBlargessflag() == null
								|| !bvo.getBlargessflag().booleanValue()) {
							money = money
									.add(bvo
											.getNsummny()
											.sub(
													bvo.getNtotalpaymny() == null ? new UFDouble(
															0.0)
															: bvo
																	.getNtotalpaymny()));
						}
					}
				}
			} catch (Exception e) {
			}
		}

		nc.vo.so.so001.SaleorderBVO[] bodyVOs = (nc.vo.so.so001.SaleorderBVO[]) vo
				.getChildrenVO();

		//UFDouble money = getOldOrderAR(strOrderID);

		for (int i = 0; i < bodyVOs.length; i++) {
			String strBodyID = bodyVOs[i].getCorder_bid();
			nc.vo.so.so001.SaleorderBVO bvo = (nc.vo.so.so001.SaleorderBVO) hs
					.get(strBodyID);
			switch (bodyVOs[i].getStatus()) {
			case VOStatus.NEW: {
				if (bodyVOs[i].getBlargessflag() != null
						&& bodyVOs[i].getBlargessflag().booleanValue())
					continue;
				money = money.add(bodyVOs[i].getNsummny());
				break;
			}
			case VOStatus.DELETED: {
				if (bvo != null && bvo.getBlargessflag() != null
						&& bvo.getBlargessflag().booleanValue())
					continue;
				if (bvo != null)
					money = money.sub(bvo.getNsummny().sub(
							bvo.getNtotalpaymny() == null ? new UFDouble(0.0)
									: bvo.getNtotalpaymny()));
				break;
			}
			case VOStatus.UPDATED: {
				if (bodyVOs[i].getBlargessflag() != null
						&& bodyVOs[i].getBlargessflag().booleanValue()) {
					if (bvo.getBlargessflag() == null
							|| !bvo.getBlargessflag().booleanValue()) {
						money = money.sub(bvo.getNsummny().sub(
								bvo.getNtotalpaymny() == null ? new UFDouble(
										0.0) : bvo.getNtotalpaymny()));
					}
				} else {
					if (bvo != null
							&& (bvo.getBlargessflag() == null || !bvo
									.getBlargessflag().booleanValue())) {
						money = money.sub(bvo.getNsummny().sub(
								bvo.getNtotalpaymny() == null ? new UFDouble(
										0.0) : bvo.getNtotalpaymny()));
					}
					money = money
							.add(bodyVOs[i]
									.getNsummny()
									.sub(
											bodyVOs[i].getNtotalpaymny() == null ? new UFDouble(
													0.0)
													: bodyVOs[i]
															.getNtotalpaymny()));
				}
				break;
			}
			}
		}
		return money;
	}

	/* (non-Javadoc)
	 * @see nc.impl.scm.so.pub.ICreditControl#isResPsn(nc.vo.so.so001.SaleOrderVO)
	 */
	public UFBoolean isResPsn(nc.vo.so.so001.SaleOrderVO saleorder)
			throws SQLException {

		UFBoolean reslut = new UFBoolean(false);

		nc.vo.so.so001.SaleorderHVO head = (nc.vo.so.so001.SaleorderHVO) saleorder
				.getParentVO();

		String opid = head.getCoperatorid();
		String custid = head.getCcustomerid();

		String oppsnid = getOpIDTOPsnID(opid);

		if (oppsnid != null) {
			String custpsnid = getCuIDTOPsnID(custid);
			if (custpsnid != null) {
				if (oppsnid.trim().equals(custpsnid.trim())) {
					reslut = new UFBoolean(true);
				}
			}
		}

		return reslut;
	}

	/* (non-Javadoc)
	 * @see nc.impl.scm.so.pub.ICreditControl#getNotNtotalinventorynumber(AggregatedValueObject)
	 */
	public UFDouble getNotNtotalinventorynumber(
			AggregatedValueObject vo) throws SQLException {

		return getOrdNotExeNumTotalByKey("ntotalinventorynumber",
				(nc.vo.so.so001.SaleOrderVO) vo);

	}

	/* (non-Javadoc)
	 * @see nc.impl.scm.so.pub.ICreditControl#getNotNtotalreceiptnumber(AggregatedValueObject)
	 */
	public UFDouble getNotNtotalreceiptnumber(AggregatedValueObject vo)
			throws SQLException {

		return getOrdNotExeNumTotalByKey("ntotalreceivenumber",
				(nc.vo.so.so001.SaleOrderVO) vo);

	}

	/**
	 * 获得订单未执行量。 创建日期：(2001-9-3 9:30:24)
	 *
	 * @return nc.vo.pub.lang.UFDouble
	 */
	private UFDouble getOrdNotExeNumTotalByKey(String key,
			nc.vo.so.so001.SaleOrderVO vo) throws SQLException {
		UFDouble uf0 = new UFDouble(0.0);
		UFDouble ntotalnum = null;

		if (key == null || key.trim().length() <= 0 || vo == null)
			return uf0;

		String csaleid = vo.getHeadVO().getCsaleid();

		//新增
		if (csaleid == null || csaleid.trim().length() <= 0) {
			ntotalnum = nc.vo.so.so016.SoVoTools.getTotalMny(vo.getBodyVOs(),
					"nnumber");
			if (ntotalnum == null)
				return uf0;
			else
				return ntotalnum;
		}
		//修改
		if (vo.getHeadVO().getFstatus() == null
				|| vo.getHeadVO().getFstatus().intValue() < 2) {
			ntotalnum = nc.vo.so.so016.SoVoTools.getTotalMny(vo
					.getAllSaleOrderVO() == null ? vo.getBodyVOs() : vo
					.getAllSaleOrderVO().getBodyVOs(), "nnumber");
			if (ntotalnum == null)
				return uf0;
			else
				return ntotalnum;
		}

		//修订
		if (vo.getAllSaleOrderVO() != null) {
			ntotalnum = nc.vo.so.so016.SoVoTools.getTotalMnySub(vo
					.getAllSaleOrderVO().getBodyVOs(), "nnumber", vo
					.getAllSaleOrderVO().getBodyVOs(), key);
			if (ntotalnum == null)
				return uf0;
			else
				return ntotalnum;
		}

		//其他
		String sql = " select COALESCE(nnumber,0)-COALESCE("
				+ key
				+ ",0) from so_saleorder_b,so_saleexecute "
				+ " where so_saleorder_b.csaleid='"
				+ csaleid
				+ "' and so_saleorder_b.beditflag = 'N' and so_saleorder_b.dr=0 "
				+ "  and so_saleorder_b.csaleid = so_saleexecute.csaleid "
				+ "  and so_saleorder_b.corder_bid = so_saleexecute.csale_bid ";

		nc.vo.so.so001.SORowData[] rows = nc.impl.scm.so.so016.SOToolsDMO
				.getAnyValueSORow(sql);

		if (rows == null || rows.length <= 0)
			return uf0;

		UFDouble uftemp = null;
		ntotalnum = uf0;
		for (int i = 0, loop = rows.length; i < loop; i++) {
			if (rows[i] != null) {
				uftemp = rows[i].getUFDouble(0);
				if (uftemp != null) {
					ntotalnum = ntotalnum.add(uftemp);
				}
			}
		}

		return ntotalnum;

	}

	/* (non-Javadoc)
	 * @see nc.impl.scm.so.pub.ICreditControl#getOrdPreceiveMny(AggregatedValueObject)
	 */
	public UFDouble getOrdPreceiveMny(AggregatedValueObject vo) {

		return (UFDouble) vo.getParentVO().getAttributeValue("npreceivemny");

	}

	/* (non-Javadoc)
	 * @see nc.impl.scm.so.pub.ICreditControl#getOrdReceiptCathMny(AggregatedValueObject)
	 */
	public UFDouble getOrdReceiptCathMny(AggregatedValueObject vo) {
		try {
			nc.impl.scm.so.so001.SaleOrderDMO dmo = new nc.impl.scm.so.so001.SaleOrderDMO();
			String csaleid = (String) vo.getParentVO().getAttributeValue(
					"csaleid");
			if (csaleid == null || csaleid.trim().length() <= 0)
				return null;
			return dmo.queryCachPayByOrdId(csaleid);
		} catch (Exception e) {
			nc.vo.scm.pub.SCMEnv.out(e.getMessage());
			/**
			 * 未获得金额
			 */
			return null;
		}

	}

	/* (non-Javadoc)
	 * @see nc.impl.scm.so.pub.ICreditControl#getPriceRate(AggregatedValueObject)
	 */
	public UFDouble getPriceRate(AggregatedValueObject vo) {
		try {
			nc.impl.scm.so.so001.SaleOrderDMO dmo = new nc.impl.scm.so.so001.SaleOrderDMO();
			String csaleid = (String) vo.getParentVO().getAttributeValue(
					"csaleid");
			if (csaleid == null || csaleid.trim().length() <= 0)
				return null;
			return dmo.queryCachPayByOrdId(csaleid);
		} catch (Exception e) {
			nc.vo.scm.pub.SCMEnv.out(e.getMessage());
			/**
			 * 未获得金额
			 */
			return null;
		}

	}
	/**
	 * 订单净价/询到净价：订单最终价格与询到价格的比率 ，没有业务逻辑判断 建日期：(2007-5-16 20:00:35)
	 * @param vo
	 * @return
	 * @throws nc.vo.pub.BusinessException
	 * 这个函数是对补丁：Ncscm31销售20061023001增加针对审批流的函数：订单含税净价／询价的含税价＊扣率（谷昌NC（二期））
	 * ,后来又有中铁物资
	 * 
	 *  v5.3此函数由"订单含税净价/询价的含税价*扣率"更名为"订单净价/询到净价"；取数规则调整
	 */
	public UFDouble examPriceRateForApprove(AggregatedValueObject vo)
	throws nc.vo.pub.BusinessException {
		SaleOrderVO ordvo = (SaleOrderVO)vo;
		if(ordvo.getAllSaleOrderVO()!=null)
			ordvo = ordvo.getAllSaleOrderVO();

		//计算价格比例：订单含税净价/询价的含税价*扣率
		SaleorderBVO[] bvos = ordvo.getBodyVOs();
		//存储表体每一行的函数计算结果
		UFDouble[] dpricerates = new UFDouble[bvos.length];
		
		//计算表体每一行的函数计算结果，并返回计算结果
		dpricerates = examPriceRate_process(ordvo,bvos);
		
		return dpricerates[dpricerates.length-1];

	}

	//wanglei 2011-10-28 返回订单行中订单净价/询到净价最小值
	public UFDouble examMinPriceRateForApprove(AggregatedValueObject vo)
	throws nc.vo.pub.BusinessException {
		SaleOrderVO ordvo = (SaleOrderVO)vo;
		if(ordvo.getAllSaleOrderVO()!=null)
			ordvo = ordvo.getAllSaleOrderVO();

		//计算价格比例：订单含税净价/询价的含税价*扣率
		SaleorderBVO[] bvos = ordvo.getBodyVOs();
		//存储表体每一行的函数计算结果
		UFDouble[] dpricerates = new UFDouble[bvos.length];
		
		//计算表体每一行的函数计算结果，并返回计算结果
		dpricerates = examPriceRate_process2(ordvo,bvos);
		
		UFDouble dpricerate = dpricerates==null? new UFDouble(UFDouble.ZERO_DBL): dpricerates[0] ;
		for (int i = 0 ; i < dpricerates.length; i++){
			if (dpricerate.compareTo(dpricerates[i]) < 0)
				continue;
			else
				dpricerate = dpricerates[i];
		}
		return dpricerate.setScale(2, UFDouble.ROUND_HALF_UP);
	}
	//end
	
	private UFDouble[] examPriceRate_process2(SaleOrderVO ordvo,SaleorderBVO[] bvos) throws BusinessException{
		// 获取系统参数SA02：基价是否含税
		String paras[] = { "SA02" };
//		String[] comnames = { "nc.bs.pub.para.SysInitDMO",
//				"nc.bs.pub.para.SysInitDMO", "nc.bs.pub.para.SysInitDMO" };
//		String[] funnames = { "queryBatchParaValues", "getParaBoolean",
//				"getPkValue" };
		// 系统参数
		ArrayList paramObjlist = new ArrayList();
		Object[] paramObjs = new Object[] { ordvo.getPk_corp(), paras };
		paramObjlist.add(paramObjs);
//		java.util.Hashtable para = null;
//		try {
			//2008-04-25 修改BY zhangcheng 
			UFBoolean SA02  =	new nc.bs.pub.para.SysInitDMO().getParaBoolean(ordvo.getPk_corp(), "SA02"); 
//			
//			Object[] retObjs = nc.ui.so.so001.panel.bom.BillTools
//					.batchRemoteCallQueryX(comnames, funnames, paramObjlist);
//			para = (Hashtable)retObjs[0];
//		} catch (Exception e) {
//			throw new BusinessException("获取系统参数SA02失败！", e);
//		}
//		UFBoolean SA02 = getParaBoolean(para, "SA02");
		
		//存储表体每一行的函数计算结果
		UFDouble[] dpricerates = new UFDouble[bvos.length];
		//订单净价
		UFDouble order_netprice = null;
		//订单数量
		UFDouble orderNumber = null;
		//询到净价
		UFDouble qt_netprice = null;
		//报价计量单位数量
		UFDouble quoteNumber = null;
		
		/*//单品折扣
		UFDouble ndiscountrate = null;
		//整单折扣
		UFDouble nheaddiscountrate = null;
		nheaddiscountrate = ordvo.getHeadVO().getNdiscountrate()==null?
				SoVoConst.duf1:ordvo.getHeadVO().getNdiscountrate();
		UFDouble uf100 = new UFDouble(100);*/
		
		//返回结果
//		UFDouble retd = null;
		UFBoolean blargessflag = null,laborflag = null, discountflag = null;
		for(int i=0,loop=bvos.length;i<loop;i++){
			blargessflag = bvos[i].getBlargessflag();
			//赠品不参加检查
			if(blargessflag!=null && blargessflag.booleanValue())
				continue;
			//是否劳务类存货，不参加检查
			laborflag = bvos[i].getLaborflag();
			if(laborflag!=null && laborflag.booleanValue())
				continue;
			//存货折扣属性，不参加检查
			discountflag = bvos[i].getDiscountflag();
			if(discountflag!=null && discountflag.booleanValue())
				continue;
			//询到净价=(基价含税？询价含税净价：询价无税净价)
			qt_netprice = SA02.booleanValue() ? bvos[i].getNqttaxnetprc() : bvos[i].getNqtorgnetprc();
			if(qt_netprice==null)
				continue;
			orderNumber = bvos[i].getNnumber();
			if (orderNumber == null)
				orderNumber = UFDouble.ONE_DBL;
			//订单净价=(基价含税？原币含税净价：原币无税净价)
			order_netprice = SA02.booleanValue() ? bvos[i].getNoriginalcurtaxnetprice() 
					: bvos[i].getNoriginalcurnetprice();
			if (order_netprice == null)
				continue;
			quoteNumber = bvos[i].getNquoteunitnum();
			if (quoteNumber == null)
				quoteNumber = UFDouble.ONE_DBL;
			
			//整单折扣
			/*if(bvos[i].getNdiscountrate()==null)
				ndiscountrate = nheaddiscountrate;
			else
				ndiscountrate = bvos[i].getNdiscountrate();
			if(bvos[i].getNitemdiscountrate()==null)
				ndiscountrate = ndiscountrate.div(uf100);
			else
				ndiscountrate = ndiscountrate.multiply(bvos[i].getNitemdiscountrate().div(uf100)).div(uf100);*/
			
			// 订单含税净价/询价的含税价
			// 当订单主单位和报价计量单位不一致时，“主单位含税净价/报价含税价格”是没有意义的
			// 这里用“(主单位含税净价*主数量)/(报价含税价格*报价单位数量)”来代替
			dpricerates[i] = (order_netprice.sub(qt_netprice)).multiply(100).div(qt_netprice,2,UFDouble.ROUND_HALF_UP);
		}
		return dpricerates;
	}
	/**
	 *  订单净价/询到净价：订单最终价格与询到价格的比率
	 *  v5.3此函数由"订单含税净价/询价的含税价*扣率"更名为"订单净价/询到净价"；取数规则调整；
	 * 	参数基价含税，  订单净价，取订单原币含税净价；
	 *               询到净价，取询价获得的最终价格（询价会获得价格、折扣、最终价，只需要存储最终价）。
	 *  参数基价不含税，订单净价，取订单原币无税净价；
	 *               询到净价，取询价获得的最终价格（询价会获得价格、折扣、最终价，只需要存储最终价）。
	 */
	public UFDouble examPriceRate(AggregatedValueObject vo)
			throws BusinessException {
		if(vo==null)
			return null;
		
		//暂存错误信息
		String errMsg = "";

		try {
			SaleOrderVO ordvo = (SaleOrderVO) vo;
			if (ordvo.getAllSaleOrderVO() != null){
				// 缓存iaction
				int newIAction = ordvo.getActionInt();
				ordvo = ordvo.getAllSaleOrderVO();
				ordvo.setIAction(newIAction);
			}

			/** 1.计算表体每一行函数计算结果************************************/
			// 计算价格比例：订单含税净价/询价的含税价*扣率
			SaleorderBVO[] bvos = ordvo.getBodyVOs();
			// 存储表体每一行的函数计算结果
			UFDouble[] dpricerates = new UFDouble[bvos.length];
			// 计算表体每一行的函数计算结果，并返回计算结果
			dpricerates = examPriceRate_process(ordvo, bvos);

			/** 2.检查表体每一行函数计算结果************************************/
			// 用于查询-单据动作约束检查函数的配置信息
			BusiFuncCalcDMO busifundmo = new BusiFuncCalcDMO();
			// 单据动作约束检查VO对象
			PfUtilActionConstrictVO[] actionConstricts = null;
			
			actionConstricts = busifundmo
					.queryActionConstrict(ordvo.getBillTypeCode(), ordvo
							.getBizTypeid(), ordvo.getActionCode(), ordvo
							.getPk_corp(), ordvo.getOperatorid());

			if (actionConstricts == null || actionConstricts.length <= 0)
				return null;

			ArrayList alist_funClassName = new ArrayList();
			ArrayList alist_className2 = new ArrayList();
			for (int i = 0, loop = actionConstricts.length; i < loop; i++) {
				if ("nc.impl.scm.so.pub.CreditControlDMO".equals(actionConstricts[i]
						.getFunClassName())
						&& "examPriceRate".equals(actionConstricts[i]
								.getMethod())) {
					alist_funClassName.add(actionConstricts[i]);
				} else if ("nc.impl.scm.so.pub.CreditControlDMO"
						.equals(actionConstricts[i].getClassName2())
						&& "examPriceRate".equals(actionConstricts[i]
								.getMethod2())) {
					alist_className2.add(actionConstricts[i]);
				}
			}

			PfUtilActionConstrictVO actionConstrictVo = null;

			if (alist_funClassName.size() > 0) {
				for (int i = 0, loop = alist_funClassName.size(); i < loop; i++) {
					actionConstrictVo = (PfUtilActionConstrictVO) alist_funClassName
							.get(i);
					String ysf = actionConstrictVo.getYsf();
					if (ysf == null || ysf.trim().length() <= 0)
						continue;
					Object value = null;
					String errMsg1 = null;
					if (actionConstrictVo.getClassName2() != null
							&& actionConstrictVo.getClassName2().equals(
									"nc.impl.scm.so.pub.CreditControlDMO")
							&& actionConstrictVo.getMethod2() != null
							&& actionConstrictVo.getMethod2().trim().length() > 0) {
						Method[] mds = this.getClass().getMethods();
						Method md = null;
						for (int k = 0, loopk = mds.length; k < loopk; k++) {
							if (mds[k].getName().equals(
									actionConstrictVo.getMethod2())) {
								if (mds[k].getParameterTypes().length == 1
										&& (mds[k].getParameterTypes()[0] == SaleOrderVO.class || mds[k]
												.getParameterTypes()[0] == AggregatedValueObject.class)) {
									md = mds[k];
									break;
								}
							}
						}
						if (md != null) {
							value = md.invoke(this, new Object[] { vo });
							errMsg1 = actionConstrictVo.getFunNote2();
							errMsg1 += "(" + value + ")";
						}
					} else if (actionConstrictVo.getValue() != null) {
						value = actionConstrictVo.getValue();
						errMsg1 = value.toString();
					}
					if (value == null)
						continue;
					UFDouble dvalue = new UFDouble(value.toString());
//					UFDouble diff = null;
					for (int m = 0, loopm = dpricerates.length; m < loopm; m++) {
						if (dpricerates[m] == null)
							continue;
						if (">".equals(ysf)
								&& dpricerates[m].compareTo(dvalue) <= 0) {
							if (bvos[m].getCrowno() != null
									&& bvos[m].getCrowno().trim().length() > 0)
								errMsg += NCLangResOnserver.getInstance()
										.getStrByID(
												"sopub",
												"UPPsopub-000071",
												null,
												new String[] {
														bvos[m].getCrowno(),
														dpricerates[m] + "",
														errMsg1 });
							// errMsg
							// +="行号为"+bvos[m].getCrowno()+"的订单行不满足约束条件：\n" +
							// "订单含税净价/询价的含税价*扣率("+dpricerates[m]+")>"+errMsg1;
							else
								errMsg += NCLangResOnserver.getInstance()
										.getStrByID(
												"sopub",
												"UPPsopub-0000171",
												null,
												new String[] {
														bvos[m].getCrowno(),
														dpricerates[m] + "",
														errMsg1 });
							// errMsg +="第"+(i+1)+"的订单行不满足约束条件：\n" +
							// "订单含税净价/询价的含税价*扣率("+dpricerates[m]+")>"+errMsg1;
						} else if (">=".equals(ysf)
								&& dpricerates[m].compareTo(dvalue) < 0) {
							if (bvos[m].getCrowno() != null
									&& bvos[m].getCrowno().trim().length() > 0)
								errMsg += NCLangResOnserver.getInstance()
										.getStrByID(
												"sopub",
												"UPPsopub-000072",
												null,
												new String[] {
														bvos[m].getCrowno(),
														dpricerates[m] + "",
														errMsg1 });
							// errMsg
							// +="行号为"+bvos[m].getCrowno()+"的订单行不满足约束条件：\n" +
							// "订单含税净价/询价的含税价*扣率("+dpricerates[m]+")>="+errMsg1;
							else
								errMsg += NCLangResOnserver.getInstance()
										.getStrByID(
												"sopub",
												"UPPsopub-0000172",
												null,
												new String[] {
														bvos[m].getCrowno(),
														dpricerates[m] + "",
														errMsg1 });
							// errMsg +="第"+(i+1)+"的订单行不满足约束条件：\n" +
							// "订单含税净价/询价的含税价*扣率("+dpricerates[m]+")>="+errMsg1;
						} else if ("=".equals(ysf)
								&& dpricerates[m].compareTo(dvalue) != 0) {
							if (bvos[m].getCrowno() != null
									&& bvos[m].getCrowno().trim().length() > 0)
								errMsg += NCLangResOnserver.getInstance()
										.getStrByID(
												"sopub",
												"UPPsopub-0000175",
												null,
												new String[] {
														bvos[m].getCrowno(),
														dpricerates[m] + "",
														errMsg1 });
							// errMsg
							// +="行号为"+bvos[m].getCrowno()+"的订单行不满足约束条件：\n" +
							// "订单含税净价/询价的含税价*扣率("+dpricerates[m]+")="+errMsg1;
							else
								errMsg += NCLangResOnserver.getInstance()
										.getStrByID(
												"sopub",
												"UPPsopub-0000176",
												null,
												new String[] {
														bvos[m].getCrowno(),
														dpricerates[m] + "",
														errMsg1 });
							// errMsg +="第"+(i+1)+"的订单行不满足约束条件：\n" +
							// "订单含税净价/询价的含税价*扣率("+dpricerates[m]+")="+errMsg1;
						} else if ("<".equals(ysf)
								&& dpricerates[m].compareTo(dvalue) >= 0) {
							if (bvos[m].getCrowno() != null
									&& bvos[m].getCrowno().trim().length() > 0)
								errMsg += NCLangResOnserver.getInstance()
										.getStrByID(
												"sopub",
												"UPPsopub-000073",
												null,
												new String[] {
														bvos[m].getCrowno(),
														dpricerates[m] + "",
														errMsg1 });
							// errMsg
							// +="行号为"+bvos[m].getCrowno()+"的订单行不满足约束条件：\n" +
							// "订单含税净价/询价的含税价*扣率("+dpricerates[m]+")<"+errMsg1;
							else
								errMsg += NCLangResOnserver.getInstance()
										.getStrByID(
												"sopub",
												"UPPsopub-0000173",
												null,
												new String[] {
														bvos[m].getCrowno(),
														dpricerates[m] + "",
														errMsg1 });
							// errMsg +="第"+(i+1)+"的订单行不满足约束条件：\n" +
							// "订单含税净价/询价的含税价*扣率("+dpricerates[m]+")<"+errMsg1;
						} else if ("<=".equals(ysf)
								&& dpricerates[m].compareTo(dvalue) > 0) {
							if (bvos[m].getCrowno() != null
									&& bvos[m].getCrowno().trim().length() > 0)
								errMsg += NCLangResOnserver.getInstance()
										.getStrByID(
												"sopub",
												"UPPsopub-000074",
												null,
												new String[] {
														bvos[m].getCrowno(),
														dpricerates[m] + "",
														errMsg1 });
							// errMsg
							// +="行号为"+bvos[m].getCrowno()+"的订单行不满足约束条件：\n" +
							// "订单含税净价/询价的含税价*扣率("+dpricerates[m]+")<="+errMsg1;
							else
								errMsg += NCLangResOnserver.getInstance()
										.getStrByID(
												"sopub",
												"UPPsopub-0000174",
												null,
												new String[] {
														bvos[m].getCrowno(),
														dpricerates[m] + "",
														errMsg1 });
							// errMsg +="第"+(i+1)+"的订单行不满足约束条件：\n" +
							// "订单含税净价/询价的含税价*扣率("+dpricerates[m]+")<="+errMsg1;
						}
						// if(">".equals(ysf) || ">=".equals(ysf) ||
						// "=".equals(ysf)){
						// if(retdmin==null)
						// retdmin = dpricerates[m];
						// if(dpricerates[m].compareTo(retdmin)<0)
						// retdmin = dpricerates[m];
						// }else{
						// if(retdmax==null)
						// retdmax = dpricerates[m];
						// if(dpricerates[m].compareTo(retdmax)>0)
						// retdmax = dpricerates[m];
						// }
					}
				}
			}
			// ///////////////////////////////////////////////////////////////////////
			if (alist_className2.size() > 0) {
				for (int i = 0, loop = alist_className2.size(); i < loop; i++) {
					actionConstrictVo = (PfUtilActionConstrictVO) alist_className2
							.get(i);
					String ysf = actionConstrictVo.getYsf();
					if (ysf == null || ysf.trim().length() <= 0)
						continue;
					Object value = null;
					String errMsg1 = null;
					if (actionConstrictVo.getFunClassName() != null
							&& actionConstrictVo.getFunClassName().equals(
									"nc.impl.scm.so.pub.CreditControlDMO")
							&& actionConstrictVo.getMethod() != null
							&& actionConstrictVo.getMethod().trim().length() > 0) {
						Method[] mds = this.getClass().getMethods();
						Method md = null;
						for (int k = 0, loopk = mds.length; k < loopk; k++) {
							if (mds[k].getName().equals(
									actionConstrictVo.getMethod())) {
								if (mds[k].getParameterTypes().length == 1
										&& (mds[k].getParameterTypes()[0] == SaleOrderVO.class || mds[k]
												.getParameterTypes()[0] == AggregatedValueObject.class)) {
									md = mds[k];
									break;
								}
							}
						}
						if (md != null) {
							value = md.invoke(this, new Object[] { vo });
							errMsg1 = actionConstrictVo.getFunNote();
							errMsg1 += "(" + value + ")";
						}
					} else if (actionConstrictVo.getValue() != null) {
						value = actionConstrictVo.getValue();
						errMsg1 = value.toString();
					}
					if (value == null)
						continue;
					UFDouble dvalue = new UFDouble(value.toString());
//					UFDouble diff = null;
					for (int m = 0, loopm = dpricerates.length; m < loopm; m++) {
						if (dpricerates[m] == null)
							continue;
						if (">".equals(ysf)
								&& dvalue.compareTo(dpricerates[m]) <= 0) {
							if (bvos[m].getCrowno() != null
									&& bvos[m].getCrowno().trim().length() > 0)
								errMsg += NCLangResOnserver.getInstance()
										.getStrByID(
												"sopub",
												"UPPsopub-000071",
												null,
												new String[] {
														bvos[m].getCrowno(),
														errMsg1,
														dpricerates[m] + "" });
							// errMsg
							// +="行号为"+bvos[m].getCrowno()+"的订单行不满足约束条件：\n" +
							// errMsg1+">"+"订单含税净价/询价的含税价*扣率("+dpricerates[m]+")";
							else
								errMsg += NCLangResOnserver.getInstance()
										.getStrByID(
												"sopub",
												"UPPsopub-0000171",
												null,
												new String[] {
														bvos[m].getCrowno(),
														errMsg1,
														dpricerates[m] + "" });
							// errMsg +="第"+(i+1)+"的订单行不满足约束条件：\n" +
							// errMsg1+">"+"订单含税净价/询价的含税价*扣率("+dpricerates[m]+")";
						} else if (">=".equals(ysf)
								&& dvalue.compareTo(dpricerates[m]) < 0) {
							if (bvos[m].getCrowno() != null
									&& bvos[m].getCrowno().trim().length() > 0)
								errMsg += NCLangResOnserver.getInstance()
										.getStrByID(
												"sopub",
												"UPPsopub-000072",
												null,
												new String[] {
														bvos[m].getCrowno(),
														errMsg1,
														dpricerates[m] + "" });
							// errMsg
							// +="行号为"+bvos[m].getCrowno()+"的订单行不满足约束条件：\n" +
							// errMsg1+">="+"订单含税净价/询价的含税价*扣率("+dpricerates[m]+")";
							else
								errMsg += NCLangResOnserver.getInstance()
										.getStrByID(
												"sopub",
												"UPPsopub-0000172",
												null,
												new String[] {
														bvos[m].getCrowno(),
														errMsg1,
														dpricerates[m] + "" });
							// errMsg +="第"+(i+1)+"的订单行不满足约束条件：\n" +
							// errMsg1+">"+"订单含税净价/询价的含税价*扣率("+dpricerates[m]+")";
						} else if ("=".equals(ysf)
								&& dvalue.compareTo(dpricerates[m]) != 0) {
							if (bvos[m].getCrowno() != null
									&& bvos[m].getCrowno().trim().length() > 0)
								errMsg += NCLangResOnserver.getInstance()
										.getStrByID(
												"sopub",
												"UPPsopub-0000175",
												null,
												new String[] {
														bvos[m].getCrowno(),
														errMsg1,
														dpricerates[m] + "" });
							// errMsg
							// +="行号为"+bvos[m].getCrowno()+"的订单行不满足约束条件：\n" +
							// errMsg1+"="+"订单含税净价/询价的含税价*扣率("+dpricerates[m]+")";
							else
								errMsg += NCLangResOnserver.getInstance()
										.getStrByID(
												"sopub",
												"UPPsopub-0000176",
												null,
												new String[] {
														bvos[m].getCrowno(),
														errMsg1,
														dpricerates[m] + "" });
							//errMsg +="第"+(i+1)+"的订单行不满足约束条件：\n" +
							//          errMsg1+"="+"订单含税净价/询价的含税价*扣率("+dpricerates[m]+")";
						} else if ("<".equals(ysf)
								&& dvalue.compareTo(dpricerates[m]) >= 0) {
							if (bvos[m].getCrowno() != null
									&& bvos[m].getCrowno().trim().length() > 0)
								errMsg += NCLangResOnserver.getInstance()
										.getStrByID(
												"sopub",
												"UPPsopub-000073",
												null,
												new String[] {
														bvos[m].getCrowno(),
														errMsg1,
														dpricerates[m] + "" });
							//errMsg +="行号为"+bvos[m].getCrowno()+"的订单行不满足约束条件：\n" +
							//		  errMsg1+"<"+"订单含税净价/询价的含税价*扣率("+dpricerates[m]+")";
							else
								errMsg += NCLangResOnserver.getInstance()
										.getStrByID(
												"sopub",
												"UPPsopub-0000173",
												null,
												new String[] {
														bvos[m].getCrowno(),
														errMsg1,
														dpricerates[m] + "" });
							//errMsg +="第"+(i+1)+"的订单行不满足约束条件：\n" +
							//          errMsg1+"<"+"订单含税净价/询价的含税价*扣率("+dpricerates[m]+")";
						} else if ("<=".equals(ysf)
								&& dvalue.compareTo(dpricerates[m]) > 0) {
							if (bvos[m].getCrowno() != null
									&& bvos[m].getCrowno().trim().length() > 0)
								errMsg += NCLangResOnserver.getInstance()
										.getStrByID(
												"sopub",
												"UPPsopub-000074",
												null,
												new String[] {
														bvos[m].getCrowno(),
														errMsg1,
														dpricerates[m] + "" });
							//errMsg +="行号为"+bvos[m].getCrowno()+"的订单行不满足约束条件：\n" +
							//		  errMsg1+"<="+"订单含税净价/询价的含税价*扣率("+dpricerates[m]+")";
							else
								errMsg += NCLangResOnserver.getInstance()
										.getStrByID(
												"sopub",
												"UPPsopub-0000174",
												null,
												new String[] {
														bvos[m].getCrowno(),
														errMsg1,
														dpricerates[m] + "" });
							//errMsg +="第"+(i+1)+"的订单行不满足约束条件：\n" +
							//          errMsg1+"<="+"订单含税净价/询价的含税价*扣率("+dpricerates[m]+")";
						}
						//						if(">".equals(ysf) || ">=".equals(ysf) || "=".equals(ysf)){
						//							if(retdmax==null)
						//								retdmax = dpricerates[m];
						//							if(dpricerates[m].compareTo(retdmax)>0)
						//								retdmax = dpricerates[m];
						//						}else{
						//							if(retdmin==null)
						//								retdmin = dpricerates[m];
						//							if(dpricerates[m].compareTo(retdmin)<0)
						//								retdmin = dpricerates[m];
						//						}
					}
				}
			}

			if (errMsg != null && errMsg.trim().length() > 0)
				throw new nc.vo.pub.BusinessException(errMsg);

			return dpricerates[dpricerates.length - 1];

		} catch (Exception e) {
			/**no throw exception*/
			if(SoVoTools.isEmptyString(errMsg))
				return null;
			throw new BusinessException(e.getMessage(), e);
		}
	}

	private UFDouble[] examPriceRate_process(SaleOrderVO ordvo,SaleorderBVO[] bvos) throws BusinessException{
		// 获取系统参数SA02：基价是否含税
		String paras[] = { "SA02" };
//		String[] comnames = { "nc.bs.pub.para.SysInitDMO",
//				"nc.bs.pub.para.SysInitDMO", "nc.bs.pub.para.SysInitDMO" };
//		String[] funnames = { "queryBatchParaValues", "getParaBoolean",
//				"getPkValue" };
		// 系统参数
		ArrayList paramObjlist = new ArrayList();
		Object[] paramObjs = new Object[] { ordvo.getPk_corp(), paras };
		paramObjlist.add(paramObjs);
//		java.util.Hashtable para = null;
//		try {
			//2008-04-25 修改BY zhangcheng 
			UFBoolean SA02  =	new nc.bs.pub.para.SysInitDMO().getParaBoolean(ordvo.getPk_corp(), "SA02"); 
//			
//			Object[] retObjs = nc.ui.so.so001.panel.bom.BillTools
//					.batchRemoteCallQueryX(comnames, funnames, paramObjlist);
//			para = (Hashtable)retObjs[0];
//		} catch (Exception e) {
//			throw new BusinessException("获取系统参数SA02失败！", e);
//		}
//		UFBoolean SA02 = getParaBoolean(para, "SA02");
		
		//存储表体每一行的函数计算结果
		UFDouble[] dpricerates = new UFDouble[bvos.length];
		//订单净价
		UFDouble order_netprice = null;
		//订单数量
		UFDouble orderNumber = null;
		//询到净价
		UFDouble qt_netprice = null;
		//报价计量单位数量
		UFDouble quoteNumber = null;
		
		/*//单品折扣
		UFDouble ndiscountrate = null;
		//整单折扣
		UFDouble nheaddiscountrate = null;
		nheaddiscountrate = ordvo.getHeadVO().getNdiscountrate()==null?
				SoVoConst.duf1:ordvo.getHeadVO().getNdiscountrate();
		UFDouble uf100 = new UFDouble(100);*/
		
		//返回结果
//		UFDouble retd = null;
		UFBoolean blargessflag = null,laborflag = null, discountflag = null;
		for(int i=0,loop=bvos.length;i<loop;i++){
			blargessflag = bvos[i].getBlargessflag();
			//赠品不参加检查
			if(blargessflag!=null && blargessflag.booleanValue())
				continue;
			//是否劳务类存货，不参加检查
			laborflag = bvos[i].getLaborflag();
			if(laborflag!=null && laborflag.booleanValue())
				continue;
			//存货折扣属性，不参加检查
			discountflag = bvos[i].getDiscountflag();
			if(discountflag!=null && discountflag.booleanValue())
				continue;
			//询到净价=(基价含税？询价含税净价：询价无税净价)
			qt_netprice = SA02.booleanValue() ? bvos[i].getNqtorgtaxnetprc() : bvos[i].getNqtorgnetprc();
			if(qt_netprice==null)
				continue;
			orderNumber = bvos[i].getNnumber();
			if (orderNumber == null)
				orderNumber = UFDouble.ONE_DBL;
			//订单净价=(基价含税？原币含税净价：原币无税净价)
			order_netprice = SA02.booleanValue() ? bvos[i].getNoriginalcurtaxnetprice() 
					: bvos[i].getNoriginalcurnetprice();
			if (order_netprice == null)
				continue;
			quoteNumber = bvos[i].getNquoteunitnum();
			if (quoteNumber == null)
				quoteNumber = UFDouble.ONE_DBL;
			
			//整单折扣
			/*if(bvos[i].getNdiscountrate()==null)
				ndiscountrate = nheaddiscountrate;
			else
				ndiscountrate = bvos[i].getNdiscountrate();
			if(bvos[i].getNitemdiscountrate()==null)
				ndiscountrate = ndiscountrate.div(uf100);
			else
				ndiscountrate = ndiscountrate.multiply(bvos[i].getNitemdiscountrate().div(uf100)).div(uf100);*/
			
			// 订单含税净价/询价的含税价
			// 当订单主单位和报价计量单位不一致时，“主单位含税净价/报价含税价格”是没有意义的
			// 这里用“(主单位含税净价*主数量)/(报价含税价格*报价单位数量)”来代替
			dpricerates[i] = order_netprice.multiply(orderNumber).div(qt_netprice.multiply(quoteNumber));
		}
		return dpricerates;
	}
	
	/**
	 * 不允许修改、删除他人的单据
	 */
	public UFBoolean examCoperatorid(AggregatedValueObject vo)
			throws BusinessException {

		if (vo == null || vo.getParentVO() == null)
			return SoVoConst.buftrue;

		String curuserid = null;

		if (vo.getClass() == SaleOrderVO.class) {
			curuserid = ((SaleOrderVO) vo).getCuruserid();
		} else if (vo.getClass() == SaleinvoiceVO.class) {
			curuserid = ((SaleinvoiceVO) vo).getCuruserid();
		} else if (vo instanceof SaleReceiveVO) {
			curuserid = ((SaleReceiveVO) vo).getCl().getUser();
		}

		if(curuserid==null )
			return SoVoConst.buftrue;

		String coperatorid = (String)vo.getParentVO().getAttributeValue("coperatorid");
		if(coperatorid==null)
			return SoVoConst.buftrue;

		return curuserid.equals(coperatorid)?SoVoConst.buftrue:SoVoConst.buffalse;

	}

	/**
	 * 检查制单人审批人不能为同一人
	 */
	public UFBoolean examCapproveid(AggregatedValueObject vo)
			throws BusinessException {

		if(vo==null || vo.getParentVO()==null)
			return SoVoConst.buftrue;

		String capproveid = (String)vo.getParentVO().getAttributeValue("capproveid");
		if(capproveid==null)
			return SoVoConst.buftrue;
		String coperatorid = (String)vo.getParentVO().getAttributeValue("coperatorid");

		return capproveid.equals(coperatorid)?SoVoConst.buffalse:SoVoConst.buftrue;

	}
	
	/* (non-Javadoc)
	 * @see nc.impl.scm.so.pub.ICreditControl#getCustBusiAr(AggregatedValueObject)
	 */
	public UFDouble getCustBusiAr(AggregatedValueObject voOrder)
	throws Exception {
		UFDouble dRe = new UFDouble(0.0);
		if (!(voOrder instanceof IExamAVO) || voOrder == null) {
			throw new BusinessException("传入的单据为空！");
		}
		IExamAVO vo = (IExamAVO) voOrder;
//		组织输入数据
		//BillCreditOriginVO voOrigin = new BillCreditOriginVO();
		int m_iBillType = vo.getBillTypeInt();
		int m_iBillAct = vo.getActionInt();
		if (m_iBillAct == 1) {//修改
			AggregatedValueObject voModified = vo.getModifiedVO();
			dRe = dRe.add(getBillBusiAr(voModified,m_iBillType));
			AggregatedValueObject voOld = vo.getOldVO();
			dRe = dRe.sub(getBillBusiAr(voOld,m_iBillType));
		} else {
			dRe = dRe.add(getBillBusiAr(voOrder,m_iBillType));
		}
		dRe = dRe.add(getBusiArFromCust(voOrder,0));
		return dRe;
	}

	/* (non-Javadoc)
	 * @see nc.impl.scm.so.pub.ICreditControl#getCustCreditMny(AggregatedValueObject)
	 */
	public UFDouble getCustCreditMny(AggregatedValueObject voOrder)
	throws Exception {
		UFDouble dRe = new UFDouble(0.0);
		if (!(voOrder instanceof IExamAVO) || voOrder == null) {
			throw new BusinessException("传入的单据为空！");
		}		
		dRe = dRe.add(getBusiArFromCust(voOrder,1));
		return dRe;
	}
	
	private UFDouble getBillBusiAr(AggregatedValueObject voOrder,
			int m_iBillType) {
		UFDouble dRe = new UFDouble(0.0);
		if (voOrder != null) {
			CircularlyAccessibleValueObject[] bodys = voOrder.getChildrenVO();
			Object obj = null;
			if (bodys != null) {
				for (int i = 0; i < bodys.length; i++) {
					if (bodys[i].getStatus() != VOStatus.DELETED) {
						obj = bodys[i].getAttributeValue("ntaxmny");
						if (obj != null) {
							dRe = dRe.add(new UFDouble(obj.toString()));
						}
					}
				}
			}
		}
		return dRe;
	}
	
	private UFDouble getBusiArFromCust(AggregatedValueObject voOrder,int type) throws Exception
	{
		UFDouble dRe = new UFDouble(0.0);
		if (voOrder != null) {
			CircularlyAccessibleValueObject parent = voOrder.getParentVO();
			String obj = null;
			obj = (String)parent.getAttributeValue("ccustomerid");
			if(obj !=null )
			{
				String sql = null;
				if(type == 0)
				{
					sql = "select busawmny from bd_cumandoc where pk_cumandoc = '"+obj+"'";
				}
				else 
				{
					sql = "select creditmny from bd_cumandoc where pk_cumandoc = '"+obj+"'";
				}
				
				SmartDMO dmo = new SmartDMO();
				Object[] o = dmo.selectBy2(sql);
				if(o!=null && o.length>0)
				{
					Object[] o1 =(Object[])o[0]; 
					if(o1!=null && o1.length>0 && o1[0]!=null )
						dRe = dRe.add(new UFDouble(o1[0].toString()));
				}
			}
			obj = (String)parent.getAttributeValue("pk_corp");
			if(obj!=null){
				nc.bs.scm.pub.DigitalQueryDMO digitalDmo = new nc.bs.scm.pub.DigitalQueryDMO();
				int iScale = digitalDmo.getCurrDecimal(obj).intValue();
				dRe = dRe
				.setScale(0 - iScale, UFDouble.ROUND_HALF_UP);
			}
			
		}
		
		
		return dRe;
	}
	
	/* (non-Javadoc)
	 * @see nc.impl.scm.so.pub.ICreditControl#examOverCreditGeneral(AggregatedValueObject)
	 */
	public UFDouble examOverCreditGeneral(AggregatedValueObject voGeneral)
			throws Exception {
		IBillInvokeCreditManager invoke = (IBillInvokeCreditManager) NCLocator
				.getInstance().lookup(IBillInvokeCreditManager.class.getName());
		return invoke.examOverCreditGeneral(voGeneral);
	}

	/* (non-Javadoc)
	 * @see nc.impl.scm.so.pub.ICreditControl#examOverCreditOrder(AggregatedValueObject)
	 */
	public UFDouble examOverCreditOrder(AggregatedValueObject voOrder)
			throws Exception{
		IBillInvokeCreditManager invoke = (IBillInvokeCreditManager) NCLocator
				.getInstance().lookup(IBillInvokeCreditManager.class.getName());
		return invoke.examOverCreditOrder(voOrder);
	}

	/* (non-Javadoc)
	 * @see nc.impl.scm.so.pub.ICreditControl#examOverPeriodGeneral(AggregatedValueObject)
	 */
	public  UFDouble examOverPeriodGeneral(AggregatedValueObject voOrder)
			throws Exception{
		IBillInvokeCreditManager invoke = (IBillInvokeCreditManager) NCLocator
				.getInstance().lookup(IBillInvokeCreditManager.class.getName());
		return invoke.examOverPeriodGeneral(voOrder);
	}

	/* (non-Javadoc)
	 * @see nc.impl.scm.so.pub.ICreditControl#examOverPeriodOrder(AggregatedValueObject)
	 */
	public  UFDouble examOverPeriodOrder(AggregatedValueObject voOrder)
			throws Exception{
		IBillInvokeCreditManager invoke = (IBillInvokeCreditManager) NCLocator
				.getInstance().lookup(IBillInvokeCreditManager.class.getName());
		return invoke.examOverPeriodOrder(voOrder);
	}

	/* (non-Javadoc)
	 * @see nc.impl.scm.so.pub.ICreditControl#examOverPeriodOrderForAudit(AggregatedValueObject)
	 */
	public  UFDouble examOverPeriodOrderForAudit(AggregatedValueObject voOrder)
	throws Exception{
		IBillInvokeCreditManager invoke = (IBillInvokeCreditManager) NCLocator
				.getInstance().lookup(IBillInvokeCreditManager.class.getName());
		return invoke.examOverPeriodOrderForAudit(voOrder);
	}
	/* (non-Javadoc)
	 * @see nc.impl.scm.so.pub.ICreditControl#examOverCreditOrderForAudit(AggregatedValueObject)
	 */
	public  UFDouble examOverCreditOrderForAudit(
			AggregatedValueObject voOrder) throws Exception{
		IBillInvokeCreditManager invoke = (IBillInvokeCreditManager) NCLocator
				.getInstance().lookup(IBillInvokeCreditManager.class.getName());
		return invoke.examOverCreditOrderForAudit(voOrder);
	}

	/* (non-Javadoc)
	 * @see nc.impl.scm.so.pub.ICreditControl#examOverCreditGeneralForAudit(AggregatedValueObject)
	 */
	public  UFDouble examOverCreditGeneralForAudit(
			AggregatedValueObject voOrder) throws Exception{
		IBillInvokeCreditManager invoke = (IBillInvokeCreditManager) NCLocator
				.getInstance().lookup(IBillInvokeCreditManager.class.getName());
		return invoke.examOverCreditGeneralForAudit(voOrder);
	}
	
	/**
	 * 客户信用保证金。 创建日期：(2001-6-21 14:00:35)
	 *
	 * @return nc.vo.pub.lang.UFDouble
	 * @param strCustomerID
	 *            java.lang.String
	 */
	private UFDouble getCustTestSaleMoney(String strCustomerID)
			throws SQLException {
		/** ********************************************************** */
		// 保留的系统管理接口：
		beforeCallMethod("nc.bs.so.pub.CreditControlDMO", "getCustCreditMoney",
				new Object[] { strCustomerID });
		/** ********************************************************** */

		String sql = "SELECT testsalemoney FROM bd_cumandoc WHERE pk_cumandoc = ? ";

		Connection con = null;
		PreparedStatement stmt = null;

		UFDouble result = new UFDouble(0);
		try {
			con = getConnection();
			stmt = con.prepareStatement(sql);
			//ccustomerid
			stmt.setString(1, strCustomerID);

			ResultSet rs = stmt.executeQuery();
			//
			if (rs.next()) {
				BigDecimal n = rs.getBigDecimal(1);
				result = (n == null ? new UFDouble(0) : new UFDouble(n));
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
		afterCallMethod("nc.bs.so.pub.CreditControlDMO", "getCustCreditMoney",
				new Object[] { strCustomerID });
		/** ********************************************************** */

		return result;
	}
	/**
	 * 客户信用保证金。 创建日期：(2001-6-21 14:00:35)
	 *
	 * @return nc.vo.pub.lang.UFDouble
	 * @param strCustomerID
	 *            java.lang.String
	 */
	public UFDouble getCustTestSaleMoney(AggregatedValueObject vo)
			throws SQLException {
		String strCustomerID = (String) vo.getParentVO().getAttributeValue(
				"ccustomerid");
		return getCustTestSaleMoney(strCustomerID);
	}

	/**
	 * 超信用金额/信用额度。（审批流使用）
	 * 
	 * 使用已有函数：检查订单超信用
	 * 
	 * 客户信用额度：信用模块取数，而非客商管理档案
	 * 
	 */
	public UFDouble calCustOvrOrdDivCrdtForAudit(AggregatedValueObject voOrder)
			throws Exception {
		IBillInvokeCreditManager invoke = (IBillInvokeCreditManager) NCLocator
				.getInstance().lookup(IBillInvokeCreditManager.class.getName());
		return invoke.examOverCreditRate(voOrder);
	}
	
	/** ========================发货单检查函数==============================* */
	/**
	 * '发货单'超信用金额
	 */
	public UFDouble examOverCreditReceive(AggregatedValueObject vo) throws Exception {
		if(vo==null)
			throw new BusinessException("单据信息错误");
		
		SaleReceiveVO srvo = (SaleReceiveVO)vo;
		
		if(!isProdEnable(srvo.getPk_corp(),"SO6"))
			throw new BusinessException("信用模块没有启用");
		
		//按客户分单
		IReceiveService ibo = (IReceiveService)NCLocator.getInstance().lookup(IReceiveService.class.getName());
		AggregatedValueObject[] avos = ibo.SplitReceiveVOs(new AggregatedValueObject[] { vo }, null,
				new String[] { "ccustmandocid" }, null, 1);
		for (AggregatedValueObject avo : avos){
			((SaleReceiveVO)avo).setIAction(srvo.getIAction());
			((SaleReceiveVO)avo).setCheckCredit(srvo.isCheckCredit());
			((SaleReceiveVO)avo).setCheckPeriod(srvo.isCheckPeriod());
		}
				
		IBillInvokeCreditManager invoke = (IBillInvokeCreditManager) NCLocator.getInstance().lookup(
				IBillInvokeCreditManager.class.getName());
		
		return invoke.examOverCreditReceiveBatch(avos);
		
	}
	
	/**
	 * '发货单'检查发货单超账期
	 * 
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	public UFDouble examOverPeriodReceive(AggregatedValueObject vo) throws Exception {
		if(vo==null)
			throw new BusinessException("单据信息错误");
		
		SaleReceiveVO srvo = (SaleReceiveVO)vo;
		
		if(!isProdEnable(srvo.getPk_corp(),"SO6"))
			throw new BusinessException("信用模块没有启用");
		
		//按客户分单
		IReceiveService ibo = (IReceiveService)NCLocator.getInstance().lookup(IReceiveService.class.getName());
		AggregatedValueObject[] avos = ibo.SplitReceiveVOs(new AggregatedValueObject[] { vo }, null,
				new String[] { "ccustmandocid" }, null, 1);
		
		for (AggregatedValueObject avo : avos){
			((SaleReceiveVO)avo).setIAction(srvo.getIAction());
			((SaleReceiveVO)avo).setCheckCredit(srvo.isCheckCredit());
			((SaleReceiveVO)avo).setCheckPeriod(srvo.isCheckPeriod());
		}
		
		IBillInvokeCreditManager invoke = (IBillInvokeCreditManager) NCLocator.getInstance().lookup(
				IBillInvokeCreditManager.class.getName());
		return invoke.examOverPeriodReceiveBatch(avos);
	}
	
	/**
	 * '发货单'只能弃审本人审批的单据
	 * 
	 * @param vo
	 * @return
	 * @throws BusinessException
	 */
	public UFBoolean examCunapproveid(AggregatedValueObject vo) throws Exception {

		// 单据类型
		String sbillType = (String) vo.getParentVO().getAttributeValue("creceipttype");
		String table = null, pk = null;

		// 当前弃审人员
		String cunapproveid = null;
		if (sbillType.equals(SaleBillType.SaleOrder)) {
			cunapproveid = ((SaleOrderVO) vo).getCuruserid();
			table = "so_sale";
			pk = "csaleid";
		} else if (sbillType.equals(SaleBillType.SaleReceive)) {
			cunapproveid = ((SaleReceiveVO) vo).getCl().getUser();
			table = "so_salereceive";
			pk = "csalereceiveid";
		}
		// add by fengjb 发票支持该校验函数
		else if (SaleBillType.SaleInvoice.equals(sbillType)) {
			cunapproveid = ((SaleinvoiceVO) vo).getCuruserid();
			table = "so_saleinvoice";
			pk = "csaleid";
		}

		// 查询当前单据记录的审批人
		Object[] obj = new SmartDMO().selectBy2("select capproveid from " + table + " where dr = 0 and " + pk + "='"
				+ vo.getParentVO().getPrimaryKey() + "'");
		String capproveid = ((Object[]) obj[0])[0].toString();

		// 比较 返回结果
		return capproveid.equals(cunapproveid) ? SoVoConst.buftrue : SoVoConst.buffalse;

	}

	public UFBoolean examBatchInv(AggregatedValueObject vo) throws BusinessException {
		try {
			SaleReceiveHVO head = ((SaleReceiveVO) vo).getHead();
			SaleReceiveBVO[] items = ((SaleReceiveVO) vo).getItems();

			String pk_corp = head.getPk_corp();
			int len = items.length;

			// 使用临时表关联存货管理档案
			ArrayList values = new ArrayList();
			for (int i = 0; i < len; i++) {
				ArrayList listTmp = new ArrayList();
				listTmp.add(i);
				listTmp.add(pk_corp);
				listTmp.add(items[i].getCinvbasdocid());
				values.add(listTmp);
			}

			String table_tmp = new TempTableDMO().getTempStringTable("t_so_exambatchinv", new String[] { "pk", "corp",
					"invbasid" }, new String[] { "int", "char(4)", "char(20)" }, "pk", values);

			String querySql = "select wholemanaflag from " + table_tmp
					+ ", bd_invmandoc where dr = 0 and corp = pk_corp and invbasid = pk_invbasdoc order by pk ";

			Object[] objs = new SmartDMO().selectBy2(querySql);

			ArrayList<String> al_crowno = new ArrayList();
			for (int i = 0; i < len; i++) {
				/** 如果是批次管理 但是没有录入批次* */
				if ((((UFBoolean) ((Object[]) objs[i])[0]).booleanValue()) && (items[i].getVbatchcode() == null)) {
					al_crowno.add(items[i].getCrowno());
				}
			}

			if (al_crowno.size() > 0){
				StringBuffer sbf = new StringBuffer("行号为：");
				for (Iterator iter = al_crowno.iterator(); iter.hasNext();) {
					sbf.append((String) iter.next());
					if (iter.hasNext())
						sbf.append("、");
				} 
				sbf.append("的存货是批次管理，但是没有录入批次号！");
				throw new BusinessException(sbf.toString());
			}else{
				return SoVoConst.buftrue;
			}

		} catch (Exception e) {
			handleException(e);
		}

		return SoVoConst.buffalse;
	}
	
	protected boolean isProdEnable(String pk_corp, String module) throws BusinessException {
		try {
			ICreateCorpQueryService icorp = (ICreateCorpQueryService) NCLocator.getInstance().lookup(
					ICreateCorpQueryService.class.getName());
			Hashtable ht = icorp.queryProductEnabled(pk_corp, new String[] { module });
			return ((UFBoolean) ht.get(module)).booleanValue();
		} catch (Exception e) {
			handleException(e);
		}

		return false;
	}

	protected void handleException(Exception e) throws BusinessException {
		//e.printStackTrace();
		SCMEnv.out(e);
		if (e instanceof BusinessException)
			throw (BusinessException) e;
		else
			throw new BusinessException(e);

	}
	
	/** ========================发货单检查函数==============================* */
	
}
