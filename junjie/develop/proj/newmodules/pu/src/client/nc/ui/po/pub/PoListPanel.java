package nc.ui.po.pub;

////////JAVA
import java.awt.Container;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;

import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import nc.bs.bd.b21.BusinessCurrencyRateUtil;
import nc.ui.po.oper.PoToftPanel;
import nc.ui.pu.jj.JJPuScmPubHelper;
import nc.ui.pu.pub.POPubSetUI;
import nc.ui.pu.pub.POPubSetUI2;
import nc.ui.pu.pub.PuTool;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIComboBox;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillListData;
import nc.ui.pub.bill.BillListPanel;
import nc.ui.pub.bill.BillModel;
import nc.ui.pub.bill.BillTotalListener;
import nc.ui.pub.bill.IBillModelSortPrepareListener;
import nc.vo.po.OrderHeaderVO;
import nc.vo.po.OrderItemVO;
import nc.vo.po.OrderVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.constant.ScmConst;
import nc.vo.pu.jjvo.InformationCostVO;
import nc.vo.scm.pu.BillTypeConst;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.scm.pu.Timer;
import nc.vo.scm.pu.VariableConst;
import nc.vo.scm.pub.SCMEnv;

public class PoListPanel extends BillListPanel
implements	ListSelectionListener,
		IBillModelSortPrepareListener 
		//since v55, 赠品不计合计(与销售订单做成一致)
		,BillTotalListener
		{	
	//调用者，该调用者需实现接口nc.ui.po.pub.PoListPanelInterface
	private	Container	m_conInvoker = null ;
	//模板ID
	private	String		m_sTemplateId = null ;
	
	//由界面类传递过来的汇率精度设置工具实例
    private POPubSetUI2 m_listPoPubSetUI2 = null;
    
    //V31SP1项目补丁,德美存储对应存货编码、名称，标识是否需要读供应商存货关系表
    private boolean m_bLoadVendCodeName = false;

    private BillModel m_billmodel = null;

/**
 * 作者：王印芬
 * 功能：构造子注解
 * 参数：Container	sInvoker		调用者，需实现接口nc.ui.po.pub.PoListPanelInterface
 *		String	sCorp				公司
 * 返回：无
 * 例外：无
 * 日期：(2002-3-13 11:39:21)
 * 修改日期，修改人，修改原因，注释标志：
 */
public PoListPanel(Container	sInvoker,String	sTemplateId){
    
    m_listPoPubSetUI2 = new POPubSetUI2();
	
    setContainer(sInvoker) ;
	setCorp(PoPublicUIClass.getLoginPk_corp()) ;
	setTemplateId(sTemplateId) ;
	setBillType(null) ;

	initi() ;
}

/**
 * 作者：王印芬
 * 功能：构造子注解
 * 参数：Container	sInvoker		调用者，需实现接口nc.ui.po.pub.PoListPanelInterface
 *		String	sCorp				公司
 * 返回：无
 * 例外：无
 * 日期：(2002-3-13 11:39:21)
 * 修改日期，修改人，修改原因，注释标志：
 */
public PoListPanel(Container	sInvoker,String	sCorp,String sBillType,POPubSetUI2 setUi){
    if(setUi != null){
        m_listPoPubSetUI2 = setUi;
    }else{
        m_listPoPubSetUI2 = new POPubSetUI2();
    }
	setContainer(sInvoker) ;
	setCorp(sCorp) ;
	setBillType(sBillType) ;
	setTemplateId(null) ;
	initi() ;
}

/**
 * 作者：王印芬
 * 功能：显示列表界面
 * 参数：int iUIPos		界面上的订单位置
 * 返回：无
 * 例外：无
 * 日期：(2002-3-13 11:39:21)
 * 修改日期，修改人，修改原因，注释标志：
 * 
 * 2005-06-17 V31 晁志平 增加是否要打*号标志
 * 
 * 2006-05-18 V31SP1 晁志平 德美要求存储对应存货编码、对应存货名称
 */
public void displayCurVO(int	iUIPos, boolean bMarkFirstRow) {
	
	displayCurVO(iUIPos, bMarkFirstRow ,false);
}
/**
 * 德美要求存储对应存货编码、对应存货名称
 * @param iUIPos
 * @param bMarkFirstRow
 * @author czp
 */
public void displayCurVO(int	iUIPos, boolean bMarkFirstRow, boolean bLoadVendCodeName) {
	Timer time = new Timer();
	time.start();
	time.addExecutePhase("*******************卡片切换列表加载表头＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊８８");
	//是否加载对应供应商存货编码、名称标志
	m_bLoadVendCodeName = bLoadVendCodeName;


	nc.vo.scm.pu.Timer timeDebug = new nc.vo.scm.pu.Timer();
	timeDebug.start();

	//清除界面数据
	getHeadBillModel().clearBodyData();
	getBodyBillModel().clearBodyData();
	timeDebug.addExecutePhase("清除界面数据");/*-=notranslate=-*/
	time.addExecutePhase("*******************从缓存中取数据＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊８８");
	OrderHeaderVO[]		voaHead = getContainer().getOrderViewHVOs() ;
	if ( voaHead==null ) {
		getContainer().setButtonsStateList() ;
		return;
	}
	//得到表头数组
	timeDebug.addExecutePhase("得到表头数组");/*-=notranslate=-*/
	time.addExecutePhase("*******************卡片切换列表加载表头１２１２１１放置表头ｖｏ＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊８８");
	//设置表头VO
//	setHeaderValueVO(voaHead);
	if(m_conInvoker instanceof PoToftPanel){
	 for (int i = 0; i < ((PoToftPanel)m_conInvoker).getBufferVOManager().getLength(); i++) {
	      // 加行
	      getHeadBillModel().addLine();
	      getHeadBillModel().setBodyRowVO(((PoToftPanel)m_conInvoker).getBufferVOManager().getHeadVOAt(i), i);

	    }
	}else{
		setHeaderValueVO(voaHead);
	}
	timeDebug.addExecutePhase("设置表头VO");/*-=notranslate=-*/
	time.addExecutePhase("*******************卡片切换列表加载表头２１２１２１２执行公式＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊８８");
	//表头公	getHeadBillModel().execLoadFormula();
	getHeadBillModel().execLoadFormula();
	
	timeDebug.addExecutePhase("表头公式");/*-=notranslate=-*/

	//装载表体
	if (getHeadTable().getRowCount()!=0) {
	    if(bMarkFirstRow){
	        getHeadBillModel().setRowState(iUIPos, BillModel.SELECTED);
	    }
		getHeadTable().setRowSelectionInterval(iUIPos,iUIPos) ;
	}
//	timeDebug.showAllExecutePhase("采购订单列表加载表头");/*-=notranslate=-*/
	time.showAllExecutePhase("*******************卡片切换列表加载表头＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊８８");
}

/**
 * 作者：王印芬
 * 功能：在列表界面实现显示指定位置的采购订单
 * 参数：int pos		现有位置，为界面位置
 * 返回：无
 * 例外：无
 * 日期：(2002-6-5 11:39:21)
 * 修改日期，修改人，修改原因，注释标志：
 */
public void displayCurVOBody(int		iUIPos) {
	nc.vo.scm.pu.Timer timeDebug = new nc.vo.scm.pu.Timer();
	timeDebug.start();

	try {
		//取得表体
		OrderVO	voViewCur = getContainer().getOrderViewVOAt( getVOPos(iUIPos) ) ;
		if(voViewCur.getHeadVO().getCvendormangid()==null){
			for(OrderItemVO voBody : voViewCur.getBodyVO()){
				voBody.setCcurrencytypeid(null);
			}
		}
		String	sCurPk_corp = voViewCur.getHeadVO().getPk_corp() ;
		timeDebug.addExecutePhase("取得表体");/*-=notranslate=-*/

		OrderItemVO[]	voaItem = voViewCur.getBodyVO() ;
		if (voaItem==null || voaItem.length==0) {
			getBodyBillModel().clearBodyData() ;
			return ;
		}
		if(m_conInvoker instanceof PoToftPanel){
			((PoToftPanel)m_conInvoker).getInvokeEventProxy().beforeSetBillVOsToListBody(voaItem);
		}
		//设置精度
		setBodyDigits_CorpRelated(sCurPk_corp) ;
		timeDebug.addExecutePhase("设置精度");/*-=notranslate=-*/
		//先关闭合计开关
		boolean needTotal=getBillModel().isNeedCalculate();
		getBillModel().setNeedCalculate(false);
		//设置VO
		setBodyValueVO(voaItem);
		timeDebug.addExecutePhase("设置VO");/*-=notranslate=-*/

		//表体币种
		PoCardPanel.resetBodyValueRelated_Curr(sCurPk_corp,voViewCur.getHeadVO().getCcurrencytypeid(),getBodyBillModel(),new BusinessCurrencyRateUtil(getCorp()),getBodyBillModel().getRowCount(),m_listPoPubSetUI2);
		timeDebug.addExecutePhase("重设表体币种及值");/*-=notranslate=-*/

		//表体公式
		getBodyBillModel().execLoadFormula();
		timeDebug.addExecutePhase("表体公式");/*-=notranslate=-*/

		//设置自由项
		PoPublicUIClass.setFreeColValue(getBodyBillModel(),"vfree");
		timeDebug.addExecutePhase("设置自由项");/*-=notranslate=-*/

		//设置换算率
		PuTool.setBillModelConvertRateAndAssNum(getBodyBillModel(),new	String[]{"cbaseid","cassistunit","nordernum","nassistnum","nconvertrate"},4, -1) ;
		timeDebug.addExecutePhase("设置换算率");/*-=notranslate=-*/

		//处理来源单据类型、来源单据号
		PuTool.loadSourceInfoAll(this,BillTypeConst.PO_ORDER);
		timeDebug.addExecutePhase("处理来源单据类型、来源单据号");/*-=notranslate=-*/

		//最高限价
		PoPublicUIClass.loadListMaxPrice(this,iUIPos,m_listPoPubSetUI2) ;
		timeDebug.addExecutePhase("最高限价");/*-=notranslate=-*/

		//审批节点：净单价超出最高限价时，表体行着色
		if (BillTypeConst.PO_AUDIT.equals(getBillType())) {
			setColor() ;
		}
	    //显示供应商的存货编码及名称
		if(m_bLoadVendCodeName){	
	        String strVendorId = voViewCur.getHeadVO().getCvendormangid();  
	        String[] saMangId = new String[voViewCur.getBodyVO().length];
	        for (int i = 0; i < saMangId.length; i++) {
	            saMangId[i] = voViewCur.getBodyVO()[i].getCmangid();
	        }
	        PuTool.loadVendorInvInfos(strVendorId,saMangId,getBodyBillModel(),0,saMangId.length -1);
		}
	  //再打开合计开关
	  getBillModel().setNeedCalculate(needTotal);
	  //add by QuSida 2010-9-11 (佛山骏杰) --- begin
	  //function：查询出对应的费用信息
	  String pk = voViewCur.getHeadVO().getCorderid();
		String sql = "cbillid = '"+pk+"' and dr = 0 ";
		InformationCostVO[] vos = null;	
		 vos = (InformationCostVO[])JJPuScmPubHelper.querySmartVOs(InformationCostVO.class, null, sql);
	if(vos!=null&&vos.length!=0){
		 getBillListData().getBodyBillModel("jj_scm_informationcost").setBodyDataVO(vos);
		 getBillListData().getBodyBillModel("jj_scm_informationcost").execLoadFormula();
	}else{
		//20101010-22-20  MeiChao 费用为空时,清空历史费用信息.
		getBillListData().getBodyBillModel("jj_scm_informationcost").setBodyDataVO(null);
	}
	
	 //add by QuSida 2010-9-11 (佛山骏杰) --- end
	} catch (Exception ex) {
		PuTool.outException(this,ex) ;
	}

	timeDebug.showAllExecutePhase("采购订单列表加载表体");/*-=notranslate=-*/

}

/**
 * 作者：王印芬
 * 功能：列表可用量查询
 * 参数：无
 * 返回：无
 * 例外：无
 * 日期：(2001-05-22 13:24:16)
 * 修改日期，修改人，修改原因，注释标志：
 */
private IPoListPanel getContainer() {
	return	(IPoListPanel)m_conInvoker;
}

/**
 * 作者：王印芬
 * 功能：列表可用量查询
 * 参数：无
 * 返回：无
 * 例外：无
 * 日期：(2001-05-22 13:24:16)
 * 修改日期，修改人，修改原因，注释标志：
 */
public int getHeadRowCount() {

	return getHeadBillModel().getRowCount() ;
}

/**
 * 作者：王印芬
 * 功能：得到列表界面下表头选中的行
 * 参数：无
 * 返回：无
 * 例外：无
 * 日期：(2002-4-22 11:39:21)
 * 修改日期，修改人，修改原因，注释标志：
 * 2003-03-10	wyf		修改取选中的行的方法
 */
public	int	getHeadRowState(int	iRow){
	return	getHeadBillModel().getRowState(iRow) ;
}

/**
 * 作者：王印芬
 * 功能：得到列表界面下表头选中的行
 * 参数：无
 * 返回：无
 * 例外：无
 * 日期：(2002-4-22 11:39:21)
 * 修改日期，修改人，修改原因，注释标志：
 * 2003-03-10	wyf		修改取选中的行的方法
 */
public	int	getHeadSelectedCount(){
	if (getHeadBillModel().getRowCount()==0) {
		return	0 ;
	}

	int[] iaSelectedRow = getHeadTable().getSelectedRows();
	if (iaSelectedRow==null ){
		return	0 ;
	}else{
		return	 iaSelectedRow.length ;
	}
}

/**
 * 作者：王印芬
 * 功能：
 * 参数：无
 * 返回：界面上的选中位置
 * 例外：无
 * 日期：(2001-05-22 13:24:16)
 * 修改日期，修改人，修改原因，注释标志：
 */
public int getHeadSelectedRow() {

	return getHeadTable().getSelectedRow();
}

/**
 * 作者：王印芬
 * 功能：得到列表界面下表头选中的行
 * 参数：无
 * 返回：无
 * 例外：无
 * 日期：(2002-4-22 11:39:21)
 * 修改日期，修改人，修改原因，注释标志：
 * 2003-03-10	wyf		修改取选中的行的方法
 */
public	int[]	getHeadSelectedRows(){
	return	getHeadTable().getSelectedRows() ;
}

/**
 * 作者：王印芬
 * 功能：得到列表界面下表头选中的行所对应的所有VO的位置，并已按VO中的位置大小排序
 * 参数：无
 * 返回：无
 * 例外：无
 * 日期：(2003-11-05 11:39:21)
 * 修改日期，修改人，修改原因，注释标志：
 */
public	Integer[]	getHeadSelectedVOPoses(){

	//需作废的订单
	HashMap	htmapIndex = new HashMap();

	int[]		iaSelectedRowCount = getHeadSelectedRows() ;
	int			iLen = iaSelectedRowCount.length ;
	for (int i = 0; i < iLen; i++) {
		int 		iVOPos = getVOPos(iaSelectedRowCount[i]);
		htmapIndex.put(new	Integer(iVOPos),"") ;
	}

	TreeMap	trmapIndex = new	TreeMap(htmapIndex) ;
	Integer[]	iaIndex = (Integer[])trmapIndex.keySet().toArray(new	Integer[iLen]) ;

	return	iaIndex ;
}

/**
 * 作者：WYF
 * 功能：接口IBillModelSortPrepareListener 的实现方法
 * 参数：String sItemKey		ITEMKEY
 * 返回：无
 * 例外：无
 * 日期：(2004-03-24  11:39:21)
 * 修改日期，修改人，修改原因，注释标志：
 */
public int getSortTypeByBillItemKey(String sItemKey){

	if ("crowno".equals(sItemKey)
		|| "csourcebillrowno".equals(sItemKey)
		|| "cancestorbillrowno".equals(sItemKey)) {
		return BillItem.DECIMAL;
	}
	return	getBodyBillModel().getItemByKey(sItemKey).getDataType() ;
}

/**
 * 作者：王印芬
 * 功能：得到模板ID
 * 参数：无
 * 返回：无
 * 例外：无
 * 日期：(2003-9-24 11:39:21)
 * 修改日期，修改人，修改原因，注释标志：
 */
private	String	getTemplateId(){
	return	m_sTemplateId ;
}

/**
 * 作者：王印芬
 * 功能：根据给定界面行 返回VO的真正下标
 * 参数：int		iUIRow		界面行
 * 返回：int				真正对应的VO下标，亦即排序前的下标
 * 例外：无
 * 日期：(2001-05-22 13:24:16)
 * 修改日期，修改人，修改原因，注释标志：
 */
public int getVOPos(int		iUIRow) {

	return PuTool.getIndexBeforeSort(this,iUIRow) ;
}

/**
 * 作者：王印芬
 * 功能：初始化
 * 参数：无
 * 返回：无
 * 例外：无
 * 日期：(2002-8-26 11:39:21)
 * 修改日期，修改人，修改原因，注释标志：
 * 2002-08-26	wyf	加入千分位的设置
 * 2002-09-20	wyf	修改自定义项的调用
 */
private void initi() { //throws Exception{

	nc.vo.scm.pu.Timer timeDebug = new nc.vo.scm.pu.Timer();
	timeDebug.start();

	// 加载模板
	try {
	  if (m_conInvoker instanceof PoToftPanel) {
	    setListData(new BillListData(((PoToftPanel) m_conInvoker).getBillTempletVo()));
	  }else	if (getTemplateId() == null) {
			loadTemplet(getBillType(), null, ClientEnvironment.getInstance().getUser().getPrimaryKey(), getCorp());
		} else {
			loadTemplet(getTemplateId());
		}
	} catch (Exception e) {
		SCMEnv.out(e);
		MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON","UPPSCMCommon-000270")/*@res "提示"*/, nc.ui.ml.NCLangRes.getInstance().getStrByID("40040200","UPP40040200-000047")/*@res "当前操作人没有可用的模板"*/);
		return;
	}
	timeDebug.addExecutePhase("加载模板");/*-=notranslate=-*/

	//隐藏项
	initiHideItems();
	timeDebug.addExecutePhase("隐藏项");/*-=notranslate=-*/
	//下拉框
	initiComboBox();
	timeDebug.addExecutePhase("下拉框");/*-=notranslate=-*/

	int iDigit = POPubSetUI.getCCurrFinanceDecimal(getCorp());

	//自定义项
	nc.ui.scm.pub.def.DefSetTool.updateBillListPanelUserDef(this,
	//当前模板
	getCorp(), //公司主键
  ScmConst.PO_Order, //单据类型
	"vdef", //单据模板中单据头的自定义项前缀
	"vdef" //单据模板中单据体的自定义项前缀
	);
	setListData(getBillListData());
	//需重置FALSE
	setEnabled(false);
	timeDebug.addExecutePhase("自定义项");/*-=notranslate=-*/
	//设置表头精度
	setHeadDigits(iDigit);
	timeDebug.addExecutePhase("设置表头精度");/*-=notranslate=-*/
	//国际化
	PuTool.setTranslateRender(this);
	//千分位
	getParentListPanel().setShowThMark(true);
	getChildListPanel().setShowThMark(true);
	//国际化
	initiListener(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
	//加入合计行
	//	getChildListPanel().setTatolRowShow(true);
	getChildListPanel().setTotalRowShow(true);
	timeDebug.addExecutePhase("其他");/*-=notranslate=-*/
	
	//V55，支持整行选中
	PuTool.setLineSelectedList(this);
	
	updateUI();
	//
	timeDebug.showAllExecutePhase("列表界面加载");/*-=notranslate=-*/
	return;
}

/**
 * 作者：王印芬
 * 功能：设置表体中的ComboBox
 * 参数：无
 * 返回：无
 * 例外：无
 * 日期：(2002-8-26 11:39:21)
 * 修改日期，修改人，修改原因，注释标志：
 */
private void initiComboBox() {
	////表头中ComboBox以索引显示
	//BillItem[] hitems = getBillListData().getHeadItems();
	//for (int i = 0; i < hitems.length; i++) {
		//if (hitems[i].getDataType() == BillItem.COMBO) {
			//hitems[i].setWithIndex(true);
		//}
	//}
	//表体中ComboBox以索引显示
	//BillItem[] bitems = getBillListData().getBodyItems();
	//for (int i = 0; i < bitems.length; i++) {
		//if (bitems[i].getDataType() == BillItem.COMBO) {
			//bitems[i].setWithIndex(true);
		//}
	//}

	getBodyItem("idiscounttaxtype").setWithIndex(true) ;
	//列表界面
	UIComboBox cbbBType =	(UIComboBox) (getBodyItem("idiscounttaxtype").getComponent());
	cbbBType.setFont(this.getFont());
	cbbBType.setBackground(this.getBackground());

	cbbBType.addItem(VariableConst.IDISCOUNTTAXTYPE_NAME_INNER);
	cbbBType.addItem(VariableConst.IDISCOUNTTAXTYPE_NAME_OUTTER);
	cbbBType.addItem(VariableConst.IDISCOUNTTAXTYPE_NAME_NOCOUNT);
	cbbBType.setSelectedIndex(1);

	cbbBType.setTranslate(true) ;
}

/**
 * 作者：王印芬
 * 功能：初始化隐藏的字段，这些字段请隐藏，因LISTSHOWFLAG不升级
 * 参数：无
 * 返回：无
 * 例外：无
 * 日期：(2002-8-26 11:39:21)
 * 修改日期，修改人，修改原因，注释标志：
 */
private void initiHideItems() {
	hideHeadTableCol("pk_corp");
	hideHeadTableCol("cbiztype");
	hideHeadTableCol("cpurorganization");
	//V5 Del:
	//hideHeadTableCol("cstoreorganization");
	hideHeadTableCol("cvendormangid");
	hideHeadTableCol("cfreecustid");

	hideHeadTableCol("caccountbankid");
//	hideHeadTableCol("cdeliveraddress");
	hideHeadTableCol("cemployeeid");
	hideHeadTableCol("cdeptid");
	hideHeadTableCol("creciever");
	hideHeadTableCol("cgiveinvoicevendor");

	hideHeadTableCol("ctransmodeid");
	hideHeadTableCol("ctermprotocolid");

	return ;
}

/**
 * 作者：王印芬
 * 功能：增加列表表头监听，子类可重写该方法
 * 参数：无
 * 返回：无
 * 例外：无
 * 日期：(2002-4-22 11:39:21)
 * 修改日期，修改人，修改原因，注释标志：
 */
protected void initiListener(int	iSelectionModel) {
	//多选监听
	getHeadTable().setCellSelectionEnabled(false);
	//getHeadTable().setSelectionMode(javax.swing.ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
	getHeadTable().setSelectionMode(iSelectionModel);
	getHeadTable().getSelectionModel().addListSelectionListener(this) ;
	//行号排序监听
	getBodyBillModel().setSortPrepareListener(this) ;
	//since v55
	getBodyBillModel().addTotalListener(this);

}

/**
 * 订单列表界面中全部取消
 * @param
 * @return
 * @exception
 * @see
 * @since		2001-04-28
*/
public void onActionDeselectAll() {

	int		iLen = getHeadBillModel().getRowCount();
	//设为全部取消选择
	getHeadTable().removeRowSelectionInterval(0, iLen-1);
}

/**
 * 作者：王印芬
 * 功能：订单列表界面中全部选择
 * 参数：无
 * 返回：无
 * 例外：无
 * 日期：(2001-4-18 13:24:16)
 * 修改日期，修改人，修改原因，注释标志：
 * 2002-06-05	王印芬	加入对全选、全消按钮的控制
 */
public void onActionSelectAll() {

	//设为全部选中
	getHeadTable().setRowSelectionInterval(0, getHeadBillModel().getRowCount()-1);
}

/**
 * 作者：王印芬
 * 功能：设置精度
 * 参数：ActionEvent e		事件
 * 返回：无
 * 例外：无
 * 日期：(2002-4-22 11:39:21)
 * 修改日期，修改人，修改原因，注释标志：
 */
private void setBodyDigits_CorpRelated(String pk_corp){

	PoCardPanel.setBodyDigits_CorpRelated(pk_corp,getBodyBillModel()) ;

	//为订单->到货及入库单使用
	int[] iaDigit = PoPublicUIClass.getShowDigits( pk_corp ) ;
	if (getBodyItem("nnotarrvnum")!=null) {
		getBodyItem("nnotarrvnum").setDecimalDigits(iaDigit[0]);
	}
	if (getBodyItem("nnotstorenum")!=null) {
		getBodyItem("nnotstorenum").setDecimalDigits(iaDigit[0]);
	}

	//转单时的单价
	if (getBodyItem("nprice")!=null) {
		getBodyItem("nprice").setDecimalDigits(iaDigit[2]);
	}
	if (getBodyItem("ntaxprice")!=null) {
		getBodyItem("ntaxprice").setDecimalDigits(iaDigit[2]);
	}
}

/**
* 作者：王印芬
* 功能：当净单价(本币)超过最高单价时，表体行着色
* 参数：无
* 返回：无
* 例外：无
* 日期：(2004-5-17 11:39:21)
* 修改日期，修改人，修改原因，注释标志：
*/
private void setColor() {
	if (getBodyItem("nmoney") == null
		|| getBodyItem("nordernum") == null
		|| getBodyItem("nmaxprice") == null) {
		return;
	}

	//净单价超出最高限价进行标识
	ArrayList listRow = new ArrayList();

	int iBodyCount = getBodyBillModel().getRowCount();

	//本币金额、数量、最高限价、单价
	UFDouble dMoney = null;
	UFDouble dNum = null;
	UFDouble dMaxprice = null;
	UFDouble dPrice = null;
	for (int i = 0; i < iBodyCount; i++) {

	    dMaxprice = PuPubVO.getUFDouble_ValueAsValue( getBodyBillModel().getValueAt(i, "nmaxprice") );
	    if (dMaxprice==null) {
	    	continue ;
	    }
	    dMoney = PuPubVO.getUFDouble_ValueAsValue( getBodyBillModel().getValueAt(i, "nmoney"));
	    dNum = PuPubVO.getUFDouble_ValueAsValue( getBodyBillModel().getValueAt(i, "nordernum"));
	    if (dMoney==null || dNum==null) {
	    	continue ;
	    }
	    dPrice = dMoney.div(dNum) ;

	    if ( dPrice.compareTo(dMaxprice)>0 ) {
	    	listRow.add(new Integer(i));
	    }
	}

	if (listRow.size() > 0) {
		int k[] = new int[listRow.size()];
		for (int i = 0; i < listRow.size(); i++)
			k[i] = ((Integer) listRow.get(i)).intValue();
		nc.ui.scm.pub.BillTools.changeRowNOColor(getBodyTable(), k);
	} else {
		nc.ui.scm.pub.BillTools.changeRowNOColor(getBodyTable(), null);
	}
}

/**
 * 作者：王印芬
 * 功能：设置当前调用者
 * 参数：无
 * 返回：无
 * 例外：无
 * 日期：(2001-05-22 13:24:16)
 * 修改日期，修改人，修改原因，注释标志：
 */
private void setContainer(Container	newCon) {
	m_conInvoker = newCon;
}

/**
 * 作者：王印芬
 * 功能：设置表头精度
 * 参数：String pk_corp	公司ID
 * 返回：无
 * 例外：无
 * 日期：(2003-9-4 11:39:21)
 * 修改日期，修改人，修改原因，注释标志：
 */
private void setHeadDigits(int iDigit){
	//预付款限额、预付款
	//int		iDigit =2 ;
	//try {
		//iDigit = POPubSetUI.getMoneyDigitByCurr_Finance(
			//POPubSetUI.getCurrArith_Finance(pk_corp).getLocalCurrPK()
			//) ;
	//} catch (Exception	e) {
		//PuTool.reportException(this,e);
	//}

	//本币财务精度金额类
	int		iLen = OrderHeaderVO.getDbMnyFields_Local_Finance().length ;
	for (int i = 0; i < iLen; i++){
		BillItem	item = getHeadItem(OrderHeaderVO.getDbMnyFields_Local_Finance()[i]) ;
		if (item!=null) {
			item.setDecimalDigits(iDigit);
		}
	}

	//版本
	BillItem	item = getHeadItem("nversion");
	if(item != null){
		item.setDecimalDigits(1) ;
	}
}

/**
 * 作者：王印芬
 * 功能：得到模板ID
 * 参数：无
 * 返回：无
 * 例外：无
 * 日期：(2003-9-24 11:39:21)
 * 修改日期，修改人，修改原因，注释标志：
 */
private	void	setTemplateId(String	sValue){
	m_sTemplateId = sValue;
}

/**
 * 作者：王印芬
 * 功能：实现ListSelectionListener的监听方法
 * 参数：ListSelectionEvent e	监听事件
 * 返回：无
 * 例外：无
 * 日期：(2002-6-27 11:39:21)
 * 修改日期，修改人，修改原因，注释标志：
 */
public void valueChanged(ListSelectionEvent e) {
	getBodyTabbedPane().setSelectedIndex(0);
	Timer time = new Timer();
	time.start();
	time.addExecutePhase("*******************卡片切换列表加载表体前１１１１１１１＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊８８");
	if (e.getValueIsAdjusting()==true) {
		return ;
	}

	int iCount = getHeadTable().getRowCount();
	for (int i = 0; i < iCount; i++) {
		getHeadBillModel().setRowState(i, BillModel.NORMAL);
	}

	//得到被选中的行
	int[] iaSelectedRow = getHeadTable().getSelectedRows();
	if ( iaSelectedRow!=null ){
		iCount = iaSelectedRow.length ;
		//选中的行表示为打＊号
		for (int i = 0; i < iCount; i++) {
			getHeadBillModel().setRowState(iaSelectedRow[i], BillModel.SELECTED);
		}
	}
	time.addExecutePhase("*******************卡片切换列表开始加载表体２２２２２２＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊８８");
	if ( getHeadSelectedCount()!=1 ) {
		setBodyValueVO(null) ;
	}else{
		//getContainer().setVOPos( getVOPos(getHeadSelectedRow()) ) ;
		displayCurVOBody( getHeadSelectedRow() );
	}

	//按钮变化
	getContainer().setButtonsStateList() ;
	time.addExecutePhase("*******************卡片切换列表加载表体结束＝＝＝＝＝＝＝＝＝＝＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊８８");
	time.showAllExecutePhase("完了＝＝＝＝＝＝＝＝００００００００００００００");
	time.showExecuteTime("加载表体时间");
}

/**
 * 获取汇率精度设置工具
 * */
public POPubSetUI2 getPoPubSetUi2(){
    if(m_listPoPubSetUI2 == null){
        m_listPoPubSetUI2 = new POPubSetUI2();
    }
    return m_listPoPubSetUI2;
}
//since v55, 赠品不计合计(与销售订单做成一致)
public UFDouble calcurateTotal(String key) {
	UFDouble total = new UFDouble(0.0);
	UFBoolean blargessflag = null;
	int iLen = getBillModel().getRowCount();
	for (int i = 0; i < iLen; i++) {
		blargessflag = PuPubVO.getUFBoolean_NullAs(getBillModel().getValueAt(i, "blargess"), UFBoolean.FALSE);
		if (OrderItemVO.isPriceOrMny(key) && blargessflag.booleanValue()){
			continue;
		}
		total = total.add(PuPubVO.getUFDouble_NullAsZero(getBillModel().getValueAt(i, key)));
	}
//	getBillModel().setNeedCalculate(doCalculate)
	return total;
}
public BillModel getBillModel(){
  if(m_billmodel == null){
    m_billmodel = getBodyBillModel();
  }
  return m_billmodel;
}
}