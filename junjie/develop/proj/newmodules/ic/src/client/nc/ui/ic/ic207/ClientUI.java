package nc.ui.ic.ic207;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Vector;

import nc.ui.ic.jjpanel.InfoCostPanel;
import nc.ui.ic.pub.bill.GeneralBillHelper;
import nc.ui.ic.pub.bill.GeneralButtonManager;
import nc.ui.ic.pub.bill.ICButtonConst;
import nc.ui.ic.pub.bill.initref.RefFilter;
import nc.ui.ic.pub.report.ICReportHelper;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.FramePanel;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.beans.UITextField;
import nc.ui.pub.bill.BillItem;
import nc.ui.scm.ic.exp.GeneralMethod;
import nc.vo.ic.jjvo.InformationCostVO;
import nc.vo.ic.pub.bill.GeneralBillHeaderVO;
import nc.vo.ic.pub.bill.GeneralBillItemVO;
import nc.vo.ic.pub.bill.GeneralBillVO;
import nc.vo.ic.pub.bill.IItemKey;
import nc.vo.ic.pub.bill.QryInfoConst;


import nc.vo.pub.ValidationException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.constant.ic.BillMode;
import nc.vo.scm.constant.ic.InOutFlag;
import nc.vo.scm.ic.bill.WhVO;
import nc.vo.scm.ic.exp.ICPriceException;

/**
 * 此处插入类型说明。
 * 创建日期：(2001-11-23 15:39:43)
 * @author：王乃军
 */
public class ClientUI extends nc.ui.ic.pub.bill.GeneralBillClientUI {
	//取结存单价
//	private ButtonObject m_boQueryPrice ;
	private boolean m_bIsOutBill= false;
	private UITextField m_uitfHinttext= new UITextField();
	//2010-10-29 MeiChao 添加--其他入库单的费用录入按钮.Begin
	
	private UFDouble number = null;//费用页签下的数量

	
	//拉式生产的最初金额 add by 付世超 2010-10-15
	private UFDouble pmny = null;
	
