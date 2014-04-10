package nc.bs.so.so120;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import javax.naming.NamingException;
import nc.bs.framework.common.NCLocator;
import nc.bs.pub.SystemException;
import nc.bs.scm.pub.smart.SmartDMO;
import nc.impl.scm.so.credit.CreditImpl;
import nc.itf.so.so120.ICreditMny;
import nc.itf.uap.busibean.ISysInitQry;
import nc.ui.pub.para.SysInitBO_Client;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.so.credit.AccountMnyVO;
import nc.vo.so.credit.CreditUtil;
import nc.vo.so.credit.CuCreditVO;
import nc.vo.so.credit.ThreeYsInfo;
import nc.vo.so.credit.log.SoCreditLogHVO;
import nc.vo.so.pub.CreditResultVO;
import nc.vo.so.so120.CreditVO;
import nc.vo.so.so120.LimitTypeCVO;
import nc.vo.sp.pub.GeneralSqlString;
import nc.vo.uap.busibean.exception.BusiBeanException;

/**
 * ����ռ�û�ȡ �����ڽ���ȡ 
 * 
 * �������ڣ�(2004-3-12 8:46:44)
 * 
 * @author����÷ 
 */
public class CreditMny extends nc.bs.pub.DataManageObject implements ICreditMny {
  /**
   * CreditMny ������ע�⡣
   * 
   * @exception javax.naming.NamingException
   *                �쳣˵����
   * @exception nc.bs.pub.SystemException
   *                �쳣˵����
   */
  public CreditMny() throws javax.naming.NamingException, nc.bs.pub.SystemException {
    super();
  }

  /**
   * CreditMny ������ע�⡣
   * 
   * @param dbName
   *            java.lang.String
   * @exception javax.naming.NamingException
   *                �쳣˵����
   * @exception nc.bs.pub.SystemException
   *                �쳣˵����
   */
  public CreditMny(String dbName) throws javax.naming.NamingException, nc.bs.pub.SystemException {
    super(dbName);
  }

  /**
   * �������ڹ�������Ӧ�����ݵ���ʱ�� �������ڣ�(2004-5-11 11:18:18)
   * 
   * @param con
   *            java.sql.Connection
   * @param sTmptab
   *            java.lang.String
   */
  private String createTmpTab(Connection con, String sTmptab) throws Exception {
    //    /*
    //     * ����ʱ������Ҫ��ʱ����Ӧ�յ����
    //     */
    //    StringBuffer sqltext = new StringBuffer();
    //    sqltext
    //        .append("caccountmnyid        char(20)                 not null,");
    //    sqltext.append("pk_corp              char(4)             null,");
    //    sqltext.append("csalecorpid          char(20)                 null,");
    //    sqltext.append("cdeptid              char(20)           null,");
    //    sqltext.append("cemployeeid          char(20)           null,");
    //    sqltext.append("cproductid           char(20)           null,");
    //    sqltext.append("cbiztypeid           char(20)           null,");
    //    sqltext.append("pk_cubasdoc          char(20)           null,");
    //    sqltext.append("pk_cumandoc          char(20)           null,");
    //    sqltext.append("norderaccmny         decimal(20,8)      null,");
    //    sqltext.append("nbusiaccmny          decimal(20,8)      null,");
    //    sqltext.append("noutnotchecknumber       decimal(20,8)      null,");
    //    sqltext.append("nsubmny       decimal(20,8)      null,");
    //    sqltext.append("nsalemny       decimal(20,8)      null,");
    //    sqltext.append("binitflag        char(1)            null,");
    //    sqltext.append("ts             char(19)           null");
    //
    //    nc.bs.mw.sqltrans.TempTable tmptabm = new nc.bs.mw.sqltrans.TempTable();
    //    return tmptabm.createTempTable(con, sTmptab, sqltext.toString(), null);
    String[] sTabColumn = new String[] { "caccountmnyid", "pk_corp", "csalecorpid", "cdeptid", "cemployeeid",
        "cproductid", "cbiztypeid", "pk_cubasdoc", "pk_cumandoc", "nnotauditaccmny", "nnotoutaccmny",
        "noutaccmny", "nestimateaccmny",
        "nassuredaccmny", "nsubmny", "nsalemny", "binitflag"
    //,"ts"
    };

    String[] sColumnType = new String[] { "char(20)", "char(4)", "char(20)", "char(20)", "char(20)", "char(20)",
        "char(20)", "char(20)", "char(20)", "decimal(20,8)", "decimal(20,8)", "decimal(20,8)", "decimal(20,8)",
        "decimal(20,8)", "decimal(20,8)",
        "decimal(20,8)", "char(1)"
    //,"char(19)"
    };
    nc.bs.scm.pub.TempTableDMO tempTableDMO = new nc.bs.scm.pub.TempTableDMO();
    return tempTableDMO.getTempStringTable(sTmptab, sTabColumn, sColumnType, null, new ArrayList());
  }

  public UFDouble[] getAccountMny(String pk_cumandoc) throws Exception {
    if (pk_cumandoc == null)
      return null;
  
    return getAccountMny(pk_cumandoc, null);
  }

