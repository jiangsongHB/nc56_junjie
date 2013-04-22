package nc.ui.so.so001.revise;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ArrayProcessor;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.pf.PfUtilClient;
import nc.ui.pub.query.QueryConditionClient;
import nc.ui.scm.so.SaleBillType;
import nc.ui.so.so001.order.SaleOrderPluginMenuUtil;
import nc.ui.so.so001.order.SoMdwhDlg;
import nc.ui.so.so001.panel.SaleBillUI;
import nc.vo.pub.BusinessException;
import nc.vo.so.so001.SaleOrderVO;
import nc.vo.so.so001.SaleorderBVO;
import nc.vo.so.so001.SaleorderHVO;

/**
 * 销售订单修订
 * 
 * @version	V5.1
 * 
 * @author zhongwei
 *
 */
public class SaleOrderReviseUI extends SaleBillUI {

	//按钮初始化标记
	private boolean b_init_btn;
	
	private boolean isInitQuery = false;
	
	public SaleOrderReviseUI(){
		super();
		super.b_isRevise = true;
		
		//v5.6连接次数降低，改为第一次查询时调用
		//resetQueryDlg();
		
		getBillCardPanel().setBillType(SaleBillType.SaleOrder);
		getBillListPanel().setBillType(SaleBillType.SaleOrder);
	}
	
	private void resetQueryDlg(){
		getQueryDlg();
		
		getQueryDlg().m_rdoAll.setSelected(false);
		getQueryDlg().m_rdoAudited.setSelected(true);
		
		getQueryDlg().m_rdoAll.setEnabled(false);
		getQueryDlg().m_rdoFree.setEnabled(false);
		getQueryDlg().m_rdoAuditing.setEnabled(false);
		getQueryDlg().m_rdoAudited.setEnabled(false);
		getQueryDlg().m_rdoFreeze.setEnabled(false);
//		getQueryDlg().m_rdoBatch.setEnabled(false);
		getQueryDlg().m_rdoBlankOut.setEnabled(false);
		getQueryDlg().m_rdoClosed.setEnabled(false);
		
	}
	
	/**
	 * 查询数据。
	 * 
	 */
	protected void onQuery() {

		if (!isInitQuery){
			isInitQuery = true;
			resetQueryDlg();
		}
		
		// 显示查询对话况
		if (getQueryDlg().showModal() == QueryConditionClient.ID_CANCEL)
			return;

		onRefresh();

	}
	
	@Override
	public String getBillButtonAction(ButtonObject bo) {
		return null;
	}
	
	@Override
	public String getBillButtonState() {
		return null;
	}
	
	@Override
	public ButtonObject[] getBillButtons() {
		if (!b_init_btn) {
			initBtnGrp();
			b_init_btn = true;
		}

		if (strShowState.equals("列表")) {
			return aryListButtonGroup;
		} else if (strShowState.equals("卡片")) {	
			return aryButtonGroup;
		} else {
			return aryBatchButtonGroup;
		}
	}
	
	/**
	 * V51要求卡片和列表的按钮一致
	 *
	 */
	private void initBtnGrp() {

		// 列表按钮
		aryListButtonGroup = new ButtonObject[] { boSave, boMaintain, boLine,
				boQuery, boBrowse, boCard, boPrntMgr, boAsstntQry };

		// 卡片按钮
		aryButtonGroup = new ButtonObject[] { boSave, boMaintain, boLine,
				boQuery, boBrowse, boReturn, boPrntMgr, boAsstntQry ,boMdmx};

		// 导入插件菜单
		ButtonObject[][] ret_butns = new SaleOrderPluginMenuUtil().addMenu(aryListButtonGroup, aryButtonGroup,
				aryBatchButtonGroup, bomButtonGroup, getModuleCode());

		this.aryListButtonGroup = ret_butns[0];
		this.aryButtonGroup = ret_butns[1];
		this.aryBatchButtonGroup = ret_butns[2];
		this.bomButtonGroup = ret_butns[3];
	}
	
