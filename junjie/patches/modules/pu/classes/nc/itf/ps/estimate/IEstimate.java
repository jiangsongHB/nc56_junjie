/*
 * �������� 2005-12-20
 *
 * TODO Ҫ���Ĵ����ɵ��ļ���ģ�壬��ת��
 * ���� �� ��ѡ�� �� Java �� ������ʽ �� ����ģ��
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
 * TODO Ҫ���Ĵ����ɵ�����ע�͵�ģ�壬��ת��
 * ���� �� ��ѡ�� �� Java �� ������ʽ �� ����ģ��
 */
public interface IEstimate {
	/**
	 * ��������:ȡ���ݹ�
	 * �������:EstimateVO[],��ǰ����ԱID
	 * ���ߣ��ܺ���
	 * ������2001-5-24 14:41:50
	 * �޸ģ���־ƽ��FOR��V30
	 */
	public abstract void antiEstimate(EstimateVO VOs[], ClientLink cl)
			throws BusinessException;
	
	public abstract void feeEstimate(EstimateVO VOs[], ArrayList paraList)
	throws BusinessException;

	public abstract void saveBillForARAP(EstimateVO VOs[], ArrayList paraList) 
			throws BusinessException;

	/**
	 * ��������:���ݹ�(��ⵥȡ��ǩ�ֵ���)
	 * �������:GeneralBillVO[]
	 * ����ֵ:void
	 * �쳣����:java.rmi.RemoteException
	 * ���ڣ�2003/06/10
	 * ���ߣ��ܺ���
	 * �޸ģ���־ƽ��FOR��V30
	 */
	public abstract void antiEstimateBatch(GeneralBillVO VOs[])
			throws BusinessException;

	/**
	 * ��������:�����ڳ��ݹ���ⵥ,���޸Ķ������ۼ��������
	 * �������:ArrayList
	 |-GeneralHVO[] , ��ⵥVO
	 |-String	������ԱID
	 |-String	����˾����
	 * ����ֵ:��
	 * �쳣����:javax.naming.NamingException, java.rmi.RemoteException, java.sql.SQLException
	 * ���ߣ��ܺ���
	 * �޸ģ���־ƽ V30  2004-06-03 ���������ݲ����
	 */
	public abstract void discard(ArrayList listPara)
			throws BusinessException;

	/**
	 * ��������:������ⵥ,���޸Ķ������ۼ��������
	 * �������:
	 * ����ֵ:[[��ⵥͷ����+������ⵥ������+��ⵥ�ţ�������ⵥʱ��],[��ⵥͷʱ���+���µ���ⵥ��ʱ���+���µ���ⵥ���ӱ�3��ʱ���]]
	 * �쳣����:
	 */
	public abstract ArrayList doSave(GeneralHVO VO, boolean bAdd,
			ArrayList lModify, String cOperator, OorderVO orderVOs[])
			throws BusinessException;

	/**
	 * ��������:�ݹ�
	 * �������:VO[],��ǰ����ԱID,��ǰ����
	 * �������ܺ���
	 * �޸ģ���־ƽ FOR  V30
	 */
	public abstract void estimate(EstimateVO VOs[], ArrayList paraList)
			throws BusinessException;

	/**
	 * �˴����뷽��˵����
	 * ��������:���������0,����ʾ
	 * �������:
	 * ����ֵ:
	 * �쳣����:
	 */
	public abstract String[][] getFreeItem0(GeneralHVO VOs[])
			throws BusinessException;

	/**
	 * �˴����뷽��˵����
	 * ��������:ҵ��Ա����������������ŵ�ID
	 * �������:ҵ��ԱID
	 * ����ֵ:����ID
	 * �쳣����:
	 */
	public abstract String getRefDeptKey(String cPsnID)
			throws BusinessException;

	/**
	 * �˴����뷽��˵����
	 * ��������:ҵ��Ա����������������ŵ�����
	 * �������:ҵ��ԱID
	 * ����ֵ:��������
	 * �쳣����:
	 */
	public abstract String getRefDeptName(String cPsnID)
			throws BusinessException;

	/**
	 * �˴����뷽��˵����
	 * ��������:ҵ��Ա������ת��Ϊ������Ա�����е�����
	 * �������:��ǰ����ԱID
	 * ����ֵ:��ǰ����Ա����Ա�����е�ID
	 * �쳣����:
	 */
	public abstract String getRefOperatorKey(String cOperatorID)
			throws BusinessException;

	/**
	 * �˴����뷽��˵����
	 * ��������:ҵ������ID����շ����(�ṩ���δ���)
	 * �������:ҵ������ID
	 * ����ֵ:�շ����ID
	 * �쳣����:
	 * �޸����ڣ�2003/02/11 xhq
	 */
	public abstract String[] getRSModeBatch(String cBusitypeID[])
			throws BusinessException;

	/**
	 * �˴����뷽��˵����
	 * ��������:�ֿ�ID��ÿ����֯ID
	 * �������:�ֿ�ID
	 * ����ֵ:�����֯ID
	 * �쳣����:
	 */
	public abstract String getStoreOrg(String cWarehouseID)
			throws BusinessException;

