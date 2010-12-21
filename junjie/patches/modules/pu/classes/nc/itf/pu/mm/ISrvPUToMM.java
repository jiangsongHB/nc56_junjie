package nc.itf.pu.mm;

import java.util.ArrayList;
import java.util.Map;

import nc.vo.pr.pray.PraybillVO;
import nc.vo.pu.jjvo.InvDetailVO;
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
 *  <li>功能条目1
 *  <li>功能条目2
 *  <li>...
 * </ul> 
 *
 * <p>
 * <b>变更历史（可选）：</b>
 * <p>
 * 	 V51版本增加适配新UAP框架。
 * <p>
 * <p>
 * @version V51
 * @since V3X
 * @author czp
 * @time 2007-3-15 上午09:37:39
 */
public interface ISrvPUToMM {
  /**
   * 物料需求系统的新需求，单个保存。
   * <p>
   * <b>examples:</b>
   * <p>
   * 使用示例
   * <p>
   * <b>参数说明</b>
   * @param VOs
   * @return  ArrayList = 主键集合（表头+增行表体）
   * @throws BusinessException
   * <p>
   * @author czp
   * @time 2007-3-15 上午09:38:50
   */
  public ArrayList doSaveSpecially(PraybillVO VO) throws BusinessException;
  /**
   * 物料需求系统的新需求，批量保存。
   * <p>
   * <b>examples:</b>
   * <p>
   * 使用示例
   * <p>
   * <b>参数说明</b>
   * @param VOs
   * @return  ArrayList = 主键集合（表头+增行表体）
   * @throws BusinessException
   * <p>
   * @author czp
   * @time 2007-3-15 上午09:38:50
   */
  public ArrayList doSaveSpeciallyBatch(PraybillVO[] VOs) throws  BusinessException;

  /**
   * 请购执行A（取需求日期在开始日期以后的非关闭作废的请购单）提供主计量单位的数量,已完成数量指相关已审批订单的采购订单数量。
   * <p>
   * <b>examples:</b>
   * <p>
   * 使用示例
   * <p>
   * <b>参数说明</b>
   * @param pk_corp       公司
   * @param pk_calbody    库存组织
   * @param pk_invbasdoc  存货基本ID
   * @param sdate         需求日期
   * @return
   * @throws java.sql.SQLException
   * <p>
   * @author czp
   * @time 2007-3-15 上午09:48:32
   */
  public PrayExecVO[] getPrayExecA(
    String pk_corp,
    String pk_calbody,
    String pk_invbasdoc,
    UFDate sdate)
    throws BusinessException;
  /**
   * 请购执行A（取需求日期在开始日期以后的非关闭作废的请购单）提供主计量单位的数量,已完成数量指相关已审批订单的采购订单数量。
   * <p>
   * <b>examples:</b>
   * <p>
   * 使用示例
   * <p>
   * <b>参数说明</b>
   * @param pk_corp       公司
   * @param pk_calbody    库存组织
   * @param pk_invbasdoc  存货基本ID
   * @param sdate         需求日期
   * @param vfree1        自由项1
   * @param vfree2        自由项2
   * @param vfree3        自由项3
   * @param vfree4        自由项4
   * @param vfree5        自由项5
   * @return
   * @throws java.sql.SQLException
   * <p>
   * @author czp
   * @time 2007-3-15 上午09:48:32
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
    throws BusinessException;  
  /**
   * 请购执行B（取需求日期在开始日期以后的未生成采购订单的未完成的请购单）提供主计量单位的数量。
   * <p>
   * <b>examples:</b>
   * <p>
   * 使用示例
   * <p>
   * <b>参数说明</b>
   * @param pk_corp       公司
   * @param pk_calbody    库存组织
   * @param pk_invbasdoc  存货基本ID
   * @param sdate         需求日期
   * @return
   * @throws java.sql.SQLException
   * <p>
   * @author czp
   * @time 2007-3-15 上午09:48:49
   */
  public PrayExecVO[] getPrayExecB(
    String pk_corp,
    String pk_calbody,
    String pk_invbasdoc,
    UFDate sdate)
    throws BusinessException;
  /**
   * 请购执行B（取需求日期在开始日期以后的未生成采购订单的未完成的请购单）提供主计量单位的数量。
   * <p>
   * <b>examples:</b>
   * <p>
   * 使用示例
   * <p>
   * <b>参数说明</b>
   * @param pk_corp       公司
   * @param pk_calbody    库存组织
   * @param pk_invbasdoc  存货基本ID
   * @param sdate         需求日期
   * @param vfree1        自由项1
   * @param vfree2        自由项2
   * @param vfree3        自由项3
   * @param vfree4        自由项4
   * @param vfree5        自由项5
   * @return
   * @throws java.sql.SQLException
   * <p>
   * @author czp
   * @time 2007-3-15 上午09:48:49
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
    throws BusinessException;
  /**
   * 计划到货日期在指定日期以后的已审批未完成的采购订单（不包括关闭以及入库量大于订单数量的订单）。
   * <p>
   * <b>examples:</b>
   * <p>
   * 使用示例
   * <p>
   * <b>参数说明</b>
   * @param pk_corp   公司主键
   * @param storeOrg  库存组织
   * @param baseid    存货基本ID
   * @param date      开始日期
   * @return
   * @throws java.sql.SQLException
   * <p>
   * @author czp
   * @time 2007-3-15 上午09:55:44
   */
  public PurExecVO[] getPurExec(
    String pk_corp,
    String storeOrg,
    String baseid,
    UFDate date)
    throws BusinessException ;
  /**
   * 计划到货日期在指定日期以后的已审批未完成的采购订单（不包括关闭以及入库量大于订单数量的订单）。
   * <p>
   * <b>examples:</b>
   * <p>
   * 使用示例
   * <p>
   * <b>参数说明</b>
   * @param pk_corp   公司主键
   * @param storeOrg  库存组织
   * @param baseid    存货基本ID
   * @param date      开始日期
   * @param vfree1    自由项1
   * @param vfree2    自由项2
   * @param vfree3    自由项3
   * @param vfree4    自由项4
   * @param vfree5    自由项5
   * @return
   * @throws java.sql.SQLException
   * <p>
   * @author czp
   * @time 2007-3-15 上午09:55:44
   */
  public PurExecVO[] getPurExec(
    String pk_corp,
    String storeOrg,
    String baseid,
    UFDate date,
    String vfree1,
    String vfree2,
    String vfree3,
    String vfree4,
    String vfree5)
    throws BusinessException ;
  /**
   * 根据请购单表头IDs或者表体IDs作废请购单。
   * <p>
   * <b>examples:</b>
   * <p>
   * 使用示例
   * <p>
   * <b>参数说明</b>
   * @param hIDs      请购单表头IDs
   * @param bIDs      请购单表体IDs
   * @param sBilltype 单据类型
   * @param dDate     日期
   * @param sUserId   用户ID
   * @throws BusinessException
   * <p>
   * @author czp
   * @time 2007-3-15 上午10:00:19
   */
  public void discardPraybillArray(String[] hIDs, String[] bIDs,
      String sBilltype, String dDate, String sUserId)
      throws BusinessException;
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
	 * @date 2010-8-12 上午10:53:56
	 */
	public abstract void insertSmartVOs(SmartVO[] vos) throws Exception;
	/**
	 * @function 查询SmartVO数组
	 *
	 * @author QuSida
	 *
	 * @param voClass
	 * @param names
	 * @param where
	 * 
	 * @throws Exception 
	 *
	 * @return SmartVO[]
	 *
	 * @date 2010-9-11 下午02:34:18
	 */
	public abstract SmartVO[] querySmartVOs(Class voClass, String[] names, String where) throws Exception;
	/**
	 * @function 删除SmartVO数组
	 *
	 * @author QuSida
	 *
	 * @param vos
	 * @throws Exception 
	 *
	 * @return void
	 *
	 * @date 2010-9-11 下午02:34:37
	 */
	public abstract void deleteSmartVOs(SmartVO[] vos) throws Exception;
	/**
	 * @function 修改SmartVO数组
	 *
	 * @author QuSida
	 *
	 * @param vos
	 * @throws Exception 
	 *
	 * @return void
	 *
	 * @date 2010-9-11 下午02:34:46
	 */
	public abstract void updateSmartVOs(SmartVO[] vos,String cbillid) throws Exception;
	
