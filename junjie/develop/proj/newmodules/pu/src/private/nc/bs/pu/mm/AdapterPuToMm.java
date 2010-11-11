package nc.bs.pu.mm;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

import javax.naming.NamingException;

import nc.bs.dao.BaseDAO;
import nc.bs.pr.pray.PraybillImpl;
import nc.bs.pu.pr.PraybillDMO;
import nc.bs.pub.SystemException;
import nc.bs.scm.pub.ScmPubDMO;
import nc.itf.pu.mm.ISrvPUToMM;
import nc.vo.pr.pray.PraybillVO;
import nc.vo.pu.pr.PrayExecVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDate;
import nc.vo.scm.pu.PurExecVO;
import nc.vo.scm.pub.smart.SmartVO;

/**
 * <p>
 * <b>本类主要完成以下功能：</b>
 * 
 * <ul>
 *  <li>给生产制造的实现统一入口
 *  <li>功能条目2
 *  <li>...
 * </ul> 
 *
 * <p>
 * <b>变更历史（可选）：</b>
 * <p>
 * 	 V51版本增加V3X的支持。
 * <p>
 * <p>
 * @version V51
 * @since V3X
 * @author czp
 * @time 2007-3-15 上午10:17:23
 */
public class AdapterPuToMm implements ISrvPUToMM{

  /**
   * 父类方法重写
   * 
   * @see nc.itf.pu.mm.ISrvPUToMM#discardPraybillArr(java.lang.String[], java.lang.String[], java.lang.String, java.lang.String, java.lang.String)
   */
  public void discardPraybillArray(String[] hIDs, String[] bIDs, String sBilltype, String dDate, String sUserId) throws BusinessException {
    new PraybillImpl().discardPraybillArr(hIDs, bIDs, sBilltype, dDate, sUserId);    
  }

  /**
   * 父类方法重写
   * 
   * @see nc.itf.pu.mm.ISrvPUToMM#doSaveSpecially(nc.vo.pr.pray.PraybillVO)
   */
  public ArrayList doSaveSpecially(PraybillVO VO) throws BusinessException {
    return new nc.bs.pu.pr.PraybillImpl().doSaveSpecially(VO);
  }

  /**
   * 父类方法重写
   * 
   * @see nc.itf.pu.mm.ISrvPUToMM#doSaveSpeciallyBatch(nc.vo.pr.pray.PraybillVO[])
   */
  public ArrayList doSaveSpeciallyBatch(PraybillVO[] VOs) throws BusinessException {
    return new nc.bs.pu.pr.PraybillImpl().doSaveSpeciallyBatch(VOs);
  }

  /**
   * 父类方法重写
   * 
   * @see nc.itf.pu.mm.ISrvPUToMM#getPrayExecA(java.lang.String, java.lang.String, java.lang.String, nc.vo.pub.lang.UFDate)
   */
  public PrayExecVO[] getPrayExecA(String pk_corp, String pk_calbody, String pk_invbasdoc, UFDate sdate) throws BusinessException {
    try {
      return new PraybillDMO().getPrayExecA(pk_corp, pk_calbody, pk_invbasdoc, sdate); 
    }
    catch (SQLException e) {
      throw new BusinessException(e.getMessage());
    }
    catch (NamingException e) {
      throw new BusinessException(e.getMessage());
    }
    catch (SystemException e) {
      throw new BusinessException(e.getMessage());
    }
  }

  /**
   * 父类方法重写
   * 
   * @see nc.itf.pu.mm.ISrvPUToMM#getPrayExecB(java.lang.String, java.lang.String, java.lang.String, nc.vo.pub.lang.UFDate)
   */
  public PrayExecVO[] getPrayExecB(String pk_corp, String pk_calbody, String pk_invbasdoc, UFDate sdate) throws BusinessException {
    try {
      return new PraybillDMO().getPrayExecB(pk_corp, pk_calbody, pk_invbasdoc, sdate); 
    }
    catch (SQLException e) {
      throw new BusinessException(e.getMessage());
    }
    catch (NamingException e) {
      throw new BusinessException(e.getMessage());
    }
    catch (SystemException e) {
      throw new BusinessException(e.getMessage());
    }
  }

  /**
   * 父类方法重写
   * 
   * @see nc.itf.pu.mm.ISrvPUToMM#getPurExec(java.lang.String, java.lang.String, java.lang.String, nc.vo.pub.lang.UFDate)
   */
  public PurExecVO[] getPurExec(String pk_corp, String storeOrg, String baseid, UFDate date) throws BusinessException {
    try {
      return new PoToMmDMO().getPurExec(pk_corp, storeOrg, baseid, date); 
    }
    catch (SQLException e) {
      throw new BusinessException(e.getMessage());
    }
    catch (NamingException e) {
      throw new BusinessException(e.getMessage());
    }
    catch (SystemException e) {
      throw new BusinessException(e.getMessage());
    }
  }

