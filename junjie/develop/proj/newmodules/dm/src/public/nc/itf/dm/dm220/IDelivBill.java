package nc.itf.dm.dm220;

import nc.vo.dm.dm220.BillViewVO;
import nc.vo.dm.model.delivbill.entity.DelivBillVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.scm.pub.session.ClientLink;

/**
 * 运输单的远程接口
 * @author 钟鸣 2008-9-21 上午09:22:26
 */
public interface IDelivBill {
  public DelivBillVO[] queryDelivBill(String sql, ClientLink clientLink)
      throws BusinessException;

  public DelivBillVO saveDelivBill(DelivBillVO bill, ClientLink clientLink)
      throws BusinessException;

  public void deleteDelivBill(DelivBillVO[] bills, ClientLink clientLink)
      throws BusinessException;

  public DelivBillVO[] auditDelivBill(DelivBillVO[] bills, ClientLink clientLink)
      throws BusinessException;

  public DelivBillVO[] unAuditDelivBill(DelivBillVO[] bills,
      ClientLink clientLink) throws BusinessException;

  public BillViewVO[] exchangeDelivBill(AggregatedValueObject[] bills,
      String sourcebilltypecode,String targetbilltypecode,
      ClientLink clientLink) throws BusinessException;
  
  public DelivBillVO loadEditDelivBill(DelivBillVO bill,
      ClientLink clientLink) throws BusinessException;
  
  public DelivBillVO signReceiveDelivBill(DelivBillVO bill,
      ClientLink clientLink) throws BusinessException;
  public DelivBillVO writebackoffdelivbill(DelivBillVO bill,ClientLink clientLink)throws BusinessException;
}
