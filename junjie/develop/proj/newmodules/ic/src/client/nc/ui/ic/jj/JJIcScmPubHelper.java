package nc.ui.ic.jj;

import nc.bs.framework.common.NCLocator;
import nc.itf.ic.pub.IGeneralBill;
import nc.itf.scm.pub.IScmPub;
import nc.vo.pub.query.ConditionVO;
import nc.vo.scm.pub.smart.SmartVO;

/**
 * business service ejb wrapper Created by EJBGenerator based on velocity
 * technology
 */
public class JJIcScmPubHelper {
	private static String beanName = IGeneralBill.class.getName();
	
	
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
		IGeneralBill bo = (IGeneralBill) NCLocator.getInstance().lookup(beanName);

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
		IGeneralBill bo = (IGeneralBill) NCLocator.getInstance().lookup(beanName);

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
		IGeneralBill bo = (IGeneralBill) NCLocator.getInstance().lookup(beanName);
		
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
		IGeneralBill bo = (IGeneralBill) NCLocator.getInstance().lookup(beanName);
		
	         bo.updateSmartVOs(vos, cbillid);
	}
	
}