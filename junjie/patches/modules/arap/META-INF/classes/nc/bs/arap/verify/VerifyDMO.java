package nc.bs.arap.verify;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import nc.bs.arap.selectedpay.BillFilterDMO;
import nc.bs.arap.util.SqlUtils;
import nc.bs.logging.Logger;
import nc.vo.arap.exception.ExceptionHandler;
import nc.vo.arap.selectedpay.DataManager;
import nc.vo.arap.verify.DJCLBVO;
import nc.vo.arap.verify.ForDapMsgParaVO;
import nc.vo.arap.verify.VerifyBackDataVO;
import nc.vo.arap.verify.VerifyUpdateBillBalanceVO;
import nc.vo.pub.lang.UFDouble;

public class VerifyDMO extends DataManager {
/**
 * VerifyDMO 构造子注解。
 * @exception javax.naming.NamingException 异常说明。
 * @exception nc.bs.pub.SystemException 异常说明。
 */
public VerifyDMO() throws javax.naming.NamingException, nc.bs.pub.SystemException {
	super();
}

/**
 * 建立临时表。
 * 创建日期：(2001-6-12 17:03:15)
 * @author：wyan
 * @return boolean
 */
public String createTabforSo(Connection con) throws Exception {

    String tname = null;
        con = getConnection();
        nc.bs.mw.sqltrans.TempTable tmptab = new nc.bs.mw.sqltrans.TempTable();
        tname =tmptab.createTempTable(
                con,
                "Hx_so",
                "djpk char(20),djtpk char(20),clje decimal(16,4)",
                "djtpk");
    return tname;
}

/**
 * 取得给会计平台信息vo信息。
 * 创建日期：(2000-12-18 16:41:55)
 * @param infoVO nc.vo.arap.verify.DJCLBVO
 */
public ForDapMsgParaVO getDataForDapMsgVO(DJCLBVO infoVO) throws Exception {

	/*************************************************************/
	// 保留的系统管理接口：
	beforeCallMethod("nc.bs.arap.verify.VerifyDMO", "getDataForDapMsgVO", new Object[]{infoVO});
	/*************************************************************/

	String sql =
		" select distinct fb.wldx,clb.djdl,clb.clbz,fb.bzbm"+
		" from arap_djclb clb "+
		" inner join arap_djfb fb on fb.fb_oid = clb.fb_oid "+
		" where clb.dwbm= ? and clb.clbh = ? and clb.hxbh = 0 "+
		" and clb.dr = 0 and clb.clbz<>6 ";


	ForDapMsgParaVO paraVO = new ForDapMsgParaVO();
	Connection con = null;
	PreparedStatement stmt = null;
	try {
		con = getConnection();
		stmt = con.prepareStatement(sql);
		if (infoVO.getDwbm() == null) {
			stmt.setNull(1,Types.CHAR);
		}
		else {
			stmt.setString(1,infoVO.getDwbm().trim());
		}
		if (infoVO.getClbh() == null) {
			stmt.setNull(2,Types.CHAR);
		}
		else {
			stmt.setString(2,infoVO.getClbh().trim());
		}

		ResultSet rs = stmt.executeQuery();

		if(rs.next()) {

		/*因为加了币种返回结果集有可能出现多条结果(例如异币种核销)*/
		/*但是这里只要一条结果就可以了，异币种核销是特例*/
			//wldx
			Integer wldx = (Integer)rs.getObject(1);
			paraVO.setWldx(wldx.intValue());
			// djdl :
			String djdl = rs.getString(2);
			paraVO.setDjdl(djdl == null ? null : djdl.trim());
			//clbz :
			Integer clbz = (Integer)rs.getObject(3);
			paraVO.setClbz(infoVO.getClbz().equals(DJCLBVO.CLBZ_BZ_IN_6)?DJCLBVO.CLBZ_BZ_OUT_5:infoVO.getClbz());
			// bzmc :
			String bzmc = rs.getString(4);
			paraVO.setBzmc(bzmc == null ? null : bzmc.trim());

		}
	}
	finally {
		try {
			if (stmt != null) {
				stmt.close();
			}
		}catch (Exception e) {}
		try {
			if (con != null) {
				con.close();
			}
		}catch (Exception e) {}
	}

	/*************************************************************/
	// 保留的系统管理接口：
	afterCallMethod("nc.bs.arap.verify.VerifyDMO", "getDataForDapMsgVO", new Object[]{infoVO});
	/*************************************************************/

	return paraVO;
}
/**
 * 获得数据库连接。
 * 创建日期：(2003-7-31 14:58:20)
 * @return java.sql.Connection
 */
public Connection getDBConnection() throws Exception {

	return getConnection();
}

/**
 * 此处插入方法描述。
 * 创建日期：(2004-3-25 22:21:36)
 * @return java.util.Hashtable
 */
public Hashtable<String,ArrayList<VerifyBackDataVO>>  getSoVerifyCreditData(String test, boolean m_bIsMostEarly)
    throws Exception {
    String CreditBackSql = "";
    String ordersql = "";
    CreditBackSql =
        "select zb.vouchid,fb.fb_oid,xyb.fkxyb_oid,zb.ywbm,zb.djdl,zb.djbh,"
            + "fb.flbh,zb.djrq,zb.shrq,zb.effectdate,fb.ordercusmandoc,"
            + "xyb.xydqr,fb.hsdj,xyb.ybye,xyb.fbye,xyb.bbye,zb.ts,fb.bzbm "
            + "from arap_djfkxyb xyb "
            + "inner join arap_djfb fb on xyb.fb_oid = fb.fb_oid "
            + "inner join arap_djzb zb on xyb.vouchid = zb.vouchid "
            + "inner join "
            + test
            + " tmp on xyb.fb_oid = tmp.djtpk "
            + "where fb.fx < 0 "
            + ordersql;
    if (!m_bIsMostEarly) {
        ordersql = " order by fb.fb_oid,xyb.xydqr desc,zb.djbh";
    } else {
        ordersql = " order by fb.fb_oid,xyb.xydqr,zb.djbh ";
    }
    Connection con = null;
    PreparedStatement stmt = null;
    con = getConnection();
    Hashtable<String,ArrayList<VerifyBackDataVO>> hCredit = new Hashtable<String, ArrayList<VerifyBackDataVO>>();
    try {
        /*贷方数据整理***************************************************************/
        stmt = con.prepareStatement(CreditBackSql);
        ResultSet credit_rs_back = stmt.executeQuery();
        Vector<VerifyBackDataVO> vCredit = onDpToBackSo(credit_rs_back);

        VerifyBackDataVO CreditBackDataVO = null;
        ArrayList<VerifyBackDataVO> CList = null;
        for (int i = 0; i < vCredit.size(); i++) {
            CreditBackDataVO = vCredit.elementAt(i);
            CList = hCredit.get(CreditBackDataVO.getFboid());
            if (CList == null) {
                CList = new ArrayList<VerifyBackDataVO>();
            }
            CList.add(CreditBackDataVO);
            hCredit.put(CreditBackDataVO.getFboid(), CList);
        }
        /*贷方数据整理***************************************************************/

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
    return hCredit;

}
/**
 * 此处插入方法描述。
 * 创建日期：(2004-3-25 22:21:36)
 * @return java.util.Hashtable
 */
public Hashtable<String,ArrayList<VerifyBackDataVO>> getSoVerifyDebitData(String test, boolean m_bIsMostEarly)
    throws Exception {
    String DebitBackSql = "";
    String ordersql = "";
    DebitBackSql =
        "select zb.vouchid,fb.fb_oid,xyb.fkxyb_oid,zb.ywbm,zb.djdl,zb.djbh,"
            + "fb.flbh,zb.djrq,zb.shrq,zb.effectdate,fb.ordercusmandoc,"
            + "xyb.xydqr,fb.hsdj,xyb.ybye,xyb.fbye,xyb.bbye,zb.ts,fb.bzbm "
            + "from arap_djfkxyb xyb "
            + "inner join arap_djfb fb on xyb.fb_oid = fb.fb_oid "
            + "inner join arap_djzb zb on xyb.vouchid = zb.vouchid "
            + "inner join "
            + test
            + " tmp on xyb.fb_oid = tmp.djtpk "
            + "where fb.fx > 0 "
            + ordersql;
    if (!m_bIsMostEarly) {
        ordersql = " order by fb.fb_oid,xyb.xydqr desc,zb.djbh ";
    } else {
        ordersql = " order by fb.fb_oid,xyb.xydqr,zb.djbh ";
    }
    Connection con = null;
    PreparedStatement stmt = null;
    con = getConnection();
    Hashtable<String,ArrayList<VerifyBackDataVO>> hDebit = new Hashtable<String, ArrayList<VerifyBackDataVO>>();
    try {
        /*借方数据整理***************************************************************/
        stmt = con.prepareStatement(DebitBackSql);
        ResultSet debit_rs_back = stmt.executeQuery();
        Vector<VerifyBackDataVO> vDebit = onDpToBackSo(debit_rs_back);

        VerifyBackDataVO DebitBackDataVO = null;
        ArrayList<VerifyBackDataVO> DList = null;
        for (int i = 0; i < vDebit.size(); i++) {
            DebitBackDataVO = vDebit.elementAt(i);
            DList = hDebit.get(DebitBackDataVO.getFboid());
            if (DList == null) {
                DList = new ArrayList<VerifyBackDataVO>();
            }
            DList.add(DebitBackDataVO);
            hDebit.put(DebitBackDataVO.getFboid(), DList);
        }
        /*借方数据整理***************************************************************/
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
    return hDebit;

}
/**
 * 主要功能：取得当前单位的最大CLID--核销生成的两条处理记录CLID在本单位唯一。
 * 主要算法：
 * 异常描述：
 * 创建日期：(2001-7-9 20:02:25)
 * 最后修改日期：(2001-7-9 20:02:25)
 * @author：wyan
 * @return int
 * @param sDwbm java.lang.String
 */
public int getVerifyClid(String sDwbm) throws Exception {

	/*************************************************************/
	// 保留的系统管理接口：
	beforeCallMethod("nc.bs.arap.verify.VerifyDMO", "getVerifyClid", new Object[] {sDwbm});
	/*************************************************************/

	String sSql = " select max(clid) from arap_djclb where dwbm='" + sDwbm + "' and dr = 0 ";

	int iClid = -1;
	Connection con = null;
	PreparedStatement stmt = null;
	try {
		con = getConnection();
		stmt = con.prepareStatement(sSql);
		ResultSet rs = stmt.executeQuery();
		if (rs.next()) {
			Integer sClid = (Integer)rs.getObject(1);
			sClid = (sClid == null)? new Integer(0) : sClid;
			iClid = sClid.intValue();
		}
	}
	finally {
		try {
			if (stmt != null) {
				stmt.close();
			}
		}
		catch (Exception e) {
		}
		try {
			if (con != null) {
				con.close();
			}
		}
		catch (Exception e) {
		}
	}

	/*************************************************************/
	// 保留的系统管理接口：
	afterCallMethod("nc.bs.arap.verify.VerifyDMO", "getVerifyClid", new Object[] {sDwbm});
	/*************************************************************/

	return iClid;
}

/**

 */
public String insertIntoTabforSo(nc.vo.so.so016.SoBalanceToArVO[] sovo)
    throws Exception {

    Connection con = null;
    PreparedStatement stmt = null;
    String tablename = createTabforSo(con);
    try {
        con = getConnection();
        ((nc.jdbc.framework.crossdb.CrossDBConnection)con).setAddTimeStamp(false);
        String sql =
            "insert into " + tablename + " (djpk,djtpk,clje) values(?,?,?) ";
        stmt = prepareStatement(con, sql);
        UFDouble ZERO = new UFDouble(0.00);
        for (int i = 0; i < sovo.length; i++) {
            if (sovo[i].getYSDID() == null) {
                stmt.setNull(1, Types.CHAR);
            } else {
                stmt.setString(1, sovo[i].getYSDID());
            }
            if (sovo[i].getYSDROWID() == null) {
                stmt.setNull(2, Types.CHAR);
            } else {
                stmt.setString(2, sovo[i].getYSDROWID());
            }
            if (sovo[i].getBCHXYBJE() == null) {
                stmt.setBigDecimal(3, ZERO.toBigDecimal());
            } else {
                stmt.setBigDecimal(3, sovo[i].getBCHXYBJE().toBigDecimal());
            }
            stmt.addBatch();

            if (sovo[i].getSKDID() == null) {
                stmt.setNull(1, Types.CHAR);
            } else {
                stmt.setString(1, sovo[i].getSKDID());
            }
            if (sovo[i].getSKDROWID() == null) {
                stmt.setNull(2, Types.CHAR);
            } else {
                stmt.setString(2, sovo[i].getSKDROWID());
            }
            if (sovo[i].getBCHXYBJE() == null) {
                stmt.setBigDecimal(3, ZERO.toBigDecimal());
            } else {
                stmt.setBigDecimal(3, sovo[i].getBCHXYBJE().toBigDecimal());
            }
            stmt.addBatch();
        }
        stmt.executeBatch();

    } catch (Exception ex) {
    	throw ExceptionHandler.handleException(this.getClass(),ex);
    } finally {
	    ((nc.jdbc.framework.crossdb.CrossDBConnection)con).setAddTimeStamp(true);
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

    return tablename;

}

/**
 * 给核销后台VO付值。
 * 创建日期：(2001-5-30 10:24:05)
 * @author：wyan
 * @return java.util.Vector
 * @param rsbackback java.sql.ResultSet
 */
private Vector<VerifyBackDataVO> onDpToBackSo(ResultSet rsback) throws Exception {

	Vector<VerifyBackDataVO> vBack = new Vector<VerifyBackDataVO>();

	while (rsback.next()) {
		VerifyBackDataVO vBackData = new VerifyBackDataVO();
		// 选择标志 :
		int XZBZ = -1;
		vBackData.setBz(XZBZ);
		// 主表ID :
		String VOUCHID = rsback.getString(1);
		vBackData.setVouchid(VOUCHID == null ? null : VOUCHID.trim());
		// 付表ID :
		String FBOID = rsback.getString(2);
		vBackData.setFboid(FBOID == null ? null : FBOID.trim());
		// 付款协议表ID :
		String FKXYBOID = rsback.getString(3);
		vBackData.setFkxyboid(FKXYBOID == null ? null : FKXYBOID.trim());
		// 业务编码 :
		String YWBM = rsback.getString(4);
		vBackData.setYwbm(YWBM == null ? null : YWBM.trim());
		// 单据大类 :
		String DJDL = rsback.getString(5);
		vBackData.setDjdl(DJDL == null ? null : DJDL.trim());
		// 单据编号 :
		String DJBH = rsback.getString(6);
		vBackData.setDjbh(DJBH == null ? null : DJBH.trim());
		// 分录编号 :
		Integer FLBH = (Integer) rsback.getObject(7);
		vBackData.setFlbh(FLBH == null ? null : FLBH.toString().trim());
		// 单据日期 :
		String DJRQ = rsback.getString(8);
		vBackData.setDjrq(DJRQ == null ? null : DJRQ.trim());
		// 审核日期 :
		String SHRQ = rsback.getString(9);
		vBackData.setShrq(SHRQ == null ? null : SHRQ.trim());
		/****************************************************/
		//生效日期
		String EffectDate = rsback.getString(10);
		vBackData.setEffectDate(EffectDate == null ? null : EffectDate.trim());
		//订单客商
		String OrderCusmanDoc  = rsback.getString(11);
		vBackData.setOrderCusmanDoc(OrderCusmanDoc == null ? null : OrderCusmanDoc.trim());
		/****************************************************/
		// 信用到期日 :
		String XYDQR = rsback.getString(12);
		vBackData.setXydqr(XYDQR == null ? null : XYDQR.trim());
		// 单价 :
		BigDecimal DJ = (BigDecimal) rsback.getObject(13);
		vBackData.setDj(DJ == null ? null : new UFDouble(DJ));
		// 原币余额 :
		BigDecimal YBYE = (BigDecimal) rsback.getObject(14);
		vBackData.setYbye(YBYE == null ? null : new UFDouble(YBYE));
		// 辅币余额 :
		BigDecimal FBYE = (BigDecimal) rsback.getObject(15);
		vBackData.setFbye(FBYE == null ? null : new UFDouble(FBYE));
		// 本币余额 :
		BigDecimal BBYE = (BigDecimal) rsback.getObject(16);
		vBackData.setBbye(BBYE == null ? null : new UFDouble(BBYE));
		
		vBackData.setTs(rsback.getString(17));
		
		vBackData.setBzbm(rsback.getString(18));
		
		// 原币折扣 :
		UFDouble YBZK = new UFDouble(0);
		vBackData.setYbzk(YBZK);
		// 辅币折扣 :
		UFDouble FBZK = new UFDouble(0);
		vBackData.setFbzk(FBZK);
		// 本币折扣 :
		UFDouble BBZK = new UFDouble(0);
		vBackData.setBbzk(BBZK);
		////wldxbm:(为了解决并账后核销出错使用)
		//String wldxbm = rsback.getString(17);
		//vBackData.setWldxbm(wldxbm == null ? "" : wldxbm.trim());

		vBack.addElement(vBackData);

	}
	return vBack;
}


/**
 * 方法说明： 由于处理表的借贷只能有一方有数，故发生额为 借+贷
 * 注：传入参数可以优化
 *创建日期：(2001-10-11 9:30:38)
 * @param vo nc.vo.arap.verify.DJCLBVO
 */
public void updateDJFB(Hashtable<String, VerifyUpdateBillBalanceVO> hdjclje,boolean isverify,String clrq) throws Exception {

    String sql = "";
    String lock = "update arap_djzb set vouchid=vouchid where vouchid= ? ";//锁定单据用
    Connection con = null;
    PreparedStatement updatestmt = null;
    PreparedStatement lockstmt = null;

    /**减余额*/
    if(isverify){
    	 sql ="UPDATE ARAP_DJFB SET YBYE = YBYE - ? , FBYE = FBYE - ? , BBYE = BBYE - ? , SHLYE = SHLYE - ?,verifyfinisheddate=case when  YBYE=? and bbye=? then '"+clrq+"' else verifyfinisheddate end, "+" isverifyfinished =case when  YBYE=? then 'Y' else isverifyfinished end "+" WHERE FB_OID = ?";
    }
    else
    {
    	 sql = "UPDATE ARAP_DJFB SET YBYE = YBYE - ? , FBYE = FBYE - ? , BBYE = BBYE - ? , SHLYE = SHLYE - ?,verifyfinisheddate='3000-12-31' , isverifyfinished ='N' "+" WHERE FB_OID = ?";
    }
       
        Logger.info(sql);

    try {

        con = getConnection();
        lockstmt = con.prepareStatement(lock);
        

	    Enumeration<VerifyUpdateBillBalanceVO> en = hdjclje.elements();

    while (en.hasMoreElements()) {
    	VerifyUpdateBillBalanceVO vo= en.nextElement();
	    lockstmt.setString(1,vo.getKey());
	    lockstmt.addBatch();
    }
    lockstmt.executeBatch();
    en = hdjclje.elements();
    updatestmt = con.prepareStatement(sql);
    //ncm wangshb
    List<String> fboids=new ArrayList<String>();
    while (en.hasMoreElements()) {
    	VerifyUpdateBillBalanceVO vo = en.nextElement();

	    //update
	    if(isverify)
	    {
	    	updatestmt.setBigDecimal(1,vo.getIncrement_original().toBigDecimal());
	        updatestmt.setBigDecimal(2,vo.getIncrement_frac().toBigDecimal());
	        updatestmt.setBigDecimal(3,vo.getIncrement_local().toBigDecimal());
	        updatestmt.setBigDecimal(4,vo.getIncrement_quantity().toBigDecimal());
	        updatestmt.setBigDecimal(5,vo.getIncrement_original().toBigDecimal());
	        updatestmt.setBigDecimal(6,vo.getIncrement_local().toBigDecimal());
	        updatestmt.setBigDecimal(7,vo.getIncrement_original().toBigDecimal());
	        updatestmt.setString(8,vo.getSubkey());
	        
	        //ncm wangshb
	        fboids.add(vo.getSubkey());
	    }
	    else
	    {
	    	updatestmt.setBigDecimal(1,vo.getIncrement_original().toBigDecimal());
	        updatestmt.setBigDecimal(2,vo.getIncrement_frac().toBigDecimal());
	        updatestmt.setBigDecimal(3,vo.getIncrement_local().toBigDecimal());
	        updatestmt.setBigDecimal(4,vo.getIncrement_quantity().toBigDecimal());
	        
	        updatestmt.setString(5,vo.getSubkey());
	    }        
        updatestmt.addBatch();
    }
    	updatestmt.executeBatch();
    	
    	
    	//FIXME
    	//ncm wangshb
    	if(fboids.size()>0){
	    	String sqlupdateFinishDate="update arap_djfb set verifyfinisheddate=" +
	    			"(select max(clrq) from arap_djclb where arap_djclb.fb_oid=arap_djfb.fb_oid) where isverifyfinished='Y' and bbye=0 and "+
	    			SqlUtils.getInStr("fb_oid", fboids.toArray(new String[]{}));
	    	
			PreparedStatement stmt = con.prepareStatement(sqlupdateFinishDate);
			stmt.execute();
		}
    	
//	    en = hdjclje.elements();
//	    String[] tfboid=new String[hdjclje.size()];
//	    ArrayList<ArrayList<Object>> verifyJE=new ArrayList<ArrayList<Object>>();
//	    int t=0;
//	    
//	    while (en.hasMoreElements()) {
//	    	VerifyUpdateBillBalanceVO vo = en.nextElement();
//		    tfboid[t]=vo.getSubkey();
//		    String op_subkey = vo.getOp_subkey(); 
//		    ArrayList<Object> list=new ArrayList<Object>();
//		    list.add(op_subkey);
//		    list.add(tfboid[t]);
//		    list.add(vo.getIncrement_original());
//		    
////		    verifyJE.add(list);
//		    
//		    t++;		    
//	    }
	    
//	    if(tfboid.length!=0)
//	    {
//	    	new BillFilterDMO().VerifyXzfk(verifyJE);
//	    }

	  
    }catch(Exception e){
    	throw ExceptionHandler.handleException(this.getClass(),e);
    } finally {
        try {
            if (updatestmt != null) {
                updatestmt.close();
            }
            if (lockstmt != null) {
                lockstmt.close();
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

}
/**
 * 方法说明:由于处理表的借贷只能有一方有数，故发生额为 借+贷
 * 主要算法：
 * 注意：传入参数可以优化
 * 创建日期：(2001-10-11 9:33:14)
 * @param vo nc.vo.arap.verify.DJCLBVO
 */
public void updateDJFKXYB(Hashtable<String, VerifyUpdateBillBalanceVO> hdjclje) throws Exception{

	String sql = "";

	/**减余额*/
	sql ="UPDATE ARAP_DJFKXYB SET YBYE = YBYE - ?, FBYE = FBYE - ? , BBYE = BBYE - ? , SHLYE = SHLYE - ? WHERE FKXYB_OID = ? ";
	 Logger.info(sql);

	Connection con = null;
	PreparedStatement updatestmt = null;

	try {
		con=getConnection();
		
        updatestmt = con.prepareStatement(sql);
        
    Enumeration<VerifyUpdateBillBalanceVO> en = hdjclje.elements();

    while (en.hasMoreElements()) {
    	VerifyUpdateBillBalanceVO vo= en.nextElement();
	    //update
    	updatestmt.setBigDecimal(1,vo.getIncrement_original().toBigDecimal());
        updatestmt.setBigDecimal(2,vo.getIncrement_frac().toBigDecimal());
        updatestmt.setBigDecimal(3,vo.getIncrement_local().toBigDecimal());
        updatestmt.setBigDecimal(4,vo.getIncrement_quantity().toBigDecimal());
        updatestmt.setString(5,vo.getDetailkey());        
        updatestmt.addBatch();
    }
    updatestmt.executeBatch();
    
} catch (Exception ex) {
	ExceptionHandler.error(sql);
    throw ExceptionHandler.createException(ex.getMessage());
	} finally {
		try {
			if (updatestmt != null) {
				updatestmt.close();
			}
		}catch (Exception e) {}
		try {
			if (con != null) {
				con.close();
			}
		}catch (Exception e) {}
	}

}
}