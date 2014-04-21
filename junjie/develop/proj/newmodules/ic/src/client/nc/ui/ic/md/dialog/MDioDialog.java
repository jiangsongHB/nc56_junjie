package nc.ui.ic.md.dialog;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.itf.ic.md.IMDTools;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ArrayProcessor;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.ui.ic.jjpanel.InvDetailRef;
import nc.ui.ic.mdck.MDConstants;
import nc.ui.ic.pub.bill.Environment;
import nc.ui.ic.pub.bill.GeneralBillClientUI;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIButton;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UILabel;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.bill.BillCardBeforeEditListener;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillEditListener;
import nc.ui.pub.bill.BillEditListener2;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillItemEvent;
import nc.ui.pub.bill.IBillItem;
import nc.ui.trade.business.HYPubBO_Client;
import nc.uif.pub.exception.UifException;
import nc.vo.ic.jjvo.InvDetailCVO;
import nc.vo.ic.md.MdcrkVO;
import nc.vo.ic.pub.bill.GeneralBillItemVO;
import nc.vo.ic.pub.bill.GeneralBillVO;
import nc.vo.ic.xcl.MdxclVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.constant.ic.BillMode;
import nc.vo.scm.constant.ic.InOutFlag;

/**
 * 码单显示界面
 * 
 * @author zhoujie
 * @since 2010-09-02 17:34:34
 */
