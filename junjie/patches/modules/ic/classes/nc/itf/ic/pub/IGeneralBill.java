/*
 * �������� 2005-12-20
 *
 * TODO Ҫ���Ĵ����ɵ��ļ���ģ�壬��ת��
 * ���� �� ��ѡ�� �� Java �� ������ʽ �� ����ģ��
 */
package nc.itf.ic.pub;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import nc.bs.pub.SystemException;
import nc.vo.ic.ic700.WastageBillVO;
import nc.vo.ic.pub.bill.GeneralBillVO;
import nc.vo.ic.pub.bill.QryConditionVO;
import nc.vo.pub.BusinessException;
import nc.vo.scm.pub.smart.SmartVO;

/**
 * @author zhanghaiyan
 * 
 * TODO Ҫ���Ĵ����ɵ�����ע�͵�ģ�壬��ת�� ���� �� ��ѡ�� �� Java �� ������ʽ �� ����ģ��
 */
public interface IGeneralBill {
	/**
	 * ���ܣ� �����ˣ�zhanghaiyan �������ڣ�2005-12-20 ��ע��
	 * 
	 * @param sBillType
	 * @return
	 */
	public abstract ArrayList queryBills(String sBillType, QryConditionVO voQC)
			throws BusinessException;

	public abstract Object queryInfo(Integer iSel, Object alQryCond)
			throws BusinessException;

	public abstract ArrayList queryJointCheckBills(QryConditionVO voQC)
			throws BusinessException;

	public abstract ArrayList queryPartbySetInfo(String sSetInvID)
			throws SQLException, BusinessException, SystemException;

	/**
	 * �����ߣ����˾� ���ܣ���ָ��������ѯ���ݡ���ģ�����浥�ݡ�v53 add ������ ���أ� ���⣺ ���ڣ�(2001-6-12
	 * 20:38:02) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־�� 2002-01-16 , ���˾�����ֲ��oracle
	 * 
	 * 
	 * @return nc.vo.ic.pub.bill.GeneralBillVO[]
	 * @param voQryCond
	 *            nc.vo.ic.pub.bill.QryConditionVO
	 */
	public ArrayList queryBillByPks_for_OutModule(String[] pks)
			throws BusinessException;

	/**
	 * �����ˣ������� �������ڣ�2008-3-31����04:18:20 ������ǩ��;�𵥱���id ���ؽ����HashMap<��ͷid,;��VO>
	 * 
	 * @param bids
	 * @return
	 * @throws BusinessException
	 */
	public HashMap<String, WastageBillVO> getWastageBillsByBids(
			ArrayList<String> bids) throws BusinessException;

	/**
	 * �����ˣ������� �������ڣ�2008-3-31����04:18:20 ����������id ���ؽ����HashMap<��ͷid,VO>
	 * 
	 * @param bids
	 * @return
	 * @throws BusinessException
	 */
	public HashMap<String, GeneralBillVO> getGeneralBillVOsByBids(
			ArrayList<String> bids) throws BusinessException;
	
	/**
	 * @function  ����SmartVO����
	 *
	 * @author QuSida
	 *
	 * @param vo
	 * @throws Exception 
	 *
	 * @return void
	 *
	 * @date 2010-8-12 ����10:53:56
	 */
	public abstract void insertSmartVOs(SmartVO[] vos) throws Exception;
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
	 * @date 2010-9-11 ����02:34:18
	 */
	public abstract SmartVO[] querySmartVOs(Class voClass, String[] names, String where) throws Exception;
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
	 * @date 2010-9-11 ����02:34:37
	 */
	public abstract void deleteSmartVOs(SmartVO[] vos) throws Exception;
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
	 * @date 2010-9-11 ����02:34:46
	 */
	public abstract void updateSmartVOs(SmartVO[] vos,String cbillid) throws Exception;
	
	/**
	 * join since 2010-11-07 MeiChao
	 * 
	 * ˵��:������ⵥȡ��ǩ��ʱ,����Ӧ���ݹ�Ӧ����,������������
	 * 
	 * 
	 */
	public abstract boolean rollbackICtoAPandIA(String generalPk, String APpks,String IApks) throws BusinessException;
	
	
	
}

