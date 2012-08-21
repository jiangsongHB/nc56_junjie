package nc.ui.dm.dm220;

import java.util.Set;

import nc.ui.dm.pub.context.DMContextInitHelper;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.scm.pattern.buffer.IBillBuffer;
import nc.ui.scm.pattern.ctrl.button.AbstractButton;
import nc.ui.scm.pattern.ctrl.card.CardCtrl;
import nc.ui.scm.pattern.ctrl.card.CardStatus;
import nc.ui.scm.pattern.ctrl.list.ListStatus;
import nc.ui.scm.pattern.ctrl.node.FunctionStatus;
import nc.ui.scm.pattern.ctrl.node.NodeStatus;
import nc.ui.scm.pattern.listcard.ListCardButtonCalculator;
import nc.ui.scm.pattern.listcard.ListCardNode;

import nc.vo.dm.dm140.ExtendTypeVO;
import nc.vo.dm.model.delivbill.entity.DelivBillHeadVO;
import nc.vo.dm.model.delivbill.entity.DelivBillItemVO;
import nc.vo.dm.model.delivbill.entity.DelivBillPackItemVO;
import nc.vo.dm.model.delivbill.entity.DelivBillVO;
import nc.vo.dm.pub.context.DelivTypeContext;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pattern.data.ValueUtils;
import nc.vo.scm.pattern.domain.scm.enumeration.FBillStatusFlag;

/**
 * @author 钟鸣 2008-8-18 下午09:06:27
 */
public class ButtonStatusCalculator extends ListCardButtonCalculator {
  private NodeContext nodeContext = null;

  public ButtonStatusCalculator(
      ListCardNode node, NodeContext nodeContext) {
    super(node);
    this.nodeContext = nodeContext;
    this.init();
  }

  private void init() {
    this.initTansactionType();
    this.initCardEdit();
    this.initCardView();

    this.initListView();
    this.initTransfer();
  }
  
  /**
   * 
   * 描述：初始化“运输类型”按钮，读取并加载其下面的子按钮
   * <p>
   * @author duy
   * @time 2009-2-16 上午10:30:20
   */
  private void initTansactionType() {
    String[] cdelivTypes = nodeContext.getContext().getCdelivTypes();
    if (cdelivTypes == null || cdelivTypes.length == 0) {
      return;
    }
    ButtonObject button = this.getNode().getButton().getButton("运输类型");
    for (String cdelivType : cdelivTypes) {
      String name = nodeContext.getContext().getTypeName(cdelivType);
      ButtonObject btn = new ButtonObject(name, name, cdelivType);
      btn.setPowerContrl(false);
      btn.setCheckboxGroup(true);
      // TAG=交易类型
      btn.setTag(cdelivType);
      button.addChildButton(btn);
    }
    button.setCheckboxGroup(true);
    if (button.getChildButtonGroup() != null
        && button.getChildButtonGroup().length > 0) {
      ButtonObject btn = button.getChildButtonGroup()[0];
      btn.setSelected(true);
      this.nodeContext.setTransactionType(btn.getTag());
    }
  }

  private void initCardEdit() {
    String[] names = new String[] {
        "生成包装记录", "复制收发货信息", "粘贴收发货信息"
    };
    AbstractButton buttonCtrl = this.getNode().getButton();
    for (String name : names) {
      ButtonObject button = buttonCtrl.getButton(name);
      this.add(CardStatus.New, button);
      this.add(CardStatus.Update, button);
    }
  }

  private void initCardView() {
    String[] names = new String[] {
        "运输类型", "参照上游", "自制", "装车录入", "签收录入", "生成任务单", "应付运费计算", "取消应付运费",
        "应付运费查询", "应收运费计算", "取消应收运费", "应收运费查询","费用暂估"
    };
    AbstractButton buttonCtrl = this.getNode().getButton();
    for (String name : names) {
      ButtonObject button = buttonCtrl.getButton(name);
      this.add(CardStatus.Browse, button);
      this.add(CardStatus.Init, button);
    }
  }

