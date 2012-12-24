package nc.ui.dm.dm220.event.edit;

import nc.ui.dm.dm040.TrancustRefMD;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.bill.BillItem;
import nc.ui.scm.pattern.context.IUIContext;
import nc.ui.scm.pattern.ctrl.card.CardCtrl;
import nc.ui.scm.pattern.ctrl.card.CardCtrlInfo;
import nc.ui.scm.pattern.ctrl.card.auxiliary.CardHelper;
import nc.ui.scm.pattern.event.card.body.CardBodyAfterEditEvent;
import nc.ui.scm.pattern.event.card.head.CardHeadTailAfterEditEvent;
import nc.ui.scm.pattern.listcard.ListCardNode;
import nc.ui.scm.pattern.tool.InvInfoTool;
import nc.ui.scm.pub.cache.CacheTool;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pattern.constant.pub.ValueConstant;
import nc.vo.scm.pattern.context.IPrecision;
import nc.vo.scm.pattern.data.ValueUtils;
import nc.vo.scm.pattern.exception.ExceptionUtils;
import nc.vo.scm.pattern.model.annotation.entity.vo.UserDefineField;
import nc.vo.scm.pattern.model.meta.entity.vo.ISmartVOMeta;
import nc.vo.scm.pattern.model.meta.entity.vo.ISmartVOMetaProxy;
import nc.vo.scm.pattern.model.meta.entity.vo.SmartVOMetaFactory;
import nc.vo.scm.pattern.pub.SCMTool;

public class CardAfterEditor
{
  private ListCardNode node = null;

  public CardAfterEditor(ListCardNode node)
  {
    this.node = node;
  }

  public void afterAstNumEdit(CardBodyAfterEditEvent event) {
    CardCtrl card = event.getCard();
    int row = event.getBillEditEvent().getRow();
    boolean flag = calculateNumChgRateByAstNum(card, row);
    if (flag)
      changeNumber(card, row);
  }

  public void afterAstunitEdit(CardBodyAfterEditEvent event)
  {
    CardCtrl card = event.getCard();
    int row = event.getBillEditEvent().getRow();
    boolean flag = calculateNumChgRateAstNumByAstUnit(card, row);
    if (flag)
      changeNumber(card, row);
  }

  public void afterNumberEdit(CardBodyAfterEditEvent event)
  {
    CardCtrl card = event.getCard();
    int row = event.getBillEditEvent().getRow();
    changeNumber(card, row);
    calculateAstNumChgRateByNum(card, row);
  }

  public void afterChangeRateEdit(CardBodyAfterEditEvent event) {
    CardCtrl card = event.getCard();
    int row = event.getBillEditEvent().getRow();
    boolean flag = calculateNumByChgRate(card, row);
    if (flag)
      changeNumber(card, row);
  }

  public void afterMoneyEdit(CardBodyAfterEditEvent event)
  {
    CardCtrl card = event.getCard();
    IUIContext context = card.getContext();
    String pk_corp = context.getPk_corp();
    IPrecision precision = context.getPrecision(pk_corp);

    CardHelper helper = new CardHelper(card);
    int row = event.getBillEditEvent().getRow();

    String value = helper.getBodyRowValue("nnumber", row);
    UFDouble nnumber = ValueUtils.getInstance().getUFDouble(value);

    value = helper.getBodyRowValue("nmoney", row);
    UFDouble nmoney = ValueUtils.getInstance().getUFDouble(value);

    if (nmoney == null) {
      helper.setBodyValueAt("nprice", null, row);
    }
    else {
      UFDouble nprice = nmoney.div(nnumber);
      nprice = precision.adjustPriceBusiness(nprice);
      helper.setBodyValueAt("nprice", nprice, row);
    }
  }

  public void afterPriceEdit(CardBodyAfterEditEvent event) {
    CardCtrl card = event.getCard();
    IUIContext context = card.getContext();
    String pk_corp = context.getPk_corp();
    IPrecision precision = context.getPrecision(pk_corp);

    CardHelper helper = new CardHelper(card);
    int row = event.getBillEditEvent().getRow();

    String value = helper.getBodyRowValue("nnumber", row);
    UFDouble nnumber = ValueUtils.getInstance().getUFDouble(value);

    value = helper.getBodyRowValue("nprice", row);
    UFDouble nprice = ValueUtils.getInstance().getUFDouble(value);

    if (nprice == null) {
      helper.setBodyValueAt("nmoney", null, row);
    }
    else {
      UFDouble nmoney = nprice.multiply(nnumber);
      nmoney = precision.adjustMoneyBusiness(nmoney);
      helper.setBodyValueAt("nmoney", nmoney, row);
    }
  }

  public void afterHeadDefDocEdit(CardHeadTailAfterEditEvent event) {
    String key = event.getBillEditEvent().getKey();
    CardCtrl card = event.getCard();
    CardCtrlInfo cardInfo = card.getCardInfo();
    ISmartVOMeta voMeta = cardInfo.getHeadVOMeta();
    String pkField = getPKField(voMeta, key);
    setDefDocPKForHead(event, pkField, key);
  }

