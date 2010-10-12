package nc.bs.scm.pub;

import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Random;
import java.util.Vector;

import javax.naming.NamingException;

import nc.bs.ml.NCLangResOnserver;
import nc.bs.pub.SystemException;
import nc.bs.pub.para.SysInitBO;
import nc.bs.scm.pub.bill.SQLUtil;
import nc.bs.scm.pub.smart.SmartDMO;
import nc.vo.bd.access.AccessorManager;
import nc.vo.bd.access.BddataVO;
import nc.vo.bd.access.IBDAccessor;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.bd.InvVO;
import nc.vo.scm.ic.bill.FreeVO;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.scm.pub.IFreeField;
import nc.vo.scm.pub.IMeasDownConv;
import nc.vo.scm.pub.IMeasField;
import nc.vo.scm.pub.SCMEnv;
import nc.vo.scm.pub.smart.SmartVO;

/**
 * 功能：公共DMO
 * 
 * 可以加入需要后台操作的公共方法。
 * 
 * 
 * 作者：顾焱
 */
public class ScmPubDMO extends nc.bs.pub.DataManageObject {
	public static final String DEPTDOC = "00010000000000000002";

	public static final String AREACL = "00010000000000000012";

	public static final String SALESTRU = "00010000000000000020";// 增加销售组织v5.02

	// by 张成
	/**
	 * ScmPubDMO 构造子注解。
	 * 
	 * @exception javax.naming.NamingException
	 *                异常说明。
	 * @exception nc.bs.pub.SystemException
	 *                异常说明。
	 */
	public ScmPubDMO() throws javax.naming.NamingException, nc.bs.pub.SystemException {
		super();
	}

	/**
	 * ScmPubDMO 构造子注解。
	 * 
	 * @param dbName
	 *            java.lang.String
	 * @exception javax.naming.NamingException
	 *                异常说明。
	 * @exception nc.bs.pub.SystemException
	 *                异常说明。
	 */
	public ScmPubDMO(String dbName) throws javax.naming.NamingException, nc.bs.pub.SystemException {
		super(dbName);
	}

	/**
	 * 作者：王印芬 功能：抛出RemoteException，适用于BO方法 参数：Exception e 具体异常 返回：无
	 * 例外：RemoteException 日期：(2003-03-25 11:39:42) 修改日期，修改人，修改原因，注释标志：
	 */
	public static void throwRemoteException(Exception e) throws java.rmi.RemoteException {
		if (e instanceof RemoteException)
			throw (RemoteException) e;
		else {
			throw new RemoteException("Remote Call", e);
		}
	}

	/**
	 * V5抛出异常工具1-保持异常链。
	 * <p>
	 * <b>examples:</b>
	 * <p>
	 * 使用示例
	 * <p>
	 * <b>参数说明</b>
	 * 
	 * @param e
	 * @throws BusinessException
	 *             <p>
	 * @author czp
	 * @time 2007-3-6 上午11:12:11
	 */
	public static void throwBusinessException(Exception e) throws BusinessException {
		if (e instanceof BusinessException)
			throw (BusinessException) e;
		else {
			SCMEnv.out(e);
			throw new BusinessException(e.getMessage());
		}
	}

	/**
	 * 作者：王印芬 功能：根据传入的分级标志及父PK，得到所有子PK，包括父PK 参数：String sPkBdInfo
	 * 为表bd_bdinfo的pk_bdinfo，有如下含义： pk_bdinfo bdcode bdname 00010000000000000002
	 * 2 部门档案 00010000000000000012 12 地区分类 String sCurP 当前的PK，如当前的地区分类ID
	 * String[] saPk_corp 需查的所有公司 返回：String[] 包括父PK的所有PK
	 * 例外：有可能是由于查询下级分类时抛出的异常，应为系统异常 日期：(2003-4-15 11:39:21) 修改日期，修改人，修改原因，注释标志：
	 */
	private static ArrayList getTotalSubPkAndNames(String sPkBdInfo, String sCurPk,
			String sCurName, String[] saPk_corp) throws Exception {

		ArrayList resultArray = new ArrayList();
		// 参数正确性检查
		if (sPkBdInfo == null) {
			return null;
		}

		int iPk_corpCount = 1;
		if (saPk_corp != null) {
			iPk_corpCount = saPk_corp.length;
		}
		// IBDAccessor[] ibdaAcc = new IBDAccessor[iPk_corpCount] ;
		Vector vecPk = new Vector(10);
		vecPk.addElement(sCurPk);

		Vector vecName = new Vector(10);
		vecName.addElement(sCurName);

		for (int i = 0; i < iPk_corpCount; i++) {
			IBDAccessor ibdaAcc = null;
			if (saPk_corp == null) {
				ibdaAcc = AccessorManager.getAccessor(sPkBdInfo);
			} else {
				ibdaAcc = AccessorManager.getAccessor(sPkBdInfo, saPk_corp[i]);
			}

			// 将原先的ibdaAcc.getChildDocsByHierarchy(sCurPk)改为ibdaAcc.getChildDocs(sCurPk);
			// 原先getChildDocsByHierarchy方法认为叶子节点是非层次结构，
			// 自动以sCurPk对应的档案的编码为前缀，返回编码包含该前缀的所有档案。应该是返回null或自身均可。by zhangcheng
			// 07-9-18
			BddataVO[] voaBdData = ibdaAcc.getChildDocs(sCurPk);
			if (voaBdData != null) {
				int iRetLen = voaBdData.length;
				for (int j = 0; j < iRetLen; j++) {
					vecPk.addElement(voaBdData[j].getPk());
					vecName.addElement(voaBdData[j].getName());
				}
			}
		}

		int iSize = vecPk.size();
		resultArray.add((String[]) (vecPk.toArray(new String[iSize])));
		resultArray.add((String[]) (vecName.toArray(new String[iSize])));
		return resultArray;
	}

