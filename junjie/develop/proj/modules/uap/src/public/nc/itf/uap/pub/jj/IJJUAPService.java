package nc.itf.uap.pub.jj;

/**
 * @function ���ܶ��ο��������ӿ�
 * 
 * @author QuSida
 * 
 * @date 2010-9-16 ����11:43:12
 *
 */
public interface IJJUAPService {
	
	/**
	 * @function ���ݴ����������ID�������ֵ
	 *
	 * @author QuSida
	 *
	 * @param pk_invbasdoc
	 * 
	 * @throws Exception 
	 *
	 * @return Object
	 *
	 * @date 2010-9-16 ����11:43:57
	 */
	public abstract Object  queryAdditionalvalue(String pk_invbasdoc) throws Exception;
	
	/**
	 * @function ���ݴ����������ID�������ϵ��
	 *
	 * @author QuSida
	 *
	 * @param pk_invbasdoc
	 * 
	 * @throws Exception 
	 *
	 * @return Object
	 *
	 * @date 2010-9-16 ����11:44:10
	 */
	public abstract Object  queryAdjustmentcoefficient(String pk_invbasdoc) throws Exception;
	
	/**
	 * @function ���ݴ����������ID���ë��ϵ��
	 *
	 * @author QuSida
	 *
	 * @param pk_invbasdoc
	 * 
	 * @throws Exception 
	 *
	 * @return Object
	 *
	 * @date 2010-9-16 ����11:44:13
	 */
	public abstract Object  queryBurrcoefficient(String pk_invbasdoc) throws Exception;
	
	/**
	 * @function ������ѯ����������sql�������Ҫ��
	 *
	 * @author QuSida
	 *
	 * @param sql
	 * 
	 * @throws Exception 
	 *
	 * @return Object
	 *
	 * @date 2010-9-16 ����11:44:16
	 */
	public abstract Object queryService(String sql) throws Exception;

}
