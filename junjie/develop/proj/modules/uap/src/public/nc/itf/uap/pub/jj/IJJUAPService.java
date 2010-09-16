package nc.itf.uap.pub.jj;

/**
 * @function 骏杰二次开发公共接口
 * 
 * @author QuSida
 * 
 * @date 2010-9-16 上午11:43:12
 *
 */
public interface IJJUAPService {
	
	/**
	 * @function 根据存货基本档案ID查出附加值
	 *
	 * @author QuSida
	 *
	 * @param pk_invbasdoc
	 * 
	 * @throws Exception 
	 *
	 * @return Object
	 *
	 * @date 2010-9-16 上午11:43:57
	 */
	public abstract Object  queryAdditionalvalue(String pk_invbasdoc) throws Exception;
	
	/**
	 * @function 根据存货基本档案ID查出理算系数
	 *
	 * @author QuSida
	 *
	 * @param pk_invbasdoc
	 * 
	 * @throws Exception 
	 *
	 * @return Object
	 *
	 * @date 2010-9-16 上午11:44:10
	 */
	public abstract Object  queryAdjustmentcoefficient(String pk_invbasdoc) throws Exception;
	
	/**
	 * @function 根据存货基本档案ID查出毛边系数
	 *
	 * @author QuSida
	 *
	 * @param pk_invbasdoc
	 * 
	 * @throws Exception 
	 *
	 * @return Object
	 *
	 * @date 2010-9-16 上午11:44:13
	 */
	public abstract Object  queryBurrcoefficient(String pk_invbasdoc) throws Exception;
	
	/**
	 * @function 公共查询方法，传入sql查出你想要的
	 *
	 * @author QuSida
	 *
	 * @param sql
	 * 
	 * @throws Exception 
	 *
	 * @return Object
	 *
	 * @date 2010-9-16 上午11:44:16
	 */
	public abstract Object queryService(String sql) throws Exception;

}