  /**
   * 父类方法重写
   * 
   * @see nc.itf.pu.mm.ISrvPUToMM#getPrayExecA(java.lang.String, java.lang.String, java.lang.String, nc.vo.pub.lang.UFDate, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
   */
  public PrayExecVO[] getPrayExecA(String pk_corp, String pk_calbody, String pk_invbasdoc, UFDate sdate, String vfree1, String vfree2, String vfree3, String vfree4, String vfree5) throws BusinessException {
    try {
      return new PraybillDMO().getPrayExecA(pk_corp, pk_calbody, pk_invbasdoc, sdate, vfree1, vfree2, vfree3, vfree4, vfree5);
    }
    catch (SQLException e) {
      throw new BusinessException(e.getMessage());
    }
    catch (NamingException e) {
      throw new BusinessException(e.getMessage());
    }
    catch (SystemException e) {
      throw new BusinessException(e.getMessage());
    }
  }

  /**
   * 父类方法重写
   * 
   * @see nc.itf.pu.mm.ISrvPUToMM#getPrayExecB(java.lang.String, java.lang.String, java.lang.String, nc.vo.pub.lang.UFDate, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
   */
  public PrayExecVO[] getPrayExecB(String pk_corp, String pk_calbody, String pk_invbasdoc, UFDate sdate, String vfree1, String vfree2, String vfree3, String vfree4, String vfree5) throws BusinessException {
    try {
      return new PraybillDMO().getPrayExecB(pk_corp, pk_calbody, pk_invbasdoc, sdate, vfree1, vfree2, vfree3, vfree4, vfree5); 
    }
    catch (SQLException e) {
      throw new BusinessException(e.getMessage());
    }
    catch (NamingException e) {
      throw new BusinessException(e.getMessage());
    }
    catch (SystemException e) {
      throw new BusinessException(e.getMessage());
    }
  }

  /**
   * 父类方法重写
   * 
   * @see nc.itf.pu.mm.ISrvPUToMM#getPurExec(java.lang.String, java.lang.String, java.lang.String, nc.vo.pub.lang.UFDate, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
   */
  public PurExecVO[] getPurExec(String pk_corp, String storeOrg, String baseid, UFDate date, String vfree1, String vfree2, String vfree3, String vfree4, String vfree5) throws BusinessException {
    try {
      return new PoToMmDMO().getPurExec(pk_corp, storeOrg, baseid, date, vfree1, vfree2, vfree3, vfree4, vfree5); 
    }
    catch (SQLException e) {
      throw new BusinessException(e.getMessage());
    }
    catch (NamingException e) {
      throw new BusinessException(e.getMessage());
    }
    catch (SystemException e) {
      throw new BusinessException(e.getMessage());
    }
  }
	/**
	 * @function  插入SmartVO数组
	 *
	 * @author QuSida
	 *
	 * @param vo
	 * @throws Exception 
	 *
	 * @return void
	 *
	 * @date 2010-8-12 上午10:45:37
	 */
	public  void insertSmartVOs(SmartVO[] vos) throws Exception {		
		PraybillDMO dmo = new PraybillDMO();
        dmo.insertSmartVOs(vos);
	}
	/**
	 * @function 查询SmartVO数组
	 *
	 * @author QuSida
	 *
	 * @param voClass
	 * @param names
	 * @param where
	 * @return SmartVO[]
	 * @throws Exception 
	 *
	 * @date 2010-9-11 下午02:28:05
	 */
	public SmartVO[] querySmartVOs(Class voClass, String[] names, String where)
			throws Exception {
		PraybillDMO dmo = new PraybillDMO();
		SmartVO[] smartVOs = dmo.querySmartVOs(voClass,names,where);
		return smartVOs;
		
	}
	/**
	 * @function 删除SmartVO数组
	 *
	 * @author QuSida
	 *
	 * @param vos
	 * @throws Exception 
	 *
	 * @date 2010-9-11 下午02:27:33
	 */
	public void deleteSmartVOs(SmartVO[] vos) throws Exception {
		PraybillDMO dmo = new PraybillDMO();
		dmo.deleteSmartVOs(vos);
	}
	/**
	 * @function 修改SmartVO数组
	 *
	 * @author QuSida
	 *
	 * @param vos
	 * @throws Exception 
	 *
	 * @date 2010-9-11 下午02:27:36
	 */
	public void updateSmartVOs(SmartVO[] vos,String cbillid) throws Exception {
		PraybillDMO dmo = new PraybillDMO();

		dmo.updateSmartVOs(vos,cbillid);
	} 

