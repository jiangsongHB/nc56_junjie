package nc.bs.scm.pub;

import java.util.HashMap;
import java.util.Vector;

import nc.bs.framework.common.NCLocator;
import nc.bs.framework.exception.ComponentException;
import nc.itf.scm.pub.IScmPub;
import nc.itf.uap.busibean.IDataPowerService;
import nc.vo.bd.datapower.TableNameConVerter;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.scm.pub.SCMEnv;
import nc.vo.scm.pub.smart.SmartVO;
import nc.vo.uap.busibean.exception.BusiBeanException;


/**
 * SCM ���� BO
 * 
 * ���Լ�����Ҫ��̨�����Ĺ���������
 * 
 * ���ߣ�����
 * 
 * @version ����޸�����(2003-9-5 15:53:35)
 * @see ��Ҫ�μ���������
 * @since �Ӳ�Ʒ����һ���汾�����౻��ӽ���������ѡ�� �޸��� + �޸����� �޸�˵��
 */
public class ScmPubImpl implements IScmPub {
	/**
	 * ScmPubBO ������ע�⡣
	 */
	public ScmPubImpl() {
		super();
	}

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
	public nc.vo.pub.query.ConditionVO[] getTotalSubPkVO(
			nc.vo.pub.query.ConditionVO[] condvo, String[] saPk_corp)
			throws BusinessException {
		nc.vo.pub.query.ConditionVO[] results = null;
		try {
			ScmPubDMO dmo = new ScmPubDMO();
			results = dmo.getTotalSubPkVO(condvo, saPk_corp);
		} catch (Exception e) {

			//reportException(e);
			throw new BusinessException(e);
		}
		return results;
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
		ScmPubDMO dmo = new ScmPubDMO();
          dmo.insertSmartVOs(vos);
	}

	/**
	 * ���ߣ�WYF ���ܣ�DMO�Ķ�Ӧ���� ������String[] saBaseId ����ID[] String]] saAssistUnit
	 * ������λID[] ���أ��� ���⣺�� ���ڣ�(2004-07-07 11:39:21) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 */
	public java.util.HashMap loadBatchInvConvRateInfo(String[] saBaseId,
			String[] saAssistUnit) throws BusinessException {
		try {
			return new ScmPubDMO().loadBatchInvConvRateInfo(saBaseId,
					saAssistUnit);
		} catch (Exception e) {
			//reportException(e);
			throw new BusinessException(e);
		}

	}

	/**
	 * ���ߣ���ӡ�� ���ܣ����ݱ���ѯ�ֶΡ���ѯ�����õ����������Ľ���� ������ String sTable ��SQL��FROM����ַ�
	 * ����Ϊ�ջ�մ� String sIdName �����ֶ�������"corderid" ����Ϊ�ջ�մ� String[] saFields
	 * ���ѯ�������� ����Ϊ��,Ԫ�ز���Ϊ�ջ�մ� String[] saId ���ѯ������ID���� ����Ϊ��,Ԫ�ز���Ϊ�ջ�մ�
	 * ���أ�Object[][] ����Ϊ�ջ���saId������ȡ��ṹ���£� ��fields[] =
	 * {"d1","d2","d3"}����=(56,"dge",2002-03-12) ���Ϊ�ձ�ʾδ�н�����ڣ�Ԫ��Ϊ�ձ�����ID��Ӧ��ֵ�����ڡ�
	 * ����������Ԫ�ؾ�Ϊ�յ����ؽ����Ϊ�գ���������Ϊ��1������� ���⣺SQLException SQL�쳣 ���ڣ�(2001-08-04
	 * 11:39:21) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־�� 2002-04-19 wyf �޸ķ��ؽ��Ϊ��ID���е�Object����
	 */
	public Object[][] queryArrayValue(String sTable, String sIdName,
			String[] saFields, String[] saId) throws BusinessException {
		try {
			return new ScmPubDMO().queryArrayValue(sTable, sIdName, saFields,
					saId, null);
		} catch (Exception e) {
			throw new BusinessException(e);
		}

	}

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
	public nc.vo.pub.query.ConditionVO[] getTotalSubPkVOs(
			nc.vo.pub.query.ConditionVO[] condvo, String saPk_corp)
			throws BusinessException {
		nc.vo.pub.query.ConditionVO[] results = null;
		String[] strsingle;
		strsingle = new String[1];
		strsingle[0] = saPk_corp;
		return getTotalSubPkVO(condvo, strsingle);
	}
	public String queryColumn(String tabName, String fieldName, String whereFilter) throws BusinessException {
		try {
			return new ScmPubDMO().queryColumn(tabName, fieldName, whereFilter);
		} catch (Exception e) {
			throw new BusinessException(e);
		}
		
	}
/**
 * 
 * ������ѯ�Ƿ�����Ȩ�ޣ�
 * ��Ҫ�ǽ���ɰ汾�ж�ε������õ�����
 * 
 * @see nc.itf.scm.pub.IScmPub#queryUsedDataPower(java.util.Vector, java.util.Vector, java.lang.String, java.lang.String)
 */
  public HashMap<String, UFBoolean> queryUsedDataPower(
      Vector<String> vecTableNames, Vector<String> vecRefNames, String pk_corp,String userID) throws BusinessException{
    HashMap<String, UFBoolean> hmUsedData = new HashMap<String, UFBoolean>();
    String strKey ,sTableName, sTableShowName;
    IDataPowerService iIDataPowerService = null;
    try {
      iIDataPowerService = (IDataPowerService) NCLocator
      .getInstance()
      .lookup(IDataPowerService.class.getName());
      for(int index = 0; index < vecTableNames.size(); index ++){
        sTableName = vecTableNames.get(index);
        sTableShowName = TableNameConVerter.convertName(vecRefNames.get(index));
        strKey = sTableName + vecRefNames.get(index) + pk_corp;
        if(iIDataPowerService != null){
          boolean bUsedPower = iIDataPowerService.isUsedDataPower(sTableName, sTableShowName, pk_corp,userID);
          hmUsedData.put(strKey, new UFBoolean(bUsedPower));
        }else{
          hmUsedData.put(strKey, UFBoolean.FALSE);
        }
      }
    } catch (ComponentException e) {
      SCMEnv.out(e);
      throw new BusinessException(e);
    }catch (BusiBeanException be) {
      SCMEnv.out(be);
      throw new BusinessException(be);
    }
    return hmUsedData;
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
	ScmPubDMO dmo = new ScmPubDMO();
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
	ScmPubDMO dmo = new ScmPubDMO();
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
	ScmPubDMO dmo = new ScmPubDMO();

	dmo.updateSmartVOs(vos,cbillid);
}
	
	
}