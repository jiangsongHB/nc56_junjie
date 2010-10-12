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
 * ���ܣ�����DMO
 * 
 * ���Լ�����Ҫ��̨�����Ĺ���������
 * 
 * 
 * ���ߣ�����
 */
public class ScmPubDMO extends nc.bs.pub.DataManageObject {
	public static final String DEPTDOC = "00010000000000000002";

	public static final String AREACL = "00010000000000000012";

	public static final String SALESTRU = "00010000000000000020";// ����������֯v5.02

	// by �ų�
	/**
	 * ScmPubDMO ������ע�⡣
	 * 
	 * @exception javax.naming.NamingException
	 *                �쳣˵����
	 * @exception nc.bs.pub.SystemException
	 *                �쳣˵����
	 */
	public ScmPubDMO() throws javax.naming.NamingException, nc.bs.pub.SystemException {
		super();
	}

	/**
	 * ScmPubDMO ������ע�⡣
	 * 
	 * @param dbName
	 *            java.lang.String
	 * @exception javax.naming.NamingException
	 *                �쳣˵����
	 * @exception nc.bs.pub.SystemException
	 *                �쳣˵����
	 */
	public ScmPubDMO(String dbName) throws javax.naming.NamingException, nc.bs.pub.SystemException {
		super(dbName);
	}

	/**
	 * ���ߣ���ӡ�� ���ܣ��׳�RemoteException��������BO���� ������Exception e �����쳣 ���أ���
	 * ���⣺RemoteException ���ڣ�(2003-03-25 11:39:42) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 */
	public static void throwRemoteException(Exception e) throws java.rmi.RemoteException {
		if (e instanceof RemoteException)
			throw (RemoteException) e;
		else {
			throw new RemoteException("Remote Call", e);
		}
	}

