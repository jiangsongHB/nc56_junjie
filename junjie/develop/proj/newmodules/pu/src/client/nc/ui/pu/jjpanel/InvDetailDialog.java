package nc.ui.pu.jjpanel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import javax.swing.ListSelectionModel;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.ui.bd.ref.busi.InvmandocDefaultRefModel;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillEditListener;
import nc.ui.pub.bill.BillEditListener2;
import nc.ui.pub.bill.BillListPanel;
import nc.ui.pub.bill.BillModel;
import nc.ui.pub.hotkey.HotkeyUtil;
import nc.vo.ic.jjvo.InformationCostVO;
import nc.vo.ml.IProductCode;
import nc.vo.pu.jjvo.InvDetailVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.rc.receive.ArriveorderItemVO;
import nc.vo.scm.pub.SCMEnv;

/**
 * 存货明细维护界面
 * @author MeiChao
 * @date 2010-12-15
 * 
 */
public class InvDetailDialog extends UIDialog implements ActionListener,BillEditListener, BillEditListener2 {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7622707941165575671L;

	//上级卡片  by 付世超 2010-10-13
	private BillCardPanel parentCardPanel = null;
	
	private BillListPanel billListPanel = null;
	// 确定按钮
	private nc.ui.pub.beans.UIButton m_btnOK = null;
	// 取消按钮
	private nc.ui.pub.beans.UIButton m_btnCancel = null;
	//2010-12-24 MeiChao add 计算按钮
	private nc.ui.pub.beans.UIButton m_btnCalculate = null;
	//2010-12-24 MeiChao add 是否计算完毕(即分配钢厂重量)
	private boolean isCalculate=true;
	//2010-12-27 MeiChao add 长度是否全为数字
	private boolean isLengthAllNumber=true;
	// 增加按钮
	private nc.ui.pub.beans.UIButton m_btnAdd = null;
	// 删除按钮
	private nc.ui.pub.beans.UIButton m_btnDel = null;
	// 修改按钮
	private nc.ui.pub.beans.UIButton m_btnMod = null;
	// 公司ID
	private String m_sLoginCorp = null;
	// 操作员ID
	private String m_sOperator = null;
	//存货基本id
	private String m_sPK_InvBasDoc=null;
	// 按钮面板
	private nc.ui.pub.beans.UIPanel m_panelSouth = null;
	// UIDialog的面板
	private javax.swing.JPanel m_dialogContentPane = null;
	// 填充面板
	private nc.ui.pub.beans.UIPanel m_pnlFillSpaceWest = null;
	private nc.ui.pub.beans.UIPanel m_pnlFillSpaceEast = null;
	private boolean m_closeMark = false;
	// 存货明细数组
	private InvDetailVO[] invDetailVOs = null;
	//旧存货明细数组(初始化时被传入的)
	private InvDetailVO[] oldInvDetailVOs = new InvDetailVO[0];
	public InvDetailVO[] getOldInvDetailVOs() {
		return oldInvDetailVOs;
	}

	public void setOldInvDetailVOs(InvDetailVO[] oldInvDetailVOs) {
		this.oldInvDetailVOs = oldInvDetailVOs;
	}

	/**
	 * 取存货明细,自动生成.
	 * @return
	 */
	public InvDetailVO[] getInvDetailVOs() {
		return invDetailVOs;
	}

	public void setInvDetailVOs(InvDetailVO[] invDetailVOs) {
		this.invDetailVOs = invDetailVOs;
	}
	//所选到货单行VO
	private ArriveorderItemVO selectedArriveBody=null;
	public ArriveorderItemVO getSelectedArriveBody() {
		return selectedArriveBody;
	}
	public void setSelectedArriveBody(ArriveorderItemVO selectedArriveBody) {
		this.selectedArriveBody = selectedArriveBody;
	}
	// 当前单据类型 by 付世超 2010-10-30
	private String billtype = null;
	
	/**
	 * @function 传入父容器,公司,操作员的构造函数
	 * 
	 * @author MeiChao
	 * 
	 * @date 2010-8-20 上午10:02:36
	 * 
	 */
	public InvDetailDialog(java.awt.Container parent, String sPk_Corp,
			String sOperatorID) {
		super(parent);
		m_sLoginCorp = sPk_Corp;
		initDialog(); // 初始化对话框
	}

	/**
	 * @function 重写父类传入父容器的构造函数
	 * 
	 * @author MeiChao
	 * 
	 * @date 2010-8-20 上午10:12:28
	 * 
	 */
	public InvDetailDialog(java.awt.Container parent,InvDetailVO[] vos,ArriveorderItemVO selectedArriveBodyRow) {
		super(parent);
		initDialog();
		
		this.getBillListPanel().setHeaderValueVO(vos);
		this.getBillListPanel().getHeadBillModel().execLoadFormula();
		if (vos != null && vos.length > 0) {
			this.setOldInvDetailVOs(vos);
			for (int i = 0; i < vos.length; i++) {// 设置行状态为: 普通. 旧VO状态设置为 未改变
				this.getBillListPanel().getHeadBillModel().setRowState(i,
						BillModel.NORMAL);
				this.oldInvDetailVOs[i].setStatus(VOStatus.UNCHANGED);
				if(this.isLengthAllNumber){//如果当前的长度是否数字标识为true ,那么遍历检查一遍
					if(!this.isDoueric(vos[i].getContractlength())){
						this.isLengthAllNumber=false;//一旦存在非数字长度,那么将长度数字标识修改为false
					}
				}
			}
		}
		this.setSelectedArriveBody(selectedArriveBodyRow);
		this.m_sPK_InvBasDoc=selectedArriveBodyRow.getCbaseid();
	}
	public InvDetailDialog(java.awt.Container parent) {
		super(parent);
		initDialog();
		}

