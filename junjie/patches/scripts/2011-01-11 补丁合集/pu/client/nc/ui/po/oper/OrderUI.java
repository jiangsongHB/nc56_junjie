package nc.ui.po.oper;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import nc.bs.framework.common.NCLocator;
import nc.itf.pu.mm.ISrvPUToMM;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.jdbc.framework.processor.MapProcessor;
import nc.ui.ml.NCLangRes;
import nc.ui.po.OrderHelper;
import nc.ui.po.pub.ContractClassParse;
import nc.ui.po.pub.InvAttrCellRenderer;
import nc.ui.po.pub.PoCardPanel;
import nc.ui.po.pub.PoListPanel;
import nc.ui.po.pub.PoPublicUIClass;
import nc.ui.po.pub.PoQueryCondition;
import nc.ui.pr.pray.PraybillHelper;
import nc.ui.pu.jj.JJPuScmPubHelper;
import nc.ui.pu.jjpanel.InfoCostPanel;
import nc.ui.pu.jjpanel.InvDetailDialog;
import nc.ui.pu.jjpanel.InvDetailDialog421;
import nc.ui.pu.pub.POPubSetUI2;
import nc.ui.pu.pub.PuTool;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UITable;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillEditListener;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillListPanel;
import nc.ui.pub.bill.BillModel;
import nc.ui.pub.bill.BillScrollPane;
import nc.ui.pub.linkoperate.ILinkAdd;
import nc.ui.pub.linkoperate.ILinkAddData;
import nc.ui.pub.linkoperate.ILinkApprove;
import nc.ui.pub.linkoperate.ILinkApproveData;
import nc.ui.pub.linkoperate.ILinkMaintain;
import nc.ui.pub.linkoperate.ILinkMaintainData;
import nc.ui.pub.linkoperate.ILinkQuery;
import nc.ui.pub.linkoperate.ILinkQueryData;
import nc.ui.pub.msg.PfLinkData;
import nc.ui.pub.pf.PfUtilClient;
import nc.ui.scm.pub.bill.IBillExtendFun;
import nc.ui.uap.sf.SFClientUtil;
import nc.vo.pf.change.PfUtilBaseTools;
import nc.vo.pi.NormalCondVO;
import nc.vo.po.OrderHeaderVO;
import nc.vo.po.OrderItemVO;
import nc.vo.po.OrderVO;
import nc.vo.po.pub.OrderPubVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.query.ConditionVO;
import nc.vo.rc.receive.ArriveorderItemVO;
import nc.vo.scm.constant.ScmConst;
import nc.vo.pu.jjvo.InformationCostVO;
import nc.vo.pu.jjvo.InvDetailVO;
import nc.vo.scm.pu.BillStatus;
import nc.vo.scm.pu.BillTypeConst;
import nc.vo.scm.pu.PUMessageVO;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.scm.pub.SCMEnv;
import nc.vo.scm.pub.session.ClientLink;

