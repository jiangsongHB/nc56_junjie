package nc.ui.ja.entityreceiptck;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ArrayProcessor;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.trade.bill.ICardController;
import nc.ui.trade.card.BillCardUI;
import nc.ui.trade.card.CardEventHandler;
import nc.vo.pub.BusinessException;


/**
 * <b> �ڴ˴���Ҫ��������Ĺ��� </b>
 *
 * <p>
 *     �ڴ˴���Ӵ����������Ϣ
 * </p>
 *
 *
 * @author author
 * @version tempProject version
 */

public class ClientUI extends AbstractClientUI{
	
	
	protected CardEventHandler createEventHandler() {
	    return new MyEventHandler(this, getUIControl());
          }

	public String getRefBillType() {
		return null;
	}
	
	/**
	 * �޸Ĵ˷�����ʼ��ģ��ؼ�����
	 */
	protected void initSelfData() {
		getBillCardPanel().getBillModel().execLoadFormula();
		
	}

	public void setDefaultData() throws Exception {
	}
	
	/**
	 * �޸Ĵ˷������Ӻ�̨У��
	 */
	public Object getUserObject() {
		return null;
	}

	@Override
	public void afterEdit(BillEditEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	
}
