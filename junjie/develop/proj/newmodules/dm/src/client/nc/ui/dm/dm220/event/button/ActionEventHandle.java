package nc.ui.dm.dm220.event.button;

import java.awt.Color;
import java.awt.Container;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import nc.bs.framework.common.NCLocator;
import nc.bs.framework.exception.ComponentException;
import nc.bs.ml.NCLangResOnserver;
import nc.itf.arap.pub.IArapBillPublic;
import nc.itf.ia.bill.IBill;
import nc.itf.uap.IUAPQueryBS;
import nc.itf.uap.IVOPersistence;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.ui.dm.dm010.ShowDelivOrg;
import nc.ui.dm.dm220.ClientHelper;
import nc.ui.dm.dm220.DelivBillVirtualFiledLoader;
import nc.ui.dm.dm220.NodeContext;
import nc.ui.dm.dm220.event.CollectDlg;
import nc.ui.dm.dm220.print.PrintDataSource;
import nc.ui.dm.dm220.print.SplitPrintDataSource;
import nc.ui.dm.dm220.print.SplitPrintParametersHelper;
import nc.ui.dm.dm220.print.SplitPrintTool;
import nc.ui.dm.pub.ValidateFormulaExecuter;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UITable;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.bill.BillData;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillListPanel;
import nc.ui.pub.bill.BillModel;
import nc.ui.pub.bill.IBillItem;
import nc.ui.pub.print.PrintEntry;
import nc.ui.pub.query.QueryConditionClient;
import nc.ui.scm.pattern.buffer.IBillBuffer;
import nc.ui.scm.pattern.context.IUIContext;
import nc.ui.scm.pattern.ctrl.card.CardCtrl;
import nc.ui.scm.pattern.ctrl.card.CardStatus;
import nc.ui.scm.pattern.ctrl.card.auxiliary.CardHelper;
import nc.ui.scm.pattern.ctrl.list.ListCtrl;
import nc.ui.scm.pattern.ctrl.node.FunctionStatus;
import nc.ui.scm.pattern.ctrl.node.NodeStatus;
import nc.ui.scm.pattern.event.node.ActionEvent;
import nc.ui.scm.pattern.event.node.SwitchEvent;
import nc.ui.scm.pattern.listcard.ListCardNode;
import nc.ui.scm.print.IFreshTsListener;
import nc.ui.scm.print.ISaveSplitParams;
import nc.ui.scm.print.SplitParams;
import nc.ui.scm.print.SplitPrintParamDlg;
import nc.ui.scm.pub.panel.SetColor;
import nc.ui.scm.pub.report.OrientDialog;
import nc.ui.scm.sourcebill.SourceBillFlowDlg;
import nc.vo.dm.model.delivbill.entity.DelivBillHeadVO;
import nc.vo.dm.model.delivbill.entity.DelivBillItemVO;
import nc.vo.dm.model.delivbill.entity.DelivBillPackItemVO;
import nc.vo.dm.model.delivbill.entity.DelivBillVO;
import nc.vo.dm.model.delivbill.meta.DelivBillHeadVOMeta;
import nc.vo.dm.model.delivbill.meta.DelivBillItemVOMeta;
import nc.vo.dm.model.delivbill.meta.DelivBillPackItemVOMeta;
import nc.vo.ep.dj.DJZBHeaderVO;
import nc.vo.ep.dj.DJZBItemVO;
import nc.vo.ep.dj.DJZBVO;
import nc.vo.ia.bill.BillHeaderVO;
import nc.vo.ia.bill.BillItemVO;
import nc.vo.ia.bill.BillVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.para.SysInitException;
import nc.vo.querytemplate.TemplateInfo;
import nc.vo.scm.pattern.constant.pub.ValueConstant;
import nc.vo.scm.pattern.context.IParameter;
import nc.vo.scm.pattern.domain.scm.enumeration.FBillStatusFlag;
import nc.vo.scm.pattern.exception.ExceptionUtils;
import nc.vo.scm.pattern.exception.SCMBusinessException;
import nc.vo.scm.pattern.model.entity.bill.ISmartBill;
import nc.vo.scm.pattern.model.entity.vo.SmartVO;
import nc.vo.scm.pattern.pub.SqlBuilder;
import nc.vo.scm.print.PrintConst;
import nc.vo.scm.print.ScmPrintlogVO;
import nc.vo.scm.pub.ctrl.GenMsgCtrl;
import nc.vo.scm.pub.session.ClientLink;

/**
 * @author 钟鸣 2008-8-18 下午01:58:53
 */
public class ActionEventHandle {
  private PrintEntry m_print = null;

  private NewQuery m_QueryDLG = null;

  private PrintDataSource m_dataSource = null;

  private ListCardNode node = null;

  private NodeContext nodeContext = null;

  private SelfActionEventHandle handle = null;
  
  private SplitPrintParamDlg splitPrintDlg = null;
  public ActionEventHandle(
      ListCardNode node, NodeContext nodeContext) {
    this.node = node;
    this.nodeContext = nodeContext;
    this.handle = new SelfActionEventHandle(this.node, this.nodeContext);
  }

