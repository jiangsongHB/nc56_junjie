package nc.bs.ep.dj;

/**
 * 对单据的处理（审核，确认，签字确认，） 创建日期：(2001-9-3 17:24:23)
 *
 * @author：邱冬强
 */
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

import nc.bs.arap.action.pubactions.LockBillItem;
import nc.bs.arap.action.pubactions.LockDJAction;
import nc.bs.arap.callouter.FipCallFacade;
import nc.bs.arap.global.ArapExtInfRunBO;
import nc.bs.arap.global.ArapInvokeSettlement;
import nc.bs.arap.outer.ArapPubShenheInterface;
import nc.bs.arap.outer.ArapPubUnShenheInterface;
import nc.bs.arap.outer.IArapPubEffectInterface;
import nc.bs.arap.outer.IArapPubUnEffectInterface;
import nc.bs.arap.pub.PubDAO;
import nc.bs.arap.verify.SystemProfile;
import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Log;
import nc.bs.logging.Logger;
import nc.bs.ml.NCLangResOnserver;
import nc.impl.arap.proxy.Proxy;
import nc.vo.arap.exception.ExceptionHandler;
import nc.vo.arap.global.ArapCommonTool;
import nc.vo.arap.global.BusiTransVO;
import nc.vo.arap.global.DjVOTreaterAid;
import nc.vo.arap.global.ResMessage;
import nc.vo.arap.mapping.Arap_djfbVOMeta;
import nc.vo.arap.pub.ArapBusinessException;
import nc.vo.bd.b120.AccidVO;
import nc.vo.cmpbill.outer.BugetAlarmBusinessException;
import nc.vo.dap.out.DapMsgVO;
import nc.vo.ep.dj.DJZBHeaderVO;
import nc.vo.ep.dj.DJZBItemVO;
import nc.vo.ep.dj.DJZBVO;
import nc.vo.ep.dj.DJZBVOConsts;
import nc.vo.ep.dj.ShenheException;
import nc.vo.ml.NCLangRes4VoTransl;
import nc.vo.pub.BusinessException;
import nc.vo.pub.ExAggregatedVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;

public class ApplayBillBO  {
	ApplayBillDAO dao=new ApplayBillDAO();
	private FipCallFacade fipcallfacade;
	/**
	 * ApplayBill 构造子注解。
	 */
	public ApplayBillBO() {
		super();
	}

//	/**
//	 * 审核(或反审核)时控制账户余额
//	 *
//	 * 作者:陈飞 此处插入方法说明。 创建日期：(2001-11-28 20:34:19)
//	 *
//	 * @return nc.vo.ep.dj.ResMessage
//	 * @param dj
//	 *            nc.vo.ep.dj.DJZBVO isShenHe=true 时为审核 false为反审核
//	 */
//	public ResMessage accountContr(DJZBVO dj) throws BusinessException,
//			ShenheException {
//
//		return shenhe_Sq(dj, true);
//	}

	/**
	 * 取单据号 作者：陈飞
	 *
	 * @version 最后修改日期
	 * @see 需要参见的其它类
	 * @return nc.vo.ep.dj.DJZBHeaderVO[]
	 */
	public DJZBVO afterShenheInf(BusiTransVO[] busitransvos,
			DJZBVO dJZB) throws BusinessException {

		if (busitransvos != null) {
			for (int i = 0; i < busitransvos.length; i++) {
				try {

					SystemProfile.getInstance().log("arapbillauditstart aftershenhe 1 "+busitransvos[i].getClassName());

					long begin1=System.currentTimeMillis();
					Log.getInstance(this.getClass()).debug("调用接口"+busitransvos[i]
					.getClassName());
					((ArapPubShenheInterface) busitransvos[i]
							.getInfClass()).afterShenheAct(dJZB);
					long end1=System.currentTimeMillis();
					 Log.getInstance(this.getClass()).debug("结束调用接口"+busitransvos[i]
					     .getClassName()+",用时"+(end1-begin1)+"ms");

					 SystemProfile.getInstance().log("arapbillauditstart aftershenhe 2 "+busitransvos[i].getClassName());

				} catch (ClassCastException e){
					ExceptionHandler.consume(e);
				}
				catch (Exception e) {
					ExceptionHandler.consume(e);
					String strerr = busitransvos[i].getUsesystemname()
							+ busitransvos[i].getNote() + "\n" + e.getMessage();
					throw ExceptionHandler.createException(strerr,e);

				}
			}
		}
		return dJZB;
	}
	/**
	 * 取单据号 作者：陈飞
	 *
	 * @version 最后修改日期
	 * @see 需要参见的其它类
	 * @return nc.vo.ep.dj.DJZBHeaderVO[]
	 */
	public nc.vo.pub.ExAggregatedVO afterEffectInf(BusiTransVO[] busitransvos,
	        nc.vo.pub.ExAggregatedVO dJZB) throws BusinessException {

		if (busitransvos != null) {
			for (int i = 0; i < busitransvos.length; i++) {
				try {
					long begin1=System.currentTimeMillis();
					Log.getInstance(this.getClass()).debug("调用接口"+busitransvos[i]
					.getClassName());
					((IArapPubEffectInterface) busitransvos[i]
							.getInfClass()).afterEffectAct(dJZB);
					long end1=System.currentTimeMillis();
					 Log.getInstance(this.getClass()).debug("结束调用接口"+busitransvos[i]
					     .getClassName()+",用时"+(end1-begin1)+"ms");
				} catch (ClassCastException e){
					ExceptionHandler.consume(e);
				}
				catch (Exception e) {
					ExceptionHandler.consume(e);
					String strerr = busitransvos[i].getUsesystemname()
							+ busitransvos[i].getNote() + "\n" + e.getMessage();
//					if (e instanceof RemoteException)
//						strerr = busitransvos[i].getUsesystemname()
//								+ busitransvos[i].getNote() + "\n"
//								+ ((RemoteException) e).detail.getMessage();

					throw ExceptionHandler.createException( strerr,e );

				}
			}
		}
		return dJZB;
	}
	/**
	 * 取单据号 作者：陈飞
	 *
	 * @version 最后修改日期
	 * @see 需要参见的其它类
	 * @return nc.vo.ep.dj.DJZBHeaderVO[]
	 */
	public nc.vo.pub.ExAggregatedVO afterUnEffectInf(BusiTransVO[] busitransvos,
	        nc.vo.pub.ExAggregatedVO dJZB) throws BusinessException {

		if (busitransvos != null) {
			for (int i = 0; i < busitransvos.length; i++) {
				try {
					long begin1=System.currentTimeMillis();
					Log.getInstance(this.getClass()).debug("调用接口"+busitransvos[i]
					.getClassName());
					((IArapPubUnEffectInterface) busitransvos[i]
							.getInfClass()).afterUnEffectAct(dJZB);
					long end1=System.currentTimeMillis();
					 Log.getInstance(this.getClass()).debug("结束调用接口"+busitransvos[i]
					     .getClassName()+",用时"+(end1-begin1)+"ms");
				} catch (ClassCastException e){
					ExceptionHandler.consume(e);
				}
				catch (Exception e) {
					ExceptionHandler.consume(e);
					String strerr = busitransvos[i].getUsesystemname()
							+ busitransvos[i].getNote() + "\n" + e.getMessage();
//					if (e instanceof RemoteException)
//						strerr = busitransvos[i].getUsesystemname()
//								+ busitransvos[i].getNote() + "\n"
//								+ ((RemoteException) e).detail.getMessage();

					throw ExceptionHandler.createException( strerr ,e);

				}
			}
		}
		return dJZB;
	}
	/**
	 * 取单据号 作者：陈飞
	 *
	 * @version 最后修改日期
	 * @see 需要参见的其它类
	 * @return nc.vo.ep.dj.DJZBHeaderVO[]
	 */
	public DJZBVO afterUnShenheInf(
			BusiTransVO[] busitransvos, DJZBVO dJZB)
			throws BusinessException {

		if (busitransvos != null) {
			for (int i = 0; i < busitransvos.length; i++) {
				try {
					long begin1=System.currentTimeMillis();
					Log.getInstance(this.getClass()).debug("调用接口"+busitransvos[i]
					.getClassName());
					((ArapPubUnShenheInterface) busitransvos[i]
							.getInfClass()).afterUnShenheAct(dJZB);
					long end1=System.currentTimeMillis();
					 Log.getInstance(this.getClass()).debug("结束调用接口"+busitransvos[i]
					     .getClassName()+",用时"+(end1-begin1)+"ms");
				}catch(ClassCastException e){
					ExceptionHandler.consume(e);
				}catch (Exception e) {
					ExceptionHandler.consume(e);
					String strerr = busitransvos[i].getUsesystemname()
							+ busitransvos[i].getNote() + "\n" + e.getMessage();
//					if (e instanceof RemoteException)
//						strerr = busitransvos[i].getUsesystemname()
//								+ busitransvos[i].getNote() + "\n"
//								+ ((RemoteException) e).detail.getMessage();

					throw ExceptionHandler.createException(strerr,e);

				}
			}
		}
		return dJZB;
	}

	/**
	 * 审核单据
	 *
	 * 账户余额控制：
	 * 包含账户的收款单、付款单、收款结算单、付款结算单、划账结算单、对外收款结算单、对外付款结算单
	 * 在审核点维护账户档案的当前余额字段且进行账户赤字控制。
	 * 如果没有与单据币种相同的账户则审核失败
	 * 账户余额 账户赤字额度时为合法
	 * 账户余额>账户赤字额度时取账户档案的账户
	 *
	 * 赤字控制方式：
	 * 控制方式为提示信息时，则出提示信息，仍可审核。 J 控制方式为限制操作时，则出提示信息且不能进行审核。
	 * 控制方式为授权管理时，判断当前登录人是否是账户赤字授权控制人，是则出提示信息且能够审核；否则出提示信息且不能审核。
	 *
	 * 账户当前余额 借方-,贷方+
	 * res.intvalue=9999说明需要授权,res.intvalue=1通过授权,res.intvalue=2说明审核成功
	 */
	public ResMessage auditABill(DJZBVO dj) throws BusinessException {
		SystemProfile.getInstance().log("arapbillauditstart"+dj.header.getDjbh());
		ResMessage res = new ResMessage();
		res.isSuccess = true;
//		res.strMessage = NCLangRes4VoTransl.getNCLangRes().getStrByID("2006030102","UPP2006030102-000543")/*@res "审核成功"*/;

		res.intValue = -1; //res.intValue=9999说明需要授权

		DJZBHeaderVO head = (DJZBHeaderVO) dj.getParentVO();//单据表头
		new LockDJAction().lockDJ(dj);

		if (head.getShr() == null || head.getShr().trim().length() < 1) {
			head.setShr(head.getLrr());
			if(dj.m_isQr==true){
				head.setShrq((UFDate)dj.m_objOne);
				head.setShkjnd((String)dj.m_objTwo);
				head.setShkjqj((String)dj.m_objThree);
			}else{
				head.setShrq(head.getDjrq());
				head.setShkjnd(head.getDjkjnd());
				head.setShkjqj(head.getDjkjqj());
			}
		}
		ArapDjBsCheckerBO bocheck = new ArapDjBsCheckerBO();

		try{
		boolean bIssettled = bocheck.isSettled(head.getDwbm(),head.getPzglh().intValue(),dj);
			if(bIssettled){

			    throw ExceptionHandler.createException(NCLangResOnserver.getInstance().getStrByID("2006030102","UPP2006030102-000071"));
			}
		}catch(Exception e){
			ExceptionHandler.consume(e);
		    throw ExceptionHandler.createException(NCLangResOnserver.getInstance().getStrByID("2006030102","UPP2006030102-000071"),e);
		}
		SystemProfile.getInstance().log("arapbillauditstart 1"+dj.header.getDjbh());
		try {
		        dj.setParam_Ext_Save();



		} catch (Exception e) {

			throw ExceptionHandler.handleException(this.getClass(), e);
		}

		ExceptionHandler.debug(NCLangRes4VoTransl.getNCLangRes().getStrByID("2006030102","UPP2006030102-000544")/*@res "@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@联查单据表体"*/);
		res.djzbvo = dj;

		/** 外系统审核前接口* */
		ArapExtInfRunBO extbo = new ArapExtInfRunBO();
		BusiTransVO[] busitransvos = extbo.initBusiTrans(
				"shenhe", head.getPzglh());
		this.doBusi(dj, busitransvos);
		this.effect(dj);

		SystemProfile.getInstance().log("arapbillauditstart end"+dj.header.getDjbh());

		res.intValue=ResMessage.$SHENHE_SUCCESS;
		res.m_Ts =head.getts().toString();
		return res;
		}
	public ResMessage shenheBack(nc.vo.ep.dj.DJZBVO dj, BusiTransVO[] busitransvos)
	throws BusinessException {
		if(null==busitransvos){
           nc.bs.arap.global.ArapExtInfRunBO extbo = new nc.bs.arap.global.ArapExtInfRunBO();
           busitransvos = extbo.initBusiTrans("shenhe", ((DJZBHeaderVO)dj.getParentVO()).getPzglh());
		}
		return shenheBack(dj,busitransvos,true);
	}
	public ResMessage shenheBack(nc.vo.ep.dj.DJZBVO dj, BusiTransVO[] busitransvos,boolean checkFlow)
			throws BusinessException {
		ResMessage res = new ResMessage();
		res.isSuccess = true;
		res.strMessage = "";
		res.intValue = -1; //res.intValue=9999说明需要授权

		nc.vo.ep.dj.DJZBHeaderVO head = (nc.vo.ep.dj.DJZBHeaderVO) dj
				.getParentVO();
		int iStatus = Proxy.getIPFWorkflowQry().queryWorkflowStatus(
				head.getXslxbm() == null
						|| head.getXslxbm().trim().length() < 1 ? "KHHH0000000000000001"
						: head.getXslxbm().trim(),
				head.getDjlxbm(), head.getVouchid());
		if (checkFlow&&!(iStatus == nc.vo.pub.pf.IWorkFlowStatus.BILLTYPE_NO_WORKFLOW
				|| iStatus == nc.vo.pub.pf.IWorkFlowStatus.BILL_NOT_IN_WORKFLOW)){
			res.isSuccess = false;
			res.strMessage = NCLangRes4VoTransl.getNCLangRes().getStrByID("2006030102","UPP2006030102-000549")/*@res "审批流单据不能后台审核."*/;
			return res;
		}

		new LockDJAction().lockDJ(dj);
		head.setDjzt(DJZBVOConsts.m_intDJStatus_Verified);
		head.setSpzt(DJZBVOConsts.m_strStatusVerifiedPass);
		try {
			dao.updateShenheBack(head);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			throw ExceptionHandler.handleException(e);
		}
		if( DjVOTreaterAid.hasSettleInfo(head )){
			try {
				new ArapInvokeSettlement ().invokeCmp(dj,head.getShrq());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				throw ExceptionHandler.handleException(this.getClass(), e);
			}
		}else{
			doBusi(dj,busitransvos);
			effect(dj);
		}
		res.djzbvo=dj;
		res.m_Ts=((DJZBHeaderVO)dj.getParentVO()).getts().toString();
		return res;
	}



