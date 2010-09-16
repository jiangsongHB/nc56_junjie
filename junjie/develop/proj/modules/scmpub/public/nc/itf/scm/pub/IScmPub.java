/*
 * ������
 * ˵����
 * 
 * ���ߣ�mwj
 * ���ڣ�2005-12-19
 */
package nc.itf.scm.pub;


import java.util.HashMap;
import java.util.Vector;

import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.scm.pub.smart.SmartVO;


/**
 * @author mwj FIXME
 */
public interface IScmPub {
	/**
	 * �˴����뷽��˵���� ��������: �Բ�ѯ�еĲ���,����ǰ����¼�����,���¼����ŵ����в��ż���,�м���or������ �������: ����ֵ: �쳣����:
	 * ����:
	 * 
	 * @return nc.vo.pub.query.ConditionVO[]
	 * @param condvo
	 *            nc.vo.pub.query.ConditionVO[]
	 * @param saPk_corp
	 *            java.lang.String[]
	 */
	public abstract nc.vo.pub.query.ConditionVO[] getTotalSubPkVO(
			nc.vo.pub.query.ConditionVO[] condvo, String[] saPk_corp)
			throws BusinessException;
	
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
	 * ���ߣ�WYF ���ܣ�DMO�Ķ�Ӧ���� ������String[] saBaseId ����ID[] String]] saAssistUnit
	 * ������λID[] ���أ��� ���⣺�� ���ڣ�(2004-07-07 11:39:21) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 */
	public abstract java.util.HashMap loadBatchInvConvRateInfo(
			String[] saBaseId, String[] saAssistUnit) throws BusinessException;

	/**
	 * ���ߣ���ӡ�� ���ܣ����ݱ���ѯ�ֶΡ���ѯ�����õ����������Ľ���� ������ String sTable ��SQL��FROM����ַ�
	 * ����Ϊ�ջ�մ� String sIdName �����ֶ�������"corderid" ����Ϊ�ջ�մ� String[] saFields
	 * ���ѯ�������� ����Ϊ��,Ԫ�ز���Ϊ�ջ�մ� String[] saId ���ѯ������ID���� ����Ϊ��,Ԫ�ز���Ϊ�ջ�մ�
	 * ���أ�Object[][] ����Ϊ�ջ���saId������ȡ��ṹ���£� ��fields[] =
	 * {"d1","d2","d3"}����=(56,"dge",2002-03-12) ���Ϊ�ձ�ʾδ�н�����ڣ�Ԫ��Ϊ�ձ�����ID��Ӧ��ֵ�����ڡ�
	 * ����������Ԫ�ؾ�Ϊ�յ����ؽ����Ϊ�գ���������Ϊ��1������� ���⣺SQLException SQL�쳣 ���ڣ�(2001-08-04
	 * 11:39:21) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־�� 2002-04-19 wyf �޸ķ��ؽ��Ϊ��ID���е�Object����
	 */
	public abstract Object[][] queryArrayValue(String sTable, String sIdName,
			String[] saFields, String[] saId) throws BusinessException;

	/**
	 * �˴����뷽��˵���� ��������: �Բ�ѯ�еĲ���,����ǰ����¼�����,���¼����ŵ����в��ż���,�м���or������
	 * ��˾ֻ�ܰ�����������,ת��һ�µ��ö๫˾�ĺ��� �������: ����ֵ: �쳣����: ����:
	 * 
	 * @return nc.vo.pub.query.ConditionVO[]
	 * @param condvo
	 *            nc.vo.pub.query.ConditionVO[]
	 * @param saPk_corp
	 *            java.lang.String[]
	 */
	public abstract nc.vo.pub.query.ConditionVO[] getTotalSubPkVOs(
			nc.vo.pub.query.ConditionVO[] condvo, String saPk_corp)
			throws BusinessException;
	
	public abstract String queryColumn(String tabName, String fieldName, String whereFilter) throws BusinessException;
	
	public abstract HashMap<String, UFBoolean> queryUsedDataPower(Vector<String> vecTableNames,
	    Vector<String>  vecRefNames,String pk_corp,String userID) throws BusinessException;
	}