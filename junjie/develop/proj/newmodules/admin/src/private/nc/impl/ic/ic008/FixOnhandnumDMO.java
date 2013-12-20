package nc.impl.ic.ic008;

/**
 * 类型说明。
 * 创建日期：(2003-2-21 9:02:25)
 * @author：程起伍
 * 修改日期：(2003-2-21 9:02:25)
 * 修改人：@author：程起伍
 * 修改原因：
 * 其它说明：
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
	 * FixOnhandnumDMO 构造子注解。
	 * 
	 * @exception javax.naming.NamingException
	 *                异常说明。
	 * @exception nc.bs.pub.SystemException
	 *                异常说明。
	 */
	
	public FixOnhandnumDMO() throws javax.naming.NamingException,
			nc.bs.pub.SystemException {
		super();
	}

	/**
	 * FixOnhandnumDMO 构造子注解。
	 * 
	 * @param dbName
	 *            java.lang.String
	 * @exception javax.naming.NamingException
	 *                异常说明。
	 * @exception nc.bs.pub.SystemException
	 *                异常说明。
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
	 * 得到数据卸载时间
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
	 * 篭n创建日期：(2003-2-21 8:52:54) 作者：程起伍 修改日期： 修改人： 修改原因： 算法说明 方法说明：BO端的入口方法。
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
	 * 篭n创建日期：(2003-2-21 8:52:54) 作者：程起伍 修改日期： 修改人： 修改原因： 算法说明 方法说明：BO端的入口方法。
	 * @deprecated by 刘家清
	 * @param voaCond
	 *            nc.vo.pub.query.ConditionVO
	 */
	public ArrayList fixOnhandnum_old(ConditionVO[] voaCond)
			throws BusinessException {
		return fixOnhandnum_old(voaCond, false);
	}

	/**
	 * 篭n创建日期：(2003-2-21 8:52:54) 作者：程起伍 修改日期： 修改人： 修改原因： 算法说明 方法说明：BO端的入口方法。
	 * @deprecated by 刘家清
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

			// 启用差异记录,获取现存量表数据,并取反
			if (isOnhandErr || bcheck) {
				String sWhere = getMatchCondition(voaCond);
				voOldHeads = getNegOldHeadVos(sWhere);

				StringBuffer bodywhere = new StringBuffer(
						" pk_onhandnum in (select pk_onhandnum from ic_onhandnum where 1=1 ");

				bodywhere.append(sWhere);
				bodywhere.append(" )");
				voOldBodys = getNegOldBodyVos(bodywhere.toString());

			}

			// 删除符合查询条件的在ic_onhandnum和ic_onhandnum_b的记录。
			if (!bcheck)
				delMatchRecord(voaCond);

			// 得到OnhandnumVO和OnhandnumHeaderVO;
			Hashtable htVO = null;

			htVO = queryWithoutSpace(voaCond, null, null);

			if (htVO == null || htVO.size() < 1) {
				alRet = new ArrayList();
				String sMsg = nc.bs.ml.NCLangResOnserver.getInstance()
						.getStrByID("4008busi", "UPP4008busi-000025")/*
																		 * @res
																		 * "根据条件没有现存量可以调整！"
																		 */;
				alRet.add(sMsg);
			}
			// 得到OnhandnumItemVO:
			ArrayList alNew = new ArrayList();
			if (getDowLoadDate() == null) { // 如果没有数据卸载
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
			// 用语日志
			OnhandnumItemVO[] voNewBodys = null;
			if (alNew.size() > 0) {
				voNewBodys = new OnhandnumItemVO[alNew.size()];
				alNew.toArray(voNewBodys);
			}
			Hashtable htItemVO = mergeOnHandItemVOsToHs(alNew);

			// 将ItemVO加入到VO中
			ArrayList alVO = getOnhandnumVO(htVO, htItemVO);
			// 插入调整后的现存量：
			if (!bcheck)
				insert(alVO);

			// 插入差异数据,新旧数据合并
			if (isOnhandErr || bcheck) {
				StringBuilder serr = new StringBuilder(" 现存量错误： ");
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
								serr.append("存货管理档案：" + vo.getCinventoryid()
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
									serr.append("货位档案：" + vo.getCspaceid()
											+ ",");
									serr.append("存货管理档案："
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
	 * 创建人：刘家清 创建时间：2009-8-13 下午06:33:31 创建原因：
	 * 在做现存量调整，当现存一般都比较大，所以把修复的现存量数据全部查出为VO，容易发生内存溢出。
	 * 在使用新现存量更新算法后，现存量差异纪录已经可以去除，并且此功能是否使用由配置文件NC_Home\modules\scmpub\scmatp\atpconfig.xml中配置项isonhanderr控制，所以同时去除此功能同时删除配置项。
	 * 现存量差异检查是在检查负结存（包括检查存货主辅数量方向不一致）时触发的，在使用新现存量更新算法后，也去除此功能后，在检查负结存时，不会去检查现存量差异，提高操作效率。
	 * 原有更新时把现存量VO先按现存量维度更新现存表，更新时没有对应记录再做插入，因为现存量调整一开始就把对应的所有现存量删除了，所以没有必要先进行更新，直接做插入操作就行。
	 * 在新的处理中，根据维度进行分组处理，执行SQL生成结果集直接插入到现存量表中。
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

			// 删除符合查询条件的在ic_onhandnum和ic_onhandnum_b的记录。
			delMatchRecord(voaCond);

			session = persist.getJdbcSession();
			// 执行现存量调整
			int rows = session.executeUpdate(getOnhandNumFixSQL(voaCond,
					persist.getDBType()));
			// 执行货位现存量调整
			int rowsb = session.executeUpdate(getSpaceOnhandNumFixSQL(voaCond,
					persist.getDBType()));
			
			//begin ncm fansj1_瑞能集团_现存量修复报错违反唯一性约束201108251245591625
			// added by lirr 2010-1-15下午05:33:17 港陆项目问题，以前的算法 若多个货位的数量和为0 则主表没记录 字表有记录，导致有子表外键为空的记录
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
		      		+") ) tbl");//不加别名 会在SQLSever报错 原因不知 陈倪娜 2010-10-23
					
				// 补全货位现存量中现存量PK
		      int rowufix = session.executeUpdate(sqlFillInsert.toString());
		      //end ncm fansj1_瑞能集团_现存量修复报错违反唯一性约束201108251245591625
		      
			
			// 补全货位现存量中现存量PK
			int rowub = session.executeUpdate(OnhandnumDMO.getSqlUpdatePK());

			if (rows <= 0) {
				alRet = new ArrayList();
				String sMsg = nc.bs.ml.NCLangResOnserver.getInstance()
						.getStrByID("4008busi", "UPP4008busi-000025")/*
																		 * @res
																		 * "根据条件没有现存量可以调整！"
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
	 * 创建人：刘家清 创建时间：2009-8-14 下午01:21:28 创建原因：返回现存量调整SQL
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
	 * 创建人：刘家清 创建时间：2009-8-14 下午01:21:56 创建原因：返回货位现存量调整SQL
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
		if (getDowLoadDate() == null) { // 如果没有数据卸载
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
	 * 向数据库插入一个VO对象。
	 * 
	 * 创建日期：(2002-12-23)
	 * 
	 * @param node
	 *            nc.vo.ic.ic2a3.AccountctrlHeaderVO
	 * @exception java.sql.SQLException
	 *                异常说明。
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
				// 换算率
				nc.vo.ic.pub.GenMethod.setStmtBigDecimal(stmt, headVO.getHsl(),
						++iINDEX);
				// 毛重
				nc.vo.ic.pub.GenMethod.setStmtBigDecimal(stmt, headVO
						.getNgrossnum(), ++iINDEX);
				// nc.vo.ic.pub.GenMethod.setStmtBigDecimal(stmt, headVO
				// .getNgrossnum1(), ++iINDEX);
				// nc.vo.ic.pub.GenMethod.setStmtBigDecimal(stmt, headVO
				// .getNgrossnum2(), ++iINDEX);

				// 执行
				executeUpdate(stmt);
			}
			executeBatch(stmt);
		} catch (Exception e) {
			// 库存组异常抛出规范
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
	 * 向数据库插入一个VO对象。
	 * 
	 * 创建日期：(2002-12-23)
	 * 
	 * @param node
	 *            nc.vo.ic.ic2a3.AccountctrlItemVO
	 * @exception java.sql.SQLException
	 *                异常说明。
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
				// 库存组织
				nc.vo.ic.pub.GenMethod.setStmtString(stmt, voItem
						.getCcalbodyid(), ++iIndex);
				// 仓库
				nc.vo.ic.pub.GenMethod.setStmtString(stmt, voItem
						.getCwarehouseid(), ++iIndex);
				// 存货
				nc.vo.ic.pub.GenMethod.setStmtString(stmt, voItem
						.getCinventoryid(), ++iIndex);
				// 批次
				nc.vo.ic.pub.GenMethod.setStmtString(stmt, voItem.getVlot(),
						++iIndex);
				// 辅计量
				nc.vo.ic.pub.GenMethod.setStmtString(stmt, voItem
						.getCastunitid(), ++iIndex);
				// 自由项
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
				// 货位
				nc.vo.ic.pub.GenMethod.setStmtString(stmt,
						voItem.getCspaceid(), ++iIndex);
				// 数量
				nc.vo.ic.pub.GenMethod.setStmtBigDecimal(stmt,
						voItem.getNnum(), ++iIndex);
				// 辅数量
				nc.vo.ic.pub.GenMethod.setStmtBigDecimal(stmt, voItem
						.getNastnum(), ++iIndex);
				// 货位[货位有点特殊]
				stmt.setString(++iIndex, cupdateid);

				// 供应商
				nc.vo.ic.pub.GenMethod.setStmtString(stmt, voItem
						.getCvendorid(), ++iIndex);
				// 换算率
				nc.vo.ic.pub.GenMethod.setStmtBigDecimal(stmt, voItem.getHsl(),
						++iIndex);
				// 毛重
				nc.vo.ic.pub.GenMethod.setStmtBigDecimal(stmt, voItem
						.getNgrossnum(), ++iIndex);
				// 公司
				nc.vo.ic.pub.GenMethod.setStmtString(stmt, voItem.getPk_corp(),
						++iIndex);

				executeUpdate(stmt);
			}
			executeBatch(stmt);
		} catch (Exception e) {
			// 库存组异常抛出规范
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

		// 得到SQL:
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
	 * 篭n创建日期：(2003-2-21 19:05:15) 作者：程起伍 修改日期： 修改人： 修改原因： 算法说明：
	 * 功能：将带货位汇总的ItemVO加入到不带货位汇总的聚合VO中。
	 * 
	 * @param htVO
	 *            java.util.Hashtable
	 * @param htItemVO
	 *            java.util.Hashtable
	 */
	private ArrayList getOnhandnumVO(Hashtable htVO, Hashtable htItemVO) {
		Enumeration headKeys = null;
		// Enumeration onhandnumVO = null;
		// 返回值：放入的类型是完整的OnhandnumVO
		ArrayList alVO = new ArrayList();
		if (htVO != null && htVO.size() > 0) {
			headKeys = htVO.keys();
			// onhandnumVO = htVO.elements();
			OnhandnumHeaderVO voHead = null;
			while (headKeys.hasMoreElements()) {

				// 取哈希表htVO的主键以及键值：
				String stempkey = (String) headKeys.nextElement();
				OnhandnumVO onhandnumtempVO = new OnhandnumVO();
				voHead = (OnhandnumHeaderVO) htVO.get(stempkey);
				onhandnumtempVO.setParentVO(voHead);
				if (htItemVO != null && htItemVO.size() > 0) {
					// 如果在htItemVO中包含主键，那么取出ItemVO
					if (htItemVO.containsKey(stempkey)) {
						// 放入聚合VO:OnhandnumVO中
						onhandnumtempVO
								.setChildrenVO((OnhandnumItemVO[]) htItemVO
										.get(stempkey));
						// 从哈希表中去掉，以减少以后的寻找。
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
	 * 篭n创建日期：(2003-2-21 20:10:27) 作者：程起伍 修改日期： 修改人： 修改原因： 算法说明：
	 * 功能：从ic_onhandnum和ic_onhandnum_b中删除符合查询条件的记录。
	 * 
	 * @param voaCond
	 *            nc.vo.pub.query.ConditionVO[]
	 */
	private void delMatchRecord(ConditionVO[] voaCond) throws Exception {
		String sWhere = getMatchCondition(voaCond);
		StringBuffer sDelBodySql = new StringBuffer(
				"delete from ic_onhandnum_b where ic_onhandnum_b.pk_onhandnum is null or pk_onhandnum in (select pk_onhandnum from ic_onhandnum where 1=1 ");  //wanglei 2013-12-19 不得已而为之。
		sDelBodySql.append(sWhere);
		sDelBodySql.append(" )");
		// 删除现存量从表纪录
		delRecord(sDelBodySql.toString());
		sDelBodySql = null;
		StringBuffer sDelHeadSql = new StringBuffer(
				"delete from ic_onhandnum where 1=1 ");
		sDelHeadSql.append(sWhere);
		// 删除现存量主表纪录
		delRecord(sDelHeadSql.toString());
		sDelHeadSql = null;

		// sDelBodySql =
		// new StringBuffer("delete from ic_onhandnum_bf where pk_onhandnum_f in
		// (select pk_onhandnum_f from ic_onhandnum_f where 1=1 ");
		// sDelBodySql.append(sWhere);
		// sDelBodySql.append(" )");
		// //删除现存量从表纪录
		// delRecord(sDelBodySql.toString());
		// sDelBodySql = null;
		// sDelHeadSql =
		// new StringBuffer("delete from ic_onhandnum_f where 1=1 ");
		// sDelHeadSql.append(sWhere);
		// //删除现存量主表纪录
		// delRecord(sDelHeadSql.toString());
		// sDelHeadSql = null;
	}

	/**
	 * 篭n创建日期：(2003-2-21 9:17:41) 作者：程起伍 修改日期： 修改人： 修改原因： 算法说明：
	 * 说明：删除符合条件的ic_onhandnum的记录。
	 * 
	 * @return java.util.Hashtable
	 * @param sSql
	 *            java.lang.String
	 * @exception java.sql.SQLException
	 *                异常说明。
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
	 * 篭n创建日期：(2003-2-21 20:11:51) 作者：程起伍 修改日期： 修改人： 修改原因： 算法说明：
	 * 功能：从查询条件中匹配得到查询ic_onhandnum的条件。
	 * 
	 * @return java.lang.String
	 * @param voaCond
	 *            nc.vo.pub.query.ConditionVO[]
	 */
	private String getMatchCondition(ConditionVO[] voaCond) {
		StringBuffer sbWhere = new StringBuffer();
		if (voaCond != null && voaCond.length > 0) {
			for (int i = 0; i < voaCond.length; i++) {
				// 由于在ic_onhandnum中批次号字段名是vlot，所以要替换掉。
				if ("vbatchcode".equals(voaCond[i].getFieldCode().toString()))
					voaCond[i].setFieldCode("vlot");
				// 由于给存货基本档案提供接口,此处要将cinvbasid的条件换一下
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
	 * 篭n创建日期：(2003-2-21 9:54:21) 作者：程起伍 修改日期： 修改人： 修改原因： 算法说明：
	 * 功能：从查询VO中得到查询条件并创建SQL。
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
	 * 篭n创建日期：(2003-2-21 9:54:21) 作者：程起伍 修改日期： 修改人： 修改原因： 算法说明：
	 * 功能：从查询VO中得到查询条件并创建SQL。
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
	 * 将VO插入母子表。
	 * <p>
	 * 创建日期：(2002-12-23)
	 * 
	 * @param vo
	 *            nc.vo.ic.ic2a3.AccountctrlVO
	 * @exception java.sql.SQLException
	 *                异常说明。
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
																				 * "现存量调整："
																				 */
					+ e.getMessage());
		}
	}

	/**
	 * 篭n创建日期：(2003-2-21 9:17:41) 作者：程起伍 修改日期： 修改人： 修改原因： 算法说明：
	 * 说明：在视图ic_keep_detail6上汇总现存量，但不针对货位汇总。
	 * 
	 * @return java.util.Hashtable
	 * @param sSql
	 *            java.lang.String
	 * @exception java.sql.SQLException
	 *                异常说明。
	 */
	// private Hashtable queryWithoutSpace_old(ConditionVO[] voaCond)
	// throws Exception {
	//
	// /** ********************************************************** */
	// /*
	// * 返回值，主键：公司+库存组织+仓库+存货+批次+辅计量+自由项+失效日期。如果每项取值为空时，用“null”代替。
	// * 键值：OnhandnumVO.
	// */
	//
	// // 2004-12-01 ydy 包含现存量、借入、借出量
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
	// // 合并VO
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
	 * 篭n创建日期：(2003-2-21 9:17:41) 作者：程起伍 修改日期： 修改人： 修改原因： 算法说明：
	 * 说明：在视图ic_keep_detail6上汇总现存量，但不针对货位汇总。
	 * 
	 * @return java.util.Hashtable
	 * @param sSql
	 *            java.lang.String
	 * @exception java.sql.SQLException
	 *                异常说明。
	 */
	// private Hashtable queryWithSpace_old(ConditionVO[] voaCond)
	// throws Exception {
	// /** ********************************************************** */
	// // 保留的系统管理接口：
	// beforeCallMethod("nc.bs.ic.ic008.FixOnhandnumDMO", "queryWithSpace",
	// new Object[] { voaCond });
	// /** ********************************************************** */
	// /*
	// * 返回值，主键：//主键规则：公司+库存组织+仓库+存货+自由项+批次号+失效日期+辅计量。如果每项取值为空时，用“null”代替。
	// * 键值：OnhandnumVO.
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
	// // 主键
	// StringBuffer sbPK = null;
	// // 得到SQL:
	// String sSql = getSqlWithSpace(voaCond);
	// // 主键
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
	// // 自由项
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
	// // 批次号
	// sBatchcode = rs.getString("vbatchcode");
	// sbPK.append(sBatchcode);
	// // 失效日期
	// // sValidate = rs.getString("dvalidate");
	// // if(sValidate == null || sValidate.trim().length() <= 0)
	// // ufdtValidate = null;
	// // else
	// // ufdtValidate = new UFDate(sValidate);
	// // sbPK.append(sValidate);
	// // 辅计量
	// sAstuintid = rs.getString("castunitid");
	// sbPK.append(sAstuintid);
	// ufdSpacenum = new UFDouble(rs.getBigDecimal("nspacenum"));
	// ufdAssitSpacenum = new UFDouble(rs
	// .getBigDecimal("nspaceassistnum"));
	// voItem.setNnum(ufdSpacenum);
	// voItem.setNastnum(ufdAssitSpacenum);
	// voItem.setCspaceid(sSpaceid);
	// // 未支持新的修改现存量的方法，将库存组织，仓库，批次等加入到voItem中。
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
	// // 保留的系统管理接口：
	// afterCallMethod("nc.bs.ic.ic008.FixOnhandnumDMO", "queryWithSpace",
	// new Object[] { voaCond });
	// /** ********************************************************** */
	//
	// return retHt;
	// }
	/**
	 * 篭n创建日期：(2003-2-21 9:17:41) 作者：程起伍 修改日期： 修改人： 修改原因： 算法说明：
	 * 说明：在视图ic_keep_detail6上汇总现存量，但不针对货位汇总。
	 * 
	 * @return java.util.Hashtable
	 * @param sSql
	 *            java.lang.String
	 * @exception java.sql.SQLException
	 *                异常说明。
	 */
	// private ArrayList queryXcl_old(ConditionVO[] voaCond) throws Exception {
	// /** ********************************************************** */
	// // 保留的系统管理接口：
	// beforeCallMethod("nc.bs.ic.ic008.FixOnhandnumDMO", "queryWithoutSpace",
	// new Object[] { voaCond });
	// /** ********************************************************** */
	// /*
	// * 返回值，主键：公司+库存组织+仓库+存货+批次+辅计量+自由项+失效日期。如果每项取值为空时，用“null”代替。
	// * 键值：OnhandnumVO.
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
	// // 主键
	// // 主键规则：公司+库存组织+仓库+存货+自由项+批次号+失效日期+辅计量
	// StringBuffer sbPK = null;
	// // 得到SQL:
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
	// // 失效日期
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
	// // 保留的系统管理接口：
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
				if (voaCond[i].getFieldCode().equals("cinvbasid"))// 主要用于为存货基本档案提供接口的条件
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
				if (newvoaCond.getFieldCode().equals("cinvbasid"))// 主要用于为存货基本档案提供接口的条件
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

		// 换算率
		UFDouble hsl = rs.getBigDecimal("hsl") == null ? null : new UFDouble(rs
				.getBigDecimal("hsl"));
		voItem.setHsl(hsl);

		for (int i = 1; i <= FreeVO.FREE_ITEM_NUM; i++) {
			voItem.setAttributeValue("vfree" + i, rs.getString("vfree" + i));
		}

		voItem.setCinvbasid(rs.getString("cinvbasid"));

		UFDouble ngrossnum = rs.getBigDecimal("ngrossnum") == null ? null
				: new UFDouble(rs.getBigDecimal("ngrossnum"));// 毛重
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
	// * 张海燕 功能:查现存量[只查货位数据],为组织现存量子表数据做准备 备注:
	// *
	// */
	// private Hashtable queryWithSpace(ConditionVO[] voaCond, int iQueryMode)
	// throws Exception {
	// /*
	// * 返回值，主键：//主键规则：公司+库存组织+仓库+存货+自由项+批次号+辅计量+供应商+换算率。如果每项取值为空时，用“null”代替。
	// * 键值：OnhandnumVO.
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
	// // 主键
	// StringBuffer sbPK = null;
	// // 得到SQL:
	//    
	// String sSql = getSqlWithSpace(voaCond,iQueryMode);
	// ArrayList alAll = new ArrayList();
	// // 主键
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
	// // 自由项
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
	// // 批次号
	// sBatchcode = rs.getString("vbatchcode");
	// sbPK.append(sBatchcode);
	// // //失效日期
	// // sValidate = rs.getString("dvalidate");
	// // if(sValidate == null || sValidate.trim().length() <= 0)
	// // ufdtValidate = null;
	// // else
	// // ufdtValidate = new UFDate(sValidate);
	// // sbPK.append(sValidate);
	// // 辅计量
	// sAstuintid = rs.getString("castunitid");
	// sbPK.append(sAstuintid);
	// // 供应商
	// cvendorid = rs.getString("cproviderid");
	// sbPK.append(cvendorid);
	// // 换算率
	// hsl = rs.getBigDecimal("hsl") == null ? null : new UFDouble(rs
	// .getBigDecimal("hsl"));
	// sbPK.append(hsl);
	//
	// ngrossnum = rs.getBigDecimal("ngrossnum") == null ? null
	// : new UFDouble(rs.getBigDecimal("ngrossnum"));// 毛重
	// ufdSpacenum = new UFDouble(rs.getBigDecimal("nspacenum"));
	// ufdAssitSpacenum = new UFDouble(rs
	// .getBigDecimal("nspaceassistnum"));
	// voItem.setNgrossnum(ngrossnum);
	// voItem.setNnum(ufdSpacenum);
	// voItem.setNastnum(ufdAssitSpacenum);
	// voItem.setCspaceid(sSpaceid);
	// // 未支持新的修改现存量的方法，将库存组织，仓库，批次等加入到voItem中。
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
	 * 篭n创建日期：(2003-2-21 9:17:41) 作者：程起伍 修改日期： 修改人： 修改原因： 算法说明：
	 * 说明：在视图ic_keep_detail6上汇总现存量，但不针对货位汇总。
	 * 
	 * @return java.util.Hashtable
	 * @param sSql
	 *            java.lang.String
	 * @exception java.sql.SQLException
	 *                异常说明。
	 */
	private Hashtable queryWithoutSpace(ConditionVO[] voaCond, String cbillid,
			String cbilltype) throws Exception {
		/*
		 * 返回值，主键：公司+库存组织+仓库+存货+批次+辅计量+自由项+供应商+换算率。如果每项取值为空时，用“null”代替。
		 * 键值：OnhandnumVO.
		 */
		// 2004-12-01 ydy 包含现存量、借入、借出量
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
		// // 合并VO
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
	 * 创建人：刘家清 创建时间：2009-8-18 上午10:39:46 创建原因： 根据维度进行分组处理，生成SQL，最终把结果集直接插入到现存量表中。
	 * @param voaCond
	 * @return
	 * @throws Exception
	 */
	private String getQueryWithoutSpaceSQL(ConditionVO[] voaCond) throws Exception {
		/*
		 * 返回值，主键：公司+库存组织+仓库+存货+批次+辅计量+自由项+供应商+换算率。如果每项取值为空时，用“null”代替。
		 * 键值：OnhandnumVO.
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
	 * 张海燕 功能:查现存量[忽略货位] 备注:
	 */
	private ArrayList queryXcl(ConditionVO[] voaCond, QueryType iQueryMode,
			String cbillid) throws Exception {
		/*
		 * 返回值，主键：公司+库存组织+仓库+存货+批次+辅计量+自由项+失效日期+供应商+换算率。如果每项取值为空时，用“null”代替。
		 * 键值：OnhandnumVO.
		 */
		ArrayList al = new ArrayList();
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		OnhandnumHeaderVO voHead = null;

		// 得到SQL:
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
					// 不写入现存量
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
	 * 张海燕 功能:查现存量[忽略货位维护] 备注:去掉dvalidate SQL: SELECT pk_corp, ccalbodyid,
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
	 * ,cproviderid order by cinventoryid 修改人：刘家清 修改时间：2008-6-17 下午04:10:50
	 * 修改原因：dbizdate>'"+getDowLoadDate()修改成dbizdate>'"+getDowLoadDate()+"-31"，不然会多统计卸载年份最后一个月的未卸出的数据。
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
	 * 张海燕 功能:查借入量 备注:1.由于已有批次号档案,现存量表不再维护dvalidate,故不需要选出来
	 * 2.将存货分为:按换算率记结存和不按换算率记结存两类 SQL:select pk_corp,pk_calbody as
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
					// 不写入现存量
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
	 * 修改人：刘家清 修改时间：2008-6-17 下午04:10:50
	 * 修改原因：dbizdate>'"+getDowLoadDate()修改成dbizdate>'"+getDowLoadDate()+"-31"，不然会多统计卸载年份最后一个月的未卸出的数据。
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
	 * 张海燕 功能:查借出量 备注:算法类似于查借入量 SQL:
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
					// 不写入现存量
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
	 * 修改人：刘家清 修改时间：2008-6-17 下午04:10:50
	 * 修改原因：dbizdate>'"+getDowLoadDate()修改成dbizdate>'"+getDowLoadDate()+"-31"，不然会多统计卸载年份最后一个月的未卸出的数据。
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

		// 得到SQL:
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
	 * 张海燕 功能:查货位现存量sql 备注: SQL: 修改人：刘家清 修改时间：2008-6-17 下午04:10:50
	 * 修改原因：dbizdate>'"+getDowLoadDate()修改成dbizdate>'"+getDowLoadDate()+"-31"，不然会多统计卸载年份最后一个月的未卸出的数据。
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
	 * ?user> 功能： 参数： 返回： 例外： 日期：(2004-11-30 9:43:25) 修改日期，修改人，修改原因，注释标志：
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
	 * ?user> 功能： 参数： 返回： 例外： 日期：(2004-11-30 9:43:25) 修改日期，修改人，修改原因，注释标志：
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
	 * 篭n创建日期：(2003-2-21 9:17:41) 作者：程起伍 修改日期： 修改人： 修改原因： 算法说明：
	 * 说明：在视图ic_keep_detail6上汇总现存量，但不针对货位汇总。
	 * 
	 * @return java.util.Hashtable
	 * @param sSql
	 *            java.lang.String
	 * @exception java.sql.SQLException
	 *                异常说明。
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
	 * 篭n创建日期：(2003-2-21 9:17:41) 作者：程起伍 修改日期： 修改人： 修改原因： 算法说明：
	 * 说明：在视图ic_keep_detail6上汇总现存量，但不针对货位汇总。
	 * 
	 * @return java.util.Hashtable
	 * @param sSql
	 *            java.lang.String
	 * @exception java.sql.SQLException
	 *                异常说明。
	 */
	private ArrayList getOnhandByID(GeneralBillVO curbillvo)
			throws BusinessException {
		if (curbillvo == null)
			return null;
		String cbillid = (String) curbillvo
				.getHeaderValue(IItemKey.CGENERALHID);
		if (cbillid == null)
			return null;// throw new BusinessException("更新现存量错误!");
		String cbilltype = (String) curbillvo
				.getHeaderValue(IItemKey.CBILLTYPECODE);
		try {

			Hashtable hshead = queryWithoutSpace(null, cbillid, cbilltype);

			ArrayList altem = queryWithSpace_v5(null, QueryType.Query_Bill,
					cbillid);

			// 用语日志
			OnhandnumItemVO[] voNewBodys = null;
			if (altem != null && altem.size() > 0) {
				voNewBodys = new OnhandnumItemVO[altem.size()];
				altem.toArray(voNewBodys);
			}
			Hashtable hsbody = mergeOnHandItemVOsToHs(altem);

			// 将ItemVO加入到VO中
			ArrayList alVO = getOnhandnumVO(hshead, hsbody);

			return alVO;
		} catch (Exception e) {
			throw GenMethod.handleException(null, e);
		}
	}

	/**
	 * 篭n创建日期：(2003-2-21 9:17:41) 作者：程起伍 修改日期： 修改人： 修改原因： 算法说明：
	 * 说明：在视图ic_keep_detail6上汇总现存量，但不针对货位汇总。
	 * 
	 * @return java.util.Hashtable
	 * @param sSql
	 *            java.lang.String
	 * @exception java.sql.SQLException
	 *                异常说明。
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
			// 日志异常
			nc.vo.scm.pub.SCMEnv.out(e);
			// 库存组异常抛出规范
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
	 * 篭n创建日期：(2003-2-21 9:17:41) 作者：程起伍 修改日期： 修改人： 修改原因： 算法说明：
	 * 说明：在视图ic_keep_detail6上汇总现存量，但不针对货位汇总。
	 * 
	 * @return java.util.Hashtable
	 * @param sSql
	 *            java.lang.String
	 * @exception java.sql.SQLException
	 *                异常说明。
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
		// //日志异常
		// nc.vo.scm.pub.SCMEnv.out(e);
		// //库存组异常抛出规范
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
			throw new BusinessException("更新现存量错误!");
		try {
			UFDouble d_1 = new UFDouble(-1);
			
			if (onHandUpdateType == ICConst.OnHandUpdateType.Onhand_All || onHandUpdateType == ICConst.OnHandUpdateType.Onhand_BillOnly){
				
				//更新月结数据
				MonthServ ms = null;
				ms = new MonthServ();
				ms.modifyMonthDataEnd(curbillvo, monthpreinfo);
						
				//更新单据现存量		
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

				// 将ItemVO加入到VO中
				ArrayList alVO = getOnhandnumVO(hthead, htbody);
				OnhandnumVO[] vos = new OnhandnumVO[alVO.size()];
				alVO.toArray(vos);
				
				vos = sortOnhandnumVO(vos);
				new OnhandnumDMO().modifyOnhandNumDirectly(vos);



				// 简单处理
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
				//更新条码现存量
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

			// 将ItemVO加入到VO中
			ArrayList alVO = getOnhandnumVO(hthead, htbody);
			OnhandnumVO[] vos = new OnhandnumVO[alVO.size()];
			alVO.toArray(vos);
			
			vos = sortOnhandnumVO(vos);
			new OnhandnumDMO().modifyOnhandNumDirectly(vos);

			if (null != handbbclist && handbbclist.size() > 0) {
				OnhandnumBBCVO[] vobbcs = mergeOnHandBBCVOs(handbbclist);
				new OnhandnumDMO().modifyOnhandBBCNumDirectly(vobbcs);
			}

			// 简单处理
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
		// 合并VO
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
	 * 创建人：刘家清 创建时间：2008-7-2 下午04:48:42 创建原因： 单据条码结存
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
	 * 创建人：刘家清 创建时间：2008-7-2 下午03:48:42 创建原因： 查询条码结存
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
	 * 创建人：刘家清 创建时间：2008-7-2 下午04:48:03 创建原因： 从业务表中查询条码结存
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

		// 得到SQL:
		String sSql = getSqlBBCXcl(voaCond, billhid, endDate, pk_corp);
		try {
			con = getConnection();
			stmt = con.prepareStatement(sSql);
			rs = stmt.executeQuery();
			nc.bs.ic.pub.GenMethod.setResultSetNoRowNumLimit(rs);
			while (rs.next()) {
				java.math.BigDecimal num = rs.getBigDecimal("nonhandnum");

				if ((num == null || num.compareTo(ZERO) == 0)) {
					// 不写入现存量
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
	 * 创建人：刘家清 创建时间：2008-9-17 下午03:50:10 创建原因：查询条码结存历史中已经数据卸载的条码历史结存。
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

		// 得到SQL:
		String sSql = getSqlBBCHisXcl(voaCond, pk_corp);
		try {
			con = getConnection();
			stmt = con.prepareStatement(sSql);
			rs = stmt.executeQuery();
			nc.bs.ic.pub.GenMethod.setResultSetNoRowNumLimit(rs);
			while (rs.next()) {
				java.math.BigDecimal num = rs.getBigDecimal("nonhandnum");

				if ((num == null || num.compareTo(ZERO) == 0)) {
					// 不写入现存量
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
	 * 创建人：刘家清 创建时间：2008-7-2 下午04:47:45 创建原因： 业务表条码结存查询主SQL
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
	 * 创建人：刘家清 创建时间：2008-9-17 下午03:50:10 创建原因：获取查询条码结存历史中已经数据卸载的条码历史结存的SQL语句。
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
	 * 创建人：刘家清 创建时间：2008-7-2 下午04:47:24 创建原因： 根据查询出结果集得到条码结存VO
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
	 * 创建人：刘家清 创建时间：2008-7-2 下午04:46:25 创建原因：以最明细维度合并条码结存VO
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
	 * 创建人：刘家清 创建时间：2008-9-18 下午04:11:59 创建原因：条码现存量调整
	 * 条码现存量调整算法如下：
		1）	删除符合条件的条码现存量记录。
		2）	查询最近一次数据卸载时间到当前时间的条码结存。
		3）	查询条码结存历史中，得到最近一次数据卸载时间的条码结存。	
		4）	把2和3中的结果，按条码结存维度进行合并，过滤掉结存数量为0的记录。
		5）	把4中的结果更新到条码现存量表中。
		@deprecated
	 * @param voaCond
	 * @return
	 * @throws BusinessException
	 */
	public ArrayList fixOnhandBBCnum_Old(ConditionVO[] voaCond)
			throws BusinessException {
		ArrayList<String> alRet = null;
		try {
			// 删除符合条件的条码现存量记录。
			delOnHandBBCRecord(voaCond);

			// 查询最近一次数据卸载时间到当前时间的条码结存。
			ArrayList<OnhandnumBBCVO> onHandBBCVOList = queryBBCXcl(voaCond,
					null);

			// 本次进行调整的条码结存
			OnhandnumBBCVO[] onHandBBCVOs = null;

			if (getDowLoadDate() != null) {
				// 查询条码结存历史中，得到最近一次数据卸载时间的条码结存。
				if (null == onHandBBCVOList)
					onHandBBCVOList = queryBBCHisXcl(voaCond, null);
				else
					onHandBBCVOList.addAll(queryBBCHisXcl(voaCond, null));

				// 按条码结存维度进行合并
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
																		 * "根据条件没有现存量可以调整！"
																		 */;
				alRet.add(sMsg);
				return alRet;
			}

			// 过滤掉结存数量为0的记录。
			OnhandnumBBCVO[] onHandBBCVOsNew = filterZeroBBCVO(onHandBBCVOs);

			// 结果更新到条码现存量表中。
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
	 * 创建人：刘家清 创建时间：2009-8-18 上午10:38:20 创建原因：
	 * 在做条码现存量调整，由于条码现存一般都比较大，所以把修复的条码现存量数据全部查出为VO，容易发生内存溢出。
	 * 原有更新时把条码现存量VO先按现存量维度更新条码现存表，更新时没有对应记录再做插入，因为条码现存量调整一开始就把对应的所有条码现存量删除了，所以没有必要先进行更新，直接做插入操作就行。
	 * 所以在新的处理中，根据维度进行分组处理，执行SQL生成结果集直接插入到条码现存量表中。
	 * 
	 * @param voaCond
	 * @return
	 * @throws BusinessException
	 */
	public ArrayList fixOnhandBBCnum(ConditionVO[] voaCond)
			throws BusinessException {
		ArrayList<String> alRet = null;
		try {
			// 删除符合条件的条码现存量记录。
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
				// 生成查询最近一次数据卸载时间到当前时间的条码结存SQL。
				execFixBBCNumSQL
						.append(getSqlBBCXcl(voaCond, null, null, null));
				// 生成查询条码结存历史中，得到最近一次数据卸载时间的条码结存。
				execFixBBCNumSQL.append(" union all ");
				execFixBBCNumSQL.append(getSqlBBCHisXcl(voaCond, null));
				execFixBBCNumSQL.append(") tempSum  ");
				execFixBBCNumSQL.append(" group by ");
				execFixBBCNumSQL.append(onhandBBCItemKeySQL);

			} else {

				// 生成查询最近一次数据卸载时间到当前时间的条码结存SQL。
				execFixBBCNumSQL
						.append(getSqlBBCXcl(voaCond, null, null, null));
			}
			execFixBBCNumSQL
					.append(") tempall where  nonhandnum > 0 or  nonhandnum < 0 ");

			// 执行条码现存量调整
			int rows = session.executeUpdate(execFixBBCNumSQL.toString());
			// 补全条码现存量中现存量PK
			int rowub = session.executeUpdate(OnhandnumDMO.getSqlUpdateBBCPK());

			if (rows == 0) {
				alRet = new ArrayList<String>();
				String sMsg = nc.bs.ml.NCLangResOnserver.getInstance()
						.getStrByID("4008busi", "UPP4008busi-000025")/*
																		 * @res
																		 * "根据条件没有现存量可以调整！"
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
		// 删除条码现存量表纪录
		delRecord(sDelSql.toString());
		sDelSql = null;

		// sDelBodySql =
		// new StringBuffer("delete from ic_onhandnum_bf where pk_onhandnum_f in
		// (select pk_onhandnum_f from ic_onhandnum_f where 1=1 ");
		// sDelBodySql.append(sWhere);
		// sDelBodySql.append(" )");
		// //删除现存量从表纪录
		// delRecord(sDelBodySql.toString());
		// sDelBodySql = null;
		// sDelHeadSql =
		// new StringBuffer("delete from ic_onhandnum_f where 1=1 ");
		// sDelHeadSql.append(sWhere);
		// //删除现存量主表纪录
		// delRecord(sDelHeadSql.toString());
		// sDelHeadSql = null;
	}
	
	/**
	 * 创建人：刘家清 创建时间：2008-7-3 下午04:29:18 创建原因： 删除条码结存历史表中相关条码历史表
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
		// 删除条码现存量表纪录
		delRecord(sDelSql.toString());
		sDelSql = null;

	}
	
	/**
	 * 创建人：刘家清 创建时间：2008-9-18 下午03:11:59 创建原因： 当数据卸载条码表时，维护备份条码结存，条码结存历史表维护。
	 * 主要算法：
	 * 1）	删除卸载区间条码历史结存。
	   2）	查询最近一次数据卸载时间到当前数据卸载时间的条码结存。
	   3）	查询条码结存历史中，得到最近一次数据卸载时间的条码结存。
	   4）	把2和3中的结果，按条码结存维度进行合并，过滤掉结存数量为0的记录。
	   5）	把4中的结果，设置年月份为当前数据卸载时间，并且更新到条码结存历史表中。
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

			// 删除卸载区间条码历史结存
			delOnHandBBCHis(endDate, pk_corp);

			// 查询最近一次数据卸载时间到当前数据卸载时间的条码结存。
			ArrayList<OnhandnumBBCVO> onHandBBCVOList = queryBBCXcl(null, null,
					endDate, pk_corp);
			// 本次进行备份的条码结存
			OnhandnumBBCVO[] onHandBBCVOs = null;

			if (null != getDowLoadDate()) { // 如果有数据卸载

				// 查询条码结存历史中，得到最近一次数据卸载时点的条码结存。
				if (null == onHandBBCVOList)
					onHandBBCVOList = queryBBCHisXcl(null, pk_corp);
				else
					onHandBBCVOList.addAll(queryBBCHisXcl(null, pk_corp));

				// 按条码结存维度进行合并
				onHandBBCVOs = mergeOnHandBBCVOs(onHandBBCVOList);
			} else {

				if (null != onHandBBCVOList && 0 < onHandBBCVOList.size()) {
					onHandBBCVOs = new OnhandnumBBCVO[onHandBBCVOList.size()];
					onHandBBCVOList.toArray(onHandBBCVOs);
				}

			}

			// 没有要备份的条码结存的话就直接返回。
			if (onHandBBCVOs == null || onHandBBCVOs.length < 1)
				return;

			// 过滤掉结存数量为0的记录。
			OnhandnumBBCVO[] onHandBBCVOsNew = filterZeroBBCVO(onHandBBCVOs);
			
			// 更新到条码结存历史表中
			new OnhandnumDMO().insertOnhandBBCHis(endDate, onHandBBCVOsNew);

		} catch (Exception e) {
			if (e instanceof BusinessException)
				throw (BusinessException) e;
			else
				throw new BusinessException("Caused by:", e);
		}
	}
	
	/**
	 * 创建人：刘家清 创建时间：2008-8-18 下午03:11:59 创建原因： 过滤掉结存数量为0的记录。。
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
	 * 创建人：刘家清 创建时间：2008-9-18 下午03:11:59 创建原因： 当数据恢复条码表时，删除条码结存历史表中相关条码历史表。
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