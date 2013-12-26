package nc.bs.pub.pf;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.pf.pub.PfDataCache;
import nc.bs.uap.oid.OidGenerator;
import nc.jdbc.framework.JdbcSession;
import nc.jdbc.framework.PersistenceManager;
import nc.jdbc.framework.SQLParameter;
import nc.jdbc.framework.exception.DbException;
import nc.jdbc.framework.processor.BaseProcessor;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.jdbc.framework.processor.ColumnListProcessor;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.pf.change.PfUtilBaseTools;
import nc.vo.pf.pub.BusitypeVO;
import nc.vo.pf.pub.INCConsts;
import nc.vo.pub.BusinessException;
import nc.vo.pub.billtype.BilltypeVO;
import nc.vo.pub.billtype.DefitemVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.pf.IPFConfigInfo;
import nc.vo.pub.pf.PfPOArriveVO;
import nc.vo.pub.pf.PfUtilActionConstrictVO;
import nc.vo.pub.pf.PfUtilActionVO;
import nc.vo.pub.pf.PfUtilBillActionVO;
import nc.vo.pub.pf.Pfi18nTools;
import nc.vo.pub.pfflow01.BillbusinessVO;
import nc.vo.pub.pfflow05.ActionconstrictVO;
import nc.vo.trade.voutils.VOUtil;

/**
 * ����ƽ̨DMO��
 * 
 * @author fangj 2001-10-8 16:04:02
 * @modifier leijun 2005-10-10 ���䵽NC5
 */
public class PfUtilDMO {

	public PfUtilDMO() {
		super();
	}

	/**
	 * ���뵥����Ŀ VO����
	 * @param billitems
	 * @throws DAOException
	 */
	public void insertBillitems(DefitemVO[] defItemVOs) throws DAOException {
		if (defItemVOs == null || defItemVOs.length == 0)
			return;

		String[] oids = OidGenerator.getInstance().nextOid("0001", defItemVOs.length);
		for (int i = 0; i < defItemVOs.length; i++) {
			//WARN::������Ŀdap_defitem.itemname�ֶξ��Ƕ�����ԴID
			defItemVOs[i].setItemname(oids[i]);
			defItemVOs[i].setPk_voitem(oids[i]);
		}
		BaseDAO dao = new BaseDAO();
		dao.insertVOArrayWithPK(defItemVOs);
	}

	/**
	 * ���Ҹö����Ƿ�õ��ݵĽ�������
	 * @param billType
	 * @param actionName
	 * @return
	 * @throws DbException
	 */
	public boolean queryLastStep(String billType, String actionName) throws DbException {
		boolean retflag = false;
		String sql = "select finishflag from pub_billaction where pk_billType=? and actiontype=?";
		PersistenceManager persist = null;
		try {
			persist = PersistenceManager.getInstance();
			JdbcSession jdbc = persist.getJdbcSession();

			SQLParameter para = new SQLParameter();
			para.addParam(billType);
			para.addParam(actionName);
			Object obj = jdbc.executeQuery(sql, para, new ColumnProcessor(1));
			if (String.valueOf(obj).equals("Y")) {
				retflag = true;
			}
		} finally {
			if (persist != null)
				persist.release();
		}

		return retflag;
	}