	/**
	 * @function 初始化对话框
	 * 
	 * @author MeiChao
	 * 
	 * @return void
	 * 
	 * @date 2010-8-20 上午10:13:42
	 */
	private void initDialog() {

		setName("InvDetail");
		setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		setTitle("到货明细");
		setBounds(37, 563, 1000, 400);
		setResizable(false);
		// 给定显示内容
		setContentPane(getUIDialogContentPane());
		// 修改参照,只显示应税劳务标志为'Y'的存货
//		UIRefPane refPane = (UIRefPane) getBillListPanel().getHeadItem("costcode").getComponent();
//		nc.ui.bd.ref.busi.InvmandocDefaultRefModel refModel = (InvmandocDefaultRefModel) refPane.getRefModel();
//		refModel.setWherePart(" bd_invbasdoc.laborflag = 'Y' and bd_invmandoc.pk_corp = '"+ClientEnvironment.getInstance().getCorporation().getPk_corp()+"'");
		this.getBillListPanel().getParentListPanel().setTatolRowShow(true);
		
		this.getBillListPanel().addBodyEditListener(this);
	//	this.getBillListPanel().setMultiSelect(true);
	//wanglei 2011-08-10 设置多选模式。	
		getBillListPanel().getHeadTable().setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

	}

	/**
	 * @function 得到显示内容面板
	 * 
	 * @author MeiChao
	 * 
	 * @return javax.swing.JPanel
	 * 
	 * @date 2010-8-20 上午10:14:10
	 */
	private javax.swing.JPanel getUIDialogContentPane() {
		if (m_dialogContentPane == null) {
			m_dialogContentPane = new javax.swing.JPanel();
			m_dialogContentPane.setName("UIDialogContentPane");
			m_dialogContentPane.setLayout(new java.awt.BorderLayout());
			// 按钮panel
			m_dialogContentPane.add(getPnlSouth(), "South");
			// 公司panel
			m_dialogContentPane.add(getBillListPanel(), "Center");
			// 边界填充panel
			m_dialogContentPane.add(getPnlFillSpaceWest(), "West");
			m_dialogContentPane.add(getPnlFillSpaceEast(), "East");
		}
		return m_dialogContentPane;
	}

	/**
	 * @function 获得按钮面板
	 * 
	 * @author MeiChao
	 * 
	 * @return nc.ui.pub.beans.UIPanel
	 * 
	 * @date 2010-8-20 上午10:15:33
	 */
	private nc.ui.pub.beans.UIPanel getPnlSouth() {
		if (m_panelSouth == null) {
			m_panelSouth = new nc.ui.pub.beans.UIPanel();
			m_panelSouth.setName("PnlSouth");
			// 增加
			m_panelSouth.add(getBtnAdd(), getBtnAdd().getName());
			// 删除
			m_panelSouth.add(getBtnDel(), getBtnDel().getName());
			//计算
			m_panelSouth.add(getBtnCalculate(),getBtnCalculate().getName());
			// 确定
			m_panelSouth.add(getBtnOK(), getBtnOK().getName());
			// 取消
			m_panelSouth.add(getBtnCancel(), getBtnCancel().getName());
			//修改
			m_panelSouth.add(getBtnMod(),getBtnMod().getName());
		}
		return m_panelSouth;
	}

	/**
	 * @function 得到确定按钮
	 * 
	 * @author MeiChao
	 * 
	 * @return nc.ui.pub.beans.UIButton
	 * 
	 * @date 2010-8-20 上午10:19:17
	 */
	private nc.ui.pub.beans.UIButton getBtnOK() {
		if (m_btnOK == null) {
			m_btnOK = new nc.ui.pub.beans.UIButton();
			m_btnOK.setName("BtnOK");
			HotkeyUtil.setHotkeyAndText(m_btnOK, 'O', nc.ui.ml.NCLangRes
					.getInstance().getStrByID(IProductCode.PRODUCTCODE_COMMON,
							"UC001-0000044"));/* @res "确定" */
			m_btnOK.addActionListener(this);
		}
		return m_btnOK;
	}

	/**
	 * 获取 "计算" 按钮
	 *@author MeiChao
	 *@date 2010-12-24 
	 */
	private nc.ui.pub.beans.UIButton getBtnCalculate() {
		if (this.m_btnCalculate== null) {
			m_btnCalculate = new nc.ui.pub.beans.UIButton();
			m_btnCalculate.setName("计算");
			HotkeyUtil.setHotkeyAndText(m_btnCalculate, 'C', "计算");/* @res "确定" */
			m_btnCalculate.addActionListener(this);
		}
		return m_btnCalculate;
	}
	
	
	/**
	 * @function 得到删除按钮
	 * 
	 * @author MeiChao
	 * 
	 * @return nc.ui.pub.beans.UIButton
	 * 
	 * @date 2010-8-20 上午10:19:39
	 */
	private nc.ui.pub.beans.UIButton getBtnDel() {
		if (m_btnDel == null) {

			m_btnDel = new nc.ui.pub.beans.UIButton("删除");
			m_btnDel.setName("BtnDel");

			m_btnDel.addActionListener(this);
		}
		return m_btnDel;
	}

