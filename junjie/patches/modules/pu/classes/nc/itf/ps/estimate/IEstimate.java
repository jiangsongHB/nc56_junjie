/*
 * 创建日期 2005-12-20
 *
 * TODO 要更改此生成的文件的模板，请转至
 * 窗口 － 首选项 － Java － 代码样式 － 代码模板
 */
package nc.itf.ps.estimate;

import java.util.ArrayList;

import nc.vo.ic.pub.bill.GeneralBillVO;
import nc.vo.ps.estimate.EstimateVO;
import nc.vo.ps.estimate.GeneralHVO;
import nc.vo.ps.settle.OorderVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.query.ConditionVO;
import nc.vo.scm.pub.session.ClientLink;

/**
 * @author xhq
 *
 * TODO 要更改此生成的类型注释的模板，请转至
 * 窗口 － 首选项 － Java － 代码样式 － 代码模板
 */
public interface IEstimate {
	/**
	 * 功能描述:取消暂估
	 * 输入参数:EstimateVO[],当前操作员ID
	 * 作者：熊海情
	 * 创建：2001-5-24 14:41:50
	 * 修改：晁志平　FOR　V30
	 */
	public abstract void antiEstimate(EstimateVO VOs[], ClientLink cl)
			throws BusinessException;
	
	public abstract void feeEstimate(EstimateVO VOs[], ArrayList paraList)
	throws BusinessException;

	public abstract void saveBillForARAP(EstimateVO VOs[], ArrayList paraList) 
			throws BusinessException;

	/**
	 * 功能描述:反暂估(入库单取消签字调用)
	 * 输入参数:GeneralBillVO[]
	 * 返回值:void
	 * 异常处理:java.rmi.RemoteException
	 * 日期：2003/06/10
	 * 作者：熊海情
	 * 修改：晁志平　FOR　V30
	 */
	public abstract void antiEstimateBatch(GeneralBillVO VOs[])
			throws BusinessException;

	/**
	 * 功能描述:作废期初暂估入库单,并修改订单的累计入库数量
	 * 输入参数:ArrayList
	 |-GeneralHVO[] , 入库单VO
	 |-String	，操作员ID
	 |-String	，公司主键
	 * 返回值:无
	 * 异常处理:javax.naming.NamingException, java.rmi.RemoteException, java.sql.SQLException
	 * 作者：熊海情
	 * 修改：晁志平 V30  2004-06-03 增加数量容差控制
	 */
	public abstract void discard(ArrayList listPara)
			throws BusinessException;

	/**
	 * 功能描述:保存入库单,并修改订单的累计入库数量
	 * 输入参数:
	 * 返回值:[[入库单头主键+新增入库单行主键+入库单号（新增入库单时）],[入库单头时间戳+更新的入库单行时间戳+更新的入库单子子表3行时间戳]]
	 * 异常处理:
	 */
	public abstract ArrayList doSave(GeneralHVO VO, boolean bAdd,
			ArrayList lModify, String cOperator, OorderVO orderVOs[])
			throws BusinessException;

	/**
	 * 功能描述:暂估
	 * 输入参数:VO[],当前操作员ID,当前日期
	 * 创建：熊海情
	 * 修改：晁志平 FOR  V30
	 */
	public abstract void estimate(EstimateVO VOs[], ArrayList paraList)
			throws BusinessException;

	/**
	 * 此处插入方法说明。
	 * 功能描述:获得自由项0,供显示
	 * 输入参数:
	 * 返回值:
	 * 异常处理:
	 */
	public abstract String[][] getFreeItem0(GeneralHVO VOs[])
			throws BusinessException;

	/**
	 * 此处插入方法说明。
	 * 功能描述:业务员的主键获得所属部门的ID
	 * 输入参数:业务员ID
	 * 返回值:部门ID
	 * 异常处理:
	 */
	public abstract String getRefDeptKey(String cPsnID)
			throws BusinessException;

	/**
	 * 此处插入方法说明。
	 * 功能描述:业务员的主键获得所属部门的名称
	 * 输入参数:业务员ID
	 * 返回值:部门名称
	 * 异常处理:
	 */
	public abstract String getRefDeptName(String cPsnID)
			throws BusinessException;

	/**
	 * 此处插入方法说明。
	 * 功能描述:业务员的主键转换为其在人员档案中的主键
	 * 输入参数:当前操作员ID
	 * 返回值:当前操作员在人员档案中的ID
	 * 异常处理:
	 */
	public abstract String getRefOperatorKey(String cOperatorID)
			throws BusinessException;

	/**
	 * 此处插入方法说明。
	 * 功能描述:业务类型ID获得收发类别(提供批次处理)
	 * 输入参数:业务类型ID
	 * 返回值:收发类别ID
	 * 异常处理:
	 * 修改日期：2003/02/11 xhq
	 */
	public abstract String[] getRSModeBatch(String cBusitypeID[])
			throws BusinessException;

