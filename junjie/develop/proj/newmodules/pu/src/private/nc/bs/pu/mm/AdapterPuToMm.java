package nc.bs.pu.mm;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

import javax.naming.NamingException;

import nc.bs.dao.BaseDAO;
import nc.bs.pr.pray.PraybillImpl;
import nc.bs.pu.pr.PraybillDMO;
import nc.bs.pub.SystemException;
import nc.bs.scm.pub.ScmPubDMO;
import nc.itf.pu.mm.ISrvPUToMM;
import nc.vo.pr.pray.PraybillVO;
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
 *  <li>�����������ʵ��ͳһ���
 *  <li>������Ŀ2
 *  <li>...
 * </ul> 
 *
 * <p>
 * <b>�����ʷ����ѡ����</b>
 * <p>
 * 	 V51�汾����V3X��֧�֡�
 * <p>
 * <p>
 * @version V51
 * @since V3X
 * @author czp
 * @time 2007-3-15 ����10:17:23
 */
public class AdapterPuToMm implements ISrvPUToMM{

  /**
   * ���෽����д
   * 
   * @see nc.itf.pu.mm.ISrvPUToMM#discardPraybillArr(java.lang.String[], java.lang.String[], java.lang.String, java.lang.String, java.lang.String)
   */
  public void discardPraybillArray(String[] hIDs, String[] bIDs, String sBilltype, String dDate, String sUserId) throws BusinessException {
    new PraybillImpl().discardPraybillArr(hIDs, bIDs, sBilltype, dDate, sUserId);    
  }

  /**
   * ���෽����д
   * 
   * @see nc.itf.pu.mm.ISrvPUToMM#doSaveSpecially(nc.vo.pr.pray.PraybillVO)
   */
  public ArrayList doSaveSpecially(PraybillVO VO) throws BusinessException {
    return new nc.bs.pu.pr.PraybillImpl().doSaveSpecially(VO);
  }

  /**
   * ���෽����д
   * 
   * @see nc.itf.pu.mm.ISrvPUToMM#doSaveSpeciallyBatch(nc.vo.pr.pray.PraybillVO[])
   */
  public ArrayList doSaveSpeciallyBatch(PraybillVO[] VOs) throws BusinessException {
    return new nc.bs.pu.pr.PraybillImpl().doSaveSpeciallyBatch(VOs);
  }

  /**
   * ���෽����д
   * 
   * @see nc.itf.pu.mm.ISrvPUToMM#getPrayExecA(java.lang.String, java.lang.String, java.lang.String, nc.vo.pub.lang.UFDate)
   */
  public PrayExecVO[] getPrayExecA(String pk_corp, String pk_calbody, String pk_invbasdoc, UFDate sdate) throws BusinessException {
    try {
      return new PraybillDMO().getPrayExecA(pk_corp, pk_calbody, pk_invbasdoc, sdate); 
    }
    catch (SQLException e) {
      throw new BusinessException(e.getMessage());
    }
    catch (NamingException e) {
      throw new BusinessException(e.getMessage());
    }
    catch (SystemException e) {
      throw new BusinessException(e.getMessage());
    }
  }

  /**
   * ���෽����д
   * 
   * @see nc.itf.pu.mm.ISrvPUToMM#getPrayExecB(java.lang.String, java.lang.String, java.lang.String, nc.vo.pub.lang.UFDate)
   */
  public PrayExecVO[] getPrayExecB(String pk_corp, String pk_calbody, String pk_invbasdoc, UFDate sdate) throws BusinessException {
    try {
      return new PraybillDMO().getPrayExecB(pk_corp, pk_calbody, pk_invbasdoc, sdate); 
    }
    catch (SQLException e) {
      throw new BusinessException(e.getMessage());
    }
    catch (NamingException e) {
      throw new BusinessException(e.getMessage());
    }
    catch (SystemException e) {
      throw new BusinessException(e.getMessage());
    }
  }

  /**
   * ���෽����д
   * 
   * @see nc.itf.pu.mm.ISrvPUToMM#getPurExec(java.lang.String, java.lang.String, java.lang.String, nc.vo.pub.lang.UFDate)
   */
  public PurExecVO[] getPurExec(String pk_corp, String storeOrg, String baseid, UFDate date) throws BusinessException {
    try {
      return new PoToMmDMO().getPurExec(pk_corp, storeOrg, baseid, date); 
    }
    catch (SQLException e) {
      throw new BusinessException(e.getMessage());
    }
    catch (NamingException e) {
      throw new BusinessException(e.getMessage());
    }
    catch (SystemException e) {
      throw new BusinessException(e.getMessage());
    }
  }