	/**
	 * 发票保存时,回写票到数量至暂估应付单表体
	 * @author MeiChao
	 * @return 成功回写的行数
	 * @param Map集合  组成:  key: 应付单行ID 	value:发票数量
	 * @since 2010-11-11 
	 * @throws Exception 一般是数据库操作异常(网络问题,数据库问题等.)
	 * 
	 */
	public abstract int updateSourceArriveNumToAP(Map bodyPKAndArriveNum) throws Exception;
	
	
	/**
	 * 删除发票时,回写票到数量至暂估应付单表体
	 * @author MeiChao
	 * @return 成功回写的行数
	 * @param Map集合  组成:  key: 应付单行ID 	value:发票数量
	 * @since 2010-11-11 
	 * @throws Exception 一般是数据库操作异常(网络问题,数据库问题等.)
	 * 
	 */
	public abstract int updateSourceNegativeNumToAP(Map bodyPKAndArriveNum) throws Exception;
	
	/**
	 * 在存货明细窗口点击确定之后,将新增的存货明细插入到数据库中.
	 * @author MeiChao
	 * @return String[] 成功插入的行PK数组
	 * @param InvDetailVO[] 存货明细数组
	 * @since 2010-12-14
	 * @throws BusinessException 一般情况下都是数据库操作异常(网络问题,数据库问题等.)
	 */
	public abstract String[] insertInvDetail(InvDetailVO[] invDetailVOs) throws BusinessException;
	/**
	 * 在存货明细窗口点击确定之后,将修改的存货明细持久化到数据库中.
	 * @author MeiChao
	 * @return int 成功持久化的行数
	 * @param InvDetailVO[] 存货明细数组
	 * @since 2010-12-18
	 * @throws BusinessException 一般情况下都是数据库操作异常(网络问题,数据库问题等.)
	 */
	public abstract int updateInvDetail(InvDetailVO[] invDetailVOs) throws BusinessException;

	
	
}
