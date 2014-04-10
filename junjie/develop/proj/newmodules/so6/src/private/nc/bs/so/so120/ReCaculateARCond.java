package nc.bs.so.so120;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import javax.naming.NamingException;

import nc.bs.pub.SystemException;
import nc.bs.scm.pub.smart.SmartDMO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.query.ConditionVO;
import nc.vo.so.pub.GeneralSqlString;

public class ReCaculateARCond extends SmartDMO {

	private ReCaculateARDMO ardmo;

	private final String nullID = "null=null=null=null=";

	public ReCaculateARCond() throws NamingException, SystemException {
		super();
	}

	/**
	 * �жϲ�ѯ���� ����Ǽ����� ��ֱ�Ӵ��� ������ԭ���㷨����
	 * 
	 * @param ardmo
	 * @param cvos
	 * @param pk_corp
	 * @return [0]����[1]Ӧ��-�����ͻ�[2]����[3]��Ӧ��[4]��Ʊ[5]Ӧ��-���ݿͻ�[6]�����¼��ͻ�
	 * @throws BusinessException
	 */
	public String[] getConditons(ReCaculateARDMO ardmo, ConditionVO[] cvos, String pk_corp) throws BusinessException {
		try {
			this.ardmo = ardmo;
			if (simpleQuery(cvos)) {
				return getDirectCond(cvos, pk_corp);
			} else {
				return getConditons(cvos, pk_corp);
			}
		} catch (Exception e) {
			throw new BusinessException(e);
		}
	}

	/**
	 * ֻ��and
	 * 
	 * @return
	 */
	private boolean simpleQuery(ConditionVO[] cvos) {
		// if(true)return false;//test

		if (cvos == null || cvos.length == 0)
			return false;

		for (ConditionVO cvo : cvos) {
			// ��������ᴫ������������
			if (cvo.getFieldCode() == null || cvo.getFieldCode().equals("null"))
				return false;

			if (!cvo.getLogic())
				return false;
		}
		return true;
	}

	/**
	 * �ͻ�:���õȼ�/�ͻ�/�ͻ�����/��������/������֯<br>
	 * ��Ʒ��<br>
	 * ҵ������<br>
	 * 
	 * ��������������id,ֻ��'��������'�а�����ϵ
	 * 
	 * @param cvos
	 * @param pk_corp
	 * @return
	 * @throws SQLException
	 * @throws NamingException
	 * @throws SystemException
	 * @throws BusinessException
	 */
	private String[] getDirectCond(ConditionVO[] cvos, String pk_corp) throws SQLException, SystemException,
			NamingException, BusinessException {
		// 1.��ò�ѯ����
		String[] sql = getSql(cvos, pk_corp);

		// 2.����Ҫ���滻
		String s0, s1, s2, s3, s4, s5, s6;

		s0 = new String(sql[0]);
		s0 = s0.replaceAll("pk_cumandoc", "th.ccustomerid");
		s0 = s0.replaceAll("cproductid", "cprolineid");
		s0 = s0.replaceAll("cbiztypeid", "th.cbiztype");

		s1 = new String(sql[1]);
		s1 = s1.replaceAll("pk_cumandoc", "tb.ordercusmandoc");
		s1 = s1.replaceAll("cproductid", "productline");
		s1 = s1.replaceAll("cbiztypeid", "xslxbm");

		s2 = new String(sql[0]);
		s2 = s2.replaceAll("pk_cumandoc", "soh.ccustomerid");
		s2 = s2.replaceAll("cproductid", "cprolineid");
		s2 = s2.replaceAll("cbiztypeid", "soh.cbiztype");

		s3 = new String(sql[0]);
		s3 = s3.replaceAll("pk_cumandoc", "th.ccustomerid");
		s3 = s3.replaceAll("cproductid", "tb.cproducelineid");
		s3 = s3.replaceAll("cbiztypeid", "tb.cbiztypeid");

		s4 = new String(sql[0]);
		s4 = s4.replaceAll("pk_cumandoc", "tih.ccustomerid");
		s4 = s4.replaceAll("cproductid", "tib.cprolineid");
		s4 = s4.replaceAll("cbiztypeid", "tih.cbiztype");

		s5 = new String(sql[0]);
		s5 = s5.replaceAll("pk_cumandoc", "ksbm_cl");
		s5 = s5.replaceAll("cproductid", "productline");
		s5 = s5.replaceAll("cbiztypeid", "xslxbm");

		s6 = getConditionS6(cvos, pk_corp);

		return new String[] { s0, s1, s2, s3, s4, s5, s6 };
	}

