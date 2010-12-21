package nc.itf.pu.mm;

import java.util.ArrayList;
import java.util.Map;

import nc.vo.pr.pray.PraybillVO;
import nc.vo.pu.jjvo.InvDetailVO;
import nc.vo.pu.pr.PrayExecVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDate;
import nc.vo.scm.pu.PurExecVO;
import nc.vo.scm.pub.smart.SmartVO;

/**
 * <p>
 * <b>������Ҫ������¹��ܣ�</b>
 * 
 * <ul>
 *  <li>������Ŀ1
 *  <li>������Ŀ2
 *  <li>...
 * </ul> 
 *
 * <p>
 * <b>�����ʷ����ѡ����</b>
 * <p>
 * 	 V51�汾����������UAP��ܡ�
 * <p>
 * <p>
 * @version V51
 * @since V3X
 * @author czp
 * @time 2007-3-15 ����09:37:39
 */
public interface ISrvPUToMM {
  /**
   * ��������ϵͳ�������󣬵������档
   * <p>
   * <b>examples:</b>
   * <p>
   * ʹ��ʾ��
   * <p>
   * <b>����˵��</b>
   * @param VOs
   * @return  ArrayList = �������ϣ���ͷ+���б��壩
   * @throws BusinessException
   * <p>
   * @author czp
   * @time 2007-3-15 ����09:38:50
   */
  public ArrayList doSaveSpecially(PraybillVO VO) throws BusinessException;
  /**
   * ��������ϵͳ���������������档
   * <p>
   * <b>examples:</b>
   * <p>
   * ʹ��ʾ��
   * <p>
   * <b>����˵��</b>
   * @param VOs
   * @return  ArrayList = �������ϣ���ͷ+���б��壩
   * @throws BusinessException
   * <p>
   * @author czp
   * @time 2007-3-15 ����09:38:50
   */
  public ArrayList doSaveSpeciallyBatch(PraybillVO[] VOs) throws  BusinessException;