	private ButtonObject expenseInput;
	private ButtonObject getButtonExpenseInput(){
		if(this.expenseInput==null){
			this.expenseInput=new ButtonObject("费用录入","费用录入","费用录入");
		}
		return this.expenseInput;
	}
	//2010-10-29 MeiChao 添加 End
	
	

/**
 * ClientUI2 构造子注解。
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

public nc.ui.pub.bill.BillCardPanel getBillCardPanel() {
	return super.getBillCardPanel();
}

public nc.ui.pub.bill.BillListPanel getBillListPanel() {
	return super.getBillListPanel();
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
protected void afterBillEdit(nc.ui.pub.bill.BillEditEvent e) {
	//nc.vo.scm.pub.SCMEnv.out("haha,bill edit/.");
	String sItemKey = e.getKey();
	int row = e.getRow();
	if("ninnum".equalsIgnoreCase(sItemKey)||"ninassistnum".equalsIgnoreCase(sItemKey)){//2010-10-14 MeiChao 增加
	    //add by QuSida 2010-9-5 (佛山骏杰)  --- begin
	    //function 当入库数量修改后及时更新费用信息中的数量
	    int temp = getBillCardPanel().getBillModel("table").getRowCount();
	   number = new UFDouble(0.0);
//	    UFDouble plannumber = new UFDouble(0.0);
//	    UFDouble taxmny = null;
		UFDouble mny = null;//单价
	    //add by 付世超 2010-10-15 应入数量
	    UFDouble plannum = new UFDouble(0.0);
	    
	    for (int i = 0; i < temp; i++) {
	    	number = number.add(new UFDouble((getBillCardPanel().getBodyValueAt(i,"ninnum")==null?0.0:getBillCardPanel().getBodyValueAt(i,"ninnum")).toString()));    
	    	//add by 付世超 2010-10-15 
	    	plannum = plannum.add(new UFDouble((getBillCardPanel().getBodyValueAt(i,"nshouldinnum")==null?0:getBillCardPanel().getBodyValueAt(i,"nshouldinnum")).toString()));//应到数量 
		}
	    temp = getBillCardPanel().getBillModel("jj_scm_informationcost").getRowCount();
	    UFDouble innum = ((GeneralButtonManager)getButtonManager()).getArrnum(); 
	    int length = 0;
//	    if(((GeneralButtonManager)getButtonManager()).getInfovos()!=null){
//	    	length = ((GeneralButtonManager)getButtonManager()).getInfovos().length;
//	    }		    
	    for (int i = length; i < temp; i++) {
	    	Boolean ismny = (Boolean)getBillCardPanel().getBillModel("jj_scm_informationcost").getValueAt(i, "ismny");		    	
	    	getBillCardPanel().getBillModel("jj_scm_informationcost").setValueAt(number, i, "nnumber");
			// add by 付世超 2010-10-14 begin 
	    	// add by 付世超 2010-10-17 添加是否为新增加入库费用的判断 使用自定义项 vdef10  0：到货单录入的费用  1：入库单录入的费用
			if("0".equals(getBillCardPanel().getBillModel("jj_scm_informationcost").getValueAt(i,"vdef10"))||getBillCardPanel().getBillModel("jj_scm_informationcost").getValueAt(i,"vdef10")==null){//到货单录入的费用
		    	if(ismny == null || !ismny){
		    		
			    	mny = new UFDouble(getBillCardPanel().getBillModel("jj_scm_informationcost").getValueAt(i,"noriginalcurprice").toString()).multiply(number);
			    	getBillCardPanel().getBillModel("jj_scm_informationcost").setValueAt(mny, i, "noriginalcurmny");
//			    	inmny = new UFDouble(getBillCardPanel().getBillModel("jj_scm_informationcost").getValueAt(i,"noriginalcurprice").toString()).multiply(number.add(innum==null?new UFDouble(0.0):innum));
//			    	taxmny = new UFDouble(getBillCardPanel().getBodyValueAt(i,"noriginalcurtaxprice").toString()).multiply(arrnumber);
		    	}else{
//		    		UFDouble price =  new UFDouble(getBillCardPanel().getBillModel("jj_scm_informationcost").getValueAt(i, "noriginalcurmny").toString()).div(number);
		    		mny = pmny.multiply(number).div(plannum);
		    		getBillCardPanel().getBillModel("jj_scm_informationcost").setValueAt(mny, i, "noriginalcurmny");
		        	
		    	}	
	    	}else if("1".equals(getBillCardPanel().getBillModel("jj_scm_informationcost").getValueAt(i,"vdef10"))){//入库单新加费用
				
				if(ismny == null || !ismny.booleanValue()){
					mny = new UFDouble(getBillCardPanel().getBillModel("jj_scm_informationcost").getValueAt(i,"noriginalcurprice").toString()).multiply(number);
		    		getBillCardPanel().getBillModel("jj_scm_informationcost").setValueAt(mny, i, "noriginalcurmny");
				}
				else{
					mny = pmny.multiply(number).div(plannum);
		    		getBillCardPanel().getBillModel("jj_scm_informationcost").setValueAt(mny, i, "noriginalcurmny");
				}
			}
			//add by 付世超 2010-10-14 end 
	    }
	    //add by QuSida 2010-9-5 (佛山骏杰)  --- end
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
protected void afterBillItemSelChg(int iRow, int iCol) {
	//nc.vo.scm.pub.SCMEnv.out("haha,sel chged!");

}
/**
 * 创建者：张欣
 * 功能：专门为其它出，入库单方法setAllData中调用仓库改变事件处理，带出仓库相关Info和库存组织Info，而不清空原有的库存组织。
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
public void afterWhEditNoClearCalbody(nc.ui.pub.bill.BillEditEvent e) {
	//仓库
	try {
//		String sNewWhName =
//			((nc.ui.pub.beans.UIRefPane) getBillCardPanel()
//				.getHeadItem(IItemKey.WAREHOUSE)
//				.getComponent())
//				.getRefName();
//		String sNewWhID =
//			((nc.ui.pub.beans.UIRefPane) getBillCardPanel()
//				.getHeadItem(IItemKey.WAREHOUSE)
//				.getComponent())
//				.getRefPK();
	    //zhy2005-05-26解决效率问题，不从界面取数，直接从vo中取
	    String sNewWhID =((GeneralBillHeaderVO)getM_voBill().getParentVO()).getCwarehouseid();
		//清空了仓库
		if (sNewWhID == null) {
			//清空批次号参照的仓库
			getLotNumbRefPane().setWHParams(null);
			//if (m_voBill != null)
				//m_voBill.setWh(null);
		} else {

			//保存名称以在列表形式下显示。
			//查询仓库信息
			//查询方式：如果已经录入了存货，需要同时查计划价。
			int iQryMode = QryInfoConst.WH;
			//参数
			Object oParam = sNewWhID;
			//当前已录入的存货数据
			ArrayList alAllInvID = new ArrayList();
			boolean bHaveInv = getCurInvID(alAllInvID);

			//仓库
			WhVO voWh = null;
			//清空批次号参照的仓库
			getLotNumbRefPane().setWHParams(null);
			if (getM_voBill() != null)
				getM_voBill().setWh(null);

			if (bHaveInv) {
				//参数：仓库ID,原库存组织ID,单位ID,存货ID
				ArrayList alParam = new ArrayList();
				alParam.add(sNewWhID);
				iQryMode = QryInfoConst.WH_PLANPRICE;
				//当前的库存组织,考虑没有仓库的情况。
				if (getM_voBill() != null && getM_voBill().getWh() != null)
					alParam.add(getM_voBill().getWh().getPk_calbody());
				else
					alParam.add(null);
				//公司
				alParam.add(getEnvironment().getCorpID());
				//当前的存货
				alParam.add(alAllInvID);
				oParam = alParam;
			}

			Object oRet =GeneralBillHelper.queryInfo(new Integer(iQryMode), oParam);
				

			//当前已录入的存货数据,并且修改了库存组织才返回一个ArrayList
			if (oRet instanceof ArrayList) {
				ArrayList alRetValue = (ArrayList) oRet;
				if (alRetValue != null && alRetValue.size() >= 2) {
					voWh = (WhVO) alRetValue.get(0);
					//刷新计划价
					freshPlanprice((ArrayList) alRetValue.get(1));
				}
			} else //否则返回 WhVO
				voWh = (WhVO) oRet;
			//库存组织处理
			nc.ui.pub.bill.BillItem biCalBody =
				getBillCardPanel().getHeadItem("pk_calbody");
			if (biCalBody != null) {
				if (voWh != null)
					biCalBody.setValue(voWh.getPk_calbody());
				else
					biCalBody.setValue(null);
			}
			nc.ui.pub.bill.BillItem biCalBodyname =
				getBillCardPanel().getHeadItem("vcalbodyname");
			if (biCalBodyname != null) {
				if (voWh != null)
					biCalBodyname.setValue(voWh.getVcalbodyname());
				else
					biCalBodyname.setValue(null);
			}

			if (getM_voBill() != null) {
				getM_voBill().setWh(voWh);
				//清表尾现存量
				getM_voBill().clearInvQtyInfo();
				getLotNumbRefPane().setWHParams(voWh);
			}

			//清货位、序列号数据放在afterEdit()的clearLocSn中
			//int iRowCount = getBillCardPanel().getRowCount();
			//for (int row = 0; row < iRowCount; row++)
			//clearRowData(0, row, IItemKey.WAREHOUSE);
			//刷新现存量显示
			//setTailValue(0);
			//设置货位分配按钮是否可用。
			setBtnStatusSpace(true);
		}

	} catch (Exception e2) {
		nc.vo.scm.pub.SCMEnv.out(e2);
	}

}
/**
 * 创建者：王乃军
 * 功能：单据编辑事件处理
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
public boolean beforeBillItemEdit(nc.ui.pub.bill.BillEditEvent e) {
	return true;
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
protected void beforeBillItemSelChg(int iRow, int iCol) {
	//nc.vo.scm.pub.SCMEnv.out("haha,before sel");

}
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
	String sSourceBillType = getSourBillTypeCode();
	try {
		boolean bCheck = true;
		bCheck = checkVO();
		//检查库存的特殊单据数量不能为负
		if (sSourceBillType != null
			&& (sSourceBillType.equals(nc.vo.ic.pub.BillTypeConst.m_assembly)
				|| sSourceBillType.equals(nc.vo.ic.pub.BillTypeConst.m_disassembly)
				|| sSourceBillType.equals(nc.vo.ic.pub.BillTypeConst.m_transform)
				|| sSourceBillType.equals(nc.vo.ic.pub.BillTypeConst.m_check)

				//2002-10-19.wnj 转库单可输入负数|| sSourceBillType.equals(nc.vo.ic.pub.BillTypeConst.m_transfer)
				|| sSourceBillType.equals(nc.vo.ic.pub.BillTypeConst.m_AllocationOrder))) {
			//来源单据类型是transfer,...check
			//数量>=0检查
			nc.vo.ic.pub.check.VOCheck.checkGreaterThanZeroInput(
				voBill.getChildrenVO(),
				getEnvironment().getNumItemKey(),
				nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UC000-0002282")/*@res "数量"*/);
		}
		return bCheck;

	} catch (ICPriceException e) {
		//显示提示
		String sErrorMessage =
			GeneralMethod.getBodyErrorMessage(
				getBillCardPanel(),
				e.getErrorRowNums(),
				e.getHint());
		showErrorMessage(sErrorMessage);
		return false;
	} catch (ValidationException e) {
		nc.vo.scm.pub.SCMEnv.out("校验异常！其他未知故障...");
		handleException(e);
		return false;
	}
}
/**
 * 此处插入方法说明。
 * 功能：
 * 参数：
 * 返回：
 * 例外：
 * 日期：(2002-5-28 16:56:37)
 * 修改日期，修改人，修改原因，注释标志：
 * @param sItemKey java.lang.String
 * @param sCond java.lang.String
 */
