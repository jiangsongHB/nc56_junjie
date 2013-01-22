package nc.ui.dm.dm220;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import nc.ui.dm.dm220.event.init.LoadFormulaInitor;
import nc.ui.scm.pattern.ctrl.card.CardCtrl;
import nc.ui.scm.pattern.tool.InvInfoTool;
import nc.vo.dm.model.delivbill.entity.DelivBillHeadVO;
import nc.vo.dm.model.delivbill.entity.DelivBillItemVO;
import nc.vo.dm.model.delivbill.entity.DelivBillPackItemVO;
import nc.vo.dm.model.delivbill.entity.DelivBillVO;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.SuperVOUtil;
import nc.vo.scm.pattern.domain.scm.enumeration.FBillStatusFlag;
import nc.vo.scm.pattern.domain.uap.enumeration.NCBillType;
import nc.vo.scm.pattern.model.entity.vo.ISmartVO;
import nc.vo.scm.pattern.model.entity.vo.SmartVO;
import nc.vo.scm.pattern.model.entity.vo.SmartVOStatus;
import nc.vo.scm.pattern.model.meta.entity.vo.ISmartVOMeta;
import nc.vo.scm.pub.SCMEnv;

/**
 * @author 钟鸣
 *
 * 2008-10-12 上午10:04:48
 */
public class DelivBillVirtualFiledLoader {
  private NodeContext nodeContext = null;
  private CardCtrl card;
  private boolean executeAllFormulas = true;
  private java.util.Set<String> changedAttributes = new java.util.LinkedHashSet<String>();

  public DelivBillVirtualFiledLoader(
      NodeContext nodeContext) {
    this.nodeContext = nodeContext;
  }
  
  public void setCardCtrl(CardCtrl card) {
    this.card = card;
  }

  public void load(DelivBillVO[] bills) {
    List<DelivBillHeadVO> headList = new ArrayList<DelivBillHeadVO>();
    List<DelivBillItemVO> invList = new ArrayList<DelivBillItemVO>();
    List<DelivBillPackItemVO> packList = new ArrayList<DelivBillPackItemVO>();

    for (DelivBillVO bill : bills) {
      headList.add(bill.getHead());
      DelivBillItemVO[] vos = bill.getInvBodys();
      if (vos != null) {
        invList.addAll(Arrays.asList(vos));
      }
      DelivBillPackItemVO[] packVOs = bill.getPackBodys();
      if (packVOs != null) {
        packList.addAll(Arrays.asList(packVOs));
      }
    }
    int size = headList.size();
    DelivBillHeadVO[] vos = new DelivBillHeadVO[size];
    vos = headList.toArray(vos);
    this.loadHead(vos);

    size = invList.size();
    DelivBillItemVO[] bodys = new DelivBillItemVO[size];
    bodys = invList.toArray(bodys);
    this.loadInvBody(bodys);

    size = packList.size();
    DelivBillPackItemVO[] packBodys = new DelivBillPackItemVO[size];
    packBodys = packList.toArray(packBodys);
    this.loadPackBody(packBodys);
  }
  
  /**
   * 
   * 方法功能：把sourceBills的属性，复制到targetBills中，不会覆盖targetBills中的已有属性<br>
   * 注意：两个参数的数组长度以及表体的长度和顺序必须一致<br>
   * <p>
   * @param sourceBills 源VO
   * @param targetBills 目标VO
   * <p>
   * @author duy
   * @time 2009-8-20 下午04:45:25
   */
  public void load(DelivBillVO[] sourceBills, DelivBillVO[] targetBills) {
    executeAllFormulas = false;
    int billNum = sourceBills.length;
    for (int i = 0; i < billNum; i++) {
      load(sourceBills[i], targetBills[i]);
    }
    load(targetBills);
  }
  
  private void load(DelivBillVO sourceBill, DelivBillVO targetBill) {
    java.util.List<CircularlyAccessibleValueObject> srcs = new java.util.ArrayList<CircularlyAccessibleValueObject>();
    srcs.add(sourceBill.getParentVO());
    SmartVO[] childs = (SmartVO[])sourceBill.getInvBodys();
    for (SmartVO child : childs){
      //add by hanbin 2009-12-3 原因：如果是删除状态的，则跳过
      if(child.getVOStatus() == SmartVOStatus.DELETED)
        continue;
      
      srcs.add(child);
    }
    childs = sourceBill.getPackBodys();
    for (SmartVO child : childs){
      //add by hanbin 2009-12-3 原因：如果是删除状态的，则跳过
      if(child.getVOStatus() == SmartVOStatus.DELETED)
        continue;
      
      srcs.add(child);
    }
    
    java.util.List<CircularlyAccessibleValueObject> targets = new java.util.ArrayList<CircularlyAccessibleValueObject>();
    targets.add(targetBill.getParentVO());
    childs = targetBill.getInvBodys();
    for (SmartVO child : childs){
      //add by hanbin 2009-12-3 原因：如果是删除状态的，则跳过
      if(child.getVOStatus() == SmartVOStatus.DELETED)
        continue;
      
      targets.add(child);
    }
    childs = targetBill.getPackBodys();
    for (SmartVO child : childs){
      //add by hanbin 2009-12-3 原因：如果是删除状态的，则跳过
      if(child.getVOStatus() == SmartVOStatus.DELETED)
        continue;
      
      targets.add(child);
    }
    
    // 复制过程
    int size = srcs.size();
    for (int i = 0; i < size; i++)
      load(srcs.get(i), targets.get(i));
  }
  
