package nc.ui.dm.dm220.ref;

import java.awt.Container;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import nc.ui.dm.boundary.ic.IC4ATo4804UI;
import nc.ui.dm.boundary.ic.IC4CTo4804UI;
import nc.ui.dm.boundary.ic.IC4YTo4804UI;
import nc.ui.dm.boundary.pu.PU21To4804UI;
import nc.ui.dm.boundary.so.SO4431To4804UI;
import nc.ui.dm.dm210.service.DM4802To4804UI;
import nc.ui.dm.dm220.NodeContext;
import nc.ui.dm.dm220.ref.ctrl.ISourceUI;

import nc.vo.dm.model.delivbill.entity.DelivBillVO;
import nc.vo.scm.pattern.domain.uap.enumeration.NCBillType;

/**
 * 运输单参照上游的界面入口
 * @author 钟鸣
 *
 * 2008-10-9 下午02:31:34
 */
public class DelivRefBill {
  private Container parent = null;
  
  private NodeContext nodeContext = null;
  
  private DelivRefDialog dialog = null;
  
  private Map<String,ISourceUI> uiIndex = new HashMap<String,ISourceUI>();
  
  public DelivRefBill(Container parent,NodeContext nodeContext){
    this.parent= parent;
    this.nodeContext = nodeContext;
  }
  
  public int  showModal(){
    ISourceUI[] sourceUIs = this.getSourceUIs();
    this.dialog = new DelivRefDialog(this.parent,this.nodeContext,sourceUIs);
    return dialog.showModal();
  }
  
  private ISourceUI[] getSourceUIs(){
    String cdelivtype = this.nodeContext.getCurrentTransactionType();
    Set<String> set = this.nodeContext.getContext().getBillTypeIndex(cdelivtype);
    List<ISourceUI> list = new ArrayList<ISourceUI>();
    String cbilltypecode = NCBillType.SO_4331.value() ;
    if( set.contains( cbilltypecode ) ){
      list.add( this.getSourceUI( cbilltypecode ));
    }
    cbilltypecode = NCBillType.IC_4C.value() ;
    if( set.contains( cbilltypecode ) ){
      list.add( this.getSourceUI( cbilltypecode ));
    }
    cbilltypecode = NCBillType.IC_4Y.value() ;
    if( set.contains( cbilltypecode ) ){
      list.add( this.getSourceUI( cbilltypecode ));
    }
    cbilltypecode = NCBillType.PU_21.value() ;
    if( set.contains( cbilltypecode ) ){
      list.add( this.getSourceUI( cbilltypecode ));
    }
    cbilltypecode = NCBillType.DM_4802.value() ;
    if( set.contains( cbilltypecode ) ){
      list.add( this.getSourceUI( cbilltypecode ));
    }
	  //lumzh 2012-09-25 增加类型为4A的情况
    cbilltypecode = NCBillType.IC_4A.value() ;
    if(set.contains( cbilltypecode )){
      list.add( this.getSourceUI( cbilltypecode ));
    }
    int size = list.size();
    ISourceUI[] uis = new ISourceUI[size];
    uis = list.toArray( uis );
    return uis;
  }
  
  private ISourceUI getSourceUI(String cbilltypecode){
    ISourceUI ui = null;
    if( this.uiIndex.containsKey( cbilltypecode )){
      ui = this.uiIndex.get( cbilltypecode );
    }
    else{
      ui = this.create(cbilltypecode);
    }
    ui.setBills( null );
    return ui;
  }
  
  private ISourceUI create(String cbilltypecode ){
    ISourceUI ui = null;
    if( cbilltypecode.equals( NCBillType.IC_4C.value())){
      ui = new IC4CTo4804UI(this.parent);
    }
    else if( cbilltypecode.equals( NCBillType.IC_4Y.value() )){
      ui = new IC4YTo4804UI(this.parent);
    }
    else if( cbilltypecode.equals( NCBillType.DM_4802.value() )){
      ui = new DM4802To4804UI(this.parent);
    }
    else if( cbilltypecode.equals( NCBillType.PU_21.value())){
      ui = new PU21To4804UI(this.parent);
    }
    else if( cbilltypecode.equals( NCBillType.SO_4331.value())){
      ui = new SO4431To4804UI( this.parent );
	  //lumzh 2012-09-25 增加类型为4A的情况
    }else if(cbilltypecode.equals( NCBillType.IC_4A.value())){
      ui =new IC4ATo4804UI( this.parent );	
    }
    this.uiIndex.put(cbilltypecode, ui);
    return ui;
  }
  
  
  public DelivBillVO[] getBills(){
    return this.dialog.getRefUI().getBills();
  }
  
  public String[] getSplitConditions(){
    return this.dialog.getRefUI().getSplitConditions();
  }
}