public void filterWhRef(String sPk_calbody) {
	//清空了库存组织
	//如果当前的仓库不属于
	//过滤仓库参照
	String sConstraint[] = null;
	if (sPk_calbody != null) {
		sConstraint = new String[1];
		sConstraint[0] = " AND pk_calbody='" + sPk_calbody + "'";
	}
	nc.ui.pub.bill.BillItem bi = getBillCardPanel().getHeadItem(IItemKey.WAREHOUSE);
	nc.ui.ic.pub.bill.initref.RefFilter.filtWh(bi, getEnvironment().getCorpID(), sConstraint);

}

/**
 * zrq 需要的得到表单的表体行数
 * 功能：
 * 参数：
 * 返回：
 * 例外：
 * 日期：(2001-11-15 14:02:17)
 * 修改日期，修改人，修改原因，注释标志：
 */
public int getCardTableRowNum() {
	return getBillCardPanel().getRowCount();
}
public GeneralBillVO getMVOBill(){
	return getM_voBill();
}
/**
 * 此处插入方法说明。
 * 创建日期：(2001-8-28 19:07:32)
 */
public int getLastSelListHeadRow() {
	return getM_iLastSelListHeadRow();
}
/**
 * 创建者：仲瑞庆
 * 功能：外部置入单据号
 * 参数：
 * 返回：
 * 例外：
 * 日期：(2001-11-26 14:50:38)
 * 修改日期，修改人，修改原因，注释标志：
 * @return boolean
 */