	/**
	 * V5�׳��쳣����1-�����쳣����
	 * <p>
	 * <b>examples:</b>
	 * <p>
	 * ʹ��ʾ��
	 * <p>
	 * <b>����˵��</b>
	 * 
	 * @param e
	 * @throws BusinessException
	 *             <p>
	 * @author czp
	 * @time 2007-3-6 ����11:12:11
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
	 * ���ߣ���ӡ�� ���ܣ����ݴ���ķּ���־����PK���õ�������PK��������PK ������String sPkBdInfo
	 * Ϊ��bd_bdinfo��pk_bdinfo�������º��壺 pk_bdinfo bdcode bdname 00010000000000000002
	 * 2 ���ŵ��� 00010000000000000012 12 �������� String sCurP ��ǰ��PK���統ǰ�ĵ�������ID
	 * String[] saPk_corp �������й�˾ ���أ�String[] ������PK������PK
	 * ���⣺�п��������ڲ�ѯ�¼�����ʱ�׳����쳣��ӦΪϵͳ�쳣 ���ڣ�(2003-4-15 11:39:21) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 */
	private static ArrayList getTotalSubPkAndNames(String sPkBdInfo, String sCurPk,
			String sCurName, String[] saPk_corp) throws Exception {

		ArrayList resultArray = new ArrayList();
		// ������ȷ�Լ��
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

			// ��ԭ�ȵ�ibdaAcc.getChildDocsByHierarchy(sCurPk)��ΪibdaAcc.getChildDocs(sCurPk);
			// ԭ��getChildDocsByHierarchy������ΪҶ�ӽڵ��Ƿǲ�νṹ��
			// �Զ���sCurPk��Ӧ�ĵ����ı���Ϊǰ׺�����ر��������ǰ׺�����е�����Ӧ���Ƿ���null��������ɡ�by zhangcheng
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
	 * ���ߣ���ӡ�� ���ܣ����ݴ���ķּ���־����PK���õ�������PK��������PK ������String sPkBdInfo
	 * Ϊ��bd_bdinfo��pk_bdinfo�������º��壺 pk_bdinfo bdcode bdname 00010000000000000002
	 * 2 ���ŵ��� 00010000000000000012 12 �������� String sCurP ��ǰ��PK���統ǰ�ĵ�������ID
	 * String[] saPk_corp �������й�˾ ���أ�String[] ������PK������PK
	 * ���⣺�п��������ڲ�ѯ�¼�����ʱ�׳����쳣��ӦΪϵͳ�쳣 ���ڣ�(2003-4-15 11:39:21) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 */
	public static String[] getTotalSubPks(String sPkBdInfo, String sCurPk, String[] saPk_corp)
			throws Exception {

		// ������ȷ�Լ��
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
	 * ���ߣ���ά�� ���ܣ��������� �������ID����������λ �Ļ�����Ϣ ����ǵ�һ�Σ����в�ѯ�������Hashtable�л�ȡ��
	 * ��Ϣ���ڹ�ϣ���У���ϣ��ṹ�� KEY: �������ID����������λ���� VALUE: Object[] [0] ������ UFDouble [1]
	 * �Ƿ�̶������� UFBoolean ��������λ�Ļ�����Ϊ1.00��Ϊ�̶������� ������String[] saBaseId ����ID[]
	 * String]] saAssistUnit ������λID[] ���أ��� ���⣺�� ���ڣ�(2003-11-13 11:39:21)
	 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 */
	public HashMap loadBatchInvConvRateInfo(String[] saBaseId, String[] saAssistUnit)
			throws Exception {

		HashMap hInvConvRate = new HashMap();
		// ���������ȷ��
		if (saBaseId == null || saAssistUnit == null || saBaseId.length == 0) {
			return hInvConvRate;
		}

		int iLen = saBaseId.length;
		// ������ʱ��
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
			// ������ʱ��
			String sTempTableName = new nc.bs.scm.pub.TempTableDMO().getTempStringTable(
					nc.vo.scm.pub.TempTableVO.PU_CLMS_PUB01, new String[] { "cbaseid",
							"cassistunit" }, new String[] { "char(20) not null ",
							"char(20) not null " }, null, listTempTableValue);

			// ��ѯ
			Object[][] ob = null;
			ob = queryResultsFromAnyTable(sTempTableName + " JOIN bd_convert ON " + sTempTableName
					+ ".cbaseid=bd_convert.pk_invbasdoc" + " AND pk_measdoc=" + sTempTableName
					+ ".cassistunit", new String[] { "pk_invbasdoc", "pk_measdoc", "mainmeasrate",
					"fixedflag" }, null);
			if (ob != null) {
				// �Ƿ������KEY
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
	 * ���ߣ���ӡ�� ���ܣ����ݱ���ѯ�ֶΡ���ѯ�����õ����������Ľ���� ���صĽ�����ظ� ������ String sTable
	 * ��SQL��FROM����ַ� String[] saFields ���ѯ����SQL��SELECT����ַ� String sWhere
	 * ������SQL��WHERE����ַ�,�ɼ�ORDER BY�� ���أ�Object[][] �ṹ���£� ����=���������
	 * ÿ��[]�е�Ԫ��Ϊ������fieldk��˳�����е��ֶε�ֵ ��fields[] =
	 * {"d1","d2","d3"}����ѯ�������м�¼���򷵻�Object[2][3] [0] ��һ��value(d1,d2,d3) [1]
	 * �ڶ���value(d1,d2,d3) ���⣺SQLException SQL�쳣 ���ڣ�(2001-08-04 11:39:21)
	 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־�� 2003-03-04 wyf ���ֶμ���DISTINCT����
	 */
	public Object[][] queryResultsFromAnyTable(String sTable, String[] saFields, String sWhere)
			throws SQLException {

		// ��������ȷ��
		if (sTable == null || sTable.trim().length() < 1 || saFields == null || saFields.length < 1) {
			System.out
					.println("nc.bs.pu.pub.PubBO.queryResultsFromAnyTable(String, String [], String)�����������");
			return null;
		}
		// Ԫ����ȷ��
		int iLen = saFields.length;
		for (int i = 0; i < iLen; i++) {
			if (saFields[i] == null || saFields[i].trim().length() < 1) {
				System.out
						.println("nc.bs.pu.pub.PubBO.queryResultsFromAnyTable(String, String [], String)�����������");
				return null;
			}
		}
		// ����SQL���
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
			// �رս��������ʱ�ͷ���Դ
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
	 * ���ߣ���ӡ�� ���ܣ����ݱ���ѯ�ֶΡ���ѯ�����õ����������Ľ���� ������ String sTable ��SQL��FROM����ַ�
	 * ����Ϊ�ջ�մ� String sIdName �����ֶ�������"corderid" ����Ϊ�ջ�մ� String[] saFields
	 * ���ѯ�������� ����Ϊ��,Ԫ�ز���Ϊ�ջ�մ� String[] saId ���ѯ������ID���� ����Ϊ��,Ԫ�ز���Ϊ�ջ�մ� String
	 * sWhere ����������������� ����Ϊ�� ���أ�Object[][] ����Ϊ�ջ���saId������ȡ��ṹ���£� ��fields[] =
	 * {"d1","d2","d3"}����=(56,"dge",2002-03-12) ���Ϊ�ձ�ʾδ�н�����ڣ�Ԫ��Ϊ�ձ�����ID��Ӧ��ֵ�����ڡ�
	 * ����������Ԫ�ؾ�Ϊ�յ����ؽ����Ϊ�գ���������Ϊ��1������� ���⣺SQLException SQL�쳣 ���ڣ�(2002-04-22
	 * 11:39:21) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 */
	public Object[][] queryArrayValue(String sTable, String sIdName, String[] saFields,
			String[] saId, String sWhere) throws Exception {

		// ����SQL���
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
			// �رս��������ʱ�ͷ���Դ
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
	 * �˴����뷽��˵���� ��������: �Զ������ŵ�������ɰ��������¼����� �߼��� ���������� 1- ConditionVO�а���
	 * "����"�򡰲��ŵ���������Ҵ���PKֵ ����ֵ: ������VO �쳣����: ����: 2003-09-05 л���� �������:
	 * 
	 * @param condvo
	 *            nc.vo.pub.query.ConditionVO[]
	 * @param String[]
	 *            saPk_corp �������й�˾ �����߼��� 1.�ȷ���������Ҫ������� 2.Ȼ��ÿ����������Ӧ����Ҫ�滻��VO�б�
	 *            3.����滻ԭ����Ӧ����
	 * 
	 * �޸�ʱ�䣺2007-8-21 �޸��ˣ��ų� �޸����ݣ����Ӷ�������֯���ͽṹ��֧��-�Զ���������֯��������ɰ��������¼�������֯
	 */
	public nc.vo.pub.query.ConditionVO[] getTotalSubPkVO(nc.vo.pub.query.ConditionVO[] condvo,
			String[] saPk_corp) throws Exception {
		int lenold;
		int lennew;

		lenold = condvo.length;
		if (lenold <= 0) {
			return condvo;
		}

		// �Ƿ��в�����
		String sCurPk = null;
		boolean flag = false;
		ArrayList rowdept = new ArrayList();
		Integer newint;
		for (int i = 0; i < lenold; i++) {
			if ((condvo[i].getFieldName() != null)
					&& (condvo[i].getFieldName().equals("����")
							|| condvo[i].getFieldName().equals("���ŵ���")
							|| condvo[i].getFieldName().equals("������֯") // v5.02����
					// by
					// zhangcheng
					|| condvo[i].getFieldName().indexOf("��������") >= 0 // v5.02����
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
		// �������������List
		ArrayList arAll = new ArrayList();
		for (int j = 0; j < rowdept.size(); j++) {
			// �������List
			ArrayList arPer = new ArrayList();
			// ȡ�¼����ŵ�ID
			int rowindex = ((Integer) rowdept.get(j)).intValue();
			// sCurPk = condvo[rowindex].getValue();
			//�����ж� �����ָ���쳣
			if(null != condvo[rowindex].getRefResult())
			sCurPk = condvo[rowindex].getRefResult().getRefPK();
			else
			sCurPk = NCLangResOnserver.getInstance().getStrByID("scmpub", "UPPscmpub-000054")/*
					 * @res
					 * "��Ч�����룡"
					 */;

			String sCurName;
			if (condvo[rowindex].getRefResult() != null) // �����жϣ������״� wj
				sCurName = condvo[rowindex].getRefResult().getRefName();
			else
				sCurName = NCLangResOnserver.getInstance().getStrByID("scmpub", "UPPscmpub-000054")/*
																									 * @res
																									 * "��Ч�����룡"
																									 */;
			String sRetName[] = null; // modify by zxj

			// v5.02�޸ģ����Ӷ�������֯�����Ĵ���////////////////////
			try {
				ArrayList retArray = null;
				if (("���ŵ���".equals(condvo[rowindex].getFieldName()))
						|| ("����".equals(condvo[rowindex].getFieldName()))) {
					retArray = getTotalSubPkAndNames(DEPTDOC, sCurPk, sCurName, saPk_corp);
				} else if (("������֯".equals(condvo[rowindex].getFieldName()))) {
					retArray = getTotalSubPkAndNames(SALESTRU, sCurPk, sCurName, saPk_corp);
				} else if (null != condvo[rowindex].getFieldName()&&
                  condvo[rowindex].getFieldName().indexOf("��������") >= 0 ) {
					retArray = getTotalSubPkAndNames(AREACL, sCurPk, sCurName, saPk_corp);
				}
				sRetPk = (String[]) retArray.get(0);
				sRetName = (String[]) retArray.get(1);
			} catch (Exception e) {
				nc.vo.scm.pub.SCMEnv.out(e);
				/** <needn't ??> */
				sRetPk = null;
			}
			// v5.02�޸�////////////////////////////////////////

			// ���ֻ��ԭ��һ��ֵ���򲻽����滻
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

				// ��ϵ��������λor
				vo.setLogic(false);
				// ��һ������������,���һ������������
				if (i == 0) {
					vo.setNoLeft(false);
					// ���ԭ���������ţ�������Ϊand
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
			// ���ԭ����������
			if (!condvo[rowindex].getNoLeft()) {
				vo = new nc.vo.pub.query.ConditionVO();
				// vo.setDataType(condvo[rowindex].getDataType());
				// ������Ϊ����,���������ϳ�Ϊ1=1 ������ַ����ϳ���1='1'���������������ݿ���Ϊ�Ƿ�
				vo.setDataType(1);
				vo.setFieldCode("1");
				vo.setOperaCode("=");
				vo.setValue("1");
				vo.setLogic(condvo[rowindex].getLogic());
				vo.setNoLeft(false);
				arPer.add(0, vo);
			}
			// ���ԭ����������
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

		// �ϳ�����List
		ArrayList arRes = new ArrayList();

		// �ȸ���ԭ�������鵽list
		for (int i = 0; i < lenold; i++) {
			arRes.add(condvo[i]);
		}
		// �Ӻ���ǰ����
		for (int j = rowdept.size() - 1; j >= 0; j--) {
			int rowindex = ((Integer) rowdept.get(j)).intValue();
			ArrayList arPer = (ArrayList) arAll.get(j);
			if (arPer == null) {
				continue;
			}
			arRes.remove(rowindex);
			if (rowindex >= arRes.size()) {
				// �����˳������
				for (int k = 0; k < arPer.size(); k++) {
					arRes.add(arPer.get(k));
				}
			} else {
				// ���Ŵ��м����
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
	 * �����ֿ�����ƥ��Լ�����:�ǹ̶��ʲ����һ���������ʲ���(Ӧ˰����ͼ۸��ۿ����ԵĴ�������)
	 * <p>
	 * <b>examples:</b>
	 * <p>
	 * ʹ��ʾ��
	 * <p>
	 * <b>����˵��</b>
	 * 
	 * @param billVO:���ݾۺ�VO
	 * @param strBillType:��������
	 * @throws BusinessException
	 *             <p>
	 * @author lixiaodong
	 * @time 2007-5-30 ����01:54:12
	 */
	public void checkStorAndInvIsCapital(AggregatedValueObject billVO, String strBillType)
			throws BusinessException {
		try {
			if (billVO == null || PuPubVO.getString_TrimZeroLenAsNull(strBillType) == null) {
				SCMEnv.out("�����������ȷ��ֱ�ӷ���");
				return;
			}

			Vector<String> vctBaseID = new Vector<String>(); // �����������ID
			Vector<String> vctWarehouseID = new Vector<String>(); // �ջ��ֿ�ID
			Object objInvBaseID = null;
			Object objWarehouseID = null;
			if (strBillType == nc.vo.scm.pu.BillTypeConst.PO_ORDER // �ɹ�����
					|| strBillType == nc.vo.scm.pu.BillTypeConst.PO_PRAY // �ɹ��빺��
					|| strBillType == nc.vo.scm.pu.BillTypeConst.PO_ARRIVE) { // �ɹ�������
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
			} else if (strBillType == nc.vo.scm.pu.BillTypeConst.STORE_PO) { // �ɹ���ⵥ
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

			Object[][] oaRetWarehouseID = null;// �Ƿ��ʲ���
			Object[][] oaRetInvBaseID = null;// �Ƿ�̶��ʲ�����

			oaRetWarehouseID = queryArrayValue("bd_stordoc", "pk_stordoc",
					new String[] { "iscapitalstor" }, arrWarehouseID, null);
			oaRetInvBaseID = queryArrayValue("bd_invbasdoc", "pk_invbasdoc", new String[] {
					"pk_assetscategory", "laborflag", "discountflag" }, arrInvBaseID, null);// pk_assetscategory:�̶��ʲ����ID
			// discountflag:�۸��ۿ�
			// laborflag:Ӧ˰����

			if (oaRetWarehouseID == null || oaRetInvBaseID == null || oaRetWarehouseID.length < 1
					|| oaRetInvBaseID.length < 1) {
				return;
			} else {
				for (int i = 0; i < oaRetWarehouseID.length; i++) {
					UFBoolean bRet = new UFBoolean((String) oaRetWarehouseID[i][0]);
					UFBoolean bRet2 = new UFBoolean((String) oaRetInvBaseID[i][0]);
					if ("Y".equalsIgnoreCase(oaRetInvBaseID[i][1].toString())
							&& "n".equalsIgnoreCase(oaRetInvBaseID[i][2].toString()))// Ӧ˰����ͼ۸��ۿ����ԵĴ�������
						if (PuPubVO.getString_TrimZeroLenAsNull(bRet2) != null
								&& !bRet.booleanValue()) {// �ǹ̶��ʲ����(pk_assetscategory��Ϊ��)һ���������ʲ���
							throw new BusinessException("�̶��ʲ�����ֻ�ܶ�Ӧ�̶��ʲ���");
						}
				}
			}
		} catch (Exception e) {
			// ��־�쳣
			nc.vo.scm.pub.SCMEnv.out(e);
			// ���淶�׳��쳣
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
					throw new BusinessException("����������ֵ����ȷ�ԣ�");

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
		// �޴����Ϣ�Ĵ����ID
		ArrayList<String> noInv = new ArrayList<String>();
		// ������������������λ�͸���λ��һ��
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
				// �����۵�λ, ���۵�λ������λһ�� ����������λ������
				if (sTmp == null || sTmp.equals(voInv.getPk_measdoc())) {

					// �Ǹ���λ����
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
					// �Ǹ���λ����,���۵�λ����������λ�����ڴ�������
					if (voInv.getAssistunit() == null || !(voInv.getAssistunit().booleanValue())) {
						errMeas.add(voInv.getInvcode());
						continue;
					}// ����λ�����۵�λһ�£����������λ�ͻ�����
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

		// �쳣����
		if (noInv.size() > 0 || errMeas.size() > 0) {
			String err = "";
			if (noInv.size() > 0) {
				err = "����ID�Ĵ�������ڣ�" + noInv.toString() + "\r\n";
			}
			if (errMeas.size() > 0) {
				err = err + "�Ǹ���λ����Ĵ�������۵�λ�������λһ�£�" + errMeas.toString() + "\r\n";
			}
			throw new BusinessException(err);
		}

	}

	/**
	 * �ӵ��ݻ�ȡ��������������������λ�������ۼ�Ŀ���ѯ���۵�λ���Ӵ��������ѯ����λ�������� if ���۵�λ==����λ ��������=������ if
	 * ���۵�λ==����λ ��������=������ else ���� ���۵�λ������λ �Ļ����� �� ��������=������/������
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

		// δ��ѯ�������Ϣ
		ArrayList<String> noInv = new ArrayList<String>();
		// δ��ѯ�����۵�λ
		ArrayList<String> noMeas = new ArrayList<String>();
		// δ��ѯ��������
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

			// ����λ
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

		// �쳣����
		if (noInv.size() > 0 || noMeas.size() > 0 || noHsl.size() > 0) {
			String err = "";
			if (noInv.size() > 0) {
				err = "����ID�Ĵ�������ڣ�" + noInv.toString() + "\r\n";
			}
			if (noMeas.size() > 0) {
				err = err + "���´�������۵�λ����ȷ(���ۼ�Ŀ������ܴ�������)��" + noMeas.toString() + "\r\n";
			}
			if (noHsl.size() > 0) {
				err = err + "���´��������λ�����۵�λ֮��û�ж��廻���ϵ��" + noHsl.toString() + "\r\n";
			}
			throw new BusinessException(err);
		}

	}

	/**
	 * ���������������õ������е�������ID��˳��Ķ�Ӧ��ϵ��
	 * <p>
	 * <b>examples:</b>
	 * <p>
	 * ʹ��ʾ��
	 * <p>
	 * <b>����˵��</b>
	 * 
	 * @param pk_corp
	 *            ��˾
	 * @return ����HASHMAP������Ϊ��
	 * @throws Exception
	 *             <p>
	 * @author wangyf
	 * @time 2007-6-15 ����11:23:59
	 */
	public static HashMap<String, Integer> getParaFreeAndPosMap(String pk_corp)
			throws BusinessException {
		// TODO �˿��Ծ�̬����һ������
		// U8RM0401
		String[] saParaCode = new String[] { "U8RM0401", "U8RM0402", "U8RM0403", "U8RM0404",
				"U8RM0405", "U8RM0406", "U8RM0407", "U8RM0408", "U8RM0409", "U8RM0410" };
		Hashtable<String, String> htPara = new nc.bs.pub.para.SysInitBO().queryBatchParaValues(
				pk_corp, saParaCode);

		// �õ�һ������ID���ĸ�������ŵ�HASH��
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
	 * ���������ڵ���ʾλ��dispcode
	 * ������
	 * @param nodeCode����ģ����ڵ��
	 * @param pos�����ڵ������λλ�ñ���
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
	 * @function  ����SmartVO����
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
	 * @date 2010-8-12 ����10:43:36
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
	 * @function ��ѯSmartVO����
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
	 * @date 2010-9-11 ����02:24:50
	 */
	public SmartVO[] querySmartVOs(Class voClass, String[] names, String where) throws SystemException, NamingException, SQLException{
		SmartDMO dmo = new SmartDMO();

		SmartVO[] smartVOs = dmo.selectBy(voClass,names,where);
		
		return smartVOs;
	}

	/**
	 * @function ɾ��SmartVO����
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
	 * @date 2010-9-11 ����02:25:17
	 */
	public void deleteSmartVOs(SmartVO[] vos) throws SystemException, NamingException, SQLException{
		SmartDMO dmo = new SmartDMO();
		for (int i = 0; i < vos.length; i++) {
			vos[i].setStatus( VOStatus.DELETED);
		}
		dmo.maintain(vos);
	}
	/**
	 * @function �޸�SmartVO����
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
	 * @date 2010-9-11 ����02:25:30
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