package nc.ui.dm.dm220.event.edit;

import nc.bs.ml.NCLangResOnserver;
import nc.ui.bd.b39.PhaseRefModel;
import nc.ui.dm.dm030.RouteRefModel;
import nc.ui.dm.dm040.TrancustRefMD;
import nc.ui.dm.dm050.VehicletypeRefModel;
import nc.ui.dm.dm060.VehicleRefModel;
import nc.ui.dm.dm070.DriverRefModel;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.bill.BillItem;
import nc.ui.scm.pattern.ctrl.card.CardCtrl;
import nc.ui.scm.pattern.ctrl.card.CardCtrlInfo;
import nc.ui.scm.pattern.ctrl.card.auxiliary.CardHelper;
import nc.ui.scm.pattern.event.card.body.CardBodyBeforeEditEvent;
import nc.ui.scm.pattern.event.card.head.CardHeadTailBeforeEditEvent;
import nc.ui.scm.pattern.listcard.ListCardNode;
import nc.ui.scm.pattern.tool.InvInfoTool;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pattern.data.ValueUtils;
import nc.vo.scm.pattern.model.annotation.entity.vo.UserDefineField;
import nc.vo.scm.pattern.model.meta.entity.vo.ISmartVOMeta;
import nc.vo.scm.pattern.model.meta.entity.vo.ISmartVOMetaProxy;
import nc.vo.scm.pattern.model.meta.entity.vo.SmartVOMetaFactory;
import nc.vo.scm.pattern.pub.SqlBuilder;

public class CardBeforeEditor
{
  private ListCardNode node = null;

  public CardBeforeEditor(ListCardNode node)
  {
    this.node = node;
  }

  public boolean beforeHeadDefDocEdit(CardHeadTailBeforeEditEvent event) {
    CardCtrl card = event.getCard();
    CardHelper helper = new CardHelper(card);
    String key = event.getBillItemEvent().getItem().getKey();
    BillItem valueItem = helper.getHeadTailItem(key);
    if (valueItem.getDataType() != 7) {
      return true;
    }

    CardCtrlInfo cardInfo = card.getCardInfo();
    ISmartVOMeta voMeta = cardInfo.getHeadVOMeta();
    String pkField = getPKField(voMeta, key);

    UIRefPane ref = (UIRefPane)valueItem.getComponent();

    if (ref == null) {
      return true;
    }

    ref.getUITextField().setEditable(true);

    String pk = helper.getHeadTailValue(pkField);
    if (pk == null) {
      ref.setPK(null);
    }
    else {
      ref.setPK(pk);
    }

    return true;
  }

  public boolean beforeBodyDefDocEdit(CardBodyBeforeEditEvent event) {
    String key = event.getBillEditEvent().getKey();
    int row = event.getBillEditEvent().getRow();
    CardCtrl card = event.getCard();
    CardHelper helper = new CardHelper(card);
    BillItem valueItem = helper.getBodyItem(key);
    if (valueItem.getDataType() != 7) {
      return true;
    }

    CardCtrlInfo cardInfo = card.getCardInfo();
    String tableCode = card.getBillCard().getCurrentBodyTableCode();
    ISmartVOMeta voMeta = cardInfo.getBodyVOMeta(tableCode);
    String pkField = getPKField(voMeta, key);

    UIRefPane ref = (UIRefPane)valueItem.getComponent();

    if (ref == null) {
      return true;
    }

    ref.getUITextField().setEditable(true);

    String pk = helper.getBodyRowValue(pkField, row);
    if (pk == null) {
      ref.setPK(null);
    }
    else {
      ref.setPK(pk);
    }
    return true;
  }