  /**
   * ���෽����д
   * 
   * @see nc.itf.pu.mm.ISrvPUToMM#getPrayExecA(java.lang.String, java.lang.String, java.lang.String, nc.vo.pub.lang.UFDate, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
   */
  public PrayExecVO[] getPrayExecA(String pk_corp, String pk_calbody, String pk_invbasdoc, UFDate sdate, String vfree1, String vfree2, String vfree3, String vfree4, String vfree5) throws BusinessException {
    try {
      return new PraybillDMO().getPrayExecA(pk_corp, pk_calbody, pk_invbasdoc, sdate, vfree1, vfree2, vfree3, vfree4, vfree5);
    }
    catch (SQLException e) {
      throw new BusinessException(e.getMessage());
    }
    catch (NamingException e) {
      throw new BusinessException(e.getMessage());
    }
    catch (SystemException e) {
      throw new BusinessException(e.getMessage());
    }
  }

  /**
   * ���෽����д
   * 
   * @see nc.itf.pu.mm.ISrvPUToMM#getPrayExecB(java.lang.String, java.lang.String, java.lang.String, nc.vo.pub.lang.UFDate, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
   */
  public PrayExecVO[] getPrayExecB(String pk_corp, String pk_calbody, String pk_invbasdoc, UFDate sdate, String vfree1, String vfree2, String vfree3, String vfree4, String vfree5) throws BusinessException {
    try {
      return new PraybillDMO().getPrayExecB(pk_corp, pk_calbody, pk_invbasdoc, sdate, vfree1, vfree2, vfree3, vfree4, vfree5); 
    }
    catch (SQLException e) {
      throw new BusinessException(e.getMessage());
    }
    catch (NamingException e) {
      throw new BusinessException(e.getMessage());
    }
    catch (SystemException e) {
      throw new BusinessException(e.getMessage());
    }
  }

  /**
   * ���෽����д
   * 
   * @see nc.itf.pu.mm.ISrvPUToMM#getPurExec(java.lang.String, java.lang.String, java.lang.String, nc.vo.pub.lang.UFDate, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
   */
  public PurExecVO[] getPurExec(String pk_corp, String storeOrg, String baseid, UFDate date, String vfree1, String vfree2, String vfree3, String vfree4, String vfree5) throws BusinessException {
    try {
      return new PoToMmDMO().getPurExec(pk_corp, storeOrg, baseid, date, vfree1, vfree2, vfree3, vfree4, vfree5); 
    }
    catch (SQLException e) {
      throw new BusinessException(e.getMessage());
    }
    catch (NamingException e) {
      throw new BusinessException(e.getMessage());
    }
    catch (SystemException e) {
      throw new BusinessException(e.getMessage());
    }
  }
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
	 * @date 2010-8-12 ����10:45:37
	 */
	public  void insertSmartVOs(SmartVO[] vos) throws Exception {		
		PraybillDMO dmo = new PraybillDMO();
        dmo.insertSmartVOs(vos);
	}
	/**
	 * @function ��ѯSmartVO����
	 *
	 * @author QuSida
	 *
	 * @param voClass
	 * @param names
	 * @param where
	 * @return SmartVO[]
	 * @throws Exception 
	 *
	 * @date 2010-9-11 ����02:28:05
	 */
	public SmartVO[] querySmartVOs(Class voClass, String[] names, String where)
			throws Exception {
		PraybillDMO dmo = new PraybillDMO();
		SmartVO[] smartVOs = dmo.querySmartVOs(voClass,names,where);
		return smartVOs;
		
	}
	/**
	 * @function ɾ��SmartVO����
	 *
	 * @author QuSida
	 *
	 * @param vos
	 * @throws Exception 
	 *
	 * @date 2010-9-11 ����02:27:33
	 */
	public void deleteSmartVOs(SmartVO[] vos) throws Exception {
		PraybillDMO dmo = new PraybillDMO();
		dmo.deleteSmartVOs(vos);
	}
	/**
	 * @function �޸�SmartVO����
	 *
	 * @author QuSida
	 *
	 * @param vos
	 * @throws Exception 
	 *
	 * @date 2010-9-11 ����02:27:36
	 */
	public void updateSmartVOs(SmartVO[] vos,String cbillid) throws Exception {
		PraybillDMO dmo = new PraybillDMO();

		dmo.updateSmartVOs(vos,cbillid);
	} 