  private void initListView() {
    String[] names = new String[] {
        "运输类型", "参照上游", "自制", "装车录入", "签收录入", "生成任务单", "应付运费计算", "取消应付运费",
        "应付运费查询", "应收运费计算", "取消应收运费", "应收运费查询"
    };
    AbstractButton buttonCtrl = this.getNode().getButton();
    for (String name : names) {
      ButtonObject button = buttonCtrl.getButton(name);
      this.add(ListStatus.Browse, button);
      this.add(ListStatus.Init, button);
    }
  }

  private void initTransfer() {
    String[] names = new String[] {
        "生成包装记录", "复制收发货信息", "粘贴收发货信息"
    };
    AbstractButton buttonCtrl = this.getNode().getButton();
    for (String name : names) {
      ButtonObject button = buttonCtrl.getButton(name);
      this.addTransferBillButton(button);
    }
  }

  @Override
  protected void exteaCalculate(Set<ButtonObject> set) {
    BillCardPanel bc = this.getNode().getCardPanel().getCard().getBillCard();
    String stablecode = bc.getCurrentBodyTableCode();
    FunctionStatus status = this.getNode().getFunctionStatus();
    if (status == FunctionStatus.TransferBill) {
      String bcanselfmade = this.nodeContext.getTransactionTypeAttribute().bcanselfmade;
      if (bcanselfmade != null && bcanselfmade.equals("N")) {
        if (stablecode.equalsIgnoreCase("dm_delivbill_b")|| stablecode.equalsIgnoreCase("invinfo")){
          //
          String[] names = new String[] {
              "增行"
          };
          this.removeButton(set, names);
        }
     /*   String[] names = new String[] {
          "增行"
        };
        this.removeButton(set, names);*/
      }
      return;
    }
    
    if( this.isInView() ){
      this.calculateInView(set);
    }
    else if( this.isInUpdateEdit() ){
      this.calculateInUpdateEdit(set);
    }
    else{
      this.calculateInNewEdit(set);
    }
  }
   
  private boolean isInView(){
    boolean flag = true;
    if (this.getNode().getNodeStatus() == NodeStatus.Card) {
      CardCtrl card =  this.getNode().getCardPanel().getCard();
      CardStatus status = card.getStatus();
      if( status != CardStatus.Browse && status != CardStatus.Init ){
        flag = false;
      }
    }
    else if (this.getNode().getNodeStatus() == NodeStatus.List) {
      flag = true;
    }
    return flag;
  }
  
  private boolean isInUpdateEdit(){
    boolean flag = true;
    if (this.getNode().getNodeStatus() == NodeStatus.Card) {
      CardCtrl card =  this.getNode().getCardPanel().getCard();
      CardStatus status = card.getStatus();
      if( status != CardStatus.Update ){
        flag = false;
      }
    }
    else if (this.getNode().getNodeStatus() == NodeStatus.List) {
      flag = false;
    }
    return flag;
  }
  
  private void calculateInView(Set<ButtonObject> set){
    if (this.nodeContext.getCurrentTransactionType() == null) {
      String[] names = new String[] {
          "参照上游", "自制","参照增行"
      };
      this.removeButton(set, names);
    }
    else {
      String bcanselfmade = this.nodeContext.getTransactionTypeAttribute().bcanselfmade;
      if (bcanselfmade != null && bcanselfmade.equals("N")) {
        String[] names = new String[] {
          "自制","增行"
        };
        this.removeButton(set, names);
      }
      String cdelivtype = this.nodeContext.getCurrentTransactionType();
      int size = this.nodeContext.getContext().getBillTypeIndex(cdelivtype)
          .size();
      //流程配置中没有上游可参照单据
      if (size == 0) {
        String[] names = new String[] {
          "参照上游","参照增行"
        };
        this.removeButton(set, names);
      }
    }
    IBillBuffer buffer = this.getNode().getCommonBillBuffer();
    int cursor = buffer.getCurrentRow();
    if (cursor == -1) {
      String[] names = new String[] {
          "装车录入", "签收录入", "应付运费计算", "取消应付运费", "应付运费查询", "应收运费计算", "取消应收运费",
          "应收运费查询", "生成任务单"
      };
      this.removeButton(set, names);
      return;
    }
    DelivBillVO bill = (DelivBillVO) buffer.getBill(cursor);
    this.calculateButtonStatus(bill, set);
  }

