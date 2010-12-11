package nc.itf.ic.md;

import nc.vo.ic.md.MdcrkVO;
import nc.vo.ic.xcl.MdxclVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDouble;

public interface IMDTools {
	
	public boolean saveMDrk(MdcrkVO[] mdvos,MdxclVO xclvo,String cgeneralbid) throws BusinessException;
	//货位更新 add by 阮睿 2010-10-18
	public void updateCargoInfo(String cgeneralbid) throws BusinessException;
	
	
	/**
	 * 回写新出库数量至销售订单执行情况表
	 * @author MeiChao
	 * @date 2010-12-10
	 * @param csalebid 销售订单表体id  newNumber 要回写至销售订单执行情况表的新入库数量的累加差额  
	 */
	public boolean updateNewOutToSo(String csalebid,UFDouble newNumber) throws BusinessException;
	
}
