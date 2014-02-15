package nc.impl.scm.so.pub;

/**
 * ����״̬����
 * �������ڣ�(2001-9-19 13:54:02)
 * @author������
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
	 * CheckStatusDMO ������ע�⡣
	 * 
	 * @exception javax.naming.NamingException
	 *                �쳣˵����
	 * @exception nc.bs.pub.SystemException
	 *                �쳣˵����
	 */
	public CheckApproveDMO() throws javax.naming.NamingException, nc.bs.pub.SystemException {
		super();
	}

	/**
	 * CheckStatusDMO ������ע�⡣
	 * 
	 * @param dbName
	 *            java.lang.String
	 * @exception javax.naming.NamingException
	 *                �쳣˵����
	 * @exception nc.bs.pub.SystemException
	 *                �쳣˵����
	 */
	public CheckApproveDMO(String dbName) throws javax.naming.NamingException, nc.bs.pub.SystemException {
		super(dbName);
	}

	/**
	 * ����Ƿ��Ѿ������� �������ڣ�(2014-01-15)
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
	 * ���ԭʼ������ �������ڣ�(2001-9-3 9:30:24)
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

		// ����
		if (strBillType.equals(SaleBillType.SaleOrder)) {
			strTable = " so_saleorder_b ";
			strField = "corder_bid";
		}
		// ��Ʊ
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
	 * ���ԭʼ������ �������ڣ�(2001-9-3 9:30:24)
	 * 
	 * @return nc.vo.pub.lang.UFDouble
	 */
	private UFDouble getOldNumber(String strBillType, String strID) throws SQLException {
		UFDouble nnumber = new UFDouble(0.0);
		String strSQL = "Select nnumber From ";
		String strField = null;

		// ����
		if (strBillType.equals(SaleBillType.SaleOrder)) {
			strSQL = strSQL + " so_saleorder_b ";
			strField = "corder_bid";
		}
		// ��Ʊ
		if (strBillType.equals(SaleBillType.SaleInvoice)) {
			strSQL = strSQL + " so_saleinvoice_b ";
			strField = "cinvoice_bid";
		}

		/** v5.5������ԭ���߼������� */
		// ������
		/*
		 * if (strBillType.equals(SaleBillType.SaleReceipt)){ strSQL = strSQL + "
		 * so_salereceipt_b "; strField = "creceipt_bid"; }
		 */

		// ���ⵥ
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
	 * ������۶����Ƿ�����ȡ���� �������ڣ�(2001-9-18 11:36:22)
	 * 
	 * @return boolean
	 * @param ���ݸ���ID:ReceiptDetailID
	 *            java.lang.StringBuffer
	 * @param ��������ID:ReceiptID
	 *            java.lang.StringBuffer
	 */
	public void isInitOutAppRequst(AggregatedValueObject billVO) throws SQLException, nc.vo.pub.BusinessException {

		nc.vo.scm.pub.SCMEnv.out("ִ��isReceiptAppRequst");

		for (int i = 0; i < billVO.getChildrenVO().length; i++) {
			CircularlyAccessibleValueObject bodyVO = billVO.getChildrenVO()[i];
			// ���۷�Ʊ������������ID���ӱ�ID����Ʊ����
			if (bodyVO.getStatus() != VOStatus.DELETED)
				vinitOutAppOrder(bodyVO.getAttributeValue("csourcebillid").toString(), bodyVO.getAttributeValue(
						"csourcebillbodyid").toString(), bodyVO.getPrimaryKey(), (UFDouble) bodyVO
						.getAttributeValue("nnumber"), bodyVO.getStatus());

		}

	}

	/**
	 * ������۶����Ƿ�����ȡ���� �������ڣ�(2001-9-18 11:36:22)
	 * 
	 * @return boolean
	 * @param ���ݸ���ID:ReceiptDetailID
	 *            java.lang.StringBuffer
	 * @param ��������ID:ReceiptID
	 *            java.lang.StringBuffer
	 */
	public void isInvoiceAppRequst(AggregatedValueObject billVO) throws SQLException, nc.vo.pub.BusinessException {

		nc.vo.scm.pub.SCMEnv.out("ִ��isInvoiceAppRequst");

		for (CircularlyAccessibleValueObject bodyVO:billVO.getChildrenVO()) {
			// ���۷�Ʊ������������ID���ӱ�ID����Ʊ����
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
	 * ������۶����޶��� �������ڣ�(2001-9-18 11:36:22)
	 * 
	 * @return boolean
	 * @param ���ݸ���ID:ReceiptDetailID
	 *            java.lang.StringBuffer
	 * @param ��������ID:ReceiptID
	 *            java.lang.StringBuffer
	 */
	public void isOrderAlterRequst(AggregatedValueObject billVO) throws SQLException, nc.vo.pub.BusinessException {

		nc.vo.scm.pub.SCMEnv.out("ִ��isOrderAlterRequst");

		for (int i = 0; i < billVO.getChildrenVO().length; i++) {
			CircularlyAccessibleValueObject bodyVO = billVO.getChildrenVO()[i];
			// ���۳��ⵥ������������ID���ӱ�ID����Ʊ����
			if (bodyVO.getAttributeValue("csaleid") != null && bodyVO.getAttributeValue("corder_bid") != null)
				vOrderAlterOrder(bodyVO.getAttributeValue("csaleid").toString(), bodyVO.getAttributeValue("corder_bid")
						.toString(), (UFDouble) bodyVO.getAttributeValue("nnumber"));

		}

	}

	/**
	 * ������۳��ⵥ�� �������ڣ�(2001-9-18 11:36:22)
	 * 
	 * @return boolean
	 * @param ���ݸ���ID:ReceiptDetailID
	 *            java.lang.StringBuffer
	 * @param ��������ID:ReceiptID
	 *            java.lang.StringBuffer
	 */
	public void isOutAppRequst(AggregatedValueObject billVO) throws SQLException, nc.vo.pub.BusinessException {

		nc.vo.scm.pub.SCMEnv.out("ִ��isReceiptAppRequst");
		if (billVO == null || billVO.getChildrenVO() == null || billVO.getChildrenVO().length <= 0)
			return;

		String[] corderbids = nc.vo.so.so016.SoVoTools.getVOsOnlyValues(billVO.getChildrenVO(), "cfirstbillbid");

		if (corderbids == null || corderbids.length <= 0)
			return;

		// �ۼƳ����������Ѿ��������� + ���γ�������
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

			// �ۼƳ����������Ѿ��������� + ���γ�������
			if (dblNumber == null)
				dblNumber = uf0;

			dblNumber = dblNumber.sub(oldnnumber).add(nReceiptNum);

			if (dblNumber1.floatValue() > 0) {

				if (dblNumber.floatValue() < 0)
					throw new nc.vo.pub.BusinessException(nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("sopub",
							"UPPsopub-000000")/* @res "�ۼƳ�����������С���㡣" */);

			} else {

				if (dblNumber.floatValue() > 0)
					throw new nc.vo.pub.BusinessException(nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("sopub",
							"UPPsopub-000001")/* @res "�ۼƳ����������ܴ����㡣" */);

			}

			// ���۳��ⵥ������������ID���ӱ�ID����Ʊ����
			// if(bodyVO.getAttributeValue("cfirstbillhid") != null &&
			// bodyVO.getAttributeValue("cfirstbillbid") != null)
			// if(bodyVO.getStatus() != VOStatus.DELETED)
			// vOutAppOrder(bodyVO.getAttributeValue("cfirstbillhid").toString(),bodyVO.getAttributeValue("cfirstbillbid").toString(),bodyVO.getPrimaryKey(),(UFDouble)bodyVO.getAttributeValue("noutnum"),bodyVO.getStatus());

		}

	}

	/**
	 * ������۶����Ƿ�����ȡ���� �������ڣ�(2001-9-18 11:36:22)
	 * 
	 * @return boolean
	 * @param ���ݸ���ID:ReceiptDetailID
	 *            java.lang.StringBuffer
	 * @param ��������ID:ReceiptID
	 *            java.lang.StringBuffer
	 */
	public void isReceiptAppRequst(AggregatedValueObject billVO) throws SQLException, nc.vo.pub.BusinessException {

		nc.vo.scm.pub.SCMEnv.out("ִ��isReceiptAppRequst");

		for (int i = 0; i < billVO.getChildrenVO().length; i++) {
			CircularlyAccessibleValueObject bodyVO = billVO.getChildrenVO()[i];
			// ���۷�Ʊ������������ID���ӱ�ID����Ʊ����

			if (bodyVO.getStatus() != VOStatus.DELETED)
				vReceiptAppOrder(bodyVO.getAttributeValue("csourcebillid").toString(), bodyVO.getAttributeValue(
						"csourcebillbodyid").toString(), bodyVO.getPrimaryKey(), (UFDouble) bodyVO
						.getAttributeValue("nnumber"), bodyVO.getStatus());

		}

	}

	/**
	 * ����ڳ�������������� �������ڣ�(2001-9-18 11:36:22)
	 * 
	 * @return boolean
	 * @param ��ض�������ID:ReceiptDetailID
	 * @param ��ض�������ID:ReceiptID
	 * @param ��������:nReceiptNum
	 */
	private void vinitOutAppOrder(String SaleID, String SaleDetailID, String ReceiptDetailID, UFDouble nReceiptNum,
			int state) throws SQLException, nc.vo.pub.BusinessException {

		nc.vo.scm.pub.SCMEnv.out("ִ��vinitOutAppOrder");

		// ԭ����
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

		// ����
		SQLRowStatus = SQLRowStatus + " where so_saleorder_b.csaleid ='" + SaleID + "' ";
		SQLRowStatus = SQLRowStatus + " and so_saleorder_b.corder_bid ='" + SaleDetailID + "' ";

		// nReceiptNum �������Ѿ��������� - �������� + ���η�������

		Connection con = null;
		PreparedStatement stmt = null;

		String bResultMsg = null;

		BigDecimal dblNumber = new BigDecimal(0);

		try {
			con = getConnection();
			stmt = con.prepareStatement(SQLRowStatus);

			ResultSet rstNumber = stmt.executeQuery();
			// �ж��Ƿ�Ϊ�ռ�¼
			if (rstNumber.next()) {
				// �ж��Ƿ�Ϊ��
				// �жϷ�������
				// ȡ��������
				Object o = rstNumber.getObject("nReceiptNum");

				if (o != null)
					dblNumber = new BigDecimal(o.toString());
				if (dblNumber.floatValue() > 0)

					bResultMsg = nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("sopub", "UPPsopub-000002")/*
																												 * @res
																												 * "�����������ܴ��ڶ���������"
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
	 * ��鶩����Ʊ��������� �������ڣ�(2001-9-18 11:36:22)
	 * 
	 * @return boolean
	 * @param ��ض�������ID:ReceiptDetailID
	 * @param ��ض�������ID:ReceiptID
	 * @param ��Ʊ����:nInvoiceNum
	 */
	private void vInvoiceAppOrder(String billType, String SaleID, String SaleDetailID, String ReceiptID,
			String ReceiptDetailID, UFDouble nInvoiceNum, int state) throws SQLException, nc.vo.pub.BusinessException {

		nc.vo.scm.pub.SCMEnv.out("ִ��vInvoiceAppOrder");

		// ԭ����
		UFDouble oldnnumber = new UFDouble(0.0);

		if (hNumber == null)
			hNumber = getOldData(nc.ui.scm.so.SaleBillType.SaleInvoice, ReceiptID);

		if (state == VOStatus.UPDATED) {
			// oldnnumber =
			// getOldNumber(nc.ui.scm.so.SaleBillType.SaleInvoice,ReceiptDetailID);
			//2009-12-16 fengjb zhangcheng fangchan ���۷�Ʊ��ʽ�����ʱ��vo״̬�Ǵ�ģ��������ʱ��
			//ReceiptDetailIDΪ�գ�Ϊ�����ָ�쳣�ж���ReceiptDetailID����
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

			// ����
			SQLRowStatus = SQLRowStatus + " where so_saleorder_b.csaleid ='" + SaleID + "' ";
			SQLRowStatus = SQLRowStatus + " and so_saleorder_b.corder_bid ='" + SaleDetailID + "' ";
			SQLRowStatus = SQLRowStatus + " and so_saleexecute.csaleid ='" + SaleID + "' ";
			SQLRowStatus = SQLRowStatus + " and so_saleexecute.csale_bid ='" + SaleDetailID + "' ";

			errMSG = nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("sopub", "UPPsopub-000003")/*
																									 * @res
																									 * "��Ʊ�������ܴ��ڶ���������"
																									 */;
		} else {

			// ���۳��ⵥ
			SQLRowStatus = "select isnull(ic_general_bb3.nsignnum,0)-ic_general_b.noutnum -(" + oldnnumber + ")+("
					+ nInvoiceNum + ")";
			SQLRowStatus = SQLRowStatus
					+ " nInvoiceNum ,ic_general_b.noutnum as nnumber,isnull(ic_general_bb3.nsignnum,0)-("
					+ oldnnumber
					+ ") ntotalinvoicenumber FROM ic_general_h INNER JOIN    ic_general_b ON ic_general_h.cgeneralhid = ic_general_b.cgeneralhid INNER JOIN ";
			SQLRowStatus = SQLRowStatus
					+ " ic_general_bb3 ON ic_general_h.cgeneralhid = ic_general_bb3.cgeneralhid AND   ic_general_b.cgeneralbid = ic_general_bb3.cgeneralbid  ";

			// ����
			SQLRowStatus = SQLRowStatus + " where ic_general_b.cgeneralhid ='" + SaleID + "' ";
			SQLRowStatus = SQLRowStatus + " and ic_general_b.cgeneralbid ='" + SaleDetailID + "' ";
			// ����
			SQLRowStatus = SQLRowStatus + " and ic_general_bb3.cgeneralhid ='" + SaleID + "' ";
			SQLRowStatus = SQLRowStatus + " and ic_general_bb3.cgeneralbid ='" + SaleDetailID + "' ";

			errMSG = nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("sopub", "UPPsopub-000004")/*
																									 * @res
																									 * "��Ʊ�������ܴ���ʵ�ʳ���������"
																									 */;
		}
		// nInvoiceNum �������Ѿ���Ʊ���� - �������� + ���ο�Ʊ����

		Connection con = null;
		PreparedStatement stmt = null;

		BigDecimal dblNumber = new BigDecimal(0);
		BigDecimal dblNumber1 = new BigDecimal(0);
		BigDecimal dblNumber2 = new BigDecimal(0);

		try {
			con = getConnection();
			stmt = con.prepareStatement(SQLRowStatus);

			ResultSet rstNumber = stmt.executeQuery();
			// �ж��Ƿ�Ϊ�ռ�¼
			if (rstNumber.next()) {
				// �ж��Ƿ�Ϊ��
				// �жϿ�Ʊ����
				// ȡ��Ʊ����
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
																			 * "���������ܴ����ѿ�Ʊ������"
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
	 * ��鶩���޶���������� �������ڣ�(2001-9-18 11:36:22)
	 * 
	 * @return boolean
	 * @param ��ض�������ID:ReceiptDetailID
	 * @param ��ض�������ID:ReceiptID
	 * @param ��Ʊ����:nInvoiceNum
	 */
	private void vOrderAlterOrder(String SaleID, String SaleDetailID, UFDouble nInvoiceNum) throws SQLException,
			nc.vo.pub.BusinessException {

		nc.vo.scm.pub.SCMEnv.out("ִ��vOrderAlterOrder");

		String SQLRowStatus = null;
		String errMSG = null;
		String bResultMsg = null;

		SQLRowStatus = "select nnumber,isnull(ntotalinvoicenumber,0) ntotalinvoicenumber,isnull(ntotalreceivenumber,0) ntotalreceivenumber,isnull(ntotalinventorynumber,0) ntotalinventorynumber ";
		SQLRowStatus = SQLRowStatus
				+ " from  so_saleorder_b left outer  join so_saleexecute on so_saleorder_b.csaleid = so_saleexecute.csaleid ";
		SQLRowStatus = SQLRowStatus + " and so_saleorder_b.corder_bid = so_saleexecute.csale_bid ";

		// ����
		SQLRowStatus = SQLRowStatus + " where so_saleorder_b.csaleid ='" + SaleID + "' ";
		SQLRowStatus = SQLRowStatus + " and so_saleorder_b.corder_bid ='" + SaleDetailID + "' ";

		errMSG = nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("sopub", "UPPsopub-000006")/*
																								 * @res
																								 * "�޶���������С���ѷ���������"
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
			// �ж��Ƿ�Ϊ�ռ�¼
			if (rstNumber.next()) {
				// �ж��Ƿ�Ϊ��
				// �жϿ�Ʊ����
				// ȡ��Ʊ����
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
																													 * "�޶�����������ԭ���������෴��"
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
																													 * "�޶�����������ԭ���������෴��"
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
	 * ��鶩��������������� �������ڣ�(2001-9-18 11:36:22)
	 * 
	 * @return boolean
	 * @param ��ض�������ID:ReceiptDetailID
	 * @param ��ض�������ID:ReceiptID
	 * @param ��������:nReceiptNum
	 */
	private void vReceiptAppOrder(String SaleID, String SaleDetailID, String ReceiptDetailID, UFDouble nReceiptNum,
			int state) throws SQLException, nc.vo.pub.BusinessException {

		nc.vo.scm.pub.SCMEnv.out("ִ��vReceiptAppOrder");

		// ԭ����
		UFDouble oldnnumber = new UFDouble(0.0);
		/** v5.5������ԭ���߼������� */
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

		// ����
		SQLRowStatus = SQLRowStatus + " where so_saleorder_b.csaleid ='" + SaleID + "' ";
		SQLRowStatus = SQLRowStatus + " and so_saleorder_b.corder_bid ='" + SaleDetailID + "' ";

		// nReceiptNum �������Ѿ��������� - �������� + ���η�������

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
			// �ж��Ƿ�Ϊ�ռ�¼
			if (rstNumber.next()) {
				// �ж��Ƿ�Ϊ��
				// �жϷ�������
				// ȡ��������

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
									.getStrByID("sopub", "UPPsopub-000008")/*@res "���������ܴ����ѷ���������"*/;
					} else {
						if (dblNumber.floatValue() > 0)
							bResultMsg = nc.bs.ml.NCLangResOnserver.getInstance()
									.getStrByID("sopub", "UPPsopub-000009")/*@res "�����������ܴ��ڶ���������"*/;
					}
				} else

				if (dblNumber.floatValue() < 0 || nReceiptNum.doubleValue() > 0)

					bResultMsg = nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("sopub", "UPPsopub-000009")/*@res "�����������ܴ��ڶ���������"*/;

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