  private String getPKField(ISmartVOMeta meta, String valueField) {
    ISmartVOMetaProxy proxy = SmartVOMetaFactory.getInstance().get(meta);
    UserDefineField userDefine = proxy.getUserDefineField();
    String pkField = null;
    if (userDefine == null) {
      return pkField;
    }
    int length = userDefine.value_fields().length;
    int index = -1;
    for (int i = 0; i < length; ++i) {
      if (userDefine.value_fields()[i].equals(valueField)) {
        index = i;
        break;
      }
    }
    if (index > -1) {
      pkField = userDefine.pk_fields()[index];
    }
    return pkField;
  }

  public boolean beforeSendStoreOrgEdit(CardBodyBeforeEditEvent event) {
    boolean flag = canSelfEdit(event);
    if (!flag) {
      return flag;
    }
    CardCtrl card = event.getCard();

    CardHelper helper = new CardHelper(card);
    int row = event.getBillEditEvent().getRow();

    String csendcorp = helper.getBodyRowValue("csendcorp", row);
    if (csendcorp == null) {
      String message = NCLangResOnserver.getInstance().getStrByID("40142020", "UPP40142020-000127");

      this.node.getAbstractUI().showHintMessage(message);
      return false;
    }
    BillItem item = helper.getBodyItem("csendstoreorgname");
    setStoreOrgItem(item, csendcorp);
    return true;
  }

  public boolean beforeReceiveStoreOrgEdit(CardBodyBeforeEditEvent event) {
    boolean flag = canSelfEdit(event);
    if (!flag) {
      return flag;
    }
    CardCtrl card = event.getCard();

    CardHelper helper = new CardHelper(card);
    int row = event.getBillEditEvent().getRow();

    String creceivecorp = helper.getBodyRowValue("creceivecorp", row);
    if (creceivecorp == null) {
      String message = NCLangResOnserver.getInstance().getStrByID("40142020", "UPP40142020-000128");

      this.node.getAbstractUI().showHintMessage(message);
      return false;
    }
    BillItem item = helper.getBodyItem("creceivestoreorgname");
    setStoreOrgItem(item, creceivecorp);
    return true;
  }

  public boolean beforeSendStoreEdit(CardBodyBeforeEditEvent event) {
    CardCtrl card = event.getCard();

    CardHelper helper = new CardHelper(card);
    int row = event.getBillEditEvent().getRow();

    String csendcorp = helper.getBodyRowValue("csendcorp", row);
    String csendstoreorgid = helper.getBodyRowValue("csendstoreorgid", row);
    if (csendcorp == null) {
      String message = NCLangResOnserver.getInstance().getStrByID("40142020", "UPP40142020-000127");

      this.node.getAbstractUI().showHintMessage(message);
      return false;
    }
    BillItem item = helper.getBodyItem("csendstorename");
    setStoreItem(item, csendcorp, csendstoreorgid);
    return true;
  }

  public boolean beforeReceiveStoreEdit(CardBodyBeforeEditEvent event) {
    CardCtrl card = event.getCard();

    CardHelper helper = new CardHelper(card);
    int row = event.getBillEditEvent().getRow();

    String creceivecorp = helper.getBodyRowValue("creceivecorp", row);
    String creceivestoreorgid = helper.getBodyRowValue("creceivestoreorgid", row);

    if (creceivecorp == null) {
      String message = NCLangResOnserver.getInstance().getStrByID("40142020", "UPP40142020-000128");

      this.node.getAbstractUI().showHintMessage(message);
      return false;
    }
    BillItem item = helper.getBodyItem("creceivestorename");
    setStoreItem(item, creceivecorp, creceivestoreorgid);
    return true;
  }

  public boolean beforeSendVendorEdit(CardBodyBeforeEditEvent event) {
    boolean flag = canSelfEdit(event);
    return flag;
  }

  public boolean beforeReceiveCustomerEdit(CardBodyBeforeEditEvent event) {
    boolean flag = canSelfEdit(event);
    return flag;
  }