	/**
	 * ͨ���������͡�ҵ�����ͺͶ�������˾Id�������˵Ĳ�ѯ����ǰԼ������
	 * <li>�����˻����޹أ��򷵻ظü�¼��
	 * <li>�������йأ��򷵻ظ��ò������йص�Լ��������
	 * <li>���йأ��򷵻ظò����˵����йص�Լ��������
	 * 
	 * @param billType
	 * @param businessType
	 * @param actionName
	 * @param corpId
	 * @param operator
	 * @return
	 * @throws DbException
	 * 
	 * @modifier �׾� 2004-03-11 ���ӻ�ȡһ���ֶ� errhintmsg
	 */
	public PfUtilActionConstrictVO[] queryActionConstrict(String billType, String businessType,
			String actionName, String corpId, String operator) throws DbException {
		String sql = "select configflag,operator,a.classname as aclassname,"
				+ " a.returntype as areturntype,a.functionnote as afunctionnote, "
				+ " a.method as amethod,a.parameter as aparameter,ysf,value, "
				+ " b.classname as bclassname,b.functionnote as bfunctionnote, "
				+ " b.method as bmethod,b.parameter as bparameter,errhintmsg,c.isbefore "
				+ " from pub_actionconstrict c inner join pub_function a on "
				+ " c.functionid=a.pk_function left join pub_function b on c.value=b.pk_function "
				+ " where (pk_corp=? or pk_corp='"
				+ INCConsts.COMMONCORP
				+ "') "//��ǰ��˾�ͼ��Ź���ҵ������
				+ " and c.pk_billtype=? and actiontype=? and pk_businesstype=? "
				+ " and (configflag="
				+ IPFConfigInfo.UserNoRelation//�Ͳ���Ա�޹�
				+ " or (configflag=" + IPFConfigInfo.UserRelation
				+ " and operator=?) "//�Ͳ���Ա�й�
				+ " or (configflag="
				+ IPFConfigInfo.RoleRelation//�ͽ�ɫ�й�,��Ҫ�����û��ڹ�˾�н�ɫ��ѯ
				+ " and operator in(select pk_role from sm_user_role where cuserid=? and pk_corp=?)) "
				+ " ) order by c.sysindex";

		PersistenceManager persist = null;
		try {
			persist = PersistenceManager.getInstance();
			JdbcSession jdbc = persist.getJdbcSession();
			SQLParameter para = new SQLParameter();

			//���ò���
			para.addParam(corpId); //��˾Id
			para.addParam(billType); //��������
			para.addParam(actionName); //��������
			para.addParam(businessType); //ҵ������
			para.addParam(operator); //����Ա
			para.addParam(operator); //�ͽ�ɫ�йصĲ���Ա
			para.addParam(corpId); //�ͽ�ɫ�йصĹ�˾Id
			List lResult = (List) jdbc.executeQuery(sql, para, new BaseProcessor() {
				public Object processResultSet(ResultSet rs) throws SQLException {
					ArrayList<PfUtilActionConstrictVO> al = new ArrayList<PfUtilActionConstrictVO>();
					while (rs.next()) {
						PfUtilActionConstrictVO actionConstrict = new PfUtilActionConstrictVO();
						String tmpString = null;

						int configFlag = rs.getInt("configflag");
						actionConstrict.setConfigFlag(configFlag);

						tmpString = rs.getString("operator");
						if (tmpString != null) {
							actionConstrict.setRelaPk(tmpString.trim());
						} else {
							actionConstrict.setRelaPk(null);
						}

						//������
						tmpString = rs.getString("aclassname");
						if (tmpString == null) {
							actionConstrict.setFunClassName(null);
						} else {
							actionConstrict.setFunClassName(tmpString.trim());
						}
						//��������
						tmpString = rs.getString("areturntype");
						if (tmpString == null) {
							actionConstrict.setFunReturnType(null);
						} else {
							actionConstrict.setFunReturnType(tmpString.trim());
						}
						//����ժҪ
						tmpString = rs.getString("afunctionnote");
						if (tmpString == null) {
							actionConstrict.setFunNote(null);
						} else {
							actionConstrict.setFunNote(tmpString.trim());
						}
						//��������
						tmpString = rs.getString("amethod");
						if (tmpString == null) {
							actionConstrict.setMethod(null);
						} else {
							actionConstrict.setMethod(tmpString.trim());
						}
						//��������
						tmpString = rs.getString("aparameter");
						if (tmpString == null) {
							actionConstrict.setParameter(null);
						} else {
							actionConstrict.setParameter(tmpString.trim());
						}
						//�����
						tmpString = rs.getString("ysf");
						if (tmpString == null) {
							actionConstrict.setYsf(null);
						} else {
							actionConstrict.setYsf(tmpString.trim());
						}
						//ֵ
						tmpString = rs.getString("value");
						if (tmpString == null) {
							actionConstrict.setValue(null);
						} else {
							actionConstrict.setValue(tmpString.trim());
						}
						//������
						tmpString = rs.getString("bclassname");
						if (tmpString == null) {
							actionConstrict.setClassName2(null);
						} else {
							actionConstrict.setClassName2(tmpString.trim());
						}
						//����ժҪ
						tmpString = rs.getString("bfunctionnote");
						if (tmpString == null) {
							actionConstrict.setFunNote2(null);
						} else {
							actionConstrict.setFunNote2(tmpString.trim());
						}
						//��������
						tmpString = rs.getString("bmethod");
						if (tmpString == null) {
							actionConstrict.setMethod2(null);
						} else {
							actionConstrict.setMethod2(tmpString.trim());
						}
						//��������
						tmpString = rs.getString("bparameter");
						if (tmpString == null) {
							actionConstrict.setParameter2(null);
						} else {
							actionConstrict.setParameter2(tmpString.trim());
						}
						//added by leijun 20040308
						tmpString = rs.getString("errhintmsg");
						if (tmpString == null) {
							actionConstrict.setErrHintMsg(null);
						} else {
							actionConstrict.setErrHintMsg(tmpString.trim());
						}
						//added by leijun 20070426
						tmpString = rs.getString("isbefore");
						if (tmpString == null) {
							actionConstrict.setIsBefore(ActionconstrictVO.TYPE_BEFORE);
						} else {
							actionConstrict.setIsBefore(tmpString);
						}

						al.add(actionConstrict);
					}
					return al;
				}
			});

			return (PfUtilActionConstrictVO[]) lResult
					.toArray(new PfUtilActionConstrictVO[lResult.size()]);
		} finally {
			if (persist != null)
				persist.release();
		}
	}

	/**
	 * ��ѯ���ݵĶ���ִ��ǰ����ʾ���
	 * 
	 * @param billType ��������PK
	 * @param actionType ��������
	 * @return
	 * @throws DbException
	 */
	public String queryActionHint(String billType, String actionType) throws DbException {
		String sql = "select showhint from pub_billaction where pk_billtype=? and actiontype=?";
		PersistenceManager persist = null;
		try {
			persist = PersistenceManager.getInstance();
			JdbcSession jdbc = persist.getJdbcSession();
			SQLParameter para = new SQLParameter();

			para.addParam(billType);
			para.addParam(actionType);
			Object obj = jdbc.executeQuery(sql, para, new ColumnProcessor(1));
			return String.valueOf(obj);
		} finally {
			if (persist != null)
				persist.release();
		}
	}

