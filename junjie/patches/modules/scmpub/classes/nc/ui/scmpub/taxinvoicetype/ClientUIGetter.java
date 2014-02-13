package nc.ui.scmpub.taxinvoicetype;

import java.io.Serializable;
import nc.vo.trade.pub.IBDGetCheckClass2;

/**
 * <b> 前台校验类的Getter类 </b>
 *
 * <p>
 *     在此处添加此类的描述信息
 * </p>
 *
 *
 * @author author
 * @version tempProject version
 */

public class ClientUIGetter implements IBDGetCheckClass2,Serializable {

	/**
	 * 前台校验类
	 */
	public String getUICheckClass() {
		return "nc.ui.scmpub.taxinvoicetype.ClientUICheckRule";
	}

	/**
	 * 后台校验类
	 */
	public String getCheckClass() {
		return null;
	}

}