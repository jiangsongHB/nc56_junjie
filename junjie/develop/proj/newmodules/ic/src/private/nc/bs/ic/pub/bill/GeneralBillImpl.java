/*
 * �������� 2005-12-19 TODO Ҫ���Ĵ����ɵ��ļ���ģ�壬��ת�� ���� �� ��ѡ�� �� Java �� ������ʽ �� ����ģ��
 */
package nc.bs.ic.pub.bill;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.naming.NamingException;

import nc.bs.dao.BaseDAO;
import nc.bs.framework.common.NCLocator;
import nc.bs.ic.pub.GenMethod;
import nc.bs.ic.pub.ICCommonBusiImpl;
import nc.bs.ic.pub.ICDbVisitor;
import nc.bs.ic.pub.tools.BillDataFillingUtil;
import nc.bs.ic.pub.vmi.ICSmartToolsDmo;
import nc.bs.logging.Logger;
import nc.bs.ml.NCLangResOnserver;
import nc.bs.pu.pr.PraybillDMO;
import nc.bs.pub.SystemException;
import nc.bs.scm.pub.smart.SmartDMO;
import nc.itf.ic.pub.IGeneralBill;
import nc.itf.uap.bd.storvscost.IStorVSCostQry;
import nc.itf.uap.querytemplate.IQueryTemplate;
import nc.ui.bd.ref.AbstractRefModel;
import nc.ui.bd.ref.RefPubUtil;
import nc.vo.bd.b06.PsndocVO;
import nc.vo.bd.datapower.StringUtil;
import nc.vo.ic.ic001.BatchcodeVO;
import nc.vo.ic.ic700.ICDataSet;
import nc.vo.ic.ic700.WastageBillBVO;
import nc.vo.ic.ic700.WastageBillVO;
import nc.vo.ic.pub.BillTypeConst;
import nc.vo.ic.pub.SmartVOUtilExt;
import nc.vo.ic.pub.bill.GeneralBillItemVO;
import nc.vo.ic.pub.bill.GeneralBillVO;
import nc.vo.ic.pub.bill.IICParaConst;
import nc.vo.ic.pub.bill.IItemKey;
import nc.vo.ic.pub.bill.QryConditionVO;
import nc.vo.ic.pub.bill.QryInfoConst;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pub.query.ConditionVO;
import nc.vo.pub.query.QueryConditionVO;
import nc.vo.pub.query.QueryTempletTotalVO;
import nc.vo.pub.template.ITemplateStyle;
import nc.vo.scm.ic.bill.WhVO;
import nc.vo.scm.pu.VariableConst;
import nc.vo.scm.pub.SCMEnv;
import nc.vo.scm.pub.smart.SmartFieldMeta;
import nc.vo.scm.pub.smart.SmartVO;
import nc.vo.sm.nodepower.OrgnizeTypeVO;
import nc.vo.uap.pf.TemplateParaVO;

/**
 * @author zhanghaiyan
 */
public class GeneralBillImpl implements IGeneralBill {

  /**
   * 
   */
  public GeneralBillImpl() {
    super();
  }

