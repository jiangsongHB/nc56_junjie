package nc.bs.uap.pub.jj;


import nc.bs.dao.BaseDAO;
import nc.jdbc.framework.processor.ArrayListProcessor;
import nc.jdbc.framework.processor.ColumnProcessor;


public class JJUAPServiceImpl implements nc.itf.uap.pub.jj.IJJUAPService {

	/**
	 * @function ���ݴ�����������������ֵ
	 *
	 * @author QuSida
	 *
	 * @param pk_invbasdoc
	 * @return Object
	 * @throws Exception 
	 *
	 * @date 2010-9-16 ����04:12:17
	 */
	public Object queryAdditionalvalue(String pk_invbasdoc) throws Exception {
		BaseDAO dao = new BaseDAO();
		String sql = "select nadditionalvalue from jj_bd_additionalvalue where pk_invbasdoc ='"+pk_invbasdoc+"'" ;
		Object o = null ;
		o = dao.executeQuery(sql, new ColumnProcessor());
	     return o;
	}

	/**
	 * @function ���ݴ�����������������ϵ��
	 *
	 * @author QuSida
	 *
	 * @param pk_invbasdoc
	 * @return Object
	 * @throws Exception 
	 *
	 * @date 2010-9-16 ����04:13:07
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
	 * @function ���ݴ�������������ë��ϵ��
	 *
	 * @author QuSida
	 *
	 * @param pk_invbasdoc
	 * @return Object
	 * @throws Exception 
	 *
	 * @date 2010-9-16 ����04:13:46
	 */
	public Object queryBurrcoefficient(String pk_invbasdoc) throws Exception {
		BaseDAO dao = new BaseDAO();
		
		//begin 2010-10-12 MeiChao �޸�����: ���˲�ѯë��ϵ��sql�ı���jj_bd_additionalvalue�޸�Ϊ��ȷ�ı���jj_bd_burrcoefficient
//		String sql = "select nflash from jj_bd_additionalvalue where pk_invbasdoc ='"+pk_invbasdoc+"'" ;
		String sql = "select nflash from jj_bd_burrcoefficient where pk_invbasdoc ='"+pk_invbasdoc+"'" ;
		//end 2010-10-12 MeiChao �޸�����: ���˲�ѯë��ϵ��sql�ı���jj_bd_additionalvalue�޸�Ϊ��ȷ�ı���jj_bd_burrcoefficient
		Object o = null ;
		o = dao.executeQuery(sql, new ColumnProcessor());
	     return o;
	}

	/**
	 * @function ����SQL�������Ҫ��
	 *
	 * @author QuSida
	 *
	 * @param sql
	 * @return Object
	 * @throws Exception 
	 *
	 * @date 2010-9-16 ����04:14:19
	 */
	public Object queryService(String sql) throws Exception {
		BaseDAO dao = new BaseDAO();
		Object o = null ;
		o = dao.executeQuery(sql, new ArrayListProcessor());
	     return o;	
	}

}