	/**
	 * 发票保存时,回写票到数量至暂估应付单表体
	 * @author MeiChao
	 * @return 成功回写的行数
	 * @param Map: key: 应付单行ID 	value:发票数量
	 * @since 2010-11-11 
	 * @throws Exception 一般是数据库操作异常(网络问题,数据库问题等.)
	 *--累加数量的语句:
	 *update arap_djfb t set t.zyx17=to_char(To_number(case when zyx17 is null then '0' else zyx17 end)+17.55) where t.fb_oid='10022210000000005F7X';
	 *--判断表体是否全部生成发票完毕并更新表头标志的语句:
	 *update arap_djzb i set i.zyx19 = 'Y' where (select case
         when (select count(*)
                 from arap_djfb t
                where t.vouchid =
                      (select t.vouchid
                         from arap_djfb t
                        where t.fb_oid = '10022210000000005F7X')
                  and t.dfshl > (case when t.zyx17 is null then '0' else t.zyx17 end)) = 0 then  --贷方数量>已生成发票数量的记录为0 那么表示已全部生成发票
          'Y'
       end
  from dual)='Y' and i.vouchid=(select t.vouchid
                         from arap_djfb t
                        where t.fb_oid = '10022210000000005F7X');
	 * 
	 */
	public int updateSourceArriveNumToAP(Map bodyPKAndArriveNum) throws Exception{
		BaseDAO dao=new BaseDAO();
		//遍历Map
		Set keyset=bodyPKAndArriveNum.keySet();
		Object[] trueKeyArray=keyset.toArray();
		//更新行数
		Integer changeNum=0;
		for(int i=0;i<trueKeyArray.length;i++){
			String apBodyPK=trueKeyArray[i].toString();//应付单行id
			String invoiceNumber=bodyPKAndArriveNum.get(apBodyPK).toString();//发票数量
			//累加表体已生成发票数量,并将影响行数累加
			changeNum+=dao.executeUpdate("update arap_djfb t set t.zyx17=to_char(To_number(case when zyx17 is null then '0' else zyx17 end)+"+invoiceNumber+") where t.fb_oid='"+apBodyPK+"'");
			String apHeadChangeSQL="update arap_djzb i set i.zyx19 = 'Y' where (select case " +
					"when (select count(*) " +
					"from arap_djfb t " +
					"where t.vouchid = " +
					"(select t.vouchid " +
					"from arap_djfb t " +
					"where t.fb_oid = '"+apBodyPK+"') " +
					"and t.dfshl > (case when t.zyx17 is null then '0' else t.zyx17 end)) = 0 then " +
					"'Y' " +
					"end " +
					"from dual)='Y' and i.vouchid=(select t.vouchid " +
					"from arap_djfb t " +
					"where t.fb_oid = '"+apBodyPK+"') ";
			//执行主表: "是否已生成费用发票完毕"  标识校验SQL
			dao.executeUpdate(apHeadChangeSQL);
		}
		return changeNum;
	}
	
	/**
	 * 删除发票时,回写票到数量至暂估应付单表体
	 * @author MeiChao
	 * @return 成功回写的行数
	 * @param Map集合  组成:  key: 应付单行ID 	value:发票数量
	 * @since 2010-11-11 
	 * @throws Exception 一般是数据库操作异常(网络问题,数据库问题等.)
	 * 表体:回写数量大于zyx17字段中的已生成发票数量时,将zyx17字段置空
	 * 如果表头的zyx19字段中有N值,那么便表示当前暂估应付单生成过发票,并已删除发票.
	 */
	public int updateSourceNegativeNumToAP(Map bodyPKAndArriveNum) throws Exception{
	
		BaseDAO dao=new BaseDAO();
		//遍历Map
		Set keyset=bodyPKAndArriveNum.keySet();
		Object[] trueKeyArray=keyset.toArray();
		//更新行数
		Integer changeNum=0;
		for(int i=0;i<trueKeyArray.length;i++){
			String apBodyPK=trueKeyArray[i].toString();//应付单行id
			String invoiceNumber=bodyPKAndArriveNum.get(apBodyPK).toString();//发票数量
			//累加表体已生成发票数量,并将影响行数累加
			changeNum+=dao.executeUpdate("update arap_djfb t set t.zyx17=to_char(case when (To_number(case when zyx17 is null then '0' else zyx17 end)-"+invoiceNumber+")<0 then null end) where t.fb_oid='"+apBodyPK+"';");
			String apHeadChangeSQL="update arap_djzb i set i.zyx19 = 'N' where (select case " +
					"when (select count(*) " +
					"from arap_djfb t " +
					"where t.vouchid = " +
					"(select t.vouchid " +
					"from arap_djfb t " +
					"where t.fb_oid = '"+apBodyPK+"') " +
					"and t.dfshl > (case when t.zyx17 is null then '0' else t.zyx17 end)) > 0 then " +
					"'N' " +
					"end " +
					"from dual)='N' and i.vouchid=(select t.vouchid " +
					"from arap_djfb t " +
					"where t.fb_oid = '"+apBodyPK+"') ";
			//执行主表: "是否已生成费用发票完毕"  标识校验SQL
			dao.executeUpdate(apHeadChangeSQL);
		}
		return changeNum;
		
	}
}
