package nc.ui.ic.pub.pf;

import java.awt.CardLayout;
import java.awt.Container;
import java.awt.LayoutManager;
import java.util.Map;

import javax.swing.JComponent;

import nc.ui.ic.pub.tools.GenMethod;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.bill.BillScrollPane;
import nc.ui.pub.query.QueryConditionClient;
import nc.ui.scm.pub.redun.RedunTool;
import nc.ui.scm.pub.redunmulti.ISourceQuery;
import nc.ui.scm.pub.sourceref.BillRefListPanel;
import nc.ui.scm.pub.sourceref.BillToBillRefPanel;
import nc.ui.scm.pub.sourceref.DefaultSrcRefCtl;
import nc.ui.scm.pub.sourceref.SimpBillRefListPanel;
import nc.vo.ic.pub.BillTypeConst;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.bill.BillTempletVO;
import nc.vo.scm.pub.OnHandRefreshVO;

public class ICSourceBillToBillBaseUI extends BillToBillRefPanel  implements ISourceQuery{
  
  private String csourcebilltype;
  private String ctargerbilltype;
  private String pk_corp;
  private String cuserid;
  private String cbiztype;
  private Container contain;
  private AggregatedValueObject[] querybillvos ;
  
  
  private ICBillReferQueryProxy  qrydlg;

  public ICSourceBillToBillBaseUI(String biztype,String sourcebilltype,String targerbilltype,String pk_corp,String userid) {
    // TODO �Զ����ɹ��캯�����
    super();
    this.csourcebilltype = sourcebilltype;
    this.ctargerbilltype = targerbilltype;
    this.pk_corp = pk_corp;
    this.cuserid = userid;
    this.cbiztype = biztype;
    init();
  }
  
  public ICSourceBillToBillBaseUI(Container contain,String biztype,String sourcebilltype,String targerbilltype,String pk_corp,String userid) {
    // TODO �Զ����ɹ��캯�����
    super();
    this.csourcebilltype = sourcebilltype;
    this.ctargerbilltype = targerbilltype;
    this.pk_corp = pk_corp;
    this.cuserid = userid;
    this.cbiztype = biztype;
    this.contain = contain;
    init();
  }

  public void init() {
        
    // TODO �Զ����ɹ��캯�����
  }
  
