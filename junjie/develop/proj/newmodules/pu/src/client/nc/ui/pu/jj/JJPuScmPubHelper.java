package nc.ui.pu.jj;

import nc.bs.framework.common.NCLocator;
import nc.itf.pu.mm.ISrvPUToMM;
import nc.vo.pub.query.ConditionVO;
import nc.vo.scm.pub.smart.SmartVO;

/**
 * business service ejb wrapper Created by EJBGenerator based on velocity
 * technology
 */
public class JJPuScmPubHelper {
	private static String beanName = ISrvPUToMM.class.getName();
	
	
	/**
	 * @function ����SmartVO����
	 *
	 * @author QuSida
	 *
	 * @param vo
	 * @throws Exception 
	 *
	 * @return void
	 *
	 * @date 2010-8-12 ����10:53:19
	 */
	public static void insertSmartVOs(SmartVO[] vos) throws Exception {
		ISrvPUToMM bo = (ISrvPUToMM) NCLocator.getInstance().lookup(beanName);

         bo.insertSmartVOs(vos);
	}

	/**
	 * @function ��ѯSmartVO����
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
	 * @date 2010-9-11 ����02:28:45
	 */
	public static SmartVO[] querySmartVOs(Class voClass, String[] names, String where) throws Exception {
		ISrvPUToMM bo = (ISrvPUToMM) NCLocator.getInstance().lookup(beanName);

         return bo.querySmartVOs(voClass,names,where);
	}
	/**
	 * @function ɾ��SmartVO����
	 *
	 * @author QuSida
	 *
	 * @param vos
	 * @throws Exception 
	 *
	 * @return void
	 *
	 * @date 2010-9-11 ����02:29:17
	 */
	public static void deleteSmartVOs(SmartVO[] vos) throws Exception{
		ISrvPUToMM bo = (ISrvPUToMM) NCLocator.getInstance().lookup(beanName);
		
	         bo.deleteSmartVOs(vos);
	}
	
	/**
	 * @function �޸�SmartVO����
	 *
	 * @author QuSida
	 *
	 * @param vos
	 * @throws Exception 
	 *
	 * @return void
	 *
	 * @date 2010-9-11 ����02:29:51
	 */
	public static void updateSmartVOs(SmartVO[] vos,String cbillid) throws Exception{
		ISrvPUToMM bo = (ISrvPUToMM) NCLocator.getInstance().lookup(beanName);
		
	         bo.updateSmartVOs(vos, cbillid);
	}
	
}