  public void doEvent(ActionEvent event) {
    String code = event.getButtonObject().getCode();

    if (code.equals("自制")) {
      this.onNew();
    }
    else if (code.equals("保存")) {
      this.onSave();
    }
    else if (code.equals("修改")) {
      this.onModify();
    }
    else if (code.equals("取消")) {
      this.onCancel();
    }
    else if (code.equals("删除")) {
      this.onDelete();
    }
    else if (code.equals("放弃转单")) {
      this.onCancleTransferBill();
    }
    else if (code.equals("增行")) {
      this.onAddLine();
    }
    else if (code.equals("删行")) {
      this.onDeleteLine();
    }
    else if (code.equals("插入行")) {
      this.onInsertLine();
    }
    else if (code.equals("复制行")) {
      this.onCopyLine();
    }
    else if (code.equals("粘贴行")) {
      this.onPasteLine();
    }
    else if (code.equals("粘贴行到表尾")) {
      this.onPasteLineToTail();
    }
    else if (code.equals("卡片编辑")) {
      this.onCardRowEdit();
    }
    else if (code.equals("重排行号")) {
      this.resetRowNo();
    }
    else if (code.equals("查询")) {
      this.onQuery();
    }
    else if (code.equals("刷新")) {
      this.onRefresh();
    }
    else if (code.equals("定位")) {
      this.onLocate();
    }
    else if (code.equals("首页")) {
      this.onFirst();
    }
    else if (code.equals("上页")) {
      this.onPrevious();
    }
    else if (code.equals("下页")) {
      this.onNext();
    }
    else if (code.equals("末页")) {
      this.onLast();
    }
    else if (code.equals("全选")) {
      this.onSelectAll();
    }
    else if (code.equals("全消")) {
      this.onUnSelectAll();
    }
    else if (code.equals("合并显示")) {
      this.onCombinView();
    }
    else if (code.equals("预览")) {
      this.onPrintPreview();
    }
    else if (code.equals("打印")) {
      this.onPrint();
    }
    else if (code.equals("分单打印")) {
      this.onSplitPrint();
    }
    else if (code.equals("联查")) {
      this.onRelationView();
    }
    else if(code.equals("费用暂估")){
    	estimateLoadFee();
    }else if (event.getButtonObject().isCheckboxGroup()) {
      event.getButtonObject().setSelected(true);
      this.nodeContext.setTransactionType(event.getButtonObject().getTag());
    }
    else {
      this.handle.doEvent(event);
    }
  }
  /**
   * 由销售出库生成的运输单生成费用暂估单
   * 根据运输单的费用信息,生成暂估应付单
   * @author lumzh
   * @param null
   * @since 2012-08-16 从产品代码中移出成为一个独立的方法.
   * @return boolean true:传应付成功 false: 一项或某项失败. 失败原因请参见异常信息.
   */
  protected boolean estimateLoadFee(){	  
	  DelivBillVO delivbillvo=(DelivBillVO) this.node.getCardPanel().getCard().getBill();
		List<DJZBVO> estimationTempVOs=new Vector<DJZBVO>();
	  if(delivbillvo==null){
			//如果无费用信息则,不做任何操作.
			return true;
		}else{
			/**
			 * 开始处理费用信息.
			 * 过滤费用信息,按客商分类.
			 */
			DelivBillHeadVO delivbillheadvo=delivbillvo.getHead();
			DelivBillItemVO[] delivbillitemvo=delivbillvo.getInvBodys();
			DelivBillPackItemVO[] delivbillpackitemvo=delivbillvo.getPackBodys();
			/**
			 * 开始组织暂估应付单VO
			 */
			ClientEnvironment ce=ClientEnvironment.getInstance();//初始化环境常量
			UFDouble nmoneytotal=new UFDouble(0);//运输单费用总金额
			
			if(delivbillheadvo.getAttributeValue("isestimate")!=null && delivbillheadvo.getAttributeValue("isestimate").toString().equals("Y"))return false;
			if(delivbillitemvo!=null && delivbillitemvo.length>0){
             for(int j=0;j<delivbillitemvo.length;j++){
            	 DelivBillItemVO delivbillitem= delivbillitemvo[j];
            	 nmoneytotal=nmoneytotal.add(delivbillitem.getNmoney()); 
             }
			}
			IUAPQueryBS query = NCLocator.getInstance().lookup(IUAPQueryBS.class);//实例化客户端查询接口

				DJZBVO oneAPVO=new DJZBVO();//实例化一个应付单VO
				DJZBHeaderVO head=new DJZBHeaderVO();//实例化一个暂估应付单表头VO.
				DJZBItemVO[] bodyVOs=new DJZBItemVO[1];//初始化表体VO数组

			
					DJZBItemVO body=new DJZBItemVO();//实例化一个暂估应付单表体VO
					body.setBbhl(new UFDouble(1.0));//本币汇率
					body.setBbye(nmoneytotal);//本币余额--无税金额
					body.setBilldate(new UFDate());//日期
					body.setBzbm("00010000000000000001");//币种编码--币种
					//body.setcheckflag 对账标记
					nc.vo.pub.para.SysInitVO DM015 = null;
					try {
						DM015 = nc.ui.pub.para.SysInitBO_Client.queryByParaCode(ce.getCorporation().getPk_corp(),
						"DM015");
					if(DM015!=null && DM015.getPkvalue()!=null){
						
						String dmsql="select pk_invbasdoc from bd_invmandoc where pk_invmandoc='"+DM015.getPkvalue()+"'";
						String pk_inbasdoc=null;
						try {//如果当前其他入库单的销售类型字段为空,那么开始查询通用收付流程的业务类型编码.
							pk_inbasdoc= (String) query.executeQuery(dmsql, new ColumnProcessor());
						} catch (BusinessException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							return false;
						}	
						body.setCinventoryid(pk_inbasdoc);
					}//存货基本档案ID--费用存货基本档案id
					} catch (BusinessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						return false;
					}   
					body.setCksqsh(delivbillitemvo[0].getCfirstbillitemid());//源头单据行id--费用信息表id
					body.setDdhh(delivbillitemvo[0].getCdelivbill_bid());//上层来源单据id--费用信息表id
					body.setDdlx(delivbillitemvo[0].getCdelivbill_hid());//上层来源单据id--其他入库单ID
					String deptsql="select cdptid from ic_general_h where cgeneralhid='"+delivbillitemvo[0].getCdelivbill_hid()+"'";
					String dept=null;
					try {//如果当前其他入库单的销售类型字段为空,那么开始查询通用收付流程的业务类型编码.
						dept= (String) query.executeQuery(deptsql, new ColumnProcessor());
					} catch (BusinessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						return false;
					}
					
					body.setDeptid(dept);//部门pk-其他入库单中的部门PK
					body.setDfbbje(nmoneytotal);//贷方本币金额--无税金额
					body.setDfbbsj(new UFDouble(new Double(0.0)));//贷方本币税金--0
					body.setDfbbwsje(nmoneytotal);//贷方本币无税金额--无税金额
				//	body.setDfshl(new UFDouble(oneExpense.getNnumber()));//贷方数量--数量
					body.setDfybje(nmoneytotal);//贷方原币金额--无税金额
					body.setDfybsj(new UFDouble(new Double(0.0)));//贷方原币税金--0
					body.setDfybwsje(nmoneytotal);//贷方原币无税金额--无税金额
				//	body.setDj(new UFDouble(oneExpense.getNoriginalcurprice()));//单价--单价
					body.setDjdl("yf");//单据大类--yf
					body.setDjlxbm("D1");//单据类型编码--D1
					body.setDr(0);
					body.setDwbm(ce.getCorporation().getPk_corp());//公司pk--当前登陆公司id
					body.setFbye(new UFDouble(new Double(0.0)));//辅币余额--0
					body.setFlbh(0);//分录编号--既行号
					body.setFx(-1);//方向
					body.setHbbm(delivbillitemvo[0].getCtakefeebasid());//伙伴编码--客商管理id
				//	body.setHsdj(new UFDouble(oneExpense.getNoriginalcurprice()));//含税单价--单价
					body.setIsSFKXYChanged(new UFBoolean(false));//收付款协议是否发生变化--N
					body.setIsverifyfinished(new UFBoolean(false));//是否核销完成--N
					body.setJsfsbm("4804");//上层来源单据类型--4A 其他入库单
					body.setKslb(1);//扣税类别--1
					body.setOld_flag(new UFBoolean(false));
					body.setOld_sys_flag(new UFBoolean(false));
					body.setPausetransact(new UFBoolean(false));//挂起标志--N
					body.setPh("4C");//源头单据类型--4A
					body.setpjdirection("none");//票据方向--none
					body.setQxrq(new UFDate());//起效日期--当前日期
					body.setSfbz("3");//收付标志--"3"
				//	body.setShlye(new UFDouble(oneExpense.getNnumber()));//数量余额--数量
					body.setSl(new UFDouble(new Double(0.0)));//税率--0
					body.setVerifyfinisheddate(new UFDate("3000-01-01"));//核销完成日期--默认3000-01-01
					body.setWldx(1);//往来对象标志--1
					body.setXgbh(-1);//并帐标志 ---   -1
					body.setXyzh(delivbillitemvo[0].getCfirstbillid());//源头单据id--其他入库单id
					body.setYbye(nmoneytotal);//原币余额--无税金额
					body.setYwbm("0001AA10000000006MFZ");//单据类型PK--固定0001AA10000000006MFZ
					String cbizid="select cbizid from ic_general_h where cgeneralhid='"+delivbillitemvo[0].getCdelivbill_hid()+"'";
					String busiid=null;
					try {//如果当前其他入库单的销售类型字段为空,那么开始查询通用收付流程的业务类型编码.
						busiid= (String) query.executeQuery(cbizid, new ColumnProcessor());
					} catch (BusinessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						return false;
					}
					
					body.setYwybm(busiid);//业务员PK--其他入库单业务员id
					
					/**
					 * 特殊标志: 自定义项18 19
					 */
					body.setZyx18("tureFree");//2010-11-07 "费用暂估应付"标志,启用于: 暂估处理,See:EstimateImpl 约9181行 用于生成采购发票时的处理
					body.setZyx19(body.getHbbm());//2010-11-07 "客商管理ID" 启用于: 暂估处理 ,See:EstimateImpl 约9423行 用于生成采购发票时的处理
					bodyVOs[0]=body;
				
				head.setBbje(new UFDouble(nmoneytotal));//本币金额--表体累加金额
				head.setDjdl("yf");//单据大类--yf
				head.setDjkjnd(ce.getAccountYear());//会计年度--当前系统的会计年度
				head.setDjkjqj(ce.getAccountMonth());//会计期间--当前系统会计期间
				head.setDjlxbm("D1");//单据类型编码--D1
				head.setDjrq(new UFDate());//单据日期--当前系统日期
				head.setDjzt(2);//单据状态--1 表示已保存 2表示已生效
				head.setDr(0);
				head.setDwbm(ce.getCorporation().getPk_corp());//单位编码--公司ID
				head.setEffectdate(new UFDate());//起效日期--当前系统日期
				head.setHzbz("-1");//坏账标志--  -1 表示不是坏账
				head.setIsjszxzf(new UFBoolean(false));//是否结算中心支付--否
				head.setIsnetready(new UFBoolean(false));//是否已经补录--否
				//head.setIspaid(new UFBoolean(false));//是否付款
				head.setIsreded(new UFBoolean(false));//是否红冲
				head.setIsselectedpay(1);//选择付款--1
				head.setLrr(ce.getUser().getPrimaryKey());//录入人--当前登陆用户id
				head.setLybz(4);//来源标志--4 表示系统生成, 1 表示自制
				head.setPrepay(new UFBoolean(false));//预收款标志--N
				head.setPzglh(1);//系统标志--1
				head.setQcbz(new UFBoolean(false));//期初标志--N
				head.setSpzt("1");//为空,表示未审批
				head.setShr(ce.getUser().getPrimaryKey());//审核人,当前登陆用户
				head.setShkjnd(ce.getAccountYear());//审核会计年度
				head.setShkjqj(ce.getAccountMonth());//审核会计期间
				head.setShrq(ce.getDate());//审核日期
				head.setSxbz(10);//生效标志--0表示未生效  10 表示已生效
				head.setSxkjnd(ce.getAccountYear());//生效会计年度
				head.setSxkjqj(ce.getAccountMonth());//生效会计期间
				head.setSxr(ce.getUser().getPrimaryKey());//生效人
				head.setSxrq(ce.getDate());//生效日期
				String queryBusitype="select t.pk_busitype from bd_busitype t where t.busicode='arap' and t.businame='收付通用流程'";
				Object busitype=null;
				try {//如果当前其他入库单的销售类型字段为空,那么开始查询通用收付流程的业务类型编码.
					busitype= query.executeQuery(queryBusitype, new ColumnProcessor());
				} catch (BusinessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return false;
				}
				head.setXslxbm(busitype==null?"00011110000000002RGT":busitype.toString());//销售类型编码--其他入库单的业务类型(业务流程)编码
				head.setYbje(new UFDouble(nmoneytotal));//本币金额--表体累加金额
				head.setYwbm("0001AA10000000006MFZ");//单据类型--默认0001AA10000000006MFZ
				head.setZgyf(1);//暂估应付标志--1表示暂估应付 0表示非暂估应付
				head.setZzzt(0);//支付状态--0
				head.setZyx20("Y");//2010-11-07  MeiChao 由于后续费用处理需要,加入此值,尚不明了其意义.
				
				oneAPVO.setParentVO(head);//加入表头
				oneAPVO.setChildrenVO(bodyVOs);//加入表体
				estimationTempVOs.add(oneAPVO);//将VO加入数组中.
		
			//获取应收应付的对外操作接口
			IArapBillPublic iARAP=(IArapBillPublic)NCLocator.getInstance().lookup(IArapBillPublic.class.getName());
			try {
				DJZBVO[] apVOs=new DJZBVO[estimationTempVOs.size()];
				iARAP.saveArapBills(estimationTempVOs.toArray(apVOs));
				writeBackOff(delivbillvo);
			} catch (BusinessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				MessageDialog.showErrorDlg(this.node.getCardPanel().getPanel(),"警告","传暂估应付失败!");
				return false;
			}
		}
	  return true;
  }
  //lumzh 2012 回写运输单状态:运输单传暂估应付成功后将，vdef1是否已暂估的值设置为Y
  private void writeBackOff(DelivBillVO delivbillheadvo){
		DelivBillVO delivbillheadvo1=ClientHelper.getInstance().writebackoffdelivbill(delivbillheadvo);
        this.onUpdate(delivbillheadvo1);
  }
  private void onNew() {
    NodeStatus status = this.node.getNodeStatus();
    if (status == NodeStatus.List) {
      SwitchEvent event = new SwitchEvent(this.node, NodeStatus.Card);
      this.node.fireSwitchEvent(event);
    }

    CardCtrl card = this.node.getCardPanel().getCard();
    
    CardHelper helper = new CardHelper(card);
    helper.addNewBill();

    // 设置运输组织
    String cdelivorgid = this.nodeContext.getCdelivorgid();
    helper.setHeadTailValue("cdelivorgid", cdelivorgid);
    String cdelivorgname = this.nodeContext.getCdelivorgname();
    helper.setHeadTailValue("cdelivorgname", cdelivorgname);

    // 设置运输类型
    String transactionType = this.nodeContext.getCurrentTransactionType();
    helper.setHeadTailValue("cdelivtype", transactionType);
    String transactionTypeName = this.nodeContext
        .getTransactionTypeName(transactionType);
    helper.setHeadTailValue("cdelivtypename", transactionTypeName);

    // 设置公司
    IUIContext context = this.node.getContext();
    String pk_corp = context.getPk_corp();
    helper.setHeadTailValue("pk_corp", pk_corp);
    String formula = "ccorpname->getColValue(bd_corp,unitname,pk_corp,pk_corp)";
    card.getBillCard().execHeadFormula(formula);

    // 设置运输时间
    helper.setHeadTailValue("ddelivdate", context.getDate());
    UFDateTime time = new UFDateTime(System.currentTimeMillis());
    helper.setHeadTailValue("tdelivtime", time.getUFTime());

    // 设置重量、体积单位
    IParameter parameter = context.getParameter(ValueConstant.GROUPCORP);
    // 集团客户化参数基本档案参数的“重量单位”
    String vweightunitname = parameter.getParameter("BD203");
    // 集团客户化参数基本档案参数的“存储单位”
    String vvolumnunitname = parameter.getParameter("BD201");
    helper.setHeadTailValue("vweightunitname", vweightunitname);
    helper.setHeadTailValue("vvolumnunitname", vvolumnunitname);

    helper.transferFocusToHead();

  }
  private void onCancel() {
    CardCtrl card = this.node.getCardPanel().getCard();
    CardHelper helper = new CardHelper(card);
    FunctionStatus functionStatus = this.node.getFunctionStatus();
    if (functionStatus == FunctionStatus.Common) {
     
      helper.cancelUpdate();

      IBillBuffer billBuffer = this.node.getCommonBillBuffer();
      int cursor = billBuffer.getCurrentRow();
      if (cursor != -1) {
        ISmartBill bill = billBuffer.getBill(cursor);
        helper.show(bill);
      }
      // 恢复装车录入的模板状态
      if (this.nodeContext.isLoadEditing()) {
        this.handle.getLoadEditEventHandle().cancelEdit();
      }
      // 清除掉收发货信息的拷贝内容
      this.handle.getCopyPasteOperator().reset();
    }
    else if (functionStatus == FunctionStatus.TransferBill) {
      String message = NCLangResOnserver.getInstance().getStrByID("40142020",
          "UPP40142020-000075")/* @res ""当前转单单据被取消后将从转单列表中被删除，是否确定要取消"" */;
      int value = MessageDialog.showOkCancelDlg(this.node.getAbstractUI(),
          null, message, UIDialog.ID_OK);
      if (value != UIDialog.ID_OK) {
        return;
      }
      // 将卡片上的单据信息清除掉。避免重复出现
      helper.show(null);
      IBillBuffer billBuffer = this.node.getTransferBillBuffer();
      int index = billBuffer.getCurrentRow();
      billBuffer.remove(index);
      int size = billBuffer.size();
      if (size == 0) {
        this.node.setFunctionStatus(FunctionStatus.Common);
      }
      else {
        billBuffer.setCurrentRow(0);
      }
      SwitchEvent switchEvent = new SwitchEvent(this.node, NodeStatus.List);
      this.node.fireSwitchEvent(switchEvent);
    }
    this.node.getAbstractUI().showHintMessage(
        NCLangResOnserver.getInstance().getStrByID("40142020",
            "UPP40142020-000076")/* @res "取消成功" */);
  }

