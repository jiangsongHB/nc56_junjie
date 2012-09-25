package nc.bs.dm.dm220.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import nc.bs.dm.dm220.exchange.CustomerExchange;
import nc.bs.dm.dm220.exchange.InventoryExchange;
import nc.bs.dm.dm220.exchange.MoneyExchange;
import nc.bs.dm.dm220.exchange.NumberExchange;
import nc.bs.dm.dm220.exchange.ProjectExchange;
import nc.bs.dm.dm220.exchange.ProviderExchange;
import nc.bs.dm.dm220.exchange.SendAddressExchange;
import nc.bs.dm.dm220.exchange.SendAreaExchange;
import nc.bs.dm.dm220.exchange.SendDetailAddressExchange;
import nc.bs.dm.dm220.exchange.VolumnWeightExchange;
import nc.bs.dm.pub.context.DMContext;
import nc.bs.pub.pf.PfUtilTools;
import nc.bs.scm.pattern.data.DataAccessUtils;
import nc.bs.scm.pattern.data.TempTableDefine;
import nc.bs.scm.pattern.data.bill.action.IBillProcess;
import nc.bs.scm.pattern.data.table.TableIDQueryCondition;

import nc.vo.dm.dm220.BillViewVO;
import nc.vo.dm.model.delivbill.entity.DelivBillHeadVO;
import nc.vo.dm.model.delivbill.entity.DelivBillItemVO;
import nc.vo.dm.model.delivbill.entity.DelivBillVO;
import nc.vo.ic.pub.bill.GeneralBillHeaderVO;
import nc.vo.ic.pub.bill.GeneralBillItemVO;
import nc.vo.ic.pub.bill.GeneralBillVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.scm.pattern.data.IRowSet;
import nc.vo.scm.pattern.domain.uap.enumeration.NCBillType;
import nc.vo.scm.pattern.exception.ExceptionUtils;
import nc.vo.scm.pattern.pub.SqlBuilder;
import nc.vo.so.soreceive.SaleReceiveBVO;
import nc.vo.so.soreceive.SaleReceiveHVO;
import nc.vo.so.soreceive.SaleReceiveVO;

/**
 * 外部单据通过VO交换形成运输单
 * @author 钟鸣
 *
 * 2008-11-11 上午10:03:47
 */
public class DelivBillExchangeAction {
  private DMContext context = null;
  
  private List<IBillProcess<DelivBillVO>> list = new ArrayList<IBillProcess<DelivBillVO>>();
  
  private Map<String,String> cinvclidIndex = new HashMap<String,String>();
  
  public DelivBillExchangeAction(DMContext context){
    this.context = context;
    list.add( new InventoryExchange(this.context));
    list.add( new NumberExchange(this.context));
    list.add( new MoneyExchange(this.context));
    list.add( new VolumnWeightExchange(this.context));
    list.add( new ProjectExchange(this.context));
    list.add( new SendAddressExchange(this.context));
    list.add( new SendDetailAddressExchange(this.context));
    list.add( new SendAreaExchange(this.context));
    list.add( new ProviderExchange(this.context));
    list.add( new CustomerExchange(this.context));
  }

  public BillViewVO[] exchange(AggregatedValueObject[] bills,
      String sourcebilltypecode,String targetbilltypecode){
    DelivBillVO[] dmbills = null;
    AggregatedValueObject[] sourceBills = bills;
    //发货单的承运商、车型、车辆、司机  押运员在表体，需要提前按行分单处理
    if( sourcebilltypecode.equals( NCBillType.SO_4331.value())){
      sourceBills = this.split((SaleReceiveVO[])bills);
    }
    //库存调拨出库单、库存销售出库单有可能来源于发货单，先提前分单处理，好处理发货单
    //的承运商、车型、车辆、司机、押运员。杨波就是不听话，非要我跑到发货单取。他自己
    //逍遥了。
    //lumzh2012-09-25 增加类型为4A的情况
    else if( sourcebilltypecode.equals( NCBillType.IC_4C.value())
              || sourcebilltypecode.equals( NCBillType.IC_4Y.value())|| sourcebilltypecode.equals( NCBillType.IC_4A.value())){
      sourceBills = this.split((GeneralBillVO[]) bills );
    }
    
    try {
      dmbills = (DelivBillVO[]) PfUtilTools.runChangeDataAry(
          sourcebilltypecode, targetbilltypecode, sourceBills);
    }
    catch (BusinessException ex) {
      ExceptionUtils.getInstance().wrappException(ex);
    }
    for(DelivBillVO bill:dmbills){
      for(IBillProcess<DelivBillVO> process:this.list ){
        process.accept( bill );
      }
    }
    for(IBillProcess<DelivBillVO> process:this.list ){
      process.process();
    }
    List<BillViewVO> list = new ArrayList<BillViewVO>();
    
    BillViewVO[] vos = new BillViewVO[0];
    for(DelivBillVO bill:dmbills){
      DelivBillItemVO[] items = bill.getInvBodys();
      for( DelivBillItemVO item:items){
        DelivBillHeadVO head = (DelivBillHeadVO)bill.getHead().clone();
        BillViewVO vo = new BillViewVO(head,item);
        //设置存货分类——前台分单的时候可能会用到
        this.setCinvclid(vo);
        list.add( vo );
      }
    }    
    vos = list.toArray( vos );
    //设置销售组织——前台分单的时候可能会用到
    this.setSaleCorp( vos );
    return vos;
  }
  
