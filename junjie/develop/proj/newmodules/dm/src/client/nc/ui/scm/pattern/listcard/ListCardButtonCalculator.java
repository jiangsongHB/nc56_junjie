package nc.ui.scm.pattern.listcard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import nc.ui.pub.ButtonObject;
import nc.ui.scm.pattern.buffer.IBillBuffer;
import nc.ui.scm.pattern.ctrl.button.AbstractButton;
import nc.ui.scm.pattern.ctrl.button.IButtonCalculator;
import nc.ui.scm.pattern.ctrl.card.CardStatus;
import nc.ui.scm.pattern.ctrl.list.ListStatus;
import nc.ui.scm.pattern.ctrl.node.FunctionStatus;
import nc.ui.scm.pattern.ctrl.node.NodeStatus;

/**
 * @author ����
 *
 * 2008-8-18 ����06:16:45
 */
public abstract class ListCardButtonCalculator implements IButtonCalculator {

  private Map<NodeStatus, List<ButtonObject>> nodeStatusButtonIndex = new HashMap<NodeStatus, List<ButtonObject>>();

  private Map<CardStatus, List<ButtonObject>> cardStatusButtonIndex = new HashMap<CardStatus, List<ButtonObject>>();

  private Map<ListStatus, List<ButtonObject>> listStatusButtonIndex = new HashMap<ListStatus, List<ButtonObject>>();

  private List<ButtonObject> transferFunctionStatusList = new ArrayList<ButtonObject>();
  
  private List<ButtonObject> commonFunctionStatusNotList = new ArrayList<ButtonObject>();

  private ListCardNode node = null;

  public ListCardButtonCalculator(
      ListCardNode node) {
    this.node = node;
    this.initNodeStatus();
    this.initTransferAddBillButton();

    this.initCardEdit();
    this.initCardView();

    this.initListView();
    
    this.initButtonNotInCommonFunctionStatus();
  }

  @SuppressWarnings("null")
  public void calculate() {
    Set<ButtonObject> set = this.calculateEnabledButton();
    this.exteaCommonCalculate(set);
    this.exteaCalculate(set);
    this.setTransactionButton(set);

    ButtonObject[] buttons = null;
    if (this.node.getNodeStatus() == NodeStatus.Card) {
      buttons = this.node.getButton().getCardButtonGroup();
    }
    else if (this.node.getNodeStatus() == NodeStatus.List) {
      buttons = this.node.getButton().getListButtonGroup();
    }
    for (ButtonObject button : buttons) {
      this.setAllButtonStatus(button, set);
    }

    this.switchButtonName();
  }

  /**
   * �����ǰ�������͵ĸ���ť���ã����Ӱ�ťҲ����
   * @param set
   */
  private void setTransactionButton(Set<ButtonObject> set) {
    List<ButtonObject> list = new ArrayList<ButtonObject>();
    for (ButtonObject button : set) {
      if (button.isCheckboxGroup()) {
        list.add(button);
      }
    }
    for (ButtonObject button : list) {
      this.setTransactionButton(set, button);
    }
  }

  private void setTransactionButton(Set<ButtonObject> set, ButtonObject button) {
    if (set.contains(button)) {
      ButtonObject[] children = button.getChildButtonGroup();
      for (ButtonObject child : children) {
        set.add(child);
      }
    }
  }