	/**
	 * ��ѯĳ����������ĳ����������е��ݶ���VO
	 * 
	 * @param billType ��������PK
	 * @param actionStyle ���������
	 * @return
	 * @throws DbException
	 */
	public PfUtilBillActionVO[] queryBillActionStyle(final String billType, String actionStyle)
			throws DbException {

		String sql = "select c.actiontype,c.actionnote "
				+ "from pub_billactiongroup a inner join pub_billactionconfig b "
				+ "on a.pk_billactiongroup=b.pk_billactiongroup "
				+ "inner join pub_billaction c on (b.actiontype=c.actiontype and "
				+ "c.pk_billtype=a.pk_billtype) where c.pk_billtype=? and a.actionstyle=? order by b.sysindex";

		PersistenceManager persist = null;
		try {
			persist = PersistenceManager.getInstance();
			JdbcSession jdbc = persist.getJdbcSession();
			SQLParameter para = new SQLParameter();

			//���ò���
			//stmt.setString(1, corpId); //��˾PK
			para.addParam(billType); //��������PK
			//stmt.setString(3, businessType); //ҵ������PK
			para.addParam(actionStyle); //���������
			List lResult = (List) jdbc.executeQuery(sql, para, new BaseProcessor() {

				public Object processResultSet(ResultSet rs) throws SQLException {
					ArrayList al = new ArrayList();

					while (rs.next()) {
						PfUtilBillActionVO billVo = new PfUtilBillActionVO();
						billVo.setPkBillType(billType);
						String tmpString = rs.getString("actiontype");
						if (tmpString == null) {
							billVo.setActionName(null);
						} else {
							billVo.setActionName(tmpString.trim());
						}
						tmpString = rs.getString("actionnote");
						if (tmpString == null) {
							billVo.setActionNote(null);
						} else {
							billVo.setActionNote(tmpString.trim());
						}
						al.add(billVo);
					}
					return al;
				}
			});
			return (PfUtilBillActionVO[]) lResult.toArray(new PfUtilBillActionVO[lResult.size()]);
		} finally {
			if (persist != null)
				persist.release();
		}
	}

	private String createBilltypeSql(String billType, String transType) {
		String strField = "";
		if (StringUtil.isEmptyWithTrim(transType)) {
			//��Ҫ�ж�������ǵ������ͻ��ǽ�������
			if (PfUtilBaseTools.isTranstype(billType))
				strField = "a.transtype='" + billType + "'";
			else
				strField = "a.pk_billtype='" + billType + "'";
		} else {
			strField = "((a.pk_billtype='" + billType + "' and a.transtype='" + transType + "')"; //��������+�����������õ�����
			strField += " or (a.pk_billtype='" + billType + "' and a.transtype is null))"; //�������ͣ��޽������ͣ����õ�����
		}
		return strField;
	}

	/**
	 * ��ѯҵ�������µ� ���Ƶ��� 2009-07-31 @dingxm V5.6
	 * @param corpId
	 * @param billType
	 * @param transType
	 * @param businessTypes
	 * @return
	 * @throws DbException
	 */
	public BillbusinessVO[] queryMakeFlagBillSource(final String corpId, final String billType,
			String transType, String[] businessTypes) throws DAOException {
		//��Ҫ�ж�������ǵ������ͻ��ǽ�������
		String strField = createBilltypeSql(billType, transType);
		String sql = "select a.pk_businesstype pk_businesstype,d.businame busitypename "
				+ "from pub_billbusiness a left join pub_billsource b on a.pk_billbusiness=b.billbusinessid "
				+ " left join bd_busitype d on a.pk_businesstype = d.pk_busitype "
				+ "where (a.pk_corp='"+corpId+"' or a.pk_corp='@@@@') and a.makebillflag = 'Y' and " + strField;
		StringBuffer sqlBusitypeIn = new StringBuffer();
		if (businessTypes != null && businessTypes.length > 0) {
			sqlBusitypeIn.append(" and a.pk_businesstype in (");
			for (int i = 0; i < businessTypes.length; i++) {
				sqlBusitypeIn.append("'" + businessTypes[i] + " ',");
			}
			sqlBusitypeIn.deleteCharAt(sqlBusitypeIn.length() - 1);
			sqlBusitypeIn.append(")");
			sql += sqlBusitypeIn.toString();
		}
		BaseDAO dao = new BaseDAO();
		List<BillbusinessVO> billlBusiList = (ArrayList) dao.executeQuery(sql,
				new BeanListProcessor(BillbusinessVO.class));
		if(billlBusiList!=null){
			return billlBusiList.toArray(new BillbusinessVO[]{});
		}
		return null;
	}