	/**
	 * 
	 * 
	 * @param cvos
	 * @param pk_corp
	 * @return ���ݿͻ�����id�ͻ���id�ֱ𹹽�����sql,�ֱ����ڹ�����Ӧ���Ͳ����sql
	 * @throws SQLException
	 * @throws BusinessException
	 * @throws NamingException
	 * @throws SystemException
	 */
	private String[] getSql(ConditionVO[] cvos, String pk_corp) throws SQLException, BusinessException,
			SystemException, NamingException {
		HashSet<String> pk_cumandoc = new HashSet();
		HashSet<String> pk_cubasdoc = new HashSet();
		HashSet<String> cproductid = new HashSet();
		HashSet<String> cbiztypeid = new HashSet();

		HashSet<String> newValue;
		String querySql;
		for (ConditionVO cvo : cvos) {
			if (cvo.getFieldCode().equals("pk_cumandoc") || cvo.getFieldCode().equals("creditlevel")
					|| cvo.getFieldCode().equals("pk_pricegroupcorp") || cvo.getFieldCode().equals("pk_areacl")
					|| cvo.getFieldCode().equals("pk_salestru")) {
				if (cvo.getFieldCode().equals("pk_cumandoc")) {
					pk_cumandoc.add(cvo.getValue());
				} else {
					if (cvo.getFieldCode().equals("pk_areacl")) {
						querySql = "select pk_cumandoc from bd_cumandoc, bd_cubasdoc where bd_cumandoc.pk_cubasdoc=bd_cubasdoc.pk_cubasdoc "
								+ " and custflag in('0','2') and bd_cumandoc.pk_corp='" + pk_corp + "' and pk_areacl ";//����custflag ����
						if ("like".equals(cvo.getOperaCode())) {
							querySql += " in (select pk_areacl from bd_areacl where areaclcode like '"
									+ cvo.getRefResult().getRefCode() + "%')";
						} else {
							querySql += "='" + cvo.getValue() + "'";
						}
					} else {
						querySql = "select pk_cumandoc from bd_cumandoc where " + cvo.getFieldCode() + "='"
								+ cvo.getValue() + "'";
					}
					newValue = queryNewValue(querySql);
					if (newValue.isEmpty())
						throw new BusinessException(nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("scmsocredit",
								"UPPSCMSOCredit-000292", null, new String[] { cvo.getFieldName() })/*
						 * @res
						 * "��ѡ{0}��û����ƥ��Ŀͻ�!"
						 */);
					updateValue(pk_cumandoc, newValue);
				}
			} else if (cvo.getFieldCode().equals("cproductid")) {
				cproductid.add(cvo.getValue());
			} else if (cvo.getFieldCode().equals("cbiztypeid")) {
				cbiztypeid.add(cvo.getValue());
			}
		}// end for

		pk_cumandoc = findAllForwardCust(pk_cumandoc, pk_corp);
		fillPkCubas(pk_cumandoc, pk_cubasdoc);

		String[] results = new String[2];
		results[0] = buildSql(pk_cumandoc, cproductid, cbiztypeid,"temp_so_forminsql001");
		results[1] = buildSql(pk_cubasdoc, cproductid, cbiztypeid,"temp_so_forminsql002");

		return results;
	}

	private HashSet<String> queryNewValue(String querySql) throws SQLException {
		Object[] objs = selectBy2(querySql);
		HashSet<String> value = new HashSet();

		if (objs == null || objs.length == 0 || objs[0] == null || ((Object[]) objs[0]).length == 0)
			return value;

		for (Object obj : objs) {
			value.add(((Object[]) obj)[0].toString());
		}

		return value;
	}

	private void updateValue(HashSet<String> oldValue, HashSet<String> newValue) {
		if (newValue.isEmpty())
			return;

		if (oldValue.isEmpty()) {
			oldValue.addAll(newValue);
		} else if (oldValue.contains(nullID)) {
			return;
		} else {
			Iterator<String> iter = oldValue.iterator();

			String curr;
			while (iter.hasNext()) {
				curr = iter.next();
				if (!newValue.contains(curr)) {
					oldValue.remove(curr);
				}
			}// end while

			if (oldValue.isEmpty())
				oldValue.add(nullID);
		}
	}