  private void calculateInNewEdit(Set<ButtonObject> set){
    String cdelivtype = this.nodeContext.getCurrentTransactionType();
    int size = this.nodeContext.getContext().getBillTypeIndex(cdelivtype)
        .size();
    //流程配置中没有上游可参照单据
    if (size == 0) {
      String[] names = new String[] {
        "参照增行"
      };
      this.removeButton(set, names);
    }
  }
  
  private void calculateInUpdateEdit(Set<ButtonObject> set){
    BillCardPanel bc = this.getNode().getCardPanel().getCard().getBillCard();
    String stablecode = bc.getCurrentBodyTableCode();
    IBillBuffer buffer = this.getNode().getCommonBillBuffer();
    int cursor = buffer.getCurrentRow();
    DelivBillVO bill = (DelivBillVO) buffer.getBill(cursor);
    
    DelivBillHeadVO head = (DelivBillHeadVO) bill.getParent();

    //如果当前单据的运输类型和按钮上的运输类型的选择不一致，则不能参照增行
    String cdelivtype = head.getCdelivtype();
    String transactionType = this.nodeContext.getCurrentTransactionType();
    if (!transactionType.equals(cdelivtype)) {
      String[] names = new String[] {
        "参照增行"
      };
      this.removeButton(set, names);
    }
    int size = this.nodeContext.getContext().getBillTypeIndex(cdelivtype)
        .size();
    //流程配置中没有上游可参照单据
    if (size == 0) {
      String[] names = new String[] {
        "参照增行"
      };
      this.removeButton(set, names);
    }

    DelivTypeContext context= this.nodeContext.getContext();
    
    //当前单据的运输类型不允许自制，则不能增行
    String bcanselfmade = context.getTypeAttribute(cdelivtype).bcanselfmade;
    if (bcanselfmade != null && bcanselfmade.equals("N")) {
      if (stablecode.equalsIgnoreCase("dm_delivbill_b")|| stablecode.equalsIgnoreCase("invinfo")){
        String[] names = new String[] {
            "增行"
        };
        this.removeButton(set, names);
      }
/*      String[] names = new String[] {
          "增行"
      };
      this.removeButton(set, names);*/
    }
    
    this.processActionType(set);
  }
  
  private void calculateButtonStatus(DelivBillVO bill, Set<ButtonObject> set) {
    DelivBillHeadVO head = (DelivBillHeadVO) bill.getParent();
    FBillStatusFlag fstatusflag = head.getFstatusflag();
    if (fstatusflag == FBillStatusFlag.FREE) {
      String[] names = new String[] {
          "弃审", "装车录入", "签收录入","费用暂估"
      };
      this.removeButton(set, names);
    }
    else if (fstatusflag == FBillStatusFlag.AUDITED) {
      String[] names = new String[] {
          "审核", "修改", "删除"
      };
    	  
      this.removeButton(set, names);
      //lumzh 2012-08-21 当运输单为已审核且已暂估时，费用暂估按钮不可用！
      if(head.getAttributeValue("isestimate")!=null && head.getAttributeValue("isestimate").toString().equals("Y")){
    	   String[] name1 = new String[] {
    			   "费用暂估"
    		      };  
    	   this.removeButton(set, name1);
      }
    }
    this.setAPCalculateButtonStatus(bill, set);
    this.setARCalculateButtonStatus(bill, set);
    this.setAssistantButtonnStatus(bill, set);
  }

  private void processActionType(Set<ButtonObject> set) {
    if (this.nodeContext.isLoadEditing()) {
      String[] names = new String[] {
          "参照增行", "生成包装记录", "复制收发货信息", "粘贴收发货信息", "增行", "删行", "插入行", "复制行",
          "粘贴行", "粘贴行到表尾", "卡片编辑", "重排行号"
      };
      this.removeButton(set, names);
    }
  }

