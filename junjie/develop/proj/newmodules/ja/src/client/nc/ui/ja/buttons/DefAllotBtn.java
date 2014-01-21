package nc.ui.ja.buttons;

import nc.ui.trade.base.IBillOperate;
import nc.vo.trade.button.ButtonVO;


/**
 * <b> 在此处简要描述此类的功能 </b><br>
 *
 * <p>
 *     在此处添加此类的描述信息
 * </p>
 *
 *
 * @author authorName
 * @version tempProject version
 */

public class DefAllotBtn {

	public ButtonVO getButtonVO(){
		ButtonVO btnVo = new ButtonVO();
		btnVo.setBtnNo(101);
		btnVo.setBtnCode("DefAllot");
		btnVo.setBtnName("分配");
		btnVo.setBtnChinaName("分配");
			
		
		//btnVo.setOperateStatus(new int[]{});			
		btnVo.setChildAry(new int[]{
		      		                   
		                });
		                
		return btnVo;
	}

}