	/**
	 * 此处插入方法说明。
	 * 功能描述:仓库ID获得库存组织ID
	 * 输入参数:仓库ID
	 * 返回值:库存组织ID
	 * 异常处理:
	 */
	public abstract String getStoreOrg(String cWarehouseID)
			throws BusinessException;

	/**
	 * 此处插入方法说明。
	 * 功能描述:判断入库单的单据号是否重复
	 * 输入参数:单位编码，单据号，表头主键（修改时）
	 * 返回值:无
	 * 异常处理:无
	 */
	public abstract boolean isCodeDuplicate(String unitCode, String billCode,
			String key) throws BusinessException;

	/**
	 * 通过单位编码返回指定公司所有记录VO数组。如果单位编码为空返回所有记录。
	 *
	 * 创建日期：(2001-5-30)
	 * @return nc.vo.ps.estimate.EstimateVO[] 查到的VO对象数组
	 * @param unitCode int
	 * @exception java.rmi.RemoteException 异常说明。
	 */
	public abstract EstimateVO[] queryEstimate(String unitCode,
			ConditionVO conditionVO[], String sZG, String sEstPriceSource)
			throws BusinessException;

	/**
	 * 
	 * 方法功能描述：
	 * <p>从入库单中查询出暂估VO
	 * <p>
	 * <b>examples:</b>
	 * <p>
	 * 使用示例
	 * <p>
	 * <b>参数说明</b>
	 * @param strLoginCorpId 当前公司ID
	 * @param sqlWherePart 查询条件
	 * @param strZG "N"查未暂估,"Y"查已暂估
	 * @return 暂估VO数组,可能为null
	 * @throws BusinessException
	 * <p>
	 * @author zhyhang
	 * @time 2008-10-6 下午08:51:40
	 */
	public EstimateVO[] queryEstimate(String strLoginCorpId,String sqlWherePart, String strZG)
	throws BusinessException;
  
	/**
	 * 此处插入方法说明。
	 * 功能描述:查询期初暂估入库单表体及结算子子表
	 * 输入参数:
	 * 返回值:
	 * 异常处理:
	 * 修改日期：2003/02/20
	 */
	public abstract ArrayList queryInitialBody(String pkHead, String ts)
			throws BusinessException;

	/**
	 * 功能:查询期初暂估入库单表体--临时表方案
	 * 输入:String[],入库单表头ID[]；String[],入库单表头TS[]
	 * 返回:ArrayList{GeneralHItemVO[],GeneralBb3VO[];...}
	 * 异常:SQLException
	 * 日期:2003/02/20
	 * 修改:晁志平 FOR V30 效率优化，采用临时表方案拼接查询条件
	 */
	public abstract ArrayList queryInitialBodyBatch(String pkHead[],
			String ts[]) throws BusinessException;

	/**
	 * 此处插入方法说明。
	 * 功能描述:订单查询(不包括费用属性的存货)
	 * 输入参数:
	 * 返回值:
	 * 异常处理:
	 */
	public abstract OorderVO[] queryOrder(String unitCode,
			ConditionVO conditionVO[], String biztypeID[],
			String sEstPriceSource) throws BusinessException;
	
	/**
	 * 
	 * 方法功能描述：
	 * <p>订单查询(不包括费用属性的存货)
	 * <p>
	 * <b>examples:</b>
	 * <p>
	 * 使用示例
	 * <p>
	 * <b>参数说明</b>
	 * @param strLoginCorpId
	 * @param sCondition
	 * @return
	 * @throws BusinessException
	 * <p>
	 * @author zhaoyha
	 * @time 2008-10-15 下午03:59:58
	 */
	public OorderVO[] queryOrder(String strLoginCorpId, String sCondition) throws BusinessException;
	
	/**
	 * 此处插入方法说明。
	 * 功能描述:来源是订单的入库单的业务类型
	 * 输入参数:
	 * 返回值:
	 * 异常处理:
	 */
	public abstract String[] querySpeBiztypeID(String unitCode)
			throws BusinessException;

	/**
	 * 此处插入方法说明。
	 * 功能描述:查询期初暂估入库单
	 * 输入参数:
	 * 返回值:
	 * 异常处理:
	 */
	public abstract GeneralHVO[] queryStockForEstimate(String unitCode,
			ConditionVO conditionVO[]) throws BusinessException;

	/**
	 * 此处插入方法说明。
	 * 功能描述:查询期初暂估入库单
	 * 输入参数:
	 * 返回值:
	 * 异常处理:
	 */
	public abstract GeneralHVO queryStockByHeadKey(String unitCode,
			String headKey) throws BusinessException;
	
	
	/**
	 * join since 2010-11-06 MeiChao
	 * 
	 * 说明:采购费用发票弃审时,将对应的红蓝单,调整单删除
	 * 
	 * 
	 */
	public abstract boolean rollbackEstimate(String invoicePK, String APpks,String IApks) throws BusinessException;
	
}