	/**
	 * ���ݹ�˾Id,��������(��������)��ҵ�����Ͳ��Ҹõ������͵���Դ
	 * 
	 * <li>��������Ƶ��ݣ�Ҳ������Ϊһ��VO����
	 * <li>Ҳ�Ӽ��Ż�ȡ�������õ���Դ����lj+
	 * 
	 * @param corpId ��˾PK
	 * @param billType ��ǰ��������(��������)PK
	 * @param businessType ҵ������PK
	 * @return
	 * @throws DbException
	 */
	public BillbusinessVO[] queryBillSource(final String corpId, final String billType,
			String transType, String businessType) throws DbException {
		//��Ҫ�ж�������ǵ������ͻ��ǽ�������
		String strField = createBilltypeSql(billType, transType);

		String sql = "select a.makebillflag,b.referbilltype,b.referbusinesstype,c.billtypename "
				+ "from pub_billbusiness a left join pub_billsource b on a.pk_billbusiness=b.billbusinessid "
				+ "left join bd_billtype c on b.referbilltype=c.pk_billtypecode "
				+ "where (a.pk_corp=? or a.pk_corp='@@@@') and " + strField;

		if (businessType != null)
			sql += " and a.pk_businesstype=?";

		PersistenceManager persist = null;
		try {
			persist = PersistenceManager.getInstance();
			JdbcSession jdbc = persist.getJdbcSession();
			SQLParameter para = new SQLParameter();

			//���ò���
			para.addParam(corpId); //��˾����
			if (businessType != null)
				para.addParam(businessType); //��������

			List lResult = (List) jdbc.executeQuery(sql, para, new BaseProcessor() {
				public Object processResultSet(ResultSet rs) throws SQLException {
					ArrayList<BillbusinessVO> al = new ArrayList<BillbusinessVO>();

					if (!rs.next()) {
						//lj@2006-4-27 �����׳��쳣
						//throw new SQLException("�������ͣ�" + billType + "��û���ڹ�˾��" + corpId + "����������Դ���ݣ�");
						return al;
					}
					BillbusinessVO referVo = null;
					if (rs.getString("makebillflag").equals("Y")) {
						referVo = new BillbusinessVO();
						referVo.setBilltypename("���Ƶ���");
						referVo.setPk_billtype("makeflag");
						referVo.setPk_businesstype("");
						al.add(referVo);
					}
					String tmpString = rs.getString("referbilltype");
					if (!StringUtil.isEmptyWithTrim(tmpString)) {
						referVo = new BillbusinessVO();
						referVo.setPk_billtype(tmpString.trim());
						tmpString = rs.getString("referbusinesstype");
						tmpString = (tmpString == null ? "" : tmpString.trim());
						referVo.setPk_businesstype(tmpString);
						tmpString = rs.getString("billtypename");
						tmpString = (tmpString == null ? "" : tmpString.trim());
						referVo.setBilltypename(tmpString);
						al.add(referVo);
					}
					while (rs.next()) {
						tmpString = rs.getString("referbilltype");
						if (!StringUtil.isEmptyWithTrim(tmpString)) {
							referVo = new BillbusinessVO();
							referVo.setPk_billtype(tmpString.trim());
							tmpString = rs.getString("referbusinesstype");
							tmpString = (tmpString == null ? "" : tmpString);
							referVo.setPk_businesstype(tmpString);
							tmpString = rs.getString("billtypename");
							tmpString = (tmpString == null ? "" : tmpString);
							referVo.setBilltypename(tmpString);
							//XXX:�����ظ�����Դ����
							if (!isAlreadyExist(al, referVo.getPk_billtype()))
								al.add(referVo);
						}
					}
					return al;
				}

				/**
				 * �ж���Դ�����Ƿ��Ѿ�����
				 * @param alList
				 * @param srcBilltype
				 * @return
				 */
				private boolean isAlreadyExist(ArrayList<BillbusinessVO> alList, String srcBilltype) {
					for (BillbusinessVO bbVO : alList) {
						if (bbVO.getPk_billtype().equals(srcBilltype))
							return true;
					}
					return false;
				}
			});
			return (BillbusinessVO[]) lResult.toArray(new BillbusinessVO[lResult.size()]);
		} finally {
			if (persist != null)
				persist.release();
		}

	}

	/**
	 * ��ѯ���ݶ���
	 * 
	 * @param corpId ��˾PK
	 * @param billType ��������PK
	 * @param billState ����״̬
	 * @param businessType ҵ������PK
	 * @param actionStyle ������PK
	 * @return
	 * @throws DbException
	 */
	public PfUtilBillActionVO[] queryBillStateActionStyle(String corpId, final String billType,
			String billState, String businessType, String actionStyle) throws DbException {

		String sql = "Select e.configflag,e.operator,c.actiontype,c.actionnote "
				+ "from pub_billactiongroup a "
				+ "inner join pub_billactionconfig b on a.pk_billactiongroup=b.pk_billactiongroup "
				+ "inner join pub_billaction c on (b.actiontype=c.actiontype and a.pk_billtype=c.pk_billtype) "
				+ "inner join pub_billstateactionconfig d on d.billactionconfigid=b.pk_billactionconfig "
				+ "inner join pub_billactionpower e on d.pk_billstateactionconfig=e.pk_billactionconfig "
				+ "where e.pk_corp=? and c.pk_billtype=? and d.billstate=? and a.actionstyle=? ";

		String strTemp = null;
		if (businessType == null) {
			strTemp = "e.pk_businesstype is null";
		} else {
			strTemp = "e.pk_businesstype=?";
		}
		sql = sql + " and " + strTemp + " order by b.sysindex";

		PersistenceManager persist = null;
		try {
			persist = PersistenceManager.getInstance();
			JdbcSession jdbc = persist.getJdbcSession();
			SQLParameter para = new SQLParameter();

			//���ò���
			para.addParam(corpId); //��˾PK
			para.addParam(billType); //��������PK
			para.addParam(billState); //����״̬
			para.addParam(actionStyle); //�������,�����������
			if (businessType != null) {
				para.addParam(businessType); //ҵ������PK
			}
			List lResult = (List) jdbc.executeQuery(sql, para, new BaseProcessor() {

				public Object processResultSet(ResultSet rs) throws SQLException {
					ArrayList<PfUtilBillActionVO> al = new ArrayList<PfUtilBillActionVO>();
					while (rs.next()) {
						PfUtilBillActionVO billVo = new PfUtilBillActionVO();
						billVo.setPkBillType(billType);
						int configFlag = rs.getInt("configflag");
						billVo.setConfigFlag(configFlag);

						String tmpString = rs.getString("operator");
						if (tmpString != null) {
							billVo.setRelaPk(tmpString.trim());
						} else {
							billVo.setRelaPk(null);
						}

						tmpString = rs.getString("actiontype");
						if (tmpString == null) {
							billVo.setActionName(null);
						} else {
							billVo.setActionName(tmpString.trim());
						}
						tmpString = rs.getString("actionnote");
						if (tmpString == null) {
							billVo.setActionNote(null);
						} else {
							billVo.setActionNote(tmpString.trim());
						}
						al.add(billVo);
					}
					return al;
				}
			});
			return (PfUtilBillActionVO[]) lResult.toArray(new PfUtilBillActionVO[lResult.size()]);
		} finally {
			if (persist != null)
				persist.release();
		}

	}