	/**
	 * @function 得到增加按钮
	 * 
	 * @author MeiChao
	 * 
	 * @return nc.ui.pub.beans.UIButton
	 * 
	 * @date 2010-8-20 上午10:20:49
	 */
	private nc.ui.pub.beans.UIButton getBtnAdd() {
		if (m_btnAdd == null) {

			m_btnAdd = new nc.ui.pub.beans.UIButton("增加");
			m_btnAdd.setName("BtnAdd");

			m_btnAdd.addActionListener(this);
		}
		return m_btnAdd;
	}
	/**
	 * @function 得到修改按钮
	 *
	 * @author MeiChao
	 *
	 * @return nc.ui.pub.beans.UIButton
	 *
	 * @date 2010-9-25 上午09:49:41
	 */
	private nc.ui.pub.beans.UIButton getBtnMod() {
		if (m_btnMod == null) {

			m_btnMod = new nc.ui.pub.beans.UIButton("修改");
			m_btnMod.setName("BtnMod");

			m_btnMod.addActionListener(this);
		}
		return m_btnMod;
	}

	/**
	 * @function 得到取消按钮
	 * 
	 * @author MeiChao
	 * 
	 * @return nc.ui.pub.beans.UIButton
	 * 
	 * @date 2010-8-20 上午10:22:01
	 */
	private nc.ui.pub.beans.UIButton getBtnCancel() {
		if (m_btnCancel == null) {
			m_btnCancel = new nc.ui.pub.beans.UIButton();
			m_btnCancel.setName("BtnCancel");
			HotkeyUtil.setHotkeyAndText(m_btnCancel, 'C', nc.ui.ml.NCLangRes
					.getInstance().getStrByID(IProductCode.PRODUCTCODE_COMMON,
							"UC001-0000008"));/* @res "取消" */
			m_btnCancel.addActionListener(this);
		}
		return m_btnCancel;
	}

	/**
	 * @function 得到用于填充界面的东面版
	 * 
	 * @author MeiChao
	 * 
	 * @return nc.ui.pub.beans.UIPanel
	 * 
	 * @date 2010-8-20 上午10:22:15
	 */
	private nc.ui.pub.beans.UIPanel getPnlFillSpaceEast() {
		if (m_pnlFillSpaceEast == null) {
			m_pnlFillSpaceEast = new nc.ui.pub.beans.UIPanel();
			m_pnlFillSpaceEast.setName("PnlFillSpaceEast");
			m_pnlFillSpaceEast.setPreferredSize(new java.awt.Dimension(15, 10));
		}
		return m_pnlFillSpaceEast;
	}

	/**
	 * @function 得到用于填充界面的西面板
	 * 
	 * @author MeiChao
	 * 
	 * @return nc.ui.pub.beans.UIPanel
	 * 
	 * @date 2010-8-20 上午10:22:47
	 */
	private nc.ui.pub.beans.UIPanel getPnlFillSpaceWest() {
		if (m_pnlFillSpaceWest == null) {
			m_pnlFillSpaceWest = new nc.ui.pub.beans.UIPanel();
			m_pnlFillSpaceWest.setName("PnlFillSpaceWest");
			m_pnlFillSpaceWest.setPreferredSize(new java.awt.Dimension(15, 16));
		}
		return m_pnlFillSpaceWest;
	}

	/**
	 * @function 得到单据列表面板
	 * 
	 * @author MeiChao
	 * 
	 * @return BillListPanel
	 * 
	 * @date 2010-8-20 上午10:23:09
	 */
	private BillListPanel getBillListPanel() {
		if (billListPanel == null) {
			billListPanel = new BillListPanel();
			// 加载模板
			billListPanel.loadTemplet("JJ001", "", this.m_sOperator, this.m_sLoginCorp);	
			//billListPanel.getHeadBillModel().setEnabledAllItems(true);
			billListPanel.addEditListener(this);
		}
		return billListPanel;
	}

