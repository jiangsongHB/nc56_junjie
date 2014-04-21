/**
 * 
 */
package nc.itf.arap.report;

import java.util.HashMap;

import nc.bs.dao.DAOException;
import nc.uif.pub.exception.UifException;
import nc.vo.pub.lang.UFDate;
import nc.vo.scm.pub.smart.SmartVO;

/**
 * @author ausu
 *
 */
public interface IExtInfo {
	public HashMap qryBusiPsnByCust(String pk_org,String ccustbasdoc) throws DAOException, InstantiationException, IllegalAccessException;
	public UFDate qryBusiDatebyCust(String pk_org,String ccustbasdoc) throws  DAOException;
}