	/**
	 * ��ѯ���������¼��Ŀ��̵���
	 * 
	 * @param pk_cumandoc
	 * @return
	 */
	private HashSet<String> findAllForwardCust(HashSet<String> pk_cumandoc, String pk_corp) throws SQLException,
			SystemException, NamingException {
		if (ardmo.HM_AllCus == null)
			ardmo.HM_AllCus = ardmo.getDataManager().getAllCusInBas(pk_corp);

		// ��ѯ��������
		HashMap<String, String> pk_cudocs = new HashMap();
		fillPkCubas(pk_cumandoc, pk_cudocs);

		Iterator<String> iter = pk_cudocs.keySet().iterator();
		CreditUtilBS cutil = new CreditUtilBS();

		// ��һ��ѯ�¼����ϲ�
		HashSet<String> pk_allcumandoc = new HashSet();
		String pk_currcubasdoc;
		while (iter.hasNext()) {
			pk_currcubasdoc = iter.next();
			ArrayList<String> al = cutil.getAllLeaf(pk_currcubasdoc, ardmo.HM_AllCus[1], null, true, true);
			pk_allcumandoc.addAll(al);
			pk_allcumandoc.add(pk_cudocs.get(pk_currcubasdoc));
		}// end while

		return pk_allcumandoc;
	}

	private void fillPkCubas(HashSet<String> pk_cumandoc, HashSet<String> pk_cubasdoc) throws SQLException {
		if (pk_cumandoc == null || pk_cumandoc.size() == 0)
			return;

		Object[] objs = selectBy2("select pk_cubasdoc from bd_cumandoc where dr=0 "
				+ GeneralSqlString.formInSQL("pk_cumandoc", pk_cumandoc.toArray(new String[0])));
		for (Object obj : objs) {
			pk_cubasdoc.add(((Object[]) obj)[0].toString());
		}
	}

	private void fillPkCubas(HashSet<String> pk_cumandoc, HashMap<String,String> pk_cudoc) throws SQLException {
		if (pk_cumandoc == null || pk_cumandoc.size() == 0)
			return;

		Object[] objs = selectBy2("select pk_cubasdoc,pk_cumandoc from bd_cumandoc where dr=0 "
				+ GeneralSqlString.formInSQL("pk_cumandoc", pk_cumandoc.toArray(new String[0])));
		//ncm_wanghfa_��ܽ��_����������֯����Ӧ������������̵�ID����_20110714
		for (Object obj : objs) {
			if(!pk_cudoc.containsKey(((Object[]) obj)[0].toString()))
			pk_cudoc.put(((Object[]) obj)[0].toString(),((Object[]) obj)[1].toString());
		}
	}
	
	private String buildSql(HashSet<String> pk_cumandoc, HashSet<String> cproductid, HashSet<String> cbiztypeid,String temptable) {
		StringBuffer where = new StringBuffer();
		String table =null;
		if (pk_cumandoc.size() > 0) {
			table = temptable;
			where.append(GeneralSqlString.formInSQL("pk_cumandoc", pk_cumandoc.toArray(new String[0]),table));
		}
		if (cproductid.size() > 0) {
			table = temptable +"b1";
			where.append(GeneralSqlString.formInSQL("cproductid", cproductid.toArray(new String[0]),table));
		}
		if (cbiztypeid.size() > 0) {
			table = temptable +"b2";
			where.append(GeneralSqlString.formInSQL("cbiztypeid", cbiztypeid.toArray(new String[0]),table));
		}

		if (where.length() > 4)
			return where.substring(4);
		else
			return where.toString();
	}

	private String getConditionS6(ConditionVO[] cvos, String pk_corp) throws SystemException, SQLException,
			NamingException {
		for (ConditionVO cvo : cvos) {
			if (cvo.getFieldCode().equals("pk_cumandoc")) {
				String pk_cubasdoc = ardmo.getDataManager().getCubasdocIDFromMan(cvo.getValue());
				if (ardmo.HM_AllCus == null)
					ardmo.HM_AllCus = ardmo.getDataManager().getAllCusInBas(pk_corp);
				ArrayList<String> al = new CreditUtilBS().getAllLeaf(pk_cubasdoc, ardmo.HM_AllCus[1], null, true, true);
				al.add(cvo.getValue());

				if (al.size() > 1) {
					String sql = GeneralSqlString.formInSQL("pk_cumandoc", al);
					return sql.substring(4);
				} else
					return "pk_cumandoc='" + cvo.getValue() + "'";

			}
		}// end for

		return null;
	}