  public boolean beforeReceiveaddressEdit(CardBodyBeforeEditEvent event) {
    CardCtrl card = event.getCard();
    CardHelper helper = new CardHelper(card);
    BillItem item = helper.getBodyItem("vreceiveaddress");
    UIRefPane refpane = (UIRefPane)item.getComponent();

    refpane.setAutoCheck(false);

    boolean flag = true;
    return flag;
  }

  public boolean beforeSendaddressEdit(CardBodyBeforeEditEvent event) {
    CardCtrl card = event.getCard();
    CardHelper helper = new CardHelper(card);
    BillItem item = helper.getBodyItem("vsendaddress");
    UIRefPane refpane = (UIRefPane)item.getComponent();

    refpane.setAutoCheck(false);

    boolean flag = true;
    return flag;
  }

  public boolean beforeApCustEdit(CardHeadTailBeforeEditEvent event) {
    CardCtrl card = event.getCard();
    CardHelper helper = new CardHelper(card);
    String ctrancustid = helper.getHeadTailValue("ctrancustid");
    if (ctrancustid == null) {
      String message = NCLangResOnserver.getInstance().getStrByID("40142020", "UPP40142020-000129");

      this.node.getAbstractUI().showHintMessage(message);
      return false;
    }
    return true;
  }

  public boolean beforeTrancustEdit(CardHeadTailBeforeEditEvent event) {
    CardCtrl card = event.getCard();
    CardHelper helper = new CardHelper(card);
    String cdelivorgid = helper.getHeadTailValue("cdelivorgid");
    if (cdelivorgid == null) {
      String message = NCLangResOnserver.getInstance().getStrByID("40142020", "UPP40142020-000130");

      this.node.getAbstractUI().showHintMessage(message);
      return false;
    }
    BillItem item = helper.getHeadTailItem("ctrancustid");
    UIRefPane refpane = (UIRefPane)item.getComponent();
    TrancustRefMD ref = (TrancustRefMD)refpane.getRefModel();
    ref.setDelivOrgPK(cdelivorgid);
    return true;
  }

  public boolean beforeDriverEdit(CardHeadTailBeforeEditEvent event) {
    CardCtrl card = event.getCard();
    CardHelper helper = new CardHelper(card);
    String cdelivorgid = helper.getHeadTailValue("cdelivorgid");
    if (cdelivorgid == null) {
      String message = NCLangResOnserver.getInstance().getStrByID("40142020", "UPP40142020-000130");

      this.node.getAbstractUI().showHintMessage(message);
      return false;
    }
    BillItem item = helper.getHeadTailItem("cdriverid");
    UIRefPane refpane = (UIRefPane)item.getComponent();
    DriverRefModel ref = (DriverRefModel)refpane.getRefModel();
    ref.setDelivOrgPK(cdelivorgid);
    return true;
  }

  public boolean beforeVehicleTypeEdit(CardHeadTailBeforeEditEvent event) {
    CardCtrl card = event.getCard();
    CardHelper helper = new CardHelper(card);
    String cdelivorgid = helper.getHeadTailValue("cdelivorgid");
    if (cdelivorgid == null) {
      String message = NCLangResOnserver.getInstance().getStrByID("40142020", "UPP40142020-000130");

      this.node.getAbstractUI().showHintMessage(message);
      return false;
    }
    BillItem item = helper.getHeadTailItem("cvehicletypeid");
    UIRefPane refpane = (UIRefPane)item.getComponent();
    VehicletypeRefModel ref = (VehicletypeRefModel)refpane.getRefModel();
    ref.setDelivOrgPK(cdelivorgid);
    return true;
  }

  public boolean beforeVehicleEdit(CardHeadTailBeforeEditEvent event) {
    CardCtrl card = event.getCard();
    CardHelper helper = new CardHelper(card);
    String cdelivorgid = helper.getHeadTailValue("cdelivorgid");
    if (cdelivorgid == null) {
      String message = NCLangResOnserver.getInstance().getStrByID("40142020", "UPP40142020-000130");

      this.node.getAbstractUI().showHintMessage(message);
      return false;
    }
    BillItem item = helper.getHeadTailItem("cvehicleid");
    UIRefPane refpane = (UIRefPane)item.getComponent();
    VehicleRefModel ref = (VehicleRefModel)refpane.getRefModel();
    ref.setDelivOrgPK(cdelivorgid);
    return true;
  }