  /**
   * �빺ִ��A��ȡ���������ڿ�ʼ�����Ժ�ķǹر����ϵ��빺�����ṩ��������λ������,���������ָ��������������Ĳɹ�����������
   * <p>
   * <b>examples:</b>
   * <p>
   * ʹ��ʾ��
   * <p>
   * <b>����˵��</b>
   * @param pk_corp       ��˾
   * @param pk_calbody    �����֯
   * @param pk_invbasdoc  �������ID
   * @param sdate         ��������
   * @return
   * @throws java.sql.SQLException
   * <p>
   * @author czp
   * @time 2007-3-15 ����09:48:32
   */
  public PrayExecVO[] getPrayExecA(
    String pk_corp,
    String pk_calbody,
    String pk_invbasdoc,
    UFDate sdate)
    throws BusinessException;
  /**
   * �빺ִ��A��ȡ���������ڿ�ʼ�����Ժ�ķǹر����ϵ��빺�����ṩ��������λ������,���������ָ��������������Ĳɹ�����������
   * <p>
   * <b>examples:</b>
   * <p>
   * ʹ��ʾ��
   * <p>
   * <b>����˵��</b>
   * @param pk_corp       ��˾
   * @param pk_calbody    �����֯
   * @param pk_invbasdoc  �������ID
   * @param sdate         ��������
   * @param vfree1        ������1
   * @param vfree2        ������2
   * @param vfree3        ������3
   * @param vfree4        ������4
   * @param vfree5        ������5
   * @return
   * @throws java.sql.SQLException
   * <p>
   * @author czp
   * @time 2007-3-15 ����09:48:32
   */
  public PrayExecVO[] getPrayExecA(
    String pk_corp,
    String pk_calbody,
    String pk_invbasdoc,
    UFDate sdate,
    String vfree1,
    String vfree2,
    String vfree3,
    String vfree4,
    String vfree5)
    throws BusinessException;  
  /**
   * �빺ִ��B��ȡ���������ڿ�ʼ�����Ժ��δ���ɲɹ�������δ��ɵ��빺�����ṩ��������λ��������
   * <p>
   * <b>examples:</b>
   * <p>
   * ʹ��ʾ��
   * <p>
   * <b>����˵��</b>
   * @param pk_corp       ��˾
   * @param pk_calbody    �����֯
   * @param pk_invbasdoc  �������ID
   * @param sdate         ��������
   * @return
   * @throws java.sql.SQLException
   * <p>
   * @author czp
   * @time 2007-3-15 ����09:48:49
   */
  public PrayExecVO[] getPrayExecB(
    String pk_corp,
    String pk_calbody,
    String pk_invbasdoc,
    UFDate sdate)
    throws BusinessException;
  /**
   * �빺ִ��B��ȡ���������ڿ�ʼ�����Ժ��δ���ɲɹ�������δ��ɵ��빺�����ṩ��������λ��������
   * <p>
   * <b>examples:</b>
   * <p>
   * ʹ��ʾ��
   * <p>
   * <b>����˵��</b>
   * @param pk_corp       ��˾
   * @param pk_calbody    �����֯
   * @param pk_invbasdoc  �������ID
   * @param sdate         ��������
   * @param vfree1        ������1
   * @param vfree2        ������2
   * @param vfree3        ������3
   * @param vfree4        ������4
   * @param vfree5        ������5
   * @return
   * @throws java.sql.SQLException
   * <p>
   * @author czp
   * @time 2007-3-15 ����09:48:49
   */
  public PrayExecVO[] getPrayExecB(
    String pk_corp,
    String pk_calbody,
    String pk_invbasdoc,
    UFDate sdate,
    String vfree1,
    String vfree2,
    String vfree3,
    String vfree4,
    String vfree5)
    throws BusinessException;
  /**
   * �ƻ�����������ָ�������Ժ��������δ��ɵĲɹ��������������ر��Լ���������ڶ��������Ķ�������
   * <p>
   * <b>examples:</b>
   * <p>
   * ʹ��ʾ��
   * <p>
   * <b>����˵��</b>
   * @param pk_corp   ��˾����
   * @param storeOrg  �����֯
   * @param baseid    �������ID
   * @param date      ��ʼ����
   * @return
   * @throws java.sql.SQLException
   * <p>
   * @author czp
   * @time 2007-3-15 ����09:55:44
   */
  public PurExecVO[] getPurExec(
    String pk_corp,
    String storeOrg,
    String baseid,
    UFDate date)
    throws BusinessException ;
  /**
   * �ƻ�����������ָ�������Ժ��������δ��ɵĲɹ��������������ر��Լ���������ڶ��������Ķ�������
   * <p>
   * <b>examples:</b>
   * <p>
   * ʹ��ʾ��
   * <p>
   * <b>����˵��</b>
   * @param pk_corp   ��˾����
   * @param storeOrg  �����֯
   * @param baseid    �������ID
   * @param date      ��ʼ����
   * @param vfree1    ������1
   * @param vfree2    ������2
   * @param vfree3    ������3
   * @param vfree4    ������4
   * @param vfree5    ������5
   * @return
   * @throws java.sql.SQLException
   * <p>
   * @author czp
   * @time 2007-3-15 ����09:55:44
   */
  public PurExecVO[] getPurExec(
    String pk_corp,
    String storeOrg,
    String baseid,
    UFDate date,
    String vfree1,
    String vfree2,
    String vfree3,
    String vfree4,
    String vfree5)
    throws BusinessException ;
  /**
   * �����빺����ͷIDs���߱���IDs�����빺����
   * <p>
   * <b>examples:</b>
   * <p>
   * ʹ��ʾ��
   * <p>
   * <b>����˵��</b>
   * @param hIDs      �빺����ͷIDs
   * @param bIDs      �빺������IDs
   * @param sBilltype ��������
   * @param dDate     ����
   * @param sUserId   �û�ID
   * @throws BusinessException
   * <p>
   * @author czp
   * @time 2007-3-15 ����10:00:19
   */
  public void discardPraybillArray(String[] hIDs, String[] bIDs,
      String sBilltype, String dDate, String sUserId)
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
	 * ��Ʊ����ʱ,��дƱ���������ݹ�Ӧ��������
	 * @author MeiChao
	 * @return �ɹ���д������
	 * @param Map����  ���:  key: Ӧ������ID 	value:��Ʊ����
	 * @since 2010-11-11 
	 * @throws Exception һ�������ݿ�����쳣(��������,���ݿ������.)
	 * 
	 */
	public abstract int updateSourceArriveNumToAP(Map bodyPKAndArriveNum) throws Exception;
	
	
	/**
	 * ɾ����Ʊʱ,��дƱ���������ݹ�Ӧ��������
	 * @author MeiChao
	 * @return �ɹ���д������
	 * @param Map����  ���:  key: Ӧ������ID 	value:��Ʊ����
	 * @since 2010-11-11 
	 * @throws Exception һ�������ݿ�����쳣(��������,���ݿ������.)
	 * 
	 */
	public abstract int updateSourceNegativeNumToAP(Map bodyPKAndArriveNum) throws Exception;
	
	/**
	 * �ڴ����ϸ���ڵ��ȷ��֮��,�������Ĵ����ϸ���뵽���ݿ���.
	 * @author MeiChao
	 * @return String[] �ɹ��������PK����
	 * @param InvDetailVO[] �����ϸ����
	 * @since 2010-12-14
	 * @throws BusinessException һ������¶������ݿ�����쳣(��������,���ݿ������.)
	 */
	public abstract String[] insertInvDetail(InvDetailVO[] invDetailVOs) throws BusinessException;
	/**
	 * �ڴ����ϸ���ڵ��ȷ��֮��,���޸ĵĴ����ϸ�־û������ݿ���.
	 * @author MeiChao
	 * @return int �ɹ��־û�������
	 * @param InvDetailVO[] �����ϸ����
	 * @since 2010-12-18
	 * @throws BusinessException һ������¶������ݿ�����쳣(��������,���ݿ������.)
	 */
	public abstract int updateInvDetail(InvDetailVO[] invDetailVOs) throws BusinessException;

	
	
}