  /**
   * ���ܣ� �����ˣ�zhanghaiyan �������ڣ�2005-12-20 ��ע��
   * 
   * @param sBillType
   * @return
   */
  public ArrayList queryBills(String sBillType, QryConditionVO voQC)
      throws BusinessException {
    ArrayList al = null;
    try {
      if (sBillType.equals(BillTypeConst.m_initIn)) {// �ڳ����
        nc.bs.ic.ic101.GeneralHBO bo = new nc.bs.ic.ic101.GeneralHBO();
        al = bo.queryBills(voQC);
      }
      else if (sBillType.equals(BillTypeConst.m_initBorrow)) {// �ڳ�����
        nc.bs.ic.ic102.GeneralHBO bo = new nc.bs.ic.ic102.GeneralHBO();
        al = bo.queryBills(voQC);
      }
      else if (sBillType.equals(BillTypeConst.m_initLend)) {// �ڳ����
        nc.bs.ic.ic103.GeneralHBO bo = new nc.bs.ic.ic103.GeneralHBO();
        al = bo.queryBills(voQC);
      }
      else if (sBillType.equals(BillTypeConst.m_initWaster)) {// �ڳ���Ʒ
        nc.bs.ic.ic104.GeneralHBO bo = new nc.bs.ic.ic104.GeneralHBO();
        al = bo.queryBills(voQC);
      }
      else if (sBillType.equals(BillTypeConst.m_initEntrustMachining)) {// �ڳ����ϼӹ�
        nc.bs.ic.ic106.GeneralHBO bo = new nc.bs.ic.ic106.GeneralHBO();
        al = bo.queryBills(voQC);
      }
      else if (sBillType.equals(BillTypeConst.m_purchaseIn)) {// �ɹ����/���⴦��
        nc.bs.ic.ic201.GeneralHBO bo = new nc.bs.ic.ic201.GeneralHBO();
        al = bo.queryBills(voQC);
      }
      else if (sBillType.equals(BillTypeConst.m_productIn)) {// ����Ʒ���
        nc.bs.ic.ic202.GeneralHBO bo = new nc.bs.ic.ic202.GeneralHBO();
        al = bo.queryBills(voQC);
      }
      else if (sBillType.equals(BillTypeConst.m_consignMachiningIn)) {// ί��ӹ����
        nc.bs.ic.ic203.GeneralHBO bo = new nc.bs.ic.ic203.GeneralHBO();
        al = bo.queryBills(voQC);
      }
      else if (sBillType.equals(BillTypeConst.m_entrustMachiningIn)) {// ���ϼӹ����
        nc.bs.ic.ic204.GeneralHBO bo = new nc.bs.ic.ic204.GeneralHBO();
        al = bo.queryBills(voQC);
      }
      else if (sBillType.equals(BillTypeConst.m_borrowIn)) {// ����
        nc.bs.ic.ic205.GeneralHBO bo = new nc.bs.ic.ic205.GeneralHBO();
        al = bo.queryBills(voQC);
      }
      else if (sBillType.equals(BillTypeConst.m_lendIn)) {// �������
        nc.bs.ic.ic206.GeneralHBO bo = new nc.bs.ic.ic206.GeneralHBO();
        al = bo.queryBills(voQC);
      }
      else if (sBillType.equals(BillTypeConst.m_otherIn)) {// �������
        nc.bs.ic.ic207.GeneralHBO bo = new nc.bs.ic.ic207.GeneralHBO();
        al = bo.queryBills(voQC);
      }
      else if (sBillType.equals(BillTypeConst.m_allocationIn)) {// �������
        nc.bs.ic.ic209.GeneralHBO bo = new nc.bs.ic.ic209.GeneralHBO();
        al = bo.queryBills(voQC);
      }
      else if (sBillType.equals(BillTypeConst.m_wasterIn)) {// �����������
        nc.bs.ic.ic104.GeneralHBO bo = new nc.bs.ic.ic104.GeneralHBO();
        al = bo.queryBills(voQC);
      }
      else if (sBillType.equals(BillTypeConst.m_saleOut)) {// ���۳���
        nc.bs.ic.ic211.GeneralHBO bo = new nc.bs.ic.ic211.GeneralHBO();
        al = bo.queryBills(voQC);
      }
      else if (sBillType.equals(BillTypeConst.m_materialOut)) {// ���ϳ���
        nc.bs.ic.ic212.GeneralHBO bo = new nc.bs.ic.ic212.GeneralHBO();
        al = bo.queryBills(voQC);
      }
      else if (sBillType.equals(BillTypeConst.m_consignMachiningOut)) {// ί��ӹ�����
        nc.bs.ic.ic213.GeneralHBO bo = new nc.bs.ic.ic213.GeneralHBO();
        al = bo.queryBills(voQC);
      }
      else if (sBillType.equals(BillTypeConst.m_entrustMachiningOut)) {// ���ϼӹ�����
        nc.bs.ic.ic214.GeneralHBO bo = new nc.bs.ic.ic214.GeneralHBO();
        al = bo.queryBills(voQC);
      }
      else if (sBillType.equals(BillTypeConst.m_lendOut)) {// ���
        nc.bs.ic.ic215.GeneralHBO bo = new nc.bs.ic.ic215.GeneralHBO();
        al = bo.queryBills(voQC);
      }
      else if (sBillType.equals(BillTypeConst.m_borrowOut)) {// ���뻹��
        nc.bs.ic.ic216.GeneralHBO bo = new nc.bs.ic.ic216.GeneralHBO();
        al = bo.queryBills(voQC);
      }
      else if (sBillType.equals(BillTypeConst.m_otherOut)) {// ��������
        nc.bs.ic.ic217.GeneralHBO bo = new nc.bs.ic.ic217.GeneralHBO();
        al = bo.queryBills(voQC);
      }
      else if (sBillType.equals(BillTypeConst.m_allocationOut)) {// ��������
        nc.bs.ic.ic218.GeneralHBO bo = new nc.bs.ic.ic218.GeneralHBO();
        al = bo.queryBills(voQC);
      }
      else if (sBillType.equals(BillTypeConst.m_discardOut)) {// ����
        nc.bs.ic.ic241.GeneralHBO bo = new nc.bs.ic.ic241.GeneralHBO();
        al = bo.queryBills(voQC);
      }
      else if (sBillType.equals(BillTypeConst.m_wasterProcess)) {// ��Ʒ����
        nc.bs.ic.ic242.GeneralHBO bo = new nc.bs.ic.ic242.GeneralHBO();
        al = bo.queryBills(voQC);
      }
      else if (sBillType.equals(BillTypeConst.m_spaceAdjust)) {// ��λ����
        nc.bs.ic.ic251.GeneralHBO bo = new nc.bs.ic.ic251.GeneralHBO();
        al = bo.queryBills(voQC);
      }
      else if (sBillType.equals(BillTypeConst.m_assetIn)) {// �ʲ���ⵥ
        nc.bs.ic.ic207.GeneralHBOAsset bo = new nc.bs.ic.ic207.GeneralHBOAsset();
        al = bo.queryBills(voQC);
      }
      else if (sBillType.equals(BillTypeConst.m_assetOut)) {// �ʲ����ⵥ
        nc.bs.ic.ic217.GeneralHBOAsset bo = new nc.bs.ic.ic217.GeneralHBOAsset();
        al = bo.queryBills(voQC);
      }

      // ����Ч�����⣺�������ⵥ����������
      BillDataFillingUtil dataFillingUtil = new BillDataFillingUtil();
      al = dataFillingUtil.fillPackTypeInfo(al);
      al = dataFillingUtil.fillBillTypeName(al);
      al = dataFillingUtil.fillQualityLevelName(al);
    }
    catch (Exception e) {
      throw GenMethod.handleException(null, e);
    }
    return al;
  }

  public Object queryInfo(Integer iSel, Object alQryCond)
      throws BusinessException {
    try {
      GeneralBillBO bo = new GeneralBillBO();
      return bo.queryInfo(iSel, alQryCond);
    }
    catch (Exception e) {
      if (e instanceof BusinessException)
        throw (BusinessException) e;
      else
        throw new BusinessException("Caused by:", e);
    }

  }

  /**
   * �����ߣ����˾� ���ܣ���ѯ����ģ�����ݣ���ѯ��������һ�����ء� ������ Cond.param(0)�� alParam: 0:String
   * sCorpID 1:String sUserID 2:String sBillTypeCode 3:String sBillPK ���أ� ���⣺
   * ���ڣ�(2001-8-28 14:07:59) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
   */
  public ArrayList queryJointCheckBills(QryConditionVO voQC)
      throws BusinessException {
    try {
      if (voQC == null || voQC.getParam(0) == null)
        throw new BusinessException(nc.bs.ml.NCLangResOnserver.getInstance()
            .getStrByID("4008bill", "UPP4008bill-000237")/* @res "���鵥�ݲ�������" */);
      ArrayList alParam = (ArrayList) voQC.getParam(0);
      if (alParam == null || alParam.size() < 4)
        throw new BusinessException(nc.bs.ml.NCLangResOnserver.getInstance()
            .getStrByID("4008bill", "UPP4008bill-000237")/* @res "���鵥�ݲ�������" */);
      // ��˾���û����������ͣ�����PK
      String sCorpID = (String) alParam.get(0);
      String sUserID = (String) alParam.get(1);
      String sBillTypeCode = (String) alParam.get(2);
      String sBillPK = (String) alParam.get(3);
      // ------------------
      // ��ѯ�������õ�BO�����ø��ڵ��µĵ���BO,���ٵ���ͨ�õĵ���DMO.

      String sQryBillBO = "";
      // //������
      // nc.bs.ic.pub.bill.GeneralBillDMO dmoBill =
      // new nc.bs.ic.pub.bill.GeneralBillDMO();
      ArrayList alBill = queryBills(sBillTypeCode, new QryConditionVO(
          "head.cgeneralhid='" + sBillPK + "'"));
      // �鲻��
      if (alBill == null || alBill.size() == 0)
        throw new BusinessException(nc.bs.ml.NCLangResOnserver.getInstance()
            .getStrByID("4008bill", "UPP4008bill-000238")/*
                                                           * @res
                                                           * "δ�ҵ����ݣ���������ϵͳ���ݻ�����ҵ����ת�ⵥ�����ݡ�"
                                                           */);
      // ��ȡģ������
      nc.bs.pub.bill.BillTempletDMO btdmo = new nc.bs.pub.bill.BillTempletDMO();
      ArrayList alRet = new ArrayList();
      // ����
      alRet.add(btdmo.findDefaultCardTempletData(sBillTypeCode, sCorpID, null,
          sUserID));

      if (alBill != null && alBill.size() > 0)
        alRet.add(alBill.get(0));
      else
        // �鲻������
        alRet.add(null);
      // С������
      // �������� ���� ȱʡֵ
      // BD501 ����С��λ 2
      // BD502 ����������С��λ 2
      // BD503 ������ 2
      // BD504 ����ɱ�����С��λ 2
      String[] saScale = new String[] {
          "2", "2", "2", "2"
      };

      String[] saParam = new String[] {
          "BD501", "BD502", "BD503", "BD504"
      };
      saScale = new nc.bs.ic.pub.bill.MiscDMO().getSysParam(sCorpID, saParam);

      alRet.add(saScale);
      return alRet;

    }
    catch (Exception e) {
      GenMethod.throwBusiException(e);
    }
    return null;
  }

