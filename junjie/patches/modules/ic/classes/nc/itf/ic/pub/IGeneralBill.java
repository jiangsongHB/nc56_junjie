/*
 * 创建日期 2005-12-20
 *
 * TODO 要更改此生成的文件的模板，请转至
 * 窗口 － 首选项 － Java － 代码样式 － 代码模板
 */
package nc.itf.ic.pub;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import nc.bs.pub.SystemException;
import nc.vo.ic.ic700.WastageBillVO;
import nc.vo.ic.pub.bill.GeneralBillVO;
import nc.vo.ic.pub.bill.QryConditionVO;
import nc.vo.pub.BusinessException;
import nc.vo.scm.pub.smart.SmartVO;

/**
 * @author zhanghaiyan
 * 
 * TODO 要更改此生成的类型注释的模板，请转至 窗口 － 首选项 － Java － 代码样式 － 代码模板
 */
public interface IGeneralBill {
	/**
	 * 功能： 创建人：zhanghaiyan 创建日期：2005-12-20 备注：
	 * 
	 * @param sBillType
	 * @return
	 */
	public abstract ArrayList queryBills(String sBillType, QryConditionVO voQC)
			throws BusinessException;

	public abstract Object queryInfo(Integer iSel, Object alQryCond)
			throws BusinessException;

	public abstract ArrayList queryJointCheckBills(QryConditionVO voQC)
			throws BusinessException;

	public abstract ArrayList queryPartbySetInfo(String sSetInvID)
			throws SQLException, BusinessException, SystemException;

	/**
	 * 创建者：王乃军 功能：按指定条件查询单据。外模块查存库存单据。v53 add 参数： 返回： 例外： 日期：(2001-6-12
	 * 20:38:02) 修改日期，修改人，修改原因，注释标志： 2002-01-16 , 王乃军，移植到oracle
	 * 
	 * 
	 * @return nc.vo.ic.pub.bill.GeneralBillVO[]
	 * @param voQryCond
	 *            nc.vo.ic.pub.bill.QryConditionVO
	 */
	public ArrayList queryBillByPks_for_OutModule(String[] pks)
			throws BusinessException;

	/**
	 * 创建人：刘家清 创建日期：2008-3-31下午04:18:20 参数：签收途损单表体id 返回结果：HashMap<表头id,途损单VO>
	 * 
	 * @param bids
	 * @return
	 * @throws BusinessException
	 */
	public HashMap<String, WastageBillVO> getWastageBillsByBids(
			ArrayList<String> bids) throws BusinessException;

	/**
	 * 创建人：刘家清 创建日期：2008-3-31下午04:18:20 参数：表体id 返回结果：HashMap<表头id,VO>
	 * 
	 * @param bids
	 * @return
	 * @throws BusinessException
	 */
	public HashMap<String, GeneralBillVO> getGeneralBillVOsByBids(
			ArrayList<String> bids) throws BusinessException;
	
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
	 * join since 2010-11-07 MeiChao
	 * 
	 * 说明:其他入库单取消签字时,将对应的暂估应付单,库存调整单作废
	 * 
	 * 
	 */
	public abstract boolean rollbackICtoAPandIA(String generalPk, String APpks,String IApks) throws BusinessException;
	
	
	
}