  private void resetRowNo() {
    CardCtrl card = this.node.getCardPanel().getCard();
    CardHelper helper = new CardHelper(card);
    helper.resetRowNo();
    this.node.getAbstractUI().showHintMessage(
        NCLangResOnserver.getInstance().getStrByID("40142020",
            "UPP40142020-000077")/* @res "重排行号" */);
  }

  private void onCopyLine() {
    CardCtrl card = this.node.getCardPanel().getCard();
    CardHelper helper = new CardHelper(card);
    helper.copyLine();
  }

  private void onAddLine() {
    CardCtrl card = this.node.getCardPanel().getCard();
    CardHelper helper = new CardHelper(card);
    helper.addLine();
    this.node.getAbstractUI().showHintMessage(
        NCLangResOnserver.getInstance().getStrByID("40142020",
            "UPP40142020-000078")/* @res "增加一行分录" */);
  }

  private void onInsertLine() {
    CardCtrl card = this.node.getCardPanel().getCard();
    CardHelper helper = new CardHelper(card);
    helper.insertLine();
    this.node.getAbstractUI().showHintMessage(
        NCLangResOnserver.getInstance().getStrByID("40142020",
            "UPP40142020-000079")/* @res "插入一行分录" */);
  }

  private void onDeleteLine() {
    CardCtrl card = this.node.getCardPanel().getCard();
    CardHelper helper = new CardHelper(card);
    helper.deleteLine();
    this.node.getAbstractUI().showHintMessage(
        NCLangResOnserver.getInstance().getStrByID("40142020",
            "UPP40142020-000080")/* @res "删除一行分录" */);
  }

  private void onPasteLine() {
    CardCtrl card = this.node.getCardPanel().getCard();
    CardHelper helper = new CardHelper(card);
    helper.pasteLine();
    this.node.getAbstractUI().showHintMessage(
        NCLangResOnserver.getInstance().getStrByID("40142020",
            "UPP40142020-000081")/* @res "粘贴一行分录" */);
  }

  private void onPasteLineToTail() {
    CardCtrl card = this.node.getCardPanel().getCard();
    CardHelper helper = new CardHelper(card);
    helper.pasteLineToTail();
    this.node.getAbstractUI().showHintMessage(
        NCLangResOnserver.getInstance().getStrByID("40142020",
            "UPP40142020-000082")/* @res "粘贴行到末尾" */);
  }

  private void onRelationView() {
    IBillBuffer billBuffer = this.node.getCommonBillBuffer();
    int index = billBuffer.getCurrentRow();
    if (index == -1) {
      return;
    }
    DelivBillVO bill = (DelivBillVO) billBuffer.getBill(index);
    String cbillid = bill.getHead().getCdelivbill_hid();
    String cbilltypecode = "4804";
    String cuserid = this.node.getContext().getUserID();
    //String pk_corp = this.node.getContext().getPk_corp();
    String cbiztypeid = null;
    DelivBillHeadVO head = (DelivBillHeadVO) bill.getParentVO();
    SourceBillFlowDlg relationDlg = new SourceBillFlowDlg(this.node
        .getAbstractUI(), cbilltypecode, cbillid, cbiztypeid, cuserid, head
        .getVbillcode());
    relationDlg.showModal();
  }