  /**
   * �����ߣ������� ���ܣ������׼���ѯ ������ ���أ� ���⣺ ���ڣ�(2001-6-26 ���� 9:17) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
   * 
   * @return java.util.ArrayList
   * @param Invid
   *          java.lang.String
   */
  public ArrayList queryPartbySetInfo(String sSetInvID)
      throws BusinessException {
    try {
      return new MiscDMO().queryPartbySetInfo(sSetInvID);
    }
    catch (Exception e) {
      GenMethod.throwBusiException(e);
    }
    return null;
  }

  /**
   * �����ߣ����˾� ���ܣ���ָ��������ѯ���ݡ���ģ�����浥�ݡ�v53 add ������ ���أ� ���⣺ ���ڣ�(2001-6-12 20:38:02)
   * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־�� 2002-01-16 , ���˾�����ֲ��oracle
   * 
   * @return nc.vo.ic.pub.bill.GeneralBillVO[]
   * @param voQryCond
   *          nc.vo.ic.pub.bill.QryConditionVO
   */
  public ArrayList queryBillByPks_for_OutModule(String[] pks)
      throws BusinessException {
    try {
      return new GeneralBillDMO().queryBillByPks_for_OutModule(pks);
    }
    catch (Exception e) {
      GenMethod.throwBusiException(e);
    }
    return null;
  }

  /**
   * �����ˣ������� �������ڣ�2008-3-31����04:18:20 ������ǩ��;�𵥱���id ���ؽ����HashMap<��ͷid,;��VO>
   * 
   * @param bids
   * @return
   * @throws BusinessException
   */

  public HashMap<String, GeneralBillVO> getGeneralBillVOsByBids(
      ArrayList<String> bids) throws BusinessException {
    HashMap<String, GeneralBillVO> hsBillVOs = new HashMap<String, GeneralBillVO>();
    if (bids == null || bids.size() <= 0)
      return hsBillVOs;
    final String code = "body.cgeneralbid";
    nc.vo.pub.query.ConditionVO[] voaCond = new nc.vo.pub.query.ConditionVO[1];
    voaCond[0] = new nc.vo.pub.query.ConditionVO();
    voaCond[0].setFieldCode(code);
    voaCond[0].setLogic(true);
    voaCond[0].setOperaCode(" in ");

    String insql = GeneralSqlString.formInSQL(code, bids);
    String insqlbak = " 1=1 " + insql;
    insql = insql.trim();
    if (insql.indexOf(code) >= 0)
      insql = insql.substring(insql.indexOf(code) + code.length());

    QryConditionVO qryconvo = new QryConditionVO(insqlbak);

    qryconvo.setParam(QryConditionVO.QRY_CONDITIONVO, voaCond);

    qryconvo.setIntParam(0, GeneralBillVO.QRY_FULL_BILL);
    try {
      ArrayList arrBillVOs = null;
      nc.bs.ic.pub.bill.GeneralBillDMO generalBillDMO = new nc.bs.ic.pub.bill.GeneralBillDMO();
      arrBillVOs = generalBillDMO.queryBills(qryconvo);
      if (null != arrBillVOs && 0 < arrBillVOs.size())
        for (int i = 0; i < arrBillVOs.size(); i++)
          if (null != arrBillVOs.get(i)
              && arrBillVOs.get(i) instanceof GeneralBillVO) {
            GeneralBillVO billVO = (GeneralBillVO) arrBillVOs.get(i);
            if (null != billVO && null != billVO.getItemVOs()
                && 0 < billVO.getItemVOs().length) {

              ArrayList<GeneralBillItemVO> arritemvos = new ArrayList<GeneralBillItemVO>();
              for (GeneralBillItemVO itemvo : billVO.getItemVOs())
                if (null != itemvo.getCgeneralbid()
                    && bids.contains(itemvo.getCgeneralbid()))
                  arritemvos.add((GeneralBillItemVO) itemvo.clone());
              if (0 < arritemvos.size()) {
                GeneralBillItemVO[] itemVOs = new GeneralBillItemVO[arritemvos
                    .size()];
                arritemvos.toArray(itemVOs);
                GeneralBillVO newbillvo = (GeneralBillVO) billVO.clone();
                newbillvo.setChildrenVO(itemVOs);
                hsBillVOs.put(newbillvo.getHeaderVO().getCgeneralhid(),
                    newbillvo);
              }

            }
          }

    }
    catch (Exception e) {
      throw GenMethod.handleException(null, e);
    }
    return hsBillVOs;
  }

  public GeneralBillVO[] getGenBillVOsByBids(ArrayList<String> bids)
      throws BusinessException {
    GeneralBillVO[] returnBillVOs = null;
    ArrayList<GeneralBillVO> hsBillVOs = new ArrayList<GeneralBillVO>();
    if (bids == null || bids.size() <= 0)
      return returnBillVOs;
    final String code = "body.cgeneralbid";
    nc.vo.pub.query.ConditionVO[] voaCond = new nc.vo.pub.query.ConditionVO[1];
    voaCond[0] = new nc.vo.pub.query.ConditionVO();
    voaCond[0].setFieldCode(code);
    voaCond[0].setLogic(true);
    voaCond[0].setOperaCode(" in ");

    String insql = GeneralSqlString.formInSQL(code, bids);
    String insqlbak = " 1=1 " + insql;
    insql = insql.trim();
    if (insql.indexOf(code) >= 0)
      insql = insql.substring(insql.indexOf(code) + code.length());

    QryConditionVO qryconvo = new QryConditionVO(insqlbak);

    qryconvo.setParam(QryConditionVO.QRY_CONDITIONVO, voaCond);

    qryconvo.setIntParam(0, GeneralBillVO.QRY_FULL_BILL);
    try {
      ArrayList arrBillVOs = null;
      nc.bs.ic.pub.bill.GeneralBillDMO generalBillDMO = new nc.bs.ic.pub.bill.GeneralBillDMO();
      arrBillVOs = generalBillDMO.queryBills(qryconvo);
      if (null != arrBillVOs && 0 < arrBillVOs.size())
        for (int i = 0; i < arrBillVOs.size(); i++)
          if (null != arrBillVOs.get(i)
              && arrBillVOs.get(i) instanceof GeneralBillVO) {
            GeneralBillVO billVO = (GeneralBillVO) arrBillVOs.get(i);
            if (null != billVO && null != billVO.getItemVOs()
                && 0 < billVO.getItemVOs().length) {

              ArrayList<GeneralBillItemVO> arritemvos = new ArrayList<GeneralBillItemVO>();
              for (GeneralBillItemVO itemvo : billVO.getItemVOs())
                if (null != itemvo.getCgeneralbid()
                    && bids.contains(itemvo.getCgeneralbid()))
                  arritemvos.add((GeneralBillItemVO) itemvo.clone());
              if (0 < arritemvos.size()) {
                GeneralBillItemVO[] itemVOs = new GeneralBillItemVO[arritemvos
                    .size()];
                arritemvos.toArray(itemVOs);
                GeneralBillVO newbillvo = (GeneralBillVO) billVO.clone();
                newbillvo.setChildrenVO(itemVOs);
                hsBillVOs.add(newbillvo);
              }

            }
          }

      if (0 < hsBillVOs.size()) {
        returnBillVOs = new GeneralBillVO[hsBillVOs.size()];
        hsBillVOs.toArray(returnBillVOs);
      }

    }
    catch (Exception e) {
      throw GenMethod.handleException(null, e);
    }
    return returnBillVOs;
  }

