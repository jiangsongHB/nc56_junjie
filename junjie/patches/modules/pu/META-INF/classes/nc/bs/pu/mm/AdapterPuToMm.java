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
 * <b>本类主要完成以下功能：</b>
 * 
 * <ul>
 *  <li>给生产制造的实现统一入口
 *  <li>功能条目2
 *  <li>...
 * </ul> 
 *
 * <p>
 * <b>变更历史（可选）：</b>
 * <p>
 * 	 V51版本增加V3X的支持。
 * <p>
 * <p>
 * @version V51
 * @since V3X
 * @author czp
 * @time 2007-3-15 上午10:17:23
 */
public class AdapterPuToMm implements ISrvPUToMM{

  /**
   * 父类方法重写
   * 
   * @see nc.itf.pu.mm.ISrvPUToMM#discardPraybillArr(java.lang.String[], java.lang.String[], java.lang.String, java.lang.String, java.lang.String)
   */
  public void discardPraybillArray(String[] hIDs, String[] bIDs, String sBilltype, String dDate, String sUserId) throws BusinessException {
    new PraybillImpl().discardPraybillArr(hIDs, bIDs, sBilltype, dDate, sUserId);    
  }

  /**
   * 父类方法重写
   * 
   * @see nc.itf.pu.mm.ISrvPUToMM#doSaveSpecially(nc.vo.pr.pray.PraybillVO)
   */
  public ArrayList doSaveSpecially(PraybillVO VO) throws BusinessException {
    return new nc.bs.pu.pr.PraybillImpl().doSaveSpecially(VO);
  }

  /**
   * 父类方法重写
   * 
   * @see nc.itf.pu.mm.ISrvPUToMM#doSaveSpeciallyBatch(nc.vo.pr.pray.PraybillVO[])
   */
  public ArrayList doSaveSpeciallyBatch(PraybillVO[] VOs) throws BusinessException {
    return new nc.bs.pu.pr.PraybillImpl().doSaveSpeciallyBatch(VOs);
  }

  /**
   * 父类方法重写
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
   * 父类方法重写
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
   * 父类方法重写
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
   * 父类方法重写
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
   * 父类方法重写
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
   * 父类方法重写
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
	 * @function  插入SmartVO数组
	 *
	 * @author QuSida
	 *
	 * @param vo
	 * @throws Exception 
	 *
	 * @return void
	 *
	 * @date 2010-8-12 上午10:45:37
	 */
	public  void insertSmartVOs(SmartVO[] vos) throws Exception {		
		PraybillDMO dmo = new PraybillDMO();
        dmo.insertSmartVOs(vos);
	}
	/**
	 * @function 查询SmartVO数组
	 *
	 * @author QuSida
	 *
	 * @param voClass
	 * @param names
	 * @param where
	 * @return SmartVO[]
	 * @throws Exception 
	 *
	 * @date 2010-9-11 下午02:28:05
	 */
	public SmartVO[] querySmartVOs(Class voClass, String[] names, String where)
			throws Exception {
		PraybillDMO dmo = new PraybillDMO();
		SmartVO[] smartVOs = dmo.querySmartVOs(voClass,names,where);
		return smartVOs;
		
	}
	/**
	 * @function 删除SmartVO数组
	 *
	 * @author QuSida
	 *
	 * @param vos
	 * @throws Exception 
	 *
	 * @date 2010-9-11 下午02:27:33
	 */
	public void deleteSmartVOs(SmartVO[] vos) throws Exception {
		PraybillDMO dmo = new PraybillDMO();
		dmo.deleteSmartVOs(vos);
	}
	/**
	 * @function 修改SmartVO数组
	 *
	 * @author QuSida
	 *
	 * @param vos
	 * @throws Exception 
	 *
	 * @date 2010-9-11 下午02:27:36
	 */
	public void updateSmartVOs(SmartVO[] vos,String cbillid) throws Exception {
		PraybillDMO dmo = new PraybillDMO();

		dmo.updateSmartVOs(vos,cbillid);
	}

}
