package nc.ui.dm.dm220.event.edit;

import nc.ui.pub.bill.BillEditEvent;
import nc.ui.scm.pattern.ctrl.card.auxiliary.InvFreeTool;
import nc.ui.scm.pattern.event.card.body.CardBodyAfterEditEvent;
import nc.ui.scm.pattern.event.card.body.CardBodyBeforeBatchEditEvent;
import nc.ui.scm.pattern.event.card.body.CardBodyBeforeEditEvent;
import nc.ui.scm.pattern.event.card.body.CardBodyTabChangedEvent;
import nc.ui.scm.pattern.event.card.head.CardHeadTailAfterEditEvent;
import nc.ui.scm.pattern.event.card.head.CardHeadTailBeforeEditEvent;
import nc.ui.scm.pattern.listcard.ListCardNode;

public class CardEditEventHandle
{
  private InvFreeTool invFreeTool = null;

  private CardBeforeEditor beforeEditor = null;

  private CardAfterEditor afterEditor = null;

  private CardBeforeBatchEditor beforeBatchEditor = null;

  private ListCardNode node = null;

  public CardEditEventHandle(ListCardNode node) {
    this.node = node;
    this.beforeEditor = new CardBeforeEditor(node);
    this.afterEditor = new CardAfterEditor(node);
    this.invFreeTool = new InvFreeTool(node);
    this.beforeBatchEditor = new CardBeforeBatchEditor(node);
  }

  public void doEvent(CardHeadTailAfterEditEvent event) {
    this.node.getAbstractUI().showHintMessage("");

    BillEditEvent billEditEvent = event.getBillEditEvent();
    String itemKey = billEditEvent.getKey();
    if (itemKey.startsWith("vdef")) {
      this.afterEditor.afterHeadDefDocEdit(event);
    }
    else if (itemKey.equals("ctrancustid"))
      this.afterEditor.afterTrancustEdit(event);
  }

  public void doEvent(CardBodyTabChangedEvent event)
  {
    this.node.updateButtons();
  }