  private void switchButtonName() {
    ButtonObject button = this.node.getButton().getButton("�л�");
    if (button == null) {
      return;
    }
    NodeStatus nodeStatus = this.node.getNodeStatus();
    if (nodeStatus == NodeStatus.Card) {
      button.setName(nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
      "UCH022")/* @res "�б���ʾ" */);
    }
    else {
      button.setName(nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
      "UCH021")/* @res "��Ƭ��ʾ" */);
    }
  }

  private void setAllButtonStatus(ButtonObject button, Set<ButtonObject> set) {
    if (set.contains(button)) {
      this.node.getButton().enable(button);
    }
    else {
      this.node.getButton().disable(button);
    }
    if (button.getChildButtonGroup() == null
        || button.getChildButtonGroup().length == 0) {
      return;
    }
    for (ButtonObject childButton : button.getChildButtonGroup()) {
      this.setAllButtonStatus(childButton, set);
    }
  }

  private Set<ButtonObject> calculateEnabledButton() {
    Set<ButtonObject> set = new HashSet<ButtonObject>();
    List<ButtonObject> list = null;
    if (this.node.getNodeStatus() == NodeStatus.Card) {
      list = this.nodeStatusButtonIndex.get(NodeStatus.Card);
    }
    else if (this.node.getNodeStatus() == NodeStatus.List) {
      list = this.nodeStatusButtonIndex.get(NodeStatus.List);
    }
    if (list != null) {
      set.addAll(list);
    }

    if (this.node.getNodeStatus() == NodeStatus.Card) {
      CardStatus cardStatus = this.node.getCardPanel().getCard().getStatus();
      list = this.cardStatusButtonIndex.get(cardStatus);
      if (list != null) {
        set = this.merge(set, list);
      }
    }
    else if (this.node.getNodeStatus() == NodeStatus.List) {
      ListStatus listStatus = this.node.getListPanel().getList().getStatus();
      list = this.listStatusButtonIndex.get(listStatus);
      if (list != null) {
        set = this.merge(set, list);
      }
    }
    if (this.node.getFunctionStatus() == FunctionStatus.TransferBill) {
      set = this.merge(set, this.transferFunctionStatusList);
    }
    else{
      for(ButtonObject button:this.commonFunctionStatusNotList){
        set.remove( button );
      }
    }
    this.calculateNavgateButton(set);

    if (this.node.getQuery().getConditionSQL() == null) {
      ButtonObject button = this.node.getButton().getButton("ˢ��");
      if (button != null) {
        set.remove(button);
      }
    }
    return set;
  }

  protected Set<ButtonObject> merge(Set<ButtonObject> set,
      List<ButtonObject> list) {
    Set<ButtonObject> resultSet = new HashSet<ButtonObject>();

    for (ButtonObject button : list) {
      if (set.contains(button)) {
        resultSet.add(button);
      }
    }
    return resultSet;
  }

  protected void add(NodeStatus status, ButtonObject button) {
    if (button == null) {
      return;
    }
    List<ButtonObject> list = null;
    if (this.nodeStatusButtonIndex.containsKey(status)) {
      list = this.nodeStatusButtonIndex.get(status);
    }
    else {
      list = new ArrayList<ButtonObject>();
      this.nodeStatusButtonIndex.put(status, list);
    }
    list.add(button);
  }

  protected void add(CardStatus status, ButtonObject button) {
    if (button == null) {
      return;
    }
    List<ButtonObject> list = null;
    if (this.cardStatusButtonIndex.containsKey(status)) {
      list = this.cardStatusButtonIndex.get(status);
    }
    else {
      list = new ArrayList<ButtonObject>();
      this.cardStatusButtonIndex.put(status, list);
    }
    list.add(button);
  }

  protected void add(ListStatus status, ButtonObject button) {
    if (button == null) {
      return;
    }
    List<ButtonObject> list = null;
    if (this.listStatusButtonIndex.containsKey(status)) {
      list = this.listStatusButtonIndex.get(status);
    }
    else {
      list = new ArrayList<ButtonObject>();
      this.listStatusButtonIndex.put(status, list);
    }
    list.add(button);
  }

  protected ListCardNode getNode() {
    return this.node;
  }

  private void initNodeStatus() {
    AbstractButton buttonCtrl = this.getNode().getButton();
    ButtonObject[] buttons = buttonCtrl.getCardButtonGroup();
    for (ButtonObject button : buttons) {
      this.addAllButton(NodeStatus.Card, button);
    }

    buttons = buttonCtrl.getListButtonGroup();
    for (ButtonObject button : buttons) {
      this.addAllButton(NodeStatus.List, button);
    }
  }

  private void addAllButton(NodeStatus status, ButtonObject button) {
    this.add(status, button);
    if (button.getChildButtonGroup() == null
        || button.getChildButtonGroup().length == 0) {
      return;
    }
    for (ButtonObject childButton : button.getChildButtonGroup()) {
      this.addAllButton(status, childButton);
    }
  }

  @SuppressWarnings("null")
  private void calculateNavgateButton(Set<ButtonObject> set) {
    FunctionStatus status = this.node.getFunctionStatus();
    IBillBuffer buffer = null;
    if (status == FunctionStatus.Common) {
      buffer = this.node.getCommonBillBuffer();
    }
    else if (status == FunctionStatus.TransferBill) {
      buffer = this.node.getTransferBillBuffer();
    }
    int size = buffer.size();
    int cursor = buffer.getCurrentRow();
    if (size <= 1 || cursor == -1) {
      String[] names = new String[] {
          "��ҳ", "��ҳ", "��ҳ", "ĩҳ"
      };
      this.removeButton(set, names);
    }
    else if (cursor == 0) {
      String[] names = new String[] {
          "��ҳ", "��ҳ"
      };
      this.removeButton(set, names);
    }
    else if (cursor == size - 1) {
      String[] names = new String[] {
          "��ҳ", "ĩҳ"
      };
      this.removeButton(set, names);
    }
  }

  protected void removeButton(Set<ButtonObject> set, String[] names) {
    for (String name : names) {
      ButtonObject button = this.node.getButton().getButton(name);
      set.remove(button);
    }
  }

  private void exteaCommonCalculate(Set<ButtonObject> set) {
    FunctionStatus status = this.getNode().getFunctionStatus();
    if (status == FunctionStatus.TransferBill) {
      return;
    }
    IBillBuffer buffer = this.getNode().getCommonBillBuffer();
    int cursor = buffer.getCurrentRow();
    if (cursor == -1) {
      String[] names = new String[] {
          "���", "����", "�޸�", "ɾ��", "����", "ȫ��", "�ϲ���ʾ", "�ֵ���ӡ", "Ԥ��", "��ӡ", "����",
          "��λ", "�л�","�����ݹ�"
      };
      this.removeButton(set, names);
      return;
    }
  }

  private void initTransferAddBillButton() {
    String[] names = new String[] {
        "����ת��","�޸�","����", "ȡ��", "����", "ɾ��", "������", "������",
        "ճ����", "ճ���е���β", "��Ƭ�༭", "�����к�","��������"
        
    };
    AbstractButton buttonCtrl = this.getNode().getButton();
    for (String name : names) {
      ButtonObject button = buttonCtrl.getButton(name);
      this.addTransferBillButton(button);
    }
  }
  
  protected void addTransferBillButton(ButtonObject button){
    if (button == null) {
      return;
    }
    this.transferFunctionStatusList.add(button);
  }

  private void initCardEdit() {
    String[] names = new String[] {
        "����", "ȡ��", "����", "ɾ��", "������", "������", "ճ����", "ճ���е���β", "��Ƭ�༭", "�����к�",
        "��������"
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
        "�޸�", "ɾ��", "����", "���", "����", "��ѯ", "ˢ��", "��λ", "��ҳ", "��ҳ", "��ҳ", "ĩҳ", "�л�",
        "�ϲ���ʾ", "Ԥ��", "��ӡ", "�ֵ���ӡ", "����","�����ݹ�"
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
        "�޸�", "ɾ��", "����", "���", "����", "��ѯ", "ˢ��", "��λ", "ȫѡ", "ȫ��", "�л�", "�ϲ���ʾ",
        "Ԥ��", "��ӡ", "�ֵ���ӡ", "����","����ת��"
    };
    AbstractButton buttonCtrl = this.getNode().getButton();
    for (String name : names) {
      ButtonObject button = buttonCtrl.getButton(name);
      this.add(ListStatus.Browse, button);
      this.add(ListStatus.Init, button);
    }
  }

  private void initButtonNotInCommonFunctionStatus() {
    String[] names = new String[] {
        "����ת��"
    };
    AbstractButton buttonCtrl = this.getNode().getButton();
    for (String name : names) {
      ButtonObject button = buttonCtrl.getButton(name);
      this.commonFunctionStatusNotList.add( button );
    }
  }
  
  protected abstract void exteaCalculate(Set<ButtonObject> set);
}
