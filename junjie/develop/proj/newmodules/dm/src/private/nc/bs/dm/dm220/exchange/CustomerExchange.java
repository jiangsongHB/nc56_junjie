package nc.bs.dm.dm220.exchange;

import java.util.HashMap;
import java.util.Map;

import nc.bs.dm.pub.context.DMContext;
import nc.bs.ml.NCLangResOnserver;
import nc.bs.scm.pattern.data.DataAccessUtils;
import nc.bs.scm.pattern.data.bill.action.IBillProcess;

import nc.vo.dm.model.delivbill.entity.DelivBillItemVO;
import nc.vo.dm.model.delivbill.entity.DelivBillVO;
import nc.vo.scm.pattern.data.IRowSet;
import nc.vo.scm.pattern.domain.uap.enumeration.NCBillType;
import nc.vo.scm.pattern.exception.ExceptionUtils;
import nc.vo.scm.pattern.pub.SqlBuilder;

/**
 * 客户多公司转换
 * @author 钟鸣
 *
 * 2008-11-11 上午09:50:00
 */
public class CustomerExchange implements IBillProcess<DelivBillVO> {
  private DMContext context = null;

  //对方公司客户管理档案到基本档案的映射
  private Map<String, String> manToBasIndex = new HashMap<String, String>();

  //客户基本档案到当前公司管理档案的映射
  private Map<String, String> basToManIndex = new HashMap<String, String>();

  //公司对应的客户基本档案的映射
  private Map<String, String> corpBasIndex = new HashMap<String, String>();

  //采购订单ID对应的公司主键
  private Map<String, String> billCorpIndex = new HashMap<String, String>();

  public CustomerExchange(
      DMContext context) {
    this.context = context;
  }

  public void accept(DelivBillVO bill) {
    DelivBillItemVO[] items = bill.getInvBodys();
    for (DelivBillItemVO item : items) {
      this.processReceiveCustDoc(item);
      this.processCosignDoc(item);
      this.processTakeFeeDoc(item);
    }
  }

  public void process() {
  }

  private void processReceiveCustDoc(DelivBillItemVO item) {
    String creceivecustid = item.getCreceivecustid();
    if (creceivecustid == null) {
      return;
    }
    String pk_cubasdoc = this.getBasDoc(creceivecustid);
    item.setCreceivecustbasid(pk_cubasdoc);
    if (pk_cubasdoc == null) {
      return;
    }
    String pk_cumandoc = this.getManDoc(pk_cubasdoc);
    item.setCreceivecustid(pk_cumandoc);
  }

  private void processCosignDoc(DelivBillItemVO item) {
    NCBillType csourcebilltypecode = item.getCsourcebilltypecode();
    String pk_cubasdoc = null;
    if (csourcebilltypecode.equals(NCBillType.SO_4331)) {
      String pk_corp = item.getCsendcorp();
      pk_cubasdoc = this.getBasDocForCorp(pk_corp);
    }
    else if (csourcebilltypecode.equals(NCBillType.IC_4C)) {
      String pk_corp = item.getCsendcorp();
      pk_cubasdoc = this.getBasDocForCorp(pk_corp);
    }
    else if (csourcebilltypecode.equals(NCBillType.IC_4Y)) {
      String pk_corp = item.getCsendcorp();
      pk_cubasdoc = this.getBasDocForCorp(pk_corp);
    }
    else if (csourcebilltypecode.equals(NCBillType.PU_21)) {
      String pk_corp = this.getPuOrderCorp(item.getCsourcebillid());
      pk_cubasdoc = this.getBasDocForCorp(pk_corp);
    }
    else if (csourcebilltypecode.equals(NCBillType.DM_4802)) {
      return;
      //lumzh 增加类型为4A的情况2012-09-25
    }else if(csourcebilltypecode.equals(NCBillType.IC_4A)){
    	 String pk_corp = item.getCsendcorp();
         pk_cubasdoc = this.getBasDocForCorp(pk_corp);
    }
    else {
      ExceptionUtils.getInstance().unSupported();
    }
    item.setCcosignbasid(pk_cubasdoc);
    if (pk_cubasdoc == null) {
      return;
    }
    String pk_cumandoc = this.getManDoc(pk_cubasdoc);
    item.setCcosignid(pk_cumandoc);
  }