public class MDioDialog extends UIDialog implements ActionListener,
		BillEditListener, BillEditListener2 {

	private static final long serialVersionUID = 1L;

	GeneralBillClientUI ui;

	BillCardPanel cardPanel;

	UIPanel buttonPanel;

	UILabel bottomPanel;

	int panelStatus;

	int status;

	boolean canloaddata = false;

	boolean edited = false;
	//2010-12-22 MeiChao 修改钢厂重量时的重量临时重量
	public UFDouble oneMDfactoryweighttemp=new UFDouble(0);
	
	//2010-12-13 MeiChao  begin 添加 钢厂重量 
	public UFDouble factoryweight= new UFDouble(0);

	public UFDouble getFactoryweight() {
		return factoryweight;
	}

	public void setFactoryweight(UFDouble factoryweight) {
		this.factoryweight = factoryweight;
	}
	//2010-12-13 MeiChao end 添加 钢厂重量
	//2010-12-22 MeiChao begin add 存货明细子表List,用于储存码单与存货明细的对照关系.
	public List<InvDetailCVO> invDetailvsMD=new ArrayList<InvDetailCVO>();
	public List<InvDetailCVO> getInvDetailvsMD() {
		return invDetailvsMD;
	}
	public void setInvDetailvsMD(List<InvDetailCVO> invDetailvsMD) {
		this.invDetailvsMD = invDetailvsMD;
	}
	//2010-12-22 MeiChao end add 存货明细子表List
	public UFDouble ssfsl = new UFDouble(0);// 实收辅数量

	public UFDouble sssl = new UFDouble(0);// 实收数量

	public UFDouble nprice = new UFDouble(0);// 实入库单价

	public UFDouble nmny = new UFDouble(0);// 实入金额

	public UFDouble grossprice = new UFDouble(0);// 毛边单价

	public UFDouble grossweight = new UFDouble(0);// 毛边重量

	public UFDouble grosssumny = new UFDouble(0);// 毛边金额

	public UFDouble stuffprice = new UFDouble(0);// 正材单价

	public UFDouble stuffweight = new UFDouble(0);// 正材重量

	public UFDouble stuffsumny = new UFDouble(0);// 正材金额

	public UFBoolean sfsqmd = new UFBoolean(false); // 是否删除码单

	private String md_note;// 厚*宽*长

	private String md_zyh;// 资源号

	private boolean sfth = false;// 是否退货

	private String vfree1;// 自由项1
	
	//wanglei 2011-06-26 增加过滤已选择的明细，在参照中过滤掉。
	private HashMap hm_invdetailpk = new HashMap(); 

	public MDioDialog(GeneralBillClientUI ui, boolean sfth)
			throws BusinessException {
		super(ui, MDUtils.getBillNameByBilltype(ui.getBillType()) + "－码单维护");
		this.ui = ui;
		this.setSize(1024, 700);
		this.sfth = sfth;
//		this.getBillCardPanel().setComponentPopupMenu(null);
		init();
		//2010-12-20 初始化之后,根据当前入库单的来源单据是否为到货单,设置存货参照字段是否显示
		if("23".equals(this.getGeneralBillVO().getItemVOs()[this.getGenSelectRowID()].getCsourcetype())){
			this.getBillCardPanel().getBillModel().getItemByKey("invdetailref").setEnabled(true);
			UIRefPane invdetailref=(UIRefPane)this.getBillCardPanel().getBillModel().getItemByKey("invdetailref").getComponent();
			invdetailref.setMultiSelectedEnabled(true);//设置参照多选
			this.getBillCardPanel().getBillModel().getItemByKey("md_width").setEnabled(false);
			this.getBillCardPanel().getBillModel().getItemByKey("md_length").setEnabled(false);
			this.getBillCardPanel().getBillModel().getItemByKey("md_meter").setEnabled(false);
			
		}else{//如果来源单据不是到货单,那么隐藏存货参照字段.
			BillItem width=this.getBillCardPanel().getBillModel().getItemByKey("invdetailref");
			//修改 需求 不需要隐藏 存货参照字段
//			this.getBillCardPanel().hideBodyTableCol("invdetailref");
			this.getBillCardPanel().getBillModel().getItemByKey("md_width").setEnabled(true);
			this.getBillCardPanel().getBillModel().getItemByKey("md_length").setEnabled(true);
			this.getBillCardPanel().getBillModel().getItemByKey("md_meter").setEnabled(true);
			this.getBillCardPanel().getBillModel().getItemByKey("def1").setEnabled(true);
		}
	}

	GeneralBillVO getGeneralBillVO() {
		GeneralBillVO nowVObill = null;
		int rownow = -1;
		if (ui.getM_iCurPanel() == BillMode.Card) {
			nowVObill = ui.getM_voBill();
		} else {
			nowVObill = (GeneralBillVO) ui.getM_alListData().get(
					ui.getM_iLastSelListHeadRow());
		}
		// 是否退货
		if (sfth == true) {
			GeneralBillItemVO[] bvos = (GeneralBillItemVO[]) nowVObill
					.getChildrenVO();
			if (bvos != null && bvos.length > 0) {
				for (int i = 0; i < bvos.length; i++) {
					bvos[i].setNinassistnum(new UFDouble(-bvos[i]
							.getNoutassistnum().doubleValue()));
					bvos[i].setNinnum(new UFDouble(-bvos[i].getNoutnum()
							.doubleValue()));
				}
			}
		}

		return nowVObill;
	}

	int getGenSelectRowID() {
		int rownow = -1;
		if (ui.getM_iCurPanel() == BillMode.Card) {
			rownow = ui.getBillCardPanel().getBillTable().getSelectedRow();
		} else {
			rownow = ui.getBillListPanel().getBodyTable().getSelectedRow();
		}
		return rownow;
	}

	MdxclVO mdxclvo;

	/**
	 * 初始化表头数据
	 */
	private void initHeadData() throws BusinessException {
		// voname nc.vo.ic.pub.bill.GeneralBillItemVO
		GeneralBillVO nowVObill = getGeneralBillVO();
		// String WhID = "";
		String InvID = "";
		if (nowVObill != null && getGenSelectRowID() >= 0) {
			// WhID = (String)nowVObill.getHeaderValue("cwarehouseid");
			InvID = (String) nowVObill.getItemValue(getGenSelectRowID(),
					"cinventoryid");
		}
		getBillCardPanel().getHeadItem("invpk").setValue(InvID);// 存货档案PK
		getBillCardPanel().getHeadItem("realWeight").setValue(
				getGeneralBillVO().getItemValue(getGenSelectRowID(), "ninnum"));// 实收数量
		getBillCardPanel().execHeadLoadFormulas();

		// 初始化资源号、厚*宽*长
		GeneralBillItemVO itemvo = (GeneralBillItemVO) nowVObill
				.getChildrenVO()[getGenSelectRowID()];
		this.setMd_note(itemvo.getVuserdef2()); // 待备用字段确认
		this.setMd_zyh(itemvo.getVuserdef1()); // 待备用字段确认
		this.setVfree1(itemvo.getVfree1());// 待处理
		// 实入数量
		// this.setSssl((UFDouble) (getGeneralBillVO().getItemValue(
		// getGenSelectRowID(), "ninnum")));
		// 实入辅数量
		this.setSsfsl((UFDouble) (getGeneralBillVO().getItemValue(
				getGenSelectRowID(), "ninassistnum")));
		if (this.getSsfsl() == null || this.getSsfsl().doubleValue() == 0)
			throw new BusinessException("实收辅数量为空，不能维护码单！");

		String gg = (String) getBillCardPanel().getHeadItem("thickness")
				.getValueObject();// 规格
		UFDouble g;
		try {
			g = new UFDouble(gg);
		} catch (Exception e) {
			setMessage("该存货由于规格的原因,不能维护长度和宽度");
			getBillCardPanel().getBodyItem("md_width").setEdit(false);
			getBillCardPanel().getBodyItem("md_length").setEdit(false);
		}

		mdxclvo = new MdxclVO();
		mdxclvo.setDr(0);
		mdxclvo.setPk_corp(getEnvironment().getCorpID());
		mdxclvo.setCcalbodyidb((String) nowVObill.getHeaderValue("pk_calbody")); // 库存组织
		mdxclvo.setCwarehouseidb((String) nowVObill
				.getHeaderValue("cwarehouseid"));// 仓库
		mdxclvo.setCinvbasid((String) nowVObill.getItemValue(
				getGenSelectRowID(), "cinvbasid"));// 基本档案
		mdxclvo.setCinventoryidb(InvID);// 存货.

		// 初始化正材单价,如果表体正材单价为空，则取表体中的单价。如果不为空，则一直取正材单价
		GeneralBillItemVO itemVOa = ((GeneralBillItemVO[]) nowVObill
				.getChildrenVO())[getGenSelectRowID()];
		UFDouble stuffprice = itemVOa.getStuffprice();
		if (stuffprice == null || stuffprice.doubleValue() == 0) {
			UFDouble nprice = (UFDouble) nowVObill.getItemValue(
					getGenSelectRowID(), "nprice");
			if (nprice == null)
				nprice = new UFDouble(0);
			this.setStuffprice(nprice);
		} else
			this.setStuffprice(stuffprice);
		// 初始化毛边单价
		UFDouble grossprice = itemVOa.getGrossprice();
		if (grossprice != null && grossprice.doubleValue() != 0)
			getBillCardPanel().setHeadItem("grossprice", grossprice);
	}

	/**
	 * 初始化
	 */
	private void init() throws BusinessException {
		this.add(getBillCardPanel(), BorderLayout.CENTER);
		this.add(getButtonPanel(), BorderLayout.SOUTH);
		// this.add(getBottomPanel(), BorderLayout.SOUTH);BorderLayout.NORTH
		this.initData();
	}

	/**
	 * 设置状态栏消息条
	 * 
	 * @param message
	 */
	void setMessage(String message) {
		getBottomPanel().setText("  " + message);
	}

	/**
	 * 初始化数据
	 */
	private void initData() throws BusinessException {
		this.initPanelStatus();
		this.initHeadData();
		this.initBodyData();
		if (this.status == MDUtils.INIT_CANEDIT) {
			String cwarehouseid = (String) getGeneralBillVO().getHeaderValue(
					"cwarehouseid");
			UIRefPane panel = (UIRefPane) getBillCardPanel().getBodyItem("box")
					.getComponent();
			panel.getRefModel().addWherePart(
					" and bd_stordoc.pk_stordoc='" + cwarehouseid + "'");
		}
	}

	/**
	 * 初始化界面状态
	 */
	void initPanelStatus() {
		if (ui.getM_iMode() == BillMode.Browse) {
			if (getGeneralBillVO() == null) {
				setMessage("单据界面没有数据,无法操作...");
				setStatus(MDUtils.INIT);
			} else if (getGeneralBillVO().getItemVOs() == null) {
				setMessage("单据表体没有数据,无法操作...");
				setStatus(MDUtils.INIT);
			} else if (getGenSelectRowID() == -1) {
				setMessage("单据表体没有选中数据,无法操作...");
				setStatus(MDUtils.INIT);
			} else {
				canloaddata = true;
				if (getGeneralBillVO().getHeaderValue("fbillflag").equals(2)) {
					setMessage("");
					setStatus(MDUtils.INIT_CANEDIT);
				} else {
					setMessage("单据非自由态,无法编辑码单信息...");
					setStatus(MDUtils.INIT);
				}
			}
		} else {
			setMessage("只有在浏览单据时才可以维护码单信息...");
			setStatus(MDUtils.INIT);
		}
	}

	/*
	 * /** 重写窗口关闭按钮 使之失效
	 */
	/*
	 * @Override protected void processWindowEvent(WindowEvent e) { }
	 */

	/**
	 * 初始化表体数据
	 */
	private void initBodyData() {
		if (canloaddata) {
			// getGeneralBillVO().getItemValue(getGenSelectRowID(),
			// "cgeneralbid")
			try {
				MdcrkVO[] vos = (MdcrkVO[]) HYPubBO_Client.queryByCondition(
						MdcrkVO.class, " cgeneralbid='"
								+ getGeneralBillVO().getItemValue(
										getGenSelectRowID(), "cgeneralbid")
								+ "' and isnull(dr,0)=0   ");
				if (vos != null && vos.length > 0) {
					getBillCardPanel().getBillModel().setBodyDataVO(vos);
					getBillCardPanel().getBillModel().execLoadFormula();
					//2010-12-22 MeiChao add begin
				 if("23".equals(this.getGeneralBillVO().getItemVOs()[this.getGenSelectRowID()].getCsourcetype())){
					//获取当前码单对应的存货明细子表VO
					InvDetailCVO[] invDetailCVOs=(InvDetailCVO[])HYPubBO_Client.queryByCondition(
							InvDetailCVO.class, " pk_mdcrk in (select pk_mdcrk from nc_mdcrk where  cgeneralbid='"
							+ getGeneralBillVO().getItemValue(
									getGenSelectRowID(), "cgeneralbid")
							+ "' and isnull(dr,0)=0  ) and isnull(dr,0)=0  ");
					Logger.debug("MDioDialog:码单VO长:"+vos.length+"明细VO长:"+invDetailCVOs.length);
					for(int i=0;i<invDetailCVOs.length;i++){//组织成List集合
						this.invDetailvsMD.add((InvDetailCVO)invDetailCVOs[i].clone());
						if(this.getBillCardPanel().getBillModel().getValueAt(i,"pk_mdcrk").equals(invDetailCVOs[i].getPk_mdcrk())){
							this.getBillCardPanel().getBillModel().setValueAt(invDetailCVOs[i].getPk_invdetail(), i, "pk_invdetail");
						}else{
							for(int j=0;j<invDetailCVOs.length;j++){
								if(invDetailCVOs[j].getPk_mdcrk().equals(this.getBillCardPanel().getBillModel().getValueAt(j,"pk_mdcrk"))){
									this.getBillCardPanel().getBillModel().setValueAt(invDetailCVOs[j].getPk_invdetail(), j, "pk_invdetail");
								}
							}
						}
					}
				 }
					//2010-12-22 MeiChao add end
				}else{//2010-12-01 MeiChao begin添加此else判断,当前无已存在码单信息时.
					//尝试去寻找相同上游单据的对应其他出库单对应的表体存货下的码单信息.(有点绕)
					vos = (MdcrkVO[]) HYPubBO_Client.queryByCondition(
							MdcrkVO.class, " isnull(dr,0)=0 and cgeneralbid=(select t.cgeneralbid " +
									"from ic_general_b t where t.csourcebillbid=" +
									"(select t.csourcebillbid from ic_general_b t where t.cgeneralbid='" 
									+ getGeneralBillVO().getItemValue(
											getGenSelectRowID(), "cgeneralbid")
									+ "' and t.dr=0) and t.cbodybilltypecode='4I' and t.dr=0)");
					if (vos != null && vos.length > 0) {
						//如果成功查询到对应的码单VO,那么将其当中的某几项修改掉: 
						//pk_mdcrk,cbodybilltypecode,pk_mdxcl_b,cgeneralbid,cwarehouseidb,cspaceid,jbh,voperatorid
						for(int i=0;i<vos.length;i++){
							vos[i].setPk_mdcrk(null);//码单PK设置为空
							vos[i].setCbodybilltypecode(this.ui.getBillTypeCode());
							vos[i].setPk_mdxcl_b(null);
							vos[i].setCgeneralbid(getGeneralBillVO().getItemValue(getGenSelectRowID(), "cgeneralbid").toString());
							vos[i].setCwarehouseidb(getGeneralBillVO().getHeaderVO().getCwarehouseid());
							vos[i].setCspaceid(null);
							//vos[i].setJbh(null);
							vos[i].setVoperatorid(ClientEnvironment.getInstance().getUser().getPrimaryKey());
						}
					getBillCardPanel().getBillModel().setBodyDataVO(vos);
					getBillCardPanel().getBillModel().execLoadFormula();
					}
				}
				//2010-12-01 MeiChao end
			} catch (UifException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 设置按钮与单据状态
	 * 
	 * @param status
	 * @see nc.ui.ic.md.dialog.MDUtils#INIT
	 * @see nc.ui.ic.md.dialog.MDUtils#INIT_CANEDIT
	 * @see nc.ui.ic.md.dialog.MDUtils#EDITING
	 */
	void setStatus(int status) {
		edited = false;
		this.status = status;
		switch (status) {
		case MDUtils.INIT:
			setPanelEnable(false);
			System.out.println("初始不可编辑...");
			break;
		case MDUtils.INIT_CANEDIT:
			setPanelEnable(false);
			getUIButton(MDUtils.EDIT_BUTTON).setEnabled(true);
			System.out.println("可编辑状态...");
			break;
		case MDUtils.EDITING:
			setPanelEnable(true);
			getUIButton(MDUtils.EDIT_BUTTON).setEnabled(false);
			setMessage("维护码单信息");
			break;
		default:
			break;
		}
	}

	public UILabel getBottomPanel() {
		if (bottomPanel == null) {
			bottomPanel = new UILabel();
			bottomPanel.setPreferredSize(new Dimension(1024, 20));
			// bottomPanel.setBorder(BorderFactory.createLoweredBevelBorder());
			bottomPanel.setRequestFocusEnabled(false);
		}
		return bottomPanel;
	}

	UIButton getUIButton(String name) {
		for (Component com : getButtonPanel().getComponents()) {
			if (com instanceof UIButton) {
				UIButton button = (UIButton) com;
				if (button.getText().equals(name)) {
					return button;
				}
			}
		}
		return MDUtils.BLANK_BUTTON;
	}

	void setPanelEnable(boolean isEnadble) {
		for (Component com : getButtonPanel().getComponents()) {
			if (com instanceof UIButton) {
				UIButton button = (UIButton) com;
				if (!button.getText().equals(MDUtils.CANCEL_BUTTON)) {
					button.setEnabled(isEnadble);
				}
			}
		}
		getBillCardPanel().setEnabled(isEnadble);
	}

	/**
	 * 初始化按钮面板
	 * 
	 * @see nc.ui.ic.md.dialog.MDUtils#getButtons()
	 * @return 拥有按钮的面板
	 */
	public UIPanel getButtonPanel() {
		if (buttonPanel == null) {
			buttonPanel = new UIPanel();
			// buttonPanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
			buttonPanel.setPreferredSize(new Dimension(1024, 50));
			for (String button_code : MDUtils.getButtons()) {
				UIButton button = new UIButton(button_code);
				buttonPanel.add(button);
				button.addActionListener(this);

			}
		}
		return buttonPanel;
	}

	BillCardPanel getBillCardPanel() {
		if (cardPanel == null) {
			cardPanel = new BillCardPanel();

			cardPanel.loadTemplet("MD01", "MD01", getEnvironment().getUserID(),
					getEnvironment().getCorpID());
			cardPanel.setRowNOShow(true);
			cardPanel.setTatolRowShow(true);
			//cardPanel.setShowMenuBar(false);
			//cardPanel.setComponentPopupMenu(null);
			cardPanel.getBodyPanel().setBBodyMenuShow(false);  //wanglei 2011-07-26 禁用右键菜单
			cardPanel.addEditListener(this);
			cardPanel.addBillEditListenerHeadTail(this);
			cardPanel.addBodyEditListener2(this);
			cardPanel.setSize(new Dimension(1024, 650));
			// cardPanel.addLine();
		}
		return cardPanel;
	}

	private Environment getEnvironment() {
		if (ui != null) {
			return ui.getEnvironment();
		}
		return null;
	}

	public void actionPerformed(ActionEvent e) {
		try {
			if (e.getSource() instanceof UIButton) {
				UIButton button = (UIButton) e.getSource();
				if (button.getText().equals(MDUtils.ADDLINE_BUTTON)) {
					onAddline();
				} else if (button.getText().equals(MDUtils.DELLINE_BUTTON)) {
					onDelline();
				} else if (button.getText().equals(MDUtils.SAVE_BUTTON)) {
					onSave();
				} else if (button.getText().equals(MDUtils.CALC_BUTTON)) {
					onCalc();
				} else if (button.getText().equals(MDUtils.CANCEL_BUTTON)) {
					if (this.status == MDUtils.EDITING) {
						int i = MessageDialog.showYesNoDlg(this, "提示",
								"正在维护码单信息,是否要取消操作?");
						if (i == this.ID_YES) {
							closeCancel();
						}
					} else
						closeCancel();
				} else if (button.getText().equals(MDUtils.EDIT_BUTTON)) {
					onEdit();
				} else {
					onElse(button);
				}
			}
		} catch (Exception e2) {
			e2.printStackTrace();
			MessageDialog.showErrorDlg(this, "异常", e2.getMessage());
		}
	}

	@Override
	public void closeCancel() {
		super.closeCancel();
		// 刷新界面
		// ui.getButtonManager().onButtonClicked(ui.getButtonManager().getButton(ICButtonConst.BTN_BROWSE_REFRESH));
	}

	private void onEdit() {
		setStatus(MDUtils.EDITING);
		GeneralBillVO nowVObill = getGeneralBillVO();
		String InvID = "";
		if (nowVObill != null && getGenSelectRowID() >= 0) {
			// WhID = (String)nowVObill.getHeaderValue("cwarehouseid");
			InvID = (String) nowVObill.getItemValue(getGenSelectRowID(),
					"cinventoryid");
		}
		// 初始化毛边单价输入框
		boolean sfmbjs;
		try {
			sfmbjs = CheckSfmbjs(InvID);
			if (sfmbjs == true)
				getBillCardPanel().getHeadItem("grossprice").setEdit(true);
			else
				getBillCardPanel().getHeadItem("grossprice").setEdit(false);
		} catch (BusinessException e) {
			e.printStackTrace();
		}

		//getBillCardPanel().getBillModel().getItemByKey("invdetailref").setRefType("<nc.ui.ic.jjpanel.InvDetailRef>");
		//wanglei 2013-12-13 效率问题调整，取消模板的参照设置。
		getBillCardPanel().getBillModel().getItemByKey("invdetailref").setDataType(IBillItem.UFREF);  //UFREF 
		//getBillCardPanel().getBillModel().getItemByKey("invdetailref").setRefType("<nc.ui.ic.jjpanel.InvDetailRef>");
		//UIRefPane ref = (UIRefPane) this.getBillCardPanel().getBillModel().getItemByKey("invdetailref").getComponent();
		UIRefPane ref = new UIRefPane();
		ref.setRefModel(new nc.ui.ic.jjpanel.InvDetailRef());
		ref.setIsCustomDefined(true);
		ref.setCacheEnabled(false);
		getBillCardPanel().getBillModel().getItemByKey("invdetailref").setComponent(ref);
//		getBillCardPanel().updateUI();
		//end 2013-12-13
	}

	private void onDelline() {
		//wanglei 2011-07-26 add
		String invdetailpk = null;

//		invdetailpk = (String) this.getBillCardPanel().getBodyValueAt(this.getBillCardPanel().getBillTable().getSelectedRow(), "pk_invdetail") ;
//		hm_invdetailpk.remove(invdetailpk);
//		
//		getBillCardPanel().delLine();

		//wanglei 2011-08-09 add 支持删除多行
		
		int[] ir =  this.getBillCardPanel().getBillTable().getSelectedRows();
		if (ir.length <= 0 )
			return;
		for (int i = ir.length -1 ; i>=0; i-- ) {
			invdetailpk = (String) this.getBillCardPanel().getBodyValueAt(ir[i], "pk_invdetail") ;
			hm_invdetailpk.remove(invdetailpk);
//			getBillCardPanel().remove(ir[i]);
		}	
		getBillCardPanel().getBillModel().delLine(ir);

		setMessage("删除一行数据...");
			setZLNULl();
		edited = true;
	}

	private void onElse(UIButton button) {

	}

	private void onCalc() throws BusinessException {
		
		String ispj = (String) getBillCardPanel().getHeadItem("ispj")
				.getValueObject();
		MdcrkVO[] mdvos = getBodyVOs();
		//2010-12-22 MeiChao 计算前先保存存货明细参照
		InvDetailCVO[] invDetailCVOsTemp=this.getInvDetailVOs();
		
		if (mdvos.length < 1)
			throw new BusinessException("码单表体没有数据！");
		// 较验数据
		UFDouble sum_srkzs = new UFDouble(0);
		boolean ispjboolean = ispj.equals("true");
		for (int i = 0; i < mdvos.length; i++) {
			mdvos[i].setDef4(new UFBoolean(false)); // 是否非计算
			mdvos[i].setSfbj(new UFBoolean(ispjboolean)); // 是否磅计
			if (mdvos[i].getSrkzs() == null
					|| mdvos[i].getSrkzs().doubleValue() == 0)
				throw new BusinessException("第" + (i + 1) + "行，支数不能为空！");
			sum_srkzs = sum_srkzs.add(mdvos[i].getSrkzs());
		}
		if (sum_srkzs.doubleValue() != this.getSsfsl().doubleValue())
			throw new BusinessException("码单入库总支数" + sum_srkzs.doubleValue()
					+ "不等于实入库辅数量" + this.getSsfsl().doubleValue());
		UFDouble num = new UFDouble((String) getBillCardPanel().getHeadItem(
				"realWeight").getValueObject());
		BillItem isgczlitem =  getBillCardPanel().getHeadItem("isgczl");
		String isgczl = null;
		if(isgczlitem!=null){
			 isgczl =  (String)isgczlitem.getValueObject();
		}
		
		if (ispj == null || ispj.equals("false")) {
			// 理计正材重量
			mdvos = MDUtils.mdLJ(mdvos);
			
			/** add by ouyangzhb 2012-03-21 计算钢厂重量 begin */
			if (isgczlitem != null && isgczl != null
					&& new UFBoolean(isgczl).booleanValue()) {
				mdvos = MDUtils.mdGCLJ(mdvos);
				for (int i = 0; i < mdvos.length; i++) {
					mdvos[i].setSfgczl(UFBoolean.TRUE);
				}
			} else {
				for (int i = 0; i < mdvos.length; i++) {
					mdvos[i].setSfgczl(UFBoolean.FALSE);
				}
			}
			/** add by ouyangzhb 2012-03-21 计算钢厂重量 end */
			
			// 判断当前存货是否需要进行毛边计算
			GeneralBillVO nowVObill = getGeneralBillVO();
			String InvID = "";
			if (nowVObill != null && getGenSelectRowID() >= 0) {
				InvID = (String) nowVObill.getItemValue(getGenSelectRowID(),
						"cinventoryid");
			}
			boolean sfmbjs = CheckSfmbjs(InvID);
			// 理计毛边的重量及金额，正材重量及金额，全部的单价
			if (sfmbjs == true)
				mdvos = mdMbjs(mdvos);
			else
				this.setNprice(this.getStuffprice());// 正材单价stuffprice
		} else {
			//mdvos = MDUtils.mdBJ(mdvos,new UFDouble(this.ui.getBillCardPanel().getBillModel("table").getValueAt(this.ui.getBillCardPanel().getBillTable("table").getSelectedRow(), "vuserdef19").toString()));
			mdvos = MDUtils.mdBJ(mdvos,num);
			/** add by ouyangzhb 2012-03-21 计算钢厂重量 begin */
			if (isgczlitem != null && isgczl != null
					&& new UFBoolean(isgczl).booleanValue()) {
				mdvos = MDUtils.mdGCBJ(mdvos, num);
				for (int i = 0; i < mdvos.length; i++) {
					mdvos[i].setSfgczl(UFBoolean.TRUE);
				}
			} else {
				for (int i = 0; i < mdvos.length; i++) {
					mdvos[i].setSfgczl(UFBoolean.FALSE);
				}
			}
			/** add by ouyangzhb 2012-03-21 计算钢厂重量 end */
			
			this.setNprice(this.getStuffprice());// 正材单价stuffprice
		}
		/**add by ouyangzhb 2012-03-20 计算钢厂重量 begin*/
		
		
			
			
			
		
		
		
		
		//获取表体选中行VO
		GeneralBillItemVO selectedBody=(GeneralBillItemVO)this.ui.getBillCardPanel().getBillModel("table").getBodyValueRowVO(this.ui.getBillCardPanel().getBillTable("table").getSelectedRow(), GeneralBillItemVO.class.getName());
		//if(selectedBody.getCsourcetype()!=null&&selectedBody.getCsourcetype().equals("21")){//如果上游单据为采购订单
		
		/*add by ouyangzhb 
		 * 问题号0000121,由于之前的设计是 当点击计算时，“钢厂总量”需要计算出来。现在改为自动带出的数量，所以需要注释一下代码。
		 * add begin 2011-02-19
		if(true){
		//平摊维护码单前的实收数量到码单钢厂重量
			//表体钢厂总重量
			UFDouble factoryWeight=new UFDouble(selectedBody.getNinnum());
			//分配基数
			UFDouble total=new UFDouble(0);
			if(mdvos[0].getMd_meter()!=null){//如果任意1行中米数不为空,那么表示以米数和支数来计数
				//码单米数*支数的总和
				for(int i=0;i<mdvos.length;i++){
					total=total.add(mdvos[i].getMd_meter().multiply(mdvos[i].getSrkzs()));
				}
				//已分配重量
				UFDouble givedNum=new UFDouble(0);
				for(int i=0;i<mdvos.length;i++){
					mdvos[i].setDef1(factoryWeight.multiply(mdvos[i].getMd_meter().multiply(mdvos[i].getSrkzs()).div(total)).setScale(3, UFDouble.ROUND_HALF_UP));
					if(i==mdvos.length-1){
						mdvos[i].setDef1(factoryWeight.sub(givedNum.setScale(3, UFDouble.ROUND_HALF_UP)));
					}else{
						givedNum=givedNum.add(mdvos[i].getDef1()).setScale(3, UFDouble.ROUND_HALF_UP);
					}
				}
			}else{//否则,按宽*长*支数 来计数
			    //码单宽*长*支数的总和
				for(int i=0;i<mdvos.length;i++){
					total=total.add(mdvos[i].getMd_length().multiply(mdvos[i].getMd_width()).multiply(mdvos[i].getSrkzs()));
				}
				//已分配重量
				UFDouble givedNum=new UFDouble(0);
				for(int i=0;i<mdvos.length;i++){
					mdvos[i].setDef1(factoryWeight.multiply(mdvos[i].getMd_length().multiply(mdvos[i].getMd_width()).multiply(mdvos[i].getSrkzs()).div(total)).setScale(3, UFDouble.ROUND_HALF_UP));
					if(i==mdvos.length-1){
						mdvos[i].setDef1(factoryWeight.sub(givedNum.setScale(3, UFDouble.ROUND_HALF_UP)));
					}else{
						givedNum=givedNum.add(mdvos[i].getDef1()).setScale(3, UFDouble.ROUND_HALF_UP);
					}
				}
			}
		}
		*add end 2011-02-19
		*/
		getBillCardPanel().getBillData().setBodyValueVO(mdvos);
		getBillCardPanel().getBillModel().execLoadFormula();
		
		for(int i=0;i<invDetailCVOsTemp.length;i++){
			String str = invDetailCVOsTemp[i].getPk_invdetail();
			this.getBillCardPanel().setBodyValueAt(invDetailCVOsTemp[i].getPk_invdetail(), i, "pk_invdetail");
		}
		// setMessage("计算成功...");
		edited = true;
		MessageDialog.showWarningDlg(this, "提示", "计算成功!");
	}

	MdcrkVO[] getBodyVOs() {
		MdcrkVO[] rsvo = (MdcrkVO[]) getBillCardPanel().getBillData()
				.getBodyValueVOs(MdcrkVO.class.getName());
		if (rsvo == null || rsvo.length == 0)
			return rsvo;
		boolean bj = getBillCardPanel().getHeadItem("ispj").getValue().equals(
				"true");
		boolean fjs = getBillCardPanel().getHeadItem("fjs").getValue().equals(
				"true");
		for (int i = 0; i < rsvo.length; i++) {
			rsvo[i].setSfbj(new UFBoolean(bj));
			rsvo[i].setDef4(new UFBoolean(fjs));
		}
		return rsvo;
	}

	/**
	 * 修改了表体数据需要重新计算重量
	 */
	void setZLNULl() {
		int i = getBillCardPanel().getRowCount();
		if (i > 0) {
			getBillCardPanel().getBillModel().setValueAt(null, i - 1, "srkzl");// 修改了表体数据需要重新计算重量
		}
	}

	private void onSave() throws BusinessException {
		getBillCardPanel().dataNotNullValidate();
		if (edited) {
			MdcrkVO[] mdvos = getBodyVOs();
			InvDetailCVO[] InvDetailCVOs=this.getInvDetailVOs();//获取页面上的存货明细子表信息
			// 是否非计算
			String fjs = (String) getBillCardPanel().getHeadItem("fjs")
					.getValueObject();
			if (fjs != null && !fjs.equals("false")) {
				// 较验数据
				UFDouble sum_srkzs = new UFDouble(0);
				for (int i = 0; i < mdvos.length; i++) {
					if (mdvos[i].getSrkzs() == null
							|| mdvos[i].getSrkzs().doubleValue() == 0)
						throw new BusinessException("第" + (i + 1) + "行，支数不能为空！");
					sum_srkzs = sum_srkzs.add(mdvos[i].getSrkzs());
				}
				if (sum_srkzs.doubleValue() != this.getSsfsl().doubleValue())
					throw new BusinessException("码单入库总支数"
							+ sum_srkzs.doubleValue() + "不等于实入库辅数量"
							+ this.getSsfsl().doubleValue());
			}
			
			//add by ouyangzhb 2012-06-25 码单维护的时候，判断 货位+件编号 是否唯一
			ArrayList jphList = new ArrayList();
			for (int i = 0; i < mdvos.length; i++){
				if(jphList.contains(mdvos[i].getCspaceid()+mdvos[i].getJbh())){
					MessageDialog.showErrorDlg(this, "提示", "货位+件编号不唯一！");
					return;
				}
				jphList.add(mdvos[i].getCspaceid()+mdvos[i].getJbh());
			}
			//add by ouyangzhb end 

			IMDTools tools = NCLocator.getInstance().lookup(IMDTools.class);
			//根据来源单据类型是否为到货单(23)以及存货明细子表数组是否成功组织,决定是否传入存货明细子表进入保存接口
			if("23".equals(this.getGeneralBillVO().getItemVOs()[this.getGenSelectRowID()].getCsourcetype())&&InvDetailCVOs!=null&&InvDetailCVOs.length>0){
				tools.saveMDrk(mdvos, mdxclvo, (String) getGeneralBillVO()
						.getItemValue(getGenSelectRowID(), "cgeneralbid"),InvDetailCVOs);
			}else{
				tools.saveMDrk(mdvos, mdxclvo, (String) getGeneralBillVO()
						.getItemValue(getGenSelectRowID(), "cgeneralbid"),null);
			}
			UFDouble sum_sssl = new UFDouble(0);
			UFDouble sum_factoryWeight=new UFDouble(0); 
			for (int i = 0; i < mdvos.length; i++) {
				sum_sssl = sum_sssl.add(mdvos[i].getSrkzl());
				sum_factoryWeight=sum_factoryWeight.add(mdvos[i].getDef1()==null?new UFDouble(0):mdvos[i].getDef1());//MeiChao 2010-12-13
			}
			// 是否删除码单
			if (sum_sssl.doubleValue() == 0)
				this.setSfsqmd(new UFBoolean(true));
			// 设置实收数量
			this.setSssl(sum_sssl);
			//设置钢厂重量--MeiChao 2010-12-13
			this.setFactoryweight(sum_factoryWeight);
			setMessage("保存成功...");
			setStatus(MDUtils.INIT_CANEDIT);
			initBodyData();

			// 补充正材重量及金额
			String ispj = (String) getBillCardPanel().getHeadItem("ispj")
					.getValueObject();
			GeneralBillVO nowVObill = getGeneralBillVO();
			String InvID = "";
			if (nowVObill != null && getGenSelectRowID() >= 0) {
				InvID = (String) nowVObill.getItemValue(getGenSelectRowID(),
						"cinventoryid");
			}
			boolean sfmbjs = CheckSfmbjs(InvID);
			if (ispj != null && !ispj.equals("false")) {
				// 正材重量之和*正材单价=正材金额 stuffsumny
				this.setStuffsumny(sum_sssl.multiply(this.getStuffprice(),
						MDConstants.JE_XSW));
				this.setStuffweight(sum_sssl);// 正材重量
				this.setNmny(this.getStuffsumny().add(this.getGrosssumny())); // 实入库金额
			} else {
				if (sfmbjs == false) {
					// 正材重量之和*正材单价=正材金额 stuffsumny
					this.setStuffsumny(sum_sssl.multiply(this.getStuffprice(),
							MDConstants.JE_XSW));
					this.setStuffweight(sum_sssl);// 正材重量
					this
							.setNmny(this.getStuffsumny().add(
									this.getGrosssumny())); // 实入库金额
				}
			}

			// 是否非计算
			if (fjs != null && !fjs.equals("false")) {
				// 正材重量之和*正材单价=正材金额 stuffsumny
				this.setStuffsumny(sum_sssl.multiply(this.getStuffprice(),
						MDConstants.JE_XSW));
				this.setStuffweight(sum_sssl);// 正材重量
				this.setNmny(this.getStuffsumny().add(this.getGrosssumny())); // 实入库金额
			}

			closeCancel();
			// ui.getButtonManager().onButtonClicked(ui.getButtonManager().getButton(ICButtonConst.BTN_BROWSE_REFRESH));
		} else
			MessageDialog.showWarningDlg(this, "提示", "请先计算！");
	}

	private void onAddline() {
		setMessage("增加一行数据...");
		// 选择的行
		int srow = getBillCardPanel().getBillTable().getSelectedRow();
		getBillCardPanel().addLine();
		int i = getBillCardPanel().getRowCount();
		// 单据类型  如果是退货，时全部作为其它入库单据
		if (sfth == false)
			getBillCardPanel().getBillModel().setValueAt(ui.getBillType(),
					i - 1, "cbodybilltypecode");
		else
			getBillCardPanel().getBillModel().setValueAt("4A", i - 1,
					"cbodybilltypecode");
		// 单据方向
		UFBoolean frep = (UFBoolean) getGeneralBillVO().getHeaderValue(
				"freplenishflag");// 是否退货标识;
		UFBoolean rk = UFBoolean.TRUE;

		// 设置单据方向, 例如: 采购入库业务:单据方向为正向, 采购入库 退货:单据方向为反向
		if (frep.booleanValue()) {
			getBillCardPanel().getBillModel().setValueAt(1, i - 1, "djfx");
			if (ui.getInOutFlag() == InOutFlag.IN) {
				rk = UFBoolean.FALSE;
			}
		} else {
			if (ui.getInOutFlag() == InOutFlag.OUT) {
				rk = UFBoolean.FALSE;
			}
			getBillCardPanel().getBillModel().setValueAt(0, i - 1, "djfx");
		}

		//wanglei 2014-03-28 单独处理一下期初单据
			if(ui.getBillType().equalsIgnoreCase("40")){
				rk = UFBoolean.TRUE;
			}
		//end
		// 设置 码单出入库方向
		if (rk.booleanValue()) {
			getBillCardPanel().getBillModel().setValueAt(0, i - 1, "crkfx");
		} else {
			getBillCardPanel().getBillModel().setValueAt(1, i - 1, "crkfx");
		}

		// cwarehouseidb 仓库pk cwarehouseid
		getBillCardPanel().getBillModel().setValueAt(
				getGeneralBillVO().getHeaderValue("cwarehouseid"), i - 1,
				"cwarehouseidb");

		// ccalbodyidb 库存组织PK pk_calbody
		getBillCardPanel().getBillModel().setValueAt(
				getGeneralBillVO().getHeaderValue("pk_calbody"), i - 1,
				"ccalbodyidb");

		getBillCardPanel().getBillModel().setValueAt(
				getGeneralBillVO().getItemValue(getGenSelectRowID(),
						"cgeneralbid"), i - 1, "cgeneralbid");
		getBillCardPanel().getBillModel().setValueAt(
				getEnvironment().getCorpID(), i - 1, "pk_corp");
		getBillCardPanel().getBillModel().setValueAt(
				getEnvironment().getLogDate(), i - 1, "dmakedate");
		getBillCardPanel().getBillModel().setValueAt(
				getEnvironment().getUserID(), i - 1, "voperatorid");

		getBillCardPanel().getBillModel().setValueAt(this.getMd_note(), i - 1,
				"md_note"); // 厚宽长
		getBillCardPanel().getBillModel().setValueAt(this.getMd_zyh(), i - 1,
				"md_zyh"); // 资源号

		getBillCardPanel().getBillModel().setValueAt(
				getBillCardPanel().getHeadItem("thickness").getValueObject(),
				i - 1, "def6");
		getBillCardPanel().getBillModel().setValueAt(
				getBillCardPanel().getHeadItem("ispj").getValueObject(), i - 1,
				"sfbj");// 是否磅计

		getBillCardPanel().getBillModel().setValueAt(this.getVfree1(), i - 1,
				"remark");// 备注
		getBillCardPanel().getBillModel().setValueAt(
				getBillCardPanel().getHeadItem("fjs").getValueObject(), i - 1,
				"def4");// 非计算

		// 如果选择的行大于0
		if (srow >= 0) {
			MdcrkVO[] vos = (MdcrkVO[]) getBillCardPanel().getBillData()
					.getBodyValueVOs(nc.vo.ic.md.MdcrkVO.class.getName());
			MdcrkVO vo = vos[srow];
			getBillCardPanel().getBillModel().setValueAt(vo.getCspaceid(),
					i - 1, "cspaceid"); // 货位
			getBillCardPanel().getBillModel().setValueAt(vo.getJbh(), i - 1,
					"jbh"); // 件编号
			getBillCardPanel().getBillModel().setValueAt(vo.getMd_length(),
					i - 1, "md_length"); // 长度
			getBillCardPanel().getBillModel().setValueAt(vo.getMd_width(),
					i - 1, "md_width"); // 宽度
			getBillCardPanel().getBillModel().setValueAt(vo.getMd_meter(),
					i - 1, "md_meter"); // 米数
			getBillCardPanel().getBillModel().setValueAt(vo.getSrkzs(), i - 1,
					"srkzs"); // 支数
			getBillCardPanel().getBillModel().setValueAt(vo.getMd_lph(), i - 1,
					"md_lph"); // 炉批号
			getBillCardPanel().getBillModel().setValueAt(vo.getMd_zlzsh(),
					i - 1, "md_zlzsh"); // 质量保证书号
			getBillCardPanel().getBillModel().setValueAt(vo.getMd_note(),
					i - 1, "md_note"); // 厚宽长
			getBillCardPanel().getBillModel().setValueAt(vo.getMd_zyh(), i - 1,
					"md_zyh");// 资源号
			getBillCardPanel().getBillModel().setValueAt(vo.getDef7(), i - 1,
					"def7");// def7
			getBillCardPanel().getBillModel().setValueAt(vo.getDef8(), i - 1,
					"def8");// def8
			getBillCardPanel().getBillModel().setValueAt(vo.getDef9(), i - 1,
					"def9");// def9
			getBillCardPanel().getBillModel().setValueAt(vo.getRemark(), i - 1,
					"remark");// 备注
			getBillCardPanel().getBillModel().setValueAt(vo.getDef4(), i - 1,
					"def4");// def4
			getBillCardPanel().getBillModel().setValueAt(vo.getSfbj(), i - 1,
					"sfbj");// 是否磅计
			getBillCardPanel().getBillModel().execLoadFormula();// 显示公式
		}

		
//		getBillCardPanel().getBillModel().getItemByKey("invdetailref").setDataType(5);  //UFREF 
//		getBillCardPanel().getBillModel().getItemByKey("invdetailref").setRefType("<nc.ui.ic.jjpanel.InvDetailRef>");
		
	}

	/**
	 * 编辑后事件
	 */
	public void afterEdit(BillEditEvent editEvent) {
		String key = editEvent.getKey();
		if (key.equals("ispj")) {
			if (editEvent.getValue().equals("true")) {
				getBillCardPanel().getHeadItem("realWeight").setEdit(true);
				getBillCardPanel().getHeadItem("grossprice").setEdit(false);
				setIspj(UFBoolean.TRUE);
				MdcrkVO[] vos = (MdcrkVO[]) getBillCardPanel().getBillData()
						.getBodyValueVOs(nc.vo.ic.md.MdcrkVO.class.getName());
				if (vos != null && vos.length > 0) {
					for (int i = 0; i < vos.length; i++) {
//						getBillCardPanel().getBillModel().setValueAt(null, i,
//								"def1");
						getBillCardPanel().getBillModel().setValueAt(null, i,
								"def2");
						getBillCardPanel().getBillModel().setValueAt(
								new UFBoolean(false), i, "def4");
					}
				}
			} else {
				getBillCardPanel().getHeadItem("realWeight").setEdit(false);

				// 判断当前存货是否需要进行毛边计算
				GeneralBillVO nowVObill = getGeneralBillVO();
				String InvID = "";
				if (nowVObill != null && getGenSelectRowID() >= 0) {
					InvID = (String) nowVObill.getItemValue(
							getGenSelectRowID(), "cinventoryid");
				}
				try {
					boolean sfmbjs = CheckSfmbjs(InvID);
					if (sfmbjs == true)
						getBillCardPanel().getHeadItem("grossprice").setEdit(
								true);
				} catch (BusinessException e) {
					e.printStackTrace();
				}
				setIspj(UFBoolean.FALSE);
			}
		}
		if (key.equals("md_meter")) {
			getBillCardPanel().getBillModel().setValueAt(null,
					editEvent.getRow(), "md_width");
			getBillCardPanel().getBillModel().setValueAt(null,
					editEvent.getRow(), "md_length");
			getBillCardPanel().getBillModel().setValueAt(null,
					editEvent.getRow(), "def7");
			getBillCardPanel().getBillModel().setValueAt(null,
					editEvent.getRow(), "def8");
			getBillCardPanel().getBillModel().setValueAt(null,
					editEvent.getRow(), "srkzl");
		} else if (key.equals("md_width") || key.equals("md_length")) {
			getBillCardPanel().getBillModel().setValueAt(null,
					editEvent.getRow(), "md_meter");
			getBillCardPanel().getBillModel().setValueAt(null,
					editEvent.getRow(), "def9");
			getBillCardPanel().getBillModel().setValueAt(null,
					editEvent.getRow(), "srkzl");
		} else if (key.equals("srkzs")) {
			getBillCardPanel().getBillModel().setValueAt(null,
					editEvent.getRow(), "srkzl");
			//2010-12-21 MeiChao add begin 在修改码单入库支数的同时,根据对应的明细PK,并计算出新的钢厂重量;
			IUAPQueryBS queryService=(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
			String queryString="select unstorageweight/unstoragenumber from (select n.arriveNUMBER - nvl(m.storagenumber, 0) as unstoragenumber," +
					" n.contractweight - nvl(m.storageweight, 0) as unstorageweight,n.pk_invdetail from scm_invdetail n left join (select sum(t.def1) as storageweight," +
					" sum(t.srkzs) as storagenumber," +
					" m.pk_invdetail" +
					" from nc_mdcrk t, scm_invdetail_c m" +
					" where t.pk_mdcrk = m.pk_mdcrk" +
					" and t.dr = 0" +
					" and m.dr = 0" +
					" group by m.pk_invdetail) m on n.pk_invdetail=m.pk_invdetail ) where  pk_invdetail='"+this.getBillCardPanel().getBodyValueAt(editEvent.getRow(), "pk_invdetail")+"'";
			UFDouble weightpernumber=null;
			try {
				//add by ouyangzhb 2012-06-02 如果钢厂入库时没有提供明细，需要计算钢厂重量的，这里会出现空指针错误，需要做控制
				Object temdata = queryService.executeQuery(queryString, new ColumnProcessor());
				weightpernumber= temdata==null? UFDouble.ZERO_DBL :new UFDouble(temdata.toString());
//				weightpernumber=new UFDouble(queryService.executeQuery(queryString, new ColumnProcessor()).toString());
				//add by ouyangzhb 2012-06-02 end
				
			} catch (BusinessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			this.getBillCardPanel().setBodyValueAt(weightpernumber.multiply(new UFDouble(this.getBillCardPanel().getBodyValueAt(editEvent.getRow(), "srkzs").toString())), editEvent.getRow(), "def1");
//			contractnumber->getColValue(scm_invdetail,contractnumber ,pk_invdetail ,pk_invdetail );
//			contractweight->getColValue(scm_invdetail,contractweight ,pk_invdetail ,pk_invdetail );
	
			//2010-12-21 MeiChao add end
		} else if (key.equals("grossprice")) {
			MdcrkVO[] vos = (MdcrkVO[]) getBillCardPanel().getBillData()
					.getBodyValueVOs(nc.vo.ic.md.MdcrkVO.class.getName());
			if (vos != null && vos.length > 0) {
				MdcrkVO[] rsvo = (MdcrkVO[]) getBillCardPanel().getBillData()
						.getBodyValueVOs(MdcrkVO.class.getName());
				if (rsvo != null && rsvo.length > 0) {
					int row = rsvo.length;
					for (int i = 0; i < row; i++)
						getBillCardPanel().getBillModel().setValueAt(null, i,
								"srkzl");
				}
			}

		}
		// 如果点了非计算选择项
		else if (key.equals("fjs")) {
			try {
				boolean boolean_fjs = false;
				if (editEvent.getValue().equals("true")) {
					init();// 初始化
					onEdit();// 编辑
					getBillCardPanel().getHeadItem("realWeight").setEdit(false); // 实收重量
					getBillCardPanel().getHeadItem("grossprice").setEdit(false); // 毛边单价
					getBillCardPanel().getHeadItem("ispj").setValue(
							new UFBoolean(false));
					getBillCardPanel().getHeadItem("isgczl").setValue(
							new UFBoolean(false));
					BillEditEvent e = new BillEditEvent(getBillCardPanel()
							.getHeadItem("ispj").getComponent(),
							getBillCardPanel().getHeadItem("ispj"), "ispj");
					// afterEdit(e);
					getBillCardPanel().getHeadItem("ispj").setEdit(false); // 是否磅计
					getBillCardPanel().getHeadItem("isgczl").setEdit(false); // 是否计算钢厂重量
					getUIButton(MDUtils.CALC_BUTTON).setEnabled(false);
					getBillCardPanel().getBodyItem("srkzl").setEnabled(true);
					boolean_fjs = true;
				} else {
					getBillCardPanel().getHeadItem("ispj").setEdit(true); // 是否磅计
					getBillCardPanel().getHeadItem("isgczl").setEdit(true); // 是否计算钢厂重量
					init();// 初始化
					onEdit();// 编辑
					getUIButton(MDUtils.CALC_BUTTON).setEnabled(true);
					getBillCardPanel().getBodyItem("srkzl").setEnabled(false);
					boolean_fjs = false;
				}
				// 重新设置值
				MdcrkVO[] rsvo = (MdcrkVO[]) getBillCardPanel().getBillData()
						.getBodyValueVOs(MdcrkVO.class.getName());
				if (rsvo != null && rsvo.length > 0) {
					for (int i = 0; i < rsvo.length; i++) {
						//add by ouyangzhb 2011-03-05
						//当点击非计算时，不需要把验收重量和钢厂重量清空
//						getBillCardPanel().getBillModel().setValueAt(null, i,
//								"srkzl");
						getBillCardPanel().getBillModel().setValueAt(
								new UFBoolean(false), i, "sfbj");
//						getBillCardPanel().getBillModel().setValueAt(null, i,
//								"def1");
						getBillCardPanel().getBillModel().setValueAt(null, i,
								"def2");
						getBillCardPanel().getBillModel().setValueAt(
								new UFBoolean(boolean_fjs), i, "def4");
					}
				}
			} catch (BusinessException e) {
				e.printStackTrace();
				MessageDialog.showWarningDlg(this, "错误", e.getMessage());
			}
		}
		// 编辑了自定义项目7
		else if (key.equals("def7")) {
			String defStr = (String) editEvent.getValue();
			// 非计算的值
			boolean fsjboolean = getBillCardPanel().getHeadItem("fjs")
					.getValue().equals("true");
			// 如果是非计算
//			if (fsjboolean) {
//				if (isNumber(defStr))
//					getBillCardPanel().getBillModel().setValueAt(
//							new UFDouble(defStr), editEvent.getRow(),
//							"md_length");
//				else
//					getBillCardPanel().getBillModel().setValueAt(
//							new UFDouble(0), editEvent.getRow(), "md_length");
//			} else {
//				if (isNumber(defStr))
//					getBillCardPanel().getBillModel().setValueAt(
//							new UFDouble(defStr), editEvent.getRow(),
//							"md_length");
//				else {
//					getBillCardPanel().getBillModel().setValueAt(null,
//							editEvent.getRow(), "def7");
//					getBillCardPanel().getBillModel().setValueAt(null,
//							editEvent.getRow(), "md_length");
//					MessageDialog.showWarningDlg(this, "提示", "长度必须是数字");
//				}
//			}
			getBillCardPanel().getBillModel().setValueAt(null,
					editEvent.getRow(), "def9");
			getBillCardPanel().getBillModel().setValueAt(null,
					editEvent.getRow(), "md_meter");
			getBillCardPanel().getBillModel().setValueAt(null,
					editEvent.getRow(), "srkzl");
		}
		// 编辑了自定义项目8
		else if (key.equals("def8")) {
			String defStr = (String) editEvent.getValue();
			// 非计算的值
			boolean fsjboolean = getBillCardPanel().getHeadItem("fjs")
					.getValue().equals("true");
			// 如果是非计算
//			if (fsjboolean) {
//				if (isNumber(defStr))
//					getBillCardPanel().getBillModel().setValueAt(
//							new UFDouble(defStr), editEvent.getRow(),
//							"md_width");
//				else
//					getBillCardPanel().getBillModel().setValueAt(
//							new UFDouble(0), editEvent.getRow(), "md_width");
//			} else {
//				if (isNumber(defStr))
//					getBillCardPanel().getBillModel().setValueAt(
//							new UFDouble(defStr), editEvent.getRow(),
//							"md_width");
//				else {
//					getBillCardPanel().getBillModel().setValueAt(null,
//							editEvent.getRow(), "def8");
//					getBillCardPanel().getBillModel().setValueAt(null,
//							editEvent.getRow(), "md_width");
//					MessageDialog.showWarningDlg(this, "提示", "宽度必须是数字");
//				}
//			}
			getBillCardPanel().getBillModel().setValueAt(null,
					editEvent.getRow(), "def9");
			getBillCardPanel().getBillModel().setValueAt(null,
					editEvent.getRow(), "md_meter");
			getBillCardPanel().getBillModel().setValueAt(null,
					editEvent.getRow(), "srkzl");
		}
		// 编辑了自定义项目9
		else if (key.equals("def9")) {
			String defStr = (String) editEvent.getValue();
			// 非计算的值
			boolean fsjboolean = getBillCardPanel().getHeadItem("fjs")
					.getValue().equals("true");
			// 如果是非计算
//			if (fsjboolean) {
//				if (isNumber(defStr))
//					getBillCardPanel().getBillModel().setValueAt(
//							new UFDouble(defStr), editEvent.getRow(),
//							"md_meter");
//				else
//					getBillCardPanel().getBillModel().setValueAt(
//							new UFDouble(0), editEvent.getRow(), "md_meter");
//			} else {
//				if (isNumber(defStr))
//					getBillCardPanel().getBillModel().setValueAt(
//							new UFDouble(defStr), editEvent.getRow(),
//							"md_meter");
//				else {
//					getBillCardPanel().getBillModel().setValueAt(null,
//							editEvent.getRow(), "def9");
//					getBillCardPanel().getBillModel().setValueAt(null,
//							editEvent.getRow(), "md_meter");
//					MessageDialog.showWarningDlg(this, "提示", "米数必须是数字");
//				}
//			}
			getBillCardPanel().getBillModel().setValueAt(null,
					editEvent.getRow(), "def7");
			getBillCardPanel().getBillModel().setValueAt(null,
					editEvent.getRow(), "def8");
			getBillCardPanel().getBillModel().setValueAt(null,
					editEvent.getRow(), "md_width");
			getBillCardPanel().getBillModel().setValueAt(null,
					editEvent.getRow(), "md_length");
			getBillCardPanel().getBillModel().setValueAt(null,
					editEvent.getRow(), "srkzl");
		}
		//2010-11-25 MeiChao add begin
		else if(key.equals("box")){//如果修改了货位编码
			getBillCardPanel().getBillModel().execLoadFormula();
		}
		//2010-11-25 MeiChao add end
		//2010-12-21 MeiChao add begin 
		else if(key.equals("invdetailref")){//如果修改了存货明细参照 那么根据参照所选PK,获取存货明细数据
			UIRefPane invdetailref=(UIRefPane)this.getBillCardPanel().getBodyItem("invdetailref").getComponent();
			String[] invdetailpk=invdetailref.getRefPKs();
			//String[] invdetailname=invdetailref.getRefNames();
			//Vector datas=invdetailref.getSelectedData();
			if (invdetailpk!=null) {
				for(int i=0;i<invdetailpk.length;i++){
				if(i>0){//i>0的时候增行操作
					ActionEvent e =new ActionEvent(getUIButton(MDUtils.ADDLINE_BUTTON),1001,MDUtils.ADDLINE_BUTTON);
					this.actionPerformed(e);
				}
				String[] formulas={"md_width->getColValue(scm_invdetail,contractwidth,pk_invdetail,\""+invdetailpk[i]+"\")",
								   "md_length->getColValue(scm_invdetail,contractlength,pk_invdetail,\""+invdetailpk[i]+"\")",
								   "md_meter->getColValue(scm_invdetail,contractmeter,pk_invdetail,\""+invdetailpk[i]+"\")",
								   //"def1->getColValue(scm_invdetail,contractweight,pk_invdetail,\""+invdetailpk[i]+"\")",
								   "def8->getColValue(scm_invdetail,arrivewidth,pk_invdetail,\""+invdetailpk[i]+"\")",
								   "def7->getColValue(scm_invdetail,arrivelength,pk_invdetail,\""+invdetailpk[i]+"\")",
								   "def9->getColValue(scm_invdetail,arrivemeter,pk_invdetail,\""+invdetailpk[i]+"\")",
								   //"srkzl->getColValue(scm_invdetail,arriveweight,pk_invdetail,\""+invdetailpk[i]+"\")",
								   //"srkzs->getColValue(scm_invdetail,arrivenumber,pk_invdetail,\""+invdetailpk[i]+"\")",
								   "pk_invdetail->\""+invdetailpk[i]+"\"",};
				this.getBillCardPanel().execBodyFormulas(i==0?editEvent.getRow():this.getBillCardPanel().getRowCount()-1, formulas);
				this.getBillCardPanel().setBodyValueAt(((Object[])invdetailref.getRefValues("unstoragenumber"))[i],i==0?editEvent.getRow():this.getBillCardPanel().getRowCount()-1, "srkzs");//支数
				this.getBillCardPanel().setBodyValueAt(((Object[])invdetailref.getRefValues("unstorageweight"))[i],i==0?editEvent.getRow():this.getBillCardPanel().getRowCount()-1, "def1");//钢厂重量
				this.getBillCardPanel().setBodyValueAt(((Object[])invdetailref.getRefValues("vdef1"))[i],i==0?editEvent.getRow():this.getBillCardPanel().getRowCount()-1, "md_lph");//炉批号
				//this.getBillCardPanel().setBodyValueAt(invdetailref.getRefValue("unstorageweight"), editEvent.getRow(), "srkzl");//验收重量
				//wanglei 2011-07-26 add
				hm_invdetailpk.put(invdetailpk[i], invdetailpk[i]);
				//end
				}
			}
			else{
				String[] formulas={"md_width->\"\"",
						   "md_length->\"\"",
						   "md_meter->\"\"",
						   //"def1->getColValue(scm_invdetail,contractweight,pk_invdetail,\""+invdetailpk[i]+"\")",
						   "def8->\"\"",
						   "def7->\"\"",
						   "def9->\"\"",
						   //"srkzl->getColValue(scm_invdetail,arriveweight,pk_invdetail,\""+invdetailpk[i]+"\")",
						   //"srkzs->getColValue(scm_invdetail,arrivenumber,pk_invdetail,\""+invdetailpk[i]+"\")",
						   "pk_invdetail->\"\"",};
				this.getBillCardPanel().execBodyFormulas(editEvent.getRow(), formulas);
//				this.getBillCardPanel().setBodyValueAt(((Object[])invdetailref.getRefValues("unstoragenumber"))[editEvent.getRow()],editEvent.getRow(), "srkzs");//支数
//				this.getBillCardPanel().setBodyValueAt(((Object[])invdetailref.getRefValues("unstorageweight"))[editEvent.getRow()],editEvent.getRow(), "def1");//钢厂重量
//				this.getBillCardPanel().setBodyValueAt(((Object[])invdetailref.getRefValues("vdef1"))[editEvent.getRow()],editEvent.getRow(), "md_lph");//炉批号

			}
				
		}
		//2010-12-21 MeiCha add end
		edited = true;
		
		// md_width
		// md_length
		// md_meter
	}

	void setIspj(UFBoolean issel) {
		MdcrkVO[] vos = (MdcrkVO[]) getBillCardPanel().getBillData()
				.getBodyValueVOs(nc.vo.ic.md.MdcrkVO.class.getName());
		if (vos != null && vos.length > 0) {
			int i = vos.length;
			for (int j = 0; j < i; j++) {
				getBillCardPanel().getBillModel().setValueAt(issel, j, "sfbj");
				getBillCardPanel().getBillModel().setValueAt(null, j, "srkzl");
			}
		}
	}

	public void bodyRowChange(BillEditEvent editEvent) {
	}

	public boolean beforeEdit(BillEditEvent billeditevent) {
		//2010-12-20 MeiChao 在采购入库单存在来源单据类型并且为到货单事,增设"钢厂数据"参照的查询条件
		// 2011-03-03 Ouyangzhb 所有的采购入库单都需要增设"钢厂数据"参照的过滤条件
		if(billeditevent.getKey().equals("invdetailref")){
			String orderBid=this.getGeneralBillVO().getItemVOs()[this.getGenSelectRowID()].getCfirstbillbid();
//			getBillCardPanel().getBillModel().getItemByKey("invdetailref").setDataType(5);  //UFREF 
//			getBillCardPanel().getBillModel().getItemByKey("invdetailref").setRefType("<nc.ui.ic.jjpanel.InvDetailRef>");
			UIRefPane invdetailref=(UIRefPane)this.getBillCardPanel().getBillModel().getItemByKey("invdetailref").getComponent();
			invdetailref.setMultiSelectedEnabled(true);
			invdetailref.setWhereString("corder_bid='"+orderBid+"' and unstoragenumber>0 and unstorageweight>0");
			
			//wanglei 2011-07-26 add
			String pk_invdetail = (String) this.getBillCardPanel().getBodyValueAt(billeditevent.getRow(), "pk_invdetail") ;
			//String pk_invdetail = billeditevent.getItem().getValue();
			if (pk_invdetail!=null && hm_invdetailpk.containsKey(pk_invdetail))
				hm_invdetailpk.remove(pk_invdetail);
			
			if (!hm_invdetailpk.isEmpty()){
				Iterator iter = hm_invdetailpk.entrySet().iterator();
				String sw_invdetailpk = " and  pk_invdetail not in (";
				while (iter.hasNext()) {
				    Map.Entry entry = (Map.Entry) iter.next();
				    sw_invdetailpk += "'" + (String) entry.getKey() + "',";
				} 
				sw_invdetailpk +=  "'##')";
				
				invdetailref.setWhereString("corder_bid='"+orderBid+"' and unstoragenumber>0 and unstorageweight>0" + sw_invdetailpk);
			}

		}
		
		if(billeditevent.getKey()=="srkzs"){
			
		}
		return true;
	}

	public UFDouble getSsfsl() {
		return ssfsl;
	}

	public void setSsfsl(UFDouble ssfsl) {
		this.ssfsl = ssfsl;
	}

	public UFDouble getSssl() {
		return sssl;
	}

	public void setSssl(UFDouble sssl) {
		this.sssl = sssl;
	}

	public UFDouble getGrossprice() {
		return grossprice;
	}

	public void setGrossprice(UFDouble grossprice) {
		this.grossprice = grossprice;
	}

	public UFDouble getGrosssumny() {
		return grosssumny;
	}

	public void setGrosssumny(UFDouble grosssumny) {
		this.grosssumny = grosssumny;
	}

	public UFDouble getGrossweight() {
		return grossweight;
	}

	public void setGrossweight(UFDouble grossweight) {
		this.grossweight = grossweight;
	}

	public UFDouble getStuffprice() {
		return stuffprice;
	}

	public void setStuffprice(UFDouble stuffprice) {
		this.stuffprice = stuffprice;
	}

	public UFDouble getStuffsumny() {
		return stuffsumny;
	}

	public void setStuffsumny(UFDouble stuffsumny) {
		this.stuffsumny = stuffsumny;
	}

	public UFDouble getStuffweight() {
		return stuffweight;
	}

	public void setStuffweight(UFDouble stuffweight) {
		this.stuffweight = stuffweight;
	}

	public UFDouble getNprice() {
		return nprice;
	}

	public void setNprice(UFDouble nprice) {
		this.nprice = nprice;
	}

	public UFDouble getNmny() {
		return nmny;
	}

	public void setNmny(UFDouble nmny) {
		this.nmny = nmny;
	}

	public UFBoolean getSfsqmd() {
		return sfsqmd;
	}

	public void setSfsqmd(UFBoolean sfsqmd) {
		this.sfsqmd = sfsqmd;
	}

	// 毛边计算
	public MdcrkVO[] mdMbjs(MdcrkVO[] mdcrkVos) throws BusinessException {
		// 毛边单价
		String grossprice_str = getBillCardPanel().getHeadItem("grossprice")
				.getValue();
		if (grossprice_str == null || grossprice_str.equals(""))
			throw new BusinessException("当前存货维护了毛边计算，理计时毛边单价不能为空！");
		UFDouble grossprice = new UFDouble(grossprice_str, MDConstants.DJ_XSW);
		if (grossprice.doubleValue() == 0)
			throw new BusinessException("理计时毛边单价不能为0");
		// 毛边系数
		UFDouble mbxs = new UFDouble(0);
		mbxs = MDUtils.getMBXS(mdxclvo.getCinvbasid(), mdxclvo
				.getCinventoryidb(), null);
		if (mbxs == null || mbxs.doubleValue() <= 0)
			throw new BusinessException("毛边系数不能小于等于0");
		UFDouble sumZczl = new UFDouble(0);// 正材重量之和
		UFDouble sumMbzl = new UFDouble(0);// 毛边重量之和
		for (int i = 0; i < mdcrkVos.length; i++) {
			//add by ouyangzhb 2011-02-25 begin 
			// 注释代码，正材重量是自动带出的，不需要设置
//			mdcrkVos[i].setDef1(mdcrkVos[i].getSrkzl());// 正材重量
			//毛边重量应该为 通过理计出的 钢材重量*毛边系数
			mdcrkVos[i].setDef2(mdcrkVos[i].getSrkzl().multiply(mbxs,
					MDConstants.ZL_XSW));// 毛边重量
			//add by ouyangzhb 2011-02-25 end
			mdcrkVos[i].setSrkzl(mdcrkVos[i].getSrkzl().add(
					mdcrkVos[i].getDef2())); // 正材+毛边重量,即实入库重量
			sumZczl = sumZczl.add(mdcrkVos[i].getDef1());// 正材重量相加
			sumMbzl = sumMbzl.add(mdcrkVos[i].getDef2());// 毛边重量相加
		}
		// 毛边单价
		this.setGrossprice(grossprice);

		// 正材重量之和*正材单价=正材金额 stuffsumny
		this.setStuffsumny(sumZczl.multiply(this.getStuffprice(),
				MDConstants.JE_XSW));
		this.setStuffweight(sumZczl);// 正材重量

		// 毛边重量之和*毛边单价=毛边金额 grosssumny
		this.setGrosssumny(sumMbzl.multiply(this.getGrossprice(),
				MDConstants.JE_XSW));
		this.setGrossweight(sumMbzl);// 毛边重量

		// 真实的单价 nprice =(正材金额+毛边金额)/(正材重量+毛边重量)
		this.setNprice((this.getStuffsumny().add(this.getGrosssumny())).div(
				this.getStuffweight().add(this.getGrossweight()),
				MDConstants.DJ_XSW));

		this.setNmny(this.getStuffsumny().add(this.getGrosssumny())); // 实入库金额

		return mdcrkVos;
	}

	// 是否需要进行毛边计算
	public  boolean CheckSfmbjs(String pk_invbas) throws BusinessException {
		IUAPQueryBS iUAPQueryBS = (IUAPQueryBS) NCLocator.getInstance().lookup(
				IUAPQueryBS.class.getName());
		//add by ouyangzhb 2011-02-25  改为查询存货管理档案中的自定义项19
		Object[] objs = (Object[]) iUAPQueryBS
				.executeQuery(
						"select t2.def19 from bd_invmandoc t2  where t2.pk_invmandoc='"
								+ pk_invbas + "'", new ArrayProcessor());
		if (objs[0] != null && objs[0].toString() != null
				&& objs[0].toString().toUpperCase().equals("Y"))
			return true;
		return false;
	}

	public String getMd_note() {
		return md_note;
	}

	public void setMd_note(String md_note) {
		this.md_note = md_note;
	}

	public String getMd_zyh() {
		return md_zyh;
	}

	public void setMd_zyh(String md_zyh) {
		this.md_zyh = md_zyh;
	}

	// 判断一个字符串是否是数字
	public static boolean isNumber(String strChar) {
		if (strChar == null || strChar.equals(""))
			return false;
		for (int i = strChar.length(); --i >= 0;) {
			if (!Character.isDigit(strChar.charAt(i)))
				return false;
		}
		return true;
	}

	public String getVfree1() {
		return vfree1;
	}

	public void setVfree1(String vfree1) {
		this.vfree1 = vfree1;
	}
	/**
	 * 组织码单页面上的存货明细子表信息 
	 * @author MeiChao
	 * @return InvDetailCVO[]<来源为到货单>  null<来源单据不为到货单>
	 */
	public InvDetailCVO[] getInvDetailVOs(){
		int rownumber=this.getBillCardPanel().getRowCount();
		InvDetailCVO[] invDetailCVOs=new InvDetailCVO[rownumber];
		for(int i=0;i<rownumber;i++){
			InvDetailCVO invDetailCVO=new InvDetailCVO();
			invDetailCVO.setCgeneralbid(this.getGeneralBillVO().getItemVOs()[this.getGenSelectRowID()].getCgeneralbid());
			invDetailCVO.setPk_invdetail(this.getBillCardPanel().getBillModel().getValueAt(i,"pk_invdetail")==null?null:this.getBillCardPanel().getBillModel().getValueAt(i,"pk_invdetail").toString());
			invDetailCVO.setTs(new UFDateTime(new Date()));
			invDetailCVO.setDr(0);
			invDetailCVOs[i]=invDetailCVO;
		}
		return invDetailCVOs;
	}

//	public boolean beforeEdit(BillItemEvent e) {
//		// TODO Auto-generated method stub
//		return false;
//	}

}
