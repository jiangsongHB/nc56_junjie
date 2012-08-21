package nc.impl.dm.dm220;

import nc.bs.dao.BaseDAO;
import nc.bs.dm.dm220.remote.DelivBillRemoteAction;

import nc.itf.dm.dm220.IDelivBill;

import nc.vo.dm.dm220.BillViewVO;
import nc.vo.dm.model.delivbill.entity.DelivBillVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.scm.pattern.exception.ExceptionUtils;
import nc.vo.scm.pub.session.ClientLink;

/**
 * @author 钟鸣 2008-9-21 上午09:26:30
 */
public class DelivBillImpl implements IDelivBill {
  public DelivBillImpl() {

  }

  public DelivBillVO[] auditDelivBill(DelivBillVO[] bills, ClientLink clientLink)
      throws BusinessException {
    DelivBillRemoteAction action = new DelivBillRemoteAction();
    DelivBillVO[] vos = null;
    try {
      vos = action.auditDelivBill(bills, clientLink);
    }
    catch (RuntimeException ex) {
      ExceptionUtils.getInstance().marsh(ex);
    }
    return vos;
  }

  public void deleteDelivBill(DelivBillVO[] bills, ClientLink clientLink)
      throws BusinessException {
    DelivBillRemoteAction action = new DelivBillRemoteAction();
    try {
      action.deleteDelivBill(bills, clientLink);
    }
    catch (RuntimeException ex) {
      ExceptionUtils.getInstance().marsh(ex);
    }
  }

  public DelivBillVO[] queryDelivBill(String condtion, ClientLink clientLink)
      throws BusinessException {
    DelivBillRemoteAction action = new DelivBillRemoteAction();
    DelivBillVO[] vos = null;
    try {
      vos = action.queryDelivBill(condtion, clientLink);
    }
    catch (RuntimeException ex) {
      ExceptionUtils.getInstance().marsh(ex);
    }
    return vos;
  }

  public DelivBillVO saveDelivBill(DelivBillVO bill, ClientLink clientLink)
      throws BusinessException {
    DelivBillRemoteAction action = new DelivBillRemoteAction();
    DelivBillVO vo = null;
    try {
      vo = action.saveDelivBill(bill, clientLink);
    }
    catch (RuntimeException ex) {
      ExceptionUtils.getInstance().marsh(ex);
    }
    return vo;
  }

  public DelivBillVO[] unAuditDelivBill(DelivBillVO[] bills,
      ClientLink clientLink) throws BusinessException {
    DelivBillRemoteAction action = new DelivBillRemoteAction();
    DelivBillVO[] vos = null;
    try {
      vos = action.unAuditDelivBill(bills, clientLink);
    }
    catch (RuntimeException ex) {
      ExceptionUtils.getInstance().marsh(ex);
    }
    return vos;
  }

  public BillViewVO[] exchangeDelivBill(AggregatedValueObject[] bills,
      String sourcebilltypecode, String targetbilltypecode,
      ClientLink clientLink) throws BusinessException {
    DelivBillRemoteAction action = new DelivBillRemoteAction();
    BillViewVO[] vos = null;
    try {
      vos = action.exchangeDelivBill(bills, sourcebilltypecode,
          targetbilltypecode, clientLink);
    }
    catch (RuntimeException ex) {
      ExceptionUtils.getInstance().marsh(ex);
    }
    return vos;
  }

  public DelivBillVO loadEditDelivBill(DelivBillVO bill,
      ClientLink clientLink) throws BusinessException{
    DelivBillRemoteAction action = new DelivBillRemoteAction();
    DelivBillVO vo = null;
    try {
      vo = action.loadEditDelivBill(bill, clientLink);
    }
    catch (RuntimeException ex) {
      ExceptionUtils.getInstance().marsh(ex);
    }
    return vo;
  }
  
  public DelivBillVO signReceiveDelivBill(DelivBillVO bill,
      ClientLink clientLink) throws BusinessException{
    DelivBillRemoteAction action = new DelivBillRemoteAction();
    DelivBillVO vo = null;
    try {
      vo = action.signReceiveDelivBill(bill, clientLink);
    }
    catch (RuntimeException ex) {
      ExceptionUtils.getInstance().marsh(ex);
    }
    return vo;
  }
  //费用暂估时回写运输单，是否已暂估状态使用lumzh 2012-08-18
  public DelivBillVO writebackoffdelivbill(DelivBillVO bill,ClientLink clientLink) throws BusinessException{
	 
	    try {
	    	 BaseDAO dao=new BaseDAO();
			 String sql="update dm_delivbill set isestimate='Y' where cdelivbill_hid ='"+bill.getPrimaryKey()+"' ";	
			 dao.executeUpdate(sql);
			 DelivBillRemoteAction action = new DelivBillRemoteAction();
			 DelivBillVO[] vos = null;
			 vos = action.queryDelivBill(" select distinct cdelivbill_hid from dm_delivbill where dr = 0 and cdelivbill_hid='"+bill.getPrimaryKey()+"'", clientLink);
			//   bill.getParent().setAttributeValue("isestimate", "Y");
	         if(vos!=null && vos.length>0){
	    	   bill=vos[0];
	         }
	    } catch (RuntimeException ex) {
	      ExceptionUtils.getInstance().marsh(ex);
	    }
	    return bill;
   } 
}