  private void processTakeFeeDoc(DelivBillItemVO item) {
    NCBillType csourcebilltypecode = item.getCsourcebilltypecode();
    String pk_cubasdoc = null;
    if (csourcebilltypecode.equals(NCBillType.SO_4331)) {
      String pk_corp = item.getCsendcorp();
      pk_cubasdoc = this.getBasDocForCorp(pk_corp);
    }
    else if (csourcebilltypecode.equals(NCBillType.IC_4C)) {
      String pk_corp = item.getCsendcorp();
      pk_cubasdoc = this.getBasDocForCorp(pk_corp);
    }
    else if (csourcebilltypecode.equals(NCBillType.IC_4Y)) {
      String pk_corp = item.getCsendcorp();
      pk_cubasdoc = this.getBasDocForCorp(pk_corp);
    }
    else if (csourcebilltypecode.equals(NCBillType.PU_21)) {
      String pk_corp = this.getPuOrderCorp(item.getCsourcebillid());
      pk_cubasdoc = this.getBasDocForCorp(pk_corp);
    }
    else if (csourcebilltypecode.equals(NCBillType.DM_4802)) {
      return;
    }
    //lumzh 增加类型为4A的情况2012-09-25
    else if(csourcebilltypecode.equals(NCBillType.IC_4A)){
   	 String pk_corp = item.getCsendcorp();
        pk_cubasdoc = this.getBasDocForCorp(pk_corp);
   }
    else {
      ExceptionUtils.getInstance().unSupported();
    }
    item.setCtakefeebasid(pk_cubasdoc);
    if (pk_cubasdoc == null) {
      return;
    }
    String pk_cumandoc = this.getManDoc(pk_cubasdoc);
    item.setCtakefeeid(pk_cumandoc);
  }

  private String getBasDoc(String pk_cumandoc) {
    String pk_cubasdoc = null;
    if (this.manToBasIndex.containsKey(pk_cumandoc)) {
      pk_cubasdoc = this.manToBasIndex.get(pk_cumandoc);
    }
    else {
      pk_cubasdoc = this.loadBasDoc(pk_cumandoc);
      this.manToBasIndex.put(pk_cumandoc, pk_cubasdoc);
    }
    return pk_cubasdoc;
  }

  private String loadBasDoc(String pk_cumandoc) {
    String pk_cubasdoc = null;
    SqlBuilder sql = new SqlBuilder();
    sql.append(" select pk_cubasdoc from bd_cumandoc where ");
    sql.append(" pk_cumandoc", pk_cumandoc);
    sql.append(" and ");
    sql.startParentheses();
    sql.append(" dr=0 or dr is null ");
    sql.endParentheses();
    IRowSet rowset = DataAccessUtils.getInstance().query(sql.toString());
    if (rowset.next()) {
      pk_cubasdoc = rowset.getString(0);
    }
    if (pk_cubasdoc == null) {
      String message = NCLangResOnserver.getInstance().getStrByID("40142020",
          "UPP40142020-000017")/*@res"当前客户管理档案【"*/
          + pk_cumandoc
          + NCLangResOnserver.getInstance().getStrByID("40142020",
              "UPP40142020-000018")/*@res"】没有对应的基本档案"*/;
      ExceptionUtils.getInstance().wrappBusinessException(message);
    }
    return pk_cubasdoc;
  }

  private String getManDoc(String pk_cubasdoc) {
    String pk_cumandoc = null;
    if (this.basToManIndex.containsKey(pk_cubasdoc)) {
      pk_cumandoc = this.basToManIndex.get(pk_cubasdoc);
    }
    else {
      pk_cumandoc = this.loadManDoc(pk_cubasdoc);
      this.basToManIndex.put(pk_cubasdoc, pk_cumandoc);
    }
    return pk_cumandoc;
  }