	public void effect(nc.vo.ep.dj.DJZBVO dj) throws BusinessException{

		SystemProfile.getInstance().log("arapbillauditstart eff begin"+dj.header.getDjbh());

		DJZBHeaderVO head = (DJZBHeaderVO) dj.getParentVO();
		ArapDjBsCheckerBO bocheck = new ArapDjBsCheckerBO();
		try{
		    boolean bIssettled = bocheck.isSettled(head.getDwbm(),head.getPzglh().intValue(),dj);
			if(bIssettled){
			   throw ExceptionHandler.createException(NCLangResOnserver.getInstance().getStrByID("2006030102","UPP2006030102-000071"));
			}
		}catch(Exception e){
			ExceptionHandler.consume(e);
		    throw ExceptionHandler.createException(NCLangResOnserver.getInstance().getStrByID("2006030102","UPP2006030102-000071"),e);
		}
		head.setSxbz(DJZBVOConsts.m_intSXBZ_VALID);
		try {
			dao.updateEffectStatus(head);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			throw ExceptionHandler.handleException(this.getClass(), e);
		}
		SystemProfile.getInstance().log("arapbillauditstart updateEffectStatus end "+dj.header.getDjbh());

		EffectAction ea = new EffectAction();
		ea.doEffectAction(dj);

		SystemProfile.getInstance().log("arapbillauditstart doeffectaction end "+dj.header.getDjbh());

		NCLocator.getInstance().lookup(nc.itf.arap.balance.IArapBalanceUpdate.class).updateForBill(new DJZBVO[]{dj});

		SystemProfile.getInstance().log("arapbillauditstart yuebiao end "+dj.header.getDjbh());

		sendMessage(dj);

		SystemProfile.getInstance().log("arapbillauditstart sendmsg end "+dj.header.getDjbh());

		SystemProfile.getInstance().log("arapbillauditstart eff end "+dj.header.getDjbh());

	}
	public void uneffect(nc.vo.ep.dj.DJZBVO dj) throws BusinessException{
		AdjustBillInf info = new AdjustBillInf();
		try {
			UFBoolean flag = info.getAdjustbill_unAudit(dj);
			if(null != flag && !flag.booleanValue()){
				throw ExceptionHandler.createException( nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("2006","UPP2006-v55-000040")/*@res "单据已经生成调整单不允许反审核，操作失败！"*/);
			}
		} catch (Exception e1) {
			throw ExceptionHandler.handleException(this.getClass(), e1);
		}
		DJZBHeaderVO head=(DJZBHeaderVO)dj.getParentVO();
		head.setSxbz(DJZBVOConsts.m_intSXBZ_NO);
		head.setSxr(null);
		head.setSxkjnd(null);
		head.setSxrq(null);
		head.setSxkjqj(null);
		long begin=System.currentTimeMillis();
		Log.getInstance(this.getClass()).debug("更新反审核单据生效状态");
		try {
			dao.updateEffectStatus(head);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			throw ExceptionHandler.handleException(this.getClass(),e);
		}
		long end=System.currentTimeMillis();
		Log.getInstance(this.getClass()).debug("结束更新反审核单据生效状态,用时"+(end-begin)+"ms");
		NCLocator.getInstance().lookup(nc.itf.arap.balance.IArapBalanceUpdate.class).updateForBillUnEff(new DJZBVO[]{dj});
		this.sendMessage_del(dj);

	}
	/**
	 * 审核单据 创建日期：(2001-9-3 17:31:51)
	 *
	 * @return java.lang.String[]
	 * @param djs
	 *            nc.vo.ep.dj.DJZBVO[] 2、账户余额控制： ü
	 *            包含账户的收款单、付款单、收款结算单、付款结算单、划账结算单、对外收款结算单、对外付款结算单
	 *            在审核点维护账户档案的当前余额字段且进行账户赤字控制。 如果没有与单据币种相同的账户则审核失败 N
	 *            账户余额≤账户赤字额度时为合法. N 账户余额>账户赤字额度时取账户档案的账户赤字控制方式： J
	 *            控制方式为提示信息时，则出提示信息，仍可审核。 J 控制方式为限制操作时，则出提示信息且不能进行审核。 J
	 *            控制方式为授权管理时，判断当前登录人是否是账户赤字授权控制人，是则出提示信息且能够审核；否则出提示信息且不能审核。
	 *
	 * 账户当前余额 借方-,贷方+
	 * res.intvalue=9999说明需要授权,res.intvalue=1通过授权,res.intvalue=2说明审核成功
	 */
	public ResMessage doBusi(nc.vo.ep.dj.DJZBVO dj,	nc.vo.arap.global.BusiTransVO[] busitransvos)
			throws BusinessException {
		ResMessage res = new ResMessage();
		res.isSuccess = true;
		res.strMessage = "";
		res.intValue = -1; //res.intValue=9999说明需要授权

		nc.vo.ep.dj.DJZBHeaderVO head = (nc.vo.ep.dj.DJZBHeaderVO) dj
				.getParentVO();
		//单据表头
		DJZBBO djzbbo=new DJZBBO();
		DJZBDAO djdmo = null;
		try {
			djdmo = new DJZBDAO();

		} catch (Exception e) {
			throw ExceptionHandler.handleException(this.getClass(), e);

		}
		try {
			dj.setParam_Ext_Save();
			DjVOTreaterAid.supplementXTFlag(dj);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			throw ExceptionHandler.handleException(e1);
		}
		DJZBItemVO[] items = dj.getChildrenVO() == null ? null
				: (DJZBItemVO[]) dj.getChildrenVO();

		SystemProfile.getInstance().log("arapbillauditstart dobusi1"+dj.header.getDjbh());

		//cf2002-01-14 add
		//查询单据表体
		if (items == null) {
			try {
					items = djdmo.findItemsForHeader(head.getPrimaryKey());
			} catch (Exception e) {
				res.strMessage = NCLangRes4VoTransl.getNCLangRes().getStrByID("2006030102","UPP2006030102-000148")/*@res "联查单据表体失败,审核失败"*/;
				res.isSuccess = false;
				return res;
			}
			dj.setChildrenVO(items);
		}

		SystemProfile.getInstance().log("arapbillauditstart dobusi2"+dj.header.getDjbh());

		//supplement validation information
		ARAPDjVOUtil.supplementValidationInfo(dj);

		res.djzbvo = dj;
        ArapDjBsCheckerBO bocheck = new ArapDjBsCheckerBO();
        ResMessage res1;
		try {
			res1 = bocheck.checkApproveBill(dj);
			if(res1!=null && res1.isSuccess==false)
		            throw ExceptionHandler.createException(res1.strMessage);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			 throw ExceptionHandler.createException(e1.getMessage());
		}

		SystemProfile.getInstance().log("arapbillauditstart dobusicheck"+dj.header.getDjbh());

		//外接口审核单据前动作
		long t1 = System.currentTimeMillis();
		beforeShenheInf(busitransvos, dj);
		ExceptionHandler.debug("外接口审核单据前动作前所用时间:"
				+ (System.currentTimeMillis() - t1));

		SystemProfile.getInstance().log("arapbillauditstart beforeshenhe end"+dj.header.getDjbh());

		res.djzbvo = dj;
		try {

			/** **********************************以下开始事项审批控制 */
			if (head.getSsflag().equals("1")) {
				new LockBillItem().lock_item_bill(items, head.getLrr(), 1);
				//itemconfigbo.checkApprove((DJZBVO) dj.clone(), NCLangRes4VoTransl.getNCLangRes().getStrByID("2006030102","UPT2006030102-000051")/*@res "审核"*/);
				//itemconfigbo.checkApprove((DJZBVO) dj.clone(), "审核");
				Proxy.getIItemConfigPrivatee().ShenHeSave(dj);
//				curdjzbbo.freeLock_item_bill(items, head.getLrr(), 1);

			}

			SystemProfile.getInstance().log("arapbillauditstart sxsp end"+dj.header.getDjbh());

			/** ******************************************以上事项审批控制* */
			/** **************** 协同单据********************** */
			ExceptionHandler.debug("协同单据...");
			if (head.getXtflag() != null && head.getXtflag().equals("审核或签字确认")) { /*-=notranslate=-*/

				djzbbo.deleteXTBill(head.getVouchid());
				//生成新的协同单据
				nc.bs.arap.billcooperation.BillCooperateBO bic = new nc.bs.arap.billcooperation.BillCooperateBO();
				bic.doCooperate(dj);

			}

			SystemProfile.getInstance().log("arapbillauditstart xt end"+dj.header.getDjbh());

			/** **************** 协同单据****************** */
			//外接口审核单据后动作

			long t2 = System.currentTimeMillis();

			afterShenheInf(busitransvos, dj);
			ExceptionHandler.debug("外接口审核单据前动作后所用时间:"
					+ (System.currentTimeMillis() - t2));
			if (dj.m_Resmessage != null) {
				if(dj.m_Resmessage.intValue==ResMessage.$BUGET_ALARM){
					throw new BugetAlarmBusinessException(dj.m_Resmessage.strMessage);
				}else{
					res.strMessage = res.strMessage + dj.m_Resmessage.strMessage;
					res.isSuccess = dj.m_Resmessage.isSuccess;
				}
			}

			SystemProfile.getInstance().log("arapbillauditstart dobusi end"+dj.header.getDjbh());

		} catch (Exception e) {
			throw ExceptionHandler.handleException(this.getClass(), e);
		}
		ExceptionHandler.debug(NCLangRes4VoTransl.getNCLangRes().getStrByID("2006030102","UPP2006030102-000548")/*@res "审核返回..."*/);
		return res;
	}

////////	/**
////////	 * 审核单据 创建日期：(2001-9-3 17:31:51)
////////	 *
////////	 * @return java.lang.String[]
////////	 * @param djs
////////	 *            nc.vo.ep.dj.DJZBVO[] 2、账户余额控制： ü
////////	 *            包含账户的收款单、付款单、收款结算单、付款结算单、划账结算单、对外收款结算单、对外付款结算单
////////	 *            在审核点维护账户档案的当前余额字段且进行账户赤字控制。 如果没有与单据币种相同的账户则审核失败 N
////////	 *            账户余额≤账户赤字额度时为合法. N 账户余额>账户赤字额度时取账户档案的账户赤字控制方式： J
////////	 *            控制方式为提示信息时，则出提示信息，仍可审核。 J 控制方式为限制操作时，则出提示信息且不能进行审核。 J
////////	 *            控制方式为授权管理时，判断当前登录人是否是账户赤字授权控制人，是则出提示信息且能够审核；否则出提示信息且不能审核。
////////	 *
////////	 * 账户当前余额 借方-,贷方+
////////	 * res.intvalue=9999说明需要授权,res.intvalue=1通过授权,res.intvalue=2说明审核成功
////////	 */
////////	public ResMessage auditABill2_out(nc.vo.ep.dj.DJZBVO dj,
////////			nc.vo.arap.global.BusiTransVO[] busitransvos)
////////			throws BusinessException {
////////		long t = System.currentTimeMillis();
////////		String result = "";
////////		String pk_accid = ""; //账户主键
////////		String pk_currtype = ""; //单据币种
////////		nc.vo.bd.b120.AccidVO accidvo = null; //账户vo
////////		ResMessage res = new ResMessage();
////////		res.isSuccess = true;
////////		res.strMessage = "";
////////		DJZBBO curdjzbbo = new DJZBBO();
////////		res.intValue = -1; //res.intValue=9999说明需要授权
////////
////////		nc.vo.ep.dj.DJZBHeaderVO head = (nc.vo.ep.dj.DJZBHeaderVO) dj
////////				.getParentVO();
////////		//单据表头
////////
////////		DJZBDMO djdmo = null;
////////		try {
////////			djdmo = new DJZBDMO();
////////			//并发控制
////////			isDistributes(dj);
////////
////////			//判断单据是否在审批流中
////////
////////			if (head.getSpzt() != null
////////					&& (head.getSpzt().trim().equalsIgnoreCase(DJZBVOConsts.m_strStatusVerifying) || head
////////							.getSpzt().trim().equalsIgnoreCase(DJZBVOConsts.m_strStatusVerifiedPass))) {
////////				res.isSuccess = false;
////////				res.strMessage = NCLangRes4VoTransl.getNCLangRes().getStrByID("2006030102","UPP2006030102-000550")/*@res "审批流单据只能在收付系统中审核."*/;
////////				return res;
////////			}
////////			nc.bs.pub.pf.PfUtilBO pfbo = new nc.bs.pub.pf.PfUtilBO();
////////			int iStatus = pfbo
////////					.queryWorkFlowStatus(
////////							head.getXslxbm() == null
////////									|| head.getXslxbm().trim().length() < 1 ? "KHHH0000000000000001"
////////									: head.getXslxbm().trim(),
////////							head.getDjlxbm(), head.getVouchid());
////////			//if (iStatus ==
////////			// nc.vo.pub.pf.IWorkFlowStatus.NOT_STARTED_IN_WORKFLOW) {
////////			///** 单据已发送到审批流中，但尚未开始审批 */
////////			//res.isSuccess = false;
////////			//res.strMessage = "审批流单据不能后台审核.";
////////			//return res;
////////			//}
////////			//if (iStatus ==
////////			// nc.vo.pub.pf.IWorkFlowStatus.ABNORMAL_WORKFLOW_STATUS) {
////////			///** 未知的异常状态 */
////////			//res.isSuccess = false;
////////			//res.strMessage = "审批流单据不能后台审核.";
////////			//return res;
////////			//}
////////			if (iStatus == nc.vo.pub.pf.IWorkFlowStatus.BILLTYPE_NO_WORKFLOW
////////					|| iStatus == nc.vo.pub.pf.IWorkFlowStatus.BILL_NOT_IN_WORKFLOW)
////////				;
////////			else {
////////				/** 该单据类型没有配置工作流 */
////////				res.isSuccess = false;
////////				res.strMessage = NCLangRes4VoTransl.getNCLangRes().getStrByID("2006030102","UPP2006030102-000550")/*@res "审批流单据只能在收付系统中审核."*/;
////////				return res;
////////
////////			}
////////
////////			//if (head.getXtflag() != null && head.getXtflag().equals("审核")) {
////////
////////			////删除协同单据
////////			//djdmo.delXtBYSourcePK(head.getVouchid());
////////
////////			//}
////////
////////		} catch (Exception e) {
////////			Log.getInstance(this.getClass()).error(e.getMessage(),e);
////////			//throw new BusinessShowException("distributeDjzb Exception!", e);
////////			if (e instanceof RemoteException) {
////////				throw (RemoteException) e;
////////			} else {
////////				throw new BusinessShowException(
////////						"ApplayBillBO::auditABill(dj) Exception!", e);
////////			}
////////		}
////////
////////		nc.vo.pub.rs.Debug
////////				.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@dj.getParentVO()");
////////		DJZBItemVO[] items = dj.getChildrenVO() == null ? null
////////				: (DJZBItemVO[]) dj.getChildrenVO();
////////		//单据表体
////////		//cf2002-01-14 add
////////		//查询单据表体
////////		if (items == null) {
////////			try {
////////
////////				if (head.getPzglh().intValue() == 3
////////						|| head.getDjdl().equals("ss"))
////////					items = djdmo.findItemsForHeader_SS(head.getPrimaryKey());
////////				else
////////					items = djdmo.findItemsForHeader(head.getPrimaryKey());
////////			} catch (Exception e) {
////////				res.strMessage = NCLangRes4VoTransl.getNCLangRes().getStrByID("2006030102","UPP2006030102-000148")/*@res "联查单据表体失败,审核失败"*/;
////////				res.isSuccess = false;
////////				return res;
////////			}
////////			dj.setChildrenVO(items);
////////		}
////////		//cf2002-01-14 add
////////
////////		//cf2002-06-06 add for 存货分类主键
////////		String pk_invcl = null;
////////		for (int j = 0; j < items.length; j++) {
////////			if (items[j].getChbm_cl() != null
////////					&& items[j].getChbm_cl().trim().length() > 1) {
////////				if (items[j].getPk_invcl() == null
////////						|| items[j].getPk_invcl().trim().length() < 1) {
////////					pk_invcl = getPk_invclByPk(items[j].getCinventoryid());
////////					items[j].setPk_invcl(pk_invcl);
////////
////////				}
////////			}
////////		}
////////		//cf2002-06-06 end
////////		nc.vo.pub.rs.Debug
////////				.println(NCLangRes4VoTransl.getNCLangRes().getStrByID("2006030102","UPP2006030102-000544")/*@res "@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@联查单据表体"*/);
////////		res.djzbvo = dj;
////////
////////		//外接口审核单据前动作
////////		long t1 = System.currentTimeMillis();
////////		//nc.bs.arap.global.ArapExtInfRunBO extbo =
////////		//new nc.bs.arap.global.ArapExtInfRunBO();
////////		//nc.vo.arap.global.BusiTransVO[] busitransvos =
////////		//extbo.initBusiTrans("shenhe", head.getPzglh());
////////		beforeShenheInf(busitransvos, dj);
////////		Log.getInstance(this.getClass()).debug("外接口审核单据前动作前所用时间:"
////////				+ (System.currentTimeMillis() - t1));
////////
////////		//cf2002-06-06 add for 存货分类主键
////////		//String pk_invcl = null;
////////		//for (int i = 0; i < items.length; i++) {
////////		//if (items[i].getChbm_cl() != null
////////		//&& items[i].getChbm_cl().trim().length() > 1) {
////////		//if (items[i].getPk_invcl() == null
////////		//|| items[i].getPk_invcl().trim().length() < 1) {
////////		//pk_invcl = getPk_invclByPk(items[i].getCinventoryid());
////////		//items[i].setPk_invcl(pk_invcl);
////////
////////		//}
////////		//}
////////		//}
////////		//cf2002-06-06 end
////////
////////		res.djzbvo = dj;
////////		nc.vo.pub.rs.Debug
////////				.println(NCLangRes4VoTransl.getNCLangRes().getStrByID("2006030102","UPP2006030102-000546")/*@res "@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@余额控制前时间:"*/
////////						+ (System.currentTimeMillis() - t));
////////		try {
////////
////////			//回写报销
//////////			Log.getInstance(this.getClass()).debug("回写报销");
//////////			nc.bs.arap.ebmanager.NOTEEBMBO notbo = new nc.bs.arap.ebmanager.NOTEEBMBO();
//////////
//////////			notbo.afterAudit(dj, null);
//////////
//////////			//回写合同
//////////			Log.getInstance(this.getClass()).debug("回写合同");
////////
////////			//curdjzbbo.insertContract(dj);
////////
////////			//计息
////////			//需要传筹投资的单据类型 sk,fk,sj,fj , hj,ws,wf
////////			//Log.getInstance(this.getClass()).debug("计息");
////////			//if (head.m_FIMIsUsed) {
////////			//if (head.getDjdl().equals("sk")
////////			//|| head.getDjdl().equals("fk")
////////			//|| head.getDjdl().equals("sj")
////////			//|| head.getDjdl().equals("fj")
////////			//|| head.getDjdl().equals("ws")
////////			//|| head.getDjdl().equals("wf")
////////			//|| head.getDjdl().equals("hj")) {
////////			//nc.bs.arap.global.ArapClassRunBO executeBo =
////////			//new nc.bs.arap.global.ArapClassRunBO();
////////
////////			//Class[] paramtype = { DJZBVO.class };
////////			//Object[] param = { dj };
////////			//executeBo.runMethod(
////////			//"nc.bs.fi.accamount.AccAmountEpBO",
////////			//"approve",
////////			//paramtype,
////////			//param);
////////
////////			//}
////////			//}
////////
////////			///**状态检查*/
////////
////////			///**预算*/
////////
////////			/** 打标志 */
////////			//以下处理账户余额控制
////////			//t = System.currentTimeMillis();
////////			Log.getInstance(this.getClass()).debug("以下处理账户余额控制...");
////////			res = shenhe_Sq(dj, true);
////////			if (!res.isSuccess)
////////				return res;
////////
////////			res.intValue = 1; //通过授权控制
////////			//nc.vo.pub.rs.Debug.println(
////////			//	"@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@处理账户余额控制时间:"
////////			//		+ (System.currentTimeMillis() - t));
////////			//以上处理账户余额控制
////////
////////			////以下处理业务应收，财务应收，和订单应收
////////			// Log.getInstance(this.getClass()).debug("以下处理业务应收，财务应收，和订单应收");
////////			// updateCoustom(dj, true);
////////			//nc.bs.so.sointerface.SaleToARDMO SaleDMO =
////////			//new nc.bs.so.sointerface.SaleToARDMO();
////////			//String xyzh = ""; //订单id
////////			//Hashtable hb = null;
////////			//if (head.getDjdl().equals("ys")) { //加
////////			//if (items[0].getWldx().intValue() == 0) { //往来对象必须是客户
////////			//if (head.getLybz().intValue() == 3
////////			//|| head.getLybz().intValue() == 4) //销售，采购系统单据,增加账务应收
////////			//{
////////			//hb = null;
////////			//if (items[0].getXyzh() != null &&
////////			// items[0].getXyzh().trim().length() > 0)
////////			//hb = SaleDMO.getSaleOrderCust(new String[] {
////////			// items[0].getXyzh()}); //取订单客户
////////			//if (hb == null || hb.isEmpty())
////////			//getCumandocBO().updateMny(head.getKsbm_cl(), head.getBbje(),
////////			// null, null);
////////			//else
////////			//getCumandocBO().updateMny(
////////			//hb.get(items[0].getXyzh()).toString(),
////////			//head.getBbje(),
////////			//null,
////////			//null);
////////
////////			//} else //非外系统三个都增加
////////			//{
////////			//getCumandocBO().updateMny(
////////			//head.getKsbm_cl(),
////////			//head.getBbje(),
////////			//head.getBbje(),
////////			//head.getBbje());
////////			//}
////////			//}
////////			//} else
////////			//if (head.getDjdl().equals("sk")) { //减
////////			//for (int i = 0; i < items.length; i++) {
////////			//if (items[i].getWldx().intValue() == 0) {
////////
////////			//getCumandocBO().updateMny(
////////			//items[i].getKsbm_cl(),
////////			//items[i].getDfbbje().multiply(-1),
////////			//items[i].getDfbbje().multiply(-1),
////////			//items[i].getDfbbje().multiply(-1));
////////
////////			//}
////////			//}
////////			//}
////////
////////			////以上处理业务应收，财务应收，和订单应收
////////
////////			//以下打上审核标志
////////			Log.getInstance(this.getClass()).debug("以下打上审核标志...");
////////			ApplayBillDMO dm = new ApplayBillDMO();
////////			dm.setFlagBill2((nc.vo.ep.dj.DJZBHeaderVO) dj.getParentVO());
////////
////////			res.intValue = 2; //通过最后审核
////////
////////			/** **********************************以下开始事项审批控制 */
////////			if (head.getDjdl().equals("yf") || head.getDjdl().equals("fk")
////////					|| head.getDjdl().equals("fj")) {
////////				nc.bs.ep.itemconfig.ItemconfigBO itemconfigbo = new nc.bs.ep.itemconfig.ItemconfigBO();
////////				curdjzbbo.lock_item_bill(items, head.getLrr(), 1);
////////				//itemconfigbo.checkApprove((DJZBVO) dj.clone(), NCLangRes4VoTransl.getNCLangRes().getStrByID("2006030102","UPT2006030102-000051")/*@res "审核"*/);
////////				itemconfigbo.checkApprove((DJZBVO) dj.clone(),"审核");
////////				curdjzbbo.freeLock_item_bill(items, head.getLrr(), 1);
////////
////////			}
////////
////////			/** ******************************************以上事项审批控制* */
////////
////////			///*************以下进行预算控制***************** */
////////			//t = System.currentTimeMillis();
////////			////费用控制
////////			//Log.getInstance(this.getClass()).debug("费用控制...");
////////			//ResMessage res2 = shenhe_Ys_Hy(dj, "审核",false);
////////			//res.strMessage = res.strMessage + res2.strMessage;
////////			//res.isSuccess = res2.isSuccess;
////////			//nc.vo.pub.rs.Debug.println(
////////			//"@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@处理预算控制时间:"
////////			//+ (System.currentTimeMillis() - t));
////////			///*************以上进行预算控制***************** */
////////			/** **************** 协同单据********************** */
////////			Log.getInstance(this.getClass()).debug("协同单据...");
////////			if (head.getXtflag() != null && head.getXtflag().equals("审核或签字确认")) {
////////
////////				//生成新的协同单据
////////				nc.bs.arap.billcooperation.BillCooperateBO bic = new nc.bs.arap.billcooperation.BillCooperateBO();
////////				bic.doCooperate(dj);
////////
////////			}
////////			/** **************** 协同单据****************** */
////////			//外接口审核单据后动作
////////			long t2 = System.currentTimeMillis();
////////
////////			afterShenheInf(busitransvos, dj);
////////			Log.getInstance(this.getClass()).debug("外接口审核单据前动作后所用时间:"
////////					+ (System.currentTimeMillis() - t2));
////////			if (dj.m_Resmessage != null) {
////////				res.strMessage = res.strMessage + dj.m_Resmessage.strMessage;
////////				res.isSuccess = dj.m_Resmessage.isSuccess;
////////			}
////////
////////			Log.getInstance(this.getClass()).debug("传平台...");
////////			sendMessage(dj);
////////
////////			String tablename = "arap_djzb";
////////			if (head.getPzglh().intValue() == 3 || head.getDjdl().equals("ss"))
////////				tablename = "arap_item";
////////			res.m_Ts = djdmo.getTsByPrimaryKey(head.getVouchid(), tablename);
////////			//
////////			unLockBill(dj);
////////			//if(true)
////////			//throw new Exception("test");
////////		} catch (Exception e) {
////////			Log.getInstance(this.getClass()).error(e.getMessage(),e);
////////			if (e instanceof RemoteException) {
////////				throw (RemoteException) e;
////////			} else {
////////				throw new BusinessShowException(
////////						"ApplayBillBO::auditABill(dj) Exception!", e);
////////			}
////////
////////		}
////////
////////		nc.vo.pub.rs.Debug.println(NCLangRes4VoTransl.getNCLangRes().getStrByID("2006030102","UPP2006030102-000548")/*@res "审核返回..."*/);
////////		return res;
////////	}
//////
//////	/**
//////	 * 此处插入方法说明。 创建日期：(2001-11-27 16:55:59)
//////	 *
//////	 * @exception BusinessException
//////	 *                异常说明。
//////	 */
//////	public String auditABillbySourceid(String id)
//////			throws BusinessException {
//////		DJZBVO dj = new DJZBVO();
//////		String sRe = "";
//////		//
//////		try {
//////			DJZBDMO djzb = new DJZBDMO();
//////			//Log.getInstance(this.getClass()).debug(" aa ddlx is :" + id);
//////			String whereString = "  ,arap_djfb where  arap_djzb.vouchid = arap_djfb.vouchid and arap_djfb.ddlx = '"
//////					+ id.trim()
//////					+ "' and arap_djzb.djzt = '1' and arap_djzb.djdl is not null and  arap_djzb.dr=0 ";
//////			DJZBHeaderVO[] headers = djzb.queryHead(whereString);
//////			if (headers == null || headers.length == 0) {
//////
//////			} else {
//////				for (int i = 0; i < headers.length; i++) {
//////					headers[i].setShr(headers[i].getLrr());
//////					headers[i].setShrq(headers[i].getDjrq());
//////					headers[i].setShkjnd(headers[i].getDjkjnd());
//////					headers[i].setShkjqj(headers[i].getDjkjqj());
//////					dj.setParentVO(headers[i]);
//////					dj.setChildrenVO(null);
//////					auditABill(dj);
//////					sendMessage(dj);
//////				}
//////			}
//////
//////		} catch (Exception e) {
//////			Log.getInstance(this.getClass()).error(e.getMessage(),e);
//////			throw new BusinessShowException(NCLangRes4VoTransl.getNCLangRes().getStrByID("2006030102","UPP2006030102-000551")/*@res "收付单据审核失败！"*/, e);
//////		}
//////
//////		return sRe;
//////
//////	}
////
////	/**
////	 * 审核单据 创建日期：(2001-9-3 17:31:51)
////	 *
////	 * @return java.lang.String[]
////	 * @param djs
////	 *            nc.vo.ep.dj.DJZBVO[] 2、账户余额控制： ü
////	 *            包含账户的收款单、付款单、收款结算单、付款结算单、划账结算单、对外收款结算单、对外付款结算单
////	 *            在审核点维护账户档案的当前余额字段且进行账户赤字控制。 如果没有与单据币种相同的账户则审核失败 N
////	 *            账户余额≤账户赤字额度时为合法. N 账户余额>账户赤字额度时取账户档案的账户赤字控制方式： J
////	 *            控制方式为提示信息时，则出提示信息，仍可审核。 J 控制方式为限制操作时，则出提示信息且不能进行审核。 J
////	 *            控制方式为授权管理时，判断当前登录人是否是账户赤字授权控制人，是则出提示信息且能够审核；否则出提示信息且不能审核。
////	 *
////	 * 账户当前余额 借方-,贷方+
////	 * res.intvalue=9999说明需要授权,res.intvalue=1通过授权,res.intvalue=2说明审核成功
////	 */
////	public ResMessage[] auditABills(nc.vo.ep.dj.DJZBVO[] djs)
////			throws BusinessException {
////		if (djs == null || djs.length < 1)
////			return null;
////		nc.vo.arap.global.ResMessage[] res = new nc.vo.arap.global.ResMessage[djs.length];
////		nc.bs.mw.itf.NCFlag.bSqlOutput = true;
////		nc.bs.arap.global.ArapExtInfRunBO extbo = new nc.bs.arap.global.ArapExtInfRunBO();
////		nc.vo.arap.global.BusiTransVO[] busitransvos = extbo.initBusiTrans(
////				"shenhe", ((DJZBHeaderVO) djs[0].getParentVO()).getPzglh());
////		for (int i = 0; i < djs.length; i++) {
////			try {
////
////				res[i] = auditABill2(djs[i], busitransvos);
////				if (res[i].isSuccess){
////				    String str = NCLangRes4VoTransl.getNCLangRes().getStrByID("2006030102","UPP2006030102-000552")/*@res "审核第{0}张单据，审核成功\n"*/ + res[i].strMessage;
////					res[i].strMessage =NCLangResOnserver.getInstance().getStrByID("2006030102","UPP2006030102-000552",null,new String[]{String.valueOf(djs[i].listIndex + 1)});
////				}else{
////				    String str = NCLangRes4VoTransl.getNCLangRes().getStrByID("2006030102","UPP2006030102-000510")/*@res "审核第{0}张单据，审核失败\n"*/ + res[i].strMessage;
////					res[i].strMessage =NCLangResOnserver.getInstance().getStrByID("2006030102","UPP2006030102-000510",null,new String[]{String.valueOf(djs[i].listIndex + 1)});
////				}
////
////			} catch (Exception e) {
////				if (res[i] == null)
////					res[i] = new ResMessage();
////				res[i].isSuccess = false;
////				if (e instanceof RemoteException
////						&& ((RemoteException) e).detail != null) {
////
////					res[i].strMessage = ((RemoteException) e).detail
////							.getMessage();
////				} else
////					res[i].strMessage = e.getMessage();
////
////			    String str = NCLangRes4VoTransl.getNCLangRes().getStrByID("2006030102","UPP2006030102-000510")/*@res "审核第{0}张单据，审核失败\n"*/ + res[i].strMessage;
////				res[i].strMessage =NCLangResOnserver.getInstance().getStrByID("2006030102","UPP2006030102-000510",null,new String[]{String.valueOf(djs[i].listIndex + 1)});
////				//+ "\n"
////				//+ e.getMessage();
////			}
////			res[i].listIndex = djs[i].listIndex;
////			res[i].vouchid = djs[i].vouchid;
////
////		}
////
////		Log.getInstance(this.getClass()).debug("批审核返回...");
////		return res;
////	}
//
//	/**
//	 * 审核单据 创建日期：(2001-9-3 17:31:51)
//	 *
//	 * @return java.lang.String[]
//	 * @param djs
//	 *            nc.vo.ep.dj.DJZBVO[]
//	 */
//	/**
//	 * modification log:2004-11-09 commented by wangqiang,ST said it is a usless
//	 * method
//	 */
//	public String[] auditBill(nc.vo.ep.dj.DJZBVO[] djs)
//			throws BusinessException {
//		String[] result = new String[djs.length];
//		//	try {
//		//		/**状态检查*/
//		//		/**预算*/
//		//		/**会计平台*/
//		//		/**打标志*/
//		//		ApplayBillDMO dm = new ApplayBillDMO();
//		//		result = dm.auditBill(djs);
//		//
//		//	}
//		//	catch (Exception e) {
//		//		Log.getInstance(this.getClass()).error(e.getMessage(),e);
//		//		throw new BusinessShowException("DefdefBO::insert(DefdefVO) Exception!",
//		// e);
//		//	}
//		//	return result;
//		return null;
//	}

