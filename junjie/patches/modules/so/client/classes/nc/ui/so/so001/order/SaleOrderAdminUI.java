package nc.ui.so.so001.order;

import java.util.Vector;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ArrayProcessor;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.pf.PfUtilClient;
import nc.ui.scm.so.SaleBillType;
import nc.ui.so.so001.panel.SaleBillUI;
import nc.util.so.junjie.JunjiePubTool;
import nc.vo.pub.BusinessException;
import nc.vo.scm.ic.bill.InvVO;
import nc.vo.so.so001.SaleOrderVO;
import nc.vo.so.so001.SaleorderBVO;
import nc.vo.so.so001.SaleorderHVO;

/**
 * 销售订单管理 创建日期：(2001-4-13 15:26:05)
 * 
 * @author 宋杰
 * 
 * @rebuild V5.1 销售订单维护 zhongwei
 */
public class SaleOrderAdminUI extends SaleBillUI {

	// 按钮初始化标记
	private boolean b_init;
	
	// 调价  按钮---chenjianhua  2013-04-15
	protected ButtonObject boAdjustPrice;

	public SaleOrderAdminUI() {
		super();
	}

	protected void initCustomerUI() {
		// new OpenSaleOrderForCRM().openNode(this);//test
	}

	public SaleOrderAdminUI(String pk_corp, String billtype, String busitype,
			String operator, String id) {
		super(pk_corp, billtype, busitype, operator, id);
	}

	@Override
	public String getBillButtonAction(nc.ui.pub.ButtonObject bo) {
		return null;
	}

	@Override
	public String getBillButtonState() {
		return null;
	}