  private void setARCalculateButtonStatus(DelivBillVO bill,
      Set<ButtonObject> set) {
    DelivBillHeadVO head = (DelivBillHeadVO) bill.getParent();
    UFBoolean barsettledflag = head.getBarsettledflag();
    String cdelivtype = head.getCdelivtype();
    DelivTypeContext context= DMContextInitHelper.getInstance().getDelivTypeContext();
    ExtendTypeVO type = context.getTypeAttribute(cdelivtype);
    // 当前运输类型需要进行应收运费计算
    boolean flag = ValueUtils.getInstance().getBoolean(
        type.getBcalculatearfee());
    if (!flag) {
      String[] names = new String[] {
          "取消应收运费", "应收运费查询", "应收运费计算"
      };
      this.removeButton(set, names);
      return;
    }
    // 已生成应收运费结算单
    if (barsettledflag.booleanValue()) {
      String[] names = new String[] {
        "应收运费计算","修改", "删除"
      };
      this.removeButton(set, names);
    }
    else {
      String[] names = new String[] {
          "取消应收运费", "应收运费查询"
      };
      this.removeButton(set, names);
    }
    UFDouble mny = head.getNartotalverifymny();
    //已经与应收发票核销，不能取消应收运费计算
    if (mny != null && mny.doubleValue() > 0) {
      String[] names = new String[] {
        "取消应收运费","弃审","装车录入","签收录入"
      };
      this.removeButton(set, names);
    }
  }

  private void setAPCalculateButtonStatus(DelivBillVO bill,
      Set<ButtonObject> set) {
    DelivBillHeadVO head = (DelivBillHeadVO) bill.getParent();
    UFBoolean bapsettledflag = head.getBapsettledflag();
    String cdelivtype = head.getCdelivtype();
    DelivTypeContext context= DMContextInitHelper.getInstance().getDelivTypeContext();
    ExtendTypeVO type = context.getTypeAttribute(cdelivtype);
    // 当前运输类型需要进行应付运费计算
    boolean flag = ValueUtils.getInstance().getBoolean(
        type.getBcalculateapfee());
    if (!flag) {
      String[] names = new String[] {
          "取消应付运费", "应付运费查询", "应付运费计算"
      };
      this.removeButton(set, names);
      return;
    }
    // 已生成应付运费结算单
    if (bapsettledflag.booleanValue()) {
      String[] names = new String[] {
        "应付运费计算","修改", "删除"
      };
      this.removeButton(set, names);
    }
    else {
      String[] names = new String[] {
          "取消应付运费", "应付运费查询"
      };
      this.removeButton(set, names);
    }
    UFDouble mny = head.getNaptotalverifymny();
    //已经与应付发票核销，不能取消应付运费计算
    if (mny != null && mny.doubleValue() > 0) {
      String[] names = new String[] {
        "取消应付运费","弃审","装车录入","签收录入"
      };
      this.removeButton(set, names);
    }
  }

  private void setAssistantButtonnStatus(DelivBillVO bill, Set<ButtonObject> set) {
    DelivBillHeadVO head = (DelivBillHeadVO) bill.getParent();
    UFBoolean bapsettledflag = head.getBapsettledflag();
    UFBoolean barsettledflag = head.getBarsettledflag();
//    UFBoolean bmissionbillflag = head.getBmissionbillflag();
    if (bapsettledflag.booleanValue() || barsettledflag.booleanValue()) {
      String[] names = new String[] {
          "弃审", "装车录入", "签收录入"
      };
      this.removeButton(set, names);
    }
  /*  if (bmissionbillflag.booleanValue()) {
      String[] names = new String[] {
        "生成任务单"
      };
      this.removeButton(set, names);
    }*/
    String ctrancustid = head.getCtrancustid();
    //有承运商就不能生成任务单
    if (ctrancustid != null) {
      String[] names = new String[] {
        "生成任务单"
      };
      this.removeButton(set, names);
    }
    boolean signed = this.hasSigned(bill);
    if (signed) {
      String[] names = new String[] {
        "装车录入"
      };
      this.removeButton(set, names);
    }
  }

  private boolean hasSigned(DelivBillVO bill) {
    boolean flag = false;
    DelivBillItemVO[] items = bill.getInvBodys();
    if (items == null || items.length == 0) {
      return flag;
    }
    for (DelivBillItemVO item : items) {
      if (item.getCsignerid() != null) {
        return true;
      }
    }
    DelivBillPackItemVO[] packVOs = bill.getPackBodys();
    if (packVOs == null || packVOs.length == 0) {
      return flag;
    }
    for (DelivBillPackItemVO vo : packVOs) {
      if (vo.getCsignerid() != null) {
        return true;
      }
    }
    return flag;
  }
}