	/**
	 * 此处插入方法说明。 创建日期：(2001-9-3 17:31:51)
	 *
	 * @return java.lang.String[]
	 * @param djs
	 *            nc.vo.ep.dj.DJZBVO[]
	 */
	public String[] bankBill(nc.vo.ep.dj.DJZBVO[] djs)
			throws BusinessException {
		String[] result = new String[djs.length];
		try {

		} catch (Exception e) {
			throw ExceptionHandler.handleException(this.getClass(), e);
		}
		return result;
	}

	/**
	 * 取单据号 作者：陈飞
	 *
	 * @version 最后修改日期
	 * @see 需要参见的其它类
	 * @return nc.vo.ep.dj.DJZBHeaderVO[]
	 */
	public DJZBVO beforeShenheInf(nc.vo.arap.global.BusiTransVO[] busitransvos,
			DJZBVO dJZB) throws BusinessException {

		if (busitransvos != null) {
			for (int i = 0; i < busitransvos.length; i++) {
				try {

					SystemProfile.getInstance().log("arapbillauditstart beforeshenhe 1 "+busitransvos[i].getClassName());

					long begin1=System.currentTimeMillis();
					Log.getInstance(this.getClass()).debug("调用接口"+busitransvos[i]
					.getClassName());
					((nc.bs.arap.outer.ArapPubShenheInterface) busitransvos[i]
							.getInfClass()).beforeShenheAct(dJZB);
					 long end1=System.currentTimeMillis();
					 Log.getInstance(this.getClass()).debug("结束调用接口"+busitransvos[i]
					     .getClassName()+",用时"+(end1-begin1)+"ms");

					 SystemProfile.getInstance().log("arapbillauditstart beforeshenhe 2 "+busitransvos[i].getClassName());

				}catch (ClassCastException e){

					ExceptionHandler.consume(e);
				}
				catch (Exception e) {
					ExceptionHandler.consume(e);
					String strerr = busitransvos[i].getUsesystemname()
							+ busitransvos[i].getNote() + "\n" + e.getMessage();
//					if (e instanceof RemoteException)
//						strerr = busitransvos[i].getUsesystemname()
//								+ busitransvos[i].getNote() + "\n"
//								+ ((RemoteException) e).detail.getMessage();

					throw ExceptionHandler.createException( strerr ,e);

				}
			}
		}
		return dJZB;
	}
	/**
	 * 取单据号 作者：陈飞
	 *
	 * @version 最后修改日期
	 * @see 需要参见的其它类
	 * @return nc.vo.ep.dj.DJZBHeaderVO[]
	 */
	public nc.vo.pub.ExAggregatedVO beforeEffectInf(nc.vo.arap.global.BusiTransVO[] busitransvos,
	        nc.vo.pub.ExAggregatedVO dJZB) throws BusinessException {

		if (busitransvos != null) {
			for (int i = 0; i < busitransvos.length; i++) {
				try {
					long begin1=System.currentTimeMillis();
					Log.getInstance(this.getClass()).debug("调用接口"+busitransvos[i]
					.getClassName());
					((nc.bs.arap.outer.IArapPubEffectInterface) busitransvos[i]
							.getInfClass()).beforeEffectAct(dJZB);
					 long end1=System.currentTimeMillis();
					 Log.getInstance(this.getClass()).debug("结束调用接口"+busitransvos[i]
					     .getClassName()+",用时"+(end1-begin1)+"ms");
				}catch (ClassCastException e){
					ExceptionHandler.consume(e);
				}
				catch (Exception e) {
					ExceptionHandler.consume(e);
					String strerr = busitransvos[i].getUsesystemname()
							+ busitransvos[i].getNote() + "\n" + e.getMessage();
//					if (e instanceof RemoteException)
//						strerr = busitransvos[i].getUsesystemname()
//								+ busitransvos[i].getNote() + "\n"
//								+ ((RemoteException) e).detail.getMessage();

					throw ExceptionHandler.createException( strerr,e );

				}
			}
		}
		return dJZB;
	}
	/**
	 * 取单据号 作者：陈飞
	 *
	 * @version 最后修改日期
	 * @see 需要参见的其它类
	 * @return nc.vo.ep.dj.DJZBHeaderVO[]
	 */
	public nc.vo.pub.ExAggregatedVO beforeUnEffectInf(nc.vo.arap.global.BusiTransVO[] busitransvos,
	        nc.vo.pub.ExAggregatedVO dJZB) throws BusinessException {

		if (busitransvos != null) {
			for (int i = 0; i < busitransvos.length; i++) {
				try {
					long begin1=System.currentTimeMillis();
					Log.getInstance(this.getClass()).debug("调用接口"+busitransvos[i]
					.getClassName());
					((nc.bs.arap.outer.IArapPubUnEffectInterface) busitransvos[i]
							.getInfClass()).beforeUnEffectAct(dJZB);
					 long end1=System.currentTimeMillis();
					 Log.getInstance(this.getClass()).debug("结束调用接口"+busitransvos[i]
					     .getClassName()+",用时"+(end1-begin1)+"ms");

				}catch (ClassCastException e){
					ExceptionHandler.consume(e);
				}
				catch (Exception e) {
					ExceptionHandler.consume(e);
					String strerr = busitransvos[i].getUsesystemname()
							+ busitransvos[i].getNote() + "\n" + e.getMessage();
//					if (e instanceof RemoteException)
//						strerr = busitransvos[i].getUsesystemname()
//								+ busitransvos[i].getNote() + "\n"
//								+ ((RemoteException) e).detail.getMessage();

					throw ExceptionHandler.createException( strerr ,e);

				}
			}
		}
		return dJZB;
	}
	/**
	 * 取单据号 作者：陈飞
	 *
	 * @version 最后修改日期
	 * @see 需要参见的其它类
	 * @return nc.vo.ep.dj.DJZBHeaderVO[]
	 */
	public DJZBVO beforeUnShenheInf(
			nc.vo.arap.global.BusiTransVO[] busitransvos, DJZBVO dJZB)
			throws BusinessException {

		if (busitransvos != null) {
			for (int i = 0; i < busitransvos.length; i++) {
				try {
					((nc.bs.arap.outer.ArapPubUnShenheInterface) busitransvos[i]
							.getInfClass()).beforeUnShenheAct(dJZB);
				} catch (java.lang.ClassCastException e){
					ExceptionHandler.consume(e);
				}
				catch (Exception e) {
					ExceptionHandler.consume(e);
					String strerr = busitransvos[i].getUsesystemname()
							+ busitransvos[i].getNote() + "\n" + e.getMessage();
//					if (e instanceof RemoteException)
//						strerr = busitransvos[i].getUsesystemname()
//								+ busitransvos[i].getNote() + "\n"
//								+ ((RemoteException) e).detail.getMessage();

					throw ExceptionHandler.createException( strerr,e );

				}
			}
		}
		return dJZB;
	}

