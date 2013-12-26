package nc.ui.ic.ic101;


import nc.ui.pub.FramePanel;
import nc.vo.ic.pub.check.VOCheck;
import nc.vo.pub.NullFieldException;
import nc.vo.pub.ValidationException;
import nc.vo.scm.constant.ic.InOutFlag;
/**
 * 类的功能、用途、现存BUG，以及其它别人可能感兴趣的介绍。
 * 作者：余大英
 * @version	最后修改日期(2001-11-30 8:57:51)
 * @see		需要参见的其它类
 * @since		从产品的那一个版本，此类被添加进来。（可选）
 * 修改人 + 修改日期
 * 修改说明
 */
public class ClientUI extends nc.ui.ic.pub.bill.GeneralBillClientUI {

/**
 * ClientUI 构造子注解。
 */
public ClientUI() {
	super();
	initialize();
}

/**
 * ClientUI 构造子注解。
 * add by liuzy 2007-12-18 根据节点编码初始化单据模版
 */
public ClientUI(FramePanel fp) {
 super(fp);
 initialize();
}
/**
 * ClientUI 构造子注解。
 * nc 2.2 提供的单据联查功能构造子。
 *
 */
public ClientUI(
	String pk_corp,
	String billType,
	String businessType,
	String operator,
	String billID) {
	super(pk_corp, billType, businessType, operator, billID);

}
/**
 * 创建者：王乃军
 * 功能：单据编辑后处理
 * 参数：
 * 返回：
 * 例外：
 * 日期：(2001-5-9 9:23:32)
 * 修改日期，修改人，修改原因，注释标志：
 *
 *
 *
 *
 */
@Override
protected void afterBillEdit(nc.ui.pub.bill.BillEditEvent e) {
	//设置业务日期
	if (e.getKey().equals(getEnvironment().getNumItemKey()) || e.getKey().equals(getEnvironment().getAssistNumItemKey())) {
		if ((getBillCardPanel().getBodyValueAt(e.getRow(), "dbizdate") == null
			|| getBillCardPanel()
				.getBodyValueAt(e.getRow(), "dbizdate")
				.toString()
				.trim()
				.length()
				== 0)
			&& m_sStartDate != null) {
			nc.vo.pub.lang.UFDate dstart = new nc.vo.pub.lang.UFDate(m_sStartDate);
			nc.vo.pub.lang.UFDate dbiz = dstart.getDateBefore(1);
			getBillCardPanel().setBodyValueAt(dbiz.toString(), e.getRow(), "dbizdate");
		}
	}
}
/**
 * 创建者：王乃军
 * 功能：表体行列选择改变
 * 参数：
 * 返回：
 * 例外：
 * 日期：(2001-5-9 9:23:32)
 * 修改日期，修改人，修改原因，注释标志：
 *
 *
 *
 *
 */
@Override
protected void afterBillItemSelChg(int iRow, int iCol) {
		////货位过滤
	//String sItemKey=getBillCardPanel().getBillModel().getBodyKeyByCol(iCol);

	//if (sItemKey.equals("vspacename")) {
		//filterSpace(iRow);
	//}


}
/**
 * 创建者：王乃军
 * 功能：单据表体编辑事件前触发处理
 * 参数：e单据编辑事件
 * 返回：
 * 例外：
 * 日期：(2001-5-8 19:08:05)
 * 修改日期，修改人，修改原因，注释标志：
 *
 *
 *
 *
 */
@Override
public boolean beforeBillItemEdit(nc.ui.pub.bill.BillEditEvent e) {
	return false;
}
/**
 * 创建者：王乃军
 * 功能：表体行列选择改变
 * 参数：
 * 返回：
 * 例外：
 * 日期：(2001-5-9 9:23:32)
 * 修改日期，修改人，修改原因，注释标志：
 *
 *
 *
 *
 */
@Override
protected void beforeBillItemSelChg(int iRow, int iCol) {}
/**
  * 创建者：王乃军
  * 功能：抽象方法：保存前的VO检查
  * 参数：待保存单据
  * 返回：
  * 例外：
  * 日期：(2001-5-24 下午 5:17)
  * 修改日期，修改人，修改原因，注释标志：
  */
protected boolean checkVO(nc.vo.ic.pub.bill.GeneralBillVO voBill) {
	if (checkVO()) {

		try {

			VOCheck.checkStartDate(getM_voBill(), "dbizdate", nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UC000-0000380")/*@res "入库日期"*/, m_sStartDate);
		} catch (NullFieldException e) {
			showErrorMessage(e.getHint());
			return false;
		} catch (ValidationException e) {
			nc.vo.scm.pub.SCMEnv.out("校验异常！其他未知故障...");
			showErrorMessage(e.getMessage());
			handleException(e);
			return false;
		}

		return true;
	} else
		return false;
}
/**
 * 子类实现该方法，返回业务界面的标题。
 * @version (00-6-6 13:33:25)
 *
 * @return java.lang.String
 */
public String getTitle() {
	return  super.getTitle();
}
/**
 * 创建者：王乃军
 * 功能：初始化系统参数
 * 参数：
 * 返回：
 * 例外：
 * 日期：(2001-5-9 9:23:32)
 * 修改日期，修改人，修改原因，注释标志：
 *
 *
 *
 *
 */
protected void initPanel() {
	//需要单据参照
	super.setNeedBillRef(false);
	//调整菜单例子
	////--- 1  在“单据维护”菜单上 增加“配比出库” ----
	super.m_bIsInitBill=true;
}

public String getBillType() {
	return nc.vo.ic.pub.BillTypeConst.m_initIn;
}

public String getFunctionNode() {
	return "40080402";
}

public int getInOutFlag() {
	return InOutFlag.IN;
}

/**
 * 创建者：王乃军
 * 功能：在列表方式下选择一张单据
 * 参数： 单据在alListData中的索引
 * 返回：无
 * 例外：
 * 日期：(2001-11-23 18:11:18)
 *  修改日期，修改人，修改原因，注释标志：
 */
protected void selectBillOnListPanel(int iBillIndex) {
	//dispSpace(iBillIndex);
}
/**
 * 创建者：王乃军
 * 功能：抽象方法：设置按钮状态，在setButtonStatus中调用。
 * 参数：
 * 返回：
 * 例外：
 * 日期：(2001-5-9 9:23:32)
 * 修改日期，修改人，修改原因，注释标志：
 *
 *
 *
 *
 */
protected void setButtonsStatus(int iBillMode) {}
}