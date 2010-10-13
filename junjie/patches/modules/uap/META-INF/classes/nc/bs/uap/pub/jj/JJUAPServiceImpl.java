package nc.bs.uap.pub.jj;


import nc.bs.dao.BaseDAO;
import nc.jdbc.framework.processor.ArrayListProcessor;
import nc.jdbc.framework.processor.ColumnProcessor;


public class JJUAPServiceImpl implements nc.itf.uap.pub.jj.IJJUAPService {

	/**
	 * @function 根据存货基本档案查出附加值
	 *
	 * @author QuSida
	 *
	 * @param pk_invbasdoc
	 * @return Object
	 * @throws Exception 
	 *
	 * @date 2010-9-16 下午04:12:17
	 */
	public Object queryAdditionalvalue(String pk_invbasdoc) throws Exception {
		BaseDAO dao = new BaseDAO();
		String sql = "select nadditionalvalue from jj_bd_additionalvalue where pk_invbasdoc ='"+pk_invbasdoc+"'" ;
		Object o = null ;
		o = dao.executeQuery(sql, new ColumnProcessor());
	     return o;
	}

	/**
	 * @function 根据存货基本档案查出理算系数
	 *
	 * @author QuSida
	 *
	 * @param pk_invbasdoc
	 * @return Object
	 * @throws Exception 
	 *
	 * @date 2010-9-16 下午04:13:07
	 */
	public Object queryAdjustmentcoefficient(String pk_invbasdoc)
			throws Exception {
		BaseDAO dao = new BaseDAO();
		String sql = "select nadjustmentcoefficient from jj_bd_adjustmentcoefficient where pk_invbasdoc ='"+pk_invbasdoc+"'" ;
		Object o = null ;
		o = dao.executeQuery(sql, new ColumnProcessor());
	     return o;
	}

	/**
	 * @function 根据存货基本档案查出毛边系数
	 *
	 * @author QuSida
	 *
	 * @param pk_invbasdoc
	 * @return Object
	 * @throws Exception 
	 *
	 * @date 2010-9-16 下午04:13:46
	 */
	public Object queryBurrcoefficient(String pk_invbasdoc) throws Exception {
		BaseDAO dao = new BaseDAO();
		
		//begin 2010-10-12 MeiChao 修改内容: 将此查询毛边系数sql的表名jj_bd_additionalvalue修改为正确的表名jj_bd_burrcoefficient
//		String sql = "select nflash from jj_bd_additionalvalue where pk_invbasdoc ='"+pk_invbasdoc+"'" ;
		String sql = "select nflash from jj_bd_burrcoefficient where pk_invbasdoc ='"+pk_invbasdoc+"'" ;
		//end 2010-10-12 MeiChao 修改内容: 将此查询毛边系数sql的表名jj_bd_additionalvalue修改为正确的表名jj_bd_burrcoefficient
		Object o = null ;
		o = dao.executeQuery(sql, new ColumnProcessor());
	     return o;
	}

	/**
	 * @function 传入SQL查出你想要的
	 *
	 * @author QuSida
	 *
	 * @param sql
	 * @return Object
	 * @throws Exception 
	 *
	 * @date 2010-9-16 下午04:14:19
	 */
	public Object queryService(String sql) throws Exception {
		BaseDAO dao = new BaseDAO();
		Object o = null ;
		o = dao.executeQuery(sql, new ArrayListProcessor());
	     return o;	
	}

}
