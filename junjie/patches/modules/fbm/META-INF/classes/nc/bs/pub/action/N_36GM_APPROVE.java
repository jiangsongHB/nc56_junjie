package nc.bs.pub.action;

import java.util.Hashtable;

import nc.bs.fbm.accept.AcceptBillService;
import nc.bs.fbm.accept.AcceptUtil;
import nc.bs.fbm.plan.PlanFacade;
import nc.bs.fbm.pub.action.BusiActionFactory;
import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.pub.compiler.AbstractCompiler2;
import nc.vo.fbm.acceptbill.AcceptVO;
import nc.vo.fbm.pub.constant.FbmActionConstant;
import nc.vo.fbm.pub.constant.FbmBusConstant;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.compiler.PfParameterVO;
import nc.vo.pub.lang.UFDate;
import nc.vo.trade.pub.HYBillVO;
import nc.vo.trade.pub.IBillStatus;
import nc.vo.uap.pf.PFBusinessException;
/**
 * 备注：票据付款的审核
单据动作执行中的动态执行类的动态执行类。
 *
 * 创建日期：(2007-9-4)
 * @author 平台脚本生成
 */
public class N_36GM_APPROVE extends AbstractCompiler2 {
private java.util.Hashtable m_methodReturnHas=new java.util.Hashtable();
private Hashtable m_keyHas=null;
/**
 * N_36GM_APPROVE 构造子注解。
 */
public N_36GM_APPROVE() {
	super();
}
/*
* 备注：平台编写规则类
* 接口执行类
*/
public Object runComClass(PfParameterVO vo) throws BusinessException {
try{
	super.m_tmpVo=vo;
	//####本脚本必须含有返回值,返回DLG和PNL的组件不允许有返回值####
	AcceptVO acceptVo = null;
	if(vo.m_preValueVo!=null&&vo.m_preValueVo.getParentVO()!=null){
		acceptVo = (AcceptVO) vo.m_preValueVo.getParentVO().clone();
	}
	procActionFlow(vo);
	Object retObj = runClass("nc.bs.trade.comstatus.BillApprove",
			"approveBill", "nc.vo.pub.AggregatedValueObject:36GM", vo,
			m_keyHas, m_methodReturnHas);

	String actioncode = null;
	if(retObj instanceof HYBillVO){
		CircularlyAccessibleValueObject parentVO = ((HYBillVO)retObj).getParentVO();
		Integer billstatus = (Integer)parentVO.getAttributeValue("vbillstatus");

		if(billstatus.intValue() == IBillStatus.CHECKPASS){//如果操作结果为审核通过
			actioncode = FbmActionConstant.AUDIT;
		}else{
			actioncode = FbmActionConstant.ONAUDIT;
		}
		BusiActionFactory.getInstance().createActionClass(FbmBusConstant.BILLTYPE_BILLPAY, actioncode).doAction((AcceptVO)parentVO, actioncode,false);
	}

	String ccReturnMsg = null;

	if(actioncode!=null&&actioncode.equals(FbmActionConstant.AUDIT)){
		ccReturnMsg = new AcceptBillService().applyRationBeforeGMApprove(acceptVo);
		//维护资金计划
		PlanFacade facade = new PlanFacade();
		facade.insertPlanExec(acceptVo, acceptVo.getPk_corp());

			//审核时，携带的保证金金额丢失，这此处把原来的acceptVo 放到HYBillVO中进行写账处理。
			HYBillVO retobj1 =  new HYBillVO();
			retobj1.setParentVO(acceptVo);
			//写银行账户账.
			AcceptUtil cbs = new AcceptUtil();
			String loginCorp = InvocationInfoProxy.getInstance().getCorpCode();
			//suzhzh 不写银行账，后续客户自己通过单据处理
			//cbs.addCMPBank(retobj1, loginCorp, vo.m_operator, new UFDate(vo.m_currentDate));
			//end
			cbs.addCMPBill(retobj1, loginCorp, vo.m_operator, new UFDate(vo.m_currentDate));
			

	}


	retObj = new Object[] { ccReturnMsg,retObj };
	if (retObj != null) {
		m_methodReturnHas.put("approveBill", retObj);
	}



	return retObj;
} catch (Exception ex) {
	if (ex instanceof BusinessException)
		throw (BusinessException) ex;
	else
    throw new PFBusinessException(ex.getMessage(), ex);
}
}
/*
* 备注：平台编写原始脚本
*/
public String getCodeRemark(){
	return nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("3620add","UPP3620ADD-000173")/*@res "	//####本脚本必须含有返回值,返回DLG和PNL的组件不允许有返回值####\n	Object retObj=null;\n	return retObj;\n"*/;}
/*
* 备注：设置脚本变量的HAS
*/
private void setParameter(String key,Object val)	{
	if (m_keyHas==null){
		m_keyHas=new Hashtable();
	}
	if (val!=null)	{
		m_keyHas.put(key,val);
	}
}
}