  private void setCinvclid(BillViewVO vo ){
    String cinvbasid = vo.getItem().getCinvbasid();
    String cinvclid = null;
    if( this.cinvclidIndex.containsKey( cinvbasid )){
      cinvclid = this.cinvclidIndex.get( cinvbasid );
    }
    else{
      SqlBuilder sql = new SqlBuilder();
      sql.append(" select pk_invcl from bd_invbasdoc where ");
      sql.append(" pk_invbasdoc",cinvbasid );
      sql.append(" and ");
      sql.startParentheses();
      sql.append(" dr=0 or dr is null ");
      sql.endParentheses();
      IRowSet rowset = DataAccessUtils.getInstance().query( sql.toString() );
      if( rowset.next() ){
        cinvclid = rowset.getString( 0 );
      }
      this.cinvclidIndex.put(cinvbasid, cinvclid);
    }
    vo.setCinvclid( cinvclid );
  }
  
  
  private SaleReceiveVO[] split(SaleReceiveVO[] bills){
    List<SaleReceiveVO> list = new ArrayList<SaleReceiveVO>();
    for(SaleReceiveVO bill:bills){
      SaleReceiveHVO head = bill.getHead();
      SaleReceiveBVO[] items = bill.getItems();
      for(SaleReceiveBVO item:items ){
        SaleReceiveVO vo = new SaleReceiveVO();
        vo.setParentVO( head );
        SaleReceiveBVO[] children = new SaleReceiveBVO[]{item};
        vo.setChildrenVO( children );
        list.add( vo );
      }
    }
    int size = list.size();
    SaleReceiveVO[] vos = new SaleReceiveVO[size];
    vos = list.toArray( vos );
    return vos;
  }
  
  private GeneralBillVO[] split(GeneralBillVO[] bills){
    List<GeneralBillVO> list = new ArrayList<GeneralBillVO>();
    for(GeneralBillVO bill:bills){
      GeneralBillHeaderVO head = (GeneralBillHeaderVO)bill.getParentVO();
      GeneralBillItemVO[] items = (GeneralBillItemVO[])bill.getChildrenVO();
      for(GeneralBillItemVO item:items ){
        GeneralBillVO vo = new GeneralBillVO();
        vo.setParentVO( head );
        GeneralBillItemVO[] children = new GeneralBillItemVO[]{item};
        vo.setChildrenVO( children );
        list.add( vo );
      }
    }
    int size = list.size();
    GeneralBillVO[] vos = new GeneralBillVO[size];
    vos = list.toArray( vos );
    return vos;
  }
  
  private void setSaleCorp(BillViewVO[] vos){
    Set<String> set = new HashSet<String>();
    for(BillViewVO vo:vos){
      NCBillType cfirstbilltypecode = vo.getItem().getCfirstbilltypecode();
      String cfirstbillid = vo.getItem().getCfirstbillid();
      if( cfirstbilltypecode != null 
          && cfirstbilltypecode.equals( NCBillType.SO_30)){
        set.add( cfirstbillid );
      }
    }
    int size = set.size();
    if( size ==0 ){
      return;
    }
    String[] ids = new String[size];
    ids = set.toArray( ids );
    Map<String,String> index = this.loadSaleCorpFromDB(ids);
    for(BillViewVO vo:vos){
      NCBillType cfirstbilltypecode = vo.getItem().getCfirstbilltypecode();
      String cfirstbillid = vo.getItem().getCfirstbillid();
      if( cfirstbilltypecode != null 
          && cfirstbilltypecode.equals( NCBillType.SO_30)){
        String csalecorpid = index.get( cfirstbillid );
        vo.setCsalecorpid( csalecorpid );
      }
    }
  }
  
  private Map<String,String> loadSaleCorpFromDB(String[] ids){
    Map<String,String> index = new HashMap<String,String>();
    SqlBuilder sql = new SqlBuilder();
    sql.append(" select csaleid,csalecorpid from so_sale ");
    if( ids.length <= TableIDQueryCondition.MAX_IN_COUNT){
      sql.append(" where ");
      sql.append(" csaleid", ids);
    }
    else{
      String table = TempTableDefine.getInstance().get( ids );
      sql.append(" , ");
      sql.append(table);
      sql.append(" temp  where csaleid=temp.id");
    }
    sql.append(" and dr=0 ");
    
    IRowSet rowset = DataAccessUtils.getInstance().query( sql.toString() );
    while(rowset.next()){
      String csaleid = rowset.getString( 0 );
      String csalecorpid = rowset.getString( 1 );
      index.put(csaleid, csalecorpid);
    }
    return index;
  }
  
}
