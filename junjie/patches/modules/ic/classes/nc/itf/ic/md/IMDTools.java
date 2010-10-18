package nc.itf.ic.md;

import nc.vo.ic.md.MdcrkVO;
import nc.vo.ic.xcl.MdxclVO;
import nc.vo.pub.BusinessException;

public interface IMDTools {
	
	public boolean saveMDrk(MdcrkVO[] mdvos,MdxclVO xclvo,String cgeneralbid) throws BusinessException;
	//货位更新 add by 阮睿 2010-10-18
	public void updateCargoInfo(String cgeneralbid) throws BusinessException;
	
}