public ArrayList getSerialData() {
	return getM_alSerialData();
}
public ArrayList getLocatorData() {
	return getM_alLocatorData();
}

/**
 * 创建者：仲瑞庆
 * 功能：
 * 参数：
 * 返回：
 * 例外：
 * 日期：(2001-11-29 19:46:17)
 * 修改日期，修改人，修改原因，注释标志：
 * @param hinttext nc.ui.pub.beans.UITextField
 */
public UITextField getUITxtFldStatus() {
	return m_uitfHinttext;
}

/**
 * 类型说明：
 * 创建日期：(2002-11-28 14:12:13)
 * 作者：张欣IC
 * 修改日期：
 * 修改人：
 * 修改原因：
 * 算法说明：
 */
public void initialize() {
	
	//add by ouyangzhb 2011-03-31 为费用页签添加合计行
    getBillCardPanel().getBodyPanel("jj_scm_informationcost").setTatolRowShow(true);
	//在父类初始化方法执行前,将费用录入按钮添加到按钮树中.
	this.getButtonManager().getButtonTree().addMenu(this.getButtonExpenseInput());
	//必须重载基类的initialize方法后,
	super.initialize();
	//	参照过滤
	BillItem bi = getBillCardPanel().getHeadItem("cotherwhid");
    RefFilter.filtWh(bi,getEnvironment().getCorpID(),null);
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
}

public String getBillType() {
	return nc.vo.ic.pub.BillTypeConst.m_otherIn;
}

public String getFunctionNode() {
	return "40080608";
}

public int getInOutFlag() {
	return InOutFlag.IN;
}

/**
 * 功能：取出当前界面上的存货的当前最新结存单价
 * 创建日期：(2002-12-25 16:34:26)
 * 作者：王乃军
 * 修改日期：
 * 修改人：
 * 修改原因：
 * 算法说明：
 */