public class OrderUI	extends PoToftPanel
	implements
		nc.ui.pf.query.ICheckRetVO,//getVo()
		IBillExtendFun,//支持产业链功能扩展::getExtendBtns()/onExtendBtnsClick()/setExtendBtnsStat()
		ILinkMaintain,//关联修改
		ILinkAdd,//关联新增
		ILinkApprove,//审批流
		ILinkQuery//逐级联查
		{
	
	private ButtonObject boInvDetail;//存货明细按钮 add by MeiChao 2010-12-14 
	public ButtonObject getBoInvDetail() {
		if(boInvDetail==null){
			boInvDetail=new ButtonObject("存货明细","存货明细","存货明细");
		}
		return boInvDetail;
	}
	private InvDetailVO[] invDetailVOs;//表体存货明细.及相应的get,set方法 add by MeiChao 2010-12-14 
	public InvDetailVO[] getInvDetailVOs() {
		return invDetailVOs;
	}
	public void setInvDetailVOs(InvDetailVO[] invDetailVOs) {
		this.invDetailVOs = invDetailVOs;
	}
	private ButtonObject boInfoCost ;//费用录入按钮  add by QuSida 2010-8-13 (佛山骏杰)
	private ButtonObject[] extendBtns ; //二次开发按钮数组  add by QuSida 2010-8-13 (佛山骏杰)
	private  UFDouble number ;//数量  add by QuSida 2010-9-7 (佛山骏杰)
/**
 * 作者：王印芬
 * 功能：默认构造子
 * 参数：无　
 * 返回：无
 * 例外：无
 * 日期：(2001-5-22 11:39:21)
 * 修改日期，修改人，修改原因，注释标志：
 */
public OrderUI() {
	super();
}

/**
 * 设置单据标题
 * @param
 * @return		String
 * @exception
 * @see
 * @since		2001-04-26
*/
public String getTitle(){
	return	getPoCardPanel().getTitle() ;
}
	//查询条件设置模板
	private OrderUIQueDlg m_condition = null;

/**
 * 作者：王印芬
 * 功能：用于设置消息中心双击消息时弹出的订单审批界面
 * 参数：	String	pk_corp				当前登陆公司ID
 * 			String	billType			单据类型，此处应为BillTypeConst.PO_ORDER
 * 			String	businessType		单据的业务类型
 * 			String	operator			当前操作员ID
 * 			String	billID				需要审批的单据ID
 * 返回：无
 * 例外：无
 * 日期：(2002-4-22 11:39:21)
 * 修改日期，修改人，修改原因，注释标志：
 */
public OrderUI(
	String	pk_corp,
	String	billType,
	String	businessType,
	String	operator,
	String	billID
	) {

	super();
	initMsgCenter() ;

	//设置卡片界面下列字段显示：
	//BillData data = getPoCardPanel().getBillData();
	getPoCardPanel().getBodyItem("naccumarrvnum").setShow(true);
	getPoCardPanel().getBodyItem("naccumstorenum").setShow(true);
	getPoCardPanel().getBodyItem("naccuminvoicenum").setShow(true);

	//setCauditid(billID);

	//没有符合条件的订单
	try {
		getBufferVOManager().resetVOs(new OrderVO[]{OrderHelper.queryOrderVOByKey(billID)}) ;
	} catch (Exception	e) {
		SCMEnv.out(e);
		MessageDialog.showHintDlg(this,NCLangRes.getInstance().getStrByID("SCMCOMMON","UPPSCMCommon-000270")/*@res "提示"*/,OrderPubVO.returnHintMsgWhenNull(e.getMessage())) ;
	}

	if(getBufferLength()==0){
		MessageDialog.showHintDlg(this,NCLangRes.getInstance().getStrByID("SCMCOMMON","UPPSCMCommon-000270")/*@res "提示"*/,NCLangRes.getInstance().getStrByID("4004020201","UPP4004020201-000023")/*@res "没有符合查询条件的订单"*/) ;
		getPoCardPanel().addNew() ;
		return ;
	}

	//设置订单VO数组
	//setVOPos(0);
	getBufferVOManager().setVOPos(0) ;

	//设置卡片界面数据
	getPoCardPanel().displayCurVO( getOrderViewVOAt( getBufferVOManager().getVOPos() ), false ) ;

	getPoCardPanel().setEnabled(false) ;
	//设置操作员
	PoPublicUIClass.setCuserId(
		new	OrderVO[]{
			getOrderViewVOAt(getBufferVOManager().getVOPos())
		}
	) ;
	
}

/**
 * 作者：王印芬
 * 功能：默认构造子
 * 参数：无　
 * 返回：无
 * 例外：无
 * 日期：(2001-5-22 11:39:21)
 * 修改日期，修改人，修改原因，注释标志：
 */
public OrderUI(nc.ui.pub.FramePanel panel) {
	super(panel);	
}
/**
 * 作者：WYF
 * 功能：获取按钮管理器
 * 参数：无
 * 返回：无
 * 例外：无
 * 日期：(2004-10-20 11:39:21)
 * 修改日期，修改人，修改原因，注释标志：
 */
protected PoToftPanelButton getBtnManager(){
	if (m_btnManager==null) {
		m_btnManager = new	OrderUIButton(this) ;


		//项目管理二次开发
		try {
			ContractClassParse.resetButton(
				getParameter(ContractClassParse.getParaName()),
				m_btnManager.btnBillContractClass) ;
        } catch (Exception e) {
        	SCMEnv.out("项目管理二次开发BUG："+e.getMessage());
        }
	}
	return	 m_btnManager;
}

/**
 * 作者：WYF
 * 功能：获取单据Panel。
 * 参数：无
 * 返回：无
 * 例外：无
 * 日期：(2001-10-20 11:39:21)
 * 修改日期，修改人，修改原因，注释标志：
 */
protected PoCardPanel getInitPoCardPanel(POPubSetUI2 setUi){
	return	new PoCardPanel(
			this,
			PoPublicUIClass.getLoginPk_corp(),
			BillTypeConst.PO_ORDER,
			setUi
		) ;
}

/**
 * 作者：WYF
 * 功能：获取列表Panel。
 * 参数：无
 * 返回：无
 * 例外：无
 * 日期：(2001-10-20 11:39:21)
 * 修改日期，修改人，修改原因，注释标志：
 */

protected PoListPanel getInitPoListPanel(POPubSetUI2 setUi){
	return	new PoListPanel(
			this,
			PoPublicUIClass.getLoginPk_corp(),
			BillTypeConst.PO_ORDER,
			setUi
		) ;
}

/**
 * 作者：WYF
 * 功能：获取查询条件设置实例
 * 参数：无
 * 返回：无
 * 例外：无
 * 日期：(2001-10-20 11:39:21)
 * 修改日期，修改人，修改原因，注释标志：
 * 2002-06-21	wyf		修改查询模板与业务类型相关的情况
 */

public PoQueryCondition getPoQueryCondition(){
	if (m_condition==null) {
		m_condition = new OrderUIQueDlg(
			this,
			"4004020201",
			PoPublicUIClass.getLoginPk_corp(),
			PoPublicUIClass.getLoginUser()) ;
		//支持打印次数
		m_condition.setShowPrintStatusPanel(true);
		m_condition.addCurToCurDate("po_order.dorderdate");
	}
	return	m_condition ;
}

/**
 * 作者：王印芬
 * 功能：得到按钮
 * 参数：无
 * 返回：无
 * 例外：无
 * 日期：(2003-9-12 11:39:21)
 * 修改日期，修改人，修改原因，注释标志：
 */

protected OrderVO[] getQueriedVOHead() throws Exception {

    NormalCondVO[] normalVos = ((OrderUIQueDlg) getPoQueryCondition()).getNormalVOs(false);
    ConditionVO[] voaUserDefined = getPoQueryCondition().getConditionVO();
    if(voaUserDefined  == null || (voaUserDefined != null && voaUserDefined.length == 0)){
    	return getBufferVOManager().getVOs();
    }
    ArrayList listVos = new ArrayList();
    if(normalVos != null && normalVos.length >0){
        int iLen = normalVos.length;
        for (int i = 0; i < iLen; i++) {
            if (normalVos[i] == null) {
                continue;
            }
            listVos.add(normalVos[i]);
        }
        NormalCondVO voTmp = new NormalCondVO();
        voTmp.setKey("ConstrictClosedLines");
        voTmp.setValue("Y");
        listVos.add(voTmp);
		/*天音专版需求：维护订单处可见关闭行(含全部订单行均关闭的订单)
		  处理规则：
		  1)、未钩选关闭：查询出的内容不包含关闭的订单行及整单关闭的订单
		  2)、只钩选关闭：只查询出关闭的订单行
		  3)、钩选关闭同时钩选其它：查询出的内容包含关闭行及整单关闭的订单
		*/
//		if(nc.vo.scm.pub.CustomerConfigVO.getCustomerName().equalsIgnoreCase(nc.vo.scm.pub.CustomerConfigVO.NAME_TIANYIN)){
        if(true){//20050406 xy ghl czp 扩充此内容到V31
			//1)、未钩选关闭：查询出的内容不包含关闭的订单行及整单关闭的订单
			if(!((OrderUIQueDlg)getPoQueryCondition()).isClosedSelected()){
				voTmp.setValue("Y");
			}
//			// 3)、钩选关闭同时钩选其它：查询出的内容包含关闭行及整单关闭的订单
			else 
        if(!((OrderUIQueDlg)getPoQueryCondition()).isOnlyClosed()){
				voTmp.setValue("N");
			}
			//2)、只钩选关闭：只查询出关闭的订单行
			if(((OrderUIQueDlg)getPoQueryCondition()).isOnlyClosed()){
				voTmp.setValue("N");
				NormalCondVO voTmp1 = new NormalCondVO();
				voTmp1.setKey("OnlyClosedLines");
				voTmp1.setValue("Y");
				listVos.add(voTmp1);
			}
		}
		//增加操作员条件
        voTmp = new NormalCondVO();
        voTmp.setKey(OrderPubVO.NORMALVO_COPERATOR);
        voTmp.setValue(PoPublicUIClass.getLoginUser());
        listVos.add(voTmp);
        
        normalVos = (NormalCondVO[]) listVos.toArray(new NormalCondVO[listVos.size()]);
    }

 
  
	return OrderHelper.queryOrderArrayOnlyHead(normalVos, voaUserDefined,getClientEnvironment().getUser().getPrimaryKey());
}

/**
 * 作者：WYF
 * 功能：获取按钮管理器
 * 参数：无
 * 返回：无
 * 例外：无
 * 日期：(2004-10-20 11:39:21)
 * 修改日期，修改人，修改原因，注释标志：
 */

private OrderUIButton getThisBtnManager(){

	return	((OrderUIButton)getBtnManager()) ;
}

/**
 * 作者：李金巧
 * 功能：该方法是实现接口ICheckRetVO的方法，与构造方法nc.ui.po.oper.OrderUI_bak(String, String, String, String, String)配合使用
 *		该接口为审批流设计，以实现审批时得到订单VO
 *		请不要随意删除及修改该方法，以避免错误
 * 参数：无
 * 返回：nc.vo.pub.AggregatedValueObject	为定单VO
 * 例外：无
 * 日期：(2001-5-13 11:39:21)
 * 修改日期，修改人，修改原因，注释标志：
 */
public nc.vo.pub.AggregatedValueObject getVo() throws Exception {

	return	getOrderViewVOAt( getBufferVOManager().getVOPos() ) ;

}

/**
 * 作者：李亮
 * 功能：消息中心初始化
 * 参数：无
 * 返回：无
 * 例外：无
 * 日期：(2001-04-21  11:39:21)
 * 修改日期，修改人，修改原因，注释标志：
 * 2002-07-25	wyf		注释掉参照公司ID的设置
 */
private void initMsgCenter() {

	setCurPk_corp( PoPublicUIClass.getLoginPk_corp() );
	//加载卡片
	setLayout(new java.awt.BorderLayout());
	add(getPoCardPanel(),java.awt.BorderLayout.CENTER);
	//设置按钮组
	setButtons(getThisBtnManager().getBtnaMsgCenter());
	//按钮
	ContractClassParse.resetButton(
		getParameter(ContractClassParse.getParaName()),
		getThisBtnManager().btnBillContractClass) ;

}

/**
 * 作者：WYF
 * 功能：配合按钮动作
 * 参数：nc.ui.pub.ButtonObject bo		按钮
 * 返回：无
 * 例外：无
 * 日期：(2003-9-12 11:39:21)
 * 修改日期，修改人，修改原因，注释标志：
 */

protected void onButtonClickedBill(nc.ui.pub.ButtonObject bo) {
  
	
	if(bo == getThisBtnManager().btnMsgCenterAudit){
		onMsgCenterAudit();
	}else if(bo == getThisBtnManager().btnMsgCenterUnAudit){
		onMsgCenterUnAudit();
		//审批中修订
	}else if(bo == getThisBtnManager().btnBillMaintainRevise){
		onMsgRevise();
	}
	//支持产业链功能扩展
	else if(PuTool.isExist(getExtendBtns(),bo)){
		for (int i = 0; i < getExtendBtns().length; i++) {
			if(bo == getExtendBtns()[i]){
				System.out.println(bo);
				System.out.println();
				
			}
			System.out.println(getExtendBtns()[i]);
		}
		  onExtendBtnsClick(bo);
    }else{
		super.onButtonClickedBill(bo) ;
	}
}
/**
 * @function 费用录入的按钮动作
 *
 * @author QuSida
 *
 * @return void
 *
 * @date 2010-8-13 上午09:33:31
 */
	private void onBoInfoCost() {
		InformationCostVO[]  vos = (InformationCostVO[] )getPoCardPanel().getBillModel("jj_scm_informationcost").getBodyValueVOs(InformationCostVO.class.getName());
		
		InfoCostPanel c = null;
		if (vos == null || vos.length == 0)
			c = new InfoCostPanel(getPoCardPanel());
		else
			c = new InfoCostPanel(getPoCardPanel(), vos);
		
		// 打开费用录入界面
		c.showModal();
		// 当费用录入界面关闭时,将录入的数据存放到费用信息页签上
		if (c.isCloseOK()) {
			InformationCostVO[] infoCostVOs = c.getInfoCostVOs();
			if (infoCostVOs != null && infoCostVOs.length != 0){
				// 当费用录入界面的vo数组不为空时,将vo存到费用录入页签上
			
					int temp = getPoCardPanel().getBillModel("table").getRowCount();
				    number = new UFDouble(0.0);
				    //UFDouble mny = null;
				   
				    for (int i = 0; i < temp; i++) {
				    	number = number.add(new UFDouble((getPoCardPanel().getBillModel("table").getValueAt(i,"nordernum")==null?"0.0":getPoCardPanel().getBillModel("table").getValueAt(i,"nordernum").toString())));    		    	  
					}		    
							    	
			}
			for (int i = 0; i < infoCostVOs.length; i++) {
				UFBoolean ismny = (UFBoolean)infoCostVOs[i].getAttributeValue("ismny");
		    	//infoCostVOs[i].setAttributeValue("nnumber", number);  2010-11-01 MeiChao 取消对费用行数量的重新赋值.
		    	if(ismny == null || !ismny.booleanValue()){
		    	//infoCostVOs[i].setAttributeValue("noriginalcurmny", infoCostVOs[i].getNoriginalcurprice().multiply(number)); 2010-11-01 MeiChao 取消对费用行金额的重新赋值.
		    	}else{
		    	//	infoCostVOs[i].setAttributeValue("noriginalcurprice", infoCostVOs[i].getNoriginalcurmny().div(number));	2010-11-01 MeiChao 取消对费用行金额的重新赋值.
		    	}
				}
				
				getPoCardPanel().getBillData().setBodyValueVO(
						"jj_scm_informationcost", infoCostVOs);
				getPoCardPanel().getBillModel("jj_scm_informationcost").execLoadFormula();
				}
	}
/**
 * 审批中修订
 */
private void onMsgRevise(){
	bRevise = true;
	bAddNew = false; 
	// 设置按钮
	setButtonsStateEdit() ;
	OrderVO vo = getOrderViewVOAt(getBufferVOManager().getVOPos());
	// 设置编辑性
	getPoCardPanel().onActionRevise(vo) ;
	// 刷新
	updateUI();
}
/**
 * 功能：该方法为审批流提供，不可随意修改及删除
 *		审批流双击消息后，弹出OrderUI界面，其上的按钮有审批按钮，对应此函数
 * 参数：无
 * 返回：无
 * 例外：无
 * 日期：(2002-10-15 16:15:35)
 * 修改日期，修改人，修改原因，注释标志：
 * 2003-03-05	wyf		修改审批后对单据的显示
 */
private void onMsgCenterAudit() {

	OrderVO vo = getOrderViewVOAt(getBufferVOManager().getVOPos());
	/**
		 * 2011-01-08 MeiChao begin 审核前检查存货明细完整性 
		 */
		if(vo.getHeadVO().getVdef20()!=null&&"Y".equals(vo.getHeadVO().getVdef20())){
		//直调采购订单不检查存货明细维护起概况,不作任何操作.
		}else {
		String checkSQL = "select * from (select nvl(t.nordernum,0) - nvl(m.sumweight,0) isover"
				+ " from (select t.nordernum, t.corder_bid"
				+ " from po_order_b t"
				+ " where t.corderid = '"
				+ vo.getHeadVO().getPrimaryKey()
				+ "' and t.dr=0) t"
				+ " left join (select sum(t.contractweight) sumweight, t.corder_bid"
				+ " from scm_invdetail t"
				+ " where t.corder_bid in"
				+ " (select m.corder_bid"
				+ " from po_order_b m"
				+ " where m.corderid = '"
				+ vo.getHeadVO().getPrimaryKey()
				+ "' and m.dr=0) and t.dr=0 "
				+ " group by t.corder_bid) m"
				+ " on t.corder_bid = m.corder_bid"
				+ " ) n where n.isover<>0 ";
		IUAPQueryBS queryService = (IUAPQueryBS) NCLocator
				.getInstance().lookup(IUAPQueryBS.class.getName());
		try {
			Object checkResult = queryService.executeQuery(checkSQL,
					new MapProcessor());
			if (checkResult != null) {
				MessageDialog.showHintDlg(this, "提示", "当前订单的存货明细没有维护完整,请补完后再进行审核操作!");
				updateUI();//刷新UI
				return;
			}
		} catch (BusinessException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	//2011-01-08 MeiChao end 审核前检查存货明细完整性
	
	
	
	PoPublicUIClass.setCuserId(new	OrderVO[]{vo}) ;

	OrderVO VOs[] = new OrderVO[1];
	VOs[0] = new OrderVO();
	VOs[0].getHeadVO().setCorderid(vo.getHeadVO().getCorderid());
	VOs[0].getHeadVO().setTs(vo.getHeadVO().getTs());
  //回退审批人及审批日期哈希表，操作失败时用到
  HashMap<String, ArrayList<Object>> mapAuditInfo = new HashMap<String, ArrayList<Object>>();
  ArrayList<Object> listAuditInfo = null;
  OrderHeaderVO headVO = null;
  for (int i = 0; i < VOs.length; i++) {
    headVO = VOs[i].getHeadVO();
    //操作失败时用到，回退审批人及审批日期哈希表
    if(PuPubVO.getString_TrimZeroLenAsNull(headVO.getCauditpsn()) != null){
      listAuditInfo = new ArrayList<Object>();
      listAuditInfo.add(headVO.getCauditpsn());
      listAuditInfo.add(headVO.getDauditdate());
      mapAuditInfo.put(headVO.getPrimaryKey(),listAuditInfo);
    }
    headVO.setCauditpsn(getClientEnvironment().getUser().getPrimaryKey());
    headVO.setDauditdate(getClientEnvironment().getDate());
  }
	try {
		String strErr = PuTool.getAuditLessThanMakeMsg(VOs,"dorderdate","vordercode", getClientEnvironment().getDate(),ScmConst.PO_Order);
		if (strErr != null) {
			throw new BusinessException(strErr);
		}
		/*
		//给所有采购订单赋操作员
		String strOpr = getClientEnvironment().getUser().getPrimaryKey();
		for (int i = 0; i < VOs.length; i++) {
			OrderHeaderVO headVO = VOs[i].getHeadVO();
			headVO.setCoperator(strOpr);
		}
		*/
	    Object[] objs = new Object[1];
	    objs[0] = new ClientLink(getClientEnvironment());

	    HashMap hmPfExParams = new HashMap();
	    hmPfExParams.put(PfUtilBaseTools.PARAM_RELOAD_VO, PfUtilBaseTools.PARAM_RELOAD_VO);

			PfUtilClient.runBatch(
				this,
				"APPROVE"+nc.ui.pub.ClientEnvironment.getInstance().getUser().getPrimaryKey(),
				ScmConst.PO_Order,
				getClientEnvironment().getDate().toString(),
				VOs,
				objs,null,hmPfExParams);
		if (!PfUtilClient.isSuccess()) {
			//回退审批人及审批日期
			if(mapAuditInfo.size() > 0){
				for (int i = 0; i < VOs.length; i++) {
					headVO = VOs[i].getHeadVO();
					if(!mapAuditInfo.containsKey(headVO.getPrimaryKey())){
						headVO.setCauditpsn(null);
						headVO.setDauditdate(null);
						continue;
					}          
					listAuditInfo = (ArrayList<Object>)mapAuditInfo.get(headVO.getPrimaryKey());
					headVO.setCauditpsn((String)listAuditInfo.get(0));
					headVO.setDauditdate((UFDate)listAuditInfo.get(1));
				}
			}
			//不审批采购订单
			return;
		} else {
			String billID = vo.getParentVO().getPrimaryKey();
      OrderVO[] voNewOrder = OrderHelper.queryOrderVOsLight(
          new String[] {billID}, "AUDIT");
		/************记录业务日志*************/
      //since 56 后台做日志处理
//      Operlog operlog=new Operlog();
//      voNewOrder.getOperatelogVO().setOpratorname(nc.ui.pub.ClientEnvironment.getInstance().getUser().getUserName());
//      voNewOrder.getOperatelogVO().setCompanyname(nc.ui.pub.ClientEnvironment.getInstance().getCorporation().getUnitname());
//      voNewOrder.getOperatelogVO().setOperatorId(nc.ui.pub.ClientEnvironment.getInstance().getUser().getPrimaryKey());
//      operlog.insertBusinessExceptionlog(voNewOrder, "审批", "审批", nc.vo.scm.funcs.Businesslog.MSGMESSAGE, nc.vo.scm.pu.BillTypeConst.PO_ORDER,
//				nc.ui.sm.cmenu.Desktop.getApplet().getParameter("USER_IP"));
      /************记录业务日志* end ************/
//    刷新表头VO
      if(voNewOrder == null){
        return;
      }
      getBufferVOManager().refreshByHeaderVo(voNewOrder[0].getHeadVO());

      //重新设置
      getPoCardPanel().reloadBillCardAfterAudit(getOrderDataVOAt(
          getBufferVOManager().getVOPos())
          ,getBufferVOManager().getVOAt_LoadItemNo(getBufferVOManager().getVOPos()));

			//刷新界面数据，显示审批人、审批日期
			//注：此处不可调用displayCurVOToBillPanel
//			getPoCardPanel().displayCurVO( getOrderViewVOAt(getBufferVOManager().getVOPos()) , false);
		}
    //
    setButtonsStateBrowse();
    //
		showHintMessage(NCLangRes.getInstance().getStrByID("SCMCOMMON","UPPSCMCommon-000236")/*@res "审核成功"*/);
	} catch (Exception e) {
		nc.ui.pu.pub.PuTool.outException(this,e) ;
	}
}
/**
 * 作者：晁志平
 * 功能：该方法为审批流提供，不可随意修改及删除
 *		审批流双击消息后，弹出OrderUI界面，其上的按钮有 弃审 按钮，对应此函数
 * 参数：无
 * 返回：无
 * 例外：无
 * 日期：(2005-06-27 15:49:35)
 * 修改日期，修改人，修改原因，注释标志：
 */


private void onMsgCenterUnAudit() {

	OrderVO vo = getOrderViewVOAt(getBufferVOManager().getVOPos());
	PoPublicUIClass.setCuserId(new	OrderVO[]{vo}) ;

	OrderVO VOs[] = new OrderVO[1];
	VOs[0] = vo;
  //回退审批人及审批日期哈希表，操作失败时用到
  HashMap<String, ArrayList<Object>> mapAuditInfo = new HashMap<String, ArrayList<Object>>();
  ArrayList<Object> listAuditInfo = null;
  OrderHeaderVO headVO = null;
	for (int i = 0; i < VOs.length; i++) {
		headVO = VOs[i].getHeadVO();
    //操作失败时用到，回退审批人及审批日期哈希表
    if(PuPubVO.getString_TrimZeroLenAsNull(headVO.getCauditpsn()) != null){
      listAuditInfo = new ArrayList<Object>();
      listAuditInfo.add(headVO.getCauditpsn());
      listAuditInfo.add(headVO.getDauditdate());
      mapAuditInfo.put(headVO.getPrimaryKey(),listAuditInfo);
    }
    	vo.getHeadVO().setAttributeValue("cauditpsnold",vo.getHeadVO().getCauditpsn());//为判断是否允许弃审他人的单据
		headVO.setCauditpsn(getClientEnvironment().getUser().getPrimaryKey());
		headVO.setDauditdate(getClientEnvironment().getDate());
	}
	if(headVO.getBcooptoso()!=null&&headVO.getBcooptoso().booleanValue()){
		MessageDialog.showHintDlg(this,
				NCLangRes.getInstance().getStrByID("SCMCOMMON","UPPSCMCommon-000270")/* @res "提示" */,
				NCLangRes.getInstance().getStrByID("4004020201","UPP4004020201-V56001")/* @res "采购订单已经协同生成销售订单,不能弃审" */);
		return ;
	}
  OrderVO[] voNewOrder = null;
	try {
		String strOpr = getClientEnvironment().getUser().getPrimaryKey();
		/*
		for (int i = 0; i < VOs.length; i++) {
			OrderHeaderVO headVO = VOs[i].getHeadVO();
			headVO.setCoperator(strOpr);
		}
		*/
			PfUtilClient.processBatchFlow(
				this,
				"UNAPPROVE"+strOpr,
        ScmConst.PO_Order,
				getClientEnvironment().getDate().toString(),
				VOs,
				null);    
		if (!PfUtilClient.isSuccess()) {
      //回退审批人及审批日期
      if(mapAuditInfo.size() > 0){
        for (int i = 0; i < VOs.length; i++) {
          headVO = VOs[i].getHeadVO();
          if(!mapAuditInfo.containsKey(headVO.getPrimaryKey())){
            headVO.setCauditpsn(null);
            headVO.setDauditdate(null);
            continue;
          }          
          listAuditInfo = (ArrayList<Object>)mapAuditInfo.get(headVO.getPrimaryKey());
          headVO.setCauditpsn((String)listAuditInfo.get(0));
          headVO.setDauditdate((UFDate)listAuditInfo.get(1));
        }
      }
			//不审批采购订单
			return;
		} else {
			String billID = vo.getParentVO().getPrimaryKey();
//			getBufferVOManager().resetVOs(
//				new	OrderVO[]{OrderHelper.queryOrderVOByKey(billID)} ) ;
//      
      voNewOrder = OrderHelper.queryOrderVOsLight(new String[]{billID},"UNAUDIT");
//
//			getBufferVOManager().setVOPos(0);
//
//			//刷新界面数据，显示审批人、审批日期
//			//注：此处不可调用displayCurVOToBillPanel
//			getPoCardPanel().displayCurVO( getOrderViewVOAt(getBufferVOManager().getVOPos()), false );
//			updateButtons();
		}
		/************记录业务日志*************/
//	      Operlog operlog=new Operlog();
//	      for (OrderVO orderVO : VOs) {
//	    	  orderVO.getOperatelogVO().setOpratorname(nc.ui.pub.ClientEnvironment.getInstance().getUser().getUserName());
//	    	  orderVO.getOperatelogVO().setCompanyname(nc.ui.pub.ClientEnvironment.getInstance().getCorporation().getUnitname());
//	    	  orderVO.getOperatelogVO().setOperatorId(nc.ui.pub.ClientEnvironment.getInstance().getUser().getPrimaryKey());
//	    	  operlog.insertBusinessExceptionlog(orderVO, "弃审", "弃审", nc.vo.scm.funcs.Businesslog.MSGMESSAGE, nc.vo.scm.pu.BillTypeConst.PO_ORDER,
//						nc.ui.sm.cmenu.Desktop.getApplet().getParameter("USER_IP"));
//		}
	      /************记录业务日志* end ************/
		//刷新表头VO
    getBufferVOManager().refreshByHeaderVo(voNewOrder[0].getHeadVO());
    //重新设置
    getPoCardPanel().reloadBillCardAfterAudit(voNewOrder[0],getBufferVOManager().getVOAt_LoadItemNo(getBufferVOManager().getVOPos()));
    //
    setButtonsStateBrowse();
    //
		showHintMessage(NCLangRes.getInstance().getStrByID("common","UCH011")/*@res "弃审成功"*/);
	} catch (Exception e) {
	      //回退审批人及审批日期
	      if(mapAuditInfo.size() > 0){
	        for (int i = 0; i < VOs.length; i++) {
	          headVO = VOs[i].getHeadVO();
	          if(!mapAuditInfo.containsKey(headVO.getPrimaryKey())){
	            headVO.setCauditpsn(null);
	            headVO.setDauditdate(null);
	            continue;
	          }          
	          listAuditInfo = (ArrayList<Object>)mapAuditInfo.get(headVO.getPrimaryKey());
	          headVO.setCauditpsn((String)listAuditInfo.get(0));
	          headVO.setDauditdate((UFDate)listAuditInfo.get(1));
	        }
	      }
		nc.ui.pu.pub.PuTool.outException(this,e) ;
	}
}
/**
 * 二次开发功能扩展按钮，要求二次开发子类给出具体实现
 * 
 * @see nc.ui.scm.pub.bill.IBillExtendFun#getExtendBtns()
 *
 */
public ButtonObject[] getExtendBtns() {
	
	if(extendBtns == null || extendBtns.length == 0){
		//加入费用录入按钮 add by QuSida 2010-8-10  （佛山骏杰）
		extendBtns = new ButtonObject[]{getBoInfoCost(),this.getBoInvDetail()};
	return extendBtns;}
	else return extendBtns;
}
/**
 * @function 得到费用录入按钮
 *
 * @author QuSida
 *
 * @return ButtonObject
 *
 * @date 2010-8-20 上午10:51:54
 */
private ButtonObject getBoInfoCost(){
	if(boInfoCost == null){
		//费用录入按钮 add by QuSida 2010-8-10  （佛山骏杰）
		boInfoCost = new ButtonObject("费用录入","费用录入","费用录入");
		return boInfoCost;
	}
	else return boInfoCost;
}



/**
 * 点击二次开发按钮后的响应处理，要求二次开发子类给出具体实现
 * 
 * @see nc.ui.scm.pub.bill.IBillExtendFun#onExtendBtnsClick(nc.ui.pub.ButtonObject)
 * 
 */
public void onExtendBtnsClick(ButtonObject bo) {
	//加入费用录入按钮的动作 add by QuSida 2010-8-10  （佛山骏杰）
	 if(bo == getBoInfoCost()){
    	this.onBoInfoCost();
    }else if(bo == getBoInvDetail()){
    	this.onBoInvDetail();
    }
	
}
/**
 * 二次开发状态与原有界面状态处理邦定，要求二次开发子类给出具体实现
 * 
 * @see nc.ui.scm.pub.bill.IBillExtendFun#setExtendBtnsStat(int)
 * 
 *  状态数值对照表：
 * 
 *  0：初始化
 *  1：浏览卡片
 *  2：修改
 *  3：浏览列表
 *  4：转入列表
 */
public void setExtendBtnsStat(int iState) {
	//设置费用录入按钮的状态  add by QuSida 2010-8-13 (佛山骏杰)
	if(iState==0){
		getBoInfoCost().setEnabled(false);
		updateButton(getBoInfoCost());
	}
	else if(iState==1){
		getBoInfoCost().setEnabled(false);
		updateButton(getBoInfoCost());
	}
	else if(iState==2){
		getBoInfoCost().setEnabled(true);
		updateButton(getBoInfoCost());
	}
	else if(iState==3){
		getBoInfoCost().setEnabled(false);
		updateButton(getBoInfoCost());
	}
	else if(iState==4){
		getBoInfoCost().setEnabled(false);
		updateButton(getBoInfoCost());
	}
		
}
/**
 * 界面关联接口方法实现 -- 维护
 **/
public void doMaintainAction(ILinkMaintainData maintaindata) {
	SCMEnv.out("进入订单维护接口...");
	
	String billID = maintaindata.getBillID(); 
	
	setCurPk_corp( PoPublicUIClass.getLoginPk_corp() );
	//加载卡片
	setLayout(new java.awt.BorderLayout());
	add(getPoCardPanel(),java.awt.BorderLayout.CENTER);

	//设置卡片界面下列字段显示：
	//BillData data = getPoCardPanel().getBillData();
	getPoCardPanel().getBodyItem("naccumarrvnum").setShow(true);
	getPoCardPanel().getBodyItem("naccumstorenum").setShow(true);
	getPoCardPanel().getBodyItem("naccuminvoicenum").setShow(true);

	//setCauditid(billID);

	//没有符合条件的订单
	try {
		
		getPoListPanel().getHeadBillModel().clearBodyData();
		getPoListPanel().getBodyBillModel().clearBodyData();
		getBufferVOManager().resetVOs(new OrderVO[]{OrderHelper.queryOrderVOByKey(billID)}) ;
	} catch (Exception	e) {
		SCMEnv.out(e);
		MessageDialog.showHintDlg(this,NCLangRes.getInstance().getStrByID("SCMCOMMON","UPPSCMCommon-000270")/*@res "提示"*/,OrderPubVO.returnHintMsgWhenNull(e.getMessage())) ;
	}
	//如果当前登录公司不是操作员制单所在公司，则界面无操作按钮，仅提供浏览功能，by chao , xy , 2006-11-07
	String strLoginCorpId = PoPublicUIClass.getLoginPk_corp();
	String strPrayCorpId = getOrderViewVOAt(0) == null ? null : getOrderViewVOAt(0).getHeadVO().getPk_corp();
	boolean bSameCorpFlag = strLoginCorpId.equals(strPrayCorpId);
	
	if(getBufferLength()==0){
		MessageDialog.showHintDlg(this,NCLangRes.getInstance().getStrByID("SCMCOMMON","UPPSCMCommon-000270")/*@res "提示"*/,NCLangRes.getInstance().getStrByID("4004020201","UPP4004020201-000023")/*@res "没有符合查询条件的订单"*/) ;
		if(bSameCorpFlag){
			getPoCardPanel().addNew() ;
		}else{
			setButtonsNull();
		}
		return ;
	}

	//设置订单VO数组
	getBufferVOManager().setVOPos(0) ;

	//设置卡片界面数据
	getPoCardPanel().displayCurVO( getOrderViewVOAt( getBufferVOManager().getVOPos() ) , false) ;

	//设置操作员
	PoPublicUIClass.setCuserId(
		new	OrderVO[]{
			getOrderViewVOAt(getBufferVOManager().getVOPos())
		}
	) ;

  //不是同一公司，所有按钮不可见
  if(!bSameCorpFlag){
    setButtonsNull();
    return;
  }
  //如果是同一公司
  OrderVO voOrder = getOrderViewVOAt( getBufferVOManager().getVOPos() );
  if(voOrder.getHeadVO().isAuditted()){
    setButtonsStateBrowse();
    return;
  }
  setButtonsStateBrowse();
}
/**
 * 界面关联接口方法实现 -- 新增
 **/
public void doAddAction(ILinkAddData adddata) {
	SCMEnv.out("进入订单新增接口...");
	if(adddata == null){
		SCMEnv.out("上游调用采购订单新增界面时未正确传入参数数据，直接返回!");
		return;
	}
	//
	String strUpBillType = adddata.getSourceBillType();
	AggregatedValueObject[] vos = null;
	//来源于价格审批单
	if(adddata.getUserObject() != null
			&& adddata.getUserObject() instanceof PUMessageVO){
		
		PUMessageVO msgVo =  (PUMessageVO) adddata.getUserObject();
		
		//since v55:只有价格审批单时才做此设置：为支持行业传入其它可生成订单的其它单据
		if(msgVo.getAskvo() instanceof nc.vo.pp.ask.PriceauditMergeVO){
			strUpBillType = BillTypeConst.PO_PRICEAUDIT;
		}
		//
		vos = new AggregatedValueObject[]{msgVo.getAskvo()};
	}
	//来源于请购单:V5暂不支持
	else if(false && BillTypeConst.PO_PRAY.equals(strUpBillType)){
		//
		String strLoginCorp =  ClientEnvironment.getInstance().getCorporation().getPrimaryKey();
		String strOperator =  ClientEnvironment.getInstance().getUser().getPrimaryKey();
		//		
		Vector<NormalCondVO> m_vecNormalVO = new Vector<NormalCondVO>();
        NormalCondVO voNormalLoginCorp = new NormalCondVO();
        voNormalLoginCorp.setKey(OrderPubVO.NORMALVO_PK_CORP_LOGIN);
        voNormalLoginCorp.setValue(strLoginCorp);
        m_vecNormalVO.addElement(voNormalLoginCorp);

        NormalCondVO voNormalOper = new NormalCondVO();
        voNormalOper.setKey(OrderPubVO.NORMALVO_COPERATOR);
        voNormalOper.setValue(strOperator);
        m_vecNormalVO.addElement(voNormalOper);

        NormalCondVO voNormalSplitFlag = new NormalCondVO();
        voNormalSplitFlag.setKey(OrderPubVO.NORMALVO_SPLIT_FLAG);/*-=是否分单=-*/
        voNormalSplitFlag.setValue(UFBoolean.TRUE);
        m_vecNormalVO.addElement(voNormalSplitFlag);
        
        NormalCondVO[] voaNormal = m_vecNormalVO.toArray(new NormalCondVO[m_vecNormalVO.size()]);
                     
		ConditionVO voCond = new ConditionVO();
		voCond.setLogic(true);
		voCond.setFieldCode("po_praybill.cpraybillid");
		voCond.setFieldName("请单表头ID");/*-=notranslate=-*/
		voCond.setOperaCode("=");
		voCond.setOperaName("等于");/*-=notranslate=-*/
		voCond.setDataType(0);
		voCond.setValue(adddata.getSourceBillID());
		
		UFDate dateLogin = ClientEnvironment.getInstance().getBusinessDate();
		//
		try{
			vos = PraybillHelper.queryForOrderBill(voaNormal,new ConditionVO[]{voCond},dateLogin,null);
		}catch(Exception e){			
			SCMEnv.out(e);
			return;
		}
	}
	//来源于其它单据
	
	//调用统一处理	
	processAfterChange(strUpBillType, vos);
}
/**
 * 界面关联接口方法实现 -- 审批
 **/
public void doApproveAction(ILinkApproveData approvedata) {
	SCMEnv.out("进入订单审批接口...");
	//-------------消息中心
	//初始化
//	initMsgCenter() ;

	//没有符合条件的订单
	try {
		OrderVO	voOrder = OrderHelper.queryOrderVOByKey( approvedata.getBillID() )  ;
		getBufferVOManager().resetVOs( voOrder==null ? null : new	OrderVO[]{voOrder});
	} catch (Exception	e) {
		int		iBtnLen = getThisBtnManager().getBtnaMsgCenter().length ;
		for (int i = 0; i < iBtnLen; i++){
			getThisBtnManager().getBtnaMsgCenter()[i].setEnabled(false) ;
		}
		updateButtons();

		PuTool.outException(this,e) ;
		return ;
	}
	//设置按钮状态
	if (getBufferLength()==0) {
		MessageDialog.showHintDlg(this,NCLangRes.getInstance().getStrByID("SCMCOMMON","UPPSCMCommon-000270")/*@res "提示"*/,NCLangRes.getInstance().getStrByID("4004020201","UPP4004020201-000023")/*@res "没有符合查询条件的订单"*/) ;
		getPoCardPanel().addNew() ;

		int		iBtnLen = getThisBtnManager().getBtnaMsgCenter().length ;
		for (int i = 0; i < iBtnLen; i++){
			getThisBtnManager().getBtnaMsgCenter()[i].setEnabled(false) ;
		}
		updateButtons();
		return ;
	}
  
	//设置订单VO数组
	getBufferVOManager().setVOPos(0) ;
	//设置卡片界面数据
	OrderVO	voCur = getOrderViewVOAt( getBufferVOManager().getVOPos() ) ;
	//设置操作员
	PoPublicUIClass.setCuserId(new	OrderVO[]{voCur}) ;

	getPoCardPanel().displayCurVO(voCur, false) ;
	getPoCardPanel().setEnabled(false) ;

  //单据是否已经冻结,所有按钮不显示，直接返回
  boolean bFrozenFlag = false ;
  if ( voCur.getHeadVO().getForderstatus().compareTo(BillStatus.FREEZE)==0 ) {
    MessageDialog.showHintDlg(this,NCLangRes.getInstance().getStrByID("SCMCOMMON","UPPSCMCommon-000270")/*@res "提示"*/,NCLangRes.getInstance().getStrByID("SCMCOMMON","UPPSCMCommon-000384")/*@res "该单据已冻结！"*/) ;
    bFrozenFlag = true ;
  }
  if(bFrozenFlag){
    setButtonsNull();
    return;
  }
  //是否相同公司
  boolean bCorpSameFlag = getCorpPrimaryKey().equals(voCur.getPk_corp()) ;
  
  //设置按钮组及状态
  
  //相同公司，走单据浏览按钮逻辑
  if(bCorpSameFlag){
    setButtons(getThisBtnManager().getBtnTree(this).getButtonArray());
    
  }
  //不同公司，保持原有处理
  else{
    setButtons(getThisBtnManager().getBtnaMsgCenter());
    getThisBtnManager().getBtnaMsgCenter()[0].setEnabled( voCur.isCanBeAudited() ) ;
    getThisBtnManager().getBtnaMsgCenter()[1].setEnabled( voCur.isCanBeUnAudited() ) ;
    getBtnManager().btnBillLnkQry.setEnabled(true);
    getBtnManager().btnListDocManage.setEnabled(true);
    getBtnManager().btnBillStatusQry.setEnabled(true);
    
  }
  getBtnManager().btnBillMaintainRevise.setVisible(true);
  //不同公司审批按钮不可用
  setButtonsStateBrowse();
  getThisBtnManager().getBtnaMsgCenter()[1].setEnabled( voCur.isCanBeUnAudited() ) ;
  updateButtonsAll();
}
/**
 * 界面关联接口方法实现 -- 逐级联查 
 **/
public void doQueryAction(ILinkQueryData querydata) {
	SCMEnv.out("进入订单逐级联查接口...");

	String billID = querydata.getBillID(); 
	
	setCurPk_corp( PoPublicUIClass.getLoginPk_corp() );
	//加载卡片
	setLayout(new java.awt.BorderLayout());
	add(getPoCardPanel(),java.awt.BorderLayout.CENTER);

	//设置卡片界面下列字段显示：
	//BillData data = getPoCardPanel().getBillData();
	getPoCardPanel().getBodyItem("naccumarrvnum").setShow(true);
	getPoCardPanel().getBodyItem("naccumstorenum").setShow(true);
	getPoCardPanel().getBodyItem("naccuminvoicenum").setShow(true);

	//setCauditid(billID);

	//没有符合条件的订单
	try {
		getBufferVOManager().resetVOs(new OrderVO[]{OrderHelper.queryOrderVOByKey(billID)}) ;
	} catch (Exception	e) {
		SCMEnv.out(e);
		MessageDialog.showHintDlg(this,NCLangRes.getInstance().getStrByID("SCMCOMMON","UPPSCMCommon-000270")/*@res "提示"*/,OrderPubVO.returnHintMsgWhenNull(e.getMessage())) ;
	}

	if(getBufferLength()==0){
		MessageDialog.showHintDlg(this,NCLangRes.getInstance().getStrByID("SCMCOMMON","UPPSCMCommon-000270")/*@res "提示"*/,NCLangRes.getInstance().getStrByID("4004020201","UPP4004020201-000023")/*@res "没有符合查询条件的订单"*/) ;
		getPoCardPanel().addNew() ;
		return ;
	}
  //
  getBufferVOManager().setVOPos(0) ;
  //
  OrderVO vo = getOrderViewVOAt( getBufferVOManager().getVOPos() );
  //
  String strPkCorp = vo.getPk_corp();
  //按照单据所属公司加载查询模版 
  OrderUIQueDlg queryDlg = new OrderUIQueDlg(this, 
      getModuleCode(), 
      strPkCorp,
      getClientEnvironment().getUser().getPrimaryKey());
  
  //查询模板中没有公司时，要设置虚拟公司
  queryDlg.initCorpRefsDataPower(OrderUIQueDlg.CORPKEY, OrderUIQueDlg.getPowerCodes(), new String[]{strPkCorp} );
  
  //调用公共方法获取该公司中控制权限的档案条件VO数组
  ConditionVO[] voaPower = queryDlg.getDataPowerConVOs(strPkCorp,PoQueryCondition.getPowerCodesOrderUI());
  
  //组织查询条件VO
  ConditionVO[] voaUserDefined = null;
  ConditionVO voHid = new ConditionVO();
  voHid.setFieldCode("po_order.corderid");
  voHid.setFieldName("采购订单头ID");/*-=notranslate=-*/
  voHid.setOperaCode("=");
  voHid.setOperaName("等于");
  voHid.setValue(billID);
  if(voaPower == null || voaPower.length == 0){
    voaUserDefined = new ConditionVO[1];
    voaUserDefined[0] = voHid;
  }else{
    voaUserDefined = new ConditionVO[voaPower.length + 1];
    for(int i=0; i<voaPower.length; i++){
      voaUserDefined[i] = voaPower[i];
    }
    voaUserDefined[voaPower.length] = voHid;
  }
  //组织第二次查询单据，按照权限和单据PK过滤  
  NormalCondVO voNormal = new NormalCondVO();
  voNormal.setKey(OrderPubVO.NORMALVO_PK_CORP_QUERY);
  voNormal.setValue(strPkCorp);
  
  NormalCondVO voNormalClosed = new NormalCondVO();
  voNormalClosed.setKey(OrderPubVO.NORMALVO_INCLUDING_CLOSED);
  voNormalClosed.setValue("Y");
  
  OrderVO[] voaRet = null;
  try{
    voaRet = OrderHelper.queryOrderArrayOnlyHead(new NormalCondVO[] {voNormal, voNormalClosed}, voaUserDefined,getClientEnvironment().getUser().getPrimaryKey());
  }catch(Exception e){
    MessageDialog.showHintDlg(this, 
        NCLangRes.getInstance().getStrByID("SCMCOMMON","UPPSCMCommon-000270")/* "提示" */, 
        e.getMessage());
    setButtonsNull();
    return;
  }
  if(voaRet == null || voaRet.length <= 0 || voaRet[0] == null){
    MessageDialog.showHintDlg(this, NCLangRes.getInstance().getStrByID("SCMCOMMON","UPPSCMCommon-000270")/* "提示" */, 
        NCLangRes.getInstance().getStrByID("common", "SCMCOMMON000000161")/* "没有察看单据的权限" */);
    return;
  }
	//设置卡片界面数据
	getPoCardPanel().displayCurVOForLinkQry( vo , false) ;

	getPoCardPanel().setEnabled(false) ;

	//设置操作员
	PoPublicUIClass.setCuserId(
		new	OrderVO[]{
			getOrderViewVOAt(getBufferVOManager().getVOPos())
		}
	) ;
  //单据是否已经冻结,所有按钮不显示，直接返回
  boolean bFrozenFlag = false ;
  if ( vo.getHeadVO().getForderstatus().compareTo(BillStatus.FREEZE)==0 ) {
    MessageDialog.showHintDlg(this,NCLangRes.getInstance().getStrByID("SCMCOMMON","UPPSCMCommon-000270")/*@res "提示"*/,NCLangRes.getInstance().getStrByID("SCMCOMMON","UPPSCMCommon-000384")/*@res "该单据已冻结！"*/) ;
    bFrozenFlag = true ;
  }
  if(bFrozenFlag){
    setButtonsNull();
    return;
  }
  boolean bCorpSameFlag = getCorpPrimaryKey().equals(vo.getPk_corp()) ;
  //设置按钮组
  if(bCorpSameFlag){
    setButtons(getThisBtnManager().getBtnTree(this).getButtonArray());
    setButtonsStateBrowse();
  }else{
    setButtonsNull();
  }
}
/**
 * 清空当前界面按钮
 */
private void setButtonsNull(){
	ButtonObject[] objs = getButtons();
	int iLen = objs == null ? 0 : objs.length;
	for (int i = 0; i < iLen; i++) {
		if (objs[i] == null) {
			continue;
		}
		objs[i].setVisible(false);
		updateButton(objs[i]);
	}
}

/**
 * 存货明细 按钮响应事件
 * @author MeiChao
 * @date 2011-01-05
 */
  private void onBoInvDetail(){
// 	 if(this.bBillList){//||!this.m_strState.equals("到货浏览")
// 		 MessageDialog.showHintDlg(this,"提示","必须在卡片浏览状态下才可维护存货明细");
// 		 return;
// 	 }
  	if(this.m_nCurOperState!=0){//||!this.m_strState.equals("到货浏览")
		 MessageDialog.showHintDlg(this,"提示","必须在卡片浏览状态下才可维护存货明细");
		 return;
	 }
 	 if(m_nCurOperState==1||m_nCurOperState==2||m_nCurOperState==3){//单据处于新增,修改,作废状态下.
 		 MessageDialog.showHintDlg(this,"提示","必须在卡片浏览状态下才可维护存货明细");
		 return;
 	 }
 	 if(this.getPoCardPanel().getHeadItem("vdef20")!=null&&"Y".equals(this.getPoCardPanel().getHeadItem("vdef20").toString())){
 		 MessageDialog.showHintDlg(this,"提示","直调订单无需维护存货明细!");
 		 return;
 	 }
 	 if(this.getPoCardPanel().getBillTable("table").getSelectedRow()==-1){
 		 MessageDialog.showHintDlg(this,"提示","请选择一个表体存货行后再维护存货明细!");
 		 return;
 	 }
 	 //选中行号
 	 Integer selectedRowNum=this.getPoCardPanel().getBillTable("table").getSelectedRow();
 	 //选中行VO
 	 OrderItemVO selectedArriveBody=(OrderItemVO)this.getPoCardPanel().getBillModel().getBodyValueRowVO(selectedRowNum, OrderItemVO.class.getName());
 	 //选中存货
 	 String selectedInv=selectedArriveBody.getCbaseid();
 	 //选中存货对应的明细
 	 ArrayList<InvDetailVO> selectedDetailList=new ArrayList<InvDetailVO>();
 	 if(this.invDetailVOs!=null&&this.invDetailVOs.length>0){
 	 for(int i=0;i<this.invDetailVOs.length;i++){
 		 if(selectedInv.equals(this.invDetailVOs[i].getPk_invbasdoc())){//如
 			 selectedDetailList.add((InvDetailVO)this.invDetailVOs[i].clone());
 		 }
 	 }
 	 }
 	 InvDetailDialog421 invDetailDlg=null;
 	 if(selectedDetailList!=null&&selectedDetailList.size()>0){
 		 invDetailDlg=new InvDetailDialog421(this,(InvDetailVO[])selectedDetailList.toArray(new InvDetailVO[selectedDetailList.size()]),selectedArriveBody);
 	 }else{
 		 invDetailDlg=new InvDetailDialog421(this,null,selectedArriveBody);
 	 }
	  invDetailDlg.showModal();
	  //更新表体的存货明细页签等后续操作.
	  if(invDetailDlg.isCloseOK()){
		  //从Dialog中取出维护后的存货明细数组(此时取出的VO数组包含新增,修改,已删除 3种状态的VO,已删除看作是修改)
		  InvDetailVO[] newInvDetails=invDetailDlg.getinvDetailVOs();
		  //开始分拣维护后的存货明细VO
		  //新增的
		  ArrayList<InvDetailVO> newVOs=new ArrayList<InvDetailVO>();
		  //修改的
		  ArrayList<InvDetailVO> changeVOs=new ArrayList<InvDetailVO>();
		  for(int i=0;i<newInvDetails.length;i++){
			  if(newInvDetails[i].getStatus()==VOStatus.NEW&&newInvDetails[i].getPk_invdetail()==null){
				newVOs.add(newInvDetails[i]);//VO状态为新增  且没有主键的记录
			  }else if(newInvDetails[i].getStatus()==VOStatus.UPDATED&&newInvDetails[i].getPk_invdetail()!=null){
				changeVOs.add(newInvDetails[i]);//VO状态为修改,且有主键的记录
			  }
		  }
		  ISrvPUToMM iService=(ISrvPUToMM)NCLocator.getInstance().lookup(ISrvPUToMM.class.getName());
		  try {
			iService.insertInvDetail(newVOs.toArray(new InvDetailVO[newVOs.size()]));
			iService.updateInvDetail(changeVOs.toArray(new InvDetailVO[changeVOs.size()]));
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		  
	  }
	  //保存成功后需要更新当前页面的数据.
		String pk = getPoCardPanel().getHeadItem("corderid").getValue();
		IUAPQueryBS iService = (IUAPQueryBS) NCLocator.getInstance().lookup(
				IUAPQueryBS.class.getName());
		String queryInvDetailSql = "select * from scm_invdetail t where t.corder_bid in (select corder_bid from po_order_b where corderid='"
				+ pk + "') and t.dr=0 order by t.pk_invbasdoc";
		List<InvDetailVO> invDetailVOList = null;
		try {
			invDetailVOList = (List<InvDetailVO>) iService
					.executeQuery(queryInvDetailSql, new BeanListProcessor(
							InvDetailVO.class));
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (invDetailVOList != null && invDetailVOList.size() > 0) {
			this.invDetailVOs = new InvDetailVO[invDetailVOList.size()];
			for (int i = 0; i < invDetailVOList.size(); i++) {
				invDetailVOs[i] = invDetailVOList.get(i);
			}
			getPoCardPanel().getBillModel("invdetail").setBodyDataVO(
					invDetailVOs);
			getPoListPanel().getBodyBillModel("invdetail").setBodyDataVO(invDetailVOs);
			getPoCardPanel().getBillModel("invdetail").execLoadFormula();
			getPoListPanel().getBodyBillModel("invdetail").execLoadFormula();
		} else {
			getPoCardPanel().getBillModel("invdetail").setBodyDataVO(null);
			getPoListPanel().getBodyBillModel("invdetail").setBodyDataVO(null);
		}
  }





}