  public boolean beforeRouteEdit(CardHeadTailBeforeEditEvent event) {
    CardCtrl card = event.getCard();
    CardHelper helper = new CardHelper(card);
    String cdelivorgid = helper.getHeadTailValue("cdelivorgid");
    if (cdelivorgid == null) {
      String message = NCLangResOnserver.getInstance().getStrByID("40142020", "UPP40142020-000130");

      this.node.getAbstractUI().showHintMessage(message);
      return false;
    }
    BillItem item = helper.getHeadTailItem("crouteid");
    UIRefPane refpane = (UIRefPane)item.getComponent();
    RouteRefModel ref = (RouteRefModel)refpane.getRefModel();
    ref.setDelivOrgPK(cdelivorgid);
    return true;
  }

  public boolean beforeAssistnumEdit(CardBodyBeforeEditEvent event) {
    CardCtrl card = event.getCard();
    CardHelper helper = new CardHelper(card);
    int row = event.getBillEditEvent().getRow();

    String cinvbasid = helper.getBodyRowValue("cinvbasid", row);
    if (cinvbasid == null) {
      String message = NCLangResOnserver.getInstance().getStrByID("40142020", "UPP40142020-000125");

      this.node.getAbstractUI().showHintMessage(message);
      return false;
    }
    String[] cinvbaseids = { cinvbasid };

    boolean flag = InvInfoTool.getInstance().isAstManage(cinvbaseids)[0];
    if (!flag) {
      String message = NCLangResOnserver.getInstance().getStrByID("40142020", "UPP40142020-000126");

      this.node.getAbstractUI().showHintMessage(message);
      return false;
    }
    String castunitid = helper.getBodyRowValue("castunitid", row);
    if (castunitid == null) {
      String message = NCLangResOnserver.getInstance().getStrByID("40142020", "UPP40142020-000131");

      this.node.getAbstractUI().showHintMessage(message);
      return false;
    }
    return true;
  }

  public boolean beforeSignAssistnumEdit(CardBodyBeforeEditEvent event) {
    CardCtrl card = event.getCard();
    CardHelper helper = new CardHelper(card);
    int row = event.getBillEditEvent().getRow();

    String cinvbasid = helper.getBodyRowValue("cinvbasid", row);
    if (cinvbasid == null) {
      String message = NCLangResOnserver.getInstance().getStrByID("40142020", "UPP40142020-000125");

      this.node.getAbstractUI().showHintMessage(message);
      return false;
    }
    String[] cinvbaseids = { cinvbasid };

    boolean flag = InvInfoTool.getInstance().isAstManage(cinvbaseids)[0];
    if (!flag) {
      String message = NCLangResOnserver.getInstance().getStrByID("40142020", "UPP40142020-000126");

      this.node.getAbstractUI().showHintMessage(message);
      return false;
    }
    String castunitid = helper.getBodyRowValue("castunitid", row);
    if (castunitid == null) {
      String message = NCLangResOnserver.getInstance().getStrByID("40142020", "UPP40142020-000131");

      this.node.getAbstractUI().showHintMessage(message);
      return false;
    }
    return true;
  }

