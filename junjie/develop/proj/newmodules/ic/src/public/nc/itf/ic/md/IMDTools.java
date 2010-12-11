package nc.itf.ic.md;

import nc.vo.ic.md.MdcrkVO;
import nc.vo.ic.xcl.MdxclVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDouble;

public interface IMDTools {
	
	public boolean saveMDrk(MdcrkVO[] mdvos,MdxclVO xclvo,String cgeneralbid) throws BusinessException;
	//��λ���� add by ��� 2010-10-18
	public void updateCargoInfo(String cgeneralbid) throws BusinessException;
	
	
	/**
	 * ��д�³������������۶���ִ�������
	 * @author MeiChao
	 * @date 2010-12-10
	 * @param csalebid ���۶�������id  newNumber Ҫ��д�����۶���ִ��������������������ۼӲ��  
	 */
	public boolean updateNewOutToSo(String csalebid,UFDouble newNumber) throws BusinessException;
	
}
