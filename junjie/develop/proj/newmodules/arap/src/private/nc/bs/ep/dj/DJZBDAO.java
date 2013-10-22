/**
 * @(#)DJZBDAO.java	V5.0 2005-12-27
 *
 * Copyright 1988-2005 UFIDA, Inc. All Rights Reserved.
 *
 * This software is the proprietary information of UFSoft, Inc.
 * Use is subject to license terms.
 *
 */

package nc.bs.ep.dj;


import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import nc.bs.arap.global.InvokeBusitrans;
import nc.bs.arap.outer.IArapPayInterface;
import nc.bs.arap.pub.CreatJoinSQLTool;
import nc.bs.arap.pub.PubDAO;
import nc.bs.arap.pub.PubMethods;
import nc.bs.arap.util.SqlUtils;
import nc.bs.arap.verify.SystemProfile;
import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Log;
import nc.bs.logging.Logger;
import nc.bs.pub.SystemException;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.PersistenceManager;
import nc.jdbc.framework.SQLParameter;
import nc.jdbc.framework.crossdb.CrossDBConnection;
import nc.jdbc.framework.exception.DbException;
import nc.jdbc.framework.processor.ArrayProcessor;
import nc.jdbc.framework.processor.ColumnListProcessor;
import nc.jdbc.framework.processor.ResultSetProcessor;
import nc.vo.arap.exception.ExceptionHandler;
import nc.vo.arap.global.BusiTransVO;
import nc.vo.arap.global.CurTime;
import nc.vo.arap.global.DjVOTreaterAid;
import nc.vo.arap.mapping.ArapBaseMappingMeta;
import nc.vo.arap.mapping.Arap_djfbVOMeta;
import nc.vo.arap.mapping.Arap_djzbVOMeta;
import nc.vo.arap.mapping.Arap_fengcunVOMeta;
import nc.vo.arap.mapping.Arap_itemVOMeta;
import nc.vo.arap.mapping.Arap_item_bVOMeta;
import nc.vo.arap.mapping.IArapMappingMeta;
import nc.vo.arap.pub.ArapBusinessException;
import nc.vo.arap.pub.ArapConstant;
import nc.vo.arap.pub.PowerCtrlVO;
import nc.vo.arap.pub.QryCondArrayVO;
import nc.vo.arap.pub.QryCondVO;
import nc.vo.arap.util.VOCompress;
import nc.vo.ep.dj.DJFBItemVO;
import nc.vo.ep.dj.DJWszzVO;
import nc.vo.ep.dj.DJZBHeaderVO;
import nc.vo.ep.dj.DJZBItemVO;
import nc.vo.ep.dj.DJZBVO;
import nc.vo.ep.dj.DJZBVOConsts;
import nc.vo.ep.dj.DjCondVO;
import nc.vo.ep.dj.DjfkxybVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;

import org.apache.commons.lang.ArrayUtils;



/**
 * <p>
 * 单据主表（ARAP_DJZB）、辅表(ARAP_DJFB)、自由项（Arap_FREE）持久化机制接口
 * </p>
 * <p>
 * <Strong>主要的类使用：</Strong>
 *  <ul>
 * 		<li>如何使用该类</li>
 *      <li>是否线程安全</li>
 * 		<li>并发性要求</li>
 * 		<li>使用约束</li>
 * 		<li>其他</li>
 * </ul>
 * </p>
 * <p>
 * <Strong>已知的BUG：</Strong>
 * 	<ul>
 * 		<li></li>
 *  </ul>
 * </p>
 *
 * <p>
 * <strong>修改历史：</strong>
 * 	<ul>
 * 		<li><ul>
 * 			<li><strong>修改人:</strong>rocking</li>
 * 			<li><strong>修改日期：</strong>2005-12-27</li>
 * 			<li><strong>修改内容：<strong></li>
 * 			</ul>
 * 		</li>
 * 		<li>
 * 		</li>
 *  </ul>
 * </p>
 *
 * @author rocking
 * @version V5.0
 * @since V3.1
 */

public class DJZBDAO {
    private BaseDAO basedao=new BaseDAO();
    private PubDAO pubdao=new PubDAO();
    private DjfkxybDAO fkdao=new DjfkxybDAO();
    private FreeDAO defdao=new FreeDAO();
    private Arap_djfbVOMeta fbmeta=new Arap_djfbVOMeta();
    private Arap_djzbVOMeta zbmeta= new Arap_djzbVOMeta();
    private Arap_itemVOMeta ssmeta=new Arap_itemVOMeta();
    private Arap_item_bVOMeta bbmeta=new Arap_item_bVOMeta();

    /**
     *
     * 2005-12-27
     * @author:rocking
     */
    public DJZBDAO() {
        super();
        //
    }


    public static void checkDataRemove(String sql) throws BusinessException {
    	if(!sql.trim().startsWith("where") && !sql.trim().startsWith("select")){
    		sql=" where "+sql;
    	}
    	
    	String querySql = null;
    	
		if( !sql.trim().startsWith("select")){
    		querySql="select vouchid from arap_dataremove "+sql;
    	}else{
    		querySql=sql;
    	}

    	querySql=nc.vo.jcom.lang.StringUtil.replaceIgnoreCase(querySql,"arap_djzb." , "");
    	querySql=nc.vo.jcom.lang.StringUtil.replaceIgnoreCase(querySql,"arap_djfb." , "");
    	querySql=nc.vo.jcom.lang.StringUtil.replaceIgnoreCase(querySql,"zb." , "");
    	querySql=nc.vo.jcom.lang.StringUtil.replaceIgnoreCase(querySql,"fb." , "");

    	Object[] objs=null;
    	try{
    		objs = (Object[]) new BaseDAO().executeQuery(querySql, new ArrayProcessor());
    	}catch (Exception e) {
    		ExceptionHandler.consume(e);
		}

    	if (!ArrayUtils.isEmpty(objs) && objs[0] != null) {
    		throw new BusinessException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("2006","UPP2006-v56-000079")/*@res "查询单据错误，数据已经被卸载！"*/);
    	}

    }

    /**
     * 通过主键查找一个VO对象。
     *
     * 创建日期：(2000-10-9)
     * @return nc.vo.ep.dj.DJZBHeaderVO
     * @param key String
     * @throws SQLException
     * @throws BusinessException 
     */
    public DJZBHeaderVO findHeaderByPrimaryKey(String key) throws SQLException, BusinessException {
//        nc.bs.arap.global.PubBO pbo = new nc.bs.arap.global.PubBO();
//        String pk_corp = "1001";
//        pk_corp=findpk_corpByPrimaryKey(key);
//        boolean isUsedGL = pbo.glIsUsed(pk_corp, "GL");
        boolean isUsedGL = false;
        String sqlFromClause = " from ARAP_DJZB zb where zb.dr=0 and zb.vouchid = '"+key+"'";
//        if (isUsedGL) {
//            sqlFromClause = " from ARAP_DJZB zb "
//                    + " left outer join dap_finindex on zb.vouchid=substring(dap_finindex.procmsg,1,20) and dap_finindex.dr=0"
//                    + " left outer join gl_voucher on gl_voucher.pk_voucher=dap_finindex.pk_vouchentry and gl_voucher.dr=0"
//                    + " left outer join bd_vouchertype on bd_vouchertype.pk_vouchertype=gl_voucher.pk_vouchertype"
//                    + " where ARAP_DJZB.dr=0 and ARAP_DJZB.vouchid = '"+key+"'";
//        }

        DJZBHeaderVO dJZBHeader = null;
        Vector vHead = this.getDJZBHeaderVOsUniversalVector(sqlFromClause,isUsedGL,null);
            if(vHead.size()>0)
                dJZBHeader = (DJZBHeaderVO)vHead.elementAt(0);
            else{
            	String sql="vouchid = '"+key+"'";
            	checkDataRemove(sql);
            }
        return dJZBHeader;
    }

    public Vector<DJZBHeaderVO> findHeaderByPrimaryKeys(String[] keys) throws DAOException, SQLException, DbException {
    	List<String> ls = new ArrayList<String>();
    	for(String key : keys){
    		if(null != key && !ls.contains(key)){
    			ls.add(key);
    		}
    	}
    	keys = ls.toArray(new String[]{});
        String sqlFromClause=null;
        if(keys.length>150)
        {
        	String table= createHeaderTempTable( keys);
        	sqlFromClause=" from ARAP_DJZB zb inner join "+table+" temp on zb.vouchid=temp.vouchid_temp where zb.dr=0";
        }
        else if(keys.length==0)
        {
        	return new Vector<DJZBHeaderVO>();
        }
        else
        {
        	sqlFromClause= " from ARAP_DJZB zb where zb.dr=0 and zb.vouchid in( ";
        	StringBuffer cause=new StringBuffer();
            for(int i=0;i<keys.length;i++)
            {
            	if(i==keys.length-1)
            	{
            		cause.append("'"+keys[i]+"')");
            	}
            	else
            	{
            		cause.append("'"+keys[i]+"',");
            	}
            }
            sqlFromClause=sqlFromClause+cause.toString();
        }
        boolean isUsedGL = false;


//        if (isUsedGL) {
//            sqlFromClause = " from ARAP_DJZB zb "
//                    + " left outer join dap_finindex on zb.vouchid=substring(dap_finindex.procmsg,1,20) and dap_finindex.dr=0"
//                    + " left outer join gl_voucher on gl_voucher.pk_voucher=dap_finindex.pk_vouchentry and gl_voucher.dr=0"
//                    + " left outer join bd_vouchertype on bd_vouchertype.pk_vouchertype=gl_voucher.pk_vouchertype"
//                    + " where ARAP_DJZB.dr=0 and ARAP_DJZB.vouchid = '"+key+"'";
//        }

        Vector<DJZBHeaderVO> vHead=null;

            vHead = this.getDJZBHeaderVOsUniversalVector(sqlFromClause, isUsedGL,null);

        return vHead;
    }
    /**
     *
     *
     * 创建日期：(2000-10-9)
     * @param key nc.vo.pub.oid.OID
     * @exception java.sql.SQLException 异常说明。
     * @throws DAOException
     */
    public void cancel_Close_SS(DJZBVO vo) throws   DAOException {
     	String ts = CurTime.getCurrentTimeStampString();
      	DJZBHeaderVO head=(DJZBHeaderVO)vo.getParentVO();
      	String cond="vouchid= '"+head.getVouchid()+"' and ts='"+head.getts()+"'";
        head.setTs(new UFDateTime(ts));
        head.setDjzt(new Integer(2));
        head.setZdr(null);
        head.setZdrq(null);
        pubdao.updateObjectPartly(head,ssmeta,new String[]{"djzt","zdr","zdrq","ts"},cond);
    }
    /**
     *author      : wangqiang
     *create time : 2004-08-05
     *function    : unclose a ss bill item
     * @exception java.sql.SQLException 异常说明。
     * @throws DAOException
     */
    public void cancel_Close_SSItem(DJZBItemVO itemVO) throws   DAOException {
      	String cond="fb_oid='"+itemVO.getFb_oid()+"'";
        pubdao.updateObjectPartly(itemVO,bbmeta,new String[]{"closer","closedate" },cond);
    }
    /**事项审批单余额为0则自动关闭
     *
     *
     * 创建日期：(2000-10-9)
     * @param key nc.vo.pub.oid.OID
     * @exception java.sql.SQLException 异常说明。
     * @throws DAOException
     */
    public void close_item(String vouchid,String zdr,UFDate zdrq) throws java.sql.SQLException, DAOException {
        DJZBHeaderVO item=new DJZBHeaderVO();
        item.setDjzt(new Integer(5));
        item.setZdr(zdr);
        item.setZdrq(zdrq);
      	String cond="vouchid='"+vouchid+"' "
    	+" and not exists ( select vouchid from arap_item_b where arap_item_b.vouchid=arap_item.vouchid and closer is null)";
        pubdao.updateObjectPartly(item,ssmeta,new String[]{"djzt","zdr","zdrq" },cond);
     }
    /**
     *
     *
     * 创建日期：(2000-10-9)
     * @param key nc.vo.pub.oid.OID
     * @exception java.sql.SQLException 异常说明。
     * @throws DAOException
     */
    public void close_SS(DJZBVO vo) throws java.sql.SQLException, DAOException {

    	/*************************************************************/
    	// 保留的系统管理接口：
    	//beforeCallMethod("nc.bs.ep.dj.DJZBDMO", "close_SS", new Object[]{vo});

    	/*************************************************************/
     	DJZBHeaderVO head=(DJZBHeaderVO)vo.getParentVO();
     	String ts = CurTime.getCurrentTimeStampString();
     	head.setDjzt(new Integer(5));
        String cond="vouchid='"+head.getVouchid()+"' and ts='"+head.getts()+"'";
        head.setTs(new UFDateTime(ts));
        pubdao.updateObjectPartly(new DJZBHeaderVO[]{head},ssmeta,new String[]{"djzt","zdr","zdrq","ts" },cond);
    }
    /**
     *author: wangqiang
     *create time : 2004-08-05
     *function    :close the ss bill item
     * @exception java.sql.SQLException 异常说明。
     * @throws DAOException
     */
    public void close_SSItem(DJZBItemVO[] itemVO) throws java.sql.SQLException, DAOException {

    	/*************************************************************/
    	// 保留的系统管理接口：
    	//beforeCallMethod("nc.bs.ep.dj.DJZBDMO", "close_SSItem", new Object[]{itemVO});

    	/*************************************************************/
        pubdao.updateObjectPartly(  itemVO ,bbmeta,new String[]{"closer","closedate" },null);
    	/*************************************************************/
    	// 保留的系统管理接口：
    	//afterCallMethod("nc.bs.ep.dj.DJZBDMO", "close_SSItem", new Object[]{itemVO});
    	/*************************************************************/
    }
    public void deleteDjByPks(String[] pks) throws BusinessException{
    	PubDAO dao=new PubDAO();
    	dao.deleteVOsByPks(this.zbmeta,pks);
    }
    /**
     * <p>删除母子表的所有内容。
     * <p>
     * 创建日期：(2000-10-9)
     * @param key String
     * @exception java.sql.SQLException 异常说明。
     * @throws DAOException
     * @throws SQLException
     */
    public void delete(DJZBVO vo) throws   BusinessException, SQLException {

        /*************************************************************/
        // 保留的系统管理接口：
        //beforeCallMethod("nc.bs.ep.dj.DJZBDMO", "delete", new Object[] { vo });
        /*************************************************************/
        //删除DJFB
        deleteItemsForHeader(((DJZBHeaderVO) vo.getParentVO()).getPrimaryKey());
        //删除DJZB
        deleteHeader((DJZBHeaderVO) vo.getParentVO());

        /*************************************************************/
        // 保留的系统管理接口：
        //afterCallMethod("nc.bs.ep.dj.DJZBDMO", "delete", new Object[] { vo });
        /*************************************************************/
    }
    /**
     * <p>删除母子表的所有内容。
     * <p>
     * 创建日期：(2000-10-9)
     * @param key String
     * @exception java.sql.SQLException 异常说明。
     * @throws DAOException
     */
    public void delete_SS(DJZBVO vo) throws java.sql.SQLException, BusinessException {

    	/*************************************************************/
    	// 保留的系统管理接口：
    	//beforeCallMethod("nc.bs.ep.dj.DJZBDMO", "delete", new Object[]{vo});
    	/*************************************************************/
    	//删除DJFB
    	deleteItemsForHeader_SS(((DJZBHeaderVO)vo.getParentVO()).getPrimaryKey());
    	//删除DJZB
    	deleteHeader_SS((DJZBHeaderVO)vo.getParentVO());

    	/*************************************************************/
    	// 保留的系统管理接口：
    	//afterCallMethod("nc.bs.ep.dj.DJZBDMO", "delete", new Object[]{vo});
    	/*************************************************************/
    }

    /**
     * 根据主键在数据库中删除一个VO对象。
     *
     * 创建日期：(2000-10-9)
     * @param key nc.vo.pub.oid.OID
     * @exception java.sql.SQLException 异常说明。
     * @throws DAOException
     */
    public void deleteHeader(DJZBHeaderVO vo) throws   BusinessException {

    	/*************************************************************/
    	// 保留的系统管理接口：
    	//beforeCallMethod("nc.bs.ep.dj.DJZBDMO", "deleteHeader", new Object[]{vo});
    	/*************************************************************/
        pubdao.deleteVOsByPks(zbmeta,new String[]{vo.getPrimaryKey()});
    	/*************************************************************/
    	// 保留的系统管理接口：
    	//afterCallMethod("nc.bs.ep.dj.DJZBDMO", "deleteHeader", new Object[]{vo});
    	/*************************************************************/
    }
    /**
     * 根据主键在数据库中删除一个VO对象。
     *
     * 创建日期：(2000-10-9)
     * @param key nc.vo.pub.oid.OID
     * @exception java.sql.SQLException 异常说明。
     * @throws DAOException
     */
    public void deleteHeader_SS(DJZBHeaderVO vo) throws  BusinessException {
    	/*************************************************************/
    	// 保留的系统管理接口：
    	//beforeCallMethod("nc.bs.ep.dj.DJZBDMO", "deleteHeader", new Object[]{vo});
    	/*************************************************************/
        pubdao.deleteVOsByPks(ssmeta,new String[]{vo.getPrimaryKey()});
    	/*************************************************************/
    	// 保留的系统管理接口：
    	//afterCallMethod("nc.bs.ep.dj.DJZBDMO", "deleteHeader", new Object[]{vo});
    	/*************************************************************/
    }
    /**
     * 根据主键在数据库中删除一个VO对象。同时删除自由项,付款协议表.
     *
     * 创建日期：(2000-10-9)
     * @param key nc.vo.pub.oid.OID
     * @exception java.sql.SQLException 异常说明。
     * @throws DAOException
     */
    public void deleteItem(DJZBItemVO vo) throws  BusinessException {

    	/*************************************************************/
    	// 保留的系统管理接口：
    	//beforeCallMethod("nc.bs.ep.dj.DJZBDMO", "deleteItem", new Object[]{vo});

    	/*************************************************************/
//      删除自由项
    	defdao.deleteFreesForFB(vo.getFb_oid());
//     删除付款协议表
    	fkdao.deleteFkxyforFB(vo.getFb_oid());
    //
    	pubdao.deleteVOsByPks( fbmeta,new String[]{vo.getFb_oid()});

    	/*************************************************************/
    	// 保留的系统管理接口：
    	//afterCallMethod("nc.bs.ep.dj.DJZBDMO", "deleteItem", new Object[]{vo});
    	/*************************************************************/
    }
    /**
     * 根据主键在数据库中删除一个VO对象。同时删除自由项,付款协议表.
     *
     * 创建日期：(2000-10-9)
     * @param key nc.vo.pub.oid.OID
     * @exception java.sql.SQLException 异常说明。
     * @throws DAOException
     */
    public void deleteItem_SS(DJZBItemVO vo) throws BusinessException {

    	/*************************************************************/
    	// 保留的系统管理接口：
    	//beforeCallMethod("nc.bs.ep.dj.DJZBDMO", "deleteItem", new Object[]{vo});

    	/*************************************************************/
//      删除自由项
    	defdao.deleteFreesForFB(vo.getFb_oid());
////     删除付款协议表
    	//deleteFkxyforFB(vo.getFb_oid());
//    //
    	pubdao.deleteVOsByPks(bbmeta,new String[]{vo.getFb_oid()});


    	/*************************************************************/
    	// 保留的系统管理接口：
    	//afterCallMethod("nc.bs.ep.dj.DJZBDMO", "deleteItem", new Object[]{vo});
    	/*************************************************************/
    }
    /**
     * 根据主键在数据库中删除一个VO对象。同时删除自由项,付款协议表.
     *
     * 创建日期：(2000-10-9)
     * @param key nc.vo.pub.oid.OID
     * @exception java.sql.SQLException 异常说明。
     * @throws DAOException
     */
    public void deleteItemPK(String Fb_oid) throws java.sql.SQLException, BusinessException {

    	/*************************************************************/
    	// 保留的系统管理接口：
    	//beforeCallMethod("nc.bs.ep.dj.DJZBDMO", "deleteItem", new Object[]{Fb_oid});
    	/*************************************************************/
//      删除自由项
    	defdao.deleteFreesForFB(Fb_oid);
//     删除付款协议表
    	fkdao.deleteFkxyforFB(Fb_oid);
    //
    	pubdao.deleteVOsByPks(fbmeta,new String[]{Fb_oid});

    	/*************************************************************/
    	// 保留的系统管理接口：
    	//afterCallMethod("nc.bs.ep.dj.DJZBDMO", "deleteItem", new Object[]{Fb_oid});
    	/*************************************************************/
    }
    /**
     * 根据主键在数据库中删除一个VO对象根据主键在数据库中删除一个VO对象。同时删除自由项,付款协议表.。
     *
     * 创建日期：(2000-10-9)
     * @param key nc.vo.pub.oid.OID
     * @exception java.sql.SQLException 异常说明。
     * @throws DAOException
     */
    public void deleteItemsForHeader(String headerKey) throws  DAOException {
//      删除自由项
        defdao.deleteFreesForZB(headerKey);
//     删除付款协议表
        fkdao.deleteFkxyforZB(headerKey);
        pubdao.deleteVOsByWhere(fbmeta,"WHERE VOUCHID = '"+headerKey+"'");
    }
    /**
     * 根据主键在数据库中删除一个VO对象根据主键在数据库中删除一个VO对象。同时删除自由项,付款协议表.。
     *
     * 创建日期：(2000-10-9)
     * @param key nc.vo.pub.oid.OID
     * @exception java.sql.SQLException 异常说明。
     * @throws DAOException
     */
    public void deleteItemsForHeader_SS(String headerKey) throws   BusinessException {

//      删除自由项
    	defdao.deleteFreesForZB(headerKey);
        pubdao.deleteVOsByPks(bbmeta,new String[]{headerKey});
    }
    /**
     * 锁定行，并判断数据是否已经被改动
     *
     * 创建日期：(2004-04-13)
     * @param key nc.vo.pub.oid.OID
     * @exception java.sql.SQLException 异常说明。
     作者：陈飞
     * @throws DbException
     */
    public void distributeDjzb_cf(String vouchid, String ts, String djdl,Integer iDjzt)
        throws java.sql.SQLException, ArapBusinessException, DbException {

        String tableName = "arap_djzb";
        if (djdl.equals("ss"))
            tableName = "arap_item";
        String sql =
            "update   "
                + tableName
                + "  set dr=dr  where vouchid ='"
                + vouchid
                + "' and ts='"
                + ts
                + "'";
        int count = 0;
        ArapBusinessException dis = new ArapBusinessException();
        nc.vo.arap.global.ResMessage res = new nc.vo.arap.global.ResMessage();

        res.strMessage = nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("2006030102","UPP2006030102-000503")/*@res "并发异常,数据已经被更新"*/;
        res.isSuccess = false;
        PersistenceManager pm=null;
        CrossDBConnection con = null;
		PreparedStatement stmt = null;
        try{
            pm = PersistenceManager.getInstance(InvocationInfoProxy.getInstance().getUserDataSource());
			con = (CrossDBConnection)pm.getJdbcSession().getConnection();
			con.setAddTimeStamp(false);
			stmt = con.prepareStatement(sql);
			count = stmt.executeUpdate();
        	} finally {
        	    pm.release();
        	}
        	if (count <= 0) {
                dis = new ArapBusinessException(nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("2006030102","UPP2006030102-000503")/*@res "并发异常,数据已经被更新"*/);
                res.strMessage = nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("2006030102","UPP2006030102-000503")/*@res "并发异常,数据已经被更新"*/;
                dis.m_ResMessage = res;
                throw dis;
            }
      }
    /**
     * 数据是否已经被改动
     *
     * 创建日期：(2000-10-9)
     * @param key nc.vo.pub.oid.OID
     * @exception java.sql.SQLException 异常说明。
     * @throws DbException
     */
    public String distributeDjzb_Item(String vouchid, String ts,String djdl)
    	throws java.sql.SQLException, ArapBusinessException, DbException {
    	//distributeDjzb_lock(vouchid,ts,djdl);
    	String newts = CurTime.getCurrentTimeStampString();
    	String tableName="arap_djzb";
    	if(djdl.equals("ss"))
    		tableName="arap_item";
    	String sql =
    		"update "+tableName+" set ts='"+newts+"'  where vouchid ='"
    			+ vouchid
    			+ "' and ts='"
    			+ ts
    			+ "'";
    	ArapBusinessException dis = new ArapBusinessException();
    	nc.vo.arap.global.ResMessage res = new nc.vo.arap.global.ResMessage();
    	res.strMessage = nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("2006030102","UPP2006030102-000503")/*@res "并发异常,数据已经被更新"*/;
    	res.isSuccess=false;
    	PersistenceManager pm=null;
        try{
            pm=PersistenceManager.getInstance(getds());
            int iCount= pm.getJdbcSession().executeUpdate(sql );
            if (iCount<=0) {
    		    dis=new ArapBusinessException(nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("2006030102","UPP2006030102-000503")/*@res "并发异常,数据已经被更新"*/);
    			res.strMessage =nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("2006030102","UPP2006030102-000503")/*@res "并发异常,数据已经被更新"*/;
    			dis.m_ResMessage = res;
    			throw dis;
    		}
        	} finally {
        	    pm.release();
        	}
    	return newts;
    }

    /**
     * 是否存业务员未关闭的当前类型的事项审批单
     *
     * 创建日期：(2001-6-6)
     * @return nc.vo.ep.dj.DjfkxybVO
     * @param key String
     * @exception java.sql.SQLException 异常说明。
     * @throws DbException
     */
    public boolean exist_ss(
        String pk_corp,
        String[] ywybm,
        String billCode,
        String vouchid)
        throws SQLException, DbException {
        String cond = "";
        if (vouchid != null && vouchid.trim().length() > 0)
            cond = " and arap_item_b.vouchid<>'" + vouchid + "'";
//        String ywy = "";
//        for (int i = 0; i < ywybm.length; i++) {
//            if (i == ywybm.length - 1)
//                ywy = ywy + "'" + ywybm[i] + "'";
//            else
//                ywy = ywy + "'" + ywybm[i] + "',";
//        }
        String sql =
            "select  count(*) from arap_item"
                + " inner join arap_item_b on arap_item_b.vouchid=arap_item.vouchid "
                + " where " +SqlUtils.getInStr("arap_item_b.ywybm", ywybm)
//                		"arap_item_b.ywybm in ( "
//                + ywy
//                + ")"

                + " and arap_item.dr=0 and arap_item_b.ybye>0 and arap_item_b.dr=0 and djzt<5 and arap_item.djlxbm='"
                + billCode
                + "' and  arap_item.dwbm='"
                + pk_corp
                + "'"
                + cond;

        boolean b = false;
        Integer ret=null;
        PersistenceManager pm=null;
        try{
            pm=PersistenceManager.getInstance(getds());
            ret=(Integer) pm.getJdbcSession().executeQuery(sql,new ResultSetProcessor(){

                /**
				 *
				 */
				private static final long serialVersionUID = 1635971580636118008L;

				public Object handleResultSet(ResultSet rs) throws SQLException {
                    //
                    Integer i = null;
                    if (rs.next()) {
                        // count :
                        i =new Integer( rs.getInt(1));
                    }
                    return i;
                }

            });

        	} finally {
        	    pm.release();
        	}
        if (ret.intValue() > 0)
            b = true;
        return b;
    }

    /**
     * 通过主键查找一个VO对象。
     *
     * 创建日期：(2001-6-6)
     * @return nc.vo.ep.dj.DjfkxybVO
     * @param key String
     * @exception java.sql.SQLException 异常说明。
     * @throws DbException
     */
    public boolean  existByBm(String billCode,String pk_corp ) throws SQLException, DbException {

    	String sql = "select  count(*) from arap_item where dr=0 and djlxbm='"+billCode+"' and  dwbm='"+pk_corp+"'";

    	boolean b=false;
    	Integer i=null;
    	PersistenceManager pm=null;
        try{
            pm=PersistenceManager.getInstance(getds());
            i=(Integer)pm.getJdbcSession().executeQuery(sql,new  ResultSetProcessor(){

                /**
				 *
				 */
				private static final long serialVersionUID = 2244036624513966990L;

				public Object handleResultSet(ResultSet rs) throws SQLException {
                    //
                    if (rs.next()) {
            			// count :
            			return new Integer(rs.getInt(1));
            		}
                    return new Integer(0);
                }
             });
     		//
    	}
    	finally {
    	 pm.release();
    	}
    	if(i.intValue()>0)
    		b=true;
    	return b;
    }
    /**
     * 事项审批单据是否被单据引用
     *
     * 创建日期：(2001-6-6)
     * @return nc.vo.ep.dj.DjfkxybVO
     * @param key String
     * @exception java.sql.SQLException 异常说明。
     * @throws DbException
     */
    public boolean  existByKey(String key) throws SQLException, DbException {
    	/*************************************************************/
    	// 保留的系统管理接口：
    	//beforeCallMethod("nc.bs.ep.dj.DJZBDMO", "existByKey", new Object[]{key});
    	/*************************************************************/
//    	String sql = "select  count(item_bill_pk) from arap_djfb where  item_bill_pk='"+key+"' and dr=0" ;
    	String sql="select count(clb.item_fbpk) from arap_item_clb clb inner join arap_item_b itemb on " +
    			"clb.item_fbpk=itemb.fb_oid where itemb.vouchid='"+key+"' and clb.dr=0 and itemb.dr=0";
    	boolean b=false;
    	Integer count;
    	PersistenceManager pm=null;
        try{
            pm=PersistenceManager.getInstance(getds());
            count=(Integer)  pm.getJdbcSession().executeQuery(sql,new ResultSetProcessor(){

                /**
				 *
				 */
				private static final long serialVersionUID = -1955268192606058190L;

				public Object handleResultSet(ResultSet arg0) throws SQLException {
                    //
                    if (arg0.next()) {
            			// count :
            			return  new Integer ( arg0.getInt(1));
            		}
                    return null;
                }
            });
        }
//            catch(SQLException e){
//            Logger.warn(e,this.getClass(),e.getMessage());
//            throw new BusinessRuntimeException(e.getMessage());
//        }
        finally{
            pm.release();
        }
    	if(null!=count&&count.intValue()>0)
    		b=true;
    	return b;
    }
    /**
     * <p>根据表头的主键查询一个VO。
     * <p>
     * 创建日期：(2000-10-9)
     * @param key ??dbFieldType??
     * @throws SQLException
     * @throws DAOException
     */
    public DJZBVO[] findByPk_bankrecive(String key) throws DAOException, SQLException {
        DJZBVO[] vos = null;
        //
        DJZBHeaderVO[] headers = findHeaderByPk_bankrecive(key);
        Vector<DJZBVO> v = new Vector<DJZBVO>();
        if (headers != null) {
            for (int i = 0; i < headers.length; i++) {
                DJZBItemVO[] items = findItemsForHeader(headers[i].getPrimaryKey());
                DJZBVO vo = new DJZBVO();
                vo.setParentVO(headers[i]);
                vo.setChildrenVO(items);
                v.addElement(vo);
            }
        }
        if (v.size() > 0) {
            vos = new DJZBVO[v.size()];
            v.copyInto(vos);
        }
        return vos;
    }
    /**
     * <p>根据表头的主键查询一个VO。
     * <p>
     * 创建日期：(2000-10-9)
     * @param key ??dbFieldType??
     * @throws SQLException
     * @throws DAOException
     */
    public DJZBVO findByPrimaryKey(String key) throws DAOException, SQLException,BusinessException {

    	DJZBVO vo = new DJZBVO();
    	//
    	DJZBHeaderVO header = findHeaderByPrimaryKey(key);
    	DJZBItemVO[] items = null;
    	if (header != null) {
    		items = findItemsForHeader(header.getPrimaryKey());
//    		List<String> ls = new ArrayList<String>();
//    		for(DJZBItemVO vo1 : items){
//    			ls.add(vo1.getFb_oid());
//    		}
//    		HashMap map = defdao.findItemvosForHeader(ls.toArray(new String[ls.size()]));
//    		for(int i = 0;i<items.length;i++){
//    			//查询自由项
//    			     //DJFBItemVO[] frees = defdao.findItemsForHeader (items[i].getFb_oid());
//    			     //items[i].items = frees;
//    			items[i].items = (DJFBItemVO[]) map.get(items[i].getFb_oid());
//    			}
    		fillFreeItems(items);
    	}
    	else
    	{
    		return null;
    	}
    	vo.setParentVO(header);
    	vo.setChildrenVO(items);

    	return vo;
    }
    @SuppressWarnings("unchecked")
	public DJZBVO[] findByPrimaryKeys(String[] keys) throws DAOException, SQLException,BusinessException, DbException {

        	if(null != keys && keys.length > 0){
        		Vector<DJZBHeaderVO> headers = findHeaderByPrimaryKeys(keys);
        		if(null==headers||headers.size()==0)
        			return null;
        		DJZBItemVO[] items = findItemsForHeaders(keys);
        		fillFreeItems(items);
        		return assembleDjzbVo(headers.toArray(new DJZBHeaderVO[]{}), items);
        	}
        	return null;

//        	HashMap<String, String> map = new HashMap<String,String>();
//           	for(int k=0,size=keys.length;k<size;k++){
//           		if(map.get(keys[k])!=null){
//           			continue;
//           		}else{
//           			map.put(keys[k],keys[k]);
//           		}
//           		DJZBVO vo = new DJZBVO();
//	           	DJZBHeaderVO header = findHeaderByPrimaryKey(keys[k]);
//	           	DJZBItemVO[] items = null;
//	           	if (header != null) {
//	           		items = findItemsForHeader(header.getPrimaryKey());
//	           		fillFreeItems(items);
////	        		List<String> ls = new ArrayList<String>();
////	        		for(DJZBItemVO vo1 : items){
////	        			ls.add(vo1.getFb_oid());
////	        		}
////	        		HashMap map1 = defdao.findItemvosForHeader(ls.toArray(new String[ls.size()]));
////	           		for(int i = 0;i<items.length;i++){
////	           			//查询自由项
//////	           			     DJFBItemVO[] frees = defdao.findItemsForHeader (items[i].getFb_oid());
//////	           			     items[i].items = frees;
////	        			items[i].items = (DJFBItemVO[]) map1.get(items[i].getFb_oid());
////	           			}
//	           	}
//	           	if(null!=header){
//			           	vo.setParentVO(header);
//			           	vo.setChildrenVO(items);
//			           	ret.add(vo);
//	           	}
//           	}
//
//           	return ret.size()==0?null:ret.toArray(new DJZBVO[]{});
           }



    public void fillFreeItems(DJZBItemVO[] items) throws BusinessException{
    	if(null==items)return ;
    	String[] ls = new String[items.length];
    	for(int i = 0 ;i < items.length;i++){
    		ls[i] = items[i].getVouchid();
    	}
		HashMap map1 = defdao.findItemvosForHeader(ls);
   		for(DJZBItemVO vo1 : items){
   			vo1.items = (DJFBItemVO[]) map1.get(vo1.getFb_oid());
   		}
    }


    public DJZBVO[] assembleDjzbVo(DJZBHeaderVO[] headers,DJZBItemVO[] items){
    	List<DJZBVO> ret=new ArrayList<DJZBVO>();
    	HashMap<String,ArrayList<DJZBItemVO>> hs = new HashMap<String,ArrayList<DJZBItemVO>>();

    	for(DJZBItemVO vo : items){
    		if(!hs.containsKey(vo.getVouchid())){
    			ArrayList<DJZBItemVO> bodyvos = new ArrayList<DJZBItemVO>();
    			bodyvos.add(vo);
    			hs.put(vo.getVouchid(), bodyvos);
    		}else{
    			hs.get(vo.getVouchid()).add(vo);
    		}
    	}
    	for(DJZBHeaderVO vo : headers){
    		DJZBVO djzbvo = new DJZBVO();
    		djzbvo.setParentVO(vo);
    		djzbvo.setChildrenVO((hs.get(vo.getVouchid())).toArray(new DJZBItemVO[]{}));
    		ret.add(djzbvo);
    	}
    	return ret.size()==0?null:ret.toArray(new DJZBVO[]{});
    }


    public String[] getVouchidsByHeadVo(DJZBHeaderVO[] headers){

    	List<String> ls = new ArrayList<String>();
    	for(DJZBHeaderVO headvo : headers){
    		String vouchid = headvo.getVouchid();
    		if(null != vouchid && !ls.contains(vouchid)){
    			ls.add(vouchid);
    		}
    	}
		return ls.size()==0? null:ls.toArray(new String[]{});
    }

    /**
     * <p>根据表头的主键查询一个VO。
     * <p>
     * 创建日期：(2000-10-9)
     * @param key ??dbFieldType??
     * @exception java.sql.SQLException 异常说明。
     * @throws DAOException
     */
    public DJZBVO findByPrimaryKey_SS(String key) throws   DAOException {

    	/*************************************************************/
    	// 保留的系统管理接口：
    	//beforeCallMethod("nc.bs.ep.dj.DJZBDMO", "findByPrimaryKey_SS", new Object[]{key});
    	/*************************************************************/

    	DJZBVO vo = new DJZBVO();
    	//
    	DJZBHeaderVO header = findHeaderByPrimaryKey_SS(key);
    	DJZBItemVO[] items = null;
    	if (header != null) {
    		items = findItemsForHeader_SS(header.getPrimaryKey());
    	}
    	//for(int i = 0;i<items.length;i++){
    	////查询自由项
    	     //DJFBItemVO[] frees = findFREEForZb(items[i].getFb_oid());
    	     //items[i].items = frees;
    	//}
    	vo.setParentVO(header);
    	vo.setChildrenVO(items);

    	/*************************************************************/
    	// 保留的系统管理接口：
    	//afterCallMethod("nc.bs.ep.dj.DJZBDMO", "findByPrimaryKey_SS", new Object[]{key});
    	/*************************************************************/
    	return vo;
    }

    /**
     * 通过主键查找一个VO对象。
     *
     * 创建日期：(2000-10-9)
     * @return nc.vo.ep.dj.DJZBItemVO
     * @param key String
     * @exception java.sql.SQLException 异常说明。
     * @throws DAOException
     */
    public DJZBItemVO[] finItem_Upgrade(String key) throws SQLException, DAOException {
        DJZBItemVO[] dJZBItems = null;
        String strWhereClause = " where dr=0 and item_bill_pk is not null and  vouchid ='"+key+"' order by flbh";
            dJZBItems = this.getDJZBItemVOUniversalArray( strWhereClause);
        return dJZBItems;
    }

    /**
     * 通过主键查找一个VO对象，查询出来的单据直接加锁
     *
     * 创建日期：(2000-10-9)
     * @return nc.vo.ep.dj.DJZBHeaderVO
     * @param key String ,operator操作员
     * @throws SQLException
     * @throws DAOException
     */
    public DJZBVO[] findDj_sell()
        throws nc.vo.ep.dj.ShenheException, DAOException, SQLException {

        String sqlFromClause = " from arap_djzb zb inner join arap_djfb fb on zb.vouchid=fb.vouchid"
                + " where  zb.dr=0"
                + " and fb.dr=0"
                + "  and fb.ybye>0"
                + " and zb.djzt=2"
                + " and zb.djdl='sk'";
        DJZBHeaderVO[] djzbheader = null;
//        nc.vo.ep.dj.ShenheException shE = new nc.vo.ep.dj.ShenheException();
            djzbheader = this.getDJZBHeaderVOsUniversalArray(sqlFromClause ,false,null);
        Vector<DJZBVO> v = new Vector<DJZBVO>();
        for (int i = 0; djzbheader!=null && i < djzbheader.length; i++) {
            DJZBVO vo = new DJZBVO();
            vo.setParentVO(djzbheader[i]);
            DJZBItemVO[] items = null;
            items = finItem_Upgrade(djzbheader[i].getVouchid());
            vo.setChildrenVO(items);
            v.addElement(vo);
        }
        DJZBVO[] vos = null;
        if (v.size() > 0) {
            vos = new DJZBVO[v.size()];
            v.copyInto(vos);
        }

        return vos;
    }
    /**
     * 通过主键查找一个VO对象。
     *
     * 创建日期：(2000-10-9)
     * @return nc.vo.ep.dj.DJZBHeaderVO
     * @param key String
     * @throws DbException
     * @throws SQLException
     * @throws DAOException
     */
