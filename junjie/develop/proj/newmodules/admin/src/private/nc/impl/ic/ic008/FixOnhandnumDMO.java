package nc.impl.ic.ic008;

/**
 * ����˵����
 * �������ڣ�(2003-2-21 9:02:25)
 * @author��������
 * �޸����ڣ�(2003-2-21 9:02:25)
 * �޸��ˣ�@author��������
 * �޸�ԭ��
 * ����˵����
 */
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Vector;

import nc.bs.ic.pub.GenMethod;
import nc.bs.ic.pub.InvATPDMO;
import nc.bs.ic.pub.InvOnHandDMO;
import nc.bs.ic.pub.bill.GeneralSqlString;
import nc.bs.ic.pub.bill.OnhandnumDMO;
import nc.bs.ic.pub.monthsum.MonthServ;
import nc.bs.scm.pub.CustomerConfigImpl;
import nc.itf.ic.service.IICPub_InvATPDMO;
import nc.jdbc.framework.JdbcSession;
import nc.jdbc.framework.PersistenceManager;
import nc.jdbc.framework.crossdb.CrossDBConnection;
import nc.jdbc.framework.util.DBConsts;
import nc.vo.ic.pub.ICConst;
import nc.vo.ic.pub.SmartVOUtilExt;
import nc.vo.ic.pub.bill.GeneralBillItemVO;
import nc.vo.ic.pub.bill.GeneralBillVO;
import nc.vo.ic.pub.bill.IItemKey;
import nc.vo.ic.pub.bill.OnhandnumBBCVO;
import nc.vo.ic.pub.bill.OnhandnumHeaderVO;
import nc.vo.ic.pub.bill.OnhandnumItemVO;
import nc.vo.ic.pub.bill.OnhandnumVO;
import nc.vo.ic.pub.billtype.BillTypeFactory;
import nc.vo.ic.pub.billtype.BookType;
import nc.vo.ic.pub.billtype.IBillType;
import nc.vo.ic.pub.billtype.ModuleCode;
import nc.vo.ic.pub.check.CheckTools;
import nc.vo.ic.pub.monthsum.SqlHelper;
import nc.vo.ic.pub.monthsum.SqlMonthSum;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.query.ConditionVO;
import nc.vo.scm.ic.ATPVO;
import nc.vo.scm.ic.bill.FreeVO;
import nc.vo.scm.pub.SCMEnv;
import nc.vo.scm.pub.SortMethod;

public class FixOnhandnumDMO extends nc.bs.pub.DataManageObject {
	private String m_vfrees = " , vfree1,vfree2,vfree3,vfree4,vfree5 ,vfree6,vfree7,vfree8,vfree9,vfree10 ";

	private String m_sDownDate = null;

	private BigDecimal ZERO = new BigDecimal(0);
	
	private String onhandItemKeySQL = "pk_corp,ccalbodyid,cwarehouseid,cinventoryid,cinvbasid,vlot,castunitid,cvendorid,hsl,vfree1, vfree2, vfree3, vfree4, vfree5,vfree6, vfree7, vfree8, vfree9, vfree10";
	private String onhandItemKeySQL_B = "pk_corp,ccalbodyid,cwarehouseid,cspaceid,cinventoryid,cinvbasid,vlot,castunitid,cvendorid,hsl,vfree1, vfree2, vfree3, vfree4, vfree5,vfree6, vfree7, vfree8, vfree9, vfree10";
	private String onhandDBKeySQL_B = "pk_corp,ccalbodyidb,cwarehouseidb,cspaceid,cinventoryidb,cinvbasid,vlotb,castunitidb,cvendorid,hsl,vfreeb1, vfreeb2, vfreeb3, vfreeb4, vfreeb5,vfreeb6, vfreeb7, vfreeb8, vfreeb9, vfreeb10";
	private String onhandItemNumSQL = "nonhandnum,nonhandastnum,ngrossnum,nnum1,nastnum1,ngrossnum1,nnum2,nastnum2,ngrossnum2 ";
	private String onhandItemNumSQL_B = "nnum,nastnum ";
	private String onhandQryKeyTranSQL = "pk_corp,ccalbodyid,cwarehouseid,cinventoryid,cinvbasid,vbatchcode as vlot,castunitid,cproviderid as cvendorid,hsl,vfree1, vfree2, vfree3, vfree4, vfree5,vfree6, vfree7, vfree8, vfree9, vfree10";
	private String onhandQryKeyTranSQL_B = "pk_corp,ccalbodyid,cwarehouseid,cspaceid,cinventoryid,cinvbasid,vbatchcode as vlot,castunitid,cproviderid as cvendorid,hsl,vfree1, vfree2, vfree3, vfree4, vfree5,vfree6, vfree7, vfree8, vfree9, vfree10";
	private String onhandQryGroupSQL = "pk_corp,ccalbodyid,cwarehouseid,cinventoryid,cinvbasid,vbatchcode,castunitid,cproviderid,hsl,vfree1, vfree2, vfree3, vfree4, vfree5,vfree6, vfree7, vfree8, vfree9, vfree10";
	private String onhandQryGroupSQL_B = "pk_corp,ccalbodyid,cwarehouseid,cspaceid,cinventoryid,cinvbasid,vbatchcode,castunitid,cproviderid,hsl,vfree1, vfree2, vfree3, vfree4, vfree5,vfree6, vfree7, vfree8, vfree9, vfree10";
	private String onhandBBCItemKeySQL = " vbarcode,vbarcodesub,vboxbarcode,cinventoryid,cwarehouseid,pk_corp,ccalbodyid,cinvbasid,vlot,castunitid,hsl,cvendorid,vfree1,vfree2,vfree3,vfree4,vfree5,vfree6,vfree7,vfree8,vfree9,vfree10 ";

	/**
	 * FixOnhandnumDMO ������ע�⡣
	 * 
	 * @exception javax.naming.NamingException
	 *                �쳣˵����
	 * @exception nc.bs.pub.SystemException
	 *                �쳣˵����
	 */
	
	public FixOnhandnumDMO() throws javax.naming.NamingException,
			nc.bs.pub.SystemException {
		super();
	}

	/**
	 * FixOnhandnumDMO ������ע�⡣
	 * 
	 * @param dbName
	 *            java.lang.String
	 * @exception javax.naming.NamingException
	 *                �쳣˵����
	 * @exception nc.bs.pub.SystemException
	 *                �쳣˵����
	 */
	public FixOnhandnumDMO(String dbName) throws javax.naming.NamingException,
			nc.bs.pub.SystemException {
		super(dbName);
	}

	private static boolean DEBUG = false;

	private String getDownLoadDate() {
		if (m_sDownDate == null)
			m_sDownDate = getDowLoadDate();

		return m_sDownDate;

	}

	/**
	 * �õ�����ж��ʱ��
	 * 
	 * @return yyyy-mm
	 */
	public static String getDowLoadDate() {
		// if(true)
		// return null;
		// if (DEBUG)
		// return "2005-10";
		// return null;

		// if (DEBUG) {

		try {

			String downYM = GenMethod.getMaxDownLoadYear(null);

			if (downYM == null) {
				return null;
			}
			if (downYM.trim().length() == 4) {
				return downYM + "-12";
			}
			if (downYM.trim().length() > 4) {
				return downYM;
			}
			return null;
		} catch (Exception e) {
//			e.printStackTrace();
		  SCMEnv.out(e.getMessage());
			return null;
		}
		// }
		// else
		// return null;
	}

	private static Hashtable mergeOnHandItemVOsToHs(ArrayList alVOs)
			throws BusinessException {
		if (alVOs == null || alVOs.size() == 0)
			return null;

		Hashtable retHt = new Hashtable();
		StringBuffer sKey = new StringBuffer();
		Vector vItems = new Vector();
		ArrayList alKeys = new ArrayList();
		String keytemp = null;
		OnhandnumItemVO[] voItemstemp = null;
		String[] keys = OnhandnumHeaderVO.onhandheadkeys;
		// new String[] { "pk_corp", "ccalbodyid", "cwarehouseid",
		// "cinventoryid", "cinvbasid", "vfree1", "vfree2", "vfree3",
		// "vfree4", "vfree5", "vfree6", "vfree7", "vfree8", "vfree9",
		// "vfree10", "vlot", "castunitid", "cvendorid", "hsl" };

		OnhandnumItemVO[] voItems = new OnhandnumItemVO[alVOs.size()];
		alVOs.toArray(voItems);

		if (voItems != null && voItems.length > 0) {
			StringBuffer key = null;
			for (int i = 0; i < voItems.length; i++) {
				key = new StringBuffer();
				for (int j = 0; j < keys.length; j++) {
					key.append(voItems[i].getAttributeValue(keys[j]));
				}
				if (retHt.containsKey(key.toString())) {
					vItems = (Vector) retHt.get(key.toString());
					vItems.add(voItems[i]);
				} else {
					vItems = new Vector();
					vItems.add(voItems[i]);
					alKeys.add(key.toString());
				}
				retHt.put(key.toString(), vItems);
			}
		}

		if (alKeys.size() > 0) {
			for (int i = 0; i < alKeys.size(); i++) {
				keytemp = (String) alKeys.get(i);
				if (keytemp != null) {
					vItems = (Vector) retHt.get(keytemp);
					voItemstemp = new OnhandnumItemVO[vItems.size()];
					vItems.copyInto(voItemstemp);
					retHt.put(keytemp, voItemstemp);
				}
			}
		}

		return retHt;
	}

	private static ArrayList mergeOnHandItemVOs(ArrayList alVOs)
			throws BusinessException {
		if (alVOs == null || alVOs.size() == 0)
			return null;

		OnhandnumItemVO[] voItems = new OnhandnumItemVO[alVOs.size()];
		alVOs.toArray(voItems);

		nc.vo.scm.merge.DefaultVOMerger m = new nc.vo.scm.merge.DefaultVOMerger();
		m.setMergeAttrs(OnhandnumItemVO.onhandbodykeys,
				OnhandnumItemVO.onhandbodynumkeys, null, null, null);
		voItems = (OnhandnumItemVO[]) m.mergeByGroup(voItems);

		ArrayList retlist = new ArrayList();

		if (voItems != null && voItems.length > 0) {
			for (int i = 0; i < voItems.length; i++) {
				retlist.add(voItems[i]);
			}
		}

		return retlist;
	}

	/**
	 * �\n�������ڣ�(2003-2-21 8:52:54) ���ߣ������� �޸����ڣ� �޸��ˣ� �޸�ԭ�� �㷨˵�� ����˵����BO�˵���ڷ�����
	 * 
	 * @param voaCond
	 *            nc.vo.pub.query.ConditionVO
	 */
	public void checkOnhandnum(GeneralBillVO curvo, GeneralBillVO oldvo)
			throws BusinessException {
		if (curvo == null || curvo.getHeaderVO() == null)
			return;

		boolean isOnhandErr = CustomerConfigImpl.isOnhandErr().booleanValue();
		if (!isOnhandErr)
			return;

		String cwarehouseid = curvo.getHeaderVO().getCwarehouseid();
		if (cwarehouseid == null) {
			cwarehouseid = (String) curvo.getHeaderVO().getAttributeValue(
					"cwastewarehouseid");
		}
		HashSet<String> cinvids = new HashSet<String>();
		GeneralBillItemVO[] itemvos = curvo.getItemVOs();
		if (itemvos != null && itemvos.length > 0) {
			for (GeneralBillItemVO itemvo : itemvos) {
				if (cinvids.contains(itemvo.getCinventoryid()))
					continue;
				cinvids.add(itemvo.getCinventoryid());
			}
		}
		String coldwarehouseid = null;
		if (oldvo != null) {
			coldwarehouseid = oldvo.getHeaderVO().getCwarehouseid();
			if (coldwarehouseid == null)
				cwarehouseid = (String) curvo.getHeaderVO().getAttributeValue(
						"cwastewarehouseid");
			itemvos = oldvo.getItemVOs();
			if (itemvos != null && itemvos.length > 0) {
				for (GeneralBillItemVO itemvo : itemvos) {
					if (cinvids.contains(itemvo.getCinventoryid()))
						continue;
					cinvids.add(itemvo.getCinventoryid());
				}
			}
		}
		if (cwarehouseid == null || cinvids.size() <= 0)
			return;
		ConditionVO[] con = new ConditionVO[2];
		con[0] = new ConditionVO();
		con[0].setFieldCode(" ");
		con[0].setDataType(ConditionVO.DECIMAL);
		con[0].setOperaCode(" ");
		con[0].setValue(" cwarehouseid in ('"
				+ cwarehouseid
				+ "' "
				+ (coldwarehouseid != null
						&& !cwarehouseid.equals(coldwarehouseid) ? ",'"
						+ coldwarehouseid + "'" : "") + " ) ");

		con[1] = new ConditionVO();
		con[1].setFieldCode(" ");
		con[1].setDataType(ConditionVO.DECIMAL);
		con[1].setOperaCode("  ");
		con[1].setValue(" 1=1 "
				+ GeneralSqlString.formInSQL("cinventoryid", cinvids
						.toArray(new String[cinvids.size()])));
		//fixOnhandnum(con, true);
	}
	
	
	

	/**
	 * �\n�������ڣ�(2003-2-21 8:52:54) ���ߣ������� �޸����ڣ� �޸��ˣ� �޸�ԭ�� �㷨˵�� ����˵����BO�˵���ڷ�����
	 * @deprecated by ������
	 * @param voaCond
	 *            nc.vo.pub.query.ConditionVO
	 */
	public ArrayList fixOnhandnum_old(ConditionVO[] voaCond)
			throws BusinessException {
		return fixOnhandnum_old(voaCond, false);
	}

	/**
	 * �\n�������ڣ�(2003-2-21 8:52:54) ���ߣ������� �޸����ڣ� �޸��ˣ� �޸�ԭ�� �㷨˵�� ����˵����BO�˵���ڷ�����
	 * @deprecated by ������
	 * @param voaCond
	 *            nc.vo.pub.query.ConditionVO
	 */
	public ArrayList fixOnhandnum_old(ConditionVO[] voaCond, boolean bcheck)
			throws BusinessException {
		ArrayList alRet = null;
		try {

			boolean isOnhandErr = CustomerConfigImpl.isOnhandErr()
					.booleanValue();
			OnhandnumHeaderVO[] voOldHeads = null;
			OnhandnumItemVO[] voOldBodys = null;

			// ���ò����¼,��ȡ�ִ���������,��ȡ��
			if (isOnhandErr || bcheck) {
				String sWhere = getMatchCondition(voaCond);
				voOldHeads = getNegOldHeadVos(sWhere);

				StringBuffer bodywhere = new StringBuffer(
						" pk_onhandnum in (select pk_onhandnum from ic_onhandnum where 1=1 ");

				bodywhere.append(sWhere);
				bodywhere.append(" )");
				voOldBodys = getNegOldBodyVos(bodywhere.toString());

			}

			// ɾ�����ϲ�ѯ��������ic_onhandnum��ic_onhandnum_b�ļ�¼��
			if (!bcheck)
				delMatchRecord(voaCond);

			// �õ�OnhandnumVO��OnhandnumHeaderVO;
			Hashtable htVO = null;

			htVO = queryWithoutSpace(voaCond, null, null);

			if (htVO == null || htVO.size() < 1) {
				alRet = new ArrayList();
				String sMsg = nc.bs.ml.NCLangResOnserver.getInstance()
						.getStrByID("4008busi", "UPP4008busi-000025")/*
																		 * @res
																		 * "��������û���ִ������Ե�����"
																		 */;
				alRet.add(sMsg);
			}
			// �õ�OnhandnumItemVO:
			ArrayList alNew = new ArrayList();
			if (getDowLoadDate() == null) { // ���û������ж��
				ArrayList alNoDownLoad = queryWithSpace_v5(voaCond,
						QueryType.Query_No_Download, null);
				alNew.addAll(alNoDownLoad);

			} else {
				ArrayList alFromIC = queryWithSpace_v5(voaCond,
						QueryType.Query_From_Ic, null);
				ArrayList alFromMonth = queryWithSpace_v5(voaCond,
						QueryType.Query_From_MonthRecord, null);
				alNew.addAll(alFromIC);
				alNew.addAll(alFromMonth);
			}
			// ������־
			OnhandnumItemVO[] voNewBodys = null;
			if (alNew.size() > 0) {
				voNewBodys = new OnhandnumItemVO[alNew.size()];
				alNew.toArray(voNewBodys);
			}
			Hashtable htItemVO = mergeOnHandItemVOsToHs(alNew);

			// ��ItemVO���뵽VO��
			ArrayList alVO = getOnhandnumVO(htVO, htItemVO);
			// �����������ִ�����
			if (!bcheck)
				insert(alVO);

			// �����������,�¾����ݺϲ�
			if (isOnhandErr || bcheck) {
				StringBuilder serr = new StringBuilder(" �ִ������� ");
				boolean berr = false;
				OnhandnumHeaderVO[] voNewHeads = new OnhandnumHeaderVO[htVO
						.size()];

				htVO.values().toArray(voNewHeads);
				ArrayList alAll = new ArrayList();
				if (voNewHeads != null)
					alAll.addAll(new ArrayList(Arrays.asList(voNewHeads)));
				if (voOldHeads != null)
					alAll.addAll(new ArrayList(Arrays.asList(voOldHeads)));
				String cupdateid = null;
				if (alAll.size() > 0) {
					OnhandnumHeaderVO[] vos1 = new OnhandnumHeaderVO[alAll
							.size()];
					alAll.toArray(vos1);
					OnhandnumHeaderVO[] vos = (OnhandnumHeaderVO[]) OnhandnumDMO
							.mergeOnhandVO(vos1);
					if (!bcheck)
						cupdateid = insertErr(vos);
					if (bcheck) {
						for (OnhandnumHeaderVO vo : vos) {
							if (!nc.vo.ic.pub.GenMethod.isEQZeroOrNull(vo
									.getNonhandnum())
									|| !nc.vo.ic.pub.GenMethod
											.isEQZeroOrNull(vo
													.getNonhandastnum())
									|| !nc.vo.ic.pub.GenMethod
											.isEQZeroOrNull(vo.getNnum1())
									|| !nc.vo.ic.pub.GenMethod
											.isEQZeroOrNull(vo.getNastnum1())
									|| !nc.vo.ic.pub.GenMethod
											.isEQZeroOrNull(vo.getNnum2())
									|| !nc.vo.ic.pub.GenMethod
											.isEQZeroOrNull(vo.getNastnum2())) {
								berr = true;
								serr.append("�����������" + vo.getCinventoryid()
										+ ",");
							}

						}
					}

				}

				if (voNewBodys != null || voOldBodys != null) {
					alAll = new ArrayList();
					if (voOldBodys != null)
						alAll.addAll(new ArrayList(Arrays.asList(voOldBodys)));
					if (voNewBodys != null)
						alAll.addAll(new ArrayList(Arrays.asList(voNewBodys)));

					if (alAll.size() > 0) {
						OnhandnumItemVO[] vos1 = new OnhandnumItemVO[alAll
								.size()];
						alAll.toArray(vos1);
						OnhandnumItemVO[] vos = (OnhandnumItemVO[]) OnhandnumDMO
								.mergeOnhandVO(vos1);
						if (!bcheck)
							insertErrB(vos, cupdateid);
						if (bcheck) {
							for (OnhandnumItemVO vo : vos) {
								if (!nc.vo.ic.pub.GenMethod.isEQZeroOrNull(vo
										.getNnum())
										|| !nc.vo.ic.pub.GenMethod
												.isEQZeroOrNull(vo.getNastnum())) {
									berr = true;
									serr.append("��λ������" + vo.getCspaceid()
											+ ",");
									serr.append("�����������"
											+ vo.getCinventoryid() + ",");
								}

							}
						}
					}

				}
				if (bcheck && berr)
					throw new BusinessException(serr.toString());
			}

		} catch (Exception e) {
			if (e instanceof BusinessException)
				throw (BusinessException) e;
			else
				throw new BusinessException("Caused by:", e);
		}
		return alRet;
	}
	
	/**
	 * �����ˣ������� ����ʱ�䣺2009-8-13 ����06:33:31 ����ԭ��
	 * �����ִ������������ִ�һ�㶼�Ƚϴ����԰��޸����ִ�������ȫ�����ΪVO�����׷����ڴ������
	 * ��ʹ�����ִ��������㷨���ִ��������¼�Ѿ�����ȥ�������Ҵ˹����Ƿ�ʹ���������ļ�NC_Home\modules\scmpub\scmatp\atpconfig.xml��������isonhanderr���ƣ�����ͬʱȥ���˹���ͬʱɾ�������
	 * �ִ������������ڼ�鸺��棨���������������������һ�£�ʱ�����ģ���ʹ�����ִ��������㷨��Ҳȥ���˹��ܺ��ڼ�鸺���ʱ������ȥ����ִ������죬��߲���Ч�ʡ�
	 * ԭ�и���ʱ���ִ���VO�Ȱ��ִ���ά�ȸ����ִ������ʱû�ж�Ӧ��¼�������룬��Ϊ�ִ�������һ��ʼ�ͰѶ�Ӧ�������ִ���ɾ���ˣ�����û�б�Ҫ�Ƚ��и��£�ֱ��������������С�
	 * ���µĴ����У�����ά�Ƚ��з��鴦��ִ��SQL���ɽ����ֱ�Ӳ��뵽�ִ������С�
	 * 
	 * @param voaCond
	 * @param bcheck
	 * @return
	 * @throws BusinessException
	 */
	public ArrayList fixOnhandnum(ConditionVO[] voaCond)
	throws BusinessException {
		ArrayList alRet = null;
		try {
			PersistenceManager persist = PersistenceManager.getInstance();
			persist.setSQLTranslator(true);
			JdbcSession session = persist.getJdbcSession();

			// ɾ�����ϲ�ѯ��������ic_onhandnum��ic_onhandnum_b�ļ�¼��
			delMatchRecord(voaCond);

			session = persist.getJdbcSession();
			// ִ���ִ�������
			int rows = session.executeUpdate(getOnhandNumFixSQL(voaCond,
					persist.getDBType()));
			// ִ�л�λ�ִ�������
			int rowsb = session.executeUpdate(getSpaceOnhandNumFixSQL(voaCond,
					persist.getDBType()));
			
			//begin ncm fansj1_���ܼ���_�ִ����޸�����Υ��Ψһ��Լ��201108251245591625
			// added by lirr 2010-1-15����05:33:17 ��½��Ŀ���⣬��ǰ���㷨 �������λ��������Ϊ0 ������û��¼ �ֱ��м�¼���������ӱ����Ϊ�յļ�¼
		      String compareSQL = "select pk_onhandnum from ic_onhandnum where temp.cinventoryidb = ic_onhandnum.cinventoryid AND temp.cwarehouseidb = ic_onhandnum.cwarehouseid AND temp.ccalbodyidb=ic_onhandnum.ccalbodyid AND (temp.pk_corp=ic_onhandnum.pk_corp) AND (temp.cvendorid=ic_onhandnum.cvendorid or temp.cvendorid is null and ic_onhandnum.cvendorid is null) AND (temp.hsl=ic_onhandnum.hsl or temp.hsl is null and ic_onhandnum.hsl is null) AND (temp.vlotb=ic_onhandnum.vlot or temp.vlotb is null and ic_onhandnum.vlot is null) AND (temp.castunitidb=ic_onhandnum.castunitid or temp.castunitidb is null and ic_onhandnum.castunitid is null) AND (temp.vfreeb1 = ic_onhandnum.vfree1 or temp.vfreeb1 is null and ic_onhandnum.vfree1 is null)AND (temp.vfreeb2 = ic_onhandnum.vfree2 or temp.vfreeb2 is null and ic_onhandnum.vfree2 is null)AND (temp.vfreeb3 = ic_onhandnum.vfree3 or temp.vfreeb3 is null and ic_onhandnum.vfree3 is null)AND (temp.vfreeb4 = ic_onhandnum.vfree4 or temp.vfreeb4 is null and ic_onhandnum.vfree4 is null)AND (temp.vfreeb5 = ic_onhandnum.vfree5 or temp.vfreeb5 is null and ic_onhandnum.vfree5 is null)AND (temp.vfreeb6 = ic_onhandnum.vfree6 or temp.vfreeb6 is null and ic_onhandnum.vfree6 is null)AND (temp.vfreeb7 = ic_onhandnum.vfree7 or temp.vfreeb7 is null and ic_onhandnum.vfree7 is null)AND (temp.vfreeb8 = ic_onhandnum.vfree8 or temp.vfreeb8 is null and ic_onhandnum.vfree8 is null)AND (temp.vfreeb9 = ic_onhandnum.vfree9 or temp.vfreeb9 is null and ic_onhandnum.vfree9 is null)AND (temp.vfreeb10 = ic_onhandnum.vfree10 or temp.vfreeb10 is null and ic_onhandnum.vfree10 is null)";
		      
		      String onhandDBKeyTOHSQL_B = "pk_corp,ccalbodyidb,cwarehouseidb,cinventoryidb,cinvbasid,vlotb,castunitidb,cvendorid,hsl,vfreeb1, vfreeb2, vfreeb3, vfreeb4, vfreeb5,vfreeb6, vfreeb7, vfreeb8, vfreeb9, vfreeb10";
		      
		      StringBuilder sqlFillInsert = new StringBuilder();
					sqlFillInsert.append("insert into ic_onhandnum (pk_onhandnum,");
					sqlFillInsert.append(onhandItemKeySQL);
					sqlFillInsert.append(") select "
					         + GenMethod.getPrimaryKeySQLByDBType(persist.getDBType(), "OF",onhandDBKeyTOHSQL_B)
					         + " as pk_onhandnum," + onhandDBKeyTOHSQL_B );
					sqlFillInsert.append(" from (select "+ onhandDBKeyTOHSQL_B +" from (select "+ onhandDBKeyTOHSQL_B + ",sum(isnull(nnum,0)) as nsumnum,sum(isnull(nastnum,0)) as nsumastnum from ic_onhandnum_b where pk_onhandnum ='"
		      + nc.vo.ic.pub.GenMethod.STRING_NULL + "' group by "+onhandDBKeyTOHSQL_B+" ) temp where temp.nsumnum = 0 and temp.nsumastnum = 0 and not exists(" +compareSQL
		      		+") ) tbl");//���ӱ��� ����SQLSever���� ԭ��֪ ������ 2010-10-23
					
				// ��ȫ��λ�ִ������ִ���PK
		      int rowufix = session.executeUpdate(sqlFillInsert.toString());
		      //end ncm fansj1_���ܼ���_�ִ����޸�����Υ��Ψһ��Լ��201108251245591625
		      
			
			// ��ȫ��λ�ִ������ִ���PK
			int rowub = session.executeUpdate(OnhandnumDMO.getSqlUpdatePK());

			if (rows <= 0) {
				alRet = new ArrayList();
				String sMsg = nc.bs.ml.NCLangResOnserver.getInstance()
						.getStrByID("4008busi", "UPP4008busi-000025")/*
																		 * @res
																		 * "��������û���ִ������Ե�����"
																		 */;
				alRet.add(sMsg);
			}

		} catch (Exception e) {
			if (e instanceof BusinessException)
				throw (BusinessException) e;
			else
				throw new BusinessException("Caused by:", e);
		}
		return alRet;
	}
	
