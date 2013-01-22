package nc.ui.dm.dm220;

import nc.ui.dm.dm220.event.button.ActionEventHandle;
import nc.ui.dm.dm220.event.edit.CardEditEventHandle;
import nc.ui.dm.dm220.event.init.CardInitor;
import nc.ui.dm.dm220.event.init.NodeInitor;
import nc.ui.ml.NCLangRes;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.FramePanel;
import nc.ui.scm.pattern.context.IUIContext;
import nc.ui.scm.pattern.context.SCMUIContext;
import nc.ui.scm.pattern.ctrl.AbstractUI;
import nc.ui.scm.pattern.ctrl.button.AbstractButton;
import nc.ui.scm.pattern.ctrl.button.ButtonTreeControl;
import nc.ui.scm.pattern.ctrl.card.CardCtrlInfo;
import nc.ui.scm.pattern.ctrl.list.ListCtrlInfo;
import nc.ui.scm.pattern.ctrl.node.AbstractNode;
import nc.ui.scm.pattern.ctrl.node.CommonCardPanel;
import nc.ui.scm.pattern.ctrl.node.CommonListPanel;
import nc.ui.scm.pattern.ctrl.node.ICardPanel;
import nc.ui.scm.pattern.ctrl.node.IListPanel;
import nc.ui.scm.pattern.ctrl.node.NodeCtrlInfo;
import nc.ui.scm.pattern.ctrl.query.QueryCtrl;
import nc.ui.scm.pattern.event.card.body.CardBodyAfterEditEvent;
import nc.ui.scm.pattern.event.card.body.CardBodyBeforeBatchEditEvent;
import nc.ui.scm.pattern.event.card.body.CardBodyBeforeEditEvent;
import nc.ui.scm.pattern.event.card.body.CardBodyTabChangedEvent;
import nc.ui.scm.pattern.event.card.head.CardHeadTailAfterEditEvent;
import nc.ui.scm.pattern.event.card.head.CardHeadTailBeforeEditEvent;
import nc.ui.scm.pattern.event.card.init.CardAfterLoadEvent;
import nc.ui.scm.pattern.event.node.ActionEvent;
import nc.ui.scm.pattern.event.node.NormalOpenEvent;
import nc.ui.scm.pattern.event.node.RelationViewEvent;
import nc.ui.scm.pattern.listcard.ListCardNode;

import nc.vo.dm.model.delivbill.entity.DelivBillHeadVO;
import nc.vo.dm.model.delivbill.entity.DelivBillItemVO;
import nc.vo.dm.model.delivbill.entity.DelivBillPackItemVO;
import nc.vo.dm.model.delivbill.entity.DelivBillVO;
import nc.vo.scm.pattern.domain.uap.enumeration.NCBillType;

/**
 * 运输单主界面
 * @author 钟鸣
 *
 * 2008-8-10 上午10:35:44
 */
public class ClientUI extends AbstractUI {

  private static final long serialVersionUID = 6085100220324693692L;

  public ClientUI() {
  }

  public ClientUI(
      FramePanel frame_pane) {
    super(frame_pane);
 
    
    //this.getButtonObjectByCode(code);
 /* ButtonObject nbutton =  this.getButtonObjectByCode("参照采购入库费用");//;getButton();
 
    if(nbutton!=null){
    	  nbutton.setVisible(true);
    	  nbutton.setEnabled(true);
      }  
    ButtonObject[] bts= this.getButtons();
    if(bts!=null){
    	
    }*/
   
    
    
  }

  @Override
  protected AbstractNode createNode() {
    String title = NCLangRes.getInstance().getStrByID("40142020",
        "UPP40142020-000000");/*"运输单"*/
    NodeCtrlInfo nodeInfo = new NodeCtrlInfo("40142020", title,
        NCBillType.DM_4804.value());
    nodeInfo.setFuncRegisterVO(getFuncRegisterVO());
    
    CardCtrlInfo cardInfo = new CardCtrlInfo(nodeInfo);
    cardInfo.setBillClass(DelivBillVO.class);
    cardInfo.setHeadVOClass(DelivBillHeadVO.class);
    cardInfo.setBodyVOForTab("dm_delivbill_b", DelivBillItemVO.class);
    cardInfo.setBodyVOForTab("dm_packbill", DelivBillPackItemVO.class);
    
    ListCtrlInfo listInfo = new ListCtrlInfo(nodeInfo);
    listInfo.setHeadVOClass(DelivBillHeadVO.class);
    listInfo.setBodyVOForTab("dm_delivbill_b", DelivBillItemVO.class);
    listInfo.setBodyVOForTab("dm_packbill", DelivBillPackItemVO.class);

    IUIContext context = new SCMUIContext();

    AbstractButton button = new ButtonTreeControl(nodeInfo);

    QueryCtrl query = new QueryCtrl(context);
    ICardPanel cardPanel = new CommonCardPanel(context, cardInfo);
    IListPanel listPanel = new CommonListPanel(context, listInfo);

    ListCardNode node = new ListCardNode(context, nodeInfo, button, query,
        cardPanel, listPanel, this);

    CardInitor cardInitor = new CardInitor();
    cardPanel.getCard().add(CardAfterLoadEvent.class, cardInitor);

    CardEditEventHandle cardEdit = new CardEditEventHandle(node);
    cardPanel.getCard().add(CardBodyAfterEditEvent.class, cardEdit);
    cardPanel.getCard().add(CardBodyBeforeEditEvent.class, cardEdit);
    cardPanel.getCard().add(CardHeadTailBeforeEditEvent.class, cardEdit);
    cardPanel.getCard().add(CardHeadTailAfterEditEvent.class, cardEdit);
    cardPanel.getCard().add(CardBodyBeforeBatchEditEvent.class, cardEdit);
    cardPanel.getCard().add(CardBodyTabChangedEvent.class, cardEdit);    
    
    NodeContext nodeContext = new NodeContext(context);

    ButtonStatusCalculator buttonCalculator = new ButtonStatusCalculator(node,
        nodeContext);
    
  
    button.addButtonCalculator(buttonCalculator);

    ActionEventHandle actionEventHandle = new ActionEventHandle(node,
        nodeContext);
    node.add(ActionEvent.class, actionEventHandle);

    NodeInitor nodeInitor = new NodeInitor(nodeContext, node);
    node.add(NormalOpenEvent.class, nodeInitor);
    node.add(RelationViewEvent.class, nodeInitor);
    
    return node;
  }
  
  //chenjianhua   2013-01-17
  public void setUIButtons(ButtonObject[] buttons) {
	    if(buttons!=null){
	    	ButtonObject[] refBtns=buttons[2].getChildButtonGroup();    	
	    	if(refBtns!=null){
	    		//参照采购入库费用按钮可用
	    		//buttons[2].getChildButtonGroup()[2].setEnabled(true);
	    		for(int i=0;i<refBtns.length;i++){
	    			if(refBtns[i].getCode().contains("费用")){
	    				refBtns[i].setEnabled(true);
	    			}
	    		}
	    	}
	    	
	    }
	    super.setButtons( buttons );
  }
  
  //end 2013-01-17

}
