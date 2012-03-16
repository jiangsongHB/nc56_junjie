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
 * ��ע��Ʊ�ݸ�������
���ݶ���ִ���еĶ�ִ̬����Ķ�ִ̬���ࡣ
 *
 * �������ڣ�(2007-9-4)
 * @author ƽ̨�ű�����
 */
public class N_36GM_APPROVE extends AbstractCompiler2 {
private java.util.Hashtable m_methodReturnHas=new java.util.Hashtable();
private Hashtable m_keyHas=null;
/**
 * N_36GM_APPROVE ������ע�⡣
 */
public N_36GM_APPROVE() {
	super();
}
/*
* ��ע��ƽ̨��д������
* �ӿ�ִ����
*/
public Object runComClass(PfParameterVO vo) throws BusinessException {
try{
	super.m_tmpVo=vo;
	//####���ű����뺬�з���ֵ,����DLG��PNL������������з���ֵ####
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

		if(billstatus.intValue() == IBillStatus.CHECKPASS){//����������Ϊ���ͨ��
			actioncode = FbmActionConstant.AUDIT;
		}else{
			actioncode = FbmActionConstant.ONAUDIT;
		}
		BusiActionFactory.getInstance().createActionClass(FbmBusConstant.BILLTYPE_BILLPAY, actioncode).doAction((AcceptVO)parentVO, actioncode,false);
	}

	String ccReturnMsg = null;

	if(actioncode!=null&&actioncode.equals(FbmActionConstant.AUDIT)){
		ccReturnMsg = new AcceptBillService().applyRationBeforeGMApprove(acceptVo);
		//ά���ʽ�ƻ�
		PlanFacade facade = new PlanFacade();
		facade.insertPlanExec(acceptVo, acceptVo.getPk_corp());

			//���ʱ��Я���ı�֤���ʧ����˴���ԭ����acceptVo �ŵ�HYBillVO�н���д�˴���
			HYBillVO retobj1 =  new HYBillVO();
			retobj1.setParentVO(acceptVo);
			//д�����˻���.
			AcceptUtil cbs = new AcceptUtil();
			String loginCorp = InvocationInfoProxy.getInstance().getCorpCode();
			//suzhzh ��д�����ˣ������ͻ��Լ�ͨ�����ݴ���
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
* ��ע��ƽ̨��дԭʼ�ű�
*/
public String getCodeRemark(){
	return nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("3620add","UPP3620ADD-000173")/*@res "	//####���ű����뺬�з���ֵ,����DLG��PNL������������з���ֵ####\n	Object retObj=null;\n	return retObj;\n"*/;}
/*
* ��ע�����ýű�������HAS
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