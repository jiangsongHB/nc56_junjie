package nc.ui.scmpub.taxinvoicetype;

import java.io.Serializable;
import nc.vo.trade.pub.IBDGetCheckClass2;

/**
 * <b> ǰ̨У�����Getter�� </b>
 *
 * <p>
 *     �ڴ˴���Ӵ����������Ϣ
 * </p>
 *
 *
 * @author author
 * @version tempProject version
 */

public class ClientUIGetter implements IBDGetCheckClass2,Serializable {

	/**
	 * ǰ̨У����
	 */
	public String getUICheckClass() {
		return "nc.ui.scmpub.taxinvoicetype.ClientUICheckRule";
	}

	/**
	 * ��̨У����
	 */
	public String getCheckClass() {
		return null;
	}

}