	/**
	 * 作者：王印芬 功能：根据传入的分级标志及父PK，得到所有子PK，包括父PK 参数：String sPkBdInfo
	 * 为表bd_bdinfo的pk_bdinfo，有如下含义： pk_bdinfo bdcode bdname 00010000000000000002
	 * 2 部门档案 00010000000000000012 12 地区分类 String sCurP 当前的PK，如当前的地区分类ID
	 * String[] saPk_corp 需查的所有公司 返回：String[] 包括父PK的所有PK
	 * 例外：有可能是由于查询下级分类时抛出的异常，应为系统异常 日期：(2003-4-15 11:39:21) 修改日期，修改人，修改原因，注释标志：
	 */
	public static String[] getTotalSubPks(String sPkBdInfo, String sCurPk, String[] saPk_corp)
			throws Exception {

		// 参数正确性检查
		if (sPkBdInfo == null) {
			return null;
		}

		int iPk_corpCount = 1;
		if (saPk_corp != null) {
			iPk_corpCount = saPk_corp.length;
		}
		// IBDAccessor[] ibdaAcc = new IBDAccessor[iPk_corpCount] ;
		Vector vecPk = new Vector(10);
		vecPk.addElement(sCurPk);

		for (int i = 0; i < iPk_corpCount; i++) {
			IBDAccessor ibdaAcc = null;
			if (saPk_corp == null) {
				ibdaAcc = AccessorManager.getAccessor(sPkBdInfo);
			} else {
				ibdaAcc = AccessorManager.getAccessor(sPkBdInfo, saPk_corp[i]);
			}

			BddataVO[] voaBdData = ibdaAcc.getChildDocs(sCurPk);
			if (voaBdData != null) {
				int iRetLen = voaBdData.length;
				for (int j = 0; j < iRetLen; j++) {
					vecPk.addElement(voaBdData[j].getPk());
				}
			}
		}

		int iSize = vecPk.size();
		return (String[]) (vecPk.toArray(new String[iSize]));
	}

	/**
	 * 作者：汪维敏 功能：批量加载 存货基本ID＋辅计量单位 的换算信息 如果是第一次，进行查询，否则从Hashtable中获取。
	 * 信息存于哈希表中，哈希表结构： KEY: 存货基本ID＋辅计量单位主键 VALUE: Object[] [0] 换算率 UFDouble [1]
	 * 是否固定换算率 UFBoolean 主计量单位的换算率为1.00，为固定换算率 参数：String[] saBaseId 基本ID[]
	 * String]] saAssistUnit 计量单位ID[] 返回：无 例外：无 日期：(2003-11-13 11:39:21)
	 * 修改日期，修改人，修改原因，注释标志：
	 */
	public HashMap loadBatchInvConvRateInfo(String[] saBaseId, String[] saAssistUnit)
			throws Exception {

		HashMap hInvConvRate = new HashMap();
		// 检验参数正确性
		if (saBaseId == null || saAssistUnit == null || saBaseId.length == 0) {
			return hInvConvRate;
		}

		int iLen = saBaseId.length;
		// 创建临时表
		ArrayList listTempTableValue = new ArrayList();
		for (int i = 0; i < iLen; i++) {
			if (PuPubVO.getString_TrimZeroLenAsNull(saBaseId[i]) == null
					|| PuPubVO.getString_TrimZeroLenAsNull(saAssistUnit[i]) == null) {
				continue;
			}
			ArrayList listElement = new ArrayList();
			listElement.add(saBaseId[i]);
			listElement.add(saAssistUnit[i]);

			listTempTableValue.add(listElement);
		}

		try {
			// 创建临时表
			String sTempTableName = new nc.bs.scm.pub.TempTableDMO().getTempStringTable(
					nc.vo.scm.pub.TempTableVO.PU_CLMS_PUB01, new String[] { "cbaseid",
							"cassistunit" }, new String[] { "char(20) not null ",
							"char(20) not null " }, null, listTempTableValue);

			// 查询
			Object[][] ob = null;
			ob = queryResultsFromAnyTable(sTempTableName + " JOIN bd_convert ON " + sTempTableName
					+ ".cbaseid=bd_convert.pk_invbasdoc" + " AND pk_measdoc=" + sTempTableName
					+ ".cassistunit", new String[] { "pk_invbasdoc", "pk_measdoc", "mainmeasrate",
					"fixedflag" }, null);
			if (ob != null) {
				// 是否包含该KEY
				iLen = ob.length;
				for (int i = 0; i < iLen; i++) {
					String sTempKey = (String) ob[i][0] + (String) ob[i][1];
					hInvConvRate.put(sTempKey, new Object[] {
							ob[i][2] == null ? null : new UFDouble(ob[i][2].toString()),
							new UFBoolean(ob[i][3] == null ? "N" : ob[i][3].toString()) });
				}
			}
		} catch (Exception e) {
			reportException(e);
			throw e;
		}
		return hInvConvRate;
	}

	/**
	 * 作者：王印芬 功能：根据表、查询字段、查询条件得到符合条件的结果。 返回的结果不重复 参数： String sTable
	 * 表，SQL中FROM后的字符 String[] saFields 需查询的域，SQL中SELECT后的字符 String sWhere
	 * 条件，SQL中WHERE后的字符,可加ORDER BY等 返回：Object[][] 结构如下： 长度=结果集长度
	 * 每个[]中的元素为按参数fieldk中顺序排列的字段的值 如fields[] =
	 * {"d1","d2","d3"}，查询共有两行记录，则返回Object[2][3] [0] 第一行value(d1,d2,d3) [1]
	 * 第二行value(d1,d2,d3) 例外：SQLException SQL异常 日期：(2001-08-04 11:39:21)
	 * 修改日期，修改人，修改原因，注释标志： 2003-03-04 wyf 对字段加入DISTINCT限制
	 */
	public Object[][] queryResultsFromAnyTable(String sTable, String[] saFields, String sWhere)
			throws SQLException {

		// 检查参数正确性
		if (sTable == null || sTable.trim().length() < 1 || saFields == null || saFields.length < 1) {
			System.out
					.println("nc.bs.pu.pub.PubBO.queryResultsFromAnyTable(String, String [], String)传入参数错误！");
			return null;
		}
		// 元素正确性
		int iLen = saFields.length;
		for (int i = 0; i < iLen; i++) {
			if (saFields[i] == null || saFields[i].trim().length() < 1) {
				System.out
						.println("nc.bs.pu.pub.PubBO.queryResultsFromAnyTable(String, String [], String)传入参数错误！");
				return null;
			}
		}
		// 构造SQL语句
		StringBuffer sbufSql = new StringBuffer("SELECT DISTINCT ");
		sbufSql.append(saFields[0]); // +
		for (int i = 1; i < saFields.length; i++) {
			sbufSql.append(",");
			sbufSql.append(saFields[i]);
		}
		sbufSql.append(" FROM ");
		sbufSql.append(sTable);
		if (PuPubVO.getString_TrimZeroLenAsNull(sWhere) != null) {
			sbufSql.append(" WHERE ");
			sbufSql.append(sWhere);
		}

		Object[][] rets = null;
		Vector vec = new Vector();
		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			con = getConnection();
			stmt = con.createStatement();
			rs = stmt.executeQuery(sbufSql.toString());
			//
			boolean flag = false;
			Object o = null;
			while (rs.next()) {
				Object[] ob = new Object[saFields.length];
				for (int i = 0; i < saFields.length; i++) {
					o = rs.getObject(i + 1);
					if (o != null && o.toString().trim().length() > 0) {
						ob[i] = o;
						flag = true;
					}
				}
				if (flag) {
					vec.addElement(ob);
				}
				flag = false;
			}
		} finally {
			// 关闭结果集，即时释放资源
			try {
				if (rs != null)
					rs.close();
			} catch (Exception e) {
			}
			try {
				if (stmt != null)
					stmt.close();
			} catch (Exception e) {
			}
			try {
				if (con != null)
					con.close();
			} catch (Exception e) {
			}
		}