  public void doEvent(CardBodyAfterEditEvent event) {
    this.node.getAbstractUI().showHintMessage("");

    BillEditEvent billEditEvent = event.getBillEditEvent();
    String itemKey = billEditEvent.getKey();
    if (itemKey.equals("vfree0")) {
      this.invFreeTool.afterEdit(event);
    }
    else if (itemKey.equals("nassistnum")) {
      this.afterEditor.afterAstNumEdit(event);
    }
    else if (itemKey.equals("castunitname")) {
      this.afterEditor.afterAstunitEdit(event);
    }
    else if (itemKey.equals("nnumber")) {
      this.afterEditor.afterNumberEdit(event);
    }
    else if (itemKey.equals("nsignnum")) {
      this.afterEditor.afterSignNumberEdit(event);
    }
    else if (itemKey.equals("nsignassistnum")) {
      this.afterEditor.afterSignAstNumEdit(event);
    }
    else if (itemKey.equals("nsignpacknum")) {
      this.afterEditor.afterSignpacknumEdit(event);
    }
    else if (itemKey.equals("nchangerate")) {
      this.afterEditor.afterChangeRateEdit(event);
    }
    else if (itemKey.equals("nmoney")) {
      this.afterEditor.afterMoneyEdit(event);
    }
    else if (itemKey.equals("nprice")) {
      this.afterEditor.afterPriceEdit(event);
    }
    else if (itemKey.startsWith("vbdef")) {
      this.afterEditor.afterBodyDefDocEdit(event);
    }
    else if (itemKey.equals("vbpackdef")) {
      this.afterEditor.afterBodyDefDocEdit(event);
    }
    else if (itemKey.equals("cprojectname")) {
      this.afterEditor.afterProjectEdit(event);
    }
    else if (itemKey.equals("cinventorycode")) {
      this.afterEditor.afterInventoryEdit(event);
    }
    else if (itemKey.equals("csendcorpname")) {
      this.afterEditor.afterSendCorpEdit(event);
    }
    else if (itemKey.equals("creceivecorpname")) {
      this.afterEditor.afterReceiveCorpEdit(event);
    }
    else if (itemKey.equals("csendvendorname")) {
      this.afterEditor.afterSendVendorEdit(event);
    }
    else if (itemKey.equals("creceivecustname")) {
      this.afterEditor.afterReceiveCustomerEdit(event);
    }
    else if (itemKey.equals("ccosignname")) {
      this.afterEditor.afterCosignerEdit(event);
    }
    else if (itemKey.equals("ctakefeename")) {
      this.afterEditor.afterTakeFeeEdit(event);
    }
    else if (itemKey.equals("creceiveaddrname")) {
      this.afterEditor.afterReceiveAddressEdit(event);
    }
    else if (itemKey.equals("csendaddrname")) {
      this.afterEditor.afterSendAddressEdit(event);
    }
    else if (itemKey.equals("creceiveareaname")) {
      this.afterEditor.afterReceiveAreaEdit(event);
    }
    else if (itemKey.equals("csendareaname")) {
      this.afterEditor.afterSendAreaEdit(event);
    }
    else if (itemKey.equals("csendstoreorgname")) {
      this.afterEditor.afterSendStoreOrgEdit(event);
    }
    else if (itemKey.equals("creceivestoreorgname")) {
      this.afterEditor.afterReceiveStoreOrgEdit(event);
    }
    else if (itemKey.equals("csendstorename")) {
      this.afterEditor.afterSendStoreEdit(event);
    }
    else if (itemKey.equals("creceivestorename")) {
      this.afterEditor.afterReceiveStoreEdit(event);
    }
    else if (itemKey.equals("cpackcode")) {
      this.afterEditor.afterPackcodeEdit(event);
    }
    else if (itemKey.equals("npacknum"))
      this.afterEditor.afterPacknumEdit(event);
    //add by ouyangzhb 2012-12-24
    else if (itemKey.equals("tmpnnumber")) {
    	afterEditor.afterNumberEdit(event);
      }
    
  }

  public void doEvent(CardBodyBeforeEditEvent event)
  {
    this.node.getAbstractUI().showHintMessage("");

    BillEditEvent billEditEvent = event.getBillEditEvent();
    String itemKey = billEditEvent.getKey();
    boolean flag = true;
    if (itemKey.equals("vfree0")) {
      flag = this.invFreeTool.beforeEdit(event);
    }
    else if (itemKey.equals("vbatch")) {
      flag = this.beforeEditor.beforeVbatchEdit(event);
    }
    else if (itemKey.equals("nsignassistnum")) {
      flag = this.beforeEditor.beforeSignAssistnumEdit(event);
    }
    else if (itemKey.equals("nassistnum")) {
      flag = this.beforeEditor.beforeAssistnumEdit(event);
    }
    else if (itemKey.equals("castunitname")) {
      flag = this.beforeEditor.beforeAstunitEdit(event);
    }
    else if (itemKey.equals("nnumber")) {
      flag = this.beforeEditor.beforeNumberEdit(event);
    }
    else if (itemKey.equals("nchangerate")) {
      flag = this.beforeEditor.beforeChangerateEdit(event);
    }
    else if (itemKey.equals("cinventorycode")) {
      flag = this.beforeEditor.beforeInventoryEdit(event);
    }
    else if (itemKey.equals("nmoney")) {
      flag = this.beforeEditor.beforeMoneyEdit(event);
    }
    else if (itemKey.equals("nprice")) {
      flag = this.beforeEditor.beforePriceEdit(event);
    }
    else if (itemKey.equals("nweight")) {
      flag = this.beforeEditor.beforeWeightEdit(event);
    }
    else if (itemKey.equals("nvolumn")) {
      flag = this.beforeEditor.beforeVolumnEdit(event);
    }
    else if (itemKey.startsWith("vbdef")) {
      flag = this.beforeEditor.beforeBodyDefDocEdit(event);
    }
    else if (itemKey.startsWith("vbpackdef")) {
      flag = this.beforeEditor.beforeBodyDefDocEdit(event);
    }
    else if (itemKey.equals("cprojectphasename")) {
      flag = this.beforeEditor.beforeProjectphaseEdit(event);
    }
    else if (itemKey.equals("csendcorpname")) {
      flag = this.beforeEditor.beforeSendcorpEdit(event);
    }
    else if (itemKey.equals("creceivecorpname")) {
      flag = this.beforeEditor.beforeReceivecorpEdit(event);
    }
    else if (itemKey.equals("csendstoreorgname")) {
      flag = this.beforeEditor.beforeSendStoreOrgEdit(event);
    }
    else if (itemKey.equals("creceivestoreorgname")) {
      flag = this.beforeEditor.beforeReceiveStoreOrgEdit(event);
    }
    else if (itemKey.equals("csendstorename")) {
      flag = this.beforeEditor.beforeSendStoreEdit(event);
    }
    else if (itemKey.equals("creceivestorename")) {
      flag = this.beforeEditor.beforeReceiveStoreEdit(event);
    }
    else if (itemKey.equals("csendvendorname")) {
      flag = this.beforeEditor.beforeSendVendorEdit(event);
    }
    else if (itemKey.equals("creceivecustname")) {
      flag = this.beforeEditor.beforeReceiveCustomerEdit(event);
    }
    else if (itemKey.equals("vsendaddress")) {
      flag = this.beforeEditor.beforeSendaddressEdit(event);
    }
    else if (itemKey.equals("vreceiveaddress")) {
      flag = this.beforeEditor.beforeReceiveaddressEdit(event);
    }
    //add by ouyangzhb 2012-12-24
    else if (itemKey.equals("tmpnnumber")) {
        flag = this.beforeEditor.beforeTmpNumberEdit(event);
      }
    
    event.setEditable(flag);
  }

