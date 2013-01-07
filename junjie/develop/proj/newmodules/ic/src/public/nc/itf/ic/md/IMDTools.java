package nc.itf.ic.md;

import nc.ui.ic.mdck.ChInfoVO;
import nc.vo.ic.jjvo.InvDetailCVO;
import nc.vo.ic.md.MdcrkVO;
import nc.vo.ic.xcl.MdxclVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDouble;

public interface IMDTools {
	
	/**
	 * @author MeiChao
	 * @param mdvos
	 * @param xclvo
	 * @param cgeneralbid
	 * @param invDetailCVOs
	 * @return
	 * @throws BusinessException
	 */
	public boolean saveMDrk(MdcrkVO[] mdvos,MdxclVO xclvo,String cgeneralbid,InvDetailCVO[] invDetailCVOs) throws BusinessException;
	//��λ���� add by ��� 2010-10-18
	public void updateCargoInfo(String cgeneralbid) throws BusinessException;
	
	
	/**
	 * ��д�³������������۶���ִ�������
	 * @author MeiChao
	 * @date 2010-12-10
	 * @param csalebid ���۶�������id  newNumber Ҫ��д�����۶���ִ��������������������ۼӲ��  
	 */
	public boolean updateNewOutToSo(String csalebid,UFDouble newNumber) throws BusinessException;
	
	/**
	 * add by ouyangzhb 2013-01-07 �뵥�����ִ���ʱ����������
	 * @param vos
	 * @param lsckVos
	 * @return
	 * @throws BusinessException
	 */
	public boolean updateXcl(MdcrkVO[] vos,MdcrkVO[] lsckVos) throws BusinessException ;
	
}
