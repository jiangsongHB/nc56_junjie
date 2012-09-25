/**
 * $文件说明$
 *
 * @author yangb
 * @version 
 * @see
 * @since
 * @time 2008-8-1 上午08:59:44
 */
package nc.ui.ic.pub.pf;


import java.util.Map;

import nc.ui.ic.pub.tools.GenMethod;
import nc.ui.pub.beans.UIDialog;
import nc.ui.scm.pub.redun.RedunTool;
import nc.ui.scm.pub.redunmulti.ISourceQuery;
import nc.ui.scm.pub.sourceref.BillRefListPanel;
import nc.ui.scm.pub.sourceref.BillToBillRefPanel;
import nc.ui.scm.pub.sourceref.DefaultSrcRefCtl;
import nc.vo.ic.pub.BillTypeConst;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;

/**
 * <p>
 * <b>本类主要完成以下功能：</b>
 * 
 * <ul>
 *  <li>功能条目1
 *  <li>功能条目2
 *  <li>...
 * </ul> 
 *
 * <p>
 * <b>变更历史（可选）：</b>
 * <p>
 * 	 XXX版本增加XXX的支持。
 * <p>
 * <p>
 * @version 本版本号
 * @since 上一版本号
 * @author yangb
 * @time 2008-8-1 上午08:59:44
 */
public class ICBillRefListPanel extends BillRefListPanel implements ISourceQuery{
  
  private ICSourceBillToBillBaseUI parentPanel ;
  //private AggregatedValueObject[] querybillvos ;

  /**
   * ICBillRefListPanel 的构造子
   * @param sourcetype
   * @param targettype
   * @param pk_corp
   */
  public ICBillRefListPanel(ICSourceBillToBillBaseUI pl){
    // TODO 自动生成构造函数存根
    super(pl.getCbiztype(),pl.getCsourcebilltype(), 
       pl.getCtargerbilltype(), pl.getPk_corp());
    parentPanel = pl;
  }
  
  public ICBillRefListPanel(String biztype,
      String sourcetype, String targettype, String pk_corp) {
    super(biztype,sourcetype,targettype, pk_corp);
  }

  @Override
  public DefaultSrcRefCtl getSourcectl() {
    if(sourcectl==null){
      sourcectl = new ICDefaultSrcRefCtl(getCbiztype(),getCsourcetype(),getCtargettype(),getPk_corp(),this);
    }
    return sourcectl;
  }


  @Override
  public AggregatedValueObject[] queryAllBillDatas() throws BusinessException {
    // TODO 自动生成方法存根
    
    AggregatedValueObject[] retvos = null;
    if((BillTypeConst.m_allocationOut.equals(getCsourcetype()) || BillTypeConst.m_saleOut.equals(getCsourcetype()) ) 
        && "4804".equals(getCtargettype())  ){
      if(getParentPanel().getQueryDlg().showModal()==UIDialog.ID_OK){
        try {
          //清现存量
            //返回查询条件
            String swhereStr = getParentPanel().getQueryDlg().getWhereSQL();
            
            Object[] reObjects = ((ICDefaultSrcRefCtl)getSourcectl()).queryAllData(swhereStr,getParentPanel().getQueryDlg().getAndConditionVO());
            
            if(reObjects!=null && reObjects[0]!=null && ((Object[])reObjects[0]).length>0){
              
              setSourceVOToUI((CircularlyAccessibleValueObject[])reObjects[0],
                  (CircularlyAccessibleValueObject[])reObjects[1]
                );
              if(reObjects.length>=3 && reObjects[2]!=null && reObjects[2].toString().trim().length()>0)
                showHintMessage(reObjects[3].toString());
            }else{
              clearUIData();
            }
          } catch (Exception e) {
            throw GenMethod.handleException(null, null, e);
          }  
      }
    }else{
      super.queryAllBillDatas();
    }
    return retvos;
  }

  public ICSourceBillToBillBaseUI getParentPanel() {
    return parentPanel;
  }
  
