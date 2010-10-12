package nc.ui.ps.estimate;

import java.util.ArrayList;

import nc.bs.framework.common.NCLocator;
import nc.itf.ps.estimate.IEstimate;
import nc.vo.ps.estimate.EstimateVO;
import nc.vo.ps.settle.OorderVO;
import nc.vo.pub.BusinessException;
import nc.vo.scm.pub.session.ClientLink;

public class EstimateHelper  {
private  static String beanName=IEstimate.class.getName();

public static void feeEstimate(nc.vo.ps.estimate.EstimateVO[] p0,ArrayList  p1) throws Exception{
	IEstimate bo = (IEstimate)nc.bs.framework.common.NCLocator.getInstance().lookup(beanName);
	bo.feeEstimate(p0,p1);
 }

public static void antiEstimate(nc.vo.ps.estimate.EstimateVO[] p0,ClientLink p1) throws Exception{
	IEstimate bo = (IEstimate)nc.bs.framework.common.NCLocator.getInstance().lookup(beanName);
	bo.antiEstimate(p0,p1);
 }

public static void antiEstimateBatch(nc.vo.ic.pub.bill.GeneralBillVO[] p0) throws Exception{
	IEstimate bo = (IEstimate)nc.bs.framework.common.NCLocator.getInstance().lookup(beanName);
	bo.antiEstimateBatch(p0);
 }

public static void discard(java.util.ArrayList p0) throws Exception{
	IEstimate bo = (IEstimate)nc.bs.framework.common.NCLocator.getInstance().lookup(beanName);
	bo.discard(p0);
 }

public static java.util.ArrayList doSave(nc.vo.ps.estimate.GeneralHVO p0,boolean p1,java.util.ArrayList p2,java.lang.String p3,nc.vo.ps.settle.OorderVO[] p4) throws Exception{
	IEstimate bo = (IEstimate)nc.bs.framework.common.NCLocator.getInstance().lookup(beanName);
	Object result=bo.doSave(p0,p1,p2,p3,p4);
	return (java.util.ArrayList)result;
 }

public static void estimate(nc.vo.ps.estimate.EstimateVO[] p0,java.util.ArrayList p1) throws Exception{
	IEstimate bo = (IEstimate)nc.bs.framework.common.NCLocator.getInstance().lookup(beanName);
	bo.estimate(p0,p1);
 }

public static java.lang.String getStoreOrg(java.lang.String p0) throws Exception{
	IEstimate bo = (IEstimate)nc.bs.framework.common.NCLocator.getInstance().lookup(beanName);
	Object result = bo.getStoreOrg(p0);
	return (java.lang.String)result;
 }

public static java.lang.String[][] getFreeItem0(nc.vo.ps.estimate.GeneralHVO[] p0) throws Exception{
	IEstimate bo = (IEstimate)nc.bs.framework.common.NCLocator.getInstance().lookup(beanName);
	Object result = bo.getFreeItem0(p0);
	if(result == null)
		return null;
	else
		return (java.lang.String[][])result;
 }

public static java.lang.String getRefDeptKey(java.lang.String p0) throws Exception{
	IEstimate bo = (IEstimate)nc.bs.framework.common.NCLocator.getInstance().lookup(beanName);
	Object result = bo.getRefDeptKey(p0);
	return (java.lang.String)result;
 }

public static java.lang.String getRefDeptName(java.lang.String p0) throws Exception{
	IEstimate bo = (IEstimate)nc.bs.framework.common.NCLocator.getInstance().lookup(beanName);
	Object result = bo.getRefDeptName(p0);
	return (java.lang.String)result;
 }

public static java.lang.String getRefOperatorKey(java.lang.String p0) throws Exception{
	IEstimate bo = (IEstimate)nc.bs.framework.common.NCLocator.getInstance().lookup(beanName);
	Object result = bo.getRefOperatorKey(p0);
	return (java.lang.String)result;
 }

public static java.lang.String[] getRSModeBatch(java.lang.String[] p0) throws Exception{
	IEstimate bo = (IEstimate)nc.bs.framework.common.NCLocator.getInstance().lookup(beanName);
	Object result = bo.getRSModeBatch(p0);
	if(result == null)
		return null;
	else
		return (java.lang.String[])result;
 }

public static boolean isCodeDuplicate(java.lang.String p0,java.lang.String p1,java.lang.String p2) throws Exception{
	IEstimate bo = (IEstimate)nc.bs.framework.common.NCLocator.getInstance().lookup(beanName);
	boolean result = bo.isCodeDuplicate(p0,p1,p2);
	return result;
 }

public static nc.vo.ps.estimate.EstimateVO[] queryEstimate(java.lang.String p0,nc.vo.pub.query.ConditionVO[] p1,java.lang.String p2,java.lang.String p3) throws Exception{
	IEstimate bo = (IEstimate)nc.bs.framework.common.NCLocator.getInstance().lookup(beanName);
	Object result = bo.queryEstimate(p0,p1,p2,p3);
	if(result == null)
		return null;
	else
		return (nc.vo.ps.estimate.EstimateVO[])result;
 }

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
public static EstimateVO[] queryEstimate(String pk_corp,String sqlWherePart,String isEst) throws Exception{
  IEstimate bo = (IEstimate)NCLocator.getInstance().lookup(beanName);
  return bo.queryEstimate(pk_corp,sqlWherePart,isEst);
}

public static java.util.ArrayList queryInitialBody(java.lang.String p0,java.lang.String p1) throws Exception{
	IEstimate bo = (IEstimate)nc.bs.framework.common.NCLocator.getInstance().lookup(beanName);
	Object result = bo.queryInitialBody(p0,p1);
	return (java.util.ArrayList)result;
 }

public static java.util.ArrayList queryInitialBodyBatch(java.lang.String[] p0,java.lang.String[] p1) throws Exception{
	IEstimate bo = (IEstimate)nc.bs.framework.common.NCLocator.getInstance().lookup(beanName);
	Object result = bo.queryInitialBodyBatch(p0,p1);
	return (java.util.ArrayList)result;
 }

public static nc.vo.ps.estimate.GeneralHVO[] queryStockForEstimate(java.lang.String p0,nc.vo.pub.query.ConditionVO[] p1) throws Exception{
	IEstimate bo = (IEstimate)nc.bs.framework.common.NCLocator.getInstance().lookup(beanName);
	Object result = bo.queryStockForEstimate(p0,p1);
	if(result == null)
		return null;
	else
		return (nc.vo.ps.estimate.GeneralHVO[])result;
 }

public static nc.vo.ps.settle.OorderVO[] queryOrder(java.lang.String p0,nc.vo.pub.query.ConditionVO[] p1,java.lang.String[] p2,java.lang.String p3) throws Exception{
	IEstimate bo = (IEstimate)nc.bs.framework.common.NCLocator.getInstance().lookup(beanName);
	Object result = bo.queryOrder(p0,p1,p2,p3);
	if(result == null)
		return null;
	else
		return (nc.vo.ps.settle.OorderVO[])result;
 }

/**
 * 
 * 方法功能描述：
 * <p>订单查询
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
 * @time 2008-10-15 下午04:13:34
 */
public static OorderVO[] queryOrder(String strLoginCorpId, String sCondition) throws BusinessException{
  IEstimate bo = (IEstimate)NCLocator.getInstance().lookup(beanName);
  return (OorderVO[])bo.queryOrder(strLoginCorpId,sCondition);
}


public static java.lang.String[] querySpeBiztypeID(java.lang.String p0) throws Exception{
	IEstimate bo = (IEstimate)nc.bs.framework.common.NCLocator.getInstance().lookup(beanName);
	Object result = bo.querySpeBiztypeID(p0);
	if(result == null)
		return null;
	else
		return (java.lang.String[])result;
 }

public static nc.vo.ps.estimate.GeneralHVO queryStockByHeadKey(java.lang.String p0,java.lang.String p1) throws Exception{
	IEstimate bo = (IEstimate)nc.bs.framework.common.NCLocator.getInstance().lookup(beanName);
	Object result = bo.queryStockByHeadKey(p0,p1);
	return (nc.vo.ps.estimate.GeneralHVO)result;
 }

public static void saveBillForARAP(nc.vo.ps.estimate.EstimateVO[] p0,ArrayList p1) throws Exception{
	IEstimate bo = (IEstimate)nc.bs.framework.common.NCLocator.getInstance().lookup(beanName);
	bo.saveBillForARAP(p0,p1);
 }
};