  /*
   * ���� Javadoc��
   * 
   * @see nc.bs.so.so120.ICreditMny#getAccountMny(nc.vo.scminter.so6.AccountMnyVO)
   */
  public UFDouble[] getAccountMny(AccountMnyVO voParm) throws Exception {

    StringBuffer sql = new StringBuffer();
    sql.append("select sum(norderaccmny),sum(noutnotchecknumber),sum(nbusiaccmny),sum(nsubmny) ");
    sql.append("\n from so_accountmny ");
    sql.append("\n where ");
    boolean bConditioned = false;
    // ע�⣺�����������У����δѡ����˾��������֯�����š�ҵ��Ա����Ʒ�ߡ�ҵ�����͡�
    // �ͻ��е��κ�һ����Ҫ���Ը�����
    if (voParm.m_pk_corp != null) {// ��˾
      if (bConditioned) {
        sql.append(" and ");
      }
      sql.append(" pk_corp='");
      sql.append(voParm.m_pk_corp);
      sql.append("' ");
      bConditioned = true;
    }
    if (voParm.m_csalecorpid != null) {// ������֯
      if (bConditioned) {
        sql.append(" and ");
      }
      sql.append(" csalecorpid='");
      sql.append(voParm.m_csalecorpid);
      sql.append("' ");
      bConditioned = true;
    }
    if (voParm.m_cdeptid != null) {// ����
      if (bConditioned) {
        sql.append(" and ");
      }
      sql.append(" cdeptid='");
      sql.append(voParm.m_cdeptid);
      sql.append("' ");
      bConditioned = true;
    }
    if (voParm.m_cemployeeid != null) {// ҵ��Ա
      if (bConditioned) {
        sql.append(" and ");
      }
      sql.append(" cemployeeid='");
      sql.append(voParm.m_cemployeeid);
      sql.append("' ");
      bConditioned = true;
    }
    if (voParm.m_cproductid != null) {// ��Ʒ��
      if (bConditioned) {
        sql.append(" and ");
      }
      sql.append(" cproductid='");
      sql.append(voParm.m_cproductid);
      sql.append("' ");
      bConditioned = true;
    }
    if (voParm.m_cbiztypeid != null) {// ҵ������
      if (bConditioned) {
        sql.append(" and ");
      }
      sql.append(" cbiztypeid='");
      sql.append(voParm.m_cbiztypeid);
      sql.append("' ");
      bConditioned = true;
    }
    if (voParm.m_pk_cumandoc != null) {// �ͻ�������
      if (bConditioned) {
        sql.append(" and ");
      }
      sql.append(" pk_cumandoc='");
      sql.append(voParm.m_pk_cumandoc);
      sql.append("' ");
      bConditioned = true;
    }

    UFDouble[] dRE = new UFDouble[6];

    Connection con = null;
    PreparedStatement stmt = null;
    try {
      con = getConnection();
      stmt = con.prepareStatement(sql.toString());
      ResultSet rs = stmt.executeQuery();
      Object obj = null;

      CreditUtil cUtil = new CreditUtil();

      while (rs.next()) {
        for (int i = 0, len = dRE.length; i < len; i++) {
          obj = rs.getObject(i + 1);
          dRE[i] = cUtil.convertObjToUFDouble(obj);
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
      }
    }
    for (int i = 0, len = dRE.length; i < len; i++) {
      if (dRE[i] == null) {
        dRE[i] = CreditUtil.ZERO;
      }
    }
    return dRE;

  }

  /**
   * ����ȷ���Ŀͻ�������ü�¼�����ض�Ӧ������Ӧ������ �������ڣ�(2004-3-12 9:45:28)
   * 
   * @return nc.vo.pub.lang.UFDouble[]
   * @param voCredit
   *            nc.vo.so.so120.CreditVO
   */
  private UFDouble[] getCreditAccountMny(Connection con, CreditVO voCredit, String sTmpTab) throws SQLException {
    return getCreditAccountMny(con, voCredit, sTmpTab, null, true);
  }

  /**
   * ����ȷ���Ŀͻ�������ü�¼�����ض�Ӧ������Ӧ������ �������ڣ�(2004-3-12 9:45:28)
   * 
   * @return nc.vo.pub.lang.UFDouble[]
   * @param voCredit
   *            nc.vo.so.so120.CreditVO
   */
  private UFDouble[] getCreditAccountMnyNoType(Connection con, CreditVO voCredit, String sTmpTab) throws SQLException {
    /*
     * select
     * sum(norderaccmny),sum(nbusiaccmny),sum(nfinanceaccmny),sum(nbusiadjustmny)
     * from so_accountmny acc where pk_corp='weik' and exists( select
     * climittypebid from so_limittype_b area where area. climittypecid
     * ='woi' and (area. cproductid ='").append(CreditUtil.NULL_DATA).append("' or area. cproductid =acc.
     * cproductid) and (area. cbiztypeid ='").append(CreditUtil.NULL_DATA).append("' or area. cbiztypeid =acc.
     * cbiztypeid) and area.dr=0 ) and pk_cumandoc ='wiedhi'
     * 
     */
    StringBuffer sql = new StringBuffer();
    sql.append("select sum(nnotauditaccmny), sum(nnotoutaccmny), sum(noutaccmny), sum(nestimateaccmny), sum(nassuredaccmny), sum(nsubmny) ");
    if (sTmpTab == null) {
      sql.append("\n from so_accountmny acc ");
    } else {
      sql.append("\n from ");
      sql.append(sTmpTab);
      sql.append(" acc ");
    }
    sql.append("\n where pk_corp='");
    sql.append(voCredit.m_pk_corp);
    sql.append("' and pk_cumandoc='");
    sql.append(voCredit.m_pk_cumandoc);
    sql.append("'");

    UFDouble[] dRE = new UFDouble[4];

    boolean bConnectionSended = (con != null); // true-�����ⲿ����

    PreparedStatement stmt = null;
    try {
      if (!bConnectionSended) {
        con = getConnection();
      }
      stmt = con.prepareStatement(sql.toString());
      ResultSet rs = stmt.executeQuery();
      Object obj = null;

      CreditUtil cUtil = new CreditUtil();

      while (rs.next()) {
        for (int i = 0; i < 4; i++) {
          obj = rs.getObject(i + 1);
          dRE[i] = cUtil.convertObjToUFDouble(obj);
        }
      }
    } finally {
      try {
        if (stmt != null) {
          stmt.close();
        }
      } catch (Exception e) {
      }
      if (!bConnectionSended) {
        // �ⲿû�д������ӣ��Źر�
        try {
          if (con != null) {
            con.close();
          }
        } catch (Exception e) {
        }
      }
    }
    for (int i = 0; i < 4; i++) {
      if (dRE[i] == null) {
        dRE[i] = CreditUtil.ZERO;
      }
    }
    return dRE;
  }

  /*
   * ���� Javadoc��
   * 
   * @see nc.bs.so.so120.ICreditMny#getCreditMny(nc.vo.so.so120.CreditVO)
   */
  public UFDouble getCreditMny(CreditVO voCredit,SoCreditLogHVO loghvo) throws java.lang.Exception {

    if (voCredit == null) {
      return CreditUtil.ZERO;
    }

    UFDouble[] dAR = getCreditAccountMny(null, voCredit, null);

    // ���ù�ʽ���㣬��������ռ��
    String sFormula = voCredit.m_voccupiedfun;
    if (loghvo!=null) {
      /**��¼��־*/
      loghvo.setSformula(sFormula);
      loghvo.setNbeforecorpordermny(dAR[0]);
      loghvo.setNbeforecorpoutmny(dAR[1]);
      loghvo.setNbeforecorpbusimny(dAR[2]);
      loghvo.setNbeforecorpsubmny(dAR[3]);
    }
    UFBoolean CC07 = new DataManager().getCC07();
    return new CreditUtil().getCreditMny(sFormula, dAR,CC07);

  }

  /*
   * ���� Javadoc��
   * 
   * @see nc.bs.so.so120.ICreditMny#getCreditMny(nc.vo.so.so120.CreditVO,
   *      nc.vo.scminter.so6.AccountMnyVO[])
   */
  public UFDouble[] getCreditMny(CreditVO voCredit, AccountMnyVO[] voAccountMnyModify,SoCreditLogHVO loghvo) throws java.lang.Exception {
    // ����ʱ������Ҫ��ʱ����Ӧ�յ����
    if (voAccountMnyModify == null || voAccountMnyModify.length == 0) {
      return new UFDouble[] { getCreditMny(voCredit,loghvo), CreditUtil.ZERO };
    }
    boolean bCreditNull = false;
    if (voCredit == null) {
      bCreditNull = true;
    }

    Connection con = null;
    try {
      con = getConnection();
      // ������ʱ��
      String sTmp = "t_so_creaccmny";
      String sTmpTab = createTmpTab(con, sTmp);
      if (sTmpTab == null) {
        throw new Exception(nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("SCMCommon",
            "UPPSCMCommon-000045")/*
         * @res
         * "������ʱ��ʧ�ܣ�"
         */);
      }

      // ��ʱ���������
      RenovateAR renovate = new RenovateAR();
      renovate.saveAccountMnyTmpTab(con, voAccountMnyModify, sTmpTab);

      UFDouble[] dAR_base = null;
      if (!bCreditNull)
        dAR_base = getCreditAccountMny(con, voCredit, null);

      UFDouble[] dAR_tmp = getCreditAccountMny(con, voCredit, sTmpTab);

      // ��ʱ������û�����ˣ�����ɾ���ˣ�

      // ���ù�ʽ���㣬��������ռ��
      String sFormula = voCredit == null ? "0" : voCredit.m_voccupiedfun;
      /**��¼��־*/
      if(loghvo != null){
        loghvo.setSformula(sFormula);
        if(dAR_base != null){
          loghvo.setNbeforecorpordermny(dAR_base[0]);
          loghvo.setNbeforecorpoutmny(dAR_base[1]);
          loghvo.setNbeforecorpbusimny(dAR_base[2]);
          loghvo.setNbeforecorpsubmny(dAR_base[3]);
        }
        if(dAR_tmp != null){
          loghvo.setNcurrentbillordermny(dAR_tmp[0]);
          loghvo.setNcurrentbilloutmny(dAR_tmp[1]);
          loghvo.setNcurrentbillbusimny(dAR_tmp[2]);
        }
      }
      UFBoolean CC07 = new DataManager().getCC07();
      if (!bCreditNull)
        return new UFDouble[] { new CreditUtil().getCreditMny(sFormula, dAR_base,CC07),
            new CreditUtil().getCreditMny(sFormula, dAR_tmp,CC07) };
      else
        return new UFDouble[] { CreditUtil.ZERO, new CreditUtil().getCreditMny(sFormula, dAR_tmp,CC07) };
      // }

    } finally {
      // �ر�����
      try {
        if (con != null) {
          con.close();
        }
      } catch (Exception e) {
      }
    }
  }

  public UFDouble getCreditMnyTemp(CreditVO voCredit, AccountMnyVO[] voAccountMnyModify) throws Exception {
    Connection con = null;
    try {
      con = getConnection();
      // ������ʱ��
      String sTmp = "t_so_creaccmny";
      String sTmpTab = createTmpTab(con, sTmp);
      if (sTmpTab == null) {
        throw new Exception(nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("SCMCommon",
            "UPPSCMCommon-000045")/*
         * @res
         * "������ʱ��ʧ�ܣ�"
         */);
      }

      // ��ʱ���������
      RenovateAR renovate = new RenovateAR();
      renovate.saveAccountMnyTmpTab(con, voAccountMnyModify, sTmpTab);

      UFDouble[] dAR_tmp = getCreditAccountMny(con, voCredit, sTmpTab);

      // ��ʱ������û�����ˣ�����ɾ���ˣ�

      // ���ù�ʽ���㣬��������ռ��
      String sFormula = voCredit.m_voccupiedfun;
      UFBoolean CC07 = new DataManager().getCC07();
      return new CreditUtil().getCreditMny(sFormula, dAR_tmp,CC07);
      // }

    } finally {
      // �ر�����
      try {
        if (con != null) {
          con.close();
        }
      } catch (Exception e) {
      }
    }
  }

  /*
   * ��������Ϊ���۶����ṩ�ӿ�
   * 
   * �ӿ����룺�ͻ���ҵ������
   * 
   * �ӿ���������ö�ȡ�����ռ��
   * 
   * �ڲ��㷨��
   * 
   * 1�� ��ȡ����SO36�������������ڼ��ͳ��Ӧ���Ƿ��ҵ�����ͽ��У�
   * 
   * 2��
   * �ҵ�ƥ��Ķ�����͡��ͻ����ü�¼���������Ϊ��ƥ�����еĸÿͻ����ö�ȼ�¼���������Ϊ�ǣ�ƥ��ÿͻ���������ͷ���ҵ�����������Ŀͻ���ȼ�¼��
   * 
   * 3�� ����ҵ�����ͶԶ�����͵�ɸѡ������������������͵�ҵ�����Ͳŷ���������
   * ��������͵ķ�Χ����������ҵ�����ͣ������������ҵ�����ͣ������ڷ�Χ֮�ڣ�
   * 
   * 4�� ����ռ�õļ��㣺����Ϊ�ǣ����ͻ���ҵ�����͵�ҵ��Ӧ��+���������⣻����Ϊ�񣬱����ҵ��Ӧ��+����������
   * 
   * 5�� ע�⣺���۶����ڵ�ԭ�е����ò�ѯ���ǽ����Զ������ڵĿͻ���Ϊ�����ģ��ڲ���Ϊ�ǵ�����£��������ѯ�Ĵ���˼·�Ѿ������˲�һ��
   * 
   */
  /*
   * ���� Javadoc��
   * 
   * @see nc.bs.so.so120.ICreditMny#getCreditMnyLeftNew(java.lang.String,
   *      java.lang.String, java.lang.String)
   */
  public nc.vo.dm.pub.DMDataVO[] getCreditMnyLeftNew(String pk_corp, String pk_cumandoc, String cbiztype)
      throws Exception {
    if (pk_cumandoc == null || cbiztype == null || pk_corp == null)
      return null;

    nc.vo.dm.pub.DMDataVO[] results = null;
    nc.vo.dm.pub.DMDataVO onevo = null;
    String t_so_crelimit_biz = null;
    StringBuffer sqlbuf = new StringBuffer();
    SmartDMO dmo = new SmartDMO();
    UFDouble dResult = null;

    CreditUtil cUtil = new CreditUtil();

    boolean bProduct = false;
    // java.util.HashMap creditmny = new java.util.HashMap();
    try {
      bProduct = cUtil.isProductline(pk_corp);
      UFDouble zero = CreditUtil.ZERO;

      ISysInitQry sysinitQry = (ISysInitQry) NCLocator.getInstance().lookup(ISysInitQry.class.getName());

      UFBoolean SO36 = sysinitQry.getParaBoolean(pk_corp, "SO36");

      //��Ʒ��+ҵ������ ������ռ�ú���
      HashMap map = new HashMap();

      //      ȡ�ò�Ʒ��+ҵ������ ������ռ�ú�����hashmap
      sqlbuf.append("select tmp.cbiztypeid,tmp.cproductid,tm.voccupiedfun \n").append(
          "from so_cucredit tm,so_limittype_b tmp \n").append(
          "where tm.climittypecid=tmp.climittypecid and tm.dr = 0 and tmp.dr=0 and tm.pk_corp = '").append(
          pk_corp).append("' and tm.pk_cumandoc = '").append(pk_cumandoc).append("'");
      Object[] o = dmo.selectBy2(sqlbuf.toString());
      if (o != null) {

        for (int i = 0; i < o.length; i++) {
          Object[] o1 = (Object[]) o[i];
          if (o1 != null) {
            String s = o1[0] == null ? "" : o1[0].toString();
            String s1 = o1[1] == null ? "" : o1[1].toString();
            if (bProduct)
              s = s + s1;
            map.put(s, new Integer(o1[2] == null ? "0" : o1[2].toString()));
          }
        }

      }

      sqlbuf.setLength(0);
      if (SO36 != null && SO36.booleanValue()) {
        // ����ҵ�����ͶԶ�����͵�ɸѡ������������������͵�ҵ�����Ͳŷ���������
        // ��������͵ķ�Χ����������ҵ�����ͣ������������ҵ�����ͣ������ڷ�Χ֮�ڣ�
        t_so_crelimit_biz = dmo.createTempTable("t_so_crelimit_biz ",
            "climittypecid char(20),cbiztypeid char(20),cproductid char(20)", "climittypecid");
        sqlbuf
            .append("insert into ")
            .append(t_so_crelimit_biz)
            .append(
                " (climittypecid, cbiztypeid,cproductid) \n select distinct climittypecid, cbiztypeid,cproductid from so_limittype_b tm where \n")
            .append("pk_corp ='")
            .append(pk_corp)
            .append("' and tm.dr = 0 and cbiztypeid = '")
            .append(cbiztype)
            .append(
                "' \n and not exists(select climittypecid from so_limittype_b ta where tm.climittypecid=ta.climittypecid")
            .append(" and tm.cbiztypeid!=ta.cbiztypeid and ta.dr = 0) and cbiztypeid is not null");
        dmo.executeUpdate(sqlbuf.toString(), new ArrayList(), new ArrayList());

        sqlbuf.setLength(0);
        sqlbuf.append("select tmp.cbiztypeid,tmp.cproductid, \n").append(
            "sum(isnull(nlimitmny,0)+isnull(ngrouplimitmny,0)) \n").append("from so_cucredit tm,").append(
            t_so_crelimit_biz).append(" tmp \n").append(
            "where tm.climittypecid=tmp.climittypecid and tm.dr = 0   and tm.pk_corp = '").append(pk_corp)
            .append("' and tm.pk_cumandoc = '").append(pk_cumandoc).append(
                "' group by tmp.cbiztypeid,tmp.cproductid ");

      } else {
        sqlbuf.append("select tmp.cbiztypeid,tmp.cproductid, \n").append(
            "sum(isnull(nlimitmny,0)+isnull(ngrouplimitmny,0)) \n").append(
            "from so_cucredit tm,so_limittype_b tmp \n").append(
            "where tm.climittypecid=tmp.climittypecid  and tm.dr = 0 and tm.pk_corp = '").append(pk_corp)
            .append("' and tm.pk_cumandoc = '").append(pk_cumandoc).append(
                "' group by tmp.cbiztypeid,tmp.cproductid");
      }
      AccountMnyVO accvo = new AccountMnyVO();
      accvo.m_pk_corp = pk_corp;
      accvo.m_pk_cumandoc = pk_cumandoc;
      if (SO36 != null && SO36.booleanValue())
        accvo.m_cbiztypeid = cbiztype;

      map = getAccountMnyForOrderQuery(accvo, map, bProduct);
      ArrayList resultList = new ArrayList();

      o = dmo.selectBy2(sqlbuf.toString());
      if (o != null && o.length > 0) {
        for (int j = 0; j < o.length; j++) {

          UFDouble creditocc = null;
          String key = "";
          Object[] o1 = (Object[]) o[j];
          if (o1 != null && o1.length > 2) {
            onevo = new nc.vo.dm.pub.DMDataVO();
            if (o1[0] != null) {
              key += (String) o1[0];
              onevo.setAttributeValue("cbiztypeid", o1[0]);
            }
            if (bProduct) {
              if (o1[1] != null) {
                key += (String) o1[1];
                onevo.setAttributeValue("cprolineid", o1[1]);
              }
            }

            if (o1[2] != null)
              dResult = cUtil.convertObjToUFDouble(o1[2]);
            else
              dResult = zero;
            onevo.setAttributeValue("ncreditmny", dResult);
            if (map.get(key) != null)
              creditocc = (UFDouble) map.get(key);
            if (creditocc == null)
              creditocc = zero;
            onevo.setAttributeValue("ncredityetmny", dResult.sub(creditocc));
            resultList.add(onevo);
          }
        }
      }

      if (resultList.size() > 0) {
        results = new nc.vo.dm.pub.DMDataVO[resultList.size()];
        resultList.toArray(results);
      }

    } finally {
      // ɾ��������ʱ��
      try {
        if (t_so_crelimit_biz != null)
          dmo.dropTempTable(t_so_crelimit_biz);
      } catch (Exception e) {
        nc.vo.scm.pub.SCMEnv.out(e);
      }

    }
    return results;
  }

  public HashMap getAccountMnyForOrderQuery(AccountMnyVO voParm, HashMap mapocc) throws Exception {
    return getAccountMnyForOrderQuery(voParm, mapocc, true);
  }

  /*
   * ���� Javadoc��
   * 
   * @see nc.bs.so.so120.ICreditMny#getAccountMnyForOrderQuery(nc.vo.scminter.so6.AccountMnyVO,
   *      java.util.HashMap)
   */
  public HashMap getAccountMnyForOrderQuery(AccountMnyVO voParm, HashMap mapocc, boolean bProduct) throws Exception {
    /*
     * select
     * sum(norderaccmny),sum(nbusiaccmny),sum(nfinanceaccmny),sum(nbusiadjustmny)
     * from so_accountmny where Pk_corp='nwl' and csalecorpid='wd' and
     * cdeptid='wah' and cemployeeid='whdoei' and cproductid='wei' and
     * cbiztypeid='wqo' and pk_cumandoc='wqpo'
     */

    CreditUtil cUtil = new CreditUtil();

    UFDouble zero = CreditUtil.ZERO;
    HashMap map = new HashMap();
    StringBuffer sql = new StringBuffer();

    StringBuffer sqlsk = new StringBuffer();
    sqlsk.append(" select xslxbm,productline,sum(tb.bbye) from  arap_djfb tb, arap_djzb th ");
    sqlsk.append(" where tb.vouchid = th.vouchid  and th.djdl='sk'  ");
    sqlsk.append(cUtil.getWhereForSK(" th.djzt "));
    sqlsk.append("  and th.dr = 0 and tb.dr = 0 and tb.wldx=0 and  ");
    //sbfTemp.append("  and ksbm_cl = pk_cumandoc  ");

    //xslxbm

    sql
        .append("select cbiztypeid,cproductid,sum(norderaccmny),sum(noutnotchecknumber),sum(nbusiaccmny),sum(nsubmny) ");
    sql.append("\n from so_accountmny ");
    sql.append("\n where ");
    boolean bConditioned = false;
    // ע�⣺�����������У����δѡ����˾��������֯�����š�ҵ��Ա����Ʒ�ߡ�ҵ�����͡�
    // �ͻ��е��κ�һ����Ҫ���Ը�����
    if (voParm.m_pk_corp != null) {// ��˾
      if (bConditioned) {
        sql.append(" and ");
        sqlsk.append(" and ");
      }
      sql.append(" pk_corp='");
      sql.append(voParm.m_pk_corp);
      sql.append("' ");

      sqlsk.append(" tb.dwbm='");
      sqlsk.append(voParm.m_pk_corp);
      sqlsk.append("' ");

      bConditioned = true;
    }
    if (voParm.m_cbiztypeid != null) {// ҵ������
      if (bConditioned) {
        sql.append(" and ");
        sqlsk.append(" and ");
      }
      sql.append(" cbiztypeid='");
      sql.append(voParm.m_cbiztypeid);
      sql.append("' ");

      sqlsk.append(" xslxbm='");
      sqlsk.append(voParm.m_cbiztypeid);
      sqlsk.append("' ");

      bConditioned = true;
    }
    if (voParm.m_pk_cumandoc != null) {// �ͻ�������
      if (bConditioned) {
        sql.append(" and ");
        sqlsk.append(" and ");
      }
      sql.append(" pk_cumandoc='");
      sql.append(voParm.m_pk_cumandoc);
      sql.append("' ");

      sqlsk.append(" ordercusmandoc='");
      sqlsk.append(voParm.m_pk_cumandoc);
      sqlsk.append("' ");

      bConditioned = true;
    }
    sql.append(" group by cbiztypeid,cproductid ");
    sqlsk.append(" group by xslxbm,productline ");

    try {

      SmartDMO dmo = new SmartDMO();
      Object[] o = dmo.selectBy2(sql.toString());
      UFBoolean CC07 = new DataManager().getCC07();
      if (o != null && o.length > 0) {
        for (int j = 0; j < o.length; j++) {
          String key = "";
          UFDouble result = zero;
          Object[] o1 = (Object[]) o[j];
          if (o1 != null && o1.length > 5) {
            if (o1[0] != null && voParm.m_cbiztypeid != null)
              key += (String) o1[0];
            else if (o1[0] != null && voParm.m_cbiztypeid == null)
              key += (String) CreditUtil.NULL_DATA;
            else if (o1[0] == null)
              key += (String) CreditUtil.NULL_DATA;
            if (bProduct) {
              if (o1[1] != null)
                key += (String) o1[1];
            }
            String creditOcc = mapocc.get(key) == null ? "0" : mapocc.get(key).toString();
            result = cUtil.getCreditMny(creditOcc, new UFDouble[] { cUtil.convertObjToUFDouble(o1[2]),
                cUtil.convertObjToUFDouble(o1[3]), cUtil.convertObjToUFDouble(o1[4]),
                cUtil.convertObjToUFDouble(o1[5]) },CC07);
            if (map.containsKey(key)) {
              result = cUtil.convertObjToUFDouble(map.get(key)).add(result);
            }
            map.put(key, result);
          }
        }
      }

      o = dmo.selectBy2(sqlsk.toString());
      if (o != null && o.length > 0) {
        for (int j = 0; j < o.length; j++) {
          String key = "";
          UFDouble result = zero;
          Object[] o1 = (Object[]) o[j];
          if (o1 != null && o1.length > 2) {
            if (o1[0] != null && voParm.m_cbiztypeid != null)
              key += (String) o1[0];
            else if (o1[0] != null && voParm.m_cbiztypeid == null)
              key += (String) CreditUtil.NULL_DATA;
            else if (o1[0] == null)
              key += (String) CreditUtil.NULL_DATA;
            if (bProduct) {
              if (o1[1] != null)
                key += (String) o1[1];
            }
            if (map.containsKey(key)) {
              result = cUtil.convertObjToUFDouble(map.get(key)).sub(cUtil.convertObjToUFDouble(o1[2]));
              map.put(key, result);
            }

          }
        }
      }
    } finally {

    }

    return map;

  }

  /**
   * @param pk_cumandoc ���̹�����id
   * @param loginDate   ��½����
   * @return ����Ӧ�� ҵ��Ӧ�� �������
   * @throws Exception
   */
  public UFDouble[] getAccountMny(String pk_cumandoc, UFDate loginDate) throws Exception {
    if (pk_cumandoc == null)
      return null;
    
    if (loginDate == null)
      loginDate = new UFDate(new Date());
    CreditUtilBS util = new CreditUtilBS();
    DataManager dm = getDataManager();
    String pk_corp = dm.getCubasdocPk_corpFromMan(pk_cumandoc);
    String pk_cubasdoc = dm.getCubasdocIDFromMan(pk_cumandoc);
    HashMap[] hm = dm.getAllCusInBas(pk_corp, pk_cubasdoc);
    String newpk = util.change2TopCusByFlag(pk_cubasdoc, hm[0], true);
    if (!newpk.equals(pk_cubasdoc)) {
      pk_cumandoc = ((Object[]) hm[0].get(newpk))[2].toString();
      pk_cubasdoc = newpk;
    }
    MatchCredit matchCredit = new MatchCredit();

    //0-4:���Ӧ�� 5:���Ӧ�� 6:�տ�δ���� 7:���ö�� 8:����ռ��
    UFDouble[] result = new UFDouble[9];
    
    DataManager dmo = new DataManager();
    
    // ƥ��ͻ�����
    CuCreditVO[] creditvos = matchCredit.getMatchCuCreditVO(pk_cumandoc, loginDate, pk_corp);

    CreditUtil cUtil = new CreditUtil();
    
    creditvos = cUtil.prepareVO(creditvos, pk_corp);
    
    //�ͻ����տ����
    UFDouble skMny = dmo.getSKMnyForCorp(pk_cubasdoc,pk_corp);
    UFDouble returnNotOutMny = CreditUtil.ZERO;
    if (creditvos != null) {
      for (int i = 0; i < creditvos.length; i++) {
        //      ������䵽�ĵ�һ����������Ǽ��Ŷ��,pk_cumandoc�Լ�CLIMITTYPECID��Ч,��Ҫ���²�ѯ
        //pk_cubasdoc��pk_cumandoc���,ת��Ϊ��˾pk
        if (creditvos[i].getPk_cubasdoc()!=null&&(creditvos[i].getPk_cumandoc()==null||creditvos[i].getPk_cubasdoc().equals(creditvos[i].getPk_cumandoc()))) {
          creditvos[i].setPk_cumandoc(getDataManager()
              .getCumandocIDFrombas(
                  creditvos[i].getPk_cubasdoc(), pk_corp));
          creditvos[i].setClimittypecid(getDataManager()
              .getClimittypecid(creditvos[i].getClimittypeid(),
                  pk_corp));
          creditvos[i].setPk_corp(pk_corp);
        }
      }
    }
    //û���ҵ�ƥ�䣬�Ƿ���п���
    UFBoolean CC03 = dmo.getCC03();

    //
    UFBoolean CC06 = dmo.getCC06();//����ռ�ü����Ƿ�����˻����뵥���˻�����
    UFBoolean CC07 = dmo.getCC07();

    if (creditvos == null || creditvos.length <= 0) {

      //����޶�Ƚ������ÿ��ƣ����ö��Ϊ0������ռ��=���Ӧ��-�������Ӧ��
      if (CC03.booleanValue()) {
        AccountMnyVO vo = new AccountMnyVO();
        vo.m_pk_cumandoc = pk_cumandoc;
        ////��ѯ���Ӧ��
        UFDouble[] dd = getAccountMny(vo);

        if (dd != null && dd.length > 0) {
          result[0] = cUtil.convertObjToUFDouble(dd[0]);
          result[1] = cUtil.convertObjToUFDouble(dd[1]);
          result[2] = cUtil.convertObjToUFDouble(dd[2]);
          result[3] = cUtil.convertObjToUFDouble(dd[3]);
          result[4] = cUtil.convertObjToUFDouble(dd[4]);
          result[5] = cUtil.convertObjToUFDouble(dd[5]);
          result[6] = cUtil.convertObjToUFDouble(skMny);
          result[7] = CreditUtil.ZERO;
          result[8] = result[0].add(result[1]).add(result[2]).add(result[3]).add(result[4]).sub(result[6]);
          if(CC07.booleanValue())
            result[8] = result[8].sub(result[5]);
        }
        
      } 
      //������ 
      else {
        result[0] = CreditUtil.ZERO;
        result[1] = CreditUtil.ZERO;
        result[2] = CreditUtil.ZERO;
        result[3] = CreditUtil.ZERO;
        result[4] = CreditUtil.ZERO;
        result[5] = CreditUtil.ZERO;
        result[6] = cUtil.convertObjToUFDouble(skMny);
        result[7] = CreditUtil.ZERO;
        result[8] = CreditUtil.ZERO.sub(result[6]);
      }
    }
    //ƥ�䵽
    else {
      Connection con = getConnection();
      for (int i = 0; i < creditvos.length; i++) {
        CreditVO vo = convertToCreditVO(creditvos[i]);
        UFDouble[] dd = getCreditAccountMny(con, vo, null, null, true);
        // δ���Ӧ��
        result[0] = cUtil.convertObjToUFDouble(result[0]).add(cUtil.convertObjToUFDouble(dd[0]));
        // δ����Ӧ��
        result[1] = cUtil.convertObjToUFDouble(result[1]).add(cUtil.convertObjToUFDouble(dd[1]));
        // �ѳ���Ӧ��
        result[2] = cUtil.convertObjToUFDouble(result[2]).add(cUtil.convertObjToUFDouble(dd[2]));
        // δȷ��Ӧ��
        result[3] = cUtil.convertObjToUFDouble(result[3]).add(cUtil.convertObjToUFDouble(dd[3]));
        // ��ȷ��Ӧ��
        result[4] = cUtil.convertObjToUFDouble(result[4]).add(cUtil.convertObjToUFDouble(dd[4]));
        // ���Ӧ��
        result[5] = cUtil.convertObjToUFDouble(result[5]).add(cUtil.convertObjToUFDouble(dd[5]));
        
        // ���ö��
        //TODO ???
//        result[7] = cUtil.convertObjToUFDouble(result[7]).add();
        
        result[7] = cUtil.convertObjToUFDouble(result[7]).add(cUtil.maxUFDoble(cUtil.convertObjToUFDouble(creditvos[i].getAttributeValue("nlimitmny")).add(
            cUtil.convertObjToUFDouble(creditvos[i].getAttributeValue("ngrouplimitmny"))),
                cUtil.convertObjToUFDouble(creditvos[i].getAttributeValue("ngtotallimitmny"))));
        
        //����ռ��
        result[8] = cUtil.convertObjToUFDouble(result[8]).add(cUtil.getCreditMny(creditvos[i].getVoccupiedfun(), dd,CC07));
        //�ͻ����˻�δ������
        returnNotOutMny = returnNotOutMny .add(dmo.getSOReturnMny(pk_cumandoc,null, creditvos[i]));
      }//end for
      
      //��ȥ�տ�Ϊ�������
      result[8] = result[8].sub(skMny);
      //��ȥ�˻�δ������
      if(CC06.booleanValue())
        result[8] = result[8].sub(returnNotOutMny);
        
    }
    
    UFDouble[] newresult = new UFDouble[3];
    newresult[0] = cUtil.convertObjToUFDouble(result[0]).add(cUtil.convertObjToUFDouble(result[1])).add(
        cUtil.convertObjToUFDouble(result[2]));
    newresult[1] = cUtil.convertObjToUFDouble(result[3]).add(cUtil.convertObjToUFDouble(result[4]));
    newresult[2] = cUtil.convertObjToUFDouble(result[7]).sub(cUtil.convertObjToUFDouble(result[8]));
    return newresult;
  }

  private static CreditVO convertToCreditVO(CuCreditVO vo) {
    CreditVO credit = new CreditVO();
    credit.m_pk_corp = vo.getPk_corp();
    credit.m_climittypecid = vo.getClimittypecid();
    credit.m_pk_cumandoc = vo.getPk_cumandoc();
    return credit;
  }

  /**
   * ����ȷ���Ŀͻ�������ü�¼�����ض�Ӧ��Ӧ������ 
   * 
   * �������ڣ�(2004-3-12 9:45:28)
   * 
   * @return nc.vo.pub.lang.UFDouble[]
   * @param voCredit
   *            nc.vo.so.so120.CreditVO
   */
  private UFDouble[] getCreditAccountMny(Connection con, CreditVO voCredit, String sTmpTab, String[] climittypecids,
      boolean binclude) throws SQLException {
    if (voCredit != null && voCredit.m_climittypecid == null && sTmpTab == null) {
      return getCreditAccountMnyNoType(con, voCredit, null);
    }
    StringBuffer sql = new StringBuffer();
    sql.append("select sum(nnotauditaccmny), sum(nnotoutaccmny), sum(noutaccmny), sum(nestimateaccmny), sum(nassuredaccmny), sum(nsubmny) ");
    if (sTmpTab == null) {
      sql.append("\n from so_accountmny acc ");
    } else {
      sql.append("\n from ");
      sql.append(sTmpTab);
      sql.append(" acc ");
    }
    if (voCredit != null) {
      sql.append("\n where pk_corp='");
      sql.append(voCredit.m_pk_corp);
      if (binclude)
        sql.append("' \n and exists( ");
      else
        sql.append("' \n and not exists( ");

      sql.append("\n        select climittypebid ");
      sql.append("\n        from so_limittype_b area ");

      sql.append("\n        where ");
      if (!binclude && climittypecids != null && climittypecids.length > 0) {
        sql.append("  area.climittypecid in ");
        sql.append(GeneralSqlString.formSubSql("area.climittypecid ", climittypecids));
      } else {
        sql.append("  area.climittypecid='");
        sql.append(voCredit.m_climittypecid);
        sql.append("'");
      }
      sql.append("\n        and (area. cproductid ='").append(CreditUtil.NULL_DATA).append(
          "' or area. cproductid =acc.cproductid)");
      sql.append("\n        and (area. cbiztypeid ='").append(CreditUtil.NULL_DATA).append(
          "' or area. cbiztypeid =acc.cbiztypeid)");
      sql.append("\n        and area.dr=0 ");

      sql.append("\n ) and pk_cumandoc='");
      sql.append(voCredit.m_pk_cumandoc);
      sql.append("'");
    }

    UFDouble[] dRE = new UFDouble[6];

    boolean bConnectionSended = (con != null); // true-�����ⲿ����

    PreparedStatement stmt = null;
    try {
      if (!bConnectionSended) {
        con = getConnection();
      }
      stmt = con.prepareStatement(sql.toString());
      ResultSet rs = stmt.executeQuery();
      Object obj = null;

      CreditUtil cUtil = new CreditUtil();

      while (rs.next()) {
        for (int i = 0,len=dRE.length; i < len; i++) {
          obj = rs.getObject(i + 1);
          dRE[i] = cUtil.convertObjToUFDouble(obj);
        }
      }
    } finally {
      try {
        if (stmt != null) {
          stmt.close();
        }
      } catch (Exception e) {
      }
      if (!bConnectionSended) {
        // �ⲿû�д������ӣ��Źر�
        try {
          if (con != null) {
            con.close();
          }
        } catch (Exception e) {
        }
      }
    }
    for (int i = 0,len=dRE.length; i < len; i++) {
      if (dRE[i] == null) {
        dRE[i] = CreditUtil.ZERO;
      }
    }
    return dRE;
  }

  /**
   * @param pk_corp ��˾id ��½��˾��id
   * @param pk_cumandoc ���̹�����id �����Ŀͻ�
   * @param biztypeid   ҵ������id ������ҵ������
   * @param productid   ��Ʒ��id ����������ϸ��ղ�Ʒ�߹������Դ����ֵ�����򴫶�����һ�еĲ�Ʒ��
   * @param loginDate   ��½����
   * @return ���鳤��Ϊ9: 0�������id 1����δִ��Ӧ�� 2������ִ��Ӧ�� 3ҵ��Ӧ�� 4���Ӧ��δ�ϲ���Ʊ��� 5�տ�δ������� 6���ö�� 7����ռ��ֵ 8�������
   * @return ���鳤��Ϊ12:   0�������id 
   *            1����δ���Ӧ�� 2����δ����Ӧ�� 3�����ѳ���Ӧ�� 4δȷ��ҵ��Ӧ�� 5��ȷ��ҵ��Ӧ�� 
   *            6���Ӧ��δ�ϲ���Ʊ��� 7�տ�δ������� 8�˻�δ������ 
   *            9���ö�� 10����ռ��ֵ 11�������
   * @throws Exception
   */
//  public CreditResultVO getCreditMnyForOrder(String pk_corp, String pk_cumandoc, String biztypeid, String productid,
//      UFDate loginDate) throws Exception {
//    if (pk_cumandoc == null ||  pk_corp == null || loginDate == null)
//      throw new IllegalArgumentException("����Ĳ����д���˾���ͻ�����½����Ϊ�գ����� ");
//    MatchCredit match = new MatchCredit();
//    DataManager dm = getDataManager();
//    String pk_cubasdoc = dm.getCubasdocIDFromMan(pk_cumandoc);
//    boolean bNeedContainSub = false;
//    String[] subCubaspk = new String[0];
//    HashMap[] hm = dm.getAllCusInBas(pk_corp);
//    CreditUtilBS util = new CreditUtilBS();
//      String newpk = util.change2TopCusByFlag(
//          pk_cubasdoc, hm[0], true);
//      if (!newpk.equals(pk_cubasdoc)) {
//        pk_cumandoc = ((Object[]) hm[0].get(newpk))[2].toString();
//        pk_cubasdoc = newpk;
//        bNeedContainSub = true;
//        ArrayList<String> al = new CreditUtilBS().getAllLeaf(pk_cubasdoc, hm[1], null, true, false);
//        al.add(pk_cubasdoc);
//        subCubaspk = al.toArray(new String[0]);
//      }
//    LimitTypeCVO voLimitType = match.getMatchLimitType(pk_corp, productid, biztypeid);
//    if (voLimitType == null)
//      throw new BusiBeanException("û��ƥ�䵽������ͣ�������ʾ�������ݡ�");
//    String sWhere = " pk_cubasdoc = '" + pk_cubasdoc + "' and climittypeid = '"
//        + voLimitType.m_climittypeid + "' and ((dfromdate <= '" + loginDate.toString() + "' and dtodate >='"
//        + loginDate.toString() + "' and ( (pk_corp = '" + pk_corp + "' and vcredittypename in ('2','3'))) or (pk_corp = '0001' and vcredittypename = '1')) or (pk_corp = '" + pk_corp + "' and vcredittypename = '4'))";
//    SmartDMO dmo = new SmartDMO();
//    // ƥ�����ö��
//    CuCreditVO[] vos = (CuCreditVO[]) dmo.selectBy(CuCreditVO.class, null, sWhere);
//    vos = new CreditUtil().prepareVO(vos, pk_corp);
//    if (vos!=null) {
//      for (int i = 0; i < vos.length; i++) {
//        //      ������䵽�ĵ�һ����������Ǽ��Ŷ��,pk_cumandoc�Լ�CLIMITTYPECID��Ч,��Ҫ���²�ѯ
//        //pk_cubasdoc��pk_cumandoc���,ת��Ϊ��˾pk
//        if (vos[i].getPk_cubasdoc()!=null&&(vos[i].getPk_cumandoc()==null||vos[i].getPk_cubasdoc().equals(vos[i].getPk_cumandoc()))) {
//          vos[i].setPk_cumandoc(getDataManager()
//              .getCumandocIDFrombas(vos[i].getPk_cubasdoc(),
//                  pk_corp));
//          vos[i].setClimittypecid(getDataManager().getClimittypecid(
//              vos[i].getClimittypeid(), pk_corp));
//        }
//      }
//    }
//    Object[] result = new Object[12];
//    result[0] = voLimitType.m_climittypeid;
//
//    CreditUtil cUtil = new CreditUtil();
//
//    // ƥ����
//    if (vos != null && vos.length > 0) {
//      CreditVO[] cvos = new CreditVO[1];
//      cvos[0] = CreditImpl.getCreditVO(vos[0]);
//      CreditImpl impl = new CreditImpl();
//      Object[] os = impl.getCreditAccountMny(cvos, pk_corp, true, bNeedContainSub, subCubaspk);
//      impl.getCreditMny(cvos, os, vos);
//      ArrayList al = (ArrayList)vos[0].getAttributeValue("allcreditoccupy");
//      UFDouble[] ysInfo = (UFDouble[])al.get(0);
//      for (int i = 1; i <= 6; i++) {
//        result[i] = cUtil.convertObjToUFDouble(ysInfo[i - 1]);
//      }
//      result[7] = vos[0].getAttributeValue("ncorpskmny");
//      result[8] = new DataManager().getSOReturnMny(pk_cumandoc, null, vos[0]);
//      if(voLimitType.m_bcontainflag.toString().equals("Y")) {//�ж��Ƿ�������ŷ����� liuzwc 2009-02-12
//        result[9] = cUtil.convertObjToUFDouble(vos[0].getAttributeValue("nlimitmny")).add(
//            cUtil.convertObjToUFDouble(vos[0].getAttributeValue("ngrouplimitmny")));
//      } else {
//        result[9] = cUtil.convertObjToUFDouble(vos[0].getAttributeValue("nlimitmny"));
//      }
//      
//      result[10] = cUtil.convertObjToUFDouble(vos[0].getAttributeValue("creditoccupy"));
//      result[11] = cUtil.convertObjToUFDouble(result[9]).sub(cUtil.convertObjToUFDouble(result[10]));
//    } else {
//      DataManager data = new DataManager();
//      UFBoolean bExamNoPatch = data.getCC03();
//      UFBoolean CC07 = data.getCC07();
//      CreditVO m_voCredit = match.getNoMatchCredit(voLimitType, data.getCubasdocIDFromMan(pk_cumandoc),
//          pk_cumandoc);
//      UFDouble[] dAR = getCreditAccountMny(null, m_voCredit, null);
//      for (int i = 1; i <= 6; i++) {
//        result[i] = cUtil.convertObjToUFDouble(dAR[i - 1]);
//      }
//      result[7] = cUtil.convertObjToUFDouble(data.getSKMnyForCorp(m_voCredit.m_pk_corp, m_voCredit.m_pk_cubasdoc,
//          m_voCredit.m_climittypecid));
//
//      if (!bExamNoPatch.booleanValue()) {
//        for (int i = 8; i < result.length; i++) {
//          result[i] = null;
//        }
//      } else {
//        result[8] = new DataManager().getSOReturnMnyForCorp(pk_cumandoc, m_voCredit);
//        result[9] = CreditUtil.ZERO;
//        result[10] = cUtil.getCreditMny("0", dAR,CC07).sub(cUtil.convertObjToUFDouble(result[7])).sub(cUtil.convertObjToUFDouble(result[8]));
//        result[11] = cUtil.convertObjToUFDouble(result[9]).sub(cUtil.convertObjToUFDouble(result[10]));
//      }
//    }
//    
//    CreditResultVO vo = new CreditResultVO();
//    vo.climittypeid=result[0].toString();
//    vo.nnotauditaccmny=(UFDouble)result[1];
//    vo.nnotoutaccmny=(UFDouble)result[2];
//    vo.noutaccmny=(UFDouble)result[3];
//    vo.nestimateaccmny=(UFDouble)result[4];
//    vo.nassuredaccmny=(UFDouble)result[5];
//    vo.nsubmny=(UFDouble)result[6];
//    vo.nskmny=(UFDouble)result[7];
//    vo.nrtappnooutmny=(UFDouble)result[8];
//    vo.ncreditmny=(UFDouble)result[9];
//    vo.ncreditinuse=(UFDouble)result[10];
//    vo.ncredityetmny=(UFDouble)result[11];
//
//    return vo;
//
//  }
  
  /**
   * ��ù�˾�ͼ��ŵ����Ӧ�ա����Ӧ��δ�ϲ���Ʊ���տ�δ�������˻�����δ�����
   * �Լ���˾���ö�ȡ�����ռ�á�Ӧ�����������ö�ȡ���������ռ�á�����������
   * ����CreditResultVO�����۶���-������ѯ-�ͻ�����
   * @author liuzwc
   * @param pk_corp ��½��˾
   * @param pk_cumandoc ���̹�����id �����Ŀͻ�
   * @param biztypeid   ҵ������id ������ҵ������
   * @param productid   ��Ʒ��id ����������ϸ��ղ�Ʒ�߹������Դ����ֵ�����򴫶�����һ�еĲ�Ʒ��
   * @param loginDate   ��½����
   * @return CreditResultVO
   * @throws Exception
   */
  public CreditResultVO getCreditMnyForOrder(String pk_corp, String pk_cumandoc, String biztypeid, String productid,
      UFDate loginDate) throws Exception {
    if (pk_cumandoc == null ||  pk_corp == null || loginDate == null)
      throw new IllegalArgumentException(nc.bs.ml.NCLangResOnserver.getInstance().getStrByID(
          "SCMSOCredit","UPPSCMSOCredit-000288")/*@res
          * ����Ĳ����д���˾���ͻ�����½����Ϊ��,����*/);
    MatchCredit match = new MatchCredit();
    DataManager dm = getDataManager();
    UFBoolean CC07 = dm.getCC07();
    UFBoolean CC03 = dm.getCC03();
    CreditResultVO resultVO = null;
    
    String pk_cubasdoc = dm.getCubasdocIDFromMan(pk_cumandoc);
//    boolean bNeedContainSub = false;
//    String[] subCubaspk = new String[0];
    HashMap[] hm = dm.getAllCusInBas(pk_corp, pk_cubasdoc);
    CreditUtilBS util = new CreditUtilBS();
    String newpk = util.change2TopCusByFlag(pk_cubasdoc, hm[0], true);
    if (!newpk.equals(pk_cubasdoc)) {
      pk_cumandoc = ((Object[]) hm[0].get(newpk))[2].toString();
      pk_cubasdoc = newpk;
      // bNeedContainSub = true;
      ArrayList<String> al = new CreditUtilBS().getAllLeaf(pk_cubasdoc, hm[1], null, true, false);
      al.add(pk_cubasdoc);
      // subCubaspk = al.toArray(new String[0]);
    }
    LimitTypeCVO voLimitType = match.getMatchLimitType(pk_corp, productid, biztypeid);
    if (voLimitType == null)
      throw new BusiBeanException(nc.bs.ml.NCLangResOnserver.getInstance().getStrByID(
          "SCMSOCredit","UPPSCMSOCredit-000287")/*@res
          * û��ƥ�䵽������ͣ�������ʾ�������ݡ�*/);
//    String sWhere = " pk_cubasdoc = '" + pk_cubasdoc + "' and climittypeid = '"
//        + voLimitType.m_climittypeid +" '";
    //��ӣ��������ڹ��˿��ö��  liuzwc 2010-01-07  
    String sWhere = " pk_cubasdoc = '" + pk_cubasdoc + "' and climittypeid = '"
      + voLimitType.m_climittypeid + "' and ((dfromdate <= '" + loginDate.toString() + "' and dtodate >='"
      + loginDate.toString() + "' and ( (pk_corp = '" + pk_corp + "' and vcredittypename in ('2','3'))) or (pk_corp = '0001' and vcredittypename = '1')) or (pk_corp = '" + pk_corp + "' and vcredittypename = '4'))";
    SmartDMO dmo = new SmartDMO();
    // ƥ�����ö��
    CuCreditVO[] vos = (CuCreditVO[]) dmo.selectBy(CuCreditVO.class, null, sWhere);
    HashMap<String, CuCreditVO> hmCreditVOs = new CreditUtil().prepareVOForOrder(vos, pk_corp);
    
    //--�޶����ʾ����ռ��  ��ҫ�ϣ�zuodm��liuzwc; ���䣺��ռ�ú���ȴ�޶��  liuzwc��zuodm 2010-01-14
    CuCreditVO vo = hmCreditVOs.get(pk_corp);
    if((vo == null && CC03.booleanValue()) || (vo != null && vo.getNlimitmny() == null)){
      vo = match.getNoMatchCredit_CuCreditVO(voLimitType, pk_cubasdoc, pk_cumandoc);
      hmCreditVOs.put(pk_corp, vo);
    }
    //------
    
    Object totalmny = null;
    boolean bIfGroup = true;//�Ƿ�Ҫ��ѯ���ŵ�
    if(hmCreditVOs.get("0001") != null)
      totalmny = hmCreditVOs.get("0001").getAttributeValue("ngtotallimitmny");
    if(totalmny == null) {
      bIfGroup = false;
    }
    
    /*��ѯ���Ӧ�ա����Ӧ��δ�ϲ���Ʊ���տ�δ�������˻�����δ������
     * ���ͻ����ò�ѯռ����ϸ�ķ���,����ÿһ��Ϊ��˾�²�ͬҵ�����̵����ݣ�*/
    ThreeYsInfo[] ysInfo = dm.getThreeYsInfoDetail(
        (String) voLimitType.m_climittypeid,pk_cubasdoc,pk_corp, bIfGroup,false);
    HashMap<String,CreditResultVO> hmYsInfo = getSumBycorp(ysInfo,pk_corp);

    //ȡ�ñ���˾vo�㹫˾����ռ��
    String bcontainflag = voLimitType.m_bcontainflag.toString();
    resultVO = getCorpCreditMny(hmYsInfo,hmCreditVOs,pk_corp,CC07,CC03,bcontainflag);
    
    //ȡ�ü���������˾vo���ֱ����ռ�ú�����������ռ�á�
    //������ÿ����˾ֵ����
    getGroupCreditMny(resultVO,hmYsInfo,hmCreditVOs,pk_corp,CC07);
    
    if(resultVO == null)
      return null;
    resultVO.setClimittypeid(voLimitType.m_climittypeid);
    return resultVO;

  }
  
  /**
   * �����ص�CreditResultVO���������Ӧ�ա����Ӧ��δ�ϲ���Ʊ���տ�δ�������˻�����δ�����ֵ
   * �����㼯�ŵ�����ռ��
   * @author liuzwc
   * @param resultVO ���VO 
   * @param hmYsInfo <corp,CreditResultVO>
   * @param hmCreditVOs ���ֵ<corp,CuCreditVO>
   * @param pk_corp ��ǰ��˾
   * @param cc07 ������
   * @throws Exception
   */
  private void getGroupCreditMny(CreditResultVO resultVO, HashMap<String, CreditResultVO> hmYsInfo
      ,HashMap<String, CuCreditVO> hmCreditVOs, String pk_corp, UFBoolean cc07) throws BusinessException {
  
    if(resultVO != null) {  
      CuCreditVO groupVO = hmCreditVOs.get("0001");
      if(groupVO == null)
        return;
      if(groupVO.getAttributeValue("ngtotallimitmny")!= null) {//û�м����ܿض���򲻼��㼯��Ӧ�գ�����������ҳǩ����ʾ����
        UFDouble ufd = new UFDouble(0.0);
        CuCreditVO creditVO = null;
        CreditResultVO tempVO = null;
        UFDouble[] dAR = new UFDouble[]{ufd,ufd,ufd,ufd,ufd,ufd,ufd,ufd,ufd};
        
        String[] keys = hmYsInfo.keySet().toArray(new String[0]);
        if(keys == null || keys.length == 0)
          return;
        //�ۼ�ÿһ������
        for(String key: keys) {
          tempVO = hmYsInfo.get(key);
          if(pk_corp.equals(key)) {//������˾YsInfo�Ӻ͵�����
            dAR[0] = dAR[0].add(tempVO.nnotauditaccmny);
            dAR[1] = dAR[1].add(tempVO.nnotoutaccmny);
            dAR[2] = dAR[2].add(tempVO.noutaccmny);
            dAR[3] = dAR[3].add(tempVO.nestimateaccmny);
            dAR[4] = dAR[4].add(tempVO.nassuredaccmny);
            dAR[5] = dAR[5].add(tempVO.nsubmny);
            dAR[6] = dAR[6].add(tempVO.nskmny);
            dAR[7] = dAR[7].add(tempVO.nrtappnooutmny);
            //����ռ��ֵ
            creditVO = hmCreditVOs.get(key);
            dAR[8] = dAR[8].add(getCreditMny(creditVO==null?"0":creditVO.getVoccupiedfun(),tempVO,cc07)
                .sub(tempVO.nskmny).sub(tempVO.nrtappnooutmny));
          } else {//����������˾�Ӻ͵�����
            dAR[0] = dAR[0].add(tempVO.ngroupnotauditaccmny);
            dAR[1] = dAR[1].add(tempVO.ngroupnotoutaccmny);
            dAR[2] = dAR[2].add(tempVO.ngroupoutaccmny);
            dAR[3] = dAR[3].add(tempVO.ngroupestimateaccmny);
            dAR[4] = dAR[4].add(tempVO.ngroupassuredaccmny);
            dAR[5] = dAR[5].add(tempVO.ngroupsubmny);
            dAR[6] = dAR[6].add(tempVO.ngroupskmny);
            dAR[7] = dAR[7].add(tempVO.ngroupreturnnotinaccmny);
            //����ռ��ֵ
            creditVO = hmCreditVOs.get(key);
            dAR[8] = dAR[8].add(getCreditMnyGroup(creditVO==null?"0":creditVO.getVoccupiedfun(),tempVO,cc07)
                .sub(tempVO.ngroupskmny).sub(tempVO.ngroupreturnnotinaccmny));
          }
        }
        //�ܿض��
        resultVO.setNgroupcreditmny((UFDouble)hmCreditVOs.get("0001").getAttributeValue("ngtotallimitmny"));
        resultVO.setNgroupnotauditaccmny(dAR[0]);
        resultVO.setNgroupnotoutaccmny(dAR[1]);
        resultVO.setNgroupoutaccmny(dAR[2]);
        resultVO.setNgroupestimateaccmny(dAR[3]);
        resultVO.setNgroupassuredaccmny(dAR[4]);
        resultVO.setNgroupsubmny(dAR[5]);
        resultVO.setNgroupskmny(dAR[6]);
        resultVO.setNgroupreturnnotinaccmny(dAR[7]);
        resultVO.setNgroupcreditinuse(dAR[8]);
        //�������
        resultVO.setNgroupcredityetmny(resultVO.ngroupcreditmny.sub(resultVO.ngroupcreditinuse));
        
        String groupCur = SysInitBO_Client.getParaString("0001", "BD211");
        resultVO.setCgroupcurrucyname(groupCur);//���ű�λ��
      }
    }
  }

  /**
   * ������CreditResultVO��ǰ��˾���Ӧ�ա����Ӧ��δ�ϲ���Ʊ���տ�δ�������˻�����δ�����ֵ
   * �����㵱ǰ��˾������ռ��
   * @author liuzwc
   * @param hmYsInfo 
   * @param hmCreditVOs
   * @param pk_corp ��ǰ��˾
   * @param CC07 �Ƿ񽫳�Ӧ�յ�δ�ϲ���Ʊ�����Ϊ���õ������
   * @param bcontainflag �Ƿ�������Ŷ��
   * @return CreditResultVO ���VO
   * @throws NamingException 
   * @throws SystemException 
   * @throws Exception
   */
  private CreditResultVO getCorpCreditMny(HashMap<String, CreditResultVO> hmYsInfo
      , HashMap<String, CuCreditVO> hmCreditVOs,String pk_corp,UFBoolean CC07,UFBoolean CC03,String bcontainflag) throws Exception {
    
    CuCreditVO vo = hmCreditVOs.get(pk_corp);
    CuCreditVO groupVO = hmCreditVOs.get("0001");
    
    //û�ж��幫˾�뼯�Ŷ��
    if(vo == null && groupVO == null) {
      return null;
    }//û�ж��幫˾�뼯�Ŷ�ȣ������˼��ŷ���
    else if(vo == null && groupVO.getAttributeValue("ngtotallimitmny") == null) {
      return null;
    }//û�ж��幫˾��ȣ����弯�Ŷ��
    else if(vo == null){
      return new CreditResultVO();
    }
    CreditResultVO resultVO = hmYsInfo.get(pk_corp);//���Ӧ�գ�������ֵ
    if(resultVO == null)
      return new CreditResultVO();
    //���ö��
    if(bcontainflag.equals("Y"))//�Ƿ�������Ŷ��
      if(groupVO == null){
        resultVO.setNcreditmny(vo.getNlimitmny());
      }else {//���ö��:��˾+���ŷ���
        resultVO.setNcreditmny((vo.getNlimitmny()==null?new UFDouble(0.0):vo.getNlimitmny()).add(
          (UFDouble)(groupVO.getAttributeValue("ngrouplimitmny")==null?new UFDouble(0.0):groupVO.getAttributeValue("ngrouplimitmny"))));
      }
    else
      resultVO.setNcreditmny(vo.getNlimitmny());
    //����ռ��
    resultVO.setNcreditinuse(getCreditMny(vo.getVoccupiedfun(),resultVO,CC07)
        .sub(resultVO.nskmny).sub(resultVO.nrtappnooutmny));
    //�������
    resultVO.setNcredityetmny(resultVO.ncreditmny.sub(resultVO.ncreditinuse));
    return resultVO;
  }
  
  /**
   * ��ù�˾����ռ��
   * 
   * @author liuzwc
   * @param sFormula ռ�ú���
   * @param resultVO ����ǰ̨�Ľ��VO
   * @param CC07 �������Ƿ񽫳�Ӧ�յ�δ�ϲ���Ʊ�����Ϊ���õ������
   * @return UFDouble ռ��ֵ
   */
  public UFDouble getCreditMny(String sFormula, CreditResultVO resultVO, UFBoolean CC07) {
    if (CC07.booleanValue()) {// �Ƿ񽫳�Ӧ�յ�δ�ϲ���Ʊ�����Ϊ���õ������
      if (sFormula==null||sFormula.equals("0")) {
        return (resultVO.nnotauditaccmny).add(resultVO.nnotoutaccmny).add(resultVO.noutaccmny)
        .add(resultVO.nestimateaccmny).add(resultVO.nassuredaccmny).sub(resultVO.nsubmny);
      } else if (sFormula.equals("1")) {
        return (resultVO.nnotoutaccmny).add(resultVO.noutaccmny)
        .add(resultVO.nestimateaccmny).add(resultVO.nassuredaccmny).sub(resultVO.nsubmny);
      } else if (sFormula.equals("2")) {
        return (resultVO.noutaccmny)
        .add(resultVO.nestimateaccmny).add(resultVO.nassuredaccmny).sub(resultVO.nsubmny);
      } else if (sFormula.equals("3")) {
        return (resultVO.nestimateaccmny).add(resultVO.nassuredaccmny).sub(resultVO.nsubmny);
      }
    } else {
      if (sFormula==null||sFormula.equals("0")) {
        return (resultVO.nnotauditaccmny).add(resultVO.nnotoutaccmny).add(resultVO.noutaccmny)
        .add(resultVO.nestimateaccmny).add(resultVO.nassuredaccmny);
      } else if (sFormula.equals("1")) {
        return (resultVO.nnotoutaccmny).add(resultVO.noutaccmny)
        .add(resultVO.nestimateaccmny).add(resultVO.nassuredaccmny);
      } else if (sFormula.equals("2")) {
        return (resultVO.noutaccmny)
        .add(resultVO.nestimateaccmny).add(resultVO.nassuredaccmny);
      } else if (sFormula.equals("3")) {
        return (resultVO.nestimateaccmny).add(resultVO.nassuredaccmny);
      }
    }
    return new UFDouble(0.0);
  }
  /**
   *��ü���������˾����ռ��
   * 
   * @author liuzwc
   * @param sFormula ռ�ú���
   * @param resultVO ����ǰ̨�Ľ��VO
   * @param CC07 �������Ƿ񽫳�Ӧ�յ�δ�ϲ���Ʊ�����Ϊ���õ������
   * @return UFDouble ռ��ֵ
   */
  public UFDouble getCreditMnyGroup(String sFormula, CreditResultVO resultVO, UFBoolean CC07) {
    if (CC07.booleanValue()) {// �Ƿ񽫳�Ӧ�յ�δ�ϲ���Ʊ�����Ϊ���õ������
      if (sFormula==null||sFormula.equals("0")) {
        return (resultVO.ngroupnotauditaccmny).add(resultVO.ngroupnotoutaccmny).add(resultVO.ngroupoutaccmny)
        .add(resultVO.ngroupestimateaccmny).add(resultVO.ngroupassuredaccmny).sub(resultVO.ngroupsubmny);
      } else if (sFormula.equals("1")) {
        return (resultVO.ngroupnotoutaccmny).add(resultVO.ngroupoutaccmny)
        .add(resultVO.ngroupestimateaccmny).add(resultVO.ngroupassuredaccmny).sub(resultVO.ngroupsubmny);
      } else if (sFormula.equals("2")) {
        return (resultVO.ngroupoutaccmny)
        .add(resultVO.ngroupestimateaccmny).add(resultVO.ngroupassuredaccmny).sub(resultVO.ngroupsubmny);
      } else if (sFormula.equals("3")) {
        return (resultVO.ngroupestimateaccmny).add(resultVO.ngroupassuredaccmny).sub(resultVO.ngroupsubmny);
      }
    } else {
      if (sFormula==null||sFormula.equals("0")) {
        return (resultVO.ngroupnotauditaccmny).add(resultVO.ngroupnotoutaccmny).add(resultVO.ngroupoutaccmny)
        .add(resultVO.ngroupestimateaccmny).add(resultVO.ngroupassuredaccmny);
      } else if (sFormula.equals("1")) {
        return (resultVO.ngroupnotoutaccmny).add(resultVO.ngroupoutaccmny)
        .add(resultVO.ngroupestimateaccmny).add(resultVO.ngroupassuredaccmny);
      } else if (sFormula.equals("2")) {
        return (resultVO.ngroupoutaccmny)
        .add(resultVO.ngroupestimateaccmny).add(resultVO.ngroupassuredaccmny);
      } else if (sFormula.equals("3")) {
        return (resultVO.ngroupestimateaccmny).add(resultVO.ngroupassuredaccmny);
      }
    }
    return new UFDouble(0.0);
  }



  /**
   * ���չ�˾�Խ�����YsInfo���з���Ӻ�
   * @author liuzwc
   * @param ysInfo
   * @param pk_corp ��½��˾
   * @return HashMap<String,CreditResultVO> ��˾=CreditResultVO
   * @throws Exception
   */
  private HashMap<String,CreditResultVO> getSumBycorp(ThreeYsInfo[] ysInfo,String pk_corp) {
    HashMap<String,CreditResultVO> map = new HashMap<String,CreditResultVO>();
    if(ysInfo == null || ysInfo.length == 0)
      return map;
    CreditResultVO vo = null;
    for(int i= 0 ;i<ysInfo.length; i++){
      String corp = ysInfo[i].getAttributeValue("pk_corp").toString();
      if(pk_corp.equals(corp)){//��ǰ��˾
        if(map.containsKey(corp)) {
          vo = map.get(corp);
          vo.nnotauditaccmny = vo.nnotauditaccmny.add((UFDouble)ysInfo[i].getAttributeValue("nnotauditaccmny"));
          vo.nnotoutaccmny = vo.nnotoutaccmny.add((UFDouble)ysInfo[i].getAttributeValue("nnotoutaccmny"));
          vo.noutaccmny = vo.noutaccmny.add((UFDouble)ysInfo[i].getAttributeValue("noutaccmny"));
          vo.nestimateaccmny = vo.nestimateaccmny.add((UFDouble)ysInfo[i].getAttributeValue("nestimateaccmny"));
          vo.nassuredaccmny = vo.nassuredaccmny.add((UFDouble)ysInfo[i].getAttributeValue("nassuredaccmny"));
          vo.nsubmny = vo.nsubmny.add((UFDouble)ysInfo[i].getAttributeValue("nsubmny"));
          vo.nskmny = vo.nskmny.add((UFDouble)ysInfo[i].getAttributeValue("nskmny"));
          vo.nrtappnooutmny = vo.nrtappnooutmny.add((UFDouble)ysInfo[i].getAttributeValue("nreturnnotinaccmny"));
        } else {
          CreditResultVO newVO = new CreditResultVO();
          newVO.nnotauditaccmny=(UFDouble)ysInfo[i].getAttributeValue("nnotauditaccmny");
          newVO.nnotoutaccmny=(UFDouble)ysInfo[i].getAttributeValue("nnotoutaccmny");
          newVO.noutaccmny=(UFDouble)ysInfo[i].getAttributeValue("noutaccmny");
          newVO.nestimateaccmny=(UFDouble)ysInfo[i].getAttributeValue("nestimateaccmny");
          newVO.nassuredaccmny=(UFDouble)ysInfo[i].getAttributeValue("nassuredaccmny");
          newVO.nsubmny=(UFDouble)ysInfo[i].getAttributeValue("nsubmny");
          newVO.nskmny=(UFDouble)ysInfo[i].getAttributeValue("nskmny");
          newVO.nrtappnooutmny=(UFDouble)ysInfo[i].getAttributeValue("nreturnnotinaccmny");
          map.put(corp, newVO);
        }
      } else {//����������˾
        if(map.containsKey(corp)) {
          vo = map.get(corp);
          vo.ngroupnotauditaccmny = vo.ngroupnotauditaccmny.add((UFDouble)ysInfo[i].getAttributeValue("ngroupnotauditaccmny"));
          vo.ngroupnotoutaccmny = vo.ngroupnotoutaccmny.add((UFDouble)ysInfo[i].getAttributeValue("ngroupnotoutaccmny"));
          vo.ngroupoutaccmny = vo.ngroupoutaccmny.add((UFDouble)ysInfo[i].getAttributeValue("ngroupoutaccmny"));
          vo.ngroupestimateaccmny = vo.ngroupestimateaccmny.add((UFDouble)ysInfo[i].getAttributeValue("ngroupestimateaccmny"));
          vo.ngroupassuredaccmny = vo.ngroupassuredaccmny.add((UFDouble)ysInfo[i].getAttributeValue("ngroupassuredaccmny"));
          vo.ngroupsubmny = vo.ngroupsubmny.add((UFDouble)ysInfo[i].getAttributeValue("ngroupsubmny"));
          vo.ngroupskmny = vo.ngroupskmny.add((UFDouble)ysInfo[i].getAttributeValue("ngroupskmny"));
          vo.ngroupreturnnotinaccmny = vo.ngroupreturnnotinaccmny.add((UFDouble)ysInfo[i].getAttributeValue("ngroupreturnnotinaccmny"));
        } else {
          CreditResultVO newVO = new CreditResultVO();
          newVO.ngroupnotauditaccmny=(UFDouble)ysInfo[i].getAttributeValue("ngroupnotauditaccmny");
          newVO.ngroupnotoutaccmny=(UFDouble)ysInfo[i].getAttributeValue("ngroupnotoutaccmny");
          newVO.ngroupoutaccmny=(UFDouble)ysInfo[i].getAttributeValue("ngroupoutaccmny");
          newVO.ngroupestimateaccmny=(UFDouble)ysInfo[i].getAttributeValue("ngroupestimateaccmny");
          newVO.ngroupassuredaccmny=(UFDouble)ysInfo[i].getAttributeValue("ngroupassuredaccmny");
          newVO.ngroupsubmny=(UFDouble)ysInfo[i].getAttributeValue("ngroupsubmny");
          newVO.ngroupskmny=(UFDouble)ysInfo[i].getAttributeValue("ngroupskmny");
          newVO.ngroupreturnnotinaccmny=(UFDouble)ysInfo[i].getAttributeValue("ngroupreturnnotinaccmny");
          map.put(corp, newVO);
        }
      }
      
    }
    return map;
  }
  
  /**
   * Ϊ�ŵ�ϵͳ�ṩ�ӿ�
   * @author jianghp  Date:2009-2-16
   * 
   * @param pk_corp
   * @param pk_cumandoc
   * @param biztypeid
   * @param productid
   * @param loginDate
   * @return
   * @throws Exception
   */
  public CreditResultVO getCreditMnyForMD(String pk_corp, String pk_cumandoc, String biztypeid, String productid,
      UFDate loginDate) throws Exception {
    if (pk_cumandoc == null ||  pk_corp == null || loginDate == null)
      throw new IllegalArgumentException(nc.bs.ml.NCLangResOnserver.getInstance().getStrByID(
          "SCMSOCredit","UPPSCMSOCredit-000288")/*@res
          * ����Ĳ����д���˾���ͻ�����½����Ϊ��,����*/);
    MatchCredit match = new MatchCredit();
    DataManager dm = getDataManager();
    String pk_cubasdoc = dm.getCubasdocIDFromMan(pk_cumandoc);
    boolean bNeedContainSub = false;
    String[] subCubaspk = new String[0];
    HashMap[] hm = dm.getAllCusInBas(pk_corp, pk_cubasdoc);
    CreditUtilBS util = new CreditUtilBS();
    String newpk = util.change2TopCusByFlag(pk_cubasdoc, hm[0], true);
    if (!newpk.equals(pk_cubasdoc)) {
      pk_cumandoc = ((Object[]) hm[0].get(newpk))[2].toString();
      pk_cubasdoc = newpk;
      bNeedContainSub = true;
      ArrayList<String> al = new CreditUtilBS().getAllLeaf(pk_cubasdoc, hm[1], null, true, false);
      al.add(pk_cubasdoc);
      subCubaspk = al.toArray(new String[0]);
    }
    LimitTypeCVO voLimitType = match.getMatchLimitType(pk_corp, productid, biztypeid);
    if (voLimitType == null)
      throw new BusiBeanException(nc.bs.ml.NCLangResOnserver.getInstance().getStrByID(
          "SCMSOCredit","UPPSCMSOCredit-000287")/*@res
          * û��ƥ�䵽������ͣ�������ʾ�������ݡ�*/);
    String sWhere = " pk_cubasdoc = '" + pk_cubasdoc + "' and climittypeid = '"
        + voLimitType.m_climittypeid + "' and ((dfromdate <= '" + loginDate.toString() + "' and dtodate >='"
        + loginDate.toString() + "' and ( (pk_corp = '" + pk_corp + "' and vcredittypename in ('2','3'))) or (pk_corp = '0001' and vcredittypename = '1')) or (pk_corp = '" + pk_corp + "' and vcredittypename = '4'))";
    SmartDMO dmo = new SmartDMO();
    // ƥ�����ö��
    CuCreditVO[] vos = (CuCreditVO[]) dmo.selectBy(CuCreditVO.class, null, sWhere);
    vos = new CreditUtil().prepareVO(vos, pk_corp);
    if (vos!=null) {
      for (int i = 0; i < vos.length; i++) {
        //      ������䵽�ĵ�һ����������Ǽ��Ŷ��,pk_cumandoc�Լ�CLIMITTYPECID��Ч,��Ҫ���²�ѯ
        //pk_cubasdoc��pk_cumandoc���,ת��Ϊ��˾pk
        if (vos[i].getPk_cubasdoc()!=null&&(vos[i].getPk_cumandoc()==null||vos[i].getPk_cubasdoc().equals(vos[i].getPk_cumandoc()))) {
          vos[i].setPk_cumandoc(getDataManager()
              .getCumandocIDFrombas(vos[i].getPk_cubasdoc(),
                  pk_corp));
          vos[i].setClimittypecid(getDataManager().getClimittypecid(
              vos[i].getClimittypeid(), pk_corp));
        }
      }
    }
    Object[] result = new Object[12];
    result[0] = voLimitType.m_climittypeid;

    CreditUtil cUtil = new CreditUtil();

    // ƥ����
    if (vos != null && vos.length > 0) {
      CreditVO[] cvos = new CreditVO[1];
      cvos[0] = CreditImpl.getCreditVO(vos[0]);
      CreditImpl impl = new CreditImpl();
      Object[] os = impl.getCreditAccountMny(cvos, pk_corp, true, bNeedContainSub, subCubaspk,null);
      impl.getCreditMny(cvos, os, vos,null);
      ArrayList al = (ArrayList)vos[0].getAttributeValue("allcreditoccupy");
      UFDouble[] ysInfo = (UFDouble[])al.get(0);
      for (int i = 1; i <= 6; i++) {
        result[i] = cUtil.convertObjToUFDouble(ysInfo[i - 1]);
      }
      result[7] = vos[0].getAttributeValue("ncorpskmny");
      result[8] = new DataManager().getSOReturnMny(pk_cumandoc, null, vos[0]);
      if(voLimitType.m_bcontainflag.equals("Y")) {//�ж��Ƿ�������ŷ����� liuzwc 2009-02-12
        result[9] = cUtil.convertObjToUFDouble(vos[0].getAttributeValue("nlimitmny")).add(
            cUtil.convertObjToUFDouble(vos[0].getAttributeValue("ngrouplimitmny")));
      } else {
        result[9] = cUtil.convertObjToUFDouble(vos[0].getAttributeValue("nlimitmny"));
      }
      
      result[10] = cUtil.convertObjToUFDouble(vos[0].getAttributeValue("creditoccupy"));
      result[11] = cUtil.convertObjToUFDouble(result[9]).sub(cUtil.convertObjToUFDouble(result[10]));
    } else {
      DataManager data = new DataManager();
      UFBoolean bExamNoPatch = data.getCC03();
      UFBoolean CC07 = data.getCC07();
      CreditVO m_voCredit = match.getNoMatchCredit(voLimitType, data.getCubasdocIDFromMan(pk_cumandoc),
          pk_cumandoc);
      UFDouble[] dAR = getCreditAccountMny(null, m_voCredit, null);
      for (int i = 1; i <= 6; i++) {
        result[i] = cUtil.convertObjToUFDouble(dAR[i - 1]);
      }
      result[7] = cUtil.convertObjToUFDouble(data.getSKMnyForCorp(m_voCredit.m_pk_corp, m_voCredit.m_pk_cubasdoc,
          m_voCredit.m_climittypecid,null));

      if (!bExamNoPatch.booleanValue()) {
        for (int i = 8; i < result.length; i++) {
          result[i] = null;
        }
      } else {
        result[8] = new DataManager().getSOReturnMny(pk_cumandoc, null, null);
        result[9] = CreditUtil.ZERO;
        result[10] = cUtil.getCreditMny("0", dAR,CC07);
        result[11] = cUtil.convertObjToUFDouble(result[9]).sub(cUtil.convertObjToUFDouble(result[10]));
      }
    }
    
    CreditResultVO vo = new CreditResultVO();
    vo.climittypeid=result[0].toString();
    vo.nnotauditaccmny=(UFDouble)result[1];
    vo.nnotoutaccmny=(UFDouble)result[2];
    vo.noutaccmny=(UFDouble)result[3];
    vo.nestimateaccmny=(UFDouble)result[4];
    vo.nassuredaccmny=(UFDouble)result[5];
    vo.nsubmny=(UFDouble)result[6];
    vo.nskmny=(UFDouble)result[7];
    vo.nrtappnooutmny=(UFDouble)result[8];
    vo.ncreditmny=(UFDouble)result[9];
    vo.ncreditinuse=(UFDouble)result[10];
    vo.ncredityetmny=(UFDouble)result[11];

    return vo;

  }
  DataManager DATA_MANAGER = null;
  private DataManager getDataManager() throws NamingException, nc.bs.pub.SystemException{
      DATA_MANAGER = new DataManager();
    return DATA_MANAGER;
  }
  
  
}