private void onQueryPrice() {
	try {
		//取当前界面显示的数据
		GeneralBillVO voCurBill = getBillVO();
		if (voCurBill != null
			&& voCurBill.getHeaderVO() != null
			&& voCurBill.getItemVOs() != null
			&& voCurBill.getItemVOs().length > 0) {
			GeneralBillHeaderVO voHead = voCurBill.getHeaderVO();
			GeneralBillItemVO[] voaItem = voCurBill.getItemVOs();
			//准备参数数据。
			//String pk_corp = getEnvironment().getCorpID(); //公司
			String pk_calbody = voHead.getPk_calbody(); //库存组织
			String cwarehouseid = voHead.getCwarehouseid(); //仓库
			Hashtable htInvBatch = new Hashtable();
			if (cwarehouseid != null && cwarehouseid.trim().length() > 0) {
				String[] cinventoryids = null; //存货们
				Vector vInv = new Vector();
				String[] sLots = null; //批次们
				Vector vLot = new Vector();
				String sKey=null;//invid+batchcode
				for (int i = 0; i < voaItem.length; i++) {
					//如果vector中没有此存货管理id，或有此存货但没有此存货的批次，的话，就加进去。
					if (voaItem[i] != null && voaItem[i].getCinventoryid() != null) {
						sKey = voaItem[i].getCinventoryid() + "&" + voaItem[i].getVbatchcode();
						if (!htInvBatch.containsKey(sKey)) {
							vInv.addElement(voaItem[i].getCinventoryid());
							//批次
							vLot.addElement(voaItem[i].getVbatchcode());
							htInvBatch.put(sKey, "");
						}
					}
				}
				//有存货！
				if (vInv.size() > 0) {
					cinventoryids = new String[vInv.size()];
					vInv.copyInto(cinventoryids);
					sLots = new String[vLot.size()]; //长度和存货相同。
					vLot.copyInto(sLots);

					ArrayList alParam = new ArrayList();
					alParam.add(getEnvironment().getCorpID());
					alParam.add(pk_calbody);
					alParam.add(cwarehouseid);
					alParam.add(cinventoryids);
					alParam.add(sLots);
					//执行查询
					ArrayList alRet =
						(ArrayList) ICReportHelper.queryInfo(new Integer(QryInfoConst.SETTLEMENT_PRICE), alParam);
					if (alRet != null && alRet.size() > 0) {
						//设置到界面上
						setPrice(alRet);
						showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008busi","UPP4008busi-000285")/*@res "读取结存单价完毕。"*/);
					}
					else {
						showWarningMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008busi","UPP4008busi-000286")/*@res "没有查询到当前存货的结存单价！"*/);
					}
				}
				else {
					showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008busi","UPP4008busi-000287")/*@res "请先录入存货，然后再试。"*/);
				}
			}
			else {
				showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008busi","UPP4008busi-000288")/*@res "请先录入仓库，然后再试。"*/);
			}
		}
		else
			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008busi","UPP4008busi-000289")/*@res "当前没有可用单据，请查询或新增单据。"*/);
	}
	catch (Exception e) {
		showWarningMessage(e.getMessage());
	}
}
/**
 * 创建者：王乃军
 * 功能：查询符合指定条件的单据
 * 参数：
 * 返回：
 * 例外：
 * 日期：(2001-5-9 9:23:32)
 * 修改日期，修改人，修改原因，注释标志：
 *
 *
 *
 */

public void queryBills(nc.vo.ic.pub.bill.QryConditionVO voQryCond) {
	try {

		setM_alListData(GeneralBillHelper.queryBills(getBillType(),voQryCond));
		if (getM_alListData() != null && getM_alListData().size() > 0) {

			GeneralBillHeaderVO voh[] = new GeneralBillHeaderVO[getM_alListData().size()];
			for (int i = 0; i < getM_alListData().size(); i++) {
				if (getM_alListData().get(i) != null)
					voh[i] =
						(GeneralBillHeaderVO) ((GeneralBillVO) getM_alListData().get(i)).getParentVO();
				else
					nc.vo.scm.pub.SCMEnv.out("list data error!-->" + i);

			}
			setListHeadData(voh);
			//当前是表单形式时，首先切换到列表形式
			if (BillMode.Card == getM_iCurPanel())
				getButtonManager().onButtonClicked(
						getButtonManager().getButton(
								ICButtonConst.BTN_SWITCH));

			//缺省表头指向第一张单据
			selectListBill(0);
			//设置当前的单据数量/序号，用于按钮控制
			setM_iLastSelListHeadRow(0);
			//初始化当前单据序号，切换时用到！！！不宜主动设置表单的数据。
			m_iCurDispBillNum = -1;
			//当前单据数
			m_iBillQty = getM_alListData().size();

			setButtonStatus(true);
			if (m_iBillQty > 0)
				showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008busi","UPP4008busi-000290",null,new String[]{String.valueOf(m_iBillQty)})/*@res "共查到{0}张单据！"*/);
			else
				showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008busi","UPP4008busi-000291")/*@res "未查到单据。"*/);

		} else {
			//?????
		}

	} catch (Exception e) {
		handleException(e);
		showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008busi","UPP4008busi-000292")/*@res "查询出错！"*/);
	}
}
/**
 * 此处插入方法说明。
 * 功能：
 * 参数：
 * 返回：
 * 例外：
 * 日期：(2002-07-19 14:05:46)
 * 修改日期，修改人，修改原因，注释标志：
 */