//    1．	There is a shady bug in the method of DJZBDMO.” findDjByPrimaryKeys(String[] keys, String tablename)”. When the “cond” var exceed certain threshold , some error will occur.
//    Referring to queryDjLb_djcond,we call tackle it with temporary table technic

    public DJZBVO[] findDjByPrimaryKeys(String[] keys)
        throws DbException, DAOException, SQLException {

        String cond = "";
        for (int i = 0; i < keys.length; i++) {
            cond = cond + "'" + keys[i] + "'";
            if (i != keys.length - 1)
                cond = cond + ",";
        }
        String sqlFromClause =" from arap_djzb zb where zb.dr=0 and zb.vouchid in ( "
                + cond
                + " )"
                + " order by zb.vouchid";
        DJZBVO[] vos = null;
        Vector v = new Vector() ;
            //if too much keys appear in the IN sql clause,we revise the sql and use temp table mechnism
            if(keys.length>50){
                String strTempTableName = this.createHeaderTempTable( keys);
                sqlFromClause =" from arap_djzb zb,"+strTempTableName+" temp where zb.dr=0 and temp.vouchid_temp =zb.vouchid order by zb.vouchid";
            }
            v = this.getDJZBHeaderVOsUniversalVector(sqlFromClause ,false,null);
            vos = this.getDjVObyHeaders(v);
        return vos;
    }
    /**
     * 通过主键查找一个VO对象。
     *
     * 创建日期：(2000-10-9)
     * @return nc.vo.ep.dj.DJZBHeaderVO
     * @param key String
     * @exception java.sql.SQLException 异常说明。
     * @throws DAOException
     * @throws SQLException
     */
