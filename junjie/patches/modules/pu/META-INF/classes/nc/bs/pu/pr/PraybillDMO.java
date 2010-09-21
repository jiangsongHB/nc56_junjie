package nc.bs.pu.pr;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Vector;

import javax.naming.NamingException;

import nc.bs.pu.pub.PubDMO;
import nc.bs.pub.SystemException;
import nc.bs.scm.pub.BillRowNoDMO;
import nc.bs.scm.pub.smart.SmartDMO;
import nc.itf.pu.inter.IPuToSo_PraybillDMO;
import nc.vo.po.OrderCloseItemVO;
import nc.vo.pr.pray.PraybillHeaderVO;
import nc.vo.pr.pray.PraybillItemVO;
import nc.vo.pr.pray.PraybillVO;
import nc.vo.pu.pr.PrayExecVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CommonConstant;
import nc.vo.pub.VOStatus;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pu.BillStatus;
import nc.vo.scm.pub.SCMEnv;
import nc.vo.scm.pub.smart.SmartVO;

/**
 * 此处插入类型说明。
 * 版本号:
 * 功能描述:
 * 作者:熊海情
 * 创建日期:
 * 修改人:
 * 修改日期:
 * 修改原因:
 */
public class PraybillDMO extends nc.bs.pub.DataManageObject implements IPuToSo_PraybillDMO {
/**
 * PraybillDMO 构造子注解。
 * @exception javax.naming.NamingException 异常说明。
 * @exception nc.bs.pub.SystemException 异常说明。
 */
public PraybillDMO() throws javax.naming.NamingException, nc.bs.pub.SystemException {
	super();
}
/**
 * PraybillDMO 构造子注解。
 * @param dbName java.lang.String
 * @exception javax.naming.NamingException 异常说明。
 * @exception nc.bs.pub.SystemException 异常说明。
 */
public PraybillDMO(String dbName) throws javax.naming.NamingException, nc.bs.pub.SystemException {
	super(dbName);
}
/**
 * 此处插入方法说明。
 * 功能描述:判断请购单的单据号是否重复
 * 输入参数:请购单VO
 * 返回值:重复，返回FALSE；否则，返回TRUE
 * 异常处理:无
 * @throws SystemException 
 */
private UFBoolean checkBeforeSave(PraybillVO VO)
	throws
		javax.naming.NamingException,
		java.sql.SQLException,
		BusinessException, SystemException {
	nc.bs.pr.pray.PraybillDMO dmo = new nc.bs.pr.pray.PraybillDMO();
	boolean b = false;
	PraybillHeaderVO headVO = VO.getHeadVO();
	String sUnitCode = headVO.getPk_corp();
	String vBillCode = headVO.getVpraycode();
	String sKey = headVO.getCpraybillid();
	b = dmo.isCodeDuplicate(sUnitCode, vBillCode, sKey);

	return new UFBoolean(!b);
}
/**
 * 此处插入方法说明。
 * 功能描述:保存,专为物料需求等系统调用
 * 输入参数:请购单VO
 * 返回值:主键集合（表头+增行表体）
 * 异常处理:无
 */
public ArrayList doSaveSpecially(PraybillVO VO) throws Exception {

	PraybillHeaderVO headVO = VO.getHeadVO();
	PraybillItemVO bodyVO[] = VO.getBodyVO();

	//检查
	if (bodyVO.length < 1) {
		throw new BusinessException(nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("40040101","UPP40040101-000036")/*@res "请购单表体为空，不能保存！"*/);
	}

	try {
		VO.validate();
	} catch (nc.vo.pub.ValidationException e) {
		SCMEnv.out(e);
		throw new BusinessException(nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("40040101","UPP40040101-000520")/*@res "未通过请购单合法性检查!"*/);
	}
	boolean b = false;
	for (int i = 0; i < bodyVO.length; i++) {
		UFDouble d1 = bodyVO[i].getNpraynum();
		UFDouble d2 = bodyVO[i].getNsuggestprice();
		UFDouble d3 = bodyVO[i].getNmoney();
		if (d1 != null && d1.doubleValue() < 0.0) {
			b = true;
			break;
		}
		if (d2 != null && d2.doubleValue() < 0.0) {
			b = true;
			break;
		}
		if (d3 != null && d3.doubleValue() < 0.0) {
			b = true;
			break;
		}
	}
	if (b) {
		throw new BusinessException(nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("40040101","UPP40040101-000513")/*@res "数量，单价，金额为负，不能保存！"*/);
	}

	if (!checkBeforeSave(VO).booleanValue()) {
		throw new BusinessException(nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("40040101","UPP40040101-000521")/*@res "请购单号不唯一！不能保存"*/);
	}

	if (!isPsnBelongDept(VO).booleanValue()) {
		throw new BusinessException(nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("40040101","UPP40040101-000517")/*@res "请购人不属于请购部门，不能保存！"*/);
	}

	//检查通过，加单据行号
	BillRowNoDMO.setVORowNoByRule(VO,nc.vo.scm.pu.BillTypeConst.PO_PRAY,"crowno");
	//检查通过，保存新增加的请购单
	nc.bs.pr.pray.PraybillDMO dmo = new nc.bs.pr.pray.PraybillDMO();
	ArrayList arrayList = new ArrayList();
	UFBoolean bTemp[] = dmo.isInvBelongStoreOrg(VO);

	for (int i = 0; i < bTemp.length; i++) {
		if (!bTemp[i].booleanValue()) {
			throw new BusinessException(								
					 nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("40040101","UPP40040101-000518",null,new String[]{String.valueOf(i+1)})/*@res "行存货和库存组织不匹配"*/);
		}
	}

	String key = dmo.insertHead(headVO);
	arrayList.add(key);

	String key0[] = dmo.insertBody(bodyVO, key);
	for (int i = 0; i < bodyVO.length; i++) {
		arrayList.add(key0[i]);
	}
	return arrayList;
}
/**
 * 功能描述：:保存,专为物料需求等系统调用
 * 输入参数：请购单VO[]
 * 返 回 值：ArrayList ,内容为 ArrayList = 主键集合（表头+增行表体）
 * 异常处理：BusinessException, SQLException
 * 修 改 人：晁志平
 * 修改说明：物料需求系统的新需求，批量保存
 */
public ArrayList doSaveSpeciallyBatch(PraybillVO[] VOs) throws Exception {

	//不合理的数据
	if (VOs == null || VOs.length <= 0)
		return null;
	PraybillVO VO = null;
	PraybillHeaderVO headVO = null;
	PraybillItemVO bodyVO[] = null;
	UFDouble d1 = null;
	UFDouble d2 = null;
	UFDouble d3 = null;

	//合法性检查检查
	for (int k = 0; k < VOs.length; k++) {
		VO = VOs[k];
		//不合理的数据
		if (VO == null || VO.getHeadVO() == null)
			return null;
		headVO = VO.getHeadVO();
		bodyVO = VO.getBodyVO();
		if (bodyVO.length < 1) {
			throw new BusinessException(nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("40040101","UPP40040101-000036")/*@res "请购单表体为空，不能保存！"*/);
		}
		try {
			VO.validate();
		} catch (nc.vo.pub.ValidationException e) {
			SCMEnv.out(e);
			throw new BusinessException(
				nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("40040101","UPP40040101-000522")/*@res "请购单合法性检查错误：\n"*/
					+ nc.vo.pub.CommonConstant.BEGIN_MARK
					+ e.getMessage()
					+ CommonConstant.END_MARK);
		}
		boolean b = false;
		for (int i = 0; i < bodyVO.length; i++) {
			d1 = bodyVO[i].getNpraynum();
			d2 = bodyVO[i].getNsuggestprice();
			d3 = bodyVO[i].getNmoney();
			if (d1 != null && d1.doubleValue() < 0.0) {
				b = true;
				break;
			}
			if (d2 != null && d2.doubleValue() < 0.0) {
				b = true;
				break;
			}
			if (d3 != null && d3.doubleValue() < 0.0) {
				b = true;
				break;
			}
		}
		if (b) {
			throw new BusinessException(nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("40040101","UPP40040101-000513")/*@res "数量，单价，金额为负，不能保存！"*/);
		}
		if (!checkBeforeSave(VO).booleanValue()) {
			throw new BusinessException(nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("40040101","UPP40040101-000515")/*@res "请购单号不唯一，不能保存！"*/);
		}
		if (!isPsnBelongDept(VO).booleanValue()) {
			throw new BusinessException(nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("40040101","UPP40040101-000517")/*@res "请购人不属于请购部门，不能保存！"*/);
		}
	}

	//检查通过，加单据行号
	BillRowNoDMO.setVOsRowNoByRule(VOs,nc.vo.scm.pu.BillTypeConst.PO_PRAY,"crowno");
	//检查通过，保存新增加的请购单
	nc.bs.pr.pray.PraybillDMO dmo = new nc.bs.pr.pray.PraybillDMO();
	ArrayList arrayRslt = new ArrayList();
	ArrayList arrayList = new ArrayList();
	UFBoolean bTemp[] = null;
	String key = null;
	String key0[] = null;

	for (int k = 0; k < VOs.length; k++) {
		VO = VOs[k];
		bTemp = dmo.isInvBelongStoreOrg(VO);
		for (int i = 0; i < bTemp.length; i++) {
			if (!bTemp[i].booleanValue()) {
				throw new BusinessException(
					nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("40040101","UPP40040101-000519")/*@res "存货库存组织不匹配：\n单据位置（序号）："*/
						+ nc.vo.pub.CommonConstant.BEGIN_MARK
						+ (k + 1)
						+ nc.vo.pub.CommonConstant.END_MARK
						+ nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("40040101","UPP40040101-000523")/*@res "\n存货位置（序号）："*/
						+ nc.vo.pub.CommonConstant.BEGIN_MARK
						+ (i + 1)
						+ nc.vo.pub.CommonConstant.END_MARK);
			}
		}
		key = dmo.insertHead(headVO);
		arrayList.add(key);
		key0 = dmo.insertBody(bodyVO, key);
		for (int i = 0; i < bodyVO.length; i++) {
			arrayList.add(key0[i]);
		}
		arrayRslt.add(arrayList);
	}

	return arrayRslt;
}
/**
 * 此处插入方法说明。
 *  功能描述:获得采购提前期  (为销售组提供)
 *  输入参数:公司编码，库存组织主键，存货基础主键，请购数量
 *  返回值:
 *  异常处理:
 */
public int getAdvanceDays(
	String sUnitCode,
	String sStoOrgId,
	String sBaseId,
	UFDouble nPrayNum)
	throws BusinessException {
	/*************************************************************/
	// 保留的系统管理接口：
	beforeCallMethod(
		"nc.bs.pu.pr.PraybillDMO",
		"getAdvanceDays",
		new Object[] { sUnitCode, sStoOrgId, sBaseId, nPrayNum });
	/*************************************************************/

	String sql = "";
	if (sUnitCode != null) {
		sql =
			"select fixedahead, aheadcoff, aheadbatch from bd_produce where pk_corp = ? and pk_calbody = ? and pk_invbasdoc = ?";
	} else {
		sql =
			"select fixedahead, aheadcoff, aheadbatch from bd_produce where pk_calbody = ? and pk_invbasdoc = ?";
	}

	UFDouble nFixedAhead = new UFDouble(0.0);
	UFDouble nAheadCoff = new UFDouble(0.0);
	UFDouble nAheadBatch = new UFDouble(0.0);

	Connection con = null;
	PreparedStatement stmt = null;
	try {
		con = getConnection();
		stmt = con.prepareStatement(sql);
		if (sUnitCode != null) {
			stmt.setString(1, sUnitCode);
			stmt.setString(2, sStoOrgId);
			stmt.setString(3, sBaseId);
		} else {
			stmt.setString(1, sStoOrgId);
			stmt.setString(2, sBaseId);
		}
		ResultSet rs = stmt.executeQuery();
		//
		if (rs.next()) {
			Object d1 = rs.getObject(1);
			if (d1 != null && d1.toString().trim().length() > 0)
				nFixedAhead = new UFDouble(d1.toString().trim());

			Object d2 = rs.getObject(2);
			if (d2 != null && d1.toString().trim().length() > 0)
				nAheadCoff = new UFDouble(d2.toString().trim());

			Object d3 = rs.getObject(3);
			if (d3 != null && d3.toString().trim().length() > 0)
				nAheadBatch = new UFDouble(d3.toString().trim());
		}
	}catch(SQLException e){
		PubDMO.throwBusinessException(e);
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

	/*************************************************************/
	// 保留的系统管理接口：
	afterCallMethod(
		"nc.bs.pu.pr.PraybillDMO",
		"getAdvanceDays",
		new Object[] { sUnitCode, sStoOrgId, sBaseId, nPrayNum });
	/*************************************************************/

	if (nPrayNum != null
		&& nFixedAhead != null
		&& nAheadCoff != null
		&& nAheadBatch != null
		&& nAheadBatch.doubleValue() != 0.0) {
		double d1 = nPrayNum.doubleValue();
		double d2 = nFixedAhead.doubleValue();
		double d3 = nAheadCoff.doubleValue();
		double d4 = nAheadBatch.doubleValue();
		if (d1 > d4) {
			double dd = d2 + (d1 - d4) * d3 / d4;
			int k = (int) dd;
			if (dd - k > 0)
				k++;
			return k;
		} else
			return (int) d2;
	} else
		return 0;

}
/**
 * 此处插入方法说明。
 *  功能描述:获得采购提前期
 *  输入参数:[公司编码]，[库存组织主键]，[存货基础主键]，[请购数量]
 *  返回值:[提前天数]
 *  异常处理:
 */
public int[] getAdvanceDaysBatch(
	String sUnitCode[],
	String saStoOrgId[],
	String saBaseId[],
	UFDouble nPrayNum[])
	throws SQLException, BusinessException {
	/*************************************************************/
	// 保留的系统管理接口：
	beforeCallMethod(
		"nc.bs.pu.pr.PraybillDMO",
		"getAdvanceDaysBatch",
		new Object[] { sUnitCode, saStoOrgId, saBaseId, nPrayNum });
	/*************************************************************/

	if (sUnitCode == null || sUnitCode.length == 0)
		throw new BusinessException(
			"getAdvanceDaysBatch Exception!",
			new Exception(nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("40040101","UPP40040101-000524")/*@res "公司为空！"*/));
	if (saStoOrgId == null || saStoOrgId.length == 0)
		throw new BusinessException(
			"getAdvanceDaysBatch Exception!",
			new Exception(nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("40040101","UPP40040101-000525")/*@res "库存组织为空！"*/));
	if (saBaseId == null || saBaseId.length == 0)
		throw new BusinessException(
			"getAdvanceDaysBatch Exception!",
			new Exception(nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("40040101","UPP40040101-000526")/*@res "存货为空！"*/));
	if (nPrayNum == null || nPrayNum.length == 0)
		throw new BusinessException(
			"getAdvanceDaysBatch Exception!",
			new Exception(nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("40040101","UPP40040101-000527")/*@res "请购数量为空！"*/));
	if (!(sUnitCode.length == saStoOrgId.length
		&& sUnitCode.length == saBaseId.length
		&& sUnitCode.length == nPrayNum.length))
		throw new BusinessException(
			"getAdvanceDaysBatch Exception!",
			new Exception(nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("40040101","UPP40040101-000528")/*@res "公司、库存组织、存货和请购数量数组大小不同！"*/));

	int nDays[] = new int[sUnitCode.length];
	Connection con = null;
	PreparedStatement stmt = null;
	ResultSet rs = null;
	try {
		con = getConnection();
		String sql =
			"select fixedahead, aheadcoff, aheadbatch from bd_produce where pk_corp = ? and pk_calbody = ? and pk_invbasdoc = ?";
		stmt = con.prepareStatement(sql);

		for (int i = 0; i < sUnitCode.length; i++) {
			stmt.setString(1, sUnitCode[i]);
			stmt.setString(2, saStoOrgId[i]);
			stmt.setString(3, saBaseId[i]);

			rs = stmt.executeQuery();
			//
			UFDouble nFixedAhead = new UFDouble(0.0);
			UFDouble nAheadCoff = new UFDouble(0.0);
			UFDouble nAheadBatch = new UFDouble(0.0);

			if (rs.next()) {
				Object d1 = rs.getObject(1);
				if (d1 != null && d1.toString().trim().length() > 0)
					nFixedAhead = new UFDouble(d1.toString().trim());

				Object d2 = rs.getObject(2);
				if (d2 != null && d1.toString().trim().length() > 0)
					nAheadCoff = new UFDouble(d2.toString().trim());

				Object d3 = rs.getObject(3);
				if (d3 != null && d3.toString().trim().length() > 0)
					nAheadBatch = new UFDouble(d3.toString().trim());
			}
			rs.close();
			//计算采购提前期
			int k = 0;
			if (nPrayNum[i] != null
				&& nFixedAhead != null
				&& nAheadCoff != null
				&& nAheadBatch != null
				&& nAheadBatch.doubleValue() != 0.0) {
				double d1 = nPrayNum[i].doubleValue();
				double d2 = nFixedAhead.doubleValue();
				double d3 = nAheadCoff.doubleValue();
				double d4 = nAheadBatch.doubleValue();
				if (d1 > d4) {
					double dd = d2 + (d1 - d4) * d3 / d4;
					k = (int) dd;
					if (dd - k > 0)
						k++;
				} else {
					k = (int) d2;
				}
			}

			nDays[i] = k;
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

	/*************************************************************/
	// 保留的系统管理接口：
	afterCallMethod(
		"nc.bs.pu.pr.PraybillDMO",
		"getAdvanceDaysBatch",
		new Object[] { sUnitCode, saStoOrgId, saBaseId, nPrayNum });
	/*************************************************************/

	return nDays;
}
/**
 * 此处插入方法说明。
 * 功能描述:请购执行A（取需求日期在开始日期以后的非关闭作废的请购单）提供主计量单位的数量
 *   已完成数量指相关已审批订单的采购订单数量
 * 输入参数:
 * 返回值:
 * 异常处理:
 */
public PrayExecVO[] getPrayExecA(
	String pk_corp,
	String pk_calbody,
	String pk_invbasdoc,
	UFDate sdate)
	throws java.sql.SQLException {
	/*************************************************************/
	// 保留的系统管理接口：
	beforeCallMethod(
		"nc.bs.pu.pr.PraybillDMO",
		"getPrayExecA",
		new Object[] { pk_corp, pk_calbody, pk_invbasdoc, sdate });
	/*************************************************************/
	//String pk_corp;//公司PK
	//String gcbmid;//库存组织PK
	//String wlbmid;//存货基本ID
	//String pk_invmandoc;//存货管理ID
	//UFDate xqrq;//需求日期
	//UFDouble xqsl;//数量
	//UFDouble wcsl;//已完成数量
	//String jldwid;//计量单位ID(主计量)
	//String lyid;//请购单主表ID
	//String fbid;// 请购单附表ID
	//String lydjh;// 请购单号
	//String ckid;//仓库ID
	//String ksid;//客商管理ID
	StringBuffer[] StrBufArray = new StringBuffer[2];
	StringBuffer strBuf = new StringBuffer();
	strBuf.append(
		"select cpraybill_bid, A.cpraybillid, A.pk_corp, B.cmangid, B.cbaseid, B.npraynum, B.cvendormangid, B.ddemanddate,");
	strBuf.append(
		" B.cwarehouseid, E.nordernum, B.pk_reqstoorg, vpraycode, pk_measdoc");
	strBuf.append(
		" from po_praybill A join po_praybill_b B on A.pk_corp = '"
			+ pk_corp
			+ "' and B.pk_reqstoorg = '"
			+ pk_calbody
			+ "'");
	strBuf.append(
		" and A.dr = 0 and B.dr = 0 and A.ibillstatus != 1 and A.cpraybillid = B.cpraybillid");
	strBuf.append(" inner join bd_invbasdoc on B.cbaseid = pk_invbasdoc");
	strBuf.append(
		" left outer join po_order_b E on E.cupsourcebilltype = '20' and E.cupsourcebillrowid = B.cpraybill_bid and E.iisactive != "+ OrderCloseItemVO.IISACTIVE_REVISION + " and E.dr = 0");
	strBuf.append(
		" left outer join po_order D on E.corderid = D.corderid  and D.forderstatus = "+ BillStatus.AUDITED );
	strBuf.append(" where B.cbaseid = '" + pk_invbasdoc + "'");
	if (sdate != null) {
		strBuf.append(" and ddemanddate >= '" + sdate.toString() + "'");
	}

	//strBuf.append(" union");
	StringBuffer strBuf1 = new StringBuffer();
	strBuf1.append(
		"select cpraybill_bid, A.cpraybillid, A.pk_corp, B.cmangid, B.cbaseid, B.npraynum, B.cvendormangid, B.ddemanddate,");
	strBuf1.append(
		" B.cwarehouseid, E.nordernum, B.pk_reqstoorg, vpraycode, pk_measdoc");
	strBuf1.append(
		" from po_praybill A inner join po_praybill_b B on A.pk_corp = '"
			+ pk_corp
			+ "' and B.pk_reqstoorg = '"
			+ pk_calbody
			+ "'");
	strBuf1.append(" and A.dr = 0 and B.dr = 0 and A.ibillstatus != 1 and A.cpraybillid = B.cpraybillid");
	strBuf1.append(" inner join bd_invbasdoc on B.cbaseid = pk_invbasdoc");
	strBuf1.append(
		" left outer join po_order_b E on E.cupsourcebilltype = '20' and E.cupsourcebillrowid = B.cpraybill_bid and E.iisactive != "+ OrderCloseItemVO.IISACTIVE_REVISION + " and E.dr = 0");
	strBuf1.append(
		" left outer join po_order D on E.corderid = D.corderid  and D.forderstatus = "+ BillStatus.AUDITED );
	strBuf1.append(" where B.cbaseid = '" + pk_invbasdoc + "'");
	if (sdate != null) {
		strBuf1.append(" and ddemanddate >= '" + sdate.toString() + "'");
	}

	StrBufArray[0] = strBuf;
	StrBufArray[1] = strBuf1;
	Vector v = new Vector();
	Connection con = null;
	PreparedStatement stmt = null;
	ResultSet rs = null;
	Vector prayIDs = new Vector();
	try {
		con = getConnection();
		for (int i = 0; i < StrBufArray.length; i++) {
			stmt = con.prepareStatement(StrBufArray[i].toString());
			rs = stmt.executeQuery();
			//
			while (rs.next()) {
				PrayExecVO prayExecVO = new PrayExecVO();
				// cpraybill_bid :
				String cpraybill_bid = rs.getString(1);
				if (prayIDs.indexOf(cpraybill_bid) >= 0)
					continue;
				else {
					prayIDs.addElement(cpraybill_bid);
					prayExecVO.setFbid(cpraybill_bid == null ? null : cpraybill_bid.trim());
					// cpraybillid :
					String cpraybillid = rs.getString(2);
					prayExecVO.setLyid(cpraybillid == null ? null : cpraybillid.trim());
					// cunitid :
					String cunitid = rs.getString(3);
					prayExecVO.setPk_corp(cunitid == null ? null : cunitid.trim());
					// cmangid :
					String cmangid = rs.getString(4);
					prayExecVO.setPk_invmandoc(cmangid == null ? null : cmangid.trim());
					// cbaseid :
					String cbaseid = rs.getString(5);
					prayExecVO.setWlbmid(cbaseid == null ? null : cbaseid.trim());
					// npraynum :
					Object npraynum =  rs.getObject(6);
					prayExecVO.setXqsl((
						npraynum == null || npraynum.toString().trim().equals(""))? new UFDouble(0.0) : new UFDouble(npraynum.toString().trim()));
					// cvendormangid :
					String cvendormangid = rs.getString(7);
					prayExecVO.setKsid(cvendormangid == null ? null : cvendormangid.trim());
					// ddemanddate :
					String ddemanddate = rs.getString(8);
					prayExecVO.setXqrq(ddemanddate == null ? null : new UFDate(ddemanddate.trim(),false));
					// cwarehouseid :
					String cwarehouseid = rs.getString(9);
					prayExecVO.setCkid(cwarehouseid == null ? null : cwarehouseid.trim());
					// naccumulatenum :
					Object naccumulatenum = rs.getObject(10);
					prayExecVO.setWcsl((
						naccumulatenum == null || naccumulatenum.toString().trim().equals("")) ? new UFDouble(0.0) : new UFDouble(naccumulatenum.toString().trim()));
					// cstoreorganization :
					String cstoreorganization = rs.getString(11);
					prayExecVO.setGcbmid(
						cstoreorganization == null ? null : cstoreorganization.trim());
					// vpraycode :
					String vpraycode = rs.getString(12);
					prayExecVO.setLydjh(vpraycode == null ? null : vpraycode.trim());
					// pk_measdoc :
					String pk_measdoc = rs.getString(13);
					prayExecVO.setJldwid(pk_measdoc == null ? null : pk_measdoc.trim());

					////满足：需求日期大于开始日期,而且累计订货数量小于请购数量 的单据数据返回
					//if (sdate.compareTo(prayExecVO.getXqrq()) < 0
					//&& prayExecVO.getWcsl().doubleValue() < prayExecVO.getXqsl().doubleValue())
					v.addElement(prayExecVO);

				}
			}
			if (rs != null) {
				rs.close();
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

	PrayExecVO returnVOs[] = new PrayExecVO[v.size()];
	if (v.size() > 0) {
		v.copyInto(returnVOs);
	}

	/*************************************************************/
	// 保留的系统管理接口：
	afterCallMethod(
		"nc.bs.pu.pr.PraybillDMO",
		"getPrayExecA",
		new Object[] { pk_corp, pk_calbody, pk_invbasdoc, sdate });
	/*************************************************************/

	return returnVOs;
}
/**
 * 此处插入方法说明。
 * 功能描述:请购执行A（取需求日期在开始日期以后的非关闭作废的请购单）提供主计量单位的数量
 *   已完成数量指相关已审批订单的采购订单数量
 * 输入参数:
 * 返回值:
 * 异常处理:
 * 
 * 修改：晁志平 梦洁需求，支持处理自由项1-5
 */
public PrayExecVO[] getPrayExecA(
	String pk_corp,
	String pk_calbody,
	String pk_invbasdoc,
	UFDate sdate,
	String vfree1,
	String vfree2,
	String vfree3,
	String vfree4,
	String vfree5)
	throws java.sql.SQLException {
	/*************************************************************/
	// 保留的系统管理接口：
	beforeCallMethod(
		"nc.bs.pu.pr.PraybillDMO",
		"getPrayExecA",
		new Object[] { pk_corp, pk_calbody, pk_invbasdoc, sdate });
	/*************************************************************/
	//String pk_corp;//公司PK
	//String gcbmid;//库存组织PK
	//String wlbmid;//存货基本ID
	//String pk_invmandoc;//存货管理ID
	//UFDate xqrq;//需求日期
	//UFDouble xqsl;//数量
	//UFDouble wcsl;//已完成数量
	//String jldwid;//计量单位ID(主计量)
	//String lyid;//请购单主表ID
	//String fbid;// 请购单附表ID
	//String lydjh;// 请购单号
	//String ckid;//仓库ID
	//String ksid;//客商管理ID
	StringBuffer[] StrBufArray = new StringBuffer[2];
	StringBuffer strBuf = new StringBuffer();
	strBuf.append(
		"select cpraybill_bid, A.cpraybillid, A.pk_corp, B.cmangid, B.cbaseid, B.npraynum, B.cvendormangid, B.ddemanddate,");
	strBuf.append(
		" B.cwarehouseid, E.nordernum, B.pk_reqstoorg, vpraycode, pk_measdoc,");
	strBuf.append("B.vfree1,B.vfree2,B.vfree3,B.vfree4,B.vfree5 ");
	strBuf.append(
		" from po_praybill A join po_praybill_b B on A.pk_corp = '"
			+ pk_corp
			+ "' and B.pk_reqstoorg = '"
			+ pk_calbody
			+ "'");
	strBuf.append(
		" and A.dr = 0 and B.dr = 0 and A.ibillstatus != 1 and A.cpraybillid = B.cpraybillid");
	strBuf.append(" inner join bd_invbasdoc on B.cbaseid = pk_invbasdoc");
	strBuf.append(
		" left outer join po_order_b E on E.cupsourcebilltype = '20' and E.cupsourcebillrowid = B.cpraybill_bid and E.iisactive != "+ OrderCloseItemVO.IISACTIVE_REVISION + " and E.dr = 0");
	strBuf.append(
		" left outer join po_order D on E.corderid = D.corderid  and D.forderstatus = "+ BillStatus.AUDITED);
	strBuf.append(" where B.cbaseid = '" + pk_invbasdoc + "' ");
	if (sdate != null) {
		strBuf.append(" and ddemanddate >= '" + sdate.toString() + "' ");
	}
	if (vfree1 != null && vfree1.trim().length() > 0){
	    strBuf.append("and B.vfree1 = '"+ vfree1.trim() +"' ");
	}
	if (vfree2 != null && vfree2.trim().length() > 0){
	    strBuf.append("and B.vfree2 = '"+ vfree2.trim() +"' ");
	}
	if (vfree3 != null && vfree3.trim().length() > 0){
	    strBuf.append("and B.vfree3 = '"+ vfree3.trim() +"' ");
	}
	if (vfree4 != null && vfree4.trim().length() > 0){
	    strBuf.append("and B.vfree4 = '"+ vfree4.trim() +"' ");
	}
	if (vfree5 != null && vfree5.trim().length() > 0){
	    strBuf.append("and B.vfree5 = '"+ vfree5.trim() +"' ");
	}
	//strBuf.append(" union");
	StringBuffer strBuf1 = new StringBuffer();
	strBuf1.append(
		"select cpraybill_bid, A.cpraybillid, A.pk_corp, B.cmangid, B.cbaseid, B.npraynum, B.cvendormangid, B.ddemanddate,");
	strBuf1.append(
		" B.cwarehouseid, E.nordernum, B.pk_reqstoorg, vpraycode, pk_measdoc,");
	strBuf1.append("B.vfree1,B.vfree2,B.vfree3,B.vfree4,B.vfree5 ");
	strBuf1.append(
		" from po_praybill A join po_praybill_b B on A.pk_corp = '"
			+ pk_corp
			+ "' and B.pk_reqstoorg = '"
			+ pk_calbody
			+ "'");
	strBuf1.append(
		" and A.dr = 0 and B.dr = 0 and A.ibillstatus != 1 and A.cpraybillid = B.cpraybillid");
	strBuf1.append(" inner join bd_invbasdoc on B.cbaseid = pk_invbasdoc");
	strBuf1.append(
		" left outer join po_order_b E on E.cupsourcebilltype = '20' and E.cupsourcebillrowid = B.cpraybill_bid and E.iisactive != "+ OrderCloseItemVO.IISACTIVE_REVISION + " and E.dr = 0");
	strBuf1.append(
		" left outer join po_order D on E.corderid = D.corderid  and D.forderstatus = "+ BillStatus.OUTPUT);
	strBuf1.append(" where B.cbaseid = '" + pk_invbasdoc + "' ");
	if (sdate != null) {
		strBuf1.append(" and ddemanddate >= '" + sdate.toString() + "' ");
	}
	if (vfree1 != null && vfree1.trim().length() > 0){
	    strBuf1.append("and B.vfree1 = '"+ vfree1.trim() +"' ");
	}
	if (vfree2 != null && vfree2.trim().length() > 0){
	    strBuf1.append("and B.vfree2 = '"+ vfree2.trim() +"' ");
	}
	if (vfree3 != null && vfree3.trim().length() > 0){
	    strBuf1.append("and B.vfree3 = '"+ vfree3.trim() +"' ");
	}
	if (vfree4 != null && vfree4.trim().length() > 0){
	    strBuf1.append("and B.vfree4 = '"+ vfree4.trim() +"' ");
	}
	if (vfree5 != null && vfree5.trim().length() > 0){
	    strBuf1.append("and B.vfree5 = '"+ vfree5.trim() +"' ");
	}

	StrBufArray[0] = strBuf;
	StrBufArray[1] = strBuf1;
	Vector v = new Vector();
	Connection con = null;
	PreparedStatement stmt = null;
	ResultSet rs = null;
	Vector prayIDs = new Vector();
	try {
		con = getConnection();
		for (int i = 0; i < StrBufArray.length; i++) {
			stmt = con.prepareStatement(StrBufArray[i].toString());
			rs = stmt.executeQuery();
			//
			while (rs.next()) {
				PrayExecVO prayExecVO = new PrayExecVO();
				// cpraybill_bid :
				String cpraybill_bid = rs.getString(1);
				if (prayIDs.indexOf(cpraybill_bid) >= 0)
					continue;
				else {
					prayIDs.addElement(cpraybill_bid);
					prayExecVO.setFbid(cpraybill_bid == null ? null : cpraybill_bid.trim());
					// cpraybillid :
					String cpraybillid = rs.getString(2);
					prayExecVO.setLyid(cpraybillid == null ? null : cpraybillid.trim());
					// cunitid :
					String cunitid = rs.getString(3);
					prayExecVO.setPk_corp(cunitid == null ? null : cunitid.trim());
					// cmangid :
					String cmangid = rs.getString(4);
					prayExecVO.setPk_invmandoc(cmangid == null ? null : cmangid.trim());
					// cbaseid :
					String cbaseid = rs.getString(5);
					prayExecVO.setWlbmid(cbaseid == null ? null : cbaseid.trim());
					// npraynum :
					Object npraynum =  rs.getObject(6);
					prayExecVO.setXqsl((
						npraynum == null || npraynum.toString().trim().equals(""))? new UFDouble(0.0) : new UFDouble(npraynum.toString().trim()));
					// cvendormangid :
					String cvendormangid = rs.getString(7);
					prayExecVO.setKsid(cvendormangid == null ? null : cvendormangid.trim());
					// ddemanddate :
					String ddemanddate = rs.getString(8);
					prayExecVO.setXqrq(ddemanddate == null ? null : new UFDate(ddemanddate.trim(),false));
					// cwarehouseid :
					String cwarehouseid = rs.getString(9);
					prayExecVO.setCkid(cwarehouseid == null ? null : cwarehouseid.trim());
					// naccumulatenum :
					Object naccumulatenum = rs.getObject(10);
					prayExecVO.setWcsl((
						naccumulatenum == null || naccumulatenum.toString().trim().equals("")) ? new UFDouble(0.0) : new UFDouble(naccumulatenum.toString().trim()));
					// cstoreorganization :
					String cstoreorganization = rs.getString(11);
					prayExecVO.setGcbmid(
						cstoreorganization == null ? null : cstoreorganization.trim());
					// vpraycode :
					String vpraycode = rs.getString(12);
					prayExecVO.setLydjh(vpraycode == null ? null : vpraycode.trim());
					// pk_measdoc :
					String pk_measdoc = rs.getString(13);
					prayExecVO.setJldwid(pk_measdoc == null ? null : pk_measdoc.trim());
					// vfree1-5 :
					prayExecVO.setVfree1(rs.getString(14));
					prayExecVO.setVfree2(rs.getString(15));
					prayExecVO.setVfree3(rs.getString(16));
					prayExecVO.setVfree4(rs.getString(17));
					prayExecVO.setVfree5(rs.getString(18));

					////满足：需求日期大于开始日期,而且累计订货数量小于请购数量 的单据数据返回
					//if (sdate.compareTo(prayExecVO.getXqrq()) < 0
					//&& prayExecVO.getWcsl().doubleValue() < prayExecVO.getXqsl().doubleValue())
					v.addElement(prayExecVO);

				}
			}
			if (rs != null) {
				rs.close();
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

	PrayExecVO returnVOs[] = new PrayExecVO[v.size()];
	if (v.size() > 0) {
		v.copyInto(returnVOs);
	}

	/*************************************************************/
	// 保留的系统管理接口：
	afterCallMethod(
		"nc.bs.pu.pr.PraybillDMO",
		"getPrayExecA",
		new Object[] { pk_corp, pk_calbody, pk_invbasdoc, sdate });
	/*************************************************************/

	return returnVOs;
}

/**
 * 此处插入方法说明。
 * 功能描述:请购执行B（取需求日期在开始日期以后的未生成采购订单的未完成的请购单）提供主计量单位的数量
 * 输入参数:
 * 返回值:
 * 异常处理:
*/
public PrayExecVO[] getPrayExecB(
	String pk_corp,
	String pk_calbody,
	String pk_invbasdoc,
	UFDate sdate)
	throws java.sql.SQLException {
	/*************************************************************/
	// 保留的系统管理接口：
	beforeCallMethod(
		"nc.bs.pu.pr.PraybillDMO",
		"getPrayExecB",
		new Object[] { pk_corp, pk_calbody, pk_invbasdoc, sdate });
	/*************************************************************/
	//String pk_corp;//公司PK
	//String gcbmid;//库存组织PK
	//String wlbmid;//存货基本ID
	//String pk_invmandoc;//存货管理ID
	//UFDate xqrq;//需求日期
	//UFDouble xqsl;//数量
	//UFDouble wcsl;//已完成数量
	//String jldwid;//计量单位ID(主计量)
	//String lyid;//请购单主表ID
	//String fbid;// 请购单附表ID
	//String lydjh;// 请购单号
	//String ckid;//仓库ID
	//String ksid;//客商管理ID
	String sql =
		"select cpraybill_bid, A.cpraybillid, A.pk_corp, cmangid, cbaseid, npraynum, cvendormangid, ddemanddate,";
	sql
		+= " cwarehouseid, naccumulatenum, B.pk_reqstoorg, vpraycode, pk_measdoc";

	sql += " from po_praybill A,po_praybill_b B, bd_invbasdoc C";
	sql
		+= " where A.pk_corp = ? and B.pk_reqstoorg = ? and A.dr = 0 and B.dr = 0 and A.cpraybillid = B.cpraybillid and cbaseid = pk_invbasdoc";
	if (!(pk_invbasdoc == null || pk_invbasdoc.trim().equals("")))
		sql += " and cbaseid = ?  ";
	Vector v = new Vector();
	Connection con = null;
	PreparedStatement stmt = null;
	try {
		con = getConnection();
		stmt = con.prepareStatement(sql);
		stmt.setString(1, pk_corp);
		stmt.setString(2, pk_calbody);
		if (!(pk_invbasdoc == null || pk_invbasdoc.trim().equals("")))
			stmt.setString(3, pk_invbasdoc);

		ResultSet rs = stmt.executeQuery();
		//
		while (rs.next()) {
			PrayExecVO prayExecVO = new PrayExecVO();
			// cpraybill_bid :
			String cpraybill_bid = rs.getString(1);
			prayExecVO.setFbid(cpraybill_bid == null ? null : cpraybill_bid.trim());
			// cpraybillid :
			String cpraybillid = rs.getString(2);
			prayExecVO.setLyid(cpraybillid == null ? null : cpraybillid.trim());
			// cunitid :
			String cunitid = rs.getString(3);
			prayExecVO.setPk_corp(cunitid == null ? null : cunitid.trim());
			// cmangid :
			String cmangid = rs.getString(4);
			prayExecVO.setPk_invmandoc(cmangid == null ? null : cmangid.trim());
			// cbaseid :
			String cbaseid = rs.getString(5);
			prayExecVO.setWlbmid(cbaseid == null ? null : cbaseid.trim());
			// npraynum :
			Object npraynum = rs.getObject(6);
			prayExecVO.setXqsl((
				npraynum == null || npraynum.toString().trim().equals(""))? new UFDouble(0.0) : new UFDouble(npraynum.toString().trim()));
			// cvendormangid :
			String cvendormangid = rs.getString(7);
			prayExecVO.setKsid(cvendormangid == null ? null : cvendormangid.trim());
			// ddemanddate :
			String ddemanddate = rs.getString(8);
			prayExecVO.setXqrq(ddemanddate == null ? null : new UFDate(ddemanddate.trim(),false));
			// cwarehouseid :
			String cwarehouseid = rs.getString(9);
			prayExecVO.setCkid(cwarehouseid == null ? null : cwarehouseid.trim());
			// naccumulatenum :
			Object naccumulatenum = rs.getObject(10);
			prayExecVO.setWcsl((
				naccumulatenum == null || naccumulatenum.toString().trim().equals(""))? new UFDouble(0.0) : new UFDouble(naccumulatenum.toString().trim()));
			// cstoreorganization :
			String cstoreorganization = rs.getString(11);
			prayExecVO.setGcbmid(
				cstoreorganization == null ? null : cstoreorganization.trim());
			// vpraycode :
			String vpraycode = rs.getString(12);
			prayExecVO.setLydjh(vpraycode == null ? null : vpraycode.trim());
			// pk_measdoc :
			String pk_measdoc = rs.getString(13);
			prayExecVO.setJldwid(pk_measdoc == null ? null : pk_measdoc.trim());
			//满足：需求日期大于开始日期,而且累计订货数量为零 的单据数据返回
			if (sdate.compareTo(prayExecVO.getXqrq()) <= 0
				&& prayExecVO.getWcsl().doubleValue() == 0.0)
				v.addElement(prayExecVO);
		}
		rs.close();
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

	PrayExecVO returnVOs[] = new PrayExecVO[v.size()];
	if (v.size() > 0) {
		v.copyInto(returnVOs);
	}

	/*************************************************************/
	// 保留的系统管理接口：
	afterCallMethod(
		"nc.bs.pu.pr.PraybillDMO",
		"getPrayExecB",
		new Object[] { pk_corp, pk_calbody, pk_invbasdoc, sdate });
	/*************************************************************/

	return returnVOs;
}

/**
 * 此处插入方法说明。
 * 功能描述:请购执行B（取需求日期在开始日期以后的未生成采购订单的未完成的请购单）提供主计量单位的数量
 * 输入参数:
 * 返回值:
 * 异常处理:
 */
public PrayExecVO[] getPrayExecB(
	String pk_corp,
	String pk_calbody,
	String pk_invbasdoc,
	UFDate sdate,
	String vfree1,
	String vfree2,
	String vfree3,
	String vfree4,
	String vfree5)
	throws java.sql.SQLException {
	/*************************************************************/
	// 保留的系统管理接口：
	beforeCallMethod(
		"nc.bs.pu.pr.PraybillDMO",
		"getPrayExecB",
		new Object[] { pk_corp, pk_calbody, pk_invbasdoc, sdate });
	/*************************************************************/
	//String pk_corp;//公司PK
	//String gcbmid;//库存组织PK
	//String wlbmid;//存货基本ID
	//String pk_invmandoc;//存货管理ID
	//UFDate xqrq;//需求日期
	//UFDouble xqsl;//数量
	//UFDouble wcsl;//已完成数量
	//String jldwid;//计量单位ID(主计量)
	//String lyid;//请购单主表ID
	//String fbid;// 请购单附表ID
	//String lydjh;// 请购单号
	//String ckid;//仓库ID
	//String ksid;//客商管理ID

	String sql =
		"select cpraybill_bid, A.cpraybillid, A.pk_corp, cmangid, cbaseid, npraynum, cvendormangid, ddemanddate,";
	sql
		+= " cwarehouseid, naccumulatenum, B.pk_reqstoorg, vpraycode, pk_measdoc,";
	sql += "B.vfree1,B.vfree2,B.vfree3,B.vfree4,B.vfree5 ";
	sql += " from po_praybill A,po_praybill_b B, bd_invbasdoc C";
	sql
		+= " where A.pk_corp = ? and B.pk_reqstoorg = ? and A.dr = 0 and B.dr = 0 and A.cpraybillid = B.cpraybillid and cbaseid = pk_invbasdoc ";
	if (!(pk_invbasdoc == null || pk_invbasdoc.trim().equals("")))
		sql += " and cbaseid = ?  ";
	if (vfree1 != null && vfree1.trim().length() > 0){
		sql += "and B.vfree1 = '"+ vfree1.trim() +"' ";
	}
	if (vfree2 != null && vfree2.trim().length() > 0){
		sql += "and B.vfree2 = '"+ vfree2.trim() +"' ";
	}
	if (vfree3 != null && vfree3.trim().length() > 0){
		sql += "and B.vfree3 = '"+ vfree3.trim() +"' ";
	}
	if (vfree4 != null && vfree4.trim().length() > 0){
		sql += "and B.vfree4 = '"+ vfree4.trim() +"' ";
	}
	if (vfree5 != null && vfree5.trim().length() > 0){
		sql += "and B.vfree5 = '"+ vfree5.trim() +"' ";
	}
	
	Vector v = new Vector();
	Connection con = null;
	PreparedStatement stmt = null;
	try {
		con = getConnection();
		stmt = con.prepareStatement(sql);
		stmt.setString(1, pk_corp);
		stmt.setString(2, pk_calbody);
		if (!(pk_invbasdoc == null || pk_invbasdoc.trim().equals("")))
			stmt.setString(3, pk_invbasdoc);

		ResultSet rs = stmt.executeQuery();
		//
		while (rs.next()) {
			PrayExecVO prayExecVO = new PrayExecVO();
			// cpraybill_bid :
			String cpraybill_bid = rs.getString(1);
			prayExecVO.setFbid(cpraybill_bid == null ? null : cpraybill_bid.trim());
			// cpraybillid :
			String cpraybillid = rs.getString(2);
			prayExecVO.setLyid(cpraybillid == null ? null : cpraybillid.trim());
			// cunitid :
			String cunitid = rs.getString(3);
			prayExecVO.setPk_corp(cunitid == null ? null : cunitid.trim());
			// cmangid :
			String cmangid = rs.getString(4);
			prayExecVO.setPk_invmandoc(cmangid == null ? null : cmangid.trim());
			// cbaseid :
			String cbaseid = rs.getString(5);
			prayExecVO.setWlbmid(cbaseid == null ? null : cbaseid.trim());
			// npraynum :
			Object npraynum = rs.getObject(6);
			prayExecVO.setXqsl((
				npraynum == null || npraynum.toString().trim().equals(""))? new UFDouble(0.0) : new UFDouble(npraynum.toString().trim()));
			// cvendormangid :
			String cvendormangid = rs.getString(7);
			prayExecVO.setKsid(cvendormangid == null ? null : cvendormangid.trim());
			// ddemanddate :
			String ddemanddate = rs.getString(8);
			prayExecVO.setXqrq(ddemanddate == null ? null : new UFDate(ddemanddate.trim(),false));
			// cwarehouseid :
			String cwarehouseid = rs.getString(9);
			prayExecVO.setCkid(cwarehouseid == null ? null : cwarehouseid.trim());
			// naccumulatenum :
			Object naccumulatenum = rs.getObject(10);
			prayExecVO.setWcsl((
				naccumulatenum == null || naccumulatenum.toString().trim().equals(""))? new UFDouble(0.0) : new UFDouble(naccumulatenum.toString().trim()));
			// cstoreorganization :
			String cstoreorganization = rs.getString(11);
			prayExecVO.setGcbmid(
				cstoreorganization == null ? null : cstoreorganization.trim());
			// vpraycode :
			String vpraycode = rs.getString(12);
			prayExecVO.setLydjh(vpraycode == null ? null : vpraycode.trim());
			// pk_measdoc :
			String pk_measdoc = rs.getString(13);
			prayExecVO.setJldwid(pk_measdoc == null ? null : pk_measdoc.trim());
			// vfree1-5 :
			prayExecVO.setVfree1(rs.getString(14));
			prayExecVO.setVfree2(rs.getString(15));
			prayExecVO.setVfree3(rs.getString(16));
			prayExecVO.setVfree4(rs.getString(17));
			prayExecVO.setVfree5(rs.getString(18));

			//满足：需求日期大于开始日期,而且累计订货数量为零 的单据数据返回
			if (sdate.compareTo(prayExecVO.getXqrq()) <= 0
				&& prayExecVO.getWcsl().doubleValue() == 0.0)
				v.addElement(prayExecVO);
		}
		rs.close();
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

	PrayExecVO returnVOs[] = new PrayExecVO[v.size()];
	if (v.size() > 0) {
		v.copyInto(returnVOs);
	}

	/*************************************************************/
	// 保留的系统管理接口：
	afterCallMethod(
		"nc.bs.pu.pr.PraybillDMO",
		"getPrayExecB",
		new Object[] { pk_corp, pk_calbody, pk_invbasdoc, sdate });
	/*************************************************************/

	return returnVOs;
}
/**
 * 此处插入方法说明。
 * 功能描述:判断请购单的请购人是否属于输入的请购部门
 * 输入参数:请购单VO
 * 返回值:
 * 异常处理:无
 * @throws SystemException 
 */
private UFBoolean isPsnBelongDept(PraybillVO VO)
	throws
		javax.naming.NamingException,
		BusinessException,
		java.sql.SQLException,
		BusinessException, SystemException {
	nc.bs.pr.pray.PraybillDMO dmo = new nc.bs.pr.pray.PraybillDMO();
	boolean b = false;
	PraybillHeaderVO headVO = VO.getHeadVO();
	String sUnitCode = headVO.getPk_corp();
	String sDeptID = headVO.getCdeptid();
	String sPsnID = headVO.getCpraypsn();
	if (sDeptID != null
		&& sDeptID.length() > 0
		&& sPsnID != null
		&& sPsnID.length() > 0)
		b = dmo.isPsnBelongDept(sUnitCode, sDeptID, sPsnID);
	else
		return new UFBoolean(!b);
	return new UFBoolean(b);
}
/**
 * 作者：周晓
 * 功能：当前操作员对应业务员所属部门
 * 参数：无
 * 返回：String
 * 例外：SQLException
 * 日期：(2004-3-2 11:51:50)
 */
public String getPrayDeptId(String pk_psndoc) throws SQLException{
	StringBuffer sbSql = new StringBuffer();
	Connection con = null;
	PreparedStatement stmt = null;
	ResultSet rs = null;
	String prayDeptId = null;
	try {
		sbSql.append( "select pk_deptdoc from bd_deptdoc where pk_psndoc ='"+pk_psndoc+"'");
		con = getConnection();
		stmt = con.prepareStatement(sbSql.toString());
		rs = stmt.executeQuery();
		if(rs.next()){
			prayDeptId = (String)rs.getObject(1);
		}
	}
	finally {
		try {
			if (rs != null) {
				rs.close();
			}
		} catch (Exception e) {}
		try {
			if (stmt != null) {
				stmt.close();
			}
		} catch (Exception e) {}
		try {
			if (con != null) {
				con.close();
			}
		}catch (Exception e) {}
	}
	return prayDeptId;
}
/**
 * @function  插入SmartVO数组
 *
 * @author QuSida
 *
 * @param vos
 * @throws SystemException
 * @throws NamingException
 * @throws SQLException 
 *
 * @return void
 *
 * @date 2010-8-12 上午10:43:36
 */
public void insertSmartVOs(SmartVO[] vos) throws SystemException, NamingException, SQLException{
	SmartDMO dmo = new SmartDMO();
	for (int i = 0; i < vos.length; i++) {
		vos[i].setPrimaryKey(dmo.getOID());
		vos[i].setAttributeValue("dr", 0);
		vos[i].setStatus(VOStatus.NEW);
	}
	dmo.maintain(vos);
}

/**
 * @function 查询SmartVO数组
 *
 * @author QuSida
 *
 * @param voClass
 * @param names
 * @param where
 *
 * @throws SystemException
 * @throws NamingException
 * @throws SQLException 
 *
 * @return SmartVO[]
 *
 * @date 2010-9-11 下午02:24:50
 */
public SmartVO[] querySmartVOs(Class voClass, String[] names, String where) throws SystemException, NamingException, SQLException{
	SmartDMO dmo = new SmartDMO();

	SmartVO[] smartVOs = dmo.selectBy(voClass,names,where);
	
	return smartVOs;
}

/**
 * @function 删除SmartVO数组
 *
 * @author QuSida
 *
 * @param vos
 * @throws SystemException
 * @throws NamingException
 * @throws SQLException 
 *
 * @return void
 *
 * @date 2010-9-11 下午02:25:17
 */
public void deleteSmartVOs(SmartVO[] vos) throws SystemException, NamingException, SQLException{
	SmartDMO dmo = new SmartDMO();
	for (int i = 0; i < vos.length; i++) {
		vos[i].setStatus( VOStatus.DELETED);
	}
	dmo.maintain(vos);
}
/**
 * @function 修改SmartVO数组
 *
 * @author QuSida
 *
 * @param vos
 * @throws SystemException
 * @throws NamingException
 * @throws SQLException 
 *
 * @return void
 *
 * @date 2010-9-11 下午02:25:30
 */
public void updateSmartVOs(SmartVO[] vos,String cbillid) throws SystemException, NamingException, SQLException{
	SmartDMO dmo = new SmartDMO();
	String preparedUpdateSql = "delete from jj_scm_informationcost where cbillid = '"+cbillid+"'";
    dmo.executeUpdate(preparedUpdateSql);
    if(vos.length == 0 || vos == null) 
    	return;
     insertSmartVOs(vos);
}

}