public void removeListHeadMouseListener() {
	//if (getBillListPanel().getHeadTable() != null)
		//getBillListPanel().getHeadTable().removeMouseListener(this);
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
protected void selectBillOnListPanel(int iBillIndex) {}
/**
 * 创建者：王乃军
 * 功能：设置界面的单据
 * 参数：
 * 返回：
 * 例外：
 * 日期：(2001-5-9 9:23:32)
 * 修改日期，修改人，修改原因，注释标志：
 *
 *
 *
 */

public void setAllData(ArrayList<GeneralBillVO> alListData) {
	try {
		m_bIsOutBill = true;
		setM_alListData(alListData);
		if (getM_alListData() != null && getM_alListData().size() > 0) {

			GeneralBillHeaderVO voh[] = new GeneralBillHeaderVO[getM_alListData().size()];
			for (int i = 0; i < getM_alListData().size(); i++) {
				if (getM_alListData().get(i) != null)
					voh[i] =
						(GeneralBillHeaderVO) ((GeneralBillVO) getM_alListData().get(i)).getParentVO();
				else
					nc.vo.scm.pub.SCMEnv.out("list data error!-->" + i);

			}
			setListHeadData(voh);
			//当前是表单形式时，首先切换到列表形式
			/*if (BillMode.Card == m_iCurPanel)
				onSwitch();
				*/

			//缺省表头指向第一张单据
			selectListBill(0);
			//设置当前的单据数量/序号，用于按钮控制
			setM_iLastSelListHeadRow(0);
			//初始化当前单据序号，切换时用到！！！不宜主动设置表单的数据。
			m_iCurDispBillNum = -1;
			//当前单据数
			m_iBillQty = getM_alListData().size();

			if (getM_iLastSelListHeadRow() >= 0
				&& m_iCurDispBillNum != getM_iLastSelListHeadRow()
				&& getM_alListData() != null
				&& getM_alListData().size() > getM_iLastSelListHeadRow()
				&& getM_alListData().get(getM_iLastSelListHeadRow()) != null) {
				for (int i = 0; i < getM_alListData().size(); i++) {
					setM_voBill((GeneralBillVO) ((GeneralBillVO) getM_alListData().get(i)));
					//zhy2005-05-26解决效率问题，因为在下面的代码中会执行首行的setBillVO(m_voBill)，所以在此不需要重复执行
					if(i!=0)
					    setBillVO(getM_voBill());
					//this.afterWhEditNoClearCalbody(null);
					getM_alListData().set(i, getM_voBill());
				}
				//zhy2005-05-27解决效率，在执行setBillVO(m_voBill);时会对vo进行clone,所以此处不需要clone
				setM_voBill((GeneralBillVO) getM_alListData().get(getM_iLastSelListHeadRow()));

				setBillVO(getM_voBill(),true,false);

			}

			setButtonStatus(true);

		} else {
			//?????
		}

	} catch (Exception e) {
		handleException(e);
		showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008busi","UPP4008busi-000292")/*@res "查询出错！"*/);
	}
}
/**
 * 创建者：仲瑞庆
 * 功能：外部置入单据号
 * 参数：
 * 返回：
 * 例外：
 * 日期：(2001-11-26 14:50:38)
 * 修改日期，修改人，修改原因，注释标志：
 * @return boolean
 */
public boolean setBillCodeAuto() {
	nc.ui.pub.bill.BillItem bi=getBillCardPanel().getHeadItem("vbillcode");
	if(bi!=null&&(bi.getValue()==null||bi.getValue().trim().length()==0)){
		if(!m_bIsEditBillCode){
			getM_voBill().setVBillCode(getEnvironment().getCorpID());
			bi.setValue(getEnvironment().getCorpID());
		}

	}

	return true;
}
/**
 * 创建者：王乃军
 * 功能：在表单设置显示VO, 不更新状态(updateValue())
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
protected void setBillValueVO(GeneralBillVO bvo) {
	getBillCardPanel().getBillModel().removeTableModelListener(this);
	try {
		getBillCardPanel().setBillValueVO(bvo);
		//执行公式
		getBillCardPanel().getBillModel().execLoadFormula();
		execHeadTailFormulas();
		//
		bvo.clearInvQtyInfo();
		//选中第一行
		//getBillCardPanel().getBillTable().setRowSelectionAllowed();
		getBillCardPanel().getBillTable().setRowSelectionInterval(0, 0);
		//刷新现存量显示
		//setTailValue(0);

	} catch (Exception e) {
		//try的目的是保证addListener被执行。
		nc.vo.scm.pub.SCMEnv.out(e.getMessage());
	}

	getBillCardPanel().getBillModel().addTableModelListener(this);
	/** 当单据的来源单据为转库单时, 进行界面控制 */
	ctrlSourceBillUI(true);
}
/**
 * 创建者：王乃军
 * 功能：在表单设置显示VO,
 		当传入为True时，只置m_voBill,否则调无参数的setBillVO()
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
public void setBillVO(GeneralBillVO bvo, boolean bIsOnlySet) {
	try {
		if (bIsOnlySet) {
			getBillCardPanel().addNew();
			setBillValueVO(bvo);
			setM_voBill(bvo);
		} else {
			setBillVO(bvo,true,false);
		}
		//执行公式
		//getBillCardPanel().getBillModel().execLoadFormula();
		//execHeadTailFormulas();

		//
	} catch (Exception e) {
		nc.vo.scm.pub.SCMEnv.error(e);
	}
}
/**
 * 创建者：仲瑞庆
 * 功能：
 * 参数：
 * 返回：
 * 例外：
 * 日期：(2001-7-19 下午 10:51)
 * 修改日期，修改人，修改原因，注释标志：
 * @param bShowFlag boolean
 */
