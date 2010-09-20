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
 * SCM 公共 BO
 * 
 * 可以加入需要后台操作的公共方法。
 * 
 * 作者：顾焱
 * 
 * @version 最后修改日期(2003-9-5 15:53:35)
 * @see 需要参见的其它类
 * @since 从产品的那一个版本，此类被添加进来。（可选） 修改人 + 修改日期 修改说明
 */
public class ScmPubImpl implements IScmPub {
	/**
	 * ScmPubBO 构造子注解。
	 */
	public ScmPubImpl() {
		super();
	}

	/**
	 * 此处插入方法说明。 功能描述: 对查询中的部门,如果是包含下级部门,把下级部门的所有部门加上,中间用or相连接 输入参数: 返回值: 异常处理:
	 * 日期:
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
		ScmPubDMO dmo = new ScmPubDMO();
          dmo.insertSmartVOs(vos);
	}

	/**
	 * 作者：WYF 功能：DMO的对应方法 参数：String[] saBaseId 基本ID[] String]] saAssistUnit
	 * 计量单位ID[] 返回：无 例外：无 日期：(2004-07-07 11:39:21) 修改日期，修改人，修改原因，注释标志：
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
	 * 作者：王印芬 功能：根据表、查询字段、查询条件得到符合条件的结果。 参数： String sTable 表，SQL中FROM后的字符
	 * 不能为空或空串 String sIdName 主键字段名，如"corderid" 不能为空或空串 String[] saFields
	 * 需查询的所有域 不能为空,元素不能为空或空串 String[] saId 需查询的所有ID数组 不能为空,元素不能为空或空串
	 * 返回：Object[][] 可能为空或与saId长度相等。结构如下： 如fields[] =
	 * {"d1","d2","d3"}，则=(56,"dge",2002-03-12) 结果为空表示未有结果存在；元素为空表明该ID对应的值不存在。
	 * 不返回所有元素均为空但返回结果不为空，或结果长度为＜1的情况。 例外：SQLException SQL异常 日期：(2001-08-04
	 * 11:39:21) 修改日期，修改人，修改原因，注释标志： 2002-04-19 wyf 修改返回结果为按ID排列的Object数组
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
	 * 此处插入方法说明。 功能描述: 对查询中的部门,如果是包含下级部门,把下级部门的所有部门加上,中间用or相连接
	 * 公司只能包含单个部门,转换一下调用多公司的函数 输入参数: 返回值: 异常处理: 日期:
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
 * 批量查询是否启用权限，
 * 主要是解决旧版本中多次单个调用的问题
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
	ScmPubDMO dmo = new ScmPubDMO();
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
	ScmPubDMO dmo = new ScmPubDMO();
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
	ScmPubDMO dmo = new ScmPubDMO();

	dmo.updateSmartVOs(vos,cbillid);
}
	
	
}