	/**
	 * 此处插入方法说明。 创建日期：(2001-9-3 17:31:51)
	 *
	 * @return java.lang.String[]
	 * @param djs
	 *            nc.vo.ep.dj.DJZBVO[]
	 */
	public String[] confirmBill(nc.vo.ep.dj.DJZBVO[] djs)
			throws BusinessException {
		String[] result = new String[djs.length];
		try {

		} catch (Exception e) {
//			Log.getInstance(this.getClass()).error(e.getMessage(),e);
//			throw ExceptionHandler.createException("DefdefBO::insert(DefdefVO) Exception!",
//					e);
			throw ExceptionHandler.handleException(this.getClass(), e);
		}
		return result;
	}

	/**
	 * 作者：邱冬强
	 *
	 * @version 最后修改日期
	 * @see 需要参见的其它类
	 * @return nc.bs.arap.parterm.Payterm
	 */
//	private nc.bs.dap.out.Dap getPFA() throws Exception {
//
//		nc.bs.dap.out.DapHome daphome = (nc.bs.dap.out.DapHome) this
//				.getBeanHome(nc.bs.dap.out.DapHome.class, "nc.bs.dap.out.DapBO");
//		nc.bs.dap.out.Dap m_dap = daphome.create();
//
//		//nc.vo.arap.parterm.PaytermVO payvo = pay.findByPrimaryKey("1");
//		return m_dap;
//	}

	/**
	 * 作者：邱冬强
	 *
	 * @version 最后修改日期
	 * @see 需要参见的其它类
	 * @return nc.bs.arap.parterm.Payterm
	 */
//	private nc.bs.dmp.out2.Dmp2 getPFDMP() throws Exception {
//
//		nc.bs.dmp.out2.Dmp2Home dmphome = (nc.bs.dmp.out2.Dmp2Home) this
//				.getBeanHome(nc.bs.dmp.out2.Dmp2Home.class,
//						"nc.bs.dmp.out2.Dmp2BO");
//		nc.bs.dmp.out2.Dmp2 m_dmp = dmphome.create();
//
//		//nc.vo.arap.parterm.PaytermVO payvo = pay.findByPrimaryKey("1");
//		return m_dmp;
//	}

	/**
	 * 通过主键获得VO对象。
	 *
	 * 创建日期：(2001-8-25)
	 *
	 * @return nc.vo.ep.dj.DefdefVO
	 * @param key
	 *            String
	 * @exception BusinessException
	 *                异常说明。
	 */
	public String getPk_invclByPk(String key) throws BusinessException {

		String pk_invcl = null;
		try {
			ApplayBillDMO dmo = new ApplayBillDMO();
			pk_invcl = dmo.getPk_invclByPk(key);
		} catch (Exception e) {
			throw ExceptionHandler.handleException(this.getClass(), e);
		}
		return pk_invcl;
	}



	/**
	 * : 主要算法：单据是否做过后续操作
	 * 注意：处理标志（clbz）：-2--折扣、-1--异币种核销、0--同币种核销、1--汇兑损益、2--红票对冲、票据贴现; 3、坏账发生;
	 * 4、坏账收回; 5、并帐转出；6、并帐转入 创建日期：(2001-11-21 20:30:15)
	 *
	 * @return int
	 * @param sourceId
	 *            java.lang.String
	 */
	public Integer iBillAfter(String sourceId) throws BusinessException {
		int iRe = 0;
		try {
			//
			if (sourceId == null || sourceId.trim().length() < 2)
				return null;
			nc.bs.ep.dj.DJZBDAO dmo = new nc.bs.ep.dj.DJZBDAO();
			Integer iCon = dmo.getClbzByPkey(sourceId);
			if (iCon != null) {
				throw ExceptionHandler.createException(ArapCommonTool.getClbzMessage(iCon));
			} else {
				iRe = -999;
			}

			//
		} catch (Exception e) {
			throw ExceptionHandler.handleException(this.getClass(), e);
		}
		return new Integer(iRe);
	}

	/**
	 * 保存审核单据 2) 销售管理订单收款处理： ? 推式生成已审核状态的收款单，系统来源为销售管理。 ?
	 * 订单推式生成的收款单的单据表体行均打上特殊标志。 ? 收款单表体行的特殊标志，在收付系统不能维护，由销售管理系统维护。 ?
	 * 单据表体行有特殊标志的收款单，不能弃审和参与后续操作，如核销、并帐、坏帐收回。
	 *
	 * 创建日期：(2001-9-3 17:31:51)
	 *
	 * @return j
	 * @param dj
	 *            nc.vo.ep.dj.DJZBVO 以下是接口要设置的值 表头(head) head.lybz=3 表体(items)
	 *            items[n].pausetransact=UFboolean(true),items[n].othersysflag="销售管理"
	 *            作者：陈飞
	 */
	public void insertShenhe_sell(nc.vo.ep.dj.DJZBVO dj)
			throws BusinessException {

		try {
			DJZBBO djbo = new DJZBBO();
			dj = djbo.insert(dj);
			//auditABill(dj);
			ARAPDjAlienBill app = new ARAPDjAlienBill();
			app.auditOneBill(dj,true);

		} catch (Exception e) {
			throw ExceptionHandler.handleException(this.getClass(), e);
		}

	}