  /**
   * ���ñ�ѡ���з�ʽ
   */
  public void setBillTableSelectMode(BillScrollPane pane) {
    if (pane==null)
      return;
    //pane.getTable().setCellSelectionEnabled(false);
    pane.getTable().setRowSelectionAllowed(true);

    pane.getTable().setColumnSelectionAllowed(false);

    pane.getTable().setSelectionMode(

             javax.swing.ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
  }
  
  protected BillRefListPanel createBillRefListPanel() {
    //���䵥   ���۳��⵽
    ICBillRefListPanel icbillrefpanel = new ICBillRefListPanel(this);
    String cnodekey = icbillrefpanel.getRefNodeCode();
    String pk_billtypecode = icbillrefpanel.getRefTempletBilltypecode();
    if(StringUtil.isEmpty(pk_billtypecode))
      pk_billtypecode = icbillrefpanel.getBillType();
    BillTempletVO tvo = icbillrefpanel.getDefaultTemplet(pk_billtypecode, getCbiztype(),
        getCuserid(), getPk_corp(),cnodekey);
    icbillrefpanel.createBillListPanel(tvo);
    setBillTableSelectMode(icbillrefpanel.getParentListPanel());
    setBillTableSelectMode(icbillrefpanel.getChildListPanel());
    return icbillrefpanel;
  }
  
  protected SimpBillRefListPanel createSimpBillRefListPanel() {
    // TODO �Զ����ɹ��캯�����
    ICSimpBillRefListPanel icbillrefpanel = new ICSimpBillRefListPanel(this);
    String cnodekey = icbillrefpanel.getRefNodeCode();
    String pk_billtypecode = icbillrefpanel.getRefTempletBilltypecode();
    if(StringUtil.isEmpty(pk_billtypecode))
      pk_billtypecode = icbillrefpanel.getBillType();
    BillTempletVO tvo = icbillrefpanel.getDefaultTemplet(pk_billtypecode, getCbiztype(),
        getCuserid(), getPk_corp(),cnodekey);
    icbillrefpanel.createBillListPanel(tvo);
    setBillTableSelectMode(icbillrefpanel.getParentListPanel());
    return icbillrefpanel;
  }

  public String getCsourcebilltype() {
    return csourcebilltype;
  }

  public String getCtargerbilltype() {
    return ctargerbilltype;
  }

  public String getPk_corp() {
    return pk_corp;
  }
  
  public String getCuserid() {
    return cuserid;
  }

  public String getCbiztype() {
    return cbiztype;
  }
  
  
  public ICBillReferQueryProxy getQueryDlg() {
    if(qrydlg==null){
      qrydlg = new ICBillReferQueryProxy(this.contain);
      if( (
            BillTypeConst.m_allocationOut.equals(getCsourcebilltype()) ||
            BillTypeConst.m_saleOut.equals(getCsourcebilltype())
          )
         && "4804".equals(getCtargerbilltype())){
          try{
            qrydlg.initData(getPk_corp(), getCuserid(), 
                nc.vo.ic.pub.GenMethod.getNodeCodeByBillType(getCsourcebilltype()), 
                null, getCtargerbilltype(), getCsourcebilltype(), null, null);
          }catch(Exception e){
            GenMethod.handleException(null, null, e);
          }
      }
    }
    return qrydlg;
  }
  
  public ICBillReferQueryProxy getQueryDlg(Map attributes) {
    if(qrydlg==null){
      qrydlg = new ICBillReferQueryProxy(this.contain);
     //lumzh 2012-09-25 ��������Ϊ4A�����
      if( (
            BillTypeConst.m_allocationOut.equals(getCsourcebilltype()) ||
            BillTypeConst.m_saleOut.equals(getCsourcebilltype())
               ||    BillTypeConst.m_otherIn.equals(getCsourcebilltype())
          )
         && "4804".equals(getCtargerbilltype())){
          try{
            qrydlg.initData(getPk_corp(), getCuserid(), 
                nc.vo.ic.pub.GenMethod.getNodeCodeByBillType(getCsourcebilltype()), 
                null, getCtargerbilltype(), getCsourcebilltype(), null, attributes);
          }catch(Exception e){
            GenMethod.handleException(null, null, e);
          }
      }
    }
    return qrydlg;
  }
  
  /**
   * ������ѯ�Ի��������ȷ����ִ��ҵ���ѯ������������true������ֱ�ӷ���false
   * @param attributes Ϊ��ǰ��ѯ����һЩ�������ԡ����磺��ǰ��������֯����ǰ��
   *         �������͵�
   * @return boolean
   */
  public boolean query(Map attributes){
    return ((ISourceQuery)getCurBillListPanel()).query(attributes);
  }

  /**
   * �õ���ǰ��ѯ������Ӻ�̨���صĵ���
   * @return AggregatedValueObject[]
   */
  public AggregatedValueObject[] getBills(){
    return this.querybillvos;
  }
  
  /**
   */
  protected AggregatedValueObject[] setBills(AggregatedValueObject[] billvos){
    return this.querybillvos = billvos;
  }

  @Override
  public ISourceQuery getISourceQuery() {
    // TODO �Զ����ɷ������
    return this;
  }
  
//  public DefaultSrcRefCtl getSourcectl() {
//    if(sourcectl==null){
//      sourcectl = new ICDefaultSrcRefCtl(getCbiztype(),getCsourcetype(),getCtargettype(),getPk_corp(),this);
//    }
//    return sourcectl;
//  }

}

  