  /**
   * 弹出查询对话框，如果点确定，执行业务查询操作，并返回true，否则，直接返回false
   * @param attributes 为当前查询增加一些附加属性。例如：当前的运输组织，当前的
   *         运输类型等
   * @return boolean
   */
  public boolean query(Map attributes){
	  //lumzh 2012-09-25 增加类型为4A的情况
    if((BillTypeConst.m_allocationOut.equals(getCsourcetype()) || BillTypeConst.m_saleOut.equals(getCsourcetype())|| BillTypeConst.m_otherIn.equals(getCsourcetype())) 
        && "4804".equals(getCtargettype())  ){
      if(getParentPanel().getQueryDlg(attributes).showModal()==UIDialog.ID_OK){
          getParentPanel().setBills(null);
          try {
            getParentPanel().getQueryDlg().saveUserBillRefModeSel();
            getParentPanel().switchShow(getParentPanel().getQueryDlg().isShowDoubleTableRef()?
                BillToBillRefPanel.ShowState.DoubleTable:BillToBillRefPanel.ShowState.OneTable);
            //清现存量
            //返回查询条件
            String swhereStr = getParentPanel().getQueryDlg().getWhereSQL();
            
            Object[] reObjects = ((ICDefaultSrcRefCtl)getSourcectl()).queryAllData(swhereStr,
                new Object[]{getParentPanel().getQueryDlg().getAndConditionVO(),
                attributes
                });
            
            if(reObjects!=null && reObjects[0]!=null && ((Object[])reObjects[0]).length>0){
              if(reObjects.length>=3 && reObjects[2]!=null && reObjects[2].toString().trim().length()>0)
                showHintMessage(reObjects[2].toString());
              getParentPanel().setBills(
                  RedunTool.getBillVos(getSourcectl().getSourceVoClassName(), 
                  getSourcectl().getSourcevoPkname(), (CircularlyAccessibleValueObject[])reObjects[0], 
                  (CircularlyAccessibleValueObject[])reObjects[1])
              );
            }else{
              clearUIData();
            }
          } catch (Exception e) {
            GenMethod.handleException(null, null, e);
          }
          return true; 
      }
    }
    return false;
  }

  /**
   * 得到当前查询操作后从后台返回的单据
   * @return AggregatedValueObject[]
   */
  public AggregatedValueObject[] getBills(){
    if(getParentPanel()!=null)
      return getParentPanel().getBills();
    return null;
  }
  
  /**
   * ISourceQuery
   * @param mode
   */
  public ISourceQuery getISourceQuery(){
    return this;
  }

  @Override
  public void setSourceVOToUI(CircularlyAccessibleValueObject[] srcHeadVOs, CircularlyAccessibleValueObject[] srcBodyVOs) throws BusinessException {
    // TODO 自动生成方法存根
    if(getParentPanel()!=null)
      getParentPanel().setBills(null);
    super.setSourceVOToUI(srcHeadVOs, srcBodyVOs);
  }
  
  /**
   * 获取参照单据模板的结点号
   */
  @Override
  public String getRefNodeCode() {
    // TODO 自动生成方法存根
    String cnodekey = "REFTEMPLET";
    if(BillTypeConst.m_allocationOut.equals(getCsourcetype()) && "4804".equals(getCtargettype())){
      cnodekey = "4YREF4804";
    }else if(BillTypeConst.m_saleOut.equals(getCsourcetype()) && "4804".equals(getCtargettype())){
      cnodekey = "4CREF4804";
    }
    return cnodekey;
  }
  
  /**
   * 获取参照单据模板的结点号
   */
  public String getRefTempletBilltypecode(){
    String cnodekey = getCsourcetype();
//    if(BillTypeConst.m_allocationOut.equals(getCsourcetype()) && "4804".equals(getCtargettype())){
//      cnodekey = "4YREF4804";
//    }else if(BillTypeConst.m_saleOut.equals(getCsourcetype()) && "4804".equals(getCtargettype())){
//      cnodekey = "4CREF4804";
//    }
    return cnodekey;
  }
  
  /**
   * 将来源单据设置到表体显示。
   */
  protected void setBodyVOToUI( CircularlyAccessibleValueObject[] srcBodyVOs){
    
    if(srcBodyVOs!=null && srcBodyVOs.length>=0){
      setBodyValueVO(srcBodyVOs);
      //默认按表头排序 陈倪娜 090924
      getBodyBillModel().sortByColumn("crowno", true);
      getBodyBillModel().execLoadFormula();
    }else{
      getBodyBillModel().clearBodyData();
    }
  }
  

}