	/**
	 * 判断单据是否可审 创建日期：(produid : AR AP EP
	 *
	 * @param vo
	 *            nc.vo.ep.dj.DJZBVO
	 */
	public boolean isApplayBill(DJZBVO vo, String produid)
			throws BusinessException {
		String dwbm = vo.getParentVO().getAttributeValue("dwbm").toString();
		//String produid =vo.getParentVO().getAttributeNames("pzglh");;
		String shnd = vo.getParentVO().getAttributeValue("shkjnd").toString();
		String shrq = vo.getParentVO().getAttributeValue("shkjqj").toString();
		String[] period = null;
		try {
//			nc.bs.sm.createcorp.CreatecorpBO co = new nc.bs.sm.createcorp.CreatecorpBO();
			period =  Proxy.getICreateCorpQueryService().querySettledPeriod(dwbm, produid);

			if (period != null) {
				if (period[0] != null && period[1] != null
						&& !(period[0].trim().equals(""))
						&& !(period[1].trim().equals(""))) {
					if ((period[0] + period[1]).compareTo(shnd + shrq) >= 0) {
						throw ExceptionHandler.createException(NCLangRes4VoTransl.getNCLangRes().getStrByID("2006030102","UPP2006030102-000563")/*@res "已结帐月不能审核单据!"*/);
						//return false;
					}
				}
			}
		} catch (Exception e) {
			throw ExceptionHandler.handleException(this.getClass(),e);
		}
		return true;
	}

	/**
	 * 此处插入方法描述。 创建日期：(2002-7-11 13:25:53)
	 *
	 * @return boolean 陈飞
	 */
	public ResMessage isDistributes(DJZBVO vo) throws BusinessException {
		try {

			DJZBDAO djdmo = new DJZBDAO();
			//并发控制
			DJZBHeaderVO head = (DJZBHeaderVO) vo.getParentVO();
			ResMessage res = new ResMessage();
			res.isSuccess = true;
			res.intValue = 2;
			res.djzbvo = vo;

			if (head.getts() == null)
				return res;
            res.m_Ts = head.getts().toString();
			Log.getInstance(this.getClass()).debug("****ApplayBillBO  before isDistributes*******"+ res.m_Ts+"****************");
            //
			djdmo.distributeDjzb_cf(head.getVouchid(), head.getts().toString(),
					head.getDjdl(), head.getDjzt());
			String ts=new DJZBBO().getTsByPrimaryKey(head.getVouchid(), "arap_djzb");
			head.setTs(new UFDateTime(ts));
            return res;
		} catch (Exception e) {
			throw ExceptionHandler.handleException(this.getClass(), e);
		}

	}

	/**
	 * 此处插入方法描述。 创建日期：(2002-7-11 13:25:53)
	 *
	 * @return boolean
	 */
	public boolean isUsedFC() throws BusinessException {
		nc.vo.dmp.coststart.StarttableVO vo = Proxy.getIPFManaCache()
				.getCostStartInfo(InvocationInfoProxy.getInstance().getUserDataSource(),
						nc.vo.dmp.outinteface.IManageAccount.PRODUCTBILL,
						new nc.vo.pub.lang.UFBoolean("N"));
		if (vo == null || vo.getIsStart() == null) {
			return false;
		}
		if (vo.getIsStart().booleanValue()) {
			return true;
		}

		return false;
	}

	/**
	 * 此处插入方法描述。 创建日期：(2002-7-11 13:25:53)
	 *
	 * @return boolean
	 */
	public boolean isUsedHC() throws BusinessException {

		nc.vo.dmp.coststart.StarttableVO vo = Proxy.getIPFManaCache()
				.getCostStartInfo(InvocationInfoProxy.getInstance().getUserDataSource(),
						nc.vo.dmp.outinteface.IManageAccount.PEOPLEBILL,
						new nc.vo.pub.lang.UFBoolean("N"));
		if (vo == null || vo.getIsStart() == null) {
			return false;
		}
		if (vo.getIsStart().booleanValue()) {
			return true;
		}
		return false;
	}

	/**
	 * 此处插入方法描述。 创建日期：(2002-7-11 13:25:53)
	 *
	 * @return boolean
	 */
//	public boolean lockBill(DJZBVO vo) throws BusinessException {
//		try {
////			KeyLock lock = new KeyLock();
//			if (KeyLock.lockKey(vo.getParentVO().getPrimaryKey(), "123", null)) {
//			} else {
//				throw new nc.vo.pub.BusinessException(NCLangRes4VoTransl.getNCLangRes().getStrByID("2006030102","UPP2006030102-000564")/*@res "单据正在被处理，请重新查询。"*/);
//			}
//
//		} catch (Exception e) {
//			throw new BusinessShowException(  e.getMessage());
//		}
//
//		return false;
//	}

	/**
	 * 确认单据 创建日期：(2001-9-3 17:31:51)
	 *
	 * @return java.lang.String[]
	 * @param djs
	 *            nc.vo.ep.dj.DJZBVO[]
	 */
	public String qr(nc.vo.ep.dj.DJZBVO dj) throws BusinessException {
		String result = "";
		try {
			/** 状态检查 */

			/** 打标志 */
			ApplayBillDMO dm = new ApplayBillDMO();
			dm.qr((nc.vo.ep.dj.DJZBHeaderVO) dj.getParentVO());
			new LockDJAction().lockDJ(dj);
		} catch (Exception e) {
			throw ExceptionHandler.handleException(this.getClass(), e);

		}
		return result;
	}

	/**
	 * 传会计平台 创建日期：(2001-9-3 17:31:51)
	 *
	 * @return java.lang.String[]
	 * @param djs
	 *            nc.vo.ep.dj.DJZBVO[]
	 *
	 */
	public void sendMessage(nc.vo.ep.dj.DJZBVO djzbvo)
			throws BusinessException {
//		if(true)return ;
//		DJZBVO dj=(DJZBVO)djzbvo.clone();
		//twei 修改成传原始vo
		DJZBVO dj=djzbvo;
		ArapDjBsCheckerBO checkerBO = new ArapDjBsCheckerBO();
		try {
			checkerBO.supplementAllInfos(new DJZBVO[] {dj});
		} catch (Exception e1) {
			throw ExceptionHandler.handleException(this.getClass(), e1);
		}
		DJZBVO vo=(DJZBVO)djzbvo.clone();
		if(djzbvo.getTrans()!=null){
			dj= nc.bs.arap.global.SettlementVO2DJZBVOTools.getDJZBVOBySettlementVO(dj, djzbvo.getTrans().getDetailMap());
		}
		nc.vo.ep.dj.DJZBHeaderVO head = (nc.vo.ep.dj.DJZBHeaderVO) dj
				.getParentVO();
		//单据表头
		long t = System.currentTimeMillis();
//		nc.bs.dap.out.Dap dp = null;
//		try {
			if (head.getDjdl().equals("ss"))
				; //事项审批单不传会计平台
			else {
				nc.vo.pub.ExAggregatedVO djVo = new nc.vo.pub.ExAggregatedVO(vo);
			    ArapExtInfRunBO extbo = new ArapExtInfRunBO();
				BusiTransVO[] busitransvos = extbo.initBusiTrans(
						"effect", head.getPzglh());
				this.beforeEffectInf(busitransvos,djVo);
				/////**会计平台
				//初始化平台VO
				DapMsgVO PfStateVO = DjbsStatTool.getDapmsgVo(dj);
				//处理类型为审核
				PfStateVO.setMsgType(DapMsgVO.ADDMSG);
				PfStateVO.setRequestNewTranscation(false);
				//传消息给会计平台
				Logger.debug(NCLangRes4VoTransl.getNCLangRes().getStrByID("2006030102","UPP2006030102-000534")/*@res "..................向平台传数据...................."*/);
//				dp = getPFA();
//				try{
//					Proxy.getIDapSendMessage().sendMessage(PfStateVO, dj);
//				}catch(ComponentException ex){
//					Logger.debug("没有找到会计平台的ejb,不影响下面的操作");
//				}
				getFipcallfacade().sendMessage(PfStateVO, dj);

				Log.getInstance(this.getClass()).debug(NCLangRes4VoTransl.getNCLangRes().getStrByID("2006030102","UPP2006030102-000565")/*@res "@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@处理会计平台时间:"*/
								+ (System.currentTimeMillis() - t));

				t = System.currentTimeMillis();
				try{
//					getFipcallfacade().sendMessage_dmp(PfStateVO,djVo);
					this.afterEffectInf(busitransvos,djVo);
				}catch(Exception e){
					throw ExceptionHandler.handleException(this.getClass(), e);
				}
				Log.getInstance(this.getClass()).debug(NCLangRes4VoTransl.getNCLangRes().getStrByID("2006030102","UPP2006030102-000540")/*@res "@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@处理向项目传单据时间:"*/
						+ (System.currentTimeMillis() - t));

			}
	}
	public void sendMessage_del(nc.vo.ep.dj.DJZBVO dj)throws BusinessException {
		sendMessage_del(dj,UFBoolean.FALSE);
	}
	/**
	 * 传会计平台 创建日期：(2001-9-3 17:31:51)
	 *
	 * @return java.lang.String[]
	 * @param djs
	 *            nc.vo.ep.dj.DJZBVO[]
	 *
	 */
	public void sendMessage_del(nc.vo.ep.dj.DJZBVO djzbvo,UFBoolean  NewTransaction)
			throws BusinessException {
		//boolean isNewTransaction=NewTransaction.booleanValue();
		DJZBVO dj=(DJZBVO)djzbvo.clone();
		if(dj.getTrans()!=null){
			long begin1=System.currentTimeMillis();
			Log.getInstance(this.getClass()).debug("传会计平台前处理结算信息");
			dj= nc.bs.arap.global.SettlementVO2DJZBVOTools.getDJZBVOBySettlementVO(dj, djzbvo.getTrans().getDetailMap());
			long end1=System.currentTimeMillis();
			Log.getInstance(this.getClass()).debug("结束传会计平台前处理结算信息,用时"+(end1-begin1)+"ms");
		}
		nc.vo.ep.dj.DJZBHeaderVO head = (nc.vo.ep.dj.DJZBHeaderVO) dj
				.getParentVO();
//		boolean isAccCanBack = true;
//		boolean isItemCanBack = true;

		//单据表头
//		long t = System.currentTimeMillis();
//		nc.bs.dap.out.Dap dp = null;
//		nc.bs.dmp.out2.Dmp2 dp2 = null;
//		try {
			if (head.getDjdl().equals("ss"))
				; //事项审批单不传会计平台
			else {

//				dp = getPFA();
//				dp2 = getPFDMP();
				/////**会计平台
				DapMsgVO PfStateVO = DjbsStatTool.getDapmsgVo(dj);
				PfStateVO.setRequestNewTranscation(false);
				//处理类型为审核
				PfStateVO.setMsgType(DapMsgVO.DELMSG);
				//try{
//				isAccCanBack = Proxy.getIDapSendMessage().isEditBillTypeOrProc(head.getDwbm(),
//						PfStateVO.getSys(), PfStateVO.getProc(), PfStateVO
//								.getBusiType(), PfStateVO.getProcMsg());
//				isAccCanBack =getFipcallfacade().isEditBillTypeOrProc(head.getDwbm(),PfStateVO.getSys(), PfStateVO.getProc(), PfStateVO.getBusiType(), PfStateVO.getProcMsg());
//				isItemCanBack = Proxy.getIDmpSendMessage().isEditBillTypeOrProc(head.getDwbm(),
//						PfStateVO.getSys(), PfStateVO.getProc(), PfStateVO
//								.getBusiType(), PfStateVO.getProcMsg());
//				isItemCanBack = getFipcallfacade().isEditBillTypeOrProc_dmp(head.getDwbm(),PfStateVO.getSys(), PfStateVO.getProc(), PfStateVO.getBusiType(), PfStateVO.getProcMsg());
//				}catch(Exception e){
//				    isAccCanBack = false;
//				}

//				if (!isAccCanBack) {
//					throw new nc.vo.pub.BusinessException(NCLangRes4VoTransl.getNCLangRes().getStrByID("2006030102","UPP2006030102-000566")/*@res "会计凭证已审核"*/);
//				}
				ArapExtInfRunBO extbo = new ArapExtInfRunBO();
				BusiTransVO[] busitransvos = extbo.initBusiTrans(
						"uneffect", head.getPzglh());
				ExAggregatedVO djvo = new ExAggregatedVO(dj);
				if(((DJZBHeaderVO)dj.getParentVO()).getPrepay()!=null && ((DJZBHeaderVO)dj.getParentVO()).getPrepay().booleanValue() &&
				        (((DJZBHeaderVO)dj.getParentVO()).getDjdl().equals("sk")||((DJZBHeaderVO)dj.getParentVO()).getDjdl().equals("fk"))){
				    DapMsgVO pfvo = DjbsStatTool.getDapmsgVOforPrePay(dj);
					//处理类型为审核
				    pfvo.setMsgType(DapMsgVO.DELMSG);

//					isAccCanBack =getFipcallfacade().isEditBillTypeOrProc(head.getDwbm(),pfvo.getSys(), pfvo.getProc(), pfvo.getBusiType(), pfvo.getProcMsg());

//					isItemCanBack = getFipcallfacade().isEditBillTypeOrProc_dmp(head.getDwbm(),pfvo.getSys(), pfvo.getProc(), pfvo.getBusiType(), pfvo.getProcMsg());

//					if (!isAccCanBack) {
//						throw new nc.vo.pub.BusinessException(NCLangRes4VoTransl.getNCLangRes().getStrByID("2006030102","UPP2006030102-000566")/*@res "会计凭证已审核"*/);
//					}
//					if (!isItemCanBack) {
//						throw new nc.vo.pub.BusinessException(NCLangRes4VoTransl.getNCLangRes().getStrByID("2006030102","UPP2006030102-000567")/*@res "项目单据已审核"*/);
//					}

					getFipcallfacade().sendMessage(pfvo, dj);

//					getFipcallfacade().sendMessage_dmp(pfvo,djvo);
				}
				long begin1=System.currentTimeMillis();
				Log.getInstance(this.getClass()).debug("调用反生效BEFORE接口");
				this.beforeUnEffectInf(busitransvos, djvo);
				 long end1=System.currentTimeMillis();
				 Log.getInstance(this.getClass()).debug("结束调用反生效BEFORE接口,用时"+(end1-begin1)+"ms");
				//传消息给会计平台
				 Log.getInstance(this.getClass()).debug(NCLangRes4VoTransl.getNCLangRes().getStrByID("2006030102","UPP2006030102-000534")/*@res "..................向平台传数据...................."*/);
				long begin=System.currentTimeMillis();
				Log.getInstance(this.getClass()).debug("反审核单据后传会计平台");
				getFipcallfacade().sendMessage(PfStateVO, dj);
				long end=System.currentTimeMillis();
				Log.getInstance(this.getClass()).debug("结束反审核单据后传会计平台,用时"+(end-begin)+"ms");
				//向项目传单据
				long begin2=System.currentTimeMillis();
				Log.getInstance(this.getClass()).debug("调用反生效AFTER接口");
//				getFipcallfacade().sendMessage_dmp(PfStateVO, djvo);
				 this.afterUnEffectInf(busitransvos, djvo);
				 long end2=System.currentTimeMillis();
				 Log.getInstance(this.getClass()).debug("结束调用反生效AFTER接口,用时"+(end2-begin2)+"ms");
			}
			Log.getInstance(this.getClass()).debug("sendMessage_del is over!");
	}
	public void hasVoucher (nc.vo.ep.dj.DJZBVO dj)throws  BusinessException {

	nc.vo.ep.dj.DJZBHeaderVO head = (nc.vo.ep.dj.DJZBHeaderVO) dj
			.getParentVO();
	boolean isAccCanBack = true;
		if (head.getDjdl().equals("ss"))
			; //事项审批单不传会计平台
		else {
			if(((DJZBHeaderVO)dj.getParentVO()).getPrepay()!=null && ((DJZBHeaderVO)dj.getParentVO()).getPrepay().booleanValue() &&
			        (((DJZBHeaderVO)dj.getParentVO()).getDjdl().equals("sk")||((DJZBHeaderVO)dj.getParentVO()).getDjdl().equals("fk"))){
				DapMsgVO pfvo = DjbsStatTool.getDapmsgVOforPrePay(dj);
				//处理类型为审核
			    pfvo.setMsgType(DapMsgVO.DELMSG);
				isAccCanBack =getFipcallfacade().isEditBillTypeOrProc(head.getDwbm(),pfvo.getSys(), pfvo.getProc(), pfvo.getBusiType(), pfvo.getProcMsg());
				if (!isAccCanBack) {
					throw ExceptionHandler.createException(NCLangRes4VoTransl.getNCLangRes().getStrByID("2006030102","UPP2006030102-000566")/*@res "会计凭证已审核"*/);
				}
			}
 		}
}
//	/**
//	 * 审核(或反审核)时控制账户余额
//	 *
//	 * 作者:陈飞 此处插入方法说明。 创建日期：(2001-11-28 20:34:19)
//	 *
//	 * @return nc.vo.ep.dj.ResMessage
//	 * @param dj
//	 *            nc.vo.ep.dj.DJZBVO isShenHe=true 时为审核 false为反审核
//	 */
//	public ResMessage shenhe_Sq(DJZBVO dj, boolean isShenHe)
//			throws BusinessException, ShenheException {
//
//		nc.vo.ep.dj.DJZBHeaderVO head = (nc.vo.ep.dj.DJZBHeaderVO) dj
//				.getParentVO();
//		//单据表头
//		ResMessage res = new ResMessage();
//		res.isSuccess = true;
//		res.djzbvo = dj;
//		if (head.getts() != null)
//			res.m_Ts = head.getts().toString();
//		if (head.getDjdl() == null || head.getDjdl().trim().length() < 1)
//			return res;
//		res=shenhe_Sq_All(dj,isShenHe);
//		if (head.getts() != null)
//			res.m_Ts = head.getts().toString();
//		return res;
//	}

