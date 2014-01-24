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

public class DefCheckBtn {

	public ButtonVO getButtonVO(){
		ButtonVO btnVo = new ButtonVO();
		btnVo.setBtnNo(102);
		btnVo.setBtnCode("DefCheck");
		btnVo.setBtnName("核销");
		btnVo.setBtnChinaName("核销");
			
		
		/*btnVo.setOperateStatus(new int[]{
				IBillOperate.OP_ADD
		});	*/		
		btnVo.setChildAry(new int[]{
		      		                   
		                });
		                
		return btnVo;
	}

}