public void setBodyMenuShow(boolean bShowFlag) {
	getBillCardPanel().setBodyMenuShow(bShowFlag);
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
protected void setButtonsStatus(int iBillMode) {
	
	//2010-10-29 MeiChao 设置费用录入按钮的状态
	this.setExpenseInputButtonStat(iBillMode);
	
	//设置读结存单价是否可用
	switch (iBillMode) {
		case BillMode.New :
		case BillMode.Update :
			getBoQueryPrice().setEnabled(true);
			break;
		case BillMode.Browse :
			getBoQueryPrice().setEnabled(false);
			break;
	}
	updateButton(getBoQueryPrice());
}
/**
 * 此处插入方法说明。
 * 功能：
 * 参数：
 * 返回：
 * 例外：
 * 日期：(2001-10-25 13:48:13)
 * 修改日期，修改人，修改原因，注释标志：
 */
public void setCardMode(int NewCardMode) {
	setM_iMode(NewCardMode);
}
/**
 * 此处插入方法说明。
 * 创建者：张欣
 * 功能：设置表单形式是否可编辑。

 	用于外部调用，需要调用者自己维护当前的New,Update,Browse等状态。

 * 参数：
 * 返回：
 * 例外：
 * 日期：(2001-6-26 15:04:38)
 * 修改日期，修改人，修改原因，注释标志：
 *
 * @param newM_iCurPanel int
 */
public void setCardPanelEnable(boolean bEnable) {
	//不可编辑
	getBillCardPanel().setEnabled(bEnable);
}
/**
 * 此处插入方法说明。
 * 功能：
 * 参数：
 * 返回：
 * 例外：
 * 日期：(2002-5-28 16:56:01)
 * 修改日期，修改人，修改原因，注释标志：
 * @param sItemKey java.lang.String
 */
public void setHeadItemEnable(String sItemKey, boolean bCan) {
	//处理:clear warehouse
	nc.ui.pub.bill.BillItem bi = getBillCardPanel().getHeadItem(sItemKey);
	if (bi != null)
		bi.setEnabled(bCan);

}
/**
 * 此处插入方法说明。
 * 创建日期：(2001-8-28 19:07:32)
 */
public void setLastSelListHeadRow(int lastrow) {
	setM_iLastSelListHeadRow(lastrow);
	selectListBill(lastrow);
}
/**
 * 功能：置当前界面上的存货的当前最新结存单价
 * 创建日期：(2002-12-25 16:34:26)
 * 作者：王乃军
 * 修改日期：
 * 修改人：
 * 修改原因：
 * 算法说明：
 */
private void setPrice(ArrayList alData) {

	final String IK_INV = "cinventoryid"; //表体存货的itemkey
	final String IK_BATCH_CODE = "vbatchcode"; //表体存货的批次号
	final String IK_PRICE = "nprice"; //表体存货的itemkey

	if (alData != null && alData.size() > 0) {
		//建立HASHTABLE:KEY=INVID+VBATCHCODE,VALUE=PRICE，提高查询效率

		getBillCardPanel().getBillModel().setNeedCalculate(false);

		Hashtable htData = new Hashtable();
		ArrayList alTempData = null;
		String sKey = null; //==invid+vlot
		for (int i = 0; i < alData.size(); i++) {
			if (alData.get(i) != null) {
				alTempData = (ArrayList) alData.get(i);
				if (alTempData.size() >= 3
					&& alTempData.get(0) != null
					&& alTempData.get(2) != null) {
					//invid+&+vlot,price
					sKey = alTempData.get(0).toString() + "&" + alTempData.get(1);
					if (!htData.containsKey(sKey))
						htData.put(sKey, alTempData.get(2));
				}
			}
		}
		int rowcount = getBillCardPanel().getRowCount();
		String cinventoryid = null, vlot = null;
		for (int i = 0; i < rowcount; i++) {
			cinventoryid = (String) getBillCardPanel().getBodyValueAt(i, IK_INV);
			vlot = (String) getBillCardPanel().getBodyValueAt(i, IK_BATCH_CODE);
			//invid+&+vlot,price
			sKey = cinventoryid + "&" + vlot;
			if (sKey != null && htData.containsKey(sKey))
				getBillCardPanel().setBodyValueAt(htData.get(sKey), i, IK_PRICE);
		}
		//同步vo
		//不需要
		getBillCardPanel().getBillModel().setNeedCalculate(true);
		getBillCardPanel().getBillModel().reCalcurateAll();

	} else {
		nc.vo.scm.pub.SCMEnv.out("no price to be set");
	}
}
/**
 * 创建者：仲瑞庆
 * 功能：外部置入单据号
 * 参数：
 * 返回：
 * 例外：
 * 日期：(2001-11-26 14:50:38)
 * 修改日期，修改人，修改原因，注释标志：
 * @return boolean
 */
public void setSerialData(ArrayList alSN) {
	setM_alSerialData(alSN);
}
/**
 * 创建者：仲瑞庆
 * 功能：
 * 参数：
 * 返回：
 * 例外：
 * 日期：(2001-11-29 19:46:17)
 * 修改日期，修改人，修改原因，注释标志：
 * @param hinttext nc.ui.pub.beans.UITextField
 */
public void setUITxtFldStatus(nc.ui.pub.beans.UITextField hinttext) {
	m_uitfHinttext= hinttext;
}
/**
 * 创建者：仲瑞庆
 * 功能：
 * 参数：
 * 返回：
 * 例外：
 * 日期：(2001-11-29 18:42:59)
 * 修改日期，修改人，修改原因，注释标志：
 * @param sMessage java.lang.String
 */
public void showErrorMessage(String sMessage) {
	nc.ui.pub.beans.MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("4008busi","UPPSCMCommon-000059")/*@res "错误"*/, sMessage);
	//super.showErrorMessage( sMessage );
}
/**
 * 创建者：仲瑞庆
 * 功能：
 * 参数：
 * 返回：
 * 例外：
 * 日期：(2001-11-29 18:42:59)
 * 修改日期，修改人，修改原因，注释标志：
 * @param sMessage java.lang.String
 */