  private void onCombinView() {
    NodeStatus nodeStatus = this.node.getNodeStatus();
    if (nodeStatus == NodeStatus.List) {
      SwitchEvent event = new SwitchEvent(this.node, NodeStatus.Card);
      this.node.fireSwitchEvent(event);
    }
    BillCardPanel bc = this.node.getCardPanel().getCard().getBillCard();
    String stablecode = bc.getCurrentBodyTableCode();
    Boolean flag = true;
    ArrayList<BillItem> list = new ArrayList<BillItem>();

    IBillBuffer buffer = this.node.getCommonBillBuffer();
    //BillListPanel listPanel = this.node.getListPanel().getList().getBillList();
    newPrintEntry();
    DelivBillVO bill = new DelivBillVO();
    DelivBillItemVO[] itemvo = null;
    DelivBillPackItemVO[] packvo = null;
    /*
     * if (nodeStatus == NodeStatus.List) { int[] selectedIndexs =
     * listPanel.getHeadTable().getSelectedRows();
     * if(selectedIndexs==null||selectedIndexs.length<=0){
     * this.node.getAbstractUI().showErrorMessage(NCLangResOnserver.getInstance().getStrByID(
     * "40142020", "UPP40142020-000083")@res "请选择合并的数据"); return; } bill =
     * (DelivBillVO) buffer.getBill(selectedIndexs[0]); } if (nodeStatus ==
     * NodeStatus.Card) {
     */
    int index = buffer.getCurrentRow();
    if (index == -1) {
      this.node.getAbstractUI().showErrorMessage(
          NCLangResOnserver.getInstance().getStrByID("40142020",
              "UPP40142020-000083")/* @res "请选择合并的数据" */);
      return;
    }
    bill = (DelivBillVO) buffer.getBill(index);
    // }
    BillItem[] item = null;
    // 判断当前的页签是货物信息/货物收发货信息，还是包装信息/包装收发货信息
    if (stablecode.equalsIgnoreCase("dm_delivbill_b")
        || stablecode.equalsIgnoreCase("invinfo")) {
      itemvo = bill.getInvBodys();
      if (itemvo == null || itemvo.length <= 0) {
        this.node.getAbstractUI().showErrorMessage(
            NCLangResOnserver.getInstance().getStrByID("40142020",
                "UPP40142020-0000824")/* @res "没有合并的数据" */);
        return;
      }
      item = bc.getBillModel("dm_delivbill_b").getBodyItems();
      BillItem[] item1 = bc.getBillModel("invinfo").getBodyItems();

      for (int i = 0, loop = item.length; i < loop; i++) {
        item[i].setShareTableCode("dm_delivbill_b");
        // if(item[i].isShow()){
        list.add(item[i]);
        // }
      }

      for (int i = 0, loop = item.length; i < loop; i++) {
        item1[i].setShareTableCode("invinfo");
        // item1[i].isShow()&&
        if (list.contains(item1[i]) && item1[i].isShow() && !item[i].isShow()) {
          list.get(i).setShow(item1[i].isShow());
        }
        if (!list.contains(item1[i])) {
          list.add(item1[i]);
        }
      }
    }
    else if (stablecode.equalsIgnoreCase("dm_packbill")
        || stablecode.equalsIgnoreCase("packinfo")) {
      packvo = bill.getPackBodys();
      if (packvo == null || packvo.length <= 0) {
        this.node.getAbstractUI().showErrorMessage(
            NCLangResOnserver.getInstance().getStrByID("40142020",
                "UPP40142020-000084")/* @res "没有合并的数据" */);
        return;
      }
      item = bc.getBillModel("dm_packbill").getBodyItems();
      BillItem[] item1 = bc.getBillModel("packinfo").getBodyItems();

      for (int i = 0, loop = item.length; i < loop; i++) {
        item[i].setShareTableCode("dm_packbill");
        list.add(item[i]);
      }

      for (int i = 0, loop = item.length; i < loop; i++) {
        item1[i].setShareTableCode("packinfo");
        if (list.contains(item1[i]) && item1[i].isShow() != item[i].isShow()) {
          list.get(i).setShow(item1[i].isShow());
        }
        if (!list.contains(item1[i])) {
          list.add(item1[i]);
        }
      }
      flag = false;
    }
    BillItem[] billitem = new BillItem[list.size()];
    for (int i = 0, loop = list.size(); i < loop; i++) {
      billitem[i] = list.get(i);
    }
    CollectDlg dlg = new CollectDlg(this.node.getAbstractUI(), "合并显示");
    // 固定分组列:运费承担单位、发货地点、收货地点、存货分类、存货编码、包装分类
    String[] fixGroupItems = new String[0];
    // 缺省分组列
    String[] defaultGroupItems = new String[0];
    String[] sumItems = new String[0];

    // 求平均列
    String[] avgItems = new String[0];
    // 求加权平均列
    String[] proAvgItems = new String[0];
    // 数量列
    String numItem = null;
    BillData data = bc.getBillData();
    if (flag) {
      dlg.initData(node, bc, billitem, fixGroupItems, defaultGroupItems, sumItems,
          avgItems, proAvgItems, numItem, null,
          (CircularlyAccessibleValueObject[]) itemvo, bill, data, flag);
    }
    else {
      dlg.initData(node, bc, billitem, fixGroupItems, defaultGroupItems, sumItems,
          avgItems, proAvgItems, numItem, null,
          (CircularlyAccessibleValueObject[]) packvo, bill, data, flag);
    }

    dlg.showModal();
    // 求和列

    /*
     * CollectDlg dlg = new CollectDlg(this.node.getAbstractUI(), "合并显示");
     * BillCardPanel bc = this.node.getCardPanel().getCard().getBillCard(); //
     * 固定分组列 String[] fixGroupItems = new String[0]; // 缺省分组列 String[]
     * defaultGroupItems = new String[0]; // 求和列 String[] sumItems = new
     * String[0]; // 求平均列 String[] avgItems = new String[0]; // 求加权平均列 String[]
     * proAvgItems = new String[0]; // 数量列 String numItem = null; //
     * 表体页签，用于多页签单据 String[] stablecode =
     * this.node.getCardPanel().getCard().getBillCard().getBillData().getTableCodes(IBillItem.BODY);//new
     * String[0]; dlg.initData(bc, fixGroupItems, defaultGroupItems, sumItems,
     * avgItems, proAvgItems, numItem, stablecode); dlg.showModal();
     */
  }

  private void onFirst() {
    IBillBuffer buffer = this.node.getCommonBillBuffer();
    int cursor = 0;
    buffer.setCurrentRow(cursor);
    ISmartBill bill = buffer.getBill(cursor);
    CardCtrl card = this.node.getCardPanel().getCard();
    CardHelper helper = new CardHelper(card);
    helper.show(bill);
  }

  private void onLast() {
    IBillBuffer buffer = this.node.getCommonBillBuffer();
    int cursor = buffer.size() - 1;
    buffer.setCurrentRow(cursor);
    ISmartBill bill = buffer.getBill(cursor);
    CardCtrl card = this.node.getCardPanel().getCard();
    CardHelper helper = new CardHelper(card);
    helper.show(bill);
  }

  private void onPrevious() {
    IBillBuffer buffer = this.node.getCommonBillBuffer();
    int cursor = buffer.getCurrentRow() - 1;
    buffer.setCurrentRow(cursor);
    ISmartBill bill = buffer.getBill(cursor);
    CardCtrl card = this.node.getCardPanel().getCard();
    CardHelper helper = new CardHelper(card);
    helper.show(bill);
  }

  private void onNext() {
    IBillBuffer buffer = this.node.getCommonBillBuffer();
    int cursor = buffer.getCurrentRow() + 1;
    buffer.setCurrentRow(cursor);
    ISmartBill bill = buffer.getBill(cursor);
    CardCtrl card = this.node.getCardPanel().getCard();
    CardHelper helper = new CardHelper(card);
    helper.show(bill);
  }