	/**
	 * ��ѯĳҵ��������ĳ��������(��������)�����ε���VO���� 
	 * 
	 * @param billType ��������(��������)PK
	 * @param busiType ҵ������PK�����Ϊ�գ��򷵻��������������иõ��ݵ����ε���
	 * @return
	 * @throws DbException
	 */
	public BillbusinessVO[] queryBillDest(String billType, String busiType) throws DbException {
		boolean isNull = StringUtil.isEmptyWithTrim(busiType);
		String sql = "select a.pk_billbusiness,a.pk_corp,a.pk_billtype,a.transtype,a.pk_businesstype,a.referbillflag,a.makebillflag,a.powerflag "
				+ "from pub_billbusiness a left join pub_billsource b on a.pk_billbusiness=b.billbusinessid "
				+ "where b.referbilltype=?" + (isNull ? "" : " and b.referbusinesstype=?");

		PersistenceManager persist = null;
		try {
			persist = PersistenceManager.getInstance();
			JdbcSession jdbc = persist.getJdbcSession();
			SQLParameter para = new SQLParameter();
			para.addParam(billType);
			if (!isNull)
				para.addParam(busiType);
			ArrayList alRet = (ArrayList) jdbc.executeQuery(sql, para, new BeanListProcessor(
					BillbusinessVO.class));
			return (BillbusinessVO[]) alRet.toArray(new BillbusinessVO[alRet.size()]);
		} finally {
			if (persist != null)
				persist.release();
		}
	}

	/**
	 * ���ݹ�˾PK,��������(��������)PK�������ù���ҵ������
	 * <li>Ҳ���ؼ������õ�ҵ������,�������õ�ҵ����������ǰ��
	 * <li>���ң�ҵ�����Ͱ��ձ�������
	 * 
	 * @param corpId
	 * @param billType �������ͻ�������
	 * @return
	 * @throws DbException
	 */
	public BusitypeVO[] queryBillBusinessType(String corpId, String billType) throws DbException {
		//XXX::��Ҫ�ж�������ǵ������ͻ��ǽ�������
		String strField;
		if (PfUtilBaseTools.isTranstype(billType))
			strField = "a.transtype";
		else
			strField = "a.pk_billtype";
		String sql = "select a.pk_businesstype,b.businame,b.busicode,a.pk_corp from pub_billbusiness a,bd_busitype b"
				+ " where a.pk_businesstype=b.pk_busitype and "
				+ " isnull(b.encapsulate,'N') <> 'Y' and " //wanglei 2013-12-26 �������̹��˵���
				+ strField
				+ "=? and (a.pk_corp=? or a.pk_corp='" + INCConsts.COMMONCORP + "') order by b.busicode";// order by b.pk_corp desc";

		//���Ʊ�2006-4-7������sqlserver����@@@@��1001���ַ�������Ĵ���պú�oracle�෴�������������ݿ�Ĳ����Ե��²���ʹ�����򣬸�Ϊ�ɳ���������
		PersistenceManager persist = null;
		try {
			persist = PersistenceManager.getInstance();
			JdbcSession jdbc = persist.getJdbcSession();
			SQLParameter para = new SQLParameter();
			para.addParam(billType); //��������
			para.addParam(corpId); //��˾����
			List lResult = (List) jdbc.executeQuery(sql, para, new BaseProcessor() {
				public Object processResultSet(ResultSet rs) throws SQLException {
					ArrayList<BusitypeVO> al = new ArrayList<BusitypeVO>();
					while (rs.next()) {
						BusitypeVO btVO = new BusitypeVO();
						String tmpString = rs.getString("pk_businesstype");
						if (tmpString == null) {
							btVO.setPrimaryKey(null);
						} else {
							btVO.setPrimaryKey(tmpString.trim());
						}
						tmpString = rs.getString("businame");
						if (tmpString == null) {
							btVO.setBusiname(null);
						} else {
							btVO.setBusiname(tmpString.trim());
						}
						tmpString = rs.getString("busicode");
						if (tmpString == null) {
							btVO.setBusicode(null);
						} else {
							btVO.setBusicode(tmpString.trim());
						}
						tmpString = rs.getString("pk_corp");
						if (tmpString == null) {
							btVO.setPk_corp(null);
						} else {
							btVO.setPk_corp(tmpString.trim());
						}

						//XXX:�����ظ���ҵ������
						if (!isAlreadyExist(al, btVO.getPrimaryKey()))
							al.add(btVO);
					}
					return al;
				}

				/**
				 * �ж��Ƿ��Ѿ������˸�ҵ������
				 * @param al
				 * @param pk_busitype
				 * @return
				 */
				private boolean isAlreadyExist(ArrayList<BusitypeVO> al, String pk_busitype) {
					if (StringUtil.isEmptyWithTrim(pk_busitype))
						return false;
					for (BusitypeVO btVO : al) {
						if (pk_busitype.equals(btVO.getPrimaryKey()))
							return true;
					}
					return false;
				}
			});
			BusitypeVO[] voRet = null;
			if (lResult != null && lResult.size() > 0) {
				voRet = new BusitypeVO[lResult.size()];
				lResult.toArray(voRet);
				VOUtil.descSort(voRet, new String[] { "pk_corp" });//����pk_corp�����ţ���֤@@@@����ǰ�棬����ʹ�����ݿ������
			}
			return voRet;
		} finally {
			if (persist != null)
				persist.release();
		}

	}