//    1．	There is a shady bug in the method of DJZBDMO.” findDjByPrimaryKeys(String[] keys, String tablename)”. When the “cond” var exceed certain threshold , some error will occur.
//    Referring to queryDjLb_djcond,we call tackle it with temporary table technic

    @SuppressWarnings("unchecked")
    public DJZBVO[] findDjByPrimaryKeys_SS(String[] keys)
        throws   DAOException, SQLException {
//        String cond = "";
//        for (int i = 0; i < keys.length; i++) {
//            cond = cond + "'" + keys[i] + "'";
//            if (i != keys.length - 1)
//                cond = cond + ",";
//        }
        String conds="dr=0 and  "+SqlUtils.getInStr("vouchid", keys)  + " order by vouchid";
        String conds1="dr=0 and   "+SqlUtils.getInStr("vouchid", keys)  + " order by vouchid,flbh";
        List<DJZBHeaderVO> zb=(List<DJZBHeaderVO>)pubdao.queryVOsByWhereClause(DJZBHeaderVO.class,ssmeta,null,conds);
        List<DJZBItemVO> fb=(List<DJZBItemVO>)pubdao.queryVOsByWhereClause(DJZBItemVO.class,bbmeta,null,conds1);
        DJZBHeaderVO[] headers=(DJZBHeaderVO[])zb.toArray(new DJZBHeaderVO[]{});
        DJZBItemVO[]  dJZBItems=  (DJZBItemVO[])fb.toArray(new DJZBItemVO[]{});
        DJZBVO[] vos=null;
        //分配
        if (headers != null
            && headers.length > 0
            && dJZBItems != null
            && dJZBItems.length > 0) {
            vos = new DJZBVO[headers.length];
            int k = 0;
            for (int i = 0; i < headers.length; i++) {
                DJZBVO dj = new DJZBVO();
                dj.setParentVO(headers[i]);
                Vector<DJZBItemVO> items_temp = new Vector<DJZBItemVO>();
                for (int j = k; j < dJZBItems.length; j++) {
                    if (headers[i].getVouchid().equals(dJZBItems[j].getVouchid())) {
                        items_temp.addElement(dJZBItems[j]);
                    } else {
                        k = j;
                        break;
                    }
                }
                if (items_temp.size() > 0) {
                    DJZBItemVO[] items = new DJZBItemVO[items_temp.size()];
                    items_temp.copyInto(items);
                    dj.setChildrenVO(items);
                }
                vos[i] = dj;
            }
        }
        return vos;
    }
    /**
     *
     *
     * 创建日期：(2001-6-6)
     * @return nc.vo.ep.dj.DjfkxybVO
     * @param key String
     * @exception java.sql.SQLException 异常说明。
     * @throws DAOException
     */
    @SuppressWarnings("unchecked")
    public DJZBItemVO findfengcunByKey(DJZBItemVO item) throws SQLException, DAOException {

        Arap_fengcunVOMeta meta =new Arap_fengcunVOMeta();
        ArapBaseMappingMeta defaultmeta=new ArapBaseMappingMeta();
        defaultmeta.setTabName(meta.getTableName());
        defaultmeta.setPk(meta.getPrimaryKey());
        defaultmeta.setAttributes(new String[]{"pk_arap_fengcun","shenpi"});
        defaultmeta.setCols(new String[]{"pk_arap_fengcun","shenpi"});
        defaultmeta.setDataTypes(meta.getDataTypesByAttrNames(new String[]{"pk_arap_fengcun","shenpi"}));
        String cond =
            "dr=0 and is_fc='N' and pk_curr='"
                + item.getBzbm()
                + "' and  pk_corp='"
                + item.getDwbm()
                + "' and pk_sz_item='"
                + item.getSzxmid()
                + "' and pk_dept='"
                + item.getDeptid()
                + "' and  jebegin<="
                + item.getJfybje()
                + " and jeend>="
                + item.getJfybje();
        List<DJZBItemVO> ret=(List<DJZBItemVO>)pubdao.queryVOsByWhereClause(DJZBItemVO.class,defaultmeta,null,cond);
        if(null!=ret&&ret.size()>0){
        	DJZBItemVO it=(DJZBItemVO)ret.toArray(new DJZBItemVO[]{})[0];
            item.setPk_arap_fengcun(it.getPk_arap_fengcun());
            item.setShenpi(it.getShenpi());
        }
        return item;
    }
    /**
     * 通过主键查找一个VO对象。
     *
     * 创建日期：(2000-10-9)
     * @return nc.vo.ep.dj.DJZBHeaderVO
     * @param key String
     * @throws SQLException
     * @throws DAOException
     */
    public DJZBHeaderVO[] findHeaderByPk_bankrecive(String key) throws DAOException, SQLException {
    	/*************************************************************/
    	// 保留的系统管理接口：
    	//beforeCallMethod("nc.bs.ep.dj.DJZBDMO", "findHeaderByPk_bankrecive", new Object[]{key});
    	/*************************************************************/
    	DJZBHeaderVO[] dJZBHeaders = null;
    	boolean isUsedGL = false;
        String sqlFromClause = " from ARAP_DJZB zb inner join arap_djfb fb on zb.vouchid=fb.vouchid "
    	+" where zb.dr=0 and fb.tbbh = '"+key+"'";
            dJZBHeaders = this.getDJZBHeaderVOsUniversalArray(sqlFromClause, isUsedGL,null);
    	/*************************************************************/
    	// 保留的系统管理接口：
    	//afterCallMethod("nc.bs.ep.dj.DJZBDMO", "findHeaderByPk_bankrecive", new Object[]{key});
    	/*************************************************************/
    	return dJZBHeaders;
    }
    /**
     * 通过主键查找一个VO对象。
     *
     * 创建日期：(2000-10-9)
     * @return nc.vo.ep.dj.DJZBHeaderVO
     * @param key String
     * @exception java.sql.SQLException 异常说明。
     * @throws DAOException
     */
    public DJZBHeaderVO findHeaderByPrimaryKey_SS(String key) throws   DAOException {
    	Collection col=basedao.retrieveByClause(DJZBHeaderVO.class,ssmeta," dr=0 and vouchid =  '"+key+"'");
    	return (DJZBHeaderVO)col.iterator().next();
    }


    /**
     * 通过条件查单据原币余额。
     *
     * 创建日期：(2000-10-9)
     * @return nc.vo.ep.dj.DJZBItemVO
     * @param key String
     * @exception java.sql.SQLException 异常说明。
     * @throws DAOException
     */
    @SuppressWarnings("unchecked")
    public DJZBItemVO[] findItem_b(String key) throws  DAOException {

    	/*************************************************************/
    	// 保留的系统管理接口：
    	//beforeCallMethod("nc.bs.ep.dj.DJZBDMO", "findItem_b", new Object[]{key});
    	/*************************************************************/
    	String sql = "select fb_oid,ybye,fbye,bbye"
    	+" from ARAP_item h left outer join arap_item_b b  on h.vouchid=b.vouchid "+key;
    	sql+=" and b.closer is null and b.dr=0 ";

    	ArapBaseMappingMeta meta=new ArapBaseMappingMeta();
        meta.setTabName(fbmeta.getTableName());
        meta.setPk(fbmeta.getPrimaryKey());
        meta.setAttributes(new String[]{"fb_oid","ybye","fbye","bbye"});
        meta.setDataTypes(fbmeta.getDataTypesByAttrNames(new String[]{"fb_oid","ybye","fbye","bbye"}));
        List<DJZBItemVO> ret=(List<DJZBItemVO>)pubdao.queryVOsBySql(DJZBItemVO.class,meta,sql);
        return ret.toArray(new DJZBItemVO[]{});
    }
    /**
     * 通过主键查找一个VO对象。
     *
     * 创建日期：(2000-10-9)
     * @return nc.vo.ep.dj.DJZBItemVO
     * @param key String
     * @exception java.sql.SQLException 异常说明。
     * @throws DAOException
     */
    public DJZBItemVO findItemByPrimaryKey(String key) throws   DAOException {
    	/*************************************************************/
    	// 保留的系统管理接口：
    	//beforeCallMethod("nc.bs.ep.dj.DJZBDMO", "findItemByPrimaryKey", new Object[]{key});
    	/*************************************************************/
    	if(key==null || key.length()==0){
    	    return null;
    	}
    	Vector v = findItemsByCondition(" where dr=0 and  fb_oid ='"+key+"'");
    		if(v!=null && v.size()>0){
    		    return (DJZBItemVO)v.elementAt(0);
    		}
    	/*************************************************************/
    	// 保留的系统管理接口：
    	//afterCallMethod("nc.bs.ep.dj.DJZBDMO", "findItemByPrimaryKey", new Object[]{key});
    	/*************************************************************/

    	return null;
    }
    public Vector findItemsByPrimaryKeys(String[] keys) throws   DAOException, SQLException {
    	/*************************************************************/
    	// 保留的系统管理接口：
    	//beforeCallMethod("nc.bs.ep.dj.DJZBDMO", "findItemByPrimaryKey", new Object[]{key});
    	/*************************************************************/
    	if(keys==null || keys.length ==0){
    	    return null;
    	}
//    	StringBuffer sb=new StringBuffer();
//    	for(int i=0,size=keys.length;i<size;i++){
//    		sb.append("'").append(keys[i]).append("'").append(",");
//    	}
    	String sql= " where dr=0 and "+SqlUtils.getInStr("fb_oid", keys);

    	return findItemsByCondition(sql);

    }
    /**
     * 通过主键查找一个VO对象。
     *
     * 创建日期：(2000-10-9)
     * @return nc.vo.ep.dj.DJZBItemVO
     * @param key String
     * @exception java.sql.SQLException 异常说明。
     * @throws DAOException
     */
    public DJZBItemVO[] findItemsForHeader(String key) throws   DAOException{

    	if(key==null || key.length()==0){
    	    return null;
    	}

       DJZBItemVO[] dJZBItems = null;
       Vector v = findItemsByCondition( " where dr=0 and xgbh ="+ArapConstant.UNITACCOUNTSTAT_DEFAULT+" and vouchid = '"+key+"'");
       //Vector v = findItemsByCondition( " where dr=0 and vouchid = '"+key+"'");
        if (v.size() > 0) {
    	     dJZBItems = new DJZBItemVO[v.size()];
            v.copyInto(dJZBItems);
        }
        return dJZBItems;
    }

    /**
     * 通过主键查找一个VO对象。
     *
     * 创建日期：(2000-10-9)
     * @return nc.vo.ep.dj.DJZBItemVO
     * @param key String
     * @exception java.sql.SQLException 异常说明。
     * @throws DAOException
     */
    public DJZBItemVO[] findItemsForHeaders(String[] keys) throws   DAOException ,SQLException {

    	if(keys==null || keys.length==0){
    	    return null;
    	}
       DJZBItemVO[] dJZBItems = null;
       Vector v = findItemsByCondition( " where dr=0 and xgbh ="+ArapConstant.UNITACCOUNTSTAT_DEFAULT+" and " + SqlUtils.getInStr("vouchid", keys));
       //Vector v = findItemsByCondition( " where dr=0 and vouchid = '"+key+"'");
        if (v.size() > 0) {
    	     dJZBItems = new DJZBItemVO[v.size()];
            v.copyInto(dJZBItems);
        }
        return dJZBItems;
    }

    /**
     * 通过主键查找一个VO对象。
     *
     作者:陈飞
     * 创建日期：(2000-10-9)
     * @return nc.vo.ep.dj.DJZBItemVO
     * @param key String
     * @exception java.sql.SQLException 异常说明。
     表体每一行的余额必须大于0
     * @throws DAOException
     * @throws SQLException
     */
    public DJZBItemVO[] findItemsForHeader_Hz(String[] keys,DjCondVO djcond) throws   DAOException, SQLException {

    	/*************************************************************/
    	// 保留的系统管理接口：
    	//beforeCallMethod("nc.bs.ep.dj.DJZBDMO", "findItemsForHeader_Hz", new Object[]{key,djcond});
    	/*************************************************************/
    	//往来对象
    	String cond_wldx="";
    	if(djcond.m_Wldx!=null)
    	 	cond_wldx=" and wldx="+djcond.m_Wldx.intValue();

    	//客户
    	String cond_hbbm="";
    	if(djcond.m_hbbm!=null && djcond.m_hbbm.trim().length()>0)
    		cond_hbbm=" and hbbm='"+djcond.m_hbbm+"'";

    	//业务员
    	String cond_ywybm="";
    	if(djcond.m_ywybm!=null && djcond.m_ywybm.trim().length()>0)
    		cond_ywybm=" and ywybm='"+djcond.m_ywybm+"'";

    	//部门
    	String cond_dept="";
    	if(djcond.m_deptid!=null && djcond.m_deptid.trim().length()>0)
    		cond_dept=" and deptid='"+djcond.m_deptid+"'";

    	//币种
    	String cond_curr="";
    	if(djcond.m_Bz!=null && djcond.m_Bz.trim().length()>0)
    		cond_curr=" and bzbm='"+djcond.m_Bz+"'";
    	String sql =" where dr=0 and (ybye - occupationmny) >0 and (pausetransact = 'N' or pausetransact is null) and "
    	//+" and  vouchid = '"+key+"'"
    	+ SqlUtils.getInStr("vouchid", keys)
    	+cond_wldx
    	+cond_hbbm
    	+cond_ywybm
    	+cond_dept
    	+cond_curr
    	;
    	DJZBItemVO[] dJZBItems = new DJZBItemVO[0];
    	Vector v = findItemsByCondition(sql);
    	if (v.size() > 0){
    		dJZBItems = new DJZBItemVO[v.size()];
    		v.copyInto(dJZBItems);
    	}
    	/*************************************************************/
    	// 保留的系统管理接口：
    	//afterCallMethod("nc.bs.ep.dj.DJZBDMO", "findItemsForHeader_Hz", new Object[]{key,djcond});
    	/*************************************************************/

    	return dJZBItems;
    }
    /**
     * 通过主键查找一个VO对象。
     *
     * 创建日期：(2000-10-9)
     * @return nc.vo.ep.dj.DJZBItemVO
     * @param key String
     * @exception java.sql.SQLException 异常说明。
     * @throws DAOException
     */
    @SuppressWarnings("unchecked")
    public DJZBItemVO[] findItemsForHeader_SS(String key) throws   DAOException {
    	Collection<DJZBItemVO> col=basedao.retrieveByClause(DJZBItemVO.class,bbmeta,"dr=0  and  vouchid =  '"+key+"'order by flbh");
    	return (DJZBItemVO[])col.toArray(new DJZBItemVO[]{});
    }
    @SuppressWarnings("unchecked")
    public DJZBItemVO[] findItemsForHeader_SS4(String key) throws   DAOException {
    	Collection<DJZBItemVO> col=basedao.retrieveByClause(DJZBItemVO.class,bbmeta,"dr=0  and closer is null and ybye>0  and   vouchid =  '"+key+"'order by flbh");
    	return col.toArray(new DJZBItemVO[]{});
    }
    /**
     * 通过主键查找一个VO对象。
     *
     * 创建日期：(2000-10-9)
     * @return nc.vo.ep.dj.DJZBItemVO
     * @param key String
     * @exception java.sql.SQLException 异常说明。
     * 2004-08-09,modification log,wangqiang
     *      add the "closer is null" to where clause,in that way the closed item is filtered out
     * @throws DAOException
     */
    @SuppressWarnings("unchecked")
    public DJZBItemVO[] findItemsForHeader_SS_4(String key) throws DAOException {
        Collection<DJZBItemVO> col=basedao.retrieveByClause(DJZBItemVO.class,bbmeta,"ybye>0 and dr=0  and closer is null and  vouchid = '"+key+"'order by flbh");
    	return col.toArray(new DJZBItemVO[]{});
    }
    /**
     * author:wangqiang
     * create time : 2004-08-10
     * function    : find the matching SS bill item for arap bill item
     * 创建日期：(2000-10-9)
     * @return nc.vo.ep.dj.DJZBItemVO
     * @param key String
     * @exception java.sql.SQLException 异常说明。
     * @throws DbException
     */
    public int findMatchingSSItem(String key) throws SQLException, DbException {

    	/*************************************************************/
    	// 保留的系统管理接口：
    	//beforeCallMethod("nc.bs.ep.dj.DJZBDMO", "findMatchingSSItem", new Object[]{key});
    	/*************************************************************/
    	String sql = "select count(b.fb_oid)"

    	+" from ARAP_item h left outer join arap_item_b b  on h.vouchid=b.vouchid "+key;
        sql+= " and b.closer is null and b.dr=0 " ;

        Integer intSSItemCount = null;
    	PersistenceManager pm=null;
        try{
            pm=PersistenceManager.getInstance(getds());
            intSSItemCount=(Integer) pm.getJdbcSession().executeQuery(sql,new  ResultSetProcessor(){

                /**
				 *
				 */
				private static final long serialVersionUID = 5278474825149770699L;

				public Object handleResultSet(ResultSet rs) throws SQLException {
                    //
                    if (null!=rs&&rs.next()) {
              			 return new Integer( rs.getInt(1));
              		}
                    return new Integer(0);
                }
            });

        	} finally {
         	    pm.release();
        	}
    	return intSSItemCount.intValue();
    }

    /**
     * 通过主键查找area_name
     *
     * 创建日期：(2001-6-6)
     * @return nc.vo.ep.dj.DjfkxybVO
     * @param key String
     * @exception java.sql.SQLException 异常说明。
     * @throws DbException
     */
    public String getAreaNameByCode(String area_code) throws SQLException, DbException {
    	String sql = "select areaname from bd_bankarea where dr=0 and areacode='"+area_code+"'";
    	String area_name="";
    	PersistenceManager pm=null;
        try{
            pm=PersistenceManager.getInstance(getds());
            area_name=(String) pm.getJdbcSession().executeQuery(sql,new  ResultSetProcessor(){

                /**
				 *
				 */
				private static final long serialVersionUID = 9177635729896443485L;

				public Object handleResultSet(ResultSet rs) throws SQLException {
                    //
                    if (rs.next()) {
            			// count :
            			return rs.getString("areaname");
            		}
                    return "";
                }

            });

        	} finally {
        	    pm.release();
        	}
    	return area_name;
    }
    /**
     * 通过主键查找一个VO对象。
     *
     * 创建日期：(2001-6-6)
     * @return nc.vo.ep.dj.DjfkxybVO
     * @param key String
     * @exception java.sql.SQLException 异常说明。
     * @throws DbException
     */
    public Integer getCheckflag(String vouchid) throws SQLException, DbException {

    	String sql = "select  count(*) from arap_djfb where dr=0 and checkflag>0 and vouchid ='"+vouchid+"'";
    	Integer checkflag=null;
    	PersistenceManager pm=null;
        try{
            pm=PersistenceManager.getInstance(getds());
            checkflag=(Integer) pm.getJdbcSession().executeQuery(sql,new  ResultSetProcessor(){

                /**
				 *
				 */
				private static final long serialVersionUID = 5117431304740801362L;

				public Object handleResultSet(ResultSet rs) throws SQLException {
                    //
                    if (null!=rs&&rs.next()) {
            			// count :
            			return (Integer)rs.getObject(1);
            		}
                    return null;
                }

            });
        	} finally {
        	    pm.release();
        	}
    	return checkflag;
    }
    /**
     * <p>根据表头的主键查询一个VO。
     处理标志（clbz）：-2——折扣、-1——异币种核销、0——同币种核销、1——汇兑损益、
    2——红票对冲、票据贴现; 3、坏账发生; 4、坏账收回;
    5、应收并账；6、应付并账；7、往来并账；
     * <p>
     * 创建日期：(2000-10-9)
     * @param key ??dbFieldType??
     * @exception java.sql.SQLException 异常说明。
     * @throws DbException
     */
    public Integer getClbzByPkey(String key) throws SQLException, DbException {
    	Integer clbz=null;
    	String sql="select clbz from arap_djclb where dr=0 and vouchid='"+key+"'";
    	PersistenceManager pm=null;
        try{
            pm=PersistenceManager.getInstance(getds());
            clbz=(Integer) pm.getJdbcSession().executeQuery(sql,new  ResultSetProcessor(){
                /**
				 *
				 */
				private static final long serialVersionUID = 5164862123033678514L;

				public Object handleResultSet(ResultSet rs) throws SQLException {
                    //
                    if (null!=rs&&rs.next()) {
            			// clbz :
            			return  (Integer)rs.getObject(1);
            		}
                    return null;
                }

            });

        	} finally {
        	    pm.release();
        	}
    	return clbz;
    }
    /**
     * 通过主键查找一个VO对象。
     *
     * 创建日期：(2001-6-6)
     * @return nc.vo.ep.dj.DjfkxybVO
     * @param key String
     * @exception java.sql.SQLException 异常说明。
     * @throws DbException
     */
    public Integer getCountByDjbh(String djbh,String pk_corp) throws SQLException, DbException {
     	String sql = "select  count(*) from arap_djzb where dr=0 and (dwbm='"+pk_corp+"') and djbh = '"+djbh+"'";
    	Integer count=null;
    	PersistenceManager pm=null;
        try{
            pm=PersistenceManager.getInstance(getds());
             count=(Integer) pm.getJdbcSession().executeQuery(sql,new  ResultSetProcessor(){

                /**
				 *
				 */
				private static final long serialVersionUID = 1L;

				public Object handleResultSet(ResultSet rs) throws SQLException {
                    //
                    if (rs.next()) {
            			// count :
            			return (Integer)rs.getObject(1);
            		}
                    return null;
                }

            });

        	} finally {

        	    pm.release();
        	}
    	return count;
    }
    /**
     * 通过主键查找一个VO对象。
     *
     * 创建日期：(2001-6-6)
     * @return nc.vo.ep.dj.DjfkxybVO
     * @param key String
     * @exception java.sql.SQLException 异常说明。
     * @throws DbException
     */
    public Integer getCountByZyx19(String zyx19) throws SQLException, DbException {
     	String sql = "select  count(*) from arap_djzb where dr=0  and item_bill_pk = '"+zyx19+"'";
    	Integer count=null;
    	PersistenceManager pm=null;
        try{
            pm=PersistenceManager.getInstance(getds());
            count=(Integer) pm.getJdbcSession().executeQuery(sql,new ResultSetProcessor(){

                /**
				 *
				 */
				private static final long serialVersionUID = 6146862989116928453L;

				public Object handleResultSet(ResultSet rs) throws SQLException {
                    //
                    if (rs.next()) {
            			// count :
            			return  (Integer)rs.getObject(1);
            		}
                    return null;
                }

            });

        	} finally {

        	    pm.release();
        	}
    	return count;
    }