	/**
	/**
	 *
	 * 创建日期：(2001-9-3 17:31:51)
	 *
	 * @return java.lang.String[]
	 * @param djs
	 *            nc.vo.ep.dj.DJZBVO[] 2、账户余额控制： ü
	 *            包含账户的收款单、付款单、收款结算单、付款结算单、划账结算单、对外收款结算单、对外付款结算单
	 *            在审核点维护账户档案的当前余额字段且进行账户赤字控制。 如果没有与单据币种相同的账户则审核失败 N
	 *            账户余额≤账户赤字额度时为合法. N 账户余额>账户赤字额度时取账户档案的账户赤字控制方式： J
	 *            控制方式为提示信息时，则出提示信息，仍可审核。 J 控制方式为限制操作时，则出提示信息且不能进行审核。 J
	 *            控制方式为授权管理时，判断当前登录人是否是账户赤字授权控制人，是则出提示信息且能够审核；否则出提示信息且不能审核。
	 *
	 * 账户当前余额 借方-,贷方+
	 *
	 * 注:当返回时res.intValue=9999说明需要授权
	 *
	 * 审核各种单据表体，检查每行的帐户余额与待收支金额运算后是否有效。如果是，则修改帐户金额；否则审核失败。
	 * 修改人：rocking
	 * 修改时间：2005-08-03
	 */
	private ResMessage shenhe_Sq_All(DJZBVO dj, boolean isShenHe)throws  BusinessException {
		DJZBItemVO[] items = null; //单据表体
		String pk_accid = ""; //账户主键
		String pk_currtype = ""; //单据币种
		nc.vo.bd.b120.AccidVO accidvo = null; //账户vo
		nc.vo.pub.lang.UFDouble changedValue = new nc.vo.pub.lang.UFDouble(0.00);//账户当前余额(修改后的)
		nc.vo.ep.dj.DJZBHeaderVO head = (nc.vo.ep.dj.DJZBHeaderVO) dj.getParentVO();//单据表头
		ResMessage res = new ResMessage();
		res.isSuccess = true;
		res.djzbvo = dj;
		ShenheException shenheE = new ShenheException();
		ArapBusinessException shygsqex = new ArapBusinessException();
		res.intValue = -1; //res.intValue=9999说明需要授权
		java.util.Vector<String> v = new java.util.Vector<String>();
		try {
			if (dj.getChildrenVO() == null || dj.getChildrenVO().length < 1) {
				//dj没有表体则联查单据表体
				DJZBDAO djdmo = new DJZBDAO();
				items = djdmo.findItemsForHeader(head.getVouchid());
			} else{
				items = (DJZBItemVO[]) dj.getChildrenVO();
			}
			//以下处理账户余额控制
			nc.bs.bd.b120.AccidDMO acciddmo = new nc.bs.bd.b120.AccidDMO(); //账户dmo
 			Hashtable<String, AccidVO> hAccidVO = new Hashtable<String, AccidVO>();
			for (int i = 0; i < items.length; i++) {
				res.isSuccess = true;
				res.intValue = 1;
				res.strMessage = "";
				pk_accid = items[i].getAccountid(); //账户主键
				pk_currtype = items[i].getBzbm(); //币种主键
				//单据表体没有账户-不维护
				if (pk_accid == null || pk_accid.trim().length() == 0)
					continue;
				String strHashKey = pk_accid + pk_currtype;
				if (hAccidVO.get(strHashKey) == null) {
					accidvo = acciddmo.findByPkandBz(pk_accid, pk_currtype);
					if (accidvo == null || accidvo.getPrimaryKey() == null) //是否有定义指定币种的账户
					{
						if("ws".equals(head.getDjdl())||"wf".equals(head.getDjdl()))
						{
							res.isSuccess = false;
							res.intValue = 1;
							res.strMessage = nc.bs.ml.NCLangResOnserver .getInstance().getStrByID("2006030102", "UPP2006030102-000599"); //@res 没有设置与单据币种相同币种的账户,审核失败"
							shenheE = new ShenheException(res.strMessage);
							shenheE.m_ResMessage = res;
							throw shenheE;
						}
						else
						continue;
					}
					hAccidVO.put(strHashKey, accidvo);
				} else
					accidvo = (nc.vo.bd.b120.AccidVO) hAccidVO.get(strHashKey);
				//单据审核时，判断帐户是否冻结或销户，否则单据不能审核。2003.12.09需求变动
				if (accidvo != null)
				{
					if ("1".equals(accidvo.getFrozenflag())) {
 						res.strMessage = nc.bs.ml.NCLangResOnserver .getInstance().getStrByID("2006030102", "UPP2006030102-000569");// @res"账户已经冻结,审核失败"
						res.isSuccess = false;
						res.intValue = 1;
						shenheE.m_ResMessage = res;
						shenheE.setBusiStyle(nc.vo.pub.pf.IPfRetExceptionStyle.NOTDEAL);
						throw shenheE;
					} else if ("2".equals(accidvo.getFrozenflag())) {
						res.strMessage = nc.bs.ml.NCLangResOnserver .getInstance().getStrByID("2006030102", "UPP2006030102-000570");//@res "账户已经销户,审核失败"
						res.isSuccess = false;
						res.intValue = 1;
						shenheE.m_ResMessage = res;
						throw shenheE;
					}
				}
				//下面开始检查帐户余额
				if (accidvo.getCurrmny() == null)
					accidvo.setCurrmny(ArapCommonTool.getZero());
				changedValue = items[i].getYbye();
				//计算出帐户是“加上”还是“减去”原必金额
				int shzb=isShenHe?1:-1;//审核标志
				int zfbz=getDJZFBZ(head.getDjdl(),items[i]);//支付标志
				changedValue =  accidvo.getCurrmny().add(changedValue.multiply(shzb*zfbz)) 	;
				//检查及修改账户最底限额
				if (changedValue.compareTo  (accidvo.getDeficit() == null ?  ArapCommonTool.getZero(): accidvo.getDeficit())<0
					&& changedValue.compareTo( accidvo.getCurrmny() )<0 && (accidvo.getIscont().booleanValue()))
				{
					if ("0".equals(accidvo.getContype())) //控制方式为:提示
					{
						res.strMessage =NCLangResOnserver.getInstance() .getStrByID( "2006030102", "UPP2006030102-000600", null, new String[] { String.valueOf(i+1), accidvo.getAccidname()});
						res.itemRow = i; //设置单据表体行号
						res.accidname = accidvo.getAccidname(); //设置账户名称
						res.pk_accid = accidvo.getPk_accid(); //设置账户主键
						v.addElement(res.strMessage);
					} else if ("1".equals(accidvo.getContype())) //控制方式为:"提示:("限制操作")
					{
						res.isSuccess = false;
						res.strMessage =  NCLangResOnserver.getInstance() .getStrByID( "2006030102", "UPP2006030102-000601", null, new String[] {String.valueOf(i+1), accidvo.getAccidname()});
						res.itemRow = i; //设置单据表体行号
						res.accidname = accidvo.getAccidname(); //设置账户名称
						res.pk_accid = accidvo.getPk_accid(); //设置账户主键
						shygsqex = new ArapBusinessException(res.strMessage);
						shygsqex.m_ResMessage = res;
						throw shygsqex;
					} else //控制方式为:授权
					{
						if (items[i].getIsSqed().booleanValue()) {
							res.strMessage =  NCLangResOnserver.getInstance() .getStrByID( "2006030102", "UPP2006030102-000602", null, new String[] {String.valueOf(i+1), accidvo.getAccidname()});//@res*"已经授权:账户余额超预算"
						} else {
							if (items[i].getSqflag().intValue() == 0) {
								res.isSuccess = false;
								res.strMessage = NCLangResOnserver .getInstance().getStrByID( "2006030102", "UPP2006030102-000582", null, new String[] { String .valueOf(i + 1) });//@res"请授权::表体第{0}行,账户余额超预算"
								res.intValue = nc.vo.bd.global.ResMessage.$REQUEST_SQ; //需要授权
								res.itemRow = i; //设置单据表体行号
								res.accidname = accidvo.getAccidname(); //设置账户名称
								res.pk_accid = accidvo.getPk_accid(); //设置账户主键
								shenheE = new ShenheException(res.strMessage);
								shenheE.setBusiStyle(nc.vo.pub.pf.IPfRetExceptionStyle.DEAL);
								shenheE.m_ResMessage = res;
								throw shenheE;
							} else if (items[i].getSqflag().intValue() == -nc.vo.bd.global.ResMessage.$CANCEL_SQ) {
								res.isSuccess = false;
								res.strMessage = NCLangResOnserver .getInstance() .getStrByID( "2006030102", "UPP2006030102-000583", null, new String[] { String.valueOf(i + 1), accidvo.getAccidname() });//@res"错误:表体第{0}行账户({1})用户取消授权"
								res.itemRow = i; //设置单据表体行号
								res.accidname = accidvo.getAccidname(); //设置账户名称
								res.pk_accid = accidvo.getPk_accid(); //设置账户主键
								res.intValue = nc.vo.bd.global.ResMessage.$CANCEL_SQ;
								shygsqex = new ArapBusinessException( res.strMessage);
								shygsqex.m_ResMessage = res;
								throw shygsqex;

							} else {
								res.isSuccess = false;
								res.strMessage = NCLangResOnserver .getInstance() .getStrByID( "2006030102", "UPP2006030102-000584", null, new String[] { String.valueOf(i + 1), accidvo.getAccidname() });// @res"错误:表体第{0}行账户({1})用户授权失败"
								res.itemRow = i; //设置单据表体行号
								res.accidname = accidvo.getAccidname(); //设置账户名称
								res.pk_accid = accidvo.getPk_accid(); //设置账户主键
								res.intValue = nc.vo.bd.global.ResMessage.$FALSE_SQ;
								shygsqex = new ArapBusinessException( res.strMessage);
								shygsqex.m_ResMessage = res;
								throw shygsqex;
							}
						}
					}
				}
				//检查及修改账户最高限额
				if (accidvo.getHighmnyiscon().booleanValue()) {
					if (changedValue.compareTo(accidvo.getHighmny() == null ? ArapCommonTool.getZero() : accidvo.getHighmny())>0
							&& changedValue .compareTo( accidvo .getCurrmny())>0 && ( accidvo.getHighmnyiscon().booleanValue()))
					//账户余额超预算
					{
						if ("0".equals(accidvo.getHighmnycontype())) //控制方式为:提示
						{
							res.strMessage =  NCLangResOnserver.getInstance() .getStrByID( "2006030102", "UPP2006030102-000585",
									null, new String[] {  String.valueOf(i + 1), accidvo.getAccidname() }); // @res提示:表体第{0}行 账户({1})余额超最高限额"
							res.itemRow = i; //设置单据表体行号
							res.accidname = accidvo.getAccidname(); //设置账户名称
							res.pk_accid = accidvo.getPk_accid(); //设置账户主键
							v.addElement(res.strMessage);
						} else if ("1".equals(accidvo.getHighmnycontype())) //控制方式为:限制操作
						{
							res.isSuccess = false;
							res.strMessage =NCLangResOnserver.getInstance() .getStrByID( "2006030102", "UPP2006030102-000586",
									null, new String[] { String.valueOf(i + 1), accidvo.getAccidname() });//@res"错误:表体第{0}行 账户({1})余额超最高限额"
							res.itemRow = i; //设置单据表体行号
							res.accidname = accidvo.getAccidname(); //设置账户名称
							res.pk_accid = accidvo.getPk_accid(); //设置账户主键
							shygsqex = new ArapBusinessException(res.strMessage);
							shygsqex.m_ResMessage = res;
							throw shygsqex;
						} else //控制方式为:授权
						{
							if (items[i].getIsSqed().booleanValue()) {
								res.strMessage =  NCLangResOnserver .getInstance() .getStrByID( "2006030102", "UPP2006030102-000587",
										null, new String[] { String.valueOf(i + 1), accidvo.getAccidname() });//@res"已经授权:表体第{0}行账户({1})余额超最高限额"
							} else {
								if (items[i].getSqflag().intValue() == 0) {
									res.isSuccess = false;
									res.strMessage =  NCLangResOnserver .getInstance() .getStrByID( "2006030102", "UPP2006030102-000588",
											null, new String[] { String .valueOf(i + 1), accidvo .getAccidname() });//@res"请授权:表体第{0}行账户({1})余额超最高限额"
									res.intValue = nc.vo.bd.global.ResMessage.$REQUEST_SQ; //需要授权
									res.itemRow = i; //设置单据表体行号
									res.accidname = accidvo.getAccidname(); //设置账户名称
									res.pk_accid = accidvo.getPk_accid(); //设置账户主键
									shenheE = new ShenheException(res.strMessage);
									shenheE .setBusiStyle(nc.vo.pub.pf.IPfRetExceptionStyle.DEAL);
									shenheE.m_ResMessage = res;
									throw shenheE;
								} else if (items[i].getSqflag().intValue() == -nc.vo.bd.global.ResMessage.$CANCEL_SQ) {
									res.isSuccess = false;
									res.strMessage =  NCLangResOnserver .getInstance() .getStrByID( "2006030102", "UPP2006030102-000583",
											null, new String[] { String .valueOf(i + 1), accidvo .getAccidname() });//@res"错误:表体第{0}行账户({1})用户取消授权"
									res.itemRow = i; //设置单据表体行号
									res.accidname = accidvo.getAccidname(); //设置账户名称
									res.pk_accid = accidvo.getPk_accid(); //设置账户主键
									res.intValue = nc.vo.bd.global.ResMessage.$CANCEL_SQ;
									shygsqex = new ArapBusinessException(
											res.strMessage);
									shygsqex.m_ResMessage = res;
									throw shygsqex;

								} else {
									res.isSuccess = false;
									res.strMessage = NCLangResOnserver .getInstance() .getStrByID( "2006030102", "UPP2006030102-000584",
											null, new String[] { String .valueOf(i + 1), accidvo .getAccidname() });//@res"错误:表体第{0}行账户({1})用户授权失败"
									res.itemRow = i; //设置单据表体行号
									res.accidname = accidvo.getAccidname(); //设置账户名称
									res.pk_accid = accidvo.getPk_accid(); //设置账户主键
									res.intValue = nc.vo.bd.global.ResMessage.$FALSE_SQ;
									shygsqex = new ArapBusinessException(res.strMessage);
									shygsqex.m_ResMessage = res;
									throw shygsqex;
								}
							}
						}
					}
				}
				//修改账户表中的当前余额
				accidvo.setCurrmny(changedValue);
			}
 			Enumeration voKeys = hAccidVO.keys();
			while (voKeys.hasMoreElements()) {
				String nextKey = (String) voKeys.nextElement();
				accidvo = (nc.vo.bd.b120.AccidVO) hAccidVO.get(nextKey);
				acciddmo.update_curYe(accidvo);
			}
 			//以上处理账户余额控制
		} catch (Exception e) {
			throw ExceptionHandler.handleException(this.getClass(), e);
		}
		res.strMessage = "";
		if (v != null && v.size() > 0)
			res.strMessage = v.elementAt(0).toString();
		return res;
	}
	/**
	 *根据单据类型返回支付标志：1-收 ；2-支
	 *创建人：rocking
	 *创建日期：2005-8-4
	 */
	private int getDJZFBZ(String djdl,DJZBItemVO item)
	{
		if ("sk".equals(djdl))return 1; //收款单 +
		else if ( "fk".equals(djdl))return -1; //付款单 -
		else if ("sj".equals(djdl))return 1; //收款结算单+
		else if ("fj".equals(djdl))return -1; //付款结算单-
		else if ( "ws".equals(djdl))return 1; //对外收款结算单+
		else if ( "wf".equals(djdl))return -1; //对外付款结算单-
		else if ( "hj".equals(djdl)) //划账结算单
		{
			if(item.getFx().intValue()==1)return -1;
			else return 1;
		}
		else return 0;
	}

