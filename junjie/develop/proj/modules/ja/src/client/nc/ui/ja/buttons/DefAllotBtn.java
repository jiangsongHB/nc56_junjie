package nc.ui.ja.buttons;

import nc.ui.trade.base.IBillOperate;
import nc.vo.trade.button.ButtonVO;


/**
 * <b> �ڴ˴���Ҫ��������Ĺ��� </b><br>
 *
 * <p>
 *     �ڴ˴���Ӵ����������Ϣ
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
		btnVo.setBtnName("����");
		btnVo.setBtnChinaName("����");
			
		
		//btnVo.setOperateStatus(new int[]{});			
		btnVo.setChildAry(new int[]{
		      		                   
		                });
		                
		return btnVo;
	}

}
