package nc.vo.ja.pub.itf;


import nc.vo.pub.SuperVO;

public interface IEntityReceipt {
	/**
	 * ����
	 */
	public void onCheck(SuperVO vo) throws Exception;
	/**
	 * ������
	 */
	public void onUnCheck(SuperVO vo) throws Exception;
}
