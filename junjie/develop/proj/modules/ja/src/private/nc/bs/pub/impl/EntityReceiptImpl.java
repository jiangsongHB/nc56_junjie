package nc.bs.pub.impl;

import nc.bs.dao.BaseDAO;
import nc.vo.ja.pub.itf.IEntityReceipt;

import nc.vo.pub.SuperVO;

public class EntityReceiptImpl implements IEntityReceipt{

	public void onCheck(SuperVO vo) throws Exception {
		// TODO Auto-generated method stub
		if(vo!=null){
			BaseDAO dao=new BaseDAO();
			dao.insertVO( vo);
		}
	}

}