	/**
	 * ���ݵ�������/����״̬/��������� ��ѯ���е��ݶ���
	 * 
	 * @param billType ��������PK
	 * @param billState ����״̬
	 * @param actionStyle ���������
	 * @return
	 * @throws DbException
	 */
	public PfUtilBillActionVO[] queryBillStateActionStyleNoBusi(final String billType,
			String billState, String actionStyle) throws DbException {

		String sql = "Select c.actiontype,c.actionnote "
				+ "from pub_billactiongroup a inner join pub_billactionconfig b on a.pk_billactiongroup=b.pk_billactiongroup "
				+ "inner join pub_billaction c on (b.actiontype=c.actiontype and a.pk_billtype=c.pk_billtype) "
				+ "inner join pub_billstateactionconfig d on d.billactionconfigid=b.pk_billactionconfig "
				+ "where c.pk_billtype=? and d.billstate=? and a.actionstyle=? order by b.sysindex";
		PersistenceManager persist = null;
		try {
			persist = PersistenceManager.getInstance();
			JdbcSession jdbc = persist.getJdbcSession();
			SQLParameter para = new SQLParameter();

			//���ò���
			para.addParam(billType); //��������
			para.addParam(billState); //����״̬
			para.addParam(actionStyle); //�������
			List lResult = (List) jdbc.executeQuery(sql, para, new BaseProcessor() {

				public Object processResultSet(ResultSet rs) throws SQLException {
					ArrayList al = new ArrayList();

					while (rs.next()) {
						PfUtilBillActionVO billVo = new PfUtilBillActionVO();
						billVo.setPkBillType(billType);
						String tmpString = rs.getString("actiontype");
						if (tmpString == null) {
							billVo.setActionName(null);
						} else {
							billVo.setActionName(tmpString.trim());
						}
						tmpString = rs.getString("actionnote");
						if (tmpString == null) {
							billVo.setActionNote(null);
						} else {
							billVo.setActionNote(tmpString.trim());
						}
						al.add(billVo);
					}
					return al;
				}
			});
			return (PfUtilBillActionVO[]) lResult.toArray(new PfUtilBillActionVO[lResult.size()]);
		} finally {
			if (persist != null)
				persist.release();
		}

	}