	/**
	 * @function 按钮动作处理
	 * 
	 * @author MeiChao
	 * 
	 * @param e
	 * 
	 * @date 2010-8-20 上午10:41:37
	 */
	public void actionPerformed(ActionEvent e) {
		// 取消按钮的动作
		if (e.getSource() == this.getBtnCancel()) {
			this.m_closeMark = false;
			this.closeCancel();
		}
		// 确定按钮动作
		else if (e.getSource() == this.getBtnOK()) {
			//判断是否已计算()
			if(!this.isCalculate){
				if(!this.isLengthAllNumber){
					MessageDialog.showErrorDlg(this,"提示","请手动输入钢厂重量.");
					return;
				}else{
					MessageDialog.showHintDlg(this,"提示","请先使用\"计算\"按钮分配钢厂重量!");
					return;
				}
			}
			invDetailVOs = (InvDetailVO[])getBillListPanel().getHeadBillModel().getBodyValueVOs(InvDetailVO.class.getName());
			if( invDetailVOs == null || invDetailVOs.length == 0){
				this.m_closeMark = true;
				this.closeOK();
			}
               String message = voCheck(invDetailVOs).trim();
			if(!message.equals("")){
				MessageDialog.showHintDlg(this, "提示",message);
				return;
			}
			if(!this.isLengthAllNumber){
				/**
				 * 首先检查明细是否已维护完毕: 明细支数=所选行到货件数
				 */
				//获取明细VO数组
				//invDetailVOs = (InvDetailVO[])getBillListPanel().getHeadBillModel().getBodyValueVOs(InvDetailVO.class.getName());
				//明细件数件数总和
				UFDouble detailSumnumber=new UFDouble(0);
				//明细钢厂重量总和
				UFDouble detailSumweight=new UFDouble(0);
				for(int i=0;i<invDetailVOs.length;i++){
					detailSumnumber=detailSumnumber.add(invDetailVOs[i].getArrivenumber());
					detailSumweight=detailSumweight.add(invDetailVOs[i].getContractweight());
				}
				//明细件数总和,与表体件数之差
				UFDouble diffNuma=detailSumnumber.sub(this.selectedArriveBody.getNassistnum());
				//明细钢厂重量总和,与表体钢厂重量之差
				UFDouble diffNumb=detailSumweight.sub(this.selectedArriveBody.getNarrvnum());
				if(diffNuma.doubleValue()>0&&this.getBillListPanel().getHeadBillModel().getRowCount()>0){
					MessageDialog.showHintDlg(this,"警告","存货明细支数总和超出表体到货件数,\n表体件数为:"+this.selectedArriveBody.getNassistnum()+"\n 当前明细件数为:"+detailSumnumber.setScale(2,UFDouble.ROUND_HALF_UP)+"\n请检查!");
					return;
				}else if(diffNuma.doubleValue()<0&&this.getBillListPanel().getHeadBillModel().getRowCount()>0){
					MessageDialog.showHintDlg(this,"警告","存货明细支数总和小于表体到货件数,\n表体件数为:"+this.selectedArriveBody.getNassistnum()+"\n 当前明细件数为:"+detailSumnumber.setScale(2,UFDouble.ROUND_HALF_UP)+"\n请检查!");
					return;
				}
				//2010-12-27 MeiChao 如果 长度中含有字母,那么需要检查钢厂总重量是否与到货表体重量相等.
				if(diffNumb.doubleValue()>0&&this.getBillListPanel().getHeadBillModel().getRowCount()>0){
					MessageDialog.showHintDlg(this,"警告","存货明细钢厂重量总和超出表体到货重量,\n表体重量为:"+this.selectedArriveBody.getNarrvnum()+"\n 当前明细总重量为:"+detailSumweight.setScale(2,UFDouble.ROUND_HALF_UP)+"\n请检查!");
					return;
				}else if(diffNumb.doubleValue()<0&&this.getBillListPanel().getHeadBillModel().getRowCount()>0){
					MessageDialog.showHintDlg(this,"警告","存货明细钢厂重量总和小于表体到货重量,\n表体重量为:"+this.selectedArriveBody.getNarrvnum()+"\n 当前明细总重量为:"+detailSumweight.setScale(2,UFDouble.ROUND_HALF_UP)+"\n请检查!");
					return;
				}
			}
			int invDetailNum=this.getBillListPanel().getHeadBillModel().getRowCount();
			//明细件数总和
			UFDouble detailSum=new UFDouble(0);
			//明细VO集合,其中除了已存在的明细VO外,还包含了被删除的明细VO
			List<InvDetailVO> allInvDetailList=new ArrayList<InvDetailVO>();
			for(int i=0;i<invDetailVOs.length;i++){//向存货明细VO中注册一些系统信息.
				if(invDetailVOs[i].getPk_invdetail()==null){//如果明细主键为空,说明肯定是新增的记录
				invDetailVOs[i].setCarriveorder_bid(this.selectedArriveBody.getCarriveorder_bid());
				invDetailVOs[i].setCreatedate(new UFDate(new Date()));
				invDetailVOs[i].setCreateoperator(ClientEnvironment.getInstance().getUser().getPrimaryKey());
				invDetailVOs[i].setCreatetime(new UFDateTime(new Date()));
				invDetailVOs[i].setPk_invbasdoc(this.selectedArriveBody.getCbaseid());
				invDetailVOs[i].setPk_corp(ClientEnvironment.getInstance().getCorporation().getPrimaryKey());
				detailSum=detailSum.add(invDetailVOs[i].getArrivenumber());//累加总件数
				invDetailVOs[i].setDr(0);//初始化删除标记
				invDetailVOs[i].setStatus(VOStatus.NEW);//VO状态设置为 新增
				allInvDetailList.add((InvDetailVO)invDetailVOs[i].clone());
				}else if(invDetailVOs[i].getPk_invdetail()!=null){//如果明细主键不为空,那么判定是否有修改记录
					for(int j=0;j<this.oldInvDetailVOs.length;j++){
							if(oldInvDetailVOs[j].getPk_invdetail().equals(invDetailVOs[i].getPk_invdetail())){
								if(oldInvDetailVOs[j].getStatus()==VOStatus.UNCHANGED){
									invDetailVOs[i].setStatus(VOStatus.UNCHANGED);//VO状态设置为 未改变
									allInvDetailList.add((InvDetailVO)invDetailVOs[i].clone());
									detailSum=detailSum.add(invDetailVOs[i].getArrivenumber());//累加总件数
								}else if(oldInvDetailVOs[j].getStatus()==VOStatus.UPDATED){
									invDetailVOs[i].setDr(0);
									invDetailVOs[i].setModifyoperator(ClientEnvironment.getInstance().getUser().getPrimaryKey());
									invDetailVOs[i].setModifydate(new UFDate(new Date()));
									invDetailVOs[i].setModifytime(new UFDateTime(new Date()));
									invDetailVOs[i].setStatus(VOStatus.UPDATED);//VO状态设置为 已改变
									allInvDetailList.add((InvDetailVO)invDetailVOs[i].clone());
									detailSum=detailSum.add(invDetailVOs[i].getArrivenumber());//累加总件数
								}
							}else{
								if(j==oldInvDetailVOs.length){//如果遍历结束,仍然没有在旧VO数组中找到对应的记录,那么显然这是鬼搞出来的...
									MessageDialog.showHintDlg(this,"怎么可能?","出问题了吧哥们?!你是怎么做到的???");
								}
							}
						}
					}
				}
			//再遍历一次旧VO数组,将已删除的旧明细VO也加入到集合中
			for(int j=0;j<this.oldInvDetailVOs.length;j++){
				if(oldInvDetailVOs[j].getStatus()==VOStatus.DELETED){
					oldInvDetailVOs[j].setDr(1);
					oldInvDetailVOs[j].setModifyoperator(ClientEnvironment.getInstance().getUser().getPrimaryKey());
					oldInvDetailVOs[j].setModifydate(new UFDate(new Date()));
					oldInvDetailVOs[j].setModifytime(new UFDateTime(new Date()));
					oldInvDetailVOs[j].setStatus(VOStatus.UPDATED);//把删除看作修改DR字段的更新,将VO标记为 已改变
					allInvDetailList.add((InvDetailVO)oldInvDetailVOs[j].clone());//将已删除的明细添加进列表中
				}
			}
			//明细总和,与表体件数之差
			UFDouble diffNum=detailSum.sub(this.selectedArriveBody.getNassistnum());
			if(diffNum.doubleValue()>0&&this.getBillListPanel().getHeadBillModel().getRowCount()>0){
				MessageDialog.showHintDlg(this,"警告","存货明细支数总和超出表体到货件数,\n表体件数为:"+this.selectedArriveBody.getNassistnum()+"\n 当前明细件数为:"+detailSum.setScale(2,UFDouble.ROUND_HALF_UP)+"\n请检查!");
				return;
			}else if(diffNum.doubleValue()<0&&this.getBillListPanel().getHeadBillModel().getRowCount()>0){
				MessageDialog.showHintDlg(this,"警告","存货明细支数总和小于表体到货件数,\n表体件数为:"+this.selectedArriveBody.getNassistnum()+"\n 当前明细件数为:"+detailSum.setScale(2,UFDouble.ROUND_HALF_UP)+"\n请检查!");
				return;
			}
			//如果通过明细件数总数与表体件数的检查那么将集合中的所有记录转化成数组赋值给invDetailVos用于返回
			this.invDetailVOs=new InvDetailVO[allInvDetailList.size()];
			this.invDetailVOs=allInvDetailList.toArray(this.invDetailVOs);
			this.m_closeMark = true;
			this.closeOK();
		}
		// 增加按钮动作
		else if (e.getSource() == this.m_btnAdd) {
			getBillListPanel().getHeadBillModel().addLine();
			int selectedHeadRow=this.getBillListPanel().getHeadTable().getSelectedRow();
			//设置存货基本,管理 PK
			this.getBillListPanel().getHeadBillModel().setValueAt(this.selectedArriveBody.getCbaseid(),this.getBillListPanel().getHeadTable().getRowCount()-1, "pk_invbasdoc");
			this.getBillListPanel().getHeadBillModel().setValueAt(this.selectedArriveBody.getCmangid(),this.getBillListPanel().getHeadTable().getRowCount()-1, "pk_invmandoc");
			this.getBillListPanel().getHeadBillModel().setValueAt(this.selectedArriveBody.getCmangid(),this.getBillListPanel().getHeadTable().getRowCount()-1, "pk_invmandoc");
			this.getBillListPanel().getHeadBillModel().execLoadFormula();
			//setValueAt(this.selectedArriveBody.getCmangid(), this.getBillListPanel().getHeadBillModel().getRowCount()-1, "invname");
			getBillListPanel().getHeadBillModel().setEnabledAllItems(true);
			//设置存货名称,钢厂厚度,钢厂重量,验收厚宽长,验收重量,验收米数,换算率,不允许手动修改.
			getBillListPanel().getHeadBillModel().getItemByKey("invname").setEnabled(false);
			getBillListPanel().getHeadBillModel().getItemByKey("contractthick").setEnabled(false);
			getBillListPanel().getHeadBillModel().getItemByKey("contractweight").setEnabled(false);
			getBillListPanel().getHeadBillModel().getItemByKey("arrivethick").setEnabled(false);
			getBillListPanel().getHeadBillModel().getItemByKey("conversionrates").setEnabled(false);
			getBillListPanel().getHeadBillModel().getItemByKey("arrivewidth").setEnabled(false);
			getBillListPanel().getHeadBillModel().getItemByKey("arrivelength").setEnabled(false);
			getBillListPanel().getHeadBillModel().getItemByKey("arrivemeter").setEnabled(false);
			getBillListPanel().getHeadBillModel().getItemByKey("arriveweight").setEnabled(false);
			this.isCalculate=false;//将是否已计算按钮设定为false
			for(int i=0;i<this.getBillListPanel().getHeadTable().getRowCount();i++){
				this.getBillListPanel().getHeadBillModel().setValueAt(null, i, "contractweight");//将钢厂重量全部置空
			}
		}
		// 删除按钮动作
		else if (e.getSource() == this.m_btnDel) {
			int[] delRows = getBillListPanel().getHeadTable().getSelectedRows();
			for(int i=0;i<delRows.length;i++){//只有已存在的行可以处于Normal及Modification状态,新增的行将永远处于
				//所选择行的状态
				int selectedRowState=this.getBillListPanel().getHeadBillModel().getRowState(delRows[i]);
				if(selectedRowState==BillModel.ADD){
					//如果该行为新增行,那么不作操作
				}else if(selectedRowState==BillModel.NORMAL||selectedRowState==BillModel.MODIFICATION||selectedRowState==BillModel.DELETE){
					for(int j=0;j<oldInvDetailVOs.length;j++){
						if(oldInvDetailVOs[j].getPk_invdetail().equals(this.getBillListPanel().getHeadBillModel().getValueAt(delRows[i], "pk_invdetail"))){
							//如果该行为普通 或 修改状态行,那么将旧明细数组中对应的VO状态设置为已删除
							this.oldInvDetailVOs[j].setStatus(VOStatus.DELETED);
							//2010-12-22 MeiChao 增加 :判断是否下游已引用了存货明细,如有,则不允许删除该行. begin
							String checkPK=this.oldInvDetailVOs[j].getPk_invdetail();
							String checkSQL="select count(t.*) from scm_invdetail_c t where t.pk_invdetail='"+checkPK+"' and t.dr=0";
							IUAPQueryBS queryService=(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
							try {
								Object checkResult=queryService.executeQuery(checkSQL,new ColumnProcessor());
								if(checkResult!=null){
									MessageDialog.showErrorDlg(this,"错误","对不起,您选择的第"+(j+1)+"行记录已被引用,不允许删除.");
									return;
								}
							} catch (BusinessException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							//2010-12-22 MeiChao end
						}
					}
				}
			}
			getBillListPanel().getHeadBillModel().delLine(delRows);
			this.isCalculate=false;//将是否已计算按钮设定为false
			for(int i=0;i<this.getBillListPanel().getHeadTable().getRowCount();i++){
				this.getBillListPanel().getHeadBillModel().setValueAt(null, i, "contractweight");//将钢厂重量全部置空
			}
			if(this.getBillListPanel().getHeadBillModel().getRowCount()==0){//如果清空了所有明细.
				this.isCalculate=true;
				this.isLengthAllNumber=true;
			}
			
		}
		// 修改按钮动作
		else if (e.getSource() == this.m_btnMod) {
			getBillListPanel().getHeadBillModel().setEnabledAllItems(true);
			//设置存货名称,钢厂厚度,钢厂重量,验收厚宽长,验收重量,验收米数,换算率,不允许手动修改.
			getBillListPanel().getHeadBillModel().getItemByKey("invname").setEnabled(false);
			getBillListPanel().getHeadBillModel().getItemByKey("contractthick").setEnabled(false);
			getBillListPanel().getHeadBillModel().getItemByKey("contractweight").setEnabled(false);
			getBillListPanel().getHeadBillModel().getItemByKey("arrivethick").setEnabled(false);
			getBillListPanel().getHeadBillModel().getItemByKey("conversionrates").setEnabled(false);
			getBillListPanel().getHeadBillModel().getItemByKey("arrivewidth").setEnabled(false);
			getBillListPanel().getHeadBillModel().getItemByKey("arrivelength").setEnabled(false);
			getBillListPanel().getHeadBillModel().getItemByKey("arrivemeter").setEnabled(false);
			getBillListPanel().getHeadBillModel().getItemByKey("arriveweight").setEnabled(false);
			if(!this.isLengthAllNumber){//如果长度不为全数字
				this.getBillListPanel().getHeadBillModel().getItemByKey("contractweight").setEnabled(true);//将钢厂重量设置为允许输入
			}
		}
		//2010-12-24 MeiChao add 计算 按钮响应
		else if(e.getSource() == this.m_btnCalculate){
			/**
			 * 首先检查明细是否已维护完毕: 明细支数=所选行到货件数
			 */
			//获取明细VO数组
			invDetailVOs = (InvDetailVO[])getBillListPanel().getHeadBillModel().getBodyValueVOs(InvDetailVO.class.getName());
			//明细件数总和
			UFDouble detailSum=new UFDouble(0);
			for(int i=0;i<invDetailVOs.length;i++){
				detailSum=detailSum.add(invDetailVOs[i].getArrivenumber());
			}
			//明细总和,与表体件数之差
			UFDouble diffNum=detailSum.sub(this.selectedArriveBody.getNassistnum());
			if(diffNum.doubleValue()>0){
				MessageDialog.showHintDlg(this,"警告","存货明细支数总和超出表体到货件数,\n表体件数为:"+this.selectedArriveBody.getNassistnum()+"\n 当前明细件数为:"+detailSum.setScale(2,UFDouble.ROUND_HALF_UP)+"\n请检查!");
				return;
			}else if(diffNum.doubleValue()<0){
				MessageDialog.showHintDlg(this,"警告","存货明细支数总和小于表体到货件数,\n表体件数为:"+this.selectedArriveBody.getNassistnum()+"\n 当前明细件数为:"+detailSum.setScale(2,UFDouble.ROUND_HALF_UP)+"\n请检查!");
				return;
			}
			/**
			 * 如果通过明细件数总数与表体件数的检查,那么开始分配钢厂重量
			 */
			//2010-12-27 MeiChao分配前还需要检查一下钢厂长度字段是否存在字母
			//for(int i=0;i<invDetailVOs.length;i++){
				//if(invDetailVOs[i].getContractlength()!=null&&!this.isDoueric(invDetailVOs[i].getContractlength())){
				if(!this.isLengthAllNumber){
					MessageDialog.showErrorDlg(this,"错误", "\"长度\"字段存在非数值型数据,不支持自动分摊重量,请手工输入钢厂重量!");
					return;
				}
			//}
			//宽*长*件数总和 或 米数*件数的总和
			UFDouble invTotal=new UFDouble(0);
			try {
				for(int i=0;i<invDetailVOs.length;i++){
					if(invDetailVOs[0].getContractmeter()!=null){//只判断第一行记录,以确定是按厚宽长计算还是米数计算.认为当前存货所有明细都是一样的计数方式.
						invTotal=invTotal.add(new UFDouble(invDetailVOs[i].getContractmeter()).multiply(invDetailVOs[i].getArrivenumber()));
					}else{
						invTotal=invTotal.add(new UFDouble(invDetailVOs[i].getContractwidth()).multiply(new UFDouble(invDetailVOs[i].getContractlength())).multiply(invDetailVOs[i].getArrivenumber()));
					}
				}
			} catch (NumberFormatException e1) {
				MessageDialog.showErrorDlg(this,"提示","请输入正确的数据!");
				e1.printStackTrace();
				return;
			}
			//第二次遍历,根据总和作为基础值,按照比例分配钢厂重量,这一次遍历不需要try/catch.
			//当前所选择存货的钢厂总重量 ,即 到货数量
			UFDouble contractWeight=this.selectedArriveBody.getNarrvnum();
			//已分配重量,要保证分配后总重量与表体相符,故将要在最后1行进行一个总重量-已分配重量 的剩余重量的重新分配
			UFDouble isCalculatedWeight=new UFDouble(0);
			for(int i=0;i<invDetailVOs.length;i++){
				if(invDetailVOs[0].getContractmeter()!=null){//依旧只判断第一行记录,以确定是按厚宽长计算还是米数计算.认为当前存货所有明细都是一样的计数方式.
					this.getBillListPanel().getHeadBillModel()
							.setValueAt(
									((new UFDouble(invDetailVOs[i]
											.getContractmeter())
											.multiply(invDetailVOs[i]
													.getArrivenumber())
											.multiply(contractWeight))
											.div(invTotal)), i,
									"contractweight");
				}else{
					this.getBillListPanel().getHeadBillModel()
							.setValueAt(
									(new UFDouble(invDetailVOs[i]
											.getContractwidth()).multiply(
											new UFDouble(invDetailVOs[i]
													.getContractlength()))
											.multiply(
													invDetailVOs[i]
															.getArrivenumber())
											.multiply(contractWeight)
											.div(invTotal)), i,
									"contractweight");
				}
			}
			this.isCalculate=true;
		}

	}

	/**
	 * @function 关闭标志
	 * 
	 * @author MeiChao
	 * 
	 * @return boolean
	 * 
	 * @date 2010-8-20 上午10:24:20
	 */
	public boolean isCloseOK() {
		return m_closeMark;
	}

	/**
	 * @function 获取存货明细
	 * 
	 * @author MeiChao
	 * 
	 * @return InvDetailVO[]
	 * 
	 * @date 2010-12-14 上午10:24:46
	 */
	public InvDetailVO[] getinvDetailVOs() {
		return invDetailVOs;
	}
	private String voCheck(InvDetailVO[] invDetailVOs) {

	    for (int i = 0; i < invDetailVOs.length; i++) {
	    	
		}
	    return "";
	}

	public void afterEdit(BillEditEvent e) {
		//任何修改后将对应行状态修改标记为已修改
		int nowRowState=this.getBillListPanel().getHeadBillModel().getRowState(e.getRow());
		if(nowRowState==BillModel.ADD){
			//如果为新增,则不作操作.
		}else if(nowRowState==BillModel.NORMAL||nowRowState==BillModel.MODIFICATION){//如果为普通状态,那么标记之为修改状态.
			for(int i=0;i<oldInvDetailVOs.length;i++){
				if(oldInvDetailVOs[i].getPk_invdetail().equals(this.getBillListPanel().getHeadBillModel().getValueAt(e.getRow(), "pk_invdetail"))){
					this.getBillListPanel().getHeadBillModel().setRowState(e.getRow(),BillModel.MODIFICATION);
					this.oldInvDetailVOs[i].setStatus(VOStatus.UPDATED);//将对应的VO设置为已修改状态.
				}
			}
		}
		if(e.getKey().equals("ismny")){
			if(	(Boolean)e.getValue()){
			   getBillListPanel().getHeadItem("noriginalcurprice").setValue(null);
			   getBillListPanel().getHeadItem("noriginalcurprice").setEdit(false);
			   getBillListPanel().getHeadItem("noriginalcurmny").setEdit(true);
			}else{
				   getBillListPanel().getHeadItem("noriginalcurmny").setValue(null);
				   getBillListPanel().getHeadItem("noriginalcurmny").setEdit(false);
				   getBillListPanel().getHeadItem("noriginalcurprice").setEdit(true);
				}
			this.isCalculate=false;//任何修改后,将是否已计算按钮设定为false
			for(int i=0;i<this.getBillListPanel().getHeadTable().getRowCount();i++){
				this.getBillListPanel().getHeadBillModel().setValueAt(null, i, "contractweight");//将钢厂重量全部置空
			}
		}
		else if(e.getKey().equals("contractlength")){//2010-12-28 MeiChao
			String curcontractlength=this.getBillListPanel().getHeadBillModel().getValueAt(e.getRow(), "contractlength").toString();	
			if(!this.isDoueric(curcontractlength)){
				if(this.isLengthAllNumber){
					MessageDialog.showHintDlg(this,"提示","您输入了非纯数字类型的存货长度,存货重量自动计算将不可用,请手动输入!");
					this.isLengthAllNumber=false;
					this.getBillListPanel().getHeadBillModel().getItemByKey("contractweight").setEnabled(true);//将钢厂重量设置为允许输入
				}
			}	
			if(!this.isLengthAllNumber){//如果this.isLengthAllNumber标记为false,遍历判断是否所有存货长度均为数字.
				for(int i=0;i<this.getBillListPanel().getHeadBillModel().getRowCount();i++){
					curcontractlength=this.getBillListPanel().getHeadBillModel().getValueAt(i, "contractlength").toString();
					if(!this.isDoueric(curcontractlength)){
						break;
					}else if(i==this.getBillListPanel().getHeadBillModel().getRowCount()-1){
						this.isLengthAllNumber=true;
						this.getBillListPanel().getHeadBillModel().getItemByKey("contractweight").setEnabled(false);//将钢厂重量设置为不允许输入
						MessageDialog.showHintDlg(this,"提示","可以自动计算钢厂重量.");
					}
				}
			}
			this.isCalculate=false;//任何修改后,将是否已计算按钮设定为false
			for(int i=0;i<this.getBillListPanel().getHeadTable().getRowCount();i++){
				this.getBillListPanel().getHeadBillModel().setValueAt(null, i, "contractweight");//将钢厂重量全部置空
			}
		}else if(e.getKey().equals("contractweight")){//修改了钢厂重量
			String curcontractweight=this.getBillListPanel().getHeadBillModel().getValueAt(e.getRow(), "contractweight").toString();
			if(!this.isDoueric(curcontractweight)){
				MessageDialog.showHintDlg(this, "提示", "钢厂重量不允许输入非数字!");
				this.getBillListPanel().getHeadBillModel().setValueAt(null, e.getRow(),"contractweight");
				return;
			}
			for(int i=0;i<this.getBillListPanel().getHeadBillModel().getRowCount();i++){
				curcontractweight=this.getBillListPanel().getHeadBillModel().getValueAt(i, "contractweight").toString();
				if(!this.isDoueric(curcontractweight)){
					break;
				}else if(i==this.getBillListPanel().getHeadBillModel().getRowCount()-1){
					this.isCalculate=true;
				}
			}
		}else{
			this.isCalculate=false;//任何其他的修改后,将是否已计算按钮设定为false
			for(int i=0;i<this.getBillListPanel().getHeadTable().getRowCount();i++){
				this.getBillListPanel().getHeadBillModel().setValueAt(null, i, "contractweight");//将钢厂重量全部置空
			}
		}
		
	}

	

	public void bodyRowChange(BillEditEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * 判断参数组成是否为全数字(Double).  
	 * MeiChao
	 */
	public static boolean isDoueric(String str) {
    
	Pattern pattern = Pattern.compile("^[-+]?(\\d+(\\.\\d*)?|\\.\\d+)([eE]([-+]?([012]?\\d{1,2}|30[0-7])|-3([01]?[4-9]|[012]?[0-3])))?[dD]?$");
	if(str==null)
		return false;
	else
		return pattern.matcher(str).matches();

	}
	
	/**
	 * 判断存货/客商编码组成是否为全数字.  
	 * 
	 */
	public static boolean isNumeric(String str) {
    
	Pattern pattern = Pattern.compile("[0-9]*");

	return pattern.matcher(str).matches();

	}

	public boolean beforeEdit(BillEditEvent arg0) {
		// TODO Auto-generated method stub
		return false;
	}
	
	

}
