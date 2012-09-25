/**
 * $�ļ�˵��$
 *
 * @author yangb
 * @version 
 * @see
 * @since
 * @time 2008-8-1 ����08:59:44
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
 * <b>������Ҫ������¹��ܣ�</b>
 * 
 * <ul>
 *  <li>������Ŀ1
 *  <li>������Ŀ2
 *  <li>...
 * </ul> 
 *
 * <p>
 * <b>�����ʷ����ѡ����</b>
 * <p>
 * 	 XXX�汾����XXX��֧�֡�
 * <p>
 * <p>
 * @version ���汾��
 * @since ��һ�汾��
 * @author yangb
 * @time 2008-8-1 ����08:59:44
 */
public class ICBillRefListPanel extends BillRefListPanel implements ISourceQuery{
  
  private ICSourceBillToBillBaseUI parentPanel ;
  //private AggregatedValueObject[] querybillvos ;

  /**
   * ICBillRefListPanel �Ĺ�����
   * @param sourcetype
   * @param targettype
   * @param pk_corp
   */
  public ICBillRefListPanel(ICSourceBillToBillBaseUI pl){
    // TODO �Զ����ɹ��캯�����
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
    // TODO �Զ����ɷ������
    
    AggregatedValueObject[] retvos = null;
    if((BillTypeConst.m_allocationOut.equals(getCsourcetype()) || BillTypeConst.m_saleOut.equals(getCsourcetype()) ) 
        && "4804".equals(getCtargettype())  ){
      if(getParentPanel().getQueryDlg().showModal()==UIDialog.ID_OK){
        try {
          //���ִ���
            //���ز�ѯ����
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
   * ������ѯ�Ի��������ȷ����ִ��ҵ���ѯ������������true������ֱ�ӷ���false
   * @param attributes Ϊ��ǰ��ѯ����һЩ�������ԡ����磺��ǰ��������֯����ǰ��
   *         �������͵�
   * @return boolean
   */
  public boolean query(Map attributes){
	  //lumzh 2012-09-25 ��������Ϊ4A�����
    if((BillTypeConst.m_allocationOut.equals(getCsourcetype()) || BillTypeConst.m_saleOut.equals(getCsourcetype())|| BillTypeConst.m_otherIn.equals(getCsourcetype())) 
        && "4804".equals(getCtargettype())  ){
      if(getParentPanel().getQueryDlg(attributes).showModal()==UIDialog.ID_OK){
          getParentPanel().setBills(null);
          try {
            getParentPanel().getQueryDlg().saveUserBillRefModeSel();
            getParentPanel().switchShow(getParentPanel().getQueryDlg().isShowDoubleTableRef()?
                BillToBillRefPanel.ShowState.DoubleTable:BillToBillRefPanel.ShowState.OneTable);
            //���ִ���
            //���ز�ѯ����
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
   * �õ���ǰ��ѯ������Ӻ�̨���صĵ���
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
    // TODO �Զ����ɷ������
    if(getParentPanel()!=null)
      getParentPanel().setBills(null);
    super.setSourceVOToUI(srcHeadVOs, srcBodyVOs);
  }
  
  /**
   * ��ȡ���յ���ģ��Ľ���
   */
  @Override
  public String getRefNodeCode() {
    // TODO �Զ����ɷ������
    String cnodekey = "REFTEMPLET";
    if(BillTypeConst.m_allocationOut.equals(getCsourcetype()) && "4804".equals(getCtargettype())){
      cnodekey = "4YREF4804";
    }else if(BillTypeConst.m_saleOut.equals(getCsourcetype()) && "4804".equals(getCtargettype())){
      cnodekey = "4CREF4804";
    }
    return cnodekey;
  }
  
  /**
   * ��ȡ���յ���ģ��Ľ���
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
   * ����Դ�������õ�������ʾ��
   */
  protected void setBodyVOToUI( CircularlyAccessibleValueObject[] srcBodyVOs){
    
    if(srcBodyVOs!=null && srcBodyVOs.length>=0){
      setBodyValueVO(srcBodyVOs);
      //Ĭ�ϰ���ͷ���� ������ 090924
      getBodyBillModel().sortByColumn("crowno", true);
      getBodyBillModel().execLoadFormula();
    }else{
      getBodyBillModel().clearBodyData();
    }
  }
  

}
