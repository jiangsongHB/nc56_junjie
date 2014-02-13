package nc.bs.pub.impl;

import nc.bs.dao.BaseDAO;
import nc.vo.ja.pub.itf.IEntityReceipt;

import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFDouble;

public class EntityReceiptImpl implements IEntityReceipt{

	public void onCheck(SuperVO vo) throws Exception {
		// TODO Auto-generated method stub
		if(vo!=null){
			BaseDAO dao=new BaseDAO();
			dao.insertVO( vo);
			//vo.getAttributeValue("def3");//实体发票明细主键
			//vo.getAttributeValue("tax");//本次核销金额
			String sql="update ja_entity_receipt_b set totalamount=nvl(totalamount,0)+"+new UFDouble(vo.getAttributeValue("tax").toString()) +
					" where pk_entityreceipt_b ='"+vo.getAttributeValue("def3")+"' ";
			dao.executeUpdate(sql);
		}
	}

	public void onUnCheck(SuperVO vo) throws Exception {
		// TODO Auto-generated method stub
		if(vo!=null){
			BaseDAO dao=new BaseDAO();
			dao.insertVO( vo);
			//vo.getAttributeValue("def3");//实体发票明细主键
			//vo.getAttributeValue("tax");//核销金额
			String sql="update ja_entity_receipt_b set totalamount=totalamount-"+new UFDouble(vo.getAttributeValue("tax").toString()) +
					",taxamount=taxamount+"+new UFDouble(vo.getAttributeValue("tax").toString())+" where pk_entityreceipt_b ='"+vo.getAttributeValue("def3")+"' ";
			dao.executeUpdate(sql);
		}
	}

}