	/**
	 * �����ˣ������� ����ʱ�䣺2009-8-14 ����01:21:28 ����ԭ�򣺷����ִ�������SQL
	 * 
	 * @param voaCond
	 * @param dBType
	 * @return
	 * @throws Exception
	 */
	private String getOnhandNumFixSQL(ConditionVO[] voaCond, int dBType)
			throws Exception {
		StringBuilder onhandNumFixSQL = new StringBuilder();
		onhandNumFixSQL.append("insert into ic_onhandnum (pk_onhandnum,");
		onhandNumFixSQL.append(onhandItemKeySQL);
		onhandNumFixSQL.append("," + onhandItemNumSQL);
		onhandNumFixSQL.append(" ) ");
		onhandNumFixSQL.append("select "
				+ GenMethod.getPrimaryKeySQLByDBType(dBType, "OH",onhandItemKeySQL)
				+ " as pk_onhandnum,");
		onhandNumFixSQL.append(onhandItemKeySQL);
		onhandNumFixSQL.append("," + onhandItemNumSQL + " from ( ");
		onhandNumFixSQL.append(" select " + onhandItemKeySQL);
		onhandNumFixSQL
				.append(",(case when sum(nonhandnum) = 0 then null else sum(nonhandnum) end) as nonhandnum,(case when sum(nonhandastnum) = 0 then null else sum(nonhandastnum) end) as nonhandastnum,(case when sum(ngrossnum) = 0 then null else sum(ngrossnum) end) as ngrossnum");
		onhandNumFixSQL
				.append(",(case when sum(nnum1) = 0 then null else sum(nnum1) end) as nnum1,(case when sum(nastnum1) = 0 then null else sum(nastnum1) end) as nastnum1,(case when sum(ngrossnum1) = 0 then null else sum(ngrossnum1) end) as ngrossnum1");
		onhandNumFixSQL
				.append(",(case when sum(nnum2) = 0 then null else sum(nnum2) end) as nnum2,(case when sum(nastnum2) = 0 then null else sum(nastnum2) end) as nastnum2,(case when sum(ngrossnum2) = 0 then null else sum(ngrossnum2) end) as ngrossnum2 ");
		onhandNumFixSQL.append(" from ( ");
		onhandNumFixSQL.append(getQueryWithoutSpaceSQL(voaCond));
		onhandNumFixSQL.append(" ) temponhand ");
		onhandNumFixSQL.append(" group by " + onhandItemKeySQL);
		onhandNumFixSQL.append(" ) tempfinal");
		return onhandNumFixSQL.toString();
	}
	
	/**
	 * �����ˣ������� ����ʱ�䣺2009-8-14 ����01:21:56 ����ԭ�򣺷��ػ�λ�ִ�������SQL
	 * 
	 * @param voaCond
	 * @param dBType
	 * @return
	 * @throws Exception
	 */
	private String getSpaceOnhandNumFixSQL(ConditionVO[] voaCond, int dBType)
			throws Exception {
		StringBuilder spaceOnhandNumFixSQL = new StringBuilder();
		spaceOnhandNumFixSQL
				.append("insert into ic_onhandnum_b (pk_onhandnum_b,pk_onhandnum,");
		spaceOnhandNumFixSQL.append(onhandDBKeySQL_B);
		spaceOnhandNumFixSQL.append("," + onhandItemNumSQL_B);
		spaceOnhandNumFixSQL.append(" ) ");
		spaceOnhandNumFixSQL.append("select "
				+ GenMethod.getPrimaryKeySQLByDBType(dBType, "OB",onhandItemKeySQL_B)
				+ " as pk_onhandnum_b,");
		spaceOnhandNumFixSQL.append("'" + nc.vo.ic.pub.GenMethod.STRING_NULL
				+ "',");
		spaceOnhandNumFixSQL.append(onhandItemKeySQL_B);
		spaceOnhandNumFixSQL.append("," + onhandItemNumSQL_B + " from ( ");
		if (getDowLoadDate() == null) { // ���û������ж��
			spaceOnhandNumFixSQL.append("select " + onhandQryKeyTranSQL_B);
			spaceOnhandNumFixSQL
					.append(",nspacenum as nnum,nspaceassistnum as nastnum from (");
			spaceOnhandNumFixSQL.append(getSqlWithSpace(voaCond,
					QueryType.Query_No_Download));
			spaceOnhandNumFixSQL.append(") tempnospace where nspacenum > 0 or nspacenum < 0 or nspaceassistnum > 0 or nspaceassistnum < 0 ");
			
		} else {
			spaceOnhandNumFixSQL.append("select " + onhandQryKeyTranSQL_B);
			spaceOnhandNumFixSQL
					.append(",sum(nspacenum) as nnum,sum(nspaceassistnum) as nastnum from (");
			spaceOnhandNumFixSQL.append(getSqlWithSpace(voaCond,
					QueryType.Query_From_Ic));
			spaceOnhandNumFixSQL.append(" union all ");
			spaceOnhandNumFixSQL.append(getSqlWithSpace(voaCond,
					QueryType.Query_From_MonthRecord));
			spaceOnhandNumFixSQL.append(") tempnospace ");
			spaceOnhandNumFixSQL.append(" group by ");
			spaceOnhandNumFixSQL.append(onhandQryGroupSQL_B);
			spaceOnhandNumFixSQL
					.append(" having ( sum(nspacenum) > 0 or sum(nspaceassistnum) > 0 or sum(nspacenum) < 0 or sum(nspaceassistnum) < 0 ) ");
		}
		spaceOnhandNumFixSQL.append(" ) tempfinal");
		return spaceOnhandNumFixSQL.toString();
	}