	/**
	 * 获得按钮数组。
	 * 
	 * 支持插件菜单,location规则如下：0 卡片列表;1 退货;2 BOM; 3 核销
	 * 
	 * 创建日期：(2001-11-15 8:58:51)
	 * 
	 * @return nc.ui.pub.ButtonObject[]
	 */
	public nc.ui.pub.ButtonObject[] getBillButtons() {
		if (!b_init) {
			initBtnGrp();
			b_init = true;
		}

		if (strShowState.equals("列表")) {
			return aryListButtonGroup;
		} else if (strShowState.equals("卡片")) {
			initLineButton();
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
		ButtonObject[] aryListButtonGroup = { boBusiType, boAdd, boSave,
				boMaintain, boLine, boAudit, boAction, boQuery, boBrowse,
				boCard, boPrntMgr, boAssistant, boAsstntQry };

		// 卡片按钮---增加 调价  按钮---chenjianhua  2013-04-15
		
		ButtonObject[] aryButtonGroup = { boBusiType, boAdd, boSave,
				boMaintain, boLine, boAudit, boAction, boQuery, boBrowse,
				boReturn, boPrntMgr, boAssistant, boAsstntQry, boMdmx,boAdjustPrice };

		// 退货卡片按钮
		ButtonObject[] aryBatchButtonGroup = { boBatch, boLine,
				boRefundmentDocument, boBack };

		// 配置销售按钮组
		ButtonObject[] bomButtonGroup = { boBomSave, boBomEdit, boBomCancel,
				boBomReturn, boOrderQuery,
		// boBomPrint
		};

		/*
		 * IFuncExtend funcExtend = getFuncExtend(); if (funcExtend != null) {
		 * ButtonObject[] boExtend = m_funcExtend.getExtendButton(); if
		 * (boExtend != null && boExtend.length > 0) { // 卡片按钮 ButtonObject[]
		 * botempcard = new ButtonObject[aryButtonGroup.length +
		 * boExtend.length];
		 * 
		 * System.arraycopy(aryButtonGroup, 0, botempcard, 0,
		 * aryButtonGroup.length);
		 * 
		 * System.arraycopy(boExtend, 0, botempcard, aryButtonGroup.length,
		 * boExtend.length);
		 * 
		 * aryButtonGroup = botempcard; // 列表按钮 ButtonObject[] botemplist = new
		 * ButtonObject[aryListButtonGroup.length + boExtend.length];
		 * 
		 * System.arraycopy(aryListButtonGroup, 0, botemplist, 0,
		 * aryListButtonGroup.length);
		 * 
		 * System.arraycopy(boExtend, 0, botemplist, aryListButtonGroup.length,
		 * boExtend.length);
		 * 
		 * aryListButtonGroup = botemplist; } } ButtonObject[] boExtend =
		 * getExtendBtns(); if (boExtend != null && boExtend.length > 0) { //
		 * 卡片按钮 ButtonObject[] botempcard = new
		 * ButtonObject[aryButtonGroup.length + boExtend.length];
		 * 
		 * System.arraycopy(aryButtonGroup, 0, botempcard, 0,
		 * aryButtonGroup.length);
		 * 
		 * System.arraycopy(boExtend, 0, botempcard, aryButtonGroup.length,
		 * boExtend.length);
		 * 
		 * aryButtonGroup = botempcard; // 列表按钮 ButtonObject[] botemplist = new
		 * ButtonObject[aryListButtonGroup.length + boExtend.length];
		 * 
		 * System.arraycopy(aryListButtonGroup, 0, botemplist, 0,
		 * aryListButtonGroup.length);
		 * 
		 * System.arraycopy(boExtend, 0, botemplist, aryListButtonGroup.length,
		 * boExtend.length);
		 * 
		 * aryListButtonGroup = botemplist; }
		 */

		// 导入插件菜单
		ButtonObject[][] ret_butns = new SaleOrderPluginMenuUtil().addMenu(
				aryListButtonGroup, aryButtonGroup, aryBatchButtonGroup,
				bomButtonGroup, getModuleCode());

		this.aryListButtonGroup = ret_butns[0];
		this.aryButtonGroup = ret_butns[1];
		this.aryBatchButtonGroup = ret_butns[2];
		this.bomButtonGroup = ret_butns[3];

	}

	@Override
	public String getBillID() {

		if ("退货".equals(strState)) { /*-=notranslate=-*/
			return id;
		}

		if (strShowState == "列表") {
			return (String) getBillListPanel().getHeadBillModel().getValueAt(
					num, "csaleid");
		} else if (strShowState == "卡片") {
			return (String) getBillCardPanel().getHeadItem("csaleid")
					.getValueObject();
		} else
			return null;
	}

	@Override
	public String getNodeCode() {
		return getModuleCode();
	}

	@Override
	public String getTitle() {
		return getBillCardPanel().getBillData().getTitle();
		// return getBillListPanel().getBillListData().getTitle();
	}

	protected void initButtons() {

		PfUtilClient.retBusiAddBtn(boBusiType, boAdd, getCorpPrimaryKey(),
				SaleBillType.SaleOrder);

		// 业务类型
		// PfUtilClient.retBusinessBtn(boBusiType, getCorpPrimaryKey(),
		// SaleBillType.SaleOrder);
		if (boBusiType.getChildButtonGroup() != null
				&& boBusiType.getChildButtonGroup().length > 0) {
			boBusiType.setTag(boBusiType.getChildButtonGroup()[0].getTag());
			boBusiType.getChildButtonGroup()[0].setSelected(true);
			boBusiType.setCheckboxGroup(true);
			// 新增
			// PfUtilClient.retAddBtn(boAdd, getCorpPrimaryKey(), getBillType(),
			// boBusiType.getChildButtonGroup()[0]);
		}

		// 维护
		boMaintain.removeAllChildren();
		boMaintain.addChildButton(boEdit);
		boMaintain.addChildButton(boCancel);
		boMaintain.addChildButton(boBlankOut);
		boMaintain.addChildButton(boCopyBill);

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

		// 执行
		retElseBtn("Order002", "Order001");
		retElseBtn("Order002", "Order003");

		// 辅助查询
		boAsstntQry.removeAllChildren();
		boAsstntQry.addChildButton(boOrderQuery);
		boAsstntQry.addChildButton(boOnHandShowHidden);
		boAsstntQry.addChildButton(boAuditFlowStatus);
		boAsstntQry.addChildButton(boCustCredit);
		boAsstntQry.addChildButton(boOrderExecRpt);
		boAsstntQry.addChildButton(boCustInfo);
		boAsstntQry.addChildButton(boPrifit);

		// 打印
		boPrntMgr.removeAllChildren();
		boPrntMgr.addChildButton(boPreview);
		boPrntMgr.addChildButton(boPrint);
		boPrntMgr.addChildButton(boSplitPrint);

		// 协同
		boAssistant.addChildButton(boCoPushPo);
		boAssistant.addChildButton(boCoRefPo);

		if (strShowState.equals("列表")) { /*-=notranslate=-*/
			setButtons(getBillButtons());
		}// end list
		else if (strShowState.equals("卡片")) { /*-=notranslate=-*/

			if (strState.equals("BOM")) {
				setButtons(bomButtonGroup);
			} else {
				setButtons(getBillButtons());
			}

		}// end card
	}

	public void setButtonsState() {
		super.setButtonsState();

		getPluginProxy().setButtonStatus();
	}

	@Override
	protected String getClientUI() {
		return SaleOrderAdminUI.class.getName();
	}

	/**
	 * 仿照onNew实现,相当于转单新增
	 * 
	 * @param vo
	 */
	public void addCRMInfo(SaleOrderVO vo) {
		try {
			// 切换到卡片
			strShowState = "卡片";
			strState = "自由";
			switchInterface();
			setButtons(getBillButtons());
			updateUI();

			// 销售不走询价：SA15--N,恢复模板的默认值
			if (!SA_15.booleanValue())
				getBillCardTools().resumeBillBodyItemsEdit(
						getBillCardTools().getSaleOrderItems_Price_Original());

			// PfUtilClient.childButtonClicked(boAdd, getCorpPrimaryKey(),
			// getNodeCode(), getClientEnvironment().getUser()
			// .getPrimaryKey(), getBillType(), this);
			vRowATPStatus = new Vector();

			boolean bisCalculate = getBillCardPanel().getBillModel()
					.isNeedCalculate();
			getBillCardPanel().getBillModel().setNeedCalculate(false);

			if (vo == null) {
				onNewBySelf();
			} else {
				// //指定一个业务类型
				getBillCardPanel().setBusiType(getSelectedBusyType());
				onNewByOther(new SaleOrderVO[] { vo });
			}

			getBillCardPanel().getBillModel().setNeedCalculate(bisCalculate);
			if (bisCalculate)
				getBillCardPanel().getBillModel().reCalcurateAll();
			binitOnNewByOther = false;

			// 设置表体菜单
			setBodyMenuItem(false);
			/** CRM销售机会传递过来的销售订单打开时编辑性设置 */
			// 放开业务类型编辑性
			if (null != getBillCardPanel().getHeadItem("cbiztype")) {
				getBillCardPanel().getHeadItem("cbiztype").setEnabled(true);
				getBillCardPanel().getHeadItem("cbiztype").setEdit(true);
			}

			if (null != getBillCardPanel().getHeadItem("cbiztypename")) {
				getBillCardPanel().getHeadItem("cbiztypename").setEnabled(true);
				getBillCardPanel().getHeadItem("cbiztypename").setEdit(true);
			}
			// 封死备注编辑性
			getBillCardPanel().getHeadItem("vnote").setEnabled(false);
			getBillCardPanel().getHeadItem("vnote").setEdit(false);
			/** CRM销售机会传递过来的销售订单打开时编辑性设置 */
			// 清除缓存数据
			getBillCardTools().clearCatheData();

			setButtonsState();
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	// 何意求增加开始 10-09-15
	@Override
	public void onButtonClicked(ButtonObject bo) {
		super.onButtonClicked(bo);
		if (bo == boMdmx) {
			
			SaleOrderVO oldvo =(SaleOrderVO) getBillCardPanel().getBillValueVO(
					SaleOrderVO.class.getName(), SaleorderHVO.class.getName(),
					SaleorderBVO.class.getName());
			getBillCardTools().setOldsaleordervo(oldvo);
			
			// 表头VO
			SaleorderHVO hvo = (SaleorderHVO) getVo().getParentVO();
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
					.getBodyValueRowVO(introw, SaleorderBVO.class.getName());
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
			
			//chenjianhua 2013-04-11
			//SoMdwhDlg dlg = new SoMdwhDlg(hvo, bvo);
			SoMdwhDlg dlg = new SoMdwhDlg(hvo, bvo,this);
			//end 2013-04-11
			
			dlg.showModal();
		}
		//调价  按钮---chenjianhua  2013-04-15
		if (bo == boAdjustPrice) {
			this.getCurrentBillNo();
			SaleOrderVO billvo=(SaleOrderVO) this.getVo();
			//this.getBillID();
			if(billvo==null){
				   MessageDialog.showHintDlg(this,"提示：","当前单据为空，请先查出要调价的销售单。"); 
				   return ;
			}
		    String cbiztype =  boBusiType.getTag();		  
		    String businame=JunjiePubTool.getNameByID("bd_busitype", "businame", "pk_busitype", cbiztype);
		    if(businame==null ||!businame.contains("销售费用流程")){
			  MessageDialog.showHintDlg(this,"提示："," 必须先选择销售费用流程！"); 
			   return ;
		    }		  
		    
		    try {
				super.onCopyBill();//复制
			} catch (Exception e) {
			 
				  e.printStackTrace();
				  MessageDialog.showErrorDlg(this,"提示：",e.getMessage()); 
				  return ;
			}
			//增加上下游关联---通过行号关联
			SaleorderHVO hvo = (SaleorderHVO) billvo.getParentVO();
			SaleorderBVO[]  bvos = billvo.getBodyVOs();
			String csaleid=hvo.getPrimaryKey();
			
			int rowcount=getBillCardPanel().getRowCount();
			String crowno=null;////行号
			SaleorderBVO bvo=null;
			for (int i = 0; i < rowcount; i++) {
				crowno= (String) getBillCardPanel().getBodyValueAt(i, "crowno");
				bvo=getSaleorderBVOByCrowno(crowno,bvos);
				getBillCardPanel().setBodyValueAt("30",i, "creceipttype");//来源单据类型
				getBillCardPanel().setBodyValueAt(csaleid,i, "csourcebillid");// 来源单据主表ID
				getBillCardPanel().setBodyValueAt(bvo.getPrimaryKey(),i, "csourcebillbodyid");// 来源单据附表ID				
			}		
		}//end 2013-04-15
	}
    
	//根据行号找出子表VO chenjianhua--2013-04-17
	private SaleorderBVO getSaleorderBVOByCrowno(String crowno, SaleorderBVO[] bvos) {
		if(crowno==null || bvos==null){
			return null;
		}
		for(int i=0;i<bvos.length;i++){
			if(crowno.equals(bvos[i].getCrowno())){
				return bvos[i];
			}
		}
		return null;
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

	// 何意求增加开始 10-09-15 end
	
	
	
	//chenjianhua  2013-04-11 便于调用
	public boolean onSave() {
		return super.onSave();
	}
	/**
	 * 初始化所有按钮对象，没有业务特性
	 * 
	 * 在初始化界面之前调用，子类可覆写
	 * chenjianhua 2013-04-15
	 * 
	 */
	protected void loadAllBtns() {
	    super.loadAllBtns();
		getBoAdjustPrice();
	}
	// chenjianhua 2013-04-15  增加调价  按钮
	protected ButtonObject getBoAdjustPrice() {
		if (boAdjustPrice == null) {
			boAdjustPrice = new ButtonObject("调价", "调价", "调价");
		}
		return boAdjustPrice;
	}
}
