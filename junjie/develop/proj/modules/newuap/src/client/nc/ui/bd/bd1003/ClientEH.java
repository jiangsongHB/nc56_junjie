package nc.ui.bd.bd1003;


import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.ui.trade.manage.ManageEventHandler;

/**
 * <p>
 * ������һ�������࣬��ҪĿ�������ɰ�ť�¼�����Ŀ��
 * <p/>
 * <b>�޸����� �޸�ʱ�� �޸���:</b>
 * <ul>
 * <li></li>
 * </ul>
 * @author libaoshan
 * @param 
 * @return 
 * @exception 
 */
    
 public class ClientEH extends ManageEventHandler {


        /**
	 * <p>
	 * ���췽��
	 * <p/>
	 * <b>�޸����� �޸�ʱ�� �޸���:</b>
	 * <ol>
	 * <li></li>
	 * </ol>
	 * @author libaoshan
	 * @param 
	 * @return 
	 * @exception 
	 */
	public ClientEH(BillManageUI billUI, IControllerBase control) {
		   super(billUI, control);
	}
        /**
	 * <p>
	 * �Զ��尴ť�Ĵ���
	 * <p/>
	 * <b>�޸����� �޸�ʱ�� �޸���:</b>
	 * <ol>
	 * <li></li>
	 * </ol>
	 * @author libaoshan
	 * @param intBtn ��ť�ı༭ 100����Ϊϵͳ��ť,100����Ϊ�Զ��尴ť
	 * @return 
	 * @exception Exception �׳��쳣
	 */
/*	protected void onBoElse(int intBtn) throws Exception {
		
	     	}*/
	@Override
	protected void onBoQuery() throws Exception {
	// TODO Auto-generated method stub
	super.onBoQuery();
	}

	
}