//    /**
//     * 通过主键查找一个VO对象。
//     *
//     * 创建日期：(2001-6-6)
//     * @return nc.vo.ep.dj.DjfkxybVO
//     * @param key String
//     * @exception java.sql.SQLException 异常说明。
//     */
//    public String getDjh(String key) throws SQLException {
//       String djh = "";
//        try {
//            if (key == null || key.trim().length() < 1)
//                djh = getOID();
//            else
//                djh = getOID(key);
//
//        } catch (Exception e) {
//        }
//
//        return djh;
//    }
    /**
     * 通过收支项目id返回对应公开要素定义数组
     *

     * 创建日期：(2001-4-26)
     * @return Vector {nc.vo.cf.freepropertyvo.freepropertyvoVO}
     * @param unitCode int
     * @exception java.sql.SQLException 异常说明。
     * @throws DbException
     */
    public String[] getFreePropertys(String m_szxmid) throws SQLException, DbException {


    	String sql = "";
    	if (m_szxmid == null || m_szxmid.trim().length() < 1) {
    		return null;
    	} else {
    		sql =
    			"select free1,free2,free3,free4,free5,free6,free7,free8,free9,free10 from bd_costsubj "
    				+ " where dr=0 and pk_costsubj= '"+m_szxmid+"'";
    	}
    	 PersistenceManager pm=null;
         try{
             pm=PersistenceManager.getInstance(getds());
             return (String[]) pm.getJdbcSession().executeQuery(sql,new  ResultSetProcessor(){

                /**
				 *
				 */
				private static final long serialVersionUID = 3304006085908676712L;

				public Object handleResultSet(ResultSet rs) throws SQLException {
                    //
                    String[] frees = new String[10];
                    while (rs.next()) {

             			//自由项1
             			String free1 = rs.getString("free1");
             			if (free1 != null)
             				frees[0] = free1;

             			//自由项2
             			String free2 = rs.getString("free2");
             			if (free2 != null)
             				frees[1] = free2;

             			//自由项3
             			String free3= rs.getString("free3");
             			if (free3 != null)
             				frees[2] = free3;

             			//自由项4
             			String free4 = rs.getString("free4");
             			if (free4 != null)
             				frees[3] = free4;

             			//自由项5
             			String free5 = rs.getString("free5");
             			if (free5 != null)
             				frees[4] = free5;

             			//自由项6
             			String free6 = rs.getString("free6");
             			if (free6 != null)
             				frees[5] = free6;

             			//自由项7
             			String free7 = rs.getString("free7");
             			if (free7 != null)
             				frees[6] = free7;

             			//自由项8
             			String free8 = rs.getString("free8");
             			if (free8 != null)
             				frees[7] = free8;

             			//自由项9
             			String free9 = rs.getString("free9");
             			if (free9 != null)
             				frees[8] = free9;

             				//自由项10
             			String free10 = rs.getString("free10");
             			if (free10 != null)
             				frees[9] = free10;
             		}
                    return frees;
                }

             });

         	} finally {

         	    pm.release();
         	}
    }
    /**
     * 通过主键查找一个VO对象。
     *
     * 创建日期：(2000-10-9)
     * @return nc.vo.ep.dj.DJZBHeaderVO
     * @param key String
     * @exception java.sql.SQLException 异常说明。
     * @throws DbException
     */
    public String getOfficialprintuser(String key) throws SQLException, DbException {
    	String sql = "select Officialprintuser from ARAP_DJZB where  vouchid = '"+key+"'";
    	String  Officialprintuser = null;
    	 PersistenceManager pm=null;
         try{
             pm=PersistenceManager.getInstance(getds());
             Officialprintuser=(String) pm.getJdbcSession().executeQuery(sql,new  ResultSetProcessor(){

                /**
				 *
				 */
				private static final long serialVersionUID = 7691733286799842695L;

				public Object handleResultSet(ResultSet rs) throws SQLException {
                    //
                    if (null!=rs&&rs.next()) {
             			return  rs.getString(1);
             		}
                    return null;
                }

             });

         	} finally {
         	    pm.release();
         	}
    	return Officialprintuser;
    }
    /**
     * 通过主键查找单据审批状态。
     *
     * 创建日期：(2000-10-9)
     * @return nc.vo.ep.dj.DJZBHeaderVO
     * @param key String
     * @exception java.sql.SQLException 异常说明。
     * @throws DbException
     */
    public String getSPZTByPk(String tablename,String key) throws SQLException, DbException {
        String sql = "select spzt from  "+ tablename +" where dr=0 and vouchid = '" + key + "'";
    	String spzt = null;
        PersistenceManager pm=null;
        try{
            pm=PersistenceManager.getInstance(getds());
            spzt=(String) pm.getJdbcSession().executeQuery(sql,new  ResultSetProcessor(){

                /**
				 *
				 */
				private static final long serialVersionUID = 8133087689358276393L;

				public Object handleResultSet(ResultSet rs) throws SQLException {
                    //
                    if (rs.next()) {
            			// spzt :
            			return  rs.getString(1);
            		}
                    return null;
                }

            });

        	} finally {
        	    pm.release();
        	}
    	return spzt;
    }
    /**
     * 通过主键查找一个VO对象。
     *
     * 创建日期：(2000-10-9)
     * @return nc.vo.ep.dj.DJZBHeaderVO
     * @param key String
     * @exception java.sql.SQLException 异常说明。
     * @throws DbException
     */
    public String getTsByPrimaryKey(String key, String tableName)
    	throws SQLException, DbException {
        String ts=null;
        String sql = "select  ts from  " + tableName + "  where   vouchid ='" + key + "'";
        PersistenceManager pm=null;
        try{
            pm=PersistenceManager.getInstance(getds());
            ts=(String)pm.getJdbcSession().executeQuery(sql,new  ResultSetProcessor()
                    {
                        /**
						 *
						 */
						private static final long serialVersionUID = 4314866937739791621L;

						public Object handleResultSet(ResultSet arg0) throws SQLException {
                            //
                            if (null!=arg0&&arg0.next()) {
                    			return  arg0.getString(1);
                    		}
                            return null;
                        }
                    });
        	} finally {
        	    pm.release();
        	}
   	return ts;
    }
    /**
     * 通过主键查找一个VO对象。
     *
     * 创建日期：(2001-6-6)
     * @return nc.vo.ep.dj.DjfkxybVO
     * @param key String
     * @exception java.sql.SQLException 异常说明。
     * @throws DbException
     */
    public Integer getXTCountBYpk(String vouchid) throws SQLException, DbException {

    	String sql = "select  count(*) from arap_djzb inner join arap_djfb on arap_djfb.vouchid=arap_djzb.vouchid where arap_djzb.dr=0 and arap_djfb.ddlx='"+vouchid+"'  and arap_djzb.djzt > 0 and arap_djzb.lybz=9 ";
    	Integer count=null;
    	PersistenceManager pm=null;
        try{
            pm=PersistenceManager.getInstance(getds());
            count=(Integer) pm.getJdbcSession().executeQuery(sql,new  ResultSetProcessor(){

                /**
				 *
				 */
				private static final long serialVersionUID = 7295881919604135476L;

				public Object handleResultSet(ResultSet rs) throws SQLException {
                    //
                    if (rs.next()) {
            			// count :
            			return (Integer)rs.getObject(1);
            		}
                    return null;
                }

            });

        	} finally {
        	    pm.release();
        	}
    	return count;
    }
    /**
     * 是否在目标公司已经生成协同单据。
     *
     * 创建日期：(2001-6-6)
     * @return nc.vo.ep.dj.DjfkxybVO
     * @param key String
     * @exception java.sql.SQLException 异常说明。
     * @throws DbException
     */
    public boolean getXTCountBYPkandCorp(String vouchid,String corp) throws SQLException, DbException {
    	String sql = "select  count(*) from arap_djzb inner join arap_djfb on arap_djfb.vouchid=arap_djzb.vouchid where arap_djzb.dr=0 and arap_djfb.ddlx='"+vouchid+"' and arap_djzb.lybz=9 and arap_djzb.dwbm= '"+corp+"'";
    	PersistenceManager pm=null;
    	Integer count=null;
    	try {
            pm=PersistenceManager.getInstance(getds());
            count=(Integer) pm.getJdbcSession().executeQuery(sql,new  ResultSetProcessor(){

                /**
				 *
				 */
				private static final long serialVersionUID = 5942145008916976253L;

				public Object handleResultSet(ResultSet rs) throws SQLException {
                    //
                    if (null!=rs&&rs.next()) {
            			// count :
            			return  (Integer)rs.getObject(1);
            		}
                    return null;
                }

            });
    		//

    	}
    	finally {
    	    pm.release();
    	}
    	//
     	if(null==count||count.intValue()!=0)
    	    return true;
    	return false;
    }
    /**
     *
     * <p>
     *   查找一张单据生成的所有单据的公司和PK。
     * </p>
     * <p>
     *    使用前提
     * </p>
     * <p>
     * <Strong>已知的BUG：</Strong>
     * 	<ul>
     * 		<li></li>
     *  </ul>
     * </p>
     *
     * <p>
     * <strong>修改历史：</strong>
     * 	<ul>
     * 		<li><ul>
     * 			<li><strong>修改人:</strong>rocking</li>
     * 			<li><strong>修改日期：</strong>2006-1-10</li>
     * 			<li><strong>修改内容：<strong></li>
     * 			</ul>
     * 		</li>
     * 		<li>
     * 		</li>
     *  </ul>
     * </p>
     *
     * @author rocking
     * @version V5.0
     * @since V3.1
     * @param vouchid
     * @return
     * @throws SQLException
     * @throws DbException
     */
    public List getXtMsgBypk(String vouchid) throws SQLException, DbException {
    	String sql = "select  corp.unitname, zb.vouchid from arap_djzb zb inner join arap_djfb fb on fb.vouchid= zb.vouchid left outer join bd_corp corp on zb.dwbm=corp.pk_corp where  zb.dr=0 and  fb.ddlx='"+vouchid+"' and  zb.lybz=9 ";
    	PersistenceManager pm=null;
    	try {
            pm=PersistenceManager.getInstance(getds());
            return (List) pm.getJdbcSession().executeQuery(sql,new  ResultSetProcessor(){

                /**
				 *
				 */
				private static final long serialVersionUID = -2084283254540215529L;

				public Object handleResultSet(ResultSet rs) throws SQLException {
                    //
                    List<String[]> retLst=new ArrayList<String[]>();
                	List<String> temp=new ArrayList<String>();
                    while (rs.next()) {
            		    String[] str=new String[2];
            		    str[0]=rs.getString(1);
            		    str[1]=rs.getString(2);
            		    if(!temp.contains(str[1])){
            				retLst.add(str);
            				temp.add(str[1]);
            		    }
            		}
                    return retLst;
                }

            });


    	}
    	finally {
    	    pm.release();
    	}
    	//
    }

    /**
     * 通过条件查单据原币余额。
     *
     * 创建日期：(2000-10-9)
     * @return nc.vo.ep.dj.DJZBItemVO
     * @param key String
     * @exception java.sql.SQLException 异常说明。
     * @throws DbException
     */
    public UFDouble getYbyeByKey(String key) throws SQLException, DbException {
    	String sql = "select sum(b.jfybje+b.dfybje)"
    	+" from ARAP_DJzb h left outer join arap_djfb b  on h.vouchid=b.vouchid "+key;
    	UFDouble ybye = null;
    	PersistenceManager pm=null;
        try{
            pm=PersistenceManager.getInstance(getds());
            ybye=(UFDouble) pm.getJdbcSession().executeQuery(sql,new  ResultSetProcessor(){

                /**
				 *
				 */
				private static final long serialVersionUID = -7140665989863356645L;

				public Object handleResultSet(ResultSet rs) throws SQLException {
                    //
                    if (rs.next()) {
                        BigDecimal ybye_t = (BigDecimal)rs.getBigDecimal(1);
            			return ybye_t == null ? null : new UFDouble(ybye_t);
            		}
                    return null;
                }

            });

        	} finally {
        	    pm.release();
        	}
    	return ybye;
    }
    /**
     * 通过条件查单据原币余额。
     *
     * 创建日期：(2000-10-9)
     * @return nc.vo.ep.dj.DJZBItemVO
     * @param key String
     * @exception java.sql.SQLException 异常说明。
     * @throws DbException
     */
    public UFDouble getYbyeByKey_SS(String key) throws SQLException, DbException {
    	String sql = "select sum(b.ybye)"
    	+" from ARAP_item h left outer join arap_item_b b  on h.vouchid=b.vouchid "+key;
        sql+= " and b.closer is null and b.dr=0 " ;
    	UFDouble ybye = null;
    	PersistenceManager pm=null;
        try{
            pm=PersistenceManager.getInstance(getds());
            ybye=(UFDouble) pm.getJdbcSession().executeQuery(sql,new  ResultSetProcessor(){

                /**
				 *
				 */
				private static final long serialVersionUID = -2543580326762414150L;

				public Object handleResultSet(ResultSet rs) throws SQLException {
                    //
                    if (rs.next()) {
                        BigDecimal ybye_t = (BigDecimal)rs.getBigDecimal(1);
            			return ybye_t == null ? null : new UFDouble(ybye_t);
            		}
                    return null;
                }

            });

        	} finally {
        	    pm.release();
        	}
    	return ybye;
    }
    /**
     * <p>将VO插入母子表。
     * <p>
     * 创建日期：(2000-10-9)
     * @param vo nc.vo.ep.dj.DJZBVO
     * @exception java.sql.SQLException 异常说明。
     * @throws DAOException
     */
    public DJZBVO insert(DJZBVO vo) throws  DAOException {
		convertDJZBVO(vo);
		DJZBHeaderVO head = (DJZBHeaderVO) vo.getParentVO();
		head = insertHeader(head );
		
		SystemProfile.getInstance().log("arapbillsave insert head");
		
		vo.setParentVO(head);
		DJZBItemVO[] items = (DJZBItemVO[]) vo.getChildrenVO();
		Map<DJZBItemVO,String> cmpMap=new HashMap<DJZBItemVO,String>();
		for(DJZBItemVO item:items){
		cmpMap.put(item,item.getFb_oid()==null?"":item.getFb_oid());
		}
		items = insertItems(items, head );
		
		Map<String,String> retMap=new HashMap<String,String>();
		for(DJZBItemVO item:items){
		retMap.put(cmpMap.get(item).length()==0?item.getFb_oid():cmpMap.get(item),
		item.getFb_oid());
		}
		vo.setChildrenVO(items);
		vo.setCmpMap(retMap);
		return vo;
    }
    /**
     * <p>将VO插入母子表。
     * <p>
     * 创建日期：(2000-10-9)
     * @param vo nc.vo.ep.dj.DJZBVO
     * @exception java.sql.SQLException 异常说明。
     * @throws DAOException
     */
    public DJZBVO insert_SS(DJZBVO vo) throws  DAOException {
    	// 插入表头：
    	convertDJZBVO(vo);
    	DJZBHeaderVO head=(DJZBHeaderVO) vo.getParentVO();
    	head = insertHeader_SS(head);
    	String key =head.getPrimaryKey();
    	vo.setParentVO(head);
    	// 插入表体各项：
    	DJZBItemVO[] items = (DJZBItemVO[]) vo.getChildrenVO();
    	for ( int i = 0; i < items.length; i++ ) {
    		items[i].setDjbh(head.getDjbh());//cf edit 2001-12-11  设置主,辅表的单据编号相同
    		insertItem_SS(items[i], key);
    		items[i].setStatus(VOStatus.UPDATED);
    	}
    	return vo;
    }
    public static  void convertDJZBVO (DJZBVO vo)
    {
        convertHeaderVO((DJZBHeaderVO)vo.getParentVO());
        DJZBItemVO[] items= (DJZBItemVO[])vo.getChildrenVO();
        if(null!=items){
        for(int i=0,size=items.length;i<size;i++)
        	DjVOTreaterAid. convertItemVO(items[i]);
        }
    }
    public  static void convertHeaderVO(DJZBHeaderVO clone)
    {
//        DJZBHeaderVO clone=(DJZBHeaderVO)head.clone();
    	if (clone.getDr() == null) {
    	     clone.setDr( new Integer( 0));
 		}
    	if (clone.getZzzt() == null) {
    	     clone.setZzzt( new Integer(DJZBVOConsts.m_intDJZzzt_Default));
 		}
    	if (clone.getSxbz() == null ) {
			//stmt.setNull(62, Types.INTEGER);
		    clone.setSxbz(new Integer( DJZBVOConsts.m_intSXBZ_NO));
		}
    	UFDateTime ts=new UFDateTime(CurTime.getCurrentTimeStampString());
 		clone.setTs(ts);
// 		head.setTs(ts);
//    	return clone;
    }
    /**
     * 向数据库插入一个VO对象。
     *
     * 创建日期：(2000-10-9)
     * @param node nc.vo.ep.dj.DJZBHeaderVO
     * @exception java.sql.SQLException 异常说明。
     * Modification log
     * 2004-11-04,wangqiang
     * Add "sxbz"(生效标志） and "jszxzf"（结算中心支付） column to the sql
     * @throws DAOException

     */
    private DJZBHeaderVO insertHeader(DJZBHeaderVO dJZBHeader ) throws   DAOException {
        String key=basedao.insertObject(dJZBHeader,zbmeta);
        dJZBHeader.setPrimaryKey(key);
    	return dJZBHeader;
    }
    /**
     * 向数据库插入一个VO对象。
     *
     * 创建日期：(2000-10-9)
     * @param node nc.vo.ep.dj.DJZBHeaderVO
     * @exception java.sql.SQLException 异常说明。
     * Modification log
     * 2004-11-04,wangqiang
     * Add "sxbz"(生效标志） column to the sql
     * @throws DAOException
     *
     */
    private DJZBHeaderVO insertHeader_SS(DJZBHeaderVO dJZBHeader) throws   DAOException {
        String  key=basedao.insertObject(dJZBHeader,ssmeta);
    	dJZBHeader.setPrimaryKey(key);
    	return dJZBHeader;
    }
    /**
     * 向数据库插入一个VO对象。
     *
     * 创建日期：(2001-8-17)
     * @param node nc.vo.a.a1.DJZBItemVO
     * @exception java.sql.SQLException 异常说明。
     * @throws DAOException
     */
    private String insertItem(DJZBItemVO dJZBItem)
        throws   DAOException {
        if(dJZBItem==null){
            return null;
        }
        DJZBItemVO item = insertItems(new DJZBItemVO[]{dJZBItem})[0];
        String key = item.getFb_oid();
    	//加单据付款协议表
    	DjfkxybVO[] fkxybs = dJZBItem.fkxyvos;
    	if (fkxybs != null) {
    	    for (int i = 0; i < fkxybs.length; i++) {
    	        if (fkxybs[i] != null) {
    	            fkxybs[i].setVouchid(dJZBItem.getVouchid());
    	            fkxybs[i].setFb_oid(key);
    	            fkxybs[i].setPrimaryKey(fkdao.insert(new DjfkxybVO[]{ fkxybs[i]})[0]);
    	        }
    	    }
    	}
    return key;
    }
    /**
     * 向数据库插入一个VO对象。
     *
     * 创建日期：(2001-8-17)
     * @param node nc.vo.a.a1.DJZBItemVO
     * @exception java.sql.SQLException 异常说明。
     * @throws DAOException
     */
    private String insertItem_SS(DJZBItemVO dJZBItem)
    	throws  DAOException {
        String key=basedao.insertObject(dJZBItem,bbmeta);
        dJZBItem.setPrimaryKey(key);
    	return key;
    }
    /**
     * <p>向数据库插入一个VO对象。
     * <p>
     * 创建日期：(2000-10-9)
     * @param DJZBItem nc.vo.ep.dj.DJZBItemVO
     * @param foreignKey String
     * @exception java.sql.SQLException 异常说明。
     * @throws DAOException
     */
    private String insertItem_SS(DJZBItemVO dJZBItem, String foreignKey) throws  DAOException {
    	dJZBItem.setVouchid(foreignKey);
    	String key = insertItem_SS(dJZBItem);
    	//加自由项
    	DJFBItemVO[] frees = dJZBItem.items;
    	if(frees != null){
    		for(int i = 0;i<frees.length;i++){
    			frees[i].setVouchid(foreignKey);
    			frees[i].setFb_oid(key);
    			defdao.insertFree(frees[i]);
    		}
    	}
    	return key;
    }