	protected void initButtons(){

		// 业务类型
		/** 无论是否加载业务类型，按钮必须初始化，否则出现其它按钮状态刷新异常* */
		PfUtilClient.retBusinessBtn(boBusiType, getCorpPrimaryKey(),
				SaleBillType.SaleOrder);
		if (boBusiType.getChildButtonGroup() != null
				&& boBusiType.getChildButtonGroup().length > 0) {
			boBusiType.setTag(boBusiType.getChildButtonGroup()[0].getTag());
			boBusiType.getChildButtonGroup()[0].setSelected(true);
			boBusiType.setCheckboxGroup(true);
		}
		/** 无论是否加载业务类型，按钮必须初始化，否则出现其它按钮状态刷新异常* */

		// 维护
		boMaintain.removeAllChildren();
		boMaintain.addChildButton(boModification);
		boMaintain.addChildButton(boCancel);
		
		// 行操作
		boLine.removeAllChildren();
		boLine.addChildButton(boAddLine);
		boLine.addChildButton(boDelLine);
		boLine.addChildButton(boInsertLine);
		boLine.addChildButton(boCopyLine);
		boLine.addChildButton(boPasteLine);
		boLine.addChildButton(boPasteLineToTail);
		boLine.addChildButton(boFindPrice);
		boLine.addChildButton(boCardEdit);
		boLine.addChildButton(boResortRowNo);
		
		// 浏览
		boBrowse.removeAllChildren();
		boBrowse.addChildButton(boRefresh);
		boBrowse.addChildButton(boFind);
		boBrowse.addChildButton(boFirst);
		boBrowse.addChildButton(boPre);
		boBrowse.addChildButton(boNext);
		boBrowse.addChildButton(boLast);
		boBrowse.addChildButton(boListSelectAll);
		boBrowse.addChildButton(boListDeselectAll);
		
		//辅助查询
		boAsstntQry.removeAllChildren();
		boAsstntQry.addChildButton(boOrderQuery);
		boAsstntQry.addChildButton(boOnHandShowHidden);
		boAsstntQry.addChildButton(boAuditFlowStatus);
		boCustCredit.setTag("CustCredited");
		boAsstntQry.addChildButton(boCustCredit);
		boOrderExecRpt.setTag("OrderExecRpt");
		boAsstntQry.addChildButton(boOrderExecRpt);
		boCustInfo.setTag("CustInfo");
		boAsstntQry.addChildButton(boCustInfo);
		boPrifit.setTag("Prifit");
		boAsstntQry.addChildButton(boPrifit);

		//打印
		boPrntMgr.removeAllChildren();
		boPrntMgr.addChildButton(boPreview);
		boPrntMgr.addChildButton(boPrint);
		boPrntMgr.addChildButton(boSplitPrint);
		
		setButtons(getBillButtons());
	}
	
	@Override
	public String getBillID() {
		if ("退货".equals(strState)) { /*-=notranslate=-*/
			return id;
		}

		if (num >= 0
				&& num < getBillListPanel().getHeadBillModel().getRowCount())
			return (String) getBillListPanel().getHeadBillModel().getValueAt(
					num, "csaleid");
		else
			return null;
	}

	@Override
	public String getTitle() {
		return getBillListPanel().getBillListData().getTitle();
	}

	@Override
	public String getNodeCode() {
		return "40060301";
	}

	@Override
	protected String getClientUI() {
		return SaleOrderReviseUI.class.getName();
	}

	/**add by ouyangzhb 2013-04-22 订单修订增加码单锁定功能*/
	@Override
	public void onButtonClicked(ButtonObject bo) {
		// TODO Auto-generated method stub
		super.onButtonClicked(bo);

		
		if (bo == boMdmx) {
			SaleOrderVO oldvo =(SaleOrderVO) getBillCardPanel().getBillValueVO(
					SaleOrderVO.class.getName(), SaleorderHVO.class.getName(),
					SaleorderBVO.class.getName());
			getBillCardTools().setOldsaleordervo(oldvo);
			
			// 表头VO
			SaleorderHVO hvo = (SaleorderHVO) getVo().getParentVO().clone();
			if (hvo == null) {
				showWarningMessage("请选择要维护码单信息的单据！");
				return;
			}
			int introw = getBillCardPanel().getBodyPanel().getTable()
					.getSelectedRow();
			if (introw < 0) {
				showWarningMessage("请选择要维护码单信息的表体行！");
				return;
			}
			SaleorderBVO bvo = (SaleorderBVO) getBillCardPanel().getBillModel()
					.getBodyValueRowVO(introw, SaleorderBVO.class.getName()).clone();
			if (bvo.getCorder_bid() == null)
				return;

			// 判断是否进行码单管理
			try {
				if (querySfmdwf(bvo.getCinvbasdocid()) == false)
					throw new BusinessException(
							"当前存货不可以维护码单，不能码单锁定，请检查存货基本档案配置！");
			} catch (BusinessException e) {
				e.printStackTrace();
				showWarningMessage(e.getMessage());
				return;
			}
			
			SoMdwhDlg dlg = new SoMdwhDlg(hvo, bvo,this);
			dlg.showModal();
		}
	}
	
	public boolean onSave() {
		return super.onSave();
	}
	
	public void onModification(){
		super.onModification();
	}
	
	// 判断是否码单维护
	public boolean querySfmdwf(String cinvbasid) throws BusinessException {
		IUAPQueryBS iUAPQueryBS = (IUAPQueryBS) NCLocator.getInstance().lookup(
				IUAPQueryBS.class.getName());
		String sql = "select def20 from bd_invbasdoc where pk_invbasdoc='"
				+ cinvbasid + "'";
		Object[] objs = (Object[]) iUAPQueryBS.executeQuery(sql,
				new ArrayProcessor());
		if (objs == null || objs.length == 0)
			throw new BusinessException("当前存货异常，在存货基本档案中不存在！");
		if (objs[0] == null)
			return false;
		else {
			String bz = (String) objs[0];
			bz = bz.toUpperCase();
			if (bz.equals("Y"))
				return true;
			else
				return false;
		}
	}
	/**add by ouyangzhb 2013-04-22 订单修订增加码单锁定功能 end */
	
}