	/**
	 * �˴����뷽��˵����
	 * ��������:�ж���ⵥ�ĵ��ݺ��Ƿ��ظ�
	 * �������:��λ���룬���ݺţ���ͷ�������޸�ʱ��
	 * ����ֵ:��
	 * �쳣����:��
	 */
	public abstract boolean isCodeDuplicate(String unitCode, String billCode,
			String key) throws BusinessException;

	/**
	 * ͨ����λ���뷵��ָ����˾���м�¼VO���顣�����λ����Ϊ�շ������м�¼��
	 *
	 * �������ڣ�(2001-5-30)
	 * @return nc.vo.ps.estimate.EstimateVO[] �鵽��VO��������
	 * @param unitCode int
	 * @exception java.rmi.RemoteException �쳣˵����
	 */
	public abstract EstimateVO[] queryEstimate(String unitCode,
			ConditionVO conditionVO[], String sZG, String sEstPriceSource)
			throws BusinessException;

	/**
	 * 
	 * ��������������
	 * <p>����ⵥ�в�ѯ���ݹ�VO
	 * <p>
	 * <b>examples:</b>
	 * <p>
	 * ʹ��ʾ��
	 * <p>
	 * <b>����˵��</b>
	 * @param strLoginCorpId ��ǰ��˾ID
	 * @param sqlWherePart ��ѯ����
	 * @param strZG "N"��δ�ݹ�,"Y"�����ݹ�
	 * @return �ݹ�VO����,����Ϊnull
	 * @throws BusinessException
	 * <p>
	 * @author zhyhang
	 * @time 2008-10-6 ����08:51:40
	 */
	public EstimateVO[] queryEstimate(String strLoginCorpId,String sqlWherePart, String strZG)
	throws BusinessException;
  
	/**
	 * �˴����뷽��˵����
	 * ��������:��ѯ�ڳ��ݹ���ⵥ���弰�������ӱ�
	 * �������:
	 * ����ֵ:
	 * �쳣����:
	 * �޸����ڣ�2003/02/20
	 */
	public abstract ArrayList queryInitialBody(String pkHead, String ts)
			throws BusinessException;

	/**
	 * ����:��ѯ�ڳ��ݹ���ⵥ����--��ʱ����
	 * ����:String[],��ⵥ��ͷID[]��String[],��ⵥ��ͷTS[]
	 * ����:ArrayList{GeneralHItemVO[],GeneralBb3VO[];...}
	 * �쳣:SQLException
	 * ����:2003/02/20
	 * �޸�:��־ƽ FOR V30 Ч���Ż���������ʱ����ƴ�Ӳ�ѯ����
	 */
	public abstract ArrayList queryInitialBodyBatch(String pkHead[],
			String ts[]) throws BusinessException;

	/**
	 * �˴����뷽��˵����
	 * ��������:������ѯ(�������������ԵĴ��)
	 * �������:
	 * ����ֵ:
	 * �쳣����:
	 */
	public abstract OorderVO[] queryOrder(String unitCode,
			ConditionVO conditionVO[], String biztypeID[],
			String sEstPriceSource) throws BusinessException;
	
	/**
	 * 
	 * ��������������
	 * <p>������ѯ(�������������ԵĴ��)
	 * <p>
	 * <b>examples:</b>
	 * <p>
	 * ʹ��ʾ��
	 * <p>
	 * <b>����˵��</b>
	 * @param strLoginCorpId
	 * @param sCondition
	 * @return
	 * @throws BusinessException
	 * <p>
	 * @author zhaoyha
	 * @time 2008-10-15 ����03:59:58
	 */
	public OorderVO[] queryOrder(String strLoginCorpId, String sCondition) throws BusinessException;
	
	/**
	 * �˴����뷽��˵����
	 * ��������:��Դ�Ƕ�������ⵥ��ҵ������
	 * �������:
	 * ����ֵ:
	 * �쳣����:
	 */
	public abstract String[] querySpeBiztypeID(String unitCode)
			throws BusinessException;

	/**
	 * �˴����뷽��˵����
	 * ��������:��ѯ�ڳ��ݹ���ⵥ
	 * �������:
	 * ����ֵ:
	 * �쳣����:
	 */
	public abstract GeneralHVO[] queryStockForEstimate(String unitCode,
			ConditionVO conditionVO[]) throws BusinessException;

	/**
	 * �˴����뷽��˵����
	 * ��������:��ѯ�ڳ��ݹ���ⵥ
	 * �������:
	 * ����ֵ:
	 * �쳣����:
	 */
	public abstract GeneralHVO queryStockByHeadKey(String unitCode,
			String headKey) throws BusinessException;
	
	
	/**
	 * join since 2010-11-06 MeiChao
	 * 
	 * ˵��:�ɹ����÷�Ʊ����ʱ,����Ӧ�ĺ�����,������ɾ��
	 * 
	 * 
	 */
	public abstract boolean rollbackEstimate(String invoicePK, String APpks,String IApks) throws BusinessException;
	
}