  public boolean beforeAstunitEdit(CardBodyBeforeEditEvent event) {
    CardCtrl card = event.getCard();
    CardHelper helper = new CardHelper(card);
    int row = event.getBillEditEvent().getRow();

    String cinvbasid = helper.getBodyRowValue("cinvbasid", row);
    if (cinvbasid == null) {
      String message = NCLangResOnserver.getInstance().getStrByID("40142020", "UPP40142020-000125");

      this.node.getAbstractUI().showHintMessage(message);
      return false;
    }
    String[] cinvbaseids = { cinvbasid };

    boolean flag = InvInfoTool.getInstance().isAstManage(cinvbaseids)[0];
    if (!flag) {
      String message = NCLangResOnserver.getInstance().getStrByID("40142020", "UPP40142020-000126");

      this.node.getAbstractUI().showHintMessage(message);
      return false;
    }

    BillItem item = helper.getBodyItem("castunitname");
    UIRefPane refPanel = (UIRefPane)item.getComponent();
    SqlBuilder sql = new SqlBuilder();
    String[] ids = InvInfoTool.getInstance().getMeasDocIDs(cinvbasid);
    if (ids.length == 0) {
      sql.append(" and 1<0 ");
    }
    else {
      sql.append(" and pk_measdoc", ids);
    }
    refPanel.getRefModel().addWherePart(sql.toString());
    return true;
  }

  public boolean beforeVbatchEdit(CardBodyBeforeEditEvent event)
  {
    return false;
  }

  public boolean beforeNumberEdit(CardBodyBeforeEditEvent event)
  {
    CardCtrl card = event.getCard();
    CardHelper helper = new CardHelper(card);
    int row = event.getBillEditEvent().getRow();

    String cinvbasid = helper.getBodyRowValue("cinvbasid", row);
    if (cinvbasid == null) {
      String message = NCLangResOnserver.getInstance().getStrByID("40142020", "UPP40142020-000125");

      this.node.getAbstractUI().showHintMessage(message);
      return false;
    }
    String[] cinvbaseids = { cinvbasid };

    boolean flag = InvInfoTool.getInstance().isAstManage(cinvbaseids)[0];
    if (flag) {
      String castunitid = helper.getBodyRowValue("castunitid", row);
      if (castunitid == null) {
        String message = NCLangResOnserver.getInstance().getStrByID("40142020", "UPP40142020-000131");

        this.node.getAbstractUI().showHintMessage(message);
        return false;
      }
    }
    return true;
  }

  /**
   * add by ouyangzhb 2012-12-24 增加一个辅助字段，允许输入负数
   * @param event
   * @return
   */
  public boolean beforeTmpNumberEdit(CardBodyBeforeEditEvent event)
  {
    CardCtrl card = event.getCard();
    CardHelper helper = new CardHelper(card);
    int row = event.getBillEditEvent().getRow();

    String cinvbasid = helper.getBodyRowValue("cinvbasid", row);
    if (cinvbasid == null) {
      String message = NCLangResOnserver.getInstance().getStrByID("40142020", "UPP40142020-000125");

      this.node.getAbstractUI().showHintMessage(message);
      return false;
    }
    String[] cinvbaseids = { cinvbasid };

    boolean flag = InvInfoTool.getInstance().isAstManage(cinvbaseids)[0];
    if (flag) {
      String castunitid = helper.getBodyRowValue("castunitid", row);
      if (castunitid == null) {
        String message = NCLangResOnserver.getInstance().getStrByID("40142020", "UPP40142020-000131");

        this.node.getAbstractUI().showHintMessage(message);
        return false;
      }
    }
    return true;
  }
  
  
  
