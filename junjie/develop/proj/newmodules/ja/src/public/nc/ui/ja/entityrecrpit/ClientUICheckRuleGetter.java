package nc.ui.ja.entityrecrpit;

import java.io.Serializable;
import nc.vo.trade.pub.IBDGetCheckClass2;

/**
 * <b> ǰ̨У�����Getter�� </b>
 *
 * <p>
 *     �ڴ˴����Ӵ����������Ϣ
 * </p>
 *
 *
 * @author author
 * @version tempProject 1.0
 */

public class ClientUICheckRuleGetter implements IBDGetCheckClass2,Serializable {

	/**
	 * ǰ̨У����
	 */
	public String getUICheckClass() {
		return "nc.ui.ja.entityrecrpit.ClientUICheckRule";
	}

	/**
	 * ��̨У����
	 */
	public String getCheckClass() {
		return null;
	}

}