		if (vec.size() > 0) {
			rets = new Object[vec.size()][];
			for (int i = 0; i < vec.size(); i++) {
				rets[i] = (Object[]) vec.elementAt(i);
			}
		}

		return rets;
	}

	/**
	 * 作者：王印芬 功能：根据表、查询字段、查询条件得到符合条件的结果。 参数： String sTable 表，SQL中FROM后的字符
	 * 不能为空或空串 String sIdName 主键字段名，如"corderid" 不能为空或空串 String[] saFields
	 * 需查询的所有域 不能为空,元素不能为空或空串 String[] saId 需查询的所有ID数组 不能为空,元素不能为空或空串 String
	 * sWhere 除主键外的其他条件 可以为空 返回：Object[][] 可能为空或与saId长度相等。结构如下： 如fields[] =
	 * {"d1","d2","d3"}，则=(56,"dge",2002-03-12) 结果为空表示未有结果存在；元素为空表明该ID对应的值不存在。
	 * 不返回所有元素均为空但返回结果不为空，或结果长度为＜1的情况。 例外：SQLException SQL异常 日期：(2002-04-22
	 * 11:39:21) 修改日期，修改人，修改原因，注释标志：
	 */
	public Object[][] queryArrayValue(String sTable, String sIdName, String[] saFields,
			String[] saId, String sWhere) throws Exception {

		// 构造SQL语句
		StringBuffer sbufSql = new StringBuffer("select ");
		sbufSql.append(sIdName);
		sbufSql.append(",");
		sbufSql.append(saFields[0]);
		int iLen = saFields.length;
		for (int i = 1; i < iLen; i++) {
			sbufSql.append(",");
			sbufSql.append(saFields[i]);
		}
		sbufSql.append(" from ");
		sbufSql.append(sTable);
		sbufSql.append(" where ");
		sbufSql.append(sIdName + " in ");
		String strIdSet = "";
		try {
			nc.bs.scm.pub.TempTableDMO dmoTmpTbl = new nc.bs.scm.pub.TempTableDMO();
			strIdSet = dmoTmpTbl.insertTempTable(saId, nc.vo.scm.pub.TempTableVO.TEMPTABLE_PU14,
					nc.vo.scm.pub.TempTableVO.TEMPPKFIELD_PU);
			if (strIdSet == null || strIdSet.trim().length() == 0)
				strIdSet = "('TempTableDMOError')";
		} catch (Exception e) {
			nc.vo.scm.pub.SCMEnv.out(e);
			throw e;
		}
		sbufSql.append(strIdSet + " ");

		if (sWhere != null && sWhere.trim().length() > 1) {
			sbufSql.append("and ");
			sbufSql.append(sWhere);
		}

		Hashtable hashRet = new Hashtable();
		Vector vec = new Vector();
		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			con = getConnection();
			stmt = con.createStatement();
			rs = stmt.executeQuery(sbufSql.toString());
			//
			while (rs.next()) {
				String sId = rs.getString(1);
				Object[] ob = new Object[saFields.length];
				for (int i = 1; i < saFields.length + 1; i++) {
					ob[i - 1] = rs.getObject(i + 1);
				}
				hashRet.put(sId, ob);
			}
		} finally {
			// 关闭结果集，即时释放资源
			try {
				if (rs != null)
					rs.close();
			} catch (Exception e) {
			}
			try {
				if (stmt != null)
					stmt.close();
			} catch (Exception e) {
			}
			try {
				if (con != null)
					con.close();
			} catch (Exception e) {
			}
		}

		Object[][] oRet = null;
		if (hashRet.size() > 0) {
			iLen = saId.length;
			oRet = new Object[iLen][saFields.length];
			for (int i = 0; i < iLen; i++) {
				oRet[i] = (Object[]) hashRet.get(saId[i]);
			}
		}

		return oRet;
	}

	/**
	 * 此处插入方法说明。 功能描述: 自动将部门的条件变成包含所有下级部门 逻辑： 满足条件： 1- ConditionVO中包含
	 * "部门"或“部门档案”项，而且存在PK值 返回值: 处理后的VO 异常处理: 日期: 2003-09-05 谢高兴 输入参数:
	 * 
	 * @param condvo
	 *            nc.vo.pub.query.ConditionVO[]
	 * @param String[]
	 *            saPk_corp 需查的所有公司 处理逻辑： 1.先返回所有需要处理的项 2.然后将每个项生成相应的需要替换的VO列表
	 *            3.最后替换原来对应的项
	 * 
	 * 修改时间：2007-8-21 修改人：张成 修改内容：增加对销售组织树型结构的支持-自动将销售组织的条件变成包含所有下级销售组织
	 */
	public nc.vo.pub.query.ConditionVO[] getTotalSubPkVO(nc.vo.pub.query.ConditionVO[] condvo,
			String[] saPk_corp) throws Exception {
		int lenold;
		int lennew;

		lenold = condvo.length;
		if (lenold <= 0) {
			return condvo;
		}

		// 是否有部门项
		String sCurPk = null;
		boolean flag = false;
		ArrayList rowdept = new ArrayList();
		Integer newint;
		for (int i = 0; i < lenold; i++) {
			if ((condvo[i].getFieldName() != null)
					&& (condvo[i].getFieldName().equals("部门")
							|| condvo[i].getFieldName().equals("部门档案")
							|| condvo[i].getFieldName().equals("销售组织") // v5.02增加
					// by
					// zhangcheng
					|| condvo[i].getFieldName().indexOf("地区分类") >= 0 // v5.02增加
					// by
					// zhangcheng
					)
					&& (("=".equals(condvo[i].getOperaCode()))
							|| ("like".equals(condvo[i].getOperaCode())) || ("LIKE"
							.equals(condvo[i].getOperaCode())))) {
				sCurPk = condvo[i].getValue();
				if (sCurPk != null && sCurPk.length() > 0) {
					newint = new Integer(i);
					rowdept.add(newint);
					flag = true;
				}
			}
		}

		if (!flag) {
			return condvo;
		}

		String[] sRetPk;
		// 包含所有子项的List
		ArrayList arAll = new ArrayList();
		for (int j = 0; j < rowdept.size(); j++) {
			// 单个项的List
			ArrayList arPer = new ArrayList();
			// 取下级部门的ID
			int rowindex = ((Integer) rowdept.get(j)).intValue();
			// sCurPk = condvo[rowindex].getValue();
			//加入判断 避免空指针异常
			if(null != condvo[rowindex].getRefResult())
			sCurPk = condvo[rowindex].getRefResult().getRefPK();
			else
			sCurPk = NCLangResOnserver.getInstance().getStrByID("scmpub", "UPPscmpub-000054")/*
					 * @res
					 * "无效的输入！"
					 */;

			String sCurName;
			if (condvo[rowindex].getRefResult() != null) // 加入判断，避免抛错 wj
				sCurName = condvo[rowindex].getRefResult().getRefName();
			else
				sCurName = NCLangResOnserver.getInstance().getStrByID("scmpub", "UPPscmpub-000054")/*
																									 * @res
																									 * "无效的输入！"
																									 */;
			String sRetName[] = null; // modify by zxj

			// v5.02修改，增加对销售组织条件的处理////////////////////
			try {
				ArrayList retArray = null;
				if (("部门档案".equals(condvo[rowindex].getFieldName()))
						|| ("部门".equals(condvo[rowindex].getFieldName()))) {
					retArray = getTotalSubPkAndNames(DEPTDOC, sCurPk, sCurName, saPk_corp);
				} else if (("销售组织".equals(condvo[rowindex].getFieldName()))) {
					retArray = getTotalSubPkAndNames(SALESTRU, sCurPk, sCurName, saPk_corp);
				} else if (null != condvo[rowindex].getFieldName()&&
                  condvo[rowindex].getFieldName().indexOf("地区分类") >= 0 ) {
					retArray = getTotalSubPkAndNames(AREACL, sCurPk, sCurName, saPk_corp);
				}
				sRetPk = (String[]) retArray.get(0);
				sRetName = (String[]) retArray.get(1);
			} catch (Exception e) {
				nc.vo.scm.pub.SCMEnv.out(e);
				/** <needn't ??> */
				sRetPk = null;
			}
			// v5.02修改////////////////////////////////////////

			// 如果只有原来一个值，则不进行替换
			if (sRetPk == null || sRetPk.length <= 1) {
				arAll.add(null);
				continue;
			}
			nc.vo.pub.query.ConditionVO vo;
			for (int i = 0; i < sRetPk.length; i++) {
				vo = new nc.vo.pub.query.ConditionVO();
				vo.setDataType(condvo[rowindex].getDataType());
				vo.setDirty(condvo[rowindex].isDirty());
				vo.setFieldCode(condvo[rowindex].getFieldCode());
				vo.setFieldName(condvo[rowindex].getFieldName());
				vo.setOperaCode(condvo[rowindex].getOperaCode());
				vo.setOperaName(condvo[rowindex].getOperaName());
				vo.setTableCode(condvo[rowindex].getTableCodeForMultiTable());
				vo.setTableName(condvo[rowindex].getTableNameForMultiTable());
				vo.setValue(sRetPk[i]);
				// modify by zxj
				nc.vo.pub.query.RefResultVO refresultvo = new nc.vo.pub.query.RefResultVO();
				refresultvo.setRefPK(sRetPk[i]);
				refresultvo.setRefName(sRetName[i]);
				vo.setRefResult(refresultvo);
				//

				// 关系操作设置位or
				vo.setLogic(false);
				// 第一个设置左括号,最后一个设置右括号
				if (i == 0) {
					vo.setNoLeft(false);
					// 如果原来有左括号，则设置为and
					if (!condvo[rowindex].getNoLeft()) {
						vo.setLogic(true);
					} else {
						vo.setLogic(condvo[rowindex].getLogic());
					}
				} else if (i == sRetPk.length - 1) {
					vo.setNoRight(false);
				}
				arPer.add(vo);
			}
			// 如果原来有左括号
			if (!condvo[rowindex].getNoLeft()) {
				vo = new nc.vo.pub.query.ConditionVO();
				// vo.setDataType(condvo[rowindex].getDataType());
				// 都设置为整数,最后的条件合成为1=1 如果是字符串合成是1='1'可能在其他的数据库中为非法
				vo.setDataType(1);
				vo.setFieldCode("1");
				vo.setOperaCode("=");
				vo.setValue("1");
				vo.setLogic(condvo[rowindex].getLogic());
				vo.setNoLeft(false);
				arPer.add(0, vo);
			}
			// 如果原来有右括号
			if (!condvo[rowindex].getNoRight()) {
				vo = new nc.vo.pub.query.ConditionVO();
				// vo.setDataType(condvo[rowindex].getDataType());
				vo.setDataType(1);
				vo.setFieldCode("1");
				vo.setOperaCode("=");
				vo.setValue("1");
				vo.setLogic(true);
				vo.setNoRight(false);
				arPer.add(vo);
			}

			arAll.add(arPer);
		}

		// 合成最后的List
		ArrayList arRes = new ArrayList();

		// 先复制原来的数组到list
		for (int i = 0; i < lenold; i++) {
			arRes.add(condvo[i]);
		}
		// 从后往前复制
		for (int j = rowdept.size() - 1; j >= 0; j--) {
			int rowindex = ((Integer) rowdept.get(j)).intValue();
			ArrayList arPer = (ArrayList) arAll.get(j);
			if (arPer == null) {
				continue;
			}
			arRes.remove(rowindex);
			if (rowindex >= arRes.size()) {
				// 从最后顺序增加
				for (int k = 0; k < arPer.size(); k++) {
					arRes.add(arPer.get(k));
				}
			} else {
				// 反着从中间插入
				for (int k = arPer.size() - 1; k >= 0; k--) {
					arRes.add(rowindex, arPer.get(k));
				}
			}
		}

		int iSize = arRes.size();
		return (nc.vo.pub.query.ConditionVO[]) (arRes
				.toArray(new nc.vo.pub.query.ConditionVO[iSize]));

	}

	/**
	 * 存货与仓库属性匹配约束检查:非固定资产存货一定不能入资产仓(应税劳务和价格折扣属性的存货不检查)
	 * <p>
	 * <b>examples:</b>
	 * <p>
	 * 使用示例
	 * <p>
	 * <b>参数说明</b>
	 * 
	 * @param billVO:单据聚合VO
	 * @param strBillType:单据类型
	 * @throws BusinessException
	 *             <p>
	 * @author lixiaodong
	 * @time 2007-5-30 下午01:54:12
	 */
	public void checkStorAndInvIsCapital(AggregatedValueObject billVO, String strBillType)
			throws BusinessException {
		try {
			if (billVO == null || PuPubVO.getString_TrimZeroLenAsNull(strBillType) == null) {
				SCMEnv.out("传入参数不正确，直接返回");
				return;
			}

			Vector<String> vctBaseID = new Vector<String>(); // 存货基本档案ID
			Vector<String> vctWarehouseID = new Vector<String>(); // 收货仓库ID
			Object objInvBaseID = null;
			Object objWarehouseID = null;
			if (strBillType == nc.vo.scm.pu.BillTypeConst.PO_ORDER // 采购订单
					|| strBillType == nc.vo.scm.pu.BillTypeConst.PO_PRAY // 采购请购单
					|| strBillType == nc.vo.scm.pu.BillTypeConst.PO_ARRIVE) { // 采购到货单
				for (int i = 0; i < billVO.getChildrenVO().length; i++) {
					if (billVO.getChildrenVO()[i] != null) {
						objInvBaseID = billVO.getChildrenVO()[i].getAttributeValue("cbaseid");
						objWarehouseID = billVO.getChildrenVO()[i]
								.getAttributeValue("cwarehouseid");
						if (PuPubVO.getString_TrimZeroLenAsNull(objInvBaseID) != null
								&& PuPubVO.getString_TrimZeroLenAsNull(objWarehouseID) != null) {
							vctBaseID.add(objInvBaseID.toString());
							vctWarehouseID.add(objWarehouseID.toString());
						}
					}
				}
			} else if (strBillType == nc.vo.scm.pu.BillTypeConst.STORE_PO) { // 采购入库单
				for (int i = 0; i < billVO.getChildrenVO().length; i++) {
					if (billVO.getChildrenVO()[i] != null && billVO.getParentVO() != null) {
						objInvBaseID = billVO.getParentVO().getAttributeValue("cinvbasid");
						objWarehouseID = billVO.getChildrenVO()[i]
								.getAttributeValue("cwarehouseid");
						if (PuPubVO.getString_TrimZeroLenAsNull(objInvBaseID) != null
								&& PuPubVO.getString_TrimZeroLenAsNull(objWarehouseID) != null) {
							vctBaseID.add(objInvBaseID.toString());
							vctWarehouseID.add(objWarehouseID.toString());
						}
					}
				}
			}

			if (vctBaseID == null || vctWarehouseID == null || vctBaseID.size() < 1
					|| vctWarehouseID.size() < 1 || vctBaseID.size() != vctWarehouseID.size()) {
				return;
			}
			String[] arrInvBaseID = new String[vctBaseID.size()];
			String[] arrWarehouseID = new String[vctWarehouseID.size()];
			vctBaseID.copyInto(arrInvBaseID);
			vctWarehouseID.copyInto(arrWarehouseID);

			Object[][] oaRetWarehouseID = null;// 是否资产仓
			Object[][] oaRetInvBaseID = null;// 是否固定资产类存货

			oaRetWarehouseID = queryArrayValue("bd_stordoc", "pk_stordoc",
					new String[] { "iscapitalstor" }, arrWarehouseID, null);
			oaRetInvBaseID = queryArrayValue("bd_invbasdoc", "pk_invbasdoc", new String[] {
					"pk_assetscategory", "laborflag", "discountflag" }, arrInvBaseID, null);// pk_assetscategory:固定资产类别ID
			// discountflag:价格折扣
			// laborflag:应税劳务

			if (oaRetWarehouseID == null || oaRetInvBaseID == null || oaRetWarehouseID.length < 1
					|| oaRetInvBaseID.length < 1) {
				return;
			} else {
				for (int i = 0; i < oaRetWarehouseID.length; i++) {
					UFBoolean bRet = new UFBoolean((String) oaRetWarehouseID[i][0]);
					UFBoolean bRet2 = new UFBoolean((String) oaRetInvBaseID[i][0]);
					if ("Y".equalsIgnoreCase(oaRetInvBaseID[i][1].toString())
							&& "n".equalsIgnoreCase(oaRetInvBaseID[i][2].toString()))// 应税劳务和价格折扣属性的存货不检查
						if (PuPubVO.getString_TrimZeroLenAsNull(bRet2) != null
								&& !bRet.booleanValue()) {// 非固定资产存货(pk_assetscategory不为空)一定不能入资产仓
							throw new BusinessException("固定资产类存货只能对应固定资产仓");
						}
				}
			}
		} catch (Exception e) {
			// 日志异常
			nc.vo.scm.pub.SCMEnv.out(e);
			// 按规范抛出异常
			throw new BusinessException(e);
		}

	}

	public static void convertFreeValue(String pk_corp, IFreeField[] voItems)
			throws BusinessException {

		if (voItems == null || voItems.length == 0)
			return;

		HashMap<String, String> hmInv = new HashMap<String, String>();
		for (int i = 0; i < voItems.length; i++) {
			String invid = voItems[i].getInvBasID();
			hmInv.put(invid, invid);

		}

		String[] invids = new String[hmInv.size()];
		hmInv.keySet().toArray(invids);

		String sql = " select pk_invbasdoc,bas.free1,bas.free2,bas.free3,bas.free4,bas.free5 from bd_invbasdoc bas where  coalesce(bas.free1,bas.free2,bas.free3,bas.free4,bas.free5,'ZZ') !='ZZ' ";
		sql = sql + SQLUtil.formInSQL("pk_invbasdoc", invids);

		Object[] values = null;
		try {
			values = new SmartDMO().selectBy2(sql);
		} catch (Exception e) {
			SCMEnv.out(e);
			throw new BusinessException(e.getMessage());
		}

		FreeVO voFree = null;
		HashMap<String, FreeVO> hmFree = new HashMap<String, FreeVO>();
		if (values != null) {
			for (int i = 0; i < values.length; i++) {
				Object[] itemValues = (Object[]) values[i];

				voFree = new FreeVO();
				voFree.setPk_invbasdoc((String) itemValues[0]);
				voFree.setVfreeid1((String) itemValues[1]);
				voFree.setVfreeid2((String) itemValues[2]);
				voFree.setVfreeid3((String) itemValues[3]);
				voFree.setVfreeid4((String) itemValues[4]);
				voFree.setVfreeid5((String) itemValues[5]);

				hmFree.put(voFree.getPk_invbasdoc(), voFree);

			}

		}

		HashMap hmPara = getParaFreeAndPosMap(pk_corp);

		for (int i = 0; i < voItems.length; i++) {
			String invid = voItems[i].getInvBasID();
			Object[] ncvalues = new Object[5];
			Object[] retailvalues = voItems[i].getRetailFreeValue();

			if (hmFree.containsKey(invid)) {
				voFree = (FreeVO) hmFree.get(invid);

				int count = 0;
				for (int j = 0; j < 5; j++) {
					String key = (String) voFree.getAttributeValue("vfreeid"
							+ String.valueOf(j + 1));
					if (key != null)
						count = count + 1;
					if (hmPara.containsKey(key)) {
						ncvalues[j] = retailvalues[((Integer) hmPara.get(key)).intValue() - 1];
					} else
						ncvalues[j] = null;
					if (ncvalues[j] != null)
						count = count - 1;

				}

				voItems[i].setNCFreeValue(ncvalues);

				if (count != 0)
					throw new BusinessException("请检查自由项值的正确性！");

			} else
				voItems[i].setNCFreeValue(null);

		}

	}

	public static void convertRetailMeas(IMeasField[] voItems) throws BusinessException {
		if (voItems == null || voItems.length == 0)
			return;

		java.util.HashSet<String> hm = new java.util.HashSet<String>();
		for (int i = 0; i < voItems.length; i++) {
			hm.add(voItems[i].getInvbas());
		}

		String[] invids = new String[hm.size()];
		hm.toArray(invids);

		HashMap hmData = queryInvVos(invids);

		UFDouble udTmpNum = null;
		String sTmp = null;
		UFDouble one = new UFDouble(1);
		ArrayList alinv = null;
		// 无存货信息的存货的ID
		ArrayList<String> noInv = new ArrayList<String>();
		// 非主辅计量管理，主单位和辅单位不一致
		ArrayList<String> errMeas = new ArrayList<String>();
		for (int i = 0; i < voItems.length; i++) {
			alinv = (ArrayList) hmData.get(voItems[i].getInvbas());
			if (alinv == null || alinv.size() == 0) {
				noInv.add(((InvVO) alinv.get(0)).getInvcode());
				continue;
			}

			udTmpNum = voItems[i].getRetailNum();
			sTmp = voItems[i].getRetailMeas();
			for (int j = 0; j < alinv.size(); j++) {
				InvVO voInv = (InvVO) alinv.get(j);
				// 无零售单位, 零售单位和主单位一致 ，按照主单位、数量
				if (sTmp == null || sTmp.equals(voInv.getPk_measdoc())) {

					// 非辅单位管理
					if (voInv.getAssistunit() == null || !(voInv.getAssistunit().booleanValue())) {
						voItems[i].setMainMeas(voInv.getPk_measdoc());
						voItems[i].setMainNum(udTmpNum);
						break;
					} else {
						voItems[i].setMainMeas(voInv.getPk_measdoc());
						voItems[i].setMainNum(udTmpNum);
						voItems[i].setAstMeas(voInv.getPk_measdoc());
						voItems[i].setAstNum(udTmpNum);
						voItems[i].setHsl(one);

						break;

					}
				} else {
					// 非辅单位管理,零售单位不等于主单位，属于错误数据
					if (voInv.getAssistunit() == null || !(voInv.getAssistunit().booleanValue())) {
						errMeas.add(voInv.getInvcode());
						continue;
					}// 辅单位和零售单位一致，计算出主单位和换算率
					else if (voInv.getCassistunitid().equals(sTmp)) {
						voItems[i].setMainMeas(voInv.getPk_measdoc());
						voItems[i].setMainNum(udTmpNum.multiply(voInv.getMainmeasrate()));
						voItems[i].setAstMeas(sTmp);
						voItems[i].setAstNum(udTmpNum);
						voItems[i].setHsl(voInv.getMainmeasrate());

						break;

					}
				}

			}

		}

		// 异常处理
		if (noInv.size() > 0 || errMeas.size() > 0) {
			String err = "";
			if (noInv.size() > 0) {
				err = "以下ID的存货不存在：" + noInv.toString() + "\r\n";
			}
			if (errMeas.size() > 0) {
				err = err + "非辅单位管理的存货，零售单位须和主单位一致：" + errMeas.toString() + "\r\n";
			}
			throw new BusinessException(err);
		}

	}

	/**
	 * 从单据获取主数量、辅数量、辅单位，从销售价目表查询零售单位，从存货档案查询主单位、换算率 if 零售单位==主单位 零售数量=主数量 if
	 * 零售单位==辅单位 零售数量=辅数量 else 按照 零售单位和主单位 的换算率 ， 零售数量=主数量/换算率
	 * 
	 * @param voItems
	 * @throws BusinessException
	 */
	public static void convertToRetailMeas(IMeasDownConv[] voItems, String pk_corp)
			throws BusinessException {

		if (voItems == null || voItems.length == 0)
			return;

		HashMap hm = new HashMap();
		for (int i = 0; i < voItems.length; i++) {
			hm.put(voItems[i].getInvbas(), voItems[i].getInvbas());

		}

		String[] invids = new String[hm.size()];
		hm.keySet().toArray(invids);

		HashMap hmData = queryInvVos(invids);

		HashMap hmMeas = queryRetailMeas(invids, pk_corp);

		String sMainMeas = null;

		ArrayList alinv = null;
		String sRetailMeas = null;

		// 未查询到存货信息
		ArrayList<String> noInv = new ArrayList<String>();
		// 未查询到零售单位
		ArrayList<String> noMeas = new ArrayList<String>();
		// 未查询到换算率
		ArrayList<String> noHsl = new ArrayList<String>();

		for (int i = 0; i < voItems.length; i++) {

			alinv = (ArrayList) hmData.get(voItems[i].getInvbas());
			if (alinv == null || alinv.size() == 0 || alinv.get(0) == null) {
				noInv.add(voItems[i].getInvbas());
				continue;
			}

			sRetailMeas = (String) hmMeas.get(voItems[i].getInvbas());
			if (sRetailMeas == null) {
				noMeas.add(((InvVO) alinv.get(0)).getInvcode());
				continue;
			}

			// 主单位
			sMainMeas = ((InvVO) alinv.get(0)).getPk_measdoc();

			if (sRetailMeas.equals(sMainMeas)) {

				voItems[i].setRetailNum(voItems[i].getMainNum());
			} else if (sRetailMeas.equals(voItems[i].getAstMeas())) {

				voItems[i].setRetailNum(voItems[i].getAstNum());

			} else {
				boolean isfind = false;
				for (int j = 0; j < alinv.size(); j++) {
					InvVO voInv = (InvVO) alinv.get(j);
					if (voInv.getCassistunitid() != null
							&& voInv.getCassistunitid().equals(sRetailMeas)) {
						voItems[i].setRetailNum(voItems[i].getMainNum()
								.div(voInv.getMainmeasrate()));
						isfind = true;
						break;
					}

				}
				if (!isfind) {
					noHsl.add(((InvVO) alinv.get(0)).getInvcode());
					continue;
				}

			}

			voItems[i].setRetailMeas(sRetailMeas);

		}

		// 异常处理
		if (noInv.size() > 0 || noMeas.size() > 0 || noHsl.size() > 0) {
			String err = "";
			if (noInv.size() > 0) {
				err = "以下ID的存货不存在：" + noInv.toString() + "\r\n";
			}
			if (noMeas.size() > 0) {
				err = err + "以下存货的零售单位不正确(零售价目表定义可能存在问题)：" + noMeas.toString() + "\r\n";
			}
			if (noHsl.size() > 0) {
				err = err + "以下存货的主单位和零售单位之间没有定义换算关系：" + noHsl.toString() + "\r\n";
			}
			throw new BusinessException(err);
		}

	}

	/**
	 * 方法功能描述：得到参数中的自由项ID和顺序的对应关系。
	 * <p>
	 * <b>examples:</b>
	 * <p>
	 * 使用示例
	 * <p>
	 * <b>参数说明</b>
	 * 
	 * @param pk_corp
	 *            公司
	 * @return 返回HASHMAP，不会为空
	 * @throws Exception
	 *             <p>
	 * @author wangyf
	 * @time 2007-6-15 上午11:23:59
	 */
	public static HashMap<String, Integer> getParaFreeAndPosMap(String pk_corp)
			throws BusinessException {
		// TODO 此可以静态保存一个常量
		// U8RM0401
		String[] saParaCode = new String[] { "U8RM0401", "U8RM0402", "U8RM0403", "U8RM0404",
				"U8RM0405", "U8RM0406", "U8RM0407", "U8RM0408", "U8RM0409", "U8RM0410" };
		Hashtable<String, String> htPara = new nc.bs.pub.para.SysInitBO().queryBatchParaValues(
				pk_corp, saParaCode);

		// 得到一自由项ID是哪个自由项，放到HASH中
		String sTempParaValue = null;
		int iFreeLen = saParaCode.length;
		HashMap<String, Integer> hmapParaFreeAndPos = new HashMap<String, Integer>(iFreeLen);
		for (int i = 0; i < iFreeLen; i++) {
			sTempParaValue = htPara.get(saParaCode[i]);
			if (sTempParaValue != null) {
				hmapParaFreeAndPos.put(sTempParaValue, new Integer(i + 1));
			}
		}

		return hmapParaFreeAndPos;

	}

	private static HashMap queryInvVos(String[] invids) throws BusinessException {
		StringBuffer sql = new StringBuffer(
				"select bas.pk_invbasdoc as cinvbasid, bas.invcode ,bas.pk_measdoc as pk_measdoc,assistunit,con.pk_measdoc as cassistunitid ,con.mainmeasrate  ");
		sql.append(" from bd_invbasdoc bas ");
		sql.append("  left outer join bd_convert con on bas.pk_invbasdoc=con.pk_invbasdoc ");
		sql.append(" where 1=1 " + SQLUtil.formInSQL("bas.pk_invbasdoc", invids));

		HashMap hmData = new HashMap();
		ArrayList alinv = null;
		try {
			InvVO[] voInvs = (InvVO[]) new SmartDMO().selectBySql(sql.toString(), InvVO.class);

			if (voInvs != null && voInvs.length > 0) {
				for (int i = 0; i < voInvs.length; i++) {
					if (hmData.containsKey(voInvs[i].getCinvbasid())) {
						alinv = (ArrayList) hmData.get(voInvs[i].getCinvbasid());
					} else {
						alinv = new ArrayList();
						hmData.put(voInvs[i].getCinvbasid(), alinv);

					}

					alinv.add(voInvs[i]);

				}
			}
		} catch (Exception e) {
			throw new BusinessException();

		}
		return hmData;

	}

	private static HashMap queryRetailMeas(String[] invids, String pk_corp)
			throws BusinessException {

		String currency = new SysInitBO().getPkValue(pk_corp, "BD301");
		StringBuffer sql = new StringBuffer(
				"select distinct cinvbasdocid as cinvbasid ,cmeasdocid as pk_measdoc  ");
		sql.append(" from prm_tariffcurlist prm ,  bd_invmandoc man ");
		// sql.append(" left outer join bd_convert con on
		// bas.pk_invbasdoc=con.pk_invbasdoc ");
		sql.append(" where prm.cinventoryid=man.pk_invmandoc and man.pk_corp='" + pk_corp
				+ "' and prm.ccurrencyid='" + currency + "'"
				+ SQLUtil.formInSQL("man.pk_invbasdoc", invids));

		HashMap hmData = new HashMap();
		ArrayList alinv = null;
		try {
			InvVO[] voInvs = (InvVO[]) new SmartDMO().selectBySql(sql.toString(), InvVO.class);

			if (voInvs != null && voInvs.length > 0) {
				for (int i = 0; i < voInvs.length; i++) {

					if (!hmData.containsKey(voInvs[i].getCinvbasid())) {

						hmData.put(voInvs[i].getCinvbasid(), voInvs[i].getPk_measdoc());

					}

				}
			}
		} catch (Exception e) {
			throw new BusinessException();

		}
		return hmData;

	}

	HashMap<String, Integer> indexMap = new HashMap<String, Integer>();
	String rootCode=null;

	/**
	 * 更新新增节点显示位置dispcode
	 * 孔晓东
	 * @param nodeCode新增模块根节点号
	 * @param pos新增节点最后两位位置编码
	 * @throws Exception
	 */
	public void updateNodeDispCode(String nodeCode, String pos) throws Exception {

		int nodeLen = nodeCode.length();
		String superDispCode = null;
		String subDispCode = null;
		String superCode = nodeCode.substring(0, nodeLen - 2);
		String funid = null;
		String sqlfunid = "select cfunid from sm_funcregister where fun_code = '" + nodeCode + "'";
		String sqlDisp = "select disp_code from sm_funcregister where fun_code =?";
		String dispCode = null;
		Connection con = null;
		PreparedStatement stmt = null;
		ArrayList<String> subCodeList = new ArrayList<String>();

		try {
			con = getConnection();
			stmt = con.prepareStatement(sqlfunid.toString());
			ResultSet result = stmt.executeQuery();
			
			if (result.next()) {
				funid = result.getString("cfunid");
				String sqlSubcode = "select fun_code from sm_funcregister where parent_id = '"
						+ funid + "' order by fun_code asc";
				stmt = con.prepareStatement(sqlSubcode.toString());
				result = stmt.executeQuery();
				while (result.next()) {
					subCodeList.add(result.getString("fun_code"));

				}

			}
			
			if(nodeLen>2){
			stmt = con.prepareStatement(sqlDisp.toString());
			stmt.setString(1, superCode);
			result = stmt.executeQuery();
			if (result.next()) {
				dispCode = result.getString("disp_code");
				superDispCode = dispCode;
			}
			stmt.setString(1, nodeCode);
			result = stmt.executeQuery();
			if (result.next()) {
				subDispCode = result.getString("disp_code");

			}
			boolean isrePeated=false;
			if(rootCode==null){
				String doubleDispcodeCheckSql="select disp_code from sm_funcregister where disp_code='"+subDispCode+"'";
				stmt = con.prepareStatement(doubleDispcodeCheckSql);
				result = stmt.executeQuery();
				int i=0;
				while(result.next()){
					i++;
				}
				
				if(i>1){
					isrePeated=true;
				}
				rootCode=nodeCode;
			}
			
			if ((dispCode != null && subDispCode != null
					&& !subDispCode.substring(0, subDispCode.length() - 2).equals(superDispCode))||isrePeated) {

				dispCode = dispCode + pos;

				for (int i = 0; i < 20; i++) {
					String isUsedSql = "select fun_code from sm_funcregister where disp_code = '"
							+ dispCode + "'";
					stmt = con.prepareStatement(isUsedSql);
					result = stmt.executeQuery();
					if (result.next()) {
						dispCode = getIncreaseCode(dispCode);
					} else {
						break;
					}
				}
				String sqlUpdate = "update sm_funcregister set disp_code='" + dispCode
						+ "' where fun_code='" + nodeCode + "'";
				stmt = con.prepareStatement(sqlUpdate.toString());
				stmt.executeUpdate();

			}
			}
			
			

		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
			} catch (Exception e) {
			}
			try {
				if (con != null) {
					con.close();
				}
			} catch (Exception e) {
				SCMEnv.error(e);
				throw new Exception(e);
			}
		}
		int index = 50;
		if(indexMap.get(subDispCode)!=null){
			index=indexMap.get(subDispCode).intValue();
		}
		for (String nodecode : subCodeList) {
			String indexString = String.valueOf(index);
			if (indexString.length() > 2) {
				indexString=String.valueOf(50+new Random().nextInt(50));

			} else if (indexString.length() == 1) {
				indexString = "0" + indexString;
			}
			updateNodeDispCode(nodecode, indexString);

			index += 2;
			indexMap.put(superCode, new Integer(index));
			
		}
		rootCode=null;

	}

	public String getIncreaseCode(String code) throws Exception {
		String indexString = String.valueOf(Integer.parseInt(code.substring(code.length() - 2, code
				.length())) + 1);
		if (indexString.length() > 2) {
		return String.valueOf(50+new Random().nextInt(50));

		} else if (indexString.length() == 1) {
			indexString = "0" + indexString;
		}

		return code.substring(0, code.length() - 2) + indexString;
	}
    
	public String[] querySingleValuesBySql(String sql)throws Exception {
		String[] results=null;
		Connection con = null;
		PreparedStatement stmt = null;
		List<String> resultList=new ArrayList<String>();

		try {
			con = getConnection();
			stmt = con.prepareStatement(sql);
			ResultSet result = stmt.executeQuery();
			if (result.next()) {

				resultList.add(result.getString(1));

			}
			results=resultList.toArray(new String[0]);

		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
			} catch (Exception e) {
			}
			try {
				if (con != null) {
					con.close();
				}
			} catch (Exception e) {
			}
		}
		return results;

	}
		
	
	/**
	 * 
	 */
	public String queryColumn(String tabName, String fieldName, String whereFilter)
			throws Exception {

		String queryColumn = null;
		Connection con = null;
		PreparedStatement stmt = null;
		String sql = "select " + fieldName + " from " + tabName + " where " + whereFilter;

		try {
			con = getConnection();
			stmt = con.prepareStatement(sql);
			ResultSet result = stmt.executeQuery();
			if (result.next()) {

				queryColumn = result.getString(fieldName);

			}

		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
			} catch (Exception e) {
			}
			try {
				if (con != null) {
					con.close();
				}
			} catch (Exception e) {
			}
		}
		return queryColumn;

	}
	/**
	 * @function  插入SmartVO数组
	 *
	 * @author QuSida
	 *
	 * @param vos
	 * @throws SystemException
	 * @throws NamingException
	 * @throws SQLException 
	 *
	 * @return void
	 *
	 * @date 2010-8-12 上午10:43:36
	 */
	public void insertSmartVOs(SmartVO[] vos) throws SystemException, NamingException, SQLException{
		SmartDMO dmo = new SmartDMO();
		for (int i = 0; i < vos.length; i++) {
			vos[i].setPrimaryKey(dmo.getOID());
			vos[i].setAttributeValue("dr", 0);
			vos[i].setStatus(VOStatus.NEW);
		}
		dmo.maintain(vos);
	}
	
	/**
	 * @function 查询SmartVO数组
	 *
	 * @author QuSida
	 *
	 * @param voClass
	 * @param names
	 * @param where
     *
	 * @throws SystemException
	 * @throws NamingException
	 * @throws SQLException 
	 *
	 * @return SmartVO[]
	 *
	 * @date 2010-9-11 下午02:24:50
	 */
	public SmartVO[] querySmartVOs(Class voClass, String[] names, String where) throws SystemException, NamingException, SQLException{
		SmartDMO dmo = new SmartDMO();

		SmartVO[] smartVOs = dmo.selectBy(voClass,names,where);
		
		return smartVOs;
	}

	/**
	 * @function 删除SmartVO数组
	 *
	 * @author QuSida
	 *
	 * @param vos
	 * @throws SystemException
	 * @throws NamingException
	 * @throws SQLException 
	 *
	 * @return void
	 *
	 * @date 2010-9-11 下午02:25:17
	 */
	public void deleteSmartVOs(SmartVO[] vos) throws SystemException, NamingException, SQLException{
		SmartDMO dmo = new SmartDMO();
		for (int i = 0; i < vos.length; i++) {
			vos[i].setStatus( VOStatus.DELETED);
		}
		dmo.maintain(vos);
	}
	/**
	 * @function 修改SmartVO数组
	 *
	 * @author QuSida
	 *
	 * @param vos
	 * @throws SystemException
	 * @throws NamingException
	 * @throws SQLException 
	 *
	 * @return void
	 *
	 * @date 2010-9-11 下午02:25:30
	 */
	public void updateSmartVOs(SmartVO[] vos,String cbillid) throws SystemException, NamingException, SQLException{
		SmartDMO dmo = new SmartDMO();
		String preparedUpdateSql = "delete from jj_scm_informationcost where cbillid = '"+cbillid+"'";
        dmo.executeUpdate(preparedUpdateSql);
        if(vos.length == 0 || vos == null) 
        	return;
         insertSmartVOs(vos);
	}
}