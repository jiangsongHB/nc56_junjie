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
 * VerifyDMO ������ע�⡣
 * @exception javax.naming.NamingException �쳣˵����
 * @exception nc.bs.pub.SystemException �쳣˵����
 */
public VerifyDMO() throws javax.naming.NamingException, nc.bs.pub.SystemException {
	super();
}

/**
 * ������ʱ��
 * �������ڣ�(2001-6-12 17:03:15)
 * @author��wyan
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
 * ȡ�ø����ƽ̨��Ϣvo��Ϣ��
 * �������ڣ�(2000-12-18 16:41:55)
 * @param infoVO nc.vo.arap.verify.DJCLBVO
 */
public ForDapMsgParaVO getDataForDapMsgVO(DJCLBVO infoVO) throws Exception {

	/*************************************************************/
	// ������ϵͳ����ӿڣ�
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

		/*��Ϊ���˱��ַ��ؽ�����п��ܳ��ֶ������(��������ֺ���)*/
		/*��������ֻҪһ������Ϳ����ˣ�����ֺ���������*/
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
	// ������ϵͳ����ӿڣ�
	afterCallMethod("nc.bs.arap.verify.VerifyDMO", "getDataForDapMsgVO", new Object[]{infoVO});
	/*************************************************************/

	return paraVO;
}
/**
 * ������ݿ����ӡ�
 * �������ڣ�(2003-7-31 14:58:20)
 * @return java.sql.Connection
 */
public Connection getDBConnection() throws Exception {

	return getConnection();
}

/**
 * �˴����뷽��������
 * �������ڣ�(2004-3-25 22:21:36)
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
        /*������������***************************************************************/
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
        /*������������***************************************************************/

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
 * �˴����뷽��������
 * �������ڣ�(2004-3-25 22:21:36)
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
        /*�跽��������***************************************************************/
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
        /*�跽��������***************************************************************/
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
 * ��Ҫ���ܣ�ȡ�õ�ǰ��λ�����CLID--�������ɵ����������¼CLID�ڱ���λΨһ��
 * ��Ҫ�㷨��
 * �쳣������
 * �������ڣ�(2001-7-9 20:02:25)
 * ����޸����ڣ�(2001-7-9 20:02:25)
 * @author��wyan
 * @return int
 * @param sDwbm java.lang.String
 */
public int getVerifyClid(String sDwbm) throws Exception {

	/*************************************************************/
	// ������ϵͳ����ӿڣ�
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
	// ������ϵͳ����ӿڣ�
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
 * ��������̨VO��ֵ��
 * �������ڣ�(2001-5-30 10:24:05)
 * @author��wyan
 * @return java.util.Vector
 * @param rsbackback java.sql.ResultSet
 */
private Vector<VerifyBackDataVO> onDpToBackSo(ResultSet rsback) throws Exception {

	Vector<VerifyBackDataVO> vBack = new Vector<VerifyBackDataVO>();

	while (rsback.next()) {
		VerifyBackDataVO vBackData = new VerifyBackDataVO();
		// ѡ���־ :
		int XZBZ = -1;
		vBackData.setBz(XZBZ);
		// ����ID :
		String VOUCHID = rsback.getString(1);
		vBackData.setVouchid(VOUCHID == null ? null : VOUCHID.trim());
		// ����ID :
		String FBOID = rsback.getString(2);
		vBackData.setFboid(FBOID == null ? null : FBOID.trim());
		// ����Э���ID :
		String FKXYBOID = rsback.getString(3);
		vBackData.setFkxyboid(FKXYBOID == null ? null : FKXYBOID.trim());
		// ҵ����� :
		String YWBM = rsback.getString(4);
		vBackData.setYwbm(YWBM == null ? null : YWBM.trim());
		// ���ݴ��� :
		String DJDL = rsback.getString(5);
		vBackData.setDjdl(DJDL == null ? null : DJDL.trim());
		// ���ݱ�� :
		String DJBH = rsback.getString(6);
		vBackData.setDjbh(DJBH == null ? null : DJBH.trim());
		// ��¼��� :
		Integer FLBH = (Integer) rsback.getObject(7);
		vBackData.setFlbh(FLBH == null ? null : FLBH.toString().trim());
		// �������� :
		String DJRQ = rsback.getString(8);
		vBackData.setDjrq(DJRQ == null ? null : DJRQ.trim());
		// ������� :
		String SHRQ = rsback.getString(9);
		vBackData.setShrq(SHRQ == null ? null : SHRQ.trim());
		/****************************************************/
		//��Ч����
		String EffectDate = rsback.getString(10);
		vBackData.setEffectDate(EffectDate == null ? null : EffectDate.trim());
		//��������
		String OrderCusmanDoc  = rsback.getString(11);
		vBackData.setOrderCusmanDoc(OrderCusmanDoc == null ? null : OrderCusmanDoc.trim());
		/****************************************************/
		// ���õ����� :
		String XYDQR = rsback.getString(12);
		vBackData.setXydqr(XYDQR == null ? null : XYDQR.trim());
		// ���� :
		BigDecimal DJ = (BigDecimal) rsback.getObject(13);
		vBackData.setDj(DJ == null ? null : new UFDouble(DJ));
		// ԭ����� :
		BigDecimal YBYE = (BigDecimal) rsback.getObject(14);
		vBackData.setYbye(YBYE == null ? null : new UFDouble(YBYE));
		// ������� :
		BigDecimal FBYE = (BigDecimal) rsback.getObject(15);
		vBackData.setFbye(FBYE == null ? null : new UFDouble(FBYE));
		// ������� :
		BigDecimal BBYE = (BigDecimal) rsback.getObject(16);
		vBackData.setBbye(BBYE == null ? null : new UFDouble(BBYE));
		
		vBackData.setTs(rsback.getString(17));
		
		vBackData.setBzbm(rsback.getString(18));
		
		// ԭ���ۿ� :
		UFDouble YBZK = new UFDouble(0);
		vBackData.setYbzk(YBZK);
		// �����ۿ� :
		UFDouble FBZK = new UFDouble(0);
		vBackData.setFbzk(FBZK);
		// �����ۿ� :
		UFDouble BBZK = new UFDouble(0);
		vBackData.setBbzk(BBZK);
		////wldxbm:(Ϊ�˽�����˺��������ʹ��)
		//String wldxbm = rsback.getString(17);
		//vBackData.setWldxbm(wldxbm == null ? "" : wldxbm.trim());

		vBack.addElement(vBackData);

	}
	return vBack;
}


/**
 * ����˵���� ���ڴ����Ľ��ֻ����һ���������ʷ�����Ϊ ��+��
 * ע��������������Ż�
 *�������ڣ�(2001-10-11 9:30:38)
 * @param vo nc.vo.arap.verify.DJCLBVO
 */
public void updateDJFB(Hashtable<String, VerifyUpdateBillBalanceVO> hdjclje,boolean isverify,String clrq) throws Exception {

    String sql = "";
    String lock = "update arap_djzb set vouchid=vouchid where vouchid= ? ";//����������
    Connection con = null;
    PreparedStatement updatestmt = null;
    PreparedStatement lockstmt = null;

    /**�����*/
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
 * ����˵��:���ڴ����Ľ��ֻ����һ���������ʷ�����Ϊ ��+��
 * ��Ҫ�㷨��
 * ע�⣺������������Ż�
 * �������ڣ�(2001-10-11 9:33:14)
 * @param vo nc.vo.arap.verify.DJCLBVO
 */
public void updateDJFKXYB(Hashtable<String, VerifyUpdateBillBalanceVO> hdjclje) throws Exception{

	String sql = "";

	/**�����*/
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