  private String loadManDoc(String pk_cubasdoc) {
    String pk_corp = this.context.getContext().getPk_corp();
    String pk_cumandoc = null;
    SqlBuilder sql = new SqlBuilder();
    sql.append(" select pk_cumandoc from bd_cumandoc where ");
    sql.append(" pk_cubasdoc", pk_cubasdoc);
    sql.append(" and ");
    sql.startParentheses();
    sql.append(" dr=0 or dr is null ");
    sql.endParentheses();
    sql.append(" and pk_corp", pk_corp);
    sql.append(" and custflag in('0','2')");

    IRowSet rowset = DataAccessUtils.getInstance().query(sql.toString());
    if (rowset.next()) {
      pk_cumandoc = rowset.getString(0);
    }
    if (pk_cumandoc == null) {
      String corpInfo = this.getCorpInfo(pk_corp);
      String custInfo = this.getCustInfo(pk_cubasdoc);
      String message = NCLangResOnserver.getInstance().getStrByID("40142020",
          "UPP40142020-000019")/*@res""客户基本档案【"*/
          + custInfo
          + NCLangResOnserver.getInstance().getStrByID("40142020",
              "UPP40142020-000020")/*@res""】没有分配到当前公司【""*/
          + corpInfo + "】";
      ExceptionUtils.getInstance().wrappBusinessException(message);
    }
    return pk_cumandoc;
  }

  private String getCorpInfo(String pk_corp) {
    SqlBuilder sql = new SqlBuilder();
    sql.append(" select unitcode,unitname from bd_corp where pk_corp", pk_corp);
    IRowSet rowset = DataAccessUtils.getInstance().query(sql.toString());
    StringBuffer buffer = new StringBuffer();
    if (rowset.next()) {
      String unitcode = rowset.getString(0);
      String unitname = rowset.getString(1);
      buffer.append(unitname);
      buffer.append("-");
      buffer.append(unitcode);
    }
    return buffer.toString();
  }

  private String getCustInfo(String pk_cubasdoc) {
    SqlBuilder sql = new SqlBuilder();
    sql.append(" select custcode,custname from bd_cubasdoc where ");
    sql.append(" pk_cubasdoc", pk_cubasdoc);
    IRowSet rowset = DataAccessUtils.getInstance().query(sql.toString());
    StringBuffer buffer = new StringBuffer();
    if (rowset.next()) {
      String custcode = rowset.getString(0);
      String custname = rowset.getString(1);
      buffer.append(custname);
      buffer.append("-");
      buffer.append(custcode);
    }
    return buffer.toString();
  }

  private String getBasDocForCorp(String pk_corp) {
    String pk_cubasdoc = null;
    if (this.corpBasIndex.containsKey(pk_corp)) {
      pk_cubasdoc = this.corpBasIndex.get(pk_corp);
    }
    else {
      pk_cubasdoc = this.loadBasDocForCorp(pk_corp);
      this.corpBasIndex.put(pk_corp, pk_cubasdoc);
    }
    return pk_cubasdoc;
  }

  private String loadBasDocForCorp(String pk_corp) {
    String pk_cubasdoc = null;
    SqlBuilder sql = new SqlBuilder();
    sql.append(" select pk_cubasdoc from bd_cubasdoc where ");
    sql.append(" pk_corp1", pk_corp);
    sql.append(" and ");
    sql.startParentheses();
    sql.append(" dr=0 or dr is null ");
    sql.endParentheses();
    IRowSet rowset = DataAccessUtils.getInstance().query(sql.toString());
    if (rowset.next()) {
      pk_cubasdoc = rowset.getString(0);
    }
    return pk_cubasdoc;
  }

  private String getPuOrderCorp(String csourcebillid) {
    String pk_corp = null;
    if (this.billCorpIndex.containsKey(csourcebillid)) {
      pk_corp = this.billCorpIndex.get(csourcebillid);
    }
    else {
      SqlBuilder sql = new SqlBuilder();
      sql.append(" select pk_corp from po_order where ");
      sql.append(" corderid", csourcebillid);
      sql.append(" and dr=0 ");
      IRowSet rowset = DataAccessUtils.getInstance().query(sql.toString());
      if (rowset.next()) {
        pk_corp = rowset.getString(0);
      }
      this.billCorpIndex.put(csourcebillid, pk_corp);
    }
    return pk_corp;
  }
}
