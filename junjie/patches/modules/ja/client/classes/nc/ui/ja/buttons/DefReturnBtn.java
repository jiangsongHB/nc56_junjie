package nc.ui.ja.buttons;


import nc.vo.trade.button.ButtonVO;

public class DefReturnBtn {
	public ButtonVO getButtonVO(){
		ButtonVO btnVo = new ButtonVO();
		btnVo.setBtnNo(104);
		btnVo.setBtnCode("DefReturn");
		btnVo.setBtnName("����");
		btnVo.setBtnChinaName("����");
		//btnVo.setOperateStatus(new int[]{1,0,3});  //����״̬
		
				
		btnVo.setChildAry(new int[]{
		      		                   
		                });
		                
		return btnVo;
	}
}
