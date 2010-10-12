package nc.ui.bd.bd1003;

import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.businessaction.IBusinessActionType;
import nc.ui.trade.button.IBillButton;
import nc.vo.bd.bd1003.AdditionalValueVO;

import nc.vo.trade.pub.HYBillVO;

public class ClientCtrl extends AbstractManageController {

	/**
	 * @function 实现父类的构造方法
	 */
	public ClientCtrl() {
		super();
	}

	/**
	 * @function 实现父类接口，暂不处理 
	 */
	public String[] getCardBodyHideCol() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @function 设置卡片按钮
	 */
	public int[] getCardButtonAry() {
		// TODO Auto-generated method stub
		return new int[] { 
				IBillButton.Query,// 查询
				IBillButton.Add, // 增加
				IBillButton.Edit, // 修改
				IBillButton.Save, // 保存
				IBillButton.Delete,// 删除
				IBillButton.Cancel,// 取消
				IBillButton.Return, // 返回
				IBillButton.Refresh,// 刷新
				IBillButton.Print // 打印

		};
	}

	/**
	 * @function 显示卡片行号
	 */
	public boolean isShowCardRowNo() {
		// TODO Auto-generated method stub
		return true;
	}

	/**
	 * @function 不显示卡片合计行 
	 */
	public boolean isShowCardTotal() {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * @function 得到单据类型
	 */
	public String getBillType() {
		// TODO Auto-generated method stub
		return "HB1003";
	}

	/**
	 * @function 得到单据VO
	 */
	public String[] getBillVoName() {
		// TODO Auto-generated method stub
		return new String[] { HYBillVO.class.getName(),
				AdditionalValueVO.class.getName(),
				AdditionalValueVO.class.getName() };
	}

	/**
	 * @function 实现父类接口，暂不处理 
	 */
	public String getBodyCondition() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @function 实现父类接口，暂不处理
	 */
	public String getBodyZYXKey() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @function 得到业务类型 
	 */
	public int getBusinessActionType() {
		// TODO Auto-generated method stub
		return IBusinessActionType.BD;
	}

	/**
	 * @function 得到子表主键
	 */
	public String getChildPkField() {
		// TODO Auto-generated method stub
		return "pk_additionalvalue";
	}

	/**
	 * @function 实现父类接口，暂不处理
	 */
	public String getHeadZYXKey() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @function 得到主键
	 */
	public String getPkField() {
		// TODO Auto-generated method stub
		return "pk_additionalvalue";
	}

	/**
	 * @function 实现父类接口，暂不处理 
	 */
	public Boolean isEditInGoing() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @function 实现父类接口，暂不处理
	 */
	public boolean isExistBillStatus() {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * @function 执行卡片加载公式 
	 */
	public boolean isLoadCardFormula() {
		// TODO Auto-generated method stub
		return true;
	}

	/**
	 * @function 实现父类接口，暂不处理
	 */
	public String[] getListBodyHideCol() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @function 设置列表按钮 
	 */
	public int[] getListButtonAry() {
		// TODO Auto-generated method stub
		return new int[] { 
				IBillButton.Query, // 查询
				IBillButton.Add, // 增加
				IBillButton.Edit, // 修改
				IBillButton.Save, // 保存
				IBillButton.Delete, // 删除
				IBillButton.Cancel, // 取消
				IBillButton.Card, // 卡片显示
				IBillButton.Refresh,// 刷新
				IBillButton.Print // 打印
		};
	}

	/**
	 * @function 实现父类接口，暂不处理
	 */
	public String[] getListHeadHideCol() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @function 显示列表行号
	 *  
	 */
	public boolean isShowListRowNo() {
		// TODO Auto-generated method stub
		return true;
	}

	/**
	 * @function 不显示列表合计行
	 * 
	 */
	public boolean isShowListTotal() {
		// TODO Auto-generated method stub
		return false;
	}

}