  /**
   * �����ˣ������� �������ڣ�2008-3-31����04:18:20 ����������id ���ؽ����HashMap<��ͷid,VO>
   * 
   * @param bids
   * @return
   * @throws BusinessException
   */
  public HashMap<String, WastageBillVO> getWastageBillsByBids(
      ArrayList<String> bids) throws BusinessException {
    HashMap<String, WastageBillVO> hsWastBillVOs = new HashMap<String, WastageBillVO>();
    if (null == bids || 0 == bids.size())
      return null;

    try {
      WastageBillVO[] wastVOs = null;
      nc.bs.ic.ic700.WastageBillDAO wastageBillDAO = new nc.bs.ic.ic700.WastageBillDAO();
      wastVOs = wastageBillDAO.queryBillData(nc.bs.ic.pub.bill.GeneralSqlString
          .formInSQL("ic_wastagebill_b.cwastagebillbid", bids));

      if (null != wastVOs && 0 < wastVOs.length) {

        for (WastageBillVO returnVO : wastVOs)
          if (null != returnVO && null != returnVO.getBodyVOs()
              && 0 < returnVO.getBodyVOs().length) {
            ArrayList<WastageBillBVO> arritemvos = new ArrayList<WastageBillBVO>();
            for (WastageBillBVO itemvo : returnVO.getBodyVOs())
              if (null != itemvo.getAttributeValue("cwastagebillbid")
                  && bids.contains(itemvo.getAttributeValue("cwastagebillbid")
                      .toString()))
                arritemvos.add((WastageBillBVO) itemvo.clone());
            if (0 < arritemvos.size()) {
              WastageBillBVO[] itemVOs = new WastageBillBVO[arritemvos.size()];
              arritemvos.toArray(itemVOs);
              WastageBillVO billvo = (WastageBillVO) returnVO.clone();
              billvo.setChildrenVO(itemVOs);
              hsWastBillVOs.put(billvo.getHeadVO().getAttributeValue(
                  "cwastagebillid").toString(), billvo);
            }

          }
      }

    }
    catch (Exception e) {
      throw GenMethod.handleException(null, e);
    }
    return hsWastBillVOs;
  }