	/**
	 * ��Ʊ����ʱ,��дƱ���������ݹ�Ӧ��������
	 * @author MeiChao
	 * @return �ɹ���д������
	 * @param Map: key: Ӧ������ID 	value:��Ʊ����
	 * @since 2010-11-11 
	 * @throws Exception һ�������ݿ�����쳣(��������,���ݿ������.)
	 *--�ۼ����������:
	 *update arap_djfb t set t.zyx17=to_char(To_number(case when zyx17 is null then '0' else zyx17 end)+17.55) where t.fb_oid='10022210000000005F7X';
	 *--�жϱ����Ƿ�ȫ�����ɷ�Ʊ��ϲ����±�ͷ��־�����:
	 *update arap_djzb i set i.zyx19 = 'Y' where (select case
         when (select count(*)
                 from arap_djfb t
                where t.vouchid =
                      (select t.vouchid
                         from arap_djfb t
                        where t.fb_oid = '10022210000000005F7X')
                  and t.dfshl > (case when t.zyx17 is null then '0' else t.zyx17 end)) = 0 then  --��������>�����ɷ�Ʊ�����ļ�¼Ϊ0 ��ô��ʾ��ȫ�����ɷ�Ʊ
          'Y'
       end
  from dual)='Y' and i.vouchid=(select t.vouchid
                         from arap_djfb t
                        where t.fb_oid = '10022210000000005F7X');
	 * 
	 */
	public int updateSourceArriveNumToAP(Map bodyPKAndArriveNum) throws Exception{
		BaseDAO dao=new BaseDAO();
		//����Map
		Set keyset=bodyPKAndArriveNum.keySet();
		Object[] trueKeyArray=keyset.toArray();
		//��������
		Integer changeNum=0;
		for(int i=0;i<trueKeyArray.length;i++){
			String apBodyPK=trueKeyArray[i].toString();//Ӧ������id
			String invoiceNumber=bodyPKAndArriveNum.get(apBodyPK).toString();//��Ʊ����
			//�ۼӱ��������ɷ�Ʊ����,����Ӱ�������ۼ�
			changeNum+=dao.executeUpdate("update arap_djfb t set t.zyx17=to_char(To_number(case when zyx17 is null then '0' else zyx17 end)+"+invoiceNumber+") where t.fb_oid='"+apBodyPK+"'");
			String apHeadChangeSQL="update arap_djzb i set i.zyx19 = 'Y' where (select case " +
					"when (select count(*) " +
					"from arap_djfb t " +
					"where t.vouchid = " +
					"(select t.vouchid " +
					"from arap_djfb t " +
					"where t.fb_oid = '"+apBodyPK+"') " +
					"and t.dfshl > (case when t.zyx17 is null then '0' else t.zyx17 end)) = 0 then " +
					"'Y' " +
					"end " +
					"from dual)='Y' and i.vouchid=(select t.vouchid " +
					"from arap_djfb t " +
					"where t.fb_oid = '"+apBodyPK+"') ";
			//ִ������: "�Ƿ������ɷ��÷�Ʊ���"  ��ʶУ��SQL
			dao.executeUpdate(apHeadChangeSQL);
		}
		return changeNum;
	}
	
	/**
	 * ɾ����Ʊʱ,��дƱ���������ݹ�Ӧ��������
	 * @author MeiChao
	 * @return �ɹ���д������
	 * @param Map����  ���:  key: Ӧ������ID 	value:��Ʊ����
	 * @since 2010-11-11 
	 * @throws Exception һ�������ݿ�����쳣(��������,���ݿ������.)
	 * ����:��д��������zyx17�ֶ��е������ɷ�Ʊ����ʱ,��zyx17�ֶ��ÿ�
	 * �����ͷ��zyx19�ֶ�����Nֵ,��ô���ʾ��ǰ�ݹ�Ӧ�������ɹ���Ʊ,����ɾ����Ʊ.
	 */
	public int updateSourceNegativeNumToAP(Map bodyPKAndArriveNum) throws Exception{
	
		BaseDAO dao=new BaseDAO();
		//����Map
		Set keyset=bodyPKAndArriveNum.keySet();
		Object[] trueKeyArray=keyset.toArray();
		//��������
		Integer changeNum=0;
		for(int i=0;i<trueKeyArray.length;i++){
			String apBodyPK=trueKeyArray[i].toString();//Ӧ������id
			String invoiceNumber=bodyPKAndArriveNum.get(apBodyPK).toString();//��Ʊ����
			//�ۼӱ��������ɷ�Ʊ����,����Ӱ�������ۼ�
			changeNum+=dao.executeUpdate("update arap_djfb t set t.zyx17=to_char(case when (To_number(case when zyx17 is null then '0' else zyx17 end)-"+invoiceNumber+")<0 then null end) where t.fb_oid='"+apBodyPK+"';");
			String apHeadChangeSQL="update arap_djzb i set i.zyx19 = 'N' where (select case " +
					"when (select count(*) " +
					"from arap_djfb t " +
					"where t.vouchid = " +
					"(select t.vouchid " +
					"from arap_djfb t " +
					"where t.fb_oid = '"+apBodyPK+"') " +
					"and t.dfshl > (case when t.zyx17 is null then '0' else t.zyx17 end)) > 0 then " +
					"'N' " +
					"end " +
					"from dual)='N' and i.vouchid=(select t.vouchid " +
					"from arap_djfb t " +
					"where t.fb_oid = '"+apBodyPK+"') ";
			//ִ������: "�Ƿ������ɷ��÷�Ʊ���"  ��ʶУ��SQL
			dao.executeUpdate(apHeadChangeSQL);
		}
		return changeNum;
		
	}
}