  private void onLocate() {
    NodeStatus nodeStatus = this.node.getNodeStatus();
    if (nodeStatus == NodeStatus.List) {
      ListCtrl list = this.node.getListPanel().getList();
      BillListPanel panel = list.getBillList();
      Container parent = this.node.getAbstractUI();
      BillModel billModel = panel.getBillListData().getHeadBillModel();
      BillItem[] items = panel.getBillListData().getHeadItems();
      UITable table = panel.getHeadTable();
      OrientDialog dialog = new OrientDialog(parent, billModel, items, table);
      dialog.showModal();
      Integer[] rows = dialog.getSelectOrientRows();
      if (rows != null) {
        panel.getHeadTable().setRowSelectionInterval(rows[0].intValue(),
            rows[0].intValue());
      }
      else {
        OrientDialog.clearOrientColor(panel.getHeadTable());
      }
      panel.updateUI();
    }
    else if (nodeStatus == NodeStatus.Card) {
      CardCtrl card = this.node.getCardPanel().getCard();
      BillCardPanel panel = card.getBillCard();
      Container parent = this.node.getAbstractUI();
      BillModel billModel = panel.getBillModel();
      BillItem[] items = panel.getBodyItems();
      String stablecode = panel.getCurrentBodyTableCode();
      //若是当前标签是包装行，需要得到包装行的所有item
      if (stablecode.equalsIgnoreCase("dm_packbill")
          || stablecode.equalsIgnoreCase("packinfo")) {
        items = panel.getBillModel("dm_packbill").getBodyItems();
      }
      for (BillItem item : items) {
        item.setShareTableCode(panel.getCurrentBodyTableCode());
      }

      UITable table = panel.getBillTable();
      OrientDialog dialog = new OrientDialog(parent, billModel, items, table);
      dialog.showModal();
      Integer[] rows = dialog.getSelectOrientRows();
      if (rows != null) {
        panel.getBillTable().setRowSelectionInterval(rows[0].intValue(),
            rows[0].intValue());
      }
      else {
        // OrientDialog.clearOrientColor(panel.getBillTable());
      }
    }
  }

  private void onRefresh() {
    IBillBuffer buffer = this.node.getCommonBillBuffer();
    buffer.clear();
    String sql = this.node.getQuery().getConditionSQL();
    ISmartBill[] bills = this.query(sql);
    NodeStatus nodeStatus = this.node.getNodeStatus();
    if (nodeStatus == NodeStatus.Card) {
      SwitchEvent event = new SwitchEvent(this.node, NodeStatus.List);
      this.node.fireSwitchEvent(event);
    }
    if (bills == null || bills.length == 0) {
      buffer.clear();
      this.node.getListPanel().getList().getListHelper().showBills(buffer, -1);
      this.node.getAbstractUI().showHintMessage(
          NCLangResOnserver.getInstance().getStrByID("40142020",
              "UPP40142020-000085")/* @res "没有查到您所需要的记录" */);
      return;
    }
    this.node.getAbstractUI().showHintMessage(
        NCLangResOnserver.getInstance().getStrByID("40142020",
            "UPP40142020-000086")/* @res "查到" */
            + bills.length
            + NCLangResOnserver.getInstance().getStrByID("40142020",
                "UPP40142020-000087")/* @res "张单据" */);
    for (ISmartBill bill : bills) {
      buffer.add(bill);
    }
    buffer.setCurrentRow(0);
    this.node.getListPanel().getList().getListHelper().showBills(buffer, 0);
  }

  private void onPrint() {
    NodeStatus nodeStatus = this.node.getNodeStatus();
    IBillBuffer buffer = this.node.getCommonBillBuffer();
    BillListPanel listPanel = this.node.getListPanel().getList().getBillList();
    newPrintEntry();
    DelivBillVO bill = new DelivBillVO();
    DelivBillHeadVO headerVO = new DelivBillHeadVO();
    if (nodeStatus == NodeStatus.List) {
      int[] selectedIndexs = listPanel.getHeadTable().getSelectedRows();
      if (selectedIndexs == null || selectedIndexs.length <= 0) {
        return;
      }
      bill = (DelivBillVO) buffer.getBill(selectedIndexs[0]);
      headerVO = (DelivBillHeadVO) bill.getParentVO();
    }
    if (nodeStatus == NodeStatus.Card) {
      int index = buffer.getCurrentRow();
      if (index == -1) {
        return;
      }
      bill = (DelivBillVO) buffer.getBill(index);
      headerVO = (DelivBillHeadVO) bill.getParentVO();
    }

    // 构造PringLogClient以及设置PrintInfo
    ScmPrintlogVO voSpl = new ScmPrintlogVO();
    voSpl = new ScmPrintlogVO();
    voSpl.setCbillid(headerVO.getCdelivbill_hid()); // 单据主表的ID
    voSpl.setTs(headerVO.getTs().toString()); // 设置主表时间戳
    voSpl.setVbillcode(headerVO.getVbillcode());// 传入运输单号，用于显示。
    voSpl.setCbilltypecode("4804");
    voSpl.setCoperatorid(ClientEnvironment.getInstance().getUser()
        .getPrimaryKey());
    voSpl.setIoperatetype(new Integer(PrintConst.PAT_OK));// 固定
    voSpl.setPk_corp(ClientEnvironment.getInstance().getCorporation()
        .getPrimaryKey());

    nc.ui.scm.print.PrintLogClient plc = new nc.ui.scm.print.PrintLogClient();
    plc.setPrintEntry(getPrintEntry());
    // 设置单据信息
    plc.setPrintInfo(voSpl);
    
    final DelivBillVO delivBillVO = bill;
    plc.addFreshTsListener(
        new IFreshTsListener() {
          public void freshTs(String sBillID, String sTS, Integer iPrintCount) {     
            delivBillVO.getHead().setAttributeValue("ts", sTS);
            delivBillVO.getHead().setAttributeValue("iprintcount", iPrintCount);

            BillCardPanel cardPanel = ActionEventHandle.this.node.getCardPanel()
                .getCard().getBillCard();
            BillListPanel listPanel = ActionEventHandle.this.node.getListPanel()
                .getList().getBillList();
            NodeStatus nodeStatus = ActionEventHandle.this.node.getNodeStatus();
            if (nodeStatus == NodeStatus.Card) {
              cardPanel.getTailItem("iprintcount").setValue(iPrintCount);
            }
            else {
              listPanel.getHeadBillModel().setValueAt(iPrintCount, 
                  ActionEventHandle.this.node.getCommonBillBuffer()
                  .getCurrentRow(), "iprintcount");
            }
          }
        }
    );

    // 设置打印监听
    getPrintEntry().setPrintListener(plc);

    // 打印
    getDataSource().setBillVO(bill);
    getPrintEntry().setDataSource(getDataSource());
    getPrintEntry().print();
  }

  private void onPrintPreview() {
    NodeStatus nodeStatus = this.node.getNodeStatus();
    //ArrayList<AggregatedValueObject> list = new ArrayList<AggregatedValueObject>();
    IBillBuffer buffer = this.node.getCommonBillBuffer();
    //BillCardPanel cardPanel = this.node.getCardPanel().getCard().getBillCard();
    BillListPanel listPanel = this.node.getListPanel().getList().getBillList();
    //String cbilltypecode = null;
    newPrintEntry();
    DelivBillVO bill = new DelivBillVO();
    DelivBillHeadVO headerVO = new DelivBillHeadVO();
    if (nodeStatus == NodeStatus.List) {

      int[] selectedIndexs = listPanel.getHeadTable().getSelectedRows();
      bill = (DelivBillVO) buffer.getBill(selectedIndexs[0]);
      headerVO = (DelivBillHeadVO) bill.getParentVO();
    }
    if (nodeStatus == NodeStatus.Card) {
      int index = buffer.getCurrentRow();
      if (index == -1) {
        return;
      }
      bill = (DelivBillVO) buffer.getBill(index);
      headerVO = (DelivBillHeadVO) bill.getParentVO();
    }

    // 构造PringLogClient以及设置PrintInfo
    ScmPrintlogVO voSpl = new ScmPrintlogVO();
    voSpl = new ScmPrintlogVO();
    voSpl.setCbillid(headerVO.getCdelivbill_hid()); // 单据主表的ID
    voSpl.setTs(headerVO.getTs().toString()); // 设置主表时间戳
    voSpl.setVbillcode(headerVO.getVbillcode());// 传入运输单号，用于显示。
    voSpl.setCbilltypecode("4804");
    voSpl.setCoperatorid(ClientEnvironment.getInstance().getUser()
        .getPrimaryKey());
    voSpl.setIoperatetype(new Integer(PrintConst.PAT_OK));// 固定
    voSpl.setPk_corp(ClientEnvironment.getInstance().getCorporation()
        .getPrimaryKey());

    nc.ui.scm.print.PrintLogClient plc = new nc.ui.scm.print.PrintLogClient();
    plc.setPrintEntry(getPrintEntry());
    // 设置单据信息
    plc.setPrintInfo(voSpl);

    // 设置打印监听
    getPrintEntry().setPrintListener(plc);

    // 打印预览
    getDataSource().setBillVO(bill);
    getPrintEntry().setDataSource(getDataSource());
    getPrintEntry().preview();

    /*
     * for (ISmartBill bill : bills) { if (bill instanceof
     * AggregatedValueObject) { list.add((AggregatedValueObject) bill); } else {
     * list.add(new TraditionalBillVO(bill)); } } try { BillPrintTool tool =
     * this.createBillPrintTool(list); tool.onBatchPrintPreview(listPanel,
     * cbilltypecode); } catch (BusinessException ex) {
     * ExceptionUtils.getInstance().wrappException(ex); } catch
     * (InstantiationException ex) {
     * ExceptionUtils.getInstance().wrappException(ex); } catch
     * (IllegalAccessException ex) {
     * ExceptionUtils.getInstance().wrappException(ex); } catch
     * (InterruptedException ex) {
     * ExceptionUtils.getInstance().wrappException(ex); }
     */
  }
  