	/**
	 * ͨ���������͡�ҵ�����͡���˾Id������Ա��ѯ���������Ķ��� �����м�¼VO����
	 * @param billType
	 * @param businessType
	 * @param corpId
	 * @param sourceAction
	 * @param operator
	 * @return
	 * @throws DbException
	 * @�޸��ߣ����Ʊ�20060430�����Ӳ���Ա������ֱ�ӹ��˳����ϲ���Ա�Ķ�������
	 */
	public PfUtilActionVO[] queryDriveAction(final String billType, String businessType,
			String corpId, String sourceAction, String operator) throws DbException {
		String sql = "select pk_driveconfig,configflag,operator,pk_billtype,pk_sourcebusinesstype,actiontype "
				+ " from pub_messagedrive where (pk_corp=? or pk_corp='" + INCConsts.COMMONCORP + "') "//��ǰ��˾�ͼ��Ź���ҵ������
				+ " and pk_sourcebilltype=? and sourceaction=? ";
		if (businessType == null) {
			sql += " and pk_businesstype is null ";
		} else {
			sql += " and pk_businesstype=? ";
		}
		sql += " and ( configflag="
				+ IPFConfigInfo.UserNoRelation//�Ͳ���Ա�޹�
				+ " or (configflag=" + IPFConfigInfo.UserRelation
				+ " and operator=?) "//�Ͳ���Ա�й�
				+ " or (configflag="
				+ IPFConfigInfo.RoleRelation//�ͽ�ɫ�й�,��Ҫ�����û��ڹ�˾�н�ɫ��ѯ
				+ "     and operator in(select pk_role from sm_user_role where cuserid=? and pk_corp=?)) "
				+ " )order by sysindex,pk_billtype";
		PersistenceManager persist = null;
		try {
			persist = PersistenceManager.getInstance();
			JdbcSession jdbc = persist.getJdbcSession();
			SQLParameter para = new SQLParameter();
			//���ò���
			para.addParam(corpId);
			para.addParam(billType);
			para.addParam(sourceAction);
			if (businessType != null) {
				para.addParam(businessType); //ҵ������
			}
			para.addParam(operator); //����Ա
			para.addParam(operator); //�ͽ�ɫ�йصĲ���Ա
			para.addParam(corpId); //�ͽ�ɫ�йصĹ�˾Id

			List lResult = (List) jdbc.executeQuery(sql, para, new BaseProcessor() {
				public Object processResultSet(ResultSet rs) throws SQLException {
					ArrayList<PfUtilActionVO> al = new ArrayList<PfUtilActionVO>();
					while (rs.next()) {
						PfUtilActionVO action = new PfUtilActionVO();
						String tmpString = null;

						tmpString = rs.getString("pk_driveconfig");
						action.setPkMessageDrive(tmpString.trim());

						int configFlag = rs.getInt("configflag");
						action.setConfigFlag(new Integer(configFlag));

						tmpString = rs.getString("operator");
						if (tmpString != null) {
							action.setRelaPK(tmpString.trim());
						} else {
							action.setRelaPK(null);
						}

						//������������
						tmpString = rs.getString("pk_billtype");
						if (tmpString == null) {
							action.setBillType(null);
						} else {
							action.setBillType(tmpString.trim());
						}
						//��������ҵ������
						tmpString = rs.getString("pk_sourcebusinesstype");
						if (tmpString == null) {
							action.setBusinessType(null);
						} else {
							action.setBusinessType(tmpString.trim());
						}
						//������������
						tmpString = rs.getString("actiontype");
						if (tmpString == null) {
							action.setActionName(null);
						} else {
							action.setActionName(tmpString.trim());
						}
						//��������������
						action.setDriveBillType(billType);
						al.add(action);
					}
					return al;
				}
			});
			return (PfUtilActionVO[]) lResult.toArray(new PfUtilActionVO[lResult.size()]);
		} finally {
			if (persist != null)
				persist.release();
		}

	}

	/**
	 * ��ѯ��ǰҵ�������Ƿ����ָ���ĵ����������� 
	 * 
	 * @param billTypeAry
	 * @param busiType
	 * @return
	 * @throws DbException
	 */
	public PfPOArriveVO queryIsSameByBillTypeAry(String billTypeAry, String busiType)
			throws DbException {
		String sql = " SELECT a.pk_billtype,a.pk_businesstype,b.busicode,b.businame "
				+ "FROM pub_billbusiness a LEFT OUTER JOIN bd_busitype b "
				+ "ON a.pk_businesstype = b.pk_busitype WHERE a.pk_businesstype=? AND "
				+ "a.pk_billtype IN (" + billTypeAry + ")";
		PersistenceManager persist = null;
		try {
			persist = PersistenceManager.getInstance();
			JdbcSession jdbc = persist.getJdbcSession();
			SQLParameter para = new SQLParameter();
			para.addParam(busiType);
			PfPOArriveVO retVo = null;
			retVo = (PfPOArriveVO) jdbc.executeQuery(sql, para, new BaseProcessor() {
				public Object processResultSet(ResultSet rs) throws SQLException {
					PfPOArriveVO arriveVo = null;
					//���Ұ����ɹ�������ҵ������
					while (rs.next()) {
						if (arriveVo == null) {
							arriveVo = new PfPOArriveVO();
						}
						String tmpString = rs.getString(1);
						//FIXME::��ô����̫�����ˣ�
						if ("23".equals(tmpString)) {
							arriveVo.setArriveFlag(new UFBoolean("Y"));
						}
						if ("45".equals(tmpString)) {
							arriveVo.setStoreFlag(new UFBoolean("Y"));
						}

						tmpString = rs.getString(2);
						if (tmpString == null) {
							arriveVo.setPk_busiType(null);
						} else {
							arriveVo.setPk_busiType(tmpString.trim());
						}
						tmpString = rs.getString(3);
						if (tmpString == null) {
							arriveVo.setBusiCode(null);
						} else {
							arriveVo.setBusiCode(tmpString.trim());
						}
						tmpString = rs.getString(4);
						if (tmpString == null) {
							arriveVo.setBusiName(null);
						} else {
							arriveVo.setBusiName(tmpString.trim());
						}
					}
					return arriveVo;
				}
			});
			return retVo;
		} finally {
			if (persist != null)
				persist.release();
		}

	}

