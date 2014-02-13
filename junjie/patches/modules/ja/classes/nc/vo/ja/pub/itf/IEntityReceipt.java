package nc.vo.ja.pub.itf;


import nc.vo.pub.SuperVO;

public interface IEntityReceipt {
	/**
	 * ºËÏú
	 */
	public void onCheck(SuperVO vo) throws Exception;
	/**
	 * ·´ºËÏú
	 */
	public void onUnCheck(SuperVO vo) throws Exception;
}