  public HashMap<String, Object> transBillDataDeal(
      HashMap<String, ArrayList> hm_paras) throws BusinessException {
    HashMap<String, Object> hmRet = new HashMap<String, Object>();
    try {
      ICSmartToolsDmo smartDMO = new ICSmartToolsDmo();
      // ��ȫ������Ϣ
      if (hm_paras.containsKey(IICParaConst.GetOrderInfoPara)) {
        ArrayList<String> al_para = hm_paras.get(IICParaConst.GetOrderInfoPara);
        ArrayList<String> al_orderbids = new ArrayList<String>();
        //StringBuilder in_Sql = new StringBuilder();
        for (int i = 1; i < al_para.size(); i++) {
 /*         if (i > 1)
            in_Sql.append(",");
          in_Sql.append("'").append(al_para.get(i)).append("'");*/
          al_orderbids.add(al_para.get(i));
        }
        String[] icvokeys = null;
        String[] dbkeys = null;
        int[] types = null;
        String cfirsttype = al_para.get(0);
        StringBuilder sql = new StringBuilder();
        if (cfirsttype.equals(BillTypeConst.SO_Order)) {
          sql
              .append("select so_saleorder_b.corder_bid, nnumber")
              .append(
                  ", ntranslossnum, ntotalinventorynumber, bifinventoryfinish ")
              .append(
                  "from so_saleorder_b inner join so_saleexecute on ( so_saleorder_b")
              .append(
                  ".corder_bid=so_saleexecute.csale_bid and  so_saleorder_b.csaleid")
              .append(" = so_saleexecute.csaleid  ) ").append(
                  " where so_saleorder_b.dr = 0 ").append(GeneralSqlString.formInSQL("so_saleorder_b.corder_bid", al_orderbids));
          dbkeys = new String[] {
              "so_saleorder_b.corder_bid", "nnumber", "ntranslossnum",
              "ntotalinventorynumber", "bifinventoryfinish"
          };
          icvokeys = new String[] {
              IItemKey.CFIRSTBILLBID, IItemKey.nordnum, IItemKey.nordwastnum,
              IItemKey.nordoutnum, IItemKey.bcloseyetord
          };
          types = new int[] {
              SmartFieldMeta.JAVATYPE_STRING, SmartFieldMeta.JAVATYPE_UFDOUBLE,
              SmartFieldMeta.JAVATYPE_UFDOUBLE,
              SmartFieldMeta.JAVATYPE_UFDOUBLE,
              SmartFieldMeta.JAVATYPE_UFBOOLEAN
          };
        }
        else if (cfirsttype.equals(BillTypeConst.SO5_ReturnApp)) {
          sql
              .append(
                  "select so_apply_b.pk_apply_b, so_saleorder_b.nnumber, so_saleexecute.ntranslossnum")
              .append(
                  ", so_saleexecute.ntotalinventorynumber, so_saleexecute.bifinventoryfinish")
              .append(
                  " from so_apply_b inner join so_saleexecute on ( so_apply_b.cfirstbillbid ")
              .append(
                  " = so_saleexecute.csale_bid and so_apply_b.cfirstbillhid = so_saleexecute.csaleid ) ")
              .append(
                  " inner join so_saleorder_b on ( so_saleexecute.csale_bid = so_saleorder_b ")
              .append(
                  ".corder_bid and so_saleexecute.csaleid = so_saleorder_b.csaleid  ) ")
              .append(" where so_apply_b.dr = 0 and so_saleorder_b.dr = 0   ").append(GeneralSqlString.formInSQL("so_apply_b.pk_apply_b", al_orderbids));
          dbkeys = new String[] {
              "so_apply_b.pk_apply_b", "so_saleorder_b.nnumber",
              "so_saleexecute.ntranslossnum",
              "so_saleexecute.ntotalinventorynumber",
              "so_saleexecute.bifinventoryfinish"
          };
          icvokeys = new String[] {
              IItemKey.CFIRSTBILLBID, IItemKey.nordnum, IItemKey.nordwastnum,
              IItemKey.nordoutnum, IItemKey.bcloseyetord
          };
          types = new int[] {
              SmartFieldMeta.JAVATYPE_STRING, SmartFieldMeta.JAVATYPE_UFDOUBLE,
              SmartFieldMeta.JAVATYPE_UFDOUBLE,
              SmartFieldMeta.JAVATYPE_UFDOUBLE,
              SmartFieldMeta.JAVATYPE_UFBOOLEAN
          };
        }
        else {
          sql
              .append(
                  " select cbill_bid, nnum, norderwaylossnum, norderoutnum, boutendflag ")
              .append(" from to_bill_b where to_bill_b.dr = 0 ").append(GeneralSqlString.formInSQL("cbill_bid", al_orderbids));
          dbkeys = new String[] {
              "cbill_bid", "nnum", "norderwaylossnum", "norderoutnum",
              "boutendflag"
          };
          icvokeys = new String[] {
              IItemKey.CFIRSTBILLBID, IItemKey.nordnum, IItemKey.nordwastnum,
              IItemKey.nordoutnum, IItemKey.bcloseyetord
          };
          types = new int[] {
              SmartFieldMeta.JAVATYPE_STRING, SmartFieldMeta.JAVATYPE_UFDOUBLE,
              SmartFieldMeta.JAVATYPE_UFDOUBLE,
              SmartFieldMeta.JAVATYPE_UFDOUBLE,
              SmartFieldMeta.JAVATYPE_UFBOOLEAN
          };
        }

        ICDataSet ds = smartDMO.getDataSet(new String[] {
          IItemKey.CFIRSTBILLBID
        }, icvokeys, types, sql.toString());
        if (null != ds && ds.getRowCount() > 0) {
          hmRet.put(IICParaConst.GetOrderInfoPara, ds);
          hmRet.put(IICParaConst.ICBillOrderItemsPara, icvokeys);
        }
      }

      // ��ȫ���ε�����Ϣ
      if (hm_paras.containsKey(IICParaConst.BatchCodePara)) {
        String sPk_invbasdoc = null;
        String sVbatchCode = null;
        ArrayList<String> al_itemkey = new ArrayList<String>();
        ArrayList<String> al_tablekey = new ArrayList<String>();
        al_itemkey.add("pk_invbasdoc");
        al_itemkey.add("vbatchcode");
        al_tablekey.add("pk_invbasdoc");
        al_tablekey.add("vbatchcode");
        StringBuilder sql = new StringBuilder(
            "select scm_batchcode.pk_invbasdoc,scm_batchcode.vbatchcode ");
        for (int i = 0; i < IItemKey.batchcodeDocFiels.length; i++) {
          sql.append(",");
          sql.append("scm_batchcode.");
          sql.append(IItemKey.batchcodeDocFiels[i]);
          al_tablekey.add(IItemKey.batchcodeDocFiels[i]);
          al_itemkey.add(IItemKey.batchcodefielsAtBillBody[i]);
        }
        sql.append(",");
        sql.append("qc_checkstate_b.");
        sql.append("ccheckstatename");
        sql
            .append(" from scm_batchcode left outer join qc_checkstate_b on qc_checkstate_b.ccheckstate_bid=scm_batchcode.cqualitylevelid where (");
        boolean addOR = false;
        for (String s : (ArrayList<String>) (hm_paras
            .get(IICParaConst.BatchCodePara))) {
          sPk_invbasdoc = s.substring(0, 20);
          sVbatchCode = s.substring(20, s.length());
          if (addOR)
            sql.append(" or ");
          addOR = true;
          sql.append(" (pk_invbasdoc = '").append(sPk_invbasdoc).append(
              "' and vbatchcode = '").append(sVbatchCode).append("') ");
        }
        sql.append(") and scm_batchcode.dr = 0 ");

        al_tablekey.add("cqualitylevelname");
        int[] queryType = SmartVOUtilExt.getSmartVOFieldTypes(
            BatchcodeVO.class, al_tablekey.toArray(new String[0]));

        queryType[queryType.length - 1] = SmartFieldMeta.JAVATYPE_STRING;// ���һ���ֶ�����������Ϊ�ַ��ͣ�����BatchcodeVO�޴��ֶ�Ĭ��Ϊ0

        al_itemkey.add("cqualitylevelname");
        ICDataSet ds = smartDMO.getDataSet(new String[] {
            "pk_invbasdoc", "vbatchcode"
        }, al_itemkey.toArray(new String[0]), queryType, sql.toString());
        // //��������ȼ����� ������ 2009-08-31

        hmRet.put(IICParaConst.BatchCodePara, ds);
      }

      if (hm_paras.containsKey(IICParaConst.DptBizerPara)) {
        ArrayList<String> al_qDptBizer = hm_paras
            .get(IICParaConst.DptBizerPara);
        String pk_corp = al_qDptBizer.get(0);
        String userid = al_qDptBizer.get(1);
        PsndocVO psndoc = ((nc.itf.uap.rbac.IUserManageQuery) NCLocator
            .getInstance().lookup("nc.itf.uap.rbac.IUserManageQuery"))
            .getPsndocByUserid(pk_corp, userid);
        if (null != psndoc && null != psndoc.getSealdate())// �з������˵���ѷ�棬���������Ϊû�ҵ���Ӧҵ��Ա������
          hmRet.put(IICParaConst.DptBizerPara, null);
        else
          hmRet.put(IICParaConst.DptBizerPara, psndoc);
      }

      if (hm_paras.containsKey(IICParaConst.ReSetWHInfoPara)) {
        String whid = (String) hm_paras.get(IICParaConst.ReSetWHInfoPara)
            .get(0);
        WhVO whvo = (WhVO) queryInfo(new Integer(QryInfoConst.WH), whid);
        hmRet.put(IICParaConst.ReSetWHInfoPara, whvo);
      }

      String sCalID = null;// �����֯ID
      if (hm_paras.containsKey(IICParaConst.CostLandPara)) {
        ArrayList<String> al_qCostCal = hm_paras.get(IICParaConst.CostLandPara);
        sCalID = al_qCostCal.get(0);
        String sWhID = al_qCostCal.get(1);
        String pk_corp = al_qCostCal.get(2);
        String sCostCalID = null;
        //����ֿ�Ϊ����ȡ�ֿ��Ӧ�Ŀ����֯ ����ȡ�ɱ������쳣 ������ 090914
        if(null==sCalID){
          String sql="select pk_calbody from bd_stordoc where pk_stordoc='"+sWhID+"'";
          ICDbVisitor dbvisitor=new ICDbVisitor();        
          ICDataSet resultSet=dbvisitor.query(sql);
          sCalID=resultSet.getStringValueAt(0, 0);  
        }
        if(null != sWhID)
          sCostCalID = ((IStorVSCostQry) NCLocator.getInstance().lookup(
              IStorVSCostQry.class)).getCostCalBody(pk_corp, sCalID, sWhID);
        hmRet.put(IICParaConst.CostLandPara, sCostCalID);
      }

      if (hm_paras.containsKey(IICParaConst.ProduceJHJPara) && null != sCalID) {
        ArrayList<String> al_qJHJ = hm_paras.get(IICParaConst.ProduceJHJPara);
        if (al_qJHJ.size() > 0) {
          HashMap<String, Object> hm_qJHJ = new HashMap<String, Object>();
          StringBuilder sql = new StringBuilder(
              "SELECT pk_invmandoc, pk_calbody, jhj, pk_rkjlcid, pk_ckjlcid FROM bd_produce WHERE pk_calbody = '");
          sql.append(sCalID+"'");
//          .append("' and pk_invmandoc in (");
//          String[] sCinventoryids = new String[al_qJHJ.size()];
//          String[] sCalIDs = new String[al_qJHJ.size()];
//          for (int i = 0; i < al_qJHJ.size(); i++) {
//            sCinventoryids[i] = al_qJHJ.get(i);
//            sCalIDs[i] = sCalID;
//            if (i > 0)
//              sql.append(" , ");
//            sql.append("'").append(sCinventoryids[i]).append("'");
//          }
//          sql.append(")");
          //������ 2009-12-09  ������ʱ����� In
          String[] sCinventoryids = new String[al_qJHJ.size()];
          for (int i = 0; i < al_qJHJ.size(); i++) {
            sCinventoryids[i] = al_qJHJ.get(i);
          }
         sql.append(GeneralSqlString.formInSQL("pk_invmandoc", sCinventoryids));  
       
          ICDataSet ds = smartDMO.getDataSet(new String[] {
              "pk_invmandoc", "pk_calbody"
          }, new String[] {
              "pk_invmandoc", "pk_calbody", "jhj", "pk_rkjlcid", "pk_ckjlcid"
          }, new int[] {
              SmartFieldMeta.JAVATYPE_STRING, SmartFieldMeta.JAVATYPE_STRING,
              SmartFieldMeta.JAVATYPE_UFDOUBLE, SmartFieldMeta.JAVATYPE_STRING,
              SmartFieldMeta.JAVATYPE_STRING
          }, sql.toString());
          String cinventoryid = null;
          String pk_calbody = null;
          String key = null;
          for (int i = 0; i < ds.getRowCount(); i++) {
            cinventoryid = ds.getStringValueAt(i, "pk_invmandoc");
            pk_calbody = ds.getStringValueAt(i, "pk_calbody");
            key = cinventoryid + pk_calbody;
            if (!hm_qJHJ.containsKey(key)) {
              Object[] obj = new Object[3];
              obj[0] = ds.getUFDoubleValueAt(i, "jhj");
              obj[1] = ds.getStringValueAt(i, "pk_rkjlcid");
              obj[2] = ds.getStringValueAt(i, "pk_ckjlcid");
              hm_qJHJ.put(key, obj);
            }
          }
          hmRet.put(IICParaConst.ProduceJHJPara, hm_qJHJ);
        }
      }
    }
    catch (Exception e) {
      // ��־�쳣
      nc.vo.scm.pub.SCMEnv.out(e);
      throw GenMethod.handleException("ȡ���κ���Ϣ����", e);
    }

    return hmRet;
  }