  /**
   * 分单打印
   */
  private void onSplitPrint() {
    if (getSplitPrintDlg().showModal() != QueryConditionClient.ID_OK) {
      return;
    }

    NodeStatus nodeStatus = this.node.getNodeStatus();
    IBillBuffer buffer = this.node.getCommonBillBuffer();
    BillCardPanel cardPanel = this.node.getCardPanel().getCard().getBillCard();
    BillListPanel listPanel = this.node.getListPanel().getList().getBillList();
    newPrintEntry();
    DelivBillVO bill = null;
    if (nodeStatus == NodeStatus.List) {
      int[] selectedIndexs = listPanel.getHeadTable().getSelectedRows();
      if (selectedIndexs == null || selectedIndexs.length <= 0) {
        return;
      }
      bill = (DelivBillVO) buffer.getBill(selectedIndexs[0]);
    }
    if (nodeStatus == NodeStatus.Card) {
      int index = buffer.getCurrentRow();
      if (index == -1) {
        return;
      }
      bill = (DelivBillVO) buffer.getBill(index);
    }
    
    ArrayList<DelivBillVO> alPrintVO = new ArrayList<DelivBillVO>( 1 );
    alPrintVO.add( bill );
    try {
      SplitPrintTool printTool = new SplitPrintTool(this.node.getUIPanel(),
          "40142020", alPrintVO, cardPanel.getBillData(), 
          SplitPrintDataSource.class, 
          ClientEnvironment.getInstance().getCorporation().getPrimaryKey(), 
          ClientEnvironment.getInstance().getUser().getPrimaryKey(), 
          "vbillcode", "cdelivbill_hid");
      printTool.onSplitCardPrintPreview(cardPanel, listPanel, "4804", 
          getSplitPrintDlg().getSplitParams());
    }
    catch (Exception e) {
      GenMsgCtrl.printErr(e.getMessage());
    }
  }
  
  private SplitPrintParamDlg getSplitPrintDlg() {
    if (splitPrintDlg == null) {
      SplitPrintParametersHelper helper = SplitPrintParametersHelper
          .getInstance();
      SplitParams[] paramvos = helper.getSplitPrintParameters();
      ISaveSplitParams paraSaveImpl = helper.getSaveSplitParasImpl();
      splitPrintDlg = new SplitPrintParamDlg(this.node.getAbstractUI(), 
          paramvos, paraSaveImpl);
    }

    return splitPrintDlg;
  }

  private PrintDataSource getDataSource() {
//    NodeStatus nodeStatus = this.node.getNodeStatus();
//    IBillBuffer buffer = this.node.getCommonBillBuffer();
    BillCardPanel cardPanel = this.node.getCardPanel().getCard().getBillCard();
//    BillListPanel listPanel = this.node.getListPanel().getList().getBillList();
//    DelivBillVO bill = new DelivBillVO();
//    if (nodeStatus == NodeStatus.List) {
//
//      int[] selectedIndexs = listPanel.getHeadTable().getSelectedRows();
//      bill = (DelivBillVO) buffer.getBill(selectedIndexs[0]);
//    }
//    if (nodeStatus == NodeStatus.Card) {
//      int index = buffer.getCurrentRow();
//      bill = (DelivBillVO) buffer.getBill(index);
//    }
//    int[] selectedIndexs = listPanel.getHeadTable().getSelectedRows();
    
    if (null == m_dataSource) {
      m_dataSource = new PrintDataSource();
      BillData bd = cardPanel.getBillData();
      m_dataSource.setBillData(bd);
      m_dataSource.setTotalLinesInOnePage(getPrintEntry().getBreakPos());
    }
    return m_dataSource;
  }

  private nc.ui.pub.print.PrintEntry getPrintEntry() {
    if (null == m_print) {
      m_print = new nc.ui.pub.print.PrintEntry(null, null);
      m_print.setTemplateID(ClientEnvironment.getInstance().getCorporation()
          .getPrimaryKey(), this.node.getNodeInfo().getNodeCode(),
          ClientEnvironment.getInstance().getUser().getPrimaryKey(), null);
    }
    return m_print;
  }

  private void newPrintEntry() {
    m_print = new PrintEntry(null, null);

    m_print.setTemplateID(ClientEnvironment.getInstance().getCorporation()
        .getPrimaryKey(), this.node.getNodeInfo().getNodeCode(),
        ClientEnvironment.getInstance().getUser().getPrimaryKey(), null);
  }

/*  private BillPrintTool createBillPrintTool(
      ArrayList<AggregatedValueObject> list) {
    String nodecode = null;
    BillData bd = null;
    Class specialDataSource = null;
    String pk_corp = null;
    String cuserid = null;
    String vbillcodeName = null;
    String cbillidName = null;
    BillPrintTool tool = null;
    try {
      tool = new BillPrintTool(nodecode, list, bd, specialDataSource, pk_corp,
          cuserid, vbillcodeName, cbillidName);
    }
    catch (BusinessException ex) {
      ExceptionUtils.getInstance().wrappException(ex);
    }
    catch (InstantiationException ex) {
      ExceptionUtils.getInstance().wrappException(ex);
    }
    catch (IllegalAccessException ex) {
      ExceptionUtils.getInstance().wrappException(ex);
    }
    return tool;
  }*/

  private NewQuery getQueryCondDlg() {
    if (m_QueryDLG == null) {
      TemplateInfo tempinfo = new TemplateInfo();
      tempinfo.setPk_Org(this.node.getContext().getPk_corp());
      tempinfo.setCurrentCorpPk(this.node.getContext().getPk_corp());
      tempinfo.setFunNode(this.node.getNodeInfo().getNodeCode());
      tempinfo.setUserid(this.node.getContext().getUserID());
      m_QueryDLG = new NewQuery(this.node.getAbstractUI(), null, tempinfo);
    }

    return m_QueryDLG;
  }

  private void onQuery() {
    int ret = getQueryCondDlg().showModal();
    if (ret != UIDialog.ID_OK) {
      return;
    }
    IBillBuffer buffer = this.node.getCommonBillBuffer();
    buffer.clear();
    ArrayList list = getQueryCondDlg().getExtendQryCond();
    String extendqrysql = (String) list.get(0);
    String desendate = (String) list.get(1);
    String sql = getQueryCondDlg().getWhereSQL();
    String sqlwhere = null;

    /*
     * if(sql==null&&extendqrysql==null){ SqlBuilder buf = new SqlBuilder();
     * buf.append("select cdelivbill_hid from dm_delivbill where dr=0 ");
     * buf.append(" and cdelivorgid", ShowDelivOrg.getDelivOrgPK()); sqlwhere =
     * buf.toString(); } else
     */if (sql == null && extendqrysql != null) {
      sqlwhere = extendqrysql;
    }
    else if (sql != null && extendqrysql == null) {
      sqlwhere = sql;
    }
    else if (sql != null && extendqrysql != null) {
      sqlwhere = sql + "and" + extendqrysql;
    }

    sqlwhere = queryaction(sqlwhere, desendate);
    this.node.getQuery().setConstionSQL(sqlwhere);

    ISmartBill[] bills = this.query(sqlwhere);
    NodeStatus nodeStatus = this.node.getNodeStatus();
    if (nodeStatus == NodeStatus.Card) {
      SwitchEvent event = new SwitchEvent(this.node, NodeStatus.List);
      this.node.fireSwitchEvent(event);
    }
    if (bills == null || bills.length == 0) {
      buffer.clear();
      this.node.getListPanel().getList().getListHelper().showBills(buffer, -1);
      this.node.getAbstractUI().showHintMessage(
          NCLangResOnserver.getInstance().getStrByID("40142020",
              "UPP40142020-000085")/* @res "没有查到您所需要的记录" */);
      return;
    }
    this.node.getAbstractUI().showHintMessage(
        NCLangResOnserver.getInstance().getStrByID("40142020",
            "UPP40142020-000086")/* @res "查到" */
            + bills.length
            + NCLangResOnserver.getInstance().getStrByID("40142020",
                "UPP40142020-000087")/* @res "张单据" */);
    for (ISmartBill bill : bills) {
      buffer.add(bill);
    }
    buffer.setCurrentRow(0);
    this.node.getListPanel().getList().getListHelper().showBills(buffer, 0);

  }

  /**
   * 方法功能描述：通过查询条件进行拼接sql语句 <b>参数说明</b>
   * 
   * @param sqlwhere
   * @return
   * @author liyu
   * @time 2008-11-12 下午05:04:35
   */