  public void afterBodyDefDocEdit(CardBodyAfterEditEvent event) {
    String key = event.getBillEditEvent().getKey();
    CardCtrl card = event.getCard();
    CardCtrlInfo cardInfo = card.getCardInfo();
    String tableCode = card.getBillCard().getCurrentBodyTableCode();
    ISmartVOMeta voMeta = cardInfo.getBodyVOMeta(tableCode);
    String pkField = getPKField(voMeta, key);
    setDefDocPKForBody(event, pkField, key);
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

  private void setDefDocPKForHead(CardHeadTailAfterEditEvent event, String pkField, String valueField)
  {
    CardCtrl card = event.getCard();
    CardHelper helper = new CardHelper(card);

    BillItem valueItem = helper.getHeadTailItem(valueField);
    BillItem pkItem = helper.getHeadTailItem(pkField);
    if ((valueItem == null) || (pkItem == null)) {
      return;
    }
    if (valueItem.getDataType() != 7) {
      helper.setHeadTailValue(pkField, null);
      return;
    }
    UIRefPane ref = (UIRefPane)valueItem.getComponent();
    String pk_defdoc = ref.getRefPK();
    helper.setHeadTailValue(pkField, pk_defdoc);
  }

  private void setDefDocPKForBody(CardBodyAfterEditEvent event, String pkField, String valueField)
  {
    CardCtrl card = event.getCard();
    CardHelper helper = new CardHelper(card);
    int row = event.getBillEditEvent().getRow();

    BillItem valueItem = helper.getBodyItem(valueField);
    BillItem pkItem = helper.getBodyItem(pkField);
    if ((valueItem == null) || (pkItem == null)) {
      return;
    }
    if (valueItem.getDataType() != 7) {
      helper.setBodyValueAt(pkField, null, row);
      return;
    }
    UIRefPane ref = (UIRefPane)valueItem.getComponent();
    String pk_defdoc = ref.getRefPK();
    helper.setBodyValueAt(pkField, pk_defdoc, row);
  }

  public void afterProjectEdit(CardBodyAfterEditEvent event) {
    CardCtrl card = event.getCard();
    CardHelper helper = new CardHelper(card);
    int row = event.getBillEditEvent().getRow();

    Object value = event.getBillEditEvent().getValue();
    String cprojectname = ValueUtils.getInstance().getString(value);

    if (SCMTool.getInstance().isNull(cprojectname)) {
      helper.setBodyValueAt("cprojectphasename", null, row);
      helper.setBodyValueAt("cprojectphase", null, row);
    }
  }

  public void afterSendStoreOrgEdit(CardBodyAfterEditEvent event) {
    CardCtrl card = event.getCard();
    CardHelper helper = new CardHelper(card);
    int row = event.getBillEditEvent().getRow();
    helper.setBodyValueAt("csendstoreid", null, row);
    helper.setBodyValueAt("csendstorename", null, row);
    String csendstoreorgid = helper.getBodyRowValue("csendstoreorgid", row);
    if (csendstoreorgid != null) {
      setDefaultSendAdress(event);
      setDefaultSendArea(event);
      setDefaultSendDetailAdress(event);
    }
  }

  public void afterReceiveStoreOrgEdit(CardBodyAfterEditEvent event) {
    CardCtrl card = event.getCard();
    CardHelper helper = new CardHelper(card);
    int row = event.getBillEditEvent().getRow();
    helper.setBodyValueAt("creceivestoreid", null, row);
    helper.setBodyValueAt("creceivestorename", null, row);
  }

  public void afterSendStoreEdit(CardBodyAfterEditEvent event) {
    CardCtrl card = event.getCard();
    CardHelper helper = new CardHelper(card);
    int row = event.getBillEditEvent().getRow();
    String csendstoreid = helper.getBodyRowValue("csendstoreid", row);
    String csendstoreorgid = helper.getBodyRowValue("csendstoreorgid", row);

    if (csendstoreorgid == null) {
      String[] formulas = { "csendstoreorgid->getColValue(bd_stordoc,pk_calbody,pk_stordoc,csendstoreid)", "csendstoreorgname->getColValue(bd_calbody,bodyname,pk_calbody,csendstoreorgid)" };

      helper.execBodyFormulas(row, formulas);
    }
    if (csendstoreid != null) {
      setDefaultSendAdress(event);
      setDefaultSendArea(event);
      setDefaultSendDetailAdress(event);
    }
  }

  public void afterReceiveStoreEdit(CardBodyAfterEditEvent event) {
    CardCtrl card = event.getCard();
    CardHelper helper = new CardHelper(card);
    int row = event.getBillEditEvent().getRow();
    String creceivestoreorgid = helper.getBodyRowValue("creceivestoreorgid", row);

    if (creceivestoreorgid == null) {
      String[] formulas = { "creceivestoreorgid->getColValue(bd_stordoc,pk_calbody,pk_stordoc,creceivestoreid)", "creceivestoreorgname->getColValue(bd_calbody,bodyname,pk_calbody,creceivestoreorgid)" };

      helper.execBodyFormulas(row, formulas);
    }
  }

  public void afterSendCorpEdit(CardBodyAfterEditEvent event) {
    CardCtrl card = event.getCard();
    CardHelper helper = new CardHelper(card);
    int row = event.getBillEditEvent().getRow();
    helper.setBodyValueAt("csendstoreid", null, row);
    helper.setBodyValueAt("csendstorename", null, row);
    helper.setBodyValueAt("csendstoreorgid", null, row);
    helper.setBodyValueAt("csendstoreorgname", null, row);
  }

  public void afterReceiveCorpEdit(CardBodyAfterEditEvent event) {
    CardCtrl card = event.getCard();
    CardHelper helper = new CardHelper(card);
    int row = event.getBillEditEvent().getRow();
    helper.setBodyValueAt("creceivestoreid", null, row);
    helper.setBodyValueAt("creceivestorename", null, row);
    helper.setBodyValueAt("creceivestoreorgid", null, row);
    helper.setBodyValueAt("creceivestoreorgname", null, row);
  }

  public void afterTrancustEdit(CardHeadTailAfterEditEvent event) {
    CardCtrl card = event.getCard();
    CardHelper helper = new CardHelper(card);

    String ctrancustid = helper.getHeadTailValue("ctrancustid");

    if (ctrancustid == null) {
      helper.setHeadTailValue("capcustbasid", null);
      helper.setHeadTailValue("capcustname", null);
      helper.setHeadTailValue("capcustid", null);
      return;
    }

    UIRefPane p = (UIRefPane)card.getBillCard().getHeadItem("ctrancustid").getComponent();
    if ((p == null) || (p.getRefModel() == null)) {
      return;
    }
    TrancustRefMD refModel = (TrancustRefMD)p.getRefModel();
    helper.setHeadTailValue("capcustid", refModel.getValue("isnull(bd_cumandoc.pk_cusmandoc2, bd_cumandoc.pk_cumandoc) capcustid"));
    helper.setHeadTailValue("capcustbasid", refModel.getValue("isnull(apbas.pk_cubasdoc, bd_cubasdoc.pk_cubasdoc) capcustbasid"));
    helper.setHeadTailValue("capcustname", refModel.getValue("isnull(apbas.custname, bd_cubasdoc.custname) capcustname"));
  }

  public void afterInventoryEdit(CardBodyAfterEditEvent event) {
    CardCtrl card = event.getCard();
    int row = event.getBillEditEvent().getRow();
    CardHelper helper = new CardHelper(card);
    card.getBillCard().getBodyTabbedPane().setSelectedIndex(0);

    BillItem bt = helper.getBodyItem("cinventorycode");
    UIRefPane ref = (UIRefPane)bt.getComponent();
    String[] cinventoryids = ref.getRefPKs();

    if ((cinventoryids == null) || (cinventoryids.length == 0)) {
      cinventoryids = new String[] { helper.getBodyRowValue("cinventoryid", row) };
    }
    if ((cinventoryids == null) || (cinventoryids.length == 0))
    {
      card.getBillCard().getBillModel().setNeedCalculate(false);
      clearInvInfo(card, row);

      card.getBillCard().getBillModel().setNeedCalculate(true);
      return;
    }
    addMultiInvInfo(event, cinventoryids);
  }

  private void addMultiInvInfo(CardBodyAfterEditEvent event, String[] cinventoryids)
  {
    CardCtrl card = event.getCard();

    card.getBillCard().getBillModel().setNeedCalculate(false);

    int row = event.getBillEditEvent().getRow();
    CardHelper helper = new CardHelper(card);
    boolean isLastRow = false;
    if (row == helper.getRowCount() - 1) {
      isLastRow = true;
    }
    else
    {
      card.getBillCard().getBillTable().setRowSelectionInterval(row + 1, row + 1);
    }

    int length = cinventoryids.length;
    for (int i = 0; i < length - 1; ++i) {
      if (isLastRow) {
        helper.addLine();
      }
      else {
        helper.insertLine();
      }
    }

    String cinventoryid = helper.getBodyRowValue("cinventoryid", row);

    if (cinventoryids.length == 1) {
      clearInvInfo(card, row);
    }
    else if ((cinventoryid != null) && (!cinventoryid.equals(cinventoryids[0]))) {
      clearInvInfo(card, row);
    }
    setInvInfo(card, row, cinventoryids);

    card.getBillCard().getBillModel().setNeedCalculate(true);
  }

  private void clearInvInfo(CardCtrl card, int row) {
    CardHelper helper = new CardHelper(card);
    helper.setBodyValueAt("cinventorycode", null, row);
    helper.setBodyValueAt("cinventoryname", null, row);
    helper.setBodyValueAt("cinventoryspec", null, row);
    helper.setBodyValueAt("cinventorytype", null, row);
    helper.setBodyValueAt("cinvbasid", null, row);
    helper.setBodyValueAt("cmeasname", null, row);
    helper.setBodyValueAt("cmeasid", null, row);
    helper.setBodyValueAt("nnumber", null, row);
    helper.setBodyValueAt("castunitid", null, row);
    helper.setBodyValueAt("castunitname", null, row);
    helper.setBodyValueAt("nassistnum", null, row);
    helper.setBodyValueAt("nchangerate", null, row);
    helper.setBodyValueAt("vbatch", null, row);
    helper.setBodyValueAt("nmoney", null, row);
    helper.setBodyValueAt("nprice", null, row);
    helper.setBodyValueAt("nvolumn", null, row);
    helper.setBodyValueAt("nweight", null, row);
    helper.setBodyValueAt("vfree0", null, row);
    helper.setBodyValueAt("vfree1", null, row);
    helper.setBodyValueAt("vfree2", null, row);
    helper.setBodyValueAt("vfree3", null, row);
    helper.setBodyValueAt("vfree4", null, row);
    helper.setBodyValueAt("vfree5", null, row);
    helper.setBodyValueAt("cfreeid1", null, row);
    helper.setBodyValueAt("cfreeid2", null, row);
    helper.setBodyValueAt("cfreeid3", null, row);
    helper.setBodyValueAt("cfreeid4", null, row);
    helper.setBodyValueAt("cfreeid5", null, row);
  }

  private void setInvInfo(CardCtrl card, int startIndex, String[] cinventoryids) {
    String[] cinvbasids = InvInfoTool.getInstance().getInvBaseDoc(cinventoryids);

    String[][] invInfos = InvInfoTool.getInstance().getInvInfo(cinvbasids);
    CardHelper helper = new CardHelper(card);

    int length = cinventoryids.length;
    for (int i = 0; i < length; ++i) {
      int row = startIndex + i;
      helper.setBodyValueAt("cinventoryid", cinventoryids[i], row);
      helper.setBodyValueAt("cinvbasid", invInfos[i][0], row);
      helper.setBodyValueAt("cinventorycode", invInfos[i][1], row);
      helper.setBodyValueAt("cinventoryname", invInfos[i][2], row);
      helper.setBodyValueAt("cinventoryspec", invInfos[i][3], row);
      helper.setBodyValueAt("cinventorytype", invInfos[i][4], row);
      helper.setBodyValueAt("cmeasid", invInfos[i][5], row);
      setAstInfo(helper, row, invInfos[i]);
    }
  }

  private void setAstInfo(CardHelper helper, int row, String[] invInfos) {
    String cmeasid = helper.getBodyRowValue("cmeasid", row);
    if (cmeasid != null) {
      String[] measdocids = { cmeasid };

      String cmeasname = InvInfoTool.getInstance().getMeasDocName(measdocids)[0];
      helper.setBodyValueAt("cmeasname", cmeasname, row);
    }
    String castunitid = helper.getBodyRowValue("castunitid", row);

    if (castunitid == null) {
      return;
    }
    String[] castunitids = { castunitid };

    String castunitname = InvInfoTool.getInstance().getMeasDocName(castunitids)[0];
    helper.setBodyValueAt("castunitname", castunitname, row);

    String cinvbasid = helper.getBodyRowValue("cinvbasid", row);
    String[] cinvbasids = { cinvbasid };

    UFDouble nchangerate = InvInfoTool.getInstance().getChangeRate(cinvbasids, castunitids)[0];

    helper.setBodyValueAt("nchangerate", nchangerate, row);
  }

  public void afterSendVendorEdit(CardBodyAfterEditEvent event) {
  }

  public void afterReceiveCustomerEdit(CardBodyAfterEditEvent event) {
  }

  public void afterCosignerEdit(CardBodyAfterEditEvent event) {
  }

  public void afterTakeFeeEdit(CardBodyAfterEditEvent event) {
  }

  public void afterReceiveAddressEdit(CardBodyAfterEditEvent event) {
    CardCtrl card = event.getCard();
    CardHelper helper = new CardHelper(card);
    int row = event.getBillEditEvent().getRow();
    String creceiveaddrid = helper.getBodyRowValue("creceiveaddrid", row);
    if (creceiveaddrid == null) {
      return;
    }
    String[] formulas = { "creceiveareaid->getColValue(bd_address,pk_areacl,pk_address,creceiveaddrid)", "creceiveareaname->getColValue(bd_areacl,areaclname,pk_areacl,creceiveareaid)" };

    new CardHelper(card).execBodyFormulas(row, formulas);
  }

  public void afterSendAddressEdit(CardBodyAfterEditEvent event) {
    CardCtrl card = event.getCard();
    CardHelper helper = new CardHelper(card);
    int row = event.getBillEditEvent().getRow();
    String csendaddrid = helper.getBodyRowValue("csendaddrid", row);
    if (csendaddrid == null) {
      return;
    }
    String[] formulas = { "csendareaid->getColValue(bd_address,pk_areacl,pk_address,csendaddrid)", "csendareaname->getColValue(bd_areacl,areaclname,pk_areacl,csendareaid)" };

    helper.execBodyFormulas(row, formulas);
  }

  public void afterReceiveAreaEdit(CardBodyAfterEditEvent event)
  {
  }

  public void afterSendAreaEdit(CardBodyAfterEditEvent event)
  {
  }

  private void setDefaultSendAdress(CardBodyAfterEditEvent event)
  {
    CardCtrl card = event.getCard();
    CardHelper helper = new CardHelper(card);
    int row = event.getBillEditEvent().getRow();
    String csendaddrid = helper.getBodyRowValue("csendaddrid", row);
    if (csendaddrid != null) {
      return;
    }
    String csendstoreid = helper.getBodyRowValue("csendstoreid", row);
    if (csendstoreid != null) {
      String[] formulas = { "csendaddrid->getColValue(bd_stordoc,pk_address,pk_stordoc,csendstoreid)", "csendaddrname->getColValue(bd_address,addrname,pk_address,csendaddrid)" };

      helper.execBodyFormulas(row, formulas);
      boolean flag = checkSendAdressValid(helper, row);
      if (flag) {
        return;
      }
    }
    String csendstoreorgid = helper.getBodyRowValue("csendstoreorgid", row);
    if (csendstoreorgid != null) {
      String[] formulas = { "csendaddrid->getColValue(bd_calbody,pk_address,pk_calbody,csendstoreorgid)", "csendaddrname->getColValue(bd_address,addrname,pk_address,csendaddrid)" };

      helper.execBodyFormulas(row, formulas);
    }
    checkSendAdressValid(helper, row);
  }

  private boolean checkSendAdressValid(CardHelper helper, int row) {
    String csendaddrid = helper.getBodyRowValue("csendaddrid", row);
    if (csendaddrid == null) {
      return false;
    }

    boolean flag = isAdressBelongCurrentCorp(csendaddrid);
    if (!flag) {
      helper.setBodyValueAt("csendaddrid", null, row);
      helper.setBodyValueAt("csendaddrname", null, row);
    }
    return flag;
  }

  private boolean isAdressBelongCurrentCorp(String caddressid) {
    Object[] value = new Object[0];
    try {
      value = (Object[])(Object[])CacheTool.getCellValue("bd_address", "pk_address", "pk_corp", caddressid);
    }
    catch (BusinessException ex)
    {
      ExceptionUtils.getInstance().wrappException(ex);
    }
    String corp = (value == null) ? null : value[0].toString();
    String pk_corp = this.node.getContext().getPk_corp();
    boolean flag = false;
    if (corp == null) {
      flag = false;
    }
    else if (corp.equals(ValueConstant.GROUPCORP)) {
      flag = true;
    }
    else if (corp.equals(pk_corp)) {
      flag = true;
    }
    return flag;
  }

  private void setDefaultSendArea(CardBodyAfterEditEvent event)
  {
    CardCtrl card = event.getCard();
    CardHelper helper = new CardHelper(card);
    int row = event.getBillEditEvent().getRow();
    String csendareaid = helper.getBodyRowValue("csendareaid", row);
    if (csendareaid != null) {
      return;
    }
    String csendaddrid = helper.getBodyRowValue("csendaddrid", row);
    if (csendaddrid == null) {
      return;
    }
    String[] formulas = { "csendareaid->getColValue(bd_address,pk_areacl,pk_address,csendaddrid)", "csendareaname->getColValue(bd_areacl,areaclname,pk_areacl,csendareaid)" };

    helper.execBodyFormulas(row, formulas);
  }

  private void setDefaultSendDetailAdress(CardBodyAfterEditEvent event)
  {
    CardCtrl card = event.getCard();
    CardHelper helper = new CardHelper(card);
    int row = event.getBillEditEvent().getRow();
    String vsendaddress = helper.getBodyRowValue("vsendaddress", row);
    if (vsendaddress != null) {
      return;
    }
    String csendstoreid = helper.getBodyRowValue("csendstoreid", row);
    if (csendstoreid != null) {
      String[] formulas = { "vsendaddress->getColValue(bd_stordoc,storaddr,pk_stordoc,csendstoreid)" };

      helper.execBodyFormulas(row, formulas);
      vsendaddress = helper.getBodyRowValue("vsendaddress", row);
      if (vsendaddress != null) {
        return;
      }
    }
    String csendstoreorgid = helper.getBodyRowValue("csendstoreorgid", row);
    if (csendstoreorgid != null) {
      String[] formulas = { "vsendaddress->getColValue(bd_calbody,area,pk_calbody,csendstoreorgid)" };

      helper.execBodyFormulas(row, formulas);
    }
  }

  private void calculateMnyPriceByNum(CardCtrl card, int row) {
    IUIContext context = card.getContext();
    String pk_corp = context.getPk_corp();
    IPrecision precision = context.getPrecision(pk_corp);
    CardHelper helper = new CardHelper(card);

    String value = helper.getBodyRowValue("nnumber", row);
    UFDouble nnumber = ValueUtils.getInstance().getUFDouble(value);
    helper.setBodyValueAt("tmpnnumber", nnumber, row);//add by ouyangzhb 2012-12-24 
    if (nnumber == null) {
      helper.setBodyValueAt("nmoney", null, row);
      helper.setBodyValueAt("nprice", null, row);
      helper.setBodyValueAt("nassistnum", null, row);
      return;
    }

    value = helper.getBodyRowValue("nprice", row);
    UFDouble nprice = ValueUtils.getInstance().getUFDouble(value);
    if (nprice != null) {
      UFDouble nmoney = nnumber.multiply(nprice);
      nmoney = precision.adjustMoneyBusiness(nmoney);
      helper.setBodyValueAt("nmoney", nmoney, row);
    }
    else if (nnumber.doubleValue() == 0.0D) {
      helper.setBodyValueAt("nprice", ValueConstant.ZERO, row);
      helper.setBodyValueAt("nmoney", ValueConstant.ZERO, row);
    }
    else {
      value = helper.getBodyRowValue("nmoney", row);
      UFDouble nmoney = ValueUtils.getInstance().getUFDouble(value);
      if (nmoney != null) {
        nprice = nmoney.div(nnumber);
        nprice = precision.adjustPriceBusiness(nprice);
        helper.setBodyValueAt("nprice", nprice, row);
      }
    }
  }

  private void calculateAstNumChgRateByNum(CardCtrl card, int row)
  {
    IUIContext context = card.getContext();
    String pk_corp = context.getPk_corp();
    IPrecision precision = context.getPrecision(pk_corp);
    CardHelper helper = new CardHelper(card);

    String value = helper.getBodyRowValue("nnumber", row);
    UFDouble nnumber = ValueUtils.getInstance().getUFDouble(value);
    if (nnumber == null) {
      helper.setBodyValueAt("nassistnum", null, row);
      return;
    }
    value = helper.getBodyRowValue("castunitid", row);
    String castunitid = ValueUtils.getInstance().getString(value);

    String cinvbasid = helper.getBodyRowValue("cinvbasid", row);
    String[] cinvbaseids = { cinvbasid };

    boolean flag = InvInfoTool.getInstance().isAstManage(cinvbaseids)[0];
    if (!flag) {
      return;
    }
    String[] cinvbasids = { cinvbasid };

    String[] cmeasdocs = { castunitid };

    boolean fixedflag = InvInfoTool.getInstance().isFixedFlag(cinvbasids, cmeasdocs)[0];

    value = helper.getBodyRowValue("nchangerate", row);
    UFDouble nchangerate = ValueUtils.getInstance().getUFDouble(value);

    value = helper.getBodyRowValue("nassistnum", row);
    UFDouble nassistnum = ValueUtils.getInstance().getUFDouble(value);
    helper.setBodyValueAt("tmpnassistnum", nassistnum, row);//add by ouyangzhb 2012-12-24 

    if (fixedflag) {
      if (nchangerate != null) {
        nassistnum = nnumber.div(nchangerate);
        nassistnum = precision.adjustAsstNumber(nassistnum);
        helper.setBodyValueAt("nassistnum", nassistnum, row);
        helper.setBodyValueAt("tmpnassistnum", nassistnum, row);//add by ouyangzhb 2012-12-24 
      }

    }
    else if (nassistnum != null) {
      nchangerate = nnumber.div(nassistnum);
      nchangerate = precision.adjustChangeRate(nchangerate);
      helper.setBodyValueAt("nchangerate", nchangerate, row);
    }
    else if (nchangerate != null) {
      nassistnum = nnumber.div(nchangerate);
      nassistnum = precision.adjustAsstNumber(nassistnum);
      helper.setBodyValueAt("nassistnum", nassistnum, row);
      helper.setBodyValueAt("tmpnassistnum", nassistnum, row);//add by ouyangzhb 2012-12-24 
    }
  }

  private boolean calculateNumByChgRate(CardCtrl card, int row)
  {
    IUIContext context = card.getContext();
    String pk_corp = context.getPk_corp();
    IPrecision precision = context.getPrecision(pk_corp);

    CardHelper helper = new CardHelper(card);

    String value = helper.getBodyRowValue("nchangerate", row);
    UFDouble nchangerate = ValueUtils.getInstance().getUFDouble(value);

    if (nchangerate == null) {
      helper.setBodyValueAt("nassistnum", null, row);
      return false;
    }

    value = helper.getBodyRowValue("nnumber", row);
    UFDouble nnumber = ValueUtils.getInstance().getUFDouble(value);

    if (nnumber == null) {
      return false;
    }

    UFDouble nassistnum = nnumber.div(nchangerate);
    nassistnum = precision.adjustAsstNumber(nassistnum);
    helper.setBodyValueAt("nassistnum", nassistnum, row);
    helper.setBodyValueAt("tmpnassistnum", nassistnum, row);//add by ouyangzhb 2012-12-24 
    return true;
  }

  private boolean calculateNumChgRateByAstNum(CardCtrl card, int row)
  {
    IUIContext context = card.getContext();
    String pk_corp = context.getPk_corp();
    IPrecision precision = context.getPrecision(pk_corp);

    CardHelper helper = new CardHelper(card);

    Object value = helper.getBodyRowValue("nassistnum", row);
    UFDouble nassistnum = ValueUtils.getInstance().getUFDouble(value);

    value = helper.getBodyRowValue("nchangerate", row);
    UFDouble nchangerate = ValueUtils.getInstance().getUFDouble(value);

    value = helper.getBodyRowValue("nnumber", row);
    UFDouble nnumber = ValueUtils.getInstance().getUFDouble(value);

    if (nassistnum == null) {
      helper.setBodyValueAt("nnumber", null, row);
      return true;
    }
    if (nchangerate != null) {
      nnumber = nassistnum.multiply(nchangerate);
      nnumber = precision.adjustNumber(nnumber);
      helper.setBodyValueAt("nnumber", nnumber, row);
      helper.setBodyValueAt("tmpnnumber", nnumber, row);//add by ouyangzhb 2012-12-24 
      return true;
    }
    if (nnumber != null) {
      nchangerate = nnumber.div(nassistnum);
      nchangerate = precision.adjustChangeRate(nchangerate);
      helper.setBodyValueAt("nchangerate", nchangerate, row);
      return false;
    }
    return false;
  }

  private boolean calculateNumChgRateAstNumByAstUnit(CardCtrl card, int row) {
    CardHelper helper = new CardHelper(card);
    IUIContext context = card.getContext();
    String pk_corp = context.getPk_corp();
    IPrecision precision = context.getPrecision(pk_corp);

    String castunitid = helper.getBodyRowValue("castunitid", row);
    if (SCMTool.getInstance().isNull(castunitid)) {
      helper.setBodyValueAt("castunitid", null, row);
      helper.setBodyValueAt("nassistnum", null, row);
      helper.setBodyValueAt("nchangerate", null, row);
      return false;
    }

    String cinvbasid = helper.getBodyRowValue("cinvbasid", row);
    String[] cinvbasids = { cinvbasid };

    String[] castmeasdocs = { castunitid };

    String value = helper.getBodyRowValue("nnumber", row);
    UFDouble nnumber = ValueUtils.getInstance().getUFDouble(value);

    value = helper.getBodyRowValue("nassistnum", row);
    UFDouble nassistnum = ValueUtils.getInstance().getUFDouble(value);

    UFDouble nchangerate = InvInfoTool.getInstance().getChangeRate(cinvbasids, castmeasdocs)[0];

    helper.setBodyValueAt("nchangerate", nchangerate, row);
    if (nnumber != null) {
      nassistnum = nnumber.div(nchangerate);
      nassistnum = precision.adjustAsstNumber(nassistnum);
      helper.setBodyValueAt("nassistnum", nassistnum, row);
      return false;
    }
    if (nassistnum != null) {
      nnumber = nassistnum.multiply(nchangerate);
      nnumber = precision.adjustNumber(nnumber);
      helper.setBodyValueAt("nnumber", nnumber, row);
      return true;
    }
    return false;
  }

  private void changeNumber(CardCtrl card, int row) {
    calculateMnyPriceByNum(card, row);
    calculateVolumnWeight(card, row);
  }

  private void calculateVolumnWeight(CardCtrl card, int row)
  {
    CardHelper helper = new CardHelper(card);
    String value = helper.getBodyRowValue("nnumber", row);
    UFDouble nnumber = ValueUtils.getInstance().getUFDouble(value);
    if (nnumber == null) {
      return;
    }
    String[] formulas = { "nweight->getColValue(bd_invbasdoc,weitunitnum,pk_invbasdoc,cinvbasid)", "nweight->nnumber*nweight", "nvolumn->getColValue(bd_invbasdoc,storeunitnum,pk_invbasdoc,cinvbasid)", "nvolumn->nnumber*nvolumn" };

    helper.execBodyFormulas(row, formulas);
  }

  public void afterPackcodeEdit(CardBodyAfterEditEvent event) {
    CardCtrl card = event.getCard();
    int row = event.getBillEditEvent().getRow();
    calculatePackVolumn(card, row);
  }

  public void afterPacknumEdit(CardBodyAfterEditEvent event) {
    CardCtrl card = event.getCard();
    int row = event.getBillEditEvent().getRow();
    calculatePackVolumn(card, row);
  }

  private void calculatePackVolumn(CardCtrl card, int row) {
    CardHelper helper = new CardHelper(card);
    String value = helper.getBodyRowValue("npacknum", row);
    UFDouble npacknum = ValueUtils.getInstance().getUFDouble(value);
    if (npacknum == null) {
      return;
    }
    value = helper.getBodyRowValue("cpackid", row);
    String cpackid = ValueUtils.getInstance().getString(value);
    if (cpackid == null) {
      return;
    }
    String[] formulas = { "npackvolumn->getColValue(dm_packsort,packvolumn,pk_packsort,cpackid)", "npackvolumn->npacknum*npackvolumn" };

    helper.execBodyFormulas(row, formulas);
  }

  public void afterSignNumberEdit(CardBodyAfterEditEvent event) {
    CardCtrl card = event.getCard();
    int row = event.getBillEditEvent().getRow();
    calculateSignVolumnWeight(card, row);
    calculateSignAstNumChgRateBySignNum(card, row);
  }

  public void afterSignpacknumEdit(CardBodyAfterEditEvent event) {
    CardCtrl card = event.getCard();
    int row = event.getBillEditEvent().getRow();
    calculateSignPackVolumn(card, row);
  }

  public void afterSignAstNumEdit(CardBodyAfterEditEvent event) {
    CardCtrl card = event.getCard();
    int row = event.getBillEditEvent().getRow();
    boolean flag = calculateSignNumChgRateBySignAstNum(card, row);
    if (flag)
      calculateSignVolumnWeight(card, row);
  }

  private boolean calculateSignNumChgRateBySignAstNum(CardCtrl card, int row)
  {
    IUIContext context = card.getContext();
    String pk_corp = context.getPk_corp();
    IPrecision precision = context.getPrecision(pk_corp);

    CardHelper helper = new CardHelper(card);

    Object value = helper.getBodyRowValue("nsignassistnum", row);
    UFDouble nsignassistnum = ValueUtils.getInstance().getUFDouble(value);

    value = helper.getBodyRowValue("nchangerate", row);
    UFDouble nchangerate = ValueUtils.getInstance().getUFDouble(value);

    value = helper.getBodyRowValue("nsignnum", row);
    UFDouble nsignnum = ValueUtils.getInstance().getUFDouble(value);

    if (nsignassistnum == null) {
      helper.setBodyValueAt("nsignnum", null, row);
      return true;
    }
    if (nchangerate != null) {
      nsignnum = nsignassistnum.multiply(nchangerate);
      nsignnum = precision.adjustNumber(nsignnum);
      helper.setBodyValueAt("nsignnum", nsignnum, row);
      return true;
    }
    return false;
  }

  private void calculateSignVolumnWeight(CardCtrl card, int row) {
    CardHelper helper = new CardHelper(card);
    String value = helper.getBodyRowValue("nsignnum", row);
    UFDouble nnumber = ValueUtils.getInstance().getUFDouble(value);
    if (nnumber == null) {
      return;
    }
    String[] formulas = { "nsignweight->getColValue(bd_invbasdoc,weitunitnum,pk_invbasdoc,cinvbasid)", "nsignweight->nsignnum*nsignweight", "nsignvolumn->getColValue(bd_invbasdoc,storeunitnum,pk_invbasdoc,cinvbasid)", "nsignvolumn->nsignnum*nsignvolumn" };

    helper.execBodyFormulas(row, formulas);
  }

  private void calculateSignPackVolumn(CardCtrl card, int row) {
    CardHelper helper = new CardHelper(card);
    String value = helper.getBodyRowValue("nsignpacknum", row);
    UFDouble nsignpacknum = ValueUtils.getInstance().getUFDouble(value);
    if (nsignpacknum == null) {
      return;
    }
    value = helper.getBodyRowValue("cpackid", row);
    String cpackid = ValueUtils.getInstance().getString(value);
    if (cpackid == null) {
      return;
    }
    String[] formulas = { "nsignpackvolumn->getColValue(dm_packsort,packvolumn,pk_packsort,cpackid)", "nsignpackvolumn->nsignpacknum*nsignpackvolumn" };

    helper.execBodyFormulas(row, formulas);
  }

  private void calculateSignAstNumChgRateBySignNum(CardCtrl card, int row) {
    IUIContext context = card.getContext();
    String pk_corp = context.getPk_corp();
    IPrecision precision = context.getPrecision(pk_corp);
    CardHelper helper = new CardHelper(card);

    String value = helper.getBodyRowValue("nsignnum", row);
    UFDouble nsignnum = ValueUtils.getInstance().getUFDouble(value);
    if (nsignnum == null) {
      helper.setBodyValueAt("nsignassistnum", null, row);
      return;
    }
    value = helper.getBodyRowValue("castunitid", row);

    String cinvbasid = helper.getBodyRowValue("cinvbasid", row);
    String[] cinvbaseids = { cinvbasid };

    boolean flag = InvInfoTool.getInstance().isAstManage(cinvbaseids)[0];
    if (!flag) {
      return;
    }
    value = helper.getBodyRowValue("nchangerate", row);
    UFDouble nchangerate = ValueUtils.getInstance().getUFDouble(value);

    value = helper.getBodyRowValue("nsignassistnum", row);
    UFDouble nsignassistnum = ValueUtils.getInstance().getUFDouble(value);

    String castunitid = helper.getBodyRowValue("castunitid", row);
    String[] cmeasdocs = { castunitid };

    boolean fixedflag = InvInfoTool.getInstance().isFixedFlag(cinvbaseids, cmeasdocs)[0];

    if (fixedflag) {
      nsignassistnum = nsignnum.div(nchangerate);
      nsignassistnum = precision.adjustAsstNumber(nsignassistnum);
      helper.setBodyValueAt("nsignassistnum", nsignassistnum, row);
    }
    else
    {
      return;
    }
  }
}