  public HashMap<String, Object> getBillVOByLinkOpen(String PkOrg,
      String billtype, String billid, String pkUser, String funCode,
      String logonCorp) throws BusinessException {
    HashMap<String, Object> retHM = new HashMap<String, Object>();
    try {
      ArrayList<GeneralBillVO> alListData = null;
      ICSmartToolsDmo smartDMO = new ICSmartToolsDmo();
      ICDataSet datas = smartDMO.getDataSet("ic_general_h", "cgeneralhid",
          new String[] {
            "pk_corp"
          }, new int[] {
            SmartFieldMeta.JAVATYPE_STRING
          }, new String[] {
            billid
          }, " dr=0 ");

      String cbillpkcorp = datas == null ? null : (String) datas.getValueAt(0,
          0);

      if (cbillpkcorp == null || cbillpkcorp.trim().length() <= 0)
        return null;

      retHM.put(IICParaConst.LinkBillCorpPara, cbillpkcorp);

      TemplateParaVO paraVo = new TemplateParaVO();
      paraVo.setTemplateType(ITemplateStyle.queryTemplate);
      paraVo.setPk_orgUnit(cbillpkcorp);
      paraVo.setOrgType(OrgnizeTypeVO.COMPANY_TYPE);
      paraVo.setFunNode(funCode);
      paraVo.setOperator(pkUser);
      paraVo.setBusiType(null);
      paraVo.setNodeKey(null);
      IQueryTemplate queryTemplateService = (IQueryTemplate) NCLocator
          .getInstance().lookup(IQueryTemplate.class.getName());
      QueryTempletTotalVO totalVO = queryTemplateService.selectTotalData(
          paraVo, funCode, cbillpkcorp);
      if (null == totalVO)
        throw new BusinessException(NCLangResOnserver.getInstance()
            .getStrByID("_Template", "UPP_Template-000106")/* ��ǰ��½�û�û�п��õĲ�ѯģ�� */);
//      QueryTranslator.translate(totalVO);
      QueryConditionVO[] m_templetDatas = totalVO.getConditionVOs();
      ArrayList<QueryConditionVO> al_convo = new ArrayList<QueryConditionVO>();
      HashMap<String, QueryConditionVO> hm_code_convo = new HashMap<String, QueryConditionVO>();
      for (QueryConditionVO vo : m_templetDatas) {
        if (vo.getIfUsed().booleanValue()) {
          if (!vo.getIfImmobility().booleanValue()) {
            al_convo.add(vo);
            hm_code_convo.put(vo.getFieldCode(), vo);
          }
        }
        else
          continue;
      }
      QueryConditionVO[] vos = al_convo.toArray(new QueryConditionVO[0]);
      String[] refcodes = null;
      if (BillTypeConst.m_allocationOut.equals(billtype)
          || BillTypeConst.m_allocationIn.equals(billtype))
        refcodes = nc.vo.ic.pub.GenMethod.getDataPowerFieldFromCondVOs(vos,
            new String[] {
                "head.cothercorpid", "head.coutcorpid", "body.creceieveid",
                "head.cothercalbodyid", "head.cotherwhid", "head.coutcalbodyid"
            });
      else
        refcodes = nc.vo.ic.pub.GenMethod.getDataPowerFieldFromCondVOs(vos,
            null);
      ConditionVO[] convos = null;
      if (null != logonCorp && logonCorp.equals(cbillpkcorp)) {
        if (null != refcodes && refcodes.length > 0) {
          QueryConditionVO vo = null;
          ArrayList<ConditionVO> al_con = new ArrayList<ConditionVO>();
          for (String code : refcodes) {
            vo = hm_code_convo.get(code);
            appendPowerCons(al_con, vo, cbillpkcorp, pkUser);
          }
          if (al_con.size() > 0) {
            convos = al_con.toArray(new ConditionVO[0]);
            convos = nc.vo.ic.pub.GenMethod.procMultCorpDeptBizDP(convos,
                billtype, cbillpkcorp);
          }

        }
      }

      alListData = qryBill(cbillpkcorp, billtype, null, pkUser, billid, convos);
      retHM.put(IICParaConst.LinkQryBillPara, alListData);

      return retHM;
    }
    catch (Exception e) {
      // ��־�쳣
      nc.vo.scm.pub.SCMEnv.out(e);
      // ���淶�׳��쳣
      throw GenMethod.handleException("��ѯ����ʱ���ϴ���", e);
    }
  }