//    //maji_TODO 数组溢出
//    private DJZBItemVO[][] splitBigArray(DJZBItemVO[] dJZBItems ){
//    	int length=dJZBItems.length;
//    	int size= length/1000 +1;
//    	DJZBItemVO[][] splitdJZBItems=new DJZBItemVO[size][];
//    	for(int i=0;i<size;i++){
//    		if((dJZBItems.length-i*1000)>1000){
//    			splitdJZBItems[i]=new DJZBItemVO[1000];
//    		}else{
//    			splitdJZBItems[i]=new DJZBItemVO[(dJZBItems.length-i*1000)];
//    		}
//    		for(int j=0;j<splitdJZBItems[i].length;j++){
//    			splitdJZBItems[i][j]=dJZBItems[i*1000+j];
//    		}
//    	}
//    	return splitdJZBItems;
//    }
    /**
     * 向数据库插入一个VO对象。
     *
     * 创建日期：(2001-8-17)
     * @param node nc.vo.a.a1.DJZBItemVO
     * @exception java.sql.SQLException 异常说明。
     * @throws DAOException
     */
    private DJZBItemVO[] insertItems(DJZBItemVO[] dJZBItems )
    	throws DAOException {
        String[] keys=basedao.insertObject(dJZBItems,fbmeta);
        for(int i=0,size=keys.length;i<size;i++)
            dJZBItems[i].setPrimaryKey(keys[i]);
    	return dJZBItems;
    }
    /**
     * <p>向数据库插入一个VO对象。
     * <p>
     * 创建日期：(2000-10-9)
     * @param DJZBItem nc.vo.ep.dj.DJZBItemVO
     * @param foreignKey String
     * @exception java.sql.SQLException 异常说明。
     * @throws DAOException
     */
    private DJZBItemVO[] insertItems(
        DJZBItemVO[] dJZBItems,
        DJZBHeaderVO head )
        throws  DAOException {
    	
    	SystemProfile.getInstance().log("arapbillsave insert items start");
    	
        if(null==dJZBItems)
            return null;
        //解析单据表体，
        Vector<DJFBItemVO> vFrees = new Vector<DJFBItemVO>();
        Vector<DjfkxybVO> vFkxy = new Vector<DjfkxybVO>();
        for (int i = 0; i < dJZBItems.length; i++) {
            dJZBItems[i].setDjbh(head.getDjbh());
            dJZBItems[i].setVouchid(head.getVouchid());
        }
        DJZBItemVO[] items = insertItems(dJZBItems );
        
        SystemProfile.getInstance().log("arapbillsave insert items end");
        
        //加自由项
        DJFBItemVO[] frees = null;
        DjfkxybVO[] fkxybs = null;
        for (int k = 0; k < items.length; k++) {
            items[k].setStatus(VOStatus.UPDATED);
            //自由项
            frees = items[k].items;
            if (frees != null) {
                for (int i = 0; i < frees.length; i++) {
                    frees[i].setVouchid(head.getVouchid());
                    frees[i].setFb_oid(items[k].getFb_oid());
                    //insertFree(frees[i],con);
                    vFrees.addElement(frees[i]);
                }
            }
            //单据付款协议表
            fkxybs = items[k].fkxyvos;
            if (fkxybs != null) {
                for (int i = 0; i < fkxybs.length; i++) {
                    if (fkxybs[i] != null) {
                        fkxybs[i].setVouchid(head.getVouchid());
                        fkxybs[i].setFb_oid(items[k].getFb_oid());
                        vFkxy.addElement(fkxybs[i]);
                    }
                }
            }
        }
        
        SystemProfile.getInstance().log("arapbillsave insert free start");
        
        //加自由项
        if (vFrees != null) {
            frees = new DJFBItemVO[vFrees.size()];
            vFrees.copyInto(frees);
            defdao.insertFrees(frees );
        }
        
        SystemProfile.getInstance().log("arapbillsave insert free end");
        
        if(vFkxy!=null){
            fkxybs = new DjfkxybVO[vFkxy.size()];
            vFkxy.copyInto(fkxybs);
            fkdao.insert (fkxybs );
        }
        
        SystemProfile.getInstance().log("arapbillsave insert fkxyb end");
        
        //加单据付款协议表
        return items;
    }
    /**
     * <p>向数据库插入一个VO对象（并账专用）。
     * <p>
     * 创建日期：(2000-10-9)
     * @param DJZBItem nc.vo.ep.dj.DJZBItemVO
     * @param foreignKey String
     * @exception java.sql.SQLException 异常说明。
     * @throws DAOException
     */
    public DJZBItemVO[] insertItemsForBZ(
        DJZBItemVO[] dJZBItems )
        throws  DAOException {
        if(null==dJZBItems)
            return null;
        //解析单据表体，
        Vector<DJFBItemVO> vFrees = new Vector<DJFBItemVO>();
        Vector<DjfkxybVO> vFkxy = new Vector<DjfkxybVO>();
        DJZBItemVO[] items = insertItems(dJZBItems );
        //加自由项
        DJFBItemVO[] frees = null;
        DjfkxybVO[] fkxybs = null;
        for (int k = 0; k < items.length; k++) {
//            items[k].setStatus(VOStatus.UPDATED);
            //自由项
            frees = items[k].items;
            if (frees != null) {
                for (int i = 0; i < frees.length; i++) {
                    frees[i].setVouchid(items[k].getVouchid());
                    frees[i].setFb_oid(items[k].getFb_oid());
                    //insertFree(frees[i],con);
                    vFrees.addElement(frees[i]);
                }
            }
            //单据付款协议表
            fkxybs = items[k].fkxyvos;
            if (fkxybs != null) {
                for (int i = 0; i < fkxybs.length; i++) {
                    if (fkxybs[i] != null) {
                        fkxybs[i].setVouchid(items[k].getVouchid());
                        fkxybs[i].setFb_oid(items[k].getFb_oid());
                        vFkxy.addElement(fkxybs[i]);
                    }
                }
            }
        }
        //加自由项
        if (vFrees != null) {
            frees = new DJFBItemVO[vFrees.size()];
            vFrees.copyInto(frees);
            defdao.insertFrees(frees );
        }
        if(vFkxy!=null){
            fkxybs = new DjfkxybVO[vFkxy.size()];
            vFkxy.copyInto(fkxybs);
            fkdao.insert (fkxybs );
        }
        //加单据付款协议表
        return items;
    }
    /**
     * 用一个VO对象的属性更新数据库中的值。
     *
     * 创建日期：(2000-10-9)
     * @param dJZBHeader nc.vo.ep.dj.DJZBHeaderVO
     * @exception java.sql.SQLException 异常说明。
     * @throws DAOException
     */
    public boolean officialPrint(DJZBHeaderVO dJZBHeader) throws  DAOException {
    	pubdao.updateObjectPartly(dJZBHeader,zbmeta,new String[]{ "officialprintuser","officialprintdate" });
    	return true;
    }
    /**
     * 单据表体挂起或取消挂起
     *
     * 创建日期：(2000-10-9)
     * @param dJZBItem nc.vo.ep.dj.DJZBItemVO
     * @exception java.sql.SQLException 异常说明。
     * @throws SQLException
     * @throws BusinessException
     */
    public String pausetransact(String key, String pausetransact)
    	throws  SQLException, BusinessException {
      	DJZBItemVO item=new DJZBItemVO();
      	item.setPrimaryKey(key);
      	item.setPausetransact(new UFBoolean(pausetransact.substring(0,1)));
    	pubdao.updateObjectPartly(item,fbmeta,new String[]{ "pausetransact" });

    	String strCurrentTS = CurTime.getCurrentTimeStampString();
		DJZBHeaderVO header=new DJZBHeaderVO();
		header.setTs(new UFDateTime(strCurrentTS));
    	String cond="where vouchid in (select vouchid from arap_djfb where fb_oid='"+key+"') and ts='"+pausetransact.substring(1)+"'";
    	pubdao.updateObjectPartly(header,zbmeta,new String[]{ "ts" },cond);

//    	if(DjVOTreaterAid.hasSettleInfo(header)){
//	    	DJZBHeaderVO head=this.findHeaderByPrimaryKey(this.findItemByPrimaryKey(key).getVouchid());
//	    	CmpMsg msg=new CmpMsg();
//	    	msg.setPk_corp(head.getDwbm());
//	    	msg.setBillkey(head.getVouchid());
//	    	msg.setBilltype(head.getDjlxbm());
//	    	msg.setStatus(UFBoolean.TRUE.equals(new UFBoolean(pausetransact.substring(0,1)))?BusiStatus.HangUp:ArapSettlementAid.getBillStatus(head));
//
//	    	ISettle4HangUp hangup=NCLocator.getInstance().lookup(nc.itf.cmp.settlement.ISettle4HangUp.class);
//	    	hangup.updateBillStatus(msg);
//    	}

    	return strCurrentTS;
    }

    /**
     * <p>根据key(where条件)查询单据集合(包括 表头,表体)。
     * <p>
     * 创建日期：(2001-10-11)
     * @param key ??dbFieldType??
     * @throws Exception
     * @exception java.sql.SQLException 异常说明。
     作者:陈飞
     */
	@SuppressWarnings("unchecked")
	public DJZBVO[] queryDjAll_Hz(DjCondVO djcond) throws   Exception {
    	DJZBHeaderVO[] headers = null;
    	String key=djcond.getSqlWhere();
    	//查询单据表头集合
    	headers = queryHead_hz(key,true);
    	if (headers == null || headers.length < 1)
    		return null;
		DJZBItemVO[] items = findItemsForHeader_Hz(getVouchidsByHeadVo(headers),djcond);
		if (items != null) {
			for (DJZBItemVO itemVO : items) {
				itemVO.setYbye(itemVO.getYbye().sub(itemVO.getOccupationmny()));
			}
		}

		fillFreeItems(items);
		return assembleDjzbVo(headers, items);

//    	DJZBItemVO[] items = null;
//    	for (int j = 0; j < headers.length; j++) {
//    	    vo = new DJZBVO();//复位
//    		header = new DJZBHeaderVO();//复位
//    		header = headers[j];
////    		HashMap map1 = null;
//    		if (header != null && header.getPrimaryKey() != null) {
//
//    	        //根据单据主键查询表体集合
//    			items = findItemsForHeader_Hz(header.getPrimaryKey(),djcond);
//    			fillFreeItems(items);
////        		List<String> ls = new ArrayList<String>();
////        		for(DJZBItemVO vo1 : items){
////        			ls.add(vo1.getFb_oid());
////        		}
////        		map1 = defdao.findItemvosForHeader(ls.toArray(new String[ls.size()]));
//    		}
////    		for (int i = 0; i < items.length; i++) {
////    			//查询自由项
//////    			DJFBItemVO[] frees = defdao.findItemsForHeader(items[i].getFb_oid());
//////    			items[i].items = frees;
////    			items[i].items = (DJFBItemVO[]) map1.get(items[i].getFb_oid());
////    		}
//
//    		vo.setParentVO(header);
//    		vo.setChildrenVO(items);
//    		vos[j]=vo;
//    	}
//    	return ret.size()==0?null:ret.toArray(new DJZBVO[]{});
    }


	   /**
     *
     * @fuction 通过主键查找一个VO对象。
     * @author maji
     * @since V6.0
     * @see queryDjLb_djcond\queryDjLbQ_Wszz\queryDjLb_djcond\queryDjLb_Yhrq\
     * @exception java.sql.SQLException
     */
    public Vector<DJZBHeaderVO> queryDjLb(Integer initPos,Integer count,DjCondVO djcond) throws SQLException,Exception {
    	//maji_TODO 需要在PubMethods中增加一个可以根据表名添加inner join的方法，将签字确认整合进去，而不必用if
        StringBuilder sqlFromClause=new StringBuilder();
        
        String fromzfb = " from arap_djzb zb inner join arap_djfb fb on zb.vouchid=fb.vouchid";
        String fromzb = " from arap_djzb zb ";
        
        String strSqlFromClause = null;
        //处理我审批待审批查询条件
        dealMyAuditCond(djcond,sqlFromClause,djcond.operator);
        QryCondArrayVO[] norCondVos = djcond.m_NorCondVos;
        PubMethods pm=new PubMethods();
        pm.setBHasDataPower(djcond.m_UseFlag==111);
        //pm.setBHasDataPower(true);
        sqlFromClause.append(pm.getBillQuerySubSql_Dj(
                        norCondVos,
                        djcond.defWhereSQL,
                        djcond.pk_corp,
                        djcond.operator));
        ;
        if(null !=djcond.getSqlWhere()&& djcond.getSqlWhere().trim().length()>0){
        	sqlFromClause.append(djcond.getSqlWhere());
        }
        //替换核销状态查询条件zb.zb.verifystatus
        strSqlFromClause = SqlUtils.replaceZbVeriStatus_1(sqlFromClause.toString())+ " order by zb.djrq,zb.djlxbm,zb.djbh ";
        Vector<DJZBHeaderVO> djzbheader = null;

        if(strSqlFromClause.indexOf("fb")!=-1){
        	strSqlFromClause=fromzfb+strSqlFromClause;
        }else{
        	strSqlFromClause=fromzb+strSqlFromClause;
        }
        
        djzbheader = this.getDJZBHeaderVOsUniversalVector(initPos,count,strSqlFromClause, djcond.isLinkPz,djcond.VoucherFlag);
        return djzbheader;
    }

    private void dealMyAuditCond(DjCondVO djcond,StringBuilder sqlFromClause,String operator) {
		if (null != djcond.m_NorCondVos && djcond.m_NorCondVos.length > 0) {
			List<QryCondArrayVO> ls = new ArrayList<QryCondArrayVO>();
			Boolean flag = false;
			String value = null ;
			String sql = null;
			for (QryCondArrayVO vo : djcond.m_NorCondVos) {//删除audit查询条件
				for (QryCondVO vo1 : vo.getItems()) {
					if (vo1.getQryfld().equals("audit")) {
						flag = true;
						value = vo1.getValue();
					}
				}
				if(!flag){
					ls.add(vo);
				}
			}
			if (flag) {//查询条件包含待我审批或已审批的项
				if (value.equals("1")) {
					sql= "((wf.checkman = '"
							+ operator
							+ "' AND wf.dr = 0 and wf.approvestatus = 0 and wf.actiontype <> 'BIZ' and  wt.tasktype = 2) or wf.billid is  null ) AND djzt = 1";
				} else {
					sql= "((wf.checkman = '"
							+ operator
							+ "' AND wf.dr = 0 and wf.approvestatus = 1 and wf.actiontype <> 'BIZ' and  wt.tasktype = 2) or zb.shr = '"+ operator + "')";
				}
				if(null != djcond.defWhereSQL && djcond.defWhereSQL.length() >0){
					djcond.defWhereSQL += "AND" + sql;
				}else{
					djcond.defWhereSQL = sql;
				}
				sqlFromClause.append(" left join pub_workflownote wf on zb.vouchid=wf.billid ");
				sqlFromClause.append(" left join pub_wf_task wt on wt.pk_wf_task = wf.pk_wf_task ");
			}
			djcond.m_NorCondVos = ls.toArray(new QryCondArrayVO[ls.size()]);
		}

	}
    /**
     * 通过主键查找一个VO对象。
     *
     * 创建日期：(2000-10-9)
     * @return nc.vo.ep.dj.DJZBHeaderVO
     * @param key String
     * @exception java.sql.SQLException 异常说明。
     nc.vo.pub.query.RefResultVO[] refs 多单位查询 单位vo集合
     */
    public Vector<DJZBHeaderVO> queryDjLbQ_djcond(Integer initPos,Integer count,DjCondVO djcond) throws SQLException, Exception {

        StringBuilder sqlFromClause=new StringBuilder();
        
        String fromzfb = " from arap_djzb zb inner join arap_djfb fb on zb.vouchid=fb.vouchid";
        String fromzb = " from arap_djzb zb ";
        
        dealMyAuditCond(djcond,sqlFromClause,djcond.operator);
        nc.vo.arap.pub.QryCondArrayVO[] norCondVos = djcond.m_NorCondVos;
        nc.vo.pub.query.ConditionVO[] defCondVos = djcond.m_DefCondVos;
        String strSql = null;
        nc.bs.arap.pub.PubMethods pubmethods = new nc.bs.arap.pub.PubMethods();

        String zgyfWhere="";
	        int zgyf=0;
	        if(null != defCondVos){
		        for(int i=0,size=defCondVos.length;i<size;i++){
			    	if("zb.zgyf".equalsIgnoreCase(defCondVos[i].getFieldCode())&&defCondVos[i].getValue()!=null&&defCondVos[i].getValue().length()>0){
			    		if(Integer.valueOf(defCondVos[i].getValue()).intValue()>2)
			    			zgyf=1;
			    		else
			    			zgyf=2;
			    		defCondVos[i].setValue(String.valueOf( Integer.valueOf(defCondVos[i].getValue()).intValue()%3));
			    	}
			    }
	        }
	        if(zgyf==1)
	        	zgyfWhere="and zb.djdl ='ys'";
	        else if (zgyf==2)
	        	zgyfWhere="and zb.djdl ='yf'";

            nc.bs.arap.pub.CreatJoinSQLTool jointool = new nc.bs.arap.pub.CreatJoinSQLTool();
            Vector<DJZBHeaderVO> vHead = new Vector<DJZBHeaderVO>();
            if (jointool.checkLegal(djcond.pk_corp)) {
                strSql =sqlFromClause.toString()+ pubmethods.getBillQuerySubSql_Dj(norCondVos,defCondVos,djcond.pk_corp,djcond.operator)+zgyfWhere+ " and " + djcond.defWhereSQL;
		        strSql = SqlUtils.replaceZbVeriStatus_1(strSql)+ " order by zb.djrq,zb.djlxbm,zb.djbh ";
		        
		        if(strSql.indexOf("fb")!=-1){
		        	strSql=fromzfb+strSql;
		        }else{
		        	strSql=fromzb+strSql;
		        }
		        
                vHead = this.getDJZBHeaderVOsUniversalVector(initPos,count,strSql, djcond.isLinkPz,djcond.VoucherFlag);
            } else {
    	         nc.vo.arap.pub.QryCondArrayVO condCorp = pubmethods.findCorpCond(norCondVos);
    	         String[] pk_corps = djcond.pk_corp;
    	         if(condCorp!=null){
    		         condCorp.setItems(new nc.vo.arap.pub.QryCondVO[]{condCorp.getItems()[0]});
    	         }
    	         int curcount=count;
    	         pubmethods.setBHasDataPower(true);
    	         for(int i=0;i<pk_corps.length&&curcount>0;i++){
    		        if(condCorp!=null){
    			        condCorp.getItems()[0].setValue(pk_corps[i]);
    		        }
    		        setCorp(norCondVos,pk_corps[i]);
    		        if(null != djcond.defWhereSQL && djcond.defWhereSQL.length() > 0){
    		        	 strSql =sqlFromClause.toString()+ pubmethods.getBillQuerySubSql_Dj(norCondVos,defCondVos,new String[]{pk_corps[i]},djcond.operator)+zgyfWhere + " and " + djcond.defWhereSQL ;
    		        }else{
    		        	 strSql =sqlFromClause.toString()+ pubmethods.getBillQuerySubSql_Dj(norCondVos,defCondVos,new String[]{pk_corps[i]},djcond.operator)+zgyfWhere ;
    		        }
    		        //替换核销状态查询条件zb.zb.verifystatus
    		        strSql = SqlUtils.replaceZbVeriStatus_1(strSql)+ " order by zb.djrq,zb.djlxbm,zb.djbh ";
    		        
    		        if(strSql.indexOf("fb")!=-1){
    		        	strSql=fromzfb+strSql;
    		        }else{
    		        	strSql=fromzb+strSql;
    		        }
    		        
    		        Vector<DJZBHeaderVO> vTemp = this.getDJZBHeaderVOsUniversalVector(initPos,curcount,strSql, djcond.isLinkPz,djcond.VoucherFlag);
    		        curcount=curcount-vTemp.size();
    		        vHead.addAll(vTemp);
    	         }
            }
        return vHead;
    }



    private void setCorp(QryCondArrayVO[] defCondVos,String corp){
    	for(QryCondArrayVO vo : defCondVos){
    		for(QryCondVO vo1 : vo.getItems()){
    			if(vo1.getQryfld().equals("dwbm")){
    				vo1.setObjValues(Arrays.asList(new String[]{corp}));
    			}
    		}
    	}
    }
    /**
     * 通过主键查找一个VO对象。
     *
     * 创建日期：(2000-10-9)
     * @return nc.vo.ep.dj.DJZBHeaderVO
     * @param key String
     * @throws DAOException
     * @throws SQLException
     * @throws SQLException
     */
    public DJZBHeaderVO[] queryHead(String key) throws DAOException, SQLException{
    	String where=nc.vo.jcom.lang.StringUtil.replaceIgnoreCase(key,"arap_djzb." , "zb.");
    	String sqlFromClause =null;
		sqlFromClause = "  from arap_djzb zb inner join arap_djfb fb on zb.vouchid=fb.vouchid "+where+" ";
    	DJZBHeaderVO[] djzbheader = null ;
    	djzbheader = this.getDJZBHeaderVOsUniversalArray(sqlFromClause, false,null);
    	return djzbheader;
    }

    /**
     * 通过主键查找一个VO对象。
     *
     * 创建日期：(2000-10-9)
     * @return nc.vo.ep.dj.DJZBHeaderVO
     * @param key String
     * @throws DAOException
     * @throws SQLException
     * @throws SQLException
     */
    public DJZBHeaderVO[] queryHeadbyTS(String key) throws DAOException, SQLException  {

        String sqlFromClause = key;
    	DJZBHeaderVO[] djzbheader = null ;
    		djzbheader = this.getDJZBHeaderVOsUniversalArray(sqlFromClause ,false,null);
    	return djzbheader;
    }

    /**
     * 用一个VO对象的属性更新数据库中的值。
     *
     * 创建日期：(2000-10-9)
     * @param dJZBHeader nc.vo.ep.dj.DJZBHeaderVO
     * @exception java.sql.SQLException 异常说明。
     * @throws DbException
     */
    public void setBankRecivePk(String fboid, String pk_bankrecive,String ts )
        throws java.sql.SQLException ,BusinessException, DbException {

      	DJZBItemVO item=new DJZBItemVO();
      	item.setPrimaryKey(fboid);
      	if (pk_bankrecive != null) {
            item.setTbbh(pk_bankrecive);
        }
    	String cond=" fb_oid = '"+ fboid+"' and dr = 0 ";
    	pubdao.updateObjectPartly(item,fbmeta,new String[]{"tbbh"  },cond);
    	String sql  = "update arap_djzb set zyx1 = zyx1 where vouchid in (select vouchid from arap_djfb where fb_oid = '"+fboid+"')";
      	PersistenceManager pm=null;
        try{
            pm=PersistenceManager.getInstance(getds());
            pm.getJdbcSession().executeUpdate(sql );
        }finally{
            pm.release();
        }
    }
    /**
     * 用一个VO对象的属性更新数据库中的值。
     *
     * 创建日期：(2000-10-9)
     * @param dJZBItem nc.vo.ep.dj.DJZBItemVO
     * @exception java.sql.SQLException 异常说明。
     fb_oid付表主键　,取消标志时othersysflag=null 打上标志时为系统明如：othersysflag='销售管理',
     取消标志时pausetransact=false,打上标志时pausetransat=true
     作者:陈飞
     * @throws DAOException
     * @throws DbException
     */
    public void setOtherSysFlag(
        String fb_oid,
        String othersysflag,
        UFBoolean pausetransact)
        throws   DAOException, DbException {

      	DJZBItemVO item=new DJZBItemVO();
      	item.setPrimaryKey(fb_oid);
      	item.setOthersysflag(othersysflag);
      	item.setPausetransact(pausetransact);
    	pubdao.updateObjectPartly(item,fbmeta,new String[]{"othersysflag","pausetransact" });

        String sqlHead = "update arap_djzb set dr=dr where vouchid = (select vouchid from arap_djfb where fb_oid='"+fb_oid+"')";
    	PersistenceManager pm=null;
        try{
            pm=PersistenceManager.getInstance(getds());
            pm.getJdbcSession().executeUpdate(sqlHead);
        }finally{
            pm.release();
        }
    }
    /**
     * <p>使用VO的值更新母子表。
     * <p>
     * 创建日期：(2000-10-9)
     * @param vo nc.vo.ep.dj.DJZBVO
     * @exception java.sql.SQLException 异常说明。
     */
    public DJZBVO update(DJZBVO vo)
    throws SQLException, BusinessException, SystemException, Exception {
    convertDJZBVO(vo);
//    删除固定资产租金信息所用dmo
//    nc.bs.fa.outer.RentDMO rentdmo=new nc.bs.fa.outer.RentDMO();
    nc.bs.arap.global.ArapClassRunBO executeBo = new
    nc.bs.arap.global.ArapClassRunBO();
    DJZBItemVO[] items = (DJZBItemVO[]) vo.getChildrenVO();
    DJZBItemVO[] newItems = null;
    String itemkey = "";
    int delCount = 0, k = 0;


    Map<DJZBItemVO,String> cmpMap=new HashMap<DJZBItemVO,String>();
    for(DJZBItemVO item:items){
    cmpMap.put(item,item.getFb_oid()==null?"":item.getFb_oid());
    }

    for (int i = 0; i < items.length; i++) {
    switch (items[i].getStatus()) {
    case VOStatus.NEW :
    itemkey = insertItem(items[i]);
    items[i].setPrimaryKey(itemkey);
    items[i].setStatus(VOStatus.UPDATED);
    break;
    case VOStatus.UPDATED :
    updateItem(items[i]);
    break;
    case VOStatus.DELETED :
    {
////    删除固定资产租金信息
//    rentdmo.delete(items[i].getFb_oid());

//    删除表体行
    deleteItem(items[i]);

    nc.bs.arap.global.PubBO pub = new nc.bs.arap.global.PubBO();
//    如果启用全成本
    if (pub.costIsUsed()) {
//    调用全成本接口删除表体行相应的全成本信息
//    nc.bs.bank.costContent.CostcontentDMO costDMO =
//     new nc.bs.bank.costContent.CostcontentDMO();
//    costDMO.delete(items[i].getVouchid(), items[i].getFb_oid());
    Class[] paramtype = {String.class,String.class};
    Object[] param = {items[i].getVouchid(),items[i].getFb_oid()};
    executeBo.runMethod("nc.bs.bank.costContent.CostcontentDMO","delete",paramtype,param);
    }

    delCount++;
    }
    }
    }
    DJZBHeaderVO head=(DJZBHeaderVO) vo.getParentVO();
    head=updateHeader(head);
    String ts=getTsByPrimaryKey(head.getPrimaryKey(),"arap_djzb");
    head.setTs(new UFDateTime(ts));
    vo.setParentVO(head);
    if (delCount < 1)
    newItems = items;
    else
    if (delCount == items.length)
    newItems = null;
    else {
    newItems = new DJZBItemVO[items.length - delCount];
    for (int i = 0; i < items.length; i++) {
    if (items[i].getStatus() == VOStatus.DELETED)
    k++;
    else
    newItems[i - k] = items[i];
    }
    }
    vo.setChildrenVO(newItems);

    Map<String,String> retMap=new HashMap<String,String>();
    for(DJZBItemVO item:newItems){
    retMap.put(cmpMap.get(item).length()==0?item.getFb_oid():cmpMap.get(item),
    item.getFb_oid());
    }
//    vo.setChildrenVO(items);
    vo.setCmpMap(retMap);
    return vo;

    }
    /**
     * <p>使用VO的值更新母子表。
     * <p>
     * 创建日期：(2000-10-9)
     * @param vo nc.vo.ep.dj.DJZBVO
     * @exception java.sql.SQLException 异常说明。
     */
    public DJZBVO update_SS(DJZBVO vo)
    	throws SQLException, BusinessException, SystemException, Exception {
    	convertDJZBVO(vo);
    	DJZBItemVO[] items = (DJZBItemVO[]) vo.getChildrenVO();
    	DJZBItemVO[] newItems = null;
    	String itemkey = "";
    	int delCount = 0, k = 0;
    	for (int i = 0; i < items.length; i++) {
    		switch (items[i].getStatus()) {
    			case VOStatus.NEW :
    				itemkey = insertItem_SS(items[i]);
    				items[i].setPrimaryKey(itemkey);
    				items[i].setStatus(VOStatus.UPDATED);
    				break;
    			case VOStatus.UPDATED :
    				updateItem_SS(items[i]);
    				break;
    			case VOStatus.DELETED :
    				{
    					deleteItem_SS(items[i]);

    					nc.bs.arap.global.PubBO pub = new nc.bs.arap.global.PubBO();
    					//如果启用全成本
    					if (pub.costIsUsed()) {
    						//调用全成本接口删除表体行相应的全成本信息
    						//nc.bs.bank.costContent.CostcontentDMO costDMO =
    							//new nc.bs.bank.costContent.CostcontentDMO();
    						//costDMO.delete(items[i].getVouchid(), items[i].getFb_oid());
    						nc.bs.arap.global.ArapClassRunBO executeBo = new nc.bs.arap.global.ArapClassRunBO();

    						Class[] paramtype = {String.class,String.class};
    						Object[] param = {items[i].getVouchid(),items[i].getFb_oid()};
    						executeBo.runMethod("nc.bs.bank.costContent.CostcontentDMO","delete",paramtype,param);
    					}

    					delCount++;
    				}
    		}
    	}
    	DJZBHeaderVO head=(DJZBHeaderVO) vo.getParentVO();
    	updateHeader_SS(head);
    	String ts=getTsByPrimaryKey(head.getPrimaryKey(),"arap_item");
    	head.setTs(new UFDateTime(ts));
    	vo.setParentVO(head);
    	if (delCount < 1)
    		newItems = items;
    	else
    		if (delCount == items.length)
    			newItems = null;
    		else {
    			newItems = new DJZBItemVO[items.length - delCount];
    			for (int i = 0; i < items.length; i++) {
    				if (items[i].getStatus() == VOStatus.DELETED)
    					k++;
    				else
    					newItems[i - k] = items[i];
    			}
    		}
    	vo.setChildrenVO(newItems);
    	return vo;
    }

    /**
     * 用一个VO对象的属性更新数据库中的值。
     *
     * 创建日期：(2000-10-9)
     * @param dJZBHeader nc.vo.ep.dj.DJZBHeaderVO
     * @exception java.sql.SQLException 异常说明。
     * Modification log
     * 2004-11-04,wangqiang
     * Add the column of sxbz（生效状态） and jszxzf(结算中心支付）
     * @throws DAOException
     */
    public DJZBHeaderVO updateHeader(DJZBHeaderVO dJZBHeaders) throws   DAOException {

        pubdao.updateObject(new DJZBHeaderVO[]{dJZBHeaders},zbmeta,null);
    	return dJZBHeaders;
    }
    /**
     * 用一个VO对象的属性更新数据库中的值。
     *
     * 创建日期：(2000-10-9)
     * @param dJZBHeader nc.vo.ep.dj.DJZBHeaderVO
     * @exception java.sql.SQLException 异常说明。
     * Modification log
     * 2004-11-04,wangqiang
     * Add the column of sxbz（生效状态）
     * @throws DAOException
     */
    private DJZBHeaderVO updateHeader_SS(DJZBHeaderVO  dJZBHeader ) throws  DAOException {
        pubdao.updateObject(new DJZBHeaderVO[]{dJZBHeader},ssmeta,null);
    	 return dJZBHeader;
    }
    /**
     * 用一个VO对象的属性更新数据库中的值。
     *
     * 创建日期：(2000-10-9)
     * @param dJZBItem nc.vo.ep.dj.DJZBItemVO
     * @exception java.sql.SQLException 异常说明。
     * @throws DAOException
     */
    private void updateItem (DJZBItemVO  dJZBItem )
    	throws   BusinessException {
        FreeDAO defdao=new FreeDAO();
        DjfkxybDAO fkdao=new DjfkxybDAO();
             DJFBItemVO[] items = (DJFBItemVO[]) dJZBItem .getChildrenVO();
//             if(items!=null)
//                 defdao.insertFrees(items);
             if(items==null)
         		;
         	else
         	{
         		for (int i = 0; i < items.length; i++) {
         			switch (items[i].getStatus()) {
         			case VOStatus.NEW :
         				defdao.insertFree(items[i]);
         				break;
         			case VOStatus.UPDATED :
         				defdao.updateFrees(new DJFBItemVO[]{items[i]});
         				break;
         			case VOStatus.DELETED :
         				defdao.deleteFree(items[i]);
         			}
         		}
         	}
    	//修改付款协议表
    	fkdao.deleteFkxyforFB(dJZBItem .getFb_oid());
    	//insertFKXY()
    	//加单据付款协议表
    	DjfkxybVO[] fkxybs = dJZBItem.fkxyvos;
    	if (fkxybs != null) {
    		for (int i = 0; i < fkxybs.length; i++) {
    			if(fkxybs[i]==null)
    				continue;
    			fkxybs[i].setVouchid(dJZBItem.getVouchid());
    			fkxybs[i].setFb_oid(dJZBItem.getFb_oid());
    		}
    		fkdao.insert(fkxybs );
    	}
    	basedao.updateObject(dJZBItem,fbmeta);
    }
    /**
     * 用一个VO对象的属性更新数据库中的值。
     *
     * 创建日期：(2000-10-9)
     * @param dJZBItem nc.vo.ep.dj.DJZBItemVO
     * @exception java.sql.SQLException 异常说明。
     * @throws DAOException
     */
    private void updateItem_b(DJZBItemVO dJZBItem)
    	throws   DAOException {
    	pubdao.updateObjectPartly( dJZBItem,bbmeta,new String[]{"ybye","fbye","bbye","closedate","closer" });
    }
    /**
     * 用一个VO对象的属性更新数据库中的值。
     *
     * 创建日期：(2000-10-9)
     * @param dJZBItem nc.vo.ep.dj.DJZBItemVO
     * @exception java.sql.SQLException 异常说明。
     * 2004-08-09,wangqiang,modification log
     *      add the source code: when the balance of SS bill item is reduce to zero,close it automatically
     * @throws DAOException
     * @throws SQLException
     *
     */
    public void updateItem_b_array(DJZBItemVO[] dJZBItems)
    	throws   DAOException, SQLException {
    	 for(int i=0;i<dJZBItems.length;i++){
    		 DjVOTreaterAid.convertItemVO(dJZBItems[i]);
    	 	updateItem_b(dJZBItems[i]);
    	 	//check whether the balance is reduced to zero,if that is the case,close the item.
    	 	if(dJZBItems[i].getYbye().compareTo(ArapConstant.DOUBLE_ZERO)==0)
    	 	   close_SSItem(new DJZBItemVO[]{dJZBItems[i]});
    	 }
    }