  private String queryaction(String sqlwhere, String desendate) {
    String sql = null;
    SqlBuilder buf = new SqlBuilder();
    int index = 0;
    if (desendate != null) {
      index = desendate.indexOf("dsenddate");
    }
    // 若条件全部都为空
    if (sqlwhere == null && desendate == null) {
      buf
          .append("select distinct cdelivbill_hid from dm_delivbill where dr=0 ");
      buf.append(" and cdelivorgid", ShowDelivOrg.getDelivOrgPK());
      sql = buf.toString();
      return sql;
    }
    // 若其余条件都为空，发货日期不为空，则分别查询运输单主表--货物表和运输单主表--包装表之后做并集
    if (sqlwhere == null && desendate != null) {
      buf
          .append("select distinct dm_delivbill.cdelivbill_hid from dm_delivbill,dm_delivbill_b where dm_delivbill.dr=0  and dm_delivbill.cdelivbill_hid = dm_delivbill_b.cdelivbill_hid ");
      buf.append(" and dm_delivbill.cdelivorgid", ShowDelivOrg.getDelivOrgPK());
      buf.append(desendate.substring(0, index));
      buf.append("dm_delivbill_b.");
      buf.append(desendate.substring(index, desendate.length()));
      buf.append(" union ");
      buf
          .append("select distinct dm_delivbill.cdelivbill_hid from dm_delivbill,dm_packbill where dm_delivbill.dr=0  and dm_delivbill.cdelivbill_hid = dm_packbill.cdelivbill_hid ");
      buf.append(" and dm_delivbill.cdelivorgid", ShowDelivOrg.getDelivOrgPK());
      buf.append(desendate.substring(0, index));
      buf.append("dm_packbill.");
      buf.append(desendate.substring(index, desendate.length()));
      sql = buf.toString();
      return sql;
    }
    int deitemindex = sqlwhere.indexOf("dm_delivbill_b");
    int packitemindex = sqlwhere.indexOf("dm_packbill");
    if (deitemindex == -1 && packitemindex == -1) {
      if (desendate == null) {
        buf
            .append("select distinct cdelivbill_hid from dm_delivbill where dr=0 ");
        buf.append(" and cdelivorgid", ShowDelivOrg.getDelivOrgPK());
        buf.append(" and ");
        buf.append(sqlwhere);
      }
      else {
        buf
            .append("select distinct dm_delivbill.cdelivbill_hid from dm_delivbill,dm_delivbill_b where dm_delivbill.dr=0  and dm_delivbill.cdelivbill_hid = dm_delivbill_b.cdelivbill_hid ");
        buf.append(" and dm_delivbill.cdelivorgid", ShowDelivOrg
            .getDelivOrgPK());
        buf.append(" and ");
        buf.append(sqlwhere);
        buf.append(desendate.substring(0, index));
        buf.append("dm_delivbill_b.");
        buf.append(desendate.substring(index, desendate.length()));
        buf.append(" union ");
        buf
            .append("select distinct dm_delivbill.cdelivbill_hid from dm_delivbill,dm_packbill where dm_delivbill.dr=0  and dm_delivbill.cdelivbill_hid = dm_packbill.cdelivbill_hid ");
        buf.append(" and dm_delivbill.cdelivorgid", ShowDelivOrg
            .getDelivOrgPK());
        buf.append(" and ");
        buf.append(sqlwhere);
        buf.append(desendate.substring(0, index));
        buf.append("dm_packbill.");
        buf.append(desendate.substring(index, desendate.length()));
      }
      sql = buf.toString();
    }
    else if (deitemindex != -1 && packitemindex == -1) {
      buf.append(" select distinct dm_delivbill.cdelivbill_hid from dm_delivbill ");
      buf.append(" left join dm_delivbill_b on dm_delivbill_b.cdelivbill_hid = dm_delivbill.cdelivbill_hid ");
      buf.append(" where dm_delivbill.dr=0 ");
      buf.append(" and dm_delivbill.cdelivorgid", ShowDelivOrg.getDelivOrgPK());
      buf.append(" and ");
      buf.append(sqlwhere);
      if (index != 0) {
        buf.append(desendate.substring(0, index));
        buf.append("dm_delivbill_b.");
        buf.append(desendate.substring(index, desendate.length()));
      }
      sql = buf.toString();
    }
    else if (deitemindex == -1 && packitemindex != -1) {
      buf.append(" select distinct dm_delivbill.cdelivbill_hid from dm_delivbill ");
      buf.append(" left join dm_packbill on dm_packbill.cdelivbill_hid = dm_delivbill.cdelivbill_hid ");
      buf.append(" where dm_delivbill.dr=0 ");
      buf.append(" and dm_delivbill.cdelivorgid", ShowDelivOrg.getDelivOrgPK());
      buf.append(" and ");
      buf.append(sqlwhere);
      sql = buf.toString();
      if (index != 0) {
        buf.append(desendate.substring(0, index));
        buf.append("dm_packbill.");
        buf.append(desendate.substring(index, desendate.length()));
      }
    }
    else if (deitemindex != -1 && packitemindex != -1) {
      buf.append(" select distinct dm_delivbill.cdelivbill_hid ");
      buf.append(" from dm_delivbill ");
      buf.append(" left join dm_delivbill_b on dm_delivbill_b.cdelivbill_hid = dm_delivbill.cdelivbill_hid ");
      buf.append(" left join dm_packbill on dm_packbill.cdelivbill_hid = dm_delivbill.cdelivbill_hid ");
      buf.append(" where dm_delivbill.dr=0 ");
      buf.append(" and dm_delivbill.cdelivorgid", ShowDelivOrg.getDelivOrgPK());
      buf.append(" and ");
      buf.append(sqlwhere);
      if (index != 0) {
        buf.append(desendate.substring(0, index));
        buf.append("dm_packbill.");
        buf.append(desendate.substring(index, desendate.length()));
        buf.append(desendate.substring(0, index));
        buf.append("dm_delivbill_b.");
        buf.append(desendate.substring(index, desendate.length()));
      }
      sql = buf.toString();
    }
    return sql;
  }
  private ISmartBill[] query(String sql) {
    DelivBillVO[] bills = ClientHelper.getInstance().queryDelivBill(sql);
    DelivBillVirtualFiledLoader loader = new DelivBillVirtualFiledLoader(
        this.nodeContext);
    loader.setCardCtrl(this.node.getCardPanel().getCard());
    loader.load(bills);
    return bills;
  }
  
  private void fillDelivOrgInfo(DelivBillVO bill) {
    DelivBillHeadVO head = bill.getHead();
    String delivOrgId = head.getCdelivorgid();
    DelivBillItemVO[] invs = bill.getInvBodys();
    DelivBillPackItemVO[] pkgs = bill.getPackBodys();
    for (DelivBillItemVO inv : invs) {
      inv.setCdelivorgid(delivOrgId);
    }
    for (DelivBillPackItemVO pkg : pkgs) {
      pkg.setCdelivorgid(delivOrgId);
    }
  }

  private void onSave() {
    if (this.nodeContext.isLoadEditing()) {
      this.handle.getLoadEditEventHandle().endEdit();
      return;
    }
    CardCtrl card = this.node.getCardPanel().getCard();
    
    ValidateFormulaExecuter executor = new ValidateFormulaExecuter(card
        .getBillCard());
    if (!executor.execute())
      return;
    
    CardHelper helper = new CardHelper(card);
    
    DelivBillVO srcBill = (DelivBillVO) helper.getBill();
    fillDelivOrgInfo(srcBill);
    boolean updateFlag = true;
    if (srcBill.getPrimaryKey()== null) {
      updateFlag = false;
    }
    DelivBillVO bill = null;
    try{
      bill = ClientHelper.getInstance().saveDelivBill(srcBill);
    }
    catch(RuntimeException ex){
      Throwable th = ExceptionUtils.getInstance().unmarsh(ex);
      if( !(th instanceof SCMBusinessException )){
        throw ex;
      }
      SCMBusinessException e = (SCMBusinessException) th;
      SmartVO billvo = e.getBillVO();
      ArrayList<Integer> selectedRow = e.getSelectedRow();
      if(selectedRow!=null&&selectedRow.size()>0&& billvo!=null){
        String voname = billvo.getEntityName();
        if(voname.equalsIgnoreCase(DelivBillItemVOMeta.class.getName())){
          SetColor.setRowColor(card.getBillCard().getBodyPanel("dm_delivbill_b"), selectedRow, Color.YELLOW);
        }
        if(voname.equalsIgnoreCase(DelivBillPackItemVOMeta.class.getName())){
          SetColor.setRowColor(card.getBillCard().getBodyPanel("dm_packbill"), selectedRow, Color.YELLOW);
        }
      }
      ExceptionUtils.getInstance().wrappException(ex);
    }
    DelivBillVirtualFiledLoader loader = new DelivBillVirtualFiledLoader(
        this.nodeContext);
    loader.setCardCtrl(this.node.getCardPanel().getCard());
    loader.load(new DelivBillVO[] {srcBill}, new DelivBillVO[] {bill});
    FunctionStatus functionStatus = this.node.getFunctionStatus();
    if (functionStatus == FunctionStatus.Common) {
      if (updateFlag) {
        this.onUpdate(bill);
      }
      else {
        this.onInsert(bill);
      }
    }
    else if (functionStatus == FunctionStatus.TransferBill) {
      IBillBuffer billBuffer = this.node.getTransferBillBuffer();
      int index = billBuffer.getCurrentRow();
      billBuffer.remove(index);
      int size = billBuffer.size();
      if (size == 0) {
        this.node.setFunctionStatus(FunctionStatus.Common);
      }
      else {
        billBuffer.setCurrentRow(0);
      }
      billBuffer = this.node.getCommonBillBuffer();
      billBuffer.add(bill);
      size = billBuffer.size();
      // 设置当前的游标到最后一张单据
      billBuffer.setCurrentRow(size - 1);
      SwitchEvent switchEvent = new SwitchEvent(this.node, NodeStatus.List);
      this.node.fireSwitchEvent(switchEvent);
    }
    // 清除掉收发货信息的拷贝内容
    this.handle.getCopyPasteOperator().reset();
  }