  public boolean beforeChangerateEdit(CardBodyBeforeEditEvent event) {
    CardCtrl card = event.getCard();
    CardHelper helper = new CardHelper(card);
    int row = event.getBillEditEvent().getRow();

    String cinvbasid = helper.getBodyRowValue("cinvbasid", row);
    if (cinvbasid == null) {
      String message = NCLangResOnserver.getInstance().getStrByID("40142020", "UPP40142020-000125");

      this.node.getAbstractUI().showHintMessage(message);
      return false;
    }
    String[] cinvbaseids = { cinvbasid };

    boolean flag = InvInfoTool.getInstance().isAstManage(cinvbaseids)[0];
    if (!flag) {
      String message = NCLangResOnserver.getInstance().getStrByID("40142020", "UPP40142020-000126");

      this.node.getAbstractUI().showHintMessage(message);
      return false;
    }
    String castunitid = helper.getBodyRowValue("castunitid", row);
    if (castunitid == null) {
      String message = NCLangResOnserver.getInstance().getStrByID("40142020", "UPP40142020-000131");

      this.node.getAbstractUI().showHintMessage(message);
      return false;
    }
    String[] cmeasdocs = { castunitid };

    flag = InvInfoTool.getInstance().isFixedFlag(cinvbaseids, cmeasdocs)[0];
    if (flag) {
      String message = NCLangResOnserver.getInstance().getStrByID("40142020", "UPP40142020-000135");

      this.node.getAbstractUI().showHintMessage(message);
      return false;
    }
    return true;
  }

  public boolean beforeMoneyEdit(CardBodyBeforeEditEvent event) {
    CardCtrl card = event.getCard();
    CardHelper helper = new CardHelper(card);
    int row = event.getBillEditEvent().getRow();

    String value = helper.getBodyRowValue("nnumber", row);
    UFDouble nnumber = ValueUtils.getInstance().getUFDouble(value);
    if (nnumber == null) {
      String message = NCLangResOnserver.getInstance().getStrByID("40142020", "UPP40142020-000136");

      this.node.getAbstractUI().showHintMessage(message);
      return false;
    }
    return true;
  }

  public boolean beforeWeightEdit(CardBodyBeforeEditEvent event) {
    CardCtrl card = event.getCard();
    CardHelper helper = new CardHelper(card);
    int row = event.getBillEditEvent().getRow();

    String cinvbasid = helper.getBodyRowValue("cinvbasid", row);
    if (cinvbasid == null) {
      String message = NCLangResOnserver.getInstance().getStrByID("40142020", "UPP40142020-000125");

      this.node.getAbstractUI().showHintMessage(message);
      return false;
    }
    String nnumber = helper.getBodyRowValue("nnumber", row);
    if (nnumber == null) {
      String message = NCLangResOnserver.getInstance().getStrByID("40142020", "UPP40142020-000136");

      this.node.getAbstractUI().showHintMessage(message);
      return false;
    }
    return true;
  }

  public boolean beforeVolumnEdit(CardBodyBeforeEditEvent event) {
    CardCtrl card = event.getCard();
    CardHelper helper = new CardHelper(card);
    int row = event.getBillEditEvent().getRow();

    String cinvbasid = helper.getBodyRowValue("cinvbasid", row);
    if (cinvbasid == null) {
      String message = NCLangResOnserver.getInstance().getStrByID("40142020", "UPP40142020-000125");

      this.node.getAbstractUI().showHintMessage(message);
      return false;
    }
    String nnumber = helper.getBodyRowValue("nnumber", row);
    if (nnumber == null) {
      String message = NCLangResOnserver.getInstance().getStrByID("40142020", "UPP40142020-000136");

      this.node.getAbstractUI().showHintMessage(message);
      return false;
    }
    return true;
  }

  public boolean beforePriceEdit(CardBodyBeforeEditEvent event) {
    CardCtrl card = event.getCard();
    CardHelper helper = new CardHelper(card);
    int row = event.getBillEditEvent().getRow();

    String value = helper.getBodyRowValue("nnumber", row);
    UFDouble nnumber = ValueUtils.getInstance().getUFDouble(value);
    if (nnumber == null) {
      String message = NCLangResOnserver.getInstance().getStrByID("40142020", "UPP40142020-000136");

      this.node.getAbstractUI().showHintMessage(message);
      return false;
    }
    return true;
  }