	/**
	 * @param busiPks
	 * @param billtype
	 * @return
	 * @throws BusinessException
	 */
	public String[] getBillbusiPKsOfBilltype(String[] busiPks, String billtype)
			throws BusinessException {
		if (busiPks == null || busiPks.length == 0)
			return null;

		//XXX:Ҫ���ִ�����ǵ������ͻ��ǽ�������
		String strField;
		if (PfUtilBaseTools.isTranstype(billtype))
			strField = "transtype";
		else
			strField = "pk_billtype";

		String sWhere = strField + "='" + billtype + "' and (pk_billbusiness='" + busiPks[0] + "'";
		for (int i = 1; i < busiPks.length; i++) {
			sWhere += " or pk_billbusiness='" + busiPks[i] + "'";
		}
		sWhere += ")";
		Collection collection = new BaseDAO().retrieveByClause(BillbusinessVO.class, sWhere,
				new String[] { "pk_businesstype" });
		if (collection == null || collection.size() == 0)
			return null;
		String[] sRet = new String[collection.size()];
		BillbusinessVO voTmp = null;
		int i = 0;
		for (Iterator iter = collection.iterator(); iter.hasNext();) {
			voTmp = (BillbusinessVO) iter.next();
			sRet[i] = voTmp.getPk_businesstype();
			i++;
		}
		return sRet;
	}

	/**
	 * ���ݹ�˾PK,��������(��������)PK����û�н���Ȩ�޿��Ƶ�ҵ������
	 * <li>Ҳ���ؼ������õ�ҵ������
	 * @param corpId
	 * @param billType ��������(��������)PK
	 * @return
	 * @throws DbException
	 * @author ���Ʊ�2006-03-29
	 */
	public String[] queryBusitypeOfNoControl(String corpId, String billType) throws DbException {
		//XXX:Ҫ���ִ�����ǵ������ͻ��ǽ�������
		String strField;
		if (PfUtilBaseTools.isTranstype(billType))
			strField = "transtype";
		else
			strField = "pk_billtype";

		String sql = "select pk_businesstype from pub_billbusiness where (pk_corp=? or pk_corp='"
				+ INCConsts.COMMONCORP + "') and " + strField
				+ "=? and (powerflag is null or powerflag='N')";
		PersistenceManager persist = null;
		try {
			persist = PersistenceManager.getInstance();
			JdbcSession jdbc = persist.getJdbcSession();
			SQLParameter para = new SQLParameter();
			//���ò���
			para.addParam(corpId); //��˾����
			para.addParam(billType); //��������
			List list = (List) jdbc.executeQuery(sql, para, new ColumnListProcessor(1));
			String[] sRet = null;
			if (list != null && list.size() > 0) {
				sRet = new String[list.size()];
				list.toArray(sRet);
			}
			return sRet;
		} finally {
			if (persist != null)
				persist.release();
		}
	}

	/**
	 * ����ĳ��ҵ�������µ�������������VO����
	 * <li>����һЩ������Ϣ
	 * @param biztype ҵ������pK
	 * @param pkcorp ��˾PK
	 * @return
	 * @throws DbException
	 */
	public BillbusinessVO[] queryBillbusiVOs(String biztype, String pkcorp) throws DbException {
		String sql = "select a.*, d.businame as destbiztypename from pub_billbusiness a "
				+ "left outer join bd_busitype d on a.destbiztype=d.pk_busitype where a.pk_businesstype=?";

		if (!StringUtil.isEmptyWithTrim(pkcorp))
			sql += " and a.pk_corp='" + pkcorp + "'";
		PersistenceManager persist = null;
		try {
			persist = PersistenceManager.getInstance();
			JdbcSession jdbc = persist.getJdbcSession();
			SQLParameter para = new SQLParameter();
			para.addParam(biztype);
			List list = (List) jdbc.executeQuery(sql, para, new BeanListProcessor(BillbusinessVO.class));
			BillbusinessVO[] vos = (BillbusinessVO[]) list.toArray(new BillbusinessVO[list.size()]);
			for (BillbusinessVO billbusiVO : vos) {
				String billType = billbusiVO.getPk_billtype();
				String transType = billbusiVO.getTranstype();
				BilltypeVO btVO = PfDataCache.getBillType(billType);
				billbusiVO.setBilltypename(Pfi18nTools.i18nBilltypeName(billType, btVO == null ? null
						: btVO.getBilltypename()));
				if (!StringUtil.isEmptyWithTrim(transType)) {
					BilltypeVO transtypeVO = PfDataCache.getBillType(transType);
					billbusiVO.setTranstypename(Pfi18nTools.i18nBilltypeName(transType,
							transtypeVO == null ? null : transtypeVO.getBilltypename()));
				}
			}
			return vos;
		} finally {
			if (persist != null)
				persist.release();
		}

	}

	/**
	 * ��ѯĳ��ҵ��������ָ���������ͻ������͵�Ŀ��ҵ������
	 * @param busitype
	 * @param billtype
	 * @return
	 * @throws DbException
	 */
	public String queryDestBusitype(String busitype, String billtype) throws DbException {
		boolean isTranstype = PfUtilBaseTools.isTranstype(billtype);
		String sql = "select destbiztype from pub_billbusiness where pk_businesstype=? ";
		if (isTranstype)
			sql += "and transtype=?";
		else
			sql += "and pk_billtype=?";
		PersistenceManager persist = PersistenceManager.getInstance();
		JdbcSession jdbc = persist.getJdbcSession();
		SQLParameter para = new SQLParameter();
		para.addParam(busitype);
		para.addParam(billtype);
		Object obj = jdbc.executeQuery(sql, para, new ColumnProcessor());
		return (String) obj;
	}
}