  private static HashMap<String, AbstractRefModel> refModelMap = new HashMap<String, AbstractRefModel>();

  // ��������
  private final static int STRING = 0; // �ַ�

  private final static int INTEGER = 1; // ����

  private final static int DECIMAL = 2; // С��

  private final static int DATE = 3; // ����

  private final static int BOOLEAN = 4; // �߼�

  private final static int UFREF = 5; // ����

  private final static int USERCOMBO = 6; // ����

  private final static int USERDEF = 7; // ����

  private final static int TIME = 8; // ʱ��

  private final static int DISPCODE = 0; // ��ʾ����

  private final static int DISPNAME = 1; // ��ʾ����

  private final static int RETURNCODE = 0; // ���ر���

  private final static int RETURNNAME = 1; // ��������

  private final static int RETURNPK = 2; // ����PK

  public static String getRefTableName(String refNodeName) throws Exception {

    if (refNodeName == null) {
      return null;
    }

    AbstractRefModel refModel = (AbstractRefModel) refModelMap.get(refNodeName);
    if (refModel == null) {
      refModel = RefPubUtil.getRefModel(refNodeName);
      refModelMap.put(refNodeName, refModel);
    }

    if (refModel == null) {
      return null;
    }
    refModel.setRefNodeName(refNodeName);
    String tableName = refModel.getTableName();
    String singleName = StringUtil.splitJoinedTableName(tableName)[0];
    return singleName;
  }

  protected void appendPowerCons(ArrayList<ConditionVO> vecVO,
      QueryConditionVO voQuery, String corpValue, String pkUser)
      throws BusinessException {
    String insql = getMultiCorpsPowerSql(voQuery, corpValue, pkUser);

    if (insql == null || insql.trim().length() <= 0)
      return;

    // ������ǡ������ġ�����Ȩ�޴�������Դ�������
    if (insql.indexOf(VariableConst.PREFIX_OF_DATAPOWER_SQL) < 0) {
      return;
    }

    String fieldcode = voQuery.getFieldCode();
    ConditionVO vo1 = new ConditionVO();
    vo1.setFieldCode(fieldcode);
    vo1.setDataType(INTEGER);
    vo1.setOperaCode(" is ");
    vo1.setValue(" NULL ");
    vo1.setNoLeft(false);

    vecVO.add(vo1);

    ConditionVO vo = new ConditionVO();
    vo.setTableName(voQuery.getTableName());
    vo.setTableCode(voQuery.getTableCode());
    vo.setFieldCode(fieldcode);
    vo.setDataType(INTEGER);
    vo.setOperaCode("in");
    if (voQuery.getReturnType().intValue() == RETURNPK)
      vo.setValue(insql);
    else {
      String sRefNodeName = voQuery.getConsultCode();
      StringBuffer subSql = new StringBuffer(" (select ");
      String sField = getRefField(sRefNodeName, voQuery.getReturnType()
          .intValue());
      subSql.append(sField).append(getRefField(sRefNodeName, -1)).append(
          " and " + getRefField(sRefNodeName, RETURNPK)).append(" in ").append(
          insql).append(")");

      vo.setValue(subSql.toString());
    }

    vo.setNoRight(false);
    vo.setLogic(false);

    vecVO.add(vo);

  }

  protected String getMultiCorpsPowerSql(QueryConditionVO voQuery, String corp,
      String pkUser) throws BusinessException {

    String sCurUser = pkUser;
    String sRefNodeName = voQuery.getConsultCode();

    String dpTableName = null;
    try {
      dpTableName = getRefTableName(sRefNodeName); // ���ݲ������ƻ�û������ݱ���
    }
    catch (Exception e) {

    }

    if (dpTableName == null)
      return null;

    String insql = ICCommonBusiImpl.getDataPowerByUser(dpTableName,
        sRefNodeName, sCurUser, corp);

    return insql;
  }

  private static HashMap<String, String> m_htRefField = null;