	/**
	 * 取消审核单据 创建日期：(2001-9-3 17:31:51)
	 *
	 * @return java.lang.String[]
	 * @param djs
	 *            nc.vo.ep.dj.DJZBVO[]
	 */
	public ResMessage unAuditABill(nc.vo.ep.dj.DJZBVO dj)
			throws BusinessException {
//		String result = "";
		ResMessage res = new ResMessage();
		res.isSuccess = true;
		res.djzbvo = dj;
		res.intValue = ResMessage.$SHENHE_SUCCESS; //res.intValue=9999说明需要授权

		AdjustBillInf info = new AdjustBillInf();
		try {
			UFBoolean flag = info.getAdjustbill_unAudit(dj);
			if(null != flag && !flag.booleanValue()){
				throw ExceptionHandler.createException( nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("2006","UPP2006-v55-000040")/*@res "单据已经生成调整单不允许反审核，操作失败！"*/);
//				res.isSuccess = false;
//				return res;
			}
		} catch (Exception e1) {
			throw ExceptionHandler.handleException(this.getClass(), e1);
		}
		nc.vo.ep.dj.DJZBHeaderVO head = (nc.vo.ep.dj.DJZBHeaderVO) dj
				.getParentVO();
		new LockDJAction().lockDJ(dj);

		DJZBDAO djdmo = null;
		try {
			djdmo = new DJZBDAO();
		} catch (Exception e) {
			throw ExceptionHandler.handleException(this.getClass(), e);
		}
		DJZBItemVO[] items = (DJZBItemVO[]) dj.getChildrenVO(); //单据表体
		//cf2002-01-14 add
		//查询单据表体
		if (items == null) {
			try {

				if (head.getPzglh().intValue() == 3
						|| head.getDjdl().equals("ss"))
					items = djdmo.findItemsForHeader_SS(head.getPrimaryKey());
				else
					items = djdmo.findItemsForHeader(head.getPrimaryKey());
			} catch (Exception e) {
				res.strMessage = NCLangRes4VoTransl.getNCLangRes().getStrByID("2006030102","UPP2006030102-000148")/*@res "联查单据表体失败,审核失败"*/;
				res.isSuccess = false;
				return res;
			}
			dj.setChildrenVO(items);
		}
		//cf2002-01-14 add

		try {
			dj.setParam_Ext_Save();
		} catch (Exception e) {
			throw ExceptionHandler.handleException(this.getClass(), e);
		}

		res.djzbvo = dj;
		List<String> ids=new ArrayList<String>();
			for (int i = 0; i < items.length; i++) {
				ids.add(items[i].getFb_oid());
				if (items[i].getPausetransact() != null
						&& items[i].getPausetransact().booleanValue()) {
					throw ExceptionHandler.createException(NCLangRes4VoTransl.getNCLangRes().getStrByID("2006030102","UPP2006030102-000609")/*@res "单据已经挂起,不能反审核"*/);
				}
			}
		try {
		    //		  Update by Songtao 2005-03-23 将反审核检查添加到方法中
			ArapDjBsCheckerBO checkbo = new ArapDjBsCheckerBO();
			ResMessage tempres = checkbo.checkUnApproveBill(dj);
//			String ts=new DJZBBO().getTsByPrimaryKey(head.getVouchid(), "arap_djzb");
//			Log.getInstance(this.getClass()).debug("****ApplayBillBO2 查询数据库中的ts*******"+ ts+"****************");

			if(tempres!=null && tempres.isSuccess==false){
			    throw ExceptionHandler.createException(tempres.strMessage);
			}
//			外接口反审核单据前动作
			nc.bs.arap.global.ArapExtInfRunBO extbo = new nc.bs.arap.global.ArapExtInfRunBO();
			nc.vo.arap.global.BusiTransVO[] busitransvos = extbo.initBusiTrans(
					"unshenhe", head.getPzglh());
			this.undoBusi(dj, busitransvos);
			this.uneffect(dj);
			res.m_Ts=head.getts().toString();
		} catch (Exception e) {
			throw ExceptionHandler.handleException(this.getClass(), e);
		}
		return res;
	}
	public ResMessage unshenheBack(nc.vo.ep.dj.DJZBVO dj,
			nc.vo.arap.global.BusiTransVO[] busitransvos,UFBoolean canWorkFlow)
			throws BusinessException {
		ResMessage res = new ResMessage();
		res.isSuccess = true;
		res.djzbvo = dj;
		res.intValue = -1; //res.intValue=9999说明需要授权

		nc.vo.ep.dj.DJZBHeaderVO head = (nc.vo.ep.dj.DJZBHeaderVO) dj
				.getParentVO();
		if (head.getSpzt() != null && head.getSpzt().trim().equals(DJZBVOConsts.m_strStatusVerifying)) {
			//res.intValue = ResMessage.$SHENHE_SUCCESS;
			// return res;
			res.isSuccess = false;
			res.strMessage = NCLangRes4VoTransl.getNCLangRes().getStrByID("2006030102","UPP2006030102-000611")/*@res "审批中单据不能后台反审核."*/;
			return res;
		}

			int iStatus = Proxy.getIPFWorkflowQry().queryWorkflowStatus(
							head.getXslxbm() == null
									|| head.getXslxbm().trim().length() < 1 ? "KHHH0000000000000001"
									: head.getXslxbm().trim(), head.getDjlxbm(),
							head.getVouchid());
			if (iStatus == nc.vo.pub.pf.IWorkFlowStatus.BILLTYPE_NO_WORKFLOW
					|| iStatus == nc.vo.pub.pf.IWorkFlowStatus.BILL_NOT_IN_WORKFLOW||canWorkFlow.booleanValue())
				;
			else {
				/** 该单据类型没有配置工作流 */
				res.isSuccess = false;
				res.strMessage = NCLangRes4VoTransl.getNCLangRes().getStrByID("2006030102","UPP2006030102-000612")/*@res "审批流单据不能后台反审核."*/;
				return res;

			}
			new LockDJAction().lockDJ(dj);
			head.setDjzt(DJZBVOConsts.m_intDJStatus_Saved);
			head.setSpzt(null);
			try {
				DJZBHeaderVO chead=(DJZBHeaderVO)head.clone();
				chead.setShrq(null);
				chead.setShr(null);
				chead.setShkjnd(null);
				chead.setShkjqj(null);
				dao.updateShenheBack(chead);
				head.setTs(chead.getts());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				throw ExceptionHandler.handleException(e);
			}
			if(!DJZBVOConsts.ACT_DELEFF_BILL.equals(head.getTransientFlag())){
				new ArapInvokeSettlement ().invokeCmp(dj,head.getDjrq());
			}
			undoBusi(dj,busitransvos);
			uneffect(dj);
			res.djzbvo=dj;
			res.m_Ts=((DJZBHeaderVO)dj.getParentVO()).getts().toString();
			return res;
	}
	/**
	 * 取消审核单据 创建日期：(2001-9-3 17:31:51)
	 *
	 * @return java.lang.String[]
	 * @param djs
	 *            nc.vo.ep.dj.DJZBVO[]
	 */
	public ResMessage undoBusi(nc.vo.ep.dj.DJZBVO dj,
			nc.vo.arap.global.BusiTransVO[] busitransvos  )
			throws BusinessException {
//		String result = "";
		ResMessage res = new ResMessage();
		res.isSuccess = true;
		DJZBBO curdjzbbo = new DJZBBO();
		res.djzbvo = dj;
		res.intValue = -1; //res.intValue=9999说明需要授权

		AdjustBillInf info = new AdjustBillInf();
		try {
			UFBoolean flag = info.getAdjustbill_unAudit(dj);
			if(null != flag && !flag.booleanValue()){
				throw ExceptionHandler.createException( nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("2006","UPP2006-v55-000040")/*@res "单据已经生成调整单不允许反审核，操作失败！"*/);
			}
		} catch (Exception e1) {
			throw ExceptionHandler.handleException(this.getClass(), e1);
		}
		nc.vo.ep.dj.DJZBHeaderVO head = (nc.vo.ep.dj.DJZBHeaderVO) dj
				.getParentVO();
		try {
			dj.setParam_Ext_Save();
			DjVOTreaterAid.supplementXTFlag(dj);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
		}
		DJZBDAO djdmo = null;

		djdmo = new DJZBDAO();

		DJZBItemVO[] items = (DJZBItemVO[]) dj.getChildrenVO(); //单据表体
		//cf2002-01-14 add
		//查询单据表体
		if (items == null) {
			try {

				if (head.getPzglh().intValue() == 3
						|| head.getDjdl().equals("ss"))
					items = djdmo.findItemsForHeader_SS(head.getPrimaryKey());
				else
					items = djdmo.findItemsForHeader(head.getPrimaryKey());
			} catch (Exception e) {
				res.strMessage = NCLangRes4VoTransl.getNCLangRes().getStrByID("2006030102","UPP2006030102-000148")/*@res "联查单据表体失败,审核失败"*/;
				res.isSuccess = false;
				return res;
			}
			dj.setChildrenVO(items);
		}
		//cf2002-01-14 add

		//cf2002-06-06 add for 存货分类主键
//		String pk_invcl = null;
//		for (int j = 0; j < items.length; j++) {
//			if (items[j].getChbm_cl() != null
//					&& items[j].getChbm_cl().trim().length() > 1) {
//				if (items[j].getPk_invcl() == null
//						|| items[j].getPk_invcl().trim().length() < 1) {
//					pk_invcl = getPk_invclByPk(items[j].getCinventoryid());
//					items[j].setPk_invcl(pk_invcl);
//
//				}
//			}
//		}
		//cf2002-06-06 end

//		try {
//			dj.setParam_Ext_Save();
//		} catch (Exception e) {
//			Log.getInstance(this.getClass()).error(e.getMessage(),e);
//			throw new BusinessShowException(
//					"nc.bs.ep.dj.ApplayBillBO.unAuditABill(DJZBVO) setParam_Ext_Save:"
//							+ e);
//		}

		res.djzbvo = dj;
		for (int i = 0; i < items.length; i++) {
			if (items[i].getPausetransact() != null
					&& items[i].getPausetransact().booleanValue()) {
				//res.strMessage="单据已经挂起,不能反审核";
				//res.isSuccess=false;
				//return res;
				throw ExceptionHandler.createException(NCLangRes4VoTransl.getNCLangRes().getStrByID("2006030102","UPP2006030102-000609")/*@res "单据已经挂起,不能反审核"*/);
			}
		}
        ArapDjBsCheckerBO bocheck = new ArapDjBsCheckerBO();
        ResMessage res1;
		try {
			long begin=System.currentTimeMillis();
			Log.getInstance(this.getClass()).debug("反审核单据前校验");
			res1 = bocheck.checkUnApproveBill(dj);
//			String ts=new DJZBBO().getTsByPrimaryKey(head.getVouchid(), "arap_djzb");
//			Log.getInstance(this.getClass()).debug("****ApplayBillDMO 查询数据库中的ts*******"+ ts+"****************");
			long end=System.currentTimeMillis();
			Log.getInstance(this.getClass()).debug("结束反审核单据前校验,用时"+(end-begin)+"ms");
			 if(res1!=null && res1.isSuccess==false)
		            throw ExceptionHandler.createException(res1.strMessage);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			 throw ExceptionHandler.createException(e1.getMessage());
		}
       head.setYhqrkjnd(null);
       head.setYhqrkjqj(null);
       head.setShkjnd(null);
       head.setShkjqj(null);
		List<DJZBItemVO> lst=new ArrayList<DJZBItemVO>();
		List<String> ids=new ArrayList<String>();
		for (int i = 0; i < items.length; i++) {
			  if(DJZBVOConsts.BODY_PAY_FAILURE .equals(items[i].getPayflag()) ){
			        items[i].setPayflag(null);
			        lst.add(items[i]);
			    }
			  ids.add(items[i].getFb_oid());
		}
		//外接口反审核单据前动作
		long t1 = System.currentTimeMillis();
		long begin=System.currentTimeMillis();
		Log.getInstance(this.getClass()).debug("反审核单据前BEFORE接口");
		beforeUnShenheInf(busitransvos, dj);
//		String ts=new DJZBBO().getTsByPrimaryKey(head.getVouchid(), "arap_djzb");
//		Log.getInstance(this.getClass()).debug("****ApplayBillDMO 查询数据库中的ts*******"+ ts+"****************");
		long end=System.currentTimeMillis();
		Log.getInstance(this.getClass()).debug("结束反审核单据前BEFORE接口,用时"+(end-begin)+"ms");
		Log.getInstance(this.getClass()).debug("外接口反审核单据前动作前所用时间:"
				+ (System.currentTimeMillis() - t1));

		try {
			dj.setParam_Ext_Save();
			  if(lst.size()>0){
				    djdmo.wszzFB((DJZBItemVO[])lst.toArray(new DJZBItemVO[]{}));
				}
			if (head.getSsflag().equals("1") ) {
				/** **********************************以下开始事项审批控制 */
				new LockBillItem().lock_item_bill(items, head.getLrr(), 1);
				//itemconfigbo.checkApprove((DJZBVO) dj.clone(), "审核反审核");
				Proxy.getIItemConfigPrivatee().unShenHe((DJZBVO) dj.clone(),false);

				/** ******************************************以上事项审批控制* */
//				String tss=new DJZBBO().getTsByPrimaryKey(head.getVouchid(), "arap_djzb");
//				Log.getInstance(this.getClass()).debug("****ApplayBillDMO 查询数据库中的tss*******"+ tss+"****************");

			}

			res.intValue = 1; //通过授权控制

			//以上处理账户余额控制

			/** **********************协同单据*********************** */

			if (head.getXtflag() != null && head.getXtflag().equals("审核或签字确认")) { /*-=notranslate=-*/
				//删除协同单据
				curdjzbbo.deleteXTBill(head.getVouchid());
			}
			/** *********************协同单据*********************** */
//			String tss=new DJZBBO().getTsByPrimaryKey(head.getVouchid(), "arap_djzb");
//			Log.getInstance(this.getClass()).debug("****ApplayBillDMO 查询数据库中的ts*******"+ tss+"****************");

			//外接口审核单据后动作
			long t2 = System.currentTimeMillis();
			afterUnShenheInf(busitransvos, dj);
			Log.getInstance(this.getClass()).debug("外接口反审核单据前动作后所用时间:"
					+ (System.currentTimeMillis() - t2));
			if (dj.m_Resmessage != null) {
				res.strMessage = res.strMessage + dj.m_Resmessage.strMessage;
				res.isSuccess = dj.m_Resmessage.isSuccess;
			}
			res.intValue = 2; //通过最后反审核
			res.m_Ts = head.getts().toString();
			head.setShrq(null);
			//
		} catch (Exception e) {

			throw ExceptionHandler.handleException(this.getClass(), e);
		}
		return res;
	}


