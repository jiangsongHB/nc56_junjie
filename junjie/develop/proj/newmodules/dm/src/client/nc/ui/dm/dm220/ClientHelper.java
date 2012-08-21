package nc.ui.dm.dm220;

import nc.bs.framework.common.NCLocator;
import nc.itf.dm.dm220.IDelivBill;
import nc.ui.pub.ClientEnvironment;
import nc.vo.dm.dm220.BillViewVO;
import nc.vo.dm.model.delivbill.entity.DelivBillVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.scm.pattern.exception.ExceptionUtils;
import nc.vo.scm.pub.session.ClientLink;

/**
 * 客户端远程连接工具类
 * @author 钟鸣
 *
 * 2008-9-26 下午08:17:54
 */
public class ClientHelper {
  private String interfaceName = IDelivBill.class.getName();

 

  private static ClientHelper helper = new ClientHelper();

  public static ClientHelper getInstance() {
    return ClientHelper.helper;
  }

  public DelivBillVO[] queryDelivBill(String sql) {
    IDelivBill bo = (IDelivBill) NCLocator.getInstance().lookup(
        this.interfaceName);
    DelivBillVO[] vos = null;
    ClientLink clientLink = new ClientLink(ClientEnvironment
        .getInstance());
    try {
      vos = bo.queryDelivBill(sql, clientLink);
    }
    catch (BusinessException ex) {
      ExceptionUtils.getInstance().wrappException(ex);
    }
    return vos;
  }

  public DelivBillVO saveDelivBill(DelivBillVO bill) {
    IDelivBill bo = (IDelivBill) NCLocator.getInstance().lookup(
        this.interfaceName);
    DelivBillVO vo = null;
    ClientLink clientLink = new ClientLink(ClientEnvironment
        .getInstance());
    try {
      vo = bo.saveDelivBill(bill, clientLink);
    }
    catch (BusinessException ex) {
      ExceptionUtils.getInstance().wrappException(ex);
    }
    return vo;
  }

  public void deleteDelivBill(DelivBillVO[] bills) {
    IDelivBill bo = (IDelivBill) NCLocator.getInstance().lookup(
        this.interfaceName);
    ClientLink clientLink = new ClientLink(ClientEnvironment
        .getInstance());
    try {
      bo.deleteDelivBill(bills, clientLink);
    }
    catch (BusinessException ex) {
      ExceptionUtils.getInstance().wrappException(ex);
    }
  }

  public DelivBillVO[] auditDelivBill(DelivBillVO[] bills) {
    IDelivBill bo = (IDelivBill) NCLocator.getInstance().lookup(
        this.interfaceName);
    DelivBillVO[] vos = null;
    ClientLink clientLink = new ClientLink(ClientEnvironment
        .getInstance());
    try {
      vos = bo.auditDelivBill(bills, clientLink);
    }
    catch (BusinessException ex) {
      ExceptionUtils.getInstance().wrappException(ex);
    }
    return vos;
  }

  public DelivBillVO[] unAuditDelivBill(DelivBillVO[] bills) {
    IDelivBill bo = (IDelivBill) NCLocator.getInstance().lookup(
        this.interfaceName);
    DelivBillVO[] vos = null;
    ClientLink clientLink = new ClientLink(ClientEnvironment
        .getInstance());
    try {
      vos = bo.unAuditDelivBill(bills, clientLink);
    }
    catch (BusinessException ex) {
      ExceptionUtils.getInstance().wrappException(ex);
    }
    return vos;
  }

  public BillViewVO[] exchangeDelivBill(AggregatedValueObject[] bills,
      String sourcebilltypecode, String targetbilltypecode) {
    IDelivBill bo = (IDelivBill) NCLocator.getInstance().lookup(
        this.interfaceName);
    BillViewVO[] vos = null;
    ClientLink clientLink = new ClientLink(ClientEnvironment
        .getInstance());
    try {
      vos = bo.exchangeDelivBill(bills, sourcebilltypecode, targetbilltypecode,
          clientLink);
    }
    catch (BusinessException ex) {
      ExceptionUtils.getInstance().wrappException(ex);
    }
    return vos;
  }

  public DelivBillVO loadEditDelivBill(DelivBillVO bill){
    IDelivBill bo = (IDelivBill) NCLocator.getInstance().lookup(
        this.interfaceName);
    DelivBillVO vo = null;
    ClientLink clientLink = new ClientLink(ClientEnvironment
        .getInstance());
    try {
      vo = bo.loadEditDelivBill(bill, clientLink);
    }
    catch (BusinessException ex) {
      ExceptionUtils.getInstance().wrappException(ex);
    }
    return vo;
  }
  
  public DelivBillVO signReceiveDelivBill(DelivBillVO bill){
    IDelivBill bo = (IDelivBill) NCLocator.getInstance().lookup(
        this.interfaceName);
    DelivBillVO vo = null;
    ClientLink clientLink = new ClientLink(ClientEnvironment
        .getInstance());
    try {
      vo = bo.signReceiveDelivBill(bill, clientLink);
    }
    catch (BusinessException ex) {
      ExceptionUtils.getInstance().wrappException(ex);
    }
    return vo;
  }
  //lumzh 2012-08-20 回写运输单是否已暂估状态
  public DelivBillVO writebackoffdelivbill(DelivBillVO bill){
	    IDelivBill bo = (IDelivBill) NCLocator.getInstance().lookup(
	        this.interfaceName);
	    DelivBillVO vo = null;
	    ClientLink clientLink = new ClientLink(ClientEnvironment
	            .getInstance());
	    try {
	      vo = bo.writebackoffdelivbill(bill,clientLink);
	    }
	    catch (BusinessException ex) {
	      ExceptionUtils.getInstance().wrappException(ex);
	    }
	    return vo;
	  }
}
