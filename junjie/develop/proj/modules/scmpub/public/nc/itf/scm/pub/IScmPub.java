/*
 * 类名：
 * 说明：
 * 
 * 作者：mwj
 * 日期：2005-12-19
 */
package nc.itf.scm.pub;


import java.util.HashMap;
import java.util.Vector;

import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.scm.pub.smart.SmartVO;


/**
 * @author mwj FIXME
 */
public interface IScmPub {
	/**
	 * 此处插入方法说明。 功能描述: 对查询中的部门,如果是包含下级部门,把下级部门的所有部门加上,中间用or相连接 输入参数: 返回值: 异常处理:
	 * 日期:
	 * 
	 * @return nc.vo.pub.query.ConditionVO[]
	 * @param condvo
	 *            nc.vo.pub.query.ConditionVO[]
	 * @param saPk_corp
	 *            java.lang.String[]
	 */
	public abstract nc.vo.pub.query.ConditionVO[] getTotalSubPkVO(
			nc.vo.pub.query.ConditionVO[] condvo, String[] saPk_corp)
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
	 * 作者：WYF 功能：DMO的对应方法 参数：String[] saBaseId 基本ID[] String]] saAssistUnit
	 * 计量单位ID[] 返回：无 例外：无 日期：(2004-07-07 11:39:21) 修改日期，修改人，修改原因，注释标志：
	 */
	public abstract java.util.HashMap loadBatchInvConvRateInfo(
			String[] saBaseId, String[] saAssistUnit) throws BusinessException;

	/**
	 * 作者：王印芬 功能：根据表、查询字段、查询条件得到符合条件的结果。 参数： String sTable 表，SQL中FROM后的字符
	 * 不能为空或空串 String sIdName 主键字段名，如"corderid" 不能为空或空串 String[] saFields
	 * 需查询的所有域 不能为空,元素不能为空或空串 String[] saId 需查询的所有ID数组 不能为空,元素不能为空或空串
	 * 返回：Object[][] 可能为空或与saId长度相等。结构如下： 如fields[] =
	 * {"d1","d2","d3"}，则=(56,"dge",2002-03-12) 结果为空表示未有结果存在；元素为空表明该ID对应的值不存在。
	 * 不返回所有元素均为空但返回结果不为空，或结果长度为＜1的情况。 例外：SQLException SQL异常 日期：(2001-08-04
	 * 11:39:21) 修改日期，修改人，修改原因，注释标志： 2002-04-19 wyf 修改返回结果为按ID排列的Object数组
	 */
	public abstract Object[][] queryArrayValue(String sTable, String sIdName,
			String[] saFields, String[] saId) throws BusinessException;

	/**
	 * 此处插入方法说明。 功能描述: 对查询中的部门,如果是包含下级部门,把下级部门的所有部门加上,中间用or相连接
	 * 公司只能包含单个部门,转换一下调用多公司的函数 输入参数: 返回值: 异常处理: 日期:
	 * 
	 * @return nc.vo.pub.query.ConditionVO[]
	 * @param condvo
	 *            nc.vo.pub.query.ConditionVO[]
	 * @param saPk_corp
	 *            java.lang.String[]
	 */
	public abstract nc.vo.pub.query.ConditionVO[] getTotalSubPkVOs(
			nc.vo.pub.query.ConditionVO[] condvo, String saPk_corp)
			throws BusinessException;
	
	public abstract String queryColumn(String tabName, String fieldName, String whereFilter) throws BusinessException;
	
	public abstract HashMap<String, UFBoolean> queryUsedDataPower(Vector<String> vecTableNames,
	    Vector<String>  vecRefNames,String pk_corp,String userID) throws BusinessException;
	}