  protected String getRefField(String sRefNodeName, int flag) {
    if (m_htRefField == null) {
      m_htRefField = new HashMap<String, String>();
      m_htRefField.put("�����֯" + String.valueOf(RETURNCODE), "bodycode");
      m_htRefField.put("�����֯" + String.valueOf(RETURNNAME), "bodyname");
      m_htRefField.put("�����֯" + String.valueOf(RETURNPK), "pk_calbody");
      m_htRefField.put("�����֯" + String.valueOf(-1),
          " from bd_calbody where 0=0 ");
      m_htRefField.put("�ֿ⵵��" + String.valueOf(RETURNCODE), "storcode");
      m_htRefField.put("�ֿ⵵��" + String.valueOf(RETURNNAME), "storname");
      m_htRefField.put("�ֿ⵵��" + String.valueOf(RETURNPK), "pk_stordoc");
      m_htRefField.put("�ֿ⵵��" + String.valueOf(-1),
          " from bd_stordoc where 0=0 ");
      m_htRefField.put("���̵���" + String.valueOf(RETURNCODE), "custcode");
      m_htRefField.put("���̵���" + String.valueOf(RETURNNAME), "custname");
      m_htRefField.put("���̵���" + String.valueOf(RETURNPK), "pk_cumandoc");
      m_htRefField
          .put("���̵���" + String.valueOf(-1),
              " from bd_cubasdoc b ,bd_cumandoc m where b.pk_cubasdoc=m.pk_cubasdoc ");
      m_htRefField.put("�ͻ�����" + String.valueOf(RETURNCODE), "custcode");
      m_htRefField.put("�ͻ�����" + String.valueOf(RETURNNAME), "custname");
      m_htRefField.put("�ͻ�����" + String.valueOf(RETURNPK), "pk_cumandoc");
      m_htRefField
          .put("�ͻ�����" + String.valueOf(-1),
              " from bd_cubasdoc b ,bd_cumandoc m where b.pk_cubasdoc=m.pk_cubasdoc ");
      m_htRefField.put("��Ӧ�̵���" + String.valueOf(RETURNCODE), "custcode");
      m_htRefField.put("��Ӧ�̵���" + String.valueOf(RETURNNAME), "custname");
      m_htRefField.put("��Ӧ�̵���" + String.valueOf(RETURNPK), "pk_cumandoc");
      m_htRefField
          .put("��Ӧ�̵���" + String.valueOf(-1),
              " from bd_cubasdoc b ,bd_cumandoc m where b.pk_cubasdoc=m.pk_cubasdoc ");
      m_htRefField.put("���ŵ���" + String.valueOf(RETURNCODE), "deptcode");
      m_htRefField.put("���ŵ���" + String.valueOf(RETURNNAME), "deptname");
      m_htRefField.put("���ŵ���" + String.valueOf(RETURNPK), "pk_deptdoc");
      m_htRefField.put("���ŵ���" + String.valueOf(-1),
          " from bd_deptdoc where 0=0 ");
      m_htRefField.put("��Ա����" + String.valueOf(RETURNCODE), "psncode");
      m_htRefField.put("��Ա����" + String.valueOf(RETURNNAME), "psnname");
      m_htRefField.put("��Ա����" + String.valueOf(RETURNPK), "pk_psndoc");
      m_htRefField.put("��Ա����" + String.valueOf(-1),
          " from bd_psndoc where 0=0 ");
      // added by czp , 2006-09-21
      m_htRefField.put("�ɹ���֯" + String.valueOf(RETURNCODE), "code");
      m_htRefField.put("�ɹ���֯" + String.valueOf(RETURNNAME), "name");
      m_htRefField.put("�ɹ���֯" + String.valueOf(RETURNPK), "pk_purorg");
      m_htRefField.put("�ɹ���֯" + String.valueOf(-1),
          " from bd_purorg where 0=0 ");

      // Added by Shaw on Sep 27, 2006
      m_htRefField.put("�������" + String.valueOf(RETURNCODE), "invcode");
      m_htRefField.put("�������" + String.valueOf(RETURNNAME), "invname");
      m_htRefField.put("�������" + String.valueOf(RETURNPK), "pk_invmandoc");
      m_htRefField
          .put("�������" + String.valueOf(-1),
              " from bd_invbasdoc b, bd_invmandoc m where b.pk_invbasdoc = m.pk_invbasdoc ");
      m_htRefField.put("�������" + String.valueOf(RETURNCODE), "invclasscode");
      m_htRefField.put("�������" + String.valueOf(RETURNNAME), "invclassname");
      m_htRefField.put("�������" + String.valueOf(RETURNPK), "pk_invcl");
      m_htRefField
          .put("�������" + String.valueOf(-1), " from bd_invcl where 0=0 ");

      m_htRefField.put("��Ŀ������" + String.valueOf(RETURNCODE), "jobcode");
      m_htRefField.put("��Ŀ������" + String.valueOf(RETURNNAME), "jobname");
      m_htRefField.put("��Ŀ������" + String.valueOf(RETURNPK), "pk_jobmngfil");
      m_htRefField
          .put("��Ŀ������" + String.valueOf(-1),
              " from bd_jobbasfil b, bd_jobmngfil m where b.pk_jobbasfil = m.pk_jobbasfil ");
      //
      m_htRefField.put("��������" + String.valueOf(RETURNCODE), "areaclcode");
      m_htRefField.put("��������" + String.valueOf(RETURNNAME), "areaclname");
      m_htRefField.put("��������" + String.valueOf(RETURNPK), "pk_areacl");
      m_htRefField.put("��������" + String.valueOf(-1),
          " from bd_areacl where 0=0 ");

      // salecorp added 20070430 zhongwei
      m_htRefField.put("������֯" + String.valueOf(RETURNCODE), "vsalestrucode");
      m_htRefField.put("������֯" + String.valueOf(RETURNNAME), "vsalestruname");
      m_htRefField.put("������֯" + String.valueOf(RETURNPK), "csalestruid");
      m_htRefField.put("������֯" + String.valueOf(-1),
          " from bd_salestru where 0=0 ");

      // ������Ŀ֧������Ȩ�� ������
      m_htRefField.put("������Ŀ" + String.valueOf(RETURNCODE), "ccheckitemcode");
      m_htRefField.put("������Ŀ" + String.valueOf(RETURNNAME), "ccheckitemname");
      m_htRefField.put("������Ŀ" + String.valueOf(RETURNPK), "ccheckitemid");
      m_htRefField.put("������Ŀ" + String.valueOf(-1),
          " from qc_checkitem where 0=0 ");
    }

    return m_htRefField.get(sRefNodeName + String.valueOf(flag));
  }

  protected ArrayList<GeneralBillVO> qryBill(String pk_corp, String billType,
      String businessType, String operator, String billID, ConditionVO[] convos)
      throws BusinessException {

    if (billID == null || billType == null || pk_corp == null) {
      SCMEnv.out("no bill param");
      return null;
    }
    String sqrywhere = "  head.cbilltypecode='" + billType
        + "' AND head.cgeneralhid='" + billID + "' ";
    QryConditionVO voCond = new QryConditionVO(sqrywhere);

    voCond.setIntParam(0, GeneralBillVO.QRY_HEAD_ONLY_PURE);
    if (convos != null && convos.length > 0) {
      voCond.setParam(QryConditionVO.QRY_CONDITIONVO, convos);
      String swhere = convos[0].getWhereSQL(convos);
      if (swhere != null && swhere.trim().length() > 0)
        voCond.setQryCond(sqrywhere + " and " + swhere);
    }
    ArrayList<GeneralBillVO> alListData = queryBills(billType, voCond);
    return alListData;
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
  
	/**
	 * 2010-11-07 MeiChao
	 * ������ⵥȡ��ǩ��ʱ,����Ӧ���ݹ�Ӧ����,������������
	 */
	public boolean rollbackICtoAPandIA(String generalPK, String APpks,String IApks)
			throws BusinessException {
		try{
		BaseDAO dao = new BaseDAO();
		String deleteAPhead="update arap_djzb t set dr=1 where t.vouchid in ("+APpks+") and t.dr=0";
		String deleteAPbody="update arap_djfb t set dr=1 where t.vouchid in ("+APpks+") and t.dr=0";
		String deleteIAhead="update ia_bill t set t.dr=1 where t.cbillid in ("+IApks+") and t.dr=0";
		String deleteIAbody="update ia_bill_b t set t.dr=1 where t.cbillid in ("+IApks+") and t.dr=0";
		dao.executeUpdate(deleteAPhead);
		dao.executeUpdate(deleteAPbody);
		dao.executeUpdate(deleteIAhead);
		dao.executeUpdate(deleteIAbody);
		}catch (Exception e){
			Logger.debug("������ⵥȡ��ǩ��ʱ,�������ε��ݳ���!");
			e.printStackTrace();
			return false;
		}
		return true;
	}

	

  
  
  

}