//    /**
//     * author     : wangqiang
//     * create time: 2004-08-09
//     * function   : same as the homonymous method with two paramenter
//     *      this method will replace that one to support the automatic item close
//     *      operation
//     *
//     * @param dJZBItem nc.vo.ep.dj.DJZBItemVO
//     * @exception java.sql.SQLException 异常说明。
//     * 2004-08-09,wangqiang,modification log
//     *      add the source code: when the balance of SS bill item is reduce to zero,close it automatically
//     * @throws DAOException
//     *
//     */
//    private void updateItem_b_array(DJZBItemVO[] dJZBItems,String strCloser,nc.vo.pub.lang.UFDate dateClose)
//    	throws java.sql.SQLException,   DAOException {
//
//    	 for(int i=0;i<dJZBItems.length;i++){
//    	 	updateItem_b(dJZBItems[i]);
//    	 	//check whether the balance is reduced to zero,if that is the case,close the item.
//    	 	if(dJZBItems[i].getYbye().doubleValue()==0){
//    		   dJZBItems[i].setCloser(strCloser);
//    		   dJZBItems[i].setClosedate(dateClose);
//    	 	   close_SSItem(new DJZBItemVO[]{dJZBItems [i]});
//    	 	}
//    	 }
//    }
    /**
     * 用一个VO对象的属性更新数据库中的值。
     *
     * 创建日期：(2000-10-9)
     * @param dJZBItem nc.vo.ep.dj.DJZBItemVO
     * @exception java.sql.SQLException 异常说明。
     * @throws BusinessException
     */
    private void updateItem_SS(DJZBItemVO dJZBItem)
    	throws  BusinessException {
    	//修改自由项
        FreeDAO defdao=new FreeDAO();
    	defdao.updateZYX(dJZBItem);
    	pubdao.updateObject(new DJZBItemVO[]{dJZBItem},bbmeta,null);
    }
    /**
     * 用一个VO对象的属性更新数据库中的值。
     *
     * 创建日期：(2000-10-9)
     * @param dJZBItem nc.vo.ep.dj.DJZBItemVO
     * @exception java.sql.SQLException 异常说明。
     * @throws BusinessException
     */
    private void updateItem_SS(DJZBItemVO[] dJZBItems)
    	throws  BusinessException {
    	//修改自由项

        FreeDAO defdao=new FreeDAO();
        for(int i=0,size=dJZBItems.length;i<size;i++)
            defdao.updateZYX(dJZBItems[i]);
    	pubdao.updateObject(dJZBItems,bbmeta,null);
    }
    /**
     * 网上转账 设置转账状态
     *
     * 创建日期：(2000-10-9)
     * @param dJFBItem nc.vo.ep.dj.DJFBItemVO
     * @exception java.sql.SQLException 异常说明.
     zzzt=1已经转账,0未转账
     * @throws DAOException
     */
    public void wszz(DJZBVO[] djzbs) throws  DAOException {
        for(DJZBVO djzb:djzbs){
	    	DJZBHeaderVO head=(DJZBHeaderVO)djzb.getParentVO();
	    	head.setJszxzf(DJZBVOConsts.m_intJSZXZF_NetBank);
	    	pubdao.updateObjectPartly(head,zbmeta,new String[]{"zzzt","jszxzf","payman","paydate"});
	    	DJZBItemVO[] items=(DJZBItemVO[])djzb.getChildrenVO();
	    	wszzFB(items);
        }
    }
  public void wszzFB(DJZBItemVO[] items) throws  DAOException {
    	pubdao.updateObjectPartly(items,fbmeta,new String[]{"payflag","payman","paydate"},null);
    }
    /**
     * 单据打上预收付标志
     *
     * 创建日期：(2000-10-9)
     * @param vouchid 单据主键
     * @exception java.sql.SQLException 异常说明。
     * @throws DAOException
     */
    public void ysfDj(String vouchid) throws   DAOException {
        DJZBHeaderVO vo=new DJZBHeaderVO();
        vo.setPrepay(UFBoolean.TRUE);
        vo.setPrimaryKey(vouchid);
        pubdao.updateObjectPartly(vo,zbmeta,new String[]{"prepay"});
    }
    public Vector queryDjTemplatePK(String pk_djlx,int syscode) throws Exception
    {
    	String sql = "select pk_billtemplet,djlxmc from arap_djlxtemplet a inner join arap_djlx b on a.pk_djlx=b.djlxoid where pk_djlx=? and syscode=? and a.dr=0 and b.dr=0";

    	PersistenceManager pm=null;
    	ResultSet rs=null;
        try{
            pm=PersistenceManager.getInstance(getds());
            return (Vector) pm.getJdbcSession().executeQuery(sql,new  ResultSetProcessor(){

                /**
				 *
				 */
				private static final long serialVersionUID = -8315714240534632150L;

				public Object handleResultSet(ResultSet rs) throws SQLException {
                    //
                    Vector<String> result=new Vector<String>();
                    while(rs.next())
            		{
            			result.add(rs.getString(1));
            			result.add(rs.getString(2));
            		}
                    return result;
                }

            });

        	} finally {
        	    if(null!=rs)
        	        rs.close();
        	    pm.release();
        	}

    }
    /**
     * author:wangqiang
     * creation time:2004-12-15
     * function:method for common use,providing the query of arap_djzb table
     *          note that all the neccesary column of arap_djzb table is included in the select sql clause of this mehtod
     *          therefore this method is basically able to meet the requirement of any query for the arap_djzb
     * @param strSqlFromClause:part of the sql statement starting from "from table_name..."
     * @param con:database connection
     * @param bWithGL:whether GL column is needed
     * @return
     * @throws SQLException
     * @throws DAOException
     */
    public DJZBHeaderVO[] getDJZBHeaderVOsUniversalArray(String strSqlFromClause, boolean bWithGL,Integer bGLChalked)throws DAOException, SQLException{
     	DJZBHeaderVO[] headerVOArray = null ;
    	Vector vHeadVOs = this.getDJZBHeaderVOsUniversalVector(strSqlFromClause, bWithGL,bGLChalked);
    	if(vHeadVOs!=null){
    	    headerVOArray = new DJZBHeaderVO[vHeadVOs.size()];
    	    vHeadVOs.copyInto(headerVOArray);
    	}
     	return headerVOArray;

    }
    private Vector<DJZBHeaderVO> getDJZBHeaderVOsUniversalVector(String strSqlFromClause, boolean bWithGL,Integer bGLChalked)throws DAOException, SQLException{
     	Vector<DJZBHeaderVO> ret= getDJZBHeaderVOsUniversalVector(new Integer(-1),new Integer(-1),strSqlFromClause, bWithGL,bGLChalked);
      	return ret;
    }

    private String getDJZBHeaderPKString(Vector vHeadVO){
        String strPKs = "";
        for(int i=0;i<vHeadVO.size();i++) {
            DJZBHeaderVO head = (DJZBHeaderVO)vHeadVO.elementAt(i);
            strPKs = strPKs + "'" + head.getVouchid() + "',";
        }
        return strPKs;
    }

    private Vector<String> getDJZBHeaderPKVector(Vector vHeadVO){
        Vector<String> vPK =new Vector<String>();
        for(int i=0;i<vHeadVO.size();i++) {
            DJZBHeaderVO head = (DJZBHeaderVO)vHeadVO.elementAt(i);
            vPK.addElement(head.getVouchid());
        }
        return vPK;
    }
    private String createHeaderTempTable( String[] strHeaderPK)throws SQLException, DbException{
        Connection con=PersistenceManager.getInstance(getds()).getJdbcSession().getConnection();
    	PreparedStatement stmt_temp=null;
    	try
    	{
            nc.bs.mw.sqltrans.TempTable tmptab = new nc.bs.mw.sqltrans.TempTable();
            String tablename = tmptab.createTempTable(  con,
                    "arap_djzb_temptable",
                    "vouchid_temp char(20),ts char(19)",
                    "vouchid_temp");
            String sql_temp = " insert into  " + tablename + " (vouchid_temp) values(?)";
            stmt_temp= con.prepareStatement(sql_temp);
            for (int i = 0; i < strHeaderPK.length; i++) {
                stmt_temp.setString(1, strHeaderPK[i]);
                stmt_temp.addBatch();
            }

            stmt_temp.executeBatch();

            return tablename;
    	}
    	finally
    	{
    		if(stmt_temp!=null)
    		{
    			stmt_temp.close();
    		}
    		if(con!=null)
    		    con.close();
    	}
    }
    /**
     * 根据查询条件得到单据表体的vo Vector
     *
     * 创建日期：(2000-10-9)
     * @return nc.vo.ep.dj.DJZBItemVO
     * @param key String
     * @exception java.sql.SQLException 异常说明。
     * @throws DAOException
     */
    public Vector<DJZBItemVO> findItemsByCondition(String strCond) throws  DAOException {
        String strWhereClause = (strCond==null?"":strCond) +" order by vouchid,flbh ";
        return this.getDJZBItemVOUniversalVector( strWhereClause);
    }
    /**根据单据表头Vector得到单据vo 数组*/
    public DJZBVO[] getDjVObyHeaderVos(DJZBHeaderVO[] voHeads) throws SQLException{
        if(voHeads==null || voHeads.length==0){
            return null;
        }
        Vector<DJZBHeaderVO> vHead = new Vector<DJZBHeaderVO>();
        for(int i=0;i<voHeads.length;i++){
            vHead.addElement(voHeads[i]);
        }
        DJZBVO[] vos = getDjVObyHeaders(vHead);
     	return vos;
    }
    @SuppressWarnings("unchecked")
	public DJZBVO[] getDjVObyHeaderVos_del(DJZBHeaderVO[] voHeads) throws SQLException{
        if(voHeads==null || voHeads.length==0){
            return null;
        }
        Vector<DJZBHeaderVO> vHead = new Vector<DJZBHeaderVO>();
        for(int i=0;i<voHeads.length;i++){
            vHead.addElement(voHeads[i]);
        }
        DJZBVO[] vos = null;
        Vector<DJZBItemVO> vItems ;
        try{
    	    String strSubSql = "";
    	    if (vHead.size() > 150) {
    	            Vector pk_v = this.getDJZBHeaderPKVector(vHead);
    	            String[] sPks = new String[pk_v.size()];
    	            pk_v.copyInto(sPks);
    	            String tablename = createHeaderTempTable( sPks);
    	            strSubSql =
    	                "  arap_djfb inner join "
    	                    + tablename
    	                    + " tmp on tmp.vouchid_temp=arap_djfb.vouchid";
    	                    String where= " where dr=1 and xgbh ="+ArapConstant.UNITACCOUNTSTAT_DEFAULT+" ";
    	            String strWhereClause = (where==null?"":where) +" order by vouchid,flbh ";
    	            List<DJZBItemVO> ret=(List<DJZBItemVO>)pubdao.queryVOsByWhereClause(DJZBItemVO.class,fbmeta,strSubSql,strWhereClause);
    	            vItems=new Vector<DJZBItemVO>(ret);

    	        } else {
    	            String pks = getDJZBHeaderPKString(vHead);
    	            strSubSql =" where dr=1 and xgbh ="+ArapConstant.UNITACCOUNTSTAT_DEFAULT+" and vouchid in ( "
    	                    + pks.substring(0, pks.length() - 1)
    	                    + ") ";
    	            vItems = findItemsByCondition(strSubSql);
    	        }
    			//分配
    	        vos = ARAPDjBSUtil.distributeDjzbVOs(vHead,vItems);
        }catch(Exception e){
            Logger.error(e,this.getClass(),e.getMessage());
            throw  new SQLException(nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("2006030102","UPP2006030102-000506")/*@res "查询单据表体行出错："*/+e.getMessage());
        }
     	return vos;
    }
    /**根据单据表头Vector得到单据vo 数组*/
    @SuppressWarnings("unchecked")
    public DJZBVO[] getDjVObyHeaders(Vector vHead) throws SQLException{
         if(vHead == null ||vHead.size()<=0){
            return null;
        }
        DJZBVO[] vos = null;
        Vector<DJZBItemVO> vItems ;
        try{
    	    String strSubSql = "";
    	    if (vHead.size() > 150) {
    	            Vector pk_v = this.getDJZBHeaderPKVector(vHead);
    	            String[] sPks = new String[pk_v.size()];
    	            pk_v.copyInto(sPks);
    	            String tablename = createHeaderTempTable( sPks);
    	            strSubSql =
    	                "  arap_djfb inner join "
    	                    + tablename
    	                    + " tmp on tmp.vouchid_temp=arap_djfb.vouchid";
    	                    String where= " where dr=0 and xgbh ="+ArapConstant.UNITACCOUNTSTAT_DEFAULT+" ";
    	            String strWhereClause = (where==null?"":where) +" order by vouchid,flbh ";
    	            List<DJZBItemVO> ret=(List<DJZBItemVO>)pubdao.queryVOsByWhereClause(DJZBItemVO.class,fbmeta,strSubSql,strWhereClause);
    	            vItems=new Vector<DJZBItemVO>(ret);

    	        } else {
    	            String pks = getDJZBHeaderPKString(vHead);
    	            strSubSql =" where dr=0 and xgbh ="+ArapConstant.UNITACCOUNTSTAT_DEFAULT+" and vouchid in ( "
    	                    + pks.substring(0, pks.length() - 1)
    	                    + ") ";
    	            vItems = findItemsByCondition(strSubSql);
    	        }
    			//分配
    	        vos = ARAPDjBSUtil.distributeDjzbVOs(vHead,vItems);
        }catch(Exception e){
            Logger.error(e,this.getClass(),e.getMessage());
            throw  new SQLException(nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("2006030102","UPP2006030102-000506")/*@res "查询单据表体行出错："*/+e.getMessage());
        }
    	return vos;
    }



     /**
     * 通过主键查找一个VO对象。
     *
     * 创建日期：(2000-10-9)
     * @return nc.vo.ep.dj.DJZBItemVO
     * @param key String
     * @exception java.sql.SQLException 异常说明。
     * @throws DAOException
     */
    public DJZBItemVO[] getDJZBItemVOUniversalArray( String strWhereClause) throws  DAOException {
         DJZBItemVO[] dJZBItems = null;
        Vector v = this.getDJZBItemVOUniversalVector( strWhereClause);
        if (v.size() > 0) {
            dJZBItems = new DJZBItemVO[v.size()];
            v.copyInto(dJZBItems);
        }
        return dJZBItems;
    }
    /**
     * 通过主键查找一个VO对象。
     *
     * 创建日期：(2000-10-9)
     * @return nc.vo.ep.dj.DJZBItemVO
     * @param key String
     * @exception java.sql.SQLException 异常说明。
     * @throws DAOException
     */
    @SuppressWarnings("unchecked")
	private Vector<DJZBItemVO> getDJZBItemVOUniversalVector(String strWhereClause) throws  DAOException {
        List<DJZBItemVO> ret=(List<DJZBItemVO>)pubdao.queryVOsByWhereClause(DJZBItemVO.class,fbmeta,null,strWhereClause);
     VOCompress.objectReference(ret);
        return new Vector<DJZBItemVO>(ret);
    }
    public void update_SS_array(DJZBVO[] vos)throws SQLException, BusinessException, SystemException, Exception
    {
    	for(int i=0;i<vos.length;i++)
    	{
    		convertDJZBVO(vos[i]);
    		DJZBItemVO[] items = (DJZBItemVO[]) vos[i].getChildrenVO();
    		updateItem_SS(items);
    		DJZBHeaderVO head=(DJZBHeaderVO) vos[i].getParentVO();
//    		if(head.getZdr()!=null && head.getZdrq()!=null && head.getDjzt().intValue()==5)
    		updateHeader_SS(head);
    	}
    }
    /**
     *
     * 此处插入方法描述。
     * 创建日期：(2003-2-12 10:02:17)
     * @return boolean
     * @throws SQLException
     * @throws DbException
     * @throws SQLException
     */
    public String hasBanked(String billpk) throws DAOException, SQLException  {
        String sql = "select zb.djbh,fb.tbbh from arap_djfb fb inner join arap_djzb zb on fb.vouchid=zb.vouchid where  fb_oid=? and fb.dr=0";

        SQLParameter parm=new SQLParameter();
        parm.addParam(billpk);
        PersistenceManager pm=null;
        try {
            pm=PersistenceManager.getInstance(InvocationInfoProxy.getInstance().getUserDataSource());
             return (String)pm.getJdbcSession().executeQuery(sql,parm,new  ResultSetProcessor(){

                /**
				 *
				 */
				private static final long serialVersionUID = -3563585813077052610L;

				public Object handleResultSet(ResultSet rs) throws SQLException {
                    //
                    if (rs.next()) {
                        String billno = rs.getString(1);
                        String zyx = rs.getString(2);
                        if (zyx == null || zyx.length() == 0)
                            return null;
                        else
                            return billno;
                    }
                    return null;
                }

             });

        }catch(Exception e)
        {
            throw new DAOException(e);
        }finally {
           pm.release();
        }
    }
    private String getds()
    {
    	return InvocationInfoProxy.getInstance().getUserDataSource();
    }


    /**
     * 通过主键查找一个VO对象。
     *
     * 创建日期：(2000-10-9)
     * @return nc.vo.ep.dj.DJZBHeaderVO
     * @param key String
     * @exception java.sql.SQLException 异常说明。
     注:isHz=true 坏账查询专用,isHe=false单据查询则hzybye,hzbbye,hzfbye用0代替没有实际意义
     * @throws DAOException
     * @throws DbException
     */
    public DJZBHeaderVO[] queryHead_hz(String key,boolean isHz) throws  Exception {


    	String sql="";

    	if(isHz)//坏账发生收回查询,每一张单据表体集合 只能是表体每一行的原币余额大于0
    	{
    		sql = "select max(arap_djzb.vouchid) , max(arap_djzb.prepay), max(arap_djzb.dwbm), max(arap_djzb.djbh), max(arap_djzb.djlxbm), max(arap_djzb.djrq), max(arap_djzb.pzglh), max(arap_djzb.qcbz), max(arap_djzb.lybz), max(arap_djzb.djzt), max(arap_djzb.lrr), max(arap_djzb.shr), max(arap_djzb.shrq), max(arap_djzb.zdr), max(arap_djzb.zdrq), max(arap_djzb.dzrq), max(arap_djzb.xslxbm), max(arap_djzb.fktjbm), max(arap_djzb.zyx1), max(arap_djzb.zyx2), max(arap_djzb.zyx3), max(arap_djzb.zyx4), max(arap_djzb.zyx5), max(arap_djzb.zyx6), max(arap_djzb.zyx7), max(arap_djzb.zyx8),max(arap_djzb.zyx9),max(arap_djzb.zyx10),max(arap_djzb.zyx11),max(arap_djzb.zyx12),max(arap_djzb.zyx13),max(arap_djzb.zyx14),max(arap_djzb.zyx15),max(arap_djzb.zyx16), max(arap_djzb.zyx17), max(arap_djzb.zyx18), max(arap_djzb.zyx19), max(arap_djzb.zyx20), max(arap_djzb.ddhbbm), max(arap_djzb.djkjnd), max(arap_djzb.djkjqj), max(arap_djzb.shkjnd), max(arap_djzb.shkjqj), max(arap_djzb.hzbz), max(arap_djzb.pj_oid), max(arap_djzb.sfkr), max(arap_djzb.kmbm), max(arap_djzb.pj_num), max(arap_djzb.pj_jsfs), max(arap_djzb.qrr), max(arap_djzb.yhqrr), max(arap_djzb.scomment),max(arap_djzb.djdl),max(arap_djzb.fj), max(arap_djzb.ybje),max(arap_djzb.fbje),max(arap_djzb.bbje) ,max(arap_djzb.vouchid),sum(case arap_djfb.dr when 0 then   arap_djfb.ybye  else 0 end),sum(case arap_djfb.dr when 0 then   arap_djfb.fbye  else 0 end),sum(case arap_djfb.dr when 0 then   arap_djfb.bbye  else 0 end),max(arap_djzb.djlxbm), max(arap_djzb.ts) "
    		+" from ARAP_DJZB"
    		+" left outer join arap_djfb on arap_djfb.vouchid=arap_djzb.vouchid"
    		+" "+key
    		+" and arap_djzb.dr=0 and hzbz = '-1'"
    		+" and (arap_djfb.pausetransact='N' or arap_djfb.pausetransact is null)  and arap_djzb.zgyf=0 "

    		+" group by arap_djzb.vouchid";
    	}
    	else//单据查询
    	{
    			sql = "select arap_djzb.vouchid, arap_djzb.prepay, arap_djzb.dwbm, arap_djzb.djbh, arap_djzb.djlxbm, arap_djzb.djrq, arap_djzb.pzglh,arap_djzb.qcbz, arap_djzb.lybz, arap_djzb.djzt, arap_djzb.lrr, arap_djzb.shr, arap_djzb.shrq, arap_djzb.zdr, arap_djzb.zdrq, arap_djzb.dzrq, arap_djzb.xslxbm, arap_djzb.fktjbm, arap_djzb.zyx1, arap_djzb.zyx2, arap_djzb.zyx3, arap_djzb.zyx4, arap_djzb.zyx5, arap_djzb.zyx6, arap_djzb.zyx7, arap_djzb.zyx8,arap_djzb.zyx9,arap_djzb.zyx10,arap_djzb.zyx11,arap_djzb.zyx12,arap_djzb.zyx13,arap_djzb.zyx14,arap_djzb.zyx15,arap_djzb.zyx16, arap_djzb.zyx17, arap_djzb.zyx18, arap_djzb.zyx19, arap_djzb.zyx20, arap_djzb.ddhbbm, arap_djzb.djkjnd, arap_djzb.djkjqj, arap_djzb.shkjnd, arap_djzb.shkjqj, arap_djzb.hzbz, arap_djzb.pj_oid, arap_djzb.sfkr, arap_djzb.kmbm, arap_djzb.pj_num, arap_djzb.pj_jsfs,arap_djzb.qrr, arap_djzb.yhqrr, arap_djzb.scomment,arap_djzb.djdl,arap_djzb.fj, arap_djzb.ybje,arap_djzb.fbje,arap_djzb.bbje ,arap_djzb.vouchid,0,0,0,arap_djzb.djlxbm "
    			+" from ARAP_DJZB"
    		+" left outer join arap_djfb on arap_djfb.vouchid=arap_djzb.vouchid"
    		+" "+key
    		+" and arap_djzb.dr=0"
    		;
    	}

    	DJZBHeaderVO dJZBHeader = null;
    	Connection con = null;
    	PreparedStatement stmt = null;
    	DJZBHeaderVO[] djzbheader = null ;
        PersistenceManager pm=null;
        try {
            pm=PersistenceManager.getInstance(InvocationInfoProxy.getInstance().getUserDataSource());
    		con = pm.getJdbcSession().getConnection();
    		stmt = con.prepareStatement(sql);
    		ResultSet rs = stmt.executeQuery();
    		//
    		Vector<DJZBHeaderVO> vHead = new Vector<DJZBHeaderVO>();
    		while (rs.next()) {//因为有函数sum和分支，故必须用序号
    			dJZBHeader = new DJZBHeaderVO(key);
    			// djmboid :
    			 rs.getString(1);
//    			dJZBHeader.setDjmboid(djmboid == null ? null : djmboid.trim());
    			// prepay :
    			String prepay = rs.getString(2);
    			dJZBHeader.setPrepay(prepay == null ? null : new UFBoolean(prepay.trim()));
    			// dwbm :
    			String dwbm = rs.getString(3);
    			dJZBHeader.setDwbm(dwbm == null ? null : dwbm.trim());
    			// djbh :
    			String djbh = rs.getString(4);
    			dJZBHeader.setDjbh(djbh == null ? null : djbh.trim());
    			// ywbm :
    			String ywbm = rs.getString(5);
    			dJZBHeader.setYwbm(ywbm == null ? null : ywbm.trim());
    			// djrq :
    			String djrq = rs.getString(6);
    			dJZBHeader.setDjrq(djrq == null ? null : new UFDate(djrq.trim()));
    			// pzglh :
    			Integer pzglh = (Integer)rs.getObject(7);
    			dJZBHeader.setPzglh(pzglh == null ? null : pzglh);
    			// qcbz :
    			String qcbz = rs.getString(8);
    			dJZBHeader.setQcbz(qcbz == null ? null : new UFBoolean(qcbz.trim()));
    			// lybz :
    			Integer lybz = (Integer)rs.getObject(9);
    			dJZBHeader.setLybz(lybz == null ? null : lybz);
    			// djzt :
    			Integer djzt = (Integer)rs.getObject(10);
    			dJZBHeader.setDjzt(djzt == null ? null : djzt);
    			// lrr :
    			String lrr = rs.getString(11);
    			dJZBHeader.setLrr(lrr == null ? null : lrr.trim());
    			// shr :
    			String shr = rs.getString(12);
    			dJZBHeader.setShr(shr == null ? null : shr.trim());
    			// shrq :
    			String shrq = rs.getString(13);
    			dJZBHeader.setShrq(shrq == null ? null : new UFDate(shrq.trim()));
    			// zdr :
    			String zdr = rs.getString(14);
    			dJZBHeader.setZdr(zdr == null ? null : zdr.trim());
    			// zdrq :
    			String zdrq = rs.getString(15);
    			dJZBHeader.setZdrq(zdrq == null ? null : new UFDate(zdrq.trim()));
    			// dzrq :
    			String dzrq = rs.getString(16);
    			dJZBHeader.setDzrq(dzrq == null ? null : new UFDate(dzrq.trim()));
    			// xslxbm :
    			String xslxbm = rs.getString(17);
    			dJZBHeader.setXslxbm(xslxbm == null ? null : xslxbm.trim());
    			// fktjbm :
    			String fktjbm = rs.getString(18);
    			dJZBHeader.setFktjbm(fktjbm == null ? null : fktjbm.trim());
    			// zyx1 :
    			String zyx1 = rs.getString(19);
    			dJZBHeader.setZyx1(zyx1 == null ? null : zyx1.trim());
    			// zyx2 :
    			String zyx2 = rs.getString(20);
    			dJZBHeader.setZyx2(zyx2 == null ? null : zyx2.trim());
    			// zyx3 :
    			String zyx3 = rs.getString(21);
    			dJZBHeader.setZyx3(zyx3 == null ? null : zyx3.trim());
    			// zyx4 :
    			String zyx4 = rs.getString(22);
    			dJZBHeader.setZyx4(zyx4 == null ? null : zyx4.trim());
    			// zyx5 :
    			String zyx5 = rs.getString(23);
    			dJZBHeader.setZyx5(zyx5 == null ? null : zyx5.trim());
    			// zyx6 :
    			String zyx6 = rs.getString(24);
    			dJZBHeader.setZyx6(zyx6 == null ? null : zyx6.trim());
    			// zyx7 :
    			String zyx7 = rs.getString(25);
    			dJZBHeader.setZyx7(zyx7 == null ? null : zyx7.trim());
    			// zyx8 :
    			String zyx8 = rs.getString(26);
    			dJZBHeader.setZyx8(zyx8 == null ? null : zyx8.trim());
    			// zyx9 :
    			String zyx9 = rs.getString(27);
    			dJZBHeader.setZyx9(zyx9 == null ? null : zyx9.trim());
    			// zyx10 :
    			String zyx10 = rs.getString(28);
    			dJZBHeader.setZyx10(zyx10 == null ? null : zyx10.trim());
    			// zyx11 :
    			String zyx11 = rs.getString(29);
    			dJZBHeader.setZyx11(zyx11 == null ? null : zyx11.trim());
    			// zyx12 :
    			String zyx12 = rs.getString(30);
    			dJZBHeader.setZyx12(zyx12 == null ? null : zyx12.trim());
    			// zyx13 :
    			String zyx13 = rs.getString(31);
    			dJZBHeader.setZyx13(zyx13 == null ? null : zyx13.trim());
    			// zyx14 :
    			String zyx14 = rs.getString(32);
    			dJZBHeader.setZyx14(zyx14 == null ? null : zyx14.trim());
    			// zyx15 :
    			String zyx15 = rs.getString(33);
    			dJZBHeader.setZyx15(zyx15 == null ? null : zyx15.trim());
    			// zyx16 :
    			String zyx16 = rs.getString(34);
    			dJZBHeader.setZyx16(zyx16 == null ? null : zyx16.trim());
    			// zyx17 :
    			String zyx17 = rs.getString(35);
    			dJZBHeader.setZyx17(zyx17 == null ? null : zyx17.trim());
    			// zyx18 :
    			String zyx18 = rs.getString(36);
    			dJZBHeader.setZyx18(zyx18 == null ? null : zyx18.trim());
    			// zyx19 :
    			String zyx19 = rs.getString(37);
    			dJZBHeader.setZyx19(zyx19 == null ? null : zyx19.trim());
    			// zyx20 :
    			String zyx20 = rs.getString(38);
    			dJZBHeader.setZyx20(zyx20 == null ? null : zyx20.trim());
    			// ddhbbm :
    			String ddhbbm = rs.getString(39);
    			dJZBHeader.setDdhbbm(ddhbbm == null ? null : ddhbbm.trim());
    			// djkjnd :
    			String djkjnd = rs.getString(40);
    			dJZBHeader.setDjkjnd(djkjnd == null ? null : djkjnd.trim());
    			// djkjqj :
    			String djkjqj = rs.getString(41);
    			dJZBHeader.setDjkjqj(djkjqj == null ? null : djkjqj.trim());
    			// shkjnd :
    			String shkjnd = rs.getString(42);
    			dJZBHeader.setShkjnd(shkjnd == null ? null : shkjnd.trim());
    			// shkjqj :
    			String shkjqj = rs.getString(43);
    			dJZBHeader.setShkjqj(shkjqj == null ? null : shkjqj.trim());
    			// hzbz :
    			String hzbz = rs.getString(44);
    			dJZBHeader.setHzbz(hzbz == null ? DJZBVOConsts.BADDEBTS_DEFAULT : hzbz.trim());
    			// pj_oid :
    			String pj_oid = rs.getString(45);
    			dJZBHeader.setPj_oid(pj_oid == null ? null : pj_oid.trim());
    			// sfkr :
    			String sfkr = rs.getString(46);
    			dJZBHeader.setSfkr(sfkr == null ? null : sfkr.trim());
    			// kmbm :
    			String kmbm = rs.getString(47);
    			dJZBHeader.setKmbm(kmbm == null ? null : kmbm.trim());
    			// pj_num :
    			String pj_num = rs.getString(48);
    			dJZBHeader.setPj_num(pj_num == null ? null : pj_num.trim());
    			// pj_jsfs :
    			String pj_jsfs = rs.getString(49);
    			dJZBHeader.setPj_jsfs(pj_jsfs == null ? null : pj_jsfs.trim());
    			// qrr :
    			String qrr = rs.getString(50);
    			dJZBHeader.setQrr(qrr == null ? null : qrr.trim());
    			// yhqrr :
    			String yhqrr = rs.getString(51);
    			dJZBHeader.setYhqrr(yhqrr == null ? null : yhqrr.trim());
    			// comment :
    			String scomment = rs.getString(52);
    			dJZBHeader.setScomment(scomment == null ? null : scomment.trim());
    			// djdl :
    			String djdl = rs.getString(53);
    			dJZBHeader.setDjdl(djdl == null ? null : djdl.trim());
    			// fj :
    			Integer fj = (Integer)rs.getObject(54);
    			dJZBHeader.setFj(fj == null ? null : fj);
    			// ybje :
    			BigDecimal ybje = (BigDecimal)rs.getBigDecimal(55);
    			dJZBHeader.setYbje(ybje == null ? null : new UFDouble(ybje));
    			// fbje :
    			BigDecimal fbje = (BigDecimal)rs.getBigDecimal(56);
    			dJZBHeader.setFbje(fbje == null ? null : new UFDouble(fbje));
    			// bbje :
    			BigDecimal bbje = (BigDecimal)rs.getBigDecimal(57);
    			dJZBHeader.setBbje(bbje == null ? null : new UFDouble(bbje));
    			//vouchid
    			String vouchid = rs.getString(58);
    			dJZBHeader.setVouchid(vouchid == null ? null : vouchid.trim());


    			//坏账原币余额
    			BigDecimal djzbhzybye =(BigDecimal)rs.getBigDecimal(59);
    			dJZBHeader.setHzybye(djzbhzybye == null ? null : new UFDouble(djzbhzybye));

    			//坏账辅币余额
    			BigDecimal djzbhzfbye =(BigDecimal)rs.getBigDecimal(60);
    			dJZBHeader.setHzfbye(djzbhzfbye == null ? null : new UFDouble(djzbhzfbye));

    			//坏账本币余额
    			BigDecimal djzbhzbbye =(BigDecimal)rs.getBigDecimal(61);
    			dJZBHeader.setHzbbye(djzbhzbbye == null ? null : new UFDouble(djzbhzbbye));

    			//djlxbm

    			String djlxbm = rs.getString(62);
    			dJZBHeader.setDjlxbm(djlxbm == null ? null : djlxbm.trim());
    			String ts = rs.getString(63);
    			dJZBHeader.setTs(ts == null ? null : new UFDateTime(ts.trim()));

    			vHead.addElement(dJZBHeader);
    		}
    		djzbheader = new DJZBHeaderVO[vHead.size()];
    		vHead.copyInto(djzbheader);

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



    	return djzbheader;
    }
    /**
     * author:wangqiang
     * creation time:2004-12-15
     * function:method for common use,providing the query of arap_djzb table
     *          note that all the neccesary column of arap_djzb table is included in the select sql clause of this mehtod
     *          therefore this method is basically able to meet the requirement of any query for the arap_djzb
     * @param strSqlFromClause:part of the sql statement starting from "from table_name..."
     * @param con:database connection
     * @param bWithGL:whether GL column is needed
     * @param bGLChalked:凭证是否已经记帐
     * @return
     * @throws SQLException
     * @throws DAOException
     * @throws SQLException
     */
    @SuppressWarnings("unchecked")
    private Vector<DJZBHeaderVO> getDJZBHeaderVOsUniversalVector(Integer initPos,Integer count,String strSqlFromClause, boolean bWithGL,Integer voucherFlag)throws   DAOException, SQLException{
    	String strSQL = getSelectSQL(zbmeta);
        strSQL+=strSqlFromClause;
        Logger.error("before-------"+System.currentTimeMillis());
        List ret;
        if(bWithGL||null!=voucherFlag)
        	ret=(List<DJZBHeaderVO>)pubdao.queryVOsBySql(DJZBHeaderVO.class,zbmeta,strSQL,initPos,count,new DJZBVOGLRSChecker(bWithGL,voucherFlag));
        else
        	ret=(List<DJZBHeaderVO>)pubdao.queryVOsBySql(DJZBHeaderVO.class,zbmeta,strSQL,initPos,count);
    	Vector<DJZBHeaderVO> vHeadVOs = new Vector<DJZBHeaderVO>(ret);

     	return vHeadVOs;

    }
    /**
     * @fuction 通过主键查找一个VO对象(针对事项审批单、费用结算单)
     * @author maji
     * @since V6.0
     * @return nc.vo.ep.dj.DJZBHeaderVO
     * @param String ,operator操作员
     * @exception java.sql.Exception
     *    */
    @SuppressWarnings("unchecked")
    public Vector queryDjLb_SS(Integer initPos,Integer count, DjCondVO djcond) throws  Exception  {
        nc.vo.arap.pub.QryCondArrayVO[] norCondVos = djcond.m_NorCondVos;
        StringBuilder sql=new StringBuilder();
        sql.append(getSelectSQL(ssmeta));
        sql.append(" from arap_item zb inner join arap_item_b fb on zb.vouchid=fb.vouchid");
        sql.append(new nc.bs.arap.pub.PubMethods().getBillQuerySubSql_Dj( norCondVos, djcond.defWhereSQL, djcond.pk_corp, djcond.operator));
    	sql.append(" order by zb.djrq,zb.djlxbm,  zb.djbh ");
    	List ret=(List)pubdao.queryVOsBySql(DJZBHeaderVO.class,ssmeta,sql.toString(),initPos,count);
    	Vector<DJZBHeaderVO> vHead = new Vector<DJZBHeaderVO>(ret);

    	return vHead;
    }

    private String getSelectSQL(IArapMappingMeta meta)
    {
        StringBuffer buf=new StringBuffer("SELECT DISTINCT zb.").append("vouchid");
        for(int i=0,size=meta.getColumns().length;i<size;i++)
            if(!"vouchid".equalsIgnoreCase(meta.getColumns()[i]))
                buf.append(", zb.").append(meta.getColumns()[i]);
        return buf.toString();
    }

    /**
     * 根据“订单行id”、“出库单行id”、“发票行id”列 查询单据
     * @param ddhid
     * @return
     * @throws SQLException
     * @throws DbException
     * @throws DAOException
     */
    @SuppressWarnings("unchecked")
	public Map<String, DJZBVO[]> getDJByXXID(String key,String[] values,int ly) throws  SQLException, DbException, DAOException {
    	PubDAO dao=new PubDAO();
      int lybz=0;
      switch(ly){
        case 0: lybz=3;break;
        case 1: lybz=4;break;
        case 2: lybz=16;break;
        case 3: lybz=4;break;//add by ouyangzhb 2011-05-09
      }

      String inStr = SqlUtils.getInStr("fb."+key, values);
      //add by ouyangzhb 2011-05-07 查询得到相对应的暂估应付单id begin
      String inStr1 =  SqlUtils.getInStr(null, values);
      String sql1="select distinct pk_cubasdoc,cinvbasid from ic_general_b where cgeneralbid "+ inStr1+" and dr=0 ";
      String sql=null;
      if(ly==1){
    	  sql =" select fb.fb_oid,fb."+key+" from arap_djfb fb inner join arap_djzb zb on fb.vouchid=zb.vouchid where "+inStr+" and fb.dr=0 and zb.zgyf=1 and zb.lybz="+lybz +" and (fb.hbbm, fb.cinventoryid ) in  ("+ sql1 + ") ";
      }else{
    	  sql =" select fb.fb_oid,fb."+key+" from arap_djfb fb inner join arap_djzb zb on fb.vouchid=zb.vouchid where "+inStr+" and fb.dr=0 and zb.zgyf=1 and zb.lybz="+lybz ;
      }
      //add by ouyangzhb 2011-05-07 查询得到相对应的暂估应付单 end
      PersistenceManager pm=null;
      Map<String,List<String>> rets=null;
     
      
      try{
          pm=PersistenceManager.getInstance(getds());
          rets=(Map<String,List<String>>)pm.getJdbcSession().executeQuery(sql,new  ResultSetProcessor()
                  {

            private static final long serialVersionUID = 4314866937739791621L;

            public Object handleResultSet(ResultSet arg0) throws SQLException {
                          //
            	Map<String,List<String>> keys=new HashMap<String, List<String>>();
                          while (null!=arg0&&arg0.next()) {
                        	String fboid = arg0.getString(1);
                            String ckdid = arg0.getString(2);
                            
                            List<String> name=new ArrayList<String>();
                        	if(keys.containsKey(ckdid)){
                        		name = keys.get(ckdid);
                        	}
                        	name.add(fboid);
                        	
                        	keys.put(ckdid, name);
                          }
                          return keys;
                      }
                  });
        } finally {
            pm.release();
        }
       ;
       
      List<String> fboids=new ArrayList<String>();
      for(List<String> fboid:rets.values()){
    	  fboids.addAll(fboid);
      }
      
      String inStr2 = SqlUtils.getInStr(key, values);

      List ret=(List)dao.queryVOsByWhereClause(DJZBItemVO.class,fbmeta, null," dr=0 and ("+SqlUtils.getInStr("fb_oid", fboids.toArray(new String[]{}))+" or "+ SqlUtils.getInStr("ddhh", fboids.toArray(new String[]{}))+") and "+inStr2)  ;

      List<String> vouchids=new ArrayList<String>();

	  Map<String,List<DJZBItemVO>> map=new HashMap<String,List<DJZBItemVO>>();
      String vd;
	  for(int i=0;i<ret.size();i++){
			vd=((DJZBItemVO)ret.get(i)).getVouchid();
			if(!vouchids.contains(vd)){
				vouchids.add(vd);
				map.put(vd,new ArrayList<DJZBItemVO>());
			}
			map.get(vd).add((DJZBItemVO)ret.get(i));
	  }
	  Vector<DJZBHeaderVO> heads=this.findHeaderByPrimaryKeys(vouchids.toArray(new String[]{}));
	  Map<String, DJZBVO[]> djzbvos=new HashMap<String, DJZBVO[]>();
	  List<DJZBVO> zbvoList=new ArrayList<DJZBVO>();
	  for(DJZBHeaderVO head:heads){
			DJZBVO djzbvo=new DJZBVO();
			if(head.getZgyf()==null || head.getZgyf().intValue()==0){
				continue;
			}
			djzbvo.setParentVO(head);
			djzbvo.setChildrenVO(map.get(head.getVouchid()).toArray(new DJZBItemVO[]{}));
			zbvoList.add(djzbvo);
	  }
	  
	  for(String retkey:rets.keySet()){
    	   djzbvos.put(retkey, getDjzbVOs(key,retkey,rets.get(retkey),zbvoList));
      }
	  
	  return djzbvos;
    }
    private DJZBVO[] getDjzbVOs(String retkey, String retValue, List<String> fbkeys, List<DJZBVO> zbvoList) {
    	List<DJZBVO> svo=new ArrayList<DJZBVO>();
    	for(DJZBVO vo:zbvoList){
    		DJZBItemVO[] items = vo.items;
    		List<DJZBItemVO> listItems=new ArrayList<DJZBItemVO>();
    		for(DJZBItemVO item:items){
    			if(fbkeys.contains(item.getFb_oid()) || (item.getDdhh()!=null && fbkeys.contains(item.getDdhh()))){
    				if(item.getAttributeValue(retkey)!=null && retValue.equals(item.getAttributeValue(retkey))){
    					listItems.add(item);
    				}
    			}
    		}
    		if(listItems.size()!=0){
    			DJZBVO clone = (DJZBVO) vo.clone();
    			clone.setChildrenVO(listItems.toArray(new DJZBItemVO[]{}));
				svo.add(clone);
    		}
    	}
		return svo.toArray(new DJZBVO[]{});
	}


	public void updateItemByEncode(String pk,String encode) throws SQLException, DbException{

        String sql="update ARAP_DJFB set encode = '"+encode+"' where fb_oid='"+pk+"'" ;
        PersistenceManager pm=null;

    	try {
            pm=PersistenceManager.getInstance(InvocationInfoProxy.getInstance().getUserDataSource());
            pm.getJdbcSession().executeUpdate(sql);
    	 } finally {
            pm.release();
         }

    }
    public void updatePayMan(DJWszzVO vo) throws BusinessException{
    	String sql  ;
    	PersistenceManager pm=null;
    	try {
            pm=PersistenceManager.getInstance(InvocationInfoProxy.getInstance().getUserDataSource());
            for(int i=0,size=vo.getZbpks().length;i<size;i++){
            	 sql="update ARAP_DJZB set payman = '"+vo.getPayman()
            	                          +"' , isnetready = '"+vo.getIsnetReady().toString() +"' ,paydate='"+vo.getPaydate();
            	 if(vo.getSettleNum()!=null)
            		 sql+="', settlenum='"+vo.getSettleNum();

            	 sql+="' where vouchid='"+vo.getZbpks()[i]+"'" ;
            	 pm.getJdbcSession().executeUpdate(sql);
            }
            for(int i=0,size=vo.getFbpks().length;i<size;i++){
            	sql="update ARAP_DJFB set payman = '"+vo.getPayman()
            	                                   	                +"',paydate='"+vo.getPaydate()+"' where fb_oid='"+vo.getFbpks()[i]+"'" ;
            	pm.getJdbcSession().addBatch(sql);
    		}
            pm.getJdbcSession().executeBatch();
    	 } catch(Exception e){
    		 throw ExceptionHandler.handleException(this.getClass(), e);
    	 } finally {
             pm.release();
    	 }
    }
    public void updateXtFlag_affirm(List<String> fbpks,Integer flag) throws BusinessException{
    	if(null==fbpks||fbpks.size()==0)
    		return ;
//    	StringBuffer where=new StringBuffer();
//    	for(String fbpk:fbpks){
//    		where.append("'").append(fbpk).append("',");
//    	}
    	flag=flag==null?new Integer(0):flag;
     	PersistenceManager pm=null;
    	try {
    	   	String sql="update ARAP_DJFB set djxtflag = "+flag.intValue()+" where  "+SqlUtils.getInStr("fb_oid", fbpks.toArray(new String[]{}))
    	   	+" and dr=0 ";

            pm=PersistenceManager.getInstance(InvocationInfoProxy.getInstance().getUserDataSource());
            int count = pm.getJdbcSession().executeUpdate(sql);
            if(count < 1){
            	throw new BusinessException(nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("2006030102","UPP2006030102-000503")/*@res "并发异常,数据已经被更新"*/);
            }
    	 } catch(Exception e){
    		 throw ExceptionHandler.handleException(this.getClass(), e);
    	 } finally {
             pm.release();
    	 }
    }

    public void updateXtFlag(List<String> fbpks,Integer flag) throws BusinessException{
    	if(null==fbpks||fbpks.size()==0)
    		return ;
//    	StringBuffer where=new StringBuffer();
//    	for(String fbpk:fbpks){
//    		where.append("'").append(fbpk).append("',");
//    	}
    	flag=flag==null?new Integer(0):flag;
     	PersistenceManager pm=null;
    	try {
    	   	String sql="update ARAP_DJFB set djxtflag = "+flag.intValue()+" where  "+SqlUtils.getInStr("fb_oid", fbpks.toArray(new String[]{}))
    	   	+" and dr=0 ";

            pm=PersistenceManager.getInstance(InvocationInfoProxy.getInstance().getUserDataSource());
            pm.getJdbcSession().executeUpdate(sql);
    	 } catch(Exception e){
    		 throw ExceptionHandler.handleException(this.getClass(), e);
    	 } finally {
             pm.release();
    	 }
    }
    //lumzh 2012-08-20运输单生成的暂估单应付单,删除时应将源头的运输单的是否已暂估状态改为N。
    public void updateDmFlag(List<String> fbpks,int flag) throws BusinessException{
    	if(null==fbpks||fbpks.size()==0)
    		return ;
    	if(flag!=1)return ;
//    	StringBuffer where=new StringBuffer();
//    	for(String fbpk:fbpks){
//    		where.append("'").append(fbpk).append("',");
//    	}
     	PersistenceManager pm=null;
    	try {
//    	   	String sql="update ARAP_DJFB set djxtflag = "+flag.intValue()+" where  "+SqlUtils.getInStr("fb_oid", fbpks.toArray(new String[]{}))
//    	   	+" and dr=0 ";
    	    String sql="update dm_delivbill set isestimate='N' where "+SqlUtils.getInStr("cdelivbill_hid", fbpks.toArray(new String[]{}))+" and dr=0";
            pm=PersistenceManager.getInstance(InvocationInfoProxy.getInstance().getUserDataSource());
            pm.getJdbcSession().executeUpdate(sql);
    	 } catch(Exception e){
    		 throw ExceptionHandler.handleException(this.getClass(), e);
    	 } finally {
             pm.release();
    	 }
    }
    /**
     *
     * @param headdatas
     * @param bodydatas
     * @param jszxzf  支付标志 2为网上支付，1为结算中心支付，3为手工支付
     * @throws BusinessException
     */
    public void updateHandPayInfo(String[] headdatas ,int jszxzf) throws BusinessException{

    	DJZBVO[] djvos=null;
		try {
			djvos = new DJZBVO[]{this.findByPrimaryKey(headdatas[2])};
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			 Log.getInstance(this.getClass()).error(e.getMessage(), e );
    		 throw  new BusinessException (e.getMessage(),e);
		}
		int zzzt=DJZBVOConsts.m_intDJZzzt_Default;
		int payflag=DJZBVOConsts.BODY_PAY_BLANK;
		if(DJZBVOConsts.m_intJSZXZF_HAND==jszxzf){
			zzzt=DJZBVOConsts.m_intDJZzzt_Succeed;
			if (djvos.length > 0 && djvos[0].header.getDjdl().equals("sk")) {
				zzzt=DJZBVOConsts.FTS_RECEIVER_SUCCESS;
				payflag = DJZBVOConsts.BODY_RECEIVE_OK;
			} else {
				payflag=DJZBVOConsts.BODY_PAY_OK;
			}


		}
		 Map<Integer,BusiTransVO[]> busiMap=new HashMap<Integer,BusiTransVO[]>();
    	  InvokeBusitrans ib=new InvokeBusitrans();
    	  nc.bs.arap.global.ArapExtInfRunBO extbo =
	            new nc.bs.arap.global.ArapExtInfRunBO();
    	  for(DJZBVO dj:djvos){
    		  if(busiMap.containsKey(((DJZBHeaderVO)dj.getParentVO()).getPzglh()))
	 	        	busiMap.put(((DJZBHeaderVO)dj.getParentVO()).getPzglh(), extbo.initBusiTrans("pay",((DJZBHeaderVO)dj.getParentVO()).getPzglh()));
	        nc.vo.arap.global.BusiTransVO[] busitransvos =  busiMap.get(((DJZBHeaderVO)dj.getParentVO()).getPzglh());
	        ib.beforePayInf(busitransvos, dj,IArapPayInterface.HandPay);
    	  }
    	String paydate = headdatas[1]==null?headdatas[1]:"'"+headdatas[1]+"'";
    	String sql="update ARAP_DJZB set payman = '"+headdatas[0]
    	                +"',paydate="+paydate+" ,jszxzf="+jszxzf+",zzzt="+zzzt+"  where vouchid='"+headdatas[2]+"'" ;
    	PersistenceManager pm=null;
    	try {
            pm=PersistenceManager.getInstance(InvocationInfoProxy.getInstance().getUserDataSource());
            pm.getJdbcSession().executeUpdate(sql);
            for(DJZBVO dj:djvos){
            for(int i=0,size=dj.getChildrenVO().length;i<size;i++){
            	DJZBItemVO item=(DJZBItemVO)dj.getChildrenVO()[i];
            	sql="update ARAP_DJFB set payman = '"+headdatas[0]
            	                                   	                +"',paydate="+paydate+" ,payflag="+payflag+"  where fb_oid='"+item.getFb_oid() +"'" ;
            	pm.getJdbcSession().addBatch(sql);
    		}
            pm.getJdbcSession().executeBatch();
            }
    	 } catch(Exception e){
    		 throw ExceptionHandler.handleException(this.getClass(), e);
    	 } finally {
             pm.release();
    	 }
    	 for(DJZBVO dj:djvos){
    		 nc.vo.arap.global.BusiTransVO[] busitransvos =  busiMap.get(((DJZBHeaderVO)dj.getParentVO()).getPzglh());
    	  ib.afterPayInf( busitransvos, dj,IArapPayInterface.HandPay);
    	 }
    }
//    public String[] getDjPksBySSPk(String sspk) throws DbException {
//    	String sql="select fb.vouchid from  arap_djfb fb inner join arap_item ss on fb.item_bill_pk=ss.vouchid " +
//    			" where fb.dr=0 and ss.vouchid='"+sspk+"' group by fb.vouchid";
//    	PersistenceManager pm=null;
//        try {
//            pm=PersistenceManager.getInstance(getds());
//            return (String[])pm.getJdbcSession().executeQuery(sql,new ResultSetProcessor(){
//
//                /**
//    			 *
//    			 */
//    			private static final long serialVersionUID = 4408592894824970592L;
//
//    			public Object handleResultSet(ResultSet rs) throws SQLException {
//                    //
//    				List<String> ret=new ArrayList<String>();
//                    while ( rs.next()) {
//        				 ret.add( rs.getString(1));
//        			}
//                    return ret.toArray(new String[]{});
//                }
//            });
//    		//
//
//    	} finally {
//    	 pm.release();
//    	}
//    }
    @SuppressWarnings("unchecked")
	public Map<String,String> getTsByPrimaryKeys(String[] key, String tableName)
	throws SQLException, DbException {
//    String ts=null;
//    StringBuffer sb=new StringBuffer();
//	for(int i=0;i<key.length;i++){
//		if(i==0){
//			sb.append(" '").append(key[i]).append("' ");
//		}else{
//			sb.append(" , '").append(key[i]).append("' ");
//		}
//	}
    String sql = "select vouchid, ts from  " + tableName + "  where "+SqlUtils.getInStr("vouchid", key);
    PersistenceManager pm=null;
    Map<String,String> rets=null;
    try{
        pm=PersistenceManager.getInstance(getds());
        rets=(Map<String,String>)pm.getJdbcSession().executeQuery(sql,new  ResultSetProcessor()
                {
                    /**
					 *
					 */
					private static final long serialVersionUID = 4314866937739791621L;

					public Object handleResultSet(ResultSet arg0) throws SQLException {
                        //
						Map<String,String> ret=new HashMap<String, String>();
                        while (null!=arg0&&arg0.next()) {
                        	ret.put(arg0.getString(1), arg0.getString(2))  ;
                		}
                        return ret;
                    }
                });
    	} finally {
    	    pm.release();
    	}
    	return rets;
}
    public void updateDJSettleNum(String[] djpks,String settlenum)throws BusinessException {
    	if(null==djpks||djpks.length==0)
    		return ;
//    	StringBuffer where=new StringBuffer();
//    	for(int i=0,size=djpks.length;i<size;i++){
//    		if(i==0)
//    			where.append(" '").append(djpks[i]).append("' ");
//    		else
//    			where.append(" , '").append(djpks[i]).append("' ");
//    	}
    	settlenum=settlenum==null?settlenum:"'"+settlenum+"'";
    	String sql;


    	PersistenceManager pm=null;
    	try {
    		sql="update arap_djzb set settlenum = "+ settlenum+"  where  "+SqlUtils.getInStr("vouchid", djpks);
            pm=PersistenceManager.getInstance(InvocationInfoProxy.getInstance().getUserDataSource());
            pm.getJdbcSession().executeUpdate(sql);
    	 } catch(Exception e){
    		 throw ExceptionHandler.handleException(this.getClass(), e);
    	 } finally {
             pm.release();
    	 }
    }
    @SuppressWarnings("unchecked")
	public List<String> getDjzbpksBySeq(String seq) throws DbException {
    	String sql="select distinct zb.vouchid from  arap_djzb zb   where zb.dr=0 and zb.settlenum='"+seq +"'";
		PersistenceManager pm=null;
		try {
		    pm=PersistenceManager.getInstance(getds());
		    return (List<String>) pm.getJdbcSession().executeQuery(sql,new ResultSetProcessor(){
				/**
				 *
				 */
				private static final long serialVersionUID = 1L;

				public Object handleResultSet(ResultSet rs) throws SQLException {
					List<String> ret=new ArrayList<String>();
		            while ( rs.next()) {
						 ret.add( rs.getString(1));
					}

		            return  ret ;
		        }
		    });

		} finally {
		 pm.release();
		}
    }
    public void updateIsNetReady(String[] djpks, UFBoolean isready) throws   DbException, SQLException {
    	if(null==djpks||djpks.length==0) return ;
//    	StringBuffer sb=new StringBuffer("");
//        for(String pk:djpks){
//        	sb.append("'").append(pk).append("'").append(",");
//        }
        String val=isready==null||!isready.booleanValue()?"N":"Y";
        String sql="update arap_djzb set isnetready = '"+val+
        		"' where  " +SqlUtils.getInStr("vouchid", djpks);
        PersistenceManager pm=null;
		try {
		    pm=PersistenceManager.getInstance(getds());
		    pm.getJdbcSession().executeUpdate(sql);
		} finally {
		 pm.release();
		}

    }

    public void updateSourceBillIsReded(String vouchid) throws BusinessException{
    	if(null==vouchid) return ;
    	String sql = "update arap_djzb set isreded = 'N' where vouchid = '"+vouchid+"'";
        PersistenceManager pm=null;
    	try {
            pm=PersistenceManager.getInstance(InvocationInfoProxy.getInstance().getUserDataSource());
            pm.getJdbcSession().executeUpdate(sql);
    	 } catch(Exception e){
    		 throw ExceptionHandler.handleException(this.getClass(), e);
    	 } finally {
             pm.release();
    	 }
    }
    public String[] findDjpksByFboids(String[] fboids) throws BusinessException{

//    	StringBuffer sb=new StringBuffer( where fb_oid in (");
//    	for(String fboid:fboids){
//    		sb.append("'").append(fboid).append("',");
//    	}
    	if( null==fboids||fboids.length==0)
    		return new String[]{};
    	PersistenceManager pm=null;
		try {
			String sql="select distinct vouchid from arap_djfb where dr=0 and "+SqlUtils.getInStr("fb_oid", fboids) ;
		    pm=PersistenceManager.getInstance(getds());
		    return (String[]) pm.getJdbcSession().executeQuery(sql,new ResultSetProcessor(){
				/**
				 *
				 */
				private static final long serialVersionUID = 1L;

				public Object handleResultSet(ResultSet rs) throws SQLException {
					List<String> ret=new ArrayList<String>();
		            while ( rs.next()) {
						 ret.add( rs.getString(1));
					}
		            return  ret.toArray(new String[]{});
		        }
		    });

		} catch(Exception e){
			throw ExceptionHandler.handleException(this.getClass(), e);
		}
		finally {
		 pm.release();
		}
    }
    @SuppressWarnings("unchecked")
    public Map<String,String> getDjfbpksByAttr(String lx,String[] values,String clbh) throws BusinessException{
//    	if("45".equals(lx))
//    		lx="1";
    	String lyattr=null;
    	if(Integer.valueOf(lx)==0)//订单行id
	    {
    		lyattr="ddhid";
	    }
	    else if(Integer.valueOf(lx)==1)//出库单行id
	    {
	    	lyattr="ckdid";
	    }
	    else if(Integer.valueOf(lx)==2)//发票行id
	    {
	    	lyattr="fphid";
	    } 
    	//add by ouyangzhb 2011-05-10 为来源类型为3的设置过滤条件
	    else if(Integer.valueOf(lx)==3)//订单行id
	    {
	    	lyattr="ddhh";//源头单据id
	    }
    	
    	String tempTableName=null;
    	PersistenceManager pm=null;
    	Connection connection = null;
    	try {
    		pm=PersistenceManager.getInstance(InvocationInfoProxy.getInstance().getUserDataSource());
    		String sql= null;
    		
    		sql= " select distinct fb_oid,ddhh from arap_djfb where  "+SqlUtils.getInStr(lyattr, values)+" and dr=0 ";
    		sql+= "and clbh='"+clbh+"'";
    			
    		
//	    	if(values.length<1000){
//	    		StringBuffer sb=new StringBuffer();
//	    		sb.append(lyattr).append(" in ( ");
//	        	for(String ddhh:values){
//	        		sb.append(" '").append(ddhh).append("' , ");
//	        	}
//	    		sql=sb.substring(0,sb.length()-2)+  ") and clbh='"+clbh+"' and dr=0 ";
//	    	}else{
//	    		connection = pm.getJdbcSession().getConnection();
//				tempTableName=createTempTable(connection, lyattr,values);
//				sql="select disctinct fb.fb_oid ,fb.ddhh from arap_djfb inner join "+tempTableName+
//				 " temp on fb."+lyattr+"=temp."+lyattr +" where fb.dr=0 and clbh='"+clbh+"'";
//	    	}
    	return (Map<String,String>) pm.getJdbcSession().executeQuery(sql,new  ResultSetProcessor(){

    		private static final long serialVersionUID = 5657955493144329477L;

    		public Object handleResultSet(ResultSet rs) throws SQLException {
    	    	Map<String,String> retMap=new HashMap<String,String>();
    	        while (rs.next()) {
    	            retMap.put(rs.getString(1), rs.getString(2));
    	        }
    	        return retMap;
    	    }
    	 });

    	}catch(Exception e)
    	{
    		throw ExceptionHandler.handleException(this.getClass(), e);
    	}finally {
    		try{
    			if(null!=tempTableName&&null!=connection){
    				nc.bs.mw.sqltrans.TempTable tmptab = new nc.bs.mw.sqltrans.TempTable();
    				tmptab.dropTempTable(connection, tempTableName);
    			}}catch(Exception e){
    				ExceptionHandler.consume(e);
    			}
    		pm.release();
    	}
    }

    /**
     * 用一个VO对象的属性更新数据库中的值。
     *
     * 创建日期：(2000-10-9)
     * @param dJZBHeader nc.vo.ep.dj.DJZBHeaderVO
     * @exception java.sql.SQLException 异常说明。
     * @throws DAOException
     */
    public void updateBillHeaders(DJZBHeaderVO  dJZBHeader ) throws  DAOException ,BusinessException{
      Arap_djzbVOMeta zbmeta=new Arap_djzbVOMeta();
      pubdao.updateObjectPartly(dJZBHeader,zbmeta,new String []{"fktjbm"} );
     }
    @SuppressWarnings("unchecked")
	public DJZBVO[] findByPrimaryKeysWithPower(String[] keys,String pk_corp,String operator) throws SQLException, BusinessException {

    	if(null != keys && keys.length > 0){
    		Vector<DJZBHeaderVO> headers = findHeaderByPrimaryKeyWithPower(keys,pk_corp,operator);
    		DJZBItemVO[] items = findItemsForHeaders(keys);
    		fillFreeItems(items);
    		return assembleDjzbVo(headers.toArray(new DJZBHeaderVO[]{}), items);

    	}
    	return null;


//    	List<DJZBVO> ret=new ArrayList<DJZBVO>();
//       	//
//    	HashMap<String, String> map = new HashMap<String,String>();
//       	for(int k=0,size=keys.length;k<size;k++){
//       		if(map.get(keys[k])!=null){
//       			continue;
//       		}else{
//       			map.put(keys[k],keys[k]);
//       		}
//       	 DJZBVO vo = new DJZBVO();
//           	DJZBHeaderVO header = findHeaderByPrimaryKeyWithPower(keys[k],pk_corp,operator);
//           	DJZBItemVO[] items = null;
//           	if (header != null) {
//           		items = findItemsForHeader(header.getPrimaryKey());
////        		List<String> ls = new ArrayList<String>();
////        		for(DJZBItemVO vo1 : items){
////        			ls.add(vo1.getFb_oid());
////        		}
////        		HashMap map1 = defdao.findItemvosForHeader(ls.toArray(new String[ls.size()]));
////           		for(int i = 0;i<items.length;i++){
////           			//查询自由项
//////           			     DJFBItemVO[] frees = defdao.findItemsForHeader (items[i].getFb_oid());
//////           			     items[i].items = frees;
////        			items[i].items = (DJFBItemVO[]) map1.get(items[i].getFb_oid());
////           			}
//           		fillFreeItems(items);
//           	}
//           	if(null!=header){
//		           	vo.setParentVO(header);
//		           	vo.setChildrenVO(items);
//		           	ret.add(vo);
//           	}
//       	}
//
//       	return ret.size()==0?null:ret.toArray(new DJZBVO[]{});
       }

    public DJZBHeaderVO findHeaderByPrimaryKeyWithPower(String key,String pk_corp,String operator) throws DAOException, SQLException {
//      nc.bs.arap.global.PubBO pbo = new nc.bs.arap.global.PubBO();
//      String pk_corp = "1001";
//      pk_corp=findpk_corpByPrimaryKey(key);
//      boolean isUsedGL = pbo.glIsUsed(pk_corp, "GL");
      boolean isUsedGL = false;
      String sqlFromClause = " from ARAP_DJZB zb inner join arap_djfb fb on zb.vouchid=fb.vouchid " ;
      try {
          CreatJoinSQLTool pathObj = new CreatJoinSQLTool();
          PowerCtrlVO pwvo = new PowerCtrlVO();
          pwvo.setPk_corp(new String[] { pk_corp });
          pwvo.setUserId(operator);
          pathObj.setIszrdeptidusepower(true);
          pwvo.setTables(PowerCtrlVO.DocTabPks);

          pathObj.setPowerCtrlVO(pwvo);
          pathObj.getPowerHashtable();
          String strJoin = pathObj.getJoinSQL(new String[]{"zb"});
          if (strJoin != null && strJoin.length() > 0) {
          	sqlFromClause += strJoin;
          }
      } catch (Exception e) {
          Log.getInstance(this.getClass()).error(e.getMessage());
      }
      sqlFromClause=sqlFromClause+"where zb.dr=0 and zb.vouchid = '"+key+"'";
      DJZBHeaderVO dJZBHeader = null;
      Vector vHead = this.getDJZBHeaderVOsUniversalVector(sqlFromClause,isUsedGL,null);
          if(vHead.size()>0)
              dJZBHeader = (DJZBHeaderVO)vHead.elementAt(0);
      return dJZBHeader;
  }


	public Vector<DJZBHeaderVO> findHeaderByPrimaryKeyWithPower(String[] keys,String pk_corp,String operator) throws DAOException, SQLException {

      boolean isUsedGL = false;
      String sqlFromClause = " from ARAP_DJZB zb inner join arap_djfb fb on zb.vouchid=fb.vouchid " ;
      try {
          CreatJoinSQLTool pathObj = new CreatJoinSQLTool();
          PowerCtrlVO pwvo = new PowerCtrlVO();
          pwvo.setPk_corp(new String[] { pk_corp });
          pwvo.setUserId(operator);
          pathObj.setIszrdeptidusepower(true);
          pwvo.setTables(PowerCtrlVO.DocTabPks);

          pathObj.setPowerCtrlVO(pwvo);
          pathObj.getPowerHashtable();
          String strJoin = pathObj.getJoinSQL(new String[]{"zb"});
          if (strJoin != null && strJoin.length() > 0) {
          	sqlFromClause += strJoin;
          }
      } catch (Exception e) {
          Log.getInstance(this.getClass()).error(e.getMessage());
      }
      sqlFromClause=sqlFromClause+" where zb.dr=0 and "+SqlUtils.getInStr("zb.vouchid", keys);
      Vector<DJZBHeaderVO> vHead = this.getDJZBHeaderVOsUniversalVector(sqlFromClause,isUsedGL,null);
      return vHead;
  }

 }