  public boolean beforeProjectphaseEdit(CardBodyBeforeEditEvent event) {
    CardCtrl card = event.getCard();
    String key = event.getBillEditEvent().getKey();
    CardHelper helper = new CardHelper(card);
    int row = event.getBillEditEvent().getRow();

    String cprojectid = helper.getBodyRowValue("cprojectid", row);
    if (cprojectid == null) {
      String message = NCLangResOnserver.getInstance().getStrByID("40142020", "UPP40142020-000137");

      this.node.getAbstractUI().showHintMessage(message);
      return false;
    }
    BillItem item = helper.getBodyItem(key);
    UIRefPane ref = (UIRefPane)item.getComponent();
    ref.setRefModel(new PhaseRefModel(cprojectid));
    return true;
  }

  public boolean beforeInventoryEdit(CardBodyBeforeEditEvent event) {
    boolean flag = checkSource(event);
    if (!flag) {
      return flag;
    }
    CardCtrl card = event.getCard();
    String key = event.getBillEditEvent().getKey();
    CardHelper helper = new CardHelper(card);
    BillItem item = helper.getBodyItem(key);
    UIRefPane ref = (UIRefPane)item.getComponent();
    SqlBuilder sql = new SqlBuilder();
    sql.append(" and bd_invbasdoc.discountflag = 'N' ");
    sql.append(" and bd_invbasdoc.laborflag = 'N' ");
    ref.getRefModel().addWherePart(sql.toString());
    return flag;
  }

  public boolean beforeSendcorpEdit(CardBodyBeforeEditEvent event) {
    boolean flag = canSelfEdit(event);
    return flag;
  }

  public boolean beforeReceivecorpEdit(CardBodyBeforeEditEvent event) {
    boolean flag = canSelfEdit(event);
    return flag;
  }

  private boolean checkSource(CardBodyBeforeEditEvent event) {
    CardCtrl card = event.getCard();
    CardHelper helper = new CardHelper(card);
    int row = event.getBillEditEvent().getRow();
    String csourcebillitemid = helper.getBodyRowValue("csourcebillitemid", row);

    boolean flag = false;
    if (csourcebillitemid == null) {
      flag = true;
    }
    if (!flag) {
      String message = NCLangResOnserver.getInstance().getStrByID("40142020", "UPP40142020-000138");

      this.node.getAbstractUI().showHintMessage(message);
    }
    return flag;
  }

  private boolean canSelfEdit(CardBodyBeforeEditEvent event) {
    CardCtrl card = event.getCard();
    String table = card.getBillCard().getCurrentBodyTableCode();
    CardCtrlInfo cardInfo = card.getCardInfo();
    String baseTable = cardInfo.getBaseTab(table);
    if (baseTable.equals("dm_packbill")) {
      return true;
    }
    boolean flag = checkSource(event);
    return flag;
  }

  private void setStoreOrgItem(BillItem item, String pk_corp) {
    UIRefPane ref = (UIRefPane)item.getComponent();

    int[] properties = { 0, 1 };

    SqlBuilder sql = new SqlBuilder();
    sql.append(" and property", properties);
    ref.getRefModel().addWherePart(sql.toString());
    ref.setPK(pk_corp);
    ref.getRefModel().setPk_corp(pk_corp);
  }

  private void setStoreItem(BillItem item, String pk_corp, String storeorgid) {
    UIRefPane ref = (UIRefPane)item.getComponent();
    int[] properties = { 0, 1 };

    SqlBuilder sql = new SqlBuilder();
    sql.append(" and isdirectstore = 'N' and gubflag='N' and sealflag='N' ");
    sql.append(" and pk_calbody in ");
    sql.startParentheses();
    sql.append(" select pk_calbody from ");

    sql.append(" bd_calbody  where ");
    sql.append(" sealflag is null or sealflag ='N' ");
    sql.append(" and property", properties);
    sql.append(" and pk_corp", pk_corp);
    sql.endParentheses();
    if (storeorgid != null) {
      sql.append(" and pk_calbody", storeorgid);
    }

    ref.getRefModel().addWherePart(sql.toString());

    ref.setPK(pk_corp);
    ref.getRefModel().setPk_corp(pk_corp);
  }
}