public void showHintMessage(String sMessage) {
	if (m_bIsOutBill) {
		//nc.ui.pub.beans.MessageDialog.showHintDlg(this, "注意", sMessage);
		getUITxtFldStatus().setText(sMessage);
	} else
		super.showHintMessage(sMessage);
}
/**
 * @return the m_boQueryPrice
 */
private ButtonObject getBoQueryPrice() {
  return getButtonManager().getButton("取结存单价");
}

/**
 * 费用录入按钮的监听处理方法.
 * 
 */
public void onExtendBtnsClick(ButtonObject bo) {
	 if(bo == this.getButtonExpenseInput()){
	    	this.onBoExpenseInput();
	    }
}

/**
 *费用录入按钮的事件响应方法
 * Meichao
 *2010-10-29 14:58
 */
private void onBoExpenseInput() {
	InformationCostVO[] vos = (InformationCostVO[] )getBillCardPanel().getBillModel("jj_scm_informationcost").getBodyValueVOs(InformationCostVO.class.getName());
	ArrayList voList = new ArrayList();
	for (int i = 0; i < vos.length; i++) {
		if(vos[i] != null){
			voList.add(vos[i]);
		}
	}
	InfoCostPanel c = null;
	if(voList.size()!=0&&voList!=null){
		InformationCostVO[] vos1 = new InformationCostVO[voList.size()];
		voList.toArray(vos1);
		c = new InfoCostPanel(getBillCardPanel(),vos1);
	}
	else 			
	c = new InfoCostPanel(getBillCardPanel());
	// 打开费用录入界面
	c.showModal();
	// 当费用录入界面关闭时,将录入的数据存放到费用信息页签上
	if (c.isCloseOK()) {
		InformationCostVO[] infoCostVOs = c.getInfoCostVOs();
		if (infoCostVOs != null && infoCostVOs.length != 0){
			// 当费用录入界面的vo数组不为空时,将vo存到费用录入页签上
					getBillCardPanel().getBillData().setBodyValueVO(
						"jj_scm_informationcost", infoCostVOs);
				getBillCardPanel().getBillModel("jj_scm_informationcost").execLoadFormula();
			}	
		
	}
}
/**
 * 
 * (设置费用录入按钮的可见/可用状态)
 *  MeiChao  补充注释   
 * @see nc.ui.ic.pub.bill.GeneralBillClientUI#setExtendBtnsStat(int)
 * 
 */
	public void setExpenseInputButtonStat(int iState) {
		switch (iState) {
		case BillMode.New:
			getButtonExpenseInput().setEnabled(true);
			break;
		case BillMode.Update:
			getButtonExpenseInput().setEnabled(true);
			break;
		case BillMode.Browse:
			getButtonExpenseInput().setEnabled(false);
			break;
		default:
			getButtonExpenseInput().setEnabled(false);
			break;
		}
	}



}