	/**
	 * 此处插入方法说明。 创建日期：(2001-9-3 17:31:51)
	 *
	 * @return java.lang.String[]
	 * @param djs
	 *            nc.vo.ep.dj.DJZBVO[]
	 */
	public String[] unBankBill(nc.vo.ep.dj.DJZBVO[] djs)
			throws BusinessException {
		String[] result = new String[djs.length];
		try {

		} catch (Exception e) {
//			Log.getInstance(this.getClass()).error(e.getMessage(),e);
//			throw ExceptionHandler.createException("DefdefBO::insert(DefdefVO) Exception!",
//					e);
			throw ExceptionHandler.handleException(this.getClass(), e);
		}
		return result;
	}

	/**
	 * 此处插入方法说明。 创建日期：(2001-9-3 17:31:51)
	 *
	 * @return java.lang.String[]
	 * @param djs
	 *            nc.vo.ep.dj.DJZBVO[]
	 */
	public String[] unConfirmBill(nc.vo.ep.dj.DJZBVO[] djs)
			throws BusinessException {
		String[] result = new String[djs.length];
		try {

		} catch (Exception e) {
			throw ExceptionHandler.handleException(this.getClass(), e);
		}
		return result;
	}

	public DJZBVO befUnQr(nc.vo.ep.dj.DJZBVO dj)throws  BusinessException{

				/** 状态检查 */
				nc.vo.ep.dj.DJZBHeaderVO head = (nc.vo.ep.dj.DJZBHeaderVO) dj
						.getParentVO();
				DJZBItemVO[] items = (DJZBItemVO[])dj.getChildrenVO();
				List<String> fbpks=new ArrayList<String>();
				for(DJZBItemVO item:items){
					if(null!=item.getDdhh()&&item.getDdhh().length()>0)
						fbpks.add(item.getDdhh());
				}
		        //外接口新增单据前动作
				DJZBBO djzbbo = new DJZBBO();
		        long t1 = System.currentTimeMillis();
		        nc.bs.arap.global.ArapExtInfRunBO extbo =
		            new nc.bs.arap.global.ArapExtInfRunBO();
		        nc.vo.arap.global.BusiTransVO[] busitransvos =
		            extbo.initBusiTrans("del", head.getPzglh());
		        djzbbo.beforeDelInf(busitransvos, dj);
		        nc.bs.logging.Log.getInstance(this.getClass()).warn("del外接口修改单据前动作前所用时间:" + (System.currentTimeMillis() - t1));
		        Proxy.getIWorkflowMachine().deleteCheckFlow(head.getDjlxbm(), head.getVouchid(), head.getShr(), true);
		        /** 打标志 */
				//并发控制
				DJZBDAO djdmo = new DJZBDAO();
				try {
					djdmo.updateXtFlag(fbpks, null);
                    djdmo.distributeDjzb_Item(head.getVouchid(), head.getts()
                    		.toString(), head.getDjdl());
    	            new LockBillItem().lock_item_bill(items, head.getLrr(), 1);
    	            Proxy.getIItemConfigPrivatee().unShenHe((DJZBVO) dj.clone(),false);
//    	            djzbbo.freeLock_item_bill(items, head.getLrr(), 1);
                } catch ( Exception e) {
                  throw  ExceptionHandler.handleException(this.getClass(), e);
                }

	            //事项审批单据余额维护

	            return dj;
			}
		/**
		 * 取消确认单据 创建日期：(2001-9-3 17:31:51)
		 *
		 * @return java.lang.String[]
		 * @param djs
		 *            nc.vo.ep.dj.DJZBVO[]
		 */
		public ResMessage unQr(nc.vo.ep.dj.DJZBVO dj)
				throws BusinessException {

		    try{
		    	dj=befUnQr(dj);
		    	nc.vo.ep.dj.DJZBHeaderVO head = (nc.vo.ep.dj.DJZBHeaderVO) dj
				.getParentVO();
				DJZBBO djbo = new DJZBBO();
				new LockDJAction().lockDJ(dj);
				ApplayBillDMO dm = new ApplayBillDMO();
				dm.unQr(head);
				djbo.returnBillCode(dj,false);
				head.setDjzt(DJZBVOConsts.m_intDJStatus_Unconfirmed);
				new ArapInvokeSettlement().invokeCmp(dj, head.getDjrq());
				return afterUnQr(dj);
				//
			} catch (Exception e) {
			  	throw ExceptionHandler.handleException(this.getClass(), e);

			}
		}
		public ResMessage afterUnQr(nc.vo.ep.dj.DJZBVO dj) throws  BusinessException{
		    ResMessage res = new ResMessage();
			res.isSuccess = true;
			DJZBDAO djdmo = new DJZBDAO();
		    nc.vo.ep.dj.DJZBHeaderVO head = (nc.vo.ep.dj.DJZBHeaderVO) dj
			.getParentVO();
		    DJZBBO djzbbo = new DJZBBO();
//		  /删除协同单据 add by rocking in 2005-10-27 for nc31sp1
			 if (head.getXtflag() != null && head.getXtflag().equals("保存")) { /*-=notranslate=-*/
			 	nc.bs.ep.dj.DJZBBO zbbo=new nc.bs.ep.dj.DJZBBO();
	           //删除协同单据
			 	List<String> ids=new ArrayList<String>();
			 for(DJZBItemVO item:(DJZBItemVO[])dj.getChildrenVO()){
				 ids.add(item.getFb_oid());
			 }
			 zbbo.deleteOutBill(head.getVouchid());
			 }
	       nc.bs.arap.global.ArapExtInfRunBO extbo =
	           new nc.bs.arap.global.ArapExtInfRunBO();
	       nc.vo.arap.global.BusiTransVO[] busitransvos =
	           extbo.initBusiTrans("del", head.getPzglh());
			djzbbo.afterDelInf(busitransvos, dj);
//			同步ts
			String tablename = "arap_djzb";
			if (head.getPzglh().intValue() == 3 || head.getDjdl().equals("ss"))
				tablename = "arap_item";
		       nc.bs.logging.Log.getInstance(this.getClass()).warn("del外接口修改单据前动作后所用时间:"  );

			try {
                res.m_Ts = djdmo.getTsByPrimaryKey(head.getVouchid(), tablename);
            } catch ( Exception e) {
            	throw ExceptionHandler.handleException(this.getClass(), e);
            }
			return res;
		}


	/**
	 * 取消签字确认单据 创建日期：(2001-9-3 17:31:51)
	 *
	 * @return java.lang.String[]
	 * @param djs
	 *            nc.vo.ep.dj.DJZBVO[]
	 */
	public ResMessage unYhqr(nc.vo.ep.dj.DJZBVO dj)
			throws BusinessException {
		ResMessage res = new ResMessage();
		try {
			/** 状态检查 */

			/** 打标志 */
			DJZBHeaderVO head = (nc.vo.ep.dj.DJZBHeaderVO) dj.getParentVO();
			new LockDJAction().lockDJ(dj);
			ApplayBillDMO dm = new ApplayBillDMO();
			dm.unYhqr(head);
			DJZBDAO djdmo = new DJZBDAO();
			res.m_Ts = djdmo.getTsByPrimaryKey(head.getVouchid(), "arap_djzb");

		} catch (Exception e) {
			throw ExceptionHandler.handleException(this.getClass(), e);
		}
		return res;
	}

	/**
	 * 签字确认单据 创建日期：(2001-9-3 17:31:51)
	 *
	 * @return java.lang.String[]
	 * @param djs
	 *            nc.vo.ep.dj.DJZBVO[]
	 */
	public ResMessage sign(nc.vo.ep.dj.DJZBVO dj)
			throws BusinessException,nc.vo.pub.BusinessException {
		ResMessage res = new ResMessage();

		res.strMessage = NCLangRes4VoTransl.getNCLangRes().getStrByID("2006030102","UPP2006030102-000616")/*@res "签字确认成功"*/;
		try {
			/** 打标志 */
			DJZBHeaderVO head = (nc.vo.ep.dj.DJZBHeaderVO) dj.getParentVO();
	    	DjVOTreaterAid.supplementXTFlag(dj);
			//whether the bill can be commissioned
	    	if(!head.getIsjszxzf().booleanValue()){
		    	Boolean flag = new ArapDjBsCheckerBO().isSettled(head.getDwbm(), head.getPzglh(), dj);
		    	if(flag){
		    		 throw ExceptionHandler.createException(NCLangRes4VoTransl.getNCLangRes().getStrByID("2006030102","UPP2006030102-000071")/*@res "本月已经结账,操作失败"*/);
		    	}
	    	}
			new LockDJAction().lockDJ(dj);
			head.setDjzt(DJZBVOConsts.m_intDJStatus_Signature);

			ApplayBillDAO dm = new ApplayBillDAO();
			dm.updateSignStatus(head);

			res.m_Ts = head.getts().toString();


		} catch (Exception e) {
			throw ExceptionHandler.handleException(this.getClass(), e);
		}
		return res;
	}
	/**
	 * 签字确认单据 创建日期：(2001-9-3 17:31:51)
	 *
	 * @return java.lang.String[]
	 * @param djs
	 *            nc.vo.ep.dj.DJZBVO[]
	 */
	public ResMessage unsign(nc.vo.ep.dj.DJZBVO dj)
			throws BusinessException,nc.vo.pub.BusinessException {
		ResMessage res = new ResMessage();

		res.strMessage = NCLangRes4VoTransl.getNCLangRes().getStrByID("2006030102","UPP2006030102-000616")/*@res "签字确认成功"*/;
		try {
			/** 打标志 */
			DJZBHeaderVO head = (nc.vo.ep.dj.DJZBHeaderVO) dj.getParentVO();
	    	DjVOTreaterAid.supplementXTFlag(dj);
			//whether the bill can be commissioned
			UFBoolean isReded = head.getIsreded()==null?new UFBoolean(false):head.getIsreded();
			if(isReded.booleanValue()){
				throw ExceptionHandler.createException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("businessbill","UPPbusinessbill-000231")/*@res "单据号为"*/+head.getDjbh()+nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("businessbill","UPPbusinessbill-000237"));
			}
			new LockDJAction().lockDJ(dj);
			if("ys".equals(head.getDjdl())||"yf".equals(head.getDjdl())||DJZBVOConsts.m_intDJStatus_Saved==head.getDjzt())
				head.setDjzt(DJZBVOConsts.m_intDJStatus_Saved);
			else
				head.setDjzt(DJZBVOConsts.m_intDJStatus_Verified);
			if(null !=head.getZzzt() && head.getZzzt()!=DJZBVOConsts.m_intDJZzzt_Succeed&&!head.getZzzt().equals(DJZBVOConsts.FTS_RECEIVER_SUCCESS)) {
				head.setZzzt(null);
				head.setPaydate(null);
				head.setPayman(null);
				Arap_djfbVOMeta fbmeta=new Arap_djfbVOMeta();
				String[] attrs = new String[]{"payman","paydate","payflag" };
			    for(DJZBItemVO item:(DJZBItemVO[])dj.getChildrenVO()){
			    	item.setPaydate(null);
			    	item.setPayman(null);
			    	item.setPayflag(null);
			    }
			    int i=new PubDAO().updateObjectPartly(((DJZBItemVO[])dj.getChildrenVO())[0],
			    		fbmeta,attrs," where vouchid ='"+head.getVouchid()+"' ");
		        if(i<=0)
					   throw ExceptionHandler.createException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("200656","UPP200656-000017")/*@res "更新单据分录支付状态失败!"*/);
			}
			ApplayBillDAO dm = new ApplayBillDAO();
			dm.updateSignStatus(head);

			res.m_Ts = head.getts().toString();


		} catch (Exception e) {
			throw ExceptionHandler.handleException(this.getClass(), e);
		}
		return res;
	}
	public FipCallFacade getFipcallfacade() {
		if(fipcallfacade==null){
			fipcallfacade = new FipCallFacade();
		}
		return fipcallfacade;
	}

}