  public void doEvent(CardHeadTailBeforeEditEvent event) {
    this.node.getAbstractUI().showHintMessage("");

    boolean flag = true;
    String itemKey = event.getBillItemEvent().getItem().getKey();
    if (itemKey.startsWith("vdef")) {
      flag = this.beforeEditor.beforeHeadDefDocEdit(event);
    }
    else if (itemKey.equals("capcustid")) {
      flag = this.beforeEditor.beforeApCustEdit(event);
    }
    else if (itemKey.equals("ctrancustid")) {
      flag = this.beforeEditor.beforeTrancustEdit(event);
    }
    else if (itemKey.equals("crouteid")) {
      flag = this.beforeEditor.beforeRouteEdit(event);
    }
    else if (itemKey.equals("cdriverid")) {
      flag = this.beforeEditor.beforeDriverEdit(event);
    }
    else if (itemKey.equals("cvehicletypeid")) {
      flag = this.beforeEditor.beforeVehicleTypeEdit(event);
    }
    else if (itemKey.equals("cvehicleid")) {
      this.beforeEditor.beforeVehicleEdit(event);
    }
    event.setEditable(flag);
  }

  public void doEvent(CardBodyBeforeBatchEditEvent event) {
    this.node.getAbstractUI().showHintMessage("");

    BillEditEvent billEditEvent = event.getBillEditEvent();
    String itemKey = billEditEvent.getKey();
    boolean flag = true;
    if (itemKey.equals("castunitname")) {
      flag = this.beforeBatchEditor.beforeAstunitEdit(event);
    }
    else if (itemKey.equals("csendstoreorgname")) {
      flag = this.beforeBatchEditor.beforeSendStoreOrgEdit(event);
    }
    else if (itemKey.equals("creceivestoreorgname")) {
      flag = this.beforeBatchEditor.beforeReceiveStoreOrgEdit(event);
    }
    else if (itemKey.equals("csendstorename")) {
      flag = this.beforeBatchEditor.beforeSendStoreEdit(event);
    }
    else if (itemKey.equals("creceivestorename")) {
      flag = this.beforeBatchEditor.beforeReceiveStoreEdit(event);
    }
    event.setEditable(flag);
  }
}