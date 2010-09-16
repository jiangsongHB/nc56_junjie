package nc.ui.scm.pub;

import nc.bs.framework.common.NCLocator;
import nc.itf.scm.pub.IScmPub;
import nc.vo.pub.query.ConditionVO;
import nc.vo.scm.pub.smart.SmartVO;

/**
 * business service ejb wrapper Created by EJBGenerator based on velocity
 * technology
 */
public class ScmPubHelper {
	private static String beanName = IScmPub.class.getName();
	
	
	/**
	 * @function 插入SmartVO数组
	 *
	 * @author QuSida
	 *
	 * @param vo
	 * @throws Exception 
	 *
	 * @return void
	 *
	 * @date 2010-8-12 上午10:53:19
	 */
	public static void insertSmartVOs(SmartVO[] vos) throws Exception {
		IScmPub bo = (IScmPub) NCLocator.getInstance().lookup(beanName);

         bo.insertSmartVOs(vos);
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
	 * @throws Exception 
	 *
	 * @return SmartVO[]
	 *
	 * @date 2010-9-11 下午02:28:45
	 */
	public static SmartVO[] querySmartVOs(Class voClass, String[] names, String where) throws Exception {
		IScmPub bo = (IScmPub) NCLocator.getInstance().lookup(beanName);

         return bo.querySmartVOs(voClass,names,where);
	}
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
	 * @date 2010-9-11 下午02:29:17
	 */
	public static void deleteSmartVOs(SmartVO[] vos) throws Exception{
		IScmPub bo = (IScmPub) NCLocator.getInstance().lookup(beanName);
		
	         bo.deleteSmartVOs(vos);
	}
	
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
	 * @date 2010-9-11 下午02:29:51
	 */
	public static void updateSmartVOs(SmartVO[] vos,String cbillid) throws Exception{
		IScmPub bo = (IScmPub) NCLocator.getInstance().lookup(beanName);
		
	         bo.updateSmartVOs(vos, cbillid);
	}
	
	public static ConditionVO[] getTotalSubPkVO(ConditionVO[] arg0,
			java.lang.String[] arg1) throws Exception {
		IScmPub bo = (IScmPub) NCLocator.getInstance().lookup(beanName);
		nc.vo.pub.query.ConditionVO[] o = null;
		o = bo.getTotalSubPkVO(arg0, arg1);
		return o;
	}

	public static java.util.HashMap loadBatchInvConvRateInfo(
			java.lang.String[] arg0, java.lang.String[] arg1) throws Exception {
		IScmPub bo = (IScmPub) NCLocator.getInstance().lookup(beanName);
		java.util.HashMap o = null;
		o = bo.loadBatchInvConvRateInfo(arg0, arg1);
		return o;
	}

	public static java.lang.Object[][] queryArrayValue(java.lang.String arg0,
			java.lang.String arg1, java.lang.String[] arg2,
			java.lang.String[] arg3) throws Exception {
		IScmPub bo = (IScmPub) NCLocator.getInstance().lookup(beanName);
		java.lang.Object[][] o = null;
		o = bo.queryArrayValue(arg0, arg1, arg2, arg3);
		return o;
	}

	public static ConditionVO[] getTotalSubPkVOs(ConditionVO[] arg0,
			java.lang.String arg1) throws Exception {
		IScmPub bo = (IScmPub) NCLocator.getInstance().lookup(beanName);
		nc.vo.pub.query.ConditionVO[] o = null;
		o = bo.getTotalSubPkVOs(arg0, arg1);
		return o;
	}
}