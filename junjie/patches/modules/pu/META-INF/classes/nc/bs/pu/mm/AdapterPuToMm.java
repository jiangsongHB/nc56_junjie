package nc.bs.pu.mm;

import java.sql.SQLException;
import java.util.ArrayList;

import javax.naming.NamingException;

import nc.bs.pr.pary.PraybillImpl;
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

}