	/**
	 * �滻��������ȫ����ѯ����
	 * 
	 * @param cvos
	 * @param pk_corp
	 * @return
	 * @throws SQLException
	 * @throws NamingException
	 * @throws SystemException
	 */
	private String[] getConditons(ConditionVO[] cvos, String pk_corp) throws SQLException, SystemException,
			NamingException {

		String[] sConditions = new String[7];
		StringBuffer sbfTempSql = new StringBuffer();
		// ������ѯ����

		if (cvos[0] == null || "null".equals(cvos[0].getFieldCode())) {
			return sConditions;
		}
		// �滻����fieldcode
		for (int i = 0; i < cvos.length; i++) {
			if ("creditlevel".equals(cvos[i].getFieldCode())) {
				cvos[i].setFieldCode("bd_cumandoc.creditlevel");
			}
			if ("pk_cumandoc".equals(cvos[i].getFieldCode())) {
				// String curCus = cvos[i].getValue().trim();
				String pk_cubasdoc = ardmo.getDataManager().getCubasdocIDFromMan(cvos[i].getValue());
				// String newpk = dm.getCumandocIDFrombas(new
				// CreditUtilBS().change2TopCusByFlag(pk_cubasdoc, HM_AllCus[0],
				// true),pk_corp);
				// ��������ϼ��ͻ�,���ò�����Ϊin,valueΪ�¼����пͻ�
				// if(!newpk.equals(curCus)){
				if (ardmo.HM_AllCus == null)
					ardmo.HM_AllCus = ardmo.getDataManager().getAllCusInBas(pk_corp);
				ArrayList<String> al = new CreditUtilBS().getAllLeaf(pk_cubasdoc, ardmo.HM_AllCus[1], null, true, true);
				// String oldvalue = cvos[i].getValue();
				al.add(cvos[i].getValue());

				if (al.size() > 1) {
					String[] manpks = new String[al.size()];
					al.toArray(manpks);
					StringBuffer sb = new StringBuffer();
					sb.append("(");
					for (int j = 0; j < al.size() - 1; j++) {
						sb.append("'").append(al.get(j)).append("',");
					}
					sb.append("'").append(al.get(al.size() - 1)).append("')");
					cvos[i].setOperaCode(" in ");
					cvos[i].setValue(sb.toString());
					// }
				}
				sConditions[6] = cvos[i].getWhereSQL(new ConditionVO[] { cvos[i] });
				cvos[i].setFieldCode("th.ccustomerid");
			}
			if ("cproductid".equals(cvos[i].getFieldCode())) {
				cvos[i].setFieldCode("cprolineid");
			}
			if ("cbiztypeid".equals(cvos[i].getFieldCode())) {
				cvos[i].setFieldCode("th.cbiztype");
			}
			if ("pk_pricegroupcorp".equals(cvos[i].getFieldCode())) {
				cvos[i].setFieldCode("bd_cumandoc.pk_pricegroupcorp");
			}

			if ("pk_salestru".equals(cvos[i].getFieldCode())) {
				cvos[i].setFieldCode("bd_cumandoc.pk_salestru");
			}

			if ("pk_areacl".equals(cvos[i].getFieldCode())) {
				if ("like".equals(cvos[i].getOperaCode())) {
					cvos[i].setOperaCode(" in ");
					cvos[i].setValue("( select pk_areacl  from  bd_areacl where areaclcode like '"
							+ cvos[i].getRefResult().getRefCode() + "%') ");
				}
				cvos[i].setFieldCode("bd_cubasdoc.pk_areacl");
			}
			if (i == 0) {
				if (!cvos[i].getNoLeft()) {
					sbfTempSql.append("(");
				}
				sbfTempSql.append(getWhereSql(cvos[i]));
				if (!cvos[i].getNoRight()) {
					sbfTempSql.append(")");
				}
			} else {
				if (" in ".equals(cvos[i].getOperaCode())) {
					sbfTempSql.append(getWhereSqlForNotOne(cvos[i]));
				} else
					sbfTempSql.append(cvos[i].getSQLStr());

			}

		}
		sConditions[0] = sbfTempSql.toString();

		// Ӧ��Ӧ����ѯ����-�����ͻ�
		sbfTempSql = new StringBuffer();
		// �滻Ӧ��Ӧ������fieldcode
		for (int i = 0; i < cvos.length; i++) {
			if ("th.ccustomerid".equals(cvos[i].getFieldCode())) {
				cvos[i].setFieldCode("bd_cumandoc.pk_cumandoc");
			}
			if ("th.cbiztype".equals(cvos[i].getFieldCode())) {
				cvos[i].setFieldCode("xslxbm");
			}
			if ("cprolineid".equals(cvos[i].getFieldCode())) {
				cvos[i].setFieldCode("productline");
			}

			if (i == 0) {
				if (!cvos[i].getNoLeft()) {
					sbfTempSql.append("(");
				}
				sbfTempSql.append(getWhereSql(cvos[i]));

				if (!cvos[i].getNoRight()) {
					sbfTempSql.append(")");
				}
			} else {
				if (" in ".equals(cvos[i].getOperaCode())) {
					sbfTempSql.append(getWhereSqlForNotOne(cvos[i]));
				} else
					sbfTempSql.append(cvos[i].getSQLStr());
			}
		}
		sConditions[1] = sbfTempSql.toString();

		// Ӧ��Ӧ����ѯ����-���ݿͻ�
		sbfTempSql = new StringBuffer();
		// �滻Ӧ��Ӧ������fieldcode
		for (int i = 0; i < cvos.length; i++) {
			if ("ordercusmandoc".equals(cvos[i].getFieldCode())) {
				cvos[i].setFieldCode("ksbm_cl");
			}
			if ("th.cbiztype".equals(cvos[i].getFieldCode())) {
				cvos[i].setFieldCode("xslxbm");
			}
			// if("productline".equals(cvos[i].getFieldCode())){
			// cvos[i].setFieldCode("tabappb.pk_productline");
			// }
			if (i == 0) {
				if (!cvos[i].getNoLeft()) {
					sbfTempSql.append("(");
				}
				sbfTempSql.append(getWhereSql(cvos[i]));

				if (!cvos[i].getNoRight()) {
					sbfTempSql.append(")");
				}
			} else {
				if (" in ".equals(cvos[i].getOperaCode())) {
					sbfTempSql.append(getWhereSqlForNotOne(cvos[i]));
				} else
					sbfTempSql.append(cvos[i].getSQLStr());
			}
		}
		sConditions[5] = sbfTempSql.toString();

		// ���ⵥ��ѯ����
		sbfTempSql = new StringBuffer();
		// �滻���ⵥ����fieldcode
		for (int i = 0; i < cvos.length; i++) {
			if ("ksbm_cl".equals(cvos[i].getFieldCode())) {
				cvos[i].setFieldCode("tgh.ccustomerid");
			} else if ("xslxbm".equals(cvos[i].getFieldCode())) {
				cvos[i].setFieldCode("tgh.cbiztype");
			} else if ("productline".equals(cvos[i].getFieldCode())) {
				cvos[i].setFieldCode("tab.pk_productline");
			}
			if (i == 0) {
				if (!cvos[i].getNoLeft()) {
					sbfTempSql.append("(");
				}
				sbfTempSql.append(getWhereSql(cvos[i]));
				// sbfTempSql.append(cvos[i].getFieldCode());
				// sbfTempSql.append(cvos[i].getOperaCode());
				// sbfTempSql.append("'");
				// sbfTempSql.append(cvos[i].getValue());
				// sbfTempSql.append("' ");
				if (!cvos[i].getNoRight()) {
					sbfTempSql.append(")");
				}
			} else {
				if (" in ".equals(cvos[i].getOperaCode())) {
					sbfTempSql.append(getWhereSqlForNotOne(cvos[i]));
				} else
					sbfTempSql.append(cvos[i].getSQLStr());
			}
		}
		sConditions[2] = sbfTempSql.toString();

		// ��Ӧ�յ���ѯ����
		sbfTempSql.setLength(0);
		for (int i = 0; i < cvos.length; i++) {
			if ("tgh.ccustomerid".equals(cvos[i].getFieldCode())) {
				cvos[i].setFieldCode("th.ccustomerid");
			} else if ("tgh.cbiztype".equals(cvos[i].getFieldCode())) {
				cvos[i].setFieldCode("tb.cbiztypeid");
			} else if ("tab.pk_productline".equals(cvos[i].getFieldCode())) {
				cvos[i].setFieldCode("tb.cproducelineid");
			}
			if (i == 0) {
				if (!cvos[i].getNoLeft()) {
					sbfTempSql.append("(");
				}
				sbfTempSql.append(getWhereSql(cvos[i]));
				// sbfTempSql.append(cvos[i].getFieldCode());
				// sbfTempSql.append(cvos[i].getOperaCode());
				// sbfTempSql.append("'");
				// sbfTempSql.append(cvos[i].getValue());
				// sbfTempSql.append("' ");
				if (!cvos[i].getNoRight()) {
					sbfTempSql.append(")");
				}
			} else {
				if (" in ".equals(cvos[i].getOperaCode())) {
					sbfTempSql.append(getWhereSqlForNotOne(cvos[i]));
				} else
					sbfTempSql.append(cvos[i].getSQLStr());
			}
		}
		sConditions[3] = sbfTempSql.toString();

		// ��Ʊ��ѯ����
		sbfTempSql.setLength(0);
		for (int i = 0; i < cvos.length; i++) {
			if ("th.ccustomerid".equals(cvos[i].getFieldCode())) {
				cvos[i].setFieldCode("tih.ccustomerid");
			} else if ("tb.cbiztypeid".equals(cvos[i].getFieldCode())) {
				cvos[i].setFieldCode("tih.cbiztype");
			} else if ("tb.cproducelineid".equals(cvos[i].getFieldCode())) {
				cvos[i].setFieldCode("tib.cprolineid");
			}
			if (i == 0) {
				if (!cvos[i].getNoLeft()) {
					sbfTempSql.append("(");
				}
				sbfTempSql.append(getWhereSql(cvos[i]));
				// sbfTempSql.append(cvos[i].getFieldCode());
				// sbfTempSql.append(cvos[i].getOperaCode());
				// sbfTempSql.append("'");
				// sbfTempSql.append(cvos[i].getValue());
				// sbfTempSql.append("' ");
				if (!cvos[i].getNoRight()) {
					sbfTempSql.append(")");
				}
			} else {
				if (" in ".equals(cvos[i].getOperaCode())) {
					sbfTempSql.append(getWhereSqlForNotOne(cvos[i]));
				} else
					sbfTempSql.append(cvos[i].getSQLStr());
			}
		}
		sConditions[4] = sbfTempSql.toString();

		return sConditions;
	}

