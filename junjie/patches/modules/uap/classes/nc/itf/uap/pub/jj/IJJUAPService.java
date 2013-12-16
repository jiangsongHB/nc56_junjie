package nc.itf.uap.pub.jj;

import java.util.List;
import nc.vo.ic.md.MdcrkVO;

/**
 * @function ���ܶ��ο��������ӿ�
 * 
 * @author QUSIDA
 * 
 * @date 2010-11-12 ����11:43:12
 *
 */
public interface IJJUAPService {
	
	/**
	 * @function ����ӿڵĲ����޸�Ϊһ��List<MdcrkVO>����,
	 * MdcrkVO�ĵ�10���Զ����ֶ��ڴ���ʱ����ע��һ���������
	 * ����PK,�ӿڵ������Ǹ��������������PK��ѯ������,�ٸ���
	 * ���,��ѯ����Ӧ�ĸ���ֵ.������Զ����ֶ�11,�����ǰ��ѯ
	 * ����,�򽫶�Ӧ�Զ����ֶ�11�ÿ�,����������Ϣ�����Զ����ֶ�12. ����ٽ�����List<MdcrkVO>����. 
	 *
	 * @author MeiChao
	 *
	 * @param List<MdcrkVO>
	 * 
	 * @throws Exception 
	 *
	 * @return List<MdcrkVO>
	 *
	 * @date 2010-11-05 ����9:58:57
	 */
	public abstract List<MdcrkVO> queryAdditionalvalue(List<MdcrkVO> mdvo) throws Exception;
	
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