  private void load(CircularlyAccessibleValueObject src,
      CircularlyAccessibleValueObject target) {
    
    if(src == null || target == null){
      SCMEnv.out("DelivBillVirtualFiledLoader.load方法：参数非法，存在null");
      return;
    }
    String[] attrs = src.getAttributeNames();
    Object srcObj, targetObj;
    for (String attr : attrs) {
      srcObj = src.getAttributeValue(attr);
      targetObj = target.getAttributeValue(attr);
      if (targetObj == null && srcObj != null) {
        target.setAttributeValue(attr, srcObj);
        changedAttributes.add(attr);
      }
      if (targetObj != null && !targetObj.equals(srcObj)) {
        changedAttributes.add(attr);
      }
    }
  }

  public void loadHead(DelivBillHeadVO[] vos) {
    for (DelivBillHeadVO head : vos) {
      FBillStatusFlag status = head.getFstatusflag();
      head.setCstatusflagname(status.getName());
      String cdelivtype = head.getCdelivtype();
      String transactionTypeName = this.nodeContext
          .getTransactionTypeName(cdelivtype);
      head.setCdelivtypename(transactionTypeName);
    }
    this.loadFromFormula(vos);
  }

  public void loadInvBody(DelivBillItemVO[] vos) {
    this.loadFromFormula(vos);
    for (DelivBillItemVO item : vos) {
      NCBillType csourcebilltypecode = item.getCsourcebilltypecode();
      if (csourcebilltypecode != null) {
        item.setCsourcebilltypecodename(csourcebilltypecode.getName());
      }

      NCBillType cfirstbilltypecode = item.getCfirstbilltypecode();
      if (cfirstbilltypecode != null) {
        item.setCfirstbilltypecodename(cfirstbilltypecode.getName());
      }
      String cinvbasid = item.getCinvbasid();
      String vfree1 = item.getVfree1();
      String vfree2 = item.getVfree2();
      String vfree3 = item.getVfree3();
      String vfree4 = item.getVfree4();
      String vfree5 = item.getVfree5();
      if( vfree1 == null && vfree2 == null && vfree3 == null 
          && vfree4 == null && vfree5 == null){
        continue;
      }
      String[] frees = InvInfoTool.getInstance().getFreeItem(cinvbasid);
      //chenjianhua 2013-01-22
      if(frees==null||frees.length==0){
    	  continue;
      }
      //end 2013-01-22
      StringBuffer buffer = new StringBuffer();
      for(int i=0;i<5;i++){
        if( frees[i] == null ){
          continue;
        }
        String value = (String) item.getAttributeValue( "vfree"+(i+1) );
        buffer.append( "[");
        buffer.append( frees[i+5]);
        buffer.append(":");
        buffer.append( value);
        buffer.append("]");
      }
      item.setVfree0( buffer.toString() );
    }
  }

  public void loadPackBody(DelivBillPackItemVO[] vos) {
    this.loadFromFormula(vos);
  }

  private void loadFromFormula(ISmartVO[] vos) {
    if (vos.length == 0) {
      return;
    }
    
    if (!executeAllFormulas && changedAttributes.size() == 0)
      return;
    
    List<String> formulaList = new ArrayList<String>();
    LoadFormulaInitor initor = new LoadFormulaInitor();
    if (card != null)
      initor.setFormula(card);
    Class<? extends ISmartVOMeta> clazz = vos[0].getVOMeta().getClass();
    Map<String, List<String>> index = initor.getFomula(clazz);
    
    if (executeAllFormulas) {
      for (List<String> list : index.values()) {
        formulaList.addAll(list);
      }
    }
    else {
      for (String changedAttribute : changedAttributes) {
        if (index.containsKey(changedAttribute))
          formulaList.addAll(index.get(changedAttribute));
      }
    }
    int size = formulaList.size();
    String[] formulas = new String[size];
    formulas = formulaList.toArray(formulas);
    SuperVOUtil.execFormulaWithVOs((CircularlyAccessibleValueObject[]) vos,
        formulas, null);
  }
}