	private String getWhereSql(ConditionVO cvo) {
		StringBuffer sbfTempSql = new StringBuffer();
		if (" in ".equals(cvo.getOperaCode())) {
			sbfTempSql.append(cvo.getFieldCode());
			sbfTempSql.append(cvo.getOperaCode());
			sbfTempSql.append(cvo.getValue());
		} else {
			sbfTempSql.append(cvo.getFieldCode());
			sbfTempSql.append(cvo.getOperaCode());
			sbfTempSql.append("'");
			sbfTempSql.append(cvo.getValue());
			sbfTempSql.append("' ");
		}
		return sbfTempSql.toString();
	}

	private String getWhereSqlForNotOne(ConditionVO cvo) {
		StringBuffer sbfTempSql = new StringBuffer();
		if (" in ".equals(cvo.getOperaCode())) {
			if (cvo.getLogic())
				sbfTempSql.append(" and ");
			else
				sbfTempSql.append(" or ");
			if (!cvo.getNoLeft()) {
				sbfTempSql.append("(");
			}

			sbfTempSql.append(cvo.getFieldCode());
			sbfTempSql.append(cvo.getOperaCode());
			sbfTempSql.append(cvo.getValue());
			if (!cvo.getNoRight()) {
				sbfTempSql.append(")");
			}
		} else {
			if (cvo.getLogic())
				sbfTempSql.append(" and ");
			else
				sbfTempSql.append(" or ");
			if (!cvo.getNoLeft()) {
				sbfTempSql.append("(");
			}
			sbfTempSql.append(cvo.getFieldCode());
			sbfTempSql.append(cvo.getOperaCode());
			sbfTempSql.append("'");
			sbfTempSql.append(cvo.getValue());
			sbfTempSql.append("' ");
			if (!cvo.getNoRight()) {
				sbfTempSql.append(")");
			}
		}
		return sbfTempSql.toString();
	}

}