	/**
	 * �����ݿ����һ��VO����
	 * 
	 * �������ڣ�(2002-12-23)
	 * 
	 * @param node
	 *            nc.vo.ic.ic2a3.AccountctrlHeaderVO
	 * @exception java.sql.SQLException
	 *                �쳣˵����
	 */
	private String insertErr(OnhandnumHeaderVO[] headVOs)
			throws BusinessException {

		if (headVOs == null || headVOs.length == 0)
			return null;

		String sqlInsertH = "insert into ic_onhanderr(conhanderrid, cupdateid,pk_corp, ccalbodyid, cwarehouseid, cinventoryid, vlot, cvendorid, castunitid, vfree10, vfree9, vfree8, vfree7, vfree6, vfree5, vfree4, vfree3, vfree2, vfree1, nonhandnum, nonhandastnum,nnum1,nastnum1,nnum2,nastnum2,hsl,ngrossnum) values(?,?,?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?,?)";

		CrossDBConnection con = null;
		PreparedStatement stmt = null;
		String key = null;
		String[] keys = getOIDs(headVOs.length + 1);
		String cupdateid = keys[headVOs.length];
		try {
			con = (CrossDBConnection) getConnection();
			stmt = prepareStatement(con, sqlInsertH);

			OnhandnumHeaderVO headVO = null;

			for (int i = 0; i < headVOs.length; i++) {
				key = (String) headVOs[i].getAttributeValue("conhanderrid");
				if (key == null) {
					key = keys[i];
					headVOs[i].setAttributeValue("conhanderrid", key);
				}
				headVOs[i].setAttributeValue("cupdateid", cupdateid);

				headVO = headVOs[i];
				if (headVO.isZero())
					continue;
				int iINDEX = 0;
				nc.vo.ic.pub.GenMethod.setStmtString(stmt, key, ++iINDEX);
				nc.vo.ic.pub.GenMethod.setStmtString(stmt, cupdateid, ++iINDEX);
				nc.vo.ic.pub.GenMethod.setStmtString(stmt, headVO.getPk_corp(),
						++iINDEX);
				nc.vo.ic.pub.GenMethod.setStmtString(stmt, headVO
						.getCcalbodyid(), ++iINDEX);
				nc.vo.ic.pub.GenMethod.setStmtString(stmt, headVO
						.getCwarehouseid(), ++iINDEX);
				nc.vo.ic.pub.GenMethod.setStmtString(stmt, headVO
						.getCinventoryid(), ++iINDEX);
				nc.vo.ic.pub.GenMethod.setStmtString(stmt, headVO.getVlot(),
						++iINDEX);
				nc.vo.ic.pub.GenMethod.setStmtString(stmt, headVO
						.getCvendorid(), ++iINDEX);
				nc.vo.ic.pub.GenMethod.setStmtString(stmt, headVO
						.getCastunitid(), ++iINDEX);
				nc.vo.ic.pub.GenMethod.setStmtString(stmt, headVO.getVfree10(),
						++iINDEX);
				nc.vo.ic.pub.GenMethod.setStmtString(stmt, headVO.getVfree9(),
						++iINDEX);
				nc.vo.ic.pub.GenMethod.setStmtString(stmt, headVO.getVfree8(),
						++iINDEX);
				nc.vo.ic.pub.GenMethod.setStmtString(stmt, headVO.getVfree7(),
						++iINDEX);
				nc.vo.ic.pub.GenMethod.setStmtString(stmt, headVO.getVfree6(),
						++iINDEX);
				nc.vo.ic.pub.GenMethod.setStmtString(stmt, headVO.getVfree5(),
						++iINDEX);
				nc.vo.ic.pub.GenMethod.setStmtString(stmt, headVO.getVfree4(),
						++iINDEX);
				nc.vo.ic.pub.GenMethod.setStmtString(stmt, headVO.getVfree3(),
						++iINDEX);
				nc.vo.ic.pub.GenMethod.setStmtString(stmt, headVO.getVfree2(),
						++iINDEX);
				nc.vo.ic.pub.GenMethod.setStmtString(stmt, headVO.getVfree1(),
						++iINDEX);
				nc.vo.ic.pub.GenMethod.setStmtBigDecimal(stmt, headVO
						.getNonhandnum(), ++iINDEX);
				nc.vo.ic.pub.GenMethod.setStmtBigDecimal(stmt, headVO
						.getNonhandastnum(), ++iINDEX);
				nc.vo.ic.pub.GenMethod.setStmtBigDecimal(stmt, headVO
						.getNnum1(), ++iINDEX);
				nc.vo.ic.pub.GenMethod.setStmtBigDecimal(stmt, headVO
						.getNastnum1(), ++iINDEX);
				nc.vo.ic.pub.GenMethod.setStmtBigDecimal(stmt, headVO
						.getNnum2(), ++iINDEX);
				nc.vo.ic.pub.GenMethod.setStmtBigDecimal(stmt, headVO
						.getNastnum2(), ++iINDEX);
				// ������
				nc.vo.ic.pub.GenMethod.setStmtBigDecimal(stmt, headVO.getHsl(),
						++iINDEX);
				// ë��
				nc.vo.ic.pub.GenMethod.setStmtBigDecimal(stmt, headVO
						.getNgrossnum(), ++iINDEX);
				// nc.vo.ic.pub.GenMethod.setStmtBigDecimal(stmt, headVO
				// .getNgrossnum1(), ++iINDEX);
				// nc.vo.ic.pub.GenMethod.setStmtBigDecimal(stmt, headVO
				// .getNgrossnum2(), ++iINDEX);

				// ִ��
				executeUpdate(stmt);
			}
			executeBatch(stmt);
		} catch (Exception e) {
			// ������쳣�׳��淶
			throw nc.bs.ic.pub.GenMethod.handleException(e.getMessage(), e);
		} finally {
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

		return cupdateid;
	}

	/**
	 * �����ݿ����һ��VO����
	 * 
	 * �������ڣ�(2002-12-23)
	 * 
	 * @param node
	 *            nc.vo.ic.ic2a3.AccountctrlItemVO
	 * @exception java.sql.SQLException
	 *                �쳣˵����
	 */
	private String[] insertErrB(OnhandnumItemVO[] voItems, String cupdateid)
			throws BusinessException {
		if (voItems == null || voItems.length == 0)
			return null;

		Connection con = null;
		PreparedStatement stmt = null;

		String[] keys = getOIDs(voItems.length);
		String sqlInsertB = "insert into ic_onhanderr_b(conhanderrbid,ccalbodyidb, cwarehouseidb, cinventoryidb, vlotb, castunitidb, vfreeb10, vfreeb9, vfreeb8, vfreeb7, vfreeb6, vfreeb5, vfreeb4, vfreeb3, vfreeb2, vfreeb1,cspaceid, nnum, nastnum,cupdateid,cvendorid,hsl,ngrossnum,pk_corp) values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?,?,?,?,?,?)";

		try {
			String key = null;
			con = getConnection();
			stmt = prepareStatement(con, sqlInsertB);

			OnhandnumItemVO voItem = null;
			for (int i = 0; i < voItems.length; i++) {
				key = keys[i];
				voItem = voItems[i];
				if (voItem.isZero())
					continue;
				int iIndex = 0;
				nc.vo.ic.pub.GenMethod.setStmtString(stmt, key, ++iIndex);
				// �����֯
				nc.vo.ic.pub.GenMethod.setStmtString(stmt, voItem
						.getCcalbodyid(), ++iIndex);
				// �ֿ�
				nc.vo.ic.pub.GenMethod.setStmtString(stmt, voItem
						.getCwarehouseid(), ++iIndex);
				// ���
				nc.vo.ic.pub.GenMethod.setStmtString(stmt, voItem
						.getCinventoryid(), ++iIndex);
				// ����
				nc.vo.ic.pub.GenMethod.setStmtString(stmt, voItem.getVlot(),
						++iIndex);
				// ������
				nc.vo.ic.pub.GenMethod.setStmtString(stmt, voItem
						.getCastunitid(), ++iIndex);
				// ������
				nc.vo.ic.pub.GenMethod.setStmtString(stmt, voItem.getVfree10(),
						++iIndex);
				nc.vo.ic.pub.GenMethod.setStmtString(stmt, voItem.getVfree9(),
						++iIndex);
				nc.vo.ic.pub.GenMethod.setStmtString(stmt, voItem.getVfree8(),
						++iIndex);
				nc.vo.ic.pub.GenMethod.setStmtString(stmt, voItem.getVfree7(),
						++iIndex);
				nc.vo.ic.pub.GenMethod.setStmtString(stmt, voItem.getVfree6(),
						++iIndex);
				nc.vo.ic.pub.GenMethod.setStmtString(stmt, voItem.getVfree5(),
						++iIndex);
				nc.vo.ic.pub.GenMethod.setStmtString(stmt, voItem.getVfree4(),
						++iIndex);
				nc.vo.ic.pub.GenMethod.setStmtString(stmt, voItem.getVfree3(),
						++iIndex);
				nc.vo.ic.pub.GenMethod.setStmtString(stmt, voItem.getVfree2(),
						++iIndex);
				nc.vo.ic.pub.GenMethod.setStmtString(stmt, voItem.getVfree1(),
						++iIndex);
				// ��λ
				nc.vo.ic.pub.GenMethod.setStmtString(stmt,
						voItem.getCspaceid(), ++iIndex);
				// ����
				nc.vo.ic.pub.GenMethod.setStmtBigDecimal(stmt,
						voItem.getNnum(), ++iIndex);
				// ������
				nc.vo.ic.pub.GenMethod.setStmtBigDecimal(stmt, voItem
						.getNastnum(), ++iIndex);
				// ��λ[��λ�е�����]
				stmt.setString(++iIndex, cupdateid);

				// ��Ӧ��
				nc.vo.ic.pub.GenMethod.setStmtString(stmt, voItem
						.getCvendorid(), ++iIndex);
				// ������
				nc.vo.ic.pub.GenMethod.setStmtBigDecimal(stmt, voItem.getHsl(),
						++iIndex);
				// ë��
				nc.vo.ic.pub.GenMethod.setStmtBigDecimal(stmt, voItem
						.getNgrossnum(), ++iIndex);
				// ��˾
				nc.vo.ic.pub.GenMethod.setStmtString(stmt, voItem.getPk_corp(),
						++iIndex);

				executeUpdate(stmt);
			}
			executeBatch(stmt);
		} catch (Exception e) {
			// ������쳣�׳��淶
			throw nc.bs.ic.pub.GenMethod.handleException(e.getMessage(), e);
		} finally {
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

		return keys;
	}

	private OnhandnumHeaderVO[] getNegOldHeadVos(String sWhere)
			throws Exception {

		OnhandnumDMO dmo = new OnhandnumDMO();
		OnhandnumHeaderVO[] vos = dmo.queryOnhandHeaderVO("ic_onhandnum",
				sWhere);
		if (vos != null && vos.length > 0) {
			for (int i = 0; i < vos.length; i++) {
				if (vos[i].getNonhandnum() != null)
					vos[i].setNonhandnum(vos[i].getNonhandnum().multiply(-1.0));

				if (vos[i].getNonhandastnum() != null)
					vos[i].setNonhandastnum(vos[i].getNonhandastnum().multiply(
							-1.0));

				if (vos[i].getNgrossnum() != null)
					vos[i].setNgrossnum(vos[i].getNgrossnum().multiply(-1.0));

				if (vos[i].getNnum1() != null)
					vos[i].setNnum1(vos[i].getNnum1().multiply(-1.0));
				if (vos[i].getNnum2() != null)
					vos[i].setNnum2(vos[i].getNnum2().multiply(-1.0));
				if (vos[i].getNastnum1() != null)
					vos[i].setNastnum1(vos[i].getNastnum1().multiply(-1.0));
				if (vos[i].getNastnum2() != null)
					vos[i].setNastnum2(vos[i].getNastnum2().multiply(-1.0));
			}

		}

		return vos;

	}

	private OnhandnumItemVO[] getNegOldBodyVos(String sWhere) throws Exception {

		OnhandnumDMO dmo = new OnhandnumDMO();
		OnhandnumItemVO[] vos = dmo.queryOnhandItemVO("ic_onhandnum_b", sWhere);
		if (vos != null && vos.length > 0) {
			for (int i = 0; i < vos.length; i++) {
				if (vos[i].getNnum() != null)
					vos[i].setNnum(vos[i].getNnum().multiply(-1.0));

				if (vos[i].getNastnum() != null)
					vos[i].setNastnum(vos[i].getNastnum().multiply(-1.0));

				if (vos[i].getNgrossnum() != null)
					vos[i].setNgrossnum(vos[i].getNgrossnum().multiply(-1.0));

				if (vos[i].getNnum1() != null)
					vos[i].setNnum1(vos[i].getNnum1().multiply(-1.0));
				if (vos[i].getNnum2() != null)
					vos[i].setNnum2(vos[i].getNnum2().multiply(-1.0));
				if (vos[i].getNastnum1() != null)
					vos[i].setNastnum1(vos[i].getNastnum1().multiply(-1.0));
				if (vos[i].getNastnum2() != null)
					vos[i].setNastnum2(vos[i].getNastnum2().multiply(-1.0));
			}

		}

		return vos;

	}

	private OnhandnumHeaderVO[] getOldBodyVos(String sWhere) throws Exception {

		ArrayList al = new ArrayList();
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		OnhandnumHeaderVO voHead = null;

		// �õ�SQL:
		String sSql = " select * from ic_onhandnum_b where 0=0 ";
		if (sWhere != null)
			sSql = sSql + sWhere;

		try {
			con = getConnection();
			stmt = con.prepareStatement(sSql);
			rs = stmt.executeQuery();
			ResultSetMetaData meta = rs.getMetaData();
			GenMethod gs = new GenMethod();
			while (rs.next()) {
				voHead = new OnhandnumHeaderVO();
				gs.setData(rs, voHead, meta);
				al.add(voHead);
			}
		} finally {
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
			try {
				if (rs != null)
					rs.close();
			} catch (Exception e) {
			}
		}

		if (al.size() > 0) {
			OnhandnumHeaderVO[] vos = new OnhandnumHeaderVO[al.size()];
			al.toArray(vos);
			return vos;
		}
		return null;

	}

	/**
	 * �\n�������ڣ�(2003-2-21 19:05:15) ���ߣ������� �޸����ڣ� �޸��ˣ� �޸�ԭ�� �㷨˵����
	 * ���ܣ�������λ���ܵ�ItemVO���뵽������λ���ܵľۺ�VO�С�
	 * 
	 * @param htVO
	 *            java.util.Hashtable
	 * @param htItemVO
	 *            java.util.Hashtable
	 */
	private ArrayList getOnhandnumVO(Hashtable htVO, Hashtable htItemVO) {
		Enumeration headKeys = null;
		// Enumeration onhandnumVO = null;
		// ����ֵ�������������������OnhandnumVO
		ArrayList alVO = new ArrayList();
		if (htVO != null && htVO.size() > 0) {
			headKeys = htVO.keys();
			// onhandnumVO = htVO.elements();
			OnhandnumHeaderVO voHead = null;
			while (headKeys.hasMoreElements()) {

				// ȡ��ϣ��htVO�������Լ���ֵ��
				String stempkey = (String) headKeys.nextElement();
				OnhandnumVO onhandnumtempVO = new OnhandnumVO();
				voHead = (OnhandnumHeaderVO) htVO.get(stempkey);
				onhandnumtempVO.setParentVO(voHead);
				if (htItemVO != null && htItemVO.size() > 0) {
					// �����htItemVO�а�����������ôȡ��ItemVO
					if (htItemVO.containsKey(stempkey)) {
						// ����ۺ�VO:OnhandnumVO��
						onhandnumtempVO
								.setChildrenVO((OnhandnumItemVO[]) htItemVO
										.get(stempkey));
						// �ӹ�ϣ����ȥ�����Լ����Ժ��Ѱ�ҡ�
						htItemVO.remove(stempkey);
					}
				}
				alVO.add(onhandnumtempVO);
			}
		}

		if (htItemVO != null && htItemVO.size() > 0) {
			Enumeration enumitemvos = htItemVO.elements();
			OnhandnumItemVO[] itemvos = null;
			OnhandnumVO handvo = null;
			while (enumitemvos.hasMoreElements()) {
				itemvos = (OnhandnumItemVO[]) enumitemvos.nextElement();
				if (itemvos != null && itemvos.length > 0) {
					for (int k = 0; k < itemvos.length; k++) {
						handvo = new OnhandnumVO();
						handvo.setParentVO(itemvos[k]
								.getOnhandnumHeaderVOFromBody());
						handvo
								.setChildrenVO(new OnhandnumItemVO[] { itemvos[k] });
						alVO.add(handvo);
					}
				}
			}
		}

		return alVO;
	}

	/**
	 * �\n�������ڣ�(2003-2-21 20:10:27) ���ߣ������� �޸����ڣ� �޸��ˣ� �޸�ԭ�� �㷨˵����
	 * ���ܣ���ic_onhandnum��ic_onhandnum_b��ɾ�����ϲ�ѯ�����ļ�¼��
	 * 
	 * @param voaCond
	 *            nc.vo.pub.query.ConditionVO[]
	 */
	private void delMatchRecord(ConditionVO[] voaCond) throws Exception {
		String sWhere = getMatchCondition(voaCond);
		StringBuffer sDelBodySql = new StringBuffer(
				"delete from ic_onhandnum_b where ic_onhandnum_b.pk_onhandnum is null or pk_onhandnum in (select pk_onhandnum from ic_onhandnum where 1=1 ");  //wanglei 2013-12-19 �����Ѷ�Ϊ֮��
		sDelBodySql.append(sWhere);
		sDelBodySql.append(" )");
		// ɾ���ִ����ӱ��¼
		delRecord(sDelBodySql.toString());
		sDelBodySql = null;
		StringBuffer sDelHeadSql = new StringBuffer(
				"delete from ic_onhandnum where 1=1 ");
		sDelHeadSql.append(sWhere);
		// ɾ���ִ��������¼
		delRecord(sDelHeadSql.toString());
		sDelHeadSql = null;

		// sDelBodySql =
		// new StringBuffer("delete from ic_onhandnum_bf where pk_onhandnum_f in
		// (select pk_onhandnum_f from ic_onhandnum_f where 1=1 ");
		// sDelBodySql.append(sWhere);
		// sDelBodySql.append(" )");
		// //ɾ���ִ����ӱ��¼
		// delRecord(sDelBodySql.toString());
		// sDelBodySql = null;
		// sDelHeadSql =
		// new StringBuffer("delete from ic_onhandnum_f where 1=1 ");
		// sDelHeadSql.append(sWhere);
		// //ɾ���ִ��������¼
		// delRecord(sDelHeadSql.toString());
		// sDelHeadSql = null;
	}

	/**
	 * �\n�������ڣ�(2003-2-21 9:17:41) ���ߣ������� �޸����ڣ� �޸��ˣ� �޸�ԭ�� �㷨˵����
	 * ˵����ɾ������������ic_onhandnum�ļ�¼��
	 * 
	 * @return java.util.Hashtable
	 * @param sSql
	 *            java.lang.String
	 * @exception java.sql.SQLException
	 *                �쳣˵����
	 */
	private void delRecord(String sSql) throws java.sql.SQLException {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			con = getConnection();
			stmt = con.prepareStatement(sSql);
			nc.vo.scm.pub.SCMEnv.out(sSql);
			stmt.executeUpdate();
		} finally {
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
			try {
				if (rs != null)
					rs.close();
			} catch (Exception e) {
			}
		}

	}

	/**
	 * �\n�������ڣ�(2003-2-21 20:11:51) ���ߣ������� �޸����ڣ� �޸��ˣ� �޸�ԭ�� �㷨˵����
	 * ���ܣ��Ӳ�ѯ������ƥ��õ���ѯic_onhandnum��������
	 * 
	 * @return java.lang.String
	 * @param voaCond
	 *            nc.vo.pub.query.ConditionVO[]
	 */
	private String getMatchCondition(ConditionVO[] voaCond) {
		StringBuffer sbWhere = new StringBuffer();
		if (voaCond != null && voaCond.length > 0) {
			for (int i = 0; i < voaCond.length; i++) {
				// ������ic_onhandnum�����κ��ֶ�����vlot������Ҫ�滻����
				if ("vbatchcode".equals(voaCond[i].getFieldCode().toString()))
					voaCond[i].setFieldCode("vlot");
				// ���ڸ�������������ṩ�ӿ�,�˴�Ҫ��cinvbasid��������һ��
				if ("cinvbasid".equals(voaCond[i].getFieldCode().toString())) {
					voaCond[i].setFieldCode("cinventoryid");
					voaCond[i].setOperaCode(" in ");
					String cinvbasid = voaCond[i].getValue();
					voaCond[i]
							.setValue("(select pk_invmandoc from bd_invmandoc where dr=0 and pk_invbasdoc='"
									+ cinvbasid + "')");
				}
				sbWhere.append(voaCond[i].getSQLStr());
			}
		}
		return sbWhere.toString();
	}

	/**
	 * �\n�������ڣ�(2003-2-21 9:54:21) ���ߣ������� �޸����ڣ� �޸��ˣ� �޸�ԭ�� �㷨˵����
	 * ���ܣ��Ӳ�ѯVO�еõ���ѯ����������SQL��
	 * 
	 * @return java.lang.String
	 * @param voaCond
	 *            nc.vo.pub.query.ConditionVO
	 */
	private String getSqlWithoutSpace_old(ConditionVO[] voaCond) {
		StringBuffer sbSQL = new StringBuffer();
		StringBuffer sbWhere = new StringBuffer();
		if (voaCond != null && voaCond.length > 0) {
			for (int i = 0; i < voaCond.length; i++) {
				sbWhere.append(voaCond[i].getSQLStr());
			}
		}
		sbSQL
				.append("SELECT pk_corp, ccalbodyid, cwarehouseid,cinventoryid, vfree1, vfree2, vfree3, vfree4, vfree5,vfree6, vfree7, vfree8, vfree9, vfree10, vbatchcode, castunitid, coalesce(sum(ninspacenum),0.0)-coalesce(sum(noutspacenum),0.0) nspacenum,coalesce(sum(ninspaceassistnum),0.0)-coalesce(sum(noutspaceassistnum),0.0) nspaceassistnum from ic_keep_detail6 where (ninspacenum is not null or noutspacenum is not null) ");
		sbSQL.append(sbWhere.toString());
		sbSQL
				.append(" group by pk_corp,ccalbodyid,cwarehouseid, cinventoryid,vfree1,vfree2,vfree3,vfree4,vfree5,vfree6,vfree7,vfree8,vfree9,vfree10,vbatchcode,castunitid order by cinventoryid");

		return sbSQL.toString();
	}

	/**
	 * �\n�������ڣ�(2003-2-21 9:54:21) ���ߣ������� �޸����ڣ� �޸��ˣ� �޸�ԭ�� �㷨˵����
	 * ���ܣ��Ӳ�ѯVO�еõ���ѯ����������SQL��
	 * 
	 * @return java.lang.String
	 * @param voaCond
	 *            nc.vo.pub.query.ConditionVO
	 */
	private String getSqlWithSpace_old(ConditionVO[] voaCond) {
		StringBuffer sbSQL = new StringBuffer();
		StringBuffer sbWhere = new StringBuffer();
		if (voaCond != null && voaCond.length > 0) {
			for (int i = 0; i < voaCond.length; i++) {
				sbWhere.append(voaCond[i].getSQLStr());
			}
		}
		sbSQL
				.append("SELECT pk_corp, ccalbodyid, cwarehouseid,cspaceid,cinventoryid, vfree1, vfree2, vfree3, vfree4, vfree5,vfree6, vfree7, vfree8, vfree9, vfree10, vbatchcode,  castunitid, coalesce(sum(ninspacenum),0.0)-coalesce(sum(noutspacenum),0.0) nspacenum,coalesce(sum(ninspaceassistnum),0.0)-coalesce(sum(noutspaceassistnum),0.0) nspaceassistnum from ic_keep_detail6 where 1=1 ");
		sbSQL.append(sbWhere.toString());
		sbSQL.append(" and cspaceid is not null ");
		sbSQL
				.append(" group by pk_corp,ccalbodyid,cwarehouseid,cspaceid, cinventoryid,vfree1,vfree2,vfree3,vfree4,vfree5,vfree6,vfree7,vfree8,vfree9,vfree10,vbatchcode,castunitid order by cinventoryid");

		return sbSQL.toString();
	}

	/**
	 * <p>
	 * ��VO����ĸ�ӱ�
	 * <p>
	 * �������ڣ�(2002-12-23)
	 * 
	 * @param vo
	 *            nc.vo.ic.ic2a3.AccountctrlVO
	 * @exception java.sql.SQLException
	 *                �쳣˵����
	 */
	private void insert(ArrayList alVO) throws nc.vo.pub.BusinessException {

		if (alVO == null || alVO.size() == 0)
			return;

		OnhandnumVO[] vos = new OnhandnumVO[alVO.size()];
		alVO.toArray(vos);
		try {
			nc.bs.ic.pub.bill.OnhandnumDMO dmo = new nc.bs.ic.pub.bill.OnhandnumDMO();
			dmo.modifyOnhandNumDirectly(vos);
		} catch (Exception e) {
			throw new nc.vo.pub.BusinessException(nc.bs.ml.NCLangResOnserver
					.getInstance().getStrByID("4008busi", "UPP4008busi-000026")/*
																				 * @res
																				 * "�ִ���������"
																				 */
					+ e.getMessage());
		}
	}

	/**
	 * �\n�������ڣ�(2003-2-21 9:17:41) ���ߣ������� �޸����ڣ� �޸��ˣ� �޸�ԭ�� �㷨˵����
	 * ˵��������ͼic_keep_detail6�ϻ����ִ�����������Ի�λ���ܡ�
	 * 
	 * @return java.util.Hashtable
	 * @param sSql
	 *            java.lang.String
	 * @exception java.sql.SQLException
	 *                �쳣˵����
	 */
	// private Hashtable queryWithoutSpace_old(ConditionVO[] voaCond)
	// throws Exception {
	//
	// /** ********************************************************** */
	// /*
	// * ����ֵ����������˾+�����֯+�ֿ�+���+����+������+������+ʧЧ���ڡ����ÿ��ȡֵΪ��ʱ���á�null�����档
	// * ��ֵ��OnhandnumVO.
	// */
	//
	// // 2004-12-01 ydy �����ִ��������롢�����
	// Hashtable retHt = new Hashtable();
	// ArrayList alxcl = queryXcl(voaCond);
	// ArrayList albrw = queryBorrow(voaCond);
	// ArrayList allnd = queryLend(voaCond);
	//
	// ArrayList al = new ArrayList();
	// al.addAll(alxcl);
	// al.addAll(albrw);
	// al.addAll(allnd);
	//
	// String[] keys = new String[] { "pk_corp", "ccalbodyid", "cwarehouseid",
	// "cinventoryid", "vfree1", "vfree2", "vfree3", "vfree4",
	// "vfree5", "vfree6", "vfree7", "vfree8", "vfree9", "vfree10",
	// "vlot", "dvalidate", "castunitid" };
	//
	// // �ϲ�VO
	//
	// OnhandnumHeaderVO[] voHeads = null;
	// if (al.size() > 0) {
	// OnhandnumHeaderVO[] voItemstmp = new OnhandnumHeaderVO[al.size()];
	// al.toArray(voItemstmp);
	// nc.vo.scm.merge.DefaultVOMerger m = new
	// nc.vo.scm.merge.DefaultVOMerger();
	// m.setMergeAttrs(keys, new String[] { "nonhandnum", "nonhandastnum",
	// "nnum1", "nastnum1", "nnum2", "nastnum2", "ngrossnum1",
	// "ngrossnum2" }, null, null, null);
	// voHeads = (OnhandnumHeaderVO[]) m.mergeByGroup(voItemstmp);
	// }
	//
	// if (voHeads != null && voHeads.length > 0) {
	// OnhandnumVO vo = null;
	// StringBuffer key = null;
	// for (int i = 0; i < voHeads.length; i++) {
	// vo = new OnhandnumVO();
	// key = new StringBuffer();
	// vo.setParentVO(voHeads[i]);
	// for (int j = 0; j < keys.length; j++) {
	// key.append(voHeads[i].getAttributeValue(keys[j]));
	// }
	// retHt.put(key.toString(), vo);
	//
	// }
	//
	// }
	//
	// return retHt;
	// }
	/**
	 * �\n�������ڣ�(2003-2-21 9:17:41) ���ߣ������� �޸����ڣ� �޸��ˣ� �޸�ԭ�� �㷨˵����
	 * ˵��������ͼic_keep_detail6�ϻ����ִ�����������Ի�λ���ܡ�
	 * 
	 * @return java.util.Hashtable
	 * @param sSql
	 *            java.lang.String
	 * @exception java.sql.SQLException
	 *                �쳣˵����
	 */
	// private Hashtable queryWithSpace_old(ConditionVO[] voaCond)
	// throws Exception {
	// /** ********************************************************** */
	// // ������ϵͳ����ӿڣ�
	// beforeCallMethod("nc.bs.ic.ic008.FixOnhandnumDMO", "queryWithSpace",
	// new Object[] { voaCond });
	// /** ********************************************************** */
	// /*
	// * ����ֵ��������//�������򣺹�˾+�����֯+�ֿ�+���+������+���κ�+ʧЧ����+�����������ÿ��ȡֵΪ��ʱ���á�null�����档
	// * ��ֵ��OnhandnumVO.
	// */
	// Hashtable retHt = new Hashtable();
	// Connection con = null;
	// PreparedStatement stmt = null;
	// ResultSet rs = null;
	// OnhandnumItemVO voItem = null;
	// Vector vecItems = null;
	// OnhandnumItemVO[] voItems = null;
	// ArrayList alHtnum = new ArrayList();
	//
	// String sPk_corp = null;
	// String sCalbodyid = null;
	// String sWarehouseid = null;
	// String sInventoryid = null;
	// String sSpaceid = null;
	// String sBatchcode = null;
	// String sAstuintid = null;
	// UFDate ufdtValidate = null;
	// String sValidate = null;
	// String sFree1 = null;
	// String sFree2 = null;
	// String sFree3 = null;
	// String sFree4 = null;
	// String sFree5 = null;
	// String sFree6 = null;
	// String sFree7 = null;
	// String sFree8 = null;
	// String sFree9 = null;
	// String sFree10 = null;
	//
	// UFDouble ufdSpacenum = null;
	// UFDouble ufdAssitSpacenum = null;
	//
	// // ����
	// StringBuffer sbPK = null;
	// // �õ�SQL:
	// String sSql = getSqlWithSpace(voaCond);
	// // ����
	// String sPK = null;
	// try {
	//
	// con = getConnection();
	// stmt = con.prepareStatement(sSql);
	// rs = stmt.executeQuery();
	//
	// while (rs.next()) {
	//
	// voItem = new OnhandnumItemVO();
	// sbPK = new StringBuffer();
	// sPk_corp = rs.getString("pk_corp");
	// sbPK.append(sPk_corp);
	// sCalbodyid = rs.getString("ccalbodyid");
	// sbPK.append(sCalbodyid);
	// sWarehouseid = rs.getString("cwarehouseid");
	// sbPK.append(sWarehouseid);
	// sSpaceid = rs.getString("cspaceid");
	// sInventoryid = rs.getString("cinventoryid");
	// sbPK.append(sInventoryid);
	// // ������
	// sFree1 = rs.getString("vfree1");
	// sbPK.append(sFree1);
	// sFree2 = rs.getString("vfree2");
	// sbPK.append(sFree2);
	// sFree3 = rs.getString("vfree3");
	// sbPK.append(sFree3);
	// sFree4 = rs.getString("vfree4");
	// sbPK.append(sFree4);
	// sFree5 = rs.getString("vfree5");
	// sbPK.append(sFree5);
	// sFree6 = rs.getString("vfree6");
	// sbPK.append(sFree6);
	// sFree7 = rs.getString("vfree7");
	// sbPK.append(sFree7);
	// sFree8 = rs.getString("vfree8");
	// sbPK.append(sFree8);
	// sFree9 = rs.getString("vfree9");
	// sbPK.append(sFree9);
	// sFree10 = rs.getString("vfree10");
	// sbPK.append(sFree10);
	// // ���κ�
	// sBatchcode = rs.getString("vbatchcode");
	// sbPK.append(sBatchcode);
	// // ʧЧ����
	// // sValidate = rs.getString("dvalidate");
	// // if(sValidate == null || sValidate.trim().length() <= 0)
	// // ufdtValidate = null;
	// // else
	// // ufdtValidate = new UFDate(sValidate);
	// // sbPK.append(sValidate);
	// // ������
	// sAstuintid = rs.getString("castunitid");
	// sbPK.append(sAstuintid);
	// ufdSpacenum = new UFDouble(rs.getBigDecimal("nspacenum"));
	// ufdAssitSpacenum = new UFDouble(rs
	// .getBigDecimal("nspaceassistnum"));
	// voItem.setNnum(ufdSpacenum);
	// voItem.setNastnum(ufdAssitSpacenum);
	// voItem.setCspaceid(sSpaceid);
	// // δ֧���µ��޸��ִ����ķ������������֯���ֿ⣬���εȼ��뵽voItem�С�
	// voItem.setCwarehouseid(sWarehouseid);
	// voItem.setCcalbodyid(sCalbodyid);
	// voItem.setCinventoryid(sInventoryid);
	// voItem.setVlot(sBatchcode);
	// voItem.setCastunitid(sAstuintid);
	// voItem.setVfree1(sFree1);
	// voItem.setVfree2(sFree2);
	// voItem.setVfree3(sFree3);
	// voItem.setVfree4(sFree4);
	// voItem.setVfree5(sFree5);
	// voItem.setVfree6(sFree6);
	// voItem.setVfree7(sFree7);
	// voItem.setVfree8(sFree8);
	// voItem.setVfree9(sFree9);
	// voItem.setVfree10(sFree10);
	// if (!retHt.containsKey(sbPK.toString())) {
	// vecItems = new Vector();
	// vecItems.add(voItem);
	// retHt.put(sbPK.toString(), vecItems);
	// alHtnum.add(sbPK.toString());
	// } else {
	// vecItems = (Vector) retHt.get(sbPK.toString());
	// vecItems.add(voItem);
	// retHt.put(sbPK.toString(), vecItems);
	// }
	// }
	// if (alHtnum.size() > 0) {
	// for (int i = 0; i < alHtnum.size(); i++) {
	// sPK = (String) alHtnum.get(i);
	// if (sPK != null) {
	// vecItems = (Vector) retHt.get(sPK);
	// if (vecItems != null && vecItems.size() > 0) {
	// voItems = new OnhandnumItemVO[vecItems.size()];
	// vecItems.copyInto(voItems);
	// retHt.put(sPK, voItems);
	// }
	// }
	// }
	// }
	// } catch (Exception e) {
	// nc.vo.scm.pub.SCMEnv.error(e);
	// throw e;
	// } finally {
	// try {
	// if (stmt != null)
	// stmt.close();
	// } catch (Exception e) {
	// }
	// try {
	// if (con != null)
	// con.close();
	// } catch (Exception e) {
	//
	// }
	// try {
	// if (rs != null)
	// rs.close();
	// } catch (Exception e) {
	// }
	// }
	// /** ********************************************************** */
	// // ������ϵͳ����ӿڣ�
	// afterCallMethod("nc.bs.ic.ic008.FixOnhandnumDMO", "queryWithSpace",
	// new Object[] { voaCond });
	// /** ********************************************************** */
	//
	// return retHt;
	// }
	/**
	 * �\n�������ڣ�(2003-2-21 9:17:41) ���ߣ������� �޸����ڣ� �޸��ˣ� �޸�ԭ�� �㷨˵����
	 * ˵��������ͼic_keep_detail6�ϻ����ִ�����������Ի�λ���ܡ�
	 * 
	 * @return java.util.Hashtable
	 * @param sSql
	 *            java.lang.String
	 * @exception java.sql.SQLException
	 *                �쳣˵����
	 */
	// private ArrayList queryXcl_old(ConditionVO[] voaCond) throws Exception {
	// /** ********************************************************** */
	// // ������ϵͳ����ӿڣ�
	// beforeCallMethod("nc.bs.ic.ic008.FixOnhandnumDMO", "queryWithoutSpace",
	// new Object[] { voaCond });
	// /** ********************************************************** */
	// /*
	// * ����ֵ����������˾+�����֯+�ֿ�+���+����+������+������+ʧЧ���ڡ����ÿ��ȡֵΪ��ʱ���á�null�����档
	// * ��ֵ��OnhandnumVO.
	// */
	// ArrayList al = new ArrayList();
	// Connection con = null;
	// PreparedStatement stmt = null;
	// ResultSet rs = null;
	// OnhandnumVO voOnhandnum = null;
	// OnhandnumHeaderVO voHead = null;
	// OnhandnumItemVO[] voItems = null;
	//
	// String sPk_corp = null;
	// String sCalbodyid = null;
	// String sWarehouseid = null;
	// String sInventoryid = null;
	//
	// String sBatchcode = null;
	// String sAstuintid = null;
	// UFDate ufdtValidate = null;
	// String sValidate = null;
	// String sFree1 = null;
	// String sFree2 = null;
	// String sFree3 = null;
	// String sFree4 = null;
	// String sFree5 = null;
	// String sFree6 = null;
	// String sFree7 = null;
	// String sFree8 = null;
	// String sFree9 = null;
	// String sFree10 = null;
	//
	// UFDouble ufdSpacenum = null;
	// UFDouble ufdAssitSpacenum = null;
	//
	// // ����
	// // �������򣺹�˾+�����֯+�ֿ�+���+������+���κ�+ʧЧ����+������
	// StringBuffer sbPK = null;
	// // �õ�SQL:
	// String sSql = getSqlWithoutSpace(voaCond);
	//
	// try {
	//
	// con = getConnection();
	// stmt = con.prepareStatement(sSql);
	// rs = stmt.executeQuery();
	//
	// while (rs.next()) {
	//
	// voHead = new OnhandnumHeaderVO();
	// voHead.setPk_corp(rs.getString("pk_corp"));
	// voHead.setCcalbodyid(rs.getString("ccalbodyid"));
	// voHead.setCwarehouseid(rs.getString("cwarehouseid"));
	// voHead.setCinventoryid(rs.getString("cinventoryid"));
	// voHead.setVfree1(rs.getString("vfree1"));
	// voHead.setVfree2(rs.getString("vfree2"));
	// voHead.setVfree3(rs.getString("vfree3"));
	// voHead.setVfree4(rs.getString("vfree4"));
	// voHead.setVfree5(rs.getString("vfree5"));
	// voHead.setVfree6(rs.getString("vfree6"));
	// voHead.setVfree7(rs.getString("vfree7"));
	// voHead.setVfree8(rs.getString("vfree8"));
	// voHead.setVfree9(rs.getString("vfree9"));
	// voHead.setVfree10(rs.getString("vfree10"));
	// voHead.setVlot(rs.getString("vbatchcode"));
	// voHead.setCastunitid(rs.getString("castunitid"));
	// // ʧЧ����
	// sValidate = rs.getString("dvalidate");
	// if (sValidate == null || sValidate.trim().length() <= 0)
	// ufdtValidate = null;
	// else
	// ufdtValidate = new UFDate(sValidate);
	//
	// voHead.setDvalidate(ufdtValidate);
	// if (ufdtValidate != null)
	// voHead.isDvalidate = true;
	//
	// java.math.BigDecimal num = rs.getBigDecimal("nspacenum");
	// java.math.BigDecimal numast = rs
	// .getBigDecimal("nspaceassistnum");
	// if (num != null)
	// voHead.setNonhandnum(new UFDouble(num));
	// if (numast != null)
	// voHead.setNonhandastnum(new UFDouble(numast));
	//
	// al.add(voHead);
	// }
	//
	// } finally {
	// try {
	// if (stmt != null)
	// stmt.close();
	// } catch (Exception e) {
	// }
	// try {
	// if (con != null)
	// con.close();
	// } catch (Exception e) {
	//
	// }
	// try {
	// if (rs != null)
	// rs.close();
	// } catch (Exception e) {
	// }
	// }
	// /** ********************************************************** */
	// // ������ϵͳ����ӿڣ�
	// afterCallMethod("nc.bs.ic.ic008.FixOnhandnumDMO", "queryWithoutSpace",
	// new Object[] { voaCond });
	// /** ********************************************************** */
	//
	// return al;
	// }
	private static OnhandnumHeaderVO getOnhandnumHeaderVO(ResultSet rs)
			throws SQLException {
		OnhandnumHeaderVO voHead = new OnhandnumHeaderVO();
		voHead.setPk_corp(rs.getString("pk_corp"));
		voHead.setCcalbodyid(rs.getString("ccalbodyid"));
		voHead.setCwarehouseid(rs.getString("cwarehouseid"));
		voHead.setCinventoryid(rs.getString("cinventoryid"));
		voHead.setVfree1(rs.getString("vfree1"));
		voHead.setVfree2(rs.getString("vfree2"));
		voHead.setVfree3(rs.getString("vfree3"));
		voHead.setVfree4(rs.getString("vfree4"));
		voHead.setVfree5(rs.getString("vfree5"));
		voHead.setVfree6(rs.getString("vfree6"));
		voHead.setVfree7(rs.getString("vfree7"));
		voHead.setVfree8(rs.getString("vfree8"));
		voHead.setVfree9(rs.getString("vfree9"));
		voHead.setVfree10(rs.getString("vfree10"));
		voHead.setVlot(rs.getString("vbatchcode"));
		voHead.setCastunitid(rs.getString("castunitid"));
		voHead.setCvendorid(rs.getString("cproviderid"));
		voHead.setCinvbasid(rs.getString("cinvbasid"));
		BigDecimal hsl = rs.getBigDecimal("hsl");
		if (hsl != null)
			voHead.setHsl(new UFDouble(hsl));

		return voHead;

	}

	private static int Query_No_Download = -1;

	private static int Query_From_Ic = 0;

	private static int Query_From_MonthRecord = 1;

	private StringBuffer getWhere(ConditionVO[] voaCond) {
		StringBuffer sbWhere = new StringBuffer();
		if (voaCond != null && voaCond.length > 0) {
			for (int i = 0; i < voaCond.length; i++) {
				if (voaCond[i].getFieldCode().equals("cinvbasid"))// ��Ҫ����Ϊ������������ṩ�ӿڵ�����
					voaCond[i].setFieldCode("a.pk_invbadsoc");
				sbWhere.append(voaCond[i].getSQLStr());
			}
		}
		return sbWhere;
	}
	
	private StringBuffer getBBCWhere(ConditionVO[] voaCond) {
		StringBuffer sbWhere = new StringBuffer();
		
		if (voaCond != null && voaCond.length > 0) {
			for (int i = 0; i < voaCond.length; i++) {
				ConditionVO newvoaCond = (ConditionVO)voaCond[i].clone();
				if (newvoaCond.getFieldCode().equals("cinvbasid"))// ��Ҫ����Ϊ������������ṩ�ӿڵ�����
					newvoaCond.setFieldCode("a.pk_invbadsoc");
				if (newvoaCond.getFieldCode().equals("pk_corp"))
					newvoaCond.setFieldCode("h.pk_corp");
				if (newvoaCond.getFieldCode().equals("ccalbodyid"))
					newvoaCond.setFieldCode("h.pk_calbody");
				if (newvoaCond.getFieldCode().equals("cwarehouseid"))
					newvoaCond.setFieldCode("h.cwarehouseid");
				if (newvoaCond.getFieldCode().equals("cinventoryid"))
					newvoaCond.setFieldCode("b.cinventoryid");
				sbWhere.append(newvoaCond.getSQLStr());
			}
		}
		return sbWhere;
	}
	
	private StringBuffer getBBCHisWhere(ConditionVO[] voaCond) {
		StringBuffer sbWhere = new StringBuffer();
		
		if (voaCond != null && voaCond.length > 0) {
			for (int i = 0; i < voaCond.length; i++) {
				sbWhere.append(voaCond[i].getSQLStr());
			}
		}
		return sbWhere;
	}

	private static OnhandnumItemVO getOnhandItemVO(ResultSet rs)
			throws SQLException {
		OnhandnumItemVO voItem = new OnhandnumItemVO();

		voItem.setPk_corp(rs.getString("pk_corp"));
		voItem.setCwarehouseid(rs.getString("cwarehouseid"));
		voItem.setCcalbodyid(rs.getString("ccalbodyid"));
		voItem.setCinventoryid(rs.getString("cinventoryid"));
		voItem.setVlot(rs.getString("vbatchcode"));
		voItem.setCastunitid(rs.getString("castunitid"));
		voItem.setCvendorid(rs.getString("cproviderid"));
		voItem.setCspaceid(rs.getString("cspaceid"));

		// ������
		UFDouble hsl = rs.getBigDecimal("hsl") == null ? null : new UFDouble(rs
				.getBigDecimal("hsl"));
		voItem.setHsl(hsl);

		for (int i = 1; i <= FreeVO.FREE_ITEM_NUM; i++) {
			voItem.setAttributeValue("vfree" + i, rs.getString("vfree" + i));
		}

		voItem.setCinvbasid(rs.getString("cinvbasid"));

		UFDouble ngrossnum = rs.getBigDecimal("ngrossnum") == null ? null
				: new UFDouble(rs.getBigDecimal("ngrossnum"));// ë��
		voItem.setNgrossnum(ngrossnum);

		UFDouble ufdSpacenum = rs.getBigDecimal("nspacenum") == null ? null
				: new UFDouble(rs.getBigDecimal("nspacenum"));
		voItem.setNnum(ufdSpacenum);

		UFDouble ufdAssitSpacenum = rs.getBigDecimal("nspaceassistnum") == null ? null
				: new UFDouble(rs.getBigDecimal("nspaceassistnum"));
		voItem.setNastnum(ufdAssitSpacenum);

		return voItem;

	}

	// /**
	// * �ź��� ����:���ִ���[ֻ���λ����],Ϊ��֯�ִ����ӱ�������׼�� ��ע:
	// *
	// */
	// private Hashtable queryWithSpace(ConditionVO[] voaCond, int iQueryMode)
	// throws Exception {
	// /*
	// * ����ֵ��������//�������򣺹�˾+�����֯+�ֿ�+���+������+���κ�+������+��Ӧ��+�����ʡ����ÿ��ȡֵΪ��ʱ���á�null�����档
	// * ��ֵ��OnhandnumVO.
	// */
	// Hashtable retHt = new Hashtable();
	// Connection con = null;
	// PreparedStatement stmt = null;
	// ResultSet rs = null;
	// OnhandnumItemVO voItem = null;
	// Vector vecItems = null;
	// OnhandnumItemVO[] voItems = null;
	// ArrayList alHtnum = new ArrayList();
	//
	// String sPk_corp = null;
	// String sCalbodyid = null;
	// String sWarehouseid = null;
	// String sInventoryid = null;
	// String cinvbasid = null;
	// String sSpaceid = null;
	// String sBatchcode = null;
	// String sAstuintid = null;
	// String sFree1 = null;
	// String sFree2 = null;
	// String sFree3 = null;
	// String sFree4 = null;
	// String sFree5 = null;
	// String sFree6 = null;
	// String sFree7 = null;
	// String sFree8 = null;
	// String sFree9 = null;
	// String sFree10 = null;
	// String cvendorid = null;
	// UFDouble hsl = null;
	//
	// UFDouble ngrossnum = null;
	// UFDouble ufdSpacenum = null;
	// UFDouble ufdAssitSpacenum = null;
	//
	// // ����
	// StringBuffer sbPK = null;
	// // �õ�SQL:
	//    
	// String sSql = getSqlWithSpace(voaCond,iQueryMode);
	// ArrayList alAll = new ArrayList();
	// // ����
	// String sPK = null;
	// try {
	// con = getConnection();
	// stmt = con.prepareStatement(sSql);
	// rs = stmt.executeQuery();
	// while (rs.next()) {
	// voItem = new OnhandnumItemVO();
	// sbPK = new StringBuffer();
	// sPk_corp = rs.getString("pk_corp");
	// sbPK.append(sPk_corp);
	// sCalbodyid = rs.getString("ccalbodyid");
	// sbPK.append(sCalbodyid);
	// sWarehouseid = rs.getString("cwarehouseid");
	// sbPK.append(sWarehouseid);
	// sSpaceid = rs.getString("cspaceid");
	// sInventoryid = rs.getString("cinventoryid");
	// sbPK.append(sInventoryid);
	// cinvbasid = rs.getString("cinvbasid");
	// sbPK.append(cinvbasid);
	// // ������
	// sFree1 = rs.getString("vfree1");
	// sbPK.append(sFree1);
	// sFree2 = rs.getString("vfree2");
	// sbPK.append(sFree2);
	// sFree3 = rs.getString("vfree3");
	// sbPK.append(sFree3);
	// sFree4 = rs.getString("vfree4");
	// sbPK.append(sFree4);
	// sFree5 = rs.getString("vfree5");
	// sbPK.append(sFree5);
	// sFree6 = rs.getString("vfree6");
	// sbPK.append(sFree6);
	// sFree7 = rs.getString("vfree7");
	// sbPK.append(sFree7);
	// sFree8 = rs.getString("vfree8");
	// sbPK.append(sFree8);
	// sFree9 = rs.getString("vfree9");
	// sbPK.append(sFree9);
	// sFree10 = rs.getString("vfree10");
	// sbPK.append(sFree10);
	// // ���κ�
	// sBatchcode = rs.getString("vbatchcode");
	// sbPK.append(sBatchcode);
	// // //ʧЧ����
	// // sValidate = rs.getString("dvalidate");
	// // if(sValidate == null || sValidate.trim().length() <= 0)
	// // ufdtValidate = null;
	// // else
	// // ufdtValidate = new UFDate(sValidate);
	// // sbPK.append(sValidate);
	// // ������
	// sAstuintid = rs.getString("castunitid");
	// sbPK.append(sAstuintid);
	// // ��Ӧ��
	// cvendorid = rs.getString("cproviderid");
	// sbPK.append(cvendorid);
	// // ������
	// hsl = rs.getBigDecimal("hsl") == null ? null : new UFDouble(rs
	// .getBigDecimal("hsl"));
	// sbPK.append(hsl);
	//
	// ngrossnum = rs.getBigDecimal("ngrossnum") == null ? null
	// : new UFDouble(rs.getBigDecimal("ngrossnum"));// ë��
	// ufdSpacenum = new UFDouble(rs.getBigDecimal("nspacenum"));
	// ufdAssitSpacenum = new UFDouble(rs
	// .getBigDecimal("nspaceassistnum"));
	// voItem.setNgrossnum(ngrossnum);
	// voItem.setNnum(ufdSpacenum);
	// voItem.setNastnum(ufdAssitSpacenum);
	// voItem.setCspaceid(sSpaceid);
	// // δ֧���µ��޸��ִ����ķ������������֯���ֿ⣬���εȼ��뵽voItem�С�
	// voItem.setPk_corp(sPk_corp);
	// voItem.setCwarehouseid(sWarehouseid);
	// voItem.setCcalbodyid(sCalbodyid);
	// voItem.setCinventoryid(sInventoryid);
	// voItem.setVlot(sBatchcode);
	// voItem.setCastunitid(sAstuintid);
	// voItem.setCvendorid(cvendorid);
	// voItem.setNgrossnum(ngrossnum);
	// voItem.setHsl(hsl);
	// voItem.setVfree1(sFree1);
	// voItem.setVfree2(sFree2);
	// voItem.setVfree3(sFree3);
	// voItem.setVfree4(sFree4);
	// voItem.setVfree5(sFree5);
	// voItem.setVfree6(sFree6);
	// voItem.setVfree7(sFree7);
	// voItem.setVfree8(sFree8);
	// voItem.setVfree9(sFree9);
	// voItem.setVfree10(sFree10);
	// voItem.setCinvbasid(cinvbasid);
	//
	// if (!retHt.containsKey(sbPK.toString())) {
	// vecItems = new Vector();
	// vecItems.add(voItem);
	// retHt.put(sbPK.toString(), vecItems);
	// alHtnum.add(sbPK.toString());
	// } else {
	// vecItems = (Vector) retHt.get(sbPK.toString());
	// vecItems.add(voItem);
	// retHt.put(sbPK.toString(), vecItems);
	// }
	// alAll.add(voItem);
	// }
	// if (alHtnum.size() > 0) {
	// for (int i = 0; i < alHtnum.size(); i++) {
	// sPK = (String) alHtnum.get(i);
	// if (sPK != null) {
	// vecItems = (Vector) retHt.get(sPK);
	// if (vecItems != null && vecItems.size() > 0) {
	// voItems = new OnhandnumItemVO[vecItems.size()];
	// vecItems.copyInto(voItems);
	// retHt.put(sPK, voItems);
	// }
	// }
	// }
	// retHt.put("all", alAll);
	// }
	// } catch (Exception e) {
	// nc.vo.scm.pub.SCMEnv.error(e);
	// throw e;
	// } finally {
	// try {
	// if (stmt != null)
	// stmt.close();
	// } catch (Exception e) {
	// }
	// try {
	// if (con != null)
	// con.close();
	// } catch (Exception e) {
	//
	// }
	// try {
	// if (rs != null)
	// rs.close();
	// } catch (Exception e) {
	// }
	// }
	//
	// return retHt;
	// }

	public static String ic_keep_detail6 = " SELECT h.pk_corp, b.dbizdate,h.daccountdate, h.pk_calbody AS ccalbodyid, h.cwarehouseid, c.cspaceid,"
			+ " b.cinventoryid, b.vfree1, b.vfree2, b.vfree3, b.vfree4, b.vfree5, b.vfree6, b.vfree7,"
			+ " b.vfree8, b.vfree9, b.vfree10, b.vbatchcode, s.dvalidate ,b.castunitid, b.hsl,COALESCE(c.ninspacenum,b.ninnum,0.0) as ninspacenum,"
			+ " COALESCE(c.ninspaceassistnum,b.ninassistnum,0.0) as ninspaceassistnum, COALESCE(c.noutspacenum,b.noutnum,0.0) as noutspacenum,"
			+ " COALESCE(c.noutspaceassistnum,b.noutassistnum,0.0) as noutspaceassistnum,  b.ccostobject, b.cprojectid,b.cprojectphaseid, b.csourcetype,"
			+ " h.fbillflag, h.cbilltypecode, h.cdispatcherid, b.cvendorid AS cproviderid,COALESCE(c.ningrossnum,b.ningrossnum,0.0) as ningrossnum,"
			+ " COALESCE(c.noutgrossnum,b.noutgrossnum,0.0) as noutgrossnum ,b.cinvbasid,b.nplannedmny,h.cgeneralhid,b.cgeneralbid"
			+ " FROM ic_general_h h inner join  ic_general_b b on h.cgeneralhid = b.cgeneralhid"
			+ " left outer join ic_general_bb1 c on b.cgeneralbid = c.cgeneralbid AND c.dr = 0 left outer join scm_batchcode s"
			+ " on b.cinvbasid=s.pk_invbasdoc and b.vbatchcode=s.vbatchcode"
			+ " WHERE (((h.cbilltypecode in ("
			+ IBillType.PURCHASE_IN
			+ ") AND b.fchecked = 0 AND (h.cbiztype IN"
			+ "   (SELECT pk_busitype"
			+ "   FROM bd_busitype"
			+ "   WHERE verifyrule <> 'J') OR"
			+ " h.cbiztype IS NULL)) OR"
			+ " h.cbilltypecode in ("
			+ IBillType.ONHAND_IN_WITHOUT_PURCHASE
			+ ")) AND ninnum IS NOT NULL OR"
			+ " ((h.cbilltypecode in ("
			+ IBillType.SALE_OUT
			+ ") AND (h.cbiztype IN"
			+ "    (SELECT pk_busitype"
			+ "   FROM bd_busitype"
			+ "   WHERE verifyrule <> 'C') OR"
			+ " h.cbiztype IS NULL)) OR"
			+ " h.cbilltypecode in ("
			+ IBillType.ONHAND_OUT_WITHOUT_SALE
			+ ")) AND noutnum IS NOT NULL OR"
			+ " h.cbilltypecode in ("
			+ IBillType.SPACE_ADJUST + ")) AND h.dr = 0 AND b.dr = 0 ";

	public enum QueryType {
		Query_No_Download, Query_From_Ic, Query_From_MonthRecord, Query_Bill
	};

	/**
	 * �\n�������ڣ�(2003-2-21 9:17:41) ���ߣ������� �޸����ڣ� �޸��ˣ� �޸�ԭ�� �㷨˵����
	 * ˵��������ͼic_keep_detail6�ϻ����ִ�����������Ի�λ���ܡ�
	 * 
	 * @return java.util.Hashtable
	 * @param sSql
	 *            java.lang.String
	 * @exception java.sql.SQLException
	 *                �쳣˵����
	 */
	private Hashtable queryWithoutSpace(ConditionVO[] voaCond, String cbillid,
			String cbilltype) throws Exception {
		/*
		 * ����ֵ����������˾+�����֯+�ֿ�+���+����+������+������+��Ӧ��+�����ʡ����ÿ��ȡֵΪ��ʱ���á�null�����档
		 * ��ֵ��OnhandnumVO.
		 */
		// 2004-12-01 ydy �����ִ��������롢�����
		ArrayList al = new ArrayList();
		if (cbillid != null) {
			ArrayList alxcl = queryXcl(voaCond, QueryType.Query_Bill, cbillid);
			al.addAll(alxcl);
		} else if (getDowLoadDate() == null) {
			ArrayList alxcl = queryXcl(voaCond, QueryType.Query_No_Download,
					null);
			al.addAll(alxcl);
		} else {
			ArrayList al1 = queryXcl(voaCond, QueryType.Query_From_Ic, null);
			ArrayList al2 = queryXcl(voaCond, QueryType.Query_From_MonthRecord,
					null);
			al.addAll(al1);
			al.addAll(al2);
		}

		if (cbillid != null) {
			if (isBorrow(cbilltype)) {
				ArrayList albrw = queryBorrow(voaCond, QueryType.Query_Bill,
						cbillid);
				al.addAll(albrw);
			}
		} else if (getDowLoadDate() == null) {
			ArrayList albrw = queryBorrow(voaCond, QueryType.Query_No_Download,
					null);
			al.addAll(albrw);
		} else {
			ArrayList al1 = queryBorrow(voaCond, QueryType.Query_From_Ic, null);
			ArrayList al2 = queryBorrow(voaCond,
					QueryType.Query_From_MonthRecord, null);
			al.addAll(al1);
			al.addAll(al2);
		}

		if (cbillid != null) {
			if (isLend(cbilltype)) {
				ArrayList albrw = queryLend(voaCond, QueryType.Query_Bill,
						cbillid);
				al.addAll(albrw);
			}
		} else if (getDowLoadDate() == null) {
			ArrayList albrw = queryLend(voaCond, QueryType.Query_No_Download,
					null);
			al.addAll(albrw);
		} else {
			ArrayList al1 = queryLend(voaCond, QueryType.Query_From_Ic, null);
			ArrayList al2 = queryLend(voaCond,
					QueryType.Query_From_MonthRecord, null);
			al.addAll(al1);
			al.addAll(al2);
		}

		Hashtable retHt = mergeOnHandVOs(al);

		// String[] keys = new String[] { "pk_corp", "ccalbodyid",
		// "cwarehouseid",
		// "cinventoryid", "cinvbasid", "vfree1", "vfree2", "vfree3",
		// "vfree4", "vfree5", "vfree6", "vfree7", "vfree8", "vfree9",
		// "vfree10", "vlot", "castunitid", "cvendorid", "hsl" };
		// // �ϲ�VO
		// OnhandnumHeaderVO[] voHeads = null;
		// if (al.size() > 0) {
		// OnhandnumHeaderVO[] voItemstmp = new OnhandnumHeaderVO[al.size()];
		// al.toArray(voItemstmp);
		// nc.vo.scm.merge.DefaultVOMerger m = new
		// nc.vo.scm.merge.DefaultVOMerger();
		// m.setMergeAttrs(keys, new String[] { "nonhandnum", "nonhandastnum",
		// "nnum1", "nastnum1", "nnum2", "nastnum2", "ngrossnum",
		// "ngrossnum1", "ngrossnum2" }, null, null, null);
		// voHeads = (OnhandnumHeaderVO[]) m.mergeByGroup(voItemstmp);
		// }
		//
		// if (voHeads != null && voHeads.length > 0) {
		// StringBuffer key = null;
		// for (int i = 0; i < voHeads.length; i++) {
		// key = new StringBuffer();
		// for (int j = 0; j < keys.length; j++) {
		// key.append(voHeads[i].getAttributeValue(keys[j]));
		// }
		// retHt.put(key.toString(), voHeads[i]);
		// }
		// }
		return retHt;
	}
	

	/**
	 * �����ˣ������� ����ʱ�䣺2009-8-18 ����10:39:46 ����ԭ�� ����ά�Ƚ��з��鴦������SQL�����հѽ����ֱ�Ӳ��뵽�ִ������С�
	 * @param voaCond
	 * @return
	 * @throws Exception
	 */
	private String getQueryWithoutSpaceSQL(ConditionVO[] voaCond) throws Exception {
		/*
		 * ����ֵ����������˾+�����֯+�ֿ�+���+����+������+������+��Ӧ��+�����ʡ����ÿ��ȡֵΪ��ʱ���á�null�����档
		 * ��ֵ��OnhandnumVO.
		 */
		StringBuilder qrySQLBuilder = new StringBuilder();
		if (getDowLoadDate() == null) {
			qrySQLBuilder.append("select "+ onhandQryKeyTranSQL);
			qrySQLBuilder.append(",nspacenum as nonhandnum,nspaceassistnum as nonhandastnum,ngrossnum");
			qrySQLBuilder.append(",0 as nnum1,0 as nastnum1,0 as ngrossnum1");
			qrySQLBuilder.append(",0 as nnum2,0 as nastnum2,0 as ngrossnum2 from (");
			qrySQLBuilder.append(getSqlWithoutSpace(voaCond, QueryType.Query_No_Download));
			qrySQLBuilder.append(") tempnospace where nspacenum > 0 or nspaceassistnum > 0 or ngrossnum > 0 or nspacenum < 0 or nspaceassistnum < 0 or ngrossnum < 0 ");
		} else {
			qrySQLBuilder.append("select "+ onhandQryKeyTranSQL);
			qrySQLBuilder.append(",sum(nspacenum) as nonhandnum,sum(nspaceassistnum) as nonhandastnum,sum(ngrossnum) as ngrossnum");
			qrySQLBuilder.append(",0 as nnum1,0 as nastnum1,0 as ngrossnum1");
			qrySQLBuilder.append(",0 as nnum2,0 as nastnum2,0 as ngrossnum2 from (");
			qrySQLBuilder.append(getSqlWithoutSpace(voaCond, QueryType.Query_From_Ic));
			qrySQLBuilder.append(" union all ");
			qrySQLBuilder.append(getSqlWithoutSpace(voaCond, QueryType.Query_From_MonthRecord));
			qrySQLBuilder.append(") tempnospace ");
			qrySQLBuilder.append(" group by ");
			qrySQLBuilder.append(onhandQryGroupSQL);
			qrySQLBuilder.append(" having ( sum(nspacenum) > 0 or sum(nspaceassistnum) > 0 or sum(ngrossnum) > 0 or sum(nspacenum) < 0 or sum(nspaceassistnum) < 0 or sum(ngrossnum) < 0 ) ");
		}

		if (getDowLoadDate() == null) {
			qrySQLBuilder.append(" union all ");
			qrySQLBuilder.append("select "+ onhandQryKeyTranSQL);
			qrySQLBuilder.append(",0 as nonhandnum,0 as nonhandastnum,0 as ngrossnum");
			qrySQLBuilder.append(",nnum1,nastnum1,ngrossnum1");
			qrySQLBuilder.append(",0 as nnum2,0 as nastnum2,0 as ngrossnum2 from (");
			qrySQLBuilder.append(getSqlBorrow(voaCond, QueryType.Query_No_Download));
			qrySQLBuilder.append(") tempborrow where nnum1 > 0 or nastnum1 > 0 or ngrossnum1 > 0 or nnum1 < 0 or nastnum1 < 0 or ngrossnum1 < 0");
		} else {
			qrySQLBuilder.append(" union all ");
			qrySQLBuilder.append("select "+ onhandQryKeyTranSQL);
			qrySQLBuilder.append(",0 as nonhandnum,0 as nonhandastnum,0 as ngrossnum");
			qrySQLBuilder.append(",sum(nnum1) as nnum1,sum(nastnum1) as nastnum1,sum(ngrossnum1) as ngrossnum1");
			qrySQLBuilder.append(",0 as nnum2,0 as nastnum2,0 as ngrossnum2 from (");
			qrySQLBuilder.append(getSqlBorrow(voaCond, QueryType.Query_From_Ic));
			qrySQLBuilder.append(" union all ");
			qrySQLBuilder.append(getSqlBorrow(voaCond, QueryType.Query_From_MonthRecord));
			qrySQLBuilder.append(") tempborrow ");
			qrySQLBuilder.append(" group by ");
			qrySQLBuilder.append(onhandQryGroupSQL);
			qrySQLBuilder.append(" having ( sum(nnum1) > 0 or sum(nastnum1) > 0 or sum(ngrossnum1) > 0 or sum(nnum1) < 0 or sum(nastnum1) < 0 or sum(ngrossnum1) < 0 ) ");
		}

		if (getDowLoadDate() == null) {
			qrySQLBuilder.append(" union all ");
			qrySQLBuilder.append("select "+ onhandQryKeyTranSQL);
			qrySQLBuilder.append(",0 as nonhandnum,0 as nonhandastnum,0 as ngrossnum");
			qrySQLBuilder.append(",0 as nnum1,0 as nastnum1,0 as ngrossnum1");
			qrySQLBuilder.append(",nnum2,nastnum2,ngrossnum2 from (");
			qrySQLBuilder.append(getSqlLend(voaCond, QueryType.Query_No_Download));
			qrySQLBuilder.append(") templend where nnum2 > 0 or nastnum2 > 0 or ngrossnum2 > 0 or nnum2 < 0 or nastnum2 < 0 or ngrossnum2 < 0");
		} else {
			qrySQLBuilder.append(" union all ");
			qrySQLBuilder.append("select "+ onhandQryKeyTranSQL);
			qrySQLBuilder.append(",0 as nonhandnum,0 as nonhandastnum,0 as ngrossnum");
			qrySQLBuilder.append(",0 as nnum1,0 as nastnum1,0 as ngrossnum1");
			qrySQLBuilder.append(",sum(nnum2) as nnum2,sum(nastnum2) as nastnum2,sum(ngrossnum2) as ngrossnum2 from (");
			qrySQLBuilder.append(getSqlLend(voaCond, QueryType.Query_From_Ic));
			qrySQLBuilder.append(" union all ");
			qrySQLBuilder.append(getSqlLend(voaCond, QueryType.Query_From_MonthRecord));
			qrySQLBuilder.append(") templend ");
			qrySQLBuilder.append(" group by ");
			qrySQLBuilder.append(onhandQryGroupSQL);
			qrySQLBuilder.append(" having ( sum(nnum2) > 0 or sum(nastnum2) > 0 or sum(ngrossnum2) > 0 or sum(nnum2) < 0 or sum(nastnum2) < 0 or sum(ngrossnum2) < 0 ) ");
		}

		return qrySQLBuilder.toString();
	}

	/**
	 * �ź��� ����:���ִ���[���Ի�λ] ��ע:
	 */
	private ArrayList queryXcl(ConditionVO[] voaCond, QueryType iQueryMode,
			String cbillid) throws Exception {
		/*
		 * ����ֵ����������˾+�����֯+�ֿ�+���+����+������+������+ʧЧ����+��Ӧ��+�����ʡ����ÿ��ȡֵΪ��ʱ���á�null�����档
		 * ��ֵ��OnhandnumVO.
		 */
		ArrayList al = new ArrayList();
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		OnhandnumHeaderVO voHead = null;

		// �õ�SQL:
		String sSql = getSqlWithoutSpace(voaCond, iQueryMode);
		try {
			con = getConnection();
			stmt = con.prepareStatement(sSql);
			if (iQueryMode == QueryType.Query_Bill)
				stmt.setString(1, cbillid);
			rs = stmt.executeQuery();
			nc.bs.ic.pub.GenMethod.setResultSetNoRowNumLimit(rs);
			while (rs.next()) {
				BigDecimal ngrossnum = rs.getBigDecimal("ngrossnum");
				java.math.BigDecimal num = rs.getBigDecimal("nspacenum");
				java.math.BigDecimal numast = rs
						.getBigDecimal("nspaceassistnum");

				if ((ngrossnum == null || ngrossnum.compareTo(ZERO) == 0)
						&& (num == null || num.compareTo(ZERO) == 0)
						&& (numast == null || numast.compareTo(ZERO) == 0)) {
					// ��д���ִ���
				} else {
					voHead = getOnhandnumHeaderVO(rs);

					if (ngrossnum != null)
						voHead.setNgrossnum(new UFDouble(ngrossnum));
					if (num != null)
						voHead.setNonhandnum(new UFDouble(num));
					if (numast != null)
						voHead.setNonhandastnum(new UFDouble(numast));

					al.add(voHead);
				}

			}
		} finally {
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
			try {
				if (rs != null)
					rs.close();
			} catch (Exception e) {
			}
		}
		return al;
	}

	/**
	 * �ź��� ����:���ִ���[���Ի�λά��] ��ע:ȥ��dvalidate SQL: SELECT pk_corp, ccalbodyid,
	 * cwarehouseid,cinventoryid, vfree1, vfree2, vfree3, vfree4, vfree5,vfree6,
	 * vfree7, vfree8, vfree9, vfree10, vbatchcode, castunitid , cproviderid,hsl
	 * ,coalesce(sum(ninspacenum),0.0)-coalesce(sum(noutspacenum),0.0)
	 * nspacenum,coalesce(sum(ninspaceassistnum),0.0)-coalesce(sum(noutspaceassistnum),0.0)
	 * nspaceassistnum
	 * ,sum(coalesce(ningrossnum,0.0)-coalesce(noutgrossnum,0.0)) ngrossnum from
	 * ic_keep_detail6 a,bd_convert b where a.pk_invbasdoc=b.pk_invbasdoc and
	 * a.castunitid=pk_measdoc and (ninspacenum is not null or noutspacenum is
	 * not null) and isstorebyconvert='Y' group by
	 * pk_corp,ccalbodyid,cwarehouseid,
	 * cinventoryid,vfree1,vfree2,vfree3,vfree4,vfree5,vfree6,vfree7,vfree8,vfree9,vfree10,vbatchcode,castunitid
	 * ,cproviderid,hsl union all SELECT pk_corp, ccalbodyid,
	 * cwarehouseid,cinventoryid, vfree1, vfree2, vfree3, vfree4, vfree5,vfree6,
	 * vfree7, vfree8, vfree9, vfree10, vbatchcode, castunitid , cproviderid
	 * ,case
	 * (coalesce(sum(ninspaceassistnum),0.0)-coalesce(sum(noutspaceassistnum),0.0))
	 * when 0.0 then null else
	 * (coalesce(sum(ninspacenum),0.0)-coalesce(sum(noutspacenum),0.0))/(coalesce(sum(ninspaceassistnum),0.0)-coalesce(sum(noutspaceassistnum),0.0))
	 * end hsl ,coalesce(sum(ninspacenum),0.0)-coalesce(sum(noutspacenum),0.0)
	 * nspacenum,coalesce(sum(ninspaceassistnum),0.0)-coalesce(sum(noutspaceassistnum),0.0)
	 * nspaceassistnum
	 * ,sum(coalesce(ningrossnum,0.0)-coalesce(noutgrossnum,0.0)) ngrossnum from
	 * ic_keep_detail6 a,bd_convert b where a.pk_invbasdoc=b.pk_invbasdoc and
	 * a.castunitid=pk_measdoc and (ninspacenum is not null or noutspacenum is
	 * not null) and isstorebyconvert='N' group by
	 * pk_corp,ccalbodyid,cwarehouseid,
	 * cinventoryid,vfree1,vfree2,vfree3,vfree4,vfree5,vfree6,vfree7,vfree8,vfree9,vfree10,vbatchcode,castunitid
	 * ,cproviderid order by cinventoryid �޸��ˣ������� �޸�ʱ�䣺2008-6-17 ����04:10:50
	 * �޸�ԭ��dbizdate>'"+getDowLoadDate()�޸ĳ�dbizdate>'"+getDowLoadDate()+"-31"����Ȼ���ͳ��ж��������һ���µ�δж�������ݡ�
	 */
	private String getSqlWithoutSpace(ConditionVO[] voaCond,
			QueryType iMonthOrIc) {

		String sSQL = "";
		if (iMonthOrIc == QueryType.Query_No_Download
				|| iMonthOrIc == QueryType.Query_From_Ic
				|| iMonthOrIc == QueryType.Query_Bill) {
			String sSQLBase = "  SELECT a.pk_corp, a.ccalbodyid, a.cwarehouseid,a.cinventoryid,a.cinvbasid, a.vfree1, a.vfree2, a.vfree3, a.vfree4, a.vfree5,a.vfree6, a.vfree7, a.vfree8, a.vfree9, a.vfree10, vbatchcode, castunitid "
					+ ", case when man.ISSUPPLIERSTOCK='Y' or man.ISSUPPLIERSTOCK='1' or wh.isgathersettle='Y' then a.cproviderid else cast(null as char) end as cproviderid "
					+ ", case when bas.isstorebyconvert='Y' or bas.isstorebyconvert='1' then a.hsl else cast(null as decimal) end as hsl "
					+ ", case when bas.ismngstockbygrswt='Y' or bas.ismngstockbygrswt='1' then a.ningrossnum else cast(null as decimal) end as ningrossnum "
					+ ", case when bas.ismngstockbygrswt='Y' or bas.ismngstockbygrswt='1' then a.noutgrossnum else cast(null as decimal) end as noutgrossnum "
					+ ", ninspacenum,noutspacenum,ninspaceassistnum,noutspaceassistnum "
					+ " from ("
					+ FixOnhandnumDMO.ic_keep_detail6
					+ ") a " // "ic_keep_detail6 a "
					+ " inner join bd_invmandoc man on a.CINVENTORYID=man.PK_INVMANDOC inner join bd_invbasdoc bas on man.pk_invbasdoc=bas.pk_invbasdoc "
					+ " inner join bd_stordoc wh on a.cwarehouseid=wh.pk_stordoc "
					+ " where (ninspacenum is not null or noutspacenum is not null) ";

			if (iMonthOrIc == QueryType.Query_Bill)
				sSQLBase = sSQLBase + " and a.cgeneralhid= ? ";
			// v5
			if (iMonthOrIc == QueryType.Query_From_Ic) {
				sSQLBase = sSQLBase + "and a.dbizdate>'" + getDowLoadDate()
						+ "-31" + "' and a.cbilltypecode not in (" + IBillType.MONTH_INITIAL + ") ";
			}
			String sFrees = ", vfree1, vfree2, vfree3, vfree4, vfree5,vfree6, vfree7, vfree8, vfree9, vfree10 ";
			String sNum = "coalesce(sum(ninspacenum),0.0)-coalesce(sum(noutspacenum),0.0) ";
			String sAstNum = "coalesce(sum(ninspaceassistnum),0.0)-coalesce(sum(noutspaceassistnum),0.0)";
			String sGrossNum = "sum(coalesce(ningrossnum,0.0)-coalesce(noutgrossnum,0.0)) ";
			String sNums = "," + sNum + " as nspacenum," + sAstNum
					+ " as nspaceassistnum, " + sGrossNum + " as ngrossnum  ";
			StringBuffer sbWhere = getWhere(voaCond);
			String sFrom = " from (" + sSQLBase + ") v ";
			String sSelect = "SELECT pk_corp,ccalbodyid,cwarehouseid,cinventoryid,cinvbasid,vbatchcode,castunitid,cproviderid,hsl "
					+ sFrees + sNums;

			String sGroupBy = " group by pk_corp,ccalbodyid,cwarehouseid,cinventoryid,cinvbasid,vbatchcode,castunitid,cproviderid,hsl "
					+ sFrees;

			sSQL = sSelect + sFrom;
			if (sbWhere != null) {
				sSQL += " where 1=1 " + sbWhere.toString();
			}
			sSQL += sGroupBy;
		} else {

			String whereBillType = SqlHelper.replace(
					SqlMonthSum.where_xcl_billtype_rec, "cbilltypecode",
					"a.cbilltypecode");
			String sSQLBase = "  SELECT a.pk_corp, a.pk_calbody as ccalbodyid, a.cwarehouseid,a.cinventoryid,a.cinvbasid, a.vfree1, a.vfree2, a.vfree3, a.vfree4, a.vfree5,a.vfree6, a.vfree7, a.vfree8, a.vfree9, a.vfree10, vbatchcode, castunitid "
					+ ", case when man.ISSUPPLIERSTOCK='Y' or man.ISSUPPLIERSTOCK='1' or wh.isgathersettle='Y' then a.cvendorid else cast(null as char) end as cproviderid "
					+ ", case when bas.isstorebyconvert='Y' or bas.isstorebyconvert='1' then a.hsl else cast(null as decimal) end as hsl "
					+ ", case when bas.ismngstockbygrswt='Y' or bas.ismngstockbygrswt='1' then a.ningrossnum else cast(null as decimal) end as ningrossnum "
					+ ", case when bas.ismngstockbygrswt='Y' or bas.ismngstockbygrswt='1' then a.noutgrossnum else cast(null as decimal) end as noutgrossnum "
					+ ", ninnum,noutnum,ninassistnum,noutassistnum "
					+ " from ic_month_record a inner join bd_invmandoc man on a.CINVENTORYID=man.PK_INVMANDOC inner join bd_invbasdoc bas on man.pk_invbasdoc=bas.pk_invbasdoc "
					+ " inner join bd_stordoc wh on a.cwarehouseid=wh.pk_stordoc "
					+ " where (ninnum is not null or noutnum is not null) "
					+ whereBillType + // "and a.cspaceid is null " +
					" and a.dyearmonth<='" + getDowLoadDate() + "' ";

			String sFrees = ", vfree1, vfree2, vfree3, vfree4, vfree5,vfree6, vfree7, vfree8, vfree9, vfree10 ";
			String sNum = "coalesce(sum(ninnum),0.0)-coalesce(sum(noutnum),0.0) ";
			String sAstNum = "coalesce(sum(ninassistnum),0.0)-coalesce(sum(noutassistnum),0.0)";
			String sGrossNum = "sum(coalesce(ningrossnum,0.0)-coalesce(noutgrossnum,0.0)) ";
			String sNums = "," + sNum + " as nspacenum," + sAstNum
					+ " as nspaceassistnum, " + sGrossNum + " as ngrossnum ";
			String sbWhere = getWhere(voaCond).toString();//

			String sFrom = " from (" + sSQLBase + ") v ";
			String sSelect = "SELECT pk_corp,ccalbodyid,cwarehouseid,cinventoryid,cinvbasid,vbatchcode,castunitid,cproviderid,hsl "
					+ sFrees + sNums;

			String sGroupBy = " group by pk_corp,ccalbodyid,cwarehouseid,cinventoryid,cinvbasid,vbatchcode,castunitid,cproviderid,hsl "
					+ sFrees;

			sSQL = sSelect + sFrom;
			if (sbWhere != null) {

				sSQL += " where 1=1 " + sbWhere.toString();
			}
			sSQL += sGroupBy;

		}
		return sSQL;
	}

	/**
	 * �ź��� ����:������� ��ע:1.�����������κŵ���,�ִ�������ά��dvalidate,�ʲ���Ҫѡ����
	 * 2.�������Ϊ:�������ʼǽ��Ͳ��������ʼǽ������ SQL:select pk_corp,pk_calbody as
	 * ccalbodyid,cwarehouseid,cinventoryid , vfree1,vfree2,vfree3,vfree4,vfree5
	 * ,vfree6,vfree7,vfree8,vfree9,vfree10 ,vbatchcode,castunitid
	 * ,cproviderid,hsl ,SUM(isnull(ninnum,0) - isnull(transnum,0) -
	 * isnull(ljhhnum, 0)) AS nnum1,SUM(isnull(ninassistnum,0) -
	 * isnull(transastnum,0) - isnull(ljhhastnum, 0)) AS nastnum1
	 * ,sum(coalesce(ningrossnum,0.0)-coalesce(noutgrossnum,0.0)) ngrossnum from (
	 * SELECT h.pk_corp, h.pk_calbody, h.cbilltypecode, h.vbillcode,
	 * h.cgeneralhid, h.daccountdate, h.cwarehouseid, b.cinventoryid, b.vfree1,
	 * b.vfree2, b.vfree3, b.vfree4, b.vfree5, b.vfree6, b.vfree7, b.vfree8,
	 * b.vfree9, b.vfree10, b.vbatchcode, b.castunitid, b.dbizdate, b.cvendorid
	 * as cproviderid, b.ninnum, b.ninassistnum, b.noutnum,b.noutassistnum,
	 * b.nretnum AS ljhhnum, b.nretastnum AS ljhhastnum, b.ntranoutnum AS
	 * transnum, b.ntranoutastnum AS transastnum, 0.0 AS restnum, 0.0 AS
	 * restastnum, b.nprice, h.fbillflag, b.hsl,b.ningrossnum, b.noutgrossnum
	 * FROM ic_general_h h INNER JOIN ic_general_b b ON h.cgeneralhid =
	 * b.cgeneralhid WHERE (h.cbilltypecode in('49','41','4J')) AND (h.dr = 0)
	 * AND (b.dr = 0) )borrow --v_ic_borrow_refbook where ninnum is not null
	 * group by pk_corp,pk_calbody,cwarehouseid,cinventoryid ,
	 * vfree1,vfree2,vfree3,vfree4,vfree5
	 * ,vfree6,vfree7,vfree8,vfree9,vfree10,vbatchcode,castunitid,cproviderid,hsl
	 */
	private ArrayList queryBorrow(ConditionVO[] voaCond, QueryType iQueryMode,
			String cbillid) throws Exception {
		ArrayList al = new ArrayList();
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		OnhandnumHeaderVO voHead = null;
		String borrowSql = getSqlBorrow(voaCond, iQueryMode);

		try {
			con = getConnection();
			stmt = con.prepareStatement(borrowSql.toString());
			if (iQueryMode == QueryType.Query_Bill) {
				stmt.setString(1, cbillid);
			}
			rs = stmt.executeQuery();
			nc.bs.ic.pub.GenMethod.setResultSetNoRowNumLimit(rs);
			while (rs.next()) {
				BigDecimal num = rs.getBigDecimal("nnum1");
				BigDecimal numast = rs.getBigDecimal("nastnum1");
				BigDecimal ngrossnum = rs.getBigDecimal("ngrossnum1");
				if ((ngrossnum == null || ngrossnum.compareTo(ZERO) == 0)
						&& (num == null || num.compareTo(ZERO) == 0)
						&& (numast == null || numast.compareTo(ZERO) == 0)) {
					// ��д���ִ���
				} else {
					voHead = getOnhandnumHeaderVO(rs);

					if (num != null)
						voHead.setNnum1(new UFDouble(num));

					if (numast != null)
						voHead.setNastnum1(new UFDouble(numast));

					if (ngrossnum != null)
						voHead.setNgrossnum1(new UFDouble(ngrossnum));

					al.add(voHead);
				}
			}
		} finally {
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
			try {
				if (rs != null)
					rs.close();
			} catch (Exception e) {
			}
		}
		return al;
	}

	/**
	 * �޸��ˣ������� �޸�ʱ�䣺2008-6-17 ����04:10:50
	 * �޸�ԭ��dbizdate>'"+getDowLoadDate()�޸ĳ�dbizdate>'"+getDowLoadDate()+"-31"����Ȼ���ͳ��ж��������һ���µ�δж�������ݡ�
	 * 
	 * @param voaCond
	 * @param iQueryMode
	 * @return
	 */
	private String getSqlBorrow(ConditionVO[] voaCond, QueryType iQueryMode) {
		String sSQL = "";
		if (iQueryMode == QueryType.Query_No_Download
				|| iQueryMode == QueryType.Query_From_Ic
				|| iQueryMode == QueryType.Query_Bill) {

			String sSQLBase = " select a.pk_corp,a.pk_calbody ,a.cwarehouseid,a.cinventoryid,a.cinvbasid,a.vbatchcode,a.castunitid  "
					+ ",case when man.ISSUPPLIERSTOCK='Y' or man.ISSUPPLIERSTOCK='1' or wh.isgathersettle='Y' then a.cproviderid else CAST(NULL AS char) end as cproviderid"
					+ ",case when bas.isstorebyconvert='Y' or bas.isstorebyconvert='1' then a.hsl else CAST(NULL AS decimal) end as hsl "
					+ ",case when bas.ismngstockbygrswt='Y' or bas.ismngstockbygrswt='1' then a.ningrossnum else CAST(NULL AS decimal) end as ningrossnum "
					+ ",case when bas.ismngstockbygrswt='Y' or bas.ismngstockbygrswt='1' then a.noutgrossnum else CAST(NULL AS decimal) end as noutgrossnum "
					+ ", a.vfree1,a.vfree2,a.vfree3,a.vfree4,a.vfree5 ,a.vfree6,a.vfree7,a.vfree8,a.vfree9,a.vfree10 "
					+ ",ninnum,ninassistnum,transnum,transastnum,ljhhnum,ljhhastnum  "
					+ " from ("
					+ GeneralSqlString.v_ic_borrow_refbook
					+ ") a inner join bd_invmandoc man on a.cinventoryid=man.pk_invmandoc inner join bd_invbasdoc bas on man.pk_invbasdoc=bas.pk_invbasdoc "
					+ " inner join bd_stordoc wh on a.cwarehouseid=wh.pk_stordoc "
					+ " where ninnum is not null ";

			if (iQueryMode == QueryType.Query_Bill) {
				sSQLBase = sSQLBase + " and a.cgeneralhid=? ";
			}

			if (iQueryMode == QueryType.Query_From_Ic) {
				sSQLBase = sSQLBase + " and a.dbizdate>'" + getDowLoadDate()
						+ "-31" + "' and a.cbilltypecode not in (" + IBillType.MONTH_INITIAL + ") ";
			}

			String sNum = " SUM(isnull(ninnum,0.0) - isnull(transnum,0.0) - isnull(ljhhnum, 0.0)) ";
			String sAstNum = " SUM(isnull(ninassistnum,0.0) - isnull(transastnum,0.0) - isnull(ljhhastnum, 0.0)) ";
			String sGrossNum = " sum(coalesce(ningrossnum,0.0)-coalesce(noutgrossnum,0.0)) ";
			String sNums = "," + sNum + " AS nnum1," + sAstNum
					+ " AS nastnum1, " + sGrossNum + " as ngrossnum1 ";
			String sSelect = "select pk_corp,pk_calbody as ccalbodyid,cwarehouseid,cinventoryid,cinvbasid ,vbatchcode,castunitid ,cproviderid,hsl "
					+ m_vfrees + sNums;
			String sFrom = " FROM (" + sSQLBase + ") v  ";
			String sGroupBy = " group by pk_corp,pk_calbody,cwarehouseid,cinventoryid,cinvbasid ,vbatchcode,castunitid,cproviderid, hsl"
					+ m_vfrees;

			StringBuffer sbWhere = getWhere(voaCond);
			String sWhere = " where 1=1 ";
			if (sbWhere.length() > 0) {
				sWhere += sbWhere.toString();
				sWhere = sWhere.replaceAll("ccalbodyid", "pk_calbody");
			}
			sSQL = sSelect + sFrom + sWhere + sGroupBy;

		} else {

			// v5
			String sSQLBase = " select a.pk_corp,a.pk_calbody as ccalbodyid ,a.cwarehouseid,a.cinventoryid,a.cinvbasid,a.vbatchcode,a.castunitid  "
					+ ",case when man.ISSUPPLIERSTOCK='Y' or man.ISSUPPLIERSTOCK='1' or wh.isgathersettle='Y' then a.cvendorid else CAST(NULL AS char) end as cproviderid"
					+ ",case when bas.isstorebyconvert='Y' or bas.isstorebyconvert='1' then a.hsl else CAST(NULL AS decimal) end as hsl "
					+ ",case when bas.ismngstockbygrswt='Y' or bas.ismngstockbygrswt='1' then a.ningrossnum else CAST(NULL AS decimal) end as ningrossnum "
					+ ",case when bas.ismngstockbygrswt='Y' or bas.ismngstockbygrswt='1' then a.noutgrossnum else CAST(NULL AS decimal) end as noutgrossnum "
					+ ", a.vfree1,a.vfree2,a.vfree3,a.vfree4,a.vfree5 ,a.vfree6,a.vfree7,a.vfree8,a.vfree9,a.vfree10 "
					+ ",ninnum,ninassistnum,ntranoutnum,ntranoutastnum,nretnum,nretastnum  "
					+ " from ic_month_record a inner join bd_invmandoc man on a.cinventoryid=man.pk_invmandoc inner join bd_invbasdoc bas on man.pk_invbasdoc=bas.pk_invbasdoc "
					+ " inner join bd_stordoc wh on a.cwarehouseid=wh.pk_stordoc "
					+ " where ninnum is not null "
					+ " and a.cbilltypecode in ("
					+ IBillType.BOOK_BORROW
					+ ") " + " and dyearmonth<='" + getDowLoadDate() + "' ";

			String sNum = " SUM(isnull(ninnum,0.0) - isnull(ntranoutnum,0.0) - isnull(nretnum, 0.0)) ";
			String sAstNum = " SUM(isnull(ninassistnum,0.0) - isnull(ntranoutastnum,0.0) - isnull(nretastnum, 0.0)) ";
			String sGrossNum = " sum(coalesce(ningrossnum,0.0)-coalesce(noutgrossnum,0.0)) ";
			String sNums = "," + sNum + " AS nnum1," + sAstNum
					+ " AS nastnum1, " + sGrossNum + " as ngrossnum1 ";
			String sSelect = "select pk_corp, ccalbodyid,cwarehouseid,cinventoryid,cinvbasid ,vbatchcode,castunitid ,cproviderid,hsl "
					+ m_vfrees + sNums;
			String sFrom = " FROM (" + sSQLBase + ") v  ";
			String sGroupBy = " group by pk_corp, ccalbodyid ,cwarehouseid,cinventoryid,cinvbasid ,vbatchcode,castunitid,cproviderid, hsl"
					+ m_vfrees;

			StringBuffer sbWhere = getWhere(voaCond);
			String sWhere = " where 1=1 ";
			if (sbWhere.length() > 0) {
				sWhere += sbWhere.toString();

			}
			sSQL = sSelect + sFrom + sWhere + sGroupBy;

		}
		return sSQL;
	}

	/**
	 * �ź��� ����:������ ��ע:�㷨�����ڲ������ SQL:
	 */
	private ArrayList queryLend(ConditionVO[] voaCond, QueryType iQueryMode,
			String cbillid) throws Exception {
		ArrayList al = new ArrayList();
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		OnhandnumHeaderVO voHead = null;

		String lendSql = getSqlLend(voaCond, iQueryMode);
		try {
			con = getConnection();
			stmt = con.prepareStatement(lendSql.toString());
			if (iQueryMode == QueryType.Query_Bill) {
				stmt.setString(1, cbillid);
			}
			rs = stmt.executeQuery();
			nc.bs.ic.pub.GenMethod.setResultSetNoRowNumLimit(rs);
			while (rs.next()) {
				BigDecimal num = rs.getBigDecimal("nnum2");
				BigDecimal numast = rs.getBigDecimal("nastnum2");
				BigDecimal ngrossnum = rs.getBigDecimal("ngrossnum2");

				if ((ngrossnum == null || ngrossnum.compareTo(ZERO) == 0)
						&& (num == null || num.compareTo(ZERO) == 0)
						&& (numast == null || numast.compareTo(ZERO) == 0)) {
					// ��д���ִ���
				} else {
					voHead = getOnhandnumHeaderVO(rs);
					if (num != null)
						voHead.setNnum2(new UFDouble(num));
					if (numast != null)
						voHead.setNastnum2(new UFDouble(numast));
					if (ngrossnum != null)
						voHead.setNgrossnum2(new UFDouble(ngrossnum));

					al.add(voHead);
				}

			}
		} finally {
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
			try {
				if (rs != null)
					rs.close();
			} catch (Exception e) {
			}
		}
		return al;
	}

	/**
	 * �޸��ˣ������� �޸�ʱ�䣺2008-6-17 ����04:10:50
	 * �޸�ԭ��dbizdate>'"+getDowLoadDate()�޸ĳ�dbizdate>'"+getDowLoadDate()+"-31"����Ȼ���ͳ��ж��������һ���µ�δж�������ݡ�
	 * 
	 * @param voaCond
	 * @param iQueryMode
	 * @return
	 */
	private String getSqlLend(ConditionVO[] voaCond, QueryType iQueryMode) {
		String sSQL = "";

		if (iQueryMode == QueryType.Query_No_Download
				|| iQueryMode == QueryType.Query_From_Ic
				|| iQueryMode == QueryType.Query_Bill) {

			String sSQLBase = " select a.pk_corp,a.pk_calbody,a.cwarehouseid,a.cinventoryid,a.cinvbasid ,a.vbatchcode,a.castunitid "
					+ ",case when man.ISSUPPLIERSTOCK='Y' or man.ISSUPPLIERSTOCK='1' or wh.isgathersettle='Y' then a.cproviderid else CAST(NULL AS char) end as cproviderid"
					+ ",case when bas.isstorebyconvert='Y' or bas.isstorebyconvert='1' then a.hsl else CAST(NULL AS decimal) end as hsl "
					+ ",case when bas.ismngstockbygrswt='Y' or bas.ismngstockbygrswt='1' then a.ningrossnum else CAST(NULL AS decimal) end as ningrossnum "
					+ ",case when bas.ismngstockbygrswt='Y' or bas.ismngstockbygrswt='1' then a.noutgrossnum else CAST(NULL AS decimal) end as noutgrossnum "
					+ ", a.vfree1,a.vfree2,a.vfree3,a.vfree4,a.vfree5 ,a.vfree6,a.vfree7,a.vfree8,a.vfree9,a.vfree10 "
					+ ",noutnum,noutassistnum,transnum,transastnum,ljhhnum,ljhhastnum "
					+ " from ("
					+ GeneralSqlString.v_ic_lend_refbook
					+ ") a "
					+ " inner join bd_invmandoc man on a.cinventoryid=man.pk_invmandoc inner join bd_invbasdoc bas on man.pk_invbasdoc=bas.pk_invbasdoc "
					+ " inner join bd_stordoc wh on a.cwarehouseid=wh.pk_stordoc "
					+ " where noutnum is not null ";

			if (iQueryMode == QueryType.Query_Bill) {
				sSQLBase = sSQLBase + " and a.cgeneralhid = ? ";
			}

			if (iQueryMode == QueryType.Query_From_Ic) {
				sSQLBase = sSQLBase + " and a.dbizdate>'" + getDowLoadDate()
						+ "-31" + "' and a.cbilltypecode not in (" + IBillType.MONTH_INITIAL + ") ";
			}

			String sNum = " SUM(isnull(noutnum,0.0) - isnull(transnum,0.0) - isnull(ljhhnum, 0.0)) ";
			String sAstNum = " SUM(isnull(noutassistnum,0.0) - isnull(transastnum,0.0) - isnull(ljhhastnum, 0.0)) ";
			String sGrossNum = " sum(coalesce(ningrossnum,0.0)-coalesce(noutgrossnum,0.0)) ";
			String sNums = "," + sNum + " AS nnum2," + sAstNum
					+ " AS nastnum2, " + sGrossNum + " as ngrossnum2 ";

			String sSelect = "select pk_corp,pk_calbody as ccalbodyid,cwarehouseid,cinventoryid,cinvbasid ,vbatchcode,castunitid ,cproviderid,hsl  "
					+ m_vfrees + sNums;
			String sFrom = " FROM (" + sSQLBase + ") v  ";
			String sGroupBy = " group by pk_corp,pk_calbody,cwarehouseid,cinventoryid,cinvbasid ,vbatchcode,castunitid,cproviderid,hsl "
					+ m_vfrees;

			StringBuffer sbWhere = getWhere(voaCond);
			String sWhere = " where 1=1 ";
			if (sbWhere.length() > 0) {
				sWhere += sbWhere.toString();
				sWhere = sWhere.replaceAll("ccalbodyid", "pk_calbody");
			}
			sSQL = sSelect + sFrom + sWhere + sGroupBy;

		}

		else {

			String sSQLBase = " select a.pk_corp,a.pk_calbody as ccalbodyid,a.cwarehouseid,a.cinventoryid,a.cinvbasid ,a.vbatchcode,a.castunitid "
					+ ",case when man.ISSUPPLIERSTOCK='Y' or man.ISSUPPLIERSTOCK='1' or wh.isgathersettle='Y' then a.cvendorid else CAST(NULL AS char) end as cproviderid"
					+ ",case when bas.isstorebyconvert='Y' or bas.isstorebyconvert='1' then a.hsl else CAST(NULL AS decimal) end as hsl "
					+ ",case when bas.ismngstockbygrswt='Y' or bas.ismngstockbygrswt='1' then a.ningrossnum else CAST(NULL AS decimal) end as ningrossnum "
					+ ",case when bas.ismngstockbygrswt='Y' or bas.ismngstockbygrswt='1' then a.noutgrossnum else CAST(NULL AS decimal) end as noutgrossnum "
					+ ", a.vfree1,a.vfree2,a.vfree3,a.vfree4,a.vfree5 ,a.vfree6,a.vfree7,a.vfree8,a.vfree9,a.vfree10 "
					+ ",noutnum,noutassistnum,ntranoutnum,ntranoutastnum, nretnum, nretastnum "
					+ " from ic_month_record  a "
					+ " inner join bd_invmandoc man on a.cinventoryid=man.pk_invmandoc inner join bd_invbasdoc bas on man.pk_invbasdoc=bas.pk_invbasdoc "
					+ " inner join bd_stordoc wh on a.cwarehouseid=wh.pk_stordoc "
					+ " where noutnum is not null and "
					+ " a.cbilltypecode in ("
					+ IBillType.BOOK_LEND
					+ ")"
					+ " and dyearmonth<='" + getDowLoadDate() + "' ";// v5

			String sNum = " SUM(isnull(noutnum,0.0) - isnull(ntranoutnum,0.0) - isnull(nretnum, 0.0)) ";
			String sAstNum = " SUM(isnull(noutassistnum,0.0) - isnull(ntranoutastnum,0.0) - isnull(nretastnum, 0.0)) ";
			String sGrossNum = " sum(coalesce(ningrossnum,0.0)-coalesce(noutgrossnum,0.0)) ";
			String sNums = "," + sNum + " AS nnum2," + sAstNum
					+ " AS nastnum2, " + sGrossNum + " as ngrossnum2 ";

			String sSelect = "select pk_corp, ccalbodyid,cwarehouseid,cinventoryid,cinvbasid ,vbatchcode,castunitid ,cproviderid,hsl  "
					+ m_vfrees + sNums;
			String sFrom = " FROM (" + sSQLBase + ") v  ";
			String sGroupBy = " group by pk_corp, ccalbodyid ,cwarehouseid,cinventoryid,cinvbasid ,vbatchcode,castunitid,cproviderid,hsl "
					+ m_vfrees;

			StringBuffer sbWhere = getWhere(voaCond);
			String sWhere = " where 1=1 ";
			if (sbWhere.length() > 0) {
				sWhere += sbWhere.toString();

			}
			sSQL = sSelect + sFrom + sWhere + sGroupBy;

		}
		return sSQL;
	}

	public static boolean isOnHandItemVOZero(OnhandnumItemVO itemvo)
			throws Exception {
		UFDouble dtemp = null;
		boolean bret = true;
		for (int i = 0; i < OnhandnumItemVO.onhandbodynumkeys.length; i++) {
			dtemp = CheckTools.toUFDouble(itemvo
					.getAttributeValue(OnhandnumItemVO.onhandbodynumkeys[i]));
			if (nc.vo.ic.pub.GenMethod.isEQZeroOrNull(dtemp)) {
				bret = false;
				break;
			}
		}
		return bret;
	}

	private ArrayList queryWithSpace_v5(ConditionVO[] voaCond,
			QueryType iQueryMode, String cbillid) throws Exception {

		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		OnhandnumItemVO voItem = null;
		ArrayList al = new ArrayList();

		// �õ�SQL:
		String sSql = getSqlWithSpace(voaCond, iQueryMode);
		try {
			con = getConnection();
			stmt = con.prepareStatement(sSql);
			if (iQueryMode == QueryType.Query_Bill) {
				stmt.setString(1, cbillid);
			}
			rs = stmt.executeQuery();
			nc.bs.ic.pub.GenMethod.setResultSetNoRowNumLimit(rs);
			while (rs.next()) {

				voItem = getOnhandItemVO(rs);
				if (isOnHandItemVOZero(voItem))
					continue;
				al.add(voItem);
			}

		} catch (Exception e) {
			throw e;
		} finally {
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
			try {
				if (rs != null)
					rs.close();
			} catch (Exception e) {
			}
		}

		return al;
	}

	/**
	 * �ź��� ����:���λ�ִ���sql ��ע: SQL: �޸��ˣ������� �޸�ʱ�䣺2008-6-17 ����04:10:50
	 * �޸�ԭ��dbizdate>'"+getDowLoadDate()�޸ĳ�dbizdate>'"+getDowLoadDate()+"-31"����Ȼ���ͳ��ж��������һ���µ�δж�������ݡ�
	 */
	private String getSqlWithSpace(ConditionVO[] voaCond, QueryType iQueryMode) {
		StringBuffer sbWhere = getWhere(voaCond);
		String sSQL = "";
		if (iQueryMode == QueryType.Query_No_Download
				|| iQueryMode == QueryType.Query_From_Ic
				|| iQueryMode == QueryType.Query_Bill) {
			String sSQLBase = "select a.pk_corp, a.ccalbodyid, cwarehouseid,a.cspaceid,cinventoryid,cinvbasid, a.vfree1, a.vfree2, a.vfree3, a.vfree4, a.vfree5,a.vfree6, a.vfree7, a.vfree8, a.vfree9, a.vfree10, vbatchcode, castunitid, ninspacenum,noutspacenum,ninspaceassistnum,noutspaceassistnum "
					+ ", case when bas.ismngstockbygrswt='Y' or bas.ismngstockbygrswt='1' then a.ningrossnum else CAST(NULL AS decimal) end as ningrossnum "
					+ ", case when bas.ismngstockbygrswt='Y' or bas.ismngstockbygrswt='1' then a.noutgrossnum else CAST(NULL AS decimal) end as noutgrossnum "
					+ " ,case when man.ISSUPPLIERSTOCK='Y' or man.ISSUPPLIERSTOCK='1' or wh.isgathersettle='Y' then a.cproviderid else CAST(NULL AS char) end as cproviderid "
					+ " ,case when bas.isstorebyconvert='Y' or bas.isstorebyconvert='1' then a.hsl else CAST(NULL AS decimal) end as hsl "
					+ " from ( "
					+ FixOnhandnumDMO.ic_keep_detail6
					+ " ) a " // ic_keep_detail6 a "
					+ "  inner join bd_invmandoc man on a.cinventoryid=man.pk_invmandoc inner join bd_invbasdoc bas on man.pk_invbasdoc=bas.pk_invbasdoc  "
					+ " inner join bd_stordoc wh on a.cwarehouseid=wh.pk_stordoc "
					+ " where cspaceid is not null ";

			if (iQueryMode == QueryType.Query_Bill) // v5
				sSQLBase = sSQLBase + "and a.cgeneralhid = ? ";
			if (iQueryMode == QueryType.Query_From_Ic) // v5
				sSQLBase = sSQLBase + "and dbizdate>'" + getDowLoadDate()
						+ "-31" + "' and a.cbilltypecode not in (" + IBillType.MONTH_INITIAL + ") ";

			if (sbWhere == null)
				sbWhere = new StringBuffer(" where ");

			String sFrees = ", vfree1, vfree2, vfree3, vfree4, vfree5,vfree6, vfree7, vfree8, vfree9, vfree10 ";
			String sNum = "coalesce(sum(ninspacenum),0.0)-coalesce(sum(noutspacenum),0.0)";
			String sAstNum = "coalesce(sum(ninspaceassistnum),0.0)-coalesce(sum(noutspaceassistnum),0.0)";
			String sNums = "," + sNum + " as nspacenum," + sAstNum
					+ " as nspaceassistnum ";
			String sFrom = " from (" + sSQLBase + ") v ";
			String sGroupBy = " group by pk_corp,ccalbodyid,cwarehouseid,cspaceid, cinventoryid,cinvbasid,vbatchcode,castunitid,cproviderid,hsl,vfree1,vfree2,vfree3,vfree4,vfree5,vfree6,vfree7,vfree8,vfree9,vfree10 ";
			String sSelect = " SELECT pk_corp, ccalbodyid, cwarehouseid,cspaceid,cinventoryid,cinvbasid, vbatchcode, castunitid,cproviderid,hsl,sum(coalesce(ningrossnum,0.0)-coalesce(noutgrossnum,0.0)) ngrossnum "
					+ sFrees + sNums;

			sSQL = sSelect + sFrom;
			if (sbWhere != null) {
				sSQL += " where 1=1 " + sbWhere.toString();
			}
			sSQL += sGroupBy;
		}
		// v5
		else {

			String sSQLBase = "select a.pk_corp, a.pk_calbody as ccalbodyid, cwarehouseid,a.cspaceid,cinventoryid,cinvbasid, a.vfree1, a.vfree2, a.vfree3, a.vfree4, a.vfree5,a.vfree6, a.vfree7, a.vfree8, a.vfree9, a.vfree10, vbatchcode, castunitid, ninnum,noutnum,ninassistnum,noutassistnum "
					+ ", case when bas.ismngstockbygrswt='Y' or bas.ismngstockbygrswt='1' then a.ningrossnum else CAST(NULL AS decimal) end as ningrossnum "
					+ ", case when bas.ismngstockbygrswt='Y' or bas.ismngstockbygrswt='1' then a.noutgrossnum else CAST(NULL AS decimal) end as noutgrossnum "
					+ " ,case when man.ISSUPPLIERSTOCK='Y' or man.ISSUPPLIERSTOCK='1' or wh.isgathersettle='Y' then a.cvendorid else CAST(NULL AS char) end as cproviderid "
					+ " ,case when bas.isstorebyconvert='Y' or bas.isstorebyconvert='1' then a.hsl else CAST(NULL AS decimal) end as hsl "
					+ " from ic_month_record a inner join bd_invmandoc man on a.cinventoryid=man.pk_invmandoc inner join bd_invbasdoc bas on man.pk_invbasdoc=bas.pk_invbasdoc "
					+ " inner join bd_stordoc wh on a.cwarehouseid=wh.pk_stordoc "
					+ " where cspaceid is not null  "
					+ SqlMonthSum.where_xcl_billtype_rec
					+ " and dyearmonth<='"
					+ getDowLoadDate() + "' ";// v5

			if (sbWhere == null)
				sbWhere = new StringBuffer(" where ");

			String sFrees = ", vfree1, vfree2, vfree3, vfree4, vfree5,vfree6, vfree7, vfree8, vfree9, vfree10 ";
			String sNum = "coalesce(sum(ninnum),0.0)-coalesce(sum(noutnum),0.0)";
			String sAstNum = "coalesce(sum(ninassistnum),0.0)-coalesce(sum(noutassistnum),0.0)";
			String sNums = "," + sNum + " as nspacenum," + sAstNum
					+ " as nspaceassistnum ";
			String sFrom = " from (" + sSQLBase + ") v ";
			String sGroupBy = " group by pk_corp,ccalbodyid,cwarehouseid,cspaceid, cinventoryid,cinvbasid,vbatchcode,castunitid,cproviderid,hsl,vfree1,vfree2,vfree3,vfree4,vfree5,vfree6,vfree7,vfree8,vfree9,vfree10 ";
			String sSelect = " SELECT pk_corp, ccalbodyid, cwarehouseid,cspaceid,cinventoryid,cinvbasid, vbatchcode, castunitid,cproviderid,hsl,sum(coalesce(ningrossnum,0.0)-coalesce(noutgrossnum,0.0)) ngrossnum "
					+ sFrees + sNums;

			sSQL = sSelect + sFrom;
			if (sbWhere != null) {
				sSQL += " where 1=1 " + sbWhere.toString();
			}
			sSQL += sGroupBy;

		}
		return sSQL;
	}

	/**
	 * ?user> ���ܣ� ������ ���أ� ���⣺ ���ڣ�(2004-11-30 9:43:25) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 * @return boolean
	 * @param vo
	 *            nc.vo.ic.pub.bill.GeneralBillVO
	 */
	private boolean isBorrow(String cbilltypecode) {
		boolean isBL = false;
		if (cbilltypecode != null) {
			IBillType billType = BillTypeFactory.getInstance().getBillType(cbilltypecode);
			if (billType.typeOf(ModuleCode.IC)
				&& (billType.getBookType() == BookType.BORROW
					|| billType.getBookType() == BookType.BORROW_RETURN))
				isBL = true;
		}
		return isBL;
	}

	/**
	 * ?user> ���ܣ� ������ ���أ� ���⣺ ���ڣ�(2004-11-30 9:43:25) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 * @return boolean
	 * @param vo
	 *            nc.vo.ic.pub.bill.GeneralBillVO
	 */
	private boolean isLend(String cbilltypecode) {
		boolean isBL = false;
		if (cbilltypecode != null) {
			IBillType billType = BillTypeFactory.getInstance().getBillType(cbilltypecode);
			if (billType.typeOf(ModuleCode.IC)
					&& (billType.getBookType() == BookType.LEND
						|| billType.getBookType() == BookType.LEND_RETURN))
				isBL = true;
		}
		return isBL;
	}

	/**
	 * �\n�������ڣ�(2003-2-21 9:17:41) ���ߣ������� �޸����ڣ� �޸��ˣ� �޸�ԭ�� �㷨˵����
	 * ˵��������ͼic_keep_detail6�ϻ����ִ�����������Ի�λ���ܡ�
	 * 
	 * @return java.util.Hashtable
	 * @param sSql
	 *            java.lang.String
	 * @exception java.sql.SQLException
	 *                �쳣˵����
	 */
	public ArrayList updateOnHandBegin(GeneralBillVO curbillvo,ICConst.OnHandUpdateType onHandUpdateType)
			throws BusinessException {
		if (curbillvo == null)
			return null;
		ArrayList<ArrayList> retlist = new ArrayList<ArrayList>();
		String cbillid = (String) curbillvo
				.getHeaderValue(IItemKey.CGENERALHID);
		if (cbillid == null)
			return null;
		String cbilltype = (String) curbillvo
				.getHeaderValue(IItemKey.CBILLTYPECODE);
		try {
			if (onHandUpdateType == ICConst.OnHandUpdateType.Onhand_All || onHandUpdateType == ICConst.OnHandUpdateType.Onhand_BillOnly){
				retlist.add(new MonthServ().modifyMonthDataBegin(curbillvo));
				retlist.add(getOnhandByID(curbillvo));
			}else{
				retlist.add(null);
				retlist.add(null);
			}
			if (onHandUpdateType == ICConst.OnHandUpdateType.Onhand_All || onHandUpdateType == ICConst.OnHandUpdateType.Onhand_BarcodeOnly)
				retlist.add(getOnhandBBCByBillHID(cbillid));
			else
				retlist.add(null);
			return retlist;
		} catch (Exception e) {
			throw GenMethod.handleException(null, e);
		}
	}

	/**
	 * �\n�������ڣ�(2003-2-21 9:17:41) ���ߣ������� �޸����ڣ� �޸��ˣ� �޸�ԭ�� �㷨˵����
	 * ˵��������ͼic_keep_detail6�ϻ����ִ�����������Ի�λ���ܡ�
	 * 
	 * @return java.util.Hashtable
	 * @param sSql
	 *            java.lang.String
	 * @exception java.sql.SQLException
	 *                �쳣˵����
	 */
	private ArrayList getOnhandByID(GeneralBillVO curbillvo)
			throws BusinessException {
		if (curbillvo == null)
			return null;
		String cbillid = (String) curbillvo
				.getHeaderValue(IItemKey.CGENERALHID);
		if (cbillid == null)
			return null;// throw new BusinessException("�����ִ�������!");
		String cbilltype = (String) curbillvo
				.getHeaderValue(IItemKey.CBILLTYPECODE);
		try {

			Hashtable hshead = queryWithoutSpace(null, cbillid, cbilltype);

			ArrayList altem = queryWithSpace_v5(null, QueryType.Query_Bill,
					cbillid);

			// ������־
			OnhandnumItemVO[] voNewBodys = null;
			if (altem != null && altem.size() > 0) {
				voNewBodys = new OnhandnumItemVO[altem.size()];
				altem.toArray(voNewBodys);
			}
			Hashtable hsbody = mergeOnHandItemVOsToHs(altem);

			// ��ItemVO���뵽VO��
			ArrayList alVO = getOnhandnumVO(hshead, hsbody);

			return alVO;
		} catch (Exception e) {
			throw GenMethod.handleException(null, e);
		}
	}

	/**
	 * �\n�������ڣ�(2003-2-21 9:17:41) ���ߣ������� �޸����ڣ� �޸��ˣ� �޸�ԭ�� �㷨˵����
	 * ˵��������ͼic_keep_detail6�ϻ����ִ�����������Ի�λ���ܡ�
	 * 
	 * @return java.util.Hashtable
	 * @param sSql
	 *            java.lang.String
	 * @exception java.sql.SQLException
	 *                �쳣˵����
	 */
	private void checkAtpAfterUpdateHand(OnhandnumVO[] vos, String sLogdate,
			boolean isCheckAtp) throws BusinessException {
		if (vos == null || vos.length == 0 || !isCheckAtp)
			return;

		try {
			InvATPDMO dmo2 = new InvATPDMO();

			if (!IICPub_InvATPDMO.IC_UNPASS.equals(dmo2.getICParam(vos[0]
					.getHeaderVO().getPk_corp())))
				return;

			nc.vo.scm.ic.ATPVO voAtp = null;
			UFDouble nonhand = null;
			UFDouble ZERO = new UFDouble(0.0);
			ArrayList alvos = new ArrayList();
			InvOnHandDMO dmo1 = new InvOnHandDMO();

			for (int i = 0; i < vos.length; i++) {
				nonhand = vos[i].getHeaderVO().getNonhandnum();
				if (nonhand == null || nonhand.compareTo(ZERO) >= 0)
					continue;
				if (vos[i].getHeaderVO().getCcalbodyid() == null)
					continue;

				if (!dmo1.isAffectAtp(vos[i].getHeaderVO().getCwarehouseid())
						.booleanValue())
					continue;

				voAtp = new nc.vo.scm.ic.ATPVO();
				voAtp.setPk_corp(vos[i].getHeaderVO().getPk_corp());
				voAtp.setCcalbodyid(vos[i].getHeaderVO().getCcalbodyid());
				voAtp.setCinventoryid(vos[i].getHeaderVO().getCinventoryid());
				voAtp.setVfree(new String[] { vos[i].getHeaderVO().getVfree1(),
						vos[i].getHeaderVO().getVfree2(),
						vos[i].getHeaderVO().getVfree3(),
						vos[i].getHeaderVO().getVfree4(),
						vos[i].getHeaderVO().getVfree5() });
				voAtp.setCwarehouseid(vos[i].getHeaderVO().getCwarehouseid());
				voAtp.setVbatchcode(vos[i].getHeaderVO().getVlot());

				alvos.add(voAtp);

			}
			if (alvos.size() > 0 && isCheckAtp) {

				ATPVO[] voAtps = new ATPVO[alvos.size()];
				alvos.toArray(voAtps);
				nc.bs.ic.pub.InvATPDMO dmo = new nc.bs.ic.pub.InvATPDMO();
				dmo.checkATP(voAtps, sLogdate);
			}
		} catch (Exception e) {
			// ��־�쳣
			nc.vo.scm.pub.SCMEnv.out(e);
			// ������쳣�׳��淶
			throw nc.bs.ic.pub.GenMethod.handleException(e.getMessage(), e);
		}

	}
  
  private OnhandnumVO[] sortOnhandnumVO(OnhandnumVO[] vos){
    if(vos==null || vos.length<=0)
      return null;
           
    ArrayList al = SortMethod.sortByKeys(
        OnhandnumHeaderVO.onhandheadkeys, null,
        SmartVOUtilExt.getHeadVOs(vos));
    if(al!=null && al.size()>0){
      int[] indexs = (int[]) al.get(0);
      if(indexs!=null && indexs.length>0 && indexs.length==vos.length){
        OnhandnumVO[] retvos = new OnhandnumVO[vos.length];
        for(int i=0;i<indexs.length;i++)
          retvos[i] = vos[indexs[i]];
        return retvos;
      }
    }
    return vos;
  }
      

	/**
	 * �\n�������ڣ�(2003-2-21 9:17:41) ���ߣ������� �޸����ڣ� �޸��ˣ� �޸�ԭ�� �㷨˵����
	 * ˵��������ͼic_keep_detail6�ϻ����ִ�����������Ի�λ���ܡ�
	 * 
	 * @return java.util.Hashtable
	 * @param sSql
	 *            java.lang.String
	 * @exception java.sql.SQLException
	 *                �쳣˵����
	 */
	public void updateOnHandEnd(GeneralBillVO curbillvo,
			GeneralBillVO oldbillvo, ArrayList prelist,ICConst.OnHandUpdateType onHandUpdateType)
			throws BusinessException {

		// month calculate
		// MonthServ ms = null;
		// try {
		// ms = new MonthServ();
		// ms.modifyMonthData(curbillvo,oldbillvo);
		// }
		// catch (Exception e) {
		// //��־�쳣
		// nc.vo.scm.pub.SCMEnv.out(e);
		// //������쳣�׳��淶
		// throw nc.bs.ic.pub.GenMethod.handleException(e.getMessage(), e);
		// }
		if (curbillvo == null)
			curbillvo = oldbillvo;

		if (curbillvo == null)
			return;

		ArrayList preinfo = null;
		ArrayList monthpreinfo = null;
		ArrayList preBBCinfo = null;
		if (prelist != null && prelist.size() >= 2) {
			if (onHandUpdateType == ICConst.OnHandUpdateType.Onhand_All || onHandUpdateType == ICConst.OnHandUpdateType.Onhand_BillOnly){
				monthpreinfo = (ArrayList) prelist.get(0);
				preinfo = (ArrayList) prelist.get(1);
			}
			if (onHandUpdateType == ICConst.OnHandUpdateType.Onhand_All || onHandUpdateType == ICConst.OnHandUpdateType.Onhand_BarcodeOnly)
				preBBCinfo = (ArrayList) prelist.get(2);
		}
		
		
		String cbillid = (String) curbillvo
		.getHeaderValue(IItemKey.CGENERALHID);
		if (cbillid == null)
			throw new BusinessException("�����ִ�������!");
		try {
			UFDouble d_1 = new UFDouble(-1);
			
			if (onHandUpdateType == ICConst.OnHandUpdateType.Onhand_All || onHandUpdateType == ICConst.OnHandUpdateType.Onhand_BillOnly){
				
				//�����½�����
				MonthServ ms = null;
				ms = new MonthServ();
				ms.modifyMonthDataEnd(curbillvo, monthpreinfo);
						
				//���µ����ִ���		
				ArrayList<OnhandnumHeaderVO> headlist = new ArrayList<OnhandnumHeaderVO>();
				ArrayList<OnhandnumItemVO> bodylist = new ArrayList<OnhandnumItemVO>();
		
				OnhandnumVO handvo = null;
				OnhandnumHeaderVO headhandvo = null;
				OnhandnumItemVO[] bodyhandvos = null;

				if (preinfo != null && preinfo.size() > 0) {
					for (int i = 0; i < preinfo.size(); i++) {
						handvo = (OnhandnumVO) preinfo.get(i);
						headhandvo = handvo.getHeaderVO();
						bodyhandvos = handvo.getItemVOs();
						headhandvo.setNonhandnum(SmartVOUtilExt.mult(d_1, headhandvo
								.getNonhandnum()));
						headhandvo.setNonhandastnum(SmartVOUtilExt.mult(d_1, headhandvo
								.getNonhandastnum()));
						headhandvo.setNgrossnum(SmartVOUtilExt.mult(d_1, headhandvo
								.getNgrossnum()));
						headhandvo.setNnum1(SmartVOUtilExt.mult(d_1, headhandvo
								.getNnum1()));
						headhandvo.setNastnum1(SmartVOUtilExt.mult(d_1, headhandvo
								.getNastnum1()));
						headhandvo.setNgrossnum1(SmartVOUtilExt.mult(d_1, headhandvo
								.getNgrossnum1()));
						headhandvo.setNnum2(SmartVOUtilExt.mult(d_1, headhandvo
								.getNnum2()));
						headhandvo.setNastnum2(SmartVOUtilExt.mult(d_1, headhandvo
								.getNastnum2()));
						headhandvo.setNgrossnum2(SmartVOUtilExt.mult(d_1, headhandvo
								.getNgrossnum2()));
						headlist.add(headhandvo);
						if (bodyhandvos != null && bodyhandvos.length > 0) {
							for (int k = 0; k < bodyhandvos.length; k++) {
								bodyhandvos[k].setNnum(SmartVOUtilExt.mult(d_1,
										bodyhandvos[k].getNnum()));
								bodyhandvos[k].setNastnum(SmartVOUtilExt.mult(d_1,
										bodyhandvos[k].getNastnum()));
								bodyhandvos[k].setNgrossnum(SmartVOUtilExt.mult(d_1,
										bodyhandvos[k].getNgrossnum()));
								bodyhandvos[k].setNnum1(SmartVOUtilExt.mult(d_1,
										bodyhandvos[k].getNnum1()));
								bodyhandvos[k].setNastnum1(SmartVOUtilExt.mult(d_1,
										bodyhandvos[k].getNastnum1()));
								bodyhandvos[k].setNgrossnum1(SmartVOUtilExt.mult(d_1,
										bodyhandvos[k].getNgrossnum1()));
								bodyhandvos[k].setNnum2(SmartVOUtilExt.mult(d_1,
										bodyhandvos[k].getNnum2()));
								bodyhandvos[k].setNastnum2(SmartVOUtilExt.mult(d_1,
										bodyhandvos[k].getNastnum2()));
								bodyhandvos[k].setNgrossnum2(SmartVOUtilExt.mult(d_1,
										bodyhandvos[k].getNgrossnum2()));
								bodylist.add(bodyhandvos[k]);
							}
						}
					}
				}
						
		
	
				ArrayList alcur = getOnhandByID(curbillvo);



				if (alcur != null && alcur.size() > 0) {
					for (int i = 0; i < alcur.size(); i++) {
						handvo = (OnhandnumVO) alcur.get(i);
						headhandvo = handvo.getHeaderVO();
						bodyhandvos = handvo.getItemVOs();
						headlist.add(headhandvo);
						if (bodyhandvos != null && bodyhandvos.length > 0) {
							for (int k = 0; k < bodyhandvos.length; k++) {
								bodylist.add(bodyhandvos[k]);
							}
						}
					}
				}



				if (headlist == null || headlist.size() < 0)
					return;

				Hashtable hthead = mergeOnHandVOs(headlist);

				bodylist = mergeOnHandItemVOs(bodylist);

				Hashtable htbody = mergeOnHandItemVOsToHs(bodylist);

				// ��ItemVO���뵽VO��
				ArrayList alVO = getOnhandnumVO(hthead, htbody);
				OnhandnumVO[] vos = new OnhandnumVO[alVO.size()];
				alVO.toArray(vos);
				
				vos = sortOnhandnumVO(vos);
				new OnhandnumDMO().modifyOnhandNumDirectly(vos);



				// �򵥴���
				String sLogdate = null;
				boolean isCheckAtp = true;
				if (curbillvo != null && curbillvo.getItemVOs() != null
						&& curbillvo.getItemVOs().length > 0) {
					sLogdate = curbillvo.getHeaderVO().getClogdatenow();
					if (curbillvo.getItemVOs()[0].getAttributeValue("ischeckatp") != null
							&& !((UFBoolean) curbillvo.getItemVOs()[0]
									.getAttributeValue("ischeckatp"))
									.booleanValue())
						isCheckAtp = false;

					String csourcetype = curbillvo.getItemVOs()[0].getCsourcetype();
					IBillType billType = BillTypeFactory.getInstance().getBillType(csourcetype);
					if (csourcetype != null
							&& !billType.typeOf(ModuleCode.IC))
						isCheckAtp = false;
				}
				checkAtpAfterUpdateHand(vos, sLogdate, isCheckAtp);
	
				
			}
			
			if (onHandUpdateType == ICConst.OnHandUpdateType.Onhand_All || onHandUpdateType == ICConst.OnHandUpdateType.Onhand_BarcodeOnly){
				//���������ִ���
				ArrayList<OnhandnumBBCVO> handbbclist = new ArrayList<OnhandnumBBCVO>();
				OnhandnumBBCVO handbbcvo = null;
				if (preBBCinfo != null && preBBCinfo.size() > 0) {
					for (int i = 0; i < preBBCinfo.size(); i++) {
						handbbcvo = (OnhandnumBBCVO) preBBCinfo.get(i);
						handbbcvo.setNonhandnum(SmartVOUtilExt.mult(d_1, handbbcvo
								.getNonhandnum()));
						handbbclist.add(handbbcvo);
					}
				}
				
				ArrayList<OnhandnumBBCVO> alcurbbc = getOnhandBBCByBillHID(curbillvo
						.getHeaderVO().getCgeneralhid());
				
				
				if (alcurbbc != null && alcurbbc.size() > 0) {
					for (int i = 0; i < alcurbbc.size(); i++) {
						handbbcvo = (OnhandnumBBCVO) alcurbbc.get(i);
						handbbclist.add(handbbcvo);
					}
				}
				
				
				if (null != handbbclist && handbbclist.size() > 0) {
					OnhandnumBBCVO[] vobbcs = mergeOnHandBBCVOs(handbbclist);
					new OnhandnumDMO().modifyOnhandBBCNumDirectly(vobbcs);
				}
				
			}
		
		} catch (Exception e) {
			throw GenMethod.handleException(null, e);
		}

/*		try {

			ArrayList alcur = getOnhandByID(curbillvo);

			ArrayList<OnhandnumBBCVO> alcurbbc = getOnhandBBCByBillHID(curbillvo
					.getHeaderVO().getCgeneralhid());

			if (alcur != null && alcur.size() > 0) {
				for (int i = 0; i < alcur.size(); i++) {
					handvo = (OnhandnumVO) alcur.get(i);
					headhandvo = handvo.getHeaderVO();
					bodyhandvos = handvo.getItemVOs();
					headlist.add(headhandvo);
					if (bodyhandvos != null && bodyhandvos.length > 0) {
						for (int k = 0; k < bodyhandvos.length; k++) {
							bodylist.add(bodyhandvos[k]);
						}
					}
				}
			}

			if (alcurbbc != null && alcurbbc.size() > 0) {
				for (int i = 0; i < alcurbbc.size(); i++) {
					handbbcvo = (OnhandnumBBCVO) alcurbbc.get(i);
					handbbclist.add(handbbcvo);
				}
			}

			if (headlist == null || headlist.size() < 0)
				return;

			Hashtable hthead = mergeOnHandVOs(headlist);

			bodylist = mergeOnHandItemVOs(bodylist);

			Hashtable htbody = mergeOnHandItemVOsToHs(bodylist);

			// ��ItemVO���뵽VO��
			ArrayList alVO = getOnhandnumVO(hthead, htbody);
			OnhandnumVO[] vos = new OnhandnumVO[alVO.size()];
			alVO.toArray(vos);
			
			vos = sortOnhandnumVO(vos);
			new OnhandnumDMO().modifyOnhandNumDirectly(vos);

			if (null != handbbclist && handbbclist.size() > 0) {
				OnhandnumBBCVO[] vobbcs = mergeOnHandBBCVOs(handbbclist);
				new OnhandnumDMO().modifyOnhandBBCNumDirectly(vobbcs);
			}

			// �򵥴���
			String sLogdate = null;
			boolean isCheckAtp = true;
			if (curbillvo != null && curbillvo.getItemVOs() != null
					&& curbillvo.getItemVOs().length > 0) {
				sLogdate = curbillvo.getHeaderVO().getClogdatenow();
				if (curbillvo.getItemVOs()[0].getAttributeValue("ischeckatp") != null
						&& !((UFBoolean) curbillvo.getItemVOs()[0]
								.getAttributeValue("ischeckatp"))
								.booleanValue())
					isCheckAtp = false;

				String csourcetype = curbillvo.getItemVOs()[0].getCsourcetype();
				IBillType billType = BillTypeFactory.getInstance().getBillType(csourcetype);
				if (csourcetype != null
						&& !billType.typeOf(ModuleCode.IC))
					isCheckAtp = false;
			}
			checkAtpAfterUpdateHand(vos, sLogdate, isCheckAtp);

		} catch (Exception e) {
			throw GenMethod.handleException(null, e);
		}*/
	}

	private static Hashtable mergeOnHandVOs(ArrayList alVOs)
			throws BusinessException {
		if (alVOs == null || alVOs.size() == 0)
			return null;

		Hashtable hthead = new Hashtable();

		// String[] keys = new String[] { "pk_corp", "ccalbodyid",
		// "cwarehouseid",
		// "cinventoryid", "cinvbasid", "vfree1", "vfree2", "vfree3",
		// "vfree4", "vfree5", "vfree6", "vfree7", "vfree8", "vfree9",
		// "vfree10", "vlot", "castunitid", "cvendorid", "hsl" };
		// �ϲ�VO
		OnhandnumHeaderVO[] voHeads = null;
		OnhandnumHeaderVO[] voItemstmp = new OnhandnumHeaderVO[alVOs.size()];
		alVOs.toArray(voItemstmp);
		nc.vo.scm.merge.DefaultVOMerger m = new nc.vo.scm.merge.DefaultVOMerger();
		m.setMergeAttrs(OnhandnumHeaderVO.onhandheadkeys,
				OnhandnumHeaderVO.onhandheadnumkeys
				/*
				 * new String[] { "nonhandnum", "nonhandastnum", "nnum1",
				 * "nastnum1", "nnum2", "nastnum2", "ngrossnum", "ngrossnum1",
				 * "ngrossnum2" }
				 */, null, null, null);
		voHeads = (OnhandnumHeaderVO[]) m.mergeByGroup(voItemstmp);

		if (voHeads != null && voHeads.length > 0) {
			StringBuffer key = null;
			for (int i = 0; i < voHeads.length; i++) {
				key = new StringBuffer();
				for (int j = 0; j < OnhandnumHeaderVO.onhandheadkeys.length; j++) {
					key
							.append(voHeads[i]
									.getAttributeValue(OnhandnumHeaderVO.onhandheadkeys[j]));
				}
				hthead.put(key.toString(), voHeads[i]);
			}
		}

		return hthead;
	}

	/**
	 * �����ˣ������� ����ʱ�䣺2008-7-2 ����04:48:42 ����ԭ�� ����������
	 * 
	 * @param billhid
	 * @return
	 * @throws BusinessException
	 */
	public ArrayList<OnhandnumBBCVO> getOnhandBBCByBillHID(String billhid)
			throws BusinessException {
		return queryBBCXcl(null, billhid);
	}
	
	/**
	 * �����ˣ������� ����ʱ�䣺2008-7-2 ����03:48:42 ����ԭ�� ��ѯ������
	 * 
	 * @param billhid
	 * @return
	 * @throws BusinessException
	 */
	private ArrayList<OnhandnumBBCVO> queryBBCXcl(ConditionVO[] voaCond,
			String billhid) throws BusinessException {
		return queryBBCXcl(voaCond,billhid,null,null);
	}

	/**
	 * �����ˣ������� ����ʱ�䣺2008-7-2 ����04:48:03 ����ԭ�� ��ҵ����в�ѯ������
	 * 
	 * @param voaCond
	 * @param billhid
	 * @return
	 * @throws BusinessException
	 */
	private ArrayList<OnhandnumBBCVO> queryBBCXcl(ConditionVO[] voaCond,
			String billhid, String endDate, String pk_corp)
			throws BusinessException {

		ArrayList<OnhandnumBBCVO> al = new ArrayList<OnhandnumBBCVO>();
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		OnhandnumBBCVO voBBC = null;

		// �õ�SQL:
		String sSql = getSqlBBCXcl(voaCond, billhid, endDate, pk_corp);
		try {
			con = getConnection();
			stmt = con.prepareStatement(sSql);
			rs = stmt.executeQuery();
			nc.bs.ic.pub.GenMethod.setResultSetNoRowNumLimit(rs);
			while (rs.next()) {
				java.math.BigDecimal num = rs.getBigDecimal("nonhandnum");

				if ((num == null || num.compareTo(ZERO) == 0)) {
					// ��д���ִ���
				} else {
					voBBC = getOnhandnumBBCVO(rs);

					if (num != null)
						voBBC.setNonhandnum(new UFDouble(num));

					al.add(voBBC);
				}

			}
		} catch (Exception e) {
			throw GenMethod.handleException(null, e);
		} finally {
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
			try {
				if (rs != null)
					rs.close();
			} catch (Exception e) {
			}
		}
		return al;
	}
	
	/**
	 * �����ˣ������� ����ʱ�䣺2008-9-17 ����03:50:10 ����ԭ�򣺲�ѯ��������ʷ���Ѿ�����ж�ص�������ʷ��档
	 * @param voaCond
	 * @param pk_corp
	 * @return
	 * @throws BusinessException
	 */
	private ArrayList<OnhandnumBBCVO> queryBBCHisXcl(ConditionVO[] voaCond,
			String pk_corp) throws BusinessException {

		ArrayList<OnhandnumBBCVO> al = new ArrayList<OnhandnumBBCVO>();
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		OnhandnumBBCVO voBBC = null;

		// �õ�SQL:
		String sSql = getSqlBBCHisXcl(voaCond, pk_corp);
		try {
			con = getConnection();
			stmt = con.prepareStatement(sSql);
			rs = stmt.executeQuery();
			nc.bs.ic.pub.GenMethod.setResultSetNoRowNumLimit(rs);
			while (rs.next()) {
				java.math.BigDecimal num = rs.getBigDecimal("nonhandnum");

				if ((num == null || num.compareTo(ZERO) == 0)) {
					// ��д���ִ���
				} else {
					voBBC = getOnhandnumBBCVO(rs);

					if (num != null)
						voBBC.setNonhandnum(new UFDouble(num));

					al.add(voBBC);
				}

			}
		} catch (Exception e) {
			throw GenMethod.handleException(null, e);
		} finally {
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
			try {
				if (rs != null)
					rs.close();
			} catch (Exception e) {
			}
		}
		return al;
	}


	/**
	 * �����ˣ������� ����ʱ�䣺2008-7-2 ����04:47:45 ����ԭ�� ҵ����������ѯ��SQL
	 * 
	 * @param voaCond
	 * @param billhid
	 * @return
	 */
	private String getSqlBBCXcl(ConditionVO[] voaCond, String billhid,
			String endDate, String pk_corp) {

		String whereid = "";
		if (billhid != null && !"".equals(billhid))
			whereid = " and h.cgeneralhid = '" + billhid + "'";
		String wherestr = "";
		if (voaCond != null && 0 < voaCond.length)
			wherestr = getBBCWhere(voaCond).toString();
		String wheredaccountdate = "";
		if (null != getDowLoadDate())
			wheredaccountdate = " and h.daccountdate > '" + getDowLoadDate()
					+ "-31" + "' and h.cbilltypecode not in (" + IBillType.MONTH_INITIAL + ") ";
		String whereendDate = "";
		if (null != endDate)
			whereendDate = " and h.daccountdate <= '" + endDate + "-31" + "' ";
		String wherepk_corp = "";
		if (null != pk_corp)
			wherepk_corp = " and h.pk_corp = '" + pk_corp + "'";
		String sql = " SELECT vbarcode,vbarcodesub,vboxbarcode,cinventoryid,cwarehouseid, "
				+ "pk_corp,ccalbodyid,cinvbasid,vlot,castunitid,hsl,cvendorid,vfree1,vfree2,vfree3,vfree4,vfree5,vfree6,vfree7,vfree8,vfree9,vfree10, "
				+ " (sum(case when (ninnum > 0.0 or noutnum<0.0) then abs(nnumber) else 0.0 end ) "
				+ " -sum(case when (ninnum < 0.0 or noutnum>0.0) then abs(nnumber) else 0.0 end )) as nonhandnum "
				+ " from ("
				+ " SELECT bbc.vbarcode,bbc.vbarcodesub,bbc.vboxbarcode,b.cinventoryid,h.cwarehouseid, "
				+ "h.pk_corp,h.pk_calbody as ccalbodyid,b.cinvbasid,b.vbatchcode as vlot,b.castunitid"
				+ ", case when bas.isstorebyconvert='Y' or bas.isstorebyconvert='1' then b.hsl else cast(null as decimal) end as hsl "
				+ ", case when man.ISSUPPLIERSTOCK='Y' or man.ISSUPPLIERSTOCK='1' or wh.isgathersettle='Y' then b.cvendorid else cast(null as char) end as cvendorid "
				+ ",b.vfree1,b.vfree2,b.vfree3,b.vfree4,b.vfree5,b.vfree6,b.vfree7,b.vfree8,b.vfree9,b.vfree10, "
				+ "b.ninnum,b.noutnum,bbc.nnumber "
				+ " FROM ic_general_h h,ic_general_b b ,ic_general_bbc bbc,"
				+ " bd_invmandoc man,bd_invbasdoc bas ,bd_stordoc wh"
				+ " where h.cgeneralhid = b.cgeneralhid and b.cgeneralbid = bbc.cgeneralbid and "
				+ " b.CINVENTORYID=man.PK_INVMANDOC and man.pk_invbasdoc=bas.pk_invbasdoc and h.cwarehouseid=wh.pk_stordoc  and "
				+ " ((((h.cbilltypecode in ("
				+ IBillType.PURCHASE_IN
				+ ") AND b.fchecked = 0 AND (h.cbiztype IN "
				+ "(SELECT pk_busitype FROM bd_busitype WHERE verifyrule <> 'J') OR h.cbiztype IS NULL)) OR "
				+ " h.cbilltypecode in ("
				+ IBillType.ONHAND_IN_WITHOUT_PURCHASE
				+ ")) AND ninnum IS NOT NULL) OR "
				+ " (((h.cbilltypecode in ("
				+ IBillType.SALE_OUT
				+ ") AND (h.cbiztype IN (SELECT pk_busitype FROM bd_busitype WHERE verifyrule <> 'C') OR "
				+ " h.cbiztype IS NULL)) OR h.cbilltypecode in ("
				+ IBillType.ONHAND_OUT_WITHOUT_SALE
				+ ")) AND noutnum IS NOT NULL)) AND h.dr = 0 AND b.dr = 0 AND bbc.dr = 0 and h.pk_corp = b.pk_corp  "
				+ whereid
				+ wherestr
				+ wheredaccountdate
				+ whereendDate
				+ wherepk_corp
				+ ") v"
				+ " group by vbarcode,vbarcodesub,vboxbarcode,cinventoryid,cwarehouseid, "
				+ "pk_corp,ccalbodyid,cinvbasid,vlot,castunitid,hsl,cvendorid,vfree1,vfree2,vfree3,vfree4,vfree5,vfree6,vfree7,vfree8,vfree9,vfree10 ";

		return sql;

	}
	
	/**
	 * �����ˣ������� ����ʱ�䣺2008-9-17 ����03:50:10 ����ԭ�򣺻�ȡ��ѯ��������ʷ���Ѿ�����ж�ص�������ʷ����SQL��䡣
	 * @param voaCond
	 * @param pk_corp
	 * @return
	 * @throws BusinessException
	 */
	private String getSqlBBCHisXcl(ConditionVO[] voaCond, String pk_corp) {

		String wherestr = "";
		if (voaCond != null && 0 < voaCond.length)
			wherestr = getBBCHisWhere(voaCond).toString();
		String wherepk_corp = "";
		if (null != pk_corp)
			wherepk_corp = " and pk_corp = '" + pk_corp + "' ";

		String sql = " SELECT vbarcode,vbarcodesub,vboxbarcode,cinventoryid,cwarehouseid, "
				+ "pk_corp,ccalbodyid,cinvbasid,vlot,castunitid,hsl,cvendorid,vfree1,vfree2,vfree3,vfree4,vfree5,vfree6,vfree7,vfree8,vfree9,vfree10, "
				+ " nonhandnum "
				+ " from ic_bbcnum_his where 1 = 1 "
				+ wherestr
				+ wherepk_corp
				+ " and dyearmonth ='"
				+ getDowLoadDate()
				+ "' ";

		return sql;

	}

	/**
	 * �����ˣ������� ����ʱ�䣺2008-7-2 ����04:47:24 ����ԭ�� ���ݲ�ѯ��������õ�������VO
	 * 
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	private static OnhandnumBBCVO getOnhandnumBBCVO(ResultSet rs)
			throws SQLException {
		OnhandnumBBCVO vonumBBC = new OnhandnumBBCVO();
		vonumBBC.setPk_corp(rs.getString("pk_corp"));
		vonumBBC.setCcalbodyid(rs.getString("ccalbodyid"));
		vonumBBC.setCwarehouseid(rs.getString("cwarehouseid"));
		vonumBBC.setCinventoryid(rs.getString("cinventoryid"));
		vonumBBC.setVfree1(rs.getString("vfree1"));
		vonumBBC.setVfree2(rs.getString("vfree2"));
		vonumBBC.setVfree3(rs.getString("vfree3"));
		vonumBBC.setVfree4(rs.getString("vfree4"));
		vonumBBC.setVfree5(rs.getString("vfree5"));
		vonumBBC.setVfree6(rs.getString("vfree6"));
		vonumBBC.setVfree7(rs.getString("vfree7"));
		vonumBBC.setVfree8(rs.getString("vfree8"));
		vonumBBC.setVfree9(rs.getString("vfree9"));
		vonumBBC.setVfree10(rs.getString("vfree10"));
		vonumBBC.setVlot(rs.getString("vlot"));
		vonumBBC.setCastunitid(rs.getString("castunitid"));
		vonumBBC.setCvendorid(rs.getString("cvendorid"));
		vonumBBC.setCinvbasid(rs.getString("cinvbasid"));
		vonumBBC.setVbarcode(rs.getString("vbarcode"));
		vonumBBC.setVbarcodesub(rs.getString("vbarcodesub"));
		vonumBBC.setVboxbarcode(rs.getString("vboxbarcode"));
		BigDecimal hsl = rs.getBigDecimal("hsl");
		if (hsl != null)
			vonumBBC.setHsl(new UFDouble(hsl));

		return vonumBBC;

	}

	/**
	 * �����ˣ������� ����ʱ�䣺2008-7-2 ����04:46:25 ����ԭ��������ϸά�Ⱥϲ�������VO
	 * 
	 * @param alBBCVOs
	 * @return
	 * @throws BusinessException
	 */
	private static OnhandnumBBCVO[] mergeOnHandBBCVOs(
			ArrayList<OnhandnumBBCVO> alBBCVOs) throws BusinessException {
		if (alBBCVOs == null || alBBCVOs.size() == 0)
			return null;

		OnhandnumBBCVO[] voItems = new OnhandnumBBCVO[alBBCVOs.size()];
		alBBCVOs.toArray(voItems);
		nc.vo.scm.merge.DefaultVOMerger m = new nc.vo.scm.merge.DefaultVOMerger();
		m.setMergeAttrs(OnhandnumBBCVO.onhandheadkeys,
				OnhandnumBBCVO.onhandheadnumkeys, null, null, null);
		voItems = (OnhandnumBBCVO[]) m.mergeByGroup(voItems);

		return voItems;
	}

	/**
	 * �����ˣ������� ����ʱ�䣺2008-9-18 ����04:11:59 ����ԭ�������ִ�������
	 * �����ִ��������㷨���£�
		1��	ɾ�����������������ִ�����¼��
		2��	��ѯ���һ������ж��ʱ�䵽��ǰʱ��������档
		3��	��ѯ��������ʷ�У��õ����һ������ж��ʱ��������档	
		4��	��2��3�еĽ������������ά�Ƚ��кϲ������˵��������Ϊ0�ļ�¼��
		5��	��4�еĽ�����µ������ִ������С�
		@deprecated
	 * @param voaCond
	 * @return
	 * @throws BusinessException
	 */
	public ArrayList fixOnhandBBCnum_Old(ConditionVO[] voaCond)
			throws BusinessException {
		ArrayList<String> alRet = null;
		try {
			// ɾ�����������������ִ�����¼��
			delOnHandBBCRecord(voaCond);

			// ��ѯ���һ������ж��ʱ�䵽��ǰʱ��������档
			ArrayList<OnhandnumBBCVO> onHandBBCVOList = queryBBCXcl(voaCond,
					null);

			// ���ν��е�����������
			OnhandnumBBCVO[] onHandBBCVOs = null;

			if (getDowLoadDate() != null) {
				// ��ѯ��������ʷ�У��õ����һ������ж��ʱ��������档
				if (null == onHandBBCVOList)
					onHandBBCVOList = queryBBCHisXcl(voaCond, null);
				else
					onHandBBCVOList.addAll(queryBBCHisXcl(voaCond, null));

				// ��������ά�Ƚ��кϲ�
				onHandBBCVOs = mergeOnHandBBCVOs(onHandBBCVOList);
			} else {

				if (null != onHandBBCVOList && 0 < onHandBBCVOList.size()) {
					onHandBBCVOs = new OnhandnumBBCVO[onHandBBCVOList.size()];
					onHandBBCVOList.toArray(onHandBBCVOs);
				}
			}

			if (onHandBBCVOs == null || onHandBBCVOs.length < 1) {
				alRet = new ArrayList<String>();
				String sMsg = nc.bs.ml.NCLangResOnserver.getInstance()
						.getStrByID("4008busi", "UPP4008busi-000025")/*
																		 * @res
																		 * "��������û���ִ������Ե�����"
																		 */;
				alRet.add(sMsg);
				return alRet;
			}

			// ���˵��������Ϊ0�ļ�¼��
			OnhandnumBBCVO[] onHandBBCVOsNew = filterZeroBBCVO(onHandBBCVOs);

			// ������µ������ִ������С�
			new OnhandnumDMO().modifyOnhandBBCNumDirectly(onHandBBCVOsNew);

		} catch (Exception e) {
			if (e instanceof BusinessException)
				throw (BusinessException) e;
			else
				throw new BusinessException("Caused by:", e);
		}
		return alRet;
	}
	

	/**
	 * �����ˣ������� ����ʱ�䣺2009-8-18 ����10:38:20 ����ԭ��
	 * ���������ִ������������������ִ�һ�㶼�Ƚϴ����԰��޸��������ִ�������ȫ�����ΪVO�����׷����ڴ������
	 * ԭ�и���ʱ�������ִ���VO�Ȱ��ִ���ά�ȸ��������ִ������ʱû�ж�Ӧ��¼�������룬��Ϊ�����ִ�������һ��ʼ�ͰѶ�Ӧ�����������ִ���ɾ���ˣ�����û�б�Ҫ�Ƚ��и��£�ֱ��������������С�
	 * �������µĴ����У�����ά�Ƚ��з��鴦��ִ��SQL���ɽ����ֱ�Ӳ��뵽�����ִ������С�
	 * 
	 * @param voaCond
	 * @return
	 * @throws BusinessException
	 */
	public ArrayList fixOnhandBBCnum(ConditionVO[] voaCond)
			throws BusinessException {
		ArrayList<String> alRet = null;
		try {
			// ɾ�����������������ִ�����¼��
			delOnHandBBCRecord(voaCond);
			PersistenceManager persist = PersistenceManager.getInstance();
			persist.setSQLTranslator(true);
			JdbcSession session = persist.getJdbcSession();

			StringBuilder execFixBBCNumSQL = new StringBuilder();
			execFixBBCNumSQL
					.append("insert into ic_onhandnum_bbc (pk_onhandnum_bbc,pk_onhandnum,"
							+ onhandBBCItemKeySQL);
			execFixBBCNumSQL.append(",nonhandnum) ");
			execFixBBCNumSQL.append("select "
					+ GenMethod.getPrimaryKeySQLByDBType(persist.getDBType(),
							"BC", onhandBBCItemKeySQL)
					+ " as pk_onhandnum_bbc,");
			execFixBBCNumSQL.append("'" + nc.vo.ic.pub.GenMethod.STRING_NULL
					+ "',");
			execFixBBCNumSQL.append(onhandBBCItemKeySQL + " ,nonhandnum ");
			execFixBBCNumSQL.append(" from ( ");
			if (getDowLoadDate() != null) {
				execFixBBCNumSQL.append(" SELECT " + onhandBBCItemKeySQL);
				execFixBBCNumSQL.append(" ,sum(nonhandnum) as nonhandnum ");
				execFixBBCNumSQL.append(" from ( ");
				// ���ɲ�ѯ���һ������ж��ʱ�䵽��ǰʱ���������SQL��
				execFixBBCNumSQL
						.append(getSqlBBCXcl(voaCond, null, null, null));
				// ���ɲ�ѯ��������ʷ�У��õ����һ������ж��ʱ��������档
				execFixBBCNumSQL.append(" union all ");
				execFixBBCNumSQL.append(getSqlBBCHisXcl(voaCond, null));
				execFixBBCNumSQL.append(") tempSum  ");
				execFixBBCNumSQL.append(" group by ");
				execFixBBCNumSQL.append(onhandBBCItemKeySQL);

			} else {

				// ���ɲ�ѯ���һ������ж��ʱ�䵽��ǰʱ���������SQL��
				execFixBBCNumSQL
						.append(getSqlBBCXcl(voaCond, null, null, null));
			}
			execFixBBCNumSQL
					.append(") tempall where  nonhandnum > 0 or  nonhandnum < 0 ");

			// ִ�������ִ�������
			int rows = session.executeUpdate(execFixBBCNumSQL.toString());
			// ��ȫ�����ִ������ִ���PK
			int rowub = session.executeUpdate(OnhandnumDMO.getSqlUpdateBBCPK());

			if (rows == 0) {
				alRet = new ArrayList<String>();
				String sMsg = nc.bs.ml.NCLangResOnserver.getInstance()
						.getStrByID("4008busi", "UPP4008busi-000025")/*
																		 * @res
																		 * "��������û���ִ������Ե�����"
																		 */;
				alRet.add(sMsg);
				return alRet;
			}

		} catch (Exception e) {
			if (e instanceof BusinessException)
				throw (BusinessException) e;
			else
				throw new BusinessException("Caused by:", e);
		}
		return alRet;
	}

	private void delOnHandBBCRecord(ConditionVO[] voaCond) throws Exception {
		String sWhere = getMatchCondition(voaCond);

		StringBuffer sDelSql = new StringBuffer(
				"delete from ic_onhandnum_bbc where 1=1 ");
		sDelSql.append(sWhere);
		// ɾ�������ִ������¼
		delRecord(sDelSql.toString());
		sDelSql = null;

		// sDelBodySql =
		// new StringBuffer("delete from ic_onhandnum_bf where pk_onhandnum_f in
		// (select pk_onhandnum_f from ic_onhandnum_f where 1=1 ");
		// sDelBodySql.append(sWhere);
		// sDelBodySql.append(" )");
		// //ɾ���ִ����ӱ��¼
		// delRecord(sDelBodySql.toString());
		// sDelBodySql = null;
		// sDelHeadSql =
		// new StringBuffer("delete from ic_onhandnum_f where 1=1 ");
		// sDelHeadSql.append(sWhere);
		// //ɾ���ִ��������¼
		// delRecord(sDelHeadSql.toString());
		// sDelHeadSql = null;
	}
	
	/**
	 * �����ˣ������� ����ʱ�䣺2008-7-3 ����04:29:18 ����ԭ�� ɾ����������ʷ�������������ʷ��
	 * 
	 * @param voOnhadBBC
	 * @return
	 * @throws BusinessException
	 * 
	 * 
	 */
	private void delOnHandBBCHis(String endDate, String pk_corp)
			throws Exception {

		StringBuffer sDelSql = new StringBuffer(
				"delete from ic_bbcnum_his where 1=1 and dyearmonth = '"
						+ endDate + "' and pk_corp = '" + pk_corp + "'");
		// ɾ�������ִ������¼
		delRecord(sDelSql.toString());
		sDelSql = null;

	}
	
	/**
	 * �����ˣ������� ����ʱ�䣺2008-9-18 ����03:11:59 ����ԭ�� ������ж�������ʱ��ά�����������棬��������ʷ��ά����
	 * ��Ҫ�㷨��
	 * 1��	ɾ��ж������������ʷ��档
	   2��	��ѯ���һ������ж��ʱ�䵽��ǰ����ж��ʱ��������档
	   3��	��ѯ��������ʷ�У��õ����һ������ж��ʱ��������档
	   4��	��2��3�еĽ������������ά�Ƚ��кϲ������˵��������Ϊ0�ļ�¼��
	   5��	��4�еĽ�����������·�Ϊ��ǰ����ж��ʱ�䣬���Ҹ��µ���������ʷ���С�
	 * @param endDate
	 * @param pk_corp
	 * @throws BusinessException
	 */

	public void moveOnhandBBCnum(String endDate, String pk_corp)
			throws BusinessException {
		try {

			if (null == endDate || null == pk_corp || "".equals(endDate.trim())
					|| "".equals(pk_corp.trim()))
				return;

			// ɾ��ж������������ʷ���
			delOnHandBBCHis(endDate, pk_corp);

			// ��ѯ���һ������ж��ʱ�䵽��ǰ����ж��ʱ��������档
			ArrayList<OnhandnumBBCVO> onHandBBCVOList = queryBBCXcl(null, null,
					endDate, pk_corp);
			// ���ν��б��ݵ�������
			OnhandnumBBCVO[] onHandBBCVOs = null;

			if (null != getDowLoadDate()) { // ���������ж��

				// ��ѯ��������ʷ�У��õ����һ������ж��ʱ��������档
				if (null == onHandBBCVOList)
					onHandBBCVOList = queryBBCHisXcl(null, pk_corp);
				else
					onHandBBCVOList.addAll(queryBBCHisXcl(null, pk_corp));

				// ��������ά�Ƚ��кϲ�
				onHandBBCVOs = mergeOnHandBBCVOs(onHandBBCVOList);
			} else {

				if (null != onHandBBCVOList && 0 < onHandBBCVOList.size()) {
					onHandBBCVOs = new OnhandnumBBCVO[onHandBBCVOList.size()];
					onHandBBCVOList.toArray(onHandBBCVOs);
				}

			}

			// û��Ҫ���ݵ�������Ļ���ֱ�ӷ��ء�
			if (onHandBBCVOs == null || onHandBBCVOs.length < 1)
				return;

			// ���˵��������Ϊ0�ļ�¼��
			OnhandnumBBCVO[] onHandBBCVOsNew = filterZeroBBCVO(onHandBBCVOs);
			
			// ���µ���������ʷ����
			new OnhandnumDMO().insertOnhandBBCHis(endDate, onHandBBCVOsNew);

		} catch (Exception e) {
			if (e instanceof BusinessException)
				throw (BusinessException) e;
			else
				throw new BusinessException("Caused by:", e);
		}
	}
	
	/**
	 * �����ˣ������� ����ʱ�䣺2008-8-18 ����03:11:59 ����ԭ�� ���˵��������Ϊ0�ļ�¼����
	 * @param onHandBBCVOList
	 * @return
	 */
	private OnhandnumBBCVO[] filterZeroBBCVO(OnhandnumBBCVO[] onHandBBCVOs){
		ArrayList<OnhandnumBBCVO> onHandBBCVOListNew = new ArrayList<OnhandnumBBCVO>();
		for (OnhandnumBBCVO onHandBBCVO : onHandBBCVOs)
			if (null != onHandBBCVO.getNonhandnum()
					&& nc.vo.ic.pub.GenMethod.ZERO.compareTo(onHandBBCVO
							.getNonhandnum()) != 0) {
				onHandBBCVOListNew.add(onHandBBCVO);
			}

		OnhandnumBBCVO[] onHandBBCVOsNew = null;
		if (null != onHandBBCVOListNew && 0 < onHandBBCVOListNew.size()){
			onHandBBCVOsNew = new OnhandnumBBCVO[onHandBBCVOListNew.size()];
			onHandBBCVOListNew.toArray(onHandBBCVOsNew);
		}
		
		return onHandBBCVOsNew;
		
	}

	/**
	 * �����ˣ������� ����ʱ�䣺2008-9-18 ����03:11:59 ����ԭ�� �����ݻָ������ʱ��ɾ����������ʷ�������������ʷ��
	 * 
	 * @param endDate
	 * @param pk_corp
	 * @throws BusinessException
	 */
	public void restoreOnhandBBCnum(String endDate, String pk_corp)
			throws BusinessException {

		try {

			if (null == endDate || null == pk_corp || "".equals(endDate.trim())
					|| "".equals(pk_corp.trim()))
				return;

			delOnHandBBCHis(endDate, pk_corp);

		} catch (Exception e) {
			if (e instanceof BusinessException)
				throw (BusinessException) e;
			else
				throw new BusinessException("Caused by:", e);
		}
	}
}