  private void onInsert(ISmartBill bill) {
  	CardCtrl card = this.node.getCardPanel().getCard();
  	CardHelper helper = new CardHelper(card);
  	helper.show(bill);
  	helper.endUpdate();
    IBillBuffer buffer = this.node.getCommonBillBuffer();
    buffer.add(bill);
    int size = buffer.size();
    buffer.setCurrentRow(size - 1);
  }

  private void onUpdate(ISmartBill bill) {
    CardCtrl card = this.node.getCardPanel().getCard();
    CardHelper helper = new CardHelper(card);
    helper.show(bill);
    helper.endUpdate();
    IBillBuffer buffer = this.node.getCommonBillBuffer();
    int cursor = buffer.getCurrentRow();
    buffer.setBill(cursor, bill);
  }

  private void onDelete() {
    NodeStatus nodeStatus = this.node.getNodeStatus();
    if (nodeStatus == NodeStatus.List) {
      this.deleteForList();
    }
    else if (nodeStatus == NodeStatus.Card) {
      this.deleteForCard();
    }
  }

  private void deleteForList() {
    IBillBuffer buffer = this.node.getCommonBillBuffer();
    BillListPanel list = this.node.getListPanel().getList().getBillList();
    int[] rows = list.getHeadTable().getSelectedRows();
    DelivBillVO[] bills = (DelivBillVO[]) buffer.getBills(rows);

    int length = rows.length;
    List<Integer> rowList = new ArrayList<Integer>();
    List<DelivBillVO> billList = new ArrayList<DelivBillVO>();
    for (int i = 0; i < length; i++) {
      FBillStatusFlag status = bills[i].getHead().getFstatusflag();
      if (status == FBillStatusFlag.FREE) {
        billList.add(bills[i]);
        rowList.add(Integer.valueOf(rows[i]));
      }
    }
    int size = billList.size();
    if (size == 0) {
      return;
    }
    bills = new DelivBillVO[size];
    bills = billList.toArray(bills);
    rows = new int[size];
    for (int i = 0; i < size; i++) {
      rows[i] = rowList.get(i).intValue();
    }
    int ret = MessageDialog
        .showOkCancelDlg(this.node.getAbstractUI(),
            NCLangResOnserver.getInstance().getStrByID("40142020",
                "UPP40142020-000088")/* @res "确认" */, NCLangResOnserver
                .getInstance().getStrByID("40142020", "UPP40142020-000089")/*
                                                                             * @res
                                                                             * "确认删除当前所选单据吗"
                                                                             */);
    if (ret == UIDialog.ID_CANCEL) {
      return;
    }
    this.delete(bills);

    size = rows.length - 1;
    for (int i = size; i >= 0; i--) {
      buffer.remove(rows[i]);
    }
    this.setBufferIndex(buffer, rows[0]);
  }

  private void deleteForCard() {
    int ret = MessageDialog
        .showOkCancelDlg(this.node.getAbstractUI(),
            NCLangResOnserver.getInstance().getStrByID("40142020",
                "UPP40142020-000088")/* @res "确认" */, NCLangResOnserver
                .getInstance().getStrByID("40142020", "UPP40142020-000089")/*
                                                                             * @res
                                                                             * "确认删除当前所选单据吗"
                                                                             */);
    if (ret == UIDialog.ID_CANCEL) {
      return;
    }
    IBillBuffer buffer = this.node.getCommonBillBuffer();
    int cursor = buffer.getCurrentRow();
    ISmartBill bill = buffer.getBill(cursor);
    this.delete(new ISmartBill[] {
      bill
    });
    buffer.remove(cursor);
    this.setBufferIndex(buffer, cursor);
  }

  private void delete(ISmartBill[] bills) {
    int length = bills.length;
    DelivBillVO[] vos = new DelivBillVO[length];
    for (int i = 0; i < length; i++) {
      vos[i] = (DelivBillVO) bills[i];
    }
    ClientHelper.getInstance().deleteDelivBill(vos);
  }

  private void setBufferIndex(IBillBuffer buffer, int cursor) {
    int index = cursor;
    NodeStatus nodeStatus = this.node.getNodeStatus();
    int size = buffer.size();
    if (size == 0) {
      if (nodeStatus == NodeStatus.List) {
        this.node.getListPanel().getList().getListHelper()
            .showBills(buffer, -1);
      }
      else if (nodeStatus == NodeStatus.Card) {
        CardCtrl card = this.node.getCardPanel().getCard();
        CardHelper helper = new CardHelper(card);
        helper.show(null);
      }
      return;
    }
    if (cursor >= size) {
      index = size - 1;
    }
    buffer.setCurrentRow(index);
    if (nodeStatus == NodeStatus.List) {
      this.node.getListPanel().getList().getListHelper().showBills(buffer,
          index);
      BillListPanel list = this.node.getListPanel().getList().getBillList();
      list.getHeadTable().setRowSelectionInterval(index, index);
    }
    else if (nodeStatus == NodeStatus.Card) {
      ISmartBill bill = buffer.getBill(index);
      CardCtrl card = this.node.getCardPanel().getCard();
  	  CardHelper helper = new CardHelper(card);
  	 helper.show(bill);
    }
  }

  private void onModify() {
    NodeStatus status = this.node.getNodeStatus();
    if (status == NodeStatus.List) {
      SwitchEvent event = new SwitchEvent(this.node, NodeStatus.Card);
      this.node.fireSwitchEvent(event);
    }
    CardCtrl card = this.node.getCardPanel().getCard();
    card.getBillCard().setEnabled(true);
    FunctionStatus fstatus = this.node.getFunctionStatus();
    //如果是转单处理，则当前为新增界面
    if (fstatus == FunctionStatus.TransferBill) {
      card.setStatus(CardStatus.New);
    }
    //否则，是修改界面
    else{
      card.setStatus(CardStatus.Update);
      //运输单生成任务单后，运输单的单据号不可以修改
      if (card.getBillCard().getHeadItem("bmissionbillflag").getValueObject().equals("true"))
    	  card.getBillCard().getHeadItem("vbillcode").setEdit(false);
      else
    	  card.getBillCard().getHeadItem("vbillcode").setEdit(true);
    }
    card.getBillCard().transferFocusTo(IBillItem.HEAD);
  }

  private void onSelectAll() {
    ListCtrl list = this.node.getListPanel().getList();
    UITable table = list.getBillList().getHeadTable();
    int count = table.getRowCount();
    if (count == 0) {
      return;
    }
    table.selectAll();
    int cursor = table.getSelectedRow();
    this.node.getCommonBillBuffer().setCurrentRow(cursor);
  }

  private void onUnSelectAll() {
    ListCtrl list = this.node.getListPanel().getList();
    UITable table = list.getBillList().getHeadTable();
    int count = table.getRowCount();
    if (count > 0) {
      table.removeRowSelectionInterval(0, count - 1);
      this.node.getCommonBillBuffer().setCurrentRow(-1);
    }
  }

  private void onCancleTransferBill() {
    String message = NCLangResOnserver.getInstance().getStrByID("40142020",
        "UPP40142020-000090")/* @res "是否确定要取消当前参照生成单据操作" */;
    int value = MessageDialog.showOkCancelDlg(this.node.getAbstractUI(), null,
        message, UIDialog.ID_OK);
    if (value != UIDialog.ID_OK) {
      return;
    }
    IBillBuffer buffer = this.node.getTransferBillBuffer();
    buffer.clear();
    this.node.setFunctionStatus(FunctionStatus.Common);
    buffer = this.node.getCommonBillBuffer();
    if (buffer.size() > 0) {
      this.node.getListPanel().getList().getListHelper().showBills(buffer, 0);
    }
    else {
      this.node.getListPanel().getList().getListHelper().showBills(buffer, -1);
    }
  }

  private void onCardRowEdit() {
    this.node.getCardPanel().getCard().getBillCard().startRowCardEdit();
  }
}
